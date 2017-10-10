/**
 * 
 */
package com.elastica.beatle.tests.splunkTests.regionalGWTests;

/**
 * 
 */
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import com.elastica.beatle.RestClient.ClientUtil;
import com.elastica.beatle.dci.DCIFunctions;
import com.elastica.beatle.infra.InfraActions;
import com.elastica.beatle.logger.Logger;
import com.elastica.beatle.protect.ProtectFunctions;
import com.elastica.beatle.protect.ProtectTestConstants;
import com.elastica.beatle.splunk.SplunkQueries;
import com.elastica.beatle.splunk.SplunkQueryResult;
import com.elastica.beatle.splunk.GWTests.GateletStatusConstants;
import com.elastica.beatle.tests.splunkTests.SplunkInitializer;

public class GateletPolicyUpdateSplunkTests extends SplunkInitializer {
	private ProtectFunctions protectFunctions;
	private List<String> hostList;	
	private static List<String> deletePolicyList;
	private static final String SPLUNK_QUERY_TIME_GAP = "-180m"; 
	private static final long SPLUNK_WAIT_TIME = 5400000;
	private static final long POLICY_ACTIVITY_WAIT_TIME = 30000;
	private static final long GATELET_ACTIVITY_WAIT_TIME = 30000;
	private static final long MAX_LOG_DELAY = 60000;
	
	@BeforeClass(alwaysRun=true)
	public void initTests() throws URISyntaxException, Exception {		
		protectFunctions = new ProtectFunctions();	
		hostList = SplunkUtils.getAllLiveHosts(suiteData.getEnvironmentName());
		deletePolicyList = new ArrayList<String>();
	}
	
	@DataProvider(name="policyData",parallel=true)
	public Object[][] policyDataProvider(){		
		return new Object[][]{
			{"FILESHARE_POLICY", new SoftAssert()},
			{"FILETRANSFER_POLICY", new SoftAssert()},
			{"ACCESSENFORCEMENT_POLICY", new SoftAssert()}
		};
	}
	
	/*
	 * GW Policy tests
	 */
	@Test(dataProvider="policyData", invocationCount= invocationCount)
	public void testGWPolicyCreateTests(String policyData,SoftAssert s_assert) throws Exception{
		String policyName = SplunkUtils.getPolicyName();
		HttpResponse createResponse = SplunkUtils.createPolicy(protectFunctions, policyData, restClient, policyName, requestHeaders, suiteData);		
		Assert.assertEquals(createResponse.getStatusLine().getStatusCode(), HttpStatus.SC_OK,"Response status doesn't match");
		String createPolicyResponseBody = ClientUtil.getResponseBody(createResponse);
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Logger.info("Create Policy Response: "+createPolicyResponseBody);
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		
		JSONObject policyResponseObject  = new JSONObject(createPolicyResponseBody);
		String policyCreationTime = policyResponseObject.getJSONObject("api_response").getString("created_on");
		s_assert = SplunkUtils.validateCreatePolicyResponse(policyResponseObject,s_assert);
		Logger.info("waiting for "+TimeUnit.MILLISECONDS.toMinutes(SPLUNK_WAIT_TIME)+" Minutes before checking for splunk logs");
		Thread.sleep(SPLUNK_WAIT_TIME);		
		SplunkQueryResult splunkResult = SplunkQueries.lookForGWPolicyCreateORUpdateLog(suiteData.getEnvironmentName(),policyName, SPLUNK_QUERY_TIME_GAP);
		s_assert = SplunkUtils.validateSplunkResultsWithTimeDiff(true, splunkResult.getQueryResult(), hostList, s_assert, 1,policyCreationTime,MAX_LOG_DELAY,SPLUNK_WAIT_TIME,true);
		deletePolicyList.add(policyName);
		s_assert.assertAll();							
	}
	
	@Test(dataProvider="policyData", invocationCount= invocationCount)
	public void testGWPolicyCreateDeleteTests(String policyData,SoftAssert s_assert) throws Exception{
		String policyName = SplunkUtils.getPolicyName();
		HttpResponse createResponse = SplunkUtils.createPolicy(protectFunctions, policyData, restClient, policyName, requestHeaders, suiteData);		
		Assert.assertEquals(createResponse.getStatusLine().getStatusCode(), HttpStatus.SC_OK,"Response status doesn't match");
		String createPolicyResponseBody = ClientUtil.getResponseBody(createResponse);
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Logger.info("Create Policy Response: "+createPolicyResponseBody);
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		JSONObject policyResponseObject  = new JSONObject(createPolicyResponseBody);
		String policyCreationTime = policyResponseObject.getJSONObject("api_response").getString("created_on");
		s_assert = SplunkUtils.validateCreatePolicyResponse(policyResponseObject,s_assert);		
		Logger.info("waiting for "+TimeUnit.MILLISECONDS.toSeconds(POLICY_ACTIVITY_WAIT_TIME)+" Seconds before doing next operation on policy "+policyName);
		Thread.sleep(POLICY_ACTIVITY_WAIT_TIME*4);				
		String policyDeleteResponseBody = protectFunctions.deletePolicy(restClient, policyName, requestHeaders, suiteData);
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Logger.info("Delete Policy Response: "+policyDeleteResponseBody);
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		String policyDeleteTime = DateTime.now().withZone(DateTimeZone.forID("America/Los_Angeles")).toString();
		System.out.println("policyDeleteResponseBody "+policyDeleteResponseBody);
		
		Logger.info("waiting for "+TimeUnit.MILLISECONDS.toMinutes(SPLUNK_WAIT_TIME)+" Minutes before checking for splunk logs");
		Thread.sleep(SPLUNK_WAIT_TIME);		

		SplunkQueryResult splunkResult = SplunkQueries.lookForGWPolicyCreateORUpdateLog(suiteData.getEnvironmentName(),policyName, SPLUNK_QUERY_TIME_GAP);
		s_assert = SplunkUtils.validateSplunkResultsWithTimeDiff(true, splunkResult.getQueryResult(), hostList, s_assert, 1,policyCreationTime,MAX_LOG_DELAY,SPLUNK_WAIT_TIME,true);
		SplunkQueryResult deleteSplunkResult = SplunkQueries.lookForGWPolicyDeleteLog(suiteData.getEnvironmentName(),policyName, SPLUNK_QUERY_TIME_GAP);
		s_assert = SplunkUtils.validateSplunkResultsWithTimeDiff(true, deleteSplunkResult.getQueryResult(), hostList, s_assert, 1,policyDeleteTime,MAX_LOG_DELAY,SPLUNK_WAIT_TIME,true);
		s_assert.assertAll();	
	}
	
	@Test(dataProvider="policyData", invocationCount= invocationCount)
	public void testGWPolicyActivateDeActivateTests(String policyData,SoftAssert s_assert) throws Exception{
		String policyName = SplunkUtils.getPolicyName();
		HttpResponse createResponse = SplunkUtils.createPolicy(protectFunctions, policyData, restClient, policyName, requestHeaders, suiteData);		
		Assert.assertEquals(createResponse.getStatusLine().getStatusCode(), HttpStatus.SC_OK,"Response status doesn't match");
		String createPolicyResponseBody = ClientUtil.getResponseBody(createResponse);
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Logger.info("Create Policy Response: "+createPolicyResponseBody);
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		JSONObject policyResponseObject  = new JSONObject(createPolicyResponseBody);
		s_assert = SplunkUtils.validateCreatePolicyResponse(policyResponseObject,s_assert);
		
		Logger.info("waiting for "+TimeUnit.MILLISECONDS.toSeconds(POLICY_ACTIVITY_WAIT_TIME)+" Seconds before doing next operation on policy "+policyName);
		Thread.sleep(POLICY_ACTIVITY_WAIT_TIME);		
		protectFunctions.activatePolicyByName(restClient, policyName, requestHeaders, suiteData);
	
		Logger.info("waiting for "+TimeUnit.MILLISECONDS.toSeconds(POLICY_ACTIVITY_WAIT_TIME)+" Seconds before doing next operation on policy "+policyName);		
		Thread.sleep(POLICY_ACTIVITY_WAIT_TIME);	
		protectFunctions.deActivatePolicy(restClient, policyName, requestHeaders, suiteData);
		
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Logger.info("waiting for "+TimeUnit.MILLISECONDS.toSeconds(SPLUNK_WAIT_TIME)+" Seconds before checking for splunk logs");		
		Thread.sleep(SPLUNK_WAIT_TIME);	
		Logger.info("Starting splunk validations for Disable azure sso");
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		//Need to change splunk query to validate the time too		
		SplunkQueryResult splunkResult = SplunkQueries.lookForGWPolicyCreateORUpdateLogWithoutTime(suiteData.getEnvironmentName(),policyName, SPLUNK_QUERY_TIME_GAP);
		s_assert = SplunkUtils.validateSplunkResults(splunkResult.getQueryResult(), hostList, s_assert, 3,SPLUNK_WAIT_TIME,true);
		deletePolicyList.add(policyName);
		s_assert.assertAll();
	}
	
	@Test(dataProvider="policyData", invocationCount= invocationCount)
	public void testGWPolicyCreateActivateTests(String policyData,SoftAssert s_assert) throws Exception {
		String policyName = SplunkUtils.getPolicyName();
		HttpResponse createResponse = SplunkUtils.createPolicy(protectFunctions, policyData, restClient, policyName, requestHeaders, suiteData);		
		Assert.assertEquals(createResponse.getStatusLine().getStatusCode(), HttpStatus.SC_OK,"Response status doesn't match");
		String createPolicyResponseBody = ClientUtil.getResponseBody(createResponse);
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Logger.info("Create Policy Response: "+createPolicyResponseBody);
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		JSONObject policyResponseObject  = new JSONObject(createPolicyResponseBody);
		s_assert = SplunkUtils.validateCreatePolicyResponse(policyResponseObject,s_assert);
		
		Logger.info("waiting for "+TimeUnit.MILLISECONDS.toSeconds(POLICY_ACTIVITY_WAIT_TIME)+" Seconds before doing next operation on policy "+policyName);
		Thread.sleep(POLICY_ACTIVITY_WAIT_TIME);				
		protectFunctions.activatePolicyByName(restClient, policyName, requestHeaders, suiteData);		
		
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Logger.info("waiting for "+TimeUnit.MILLISECONDS.toSeconds(SPLUNK_WAIT_TIME)+" Seconds before checking for splunk logs");		
		Thread.sleep(SPLUNK_WAIT_TIME);	
		Logger.info("Starting splunk validations for Disable azure sso");
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		//Need to change splunk query to validate the time too				
		SplunkQueryResult splunkResult = SplunkQueries.lookForGWPolicyCreateORUpdateLogWithoutTime(suiteData.getEnvironmentName(),policyName, SPLUNK_QUERY_TIME_GAP);
		s_assert = SplunkUtils.validateSplunkResults(splunkResult.getQueryResult(), hostList, s_assert, 2,SPLUNK_WAIT_TIME,true);
		deletePolicyList.add(policyName);
		s_assert.assertAll();	
	}
	
	@Test(dataProvider="policyData", invocationCount= invocationCount)
	public void testGWPolicyCreateEditTests(String policyData,SoftAssert s_assert) throws Exception {
		String policyName = SplunkUtils.getPolicyName();
		HttpResponse createResponse = SplunkUtils.createPolicy(protectFunctions, policyData, restClient, policyName, requestHeaders, suiteData);		
		Assert.assertEquals(createResponse.getStatusLine().getStatusCode(), HttpStatus.SC_OK,"Response status doesn't match");
		String createPolicyResponseBody = ClientUtil.getResponseBody(createResponse);
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Logger.info("Create Policy Response: "+createPolicyResponseBody);
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		JSONObject policyResponseObject  = new JSONObject(createPolicyResponseBody);
		s_assert = SplunkUtils.validateCreatePolicyResponse(policyResponseObject,s_assert);
		
		Logger.info("waiting for "+TimeUnit.MILLISECONDS.toSeconds(POLICY_ACTIVITY_WAIT_TIME)+" Seconds before doing next operation on policy "+policyName);
		Thread.sleep(POLICY_ACTIVITY_WAIT_TIME);	
		
		Map<String, String> policyDetails = protectFunctions.getPolicyDetailsByName(restClient, policyName, requestHeaders, suiteData);		
		JSONObject policyObject = new JSONObject(SplunkUtils.getPolicyJSON(policyData, policyName));		
		policyObject.put("policy_id", policyDetails.get(ProtectTestConstants.SUB_ID));
		policyObject.remove("policy_description");
		policyObject.put("policy_description", "Edited policy");
		policyObject.remove("applications");
		JSONArray appArray = new JSONArray();
		appArray.put("Box");
		policyObject.putOnce("applications", appArray);
		
		HttpResponse response = protectFunctions.editExistingPolicy(restClient, requestHeaders, suiteData,policyObject.toString());
		Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_OK,"Response status doesn't match");
		String editPolicyResponseBody = ClientUtil.getResponseBody(response);
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Logger.info("Edit Policy Response: "+editPolicyResponseBody);
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Logger.info("waiting for "+TimeUnit.MILLISECONDS.toSeconds(SPLUNK_WAIT_TIME)+" Seconds before checking for splunk logs");		
		Thread.sleep(SPLUNK_WAIT_TIME);	
		Logger.info("Starting splunk validations for Disable azure sso");
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		//Need to change splunk query to validate the time too
		SplunkQueryResult splunkResult = SplunkQueries.lookForGWPolicyCreateORUpdateLogWithoutTime(suiteData.getEnvironmentName(),policyName, SPLUNK_QUERY_TIME_GAP);
		s_assert = SplunkUtils.validateSplunkResults(splunkResult.getQueryResult(), hostList, s_assert, 2,SPLUNK_WAIT_TIME,true);
		deletePolicyList.add(policyName);
		s_assert.assertAll();
	}
	
	/*
	 * SSO Enabling/disabling config update test
	 */
	@Test
	public void testAzureSSOEnablingDisable() throws Exception{
		SplunkUtils splunkUtils = new SplunkUtils();
		DCIFunctions dciFunctions = new DCIFunctions();
		SoftAssert s_assert = new SoftAssert();
		List<NameValuePair> browserCookies = dciFunctions.getBrowserHeaders(suiteData);
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Logger.info("Enabling Azure AD SSO");
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		splunkUtils.ssoEnableAzureAD(restClient, suiteData, browserCookies);
		HashMap<String, String> ssoData = splunkUtils.getTenantSSOStatus(restClient, suiteData, requestHeaders);
		String id = ssoData.get("id");
		
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Logger.info("waiting for "+TimeUnit.MILLISECONDS.toSeconds(GATELET_ACTIVITY_WAIT_TIME)+" Seconds before doing next operation on gatelet "+suiteData.getSaasApp());
		Thread.sleep(GATELET_ACTIVITY_WAIT_TIME);
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Logger.info("Disabling Azure AD SSO");
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		splunkUtils.ssoDisableAzureAD(restClient, suiteData, requestHeaders, ssoData.get("id"));
		
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Logger.info("waiting for "+TimeUnit.MILLISECONDS.toMinutes(SPLUNK_WAIT_TIME)+" Minutes before checking for splunk logs");
		Thread.sleep(SPLUNK_WAIT_TIME);			
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
			
		Logger.info("Starting splunk validations for Enable azure sso");
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		//Need to change splunk query to validate the time too
		SplunkQueryResult splunkResult = SplunkQueries.lookForEnableAzureWithoutTime(suiteData.getEnvironmentName(), suiteData.getTenantName(), id, SPLUNK_QUERY_TIME_GAP);
		s_assert = SplunkUtils.validateSplunkResults(splunkResult.getQueryResult(), hostList, s_assert, 1,SPLUNK_WAIT_TIME,true);				
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		
		Logger.info("Starting splunk validations for Disable azure sso");
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		//Need to change splunk query to validate the time too
		SplunkQueryResult disablesplunkResult = SplunkQueries.lookForDisableAzureWithoutTime(suiteData.getEnvironmentName(), suiteData.getTenantName(), id, SPLUNK_QUERY_TIME_GAP);
		s_assert = SplunkUtils.validateSplunkResults(disablesplunkResult.getQueryResult(), hostList, s_assert, 1,SPLUNK_WAIT_TIME,true);				
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		s_assert.assertAll();
	}
	
	/*
	 * Response template config update tests
	 */
	@Test(dataProvider="policyData", invocationCount= invocationCount)
	public void testCreateResponseTemplate(String policyType, SoftAssert s_assert) throws Exception{
		SplunkUtils splunkUtils = new SplunkUtils();
		String templateName = SplunkUtils.getPolicyName();
		HttpResponse response = splunkUtils.createPolicyTemplate(protectFunctions, policyType, restClient, templateName, requestHeaders, suiteData);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_OK,"Status code doesn't match");
		String responeBody = ClientUtil.getResponseBody(response);
		protectFunctions.verifyResponseTemplateCreated(responeBody);	
		String creationTime = protectFunctions.getResponseTemplateParameter(restClient, requestHeaders, suiteData, templateName, "created_on");
		String templateID = protectFunctions.getResponseTemplateParameter(restClient, requestHeaders, suiteData, templateName, "id");
	
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");		
		Logger.info("waiting for "+TimeUnit.MILLISECONDS.toMinutes(SPLUNK_WAIT_TIME)+" Minutes before checking for splunk logs");
		Thread.sleep(SPLUNK_WAIT_TIME);	
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		
		Logger.info("Starting splunk validations for create response template");
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		SplunkQueryResult splunkResult = SplunkQueries.lookForCreateResponseTemplate(suiteData.getEnvironmentName(),suiteData.getTenantName(),templateID, SPLUNK_QUERY_TIME_GAP);
		s_assert = SplunkUtils.validateSplunkResultsWithTimeDiff(true, splunkResult.getQueryResult(), hostList, s_assert, 1,creationTime,MAX_LOG_DELAY,SPLUNK_WAIT_TIME,true);
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		s_assert.assertAll();
	}
	
	@Test(dataProvider="policyData", invocationCount= invocationCount)
	public void testdeleteResponseTemplate(String policyType, SoftAssert s_assert) throws Exception{
		SplunkUtils splunkUtils = new SplunkUtils();
		String templateName = SplunkUtils.getPolicyName();
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Logger.info("Creating a policy template: "+templateName);
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		HttpResponse response = splunkUtils.createPolicyTemplate(protectFunctions, policyType, restClient, templateName, requestHeaders, suiteData);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_OK,"Status code doesn't match");
		String responeBody = ClientUtil.getResponseBody(response);
		protectFunctions.verifyResponseTemplateCreated(responeBody);				
		String templateID = protectFunctions.getResponseTemplateParameter(restClient, requestHeaders, suiteData, templateName, "id");
		
		Logger.info("waiting for "+TimeUnit.MILLISECONDS.toSeconds(POLICY_ACTIVITY_WAIT_TIME)+" Seconds before doing next operation on response template "+templateName);
		Thread.sleep(POLICY_ACTIVITY_WAIT_TIME);
		
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Logger.info("Deleting a policy template name: "+templateName+" and template id: "+templateID);
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		HttpResponse deleteResponse = splunkUtils.deletePolicyTemplate(restClient, requestHeaders, suiteData, protectFunctions, templateName);
		Assert.assertEquals(deleteResponse.getStatusLine().getStatusCode(), HttpStatus.SC_OK,"Status code doesn't match");
		String modifiedTime = protectFunctions.getResponseTemplateParameter(restClient, requestHeaders, suiteData, templateName, "modified_on");
			
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");		
		Logger.info("waiting for "+TimeUnit.MILLISECONDS.toMinutes(SPLUNK_WAIT_TIME)+" Minutes before checking for splunk logs");
		Thread.sleep(SPLUNK_WAIT_TIME);	
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		
		Logger.info("Starting splunk validations for delete response template");
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		SplunkQueryResult splunkResult = SplunkQueries.lookForDeleteResponseTemplate(suiteData.getEnvironmentName(), suiteData.getTenantName(), templateID, SPLUNK_QUERY_TIME_GAP);
		s_assert = SplunkUtils.validateSplunkResultsWithTimeDiff(true, splunkResult.getQueryResult(), hostList, s_assert, 1,modifiedTime,MAX_LOG_DELAY,SPLUNK_WAIT_TIME,true);
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		s_assert.assertAll();
	}
	
	/*
	 * Not Verifying the time diff between operation because of splunk log constraint between create and edit operations. 
	 * So we need some data from dev
	 * We can monitor the number of events now but can't calculate the time diff for two operations.
	 */
	@Test(dataProvider="policyData", invocationCount= invocationCount)
	public void testEditResponseTemplate(String policyType, SoftAssert s_assert) throws Exception{
		SplunkUtils splunkUtils = new SplunkUtils();
		String templateName = SplunkUtils.getPolicyName();
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Logger.info("Creating a policy template: "+templateName);
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		HttpResponse response = splunkUtils.createPolicyTemplate(protectFunctions, policyType, restClient, templateName, requestHeaders, suiteData);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_OK,"Status code doesn't match");
		String responeBody = ClientUtil.getResponseBody(response);
		protectFunctions.verifyResponseTemplateCreated(responeBody);	
		String templateID = protectFunctions.getResponseTemplateParameter(restClient, requestHeaders, suiteData, templateName, "id");
					
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Logger.info("Editing a policy template name: "+templateName+" and template id: "+templateID);
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		HttpResponse editresponseBody = splunkUtils.editPolicyTemplate(restClient, requestHeaders, suiteData, protectFunctions, templateName, policyType);
		Assert.assertEquals(editresponseBody.getStatusLine().getStatusCode(), HttpStatus.SC_OK,"Status code doesn't match");		
		String editResponse = ClientUtil.getResponseBody(editresponseBody);
		protectFunctions.verifyResponseTemplateCreated(editResponse);					
			
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");		
		Logger.info("waiting for "+TimeUnit.MILLISECONDS.toMinutes(SPLUNK_WAIT_TIME)+" Minutes before checking for splunk logs");
		Thread.sleep(SPLUNK_WAIT_TIME);	
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		
		Logger.info("Starting splunk validations for Edit response template");
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		SplunkQueryResult splunkResult = SplunkQueries.lookForCreateResponseTemplateWithoutTime(suiteData.getEnvironmentName(), suiteData.getTenantName(), templateID, SPLUNK_QUERY_TIME_GAP);
		s_assert = SplunkUtils.validateSplunkResults(splunkResult.getQueryResult(), hostList, s_assert, 2,SPLUNK_WAIT_TIME,true);
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		s_assert.assertAll();
	}
	
	/*
	 * Groups config update tests
	 */
	@Test(invocationCount= invocationCount)
	public void testCreateAndDeleteGroup() throws Exception{
		SplunkUtils splunkUtils = new SplunkUtils();
		InfraActions infraActions = new InfraActions();
		SoftAssert s_assert = new SoftAssert();
		Map<String, String> groupData = new HashMap<String, String>();
		String groupName = SplunkUtils.getPolicyName();
		Logger.info("Creating a group: "+groupName);
		groupData.put("name", groupName);
		groupData.put("description", "");
		groupData.put("notes", "Notes");
		groupData.put("is_active", "true");
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		splunkUtils.createGroup(suiteData, requestHeaders, groupData);

		Logger.info("waiting for "+TimeUnit.MILLISECONDS.toSeconds(GATELET_ACTIVITY_WAIT_TIME)+" Seconds before doing next operation on gatelet "+suiteData.getSaasApp());
		Thread.sleep(GATELET_ACTIVITY_WAIT_TIME);
		
		HttpResponse searchresponse = infraActions.SearchGroup(suiteData, requestHeaders, groupName);
		Assert.assertEquals(searchresponse.getStatusLine().getStatusCode(), HttpStatus.SC_OK,"Response status doesn't match");
		String responseBody = ClientUtil.getResponseBody(searchresponse);
		HashMap<String, String> groupNameData = infraActions.getGroupData(responseBody, groupName);
		String createdTime = groupNameData.get("created_on");
		String groupID = infraActions.getGroupid(responseBody,groupName);
		
		Logger.info("waiting for "+TimeUnit.MILLISECONDS.toSeconds(GATELET_ACTIVITY_WAIT_TIME)+" Seconds before doing next operation on gatelet "+suiteData.getSaasApp());		
		Thread.sleep(GATELET_ACTIVITY_WAIT_TIME);
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Logger.info("Deleting a group: "+groupName);
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");		
		splunkUtils.deleteGroup(suiteData, requestHeaders, groupID);
		String deleteTime = new DCIFunctions().getCurrentTime();
		
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");		
		Logger.info("waiting for "+TimeUnit.MILLISECONDS.toMinutes(SPLUNK_WAIT_TIME)+" Minutes before checking for splunk logs");
		Thread.sleep(SPLUNK_WAIT_TIME);	
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		
		Logger.info("Starting splunk validations for create group");
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		SplunkQueryResult splunkResult = SplunkQueries.lookForGroupCreatingLogs(suiteData.getEnvironmentName(), suiteData.getTenantName(), groupID, SPLUNK_QUERY_TIME_GAP);
		s_assert = SplunkUtils.validateSplunkResultsWithTimeDiff(true, splunkResult.getQueryResult(), hostList, s_assert, 1,createdTime,MAX_LOG_DELAY,SPLUNK_WAIT_TIME,true);			
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		
		Logger.info("Starting splunk validations for delete group");
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		SplunkQueryResult splunkResult1 = SplunkQueries.lookForGroupDeletionLogs(suiteData.getEnvironmentName(), suiteData.getTenantName(), groupName, SPLUNK_QUERY_TIME_GAP);
		s_assert = SplunkUtils.validateSplunkResultsWithTimeDiff(true, splunkResult1.getQueryResult(), hostList, s_assert, 1,deleteTime,MAX_LOG_DELAY,SPLUNK_WAIT_TIME,true);	
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		s_assert.assertAll();
	}
	
	@Test(invocationCount= invocationCount)
	public void testDeactivatingGroup() throws Exception{
		SplunkUtils splunkUtils = new SplunkUtils();
		InfraActions infraActions = new InfraActions();
		SoftAssert s_assert = new SoftAssert();
		Map<String, String> groupData = new HashMap<String, String>();
		String groupName = SplunkUtils.getPolicyName();
		Logger.info("Creating a group: "+groupName);
		groupData.put("name", groupName);
		groupData.put("description", "");
		groupData.put("notes", "Notes");
		groupData.put("is_active", "true");
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		splunkUtils.createGroup(suiteData, requestHeaders, groupData);

		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Logger.info("waiting for "+TimeUnit.MILLISECONDS.toSeconds(GATELET_ACTIVITY_WAIT_TIME)+" Seconds before doing next operation on gatelet "+suiteData.getSaasApp());
		Thread.sleep(GATELET_ACTIVITY_WAIT_TIME);
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		
		HttpResponse searchresponse = infraActions.SearchGroup(suiteData, requestHeaders, groupName);
		Assert.assertEquals(searchresponse.getStatusLine().getStatusCode(), HttpStatus.SC_OK,"Response status doesn't match");
		String searchResponseBody = ClientUtil.getResponseBody(searchresponse);
		String groupID = infraActions.getGroupid(searchResponseBody,groupName);
		groupData.put("id", groupID);
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Logger.info("Deactivating a group: "+groupName);
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		groupData.put("is_active", "false");
		splunkUtils.editGroup(restClient, suiteData, requestHeaders, groupData);
		
		searchresponse = infraActions.SearchGroup(suiteData, requestHeaders, groupName);
		searchResponseBody = ClientUtil.getResponseBody(searchresponse);
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Logger.info("waiting for "+TimeUnit.MILLISECONDS.toSeconds(GATELET_ACTIVITY_WAIT_TIME)+" Seconds before doing next operation on gatelet "+suiteData.getSaasApp());
		Thread.sleep(GATELET_ACTIVITY_WAIT_TIME);
		
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");		
		Logger.info("waiting for "+TimeUnit.MILLISECONDS.toMinutes(SPLUNK_WAIT_TIME)+" Minutes before checking for splunk logs");
		Thread.sleep(SPLUNK_WAIT_TIME);	
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		
		Logger.info("Starting splunk validations for create group");
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		//Need to change splunk query
		SplunkQueryResult splunkResult = SplunkQueries.lookForGroupCreatingLogsWithoutTime(suiteData.getEnvironmentName(), suiteData.getTenantName(), groupID, SPLUNK_QUERY_TIME_GAP);
		s_assert = SplunkUtils.validateSplunkResults(splunkResult.getQueryResult(), hostList, s_assert, 2,SPLUNK_WAIT_TIME,true);	
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		splunkUtils.deleteGroup(suiteData, requestHeaders, groupID);
		s_assert.assertAll();		
	}
	
	@Test(invocationCount= invocationCount)
	public void testActivateGroup() throws Exception{
		SplunkUtils splunkUtils = new SplunkUtils();
		InfraActions infraActions = new InfraActions();
		SoftAssert s_assert = new SoftAssert();
		Map<String, String> groupData = new HashMap<String, String>();
		String groupName = SplunkUtils.getPolicyName();
		Logger.info("Creating a group: "+groupName);
		groupData.put("name", groupName);
		groupData.put("description", "");
		groupData.put("notes", "Notes");
		groupData.put("is_active", "false");
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		splunkUtils.createGroup(suiteData, requestHeaders, groupData);
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Logger.info("waiting for "+TimeUnit.MILLISECONDS.toSeconds(GATELET_ACTIVITY_WAIT_TIME)+" Seconds before doing next operation on gatelet "+suiteData.getSaasApp());
		Thread.sleep(GATELET_ACTIVITY_WAIT_TIME);
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		
		HttpResponse searchresponse = infraActions.SearchGroup(suiteData, requestHeaders, groupName);
		Assert.assertEquals(searchresponse.getStatusLine().getStatusCode(), HttpStatus.SC_OK,"Response status doesn't match");
		String searchResponseBody = ClientUtil.getResponseBody(searchresponse);
		String groupID = infraActions.getGroupid(searchResponseBody,groupName);
		groupData.put("id", groupID);
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Logger.info("Activating a group: "+groupName);
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		groupData.put("is_active", "true");
		splunkUtils.editGroup(restClient, suiteData, requestHeaders, groupData);
		
		searchresponse = infraActions.SearchGroup(suiteData, requestHeaders, groupName);
		searchResponseBody = ClientUtil.getResponseBody(searchresponse);
		Logger.info("waiting for "+TimeUnit.MILLISECONDS.toSeconds(GATELET_ACTIVITY_WAIT_TIME)+" Seconds before doing next operation on gatelet "+suiteData.getSaasApp());
		Thread.sleep(GATELET_ACTIVITY_WAIT_TIME);
		
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");		
		Logger.info("waiting for "+TimeUnit.MILLISECONDS.toMinutes(SPLUNK_WAIT_TIME)+" Minutes before checking for splunk logs");
		Thread.sleep(SPLUNK_WAIT_TIME);	
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		
		Logger.info("Starting splunk validations for create group");
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		//Need to change splunk query
		SplunkQueryResult splunkResult = SplunkQueries.lookForGroupCreatingLogsWithoutTime(suiteData.getEnvironmentName(), suiteData.getTenantName(), groupID, SPLUNK_QUERY_TIME_GAP);
		s_assert = SplunkUtils.validateSplunkResults(splunkResult.getQueryResult(), hostList, s_assert, 2,SPLUNK_WAIT_TIME,true);			
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		splunkUtils.deleteGroup(suiteData, requestHeaders, groupID);
		s_assert.assertAll();		
	}
	
	@Test(invocationCount= invocationCount)
	public void testEditGroup() throws Exception{
		SplunkUtils splunkUtils = new SplunkUtils();
		InfraActions infraActions = new InfraActions();
		SoftAssert s_assert = new SoftAssert();
		Map<String, String> groupData = new HashMap<String, String>();
		String groupName = SplunkUtils.getPolicyName();
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Logger.info("Creating a group: "+groupName);
		groupData.put("name", groupName);
		groupData.put("description", "");
		groupData.put("notes", "Notes");
		groupData.put("is_active", "true");
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		splunkUtils.createGroup(suiteData, requestHeaders, groupData);

		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Logger.info("waiting for "+TimeUnit.MILLISECONDS.toSeconds(GATELET_ACTIVITY_WAIT_TIME)+" Seconds before doing next operation on gatelet "+suiteData.getSaasApp());
		Thread.sleep(GATELET_ACTIVITY_WAIT_TIME);
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		
		HttpResponse searchresponse = infraActions.SearchGroup(suiteData, requestHeaders, groupName);
		Assert.assertEquals(searchresponse.getStatusLine().getStatusCode(), HttpStatus.SC_OK,"Response status doesn't match");
		String searchResponseBody = ClientUtil.getResponseBody(searchresponse);
		String groupID = infraActions.getGroupid(searchResponseBody,groupName);
		groupData.put("id", groupID);
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Logger.info("Editing a group: "+groupName);
		groupData.put("description", "Description");
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		splunkUtils.editGroup(restClient, suiteData, requestHeaders, groupData);
		
		searchresponse = infraActions.SearchGroup(suiteData, requestHeaders, groupName);
		searchResponseBody = ClientUtil.getResponseBody(searchresponse);
		HashMap<String, String> groupNameData = infraActions.getGroupData(searchResponseBody, groupName);

		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");		
		Logger.info("waiting for "+TimeUnit.MILLISECONDS.toMinutes(SPLUNK_WAIT_TIME)+" Minutes before checking for splunk logs");
		Thread.sleep(SPLUNK_WAIT_TIME);	
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Logger.info("Starting splunk validations for create group");
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		//Need to change splunk query
		SplunkQueryResult splunkResult = SplunkQueries.lookForGroupCreatingLogsWithoutTime(suiteData.getEnvironmentName(), suiteData.getTenantName(), groupID, SPLUNK_QUERY_TIME_GAP);
		s_assert = SplunkUtils.validateSplunkResults(splunkResult.getQueryResult(), hostList, s_assert, 2,SPLUNK_WAIT_TIME,true);		
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		splunkUtils.deleteGroup(suiteData, requestHeaders, groupID);
		s_assert.assertAll();		
	}
	
	/*
	 * Key secure config update tests
	 */
	@Test
	public void testCreateKeySecure() throws Exception{
		SplunkUtils splunkUtils = new SplunkUtils();
		DCIFunctions dciFunctions = new DCIFunctions();
		SoftAssert s_assert = new SoftAssert();
		String keySecureName = SplunkUtils.getPolicyName();
		List<NameValuePair> browserCookies = dciFunctions.getBrowserHeaders(suiteData);
		Logger.info("Adding Key Secure: "+keySecureName);
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Map<String, String> keySecureData = splunkUtils.addKeySecure(restClient, suiteData, browserCookies, keySecureName);
		String keyID = keySecureData.get("id");
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Logger.info("waiting for "+TimeUnit.MILLISECONDS.toMinutes(SPLUNK_WAIT_TIME)+" Minutes before checking for splunk logs");
		Thread.sleep(SPLUNK_WAIT_TIME);			
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
			
		Logger.info("Starting splunk validations for Key Secure");
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		//Need to change splunk query to validate the time too
		SplunkQueryResult splunkResult = SplunkQueries.lookForCreateKeySecureWithoutTime(suiteData.getEnvironmentName(), suiteData.getTenantName(), keyID, SPLUNK_QUERY_TIME_GAP);
		s_assert = SplunkUtils.validateSplunkResults(splunkResult.getQueryResult(), hostList, s_assert, 1,SPLUNK_WAIT_TIME,true);			
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		s_assert.assertAll();
	}

	@Test
	public void testEditKeySecure() throws Exception{
		SplunkUtils splunkUtils = new SplunkUtils();
		DCIFunctions dciFunctions = new DCIFunctions();
		SoftAssert s_assert = new SoftAssert();
		String keySecureName = SplunkUtils.getPolicyName();
		List<NameValuePair> browserCookies = dciFunctions.getBrowserHeaders(suiteData);
		Logger.info("Adding Key Secure: "+keySecureName);
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Map<String, String> keySecureData = splunkUtils.addKeySecure(restClient, suiteData, browserCookies, keySecureName);
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Logger.info("waiting for "+TimeUnit.MILLISECONDS.toSeconds(GATELET_ACTIVITY_WAIT_TIME)+" Seconds before doing next operation on gatelet "+suiteData.getSaasApp());
		Thread.sleep(GATELET_ACTIVITY_WAIT_TIME);
		
		Logger.info("Editing Key Secure: "+keySecureName);
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		keySecureData = splunkUtils.editKeySecure(restClient, suiteData, browserCookies, keySecureData, "KeySecureDetails");
		String keyID = keySecureData.get("id");
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Logger.info("waiting for "+TimeUnit.MILLISECONDS.toMinutes(SPLUNK_WAIT_TIME)+" Minutes before checking for splunk logs");
		Thread.sleep(SPLUNK_WAIT_TIME);			
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
			
		Logger.info("Starting splunk validations for Key Secure");
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		//Need to change splunk query to validate the time too
		SplunkQueryResult splunkResult = SplunkQueries.lookForCreateKeySecureWithoutTime(suiteData.getEnvironmentName(), suiteData.getTenantName(), keyID, SPLUNK_QUERY_TIME_GAP);
		s_assert = SplunkUtils.validateSplunkResults(splunkResult.getQueryResult(), hostList, s_assert, 2,SPLUNK_WAIT_TIME,true);				
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		s_assert.assertAll();
	}
	
	@Test
	public void testActivateDeactivateKeySecure() throws Exception{
		SplunkUtils splunkUtils = new SplunkUtils();
		DCIFunctions dciFunctions = new DCIFunctions();
		SoftAssert s_assert = new SoftAssert();
		String keySecureName = SplunkUtils.getPolicyName();
		List<NameValuePair> browserCookies = dciFunctions.getBrowserHeaders(suiteData);
		Logger.info("Adding Key Secure: "+keySecureName);
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Map<String, String> keySecureData = splunkUtils.addKeySecure(restClient, suiteData, browserCookies, keySecureName);
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		
		Logger.info("waiting for "+TimeUnit.MILLISECONDS.toSeconds(GATELET_ACTIVITY_WAIT_TIME)+" Seconds before doing next operation on gatelet "+suiteData.getSaasApp());
		Thread.sleep(GATELET_ACTIVITY_WAIT_TIME);
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		
		Logger.info("Activating Key Secure: "+keySecureName);
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		splunkUtils.activateKeySecure(restClient, suiteData, requestHeaders, keySecureData.get("id"));
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		
		Logger.info("waiting for "+TimeUnit.MILLISECONDS.toSeconds(GATELET_ACTIVITY_WAIT_TIME)+" Seconds before doing next operation on gatelet "+suiteData.getSaasApp());
		Thread.sleep(GATELET_ACTIVITY_WAIT_TIME);
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		
		Logger.info("Deactivating Key Secure: "+keySecureName);
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		keySecureData = splunkUtils.deactivateKeySecure(restClient, suiteData, requestHeaders, keySecureData.get("id"));
		String keyID = keySecureData.get("id");
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Logger.info("waiting for "+TimeUnit.MILLISECONDS.toMinutes(SPLUNK_WAIT_TIME)+" Minutes before checking for splunk logs");
		Thread.sleep(SPLUNK_WAIT_TIME);			
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
			
		Logger.info("Starting splunk validations for Key Secure");
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		//Need to change splunk query to validate the time too
		SplunkQueryResult splunkResult = SplunkQueries.lookForCreateKeySecureWithoutTime(suiteData.getEnvironmentName(), suiteData.getTenantName(), keyID, SPLUNK_QUERY_TIME_GAP);
		s_assert = SplunkUtils.validateSplunkResults(splunkResult.getQueryResult(), hostList, s_assert, 3,SPLUNK_WAIT_TIME,true);				
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		s_assert.assertAll();
	}
	
	@Test
	public void testdeleteKeySecure() throws Exception{
		SplunkUtils splunkUtils = new SplunkUtils();
		DCIFunctions dciFunctions = new DCIFunctions();
		SoftAssert s_assert = new SoftAssert();
		String keySecureName = SplunkUtils.getPolicyName();
		List<NameValuePair> browserCookies = dciFunctions.getBrowserHeaders(suiteData);
		Logger.info("Adding Key Secure: "+keySecureName);
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Map<String, String> keySecureData = splunkUtils.addKeySecure(restClient, suiteData, browserCookies, keySecureName);
		String keyID = keySecureData.get("id");
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		
		Logger.info("waiting for "+TimeUnit.MILLISECONDS.toSeconds(GATELET_ACTIVITY_WAIT_TIME)+" Seconds before doing next operation on gatelet "+suiteData.getSaasApp());
		Thread.sleep(GATELET_ACTIVITY_WAIT_TIME);
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		
		Logger.info("Deleting Key Secure: "+keySecureName);
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		splunkUtils.deleteKeySecure(restClient, suiteData, requestHeaders, keySecureData.get("id"));
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Logger.info("waiting for "+TimeUnit.MILLISECONDS.toMinutes(SPLUNK_WAIT_TIME)+" Minutes before checking for splunk logs");
		Thread.sleep(SPLUNK_WAIT_TIME);			
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
			
		Logger.info("Starting splunk validations for Key Secure");
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		//Need to change splunk query to validate the time too
		SplunkQueryResult splunkResult = SplunkQueries.lookForDeleteKeySecureWithoutTime(suiteData.getEnvironmentName(), suiteData.getTenantName(), keyID, SPLUNK_QUERY_TIME_GAP);
		s_assert = SplunkUtils.validateSplunkResults(splunkResult.getQueryResult(), hostList, s_assert, 1,SPLUNK_WAIT_TIME,true);				
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		s_assert.assertAll();
	}
	
	/*
	 * Gatelet activation/deactivation tests
	 */
	@Test
	public void testActivateDeactivationGatelet() throws Exception{
		SplunkUtils splunkUtils = new SplunkUtils();
		SoftAssert s_assert = new SoftAssert();
		String saasApp = suiteData.getSaasApp();
		Logger.info("Activating "+saasApp+" gatelet");
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		String gateletApplications = splunkUtils.getAllGatelets(restClient, requestHeaders, suiteData);
		Map<String, String> galeletDetails = splunkUtils.setAllGateletParameters(gateletApplications, saasApp);
		HttpResponse gateletActivateResponse = splunkUtils.generatePayloadForGaleletActivationDeactivation(restClient, requestHeaders, suiteData, saasApp, "activate", galeletDetails);
		splunkUtils.verifyGateletStatusResponse(gateletActivateResponse, "activate");
		Logger.info("waiting for "+TimeUnit.MILLISECONDS.toSeconds(GATELET_ACTIVITY_WAIT_TIME)+" Seconds before doing next operation on gatelet "+suiteData.getSaasApp());
		Thread.sleep(GATELET_ACTIVITY_WAIT_TIME);
		gateletApplications = splunkUtils.getAllGatelets(restClient, requestHeaders, suiteData);
		String gateletActivationTime = splunkUtils.setAllGateletParameters(gateletApplications, saasApp).get(GateletStatusConstants.MODIFIED_ON);
		
		Logger.info("Deactivating the gatelets");
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		galeletDetails = splunkUtils.setAllGateletParameters(gateletApplications, saasApp);
		HttpResponse gateletDeactivateResponse = splunkUtils.generatePayloadForGaleletActivationDeactivation(restClient, requestHeaders, suiteData, saasApp, "deactivate", galeletDetails);
		splunkUtils.verifyGateletStatusResponse(gateletDeactivateResponse, "deactivate");
		
		Logger.info("Finalizing the gatelet deactivation");
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		galeletDetails = splunkUtils.setAllGateletParameters(gateletApplications, saasApp);
		HttpResponse gateletDeactivateFinalizeResponse = splunkUtils.generatePayloadForGaleletActivationDeactivation(restClient, requestHeaders, suiteData, saasApp, "deactivatefinalize", galeletDetails);
		splunkUtils.verifyGateletStatusResponse(gateletDeactivateFinalizeResponse, "deactivatefinalize");
		Logger.info("waiting for "+TimeUnit.MILLISECONDS.toSeconds(GATELET_ACTIVITY_WAIT_TIME)+" Seconds before doing next operation on gatelet "+suiteData.getSaasApp());
		Thread.sleep(GATELET_ACTIVITY_WAIT_TIME);
		gateletApplications = splunkUtils.getAllGatelets(restClient, requestHeaders, suiteData);
		String gateletDeactivationTime = splunkUtils.setAllGateletParameters(gateletApplications, saasApp).get(GateletStatusConstants.MODIFIED_ON);
		
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Logger.info("waiting for "+TimeUnit.MILLISECONDS.toMinutes(SPLUNK_WAIT_TIME)+" Minutes before checking for splunk logs");
		Thread.sleep(SPLUNK_WAIT_TIME);			
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Logger.info("Starting splunnk validations");
		
		SplunkQueryResult splunkResult = SplunkQueries.lookForGateletActivationLogs(suiteData.getEnvironmentName(), suiteData.getTenantName(), saasApp, SPLUNK_QUERY_TIME_GAP);
		s_assert = SplunkUtils.validateSplunkResultsWithTimeDiff(false, splunkResult.getQueryResult(), hostList, s_assert, 1,gateletActivationTime,MAX_LOG_DELAY,SPLUNK_WAIT_TIME,false);				
		
		SplunkQueryResult splunkResult1 = SplunkQueries.lookForGateletDeActivationLogs(suiteData.getEnvironmentName(), suiteData.getTenantName(), saasApp, SPLUNK_QUERY_TIME_GAP);
		s_assert = SplunkUtils.validateSplunkResultsWithTimeDiff(false, splunkResult1.getQueryResult(), hostList, s_assert, 1,gateletDeactivationTime,MAX_LOG_DELAY,SPLUNK_WAIT_TIME,false);
		s_assert.assertAll();
	}
	
	@Test(invocationCount= invocationCount)
	public void testCreateActivateDeactivateDeleteUser() throws Exception{
		SplunkUtils splunkUtils = new SplunkUtils();
		InfraActions infraActions = new InfraActions();
		SoftAssert s_assert = new SoftAssert();
		Map<String, String> userData = new HashMap<String, String>();
		String email = suiteData.getSaasAppUsername();
		
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Logger.info("Create a new user: "+email);
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		userData.put("first_name", "Infra");
		userData.put("last_name", "Beatle");
		userData.put("email", email);
		userData.put("title", "");
		userData.put("is_active", "true");
		infraActions.deleteIfUserExist(suiteData, requestHeaders, email);
		splunkUtils.createUser(suiteData, requestHeaders, userData);
		
		HttpResponse searchresponse = infraActions.SearchUser(suiteData, requestHeaders, email);
		String responseBody = ClientUtil.getResponseBody(searchresponse);
		String userId = infraActions.getUserid(responseBody, email);
		HashMap<String, String> userNameData = infraActions.getUserData(responseBody, email);
		
		Logger.info("waiting for "+TimeUnit.MILLISECONDS.toSeconds(GATELET_ACTIVITY_WAIT_TIME)+" Seconds before doing next operation");
		Thread.sleep(GATELET_ACTIVITY_WAIT_TIME);
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Logger.info("Editing a user: "+email);
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		userData.put("title", "Title");
		userData.put("id", userId);
		infraActions.editUser(restClient, suiteData, requestHeaders, userData);
		searchresponse = infraActions.SearchUser(suiteData, requestHeaders, email);
		responseBody = ClientUtil.getResponseBody(searchresponse);
		userNameData = infraActions.getUserData(responseBody, email);

		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Logger.info("waiting for "+TimeUnit.MILLISECONDS.toSeconds(GATELET_ACTIVITY_WAIT_TIME)+" Seconds before doing next operation");
		Thread.sleep(GATELET_ACTIVITY_WAIT_TIME);
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Logger.info("Activating a user: "+email);
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		infraActions.activateUser(restClient, suiteData, requestHeaders, userId);
		
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Logger.info("waiting for "+TimeUnit.MILLISECONDS.toSeconds(GATELET_ACTIVITY_WAIT_TIME)+" Seconds before Performing SaaS App actions");
		Thread.sleep(GATELET_ACTIVITY_WAIT_TIME);
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Logger.info("Starting SaaS App actions");
		WebDriver driver = splunkUtils.setWebDriverGatewayProxy(restClient, suiteData, requestHeaders);
		splunkUtils.loginCloudSOC(driver, suiteData);
		splunkUtils.loginDropbox(driver, suiteData);
		//splunkUtils.logoutDropbox(driver);
		driver.close();
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Logger.info("Completed SaaS app actions");
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Logger.info("waiting for "+TimeUnit.MILLISECONDS.toSeconds(GATELET_ACTIVITY_WAIT_TIME)+" Seconds before doing next operation");
		Thread.sleep(GATELET_ACTIVITY_WAIT_TIME);
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Logger.info("Deactivating a user: "+email);
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		infraActions.deactivateUser(restClient, suiteData, requestHeaders, userId);
		searchresponse = infraActions.SearchUser(suiteData, requestHeaders, email);
		userNameData = infraActions.getUserData(ClientUtil.getResponseBody(searchresponse), email);
		String deactivateTime = userNameData.get("modified_on");
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Logger.info("waiting for "+TimeUnit.MILLISECONDS.toSeconds(GATELET_ACTIVITY_WAIT_TIME)+" Seconds before doing next operation");
		Thread.sleep(GATELET_ACTIVITY_WAIT_TIME);
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Logger.info("Deleting a user: "+email);
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		infraActions.deleteUser(suiteData, requestHeaders, userId);
		String deleteTime = new DCIFunctions().getCurrentTime();
		
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Logger.info("waiting for "+TimeUnit.MILLISECONDS.toMinutes(SPLUNK_WAIT_TIME)+" Minutes before checking for splunk logs");		
		Thread.sleep(SPLUNK_WAIT_TIME);	
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		
		SplunkQueryResult splunkCreateResult = SplunkQueries.lookForUserCreateEditLogs(suiteData.getEnvironmentName(), suiteData.getTenantName(), userId, SPLUNK_QUERY_TIME_GAP);
		List<String> sjcHosts = splunkUtils.getSJCHosts(suiteData.getEnvironmentName()); 		
		s_assert = SplunkUtils.validateSplunkResultsWithTimeDiff(true, splunkCreateResult.getQueryResult(), sjcHosts, s_assert, 1,deactivateTime,MAX_LOG_DELAY,SPLUNK_WAIT_TIME,true);
		
		SplunkQueryResult splunkDeleteResult = SplunkQueries.lookForUserDeletionLogs(suiteData.getEnvironmentName(), suiteData.getTenantName(), email, SPLUNK_QUERY_TIME_GAP);
		s_assert = SplunkUtils.validateSplunkResultsWithTimeDiff(true, splunkDeleteResult.getQueryResult(), sjcHosts, s_assert, 1,deleteTime,MAX_LOG_DELAY,SPLUNK_WAIT_TIME,true);
		s_assert.assertAll();
	}
	
	@Test(invocationCount= invocationCount)
	public void testAddRemoveUserFromGroup()throws Exception{
		SplunkUtils splunkUtils = new SplunkUtils();
		InfraActions infraActions = new InfraActions();
		Map<String, String> userData = new HashMap<String, String>();
		Map<String, String> groupData = new HashMap<String, String>();

		//Create Group
		String groupName = "Group1";
		groupData.put("name", groupName);
		groupData.put("description", "");
		groupData.put("notes", "Notes");
		groupData.put("is_active", "true");
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Logger.info("Create a new group: "+groupName);
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		infraActions.deleteIfGroupExists(suiteData, requestHeaders, groupName);
		splunkUtils.createGroup(suiteData, requestHeaders, groupData);
		
		//Create User
		String email = "testuser2@infrabeatle.com";
		Logger.info("Creating a user: "+email);
		userData.put("first_name", "Infra");
		userData.put("last_name", "Beatle");
		userData.put("email", email);
		userData.put("title", "");
		userData.put("is_active", "true");
		userData.put("is_admin","true");
		infraActions.deleteIfUserExist(suiteData, requestHeaders, email);
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Logger.info("Create a new user: "+email);
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		splunkUtils.createUser(suiteData, requestHeaders, userData);
		
		//Get User id
		HttpResponse searchUserResponse = infraActions.SearchUser(suiteData, requestHeaders, email);
		String searchUserResponseBody = ClientUtil.getResponseBody(searchUserResponse);
		String userId = infraActions.getUserid(searchUserResponseBody, email);
		
		//Add user to a group
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Logger.info("Add user \""+email+"\" to a group \""+groupName+"\"");
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		infraActions.addUserToGroup(restClient, suiteData, requestHeaders, groupName, email);
		HashMap<String, String> groupUserDetails = infraActions.getGroupUserDetails(restClient, suiteData, requestHeaders, groupName, email);
		String addUserTime = groupUserDetails.get("created_on");
		
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Logger.info("waiting for "+TimeUnit.MILLISECONDS.toSeconds(GATELET_ACTIVITY_WAIT_TIME)+" Seconds before doing next operation on gatelet "+suiteData.getSaasApp());
		Thread.sleep(GATELET_ACTIVITY_WAIT_TIME);
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
				
		WebDriver driver = splunkUtils.setWebDriverGatewayProxy(restClient, suiteData, requestHeaders);
		splunkUtils.loginCloudSOC(driver, suiteData);
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Logger.info("Dropbox Login");
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		driver.get("https://www.dropbox.com/login");
		Thread.sleep(10000);
		Logger.info("Login to dropbox in progress");
		Logger.info(driver.getCurrentUrl());
		Logger.info("Enter Username: "+email);
		driver.findElement(By.xpath(".//input[@class='text-input-input autofocus']")).sendKeys(email);
		Logger.info("Enter Password: "+suiteData.getSaasAppPassword());
		driver.findElement(By.xpath(".//input[@class='password-input text-input-input']")).sendKeys(suiteData.getSaasAppPassword());
		Logger.info("Click login Button");
		driver.findElement(By.xpath(".//button[@class='login-button button-primary']")).click();
		Thread.sleep(5000);
		Logger.info("Login to dropbox successful");
		driver.close();
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Logger.info("waiting for "+TimeUnit.MILLISECONDS.toSeconds(GATELET_ACTIVITY_WAIT_TIME)+" Seconds before doing next operation on gatelet "+suiteData.getSaasApp());
		Thread.sleep(GATELET_ACTIVITY_WAIT_TIME);
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		
		//delete user from a group
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Logger.info("Remove user \""+email+"\" from a group \""+groupName+"\"");
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		infraActions.deleteUserFromGroup(restClient, suiteData, requestHeaders, groupUserDetails);
		String removeUserTime = new DCIFunctions().getCurrentTime();
		
		//Add user to a group
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Logger.info("Add user \""+email+"\" to a group \""+groupName+"\"");
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		infraActions.addUserToGroup(restClient, suiteData, requestHeaders, groupName, email);
		groupUserDetails = infraActions.getGroupUserDetails(restClient, suiteData, requestHeaders, groupName, email);
		addUserTime = groupUserDetails.get("created_on");
		String groupID = groupUserDetails.get("id");
		Logger.info("GroupUser id: "+groupID);
				
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Logger.info("waiting for "+TimeUnit.MILLISECONDS.toMinutes(SPLUNK_WAIT_TIME)+" Minutes before checking for splunk logs");		
		Thread.sleep(SPLUNK_WAIT_TIME);	
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		
		//Splunk validation
		SoftAssert s_assert = new SoftAssert();
		SplunkQueryResult splunkCreateResult = SplunkQueries.lookForUserGroupsCreateEditLogs(suiteData.getEnvironmentName(), suiteData.getTenantName(), groupID, SPLUNK_QUERY_TIME_GAP);
		List<String> sjcHosts = splunkUtils.getSJCHosts(suiteData.getEnvironmentName());
		s_assert = SplunkUtils.validateSplunkResults(splunkCreateResult.getQueryResult(), sjcHosts, s_assert, 1,SPLUNK_WAIT_TIME,true);
		
		SplunkQueryResult splunkDeleteResult = SplunkQueries.lookForUserGroupsDeleteEditLogs(suiteData.getEnvironmentName(), suiteData.getTenantName(), groupName+email, SPLUNK_QUERY_TIME_GAP);
		s_assert = SplunkUtils.validateSplunkResults(splunkDeleteResult.getQueryResult(), sjcHosts, s_assert, 1,SPLUNK_WAIT_TIME,true);

		//Delete User
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Logger.info("Deleting a user: "+email);
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		infraActions.deleteUser(suiteData, requestHeaders, userId);
		String deleteUserTime = new DCIFunctions().getCurrentTime();
		s_assert.assertAll();
	}
	@AfterSuite(alwaysRun=true)
	public void cleanDataAfterExecution() throws Exception{
		new SplunkUtils().deleteAllKeySecure(restClient, suiteData, requestHeaders);
		protectFunctions.deleteAllPolicies(restClient, requestHeaders, suiteData);
		protectFunctions.deleteAllPolicyTemplate(restClient, requestHeaders, suiteData);
	}
}