/**
 * 
 */
package com.elastica.beatle.tests.audit;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.TimeZone;

import javax.xml.bind.JAXBException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.introspect.JacksonAnnotationIntrospector;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.elastica.beatle.FileCompressionUtils.CompressionUtils;
import com.elastica.beatle.RestClient.Client;
import com.elastica.beatle.RestClient.ClientUtil;
import com.elastica.beatle.audit.AuditDSStatusDTO;
import com.elastica.beatle.audit.AuditFunctions;
import com.elastica.beatle.audit.AuditGoldenSetDataController3;
import com.elastica.beatle.audit.AuditInitializeTests;
import com.elastica.beatle.audit.AuditSummary;
import com.elastica.beatle.audit.AuditTestConstants;
import com.elastica.beatle.audit.AuditTestUtils;
import com.elastica.beatle.audit.TIAIncidentDto;
import com.elastica.beatle.audit.TIA_IOI_Code;
import com.elastica.beatle.audit.TiaFirewallDto;
import com.elastica.beatle.detect.dto.AttributeBean;
import com.elastica.beatle.es.ESQueryBuilder;
import com.elastica.beatle.es.ElasticSearchLogs;
import com.elastica.beatle.logger.Logger;

/**
 * @author Mallesh
 *
 */
public class AuditTIAWebUploadTests extends AuditInitializeTests
{
	protected Client restClient;	
	protected String FireWallType; 
	protected String sourceID = null;
	protected Properties firewallLogDataProps;
	protected AuditDSStatusDTO auditDSStatusDTO;
	protected ArrayList<AuditDSStatusDTO> inCompleteDsList=new ArrayList<AuditDSStatusDTO>();
	ArrayList<String> goldenSetErrorList=new ArrayList<String>();
	ESQueryBuilder esQueryBuilder;
	private static final String OBJECTS = "audit_preferences";
	private static final String TIA_SUCESS_LAST_DETECT_MSG="Anomalies analysis completed";
	private static final String TIA_FAILED_LAST_DETECT_MSG="Anomalies analysis failed";
	@SuppressWarnings("unused")
	private static final String EXPECTED_TIA_LAST_DETECT_STATUS="Completed";
	private String lowReputationUser=null;
	private String bc_upload_download_TBI_BBI_User=null;
	private String bc_nosessions_TBI_BBI_User=null;



	/**
	 * @param FireWallName
	 */
	public AuditTIAWebUploadTests(String FireWallName) {
		restClient = new Client();
		this.FireWallType = FireWallName;
		esQueryBuilder = new ESQueryBuilder();

	}	
	@BeforeClass(alwaysRun=true)
	public void clearTIAIncident() throws Exception {
		Reporter.log("********************************* clearTIAIncident:Test Description ****************************************************** ", true);
		Reporter.log("***************Cleanup up test for all the incidents for last 6 months for the tenant*************************************",true);
		Reporter.log("Get all the TIA incidents for the last 6 months************",true);
		String responseBody =  getListOfIncidents();
		JsonNode jnode = unmarshall(responseBody, JsonNode.class);
		if (jnode.isArray()) {
			for (final JsonNode objNode : jnode) {

				String report_id = getJSONValue(objNode.toString(), "id").toString().replace("\"", "");
				String severity = getJSONValue(objNode.toString(), "s").toString().replace("\"", "");
				String facilty = getJSONValue(objNode.toString(), "f").toString().replace("\"", "");
				String email = getJSONValue(objNode.toString(), "e").toString().replace("\"", "");
				String index = getJSONValue(objNode.toString(), "index").toString().replace("\"", "");
				String source = getJSONValue(objNode.toString(), "src").toString().replace("\"", "");
				String attr_set = getJSONValue(objNode.toString(), "ats").toString().replace("\"", "");
				String Activity_type = getJSONValue(objNode.toString(), "at").toString().replace("\"", "");
				String ObjectType = getJSONValue(objNode.toString(), "ot").toString().replace("\"", "");

				Reporter.log("facilty:"+facilty+" email:"+email+" index:"+index+" source:"+source+" attr_set:"+attr_set+" Activity_type:"+Activity_type+" ObjectType:"+ObjectType,true);
				updateLog(report_id, severity, ObjectType, email, Activity_type, index, facilty, source, attr_set);
				Reporter.log("*************Completed TIA incidents cleanup test************",true);

			}
		}
		else{
			Reporter.log("*************No TIA Preferences are available to cleanup***********",true);
		}

	}

	@Test(priority=1)
	public void setTIAPreferences() throws Exception {
		Reporter.log("********************************* setTIAPreferences:Test Description ****************************************************** ", true);
		Reporter.log("***************TIA preferences Reset Test to the preference: TOO_MANY_LOW_REPUTATION_DESTINATIONS *************************************",true);
		Reporter.log("***************Resetting the preferences (threshold=1, window=3 mins, importance=important(2) ) to the preference TOO_MANY_LOW_REPUTATION_DESTINATIONS  *************************************",true);
		
		String responseBody;
		Reporter.log("get All the TIA preferences",true);
		HttpResponse resp = AuditFunctions.getTIAAuditPreferences(restClient);
		responseBody = ClientUtil.getResponseBody(resp);
		Reporter.log("get All the TIA preference Response..."+responseBody,true);
		org.json.JSONArray getResponseArray = new JSONObject(responseBody).getJSONArray(OBJECTS);
		TiaFirewallDto tiafirewallDto=null;
		String tmpFile="tempFile";
		
		switch(FireWallType)
		{
		case AuditTestConstants.TIA_FIREWALL_BC_ULDL_TBI_BBI: 
		{
			
		//setting the tia preferences
			String[] ioi_Codes_tbi  = { AuditTestConstants.TBI_INCIDENT_DOWNLOADS,AuditTestConstants.TBI_INCIDENT_UPLOADS};
			AttributeBean tbi_incidents_downloads_uploads_attributeBean = new AttributeBean(500, 10, 2, true);
			AuditTestUtils.updateTIAAttributes(tbi_incidents_downloads_uploads_attributeBean.isEnabled(), tbi_incidents_downloads_uploads_attributeBean, getResponseArray, ioi_Codes_tbi);
			
			 resp = AuditFunctions.getTIAAuditPreferences(restClient);
			responseBody = ClientUtil.getResponseBody(resp);
			Reporter.log("get All the TIA preference Response..."+responseBody,true);
			org.json.JSONArray getResponseArray1 = new JSONObject(responseBody).getJSONArray(OBJECTS);
			
			
			String[] ioi_Codes_bbi  = { AuditTestConstants.BBI_INCIDENT_ANAMALOUSLY_DOWNLOADS,AuditTestConstants.BBI_INCIDENT_ANAMALOUSLY_UPLOADS};
			AttributeBean bbi_incidents_download_upload = new AttributeBean(10, 2, true);
			AuditTestUtils.updateBBIAttributes(bbi_incidents_download_upload.isEnabled(), bbi_incidents_download_upload, getResponseArray1, ioi_Codes_bbi);
			
			
			//rename the user of the log file
			tiafirewallDto=new TiaFirewallDto();
			tiafirewallDto.setTempDirPath(AuditTestConstants.TIA_COMPRESSED_FILES_PATH);
			tiafirewallDto.setTiaFileName(tmpFile);
			tiafirewallDto.setTiaFileFormat(".log");
			tiafirewallDto.setActualFileName(AuditTestConstants.TIA_LOG_FILES_PATH+"BlueCoatBigSample_20160126_UploadDownloadBBI_PROFILE_INCIDENT_29Apr.log");
			tiafirewallDto.setTempRenamedActualFile(AuditTestConstants.TIA_COMPRESSED_FILES_PATH+"BlueCoatBigSample_20160126_UploadDownloadBBI_PROFILE_INCIDENT_29Apr.log");
			tiafirewallDto.setZipFileName(AuditTestConstants.TIA_COMPRESSED_FILES_PATH+"BlueCoat_UploadDownloadBBI_BC26_26Jan2016.zip");
			tiafirewallDto.setTiaUser(AuditTestConstants.TIA_FIREWALL_BC_ULDL_TBI_BBI_USER);
			tiafirewallDto.setRandomId(Math.round(Math.random()*1000));
			bc_upload_download_TBI_BBI_User=tiafirewallDto.getTiaUser()+"_"+tiafirewallDto.getRandomId();
			CompressionUtils.editTheFirewallLogAndMakeItCompressionFormat(tiafirewallDto);
			break;
		}
		case AuditTestConstants.TIA_FIREWALL_BC_NO_SESSIONS_TBI:
		{
			
			String[] ioi_Codes_tbi  = { "TOO_MANY_SESSIONS"};
			AttributeBean tbi_incidents_too_many_sessions_attributeBean = new AttributeBean(50, 10, 2, true);
			AuditTestUtils.updateTIAAttributes(tbi_incidents_too_many_sessions_attributeBean.isEnabled(), tbi_incidents_too_many_sessions_attributeBean, getResponseArray, ioi_Codes_tbi);
			
			tiafirewallDto=new TiaFirewallDto();
			tiafirewallDto.setTempDirPath(AuditTestConstants.TIA_COMPRESSED_FILES_PATH);
			tiafirewallDto.setTiaFileName(tmpFile);
			tiafirewallDto.setTiaFileFormat(".log");
			tiafirewallDto.setActualFileName(AuditTestConstants.TIA_LOG_FILES_PATH+"BlueCoat_NoSessions_50_BC03_03Jan2016.log");
			tiafirewallDto.setTempRenamedActualFile(AuditTestConstants.TIA_COMPRESSED_FILES_PATH+"BlueCoat_NoSessions_50_BC03_03Jan2016.log");
			tiafirewallDto.setZipFileName(AuditTestConstants.TIA_COMPRESSED_FILES_PATH+"BlueCoat_NoSessions_50_BC03_03Jan2016.zip");
			tiafirewallDto.setTiaUser(AuditTestConstants.TIA_FIREWALL_BC_NO_SESSIONS_TBI_USER);
			tiafirewallDto.setRandomId(Math.round(Math.random()*1000));
			bc_nosessions_TBI_BBI_User=tiafirewallDto.getTiaUser()+"_"+tiafirewallDto.getRandomId();
			CompressionUtils.editTheFirewallLogAndMakeItCompressionFormat(tiafirewallDto);
			
			break;
		}
		
		case AuditTestConstants.TIA_FIREWALL_MCA2:
		{
			
			String[] ioi_Codes_tbi  = { "TOO_MANY_LOW_REPUTATION_DESTINATIONS"};
			AttributeBean tbi_incidents_toomany_low_reputations_attributeBean = new AttributeBean(1, 3, 2, true);
			AuditTestUtils.updateTIAAttributes(tbi_incidents_toomany_low_reputations_attributeBean.isEnabled(), tbi_incidents_toomany_low_reputations_attributeBean, getResponseArray, ioi_Codes_tbi);
			
			tiafirewallDto=new TiaFirewallDto();
			tiafirewallDto.setTempDirPath(AuditTestConstants.TIA_COMPRESSED_FILES_PATH);
			tiafirewallDto.setTiaFileName(tmpFile);
			tiafirewallDto.setTiaFileFormat(".log");
			tiafirewallDto.setActualFileName(AuditTestConstants.TIA_LOG_FILES_PATH+"McAfee_LowRepURL_01Apr16_UserTIA01.log");
			tiafirewallDto.setTempRenamedActualFile(AuditTestConstants.TIA_COMPRESSED_FILES_PATH+"McAfee_LowRepURL_01Apr16_UserTIA01.log");
			tiafirewallDto.setZipFileName(AuditTestConstants.TIA_COMPRESSED_FILES_PATH+"McAfee_LowRepURL_01Apr16_UserTIA01.zip");
			tiafirewallDto.setTiaUser(AuditTestConstants.TIA_FIREWALL_MCA2_USER);
			tiafirewallDto.setRandomId(Math.round(Math.random()*1000));
			lowReputationUser=tiafirewallDto.getTiaUser()+"_"+tiafirewallDto.getRandomId();
			CompressionUtils.editTheFirewallLogAndMakeItCompressionFormat(tiafirewallDto);
			break;
		}
		}
	
	}
	
	@Test(priority=5,dependsOnMethods={"testAuditReport"})	
	public void testValidateTIAIncidents1() throws Exception{
		Reporter.log("********************************* testValidateTIAIncidents:Test Description ****************************************************** ", true);
		Reporter.log("*****************Validate  TIA Incidents started ***************************",true);
		
		
		switch(FireWallType)
		{
		case AuditTestConstants.TIA_FIREWALL_BC_ULDL_TBI_BBI: 
		{
			String[] ioi_Codes_tbi  = { AuditTestConstants.TBI_INCIDENT_DOWNLOADS,
					                    AuditTestConstants.TBI_INCIDENT_UPLOADS,
					                    AuditTestConstants.BBI_INCIDENT_ANAMALOUSLY_DOWNLOADS,
					                    AuditTestConstants.BBI_INCIDENT_ANAMALOUSLY_UPLOADS};
			TIAIncidentDto tiaIncidentDto=null;
			ArrayList<TIAIncidentDto> expecteListOfIncidents=new ArrayList<TIAIncidentDto>();
			for(String incidentType:ioi_Codes_tbi)
			{
				tiaIncidentDto=new TIAIncidentDto();
				//tiaIncidentDto.setIncidentType(incidentType);
				tiaIncidentDto.setIncidentSeverity("low");
				tiaIncidentDto.setService("Salesforce");
				tiaIncidentDto.setIncidentDate("2016-01-26");
				tiaIncidentDto.setUserOfIncident(bc_upload_download_TBI_BBI_User);
				
				expecteListOfIncidents.add(tiaIncidentDto);
			}
			
			validateIncidents(AuditTestConstants.TIA_FIREWALL_BC_ULDL_TBI_BBI,bc_upload_download_TBI_BBI_User,expecteListOfIncidents);
			break;
		}
		case AuditTestConstants.TIA_FIREWALL_BC_NO_SESSIONS_TBI:
		{
			String[] ioi_Codes_tbi  = { "TOO_MANY_SESSIONS"};
			TIAIncidentDto tiaIncidentDto=null;
			ArrayList<TIAIncidentDto> expecteListOfIncidents=new ArrayList<TIAIncidentDto>();
			for(String incidentType:ioi_Codes_tbi)
			{
				tiaIncidentDto=new TIAIncidentDto();
				tiaIncidentDto.setIncidentType(incidentType);
				tiaIncidentDto.setIncidentSeverity("low");
				tiaIncidentDto.setService("Salesforce");
				tiaIncidentDto.setIncidentDate("2016-01-03");
				tiaIncidentDto.setUserOfIncident(bc_nosessions_TBI_BBI_User);
			    expecteListOfIncidents.add(tiaIncidentDto);
			}
			validateIncidents(AuditTestConstants.TIA_FIREWALL_BC_NO_SESSIONS_TBI,bc_nosessions_TBI_BBI_User,expecteListOfIncidents);
			
			break;
		}
		case AuditTestConstants.TIA_FIREWALL_MCA2:
		{
			String[] ioi_Codes_tbi  = { "TOO_MANY_LOW_REPUTATION_DESTINATIONS"};
			TIAIncidentDto tiaIncidentDto=null;
			ArrayList<TIAIncidentDto> expecteListOfIncidents=new ArrayList<TIAIncidentDto>();
			for(String incidentType:ioi_Codes_tbi)
			{
				tiaIncidentDto=new TIAIncidentDto();
				tiaIncidentDto.setIncidentType(incidentType);
				tiaIncidentDto.setIncidentSeverity("low");
				tiaIncidentDto.setService("Across Services");
				tiaIncidentDto.setIncidentDate("2016-04-01");
				tiaIncidentDto.setUserOfIncident(lowReputationUser);
				expecteListOfIncidents.add(tiaIncidentDto);
			}
			
			validateIncidents(AuditTestConstants.TIA_FIREWALL_MCA2,lowReputationUser,expecteListOfIncidents);
			break;
		
		}
			
		}
		
	}
		
	

		public void validateIncidents(String firewallType, String user,ArrayList<TIAIncidentDto> expecteListOfIncidents) throws Exception {
			Reporter.log("Sleep 1 min for generated incidents************",true);
			//Thread.sleep(1* 60* 1000);
			String responseBody =  getListOfIncidents();
			Reporter.log("getListOfIncidents::"+responseBody,true);
			boolean isIncidentsListed=false;
			isIncidentsListed=isIncidentListedNew(responseBody,user);
			
			long totalWaitTime=20*60*1000;
			long incidentsWaittime=1* 60* 1000;
			long currentWaitTime=0;
			Reporter.log("Incidents generated -"+isIncidentsListed,true);
			while(!(isIncidentsListed) && currentWaitTime <= totalWaitTime){
				Reporter.log("Incidents generated Wait Time*************** :"+currentWaitTime,true);
				Thread.sleep(incidentsWaittime);
				currentWaitTime += incidentsWaittime;
				isIncidentsListed=isIncidentListed(responseBody,user);
				if(isIncidentsListed)
				{
					Reporter.log("Incidents generated -time["+isIncidentsListed+"-"+currentWaitTime+"]",true);
					break;
				}
				
			}
			
			if(isIncidentsListed){//then do the validation
				JsonNode jnode = unmarshall(responseBody, JsonNode.class);
				validateUserIncidents(jnode,expecteListOfIncidents);
			}
			else{
				Assert.assertTrue(isIncidentsListed,"Incidents not generated :["+firewallType+"-"+user+"]");
			}
			
			
			
		}
		
		public Boolean isIncidentListedNew(String responseBody, String user) throws Exception {
			JsonNode jnode = unmarshall(responseBody, JsonNode.class);
			System.out.println("Respon : " + responseBody);
			if (jnode.isArray()) {
				for (final JsonNode objNode : jnode) {
					String userValue = getJSONValue(objNode.toString(), "u").toString().replace("\"", "");
					//String email = getJSONValue(objNode.toString(), "e").toString().replace("\"", "");
					Assert.assertEquals(userValue, user,"User should be: ");

					if ( userValue.equals(user)) {
					   return true;
						
					}
				}
			}

			return false;
		}
		
		public void validateUserIncidents(JsonNode jnode,ArrayList<TIAIncidentDto> expecteListOfIncidents)
		{
			
			Assert.assertEquals(jnode.size(), expecteListOfIncidents.size(), "Incidents size should be correct");
			
			TIAIncidentDto actualTIAIncidentDto=null;
			ArrayList<TIAIncidentDto> actualListOfIncidents=new ArrayList<TIAIncidentDto>();
			if (jnode.isArray()) {
				for (final JsonNode objNode : jnode) {
					actualTIAIncidentDto=new TIAIncidentDto();
					
					String incidentType = getJSONValue(objNode.toString(), "ioi").toString().replace("\"", "");
					actualTIAIncidentDto.setIncidentType(incidentType);
					
					String actualSeverity = getJSONValue(objNode.toString(), "s").toString().replace("\"", "");
					actualTIAIncidentDto.setIncidentSeverity(actualSeverity);
				
					String actualService = getJSONValue(objNode.toString(), "f").toString().replace("\"", "");
					actualTIAIncidentDto.setService(actualService);
					
					String date = getJSONValue(objNode.toString(), "its").toString().replace("\"", "");
					actualTIAIncidentDto.setIncidentDate(date.substring(0,date.indexOf('T')));
					
				
					String usr = getJSONValue(objNode.toString(), "u").toString().replace("\"", "");
					//Assert.assertEquals(usr, "UserTIA30 Hilton");
					actualTIAIncidentDto.setUserOfIncident(usr);

					actualListOfIncidents.add(actualTIAIncidentDto);
				
				}
			}
			
			Reporter.log("Expected List of incidents:"+expecteListOfIncidents,true);
			Reporter.log("Actual List of incidents:"+actualListOfIncidents,true);
			
			Collections.sort(expecteListOfIncidents);
			Collections.sort(actualListOfIncidents);
			
			
			
			//CollectionUtils.isEqualCollection(arg0, arg1)(Collections.sort(expecteListOfIncidents), Collections.sort(actualListOfIncidents));
			
			//Assert.assertTrue(CollectionUtils.isEqualCollection(expecteListOfIncidents, actualListOfIncidents),"Generated Incidents are not equal");
			
			
		}

	
	
	/**
	 * @throws IOException
	 * @throws Exception
	 * process the datasource
	 */
	@Test(priority=2)
	public void createDataSourceTestForTIALogs() throws IOException, Exception{	

		Reporter.log("********************************* Test Description ****************************************************** ", true);
		Reporter.log("1. Create TIA Datasource through WebUpload Transportation", true);
		Reporter.log("2. Process the Datasource ", true);
		Reporter.log("3. Poll the Datasource status by calling the Datasource Api for every 2 minutes until it gets Completed ", true);
		Reporter.log("********************************************************************************************************* ", true);

		Reporter.log("*************Datasource Creation started for:"+FireWallType+"****************",true);
		String requestPayload=AuditTestUtils.createWebUploadPostBody(FireWallType,suiteData.getEnvironmentName(),FireWallType);
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
		Logger.info("Getting signed URl for "+FireWallType+" to upload");
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
		String last_detect_msg ="";
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
			last_detect_msg=pollRespObject.getString("last_detect_message");
			Logger.info("Actual Last Status & tia anomaly status of Datasource: "+ sourceID +" is "+ last_Status+","+last_detect_msg);

			if(  ("Completed".equals(last_Status) || "Failed".equals(last_Status) )){//checking the datasource status
				Thread.sleep(900000);
				pollForStatusResponse = AuditFunctions.pollForDataSourceStatus(restClient, sourceID);
				Assert.assertEquals(pollForStatusResponse.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
				pollRespObject = new JSONObject(ClientUtil.getResponseBody(pollForStatusResponse));
				
				last_detect_msg=pollRespObject.getString("last_detect_message");
				Logger.info("Actual Last Status & tia anomaly status of Datasource: "+ sourceID +" is "+ last_Status+","+last_detect_msg);
				if(TIA_SUCESS_LAST_DETECT_MSG.equals(last_detect_msg))
				{
					Reporter.log("enter into TIA anomalies completed section:",true);
					break;
				}
				else{
					Reporter.log("enter into TIA failed section:"+last_detect_msg,true);
					Assert.assertEquals(last_detect_msg,TIA_SUCESS_LAST_DETECT_MSG,"Expected TIA last_detect_message is:"+TIA_SUCESS_LAST_DETECT_MSG+" but found:"+last_detect_msg);
				}
			}
		
	}
		if(!"Completed".equals(last_Status) || !"Failed".equals(last_Status))
			Assert.assertTrue(currentWaitTime <= AuditTestConstants.AUDIT_PROCESSING_MAX_WAITTIME," File processing took more than SLA. Last status of this source file was "+last_Status);
		
	}
	
	@Test(priority=3,dependsOnMethods={"createDataSourceTestForTIALogs"})	
	public void testAuditSummary() throws Exception {
		Reporter.log("************************** Test Description ****************************** ", true);
		Reporter.log("1. Call Audit summary Api for the created Datasource", true);
		Reporter.log("2. Verify the Audit summary data (date_range, id, earliest_date, latest_date...)", true);
		Reporter.log("************************************************************************** ", true);
		Reporter.log("**********Audit Summary Verification Test for "+ FireWallType +" its ID is: "+sourceID+" started********* ",true);
		
		
		HttpResponse response;
		JSONObject summaryObject=null;
		
		switch(FireWallType)
		{
		
		case AuditTestConstants.TIA_FIREWALL_MCA2: 
		{
			
			response  = AuditFunctions.getSummary(restClient, new StringEntity(AuditTestUtils.getAuditSummaryLatest(sourceID,"86400","1456963200","1459555199")));				
			Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_CREATED);
			
			String strResponse=ClientUtil.getResponseBody(response);
			JSONArray jsonarray = new JSONArray(strResponse);
			summaryObject = jsonarray.getJSONObject(0);	
			
			Assert.assertEquals(summaryObject.get("datasource_id"), sourceID);										   
			Assert.assertNotNull(summaryObject.get("earliest_date"),"earliest date is null");		
			Assert.assertNotNull(summaryObject.get("latest_date"),"Latest date is null");	
			break;
		}
		case AuditTestConstants.TIA_FIREWALL_BC_ULDL_TBI_BBI:{
			response  = AuditFunctions.getSummary(restClient, new StringEntity(AuditTestUtils.getAuditSummaryLatest(sourceID,"86400","1451260800","1453852799")));				
			Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_CREATED);
			
			String strResponse=ClientUtil.getResponseBody(response);
			JSONArray jsonarray = new JSONArray(strResponse);
			summaryObject = jsonarray.getJSONObject(0);	
			
			Assert.assertEquals(summaryObject.get("datasource_id"), sourceID);										   
			Assert.assertNotNull(summaryObject.get("earliest_date"),"earliest date is null");		
			Assert.assertNotNull(summaryObject.get("latest_date"),"Latest date is null");	
			break;
		}
		
	    case AuditTestConstants.TIA_FIREWALL_BC_NO_SESSIONS_TBI:{
	    	response  = AuditFunctions.getSummary(restClient, new StringEntity(AuditTestUtils.getAuditSummaryLatest(sourceID,"86400","1451260800","1453852799")));				
			Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_CREATED);
			
			String strResponse=ClientUtil.getResponseBody(response);
			JSONArray jsonarray = new JSONArray(strResponse);
			summaryObject = jsonarray.getJSONObject(0);	
			
			Assert.assertEquals(summaryObject.get("datasource_id"), sourceID);										   
			Assert.assertNotNull(summaryObject.get("earliest_date"),"earliest date is null");		
			Assert.assertNotNull(summaryObject.get("latest_date"),"Latest date is null");	
			break;
	    }
		
		}	

	}
	
	@Test(priority=4,dependsOnMethods={"testAuditSummary"})
	public void testAuditReport() throws Exception {
		Reporter.log("************************** Test Description ****************************** ", true);
		Reporter.log("1. Call Audit Report Api verification test for the created Datasource", true);
		Reporter.log("2. Verify the Audit Report data (date_range, datasource_id, earliest_date, latest_date...)", true);
		Reporter.log("************************************************************************** ", true);
		Reporter.log("**********Audit Report Verification Test for "+ FireWallType +" its ID is: "+sourceID+" started********* ",true);
		
		
		HttpResponse response =null;
		JSONObject reportObject=null;
		switch(FireWallType)
		{
		
		case AuditTestConstants.TIA_FIREWALL_MCA2: 
		{
			
			
			response  = AuditFunctions.getAuditReport(restClient, new StringEntity(AuditTestUtils.getAuditReportPayloadLatest(sourceID,"86400","1456963200","1459555199")));				
			Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_CREATED);
			
			String strResponse=ClientUtil.getResponseBody(response);
			JSONArray jsonarray = new JSONArray(strResponse);
			reportObject = jsonarray.getJSONObject(0);
			Reporter.log("Actual Audit Report Response:"+reportObject,true);
			
			Assert.assertEquals(reportObject.get("datasource_id"), sourceID);
			Assert.assertNotNull(reportObject.get("earliest_date"),"earliest date is null");		
			Assert.assertNotNull(reportObject.get("latest_date"),"Latest date is null");
			
			
			break;
		}
		case AuditTestConstants.TIA_FIREWALL_BC_ULDL_TBI_BBI:{
			response  = AuditFunctions.getAuditReport(restClient, new StringEntity(AuditTestUtils.getAuditReportPayloadLatest(sourceID,"86400","1451260800","1453852799")));				
			Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_CREATED);
			
			String strResponse=ClientUtil.getResponseBody(response);
			JSONArray jsonarray = new JSONArray(strResponse);
			reportObject = jsonarray.getJSONObject(0);
			Reporter.log("Actual Audit Report Response:"+reportObject,true);
			
			Assert.assertEquals(reportObject.get("datasource_id"), sourceID);
			Assert.assertNotNull(reportObject.get("earliest_date"),"earliest date is null");		
			Assert.assertNotNull(reportObject.get("latest_date"),"Latest date is null");
			
			break;
		}
		
	    case AuditTestConstants.TIA_FIREWALL_BC_NO_SESSIONS_TBI:{
	    	response  = AuditFunctions.getAuditReport(restClient, new StringEntity(AuditTestUtils.getAuditReportPayloadLatest(sourceID,"86400","1451260800","1453852799")));				
			Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_CREATED);
			
			String strResponse=ClientUtil.getResponseBody(response);
			JSONArray jsonarray = new JSONArray(strResponse);
			reportObject = jsonarray.getJSONObject(0);
			Reporter.log("Actual Audit Report Response:"+reportObject,true);
			
			Assert.assertEquals(reportObject.get("datasource_id"), sourceID);
			Assert.assertNotNull(reportObject.get("earliest_date"),"earliest date is null");		
			Assert.assertNotNull(reportObject.get("latest_date"),"Latest date is null");
	    }
		
		}
	}


	//@Test(priority=5,dependsOnMethods={"testAuditReport"})	
	public void testValidateTIAIncidents() throws Exception{
		Reporter.log("********************************* testValidateTIAIncidents:Test Description ****************************************************** ", true);
		Reporter.log("*****************Validate  TIA Incidents started ***************************",true);
		
		
		
		validateIncidents("",TIA_IOI_Code.TOO_MANY_LOW_REPUTATION_DESTINATIONS.toString());
		Reporter.log("********************************* Completed testValidateTIAIncidents ****************************************************** ", true);
		
		//getListOfIncidents();
	}

	//@Test(priority=6,dependsOnMethods={"testValidateTIAIncidents1"})
	public void deleteDataSourceTestForTiaLogs() throws Exception {
		Reporter.log("************************** Test Description ****************************** ", true);
		Reporter.log("1. Call Datasource delete Api for the created Datasource", true);
		Reporter.log("2. Deleting Data Source for "+ FireWallType +" its ID is: "+sourceID+" started",true);
		Reporter.log("************************************************************************** ", true);
		HttpResponse response = AuditFunctions.deleteDataSource(restClient, sourceID);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_NO_CONTENT);
		Reporter.log("*****************************************Datasource: "+sourceID+" deleted sucessfully",true);
		Reporter.log("**************************Datasource Deletion Test Completed**************************",true);

	}



	public void validateIncidents(String user, String ioi_Code) throws Exception {
		Reporter.log("Sleep 1 min for generated incidents************",true);
		//Thread.sleep(1* 60* 1000);
		String responseBody =  getListOfIncidents();
		boolean isIncidentsListed=false;
		isIncidentsListed=isIncidentListed(responseBody,ioi_Code);
		
		long totalWaitTime=10*60*1000;
		long incidentsWaittime=1* 60* 1000;
		long currentWaitTime=0;
		Reporter.log("Incidents generated -"+isIncidentsListed,true);
		while(!(isIncidentsListed) && currentWaitTime <= totalWaitTime){
			Reporter.log("Incidents generated Wait Time*************** :"+currentWaitTime,true);
			Thread.sleep(incidentsWaittime);
			currentWaitTime += incidentsWaittime;
			isIncidentsListed=isIncidentListed(responseBody,ioi_Code);
			if(isIncidentsListed)
			{
				Reporter.log("Incidents generated -time["+isIncidentsListed+"-"+currentWaitTime+"]",true);
				break;
			}
			
		}
		if(isIncidentsListed){//then do the validation
			JsonNode jnode = unmarshall(responseBody, JsonNode.class);
			if (jnode.isArray()) {
				for (final JsonNode objNode : jnode) {

					String actualSeverity = getJSONValue(objNode.toString(), "s").toString().replace("\"", "");
					String expectedSeverity="low";
					Assert.assertEquals(actualSeverity, expectedSeverity," Expected severity: "+expectedSeverity+" but found:"+actualSeverity);

					String actualService = getJSONValue(objNode.toString(), "f").toString().replace("\"", "");
					String expectedService="Across Services";
					Assert.assertEquals(actualService, expectedService," Expected service: "+expectedService+" but found:"+actualService);

					String usr = getJSONValue(objNode.toString(), "u").toString().replace("\"", "");
					//Assert.assertEquals(usr, "UserTIA30 Hilton");

					String date = getJSONValue(objNode.toString(), "its").toString().replace("\"", "");
					Assert.assertNotNull(date,"timestamp is null");

					String incidentType = getJSONValue(objNode.toString(), "m").toString().replace("\"", "");
					Assert.assertTrue(incidentType.contains(TIA_IOI_Code.TOO_MANY_LOW_REPUTATION_DESTINATIONS.getMessage()));

					String threadScore = getJSONValue(objNode.toString(), "ar").toString().replace("\"", "");
					Assert.assertNotNull(threadScore,"Threadscore is null");

					Reporter.log("severity:"+actualSeverity+" service:"+actualService+" usr:"+usr+" date:"+date+"incidentType "+incidentType+"threadScore:"+threadScore, true);

				}
			}
		}
		else{
			Assert.assertTrue(isIncidentsListed,"Incidents not generated to the preference:"+ioi_Code);
		}
		//return false;
	}

	//test listed incidents


	public String getListOfIncidents() {
		ElasticSearchLogs esLogs = new ElasticSearchLogs();
		String responseBody = null;
		Date dateTo = new Date();
		SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd'T'HH");
		formatDate.setTimeZone(TimeZone.getTimeZone("GMT"));
		String strDateTimeTo = formatDate.format(dateTo);
		Date dateFrom = fromDateRange(dateTo,DateFilterEnum._6MONTHS);
		String strDateTimeFrom = formatDate.format(dateFrom);
		Reporter.log("Capture TIA Incidents in the date range between:  "+(strDateTimeFrom+"-"+strDateTimeTo),true);

		HttpResponse response;
		try {

			String payload = esQueryBuilder.getSearchQueryForTIALogs(strDateTimeFrom + ":00:00.000Z",
					strDateTimeTo + ":59:59.999Z");
			response = esLogs.getCloudServiceAnomalies(restClient, AuditFunctions.buildBasicHeaders(), new StringEntity(payload),suiteData.getHost());
			Assert.assertEquals(response.getStatusLine().getStatusCode(), 200, "Response code is not equal");
			responseBody = ClientUtil.getResponseBody(response);
			Reporter.log(" List of Incidents::::::  Response::::   " + responseBody, true);

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return responseBody;
	}

	public Boolean isIncidentListed(String responseBody, String ioi_Code) throws Exception {
		JsonNode jnode = unmarshall(responseBody, JsonNode.class);
		System.out.println("Respon : " + responseBody);
		if (jnode.isArray()) {
			for (final JsonNode objNode : jnode) {
				String ioi = getJSONValue(objNode.toString(), "ioi").toString().replace("\"", "");
				//String email = getJSONValue(objNode.toString(), "e").toString().replace("\"", "");

				if ( ioi.equals(ioi_Code)) {
					return true;
				}
			}
		}

		return false;
	}


	enum DateFilterEnum
	{
		_1DAY, _1WEEK, _1MONTH, _3MONTHS, _6MONTHS, ALL 
	}

	public static Date fromDateRange(Date date,DateFilterEnum dateFilter)
	{
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		switch (dateFilter) {
		case _1DAY:
			cal.add(Calendar.DATE, -1);
			break;
		case _1WEEK:
			cal.add(Calendar.DATE, -7);
			break;
		case _1MONTH:
			cal.add(Calendar.MONTH, -1);
			break;
		case _3MONTHS:
			cal.add(Calendar.MONTH, -3);
			break;
		case _6MONTHS:
			cal.add(Calendar.MONTH, -6);
			break;
		case ALL:
			cal.add(Calendar.MONTH, -36);
			break;
		default:
		}
		return cal.getTime();
	}

	public  void updateLog(String id, String severity, String Object_type, String user, 
			String Activity_type, String index,String facility,String source,String attr_set) throws Exception {
		Reporter.log("************Started incidents Cleanup*************************",true);

		String value = "{\"id\":\"%s\",\"type\":\"elastica_state\",\"severity\":\"%s\",\"Object_type\":\"%s\",\"user\":\"%s\",\"Activity_type\":\"%s\",\"index\":\"%s\",\"facility\":\"%s\",\"child_drs\":[\"\"],"
				+ "\"__source\":\"%s\",\"attr_set\":\"%s\",\"anomaly_status\":1,\"event_type\":\"AnomalyReport\",\"message\":\"\",\"notes\":\"User changed'Verified Alert?' from  Unknown to Yes(Cleared Alert)\",\"updated_sev\":\"informational\"}";
		String value1 = String.format(value, id, severity, Object_type, user, Activity_type, index,facility,source,attr_set);
		JSONObject object = new JSONObject() ;
		object.put("source", value1);

		StringEntity se = new StringEntity(object.toString());
		Reporter.log("incidents Cleanup Request Body  ::::: " + object.toString(), true);
		ElasticSearchLogs esLogs = new ElasticSearchLogs();
		HttpResponse response =	esLogs.updateLog(restClient, AuditFunctions.buildBasicHeaders(), se, suiteData.getHost());
		String responseBody = ClientUtil.getResponseBody(response);
		Reporter.log("incidents Cleanup Response   ::::: " + responseBody, true);
	}

	public <T> T unmarshall(String data, final Class<T> klass) throws JAXBException {		
		return unmarshallJSON(data, klass);		
	}

	protected static <T> T unmarshallJSON(final String json, final Class<T> klass) {
		final ObjectMapper mapper = new ObjectMapper();
		final AnnotationIntrospector introspector = new JacksonAnnotationIntrospector();
		// make deserializer use JAXB annotations (only)
		mapper.setAnnotationIntrospector(introspector);
		// make serializer use JAXB annotations (only)		
		mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		try {
			return mapper.readValue(json, klass);
		} catch (final IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	public  String getJSONValue(String json, String key){
		JsonFactory factory = new JsonFactory();

		ObjectMapper mapper = new ObjectMapper(factory);
		JsonNode rootNode;
		try {
			rootNode = mapper.readTree(json);
			return  rootNode.get(key).toString();
		} catch (Exception e) {
			Reporter.log("key::::::    "+key, true);
			Reporter.log("error::::  "+e.getMessage(), true);
			//e.printStackTrace();
		}
		return null;


	}


}