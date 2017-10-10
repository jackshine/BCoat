/**
 * 
 */
package com.elastica.beatle.tests.audit;

import java.io.IOException;
import java.util.ArrayList;
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
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import com.elastica.beatle.RestClient.Client;
import com.elastica.beatle.RestClient.ClientUtil;
import com.elastica.beatle.audit.AuditDSStatusDTO;
import com.elastica.beatle.audit.AuditFunctions;
import com.elastica.beatle.audit.AuditGoldenSetDataController;
import com.elastica.beatle.audit.AuditInitializeTests;
import com.elastica.beatle.audit.AuditTestConstants;
import com.elastica.beatle.audit.AuditTestUtils;
import com.elastica.beatle.fileHandler.FileHandlingUtils;
import com.elastica.beatle.logger.Logger;

/**
 * @author Mallesh
 *
 */
public class AuditSanityWebUploadTests extends AuditInitializeTests {
	protected Client restClient;	
	protected String FireWallType; 
	protected String sourceID = null;
	protected Properties firewallLogDataProps;
	protected AuditDSStatusDTO auditDSStatusDTO;
	
	

	protected ArrayList<AuditDSStatusDTO> inCompleteDsList=new ArrayList<AuditDSStatusDTO>();
	
	/**
	 * @param FireWallName
	 */
	public AuditSanityWebUploadTests(String FireWallName) {
		restClient = new Client();
		this.FireWallType = FireWallName;
	}		
	
	/**
	 * @throws IOException
	 * @throws Exception
	 */
	@Test()
	public void createDataSourceTest() throws IOException, Exception{	
		Reporter.log("********************************* Test Description ****************************************************** ", true);
		Reporter.log("1. Create Datasource through WebUpload Transportation", true);
		Reporter.log("2. Process the Datasource ", true);
		Reporter.log("3. Poll the Datasource status by calling the Datasource Api for every 2 minutes until it gets Completed ", true);
		Reporter.log("********************************************************************************************************* ", true);
		
		Reporter.log("*************Datasource Creation started for:"+FireWallType+"****************",true);
		//Create datasource
		String requestPayload=AuditTestUtils.createWebUploadPostBody(FireWallType,suiteData.getEnvironmentName(),AuditTestConstants.AUDIT_WU_DS_NAME);
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
		List<NameValuePair> queryParams = new ArrayList<NameValuePair>();
		queryParams.add(new BasicNameValuePair("filename", AuditTestUtils.getFireWallLogFileName(FireWallType)));
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
		Logger.info("Uploading file using S3 signed url for "+ FireWallType);
		HttpResponse uploadFileResponse = AuditFunctions.uploadFirewallLogFile(restClient,signedURL.trim(), AuditTestUtils.getFirewallLogFilePath(FireWallType));
		Assert.assertEquals(uploadFileResponse.getStatusLine().getStatusCode(), HttpStatus.SC_OK);		
		
		// Notify successful upload
		Logger.info("Notifying the upload status for "+FireWallType);
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
		
		// Poll for data source upload status
		HttpResponse pollForStatusResponse = AuditFunctions.pollForDataSourceStatus(restClient, sourceID);
		Assert.assertEquals(pollForStatusResponse.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
		JSONObject pollRespObject = new JSONObject(ClientUtil.getResponseBody(pollForStatusResponse));
		Reporter.log("Actual Datasource Response:"+pollRespObject,true);
		String last_Status = pollRespObject.getString("last_status");
		Reporter.log("Actual Datasource Status:"+last_Status,true);
		String expectedCompletedStatus="Completed";
		Reporter.log("Expected Datasource Status:"+expectedCompletedStatus,true);
		
		long currentWaitTime = 0;
		while(("Pending Data".equals(last_Status) || "Pending Validation".equals(last_Status) || "Queued".equals(last_Status) || "Processing".equals(last_Status))&& currentWaitTime <= AuditTestConstants.AUDIT_PROCESSING_SANITY_MAX_WAITTIME){
			Reporter.log("*****************Datasource Process Wait Time*************** :"+AuditTestConstants.AUDIT_THREAD_WAITTIME,true);
			Thread.sleep(AuditTestConstants.AUDIT_THREAD_WAITTIME);
			currentWaitTime += AuditTestConstants.AUDIT_THREAD_WAITTIME;
			pollForStatusResponse = AuditFunctions.pollForDataSourceStatus(restClient, sourceID);
			Assert.assertEquals(pollForStatusResponse.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
			pollRespObject = new JSONObject(ClientUtil.getResponseBody(pollForStatusResponse));
			last_Status = pollRespObject.getString("last_status");
			Logger.info("Actual Last Status of "+ sourceID +" is "+ last_Status);
			if("Completed".equals(last_Status) || "Failed".equals(last_Status))			
				break;
		}
		if(!"Completed".equals(last_Status) )
		{
			pollForStatusResponse = AuditFunctions.pollForDataSourceStatus(restClient, sourceID);
			Assert.assertEquals(pollForStatusResponse.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
			pollRespObject = new JSONObject(ClientUtil.getResponseBody(pollForStatusResponse));
			
			auditDSStatusDTO= AuditTestUtils.populateInCompleteDataSources(pollRespObject);
			inCompleteDsList.add(auditDSStatusDTO);
		}
		if(!"Completed".equals(last_Status) || !"Failed".equals(last_Status))
			Assert.assertTrue(currentWaitTime <= AuditTestConstants.AUDIT_PROCESSING_SANITY_MAX_WAITTIME," File processing time took more than "+ (int) ((AuditTestConstants.AUDIT_PROCESSING_SANITY_MAX_WAITTIME / (1000*60)) % 60)+" minutes. Last status of this source file was "+last_Status);
		Assert.assertEquals(pollRespObject.get("last_status"), "Completed","create data soure procesing is not completed. Last status of this source file was "+last_Status);				
		Assert.assertNotNull(pollRespObject.get("resource_uri"), "Resource URI is null. Last status of this source file was "+last_Status);
		Assert.assertFalse(((String) pollRespObject.get("resource_uri")).isEmpty(), "resource URI is empty. Last status of this source file was "+last_Status);
		Assert.assertNotNull(pollRespObject.get("log_format"), "Resource URI is null. Last status of this source file was "+last_Status);
		Assert.assertFalse(((String) pollRespObject.get("log_format")).isEmpty(), "resource URI is empty. Last status of this source file was "+last_Status);
		Assert.assertNotNull(pollRespObject.get("log_transport"), "Resource URI is null. Last status of this source file was "+last_Status);
		Assert.assertFalse(((String) pollRespObject.get("log_transport")).isEmpty(), "resource URI is empty. Last status of this source file was "+last_Status);
		Assert.assertEquals(pollRespObject.getString("log_transport"), AuditTestConstants.AUDIT_WEBUPLOAD_FILEFORMAT,"Log Transport method doesn't match. Last status of this source file was "+last_Status);
		Assert.assertNotNull(pollRespObject.get("id"), "Data Source Id is null. Last status of this source file was "+last_Status);
		Assert.assertFalse(((String) pollRespObject.get("id")).isEmpty(),"ID is empty. Last status of this source file was "+last_Status);
		Assert.assertEquals(pollRespObject.get("last_status_message"), "Logs processed successfully","Logs processing was not successful. Last status of this source file was "+last_Status);
		
		Reporter.log("************************** Datasource process Completed sucessfully ******************************", true);
		Reporter.log("**************************Datasource creation Test Completed**************************",true);
	}
	
	@Test(dependsOnMethods={"createDataSourceTest"})	
	public void TestAuditSummary() throws Exception {
		Reporter.log("************************** Test Description ****************************** ", true);
		Reporter.log("1. Call Audit summary Api for the created Datasource", true);
		Reporter.log("2. Verify the Audit summary data (date_range, id, earliest_date, latest_date...)", true);
		Reporter.log("************************************************************************** ", true);
		Reporter.log("**********Audit Summary Verification Test for "+ FireWallType +" its ID is: "+sourceID+" started********* ",true);
		
		String range = "1mo";			
		List<NameValuePair> queryParam = new ArrayList<NameValuePair>();				
		queryParam.add(new BasicNameValuePair("format", "json"));
		queryParam.add(new BasicNameValuePair("range", range));
		queryParam.add(new BasicNameValuePair("ds_id", sourceID));
		HttpResponse response  = AuditFunctions.getAuditSummary(restClient, queryParam);				
		Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
		AuditGoldenSetDataController controller=null;
		JSONObject summaryObject = (JSONObject) new JSONObject(ClientUtil.getResponseBody(response)).getJSONArray("objects").get(0);				
		
		//summary validation
		Reporter.log("Actual Audit summary Api Response:"+summaryObject,true);
		String expectedAuditSummaryResponse=    " [datasource_id=" + "Datasource Id not be null/empty"+
                ", date_range=" + range +
                ", earliest_date=" + "earliest_date not be null" +
                ", latest_date=" + "latest_date not be null" +
                ", resource_uri=" +"resource_uri is not empty" +"]";
		Reporter.log("Expected Audit summary few fields:"+expectedAuditSummaryResponse,true);
		
		Assert.assertEquals(summaryObject.get("datasource_id"), sourceID);										   
		Assert.assertEquals(summaryObject.get("date_range"), range);
		Assert.assertNotNull(summaryObject.get("id"), "Id is null");
		Assert.assertFalse(((String) summaryObject.get("id")).isEmpty(),"ID is empty");
		Assert.assertNotNull(summaryObject.get("resource_uri"),"resource_uri is null");
		Assert.assertFalse(((String)summaryObject.get("resource_uri")).isEmpty(),"resource_uri is empty");
		Assert.assertTrue(summaryObject.getBoolean("is_valid"));		
		Assert.assertNotNull(summaryObject.get("earliest_date"),"earliest date is null");		
		Assert.assertNotNull(summaryObject.get("latest_date"),"Latest date is null");	
	
		Reporter.log("**************************Audit Summary  Test Completed**************************",true);
		

	}
	
	@Test(dependsOnMethods={"TestAuditSummary"})
	public void testAuditReport() throws Exception {
		Reporter.log("************************** Test Description ****************************** ", true);
		Reporter.log("1. Call Audit Report Api for the created Datasource", true);
		Reporter.log("2. Verify the Audit Report data (date_range, datasource_id, earliest_date, latest_date...)", true);
		Reporter.log("************************************************************************** ", true);
		Reporter.log("**********Audit Report Verification Test for "+ FireWallType +" its ID is: "+sourceID+" started********* ",true);
		
		List<NameValuePair> queryParam = new ArrayList<NameValuePair>();
		String range = "1mo";
		queryParam.add(new BasicNameValuePair("format", "json"));
		queryParam.add(new BasicNameValuePair("range", range));
		queryParam.add(new BasicNameValuePair("ds_id", sourceID));
		HttpResponse response  = AuditFunctions.getAuditReport(restClient, queryParam);		
		Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_OK);		
		JSONObject reportObject = (JSONObject) new JSONObject(ClientUtil.getResponseBody(response)).getJSONArray("objects").get(0);
		Reporter.log("Actual Audit Report Response:"+reportObject,true);
		
		String expectedAuditReportResponse=    " [datasource_id=" + "Datasource Id not be null/empty"+
                ", date_range=" + range +
                ", earliest_date=" + "earliest_date not be null" +
                ", latest_date=" + "latest_date not be null" +
                ", generated_date=" +"generated_date not be null" +"]";
		Reporter.log("Expected Audit Report:"+expectedAuditReportResponse,true);
		
		//Report validations
		Assert.assertEquals(reportObject.get("datasource_id"), sourceID);
		Assert.assertEquals(reportObject.get("date_range"), range);
		Assert.assertNotNull(reportObject.get("earliest_date"),"earliest date is null");		
		Assert.assertNotNull(reportObject.get("generated_date"),"earliest date is null");		
		Assert.assertNotNull(reportObject.get("latest_date"),"Latest date is null");	
		
		Reporter.log("**************************Audit Report Verification Test Completed**************************",true);
		
	}
	
	/*
	 * This test case deletes the data source
	 */
	@Test(dependsOnMethods={"testAuditReport"})
	public void deleteDataSourceTest() throws Exception {
		Reporter.log("************************** Test Description ****************************** ", true);
		Reporter.log("1. Call Datasource delete Api for the created Datasource", true);
		Reporter.log("2. Deleting Data Source for "+ FireWallType +" its ID is: "+sourceID+" started",true);
		Reporter.log("************************************************************************** ", true);
		HttpResponse response = AuditFunctions.deleteDataSource(restClient, sourceID);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_NO_CONTENT);
		
		Reporter.log("*****************************************Datasource: "+sourceID+" deleted sucessfully",true);
		Reporter.log("**************************Datasource Deletion Test Completed**************************",true);
		
	}
	
	@AfterClass
	public void testPopulateIncompletedDataSourcesData() throws Exception
	{
		Reporter.log("===============WebUpload Regression: In-Complete Datasources Reached SLA Time Failure analysis:================", true);
		if(!inCompleteDsList.isEmpty()){
			for(AuditDSStatusDTO dto:inCompleteDsList)
			{
				Reporter.log(""+dto,true);
			}
		}
		
	}
	
	
	
}
