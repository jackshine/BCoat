/**
 * 
 */
package com.elastica.gateway;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import net.sf.json.JSONArray;
import net.sf.json.util.JSONTokener;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.codehaus.jackson.JsonProcessingException;
import org.testng.Assert;
import org.testng.Reporter;

import com.elastica.common.SuiteData;
import com.elastica.logger.Logger;
import com.google.common.io.Files;

import com.elastica.restClient.Client;
import com.elastica.restClient.ClientUtil;

/**
 * @author shri
 *
 */
public class ProtectFunctions{

	public enum ObjectAccessed{
		FILE,
		FOLDER,
		GROUP,
		ROLE,
		SESSION,
		UNKNOWNDEVICE,
		USER
	}
	
	public enum FileOperations{
		COPY,
		DELETE,
		DOWNLOAD,
		EDIT,
		EXPIRESHARING,
		LOCK,
		MOVE,
		PREVIEW,
		RENAME,
		SHARE,
		STORAGEEXPIRATION,
		UNDELETE,
		UNLOCK,
		UNSHARE,
		UPLOAD
	}
	/**
	 * Create Threat Score Based Policy
	 * @param restClient
	 * @param policyName
	 * @param ciqFileName
	 * @return
	 */
	public HttpResponse createThreatScoreBasedPolicy(Client restClient, String policyName, String saasApp, List<NameValuePair> requestHeader, SuiteData suiteData){
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
	 * 
	 * @param restClient
	 * @param policyName
	 * @param saasApp
	 * @param user
	 * @param requestHeader
	 * @param riskLevel
	 * @param suiteData
	 * @return
	 */
	public HttpResponse createThreatScoreBasedPolicy(Client restClient, String policyName, String saasApp, String user, List<NameValuePair> requestHeader, String riskLevel, SuiteData suiteData){
		HttpResponse response = null;
		String restAPI = null;
		StringEntity entity = null;
		URI uri = null;
		try{
			restAPI = replaceGenericParams(suiteData.getAPIMap().get("createPolicy"), suiteData);
			entity = new StringEntity("{\"policy_name\":\""+policyName+"\",\"policy_description\":\"threat score policy\",\"applications\":[\""+saasApp+"\"],\"users_scope\":{\"users\":[\""+user+"\"],\"groups\":[]},\"users_scope_whitelist\":{\"users\":[],\"groups\":[]},\"risk_level\":"+riskLevel+",\"response\":{\"action\":[\"BLOCK_SERVICE\"],\"notify_email\":[],\"notify_sms\":[],\"notify_ticketing_email\":[],\"notify_user\":false},\"violations\":1,\"policy_type\":\"anomalydetect\",\"is_active\":true}");
			uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(),restAPI);
			response = restClient.doPost(uri, requestHeader, null, entity);
		}catch(Exception e){
			e.printStackTrace();
		}
		return response;
	}
	
	
	/**
	 * Get Policy Type
	 * @param responseBody
	 * @param policyNameForType
	 * @return
	 */
	public String getPolicyType(String responseBody, String policyNameForType){
		String policies;
		String policyType = null;
		try {
			policies = ClientUtil.getJSONValue(responseBody, ProtectTestConstants.POLICIES);
			JSONArray policiesArray = (JSONArray) new JSONTokener(policies).nextValue();
			for (int i = 0; i < policiesArray.size(); i++) {
				String policyName = ClientUtil.getJSONValue(policiesArray.getJSONObject(i).toString(), "policy_name").trim();
				policyName = policyName.substring(1, policyName.length()-1);
				if(policyNameForType.equals(policyName)){
					policyType = ClientUtil.getJSONValue(policiesArray.getJSONObject(i).toString(), "policy_type");
					policyType = policyType.substring(1, policyType.length()-1);
					break;
				}
			}
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return policyType;
	}
	
	/**
	 * Get the policy Sub ID
	 * @param response
	 * @param policyName
	 */
	public String getPolicyID(String responseBody, String policyNameForID){
		String policies;
		String policySubId = null;
		try {
			policies = ClientUtil.getJSONValue(responseBody, ProtectTestConstants.POLICIES);
			JSONArray policiesArray = (JSONArray) new JSONTokener(policies).nextValue();
			for (int i = 0; i < policiesArray.size(); i++) {
				String policyName = ClientUtil.getJSONValue(policiesArray.getJSONObject(i).toString(), "policy_name").trim();
				policyName = policyName.substring(1, policyName.length()-1);
				if(policyNameForID.equals(policyName)){
					policySubId = ClientUtil.getJSONValue(policiesArray.getJSONObject(i).toString(), "sub_id");
					policySubId = policySubId.substring(1, policySubId.length()-1);
					break;
				}
			}
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return policySubId;
	}
	
	/**
	 * This method creates the Content IQ Profile
	 * @param restClient
	 * @param data
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public HttpResponse createContentIQProfile(Client restClient, List<NameValuePair> requestHeader, SuiteData suiteData, Object...data) throws Exception{
		HttpResponse response= null;
		String groupArrayString = "";
		ArrayList<ArrayList<String>> operandsList = new ArrayList<ArrayList<String>>();
		operandsList = (ArrayList<ArrayList<String>>) data[4];
		int arraySize = operandsList.size();
		for(int i=0;i<arraySize;i++){
			ArrayList<String> insideArray = operandsList.get(i);
			groupArrayString = groupArrayString+"{\"name\":\"\",\"weight\":1,\"is_not\":false,\"value\":[\""+insideArray.get(1)+"\"],\"source\":\""+insideArray.get(2)+"\",\"min_count\":1},";
		}
		groupArrayString = groupArrayString.substring(0, groupArrayString.length()-1);
		String restAPI = replaceGenericParams(suiteData.getAPIMap().get("createPolicy"), suiteData);
		String stringEntity = "{\"threshold\":"+data[0].toString()+",\"description\":\""+data[1].toString()+"\",\"name\":\""+data[3]+"\",\"groups\":[{\"operator\":\"AND\",\"operands\":["+groupArrayString+"]}],\"appliesToSecurlets\":"+data[5].toString()+",\"api_enabled\":"+data[6].toString()+"}";
		StringEntity entity = new StringEntity(stringEntity);
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(),restAPI);
		response = restClient.doPost(dataUri, requestHeader, null, entity);	
		return response;
	}
	
	/**
	 * Activate Policy
	 * 
	 * @param restClient
	 * @return
	 * @throws Exception
	 */
	public HttpResponse activatePolicy(Client restClient, String policyType, String policyId, List<NameValuePair> requestHeader, SuiteData suiteData) throws Exception{
		String restAPI = replaceGenericParams(suiteData.getAPIMap().get("activatePolicy"), suiteData);
		StringEntity entity = new StringEntity(
				"{\"policy_type\":\"" + policyType + "\",\"status\":true,\"sub_id\":\""+policyId+"\"}");
		Reporter.log("Policy Activation Payload: "+ entity);
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(),restAPI);		
		HttpResponse response = restClient.doPost(dataUri, requestHeader, null, entity);	
		return response;
	}
	
	/**
	 * Activate Policy by Name
	 * @param restClient
	 * @param policyName
	 * @param requestHeader
	 * @param suiteData
	 * @return
	 * @throws Exception
	 */
	public void activatePolicyByName(Client restClient, String policyName, List<NameValuePair> requestHeader, SuiteData suiteData) throws Exception{
		Reporter.log("**********************************************************", true);
		Reporter.log("Activating policy - " + policyName, true);
		Reporter.log("**********************************************************", true);Map<String, String> policyDetails = this.getPolicyDetailsByName(restClient, policyName, requestHeader, suiteData);
		Thread.sleep(10000);
		String policyType = policyDetails.get(ProtectTestConstants.POLICY_TYPE);
		String policySubId = policyDetails.get(ProtectTestConstants.POLICY_SUB_ID);
		String restAPI = replaceGenericParams(suiteData.getAPIMap().get("activatePolicy"), suiteData);
		StringEntity entity = new StringEntity(
				"{\"policy_type\":\"" + policyType + "\",\"status\":true,\"sub_id\":\""+policySubId+"\"}");	
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(),restAPI);		
		HttpResponse policyActiveResponse = restClient.doPost(dataUri, requestHeader, null, entity);
		String policyActiveResponseBody = ClientUtil.getResponseBody(policyActiveResponse);
		Reporter.log("Activated policy response: "+policyActiveResponseBody, true);
		Reporter.log("**********************************************************", true);String policyActiveStatus = ClientUtil.getJSONValue(policyActiveResponseBody, ProtectTestConstants.ACTION_STATUS);
		policyActiveStatus = policyActiveStatus.substring(1, policyActiveStatus.length() - 1);
		Assert.assertEquals(policyActiveStatus, ProtectTestConstants.SUCCESS,"Policy Activation Failed for the policy:"+policyName);
		Reporter.log("Policy Activated - " + policyName, true);
		Reporter.log("**********************************************************", true);
	}
	
	/**
	 * Deactivate Policy by Name
	 * @param restClient
	 * @param policyName
	 * @param requestHeader
	 * @param suiteData
	 * @return
	 * @throws Exception
	 */
	public void deActivatePolicy(Client restClient, String policyName, List<NameValuePair> requestHeader, SuiteData suiteData) throws Exception{
		Reporter.log("Deactivate Policy - " + policyName, true);
		Map<String, String> policyDetails = this.getPolicyDetailsByName(restClient, policyName, requestHeader, suiteData);
		String policySubId = policyDetails.get(ProtectTestConstants.POLICY_SUB_ID);
		String policyType = policyDetails.get(ProtectTestConstants.POLICY_TYPE);
		String restAPI = replaceGenericParams(suiteData.getAPIMap().get("activatePolicy"), suiteData);
		StringEntity entity = new StringEntity(
				"{\"policy_type\":\""+policyType+"\",\"status\":false,\"sub_id\":\""+policySubId+"\"}");	
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(),restAPI);		
		HttpResponse deactivateResponse = restClient.doPost(dataUri, requestHeader, null, entity);
		String policyDeactivateResponseBody = ClientUtil.getResponseBody(deactivateResponse);
		String policyDeactivateStatus = ClientUtil.getJSONValue(policyDeactivateResponseBody, ProtectTestConstants.ACTION_STATUS);
		policyDeactivateStatus = policyDeactivateStatus.substring(1, policyDeactivateStatus.length() - 1);
		Assert.assertEquals(policyDeactivateStatus, ProtectTestConstants.SUCCESS,"Policy Deactivation Failed for the policy:"+policyName);
		Reporter.log("Policy deactivated - " + policyName, true);
	}
	
	/**
	 * 
	 * @param restClient
	 * @param policyType
	 * @param policyId
	 * @param requestHeader
	 * @param suiteData
	 * @return
	 * @throws Exception
	 */
	public HttpResponse deActivatePolicy(Client restClient, String policyType, String policyId, List<NameValuePair> requestHeader, SuiteData suiteData) throws Exception{
		
		String restAPI = replaceGenericParams(suiteData.getAPIMap().get("activatePolicy"), suiteData);
		StringEntity entity = new StringEntity(
				"{\"policy_type\":\""+policyType+"\",\"status\":false,\"sub_id\":\""+policyId+"\"}");	
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(),restAPI);		
		return restClient.doPost(dataUri, requestHeader, null, entity);	
	}
	
	/**
	 * Delete Policy
	 * 
	 * @param restClient
	 * @return
	 * @throws Exception
	 */
	public HttpResponse deletePolicy(Client restClient, String policyType, String policySubId, List<NameValuePair> requestHeader, SuiteData suiteData) throws Exception{
		String restAPI = replaceGenericParams(suiteData.getAPIMap().get("deletePolicy"), suiteData);
		String entityString = "{\"policy_type\":\""+policyType+"\",\"action\":true,\"sub_id\":\""+policySubId+"\"}";
		Logger.info(entityString);
		StringEntity entity = new StringEntity(entityString);
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(),restAPI);		
		return restClient.doPost(dataUri, requestHeader, null, entity);	
	}
	
	/**
	 * Delete policy by name
	 * @param restClient
	 * @param policyName
	 * @param requestHeader
	 * @param suiteData
	 * @return
	 * @throws Exception
	 */
	public void deletePolicy(Client restClient, String policyName, List<NameValuePair> requestHeader, SuiteData suiteData) throws Exception{
		Reporter.log("Delete Policy - " + policyName, true);
		Map<String, String> policyDetails = this.getPolicyDetailsByName(restClient, policyName, requestHeader, suiteData);
		HttpResponse deleteResponse = this.deletePolicy(restClient, policyDetails.get(ProtectTestConstants.POLICY_TYPE), policyDetails.get(ProtectTestConstants.POLICY_SUB_ID), requestHeader, suiteData);
		String policyDeleteResponseBody = ClientUtil.getResponseBody(deleteResponse);
		String policyDeleteStatus = ClientUtil.getJSONValue(policyDeleteResponseBody, ProtectTestConstants.ACTION_STATUS);
		policyDeleteStatus = policyDeleteStatus.substring(1, policyDeleteStatus.length() - 1);
		Assert.assertEquals(policyDeleteStatus, ProtectTestConstants.SUCCESS,"Policy Deletion Failed for the policy:"+policyName);
		Reporter.log("Policy Deleted - " + policyName, true);
	}
	
	/**
	 * deletePolicyIfAlreadyExist delete if only policy exist
	 * @param restClient
	 * @param policyName
	 * @param requestHeader
	 * @param suiteData
	 * @return
	 * @throws Exception
	 */
	public void deletePolicyIfAlreadyExist(Client restClient, String policyName, List<NameValuePair> requestHeader, SuiteData suiteData) throws Exception{
		//Logger.info("**************************************************************************");
		Map<String, String> policyDetails = this.getPolicyDetailsByName(restClient, policyName, requestHeader, suiteData);
		if (policyDetails.get(ProtectTestConstants.POLICY_ID)!=null){
			HttpResponse deleteResponse = this.deletePolicy(restClient, policyDetails.get(ProtectTestConstants.POLICY_TYPE), policyDetails.get(ProtectTestConstants.POLICY_SUB_ID), requestHeader, suiteData);
			String policyDeleteResponseBody = ClientUtil.getResponseBody(deleteResponse);
			String policyDeleteStatus = ClientUtil.getJSONValue(policyDeleteResponseBody, ProtectTestConstants.ACTION_STATUS);
			policyDeleteStatus = policyDeleteStatus.substring(1, policyDeleteStatus.length() - 1);
			Logger.info("Delete policy status" + policyDeleteStatus);
			Logger.info("**************************************************************************");
			Logger.info("Policy Deleted - " + policyName);
			Logger.info("**************************************************************************");
		}
	}
	public void deactivateAndDeletePolicy(Client restClient, String policyName, List<NameValuePair> requestHeader, SuiteData suiteData) throws Exception{
		this.deActivatePolicy(restClient, policyName, requestHeader, suiteData);
		this.deletePolicy(restClient, policyName, requestHeader, suiteData);

	}
	
	public void deleteExistingPolicy( Client restClient,String policyName, List<NameValuePair> requestHeader, SuiteData suiteData) throws Exception{
		if (isPolicyExists(restClient, policyName, requestHeader, suiteData)) {
			deletePolicy(restClient, policyName, requestHeader, suiteData);
		}
	}
	
	/**
	 * Check is policy exists
	 * @param restClient
	 * @param policyName
	 * @param requestHeader
	 * @param suiteData
	 * @return
	 * @throws Exception
	 */
	public boolean isPolicyExists(Client restClient, String policyName, List<NameValuePair> requestHeader, SuiteData suiteData) throws Exception{
		boolean flag = false;
		Map<String, String> policyDetails = this.getPolicyDetailsByName(restClient, policyName, requestHeader, suiteData);
		if(policyDetails.get(ProtectTestConstants.POLICY_TYPE) == null){
			flag = false;
		}else{
			flag = true;
			Reporter.log("Policy Already Exists - " + policyName, true);
		}
		return flag;
	}
	
	/**
	 * Check is policy active
	 * @param restClient
	 * @param policyName
	 * @param requestHeader
	 * @param suiteData
	 * @return
	 * @throws Exception
	 */
	public boolean isPolicyActive(Client restClient, String policyName, List<NameValuePair> requestHeader, SuiteData suiteData) throws Exception{
		boolean flag = false;
		Map<String, String> policyDetails = this.getPolicyDetailsByName(restClient, policyName, requestHeader, suiteData);
		if(!policyDetails.get(ProtectTestConstants.ACTION_STATUS).equals(true)){
			flag = true;
		}
		return flag;
	}
	/**
	 * Get policy type, id, subid and status by name
	 * @param restClient
	 * @param policyName
	 * @param requestHeader
	 * @param suiteData
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> getPolicyDetailsByName(Client restClient, String policyName, List<NameValuePair> requestHeader, SuiteData suiteData) throws Exception{
		Map<String, String> policyDetails = new HashMap<String, String>();
		String policySubId=null;
		String policyId=null;
		String restAPI = replaceGenericParams(suiteData.getAPIMap().get("policyList"), suiteData);
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(),restAPI);
		Logger.info("Fetching list of all policies... ");
		HttpResponse policyListResponse = restClient.doGet(dataUri, requestHeader);
		String policyListResponseBody = ClientUtil.getResponseBody(policyListResponse);
		String policiesList = ClientUtil.getJSONValue(policyListResponseBody, "policies"); 
		//Reporter.log("Policies List in the Portal: "+policiesList, true);
		JSONArray jArray = (JSONArray) new JSONTokener(policiesList).nextValue();
		for (int i = 0; i < jArray.size(); i++) {
			String name = ClientUtil.getJSONValue(jArray.getJSONObject(i).toString(), "policy_name");
			name = name.substring(1, name.length()-1);
			//Reporter.log("Policy Name: "+name, true);

			if(name.equalsIgnoreCase(policyName)){
				String policyType = ClientUtil.getJSONValue(jArray.getJSONObject(i).toString(), "policy_type");
				policyType = policyType.substring(1, policyType.length()-1);
				policyId = ClientUtil.getJSONValue(jArray.getJSONObject(i).toString(), "id");
				policyId = policyId.substring(1, policyId.length()-1);
				policySubId = ClientUtil.getJSONValue(jArray.getJSONObject(i).toString(), "sub_id");
				policySubId = policySubId.substring(1, policySubId.length()-1);
				String policyStatus = ClientUtil.getJSONValue(jArray.getJSONObject(i).toString(), "is_active");
				//policyStatus = policyStatus.substring(1, policyStatus.length()-1);
				policyDetails.put(ProtectTestConstants.POLICY_TYPE, policyType);
				policyDetails.put(ProtectTestConstants.POLICY_ID, policyId);
				policyDetails.put(ProtectTestConstants.POLICY_SUB_ID, policySubId);
				policyDetails.put(ProtectTestConstants.ACTION_STATUS, policyStatus);
				Logger.info("Policy Exist");
				break;
			}
		}
		if (policyDetails.get(ProtectTestConstants.POLICY_ID)==null){
			Logger.info("Policy does not Exist");
		}
		Logger.info("**************************************************************************");
		return policyDetails;
	}
	
	/**
	 * Create Content IQ Profile
	 * 
	 * @param restClient
	 * @return
	 * @throws Exception
	 */
	public HttpResponse createContentIQProfileByCustomTerms(Client restClient, String ciqProfileName, String customTerm, List<NameValuePair> requestHeader, SuiteData suiteData) throws Exception{
		String restAPI = replaceGenericParams(suiteData.getAPIMap().get("createContentIQ"), suiteData);
		StringEntity entity = new StringEntity(
				"{\"threshold\":1,\"description\":\"Test CIQ Decscription\",\"name\":\""+ciqProfileName+"\",\"groups\":[{\"operator\":\"AND\",\"operands\":[{\"name\":\"\",\"weight\":1,\"is_not\":false,\"value\":[\""+customTerm+"\"],\"source\":\"CUSTOM_CONTENT\",\"min_count\":1}]}],\"appliesToSecurlets\":false,\"api_enabled\":false}");	
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(),restAPI);		
		return restClient.doPost(dataUri, requestHeader, null, entity);	
	}	
	
	/**
	 * List Content IQ Profile
	 * 
	 * @param restClient
	 * @return
	 * @throws Exception
	 */
	public HttpResponse listContentIQProfileByCustomTerms(Client restClient, List<NameValuePair> requestHeader, SuiteData suiteData) throws Exception{
		String restAPI = replaceGenericParams(suiteData.getAPIMap().get("listContentIQ"), suiteData);
		StringEntity entity = new StringEntity(
				"{\"url\":\"contentprofiles\",\"id\":\"\",\"action\":\"list\",\"params\":{\"limit\":0,\"format\":\"json\"}}");	
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(),restAPI);		
		return restClient.doPost(dataUri, requestHeader, null, entity);	
	}
	
	/**
	 * Get Content IQ Profile
	 * @param response
	 * @param profileName
	 * @return
	 */
	public String getContentIQProfileID(HttpResponse response, String profileName){
		String ciqResponse = ClientUtil.getResponseBody(response);
		String ciqProfileID = null;
		try {
			String data = ClientUtil.getJSONValue(ciqResponse, "data");
			String ciqProfiles = ClientUtil.getJSONValue(data, "objects");
			JSONArray ciqArray = (JSONArray) new JSONTokener(ciqProfiles).nextValue();
			for (int i = 0; i < ciqArray.size(); i++) {
				String policyNameByID = ClientUtil.getJSONValue(ciqArray.getJSONObject(i).toString(), ProtectTestConstants.PROFILE_NAME);
				policyNameByID = policyNameByID.substring(1, policyNameByID.length() - 1);
				if (policyNameByID.equals(profileName)) {
					ciqProfileID = ClientUtil.getJSONValue(ciqArray.getJSONObject(i).toString(), "id");
					ciqProfileID = ciqProfileID.substring(1, ciqProfileID.length() - 1);
					break;
				}
			}			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ciqProfileID;
	}
	
	/**
	 * Delete Content IQ Profile
	 * 
	 * @param restClient
	 * @return
	 * @throws Exception
	 */
	public HttpResponse deleteContentIQProfileById(Client restClient, String id, List<NameValuePair> requestHeader, SuiteData suiteData) throws Exception{
		String restAPI = replaceGenericParams(suiteData.getAPIMap().get("listContentIQ"), suiteData);
		StringEntity entity = new StringEntity(
				"{\"url\":\"contentprofiles\",\"id\":\""+id+"\",\"action\":\"delete\"}");	
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(),restAPI);		
		return restClient.doPost(dataUri, requestHeader, null, entity);	
	}
	
	/**
	 * @param url
	 * @return
	 */
	private String replaceGenericParams(String url, SuiteData suiteData){
		if(url.contains("tenantName"))
			url = url.replace("{tenantName}", suiteData.getTenantName());
		if(url.contains("version"))
			url = url.replace("{version}", suiteData.getBaseVersion());
		return url;
	}
	
	/**
	 * Get TimeStamp
	 * @param format
	 * @return
	 */
	public String getDateTimeFormat(String format){
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		String strDate = sdf.format(cal.getTime());
		return strDate;
	}
	
	/**
	 * Wait for time
	 * @param i
	 */
	public void waitForMinutes(double i){
		try {
			Thread.sleep(1000*60*Math.round(i));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	
	/**
	 * Deactivate Policy by Name
	 * @param restClient
	 * @param policyName
	 * @param requestHeader
	 * @param suiteData
	 * @return
	 * @throws Exception
	 */
	public HttpResponse deActivatePolicyByName(Client restClient, String policyName, List<NameValuePair> requestHeader, SuiteData suiteData) throws Exception{
		Map<String, String> policyDetails = this.getPolicyDetailsByName(restClient, policyName, requestHeader, suiteData);
		String policySubId = policyDetails.get(ProtectTestConstants.POLICY_SUB_ID);
		String policyType = policyDetails.get(ProtectTestConstants.POLICY_TYPE);
		String restAPI = replaceGenericParams(suiteData.getAPIMap().get("activatePolicy"), suiteData);
		StringEntity entity = new StringEntity(
				"{\"policy_type\":\""+policyType+"\",\"status\":false,\"sub_id\":\""+policySubId+"\"}");	
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(),restAPI);		
		return restClient.doPost(dataUri, requestHeader, null, entity);	
	}

}