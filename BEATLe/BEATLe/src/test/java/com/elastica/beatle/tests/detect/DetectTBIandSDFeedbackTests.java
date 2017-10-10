package com.elastica.beatle.tests.detect;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;
import java.util.TimeZone;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpResponse;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.elastica.beatle.FixRetryListener;
import com.elastica.beatle.detect.DetectCommonutils;
import com.elastica.beatle.detect.DetectInitializeTests;
import com.elastica.beatle.detect.DetectSequenceDetector;
import com.elastica.beatle.detect.SequenceDetectorConstants;
import com.elastica.beatle.detect.dto.AttributeBean;
import com.elastica.beatle.detect.dto.DetectAttributeDto;
import com.elastica.beatle.detect.dto.IOI_Code;
import com.elastica.beatle.detect.dto.InputBean;
import com.elastica.beatle.detect.dto.SDInput;
import com.elastica.beatle.es.ActivityLogs;
import com.elastica.beatle.es.ElasticSearchLogs;

@Listeners(value = FixRetryListener.class)
public class DetectTBIandSDFeedbackTests extends DetectUtils {

	private static final String OBJECTS = "detect_attributes";
	String user = null;
	String responseBody ;
	DetectCommonutils utils = new DetectCommonutils();
	DetectSequenceDetector dsd = new DetectSequenceDetector();
	
	@DataProvider()
    public static  Object[][] dataProvider(Method method)  {
		
		Object[][] feedbacks = {
				{"Yes", false},
				{"No", false},
				{"Yes", true},
				{"No", true}
				};
			
		return feedbacks;
	}
	
	@Test(dataProvider = "dataProvider", description = "")
	public void suspicious_Logins_Tests(Method method, String unusual, Boolean clearIncident) throws Exception {
		
		AttributeBean attributeBean = new AttributeBean(2, 3, 2, true);
		
		InputBean inputBean = createFileUpdateData1("test_login_001", attributeBean);
		
		Log(method, attributeBean, inputBean);
		
		String responseBody;
		HttpResponse resp = getDetectAttributes();
		responseBody = getResponseBody(resp);
		org.json.JSONArray getResponseArray = new JSONObject(responseBody).getJSONArray(OBJECTS);
		String ioi_Code = IOI_Code.TOO_MANY_SUSPICIOUS_LOGINS.toString();
		
		boolean enabled = attributeBean.isEnabled();
		updateDetectAttributesForTBI(enabled, attributeBean, getResponseArray, ioi_Code);

		scpActivityLogsAndValidate(method, inputBean);
		
		user = inputBean.getUser();
		if(enabled){

			boolean abortTest = false;
			resp = getDetectAttributes();
			responseBody = getResponseBody(resp);
			getResponseArray = new JSONObject(responseBody).getJSONArray(OBJECTS);
			for (int index = 0; index < getResponseArray.length(); index++) {
				JSONObject attributeObject = getResponseArray.getJSONObject(index);
				if (ioi_Code.toString().equals((String) attributeObject.get(NAME))) {
					if(attributeObject.get("enabled").toString().equals("false")){
						Reporter.log("");
						Reporter.log("preference got changed in between test excetuion so terminating validation::::::", true);
						Reporter.log("");
						abortTest=true;
						throw new SkipException("preference got changed in between test excetuion so skipping test");
					}
				}
			}
		if (!abortTest) {
			HashSet<String> expIncidents = new HashSet<String>();
			expIncidents.add(ioi_Code);

			responseBody = verifyStateIncidents(inputBean.getUser(), expIncidents);
			Set<String> ioi_codes = new HashSet<>();
			ioi_codes.add(ioi_Code);
			 validateTBIIncidents(responseBody, attributeBean, inputBean,ioi_codes );
			verifyThreatscoreIncidets(user, expIncidents);
		}
	
		}else{
			verifyNoIncidents(inputBean.getUser());
		}
		
		getAndFeedbackToIncident(inputBean.getUser(), ioi_Code, unusual, clearIncident);
		
		if (!clearIncident) {
			Set<String> ioi_codes = new HashSet<>();
			ioi_codes.add(ioi_Code);
			validateTBIIncidents(responseBody, attributeBean, inputBean,ioi_codes );
		} else {
			Assert.assertFalse(isIncidentListed(inputBean.getUser(), ioi_Code), "The Expected Incident is NOT cleared");
		}
		
		Reporter.log("");
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);	
		Reporter.log("");
	}

	@Test(dataProvider = "dataProvider", description = "")
	public void invalid_Login_Test(Method method, String unusual, Boolean clearIncident) throws Exception {
		
		AttributeBean  attributeBean = new AttributeBean(2, 3, 2, true);
		
		InputBean 	inputBean = createFileUpdateData1("test_login_003", attributeBean);
		
		Log(method, attributeBean, inputBean);
		
		String ioi_Code = "TOO_MANY_INVALID_LOGINS";
		boolean enabled = attributeBean.isEnabled();
		HttpResponse resp = getDetectAttributes();
		String responseBody = getResponseBody(resp);
		org.json.JSONArray getResponseArray = new JSONObject(responseBody).getJSONArray(OBJECTS);
		updateDetectAttributesForTBI(enabled, attributeBean, getResponseArray, ioi_Code);
		
		
		
		
		 user = inputBean.getUser();

		scpActivityLogsAndValidate(method, inputBean);
		// get state
		//Thread.sleep(30000);
		if(enabled){

			boolean abortTest = false;
			resp = getDetectAttributes();
			responseBody = getResponseBody(resp);
			getResponseArray = new JSONObject(responseBody).getJSONArray(OBJECTS);
			for (int index = 0; index < getResponseArray.length(); index++) {
				JSONObject attributeObject = getResponseArray.getJSONObject(index);
				if (ioi_Code.toString().equals((String) attributeObject.get(NAME))) {
					if(attributeObject.get("enabled").toString().equals("false")){
						Reporter.log("");
						Reporter.log("preference got changed in between test excetuion so terminating validation::::::", true);
						Reporter.log("");
						abortTest=true;
						throw new SkipException("preference got changed in between test excetuion so skipping test");
					}
				}
			}
		if (!abortTest) {

			HashSet<String> expIncidents = new HashSet<String>();
			expIncidents.add(ioi_Code);

			responseBody = verifyStateIncidents(inputBean.getUser(), expIncidents);
			Set<String> ioi_codes = new HashSet<>();
			ioi_codes.add(ioi_Code);
			 validateTBIIncidents(responseBody, attributeBean, inputBean,ioi_codes );
			verifyThreatscoreIncidets(inputBean.getUser(), expIncidents);
		}
	
		}else{
			verifyNoIncidents(user);
		}
		
		getAndFeedbackToIncident(inputBean.getUser(), ioi_Code, unusual, clearIncident);
		
		if (!clearIncident) {
			Set<String> ioi_codes = new HashSet<>();
			ioi_codes.add(ioi_Code);
			validateTBIIncidents(responseBody, attributeBean, inputBean,ioi_codes );
		} else {
			Assert.assertFalse(isIncidentListed(inputBean.getUser(), ioi_Code), "The Expected Incident is NOT cleared");
		}
		
		Reporter.log("");
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);	
		Reporter.log("");
	}

	@Test(dataProvider = "dataProvider", description = "")
	public void download_Limit_Tests(Method method, String unusual, Boolean clearIncident) throws Exception {
		
		AttributeBean  attributeBean = new AttributeBean(2, 3, 2, true);
		
		InputBean inputBean = createFileUpdateData1("test_D_01", attributeBean);
		
		Log(method, attributeBean, inputBean);
		HttpResponse resp = getDetectAttributes();
		String responseBody = getResponseBody(resp);
		org.json.JSONArray getResponseArray = new JSONObject(responseBody).getJSONArray(OBJECTS);
		String ioi_Code = "TOO_MANY_SUM_LARGE_DOWNLOADS";
		boolean enabled = attributeBean.isEnabled();
		updateDetectAttributesForTBI(enabled, attributeBean, getResponseArray, ioi_Code);
		
		
		
		 user = inputBean.getUser();

		scpActivityLogsAndValidate(method, inputBean);
		// get state
		//Thread.sleep(30000);
		if(enabled){

			boolean abortTest = false;
			resp = getDetectAttributes();
			responseBody = getResponseBody(resp);
			getResponseArray = new JSONObject(responseBody).getJSONArray(OBJECTS);
			for (int index = 0; index < getResponseArray.length(); index++) {
				JSONObject attributeObject = getResponseArray.getJSONObject(index);
				if (ioi_Code.toString().equals((String) attributeObject.get(NAME))) {
					if(attributeObject.get("enabled").toString().equals("false")){
						Reporter.log("");
						Reporter.log("preference got changed in between test excetuion so terminating validation::::::", true);
						Reporter.log("");
						abortTest=true;
						throw new SkipException("preference got changed in between test excetuion so skipping test");
					}
				}
			}
		if (!abortTest) {

			HashSet<String> expIncidents = new HashSet<String>();
			expIncidents.add(ioi_Code);

			responseBody = verifyStateIncidents(inputBean.getUser(), expIncidents);
			Set<String> ioi_codes = new HashSet<>();
			ioi_codes.add(ioi_Code);
			 validateTBIIncidents(responseBody, attributeBean, inputBean,ioi_codes );
			verifyThreatscoreIncidets(inputBean.getUser(), expIncidents);
		}
	
		}else{
			verifyNoIncidents(user);
		}

		getAndFeedbackToIncident(inputBean.getUser(), ioi_Code, unusual, clearIncident);
		
		if (!clearIncident) {
			Set<String> ioi_codes = new HashSet<>();
			ioi_codes.add(ioi_Code);
			validateTBIIncidents(responseBody, attributeBean, inputBean,ioi_codes );
		} else {
			Assert.assertFalse(isIncidentListed(inputBean.getUser(), ioi_Code), "The Expected Incident is NOT cleared");
		}
		
		Reporter.log("");
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);	
		Reporter.log("");
	}

	@Test(dataProvider = "dataProvider", description = "")
	public void upload_Limit_Tests(Method method, String unusual, Boolean clearIncident) throws Exception {
		
		AttributeBean  attributeBean = new AttributeBean(2, 3, 2, true);
		
		InputBean inputBean = createFileUpdateData1("test_u_01", attributeBean);
		
		Log(method, attributeBean, inputBean);
		HttpResponse resp = getDetectAttributes();
		String responseBody = getResponseBody(resp);
		org.json.JSONArray getResponseArray = new JSONObject(responseBody).getJSONArray(OBJECTS);
		String ioi_Code = "TOO_MANY_SUM_LARGE_UPLOADS";
		boolean enabled = attributeBean.isEnabled();
		updateDetectAttributesForTBI(enabled, attributeBean, getResponseArray, ioi_Code);
		
		
		
		 user = inputBean.getUser();

		scpActivityLogsAndValidate(method, inputBean);
		// get state
		//Thread.sleep(30000);
		
		if(enabled){

			boolean abortTest = false;
			resp = getDetectAttributes();
			responseBody = getResponseBody(resp);
			getResponseArray = new JSONObject(responseBody).getJSONArray(OBJECTS);
			for (int index = 0; index < getResponseArray.length(); index++) {
				JSONObject attributeObject = getResponseArray.getJSONObject(index);
				if (ioi_Code.toString().equals((String) attributeObject.get(NAME))) {
					if(attributeObject.get("enabled").toString().equals("false")){
						Reporter.log("");
						Reporter.log("preference got changed in between test excetuion so terminating validation::::::", true);
						Reporter.log("");
						abortTest=true;
						throw new SkipException("preference got changed in between test excetuion so skipping test");
					}
				}
			}
		if (!abortTest) {

			HashSet<String> expIncidents = new HashSet<String>();
			expIncidents.add(ioi_Code);

			responseBody = verifyStateIncidents(inputBean.getUser(), expIncidents);
			Set<String> ioi_codes = new HashSet<>();
			ioi_codes.add(ioi_Code);
			 validateTBIIncidents(responseBody, attributeBean, inputBean,ioi_codes );
			verifyThreatscoreIncidets(inputBean.getUser(), expIncidents);
		}
	
		}else{
			verifyNoIncidents(user);
		}

		getAndFeedbackToIncident(inputBean.getUser(), ioi_Code, unusual, clearIncident);
		
		if (!clearIncident) {
			Set<String> ioi_codes = new HashSet<>();
			ioi_codes.add(ioi_Code);
			validateTBIIncidents(responseBody, attributeBean, inputBean,ioi_codes );
		} else {
			Assert.assertFalse(isIncidentListed(inputBean.getUser(), ioi_Code), "The Expected Incident is NOT cleared");
		}
		
		Reporter.log("");
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);	
		Reporter.log("");
	}

	@Test(dataProvider = "dataProvider", description = "")
	public void policy_Violations_Tests(Method method, String unusual, Boolean clearIncident) throws Exception {
		
		AttributeBean  attributeBean = new AttributeBean(2, 3, 2, true);
		
		InputBean inputBean = createFileUpdateData1("test_p_01", attributeBean);
		Log(method, attributeBean,inputBean);
		//###########create policy##############
		//createPolicy();
		Reporter.log("Polcy created sucesfully ::::::     ",true);
		
		HttpResponse resp = getDetectAttributes();
		String responseBody = getResponseBody(resp);
		org.json.JSONArray getResponseArray = new JSONObject(responseBody).getJSONArray(OBJECTS);
		String ioi_Code = "TOO_MANY_POLICY_VIOLATIONS";
		
		boolean enabled = attributeBean.isEnabled();
		updateDetectAttributesForTBI(enabled, attributeBean, getResponseArray, ioi_Code);
		
		

		 user = inputBean.getUser();

		scpActivityLogsAndValidate(method, inputBean);
		// get state
		//Thread.sleep(1* 60 * 1000);
		if(enabled){

			boolean abortTest = false;
			resp = getDetectAttributes();
			responseBody = getResponseBody(resp);
			getResponseArray = new JSONObject(responseBody).getJSONArray(OBJECTS);
			for (int index = 0; index < getResponseArray.length(); index++) {
				JSONObject attributeObject = getResponseArray.getJSONObject(index);
				if (ioi_Code.toString().equals((String) attributeObject.get(NAME))) {
					if(attributeObject.get("enabled").toString().equals("false")){
						Reporter.log("");
						Reporter.log("preference got changed in between test excetuion so terminating validation::::::", true);
						Reporter.log("");
						abortTest=true;
						throw new SkipException("preference got changed in between test excetuion so skipping test");
					}
				}
			}
		if (!abortTest) {

			HashSet<String> expIncidents = new HashSet<String>();
			expIncidents.add(ioi_Code);

			responseBody = verifyStateIncidents(inputBean.getUser(), expIncidents);
			Set<String> ioi_codes = new HashSet<>();
			ioi_codes.add(ioi_Code);
			 validateTBIIncidents(responseBody, attributeBean, inputBean,ioi_codes );
			verifyThreatscoreIncidets(inputBean.getUser(), expIncidents);
		}
	
		}else{
			verifyNoIncidents(user);
		}

		getAndFeedbackToIncident(inputBean.getUser(), ioi_Code, unusual, clearIncident);
		
		if (!clearIncident) {
			Set<String> ioi_codes = new HashSet<>();
			ioi_codes.add(ioi_Code);
			validateTBIIncidents(responseBody, attributeBean, inputBean,ioi_codes );
		} else {
			Assert.assertFalse(isIncidentListed(inputBean.getUser(), ioi_Code), "The Expected Incident is NOT cleared");
		}
		
		Reporter.log("");
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);	
		Reporter.log("");
	}

	@Test(dataProvider = "dataProvider", description = "")
	public void divices_Tests(Method method, String unusual, Boolean clearIncident) throws Exception {
		
		AttributeBean  attributeBean = new AttributeBean(2, 3, 2, true);
		
		InputBean inputBean = createFileUpdateData1("test_b_01",attributeBean );
		Log(method, attributeBean,inputBean);
		HttpResponse resp = getDetectAttributes();
		String responseBody = getResponseBody(resp);
		org.json.JSONArray getResponseArray = new JSONObject(responseBody).getJSONArray(OBJECTS);
		
		String ioi_Code = "TOO_MANY_DEVICES";
		boolean enabled = attributeBean.isEnabled();
		updateDetectAttributesForTBI(enabled, attributeBean, getResponseArray, ioi_Code);
		
		

		scpActivityLogsAndValidate(method, inputBean);
		user = inputBean.getUser();
		

		//Thread.sleep(1* 10 * 1000);	
		if(enabled){

			boolean abortTest = false;
			resp = getDetectAttributes();
			responseBody = getResponseBody(resp);
			getResponseArray = new JSONObject(responseBody).getJSONArray(OBJECTS);
			for (int index = 0; index < getResponseArray.length(); index++) {
				JSONObject attributeObject = getResponseArray.getJSONObject(index);
				if (ioi_Code.toString().equals((String) attributeObject.get(NAME))) {
					if(attributeObject.get("enabled").toString().equals("false")){
						Reporter.log("");
						Reporter.log("preference got changed in between test excetuion so terminating validation::::::", true);
						Reporter.log("");
						abortTest=true;
						throw new SkipException("preference got changed in between test excetuion so skipping test");
					}
				}
			}
		if (!abortTest) {

			HashSet<String> expIncidents = new HashSet<String>();
			expIncidents.add(ioi_Code);

			responseBody = verifyStateIncidents(inputBean.getUser(), expIncidents);
			Set<String> ioi_codes = new HashSet<>();
			ioi_codes.add(ioi_Code);
			 validateTBIIncidents(responseBody, attributeBean, inputBean,ioi_codes );
			verifyThreatscoreIncidets(inputBean.getUser(), expIncidents);
		}
	
		}else{
			verifyNoIncidents(inputBean.getUser());
		}

		getAndFeedbackToIncident(inputBean.getUser(), ioi_Code, unusual, clearIncident);
		
		if (!clearIncident) {
			Set<String> ioi_codes = new HashSet<>();
			ioi_codes.add(ioi_Code);
			validateTBIIncidents(responseBody, attributeBean, inputBean,ioi_codes );
		} else {
			Assert.assertFalse(isIncidentListed(inputBean.getUser(), ioi_Code), "The Expected Incident is NOT cleared");
		}
		
		Reporter.log("");
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);	
		Reporter.log("");
	}

	@Test(dataProvider = "dataProvider", description = "")
	public void browsers_Tests(Method method, String unusual, Boolean clearIncident) throws Exception {
		
		AttributeBean  attributeBean = new AttributeBean(2, 3, 2, true);
		
		InputBean inputBean = createFileUpdateData1("test001",attributeBean);
		Log(method, attributeBean,inputBean);
		HttpResponse resp = getDetectAttributes();
		String responseBody = getResponseBody(resp);
		org.json.JSONArray getResponseArray = new JSONObject(responseBody).getJSONArray(OBJECTS);
		String ioi_Code = "TOO_MANY_BROWSERS";
		boolean enabled = attributeBean.isEnabled();
		updateDetectAttributesForTBI(enabled, attributeBean, getResponseArray, ioi_Code);
		
		
		user = inputBean.getUser();
		scpActivityLogsAndValidate(method, inputBean);
		

		//Thread.sleep(30000);
		if(enabled){

			boolean abortTest = false;
			resp = getDetectAttributes();
			responseBody = getResponseBody(resp);
			getResponseArray = new JSONObject(responseBody).getJSONArray(OBJECTS);
			for (int index = 0; index < getResponseArray.length(); index++) {
				JSONObject attributeObject = getResponseArray.getJSONObject(index);
				if (ioi_Code.toString().equals((String) attributeObject.get(NAME))) {
					if(attributeObject.get("enabled").toString().equals("false")){
						Reporter.log("");
						Reporter.log("preference got changed in between test excetuion so terminating validation::::::", true);
						Reporter.log("");
						abortTest=true;
						throw new SkipException("preference got changed in between test excetuion so skipping test");
					}
				}
			}
		if (!abortTest) {

			HashSet<String> expIncidents = new HashSet<String>();
			expIncidents.add(ioi_Code);

			responseBody = verifyStateIncidents(inputBean.getUser(), expIncidents);
			Set<String> ioi_codes = new HashSet<>();
			ioi_codes.add(ioi_Code);
			 validateTBIIncidents(responseBody, attributeBean, inputBean,ioi_codes );
			verifyThreatscoreIncidets(inputBean.getUser(), expIncidents);
		}
	
		}else{
			verifyNoIncidents(inputBean.getUser());
		}
		
		getAndFeedbackToIncident(inputBean.getUser(), ioi_Code, unusual, clearIncident);
		
		if (!clearIncident) {
			Set<String> ioi_codes = new HashSet<>();
			ioi_codes.add(ioi_Code);
			validateTBIIncidents(responseBody, attributeBean, inputBean,ioi_codes );
		} else {
			Assert.assertFalse(isIncidentListed(inputBean.getUser(), ioi_Code), "The Expected Incident is NOT cleared");
		}
		
		Reporter.log("");
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);	
		Reporter.log("");
	}

	@Test(dataProvider = "dataProvider", description = "")
	public void encripted_Files_Tests(Method method, String unusual, Boolean clearIncident) throws Exception {
		
		AttributeBean  attributeBean = new AttributeBean(2, 3, 2, true);
		
		InputBean inputBean = createFileUpdateData1("test_e_1",attributeBean);
		Log(method, attributeBean,inputBean);
		HttpResponse resp = getDetectAttributes();
		String responseBody = getResponseBody(resp);
		org.json.JSONArray getResponseArray = new JSONObject(responseBody).getJSONArray(OBJECTS);
		String ioi_Code = "TOO_MANY_ENCRYPTED_FILES";
		boolean enabled = attributeBean.isEnabled();
		updateDetectAttributesForTBI(enabled, attributeBean, getResponseArray, ioi_Code);
		
		
		user = inputBean.getUser();
		scpActivityLogsAndValidate(method, inputBean);
		
		

		if(enabled){

			boolean abortTest = false;
			resp = getDetectAttributes();
			responseBody = getResponseBody(resp);
			getResponseArray = new JSONObject(responseBody).getJSONArray(OBJECTS);
			for (int index = 0; index < getResponseArray.length(); index++) {
				JSONObject attributeObject = getResponseArray.getJSONObject(index);
				if (ioi_Code.toString().equals((String) attributeObject.get(NAME))) {
					if(attributeObject.get("enabled").toString().equals("false")){
						Reporter.log("");
						Reporter.log("preference got changed in between test excetuion so terminating validation::::::", true);
						Reporter.log("");
						abortTest=true;
						throw new SkipException("preference got changed in between test excetuion so skipping test");
					}
				}
			}
		if (!abortTest) {

			HashSet<String> expIncidents = new HashSet<String>();
			expIncidents.add(ioi_Code);

			responseBody = verifyStateIncidents(inputBean.getUser(), expIncidents);
			Set<String> ioi_codes = new HashSet<>();
			ioi_codes.add(ioi_Code);
			 validateTBIIncidents(responseBody, attributeBean, inputBean,ioi_codes );
			verifyThreatscoreIncidets(inputBean.getUser(), expIncidents);
		}
	
		}else{
			verifyNoIncidents(inputBean.getUser());
		}

		getAndFeedbackToIncident(inputBean.getUser(), ioi_Code, unusual, clearIncident);
		
		if (!clearIncident) {
			Set<String> ioi_codes = new HashSet<>();
			ioi_codes.add(ioi_Code);
			validateTBIIncidents(responseBody, attributeBean, inputBean,ioi_codes );
		} else {
			Assert.assertFalse(isIncidentListed(inputBean.getUser(), ioi_Code), "The Expected Incident is NOT cleared");
		}
		
		Reporter.log("");
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);	
		Reporter.log("");
	}
	
	
	@Test(dataProvider = "dataProvider", description = "")
	public void suspicious_Location_Test(Method method, String unusual, Boolean clearIncident) throws Exception {
		
		AttributeBean  attributeBean = new AttributeBean(2, 1440, 2, true);
		
		InputBean inputBean = createFileUpdateData1("test_login_002", attributeBean);
		Log(method, attributeBean,inputBean);
		HttpResponse resp = getDetectAttributes();
		String responseBody = getResponseBody(resp);
		org.json.JSONArray getResponseArray = new JSONObject(responseBody).getJSONArray(OBJECTS);
		String ioi_Code = "TOO_MANY_INFEASIBLE_LOCATIONS";
		boolean enabled = attributeBean.isEnabled();
		updateDetectAttributesForTBI(enabled, attributeBean, getResponseArray, ioi_Code);
		

		 user = inputBean.getUser();

		scpActivityLogsAndValidate(method, inputBean);
		// get state
		//Thread.sleep(30000);
		if(enabled){

			boolean abortTest = false;
			resp = getDetectAttributes();
			responseBody = getResponseBody(resp);
			getResponseArray = new JSONObject(responseBody).getJSONArray(OBJECTS);
			for (int index = 0; index < getResponseArray.length(); index++) {
				JSONObject attributeObject = getResponseArray.getJSONObject(index);
				if (ioi_Code.toString().equals((String) attributeObject.get(NAME))) {
					if(attributeObject.get("enabled").toString().equals("false")){
						Reporter.log("");
						Reporter.log("preference got changed in between test excetuion so terminating validation::::::", true);
						Reporter.log("");
						abortTest=true;
						throw new SkipException("preference got changed in between test excetuion so skipping test");
					}
				}
			}
		if (!abortTest) {

			HashSet<String> expIncidents = new HashSet<String>();
			expIncidents.add(ioi_Code);

			responseBody = verifyStateIncidents(inputBean.getUser(), expIncidents);
			Set<String> ioi_codes = new HashSet<>();
			ioi_codes.add(ioi_Code);
			 validateTBIIncidents(responseBody, attributeBean, inputBean,ioi_codes );
			verifyThreatscoreIncidets(inputBean.getUser(), expIncidents);
		}
	
		}else{
			verifyNoIncidents(user);
		}

		getAndFeedbackToIncident(inputBean.getUser(), ioi_Code, unusual, clearIncident);
		
		if (!clearIncident) {
			Set<String> ioi_codes = new HashSet<>();
			ioi_codes.add(ioi_Code);
			validateTBIIncidents(responseBody, attributeBean, inputBean,ioi_codes );
		} else {
			Assert.assertFalse(isIncidentListed(inputBean.getUser(), ioi_Code), "The Expected Incident is NOT cleared");
		}
		
		Reporter.log("");
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);	
		Reporter.log("");		
	}
	
	@Test(description = "This test operates on elastic search injected data, and generates TOO_MANY_INVALID_LOGINS_GROUP incidents.")
	public void InvalidLogins_Group_Tests(Method method) throws Exception {
		
		AttributeBean  attributeBean = new AttributeBean(10, 10, 3, true);
	
		String tcId = "invalidLGTests";
		InputBean inputBean = null;
		ActivityLogs al = new ActivityLogs();
		String tmplFileName = tcId + ".json";
		String  fileName = tcId + "_" + suiteData.getTenantName() + ".json";
		 String randomString1 = RandomStringUtils.randomAlphanumeric(12);
		 String testId = randomString1+ "_" +tcId;
	 		
		inputBean = new InputBean(tmplFileName, fileName, null, user, testId);
		
		Log(method, attributeBean, inputBean);
		
		String ioi_Code = "TOO_MANY_INVALID_LOGINS_GROUP";
		HttpResponse resp = getDetectAttributes();
		String responseBody = getResponseBody(resp);
		org.json.JSONArray getResponseArray = new JSONObject(responseBody).getJSONArray(OBJECTS);
		attributeBean.setIoi_code(ioi_Code);
		boolean enabled = attributeBean.isEnabled();
		updateDetectAttributesForTBI(enabled, attributeBean, getResponseArray, ioi_Code);
		
		System.out.println("Test Details ###  " + inputBean.toString());
		
		String tmplFilePath = suiteData.getGoldenInputTmplPath();
		String resLogFilePath = suiteData.getGoldenInputFilePath();
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.setVisibility(JsonMethod.FIELD, Visibility.ANY);
		
		SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    	formatDate.setTimeZone(TimeZone.getTimeZone("GMT"));
    	
    	SimpleDateFormat formatYear = new SimpleDateFormat("yyyy");
    	formatYear.setTimeZone(TimeZone.getTimeZone("GMT"));
		try {
			Set<String> saasApps = new HashSet<>();
			BufferedReader br = new BufferedReader(new FileReader(tmplFilePath + tmplFileName));
			File logFile = new File(resLogFilePath + fileName);
			logFile.createNewFile();
			BufferedWriter bw = new BufferedWriter(new FileWriter(logFile));
			long count =0;
			
			int idx = 1;
		    for(String line; (line = br.readLine()) != null; idx++) {
		 		
		    	count++;
		    	Calendar cal = Calendar.getInstance();
		    	cal.add(Calendar.SECOND, idx);
		    	String strDateTime = formatDate.format(cal.getTime());
		    	String strYear = formatYear.format(cal.getTime());
		    	
		    	String sourceId = RandomStringUtils.randomAlphanumeric(22);
		    	
		    	
		        JsonNode activity = mapper.readTree(line);
		        JsonNode _source = activity.path("_source");
		        
		        saasApps.add((String)_source.get("facility").toString().replace("\"", ""));
		        
		        if (user!=null && activity.has("_id")) {
		        	((ObjectNode)activity).put("_id", sourceId);
		        }
		        ((ObjectNode)_source).put("test_id", testId);
		       
		        ((ObjectNode)_source).put("created_timestamp", strDateTime);
		        ((ObjectNode)_source).put("inserted_timestamp", strDateTime);
		        ((ObjectNode)activity).put("_index", "alias_logs_" + suiteData.getTenantName() + "-" + strYear);
		        
		        bw.write(activity.toString() + "\n");
		    }
		    inputBean.setCount(count);
		    inputBean.setSaasApps(saasApps);
		    bw.flush();
		    bw.close();
		    br.close();
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	

		scpActivityLogsAndValidate(method, inputBean);
		
		String[] users = {"sagar2@elastica.co","sagar3@elastica.co","sagar4@elastica.co","sagar5@elastica.co","sagar6@elastica.co",
				"sagar7@elastica.co","sagar8@elastica.co","sagar9@elastica.co","sagar10@elastica.co","sagar11@elastica.co","sagar12@elastica.co"};
		
		Assert.assertTrue(isMultiUserIncidentListed(users, ioi_Code), "One or More Users Information is NOT listed Under DETECT");
		
		getAndFeedbackToIncident("MULTI_USER", ioi_Code, "No", false);
		Assert.assertTrue(isMultiUserIncidentListed(users, ioi_Code), "The Expected Incident is CLEARED");
		
		getAndFeedbackToIncident("MULTI_USER", ioi_Code, "No", true);
		Assert.assertFalse(isMultiUserIncidentListed(users, ioi_Code), "The Expected Incident is NOT cleared");
		
			Reporter.log("");
			Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);	
			Reporter.log("");
	}
	

	public Boolean isMultiUserIncidentListed(String[] users, String ioi_Code) throws Exception {
		String responseBody =  getListOfIncidents();
		JsonNode jnode = unmarshall(responseBody, JsonNode.class);
		System.out.println("Respon : " + responseBody);
		if (jnode.isArray()) {
		    for (final JsonNode objNode : jnode) {
		    	String ioi = getJSONValue(objNode.toString(), "ioi").toString().replace("\"", "");
		    	String user = getJSONValue(objNode.toString(), "u").toString().replace("\"", "");
		    	
		    	if (user.equals("Multiple users") && ioi.equals(ioi_Code)) {
		    		//TODO: List of Users validation should be done. 
		    		return true;
		    	}
		       }
		    }
		
		return false;
	}
	

	@Test(dataProvider = "dataProvider", description = "")
	public void SD_ShareUnshareWaitRepeatTest(Method method, String unusual, Boolean clearIncident) throws Exception{
		
		String randomString = RandomStringUtils.randomAlphanumeric(12);
		String userName = "detect_" + randomString;
		// String userName = "tarak.elastica";
		user = userName + "@" + suiteData.getTenantDomainName();
		String name = SequenceDetectorConstants.SHARE_UNSHARE_WAIT_REPEAT;
		
		Log(method, name, user, userName );
		
		try {
			Reporter.log("Cleaning up all the existing Sequences!!!");
			dsd.deleteSequenceDetectors(suiteData);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String[] activities = new String[]{"Share", "Unshare"};
		String[] failities = new String[]{"__any", "__any"} ;
		String[] sources = new String[]{"__any", "__any"} ;
		String[] users = new String[]{user, user} ;
		String[] objects = new String[]{"__any", "__any"} ;
		
		SDInput sdInput = dsd.createSDInput(name, 3,36, false   ,false, true,
				activities, failities, sources, users, objects);
		HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
		
		System.out.println("dsdto:::::: "+response.getStatusLine());
		String responseBody = getResponseBody(response);
		Reporter.log("Sequence detectors response body:::::: "+responseBody, true);
		InputBean inputBean = createFileUpdateData("shareUnshare", user, userName, randomString);

		scpActivityLogsAndValidate(method, inputBean);
		

		String ioicode = TOO_MANY_SEQUENCE+name;
		
		HashSet<String> expIncidents = new HashSet<String>();
		expIncidents.add(ioicode);
		verifyStateIncidents(inputBean.getUser(), expIncidents);
		
		getAndFeedbackToIncident(inputBean.getUser(), ioicode, unusual, clearIncident);
		
		if (!clearIncident) {
			verifyStateIncidents(inputBean.getUser(), expIncidents);
			Assert.assertTrue(isIncidentListed(inputBean.getUser(), ioicode), "The Expected Incident is cleared, which is NOT expected.");
		} else {
			Assert.assertFalse(isIncidentListed(inputBean.getUser(), ioicode), "The Expected Incident is NOT cleared");
		}
		
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
		
		
	}
	
	@Test(dataProvider = "dataProvider", description = "This test operates on real API data, and generates BBI frequent sessions  incidents.")
	public void SD_largeShareAndUnshareTest(Method method, String unusual, Boolean clearIncident) throws Exception {
		AttributeBean  attributeBean = new AttributeBean(20, 2,true);
		InputBean inputBean = createFileUpdateDataforfrequent("largeShareProfile", attributeBean);
		
		Log(method, attributeBean,inputBean);
		
		try {
			Reporter.log("Cleaning up all the existing Sequences!!!");
			dsd.deleteSequenceDetectors(suiteData);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		HttpResponse resp = getDetectAttributes();
		String responseBody = getResponseBody(resp);
		org.json.JSONArray getResponseArray = new JSONObject(responseBody).getJSONArray(OBJECTS);
		String ioi_Code = IOI_Code.ANOMALOUSLY_LARGE_SHARING.toString();
		boolean enabled = attributeBean.isEnabled();
		DetectAttributeDto detectAttributeDto = new DetectAttributeDto();
		updateBBIAttributes(enabled, attributeBean, getResponseArray, detectAttributeDto, ioi_Code);
		Reporter.log("updated preferences ::::::     " + attributeBean.toString(), true);
		
		user = inputBean.getUser();
		scpActivityLogsAndValidate(method, inputBean);
		
		verifyProfile(inputBean.getUser(), ioi_Code);
		
		String name = SequenceDetectorConstants.LARGE_SHARE_UNSHARE;
		

		String[] activityTypes = new String[]{"Unshare",  ioi_Code};
		String[] facilities = new String[]{"__any", "__any"} ;
		String[] sources = new String[]{"__any", "__any"} ;
		String[] users = new String[]{user, user} ;
		String[] objects = new String[]{"__any", "__any"} ;
		Integer[] steps = new Integer[]{0,1};
		
		SDInput sdInput = dsd.createSDInputForBBIinSD(name, 1, 300, false, false, 
				true, activityTypes, facilities, sources, users, objects, steps);
		
		HttpResponse response = dsd.createSequenceDetectorforBBIinSD(sdInput, suiteData);
		 responseBody = getResponseBody(response);
		JsonNode detect_sequences = new ObjectMapper().readTree(getJSONValue(responseBody.toString(), "detect_sequences"));
		System.out.println("detect_sequences::::::::::: "+detect_sequences.toString());
		Assert.assertEquals(response.getStatusLine().getStatusCode(), 200, "Response code is not equal");
		
		 inputBean =  createFileUpdateDataforBBIInSequenceDetector("largeShareUnshareSDTest", attributeBean, inputBean);
		 scpActivityLogsAndValidate1(method, inputBean);
		 
		 String ioicode = TOO_MANY_SEQUENCE+name;
			
			HashSet<String> expIncidents = new HashSet<String>();
			expIncidents.add(ioicode);
			expIncidents.add(ioi_Code);
			verifyStateIncidents(inputBean.getUser(), expIncidents);
		
			getAndFeedbackToIncident(inputBean.getUser(), ioicode, unusual, clearIncident);
			
			if (!clearIncident) {
				verifyStateIncidents(inputBean.getUser(), expIncidents);
				Assert.assertTrue(isIncidentListed(inputBean.getUser(), ioicode), "The Expected Incident is cleared, which is NOT expected.");
			} else {
				Assert.assertFalse(isIncidentListed(inputBean.getUser(), ioicode), "The Expected Incident is NOT cleared");
			}
	
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
	}
	
	@Test(dataProvider = "dataProvider", description = "This test operates on real API data, and generates BBI frequent sessions  incidents.")
	public void SD_frequentDeleteAndDownloadTest(Method method, String unusual, Boolean clearIncident) throws Exception {
		AttributeBean  attributeBean = new AttributeBean(20, 2,true);
		InputBean inputBean = createFileUpdateDataforfrequent("largeDeleteProfile", attributeBean);
		
		Log(method, attributeBean,inputBean);
		
		try {
			Reporter.log("Cleaning up all the existing Sequences!!!");
			dsd.deleteSequenceDetectors(suiteData);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		HttpResponse resp = getDetectAttributes();
		String responseBody = getResponseBody(resp);
		org.json.JSONArray getResponseArray = new JSONObject(responseBody).getJSONArray(OBJECTS);
		String ioi_Code = IOI_Code.ANOMALOUSLY_FREQUENT_DELETES.toString();
		boolean enabled = attributeBean.isEnabled();
		DetectAttributeDto detectAttributeDto = new DetectAttributeDto();
		updateBBIAttributes(enabled, attributeBean, getResponseArray, detectAttributeDto, ioi_Code);
		Reporter.log("updated preferences ::::::     " + attributeBean.toString(), true);
		
		user = inputBean.getUser();
		scpActivityLogsAndValidate(method, inputBean);
		
		verifyProfile(inputBean.getUser(), ioi_Code);
		
		String name = SequenceDetectorConstants.FREQUENT_DELETE_DOWNLOAD;
		String[] activityTypes = new String[]{"Download", ioi_Code};
		String[] facilities = new String[]{"__any", "__any"} ;
		String[] sources = new String[]{"__any", "__any"} ;
		String[] users = new String[]{user, user} ;
		String[] objects = new String[]{"__any", "__any"} ;
		Integer[] steps = new Integer[]{0,1};
		
		SDInput sdInput = dsd.createSDInputForBBIinSD(name, 1, 300, false, false, 
				true, activityTypes, facilities, sources, users, objects, steps);
		
		HttpResponse response = dsd.createSequenceDetectorforBBIinSD(sdInput, suiteData);
		 responseBody = getResponseBody(response);
		JsonNode detect_sequences = new ObjectMapper().readTree(getJSONValue(responseBody.toString(), "detect_sequences"));
		System.out.println("detect_sequences::::::::::: "+detect_sequences.toString());
		Assert.assertEquals(response.getStatusLine().getStatusCode(), 200, "Response code is not equal");
		
		 inputBean =  createFileUpdateDataforBBIInSequenceDetector("frequentDeleteDownloadSDTest", attributeBean, inputBean);
		 scpActivityLogsAndValidate1(method, inputBean);
		 
		 String ioicode = TOO_MANY_SEQUENCE+name;
			
			HashSet<String> expIncidents = new HashSet<String>();
			expIncidents.add(ioicode);
			expIncidents.add(ioi_Code);
			verifyStateIncidents(inputBean.getUser(), expIncidents);

			getAndFeedbackToIncident(inputBean.getUser(), ioicode, unusual, clearIncident);
			
			if (!clearIncident) {
				verifyStateIncidents(inputBean.getUser(), expIncidents);
				Assert.assertTrue(isIncidentListed(inputBean.getUser(), ioicode), "The Expected Incident is cleared, which is NOT expected.");
			} else {
				Assert.assertFalse(isIncidentListed(inputBean.getUser(), ioicode), "The Expected Incident is NOT cleared");
			}
	
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
	}
	
	@AfterMethod
	public void tearDown(ITestResult results) throws Exception {
		Reporter.log("         :::::::::::::::::::::::::::::::::::::::::::::::::::::",true);
		Reporter.log("",true);
		String result ="FAILED";
		if(results.isSuccess()){
			result = "PASSED";
		}
		
		Reporter.log("TestCase Name::::::: "+results.getName()+"   Test result::::::: "+result,true);
		if(!results.isSuccess()){
		Reporter.log("Please check  Assertion exception for  failures");
		}
		Reporter.log("",true);
		Reporter.log("         :::::::::::::::::::::::::::::::::::::::::::::::::::::",true);
		
		Reporter.log("Cleaning up Resources",true);
		
		HttpResponse response;
		String responseBody;
		ElasticSearchLogs esLogs = new ElasticSearchLogs();
		
		if (results.isSuccess() && user != null) {
			
			response = esLogs.clearThreatscore(restClient, user, suiteData.getTenantName());
			responseBody = getResponseBody(response);
			System.out.println(responseBody);

			response = esLogs.clearState(restClient, user, suiteData.getTenantName());
			responseBody = getResponseBody(response);
			System.out.println(responseBody);

			response = esLogs.clearDetectProfile(restClient, user, suiteData.getTenantName());
			responseBody = getResponseBody(response);
			System.out.println(responseBody);
			
			user = null;
		} else {
			Reporter.log("No Cleanup Required - I am not cleaning up data to debug if needed!",true);
		}
		Reporter.log("         :::::::::::::::::::::::::::::::::::::::::::::::::::::",true);
		Reporter.log("",true);
		Reporter.log("Cleaning up Resources - COMPLETED",true);
		Reporter.log("",true);
		Reporter.log("         :::::::::::::::::::::::::::::::::::::::::::::::::::::",true);
	}
	

	private void Log(Method method, String name, String user, String userName) {
		Reporter.log("       ##################:::::::::::###################     ");
		Reporter.log(" ");
		Reporter.log("Execution Started - Test Case Name::::: " + method.getName(), true);
		Reporter.log(" ");
		Reporter.log(" This test  will create squence detector , inject activities and verify the incident::: name of squence detector:::"
				+ ": "+name+"  ::::user::::  "+user+"  :::::: user name:::: "+userName,true);
		Reporter.log(" ");
		Reporter.log("To verify manually login to "+suiteData.getEnvironmentName()+"  with user:: "+suiteData.getUsername()+" and pass word for this user is :: "+suiteData.getPassword(),true);
		Reporter.log(" ");
		Reporter.log("        #################::::::::::::####################    ");
	}
	
	private void Log(Method method, AttributeBean attributeBean, InputBean inputBean) {
		Reporter.log("       #####################################");
		Reporter.log(" ");
		Reporter.log("Execution Started - Test Case Name::::: " + method.getName(), true);
		Reporter.log(" ");
		Reporter.log("This Test is to validate " + method.getName().replace("_Tests", "")+",   And preferences for this test are  preference enabled???:::::: "+attributeBean.isEnabled() +" window:: "
				+ ""+attributeBean.getWindow()+" event::: "+attributeBean.getThreshold()+" importance:::: "+attributeBean.getImportance(), true);
		Reporter.log(" ");
		Reporter.log("Test details :::::::  userName::  " + inputBean.getUserName()+"  user::::: "+inputBean.getUser()+" testId:::  "+inputBean.getTestId(), true);
		Reporter.log(" ");
		Reporter.log("To verify manually login to "+suiteData.getEnvironmentName()+"  with user:: "+suiteData.getUsername()+" and password for this user is :: "+suiteData.getPassword(),true);
		Reporter.log(" ");
		Reporter.log("        #####################################");
	}

}
