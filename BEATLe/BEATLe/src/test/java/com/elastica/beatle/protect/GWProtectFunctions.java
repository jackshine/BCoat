package com.elastica.beatle.protect;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.entity.StringEntity;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.testng.Assert;
import org.testng.Reporter;

import com.elastica.beatle.CommonTest;
import com.elastica.beatle.TestSuiteDTO;
import com.elastica.beatle.RestClient.Client;
import com.elastica.beatle.RestClient.ClientUtil;
import com.elastica.beatle.gateway.GatewayTestConstants;
import com.elastica.beatle.securlets.ESQueryBuilder;

import net.sf.json.JSONArray;
import net.sf.json.util.JSONTokener;

public class GWProtectFunctions {

	ESQueryBuilder esQueryBuilder = new ESQueryBuilder();
	CommonTest commonTest = new CommonTest();
	
	
	public HttpResponse createAccessEnforcementPolicyGeneric(Client restClient, List<NameValuePair> requestHeader, TestSuiteDTO suiteData, Map<String, String > policyDataMap){
		HttpResponse response = null;
		String restAPI = null;
		StringEntity entity = null;
		URI uri = null;
		String entityTemplate;
		entityTemplate=createAccessEnforcementPolicyPayLoad(policyDataMap);
			restAPI = replaceGenericParams(suiteData.getAPIMap().get("createFileTransferPolicy"), suiteData);
			try{
			entity = new StringEntity(entityTemplate);	
			uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(),restAPI);
			System.out.println("Uri:"+uri);
			response = restClient.doPost(uri, requestHeader, null, entity);
		}catch(Exception e){
			e.printStackTrace();
		}
		return response;
	}
	
	
	
	public HttpResponse createAccessEnforcementPolicy(Client restClient, List<NameValuePair> requestHeader, TestSuiteDTO suiteData, Map<String, String > policyDataMap){
		HttpResponse response = null;
		String restAPI = null;
		StringEntity entity = null;
		//String Applications="__ALL_EL__";
		//String groups=\"__ALL_EL__\"
		URI uri = null;
		try{
			System.out.println("suiteData: "+suiteData);
			System.out.println("Header: "+requestHeader);
			
			
			String entityTemplate="{\"policy_name\":\"POLICY_NAME\",\"policy_description\":\"Policy Desc\","
					+ "\"applications\":[\"APPLICATIONS\"],"
					+ "\"users_scope\":{\"users\":[TARGET_USER],\"groups\":[TARGET_GROUP],\"domains\":[\"__ALL_EL__\"] },"
					+ "\"users_scope_whitelist\":{\"users\":[],\"groups\":[]},\"risk_level\":0,"
					+ "\"response\":{\"action\":[\"BLOCK_SHARE\",\"NOTIFY_USER\"],\"notify_user\":true,"
					+ "\"notify_email\":[NOTIFY_EMAILID],\"notify_sms\":[],\"notify_ticketing_email\":[]},\"violations\":1,"
					+ "\"geoip_scope\":[\"__ALL_EL__\"],\"geoip_whitelist\":[],"
					+ "\"platform\":[{\"item\":\"__ALL_EL__\"}],\"platform_whitelist\":[],"
					+ "\"browser\":[{\"item\":\"__ALL_EL__\"}],\"browser_whitelist\":[],"
					+ "\"activities_scope\":[{\"OBJECT_ACCESS\":[\"ACTIVITY_ACCESS\"]}],"
					+ "\"policy_type\":\"accessenforcement\",\"device_management_info\":{\"status\":\"__ALL_EL__\",\"posture\":\"__ALL_EL__\",\"ownership\":\"__ALL_EL__\"} ,\"is_active\":false}";
			
			
			entityTemplate=entityTemplate.replaceAll("POLICY_NAME", policyDataMap.get(GatewayTestConstants.POLICY_NAME));
			entityTemplate=entityTemplate.replaceAll("APPLICATIONS", policyDataMap.get(GatewayTestConstants.APPLICATIONS));
			
			if (policyDataMap.get(GatewayTestConstants.TARGET_USER) != null) {
				if (policyDataMap.get(GatewayTestConstants.TARGET_USER).isEmpty()) {
					entityTemplate=entityTemplate.replaceAll("TARGET_USER", "");
				} else {
					entityTemplate=entityTemplate.replaceAll("TARGET_USER", "\"" + policyDataMap.get(GatewayTestConstants.TARGET_USER) + "\"");
				}
			} else {
				entityTemplate=entityTemplate.replaceAll("TARGET_USER", "");
			}
			
			if (policyDataMap.get(GatewayTestConstants.TARGET_GROUP) != null) {
				if (policyDataMap.get(GatewayTestConstants.TARGET_GROUP).isEmpty()) {
					entityTemplate=entityTemplate.replaceAll("TARGET_GROUP", "");
				} else {
					entityTemplate=entityTemplate.replaceAll("TARGET_GROUP", "\"" + policyDataMap.get(GatewayTestConstants.TARGET_GROUP) + "\"");
				}
			} else {
				entityTemplate=entityTemplate.replaceAll("TARGET_GROUP", "");
			}
			
			if (policyDataMap.get(GatewayTestConstants.NOTIFY_EMAILID) != null) {
				if (policyDataMap.get(GatewayTestConstants.NOTIFY_EMAILID).isEmpty()) {
					entityTemplate=entityTemplate.replaceAll("NOTIFY_EMAILID", "");
				} else {
					entityTemplate=entityTemplate.replaceAll("NOTIFY_EMAILID", "\"" + policyDataMap.get(GatewayTestConstants.NOTIFY_EMAILID) + "\"");
				}
			} else {
				entityTemplate=entityTemplate.replaceAll("NOTIFY_EMAILID", "");
			}
			
			entityTemplate=entityTemplate.replaceAll("OBJECT_ACCESS", policyDataMap.get(GatewayTestConstants.OBJECT_ACCESS));
			entityTemplate=entityTemplate.replaceAll("ACTIVITY_ACCESS", policyDataMap.get(GatewayTestConstants.ACTIVITY_ACCESS));
			
			Reporter.log("entityTemplate "+entityTemplate, true);
			Reporter.log("Uri: "+suiteData.getAPIMap().get("createFileTransferPolicy"), true);
			restAPI = replaceGenericParams(suiteData.getAPIMap().get("createFileTransferPolicy"), suiteData);
			entity = new StringEntity(entityTemplate);	
			uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(),restAPI);
			response = restClient.doPost(uri, requestHeader, null, entity);
			String policyCreationResponseBody = ClientUtil.getResponseBody(response);
			Reporter.log("Policy Creation Reponse : " + policyCreationResponseBody, true);
			
			String policyActiveStatus = ClientUtil.getJSONValue(policyCreationResponseBody, ProtectTestConstants.ACTION_STATUS);
			policyActiveStatus = policyActiveStatus.substring(1, policyActiveStatus.length() - 1);
			Assert.assertEquals(policyActiveStatus, ProtectTestConstants.SUCCESS, "Policy Creation Failed!!.");
		}catch(Exception e){
			e.printStackTrace();
		}
		return response;
	}
	
	
	public HttpResponse createAccessEnforcementPolicyPlatform(Client restClient, List<NameValuePair> requestHeader, TestSuiteDTO suiteData, Map<String, String > policyDataMap){
		HttpResponse response = null;
		String restAPI = null;
		StringEntity entity = null;
		//String Applications="__ALL_EL__";
		//String groups=\"__ALL_EL__\"
		URI uri = null;
		try{
			System.out.println("suiteData: "+suiteData);
			System.out.println("Header: "+requestHeader);
			String entityTemplate="{\"policy_name\":\"POLICY_NAME\",\"policy_description\":\"Policy Desc\","
					+ "\"applications\":[\"APPLICATIONS\"],"
					+ "\"users_scope\":{\"users\":[\"TARGET_USER\"],\"groups\":[],\"domains\":[\"__ALL_EL__\"]},"
					+ "\"users_scope_whitelist\":{\"users\":[],\"groups\":[]},\"risk_level\":0,"
					+ "\"response\":{\"action\":[\"BLOCK_SHARE\",\"NOTIFY_USER\"],\"notify_user\":true,"
					+ "\"notify_email\":[\"NOTIFY_EMAILID\"],\"notify_sms\":[],\"notify_ticketing_email\":[]},\"violations\":1,"
					+ "\"geoip_scope\":[\"__ALL_EL__\"],\"geoip_whitelist\":[],"
					+ "\"platform\":[{\"item\":\"PLATFORM_TYPE\",\"version\":\"__ALL_EL__\",\"condition\":\"\"}],\"platform_whitelist\":[],"
					+ "\"browser\":[{\"item\":\"__ALL_EL__\"}],\"browser_whitelist\":[],"
					+ "\"activities_scope\":[{\"OBJECT_ACCESS\":[\"ACTIVITY_ACCESS\"]}],"
					+ "\"policy_type\":\"accessenforcement\",\"device_management_info\":{\"status\":\"__ALL_EL__\",\"posture\":\"__ALL_EL__\",\"ownership\":\"__ALL_EL__\"} ,\"is_active\":false}";
			
			entityTemplate=entityTemplate.replaceAll("POLICY_NAME", policyDataMap.get(GatewayTestConstants.POLICY_NAME));
			entityTemplate=entityTemplate.replaceAll("APPLICATIONS", policyDataMap.get(GatewayTestConstants.APPLICATIONS));
			entityTemplate=entityTemplate.replaceAll("TARGET_USER", policyDataMap.get(GatewayTestConstants.TARGET_USER));
			entityTemplate=entityTemplate.replaceAll("NOTIFY_EMAILID", policyDataMap.get(GatewayTestConstants.NOTIFY_EMAILID));
			entityTemplate=entityTemplate.replaceAll("OBJECT_ACCESS", policyDataMap.get(GatewayTestConstants.OBJECT_ACCESS));
			entityTemplate=entityTemplate.replaceAll("ACTIVITY_ACCESS", policyDataMap.get(GatewayTestConstants.ACTIVITY_ACCESS));
			entityTemplate=entityTemplate.replaceAll("PLATFORM_TYPE", policyDataMap.get(GatewayTestConstants.PLATFORM_TYPE));
			
			
			
			System.out.println("Uri: "+suiteData.getAPIMap().get("createFileTransferPolicy"));
			System.out.println("entityTemplate "+entityTemplate);
			restAPI = replaceGenericParams(suiteData.getAPIMap().get("createFileTransferPolicy"), suiteData);
			//restAPI = replaceGenericParams("/controls/filexfer/add", suiteData);
			entity = new StringEntity(entityTemplate);	
			
			
			uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(),restAPI);
			System.out.println("Uri:"+uri);
			//uri = ClientUtil.BuidURI("https://perf.elastica-inc.com/controls/accessenforcement/add");
			
			//Older ver of Api
			//uri=ClientUtil.BuidURI("https://perf.elastica-inc.com/controls/accessenforcement/add");
			response = restClient.doPost(uri, requestHeader, null, entity);
		}catch(Exception e){
			e.printStackTrace();
		}
		return response;
	}
	
	public HttpResponse createAccessEnforcementPolicyBrowser(Client restClient, List<NameValuePair> requestHeader, TestSuiteDTO suiteData, Map<String, String > policyDataMap){
		HttpResponse response = null;
		String restAPI = null;
		StringEntity entity = null;
		//String Applications="__ALL_EL__";
		//String groups=\"__ALL_EL__\"
		URI uri = null;
		try{
			System.out.println("suiteData: "+suiteData);
			System.out.println("Header: "+requestHeader);
			String entityTemplate="{\"policy_name\":\"POLICY_NAME\",\"policy_description\":\"Policy Desc\","
					+ "\"applications\":[\"APPLICATIONS\"],"
					+ "\"users_scope\":{\"users\":[\"TARGET_USER\"],\"groups\":[],\"domains\":[\"__ALL_EL__\"]},"
					+ "\"users_scope_whitelist\":{\"users\":[],\"groups\":[]},\"risk_level\":0,"
					+ "\"response\":{\"action\":[\"BLOCK_SHARE\",\"NOTIFY_USER\"],\"notify_user\":true,"
					+ "\"notify_email\":[\"NOTIFY_EMAILID\"],\"notify_sms\":[],\"notify_ticketing_email\":[]},\"violations\":1,"
					+ "\"geoip_scope\":[\"__ALL_EL__\"],\"geoip_whitelist\":[],"
					+ "\"platform\":[{\"item\":\"PLATFORM_TYPE\",\"version\":\"__ALL_EL__\",\"condition\":\"\"}],\"platform_whitelist\":[],"
					+ "\"browser\":[{\"item\":\"BROWSER_TYPE\",\"version\":\"__ALL_EL__\",\"condition\":\"\"}],\"browser_whitelist\":[],"
					+ "\"activities_scope\":[{\"OBJECT_ACCESS\":[\"ACTIVITY_ACCESS\"]}],"
					+ "\"policy_type\":\"accessenforcement\",\"device_management_info\":{\"status\":\"__ALL_EL__\",\"posture\":\"__ALL_EL__\",\"ownership\":\"__ALL_EL__\"} ,\"is_active\":false}";
			
			entityTemplate=entityTemplate.replaceAll("POLICY_NAME", policyDataMap.get(GatewayTestConstants.POLICY_NAME));
			entityTemplate=entityTemplate.replaceAll("APPLICATIONS", policyDataMap.get(GatewayTestConstants.APPLICATIONS));
			entityTemplate=entityTemplate.replaceAll("TARGET_USER", policyDataMap.get(GatewayTestConstants.TARGET_USER));
			entityTemplate=entityTemplate.replaceAll("NOTIFY_EMAILID", policyDataMap.get(GatewayTestConstants.NOTIFY_EMAILID));
			entityTemplate=entityTemplate.replaceAll("OBJECT_ACCESS", policyDataMap.get(GatewayTestConstants.OBJECT_ACCESS));
			entityTemplate=entityTemplate.replaceAll("ACTIVITY_ACCESS", policyDataMap.get(GatewayTestConstants.ACTIVITY_ACCESS));
			entityTemplate=entityTemplate.replaceAll("PLATFORM_TYPE", policyDataMap.get(GatewayTestConstants.PLATFORM_TYPE));
			entityTemplate=entityTemplate.replaceAll("BROWSER_TYPE", policyDataMap.get(GatewayTestConstants.BROWSER_TYPE));
			
			
			
			System.out.println("Uri: "+suiteData.getAPIMap().get("createFileTransferPolicy"));
			System.out.println("entityTemplate "+entityTemplate);
			restAPI = replaceGenericParams(suiteData.getAPIMap().get("createFileTransferPolicy"), suiteData);
			//restAPI = replaceGenericParams("/controls/filexfer/add", suiteData);
			entity = new StringEntity(entityTemplate);	
			
			
			uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(),restAPI);
			System.out.println("Uri:"+uri);
			//uri = ClientUtil.BuidURI("https://perf.elastica-inc.com/controls/accessenforcement/add");
			
			//Older ver of Api
			//uri=ClientUtil.BuidURI("https://perf.elastica-inc.com/controls/accessenforcement/add");
			response = restClient.doPost(uri, requestHeader, null, entity);
		}catch(Exception e){
			e.printStackTrace();
		}
		return response;
	}
	
	
	
	
	
	public HttpResponse createAccessEnforcementPolicyNoBlock(Client restClient, List<NameValuePair> requestHeader, TestSuiteDTO suiteData, Map<String, String > policyDataMap){
		HttpResponse response = null;
		String restAPI = null;
		StringEntity entity = null;
		//String Applications="__ALL_EL__";
		//String groups=\"__ALL_EL__\"
		URI uri = null;
		try{
			System.out.println("suiteData: "+suiteData);
			System.out.println("Header: "+requestHeader);
			String entityTemplate="{\"policy_name\":\"POLICY_NAME\",\"policy_description\":\"Policy Desc\","
					+ "\"applications\":[\"APPLICATIONS\"],"
					+ "\"users_scope\":{\"users\":[\"TARGET_USER\"],\"groups\":[],\"domains\":[\"__ALL_EL__\"]},"
					+ "\"users_scope_whitelist\":{\"users\":[],\"groups\":[]},\"risk_level\":0,"
					+ "\"response\":{\"action\":[\"NOTIFY_USER\"],\"notify_user\":true,"
					+ "\"notify_email\":[\"NOTIFY_EMAILID\"],\"notify_sms\":[],\"notify_ticketing_email\":[]},\"violations\":1,"
					+ "\"geoip_scope\":[\"__ALL_EL__\"],\"geoip_whitelist\":[],"
					+ "\"platform\":[{\"item\":\"__ALL_EL__\"}],\"platform_whitelist\":[],"
					+ "\"browser\":[{\"item\":\"__ALL_EL__\"}],\"browser_whitelist\":[],"
					+ "\"activities_scope\":[{\"OBJECT_ACCESS\":[\"ACTIVITY_ACCESS\"]}],"
					+ "\"policy_type\":\"accessenforcement\",\"device_management_info\":{\"status\":\"__ALL_EL__\",\"posture\":\"__ALL_EL__\",\"ownership\":\"__ALL_EL__\"} ,\"is_active\":false}";
			
			entityTemplate=entityTemplate.replaceAll("POLICY_NAME", policyDataMap.get(GatewayTestConstants.POLICY_NAME));
			entityTemplate=entityTemplate.replaceAll("APPLICATIONS", policyDataMap.get(GatewayTestConstants.APPLICATIONS));
			entityTemplate=entityTemplate.replaceAll("TARGET_USER", policyDataMap.get(GatewayTestConstants.TARGET_USER));
			entityTemplate=entityTemplate.replaceAll("NOTIFY_EMAILID", policyDataMap.get(GatewayTestConstants.NOTIFY_EMAILID));
			entityTemplate=entityTemplate.replaceAll("OBJECT_ACCESS", policyDataMap.get(GatewayTestConstants.OBJECT_ACCESS));
			entityTemplate=entityTemplate.replaceAll("ACTIVITY_ACCESS", policyDataMap.get(GatewayTestConstants.ACTIVITY_ACCESS));

			System.out.println("Uri: "+suiteData.getAPIMap().get("createFileTransferPolicy"));
			System.out.println("entityTemplate "+entityTemplate);
			restAPI = replaceGenericParams(suiteData.getAPIMap().get("createFileTransferPolicy"), suiteData);
			//restAPI = replaceGenericParams("/controls/filexfer/add", suiteData);
			entity = new StringEntity(entityTemplate);	
			uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(),restAPI);
			System.out.println("Uri:"+uri);
			response = restClient.doPost(uri, requestHeader, null, entity);
		}catch(Exception e){
			e.printStackTrace();
		}
		return response;
	}
	
	
	
	/**
	 * Create File Transfer
	 * @param restClient
	 * @param policyName
	 * @param ciqFileName
	 * @return
	 */
	public HttpResponse createFileTransferPolicy(Client restClient, List<NameValuePair> requestHeader, TestSuiteDTO suiteData, Map<String, String > policyDataMap){
		HttpResponse response = null;
		String restAPI = null;
		StringEntity entity = null;
		URI uri = null;
		try{
			System.out.println("suiteData: "+suiteData);
			System.out.println("Header: "+requestHeader);
			String entityTemplate="{\"policy_name\":\"POLICY_NAME\",\"policy_description\":\"File Transfer\",\"applications\":[\"APPLICATIONS\"],"
					+ "\"users_scope\":{\"users\":[TARGET_USER],\"groups\":[TARGET_GROUP],\"domains\":[\"__ALL_EL__\"]},\"users_scope_whitelist\":{\"users\":[],\"groups\":[]},"
					+ "\"risk_level\":0,\"response\":{\"action\":[\"BLOCK_SHARE\",\"NOTIFY_USER\",\"NOTIFY_EMAIL\"],\"notify_user\":true,"
					+ "\"notify_email\":[NOTIFY_EMAILID],\"notify_sms\":[],\"notify_ticketing_email\":[]},\"violations\":1,"
					+ "\"transfer_type\":[\"TRANSFER_TYPE\"],\"geoip_scope\":[\"__ALL_EL__\"],\"geoip_whitelist\":[],"
					+ "\"vulnerability_type\":[],"
					+ "\"content_profiles\":[CONTENT_IQ],\"filename_pattern\":[FILE_NAME],\"file_scope_types\":[FILE_TYPE],"
							+ "\"file_scope_whitelist\":[],\"policy_type\":\"filexfer\",\"device_management_info\":{\"status\":\"__ALL_EL__\",\"posture\":\"__ALL_EL__\",\"ownership\":\"__ALL_EL__\"}  ,\"is_active\":false}";
			
			entityTemplate=entityTemplate.replaceAll("POLICY_NAME", policyDataMap.get(GatewayTestConstants.POLICY_NAME));
			entityTemplate=entityTemplate.replaceAll("APPLICATIONS", policyDataMap.get(GatewayTestConstants.APPLICATIONS));
			
			if (policyDataMap.get(GatewayTestConstants.TARGET_USER) != null) {
				if (policyDataMap.get(GatewayTestConstants.TARGET_USER).isEmpty()) {
					entityTemplate=entityTemplate.replaceAll("TARGET_USER", "");
				} else {
					entityTemplate=entityTemplate.replaceAll("TARGET_USER", "\"" + policyDataMap.get(GatewayTestConstants.TARGET_USER) + "\"");
				}
			} else {
				entityTemplate=entityTemplate.replaceAll("TARGET_USER", "");
			}
			
			if (policyDataMap.get(GatewayTestConstants.TARGET_GROUP) != null) {
				if (policyDataMap.get(GatewayTestConstants.TARGET_GROUP).isEmpty()) {
					entityTemplate=entityTemplate.replaceAll("TARGET_GROUP", "");
				} else {
					entityTemplate=entityTemplate.replaceAll("TARGET_GROUP", "\"" + policyDataMap.get(GatewayTestConstants.TARGET_GROUP) + "\"");
				}
			} else {
				entityTemplate=entityTemplate.replaceAll("TARGET_GROUP", "");
			}
			
			if (policyDataMap.get(GatewayTestConstants.NOTIFY_EMAILID) != null) {
				if (policyDataMap.get(GatewayTestConstants.NOTIFY_EMAILID).isEmpty()) {
					entityTemplate=entityTemplate.replaceAll("NOTIFY_EMAILID", "");
				} else {
					entityTemplate=entityTemplate.replaceAll("NOTIFY_EMAILID", "\"" + policyDataMap.get(GatewayTestConstants.NOTIFY_EMAILID) + "\"");
				}
			} else {
				entityTemplate=entityTemplate.replaceAll("NOTIFY_EMAILID", "");
			}
			
			entityTemplate=entityTemplate.replaceAll("TRANSFER_TYPE", policyDataMap.get(GatewayTestConstants.TRANSFER_TYPE));
			
			if (policyDataMap.get(GatewayTestConstants.CONTENT_IQ) != null) {
				if (policyDataMap.get(GatewayTestConstants.CONTENT_IQ).isEmpty()) {
					entityTemplate=entityTemplate.replaceAll("CONTENT_IQ", "");
				} else {
					entityTemplate=entityTemplate.replaceAll("CONTENT_IQ", "\"" + policyDataMap.get(GatewayTestConstants.CONTENT_IQ) + "\"");
				}
			} else {
				entityTemplate=entityTemplate.replaceAll("CONTENT_IQ", "");
			}
			
			if (policyDataMap.get(GatewayTestConstants.FILE_NAME) != null) {
				if (policyDataMap.get(GatewayTestConstants.FILE_NAME).isEmpty() ||
						policyDataMap.get(GatewayTestConstants.FILE_NAME).equalsIgnoreCase("ANY")) {
					entityTemplate=entityTemplate.replaceAll("FILE_NAME", "");
				} else {
					entityTemplate=entityTemplate.replaceAll("FILE_NAME", "\"" + policyDataMap.get(GatewayTestConstants.FILE_NAME) + "\"");
				}
			} else {
				entityTemplate=entityTemplate.replaceAll("FILE_NAME", "");
			}
			
			if (policyDataMap.get(GatewayTestConstants.FILE_TYPE) != null) {
				if (policyDataMap.get(GatewayTestConstants.FILE_TYPE).isEmpty()) {
					entityTemplate=entityTemplate.replaceAll("FILE_TYPE", "\"__ALL_EL__\"");
				} else {
					entityTemplate=entityTemplate.replaceAll("FILE_TYPE", "\"" + policyDataMap.get(GatewayTestConstants.FILE_TYPE) + "\"");
				}
			} else {
				entityTemplate=entityTemplate.replaceAll("FILE_TYPE", "\"__ALL_EL__\"");
			}
			
			Reporter.log("entityTemplate "+entityTemplate, true);
			restAPI = replaceGenericParams(suiteData.getAPIMap().get("createFileTransferPolicy"), suiteData);
			entity = new StringEntity(entityTemplate);	
			uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(),restAPI);
			Reporter.log("Policyurl: "+uri, true);
			response = restClient.doPost(uri, requestHeader, null, entity);
			String policyCreationResponseBody = ClientUtil.getResponseBody(response);
			Reporter.log("Policy Creation Reponse : " + policyCreationResponseBody, true);
			
			String policyActiveStatus = ClientUtil.getJSONValue(policyCreationResponseBody, ProtectTestConstants.ACTION_STATUS);
			policyActiveStatus = policyActiveStatus.substring(1, policyActiveStatus.length() - 1);
			Assert.assertEquals(policyActiveStatus, ProtectTestConstants.SUCCESS, "Policy Creation Failed!!.");
		}catch(Exception e){
			e.printStackTrace();
		}
		return response;
	}
	
	
	public HttpResponse createFileTransferPolicyWithFileSize(Client restClient, List<NameValuePair> requestHeader, TestSuiteDTO suiteData, Map<String, String > policyDataMap){
		HttpResponse response = null;
		String restAPI = null;
		StringEntity entity = null;
		//String Applications="__ALL_EL__";
		//String groups=\"__ALL_EL__\"
		URI uri = null;
		try{
			System.out.println("suiteData: "+suiteData);
			System.out.println("Header: "+requestHeader);
			String entityTemplate="{\"policy_name\":\"POLICY_NAME\",\"policy_description\":\"File Transfer\",\"applications\":[\"APPLICATIONS\"],"
					+ "\"users_scope\":{\"users\":[\"TARGET_USER\"],\"groups\":[],\"domains\":[\"__ALL_EL__\"]},\"users_scope_whitelist\":{\"users\":[],\"groups\":[]},"
					+ "\"risk_level\":0,\"response\":{\"action\":[\"BLOCK_SHARE\",\"NOTIFY_USER\",\"NOTIFY_EMAIL\"],\"notify_user\":true,"
					+ "\"notify_email\":[\"NOTIFY_EMAILID\"],\"notify_sms\":[],\"notify_ticketing_email\":[]},\"violations\":1,"
					+ "\"transfer_type\":[\"TRANSFER_TYPE\"],\"file_size\":{\"larger_than\":LARGER_THAN,\"smaller_than\":SMALLER_THAN},\"geoip_scope\":[\"__ALL_EL__\"],\"geoip_whitelist\":[],"
					+ "\"vulnerability_type\":[],"
					+ "\"content_profiles\":[],\"filename_pattern\":[FILE_NAME],\"file_scope_types\":[\"FILE_TYPE\"],"
							+ "\"file_scope_whitelist\":[],\"policy_type\":\"filexfer\",\"device_management_info\":{\"status\":\"__ALL_EL__\",\"posture\":\"__ALL_EL__\",\"ownership\":\"__ALL_EL__\"} ,\"is_active\":false}";
			
			int smaller_Than=Integer.parseInt(policyDataMap.get(GatewayTestConstants.SMALLER_THAN));
			int larger_Than=Integer.parseInt(policyDataMap.get(GatewayTestConstants.LARGER_THAN));
			
			long smaller_Than_In_Byt=smaller_Than*1048576;
			long larger_Than_In_Byt=larger_Than*1048576;
					
					
			
			
			
			
			entityTemplate=entityTemplate.replaceAll("POLICY_NAME", policyDataMap.get(GatewayTestConstants.POLICY_NAME));
			entityTemplate=entityTemplate.replaceAll("APPLICATIONS", policyDataMap.get(GatewayTestConstants.APPLICATIONS));
			entityTemplate=entityTemplate.replaceAll("TARGET_USER", policyDataMap.get(GatewayTestConstants.TARGET_USER));
			entityTemplate=entityTemplate.replaceAll("NOTIFY_EMAILID", policyDataMap.get(GatewayTestConstants.NOTIFY_EMAILID));
			entityTemplate=entityTemplate.replaceAll("TRANSFER_TYPE", policyDataMap.get(GatewayTestConstants.TRANSFER_TYPE));
			entityTemplate=entityTemplate.replaceAll("LARGER_THAN", Long.toString(larger_Than_In_Byt));
			entityTemplate=entityTemplate.replaceAll("SMALLER_THAN", Long.toString(smaller_Than_In_Byt));
			if (policyDataMap.get(GatewayTestConstants.FILE_NAME).equalsIgnoreCase("ANY")){
				entityTemplate=entityTemplate.replaceAll("FILE_NAME", "");
			}
			else {
				entityTemplate=entityTemplate.replaceAll("FILE_NAME", "\""+policyDataMap.get(GatewayTestConstants.FILE_NAME)+"\"");
			}
			entityTemplate=entityTemplate.replaceAll("FILE_TYPE", policyDataMap.get(GatewayTestConstants.FILE_TYPE));
			
			System.out.println("entityTemplate "+entityTemplate);
			restAPI = replaceGenericParams(suiteData.getAPIMap().get("createFileTransferPolicy"), suiteData);
			//restAPI = replaceGenericParams("/controls/filexfer/add", suiteData);
			entity = new StringEntity(entityTemplate);	
			
			
			uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(),restAPI);
			System.out.println("Policyurl: "+uri);
			response = restClient.doPost(uri, requestHeader, null, entity);
		}catch(Exception e){
			e.printStackTrace();
		}
		return response;
	}
	
	public HttpResponse createFileTransferPolicyWithRisk(Client restClient, List<NameValuePair> requestHeader, TestSuiteDTO suiteData, Map<String, String > policyDataMap){
		HttpResponse response = null;
		String restAPI = null;
		StringEntity entity = null;
		//String Applications="__ALL_EL__";
		//String groups=\"__ALL_EL__\"
		
		URI uri = null;
		try{
			System.out.println("suiteData: "+suiteData);
			System.out.println("Header: "+requestHeader);
			String entityTemplate="{\"policy_name\":\"POLICY_NAME\",\"policy_description\":\"File Transfer\",\"applications\":[\"APPLICATIONS\"],"
					+ "\"users_scope\":{\"users\":[\"TARGET_USER\"],\"groups\":[],\"domains\":[\"__ALL_EL__\"]},\"users_scope_whitelist\":{\"users\":[],\"groups\":[]},"
					+ "\"risk_level\":0,\"response\":{\"action\":[\"NOTIFY_USER\",\"NOTIFY_EMAIL\"],\"notify_user\":true,"
					+ "\"notify_email\":[\"NOTIFY_EMAILID\"],\"notify_sms\":[],\"notify_ticketing_email\":[]},\"violations\":1,"
					+ "\"transfer_type\":[\"TRANSFER_TYPE\"],\"geoip_scope\":[\"__ALL_EL__\"],\"geoip_whitelist\":[],"
					+ "\"vulnerability_type\":[{\"type\":\"VULNERABILITY_TYPE\",\"meta_info\":{}}],"
					+ "\"content_profiles\":[CONTENT_IQ],\"filename_pattern\":[FILE_NAME],\"file_scope_types\":[\"FILE_TYPE\"],"
							+ "\"file_scope_whitelist\":[],\"policy_type\":\"filexfer\",\"device_management_info\":{\"status\":\"__ALL_EL__\",\"posture\":\"__ALL_EL__\",\"ownership\":\"__ALL_EL__\"} ,\"is_active\":false}";
			
			entityTemplate=entityTemplate.replaceAll("POLICY_NAME", policyDataMap.get(GatewayTestConstants.POLICY_NAME));
			entityTemplate=entityTemplate.replaceAll("APPLICATIONS", policyDataMap.get(GatewayTestConstants.APPLICATIONS));
			entityTemplate=entityTemplate.replaceAll("TARGET_USER", policyDataMap.get(GatewayTestConstants.TARGET_USER));
			entityTemplate=entityTemplate.replaceAll("NOTIFY_EMAILID", policyDataMap.get(GatewayTestConstants.NOTIFY_EMAILID));
			entityTemplate=entityTemplate.replaceAll("TRANSFER_TYPE", policyDataMap.get(GatewayTestConstants.TRANSFER_TYPE));
			if (policyDataMap.get(GatewayTestConstants.FILE_NAME).equalsIgnoreCase("ANY")){
				entityTemplate=entityTemplate.replaceAll("FILE_NAME", "");
			}
			else {
				entityTemplate=entityTemplate.replaceAll("FILE_NAME", "\""+policyDataMap.get(GatewayTestConstants.FILE_NAME)+"\"");
			}
			entityTemplate=entityTemplate.replaceAll("FILE_TYPE", policyDataMap.get(GatewayTestConstants.FILE_TYPE));
			//entityTemplate=entityTemplate.replaceAll("FILE_NAME", policyDataMap.get(GatewayTestConstants.FILE_NAME));
			
			entityTemplate=entityTemplate.replaceAll("VULNERABILITY_TYPE", policyDataMap.get(GatewayTestConstants.VULNERABILITY_TYPE));
			if (policyDataMap.get(GatewayTestConstants.CONTENT_IQ)!=null)
			entityTemplate=entityTemplate.replaceAll("CONTENT_IQ", "\""+policyDataMap.get(GatewayTestConstants.CONTENT_IQ)+"\"");
			else entityTemplate=entityTemplate.replaceAll("CONTENT_IQ", "");
			System.out.println("entityTemplate "+entityTemplate);
			restAPI = replaceGenericParams(suiteData.getAPIMap().get("createFileTransferPolicy"), suiteData);
			//restAPI = replaceGenericParams("/controls/filexfer/add", suiteData);
			entity = new StringEntity(entityTemplate);	
			
			
			uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(),restAPI);
			System.out.println("Policyurl: "+uri);
			response = restClient.doPost(uri, requestHeader, null, entity);
		}catch(Exception e){
			e.printStackTrace();
		}
		return response;
	}
	
	
	/**
	 * Create File Sharing
	 * @param restClient
	 * @param policyName
	 * @param ciqFileName
	 * @return
	 */
	public HttpResponse createFileSharingPolicy(Client restClient, List<NameValuePair> requestHeader, TestSuiteDTO suiteData, Map<String, String > policyDataMap){
		HttpResponse response = null;
		String restAPI = null;
		StringEntity entity = null;
		URI uri = null;
		
		//{"policy_name":"Tst sharing","policy_description":"Ths is","applications":["Box"],"users_scope":{"users":["user1@gatewaybeatle.com"],"groups":[]},"users_scope_whitelist":{"users":[],"groups":[]},"risk_level":0,"response":{"action":["BLOCK_SHARE","NOTIFY_USER"],"notify_user":true,"notify_email":[],"notify_sms":[],"notify_ticketing_email":[]},"violations":1,"recipient_scope":{"users":["admin@gatewaybeatle.com"],"groups":[]},"recipient_scope_whitelist":{"users":[],"groups":[]},"geoip_scope":["__ALL_EL__"],"geoip_whitelist":[],"vulnerability_type":[],"content_profiles":[],"filename_pattern":["Test"],"file_scope_types":["pdf"],"file_scope_whitelist":[],"policy_type":"documentshare","is_active":false}
		try {
			restAPI = replaceGenericParams(suiteData.getAPIMap().get("createFileSharingPolicy"), suiteData);
			String entityTemplate= "{\"policy_name\":\"POLICY_NAME\",\"policy_description\":\"File Sharing\",\"applications\":[\"APPLICATIONS\"],"
					+ "\"users_scope\":{\"users\":[SHARED_BY_USER],\"groups\":[SHARED_BY_GROUP],\"domains\":[\"__ALL_EL__\"]},\"users_scope_whitelist\":{\"users\":[],\"groups\":[]},"
					+ "\"risk_level\":0,\"response\":{\"action\":[BLOCK_SHARE \"NOTIFY_USER\",\"SEVERITY_LEVEL\"],\"notify_user\":true,"
					+ "\"notify_email\":[NOTIFY_EMAILID],\"notify_sms\":[],\"notify_ticketing_email\":[], \"severity_level\":\"medium\"}, \"violations\":1,"
					+ "\"recipient_scope\":{\"users\":[SHARE_WITH_USER],\"groups\":[SHARE_WITH_GROUP]},\"recipient_scope_whitelist\":{\"users\":[],\"groups\":[]},"
					+ "\"geoip_scope\":[\"__ALL_EL__\"],\"geoip_whitelist\":[],\"vulnerability_type\":[],\"content_profiles\":[],\"filename_pattern\":[FILE_NAME],"
							+ "\"file_scope_types\":[FILE_TYPE],\"file_scope_whitelist\":[],\"policy_type\":\"documentshare\",\"device_management_info\":{\"status\":\"__ALL_EL__\",\"posture\":\"__ALL_EL__\",\"ownership\":\"__ALL_EL__\"} ,\"is_active\":false}";
	
			entityTemplate=entityTemplate.replaceAll("POLICY_NAME", policyDataMap.get(GatewayTestConstants.POLICY_NAME));
			entityTemplate=entityTemplate.replaceAll("APPLICATIONS", policyDataMap.get(GatewayTestConstants.APPLICATIONS));
			if (policyDataMap.get(GatewayTestConstants.SHARED_BY) != null) {
				if (policyDataMap.get(GatewayTestConstants.SHARED_BY).isEmpty()) {
					entityTemplate=entityTemplate.replaceAll("SHARED_BY_USER", "");
				} else {
					entityTemplate=entityTemplate.replaceAll("SHARED_BY_USER", "\"" + policyDataMap.get(GatewayTestConstants.SHARED_BY) + "\"");
				}
			} else {
				entityTemplate=entityTemplate.replaceAll("SHARED_BY_USER", "");
			}
			if (policyDataMap.get(GatewayTestConstants.SHARED_BY_GROUP) != null) {
				if (policyDataMap.get(GatewayTestConstants.SHARED_BY_GROUP).isEmpty()) {
					entityTemplate=entityTemplate.replaceAll("SHARED_BY_GROUP", "");
				} else {
					entityTemplate=entityTemplate.replaceAll("SHARED_BY_GROUP", "\"" + policyDataMap.get(GatewayTestConstants.SHARED_BY_GROUP) + "\"");
				}
			} else {
				entityTemplate=entityTemplate.replaceAll("SHARED_BY_GROUP", "");
			}
			if (policyDataMap.get(GatewayTestConstants.BLOCK_SHARE) != null) {
				if (policyDataMap.get(GatewayTestConstants.BLOCK_SHARE).equalsIgnoreCase("BLOCK_SHARE")) {
					entityTemplate=entityTemplate.replaceAll("BLOCK_SHARE", "\"" + policyDataMap.get(GatewayTestConstants.BLOCK_SHARE) + "\",");
				} else {
					entityTemplate=entityTemplate.replaceAll("BLOCK_SHARE", "");
				}
			} else {
				entityTemplate=entityTemplate.replaceAll("BLOCK_SHARE", "\"BLOCK_SHARE\",");
			}
			
			if (policyDataMap.get(GatewayTestConstants.NOTIFY_EMAILID) != null) {
				if (policyDataMap.get(GatewayTestConstants.NOTIFY_EMAILID).isEmpty()) {
					entityTemplate=entityTemplate.replaceAll("NOTIFY_EMAILID", "");
				} else {
					entityTemplate=entityTemplate.replaceAll("NOTIFY_EMAILID", "\"" + policyDataMap.get(GatewayTestConstants.NOTIFY_EMAILID) + "\"");
				}
			} else {
				entityTemplate=entityTemplate.replaceAll("NOTIFY_EMAILID", "");
			}
			
//			if (policyDataMap.get(GatewayTestConstants.SHARE_WITH_GROUP) == null && policyDataMap.get(GatewayTestConstants.SHARE_WITH) == null) {
//				entityTemplate=entityTemplate.replaceAll("SHARE_WITH_GROUP", "\"__ALL_EL__\"");
//			}
			if (policyDataMap.get(GatewayTestConstants.SHARE_WITH) != null) {
				if (policyDataMap.get(GatewayTestConstants.SHARE_WITH).isEmpty()) {
					entityTemplate=entityTemplate.replaceAll("SHARE_WITH_USER", "\"__ALL_EL__\"");
				} else {
					entityTemplate=entityTemplate.replaceAll("SHARE_WITH_USER", "\"" + policyDataMap.get(GatewayTestConstants.SHARE_WITH) + "\"");
				}
			} else {
				entityTemplate=entityTemplate.replaceAll("SHARE_WITH_USER", "\"__ALL_EL__\"");
			}
			if (policyDataMap.get(GatewayTestConstants.SHARE_WITH_GROUP) != null) {
				if (policyDataMap.get(GatewayTestConstants.SHARE_WITH_GROUP).isEmpty()) {
					entityTemplate=entityTemplate.replaceAll("SHARE_WITH_GROUP", "\"__ALL_EL__\"");
				} else {
					entityTemplate=entityTemplate.replaceAll("SHARE_WITH_GROUP", "\"" + policyDataMap.get(GatewayTestConstants.SHARE_WITH_GROUP) + "\"");
				}
			} else {
				entityTemplate=entityTemplate.replaceAll("SHARE_WITH_GROUP", "\"__ALL_EL__\"");
			}
			
			if (policyDataMap.get(GatewayTestConstants.FILE_NAME) != null) {
				if (policyDataMap.get(GatewayTestConstants.FILE_NAME).equalsIgnoreCase("ANY") || 
						policyDataMap.get(GatewayTestConstants.FILE_NAME).isEmpty()){
					entityTemplate=entityTemplate.replaceAll("FILE_NAME", "");
				} else {
					entityTemplate=entityTemplate.replaceAll("FILE_NAME", "\""+policyDataMap.get(GatewayTestConstants.FILE_NAME)+"\"");
				}
			} else {
				entityTemplate=entityTemplate.replaceAll("FILE_NAME", "");
			}
			if (policyDataMap.get(GatewayTestConstants.FILE_TYPE) != null) {
				if (policyDataMap.get(GatewayTestConstants.FILE_TYPE).isEmpty()){
					entityTemplate=entityTemplate.replaceAll("FILE_TYPE", "\"__ALL_EL__\"");
				} else {
					entityTemplate=entityTemplate.replaceAll("FILE_TYPE", "\""+policyDataMap.get(GatewayTestConstants.FILE_TYPE)+"\"");
				}
			} else {
				entityTemplate=entityTemplate.replaceAll("FILE_TYPE", "\"__ALL_EL__\"");
			}
			
			Reporter.log("entityTemplate "+entityTemplate, true);
			entity = new StringEntity(entityTemplate);	
			
			uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(),restAPI);
			response = restClient.doPost(uri, requestHeader, null, entity);
			String policyCreationResponseBody = ClientUtil.getResponseBody(response);
			Reporter.log("Policy Creation Reponse : " + policyCreationResponseBody, true);
			
			String policyActiveStatus = ClientUtil.getJSONValue(policyCreationResponseBody, ProtectTestConstants.ACTION_STATUS);
			policyActiveStatus = policyActiveStatus.substring(1, policyActiveStatus.length() - 1);
			Assert.assertEquals(policyActiveStatus, ProtectTestConstants.SUCCESS, "Policy Creation Failed!!.");
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return response;
	}
	
	/**
	 * Create Threat Score Based Policy
	 * @param restClient
	 * @param policyName
	 * @param ciqFileName
	 * @return
	 */
	public HttpResponse createThreatScoreBasedPolicy(Client restClient, String policyName, String saasApp, List<NameValuePair> requestHeader, TestSuiteDTO suiteData){
		HttpResponse response = null;
		String restAPI = null;
		StringEntity entity = null;
		URI uri = null;
		try{
			restAPI = replaceGenericParams(suiteData.getAPIMap().get("createPolicy"), suiteData);
		//	entity = new StringEntity("{\"policy_name\":\""+ policyName + "\",\"policy_description\":\"File Sharing\",\"applications\":[\"__ALL_EL__\"],\"users_scope\":{\"users\":[\"__ALL_EL__\"],\"groups\":[\"__ALL_EL__\"]},\"users_scope_whitelist\":{\"users\":[],\"groups\":[]},\"risk_level\":0,\"response\":{\"action\":[\"BLOCK_SHARE\",\"NOTIFY_USER\",\"NOTIFY_EMAIL\"],\"notify_user\":true,\"notify_email\":[\"qa-admin@elasticaqa.net\"],\"notify_sms\":[],\"notify_ticketing_email\":[]},\"violations\":1,\"recipient_scope\":{\"users\":[\"mayurbelekar@hotmail.com\"],\"groups\":[]},\"recipient_scope_whitelist\":{\"users\":[],\"groups\":[]},\"geoip_scope\":[\"__ALL_EL__\"],\"geoip_whitelist\":[],\"vulnerability_type\":[],\"content_profiles\":[],\"filename_pattern\":[\""+fileName+"\"],\"file_scope_types\":[\"txt\"],\"file_scope_whitelist\":[],\"policy_type\":\"documentshare\",\"is_active\":false}");
			entity = new StringEntity("{\"policy_name\":\""+policyName+"\",\"policy_description\":\"threat score policy\",\"applications\":[\""+saasApp+"\"],\"users_scope\":{\"users\":[\"__ALL_EL__\"],\"groups\":[\"__ALL_EL__\"]},\"users_scope_whitelist\":{\"users\":[],\"groups\":[]},\"risk_level\":0,\"response\":{\"action\":[\"BLOCK_SERVICE\"],\"notify_email\":[],\"notify_sms\":[],\"notify_ticketing_email\":[],\"notify_user\":false},\"violations\":1,\"policy_type\":\"anomalydetect\",\"is_active\":true}");
			uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(),restAPI);
			response = restClient.doPost(uri, requestHeader, null, entity);
		}catch(Exception e){
			e.printStackTrace();
		}
		return response;
	}
	
	/**
	 * @param url
	 * @return
	 */
	private String replaceGenericParams(String url, TestSuiteDTO suiteData){
		if(url.contains("tenantName"))
			url = url.replace("{tenantName}", suiteData.getTenantName());
		if(url.contains("version"))
			url = url.replace("{version}", suiteData.getBaseVersion());
		return url;
	}
	
	
	
	public JSONArray getAllPoliciesNamesList(Client restClient, List<NameValuePair> requestHeader, TestSuiteDTO suiteData) throws Exception{
		Map<String, String> policyDetails = new HashMap<String, String>();
		String restAPI = replaceGenericParams(suiteData.getAPIMap().get("policyList"), suiteData);
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(),restAPI);
		HttpResponse policyListResponse = restClient.doGet(dataUri, requestHeader);
		String policyListResponseBody = ClientUtil.getResponseBody(policyListResponse);
		String policiesList = getJSONValue(policyListResponseBody, "policies"); 
		JSONArray jArray = (JSONArray) new JSONTokener(policiesList).nextValue();
		/*for (int i = 0; i < jArray.size(); i++) {
			String name = getJSONValue(jArray.getJSONObject(i).toString(), "policy_name");
			name = name.substring(1, name.length()-1);
			System.out.println("Name of policies: "+name);
			if(name.equalsIgnoreCase(policyName)){
				String policyType = getJSONValue(jArray.getJSONObject(i).toString(), "policy_type");
				policyType = policyType.substring(1, policyType.length()-1);
				String policyId = getJSONValue(jArray.getJSONObject(i).toString(), "id");
				policyId = policyId.substring(1, policyId.length()-1);
				String policySubId = getJSONValue(jArray.getJSONObject(i).toString(), "sub_id");
				policySubId = policySubId.substring(1, policySubId.length()-1);
				String policyStatus = getJSONValue(jArray.getJSONObject(i).toString(), "is_active");
				policyStatus = policyStatus.substring(1, policyStatus.length()-1);
				policyDetails.put(ProtectTestConstants.POLICY_TYPE, policyType);
				policyDetails.put(ProtectTestConstants.POLICY_ID, policyId);
				policyDetails.put(ProtectTestConstants.POLICY_SUB_ID, policySubId);
				policyDetails.put(ProtectTestConstants.ACTION_STATUS, policyStatus);
				break;
			}
		}*/
		return jArray;
	}
	
	/**
	 * This method returns the Json Value of the object
	 * @param json
	 * @param key
	 * @return
	 * @throws JsonProcessingException
	 * @throws IOException
	 */
	public  String getJSONValue(String json, String key) throws JsonProcessingException, IOException {
		JsonFactory factory = new JsonFactory();
		ObjectMapper mapper = new ObjectMapper(factory);
		JsonNode rootNode = mapper.readTree(json);
		return rootNode.get(key).toString();

	}
	
	
	public String createAccessEnforcementPolicyPayLoad(Map<String, String > policyDataMap){
		
		String entityTemplate="{\"policy_name\":\"POLICY_NAME\",\"policy_description\":\"Policy Desc\","
				+ "\"applications\":[\"APPLICATIONS\"],"
				+ "\"users_scope\":{\"users\":[\"TARGET_USER\"],\"groups\":[GROUP],\"domains\":[\"__ALL_EL__\"]},"
				+ "\"users_scope_whitelist\":{\"users\":[USER_WLIST],\"groups\":[GRP_WLIST]},\"risk_level\":0,"
				+ "\"response\":{\"action\":[ACTIONS],\"notify_user\":true,"
				+ "\"notify_email\":[NOTIFY_EMAILID],\"notify_sms\":[],\"notify_ticketing_email\":[]},\"violations\":1,"
				+ "\"geoip_scope\":[\"GEOIP_SCOPE\"],\"geoip_whitelist\":[GEOIP_WLIST],"
				+ "\"platform\":[{\"item\":\"PLATFORM_LIST\"PLATFORM_VERSIONS}],\"platform_whitelist\":[PLATFORM_WLIST],"
				+ "\"browser\":[{\"item\":\"BROWSER_LIST\"BROWSER_VERSIONS}],\"browser_whitelist\":[BROWSER_WLIST],"
				+ "\"activities_scope\":[{\"OBJECT_ACCESS\":[\"ACTIVITY_ACCESS\"]}],\"account_type\":[\"ACOUNT_TYPE\"],"
				+ "\"policy_type\":\"accessenforcement\",\"device_management_info\":{\"status\":\"__ALL_EL__\",\"posture\":\"__ALL_EL__\",\"ownership\":\"__ALL_EL__\"} ,\"is_active\":false}";
		
		
		
		
		entityTemplate=entityTemplate.replaceAll("POLICY_NAME", policyDataMap.get(GatewayTestConstants.POLICY_NAME));
		entityTemplate=entityTemplate.replaceAll("APPLICATIONS", policyDataMap.get(GatewayTestConstants.APPLICATIONS));
		entityTemplate=entityTemplate.replaceAll("TARGET_USER", policyDataMap.get(GatewayTestConstants.TARGET_USER));
		entityTemplate=entityTemplate.replaceAll("OBJECT_ACCESS", policyDataMap.get(GatewayTestConstants.OBJECT_ACCESS));
		entityTemplate=entityTemplate.replaceAll("ACTIVITY_ACCESS", policyDataMap.get(GatewayTestConstants.ACTIVITY_ACCESS));
		
		
		
		//_________________________________________________
		if ((policyDataMap.get(GatewayTestConstants.GEOIP_SCOPE)==null)||(policyDataMap.get(GatewayTestConstants.GEOIP_SCOPE).equalsIgnoreCase("NONE"))){
			entityTemplate=entityTemplate.replaceAll("GEOIP_SCOPE", "__ALL_EL__");
		}
		else {
			entityTemplate=entityTemplate.replaceAll("GEOIP_SCOPE", "\""+policyDataMap.get(GatewayTestConstants.GEOIP_SCOPE)+"\"");
		}
		if ((policyDataMap.get(GatewayTestConstants.GEOIP_SCOPE_WLIST)==null)||(policyDataMap.get(GatewayTestConstants.GEOIP_SCOPE_WLIST).equalsIgnoreCase("NONE"))){
			entityTemplate=entityTemplate.replaceAll("GEOIP_WLIST", "");
		}
		else {
			entityTemplate=entityTemplate.replaceAll("GEOIP_WLIST", "\""+policyDataMap.get(GatewayTestConstants.GEOIP_SCOPE_WLIST)+"\"");
		}
		//_________________________________________________
		
		//_________________________________________________
		if ((policyDataMap.get(GatewayTestConstants.PLATFORM_LIST)==null)||(policyDataMap.get(GatewayTestConstants.PLATFORM_LIST).equalsIgnoreCase("NONE"))){
			entityTemplate=entityTemplate.replaceAll("PLATFORM_LIST", "__ALL_EL__");
			entityTemplate=entityTemplate.replaceAll("PLATFORM_VERSIONS", "");
		}
		else {
			entityTemplate=entityTemplate.replaceAll("PLATFORM_LIST", policyDataMap.get(GatewayTestConstants.PLATFORM_LIST));
			String version=",\"version\":\"__ALL_EL__\",\"condition\":\"\"";
			entityTemplate=entityTemplate.replaceAll("PLATFORM_VERSIONS", version);
			
		}
		if ((policyDataMap.get(GatewayTestConstants.PLATFORM_WLIST)==null)||(policyDataMap.get(GatewayTestConstants.PLATFORM_WLIST).equalsIgnoreCase("NONE"))){
			entityTemplate=entityTemplate.replaceAll("PLATFORM_WLIST", "");
		}
		else {
			entityTemplate=entityTemplate.replaceAll("PLATFORM_WLIST", "\""+policyDataMap.get(GatewayTestConstants.PLATFORM_WLIST)+"\"");
		}
		//_________________________________________________
		
		
		//_________________________________________________
		if ((policyDataMap.get(GatewayTestConstants.BROWSER_LIST)==null)||(policyDataMap.get(GatewayTestConstants.BROWSER_LIST).equalsIgnoreCase("NONE"))){
			entityTemplate=entityTemplate.replaceAll("BROWSER_LIST", "__ALL_EL__");
			entityTemplate=entityTemplate.replaceAll("BROWSER_VERSIONS", "");
		}
		else {
			entityTemplate=entityTemplate.replaceAll("BROWSER_LIST", policyDataMap.get(GatewayTestConstants.BROWSER_LIST));
			String version=",\"version\":\"__ALL_EL__\",\"condition\":\"\"";
			entityTemplate=entityTemplate.replaceAll("BROWSER_VERSIONS", version);
			
		}
		if ((policyDataMap.get(GatewayTestConstants.BROWSER_WLIST)==null)||(policyDataMap.get(GatewayTestConstants.BROWSER_WLIST).equalsIgnoreCase("NONE"))){
			entityTemplate=entityTemplate.replaceAll("BROWSER_WLIST", "");
		}
		else {
			entityTemplate=entityTemplate.replaceAll("BROWSER_WLIST", "\""+policyDataMap.get(GatewayTestConstants.BROWSER_WLIST)+"\"");
		}
		//_________________________________________________
		
		//_________________________________________________
		if (policyDataMap.get(GatewayTestConstants.ACOUNT_TYPE)!=null){
		entityTemplate=entityTemplate.replaceAll("ACOUNT_TYPE", policyDataMap.get(GatewayTestConstants.ACOUNT_TYPE));
		}
		else{
			entityTemplate=entityTemplate.replaceAll("ACOUNT_TYPE", "__ALL_EL__");
		}
		//_________________________________________________
		
		
		//_________________________________________________
		if ((policyDataMap.get(GatewayTestConstants.GROUP)==null)||(policyDataMap.get(GatewayTestConstants.GROUP).equalsIgnoreCase("NONE"))){
			entityTemplate=entityTemplate.replaceAll("GROUP", "");
		}
		else {
			entityTemplate=entityTemplate.replaceAll("GROUP", "\""+policyDataMap.get(GatewayTestConstants.GROUP)+"\"");
		}
		if ((policyDataMap.get(GatewayTestConstants.GROUP_WLIST)==null)||(policyDataMap.get(GatewayTestConstants.GROUP_WLIST).equalsIgnoreCase("NONE"))){
			entityTemplate=entityTemplate.replaceAll("GRP_WLIST", "");
		}
		else {
			entityTemplate=entityTemplate.replaceAll("GRP_WLIST", "\""+policyDataMap.get(GatewayTestConstants.GROUP_WLIST)+"\"");
		}
		//_________________________________________________
		
		//_________________________________________________
		if ((policyDataMap.get(GatewayTestConstants.USER_WLIST)==null)||(policyDataMap.get(GatewayTestConstants.USER_WLIST).equalsIgnoreCase("NONE"))){
			entityTemplate=entityTemplate.replaceAll("USER_WLIST", "");
		}
		else {
			entityTemplate=entityTemplate.replaceAll("USER_WLIST", "\""+policyDataMap.get(GatewayTestConstants.USER_WLIST)+"\"");
		}
		//_________________________________________________
		
		//"BLOCK_SHARE","NOTIFY_USER","NOTIFY_EMAIL"
		//_________________________________________________
		if ((policyDataMap.get(GatewayTestConstants.ACTIONS)==null)||(policyDataMap.get(GatewayTestConstants.ACTIONS).equalsIgnoreCase("NOBLOCK"))){
			entityTemplate=entityTemplate.replaceAll("ACTIONS", "");
		}
		else if (policyDataMap.get(GatewayTestConstants.ACTIONS).equalsIgnoreCase("BLOCK_SHARE")){
			entityTemplate=entityTemplate.replaceAll("ACTIONS", "\"BLOCK_SHARE\"");
		}
		else if (policyDataMap.get(GatewayTestConstants.ACTIONS).equalsIgnoreCase("NOTIFY_EMAIL")){
			entityTemplate=entityTemplate.replaceAll("ACTIONS", "\"NOTIFY_EMAIL\"");
		}
		else if (policyDataMap.get(GatewayTestConstants.ACTIONS).equalsIgnoreCase("NOTIFY_USER")){
			entityTemplate=entityTemplate.replaceAll("ACTIONS", "\"NOTIFY_USER\"");
		}
		
		else if (policyDataMap.get(GatewayTestConstants.ACTIONS).equalsIgnoreCase("BLOCK_SHARE_NOTIFY_EMAIL")){
			entityTemplate=entityTemplate.replaceAll("ACTIONS", "\"BLOCK_SHARE\",\"NOTIFY_EMAIL\"");
		}
		
		else if (policyDataMap.get(GatewayTestConstants.ACTIONS).equalsIgnoreCase("BLOCK_SHARE_NOTIFY_EMAIL_NOTIFY_USER")){
			entityTemplate=entityTemplate.replaceAll("ACTIONS", "\"BLOCK_SHARE\",\"NOTIFY_USER\",\"NOTIFY_USER\"");
		}
		//_________________________________________________
		
		//_________________________________________________
		if ((policyDataMap.get(GatewayTestConstants.NOTIFY_EMAILID)==null)||(policyDataMap.get(GatewayTestConstants.NOTIFY_EMAILID).equalsIgnoreCase("NONE"))){
			entityTemplate=entityTemplate.replaceAll("NOTIFY_EMAILID", "");
		}
		else {
			entityTemplate=entityTemplate.replaceAll("NOTIFY_EMAILID","\""+policyDataMap.get(GatewayTestConstants.NOTIFY_EMAILID)+"\"" );
		}
		//_________________________________________________
		
		System.out.println(entityTemplate);
	return entityTemplate;
	
}

	
}
