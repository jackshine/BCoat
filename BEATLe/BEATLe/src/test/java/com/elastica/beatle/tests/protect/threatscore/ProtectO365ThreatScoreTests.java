package com.elastica.beatle.tests.protect.threatscore;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.entity.StringEntity;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.elastica.beatle.TestSuiteDTO;
import com.elastica.beatle.RestClient.Client;
import com.elastica.beatle.RestClient.ClientUtil;
import com.elastica.beatle.detect.DetectCommonutils;
import com.elastica.beatle.es.ESQueryBuilder;
import com.elastica.beatle.es.ElasticSearchLogs;
import com.elastica.beatle.protect.PolicyBean;
import com.elastica.beatle.protect.ProtectFunctions;
import com.elastica.beatle.protect.ProtectInitializeTests;
import com.elastica.beatle.protect.ProtectTestConstants;
import com.universal.common.UniversalApi;
import com.universal.dtos.onedrive.Folder;


public class ProtectO365ThreatScoreTests extends ProtectInitializeTests {
	
	UniversalApi universalApi;
	Client restClient;
	ProtectFunctions protectFunctions = new ProtectFunctions();
	PolicyBean policyBean = new PolicyBean();
	Map<String, String> policyActualName = new HashMap<String, String>();
	DetectCommonutils DetectCommonUtils = new DetectCommonutils();

	
	@BeforeClass
	public void init() throws Exception {
		universalApi = protectFunctions.loginToApp(suiteData);
		restClient = new Client();
	}
	
	/**
	 * List for all the policies
	 * @return
	 */
	@DataProvider(name = "ThreatScore")
	public Object[][] getData() {
		return new Object[][] {
			new String[] { "TSPOLICY1", "Office 365", "admin@protecto365autobeatle.com", "NA", "NA", "NA", "NA", "10"},
			new String[] { "TSPOLICY2", "Office 365", "admin@protecto365autobeatle.com", "NA", "NA", "NA", "NA", "0"},
		};
	}
	
	/**
	 * DataProvider to get the list of policies
	 * @return
	 */
	@DataProvider(name = "ThreatScoreList")
	public Object[][] getDataList() {
		List<Object[][]> list = new ArrayList<Object[][]>(); 
		list.add(getData());
		return new Object[][] { { list } };  
	}

	/**
	 * 
	 * @param data
	 * @throws Exception
	 */
	@Test(dataProvider = "ThreatScoreList", priority = 1)
	public void testThreatScorePolicy(List<String[]> list) throws Exception {
		System.out.println("Input data printing...." + list.size());
		Object[] arr = list.get(0);
		List<Object> arrList = Arrays.asList(arr);
		Iterator<Object> iterator = arrList.iterator();
		while (iterator.hasNext()) {
			try {
				// Create a policy for Threat Score
				String[] data = (String[]) iterator.next();
				String policyName = "P"+protectFunctions.generateAlphaNumericString(7);
				policyActualName.put(policyBean.getPolicyName(), policyName);
				policyBean = protectFunctions.setThreatScorePolicyData(data);
				policyBean.setPolicyName(policyName);
				protectFunctions.createThreatScoreBasedPolicy(restClient, requestHeader, suiteData, policyBean);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		File file = protectFunctions.createDynamicFile(ProtectTestConstants.PROTECT_RESOURCE_PATH, new File(""),
				"exe");
		protectFunctions.waitForMinutes(0.5);
		String folderName = "A_" + UUID.randomUUID().toString();
		Reporter.log("Creating a folder in One Drive : " + folderName, true);
		Folder folder = universalApi.createFolder(folderName);
		universalApi.uploadSimpleFile(folder.getId(), file.getAbsolutePath(), file.getName());
		protectFunctions.waitForMinutes(10);
	}
	
	@Test(dataProvider = "ThreatScore", priority = 2)
	public void verifyPolicyViolationForThreatScore(String... data) throws Exception{
		policyBean = protectFunctions.setThreatScorePolicyData(data);
		policyBean.setPolicyName(policyActualName.get(policyBean.getPolicyName()));
		Map<String, String> policyViolationLogDetails = protectFunctions.getThreatScorePolicyViolationAlertLogDetails(restClient, policyBean, requestHeader, suiteData);
		protectFunctions.assertThreatScorePolicyViolation(policyViolationLogDetails, policyBean, suiteData);
		Map<String, String> blockDetails = protectFunctions.getProtectBlockDetails(restClient, policyBean, requestHeader, suiteData);
		protectFunctions.clearBlock(restClient, policyBean, requestHeader, suiteData, blockDetails);
	}
	
	@Test(dataProvider = "ThreatScoreList", priority = 3)
	public void deletePolicies(List<String[]> list){
		System.out.println("Input data printing...." + list.size());
		Object[] arr = list.get(0);
		List<Object> arrList = Arrays.asList(arr);
		Iterator<Object> iterator = arrList.iterator();
		while (iterator.hasNext()) {
			try {
				String[] data = (String[]) iterator.next();
				policyBean = protectFunctions.setThreatScorePolicyData(data);
				String policyName = policyBean.getPolicyName();
				Reporter.log("Starting testcase: testDeletePolicy - " + policyName, true);
				protectFunctions.deactivateAndDeletePolicy(restClient, policyName, requestHeader, suiteData);
				Reporter.log("Completed testcase: testDeletePolicy - " + policyName, true);
			} catch (Exception e) {
				continue;
			} catch(Error e) {
				continue;
			}
		}
	}
	
	public String getThreatScore(Client restClient, List<NameValuePair> requestHeader, TestSuiteDTO suiteData) throws Exception{
		String csfrToken = requestHeader.get(0).getValue();
		String sessionID = requestHeader.get(4).getValue();
		String payload1 = "{\"sourceName\":\"DETECT\",\"apiServerUrl\":\"https://api-eoe.elastica-inc.com/\",\"csrftoken\":\""+csfrToken+"\",\"sessionid\":\""+sessionID+"\",\"userid\":\"admin@protecto365autobeatle.com\"}";
		StringEntity se = new StringEntity(payload1);
		HttpResponse response = this.getUserThreatsData(restClient, requestHeader, se, suiteData);
		String threatUserData = ClientUtil.getResponseBody(response);
		JsonNode userNode = new ObjectMapper().readTree(ClientUtil.getJSONValue(threatUserData.toString(), "users_list"));
		JsonNode emailNode = new ObjectMapper().readTree(ClientUtil.getJSONValue(userNode.toString(), suiteData.getUsername()));
		JsonNode servicesNode  = new ObjectMapper().readTree(ClientUtil.getJSONValue(emailNode.toString(), "services"));
		String servicesName = null;
		String threatScoreActual = null;
		if(servicesNode.isArray()){
			for(final JsonNode serviceDetails : servicesNode){
				servicesName = ClientUtil.getJSONValue(serviceDetails.toString(), "name");
				threatScoreActual= ClientUtil.getJSONValue(serviceDetails.toString(), "threat_score");
				System.out.println("Services name is "+servicesName);
				System.out.println("threatScoreActual::::"+threatScoreActual);
			}
		}
		return threatScoreActual;
	}
	
	public HttpResponse getUserThreatsData(Client restClient, List<NameValuePair> requestCookieHeader, StringEntity se,TestSuiteDTO suiteData) throws Exception {
		String requestUri = "/eslogs/getUserThreatsData/";
		URI dataUri = ClientUtil.BuidURI("https", suiteData.getApiserverHostName(), requestUri);
		System.out.println("unblockUser dataUri:::::::: "+dataUri);
		return restClient.doPost(dataUri, requestCookieHeader, null,se);
	}
	
	public  void updateLog(String id, String severity, String Object_type, String user, 
			String Activity_type, String index,String facility,String source,String attr_set,
			Client restClient, List<NameValuePair> requestCookieHeader) throws Exception {
		System.out.println("Started - updateLog");
		String value = "{\"id\":\"%s\",\"type\":\"elastica_state\",\"severity\":\"%s\",\"Object_type\":\"%s\",\"user\":\"%s\",\"Activity_type\":\"%s\",\"index\":\"%s\",\"facility\":\"%s\",\"child_drs\":[\"\"],"
				+ "\"__source\":\"%s\",\"attr_set\":\"%s\",\"anomaly_status\":1,\"event_type\":\"AnomalyReport\",\"message\":\"\",\"notes\":\"User changed'Verified Alert?' from  Unknown to Yes(Cleared Alert)\",\"updated_sev\":\"informational\"}";
		String value1 = String.format(value, id, severity, Object_type, user, Activity_type, index,facility,source,attr_set);
		JSONObject object = new JSONObject() ;
		object.put("source", value1);
		StringEntity se = new StringEntity(object.toString());
		Reporter.log("Request Body  ::::: " + object.toString(), true);
		String requestUri = "/admin/analytics/updateLog";
		URI dataUri = ClientUtil.BuidURI("https", suiteData.getHost(), requestUri);
		System.out.println("unblockUser dataUri:::::::: "+dataUri);
		HttpResponse response =	 restClient.doPost(dataUri, requestCookieHeader, null,se);
		String responseBody = ClientUtil.getResponseBody(response);
		Reporter.log("Response Body  ::::: " + responseBody, true);
	}
	
	public String getListOfIncidents(Client restClient, List<NameValuePair> requestHeader, TestSuiteDTO suiteData) {
		Reporter.log("Retrieving the logs from Elastic Search ...");
		ElasticSearchLogs esLogs = new ElasticSearchLogs();
		ESQueryBuilder esQueryBuilder = new ESQueryBuilder();
		String responseBody = null;
		Date dateTo = new Date();
    	SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd'T'HH");
    	formatDate.setTimeZone(TimeZone.getTimeZone("GMT"));
    	String strDateTimeTo = formatDate.format(dateTo);
    	System.out.println(strDateTimeTo);
    	Calendar cal = Calendar.getInstance();
    	cal.add(Calendar.DATE, -2);
    	Date dateFrom = cal.getTime();
    	String strDateTimeFrom = formatDate.format(dateFrom);
    	System.out.println(strDateTimeFrom);
		HttpResponse response;
		try {
			String payload = esQueryBuilder.getSearchQueryForDetect(strDateTimeFrom + ":00:00.000Z", strDateTimeTo + ":59:59.999Z");
			response = this.getCloudServiceAnomalies(restClient,requestHeader, new StringEntity(payload),suiteData);
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
	
	public HttpResponse getCloudServiceAnomalies(Client restClient, List<NameValuePair> requestCookieHeader, StringEntity se, TestSuiteDTO suiteData ) throws Exception {
		String requestUri = "/admin/application/list/cloud_service_anomalies/";
		URI dataUri = ClientUtil.BuidURI("https", suiteData.getHost(), requestUri);
		return restClient.doPost(dataUri, requestCookieHeader, null, se);
	}
	
	/**
	 * 
	 * @param enabled
	 * @param attributeBean
	 * @param getResponseArray
	 * @param ioi_Code
	 * @throws JSONException
	 * @throws Exception
	 * @throws UnsupportedEncodingException
	 * @throws JAXBException
	 *//*
	protected void updateDetectAttributes(boolean enabled, AttributeBean attributeBean,
			org.json.JSONArray getResponseArray, String ioi_Code)
					throws JSONException, Exception, UnsupportedEncodingException, JAXBException {
		List<Objects> objects = new ArrayList<>();
		for (int index = 0; index < getResponseArray.length(); index++) {
			JSONObject attributeObject = getResponseArray.getJSONObject(index);
			
			if (ioi_Code.toString().equals((String) attributeObject.get("name"))) {
				
				String uri = (String) attributeObject.get("resource_uri");
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
		//Thread.sleep(1* 30* 1000);
	}*/
	
	/**
	 * 
	 * @param objects
	 * @throws Exception
	 * @throws UnsupportedEncodingException
	 * @throws JAXBException
	 *//*
	protected void postAttributes(List<Objects> objects)
			throws Exception, UnsupportedEncodingException, JAXBException {
		HttpResponse resp;
		DetectAttributeDto detectAttributeDto = new DetectAttributeDto();
		detectAttributeDto.setObjects(objects);
		URI getAttributesURI = DetectPrefrencesAPI.postDetectattributes();
		StringEntity se = new StringEntity(DetectCommonUtils.marshall(detectAttributeDto));
		Reporter.log("detectAttributes::: "+DetectCommonUtils.marshall(detectAttributeDto));
		se.setContentType("application/json");
		resp = restClient.doPost(getAttributesURI, buildCookieHeaders(), null, se);
		Assert.assertEquals(resp.getStatusLine().getStatusCode(), 200, "Response code is not equal");
	}*/
	
	// Clear threatscore for the user
  /*ElasticSearchLogs esLogs = new ElasticSearchLogs();
	HttpResponse response = esLogs.getThreatscore(restClient, "protectauto@protectbeatle.com", "protectbeatlecom");
	System.out.println(ClientUtil.getResponseBody(response));
	esLogs.clearThreatscore(restClient, "protectauto@protectbeatle.com", "protectbeatlecom");
	esLogs.clearState(restClient, "protectauto@protectbeatle.com", "protectbeatlecom");
	esLogs.clearDetectProfile(restClient, "protectauto@protectbeatle.com", "protectbeatlecom");*/
	/*ElasticSearchLogs esLogs = new ElasticSearchLogs();
	DetectUtils detectUtils = new DetectUtils();
	DetectBBITests_GW detectFunctions = new DetectBBITests_GW();
	
	String threatScore1 = this.getThreatScore(restClient, requestHeader, suiteData);
	String incidentResponseBody = this.getListOfIncidents(restClient, requestHeader, suiteData);
	JsonNode jnode = detectUtils.unmarshall(incidentResponseBody, JsonNode.class);
	if (jnode.isArray()) {
	    for (final JsonNode objNode : jnode) {
	    	String report_id = ClientUtil.getJSONValue(objNode.toString(), "id").toString().replace("\"", "");
	    	String facilty = ClientUtil.getJSONValue(objNode.toString(), "f").toString().replace("\"", "");
	    	String email = ClientUtil.getJSONValue(objNode.toString(), "e").toString().replace("\"", "");
	    	String index = ClientUtil.getJSONValue(objNode.toString(), "index").toString().replace("\"", "");
	    	String source = ClientUtil.getJSONValue(objNode.toString(), "src").toString().replace("\"", "");
	    	String attr_set = ClientUtil.getJSONValue(objNode.toString(), "ats").toString().replace("\"", "");
	    	String Activity_type = ClientUtil.getJSONValue(objNode.toString(), "at").toString().replace("\"", "");
	    	String severity = ClientUtil.getJSONValue(objNode.toString(), "s").toString().replace("\"", "");
	    	String ObjectType = ClientUtil.getJSONValue(objNode.toString(), "ot").toString().replace("\"", "");
	    	
	    	this.updateLog(report_id, severity, ObjectType, email, Activity_type, index, facilty, source, attr_set, restClient, requestHeader);
	    }
	}
	String threatScore2 = this.getThreatScore(restClient, requestHeader, suiteData);
	//dUtils.updateLog(id, severity, Object_type, user, Activity_type, index, facility, source, attr_set);
	URI getAttributesURI = DetectPrefrencesAPI.getDetectattributes();
	HttpResponse resp =  restClient.doGet(getAttributesURI, buildCookieHeaders());
	Assert.assertEquals(resp.getStatusLine().getStatusCode(), 200, "Response code is not equal");
	String responseBody = ClientUtil.getResponseBody(resp);
	System.out.println("Response Body printing....."+ responseBody);
	org.json.JSONArray getResponseArray = new JSONObject(responseBody).getJSONArray("detect_attributes");
	AttributeBean  attributeBean = new AttributeBean(1, 3, 4, true);
	String ioi_Code = "TOO_MANY_SUM_LARGE_UPLOADS";
	boolean enabled = attributeBean.isEnabled();
	updateDetectAttributes(enabled, attributeBean, getResponseArray, ioi_Code);
	Reporter.log("updated preferences ::::::     " + attributeBean.toString(), true);*/				
	
					
}
