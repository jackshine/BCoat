package com.elastica.beatle.tests.detect;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.UUID;

import javax.ws.rs.core.HttpHeaders;
import javax.xml.bind.JAXBException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.introspect.JacksonAnnotationIntrospector;
import org.codehaus.jackson.node.ObjectNode;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.SkipException;
import org.testng.annotations.BeforeClass;
import org.testng.asserts.SoftAssert;

import com.elastica.beatle.TestSuiteDTO;
import com.elastica.beatle.Authorization.AuthorizationHandler;
import com.elastica.beatle.RestClient.Client;
import com.elastica.beatle.RestClient.ClientUtil;
import com.elastica.beatle.detect.DetectCommonutils;
import com.elastica.beatle.detect.DetectInitializeTests;
import com.elastica.beatle.detect.dto.AttributeBean;
import com.elastica.beatle.detect.dto.DetectAttributeDto;
import com.elastica.beatle.detect.dto.IOI_Code;
import com.elastica.beatle.detect.dto.InputBean;
import com.elastica.beatle.detect.dto.Objects;
import com.elastica.beatle.es.ActivityLogs;
import com.elastica.beatle.es.ESQueryBuilder;
import com.elastica.beatle.es.ESScripts;
import com.elastica.beatle.es.ElasticSearchLogs;
import com.elastica.beatle.logger.Logger;
import com.elastica.beatle.protect.ProtectFunctions;
import com.elastica.beatle.protect.ProtectTestConstants;

public class DetectUtils extends DetectInitializeTests {
	
	
	
	private static final String INSERT = "insert";
	private static final String APPLICATION_JSON = "application/json";
	protected static final String NAME = "name";
	protected static final String RESOURCE_URI = "resource_uri";
	protected static final String OBJECTS = "detect_attributes";
	private static final String UPDATE = "update";
	protected static final String TOO_MANY_SEQUENCE = "TOO_MANY_SEQUENCE_";
	protected static final String PRIORITY_1 = "P1 test cases";
	protected static final String ALL = "All";
	
	DetectCommonutils utils = new DetectCommonutils();
		
	
	
	public InputBean createFileUpdateData1(String tcId, AttributeBean attributeBean) {

		InputBean inputBean = null;
		String randomString = RandomStringUtils.randomAlphanumeric(12);
		ActivityLogs al = new ActivityLogs();
		String tmplFileName = tcId + ".json";
		String fileName = tcId + "_" + suiteData.getTenantName()+"_"+attributeBean.getThreshold()+"_"+attributeBean.getWindow()+"_"+attributeBean.getImportance() + ".json";
		String userName = "detect_" + randomString + "_" + tcId;
		// String userName = "tarak.elastica";
		String user = userName + "@" + suiteData.getTenantDomainName();

		String testId = randomString+ "_" +tcId;
		inputBean = new InputBean(tmplFileName, fileName, userName, user, testId);
		System.out.println("Test Details ###  " + inputBean.toString());

		al.produceActivityLogs(tmplFileName, fileName, suiteData.getTenantName(), user, userName, testId, inputBean,  attributeBean, suiteData);

		return inputBean;
	}
	
	public InputBean createFileUpdateData(String tcId, String user, String userName, String randomString) {

		InputBean inputBean = null;
		
		ActivityLogs al = new ActivityLogs();
		String tmplFileName = tcId + ".json";
		String fileName = tcId + "_" + suiteData.getTenantName() + ".json";
		

		String testId = randomString+ "_" +tcId;
		inputBean = new InputBean(tmplFileName, fileName, userName, user, testId);
		Reporter.log("Test Details ###  " + inputBean.toString());

		al.produceActivityLogs(tmplFileName, fileName,
				suiteData.getTenantName(), user, userName, testId,inputBean, suiteData);
		return inputBean;
	}
	
	public InputBean createFileUpdateDatasd1(String tcId, String user, String userName, String randomString) {

		InputBean inputBean = null;
		
		ActivityLogs al = new ActivityLogs();
		String tmplFileName = tcId + ".json";
		String fileName = tcId + "_" + suiteData.getTenantName() + ".json";
		

		String testId = randomString+ "_" +tcId;
		inputBean = new InputBean(tmplFileName, fileName, userName, user, testId);
		Reporter.log("Test Details ###  " + inputBean.toString());

		al.produceActivityLogsforSD1(tmplFileName, fileName,
				suiteData.getTenantName(), user, userName, testId,inputBean, suiteData);
		return inputBean;
	}
	
	public InputBean createFileUpdateDataforLargeBBI(String tcId, AttributeBean attributeBean) throws InterruptedException {

		InputBean inputBean = null;
		String randomString = RandomStringUtils.randomAlphanumeric(12);
		ActivityLogs al = new ActivityLogs();
		String tmplFileName = tcId + ".json";
		String fileName = tcId + "_" + suiteData.getTenantName() +"_"+attributeBean.getConfidence()+"_"+attributeBean.getImportance()+".json";
		String userName = "detect_" + randomString + "_" + tcId;
		// String userName = "tarak.elastica";
		String user = userName + "@" + suiteData.getTenantDomainName();

		String testId = randomString+ "_" +tcId;
		inputBean = new InputBean(tmplFileName, fileName, userName, user, testId);
		System.out.println("Test Details ###  " + inputBean.toString());

		Reporter.log("Test Details ### UserName: " +inputBean.toString(), true);
		
		al.produceActivityLogsForLargeBBI(tmplFileName, fileName,
				suiteData.getTenantName(), user, userName,inputBean.getTestId(),inputBean, suiteData);
		return inputBean;
	}
	
	public InputBean createFileUpdateDataforLargeBBI1(String tcId, AttributeBean attributeBean) throws InterruptedException {

		InputBean inputBean = null;
		String randomString = RandomStringUtils.randomAlphanumeric(12);
		ActivityLogs al = new ActivityLogs();
		String tmplFileName = tcId + ".json";
		String fileName = tcId + "_" + suiteData.getTenantName() +"_"+attributeBean.getConfidence()+"_"+attributeBean.getImportance()+".json";
		String userName = "detect_" + randomString + "_" + tcId;
		// String userName = "tarak.elastica";
		String user = userName + "@" + suiteData.getTenantDomainName();

		String testId = randomString+ "_" +tcId;
		inputBean = new InputBean(tmplFileName, fileName, userName, user, testId);
		System.out.println("Test Details ###  " + inputBean.toString());

		Reporter.log("Test Details ### UserName: " +inputBean.toString(), true);
		
		al.produceActivityLogsLargeBBITest1(tmplFileName, fileName,
				suiteData.getTenantName(), user, userName, inputBean, attributeBean, suiteData);
		return inputBean;
	}
	
	public InputBean createFileUpdateDataforLargeBBI2(String tcId, AttributeBean attributeBean) throws InterruptedException {

		InputBean inputBean = null;
		String randomString = RandomStringUtils.randomAlphanumeric(12);
		ActivityLogs al = new ActivityLogs();
		String tmplFileName = tcId + ".json";
		String fileName = tcId + "_" + suiteData.getTenantName() +"_"+attributeBean.getConfidence()+"_"+attributeBean.getImportance()+".json";
		String userName = "detect_" + randomString + "_" + tcId;
		// String userName = "tarak.elastica";
		String user = userName + "@" + suiteData.getTenantDomainName();

		String testId = randomString+ "_" +tcId;
		inputBean = new InputBean(tmplFileName, fileName, userName, user, testId);
		System.out.println("Test Details ###  " + inputBean.toString());

		Reporter.log("Test Details ### UserName: " +inputBean.toString(), true);
		
		al.produceActivityLogsLargeBBITest2(tmplFileName, fileName,
				suiteData.getTenantName(), tcId, user, userName, inputBean, suiteData);
		return inputBean;
	}
	
	public InputBean createFileUpdateDataforfrequent(String tcId, AttributeBean attributeBean) throws InterruptedException {

		InputBean inputBean = null;
		String randomString = RandomStringUtils.randomAlphanumeric(12);
		ActivityLogs al = new ActivityLogs();
		String tmplFileName = tcId + ".json";
		String fileName = tcId + "_" + suiteData.getTenantName() +"_"+attributeBean.getConfidence()+"_"+attributeBean.getImportance()+".json";
		String userName = "detect_" + randomString + "_" + tcId;
		// String userName = "tarak.elastica";
		String user = userName + "@" + suiteData.getTenantDomainName();

		String testId = randomString+ "_" +tcId;
		inputBean = new InputBean(tmplFileName, fileName, userName, user, testId);
		System.out.println("Test Details ###  " + inputBean.toString());

		System.out.println("Test Details ### UserName: " +inputBean.toString());
		
		al.produceActivityLogsfrequentBBI(tmplFileName, fileName,
				suiteData.getTenantName(), user, userName, inputBean, attributeBean, suiteData);
		return inputBean;
	}
	
	
	public InputBean createFileUpdateDataforEvolvingProfile(String tcId, AttributeBean attributeBean, String dateDiff) throws InterruptedException {

		InputBean inputBean = null;
		String randomString = RandomStringUtils.randomAlphanumeric(12);
		ActivityLogs al = new ActivityLogs();
		String tmplFileName = tcId + ".json";
		String fileName = tcId + "_" + suiteData.getTenantName() +"_"+attributeBean.getConfidence()+"_"+attributeBean.getImportance()+".json";
		String userName = "detect_" + randomString + "_" + tcId;
		// String userName = "tarak.elastica";
		String user = userName + "@" + suiteData.getTenantDomainName();

		String testId = randomString+ "_" +tcId;
		inputBean = new InputBean(tmplFileName, fileName, userName, user, testId);
		inputBean.setCount(0);
		System.out.println("Test Details ###  " + inputBean.toString());

		System.out.println("Test Details ### UserName: " +inputBean.toString());
		
		al.produceActivityLogsforEvolvingProfile(tmplFileName, fileName,
				suiteData.getTenantName(), user, userName, inputBean, attributeBean, suiteData, dateDiff);
		return inputBean;
	}
	
	
	public InputBean createFileUpdateDataforBBIInSequenceDetector(String tcId, AttributeBean attributeBean, InputBean inputBean) throws InterruptedException {

		ActivityLogs al = new ActivityLogs();
		String tmplFileName = tcId + ".json";
		String fileName = tcId + "_" + suiteData.getTenantName() +"_"+attributeBean.getConfidence()+"_"+attributeBean.getImportance()+".json";
		Long count = inputBean.getCount();
		inputBean = new InputBean(tmplFileName, fileName, inputBean.getUserName(), inputBean.getUser(), inputBean.getTestId());
		inputBean.setCount(count);
		System.out.println("Test Details ###  " + inputBean.toString());

		System.out.println("Test Details ### UserName: " +inputBean.toString());
		al.produceActivityLogs(tmplFileName, fileName,
				suiteData.getTenantName(),  inputBean, attributeBean, suiteData);
		return inputBean;
	}
	
	
	public InputBean createFileUpdateDataforEvolvingprofileIncident(String tcId, AttributeBean attributeBean, InputBean inputBean, String dateDiff) throws InterruptedException {

		ActivityLogs al = new ActivityLogs();
		String tmplFileName = tcId + ".json";
		String fileName = tcId + "_" + suiteData.getTenantName() +"_"+attributeBean.getConfidence()+"_"+attributeBean.getImportance()+".json";
		Long count = inputBean.getCount();
		inputBean = new InputBean(tmplFileName, fileName, inputBean.getUserName(), inputBean.getUser(), inputBean.getTestId());
		inputBean.setCount(count);
		System.out.println("Test Details ###  " + inputBean.toString());

		System.out.println("Test Details ### UserName: " +inputBean.toString());
		al.produceActivityLogsforEvolvingProfile(tmplFileName, fileName,
				suiteData.getTenantName(), inputBean.getUser(), inputBean.getUserName(), inputBean, attributeBean, suiteData, dateDiff);
		return inputBean;
	}

	
	public String validateIncidentsEndtoEnd(String ioicode) {
		Reporter.log("Retrieving the logs from Elastic Search ...");
		ElasticSearchLogs esLogs = new ElasticSearchLogs();
		String responseBody = null;
		Date dateTo = new Date();
    	SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    	formatDate.setTimeZone(TimeZone.getTimeZone("GMT"));
    	String strDateTimeTo = formatDate.format(dateTo);
    	System.out.println(strDateTimeTo);
    	Calendar cal = Calendar.getInstance();
    	cal.add(Calendar.MINUTE, -50);
    	Date dateFrom = cal.getTime();
    	String strDateTimeFrom = formatDate.format(dateFrom);
    	System.out.println(strDateTimeFrom);
    	
		HttpResponse response;
		Boolean found_ioi =false;
		int ite=1;
		for (; ite<=15; ite++) {
		try {
		
				String payload = esQueryBuilder.getSearchQueryForDetect(strDateTimeFrom,
						strDateTimeTo);
				response = esLogs.getCloudServiceAnomalies(restClient, buildCookieHeaders(), new StringEntity(payload),suiteData);
				Assert.assertEquals(response.getStatusLine().getStatusCode(), 200, "Response code is not equal");
				 responseBody = getResponseBody(response);
				Reporter.log(" validateIncidents::::  Response::::   " + responseBody, true);
				JsonNode jnode = unmarshall(responseBody, JsonNode.class);
				
				if (jnode.isArray()) {
				    for (final JsonNode objNode : jnode) {
				    	String    	ioi_found = getJSONValue(objNode.toString(), "ioi").toString().replace("\"", "");
				    	Reporter.log("found ioicode:::::::: "+ioi_found, true);
				    	Reporter.log("Expected ioicode:::::::: "+ioicode, true);
				    	if (ioicode.equalsIgnoreCase(ioi_found)) {
							found_ioi = true;
							break;
						}
				    	
				       }
				    if (found_ioi) {
						break;
					} else {
						Thread.sleep(1 * 60 * 1000);	//wait for 1 minute.
						Reporter.log("Incident  is not found in the detect page Retrying::::::: " + ite + "Minutes of waiting");
					}
				    
				    }
		
				
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		}
		Reporter.log("Going to Assert after " + ite + "Minutes of waiting");
		
		Reporter.log("Expected IOI_Code:::::: " + ioicode + " not found in the incident list",true);
		Assert.assertTrue(found_ioi, "incident not listed");
		
		return responseBody;
	}
	
	public String validateIncidentsClearedEndtoEnd(String ioicode) {
		Reporter.log("Retrieving the logs from Elastic Search ...");
		ElasticSearchLogs esLogs = new ElasticSearchLogs();
		String responseBody = null;
		Date dateTo = new Date();
    	SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    	formatDate.setTimeZone(TimeZone.getTimeZone("GMT"));
    	String strDateTimeTo = formatDate.format(dateTo);
    	System.out.println(strDateTimeTo);
    	Calendar cal = Calendar.getInstance();
    	cal.add(Calendar.MINUTE, -50);
    	Date dateFrom = cal.getTime();
    	String strDateTimeFrom = formatDate.format(dateFrom);
    	System.out.println(strDateTimeFrom);
    	
		HttpResponse response;
		Boolean found_ioi =false;
		int ite=1;
		for (; ite<=2; ite++) {
		try {
		
				String payload = esQueryBuilder.getSearchQueryForDetect(strDateTimeFrom,
						strDateTimeTo);
				response = esLogs.getCloudServiceAnomalies(restClient, buildCookieHeaders(), new StringEntity(payload),suiteData);
				Assert.assertEquals(response.getStatusLine().getStatusCode(), 200, "Response code is not equal");
				 responseBody = getResponseBody(response);
				Reporter.log(" validateIncidents::::  Response::::   " + responseBody, true);
				JsonNode jnode = unmarshall(responseBody, JsonNode.class);
				
				if (jnode.isArray()) {
				    for (final JsonNode objNode : jnode) {
				    	String    	ioi_found = getJSONValue(objNode.toString(), "ioi").toString().replace("\"", "");
				    	Reporter.log("found ioicode:::::::: "+ioi_found, true);
				    	Reporter.log("Expected ioicode:::::::: "+ioicode, true);
				    	if (ioicode.equalsIgnoreCase(ioi_found)) {
							found_ioi = true;
							break;
						}
				    	
				       }
				    if (found_ioi) {
						break;
					} else {
						Thread.sleep(1 * 30 * 1000);	//wait for 1 minute.
						Reporter.log("Incident  is not found in the detect page Retrying::::::: " + ite + "Minutes of waiting");
					}
				    
				    }
		
				
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		}
		Reporter.log("Going to Assert after " + ite + "Minutes of waiting");
		
		Reporter.log("Expected IOI_Code:::::: " + ioicode + " not found in the incident list",true);
		Assert.assertFalse(found_ioi, "incident is listed");
		
		return responseBody;
	}
	
	public HttpResponse getDetectAttributes() throws Exception {
		URI getAttributesURI = getDetectattributes();
		HttpResponse resp =  restClient.doGet(getAttributesURI, buildCookieHeaders());
		Assert.assertEquals(resp.getStatusLine().getStatusCode(), 200, "Response code is not equal");
		return resp;
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
		
		public HttpResponse getDetectAttributes(TestSuiteDTO suiteData) throws Exception {
			URI getAttributesURI = getDetectattributes();
			HttpResponse resp =  restClient.doGet(getAttributesURI, buildCookieHeaders(suiteData.getCSRFToken(), suiteData.getSessionID()));
			Assert.assertEquals(resp.getStatusLine().getStatusCode(), 200, "Response code is not equal");
			return resp;
			}
		
		
			
		
	protected List<NameValuePair> buildBasicHeaders(String csfrToken,String sessionID ) throws Exception{
		List<NameValuePair> requestHeader = new ArrayList<NameValuePair>();	
		requestHeader.add(new BasicNameValuePair(HttpHeaders.AUTHORIZATION, AuthorizationHandler.getAuthParam(suiteData.getUsername(),suiteData.getPassword())));
		requestHeader.add(new BasicNameValuePair("X-CSRFToken", csfrToken));
		requestHeader.add(new BasicNameValuePair("X-Session", sessionID));
		requestHeader.add(new BasicNameValuePair("X-TenantToken", suiteData.getTenantToken()));
		requestHeader.add(new BasicNameValuePair("X-User", suiteData.getUsername()));
		
		return requestHeader;
	}
	protected  List<NameValuePair> buildCookieHeaders() throws Exception{
		List<NameValuePair> requestHeader = new ArrayList<NameValuePair>();
	//	HttpResponse CSRFHeader = AuthorizationHandler.getCSRFHeaders(suiteData.getUsername(),suiteData.getPassword(), suiteData);
	//	String csfrToken = AuthorizationHandler.getCSRFToken(CSRFHeader);
		//String sessionID = AuthorizationHandler.getUserSessionID(CSRFHeader);
		requestHeader.add(new BasicNameValuePair(HttpHeaders.AUTHORIZATION, AuthorizationHandler.getAuthParam(suiteData.getUsername(),suiteData.getPassword())));
		requestHeader.add(new BasicNameValuePair("Cookie", "sessionid=" + suiteData.getSessionID() + "; csrftoken=" + suiteData.getCSRFToken() + ";"));
		requestHeader.add(new BasicNameValuePair("X-CSRFToken", suiteData.getCSRFToken()));
		requestHeader.add(new BasicNameValuePair("Referer", suiteData.getReferer()));
		requestHeader.add(new BasicNameValuePair("Host", suiteData.getHost()));
		requestHeader.add(new BasicNameValuePair("Accept", "application/json"));
		requestHeader.add(new BasicNameValuePair("Content-Type", "application/json"));
		System.out.println(requestHeader.toString());
		
		
		return requestHeader;
	}
	
	
	protected  List<NameValuePair> buildCookieHeaders(String csfrToken,String sessionID) throws Exception{
		List<NameValuePair> requestHeader = new ArrayList<NameValuePair>();
	//	HttpResponse CSRFHeader = AuthorizationHandler.getCSRFHeaders(suiteData.getUsername(),suiteData.getPassword(), suiteData);
	//	String csfrToken = AuthorizationHandler.getCSRFToken(CSRFHeader);
	//	String sessionID = AuthorizationHandler.getUserSessionID(CSRFHeader);
		requestHeader.add(new BasicNameValuePair(HttpHeaders.AUTHORIZATION, AuthorizationHandler.getAuthParam(suiteData.getUsername(),suiteData.getPassword())));
		requestHeader.add(new BasicNameValuePair("Cookie", "sessionid=" + sessionID + "; csrftoken=" + csfrToken + ";"));
		requestHeader.add(new BasicNameValuePair("X-CSRFToken", csfrToken));
		requestHeader.add(new BasicNameValuePair("Referer", suiteData.getReferer()));
		requestHeader.add(new BasicNameValuePair("Host", suiteData.getHost()));
		requestHeader.add(new BasicNameValuePair("Accept", "application/json"));
		requestHeader.add(new BasicNameValuePair("Content-Type", "application/json"));
		System.out.println(requestHeader.toString());
		
		
		return requestHeader;
	}
	
	protected  List<NameValuePair> buildCookieHeaderstext() throws Exception{
		List<NameValuePair> requestHeader = new ArrayList<NameValuePair>();
		//HttpResponse CSRFHeader = AuthorizationHandler.getCSRFHeaders(suiteData.getUsername(),suiteData.getPassword(), suiteData);
		//String csfrToken = AuthorizationHandler.getCSRFToken(CSRFHeader);
		//String sessionID = AuthorizationHandler.getUserSessionID(CSRFHeader);
		requestHeader.add(new BasicNameValuePair(HttpHeaders.AUTHORIZATION, AuthorizationHandler.getAuthParam(suiteData.getUsername(),suiteData.getPassword())));
		requestHeader.add(new BasicNameValuePair("Cookie", "sessionid=" + suiteData.getSessionID() + "; csrftoken=" + suiteData.getCSRFToken() + ";"));
		requestHeader.add(new BasicNameValuePair("X-CSRFToken", suiteData.getCSRFToken()));
		requestHeader.add(new BasicNameValuePair("Referer", suiteData.getReferer()));
		requestHeader.add(new BasicNameValuePair("Host", suiteData.getHost()));
		requestHeader.add(new BasicNameValuePair("Accept", "text/plain"));
		requestHeader.add(new BasicNameValuePair("Content-Type", "text/plain"));
		System.out.println(requestHeader.toString());
		
		
		return requestHeader;
	}
	
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
	
	public void verifyActivityCount(String query, int count) {
		Reporter.log("Retrieving the logs from Elastic Search ...");
		ElasticSearchLogs esLogs = new ElasticSearchLogs();
		
		Date dateTo = new Date();
    	SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd'T'HH");
    	formatDate.setTimeZone(TimeZone.getTimeZone("GMT"));
    	String strDateTimeTo = formatDate.format(dateTo);
    	System.out.println(strDateTimeTo);
    	Calendar cal = Calendar.getInstance();
    	cal.add(Calendar.DATE, -30);
    	Date dateFrom = cal.getTime();
    	String strDateTimeFrom = formatDate.format(dateFrom);
    	System.out.println(strDateTimeFrom);
    	com.elastica.beatle.securlets.ESQueryBuilder esqueryBuilder1 = new com.elastica.beatle.securlets.ESQueryBuilder();
    	
		HttpResponse response;
		try {
			int activityCount = 0;
		
			for (int i = 1; i <= 3; i++) {
				
					HashMap<String, String> terms = 	new HashMap<>();
					String payload = esQueryBuilder.getSearchQueryForDisplayLogs(strDateTimeFrom + ":00:00.000Z",
							strDateTimeTo + ":59:59.999Z", query, "Elastica", 100, apiServer, suiteData.getCSRFToken(), suiteData.getSessionID(),
							suiteData.getUsername());

					
				
				Reporter.log("getting investigate logs ::::::  payload::::   " + payload, true);
				response = esLogs.getDisplayLogs(restClient, buildBasicHeaders(suiteData.getCSRFToken(), suiteData.getSessionID()), suiteData.getApiserverHostName(),
						new StringEntity(payload));
				Assert.assertEquals(response.getStatusLine().getStatusCode(), 200, "Response code is not equal");
				String responseBody = getResponseBody(response);
				Reporter.log(" investigate logs ::::::  Response::::   " + responseBody, true);
				activityCount = (Integer) new JSONObject(responseBody).getJSONObject("hits").get("total");
				Reporter.log(String.format(" investigate logs count:::::: %s logs uploaded to elastic search::::   %s",
						activityCount, count), true);
				
				if(activityCount==count||i==3){
					break;
				}else if(activityCount!=count){
					Reporter.log("Injected activities are not equal with the investigate logs, Retrying :::::"+i+" times wait time between each retry is:::: 30 sec");
					Thread.sleep(1*30*1000);
					continue;
					
				}
			}
			Reporter.log(" Expected::: "+count+ " Actual:::::: "+activityCount);
			Assert.assertTrue(activityCount==count, "Number of Activities returned are not Equal with Injected ones");
		
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getListOfIncidents() {
		Reporter.log("Retrieving the logs from Elastic Search ...");
		ElasticSearchLogs esLogs = new ElasticSearchLogs();
		String responseBody = null;
		Date dateTo = new Date();
    	SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd'T'HH");
    	formatDate.setTimeZone(TimeZone.getTimeZone("GMT"));
    	String strDateTimeTo = formatDate.format(dateTo);
    	System.out.println(strDateTimeTo);
    	Calendar cal = Calendar.getInstance();
    	cal.add(Calendar.DATE, -1);
    	Date dateFrom = cal.getTime();
    	String strDateTimeFrom = formatDate.format(dateFrom);
    	System.out.println(strDateTimeFrom);
    	
		HttpResponse response;
		try {
		
				String payload = esQueryBuilder.getSearchQueryForDetect(strDateTimeFrom + ":00:00.000Z",
						strDateTimeTo + ":59:59.999Z");
				response = esLogs.getCloudServiceAnomalies(restClient, buildCookieHeaders(suiteData.getCSRFToken(), suiteData.getSessionID()), new StringEntity(payload),suiteData);
				Assert.assertEquals(response.getStatusLine().getStatusCode(), 200, "Response code is not equal");
				 responseBody = getResponseBody(response);
				Reporter.log(" List of Incidents::::::  Response::::   " + responseBody, true);
		
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return responseBody;
	}
	
	
	public String getListOfIncidentsforDays(int days) {
		Reporter.log("Retrieving the logs from Elastic Search ...");
		ElasticSearchLogs esLogs = new ElasticSearchLogs();
		String responseBody = null;
		Date dateTo = new Date();
    	SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd'T'HH");
    	formatDate.setTimeZone(TimeZone.getTimeZone("GMT"));
    	String strDateTimeTo = formatDate.format(dateTo);
    	System.out.println(strDateTimeTo);
    	Calendar cal = Calendar.getInstance();
    	cal.add(Calendar.DATE, -days);
    	Date dateFrom = cal.getTime();
    	String strDateTimeFrom = formatDate.format(dateFrom);
    	System.out.println(strDateTimeFrom);
    	
		HttpResponse response;
		try {
		
				String payload = esQueryBuilder.getSearchQueryForDetect(strDateTimeFrom + ":00:00.000Z",
						strDateTimeTo + ":59:59.999Z");
				response = esLogs.getCloudServiceAnomalies(restClient, buildCookieHeaders(suiteData.getCSRFToken(), suiteData.getSessionID()), new StringEntity(payload),suiteData);
				Assert.assertEquals(response.getStatusLine().getStatusCode(), 200, "Response code is not equal");
				 responseBody = getResponseBody(response);
				Reporter.log(" List of Incidents::::::  Response::::   " + responseBody, true);
		
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return responseBody;
	}
	
	protected void unblockUser(String facility, String user, String report_id) throws Exception {
		Reporter.log("Retrieving the Detected Users information from Elastic Search ...");
		ElasticSearchLogs esLogs = new ElasticSearchLogs();
		
		List<NameValuePair> queryParams = new ArrayList<NameValuePair>();
		queryParams.add(new BasicNameValuePair("app_action", "NOTHREAT"));
		queryParams.add(new BasicNameValuePair("blocked_apps", facility));	
		queryParams.add(new BasicNameValuePair("email",suiteData.getSaasAppUsername().toLowerCase().replace("%40", "@")));
		queryParams.add(new BasicNameValuePair("report_id", report_id));
		
				HttpResponse response = esLogs.unblockUser(restClient, buildCookieHeaders(), queryParams,suiteData);
				String responseBody = getResponseBody(response);
				System.out.println("Response Body Printing....... : " + responseBody);
				
				
				
	}	

	public void verifyProfile(String user, String ioi_Code) throws Exception{
		
		Reporter.log("       #####################################");
		Reporter.log(" ");
		Logger.info("Verifying the profile :::: ");
		Reporter.log(" ");
		Reporter.log("       #####################################");
		
		ElasticSearchLogs esLogs = new ElasticSearchLogs();
		boolean profileCreated = false;
		outerloop:
		for (int ite=1; ite<=5; ite++) {
		HttpResponse response = esLogs.getState(restClient, user, suiteData.getTenantName());
		String responseBody = getResponseBody(response);
		System.out.println("Response Body : " + responseBody);

		JsonNode arrNode = new ObjectMapper().readTree(getJSONValue(responseBody, "hits")).get("hits");
		
		
		if (arrNode.isArray()) {
			if(arrNode.size()!=0){
		    for (final JsonNode objNode : arrNode) {
		    	JsonNode _score = objNode.path("_score");
		    	System.out.println(_score.asText());
		    	JsonNode _source = objNode.path("_source");
		    	if (_source.has("event_type")) {
		        	System.out.println(_source.path("event_type").asText());
		        	if (_source.path("event_type").asText().equals("ProfileReport") && 
		        			(_source.path("ioi_code").asText().equals(ioi_Code))) {
		        		profileCreated =true;
		        		break outerloop;
		        	}
		        }
		    	}
			}

				Reporter.log("profile not created retrying: user::::: "+user+" ioi Code ::::  "+ioi_Code+"  Wating for "+ite+" minutes" , true);
				if(ite==5){	
				break;
				}
				
				Thread.sleep(1 * 60 * 1000); //Wait for 1 Minute
			
			}
		}
		
		Assert.assertTrue(profileCreated, "profile is not created for user::::: "+user+" ioi Code ::::  "+ioi_Code);
		Reporter.log("       ::::::::::::::::*******************:::::::::::::::::      ");
		Reporter.log("     ");
		Reporter.log("     ");
		Reporter.log("profile got created : user::::: "+user+" ioi Code ::::  "+ioi_Code, true);
		Reporter.log("     ");
		Reporter.log("     ");
		Reporter.log("       ::::::::::::::::*******************:::::::::::::::::      ");
	}
	
	
	
	public String verifyProfile1(String user, String ioi_Code) throws Exception{
		ElasticSearchLogs esLogs = new ElasticSearchLogs();
		boolean profileCreated = false;
		String responseBody = null  ;
		outerloop:
		for (int ite=1; ite<=5; ite++) {
		HttpResponse response = esLogs.getState(restClient, user, suiteData.getTenantName());
		 responseBody = getResponseBody(response);
		System.out.println("Response Body : " + responseBody);

		JsonNode arrNode = new ObjectMapper().readTree(getJSONValue(responseBody, "hits")).get("hits");
		
		
		if (arrNode.isArray()) {
			if(arrNode.size()!=0){
		    for (final JsonNode objNode : arrNode) {
		    	JsonNode _score = objNode.path("_score");
		    	System.out.println(_score.asText());
		    	JsonNode _source = objNode.path("_source");
		    	if (_source.has("event_type")) {
		        	System.out.println(_source.path("event_type").asText());
		        	if (_source.path("event_type").asText().equals("ProfileReport") && 
		        			(_source.path("ioi_code").asText().equals(ioi_Code))) {
		        		profileCreated =true;
		        		break outerloop;
		        	}
		        }
		    	}
			}

				Reporter.log("profile not created retrying: user::::: "+user+" ioi Code ::::  "+ioi_Code+"  Wating for "+ite+" minutes" , true);
				if(ite==5){	
				break;
				}
				
				Thread.sleep(1 * 60 * 1000); //Wait for 1 Minute
			
			}
		}
		
		Assert.assertTrue(profileCreated, "profile is not created for user::::: "+user+" ioi Code ::::  "+ioi_Code);
		Reporter.log("       ::::::::::::::::*******************:::::::::::::::::      ");
		Reporter.log("     ");
		Reporter.log("     ");
		Reporter.log("profile got created : user::::: "+user+" ioi Code ::::  "+ioi_Code, true);
		Reporter.log("     ");
		Reporter.log("     ");
		Reporter.log("       ::::::::::::::::*******************:::::::::::::::::      ");
		
		return responseBody;
	}
	
	
	
	public boolean verifyProfile(String user, String saasApp, String ioi_Code) throws Exception{
		ElasticSearchLogs esLogs = new ElasticSearchLogs();
		boolean profileCreated = false;
		HttpResponse response = esLogs.getState(restClient, user, suiteData.getTenantName());
		String responseBody = getResponseBody(response);
		System.out.println("Response Body : " + responseBody);

		JsonNode arrNode = new ObjectMapper().readTree(getJSONValue(responseBody, "hits")).get("hits");
		
		//TODO::Refacoter this logic add saas app
		if (arrNode.isArray()) {
			if(arrNode.size()!=0){
		    for (final JsonNode objNode : arrNode) {
		    	JsonNode _score = objNode.path("_score");
		    	System.out.println(_score.asText());
		    	JsonNode _source = objNode.path("_source");
		    	if (_source.has("event_type")) {
		        	System.out.println(_source.path("event_type").asText());
		        	if (_source.path("event_type").asText().equals("ProfileReport") && 
		        			(_source.path("ioi_code").asText().equals(ioi_Code))) {
		        		profileCreated =true;
		        	}
		        }
		    	}
			}

			
			}
		
	return profileCreated;
	}
	
	
	public void verifyNoProfile(String user, String ioi_Code) throws Exception{
		ElasticSearchLogs esLogs = new ElasticSearchLogs();
		boolean profileCreated = false;
		outerloop:
		for (int ite=1; ite<=2; ite++) {
		HttpResponse response = esLogs.getState(restClient, user, suiteData.getTenantName());
		String responseBody = getResponseBody(response);
		System.out.println("Response Body : " + responseBody);

		JsonNode arrNode = new ObjectMapper().readTree(getJSONValue(responseBody, "hits")).get("hits");
		
		
		if (arrNode.isArray()) {
			if(arrNode.size()!=0){
		    for (final JsonNode objNode : arrNode) {
		    	JsonNode _score = objNode.path("_score");
		    	System.out.println(_score.asText());
		    	JsonNode _source = objNode.path("_source");
		    	if (_source.has("event_type")) {
		        	System.out.println(_source.path("event_type").asText());
		        	if (_source.path("event_type").asText().equals("ProfileReport") && 
		        			(_source.path("ioi_code").asText().equals(ioi_Code))) {
		        		profileCreated =true;
		        		break outerloop;
		        	}
		        }
		    	}
			}

				Reporter.log("profile not created retrying: user::::: "+user+" ioi Code ::::  "+ioi_Code+"  Wating for "+ite+" minutes" , true);
				if(ite==2){	
				break;
				}
				
				Thread.sleep(1 * 60 * 1000); //Wait for 1 Minute
			
	        
		}
		}
		
		Assert.assertFalse(profileCreated, "Profile is  AVAILABLE for user::::: "+user+" ioi Code ::::  "+ioi_Code);
		Reporter.log("       ::::::::::::::::*******************:::::::::::::::::      ");
		Reporter.log("     ");
		Reporter.log("     ");
		Reporter.log("profile got created : user::::: "+user+" ioi Code ::::  "+ioi_Code, true);
		Reporter.log("     ");
		Reporter.log("     ");
		Reporter.log("       ::::::::::::::::*******************:::::::::::::::::      ");
	}
	
	
	@SuppressWarnings("unchecked")
	public String verifyStateIncidents(String user, HashSet<String> expIncidents) {
		Reporter.log("Retrieving the Detected Users information from Elastic Search ...");
		
		ElasticSearchLogs esLogs = new ElasticSearchLogs();
		HashSet<String> expDetectIncidents = new HashSet<String>();
		HashSet<String> expAnomalyIncidents = new HashSet<String>();
		Boolean matching = false;
		String responseBody =null;
		
		for (int ite=1; ite<=10; ite++) {
			
			HashSet<String> detectIncidents = new HashSet<String>();
			HashSet<String> anomalyIncidents = new HashSet<String>();
			expDetectIncidents = (HashSet<String>) expIncidents.clone();
			expAnomalyIncidents = (HashSet<String>) expIncidents.clone();
			
			try {
				HttpResponse response = esLogs.getState(restClient, user, suiteData.getTenantName());
				Assert.assertEquals(response.getStatusLine().getStatusCode(), 200, "Response code is not equal");
				 responseBody = getResponseBody(response);
				 Reporter.log(" Get state ::::::  Response::::   "+responseBody , true);
				
				JsonNode arrNode = new ObjectMapper().readTree(getJSONValue(responseBody, "hits")).get("hits");

				if (arrNode.isArray()) {
					for (final JsonNode objNode : arrNode) {
						
						JsonNode _source = objNode.path("_source");
						if (_source.path("event_type").asText().equals("DetectorReport") && _source.has("ioi_code")) {
							detectIncidents.add(_source.path("ioi_code").asText());
							 Reporter.log(_source.path("ioi_code").asText() , true);
						}
						if (_source.path("event_type").asText().equals("AnomalyReport") && _source.has("ioi_code")) {
							anomalyIncidents.add(_source.path("ioi_code").asText());
							 Reporter.log(_source.path("ioi_code").asText() , true);
						}
					}
				}
				Boolean detectMatch = false;
				Boolean anomalyMatch = false;
				if (detectIncidents.containsAll(expDetectIncidents)) {
					detectMatch = true;
					System.out.println("Results are Matching for DetectReport");
				} else {
					expDetectIncidents.removeAll(detectIncidents);
				}
				if (anomalyIncidents.containsAll(expAnomalyIncidents)) {
					anomalyMatch = true;
					System.out.println("Results are Matching for AnomalyReport");
				} else {
					expAnomalyIncidents.removeAll(detectIncidents);
				}
				if (detectMatch && anomalyMatch) {
					matching = true;
					
					break;
				} else {
					Reporter.log("The Anomaly Incidents NOT created but expected:" + expAnomalyIncidents, true);
					Reporter.log("The Detect Incidents NOT created but expected:" + expDetectIncidents, true);
					if(ite==20){	
							break;
					}
					Reporter.log("The Anomaly Incidents NOT created Retrying:::::::::: " +ite+" times with 1 minute of wait time in each retry", true);
					Thread.sleep(1 * 60 * 1000); //Wait for 1 Minute
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		Reporter.log("Going to Assert after 20 minutes of waiting", true);
		if(!matching){
		Reporter.log( "Test failed because Incidents " + expDetectIncidents + " in DetectorReport and " + expAnomalyIncidents + " in AnomalyReport are NOT created.",true);
		}
		Assert.assertTrue(matching, "Incidents " + expDetectIncidents + " in DetectorReport and " + expAnomalyIncidents + " in AnomalyReport are NOT created.");
		return responseBody;
	}
	
	@SuppressWarnings("unchecked")
	public String verifyStateIncident(String user, HashSet<String> expIncidents) {
		Reporter.log("Retrieving the Detected Users information from Elastic Search ...");
		
		ElasticSearchLogs esLogs = new ElasticSearchLogs();
		HashSet<String> expDetectIncidents = new HashSet<String>();
		HashSet<String> expAnomalyIncidents = new HashSet<String>();
		Boolean matching = false;
		String responseBody =null;
		
		for (int ite=0; ite<5; ite++) {
			
			HashSet<String> detectIncidents = new HashSet<String>();
			HashSet<String> anomalyIncidents = new HashSet<String>();
			expDetectIncidents = (HashSet<String>) expIncidents.clone();
			expAnomalyIncidents = (HashSet<String>) expIncidents.clone();
			
			try {
				HttpResponse response = esLogs.getState(restClient, user, suiteData.getTenantName());
				Assert.assertEquals(response.getStatusLine().getStatusCode(), 200, "Response code is not equal");
				 responseBody = getResponseBody(response);
				 Reporter.log(" Get state ::::::  Response::::   "+responseBody , true);
				
				JsonNode arrNode = new ObjectMapper().readTree(getJSONValue(responseBody, "hits")).get("hits");

				if (arrNode.isArray()) {
					for (final JsonNode objNode : arrNode) {
						JsonNode _source = objNode.path("_source");
						if (_source.path("event_type").asText().equals("DetectorReport") && _source.has("ioi_code")) {
							detectIncidents.add(_source.path("ioi_code").asText());
							 Reporter.log(_source.path("ioi_code").asText() , true);
						}
						if (_source.path("event_type").asText().equals("AnomalyReport") && _source.has("ioi_code")) {
							anomalyIncidents.add(_source.path("ioi_code").asText());
							 Reporter.log(_source.path("ioi_code").asText() , true);
						}
					}
				}
				Boolean detectMatch = false;
				Boolean anomalyMatch = false;
				if (detectIncidents.containsAll(expDetectIncidents)) {
					detectMatch = true;
					System.out.println("Results are Matching for DetectReport");
				} else {
					expDetectIncidents.removeAll(detectIncidents);
				}
				if (anomalyIncidents.containsAll(expAnomalyIncidents)) {
					anomalyMatch = true;
					System.out.println("Results are Matching for AnomalyReport");
				} else {
					expAnomalyIncidents.removeAll(detectIncidents);
				}
				if (detectMatch && anomalyMatch) {
					matching = true;
					
					break;
				} else {
					System.out.println("The Anomaly Incidents NOT created but expected:" + expAnomalyIncidents);
					System.out.println("The Detect Incidents NOT created but expected:" + expDetectIncidents);
					if(ite==4){	
					break;
					}
					
					Thread.sleep(1 * 60 * 1000); //Wait for 1 Minute
				}
				
				
				
				
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		Assert.assertTrue(matching, "Incidents " + expDetectIncidents + " in DetectorReport and " + expAnomalyIncidents + " in AnomalyReport are NOT created.");
		return responseBody;
	}
	
	
	public void validateTBIIncidents(String responseBody, AttributeBean bean,InputBean inputBean,  Set<String> ioiCodes) throws Exception {
		 SoftAssert asert=new SoftAssert();
		String[] event_type = {"AnomalyReport","DetectorReport"};
		
		System.out.println(responseBody);
		
		Assert.assertNotNull(responseBody, "responce body is null");
		
		asert.assertEquals(getJSONValue(responseBody, "timed_out"), "false", "incidents are not created");
		asert.assertNotNull(getJSONValue(responseBody, "_shards"), "incidents are not created");
		
		
		JsonNode shards = new ObjectMapper().readTree(getJSONValue(responseBody, "_shards"));
		asert.assertNotNull(getJSONValue(shards.toString(), "total"), "incidents are not created");
		asert.assertNotNull(getJSONValue(shards.toString(), "successful"), "incidents are not created");
		asert.assertNotNull(getJSONValue(shards.toString(), "failed"), "incidents are not created");
		
		
		asert.assertNotNull(getJSONValue(responseBody, "hits"), "incidents are not created");
		JsonNode Hits = new ObjectMapper().readTree(getJSONValue(responseBody, "hits"));
		asert.assertNotNull(getJSONValue(Hits.toString(), "total"), "incidents are not created");
		asert.assertNotNull(getJSONValue(Hits.toString(), "max_score"), "max_score is null");
		
		float max_score = Float.parseFloat(getJSONValue(Hits.toString(), "max_score"));
		System.out.println("max_score ::::::  "+max_score);
		asert.assertTrue(max_score>0, "max score is less thean 5, check theartscore failed");
		
		
		JsonNode hits = new ObjectMapper().readTree(getJSONValue(Hits.toString(), "hits"));
		
		if (hits.isArray()) {
		    for (final JsonNode objNode : hits) {
		    	
		    	asert.assertNotNull(getJSONValue(objNode.toString(), "_id"), "_id is null");
		    	asert.assertNotNull(getJSONValue(objNode.toString(), "_score"), "_score is null");
				
				JsonNode source = new ObjectMapper().readTree(getJSONValue(objNode.toString(), "_source"));
				
				
				asert.assertNotNull(getJSONValue(source.toString(), "ioi_code"), "IOI_CODE is null");
				String ioi_code = getJSONValue(source.toString(), "ioi_code").toString().replace("\"", "");
				asert.assertTrue(ioiCodes.contains(getJSONValue(source.toString(), "ioi_code").toString().replace("\"", "")), "IOI_CODE is not matching with expected");
				
				asert.assertNotNull(getJSONValue(source.toString(), "event_type"), "event_type is null");
				String eventType = getJSONValue(source.toString(), "event_type").toString().replace("\"", "");
				asert.assertTrue(Arrays.asList(event_type).contains(eventType), "event_type is not matching with AnomalyReport/DetectorReport");
				
				
				asert.assertNotNull(getJSONValue(source.toString(), "updated_timestamp"), "updated_timestamp is null");
				//TODO::Need to check the severity
				asert.assertNotNull(getJSONValue(source.toString(), "severity"), "severity is null");
				asert.assertNotNull(getJSONValue(source.toString(), "message"), "message is null");
				
				
				if(eventType.equals("AnomalyReport")){
					
					asert.assertNotNull(getJSONValue(source.toString(), "user_name"), "user_name is null");
					asert.assertEquals(getJSONValue(source.toString(), "user_name").toString().replace("\"", ""), inputBean.getUserName(), "userName is not matching with expected user-name");
					
					asert.assertNotNull(getJSONValue(source.toString(), "user"), "user is null");
					Reporter.log("AnomalyReport user  :::::::  "+getJSONValue(source.toString(), "user"), true);
					asert.assertEquals(getJSONValue(source.toString(), "user").toString().replace("\"", ""), inputBean.getUser(), "user is null");
					//FIXME:::
					String responsible_logs = getJSONValue(source.toString(), "responsible_logs");
					if(responsible_logs==null){
						Reporter.log("AnomalyReport :::::::  responsible_logs is null  :::::::responce  "+responseBody, true);
						asert.assertNotNull(responsible_logs, "responsible_logs is null");
						JsonNode res_logs = new ObjectMapper().readTree(getJSONValue(source.toString(), "responsible_logs"));
						if (res_logs.isArray()) {
							asert.assertTrue(res_logs.size()>=inputBean.getCount(), "res_logs are not more than threshold");
						}
					}
					
						
					for(IOI_Code code :IOI_Code.values()){
						if(code.toString().equals(ioi_code)){
							String m = code.getMessage();
							
							String m1=	m.replaceAll("9", String.valueOf(bean.getThreshold()-1));
							String m2  = m1.replaceAll("60", String.valueOf(bean.getWindow()*60));
							String m3  = m2.replaceAll("41", String.valueOf(inputBean.getCount()-bean.getThreshold()));
							String message  = m3.replaceAll("1.777", String.valueOf(Float.parseFloat(String.valueOf(bean.getThreshold()))-0.001));
							String message1 =null;
							if(inputBean.getCount()-bean.getThreshold()==1){
								message1  = message.replaceAll("violations", "violation");
								Reporter.log("Constructed message :::::::  "+message1, true);
								String msgFromLog = getJSONValue(source.toString(), "message").replace("\"", "");
								Reporter.log("AnomalyReport  message from logs  :::::::  "+msgFromLog, true);
								Reporter.log(" AnomalyReport message matching? ::::: "+msgFromLog.equalsIgnoreCase(message1), true);
								if(!ioi_code.equalsIgnoreCase(IOI_Code.TOO_MANY_POLICY_VIOLATIONS.toString())){
							//	Assert.assertEquals(msgFromLog, message1, "AnomalyReport message is not generated properly");
								}
							}else{
								Reporter.log("Constructed message :::::::  "+message, true);
								String msgFromLog = getJSONValue(source.toString(), "message").replace("\"", "");
								Reporter.log("AnomalyReport  message from logs  :::::::  "+msgFromLog, true);
								Reporter.log("AnomalyReport message matching? ::::: "+msgFromLog.equalsIgnoreCase(message), true);
								//Assert.assertEquals(msgFromLog,message, "AnomalyReport message is not generated properly");
							}
							
						}
					}
				}
				if(eventType.equals("DetectorReport")){
					
					asert.assertNotNull(getJSONValue(source.toString(), "user_name"), "user_name is null");
					asert.assertEquals(getJSONValue(source.toString(), "user_name").toString().replace("\"", ""), inputBean.getUserName(), "userName is not matching with expected user-name");
					
					asert.assertNotNull(getJSONValue(source.toString(), "user"), "user is null");
					Reporter.log("DetectorReport user  :::::::  "+getJSONValue(source.toString(), "user"), true);
					asert.assertEquals(getJSONValue(source.toString(), "user").toString().replace("\"", ""), inputBean.getUser(), "user is null");
					
				String responsible_logs =	getJSONValue(source.toString(), "responsible_logs");
				if(responsible_logs!=null){
					Reporter.log("DetectorReport :::::::  responsible_logs is null  :::::::responce  "+responseBody, true);
					asert.assertNotNull(responsible_logs, "responsible_logs is null");
					JsonNode res_logs = new ObjectMapper().readTree(getJSONValue(source.toString(), "responsible_logs"));
					if (res_logs.isArray()) {
						asert.assertTrue(res_logs.size()>=inputBean.getCount()-bean.getThreshold(), "res_logs are not more than threshold");
					}
				}
					
					
					for(IOI_Code code :IOI_Code.values()){
						if(code.toString().equals(ioi_code)){
							String m = code.getMessage();
							
							String m1=	m.replaceAll("9", String.valueOf(bean.getThreshold()-1));
							String m2  = m1.replaceAll("60", String.valueOf(bean.getWindow()*60));
							String m3  = m2.replaceAll("41", String.valueOf(inputBean.getCount()-bean.getThreshold()));
							String message  = m3.replaceAll("1.777", String.valueOf(Float.parseFloat(String.valueOf(bean.getThreshold()))-0.001));
							String message1 =null;
							if(inputBean.getCount()-bean.getThreshold()==1){
								message1  = message.replaceAll("violations", "violation");
								Reporter.log("Constructed message :::::::  "+message1, true);
								String msgFromLog = getJSONValue(source.toString(), "message").replace("\"", "");
								Reporter.log("DetectorReport  message from logs  :::::::  "+msgFromLog, true);
								Reporter.log(" DetectorReport message matching? ::::: "+msgFromLog.equalsIgnoreCase(message1), true);
								if(!ioi_code.equalsIgnoreCase(IOI_Code.TOO_MANY_POLICY_VIOLATIONS.toString())){
									//Assert.assertEquals(msgFromLog, message1, "DetectorReport message is not generated properly");
								}
							}else{
								Reporter.log("Constructed message :::::::  "+message, true);
								String msgFromLog = getJSONValue(source.toString(), "message").replace("\"", "");
								Reporter.log("DetectorReport  message from logs  :::::::  "+msgFromLog, true);
								Reporter.log("DetectorReport message matching? ::::: "+msgFromLog.equalsIgnoreCase(message), true);
								//Assert.assertEquals(msgFromLog,message, "DetectorReport message is not generated properly");
							}
							
						}
					}
				}
		    }
		}
		asert.assertAll();
		
	}

	@SuppressWarnings("unchecked")
	public void verifyNoIncidents(String user) throws Exception {
		Reporter.log("Retrieving the Detected Users information from Elastic Search ...");
		
		ElasticSearchLogs esLogs = new ElasticSearchLogs();
		
				HttpResponse response = esLogs.getState(restClient, user, suiteData.getTenantName());
				String responseBody = getResponseBody(response);
				System.out.println(responseBody);
				for (int ite=0; ite<3; ite++) {
				JsonNode arrNode = new ObjectMapper().readTree(getJSONValue(responseBody, "hits")).get("hits");
				Thread.sleep(1* 10 * 1000);	
			String message =	"arrNode::::"+arrNode.toString()+" size " +arrNode.size();
				Reporter.log("expecting no incidents  create"+message, true);
				Assert.assertTrue(arrNode.size()==0, "incidents created");
				
				}
	}
	
	@SuppressWarnings("unchecked")
	public void verifyIncidentCleared(String user) throws Exception {
		Reporter.log("Retrieving the Detected Users information from Elastic Search ...");
		
		ElasticSearchLogs esLogs = new ElasticSearchLogs();
		try {
			HttpResponse response = esLogs.getState(restClient, user, suiteData.getTenantName());
			Assert.assertEquals(response.getStatusLine().getStatusCode(), 200, "Response code is not equal");
			String  responseBody = getResponseBody(response);
			 Reporter.log(" Get state ::::::  Response::::   "+responseBody , true);
			
			JsonNode arrNode = new ObjectMapper().readTree(getJSONValue(responseBody, "hits")).get("hits");

			if (arrNode.isArray()) {
				for (final JsonNode objNode : arrNode) {
					
					JsonNode _source = objNode.path("_source");
					
					if (_source.path("event_type").asText().equals("AnomalyReport") && _source.has("ioi_code")) {
						JsonNode	notes =	_source.path("notes");
						
						if(notes.path("note").asText().equals("User changed'Verified Alert?' from Unknown to Yes(Cleared Alert)")){
							 Reporter.log("User changed'Verified Alert?' from Unknown to Yes(Cleared Alert)");
						}else{
							
						}
						
						
						
					}
				}
			}
		
				Thread.sleep(1 * 60 * 1000); //Wait for 1 Minute
	
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
				
				}
	
	
	
	@SuppressWarnings("unchecked")
	public void verifyThreatscoreIncidets(String user, HashSet<String> expIncidents) {
		Reporter.log("Retrieving the Detected Users information from Elastic Search ...");
		ElasticSearchLogs esLogs = new ElasticSearchLogs();
		HashSet<String> tmpIncidents = new HashSet<String>();
		
		Boolean matching = false;
		for (int ite=1; ite<=5; ite++) {
			
			HashSet<String> actIncidents = new HashSet<String>();
			tmpIncidents = (HashSet<String>) expIncidents.clone();
			
			try {
				HttpResponse response = esLogs.getThreatscore(restClient, user, suiteData.getTenantName());
				Assert.assertEquals(response.getStatusLine().getStatusCode(), 200, "Response code is not equal");
				String responseBody = getResponseBody(response);
				Reporter.log("get threat score responce::::::     " + responseBody, true);
				
				
				JsonNode arrNode;
				arrNode = new ObjectMapper().readTree(getJSONValue(responseBody, "hits")).get("hits");

				if (arrNode.isArray()) {
					for (final JsonNode objNode : arrNode) {
						JsonNode _source = objNode.path("_source");
						if (_source.has("ioi_code")) {
							System.out.println(_source.path("ioi_code").asText());
							actIncidents.add(_source.path("ioi_code").asText());
						}
					}
				}
				if (actIncidents.size() == tmpIncidents.size() && actIncidents.containsAll(tmpIncidents)) {
					matching = true;
					System.out.println("Results are Matching");
					break;
				} else {
					tmpIncidents.removeAll(actIncidents);
					Reporter.log("The Incidents NOT created but expected:" + tmpIncidents);
					if(ite==5){
						
						break;
					}
					Reporter.log("Incident is not listed under Detect user page, Retrying :::::"+ite+" times wait time between each retry is:::: 60 sec");
					Thread.sleep(1* 60 * 1000);	//Wait for 1 Minute
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		Reporter.log("Going to Assert after 5 minutes of waiting", true);
		if(!matching){
		Reporter.log( "Test failed because Incidents " + tmpIncidents +"  are NOT created.",true);
		}
		Assert.assertTrue(matching, "Incidents " + tmpIncidents + "are NOT created.");
	}

	/**
	 * 
	 * @param <T>
	 * @param data
	 * @param klass
	 * @return
	 * @throws JAXBException 
	 */
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
	
	
	protected void scpAndInjectActivities(Method method, InputBean inputBean)
			throws InterruptedException, Exception, IOException, JsonProcessingException, UnsupportedEncodingException {
		
		ESScripts esScripts = new ESScripts();

		String fileName = inputBean.getFileName();

		String consoleLogs;
		consoleLogs = esScripts.scpActivityLogs(fileName, suiteData.getGoldenInputFilePath(),
				suiteData.getEsScriptsHostName(), suiteData.getEsScriptsUserName());
		System.out.println("Console Log after scp :" + consoleLogs);
		Assert.assertEquals(consoleLogs, "", "File scp Failed!");
		

		consoleLogs = esScripts.injectActivityLogs(fileName, suiteData.getEsScriptsHostName(),
				suiteData.getEsScriptsUserName());
		Reporter.log("Console Log after InjectActivities :" + consoleLogs , true);
		Assert.assertTrue(
				consoleLogs.toLowerCase().contains(
						("Wrote " + inputBean.getCount() + " records of " + inputBean.getCount()).toLowerCase()),
				"Injecting Activities to ES failed");
		/**/
		Reporter.log("Sucessfully uploaded  Activity logs to elastic search waiting for 1 minute::::::     " , true);
	
	}
	
	protected void scpActivityLogsAndValidate(Method method, InputBean inputBean)
			throws InterruptedException, Exception, IOException, JsonProcessingException, UnsupportedEncodingException {
		
		ESScripts esScripts = new ESScripts();

		String fileName = inputBean.getFileName();

		String consoleLogs;
		consoleLogs = esScripts.scpActivityLogs(fileName, suiteData.getGoldenInputFilePath(),
				suiteData.getEsScriptsHostName(), suiteData.getEsScriptsUserName());
		System.out.println("Console Log after scp :" + consoleLogs);
		Assert.assertEquals(consoleLogs, "", "File scp Failed!");
		

		consoleLogs = esScripts.injectActivityLogs(fileName, suiteData.getEsScriptsHostName(),
				suiteData.getEsScriptsUserName());
		Reporter.log("Console Log after InjectActivities :" + consoleLogs , true);
		Assert.assertTrue(
				consoleLogs.toLowerCase().contains(
						("Wrote " + inputBean.getCount() + " records of " + inputBean.getCount()).toLowerCase()),
				"Injecting Activities to ES failed");
		/**/
		Reporter.log("Sucessfully uploaded  Activity logs to elastic search waiting for 1 minute::::::     " , true);
		Thread.sleep(1* 30* 1000);
		Long count = inputBean.getCount();

		verifyActivityCount(inputBean.getTestId(), Integer.parseInt(String.valueOf(count)));

		
		
	}
	
	protected void scpActivityLogsAndValidate1(Method method, InputBean inputBean)
			throws InterruptedException, Exception, IOException, JsonProcessingException, UnsupportedEncodingException {
		
		ESScripts esScripts = new ESScripts();

		String fileName = inputBean.getFileName();

		String consoleLogs;
		consoleLogs = esScripts.scpActivityLogs(fileName, suiteData.getGoldenInputFilePath(),
				suiteData.getEsScriptsHostName(), suiteData.getEsScriptsUserName());
		System.out.println("Console Log after scp :" + consoleLogs);
		Assert.assertEquals(consoleLogs, "", "File scp Failed!");
		

		consoleLogs = esScripts.injectActivityLogs(fileName, suiteData.getEsScriptsHostName(),
				suiteData.getEsScriptsUserName());
		Reporter.log("Console Log after InjectActivities :" + consoleLogs , true);
		
		/**/
		Reporter.log("Sucessfully uploaded  Activity logs to elastic search waiting for 1 minute::::::     " , true);
		Thread.sleep(1* 30* 1000);
		Long count = inputBean.getCount();

		verifyActivityCount(inputBean.getTestId(), Integer.parseInt(String.valueOf(count)));

		
		
	}
	
	protected void updateDetectAttributes(boolean enabled, AttributeBean attributeBean,
			org.json.JSONArray getResponseArray, String ioi_Code)
					throws JSONException, Exception, UnsupportedEncodingException, JAXBException {
		HttpResponse resp;
		List<Objects> objects = new ArrayList<>();
		for (int index = 0; index < getResponseArray.length(); index++) {
			JSONObject attributeObject = getResponseArray.getJSONObject(index);
			
			if (ioi_Code.toString().equals((String) attributeObject.get(NAME))) {
				
				String uri = (String) attributeObject.get(RESOURCE_URI);
				Objects object = new Objects();
				
				object.setEnabled(enabled);
				object.setConfidence(10);
				object.setImportance(attributeBean.getImportance());
				object.setThreshold(attributeBean.getThreshold());
				object.setWindow(attributeBean.getWindow());
				object.setResource_uri(uri);
				objects.add(object);
			}
		}
		
		postAttributes( objects);
		Reporter.log("setting preferences waiting for 1 min",true);
		//Thread.sleep(1* 60* 1000);
		Reporter.log("");
		Reporter.log("updated preferences ::::::     threshold=" + attributeBean.getThreshold()  + ", window=" + attributeBean.getWindow() + ", importance=" + attributeBean.getImportance()
				+ ", enabled=" + enabled , true);
		Reporter.log("");
		
	}
	
	
	
	
	protected void updateDetectAttributesForTBI(boolean enabled, AttributeBean attributeBean,
			org.json.JSONArray getResponseArray, String ioi_Code)
					throws JSONException, Exception, UnsupportedEncodingException, JAXBException {
		HttpResponse resp;
		List<Objects> objects = new ArrayList<>();
		for (int index = 0; index < getResponseArray.length(); index++) {
			JSONObject attributeObject = getResponseArray.getJSONObject(index);
			
			if (ioi_Code.toString().equals((String) attributeObject.get(NAME))) {
				
				String uri = (String) attributeObject.get(RESOURCE_URI);
				Objects object = new Objects();
				
				object.setEnabled(enabled);
				object.setConfidence(10);
				object.setImportance(attributeBean.getImportance());
				object.setThreshold(attributeBean.getThreshold());
				object.setWindow(attributeBean.getWindow());
				object.setResource_uri(uri);
				objects.add(object);
			}else{
				String uri = (String) attributeObject.get(RESOURCE_URI);
				Objects object = new Objects();
				object.setEnabled(false);
				object.setConfidence(10);
				object.setImportance(attributeBean.getImportance());
				object.setThreshold(attributeBean.getThreshold());
				object.setWindow(attributeBean.getWindow());
				object.setResource_uri(uri);
				objects.add(object);
			
			}
		}
		
		postAttributes( objects);
		Reporter.log("setting preferences waiting for 1 min",true);
		Thread.sleep(1* 60* 1000);
		Reporter.log("");
		Reporter.log("updated preferences ::::::     threshold=" + attributeBean.getThreshold()  + ", window=" + attributeBean.getWindow() + ", importance=" + attributeBean.getImportance()
				+ ", enabled=" + enabled , true);
		Reporter.log("");
		
	}
	
	protected void updateDetectAttributes(boolean enabled, AttributeBean attributeBean,
			org.json.JSONArray getResponseArray, String[] ioi_Codes)
					throws JSONException, Exception, UnsupportedEncodingException, JAXBException {
		HttpResponse resp;
		List<Objects> objects = new ArrayList<>();
		for (int index = 0; index < getResponseArray.length(); index++) {
			JSONObject attributeObject = getResponseArray.getJSONObject(index);
			
			if (Arrays.asList(ioi_Codes).contains(((String) attributeObject.get(NAME)))) {
				
				String uri = (String) attributeObject.get(RESOURCE_URI);
				Objects object = new Objects();
				
				object.setEnabled(enabled);
				object.setConfidence(10);
				object.setImportance(attributeBean.getImportance());
				object.setThreshold(attributeBean.getThreshold());
				object.setWindow(attributeBean.getWindow());
				object.setResource_uri(uri);
				objects.add(object);
			}
		}
		
		postAttributes( objects);
		Thread.sleep(1* 30* 1000);
	}
	
	
	
	
	protected String  updateBBIAttributes(boolean enabled, AttributeBean attributeBean,
			org.json.JSONArray getResponseArray, DetectAttributeDto detectAttributeDto, String ioi_Code)
					throws JSONException, Exception, UnsupportedEncodingException {
		
		List<Objects> objects = new ArrayList<>();
		for (int index = 0; index < getResponseArray.length(); index++) {
			JSONObject attributeObject = getResponseArray.getJSONObject(index);
			
			if (ioi_Code.toString().equals((String) attributeObject.get(NAME))) {
				
				String uri = (String) attributeObject.get(RESOURCE_URI);
				Objects object = new Objects();
				object.setEnabled(enabled);
				object.setConfidence(attributeBean.getConfidence());
				object.setImportance(attributeBean.getImportance());
				object.setResource_uri(uri);
				objects.add(object);
				
			}else{
				Objects object = new Objects();
				String uri = (String) attributeObject.get(RESOURCE_URI);
				object.setEnabled(false);
				object.setConfidence(attributeBean.getConfidence());
				object.setImportance(attributeBean.getImportance());
				object.setResource_uri(uri);
				objects.add(object);
				
			}
		}
		
		String responseBody = postAttributes(objects);
		Reporter.log("Response Body after postAttributes : " + responseBody, true);
		Reporter.log("setting preferences waiting for 1 min",true);
		Thread.sleep(1* 60* 1000);
		Reporter.log("");
		Reporter.log("updated preferences ::::::     confidence=" + attributeBean.getConfidence()  + ", importance=" + attributeBean.getImportance()
				+ ", enabled=" + enabled , true);
		Reporter.log("");
		
		return responseBody;
	}
	
	
	protected void updateBBIAttributes1(boolean enabled, AttributeBean attributeBean,
			org.json.JSONArray getResponseArray, String[] ioi_Codes)
					throws JSONException, Exception, UnsupportedEncodingException {
		HttpResponse resp;
		List<Objects> objects = new ArrayList<>();
		for (int index = 0; index < getResponseArray.length(); index++) {
			JSONObject attributeObject = getResponseArray.getJSONObject(index);
			
			if (Arrays.asList(ioi_Codes).contains(((String) attributeObject.get(NAME)))) {
				
				String uri = (String) attributeObject.get(RESOURCE_URI);
				Objects object = new Objects();
				object.setEnabled(enabled);
				object.setConfidence(attributeBean.getConfidence());
				object.setImportance(attributeBean.getImportance());
				object.setResource_uri(uri);
				objects.add(object);
			}
		}
		
		postAttributes(objects);
		Reporter.log("setting preferences waiting for 1 min",true);
		Thread.sleep(1* 45* 1000);
		Reporter.log("");
		Reporter.log("updated preferences ::::::     confidence=" + attributeBean.getConfidence()  + ", importance=" + attributeBean.getImportance()
				+ ", enabled=" + enabled , true);
		Reporter.log("");
	}
	
	protected void updateDetectAttributes()
					throws JSONException, Exception, UnsupportedEncodingException, JAXBException {
		HttpResponse resp;
		resp  = getDetectAttributes();
		String responseBody = getResponseBody(resp);
		org.json.JSONArray getResponseArray = new JSONObject(responseBody).getJSONArray(OBJECTS);
		List<Objects> objects = new ArrayList<>();
		for (int index = 0; index < getResponseArray.length(); index++) {
			JSONObject attributeObject = getResponseArray.getJSONObject(index);
			
				
				String uri = (String) attributeObject.get(RESOURCE_URI);
				Objects object = new Objects();
				
				object.setEnabled(false);
				object.setConfidence(90);
				object.setImportance(1);
				object.setThreshold(10);
				object.setWindow(10);
				object.setResource_uri(uri);
				objects.add(object);
				
				
		}
		postAttributes( objects);
	}
	
	

	protected String postAttributes(List<Objects> objects)
			throws Exception, UnsupportedEncodingException, JAXBException {
		
		HttpResponse resp;
		DetectAttributeDto detectAttributeDto = new DetectAttributeDto();
		detectAttributeDto.setObjects(objects);
		URI getAttributesURI = postDetectattributes();
		StringEntity se = new StringEntity(utils.marshall(detectAttributeDto));
		Reporter.log("detectAttributes::: "+utils.marshall(detectAttributeDto));
		se.setContentType(APPLICATION_JSON);
		resp = restClient.doPost(getAttributesURI, buildCookieHeaders(), null, se);
		
		String responseBody = ClientUtil.getResponseBody(resp);
		Reporter.log("POST AttributesPreferences response:" + responseBody, true);
		
		Assert.assertEquals(resp.getStatusLine().getStatusCode(), 200, "Response code is not equal");
		
		return responseBody;
	}

	
	
	
	protected void updateDetectAttributesFromFile(String fileName)
					throws JSONException, Exception, UnsupportedEncodingException {
		HttpResponse resp;
		
		String tmplFilePath = suiteData.getGoldenInputTmplPath();
		//attributes.json
		String attributes = readFile(tmplFilePath + "attributes.json");
		
		
		URI getAttributesURI = postDetectattributes();
		StringEntity se = new StringEntity(attributes.toString());
		System.out.println("detectAttributeDto::: "+attributes);
		se.setContentType(APPLICATION_JSON);
		resp = restClient.doPost(getAttributesURI, buildCookieHeaders(), null, se);
		Assert.assertEquals(resp.getStatusLine().getStatusCode(), 200, "Response code is not equal");
	}
	
	
	protected String readFile(String fileName) throws IOException {
		
	    BufferedReader reader = new BufferedReader( new FileReader(fileName));
	    String         line = null;
	    StringBuilder  stringBuilder = new StringBuilder();
	    String         ls = System.getProperty("line.separator");

	    while( ( line = reader.readLine() ) != null ) {
	        stringBuilder.append( line );
	        stringBuilder.append( ls );
	    }

	    return stringBuilder.toString();
	}
	
	protected int verifyDetectedUser(String user) {
		Reporter.log("Retrieving the Detected Users information from Elastic Search ...");
		ElasticSearchLogs esLogs = new ElasticSearchLogs();
		
		List<NameValuePair> queryParams = new ArrayList<NameValuePair>();
		queryParams.add(new BasicNameValuePair("source", "{\"query\":{\"filtered\":{\"query\":{\"bool\":{\"must\":[{\"term\":{\"facility\":\"_ALL\"}}]}},\"filter\":{\"and\":[{\"or\":[{\"term\":{\"__source\":\"\"}},{\"term\":{\"__source\":\"GW\"}},{\"term\":{\"__source\":\"API\"}},{\"missing\":{\"field\":\"__source\"}}]}]}}},\"sort\":{\"threat_score\":{\"order\":\"desc\",\"ignore_unmapped\":\"true\"}},\"size\":100000}"));
		queryParams.add(new BasicNameValuePair("sourceName", "DETECT"));		
		int rThreatScore = 0;
		Boolean foundUser = false;
		int ite=1;
		String rUser =null;
		for (; ite<=10; ite++) {
			try {
				HttpResponse response = esLogs.getUserScoreAnomalies(restClient, this.buildCookieHeaders(), suiteData.getHost(),queryParams);
				String responseBody = getResponseBody(response);
				Reporter.log("Response Body  ::::: " + responseBody, true);
				
				
				JsonNode hits = new ObjectMapper().readTree(getJSONValue(getJSONValue(responseBody, "hits"), "hits"));
				
				
				
				if (hits.isArray()) {
				    for (final JsonNode objNode : hits) {
						
						JsonNode source = new ObjectMapper().readTree(getJSONValue(objNode.toString(), "_source"));
					rUser = getJSONValue(source.toString(), "user");
					rThreatScore =  Integer.parseInt(getJSONValue(source.toString(), "threat_score"));
					//System.out.println("Msg Printing....." + rUser + " " + rThreatScore);
					if (rUser.equalsIgnoreCase("\"" + user + "\"")) {
						foundUser = true;
						break;
					}
				}
				
				if (foundUser) {
					break;
				} else {
					Thread.sleep(1 * 60 * 1000);	//wait for 1 minute.
					Reporter.log("Detect user is not found in the detect page Retrying::::::: " + ite + "Minutes of waiting");
				}
			} }catch (JsonProcessingException e) {
				e.printStackTrace();
			} catch (NumberFormatException | IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		Reporter.log("Going to Assert after " + ite + "Minutes of waiting");
		
		Reporter.log("Expected User:::::: " + user + "  Found User:::::: "+rUser,true);
		Assert.assertTrue(foundUser, "Users Information is not listed Under DETECT");
		return rThreatScore;
	}	
	
	public void createPolicy() throws Exception{
		
		ProtectFunctions protectFunctions = new ProtectFunctions();
		Map<String, String> aPIMap = new HashMap<>();
		aPIMap.put("policyList", "/controls/list");
		suiteData.setAPIMap(aPIMap);
		Map<String, String> policyDetails =	protectFunctions.getPolicyDetailsByName(restClient, "Detcet Report", buildCookieHeaders(), suiteData);
		/*if(policyDetails.isEmpty())
		protectFunctions.createThreatScoreBasedPolicy(restClient, "Detcet Report", "[\"__ALL_EL__\"]", buildCookieHeaders(), suiteData);
		*/
	}
	
	
	
	
	public static void main(String[] args) {
		try {
			//updateLog();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println(e);
			e.printStackTrace();
			
		}
		
	}
	
public static void findDateDiff() throws IOException{
	String tmplFilePath = "src/test/resources/detect/golden_input_tmpl/";
	@SuppressWarnings("resource")
	BufferedReader br = new BufferedReader(new FileReader(tmplFilePath + "frequentDeleteTest1.json"));

	ObjectMapper mapper = new ObjectMapper();
	mapper.setVisibility(JsonMethod.FIELD, Visibility.ANY);
	//Map<Integer, String> dateMap = new HashMap<>();
	int idx =0;
	String date ;
	 for(String line; (line = br.readLine()) != null;) {
		 JsonNode activity = mapper.readTree(line);
		  JsonNode _source = activity.path("_source");
		  if(idx>1){
		  date =  ((ObjectNode)_source).get("created_timestamp").toString().replace("\"", "");
		  }
		 
		 
		// System.out.println("date:::::::::::: "+date);
		// dateMap.put(count, date);
		 //count++;
	 }
		
	
	 
	}
	
	public static void findDateTotal(){
		
		String dateDiff = "16, 45, 19, 44, 39, 121, 79, 40, 35, 36, 38, 41, 95, 90, 68, 108, 68, 16, 53, 17, 38, 26, 65, 29, 46, 15, 60, 17, 14, 28";
		
	String[] dateDiffArray =	dateDiff.split(",");
	int total = 0 ;
	Integer[] diffArray = new Integer[dateDiffArray.length];
	for (int i = 0; i < dateDiffArray.length; i++) {
		int diff = Integer.parseInt(dateDiffArray[i].trim());
		diffArray[i] = diff;
		total = total+diff;
		
	}
	System.out.println("total:::::  "+total+"diffArray::::::  "+Arrays.toString(diffArray));	
		
	}
	
	
	
	public void setPreferences() throws Exception {
		System.out.println("Started - Setting Preferences");
		
		StringEntity se = new StringEntity("{\"objects\":[{\"resource_uri\":\"/detectbackendautomationcom/api/admin/v1/detectattributes/55ad715617f1c10a8941959d/\",\"importance\":1,\"confidence\":100,\"threshold\":10,\"window\":10,\"enabled\":true},{\"resource_uri\":\"/detectbackendautomationcom/api/admin/v1/detectattributes/55ad715617f1c10a8941959c/\",\"importance\":2,\"confidence\":100,\"threshold\":1,\"window\":1440,\"enabled\":true},{\"resource_uri\":\"/detectbackendautomationcom/api/admin/v1/detectattributes/55ad715617f1c10a8941959f/\",\"importance\":4,\"confidence\":100,\"threshold\":10,\"window\":5,\"enabled\":true},{\"resource_uri\":\"/detectbackendautomationcom/api/admin/v1/detectattributes/55ad715617f1c10a8941959e/\",\"importance\":3,\"confidence\":100,\"threshold\":3,\"window\":1,\"enabled\":true},{\"resource_uri\":\"/detectbackendautomationcom/api/admin/v1/detectattributes/55ad715617f1c10a894195a2/\",\"importance\":2,\"confidence\":100,\"threshold\":60000,\"window\":1,\"enabled\":false},{\"resource_uri\":\"/detectbackendautomationcom/api/admin/v1/detectattributes/55ad715617f1c10a894195a1/\",\"importance\":2,\"confidence\":100,\"threshold\":30000,\"window\":1,\"enabled\":false},{\"resource_uri\":\"/detectbackendautomationcom/api/admin/v1/detectattributes/55ad715617f1c10a894195a0/\",\"importance\":3,\"confidence\":100,\"threshold\":30,\"window\":10,\"enabled\":true},{\"resource_uri\":\"/detectbackendautomationcom/api/admin/v1/detectattributes/55ad715617f1c10a8941959a/\",\"importance\":4,\"confidence\":100,\"threshold\":30,\"window\":10,\"enabled\":true},{\"resource_uri\":\"/detectbackendautomationcom/api/admin/v1/detectattributes/55ad715617f1c10a8941959b/\",\"importance\":1,\"confidence\":100,\"threshold\":3,\"window\":10,\"enabled\":true},{\"resource_uri\":\"/detectbackendautomationcom/api/admin/v1/detectattributes/55ad715617f1c10a89419599/\",\"importance\":3,\"confidence\":100,\"threshold\":3,\"window\":10,\"enabled\":true},{\"resource_uri\":\"/detectbackendautomationcom/api/admin/v1/detectattributes/55ad715617f1c10a89419598/\",\"importance\":2,\"confidence\":100,\"threshold\":3,\"window\":1,\"enabled\":true},{\"resource_uri\":\"/detectbackendautomationcom/api/admin/v1/detectattributes/55ad715617f1c10a894195a3/\",\"importance\":3,\"confidence\":30,\"threshold\":0,\"window\":0,\"enabled\":true},{\"resource_uri\":\"/detectbackendautomationcom/api/admin/v1/detectattributes/55ad715617f1c10a894195a4/\",\"importance\":4,\"confidence\":30,\"threshold\":0,\"window\":0,\"enabled\":true},{\"resource_uri\":\"/detectbackendautomationcom/api/admin/v1/detectattributes/55ad715617f1c10a894195a5/\",\"importance\":2,\"confidence\":30,\"threshold\":0,\"window\":0,\"enabled\":true},{\"resource_uri\":\"/detectbackendautomationcom/api/admin/v1/detectattributes/55ad715617f1c10a894195a6/\",\"importance\":1,\"confidence\":30,\"threshold\":0,\"window\":0,\"enabled\":true},{\"resource_uri\":\"/detectbackendautomationcom/api/admin/v1/detectattributes/55ad715617f1c10a894195a7/\",\"importance\":3,\"confidence\":30,\"threshold\":0,\"window\":0,\"enabled\":true},{\"resource_uri\":\"/detectbackendautomationcom/api/admin/v1/detectattributes/55ad715617f1c10a894195ab/\",\"importance\":2,\"confidence\":30,\"threshold\":0,\"window\":0,\"enabled\":true},{\"resource_uri\":\"/detectbackendautomationcom/api/admin/v1/detectattributes/55ad715617f1c10a894195aa/\",\"importance\":2,\"confidence\":30,\"threshold\":0,\"window\":0,\"enabled\":true},{\"resource_uri\":\"/detectbackendautomationcom/api/admin/v1/detectattributes/55ad715617f1c10a894195a8/\",\"importance\":1,\"confidence\":30,\"threshold\":0,\"window\":0,\"enabled\":true},{\"resource_uri\":\"/detectbackendautomationcom/api/admin/v1/detectattributes/55ad715617f1c10a894195a9/\",\"importance\":4,\"confidence\":30,\"threshold\":0,\"window\":0,\"enabled\":true}]}");
		
		HttpResponse response = restClient.doPost(postDetectattributes(), this.buildCookieHeaders(), null, se);
		String responseBody = getResponseBody(response);
		System.out.println("Response Body Printing....... : " + responseBody);
		
		System.out.println("Waiting for 1Minute to get Preferences Refreshed!");
		Thread.sleep(1 * 60 * 1000);	//Wait for 1 Minute
		
		System.out.println("Completed - Setting Preferences");
	}
	
	
	public  void updateLog(String id, String severity, String Object_type, String user, 
			String Activity_type, String index,String facility, String cdrs,String source,String attr_set) throws Exception {
		System.out.println("Started - updateLog");
		
		String value = "{\"id\":\"%s\",\"type\":\"elastica_state\",\"severity\":\"%s\",\"Object_type\":\"%s\",\"user\":\"%s\",\"Activity_type\":\"%s\",\"index\":\"%s\",\"facility\":\"%s\",\"child_drs\":[\"%s\"],"
				+ "\"__source\":\"%s\",\"attr_set\":\"%s\",\"anomaly_status\":1,\"event_type\":\"AnomalyReport\",\"message\":\"\",\"notes\":\"User changed'Verified Alert?' from  Unknown to Yes(Cleared Alert)\",\"updated_sev\":\"informational\"}";
		String value1 = String.format(value, id, severity, Object_type, user, Activity_type, index,facility,cdrs,source,attr_set);
	JSONObject object = new JSONObject() ;
	object.put("source", value1);
		
		StringEntity se = new StringEntity(object.toString());
		Reporter.log("Request Body  ::::: " + object.toString(), true);
		ElasticSearchLogs esLogs = new ElasticSearchLogs();
	HttpResponse response =	esLogs.updateLog(restClient, this.buildCookieHeaders(), se, suiteData);
		String responseBody = getResponseBody(response);
		Reporter.log("Response Body  ::::: " + responseBody, true);
	}
	
	


public  void updateLog(String id, String severity, String Object_type,String term, String user, 
		String Activity_type, String index,String facility, String source,String attr_set, String ioi_Code) throws Exception {
	System.out.println("Started - updateLog");
	
	String value = "{\"id\":\"%s\",\"type\":\"elastica_state\",\"severity\":\"%s\",\"Object_type\":\"%s\",\"user\":{\"term\":\"%s\","
			+ "\"user\":\"%s\"},\"Activity_type\":\"%s\",\"index\":\"%s\",\"facility\":\"%s\",\"child_drs\":[\"\"],\"__source\":\"%s\","
			+ "\"attr_set\":\"%s\",\"anomaly_status\":0,\"event_type\":\"AnomalyReport\",\"message\":\"\",\"profile\":\"clean\",\"ioi_code\":\"%s\","
			+ "\"updated_sev\":\"informational\",\"notes\":\"Cleared Alert\"}";
	String value1 = String.format(value, id, severity, Object_type, user, Activity_type, index,facility,source,attr_set,ioi_Code);
	JSONObject object = new JSONObject() ;
	object.put("source", value1);
	
	StringEntity se = new StringEntity(object.toString());
	Reporter.log("Request Body  ::::: " + object.toString(), true);
	ElasticSearchLogs esLogs = new ElasticSearchLogs();
	HttpResponse response =	esLogs.updateLog(restClient, this.buildCookieHeaders(), se, suiteData);
	String responseBody = getResponseBody(response);
	Reporter.log("Response Body  ::::: " + responseBody, true);
}
	
	
	
	public void tearDown(String user) throws Exception {
		System.out.println("Cleaning up Resources");
		
		HttpResponse response;
		String responseBody;
		ElasticSearchLogs esLogs = new ElasticSearchLogs();
		
		if (user != null) {
			
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
	
	
	
	public void verifyActivityCount(String query, List<String> messageList) throws Exception {
		Reporter.log("Retrieving the logs from Elastic Search ...");
		ElasticSearchLogs esLogs = new ElasticSearchLogs();
		
		Date dateTo = new Date();
    	SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd'T'HH");
    	formatDate.setTimeZone(TimeZone.getTimeZone("GMT"));
    	String strDateTimeTo = formatDate.format(dateTo);
    	System.out.println(strDateTimeTo);
    	Calendar cal = Calendar.getInstance();
    	cal.add(Calendar.MINUTE, -59);
    	Date dateFrom = cal.getTime();
    	String strDateTimeFrom = formatDate.format(dateFrom);
    	System.out.println(strDateTimeFrom);
    	com.elastica.beatle.securlets.ESQueryBuilder esqueryBuilder1 = new com.elastica.beatle.securlets.ESQueryBuilder();
    	
		HttpResponse response = null;
		try {
			int messageCount = messageList.size();
			int count = 0;
		HashMap<String, String> terms = new HashMap<>();
			for (int i = 1; i<=10; i++) {
				String payload = 	esqueryBuilder1.getESQueryForInvestigate(strDateTimeFrom+ ":00:00.000Z", strDateTimeTo+":59:59.999Z", "Elastica",
						terms, query, apiServer, suiteData.getCSRFToken(), suiteData.getSessionID(), 0, 1000);
//				String payload = esQueryBuilder.getSearchQueryForDisplayLogs(strDateTimeFrom + ":00:00.000Z",
//						strDateTimeTo + ":59:59.999Z", query, "Elastica", 1000, apiServer, csfrToken, sessionID,
//						suiteData.getUsername());
				
				Reporter.log("getting investigate logs ::::::  payload::::   " + payload, true);
				
				response = esLogs.getDisplayLogs(restClient, buildBasicHeaders(suiteData.getCSRFToken(), suiteData.getSessionID()),suiteData.getApiserverHostName(),
						new StringEntity(payload));
				Assert.assertEquals(response.getStatusLine().getStatusCode(), 200, "Response code is not equal");
				String responseBody = getResponseBody(response);
				Reporter.log("responseBody:::::::::::::: "+responseBody, true);
				JsonNode arrNode = new ObjectMapper().readTree(getJSONValue(responseBody, "hits")).get("hits");
				
				if (arrNode.isArray()) {
					if(arrNode.size()!=0){
				    for (final JsonNode objNode : arrNode) {
				    	 JsonNode _source = objNode.path("_source");
				    	JsonNode message = _source.path("message");
				    	if(messageList.contains(message.asText())){
				    		Reporter.log("Activity log message on invetsigate page::::::::::  "+message, true);
				    		messageList.remove(message.asText());
				    		count++;
				    	}
				    	
				    	}
					}
				}
				
			if(messageList.size()!=0){
				Reporter.log(" Expected::: "+messageCount+ " Actual:::::: "+count, true);
				Reporter.log("Saas App activities are not equal with the investigate logs, Retrying :::::"+i+" times wait time between each retry is:::: 1 min", true);
				Thread.sleep(1*60*1000);
				continue;
			}else if(messageList.size()==0){
				Reporter.log(" Expected::: "+messageCount+ " Actual:::::: "+count, true);
				break;
				}
			}
			if(messageCount!=count){
			Reporter.log(" Expected::: "+messageCount+ " Actual:::::: "+count, true);
			throw new SkipException("Number of Activities returned are not Equal");
			}
			
		}  catch (Exception e) {
			throw e;
		}
	}
	
	public void verifyActivityCount(String query, List<String> messageList, int miutes, int hours) throws Exception {
		Reporter.log("Retrieving the logs from Elastic Search ...");
		ElasticSearchLogs esLogs = new ElasticSearchLogs();
		
		Date dateTo = new Date();
    	SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd'T'HH");
    	formatDate.setTimeZone(TimeZone.getTimeZone("GMT"));
    	String strDateTimeTo = formatDate.format(dateTo);
    	System.out.println(strDateTimeTo);
    	Calendar cal = Calendar.getInstance();
    	cal.add(Calendar.MINUTE, -miutes);
    	cal.add(Calendar.HOUR, -hours);
    	Date dateFrom = cal.getTime();
    	String strDateTimeFrom = formatDate.format(dateFrom);
    	System.out.println(strDateTimeFrom);
    	
		HttpResponse response = null;
		try {
			int messageCount = messageList.size();
			int count = 0;
		
			for (int i = 1; i<=20; i++) {
				String payload = esQueryBuilder.getSearchQueryForDisplayLogs(strDateTimeFrom + ":00:00.000Z",
						strDateTimeTo + ":59:59.999Z", query, "Elastica", 100, apiServer, suiteData.getCSRFToken(), suiteData.getSessionID(),
						suiteData.getUsername());
				Reporter.log("getting investigate logs ::::::  payload::::   " + payload, true);
				
				response = esLogs.getDisplayLogs(restClient, buildBasicHeaders(suiteData.getCSRFToken(), suiteData.getSessionID()),suiteData.getApiserverHostName(),
						new StringEntity(payload));
				Assert.assertEquals(response.getStatusLine().getStatusCode(), 200, "Response code is not equal");
				String responseBody = getResponseBody(response);
				Reporter.log("responseBody:::::::::::::: "+responseBody, true);
				JsonNode arrNode = new ObjectMapper().readTree(getJSONValue(responseBody, "hits")).get("hits");
				
				if (arrNode.isArray()) {
					if(arrNode.size()!=0){
				    for (final JsonNode objNode : arrNode) {
				    	 JsonNode _source = objNode.path("_source");
				    	JsonNode message = _source.path("message");
				    	if(messageList.contains(message.asText())){
				    		Reporter.log("Activity log message on invetsigate page::::::::::  "+message, true);
				    		messageList.remove(message.asText());
				    		count++;
				    	}
				    	
				    	}
					}
				}
				
			if(messageList.size()!=0){
				Reporter.log(" Expected::: "+messageCount+ " Actual:::::: "+count, true);
				Reporter.log("Saas App activities are not equal with the investigate logs, Retrying :::::"+i+" times wait time between each retry is:::: 1 min", true);
				Thread.sleep(1*60*1000);
				continue;
			}else if(messageList.size()==0){
				Reporter.log(" Expected::: "+messageCount+ " Actual:::::: "+count, true);
				break;
				}
			}
			if(messageCount!=count){
			Reporter.log(" Expected::: "+messageCount+ " Actual:::::: "+count, true);
			throw new SkipException("Number of Activities returned are not Equal");
			}
			
		}  catch (Exception e) {
			throw e;
		}
	}

	public HttpResponse addNoteToIncident(String id, String index, String notes) throws Exception {
		System.out.println("Started - updateNotes");
		
		JSONObject object = new JSONObject() ;
		String value = "{\"id\":\"%s\",\"type\":\"elastica_state\",\"index\":\"%s\",\"event_type\":\"AnomalyReport\",\"notes\":\"%s\"}";
		object.put("source", String.format(value, id, index, notes));
		
		StringEntity se = new StringEntity(object.toString());
		Reporter.log("Request Body  ::::: " + object.toString(), true);
		ElasticSearchLogs esLogs = new ElasticSearchLogs();
		HttpResponse response =	esLogs.updateNotes(restClient, this.buildCookieHeaders(), se, suiteData);
		
		return response;
	}
	
	public HttpResponse feedbackToIncident(String id, String severity, String Object_type, String user, 
			String Activity_type, String index,String facility,String source,String attr_set, String unusual, Boolean clearIncident, Boolean clearProfile, String ioi_code) throws Exception {
		System.out.println("Started - updateLog");
		
		String notes = "User changed'Verified Alert?' from  Unknown to " + unusual;
		String updated_sev = severity;
		int anomaly_status = 1;
		if (clearIncident) {
			notes += "(Cleared Alert)";
			updated_sev = "informational";
			anomaly_status = 2;
		}
		
		JSONObject object = new JSONObject() ;
		
		if (!clearProfile) {
			String value = "{\"id\":\"%s\",\"type\":\"elastica_state\",\"severity\":\"%s\",\"Object_type\":\"%s\",\"user\":\"%s\",\"Activity_type\":\"%s\",\"index\":\"%s\",\"facility\":\"%s\",\"child_drs\":[\"\"],"
					+ "\"__source\":\"%s\",\"attr_set\":\"%s\",\"anomaly_status\":%d,\"event_type\":\"AnomalyReport\",\"message\":\"\",\"notes\":\"%s\",\"updated_sev\":\"%s\"}";
			String value1 = String.format(value, id, severity, Object_type, user, Activity_type, index,facility,source,attr_set, anomaly_status, notes, updated_sev);
			object.put("source", value1);
		} else {
			String value = "{\"id\":\"%s\",\"type\":\"elastica_state\",\"severity\":\"%s\",\"Object_type\":\"%s\",\"user\":\"%s\",\"Activity_type\":\"%s\",\"index\":\"%s\",\"facility\":\"%s\",\"child_drs\":[\"\"],"
					+ "\"__source\":\"%s\",\"attr_set\":\"%s\",\"anomaly_status\":%d,\"event_type\":\"AnomalyReport\",\"message\":\"\",\"profile\":\"clean\",\"ioi_code\":\"%s\",\"notes\":\"%s\",\"updated_sev\":\"%s\"}";
			String value1 = String.format(value, id, severity, Object_type, user, Activity_type, index,facility,source,attr_set, anomaly_status, ioi_code, notes, updated_sev);
			object.put("source", value1);
		}
				
		StringEntity se = new StringEntity(object.toString());
		Reporter.log("Request Body  ::::: " + object.toString(), true);
		ElasticSearchLogs esLogs = new ElasticSearchLogs();
		HttpResponse response =	esLogs.updateLog(restClient, this.buildCookieHeaders(), se, suiteData);
		
		Reporter.log(" Going to waiting for 1 minute to take effect of Incident Feedback!", true);
		Thread.sleep(1*60*1000);
		
		return response;
	}


	public void getAndFeedbackToIncident(String user, String ioi_Code, String unusual, Boolean clearIncident) throws Exception {
		Boolean clearProfile = false;
		
		String responseBody =  getListOfIncidents();
		JsonNode jnode = unmarshall(responseBody, JsonNode.class);
		System.out.println("Respon : " + responseBody);
		if (jnode.isArray()) {
		    for (final JsonNode objNode : jnode) {
		    	String report_id = getJSONValue(objNode.toString(), "id").toString().replace("\"", "");
		    	String facilty = getJSONValue(objNode.toString(), "f").toString().replace("\"", "");
		    	String ioi = getJSONValue(objNode.toString(), "ioi").toString().replace("\"", "");
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
		    	
		    	if ((user.equals("MULTI_USER") || email.equals(user)) && ioi.equals(ioi_Code)) {
		    		System.out.println(report_id + ":" + severity + ":" + ObjectType + ":" + email + ":" + Activity_type + ":" + index + ":" + facilty + ":" + source + ":" + attr_set);
		    		feedbackToIncident(report_id, severity, ObjectType, email, Activity_type, index, facilty, source, attr_set, unusual, clearIncident, clearProfile, ioi_Code);
		    	}
		       }
		    }	
	}
	
	public void getAndFeedbackToIncident(String user, String ioi_Code, String unusual, Boolean clearIncident, Boolean clearProfile) throws Exception {
		String responseBody =  getListOfIncidents();
		JsonNode jnode = unmarshall(responseBody, JsonNode.class);
		System.out.println("Respon : " + responseBody);
		if (jnode.isArray()) {
		    for (final JsonNode objNode : jnode) {
		    	String report_id = getJSONValue(objNode.toString(), "id").toString().replace("\"", "");
		    	String facilty = getJSONValue(objNode.toString(), "f").toString().replace("\"", "");
		    	String ioi = getJSONValue(objNode.toString(), "ioi").toString().replace("\"", "");
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
		    	
		    	if ((user.equals("MULTI_USER") || email.equals(user)) && ioi.equals(ioi_Code)) {
		    		System.out.println(report_id + ":" + severity + ":" + ObjectType + ":" + email + ":" + Activity_type + ":" + index + ":" + facilty + ":" + source + ":" + attr_set);
		    		feedbackToIncident(report_id, severity, ObjectType, email, Activity_type, index, facilty, source, attr_set, unusual, clearIncident, clearProfile, ioi_Code);
		    	}
		       }
		    }	
	}
	
	public Boolean isIncidentListed(String user, String ioi_Code) throws Exception {
		String responseBody =  getListOfIncidents();
		JsonNode jnode = unmarshall(responseBody, JsonNode.class);
		System.out.println("Respon : " + responseBody);
		if (jnode.isArray()) {
		    for (final JsonNode objNode : jnode) {
		    	String ioi = getJSONValue(objNode.toString(), "ioi").toString().replace("\"", "");
		    	String email = getJSONValue(objNode.toString(), "e").toString().replace("\"", "");
		    	
		    	if (email.equals(user) && ioi.equals(ioi_Code)) {
		    		return true;
		    	}
		       }
		    }
		
		return false;
	}
	
	public URI getDetectattributes() throws Exception{
		String requestUri = "/admin/analytics/getdetectattributes";
		return ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(), requestUri);
	}
	
	public URI postDetectattributes() throws Exception{
		String requestUri = "/admin/analytics/postattributesvalues";
		return ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(), requestUri);
	}
	
		public void clearIncidents() {
		try {
		String responseBody =  getListOfIncidents();
		JsonNode jnode = unmarshall(responseBody, JsonNode.class);
		if (jnode.isArray()) {
			System.out.println("Count of incidents :::::"+jnode.size());
		    for (final JsonNode objNode : jnode) {
		    	String report_id = getJSONValue(objNode.toString(), "id").toString().replace("\"", "");
		    	String facilty = getJSONValue(objNode.toString(), "f").toString().replace("\"", "");
		    	String email = getJSONValue(objNode.toString(), "e").toString().replace("\"", "");
		    	String index = getJSONValue(objNode.toString(), "index").toString().replace("\"", "");
		    	String source = getJSONValue(objNode.toString(), "src").toString().replace("\"", "");
		    	
		    	String attr_set = getJSONValue(objNode.toString(), "ats").toString().replace("\"", "");
		    	String cdrs1 = getJSONValue(objNode.toString(), "cdrs").toString().replace("\"", "");
		    	String cdrs =  cdrs1.replace("[", "").replace("]", "");
		    	
		    	if(attr_set!=null){
		    	String Activity_type = getJSONValue(objNode.toString(), "at").toString().replace("\"", "");
		    	String severity = getJSONValue(objNode.toString(), "s").toString().replace("\"", "");
		    	String ObjectType = getJSONValue(objNode.toString(), "ot").toString().replace("\"", "");
		    	
		    	updateLog(report_id, severity, ObjectType, email, Activity_type, index, facilty, cdrs, source, attr_set);
		    	}
		    	
		       }
		    }
		Reporter.log("waiting for 1 min after clearing all the incidents", true);
		Thread.sleep(1 * 60 * 1000);
		 } catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
		
		public void clearIncidentAndProfile(String user, String ioiCode) throws Exception {
			
			getAndFeedbackToIncident(user, ioiCode, "Yes", true, true);
		}
		
		public String validateIncidents(String ioicode) {
			Reporter.log("Retrieving the logs from Elastic Search ...");
			ElasticSearchLogs esLogs = new ElasticSearchLogs();
			String responseBody = null;
			Date dateTo = new Date();
	    	SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	    	formatDate.setTimeZone(TimeZone.getTimeZone("GMT"));
	    	String strDateTimeTo = formatDate.format(dateTo);
	    	System.out.println(strDateTimeTo);
	    	Calendar cal = Calendar.getInstance();
	    	cal.add(Calendar.DATE, -1);
	    	Date dateFrom = cal.getTime();
	    	String strDateTimeFrom = formatDate.format(dateFrom);
	    	System.out.println(strDateTimeFrom);
	    	
			HttpResponse response;
			Boolean found_ioi =false;
			int ite=1;
			for (; ite<=10; ite++) {
			try {
			
					String payload = esQueryBuilder.getSearchQueryForDetect(strDateTimeFrom,
							strDateTimeTo);
					response = esLogs.getCloudServiceAnomalies(restClient, buildCookieHeaders(suiteData.getCSRFToken(), suiteData.getSessionID()), new StringEntity(payload),suiteData);
					Assert.assertEquals(response.getStatusLine().getStatusCode(), 200, "Response code is not equal");
					 responseBody = getResponseBody(response);
					Reporter.log(" validateIncidents::::  Response::::   " + responseBody, true);
					JsonNode jnode = unmarshall(responseBody, JsonNode.class);
					
					if (jnode.isArray()) {
					    for (final JsonNode objNode : jnode) {
					    	String    	ioi_found = getJSONValue(objNode.toString(), "ioi").toString().replace("\"", "");
					    	Reporter.log("found ioicode:::::::: "+ioi_found, true);
					    	Reporter.log("Expected ioicode:::::::: "+ioicode, true);
					    	if (ioicode.equalsIgnoreCase(ioi_found)) {
								found_ioi = true;
								break;
							}	
					    	
					       }
					    if (found_ioi) {
							break;
						} else {
							Thread.sleep(1 * 60 * 1000);	//wait for 1 minute.
							Reporter.log("Incident  is not found in the detect page Retrying::::::: " + ite + "Minutes of waiting");
						}
					    
					    }
			
					
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			}
			Reporter.log("Going to Assert after " + ite + "Minutes of waiting");
			
			Reporter.log("Expected IOI_Code:::::: " + ioicode + " not found in the incident list",true);
			Assert.assertTrue(found_ioi, "incident not listed");
			
			return responseBody;
		}
		
		
		
}
