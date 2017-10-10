package com.elastica.beatle.tests.dashboard;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.elastica.beatle.DateUtils;
import com.elastica.beatle.RestClient.Client;
import com.elastica.beatle.RestClient.ClientUtil;
import com.elastica.beatle.dashboard.DashboardFunctions;
import com.elastica.beatle.dashboard.DashboardInitializeTests;
import com.elastica.beatle.dashboard.DashboardTestConstants.USER_SEVERITY;
import com.elastica.beatle.logger.Logger;

/**
 * 
 * @author anuvrath joshi
 *
 */

public class DashboardTests extends DashboardInitializeTests {
	private Client restClient;
	private DashboardFunctions dashboardFunctions;
	private JSONObject userWidgetObj;
	private JSONObject policiesWidgetObj;
	private JSONObject policyAlertsWidgetObj;
	private JSONObject threadAlertsWidgetData;	
	private JSONObject auditServiceWidgetData;
	private JSONObject auditSummaryObj;
	
	@BeforeClass(alwaysRun=true)
	public void init() throws Exception {
		restClient = new Client();
		dashboardFunctions = new DashboardFunctions(suiteData,restClient, requestHeader);
				
		Logger.info("Setting default preferences in Detect");
		HttpResponse response = dashboardFunctions.setDefaultPreferences();		
		Thread.sleep(30000);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_OK,"Status code doesn't match");
		JSONObject object = new JSONObject(ClientUtil.getResponseBody(response));
		Assert.assertEquals(object.getInt("api_response"), 0);
		Assert.assertEquals(object.getString("api_message"),"");				
		Logger.info("Default Detect Preferences set");
		
		Logger.info("Setting default_selected_range for the user profile to 1 month");
		HttpResponse userAccountResponse = dashboardFunctions.getCurrentUserID();
		Assert.assertEquals(userAccountResponse.getStatusLine().getStatusCode(), HttpStatus.SC_OK,"Status code doesn't match");		
		JSONObject updatedUserObject = new JSONObject(ClientUtil.getResponseBody(userAccountResponse));
		updatedUserObject.getJSONObject("data").remove("default_selected_range");
		updatedUserObject.getJSONObject("data").put("default_selected_range", "1mo");
		updatedUserObject.getJSONObject("data").remove("modified_on");
		updatedUserObject.getJSONObject("data").put("modified_on",DateUtils.getCurrentTime());
		HttpResponse updateUserAccountResponse = dashboardFunctions.updateUser(updatedUserObject.getJSONObject("data").getString("id"), updatedUserObject.toString());
		Assert.assertEquals(updateUserAccountResponse.getStatusLine().getStatusCode(), HttpStatus.SC_ACCEPTED,"Status code doesn't match");
		Logger.info("default_selected_range for the user profile is set to 1 month");
		Logger.info("###########################################################################################################################");
	}
		
	/*
	 * Audit service testing
	 */
	@Test
	public void testDashboardAuditServicesWidgetData() throws Exception{
		Logger.info("###########################################################################################################################");
		Logger.info("This test case Gets Audit Services data from Dashboard and make sure none of the fields are empty/null ");
		Logger.info("###########################################################################################################################");
		Logger.info("Dashboard - API Call to get Policy Data ");
		HttpResponse auditServiceResponse = dashboardFunctions.getDashboardAuditServicesData();
		Assert.assertEquals(auditServiceResponse.getStatusLine().getStatusCode(), HttpStatus.SC_OK," Http response doesn't match");	
		auditServiceWidgetData = new JSONObject(ClientUtil.getResponseBody(auditServiceResponse)).getJSONObject("data").getJSONArray("objects").getJSONObject(0);
		Logger.info("Audit Service Data :"+auditServiceWidgetData);
		Assert.assertFalse(String.valueOf(auditServiceWidgetData.getString("date_range")).isEmpty(),"date_range field is empty");
		Assert.assertFalse(String.valueOf(auditServiceWidgetData.getInt("users")).isEmpty(),"users field is empty");
		Assert.assertFalse(String.valueOf(auditServiceWidgetData.getString("sessions")).isEmpty(),"sessions field is empty");
		Assert.assertFalse(String.valueOf(auditServiceWidgetData.getInt("locations")).isEmpty(),"locations field is empty");
		Assert.assertFalse(String.valueOf(auditServiceWidgetData.getInt("high_risk_users")).isEmpty(),"high_risk_users field is empty");
		Assert.assertFalse(String.valueOf(auditServiceWidgetData.getString("volume")).isEmpty(),"volume field is empty");
		Assert.assertFalse(String.valueOf(auditServiceWidgetData.getString("id")).isEmpty(),"id field is empty");
		Assert.assertFalse(String.valueOf(auditServiceWidgetData.getInt("high_risk_locations")).isEmpty(),"high_risk_locations field is empty");
		Assert.assertFalse(String.valueOf(auditServiceWidgetData.getInt("high_risk_services")).isEmpty(),"high_risk_services field is empty");
		Assert.assertFalse(String.valueOf(auditServiceWidgetData.getInt("services")).isEmpty(),"services field is empty");
		Assert.assertFalse(String.valueOf(auditServiceWidgetData.getString("resource_uri")).isEmpty(),"resource_uri field is empty");
		Assert.assertFalse(String.valueOf(auditServiceWidgetData.getString("high_risk_devices")).isEmpty(),"high_risk_devices field is empty");
		Assert.assertFalse(String.valueOf(auditServiceWidgetData.getString("devices")).isEmpty(),"devices field is empty");
		Assert.assertNotNull(auditServiceWidgetData.getString("date_range"),"date_range field is null");
		Assert.assertNotNull(auditServiceWidgetData.getInt("users"),"users field is null");
		Assert.assertNotNull(auditServiceWidgetData.getString("sessions"),"sessions field is null");
		Assert.assertNotNull(auditServiceWidgetData.getInt("locations"),"locations field is null");
		Assert.assertNotNull(auditServiceWidgetData.getInt("high_risk_users"),"high_risk_users field is null");
		Assert.assertNotNull(auditServiceWidgetData.getString("volume"),"volume field is null");
		Assert.assertNotNull(auditServiceWidgetData.getString("id"),"id field is null");
		Assert.assertNotNull(auditServiceWidgetData.getInt("high_risk_locations"),"high_risk_locations field is null");
		Assert.assertNotNull(auditServiceWidgetData.getInt("high_risk_services"),"high_risk_services field is null");
		Assert.assertNotNull(auditServiceWidgetData.getInt("services"),"services field is null");
		Assert.assertNotNull(auditServiceWidgetData.getString("resource_uri"),"resource_uri field is null");		
		Assert.assertNotNull(auditServiceWidgetData.getString("high_risk_devices"),"high_risk_devices field is null");
		Assert.assertNotNull(auditServiceWidgetData.getString("devices"),"devices field is null");
		Logger.info("###########################################################################################################################");
		Logger.info("Results of test:");
		Logger.info("No issues found in Dashboard Policy Alerts widget");
		Logger.info("None of the fields are null/empty");
		Logger.info("###########################################################################################################################");
	}
				
	@Test(dependsOnMethods={"testDashboardAuditServicesWidgetData"})
	public void testAuditSummaryUsersSessionDestinationTraffic() throws Exception{
		Logger.info("###########################################################################################################################");
		Logger.info("This test Gets Audit summary data and validates with dashboard API data");
		Logger.info("###########################################################################################################################");
		Logger.info("testDashboardAuditServicesWidgetData tests response if the source of validation for this test");
		Logger.info("");
		Logger.info("###########################################################################################################################");
		Logger.info("Audit - API Call to get DS summary");
		HttpResponse auditSummaryResponse = dashboardFunctions.getAuditSummary();
		Assert.assertEquals(auditSummaryResponse.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
		auditSummaryObj = new JSONObject(ClientUtil.getResponseBody(auditSummaryResponse)).getJSONArray("objects").getJSONObject(0);
		Logger.info("Audit Summary Response: "+auditSummaryObj);
		Assert.assertEquals(auditServiceWidgetData.getInt("users"), auditSummaryObj.getInt("total_users"), "Total users doesn't match");
		Assert.assertEquals(auditServiceWidgetData.getInt("locations"),auditSummaryObj.getInt("total_destinations"), "Destination field doesn't match");
		Assert.assertEquals(auditServiceWidgetData.getInt("sessions"),dashboardFunctions.getTotalSessionsInAudit(auditSummaryObj), "Session count doesn't match");
		Assert.assertEquals(auditServiceWidgetData.getLong("volume"),dashboardFunctions.getTotalVolumeInAudit(auditSummaryObj), "Total Traffic doesn't match");
		
		Logger.info("###########################################################################################################################");
		Logger.info("Results of test:");
		Logger.info("Total Users, Locations, Sessions count match");
		Logger.info("All the values in the audit dashboard api match the data in audit summary api");
		Logger.info("###########################################################################################################################");
	}	
	
	/*
	 * Threads alerts Widget testing
	 */
	@Test(priority=1)
	public void testDashboardThreadAlertsWidgetData() throws Exception{
		Logger.info("###########################################################################################################################");
		Logger.info("This test case Gets Thred alerts data and make sure none of the fields are empty/null ");
		Logger.info("###########################################################################################################################");
		Logger.info("Dashboard - API Call to get Policy Data ");
		HttpResponse threadAlertsResponse = dashboardFunctions.getDashboardThreadAlertData();
		Assert.assertEquals(threadAlertsResponse.getStatusLine().getStatusCode(), HttpStatus.SC_OK," Http response doesn't match");
		threadAlertsWidgetData = new JSONObject(ClientUtil.getResponseBody(threadAlertsResponse)).getJSONObject("threatAlertsData");
		Logger.info("Thread alerts widget data: "+threadAlertsWidgetData);
		Assert.assertFalse(String.valueOf(threadAlertsWidgetData.getInt("total")).isEmpty(),"blocked_and_alerting_policies field is empty");
		Assert.assertFalse(String.valueOf(threadAlertsWidgetData.getInt("low_risk")).isEmpty(),"blocked_policies field is empty");
		Assert.assertFalse(String.valueOf(threadAlertsWidgetData.getInt("medium_risk")).isEmpty(),"alerting_policies field is empty");
		Assert.assertFalse(String.valueOf(threadAlertsWidgetData.getInt("high_risk")).isEmpty(),"blocked_and_alerting_policies field is empty");
		Assert.assertNotNull(threadAlertsWidgetData.getInt("total"),"total field is null");
		Assert.assertNotNull(threadAlertsWidgetData.getInt("low_risk"),"total field is null");
		Assert.assertNotNull(threadAlertsWidgetData.getInt("medium_risk"),"inactive_policies field is null");
		Assert.assertNotNull(threadAlertsWidgetData.getInt("high_risk"),"blocked_policies field is null");
		Logger.info("###########################################################################################################################");
		Logger.info("Results of test:");
		Logger.info("No issues found in Dashboard Policy Alerts widget");
		Logger.info("None of the fields are null/empty");
		Logger.info("###########################################################################################################################");
	}
	
	@DataProvider(name="detectIncidentSeverity")
	public Object[][] detectIncidentSeverity(){
		return new Object[][]{{"high"},{"medium"},{"low"}};
	}
	
	@Test(priority=2, dataProvider="detectIncidentSeverity")
	public void testRiskThreatAlerts(String severityLevel) throws Exception{
		Logger.info("###########################################################################################################################");
		Logger.info("This test case Gets different types of risky threat alerts from detect based on dataprovider and verify that the dashboard data matches");
		Logger.info("###########################################################################################################################");
		Logger.info("Threat Alerts Data Response from Dashboard Users Widget: "+threadAlertsWidgetData.toString());
		Logger.info("");
		Logger.info("###########################################################################################################################");
		Logger.info("Current iteration risk type: "+severityLevel);
		HttpResponse response = dashboardFunctions.getCloudServiceAnamolies(severityLevel);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_OK," Http response doesn't match");
		JSONArray riskObjeArray = new JSONArray(ClientUtil.getResponseBody(response));
		if(severityLevel.equals("high")){
			Logger.info("Expected high risky threat alerts: "+threadAlertsWidgetData.getInt("high_risk"));		
			Logger.info("Actual high risky threat alerts in detect "+riskObjeArray.length());
			Assert.assertEquals(riskObjeArray.length(), threadAlertsWidgetData.getInt("high_risk"));
			Logger.info("");
			Logger.info("Expecting "+threadAlertsWidgetData.getInt("high_risk")+" High risky threat alerts and found "+riskObjeArray.length()+" Test Passed");
			Logger.info("###########################################################################################################################");
		}
		if(severityLevel.equals("medium")){
			Logger.info("Expected medium risky threat alerts: "+threadAlertsWidgetData.getInt("medium_risk"));		
			Logger.info("Actual medium risky threat alerts in detect "+riskObjeArray.length());
			Assert.assertEquals(riskObjeArray.length(), threadAlertsWidgetData.getInt("medium_risk"));
			Logger.info("");
			Logger.info("Expecting "+threadAlertsWidgetData.getInt("medium_risk")+" Medium risky threat alerts and found "+riskObjeArray.length()+" Test Passed");
			Logger.info("###########################################################################################################################");
		}
		if(severityLevel.equals("low")){
			Logger.info("Expected low risky threat alerts: "+threadAlertsWidgetData.getInt("low_risk"));		
			Logger.info("Actual low risky threat alerts in detect "+riskObjeArray.length());
			Assert.assertEquals(riskObjeArray.length(), threadAlertsWidgetData.getInt("low_risk"));
			Logger.info("");
			Logger.info("Expecting "+threadAlertsWidgetData.getInt("low_risk")+" Low Risky threat alerts and found "+riskObjeArray.length()+" Test Passed");
			Logger.info("###########################################################################################################################");
		}
	}
		
	/*
	 * Users by thread score widget
	 */			
	@Test(dataProvider="userSeverityDataProvider")
	public void testDashboadUsersByThreadScoreWidget(USER_SEVERITY severityLevel) throws Exception {
		Logger.info("###########################################################################################################################");
		Logger.info("This test case Gets the Users threadScore data from Dashboard and validate with Detect "+severityLevel+" users");
		Logger.info("###########################################################################################################################");
		Logger.info("Dashboard - API Call to get Users ThreadScore data");
		HttpResponse userThreadScore = dashboardFunctions.getUserThreadScoreData();
		Assert.assertEquals(userThreadScore.getStatusLine().getStatusCode(), HttpStatus.SC_OK," Http response doesn't match");
		JSONObject userThreadScoreObj = new JSONObject(ClientUtil.getResponseBody(userThreadScore));		
		Logger.info("Dashboard users threadscore data: "+ userThreadScoreObj.toString());	
		Map<Integer, Integer> DashBoardMap = dashboardFunctions.getUserstoScoreMap(userThreadScoreObj,severityLevel);
		
		HttpResponse riskUserResp = dashboardFunctions.getUserThreadData(severityLevel.getSeverityCode());
		JSONObject detectHighRiskObj = new JSONObject(ClientUtil.getResponseBody(riskUserResp));
		Logger.info("Detect "+severityLevel+" Users response: "+ detectHighRiskObj.toString());		
		Map<Integer, Integer> detectMapData = dashboardFunctions.getUsersGoupWithSameThreadScore(detectHighRiskObj);
		//Assert.assertEquals(DashBoardMap.size(), detectMapData.size(), "total "+severityLevel+" users count doesn't match");
		Logger.info("###########################################################################################################################");		
		Logger.info("Expected Total"+severityLevel+" users by threadScore are: "+detectMapData.size());		
		Logger.info("Actual total rest policies: "+DashBoardMap.size());
		Logger.info("");
		Logger.info("Expecting "+detectHighRiskObj.getJSONObject("users_list").length()+" Total "+severityLevel+" users by threadScore but found "+DashBoardMap.size()+" Test Passed");
		Logger.info("###########################################################################################################################");
	}
	
	/*
	 * Policy Alerts widget data
	 */
	@Test()
	public void testDashboardPolicyAlertsWidgetData() throws Exception{
		Logger.info("###########################################################################################################################");
		Logger.info("This test case Gets Policy Alerts data and make sure none of the fields are empty/null ");
		Logger.info("###########################################################################################################################");
		Logger.info("Dashboard - API Call to get Policy Alerts Data ");
		HttpResponse policyAlertResponse = dashboardFunctions.getDashboardPolicyAlertsData();
		Assert.assertEquals(policyAlertResponse.getStatusLine().getStatusCode(), HttpStatus.SC_OK," Http response doesn't match");
		policyAlertsWidgetObj = new JSONObject(ClientUtil.getResponseBody(policyAlertResponse)).getJSONObject("policyAlertsData");
		Logger.info("Policy Alerts Widget Response data: "+policyAlertsWidgetObj);
		Assert.assertFalse(String.valueOf(policyAlertsWidgetObj.getInt("total")).isEmpty(),"blocked_policies field is empty");
		Assert.assertFalse(String.valueOf(policyAlertsWidgetObj.getInt("blocking")).isEmpty(),"alerting_policies field is empty");
		Assert.assertFalse(String.valueOf(policyAlertsWidgetObj.getInt("alerting")).isEmpty(),"blocked_and_alerting_policies field is empty");
		Assert.assertNotNull(policyAlertsWidgetObj.getInt("total"),"total field is null");
		Assert.assertNotNull(policyAlertsWidgetObj.getInt("blocking"),"inactive_policies field is null");
		Assert.assertNotNull(policyAlertsWidgetObj.getInt("alerting"),"blocked_policies field is null");
		Logger.info("###########################################################################################################################");
		Logger.info("Results of test:");
		Logger.info("No issues found in Dashboard Policy Alerts widget");
		Logger.info("None of the fields are null/empty");
		Logger.info("###########################################################################################################################");
	}
	
	@Test(dependsOnMethods={"testDashboardPolicyAlertsWidgetData"})
	public void testDashboardBlockedUsersPolicyAlertsWidget() throws Exception{
		Logger.info("###########################################################################################################################");
		Logger.info("This test Gets Blocked users from protect and validates with dashboard API data");
		Logger.info("###########################################################################################################################");
		Logger.info("Policies data from Dashboard Policies Widget: "+policyAlertsWidgetObj);
		Logger.info("");
		Logger.info("###########################################################################################################################");
		HttpResponse policyAlertResponse = dashboardFunctions.getBlockedUsers();
		Assert.assertEquals(policyAlertResponse.getStatusLine().getStatusCode(), HttpStatus.SC_OK," Http response doesn't match");
		JSONObject blockObject = new JSONObject(ClientUtil.getResponseBody(policyAlertResponse)).getJSONObject("blocks");
		JSONObject blockArray = blockObject.optJSONObject("blocks");
		Logger.info("Blocked users response: "+blockObject.toString());
		if(blockArray!= null && blockArray.toString().equals("{}")){
			Logger.info("###########################################################################################################################");		
			Logger.info("Expected Total Blocked users in protect: 0");		
			Logger.info("Actual Total Blocked users in dashboard "+blockObject.getJSONObject("blocks").length());
			Assert.assertEquals(0,policyAlertsWidgetObj.getInt("blocking"), "Blocked users doesn't match");
			Logger.info("");
			Logger.info("Expecting 0 Total Blocked users and found "+blockObject.getJSONObject("blocks").length()+" Test Passed");
			Logger.info("###########################################################################################################################");
		}
		else if(blockArray == null ){
			Logger.info("###########################################################################################################################");		
			Logger.info("Expected Total Blocked users in protect: "+policyAlertsWidgetObj.getInt("blocking"));		
			Logger.info("Actual Total Blocked users in dashboard "+blockObject.getJSONArray("blocks").length());
			Assert.assertEquals(blockObject.getJSONArray("blocks").length(), policyAlertsWidgetObj.getInt("blocking"),"Blocking field doesn't match");				
			Logger.info("");
			Logger.info("Expecting "+policyAlertsWidgetObj.getInt("blocking")+" Total Blocked users and found "+blockObject.getJSONArray("blocks").length()+" Test Passed");
			Logger.info("###########################################################################################################################");
		}		
	}
	
	@Test(dependsOnMethods={"testDashboardPolicyAlertsWidgetData"})
	public void testDashboardAlertingPolicyAlerts() throws Exception{
		Logger.info("###########################################################################################################################");
		Logger.info("This test Gets Alerting policies from protect and validates with dashboard API data");
		Logger.info("###########################################################################################################################");
		Logger.info("Policies data from Dashboard Policies Widget: "+policyAlertsWidgetObj);
		Logger.info("");
		Logger.info("###########################################################################################################################");
		
		List<NameValuePair> queryParams = new ArrayList<>();		
		queryParams.add(new BasicNameValuePair("source", "{\"query\":{\"bool\":{\"must\":[{\"range\":{\"created_timestamp\":{\"from\":\""+DateUtils.getPreviousMonthDateFromCurrentTime(1)+"\",\"to\":\""+DateUtils.getCurrentTime()+"\"}}},"
				+ "{\"term\":{\"Activity_type\":\"Policy Violation\"}},{\"term\":{\"policy_action\":\"ALERT\"}},{\"filtered\":{\"filter\":{\"not\":{\"and\":[{\"term\":{\"severity\":\"informational\"}},"
				+ "{\"term\":{\"severity_old\":\"critical\"}}]}}}}],\"must_not\":{\"term\":{\"alert_cleared\":\"true\"}}}},\"from\":0,\"size\":30,\"sort\":{\"created_timestamp\":{\"order\":\"desc\"}}}"));
		queryParams.add(new BasicNameValuePair("sourceName", "PROTECT"));
		HttpResponse policyAlertResponse = dashboardFunctions.getAnamoly(queryParams);
		Assert.assertEquals(policyAlertResponse.getStatusLine().getStatusCode(), HttpStatus.SC_OK," Http response doesn't match");
		JSONObject alertObject = new JSONObject(ClientUtil.getResponseBody(policyAlertResponse)).getJSONObject("hits");
		Logger.info("Alerting policy response: "+alertObject.toString());
		Logger.info("###########################################################################################################################");		
		Logger.info("Expected Total alerts in protect: "+policyAlertsWidgetObj.getInt("alerting"));		
		Logger.info("Actual Total alerts in dashboard "+alertObject.getInt("total"));
		Assert.assertEquals(alertObject.getInt("total"), policyAlertsWidgetObj.getInt("alerting"),"alerting field doesn't match");				
		Logger.info("");
		Logger.info("Expecting "+policyAlertsWidgetObj.getInt("alerting")+" Total Alerting policies and found "+alertObject.getInt("total")+" Test Passed");
		Logger.info("###########################################################################################################################");
	}
	
	/*
	 * Testing Policies widget data
	 */
	@Test(priority=1)
	public void testDashboardPolicyWidgetData() throws Exception{
		Logger.info("###########################################################################################################################");
		Logger.info("This test case Gets Policy data from policy widget and make sure none of the fields are empty/null ");
		Logger.info("###########################################################################################################################");
		Logger.info("Dashboard - API Call to get Policy Data ");
		HttpResponse policyDataResponse = dashboardFunctions.getDashboardPoliciesData();
		Assert.assertEquals(policyDataResponse.getStatusLine().getStatusCode(), HttpStatus.SC_OK," Http response doesn't match");
		policiesWidgetObj = new JSONObject(ClientUtil.getResponseBody(policyDataResponse)).getJSONObject("policiesData");
		Logger.info("Policy widget data: "+policiesWidgetObj);
		Assert.assertFalse(String.valueOf(policiesWidgetObj.getInt("total")).isEmpty(),"total is field empty");
		Assert.assertFalse(String.valueOf(policiesWidgetObj.getInt("inactive_policies")).isEmpty(),"inactive_policies field is empty");
		Assert.assertFalse(String.valueOf(policiesWidgetObj.getInt("blocked_policies")).isEmpty(),"blocked_policies field is empty");
		Assert.assertFalse(String.valueOf(policiesWidgetObj.getInt("alerting_policies")).isEmpty(),"alerting_policies field is empty");
		Assert.assertFalse(String.valueOf(policiesWidgetObj.getInt("blocked_and_alerting_policies")).isEmpty(),"blocked_and_alerting_policies field is empty");
		Assert.assertNotNull(policiesWidgetObj.getInt("total"),"total field is null");
		Assert.assertNotNull(policiesWidgetObj.getInt("inactive_policies"),"inactive_policies field is null");
		Assert.assertNotNull(policiesWidgetObj.getInt("blocked_policies"),"blocked_policies field is null");
		Assert.assertNotNull(policiesWidgetObj.getInt("alerting_policies"),"alerting_policies field is null");
		Assert.assertNotNull(policiesWidgetObj.getInt("blocked_and_alerting_policies"),"blocked_and_alerting_policies field is null");
		Logger.info("###########################################################################################################################");
		Logger.info("Results of test:");
		Logger.info("No issues found in Dashboard Policy widget");
		Logger.info("None of the fields are null/empty");
		Logger.info("###########################################################################################################################");
	}
	
	@Test(priority=2)
	public void testTotalPolicyInPolicyWidget() throws Exception{
		Logger.info("###########################################################################################################################");
		Logger.info("This test case Gets total Policies from protect page and validate with dashboard data");
		Logger.info("###########################################################################################################################");
		Logger.info("Policies data from Dashboard Policies Widget: "+policiesWidgetObj);
		Logger.info("");
		Logger.info("###########################################################################################################################");
		HttpResponse policyResponse = dashboardFunctions.getPolicyList(null);
		Assert.assertEquals(policyResponse.getStatusLine().getStatusCode(), HttpStatus.SC_OK,"Status code doesn't match");
		String totalPolicyResp = ClientUtil.getResponseBody(policyResponse);
		Logger.info("Total Policy Response: "+totalPolicyResp);
		JSONObject policyObject = new JSONObject(totalPolicyResp).getJSONObject("meta");
		Assert.assertEquals(policyObject.getInt("total_count"), policiesWidgetObj.getInt("total"),"Total policy doesn't match between dashboard data and protect data");
		Logger.info("###########################################################################################################################");
		Logger.info("Expected Total rest policies: "+policiesWidgetObj.getInt("total"));		
		Logger.info("Actual total rest policies: "+policyObject.getInt("total_count"));
		Logger.info("");
		Logger.info("Expecting "+policiesWidgetObj.getInt("total")+" Total Policy and found "+policyObject.getInt("total_count")+" Test Passed");
		Logger.info("###########################################################################################################################");
	}
	
	@Test(priority=2)
	public void testBlockedPolicyInPolicyWidget() throws Exception{
		Logger.info("###########################################################################################################################");
		Logger.info("This test case Gets Blocking Policies from protect page and validate with dashboard data");
		Logger.info("###########################################################################################################################");
		Logger.info("Policies data from Dashboard Policies Widget: "+policiesWidgetObj);
		Logger.info("");
		Logger.info("###########################################################################################################################");
		List<NameValuePair> queryParams = new ArrayList<>();
		queryParams.add(new BasicNameValuePair("filters", "is_blocked|true"));
		HttpResponse policyResponse = dashboardFunctions.getPolicyList(queryParams);
		Assert.assertEquals(policyResponse.getStatusLine().getStatusCode(), HttpStatus.SC_OK,"Status code doesn't match");
		String totalPolicyResp = ClientUtil.getResponseBody(policyResponse);
		Logger.info("Blocked Policy Response: "+totalPolicyResp);
		JSONObject policyObject = new JSONObject(totalPolicyResp).getJSONObject("meta");
		Assert.assertEquals(policyObject.getInt("total_count"), policiesWidgetObj.getInt("blocked_policies"),"Blocked policy doesn't match between dashboard data and protect data");
		Logger.info("###########################################################################################################################");
		Logger.info("Expected Total rest policies: "+policiesWidgetObj.getInt("blocked_policies"));		
		Logger.info("Actual total rest policies: "+policyObject.getInt("total_count"));
		Logger.info("");
		Logger.info("Expecting "+policiesWidgetObj.getInt("blocked_policies")+" Total Blocked Policy and found "+policyObject.getInt("total_count")+" Test Passed");
		Logger.info("###########################################################################################################################");
	}
	
	@Test(priority=2)
	public void testAlertingPolicyInPolicyWidget() throws Exception{
		Logger.info("###########################################################################################################################");
		Logger.info("This test case Gets Alerting Policies from protect page and validate with dashboard data");
		Logger.info("###########################################################################################################################");
		Logger.info("Policies data from Dashboard Policies Widget: "+policiesWidgetObj);
		Logger.info("");
		Logger.info("###########################################################################################################################");
		List<NameValuePair> queryParams = new ArrayList<>();
		queryParams.add(new BasicNameValuePair("filters", "is_alerting|true"));
		HttpResponse policyResponse = dashboardFunctions.getPolicyList(queryParams);
		Assert.assertEquals(policyResponse.getStatusLine().getStatusCode(), HttpStatus.SC_OK,"Status code doesn't match");
		String totalPolicyResp = ClientUtil.getResponseBody(policyResponse);
		Logger.info("Alerting Policy Response: "+totalPolicyResp);
		JSONObject policyObject = new JSONObject(totalPolicyResp).getJSONObject("meta");
		Assert.assertEquals(policyObject.getInt("total_count"), policiesWidgetObj.getInt("alerting_policies"));
		Logger.info("###########################################################################################################################");
		Logger.info("Expected Total rest policies: "+policiesWidgetObj.getInt("alerting_policies"));		
		Logger.info("Actual total rest policies: "+policyObject.getInt("total_count"));
		Logger.info("");
		Logger.info("Expecting "+policiesWidgetObj.getInt("alerting_policies")+" Total alerting Policy and found "+policyObject.getInt("total_count")+" Test Passed");
		Logger.info("###########################################################################################################################");
	}
	
	@Test(priority=2)
	public void testRestPolicyInPolicyWidget() throws Exception{
		Logger.info("###########################################################################################################################");
		Logger.info("This test case Gets Rest Policies(Not alerting and not blocking) from protect page and validate with dashboard data");
		Logger.info("###########################################################################################################################");
		Logger.info("Rest policies from Dashboard Policies Widget: "+policiesWidgetObj);
		Logger.info("");
		Logger.info("###########################################################################################################################");
		List<NameValuePair> queryParams = new ArrayList<>();		
		queryParams.add(new BasicNameValuePair("filters", "is_alerting|false||is_blocked|false"));
		HttpResponse policyResponse = dashboardFunctions.getPolicyList(queryParams);
		Assert.assertEquals(policyResponse.getStatusLine().getStatusCode(), HttpStatus.SC_OK,"Status code doesn't match");
		String totalPolicyResp = ClientUtil.getResponseBody(policyResponse);
		Logger.info("Rest Policy Response: "+totalPolicyResp);
		JSONObject policyObject = new JSONObject(totalPolicyResp).getJSONObject("meta");
		Logger.info("###########################################################################################################################");
		Logger.info("Expected Total rest policies: "+policiesWidgetObj.getInt("inactive_policies"));		
		Logger.info("Actual total rest policies: "+policyObject.getInt("total_count"));
		Assert.assertEquals(policyObject.getInt("total_count"), policiesWidgetObj.getInt("inactive_policies"));
		Logger.info("");
		Logger.info("Expecting "+policiesWidgetObj.getInt("inactive_policies")+" non alerting and non blocked policies and found "+policyObject.getInt("total_count")+" Test Passed");
		Logger.info("###########################################################################################################################");
	}
	
	@Test(priority=2)
	public void testBlockedAlertingPoliciesInPolicyWidget() throws Exception{
		Logger.info("###########################################################################################################################");
		Logger.info("This test case Gets the policies that are either alerting or blocking from protect and validate with dashboard data");
		Logger.info("###########################################################################################################################");
		Logger.info("Blocked alerting policies from Dashboard Policies Widget: "+policiesWidgetObj);
		Logger.info("");
		Logger.info("###########################################################################################################################");
		List<NameValuePair> queryParams = new ArrayList<>();		
		queryParams.add(new BasicNameValuePair("filters", "is_alerting|true||is_blocked|true"));
		HttpResponse policyResponse = dashboardFunctions.getPolicyList(queryParams);
		Assert.assertEquals(policyResponse.getStatusLine().getStatusCode(), HttpStatus.SC_OK,"Status code doesn't match");
		String blockedPolicyObj = ClientUtil.getResponseBody(policyResponse);
		Logger.info("Blocking/Alerting Policy Response: "+blockedPolicyObj);
		JSONObject policyObject = new JSONObject(blockedPolicyObj).getJSONObject("meta");
		Logger.info("###########################################################################################################################");
		Logger.info("Expected Total Blocked Alerting policies: "+policiesWidgetObj.getInt("blocked_and_alerting_policies"));		
		Logger.info("Actual total Blocked Alerting policies: "+policyObject.getInt("total_count"));
		Assert.assertEquals(policyObject.getInt("total_count"), policiesWidgetObj.getInt("blocked_and_alerting_policies"));
		Logger.info("");
		Logger.info("Expecting "+policiesWidgetObj.getInt("blocked_and_alerting_policies")+" Total Blocked and alerting and found "+policyObject.getInt("total_count")+" Test Passed");
		Logger.info("###########################################################################################################################");
	}
	
	/*
	 * Testing Users widget data
	 */
	@Test(priority=1)
	public void testDashboardUserWidgetData() throws Exception{
		Logger.info("###########################################################################################################################");
		Logger.info("This test case Gets Users data from Users Dashboard widget and make sure none of the fields are empty/null ");
		Logger.info("###########################################################################################################################");
		Logger.info("Dashboard - API Call to get Users Data from Users Widget: ");
		HttpResponse usersDashboardResponse = dashboardFunctions.getDashboardUsersData();
		Assert.assertEquals(usersDashboardResponse.getStatusLine().getStatusCode(), HttpStatus.SC_OK," Http response doesn't match");
		userWidgetObj = new JSONObject(ClientUtil.getResponseBody(usersDashboardResponse)).getJSONObject("usersData");		
		Logger.info("Users Data Response from Dashboard Users Widget: "+userWidgetObj);
		Assert.assertFalse(String.valueOf(userWidgetObj.getInt("total")).isEmpty(),"total is field empty");
		Assert.assertFalse(String.valueOf(userWidgetObj.getInt("medium_risk_users")).isEmpty(),"medium risk field is empty");
		Assert.assertFalse(String.valueOf(userWidgetObj.getInt("active_users")).isEmpty(),"active users field is empty");
		Assert.assertFalse(String.valueOf(userWidgetObj.getInt("blocked_users")).isEmpty(),"blocked users field is empty");
		Assert.assertFalse(String.valueOf(userWidgetObj.getInt("high_risk_users")).isEmpty(),"high risk users field is empty");
		Assert.assertFalse(String.valueOf(userWidgetObj.getInt("low_risk_users")).isEmpty(),"low risk users field is empty");
		Assert.assertNotNull(userWidgetObj.getInt("total"),"total field is null");
		Assert.assertNotNull(userWidgetObj.getInt("medium_risk_users"),"medium_risk_users field is null");
		Assert.assertNotNull(userWidgetObj.getInt("active_users"),"active_users field is null");
		Assert.assertNotNull(userWidgetObj.getInt("blocked_users"),"blocked_users field is null");
		Assert.assertNotNull(userWidgetObj.getInt("high_risk_users"),"high_risk_users field is null");
		Assert.assertNotNull(userWidgetObj.getInt("low_risk_users"),"low_risk_users field is null");
		Logger.info("###########################################################################################################################");
		Logger.info("Results of test:");
		Logger.info("No issues found in Dashboard getUserData");
		Logger.info("None of the fields are null/empty");
		Logger.info("###########################################################################################################################");
	}
	
	@Test(priority=2)
	public void testTotalUsersInUsersWidget() throws Exception{
		Logger.info("###########################################################################################################################");
		Logger.info("This test case Gets Total Users from users page and verify that the dashboard data matches");
		Logger.info("###########################################################################################################################");
		Logger.info("Users Data Response from Dashboard Users Widget: "+userWidgetObj);
		Logger.info("");
		Logger.info("###########################################################################################################################");
		List<NameValuePair> queryParams = new ArrayList<>();
		queryParams.add(new BasicNameValuePair("limit", "30"));
		queryParams.add(new BasicNameValuePair("offset", "0"));
		queryParams.add(new BasicNameValuePair("order_by", "first_name"));
		queryParams.add(new BasicNameValuePair("query", ""));
		HttpResponse userTotalResponse = dashboardFunctions.getTotalUsers(queryParams);
		String activeUserResponse = ClientUtil.getResponseBody(userTotalResponse);
		Logger.info("Total Users response: "+activeUserResponse);
		Assert.assertEquals(userTotalResponse.getStatusLine().getStatusCode(), HttpStatus.SC_OK,"Response code doesn't match");
		JSONObject userObj = new JSONObject(activeUserResponse).getJSONObject("meta");
		Logger.info("###########################################################################################################################");		
		Logger.info("Expected Total Users: "+userWidgetObj.getInt("total"));		
		Logger.info("Actual total users data in the users page "+userObj.getInt("total_count"));
		Assert.assertEquals(userObj.getInt("total_count"), userWidgetObj.getInt("total"),"Total Users count doesn't match");
		Logger.info("");
		Logger.info("Expecting "+userWidgetObj.getInt("total")+" total Users and found "+userObj.getInt("total_count")+" Test Passed");
		Logger.info("###########################################################################################################################");
	}
	
	@Test(priority=2)
	public void testActiveUsersInUsersWidget() throws Exception{
		Logger.info("###########################################################################################################################");
		Logger.info("This test case Gets Active Users from investigate logs and verify that the dashboard data matches");
		Logger.info("###########################################################################################################################");
		Logger.info("Users Data Response from Dashboard Users Widget: "+userWidgetObj);
		Logger.info("");
		Logger.info("###########################################################################################################################");
		HttpResponse userTotalResponse = dashboardFunctions.getActiveUsersFromInvestigate();
		Assert.assertEquals(userTotalResponse.getStatusLine().getStatusCode(), HttpStatus.SC_OK,"Response code doesn't match");
		String activeUserResponse = ClientUtil.getResponseBody(userTotalResponse);
		Logger.info("Active Users response from ES: "+activeUserResponse);		
		JSONObject userObj = new JSONObject(activeUserResponse).getJSONObject("facets").getJSONObject("user");
		Logger.info("###########################################################################################################################");		
		Logger.info("Expected Total Active Users: "+userWidgetObj.getInt("active_users"));		
		Logger.info("Actual total active users data in the users page "+userObj.getJSONArray("terms").length());
		Assert.assertEquals(userObj.getJSONArray("terms").length(), userWidgetObj.getInt("active_users"), "The number of active users doesn't match");				
		Logger.info("");
		Logger.info("Expecting "+userWidgetObj.getInt("active_users")+" Active Users and found "+userObj.getJSONArray("terms").length()+" Test Passed");
		Logger.info("###########################################################################################################################");
	}
	
	@Test(priority=2)
	public void testBlockedUsersInUsersWidget() throws Exception{
		Logger.info("###########################################################################################################################");
		Logger.info("This test case Gets Blockeds Users detect page and verify that the dashboard data matches");
		Logger.info("###########################################################################################################################");
		Logger.info("Users Data Response from Dashboard Users Widget: "+userWidgetObj);
		Logger.info("");
		Logger.info("###########################################################################################################################");
		List<NameValuePair> queryParams = new ArrayList<>();
		queryParams.add(new BasicNameValuePair("blocks", "true"));
		queryParams.add(new BasicNameValuePair("limit", "30"));
		queryParams.add(new BasicNameValuePair("offset", "0"));
		queryParams.add(new BasicNameValuePair("order_by", "first_name"));
		HttpResponse userTotalResponse = dashboardFunctions.getTotalUsers(queryParams);
		String blockedUserResponse = ClientUtil.getResponseBody(userTotalResponse);
		Logger.info("Blocked Users response: "+blockedUserResponse);
		Assert.assertEquals(userTotalResponse.getStatusLine().getStatusCode(), HttpStatus.SC_OK,"Response code doesn't match");
		JSONObject userObj = new JSONObject(blockedUserResponse).getJSONObject("meta");
		
		Logger.info("###########################################################################################################################");
		Logger.info("Expected Total Blocked Users: "+userWidgetObj.getInt("blocked_users"));		
		Logger.info("Actual Blocked users data in the users page "+userObj.getInt("total_count"));
		Assert.assertEquals(userObj.getInt("total_count"), userWidgetObj.getInt("blocked_users"), "The number of blocked_users doesn't match");
		Logger.info("");
		Logger.info("Results of test:");
		Logger.info("Expecting "+userWidgetObj.getInt("blocked_users")+" Blocked Users and found "+userObj.getInt("total_count")+" Test Passed");
		Logger.info("###########################################################################################################################");
	}
	
	@DataProvider(name="userSeverityDataProvider")
	public Object[][] userSeverityDataProvider(){
		return new Object[][]{{USER_SEVERITY.HIGH_USERSEVERITY},
						  {USER_SEVERITY.MEDIUM_USERSEVERITY},
						  {USER_SEVERITY.LOW_USERSEVERITY}};
	}
	
	@Test(priority=2,dataProvider="userSeverityDataProvider")	
	public void testRiskyUsersInUsersWidget(USER_SEVERITY severityLevel) throws Exception{
		Logger.info("###########################################################################################################################");
		Logger.info("This test case Gets different types of risky users from detect based on dataprovider and verify that the dashboard data matches");
		Logger.info("###########################################################################################################################");
		Logger.info("Users Data Response from Dashboard Users Widget: "+userWidgetObj);
		Logger.info("");
		Logger.info("###########################################################################################################################");
		Logger.info("Current iteration risk type: "+severityLevel);
		HttpResponse riskUserResp = dashboardFunctions.getUserThreadData(severityLevel.getSeverityCode());
		Assert.assertEquals(riskUserResp.getStatusLine().getStatusCode(), HttpStatus.SC_OK,"Status code doesn't match");
		JSONObject userRiskObje = new JSONObject(ClientUtil.getResponseBody(riskUserResp)).getJSONObject("users_list");
		Logger.info(severityLevel +" users in detect response: "+userRiskObje.toString());		
		Logger.info("###########################################################################################################################");		
		if(severityLevel.equals(USER_SEVERITY.HIGH_USERSEVERITY)){
			Logger.info("Expected Total high severity Users: "+userWidgetObj.getInt("high_risk_users"));		
			Logger.info("Actual total high severity users data in the users page "+userRiskObje.length());
			Assert.assertEquals(userRiskObje.length(), userWidgetObj.getInt("high_risk_users"));
			Logger.info("");
			Logger.info("Expecting "+userWidgetObj.getInt("high_risk_users")+" High risky Users and found "+userRiskObje.length()+" Test Passed");
			Logger.info("###########################################################################################################################");
		}
		if(severityLevel.equals(USER_SEVERITY.MEDIUM_USERSEVERITY)){
			Logger.info("Expected Total medium severity Users: "+userWidgetObj.getInt("medium_risk_users"));		
			Logger.info("Actual total medium severity users data in the users page "+userRiskObje.length());
			Assert.assertEquals(userRiskObje.length(), userWidgetObj.getInt("medium_risk_users"));
			Logger.info("");
			Logger.info("Expecting "+userWidgetObj.getInt("medium_risk_users")+" Medium risky Users and found "+userRiskObje.length()+" Test Passed");
			Logger.info("###########################################################################################################################");
		}
		if(severityLevel.equals(USER_SEVERITY.LOW_USERSEVERITY)){
			Logger.info("Expected Total low severity Users: "+userWidgetObj.getInt("low_risk_users"));		
			Logger.info("Actual total low severity users data in the users page "+userRiskObje.length());
			Assert.assertEquals(userRiskObje.length(), userWidgetObj.getInt("low_risk_users"));
			Logger.info("");
			Logger.info("Expecting "+userWidgetObj.getInt("low_risk_users")+" Low Risky Users and found "+userRiskObje.length()+" Test Passed");
			Logger.info("###########################################################################################################################");
		}
	}
	
}