/**
 * 
 */
package com.elastica.beatle.tests.audit;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.SortedSet;
import java.util.TimeZone;

import javax.xml.bind.JAXBException;

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

import com.elastica.beatle.SFTPUtils;
import com.elastica.beatle.FileCompressionUtils.CompressionUtils;
import com.elastica.beatle.RestClient.Client;
import com.elastica.beatle.RestClient.ClientUtil;
import com.elastica.beatle.audit.AuditDSStatusDTO;
import com.elastica.beatle.audit.AuditFunctions;
import com.elastica.beatle.audit.AuditGoldenSetTestDataSetup;
import com.elastica.beatle.audit.AuditInitializeTests;
import com.elastica.beatle.audit.AuditTestConstants;
import com.elastica.beatle.audit.AuditTestUtils;
import com.elastica.beatle.audit.SpanVATestsUtils;
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
public class AuditSpanvaAnonyEnabledTIATests extends AuditInitializeTests
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
	
	private String bc_nosessions_TBI_BBI_User=null;
	
	
	// scp/sftp upload params
	protected String rsa_key_file_path;
	protected ArrayList<String> datasourceIdsList=new ArrayList<String>();

	protected String fileToBeUploaded;
	protected String sftpTenantUsername;
	protected String sftpServerHost;
	protected String sftpServerDestinationDir;

	
	String spanVAPayload;
	String firewallLogFilePath;
	String agentId;
	protected String scpcompltedCheckEmptyFilePath;
	
	AuditGoldenSetTestDataSetup goldenSetTestDataSetup=null;
	List<String> auditReportValidationsErrors = new ArrayList<String>();
	ArrayList<String> auditSummaryValidationsErrors = new ArrayList<String>();
	ArrayList<String> auditSummaryForConsumerServicesValidationsErrors = new ArrayList<String>();
	SpanVATestsUtils spnavaUtils=null;
	private Map<String,String> spanVAAliveAgentDetailsMap;



	/**
	 * @param FireWallName
	 */
	public AuditSpanvaAnonyEnabledTIATests(String FireWallName) {
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
	
@BeforeClass(alwaysRun = true)
	public void intScpSftpData() throws Exception{

		spnavaUtils = new SpanVATestsUtils();
		spanVAAliveAgentDetailsMap = spnavaUtils.getSpanvaAliveAgentOnThisInstance(suiteData, restClient);
		agentId=spanVAAliveAgentDetailsMap.get("agent_id");
		
		Reporter.log("*******************populate Spanva Data for the test****************************************",true);
		Reporter.log("Spanva Host:- "+suiteData.getSpanvaIp(),true);
		Reporter.log("Spanva Host credentials: username:- "+suiteData.getSpanvausername()+" password:- "+suiteData.getSpanvapwd()+" spnavaAgentName:- "+suiteData.getSpanvaAgentName(),true);
		Reporter.log("Spanva AgentId:- "+agentId,true);
		Reporter.log("Spanva version:- "+spanVAAliveAgentDetailsMap.get("current_version"),true);
		
		spanVAPayload = AuditTestUtils.createSpanVAUploadBody(FireWallType,agentId,suiteData.getEnvironmentName(),AuditTestConstants.AUDIT_SPANVA_DS_NAME);
		firewallLogFilePath = AuditTestUtils.getFirewallLogFilePath(FireWallType);
		scpcompltedCheckEmptyFilePath=AuditTestUtils.getFirewallLogFilePath(AuditTestConstants.SCP_COMPLETED);
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
		}
	
	}
	
	@Test(priority=2, dependsOnMethods="setTIAPreferences")
	public void testSpanVADatasourceCreation() throws Exception
	{
		Reporter.log("********************************* Test Description ****************************************************** ", true);
		Reporter.log("1. Create Datasource through SpanVA Transportation", true);
		Reporter.log("2. Process the Datasource ", true);
		Reporter.log("3. Poll the Datasource status by calling the Datasource Api for every 2 minutes until it gets Completed ", true);
		Reporter.log("********************************************************************************************************* ", true);

		Reporter.log("*************Datasource Creation started for:"+FireWallType+"****************",true);


		//create spanva datasource
		Reporter.log("Request Payload: "+spanVAPayload,true);
		HttpResponse createResp = AuditFunctions.createSpanVADataSource(restClient,new StringEntity(spanVAPayload));
		Assert.assertEquals(createResp.getStatusLine().getStatusCode(), HttpStatus.SC_CREATED);
		JSONObject spanVAConnectionObject = new JSONObject(ClientUtil.getResponseBody(createResp));

		Reporter.log("Actual Datasource Response:"+spanVAConnectionObject,true);

		String dsName=(String)spanVAConnectionObject.get("name");
		Reporter.log("dsName::"+dsName,true);
		String expected_str_setup_by=suiteData.getUsername();
		String expectedResponse=    " [log_transport=" + AuditTestConstants.AUDIT_SPANVA_LOG_TRANSPORT+
				", DatasourceId=" + "DatasourceId is not null" +
				", resource_uri=" + "resource_uri is not null" +
				", SetupBy=" + expected_str_setup_by +
				", datasource_format=" + "datasource_format is not empty" +
				", agent_id=" + "agent_id is not empty" +
				", agent_name=" + "agent_name is not empty" +
				", log_collection_type=" + "log_collection_type is not empty" +
				", user=" + "user is not empty" +
				", dst_dir=" + "dst_dir is not empty" +
				", datasource_type=" + "datasource_type is not empty"+" ]";
		Reporter.log("Expected Datasource Response fields:"+expectedResponse,true);
		validateSpanVADataSource(spanVAConnectionObject);
		sourceID = (String) spanVAConnectionObject.get("id");
		JSONObject agentInfoObject=(JSONObject) spanVAConnectionObject.get("agent_info");

		Reporter.log("agentInfoObject:"+agentInfoObject,true);
		String username=(String)agentInfoObject.get("user");
		String host=(String)agentInfoObject.get("host");
		String dest_dir=(String)agentInfoObject.get("dst_dir");


		//getting data source from getDatasources list
		Reporter.log("getting the datasource from the datasources list:",true);
		List<NameValuePair> queryParam = new ArrayList<NameValuePair>();		
		queryParam.add(new BasicNameValuePair("fields", "datasources"));
		HttpResponse datataSourceListResp = AuditFunctions.getDataSourceList(restClient,queryParam);
		Assert.assertEquals(datataSourceListResp.getStatusLine().getStatusCode(), HttpStatus.SC_OK);

		String strDatataSourceListResp=ClientUtil.getResponseBody(datataSourceListResp);
		Reporter.log("List of Datasources:"+strDatataSourceListResp,true);

		JSONObject listObj=new JSONObject(strDatataSourceListResp);
		JSONObject tenantObj=listObj.getJSONObject("objects");
		JSONArray datasourcesList = tenantObj.getJSONArray("datasources");
		String datasourcename=null;
		for(int i=0; i<datasourcesList.length(); i++)
		{
			datasourcename=((JSONObject)datasourcesList.get(i)).getString("name");
			if(dsName.equals(datasourcename))
			{
				sourceID=((JSONObject)datasourcesList.get(i)).getString("id");
				break;
			}
		}


		//upload file to the span VA datasource
		Reporter.log("******************Upload file using Scp:****************************************",true);
		SFTPUtils sftpUtils=getSftpUtilsConfiguration(username, host,dest_dir);
		Reporter.log("******************scp upload detais:********************",true);
		Reporter.log("scpUsername:"+username,true);
		Reporter.log("scpPassword:"+sftpUtils.getPassWord(),true);
		Reporter.log("scpServerHost:"+host,true);
		Reporter.log("scpServerDestinationDir:"+dest_dir,true);
		Reporter.log("scpUploadedfirewallLogFilePath:"+firewallLogFilePath,true);
		FileInputStream fin;
		File file = new File(System.getProperty("user.dir")+firewallLogFilePath);
		fin = new FileInputStream(file);
		String result = "";
		Reporter.log("******************scp upload started:********************",true);
		result = sftpUtils.uploadFileToFTP(file.getName(), fin, true);
		Reporter.log("scp file upload status:  " + result,true);
		Reporter.log("******************scp upload completed sucessfully:********************",true);

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



		Reporter.log("******************Get Datasource status verification****************************************",true);
		//verification logfile process attached to the datasource
		HttpResponse pollForStatusResponse = AuditFunctions.pollForDataSourceStatus(restClient, sourceID);
		Assert.assertEquals(pollForStatusResponse.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
		JSONObject pollRespObject = new JSONObject(ClientUtil.getResponseBody(pollForStatusResponse));
		Reporter.log("Actual Datasource Response:"+pollRespObject,true);	
		String last_Status = pollRespObject.getString("last_status");
		Reporter.log("Actual Datasource Status:"+last_Status,true);
		String expectedCompletedStatus="Completed";
		String last_detect_msg ="";
		Reporter.log("Expected Datasource Status:"+expectedCompletedStatus,true);
		long currentWaitTime = 0;
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
		Assert.assertTrue(currentWaitTime <= AuditTestConstants.AUDIT_PROCESSING_MAX_WAITTIME," File processing took more than SLA. Last status of this source file was "+last_Status);
		Assert.assertEquals(pollRespObject.get("last_status"), "Completed","create data soure procesing is not completed. Last status of this source file was "+last_Status);				
		Assert.assertNotNull(pollRespObject.get("resource_uri"), "Resource URI is null. Last status of this source file was "+last_Status);
		Assert.assertFalse(((String) pollRespObject.get("resource_uri")).isEmpty(), "resource URI is empty. Last status of this source file was "+last_Status);
		Assert.assertNotNull(pollRespObject.get("log_format"), "Resource URI is null. Last status of this source file was "+last_Status);
		Assert.assertFalse(((String) pollRespObject.get("log_format")).isEmpty(), "resource URI is empty. Last status of this source file was "+last_Status);
		Assert.assertNotNull(pollRespObject.get("log_transport"), "Resource URI is null. Last status of this source file was "+last_Status);
		Assert.assertFalse(((String) pollRespObject.get("log_transport")).isEmpty(), "resource URI is empty. Last status of this source file was "+last_Status);
		Assert.assertEquals(pollRespObject.getString("log_transport"), AuditTestConstants.AUDIT_SPANVA_LOG_TRANSPORT,"Log Transport method doesn't match. Last status of this source file was "+last_Status);
		Assert.assertNotNull(pollRespObject.get("id"), "Data Source Id is null. Last status of this source file was "+last_Status);
		Assert.assertFalse(((String) pollRespObject.get("id")).isEmpty(),"ID is empty. Last status of this source file was "+last_Status);
		Assert.assertEquals(pollRespObject.get("last_status_message"), "Logs processed successfully","Logs processing was not successful. Last status of this source file was "+last_Status);

		Reporter.log("**************************Scp Datasource creation Test Completed**************************",true);

	}
	
	@Test(dependsOnMethods={"testSpanVADatasourceCreation"})
	public void getUserCheckOfDataSource() throws Exception
	{
		//sourceID="574ec3dfea078121ee3f3d3c";
		Reporter.log("sourceID:"+sourceID,true);
		SortedSet<String> actulAnonyUsersSet=null;
		//goldenSetTestDataSetup=suiteData.getAuditGoldenSetTestDataSetup();
		
		switch(FireWallType)
		{
		case AuditTestConstants.TIA_FIREWALL_BC_NO_SESSIONS_TBI:
		{
			
			actulAnonyUsersSet=AuditTestUtils.getDatasourceAnonyUsers(sourceID,"86400","1451779200","1451865599");
			Assert.assertNotNull(actulAnonyUsersSet,"Audit users not empty:");
			//Assert.assertTrue(actulAnonyUsersSet.contains(bc_nosessions_TBI_BBI_User),"Users should be: ");
		break;
		}
		}
	}
	
	

	
	@Test(priority=5,dependsOnMethods={"testSpanVADatasourceCreation"})	
	public void testValidateTIAIncidents1() throws Exception{
		Reporter.log("********************************* testValidateTIAIncidents:Test Description ****************************************************** ", true);
		Reporter.log("*****************Validate  TIA Incidents started ***************************",true);
		
		
		switch(FireWallType)
		{
		
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
		
			
		}
		
	}
		
	

		public void validateIncidents(String firewallType, String user,ArrayList<TIAIncidentDto> expecteListOfIncidents) throws Exception {
			Reporter.log("Sleep 1 min for generated incidents************",true);
			//Thread.sleep(1* 60* 1000);
			String responseBody =  getListOfIncidents();
			Reporter.log("getListOfIncidents::"+responseBody,true);
			boolean isIncidentsListed=false;
			isIncidentsListed=isIncidentListedNew(responseBody,user);
			
			long totalWaitTime=10*60*1000;
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

	
	
	
	

//	@Test(priority=6,dependsOnMethods={"testValidateTIAIncidents1"})
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

	
	private void validateSpanVADataSource(JSONObject spanVAConnectionObject)
			throws Exception {
		Assert.assertEquals(spanVAConnectionObject.get("log_transport"), AuditTestConstants.AUDIT_SPANVA_LOG_TRANSPORT);
		Assert.assertNotNull(spanVAConnectionObject.get("id"), "Data Source Id is null");
		Assert.assertNotNull(spanVAConnectionObject.get("resource_uri"), "Resource URI is null");
		Assert.assertFalse(((String) spanVAConnectionObject.get("resource_uri")).isEmpty(), "resource URI is empty");
		Assert.assertFalse(((String) spanVAConnectionObject.get("datasource_format")).isEmpty(),"Data source format is empty");
		Assert.assertFalse(((String) spanVAConnectionObject.get("datasource_type")).isEmpty(),"Data source type is empty");
		Assert.assertNotNull(spanVAConnectionObject.get("agent_id"), "Agent Id is null");
		Assert.assertNotNull(spanVAConnectionObject.get("agent_name"), "Agent Id is null");
		JSONObject agentInfoObject=(JSONObject) spanVAConnectionObject.get("agent_info");
		Assert.assertNotNull(agentInfoObject.get("host"), "Agent host is null");
		Assert.assertNotNull(agentInfoObject.get("log_collection_type"), "log_collection_type is null");
		Assert.assertNotNull(agentInfoObject.get("user"), "user is null");
		Assert.assertNotNull(agentInfoObject.get("dst_dir"), "dst_diris null");


	}
	
	
	private SFTPUtils getSftpUtilsConfiguration(String sftpTenantUsername,String sftpServerHost, String sftpServerDestinationDir ) throws Exception {
		SFTPUtils sftpUtils=new SFTPUtils();
		sftpUtils.setHostName(sftpServerHost);
		sftpUtils.setHostPort(AuditTestConstants.AUDIT_SCP_PORT);
		sftpUtils.setUserName(sftpTenantUsername);
		if(suiteData.getApiserverHostName().contains("qa-vpc")){
			sftpUtils.setPassWord(AuditTestConstants.AUDIT_SPANVA_VPC_TENANT_PWD);}
		else if(suiteData.getApiserverHostName().contains("api-vip.elastica.net") && suiteData.getTenantName() .contains("protecto365autobeatlecom")  ){
			sftpUtils.setPassWord(AuditTestConstants.AUDIT_TENANT_PROD_SCP_PWD);
		}
		else if(suiteData.getApiserverHostName().contains("api-vip.elastica.net") && suiteData.getTenantName() .contains("elasticaqanet") ){
			sftpUtils.setPassWord(AuditTestConstants.AUDIT_TENANT_PROD_SCP_PWD1);
		}
		else if(suiteData.getApiserverHostName().contains("api.eu.elastica.net")){
			sftpUtils.setPassWord(AuditTestConstants.AUDIT_TENANT_CEP_SCP_PWD);
		}
		else if(suiteData.getApiserverHostName().contains("api-eoe.elastica-inc.com") && suiteData.getTenantName() .contains("anonyenabled")){
			sftpUtils.setPassWord(AuditTestConstants.AUDIT_TENANT_EOE_ANONY_SCP_PWD);
		}
		else if(suiteData.getApiserverHostName().contains("api-eoe.elastica-inc.com") && suiteData.getTenantName() .equals("spanvatestanyenacom")){
			sftpUtils.setPassWord(AuditTestUtils.getSpanVATenantScpPwd(suiteData.getTenantName()));
		}
		
		else if(suiteData.getApiserverHostName().contains("api-eoe.elastica-inc.com") && suiteData.getTenantName() .equals("eoeauditspanvatiaco")){
			sftpUtils.setPassWord("fFcX5SJf");
		}
		
		else{
			sftpUtils.setPassWord(AuditTestConstants.AUDIT_EOE_SPANVA_ING_SCP_PWD);;}
		sftpUtils.setDestinationDir(sftpServerDestinationDir);
		return sftpUtils;
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