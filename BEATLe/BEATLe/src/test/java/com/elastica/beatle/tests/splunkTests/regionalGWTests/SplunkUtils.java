/**
 * 
 */
package com.elastica.beatle.tests.splunkTests.regionalGWTests;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.asserts.SoftAssert;

import com.elastica.beatle.TestSuiteDTO;
import com.elastica.beatle.RestClient.Client;
import com.elastica.beatle.RestClient.ClientUtil;
import com.elastica.beatle.fileHandler.FileHandlingUtils;
import com.elastica.beatle.infra.InfraActions;
import com.elastica.beatle.logger.Logger;
import com.elastica.beatle.protect.ProtectFunctions;
import com.elastica.beatle.protect.ProtectTestConstants;
import com.elastica.beatle.splunk.SplunkQueries;
import com.elastica.beatle.splunk.SplunkQueryResult;
import com.elastica.beatle.splunk.GWTests.GateletQueryBuilder;
import com.elastica.beatle.splunk.GWTests.GateletStatusConstants;

import junit.framework.Assert;
import net.sf.json.util.JSONTokener;

/**
 * @author anuvrath
 *
 */
public class SplunkUtils {
	
	/**
	 * @return
	 */
	public synchronized static String getPolicyName() {
		return UUID.randomUUID().toString();
	}
	
	/**
	 * @return
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public static List<String> getGWHostDetails() throws FileNotFoundException, IOException {
		String[] hosts = FileHandlingUtils.readDataFromFile("/src/test/resources/Gateway/GWData/GWHostList_prod").split("\n");
		List<String> hostList1 = new ArrayList<>();
		for(String host: hosts){
			hostList1.add(host);
		}
		return hostList1;
	}

	/**
	 * @param policyType
	 * @param suiteData 
	 * @param requestHeaders 
	 * @return
	 * @throws Exception 
	 */
	public synchronized static HttpResponse createPolicy(ProtectFunctions protectFunctions,String policyType,Client restClient,String policyName, List<NameValuePair> requestHeaders, TestSuiteDTO suiteData) throws Exception {
		if(policyType.equals("FILESHARE_POLICY"))
			return protectFunctions.createGenericFileSharingPolicyGatelet(restClient, policyName, requestHeaders, suiteData);
		else if(policyType.equals("FILETRANSFER_POLICY"))
			return protectFunctions.createGenericFileTransferPolicyGatelet(restClient, policyName, requestHeaders, suiteData);
		else if(policyType.equals("ACCESSENFORCEMENT_POLICY"))
			return protectFunctions.createGenericAccessEnforcementGatelet(restClient, policyName, requestHeaders, suiteData);
		return null;
	}

	/**
	 * @param policyType
	 * @param policyName 
	 * @return
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public synchronized static String getPolicyJSON(String policyType, String policyName) throws FileNotFoundException, IOException {
		if(policyType.equals("FILESHARE_POLICY"))
			return FileHandlingUtils.readDataFromFile(ProtectTestConstants.GENERIC_FILE_SHARING_POLICY).replace("{POLICY_NAME}", policyName);
		else if(policyType.equals("FILETRANSFER_POLICY"))
			return FileHandlingUtils.readDataFromFile(ProtectTestConstants.GENERIC_FILE_TRANSFER_POLICY).replace("{POLICY_NAME}", policyName);
		else if(policyType.equals("ACCESSENFORCEMENT_POLICY"))
			return FileHandlingUtils.readDataFromFile(ProtectTestConstants.GENERIC_ACCESS_ENFORCEMENT_POLICY).replace("{POLICY_NAME}", policyName);
		return null;
	}
	
	/**
	 * @param policyResponseObject
	 * @throws JSONException
	 */
	public synchronized static SoftAssert validateCreatePolicyResponse(JSONObject policyResponseObject, SoftAssert s_assert) throws JSONException{
		s_assert.assertEquals(policyResponseObject.getString("exceptions"), "","Create response exception is not empty");
		s_assert.assertEquals(policyResponseObject.getString("action_status"), "success","Create Policy action status is not success");
		s_assert.assertFalse(policyResponseObject.getString("api_response").isEmpty(),"Create response api_response is empty");
		s_assert.assertEquals(policyResponseObject.getString("__error"), "","Create response error is not empty");
		return s_assert;
	}

	/**
	 * @param queryResult
	 * @param hostList 
	 * @param s_assert
	 * @param policyCreationTime 
	 * @param i
	 * @return
	 * @throws JSONException 
	 */
	public synchronized static SoftAssert validateSplunkResultsWithTimeDiff(boolean validateCount, JSONObject queryResult, List<String> hostList, SoftAssert s_assert, int operationCount, String operationTime, long maxDelay, long splunkWaitTime, boolean validateTotalCount) throws JSONException {
		JSONArray resultArray = queryResult.getJSONArray("results");
		s_assert = validateSplunkResultsData(resultArray,hostList,s_assert,operationCount,splunkWaitTime, validateTotalCount);
		Logger.info("Operation time:"+operationTime);
		Logger.info("_________________________________________________________________");
		Logger.info("");
		
		for(int counter = 0; counter<resultArray.length(); counter++){
			JSONObject object = resultArray.getJSONObject(counter);			
			Logger.info("Records found for host: "+object.getString("host"));
			Logger.info("Expected total number of events per host: "+operationCount);
			Logger.info("Actual total number of events per host: "+object.getString("count"));
			if(validateCount)
				s_assert.assertEquals(object.getInt("count"), operationCount,"Total number of operation count doesn't match.");
			else
				s_assert.assertTrue(object.getInt("count")>=1,"Total number of operation count doesn't match.");
			DateTime operationDateTime = convertTimeZones("UTC", operationTime);
			DateTime splunkEventDateTime = convertTimeZones("UTC", object.getString("_time"));
			Logger.info("Maximum delay expected between operation and change propogation is: "+maxDelay);
			long actualDelay = splunkEventDateTime.getMillis() - operationDateTime.getMillis();
			Logger.info("Actual delay between operation and change propogation is: "+actualDelay);
			s_assert.assertTrue(actualDelay<= maxDelay,"Actual log propagation delay is more than expected");
			Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		}
		return s_assert;
	}
	
	public synchronized static SoftAssert validateSplunkResults(JSONObject queryResult, List<String> hostList, SoftAssert s_assert, int operationCount, long splunkWaitTime, boolean validateTotalCount) throws JSONException {
		JSONArray resultArray = queryResult.getJSONArray("results");
		s_assert = validateSplunkResultsData(resultArray,hostList,s_assert,operationCount, splunkWaitTime,validateTotalCount);
		Logger.info("_________________________________________________________________");
		Logger.info("");
		return s_assert;
	}
	
	public synchronized static SoftAssert validateSplunkResultsData(JSONArray resultArray, List<String> hostList, SoftAssert s_assert, int operationCount, long splunkWaitTime, boolean validateTotalCount) throws JSONException{
		List<String> splunkResultHostList = new ArrayList<>();
		for(int i = 0 ; i<resultArray.length();i++ ){
				splunkResultHostList.add(resultArray.getJSONObject(i).getString("host"));
		}
		
		Logger.info("_________________________________________________________________");
		Logger.info("Validating results for all hosts");
		Logger.info("_________________________________________________________________");
		Logger.info("Expected results");		
		Logger.info("Expected total number of GW hosts: "+hostList.size());
		Logger.info("Expected total number of events per host: "+operationCount);
		if(validateTotalCount)
			s_assert.assertEquals(splunkResultHostList.size(), hostList.size(),"Total number of hosts found in splunk is not equal to the number of hosts we are expecting");		
		Logger.info("Actual results");				
		Logger.info("Actual total number of GW hosts found in the splunk results: "+splunkResultHostList.size());
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Logger.info("Hosts that are found/Not found in the splunk query results are: ");
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		
		if(splunkResultHostList.size()<= hostList.size()){
			for(String host: hostList){
				if(splunkResultHostList.contains(host))
					Logger.info("Host "+host+" is found in the splunk results");
				else
					Logger.info("Host "+host+" NOT found in the splunk results");
				s_assert.assertTrue(splunkResultHostList.contains(host),"Host "+ host+" Didn't get the config updates even after waiting for "+TimeUnit.MILLISECONDS.toMinutes(splunkWaitTime)+" Minutes");
			}
		}else if(splunkResultHostList.size()>hostList.size()){
			for(String host: splunkResultHostList){
				if(hostList.contains(host))
					Logger.info("Host "+host+" is found in the Host Master list");
				else
					Logger.info("Host "+host+" NOT found in the Host Master list");
			}
		}
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Logger.info(" Validating the number of events per host and the timestamp of the logs");
		Logger.info(" Validating the number of events for "+resultArray.length()+" hosts");
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		for(int counter = 0; counter<resultArray.length(); counter++){
			JSONObject object = resultArray.getJSONObject(counter);			
			Logger.info("Records found for host: "+object.getString("host"));			
			Logger.info("Expected total number of events per host: "+operationCount);
			Logger.info("Actual total number of events per host: "+object.getString("count"));	
			if(validateTotalCount)
				s_assert.assertEquals(object.getInt("count"), operationCount,"Total number of operation count doesn't match.");
			else
				s_assert.assertTrue(object.getInt("count")>= operationCount,"Total number of operation count doesn't match.");
			Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		}
		return s_assert;
	}
	/**
	 * @param toTimeZoneString
	 * @param fromDateTime
	 * @return
	 */
	public static DateTime convertTimeZones(String toTimeZoneString, String fromDateTime) {
	    return new DateTime(fromDateTime, DateTimeZone.forID(toTimeZoneString));
	}
	
	public String getAllGatelets(Client restClient, List<NameValuePair> requestHeader, TestSuiteDTO suiteData) throws Exception{
		String requestUri = "/admin/application/list/tenantavailableapps?lang=en";
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(),requestUri);
		HttpResponse response = restClient.doGet(dataUri, requestHeader);
		String responseBody = ClientUtil.getResponseBody(response);
		String applications = ClientUtil.getJSONValue(responseBody, "applications");
		return applications;
	}

	/**
	 * Gatelet activation/deactivation/finalize deactivation
	 * @param restClient
	 * @param requestHeader
	 * @param suiteData
	 * @param appName
	 * @param activity
	 * @param gateletData
	 * @return
	 * @throws Exception
	 */
	public HttpResponse generatePayloadForGaleletActivationDeactivation(Client restClient, List<NameValuePair> requestHeader, TestSuiteDTO suiteData,
			String appName, String activity, Map<String, String> gateletData) throws Exception{
		GateletQueryBuilder builder = new GateletQueryBuilder();
		List<String> policyBlackList = new ArrayList<String>();
		List<String> category = new ArrayList<String>();
		Map<String, String> appAccounts = new HashMap<String, String>();
		String payload = null;
		net.sf.json.JSONArray policyBlackListArray = (net.sf.json.JSONArray) new JSONTokener(gateletData.get(GateletStatusConstants.POLICY_BLACKLIST)).nextValue();
		for (int i = 0; i < policyBlackListArray.size(); i++) {
			policyBlackList.add(policyBlackListArray.getString(i));
		}
		
		net.sf.json.JSONArray categoryArray = (net.sf.json.JSONArray) new JSONTokener(gateletData.get(GateletStatusConstants.CATEGORY)).nextValue();
		System.out.println(categoryArray.size());
		for (int j = 0; j < categoryArray.size(); j++) {
			category.add(categoryArray.getString(j));
		}
		net.sf.json.JSONArray appAccountsArray = (net.sf.json.JSONArray) new JSONTokener(gateletData.get(GateletStatusConstants.APP_ACCOUNTS)).nextValue();
		for (int i = 0; i < appAccountsArray.size(); i++) {
			String appAccountsObject = appAccountsArray.getJSONObject(i).toString();
			String domain = ClientUtil.getJSONValue(appAccountsObject, "domain");
			domain = domain.substring(1, domain.length()-1);
			appAccounts.put(GateletStatusConstants.DOMAIN, domain);
			String adminEmail = ClientUtil.getJSONValue(appAccountsObject, "admin_email");
			adminEmail = adminEmail.substring(1, adminEmail.length()-1);
			appAccounts.put(GateletStatusConstants.ADMIN_EMAIL, adminEmail);
			String accountName = ClientUtil.getJSONValue(appAccountsObject, "account_name");
			accountName = accountName.substring(1, accountName.length()-1);
			appAccounts.put(GateletStatusConstants.ACCOUNT_NAME, accountName);
			String activationTime = ClientUtil.getJSONValue(appAccountsObject, "activation_time");
			activationTime = activationTime.substring(1, activationTime.length()-1);
			appAccounts.put(GateletStatusConstants.ACTIVATION_TIME, activationTime);
			String accountId = ClientUtil.getJSONValue(appAccountsObject, "account_id");
			accountId = accountId.substring(1, accountId.length()-1);
			appAccounts.put(GateletStatusConstants.ACCOUNT_ID, accountId);
		}
		if(activity.equalsIgnoreCase("activate")){
			payload = builder.activateGateletQuery(appName, appAccounts, category, policyBlackList, gateletData);
		}else if(activity.equalsIgnoreCase("deactivate")){
			payload = builder.pendingDeactivateGateletQuery(appName, appAccounts, category, policyBlackList, gateletData);
		}else if(activity.equalsIgnoreCase("deactivatefinalize")){
			payload = builder.deactivateFinalizeGateletQuery(appName, appAccounts, category, policyBlackList, gateletData);
		}
		String requestUri = "/admin/application/updategwapp";
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(),requestUri);
		StringEntity entity = new StringEntity(payload);
		HttpResponse response = restClient.doPost(dataUri, requestHeader, null, entity);
		return response;
	}

	/**
	 * Parsing the gatelet list response for generating the gatelet activation payload
	 * @param applications
	 * @param appName
	 * @return
	 * @throws JsonProcessingException
	 * @throws IOException
	 */
	public Map<String, String> setAllGateletParameters(String applications, String appName) throws JsonProcessingException, IOException{

		net.sf.json.JSONArray jArray = (net.sf.json.JSONArray) new JSONTokener(applications).nextValue();
		Map<String, String> gateletData = new HashMap<String, String>();
		for (int i = 0; i < jArray.size(); i++) {
			String gateletDetails = jArray.getJSONObject(i).toString();
			String application = ClientUtil.getJSONValue(gateletDetails, "app_name");
			application = application.substring(1, application.length()-1);
			if(appName.equals(application)){
				gateletData.put(GateletStatusConstants.APP_NAME, application);
				String activationStatus = ClientUtil.getJSONValue(gateletDetails, "api_activated");
				gateletData.put(GateletStatusConstants.ACTIVATION_STATUS, activationStatus);
				String pendingDelete = ClientUtil.getJSONValue(gateletDetails, "pending_delete");
				gateletData.put(GateletStatusConstants.PENDING_DELETE, pendingDelete);
				
				String video2_desc = ClientUtil.getJSONValue(gateletDetails, "video2_desc");
				video2_desc = video2_desc.substring(1, video2_desc.length()-1);
				gateletData.put(GateletStatusConstants.VIDEO2_DESC, video2_desc);
				String app_accounts = ClientUtil.getJSONValue(gateletDetails, "app_accounts");
				gateletData.put(GateletStatusConstants.APP_ACCOUNTS, app_accounts);
				String epd_less_category = ClientUtil.getJSONValue(gateletDetails, "epd_less_category");
				epd_less_category = epd_less_category.substring(1, epd_less_category.length()-1);
				gateletData.put(GateletStatusConstants.EPD_LESS_CATEGORY, epd_less_category);
				String is_multi_account = ClientUtil.getJSONValue(gateletDetails, "is_multi_account");
				gateletData.put(GateletStatusConstants.IS_MULTI_ACCOUNT, is_multi_account);
				String gateway_subscription_id = ClientUtil.getJSONValue(gateletDetails, "gateway_subscription_id");
				gateway_subscription_id = gateway_subscription_id.substring(1, gateway_subscription_id.length()-1);
				gateletData.put(GateletStatusConstants.GATEWAY_SUBSCRIPTION_ID, gateway_subscription_id);
				String category = ClientUtil.getJSONValue(gateletDetails, "category");
				gateletData.put(GateletStatusConstants.CATEGORY, category);
				String own_dashboard = ClientUtil.getJSONValue(gateletDetails, "own_dashboard");
				gateletData.put(GateletStatusConstants.OWN_DASHBOARD, own_dashboard);
				String id = ClientUtil.getJSONValue(gateletDetails, "id");
				id = id.substring(1, id.length()-1);
				gateletData.put(GateletStatusConstants.ID, id);
				String video4_desc = ClientUtil.getJSONValue(gateletDetails, "video4_desc");
				video4_desc = video4_desc.substring(1, video4_desc.length()-1);
				gateletData.put(GateletStatusConstants.VIDEO4_DESC, video4_desc);
				String is_api_subscribed = ClientUtil.getJSONValue(gateletDetails, "is_api_subscribed");
				gateletData.put(GateletStatusConstants.IS_API_SUBSCRIBED, is_api_subscribed);
				String video3_image_url = ClientUtil.getJSONValue(gateletDetails, "video3_image_url");
				video3_image_url = video3_image_url.substring(1, video3_image_url.length()-1);
				gateletData.put(GateletStatusConstants.VIDEO3_IMAGE_URL, video3_image_url);
				String apparazzi_service_id = ClientUtil.getJSONValue(gateletDetails, "apparazzi_service_id");
				gateletData.put(GateletStatusConstants.APPARAZZI_SERVICE_ID, apparazzi_service_id);
				String display_in_showcase = ClientUtil.getJSONValue(gateletDetails, "display_in_showcase");
				gateletData.put(GateletStatusConstants.DISPLAY_IN_SHOWCASE, display_in_showcase);
				String in_beta = ClientUtil.getJSONValue(gateletDetails, "in_beta");
				gateletData.put(GateletStatusConstants.IN_BETA, in_beta);
				String tagline = ClientUtil.getJSONValue(gateletDetails, "tagline");
				tagline = tagline.substring(1, tagline.length()-1);
				gateletData.put(GateletStatusConstants.TAGLINE, tagline);
				String epd_capable = ClientUtil.getJSONValue(gateletDetails, "epd_capable");
				gateletData.put(GateletStatusConstants.EPD_CAPABLE, epd_capable);
				String short_desc = ClientUtil.getJSONValue(gateletDetails, "short_desc");
				short_desc = short_desc.substring(1, short_desc.length()-1);
				gateletData.put(GateletStatusConstants.SHORT_DESC, short_desc);
				String small_icon_uri = ClientUtil.getJSONValue(gateletDetails, "small_icon_uri");
				small_icon_uri= small_icon_uri.substring(1, small_icon_uri.length()-1);
				gateletData.put(GateletStatusConstants.SMALL_ICON_URI, small_icon_uri);
				String epd_less_capable = ClientUtil.getJSONValue(gateletDetails, "epd_less_capable");
				gateletData.put(GateletStatusConstants.EPD_LESS_CAPABLE, epd_less_capable);
				String screenshot3_uri = ClientUtil.getJSONValue(gateletDetails, "screenshot3_uri");
				screenshot3_uri = screenshot3_uri.substring(1, screenshot3_uri.length()-1);
				gateletData.put(GateletStatusConstants.SCREENSHOT3_URI, screenshot3_uri);
				String video2_image_url = ClientUtil.getJSONValue(gateletDetails, "video2_image_url");
				video2_image_url = video2_image_url.substring(1, video2_image_url.length()-1);
				gateletData.put(GateletStatusConstants.VIDEO2_IMAGE_URL, video2_image_url);
				String api_policy_capable = ClientUtil.getJSONValue(gateletDetails, "api_policy_capable");
				gateletData.put(GateletStatusConstants.API_POLICY_CAPABLE, api_policy_capable);
				String video3_url = ClientUtil.getJSONValue(gateletDetails, "video3_url");
				video3_url = video3_url.substring(1, video3_url.length()-1);
				gateletData.put(GateletStatusConstants.VIDEO3_URL, video3_url);
				String generic_epd = ClientUtil.getJSONValue(gateletDetails, "generic_epd");
				gateletData.put(GateletStatusConstants.GENERIC_EPD, generic_epd);
				String subscription_id = ClientUtil.getJSONValue(gateletDetails, "subscription_id");
				subscription_id = subscription_id.substring(1, subscription_id.length()-1);
				gateletData.put(GateletStatusConstants.SUBSCRIPTION_ID, subscription_id);
				String api_key = ClientUtil.getJSONValue(gateletDetails, "api_key");
				api_key = api_key.substring(1, api_key.length()-1);
				gateletData.put(GateletStatusConstants.API_KEY, api_key);
				String api_on_demand_scan_capable = ClientUtil.getJSONValue(gateletDetails, "api_on_demand_scan_capable");
				gateletData.put(GateletStatusConstants.API_ON_DEMAND_SCAN_CAPABLE, api_on_demand_scan_capable);
				String video4_url = ClientUtil.getJSONValue(gateletDetails, "video4_url");
				video4_url = video4_url.substring(1, video4_url.length()-1);
				gateletData.put(GateletStatusConstants.VIDEO4_URL, video4_url);
				String screenshot1_uri = ClientUtil.getJSONValue(gateletDetails, "screenshot1_uri");
				screenshot1_uri = screenshot1_uri.substring(1, screenshot1_uri.length()-1);
				gateletData.put(GateletStatusConstants.SCREENSHOT1_URI, screenshot1_uri);
				String long_desc = ClientUtil.getJSONValue(gateletDetails, "long_desc");
				long_desc = long_desc.substring(1, long_desc.length()-1);
				gateletData.put(GateletStatusConstants.LONG_DESC, long_desc);
				String screenshot5_uri = ClientUtil.getJSONValue(gateletDetails, "screenshot5_uri");
				screenshot5_uri = screenshot5_uri.substring(1, screenshot5_uri.length()-1);
				gateletData.put(GateletStatusConstants.SCREENSHOT5_URI, screenshot5_uri);
				String policy_blacklist = ClientUtil.getJSONValue(gateletDetails, "policy_blacklist");
				gateletData.put(GateletStatusConstants.POLICY_BLACKLIST, policy_blacklist);
				String signup_capable = ClientUtil.getJSONValue(gateletDetails, "signup_capable");
				gateletData.put(GateletStatusConstants.SIGNUP_CAPABLE, signup_capable);
				String video1_desc = ClientUtil.getJSONValue(gateletDetails, "video1_desc");
				video1_desc = video1_desc.substring(1, video1_desc.length()-1);
				gateletData.put(GateletStatusConstants.VIDEO1_DESC, video1_desc);
				String price = ClientUtil.getJSONValue(gateletDetails, "price");
				gateletData.put(GateletStatusConstants.PRICE, price);
				String is_active = ClientUtil.getJSONValue(gateletDetails, "is_active");
				gateletData.put(GateletStatusConstants.IS_ACTIVE, is_active);
				String video4_image_url = ClientUtil.getJSONValue(gateletDetails, "video4_image_url");
				video4_image_url = video4_image_url.substring(1, video4_image_url.length()-1);
				gateletData.put(GateletStatusConstants.VIDEO4_IMAGE_URL, video4_image_url);
				String header_bg_color = ClientUtil.getJSONValue(gateletDetails, "header_bg_color");
				header_bg_color = header_bg_color.substring(1, header_bg_color.length()-1);
				gateletData.put(GateletStatusConstants.HEADER_BG_COLOR, header_bg_color);
				String display_in_store = ClientUtil.getJSONValue(gateletDetails, "display_in_store");
				gateletData.put(GateletStatusConstants.DISPLAY_IN_STORE, display_in_store);
				String is_available = ClientUtil.getJSONValue(gateletDetails, "is_available");
				gateletData.put(GateletStatusConstants.IS_AVAILABLE, is_available);
				String is_alerting = ClientUtil.getJSONValue(gateletDetails, "is_alerting");
				gateletData.put(GateletStatusConstants.IS_ALERTING, is_alerting);
				String modified_on = ClientUtil.getJSONValue(gateletDetails, "modified_on");
				modified_on = modified_on.substring(1, modified_on.length()-1);
				gateletData.put(GateletStatusConstants.MODIFIED_ON, modified_on);
				String screenshot2_uri = ClientUtil.getJSONValue(gateletDetails, "screenshot2_uri");
				screenshot2_uri = screenshot2_uri.substring(1, screenshot2_uri.length()-1);
				gateletData.put(GateletStatusConstants.SCREENSHOT2_URI, screenshot2_uri);
				String is_blocked = ClientUtil.getJSONValue(gateletDetails, "is_blocked");
				gateletData.put(GateletStatusConstants.IS_BLOCKED, is_blocked);
				String is_gateway_subscribed = ClientUtil.getJSONValue(gateletDetails, "is_gateway_subscribed");
				gateletData.put(GateletStatusConstants.IS_GATEWAY_SUBSCRIBED, is_gateway_subscribed);
				String long_desc1 = ClientUtil.getJSONValue(gateletDetails, "long_desc1");
				long_desc1 = long_desc1.substring(1, long_desc1.length()-1);
				gateletData.put(GateletStatusConstants.LONG_DESC1, long_desc1);
				String lang = ClientUtil.getJSONValue(gateletDetails, "lang");
				lang = lang.substring(1, lang.length()-1);
				gateletData.put(GateletStatusConstants.LANG, lang);
				String video3_desc = ClientUtil.getJSONValue(gateletDetails, "video3_desc");
				video3_desc = video3_desc.substring(1, video3_desc.length()-1);
				gateletData.put(GateletStatusConstants.VIDEO3_DESC, video3_desc);
				String name = ClientUtil.getJSONValue(gateletDetails, "name");
				name = name.substring(1, name.length()-1);
				gateletData.put(GateletStatusConstants.NAME, name);
				String long_desc2 = ClientUtil.getJSONValue(gateletDetails, "long_desc2");
				long_desc2 = long_desc2.substring(1, long_desc2.length()-1);
				gateletData.put(GateletStatusConstants.LONG_DESC2, long_desc2);
				String is_gateway_subscription_requested = ClientUtil.getJSONValue(gateletDetails, "is_gateway_subscription_requested");
				gateletData.put(GateletStatusConstants.IS_GATEWAY_SUBSCRIPTION_REQUESTED, is_gateway_subscription_requested);
				String screenshot4_uri = ClientUtil.getJSONValue(gateletDetails, "screenshot4_uri");
				screenshot4_uri = screenshot4_uri.substring(1, screenshot4_uri.length()-1);
				gateletData.put(GateletStatusConstants.SCREENSHOT4_URI, screenshot4_uri);
				String big_icon_uri = ClientUtil.getJSONValue(gateletDetails, "big_icon_uri");
				big_icon_uri = big_icon_uri.substring(1, big_icon_uri.length()-1);
				gateletData.put(GateletStatusConstants.BIG_ICON_URI, big_icon_uri);
				String popularity = ClientUtil.getJSONValue(gateletDetails, "popularity");
				gateletData.put(GateletStatusConstants.POPULARITY, popularity);
				String addl_params_needed = ClientUtil.getJSONValue(gateletDetails, "addl_params_needed");
				gateletData.put(GateletStatusConstants.ADDL_PARAMS_NEEDED, addl_params_needed);
				String is_api_subscription_requested = ClientUtil.getJSONValue(gateletDetails, "is_api_subscription_requested");
				gateletData.put(GateletStatusConstants.IS_API_SUBSCRIPTION_REQUESTED, is_api_subscription_requested);
				String risk_status = ClientUtil.getJSONValue(gateletDetails, "risk_status");
				risk_status = risk_status.substring(1, risk_status.length()-1);
				gateletData.put(GateletStatusConstants.RISK_STATUS, risk_status);
				String video1_image_url = ClientUtil.getJSONValue(gateletDetails, "video1_image_url");
				video1_image_url = video1_image_url.substring(1, video1_image_url.length()-1);
				gateletData.put(GateletStatusConstants.VIDEO1_IMAGE_URL, video1_image_url);
				String hosts = ClientUtil.getJSONValue(gateletDetails, "hosts");
				hosts = hosts.substring(1, hosts.length()-1);
				gateletData.put(GateletStatusConstants.HOSTS, hosts);
				String api_capable = ClientUtil.getJSONValue(gateletDetails, "api_capable");
				gateletData.put(GateletStatusConstants.API_CAPABLE, api_capable);
				String support_gateway_encryption = ClientUtil.getJSONValue(gateletDetails, "support_gateway_encryption");
				gateletData.put(GateletStatusConstants.SUPPORT_GATEWAY_ENCRYPTION, support_gateway_encryption);
				String tenant_app_id = ClientUtil.getJSONValue(gateletDetails, "tenant_app_id");
				tenant_app_id = tenant_app_id.substring(1, tenant_app_id.length()-1);
				gateletData.put(GateletStatusConstants.TENANT_APP_ID, tenant_app_id);
				String resource_uri = ClientUtil.getJSONValue(gateletDetails, "resource_uri");
				resource_uri = resource_uri.substring(1, resource_uri.length()-1);
				gateletData.put(GateletStatusConstants.RESOURCE_URI, resource_uri);
				String pac_urls = ClientUtil.getJSONValue(gateletDetails, "pac_urls");
				pac_urls = pac_urls.substring(1, pac_urls.length()-1);
				gateletData.put(GateletStatusConstants.PAC_URLS, pac_urls);
				String video1_url = ClientUtil.getJSONValue(gateletDetails, "video1_url");
				video1_url = video1_url.substring(1, video1_url.length()-1);
				gateletData.put(GateletStatusConstants.VIDEO1_URL, video1_url);
				String video2_url = ClientUtil.getJSONValue(gateletDetails, "video2_url");
				video2_url = video2_url.substring(1, video2_url.length()-1);
				gateletData.put(GateletStatusConstants.VIDEO2_URL, video2_url);
				break;
			}
		}
		return gateletData;
	}
	
	/**
	 * Verifing the GateletStatusResponse
	 * @param response
	 * @param gateletStatus
	 * @throws JsonProcessingException
	 * @throws IOException
	 */
	public void verifyGateletStatusResponse(HttpResponse response, String gateletStatus) throws JsonProcessingException, IOException{
		String gateletStatusResponseBody = ClientUtil.getResponseBody(response);
		if(gateletStatus.equals("")){
			Assert.assertEquals(true, ClientUtil.getJSONValue(gateletStatusResponseBody, "is_active"));
			Assert.assertEquals(false, ClientUtil.getJSONValue(gateletStatusResponseBody, "pending_delete"));
			Assert.assertEquals(false, ClientUtil.getJSONValue(gateletStatusResponseBody, "is_available"));
		}else if(gateletStatus.equals("")){
			Assert.assertEquals(true, ClientUtil.getJSONValue(gateletStatusResponseBody, "is_active"));
			Assert.assertEquals(true, ClientUtil.getJSONValue(gateletStatusResponseBody, "pending_delete"));
			Assert.assertEquals(false, ClientUtil.getJSONValue(gateletStatusResponseBody, "is_available"));
		}else if(gateletStatus.equals("")){
			Assert.assertEquals(false, ClientUtil.getJSONValue(gateletStatusResponseBody, "is_active"));
			Assert.assertEquals(false, ClientUtil.getJSONValue(gateletStatusResponseBody, "pending_delete"));
			Assert.assertEquals(false, ClientUtil.getJSONValue(gateletStatusResponseBody, "is_available"));
		}
	}

	/**
	 * @param environmentName
	 * @return
	 * @throws JSONException 
	 */
	public static List<String> getAllLiveHosts(String environmentName) throws JSONException {
		SplunkQueryResult result = SplunkQueries.getAllLiveGWHosts(environmentName);
		JSONArray objectArray = new JSONArray(result.getQueryResult().getString("results"));
		List<String> hostList = new ArrayList<>();
		for(int i=0;i<objectArray.length();i++){
			JSONObject object = objectArray.getJSONObject(i);
			if(!object.getString("host").contains("gatewaym"))
				hostList.add(object.getString("host"));			
		}
		return hostList;
	}
	
	/**
	 * @param environmentName
	 * @return
	 * @throws JSONException 
	 */
	public List<String> getSJCHosts(String environmentName) throws JSONException {
		SplunkQueryResult result = SplunkQueries.getAllLiveGWHosts(environmentName);
		JSONArray objectArray = new JSONArray(result.getQueryResult().getString("results"));
		List<String> hostList = new ArrayList<>();
		for(int i=0;i<objectArray.length();i++){
			JSONObject object = objectArray.getJSONObject(i);
			if(object.getString("host").contains("-sjc-") && !object.getString("host").contains("gatewaym"))
				hostList.add(object.getString("host"));			
		}
		return hostList;
	}
	
	public HttpResponse createPolicyTemplate(ProtectFunctions protectFunctions,String policyType,Client restClient,String templateName, List<NameValuePair> requestHeaders, TestSuiteDTO suiteData) throws Exception {
		if(policyType.equals("FILESHARE_POLICY")){
			policyType = "documentshare";
		} else if(policyType.equals("FILETRANSFER_POLICY")){
			policyType = "filexfer";
		} else if(policyType.equals("ACCESSENFORCEMENT_POLICY")){
			policyType = "accessenforcement";
		}
		return protectFunctions.createPolicyTemplate(restClient, requestHeaders, suiteData, templateName, policyType);
	}
	
	public HttpResponse editPolicyTemplate(Client restClient, List<NameValuePair> requestHeaders, TestSuiteDTO suiteData, ProtectFunctions protectFunctions, String templateName, String policyType) throws Exception{
		if(policyType.equals("FILESHARE_POLICY")){
			policyType = "documentshare";
		} else if(policyType.equals("FILETRANSFER_POLICY")){
			policyType = "filexfer";
		} else if(policyType.equals("ACCESSENFORCEMENT_POLICY")){
			policyType = "accessenforcement";
		}
		String templateId = protectFunctions.getResponseTemplateParameter(restClient, requestHeaders, suiteData, templateName, "id");
		return protectFunctions.editPolicyTemplate(restClient, requestHeaders, suiteData, templateName, templateId, policyType);
	}
	
	public HttpResponse deletePolicyTemplate(Client restClient, List<NameValuePair> requestHeaders, TestSuiteDTO suiteData, ProtectFunctions protectFunctions, String templateName) throws Exception{
		String templateId = protectFunctions.getResponseTemplateParameter(restClient, requestHeaders, suiteData, templateName, "id");
		return protectFunctions.deletePolicyTemplate(restClient, requestHeaders, suiteData, templateName, templateId);
	}
	
	public HttpResponse createUser(TestSuiteDTO suiteDatadto, List<NameValuePair> headers, Map<String, String> userData) throws Exception{
		InfraActions infraActions = new InfraActions();
		String payload = "{\"user\":"
				+ "{\"first_name\":\""+userData.get("first_name")+"\",\"last_name\":\""+userData.get("last_name")+"\","
				+ "\"email\":\""+userData.get("email")+"\",\"secondary_user_id\":\"\","
				+ "\"password\":\"\",\"title\":\""+userData.get("title")+"\",\"work_phone\":\"1-123123123\","
				+ "\"cell_phone\":\"1-123123123\",\"access_profiles\":[],\"is_admin\":false,"
				+ "\"is_active\":"+userData.get("is_active")+",\"notes\":\"Notes\",\"is_dpo\":false}}";
		return infraActions.createUser(suiteDatadto, headers, payload);
	}
	
	public HttpResponse createGroup(TestSuiteDTO suiteDatadto, List<NameValuePair> headers, Map<String, String> groupData) throws Exception{
		InfraActions infraActions = new InfraActions();
		String payload = "{\"group\":{\"name\":\""+groupData.get("name")+"\",\"description\":\""+groupData.get("description")+"\",\"is_active\":"+groupData.get("is_active")+",\"notes\":\""+groupData.get("notes")+"\"}}";
		return infraActions.createGroup(suiteDatadto, headers, payload);
	}
	
	public HttpResponse editGroup(Client restClient, TestSuiteDTO suiteData, List<NameValuePair> headers, Map<String, String> groupData) throws Exception{
		InfraActions infraActions = new InfraActions();
		return infraActions.editGroup(restClient, suiteData, headers, groupData);
	}
	
	public HttpResponse deleteGroup(TestSuiteDTO suiteDatadto, List<NameValuePair> headers, String groupId) throws Exception{
		InfraActions infraActions = new InfraActions();
		return infraActions.deleteGroup(suiteDatadto, headers, groupId);
	}
	
	public Map<String, String> addKeySecure(Client restClient, TestSuiteDTO suiteData, List<NameValuePair> headers, String keySecureName) throws Exception{
		//keySecureName = "Abc";
		File file = new File(GateletStatusConstants.CLIENT_FILE_PATH);
		System.out.println(file.exists());
		File file1 = new File(GateletStatusConstants.SECURE_FILE_PATH);
		System.out.println(file1.exists());
		MultipartEntityBuilder reqEntity = MultipartEntityBuilder.create();
		FileBody fileBodyClient = new FileBody(new File(GateletStatusConstants.CLIENT_FILE_PATH), org.apache.http.entity.ContentType.create("application/x-pkcs12"), "Client-cert.p12");
		FileBody fileBodyServer = new FileBody(new File(GateletStatusConstants.SECURE_FILE_PATH), org.apache.http.entity.ContentType.create("application/x-x509-ca-cert"), "Elastica-KeySecure.crt");
		HttpEntity entity = reqEntity
				.setBoundary("----WebKitFormBoundaryCLQHpDNmcjWZz7aR")
				.setCharset(Charset.forName("UTF-8"))
				.setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
				.addPart("file", fileBodyClient)
				//.addBinaryBody("client_certificate", new File(GateletStatusConstants.CLIENT_FILE_PATH))
				.addPart("file", fileBodyClient)
				//.addBinaryBody("server_certificate", new File(GateletStatusConstants.SECURE_FILE_PATH))
				.addTextBody("username", "")
				.addTextBody("client_certificate_password", "1234")
				.addTextBody("vendor", "SafeNet KeySecure")
				.addTextBody("description", "")
				.addTextBody("keys", "[{\"version\":1,\"name\":\"amateen-cert-test\"}]")
				.addTextBody("nae_host", "50.207.165.70")
				.addTextBody("is_active", "false")
				.addTextBody("timeout", "30")
				.addTextBody("kmip_host", "")
				.addTextBody("nae_port", "9000")
				.addTextBody("file_size", "20480")
				.addTextBody("kmip_port", "9000")
				.addTextBody("password", "")
				.addTextBody("crypto_at", "Client")
				.addTextBody("name", keySecureName).build();
		String keySecureUri = "/admin/application/add_keyserver";
		URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(), keySecureUri);
		HttpResponse response = restClient.doPost(uri, headers, null, entity);
		String responseBody = ClientUtil.getResponseBody(response);
		Logger.info(responseBody);
		HashMap<String, String> respPara = new ObjectMapper().readValue(responseBody, HashMap.class);
		Assert.assertEquals(true, respPara.get("ok"));
		Assert.assertEquals(201, respPara.get("status"));
		Map<String, String> keySecureData = new ObjectMapper().readValue(ClientUtil.getJSONValue(responseBody, "data"), HashMap.class);
		return keySecureData;
		
	}
	
	public Map<String, String> getKeySecureDetails(Client restClient, TestSuiteDTO suiteData, List<NameValuePair> headers, String keySecureName) throws Exception{
		Map<String, String> keySecureDetails = new HashMap<String, String>();
		String payload = "{\"url\":\"keyserver\",\"id\":null,\"action\":\"list\",\"params\":{\"limit\":1000},\"data\":null}";
		URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(), "/controls/make_api_request");
		StringEntity entity = new StringEntity(payload);
		HttpResponse response = restClient.doPost(uri, headers, null, entity);
		String responseBody = ClientUtil.getResponseBody(response);
		String objects = ClientUtil.getJSONValue(responseBody, "objects");
		net.sf.json.JSONArray jArray = (net.sf.json.JSONArray) new JSONTokener(objects).nextValue();
		String name = "";
		for (int i = 0; i < jArray.size(); i++) {
			 String objectdetails = jArray.getJSONObject(i).toString();
			 name = ClientUtil.getJSONValue(objectdetails, "name");
			 if(name.equals(keySecureName)){
				 keySecureDetails = new ObjectMapper().readValue(objectdetails, HashMap.class);
				 break;
			 }
		}
		return keySecureDetails;
	}
	
	public Map<String, String> editKeySecure(Client restClient, TestSuiteDTO suiteData, List<NameValuePair> headers, Map<String, String> keySecureDetails, String description) throws Exception{
		MultipartEntityBuilder reqEntity = MultipartEntityBuilder.create();
		String filesize = String.valueOf(keySecureDetails.get("file_size"));
		String id = String.valueOf(keySecureDetails.get("id"));
		String username = String.valueOf(keySecureDetails.get("username"));
		String keys = String.valueOf(keySecureDetails.get("keys"));
		String cryptoAt = String.valueOf(keySecureDetails.get("crypto_at"));
		String active = String.valueOf(keySecureDetails.get("is_active"));
		String naeport = String.valueOf(keySecureDetails.get("nae_port"));
		String clientCertificateSignature = String.valueOf(keySecureDetails.get("client_certificate_signature"));
		String password = String.valueOf(keySecureDetails.get("password"));
		String name = String.valueOf(keySecureDetails.get("name"));
		String naeHost = String.valueOf(keySecureDetails.get("nae_host"));
		String kmipHost = String.valueOf(keySecureDetails.get("kmip_host"));
		String timeout = String.valueOf(keySecureDetails.get("timeout"));
		String kmipPort = String.valueOf(keySecureDetails.get("kmip_port"));
		HttpEntity entity = reqEntity
				.setBoundary("----WebKitFormBoundaryCLQHpDNmcjWZz7aR")
				.setCharset(Charset.forName("UTF-8"))
				.setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
				.addTextBody("client_certificate", "undefined")
				.addTextBody("server_certificate", "undefined")
				.addTextBody("server_certificate_signature", "")
				.addTextBody("file_size", filesize)
				.addTextBody("id", id)
				.addTextBody("client_certificate_password", "1234")
				.addTextBody("username", username)
				.addTextBody("vendor", "SafeNet KeySecure")
				.addTextBody("description", description)
				.addTextBody("keys", "[{\"version\":1,\"id\":\"__NESTED_ID__\",\"name\":\"amateen-cert-test\"}]")
				.addTextBody("crypto_at", cryptoAt)
				.addTextBody("is_active", active)
				.addTextBody("nae_port", naeport)
				.addTextBody("client_certificate_signature", "No friendly name.")
				.addTextBody("password", password)
				.addTextBody("name", name)
				.addTextBody("nae_host", naeHost)
				.addTextBody("kmip_host", kmipHost)
				.addTextBody("timeout", timeout)
				.addTextBody("kmip_port", kmipPort)
				.build();
		String keySecureUri = "/admin/application/add_keyserver";
		URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(), keySecureUri);
		HttpResponse response =  restClient.doPost(uri, headers, null, entity);
		String responseBody = ClientUtil.getResponseBody(response);
		Logger.info(responseBody);
		HashMap<String, String> respPara = new ObjectMapper().readValue(responseBody, HashMap.class);
		Assert.assertEquals(true, respPara.get("ok"));
		Assert.assertEquals(202, respPara.get("status"));
		Map<String, String> keySecureData = new ObjectMapper().readValue(ClientUtil.getJSONValue(responseBody, "data"), HashMap.class);
		return keySecureData;
	}
	
	public Map<String, String> activateKeySecure(Client restClient, TestSuiteDTO suiteData, List<NameValuePair> headers, String keySecureId) throws Exception{
		String payload = "{\"url\":\"keyserver\",\"id\":\""+keySecureId+"\",\"action\":\"patch\",\"params\":null,\"data\":{\"is_active\":true}}";
		URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(), "/controls/make_api_request");
		StringEntity entity = new StringEntity(payload);
		HttpResponse response = restClient.doPost(uri, headers, null, entity);
		String responseBody = ClientUtil.getResponseBody(response);
		Logger.info(responseBody);
		HashMap<String, String> respPara = new ObjectMapper().readValue(responseBody, HashMap.class);
		Assert.assertEquals(true, respPara.get("ok"));
		Assert.assertEquals(202, respPara.get("status"));
		Map<String, String> keySecureData = new ObjectMapper().readValue(ClientUtil.getJSONValue(responseBody, "data"), HashMap.class);
		return keySecureData;
	}
	
	public Map<String, String> deactivateKeySecure(Client restClient, TestSuiteDTO suiteData, List<NameValuePair> headers, String keySecureId) throws Exception{
		String payload = "{\"url\":\"keyserver\",\"id\":\""+keySecureId+"\",\"action\":\"patch\",\"params\":null,\"data\":{\"is_active\":false}}";
		URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(), "/controls/make_api_request");
		StringEntity entity = new StringEntity(payload);
		HttpResponse response =  restClient.doPost(uri, headers, null, entity);
		String responseBody = ClientUtil.getResponseBody(response);
		Logger.info(responseBody);
		HashMap<String, String> respPara = new ObjectMapper().readValue(responseBody, HashMap.class);
		Assert.assertEquals(true, respPara.get("ok"));
		Assert.assertEquals(202, respPara.get("status"));
		Map<String, String> keySecureData = new ObjectMapper().readValue(ClientUtil.getJSONValue(responseBody, "data"), HashMap.class);
		return keySecureData;
	}
	
	public void deleteKeySecure(Client restClient, TestSuiteDTO suiteData, List<NameValuePair> headers, String keySecureId) throws Exception{
		String payload = "{\"url\":\"keyserver\",\"id\":\""+keySecureId+"\",\"action\":\"delete\",\"params\":null,\"data\":null}";
		URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(), "/controls/make_api_request");
		StringEntity entity = new StringEntity(payload);
		HttpResponse response = restClient.doPost(uri, headers, null, entity);
		String responseBody = ClientUtil.getResponseBody(response);
		Logger.info(responseBody);
		HashMap<String, String> respPara = new ObjectMapper().readValue(responseBody, HashMap.class);
		Assert.assertEquals(true, respPara.get("ok"));
		Assert.assertEquals(204, respPara.get("status"));
	}
	
	public void ssoEnableAzureAD(Client restClient, TestSuiteDTO suiteData, List<NameValuePair> headers) throws Exception{
		MultipartEntityBuilder reqEntity = MultipartEntityBuilder.create();
		FileBody fileBodyClient = new FileBody(new File(GateletStatusConstants.AZURE_AD_PATH), org.apache.http.entity.ContentType.create("text/xml"), "azureadmetadata.xml");
		String serviceJson = FileHandlingUtils.readDataFromFile(GateletStatusConstants.AZURE_SERVICE_PATH);
		HttpEntity entity = reqEntity
				.setBoundary("----WebKitFormBoundaryCLQHpDNmcjWZz7aR")
				.setCharset(Charset.forName("UTF-8"))
				.setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
				.addPart("file", fileBodyClient)
				.addTextBody("service", serviceJson)
				.build();
		
		String ssoUri = "/admin/user/tenant_sso_status";
		URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(), ssoUri);
		HttpResponse response =  restClient.doPost(uri, headers, null, entity);
		String responseBody = ClientUtil.getResponseBody(response);
		String apiMessage = ClientUtil.getJSONValue(responseBody, "api_message");
		Assert.assertEquals("Service configured successfully.", apiMessage.substring(1, apiMessage.length()-1));
	}
	
	public HashMap<String, String> getTenantSSOStatus(Client restClient, TestSuiteDTO suiteData, List<NameValuePair> headers) throws Exception{
		String ssoUri = "/admin/user/tenant_sso_status";
		URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(), ssoUri);
		HttpResponse response = restClient.doGet(uri, headers);
		String responseBody = ClientUtil.getResponseBody(response);
		String ssoDetails = ClientUtil.getJSONValue(responseBody, "sso");
		HashMap<String, String> ssoData = new ObjectMapper().readValue(ssoDetails, HashMap.class);
		return ssoData;
	}
	
	public void ssoDisableAzureAD(Client restClient, TestSuiteDTO suiteData, List<NameValuePair> headers, String appId) throws Exception{
		String payload = "{\"app_id\":\""+appId+"\"}";
		String ssoUri = "/admin/user/remove_sso_provider";
		URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(), ssoUri);
		HttpResponse response =  restClient.doPost(uri, headers, null, new StringEntity(payload));
		String responseBody = ClientUtil.getResponseBody(response);
		String actionStatus = ClientUtil.getJSONValue(responseBody, "action_status");
		Assert.assertEquals("success", actionStatus.substring(1, actionStatus.length()-1));
	}
	
	public void deleteAllKeySecure(Client restClient, TestSuiteDTO suiteData, List<NameValuePair> headers) throws Exception{
		String payload = "{\"url\":\"keyserver\",\"id\":null,\"action\":\"list\",\"params\":{\"limit\":1000},\"data\":null}";
		String keySecureListUri = "/controls/make_api_request";
		URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(), keySecureListUri);
		HttpResponse response =  restClient.doPost(uri, headers, null, new StringEntity(payload));
		String responseBody = ClientUtil.getResponseBody(response);
		String objects = ClientUtil.getJSONValue(ClientUtil.getJSONValue(responseBody, "data"), "objects");
		JSONArray objectArray = new JSONArray(objects);
		List<String> hostList = new ArrayList<>();
		if(objectArray.length()>0){
			for(int i=0;i<objectArray.length();i++){
				JSONObject object = objectArray.getJSONObject(i);
				String keySecureId = object.getString("id");
				try{
					this.deleteKeySecure(restClient, suiteData, headers, keySecureId);
				}catch(Exception e){}
			}
		}
	}
	
	public String getPACUrl(Client restClient, TestSuiteDTO suiteData, List<NameValuePair> headers) throws Exception{
		String proxyUrl = "/admin/user/get_proxy";
		URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(), proxyUrl);
		HttpResponse response = restClient.doGet(uri, headers);
		String responseBody = ClientUtil.getResponseBody(response);
		String pacUrl = ClientUtil.getJSONValue(ClientUtil.getJSONValue(responseBody, "objects"), "url");
		pacUrl = pacUrl.substring(1, pacUrl.length()-1);
		return pacUrl;
	}

	public WebDriver setWebDriverGatewayProxy(Client restClient, TestSuiteDTO suiteData, List<NameValuePair> headers) throws Exception{
		String proxyExtension = suiteData.getProxyExtension();
		proxyExtension = GateletStatusConstants.SPLUNK_FOLDER_PATH+proxyExtension;
		String pacUrl = getPACUrl(restClient, suiteData, headers);
		FirefoxProfile profile = new FirefoxProfile();
		profile.setPreference("xpinstall.signatures.required", false);
		profile.setPreference("network.proxy.type", 2);
	    profile.setPreference("network.proxy.autoconfig_url",pacUrl);
		//profile.setPreference("network.proxy.autoconfig_url",suiteData.getProxyUrl());
		File file = new File(proxyExtension);
		System.out.println(file.getAbsolutePath());
		System.out.println(file.exists());
		profile.addExtension(file);
		profile.setPreference("extensions.elastica_auth.currentVersion", suiteData.getProxyExtensionVersion());
		//profile.setAcceptUntrustedCertificates(true);
		
		/*Proxy proxy = new Proxy();
		proxy.setProxyType(Proxy.ProxyType.PAC);
		proxy.setProxyAutoconfigUrl(suiteData.getProxyUrl());*/
		
		DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
		//desiredCapabilities.setCapability("proxy", proxy);
		desiredCapabilities = DesiredCapabilities.firefox();
		desiredCapabilities.setCapability(FirefoxDriver.PROFILE, profile);
		//desiredCapabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
		
		WebDriver driver = new FirefoxDriver(desiredCapabilities);
		driver.manage().window().maximize();
		Cookie cookie = new Cookie("mf_authenticated","EzTxmV5reXvpfmyULo45lsawAeFgT1UZ1uCxSrI8");
		driver.manage().addCookie(cookie);
		driver.navigate().refresh();
		return driver;
	}
	
	public void loginCloudSOC(WebDriver driver, TestSuiteDTO suiteData) throws InterruptedException{
		Logger.info("Loading cloudSoc portal for SSO Login");
		try {
			driver.navigate().refresh();
			driver.get("https://app.elastica.net/");
			Thread.sleep(10000);
			Logger.info("User name: " + suiteData.getUsername()  + " and Password:" + suiteData.getPassword() );
			Logger.info("Enter Username: "+suiteData.getUsername());
			driver.findElement(By.xpath(".//input[@name='email']")).clear();
			driver.findElement(By.xpath(".//input[@name='email']")).sendKeys(suiteData.getUsername());
			Logger.info("Enter Password: "+suiteData.getPassword());
			driver.findElement(By.xpath(".//input[@name='password']")).clear();
			driver.findElement(By.xpath(".//input[@name='password']")).sendKeys(suiteData.getPassword());
			Logger.info("Login to CloudSoc in Progress");
			driver.findElement(By.xpath(".//button[@type='submit']")).click();
			Thread.sleep(20000);
			Logger.info("Logon to CloudSoc Successful");
		} catch(Exception e) {}
	}
	
	public void loginDropbox(WebDriver driver, TestSuiteDTO suiteData) throws InterruptedException{
		driver.get("https://www.dropbox.com/login");
		/*Thread.sleep(3000);
		driver.findElement(By.xpath(".//input[@name='email']")).sendKeys(suiteData.getUsername());
		driver.findElement(By.xpath(".//input[@name='password']")).sendKeys(suiteData.getPassword());
		driver.findElement(By.xpath(".//button[@type='submit']")).click();*/
		Thread.sleep(10000);
		Logger.info("Login to dropbox in progress");
		Logger.info(driver.getCurrentUrl());
		Logger.info("Enter Username: "+suiteData.getSaasAppUsername());
		driver.findElement(By.xpath(".//input[@class='text-input-input autofocus']")).sendKeys(suiteData.getSaasAppUsername());
		Logger.info("Enter Password: "+suiteData.getSaasAppPassword());
		driver.findElement(By.xpath(".//input[@class='password-input text-input-input']")).sendKeys(suiteData.getSaasAppPassword());
		Logger.info("Click login Button");
		driver.findElement(By.xpath(".//button[@class='login-button button-primary']")).click();
		Thread.sleep(5000);
		Logger.info("Login to dropbox successful");
	}
	
	public void createFolderDropbox(WebDriver driver, String folderName) throws InterruptedException{
		driver.findElement(By.xpath(".//a[@id='new_folder_button']")).click();
		Thread.sleep(3000);
		driver.findElement(By.xpath(".//input[@class='editor_field']")).sendKeys(folderName);
		Thread.sleep(3000);
		driver.findElement(By.xpath(".//input[@class='editor_field']")).sendKeys(Keys.TAB.toString());
		Thread.sleep(7000);
	}
	
	public void logoutDropbox(WebDriver driver) throws InterruptedException{
		driver.findElement(By.xpath(".//li[@id='header-account-menu']/span[@class='bubble-dropdown-container']")).click();
		Thread.sleep(3000);
		driver.findElement(By.xpath(".//a[text()='Sign out']")).click();
	}
}