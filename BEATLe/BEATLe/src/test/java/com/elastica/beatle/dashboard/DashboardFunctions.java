package com.elastica.beatle.dashboard;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.map.HashedMap;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.codehaus.jackson.JsonProcessingException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.elastica.beatle.DateUtils;
import com.elastica.beatle.TestSuiteDTO;
import com.elastica.beatle.RestClient.Client;
import com.elastica.beatle.RestClient.ClientUtil;
import com.elastica.beatle.dashboard.DashboardTestConstants.USER_SEVERITY;
import com.elastica.beatle.es.ElasticSearchLogs;
import com.elastica.beatle.fileHandler.FileHandlingUtils;
import com.elastica.beatle.logger.Logger;

/**
 * @author anuvrath
 */

public class DashboardFunctions {
	
	private TestSuiteDTO suiteData;
	private Client restClient;
	private List<NameValuePair> requestHeader;
	
	public DashboardFunctions(TestSuiteDTO suiteData,Client restClient,List<NameValuePair> requestHeader){
		this.suiteData = suiteData;
		this.restClient = restClient;
		this.requestHeader = requestHeader;
	}
	
	/**
	 * @return
	 * @throws Exception
	 */
	public HttpResponse getActiveUsersFromInvestigate() throws Exception{				
		String payload = FileHandlingUtils.readDataFromFile(DashboardTestConstants.ES_QUERY_JSON);
		payload = payload.replace("FROM_TIME", DateUtils.getPreviousMonthDateFromCurrentTime(1)).replace("TO_TIME", DateUtils.getCurrentTime());		
		payload = payload.replace("API_SERVER_HOST_NAME", suiteData.getApiserverHostName()).replace("CSRF_TOKEN", suiteData.getCSRFToken());		
		payload = payload.replace("SESSION_ID", suiteData.getSessionID()).replace("USER_ID", suiteData.getUsername());		
		Logger.info("Payload for esLog search: "+payload);
		StringEntity entity = new StringEntity(payload);
		return new ElasticSearchLogs().getDisplayLogs(restClient, requestHeader, suiteData.getApiserverHostName(), entity);				
	}
	
	/**
	 * @param severityLevel
	 * @return
	 * @throws Exception
	 */
	public HttpResponse getCloudServiceAnamolies(String severityLevel) throws Exception {
		String payload = FileHandlingUtils.readDataFromFile(DashboardTestConstants.ES_QUERY_FOR_DETECT_INCIDENTS);
		payload = payload.replace("FROM_TIME", DateUtils.getPreviousMonthDateFromCurrentTime(1)).replace("TO_TIME", DateUtils.getCurrentTime());
		payload = payload.replace("SEVERITY_LEVEL", severityLevel);
		String restAPI = replaceGenericParams(suiteData.getAPIMap().get("getCloudServiceAnamolies"));				
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(),restAPI);
		return restClient.doPost(dataUri, requestHeader, null, new StringEntity(payload));
	}
	/**
	 * @param restClient
	 * @param suiteData
	 * @param requestHeader
	 * @return
	 * @throws Exception
	 * @throws JsonProcessingException
	 * @throws IOException
	 */
	public HttpResponse getDashboardUsersData() throws Exception, JsonProcessingException, IOException {		
		String restAPI = replaceGenericParams(suiteData.getAPIMap().get("getDashboardUsersData"));
		String payload = "{\"from_date\":\""+DateUtils.getPreviousMonthDateFromCurrentTime(1)+"\",\"to_date\":\""+DateUtils.getCurrentTime()+"\"}";
		Logger.info("Request Payload: "+payload);
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(),restAPI);		
		return restClient.doPost(dataUri, requestHeader, null, new StringEntity(payload));			
	}
	
	public HttpResponse setDefaultPreferences() throws Exception{
		String restAPI = replaceGenericParams(suiteData.getAPIMap().get("detectPreferenceAPI"));
		String payload = "{\"errorThreshold\":40,\"criticalThreshold\":60}";
		Logger.info("Request Payload: "+payload);
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(),restAPI);		
		return restClient.doPost(dataUri, requestHeader, null, new StringEntity(payload));
	}
	/**
	 * 
	 * @param suiteData
	 * @param requestHeader
	 * @return
	 * @throws Exception
	 * @throws JsonProcessingException
	 * @throws IOException
	 */
	public HttpResponse getDashboardPoliciesData() throws Exception, JsonProcessingException, IOException {
		String restAPI = replaceGenericParams(suiteData.getAPIMap().get("getDashboardPoliciesData"));
		StringEntity entity = new StringEntity("{\"from_date\":\""+DateUtils.getPreviousMonthDateFromCurrentTime(1)+"\",\"to_date\":\""+DateUtils.getCurrentTime()+"\"}");			
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(),restAPI);		
		return restClient.doPost(dataUri, requestHeader, null, entity);	
	}
	
	/**
	 * @param restClient
	 * @param suiteData
	 * @param requestHeader
	 * @return
	 * @throws Exception
	 * @throws JsonProcessingException
	 * @throws IOException
	 */
	public HttpResponse getDashboardPolicyAlertsData() throws Exception, JsonProcessingException, IOException {
		String restAPI = replaceGenericParams(suiteData.getAPIMap().get("getPolicyAlertsData"));
		StringEntity entity = new StringEntity("{\"from_date\":\""+DateUtils.getPreviousMonthDateFromCurrentTime(1)+"\",\"to_date\":\""+DateUtils.getCurrentTime()+"\"}");
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(),restAPI);		
		return restClient.doPost(dataUri, requestHeader, null, entity);	
	}
	
	/**
	 * @param restClient
	 * @param suiteData
	 * @param requestHeader
	 * @return
	 * @throws Exception
	 * @throws JsonProcessingException
	 * @throws IOException
	 */
	public HttpResponse getDashboardThreadAlertData() throws Exception, JsonProcessingException, IOException {
		String restAPI = replaceGenericParams(suiteData.getAPIMap().get("getThreadAlertsData"));
		StringEntity entity = new StringEntity("{\"from_date\":\""+DateUtils.getPreviousMonthDateFromCurrentTime(1)+"\",\"to_date\":\""+DateUtils.getCurrentTime()+"\"}");			
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(),restAPI);		
		return restClient.doPost(dataUri, requestHeader, null, entity);	
	}
	
	/**
	 * @param restClient
	 * @param suiteData
	 * @param requestHeader
	 * @return
	 * @throws Exception
	 * @throws JsonProcessingException
	 * @throws IOException
	 */
	public HttpResponse getDashboardAuditServicesData() throws Exception, JsonProcessingException, IOException {
		String restAPI = replaceGenericParams(suiteData.getAPIMap().get("getAuditServiceData"));
		StringEntity entity = new StringEntity("{\"date_range\":\"1mo\"}");			
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(),restAPI);		
		return restClient.doPost(dataUri, requestHeader, null, entity);	
	}
	
	/**
	 * @param restClient
	 * @param suiteData
	 * @param requestHeader
	 * @return
	 * @throws Exception
	 * @throws JsonProcessingException
	 * @throws IOException
	 */
	public HttpResponse getDashboardMapData() throws Exception, JsonProcessingException, IOException {
		String restAPI = replaceGenericParams(suiteData.getAPIMap().get("getMapData"));
		StringEntity entity = new StringEntity("{\"from_date\":\""+DateUtils.getPreviousMonthDateFromCurrentTime(1)+"\",\"to_date\":\""+DateUtils.getCurrentTime()+"\"}");			
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(),restAPI);		
		return restClient.doPost(dataUri, requestHeader, null, entity);	
	}
	
	
	/**
	 * @param severityLevel
	 * @return
	 * @throws Exception
	 */
	public HttpResponse getUserThreadData(String severityLevel) throws Exception{
		String restAPI = replaceGenericParams(suiteData.getAPIMap().get("getUserData"));
		String payload = "{\"sourceName\":\"DETECT\",\"filters\":[{\"severity\":{\""+severityLevel+"}}],"
				+ "\"apiServerUrl\":\""+suiteData.getApiserverHostName()+"\","
				+ "\"csrftoken\":\""+suiteData.getCSRFToken()+"\","
				+ "\"sessionid\":\""+suiteData.getSessionID()+"\","
				+ "\"userid\":\""+suiteData.getUsername()+"\"}";
		Logger.info("Get user thread Data payload: "+payload);
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(),restAPI);		
		return restClient.doPost(dataUri, requestHeader, null, new StringEntity(payload));
	}
	
	/**
	 * @param severityLevel
	 * @return
	 * @throws Exception
	 */
	public HttpResponse getUserThreadScoreData() throws Exception{
		String restAPI = replaceGenericParams(suiteData.getAPIMap().get("getDashboardUserThreadScore"));
		String payload = "{\"apiServerUrl\":\""+suiteData.getApiserverHostName()+"\",\"csrftoken\":\""+suiteData.getCSRFToken()+"\","
				+ "\"sessionid\":\""+suiteData.getSessionID()+"\",\"userid\":\""+suiteData.getUsername()+"\"}";
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(),restAPI);		
		return restClient.doPost(dataUri, requestHeader, null, new StringEntity(payload));
	}
	
	/**
	 * @param severityLevel
	 * @return
	 * @throws Exception
	 */
	public HttpResponse getTotalUsers(List<NameValuePair> queryParams) throws Exception{
		String restAPI = replaceGenericParams(suiteData.getAPIMap().get("getTotalUsers"));		
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(),restAPI,queryParams);
		return restClient.doGet(dataUri, requestHeader);
	}
	
	/**
	 * @return
	 * @throws Exception
	 */
	public HttpResponse getPolicyList(List<NameValuePair> queryParams) throws Exception {
		String restAPI = replaceGenericParams(suiteData.getAPIMap().get("getPolicyList"));
		URI dataUri;
		if(queryParams == null)				
			dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(),restAPI);
		else
			dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(),restAPI,queryParams);
		return restClient.doGet(dataUri, requestHeader);
	}
	
	/**
	 * @param queryParams
	 * @return
	 * @throws Exception
	 */
	public HttpResponse getAnamoly(List<NameValuePair> queryParams) throws Exception{
		String restAPI = replaceGenericParams(suiteData.getAPIMap().get("getAnamoly"));
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(),restAPI,queryParams);
		return restClient.doGet(dataUri, requestHeader);
	}
	
	/**
	 * @param restClient
	 * @param queryParams 	 
	 * @return
	 * @throws Exception  
	 */	
	public HttpResponse getAuditSummary() throws Exception{
		List<NameValuePair> queryParam = new ArrayList<NameValuePair>();				
		queryParam.add(new BasicNameValuePair("format", "json"));
		queryParam.add(new BasicNameValuePair("range", "1mo"));
		URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(),replaceGenericParams(suiteData.getAPIMap().get("getAuditSummary")),queryParam);
		return restClient.doGet(uri, requestHeader);
	}
	/**
	 * @param url
	 * @param suiteData
	 * @return
	 */
	private String replaceGenericParams(String url){
		if(url.contains("tenantName"))
			url = url.replace("{tenantName}", suiteData.getTenantName());
		if(url.contains("version"))
			url = url.replace("{version}", suiteData.getBaseVersion());
		return url;
	}

	/**
	 * @return
	 */
	public HttpResponse getBlockedUsers() throws Exception {
		String restAPI = replaceGenericParams(suiteData.getAPIMap().get("getBlockedUsers"));
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(),restAPI);
		return restClient.doGet(dataUri, requestHeader);
	}

	/**
	 * @return
	 * @throws Exception 
	 */
	public HttpResponse getCurrentUserID() throws Exception {
		String restAPI = replaceGenericParams(suiteData.getAPIMap().get("getUserDetailsAPI"));
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(),restAPI);
		return restClient.doGet(dataUri, requestHeader);	
	}
	/**
	 * @param string
	 * @param string2
	 * @return
	 * @throws Exception 
	 * @throws UnsupportedEncodingException 
	 */
	public HttpResponse updateUser(String userID, String payload) throws UnsupportedEncodingException, Exception {
		String restAPI = replaceGenericParams(suiteData.getAPIMap().get("updateUserDetailsAPI"));
		restAPI = restAPI.replace("{id}", userID);
		restAPI = restAPI.replace("{tenantName}", suiteData.getTenantName());
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(),restAPI);					
		return restClient.doPatch(dataUri, requestHeader, null, new StringEntity(payload));		
	}
	
	/**
	 * @param auditSummaryObj
	 * @return
	 * @throws JSONException 
	 */
	public int getTotalSessionsInAudit(JSONObject auditSummaryObj) throws JSONException {
		int sessionCount = 0;
		JSONArray topUsedService = auditSummaryObj.getJSONArray("top_used_services");
		for(int i= 0;i<topUsedService.length();i++){
			JSONArray userArray = topUsedService.getJSONObject(i).getJSONArray("top_users");
			for(int j=0;j<userArray.length();j++){
				sessionCount+=userArray.getJSONObject(j).getInt("sessions");
			}
		}
		return sessionCount;
	}

	/**
	 * @param userThreadScoreObj
	 * @param highUserseverity 
	 * @return
	 * @throws JSONException 
	 */
	public Map<Integer, Integer> getUserstoScoreMap(JSONObject userThreadScoreObj, USER_SEVERITY severityType) throws JSONException {
		Map<Integer, Integer> useMap = new HashedMap();
		int lowerBoundry = getLowerBoundaryValue(severityType);
		int higherBoundry = gethigherBoundaryValue(severityType);
		JSONArray terms = userThreadScoreObj.getJSONObject("facets").getJSONObject("user_threatscore").getJSONArray("terms");
		for(int i = 0;i<terms.length();i++){
			JSONObject termObject = terms.getJSONObject(i);
			int termScore = termObject.getInt("term"); 
			if(termScore >= lowerBoundry && termScore<= higherBoundry){
				useMap.put(termObject.getInt("term"), termObject.getInt("count"));
			}
		}
		return useMap;
	}

	/**
	 * @param severityType
	 * @return
	 */
	private int getLowerBoundaryValue(USER_SEVERITY severityType) {
		if(severityType.equals(USER_SEVERITY.HIGH_USERSEVERITY))
			return 60;
		if(severityType.equals(USER_SEVERITY.MEDIUM_USERSEVERITY))
			return 41;
		if(severityType.equals(USER_SEVERITY.LOW_USERSEVERITY))
			return 1;
		return 0;
	}

	/**
	 * @param severityType
	 * @return
	 */
	private int gethigherBoundaryValue(USER_SEVERITY severityType) {
		if(severityType.equals(USER_SEVERITY.HIGH_USERSEVERITY))
			return 100;
		if(severityType.equals(USER_SEVERITY.MEDIUM_USERSEVERITY))
			return 59;
		if(severityType.equals(USER_SEVERITY.LOW_USERSEVERITY))
			return 40;
		return 0;
	}

	/**
	 * @param jsonObject
	 * @return
	 * @throws JSONException 
	 */
	public Map<Integer, Integer> getUsersGoupWithSameThreadScore(JSONObject usersList) throws JSONException {
		Map<Integer,Integer> userMap = new HashedMap();
		if(usersList.getJSONObject("users_list").length()>0){
			String[] userObjList = JSONObject.getNames(usersList.getJSONObject("users_list"));	
			for(int i=0;i<userObjList.length;i++){
				JSONObject object = usersList.getJSONObject("users_list").getJSONObject(userObjList[i]);			
				int counter = 0;
				if(userMap.containsKey(object.getInt("threat_score"))){
					counter = userMap.get(object.getInt("threat_score"));
					userMap.remove(object.getInt("threat_score"));
					userMap.put(object.getInt("threat_score"),counter+=1);
				}else{
					userMap.put(object.getInt("threat_score"),counter+=1);
				}
			}
		}			
		return userMap;
	}

	/**
	 * @param auditSummaryObj
	 * @return
	 * @throws JSONException 
	 */
	public Object getTotalVolumeInAudit(JSONObject auditSummaryObj) throws JSONException {
		long totalTraffic = 0;
		JSONArray topUsedService = auditSummaryObj.getJSONArray("top_used_services");
		for(int i= 0;i<topUsedService.length();i++){
			JSONArray userArray = topUsedService.getJSONObject(i).getJSONArray("top_users");
			for(int j=0;j<userArray.length();j++){
				totalTraffic+=userArray.getJSONObject(j).getLong("traffic");
			}
		}
		return totalTraffic;
	}
}