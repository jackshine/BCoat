/**
 * 
 */
package com.elastica.beatle.tests.audit;

import java.io.IOException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

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
import org.testng.annotations.Test;

import com.elastica.beatle.RestClient.Client;
import com.elastica.beatle.RestClient.ClientUtil;
import com.elastica.beatle.audit.AuditDSStatusDTO;
import com.elastica.beatle.audit.AuditFunctions;
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
import com.elastica.beatle.audit.GoldenSetData;
import com.elastica.beatle.audit.SummaryTabDto;
import com.elastica.beatle.i18n.I18N;
import com.elastica.beatle.logger.Logger;

/**
 * @author anuvrath
 *
 */
public class AuditI18Tests extends AuditInitializeTests{
	protected Client restClient;	
	protected String fireWallType; 
	protected String sourceID = null;
	protected Properties firewallLogDataProps;
	protected AuditDSStatusDTO auditDSStatusDTO;
	protected ArrayList<AuditDSStatusDTO> inCompleteDsList=new ArrayList<AuditDSStatusDTO>();
	ArrayList<String> goldenSetErrorList=new ArrayList<String>();
	AuditGoldenSetTestDataSetup goldenSetTestDataSetup=null;
	List<String> auditReportValidationsErrors = new ArrayList<String>();
	ArrayList<String> auditSummaryValidationsErrors = new ArrayList<String>();



	/**
	 * @param FireWallName
	 */
	public AuditI18Tests(String FireWallName) {
		restClient = new Client();
		this.fireWallType = FireWallName;
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

		Reporter.log("*************Datasource Creation started for:"+fireWallType+"****************",true);
		String requestPayload=AuditTestUtils.createWebUploadPostBody(fireWallType,suiteData.getEnvironmentName(), I18N.getString("language", "ja_JP"));
		Reporter.log("Request Payload: "+requestPayload,true);
 
		HttpResponse createResp = AuditFunctions.createDataSource(restClient,new StringEntity(requestPayload, "UTF-8"));		
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
		while(("Pending Data".equals(last_Status) || "Pending Validation".equals(last_Status) || "Queued".equals(last_Status) || "Processing".equals(last_Status))&& currentWaitTime <= AuditTestConstants.AUDIT_PROCESSING_MAX_WAITTIME){
			Reporter.log("Datasource Process Wait Time*************** :"+AuditTestConstants.AUDIT_THREAD_WAITTIME,true);
			Thread.sleep(AuditTestConstants.AUDIT_THREAD_WAITTIME);
			currentWaitTime += AuditTestConstants.AUDIT_THREAD_WAITTIME;
			pollForStatusResponse = AuditFunctions.pollForDataSourceStatus(restClient, sourceID);
			Assert.assertEquals(pollForStatusResponse.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
			pollRespObject = new JSONObject(ClientUtil.getResponseBody(pollForStatusResponse));
			last_Status = pollRespObject.getString("last_status");
			Logger.info("Actual Last Status of Datasource: "+ sourceID +" is "+ last_Status);
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
		if(!"Completed".equals(last_Status) || !"Failed".equals(last_Status))
			Assert.assertTrue(currentWaitTime <= AuditTestConstants.AUDIT_PROCESSING_MAX_WAITTIME," File processing took more than SLA. Last status of this source file was "+last_Status);
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
	public void deleteDataSourceTest() throws Exception {
		Reporter.log("************************** Test Description ****************************** ", true);
		Reporter.log("1. Call Datasource delete Api for the created Datasource", true);
		Reporter.log("2. Deleting Data Source for "+ fireWallType +" its ID is: "+sourceID+" started",true);
		Reporter.log("************************************************************************** ", true);
		
		
		 if(!(suiteData.getApiserverHostName().contains("qa-vpc") && suiteData.getTenantName().contains("datasourcemulti"))){
		        
				HttpResponse response = AuditFunctions.deleteDataSource(restClient, sourceID);
				Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_NO_CONTENT);
				 }
		Reporter.log("*****************************************Datasource: "+sourceID+" deleted sucessfully",true);
		Reporter.log("**************************Datasource Deletion Test Completed**************************",true);
		
		

	}

	//@AfterClass
	public void testPopulateIncompletedDataSourcesData() throws Exception
	{
		Reporter.log("===============WebUpload Regression: In-Complete Datasources Reached SLA Time Failure analysis"+fireWallType, true);
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
		Reporter.log("*****************WebUpload Regression: Audit Summary Validation Errors for "+fireWallType,true);

		for(String str: auditSummaryValidationsErrors)
		{
			Reporter.log(str,true);
		}
	}

	@AfterClass
	public void testPopulateAuditReportFailures() throws Exception
	{
		Reporter.log("*****************WebUpload Regression: Audit Report Validation Errors for  "+fireWallType,true);

		for(String str: auditReportValidationsErrors)
		{
			Reporter.log(str,true);
		}
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
