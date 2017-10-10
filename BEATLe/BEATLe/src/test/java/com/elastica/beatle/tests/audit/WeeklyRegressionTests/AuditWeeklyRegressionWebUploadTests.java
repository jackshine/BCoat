/**
 * 
 */
package com.elastica.beatle.tests.audit.WeeklyRegressionTests;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
import com.elastica.beatle.audit.AuditGoldenSetDataController3;
import com.elastica.beatle.audit.AuditGoldenSetTestDataSetup;
import com.elastica.beatle.audit.AuditInitializeTests;
import com.elastica.beatle.audit.AuditReport;
import com.elastica.beatle.audit.AuditSummary;
import com.elastica.beatle.audit.AuditTestConstants;
import com.elastica.beatle.audit.AuditTestUtils;
import com.elastica.beatle.audit.AuditWeeklyRegressionConstants;
import com.elastica.beatle.audit.GoldenSetData;
import com.elastica.beatle.logger.Logger;

/**
 * @author anuvrath
 *
 */
public class AuditWeeklyRegressionWebUploadTests extends AuditInitializeTests{
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
	ArrayList<String> auditSummaryForConsumerServicesValidationsErrors = new ArrayList<String>();



	/**
	 * @param FireWallName
	 */
	public AuditWeeklyRegressionWebUploadTests(String FireWallName) {
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
		
		// Download file using amazon S3 URL
		AuditFunctions.DownloadFileFormS3(fireWallType);
		Thread.sleep(30000);
		
		// upload firewall using amazon S3 URL
		Logger.info("Uploading file using S3 signed url for "+ fireWallType);
		HttpResponse uploadFileResponse = AuditFunctions.uploadFirewallLogFile(restClient,signedURL.trim(), AuditTestUtils.getFirewallLogFilePath(fireWallType));
		Logger.info("Expected fireWallType:"+AuditTestUtils.getFirewallLogFilePath(fireWallType));
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



	
//populateActualAuditSummaryofConsumerServices
	
	@Test(dependsOnMethods={"createDataSourceTest"})
	
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


	@Test(dependsOnMethods={"testAuditReport"})
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
	public void testPopulateAuditSummaryConsumerFailures() throws Exception
	{
		Reporter.log("*****************WebUpload Regression: Audit Summary Validation consumer Errors for "+fireWallType,true);

		for(String str: auditSummaryForConsumerServicesValidationsErrors)
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
