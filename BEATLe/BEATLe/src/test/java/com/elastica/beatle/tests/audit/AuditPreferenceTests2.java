package com.elastica.beatle.tests.audit;

import static net.javacrumbs.jsonunit.JsonAssert.assertJsonEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.elastica.beatle.SFTPUtils;
import com.elastica.beatle.RestClient.Client;
import com.elastica.beatle.RestClient.ClientUtil;
import com.elastica.beatle.audit.AuditDSStatusDTO;
import com.elastica.beatle.audit.AuditFunctions;
import com.elastica.beatle.audit.AuditGoldenSetDataController3;
import com.elastica.beatle.audit.AuditGoldenSetTestDataSetup;
import com.elastica.beatle.audit.AuditInitializeTests;
import com.elastica.beatle.audit.AuditSummary;
import com.elastica.beatle.audit.AuditTestConstants;
import com.elastica.beatle.audit.AuditTestUtils;
import com.elastica.beatle.audit.GoldenSetData;
import com.elastica.beatle.fileHandler.FileHandlingUtils;
import com.elastica.beatle.logger.Logger;

/**
 * @author anuvrath
 *
 */
public class AuditPreferenceTests2 extends  AuditInitializeTests  {
	
	private Client restClient;
	String preferenceRespString;
	AuditGoldenSetTestDataSetup goldenSetTestDataSetup=null;
	private HashMap<String, String> serviceBrrMapWithDefaultPrefs;
	private HashMap<String, String> serviceBrrMapWithModifiedPrefs;
	//protected String sourceID = null;
	protected AuditDSStatusDTO auditDSStatusDTO;
	protected ArrayList<AuditDSStatusDTO> inCompleteDsList=new ArrayList<AuditDSStatusDTO>();
	Properties spanvaFirewalls=null;
	Properties processedDs=new Properties();
	List<String> dsList=new ArrayList<String>();
	HashMap<String, HashMap<String, String>> mapDsbefore=new HashMap<String,HashMap<String, String>>();
	HashMap<String, HashMap<String, String>> mapDsAfter=new HashMap<String,HashMap<String, String>>();



	public AuditPreferenceTests2(String FireWallName) {
		restClient = new Client();
		serviceBrrMapWithDefaultPrefs= new HashMap<String,String>();
		serviceBrrMapWithModifiedPrefs= new HashMap<String,String>();
		//this.fireWallType = FireWallName;
		spanvaFirewalls = new Properties();
		
		
	}		
	
	
	@Test(priority=1)
	public void restoreDefaultsPreferencesBeforeModify() throws Exception {
		//Reporter.log("*************restoreDefaultsPreferencesBeforeModify started for:"+fireWallType+"****************",true);
		preferenceRespString=AuditTestUtils.getRestoreDefaultsPreferenceJSON();
		HttpResponse preferenceChangeResponse = AuditFunctions.setAuditPreferences(restClient, new StringEntity(preferenceRespString));
		Assert.assertEquals(preferenceChangeResponse.getStatusLine().getStatusCode(), HttpStatus.SC_ACCEPTED);
	
	}
	
	@DataProvider(name = "currentSpanvaScpDataSources", parallel = true)
	public Object[][] populateCurrentSpanvaScpDataSources() throws Exception {
		String sourceID=null;
		spanvaFirewalls.load(new FileInputStream(
				FileHandlingUtils.getFileAbsolutePath(AuditTestConstants.PREFERENCE_FIREWALLS_LISTS)));

		Object[][] inputData = new Object[spanvaFirewalls.size()][2];
		int j = 0;
		String firewallType;
		for (String key : spanvaFirewalls.stringPropertyNames()) {
			firewallType = spanvaFirewalls.getProperty(key);
			// Download file using amazon S3 URL
			AuditFunctions.DownloadFileFormS3(firewallType);
			Thread.sleep(30000);
			
			Reporter.log("*************Datasource Creation started for:"+firewallType+"****************",true);
			String requestPayload=AuditTestUtils.createWebUploadPostBody(firewallType,suiteData.getEnvironmentName(),AuditTestConstants.AUDIT_WU_DS_NAME);
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
			//Logger.info("Getting signed URl for "+fireWallType+" to upload");
			List<NameValuePair> queryParams = new ArrayList<NameValuePair>();
			queryParams.add(new BasicNameValuePair("filename", AuditTestUtils.getFireWallLogFileName(firewallType)));
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
			AuditFunctions.DownloadFileFormS3(firewallType);
			Thread.sleep(30000);
			
			// upload firewall using amazon S3 URL
			Logger.info("Uploading file using S3 signed url for "+ firewallType);
			HttpResponse uploadFileResponse = AuditFunctions.uploadFirewallLogFile(restClient,signedURL.trim(), AuditTestUtils.getFirewallLogFilePath(firewallType));
			Assert.assertEquals(uploadFileResponse.getStatusLine().getStatusCode(), HttpStatus.SC_OK);		

			// Notify successful upload
			Logger.info("Notifying the upload status for "+firewallType);
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
			inputData[j][0] = firewallType;
			inputData[j][1] = sourceID;
			j++;
		}
		Reporter.log("inputData.." + inputData, true);

		return inputData;
	}

	@Test(priority = 2, dependsOnMethods = "restoreDefaultsPreferencesBeforeModify", dataProvider = "currentSpanvaScpDataSources", threadPoolSize = 19)
	public void testSpanVAScpDatasourcesVerification(String firewallType, String datasourceid)
			throws Exception {
		Reporter.log(
				"firewallType:- " + firewallType +  " datasourceid:- " + datasourceid,
				true);
		processedDs.put(firewallType, datasourceid);
		
		this.testDataSourceProcessAndMonitorLogsVerificationCheck(firewallType,datasourceid,dsList);
	}
	
	
	public void testDataSourceProcessAndMonitorLogsVerificationCheck(String firewallType,String sourceID,List<String> dsList) throws Exception {
		HttpResponse pollForStatusResponse = AuditFunctions.pollForDataSourceStatus(restClient, sourceID);
		Assert.assertEquals(pollForStatusResponse.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
		JSONObject pollRespObject = new JSONObject(ClientUtil.getResponseBody(pollForStatusResponse));
		String last_Status = pollRespObject.getString("last_status");
		long currentWaitTime = 0;
		long totalWaitTime = 36000000;
		while (("Pending Data".equals(last_Status) || "Pending Validation".equals(last_Status)
				|| "Queued".equals(last_Status) || "Processing".equals(last_Status))
				&& currentWaitTime <= totalWaitTime) {
			Thread.sleep(AuditTestConstants.AUDIT_THREAD_WAITTIME);
			currentWaitTime += AuditTestConstants.AUDIT_THREAD_WAITTIME;
			pollForStatusResponse = AuditFunctions.pollForDataSourceStatus(restClient, sourceID);
			Assert.assertEquals(pollForStatusResponse.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
			pollRespObject = new JSONObject(ClientUtil.getResponseBody(pollForStatusResponse));
			last_Status = pollRespObject.getString("last_status");
			Reporter.log("Last Status of " + sourceID + " is " + last_Status, true);
			/*
			 * finalMonitorLogElementsOfCompletedDS.addAll(
			 * verifyMonitorLogsDuringDataSourceProcess(sourceID)); for(String
			 * str: finalMonitorLogElementsOfCompletedDS) {
			 * Assert.assertFalse(str.contains("Upload failed"),
			 * "Upload Message captured in the SpanVA Monitor Logs"); }
			 */
			if ("Completed".equals(last_Status) || "Failed".equals(last_Status))
			{
				dsList.add(firewallType+"~"+sourceID);
			}
				
		}
		Assert.assertTrue(currentWaitTime <= totalWaitTime,
				" File processing took " + (int) ((totalWaitTime / (1000 * 60)) % 60)
						+ " minutes. Current Datasource details id - status:[" + sourceID + "-" + last_Status + "]");

	}
	
	@Test(priority=3,dependsOnMethods="testSpanVAScpDatasourcesVerification")
	public void testGetBrronDefaultPrefs() throws Exception
	{
		
		String[] stAr=null;
		String firewall=null;
		String dsId=null;
		if(!dsList.isEmpty())
		{
			for (String dsIdString: dsList)
			{
				stAr=dsIdString.split("~");
				firewall=stAr[0];
				dsId=stAr[1];
				serviceBrrMapWithDefaultPrefs=this.testAuditBrrRateVerification(serviceBrrMapWithDefaultPrefs,firewall,dsId);
				mapDsbefore.put(dsId, serviceBrrMapWithDefaultPrefs);
				
				Reporter.log("*** serviceBrrMapWithDefaultPrefs ***"+mapDsbefore,true);
			}
		}
		
	}
	
	@Test(priority=4,dependsOnMethods={"testGetBrronDefaultPrefs"})	
	public void testChangeExistingValidPreferenceWeights() throws Exception {
		// Get existing preferences
		
		HttpResponse existingPreferences = AuditFunctions.getAuditPreferences(restClient, null);
		Assert.assertEquals(existingPreferences.getStatusLine().getStatusCode(), HttpStatus.SC_OK);		
		JSONObject preferenceMetaObjects = new JSONObject(ClientUtil.getResponseBody(existingPreferences)).getJSONObject("meta");								
		List<NameValuePair> queryParams = new ArrayList<NameValuePair>();		
		queryParams.add(new BasicNameValuePair("limit", String.valueOf(preferenceMetaObjects.getInt("total_count"))));
		queryParams.add(new BasicNameValuePair("offset", String.valueOf(0)));
		existingPreferences = AuditFunctions.getAuditPreferences(restClient, queryParams);
		preferenceRespString = ClientUtil.getResponseBody(existingPreferences);	
		
		
		// change the attribute_weight for all the preferences
		Integer[] attr_weight = {0,1,3,8};		
		JSONArray existingPreferenceObject  = new JSONObject(preferenceRespString).getJSONArray("objects");
		JSONArray postBodyJSON = new JSONArray();
		for(int index = 0; index < existingPreferenceObject.length();index++){
			JSONObject object = existingPreferenceObject.getJSONObject(index);
			object.remove("attribute_weight");
			object.put("attribute_weight", attr_weight[new Random().nextInt(attr_weight.length)]);			
			postBodyJSON.put(object);			
		}		
		String postBody = new JSONObject().put("objects",postBodyJSON).toString();
		HttpResponse preferenceChangeResponse = AuditFunctions.setAuditPreferences(restClient, new StringEntity(postBody));
		Assert.assertEquals(preferenceChangeResponse.getStatusLine().getStatusCode(), HttpStatus.SC_ACCEPTED);			
		
		// Get the preferences again to validate
		HttpResponse preferenceGetResponse = AuditFunctions.getAuditPreferences(restClient, queryParams);
		Assert.assertEquals(preferenceGetResponse.getStatusLine().getStatusCode(), HttpStatus.SC_OK);						
		String getreponseString = ClientUtil.getResponseBody(preferenceGetResponse);
		JSONArray getResponseArray = new JSONObject(getreponseString).getJSONArray("objects");		
		assertJsonEquals(getResponseArray.toString(), new JSONObject(postBody).getJSONArray("objects").toString());				
		
		for(int index = 0; index< getResponseArray.length();index++){
			JSONObject attributeObject = getResponseArray.getJSONObject(index);
			Assert.assertFalse(((String)attributeObject.get("attribute_category")).isEmpty());
			Assert.assertFalse(((String)attributeObject.getString("attribute_risk_bucket")).isEmpty(),"Risk Bucket is empty");
			Assert.assertFalse(((String)attributeObject.get("attribute_name")).isEmpty(),"Attribute Name is empty");
			Assert.assertFalse(((String)attributeObject.get("id")).isEmpty(),"id is empty");
			Assert.assertFalse(((String)attributeObject.get("resource_uri")).isEmpty(),"resource_uri is empty");			
			Assert.assertNotNull(attributeObject.get("attribute_category"), "attribute category is null");
			Assert.assertNotNull(attributeObject.get("attribute_id"),"attribute id is null");
			Assert.assertNotNull(attributeObject.get("attribute_name"),"name is null");
			Assert.assertNotNull(attributeObject.get("id"),"id is null");
			Assert.assertNotNull(attributeObject.get("resource_uri"),"resource_uri is null");						
		}	
		
	}
	
	@Test(priority=5,dependsOnMethods="testChangeExistingValidPreferenceWeights")
	public void testGetBrronChangedPrefs() throws Exception
	{	
		String[] stAr=null;
		String firewall=null;
		String dsId=null;
		if(!dsList.isEmpty())
		{
			for (String dsIdString: dsList)
			{
				stAr=dsIdString.split("~");
				firewall=stAr[0];
				dsId=stAr[1];
				serviceBrrMapWithModifiedPrefs=testAuditBrrRateVerification(serviceBrrMapWithModifiedPrefs,firewall,dsId);
				mapDsAfter.put(dsId, serviceBrrMapWithDefaultPrefs);
				Reporter.log("*** serviceBrrMapWithDefaultPrefs ***"+mapDsAfter,true);
				}
		}
	}
	
	@Test(priority=6,dependsOnMethods="testGetBrronChangedPrefs")
	public void compareBrrDefaultPrefsWithModifiedPrefs() throws Exception
	{
		compareserviceBrrMap(mapDsbefore,mapDsAfter);
	}
	
	@Test(priority=7,dependsOnMethods="compareBrrDefaultPrefsWithModifiedPrefs")
	public void restoreDefaultsPreferencesAfterModify() throws Exception {
		
		preferenceRespString=AuditTestUtils.getRestoreDefaultsPreferenceJSON();
		HttpResponse preferenceChangeResponse = AuditFunctions.setAuditPreferences(restClient, new StringEntity(preferenceRespString));
		Assert.assertEquals(preferenceChangeResponse.getStatusLine().getStatusCode(), HttpStatus.SC_ACCEPTED);
	
	}
	
	//datasource creation part
	//brr verification test
	public HashMap<String, String> testAuditBrrRateVerification(HashMap<String, String> serviceBrrMap,String firewallType,String sourceID) throws Exception {
		Reporter.log("******************AuditSummary Test started for :****************"+firewallType,true);
		//goldenSetTestDataSetup=suiteData.getAuditGoldenSetTestDataSetup();
		//Reporter.log("fireWallType:"+fireWallType,true);
		
		//sourceID="578359d9c80e0e06e61f7be8";
		
		Reporter.log("sourceID:"+sourceID,true);
		AuditSummary expectedAuditSummary=null;
		AuditGoldenSetDataController3 controller=null;
		List<GoldenSetData> goldenSetDataList = null;

		AuditSummary actualAuditSummary=null;

		switch(firewallType)
		{

		case AuditTestConstants.FIREWALL_BE_BARRACUDA_CLI: 
		case AuditTestConstants.FIREWALL_BE_BARRACUDA_CLI_7Z:
		case AuditTestConstants.FIREWALL_BE_BARRACUDA_CLI_7ZA:{
			Reporter.log("Audit Summary Verification Test stated for ."+firewallType,true);
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_BARRACUDA_CLI_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			goldenSetTestDataSetup=new AuditGoldenSetTestDataSetup(goldenSetDataList);
			expectedAuditSummary = goldenSetTestDataSetup.populateAuditSummary(goldenSetDataList);
			Reporter.log("expectedAuditSummary: for::"+firewallType+"  "+expectedAuditSummary,true);
			//actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID);
			actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(firewallType,sourceID,"86400","1391385600","1393977599");
			Reporter.log("actualAuditSummary: for::"+firewallType+"  "+actualAuditSummary,true);
			Reporter.log("actualAuditSummary: for::"+actualAuditSummary,true);
			serviceBrrMap=actualAuditSummary.getServiceBrrMap();
			
			break;
		}
		case AuditTestConstants.FIREWALL_BE_BARRACUDA_SYS:
		case AuditTestConstants.FIREWALL_BE_BARRACUDA_SYS_7Z:
		case AuditTestConstants.FIREWALL_BE_BARRACUDA_SYS_7ZA:{
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_BARRACUDA_SYS_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			goldenSetTestDataSetup=new AuditGoldenSetTestDataSetup(goldenSetDataList);
			expectedAuditSummary = goldenSetTestDataSetup.populateAuditSummary(goldenSetDataList);
			Reporter.log("expectedAuditSummary: for::"+firewallType+"  "+expectedAuditSummary,true);
			//actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID);
			actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(firewallType,sourceID,"86400","1391385600","1393977599");
			Reporter.log("actualAuditSummary: for::"+actualAuditSummary,true);
			serviceBrrMap=actualAuditSummary.getServiceBrrMap();
			
			break;
		}

		case AuditTestConstants.FIREWALL_BE_BLUECOAT_PROXY: 
		case AuditTestConstants.FIREWALL_BE_BLUECOAT_PROXY_7Z: 
		case AuditTestConstants.FIREWALL_BE_BLUECOAT_PROXY_7ZA: {
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_BLUECOAT_PROXY_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			goldenSetTestDataSetup=new AuditGoldenSetTestDataSetup(goldenSetDataList);
			expectedAuditSummary = goldenSetTestDataSetup.populateAuditSummary(goldenSetDataList);
			Reporter.log("expectedAuditSummary: for::"+firewallType+"  "+expectedAuditSummary,true);
			//actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID);
			actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(firewallType,sourceID,"86400","1394928000","1397519999");
			Reporter.log("actualAuditSummary: for::"+actualAuditSummary,true);
			serviceBrrMap=actualAuditSummary.getServiceBrrMap();
			break;
		}


		case AuditTestConstants.FIREWALL_BE_BLUECOATPROXY_SPLUNK_WO_CH:
		case AuditTestConstants.FIREWALL_BE_BLUECOATPROXY_SPLUNK_WO_CH_7Z:
		case AuditTestConstants.FIREWALL_BE_BLUECOATPROXY_SPLUNK_WO_CH_7ZA:{
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_BLUECOATPROXY_SPLUNK_WO_CH_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			goldenSetTestDataSetup=new AuditGoldenSetTestDataSetup(goldenSetDataList);
			expectedAuditSummary = goldenSetTestDataSetup.populateAuditSummary(goldenSetDataList);
			Reporter.log("expectedAuditSummary: for::"+firewallType+"  "+expectedAuditSummary,true);
			//actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID);
			actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(firewallType,sourceID,"86400","1442448000","1445039999");
			Reporter.log("actualAuditSummary: for::"+actualAuditSummary,true);
			serviceBrrMap=actualAuditSummary.getServiceBrrMap();
			//auditSummaryValidationsErrors=AuditTestUtils.validateSummary(fireWallType,goldenSetTestDataSetup.getSernameNameWithServiceIdMap(),expectedAuditSummary,actualAuditSummary,auditSummaryValidationsErrors);
			//Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_BLUECOATPROXY_SPLUNK_WO_CH+" Audit summary Info wrong ");
			break;
		}

		case AuditTestConstants.FIREWALL_CHECKPOINT_CSV:
		case AuditTestConstants.FIREWALL_BE_CHECKPOINT_CSV_7Z:
		case AuditTestConstants.FIREWALL_BE_CHECKPOINT_CSV_7ZA:{
			break;
		}
		case AuditTestConstants.FIREWALL_BE_CHECKPOINT_SMARTVIEW:
		case AuditTestConstants.FIREWALL_BE_CHECKPOINT_SMARTVIEW_7Z:
		case AuditTestConstants.FIREWALL_BE_CHECKPOINT_SMARTVIEW_7ZA:{
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_CHECKPOINT_SMARTVIEW_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			goldenSetTestDataSetup=new AuditGoldenSetTestDataSetup(goldenSetDataList);
			expectedAuditSummary = goldenSetTestDataSetup.populateAuditSummary(goldenSetDataList);
			Reporter.log("expectedAuditSummary: for::"+firewallType+"  "+expectedAuditSummary,true);
			//actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID);
			actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(firewallType,sourceID,"86400","1437609600","1440201599");
			Reporter.log("actualAuditSummary: for::"+actualAuditSummary,true);
			serviceBrrMap=actualAuditSummary.getServiceBrrMap();
			break;
		}


		case AuditTestConstants.FIREWALL_BE_JUNIPER_SCREENOS:
		case AuditTestConstants.FIREWALL_BE_JUNIPER_SCREENOS_7Z:
		case AuditTestConstants.FIREWALL_BE_JUNIPER_SCREENOS_7ZA:{
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_JUNIPER_SCREENOS_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			goldenSetTestDataSetup=new AuditGoldenSetTestDataSetup(goldenSetDataList);
			expectedAuditSummary = goldenSetTestDataSetup.populateAuditSummary(goldenSetDataList);
			Reporter.log("expectedAuditSummary: for::"+firewallType+"  "+expectedAuditSummary,true);
			//actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID);
			actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(firewallType,sourceID,"86400","1387152000","1389743999");
			
			Reporter.log("actualAuditSummary: for::"+actualAuditSummary,true);
			serviceBrrMap=actualAuditSummary.getServiceBrrMap();
			break;
		}

		case AuditTestConstants.FIREWALL_MCAFEE_SEF: 
		case AuditTestConstants.FIREWALL_MCAFEE_SEF_7Z: 
		case AuditTestConstants.FIREWALL_MCAFEE_SEF_7ZA: {
			controller = new AuditGoldenSetDataController3(AuditTestConstants.MCAFEE_SEF_SHEET);
			goldenSetDataList = controller.loadXlData();
			goldenSetTestDataSetup=new AuditGoldenSetTestDataSetup(goldenSetDataList);
			expectedAuditSummary = goldenSetTestDataSetup.populateAuditSummary(goldenSetDataList);
			Reporter.log("expectedAuditSummary: for::"+firewallType+"  "+expectedAuditSummary,true);
		//	actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID);
			actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(firewallType,sourceID,"86400","1387670400","1390262399");
			
			Reporter.log("actualAuditSummary: for::"+actualAuditSummary,true);
			serviceBrrMap=actualAuditSummary.getServiceBrrMap();
			break;
		}

   	case AuditTestConstants.FIREWALL_BE_PANCSV:
		case AuditTestConstants.FIREWALL_BE_PANCSV_7Z:
		case AuditTestConstants.FIREWALL_BE_PANCSV_7ZA:{
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_PANCSV_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			goldenSetTestDataSetup=new AuditGoldenSetTestDataSetup(goldenSetDataList);
			expectedAuditSummary = goldenSetTestDataSetup.populateAuditSummary(goldenSetDataList);
			Reporter.log("expectedAuditSummary: for::"+firewallType+"  "+expectedAuditSummary,true);
			//actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID);
			actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(firewallType,sourceID,"86400","1377388800","1379980799");
			
			Reporter.log("actualAuditSummary: for::"+actualAuditSummary,true);
			serviceBrrMap=actualAuditSummary.getServiceBrrMap();
			
			break;
		}
		case AuditTestConstants.FIREWALL_BE_PANCSV_SPLUNK_WO_CH:
		case AuditTestConstants.FIREWALL_BE_PANCSV_SPLUNK_WO_CH_7Z:
		case AuditTestConstants.FIREWALL_BE_PANCSV_SPLUNK_WO_CH_7ZA:{
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_PANCSV_SPLUNK_WO_CH_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			goldenSetTestDataSetup=new AuditGoldenSetTestDataSetup(goldenSetDataList);
			expectedAuditSummary = goldenSetTestDataSetup.populateAuditSummary(goldenSetDataList);
			Reporter.log("expectedAuditSummary: for::"+firewallType+"  "+expectedAuditSummary,true);
			//actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID);
			actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(firewallType,sourceID,"86400","1442275200","1444867199");
			
			
			Reporter.log("actualAuditSummary: for::"+actualAuditSummary,true);
			serviceBrrMap=actualAuditSummary.getServiceBrrMap();
			break;
		}

		case AuditTestConstants.FIREWALL_SQUID_PROXY: 
		case AuditTestConstants.FIREWALL_SQUID_PROXY_7Z: 
		case AuditTestConstants.FIREWALL_SQUID_PROXY_7ZA: {
			controller = new AuditGoldenSetDataController3(AuditTestConstants.SQUID_PROXY_SHEET);
			goldenSetDataList = controller.loadXlData();
			goldenSetTestDataSetup=new AuditGoldenSetTestDataSetup(goldenSetDataList);
			expectedAuditSummary = goldenSetTestDataSetup.populateAuditSummary(goldenSetDataList);
			Reporter.log("expectedAuditSummary: for::"+firewallType+"  "+expectedAuditSummary,true);
			//actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID);
			actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(firewallType,sourceID,"2592000","1372636800","1404172799");
			Reporter.log("actualAuditSummary: for::"+actualAuditSummary,true);
			serviceBrrMap=actualAuditSummary.getServiceBrrMap();
			//squid proxy file we do not hava valid data set so we are commenting validation part.
			//auditSummaryValidationsErrors=AuditTestUtils.validateSummary(fireWallType,goldenSetTestDataSetup.getSernameNameWithServiceIdMap(),expectedAuditSummary,actualAuditSummary,auditSummaryValidationsErrors);
			//Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_SQUID_PROXY+" Audit summary Info wrong ");
			break;
		}
		case AuditTestConstants.FIREWALL_BE_WSAW3C:
		case AuditTestConstants.FIREWALL_BE_WSAW3C_7Z:
		case AuditTestConstants.FIREWALL_BE_WSAW3C_7ZA:{
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_WSAW3C_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			goldenSetTestDataSetup=new AuditGoldenSetTestDataSetup(goldenSetDataList);
			expectedAuditSummary = goldenSetTestDataSetup.populateAuditSummary(goldenSetDataList);
			Reporter.log("expectedAuditSummary: for::"+firewallType+"  "+expectedAuditSummary,true);
			//actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID);
			actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(firewallType,sourceID,"86400","1412467200","1415059199");
			
			Reporter.log("actualAuditSummary: for::"+actualAuditSummary,true);
			serviceBrrMap=actualAuditSummary.getServiceBrrMap();
			
			break;
		}
		case AuditTestConstants.FIREWALL_BE_WSA_ACCESS: 
		case AuditTestConstants.FIREWALL_BE_WSA_ACCESS_7Z: 
		case AuditTestConstants.FIREWALL_BE_WSA_ACCESS_7ZA: {
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_WSA_ACCESS_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			goldenSetTestDataSetup=new AuditGoldenSetTestDataSetup(goldenSetDataList);
			expectedAuditSummary = goldenSetTestDataSetup.populateAuditSummary(goldenSetDataList);
			Reporter.log("expectedAuditSummary: for::"+firewallType+"  "+expectedAuditSummary,true);
			//actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID);
			actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(firewallType,sourceID,"2592000","1388534400","1420070399");
			
			Reporter.log("actualAuditSummary: for::"+actualAuditSummary,true);
			serviceBrrMap=actualAuditSummary.getServiceBrrMap();
			break;
		}
		case AuditTestConstants.FIREWALL_BE_ZSCALAR:
		case AuditTestConstants.FIREWALL_BE_ZSCALAR_7Z:
		case AuditTestConstants.FIREWALL_BE_ZSCALAR_7ZA:{
			controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_ZSCALAR_DATA_SHEET);
			goldenSetDataList = controller.loadXlData();
			goldenSetTestDataSetup=new AuditGoldenSetTestDataSetup(goldenSetDataList);
			expectedAuditSummary = goldenSetTestDataSetup.populateAuditSummary(goldenSetDataList);
			Reporter.log("expectedAuditSummary: for::"+firewallType+"  "+expectedAuditSummary,true);
		//	actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID);
			actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(firewallType,sourceID,"86400","1432857600","1435449599");
			
			Reporter.log("actualAuditSummary: for::"+actualAuditSummary,true);
			serviceBrrMap=actualAuditSummary.getServiceBrrMap();
			break;
		}
		case AuditTestConstants.FIREWALL_WEBSENSE_ARC:
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
		case AuditTestConstants.FIREWALL_WEBSENSE_HOSTED:
		{
			controller = new AuditGoldenSetDataController3(AuditTestConstants.FIREWALL_WEBSENSE_HOSTED);
			goldenSetDataList = controller.loadXlData();
			goldenSetTestDataSetup=new AuditGoldenSetTestDataSetup(goldenSetDataList);
			expectedAuditSummary = goldenSetTestDataSetup.populateAuditSummary(goldenSetDataList);
			Reporter.log("expectedAuditSummary: for::"+firewallType+"  "+expectedAuditSummary,true);
			actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(firewallType,sourceID,"86400","1414886400","1417478399");
			//auditSummaryValidationsErrors=AuditTestUtils.validateSummary(fireWallType,goldenSetTestDataSetup.getSernameNameWithServiceIdMap(),expectedAuditSummary,actualAuditSummary,auditSummaryValidationsErrors);
			//Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_ZSCALAR+" Audit summary Info wrong ");
			Reporter.log("actualAuditSummary: for::"+actualAuditSummary,true);
			serviceBrrMap=actualAuditSummary.getServiceBrrMap();
			break;
		}
		case AuditTestConstants.FIREWALL_WEBSENSE_ARC_TAR:
		{
			controller = new AuditGoldenSetDataController3(AuditTestConstants.FIREWALL_WEBSENSE_ARC);
			goldenSetDataList = controller.loadXlData();
			goldenSetTestDataSetup=new AuditGoldenSetTestDataSetup(goldenSetDataList);
			expectedAuditSummary = goldenSetTestDataSetup.populateAuditSummary(goldenSetDataList);
			Reporter.log("expectedAuditSummary: for::"+firewallType+"  "+expectedAuditSummary,true);
			//actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID);
			//auditSummaryValidationsErrors=AuditTestUtils.validateSummary(fireWallType,goldenSetTestDataSetup.getSernameNameWithServiceIdMap(),expectedAuditSummary,actualAuditSummary,auditSummaryValidationsErrors);
			//Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_ZSCALAR+" Audit summary Info wrong ");
			Reporter.log("actualAuditSummary: for::"+actualAuditSummary,true);
			serviceBrrMap=actualAuditSummary.getServiceBrrMap();
			break;
		}
		case AuditTestConstants.FIREWALL_SONICWALL:
		{
			/*controller = new AuditGoldenSetDataController3(AuditTestConstants.FIREWALL_WEBSENSE_ARC);
			goldenSetDataList = controller.loadXlData();
			expectedAuditSummary = goldenSetTestDataSetup.populateAuditSummary(goldenSetDataList);
			Reporter.log("expectedAuditSummary: for::"+fireWallType+"  "+expectedAuditSummary,true);*/
			//actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID);
			actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(firewallType,sourceID,"86400","1394064000","1396655999");
			
			Reporter.log("actualAuditSummary: for::"+actualAuditSummary,true);
			serviceBrrMap=actualAuditSummary.getServiceBrrMap();
			
			//auditSummaryValidationsErrors=AuditTestUtils.validateSummary(fireWallType,goldenSetTestDataSetup.getSernameNameWithServiceIdMap(),expectedAuditSummary,actualAuditSummary,auditSummaryValidationsErrors);
			//Assert.assertTrue(auditSummaryValidationsErrors.isEmpty(),AuditTestConstants.FIREWALL_BE_ZSCALAR+" Audit summary Info wrong ");
			
			break;
		}

		case AuditTestConstants.FIREWALL_WALLMART_PAN_CSV: {
			Reporter.log("expectedAuditSummary: for::"+firewallType+"  "+expectedAuditSummary,true);
			//actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID);
			break;
		}
		case AuditTestConstants.FIREWALL_WALLMART_PAN_SYS: {
			Reporter.log("expectedAuditSummary: for::"+firewallType+"  "+expectedAuditSummary,true);
			//actualAuditSummary=this.populateActualAuditSummaryObject(fireWallType,sourceID);
			break;
		}
		case AuditTestConstants.FIREWALL_WALLMART_MCAFEE_WEB_GATEWAY: {
			Reporter.log("expectedAuditSummary: for::"+firewallType+"  "+expectedAuditSummary,true);
			//actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID);
			break;
		}

		case AuditTestConstants.FIREWALL_JUNIPER_SRX: {
			Reporter.log("expectedAuditSummary: for::"+firewallType+"  "+expectedAuditSummary,true);
			//actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID);
			actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(firewallType,sourceID,"86400","1434326400","1436918399");
			Reporter.log("actualAuditSummary: for::"+actualAuditSummary,true);
			serviceBrrMap=actualAuditSummary.getServiceBrrMap();
			break;
		}
		case AuditTestConstants.FIREWALL_SCANSAFE: {
			Reporter.log("expectedAuditSummary: for::"+firewallType+"  "+expectedAuditSummary,true);
			//actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(fireWallType,sourceID);
			actualAuditSummary=AuditTestUtils.populateActualAuditSummaryObject(firewallType,sourceID,"86400","1399161600","1401753599");
			Reporter.log("actualAuditSummary: for::"+actualAuditSummary,true);
			serviceBrrMap=actualAuditSummary.getServiceBrrMap();
			break;
		}
		case AuditTestConstants.FIREWALL_CISCO_ASA_SERIES: {
			Reporter.log("expectedAuditSummary: for::"+firewallType+"  "+expectedAuditSummary,true);
			//actualAuditSummary=this.populateActualAuditSummaryObject(fireWallType,sourceID);
			break;
		}

		}
		return serviceBrrMap;
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

	public static void compareserviceBrrMap(HashMap<String,HashMap<String, String>> serviceBeforeBrrMap, HashMap<String,HashMap<String, String>> serviceAfterBrrMap) throws Exception{
				
				Reporter.log("service Before Brr Map: for::"+serviceBeforeBrrMap,true);
				Reporter.log("service After  Brr Map: for::"+serviceAfterBrrMap,true);
				Assert.assertNotEquals(serviceAfterBrrMap, serviceBeforeBrrMap," - Both Brr list are same");
		
	}
	
	
}
