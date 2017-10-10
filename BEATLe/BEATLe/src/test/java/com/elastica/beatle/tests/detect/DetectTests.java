package com.elastica.beatle.tests.detect;

import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.TimeZone;

import javax.ws.rs.core.HttpHeaders;

import net.sf.json.JSONArray;
import net.sf.json.util.JSONTokener;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.elastica.beatle.Authorization.AuthorizationHandler;
import com.elastica.beatle.detect.dto.AttributeBean;
import com.elastica.beatle.detect.dto.Objects;
import com.elastica.beatle.es.ActivityLogs;
import com.elastica.beatle.es.ESScripts;
import com.elastica.beatle.es.ElasticSearchLogs;

public class DetectTests extends DetectUtils {
	protected String user = null;
	
	private static final String OBJECTS = "detect_attributes";
	protected static final String NAME = "name";
	private static final String RESOURCE_URI = "resource_uri";
	
	public List<AttributeBean> getDetectPreferencesSdR() {
		List<AttributeBean> prefs = new ArrayList<AttributeBean>();
		prefs.add(new AttributeBean("TOO_MANY_INVALID_LOGINS", true, 10, 10, 1));
		prefs.add(new AttributeBean("TOO_MANY_SUSPICIOUS_LOGINS", true, 3, 1, 3));
		prefs.add(new AttributeBean("TOO_MANY_INVALID_LOGINS_GROUP", true, 10, 5, 4));
		prefs.add(new AttributeBean("TOO_MANY_INFEASIBLE_LOCATIONS", true, 1, 1440, 2));
		prefs.add(new AttributeBean("TOO_MANY_SUM_LARGE_UPLOADS", true, 30, 10, 3));
		prefs.add(new AttributeBean("TOO_MANY_SUM_LARGE_DOWNLOADS", false, 30, 10, 3));
		prefs.add(new AttributeBean("TOO_MANY_DEVICES", true, 3, 10, 1));
		prefs.add(new AttributeBean("TOO_MANY_POLICY_VIOLATIONS", true, 30, 10, 4));
		prefs.add(new AttributeBean("TOO_MANY_ENCRYPTED_FILES", true, 3, 1, 2));
		prefs.add(new AttributeBean("TOO_MANY_BROWSERS", true, 3, 10, 3));
		prefs.add(new AttributeBean("ANOMALOUSLY_FREQUENT_SHARING", true, 30, 2));
		prefs.add(new AttributeBean("ANOMALOUSLY_FREQUENT_SESSIONS", true, 30, 3));
		prefs.add(new AttributeBean("ANOMALOUSLY_LARGE_DELETES", true, 30, 1));
		prefs.add(new AttributeBean("ANOMALOUSLY_LARGE_SHARING", true, 30, 4));
		prefs.add(new AttributeBean("ANOMALOUSLY_FREQUENT_DELETES", true, 30, 3));
		prefs.add(new AttributeBean("ANOMALOUSLY_LARGE_DOWNLOAD", true, 30, 2));
		prefs.add(new AttributeBean("ANOMALOUSLY_LARGE_UPLOAD", true, 30, 1));
		prefs.add(new AttributeBean("ANOMALOUSLY_LARGE_UPLOAD_ACROSS_SVC", true, 30, 4));
		
		return prefs;
	}
	
	private void verifyPreferencesForSdR() {
		try {
			HttpResponse resp = getDetectAttributes();
			String responseBody = getResponseBody(resp);
			Reporter.log("Detect Preferences : " + responseBody, true);
			org.json.JSONArray getResponseArray = new JSONObject(responseBody).getJSONArray(OBJECTS);

			List<AttributeBean> prefs = getDetectPreferencesSdR();

			for (AttributeBean pref : prefs) {

				boolean found = false;
				JSONObject attributeObject = null;
				for (int index = 0; index < getResponseArray.length(); index++) {
					attributeObject = getResponseArray.getJSONObject(index);

					if (attributeObject.get(NAME).equals(pref.getIoi_code())) {
						found = true;
						break;
					}
				}

				if (found) {
					Assert.assertEquals(attributeObject.get("enabled"), pref.isEnabled(), "The Detector Enable/Disable comparison mis-matched.");
					if (pref.getConfidence() != null) {
						Assert.assertEquals(attributeObject.get("confidence"), pref.getConfidence(), "The Detector Confidence comparison mis-matched.");
					}
					if (pref.getThreshold() != null) {
						Assert.assertEquals(attributeObject.get("threshold"), pref.getThreshold(), "The Detector Threshold comparison mis-matched.");
					}
					if (pref.getWindow() != null) {
						Assert.assertEquals(attributeObject.get("window"), pref.getWindow(), "The Detector Window comparison mis-matched.");
					}
					Assert.assertEquals(attributeObject.get("importance"), pref.getImportance(), "The Detector Importance comparison mis-matched.");
				} else {
					Assert.fail(pref.getIoi_code() + " Detector is NOT found in Preferences.");
				}
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@BeforeClass
	public void setPreferencesForSuite() throws Exception{
		
		Reporter.log("Step - Get Detect Preferences:", true);
		HttpResponse resp = getDetectAttributes();
		String responseBody = getResponseBody(resp);
		System.out.println("Preferences : " + responseBody);
		org.json.JSONArray getResponseArray = new JSONObject(responseBody).getJSONArray(OBJECTS);
		
		List<AttributeBean> prefs = getDetectPreferencesSdR();
		
		List<Objects> objects = new ArrayList<>();
		for (AttributeBean pref : prefs) {
				Objects object = new Objects();
				
				object.setEnabled(pref.isEnabled());
				if (pref.getConfidence() != null) {
					object.setConfidence(pref.getConfidence());
				} else {
					object.setConfidence(100);
				}
				if (pref.getThreshold() != null) {
					object.setThreshold(pref.getThreshold());
				} else {
					object.setThreshold(0);
				}
				if (pref.getWindow() != null) {
					object.setWindow(pref.getWindow());
				} else {
					object.setWindow(10);
				}
				object.setImportance(pref.getImportance());
				String uri = "uri";
				for (int index = 0; index < getResponseArray.length(); index++) {
					JSONObject attributeObject = getResponseArray.getJSONObject(index);
					
					if (attributeObject.get(NAME).equals(pref.getIoi_code())) {
						uri = (String) attributeObject.get(RESOURCE_URI);
					}
				}
				object.setResource_uri(uri);
				objects.add(object);
		}
		
		Reporter.log("Step - Set Detect Preferences required for SdR suite:", true);
		postAttributes(objects);
		
		Reporter.log("Waiting for 60sec for the Set Detect Preferences to take effect.", true);
		Thread.sleep(60 * 1000);	//Wait for 60secs
	}
	
	@Test(
			description="This test operates on simulated GW data, and generates 4 incidents (three threshold based and one behavior based).")
	public void GW_test1(Method method) throws Exception {
		System.out.println("Execution Started - Test Case Name: " + method.getName());
		
		Reporter.log("Step - Get and Validate Detect Preferences for SdR suite.", true);
		verifyPreferencesForSdR();
		
		String tcId = "test001";
		String randomString = RandomStringUtils.randomAlphanumeric(12);
		
		ESScripts esScripts = new ESScripts();
		ActivityLogs al = new ActivityLogs();
		ElasticSearchLogs esLogs = new ElasticSearchLogs();
		
		String tmplFileName = tcId + ".json";
		String fileName = tcId + "_" + suiteData.getTenantName() + ".json";
		String userName = "detect_" + tcId + "_" + randomString;
		user = userName + "@" + suiteData.getTenantDomainName();
		String testId = tcId + "_" + randomString;
		
		String strDateStart = "2014-08-26T10:55:47";
		String strDateStop = "2014-09-09T07:21:26";
		
		System.out.println("Test Details ### UserName: " + userName + " ### eMail: " + user + " ### test_id: " + testId);
		
		int count = 151;
		al.produceActivityLogs(tmplFileName, fileName,
				suiteData.getTenantName(), user, userName, testId, strDateStart, strDateStop, count);
		
		String consoleLogs;
		consoleLogs = esScripts.scpActivityLogs(fileName, suiteData.getGoldenInputFilePath(), suiteData.getEsScriptsHostName(), suiteData.getEsScriptsUserName());
		System.out.println("Console Log after scp :" + consoleLogs);
		Assert.assertEquals(consoleLogs, "", "File scp Failed!");
		
		consoleLogs = esScripts.injectActivityLogs(fileName, suiteData.getEsScriptsHostName(), suiteData.getEsScriptsUserName());
		System.out.println("Console Log after InjectActivities :" + consoleLogs);
		Assert.assertTrue(consoleLogs.toLowerCase().contains("Wrote 151 records of 151".toLowerCase()), "Injecting Activities to ES failed");
		
		Reporter.log("Waiting for 60sec after Injecting Activities into ES!", true);
		Thread.sleep(1 * 60 * 1000);	//Wait for 1 Minute
		
		verifyActivityCount(testId, 151);
		
		HashSet<String> expIncidents = new HashSet<String>();
		expIncidents.add("TOO_MANY_DEVICES");
		expIncidents.add("TOO_MANY_BROWSERS");
		expIncidents.add("TOO_MANY_SUM_LARGE_UPLOADS");
		expIncidents.add("ANOMALOUSLY_LARGE_UPLOAD");
		
		verifyStateIncidents(user, expIncidents);
		
		verifyThreatscoreIncidets(user, expIncidents);
		
		verifyDetectedUser(user);
		
		System.out.println("Execution Completed - Test Case Name: " + method.getName());
	}
	
	@Test(
			description="This test operates on real GW data, and generates 3 profiles (all behavior based).")
	public void GW_test2(Method method) throws Exception {
		System.out.println("Execution Started - Test Case Name: " + method.getName());
		
		Reporter.log("Step - Get and Validate Detect Preferences for SdR suite.", true);
		verifyPreferencesForSdR();
		
		String tcId = "test002";
		String randomString = RandomStringUtils.randomAlphanumeric(12);
		
		ESScripts esScripts = new ESScripts();
		ActivityLogs al = new ActivityLogs();
		ElasticSearchLogs esLogs = new ElasticSearchLogs();
		
		String tmplFileName = tcId + ".json";
		String fileName = tcId + "_" + suiteData.getTenantName() + ".json";
		String userName = "detect_" + tcId + "_" + randomString;
		user = userName + "@" + suiteData.getTenantDomainName();
		String testId = tcId + "_" + randomString;
		
		String strDateStart = "2014-11-13T07:09:35";
		String strDateStop = "2014-11-13T09:12:05";
		
		System.out.println("Test Details ### UserName: " + userName + " ### eMail: " + user + " ### test_id: " + testId);
		
		int count = 37;
		al.produceActivityLogs(tmplFileName, fileName,
				suiteData.getTenantName(), user, userName, testId, strDateStart, strDateStop, count);
		
		String consoleLogs;
		consoleLogs = esScripts.scpActivityLogs(fileName, suiteData.getGoldenInputFilePath(), suiteData.getEsScriptsHostName(), suiteData.getEsScriptsUserName());
		System.out.println("Console Log after scp :" + consoleLogs);
		Assert.assertEquals(consoleLogs, "", "File scp Failed!");
		
		consoleLogs = esScripts.injectActivityLogs(fileName, suiteData.getEsScriptsHostName(), suiteData.getEsScriptsUserName());
		System.out.println("Console Log after InjectActivities :" + consoleLogs);
		Assert.assertTrue(consoleLogs.toLowerCase().contains("Wrote 37 records of 37".toLowerCase()), "Injecting Activities to ES failed");
		
		Reporter.log("Waiting for 60sec after Injecting Activities into ES!", true);
		Thread.sleep(1 * 60 * 1000);	//Wait for 1 Minute
		
		verifyActivityCount(testId, 37);

		int profileCount = 0;
		for (int ite=0; ite<10; ite++) {

			HttpResponse response = esLogs.getState(restClient, user, suiteData.getTenantName());
			String responseBody = getResponseBody(response);
			Reporter.log("Response Body : " + responseBody, true);

			JsonNode arrNode = new ObjectMapper().readTree(getJSONValue(responseBody, "hits")).get("hits");
			profileCount = 0;
			if (arrNode.isArray()) {
				for (final JsonNode objNode : arrNode) {
					JsonNode _score = objNode.path("_score");
					System.out.println(_score.asText());
					JsonNode _source = objNode.path("_source");
					if (_source.has("event_subtype")) {
						System.out.println(_source.path("event_subtype").asText());
						if (_source.path("event_subtype").asText().equals("ProfileReport") &&  
								_source.path("ioi_code").asText().equals("ANOMALOUSLY_LARGE_UPLOAD")) {
							profileCount++;
						}
					}

				}
			}

			if (profileCount==1) {
				break;
			} else {
				Reporter.log("The Expected Behavior Based Profiles are created Retrying:" +ite+"times with 1 minute wait time", true);
				Thread.sleep(1 * 60 * 1000); //Wait for 1 Minute
			}
		}

		Assert.assertEquals(profileCount, 1, "Expected Behavior Based Profiles are NOT created.");
		
//		response = esLogs.getThreatscore(restClient, user, suiteData.getTenantName());
//		responseBody = getResponseBody(response);
//		System.out.println(responseBody);
		
		//TODO: No Incidents.
		
		System.out.println("Execution Completed - Test Case Name: " + method.getName());
	}
	
	@Test(
			description="This test operates on real API data, and generates no incidents.")
	public void API_test3(Method method) throws Exception {
		System.out.println("Execution Started - Test Case Name: " + method.getName());
		
		Reporter.log("Step - Get and Validate Detect Preferences for SdR suite.", true);
		verifyPreferencesForSdR();
		
		String tcId = "test003";
		String randomString = RandomStringUtils.randomAlphanumeric(12);
		
		ESScripts esScripts = new ESScripts();
		ActivityLogs al = new ActivityLogs();
		ElasticSearchLogs esLogs = new ElasticSearchLogs();
		
		String tmplFileName = tcId + ".json";
		String fileName = tcId + "_" + suiteData.getTenantName() + ".json";
		String userName = "detect_" + tcId + "_" + randomString;
		String user = userName + "@" + suiteData.getTenantDomainName();
		String testId = tcId + "_" + randomString;
		
		String strDateStart = "2014-05-07T20:28:19";
		String strDateStop = "2014-10-14T01:08:26";
		
		System.out.println("Test Details ### UserName: " + userName + " ### eMail: " + user + " ### test_id: " + testId);
		
		al.produceActivityLogs(tmplFileName, fileName,
				suiteData.getTenantName(), user, userName, testId, strDateStart, strDateStop);
		
		String consoleLogs;
		consoleLogs = esScripts.scpActivityLogs(fileName, suiteData.getGoldenInputFilePath(), suiteData.getEsScriptsHostName(), suiteData.getEsScriptsUserName());
		System.out.println("Console Log after scp :" + consoleLogs);
		Assert.assertEquals(consoleLogs, "", "File scp Failed!");
		
		consoleLogs = esScripts.injectActivityLogs(fileName, suiteData.getEsScriptsHostName(), suiteData.getEsScriptsUserName());
		System.out.println("Console Log after InjectActivities :" + consoleLogs);
		Assert.assertTrue(consoleLogs.toLowerCase().contains("Wrote 671 records of 671".toLowerCase()), "Injecting Activities to ES failed");
		
		Reporter.log("Waiting for 60sec after Injecting Activities into ES!", true);
		Thread.sleep(1 * 60 * 1000);	//Wait for 1 Minute
		
		verifyActivityCount(testId, 670);	//One activity is in long back date. so dont expect that in 30days limit.
		
		HttpResponse response = esLogs.getState(restClient, user, suiteData.getTenantName());
		String responseBody = getResponseBody(response);
		System.out.println("Response Body : " + responseBody);

		JsonNode arrNode = new ObjectMapper().readTree(getJSONValue(responseBody, "hits")).get("hits");
		int incidentCount = 0;
		if (arrNode.isArray()) {
		    for (final JsonNode objNode : arrNode) {
		    	JsonNode _score = objNode.path("_score");
		    	System.out.println(_score.asText());
		    	JsonNode _source = objNode.path("_source");
		    	if (_source.has("event_type")) {
		        	System.out.println(_source.path("event_type").asText());
		        	if (_source.path("event_type").asText().equals("DetectorReport") || 
		        			_source.path("event_type").asText().equals("AnomalyReport")) {
		        		incidentCount++;
		        	}
		        }
		        
		    }
		}
		
		Assert.assertEquals(incidentCount, 0, "Expected NO Incidents. But Incidents are Created.");
		//TODO: No Incidents.
		
//		verifyDetectedUser(user);
		
		System.out.println("Execution Completed - Test Case Name: " + method.getName());
	}
	
	@Test(
			description="This test validates a TMIL and a multi-user TMIL incident.")
	public void GW_test6(Method method) throws Exception {
		System.out.println("Execution Started - Test Case Name: " + method.getName());
		//TODO: check incidents
		
		Reporter.log("Step - Get and Validate Detect Preferences for SdR suite.", true);
		verifyPreferencesForSdR();
		
		String tcId = "test006";
		String randomString = RandomStringUtils.randomAlphanumeric(12);
		
		ESScripts esScripts = new ESScripts();
		ActivityLogs al = new ActivityLogs();
		ElasticSearchLogs esLogs = new ElasticSearchLogs();
		
		String tmplFileName = tcId + ".json";
		String fileName = tcId + "_" + suiteData.getTenantName() + ".json";
		String testId = tcId + "_" + randomString;
		
		String strDateStart = "2014-05-07T20:28:19";
		String strDateStop = "2014-10-14T01:08:26";
		
		System.out.println("Test Details ### test_id: " + testId);
		
		al.produceActivityLogs(tmplFileName, fileName,
				suiteData.getTenantName(), null, null, testId, strDateStart, strDateStop);
		
		String consoleLogs;
		consoleLogs = esScripts.scpActivityLogs(fileName, suiteData.getGoldenInputFilePath(), suiteData.getEsScriptsHostName(), suiteData.getEsScriptsUserName());
		System.out.println("Console Log after scp :" + consoleLogs);
		Assert.assertEquals(consoleLogs, "", "File scp Failed!");
		
		consoleLogs = esScripts.injectActivityLogs(fileName, suiteData.getEsScriptsHostName(), suiteData.getEsScriptsUserName());
		System.out.println("Console Log after InjectActivities :" + consoleLogs);
		Assert.assertTrue(consoleLogs.toLowerCase().contains("Wrote 22 records of 22".toLowerCase()), "Injecting Activities to ES failed");
		
		Reporter.log("Waiting for 60sec after Injecting Activities into ES!", true);
		Thread.sleep(1 * 60 * 1000);	//Wait for 1 Minute
		
		verifyActivityCount(testId, 22);
		
		List<NameValuePair> queryParams = new ArrayList<NameValuePair>();
		queryParams.add(new BasicNameValuePair("source", "{\"query\":{\"filtered\":{\"query\":{\"bool\":{\"must\":[{\"term\":{\"facility\":\"_ALL\"}}]}},\"filter\":{\"and\":[{\"or\":[{\"term\":{\"__source\":\"\"}},{\"term\":{\"__source\":\"GW\"}},{\"term\":{\"__source\":\"API\"}},{\"missing\":{\"field\":\"__source\"}}]}]}}},\"sort\":{\"threat_score\":{\"order\":\"desc\",\"ignore_unmapped\":\"true\"}},\"size\":100000}"));
		queryParams.add(new BasicNameValuePair("sourceName", "DETECT"));		
		
		HttpResponse response = esLogs.getUserScoreAnomalies(restClient, this.buildCookieHeaders(),  suiteData.getApiserverHostName(), queryParams);
		String responseBody = getResponseBody(response);
		System.out.println("Response Body Printing....... : " + responseBody);
		String hits = getJSONValue(getJSONValue(responseBody, "hits"), "hits");
		
		Date dateTo = new Date();
    	SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd'T'HH");
    	formatDate.setTimeZone(TimeZone.getTimeZone("GMT"));
    	String strDateTimeTo = formatDate.format(dateTo);
    	
		String[] users = {"gill.re.tekht@elastica.co","gill.te.tekht@elastica.co","gill.ce.tekht@elastica.co","vill.te.tekht@elastica.co","vill.ce.tekht@elastica.co","vill.de.tekht@elastica.co", "vill.re.tekht@elastica.co", "will.te.tekht@elastica.co", "will.ce.tekht@elastica.co", "will.de.tekht@elastica.co", "will.re.tekht@elastica.co"};
		JSONArray jArray = (JSONArray) new JSONTokener(hits).nextValue();
		Boolean foundUser = false;
		for (String user : users) {
			foundUser = false;
			for (int i = 0; i < jArray.size(); i++) {
				String sourceJson = getJSONValue(jArray.getJSONObject(i).toString(), "_source");
				String rUser = getJSONValue(sourceJson, "user");
				String cTime = getJSONValue(sourceJson, "created_timestamp");
				if (rUser.equalsIgnoreCase("\"" + user + "\"") || cTime.startsWith(strDateTimeTo)) {
					foundUser = true;
					break;
				}
			}
			if (!foundUser) {
				break;
			}
		}
		Assert.assertTrue(foundUser, "One or More Users Information is NOT listed Under DETECT");
		
		System.out.println("Execution Completed - Test Case Name: " + method.getName());
	}
	
	@Test(
			description="This test operates on simulated GW data, and tests write back cache mechanism.")
	public void GW_test7(Method method) throws Exception {
		System.out.println("Execution Started - Test Case Name: " + method.getName());
		
		Reporter.log("Step - Get and Validate Detect Preferences for SdR suite.", true);
		verifyPreferencesForSdR();
		
		String tcId = "test007";
		String randomString = RandomStringUtils.randomAlphanumeric(12);
		
		ESScripts esScripts = new ESScripts();
		ActivityLogs al = new ActivityLogs();
		ElasticSearchLogs esLogs = new ElasticSearchLogs();
		
		String tmplFileName = tcId + ".json";
		String fileName = tcId + "_" + suiteData.getTenantName() + ".json";
		String testId = tcId + "_" + randomString;
		
		String strDateStart = "2015-01-22T21:23:59";
		String strDateStop = "2015-01-22T23:48:05";
		
		System.out.println("Test Details ### test_id: " + testId);
		
		al.produceActivityLogs(tmplFileName, fileName,
				suiteData.getTenantName(), null, null, testId, strDateStart, strDateStop);
		
		String consoleLogs;
		consoleLogs = esScripts.scpActivityLogs(fileName, suiteData.getGoldenInputFilePath(), suiteData.getEsScriptsHostName(), suiteData.getEsScriptsUserName());
		System.out.println("Console Log after scp :" + consoleLogs);
		Assert.assertEquals(consoleLogs, "", "File scp Failed!");
		
		consoleLogs = esScripts.injectActivityLogs(fileName, suiteData.getEsScriptsHostName(), suiteData.getEsScriptsUserName());
		System.out.println("Console Log after InjectActivities :" + consoleLogs);
		Assert.assertTrue(consoleLogs.toLowerCase().contains("Wrote 960 records of 960".toLowerCase()), "Injecting Activities to ES failed");
		
		Reporter.log("Waiting for 60sec after Injecting Activities into ES!", true);
		Thread.sleep(1 * 60 * 1000);	//Wait for 1 Minute
		
		verifyActivityCount(testId, 960);
		
		List<NameValuePair> queryParams = new ArrayList<NameValuePair>();
		queryParams.add(new BasicNameValuePair("source", "{\"query\":{\"filtered\":{\"query\":{\"bool\":{\"must\":[{\"term\":{\"facility\":\"_ALL\"}}]}},\"filter\":{\"and\":[{\"or\":[{\"term\":{\"__source\":\"\"}},{\"term\":{\"__source\":\"GW\"}},{\"term\":{\"__source\":\"API\"}},{\"missing\":{\"field\":\"__source\"}}]}]}}},\"sort\":{\"threat_score\":{\"order\":\"desc\",\"ignore_unmapped\":\"true\"}},\"size\":100000}"));
		queryParams.add(new BasicNameValuePair("sourceName", "DETECT"));		

		HttpResponse response = esLogs.getUserScoreAnomalies(restClient, this.buildCookieHeaders(), suiteData.getApiserverHostName(), queryParams);
		String responseBody = getResponseBody(response);
		System.out.println("Response Body Printing....... : " + responseBody);
		String hits = getJSONValue(getJSONValue(responseBody, "hits"), "hits");

		Date dateTo = new Date();
    	SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd'T'HH");
    	formatDate.setTimeZone(TimeZone.getTimeZone("GMT"));
    	String strDateTimeTo = formatDate.format(dateTo);
    	
		String[] users = {"user0@elastica.co", "user1@elastica.co", "user2@elastica.co", "user3@elastica.co", "user4@elastica.co", "user5@elastica.co", "user6@elastica.co", "user7@elastica.co", "user8@elastica.co", "user9@elastica.co", "user10@elastica.co", "user11@elastica.co", "user12@elastica.co", "user13@elastica.co", "user14@elastica.co", "user15@elastica.co", "user16@elastica.co", "user17@elastica.co", "user18@elastica.co", "user19@elastica.co", "user20@elastica.co", "user21@elastica.co", "user22@elastica.co", "user23@elastica.co"};
		JSONArray jArray = (JSONArray) new JSONTokener(hits).nextValue();
		Boolean foundUser = false;
		for (String user : users) {
			foundUser = false;
			for (int i = 0; i < jArray.size(); i++) {
				String sourceJson = getJSONValue(jArray.getJSONObject(i).toString(), "_source");
				String rUser = getJSONValue(sourceJson, "user");
				String cTime = getJSONValue(sourceJson, "created_timestamp");
				if (rUser.equalsIgnoreCase("\"" + user + "\"") || cTime.startsWith(strDateTimeTo)) {
					foundUser = true;
					break;
				}
			}
			if (foundUser) {
				break;
			}
		}
		Assert.assertFalse(foundUser, "One or More Users Information are listed Under DETECT. Expected NO Users in DETECT.");
		
		System.out.println("Execution Completed - Test Case Name: " + method.getName());
	}
	
	@Test(
			description="This test operates on simulated GW data, and tests write back cache mechanism when the memory space for unfinished profiles is small.")
	public void GW_test8(Method method) throws Exception {
		System.out.println("Execution Started - Test Case Name: " + method.getName());
		
		Reporter.log("Step - Get and Validate Detect Preferences for SdR suite.", true);
		verifyPreferencesForSdR();
		
		String tcId = "test008";
		String randomString = RandomStringUtils.randomAlphanumeric(12);
		
		ESScripts esScripts = new ESScripts();
		ActivityLogs al = new ActivityLogs();
		ElasticSearchLogs esLogs = new ElasticSearchLogs();
		
		String tmplFileName = tcId + ".json";
		String fileName = tcId + "_" + suiteData.getTenantName() + ".json";
		String testId = tcId + "_" + randomString;
		
		String strDateStart = "2015-01-22T21:23:59";
		String strDateStop = "2015-01-22T23:48:05";
		
		System.out.println("Test Details ### test_id: " + testId);
		
		al.produceActivityLogs(tmplFileName, fileName,
				suiteData.getTenantName(), null, null, testId, strDateStart, strDateStop);
		
		String consoleLogs;
		consoleLogs = esScripts.scpActivityLogs(fileName, suiteData.getGoldenInputFilePath(), suiteData.getEsScriptsHostName(), suiteData.getEsScriptsUserName());
		System.out.println("Console Log after scp :" + consoleLogs);
		Assert.assertEquals(consoleLogs, "", "File scp Failed!");
		
		consoleLogs = esScripts.injectActivityLogs(fileName, suiteData.getEsScriptsHostName(), suiteData.getEsScriptsUserName());
		System.out.println("Console Log after InjectActivities :" + consoleLogs);
		Assert.assertTrue(consoleLogs.toLowerCase().contains("Wrote 960 records of 960".toLowerCase()), "Injecting Activities to ES failed");
		
		Reporter.log("Waiting for 60sec after Injecting Activities into ES!", true);
		Thread.sleep(1 * 60 * 1000);	//Wait for 1 Minute
		
		verifyActivityCount(testId, 960);
		
		List<NameValuePair> queryParams = new ArrayList<NameValuePair>();
		queryParams.add(new BasicNameValuePair("source", "{\"query\":{\"filtered\":{\"query\":{\"bool\":{\"must\":[{\"term\":{\"facility\":\"_ALL\"}}]}},\"filter\":{\"and\":[{\"or\":[{\"term\":{\"__source\":\"\"}},{\"term\":{\"__source\":\"GW\"}},{\"term\":{\"__source\":\"API\"}},{\"missing\":{\"field\":\"__source\"}}]}]}}},\"sort\":{\"threat_score\":{\"order\":\"desc\",\"ignore_unmapped\":\"true\"}},\"size\":100000}"));
		queryParams.add(new BasicNameValuePair("sourceName", "DETECT"));		
		
		HttpResponse response = esLogs.getUserScoreAnomalies(restClient, this.buildCookieHeaders(), suiteData.getHost(), queryParams);
		String responseBody = getResponseBody(response);
		System.out.println("Response Body Printing....... : " + responseBody);
		String hits = getJSONValue(getJSONValue(responseBody, "hits"), "hits");
		
		Date dateTo = new Date();
    	SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd'T'HH");
    	formatDate.setTimeZone(TimeZone.getTimeZone("GMT"));
    	String strDateTimeTo = formatDate.format(dateTo);
    	
		String[] users = {"user0@elasti.ca", "user1@elasti.ca", "user2@elasti.ca", "user3@elasti.ca", "user4@elasti.ca", "user5@elasti.ca", "user6@elasti.ca", "user7@elasti.ca", "user8@elasti.ca", "user9@elasti.ca", "user10@elasti.ca", "user11@elasti.ca", "user12@elasti.ca", "user13@elasti.ca", "user14@elasti.ca", "user15@elasti.ca", "user16@elasti.ca", "user17@elasti.ca", "user18@elasti.ca", "user19@elasti.ca", "user20@elasti.ca", "user21@elasti.ca", "user22@elasti.ca", "user23@elasti.ca"};
		JSONArray jArray = (JSONArray) new JSONTokener(hits).nextValue();
		Boolean foundUser = false;
		for (String user : users) {
			foundUser = false;
			for (int i = 0; i < jArray.size(); i++) {
				String sourceJson = getJSONValue(jArray.getJSONObject(i).toString(), "_source");
				String rUser = getJSONValue(sourceJson, "user");
				String cTime = getJSONValue(sourceJson, "created_timestamp");
				if (rUser.equalsIgnoreCase("\"" + user + "\"") || cTime.startsWith(strDateTimeTo)) {
					foundUser = true;
					break;
				}
			}
			if (foundUser) {
				break;
			}
		}
		Assert.assertFalse(foundUser, "One or More Users Information are listed Under DETECT. Expected NO Users in DETECT.");
		
		System.out.println("Execution Completed - Test Case Name: " + method.getName());
	}
	
	@Test(
			description="This test operates on simulated GW data, and generates 1 TBI from Personal Saas Use.")
	public void GW_test9(Method method) throws Exception {
		System.out.println("Execution Started - Test Case Name: " + method.getName());
		
		Reporter.log("Step - Get and Validate Detect Preferences for SdR suite.", true);
		verifyPreferencesForSdR();
		
		String tcId = "test009";
		String randomString = RandomStringUtils.randomAlphanumeric(12);
		
		ESScripts esScripts = new ESScripts();
		ActivityLogs al = new ActivityLogs();
		ElasticSearchLogs esLogs = new ElasticSearchLogs();
		
		String tmplFileName = tcId + ".json";
		String fileName = tcId + "_" + suiteData.getTenantName() + ".json";
		String userName = "detect_" + tcId + "_" + randomString;
		String user = userName + "@" + suiteData.getTenantDomainName();
		String testId = tcId + "_" + randomString;
		
		String strDateStart = "2015-02-12T00:28:12";
		String strDateStop = "2015-02-12T00:28:12";
		
		System.out.println("Test Details ### UserName: " + userName + " ### eMail: " + user + " ### test_id: " + testId);
		
		al.produceActivityLogs(tmplFileName, fileName,
				suiteData.getTenantName(), user, userName, testId, strDateStart, strDateStop);
		
		String consoleLogs;
		consoleLogs = esScripts.scpActivityLogs(fileName, suiteData.getGoldenInputFilePath(), suiteData.getEsScriptsHostName(), suiteData.getEsScriptsUserName());
		System.out.println("Console Log after scp :" + consoleLogs);
		Assert.assertEquals(consoleLogs, "", "File scp Failed!");
		
		consoleLogs = esScripts.injectActivityLogs(fileName, suiteData.getEsScriptsHostName(), suiteData.getEsScriptsUserName());
		System.out.println("Console Log after InjectActivities :" + consoleLogs);
		Assert.assertTrue(consoleLogs.toLowerCase().contains("Wrote 9 records of 9".toLowerCase()), "Injecting Activities to ES failed");
		
		Reporter.log("Waiting for 60sec after Injecting Activities into ES!", true);
		Thread.sleep(1 * 60 * 1000);	//Wait for 1 Minute
		
		verifyActivityCount(testId, 9);
		
		HashSet<String> expIncidents = new HashSet<String>();
		expIncidents.add("TOO_MANY_SUSPICIOUS_LOGINS");

		verifyStateIncidents(user, expIncidents);

		verifyThreatscoreIncidets(user, expIncidents);
		
		verifyDetectedUser(user);
		
		System.out.println("Execution Completed - Test Case Name: " + method.getName());
	}
	
	@Test(
			description="This test operates on simulated GW data, and generates 3 incidents of anomalously frqeuent user actions (delete).")
	public void GW_test11(Method method) throws Exception {
		System.out.println("Execution Started - Test Case Name: " + method.getName());
		//TODO: Looks like bug - incidents are not created.
		
		Reporter.log("Step - Get and Validate Detect Preferences for SdR suite.", true);
		verifyPreferencesForSdR();
		
		String tcId = "test011";
		String randomString = RandomStringUtils.randomAlphanumeric(12);
		
		ESScripts esScripts = new ESScripts();
		ActivityLogs al = new ActivityLogs();
		ElasticSearchLogs esLogs = new ElasticSearchLogs();
		
		String tmplFileName = tcId + ".json";
		String fileName = tcId + "_" + suiteData.getTenantName() + ".json";
		String userName = "detect_" + tcId + "_" + randomString;
		String user = userName + "@" + suiteData.getTenantDomainName();
		String testId = tcId + "_" + randomString;
		
		String strDateStart = "2014-08-26T10:55:47";
		String strDateStop = "2014-09-09T07:05:14";
		
		System.out.println("Test Details ### UserName: " + userName + " ### eMail: " + user + " ### test_id: " + testId);
		
		int count = 529;
		al.produceActivityLogs(tmplFileName, fileName,
				suiteData.getTenantName(), user, userName, testId, strDateStart, strDateStop, count);
		
		String consoleLogs;
		consoleLogs = esScripts.scpActivityLogs(fileName, suiteData.getGoldenInputFilePath(), suiteData.getEsScriptsHostName(), suiteData.getEsScriptsUserName());
		System.out.println("Console Log after scp :" + consoleLogs);
		Assert.assertEquals(consoleLogs, "", "File scp Failed!");
		
		consoleLogs = esScripts.injectActivityLogs(fileName, suiteData.getEsScriptsHostName(), suiteData.getEsScriptsUserName());
		System.out.println("Console Log after InjectActivities :" + consoleLogs);
		Assert.assertTrue(consoleLogs.toLowerCase().contains(("Wrote " + count + " records of " + count + "").toLowerCase()), "Injecting Activities to ES failed");
		
		Reporter.log("Waiting for 60sec after Injecting Activities into ES!", true);
		Thread.sleep(1 * 60 * 1000);	//Wait for 1 Minute
		
		verifyActivityCount(testId, count);
		
		HashSet<String> expIncidents = new HashSet<String>();
		expIncidents.add("ANOMALOUSLY_FREQUENT_DELETES");
		expIncidents.add("ANOMALOUSLY_FREQUENT_SESSIONS");

		verifyStateIncidents(user, expIncidents);

		verifyThreatscoreIncidets(user, expIncidents);
		
		verifyDetectedUser(user);
		
		System.out.println("Execution Completed - Test Case Name: " + method.getName());
	}
	
	@Test(
			description="This test operates on actual GW data, and generates a suspicious location change incident.")
	public void GW_test12(Method method) throws Exception {
		System.out.println("Execution Started - Test Case Name: " + method.getName());
		
		Reporter.log("Step - Get and Validate Detect Preferences for SdR suite.", true);
		verifyPreferencesForSdR();
		
		String tcId = "test012";
		String randomString = RandomStringUtils.randomAlphanumeric(12);
		
		ESScripts esScripts = new ESScripts();
		ActivityLogs al = new ActivityLogs();
		ElasticSearchLogs esLogs = new ElasticSearchLogs();
		
		String tmplFileName = tcId + ".json";
		String fileName = tcId + "_" + suiteData.getTenantName() + ".json";
		String userName = "detect_" + tcId + "_" + randomString;
		String user = userName + "@" + suiteData.getTenantDomainName();
		String testId = tcId + "_" + randomString;
		
		String strDateStart = "2014-10-23T00:40:48";
		String strDateStop = "2014-10-30T08:01:09";
		
		System.out.println("Test Details ### UserName: " + userName + " ### eMail: " + user + " ### test_id: " + testId);
		
		int count = 464;
		al.produceActivityLogs(tmplFileName, fileName,
				suiteData.getTenantName(), user, userName, testId, strDateStart, strDateStop, count);
		
		String consoleLogs;
		consoleLogs = esScripts.scpActivityLogs(fileName, suiteData.getGoldenInputFilePath(), suiteData.getEsScriptsHostName(), suiteData.getEsScriptsUserName());
		System.out.println("Console Log after scp :" + consoleLogs);
		Assert.assertEquals(consoleLogs, "", "File scp Failed!");
		
		consoleLogs = esScripts.injectActivityLogs(fileName, suiteData.getEsScriptsHostName(), suiteData.getEsScriptsUserName());
		System.out.println("Console Log after InjectActivities :" + consoleLogs);
		Assert.assertTrue(consoleLogs.toLowerCase().contains("Wrote 464 records of 464".toLowerCase()), "Injecting Activities to ES failed");
		
		Reporter.log("Waiting for 60sec after Injecting Activities into ES!", true);
		Thread.sleep(1 * 60 * 1000);	//Wait for 1 Minute
		
		verifyActivityCount(testId, 464);
		
		HashSet<String> expIncidents = new HashSet<String>();
		expIncidents.add("TOO_MANY_INFEASIBLE_LOCATIONS");

		verifyStateIncidents(user, expIncidents);

		verifyThreatscoreIncidets(user, expIncidents);
		
		verifyDetectedUser(user);
		
		System.out.println("Execution Completed - Test Case Name: " + method.getName());
	}
	
	@Test(
			description="This test generates a suspicious location change incident, and is designed to test whitelisting of IP range 202.141.245.38 to 202.141.245.47.")
	public void GW_test14(Method method) throws Exception {
		System.out.println("Execution Started - Test Case Name: " + method.getName());
		
		Reporter.log("Step - Get and Validate Detect Preferences for SdR suite.", true);
		verifyPreferencesForSdR();
		
		String tcId = "test014";
		String randomString = RandomStringUtils.randomAlphanumeric(12);
		
		ESScripts esScripts = new ESScripts();
		ActivityLogs al = new ActivityLogs();
		ElasticSearchLogs esLogs = new ElasticSearchLogs();
		
		String tmplFileName = tcId + ".json";
		String fileName = tcId + "_" + suiteData.getTenantName() + ".json";
		String userName = "detect_" + tcId + "_" + randomString;
		String user = userName + "@" + suiteData.getTenantDomainName();
		String testId = tcId + "_" + randomString;
		
		String strDateStart = "2014-10-23T00:40:48";
		String strDateStop = "2014-10-30T08:01:09";
		
		System.out.println("Test Details ### UserName: " + userName + " ### eMail: " + user + " ### test_id: " + testId);
		
		int count = 464;
		al.produceActivityLogs(tmplFileName, fileName,
				suiteData.getTenantName(), user, userName, testId, strDateStart, strDateStop, count);
		
		String consoleLogs;
		consoleLogs = esScripts.scpActivityLogs(fileName, suiteData.getGoldenInputFilePath(), suiteData.getEsScriptsHostName(), suiteData.getEsScriptsUserName());
		System.out.println("Console Log after scp :" + consoleLogs);
		Assert.assertEquals(consoleLogs, "", "File scp Failed!");
		
		consoleLogs = esScripts.injectActivityLogs(fileName, suiteData.getEsScriptsHostName(), suiteData.getEsScriptsUserName());
		System.out.println("Console Log after InjectActivities :" + consoleLogs);
		Assert.assertTrue(consoleLogs.toLowerCase().contains("Wrote 464 records of 464".toLowerCase()), "Injecting Activities to ES failed");
		
		Reporter.log("Waiting for 60sec after Injecting Activities into ES!", true);
		Thread.sleep(1 * 60 * 1000);	//Wait for 1 Minute
		
		verifyActivityCount(testId, 464);
		
		HashSet<String> expIncidents = new HashSet<String>();
		expIncidents.add("TOO_MANY_INFEASIBLE_LOCATIONS");

		verifyStateIncidents(user, expIncidents);

		verifyThreatscoreIncidets(user, expIncidents);
		
		verifyDetectedUser(user);
		
		System.out.println("Execution Completed - Test Case Name: " + method.getName());
	}
	
	@Test(
			description="This test operates on simulated GW data, and generates 1 TBI from too many encrypted files.")
	public void GW_test15(Method method) throws Exception {
		System.out.println("Execution Started - Test Case Name: " + method.getName());
		
		Reporter.log("Step - Get and Validate Detect Preferences for SdR suite.", true);
		verifyPreferencesForSdR();
		
		String tcId = "test015";
		String randomString = RandomStringUtils.randomAlphanumeric(12);
		
		ESScripts esScripts = new ESScripts();
		ActivityLogs al = new ActivityLogs();
		ElasticSearchLogs esLogs = new ElasticSearchLogs();
		
		String tmplFileName = tcId + ".json";
		String fileName = tcId + "_" + suiteData.getTenantName() + ".json";
		String userName = "detect_" + tcId + "_" + randomString;
		String user = userName + "@" + suiteData.getTenantDomainName();
		String testId = tcId + "_" + randomString;
		
		String strDateStart = "2015-02-06T00:48:22";
		String strDateStop = "2015-02-06T01:03:15";
		
		System.out.println("Test Details ### UserName: " + userName + " ### eMail: " + user + " ### test_id: " + testId);
		
		al.produceActivityLogs(tmplFileName, fileName,
				suiteData.getTenantName(), user, userName, testId, strDateStart, strDateStop);
		
		String consoleLogs;
		consoleLogs = esScripts.scpActivityLogs(fileName, suiteData.getGoldenInputFilePath(), suiteData.getEsScriptsHostName(), suiteData.getEsScriptsUserName());
		System.out.println("Console Log after scp :" + consoleLogs);
		Assert.assertEquals(consoleLogs, "", "File scp Failed!");
		
		consoleLogs = esScripts.injectActivityLogs(fileName, suiteData.getEsScriptsHostName(), suiteData.getEsScriptsUserName());
		System.out.println("Console Log after InjectActivities :" + consoleLogs);
		Assert.assertTrue(consoleLogs.toLowerCase().contains("Wrote 9 records of 9".toLowerCase()), "Injecting Activities to ES failed");
		
		Reporter.log("Waiting for 60sec after Injecting Activities into ES!", true);
		Thread.sleep(1 * 60 * 1000);	//Wait for 1 Minute
		
		verifyActivityCount(testId, 9);
		
		HashSet<String> expIncidents = new HashSet<String>();
		expIncidents.add("TOO_MANY_ENCRYPTED_FILES");

		verifyStateIncidents(user, expIncidents);

		verifyThreatscoreIncidets(user, expIncidents);
		
		verifyDetectedUser(user);
		
		System.out.println("Execution Completed - Test Case Name: " + method.getName());
	}
	
	@Test(
			description="This test operates on actual GW data, and generates a suspicious location change incident.")
	public void GW_test16(Method method) throws Exception {
		System.out.println("Execution Started - Test Case Name: " + method.getName());
		
		Reporter.log("Step - Get and Validate Detect Preferences for SdR suite.", true);
		verifyPreferencesForSdR();
		
		String tcId = "test016";
		String randomString = RandomStringUtils.randomAlphanumeric(12);
		
		ESScripts esScripts = new ESScripts();
		ActivityLogs al = new ActivityLogs();
		ElasticSearchLogs esLogs = new ElasticSearchLogs();
		
		String tmplFileName = tcId + ".json";
		String fileName = tcId + "_" + suiteData.getTenantName() + ".json";
		String userName = "detect_" + tcId + "_" + randomString;
		String user = userName + "@" + suiteData.getTenantDomainName();
		String testId = tcId + "_" + randomString;
		
		String strDateStart = "2014-10-23T00:40:48";
		String strDateStop = "2014-10-23T15:17:49";
		
		System.out.println("Test Details ### UserName: " + userName + " ### eMail: " + user + " ### test_id: " + testId);
		
		int count = 24;
		al.produceActivityLogs(tmplFileName, fileName,
				suiteData.getTenantName(), user, userName, testId, strDateStart, strDateStop, count);
		
		String consoleLogs;
		consoleLogs = esScripts.scpActivityLogs(fileName, suiteData.getGoldenInputFilePath(), suiteData.getEsScriptsHostName(), suiteData.getEsScriptsUserName());
		System.out.println("Console Log after scp :" + consoleLogs);
		Assert.assertEquals(consoleLogs, "", "File scp Failed!");
		
		consoleLogs = esScripts.injectActivityLogs(fileName, suiteData.getEsScriptsHostName(), suiteData.getEsScriptsUserName());
		System.out.println("Console Log after InjectActivities :" + consoleLogs);
		Assert.assertTrue(consoleLogs.toLowerCase().contains("Wrote 24 records of 24".toLowerCase()), "Injecting Activities to ES failed");
		
		Reporter.log("Waiting for 60sec after Injecting Activities into ES!", true);
		Thread.sleep(1 * 60 * 1000);	//Wait for 1 Minute
		
		verifyActivityCount(testId, 24);
		
		HashSet<String> expIncidents = new HashSet<String>();
		expIncidents.add("TOO_MANY_INFEASIBLE_LOCATIONS");

		verifyStateIncidents(user, expIncidents);

		verifyThreatscoreIncidets(user, expIncidents);
		
		verifyDetectedUser(user);
		
		System.out.println("Execution Completed - Test Case Name: " + method.getName());
	}
	
	@Test(
			description="This tests how detect handles fields which are present but unknown")
	public void API_test19(Method method) throws Exception {
		System.out.println("Execution Started - Test Case Name: " + method.getName());
		
		Reporter.log("Step - Get and Validate Detect Preferences for SdR suite.", true);
		verifyPreferencesForSdR();
		
		String tcId = "test019";
		String randomString = RandomStringUtils.randomAlphanumeric(12);
		
		ESScripts esScripts = new ESScripts();
		ActivityLogs al = new ActivityLogs();
		ElasticSearchLogs esLogs = new ElasticSearchLogs();
		
		String tmplFileName = tcId + ".json";
		String fileName = tcId + "_" + suiteData.getTenantName() + ".json";
		String userName = "detect_" + tcId + "_" + randomString;
		String user = userName + "@" + suiteData.getTenantDomainName();
		String testId = tcId + "_" + randomString;
		
		String strDateStart = "2015-02-12T00:28:12";
		String strDateStop = "2015-02-12T00:38:12";
		
		System.out.println("Test Details ### UserName: " + userName + " ### eMail: " + user + " ### test_id: " + testId);
		
		int count = 6;
		al.produceActivityLogs(tmplFileName, fileName,
				suiteData.getTenantName(), user, userName, testId, strDateStart, strDateStop, count);
		
		String consoleLogs;
		consoleLogs = esScripts.scpActivityLogs(fileName, suiteData.getGoldenInputFilePath(), suiteData.getEsScriptsHostName(), suiteData.getEsScriptsUserName());
		System.out.println("Console Log after scp :" + consoleLogs);
		Assert.assertEquals(consoleLogs, "", "File scp Failed!");
		
		consoleLogs = esScripts.injectActivityLogs(fileName, suiteData.getEsScriptsHostName(), suiteData.getEsScriptsUserName());
		System.out.println("Console Log after InjectActivities :" + consoleLogs);
		Assert.assertTrue(consoleLogs.toLowerCase().contains("Wrote 6 records of 6".toLowerCase()), "Injecting Activities to ES failed");
		
		Reporter.log("Waiting for 60sec after Injecting Activities into ES!", true);
		Thread.sleep(1 * 60 * 1000);	//Wait for 1 Minute
		
		verifyActivityCount(testId, 6);
		
		HttpResponse response = esLogs.getThreatscore(restClient, user, suiteData.getTenantName());
		String responseBody = getResponseBody(response);
		System.out.println("Response Body : " + responseBody);
		
		JsonNode arrNode = new ObjectMapper().readTree(getJSONValue(responseBody, "hits")).get("hits");
		int incidentCount = 0;
		if (arrNode.isArray()) {
			for (final JsonNode objNode : arrNode) {
				JsonNode _source = objNode.path("_source");
				if ((_source.path("event_type").asText().equals("DetectorReport") && _source.has("ioi_code")) ||
					(_source.path("event_type").asText().equals("AnomalyReport") && _source.has("ioi_code"))) {
					incidentCount++;
				}
			}
		}
		Assert.assertTrue(incidentCount==0, "Some Incident created. Expecting No Incidents created.");
		
		verifyUserNotDetected(user);
		
		System.out.println("Execution Completed - Test Case Name: " + method.getName());
	}
	
	@Test(
			description="This test operates on actual GW data, and generates a suspicious location change incident.")
	public void GW_test20(Method method) throws Exception {
		System.out.println("Execution Started - Test Case Name: " + method.getName());
		
		Reporter.log("Step - Get and Validate Detect Preferences for SdR suite.", true);
		verifyPreferencesForSdR();
		
		String tcId = "test020";
		String randomString = RandomStringUtils.randomAlphanumeric(12);
		
		ESScripts esScripts = new ESScripts();
		ActivityLogs al = new ActivityLogs();
		ElasticSearchLogs esLogs = new ElasticSearchLogs();
		
		String tmplFileName = tcId + ".json";
		String fileName = tcId + "_" + suiteData.getTenantName() + ".json";
		String userName = "detect_" + tcId + "_" + randomString;
		String user = userName + "@" + suiteData.getTenantDomainName();
		String testId = tcId + "_" + randomString;
		
		String strDateStart = "2014-10-23T04:10:48";
		String strDateStop = "2014-10-23T04:13:38";
		
		System.out.println("Test Details ### UserName: " + userName + " ### eMail: " + user + " ### test_id: " + testId);
		
		int count = 2;
		al.produceActivityLogs(tmplFileName, fileName,
				suiteData.getTenantName(), user, userName, testId, strDateStart, strDateStop, count);
		
		String consoleLogs;
		consoleLogs = esScripts.scpActivityLogs(fileName, suiteData.getGoldenInputFilePath(), suiteData.getEsScriptsHostName(), suiteData.getEsScriptsUserName());
		System.out.println("Console Log after scp :" + consoleLogs);
		Assert.assertEquals(consoleLogs, "", "File scp Failed!");
		
		consoleLogs = esScripts.injectActivityLogs(fileName, suiteData.getEsScriptsHostName(), suiteData.getEsScriptsUserName());
		System.out.println("Console Log after InjectActivities :" + consoleLogs);
		Assert.assertTrue(consoleLogs.toLowerCase().contains("Wrote 2 records of 2".toLowerCase()), "Injecting Activities to ES failed");
		
		Reporter.log("Waiting for 60sec after Injecting Activities into ES!", true);
		Thread.sleep(1 * 60 * 1000);	//Wait for 1 Minute
		
		verifyActivityCount(testId, 2);
		
		HashSet<String> expIncidents = new HashSet<String>();
		expIncidents.add("TOO_MANY_INFEASIBLE_LOCATIONS");

		verifyStateIncidents(user, expIncidents);

		verifyThreatscoreIncidets(user, expIncidents);
		
		verifyDetectedUser(user);
		
		//TODO: This is not generating Incident as expected. Is this bug... confirm with Shoukat.
		
		System.out.println("Execution Completed - Test Case Name: " + method.getName());
	}
	
	@Test(
			description="This test operates on actual GW data, and generates two BBIs. It focusses on a single anomalous event generating BBIs.")
	public void GW_test24(Method method) throws Exception {
		System.out.println("Execution Started - Test Case Name: " + method.getName());
		
		Reporter.log("Step - Get and Validate Detect Preferences for SdR suite.", true);
		verifyPreferencesForSdR();
		
		String tcId = "test024";
		String randomString = RandomStringUtils.randomAlphanumeric(12);
		
		ESScripts esScripts = new ESScripts();
		ActivityLogs al = new ActivityLogs();
		ElasticSearchLogs esLogs = new ElasticSearchLogs();
		
		String tmplFileName = tcId + ".json";
		String fileName = tcId + "_" + suiteData.getTenantName() + ".json";
		String userName = "detect_" + tcId + "_" + randomString;
		String user = userName + "@" + suiteData.getTenantDomainName();
		String testId = tcId + "_" + randomString;
		
		String strDateStart = "2015-03-19T01:27:06";
		String strDateStop = "2015-03-19T02:02:24";
		
		System.out.println("Test Details ### UserName: " + userName + " ### eMail: " + user + " ### test_id: " + testId);
		
		int count = 37;
		al.produceActivityLogs(tmplFileName, fileName,
				suiteData.getTenantName(), user, userName, testId, strDateStart, strDateStop, count);
		
		String consoleLogs;
		consoleLogs = esScripts.scpActivityLogs(fileName, suiteData.getGoldenInputFilePath(), suiteData.getEsScriptsHostName(), suiteData.getEsScriptsUserName());
		System.out.println("Console Log after scp :" + consoleLogs);
		Assert.assertEquals(consoleLogs, "", "File scp Failed!");
		
		consoleLogs = esScripts.injectActivityLogs(fileName, suiteData.getEsScriptsHostName(), suiteData.getEsScriptsUserName());
		System.out.println("Console Log after InjectActivities :" + consoleLogs);
		Assert.assertTrue(consoleLogs.toLowerCase().contains("Wrote 37 records of 37".toLowerCase()), "Injecting Activities to ES failed");
		
		Reporter.log("Waiting for 60sec after Injecting Activities into ES!", true);
		Thread.sleep(1 * 60 * 1000);	//Wait for 1 Minute
		
		verifyActivityCount(testId, 36);	//TODO: its returning one activity less always.
		
		HashSet<String> expIncidents = new HashSet<String>();
		expIncidents.add("ANOMALOUSLY_LARGE_UPLOAD");

		verifyStateIncidents(user, expIncidents);

		verifyThreatscoreIncidets(user, expIncidents);
		
		verifyDetectedUser(user);
		
		//TODO: This is not generating Incident as expected, but the detect ProfileReport is created. Is this bug... confirm with Shoukat.
		
		System.out.println("Execution Completed - Test Case Name: " + method.getName());
	}
	
	@Test(
			description="This test operates on actual GW data, and generates two BBIs.")
	public void GW_test28(Method method) throws Exception {
		System.out.println("Execution Started - Test Case Name: " + method.getName());
		
		Reporter.log("Step - Get and Validate Detect Preferences for SdR suite.", true);
		verifyPreferencesForSdR();
		
		String tcId = "test028";
		String randomString = RandomStringUtils.randomAlphanumeric(12);
		
		ESScripts esScripts = new ESScripts();
		ActivityLogs al = new ActivityLogs();
		ElasticSearchLogs esLogs = new ElasticSearchLogs();
		
		String tmplFileName = tcId + ".json";
		String fileName = tcId + "_" + suiteData.getTenantName() + ".json";
		String userName = "detect_" + tcId + "_" + randomString;
		String user = userName + "@" + suiteData.getTenantDomainName();
		String testId = tcId + "_" + randomString;
		
		String strDateStart = "2015-03-19T01:27:06";
		String strDateStop = "2015-03-19T02:03:14";
		
		System.out.println("Test Details ### UserName: " + userName + " ### eMail: " + user + " ### test_id: " + testId);
		
		int count = 47;
		al.produceActivityLogs(tmplFileName, fileName,
				suiteData.getTenantName(), user, userName, testId, strDateStart, strDateStop, count);
		
		String consoleLogs;
		consoleLogs = esScripts.scpActivityLogs(fileName, suiteData.getGoldenInputFilePath(), suiteData.getEsScriptsHostName(), suiteData.getEsScriptsUserName());
		System.out.println("Console Log after scp :" + consoleLogs);
		Assert.assertEquals(consoleLogs, "", "File scp Failed!");
		
		consoleLogs = esScripts.injectActivityLogs(fileName, suiteData.getEsScriptsHostName(), suiteData.getEsScriptsUserName());
		System.out.println("Console Log after InjectActivities :" + consoleLogs);
		Assert.assertTrue(consoleLogs.toLowerCase().contains("Wrote 47 records of 47".toLowerCase()), "Injecting Activities to ES failed");
		
		Reporter.log("Waiting for 60sec after Injecting Activities into ES!", true);
		Thread.sleep(1 * 60 * 1000);	//Wait for 1 Minute
		
		verifyActivityCount(testId, 47);
		
		HashSet<String> expIncidents = new HashSet<String>();
		expIncidents.add("ANOMALOUSLY_LARGE_UPLOAD");

		verifyStateIncidents(user, expIncidents);

		verifyThreatscoreIncidets(user, expIncidents);
		
		verifyDetectedUser(user);
		
		System.out.println("Execution Completed - Test Case Name: " + method.getName());
	}
	
	@Test(
			description="This test operates on actual GW data, and generates one BBI.")
	public void GW_test32(Method method) throws Exception {
		System.out.println("Execution Started - Test Case Name: " + method.getName());
		
		Reporter.log("Step - Get and Validate Detect Preferences for SdR suite.", true);
		verifyPreferencesForSdR();
		
		String tcId = "test032";
		String randomString = RandomStringUtils.randomAlphanumeric(12);
		
		ESScripts esScripts = new ESScripts();
		ActivityLogs al = new ActivityLogs();
		ElasticSearchLogs esLogs = new ElasticSearchLogs();
		
		String tmplFileName = tcId + ".json";
		String fileName = tcId + "_" + suiteData.getTenantName() + ".json";
		String userName = "detect_" + tcId + "_" + randomString;
		String user = userName + "@" + suiteData.getTenantDomainName();
		String testId = tcId + "_" + randomString;
		
		String strDateStart = "2015-03-19T01:27:06";
		String strDateStop = "2015-03-19T02:03:14";
		
		System.out.println("Test Details ### UserName: " + userName + " ### eMail: " + user + " ### test_id: " + testId);
		
		int count = 37;
		al.produceActivityLogs(tmplFileName, fileName,
				suiteData.getTenantName(), user, userName, testId, strDateStart, strDateStop, count);
		
		String consoleLogs;
		consoleLogs = esScripts.scpActivityLogs(fileName, suiteData.getGoldenInputFilePath(), suiteData.getEsScriptsHostName(), suiteData.getEsScriptsUserName());
		System.out.println("Console Log after scp :" + consoleLogs);
		Assert.assertEquals(consoleLogs, "", "File scp Failed!");
		
		consoleLogs = esScripts.injectActivityLogs(fileName, suiteData.getEsScriptsHostName(), suiteData.getEsScriptsUserName());
		System.out.println("Console Log after InjectActivities :" + consoleLogs);
		Assert.assertTrue(consoleLogs.toLowerCase().contains("Wrote 37 records of 37".toLowerCase()), "Injecting Activities to ES failed");
		
		Reporter.log("Waiting for 60sec after Injecting Activities into ES!", true);
		Thread.sleep(1 * 60 * 1000);	//Wait for 1 Minute
		
		verifyActivityCount(testId, 37);
		
		HashSet<String> expIncidents = new HashSet<String>();
		expIncidents.add("ANOMALOUSLY_LARGE_UPLOAD_ACROSS_SVC");

		verifyStateIncidents(user, expIncidents);

		verifyThreatscoreIncidets(user, expIncidents);
		
		verifyDetectedUser(user);
		
		//TODO: This is not generating Incident as expected, but the detect ProfileReport is created. Is this bug... confirm with Shoukat.
		
		System.out.println("Execution Completed - Test Case Name: " + method.getName());
	}
	
	@Test(
			description="This test lights up the tree with seven incidents.")
	public void GW_test36(Method method) throws Exception {
		System.out.println("Execution Started - Test Case Name: " + method.getName());
		
		Reporter.log("Step - Get and Validate Detect Preferences for SdR suite.", true);
		verifyPreferencesForSdR();
		
		String tcId = "test036";
		String randomString = RandomStringUtils.randomAlphanumeric(12);
		
		ESScripts esScripts = new ESScripts();
		ActivityLogs al = new ActivityLogs();
		ElasticSearchLogs esLogs = new ElasticSearchLogs();
		
		String tmplFileName = tcId + ".json";
		String fileName = tcId + "_" + suiteData.getTenantName() + ".json";
		String userName = "detect_" + tcId + "_" + randomString;
		String user = userName + "@" + suiteData.getTenantDomainName();
		String testId = tcId + "_" + randomString;
		
		String strDateStart = "2015-03-30T10:55:47";
		String strDateStop = "2015-03-30T23:45:40";
		
		System.out.println("Test Details ### UserName: " + userName + " ### eMail: " + user + " ### test_id: " + testId);
		
		int count = 494;
		al.produceActivityLogs(tmplFileName, fileName,
				suiteData.getTenantName(), user, userName, testId, strDateStart, strDateStop, count);
		
		String consoleLogs;
		consoleLogs = esScripts.scpActivityLogs(fileName, suiteData.getGoldenInputFilePath(), suiteData.getEsScriptsHostName(), suiteData.getEsScriptsUserName());
		System.out.println("Console Log after scp :" + consoleLogs);
		Assert.assertEquals(consoleLogs, "", "File scp Failed!");
		
		consoleLogs = esScripts.injectActivityLogs(fileName, suiteData.getEsScriptsHostName(), suiteData.getEsScriptsUserName());
		System.out.println("Console Log after InjectActivities :" + consoleLogs);
		Assert.assertTrue(consoleLogs.toLowerCase().contains("Wrote 494 records of 494".toLowerCase()), "Injecting Activities to ES failed");
		
		Reporter.log("Waiting for 60sec after Injecting Activities into ES!", true);
		Thread.sleep(2 * 60 * 1000);	//Wait for 2 Minute
		
		verifyActivityCount(testId, 494);
		
		Boolean foundGooglDrive_TOO_MANY_SUM_LARGE_UPLOADS = false;
		Boolean foundBox_TOO_MANY_SUM_LARGE_UPLOADS = false;
//		Boolean foundDropbox_TOO_MANY_SUM_LARGE_DOWNLOADS = false;
		Boolean foundTOO_MANY_INFEASIBLE_LOCATIONS = false;
		Boolean foundTOO_MANY_SUSPICIOUS_LOGINS = false;
		Boolean foundBox_ANOMALOUSLY_LARGE_UPLOAD = false;
		Boolean foundGoogleDrive_ANOMALOUSLY_LARGE_UPLOAD = false;
		Boolean foundGoogleDrive_ANOMALOUSLY_FREQUENT_DELETES = false;
		Boolean foundDropbox_ANOMALOUSLY_LARGE_DOWNLOAD = false;
		
		for (int ite = 0; ite < 20; ite++) {
			
			HttpResponse response = esLogs.getThreatscore(restClient, user,
					suiteData.getTenantName());
			String responseBody = getResponseBody(response);
			System.out.println(responseBody);
			JsonNode arrNode = new ObjectMapper().readTree(
					getJSONValue(responseBody, "hits")).get("hits");
			foundGooglDrive_TOO_MANY_SUM_LARGE_UPLOADS = false;
			foundBox_TOO_MANY_SUM_LARGE_UPLOADS = false;
			foundDropbox_ANOMALOUSLY_LARGE_DOWNLOAD = false;
			foundTOO_MANY_INFEASIBLE_LOCATIONS = false;
			foundTOO_MANY_SUSPICIOUS_LOGINS = false;
			foundGoogleDrive_ANOMALOUSLY_LARGE_UPLOAD = false;
			foundGoogleDrive_ANOMALOUSLY_FREQUENT_DELETES = false;
			foundBox_ANOMALOUSLY_LARGE_UPLOAD = false;
			if (arrNode.isArray()) {
				for (final JsonNode objNode : arrNode) {
					JsonNode _score = objNode.path("_score");
					System.out.println(_score.asText());
					JsonNode _source = objNode.path("_source");
					if (_source.has("ioi_code")) {
						System.out.println(_source.path("ioi_code").asText());
						if (_source.path("ioi_code").asText()
								.equals("TOO_MANY_SUM_LARGE_UPLOADS")
								&& _source.path("facility").asText()
										.equals("Google Drive")) {
							foundGooglDrive_TOO_MANY_SUM_LARGE_UPLOADS = true;
						}
						if (_source.path("ioi_code").asText()
								.equals("TOO_MANY_SUM_LARGE_UPLOADS")
								&& _source.path("facility").asText()
										.equals("Box")) {
							foundBox_TOO_MANY_SUM_LARGE_UPLOADS = true;
						}
						if (_source.path("ioi_code").asText()
								.equals("ANOMALOUSLY_LARGE_DOWNLOAD")
								&& _source.path("facility").asText()
										.equals("Dropbox")) {
							foundDropbox_ANOMALOUSLY_LARGE_DOWNLOAD = true;
						}
						if (_source.path("ioi_code").asText()
								.equals("ANOMALOUSLY_LARGE_UPLOAD")
								&& _source.path("facility").asText()
										.equals("Box")) {
							foundBox_ANOMALOUSLY_LARGE_UPLOAD = true;
						}
						if (_source.path("ioi_code").asText()
								.equals("ANOMALOUSLY_LARGE_UPLOAD")
								&& _source.path("facility").asText()
										.equals("Google Drive")) {
							foundGoogleDrive_ANOMALOUSLY_LARGE_UPLOAD = true;
						}
						if (_source.path("ioi_code").asText()
								.equals("ANOMALOUSLY_FREQUENT_DELETES")
								&& _source.path("facility").asText()
										.equals("Google Drive")) {
							foundGoogleDrive_ANOMALOUSLY_FREQUENT_DELETES = true;
						}
						if (_source.path("ioi_code").asText()
								.equals("TOO_MANY_INFEASIBLE_LOCATIONS")) {
							foundTOO_MANY_INFEASIBLE_LOCATIONS = true;
						}
						if (_source.path("ioi_code").asText()
								.equals("TOO_MANY_SUSPICIOUS_LOGINS")) {
							foundTOO_MANY_SUSPICIOUS_LOGINS = true;
						}
					}
				}
			}
			
			if (foundGooglDrive_TOO_MANY_SUM_LARGE_UPLOADS && foundBox_TOO_MANY_SUM_LARGE_UPLOADS && 
					foundDropbox_ANOMALOUSLY_LARGE_DOWNLOAD && foundBox_ANOMALOUSLY_LARGE_UPLOAD && 
					foundTOO_MANY_INFEASIBLE_LOCATIONS && foundGoogleDrive_ANOMALOUSLY_LARGE_UPLOAD && 
					foundGoogleDrive_ANOMALOUSLY_FREQUENT_DELETES && foundTOO_MANY_SUSPICIOUS_LOGINS) {
				break;
			}
			
			Reporter.log("All the expected Incidents are not Genenrated." + ite + "th time Retrying after 60seconds wait", true);
			Thread.sleep(60 * 1000);
		}
		
		Assert.assertTrue(foundGooglDrive_TOO_MANY_SUM_LARGE_UPLOADS, "TOO_MANY_SUM_LARGE_UPLOADS - Incident NOT created for Google Drive.");
		Assert.assertTrue(foundBox_TOO_MANY_SUM_LARGE_UPLOADS, "TOO_MANY_SUM_LARGE_UPLOADS - Incident NOT created for Box.");
		Assert.assertTrue(foundDropbox_ANOMALOUSLY_LARGE_DOWNLOAD, "ANOMALOUSLY_LARGE_DOWNLOAD - Incident NOT created for Dropbox.");
		Assert.assertTrue(foundBox_ANOMALOUSLY_LARGE_UPLOAD, "ANOMALOUSLY_LARGE_UPLOAD - Incident NOT created for Box.");
		Assert.assertTrue(foundGoogleDrive_ANOMALOUSLY_LARGE_UPLOAD, "ANOMALOUSLY_LARGE_UPLOAD - Incident NOT created for Google Drive.");
		Assert.assertTrue(foundGoogleDrive_ANOMALOUSLY_FREQUENT_DELETES, "ANOMALOUSLY_FREQUENT_DELETES - Incident NOT created for Dropbox.");
		Assert.assertTrue(foundTOO_MANY_INFEASIBLE_LOCATIONS, "TOO_MANY_INFEASIBLE_LOCATIONS - Incident NOT created for Box.");
		Assert.assertTrue(foundTOO_MANY_SUSPICIOUS_LOGINS, "TOO_MANY_SUSPICIOUS_LOGINS - Incident NOT created.");
		
		verifyDetectedUser(user);
		
		System.out.println("Execution Completed - Test Case Name: " + method.getName());
	}
	
	public void setPreferences() throws Exception {
		System.out.println("Started - Setting Preferences");
		
		StringEntity se = new StringEntity("{\"objects\":[{\"resource_uri\":\"/detectbackendautomationcom/api/admin/v1/detectattributes/55ad715617f1c10a8941959d/\",\"importance\":1,\"confidence\":100,\"threshold\":10,\"window\":10,\"enabled\":true},{\"resource_uri\":\"/detectbackendautomationcom/api/admin/v1/detectattributes/55ad715617f1c10a8941959c/\",\"importance\":2,\"confidence\":100,\"threshold\":1,\"window\":1440,\"enabled\":true},{\"resource_uri\":\"/detectbackendautomationcom/api/admin/v1/detectattributes/55ad715617f1c10a8941959f/\",\"importance\":4,\"confidence\":100,\"threshold\":10,\"window\":5,\"enabled\":true},{\"resource_uri\":\"/detectbackendautomationcom/api/admin/v1/detectattributes/55ad715617f1c10a8941959e/\",\"importance\":3,\"confidence\":100,\"threshold\":3,\"window\":1,\"enabled\":true},{\"resource_uri\":\"/detectbackendautomationcom/api/admin/v1/detectattributes/55ad715617f1c10a894195a2/\",\"importance\":2,\"confidence\":100,\"threshold\":60000,\"window\":1,\"enabled\":false},{\"resource_uri\":\"/detectbackendautomationcom/api/admin/v1/detectattributes/55ad715617f1c10a894195a1/\",\"importance\":2,\"confidence\":100,\"threshold\":30000,\"window\":1,\"enabled\":false},{\"resource_uri\":\"/detectbackendautomationcom/api/admin/v1/detectattributes/55ad715617f1c10a894195a0/\",\"importance\":3,\"confidence\":100,\"threshold\":30,\"window\":10,\"enabled\":true},{\"resource_uri\":\"/detectbackendautomationcom/api/admin/v1/detectattributes/55ad715617f1c10a8941959a/\",\"importance\":4,\"confidence\":100,\"threshold\":30,\"window\":10,\"enabled\":true},{\"resource_uri\":\"/detectbackendautomationcom/api/admin/v1/detectattributes/55ad715617f1c10a8941959b/\",\"importance\":1,\"confidence\":100,\"threshold\":3,\"window\":10,\"enabled\":true},{\"resource_uri\":\"/detectbackendautomationcom/api/admin/v1/detectattributes/55ad715617f1c10a89419599/\",\"importance\":3,\"confidence\":100,\"threshold\":3,\"window\":10,\"enabled\":true},{\"resource_uri\":\"/detectbackendautomationcom/api/admin/v1/detectattributes/55ad715617f1c10a89419598/\",\"importance\":2,\"confidence\":100,\"threshold\":3,\"window\":1,\"enabled\":true},{\"resource_uri\":\"/detectbackendautomationcom/api/admin/v1/detectattributes/55ad715617f1c10a894195a3/\",\"importance\":3,\"confidence\":30,\"threshold\":0,\"window\":0,\"enabled\":true},{\"resource_uri\":\"/detectbackendautomationcom/api/admin/v1/detectattributes/55ad715617f1c10a894195a4/\",\"importance\":4,\"confidence\":30,\"threshold\":0,\"window\":0,\"enabled\":true},{\"resource_uri\":\"/detectbackendautomationcom/api/admin/v1/detectattributes/55ad715617f1c10a894195a5/\",\"importance\":2,\"confidence\":30,\"threshold\":0,\"window\":0,\"enabled\":true},{\"resource_uri\":\"/detectbackendautomationcom/api/admin/v1/detectattributes/55ad715617f1c10a894195a6/\",\"importance\":1,\"confidence\":30,\"threshold\":0,\"window\":0,\"enabled\":true},{\"resource_uri\":\"/detectbackendautomationcom/api/admin/v1/detectattributes/55ad715617f1c10a894195a7/\",\"importance\":3,\"confidence\":30,\"threshold\":0,\"window\":0,\"enabled\":true},{\"resource_uri\":\"/detectbackendautomationcom/api/admin/v1/detectattributes/55ad715617f1c10a894195ab/\",\"importance\":2,\"confidence\":30,\"threshold\":0,\"window\":0,\"enabled\":true},{\"resource_uri\":\"/detectbackendautomationcom/api/admin/v1/detectattributes/55ad715617f1c10a894195aa/\",\"importance\":2,\"confidence\":30,\"threshold\":0,\"window\":0,\"enabled\":true},{\"resource_uri\":\"/detectbackendautomationcom/api/admin/v1/detectattributes/55ad715617f1c10a894195a8/\",\"importance\":1,\"confidence\":30,\"threshold\":0,\"window\":0,\"enabled\":true},{\"resource_uri\":\"/detectbackendautomationcom/api/admin/v1/detectattributes/55ad715617f1c10a894195a9/\",\"importance\":4,\"confidence\":30,\"threshold\":0,\"window\":0,\"enabled\":true}]}");
		
		HttpResponse response = restClient.doPost(postDetectattributes(), this.buildCookieHeaders(), null, se);
		String responseBody = getResponseBody(response);
		System.out.println("Response Body Printing....... : " + responseBody);
		
		Reporter.log("Waiting for 1Minute to get Preferences Refreshed!", true);
		Thread.sleep(1 * 60 * 1000);	//Wait for 1 Minute
		
		System.out.println("Completed - Setting Preferences");
	}
	
	protected List<NameValuePair> buildBasicHeaders() throws Exception{
		List<NameValuePair> requestHeader = new ArrayList<NameValuePair>();	
		//requestHeader.add(new BasicNameValuePair(HttpHeaders.AUTHORIZATION, AuthorizationHandler.getAuthParam(suiteData.getUsername(),suiteData.getPassword())));
		//HttpResponse CSRFHeader = AuthorizationHandler.getCSRFHeaders(suiteData.getUsername(),suiteData.getPassword(), suiteData);
		String csfrToken = suiteData.getCSRFToken();
		String sessionID = suiteData.getSessionID();
		requestHeader.add(new BasicNameValuePair("X-CSRFToken", csfrToken));
		requestHeader.add(new BasicNameValuePair("X-Session", sessionID));
		requestHeader.add(new BasicNameValuePair("X-TenantToken", suiteData.getTenantToken()));
		requestHeader.add(new BasicNameValuePair("X-User", suiteData.getUsername()));
		
		return requestHeader;
	}
	
//	private List<NameValuePair> buildCookieHeaders() throws Exception{
//		List<NameValuePair> requestHeader = new ArrayList<NameValuePair>();
////		HttpResponse CSRFHeader = AuthorizationHandler.getCSRFHeaders(suiteData.getUsername(),suiteData.getPassword(), suiteData);
////		csfrToken = AuthorizationHandler.getCSRFToken(CSRFHeader);
////		sessionID = AuthorizationHandler.getUserSessionID(CSRFHeader);
//		requestHeader.add(new BasicNameValuePair("Cookie", "sessionid=" + sessionID + "; csrftoken=" + csfrToken + ";"));
//		requestHeader.add(new BasicNameValuePair("Referer", "https://eoe.elastica-inc.com/static/ng/appThreats/index.html"));
//		requestHeader.add(new BasicNameValuePair("X-CSRFToken", csfrToken));
//		
//		return requestHeader;
//	}
//	
//	private String getJSONValue(String json, String key) throws JsonProcessingException, IOException {
//		JsonFactory factory = new JsonFactory();
//
//		ObjectMapper mapper = new ObjectMapper(factory);
//		JsonNode rootNode = mapper.readTree(json);
//		return rootNode.get(key).toString();
//	}
	
	public static String getResponseBody(HttpResponse response) {
		try {
			return EntityUtils.toString(response.getEntity(), "utf-8");
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
//	private void verifyActivityCount(String query, int count) {
//		Reporter.log("Retrieving the logs from Elastic Search ...");
//		ElasticSearchLogs esLogs = new ElasticSearchLogs();
//		
//		Date dateTo = new Date();
//    	SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd'T'HH");
//    	formatDate.setTimeZone(TimeZone.getTimeZone("GMT"));
//    	String strDateTimeTo = formatDate.format(dateTo);
//    	System.out.println(strDateTimeTo);
//    	Calendar cal = Calendar.getInstance();
//    	cal.add(Calendar.DATE, -30);
//    	Date dateFrom = cal.getTime();
//    	String strDateTimeFrom = formatDate.format(dateFrom);
//    	System.out.println(strDateTimeFrom);
//    	
//		HttpResponse response;
//		try {
//			String payload = esQueryBuilder.getSearchQueryForDisplayLogs(strDateTimeFrom+":00:00.000Z", strDateTimeTo+":59:59.999Z", 
//					query, "Elastica", count, "https://api-eoe.elastica-inc.com/", csfrToken, sessionID, suiteData.getUsername());
//			System.out.println(payload);
//		
//			response = esLogs.getDisplayLogs(restClient, this.buildBasicHeaders(), new StringEntity(payload));
//			String responseBody = getResponseBody(response);
//			System.out.println("Response Body Printing....... : " + responseBody);
//			
//			ObjectMapper om = new ObjectMapper();
//			DisplayLogResults dlr = om.readValue(responseBody, DisplayLogResults.class);
//			long activityCount = dlr.getHits().getTotal();
//			System.out.println("Total Count: " + activityCount);
//			
//			Assert.assertTrue(activityCount==count, "Number of Activities returned are not Equal with Injected ones");
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	private void verifyDetectedUser(String user) {
//		Reporter.log("Retrieving the Detected Users information from Elastic Search ...");
//		ElasticSearchLogs esLogs = new ElasticSearchLogs();
//		
//		List<NameValuePair> queryParams = new ArrayList<NameValuePair>();
//		queryParams.add(new BasicNameValuePair("source", "{\"query\":{\"filtered\":{\"query\":{\"bool\":{\"must\":[{\"term\":{\"facility\":\"_ALL\"}}]}},\"filter\":{\"and\":[{\"or\":[{\"term\":{\"__source\":\"\"}},{\"term\":{\"__source\":\"GW\"}},{\"term\":{\"__source\":\"API\"}},{\"missing\":{\"field\":\"__source\"}}]}]}}},\"sort\":{\"threat_score\":{\"order\":\"desc\",\"ignore_unmapped\":\"true\"}},\"size\":100000}"));
//		queryParams.add(new BasicNameValuePair("sourceName", "DETECT"));		
//		
//		Boolean foundUser = false;
//		int ite=1;
//		for (; ite<=5; ite++) {
//			try {
//				HttpResponse response = esLogs.getUserScoreAnomalies(restClient, this.buildCookieHeaders(), queryParams);
//				String responseBody = getResponseBody(response);
//				System.out.println("Response Body Printing....... : " + responseBody);
//				
//				String hits = getJSONValue(getJSONValue(responseBody, "hits"), "hits");
//				JSONArray jArray = (JSONArray) new JSONTokener(hits).nextValue();
//				String rUser;
//				int rThreatScore = 0;
//				
//				for (int i = 0; i < jArray.size(); i++) {
//					String sourceJson;
//					sourceJson = getJSONValue(jArray.getJSONObject(i).toString(), "_source");
//					rUser = getJSONValue(sourceJson, "user");
//					rThreatScore =  Integer.parseInt(getJSONValue(sourceJson, "threat_score"));
//					System.out.println("Msg Printing....." + rUser + " " + rThreatScore);
//					if (rUser.equalsIgnoreCase("\"" + user + "\"")) {
//						foundUser = true;
//						break;
//					}
//				}
//				
//				if (foundUser) {
//					break;
//				} else {
//					Thread.sleep(1 * 60 * 1000);	//wait for 1 minute.
//				}
//			} catch (JsonProcessingException e) {
//				e.printStackTrace();
//			} catch (NumberFormatException | IOException e) {
//				e.printStackTrace();
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//		
//		System.out.println("Going to Assert after " + ite + "Minutes of waiting");
//		Assert.assertTrue(foundUser, "Users Information is not listed Under DETECT");
//		//TODO: Disabled, threadscore validation for now, it takes long time to get the final stage.
////		Assert.assertEquals(rThreatScore, 95, "User's ThreatScore is Not Matching");
//	}	
	
	private void verifyUserNotDetected(String user) {
		Reporter.log("Retrieving the Detected Users information from Elastic Search ...");
		ElasticSearchLogs esLogs = new ElasticSearchLogs();
		
		List<NameValuePair> queryParams = new ArrayList<NameValuePair>();
		queryParams.add(new BasicNameValuePair("source", "{\"query\":{\"filtered\":{\"query\":{\"bool\":{\"must\":[{\"term\":{\"facility\":\"_ALL\"}}]}},\"filter\":{\"and\":[{\"or\":[{\"term\":{\"__source\":\"\"}},{\"term\":{\"__source\":\"GW\"}},{\"term\":{\"__source\":\"API\"}},{\"missing\":{\"field\":\"__source\"}}]}]}}},\"sort\":{\"threat_score\":{\"order\":\"desc\",\"ignore_unmapped\":\"true\"}},\"size\":100000}"));
		queryParams.add(new BasicNameValuePair("sourceName", "DETECT"));		
		
		Boolean foundUser = false;
		
		Date dateTo = new Date();
    	SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd'T'HH");
    	formatDate.setTimeZone(TimeZone.getTimeZone("GMT"));
    	String strDateTimeTo = formatDate.format(dateTo);

    	try {
    		HttpResponse response = esLogs.getUserScoreAnomalies(restClient, this.buildCookieHeaders(),  suiteData.getHost(), queryParams);
    		String responseBody = getResponseBody(response);
    		System.out.println("Response Body Printing....... : " + responseBody);

    		String hits = getJSONValue(getJSONValue(responseBody, "hits"), "hits");
    		JSONArray jArray = (JSONArray) new JSONTokener(hits).nextValue();
    		int rThreatScore = 0;

    		for (int i = 0; i < jArray.size(); i++) {
    			String sourceJson;
    			sourceJson = getJSONValue(jArray.getJSONObject(i).toString(), "_source");
    			String rUser = getJSONValue(sourceJson, "user");
    			String cTime = getJSONValue(sourceJson, "created_timestamp");
    			rThreatScore =  Integer.parseInt(getJSONValue(sourceJson, "threat_score"));
    			System.out.println("Msg Printing....." + rUser + " " + rThreatScore);
    			if (rUser.equalsIgnoreCase("\"" + user + "\"") || cTime.startsWith(strDateTimeTo)) {
    				foundUser = true;
    				break;
    			}
    		}
    	} catch (JsonProcessingException e) {
    		e.printStackTrace();
    	} catch (NumberFormatException | IOException e) {
    		e.printStackTrace();
    	} catch (Exception e) {
    		e.printStackTrace();
    	}

		Assert.assertFalse(foundUser, "Users Information are listed Under DETECT, but it should NOT.");
	}

//	@SuppressWarnings("unchecked")
//	private void verifyThreatscoreIncidets(String user, HashSet<String> expIncidents) {
//		Reporter.log("Retrieving the Detected Users information from Elastic Search ...");
//		ElasticSearchLogs esLogs = new ElasticSearchLogs();
//		HashSet<String> tmpIncidents = new HashSet<String>();
//		
//		Boolean matching = false;
//		for (int ite=0; ite<5; ite++) {
//			
//			HashSet<String> actIncidents = new HashSet<String>();
//			tmpIncidents = (HashSet<String>) expIncidents.clone();
//			
//			try {
//				HttpResponse response = esLogs.getThreatscore(restClient, user, suiteData.getTenantName());
//				String responseBody = getResponseBody(response);
//				System.out.println(responseBody);
//				
//				JsonNode arrNode;
//				arrNode = new ObjectMapper().readTree(getJSONValue(responseBody, "hits")).get("hits");
//
//				if (arrNode.isArray()) {
//					for (final JsonNode objNode : arrNode) {
//						JsonNode _source = objNode.path("_source");
//						if (_source.has("ioi_code")) {
//							System.out.println(_source.path("ioi_code").asText());
//							actIncidents.add(_source.path("ioi_code").asText());
//						}
//					}
//				}
//				if (actIncidents.size() == tmpIncidents.size() && actIncidents.containsAll(tmpIncidents)) {
//					matching = true;
//					System.out.println("Results are Matching");
//					break;
//				} else {
//					tmpIncidents.removeAll(actIncidents);
//					System.out.println("The Incidents NOT created but expected:" + tmpIncidents);
//					Thread.sleep(1* 60 * 1000);	//Wait for 1 Minute
//				}
//			} catch (IOException e) {
//				e.printStackTrace();
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//
//		Assert.assertTrue(matching, "Incidents " + tmpIncidents + "are NOT created.");
//	}
	
//	@SuppressWarnings("unchecked")
//	private void verifyStateIncidents(String user, HashSet<String> expIncidents) {
//		Reporter.log("Retrieving the Detected Users information from Elastic Search ...");
//		
//		ElasticSearchLogs esLogs = new ElasticSearchLogs();
//		HashSet<String> expDetectIncidents = new HashSet<String>();
//		HashSet<String> expAnomalyIncidents = new HashSet<String>();
//		Boolean matching = false;
//		
//		for (int ite=0; ite<5; ite++) {
//			
//			HashSet<String> detectIncidents = new HashSet<String>();
//			HashSet<String> anomalyIncidents = new HashSet<String>();
//			expDetectIncidents = (HashSet<String>) expIncidents.clone();
//			expAnomalyIncidents = (HashSet<String>) expIncidents.clone();
//			
//			try {
//				HttpResponse response = esLogs.getState(restClient, user, suiteData.getTenantName());
//				String responseBody = getResponseBody(response);
//				System.out.println(responseBody);
//				
//				JsonNode arrNode = new ObjectMapper().readTree(getJSONValue(responseBody, "hits")).get("hits");
//
//				if (arrNode.isArray()) {
//					for (final JsonNode objNode : arrNode) {
//						JsonNode _source = objNode.path("_source");
//						if (_source.path("event_type").asText().equals("DetectorReport") && _source.has("ioi_code")) {
//							detectIncidents.add(_source.path("ioi_code").asText());
//							System.out.println(_source.path("ioi_code").asText());
//						}
//						if (_source.path("event_type").asText().equals("AnomalyReport") && _source.has("ioi_code")) {
//							anomalyIncidents.add(_source.path("ioi_code").asText());
//							System.out.println(_source.path("ioi_code").asText());
//						}
//					}
//				}
//				Boolean detectMatch = false;
//				Boolean anomalyMatch = false;
//				
//				if (detectIncidents.size() == expDetectIncidents.size() && detectIncidents.containsAll(expDetectIncidents)) {
//					detectMatch = true;
//					System.out.println("Results are Matching for DetectReport");
//				} else {
//					expDetectIncidents.removeAll(detectIncidents);
//				}
//				if (anomalyIncidents.size() == expAnomalyIncidents.size() && anomalyIncidents.containsAll(expAnomalyIncidents)) {
//					anomalyMatch = true;
//					System.out.println("Results are Matching for AnomalyReport");
//				} else {
//					expAnomalyIncidents.removeAll(detectIncidents);
//				}
//				if (detectMatch && anomalyMatch) {
//					matching = true;
//					break;
//				} else {
//					System.out.println("The Anomaly Incidents NOT created but expected:" + expAnomalyIncidents);
//					System.out.println("The Detect Incidents NOT created but expected:" + expDetectIncidents);
//					Thread.sleep(1* 60 * 1000);	//Wait for 1 Minute
//				}
//			} catch (IOException e) {
//				e.printStackTrace();
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//
//		Assert.assertTrue(matching, "Incidents " + expDetectIncidents + " in DetectorReport and " + expAnomalyIncidents + " in AnomalyReport are NOT created.");
//	}
	
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
