package com.elastica.beatle.tests.detect;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import org.apache.http.HttpResponse;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.elastica.beatle.detect.dto.AttributeBean;
import com.elastica.beatle.detect.dto.DetectAttributeDto;
import com.elastica.beatle.detect.dto.InputBean;
import com.elastica.beatle.es.ElasticSearchLogs;

public class DetectBBIFeedbackTests extends DetectUtils {

	private static final String OBJECTS = "detect_attributes";
	String user = null;

	@DataProvider()
	public static  Object[][] dataProvider(Method method)  {

		Object[][] feedbacks = {
				{"Yes", false, false},
				{"Yes", true, false},
				{"Yes", false, true},
				{"Yes", true, true},
				{"No", false, false},
				{"No", true, false},
				{"No", false, true},
				{"No", true, true}
		};

		return feedbacks;
	}

	@Test(dataProvider = "dataProvider", description = "This test operates on real API data, and generates BBI frequent sessions  incidents.")
	public void frequent_Sessions_Tests(Method method, String unusual, Boolean clearIncident, Boolean clearProfile) throws Exception {

		AttributeBean attributeBean = new AttributeBean(30, 3, true);

		InputBean inputBean = createFileUpdateDataforfrequent("frequentUserActions", attributeBean);

		Log(method, attributeBean,inputBean);
		Reporter.log("Test Parameters :::::::  unusual ::  " + unusual + "  clearIncident :: " + clearIncident + " clearProfile :: " + clearProfile, true);

		HttpResponse resp = getDetectAttributes();
		String responseBody = getResponseBody(resp);
		org.json.JSONArray getResponseArray = new JSONObject(responseBody).getJSONArray(OBJECTS);
		String ioi_Code = "ANOMALOUSLY_FREQUENT_SESSIONS";
		DetectAttributeDto detectAttributeDto = new DetectAttributeDto();
		updateBBIAttributes(attributeBean.isEnabled(), attributeBean, getResponseArray, detectAttributeDto, ioi_Code);
		Reporter.log("updated preferences ::::::     " + attributeBean.toString(), true);

		user = inputBean.getUser();
		scpActivityLogsAndValidate(method, inputBean);

		HashSet<String> expIncidents = new HashSet<String>();
		expIncidents.add(ioi_Code);

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

		Assert.assertFalse(abortTest, "preference got changed in between test excetuion so skipping test");

		verifyProfile(inputBean.getUser(), ioi_Code);
		responseBody = verifyStateIncidents(inputBean.getUser(), expIncidents);
		Set<String> ioi_codes = new HashSet<>();
		ioi_codes.add(ioi_Code);
		verifyThreatscoreIncidets(inputBean.getUser(), expIncidents);

		getAndFeedbackToIncident(inputBean.getUser(), ioi_Code, unusual, clearIncident, clearProfile);

		if (!clearIncident) {
			responseBody = verifyStateIncidents(inputBean.getUser(), expIncidents);
		} else {
			Assert.assertFalse(isIncidentListed(inputBean.getUser(), ioi_Code), "The Expected Incident is NOT cleared");
		}

		if (clearProfile) {
			verifyNoProfile(inputBean.getUser(), ioi_Code);
		} else {
			verifyProfile(inputBean.getUser(), ioi_Code);
		}

		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
	}

	@Test(dataProvider = "dataProvider", description = "This test operates on real API data, and generates BBI for  frequent deletes incidents.")
	public void frequent_Deletes_Tests(Method method, String unusual, Boolean clearIncident, Boolean clearProfile) throws Exception {

		AttributeBean attributeBean = new AttributeBean(30, 3, true);

		InputBean inputBean = createFileUpdateDataforfrequent("frequentDeletesTest", attributeBean);

		Log(method, attributeBean,inputBean);
		Reporter.log("Test Parameters :::::::  unusual ::  " + unusual + "  clearIncident :: " + clearIncident + " clearProfile :: " + clearProfile, true);

		HttpResponse resp = getDetectAttributes();
		String responseBody = getResponseBody(resp);
		org.json.JSONArray getResponseArray = new JSONObject(responseBody).getJSONArray(OBJECTS);
		String ioi_Code = "ANOMALOUSLY_FREQUENT_DELETES";
		DetectAttributeDto detectAttributeDto = new DetectAttributeDto();
		updateBBIAttributes(attributeBean.isEnabled(), attributeBean, getResponseArray, detectAttributeDto, ioi_Code);
		Reporter.log("updated preferences ::::::     " + attributeBean.toString(), true);

		user = inputBean.getUser();
		scpActivityLogsAndValidate(method, inputBean);

		HashSet<String> expIncidents = new HashSet<String>();
		expIncidents.add(ioi_Code);

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

		Assert.assertFalse(abortTest, "preference got changed in between test excetuion so skipping test");

		verifyProfile(inputBean.getUser(), ioi_Code);
		responseBody = verifyStateIncidents(inputBean.getUser(), expIncidents);
		Set<String> ioi_codes = new HashSet<>();
		ioi_codes.add(ioi_Code);
		verifyThreatscoreIncidets(inputBean.getUser(), expIncidents);

		getAndFeedbackToIncident(inputBean.getUser(), ioi_Code, unusual, clearIncident, clearProfile);

		if (!clearIncident) {
			responseBody = verifyStateIncidents(inputBean.getUser(), expIncidents);
		} else {
			Assert.assertFalse(isIncidentListed(inputBean.getUser(), ioi_Code), "The Expected Incident is NOT cleared");
		}

		if (clearProfile) {
			verifyNoProfile(inputBean.getUser(), ioi_Code);
		} else {
			verifyProfile(inputBean.getUser(), ioi_Code);
		}

		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
	}

	@Test(dataProvider = "dataProvider",  description = "This test operates on real API data, and generates BBI frequent sharing incidents.")
	public void frequent_Sharing_Tests(Method method, String unusual, Boolean clearIncident, Boolean clearProfile) throws Exception {

		AttributeBean attributeBean = new AttributeBean(30, 3, true);

		InputBean inputBean = createFileUpdateDataforfrequent("largeShareTest", attributeBean);

		Log(method, attributeBean,inputBean);
		Reporter.log("Test Parameters :::::::  unusual ::  " + unusual + "  clearIncident :: " + clearIncident + " clearProfile :: " + clearProfile, true);

		HttpResponse resp = getDetectAttributes();
		String responseBody = getResponseBody(resp);
		org.json.JSONArray getResponseArray = new JSONObject(responseBody).getJSONArray(OBJECTS);
		String ioi_Code = "ANOMALOUSLY_FREQUENT_SHARING";
		DetectAttributeDto detectAttributeDto = new DetectAttributeDto();
		updateBBIAttributes(attributeBean.isEnabled(), attributeBean, getResponseArray, detectAttributeDto, ioi_Code);
		Reporter.log("updated preferences ::::::     " + attributeBean.toString(), true);

		user = inputBean.getUser();
		scpActivityLogsAndValidate(method, inputBean);

		HashSet<String> expIncidents = new HashSet<String>();
		expIncidents.add(ioi_Code);

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

		Assert.assertFalse(abortTest, "preference got changed in between test excetuion so skipping test");

		verifyProfile(inputBean.getUser(), ioi_Code);

		responseBody = verifyStateIncidents(inputBean.getUser(), expIncidents);
		Set<String> ioi_codes = new HashSet<>();
		ioi_codes.add(ioi_Code);
		verifyThreatscoreIncidets(inputBean.getUser(), expIncidents);

		getAndFeedbackToIncident(inputBean.getUser(), ioi_Code, unusual, clearIncident, clearProfile);

		if (!clearIncident) {
			responseBody = verifyStateIncidents(inputBean.getUser(), expIncidents);
		} else {
			Assert.assertFalse(isIncidentListed(inputBean.getUser(), ioi_Code), "The Expected Incident is NOT cleared");
		}

		if (clearProfile) {
			verifyNoProfile(inputBean.getUser(), ioi_Code);
		} else {
			verifyProfile(inputBean.getUser(), ioi_Code);
		}

		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
	}


	@Test(dataProvider = "dataProvider", description = "This test operates on real API data, and generates BBI for large sharing incidents.")
	public void large_Sharing_Tests(Method method, String unusual, Boolean clearIncident, Boolean clearProfile) throws Exception {

		AttributeBean attributeBean = new AttributeBean(30, 3, true);

		InputBean inputBean = createFileUpdateDataforLargeBBI("largeShareTest", attributeBean);

		Log(method, attributeBean,inputBean);
		Reporter.log("Test Parameters :::::::  unusual ::  " + unusual + "  clearIncident :: " + clearIncident + " clearProfile :: " + clearProfile, true);

		HttpResponse resp = getDetectAttributes();
		String responseBody = getResponseBody(resp);
		org.json.JSONArray getResponseArray = new JSONObject(responseBody).getJSONArray(OBJECTS);
		String ioi_Code = "ANOMALOUSLY_LARGE_SHARING";
		DetectAttributeDto detectAttributeDto = new DetectAttributeDto();
		updateBBIAttributes(attributeBean.isEnabled(), attributeBean, getResponseArray, detectAttributeDto, ioi_Code);
		Reporter.log("updated preferences ::::::     " + attributeBean.toString(), true);

		user = inputBean.getUser();
		scpActivityLogsAndValidate(method, inputBean);

		HashSet<String> expIncidents = new HashSet<String>();
		expIncidents.add(ioi_Code);

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

		Assert.assertFalse(abortTest, "preference got changed in between test excetuion so skipping test");

		verifyProfile(inputBean.getUser(), ioi_Code);

		responseBody = verifyStateIncidents(inputBean.getUser(), expIncidents);
		Set<String> ioi_codes = new HashSet<>();
		ioi_codes.add(ioi_Code);
		verifyThreatscoreIncidets(inputBean.getUser(), expIncidents);

		getAndFeedbackToIncident(inputBean.getUser(), ioi_Code, unusual, clearIncident, clearProfile);

		if (!clearIncident) {
			responseBody = verifyStateIncidents(inputBean.getUser(), expIncidents);
		} else {
			Assert.assertFalse(isIncidentListed(inputBean.getUser(), ioi_Code), "The Expected Incident is NOT cleared");
		}

		if (clearProfile) {
			verifyNoProfile(inputBean.getUser(), ioi_Code);
		} else {
			verifyProfile(inputBean.getUser(), ioi_Code);
		}

		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
	}



	@Test(dataProvider = "dataProvider", description = "This test operates on real API data, and generates BBI large delets incidents.")
	public void large_Deletes_Tests(Method method, String unusual, Boolean clearIncident, Boolean clearProfile) throws Exception {

		AttributeBean attributeBean = new AttributeBean(30, 3, true);

		InputBean inputBean = createFileUpdateDataforLargeBBI("largeDeletesTest", attributeBean);

		Log(method, attributeBean,inputBean);
		Reporter.log("Test Parameters :::::::  unusual ::  " + unusual + "  clearIncident :: " + clearIncident + " clearProfile :: " + clearProfile, true);

		HttpResponse resp = getDetectAttributes();
		String responseBody = getResponseBody(resp);
		org.json.JSONArray getResponseArray = new JSONObject(responseBody).getJSONArray(OBJECTS);
		String ioi_Code = "ANOMALOUSLY_LARGE_DELETES";
		DetectAttributeDto detectAttributeDto = new DetectAttributeDto();
		updateBBIAttributes(attributeBean.isEnabled(), attributeBean, getResponseArray, detectAttributeDto, ioi_Code);
		Reporter.log("updated preferences ::::::     " + attributeBean.toString(), true);

		user = inputBean.getUser();
		scpActivityLogsAndValidate(method, inputBean);

		HashSet<String> expIncidents = new HashSet<String>();
		expIncidents.add(ioi_Code);

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

		Assert.assertFalse(abortTest, "preference got changed in between test excetuion so skipping test");

		verifyProfile(inputBean.getUser(), ioi_Code);

		responseBody = verifyStateIncidents(inputBean.getUser(), expIncidents);
		Set<String> ioi_codes = new HashSet<>();
		ioi_codes.add(ioi_Code);
		verifyThreatscoreIncidets(inputBean.getUser(), expIncidents);

		getAndFeedbackToIncident(inputBean.getUser(), ioi_Code, unusual, clearIncident, clearProfile);

		if (!clearIncident) {
			responseBody = verifyStateIncidents(inputBean.getUser(), expIncidents);
		} else {
			Assert.assertFalse(isIncidentListed(inputBean.getUser(), ioi_Code), "The Expected Incident is NOT cleared");
		}

		if (clearProfile) {
			verifyNoProfile(inputBean.getUser(), ioi_Code);
		} else {
			verifyProfile(inputBean.getUser(), ioi_Code);
		}

		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
	}



	@Test(dataProvider = "dataProvider",  description = "This test operates on real API data, and generates BBI large upload incidents.")
	public void large_Uploads_Tests(Method method, String unusual, Boolean clearIncident, Boolean clearProfile) throws Exception {

		AttributeBean attributeBean = new AttributeBean(30, 3, true);

		InputBean inputBean = createFileUpdateDataforLargeBBI("largeUploadTest", attributeBean);

		Log(method, attributeBean,inputBean);
		Reporter.log("Test Parameters :::::::  unusual ::  " + unusual + "  clearIncident :: " + clearIncident + " clearProfile :: " + clearProfile, true);

		HttpResponse resp = getDetectAttributes();
		String responseBody = getResponseBody(resp);
		org.json.JSONArray getResponseArray = new JSONObject(responseBody).getJSONArray(OBJECTS);
		String ioi_Code = "ANOMALOUSLY_LARGE_UPLOAD";

		DetectAttributeDto detectAttributeDto = new DetectAttributeDto();
		updateBBIAttributes(attributeBean.isEnabled(), attributeBean, getResponseArray, detectAttributeDto, ioi_Code);
		Reporter.log("updated preferences ::::::     " + attributeBean.toString(), true);

		user = inputBean.getUser();
		scpActivityLogsAndValidate(method, inputBean);

		HashSet<String> expIncidents = new HashSet<String>();
		expIncidents.add(ioi_Code);

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

		Assert.assertFalse(abortTest, "preference got changed in between test excetuion so skipping test");

		verifyProfile(inputBean.getUser(), ioi_Code);

		responseBody = verifyStateIncidents(inputBean.getUser(), expIncidents);
		Set<String> ioi_codes = new HashSet<>();
		ioi_codes.add(ioi_Code);
		verifyThreatscoreIncidets(inputBean.getUser(), expIncidents);

		getAndFeedbackToIncident(inputBean.getUser(), ioi_Code, unusual, clearIncident, clearProfile);

		if (!clearIncident) {
			responseBody = verifyStateIncidents(inputBean.getUser(), expIncidents);
		} else {
			Assert.assertFalse(isIncidentListed(inputBean.getUser(), ioi_Code), "The Expected Incident is NOT cleared");
		}

		if (clearProfile) {
			verifyNoProfile(inputBean.getUser(), ioi_Code);
		} else {
			verifyProfile(inputBean.getUser(), ioi_Code);
		}

		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
	}


	@Test(dataProvider = "dataProvider",  description = "This test operates on real API data, and generates BBI large download incidents.")
	public void large_Downloads_Tests(Method method, String unusual, Boolean clearIncident, Boolean clearProfile) throws Exception {

		AttributeBean attributeBean = new AttributeBean(30, 3, true);

		InputBean inputBean = createFileUpdateDataforLargeBBI("largeDownloadTest", attributeBean);

		Log(method, attributeBean,inputBean);
		Reporter.log("Test Parameters :::::::  unusual ::  " + unusual + "  clearIncident :: " + clearIncident + " clearProfile :: " + clearProfile, true);

		HttpResponse resp = getDetectAttributes();
		String responseBody = getResponseBody(resp);
		org.json.JSONArray getResponseArray = new JSONObject(responseBody).getJSONArray(OBJECTS);
		String ioi_Code = "ANOMALOUSLY_LARGE_DOWNLOAD";

		DetectAttributeDto detectAttributeDto = new DetectAttributeDto();
		updateBBIAttributes(attributeBean.isEnabled(), attributeBean, getResponseArray, detectAttributeDto, ioi_Code);
		Reporter.log("updated preferences ::::::     " + attributeBean.toString(), true);

		user = inputBean.getUser();
		scpActivityLogsAndValidate(method, inputBean);

		HashSet<String> expIncidents = new HashSet<String>();
		expIncidents.add(ioi_Code);

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

		Assert.assertFalse(abortTest, "preference got changed in between test excetuion so skipping test");

		verifyProfile(inputBean.getUser(), ioi_Code);

		responseBody = verifyStateIncidents(inputBean.getUser(), expIncidents);
		Set<String> ioi_codes = new HashSet<>();
		ioi_codes.add(ioi_Code);
		verifyThreatscoreIncidets(inputBean.getUser(), expIncidents);

		getAndFeedbackToIncident(inputBean.getUser(), ioi_Code, unusual, clearIncident, clearProfile);

		if (!clearIncident) {
			responseBody = verifyStateIncidents(inputBean.getUser(), expIncidents);
		} else {
			Assert.assertFalse(isIncidentListed(inputBean.getUser(), ioi_Code), "The Expected Incident is NOT cleared");
		}

		if (clearProfile) {
			verifyNoProfile(inputBean.getUser(), ioi_Code);
		} else {
			verifyProfile(inputBean.getUser(), ioi_Code);
		}

		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);	
	}

	@Test(dataProvider = "dataProvider",  description = "This test operates on real API data, and generates BBI large upload across services incidents.")
	public void large_Upload_Across_Services_Tests(Method method, String unusual, Boolean clearIncident, Boolean clearProfile)
			throws Exception {

		AttributeBean attributeBean = new AttributeBean(30, 3, true);

		InputBean inputBean = createFileUpdateDataforLargeBBI("largeUploadASTest_twoservices", attributeBean);

		Log(method, attributeBean,inputBean);
		Reporter.log("Test Parameters :::::::  unusual ::  " + unusual + "  clearIncident :: " + clearIncident + " clearProfile :: " + clearProfile, true);

		HttpResponse resp = getDetectAttributes();
		String responseBody = getResponseBody(resp);
		org.json.JSONArray getResponseArray = new JSONObject(responseBody).getJSONArray(OBJECTS);
		String ioi_Code = "ANOMALOUSLY_LARGE_UPLOAD_ACROSS_SVC";
		DetectAttributeDto detectAttributeDto = new DetectAttributeDto();
		updateBBIAttributes(attributeBean.isEnabled(), attributeBean, getResponseArray, detectAttributeDto, ioi_Code);
		Reporter.log("updated preferences ::::::     " + attributeBean.toString(), true);

		user = inputBean.getUser();
		scpActivityLogsAndValidate(method, inputBean);

		HashSet<String> expIncidents = new HashSet<String>();
		expIncidents.add(ioi_Code);

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

		Assert.assertFalse(abortTest, "preference got changed in between test excetuion so skipping test");

		verifyProfile(inputBean.getUser(), ioi_Code);

		responseBody = verifyStateIncidents(inputBean.getUser(), expIncidents);
		Set<String> ioi_codes = new HashSet<>();
		ioi_codes.add(ioi_Code);
		verifyThreatscoreIncidets(inputBean.getUser(), expIncidents);

		getAndFeedbackToIncident(inputBean.getUser(), ioi_Code, unusual, clearIncident, clearProfile);

		if (!clearIncident) {
			responseBody = verifyStateIncidents(inputBean.getUser(), expIncidents);
		} else {
			Assert.assertFalse(isIncidentListed(inputBean.getUser(), ioi_Code), "The Expected Incident is NOT cleared");
		}

		if (clearProfile) {
			verifyNoProfile(inputBean.getUser(), ioi_Code);
		} else {
			verifyProfile(inputBean.getUser(), ioi_Code);
		}

		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
	}

	private void Log(Method method, AttributeBean attributeBean, InputBean inputBean) {
		Reporter.log("       #####################################");
		Reporter.log(" ");
		Reporter.log("Execution Started - Test Case Name::::: " + method.getName(), true);
		Reporter.log(" ");
		Reporter.log("This Test is to validate " + method.getName().replace("_Tests", "")+",   And preferences for this test are  preference enabled???:::::: "+attributeBean.isEnabled() +" confidence:: "
				+ ""+attributeBean.getConfidence()+" importance:::: "+attributeBean.getImportance(), true);
		Reporter.log(" ");
		Reporter.log("Test details :::::::  userName::  " + inputBean.getUserName()+"  user::::: "+inputBean.getUser()+" testId:::  "+inputBean.getTestId(), true);
		Reporter.log(" ");
		Reporter.log("To verify manually login to "+suiteData.getEnvironmentName()+"  with user:: "+suiteData.getUsername()+" and pass word for this user is :: "+suiteData.getPassword(),true);
		Reporter.log(" ");
		Reporter.log("        #####################################");
	}

	@AfterMethod
	public void tearDown(ITestResult results) throws Exception {
		System.out.println("Cleaning up Resources");

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
			System.out.println("No Cleanup Required - I am not cleaning up data to debug if needed!");
		}

		System.out.println("Cleaning up Resources - COMPLETED");
	}

}
