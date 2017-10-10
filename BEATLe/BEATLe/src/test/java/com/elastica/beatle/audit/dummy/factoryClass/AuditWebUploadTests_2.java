/**
 * 
 */
package com.elastica.beatle.audit.dummy.factoryClass;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
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
import com.elastica.beatle.logger.Logger;

/**
 * @author MALLESH
 *
 */
public class AuditWebUploadTests_2 extends AuditInitializeTests{
	protected Client restClient;	
	protected String fireWallType; 
	protected String sourceID = null;
	protected Properties firewallLogDataProps;
	protected AuditDSStatusDTO auditDSStatusDTO;
	protected ArrayList<AuditDSStatusDTO> inCompleteDsList=new ArrayList<AuditDSStatusDTO>();
	ArrayList<String> goldenSetErrorList=new ArrayList<String>();
	Properties props=new Properties();
	AuditGoldenSetTestDataSetup goldenSetTestDataSetup=null;
	List<String> auditReportValidationsErrors = new ArrayList<String>();
	ArrayList<String> auditSummaryValidationsErrors = new ArrayList<String>();
	
	

	
	@BeforeClass
	public void init() throws Exception
	{
		Reporter.log("started test data setup...",true);
		//suiteData.setAuditGoldenSetTestDataSetup(new AuditGoldenSetTestDataSetup());
	}
	
	
	/**
	 * @param FireWallName
	 */
	public AuditWebUploadTests_2(String FireWallName) {
		restClient = new Client();
		this.fireWallType = FireWallName;
		
		
	}	

	/*
	
	*//**
	 * @throws IOException
	 * @throws Exception
	 *//*
	@Test()
	public void createDataSourceTest() throws IOException, Exception{	
		
		Reporter.log("********************************* Test Description ****************************************************** ", true);
		Reporter.log("1. Create Datasource through WebUpload Transportation", true);
		Reporter.log("2. Process the Datasource ", true);
		Reporter.log("3. Poll the Datasource status by calling the Datasource Api for every 2 minutes until it gets Completed ", true);
		Reporter.log("********************************************************************************************************* ", true);
		
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
		
		Reporter.log("************************** Datasource process Completed sucessfully::for the firewall: "+fireWallType, true);
		Reporter.log("**************************Datasource creation Test Completed**************************",true);
	}
	
	
	
	private void validateAuditSummaryJsonObject(JSONObject summaryObject) throws Exception
	{
		
		Assert.assertNotNull(summaryObject.getJSONObject("company_brr"), "company_brr is null");
		Assert.assertNotNull(summaryObject.getJSONObject("company_brr"), "company_brr is null");
		Assert.assertNotNull(summaryObject.getJSONObject("company_brr"), "company_brr is null");
		
		Assert.assertNotNull(summaryObject.getJSONObject("company_brr"), "company_brr is null");
	
		
	}

	
	
	public AuditSummary populateActualAuditSummaryObject(String fireWallType, String sourceID ) throws Exception
	{
		
		String range ="";

		 if(fireWallType.equals(AuditTestConstants.FIREWALL_SQUID_PROXY) ||
				 fireWallType.equals(AuditTestConstants.FIREWALL_BE_WSA_ACCESS ) )
		{
			range="1y";
		}
		 else{
			 range = "1mo";	
		 }
			List<NameValuePair> queryParam = new ArrayList<NameValuePair>();				
			queryParam.add(new BasicNameValuePair("format", "json"));
			queryParam.add(new BasicNameValuePair("range", range));
			queryParam.add(new BasicNameValuePair("ds_id", sourceID));
			HttpResponse response  = AuditFunctions.getAuditSummary(restClient, queryParam);				
			Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
			
		JSONObject summaryObject = (JSONObject) new JSONObject(ClientUtil.getResponseBody(response)).getJSONArray("objects").get(0);				
		Reporter.log("Actual Audit summar Api Response: "+fireWallType+"_"+sourceID+"::"+summaryObject,true);
		
		
		String summaryLabel;
		
		AuditSummary actualAuditSummaryObj=new AuditSummary();
		JSONObject companyBrr = summaryObject.getJSONObject("company_brr");
		String actual_audit_score= companyBrr.getString("value"); 
		actualAuditSummaryObj.setAuditScore(actual_audit_score);
		
		summaryLabel="total_services";
		String actual_audit_saasservices=summaryObject.getString(summaryLabel);
		actualAuditSummaryObj.setSaas_services_count(Integer.parseInt(actual_audit_saasservices));
		
		summaryLabel="total_destinations";
		String actual_audit_destinations=summaryObject.getString(summaryLabel);
		actualAuditSummaryObj.setDestination_count(Integer.parseInt(actual_audit_destinations));
		
		summaryLabel="total_users";
		String actual_audit_users=summaryObject.getString(summaryLabel);
		actualAuditSummaryObj.setUsers_count(Integer.parseInt(actual_audit_users));
		
		//Risky services
		String label="top_risky_services";
		int topRiskyServicesArrayLength=0;
		String top_risky_serviceBRR=null;
		String serviceId=null;
		String userCount=null;
		String serviceName=null;
		AuditSummaryTopRiskyServices auditSummaryTopRiskyServices=null;
		List<AuditSummaryTopRiskyServices> auditSummaryTopRiskyServicesList=new ArrayList<AuditSummaryTopRiskyServices>();
		Set<String> setServices=new HashSet<String>();
		List<String> totalAuditServicesList=new ArrayList<String>();;
		
		
		if (summaryObject.has(label) && !summaryObject.isNull(label)) {
			JSONArray topRiskyServicesArray = summaryObject.getJSONArray(label);
			topRiskyServicesArrayLength =topRiskyServicesArray.length();
			

			for(int i=0; i<topRiskyServicesArrayLength; i++)
			{
				auditSummaryTopRiskyServices= new AuditSummaryTopRiskyServices();
				serviceId=((JSONObject)topRiskyServicesArray.get(i)).getString("service_id");
				serviceName=AuditTestUtils.getServiceName(serviceId);
				auditSummaryTopRiskyServices.setServicename(serviceName);
				//setServices.add(serviceName);
				
				top_risky_serviceBRR=((JSONObject)topRiskyServicesArray.get(i)).getString("sort_key_brr");
				auditSummaryTopRiskyServices.setService_brr(top_risky_serviceBRR);
				
				userCount=((JSONObject)topRiskyServicesArray.get(i)).getString("users_count");
				auditSummaryTopRiskyServices.setService_user_count(Integer.parseInt(userCount));
				auditSummaryTopRiskyServicesList.add(auditSummaryTopRiskyServices);
				
			}
		}
		
		actualAuditSummaryObj.setSummaryTopRiskyServicesList(auditSummaryTopRiskyServicesList);
		
		//top used services
		label="top_used_services";
		int topUsedServicesArrayLength=0;

		if (summaryObject.has(label) && !summaryObject.isNull(label)) {
			JSONArray topUsedServicesArray = summaryObject.getJSONArray(label);
			topUsedServicesArrayLength =topUsedServicesArray.length();

			for(int i=0; i<topUsedServicesArrayLength; i++)
			{
				serviceId=((JSONObject)topUsedServicesArray.get(i)).getString("service_id");
				serviceName=AuditTestUtils.getServiceName(serviceId);
				//setServices.add(serviceName);
				
			}

		}
		
		SummaryTabDto summaryTabDto=AuditTestUtils.getServicesTabData(sourceID);
		for(String serviceID:summaryTabDto.getServiceUsersMap().keySet())
		{
			serviceName=AuditTestUtils.getServiceName(serviceID);
			setServices.add(serviceName);
		}
		actualAuditSummaryObj.setTotalAuditServicesList(new ArrayList<>(setServices));
		
		
		label="high_risk_services";
		String actual_audit_high_risky_services=summaryObject.getString(label);
		actualAuditSummaryObj.setHigh_risky_services_count(Integer.parseInt(actual_audit_high_risky_services));
		
		
		label="med_risk_services";
		String actual_audit_med_risk_services=summaryObject.getString(label);
		actualAuditSummaryObj.setMed_risky_services_count(Integer.parseInt(actual_audit_med_risk_services));
		
       return actualAuditSummaryObj;
		
	}
	@Test(dependsOnMethods={"createDataSourceTest"})
	//@Test
	public void testAuditSummaryNew() throws Exception {
		 Reporter.log("******************AuditSummary Test started for :****************"+fireWallType,true);
		 
		//sourceID="56908c989a1d910578b0673c"; //baracuda cli
		//sourceID="5690293851ea04357ef1e76b";//squidproxy
		
		goldenSetTestDataSetup=suiteData.getAuditGoldenSetTestDataSetup();
		//Reporter.log("fireWallType:"+fireWallType,true);
		Reporter.log("sourceID:"+sourceID,true);
		AuditSummary expectedAuditSummary=null;
		AuditGoldenSetDataController3 controller=null;
		List<GoldenSetData> goldenSetDataList = null;
		
		AuditSummary actualAuditSummary=null;
		
		switch(fireWallType)
		{
		case AuditTestConstants.FIREWALL_BE_BARRACUDA_CLI: {
		    controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_BARRACUDA_CLI_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			expectedAuditSummary = goldenSetTestDataSetup.populateAuditSummary(goldenSetDataList);
			Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);
			actualAuditSummary=this.populateActualAuditSummaryObject(fireWallType,sourceID);
			Reporter.log("actualAuditSummary: for::"+fireWallType+"  "+actualAuditSummary,true);
			auditSummaryValidationsErrors=AuditTestUtils.validateSummary(fireWallType,goldenSetTestDataSetup.getSernameNameWithServiceIdMap(),expectedAuditSummary,actualAuditSummary,auditSummaryValidationsErrors);
			Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_BARRACUDA_CLI+" Audit summary Info wrong ");
			break;
		}
		case AuditTestConstants.FIREWALL_BE_BARRACUDA_SYS: {
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_BARRACUDA_SYS_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			expectedAuditSummary = goldenSetTestDataSetup.populateAuditSummary(goldenSetDataList);
			Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);
			actualAuditSummary=this.populateActualAuditSummaryObject(fireWallType,sourceID);
			auditSummaryValidationsErrors=AuditTestUtils.validateSummary(fireWallType,goldenSetTestDataSetup.getSernameNameWithServiceIdMap(),expectedAuditSummary,actualAuditSummary,auditSummaryValidationsErrors);
			Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_BARRACUDA_SYS+" Audit summary Info wrong ");
			break;
		}
		
		case AuditTestConstants.FIREWALL_BE_BLUECOAT_PROXY: {
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_BLUECOAT_PROXY_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			expectedAuditSummary = goldenSetTestDataSetup.populateAuditSummary(goldenSetDataList);
			Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);
			actualAuditSummary=this.populateActualAuditSummaryObject(fireWallType,sourceID);
			auditSummaryValidationsErrors=AuditTestUtils.validateSummary(fireWallType,goldenSetTestDataSetup.getSernameNameWithServiceIdMap(),expectedAuditSummary,actualAuditSummary,auditSummaryValidationsErrors);
			Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_BLUECOAT_PROXY+" Audit summary Info wrong ");
			break;
		}
		

		case AuditTestConstants.FIREWALL_BE_BLUECOATPROXY_SPLUNK_WO_CH: {
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_BLUECOATPROXY_SPLUNK_WO_CH_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			expectedAuditSummary = goldenSetTestDataSetup.populateAuditSummary(goldenSetDataList);
			Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);
			actualAuditSummary=this.populateActualAuditSummaryObject(fireWallType,sourceID);
			//auditSummaryValidationsErrors=AuditTestUtils.validateSummary(fireWallType,goldenSetTestDataSetup.getSernameNameWithServiceIdMap(),expectedAuditSummary,actualAuditSummary,auditSummaryValidationsErrors);
			//Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_BLUECOATPROXY_SPLUNK_WO_CH+" Audit summary Info wrong ");
			break;
		}
		
		case AuditTestConstants.FIREWALL_CHECKPOINT_CSV: {
			break;
		}
		case AuditTestConstants.FIREWALL_BE_CHECKPOINT_SMARTVIEW: {
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_CHECKPOINT_SMARTVIEW_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			expectedAuditSummary = goldenSetTestDataSetup.populateAuditSummary(goldenSetDataList);
			Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);
			actualAuditSummary=this.populateActualAuditSummaryObject(fireWallType,sourceID);
			auditSummaryValidationsErrors=AuditTestUtils.validateSummary(fireWallType,goldenSetTestDataSetup.getSernameNameWithServiceIdMap(),expectedAuditSummary,actualAuditSummary,auditSummaryValidationsErrors);
			Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_CHECKPOINT_SMARTVIEW+" Audit summary Info wrong ");
			break;
		}
		
		
		case AuditTestConstants.FIREWALL_BE_JUNIPER_SCREENOS: {
		    controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_JUNIPER_SCREENOS_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			expectedAuditSummary = goldenSetTestDataSetup.populateAuditSummary(goldenSetDataList);
			Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);
			actualAuditSummary=this.populateActualAuditSummaryObject(fireWallType,sourceID);
			auditSummaryValidationsErrors=AuditTestUtils.validateSummary(fireWallType,goldenSetTestDataSetup.getSernameNameWithServiceIdMap(),expectedAuditSummary,actualAuditSummary,auditSummaryValidationsErrors);
			Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_JUNIPER_SCREENOS+" Audit summary Info wrong ");
			break;
		}
		
		case AuditTestConstants.FIREWALL_MCAFEE_SEF: {
		    controller = new AuditGoldenSetDataController3(AuditTestConstants.MCAFEE_SEF_SHEET);
			goldenSetDataList = controller.loadXlData();
			expectedAuditSummary = goldenSetTestDataSetup.populateAuditSummary(goldenSetDataList);
			Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);
			actualAuditSummary=this.populateActualAuditSummaryObject(fireWallType,sourceID);
			auditSummaryValidationsErrors=AuditTestUtils.validateSummary(fireWallType,goldenSetTestDataSetup.getSernameNameWithServiceIdMap(),expectedAuditSummary,actualAuditSummary,auditSummaryValidationsErrors);
			Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_MCAFEE_SEF+" Audit summary Info wrong ");
			break;
		}
		
		
	
		case AuditTestConstants.FIREWALL_BE_PANCSV: {
		    controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_PANCSV_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			expectedAuditSummary = goldenSetTestDataSetup.populateAuditSummary(goldenSetDataList);
			Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);
			actualAuditSummary=this.populateActualAuditSummaryObject(fireWallType,sourceID);
			auditSummaryValidationsErrors=AuditTestUtils.validateSummary(fireWallType,goldenSetTestDataSetup.getSernameNameWithServiceIdMap(),expectedAuditSummary,actualAuditSummary,auditSummaryValidationsErrors);
			Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_PANCSV+" Audit summary Info wrong ");
			break;
		}
		case AuditTestConstants.FIREWALL_BE_PANCSV_SPLUNK_WO_CH: {
		    controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_PANCSV_SPLUNK_WO_CH_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			expectedAuditSummary = goldenSetTestDataSetup.populateAuditSummary(goldenSetDataList);
			Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);
			actualAuditSummary=this.populateActualAuditSummaryObject(fireWallType,sourceID);
			auditSummaryValidationsErrors=AuditTestUtils.validateSummary(fireWallType,goldenSetTestDataSetup.getSernameNameWithServiceIdMap(),expectedAuditSummary,actualAuditSummary,auditSummaryValidationsErrors);
			Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_PANCSV_SPLUNK_WO_CH+" Audit summary Info wrong ");
			break;
		}
		
		case AuditTestConstants.FIREWALL_SQUID_PROXY: {
		    controller = new AuditGoldenSetDataController3(AuditTestConstants.SQUID_PROXY_SHEET);
			goldenSetDataList = controller.loadXlData();
			expectedAuditSummary = goldenSetTestDataSetup.populateAuditSummary(goldenSetDataList);
			Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);
			actualAuditSummary=this.populateActualAuditSummaryObject(fireWallType,sourceID);
			//squid proxy file we do not hava valid data set so we are commenting validation part.
			//auditSummaryValidationsErrors=AuditTestUtils.validateSummary(fireWallType,goldenSetTestDataSetup.getSernameNameWithServiceIdMap(),expectedAuditSummary,actualAuditSummary,auditSummaryValidationsErrors);
			//Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_SQUID_PROXY+" Audit summary Info wrong ");
			break;
		}
		case AuditTestConstants.FIREWALL_BE_WSAW3C: {
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_WSAW3C_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			expectedAuditSummary = goldenSetTestDataSetup.populateAuditSummary(goldenSetDataList);
			Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);
			actualAuditSummary=this.populateActualAuditSummaryObject(fireWallType,sourceID);
			auditSummaryValidationsErrors=AuditTestUtils.validateSummary(fireWallType,goldenSetTestDataSetup.getSernameNameWithServiceIdMap(),expectedAuditSummary,actualAuditSummary,auditSummaryValidationsErrors);
			Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_WSAW3C+" Audit summary Info wrong ");
			break;
		}
		case AuditTestConstants.FIREWALL_BE_WSA_ACCESS: {
		    controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_WSA_ACCESS_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			expectedAuditSummary = goldenSetTestDataSetup.populateAuditSummary(goldenSetDataList);
			Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);
			actualAuditSummary=this.populateActualAuditSummaryObject(fireWallType,sourceID);
			auditSummaryValidationsErrors=AuditTestUtils.validateSummary(fireWallType,goldenSetTestDataSetup.getSernameNameWithServiceIdMap(),expectedAuditSummary,actualAuditSummary,auditSummaryValidationsErrors);
			Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_WSA_ACCESS+" Audit summary Info wrong ");
			break;
		}
		case AuditTestConstants.FIREWALL_BE_ZSCALAR: {
		    controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_ZSCALAR_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			expectedAuditSummary = goldenSetTestDataSetup.populateAuditSummary(goldenSetDataList);
			Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);
			actualAuditSummary=this.populateActualAuditSummaryObject(fireWallType,sourceID);
			auditSummaryValidationsErrors=AuditTestUtils.validateSummary(fireWallType,goldenSetTestDataSetup.getSernameNameWithServiceIdMap(),expectedAuditSummary,actualAuditSummary,auditSummaryValidationsErrors);
			Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_ZSCALAR+" Audit summary Info wrong ");
			break;
		}
		
		case AuditTestConstants.FIREWALL_WALLMART_PAN_CSV: {
			Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);
			actualAuditSummary=this.populateActualAuditSummaryObject(fireWallType,sourceID);
			break;
		}
		case AuditTestConstants.FIREWALL_WALLMART_PAN_SYS: {
			Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);
			//actualAuditSummary=this.populateActualAuditSummaryObject(fireWallType,sourceID);
			break;
		}
		case AuditTestConstants.FIREWALL_WALLMART_MCAFEE_WEB_GATEWAY: {
			Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);
			actualAuditSummary=this.populateActualAuditSummaryObject(fireWallType,sourceID);
			break;
		}
		
		case AuditTestConstants.FIREWALL_JUNIPER_SRX: {
			Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);
			actualAuditSummary=this.populateActualAuditSummaryObject(fireWallType,sourceID);
			break;
		}
		case AuditTestConstants.FIREWALL_SCANSAFE: {
			Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);
			actualAuditSummary=this.populateActualAuditSummaryObject(fireWallType,sourceID);
			break;
		}
		case AuditTestConstants.FIREWALL_CISCO_ASA_SERIES: {
			Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);
			//actualAuditSummary=this.populateActualAuditSummaryObject(fireWallType,sourceID);
			break;
		}
	
	}
	}
		
	
	
	public AuditReport populateActualAuditReportData(String fireWallType,String sourceID) throws Exception
	{

		String range="";
		if(fireWallType.equals(AuditTestConstants.FIREWALL_SQUID_PROXY) ||
				fireWallType.equals(AuditTestConstants.FIREWALL_BE_WSA_ACCESS ) )
		{
			range="1y";
		}
		else{
			range = "1mo";	
		}

		List<NameValuePair> queryParam = new ArrayList<NameValuePair>();


		queryParam.add(new BasicNameValuePair("format", "json"));
		queryParam.add(new BasicNameValuePair("range", range));
		queryParam.add(new BasicNameValuePair("ds_id", sourceID));
		HttpResponse response = AuditFunctions.getAuditReport(restClient, queryParam);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
		JSONObject reportObject = (JSONObject) new JSONObject(ClientUtil.getResponseBody(response))
				.getJSONArray("objects").get(0);
		Reporter.log("Actual Audit Report Response:" + reportObject, true);


		String firewallType=null;
		String dsStatus=null;
		AuditReportTotals actualAuditReportTotal=null;
		AuditReportRiskyServices auditReportRiskyServices=null;
		AuditReportMostUsedServices auditReportMostUsedServices=null;
		AuditReportServiceCategories auditReportServiceCategories=null;
		AuditReport auditReport=new AuditReport();


		//Totals
		if (reportObject.has("total") && !reportObject.isNull("total")) {
			JSONObject totalobj = reportObject.getJSONObject("total");
			actualAuditReportTotal=new AuditReportTotals();
			actualAuditReportTotal.setUsers(Long.parseLong(totalobj.getString("users")));
			actualAuditReportTotal.setSessions(Long.parseLong(totalobj.getString("sessions")));
			actualAuditReportTotal.setServices(Long.parseLong(totalobj.getString("services")));
			actualAuditReportTotal.setLocations(Long.parseLong(totalobj.getString("locations")));
			actualAuditReportTotal.setCategories(Long.parseLong(totalobj.getString("categories")));
			actualAuditReportTotal.setTraffic(Long.parseLong(totalobj.getString("traffic")));
			actualAuditReportTotal.setUploads(Long.parseLong(totalobj.getString("uploads")));
			actualAuditReportTotal.setDownloads(Long.parseLong(totalobj.getString("downloads")));
			Reporter.log("actual actualAuditReportTotal::"+actualAuditReportTotal,true);

			auditReport.setAuditReportTotals(actualAuditReportTotal);
		}

		//Risky_services
		if (reportObject.has("risky_services") && !reportObject.isNull("risky_services")) {
			JSONObject risky_services_Obj = reportObject.getJSONObject("risky_services");
			auditReportRiskyServices=new AuditReportRiskyServices();

			auditReportRiskyServices.setMed_risky_services(Long.parseLong(risky_services_Obj.getString("medium_risk_services")));
			auditReportRiskyServices.setUsers(Long.parseLong(risky_services_Obj.getString("users")));
			auditReportRiskyServices.setSessions(Long.parseLong(risky_services_Obj.getString("sessions")));
			auditReportRiskyServices.setDownloads(Long.parseLong(risky_services_Obj.getString("downloads")));
			auditReportRiskyServices.setMost_used_services(Long.parseLong(risky_services_Obj.getString("mostused_services")));
			auditReportRiskyServices.setLocations(Long.parseLong(risky_services_Obj.getString("locations")));
			auditReportRiskyServices.setTotal_services(Long.parseLong(risky_services_Obj.getString("total_services")));
			auditReportRiskyServices.setUploads(Long.parseLong(risky_services_Obj.getString("uploads")));
			auditReportRiskyServices.setHigh_risky_services(Long.parseLong(risky_services_Obj.getString("high_risk_services")));
			auditReportRiskyServices.setTotal_traffic(Long.parseLong(risky_services_Obj.getString("total_traffic")));
			//auditReportRiskyServices.setUsers(Long.parseLong(risky_services_Obj.getString("new_disco_services")));
			auditReportRiskyServices.setCategories(Long.parseLong(risky_services_Obj.getString("categories")));
			Reporter.log("actual auditReportRiskyServices::"+auditReportRiskyServices,true);

			auditReport.setAuditReportRiskyServices(auditReportRiskyServices);
		}

		//most_used_services

		if (reportObject.has("most_used_services") && !reportObject.isNull("most_used_services")) {
			JSONObject most_used_services = reportObject.getJSONObject("most_used_services");
			auditReportMostUsedServices=new AuditReportMostUsedServices();

			auditReportMostUsedServices.setMed_risky_services(Long.parseLong(most_used_services.getString("medium_risk_services")));
			auditReportMostUsedServices.setUsers(Long.parseLong(most_used_services.getString("users")));
			auditReportMostUsedServices.setSessions(Long.parseLong(most_used_services.getString("sessions")));
			auditReportMostUsedServices.setDownloads(Long.parseLong(most_used_services.getString("downloads")));
			auditReportMostUsedServices.setLocations(Long.parseLong(most_used_services.getString("locations")));
			auditReportMostUsedServices.setTotal_services(Long.parseLong(most_used_services.getString("total_services")));
			auditReportMostUsedServices.setUploads(Long.parseLong(most_used_services.getString("uploads")));
			auditReportMostUsedServices.setHigh_risky_services(Long.parseLong(most_used_services.getString("high_risk_services")));
			auditReportMostUsedServices.setTotal_traffic(Long.parseLong(most_used_services.getString("total_traffic")));
			auditReportMostUsedServices.setCategories(Long.parseLong(most_used_services.getString("categories")));
			auditReportMostUsedServices.setRiskyServices(auditReportMostUsedServices.getHigh_risky_services()+auditReportMostUsedServices.getMed_risky_services());
			Reporter.log("actual auditReportMostUsedServices::"+auditReportMostUsedServices,true);
			auditReport.setAuditMostUsedServices(auditReportMostUsedServices);

		}

		//servicedetails object
		if (reportObject.has("service_details") && !reportObject.isNull("service_details")) {
			JSONObject serviceDetailsObject = reportObject.getJSONObject("service_details");

			JSONArray serviceDetailsDataArray = serviceDetailsObject.getJSONArray("data");
			int serviceDetailsArrayLength =serviceDetailsDataArray.length();

			JSONArray serviceDataArray;
			List<AuditReportServiceDetails> auditReportServiceDetailsList=new ArrayList<AuditReportServiceDetails>();
			AuditReportServiceDetails  auditReportServiceDetails= null;
			for(int i=0; i<serviceDetailsArrayLength; i++)
			{
				auditReportServiceDetails=new AuditReportServiceDetails();
				serviceDataArray=(JSONArray)serviceDetailsDataArray.get(i);
				auditReportServiceDetails.setServiceId((String)serviceDataArray.getString(0));
				auditReportServiceDetails.setIs_new((String)serviceDataArray.getString(1));
				auditReportServiceDetails.setIs_Most_Used((String)serviceDataArray.getString(2));
				auditReportServiceDetails.setCat1((String)serviceDataArray.getString(3));
				auditReportServiceDetails.setCat2((String)serviceDataArray.getString(4));
				auditReportServiceDetails.setCat3((String)serviceDataArray.getString(5));
				auditReportServiceDetails.setCat4((String)serviceDataArray.getString(6));
				auditReportServiceDetails.setUsers_Count(new Long((String)serviceDataArray.getString(7)).longValue());
				auditReportServiceDetails.setUploads(new Long((String)serviceDataArray.getString(8)).longValue());
				auditReportServiceDetails.setDownloads(new Long((String)serviceDataArray.getString(9)).longValue());
				auditReportServiceDetails.setSessions(new Long((String)serviceDataArray.getString(10)).longValue());
				auditReportServiceDetails.setLocations_Count(new Long((String)serviceDataArray.getString(11)).longValue());
				auditReportServiceDetails.setService_Brr((String)serviceDataArray.getString(12));
				auditReportServiceDetails.setService_Url((String)serviceDataArray.getString(13));
				auditReportServiceDetailsList.add(auditReportServiceDetails);


			}
			auditReport.setAuditReportServiceDetailsList(auditReportServiceDetailsList);
		}



		return auditReport;

	}
	
	@Test(dependsOnMethods={"testAuditSummaryNew"})
	public void testAuditReport() throws Exception {
		
	    Reporter.log("********************Audit Report test started*****************************"+fireWallType,true);
	   goldenSetTestDataSetup=suiteData.getAuditGoldenSetTestDataSetup();
		
		Reporter.log("************************sourceID:"+sourceID,true);
		
		AuditReport expectedAuditReport=null;
		AuditGoldenSetDataController3 controller=null;
		List<GoldenSetData> goldenSetDataList = null;
		AuditReport actualAuditReport=null;
		
		switch(fireWallType)
		{
		case AuditTestConstants.FIREWALL_BE_BARRACUDA_CLI: {
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_BARRACUDA_CLI_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			expectedAuditReport = goldenSetTestDataSetup.prepareAuditReportData(goldenSetDataList);
			Reporter.log("expectedAuditReport: for::"+fireWallType+"  "+expectedAuditReport,true);
			actualAuditReport=this.populateActualAuditReportData(fireWallType,sourceID);
			Reporter.log("actualAuditReport: for::"+fireWallType+"  "+actualAuditReport,true);
			auditReportValidationsErrors=AuditTestUtils.validateAuditReport(fireWallType,expectedAuditReport,actualAuditReport,auditReportValidationsErrors);
			Assert.assertTrue(auditReportValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_SQUID_PROXY+" Audit Report Info wrong ");
		break;
		}
		case AuditTestConstants.FIREWALL_BE_BARRACUDA_SYS: {
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_BARRACUDA_SYS_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			expectedAuditReport = goldenSetTestDataSetup.prepareAuditReportData(goldenSetDataList);
			Reporter.log("expectedAuditReport: for::"+fireWallType+"  "+expectedAuditReport,true);
			actualAuditReport=this.populateActualAuditReportData(fireWallType,sourceID);
			//Reporter.log("actualAuditReport: for::"+fireWallType+"  "+actualAuditReport,true);
			//auditReportValidationsErrors=AuditTestUtils.validateAuditReport(fireWallType,expectedAuditReport,actualAuditReport,auditReportValidationsErrors);
			//Assert.assertTrue(auditReportValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_SQUID_PROXY+" Audit Report Info wrong ");
		break;
		}
		
		case AuditTestConstants.FIREWALL_BE_BLUECOAT_PROXY: {
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_BLUECOAT_PROXY_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			expectedAuditReport = goldenSetTestDataSetup.prepareAuditReportData(goldenSetDataList);
			Reporter.log("expectedAuditReport: for::"+fireWallType+"  "+expectedAuditReport,true);
			actualAuditReport=this.populateActualAuditReportData(fireWallType,sourceID);
			//Reporter.log("actualAuditReport: for::"+fireWallType+"  "+actualAuditReport,true);
			//auditReportValidationsErrors=AuditTestUtils.validateAuditReport(fireWallType,expectedAuditReport,actualAuditReport,auditReportValidationsErrors);
			//Assert.assertTrue(auditReportValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_SQUID_PROXY+" Audit Report Info wrong ");
	    break;
		}
		
		case AuditTestConstants.FIREWALL_BE_BLUECOATPROXY_SPLUNK_WO_CH: {
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_BLUECOATPROXY_SPLUNK_WO_CH_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			expectedAuditReport = goldenSetTestDataSetup.prepareAuditReportData(goldenSetDataList);
			Reporter.log("expectedAuditReport: for::"+fireWallType+"  "+expectedAuditReport,true);
			actualAuditReport=this.populateActualAuditReportData(fireWallType,sourceID);
			//Reporter.log("actualAuditReport: for::"+fireWallType+"  "+actualAuditReport,true);
			//auditReportValidationsErrors=AuditTestUtils.validateAuditReport(fireWallType,expectedAuditReport,actualAuditReport,auditReportValidationsErrors);
			//Assert.assertTrue(auditReportValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_BLUECOATPROXY_SPLUNK_WO_CH+" Audit Report Info wrong ");
	     break;
		}
		
		case AuditTestConstants.FIREWALL_CHECKPOINT_CSV: {
			break;
		}
		case AuditTestConstants.FIREWALL_BE_CHECKPOINT_SMARTVIEW: {
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_CHECKPOINT_SMARTVIEW_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			expectedAuditReport = goldenSetTestDataSetup.prepareAuditReportData(goldenSetDataList);
			Reporter.log("expectedAuditReport: for::"+fireWallType+"  "+expectedAuditReport,true);
			actualAuditReport=this.populateActualAuditReportData(fireWallType,sourceID);
			//Reporter.log("actualAuditReport: for::"+fireWallType+"  "+actualAuditReport,true);
			//auditReportValidationsErrors=AuditTestUtils.validateAuditReport(fireWallType,expectedAuditReport,actualAuditReport,auditReportValidationsErrors);
			//Assert.assertTrue(auditReportValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_SQUID_PROXY+" Audit Report Info wrong ");
	     break;
		}
		
		case AuditTestConstants.FIREWALL_BE_JUNIPER_SCREENOS: {
		    controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_JUNIPER_SCREENOS_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			expectedAuditReport = goldenSetTestDataSetup.prepareAuditReportData(goldenSetDataList);
			Reporter.log("expectedAuditReport: for::"+fireWallType+"  "+expectedAuditReport,true);
			actualAuditReport=this.populateActualAuditReportData(fireWallType,sourceID);
			//Reporter.log("actualAuditReport: for::"+fireWallType+"  "+actualAuditReport,true);
			//auditReportValidationsErrors=AuditTestUtils.validateAuditReport(fireWallType,expectedAuditReport,actualAuditReport,auditReportValidationsErrors);
			//Assert.assertTrue(auditReportValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_JUNIPER_SCREENOS+" Audit Report Info wrong ");
	     break;
		}
		
		
		case AuditTestConstants.FIREWALL_MCAFEE_SEF: {
		    controller = new AuditGoldenSetDataController3(AuditTestConstants.MCAFEE_SEF_SHEET);
			goldenSetDataList = controller.loadXlData();
			expectedAuditReport = goldenSetTestDataSetup.prepareAuditReportData(goldenSetDataList);
			Reporter.log("expectedAuditReport: for::"+fireWallType+"  "+expectedAuditReport,true);
			actualAuditReport=this.populateActualAuditReportData(fireWallType,sourceID);
			//Reporter.log("actualAuditReport: for::"+fireWallType+"  "+actualAuditReport,true);
			//auditReportValidationsErrors=AuditTestUtils.validateAuditReport(fireWallType,expectedAuditReport,actualAuditReport,auditReportValidationsErrors);
			//Assert.assertTrue(auditReportValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_MCAFEE_SEF+" Audit Report Info wrong ");
	     break;
		}
		
		case AuditTestConstants.FIREWALL_BE_PANCSV: {
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_PANCSV_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			expectedAuditReport = goldenSetTestDataSetup.prepareAuditReportData(goldenSetDataList);
			Reporter.log("expectedAuditReport: for::"+fireWallType+"  "+expectedAuditReport,true);
			actualAuditReport=this.populateActualAuditReportData(fireWallType,sourceID);
			//Reporter.log("expectedAuditReport: for::"+fireWallType+"  "+expectedAuditReport,true);
			//auditReportValidationsErrors=AuditTestUtils.validateAuditReport(fireWallType,expectedAuditReport,actualAuditReport,auditReportValidationsErrors);
			//Assert.assertTrue(auditReportValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_SQUID_PROXY+" Audit Report Info wrong ");
	     break;
		}
		case AuditTestConstants.FIREWALL_BE_PANCSV_SPLUNK_WO_CH: {
		  	controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_PANCSV_SPLUNK_WO_CH_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			expectedAuditReport = goldenSetTestDataSetup.prepareAuditReportData(goldenSetDataList);
			Reporter.log("expectedAuditReport: for::"+fireWallType+"  "+expectedAuditReport,true);
			actualAuditReport=this.populateActualAuditReportData(fireWallType,sourceID);
			//Reporter.log("expectedAuditReport: for::"+fireWallType+"  "+expectedAuditReport,true);
			//auditReportValidationsErrors=AuditTestUtils.validateAuditReport(fireWallType,expectedAuditReport,actualAuditReport,auditReportValidationsErrors);
			//Assert.assertTrue(auditReportValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_PANCSV_SPLUNK_WO_CH+" Audit Report Info wrong ");
	     break;
		}
		case AuditTestConstants.FIREWALL_SQUID_PROXY: {
			controller = new AuditGoldenSetDataController3(AuditTestConstants.SQUID_PROXY_SHEET);
			goldenSetDataList = controller.loadXlData();
			expectedAuditReport = goldenSetTestDataSetup.prepareAuditReportData(goldenSetDataList);
			Reporter.log("expectedAuditReport: for::"+fireWallType+"  "+expectedAuditReport,true);
			actualAuditReport=this.populateActualAuditReportData(fireWallType,sourceID);
			Reporter.log("actualAuditReport: for::"+fireWallType+"  "+actualAuditReport,true);
			//squid proxy file we do not hava valid data set so we are commenting validation part.
			//auditReportValidationsErrors=AuditTestUtils.validateAuditReport(fireWallType,expectedAuditReport,actualAuditReport,auditReportValidationsErrors);
			//Assert.assertTrue(auditReportValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_SQUID_PROXY+" Audit Report Info wrong ");
	     break;
		}
		case AuditTestConstants.FIREWALL_BE_WSAW3C: {
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_WSAW3C_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			expectedAuditReport = goldenSetTestDataSetup.prepareAuditReportData(goldenSetDataList);
			Reporter.log("actualAuditReport: for::"+fireWallType+"  "+actualAuditReport,true);
			actualAuditReport=this.populateActualAuditReportData(fireWallType,sourceID);
			//Reporter.log("expectedAuditReport: for::"+fireWallType+"  "+expectedAuditReport,true);
			//auditReportValidationsErrors=AuditTestUtils.validateAuditReport(fireWallType,expectedAuditReport,actualAuditReport,auditReportValidationsErrors);
			//Assert.assertTrue(auditReportValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_WSAW3C+" Audit Report Info wrong ");
	     break;
		}
		case AuditTestConstants.FIREWALL_BE_WSA_ACCESS: {
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_WSA_ACCESS_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			expectedAuditReport = goldenSetTestDataSetup.prepareAuditReportData(goldenSetDataList);
			Reporter.log("expectedAuditReport: for::"+fireWallType+"  "+expectedAuditReport,true);
			actualAuditReport=this.populateActualAuditReportData(fireWallType,sourceID);
			//Reporter.log("expectedAuditReport: for::"+fireWallType+"  "+expectedAuditReport,true);
			//auditReportValidationsErrors=AuditTestUtils.validateAuditReport(fireWallType,expectedAuditReport,actualAuditReport,auditReportValidationsErrors);
			//Assert.assertTrue(auditReportValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_WSA_ACCESS+" Audit Report Info wrong ");
	     break;
		}
		case AuditTestConstants.FIREWALL_BE_ZSCALAR: {
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_ZSCALAR_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			expectedAuditReport = goldenSetTestDataSetup.prepareAuditReportData(goldenSetDataList);
			Reporter.log("expectedAuditReport: for::"+fireWallType+"  "+expectedAuditReport,true);
			actualAuditReport=this.populateActualAuditReportData(fireWallType,sourceID);
			//Reporter.log("expectedAuditReport: for::"+fireWallType+"  "+expectedAuditReport,true);
			//auditReportValidationsErrors=AuditTestUtils.validateAuditReport(fireWallType,expectedAuditReport,actualAuditReport,auditReportValidationsErrors);
			//Assert.assertTrue(auditReportValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_SQUID_PROXY+" Audit Report Info wrong ");
	     break;
		}
		case AuditTestConstants.FIREWALL_WALLMART_PAN_CSV: {
			actualAuditReport=this.populateActualAuditReportData(fireWallType,sourceID);
			Reporter.log("actualAuditReport: for::"+fireWallType+"  "+actualAuditReport,true);
			
			break;
		}
		case AuditTestConstants.FIREWALL_WALLMART_PAN_SYS: {
			actualAuditReport=this.populateActualAuditReportData(fireWallType,sourceID);
			Reporter.log("actualAuditReport: for::"+fireWallType+"  "+actualAuditReport,true);
			
			break;
		}
		case AuditTestConstants.FIREWALL_WALLMART_MCAFEE_WEB_GATEWAY: {
			actualAuditReport=this.populateActualAuditReportData(fireWallType,sourceID);
			Reporter.log("actualAuditReport: for::"+fireWallType+"  "+actualAuditReport,true);
			
			break;
		}
		
		case AuditTestConstants.FIREWALL_JUNIPER_SRX: {
			actualAuditReport=this.populateActualAuditReportData(fireWallType,sourceID);
			Reporter.log("actualAuditReport: for::"+fireWallType+"  "+actualAuditReport,true);
			
			break;
		}
		case AuditTestConstants.FIREWALL_SCANSAFE: {
			actualAuditReport=this.populateActualAuditReportData(fireWallType,sourceID);
			Reporter.log("actualAuditReport: for::"+fireWallType+"  "+actualAuditReport,true);
			
			break;
		}
		case AuditTestConstants.FIREWALL_CISCO_ASA_SERIES: {
			actualAuditReport=this.populateActualAuditReportData(fireWallType,sourceID);
			Reporter.log("actualAuditReport: for::"+fireWallType+"  "+actualAuditReport,true);
			
			break;
		}
	
	}
	}
	
	
	
	
	 * This test case deletes the data source
	 
	@Test(dependsOnMethods={"testAuditReport"})
	public void deleteDataSourceTest() throws Exception {
		Reporter.log("************************** Test Description ****************************** ", true);
		Reporter.log("1. Call Datasource delete Api for the created Datasource", true);
		Reporter.log("2. Deleting Data Source for "+ fireWallType +" its ID is: "+sourceID+" started",true);
		Reporter.log("************************************************************************** ", true);
		HttpResponse response = AuditFunctions.deleteDataSource(restClient, sourceID);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_NO_CONTENT);
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
	
	*//**
	 * This method will verify the Audit summary/Report results for un-processed logs
	 * @param sourceID
	 * @throws Exception
	 *//*
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
	*/
	
}
