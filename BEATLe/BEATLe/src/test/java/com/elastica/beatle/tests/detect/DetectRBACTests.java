package com.elastica.beatle.tests.detect;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.SkipException;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.elastica.beatle.Authorization.AuthorizationHandler;
import com.elastica.beatle.RestClient.ClientUtil;
import com.elastica.beatle.detect.DetectSequenceDetector;
import com.elastica.beatle.detect.dto.AttributeBean;
import com.elastica.beatle.detect.dto.DetectAttributeDto;
import com.elastica.beatle.detect.dto.DetectSequenceDto;
import com.elastica.beatle.detect.dto.SDInput;
import com.elastica.beatle.es.ElasticSearchLogs;

public class DetectRBACTests extends DetectUtils {
	
	private static final String OBJECTS = "detect_attributes";

	private static String viewPassword = null;
	private static String viewuser = null;
	
	private static String updatePassword = null;
	private static String updateuser = null;
	
	private static String allPassword = null;
	private static String alluser = null;
	
	

	DetectSequenceDetector dsd = new DetectSequenceDetector();;
	private ElasticSearchLogs esLogs = new ElasticSearchLogs();;
	
	public void changeSuiteUser(String userName, String userPassword) throws Exception {
		suiteData.setUsername(userName);
		suiteData.setPassword(userPassword);
		
		HttpResponse CSRFHeader = AuthorizationHandler.getCSRFHeaders(userName, userPassword, suiteData);
		suiteData.setCSRFToken(AuthorizationHandler.getCSRFToken(CSRFHeader));
		suiteData.setSessionID(AuthorizationHandler.getUserSessionID(CSRFHeader));
	}
	
	@BeforeTest
	void getUserdeatils(){
		
		 viewuser = suiteData.getSaasAppEndUser2Name();
		 viewPassword = suiteData.getSaasAppEndUser2Password();
		
		 updateuser = suiteData.getSaasAppEndUser1Name();
		 updatePassword = suiteData.getSaasAppEndUser1Password();
		 
		 alluser = suiteData.getUsername();
		 allPassword = suiteData.getPassword();
		
		
		
	}
	
	@DataProvider()
    public static  Object[][] dataProvider(Method method)  {
		
	
		
		Object[][] feedbacks = {
				{viewuser, viewPassword, "view"},
				{updateuser, updatePassword, "update"},
				{alluser, allPassword, "all"}
				};
			
		return feedbacks;
	}
	
	@Test(dataProvider="dataProvider")
	public void detect_RBAC_get_Incidents_List(Method method, String adminUserName, String adminUserPassword, String permission) throws Exception {
		Reporter.log("Execution Started - Test Case Name: " + method.getName(), true);
		
		String expectedSeverity = "all";
		if (permission.equalsIgnoreCase("view")) {
			expectedSeverity = "low";
		}
		if (permission.equalsIgnoreCase("update")) {
			expectedSeverity = "high";
		}
		
		changeSuiteUser(adminUserName, adminUserPassword);
		
		String responseBody = getListOfIncidents();
		Reporter.log("Get SD List response:" + responseBody, true);
		
		//Verify the Sevirity of Incidents.
		JsonNode jnode = unmarshall(responseBody, JsonNode.class);
		if (!expectedSeverity.equalsIgnoreCase("all") && jnode.isArray()) {
		    for (final JsonNode objNode : jnode) {
		    	String attr_set = getJSONValue(objNode.toString(), "ats").toString().replace("\"", "");
		    	String severity = getJSONValue(objNode.toString(), "s").toString().replace("\"", "");
		    	String ioi_code = getJSONValue(objNode.toString(), "ioi").toString().replace("\"", "");
		    	Reporter.log("attr : " + attr_set + " severity : " + severity + " ioi : " + ioi_code, true);
		    	Assert.assertTrue(severity.equalsIgnoreCase(expectedSeverity), "Incidents with unexpected Severity Returned in Response!!");
		    }
		}
		
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
	}
	
	@Test(dataProvider="dataProvider")
	public void detect_RBAC_get_IncidentDetails(Method method, String adminUserName, String adminUserPassword, String permission) throws Exception {
		Reporter.log("Execution Started - Test Case Name: " + method.getName(), true);
		
		changeSuiteUser(adminUserName, adminUserPassword);
		
		String responseBody = getListOfIncidents();
		Reporter.log("Get SD List response:" + responseBody, true);
		
		if(2==responseBody.length()){
			throw new SkipException("there are no incidents so skipping the tests");
		}
		JsonNode jnode = unmarshall(responseBody, JsonNode.class);
		JsonNode objNode = jnode.get(1);
		String id = getJSONValue(objNode.toString(), "id").toString().replace("\"", "");
		String ioi_code = getJSONValue(objNode.toString(), "ioi").toString().replace("\"", "");
		Reporter.log("id : " + id + " ioi : " + ioi_code, true);
		
		List<NameValuePair> queryParams = new ArrayList<NameValuePair>();
		queryParams.add(new BasicNameValuePair("source", "{\"id\":\""+id+"\",\"type\":\"elastica_state\",\"index\":\"alias_logs_detectsdbeatlecom-2015\",\"event_type\":\"AnomalyReport\"}"));
		queryParams.add(new BasicNameValuePair("sourceName", "DETECT"));
		
		HttpResponse response = esLogs.getNotes(restClient, buildCookieHeaders(), queryParams, suiteData);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_OK, "The GET Notes API failed to respond with Incident Details!!");

		responseBody = getResponseBody(response);
		Reporter.log("ResponseBody Of GetNotes ::::: "+responseBody, true);
		
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
	}
	
	@Test(dataProvider="dataProvider")
	public void detect_RBAC_get_Feedback_to_Incident(Method method, String adminUserName, String adminUserPassword, String permission) throws Exception {
		Reporter.log("Execution Started - Test Case Name: " + method.getName(), true);
		
		changeSuiteUser(adminUserName, adminUserPassword);
		
		String responseBody = getListOfIncidents();
		Reporter.log("Get SD List response:" + responseBody, true);
		
		if(2==responseBody.length()){
			throw new SkipException("there are no incidents so skipping the tests");
		}
		
		JsonNode jnode = unmarshall(responseBody, JsonNode.class);
		JsonNode objNode = jnode.get(0);
		
		String report_id = getJSONValue(objNode.toString(), "id").toString().replace("\"", "");
    	String facilty = getJSONValue(objNode.toString(), "f").toString().replace("\"", "");
    	String ioicode = getJSONValue(objNode.toString(), "ioi").toString().replace("\"", "");
    	String email = getJSONValue(objNode.toString(), "e").toString().replace("\"", "");
    	String index = getJSONValue(objNode.toString(), "index").toString().replace("\"", "");
    	String source = getJSONValue(objNode.toString(), "src").toString().replace("\"", "");
    	String attr_set = null;
    	if(null!=getJSONValue(objNode.toString(), "ats")){
    	 attr_set = getJSONValue(objNode.toString(), "ats").toString().replace("\"", "");
    	}
    	String Activity_type = getJSONValue(objNode.toString(), "at").toString().replace("\"", "");
    	String severity = getJSONValue(objNode.toString(), "s").toString().replace("\"", "");
    	String ObjectType = getJSONValue(objNode.toString(), "ot").toString().replace("\"", "");
    	
    	System.out.println(report_id + ":" + severity + ":" + ObjectType + ":" + email + ":" + Activity_type + ":" + index + ":" + facilty + ":" + source + ":" + attr_set);
    	HttpResponse response = feedbackToIncident(report_id, severity, ObjectType, email, Activity_type, index, facilty, source, attr_set, "No", false, false, ioicode);
    	
		responseBody = getResponseBody(response);
		Reporter.log("ResponseBody Of updateLog ::::: "+responseBody, true);
		
		if (permission.equalsIgnoreCase("view")) {
			Assert.assertTrue(responseBody.isEmpty(), "Feedback to Incident is updated without proper permission!!");
		}
		if (permission.equalsIgnoreCase("update") || permission.equalsIgnoreCase("all")) {
			Boolean ok = new JSONObject(responseBody).getBoolean("ok");
			Assert.assertTrue(ok, "The POST of Feedback to Incident is NOT Sucessful as Expected!!");
		}
		
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
	}
	
	@Test(dataProvider="dataProvider")
	public void detect_RBAC_get_AddNote_to_Incident(Method method, String adminUserName, String adminUserPassword, String permission) throws Exception {
		Reporter.log("Execution Started - Test Case Name: " + method.getName(), true);
		
		changeSuiteUser(alluser, allPassword);
		
		String responseBody = getListOfIncidents();
		Reporter.log("Get SD List response:" + responseBody, true);
		
		if(2==responseBody.length()){
			throw new SkipException("there are no incidents so skipping the tests");
		}
		
		JsonNode jnode = unmarshall(responseBody, JsonNode.class);
		JsonNode objNode = jnode.get(0);
		
		String report_id = getJSONValue(objNode.toString(), "id").toString().replace("\"", "");
    	String index = getJSONValue(objNode.toString(), "index").toString().replace("\"", "");
    	System.out.println(report_id + ":" + index);
    	
    	changeSuiteUser(adminUserName, adminUserPassword);
		
    	HttpResponse response = addNoteToIncident(report_id, index, "test note");
		responseBody = getResponseBody(response);
		Reporter.log("ResponseBody Of updateLog ::::: "+responseBody, true);
		
		if (permission.equalsIgnoreCase("view")) {
			Assert.assertTrue(responseBody.isEmpty(), "Add Note to Incident is updated without proper permission!!");
		}
		if (permission.equalsIgnoreCase("update") || permission.equalsIgnoreCase("all")) {
			Boolean ok = new JSONObject(responseBody).getBoolean("ok");
			Assert.assertTrue(ok, "The POST of AddNotes to Incidents is NOT Sucessful as Expected!!");
		}
		
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
	}

	@Test(dataProvider="dataProvider")
	public void detect_RBAC_get_SD_List(Method method, String adminUserName, String adminUserPassword, String permission) throws Exception {
		Reporter.log("Execution Started - Test Case Name: " + method.getName(), true);
		
		changeSuiteUser(adminUserName, adminUserPassword);
		
		DetectSequenceDto dsdto = new DetectSequenceDto();
		dsdto.setLimit(20);
		dsdto.setOffset(0);
		dsdto.setRequestType("list");
		Reporter.log("Sequence detector :::::: "+utils.marshall(dsdto));
		StringEntity se =new StringEntity(utils.marshall(dsdto));
		HttpResponse response = esLogs.detectsequences(restClient, buildCookieHeaders(), se, suiteData);
		
		String responseBody = ClientUtil.getResponseBody(response);
		Reporter.log("Get SD List response:" + responseBody, true);
		
		Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_OK, "The GET of SequenceDetector is NOT success!!");
		
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
	}

	@Test(dataProvider="dataProvider")
	public void detect_RBAC_post_SDs(Method method, String adminUserName, String adminUserPassword, String permission) throws Exception {
		Reporter.log("Execution Started - Test Case Name: " + method.getName(), true);
		
		changeSuiteUser(adminUserName, adminUserPassword);
		
		String name = UUID.randomUUID().toString();
		String[] activities = new String[]{"Create", "Share"};
		String[] failities = new String[]{"__any", "__any"} ;
		String[] sources = new String[]{"__any", "__any"} ;
		String[] users = new String[]{"__any", "__any"} ;
		String[] objects = new String[]{"__any", "__any"} ;
		
		DetectSequenceDetector dsd = new DetectSequenceDetector();
		SDInput sdInput = dsd.createSDInput(name, 3,36, false   ,false, true,
				activities, failities, sources, users, objects);
		HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
		
		String responseBody = getResponseBody(response);
		Reporter.log("Response from Create Seequences : " + responseBody, true);
		
		JsonNode detect_sequences = new ObjectMapper().readTree(getJSONValue(responseBody.toString(), "detect_sequences"));
		
		Reporter.log("Seequences : " + detect_sequences.toString(), true);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), 200, "Response code is not equal");
		
		if (permission.equalsIgnoreCase("view")) {
			String apiResponse = new JSONObject(responseBody).getString("api_message");
			Assert.assertEquals(apiResponse, "Forbidden request", "The POST of Attributes Preferences is NOT Failed as Expected!!");
		}
		if (permission.equalsIgnoreCase("update") || permission.equalsIgnoreCase("all")) {
			String actionStatus = new JSONObject(detect_sequences.toString()).getString("action_status");
			Assert.assertEquals(actionStatus, "success", "The POST of Attributes Preferences is NOT Failed as Expected!!");
		}
		
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
	}
	
	@Test(dataProvider="dataProvider")
	public void detect_RBAC_get_AttributesPreferences(Method method, String adminUserName, String adminUserPassword, String permission) throws Exception {
		Reporter.log("Execution Started - Test Case Name: " + method.getName(), true);
		
		changeSuiteUser(adminUserName, adminUserPassword);
		
		HttpResponse response = getDetectAttributes();
		String responseBody = ClientUtil.getResponseBody(response);
		Reporter.log("Get AttributesPreferences response:" + responseBody, true);
		
		Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_OK, "The GET of AnomalyValue is NOT success!!");
		
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
	}
	
	@Test(dataProvider="dataProvider")
	public void detect_RBAC_post_AttributesPreferences(Method method, String adminUserName, String adminUserPassword, String permission) throws Exception {
		Reporter.log("Execution Started - Test Case Name: " + method.getName(), true);
		
		changeSuiteUser(adminUserName, adminUserPassword);
		
		AttributeBean attributeBean = new AttributeBean(30, 3, true);

		HttpResponse resp = getDetectAttributes();
		String responseBody = getResponseBody(resp);
		org.json.JSONArray getResponseArray = new JSONObject(responseBody).getJSONArray(OBJECTS);
		String ioi_Code = "ANOMALOUSLY_FREQUENT_DELETES";
		DetectAttributeDto detectAttributeDto = new DetectAttributeDto();
		responseBody = updateBBIAttributes(attributeBean.isEnabled(), attributeBean, getResponseArray, detectAttributeDto, ioi_Code);
		Reporter.log("Post AttributesPreferences response:" + responseBody, true);
		
		String apiResponse = new JSONObject(responseBody).getString("api_message");
		if (permission.equalsIgnoreCase("view")) {
			Assert.assertEquals(apiResponse, "Forbidden request", "The POST of Attributes Preferences is NOT Failed as Expected!!");
		}
		if (permission.equalsIgnoreCase("update") || permission.equalsIgnoreCase("all")) {
			Assert.assertEquals(apiResponse, "", "The POST of Attributes Preferences is NOT Failed as Expected!!");
		}
		
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
	}
	
	@Test(dataProvider="dataProvider")
	public void detect_RBAC_get_anomalyvalues(Method method, String adminUserName, String adminUserPassword, String permission) throws Exception {
		Reporter.log("Execution Started - Test Case Name: " + method.getName(), true);
		
		changeSuiteUser(adminUserName, adminUserPassword);
		
		HttpResponse response = esLogs.getanomalyvalues(restClient, buildCookieHeaders(), suiteData);
		String responseBody = ClientUtil.getResponseBody(response);
		Reporter.log("Get Anomaly Values response:" + responseBody, true);
		
		Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_OK, "The GET of AnomalyValue is NOT success!!");
		
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
	}
	
	@Test(dataProvider="dataProvider")
	public void detect_RBAC_post_anomalyvalues(Method method, String adminUserName, String adminUserPassword, String permission) throws Exception {
		Reporter.log("Execution Started - Test Case Name: " + method.getName(), true);
		
		changeSuiteUser(adminUserName, adminUserPassword);
		
		JSONObject obj = new JSONObject();
		obj.put("errorThreshold", new Integer(50));
		obj.put("criticalThreshold",new Integer(80));
		
		StringEntity se = new StringEntity(obj.toString());
		
		HttpResponse response = esLogs.postanomalyvalues(restClient, buildCookieHeaders(), se, suiteData);
		String responseBody = ClientUtil.getResponseBody(response);
		Reporter.log("POST Anomaly Values response:" + responseBody, true);
		
		if (permission.equalsIgnoreCase("view")) {
			Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_FORBIDDEN, "The POST of AnomalyValue is NOT failed with Expected Error!!");
		}
		if (permission.equalsIgnoreCase("update") || permission.equalsIgnoreCase("all")) {
			Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_OK, "The POST of AnomalyValue is Failed!!");
		}
		
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
	}
	
}
