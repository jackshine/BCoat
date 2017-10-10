/**
 * 
 */
package com.elastica.beatle.tests.audit.dummy;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.elastica.beatle.RestClient.Client;
import com.elastica.beatle.RestClient.ClientUtil;
import com.elastica.beatle.audit.AuditDSStatusDTO;
import com.elastica.beatle.audit.AuditFunctions;
import com.elastica.beatle.audit.AuditGoldenSetTestDataSetup;
import com.elastica.beatle.audit.AuditInitializeTests;
import com.elastica.beatle.audit.AuditTestConstants;
import com.elastica.beatle.audit.AuditTestUtils;
import com.elastica.beatle.logger.Logger;

/**
 * @author Mallesh
 *
 */
public class AuditWebUploadTestsNew4 extends AuditInitializeTests{
	protected Client restClient;	
	protected String fireWallType; 
	protected String sourceID = null;
	protected Properties firewallLogDataProps;
	protected AuditDSStatusDTO auditDSStatusDTO;
	protected ArrayList<AuditDSStatusDTO> inCompleteDsList=new ArrayList<AuditDSStatusDTO>();
	ArrayList<String> goldenSetErrorList=new ArrayList<String>();
	List<String>firwallsList;
	Properties summaryProps=new Properties();
	AuditGoldenSetTestDataSetup goldenSetTestDataSetup=null;
	List<String> auditReportValidationsErrors = new ArrayList<String>();
	
	
	public AuditWebUploadTestsNew4() {
		restClient = new Client();
		
	}
	
	@BeforeClass(alwaysRun=true)
	public void initializeTests() throws Exception {
		Reporter.log("AuditWebuploadTestsNew3 before class is calling..",true);
		// goldenSetTestDataSetup=new AuditGoldenSetTestDataSetup();
	}
	
	@Test
	public void test()
	{
		Reporter.log("AuditWebuploadTestsNew3 test method calling..",true);
		
	}

	
	
	
	public Map<String,String> testFirewallDataSourceCreation(String fireWallType) throws Exception
	{
		Reporter.log("********************************* Test Description ****************************************************** ", true);
		Reporter.log("1. Create Datasource through WebUpload Transportation", true);
		Reporter.log("2. Process the Datasource ", true);
		Reporter.log("3. Poll the Datasource status by calling the Datasource Api for every 2 minutes until it gets Completed ", true);
		Reporter.log("********************************************************************************************************* ", true);
		Map<String,String> dataSourcesMap=new HashMap<String,String>();
		Reporter.log("*************Datasource Creation started for:"+fireWallType+"****************",true);
		String requestPayload=AuditTestUtils.createWebUploadPostBody(fireWallType,suiteData.getEnvironmentName(),AuditTestConstants.AUDIT_WU_DS_NAME);
		Reporter.log("Request Payload: "+requestPayload,true);
		
		HttpResponse createResp = AuditFunctions.createDataSource(restClient,new StringEntity(requestPayload));		
		Assert.assertEquals(createResp.getStatusLine().getStatusCode(), HttpStatus.SC_CREATED);		
		String createResponse = ClientUtil.getResponseBody(createResp);
		Reporter.log("Actual Datasource Response:"+createResponse,true);
		JSONObject createResponseObject = new JSONObject(createResponse);
		
		
		//Expected values preparation
		JSONObject expectedDSResponse = new JSONObject(requestPayload);
		String expected_str_datasource_name=(String)expectedDSResponse.get("name");
		String expected_str_datasource_format=(String)expectedDSResponse.get("datasource_format");
		String expected_str_datasource_type=(String)expectedDSResponse.get("datasource_type");
		String expected_str_setup_by=suiteData.getUsername();
		String str_last_status_or_detectstatus="Pending Data";
		String expectedResponse=    " [Datasourcename=" + expected_str_datasource_name+
		                 ", DatasourceFormat=" + expected_str_datasource_format +
		                 ", DatasourceType=" + expected_str_datasource_type +
		                 ", SetupBy=" + expected_str_setup_by +
		                 ", last_status=" + str_last_status_or_detectstatus +
		                 ", last_detect_status=" + str_last_status_or_detectstatus+" ]";
		Reporter.log("Expected Datasource Response fields:"+expectedResponse,true);
		Assert.assertEquals(createResponseObject.get("name"), expected_str_datasource_name);
		Assert.assertNotNull(createResponseObject.get("datasource_format"), "Data Source Format is null");
		Assert.assertFalse(((String) createResponseObject.get("datasource_format")).isEmpty(),"Data source format is empty");
		Assert.assertEquals(createResponseObject.get("datasource_format"), expected_str_datasource_format);
		Assert.assertNotNull(createResponseObject.get("datasource_type"), "Data Source Type is null");
		Assert.assertFalse(((String) createResponseObject.get("datasource_type")).isEmpty(),"Data source type is empty");	
		Assert.assertEquals(createResponseObject.get("datasource_type"), expected_str_datasource_type);
		Assert.assertNotNull(createResponseObject.get("setup_by"), "SetUp by is null");
		Assert.assertFalse(((String) createResponseObject.get("setup_by")).isEmpty(),"Set Up by is empty");
		Assert.assertEquals(createResponseObject.get("setup_by"), expected_str_setup_by);
		Assert.assertNotNull(createResponseObject.get("id"), "Data Source Id is null");
		Assert.assertFalse(((String) createResponseObject.get("id")).isEmpty(),"ID is empty");
		Assert.assertNotNull(createResponseObject.get("resource_uri"), "Resource URI is null");
		Assert.assertFalse(((String) createResponseObject.get("resource_uri")).isEmpty(), "resource URI is empty");
		Assert.assertEquals(createResponseObject.get("last_status"), str_last_status_or_detectstatus,"Last status is not \"Pending Data\"");
		Assert.assertEquals(createResponseObject.get("last_detect_status"), str_last_status_or_detectstatus,"Last status is not \"Pending Data\"");	
				 
				
		sourceID = (String) createResponseObject.get("id");
				
		// Get Signed URL for the data Source
		Logger.info("Getting signed URl for "+fireWallType+" to upload");
		List<NameValuePair> queryParams = new ArrayList<NameValuePair>();
		queryParams.add(new BasicNameValuePair("filename", AuditTestUtils.getFireWallLogFileName(fireWallType)));
		queryParams.add(new BasicNameValuePair("filetype", "application/zip"));		
		HttpResponse signedURLResp = AuditFunctions.getSignedDataResourceURL(restClient, queryParams, sourceID);
		Assert.assertEquals(signedURLResp.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
		String signedURLRespString = ClientUtil.getResponseBody(signedURLResp);
		Reporter.log("Actual signedURLResp:"+signedURLRespString,true);
		JSONObject signedURLObject = new JSONObject(signedURLRespString);	
		String expectedsignedURLResponse=    " [signed_request=" + "Should not be null"+
                ", url=" + "should not be null" +
                ", signed_request=" +"signed_request is not empty" +
                ", url=" + "url is not empty"+" ]";
		Reporter.log("Expected signedURLResp:"+expectedsignedURLResponse,true);
		
		Assert.assertNotNull(signedURLObject.get("signed_request"),"Signed Request is null");
		Assert.assertNotNull(signedURLObject.get("url"),"Signed URL is null");		
		Assert.assertFalse(((String) signedURLObject.get("url")).isEmpty(),"URL is empty");
		Assert.assertFalse(((String) signedURLObject.get("signed_request")).isEmpty(),"Signed request is empty");		
		String signedURL = (String) signedURLObject.get("signed_request");
						
		// upload firewall using amazon S3 URL
		Logger.info("Uploading file using S3 signed url for "+ fireWallType);
		HttpResponse uploadFileResponse = AuditFunctions.uploadFirewallLogFile(restClient,signedURL.trim(), AuditTestUtils.getFirewallLogFilePath(fireWallType));
		Assert.assertEquals(uploadFileResponse.getStatusLine().getStatusCode(), HttpStatus.SC_OK);		
		
		// Notify successful upload
		Logger.info("Notifying the upload status for "+fireWallType);
		HttpResponse notifyResponse = AuditFunctions.notifyFileUploadStatus(restClient, sourceID);		
		Assert.assertEquals(notifyResponse.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
		JSONObject notifyObject = new JSONObject(ClientUtil.getResponseBody(notifyResponse));
		Reporter.log("Actual Notifying the upload status Response:"+notifyObject,true);
		String uploadNotification="Upload notification successful.";
		String sucess="success";
		String expectedNotifyUploadResp= " [uploadNotification=" + uploadNotification+", sucess=" + sucess+" ]";
		Reporter.log("Expected Notifying the upload status Response:"+expectedNotifyUploadResp,true);
		Assert.assertEquals(notifyObject.get("message"), uploadNotification);
		Assert.assertEquals(notifyObject.get("status"), sucess);
		
		dataSourcesMap.put(fireWallType, sourceID);
		return dataSourcesMap;
		
	}
	
	public Properties  testDatasourceProcessVerification(Map<String,String> dataSourcesMap) throws Exception
	{
		Properties processedDataSources=new Properties();
		List<String> dataSourceIDsList=new ArrayList<String>(dataSourcesMap.values());
		String strFirewallType=null;
		long currentWaitTime=0;
		process(dataSourceIDsList,currentWaitTime);
			
			List<String> dataSourceIDsListFinal=new ArrayList<String>(dataSourcesMap.values());
			for(String strID: dataSourceIDsListFinal)
			{
				strFirewallType=getKeysFromValue(dataSourcesMap,strID);
				processedDataSources.put(strFirewallType, strID);
			}
			
			return processedDataSources;
	
	}
	
	
	public void process( List<String> dataSourceIDsList,long currentWaitTime ) throws Exception
 {
		String last_Status = "";
		Reporter.log("dataSourceIDsList. outside while" + dataSourceIDsList, true);
		String firstDataSourceID = "";

		if (!dataSourceIDsList.isEmpty() && dataSourceIDsList.size() > 0) {
			firstDataSourceID = ((!dataSourceIDsList.isEmpty() && dataSourceIDsList.size() > 0)
					? dataSourceIDsList.get(0) : "");
			last_Status = pollForDataSourceStatus(restClient, firstDataSourceID);

		}

		if ("Completed".equals(last_Status)) {
			Reporter.log("if block Datasource Process total Wait Time outside*************** :" + currentWaitTime,
					true);
			dataSourceIDsList.remove(firstDataSourceID);
			if (!dataSourceIDsList.isEmpty() && dataSourceIDsList.size() > 0) {
				Collections.shuffle(dataSourceIDsList);
				process(dataSourceIDsList, currentWaitTime);
			}
		} else {

			String strFirewallType = null;

			while (("Pending Data".equals(last_Status) || "Pending Validation".equals(last_Status)
					|| "Queued".equals(last_Status) || "Processing".equals(last_Status))
					&& currentWaitTime <= AuditTestConstants.AUDIT_PROCESSING_MAX_WAITTIME) {
				Reporter.log("Wait Time*************** :" + AuditTestConstants.AUDIT_THREAD_WAITTIME, true);
				Thread.sleep(AuditTestConstants.AUDIT_THREAD_WAITTIME);
				currentWaitTime += AuditTestConstants.AUDIT_THREAD_WAITTIME;
				Reporter.log("dataSourceIDsList. inside while" + dataSourceIDsList, true);

				if (dataSourceIDsList.size() == 0)
					break;
				else {
					if (!dataSourceIDsList.isEmpty()) {
						Collections.shuffle(dataSourceIDsList);
						firstDataSourceID = dataSourceIDsList.get(0);
						last_Status = pollForDataSourceStatus(restClient, firstDataSourceID);
					}

					Logger.info("shuffling and Last Status of Datasource: " + firstDataSourceID + " is " + last_Status);
					Reporter.log("Datasource list size *************** :" + dataSourceIDsList.size(), true);

					if ("Completed".equals(last_Status)) {
						Reporter.log("else block Datasource Process total Wait Time outside*************** :"
								+ currentWaitTime, true);
						dataSourceIDsList.remove(firstDataSourceID);
					}
					Reporter.log("Datasource atleast list size after *************** :" + dataSourceIDsList.size(),
							true);
					
					if (!dataSourceIDsList.isEmpty()) {
						Collections.shuffle(dataSourceIDsList);
						process(dataSourceIDsList, currentWaitTime);
					}
				}

			}
		}
	}
	
	 public static String getKeysFromValue(Map<String, String> hm, Object value){
		    List <String>list = new ArrayList<String>();
		    String key=null;
		    for(String o:hm.keySet()){
		        if(hm.get(o).equals(value)) {
		        	key=o;
		        }
		    }
		    return key;
		  }
	
	@DataProvider(name="dataSourceDP")//processedDataSourcesList getCompletedDSs
	public Object[][] processedDataSourcesList() throws Exception
	{
		
		firwallsList=AuditTestConstants.getFirewallsList();
		
		Properties dsProps=new Properties();
	       
	       Map<String,String> dataSourcesMap1=new HashMap<String,String>();
		   //Create Datasources
	       for(String key:firwallsList )
			{
	    	   dataSourcesMap1.putAll(this.testFirewallDataSourceCreation(key));
			 
			}
	       
	       Reporter.log("dataSourcesMap1::"+dataSourcesMap1,true);
	       
	       //Verify Datasource process check and get the all the datasources
	       dsProps=testDatasourceProcessVerification(dataSourcesMap1);
	
		Reporter.log("processedDataSources:::"+dsProps+"  size::"+dsProps.size(),true);
		Object[][] inputData = new Object[dsProps.size()][3];
		int j=0;
		String last_Status=null;
		

		for(String firewallType:dsProps.stringPropertyNames())
		{
			inputData[j][0] = firewallType;
			inputData[j][1] = dsProps.getProperty(firewallType);
			last_Status=pollForDataSourceStatus(restClient,dsProps.getProperty(firewallType));
			inputData[j][2] = last_Status;
			j++;
		}
		Reporter.log("Webupload Datasources"+inputData,true);
		Reporter.log("webupload completed datasources length::"+inputData.length,true);
		return inputData;
	}
	

	public String pollForDataSourceStatus(Client restClient,String dataSourceID) throws Exception
	{
		
		HttpResponse pollForStatusResponse = AuditFunctions.pollForDataSourceStatus(restClient, dataSourceID);
		Assert.assertEquals(pollForStatusResponse.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
		JSONObject pollRespObject = new JSONObject(ClientUtil.getResponseBody(pollForStatusResponse));
		Reporter.log("Actual Datasource Response:"+pollRespObject,true);
		String last_Status = pollRespObject.getString("last_status");
		Reporter.log("Actual Datasource Status:"+last_Status,true);
		String expectedCompletedStatus="Completed";
		Reporter.log("Expected Datasource Status:"+expectedCompletedStatus,true);
		return last_Status;
	}
	
	
	@Test(priority=1,dataProvider="dataSourceDP")
	public void testDataSourceCreationAndProcessForDifferentFirewalls(String firewallType, String dsID,String dsStatus) throws Exception
	{
		Reporter.log("started testDataSourceCreationAndProcessForDifferentFirewalls****************");
		
		summaryProps.put(firewallType+"~"+dsStatus, dsID);
		
		Reporter.log("firewallType:"+firewallType+ " dsID:"+dsID+" dsStatus:"+dsStatus);;
		if("Completed".equals(dsStatus) ){
			 Assert.assertTrue(true);
		}
		else{
				Assert.assertFalse(!"Completed".equals(dsStatus),
					firewallType+" DataSource "+dsID+" not processed in "+AuditTestConstants.AUDIT_PROCESSING_MAX_WAITTIME +"  and current status is "+dsStatus);
		}
		
	}
	
	
}
