/**
 * 
 */
package com.elastica.beatle.tests.audit;

import java.io.File;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.elastica.beatle.SFTPUtils;
import com.elastica.beatle.RestClient.Client;
import com.elastica.beatle.RestClient.ClientUtil;
import com.elastica.beatle.audit.AuditDSStatusDTO;
import com.elastica.beatle.audit.AuditFunctions;
import com.elastica.beatle.audit.AuditGoldenSetDataController;
import com.elastica.beatle.audit.AuditInitializeTests;
import com.elastica.beatle.audit.AuditTestConstants;
import com.elastica.beatle.audit.AuditTestUtils;
import com.elastica.beatle.logger.Logger;

/**
 * @author Mallesh
 *
 */
public class AuditScpDeviceLogsTests extends AuditInitializeTests{
	protected Client restClient;
	protected String sourceID = null;
	protected String fileToBeUploaded;
	protected String sftpTenantUsername;
	protected String sftpServerHost;
	protected String sftpServerDestinationDir;
	protected String fireWallType;
	protected String scpPayload;
	protected String firewallLogFilePath;
	Properties firewallLogDataProps;
	protected ArrayList<String> datasourceIdsList=new ArrayList<String>();
	protected AuditDSStatusDTO auditDSStatusDTO;
	protected ArrayList<AuditDSStatusDTO> inCompleteDsList=new ArrayList<AuditDSStatusDTO>();
	ArrayList<String> goldenSetErrorList=new ArrayList<String>();
	protected String scpcompltedCheckEmptyFilePath;
	
	
	JSONArray hardWareIds=null;
	ArrayList<String> deviceLogsData=new ArrayList<String>();
	/**
	 * @param FireWallName
	 */
	public AuditScpDeviceLogsTests(String FireWallName) {
		restClient = new Client();
		this.fireWallType = FireWallName;
	}	
	/**
	 * Prepares the payload for datasource creation
	 * set the firewall log path. 
	 * @throws Exception
	 */
	@BeforeClass(alwaysRun = true)
	public void intScpSftpData() throws Exception{

		//Download the log file from s3 and keeping temfolder
		Thread.sleep(30000);
		AuditFunctions.DownloadFileFormS3(fireWallType);
		Thread.sleep(30000);
		
		scpPayload = AuditTestUtils.createDeviceIdSCPUploadBody(fireWallType,suiteData.getEnvironmentName(),AuditTestConstants.AUDIT_SCP_DEVICE_ID_DS_NAME);
		firewallLogFilePath = AuditTestUtils.getFirewallLogFilePath(fireWallType);
		scpcompltedCheckEmptyFilePath=AuditTestUtils.getFirewallLogFilePath(AuditTestConstants.SCP_COMPLETED);
	}
	

	
	@DataProvider(name="getDeviceLogIds")
	public Object[][] getDataProvider() throws Exception
	{
		int size=deviceLogsData.size();

		Object[][] inputData = new Object[size][2];
		int j=0;

		for(String str:deviceLogsData )
		{
			inputData[j][0] = fireWallType+"_"+str;
			inputData[j][1] = str;
			j++;
		}
		Reporter.log("input data::"+inputData,true);
		return inputData;
	}

	


	@Test(priority=1)
	public void testCreateDeviceLogsUsingScpTransPort() throws IOException, Exception{						
				
		HttpResponse createResp = AuditFunctions.createDataSource(restClient,new StringEntity(scpPayload));
		Assert.assertEquals(createResp.getStatusLine().getStatusCode(), HttpStatus.SC_CREATED);
		JSONObject scpConnectionObject = new JSONObject(ClientUtil.getResponseBody(createResp));
		validateCreatedDataSource(scpConnectionObject);
		sourceID = (String) scpConnectionObject.get("id");
		sftpServerDestinationDir=(String)scpConnectionObject.get("upload_path");

		// get credentials of scp/sftp connection
		HttpResponse getScpCredentialsResponse = AuditFunctions.getCredentials(restClient);
		Assert.assertEquals(getScpCredentialsResponse.getStatusLine().getStatusCode(),HttpStatus.SC_OK);
		JSONObject scpCredentialsMetaData = (JSONObject) new JSONObject(ClientUtil.getResponseBody(getScpCredentialsResponse)).getJSONArray("objects").get(0);	
		//validatescpCrentials(scpCredentialsMetaData);
		sftpTenantUsername=(String) scpCredentialsMetaData.get("username");
		//sftpServerHost=(String) scpCredentialsMetaData.get("server");
		sftpServerHost=getScpSftpServerHost();
		

		//upload file to the datasource using scp
		SFTPUtils sftpUtils=getSftpUtilsConfiguration(sftpTenantUsername, sftpServerHost,sftpServerDestinationDir);
		FileInputStream fin;
		File file = new File(System.getProperty("user.dir")+firewallLogFilePath);
		fin = new FileInputStream(file);
		String result = "";
		result = sftpUtils.uploadFileToFTP(file.getName(), fin, true);
		Reporter.log("sftp file upload status:  " + result,true);
		
		//upload completed check
		FileInputStream fuploadCompltedCheckInputStream;
		File uploadCompletedFile = new File(System.getProperty("user.dir")+scpcompltedCheckEmptyFilePath);
		fuploadCompltedCheckInputStream = new FileInputStream(uploadCompletedFile);
		String uploadCompletedStatus = "";
		Reporter.log("******************scp upload completed started:********************",true);
		result = sftpUtils.uploadFileToFTP(uploadCompletedFile.getName(), fuploadCompltedCheckInputStream, true);
		Reporter.log("scp completed status:  " + uploadCompletedStatus,true);
		Reporter.log("******************SCP COMPLETED:********************",true);
		//upload conpleted check end
		
		Thread.sleep(10000);//wait 10 sec
		

		//verification logfile process attached to the datasource
		HttpResponse pollForStatusResponse = AuditFunctions.pollForDataSourceStatus(restClient, sourceID);
		Assert.assertEquals(pollForStatusResponse.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
		JSONObject pollRespObject = new JSONObject(ClientUtil.getResponseBody(pollForStatusResponse));
		String last_Status = pollRespObject.getString("last_status");
		long currentWaitTime = 0;
		while(("Pending Data".equals(last_Status) || "Pending Validation".equals(last_Status) || "Queued".equals(last_Status) || "Processing".equals(last_Status))&&
				currentWaitTime <= AuditTestConstants.AUDIT_PROCESSING_MAX_WAITTIME){
			Thread.sleep(AuditTestConstants.AUDIT_THREAD_WAITTIME);
			currentWaitTime += AuditTestConstants.AUDIT_THREAD_WAITTIME;
			pollForStatusResponse = AuditFunctions.pollForDataSourceStatus(restClient, sourceID);
			Assert.assertEquals(pollForStatusResponse.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
			pollRespObject = new JSONObject(ClientUtil.getResponseBody(pollForStatusResponse));
			last_Status = pollRespObject.getString("last_status");
			Reporter.log("Last Status of "+ sourceID +" is "+ last_Status, true);
			if("Completed".equals(last_Status) || "Failed".equals(last_Status))	{
				 deviceLogsData.add(sourceID);
				 hardWareIds = (JSONArray) pollRespObject.getJSONArray("logsource_ids");
				 for(int i=0; i<hardWareIds.length(); i++){
					 deviceLogsData.add((String)hardWareIds.get(i));
				 }
				 break;
			}
		}
		
		if(!"Completed".equals(last_Status) )
		{
			//call summary and will not produce any results.
		    testVerifyAuditResultsNotProducedForUnProcessedLogs(sourceID);
		    
		    pollForStatusResponse = AuditFunctions.pollForDataSourceStatus(restClient, sourceID);
			Assert.assertEquals(pollForStatusResponse.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
			pollRespObject = new JSONObject(ClientUtil.getResponseBody(pollForStatusResponse));
			
			auditDSStatusDTO= AuditTestUtils.populateInCompleteDataSources(pollRespObject);
			inCompleteDsList.add(auditDSStatusDTO);
		}
		Assert.assertTrue(currentWaitTime <= AuditTestConstants.AUDIT_PROCESSING_MAX_WAITTIME," File processing took more than SLA. Last status of this source file was "+last_Status);

	
	}
	
	public String getScpSftpServerHost()
	{
		String sftpServerHost=null;
		if(suiteData.getApiserverHostName().contains("qa-vpc")){
			sftpServerHost=AuditTestConstants.AUDIT_QAVPC_SCP_SERVER;}
        else if(suiteData.getApiserverHostName().contains("api-vip.elastica.net")){
        	sftpServerHost=AuditTestConstants.AUDIT_SCP_SERVER_PROD;
        }
        else if(suiteData.getApiserverHostName().contains("api.eu.elastica.net")){
        	sftpServerHost=AuditTestConstants.AUDIT_SCP_SERVER_CEP;
        }
        else{
        	sftpServerHost=AuditTestConstants.AUDIT_SCP_SERVER;
        }
		Reporter.log("scp/sftp serverhost: "+sftpServerHost,true);
		return sftpServerHost;
	}
	
	
	
	@Test (priority=2, dataProvider="getDeviceLogIds", dependsOnMethods="testCreateDeviceLogsUsingScpTransPort")
	public void testAuditSummaryForDeviceIds(String deviceidname,String deviceId) throws Exception
	{
		
Reporter.log("Getting summary for "+ fireWallType +" its ID is: "+sourceID,true);
		
		Reporter.log("device ids:"+deviceidname+" "+deviceId,true);
		List<NameValuePair> queryParams = null;	
		AuditGoldenSetDataController controller=null;
		JSONObject summaryObject=null;
		
		switch(fireWallType)
		{
		
		case AuditTestConstants.FIREWALL_CWS_MULTIPLE_DEVICE_IDS: {
			if(AuditTestConstants.CWS_DEVICEID_TSV_1234567890_SHEET.contains(deviceId))
			{
				summaryObject = AuditTestUtils.populateMultipleDevicesActualAuditSummary(restClient,AuditTestUtils.getAuditMultipleDeviceidsSummaryPayloadLatest(AuditTestConstants.CWS_DEVICEID_TSV_1234567890_SHEET,sourceID,deviceId,"86400","1386201600","1388793599") );
				validatePartialAuditSummaryObject(summaryObject);
				/*controller=new AuditGoldenSetDataController(AuditTestConstants.CWS_DEVICEID_TSV_1234567890_SHEET);
				goldenSetErrorList=new ArrayList<String>();
				goldenSetErrorList=AuditTestUtils.auditSummaryGoldenSetData(controller.readExcelFileDataAndPrepareGoldenSetData(), summaryObject,fireWallType+":"+AuditTestConstants.CWS_DEVICEID_TSV_1234567890_SHEET,goldenSetErrorList);
				Assert.assertTrue(goldenSetErrorList.isEmpty(),AuditTestConstants.CWS_DEVICEID_TSV_1234567890_SHEET+" Summary Results are wrong");
			*/}
			else if(AuditTestConstants.CWS_DEVICEID_TSV_1234567880_SHEET.contains(deviceId)){
				summaryObject = AuditTestUtils.populateMultipleDevicesActualAuditSummary(restClient,AuditTestUtils.getAuditMultipleDeviceidsSummaryPayloadLatest(AuditTestConstants.CWS_DEVICEID_TSV_1234567880_SHEET,sourceID,deviceId,"86400","1386201600","1388793599") );
				validatePartialAuditSummaryObject(summaryObject);
				/*controller=new AuditGoldenSetDataController(AuditTestConstants.CWS_DEVICEID_TSV_1234567880_SHEET);
				goldenSetErrorList=new ArrayList<String>();
				goldenSetErrorList=AuditTestUtils.auditSummaryGoldenSetData(controller.readExcelFileDataAndPrepareGoldenSetData(), summaryObject,fireWallType+":"+AuditTestConstants.CWS_DEVICEID_TSV_1234567880_SHEET,goldenSetErrorList);
				Assert.assertTrue(goldenSetErrorList.isEmpty(),AuditTestConstants.CWS_DEVICEID_TSV_1234567880_SHEET+" Summary Results are wrong");
			*/
			}
			else if(AuditTestConstants.CWS_DEVICEID_TSV_1234567860_SHEET.contains(deviceId)){
				summaryObject = AuditTestUtils.populateMultipleDevicesActualAuditSummary(restClient,AuditTestUtils.getAuditMultipleDeviceidsSummaryPayloadLatest(AuditTestConstants.CWS_DEVICEID_TSV_1234567860_SHEET,sourceID,deviceId,"86400","1386201600","1388793599") );
				validatePartialAuditSummaryObject(summaryObject);
				/*controller=new AuditGoldenSetDataController(AuditTestConstants.CWS_DEVICEID_TSV_1234567860_SHEET);
				goldenSetErrorList=new ArrayList<String>();
				goldenSetErrorList=AuditTestUtils.auditSummaryGoldenSetData(controller.readExcelFileDataAndPrepareGoldenSetData(), summaryObject,fireWallType+":"+AuditTestConstants.CWS_DEVICEID_TSV_1234567860_SHEET,goldenSetErrorList);
				Assert.assertTrue(goldenSetErrorList.isEmpty(),AuditTestConstants.CWS_DEVICEID_TSV_1234567860_SHEET+" Summary Results are wrong");
			*/
			}
			else if(AuditTestConstants.CWS_DEVICEID_TSV_1234567870_SHEET.contains(deviceId)){
				summaryObject = AuditTestUtils.populateMultipleDevicesActualAuditSummary(restClient,AuditTestUtils.getAuditMultipleDeviceidsSummaryPayloadLatest(AuditTestConstants.CWS_DEVICEID_TSV_1234567870_SHEET,sourceID,deviceId,"86400","1386201600","1388793599") );
				validatePartialAuditSummaryObject(summaryObject);
				/*controller=new AuditGoldenSetDataController(AuditTestConstants.CWS_DEVICEID_TSV_1234567870_SHEET);
				goldenSetErrorList=new ArrayList<String>();
				goldenSetErrorList=AuditTestUtils.auditSummaryGoldenSetData(controller.readExcelFileDataAndPrepareGoldenSetData(), summaryObject,fireWallType+":"+AuditTestConstants.CWS_DEVICEID_TSV_1234567870_SHEET,goldenSetErrorList);
				Assert.assertTrue(goldenSetErrorList.isEmpty(),AuditTestConstants.CWS_DEVICEID_TSV_1234567870_SHEET+" Summary Results are wrong");
			*/
			}
			else{
				summaryObject = AuditTestUtils.populateMultipleDevicesActualAuditSummary(restClient,AuditTestUtils.getAuditMultipleDeviceidsSummaryPayloadLatest(AuditTestConstants.FIREWALL_CWS_MULTIPLE_DEVICE_IDS,sourceID,null,"86400","1386201600","1388793599") );
				validatePartialAuditSummaryObject(summaryObject);
			/*	controller=new AuditGoldenSetDataController(AuditTestConstants.CWS_DEVICEID_TSV_SHEET);
				goldenSetErrorList=new ArrayList<String>();
				goldenSetErrorList=AuditTestUtils.auditSummaryGoldenSetData(controller.readExcelFileDataAndPrepareGoldenSetData(), summaryObject,fireWallType,goldenSetErrorList);
				Assert.assertTrue(goldenSetErrorList.isEmpty(),AuditTestConstants.FIREWALL_CWS_MULTIPLE_DEVICE_IDS+" Summary Results are wrong");
				*/
			}
			break;
		}
		
		//csv deviceid validation checks are commented temporarly
		case AuditTestConstants.FIREWALL_PAN_CSV_MULTIPLE_DEVICE_IDS: {
			if(AuditTestConstants.PAN_CSV_DEVICE_ID_1606015701_SHEET.contains(deviceId))
			{
				summaryObject = AuditTestUtils.populateMultipleDevicesActualAuditSummary(restClient,AuditTestUtils.getAuditMultipleDeviceidsSummaryPayloadLatest(AuditTestConstants.PAN_CSV_DEVICE_ID_1606015701_SHEET,sourceID,deviceId,"86400","1378512000","1381103999") );
				validatePartialAuditSummaryObject(summaryObject);
			/*	goldenSetErrorList=new ArrayList<String>();
				controller=new AuditGoldenSetDataController(AuditTestConstants.PAN_CSV_DEVICE_ID_1606015701_SHEET);
				goldenSetErrorList=AuditTestUtils.auditSummaryGoldenSetData(controller.readExcelFileDataAndPrepareGoldenSetData(), summaryObject,fireWallType+":"+AuditTestConstants.PAN_CSV_DEVICE_ID_1606015701_SHEET,goldenSetErrorList);
				Assert.assertTrue(goldenSetErrorList.isEmpty(),AuditTestConstants.PAN_CSV_DEVICE_ID_1606015701_SHEET+" Summary Results are wrong");
			*/
				
			}
			else if(AuditTestConstants.PAN_CSV_DEVICE_ID_1606015301_SHEET.contains(deviceId)){
				summaryObject = AuditTestUtils.populateMultipleDevicesActualAuditSummary(restClient,AuditTestUtils.getAuditMultipleDeviceidsSummaryPayloadLatest(AuditTestConstants.PAN_CSV_DEVICE_ID_1606015301_SHEET,sourceID,deviceId,"86400","1378512000","1381103999") );
				validatePartialAuditSummaryObject(summaryObject);
			/*	goldenSetErrorList=new ArrayList<String>();
				controller=new AuditGoldenSetDataController(AuditTestConstants.PAN_CSV_DEVICE_ID_1606015301_SHEET);
				goldenSetErrorList=AuditTestUtils.auditSummaryGoldenSetData(controller.readExcelFileDataAndPrepareGoldenSetData(), summaryObject,fireWallType+":"+AuditTestConstants.PAN_CSV_DEVICE_ID_1606015301_SHEET,goldenSetErrorList);
				Assert.assertTrue(goldenSetErrorList.isEmpty(),AuditTestConstants.PAN_CSV_DEVICE_ID_1606015301_SHEET+" Summary Results are wrong");
			*/
			}
			else if(AuditTestConstants.PAN_CSV_DEVICE_ID_1606015501_SHEET.contains(deviceId)){
				summaryObject = AuditTestUtils.populateMultipleDevicesActualAuditSummary(restClient,AuditTestUtils.getAuditMultipleDeviceidsSummaryPayloadLatest(AuditTestConstants.PAN_CSV_DEVICE_ID_1606015501_SHEET,sourceID,deviceId,"86400","1378512000","1381103999") );
				validatePartialAuditSummaryObject(summaryObject);
				/*goldenSetErrorList=new ArrayList<String>();
				controller=new AuditGoldenSetDataController(AuditTestConstants.PAN_CSV_DEVICE_ID_1606015501_SHEET);
				goldenSetErrorList=AuditTestUtils.auditSummaryGoldenSetData(controller.readExcelFileDataAndPrepareGoldenSetData(), summaryObject,fireWallType+":"+AuditTestConstants.PAN_CSV_DEVICE_ID_1606015501_SHEET,goldenSetErrorList);
				Assert.assertTrue(goldenSetErrorList.isEmpty(),AuditTestConstants.PAN_CSV_DEVICE_ID_1606015501_SHEET+" Summary Results are wrong");
			*/
			}
			else if(AuditTestConstants.PAN_CSV_DEVICE_ID_1606015901_SHEET.contains(deviceId)){
				summaryObject = AuditTestUtils.populateMultipleDevicesActualAuditSummary(restClient,AuditTestUtils.getAuditMultipleDeviceidsSummaryPayloadLatest(AuditTestConstants.PAN_CSV_DEVICE_ID_1606015901_SHEET,sourceID,deviceId,"86400","1378512000","1381103999") );
				validatePartialAuditSummaryObject(summaryObject);
				/*goldenSetErrorList=new ArrayList<String>();
				controller=new AuditGoldenSetDataController(AuditTestConstants.PAN_CSV_DEVICE_ID_1606015901_SHEET);
				goldenSetErrorList=AuditTestUtils.auditSummaryGoldenSetData(controller.readExcelFileDataAndPrepareGoldenSetData(), summaryObject,fireWallType+":"+AuditTestConstants.PAN_CSV_DEVICE_ID_1606015901_SHEET,goldenSetErrorList);
				Assert.assertTrue(goldenSetErrorList.isEmpty(),AuditTestConstants.PAN_CSV_DEVICE_ID_1606015901_SHEET+" Summary Results are wrong");
			*/
			}
			
			else if(AuditTestConstants.PAN_CSV_DEVICE_ID_1606015019_SHEET.contains(deviceId)){
				summaryObject = AuditTestUtils.populateMultipleDevicesActualAuditSummary(restClient,AuditTestUtils.getAuditMultipleDeviceidsSummaryPayloadLatest(AuditTestConstants.PAN_CSV_DEVICE_ID_1606015019_SHEET,sourceID,deviceId,"86400","1378512000","1381103999") );
				validatePartialAuditSummaryObject(summaryObject);
				/*goldenSetErrorList=new ArrayList<String>();
				controller=new AuditGoldenSetDataController(AuditTestConstants.PAN_CSV_DEVICE_ID_1606015019_SHEET);
				goldenSetErrorList=AuditTestUtils.auditSummaryGoldenSetData(controller.readExcelFileDataAndPrepareGoldenSetData(), summaryObject,fireWallType+":"+AuditTestConstants.PAN_CSV_DEVICE_ID_1606015019_SHEET,goldenSetErrorList);
				Assert.assertTrue(goldenSetErrorList.isEmpty(),AuditTestConstants.PAN_CSV_DEVICE_ID_1606015019_SHEET+" Summary Results are wrong");
			*/
			}
			else if(AuditTestConstants.PAN_CSV_DEVICE_ID_1606015401_SHEET.contains(deviceId)){
				summaryObject = AuditTestUtils.populateMultipleDevicesActualAuditSummary(restClient,AuditTestUtils.getAuditMultipleDeviceidsSummaryPayloadLatest(AuditTestConstants.PAN_CSV_DEVICE_ID_1606015401_SHEET,sourceID,deviceId,"86400","1378512000","1381103999") );
				validatePartialAuditSummaryObject(summaryObject);
				/*goldenSetErrorList=new ArrayList<String>();
				controller=new AuditGoldenSetDataController(AuditTestConstants.PAN_CSV_DEVICE_ID_1606015401_SHEET);
				goldenSetErrorList=AuditTestUtils.auditSummaryGoldenSetData(controller.readExcelFileDataAndPrepareGoldenSetData(), summaryObject,fireWallType+":"+AuditTestConstants.PAN_CSV_DEVICE_ID_1606015401_SHEET,goldenSetErrorList);
				Assert.assertTrue(goldenSetErrorList.isEmpty(),AuditTestConstants.PAN_CSV_DEVICE_ID_1606015401_SHEET+" Summary Results are wrong");
			*/
			}
			else if(AuditTestConstants.PAN_CSV_DEVICE_ID_1606015801_SHEET.contains(deviceId)){
				summaryObject = AuditTestUtils.populateMultipleDevicesActualAuditSummary(restClient,AuditTestUtils.getAuditMultipleDeviceidsSummaryPayloadLatest(AuditTestConstants.PAN_CSV_DEVICE_ID_1606015801_SHEET,sourceID,deviceId,"86400","1378512000","1381103999") );
				validatePartialAuditSummaryObject(summaryObject);
			/*	goldenSetErrorList=new ArrayList<String>();
				controller=new AuditGoldenSetDataController(AuditTestConstants.PAN_CSV_DEVICE_ID_1606015801_SHEET);
				goldenSetErrorList=AuditTestUtils.auditSummaryGoldenSetData(controller.readExcelFileDataAndPrepareGoldenSetData(), summaryObject,fireWallType+":"+AuditTestConstants.PAN_CSV_DEVICE_ID_1606015801_SHEET,goldenSetErrorList);
				Assert.assertTrue(goldenSetErrorList.isEmpty(),AuditTestConstants.PAN_CSV_DEVICE_ID_1606015801_SHEET+" Summary Results are wrong");
			*/
			}
			else if(AuditTestConstants.PAN_CSV_DEVICE_ID_1606015601_SHEET.contains(deviceId)){
				summaryObject = AuditTestUtils.populateMultipleDevicesActualAuditSummary(restClient,AuditTestUtils.getAuditMultipleDeviceidsSummaryPayloadLatest(AuditTestConstants.PAN_CSV_DEVICE_ID_1606015601_SHEET,sourceID,deviceId,"86400","1378512000","1381103999") );
				validatePartialAuditSummaryObject(summaryObject);
			/*	goldenSetErrorList=new ArrayList<String>();
				controller=new AuditGoldenSetDataController(AuditTestConstants.PAN_CSV_DEVICE_ID_1606015601_SHEET);
				goldenSetErrorList=AuditTestUtils.auditSummaryGoldenSetData(controller.readExcelFileDataAndPrepareGoldenSetData(), summaryObject,fireWallType+":"+AuditTestConstants.PAN_CSV_DEVICE_ID_1606015601_SHEET,goldenSetErrorList);
				Assert.assertTrue(goldenSetErrorList.isEmpty(),AuditTestConstants.PAN_CSV_DEVICE_ID_1606015601_SHEET+" Summary Results are wrong");
			*/
			}
			else if(AuditTestConstants.PAN_CSV_DEVICE_ID_1606015201_SHEET.contains(deviceId)){
				summaryObject = AuditTestUtils.populateMultipleDevicesActualAuditSummary(restClient,AuditTestUtils.getAuditMultipleDeviceidsSummaryPayloadLatest(AuditTestConstants.PAN_CSV_DEVICE_ID_1606015201_SHEET,sourceID,deviceId,"86400","1378512000","1381103999") );
				validatePartialAuditSummaryObject(summaryObject);
				/*goldenSetErrorList=new ArrayList<String>();
				controller=new AuditGoldenSetDataController(AuditTestConstants.PAN_CSV_DEVICE_ID_1606015201_SHEET);
				goldenSetErrorList=AuditTestUtils.auditSummaryGoldenSetData(controller.readExcelFileDataAndPrepareGoldenSetData(), summaryObject,fireWallType+":"+AuditTestConstants.PAN_CSV_DEVICE_ID_1606015201_SHEET,goldenSetErrorList);
				Assert.assertTrue(goldenSetErrorList.isEmpty(),AuditTestConstants.PAN_CSV_DEVICE_ID_1606015201_SHEET+" Summary Results are wrong");
			*/
			}
			
			else{
				summaryObject = AuditTestUtils.populateMultipleDevicesActualAuditSummary(restClient,AuditTestUtils.getAuditMultipleDeviceidsSummaryPayloadLatest(AuditTestConstants.FIREWALL_PAN_CSV_MULTIPLE_DEVICE_IDS,sourceID,null,"86400","1378512000","1381103999") );
				validatePartialAuditSummaryObject(summaryObject);
			/*	goldenSetErrorList=new ArrayList<String>();
				controller=new AuditGoldenSetDataController(AuditTestConstants.PAN_CSV_DEVICE_ID_SHEET);
				goldenSetErrorList=AuditTestUtils.auditSummaryGoldenSetData(controller.readExcelFileDataAndPrepareGoldenSetData(), summaryObject,fireWallType,goldenSetErrorList);
				Assert.assertTrue(goldenSetErrorList.isEmpty(),AuditTestConstants.FIREWALL_PAN_CSV_MULTIPLE_DEVICE_IDS+" Summary Results are wrong");
				*/
				
			}
			//AuditTestUtils.auditSummaryGoldenSetData(controller.readExcelFileDataAndPrepareGoldenSetData(), summaryObject,fireWallType);
			break;
		}
		case AuditTestConstants.FIREWALL_PAN_SYS_MULTIPLE_DEVICE_IDS: {
			if(AuditTestConstants.PAN_SYS_DEVICE_ID_001901000402_SHEET.contains(deviceId))
			{
				summaryObject = AuditTestUtils.populateMultipleDevicesActualAuditSummary(restClient,AuditTestUtils.getAuditMultipleDeviceidsSummaryPayloadLatest(AuditTestConstants.PAN_SYS_DEVICE_ID_001901000402_SHEET,sourceID,deviceId,"86400","1398211200","1400803199") );
				validatePartialAuditSummaryObject(summaryObject);
				/*goldenSetErrorList=new ArrayList<String>();
				controller=new AuditGoldenSetDataController(AuditTestConstants.PAN_SYS_DEVICE_ID_001901000402_SHEET);
				goldenSetErrorList=AuditTestUtils.auditSummaryGoldenSetData(controller.readExcelFileDataAndPrepareGoldenSetData(), summaryObject,fireWallType+":"+AuditTestConstants.PAN_SYS_DEVICE_ID_001901000402_SHEET,goldenSetErrorList);
				Assert.assertTrue(goldenSetErrorList.isEmpty(),AuditTestConstants.PAN_SYS_DEVICE_ID_001901000402_SHEET+" Summary Results are wrong");
			*/
			}
			else if(AuditTestConstants.PAN_SYS_DEVICE_ID_001901123456_SHEET.contains(deviceId)){
				summaryObject = AuditTestUtils.populateMultipleDevicesActualAuditSummary(restClient,AuditTestUtils.getAuditMultipleDeviceidsSummaryPayloadLatest(AuditTestConstants.PAN_SYS_DEVICE_ID_001901123456_SHEET,sourceID,deviceId,"86400","1398211200","1400803199") );
				validatePartialAuditSummaryObject(summaryObject);
			/*	goldenSetErrorList=new ArrayList<String>();
				controller=new AuditGoldenSetDataController(AuditTestConstants.PAN_SYS_DEVICE_ID_001901123456_SHEET);
				goldenSetErrorList=AuditTestUtils.auditSummaryGoldenSetData(controller.readExcelFileDataAndPrepareGoldenSetData(), summaryObject,fireWallType+":"+AuditTestConstants.PAN_SYS_DEVICE_ID_001901123456_SHEET,goldenSetErrorList);
				Assert.assertTrue(goldenSetErrorList.isEmpty(),AuditTestConstants.PAN_SYS_DEVICE_ID_001901123456_SHEET+" Summary Results are wrong");
			*/
			}
			else if(AuditTestConstants.PAN_SYS_DEVICE_ID_881901123456_SHEET.contains(deviceId)){
				summaryObject = AuditTestUtils.populateMultipleDevicesActualAuditSummary(restClient,AuditTestUtils.getAuditMultipleDeviceidsSummaryPayloadLatest(AuditTestConstants.PAN_SYS_DEVICE_ID_881901123456_SHEET,sourceID,deviceId,"86400","1398211200","1400803199") );
				validatePartialAuditSummaryObject(summaryObject);
			/*	goldenSetErrorList=new ArrayList<String>();
				controller=new AuditGoldenSetDataController(AuditTestConstants.PAN_SYS_DEVICE_ID_881901123456_SHEET);
				goldenSetErrorList=AuditTestUtils.auditSummaryGoldenSetData(controller.readExcelFileDataAndPrepareGoldenSetData(), summaryObject,fireWallType+":"+AuditTestConstants.PAN_SYS_DEVICE_ID_881901123456_SHEET,goldenSetErrorList);
				Assert.assertTrue(goldenSetErrorList.isEmpty(),AuditTestConstants.PAN_SYS_DEVICE_ID_881901123456_SHEET+" Summary Results are wrong");
			*/
			}
			else if(AuditTestConstants.PAN_SYS_DEVICE_ID_991901123456_SHEET.contains(deviceId)){
				summaryObject = AuditTestUtils.populateMultipleDevicesActualAuditSummary(restClient,AuditTestUtils.getAuditMultipleDeviceidsSummaryPayloadLatest(AuditTestConstants.PAN_SYS_DEVICE_ID_991901123456_SHEET,sourceID,deviceId,"86400","1398211200","1400803199") );
				validatePartialAuditSummaryObject(summaryObject);
			/*	goldenSetErrorList=new ArrayList<String>();
				controller=new AuditGoldenSetDataController(AuditTestConstants.PAN_SYS_DEVICE_ID_991901123456_SHEET);
				goldenSetErrorList=AuditTestUtils.auditSummaryGoldenSetData(controller.readExcelFileDataAndPrepareGoldenSetData(), summaryObject,fireWallType+":"+AuditTestConstants.PAN_SYS_DEVICE_ID_991901123456_SHEET,goldenSetErrorList);
				Assert.assertTrue(goldenSetErrorList.isEmpty(),AuditTestConstants.PAN_SYS_DEVICE_ID_991901123456_SHEET+" Summary Results are wrong");
			*/
			}
			else{
				summaryObject = AuditTestUtils.populateMultipleDevicesActualAuditSummary(restClient,AuditTestUtils.getAuditMultipleDeviceidsSummaryPayloadLatest(AuditTestConstants.FIREWALL_PAN_SYS_MULTIPLE_DEVICE_IDS,sourceID,null,"86400","1398211200","1400803199") );
				Reporter.log("summaryObject:"+summaryObject,true);
				validatePartialAuditSummaryObject(summaryObject);
			/*	goldenSetErrorList=new ArrayList<String>();
				controller=new AuditGoldenSetDataController(AuditTestConstants.PAN_SYS_DEVICE_ID_SHEET);
				goldenSetErrorList=AuditTestUtils.auditSummaryGoldenSetData(controller.readExcelFileDataAndPrepareGoldenSetData(), summaryObject,fireWallType,goldenSetErrorList);
				Assert.assertTrue(goldenSetErrorList.isEmpty(),AuditTestConstants.FIREWALL_PAN_SYS_MULTIPLE_DEVICE_IDS+" Summary Results are wrong");
				*/
			}
			//AuditTestUtils.auditSummaryGoldenSetData(controller.readExcelFileDataAndPrepareGoldenSetData(), summaryObject,fireWallType);
			break;
		}
		
		case AuditTestConstants.FIREWALL_BLUECOAT_PROXYSG_DEVICE_IDS: {
			if(deviceId.contains(AuditTestConstants.FIREWALL_BLUECOAT_PROXYSG_DEVICE_10_0_0_1_SHEET))
			{
				summaryObject = AuditTestUtils.populateMultipleDevicesActualAuditSummary(restClient,AuditTestUtils.getAuditMultipleDeviceidsSummaryPayloadLatest(AuditTestConstants.FIREWALL_BLUECOAT_PROXYSG_DEVICE_10_0_0_1_SHEET,sourceID,deviceId,"86400","1437177600","1439769599") );
				validatePartialAuditSummaryObject(summaryObject);
			/*	goldenSetErrorList=new ArrayList<String>();
				//controller=new AuditGoldenSetDataController(AuditTestConstants.FIREWALL_BLUECOAT_PROXYSG_DEVICE_10_0_0_1_SHEET);
				goldenSetErrorList=AuditTestUtils.auditSummaryGoldenSetData(controller.readExcelFileDataAndPrepareGoldenSetData(), summaryObject,fireWallType+":"+AuditTestConstants.FIREWALL_BLUECOAT_PROXYSG_DEVICE_10_0_0_1_SHEET,goldenSetErrorList);
				Assert.assertTrue(goldenSetErrorList.isEmpty(),AuditTestConstants.FIREWALL_BLUECOAT_PROXYSG_DEVICE_10_0_0_1_SHEET+" Summary Results are wrong");
				*/
			}
			else if(deviceId.contains(AuditTestConstants.FIREWALL_BLUECOAT_PROXYSG_DEVICE_192_168_1_100_SHEET)){
				summaryObject = AuditTestUtils.populateMultipleDevicesActualAuditSummary(restClient,AuditTestUtils.getAuditMultipleDeviceidsSummaryPayloadLatest(AuditTestConstants.FIREWALL_BLUECOAT_PROXYSG_DEVICE_192_168_1_100_SHEET,sourceID,deviceId,"86400","1437177600","1439769599") );
				validatePartialAuditSummaryObject(summaryObject);
				/*goldenSetErrorList=new ArrayList<String>();
				controller=new AuditGoldenSetDataController(AuditTestConstants.FIREWALL_BLUECOAT_PROXYSG_DEVICE_192_168_1_100_SHEET);
				goldenSetErrorList=AuditTestUtils.auditSummaryGoldenSetData(controller.readExcelFileDataAndPrepareGoldenSetData(), summaryObject,fireWallType+":"+AuditTestConstants.FIREWALL_BLUECOAT_PROXYSG_DEVICE_192_168_1_100_SHEET,goldenSetErrorList);
				Assert.assertTrue(goldenSetErrorList.isEmpty(),AuditTestConstants.FIREWALL_BLUECOAT_PROXYSG_DEVICE_192_168_1_100_SHEET+" Summary Results are wrong");
			*/
			}
			else if(deviceId.contains(AuditTestConstants.FIREWALL_BLUECOAT_PROXYSG_DEVICE_192_168_2_100_SHEET)){
				summaryObject = AuditTestUtils.populateMultipleDevicesActualAuditSummary(restClient,AuditTestUtils.getAuditMultipleDeviceidsSummaryPayloadLatest(AuditTestConstants.FIREWALL_BLUECOAT_PROXYSG_DEVICE_192_168_2_100_SHEET,sourceID,deviceId,"86400","1437177600","1439769599") );
				validatePartialAuditSummaryObject(summaryObject);
			/*	goldenSetErrorList=new ArrayList<String>();
				controller=new AuditGoldenSetDataController(AuditTestConstants.FIREWALL_BLUECOAT_PROXYSG_DEVICE_192_168_2_100_SHEET);
				goldenSetErrorList=AuditTestUtils.auditSummaryGoldenSetData(controller.readExcelFileDataAndPrepareGoldenSetData(), summaryObject,fireWallType+":"+AuditTestConstants.FIREWALL_BLUECOAT_PROXYSG_DEVICE_192_168_2_100_SHEET,goldenSetErrorList);
				Assert.assertTrue(goldenSetErrorList.isEmpty(),AuditTestConstants.FIREWALL_BLUECOAT_PROXYSG_DEVICE_192_168_2_100_SHEET+" Summary Results are wrong");
			*/
			}
			else if(deviceId.contains(AuditTestConstants.FIREWALL_BLUECOAT_PROXYSG_DEVICE_192_168_3_100_SHEET)){
				summaryObject = AuditTestUtils.populateMultipleDevicesActualAuditSummary(restClient,AuditTestUtils.getAuditMultipleDeviceidsSummaryPayloadLatest(AuditTestConstants.FIREWALL_BLUECOAT_PROXYSG_DEVICE_192_168_3_100_SHEET,sourceID,deviceId,"86400","1437177600","1439769599") );
				validatePartialAuditSummaryObject(summaryObject);
			/*	goldenSetErrorList=new ArrayList<String>();
				controller=new AuditGoldenSetDataController(AuditTestConstants.FIREWALL_BLUECOAT_PROXYSG_DEVICE_192_168_3_100_SHEET);
				goldenSetErrorList=AuditTestUtils.auditSummaryGoldenSetData(controller.readExcelFileDataAndPrepareGoldenSetData(), summaryObject,fireWallType+":"+AuditTestConstants.FIREWALL_BLUECOAT_PROXYSG_DEVICE_192_168_3_100_SHEET,goldenSetErrorList);
				Assert.assertTrue(goldenSetErrorList.isEmpty(),AuditTestConstants.FIREWALL_BLUECOAT_PROXYSG_DEVICE_192_168_3_100_SHEET+" Summary Results are wrong");
			*/
			}
			else{
				summaryObject = AuditTestUtils.populateMultipleDevicesActualAuditSummary(restClient,AuditTestUtils.getAuditMultipleDeviceidsSummaryPayloadLatest(AuditTestConstants.FIREWALL_BLUECOAT_PROXYSG_DEVICE_IDS,sourceID,null,"86400","1437177600","1439769599") );
				validatePartialAuditSummaryObject(summaryObject);
			/*	goldenSetErrorList=new ArrayList<String>();
				controller=new AuditGoldenSetDataController(AuditTestConstants.FIREWALL_BLUECOAT_PROXYSG_DEVICE_SHEET);
				goldenSetErrorList=AuditTestUtils.auditSummaryGoldenSetData(controller.readExcelFileDataAndPrepareGoldenSetData(), summaryObject,fireWallType,goldenSetErrorList);
				Assert.assertTrue(goldenSetErrorList.isEmpty(),AuditTestConstants.FIREWALL_BLUECOAT_PROXYSG_DEVICE_IDS+" Summary Results are wrong");
				*/
			}
			//AuditTestUtils.auditSummaryGoldenSetData(controller.readExcelFileDataAndPrepareGoldenSetData(), summaryObject,fireWallType);
			break;
		}
		
		case AuditTestConstants.FIREWALL_WSA_W3C_DEVICE_IDS: {
			if(deviceId.contains(AuditTestConstants.WSA_W3C_DEVICE_ID_10_0_0_1_SHEET))
				
			{
				summaryObject = AuditTestUtils.populateMultipleDevicesActualAuditSummary(restClient,AuditTestUtils.getAuditMultipleDeviceidsSummaryPayloadLatest(AuditTestConstants.WSA_W3C_DEVICE_ID_10_0_0_1_SHEET,sourceID,deviceId,"86400","1415059200","1417651199") );
				Reporter.log("summaryObject"+summaryObject,true);
				validatePartialAuditSummaryObject(summaryObject);
				//controller=new AuditGoldenSetDataController(AuditTestConstants.WSA_W3C_DEVICE_ID_10_0_0_1_SHEET);
				//goldenSetErrorList=AuditTestUtils.auditSummaryGoldenSetData(controller.readExcelFileDataAndPrepareGoldenSetData(), summaryObject,fireWallType+":"+AuditTestConstants.WSA_W3C_DEVICE_ID_10_0_0_1_SHEET,goldenSetErrorList);
				//Assert.assertTrue(goldenSetErrorList.isEmpty(),AuditTestConstants.WSA_W3C_DEVICE_ID_10_0_0_1_SHEET+" Summary Results are wrong");
			
			}
			else if(deviceId.contains(AuditTestConstants.WSA_W3C_DEVICE_ID_10_0_0_2_SHEET)){
				summaryObject = AuditTestUtils.populateMultipleDevicesActualAuditSummary(restClient,AuditTestUtils.getAuditMultipleDeviceidsSummaryPayloadLatest(AuditTestConstants.WSA_W3C_DEVICE_ID_10_0_0_2_SHEET,sourceID,deviceId,"86400","1415059200","1417651199") );
				Reporter.log("summaryObject"+summaryObject,true);
				validatePartialAuditSummaryObject(summaryObject);
				//controller=new AuditGoldenSetDataController(AuditTestConstants.WSA_W3C_DEVICE_ID_10_0_0_2_SHEET);
				//goldenSetErrorList=AuditTestUtils.auditSummaryGoldenSetData(controller.readExcelFileDataAndPrepareGoldenSetData(), summaryObject,fireWallType+":"+AuditTestConstants.WSA_W3C_DEVICE_ID_10_0_0_2_SHEET,goldenSetErrorList);
				//Assert.assertTrue(goldenSetErrorList.isEmpty(),AuditTestConstants.WSA_W3C_DEVICE_ID_10_0_0_2_SHEET+" Summary Results are wrong");
			
			}
			else if(deviceId.contains(AuditTestConstants.WSA_W3C_DEVICE_ID_192_168_1_100_SHEET)){
				summaryObject = AuditTestUtils.populateMultipleDevicesActualAuditSummary(restClient,AuditTestUtils.getAuditMultipleDeviceidsSummaryPayloadLatest(AuditTestConstants.WSA_W3C_DEVICE_ID_192_168_1_100_SHEET,sourceID,deviceId,"86400","1415059200","1417651199") );
				validatePartialAuditSummaryObject(summaryObject);
				//controller=new AuditGoldenSetDataController(AuditTestConstants.WSA_W3C_DEVICE_ID_192_168_1_100_SHEET);
				//goldenSetErrorList=AuditTestUtils.auditSummaryGoldenSetData(controller.readExcelFileDataAndPrepareGoldenSetData(), summaryObject,fireWallType+":"+AuditTestConstants.WSA_W3C_DEVICE_ID_192_168_1_100_SHEET,goldenSetErrorList);
				//Assert.assertTrue(goldenSetErrorList.isEmpty(),AuditTestConstants.WSA_W3C_DEVICE_ID_192_168_1_100_SHEET+" Summary Results are wrong");
			
			}
			else if(deviceId.contains(AuditTestConstants.WSA_W3C_DEVICE_ID_192_168_1_101_SHEET)){
				summaryObject = AuditTestUtils.populateMultipleDevicesActualAuditSummary(restClient,AuditTestUtils.getAuditMultipleDeviceidsSummaryPayloadLatest(AuditTestConstants.WSA_W3C_DEVICE_ID_192_168_1_101_SHEET,sourceID,deviceId,"86400","1415059200","1417651199") );
				validatePartialAuditSummaryObject(summaryObject);
				//controller=new AuditGoldenSetDataController(AuditTestConstants.WSA_W3C_DEVICE_ID_192_168_1_101_SHEET);
				//goldenSetErrorList=AuditTestUtils.auditSummaryGoldenSetData(controller.readExcelFileDataAndPrepareGoldenSetData(), summaryObject,fireWallType+":"+AuditTestConstants.WSA_W3C_DEVICE_ID_192_168_1_101_SHEET,goldenSetErrorList);
				//Assert.assertTrue(goldenSetErrorList.isEmpty(),AuditTestConstants.WSA_W3C_DEVICE_ID_192_168_1_101_SHEET+" Summary Results are wrong");
			
			}
			else{
				summaryObject = AuditTestUtils.populateMultipleDevicesActualAuditSummary(restClient,AuditTestUtils.getAuditMultipleDeviceidsSummaryPayloadLatest(AuditTestConstants.FIREWALL_WSA_W3C_DEVICE_IDS,sourceID,null,"86400","1415059200","1417651199") );
				validatePartialAuditSummaryObject(summaryObject);
				//controller=new AuditGoldenSetDataController(AuditTestConstants.WSA_W3C_DEVICE_ID_SHEET);
				//goldenSetErrorList=AuditTestUtils.auditSummaryGoldenSetData(controller.readExcelFileDataAndPrepareGoldenSetData(), summaryObject,fireWallType,goldenSetErrorList);
				//Assert.assertTrue(goldenSetErrorList.isEmpty(),AuditTestConstants.FIREWALL_WSA_W3C_DEVICE_IDS+" Summary Results are wrong");
				
			}
			//AuditTestUtils.auditSummaryGoldenSetData(controller.readExcelFileDataAndPrepareGoldenSetData(), summaryObject,fireWallType);
			break;
		}
		
		
		case AuditTestConstants.FIREWALL_WSA_ACCESS_DEVICE_IDS: {
			if(deviceId.contains(AuditTestConstants.WSA_ACCESS_DEVICE_ID_192_168_1_0_SHEET))
			{
				summaryObject = AuditTestUtils.populateMultipleDevicesActualAuditSummary(restClient,AuditTestUtils.getAuditMultipleDeviceidsSummaryPayloadLatest(AuditTestConstants.WSA_ACCESS_DEVICE_ID_192_168_1_0_SHEET,sourceID,deviceId,"86400","1415059200","1417651199") );
				validatePartialAuditSummaryObject(summaryObject);
				//controller=new AuditGoldenSetDataController(AuditTestConstants.WSA_ACCESS_DEVICE_ID_192_168_1_0_SHEET);
				//goldenSetErrorList=AuditTestUtils.auditSummaryGoldenSetData(controller.readExcelFileDataAndPrepareGoldenSetData(), summaryObject,fireWallType+":"+AuditTestConstants.WSA_ACCESS_DEVICE_ID_192_168_1_0_SHEET,goldenSetErrorList);
				//Assert.assertTrue(goldenSetErrorList.isEmpty(),AuditTestConstants.WSA_ACCESS_DEVICE_ID_192_168_1_0_SHEET+" Summary Results are wrong");
			
				
			}
			else if(deviceId.contains(AuditTestConstants.WSA_ACCESS_DEVICE_ID_192_168_1_1_SHEET)){
				summaryObject = AuditTestUtils.populateMultipleDevicesActualAuditSummary(restClient,AuditTestUtils.getAuditMultipleDeviceidsSummaryPayloadLatest(AuditTestConstants.WSA_ACCESS_DEVICE_ID_192_168_1_1_SHEET,sourceID,deviceId,"86400","1415059200","1417651199") );
				//validatePartialAuditSummaryObject(summaryObject);
				//controller=new AuditGoldenSetDataController(AuditTestConstants.WSA_ACCESS_DEVICE_ID_192_168_1_1_SHEET);
				//goldenSetErrorList=AuditTestUtils.auditSummaryGoldenSetData(controller.readExcelFileDataAndPrepareGoldenSetData(), summaryObject,fireWallType+":"+AuditTestConstants.WSA_ACCESS_DEVICE_ID_192_168_1_1_SHEET,goldenSetErrorList);
				//Assert.assertTrue(goldenSetErrorList.isEmpty(),AuditTestConstants.WSA_ACCESS_DEVICE_ID_192_168_1_1_SHEET+" Summary Results are wrong");
			
			}
			else{
				summaryObject = AuditTestUtils.populateMultipleDevicesActualAuditSummary(restClient,AuditTestUtils.getAuditMultipleDeviceidsSummaryPayloadLatest(AuditTestConstants.FIREWALL_WSA_ACCESS_DEVICE_IDS,sourceID,null,"86400","1415059200","1417651199") );
				//validatePartialAuditSummaryObject(summaryObject);
				//controller=new AuditGoldenSetDataController(AuditTestConstants.WSA_ACCESS_DEVICE_ID_SHEET);
				//goldenSetErrorList=AuditTestUtils.auditSummaryGoldenSetData(controller.readExcelFileDataAndPrepareGoldenSetData(), summaryObject,fireWallType,goldenSetErrorList);
				//Assert.assertTrue(goldenSetErrorList.isEmpty(),AuditTestConstants.FIREWALL_WSA_ACCESS_DEVICE_IDS+" Summary Results are wrong");
				
			}
			//AuditTestUtils.auditSummaryGoldenSetData(controller.readExcelFileDataAndPrepareGoldenSetData(), summaryObject,fireWallType);
			break;
		}
		default: {

			break;
		}
		}
		
		
		
	}
	
	@Test (priority=3,  dependsOnMethods="testCreateDeviceLogsUsingScpTransPort")
	public void testAuditSummaryBySelectingMultipleDeviceIdsOfSingeDatasource() throws Exception
	{
		Reporter.log("Getting summary for "+ fireWallType +" its ID is: "+sourceID,true);
		int size=deviceLogsData.size();
		Reporter.log("multiple deviceids "+deviceLogsData,true);
		Reporter.log("multiple deviceids size:"+size,true);
		JSONObject summaryObject=null;
		AuditGoldenSetDataController controller=null;
		
		switch(fireWallType)
		{
		
		case AuditTestConstants.FIREWALL_CWS_MULTIPLE_DEVICE_IDS: {
		
				summaryObject = AuditTestUtils.populateMultipleDevicesActualAuditSummary(restClient,AuditTestUtils.getAuditMultipleDeviceidsSummaryorReportBySelectingAllSourceIdsPayloadLatest(sourceID,deviceLogsData,"86400","1386201600","1388793599") );
				validatePartialAuditSummaryObject(summaryObject);
				/*controller=new AuditGoldenSetDataController(AuditTestConstants.CWS_DEVICEID_TSV_1234567890_SHEET);
				goldenSetErrorList=new ArrayList<String>();
				goldenSetErrorList=AuditTestUtils.auditSummaryGoldenSetData(controller.readExcelFileDataAndPrepareGoldenSetData(), summaryObject,fireWallType+":"+AuditTestConstants.CWS_DEVICEID_TSV_1234567890_SHEET,goldenSetErrorList);
				Assert.assertTrue(goldenSetErrorList.isEmpty(),AuditTestConstants.CWS_DEVICEID_TSV_1234567890_SHEET+" Summary Results are wrong");
		*/
		}
		case AuditTestConstants.FIREWALL_PAN_CSV_MULTIPLE_DEVICE_IDS: {
			summaryObject = AuditTestUtils.populateMultipleDevicesActualAuditSummary(restClient,AuditTestUtils.getAuditMultipleDeviceidsSummaryorReportBySelectingAllSourceIdsPayloadLatest(sourceID,deviceLogsData,"86400","1378512000","1381103999") );
			validatePartialAuditSummaryObject(summaryObject);
			/*controller=new AuditGoldenSetDataController(AuditTestConstants.CWS_DEVICEID_TSV_1234567890_SHEET);
			goldenSetErrorList=new ArrayList<String>();
			goldenSetErrorList=AuditTestUtils.auditSummaryGoldenSetData(controller.readExcelFileDataAndPrepareGoldenSetData(), summaryObject,fireWallType+":"+AuditTestConstants.CWS_DEVICEID_TSV_1234567890_SHEET,goldenSetErrorList);
			Assert.assertTrue(goldenSetErrorList.isEmpty(),AuditTestConstants.CWS_DEVICEID_TSV_1234567890_SHEET+" Summary Results are wrong");
	*/
			
		}
		case AuditTestConstants.FIREWALL_PAN_SYS_MULTIPLE_DEVICE_IDS: {
			summaryObject = AuditTestUtils.populateMultipleDevicesActualAuditSummary(restClient,AuditTestUtils.getAuditMultipleDeviceidsSummaryorReportBySelectingAllSourceIdsPayloadLatest(sourceID,deviceLogsData,"86400","1398211200","1400803199") );
			validatePartialAuditSummaryObject(summaryObject);
			/*controller=new AuditGoldenSetDataController(AuditTestConstants.CWS_DEVICEID_TSV_1234567890_SHEET);
			goldenSetErrorList=new ArrayList<String>();
			goldenSetErrorList=AuditTestUtils.auditSummaryGoldenSetData(controller.readExcelFileDataAndPrepareGoldenSetData(), summaryObject,fireWallType+":"+AuditTestConstants.CWS_DEVICEID_TSV_1234567890_SHEET,goldenSetErrorList);
			Assert.assertTrue(goldenSetErrorList.isEmpty(),AuditTestConstants.CWS_DEVICEID_TSV_1234567890_SHEET+" Summary Results are wrong");
	*/
		}
		case AuditTestConstants.FIREWALL_BLUECOAT_PROXYSG_DEVICE_IDS: {
			summaryObject = AuditTestUtils.populateMultipleDevicesActualAuditSummary(restClient,AuditTestUtils.getAuditMultipleDeviceidsSummaryorReportBySelectingAllSourceIdsPayloadLatest(sourceID,deviceLogsData,"86400","1437177600","1439769599") );
			validatePartialAuditSummaryObject(summaryObject);
			/*controller=new AuditGoldenSetDataController(AuditTestConstants.CWS_DEVICEID_TSV_1234567890_SHEET);
			goldenSetErrorList=new ArrayList<String>();
			goldenSetErrorList=AuditTestUtils.auditSummaryGoldenSetData(controller.readExcelFileDataAndPrepareGoldenSetData(), summaryObject,fireWallType+":"+AuditTestConstants.CWS_DEVICEID_TSV_1234567890_SHEET,goldenSetErrorList);
			Assert.assertTrue(goldenSetErrorList.isEmpty(),AuditTestConstants.CWS_DEVICEID_TSV_1234567890_SHEET+" Summary Results are wrong");
	*/
			
		}
		}
		
	}
	
	
	@Test(priority=3,dataProvider="getDeviceLogIds", dependsOnMethods="testAuditSummaryForDeviceIds")
	public void testAuditReport(String deviceidname,String deviceId) throws Exception {
		
		Logger.info("Getting Audit Report for "+ fireWallType +" its ID is: "+sourceID);
		List<NameValuePair> queryParams = null;	
		AuditGoldenSetDataController controller=null;
		JSONObject reportObject=null;
		
		switch(fireWallType)
		{
		case AuditTestConstants.FIREWALL_CWS_MULTIPLE_DEVICE_IDS: {
			if(AuditTestConstants.CWS_DEVICEID_TSV_1234567890_SHEET.contains(deviceId))
			{
				reportObject = AuditTestUtils.populateMultipleDevicesActualAuditReport(restClient,AuditTestUtils.getAuditMultipleDeviceidsSummaryPayloadLatest(AuditTestConstants.CWS_DEVICEID_TSV_1234567890_SHEET,sourceID,deviceId,"86400","1386201600","1388793599") );
				validatePartialAuditReportObject(reportObject);
				//controller=new AuditGoldenSetDataController(AuditTestConstants.CWS_DEVICEID_TSV_1234567890_SHEET);
			}
			else if(AuditTestConstants.CWS_DEVICEID_TSV_1234567880_SHEET.contains(deviceId)){
				reportObject = AuditTestUtils.populateMultipleDevicesActualAuditReport(restClient,AuditTestUtils.getAuditMultipleDeviceidsSummaryPayloadLatest(AuditTestConstants.CWS_DEVICEID_TSV_1234567880_SHEET,sourceID,deviceId,"86400","1386201600","1388793599") );
				validatePartialAuditReportObject(reportObject);
				//controller=new AuditGoldenSetDataController(AuditTestConstants.CWS_DEVICEID_TSV_1234567880_SHEET);
			}
			else if(AuditTestConstants.CWS_DEVICEID_TSV_1234567860_SHEET.contains(deviceId)){
				reportObject = AuditTestUtils.populateMultipleDevicesActualAuditReport(restClient,AuditTestUtils.getAuditMultipleDeviceidsSummaryPayloadLatest(AuditTestConstants.CWS_DEVICEID_TSV_1234567860_SHEET,sourceID,deviceId,"86400","1386201600","1388793599") );
				validatePartialAuditReportObject(reportObject);
				//controller=new AuditGoldenSetDataController(AuditTestConstants.CWS_DEVICEID_TSV_1234567860_SHEET);
			}
			else if(AuditTestConstants.CWS_DEVICEID_TSV_1234567870_SHEET.contains(deviceId)){
				reportObject = AuditTestUtils.populateMultipleDevicesActualAuditReport(restClient,AuditTestUtils.getAuditMultipleDeviceidsSummaryPayloadLatest(AuditTestConstants.CWS_DEVICEID_TSV_1234567870_SHEET,sourceID,deviceId,"86400","1386201600","1388793599") );
				validatePartialAuditReportObject(reportObject);
				//controller=new AuditGoldenSetDataController(AuditTestConstants.CWS_DEVICEID_TSV_1234567870_SHEET);
			}
			else{
				reportObject = AuditTestUtils.populateMultipleDevicesActualAuditReport(restClient,AuditTestUtils.getAuditMultipleDeviceidsSummaryPayloadLatest(AuditTestConstants.FIREWALL_CWS_MULTIPLE_DEVICE_IDS,sourceID,null,"86400","1386201600","1388793599") );
				validatePartialAuditReportObject(reportObject);
				//controller=new AuditGoldenSetDataController(AuditTestConstants.CWS_DEVICEID_TSV_SHEET);
			}
			//AuditTestUtils.auditSummaryGoldenSetData(controller.readExcelFileDataAndPrepareGoldenSetData(), summaryObject,fireWallType);
			break;
		}	
		case AuditTestConstants.FIREWALL_PAN_CSV_MULTIPLE_DEVICE_IDS: {
			if(AuditTestConstants.PAN_CSV_DEVICE_ID_1606015701_SHEET.contains(deviceId))
			{
				reportObject = AuditTestUtils.populateMultipleDevicesActualAuditReport(restClient,AuditTestUtils.getAuditMultipleDeviceidsSummaryPayloadLatest(AuditTestConstants.PAN_CSV_DEVICE_ID_1606015701_SHEET,sourceID,deviceId,"86400","1378512000","1381103999") );
				validatePartialAuditReportObject(reportObject);
				//controller=new AuditGoldenSetDataController(AuditTestConstants.CWS_DEVICEID_TSV_1234567890_SHEET);
			}
			else if(AuditTestConstants.PAN_CSV_DEVICE_ID_1606015301_SHEET.contains(deviceId)){
				reportObject = AuditTestUtils.populateMultipleDevicesActualAuditReport(restClient,AuditTestUtils.getAuditMultipleDeviceidsSummaryPayloadLatest(AuditTestConstants.PAN_CSV_DEVICE_ID_1606015301_SHEET,sourceID,deviceId,"86400","1378512000","1381103999") );
				validatePartialAuditReportObject(reportObject);
				//controller=new AuditGoldenSetDataController(AuditTestConstants.CWS_DEVICEID_TSV_1234567880_SHEET);
			}
			else if(AuditTestConstants.PAN_CSV_DEVICE_ID_1606015501_SHEET.contains(deviceId)){
				reportObject = AuditTestUtils.populateMultipleDevicesActualAuditReport(restClient,AuditTestUtils.getAuditMultipleDeviceidsSummaryPayloadLatest(AuditTestConstants.PAN_CSV_DEVICE_ID_1606015501_SHEET,sourceID,deviceId,"86400","1378512000","1381103999") );
				validatePartialAuditReportObject(reportObject);
				//controller=new AuditGoldenSetDataController(AuditTestConstants.CWS_DEVICEID_TSV_1234567860_SHEET);
			}
			else if(AuditTestConstants.PAN_CSV_DEVICE_ID_1606015901_SHEET.contains(deviceId)){
				reportObject = AuditTestUtils.populateMultipleDevicesActualAuditReport(restClient,AuditTestUtils.getAuditMultipleDeviceidsSummaryPayloadLatest(AuditTestConstants.PAN_CSV_DEVICE_ID_1606015901_SHEET,sourceID,deviceId,"86400","1378512000","1381103999") );
				validatePartialAuditReportObject(reportObject);
				//controller=new AuditGoldenSetDataController(AuditTestConstants.CWS_DEVICEID_TSV_1234567870_SHEET);
			}
			
			else if(AuditTestConstants.PAN_CSV_DEVICE_ID_1606015019_SHEET.contains(deviceId)){
				reportObject = AuditTestUtils.populateMultipleDevicesActualAuditReport(restClient,AuditTestUtils.getAuditMultipleDeviceidsSummaryPayloadLatest(AuditTestConstants.PAN_CSV_DEVICE_ID_1606015019_SHEET,sourceID,deviceId,"86400","1378512000","1381103999") );
				validatePartialAuditReportObject(reportObject);
				//controller=new AuditGoldenSetDataController(AuditTestConstants.CWS_DEVICEID_TSV_1234567880_SHEET);
			}
			else if(AuditTestConstants.PAN_CSV_DEVICE_ID_1606015401_SHEET.contains(deviceId)){
				reportObject = AuditTestUtils.populateMultipleDevicesActualAuditReport(restClient,AuditTestUtils.getAuditMultipleDeviceidsSummaryPayloadLatest(AuditTestConstants.PAN_CSV_DEVICE_ID_1606015401_SHEET,sourceID,deviceId,"86400","1378512000","1381103999") );
				validatePartialAuditReportObject(reportObject);
				//controller=new AuditGoldenSetDataController(AuditTestConstants.CWS_DEVICEID_TSV_1234567860_SHEET);
			}
			else if(AuditTestConstants.PAN_CSV_DEVICE_ID_1606015801_SHEET.contains(deviceId)){
				reportObject = AuditTestUtils.populateMultipleDevicesActualAuditReport(restClient,AuditTestUtils.getAuditMultipleDeviceidsSummaryPayloadLatest(AuditTestConstants.PAN_CSV_DEVICE_ID_1606015801_SHEET,sourceID,deviceId,"86400","1378512000","1381103999") );
				validatePartialAuditReportObject(reportObject);
				//controller=new AuditGoldenSetDataController(AuditTestConstants.CWS_DEVICEID_TSV_1234567870_SHEET);
			}
			else if(AuditTestConstants.PAN_CSV_DEVICE_ID_1606015601_SHEET.contains(deviceId)){
				reportObject = AuditTestUtils.populateMultipleDevicesActualAuditReport(restClient,AuditTestUtils.getAuditMultipleDeviceidsSummaryPayloadLatest(AuditTestConstants.PAN_CSV_DEVICE_ID_1606015601_SHEET,sourceID,deviceId,"86400","1378512000","1381103999") );
				validatePartialAuditReportObject(reportObject);
				//controller=new AuditGoldenSetDataController(AuditTestConstants.CWS_DEVICEID_TSV_1234567880_SHEET);
			}
			else if(AuditTestConstants.PAN_CSV_DEVICE_ID_1606015201_SHEET.contains(deviceId)){
				reportObject = AuditTestUtils.populateMultipleDevicesActualAuditReport(restClient,AuditTestUtils.getAuditMultipleDeviceidsSummaryPayloadLatest(AuditTestConstants.PAN_CSV_DEVICE_ID_1606015201_SHEET,sourceID,deviceId,"86400","1378512000","1381103999") );
				validatePartialAuditReportObject(reportObject);
				//controller=new AuditGoldenSetDataController(AuditTestConstants.CWS_DEVICEID_TSV_1234567860_SHEET);
			}
			
			else{
				reportObject = AuditTestUtils.populateMultipleDevicesActualAuditReport(restClient,AuditTestUtils.getAuditMultipleDeviceidsSummaryPayloadLatest(AuditTestConstants.PAN_CSV_DEVICE_ID_SHEET,sourceID,null,"86400","1378512000","1381103999") );
				Reporter.log("reportObject:"+reportObject,true);
				validatePartialAuditReportObject(reportObject);
				//controller=new AuditGoldenSetDataController(AuditTestConstants.PAN_CSV_DEVICE_ID_SHEET);
				//AuditTestUtils.auditSummaryGoldenSetData(controller.readExcelFileDataAndPrepareGoldenSetData(), summaryObject,fireWallType);
			}
			//AuditTestUtils.auditSummaryGoldenSetData(controller.readExcelFileDataAndPrepareGoldenSetData(), summaryObject,fireWallType);
			break;
		}
		default: {

			break;
		}
		}
		
	}	
	
	
	// * This test case deletes the data source
	 
	@Test(dependsOnMethods={"testAuditSummaryBySelectingMultipleDeviceIdsOfSingeDatasource"})
	public void deleteDataSourceTest() throws Exception {
		Logger.info("Deleting Data Source "+ fireWallType +" its ID is: "+sourceID);
		 if(!(suiteData.getApiserverHostName().contains("qa-vpc") && suiteData.getTenantName().contains("datasourcemulti"))){
		        
				HttpResponse response = AuditFunctions.deleteDataSource(restClient, sourceID);
				Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_NO_CONTENT);
				 }
	}
	
  public List<NameValuePair> getAuditSummaryQueryParams(String deviceLog,String sourceId,String deviceId) {
		

		String range = "1mo";			
		List<NameValuePair> queryParam = new ArrayList<NameValuePair>();
		queryParam.add(new BasicNameValuePair("format", "json"));
		queryParam.add(new BasicNameValuePair("range", range));
		queryParam.add(new BasicNameValuePair("allowed", "true"));
		queryParam.add(new BasicNameValuePair("blocked", "false"));
		
		if(AuditTestConstants.FIREWALL_CWS_MULTIPLE_DEVICE_IDS.equals(deviceLog) ||
				AuditTestConstants.FIREWALL_WSA_ACCESS_DEVICE_IDS.equals(deviceLog) ||
				AuditTestConstants.FIREWALL_WSA_W3C_DEVICE_IDS.equals(deviceLog) ||
				AuditTestConstants.FIREWALL_PAN_CSV_MULTIPLE_DEVICE_IDS.equals(deviceLog)||
				AuditTestConstants.FIREWALL_PAN_SYS_MULTIPLE_DEVICE_IDS.equals(deviceLog) ||
				AuditTestConstants.FIREWALL_BLUECOAT_PROXYSG_DEVICE_IDS.equals(deviceLog) 
				) 
		{
			queryParam.add(new BasicNameValuePair("ds_id", sourceId));
		}
		else{
			queryParam.add(new BasicNameValuePair("ds_id", sourceId));
			queryParam.add(new BasicNameValuePair("hardware_ids", deviceId));
		}
		return queryParam;
		
	}
	
  public JSONObject getAuditSummary(Client restClient, List<NameValuePair> queryParams) throws Exception
	{
		HttpResponse response=null;
		JSONObject auditSummaryObject=null;
		
	    response  = AuditFunctions.getAuditSummary(restClient, queryParams);				
		Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
		
		auditSummaryObject = (JSONObject) new JSONObject(ClientUtil.getResponseBody(response)).getJSONArray("objects").get(0);
		return auditSummaryObject;
		
	}
  
  public JSONObject getAuditReport(Client restClient, List<NameValuePair> queryParams) throws Exception
	{
		HttpResponse response=null;
		JSONObject auditReportObject=null;
		
	    response  = AuditFunctions.getAuditReport(restClient, queryParams);				
		Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
		
		auditReportObject = (JSONObject) new JSONObject(ClientUtil.getResponseBody(response)).getJSONArray("objects").get(0);
		return auditReportObject;
		
	}
   
   
   public void validatePartialAuditSummaryObject(JSONObject summaryObject) throws Exception
   {
	    //Assert.assertEquals(summaryObject.get("datasource_id"), sourceID);
		//Assert.assertEquals(summaryObject.get("date_range"), "1mo");
		Assert.assertNotNull(summaryObject.get("earliest_date"),"earliest date is null");		
		Assert.assertNotNull(summaryObject.get("latest_date"),"Latest date is null");	
   }
   
   public void validatePartialAuditReportObject(JSONObject reportObject) throws Exception
   {
	    //Assert.assertEquals(reportObject.get("datasource_id"), sourceID);
		//Assert.assertEquals(reportObject.get("date_range"), "1mo");
		Assert.assertNotNull(reportObject.get("earliest_date"),"earliest date is null");		
		Assert.assertNotNull(reportObject.get("generated_date"),"earliest date is null");		
		Assert.assertNotNull(reportObject.get("latest_date"),"Latest date is null");	
   }  
   /**
	 * This method will verify the Audit summary/Report results for un-processed logs
	 * @param sourceID
	 * @throws Exception
	 */
	private  void testVerifyAuditResultsNotProducedForUnProcessedLogs(String sourceID) throws Exception
	{
		HttpResponse response=null;
		
		String range = "1mo";			
		List<NameValuePair> queryParam = new ArrayList<NameValuePair>();				
		queryParam.add(new BasicNameValuePair("format", "json"));
		queryParam.add(new BasicNameValuePair("range", range));
		queryParam.add(new BasicNameValuePair("ds_id", sourceID));
		response  = AuditFunctions.getAuditSummary(restClient, queryParam);				
		Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
		
		//summary results verification
		JSONObject summaryObject = (JSONObject) new JSONObject(ClientUtil.getResponseBody(response)).getJSONArray("objects").get(0);				
		Assert.assertEquals(summaryObject.get("total_users"), 0, "From Summary: Total users should be ");
		Assert.assertEquals(summaryObject.get("total_destinations"), 0, "From Summary: Total Destinations should be ");
		Assert.assertEquals(summaryObject.get("total_services"), 0, "From Summary: Total Services should be ");
		Assert.assertEquals(summaryObject.get("high_risk_services"), 0, "From Summary: Total high_risk_services should be ");
		
		
		//report results verification
		
	    response  = AuditFunctions.getAuditReport(restClient, queryParam);		
		Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_OK);		
		JSONObject reportObject = (JSONObject) new JSONObject(ClientUtil.getResponseBody(response)).getJSONArray("objects").get(0);
		JSONObject totalReport = (JSONObject) reportObject.getJSONObject("total");
		Assert.assertEquals(totalReport.get("users"), 0, "From Report: users should be ");
		Assert.assertEquals(totalReport.get("sessions"), 0, "From Report: Sessions should be ");
		Assert.assertEquals(totalReport.get("services"), 0, "From Report: Services should be ");
		Assert.assertEquals(totalReport.get("traffic"), 0, "From Report: traffic should be ");
		Assert.assertEquals(totalReport.get("locations"), 0, "From Report: locations should be ");
		Assert.assertEquals(totalReport.get("categories"), 0, "From Report: categories should be ");
		Assert.assertEquals(totalReport.get("uploads"), 0, "From Report: uploads should be ");
		Assert.assertEquals(totalReport.get("downloads"), 0, "From Report: downloads should be ");
		Assert.assertEquals(totalReport.get("new_services"), 0, "From Report: new_services should be ");

	}	
	private void validateCreatedDataSource(JSONObject scpConnectionObject)
			throws Exception {
		Assert.assertEquals(scpConnectionObject.get("log_transport"), AuditTestConstants.AUDIT_SCP_FILEFORMAT);
		//Assert.assertEquals(scpConnectionObject.get("datasource_type"), dataSourceDTO.get("datasource_type"));
		Assert.assertNotNull(scpConnectionObject.get("id"), "Data Source Id is null");
		Assert.assertNotNull(scpConnectionObject.get("resource_uri"), "Resource URI is null");
		Assert.assertFalse(((String) scpConnectionObject.get("resource_uri")).isEmpty(), "resource URI is empty");
		Assert.assertNotNull(scpConnectionObject.get("setup_by"), "SetUp by is null");
		Assert.assertFalse(((String) scpConnectionObject.get("datasource_format")).isEmpty(),
				"Data source format is empty");
		Assert.assertFalse(((String) scpConnectionObject.get("datasource_type")).isEmpty(),
				"Data source type is empty");
		Assert.assertFalse(((String) scpConnectionObject.get("setup_by")).isEmpty(), "Set Up by is empty");
		Assert.assertEquals(scpConnectionObject.get("last_status"), "Pending Data",
				"Last status is not \"Pending Data\"");
		Assert.assertEquals(scpConnectionObject.get("last_detect_status"), "Pending Data",
				"Last status is not \"Pending Data\"");
	}
	
	/**
	 * validate getcredentials api
	 * @param scpCredentialsObject
	 * @throws Exception
	 */
	private void validatescpCrentials(JSONObject scpCredentialsObject) throws Exception
	{
		Assert.assertNotNull(scpCredentialsObject.get("id"), "Data Source Id is null");
		Assert.assertNotNull(scpCredentialsObject.get("username"), "scp server username is null");
		Assert.assertNotNull(scpCredentialsObject.get("password"), "scp server password is null");
		Assert.assertNotNull(scpCredentialsObject.get("created_on"), "scp server created on  is null");
		Assert.assertNotNull(scpCredentialsObject.get("modified_on"), "scp server modified on is null");
		Assert.assertNotNull(scpCredentialsObject.get("resource_uri"), "scp server getCredentials resource url is null");
		if(suiteData.getApiserverHostName().contains("qa-vpc")){
			Assert.assertEquals(scpCredentialsObject.get("server"), AuditTestConstants.AUDIT_QAVPC_SCP_SERVER);
		}
		else if(suiteData.getApiserverHostName().contains("api-vip.elastica.net")){
			Assert.assertEquals(scpCredentialsObject.get("server"), AuditTestConstants.AUDIT_SCP_SERVER_PROD);
        }
		else if(suiteData.getApiserverHostName().contains("api-cep.elastica.net")){
			Assert.assertEquals(scpCredentialsObject.get("server"), AuditTestConstants.AUDIT_SCP_SERVER_CEP);
        }
		else{
			Assert.assertEquals(scpCredentialsObject.get("server"), AuditTestConstants.AUDIT_SCP_SERVER);
			
		}
		
		
	}	
	/**
	 * prepare sftpUtils configuration
	 * @param sftpTenantUsername
	 * @param sftpServerHost
	 * @param sftpServerDestinationDir
	 * @return
	 */
	private SFTPUtils getSftpUtilsConfiguration(String sftpTenantUsername,String sftpServerHost, String sftpServerDestinationDir ) {
		SFTPUtils sftpUtils=new SFTPUtils();
		sftpUtils.setHostName(sftpServerHost);
        sftpUtils.setHostPort(AuditTestConstants.AUDIT_SCP_PORT);
        sftpUtils.setUserName(sftpTenantUsername);
        if(suiteData.getApiserverHostName().contains("qa-vpc") && suiteData.getTenantName().contains("vpcauditmultidevice")){
        	sftpUtils.setPassWord(AuditTestConstants.AUDIT_TENANT_VPC_MULTIDEVICEID_SCP_PWD);}
        else if(suiteData.getApiserverHostName().contains("qa-vpc") && suiteData.getTenantName().contains("datasourcemulti")){
        	sftpUtils.setPassWord(AuditTestConstants.AUDIT_TENANT_VPC_PWD1);}
        else if(suiteData.getApiserverHostName().contains("api-vip.elastica.net")){
        	sftpUtils.setPassWord(AuditTestConstants.AUDIT_TENANT_PROD_SCP_PWD);
        }
        else if(suiteData.getApiserverHostName().contains("api.eu.elastica.net")){
        	sftpUtils.setPassWord(AuditTestConstants.AUDIT_TENANT_CEP_SCP_PWD);
        }
        else{
        	sftpUtils.setPassWord(AuditTestConstants.AUDIT_SCP_DEVICEID_TENANT_PWD);
        }
        sftpUtils.setDestinationDir(sftpServerDestinationDir);
        return sftpUtils;
	}
	
	
	@AfterMethod
	public void testPopulateAuditSummaryFailures() throws Exception
	{
		if(!goldenSetErrorList.isEmpty()){
			Reporter.log("*****************Scp: Audit DeviceLogs Summary Validation Errors***********************",true);
			
		 for(String str: goldenSetErrorList)
		  {
			  Reporter.log(str,true);
		  }
		}
	}
	
	@AfterClass
	public void testPopulateIncompletedDataSourcesData() throws Exception
	{
		Reporter.log("===============SCP DeviceLogs: In-Complete Datasources Reached SLA Time Failure analysis================", true);
		if(!inCompleteDsList.isEmpty()){
			for(AuditDSStatusDTO dto:inCompleteDsList)
			{
				Reporter.log(""+dto,true);
			}
		}
		
	}
		
	
}
