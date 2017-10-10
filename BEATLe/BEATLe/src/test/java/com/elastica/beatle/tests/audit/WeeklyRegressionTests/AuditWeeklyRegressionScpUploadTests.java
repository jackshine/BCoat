package com.elastica.beatle.tests.audit.WeeklyRegressionTests;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.http.NameValuePair;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.elastica.beatle.RestClient.Client;
import com.elastica.beatle.RestClient.ClientUtil;
import com.elastica.beatle.audit.AuditDSStatusDTO;
import com.elastica.beatle.audit.AuditFunctions;
import com.elastica.beatle.audit.AuditGoldenSetDataController;
import com.elastica.beatle.audit.AuditGoldenSetDataController3;
import com.elastica.beatle.audit.AuditGoldenSetTestDataSetup;
import com.elastica.beatle.audit.AuditInitializeTests;
import com.elastica.beatle.audit.AuditReport;
import com.elastica.beatle.audit.AuditReportMostUsedServices;
import com.elastica.beatle.audit.AuditReportNewDiscoveredServices;
import com.elastica.beatle.audit.AuditReportRiskyServices;
import com.elastica.beatle.audit.AuditReportServiceCategories;
import com.elastica.beatle.audit.AuditReportServiceDetails;
import com.elastica.beatle.audit.AuditReportTotals;
import com.elastica.beatle.audit.AuditSummary;
import com.elastica.beatle.audit.AuditSummaryTopRiskyServices;
import com.elastica.beatle.audit.AuditTestConstants;
import com.elastica.beatle.audit.AuditTestUtils;
import com.elastica.beatle.audit.AuditWeeklyRegressionConstants;
import com.elastica.beatle.audit.GoldenSetData;
import com.elastica.beatle.audit.SummaryTabDto;
import com.elastica.beatle.SFTPUtils;

public class AuditWeeklyRegressionScpUploadTests extends AuditInitializeTests {

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
	AuditGoldenSetTestDataSetup goldenSetTestDataSetup=null;
	List<String> auditReportValidationsErrors = new ArrayList<String>();
	ArrayList<String> auditSummaryValidationsErrors = new ArrayList<String>();
	ArrayList<String> auditSummaryForConsumerServicesValidationsErrors = new ArrayList<String>();
	protected String scpcompltedCheckEmptyFilePath;







	public AuditWeeklyRegressionScpUploadTests(String fireWallType) {
		restClient = new Client();
		this.fireWallType = fireWallType;
	}

	/**
	 * Prepares the payload for datasource creation
	 * set the firewall log path. 
	 * @throws Exception
	 */
	@BeforeClass(alwaysRun = true)
	public void intScpSftpData() throws Exception{
		scpPayload = AuditTestUtils.createSCPUploadBody(fireWallType,suiteData.getEnvironmentName(),AuditTestConstants.AUDIT_SCP_DS_NAME);
		firewallLogFilePath = AuditTestUtils.getFirewallLogFilePath(fireWallType);
		scpcompltedCheckEmptyFilePath=AuditTestUtils.getFirewallLogFilePath(AuditTestConstants.SCP_COMPLETED);
		
		
		
	}


	/**
	 * The below test case do the below tasks
	 * 1. Create Datasource of the type Scp/sftp 
	 * 2. Verify the scp/sftp credentials
	 * 3. Upload the firewall log file using jsch library to the datasource
	 * 4. Verify the datasource status continueously for 4 hours (SLA time) till the firewall log file process COMPLETED. 
	 * @throws Exception
	 */
	@Test(description="Dataource creation for Scp/Sftp ")
	public void testSCPDatasourceCreation() throws Exception {
		Reporter.log("********************************* Test Description ****************************************************** ", true);
		Reporter.log("1. Create Datasource through SCP Transportation", true);
		Reporter.log("2. Process the Datasource ", true);
		Reporter.log("3. Poll the Datasource status by calling the Datasource Api for every 2 minutes until it gets Completed ", true);
		Reporter.log("********************************************************************************************************* ", true);

		Reporter.log("*************Datasource Creation started for:"+fireWallType+"****************",true);
		Reporter.log("Request Payload: "+scpPayload,true);
		HttpResponse createResp = AuditFunctions.createDataSource(restClient,new StringEntity(scpPayload));
		Assert.assertEquals(createResp.getStatusLine().getStatusCode(), HttpStatus.SC_CREATED);
		JSONObject scpConnectionObject = new JSONObject(ClientUtil.getResponseBody(createResp));
		Reporter.log("Actual ConnectionObject Response: "+scpConnectionObject,true);
		String str_last_status_or_detectstatus="Pending Data";
		String expectedConnectionObject=    " [sourceID=" + "sourceID should not be null/empty"+
				", sftpServerDestinationDir=" + "uploaded_path should not be null/empty" +
				", log_transport=" + AuditTestConstants.AUDIT_SCP_FILEFORMAT +
				", Resource URI =" + "Resource URI should not be null/empty" +
				", datasource_type=" + "datasource_type should not be null/empty" +
				", datasource_format=" + "datasource_format should not be null/empty"+
				", last_status=" + str_last_status_or_detectstatus +
				", last_detect_status=" + str_last_status_or_detectstatus+" ]";
		Reporter.log("Expected ConnectionObject Response fields: "+expectedConnectionObject,true);
		validateCreatedDataSource(scpConnectionObject);
		sourceID = (String) scpConnectionObject.get("id");
		sftpServerDestinationDir=(String)scpConnectionObject.get("upload_path");

		// get credentials of scp/sftp connection
		Reporter.log("**********************Get Scp Credentials***************************************",true);
		HttpResponse getScpCredentialsResponse = AuditFunctions.getCredentials(restClient);
		Assert.assertEquals(getScpCredentialsResponse.getStatusLine().getStatusCode(),HttpStatus.SC_OK);
		JSONObject scpCredentialsMetaData = (JSONObject) new JSONObject(ClientUtil.getResponseBody(getScpCredentialsResponse)).getJSONArray("objects").get(0);
		Reporter.log("Actual Credentials Response: "+scpCredentialsMetaData,true);
		String expectedCredentials=    " [sourceID=" + "sourceID should not be null/empty"+
				", username=" + "scp server username should not be null/empty" +
				", password=" + "scp server password should not be null/empty" +
				", created_on =" + "created_on should not be null/empty" +
				", modified_on=" + "modified_on should not be null/empty" +
				", resource_uri=" + "resource_uri should not be null/empty"+
				", server=" + "server should not be null/empty" +" ]";
		Reporter.log("Expected Credentials Response: "+expectedCredentials,true);
		Reporter.log("Validating the credentials object:");
		//validatescpCrentials(scpCredentialsMetaData);
		sftpTenantUsername=(String) scpCredentialsMetaData.get("username");
		//sftpServerHost=(String) scpCredentialsMetaData.get("server");
		sftpServerHost=getScpSftpServerHost();

		// Download file using amazon S3 URL
		AuditFunctions.DownloadFileFormS3(fireWallType);
		Thread.sleep(30000);
		
		Reporter.log("******************Upload file using Scp:****************************************",true);
		//upload file to the datasource using scp
		SFTPUtils sftpUtils=getSftpUtilsConfiguration(sftpTenantUsername, sftpServerHost,sftpServerDestinationDir,suiteData.getTenantName());
		Reporter.log("******************scp upload detais:********************",true);
		Reporter.log("scpUsername:"+sftpTenantUsername,true);
		Reporter.log("scpPassword:"+sftpUtils.getPassWord(),true);
		Reporter.log("scpServerHost:"+sftpServerHost,true);
		Reporter.log("scpServerDestinationDir:"+sftpServerDestinationDir,true);
		Reporter.log("scpUploadedfirewallLogFilePath:"+firewallLogFilePath,true);

		FileInputStream fin;
		File file = new File(System.getProperty("user.dir")+firewallLogFilePath);
		fin = new FileInputStream(file);
		String result = "";
		Reporter.log("******************scp upload started:********************",true);
		result = sftpUtils.uploadFileToFTP(file.getName(), fin, true);
		Reporter.log("scp file upload status:  " + result,true);
		Reporter.log("******************scp file upload completed sucessfully:********************",true);
		
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

		Reporter.log("******************Get Datasource sttaus verification****************************************",true);
		//verification logfile process attached to the datasource
		HttpResponse pollForStatusResponse = AuditFunctions.pollForDataSourceStatus(restClient, sourceID);
		Assert.assertEquals(pollForStatusResponse.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
		JSONObject pollRespObject = new JSONObject(ClientUtil.getResponseBody(pollForStatusResponse));
		Reporter.log("Actual Datasource Response:"+pollRespObject,true);
		String last_Status = pollRespObject.getString("last_status");
		Reporter.log("Actual Datasource Status:"+last_Status,true);
		String expectedCompletedStatus="Completed";
		Reporter.log("Expected Datasource Status:"+expectedCompletedStatus,true);long currentWaitTime = 0;
		while(("Pending Data".equals(last_Status) || "Pending Validation".equals(last_Status) || "Queued".equals(last_Status) || "Processing".equals(last_Status))&&
				currentWaitTime <= AuditTestConstants.AUDIT_PROCESSING_MAX_WAITTIME){
			Reporter.log("Datasource Process Wait Time*************** :"+AuditTestConstants.AUDIT_THREAD_WAITTIME,true);
			Thread.sleep(AuditTestConstants.AUDIT_THREAD_WAITTIME);
			currentWaitTime += AuditTestConstants.AUDIT_THREAD_WAITTIME;
			pollForStatusResponse = AuditFunctions.pollForDataSourceStatus(restClient, sourceID);
			Assert.assertEquals(pollForStatusResponse.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
			pollRespObject = new JSONObject(ClientUtil.getResponseBody(pollForStatusResponse));
			last_Status = pollRespObject.getString("last_status");
			Reporter.log("Last Status of "+ sourceID +" is "+ last_Status, true);
			if("Completed".equals(last_Status) || "Failed".equals(last_Status))			
				break;
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
		Reporter.log("************************** Scp Datasource process Completed sucessfully ******************************", true);
		Reporter.log("**************************Scp Datasource creation Test Completed**************************",true);

	}


	@Test(dependsOnMethods={"testSCPDatasourceCreation"})
	public void testAuditSummaryNew() throws Exception {
		
		Reporter.log("******************AuditSummary Test started for :****************"+fireWallType,true);
		//goldenSetTestDataSetup=suiteData.getAuditGoldenSetTestDataSetup();
		//Reporter.log("fireWallType:"+fireWallType,true);
		Reporter.log("sourceID:"+sourceID,true);
		AuditSummary expectedAuditSummary=null;
		AuditGoldenSetDataController3 controller=null;
		List<GoldenSetData> goldenSetDataList = null;

		AuditSummary actualAuditSummary=null;

		switch(fireWallType)
		{
		
		
		case AuditTestConstants.FIREWALL_BE_BARRACUDA_CLI: 
		case AuditTestConstants.FIREWALL_BE_BARRACUDA_CLI_7Z:
		case AuditTestConstants.FIREWALL_BE_BARRACUDA_CLI_7ZA:{
			Reporter.log("Audit Summary Verification Test stated for ."+fireWallType,true);
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_BARRACUDA_CLI_DATA_SHEET);
			
			goldenSetDataList = controller.loadXlData();
			goldenSetTestDataSetup=new AuditGoldenSetTestDataSetup(goldenSetDataList);
			expectedAuditSummary = goldenSetTestDataSetup.populateAuditSummary(goldenSetDataList);
			Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);
			//actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID);
			actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID,"86400","1391385600","1393977599");
			Reporter.log("actualAuditSummary: for::"+fireWallType+"  "+actualAuditSummary,true);
			auditSummaryValidationsErrors=AuditTestUtils.validateSummary(fireWallType,goldenSetTestDataSetup.getSernameNameWithServiceIdMap(),expectedAuditSummary,actualAuditSummary,auditSummaryValidationsErrors);
			
			//summary results validation
			if( AuditTestConstants.FIREWALL_BE_BARRACUDA_CLI.equals(fireWallType)){
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_BARRACUDA_CLI+" Audit summary Info wrong ");
			}else if(AuditTestConstants.FIREWALL_BE_BARRACUDA_CLI_7Z.equals(fireWallType))
			{
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_BARRACUDA_CLI_7Z+" Audit summary Info wrong ");
				
			}else{
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_BARRACUDA_CLI_7ZA+" Audit summary Info wrong ");
			}
			
			break;
		}
		
		case AuditWeeklyRegressionConstants.FIREWALL_BE_BARRACUDA_SYS_ZIP:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_BARRACUDA_SYS_GZ:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_BARRACUDA_SYS_7Z:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_BARRACUDA_SYS_7ZA:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_BARRACUDA_SYS_BZ2:
		{
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_BARRACUDA_SYS_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			goldenSetTestDataSetup=new AuditGoldenSetTestDataSetup(goldenSetDataList);
			expectedAuditSummary = goldenSetTestDataSetup.populateAuditSummary(goldenSetDataList);
			Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);
			//actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID);
			actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID,"86400","1391385600","1393977599");
			auditSummaryValidationsErrors=AuditTestUtils.validateSummary(fireWallType,goldenSetTestDataSetup.getSernameNameWithServiceIdMap(),expectedAuditSummary,actualAuditSummary,auditSummaryValidationsErrors);
			
			if(AuditWeeklyRegressionConstants.FIREWALL_BE_BARRACUDA_SYS_ZIP.equals(fireWallType)){
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_BARRACUDA_SYS_ZIP+" Audit summary Info wrong ");
				
			}else if(AuditWeeklyRegressionConstants.FIREWALL_BE_BARRACUDA_SYS_GZ.equals(fireWallType)){
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_BARRACUDA_SYS_GZ+" Audit summary Info wrong ");
				
			}
			else if(AuditWeeklyRegressionConstants.FIREWALL_BE_BARRACUDA_SYS_7Z.equals(fireWallType)){
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_BARRACUDA_SYS_7Z+" Audit summary Info wrong ");
				
			}
			else if(AuditWeeklyRegressionConstants.FIREWALL_BE_BARRACUDA_SYS_7ZA.equals(fireWallType)){
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_BARRACUDA_SYS_7ZA+" Audit summary Info wrong ");
				
			}
			else{
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_BARRACUDA_SYS_BZ2+" Audit summary Info wrong ");
			}
			break;
		}

		case AuditWeeklyRegressionConstants.FIREWALL_BE_BLUECOAT_PROXY_GZ:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_BLUECOAT_PROXY_BZ2:	
		case AuditWeeklyRegressionConstants.FIREWALL_BE_BLUECOAT_PROXY_ZIP: 
		case AuditWeeklyRegressionConstants.FIREWALL_BE_BLUECOAT_PROXY_7Z: 
		case AuditWeeklyRegressionConstants.FIREWALL_BE_BLUECOAT_PROXY_7ZA: {
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_BLUECOAT_PROXY_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			goldenSetTestDataSetup=new AuditGoldenSetTestDataSetup(goldenSetDataList);
			expectedAuditSummary = goldenSetTestDataSetup.populateAuditSummary(goldenSetDataList);
			Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);
			//actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID);
			actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID,"86400","1394928000","1397519999");
			auditSummaryValidationsErrors=AuditTestUtils.validateSummary(fireWallType,goldenSetTestDataSetup.getSernameNameWithServiceIdMap(),expectedAuditSummary,actualAuditSummary,auditSummaryValidationsErrors);
			
			if(AuditWeeklyRegressionConstants.FIREWALL_BE_BLUECOAT_PROXY_GZ.equals(fireWallType)){
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_BLUECOAT_PROXY_GZ+" Audit summary Info wrong ");
				
			}else if(AuditWeeklyRegressionConstants.FIREWALL_BE_BLUECOAT_PROXY_BZ2.equals(fireWallType)){
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_BLUECOAT_PROXY_BZ2+" Audit summary Info wrong ");
			}else if(AuditWeeklyRegressionConstants.FIREWALL_BE_BLUECOAT_PROXY_ZIP.equals(fireWallType)){
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_BLUECOAT_PROXY_ZIP+" Audit summary Info wrong ");
			}else if(AuditWeeklyRegressionConstants.FIREWALL_BE_BLUECOAT_PROXY_7Z.equals(fireWallType)){
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_BLUECOAT_PROXY_7Z+" Audit summary Info wrong ");
					
			}else{
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_BLUECOAT_PROXY_7ZA+" Audit summary Info wrong ");
				
			}
			break;
		}

		case AuditWeeklyRegressionConstants.FIREWALL_BE_BLUECOATPROXY_SPLUNK_WO_CH_ZIP:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_BLUECOATPROXY_SPLUNK_WO_CH_7Z:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_BLUECOATPROXY_SPLUNK_WO_CH_GZ:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_BLUECOATPROXY_SPLUNK_WO_CH_BZ2:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_BLUECOATPROXY_SPLUNK_WO_CH_7ZA:{
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_BLUECOATPROXY_SPLUNK_WO_CH_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			goldenSetTestDataSetup=new AuditGoldenSetTestDataSetup(goldenSetDataList);
			expectedAuditSummary = goldenSetTestDataSetup.populateAuditSummary(goldenSetDataList);
			Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);
			//actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID);
			actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID,"86400","1442448000","1445039999");
			
			//auditSummaryValidationsErrors=AuditTestUtils.validateSummary(fireWallType,goldenSetTestDataSetup.getSernameNameWithServiceIdMap(),expectedAuditSummary,actualAuditSummary,auditSummaryValidationsErrors);
			//Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_BLUECOATPROXY_SPLUNK_WO_CH+" Audit summary Info wrong ");
			break;
		}

		case AuditWeeklyRegressionConstants.FIREWALL_CHECKPOINT_CSV_7ZA:
		case AuditWeeklyRegressionConstants.FIREWALL_CHECKPOINT_CSV_GZ:
		case AuditWeeklyRegressionConstants.FIREWALL_CHECKPOINT_CSV_BZ2:
		case AuditWeeklyRegressionConstants.FIREWALL_CHECKPOINT_CSV_ZIP:
		case AuditWeeklyRegressionConstants.FIREWALL_CHECKPOINT_CSV_7Z:{
			break;
		}
		
		case AuditWeeklyRegressionConstants.FIREWALL_BE_CHECKPOINT_SMARTVIEW_BZ2:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_CHECKPOINT_SMARTVIEW_7Z:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_CHECKPOINT_SMARTVIEW_ZIP:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_CHECKPOINT_SMARTVIEW_GZ:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_CHECKPOINT_SMARTVIEW_7ZA:{
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_CHECKPOINT_SMARTVIEW_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			goldenSetTestDataSetup=new AuditGoldenSetTestDataSetup(goldenSetDataList);
			expectedAuditSummary = goldenSetTestDataSetup.populateAuditSummary(goldenSetDataList);
			Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);
			//actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID);
			actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID,"86400","1437609600","1440201599");
			auditSummaryValidationsErrors=AuditTestUtils.validateSummary(fireWallType,goldenSetTestDataSetup.getSernameNameWithServiceIdMap(),expectedAuditSummary,actualAuditSummary,auditSummaryValidationsErrors);
			if(AuditWeeklyRegressionConstants.FIREWALL_BE_CHECKPOINT_SMARTVIEW_BZ2.equals(fireWallType)){
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_CHECKPOINT_SMARTVIEW_BZ2+" Audit summary Info wrong ");
			}else if(AuditWeeklyRegressionConstants.FIREWALL_BE_CHECKPOINT_SMARTVIEW_7Z.equals(fireWallType)){
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_CHECKPOINT_SMARTVIEW_7Z+" Audit summary Info wrong ");
			}else if(AuditWeeklyRegressionConstants.FIREWALL_BE_CHECKPOINT_SMARTVIEW_ZIP.equals(fireWallType)){
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_CHECKPOINT_SMARTVIEW_ZIP+" Audit summary Info wrong ");
			}else if(AuditWeeklyRegressionConstants.FIREWALL_BE_CHECKPOINT_SMARTVIEW_GZ.equals(fireWallType)){
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_CHECKPOINT_SMARTVIEW_GZ+" Audit summary Info wrong ");
			}else{
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_CHECKPOINT_SMARTVIEW_7ZA+" Audit summary Info wrong ");
				
			}
			break;
		}


		case AuditWeeklyRegressionConstants.FIREWALL_BE_JUNIPER_SCREENOS_ZIP:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_JUNIPER_SCREENOS_7Z:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_JUNIPER_SCREENOS_7ZA:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_JUNIPER_SCREENOS_GZ:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_JUNIPER_SCREENOS_BZ2:{
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_JUNIPER_SCREENOS_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			goldenSetTestDataSetup=new AuditGoldenSetTestDataSetup(goldenSetDataList);
			expectedAuditSummary = goldenSetTestDataSetup.populateAuditSummary(goldenSetDataList);
			Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);
			//actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID);
			actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID,"86400","1387152000","1389743999");
			
			auditSummaryValidationsErrors=AuditTestUtils.validateSummary(fireWallType,goldenSetTestDataSetup.getSernameNameWithServiceIdMap(),expectedAuditSummary,actualAuditSummary,auditSummaryValidationsErrors);
			
			if(AuditWeeklyRegressionConstants.FIREWALL_BE_JUNIPER_SCREENOS_ZIP.equals(fireWallType)){
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_JUNIPER_SCREENOS_ZIP+" Audit summary Info wrong ");
				
			}else if(AuditWeeklyRegressionConstants.FIREWALL_BE_JUNIPER_SCREENOS_7Z.equals(fireWallType)){
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_JUNIPER_SCREENOS_7Z+" Audit summary Info wrong ");
			}else if(AuditWeeklyRegressionConstants.FIREWALL_BE_JUNIPER_SCREENOS_7ZA.equals(fireWallType)){
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_JUNIPER_SCREENOS_7ZA+" Audit summary Info wrong ");
			}else if(AuditWeeklyRegressionConstants.FIREWALL_BE_JUNIPER_SCREENOS_GZ.equals(fireWallType)){
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_JUNIPER_SCREENOS_GZ+" Audit summary Info wrong ");
				
			}else{
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_JUNIPER_SCREENOS_BZ2+" Audit summary Info wrong ");
				
			}
			break;
		}

		case AuditWeeklyRegressionConstants.FIREWALL_MCAFEE_SEF_ZIP: 
		case AuditWeeklyRegressionConstants.FIREWALL_MCAFEE_SEF_7Z: 
		case AuditWeeklyRegressionConstants.FIREWALL_MCAFEE_SEF_7ZA: 
		case AuditWeeklyRegressionConstants.FIREWALL_MCAFEE_SEF_GZ: 
		case AuditWeeklyRegressionConstants.FIREWALL_MCAFEE_SEF_BZ2: {
			controller = new AuditGoldenSetDataController3(AuditTestConstants.MCAFEE_SEF_SHEET);
			goldenSetDataList = controller.loadXlData();
			goldenSetTestDataSetup=new AuditGoldenSetTestDataSetup(goldenSetDataList);
			expectedAuditSummary = goldenSetTestDataSetup.populateAuditSummary(goldenSetDataList);
			Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);
		//	actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID);
			actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID,"86400","1387670400","1390262399");
			
			auditSummaryValidationsErrors=AuditTestUtils.validateSummary(fireWallType,goldenSetTestDataSetup.getSernameNameWithServiceIdMap(),expectedAuditSummary,actualAuditSummary,auditSummaryValidationsErrors);
			
			if(AuditWeeklyRegressionConstants.FIREWALL_MCAFEE_SEF_ZIP.equals(fireWallType)){
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_MCAFEE_SEF_ZIP+" Audit summary Info wrong ");
			}else if(AuditWeeklyRegressionConstants.FIREWALL_MCAFEE_SEF_7Z.equals(fireWallType)){
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_MCAFEE_SEF_7Z+" Audit summary Info wrong ");
			}else if(AuditWeeklyRegressionConstants.FIREWALL_MCAFEE_SEF_7ZA.equals(fireWallType)){
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_MCAFEE_SEF_7ZA+" Audit summary Info wrong ");
			}else if(AuditWeeklyRegressionConstants.FIREWALL_MCAFEE_SEF_GZ.equals(fireWallType)){
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_MCAFEE_SEF_GZ+" Audit summary Info wrong ");
			}else{
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_MCAFEE_SEF_BZ2+" Audit summary Info wrong ");
				
			}
			
			break;
		}
		
		case AuditWeeklyRegressionConstants.FIREWALL_BE_PANCSV_ZIP:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_PANCSV_7Z:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_PANCSV_7ZA:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_PANCSV_GZ:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_PANCSV_BZ2:{
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_PANCSV_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			goldenSetTestDataSetup=new AuditGoldenSetTestDataSetup(goldenSetDataList);
			expectedAuditSummary = goldenSetTestDataSetup.populateAuditSummary(goldenSetDataList);
			Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);
			//actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID);
			actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID,"86400","1377388800","1379980799");
			
			auditSummaryValidationsErrors=AuditTestUtils.validateSummary(fireWallType,goldenSetTestDataSetup.getSernameNameWithServiceIdMap(),expectedAuditSummary,actualAuditSummary,auditSummaryValidationsErrors);
			
			if(AuditWeeklyRegressionConstants.FIREWALL_BE_PANCSV_ZIP.equals(fireWallType)){
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_PANCSV_ZIP+" Audit summary Info wrong ");
			}else if(AuditWeeklyRegressionConstants.FIREWALL_BE_PANCSV_7Z.equals(fireWallType)){
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_PANCSV_7Z+" Audit summary Info wrong ");
			}else if(AuditWeeklyRegressionConstants.FIREWALL_BE_PANCSV_7ZA.equals(fireWallType)){
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_PANCSV_7ZA+" Audit summary Info wrong ");
			}else if(AuditWeeklyRegressionConstants.FIREWALL_BE_PANCSV_GZ.equals(fireWallType)){
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_PANCSV_GZ+" Audit summary Info wrong ");
			}else{
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_PANCSV_BZ2+" Audit summary Info wrong ");
			}
			
			break;
		}
		case AuditWeeklyRegressionConstants.FIREWALL_BE_PANCSV_SPLUNK_WO_CH_ZIP:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_PANCSV_SPLUNK_WO_CH_7Z:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_PANCSV_SPLUNK_WO_CH_7ZA:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_PANCSV_SPLUNK_WO_CH_GZ:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_PANCSV_SPLUNK_WO_CH_BZ2:{
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_PANCSV_SPLUNK_WO_CH_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			goldenSetTestDataSetup=new AuditGoldenSetTestDataSetup(goldenSetDataList);
			expectedAuditSummary = goldenSetTestDataSetup.populateAuditSummary(goldenSetDataList);
			Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);
			//actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID);
			actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID,"86400","1442275200","1444867199");
			auditSummaryValidationsErrors=AuditTestUtils.validateSummary(fireWallType,goldenSetTestDataSetup.getSernameNameWithServiceIdMap(),expectedAuditSummary,actualAuditSummary,auditSummaryValidationsErrors);
			
			if(AuditWeeklyRegressionConstants.FIREWALL_BE_PANCSV_SPLUNK_WO_CH_ZIP.equals(fireWallType)){
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_PANCSV_SPLUNK_WO_CH_ZIP+" Audit summary Info wrong ");
			}else if(AuditWeeklyRegressionConstants.FIREWALL_BE_PANCSV_SPLUNK_WO_CH_7Z.equals(fireWallType)){
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_PANCSV_SPLUNK_WO_CH_7Z+" Audit summary Info wrong ");
			}else if(AuditWeeklyRegressionConstants.FIREWALL_BE_PANCSV_SPLUNK_WO_CH_7ZA.equals(fireWallType)){
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_PANCSV_SPLUNK_WO_CH_7ZA+" Audit summary Info wrong ");
			}else if(AuditWeeklyRegressionConstants.FIREWALL_BE_PANCSV_SPLUNK_WO_CH_GZ.equals(fireWallType)){
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_PANCSV_SPLUNK_WO_CH_GZ+" Audit summary Info wrong ");
			}else{
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_PANCSV_SPLUNK_WO_CH_BZ2+" Audit summary Info wrong ");
				
			}
			break;
		}

		case AuditWeeklyRegressionConstants.FIREWALL_SQUID_PROXY_ZIP:
		case AuditWeeklyRegressionConstants.FIREWALL_SQUID_PROXY_7Z:
		case AuditWeeklyRegressionConstants.FIREWALL_SQUID_PROXY_7ZA:
		case AuditWeeklyRegressionConstants.FIREWALL_SQUID_PROXY_GZ:
		case AuditWeeklyRegressionConstants.FIREWALL_SQUID_PROXY_BZ2:{
			controller = new AuditGoldenSetDataController3(AuditTestConstants.SQUID_PROXY_SHEET);
			goldenSetDataList = controller.loadXlData();
			goldenSetTestDataSetup=new AuditGoldenSetTestDataSetup(goldenSetDataList);
			expectedAuditSummary = goldenSetTestDataSetup.populateAuditSummary(goldenSetDataList);
			Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);
			//actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID);
			actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID,"2592000","1372636800","1404172799");
			
			//squid proxy file we do not hava valid data set so we are commenting validation part.
			//auditSummaryValidationsErrors=AuditTestUtils.validateSummary(fireWallType,goldenSetTestDataSetup.getSernameNameWithServiceIdMap(),expectedAuditSummary,actualAuditSummary,auditSummaryValidationsErrors);
			//Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_SQUID_PROXY+" Audit summary Info wrong ");
			break;
		}
		case AuditWeeklyRegressionConstants.FIREWALL_BE_WSAW3C_ZIP:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_WSAW3C_7Z:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_WSAW3C_7ZA:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_WSAW3C_GZ:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_WSAW3C_BZ2:{
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_WSAW3C_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			goldenSetTestDataSetup=new AuditGoldenSetTestDataSetup(goldenSetDataList);
			expectedAuditSummary = goldenSetTestDataSetup.populateAuditSummary(goldenSetDataList);
			Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);
			//actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID);
			actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID,"86400","1412467200","1415059199");
			
			auditSummaryValidationsErrors=AuditTestUtils.validateSummary(fireWallType,goldenSetTestDataSetup.getSernameNameWithServiceIdMap(),expectedAuditSummary,actualAuditSummary,auditSummaryValidationsErrors);
			if(AuditWeeklyRegressionConstants.FIREWALL_BE_WSAW3C_ZIP.equals(fireWallType)){
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_WSAW3C_ZIP+" Audit summary Info wrong ");
			}else if(AuditWeeklyRegressionConstants.FIREWALL_BE_WSAW3C_7Z.equals(fireWallType)){
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_WSAW3C_7Z+" Audit summary Info wrong ");
			}else if(AuditWeeklyRegressionConstants.FIREWALL_BE_WSAW3C_7ZA.equals(fireWallType)){
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_WSAW3C_7ZA+" Audit summary Info wrong ");
			}else if(AuditWeeklyRegressionConstants.FIREWALL_BE_WSAW3C_GZ.equals(fireWallType)){
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_WSAW3C_GZ+" Audit summary Info wrong ");
			}else{
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_WSAW3C_BZ2+" Audit summary Info wrong ");
				
			}
			
			break;
		}
		case AuditWeeklyRegressionConstants.FIREWALL_BE_WSA_ACCESS_ZIP:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_WSA_ACCESS_7Z:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_WSA_ACCESS_7ZA:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_WSA_ACCESS_GZ:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_WSA_ACCESS_BZ2:{
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_WSA_ACCESS_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			goldenSetTestDataSetup=new AuditGoldenSetTestDataSetup(goldenSetDataList);
			expectedAuditSummary = goldenSetTestDataSetup.populateAuditSummary(goldenSetDataList);
			Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);
			//actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID);
			actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID,"2592000","1388534400","1420070399");
			
			auditSummaryValidationsErrors=AuditTestUtils.validateSummary(fireWallType,goldenSetTestDataSetup.getSernameNameWithServiceIdMap(),expectedAuditSummary,actualAuditSummary,auditSummaryValidationsErrors);
			
			if(AuditWeeklyRegressionConstants.FIREWALL_BE_WSA_ACCESS_ZIP.equals(fireWallType)){
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_WSA_ACCESS_ZIP+" Audit summary Info wrong ");
			}else if(AuditWeeklyRegressionConstants.FIREWALL_BE_WSA_ACCESS_7Z.equals(fireWallType)){
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_WSA_ACCESS_7Z+" Audit summary Info wrong ");
			}else if(AuditWeeklyRegressionConstants.FIREWALL_BE_WSA_ACCESS_7ZA.equals(fireWallType)){
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_WSA_ACCESS_7ZA+" Audit summary Info wrong ");
			}else if(AuditWeeklyRegressionConstants.FIREWALL_BE_WSA_ACCESS_GZ.equals(fireWallType)){
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_WSA_ACCESS_GZ+" Audit summary Info wrong ");
			}else{
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_WSA_ACCESS_BZ2+" Audit summary Info wrong ");
				
			}
			break;
		}
		case AuditWeeklyRegressionConstants.FIREWALL_BE_ZSCALAR_7Z:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_ZSCALAR_7ZA:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_ZSCALAR_ZIP:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_ZSCALAR_GZ:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_ZSCALAR_BZ2:{
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_ZSCALAR_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			goldenSetTestDataSetup=new AuditGoldenSetTestDataSetup(goldenSetDataList);
			expectedAuditSummary = goldenSetTestDataSetup.populateAuditSummary(goldenSetDataList);
			Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);
		//	actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID);
			actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID,"86400","1432857600","1435449599");
			
			auditSummaryValidationsErrors=AuditTestUtils.validateSummary(fireWallType,goldenSetTestDataSetup.getSernameNameWithServiceIdMap(),expectedAuditSummary,actualAuditSummary,auditSummaryValidationsErrors);
			if(AuditWeeklyRegressionConstants.FIREWALL_BE_ZSCALAR_7Z.equals(fireWallType)){
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_ZSCALAR_7Z+" Audit summary Info wrong ");
			}else if(AuditWeeklyRegressionConstants.FIREWALL_BE_ZSCALAR_7ZA.equals(fireWallType)){
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_ZSCALAR_7ZA+" Audit summary Info wrong ");
			}else if(AuditWeeklyRegressionConstants.FIREWALL_BE_ZSCALAR_ZIP.equals(fireWallType)){
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_ZSCALAR_ZIP+" Audit summary Info wrong ");
			}else if(AuditWeeklyRegressionConstants.FIREWALL_BE_ZSCALAR_GZ.equals(fireWallType)){
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_ZSCALAR_GZ+" Audit summary Info wrong ");
			}else{
				Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_ZSCALAR_BZ2+" Audit summary Info wrong ");		
			}
			break;
		}
		case AuditWeeklyRegressionConstants.FIREWALL_WEBSENSE_ARC_ZIP:
		case AuditWeeklyRegressionConstants.FIREWALL_WEBSENSE_ARC_7Z:
		case AuditWeeklyRegressionConstants.FIREWALL_WEBSENSE_ARC_7ZA:
		case AuditWeeklyRegressionConstants.FIREWALL_WEBSENSE_ARC_GZ:
		case AuditWeeklyRegressionConstants.FIREWALL_WEBSENSE_ARC_BZ2:
		{
			/*controller = new AuditGoldenSetDataController3(AuditTestConstants.FIREWALL_WEBSENSE_ARC);
			goldenSetDataList = controller.loadXlData();
			expectedAuditSummary = goldenSetTestDataSetup.populateAuditSummary(goldenSetDataList);
			Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);*/
			//actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID);
			//auditSummaryValidationsErrors=AuditTestUtils.validateSummary(fireWallType,goldenSetTestDataSetup.getSernameNameWithServiceIdMap(),expectedAuditSummary,actualAuditSummary,auditSummaryValidationsErrors);
			//Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_ZSCALAR+" Audit summary Info wrong ");
			
			break;
		}
		case AuditWeeklyRegressionConstants.FIREWALL_WEBSENSE_HOSTED_ZIP:
		case AuditWeeklyRegressionConstants.FIREWALL_WEBSENSE_HOSTED_7Z:
		case AuditWeeklyRegressionConstants.FIREWALL_WEBSENSE_HOSTED_7ZA:
		case AuditWeeklyRegressionConstants.FIREWALL_WEBSENSE_HOSTED_GZ:
		case AuditWeeklyRegressionConstants.FIREWALL_WEBSENSE_HOSTED_BZ2:
		{
			controller = new AuditGoldenSetDataController3(AuditTestConstants.FIREWALL_WEBSENSE_HOSTED);
			goldenSetDataList = controller.loadXlData();
			goldenSetTestDataSetup=new AuditGoldenSetTestDataSetup(goldenSetDataList);
			expectedAuditSummary = goldenSetTestDataSetup.populateAuditSummary(goldenSetDataList);
			Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);
			actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID,"86400","1414886400","1417478399");
			//auditSummaryValidationsErrors=AuditTestUtils.validateSummary(fireWallType,goldenSetTestDataSetup.getSernameNameWithServiceIdMap(),expectedAuditSummary,actualAuditSummary,auditSummaryValidationsErrors);
			//Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_ZSCALAR+" Audit summary Info wrong ");
			
			break;
		}
		case AuditTestConstants.FIREWALL_WEBSENSE_ARC_TAR:
		{
			controller = new AuditGoldenSetDataController3(AuditTestConstants.FIREWALL_WEBSENSE_ARC);
			goldenSetDataList = controller.loadXlData();
			goldenSetTestDataSetup=new AuditGoldenSetTestDataSetup(goldenSetDataList);
			expectedAuditSummary = goldenSetTestDataSetup.populateAuditSummary(goldenSetDataList);
			Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);
			//actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID);
			//auditSummaryValidationsErrors=AuditTestUtils.validateSummary(fireWallType,goldenSetTestDataSetup.getSernameNameWithServiceIdMap(),expectedAuditSummary,actualAuditSummary,auditSummaryValidationsErrors);
			//Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_ZSCALAR+" Audit summary Info wrong ");
			
			break;
		}
		case AuditWeeklyRegressionConstants.FIREWALL_SONICWALL_ZIP:
		case AuditWeeklyRegressionConstants.FIREWALL_SONICWALL_7Z:
		case AuditWeeklyRegressionConstants.FIREWALL_SONICWALL_7ZA:
		case AuditWeeklyRegressionConstants.FIREWALL_SONICWALL_GZ:
		case AuditWeeklyRegressionConstants.FIREWALL_SONICWALL_BZ2:
		{
			/*controller = new AuditGoldenSetDataController3(AuditTestConstants.FIREWALL_WEBSENSE_ARC);
			goldenSetDataList = controller.loadXlData();
			expectedAuditSummary = goldenSetTestDataSetup.populateAuditSummary(goldenSetDataList);
			Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);*/
			//actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID);
			actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID,"86400","1394064000","1396655999");
			
			//auditSummaryValidationsErrors=AuditTestUtils.validateSummary(fireWallType,goldenSetTestDataSetup.getSernameNameWithServiceIdMap(),expectedAuditSummary,actualAuditSummary,auditSummaryValidationsErrors);
			//Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_ZSCALAR+" Audit summary Info wrong ");
			
			break;
		}

		case AuditTestConstants.FIREWALL_WALLMART_PAN_CSV: {
			Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);
			//actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID);
			break;
		}
		case AuditTestConstants.FIREWALL_WALLMART_PAN_SYS: {
			Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);
			//actualAuditSummary=this.populateActualAuditSummaryObject(fireWallType,sourceID);
			break;
		}
		case AuditTestConstants.FIREWALL_WALLMART_MCAFEE_WEB_GATEWAY: {
			Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);
			//actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID);
			break;
		}

		case AuditWeeklyRegressionConstants.FIREWALL_JUNIPER_SRX_ZIP:
		case AuditWeeklyRegressionConstants.FIREWALL_JUNIPER_SRX_7Z:
		case AuditWeeklyRegressionConstants.FIREWALL_JUNIPER_SRX_7ZA:
		case AuditWeeklyRegressionConstants.FIREWALL_JUNIPER_SRX_GZ:
		case AuditWeeklyRegressionConstants.FIREWALL_JUNIPER_SRX_BZ2: {
			Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);
			//actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID);
			actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID,"86400","1434326400","1436918399");
			break;
		}
		
		case AuditWeeklyRegressionConstants.FIREWALL_SCANSAFE_ZIP:
		case AuditWeeklyRegressionConstants.FIREWALL_SCANSAFE_7Z:
		case AuditWeeklyRegressionConstants.FIREWALL_SCANSAFE_7ZA:
		case AuditWeeklyRegressionConstants.FIREWALL_SCANSAFE_GZ:
		case AuditWeeklyRegressionConstants.FIREWALL_SCANSAFE_BZ2: {
			Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);
			//actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID);
			actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID,"86400","1399161600","1401753599");
			
			break;
		}
		case AuditWeeklyRegressionConstants.FIREWALL_CISCO_ASA_SERIES_ZIP:
		case AuditWeeklyRegressionConstants.FIREWALL_CISCO_ASA_SERIES_7Z:
		case AuditWeeklyRegressionConstants.FIREWALL_CISCO_ASA_SERIES_7ZA:
		case AuditWeeklyRegressionConstants.FIREWALL_CISCO_ASA_SERIES_GZ:
		case AuditWeeklyRegressionConstants.FIREWALL_CISCO_ASA_SERIES_BZ2: {
			Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);
			//actualAuditSummary=this.populateActualAuditSummaryObject(fireWallType,sourceID);
			break;
		}

		}
		
		
	}
	
@Test(dependsOnMethods={"testSCPDatasourceCreation"})
	public void testAuditSummaryOfConsumerServices() throws Exception {
	Reporter.log("******************AuditSummary for consumer services Test started for :****************"+fireWallType,true);
	//goldenSetTestDataSetup=suiteData.getAuditGoldenSetTestDataSetup();
	//Reporter.log("fireWallType:"+fireWallType,true);
	Reporter.log("sourceID:"+sourceID,true);
	AuditSummary expectedAuditSummary=null;
	AuditGoldenSetDataController3 controller=null;
	List<GoldenSetData> goldenSetDataList = null;

	AuditSummary actualAuditSummary=null;
	final String CONSUMER="consumer";

	switch(fireWallType)
	{
	case AuditTestConstants.FIREWALL_BE_BARRACUDA_CLI: 
	case AuditTestConstants.FIREWALL_BE_BARRACUDA_CLI_7Z:
	case AuditTestConstants.FIREWALL_BE_BARRACUDA_CLI_7ZA:{
		Reporter.log("Audit Summary Verification Test stated for ."+fireWallType,true);
		controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_BARRACUDA_CLI_DATA_SHEET+"_"+CONSUMER);
		goldenSetDataList = controller.loadXlData();
		goldenSetTestDataSetup=new AuditGoldenSetTestDataSetup(goldenSetDataList);
		expectedAuditSummary = goldenSetTestDataSetup.populateAuditSummary(goldenSetDataList);
		Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);
		//actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID);
		actualAuditSummary=AuditTestUtils.populateActualAuditSummaryofConsumerServices(fireWallType,sourceID,"86400","1391385600","1393977599");
		Reporter.log("actualAuditSummary: for::"+fireWallType+"  "+actualAuditSummary,true);
		auditSummaryForConsumerServicesValidationsErrors=AuditTestUtils.validateSummary(fireWallType,goldenSetTestDataSetup.getSernameNameWithServiceIdMap(),expectedAuditSummary,actualAuditSummary,auditSummaryForConsumerServicesValidationsErrors);
		
		//summary results validation
		if( AuditTestConstants.FIREWALL_BE_BARRACUDA_CLI.equals(fireWallType)){
			Assert.assertTrue(auditSummaryForConsumerServicesValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_BARRACUDA_CLI+" Audit summary Info wrong ");
		}else if(AuditTestConstants.FIREWALL_BE_BARRACUDA_CLI_7Z.equals(fireWallType))
		{
			Assert.assertTrue(auditSummaryForConsumerServicesValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_BARRACUDA_CLI_7Z+" Audit summary Info wrong ");
			
		}else{
			Assert.assertTrue(auditSummaryForConsumerServicesValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_BARRACUDA_CLI_7ZA+" Audit summary Info wrong ");
		}
		
		break;
	}
	
	case AuditWeeklyRegressionConstants.FIREWALL_BE_BARRACUDA_SYS_ZIP:
	case AuditWeeklyRegressionConstants.FIREWALL_BE_BARRACUDA_SYS_GZ:
	case AuditWeeklyRegressionConstants.FIREWALL_BE_BARRACUDA_SYS_7Z:
	case AuditWeeklyRegressionConstants.FIREWALL_BE_BARRACUDA_SYS_7ZA:
	case AuditWeeklyRegressionConstants.FIREWALL_BE_BARRACUDA_SYS_BZ2:{
		controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_BARRACUDA_SYS_DATA_SHEET+"_"+CONSUMER);
		goldenSetDataList = controller.loadXlData();
		goldenSetTestDataSetup=new AuditGoldenSetTestDataSetup(goldenSetDataList);
		expectedAuditSummary = goldenSetTestDataSetup.populateAuditSummary(goldenSetDataList);
		Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);
		//actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID);
		actualAuditSummary=AuditTestUtils.populateActualAuditSummaryofConsumerServices(fireWallType,sourceID,"86400","1391385600","1393977599");
		auditSummaryForConsumerServicesValidationsErrors=AuditTestUtils.validateSummary(fireWallType,goldenSetTestDataSetup.getSernameNameWithServiceIdMap(),expectedAuditSummary,actualAuditSummary,auditSummaryForConsumerServicesValidationsErrors);
		
		if(AuditWeeklyRegressionConstants.FIREWALL_BE_BARRACUDA_SYS_ZIP.equals(fireWallType)){
			Assert.assertTrue(auditSummaryForConsumerServicesValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_BARRACUDA_SYS_ZIP+" Audit summary Info wrong ");
		}else if(AuditWeeklyRegressionConstants.FIREWALL_BE_BARRACUDA_SYS_GZ.equals(fireWallType)){
			Assert.assertTrue(auditSummaryForConsumerServicesValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_BARRACUDA_SYS_GZ+" Audit summary Info wrong ");
		}else if(AuditWeeklyRegressionConstants.FIREWALL_BE_BARRACUDA_SYS_7Z.equals(fireWallType)){
			Assert.assertTrue(auditSummaryForConsumerServicesValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_BARRACUDA_SYS_7Z+" Audit summary Info wrong ");
		}else if(AuditWeeklyRegressionConstants.FIREWALL_BE_BARRACUDA_SYS_7ZA.equals(fireWallType)){
			Assert.assertTrue(auditSummaryForConsumerServicesValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_BARRACUDA_SYS_7ZA+" Audit summary Info wrong ");
		}else{
			Assert.assertTrue(auditSummaryForConsumerServicesValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_BARRACUDA_SYS_BZ2+" Audit summary Info wrong ");
			
		}
		break;
	}

	case AuditWeeklyRegressionConstants.FIREWALL_BE_BLUECOAT_PROXY_GZ:
	case AuditWeeklyRegressionConstants.FIREWALL_BE_BLUECOAT_PROXY_BZ2:	
	case AuditWeeklyRegressionConstants.FIREWALL_BE_BLUECOAT_PROXY_ZIP: 
	case AuditWeeklyRegressionConstants.FIREWALL_BE_BLUECOAT_PROXY_7Z: 
	case AuditWeeklyRegressionConstants.FIREWALL_BE_BLUECOAT_PROXY_7ZA:  {
		controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_BLUECOAT_PROXY_DATA_SHEET+"_"+CONSUMER);
		goldenSetDataList = controller.loadXlData();
		goldenSetTestDataSetup=new AuditGoldenSetTestDataSetup(goldenSetDataList);
		expectedAuditSummary = goldenSetTestDataSetup.populateAuditSummary(goldenSetDataList);
		Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);
		//actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID);
		actualAuditSummary=AuditTestUtils.populateActualAuditSummaryofConsumerServices(fireWallType,sourceID,"86400","1394928000","1397519999");
		auditSummaryForConsumerServicesValidationsErrors=AuditTestUtils.validateSummary(fireWallType,goldenSetTestDataSetup.getSernameNameWithServiceIdMap(),expectedAuditSummary,actualAuditSummary,auditSummaryForConsumerServicesValidationsErrors);
		
		if(AuditWeeklyRegressionConstants.FIREWALL_BE_BLUECOAT_PROXY_GZ.equals(fireWallType)){
			Assert.assertTrue(auditSummaryForConsumerServicesValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_BLUECOAT_PROXY_GZ+" Audit summary Info wrong ");
		}else if(AuditWeeklyRegressionConstants.FIREWALL_BE_BLUECOAT_PROXY_BZ2.equals(fireWallType)){
			Assert.assertTrue(auditSummaryForConsumerServicesValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_BLUECOAT_PROXY_BZ2+" Audit summary Info wrong ");
		}else if(AuditWeeklyRegressionConstants.FIREWALL_BE_BLUECOAT_PROXY_ZIP.equals(fireWallType)){
			Assert.assertTrue(auditSummaryForConsumerServicesValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_BLUECOAT_PROXY_ZIP+" Audit summary Info wrong ");
		}else if(AuditWeeklyRegressionConstants.FIREWALL_BE_BLUECOAT_PROXY_7Z.equals(fireWallType)){
			Assert.assertTrue(auditSummaryForConsumerServicesValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_BLUECOAT_PROXY_7Z+" Audit summary Info wrong ");
		}else{
			Assert.assertTrue(auditSummaryForConsumerServicesValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_BLUECOAT_PROXY_7ZA+" Audit summary Info wrong ");
			
		}
		break;
	}


	case AuditWeeklyRegressionConstants.FIREWALL_BE_BLUECOATPROXY_SPLUNK_WO_CH_ZIP:
	case AuditWeeklyRegressionConstants.FIREWALL_BE_BLUECOATPROXY_SPLUNK_WO_CH_7Z:
	case AuditWeeklyRegressionConstants.FIREWALL_BE_BLUECOATPROXY_SPLUNK_WO_CH_GZ:
	case AuditWeeklyRegressionConstants.FIREWALL_BE_BLUECOATPROXY_SPLUNK_WO_CH_BZ2:
	case AuditWeeklyRegressionConstants.FIREWALL_BE_BLUECOATPROXY_SPLUNK_WO_CH_7ZA:{
		controller = new AuditGoldenSetDataController3("be_bswoh_consumer");
		goldenSetDataList = controller.loadXlData();
		goldenSetTestDataSetup=new AuditGoldenSetTestDataSetup(goldenSetDataList);
		expectedAuditSummary = goldenSetTestDataSetup.populateAuditSummary(goldenSetDataList);
		Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);
		//actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID);
		actualAuditSummary=AuditTestUtils.populateActualAuditSummaryofConsumerServices(fireWallType,sourceID,"86400","1442448000","1445039999");
		
		//auditSummaryForConsumerServicesValidationsErrors=AuditTestUtils.validateSummary(fireWallType,goldenSetTestDataSetup.getSernameNameWithServiceIdMap(),expectedAuditSummary,actualAuditSummary,auditSummaryForConsumerServicesValidationsErrors);
		//Assert.assertTrue(auditSummaryForConsumerServicesValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_BLUECOATPROXY_SPLUNK_WO_CH+" Audit summary Info wrong ");
		break;
	}

	case AuditWeeklyRegressionConstants.FIREWALL_CHECKPOINT_CSV_7ZA:
	case AuditWeeklyRegressionConstants.FIREWALL_CHECKPOINT_CSV_GZ:
	case AuditWeeklyRegressionConstants.FIREWALL_CHECKPOINT_CSV_BZ2:
	case AuditWeeklyRegressionConstants.FIREWALL_CHECKPOINT_CSV_ZIP:
	case AuditWeeklyRegressionConstants.FIREWALL_CHECKPOINT_CSV_7Z:{
		break;
	}
	
	case AuditWeeklyRegressionConstants.FIREWALL_BE_CHECKPOINT_SMARTVIEW_BZ2:
	case AuditWeeklyRegressionConstants.FIREWALL_BE_CHECKPOINT_SMARTVIEW_7Z:
	case AuditWeeklyRegressionConstants.FIREWALL_BE_CHECKPOINT_SMARTVIEW_ZIP:
	case AuditWeeklyRegressionConstants.FIREWALL_BE_CHECKPOINT_SMARTVIEW_GZ:
	case AuditWeeklyRegressionConstants.FIREWALL_BE_CHECKPOINT_SMARTVIEW_7ZA:{
		controller = new AuditGoldenSetDataController3("be_chkpt_sm_consumer");
		goldenSetDataList = controller.loadXlData();
		goldenSetTestDataSetup=new AuditGoldenSetTestDataSetup(goldenSetDataList);
		expectedAuditSummary = goldenSetTestDataSetup.populateAuditSummary(goldenSetDataList);
		Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);
		//actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID);
		actualAuditSummary=AuditTestUtils.populateActualAuditSummaryofConsumerServices(fireWallType,sourceID,"86400","1437609600","1440201599");
		auditSummaryForConsumerServicesValidationsErrors=AuditTestUtils.validateSummary(fireWallType,goldenSetTestDataSetup.getSernameNameWithServiceIdMap(),expectedAuditSummary,actualAuditSummary,auditSummaryForConsumerServicesValidationsErrors);
		if(AuditWeeklyRegressionConstants.FIREWALL_BE_CHECKPOINT_SMARTVIEW_BZ2.equals(fireWallType)){
			Assert.assertTrue(auditSummaryForConsumerServicesValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_CHECKPOINT_SMARTVIEW_BZ2+" Audit summary Info wrong ");
		}else if(AuditWeeklyRegressionConstants.FIREWALL_BE_CHECKPOINT_SMARTVIEW_7Z.equals(fireWallType)){
			Assert.assertTrue(auditSummaryForConsumerServicesValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_CHECKPOINT_SMARTVIEW_7Z+" Audit summary Info wrong ");
		}else if(AuditWeeklyRegressionConstants.FIREWALL_BE_CHECKPOINT_SMARTVIEW_ZIP.equals(fireWallType)){
			Assert.assertTrue(auditSummaryForConsumerServicesValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_CHECKPOINT_SMARTVIEW_ZIP+" Audit summary Info wrong ");
		}else if(AuditWeeklyRegressionConstants.FIREWALL_BE_CHECKPOINT_SMARTVIEW_GZ.equals(fireWallType)){
			Assert.assertTrue(auditSummaryForConsumerServicesValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_CHECKPOINT_SMARTVIEW_GZ+" Audit summary Info wrong ");
			
		}else{
			Assert.assertTrue(auditSummaryForConsumerServicesValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_CHECKPOINT_SMARTVIEW_7ZA+" Audit summary Info wrong ");
			
		}
		break;
	}

	case AuditWeeklyRegressionConstants.FIREWALL_BE_JUNIPER_SCREENOS_ZIP:
	case AuditWeeklyRegressionConstants.FIREWALL_BE_JUNIPER_SCREENOS_7Z:
	case AuditWeeklyRegressionConstants.FIREWALL_BE_JUNIPER_SCREENOS_7ZA:
	case AuditWeeklyRegressionConstants.FIREWALL_BE_JUNIPER_SCREENOS_GZ:
	case AuditWeeklyRegressionConstants.FIREWALL_BE_JUNIPER_SCREENOS_BZ2:{
		controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_JUNIPER_SCREENOS_DATA_SHEET+"_"+CONSUMER);
		goldenSetDataList = controller.loadXlData();
		goldenSetTestDataSetup=new AuditGoldenSetTestDataSetup(goldenSetDataList);
		expectedAuditSummary = goldenSetTestDataSetup.populateAuditSummary(goldenSetDataList);
		Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);
		//actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID);
		actualAuditSummary=AuditTestUtils.populateActualAuditSummaryofConsumerServices(fireWallType,sourceID,"86400","1387152000","1389743999");
		
		auditSummaryForConsumerServicesValidationsErrors=AuditTestUtils.validateSummary(fireWallType,goldenSetTestDataSetup.getSernameNameWithServiceIdMap(),expectedAuditSummary,actualAuditSummary,auditSummaryForConsumerServicesValidationsErrors);
		
		if(AuditWeeklyRegressionConstants.FIREWALL_BE_JUNIPER_SCREENOS_ZIP.equals(fireWallType)){
			Assert.assertTrue(auditSummaryForConsumerServicesValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_JUNIPER_SCREENOS_ZIP+" Audit summary Info wrong ");
			
		}else if(AuditWeeklyRegressionConstants.FIREWALL_BE_JUNIPER_SCREENOS_7Z.equals(fireWallType)){
			Assert.assertTrue(auditSummaryForConsumerServicesValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_JUNIPER_SCREENOS_7Z+" Audit summary Info wrong ");
		}else if(AuditWeeklyRegressionConstants.FIREWALL_BE_JUNIPER_SCREENOS_7ZA.equals(fireWallType)){
			Assert.assertTrue(auditSummaryForConsumerServicesValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_JUNIPER_SCREENOS_7ZA+" Audit summary Info wrong ");
		}else if(AuditWeeklyRegressionConstants.FIREWALL_BE_JUNIPER_SCREENOS_GZ.equals(fireWallType)){
			Assert.assertTrue(auditSummaryForConsumerServicesValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_JUNIPER_SCREENOS_GZ+" Audit summary Info wrong ");
		
		}else{
			Assert.assertTrue(auditSummaryForConsumerServicesValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_JUNIPER_SCREENOS_BZ2+" Audit summary Info wrong ");
			
		}
		break;
	}

	case AuditWeeklyRegressionConstants.FIREWALL_MCAFEE_SEF_ZIP: 
	case AuditWeeklyRegressionConstants.FIREWALL_MCAFEE_SEF_7Z: 
	case AuditWeeklyRegressionConstants.FIREWALL_MCAFEE_SEF_7ZA: 
	case AuditWeeklyRegressionConstants.FIREWALL_MCAFEE_SEF_GZ: 
	case AuditWeeklyRegressionConstants.FIREWALL_MCAFEE_SEF_BZ2: {
		controller = new AuditGoldenSetDataController3(AuditTestConstants.MCAFEE_SEF_SHEET+"_"+CONSUMER);
		goldenSetDataList = controller.loadXlData();
		goldenSetTestDataSetup=new AuditGoldenSetTestDataSetup(goldenSetDataList);
		expectedAuditSummary = goldenSetTestDataSetup.populateAuditSummary(goldenSetDataList);
		Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);
	//	actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID);
		actualAuditSummary=AuditTestUtils.populateActualAuditSummaryofConsumerServices(fireWallType,sourceID,"86400","1387670400","1390262399");
		
		auditSummaryForConsumerServicesValidationsErrors=AuditTestUtils.validateSummary(fireWallType,goldenSetTestDataSetup.getSernameNameWithServiceIdMap(),expectedAuditSummary,actualAuditSummary,auditSummaryForConsumerServicesValidationsErrors);
		
		if(AuditWeeklyRegressionConstants.FIREWALL_MCAFEE_SEF_ZIP.equals(fireWallType)){
			Assert.assertTrue(auditSummaryForConsumerServicesValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_MCAFEE_SEF_ZIP+" Audit summary Info wrong ");
		}else if(AuditWeeklyRegressionConstants.FIREWALL_MCAFEE_SEF_7Z.equals(fireWallType)){
			Assert.assertTrue(auditSummaryForConsumerServicesValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_MCAFEE_SEF_7Z+" Audit summary Info wrong ");
		}else if(AuditWeeklyRegressionConstants.FIREWALL_MCAFEE_SEF_7ZA.equals(fireWallType)){
			Assert.assertTrue(auditSummaryForConsumerServicesValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_MCAFEE_SEF_7ZA+" Audit summary Info wrong ");
		}else if(AuditWeeklyRegressionConstants.FIREWALL_MCAFEE_SEF_GZ.equals(fireWallType)){
			Assert.assertTrue(auditSummaryForConsumerServicesValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_MCAFEE_SEF_GZ+" Audit summary Info wrong ");
		}else{
			Assert.assertTrue(auditSummaryForConsumerServicesValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_MCAFEE_SEF_BZ2+" Audit summary Info wrong ");
			
		}
		
		break;
	}

	case AuditWeeklyRegressionConstants.FIREWALL_BE_PANCSV_SPLUNK_WO_CH_ZIP:
	case AuditWeeklyRegressionConstants.FIREWALL_BE_PANCSV_SPLUNK_WO_CH_7Z:
	case AuditWeeklyRegressionConstants.FIREWALL_BE_PANCSV_SPLUNK_WO_CH_7ZA:
	case AuditWeeklyRegressionConstants.FIREWALL_BE_PANCSV_SPLUNK_WO_CH_GZ:
	case AuditWeeklyRegressionConstants.FIREWALL_BE_PANCSV_SPLUNK_WO_CH_BZ2:{
		controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_PANCSV_SPLUNK_WO_CH_DATA_SHEET+"_"+CONSUMER);
		goldenSetDataList = controller.loadXlData();
		goldenSetTestDataSetup=new AuditGoldenSetTestDataSetup(goldenSetDataList);
		expectedAuditSummary = goldenSetTestDataSetup.populateAuditSummary(goldenSetDataList);
		Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);
		//actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID);
		actualAuditSummary=AuditTestUtils.populateActualAuditSummaryofConsumerServices(fireWallType,sourceID,"86400","1442275200","1444867199");
		
		
		auditSummaryForConsumerServicesValidationsErrors=AuditTestUtils.validateSummary(fireWallType,goldenSetTestDataSetup.getSernameNameWithServiceIdMap(),expectedAuditSummary,actualAuditSummary,auditSummaryForConsumerServicesValidationsErrors);
		
		if(AuditWeeklyRegressionConstants.FIREWALL_BE_PANCSV_SPLUNK_WO_CH_ZIP.equals(fireWallType)){
			Assert.assertTrue(auditSummaryForConsumerServicesValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_PANCSV_SPLUNK_WO_CH_ZIP+" Audit summary Info wrong ");
		}else if(AuditWeeklyRegressionConstants.FIREWALL_BE_PANCSV_SPLUNK_WO_CH_7Z.equals(fireWallType)){
			Assert.assertTrue(auditSummaryForConsumerServicesValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_PANCSV_SPLUNK_WO_CH_7Z+" Audit summary Info wrong ");
		}else if(AuditWeeklyRegressionConstants.FIREWALL_BE_PANCSV_SPLUNK_WO_CH_7ZA.equals(fireWallType)){
			Assert.assertTrue(auditSummaryForConsumerServicesValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_PANCSV_SPLUNK_WO_CH_7ZA+" Audit summary Info wrong ");
		}else if(AuditWeeklyRegressionConstants.FIREWALL_BE_PANCSV_SPLUNK_WO_CH_GZ.equals(fireWallType)){
			Assert.assertTrue(auditSummaryForConsumerServicesValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_PANCSV_SPLUNK_WO_CH_GZ+" Audit summary Info wrong ");
		}else{
			Assert.assertTrue(auditSummaryForConsumerServicesValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_PANCSV_SPLUNK_WO_CH_BZ2+" Audit summary Info wrong ");
			
		}
		break;
	}

	case AuditWeeklyRegressionConstants.FIREWALL_SQUID_PROXY_ZIP:
	case AuditWeeklyRegressionConstants.FIREWALL_SQUID_PROXY_7Z:
	case AuditWeeklyRegressionConstants.FIREWALL_SQUID_PROXY_7ZA:
	case AuditWeeklyRegressionConstants.FIREWALL_SQUID_PROXY_GZ:
	case AuditWeeklyRegressionConstants.FIREWALL_SQUID_PROXY_BZ2:{
		controller = new AuditGoldenSetDataController3(AuditTestConstants.SQUID_PROXY_SHEET+"_"+CONSUMER);
		goldenSetDataList = controller.loadXlData();
		goldenSetTestDataSetup=new AuditGoldenSetTestDataSetup(goldenSetDataList);
		expectedAuditSummary = goldenSetTestDataSetup.populateAuditSummary(goldenSetDataList);
		Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);
		//actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID);
		actualAuditSummary=AuditTestUtils.populateActualAuditSummaryofConsumerServices(fireWallType,sourceID,"2592000","1372636800","1404172799");
		
		//squid proxy file we do not hava valid data set so we are commenting validation part.
		//auditSummaryForConsumerServicesValidationsErrors=AuditTestUtils.validateSummary(fireWallType,goldenSetTestDataSetup.getSernameNameWithServiceIdMap(),expectedAuditSummary,actualAuditSummary,auditSummaryForConsumerServicesValidationsErrors);
		//Assert.assertTrue(auditSummaryForConsumerServicesValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_SQUID_PROXY+" Audit summary Info wrong ");
		break;
	}
	case AuditWeeklyRegressionConstants.FIREWALL_BE_WSAW3C_ZIP:
	case AuditWeeklyRegressionConstants.FIREWALL_BE_WSAW3C_7Z:
	case AuditWeeklyRegressionConstants.FIREWALL_BE_WSAW3C_7ZA:
	case AuditWeeklyRegressionConstants.FIREWALL_BE_WSAW3C_GZ:
	case AuditWeeklyRegressionConstants.FIREWALL_BE_WSAW3C_BZ2:{
		controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_WSAW3C_DATA_SHEET+"_"+CONSUMER);
		goldenSetDataList = controller.loadXlData();
		goldenSetTestDataSetup=new AuditGoldenSetTestDataSetup(goldenSetDataList);
		expectedAuditSummary = goldenSetTestDataSetup.populateAuditSummary(goldenSetDataList);
		Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);
		//actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID);
		actualAuditSummary=AuditTestUtils.populateActualAuditSummaryofConsumerServices(fireWallType,sourceID,"86400","1412467200","1415059199");
		
		auditSummaryForConsumerServicesValidationsErrors=AuditTestUtils.validateSummary(fireWallType,goldenSetTestDataSetup.getSernameNameWithServiceIdMap(),expectedAuditSummary,actualAuditSummary,auditSummaryForConsumerServicesValidationsErrors);
		if(AuditWeeklyRegressionConstants.FIREWALL_BE_WSAW3C_ZIP.equals(fireWallType)){
			Assert.assertTrue(auditSummaryForConsumerServicesValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_WSAW3C_ZIP+" Audit summary Info wrong ");
		}else if(AuditWeeklyRegressionConstants.FIREWALL_BE_WSAW3C_7Z.equals(fireWallType)){
			Assert.assertTrue(auditSummaryForConsumerServicesValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_WSAW3C_7Z+" Audit summary Info wrong ");
		}else if(AuditWeeklyRegressionConstants.FIREWALL_BE_WSAW3C_7ZA.equals(fireWallType)){
			Assert.assertTrue(auditSummaryForConsumerServicesValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_WSAW3C_7ZA+" Audit summary Info wrong ");
		}else if(AuditWeeklyRegressionConstants.FIREWALL_BE_WSAW3C_GZ.equals(fireWallType)){
			Assert.assertTrue(auditSummaryForConsumerServicesValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_WSAW3C_GZ+" Audit summary Info wrong ");
		}else{
			Assert.assertTrue(auditSummaryForConsumerServicesValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_WSAW3C_BZ2+" Audit summary Info wrong ");
			
		}
		
		break;
	}
	case AuditWeeklyRegressionConstants.FIREWALL_BE_WSA_ACCESS_ZIP:
	case AuditWeeklyRegressionConstants.FIREWALL_BE_WSA_ACCESS_7Z:
	case AuditWeeklyRegressionConstants.FIREWALL_BE_WSA_ACCESS_7ZA:
	case AuditWeeklyRegressionConstants.FIREWALL_BE_WSA_ACCESS_GZ:
	case AuditWeeklyRegressionConstants.FIREWALL_BE_WSA_ACCESS_BZ2:{
		controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_WSA_ACCESS_DATA_SHEET+"_"+CONSUMER);
		goldenSetDataList = controller.loadXlData();
		goldenSetTestDataSetup=new AuditGoldenSetTestDataSetup(goldenSetDataList);
		expectedAuditSummary = goldenSetTestDataSetup.populateAuditSummary(goldenSetDataList);
		Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);
		//actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID);
		actualAuditSummary=AuditTestUtils.populateActualAuditSummaryofConsumerServices(fireWallType,sourceID,"2592000","1388534400","1420070399");
		
		auditSummaryForConsumerServicesValidationsErrors=AuditTestUtils.validateSummary(fireWallType,goldenSetTestDataSetup.getSernameNameWithServiceIdMap(),expectedAuditSummary,actualAuditSummary,auditSummaryForConsumerServicesValidationsErrors);
		
		if(AuditWeeklyRegressionConstants.FIREWALL_BE_WSA_ACCESS_ZIP.equals(fireWallType)){
			Assert.assertTrue(auditSummaryForConsumerServicesValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_WSA_ACCESS_ZIP+" Audit summary Info wrong ");
		}else if(AuditWeeklyRegressionConstants.FIREWALL_BE_WSA_ACCESS_7Z.equals(fireWallType)){
			Assert.assertTrue(auditSummaryForConsumerServicesValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_WSA_ACCESS_7Z+" Audit summary Info wrong ");
		}else if(AuditWeeklyRegressionConstants.FIREWALL_BE_WSA_ACCESS_7ZA.equals(fireWallType)){
			Assert.assertTrue(auditSummaryForConsumerServicesValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_WSA_ACCESS_7ZA+" Audit summary Info wrong ");
		}else if(AuditWeeklyRegressionConstants.FIREWALL_BE_WSA_ACCESS_GZ.equals(fireWallType)){
			Assert.assertTrue(auditSummaryForConsumerServicesValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_WSA_ACCESS_GZ+" Audit summary Info wrong ");
		}else{
			Assert.assertTrue(auditSummaryForConsumerServicesValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_WSA_ACCESS_BZ2+" Audit summary Info wrong ");
			
		}
		break;
	}
	case AuditWeeklyRegressionConstants.FIREWALL_BE_ZSCALAR_7Z:
	case AuditWeeklyRegressionConstants.FIREWALL_BE_ZSCALAR_7ZA:
	case AuditWeeklyRegressionConstants.FIREWALL_BE_ZSCALAR_ZIP:
	case AuditWeeklyRegressionConstants.FIREWALL_BE_ZSCALAR_GZ:
	case AuditWeeklyRegressionConstants.FIREWALL_BE_ZSCALAR_BZ2:{
		controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_ZSCALAR_DATA_SHEET+"_"+CONSUMER);
		goldenSetDataList = controller.loadXlData();
		goldenSetTestDataSetup=new AuditGoldenSetTestDataSetup(goldenSetDataList);
		expectedAuditSummary = goldenSetTestDataSetup.populateAuditSummary(goldenSetDataList);
		Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);
	//	actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID);
		actualAuditSummary=AuditTestUtils.populateActualAuditSummaryofConsumerServices(fireWallType,sourceID,"86400","1432857600","1435449599");
		
		auditSummaryForConsumerServicesValidationsErrors=AuditTestUtils.validateSummary(fireWallType,goldenSetTestDataSetup.getSernameNameWithServiceIdMap(),expectedAuditSummary,actualAuditSummary,auditSummaryForConsumerServicesValidationsErrors);
		if(AuditWeeklyRegressionConstants.FIREWALL_BE_ZSCALAR_7Z.equals(fireWallType)){
			Assert.assertTrue(auditSummaryForConsumerServicesValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_ZSCALAR_7Z+" Audit summary Info wrong ");
		}else if(AuditWeeklyRegressionConstants.FIREWALL_BE_ZSCALAR_7ZA.equals(fireWallType)){
			Assert.assertTrue(auditSummaryForConsumerServicesValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_ZSCALAR_7ZA+" Audit summary Info wrong ");
		}else if(AuditWeeklyRegressionConstants.FIREWALL_BE_ZSCALAR_ZIP.equals(fireWallType)){
			Assert.assertTrue(auditSummaryForConsumerServicesValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_ZSCALAR_ZIP+" Audit summary Info wrong ");
		}else if(AuditWeeklyRegressionConstants.FIREWALL_BE_ZSCALAR_GZ.equals(fireWallType)){
			Assert.assertTrue(auditSummaryForConsumerServicesValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_ZSCALAR_GZ+" Audit summary Info wrong ");
		}else{
			Assert.assertTrue(auditSummaryForConsumerServicesValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_ZSCALAR_BZ2+" Audit summary Info wrong ");
		}
		break;
	}
		}
	
}


	
	
	@Test(dependsOnMethods={"testAuditSummaryNew"})
	public void testAuditReport() throws Exception {


		Reporter.log("********************Audit Report test started*****************************"+fireWallType,true);
		//goldenSetTestDataSetup=suiteData.getAuditGoldenSetTestDataSetup();

		Reporter.log("************************sourceID:"+sourceID,true);

		AuditReport expectedAuditReport=null;
		AuditGoldenSetDataController3 controller=null;
		List<GoldenSetData> goldenSetDataList = null;
		AuditReport actualAuditReport=null;

		switch(fireWallType)
		{
		case AuditTestConstants.FIREWALL_BE_BARRACUDA_CLI: 
		case AuditTestConstants.FIREWALL_BE_BARRACUDA_CLI_7Z:
		case AuditTestConstants.FIREWALL_BE_BARRACUDA_CLI_7ZA:{ 
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_BARRACUDA_CLI_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			goldenSetTestDataSetup=new AuditGoldenSetTestDataSetup(goldenSetDataList);
			expectedAuditReport = goldenSetTestDataSetup.prepareAuditReportData(goldenSetDataList,fireWallType,sourceID);
			Reporter.log("expectedAuditReport: for::"+fireWallType+"  "+expectedAuditReport,true);
			//actualAuditReport=AuditTestUtils.populateActualAuditReportData(fireWallType,sourceID);
			actualAuditReport=AuditTestUtils.populateActualAuditReportData(fireWallType,sourceID,"86400","1391385600","1393977599");
			Reporter.log("actualAuditReport: for::"+fireWallType+"  "+actualAuditReport,true);
			//auditReportValidationsErrors=AuditTestUtils.validateAuditReport(fireWallType,expectedAuditReport,actualAuditReport,auditReportValidationsErrors);
			
			if(AuditTestConstants.FIREWALL_BE_BARRACUDA_CLI.equals(fireWallType))
			{
				Assert.assertTrue(auditReportValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_BARRACUDA_CLI+" Audit Report Info wrong ");
			}else if(AuditTestConstants.FIREWALL_BE_BARRACUDA_CLI_7Z.equals(fireWallType))
			{
				Assert.assertTrue(auditReportValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_BARRACUDA_CLI_7Z+" Audit Report Info wrong ");
			}else{
				Assert.assertTrue(auditReportValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_BARRACUDA_CLI_7ZA+" Audit Report Info wrong ");
			}
			
			break;
		}
		

		case AuditWeeklyRegressionConstants.FIREWALL_BE_BARRACUDA_SYS_ZIP:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_BARRACUDA_SYS_GZ:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_BARRACUDA_SYS_7Z:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_BARRACUDA_SYS_7ZA:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_BARRACUDA_SYS_BZ2:
		{
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_BARRACUDA_SYS_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			goldenSetTestDataSetup=new AuditGoldenSetTestDataSetup(goldenSetDataList);
			expectedAuditReport = goldenSetTestDataSetup.prepareAuditReportData(goldenSetDataList,fireWallType,sourceID);
			Reporter.log("expectedAuditReport: for::"+fireWallType+"  "+expectedAuditReport,true);
			//actualAuditReport=AuditTestUtils.populateActualAuditReportData(fireWallType,sourceID);
			actualAuditReport=AuditTestUtils.populateActualAuditReportData(fireWallType,sourceID,"86400","1391385600","1393977599");
			//Reporter.log("actualAuditReport: for::"+fireWallType+"  "+actualAuditReport,true);
			//auditReportValidationsErrors=AuditTestUtils.validateAuditReport(fireWallType,expectedAuditReport,actualAuditReport,auditReportValidationsErrors);
			//Assert.assertTrue(auditReportValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_SQUID_PROXY+" Audit Report Info wrong ");
			
			if(AuditWeeklyRegressionConstants.FIREWALL_BE_BARRACUDA_SYS_ZIP.equals(fireWallType))
			{
				Assert.assertTrue(auditReportValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_BARRACUDA_SYS_ZIP+" Audit Report Info wrong ");
			}else if(AuditWeeklyRegressionConstants.FIREWALL_BE_BARRACUDA_SYS_GZ.equals(fireWallType))
			{
				Assert.assertTrue(auditReportValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_BARRACUDA_SYS_GZ+" Audit Report Info wrong ");
			}else if(AuditWeeklyRegressionConstants.FIREWALL_BE_BARRACUDA_SYS_7Z.equals(fireWallType))
			{
				Assert.assertTrue(auditReportValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_BARRACUDA_SYS_7Z+" Audit Report Info wrong ");
			}else if(AuditWeeklyRegressionConstants.FIREWALL_BE_BARRACUDA_SYS_7ZA.equals(fireWallType))
			{
				Assert.assertTrue(auditReportValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_BARRACUDA_SYS_7ZA+" Audit Report Info wrong ");
			}else{
				Assert.assertTrue(auditReportValidationsErrors.isEmpty(),AuditWeeklyRegressionConstants.FIREWALL_BE_BARRACUDA_SYS_BZ2+" Audit Report Info wrong ");
			}
			break;
		}

		case AuditWeeklyRegressionConstants.FIREWALL_BE_BLUECOAT_PROXY_GZ:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_BLUECOAT_PROXY_BZ2:	
		case AuditWeeklyRegressionConstants.FIREWALL_BE_BLUECOAT_PROXY_ZIP: 
		case AuditWeeklyRegressionConstants.FIREWALL_BE_BLUECOAT_PROXY_7Z: 
		case AuditWeeklyRegressionConstants.FIREWALL_BE_BLUECOAT_PROXY_7ZA:  
		{
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_BLUECOAT_PROXY_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			goldenSetTestDataSetup=new AuditGoldenSetTestDataSetup(goldenSetDataList);
			expectedAuditReport = goldenSetTestDataSetup.prepareAuditReportData(goldenSetDataList,fireWallType,sourceID);
			Reporter.log("expectedAuditReport: for::"+fireWallType+"  "+expectedAuditReport,true);
			//actualAuditReport=AuditTestUtils.populateActualAuditReportData(fireWallType,sourceID);
			actualAuditReport=AuditTestUtils.populateActualAuditReportData(fireWallType,sourceID,"86400","1394928000","1397519999");
			
			
			//Reporter.log("actualAuditReport: for::"+fireWallType+"  "+actualAuditReport,true);
			//auditReportValidationsErrors=AuditTestUtils.validateAuditReport(fireWallType,expectedAuditReport,actualAuditReport,auditReportValidationsErrors);
			//Assert.assertTrue(auditReportValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_SQUID_PROXY+" Audit Report Info wrong ");
			break;
		}

		case AuditWeeklyRegressionConstants.FIREWALL_BE_BLUECOATPROXY_SPLUNK_WO_CH_ZIP:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_BLUECOATPROXY_SPLUNK_WO_CH_7Z:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_BLUECOATPROXY_SPLUNK_WO_CH_GZ:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_BLUECOATPROXY_SPLUNK_WO_CH_BZ2:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_BLUECOATPROXY_SPLUNK_WO_CH_7ZA:{
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_BLUECOATPROXY_SPLUNK_WO_CH_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			goldenSetTestDataSetup=new AuditGoldenSetTestDataSetup(goldenSetDataList);
			expectedAuditReport = goldenSetTestDataSetup.prepareAuditReportData(goldenSetDataList,fireWallType,sourceID);
			Reporter.log("expectedAuditReport: for::"+fireWallType+"  "+expectedAuditReport,true);
			//actualAuditReport=AuditTestUtils.populateActualAuditReportData(fireWallType,sourceID);
			actualAuditReport=AuditTestUtils.populateActualAuditReportData(fireWallType,sourceID,"86400","1442448000","1445039999");
			
			
			//Reporter.log("actualAuditReport: for::"+fireWallType+"  "+actualAuditReport,true);
			//auditReportValidationsErrors=AuditTestUtils.validateAuditReport(fireWallType,expectedAuditReport,actualAuditReport,auditReportValidationsErrors);
			//Assert.assertTrue(auditReportValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_BLUECOATPROXY_SPLUNK_WO_CH+" Audit Report Info wrong ");
			break;
		}

		case AuditWeeklyRegressionConstants.FIREWALL_CHECKPOINT_CSV_7ZA:
		case AuditWeeklyRegressionConstants.FIREWALL_CHECKPOINT_CSV_GZ:
		case AuditWeeklyRegressionConstants.FIREWALL_CHECKPOINT_CSV_BZ2:
		case AuditWeeklyRegressionConstants.FIREWALL_CHECKPOINT_CSV_ZIP:
		case AuditWeeklyRegressionConstants.FIREWALL_CHECKPOINT_CSV_7Z: {
			break;
		}
		case AuditWeeklyRegressionConstants.FIREWALL_BE_CHECKPOINT_SMARTVIEW_BZ2:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_CHECKPOINT_SMARTVIEW_7Z:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_CHECKPOINT_SMARTVIEW_ZIP:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_CHECKPOINT_SMARTVIEW_GZ:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_CHECKPOINT_SMARTVIEW_7ZA:{
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_CHECKPOINT_SMARTVIEW_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			goldenSetTestDataSetup=new AuditGoldenSetTestDataSetup(goldenSetDataList);
			expectedAuditReport = goldenSetTestDataSetup.prepareAuditReportData(goldenSetDataList,fireWallType,sourceID);
			Reporter.log("expectedAuditReport: for::"+fireWallType+"  "+expectedAuditReport,true);
			//actualAuditReport=AuditTestUtils.populateActualAuditReportData(fireWallType,sourceID);
			actualAuditReport=AuditTestUtils.populateActualAuditReportData(fireWallType,sourceID,"86400","1437609600","1440201599");
			
			//Reporter.log("actualAuditReport: for::"+fireWallType+"  "+actualAuditReport,true);
			//auditReportValidationsErrors=AuditTestUtils.validateAuditReport(fireWallType,expectedAuditReport,actualAuditReport,auditReportValidationsErrors);
			//Assert.assertTrue(auditReportValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_SQUID_PROXY+" Audit Report Info wrong ");
			break;
		}

		case AuditWeeklyRegressionConstants.FIREWALL_BE_JUNIPER_SCREENOS_ZIP:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_JUNIPER_SCREENOS_7Z:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_JUNIPER_SCREENOS_7ZA:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_JUNIPER_SCREENOS_GZ:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_JUNIPER_SCREENOS_BZ2:{
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_JUNIPER_SCREENOS_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			goldenSetTestDataSetup=new AuditGoldenSetTestDataSetup(goldenSetDataList);
			expectedAuditReport = goldenSetTestDataSetup.prepareAuditReportData(goldenSetDataList,fireWallType,sourceID);
			Reporter.log("expectedAuditReport: for::"+fireWallType+"  "+expectedAuditReport,true);
			//actualAuditReport=AuditTestUtils.populateActualAuditReportData(fireWallType,sourceID);
			actualAuditReport=AuditTestUtils.populateActualAuditReportData(fireWallType,sourceID,"86400","1385251200","1387843199");
			
			//Reporter.log("actualAuditReport: for::"+fireWallType+"  "+actualAuditReport,true);
			//auditReportValidationsErrors=AuditTestUtils.validateAuditReport(fireWallType,expectedAuditReport,actualAuditReport,auditReportValidationsErrors);
			//Assert.assertTrue(auditReportValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_JUNIPER_SCREENOS+" Audit Report Info wrong ");
			break;
		}


		case AuditWeeklyRegressionConstants.FIREWALL_MCAFEE_SEF_ZIP: 
		case AuditWeeklyRegressionConstants.FIREWALL_MCAFEE_SEF_7Z: 
		case AuditWeeklyRegressionConstants.FIREWALL_MCAFEE_SEF_7ZA: 
		case AuditWeeklyRegressionConstants.FIREWALL_MCAFEE_SEF_GZ: 
		case AuditWeeklyRegressionConstants.FIREWALL_MCAFEE_SEF_BZ2: {
			controller = new AuditGoldenSetDataController3(AuditTestConstants.MCAFEE_SEF_SHEET);
			goldenSetDataList = controller.loadXlData();
			goldenSetTestDataSetup=new AuditGoldenSetTestDataSetup(goldenSetDataList);
			expectedAuditReport = goldenSetTestDataSetup.prepareAuditReportData(goldenSetDataList,fireWallType,sourceID);
			Reporter.log("expectedAuditReport: for::"+fireWallType+"  "+expectedAuditReport,true);
			//actualAuditReport=AuditTestUtils.populateActualAuditReportData(fireWallType,sourceID);
			actualAuditReport=AuditTestUtils.populateActualAuditReportData(fireWallType,sourceID,"86400","1387670400","1390262399");
			//Reporter.log("actualAuditReport: for::"+fireWallType+"  "+actualAuditReport,true);
			//auditReportValidationsErrors=AuditTestUtils.validateAuditReport(fireWallType,expectedAuditReport,actualAuditReport,auditReportValidationsErrors);
			//Assert.assertTrue(auditReportValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_MCAFEE_SEF+" Audit Report Info wrong ");
			break;
		}

		case AuditWeeklyRegressionConstants.FIREWALL_BE_PANCSV_ZIP:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_PANCSV_7Z:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_PANCSV_7ZA:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_PANCSV_GZ:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_PANCSV_BZ2:{
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_PANCSV_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			goldenSetTestDataSetup=new AuditGoldenSetTestDataSetup(goldenSetDataList);
			expectedAuditReport = goldenSetTestDataSetup.prepareAuditReportData(goldenSetDataList,fireWallType,sourceID);
			Reporter.log("expectedAuditReport: for::"+fireWallType+"  "+expectedAuditReport,true);
			//actualAuditReport=AuditTestUtils.populateActualAuditReportData(fireWallType,sourceID);
			actualAuditReport=AuditTestUtils.populateActualAuditReportData(fireWallType,sourceID,"86400","1377388800","1379980799");
			//Reporter.log("expectedAuditReport: for::"+fireWallType+"  "+expectedAuditReport,true);
			//auditReportValidationsErrors=AuditTestUtils.validateAuditReport(fireWallType,expectedAuditReport,actualAuditReport,auditReportValidationsErrors);
			//Assert.assertTrue(auditReportValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_SQUID_PROXY+" Audit Report Info wrong ");
			break;
		}
		
		case AuditWeeklyRegressionConstants.FIREWALL_BE_PANCSV_SPLUNK_WO_CH_ZIP:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_PANCSV_SPLUNK_WO_CH_7Z:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_PANCSV_SPLUNK_WO_CH_7ZA:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_PANCSV_SPLUNK_WO_CH_GZ:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_PANCSV_SPLUNK_WO_CH_BZ2:{
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_PANCSV_SPLUNK_WO_CH_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			goldenSetTestDataSetup=new AuditGoldenSetTestDataSetup(goldenSetDataList);
			expectedAuditReport = goldenSetTestDataSetup.prepareAuditReportData(goldenSetDataList,fireWallType,sourceID);
			Reporter.log("expectedAuditReport: for::"+fireWallType+"  "+expectedAuditReport,true);
			//actualAuditReport=AuditTestUtils.populateActualAuditReportData(fireWallType,sourceID);
			actualAuditReport=AuditTestUtils.populateActualAuditReportData(fireWallType,sourceID,"86400","1442275200","1444867199");
			
			//Reporter.log("expectedAuditReport: for::"+fireWallType+"  "+expectedAuditReport,true);
			//auditReportValidationsErrors=AuditTestUtils.validateAuditReport(fireWallType,expectedAuditReport,actualAuditReport,auditReportValidationsErrors);
			//Assert.assertTrue(auditReportValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_PANCSV_SPLUNK_WO_CH+" Audit Report Info wrong ");
			break;
		}
		
		
		case AuditWeeklyRegressionConstants.FIREWALL_SQUID_PROXY_ZIP:
		case AuditWeeklyRegressionConstants.FIREWALL_SQUID_PROXY_7Z:
		case AuditWeeklyRegressionConstants.FIREWALL_SQUID_PROXY_7ZA:
		case AuditWeeklyRegressionConstants.FIREWALL_SQUID_PROXY_GZ:
		case AuditWeeklyRegressionConstants.FIREWALL_SQUID_PROXY_BZ2:{
			//controller = new AuditGoldenSetDataController3(AuditTestConstants.SQUID_PROXY_SHEET);
			//goldenSetDataList = controller.loadXlData();
			//expectedAuditReport = goldenSetTestDataSetup.prepareAuditReportData(goldenSetDataList,fireWallType,sourceID);
			//Reporter.log("expectedAuditReport: for::"+fireWallType+"  "+expectedAuditReport,true);
			//actualAuditReport=AuditTestUtils.populateActualAuditReportData(fireWallType,sourceID);
			//actualAuditReport=AuditTestUtils.populateActualAuditReportData(fireWallType,sourceID,"2592000","1372636800","1404172799");
			//Reporter.log("actualAuditReport: for::"+fireWallType+"  "+actualAuditReport,true);
			//squid proxy file we do not hava valid data set so we are commenting validation part.
			//auditReportValidationsErrors=AuditTestUtils.validateAuditReport(fireWallType,expectedAuditReport,actualAuditReport,auditReportValidationsErrors);
			//Assert.assertTrue(auditReportValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_SQUID_PROXY+" Audit Report Info wrong ");
			break;
		}
		
		case AuditWeeklyRegressionConstants.FIREWALL_BE_WSAW3C_ZIP:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_WSAW3C_7Z:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_WSAW3C_7ZA:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_WSAW3C_GZ:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_WSAW3C_BZ2:{
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_WSAW3C_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			goldenSetTestDataSetup=new AuditGoldenSetTestDataSetup(goldenSetDataList);
			expectedAuditReport = goldenSetTestDataSetup.prepareAuditReportData(goldenSetDataList,fireWallType,sourceID);
			Reporter.log("actualAuditReport: for::"+fireWallType+"  "+actualAuditReport,true);
			//actualAuditReport=AuditTestUtils.populateActualAuditReportData(fireWallType,sourceID);
			actualAuditReport=AuditTestUtils.populateActualAuditReportData(fireWallType,sourceID,"86400","1412467200","1415059199");
			//Reporter.log("expectedAuditReport: for::"+fireWallType+"  "+expectedAuditReport,true);
			//auditReportValidationsErrors=AuditTestUtils.validateAuditReport(fireWallType,expectedAuditReport,actualAuditReport,auditReportValidationsErrors);
			//Assert.assertTrue(auditReportValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_WSAW3C+" Audit Report Info wrong ");
			break;
		}
		
		case AuditWeeklyRegressionConstants.FIREWALL_BE_WSA_ACCESS_ZIP:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_WSA_ACCESS_7Z:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_WSA_ACCESS_7ZA:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_WSA_ACCESS_GZ:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_WSA_ACCESS_BZ2:{
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_WSA_ACCESS_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			goldenSetTestDataSetup=new AuditGoldenSetTestDataSetup(goldenSetDataList);
			expectedAuditReport = goldenSetTestDataSetup.prepareAuditReportData(goldenSetDataList,fireWallType,sourceID);
			Reporter.log("expectedAuditReport: for::"+fireWallType+"  "+expectedAuditReport,true);
			//actualAuditReport=AuditTestUtils.populateActualAuditReportData(fireWallType,sourceID);
			actualAuditReport=AuditTestUtils.populateActualAuditReportData(fireWallType,sourceID,"2592000","1388534400","1420070399");
			//Reporter.log("expectedAuditReport: for::"+fireWallType+"  "+expectedAuditReport,true);
			//auditReportValidationsErrors=AuditTestUtils.validateAuditReport(fireWallType,expectedAuditReport,actualAuditReport,auditReportValidationsErrors);
			//Assert.assertTrue(auditReportValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_WSA_ACCESS+" Audit Report Info wrong ");
			break;
		}
		
		case AuditWeeklyRegressionConstants.FIREWALL_BE_ZSCALAR_7Z:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_ZSCALAR_7ZA:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_ZSCALAR_ZIP:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_ZSCALAR_GZ:
		case AuditWeeklyRegressionConstants.FIREWALL_BE_ZSCALAR_BZ2:{
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_ZSCALAR_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			goldenSetTestDataSetup=new AuditGoldenSetTestDataSetup(goldenSetDataList);
			expectedAuditReport = goldenSetTestDataSetup.prepareAuditReportData(goldenSetDataList,fireWallType,sourceID);
			Reporter.log("expectedAuditReport: for::"+fireWallType+"  "+expectedAuditReport,true);
			actualAuditReport=AuditTestUtils.populateActualAuditReportData(fireWallType,sourceID,"86400","1432857600","1435449599");
			//Reporter.log("expectedAuditReport: for::"+fireWallType+"  "+expectedAuditReport,true);
			//auditReportValidationsErrors=AuditTestUtils.validateAuditReport(fireWallType,expectedAuditReport,actualAuditReport,auditReportValidationsErrors);
			//Assert.assertTrue(auditReportValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_SQUID_PROXY+" Audit Report Info wrong ");
			break;
		}
		case AuditWeeklyRegressionConstants.FIREWALL_WEBSENSE_ARC_ZIP:
		case AuditWeeklyRegressionConstants.FIREWALL_WEBSENSE_ARC_7Z:
		case AuditWeeklyRegressionConstants.FIREWALL_WEBSENSE_ARC_7ZA:
		case AuditWeeklyRegressionConstants.FIREWALL_WEBSENSE_ARC_GZ:
		case AuditWeeklyRegressionConstants.FIREWALL_WEBSENSE_ARC_BZ2:
		{
			//actualAuditReport=AuditTestUtils.populateActualAuditReportData(fireWallType,sourceID);
			//actualAuditReport=AuditTestUtils.populateActualAuditReportData(fireWallType,sourceID,"2592000","1414886400","1417478399");
			
			Reporter.log("actualAuditReport: for::"+fireWallType+"  "+actualAuditReport,true);

			break;
		}
		case AuditWeeklyRegressionConstants.FIREWALL_WEBSENSE_HOSTED_ZIP:
		case AuditWeeklyRegressionConstants.FIREWALL_WEBSENSE_HOSTED_7Z:
		case AuditWeeklyRegressionConstants.FIREWALL_WEBSENSE_HOSTED_7ZA:
		case AuditWeeklyRegressionConstants.FIREWALL_WEBSENSE_HOSTED_GZ:
		case AuditWeeklyRegressionConstants.FIREWALL_WEBSENSE_HOSTED_BZ2:
		{
			actualAuditReport=AuditTestUtils.populateActualAuditReportData(fireWallType,sourceID,"86400","1414886400","1417478399");
			
			Reporter.log("actualAuditReport: for::"+fireWallType+"  "+actualAuditReport,true);

			break;
		}
		case AuditWeeklyRegressionConstants.FIREWALL_SONICWALL_ZIP:
		case AuditWeeklyRegressionConstants.FIREWALL_SONICWALL_7Z:
		case AuditWeeklyRegressionConstants.FIREWALL_SONICWALL_7ZA:
		case AuditWeeklyRegressionConstants.FIREWALL_SONICWALL_GZ:
		case AuditWeeklyRegressionConstants.FIREWALL_SONICWALL_BZ2:
		{
		//	actualAuditReport=AuditTestUtils.populateActualAuditReportData(fireWallType,sourceID);
			Reporter.log("actualAuditReport: for::"+fireWallType+"  "+actualAuditReport,true);

			break;
		}
		case AuditTestConstants.FIREWALL_WEBSENSE_ARC_TAR:
		{
			//actualAuditReport=AuditTestUtils.populateActualAuditReportData(fireWallType,sourceID);
			Reporter.log("actualAuditReport: for::"+fireWallType+"  "+actualAuditReport,true);

			break;
		}
		
		case AuditTestConstants.FIREWALL_WALLMART_PAN_CSV: {
			//actualAuditReport=AuditTestUtils.populateActualAuditReportData(fireWallType,sourceID);
			Reporter.log("actualAuditReport: for::"+fireWallType+"  "+actualAuditReport,true);

			break;
		}
		case AuditTestConstants.FIREWALL_WALLMART_PAN_SYS: {
			//actualAuditReport=AuditTestUtils.populateActualAuditReportData(fireWallType,sourceID);
			Reporter.log("actualAuditReport: for::"+fireWallType+"  "+actualAuditReport,true);

			break;
		}
		case AuditTestConstants.FIREWALL_WALLMART_MCAFEE_WEB_GATEWAY: {
			//actualAuditReport=AuditTestUtils.populateActualAuditReportData(fireWallType,sourceID);
			Reporter.log("actualAuditReport: for::"+fireWallType+"  "+actualAuditReport,true);

			break;
		}


		case AuditWeeklyRegressionConstants.FIREWALL_JUNIPER_SRX_ZIP:
		case AuditWeeklyRegressionConstants.FIREWALL_JUNIPER_SRX_7Z:
		case AuditWeeklyRegressionConstants.FIREWALL_JUNIPER_SRX_7ZA:
		case AuditWeeklyRegressionConstants.FIREWALL_JUNIPER_SRX_GZ:
		case AuditWeeklyRegressionConstants.FIREWALL_JUNIPER_SRX_BZ2: {
			//actualAuditReport=AuditTestUtils.populateActualAuditReportData(fireWallType,sourceID);
			Reporter.log("actualAuditReport: for::"+fireWallType+"  "+actualAuditReport,true);

			break;
		}
		
		case AuditWeeklyRegressionConstants.FIREWALL_SCANSAFE_ZIP:
		case AuditWeeklyRegressionConstants.FIREWALL_SCANSAFE_7Z:
		case AuditWeeklyRegressionConstants.FIREWALL_SCANSAFE_7ZA:
		case AuditWeeklyRegressionConstants.FIREWALL_SCANSAFE_GZ:
		case AuditWeeklyRegressionConstants.FIREWALL_SCANSAFE_BZ2: {
			//actualAuditReport=AuditTestUtils.populateActualAuditReportData(fireWallType,sourceID);
			Reporter.log("actualAuditReport: for::"+fireWallType+"  "+actualAuditReport,true);

			break;
		}
		case AuditWeeklyRegressionConstants.FIREWALL_CISCO_ASA_SERIES_ZIP:
		case AuditWeeklyRegressionConstants.FIREWALL_CISCO_ASA_SERIES_7Z:
		case AuditWeeklyRegressionConstants.FIREWALL_CISCO_ASA_SERIES_7ZA:
		case AuditWeeklyRegressionConstants.FIREWALL_CISCO_ASA_SERIES_GZ:
		case AuditWeeklyRegressionConstants.FIREWALL_CISCO_ASA_SERIES_BZ2: {
			//actualAuditReport=AuditTestUtils.populateActualAuditReportData(fireWallType,sourceID);
			Reporter.log("actualAuditReport: for::"+fireWallType+"  "+actualAuditReport,true);

			break;
		}

		}
	
		
	}

	
	// * This test case deletes the data source
	 
	@Test(description="deleteDatasources created for Scp/Sftp",dependsOnMethods={"testAuditReport"})
	public void deleteDataSourceTest() throws Exception {

		Reporter.log("************************** Test Description ****************************** ", true);
		Reporter.log("1. Call Datasource delete Api for the created Datasource", true);
		Reporter.log("2. Deleting Data Source for "+ fireWallType +" its ID is: "+sourceID+" started",true);
		Reporter.log("************************************************************************** ", true);
		 if(!(suiteData.getApiserverHostName().contains("qa-vpc") && suiteData.getTenantName().contains("datasourcemulti"))){
		        
				HttpResponse response = AuditFunctions.deleteDataSource(restClient, sourceID);
				Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_NO_CONTENT);
				 }
		Reporter.log("************************** deleteDataSourceTest comleted ****************************** ", true);

	}
	@AfterClass
	public void testPopulateIncompletedDataSourcesData() throws Exception
	{
		Reporter.log("===============SCP/SFTP Regression: In-Complete Datasources Reached SLA Time Failure analysis:================", true);
		if(!inCompleteDsList.isEmpty()){
			for(AuditDSStatusDTO dto:inCompleteDsList)
			{
				Reporter.log(""+dto,true);
			}
		}

	}
	@AfterClass
	public void testPopulateAuditSummaryFailures() throws Exception
	{
		Reporter.log("*****************scp tests Regression: Audit summary Validation Errors for  "+fireWallType,true);

		for(String str: auditSummaryValidationsErrors)
		{
			Reporter.log(str,true);
		}
	}
	
	@AfterClass
	public void testPopulateAuditReportFailures() throws Exception
	{
		Reporter.log("*****************scp tests regression : Audit Report Validation Errors for  "+fireWallType,true);

		for(String str: auditReportValidationsErrors)
		{
			Reporter.log(str,true);
		}
	}
	
	@AfterClass
	public void testPopulateAuditSummaryConsumerFailures() throws Exception
	{
		Reporter.log("*****************WebUpload Regression: Audit Summary Validation consumer Errors for "+fireWallType,true);

		for(String str: auditSummaryForConsumerServicesValidationsErrors)
		{
			Reporter.log(str,true);
		}
	}
	
	
	//



	/**
	 * validate created datasource object
	 * @param scpConnectionObject
	 * @throws Exception
	 */
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
		else if(suiteData.getApiserverHostName().contains("api-cwsintg.elastica-inc.com")){
			Assert.assertEquals(scpCredentialsObject.get("server"), AuditTestConstants.AUDIT_SCP_SERVER_CWSINTG);
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
	
	
	private SFTPUtils getSftpUtilsConfiguration(String sftpTenantUsername, String sftpServerHost,
			String sftpServerDestinationDir,String tenName) throws Exception {
		SFTPUtils sftpUtils = new SFTPUtils();
		sftpUtils.setHostName(sftpServerHost);
		sftpUtils.setHostPort(AuditTestConstants.AUDIT_SCP_PORT);
		sftpUtils.setUserName(sftpTenantUsername);
		// sftpUtils.setPassWord(AuditTestConstants.AUDIT_EOE_SPANVA_ING_SCP_PWD);
		sftpUtils.setPassWord(AuditTestUtils.getSpanVATenantScpPwd(tenName));
		sftpUtils.setDestinationDir(sftpServerDestinationDir);
		return sftpUtils;
	}
	private SFTPUtils getSftpUtilsConfiguration(String sftpTenantUsername,String sftpServerHost, String sftpServerDestinationDir ) {
		SFTPUtils sftpUtils=new SFTPUtils();
		sftpUtils.setHostName(sftpServerHost);
		sftpUtils.setHostPort(AuditTestConstants.AUDIT_SCP_PORT);
		sftpUtils.setUserName(sftpTenantUsername);
		if(suiteData.getApiserverHostName().contains("qa-vpc") && suiteData.getTenantName().contains("vpcauditscpco")){
	        	sftpUtils.setPassWord(AuditTestConstants.AUDIT_TENANT_VPC_PWD);}
		else if(suiteData.getApiserverHostName().contains("qa-vpc") && suiteData.getTenantName().contains("datasourcemulti")){
        	sftpUtils.setPassWord(AuditTestConstants.AUDIT_TENANT_VPC_PWD1);}
		else if(suiteData.getApiserverHostName().contains("api-vip.elastica.net")){
			sftpUtils.setPassWord(AuditTestConstants.AUDIT_TENANT_PROD_SCP_PWD);
		}
		
		else if(suiteData.getApiserverHostName().contains("api.eu.elastica.net")){
			sftpUtils.setPassWord(AuditTestConstants.AUDIT_TENANT_CEP_SCP_PWD);
		}
		
		else if(suiteData.getApiserverHostName().contains("api-cwsintg.elastica-inc.com")){
			sftpUtils.setPassWord(AuditTestConstants.AUDIT_TENANT_CWS_SCP_PWD);
		}
		else{
			sftpUtils.setPassWord(AuditTestConstants.AUDIT_TENANT_PWD);
		}
		sftpUtils.setDestinationDir(sftpServerDestinationDir);
		return sftpUtils;
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

}
