package com.elastica.beatle.tests.detect;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpResponse;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.elastica.beatle.detect.dto.AttributeBean;
import com.elastica.beatle.detect.dto.IOI_Code;
import com.elastica.beatle.detect.dto.InputBean;
import com.elastica.beatle.es.ActivityLogs;
import com.elastica.beatle.es.ElasticSearchLogs;

public class DetectThreatScoreTests extends DetectUtils {
	
	private static final String OBJECTS = "detect_attributes";
	
	String user = null;
	String userName =null;
	String randomString = null;
	
	@BeforeTest
	public void beforeTests(){
	 randomString = RandomStringUtils.randomAlphanumeric(12);
	 userName = "detect_" + randomString;
	 user = userName + "@" + suiteData.getTenantDomainName();
	}
	
	
	@Test(priority=1, description = "This test operates on elastic search injected data, and generates  suspicious_AND_Invalid_Logins_Tests incidents.")
	public void suspicious_AND_Invalid_Logins_Tests(Method method) throws Exception {
		Reporter.log("Execution Started - Test Case Name::::: " + method.getName(), true);
		AttributeBean  attributeBean = new AttributeBean(2, 1, 1, true);
		Reporter.log("Detect Attributes::::::::   " + attributeBean.toString(), true);
		
		String responseBody;
		HttpResponse resp = getDetectAttributes();
		 responseBody = getResponseBody(resp);
		org.json.JSONArray getResponseArray = new JSONObject(responseBody).getJSONArray(OBJECTS);
		
		String[] ioi_codes = {IOI_Code.TOO_MANY_SUSPICIOUS_LOGINS.toString(), IOI_Code.TOO_MANY_INVALID_LOGINS.toString()};
		boolean enabled = attributeBean.isEnabled();
		updateDetectAttributes(enabled, attributeBean, getResponseArray, ioi_codes);
		Reporter.log("updated preferences ::::::     " + attributeBean.toString()+"updated prefrences for :::: "+ioi_codes.toString(), true);
		
		String[] fileNames = {"test_login_001","test_login_003"};
		int threatscore0 =0; 
		int threatscore1 =0;
		InputBean inputBean = null;
		for (int i = 0; i < fileNames.length; i++) {
			inputBean = null;
			String tcId =fileNames[i];
			ActivityLogs al = new ActivityLogs();
			String tmplFileName = tcId + ".json";
			String fileName = tcId + "_" + suiteData.getTenantName() + ".json";
			String rString = RandomStringUtils.randomAlphanumeric(12);
			String testId = rString + "_Threat_Tree";
			inputBean = new InputBean(tmplFileName, fileName, userName, user, testId);
			System.out.println("Test Details ###  " + inputBean.toString());
			al.produceActivityLogs(tmplFileName, fileName, suiteData.getTenantName(), user, userName, testId, inputBean,
					attributeBean, suiteData);
			scpActivityLogsAndValidate(method, inputBean);
			if(i==0){
		 threatscore0 = 	verifyDetectedUser(user);
			}if(i==1){
				 threatscore1 = 	verifyDetectedUser(user);
					}
		}
		System.out.println("threatscore1>=threatscore0:::: "+threatscore1+":::::::::::  "+threatscore0);
		Assert.assertTrue(threatscore1>=threatscore0, "threat score not increased");
		HashSet<String> expIncidents = new HashSet<String>();
		Set<String> set = new HashSet<String>(Arrays.asList(ioi_codes));
		expIncidents.addAll(set);
			
				if(user!=null){
				ElasticSearchLogs esLogs = new ElasticSearchLogs();
				 resp =	esLogs.get_es_logs_threatscore_tree(restClient, buildCookieHeaders(), suiteData.getHost(), user);
				 responseBody = getResponseBody(resp);
				Assert.assertEquals(resp.getStatusLine().getStatusCode(), 200, "Response code is not equal");
				Reporter.log("verify_Threat_Tree :::::responseBody:::::  " + responseBody, true);
				validateThreatTree(responseBody, threatscore1, inputBean);
			 
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);	
	 }
	}
	
	
	@Test(priority=2, description = "This test operates on elastic search injected data, and generates  suspicious_AND_Invalid_Logins_Tests incidents.")
	public void ValidateUpDownloadThreatTree(Method method) throws Exception {
		Reporter.log("Execution Started - Test Case Name::::: " + method.getName(), true);
		AttributeBean  attributeBean = new AttributeBean(1, 3, 2, true);
		Reporter.log("Detect Attributes::::::::   " + attributeBean.toString(), true);
		
		
		 
		 
		String responseBody;
		HttpResponse resp = getDetectAttributes();
		 responseBody = getResponseBody(resp);
		org.json.JSONArray getResponseArray = new JSONObject(responseBody).getJSONArray(OBJECTS);
		
		String[] ioi_codes = {IOI_Code.TOO_MANY_SUM_LARGE_DOWNLOADS.toString(), IOI_Code.TOO_MANY_SUM_LARGE_UPLOADS.toString()};
		boolean enabled = attributeBean.isEnabled();
		updateDetectAttributes(enabled, attributeBean, getResponseArray, ioi_codes);
		Reporter.log("updated preferences ::::::     " + attributeBean.toString(), true);
		InputBean inputBean;
			inputBean = null;
			String tcId ="largeUpDownloadTest";
			ActivityLogs al = new ActivityLogs();
			String tmplFileName = tcId + ".json";
			String fileName = tcId + "_" + suiteData.getTenantName() + ".json";
			String rString = RandomStringUtils.randomAlphanumeric(12);
			String testId = rString + "_Threat_Tree";
			inputBean = new InputBean(tmplFileName, fileName, userName, user, testId);
			System.out.println("Test Details ###  " + inputBean.toString());
			al.produceActivityLogs(tmplFileName, fileName, suiteData.getTenantName(), user, userName, testId, inputBean, suiteData);
			scpActivityLogsAndValidate(method, inputBean);
			
		  HashSet<String> expIncidents = new HashSet<String>();
		Set<String> set = new HashSet<String>(Arrays.asList(ioi_codes));
		expIncidents.addAll(set);
		verifyStateIncident(user, expIncidents);
			int threatscore1 = 	verifyDetectedUser(user);
				if(user!=null){
				ElasticSearchLogs esLogs = new ElasticSearchLogs();
				 resp =	esLogs.get_es_logs_threatscore_tree(restClient, buildCookieHeaders(), suiteData.getHost(), user);
				 responseBody = getResponseBody(resp);
				Assert.assertEquals(resp.getStatusLine().getStatusCode(), 200, "Response code is not equal");
				Reporter.log("verify_Threat_Tree :::::responseBody:::::  " + responseBody, true);
				validateThreatTree(responseBody, threatscore1, inputBean);
			 
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);	
	 }
	}
	
	@Test(priority=3, description = "This test operates on elastic search injected data, and generates  TBIBBIThreatTree.")
	public void browsersThreat_Tree(Method method) throws Exception {
		Reporter.log("Execution Started - Test Case Name::::: " + method.getName(), true);
		String tcId ="test_b_01";
		AttributeBean  attributeBean = new AttributeBean(2, 3, 2, true);
		Reporter.log("Detect Attributes::::::::   " + attributeBean.toString(), true);
		 
			String responseBody;
			HttpResponse resp = getDetectAttributes();
			 responseBody = getResponseBody(resp);
			org.json.JSONArray getResponseArray = new JSONObject(responseBody).getJSONArray(OBJECTS);
			
			String[] ioi_codes = {IOI_Code.TOO_MANY_BROWSERS.toString(), IOI_Code.TOO_MANY_DEVICES.toString()};
			boolean enabled = attributeBean.isEnabled();
			updateDetectAttributes(enabled, attributeBean, getResponseArray, ioi_codes);
			Reporter.log("updated preferences ::::::     " + attributeBean.toString(), true);
		
		InputBean inputBean;
			inputBean = null;
		
			ActivityLogs al = new ActivityLogs();
			String tmplFileName = tcId + ".json";
			String fileName = tcId + "_" + suiteData.getTenantName() + ".json";
			String rString = RandomStringUtils.randomAlphanumeric(12);
			String testId = rString + "_Threat_Tree";
			inputBean = new InputBean(tmplFileName, fileName, userName, user, testId);
			System.out.println("Test Details ###  " + inputBean.toString());
			
			al.produceActivityLogs(tmplFileName, fileName, suiteData.getTenantName(), user, userName, testId, inputBean, suiteData);
			
			scpActivityLogsAndValidate(method, inputBean);
			
			Thread.sleep(2 * 60 * 1000);
			 HashSet<String> expIncidents = new HashSet<String>();
				Set<String> set = new HashSet<String>(Arrays.asList(ioi_codes));
				expIncidents.addAll(set);
			verifyStateIncident(user, expIncidents);
			int threatscore1 = 	verifyDetectedUser(user);
				if(user!=null){
				ElasticSearchLogs esLogs = new ElasticSearchLogs();
				 resp =	esLogs.get_es_logs_threatscore_tree(restClient, buildCookieHeaders(), suiteData.getHost(), user);
				 responseBody = getResponseBody(resp);
				Assert.assertEquals(resp.getStatusLine().getStatusCode(), 200, "Response code is not equal");
				Reporter.log("verify_Threat_Tree :::::responseBody:::::  " + responseBody, true);
				validateThreatTree(responseBody, threatscore1, inputBean);
			 
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);	
	 }
	}
	
	@Test(priority=4, description = "This test operates on elastic search injected data, and generates  TBIBBIThreatTree.")
	public void Devices_Threat_Tree(Method method) throws Exception {
		Reporter.log("Execution Started - Test Case Name::::: " + method.getName(), true);
		String tcId ="test_e_1";
		AttributeBean  attributeBean = new AttributeBean(2, 3, 2, true);
		Reporter.log("Detect Attributes::::::::   " + attributeBean.toString(), true);
		 
			String responseBody;
			HttpResponse resp = getDetectAttributes();
			 responseBody = getResponseBody(resp);
			org.json.JSONArray getResponseArray = new JSONObject(responseBody).getJSONArray(OBJECTS);
			
			String[] ioi_codes = {IOI_Code.TOO_MANY_BROWSERS.toString(), IOI_Code.TOO_MANY_DEVICES.toString()};
			boolean enabled = attributeBean.isEnabled();
			updateDetectAttributes(enabled, attributeBean, getResponseArray, ioi_codes);
			Reporter.log("updated preferences ::::::     " + attributeBean.toString(), true);
		
		InputBean inputBean;
			inputBean = null;
		
			ActivityLogs al = new ActivityLogs();
			String tmplFileName = tcId + ".json";
			String fileName = tcId + "_" + suiteData.getTenantName() + ".json";
			String rString = RandomStringUtils.randomAlphanumeric(12);
			String testId = rString + "_Threat_Tree";
			inputBean = new InputBean(tmplFileName, fileName, userName, user, testId);
			System.out.println("Test Details ###  " + inputBean.toString());
			
			al.produceActivityLogs(tmplFileName, fileName, suiteData.getTenantName(), user, userName, testId, inputBean, suiteData);
			
			scpActivityLogsAndValidate(method, inputBean);
			
			Thread.sleep(2 * 60 * 1000);
			 HashSet<String> expIncidents = new HashSet<String>();
				Set<String> set = new HashSet<String>(Arrays.asList(ioi_codes));
				expIncidents.addAll(set);
			int threatscore1 = 	verifyDetectedUser(user);
				if(user!=null){
				ElasticSearchLogs esLogs = new ElasticSearchLogs();
				 resp =	esLogs.get_es_logs_threatscore_tree(restClient, buildCookieHeaders(), suiteData.getHost(), user);
				 responseBody = getResponseBody(resp);
				Assert.assertEquals(resp.getStatusLine().getStatusCode(), 200, "Response code is not equal");
				Reporter.log("verify_Threat_Tree :::::responseBody:::::  " + responseBody, true);
				validateThreatTree(responseBody, threatscore1, inputBean);
			 
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);	
	 }
	}
	
	public void validateThreatTree(String responseBody, int threatscore1, InputBean inputBean) throws JsonProcessingException, IOException{
		 SoftAssert asert=new SoftAssert();
		Assert.assertNotNull(responseBody, "responce body is null");
		asert.assertNotNull(getJSONValue(responseBody, "services"), "services is null");
		asert.assertNotNull(getJSONValue(responseBody, "threat_score"), "threat_score is null");
		String threat_score = getJSONValue(responseBody, "threat_score");
		asert.assertEquals(Integer.parseInt(threat_score), threatscore1, "threat_score is not same as detect user");
		asert.assertNotNull(getJSONValue(responseBody, "user"), "user is null");
		asert.assertEquals(getJSONValue(responseBody, "user").toString().replace("\"", ""), user, "user is not same as detect user");
		asert.assertNotNull(getJSONValue(responseBody, "user_name"), "user_name is null");
		asert.assertEquals(getJSONValue(responseBody, "user_name").toString().replace("\"", ""), userName, "user_name is not same as detect user");
		JsonNode services = new ObjectMapper().readTree(getJSONValue(responseBody, "services"));
		    	JsonNode acrossServices = services.get("Across Services");
		    	//TODO:::Fix ASAP
		    	if (acrossServices != null){/*
			    	String t_Score =	acrossServices.get("threat_score").toString().replace("\"", "");
			    	asert.assertNotNull(t_Score, "t_Score is null in acrossServices");
			    	
			    	JsonNode iois = acrossServices.get("iois");
			    	 Iterator<JsonNode> nodes = iois.getElements();
			    	 while (nodes.hasNext()) {
			    		 JsonNode ioi  = nodes.next();
			    	
			    		String is_from_Seq_Dect = ioi.get("is_from_seq_det").toString();
				    	asert.assertNotNull(is_from_Seq_Dect, "t_Score is null in acrossServices");
				    	asert.assertEquals(is_from_Seq_Dect.replace("\"", ""), "false", "is_from_Seq_Dect shoulb be false");
				    	
				    	String	t_Score1 =	ioi.get("threat_score").toString().replace("\"", "");
				    	asert.assertNotNull(t_Score1, "threat_score is null");
				    	
				    	int threat_Score = 	Integer.parseInt(t_Score1);
				    	asert.assertTrue(Integer.parseInt(t_Score)>=threat_Score, "iois threat score should be less than or equal to saas App threat score");
				    	
				    	String	alert_id =	ioi.get("alert_id").toString();
				    	asert.assertNotNull(alert_id.replace("\"", ""), "threat_score is null");
				    	
				    	String	logs_count =	ioi.get("logs_count").toString();
				    	asert.assertNotNull(logs_count.replace("\"", ""), "logs_count is null");
				    	//asert.assertEquals(Integer.parseInt(logs_count), inputBean.getCount(), "");
				    	
				    	String	message =	ioi.get("message").toString();
				    	asert.assertNotNull(message, "message is null");
			    		
			    	 }
			    	
			    	 
			    	 JsonNode activities = acrossServices.get("activities");
			    	 if (activities!=null) {
						Iterator<JsonNode> activitiesNodes = activities.getElements();
						while (activitiesNodes.hasNext()) {
							JsonNode activity = activitiesNodes.next();
							iois = activity.get("iois");
							nodes = iois.getElements();
							while (nodes.hasNext()) {
								JsonNode ioi = nodes.next();

								validatIOI("Across Services", t_Score, ioi);

							}

						} 
					}
			    	
			    	*/}
		   Set<String> saasApss = 	inputBean.getSaasApps();
		   for (String string : saasApss) {
			   JsonNode saasApp = services.get(string);
			   if (saasApp != null){
			    	String t_Score =	saasApp.get("threat_score").toString().replace("\"", "");
			    	asert.assertNotNull(t_Score, "t_Score is null in "+string);
			    	
			    	JsonNode iois = saasApp.get("iois");
			    	 if (iois!=null) {
			    	 Iterator<JsonNode> nodes = iois.getElements();
			    	 while (nodes.hasNext()) {
			    		 JsonNode ioi  = nodes.next();
			    	
			    		validatIOI(string, t_Score, ioi);
			    		
			    	}
			    	 }
			    	 JsonNode activities = saasApp.get("activities");
			    	 if (activities!=null) {
						Iterator<JsonNode> activitiesNodes = activities.getElements();
						while (activitiesNodes.hasNext()) {
							JsonNode activity = activitiesNodes.next();
							iois = activity.get("iois");
							Iterator<JsonNode>	nodes = iois.getElements();
							while (nodes.hasNext()) {
								JsonNode ioi = nodes.next();

								validatIOI(string, t_Score, ioi);

							}

						} 
					}
			
			   }
		    	
		   }	
		asert.assertAll();
	}


	private void validatIOI(String saasApp, String t_Score, JsonNode ioi) {
		 SoftAssert asert=new SoftAssert();
		String is_from_Seq_Dect = ioi.get("is_from_seq_det").toString();
		asert.assertNotNull(is_from_Seq_Dect, "t_Score is null in "+saasApp);
		asert.assertEquals(is_from_Seq_Dect.replace("\"", ""), "false", "is_from_Seq_Dect shoulb be false");
		
		String	t_Score1 =	ioi.get("threat_score").toString().replace("\"", "");
		System.out.println("threat_score:::::: "+t_Score1);
		asert.assertNotNull(t_Score1, "threat_score is null");
		
		int threat_Score = 	Integer.parseInt(t_Score1);
		asert.assertTrue(Integer.parseInt(t_Score)>=threat_Score, "iois threat score should be less than or equal to saas App threat score");
		
		String	alert_id =	ioi.get("alert_id").toString();
		asert.assertNotNull(alert_id.replace("\"", ""), "threat_score is null");
		
		String	logs_count =	ioi.get("logs_count").toString();
		asert.assertNotNull(logs_count.replace("\"", ""), "logs_count is null");
		//asert.assertEquals(Integer.parseInt(logs_count), inputBean.getCount(), "");
		
		String	message =	ioi.get("message").toString();
		asert.assertNotNull(message.replace("\"", ""), "message is null");
	}
	
/*	@AfterTest
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
	}*/
}
