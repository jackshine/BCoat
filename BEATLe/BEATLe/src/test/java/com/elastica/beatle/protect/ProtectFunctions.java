/**
 * 
 */
package com.elastica.beatle.protect;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.TimeZone;
import java.util.UUID;

import net.sf.json.JSONArray;
import net.sf.json.util.JSONTokener;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.apache.pdfbox.util.PDFTextStripperByArea;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.SkipException;

import com.elastica.beatle.DateUtils;
import com.elastica.beatle.MarshallingUtils;
import com.elastica.beatle.RawJsonParser;
import com.elastica.beatle.TestSuiteDTO;
import com.elastica.beatle.RestClient.Client;
import com.elastica.beatle.RestClient.ClientUtil;
import com.elastica.beatle.dci.DCIFunctions;
import com.elastica.beatle.dci.SaasType;
import com.elastica.beatle.es.ElasticSearchLogs;
import com.elastica.beatle.fileHandler.FileHandlingUtils;
import com.elastica.beatle.logger.Logger;
import com.elastica.beatle.securlets.ESQueryBuilder;
import com.elastica.beatle.splunk.SplunkQueries;
import com.elastica.beatle.splunk.SplunkQueryResult;
import com.elastica.beatle.tests.protect.accessmonitoring.BoxFileOperationTests;
import com.elastica.beatle.tests.protect.accessmonitoring.BoxFolderOperationTests;
import com.elastica.beatle.tests.protect.accessmonitoring.BoxUserOperationTests;
import com.elastica.beatle.tests.securlets.OneDriveUtils;
import com.google.common.io.Files;
import com.universal.common.GDrive;
import com.universal.common.GDriveAuthorization;
import com.universal.common.GoogleMailServices;
import com.universal.common.Office365MailActivities;
import com.universal.common.Salesforce;
import com.universal.common.UniversalApi;
import com.universal.dtos.UserAccount;
import com.universal.dtos.box.AccessibleBy;
import com.universal.dtos.box.BoxCollaboration;
import com.universal.dtos.box.BoxFolder;
import com.universal.dtos.box.BoxGroup;
import com.universal.dtos.box.BoxMembership;
import com.universal.dtos.box.BoxUserInfo;
import com.universal.dtos.box.CollaborationInput;
import com.universal.dtos.box.FileEntry;
import com.universal.dtos.box.FileUploadResponse;
import com.universal.dtos.box.GroupInput;
import com.universal.dtos.box.Item;
import com.universal.dtos.box.MembershipInput;
import com.universal.dtos.box.SharedLink;
import com.universal.dtos.onedrive.DocumentSharingResult;
import com.universal.dtos.onedrive.Folder;
import com.universal.dtos.onedrive.ItemResource;
import com.universal.dtos.onedrive.ListItemAllFields;
import com.universal.dtos.onedrive.Metadata;
import com.universal.dtos.onedrive.SharingUserRoleAssignment;
import com.universal.dtos.onedrive.SiteFileResource;
import com.universal.dtos.onedrive.UserRoleAssignment;
import com.universal.dtos.salesforce.FileShares;


/**
 * @author shri
 *
 */
public class ProtectFunctions{
	
	
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
			entity = new StringEntity("{\"policy_name\":\""+policyName+"\",\"policy_description\":\"threat score policy\",\"applications\":[\""+saasApp+"\"],\"users_scope\":{\"users\":[\"__ALL_EL__\"],\"groups\":[],\"domains\":[\"__ALL_EL__\"]},\"users_scope_whitelist\":{\"users\":[],\"groups\":[]},\"risk_level\":0,\"response\":{\"action\":[\"BLOCK_SERVICE\"],\"notify_email\":[],\"notify_sms\":[],\"notify_ticketing_email\":[],\"notify_user\":false},\"violations\":1,\"policy_type\":\"anomalydetect\",\"is_active\":true}");
			uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(),restAPI);
			response = restClient.doPost(uri, requestHeader, null, entity);
		}catch(Exception e){
			e.printStackTrace();
		}
		return response;
	}
	
	/* -----------------------------  ThreatScore policy Functions  ----------------------------- */
	
	/**
	 * 
	 * @param data
	 * @return
	 */
	public PolicyBean setThreatScorePolicyData(String[] data){
		PolicyBean policyBean = new PolicyBean();
		policyBean.setPolicyName(data[0]);
		policyBean.setCloudService(data[1]);
		policyBean.setFileOwnerUser(data[2]);
		policyBean.setFileOwnerGroup(data[3]);
		policyBean.setDomainName(data[4]);
		policyBean.setFileOwnerUserException(data[5]);
		policyBean.setFileOwnerGroupException(data[6]);
		policyBean.setThreatScore(data[7]);
		policyBean.setBlock(data[8]);
		return policyBean;
	}
	
	/**
	 * 
	 * @param restClient
	 * @param requestHeader
	 * @param suiteData
	 * @param policyBean
	 * @return
	 */
	public void createThreatScoreBasedPolicy(Client restClient, List<NameValuePair> requestHeader,  TestSuiteDTO suiteData, PolicyBean policyBean){
		HttpResponse response = null;
		String restAPI = null;
		StringEntity entity = null;
		URI uri = null;
		String policyName = policyBean.getPolicyName();
		try{
			if(this.isPolicyExists(restClient, policyName, requestHeader, suiteData)){
				this.deletePolicy(restClient, policyName, requestHeader, suiteData);
			}
			if((policyBean.getFileOwnerUser().equalsIgnoreCase("Any") && policyBean.getFileOwnerGroup().equalsIgnoreCase("Any")) && policyBean.getDomainName().equalsIgnoreCase("Any")){
				policyBean.setFileOwnerUser("__ALL_EL__");
				policyBean.setFileOwnerGroup("__ALL_EL__");
				policyBean.setDomainName("__ALL_EL__");
			}else {
				if(policyBean.getFileOwnerUser().equalsIgnoreCase("Any")){
					policyBean.setFileOwnerUser("");
				}else{
					policyBean.setFileOwnerUser(policyBean.getFileOwnerUser());
				}
				
				if(policyBean.getFileOwnerGroup().equalsIgnoreCase("Any")){
					policyBean.setFileOwnerGroup("");
				}else{
					policyBean.setFileOwnerGroup(policyBean.getFileOwnerGroup());
				}
				
				if(policyBean.getDomainName().equalsIgnoreCase("Any")){
					policyBean.setDomainName("__ALL_EL__");
				}else{
					policyBean.setDomainName(policyBean.getDomainName());
				}
				
			}
			
			List<String> fileOwnerUserException = new ArrayList<String>();
			if(policyBean.getFileOwnerUserException().equalsIgnoreCase("No")){
				fileOwnerUserException.add("");
			}else{
				if(policyBean.getFileOwnerUserException().contains(",")){
					String[] userException = policyBean.getFileOwnerUserException().split(",");
					for(int i=0;i<userException.length;i++){
						fileOwnerUserException.add(userException[i]);
					}
				}else{
					fileOwnerUserException.add(policyBean.getFileOwnerUserException());
				}
			}			
			
			
			if(policyBean.getFileOwnerGroupException().equalsIgnoreCase("No")){
				policyBean.setFileOwnerGroupException("");
			}else{
				policyBean.setFileOwnerGroupException(policyBean.getFileOwnerGroupException());
			}
			
			restAPI = replaceGenericParams(suiteData.getAPIMap().get("createPolicy"), suiteData);
			ProtectQueryBuilder pqb = new ProtectQueryBuilder();
			String payload = pqb.getESQueryForThreatScorePolicy(policyBean, fileOwnerUserException);
			//String payload = "{\"policy_name\":\""+policyBean.getPolicyName()+"\",\"policy_description\":\"threat score policy\",\"applications\":[\""+policyBean.getCloudService()+"\"],\"users_scope\":{\"users\":"+policyBean.getFileOwnerUser()+",\"groups\":"+policyBean.getFileOwnerGroup()+",\"domains\":"+policyBean.getDomainName()+"},\"users_scope_whitelist\":{\"users\":"+policyBean.getFileOwnerUserException()+",\"groups\":"+policyBean.getFileOwnerGroupException()+"},\"risk_level\":"+policyBean.getThreatScore()+",\"response\":{\"action\":[\""+policyBean.getBlock()+"\"],\"notify_email\":[],\"notify_sms\":[],\"notify_ticketing_email\":[],\"notify_user\":false},\"violations\":1,\"policy_type\":\"anomalydetect\",\"is_active\":true}";
			payload = payload.replaceAll("\"\"", "");
			Reporter.log("ThreatScore Payload: "+payload, true);
			entity = new StringEntity(payload);
			uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(),restAPI);
			response = restClient.doPost(uri, requestHeader, null, entity);
			String policyResponseBody = ClientUtil.getResponseBody(response);
			Reporter.log("Create policy response: "+policyResponseBody, true);
			String createPolicyStatus = ClientUtil.getJSONValue(policyResponseBody, ProtectTestConstants.ACTION_STATUS);
			createPolicyStatus = createPolicyStatus.substring(1, createPolicyStatus.length() - 1);
			Assert.assertEquals(createPolicyStatus, ProtectTestConstants.SUCCESS);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void createAndActivateThreatScoreBasedPolicy(Client restClient, List<NameValuePair> requestHeader,  TestSuiteDTO suiteData, PolicyBean policyBean) throws Exception{
		this.createThreatScoreBasedPolicy(restClient, requestHeader, suiteData, policyBean);
		this.activatePolicyByName(restClient, policyBean.getPolicyName(), requestHeader, suiteData);
	}
	
	/**
	 * 
	 * @param restClient
	 * @param policyBean
	 * @param requestHeader
	 * @param suiteData
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> getThreatScorePolicyViolationAlertLogDetails(Client restClient, PolicyBean policyBean, List<NameValuePair> requestHeader, TestSuiteDTO suiteData) throws Exception{
		Map<String, String> responseMessages = new HashMap<String, String>();
		HttpResponse response = null;
		ElasticSearchLogs esLogs = new ElasticSearchLogs();
		String csfrToken = requestHeader.get(0).getValue();
		String sessionID = requestHeader.get(4).getValue();
		
		//if (policyBean.getCloudService().equalsIgnoreCase("Office 365") || policyBean.getCloudService().equalsIgnoreCase("Dropbox"))
			//policyBean.setCloudService(policyBean.getCloudService().toLowerCase());
		
		String payload = "{\"source\":{\"query\":{\"filtered\":{\"query\":{\"bool\":{\"must\":[{\"range\":{\"created_timestamp\":{\"from\":\""+getDateFromCurrent(-1)+"T12:35:27\",\"to\":\""+getDateFromCurrent(2)+"T12:35:27\"}}},{\"query_string\":{\"query\":\"" 
				+ policyBean.getPolicyName()
				+ "\",\"default_operator\":\"AND\",\"analyzer\":\"custom_search_analyzer\",\"allow_leading_wildcard\":\"false\"}},{\"term\":{\"facility\":\""
				+ policyBean.getCloudService()
				+ "\"}}]}},\"filter\":{}}},\"from\":0,\"size\":160,\"sort\":{\"created_timestamp\":{\"order\":\"desc\",\"ignore_unmapped\":\"true\"}}},\"sourceName\":\""+policyBean.getCloudService()+"\",\"apiServerUrl\":\"http://"+suiteData.getApiserverHostName()+"/\",\"csrftoken\":\""
				+ csfrToken + "\",\"sessionid\":\"" + sessionID + "\",\"userid\":\"" + suiteData.getUsername()
				+ "\"}";
		StringEntity entity = new StringEntity(payload);
		response = esLogs.getDisplayLogs(restClient, requestHeader, suiteData.getApiserverHostName(), entity);
		
		String responseBody = ClientUtil.getResponseBody(response);
		String hits = ClientUtil.getJSONValue(ClientUtil.getJSONValue(responseBody, "hits"), "hits");
		JSONArray jArray = (JSONArray) new JSONTokener(hits).nextValue();
		String message = "";
		for (int i = 0; i < jArray.size(); i++) {
			String source = ClientUtil.getJSONValue(jArray.getJSONObject(i).toString(), "_source");
			Logger.info(source);
			message = ClientUtil.getJSONValue(source, "message");
			if((message.contains("[ALERT] User performed anomalous activities that violated policy : ")) && (message.contains(policyBean.getPolicyName()))){
				String severity = ClientUtil.getJSONValue(source, ProtectTestConstants.SEVERITY);
				severity = severity.substring(1, severity.length()-1);
				String policyAction = ClientUtil.getJSONValue(source, ProtectTestConstants.POLICY_ACTION);
				policyAction = policyAction.substring(1, policyAction.length()-1);
				String facility = ClientUtil.getJSONValue(source, ProtectTestConstants.FACILITY);
				facility = facility.substring(1, facility.length()-1);
				String __source = ClientUtil.getJSONValue(source, ProtectTestConstants.SOURCE);
				__source = __source.substring(1, __source.length()-1);
				String policyViolated = ClientUtil.getJSONValue(source, ProtectTestConstants.POLICY_VIOLATED);
				policyViolated = policyViolated.substring(1, policyViolated.length()-1);
				String policyType = ClientUtil.getJSONValue(source, ProtectTestConstants.POLICY_TYPE);
				policyType = policyType.substring(1, policyType.length()-1);
				message = message.substring(1, message.length()-1);
				String activityType = ClientUtil.getJSONValue(source, ProtectTestConstants.ACTIVITY_TYPE);
				activityType = activityType.substring(1, activityType.length()-1);
				String user = ClientUtil.getJSONValue(source, ProtectTestConstants.USER);
				user = user.substring(1, user.length()-1);
				String domain = ClientUtil.getJSONValue(source, ProtectTestConstants.DOMAIN);
				domain = domain.substring(1, domain.length()-1);
				String blockedApps = ClientUtil.getJSONValue(source, ProtectTestConstants.BLOCKED_APPS);
				blockedApps = blockedApps.substring(1, blockedApps.length()-1);
				responseMessages.put(ProtectTestConstants.SEVERITY, severity);
				responseMessages.put(ProtectTestConstants.POLICY_ACTION, policyAction);
				responseMessages.put(ProtectTestConstants.FACILITY, facility);
				responseMessages.put(ProtectTestConstants.SOURCE, __source);
				responseMessages.put(ProtectTestConstants.POLICY_VIOLATED, policyViolated);
				responseMessages.put(ProtectTestConstants.POLICY_TYPE, policyType);
				responseMessages.put(ProtectTestConstants.MESSAGE, message);
				responseMessages.put(ProtectTestConstants.ACTIVITY_TYPE, activityType);
				responseMessages.put(ProtectTestConstants.USER, user);
				responseMessages.put(ProtectTestConstants.DOMAIN, domain);
				responseMessages.put(ProtectTestConstants.BLOCKED_APPS, blockedApps);
				break;
			}
		}
	return responseMessages;
	}
	
	/**
	 * 
	 * @param policyViolationLogs
	 * @param policyBean
	 * @param suiteData
	 */
	public void assertThreatScorePolicyViolation(Map<String, String> policyViolationLogs, PolicyBean policyBean, TestSuiteDTO suiteData){
		Reporter.log(policyBean.getPolicyName()+" : "+ policyViolationLogs, true);
		Reporter.log("Actual Policy Violation Parameters", true);
		Reporter.log("--------------------Actual Policy Violation Parameters--------------------", true);
		Reporter.log("Severity        : "+policyViolationLogs.get(ProtectTestConstants.SEVERITY), true);
		Reporter.log("Policy Action   : "+policyViolationLogs.get(ProtectTestConstants.POLICY_ACTION), true);
		Reporter.log("Facility        : "+policyViolationLogs.get(ProtectTestConstants.FACILITY), true);
		Reporter.log("Source          : "+policyViolationLogs.get(ProtectTestConstants.SOURCE), true);
		Reporter.log("Policy Violated : "+policyViolationLogs.get(ProtectTestConstants.POLICY_VIOLATED), true);
		Reporter.log("Policy Type     : "+policyViolationLogs.get(ProtectTestConstants.POLICY_TYPE), true);
		Reporter.log("Activity Type   : "+policyViolationLogs.get(ProtectTestConstants.ACTIVITY_TYPE), true);
		Reporter.log("User            : "+policyViolationLogs.get(ProtectTestConstants.USER), true);
		Reporter.log("Domain          : "+policyViolationLogs.get(ProtectTestConstants.DOMAIN), true);
		Reporter.log("Blocked Apps    : "+policyViolationLogs.get(ProtectTestConstants.BLOCKED_APPS), true);
		Reporter.log("Message         : "+policyViolationLogs.get(ProtectTestConstants.MESSAGE), true);
		Reporter.log("--------------------------------------------------------------------------", true);
		Reporter.log("--------------------Expected Policy Violation Parameters--------------------", true);
		//Reporter.log("Severity        : "+ProtectTestConstants.CRITICAL, true);
		Reporter.log("Policy Action   : "+ProtectTestConstants.BLOCK, true);
		Reporter.log("Facility        : "+policyBean.getCloudService(), true);
		Reporter.log("Source          : "+ProtectTestConstants.API, true);
		Reporter.log("Policy Violated : "+policyBean.getPolicyName(), true);
		Reporter.log("Policy Type     : "+ProtectTestConstants.THREATSCORE, true);
		Reporter.log("Activity Type   : "+ProtectTestConstants.POLICY_VIOLATION, true);
		Reporter.log("User            : "+suiteData.getSaasAppUsername(), true);
		Reporter.log("Domain          : "+suiteData.getDomainName(), true);
		Reporter.log("Blocked Apps    : "+policyBean.getCloudService(), true);
		Reporter.log("Message         : "+"[ALERT] User performed anomalous activities that violated policy : "+policyBean.getPolicyName(), true);
		Reporter.log("----------------------------------------------------------------------------", true);
		Reporter.log("------------------Actual and Expected Parameters Assertion------------------", true);
		//Assert.assertEquals(policyViolationLogs.get(ProtectTestConstants.SEVERITY), ProtectTestConstants.CRITICAL);
		Assert.assertEquals(policyViolationLogs.get(ProtectTestConstants.POLICY_ACTION), ProtectTestConstants.BLOCK);
		Assert.assertEquals(policyViolationLogs.get(ProtectTestConstants.FACILITY), policyBean.getCloudService());
		Assert.assertEquals(policyViolationLogs.get(ProtectTestConstants.SOURCE), ProtectTestConstants.API);
		Assert.assertEquals(policyViolationLogs.get(ProtectTestConstants.POLICY_VIOLATED), policyBean.getPolicyName());
		Assert.assertEquals(policyViolationLogs.get(ProtectTestConstants.POLICY_TYPE), ProtectTestConstants.THREATSCORE);
		Assert.assertEquals(policyViolationLogs.get(ProtectTestConstants.ACTIVITY_TYPE), ProtectTestConstants.POLICY_VIOLATION);
		Assert.assertEquals(policyViolationLogs.get(ProtectTestConstants.USER), suiteData.getSaasAppUsername());
		Assert.assertEquals(policyViolationLogs.get(ProtectTestConstants.DOMAIN), suiteData.getDomainName());
		Assert.assertEquals(policyViolationLogs.get(ProtectTestConstants.BLOCKED_APPS), policyBean.getCloudService());
		Assert.assertEquals(policyViolationLogs.get(ProtectTestConstants.MESSAGE), "[ALERT] User performed anomalous activities that violated policy : "+policyBean.getPolicyName());
		Reporter.log("----------------------------------------------------------------------------", true);
	}
	
	/**
	 * 
	 * @param restClient
	 * @param policyBean
	 * @param requestHeader
	 * @param suiteData
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> getProtectBlockDetails(Client restClient, PolicyBean policyBean, List<NameValuePair> requestHeader, TestSuiteDTO suiteData) throws Exception{
		Map<String, String> policyBlockDetails = new HashMap<String, String>();
		String restAPI = replaceGenericParams(suiteData.getAPIMap().get("getBlocks"), suiteData);
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(),restAPI);
		HttpResponse policyBlockResponse = restClient.doGet(dataUri, requestHeader);
		String policyBlockResponseBody = ClientUtil.getResponseBody(policyBlockResponse);
		String blocks = ClientUtil.getJSONValue(ClientUtil.getJSONValue(policyBlockResponseBody, "blocks"), "blocks");
		JSONArray jArray = (JSONArray) new JSONTokener(blocks).nextValue();
		for (int i = 0; i < jArray.size(); i++) {
			String block = ClientUtil.getJSONValue(jArray.getJSONObject(i).toString(), "block");
			String policyList = ClientUtil.getJSONValue(block, "blocked_apps");
			if(policyList.contains(policyBean.getPolicyName())){
				String id = ClientUtil.getJSONValue(block, "id");
				id = id.substring(1, id.length()-1);
				policyBlockDetails.put("id", id);
				String isBlocked = ClientUtil.getJSONValue(block, "is_blocked");
				policyBlockDetails.put(ProtectTestConstants.BLOCK, isBlocked);
				String unblockedApps = ClientUtil.getJSONValue(policyList, policyBean.getPolicyName());
				JSONArray unblockAppArray = (JSONArray) new JSONTokener(unblockedApps).nextValue();
				for (int j = 0; j < unblockAppArray.size(); j++) {
					String appName = unblockAppArray.getString(j);
					if(appName.contains(policyBean.getCloudService())){
						break;
					}
				}
				break;
			}
		}
		return policyBlockDetails;
	}
	
	/**
	 * 
	 * @param restClient
	 * @param policyBean
	 * @param requestHeader
	 * @param suiteData
	 * @param blockData
	 * @throws Exception
	 */
	public void clearBlock(Client restClient, PolicyBean policyBean, List<NameValuePair> requestHeader, TestSuiteDTO suiteData, Map<String, String> blockData) throws Exception{
		Reporter.log("Clear Block for "+policyBean.getPolicyName(), true);
		String payload = "{\"user_id\":\""+blockData.get("id")+"\",\"email\":\""+suiteData.getSaasAppUsername()+"\",\"app_action\":\"UNBLOCK\",\"unblock_app\":\""+policyBean.getCloudService()+"\"}";
		Reporter.log("Payload for Clear Block: "+payload, true);
		StringEntity entity = new StringEntity(payload);
		String restAPI = replaceGenericParams(suiteData.getAPIMap().get("clearBlock"), suiteData);
		URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(),restAPI);
		HttpResponse clearBlockResponse = restClient.doPost(uri, requestHeader, null, entity);
		String clearBlockResponseBody = ClientUtil.getResponseBody(clearBlockResponse);
		Reporter.log("Clear block response body: "+clearBlockResponseBody, true);
		String clearBlockStatus = ClientUtil.getJSONValue(clearBlockResponseBody, ProtectTestConstants.ACTION_STATUS);
		clearBlockStatus = clearBlockStatus.substring(1, clearBlockStatus.length() - 1);
		Assert.assertEquals(clearBlockStatus, ProtectTestConstants.SUCCESS);
	}
	
	/* -----------------------------  ThreatScore policy Functions  ----------------------------- */
	
	/* -----------------------------  DataScore policy Functions  ----------------------------- */
	/**
	 * Add remediation activity
	 * @param appname
	 * @param exposureType
	 * @param remediationActivity
	 * @return
	 * @throws Exception 
	 */
	public String addRemediation(String appname, String remActivities){
		String remediation = "";
		if(appname.equalsIgnoreCase("Dropbox")){
			if(remActivities.equalsIgnoreCase("sharedLinkRevoke")){
				remediation = "{\"code\":\"SHARED_LINK_REVOKE\",\"meta_info\":{}},";
			}
			if(remActivities.equalsIgnoreCase("delete")){
				remediation = "{\"code\":\"MOVE_TO_TRASH\",\"meta_info\":{}},";
			}
		}else if(appname.equalsIgnoreCase("Box")){
			
			String[] remediationActivities = remActivities.split(",");
			
			for (String remediationActivity : remediationActivities) {
			
			if(remediationActivity.equalsIgnoreCase("unshare")){
				remediation = remediation+"{\"code\":\"UNSHARE\",\"meta_info\":{}},";
			}
			if(remediationActivity.equalsIgnoreCase("company")){
				remediation = remediation+"{\"code\":\"SHARE_ACCESS\",\"meta_info\":{\"access\":\"company\"}},";
			}
			if(remediationActivity.equalsIgnoreCase("collaborators")){
				remediation = remediation+"{\"code\":\"SHARE_ACCESS\",\"meta_info\":{\"access\":\"collaborators\"}},";
			}
			if(remediationActivity.equalsIgnoreCase("open")){
				remediation = remediation+"{\"code\":\"SHARE_ACCESS\",\"meta_info\":{\"access\":\"open\"}},";
			}
			if(remediationActivity.equalsIgnoreCase("expires")){
				remediation = remediation+"{\"code\":\"SHARE_EXPIRE\",\"meta_info\":{\"expires_on\":\"2\"}},";
			}
			if(remediationActivity.equalsIgnoreCase("removeCollab")){
				remediation = remediation+"{\"code\":\"COLLAB_REMOVE\",\"meta_info\":{\"collabs\":[]}},";
			}
			if(remediationActivity.equalsIgnoreCase("editor")){
				remediation = remediation+"{\"code\":\"COLLAB_UPDATE\",\"meta_info\":{\"role\":\"editor\",\"collabs\":[]}},";
			}
			if(remediationActivity.equalsIgnoreCase("viewer")){
				remediation = remediation+"{\"code\":\"COLLAB_UPDATE\",\"meta_info\":{\"role\":\"viewer\",\"collabs\":[]}},";
			}
			if(remediationActivity.equalsIgnoreCase("previewer")){
				remediation = remediation+"{\"code\":\"COLLAB_UPDATE\",\"meta_info\":{\"role\":\"previewer\",\"collabs\":[]}},";
			}
			if(remediationActivity.equalsIgnoreCase("uploader")){
				remediation = remediation+"{\"code\":\"COLLAB_UPDATE\",\"meta_info\":{\"role\":\"uploader\",\"collabs\":[]}},";
			}
			if(remediationActivity.equalsIgnoreCase("previewer uploader")){
				remediation = remediation+"{\"code\":\"COLLAB_UPDATE\",\"meta_info\":{\"role\":\"previewer uploader\",\"collabs\":[]}},";
			}
			if(remediationActivity.equalsIgnoreCase("viewer uploader")){
				remediation = remediation+"{\"code\":\"COLLAB_UPDATE\",\"meta_info\":{\"role\":\"viewer uploader\",\"collabs\":[]}},";
			}
			if(remediationActivity.equalsIgnoreCase("co-owner")){
				remediation = remediation+"{\"code\":\"COLLAB_UPDATE\",\"meta_info\":{\"role\":\"co-owner\",\"collabs\":[]}},";
			}
			}
		} else if(appname.equalsIgnoreCase("Google Drive")){
			
			String[] remediationActivities = remActivities.split(",");
			
			for (String remediationActivity : remediationActivities) {
			if(remediationActivity.contains("unshare")){
				remediation = remediation+"{\"code\":\"UNSHARE\",\"meta_info\":{}},";
			}
			if(remediationActivity.contains("removeCollab")){
				remediation = remediation+"{\"code\":\"COLLAB_REMOVE\",\"meta_info\":{\"collabs\":[]}},";
			}
			if(remediationActivity.contains("writer")){
				remediation = remediation+"{\"code\":\"SHARE_ACCESS\",\"meta_info\":{\"access\":\"open-writer\"}},";
			}
			if(remediationActivity.contains("reader")){
				remediation = remediation+"{\"code\":\"SHARE_ACCESS\",\"meta_info\":{\"access\":\"open-reader\"}},";
			}
			if(remediationActivity.contains("commenter")){
				remediation = remediation+"{\"code\":\"SHARE_ACCESS\",\"meta_info\":{\"access\":\"open-commenter\"}},";
			}
			if(remediationActivity.contains("linkWriter")){
				remediation = remediation+"{\"code\":\"SHARE_ACCESS\",\"meta_info\":{\"access\":\"open-withlink-writer\"}},";
			}
			if(remediationActivity.contains("linkReader")){
				remediation = remediation+"{\"code\":\"SHARE_ACCESS\",\"meta_info\":{\"access\":\"open-withlink-reader\"}},";
			}
			if(remediationActivity.contains("linkCommenter")){
				remediation = remediation+"{\"code\":\"SHARE_ACCESS\",\"meta_info\":{\"access\":\"open-withlink-commenter\"}},";
			}
			if(remediationActivity.contains("companyWriter")){
				remediation = remediation+"{\"code\":\"SHARE_ACCESS\",\"meta_info\":{\"access\":\"company-writer\"}},";
			}
			if(remediationActivity.contains("companyReader")){
				remediation = remediation+"{\"code\":\"SHARE_ACCESS\",\"meta_info\":{\"access\":\"company-reader\"}},";
			}
			if(remediationActivity.contains("companyCommenter")){
				remediation = remediation+"{\"code\":\"SHARE_ACCESS\",\"meta_info\":{\"access\":\"company-commenter\"}},";
			}
			if(remediationActivity.contains("companyLinkWriter")){
				remediation = remediation+"{\"code\":\"SHARE_ACCESS\",\"meta_info\":{\"access\":\"company-withlink-writer\"}},";
			}
			if(remediationActivity.contains("companyLinkReader")){
				remediation = remediation+"{\"code\":\"SHARE_ACCESS\",\"meta_info\":{\"access\":\"company-withlink-reader\"}},";
			}
			if(remediationActivity.contains("companyLinkCommenter")){
				remediation = remediation+"{\"code\":\"SHARE_ACCESS\",\"meta_info\":{\"access\":\"company-withlink-commenter\"}},";
			}
			
			if(remediationActivity.contains("updateWriter")){
				remediation = remediation+"{\"code\":\"READ_ONLY\",\"meta_info\":{\"role\":\"writer\",\"collabs\":[]}},";
			}
			if(remediationActivity.contains("updateCommenter")){
				remediation = remediation+"{\"code\":\"READ_ONLY\",\"meta_info\":{\"role\":\"commenter\",\"collabs\":[]}},";
			}
			if(remediationActivity.contains("updateReader")){
				remediation = remediation+"{\"code\":\"READ_ONLY\",\"meta_info\":{\"role\":\"reader\",\"collabs\":[]}},";
			}
			if(remediationActivity.contains("preventWritersCanShare")){
				remediation = remediation+"{\"code\":\"ACCESS_RESTRICTION\",\"possible_values\":[\"copy-print-download\",\"writers-can-share\"],\"meta_info\":{\"access\":\"writers-can-share\",\"collabs\":[]}},";
			}
			if(remediationActivity.contains("preventCopyPrintDownload")){
				remediation = remediation+"{\"code\":\"ACCESS_RESTRICTION\",\"possible_values\":[\"copy-print-download\",\"writers-can-share\"],\"meta_info\":{\"access\":\"copy-print-download\",\"collabs\":[]}},";
			}
			}
		}else if(appname.equalsIgnoreCase("Gmail")){
			if(remActivities.contains("moveToTrash")){
				remediation = remediation+"{\"code\":\"ITEM_TRASH_MAIL\",\"meta_info\":{}},";
			}
		}else if(appname.equalsIgnoreCase("Office 365")){

			String[] remediationActivities = remActivities.split(",");
			
			for (String remediationActivity : remediationActivities) {			
			
            if(remediationActivity.contains("unshare")){
                remediation = remediation+"{\"code\":\"UNSHARE\",\"meta_info\":{\"current_link\":\"unicode\"}},";
            }
            if(remediationActivity.contains("everyoneRead")){
            	remediation = remediation+"{\"code\":\"SHARE_ACCESS\",\"meta_info\":{\"access\":\"Everyone-Read\",\"current_link\":\"unicode\"}},";
            }
            if(remediationActivity.contains("readExceptExternal")){
            	remediation = remediation+"{\"code\":\"SHARE_ACCESS\",\"meta_info\":{\"access\":\"Everyone except external users-Read\",\"current_link\":\"unicode\"}},";
            }
            if(remediationActivity.contains("everyoneContribute")){
            	remediation = remediation+"{\"code\":\"SHARE_ACCESS\",\"meta_info\":{\"access\":\"Everyone-Contribute\",\"current_link\":\"unicode\"}},";
            }
            if(remediationActivity.contains("contributeExceptExternal")){
            	remediation = remediation+"{\"code\":\"SHARE_ACCESS\",\"meta_info\":{\"access\":\"Everyone except external users-Contribute\",\"current_link\":\"unicode\"}},";
            }
            if(remediationActivity.contains("everyoneDesign")) {
            	remediation = remediation+"{\"code\":\"SHARE_ACCESS\",\"meta_info\":{\"access\":\"Everyone-Design\",\"current_link\":\"unicode\"}},";
            }
            if(remediationActivity.contains("designExceptExternal")) {
            	remediation = remediation+"{\"code\":\"SHARE_ACCESS\",\"meta_info\":{\"access\":\"Everyone except external users-Design\",\"current_link\":\"unicode\"}},";
            }
            if(remediationActivity.contains("everyoneEdit")) {
            	remediation = remediation+"{\"code\":\"SHARE_ACCESS\",\"meta_info\":{\"access\":\"Everyone-Edit\",\"current_link\":\"unicode\"}},";
            }
            if(remediationActivity.contains("editExceptExternal")) {
            	remediation = remediation+"{\"code\":\"SHARE_ACCESS\",\"meta_info\":{\"access\":\"Everyone except external users-Edit\",\"current_link\":\"unicode\"}},";
            }
            if(remediationActivity.contains("everyoneFullControl")) {
            	remediation = remediation+"{\"code\":\"SHARE_ACCESS\",\"meta_info\":{\"access\":\"Everyone-Full Control\",\"current_link\":\"unicode\"}},";
            }
            if(remediationActivity.contains("fullcontrolExceptExternal")) {
            	remediation = remediation+"{\"code\":\"SHARE_ACCESS\",\"meta_info\":{\"access\":\"Everyone except external users-Full Control\",\"current_link\":\"unicode\"}},";
            }
            
            if(remediationActivity.contains("removeCollab")){
            	remediation = remediation+"{\"code\":\"COLLAB_REMOVE\",\"meta_info\":{\"collabs\":[]}},";
            }
            if(remediationActivity.contains("updateCollabRead")){
            	remediation = remediation+"{\"code\":\"COLLAB_UPDATE\",\"meta_info\":{\"role\":\"Read\",\"collabs\":[]}},";
            }
            if(remediationActivity.contains("updateCollabContribute")){
            	remediation = remediation+"{\"code\":\"COLLAB_UPDATE\",\"meta_info\":{\"role\":\"Contribute\",\"collabs\":[]}},";
            }
            if(remediationActivity.contains("updateCollabDesign")){
            	remediation = remediation+"{\"code\":\"COLLAB_UPDATE\",\"meta_info\":{\"role\":\"Design\",\"collabs\":[]}},";
            }
            if(remediationActivity.contains("updateCollabEdit")){
            	remediation = remediation+"{\"code\":\"COLLAB_UPDATE\",\"meta_info\":{\"role\":\"Edit\",\"collabs\":[]}},";
            }
            if(remediationActivity.contains("updateCollabFullControl")){
            	remediation = remediation+"{\"code\":\"COLLAB_UPDATE\",\"meta_info\":{\"role\":\"Full Control\",\"collabs\":[]}},";
            }            
            if(remediationActivity.contains("deleteUniquePermissions")){
            	remediation = remediation+"{\"code\":\"RESET_PERMISSIONS\",\"meta_info\":{}},";
            }
			}
        } else if(appname.equalsIgnoreCase("Office 365 Email")){
            if(remActivities.contains("deleteEmail")){
            	remediation = remediation+"{\"code\":\"ITEM_DELETE_MAIL\",\"meta_info\":{}},";
            }
        } else if(appname.equalsIgnoreCase("salesforce")){
        	
        	if(remActivities.contains("deleteSharedLink")){
        		remediation = remediation+"{\"code\":\"UNSHARE\",\"meta_info\":{\"current_link\":\"unicode\",\"object_type\":\"Chatter\"}},";
        	}
        	if(remActivities.contains("internalUserCollaborate")){
        		remediation = remediation+"{\"code\":\"SHARE_ACCESS\",\"meta_info\":{\"access\":\"company-C\",\"current_link\":\"unicode\",\"object_type\":\"Chatter\"}},";
        	}
        	if(remActivities.contains("internalUserView")){
        		remediation = remediation+"{\"code\":\"SHARE_ACCESS\",\"meta_info\":{\"access\":\"company-V\",\"current_link\":\"unicode\",\"object_type\":\"Chatter\"}},";
        	}
        	if(remActivities.contains("removeCollaborator")){
        		remediation = remediation+"{\"code\":\"COLLAB_REMOVE\",\"meta_info\":{\"collabs\":[]}},";
        	}
        	if(remActivities.contains("updateCollaboratorCollaborate")){
        		remediation = remediation+"{\"code\":\"COLLAB_UPDATE\",\"meta_info\":{\"role\":\"C\",\"collabs\":[],\"object_type\":\"Chatter\"}},";
        	}
        	if(remActivities.contains("updateCollaboratorView")){
        		remediation = remediation+"{\"code\":\"COLLAB_UPDATE\",\"meta_info\":{\"role\":\"V\",\"collabs\":[],\"object_type\":\"Chatter\"}},";
        	}
        	
        } else if(appname.equalsIgnoreCase("Office 365 Site")){
            if(remActivities.contains("removeShare")){
            	remediation = remediation+"{\"code\":\"UNSHARE\",\"meta_info\":{\"current_link\":\"unicode\"}},";
            }
            if(remActivities.contains("removeCollab")){
            	remediation = remediation+"{\"code\":\"COLLAB_REMOVE\",\"meta_info\":{\"collabs\":[]}},";
            }
            if(remActivities.contains("deletePermissions")){
            	remediation = remediation+"{\"code\":\"RESET_PERMISSIONS\",\"meta_info\":{}},";
            }
            if(remActivities.contains("everyoneRead")){
            	remediation = remediation+"{\"code\":\"SHARE_ACCESS\",\"meta_info\":{\"access\":\"Everyone-Read\",\"current_link\":\"unicode\"}},";
            }
            if(remActivities.contains("everyoneContribute")){
            	remediation = remediation+"{\"code\":\"SHARE_ACCESS\",\"meta_info\":{\"access\":\"Everyone-Contribute\",\"current_link\":\"unicode\"}},";
            }
            if(remActivities.contains("everyoneDesign")){
            	remediation = remediation+"{\"code\":\"SHARE_ACCESS\",\"meta_info\":{\"access\":\"Everyone-Design\",\"current_link\":\"unicode\"}},";
            }
            if(remActivities.contains("everyoneEdit")){
            	remediation = remediation+"{\"code\":\"SHARE_ACCESS\",\"meta_info\":{\"access\":\"Everyone-Edit\",\"current_link\":\"unicode\"}},";
            }
            if(remActivities.contains("everyoneFullControl")){
            	remediation = remediation+"{\"code\":\"SHARE_ACCESS\",\"meta_info\":{\"access\":\"Everyone-Full Control\",\"current_link\":\"unicode\"}},";
            }
            if(remActivities.contains("everyoneExceptExternalUserRead")){
            	remediation = remediation+"{\"code\":\"SHARE_ACCESS\",\"meta_info\":{\"access\":\"Everyone except external users-Read\",\"current_link\":\"unicode\"}},";
            }
            if(remActivities.contains("everyoneExceptExternalUserContribute")){
            	remediation = remediation+"{\"code\":\"SHARE_ACCESS\",\"meta_info\":{\"access\":\"Everyone except external users-Contribute\",\"current_link\":\"unicode\"}},";
            }
            if(remActivities.contains("everyoneExceptExternalUserDesign")){
            	remediation = remediation+"{\"code\":\"SHARE_ACCESS\",\"meta_info\":{\"access\":\"Everyone except external users-Design\",\"current_link\":\"unicode\"}},";
            }
            if(remActivities.contains("everyoneExceptExternalUserEdit")){
            	remediation = remediation+"{\"code\":\"SHARE_ACCESS\",\"meta_info\":{\"access\":\"Everyone except external users-Edit\",\"current_link\":\"unicode\"}},";
            }
            if(remActivities.contains("everyoneExceptExternalUserFullControl")){
            	remediation = remediation+"{\"code\":\"SHARE_ACCESS\",\"meta_info\":{\"access\":\"Everyone except external users-Full Control\",\"current_link\":\"unicode\"}},";
            }
            if(remActivities.contains("updateCollabRead")){
            	remediation = remediation+"{\"code\":\"COLLAB_UPDATE\",\"meta_info\":{\"role\":\"Read\",\"collabs\":[]}},";
            }
            if(remActivities.contains("updateCollabContribute")){
            	remediation = remediation+"{\"code\":\"COLLAB_UPDATE\",\"meta_info\":{\"role\":\"Contribute\",\"collabs\":[]}},";
            }
            if(remActivities.contains("updateCollabDesign")){
            	remediation = remediation+"{\"code\":\"COLLAB_UPDATE\",\"meta_info\":{\"role\":\"Design\",\"collabs\":[]}},";
            }
            if(remActivities.contains("updateCollabEdit")){
            	remediation = remediation+"{\"code\":\"COLLAB_UPDATE\",\"meta_info\":{\"role\":\"Edit\",\"collabs\":[]}},";
            }
            if(remActivities.contains("updateCollabFullControl")){
            	remediation = remediation+"{\"code\":\"COLLAB_UPDATE\",\"meta_info\":{\"role\":\"Full Control\",\"collabs\":[]}},";
            }
        }
		if(!remediation.equals("")){
			remediation = remediation.substring(0, remediation.length()-1);
		}
		return remediation;
	}
	

	public String generateEditDataExposurePolicyPayload(Client restClient, PolicyBean policyBean, List<NameValuePair> requestHeader, TestSuiteDTO suiteData, String policyId) throws IOException{
		String payload = null;
		String policyRemediation = this.addRemediation(policyBean.getCloudService(), policyBean.getRemediationActivity());
		List<String> fileOwnerUser = new ArrayList<String>();
		List<String> fileOwnerGroup = new ArrayList<String>();
		List<String> domainName = new ArrayList<String>();
		if((policyBean.getFileOwnerUser().equalsIgnoreCase("Any") && policyBean.getFileOwnerGroup().equalsIgnoreCase("Any")) && policyBean.getDomainName().equalsIgnoreCase("Any")){
			fileOwnerUser.add("__ALL_EL__");
			fileOwnerGroup.add("__ALL_EL__");
			domainName.add("__ALL_EL__");
		}else{
			if(policyBean.getFileOwnerUser().equalsIgnoreCase("Any")){
				fileOwnerUser.add("");
			}else{
				if(policyBean.getFileOwnerUser().contains(",")){
					String[] users = policyBean.getFileOwnerUser().split(",");
					for(int i=0;i<users.length;i++){
						fileOwnerUser.add(users[i]);
					}
				}else{
					fileOwnerUser.add(policyBean.getFileOwnerUser());
				}
			}
			if(policyBean.getFileOwnerGroup().equalsIgnoreCase("Any")){
				fileOwnerGroup.add("");
			}else{
				if(policyBean.getFileOwnerGroup().contains(",")){
					String[] groups = policyBean.getFileOwnerGroup().split(",");
					for(int i=0;i<groups.length;i++){
						fileOwnerGroup.add(groups[i]);
					}
				}else{
					fileOwnerGroup.add(policyBean.getFileOwnerGroup());
				}
				
			}
			if(policyBean.getDomainName().equalsIgnoreCase("Any")){
				domainName.add("__ALL_EL__");
			}else{
				if(policyBean.getDomainName().contains(",")){
					String[] domains = policyBean.getDomainName().split(",");
					for(int i=0;i<domains.length;i++){
						domainName.add(domains[i]);
					}
				}else{
					domainName.add(policyBean.getDomainName());
				}
			}
		}
		
		List<String> fileOwnerUserException = new ArrayList<String>();
		if(policyBean.getFileOwnerUserException().equalsIgnoreCase("No")){
			fileOwnerUserException.add("");
		}else{
			if(policyBean.getFileOwnerUserException().contains(",")){
				String[] userException = policyBean.getFileOwnerUserException().split(",");
				for(int i=0;i<userException.length;i++){
					fileOwnerUserException.add(userException[i]);
				}
			}else{
				fileOwnerUserException.add(policyBean.getFileOwnerUserException());
			}
		}
		
		List<String> fileOwnerGroupException = new ArrayList<String>();
		if(policyBean.getFileOwnerGroupException().equalsIgnoreCase("No")){
			fileOwnerGroupException.add("");
		}else{
			if(policyBean.getFileOwnerGroupException().contains(",")){
				String[] groupException = policyBean.getFileOwnerGroupException().split(",");
				for(int i=0;i<groupException.length;i++){
					fileOwnerGroupException.add(groupException[i]);
				}
			}else{
				fileOwnerGroupException.add(policyBean.getFileOwnerGroupException());
			}
		}
		
		List<String> sharedWithUser = new ArrayList<String>();
		List<String> sharedWithGroup = new ArrayList<String>();
		if((policyBean.getSharedWithUser().equalsIgnoreCase("Any") && policyBean.getSharedWithGroup().equalsIgnoreCase("Any"))){
			sharedWithUser.add("__ALL_EL__"); 
			sharedWithGroup.add("__ALL_EL__");
		}else{
			if(policyBean.getSharedWithUser().equalsIgnoreCase("Any")){
				sharedWithUser.add(""); 
			}else{
				if(policyBean.getSharedWithUser().contains(",")){
					String[] sharedUser = policyBean.getSharedWithUser().split(",");
					for(int i=0;i<sharedUser.length;i++){
						sharedWithUser.add(sharedUser[i]);
					}
				}else{
					sharedWithUser.add(policyBean.getSharedWithUser());
				}
			}
			if(policyBean.getSharedWithGroup().equalsIgnoreCase("Any")){
				sharedWithGroup.add("");
			}else{
				if(policyBean.getSharedWithGroup().contains(",")){
					String[] sharedGroup = policyBean.getSharedWithGroup().split(",");
					for(int i=0;i<sharedGroup.length;i++){
						sharedWithGroup.add(sharedGroup[i]);
					}
				}else{
					sharedWithGroup.add(policyBean.getSharedWithGroup());
				}
			}
		}
		
		List<String> sharedWithUserException = new ArrayList<String>();
		if(policyBean.getSharedWithUserException().equalsIgnoreCase("No")){
			sharedWithUserException.add("");
		}else{
			if(policyBean.getSharedWithUserException().contains(",")){
				String[] sharedUserException = policyBean.getSharedWithUserException().split(",");
				for(int i=0;i<sharedUserException.length;i++){
					sharedWithUserException.add(sharedUserException[i]);
				}
			}else{
				sharedWithUserException.add(policyBean.getSharedWithUserException());
			}
		}
		
		List<String> sharedWithGroupException = new ArrayList<String>();
		if(policyBean.getSharedWithGroupException().equalsIgnoreCase("No")){
			sharedWithGroupException.add("");
		}else{
			if(policyBean.getSharedWithGroupException().contains(",")){
				String[] sharedGroupException = policyBean.getSharedWithGroupException().split(",");
				for(int i=0;i<sharedGroupException.length;i++){
					sharedWithGroupException.add(sharedGroupException[i]);
				}
			}else{
				sharedWithGroupException.add(policyBean.getSharedWithGroupException());
			}
		}
		
		List<String> fileNameList = new ArrayList<String>();
		if(policyBean.getFileName().equalsIgnoreCase("No")){
			fileNameList.add("");
		}else{
			if(policyBean.getFileName().contains(",")){
				String[] fileNameArray = policyBean.getFileName().split(",");
				for(int i=0;i<fileNameArray.length;i++){
					fileNameList.add(fileNameArray[i]);
				}
			}else{
				fileNameList.add(policyBean.getFileName());
			}
		}
		List<String> fileExceptionList = new ArrayList<String>();
		fileExceptionList.add("");
		
		List<String> exposureList = new ArrayList<String>();
		String exposureMatch = null;
		if(policyBean.getExposureType().contains(",")){
			String[] exposureArray = policyBean.getExposureType().split(",");
			for(int i=0;i<exposureArray.length;i++){
				if(!exposureArray[i].contains("all") || !exposureArray[i].contains("any")){
					exposureList.add(exposureArray[i]);
				}else{
					exposureMatch = exposureArray[i];
				}
			}
		}else{
			exposureList.add(policyBean.getExposureType());
		}
		if(exposureMatch == null){
			exposureMatch = "any";
		}
		
		ArrayList<String> risksList = new ArrayList<String>();
		if(!policyBean.getRiskType().equalsIgnoreCase("No")){
			if(policyBean.getRiskType().contains(",")){
				String[] risksArray = policyBean.getRiskType().split(",");
				for(int i=0;i<risksArray.length;i++){
					risksList.add(risksArray[i]);
				}
			}else{
				risksList.add(policyBean.getRiskType());
			}
		}
		
		List<String> contentIQList = new ArrayList<String>();
		if(policyBean.getCiqProfile().equalsIgnoreCase("No")){
			contentIQList.add("");
		}else{
			if(policyBean.getCiqProfile().contains(",")){
				String[] contentIQs = policyBean.getCiqProfile().split(",");
				for(int i=0;i<contentIQs.length;i++){
					contentIQList.add(contentIQs[i]);
				}
			}else{
				contentIQList.add(policyBean.getCiqProfile());
			}
		}
		
		List<String> cloudServiceList = new ArrayList<String>();
		List<String> cloudServiceSubfeature = new ArrayList<String>();
		if(policyBean.getCloudService().equalsIgnoreCase("Office 365")){
			cloudServiceList.add("Office 365");
			cloudServiceSubfeature.add("OneDrive");
		}else if(policyBean.getCloudService().equalsIgnoreCase("Office 365 Email")){
			cloudServiceList.add("Office 365");
			cloudServiceSubfeature.add("Mail");
		}else if(policyBean.getCloudService().equalsIgnoreCase("Google Drive")){
			cloudServiceList.add("Google Apps");
			cloudServiceSubfeature.add("Drive");
		}else if(policyBean.getCloudService().equalsIgnoreCase("Gmail")){
			cloudServiceList.add("Google Apps");
			cloudServiceSubfeature.add("Mail");
		}else{
			cloudServiceList.add(policyBean.getCloudService());
			cloudServiceSubfeature.add("");
		}
		
		int largerSize;
		int smallerSize;
		String[] fileSizes = policyBean.getFileSize().split(",");
		largerSize = Integer.parseInt(fileSizes[0]);
		smallerSize = Integer.parseInt(fileSizes[1]);
		ProtectQueryBuilder protectQueryBuilder = new ProtectQueryBuilder(); 
		payload = protectQueryBuilder.getESQueryForDataExposureEditPolicy(policyBean.getPolicyName(), fileOwnerUser, fileOwnerGroup, domainName, fileOwnerUserException, fileOwnerGroupException, sharedWithUser, sharedWithGroup, sharedWithUserException, sharedWithGroupException, cloudServiceList, cloudServiceSubfeature, contentIQList, fileNameList, fileExceptionList, risksList, null, exposureList, largerSize, smallerSize, exposureMatch, policyId);
		payload = payload.replace("\"remediation\"", policyRemediation);
		payload = payload.replace("\"\"", "");

		return payload;
	}
	
	@SuppressWarnings("null")
	public String generatePayloadForDataExposurePolicy(PolicyBean policyBean, TestSuiteDTO suiteData) throws IOException{
		String payload = null;
		String policyRemediation = this.addRemediation(policyBean.getCloudService(), policyBean.getRemediationActivity());
		List<String> fileOwnerUser = new ArrayList<String>();
		List<String> fileOwnerGroup = new ArrayList<String>();
		List<String> domainName = new ArrayList<String>();
		if((policyBean.getFileOwnerUser().equalsIgnoreCase("Any") && policyBean.getFileOwnerGroup().equalsIgnoreCase("Any")) && policyBean.getDomainName().equalsIgnoreCase("Any")){
			fileOwnerUser.add("__ALL_EL__");
			fileOwnerGroup.add("__ALL_EL__");
			domainName.add("__ALL_EL__");
		}else{
			if(policyBean.getFileOwnerUser().equalsIgnoreCase("Any")){
				fileOwnerUser.add("");
			}else{
				if(policyBean.getFileOwnerUser().contains(",")){
					String[] users = policyBean.getFileOwnerUser().split(",");
					for(int i=0;i<users.length;i++){
						fileOwnerUser.add(users[i]);
					}
				}else{
					fileOwnerUser.add(policyBean.getFileOwnerUser());
				}
			}
			if(policyBean.getFileOwnerGroup().equalsIgnoreCase("Any")){
				fileOwnerGroup.add("");
			}else{
				if(policyBean.getFileOwnerGroup().contains(",")){
					String[] groups = policyBean.getFileOwnerGroup().split(",");
					for(int i=0;i<groups.length;i++){
						fileOwnerGroup.add(groups[i]);
					}
				}else{
					fileOwnerGroup.add(policyBean.getFileOwnerGroup());
				}
				
			}
			if(policyBean.getDomainName().equalsIgnoreCase("Any")){
				domainName.add("__ALL_EL__");
			}else{
				if(policyBean.getDomainName().contains(",")){
					String[] domains = policyBean.getDomainName().split(",");
					for(int i=0;i<domains.length;i++){
						domainName.add(domains[i]);
					}
				}else{
					domainName.add(policyBean.getDomainName());
				}
			}
		}
		
		List<String> fileOwnerUserException = new ArrayList<String>();
		if(policyBean.getFileOwnerUserException().equalsIgnoreCase("No")){
			fileOwnerUserException.add("");
		}else{
			if(policyBean.getFileOwnerUserException().contains(",")){
				String[] userException = policyBean.getFileOwnerUserException().split(",");
				for(int i=0;i<userException.length;i++){
					fileOwnerUserException.add(userException[i]);
				}
			}else{
				fileOwnerUserException.add(policyBean.getFileOwnerUserException());
			}
		}
		
		List<String> fileOwnerGroupException = new ArrayList<String>();
		if(policyBean.getFileOwnerGroupException().equalsIgnoreCase("No")){
			fileOwnerGroupException.add("");
		}else{
			if(policyBean.getFileOwnerGroupException().contains(",")){
				String[] groupException = policyBean.getFileOwnerGroupException().split(",");
				for(int i=0;i<groupException.length;i++){
					fileOwnerGroupException.add(groupException[i]);
				}
			}else{
				fileOwnerGroupException.add(policyBean.getFileOwnerGroupException());
			}
		}
		
		List<String> sharedWithUser = new ArrayList<String>();
		List<String> sharedWithGroup = new ArrayList<String>();
		if((policyBean.getSharedWithUser().equalsIgnoreCase("Any") && policyBean.getSharedWithGroup().equalsIgnoreCase("Any"))){
			sharedWithUser.add("__ALL_EL__"); 
			sharedWithGroup.add("__ALL_EL__");
		}else{
			if(policyBean.getSharedWithUser().equalsIgnoreCase("Any")){
				sharedWithUser.add(""); 
			}else{
				if(policyBean.getSharedWithUser().contains(",")){
					String[] sharedUser = policyBean.getSharedWithUser().split(",");
					for(int i=0;i<sharedUser.length;i++){
						sharedWithUser.add(sharedUser[i]);
					}
				}else{
					sharedWithUser.add(policyBean.getSharedWithUser());
				}
			}
			if(policyBean.getSharedWithGroup().equalsIgnoreCase("Any")){
				sharedWithGroup.add("");
			}else{
				if(policyBean.getSharedWithGroup().contains(",")){
					String[] sharedGroup = policyBean.getSharedWithGroup().split(",");
					for(int i=0;i<sharedGroup.length;i++){
						sharedWithGroup.add(sharedGroup[i]);
					}
				}else{
					sharedWithGroup.add(policyBean.getSharedWithGroup());
				}
			}
		}
		
		List<String> sharedWithUserException = new ArrayList<String>();
		if(policyBean.getSharedWithUserException().equalsIgnoreCase("No")){
			sharedWithUserException.add("");
		}else{
			if(policyBean.getSharedWithUserException().contains(",")){
				String[] sharedUserException = policyBean.getSharedWithUserException().split(",");
				for(int i=0;i<sharedUserException.length;i++){
					sharedWithUserException.add(sharedUserException[i]);
				}
			}else{
				sharedWithUserException.add(policyBean.getSharedWithUserException());
			}
		}
		
		List<String> sharedWithGroupException = new ArrayList<String>();
		if(policyBean.getSharedWithGroupException().equalsIgnoreCase("No")){
			sharedWithGroupException.add("");
		}else{
			if(policyBean.getSharedWithGroupException().contains(",")){
				String[] sharedGroupException = policyBean.getSharedWithGroupException().split(",");
				for(int i=0;i<sharedGroupException.length;i++){
					sharedWithGroupException.add(sharedGroupException[i]);
				}
			}else{
				sharedWithGroupException.add(policyBean.getSharedWithGroupException());
			}
		}
		
		List<String> fileNameList = new ArrayList<String>();
		if(policyBean.getFileName().equalsIgnoreCase("No")){
			fileNameList.add("");
		}else{
			if(policyBean.getFileName().contains(",")){
				String[] fileNameArray = policyBean.getFileName().split(",");
				for(int i=0;i<fileNameArray.length;i++){
					fileNameList.add(fileNameArray[i]);
				}
			}else{
				fileNameList.add(policyBean.getFileName());
			}
		}
		List<String> fileExceptionList = new ArrayList<String>();
		fileExceptionList.add("");
		
		List<String> fileTypeList = new ArrayList<String>();
		
/*		if (!(suiteData.getEnvironmentName().equalsIgnoreCase("eoe")
				|| suiteData.getEnvironmentName().equalsIgnoreCase("cep") || suiteData
				.getEnvironmentName().equalsIgnoreCase("friends"))) {

			if (policyBean.getFileType().equalsIgnoreCase("No")) {
				fileTypeList.add("");
			} else {
				if (policyBean.getFileExt().contains(",")) {
					String[] fileTypeArray = policyBean.getFileExt().split(",");
					for (int i = 0; i < fileTypeArray.length; i++) {
						fileTypeList.add(fileTypeArray[i]);
					}
				} else {
					fileTypeList.add(policyBean.getFileExt());
				}
			}
		} else {
			fileTypeList.add("__ALL_EL__");
		}*/
		fileTypeList.add("__ALL_EL__");
		
		
		
		List<String> exposureList = new ArrayList<String>();
		String exposureMatch = null;
		if(policyBean.getExposureType().contains(",")){
			String[] exposureArray = policyBean.getExposureType().split(",");
			for(int i=0;i<exposureArray.length;i++){
				if(!(exposureArray[i].contains("all") || exposureArray[i].contains("any"))){
					exposureList.add(exposureArray[i]);
				}else{
					exposureMatch = exposureArray[i];
				}
			}
		}else{
			exposureList.add(policyBean.getExposureType());
		}
		if(exposureMatch == null){
			exposureMatch = "any";
		}
		
		ArrayList<String> risksList = new ArrayList<String>();
		if(!policyBean.getRiskType().equalsIgnoreCase("No")){
			if(policyBean.getRiskType().contains(",")){
				String[] risksArray = policyBean.getRiskType().split(",");
				for(int i=0;i<risksArray.length;i++){
					risksList.add(risksArray[i]);
				}
			} else{
				risksList.add(policyBean.getRiskType());
			}
		} else if (!policyBean.getCiqProfile().equalsIgnoreCase("no")) {
			risksList.add(policyBean.getCiqProfile());
		} else if (policyBean.getFileFormat() != null) {
			risksList.add(policyBean.getFileFormat());
		}
		
		List<String> contentIQList = new ArrayList<String>(risksList);
		risksList.clear();
		
		//List<String> contentIQList = new ArrayList<String>();
/*		if(policyBean.getRiskType().equalsIgnoreCase("No")){
			contentIQList.add("");
		}else{
			if(policyBean.getRiskType().contains(",")){
				String[] contentIQs = policyBean.getRiskType().split(",");
				for(int i=0;i<contentIQs.length;i++){
					contentIQList.add(contentIQs[i]);
				}
			}else{
				//contentIQList.add(policyBean.getRiskType());
			}
		}*/
		
		List<String> notifySmsList = new ArrayList<String>();
		if(policyBean.getNotifySms() == null){
			//notifySmsList.add("");
		}else{
			if(policyBean.getNotifySms().contains(",")){
				String[] notifySmsArray = policyBean.getNotifySms().split(",");
				for(int i=0;i<notifySmsArray.length;i++){
					notifySmsList.add(notifySmsArray[i]);
				}
			}else{
				notifySmsList.add(policyBean.getNotifySms());
			}
		}
		
		List<String> ciqExceptionList = new ArrayList<String>();
		if(policyBean.getCiqProfileException() == null || policyBean.getCiqProfileException().equalsIgnoreCase("No")){
			ciqExceptionList.add("");
		}else{
			if(policyBean.getCiqProfileException().contains(",")){
				String[] ciqExceptionArray = policyBean.getFileOwnerUserException().split(",");
				for(int i=0;i<ciqExceptionArray.length;i++){
					ciqExceptionList.add(ciqExceptionArray[i]);
				}
			}else{
				ciqExceptionList.add(policyBean.getCiqProfileException());
			}
		}
		
		
		List<String> cloudServiceList = new ArrayList<String>();
		List<String> cloudServiceSubfeature = new ArrayList<String>();
		if(policyBean.getCloudService().equalsIgnoreCase("Office 365")){
			cloudServiceList.add("Office 365");
			cloudServiceSubfeature.add("OneDrive");
		}else if(policyBean.getCloudService().equalsIgnoreCase("Office 365 Email")){
			cloudServiceList.add("Office 365");
			cloudServiceSubfeature.add("Mail");
		}else if(policyBean.getCloudService().equalsIgnoreCase("Google Drive")){
			cloudServiceList.add("Google Apps");
			cloudServiceSubfeature.add("Drive");
		}else if(policyBean.getCloudService().equalsIgnoreCase("Gmail")){
			cloudServiceList.add("Google Apps");
			cloudServiceSubfeature.add("Mail");
		}else{
			cloudServiceList.add(policyBean.getCloudService());
			cloudServiceSubfeature.add("");
		}
		
		int largerSize;
		int smallerSize;
		String[] fileSizes = policyBean.getFileSize().split(",");
		largerSize = Integer.parseInt(fileSizes[0]);
		smallerSize = Integer.parseInt(fileSizes[1]);
		String severity = policyBean.getSeverity();
		String policyDesc = policyBean.getPolicyDesc();
		ProtectQueryBuilder protectQueryBuilder = new ProtectQueryBuilder(); 

		payload = protectQueryBuilder.getESQueryForDataExposurePolicy(policyBean.getPolicyName(), fileOwnerUser, fileOwnerGroup, domainName, fileOwnerUserException, fileOwnerGroupException, sharedWithUser, sharedWithGroup, sharedWithUserException, sharedWithGroupException, cloudServiceList, cloudServiceSubfeature, contentIQList, fileNameList, fileExceptionList, risksList, fileTypeList, exposureList, largerSize, smallerSize, exposureMatch, severity, notifySmsList, ciqExceptionList);
/*		if (suiteData.getEnvironmentName().equalsIgnoreCase("eoe") || suiteData.getEnvironmentName().equalsIgnoreCase("cep") || suiteData.getEnvironmentName().equalsIgnoreCase("friends"))
			payload = protectQueryBuilder.getESQueryForDataExposurePolicyTemp(policyBean.getPolicyName(), fileOwnerUser, fileOwnerGroup, domainName, fileOwnerUserException, fileOwnerGroupException, sharedWithUser, sharedWithGroup, sharedWithUserException, sharedWithGroupException, cloudServiceList, cloudServiceSubfeature, contentIQList, fileNameList, fileExceptionList, risksList, fileTypeList, exposureList, largerSize, smallerSize, exposureMatch, severity, notifySmsList);
		else
			payload = protectQueryBuilder.getESQueryForDataExposurePolicy(policyBean.getPolicyName(), fileOwnerUser, fileOwnerGroup, domainName, fileOwnerUserException, fileOwnerGroupException, sharedWithUser, sharedWithGroup, sharedWithUserException, sharedWithGroupException, cloudServiceList, cloudServiceSubfeature, contentIQList, fileNameList, fileExceptionList, risksList, fileTypeList, exposureList, largerSize, smallerSize, exposureMatch, severity, notifySmsList);*/
		
		
		payload = payload.replace("\"remediation\"", policyRemediation);
		payload = payload.replace("\"Policy Desc\"", "\""+policyDesc+"\"");
		payload = payload.replace("\"\"", "");

		return payload;
	}
	
	public String generatePayloadForDataExposurePolicyWithCIQ(PolicyBean policyBean, TestSuiteDTO suiteData) throws IOException{
		String payload = null;
		String policyRemediation = this.addRemediation(policyBean.getCloudService(), policyBean.getRemediationActivity());
		List<String> fileOwnerUser = new ArrayList<String>();
		List<String> fileOwnerGroup = new ArrayList<String>();
		List<String> domainName = new ArrayList<String>();
		if((policyBean.getFileOwnerUser().equalsIgnoreCase("Any") && policyBean.getFileOwnerGroup().equalsIgnoreCase("Any")) && policyBean.getDomainName().equalsIgnoreCase("Any")){
			fileOwnerUser.add("__ALL_EL__");
			fileOwnerGroup.add("__ALL_EL__");
			domainName.add("__ALL_EL__");
		}else{
			if(policyBean.getFileOwnerUser().equalsIgnoreCase("Any")){
				fileOwnerUser.add("");
			}else{
				if(policyBean.getFileOwnerUser().contains(",")){
					String[] users = policyBean.getFileOwnerUser().split(",");
					for(int i=0;i<users.length;i++){
						fileOwnerUser.add(users[i]);
					}
				}else{
					fileOwnerUser.add(policyBean.getFileOwnerUser());
				}
			}
			if(policyBean.getFileOwnerGroup().equalsIgnoreCase("Any")){
				fileOwnerGroup.add("");
			}else{
				if(policyBean.getFileOwnerGroup().contains(",")){
					String[] groups = policyBean.getFileOwnerGroup().split(",");
					for(int i=0;i<groups.length;i++){
						fileOwnerGroup.add(groups[i]);
					}
				}else{
					fileOwnerGroup.add(policyBean.getFileOwnerGroup());
				}
				
			}
			if(policyBean.getDomainName().equalsIgnoreCase("Any")){
				domainName.add("__ALL_EL__");
			}else{
				if(policyBean.getDomainName().contains(",")){
					String[] domains = policyBean.getDomainName().split(",");
					for(int i=0;i<domains.length;i++){
						domainName.add(domains[i]);
					}
				}else{
					domainName.add(policyBean.getDomainName());
				}
			}
		}
		
		List<String> fileOwnerUserException = new ArrayList<String>();
		if(policyBean.getFileOwnerUserException().equalsIgnoreCase("No")){
			fileOwnerUserException.add("");
		}else{
			if(policyBean.getFileOwnerUserException().contains(",")){
				String[] userException = policyBean.getFileOwnerUserException().split(",");
				for(int i=0;i<userException.length;i++){
					fileOwnerUserException.add(userException[i]);
				}
			}else{
				fileOwnerUserException.add(policyBean.getFileOwnerUserException());
			}
		}
		
		List<String> fileOwnerGroupException = new ArrayList<String>();
		if(policyBean.getFileOwnerGroupException().equalsIgnoreCase("No")){
			fileOwnerGroupException.add("");
		}else{
			if(policyBean.getFileOwnerGroupException().contains(",")){
				String[] groupException = policyBean.getFileOwnerGroupException().split(",");
				for(int i=0;i<groupException.length;i++){
					fileOwnerGroupException.add(groupException[i]);
				}
			}else{
				fileOwnerGroupException.add(policyBean.getFileOwnerGroupException());
			}
		}
		
		List<String> sharedWithUser = new ArrayList<String>();
		List<String> sharedWithGroup = new ArrayList<String>();
		if((policyBean.getSharedWithUser().equalsIgnoreCase("Any") && policyBean.getSharedWithGroup().equalsIgnoreCase("Any"))){
			sharedWithUser.add("__ALL_EL__"); 
			sharedWithGroup.add("__ALL_EL__");
		}else{
			if(policyBean.getSharedWithUser().equalsIgnoreCase("Any")){
				sharedWithUser.add(""); 
			}else{
				if(policyBean.getSharedWithUser().contains(",")){
					String[] sharedUser = policyBean.getSharedWithUser().split(",");
					for(int i=0;i<sharedUser.length;i++){
						sharedWithUser.add(sharedUser[i]);
					}
				}else{
					sharedWithUser.add(policyBean.getSharedWithUser());
				}
			}
			if(policyBean.getSharedWithGroup().equalsIgnoreCase("Any")){
				sharedWithGroup.add("");
			}else{
				if(policyBean.getSharedWithGroup().contains(",")){
					String[] sharedGroup = policyBean.getSharedWithGroup().split(",");
					for(int i=0;i<sharedGroup.length;i++){
						sharedWithGroup.add(sharedGroup[i]);
					}
				}else{
					sharedWithGroup.add(policyBean.getSharedWithGroup());
				}
			}
		}
		
		List<String> sharedWithUserException = new ArrayList<String>();
		if(policyBean.getSharedWithUserException().equalsIgnoreCase("No")){
			sharedWithUserException.add("");
		}else{
			if(policyBean.getSharedWithUserException().contains(",")){
				String[] sharedUserException = policyBean.getSharedWithUserException().split(",");
				for(int i=0;i<sharedUserException.length;i++){
					sharedWithUserException.add(sharedUserException[i]);
				}
			}else{
				sharedWithUserException.add(policyBean.getSharedWithUserException());
			}
		}
		
		List<String> sharedWithGroupException = new ArrayList<String>();
		if(policyBean.getSharedWithGroupException().equalsIgnoreCase("No")){
			sharedWithGroupException.add("");
		}else{
			if(policyBean.getSharedWithGroupException().contains(",")){
				String[] sharedGroupException = policyBean.getSharedWithGroupException().split(",");
				for(int i=0;i<sharedGroupException.length;i++){
					sharedWithGroupException.add(sharedGroupException[i]);
				}
			}else{
				sharedWithGroupException.add(policyBean.getSharedWithGroupException());
			}
		}
		
		List<String> fileNameList = new ArrayList<String>();
		if(policyBean.getFileName().equalsIgnoreCase("No")){
			fileNameList.add("");
		}else{
			if(policyBean.getFileName().contains(",")){
				String[] fileNameArray = policyBean.getFileName().split(",");
				for(int i=0;i<fileNameArray.length;i++){
					fileNameList.add(fileNameArray[i]);
				}
			}else{
				fileNameList.add(policyBean.getFileName());
			}
		}
		List<String> fileExceptionList = new ArrayList<String>();
		fileExceptionList.add("");
		
		List<String> fileTypeList = new ArrayList<String>();
		fileTypeList.add("__ALL_EL__");
		
		List<String> exposureList = new ArrayList<String>();
		String exposureMatch = null;
		if(policyBean.getExposureType().contains(",")){
			String[] exposureArray = policyBean.getExposureType().split(",");
			for(int i=0;i<exposureArray.length;i++){
				if(!(exposureArray[i].contains("all") || exposureArray[i].contains("any"))){
					exposureList.add(exposureArray[i]);
				}else{
					exposureMatch = exposureArray[i];
				}
			}
		}else{
			exposureList.add(policyBean.getExposureType());
		}
		if(exposureMatch == null){
			exposureMatch = "any";
		}
		
		List<String> contentIQList = new ArrayList<String>();
		if(!policyBean.getCiqProfile().equals("no")){
			contentIQList.add(policyBean.getCiqProfile());
		}else if(!policyBean.getRiskType().equals("no")){
			contentIQList.add(policyBean.getRiskType());
		}
		
		List<String> notifySmsList = new ArrayList<String>();
		if(policyBean.getNotifySms() == null){
			//notifySmsList.add("");
		}else{
			if(policyBean.getNotifySms().contains(",")){
				String[] notifySmsArray = policyBean.getNotifySms().split(",");
				for(int i=0;i<notifySmsArray.length;i++){
					notifySmsList.add(notifySmsArray[i]);
				}
			}else{
				notifySmsList.add(policyBean.getNotifySms());
			}
		}
		
		List<String> ciqExceptionList = new ArrayList<String>();
		if(policyBean.getCiqProfileException() == null || policyBean.getCiqProfileException().equalsIgnoreCase("No")){
			ciqExceptionList.add("");
		}else{
			if(policyBean.getCiqProfileException().contains(",")){
				String[] ciqExceptionArray = policyBean.getFileOwnerUserException().split(",");
				for(int i=0;i<ciqExceptionArray.length;i++){
					ciqExceptionList.add(ciqExceptionArray[i]);
				}
			}else{
				ciqExceptionList.add(policyBean.getCiqProfileException());
			}
		}
		
		
		List<String> cloudServiceList = new ArrayList<String>();
		List<String> cloudServiceSubfeature = new ArrayList<String>();
		if(policyBean.getCloudService().equalsIgnoreCase("Office 365")){
			cloudServiceList.add("Office 365");
			cloudServiceSubfeature.add("OneDrive");
		}else if(policyBean.getCloudService().equalsIgnoreCase("Office 365 Email")){
			cloudServiceList.add("Office 365");
			cloudServiceSubfeature.add("Mail");
		}else if(policyBean.getCloudService().equalsIgnoreCase("Office 365 Site")){
			cloudServiceList.add("Office 365");
			cloudServiceSubfeature.add("Sites");
		}else if(policyBean.getCloudService().equalsIgnoreCase("Google Drive")){
			cloudServiceList.add("Google Apps");
			cloudServiceSubfeature.add("Drive");
		}else if(policyBean.getCloudService().equalsIgnoreCase("Gmail")){
			cloudServiceList.add("Google Apps");
			cloudServiceSubfeature.add("Mail");
		}else{
			cloudServiceList.add(policyBean.getCloudService());
			cloudServiceSubfeature.add("");
		}
		
		int largerSize;
		int smallerSize;
		String[] fileSizes = policyBean.getFileSize().split(",");
		largerSize = Integer.parseInt(fileSizes[0]);
		smallerSize = Integer.parseInt(fileSizes[1]);
		String severity = policyBean.getSeverity();
		String policyDesc = policyBean.getPolicyDesc();
		ProtectQueryBuilder protectQueryBuilder = new ProtectQueryBuilder(); 
		List<String> risksList = new ArrayList<String>();
		
		payload = protectQueryBuilder.getESQueryForDataExposurePolicy(policyBean.getPolicyName(), fileOwnerUser, fileOwnerGroup, domainName, fileOwnerUserException, fileOwnerGroupException, sharedWithUser, sharedWithGroup, sharedWithUserException, sharedWithGroupException, cloudServiceList, cloudServiceSubfeature, contentIQList, fileNameList, fileExceptionList, risksList, fileTypeList, exposureList, largerSize, smallerSize, exposureMatch, severity, notifySmsList, ciqExceptionList);
		payload = payload.replace("\"remediation\"", policyRemediation);
		payload = payload.replace("\"Policy Desc\"", "\""+policyDesc+"\"");
		payload = payload.replace("\"\"", "");

		return payload;
	}
	
	/**
	 * 
	 * @param restClient
	 * @param payload
	 * @param requestHeader
	 * @param suiteData
	 * @param policyName
	 * @return
	 */
	public HttpResponse createPolicy(Client restClient, String payload, List<NameValuePair> requestHeader, TestSuiteDTO suiteData, String policyName){
		Reporter.log("Creating a new policy name - " + policyName, true);
		HttpResponse policyResponse = null;
		String restAPI;
		URI uri;
		StringEntity entity;
		try{
			restAPI = replaceGenericParams(suiteData.getAPIMap().get("createPolicy"), suiteData);
			Reporter.log("PayLoad for Policy: "+payload, true);
			entity = new StringEntity(payload,"UTF-8");
			uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(),restAPI);
			policyResponse = restClient.doPost(uri, requestHeader, null, entity);
			Reporter.log("Created a new policy name - " + policyName, true);
		}catch(Exception e){
			e.printStackTrace();
		}
		return policyResponse;
	}
	
	/**
	 * 
	 * @param restClient
	 * @param policyBean
	 * @param requestHeader
	 * @param suiteData
	 * @throws Exception
	 */
	public void createAndActivateDataExposurePolicy(Client restClient, PolicyBean policyBean, List<NameValuePair> requestHeader, TestSuiteDTO suiteData) throws Exception{
		Reporter.log("**************************************", true);
		if (isPolicyExists(restClient, policyBean.getPolicyName(), requestHeader, suiteData)) {
			deletePolicy(restClient, policyBean.getPolicyName(), requestHeader, suiteData);
		}
		
		String riskType = policyBean.getRiskType();
		String fileFormat = policyBean.getFileFormat();
		
		HttpResponse response = null;
		// && getCIQId(restClient, riskType, requestHeader, suiteData) == null		
		if (!riskType.equalsIgnoreCase("no")) {
				if (riskType.equalsIgnoreCase("Source Code") || riskType.equalsIgnoreCase("Encryption")) {
					response = createContentIQProfileByContentType(restClient, riskType, requestHeader, suiteData);
				} else {
					response = createContentIQProfileByRiskType(restClient, riskType, requestHeader, suiteData);
				}
		}
		else if (fileFormat !=null) {
			response = createContentIQProfileForFileFormat(restClient, requestHeader, suiteData, fileFormat);
		}
		
		if (response != null)
			Logger.info("CIQ Profile creation Response: "+ClientUtil.getResponseBody(response));
		
		
		String payload = this.generatePayloadForDataExposurePolicy(policyBean, suiteData);
		Logger.info("Payload:"+payload);
		
		
		
		HttpResponse policyResponse = this.createPolicy(restClient, payload, requestHeader, suiteData, policyBean.getPolicyName());
		this.verifyCreateAndUpdatePolicyResponse(policyResponse);
		Reporter.log("**************************************", true);
		Reporter.log("**************************************", true);
		this.activatePolicyByName(restClient, policyBean.getPolicyName(), requestHeader, suiteData);
		Reporter.log("**************************************", true);
	}
	
	
	public void createAndActivateDataExposurePolicyWithCIQ(Client restClient, PolicyBean policyBean, List<NameValuePair> requestHeader, TestSuiteDTO suiteData) throws Exception{
		Reporter.log("**************************************", true);
		if (isPolicyExists(restClient, policyBean.getPolicyName(), requestHeader, suiteData)) {
			deletePolicy(restClient, policyBean.getPolicyName(), requestHeader, suiteData);
		}
		this.createCIQProtectProfiles(restClient, requestHeader, suiteData, policyBean);
		
		String payload = this.generatePayloadForDataExposurePolicyWithCIQ(policyBean, suiteData);
		Logger.info("Payload:"+payload);
		
		HttpResponse policyResponse = this.createPolicy(restClient, payload, requestHeader, suiteData, policyBean.getPolicyName());
		this.verifyCreateAndUpdatePolicyResponse(policyResponse);
		Reporter.log("**************************************", true);
		Reporter.log("**************************************", true);
		this.activatePolicyByName(restClient, policyBean.getPolicyName(), requestHeader, suiteData);
		Reporter.log("**************************************", true);
	}	
	
	
	public HttpResponse editDataExposurePolicy(Client restClient, String payload, List<NameValuePair> requestHeader, TestSuiteDTO suiteData, String policyName){
		Reporter.log("Editing a new policy name - " + policyName, true);
		URI uri;
		String restAPI;
		StringEntity entity;
		HttpResponse policyResponse = null;
		try{
			restAPI = replaceGenericParams(suiteData.getAPIMap().get("editPolicy"), suiteData);
			Reporter.log("PayLoad for Policy: "+payload, true);
			entity = new StringEntity(payload);
			uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(),restAPI);
			policyResponse = restClient.doPost(uri, requestHeader, null, entity);
			Reporter.log("Edited a policy - " + policyName, true);
		}catch(Exception e){
			e.printStackTrace();
		}
		return policyResponse;
	}
	
	/**
	 * Verify create and update policy response
	 * @param response
	 * @throws JsonProcessingException
	 * @throws IOException
	 */
	public void verifyCreateAndUpdatePolicyResponse(HttpResponse response) throws JsonProcessingException, IOException{
		Reporter.log("Verifing created or updated policy response", true);
		String policyResponseBody = ClientUtil.getResponseBody(response);
		Reporter.log("Create policy response: "+policyResponseBody, true);
		String createPolicyStatus = ClientUtil.getJSONValue(policyResponseBody, ProtectTestConstants.ACTION_STATUS);
		String apiResponse = ClientUtil.getJSONValue(policyResponseBody, "api_response");
		String activeStatus = ClientUtil.getJSONValue(apiResponse, "is_active");
		createPolicyStatus = createPolicyStatus.substring(1, createPolicyStatus.length() - 1);
		Assert.assertEquals(createPolicyStatus, ProtectTestConstants.SUCCESS);
		Assert.assertEquals(activeStatus, ProtectTestConstants.FALSE);
		Reporter.log("Verified created or updated policy response", true);
	}
	
	public String generatePayloadForAccessMonitoringPolicy(PolicyBean policyBean) throws IOException{
		String payload = null;
		List<String> fileOwnerUser = new ArrayList<String>();
		List<String> fileOwnerGroup = new ArrayList<String>();
		List<String> domainName = new ArrayList<String>();
		if((policyBean.getFileOwnerUser().equalsIgnoreCase("Any") && policyBean.getFileOwnerGroup().equalsIgnoreCase("Any")) && policyBean.getDomainName().equalsIgnoreCase("Any")){
			fileOwnerUser.add("__ALL_EL__");
			fileOwnerGroup.add("__ALL_EL__");
			domainName.add("__ALL_EL__");
		}else{
			if(policyBean.getFileOwnerUser().equalsIgnoreCase("Any")){
				fileOwnerUser.add("");
			}else{
				if(policyBean.getFileOwnerUser().contains(",")){
					String[] users = policyBean.getFileOwnerUser().split(",");
					for(int i=0;i<users.length;i++){
						fileOwnerUser.add(users[i]);
					}
				}else{
					fileOwnerUser.add(policyBean.getFileOwnerUser());
				}
			}
			if(policyBean.getFileOwnerGroup().equalsIgnoreCase("Any")){
				fileOwnerGroup.add("");
			}else{
				if(policyBean.getFileOwnerGroup().contains(",")){
					String[] groups = policyBean.getFileOwnerGroup().split(",");
					for(int i=0;i<groups.length;i++){
						fileOwnerGroup.add(groups[i]);
					}
				}else{
					fileOwnerGroup.add(policyBean.getFileOwnerGroup());
				}
				
			}
			if(policyBean.getDomainName().equalsIgnoreCase("Any")){
				domainName.add("__ALL_EL__");
			}else{
				if(policyBean.getDomainName().contains(",")){
					String[] domains = policyBean.getDomainName().split(",");
					for(int i=0;i<domains.length;i++){
						domainName.add(domains[i]);
					}
				}else{
					domainName.add(policyBean.getDomainName());
				}
			}
		}
		
		List<String> fileOwnerUserException = new ArrayList<String>();
		if(policyBean.getFileOwnerUserException().equalsIgnoreCase("No")){
			fileOwnerUserException.add("");
		}else{
			if(policyBean.getFileOwnerUserException().contains(",")){
				String[] userException = policyBean.getFileOwnerUserException().split(",");
				for(int i=0;i<userException.length;i++){
					fileOwnerUserException.add(userException[i]);
				}
			}else{
				fileOwnerUserException.add(policyBean.getFileOwnerUserException());
			}
		}
		
		List<String> fileOwnerGroupException = new ArrayList<String>();
		if(policyBean.getFileOwnerGroupException().equalsIgnoreCase("No")){
			fileOwnerGroupException.add("");
		}else{
			if(policyBean.getFileOwnerGroupException().contains(",")){
				String[] groupException = policyBean.getFileOwnerGroupException().split(",");
				for(int i=0;i<groupException.length;i++){
					fileOwnerGroupException.add(groupException[i]);
				}
			}else{
				fileOwnerGroupException.add(policyBean.getFileOwnerGroupException());
			}
		}
		String policyName = policyBean.getPolicyName();
		List<String> cloudServiceList = new ArrayList<String>();
		List<String> cloudServiceSubfeature = new ArrayList<String>();
		if(policyBean.getCloudService().equalsIgnoreCase("Office 365")){
			cloudServiceList.add("Office 365");
			cloudServiceSubfeature.add("OneDrive");
		}else if(policyBean.getCloudService().equalsIgnoreCase("Office 365 Email")){
			cloudServiceList.add("Office 365");
			cloudServiceSubfeature.add("Mail");
		}else if(policyBean.getCloudService().equalsIgnoreCase("Google Drive")){
			cloudServiceList.add("Google Apps");
			cloudServiceSubfeature.add("Drive");
		}else if(policyBean.getCloudService().equalsIgnoreCase("Gmail")){
			cloudServiceList.add("Google Apps");
			cloudServiceSubfeature.add("Mail");
		}else{
			cloudServiceList.add(policyBean.getCloudService());
			cloudServiceSubfeature.add("");
		}
		String threatScore = policyBean.getThreatScore();
		int riskValue = Integer.parseInt(threatScore);
		String activities = policyBean.getActivityScope();
		String[] activityList = activities.split(",");
		Map<String, ArrayList<String>> objectAccessMap = new HashMap<String, ArrayList<String>>();
		ArrayList<String> fileAccessTypeList = new ArrayList<String>();
		ArrayList<String> folderAccessTypeList = new ArrayList<String>();
		ArrayList<String> userAccessTypeList = new ArrayList<String>();
		ArrayList<String> groupAccessTypeList = new ArrayList<String>();
		for(int i=0;i<activityList.length;i++){
			String objectAccessed = activityList[i].split(":")[0];
			String accessType = activityList[i].split(":")[1];
			if(objectAccessed.equalsIgnoreCase("file")){
				fileAccessTypeList.add(accessType);
			}
			if(objectAccessed.equalsIgnoreCase("folder")){
				folderAccessTypeList.add(accessType);			
			}
			if(objectAccessed.equalsIgnoreCase("user")){
				userAccessTypeList.add(accessType);
			}
			if(objectAccessed.equalsIgnoreCase("group")){
				groupAccessTypeList.add(accessType);
			}
		}
		if(!fileAccessTypeList.isEmpty()){
			objectAccessMap.put("File", fileAccessTypeList);
		}
		if(!folderAccessTypeList.isEmpty()){
			objectAccessMap.put("Folder", folderAccessTypeList);
		}
		if(!userAccessTypeList.isEmpty()){
			objectAccessMap.put("User", userAccessTypeList);
		}
		if(!groupAccessTypeList.isEmpty()){
			objectAccessMap.put("Group", groupAccessTypeList);
		}
		ProtectQueryBuilder protectQueryBuilder = new ProtectQueryBuilder(); 
		payload = protectQueryBuilder.getESQueryForAccessMonitoringPolicy(policyName, riskValue, objectAccessMap, fileOwnerUser, fileOwnerGroup, domainName, fileOwnerUserException, fileOwnerGroupException, cloudServiceList, cloudServiceSubfeature);
		payload = payload.replace("\"\"", "");
		return payload;
	}
	
	/**
	 * 
	 * @param restClient
	 * @param policyBean
	 * @param requestHeader
	 * @param suiteData
	 */
	public void createAccessEnforcementPolicy(Client restClient, PolicyBean policyBean, List<NameValuePair> requestHeader, TestSuiteDTO suiteData){
		Reporter.log("Creating a new access enforcement policy name - " + policyBean.getPolicyName(), true);
		String restAPI;
		StringEntity entity;
		URI uri;
		String activityScope = "";
		if (!validateDataExposureBeanAndSetDefaultValues(policyBean)){
			Assert.assertTrue(false, "Invalid Saas App parameter");
		}
		if((policyBean.getFileOwnerUser().equalsIgnoreCase("Any") && policyBean.getFileOwnerGroup().equalsIgnoreCase("Any")) && policyBean.getDomainName().equalsIgnoreCase("Any")){
			policyBean.setFileOwnerUser("[\"__ALL_EL__\"]");
			policyBean.setFileOwnerGroup("[\"__ALL_EL__\"]");
			policyBean.setDomainName("[\"__ALL_EL__\"]");
		}else {
			if(policyBean.getFileOwnerUser().equalsIgnoreCase("Any")){
				policyBean.setFileOwnerUser("[]");
			}else{
				policyBean.setFileOwnerUser("[\""+policyBean.getFileOwnerUser()+"\"]");
			}
			
			if(policyBean.getFileOwnerGroup().equalsIgnoreCase("Any")){
				policyBean.setFileOwnerGroup("[]");
			}else{
				policyBean.setFileOwnerGroup("[\""+policyBean.getFileOwnerGroup()+"\"]");
			}
			
			if(policyBean.getDomainName().equalsIgnoreCase("Any")){
				policyBean.setDomainName("[\"__ALL_EL__\"]");
			}else{
				policyBean.setDomainName("[\""+policyBean.getDomainName()+"\"]");
			}
			
		}
		if(policyBean.getFileOwnerUserException().equalsIgnoreCase("No")){
			policyBean.setFileOwnerUserException("[]");
		}else{
			policyBean.setFileOwnerUserException("[\""+policyBean.getFileOwnerUserException()+"\"]");
		}
		
		if(policyBean.getFileOwnerGroupException().equalsIgnoreCase("No")){
			policyBean.setFileOwnerGroupException("[]");
		}else{
			policyBean.setFileOwnerGroupException("[\""+policyBean.getFileOwnerGroupException()+"\"]");
		}
		if(policyBean.getActivityScope().contains(",")){
			String[] activities = policyBean.getActivityScope().split(",");
			for(int i=0;i<activities.length;i++){
				String[] activity = activities[i].split(":");
				String type = activity[0];
				String scope = activity[1];
				if(activityScope.contains(type)){
					
				}else{
					if(activityScope.equals("")){
						activityScope = "[{\""+type+"\":[\""+scope+"\"]}]";
					}else{
						activityScope = activityScope + ",[{\""+type+"\":[\""+scope+"\"]}]";
					}
				}
			}
		}else{
			String[] activity = policyBean.getActivityScope().split(":");
			activityScope = "[{\""+activity[0]+"\":[\""+activity[1]+"\"]}]";
		}
		try{
			restAPI = replaceGenericParams(suiteData.getAPIMap().get("createPolicy"), suiteData);
			
			String entityString = "{\"policy_name\":\""+policyBean.getPolicyName()+"\",\"policy_description\":\"Policy\",\"applications\":[\""+policyBean.getCloudService()+"\"],"
					+ "\"users_scope\":{\"users\":"+policyBean.getFileOwnerUser()+",\"groups\":"+policyBean.getFileOwnerGroup()+",\"domains\":"+policyBean.getDomainName()+"},"
					+ "\"users_scope_whitelist\":{\"users\":"+policyBean.getFileOwnerUserException()+",\"groups\":"+policyBean.getFileOwnerGroupException()+"},\"risk_level\":"+policyBean.getThreatScore()+","
					+ "\"response\":[{\"type\":\"NOTIFY_USER\",\"meta_info\":{\"notify_user\":true}},{"
					+ "\"type\": \"SEVERITY_LEVEL\",\"meta_info\": {\"severity_level\": \"high\"}}],\"violations\":1,"
					+ "\"geoip_scope\":[],\"geoip_whitelist\":[],\"platform\":[],\"platform_whitelist\":[],\"browser\":[],\"browser_whitelist\":[],"
					+ "\"activities_scope\":"+activityScope+","
					+ "\"account_type\":[],\"policy_type\":\"accessenforceapi\",\"is_active\":false}";
			Reporter.log("PayLoad for Policy: "+entityString, true);
			entity = new StringEntity(entityString);
			uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(),restAPI);
			HttpResponse policyResponse = restClient.doPost(uri, requestHeader, null, entity);
			String policyResponseBody = ClientUtil.getResponseBody(policyResponse);
			Reporter.log("Create policy response: "+policyResponseBody, true);
			String createPolicyStatus = ClientUtil.getJSONValue(policyResponseBody, ProtectTestConstants.ACTION_STATUS);
			createPolicyStatus = createPolicyStatus.substring(1, createPolicyStatus.length() - 1);
			Assert.assertEquals(createPolicyStatus, ProtectTestConstants.SUCCESS);
			Reporter.log("Created a new policy name - " + policyBean.getPolicyName(), true);
		}catch(Exception e ){
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param restClient
	 * @param policyBean
	 * @param requestHeader
	 * @param suiteData
	 * @throws Exception
	 */
	public void createAndActivateAccessEnforcementPolicy(Client restClient, PolicyBean policyBean, List<NameValuePair> requestHeader, TestSuiteDTO suiteData) throws Exception{
		Reporter.log("**************************************", true);
		if (isPolicyExists(restClient, policyBean.getPolicyName(), requestHeader, suiteData)) {
			deletePolicy(restClient, policyBean.getPolicyName(), requestHeader, suiteData);
		}
		//this.createAccessEnforcementPolicy(restClient, policyBean, requestHeader, suiteData);
		String payload = this.generatePayloadForAccessMonitoringPolicy(policyBean);
		this.createPolicy(restClient, payload, requestHeader, suiteData, policyBean.getPolicyName());
		Reporter.log("**************************************", true);
		Reporter.log("**************************************", true);
		this.activatePolicyByName(restClient, policyBean.getPolicyName(), requestHeader, suiteData);
		Reporter.log("**************************************", true);
	}
	
	/**
	 * 
	 * @param data
	 * @return
	 */
	public PolicyBean setAccessEnforcementPolicyData(String[]  data){
		PolicyBean policyBean = new PolicyBean();
		policyBean.setPolicyName(data[0]);
		policyBean.setCloudService(data[1]);
		policyBean.setFileOwnerUser(data[2]);
		policyBean.setFileOwnerGroup(data[3]);
		policyBean.setDomainName(data[4]);
		policyBean.setFileOwnerUserException(data[5]);
		policyBean.setFileOwnerGroupException(data[6]);
		policyBean.setThreatScore(data[7]);
		policyBean.setActivityScope(data[8]);
		policyBean.setOptionalField(data[9]);
		//policyBean.setFileExt(data[9]);
		return policyBean;
	}
	
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
	
	public enum FolderOperations{
		ACCEPTCOLLABORATION,
		ADD,
		CHANGECOLLABORATIONROLE,
		COPY,
		DELETE,
		DOWNLOAD,
		EXPIRECOLLABORATION,
		EXPIRESHARING,
		MOVE,
		REMOVE,
		RENAME,
		SHARE,
		STORAGEEXPIRATION,
		SYNC,
		TRANSFEROWNERSHIP,
		UNDELETE,
		UNSHARE,
		UNSYNC,
		UPLOAD
	}
	public enum GroupOperation{
		Create,
		Delete,
		Edit
	}
	public enum RoleOperation{
		Change
	}
	public enum SessionOperation{
		login,
		InvalidLogin
	}
	public enum UserOperation{
		ADD,
		DELETE,
		EDIT,
		REMOVE
	}
	
	/**
	 * 
	 * @param policyBean
	 * @param universalApi
	 * @throws Exception
	 */
	public void setAccessEnforcementBoxOperations(PolicyBean policyBean, UniversalApi universalApi, File file, BoxFolder parentFolder) throws Exception{
		String activityScope = "";
		BoxFolder folder = null;
		String folderName = null;
		String policyName = policyBean.getPolicyName();
		ObjectAccessed objectAccessed;
		if(policyBean.getActivityScope().contains(",")){
			String[] activityScopes = policyBean.getActivityScope().split(",");
			activityScope = activityScopes[0];
		}else{
			activityScope = policyBean.getActivityScope();
		}
		String[] activities = policyBean.getActivityScope().split(":");
		String activity = activities[0].toUpperCase();
		String operation = activities[1].toUpperCase();
		objectAccessed = ObjectAccessed.valueOf(activity);
		switch (objectAccessed) {
		case FILE:
			folderName = "A_" + UUID.randomUUID().toString();
			Reporter.log("Creating a folder in Box : " + folderName, true);
			folder = universalApi.createFolder(folderName);
			FileUploadResponse fileUploadResponse = universalApi.uploadFile(folder.getId(), file.getAbsolutePath(), file.getName());
			this.waitForMinutes(0.5);
			FileOperations fileOperations = FileOperations.valueOf(operation);
			switch(fileOperations){
			case COPY:
				String copyfoldername = "A_" + UUID.randomUUID().toString();
				Reporter.log("Creating a folder in Box to copy the file: " + copyfoldername, true);
				BoxFolder copyFolder = universalApi.createFolder(copyfoldername);
				universalApi.copyFile(fileUploadResponse.getFileId(), copyFolder.getId());
				BoxFileOperationTests.fileCollection.put(policyName, file.getName());
				BoxFileOperationTests.folderCollection.put(policyName, copyFolder);
				break;
			case DELETE:
				universalApi.deleteFile(fileUploadResponse.getFileId(), fileUploadResponse.getEtag());
				BoxFileOperationTests.fileCollection.put(policyName, file.getName());
				BoxFileOperationTests.folderCollection.put(policyName, folder);
				break;
			case DOWNLOAD:
				universalApi.downloadFile(fileUploadResponse.getFileId(), file.getName());
				BoxFileOperationTests.fileCollection.put(policyName, file.getName());
				BoxFileOperationTests.folderCollection.put(policyName, folder);
				break;
			case EDIT:
				universalApi.updateFile(fileUploadResponse.getFileId(), "Hello.java", fileUploadResponse.getEtag(), file.getName());
				BoxFileOperationTests.fileCollection.put(policyName, file.getName());
				BoxFileOperationTests.folderCollection.put(policyName, folder);
				break;
			case EXPIRESHARING:
				SharedLink sharedLink2 = new SharedLink();
				sharedLink2.setAccess("open");
				universalApi.createSharedLink(fileUploadResponse.getFileId(), sharedLink2);
				this.waitForMinutes(0.5);
				sharedLink2.setUnsharedAt(DateUtils.getDaysFromCurrentTime(1));
				BoxFileOperationTests.fileCollection.put(policyName, file.getName());
				BoxFileOperationTests.folderCollection.put(policyName, folder);
				break;
			case LOCK:
				universalApi.lockFile(fileUploadResponse.getFileId(), true);
				BoxFileOperationTests.fileCollection.put(policyName, file.getName());
				BoxFileOperationTests.folderCollection.put(policyName, folder);
				break;
			case MOVE:
				String movefoldername = "A_" + UUID.randomUUID().toString();
				Reporter.log("Creating a folder in Box to move the file: " + movefoldername, true);
				BoxFolder moveFolder = universalApi.createFolder(movefoldername);
				universalApi.moveFolder(fileUploadResponse.getFileId(), moveFolder.getId());
				BoxFileOperationTests.fileCollection.put(policyName, file.getName());
				BoxFileOperationTests.folderCollection.put(policyName, moveFolder);
				break;
			case PREVIEW:
				universalApi.previewFile(fileUploadResponse.getFileId(), file.getName());
				BoxFileOperationTests.fileCollection.put(policyName, file.getName());
				BoxFileOperationTests.folderCollection.put(policyName, folder);
				break;
			case RENAME:
				String newName = this.generateAlphaNumericString(7)+".txt";
				universalApi.renameFile(fileUploadResponse.getFileId(), newName);
				BoxFileOperationTests.fileCollection.put(policyName, newName);
				BoxFileOperationTests.folderCollection.put(policyName, folder);
				break;
			case SHARE:
				SharedLink sharedLink = new SharedLink();
				sharedLink.setAccess("open");
				universalApi.createSharedLink(fileUploadResponse.getFileId(), sharedLink);
				BoxFileOperationTests.fileCollection.put(policyName, file.getName());
				BoxFileOperationTests.folderCollection.put(policyName, folder);
				break;
			case STORAGEEXPIRATION:
				break;
			case UNDELETE:
				universalApi.deleteFile(fileUploadResponse.getFileId(), fileUploadResponse.getEtag());
				this.waitForMinutes(0.5);
				String restoreName = this.generateAlphaNumericString(7)+".txt";
				universalApi.restoreFileFromTrash(fileUploadResponse.getFileId(), restoreName);
				BoxFileOperationTests.fileCollection.put(policyName, restoreName);
				BoxFileOperationTests.folderCollection.put(policyName, folder);
				break;
			case UNLOCK:
				universalApi.lockFile(fileUploadResponse.getFileId(), true);
				this.waitForMinutes(0.5);
				universalApi.unlockFile(fileUploadResponse.getFileId());
				BoxFileOperationTests.fileCollection.put(policyName, file.getName());
				BoxFileOperationTests.folderCollection.put(policyName, folder);
				break;
			case UNSHARE:
				SharedLink sharedLink1 = new SharedLink();
				sharedLink1.setAccess("open");
				universalApi.createSharedLink(fileUploadResponse.getFileId(), sharedLink1);
				this.waitForMinutes(0.5);
				universalApi.disableSharedLink(fileUploadResponse.getFileId());
				BoxFileOperationTests.fileCollection.put(policyName, file.getName());
				BoxFileOperationTests.folderCollection.put(policyName, folder);
				break;
			case UPLOAD:
				Reporter.log("File uploaded successfully: "+file.getName(), true);
				BoxFileOperationTests.fileCollection.put(policyName, file.getName());
				BoxFileOperationTests.folderCollection.put(policyName, folder);
				break;
			default:
				break;
			}
			break;

		case FOLDER:
			FolderOperations folderOperations = FolderOperations.valueOf(operation);
			switch(folderOperations){
			case ACCEPTCOLLABORATION:
			break;
			case ADD:
				break;
			case CHANGECOLLABORATIONROLE:
			break;
			case COPY:
				folderName = "A_" + UUID.randomUUID().toString();
				Reporter.log("Creating a folder in Box : " + folderName, true);
				folder = universalApi.createFolder(folderName, parentFolder.getId());
				String copyFolderName = "D_" + UUID.randomUUID().toString();
				BoxFolder copyFolder = universalApi.createFolder(copyFolderName, parentFolder.getId());
				universalApi.moveFolder(folder.getId(), copyFolder.getId());
				BoxFolderOperationTests.folderCollection.put(policyName, folder);
				break;
			case DELETE:
				folderName = "A_" + UUID.randomUUID().toString();
				Reporter.log("Creating a folder in Box : " + folderName, true);
				folder = universalApi.createFolder(folderName, parentFolder.getId());
				universalApi.deleteFolder(folder.getId(), true, folder.getEtag());
				BoxFolderOperationTests.folderCollection.put(policyName, folder);
				break;
			case DOWNLOAD:
				folderName = "A_" + UUID.randomUUID().toString();
				Reporter.log("Creating a folder in Box : " + folderName, true);
				folder = universalApi.createFolder(folderName, parentFolder.getId());
				universalApi.downloadFile(folder.getId(), folderName);
				BoxFolderOperationTests.folderCollection.put(policyName, folder);
				break;
			case EXPIRECOLLABORATION:
			break;
			case EXPIRESHARING:
			break;
			case MOVE:
				folderName = "A_" + UUID.randomUUID().toString();
				Reporter.log("Creating a folder in Box : " + folderName, true);
				folder = universalApi.createFolder(folderName, parentFolder.getId());
				String moveFolderName = "D_" + UUID.randomUUID().toString();
				BoxFolder moveFolder = universalApi.createFolder(moveFolderName, parentFolder.getId());
				universalApi.moveFolder(folder.getId(), moveFolder.getId());
				BoxFolderOperationTests.folderCollection.put(policyName, folder);
				break;
			case REMOVE:
				// TODO
			break;
			case RENAME:
				folderName = "A_" + UUID.randomUUID().toString();
				Reporter.log("Creating a folder in Box : " + folderName, true);
				folder = universalApi.createFolder(folderName, parentFolder.getId());
				universalApi.renameFolder(folder.getId(), "A_UNIQUEFOLDER");
				BoxFolderOperationTests.folderCollection.put(policyName, folder);
				break;
			case SHARE:
				folderName = "A_" + UUID.randomUUID().toString();
				Reporter.log("Creating a folder in Box : " + folderName, true);
				folder = universalApi.createFolder(folderName, parentFolder.getId());
				SharedLink sharedLink = new SharedLink();
				sharedLink.setAccess("open");
				universalApi.createSharedLinkForFolder(folder.getId(), sharedLink);
				BoxFolderOperationTests.folderCollection.put(policyName, folder);
				break;
			case STORAGEEXPIRATION:
			break;
			case SYNC:
			break;
			case TRANSFEROWNERSHIP:
			break;
			case UNDELETE:
				folderName = "A_" + UUID.randomUUID().toString();
				Reporter.log("Creating a folder in Box : " + folderName, true);
				folder = universalApi.createFolder(folderName, parentFolder.getId());
				universalApi.deleteFolder(folder.getId(), true, folder.getEtag());
				this.waitForMinutes(0.5);
				universalApi.restoreFolder(folder.getId(), folderName);
				BoxFolderOperationTests.folderCollection.put(policyName, folder);
				break;
			case UNSHARE:
				folderName = "A_" + UUID.randomUUID().toString();
				Reporter.log("Creating a folder in Box : " + folderName, true);
				folder = universalApi.createFolder(folderName, parentFolder.getId());
				SharedLink sharedLink1 = new SharedLink();
				sharedLink1.setAccess("open");
				universalApi.createSharedLinkForFolder(folder.getId(), sharedLink1);
				universalApi.disableSharedLinkForFolder(folder.getId());
				BoxFolderOperationTests.folderCollection.put(policyName, folder);
				break;
			case UNSYNC:
			break;
			case UPLOAD:
				folderName = "A_" + UUID.randomUUID().toString();
				Reporter.log("Creating a folder in Box : " + folderName, true);
				folder = universalApi.createFolder(folderName, parentFolder.getId());
				BoxFolderOperationTests.folderCollection.put(policyName, folder);
				break;
			}
			break;
			
		case GROUP:
			folderName = "A_" + UUID.randomUUID().toString();
			folder = universalApi.createFolder(folderName);
			// Create Group
			GroupInput groupInput = new GroupInput();
			groupInput.setName("QA Bangalore");
			groupInput.setProvenance("Elastica");
			groupInput.setDescription("Bangalore Team");
			BoxGroup boxGroup = universalApi.createGroup(groupInput);
			this.waitForMinutes(4);
			
			// Edit Group
			boxGroup.setName("Updated QA Bangalore");
			boxGroup = universalApi.updateGroup(boxGroup);
			this.waitForMinutes(4);
			
			// Delete Group
			universalApi.deleteGroup(boxGroup);
			this.waitForMinutes(4);
			break;
			
		case ROLE:
			BoxUserInfo changeRoleInfo = new BoxUserInfo();
			changeRoleInfo.setRole(policyBean.getOptionalField());
			universalApi.updateUser(changeRoleInfo);
			break;
			
		case SESSION:
			break;
			
		case UNKNOWNDEVICE:
			break;
			
		case USER:
			folderName = "A_" + UUID.randomUUID().toString();
			folder = universalApi.createFolder(folderName);
			BoxUserInfo userInfo = new BoxUserInfo();
			String useremail = "box-coadmin@protectbeatle.com";
			userInfo.setLogin(useremail);
			String uniqueId = UUID.randomUUID().toString();
			GroupInput ginput = new GroupInput();
			ginput.setName(BoxUserOperationTests.groupname);
			ginput.setProvenance("Elastica");
			ginput.setDescription("Bangalore Team");
			BoxGroup bgroup = universalApi.createGroup(ginput);
			userInfo.setName(BoxUserOperationTests.username);
			userInfo.setRole("coadmin");
			userInfo.getName();
			Reporter.log("User Creation Input:"+userInfo, true);
			userInfo = universalApi.createUser(userInfo);
			Reporter.log("created user object:"+MarshallingUtils.marshall(userInfo), true);
			
			this.waitForMinutes(3);
			userInfo.setName(BoxUserOperationTests.updatedUsername);
			universalApi.updateUser(userInfo);
			
			this.waitForMinutes(3);
			MembershipInput membershipInput = new MembershipInput();
			membershipInput.setGroup(bgroup);
			membershipInput.setUser(userInfo);
			BoxMembership boxMembership = universalApi.createMembership(membershipInput);
			
			this.waitForMinutes(3);
			universalApi.deleteMembership(boxMembership);
			
			this.waitForMinutes(3);
			universalApi.deleteUser(userInfo);
			
			universalApi.deleteGroup(bgroup);
			break;
			
		default:
			break;
		}
	}
	
	public Map<String, String> getAccessMonitoringPolicyViolationAlertLogDetails(Client restClient, PolicyBean policyBean, List<NameValuePair> requestHeader, TestSuiteDTO suiteData) throws Exception{
		Map<String, String> responseMessages = new HashMap<String, String>();
		HttpResponse response = null;
		ElasticSearchLogs esLogs = new ElasticSearchLogs();
		String csfrToken = requestHeader.get(0).getValue();
		String sessionID = requestHeader.get(4).getValue();
		String payload = "{\"source\":{\"query\":{\"filtered\":{\"query\":{\"bool\":{\"must\":[{\"range\":{\"created_timestamp\":{\"from\":\""+getDateFromCurrent(-1)+"T12:35:27\",\"to\":\""+getDateFromCurrent(2)+"T12:35:27\"}}},{\"query_string\":{\"query\":\"" 
				+ policyBean.getPolicyName()
				+ "\",\"default_operator\":\"AND\",\"analyzer\":\"custom_search_analyzer\",\"allow_leading_wildcard\":\"false\"}},{\"term\":{\"facility\":\""
				+ policyBean.getCloudService()
				+ "\"}}]}},\"filter\":{}}},\"from\":0,\"size\":160,\"sort\":{\"created_timestamp\":{\"order\":\"desc\",\"ignore_unmapped\":\"true\"}}},\"sourceName\":\"investigate\",\"apiServerUrl\":\"http://"+suiteData.getApiserverHostName()+"/\",\"csrftoken\":\""
				+ csfrToken + "\",\"sessionid\":\"" + sessionID + "\",\"userid\":\"" + suiteData.getUsername()
				+ "\"}";
		StringEntity entity = new StringEntity(payload);
		response = esLogs.getDisplayLogs(restClient, requestHeader, suiteData.getApiserverHostName(), entity);
		
		String responseBody = ClientUtil.getResponseBody(response);
		String hits = ClientUtil.getJSONValue(ClientUtil.getJSONValue(responseBody, "hits"), "hits");
		JSONArray jArray = (JSONArray) new JSONTokener(hits).nextValue();
		String message = "";
		for (int i = 0; i < jArray.size(); i++) {
			String source = ClientUtil.getJSONValue(jArray.getJSONObject(i).toString(), "_source");
			Logger.info(source);
			message = ClientUtil.getJSONValue(source, "message");
			if((message.contains("[ALERT] User performed activity")) && (message.contains(policyBean.getPolicyName()))){
				String severity = ClientUtil.getJSONValue(source, ProtectTestConstants.SEVERITY);
				severity = severity.substring(1, severity.length()-1);
				String policyAction = ClientUtil.getJSONValue(source, ProtectTestConstants.POLICY_ACTION);
				policyAction = policyAction.substring(1, policyAction.length()-1);
				String facility = ClientUtil.getJSONValue(source, ProtectTestConstants.FACILITY);
				facility = facility.substring(1, facility.length()-1);
				String __source = ClientUtil.getJSONValue(source, ProtectTestConstants.SOURCE);
				__source = __source.substring(1, __source.length()-1);
				String policyViolated = ClientUtil.getJSONValue(source, ProtectTestConstants.POLICY_VIOLATED);
				policyViolated = policyViolated.substring(1, policyViolated.length()-1);
				String policyType = ClientUtil.getJSONValue(source, ProtectTestConstants.POLICY_TYPE);
				policyType = policyType.substring(1, policyType.length()-1);
				message = message.substring(1, message.length()-1);
				String activityType = ClientUtil.getJSONValue(source, ProtectTestConstants.ACTIVITY_TYPE);
				activityType = activityType.substring(1, activityType.length()-1);
				String user = ClientUtil.getJSONValue(source, ProtectTestConstants.USER);
				user = user.substring(1, user.length()-1);
				String domain = ClientUtil.getJSONValue(source, ProtectTestConstants.DOMAIN);
				domain = domain.substring(1, domain.length()-1);
				String details = ClientUtil.getJSONValue(source, ProtectTestConstants.DETAILS);
				details = details.substring(1, details.length()-1);
				responseMessages.put(ProtectTestConstants.SEVERITY, severity);
				responseMessages.put(ProtectTestConstants.POLICY_ACTION, policyAction);
				responseMessages.put(ProtectTestConstants.FACILITY, facility);
				responseMessages.put(ProtectTestConstants.SOURCE, __source);
				responseMessages.put(ProtectTestConstants.POLICY_VIOLATED, policyViolated);
				responseMessages.put(ProtectTestConstants.POLICY_TYPE, policyType);
				responseMessages.put(ProtectTestConstants.MESSAGE, message);
				responseMessages.put(ProtectTestConstants.ACTIVITY_TYPE, activityType);
				responseMessages.put(ProtectTestConstants.USER, user);
				responseMessages.put(ProtectTestConstants.DOMAIN, domain);
				responseMessages.put(ProtectTestConstants.DETAILS, details);
				break;
			}
		}
		return responseMessages;
	}

	public void assertAccessMonitoringViolationPolicyLogs(Map<String, String> policyViolationLogs, PolicyBean policyBean, TestSuiteDTO suiteData){
		Reporter.log(policyBean.getPolicyName()+" : "+ policyViolationLogs, true);
		String[] activities = policyBean.getActivityScope().split(":");
		Reporter.log("Actual Policy Violation Parameters", true);
		Reporter.log("--------------------Actual Policy Violation Parameters--------------------", true);
		Reporter.log("Severity        : "+policyViolationLogs.get(ProtectTestConstants.SEVERITY), true);
		Reporter.log("Policy Action   : "+policyViolationLogs.get(ProtectTestConstants.POLICY_ACTION), true);
		Reporter.log("Facility        : "+policyViolationLogs.get(ProtectTestConstants.FACILITY), true);
		Reporter.log("Source          : "+policyViolationLogs.get(ProtectTestConstants.SOURCE), true);
		Reporter.log("Policy Violated : "+policyViolationLogs.get(ProtectTestConstants.POLICY_VIOLATED), true);
		Reporter.log("Policy Type     : "+policyViolationLogs.get(ProtectTestConstants.POLICY_TYPE), true);
		Reporter.log("Policy Action   : "+policyViolationLogs.get(ProtectTestConstants.POLICY_ACTION), true);
		Reporter.log("Activity Type   : "+policyViolationLogs.get(ProtectTestConstants.ACTIVITY_TYPE), true);
		Reporter.log("User            : "+policyViolationLogs.get(ProtectTestConstants.USER), true);
		Reporter.log("Domain          : "+policyViolationLogs.get(ProtectTestConstants.DOMAIN), true);
		Reporter.log("Message         : "+policyViolationLogs.get(ProtectTestConstants.MESSAGE), true);
		Reporter.log("--------------------------------------------------------------------------", true);
		Reporter.log("--------------------Expected Policy Violation Parameters--------------------", true);
		//Reporter.log("Severity        : "+ProtectTestConstants.CRITICAL, true);
		Reporter.log("Policy Action   : "+ProtectTestConstants.ALERT, true);
		Reporter.log("Facility        : "+policyBean.getCloudService(), true);
		Reporter.log("Source          : "+ProtectTestConstants.API, true);
		Reporter.log("Policy Violated : "+policyBean.getPolicyName(), true);
		Reporter.log("Policy Type     : "+ProtectTestConstants.ACCESS_ENFORCEMENT, true);
		Reporter.log("Policy Action   : "+ProtectTestConstants.ALERT, true);
		Reporter.log("Activity Type   : "+ProtectTestConstants.POLICY_VIOLATION, true);
		Reporter.log("User            : "+suiteData.getSaasAppUsername(), true);
		Reporter.log("Domain          : "+suiteData.getDomainName(), true);
		String expectedMessage = "[ALERT] User performed activity :\'"+activities[1]+"\' on object type: \'"+activities[0]+"\' which violated policy : "+policyBean.getPolicyName();
		Reporter.log("Message         : "+expectedMessage, true);
		Reporter.log("----------------------------------------------------------------------------", true);
		Reporter.log("------------------Actual and Expected Parameters Assertion------------------", true);
		//Assert.assertEquals(policyViolationLogs.get(ProtectTestConstants.SEVERITY),        ProtectTestConstants.CRITICAL);
		Assert.assertEquals(policyViolationLogs.get(ProtectTestConstants.POLICY_ACTION),   ProtectTestConstants.ALERT);
		Assert.assertEquals(policyViolationLogs.get(ProtectTestConstants.FACILITY),        policyBean.getCloudService());
		Assert.assertEquals(policyViolationLogs.get(ProtectTestConstants.SOURCE),          ProtectTestConstants.API);
		Assert.assertEquals(policyViolationLogs.get(ProtectTestConstants.POLICY_VIOLATED), policyBean.getPolicyName());
		Assert.assertEquals(policyViolationLogs.get(ProtectTestConstants.POLICY_TYPE),     ProtectTestConstants.ACCESS_ENFORCEMENT);
		Assert.assertEquals(policyViolationLogs.get(ProtectTestConstants.POLICY_ACTION),   ProtectTestConstants.ALERT);
		Assert.assertEquals(policyViolationLogs.get(ProtectTestConstants.ACTIVITY_TYPE),   ProtectTestConstants.POLICY_VIOLATION);
		Assert.assertEquals(policyViolationLogs.get(ProtectTestConstants.USER),            suiteData.getSaasAppUsername());
		Assert.assertEquals(policyViolationLogs.get(ProtectTestConstants.DOMAIN),          suiteData.getDomainName());
		Assert.assertEquals(policyViolationLogs.get(ProtectTestConstants.MESSAGE),         expectedMessage);
		Reporter.log("----------------------------------------------------------------------------", true);
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
	public HttpResponse createContentIQProfile(Client restClient, List<NameValuePair> requestHeader, TestSuiteDTO suiteData, Object...data) throws Exception{
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
	public HttpResponse activatePolicy(Client restClient, String policyType, String policyId, List<NameValuePair> requestHeader, TestSuiteDTO suiteData) throws Exception{
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
	public synchronized void activatePolicyByName(Client restClient, String policyName, List<NameValuePair> requestHeader, TestSuiteDTO suiteData) throws Exception{
		Logger.info("Activating a policy - " + policyName);
		Map<String, String> policyDetails = this.getPolicyDetailsByName(restClient, policyName, requestHeader, suiteData);
		String policyType = policyDetails.get(ProtectTestConstants.POLICY_TYPE);
		String policySubId = policyDetails.get(ProtectTestConstants.POLICY_ID);
		String restAPI = replaceGenericParams(suiteData.getAPIMap().get("activatePolicy"), suiteData);
		StringEntity entity = new StringEntity(
				"{\"policy_type\":\"" + policyType + "\",\"status\":true,\"sub_id\":\""+policySubId+"\"}");
		Logger.info("{\"policy_type\":\"" + policyType + "\",\"status\":true,\"sub_id\":\""+policySubId+"\"}");
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(),restAPI);		
		HttpResponse policyActiveResponse = restClient.doPost(dataUri, requestHeader, null, entity);
		String policyActiveResponseBody = ClientUtil.getResponseBody(policyActiveResponse);
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Logger.info("Activated policy response: "+policyActiveResponseBody);
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		String policyActiveStatus = ClientUtil.getJSONValue(policyActiveResponseBody, ProtectTestConstants.ACTION_STATUS);
		policyActiveStatus = policyActiveStatus.substring(1, policyActiveStatus.length() - 1);
		Assert.assertEquals(policyActiveStatus, ProtectTestConstants.SUCCESS, "Policy Activation Failed!!");
		Logger.info("Policy Activated - " + policyName);
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
	public synchronized void deActivatePolicy(Client restClient, String policyName, List<NameValuePair> requestHeader, TestSuiteDTO suiteData) throws Exception{
		Logger.info("Deactivate Policy - " + policyName);
		Map<String, String> policyDetails = this.getPolicyDetailsByName(restClient, policyName, requestHeader, suiteData);
		String policySubId = policyDetails.get(ProtectTestConstants.POLICY_ID);
		String policyType = policyDetails.get(ProtectTestConstants.POLICY_TYPE);
		String restAPI = replaceGenericParams(suiteData.getAPIMap().get("activatePolicy"), suiteData);
		StringEntity entity = new StringEntity(
				"{\"policy_type\":\""+policyType+"\",\"status\":false,\"sub_id\":\""+policySubId+"\"}");	
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(),restAPI);		
		HttpResponse deactivateResponse = restClient.doPost(dataUri, requestHeader, null, entity);
		String policyDeactivateResponseBody = ClientUtil.getResponseBody(deactivateResponse);
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Logger.info("Deactivated policy response: "+policyDeactivateResponseBody);
		Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		String policyDeactivateStatus = ClientUtil.getJSONValue(policyDeactivateResponseBody, ProtectTestConstants.ACTION_STATUS);
		policyDeactivateStatus = policyDeactivateStatus.substring(1, policyDeactivateStatus.length() - 1);
		Assert.assertEquals(policyDeactivateStatus, ProtectTestConstants.SUCCESS);
		Logger.info("Policy deactivated - " + policyName);
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
	public HttpResponse deActivatePolicy(Client restClient, String policyType, String policyId, List<NameValuePair> requestHeader, TestSuiteDTO suiteData) throws Exception{
		
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
	public HttpResponse deletePolicy(Client restClient, String policyType, String policySubId, List<NameValuePair> requestHeader, TestSuiteDTO suiteData) throws Exception{
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
	public String deletePolicy(Client restClient, String policyName, List<NameValuePair> requestHeader, TestSuiteDTO suiteData) throws Exception{
		Reporter.log("Delete Policy - " + policyName, true);
		Map<String, String> policyDetails = this.getPolicyDetailsByName(restClient, policyName, requestHeader, suiteData);
		HttpResponse deleteResponse = this.deletePolicy(restClient, policyDetails.get(ProtectTestConstants.POLICY_TYPE), policyDetails.get(ProtectTestConstants.POLICY_ID), requestHeader, suiteData);
		String policyDeleteResponseBody = ClientUtil.getResponseBody(deleteResponse);
		String policyDeleteStatus = ClientUtil.getJSONValue(policyDeleteResponseBody, ProtectTestConstants.ACTION_STATUS);
		policyDeleteStatus = policyDeleteStatus.substring(1, policyDeleteStatus.length() - 1);
		Assert.assertEquals(policyDeleteStatus, ProtectTestConstants.SUCCESS);
		Reporter.log("Policy Deleted - " + policyName, true);
		return policyDeleteResponseBody;
	}
	
	/**
	 * 
	 * @param policyName
	 * @param restClient
	 * @param requestHeader
	 * @param suiteData
	 * @throws Exception
	 */
	public void deleteExistingPolicy(String policyName, Client restClient, List<NameValuePair> requestHeader, TestSuiteDTO suiteData) throws Exception{
		if (isPolicyExists(restClient, policyName, requestHeader, suiteData)) {
			deletePolicy(restClient, policyName, requestHeader, suiteData);
		}
	}
	
	/**
	 * 
	 * @param restClient
	 * @param policyName
	 * @param requestHeader
	 * @param suiteData
	 * @throws Exception
	 */
	public void deactivateAndDeletePolicy(Client restClient, String policyName, List<NameValuePair> requestHeader, TestSuiteDTO suiteData) throws Exception{
		this.deActivatePolicy(restClient, policyName, requestHeader, suiteData);
		this.deletePolicy(restClient, policyName, requestHeader, suiteData);

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
	public boolean isPolicyExists(Client restClient, String policyName, List<NameValuePair> requestHeader, TestSuiteDTO suiteData) throws Exception{
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
	public boolean isPolicyActive(Client restClient, String policyName, List<NameValuePair> requestHeader, TestSuiteDTO suiteData) throws Exception{
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
	public Map<String, String> getPolicyDetailsByName(Client restClient, String policyName, List<NameValuePair> requestHeader, TestSuiteDTO suiteData) throws Exception{
		Map<String, String> policyDetails = new HashMap<String, String>();
		String restAPI = replaceGenericParams(suiteData.getAPIMap().get("policyList"), suiteData);
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(),restAPI);
		HttpResponse policyListResponse = restClient.doGet(dataUri, requestHeader);
		String policyListResponseBody = ClientUtil.getResponseBody(policyListResponse);
		String policiesList = ClientUtil.getJSONValue(policyListResponseBody, "policies"); 
		JSONArray jArray = (JSONArray) new JSONTokener(policiesList).nextValue();
		String policySubId = null; String exposureType = null;
		for (int i = 0; i < jArray.size(); i++) {
			String name = ClientUtil.getJSONValue(jArray.getJSONObject(i).toString(), "policy_name");
			name = name.substring(1, name.length()-1);
			if(name.equalsIgnoreCase(policyName)){
				String policyType = ClientUtil.getJSONValue(jArray.getJSONObject(i).toString(), "policy_type");
				policyType = policyType.substring(1, policyType.length()-1);
				String policyId = ClientUtil.getJSONValue(jArray.getJSONObject(i).toString(), "id");
				policyId = policyId.substring(1, policyId.length()-1);

				if (!suiteData.getEnvironmentName().equalsIgnoreCase("eoe")) {
				
					policySubId = ClientUtil.getJSONValue(jArray.getJSONObject(i).toString(), "sub_id");
					policySubId = policySubId.substring(1, policySubId.length()-1);
				
				}

				
				String policyStatus = ClientUtil.getJSONValue(jArray.getJSONObject(i).toString(), "is_active");
				String createdBy = ClientUtil.getJSONValue(jArray.getJSONObject(i).toString(), "created_by");
				createdBy = createdBy.substring(1, createdBy.length()-1);
				String modifiedBy = ClientUtil.getJSONValue(jArray.getJSONObject(i).toString(), "modified_by");
				modifiedBy = modifiedBy.substring(1, modifiedBy.length()-1);
				policyDetails.put(ProtectTestConstants.POLICY_TYPE, policyType);
				policyDetails.put(ProtectTestConstants.POLICY_ID, policyId);
				if (!suiteData.getEnvironmentName().equalsIgnoreCase("eoe"))
					policyDetails.put(ProtectTestConstants.POLICY_ID, policySubId);
				policyDetails.put(ProtectTestConstants.ACTION_STATUS, policyStatus);
				policyDetails.put(ProtectTestConstants.CREATED_BY, createdBy);
				policyDetails.put(ProtectTestConstants.MODIFIED_BY, modifiedBy);
				//policy_details
				
				if (!suiteData.getEnvironmentName().equalsIgnoreCase("eoe")){
				
					String policyDetail = ClientUtil.getJSONValue(jArray.getJSONObject(i).toString(), "policy_details");
					if(policyDetail.contains("exposure_scope")){
						exposureType = ClientUtil.getJSONValue(policyDetail, "exposure_scope");
						JSONArray fileArray = (JSONArray) new JSONTokener(exposureType).nextValue();
						if(fileArray.size()>0){
							exposureType = fileArray.get(0).toString();
							policyDetails.put(ProtectTestConstants.EXPOSURE_TYPE, exposureType);
						}
					}
				} else{
					if(jArray.getJSONObject(i).toString().contains("exposure_scope")){
						exposureType = ClientUtil.getJSONValue(jArray.getJSONObject(i).toString(), "exposure_scope");
						JSONArray fileArray = (JSONArray) new JSONTokener(exposureType).nextValue();
						if(fileArray.size()>0){
							exposureType = fileArray.get(0).toString();
							policyDetails.put(ProtectTestConstants.EXPOSURE_TYPE, exposureType);
						}
					}
				}
				break;
			}
		}
		return policyDetails;
	}
	
	/**
	 * Create Content IQ Profile
	 * 
	 * @param restClient
	 * @return
	 * @throws Exception
	 */
	public HttpResponse createContentIQProfileByCustomTerms(Client restClient, String ciqProfileName, String customTerm, List<NameValuePair> requestHeader, TestSuiteDTO suiteData) throws Exception{
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
	public HttpResponse listContentIQProfileByCustomTerms(Client restClient, List<NameValuePair> requestHeader, TestSuiteDTO suiteData) throws Exception{
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
	public HttpResponse deleteContentIQProfileById(Client restClient, String id, List<NameValuePair> requestHeader, TestSuiteDTO suiteData) throws Exception{
		String restAPI = replaceGenericParams(suiteData.getAPIMap().get("listContentIQ"), suiteData);
		StringEntity entity = new StringEntity(
				"{\"url\":\"contentprofiles\",\"id\":\""+id+"\",\"action\":\"delete\"}");	
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(),restAPI);		
		return restClient.doPost(dataUri, requestHeader, null, entity);	
	}
	
	/**
	 * @param restClient
	 * @return
	 * @throws Exception
	 */
	public HttpResponse getProtectPolicyViolationAlert(Client restClient, String fileName, List<NameValuePair> requestHeader, TestSuiteDTO suiteData) throws Exception{
		String restAPI = replaceGenericParams(suiteData.getAPIMap().get("getAnomalyLogs"), suiteData);
		String source = "{\"query\":{\"bool\":{\"must\":[{\"range\":{\"created_timestamp\":{\"from\":\""+new DCIFunctions().getMinusMinutesFromCurrentTime(10)+"\",\"to\":\""+new DCIFunctions().getCurrentTime()+"\"}}},{\"term\":{\"Activity_type\":\"Policy Violation\"}},{\"term\":{\"severity\":\"critical\"}},{\"term\":{\"policy_action\":\"ALERT\"}},{\"query_string\":{\"query\":\"*"+ fileName +"\"}}]}},\"from\":0,\"size\":30,\"sort\":{\"created_timestamp\":{\"order\":\"desc\"}}}"; 
		List<NameValuePair> queryParams = new ArrayList<NameValuePair>();		
		queryParams.add(new BasicNameValuePair("source", source));
		queryParams.add(new BasicNameValuePair("sourceName", "PROTECT"));
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(), restAPI,queryParams);
		return restClient.doGet(dataUri, requestHeader);
	}
	
	/**
	 * @param url
	 * @return
	 */
	public String replaceGenericParams(String url, TestSuiteDTO suiteData){
		if(url.contains("tenantName"))
			url = url.replace("{tenantName}", suiteData.getTenantName());
		if(url.contains("version"))
			url = url.replace("{version}", suiteData.getBaseVersion());
		return url;
	}
	
	public File createContentTypeFile(String filePath, String fileContentType, String fileExt){
		File originalFile = null, renameFile;
		File newFolder = new File(ProtectTestConstants.PROTECT_RESOURCE_PATH + File.separator + "newFiles");
		if(!newFolder.exists()){
			newFolder.mkdir();
		}
		String timeStamp = generateAlphaNumericString(7);
		if(fileContentType.equals("business")){
			originalFile = new File(filePath + File.separator + "business.docx");
		}
		if(fileContentType.equals("computing")){
			originalFile = new File(filePath + File.separator + "computing.doc");
		}
		if(fileContentType.equals("cryptographic")){
			originalFile = new File(filePath + File.separator + "cryptographic.pem");
		}
		if(fileContentType.equals("design")){
			originalFile = new File(filePath + File.separator + "design.pdf");
		}
		if(fileContentType.equals("encryption")){
			originalFile = new File(filePath + File.separator + "encryption.bin");
		}
		if(fileContentType.equals("engineering")){
			originalFile = new File(filePath + File.separator + "engineering.doc");
		}
		if(fileContentType.equals("health")){
			originalFile = new File(filePath + File.separator + "health.txt");
		}
		if(fileContentType.equals("legal")){
			originalFile = new File(filePath + File.separator + "legal.html");
		}
		if(fileContentType.equals("sourcecode")){
			originalFile = new File(filePath + File.separator + "sourcecode.java");
		}
		String newFileName = "f"+timeStamp+"."+fileExt;
		renameFile = new File(newFolder.toString()+File.separator+newFileName);
		try {
			Files.copy(originalFile, renameFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return renameFile;
	}
	
	public File createSpecialCharacterFile(String filePath, String fileExt) throws IOException{
		File originalFile = null, renameFile;
		File newFolder = new File(filePath + File.separator + "newFiles");
		if(!newFolder.exists()){
			newFolder.mkdir();
		}
		String timeStamp = generateAlphaNumericSpecialCharacterString(9);
		if(fileExt.equals("txt")){
			originalFile = new File(filePath + File.separator + "policyfiletransfer.txt");
		}
		String newFileName = "f"+timeStamp+"."+fileExt;
		String filename = newFileName;
		renameFile = new File(filename);
		renameFile.createNewFile();
		try {
			Files.copy(originalFile, renameFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return renameFile;
	}
	/**
	 * Create Dynamic File (Need to deprecate this)
	 * @param filePath
	 * @param existingFile
	 * @return
	 * @throws IOException
	 */
	public File createDynamicFile(String filePath, File existingFile, String fileExt){
		File originalFile = null, renameFile;
		File newFolder = new File(filePath + File.separator + "newFiles");
		if(!newFolder.exists()){
			newFolder.mkdir();
		}
		String timeStamp = "";
		if(existingFile.getName().startsWith("f")){
			timeStamp = existingFile.getName();
		}else{
			timeStamp = generateAlphaNumericString(7);
		}
		if(fileExt.equals("java")){
			originalFile = new File(filePath + File.separator + "protect.java");
		}else if(fileExt.equals("html")){
			originalFile = new File(filePath + File.separator + "protect.html");
		}else if(fileExt.equals("txt") || fileExt.equals("doc") || fileExt.equals("rtf")){
			originalFile = new File(filePath + File.separator + "policyfiletransfer.txt");
		}else if(fileExt.equals("jpg")){
			originalFile = new File(filePath + File.separator + "protect.jpg");
		}else if(fileExt.equals("html")){
			originalFile = new File(filePath + File.separator + "protect.html");
		}else if(fileExt.equals("exe")){
			originalFile = new File(filePath + File.separator + "protect.exe");
		}else if(fileExt.equals("pdf")){
			originalFile = new File(filePath + File.separator + "protect.pdf");
		}else if(fileExt.equals("docx")){
			originalFile = new File(filePath + File.separator + "protect.docx");
		}else if(fileExt.equals("rar")){
			originalFile = new File(filePath + File.separator + "protect.rar");
		}else if(fileExt.equals("xls")){
			originalFile = new File(filePath + File.separator + "protect.xls");
		}else if(fileExt.equals("xlsm")){
			originalFile = new File(filePath + File.separator + "protect.xlsm");
		}else if(fileExt.equals("cs")){
			originalFile = new File(filePath + File.separator + "protect.cs");
		}else if(fileExt.equals("bin")){
			originalFile = new File(filePath + File.separator + "protect.bin");
		}else if(fileExt.equals("zip")){
			originalFile = new File(filePath + File.separator + "protect.zip");
		}else if(fileExt.equals("js")){
			originalFile = new File(filePath + File.separator + "protect.js");
		}else if(fileExt.equals("avi")){
			originalFile = new File(filePath + File.separator + "protect.avi");
		}else if(fileExt.equals("mxf")){
			originalFile = new File(filePath + File.separator + "protect.mxf");
		}else if(fileExt.equals("mp4")){
			originalFile = new File(filePath + File.separator + "protect.mp4");
		}else if(fileExt.equals("mov")){
			originalFile = new File(filePath + File.separator + "protect.mov");
		}else if(fileExt.equals("webm")){
			originalFile = new File(filePath + File.separator + "protect.webm");
		}
		String newFileName = "f"+timeStamp+"."+fileExt;
		renameFile = new File(newFolder.toString()+File.separator+newFileName);
		try {
			Files.copy(originalFile, renameFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return renameFile;
	}
	
	public File createDynamicFileFormat(String filePath, File existingFile, String fileFormat){
		File originalFile = null, renameFile;
		File newFolder = new File(filePath + File.separator + "newFiles");
		if(!newFolder.exists()){
			newFolder.mkdir();
		}
		String timeStamp = "";
		if(existingFile.getName().startsWith("f")){
			timeStamp = existingFile.getName();
		}else{
			timeStamp = generateAlphaNumericString(7);
		}
		if(fileFormat.equalsIgnoreCase("Animation"))
			originalFile = new File(filePath + File.separator + "animation.dir");
		else if (fileFormat.equalsIgnoreCase("Database"))
			originalFile = new File(filePath + File.separator + "database.mdb");
		else if (fileFormat.equalsIgnoreCase("Encapsulation"))
			originalFile = new File(filePath + File.separator + "encapsulation.dmg");
		else if (fileFormat.equalsIgnoreCase("Executable"))
			originalFile = new File(filePath + File.separator + "executable.bat");
		else if (fileFormat.equalsIgnoreCase("FaxFormat"))
			originalFile = new File(filePath + File.separator + "faxformat.dcx");
		else if (fileFormat.equalsIgnoreCase("FONT"))
			originalFile = new File(filePath + File.separator + "font.otf");
		else if (fileFormat.equalsIgnoreCase("Misc"))
			originalFile = new File(filePath + File.separator + "misc.ASF");
		else if (fileFormat.equalsIgnoreCase("Movie"))
			originalFile = new File(filePath + File.separator + "movie.mpg");
		else if (fileFormat.equalsIgnoreCase("Presentation"))
			originalFile = new File(filePath + File.separator + "presentation.pptm");
		else if (fileFormat.equalsIgnoreCase("RasterImage"))
			originalFile = new File(filePath + File.separator + "rasterimage.bmp");
		else if (fileFormat.equalsIgnoreCase("Sound"))
			originalFile = new File(filePath + File.separator + "sound.ac3");
		else if (fileFormat.equalsIgnoreCase("Spreadsheet"))
			originalFile = new File(filePath + File.separator + "spreadsheets.csv");
		else if (fileFormat.equalsIgnoreCase("VectorGraphic"))
			originalFile = new File(filePath + File.separator + "vectorgraphic.dxf");
		else if (fileFormat.equalsIgnoreCase("WordProcessor"))
			originalFile = new File(filePath + File.separator + "wordprocessor.txt");

		

		String fileName = originalFile.getName();
		String fileNameExt = fileName.substring(fileName.indexOf(".")+1);
		
		
		String newFileName = "f"+timeStamp+"."+fileNameExt;
		renameFile = new File(newFolder.toString()+File.separator+newFileName);
		try {
			Files.copy(originalFile, renameFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return renameFile;
	}	
	
	/**
	 * Create Dynamic File (Need to deprecate this)
	 * @param filePath
	 * @param existingFile
	 * @return
	 * @throws IOException
	 */
	public File createDynamicCIFile(String filePath, File existingFile, String fileExt, String riskType){
		File originalFile = null, renameFile;
		File newFolder = new File(filePath + File.separator + "newFiles");
		if(!newFolder.exists()){
			newFolder.mkdir();
		}
		String timeStamp = "";
		if(existingFile.getName().startsWith("f")){
			timeStamp = existingFile.getName();
		}else{
			timeStamp = generateAlphaNumericString(7);
		}
		
		if(riskType.equalsIgnoreCase("pii"))
			originalFile = new File(filePath + File.separator + "pii.rtf");
		else if(riskType.equalsIgnoreCase("pci"))
			originalFile = new File(filePath + File.separator + "pci.txt");
		else if(riskType.equalsIgnoreCase("hipaa"))
			originalFile = new File(filePath + File.separator + "hipaa.rtf");
		else if(riskType.equalsIgnoreCase("virus"))
			originalFile = new File(filePath + File.separator + "virus.zip");
		else if(riskType.equalsIgnoreCase("ferpa"))
			originalFile = new File(filePath + File.separator + "ferpa.pdf");
		else if(riskType.equalsIgnoreCase("glba"))
			originalFile = new File(filePath + File.separator + "glba.txt");
		else if(riskType.equalsIgnoreCase("encryption"))
			originalFile = new File(filePath + File.separator + "encryption.bin");
		else if(riskType.equalsIgnoreCase("no"))
			originalFile = new File(filePath + File.separator + "encryption.bin");		
		
		
		
		String newFileName = "f"+timeStamp+"."+fileExt;
		renameFile = new File(newFolder.toString()+File.separator+newFileName);
		try {
			Files.copy(originalFile, renameFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return renameFile;
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
	
	
	public Map<String , String> getProtectPolicyViolationCIAlertLogMessage(Client restClient, String fileName, String policyName, List<NameValuePair> requestHeader, TestSuiteDTO suiteData) throws Exception{
		Map<String, String> responseMessages = new HashMap<String, String>();
		HttpResponse response = null;
		ESQueryBuilder queryBuilder = new ESQueryBuilder();
		String restAPI = replaceGenericParams(suiteData.getAPIMap().get("getAnomalyLogs"), suiteData);
		List<String> queryList = new ArrayList<String>();
		queryList.add(fileName);
		queryList.add("User uploaded or modified");
		String source = queryBuilder.getESQueryForPolicyAlert(getDateFromCurrent(-1)+"T12:35:27", getDateFromCurrent(2)+"T12:35:27", queryList);
		//String source = "{\"query\":{\"bool\":{\"must\":[{\"range\":{\"created_timestamp\":{\"from\":\""+getDateFromCurrent(-1)+"T12:35:27\",\"to\":\""+getDateFromCurrent(2)+"T12:35:27\"}}},{\"term\":{\"Activity_type\":\"Policy Violation\"}},{\"term\":{\"severity\":\"critical\"}},{\"term\":{\"policy_action\":\"ALERT\"}},{\"query_string\":{\"query\":\""+fileName+"\"}}]}},\"from\":0,\"size\":30,\"sort\":{\"created_timestamp\":{\"order\":\"desc\"}}}";
		List<NameValuePair> queryParams = new ArrayList<NameValuePair>();		
		queryParams.add(new BasicNameValuePair("source", source));
		queryParams.add(new BasicNameValuePair("sourceName", "PROTECT"));
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(), restAPI,queryParams);
		response = restClient.doGet(dataUri, requestHeader);
		String responseBody = ClientUtil.getResponseBody(response);
		String hits = ClientUtil.getJSONValue(ClientUtil.getJSONValue(responseBody, "hits"), "hits");
		String message = "";
		JSONArray jArray = (JSONArray) new JSONTokener(hits).nextValue();
		for (int i = 0; i < jArray.size(); i++) {
			String _source = ClientUtil.getJSONValue(jArray.getJSONObject(i).toString(), "_source");
			message = ClientUtil.getJSONValue(_source, "message");
			if ((message.contains("[ALERT]")) && (message.contains(fileName)) && (message.contains(policyName))) {
			   if((message.contains("content inspection"))) {
				   //Reporter.log("Json Source Printing...getpolicyViolationCIAlert Message..."+_source, true);
				   populateResponseMap(responseMessages, message, _source); 
				   break;
			   }
			} 
			
		}
		return responseMessages;
	}
	
	public Map<String , String> getProtectPolicyViolationAlertLogMessage(Client restClient, String fileName, String policyName, List<NameValuePair> requestHeader, TestSuiteDTO suiteData) throws Exception{
		Map<String, String> responseMessages = new HashMap<String, String>();
		HttpResponse response = null;
		ESQueryBuilder queryBuilder = new ESQueryBuilder();
		List<String> queryList = new ArrayList<String>();
		queryList.add(fileName);
		String restAPI = replaceGenericParams(suiteData.getAPIMap().get("getAnomalyLogs"), suiteData);
		String source = queryBuilder.getESQueryForPolicyAlert(getDateFromCurrent(-1)+"T12:35:27", getDateFromCurrent(2)+"T12:35:27", queryList);
		//String source = "{\"query\":{\"bool\":{\"must\":[{\"range\":{\"created_timestamp\":{\"from\":\""+getDateFromCurrent(-1)+"T12:35:27\",\"to\":\""+getDateFromCurrent(2)+"T12:35:27\"}}},{\"term\":{\"Activity_type\":\"Policy Violation\"}},{\"term\":{\"severity\":\"critical\"}},{\"term\":{\"policy_action\":\"ALERT\"}},{\"query_string\":{\"query\":\""+fileName+"\"}}]}},\"from\":0,\"size\":30,\"sort\":{\"created_timestamp\":{\"order\":\"desc\"}}}";
		List<NameValuePair> queryParams = new ArrayList<NameValuePair>();		
		queryParams.add(new BasicNameValuePair("source", source));
		queryParams.add(new BasicNameValuePair("sourceName", "PROTECT"));
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(), restAPI,queryParams);
		response = restClient.doGet(dataUri, requestHeader);
		String responseBody = ClientUtil.getResponseBody(response);
		String hits = ClientUtil.getJSONValue(ClientUtil.getJSONValue(responseBody, "hits"), "hits");
		String message = "";
		JSONArray jArray = (JSONArray) new JSONTokener(hits).nextValue();
		for (int i = 0; i < jArray.size(); i++) {
			String _source = ClientUtil.getJSONValue(jArray.getJSONObject(i).toString(), "_source");
			message = ClientUtil.getJSONValue(_source, "message");
			if ((message.contains("[ALERT]")) && (message.contains(fileName)) && (message.contains(policyName))) {
			   if(!(message.contains("content inspection"))) {
				   //Reporter.log("Json Source Printing...getpolicyViolationAlert Message..."+_source, true);
				   populateResponseMap(responseMessages, message, _source); 
				   break;
			   }
			} 
			
		}
		return responseMessages;
	}	
	


	public void verifyAllDataExposurePolicyViolationLogs(Client restClient, List<NameValuePair> requestHeader, TestSuiteDTO suiteData, PolicyBean policyBean) throws Exception{
		String fileName = policyBean.getFileName();
		String policyName = policyBean.getPolicyName();
		
		Map<String, String> alertLogsPolViolMessage = this.getProtectPolicyViolationAlertLogMessage(restClient, fileName,
 				policyName, requestHeader, suiteData);
		Map<String, String> alertLogsPolViolCIMessage = this.getProtectPolicyViolationCIAlertLogMessage(restClient, fileName,
				policyName, requestHeader, suiteData);
 		Map<String, String> investigateLogsPolViolMessage = this.getSecurletPolicyViolationLogs(restClient,
 				policyBean.getCloudService(), fileName, policyName, requestHeader, suiteData);
		Map<String, String> investigateLogsPolViolCIMessage = this.getSecurletCIPolicyViolationLogs(restClient,
				policyBean.getCloudService(), fileName, policyName, requestHeader, suiteData);
		
		
	    String alertPolViolLog = alertLogsPolViolMessage.get(ProtectTestConstants.MESSAGE);
	    String alertPolViolCILog = alertLogsPolViolCIMessage.get(ProtectTestConstants.MESSAGE);
	    String investigatePolViolLog = investigateLogsPolViolMessage.get(ProtectTestConstants.MESSAGE);
	    String investigatePolViolCILog = investigateLogsPolViolCIMessage.get(ProtectTestConstants.MESSAGE);
	    
		// Skip verification and fail the test if both share and ci logs are null
		if (alertPolViolLog == null && alertPolViolCILog == null) {
			Logger.info("Test case failed because Policy Violation Logs are not triggered for the activity performed");
			checkSplunkLogs(suiteData, fileName, false);
			Assert.assertTrue(false, "Test case failed because Policy Violation Logs are not triggered for the activity performed");
			return;
		} 

		// Verify Share Violation log
		if (((alertPolViolLog != null && investigatePolViolLog != null) && alertPolViolCILog == null) || ( (alertPolViolLog !=null && investigatePolViolLog != null) && (alertPolViolCILog!=null && investigatePolViolCILog != null))) {

				Reporter.log("Verification of Policy Violation and Remediation Log for Policy: " + policyName + " and File: "
						+ fileName);
				Reporter.log("Get Policy Alert logs", true);
				Reporter.log("*****************************************************************************************", true);
				Reporter.log("Policy Alert Logs Response:" + alertLogsPolViolMessage, true);
				Reporter.log(
						"#############################Asserting Policy Alerts from Protect#####################################################################",
						true);
				this.assertPolicyViolation(alertLogsPolViolMessage, policyBean.getCloudService(), fileName, policyName);

				Reporter.log("*****************************************************************************************", true);
				Reporter.log("Investigate Logs Response:" + investigateLogsPolViolMessage, true);
				Reporter.log(
						"#############################Asserting Policy Violation Log From Investigate Page#####################################################",
						true);
				this.assertPolicyViolation(investigateLogsPolViolMessage, policyBean.getCloudService(), fileName, policyName);
			if ((policyBean.getRiskType().equalsIgnoreCase("PII") || policyBean.getRiskType().equalsIgnoreCase("PCI")
					|| policyBean.getRiskType().equalsIgnoreCase("HIPAA") || policyBean.getRiskType().equalsIgnoreCase("Virus") || policyBean
					.getRiskType().equalsIgnoreCase("GLBA")) && !policyBean.getPolicyName().startsWith("DELUNIQ"))
					validateContentChecks(policyBean, investigateLogsPolViolMessage);
		}
		
		// Verify CI violation log if share log is null
		else if ((alertPolViolCILog != null && alertPolViolLog ==null)) {
			if (!policyBean.getRiskType().equalsIgnoreCase("no")) {
				Reporter.log("Verification of Content Inspection Policy Violation and Remediation Log for Policy: " + policyName
						+ " and File: " + fileName);
				Reporter.log("Get Policy Alert logs", true);
				Reporter.log("*****************************************************************************************", true);
				Reporter.log("Policy Alert Logs Response:" + alertLogsPolViolCIMessage, true);
				Reporter.log(
						"#############################Asserting Content Inspection  Policy Alerts from Protect#####################################################################",
						true);
				this.assertCIPolicyViolation(alertLogsPolViolCIMessage, policyBean.getCloudService(), fileName,
						policyName);

				Reporter.log("*****************************************************************************************", true);
				Reporter.log("Investigate Logs Response:" + investigateLogsPolViolCIMessage, true);
				Reporter.log(
						"#############################Asserting Content Inspection Policy Violation Log From Investigate Page#####################################################",
						true);
				this.assertCIPolicyViolation(investigateLogsPolViolCIMessage, policyBean.getCloudService(), fileName, policyName);
				if ((policyBean.getRiskType().equalsIgnoreCase("PII") || policyBean.getRiskType().equalsIgnoreCase("PCI")
						|| policyBean.getRiskType().equalsIgnoreCase("HIPAA") || policyBean.getRiskType().equalsIgnoreCase("Virus") || policyBean
						.getRiskType().equalsIgnoreCase("GLBA")) && !policyBean.getPolicyName().startsWith("DELUNIQ"))
						validateContentChecks(policyBean, investigateLogsPolViolCIMessage);
				
			}
		}		
		
		
	}	
	
	public String getPolicyViolationLogs(Client restClient, List<NameValuePair> requestHeader, TestSuiteDTO suiteData, PolicyBean policyBean) throws Exception{
		String fileName = policyBean.getFileName();
		String policyName = policyBean.getPolicyName();
		
		Map<String, String> alertLogsPolViolMessage = this.getProtectPolicyViolationAlertLogMessage(restClient, fileName,
 				policyName, requestHeader, suiteData);
		Map<String, String> alertLogsPolViolCIMessage = this.getProtectPolicyViolationCIAlertLogMessage(restClient, fileName,
				policyName, requestHeader, suiteData);


 		Map<String, String> investigateLogsPolViolMessage = this.getSecurletPolicyViolationLogs(restClient,
 				policyBean.getCloudService(), fileName, policyName, requestHeader, suiteData);
		Map<String, String> investigateLogsPolViolCIMessage = this.getSecurletCIPolicyViolationLogs(restClient,
				policyBean.getCloudService(), fileName, policyName, requestHeader, suiteData);
		
		
	    String investigatePolViolLog = investigateLogsPolViolMessage.get(ProtectTestConstants.MESSAGE);
	    String investigatePolViolCILog = investigateLogsPolViolCIMessage.get(ProtectTestConstants.MESSAGE);
		String alertPolViolLog = alertLogsPolViolMessage.get(ProtectTestConstants.MESSAGE);
	    String alertPolViolCILog = alertLogsPolViolCIMessage.get(ProtectTestConstants.MESSAGE);
	    

	    String polViolMsg = null;
	    String alertPolViolMsg = null;
	    if (investigatePolViolLog != null || investigatePolViolCILog != null) {
	    	polViolMsg = (investigatePolViolLog == null)? investigatePolViolCILog : investigatePolViolLog; 
	    }
	    if (alertPolViolLog != null || alertPolViolCILog != null) {
	    	alertPolViolMsg = (alertPolViolLog == null)? alertPolViolCILog : alertPolViolLog; 
	    }
	    
	    if (polViolMsg != null && alertPolViolMsg != null)
	    	return polViolMsg;
	    else
	    	return null;
		
		
	}	
	
	public void verifyAllDataExposurePolicyViolationLogs(Client restClient, List<NameValuePair> requestHeader, TestSuiteDTO suiteData,
			PolicyBean policyBean, String severity) throws Exception {
		String fileName = policyBean.getFileName();
		String policyName = policyBean.getPolicyName();
		Map<String, String> shareDisplayLogsMessage = this.getSecurletPolicyViolationLogs(restClient, policyBean.getCloudService(),
				fileName, policyName, requestHeader, suiteData);
		Map<String, String> ciDisplayLogsMessage = this.getSecurletCIPolicyViolationLogs(restClient, policyBean.getCloudService(),
				fileName, policyName, requestHeader, suiteData);
		
		if (shareDisplayLogsMessage.isEmpty() && ciDisplayLogsMessage.isEmpty())
			Assert.assertTrue(false, "Policy Violation Alert Logs are not triggered, hence failing the test");
		else if(!shareDisplayLogsMessage.isEmpty()){
			this.assertPolicyViolationForSeverity(shareDisplayLogsMessage, policyBean.getCloudService(), fileName, policyName, severity);
		}else{
			this.assertPolicyViolationForSeverity(ciDisplayLogsMessage, policyBean.getCloudService(), fileName, policyName, severity);
		}
	}
	
	public void assertPolicyViolationForSeverity(Map<String, String> policyLogs, String appName, String fileName, String policyName, String severity){
		Reporter.log("Retriving the Policy Alert logs for - "+policyName, true);
		Reporter.log("***********Expected Log Attributes****************************", true);
		Reporter.log("Severity        - "+severity, true);
		Reporter.log("Policy Action   - "+ProtectTestConstants.ALERT, true);
		Reporter.log("Facility        - "+appName, true);
		Reporter.log("Source          - "+ProtectTestConstants.API, true);
		Reporter.log("Object Name     - "+fileName, true);
		Reporter.log("Policy Violated - "+policyName, true);
		Reporter.log("Policy Type     - "+ProtectTestConstants.FILE_SHARING_API, true);
		Reporter.log("Policy Log1     - "+ProtectTestConstants.ALERT_MESSAGE_1+fileName+ProtectTestConstants.ALERT_MESSAGE_2+policyName, true);
		Reporter.log("Policy Log2     - "+ProtectTestConstants.ALERT_MESSAGE_4+fileName+ProtectTestConstants.ALERT_MESSAGE_5+policyName, true);
		Reporter.log("Activity Type   - "+ProtectTestConstants.POLICY_VIOLATION, true);
		Reporter.log("**********************************************************", true);
		Reporter.log("***********Actual Log Attributes****************************", true);
		Reporter.log("Severity        - "+policyLogs.get(ProtectTestConstants.SEVERITY), true);
		Reporter.log("Policy Action   - "+policyLogs.get(ProtectTestConstants.POLICY_ACTION), true);
		Reporter.log("Facility        - "+policyLogs.get(ProtectTestConstants.FACILITY), true);
		Reporter.log("Source          - "+policyLogs.get(ProtectTestConstants.SOURCE), true);
		Reporter.log("Object Name     - "+policyLogs.get(ProtectTestConstants.OBJECT_NAME), true);
		Reporter.log("Policy Violated - "+policyLogs.get(ProtectTestConstants.POLICY_VIOLATED), true);
		Reporter.log("Policy Type     - "+policyLogs.get(ProtectTestConstants.POLICY_TYPE), true);
		Reporter.log("Policy Log      - "+policyLogs.get(ProtectTestConstants.MESSAGE), true);
		Reporter.log("Activity Type   - "+policyLogs.get(ProtectTestConstants.ACTIVITY_TYPE), true);
		Reporter.log("**********************************************************", true);
		Assert.assertEquals(policyLogs.get(ProtectTestConstants.SEVERITY), severity, "Severity in the policy violation alerts doesn't matches the expected severity: "+severity);
		Assert.assertEquals(policyLogs.get(ProtectTestConstants.POLICY_ACTION), ProtectTestConstants.ALERT);
		Assert.assertEquals(policyLogs.get(ProtectTestConstants.FACILITY), appName);
		Assert.assertEquals(policyLogs.get(ProtectTestConstants.SOURCE), ProtectTestConstants.API);
		Assert.assertEquals(policyLogs.get(ProtectTestConstants.OBJECT_NAME), fileName);
		Assert.assertEquals(policyLogs.get(ProtectTestConstants.POLICY_VIOLATED), policyName);
		Assert.assertEquals(policyLogs.get(ProtectTestConstants.POLICY_TYPE), ProtectTestConstants.FILE_SHARING_API);
		Assert.assertEquals(policyLogs.get(ProtectTestConstants.ACTIVITY_TYPE), ProtectTestConstants.POLICY_VIOLATION);
		Reporter.log("****Verified all the parameters for policy violation/ alert logs for - "+policyName, true);
	}
	
	public void verifyAllDataExposurePolicyViolationLogs(Client restClient, List<NameValuePair> requestHeader, TestSuiteDTO suiteData,
			PolicyBean policyBean, Map<String, String> policyFileMap) throws Exception {
		String fileName = policyBean.getFileName();
		String policyName = policyBean.getPolicyName();
		String noExcPolicyName = null;
		Map<String, String> sharePolicyViolationLogsMessage = this.getProtectPolicyViolationAlertLogMessage(restClient, fileName,
				policyName, requestHeader, suiteData);
		Map<String, String> ciPolicyViolationLogsMessage = this.getProtectPolicyViolationCIAlertLogMessage(restClient, fileName,
				policyName, requestHeader, suiteData);
		Map<String, String> shareDisplayLogsMessage = this.getSecurletPolicyViolationLogs(restClient, policyBean.getCloudService(),
				fileName, policyName, requestHeader, suiteData);
		Map<String, String> ciDisplayLogsMessage = this.getSecurletCIPolicyViolationLogs(restClient, policyBean.getCloudService(),
				fileName, policyName, requestHeader, suiteData);

		String shareLog = sharePolicyViolationLogsMessage.get(ProtectTestConstants.MESSAGE);
		String ciLog = ciPolicyViolationLogsMessage.get(ProtectTestConstants.MESSAGE);
		String shareDisplayLog = shareDisplayLogsMessage.get(ProtectTestConstants.MESSAGE);
		String ciDisplayLog = ciDisplayLogsMessage.get(ProtectTestConstants.MESSAGE);

		if (policyName.endsWith("Exception")) {
			// get logs for the policy to be compared
			if (policyName.endsWith("GrpException"))
				noExcPolicyName = policyName.substring(0, policyName.indexOf("GrpException"));
			if (policyName.endsWith("UsrException"))
				noExcPolicyName = policyName.substring(0, policyName.indexOf("UsrException"));
			String noExcFileName = policyFileMap.get(noExcPolicyName);
			Map<String, String> noExcCIShareDisplayLogsMessage = this.getSecurletCIPolicyViolationLogs(restClient,
					policyBean.getCloudService(), noExcFileName, noExcPolicyName, requestHeader, suiteData);

			Map<String, String> noExcShareDisplayLogsMessage = this.getSecurletPolicyViolationLogs(restClient,
					policyBean.getCloudService(), noExcFileName, noExcPolicyName, requestHeader, suiteData);

			String noExcCIDisplayLog = noExcCIShareDisplayLogsMessage.get(ProtectTestConstants.MESSAGE);
			String noExcShareDisplayLog = noExcShareDisplayLogsMessage.get(ProtectTestConstants.MESSAGE);

			if ((noExcCIDisplayLog != null && ciDisplayLog == null) || (noExcShareDisplayLog != null && shareDisplayLog == null)) {
				if (policyName.endsWith("GrpException"))
					Logger.info("Policy is not violated because of the group level exception for Group: "+policyBean.getFileOwnerGroupException());
				else if (policyName.endsWith("UsrException"))
					Logger.info("Policy is not violated because of the user level exception for User: "+policyBean.getFileOwnerUserException());
				Assert.assertTrue(true);
				return;
			} else if ((noExcCIDisplayLog == null && ciDisplayLog == null) || (noExcShareDisplayLog == null && shareDisplayLog == null)) {
				throw new SkipException("Skipping this test as the test to be compared for exception scenario has got failed");
			} else {
				Assert.assertTrue(false);
				return;
			}

		} else {

			// Skip verification and fail the test if both share and CI logs are null
			if (shareLog == null && ciLog == null) {
				Logger.info("Test case failed because Policy Violation Logs are not triggered for the activity performed after a wait time of 10 minutes");
				Assert.assertTrue(false);
				return;
			}

			// Verify Share Violation log
			if (((shareLog != null && shareDisplayLog != null) && ciLog == null)
					|| ((shareLog != null && shareDisplayLog != null) && (ciLog != null && ciDisplayLog != null))) {

				Reporter.log("Verification of Policy Violation and Remediation Log for Policy: " + policyName + " and File: " + fileName);
				Reporter.log("Get Policy Alert logs", true);
				Reporter.log("*****************************************************************************************", true);
				Reporter.log("Policy Alert Logs Response:" + sharePolicyViolationLogsMessage, true);
				Reporter.log(
						"#############################Asserting Policy Alerts from Protect#####################################################################",
						true);
				this.assertPolicyViolation(sharePolicyViolationLogsMessage, policyBean.getCloudService(), fileName, policyName);

				Reporter.log("*****************************************************************************************", true);
				Reporter.log("Investigate Logs Response:" + shareDisplayLogsMessage, true);
				Reporter.log(
						"#############################Asserting Policy Violation Log From Investigate Page#####################################################",
						true);
				this.assertPolicyViolation(shareDisplayLogsMessage, policyBean.getCloudService(), fileName, policyName);
				if (policyBean.getRiskType().equalsIgnoreCase("PII") || policyBean.getRiskType().equalsIgnoreCase("PCI")
						|| policyBean.getRiskType().equalsIgnoreCase("HIPAA") || policyBean.getRiskType().equalsIgnoreCase("Virus")
						|| policyBean.getRiskType().equalsIgnoreCase("GLBA"))
					validateContentChecks(policyBean, shareDisplayLogsMessage);

			}

			// Verify CI violation log if share log is null
			else if ((ciLog != null && shareLog == null)) {
				if (!policyBean.getRiskType().equalsIgnoreCase("no")) {
					Reporter.log("Verification of Content Inspection Policy Violation and Remediation Log for Policy: " + policyName
							+ " and File: " + fileName);
					Reporter.log("Get Policy Alert logs", true);
					Reporter.log("*****************************************************************************************", true);
					Reporter.log("Policy Alert Logs Response:" + ciPolicyViolationLogsMessage, true);
					Reporter.log(
							"#############################Asserting Content Inspection  Policy Alerts from Protect#####################################################################",
							true);
					this.assertCIPolicyViolation(ciPolicyViolationLogsMessage, policyBean.getCloudService(), fileName, policyName);

					Reporter.log("*****************************************************************************************", true);
					Reporter.log("Investigate Logs Response:" + ciDisplayLogsMessage, true);
					Reporter.log(
							"#############################Asserting Content Inspection Policy Violation Log From Investigate Page#####################################################",
							true);
					this.assertCIPolicyViolation(ciDisplayLogsMessage, policyBean.getCloudService(), fileName, policyName);
					if (policyBean.getRiskType().equalsIgnoreCase("PII") || policyBean.getRiskType().equalsIgnoreCase("PCI")
							|| policyBean.getRiskType().equalsIgnoreCase("HIPAA") || policyBean.getRiskType().equalsIgnoreCase("Virus")
							|| policyBean.getRiskType().equalsIgnoreCase("GLBA"))
						validateContentChecks(policyBean, ciDisplayLogsMessage);

				}
			}
		}

	}
	
	private void validateContentChecks(PolicyBean policyBean, Map<String, String> activityLog)
			throws JsonProcessingException, IOException {
		String source = activityLog.get(ProtectTestConstants.CONTENT_VULNERABILITY);
		Reporter.log("Source..."+source);
		if (source == null) {
			Logger.info("Testcase failed because Content Vulnerabilities are not populated as expected in Policy Violation Logs");
			Assert.assertTrue(false, "Testcase failed because Content Vulnerabilities are not populated as expected in Policy Violation Logs");
			return;
		} else {
			
			String contentChecks = ClientUtil.getJSONValue(source, "content_checks");
			String risk = policyBean.getRiskType().toLowerCase();
			Logger.info("Content Checks..."+contentChecks);
			//content checks should not be null and should have the risk which the policy had
			Assert.assertTrue(contentChecks != null && contentChecks.contains(risk));
		}
		
		// Temporarily commented the changes as Content Vulnerabilities validation are done from DCI automation
/*		String riskType = ClientUtil.getJSONValue(contentChecks, risk);
		String expressions = ClientUtil.getJSONValue(riskType, "expressions");
		String fileExt = policyBean.getFileExt();
		String expectedRisk = null;
		if(fileExt.equals("txt") || fileExt.equals("doc") || fileExt.equals("rtf")){
			fileExt = "txt";
		}
		
		if ((policyBean.getCloudService().equalsIgnoreCase("Office 365") || policyBean.getCloudService().equalsIgnoreCase("Box")) && !risk.equalsIgnoreCase("virus")) {
			expectedRisk = new ProtectFunctions().getExpectedRisk(risk, "protect_od."+fileExt);
		} else if (policyBean.getCloudService().equalsIgnoreCase("Dropbox") && !risk.equalsIgnoreCase("virus")) {
			expectedRisk = new ProtectFunctions().getExpectedRisk(risk, "protect_db."+fileExt);
		} else {
			expectedRisk = new ProtectFunctions().getExpectedRisk(risk, "protect."+fileExt);
		}
		Logger.info("Actual Content Vulnerabilities Details....."+expressions);
		Logger.info("Expected Content Vulnerabilities Details....."+expectedRisk);
		
		Assert.assertTrue(expectedRisk.contains(expressions), "Actual Content Vulnerability details: "+expressions+ " doesn't match the Expected Content Vulnerability details: "+expectedRisk);*/
		
	}
	

	private void populateResponseMap(Map<String, String> responseMessages,
			String message, String _source) throws JsonProcessingException,
			IOException {
		Map result = new ObjectMapper().readValue(_source, HashMap.class);
		
/*	   Iterator<Entry<String, String>> it = result.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        Reporter.log("Response Map: " + pair.getKey().toString() + " = " + pair.getValue().toString());
	    }*/
		
		
		String severity = result.get(ProtectTestConstants.SEVERITY).toString();
		String policyAction = result.get(ProtectTestConstants.POLICY_ACTION).toString();
		String facility = result.get(ProtectTestConstants.FACILITY).toString();
		String __source = result.get(ProtectTestConstants.SOURCE).toString();
		String objectName = result.get(ProtectTestConstants.OBJECT_NAME).toString();
		String policyViolated = result.get(ProtectTestConstants.POLICY_VIOLATED).toString();
		String policyType = result.get(ProtectTestConstants.POLICY_TYPE).toString();
		String activityType = result.get(ProtectTestConstants.ACTIVITY_TYPE).toString();
		String contentChecks = result.get(ProtectTestConstants.CONTENT_CHECKS).toString();

		message = result.get(ProtectTestConstants.MESSAGE).toString();
		responseMessages.put(ProtectTestConstants.SEVERITY, severity);
		responseMessages.put(ProtectTestConstants.POLICY_ACTION, policyAction);
		responseMessages.put(ProtectTestConstants.FACILITY, facility);
		responseMessages.put(ProtectTestConstants.SOURCE, __source);
		responseMessages.put(ProtectTestConstants.OBJECT_NAME, objectName);
		responseMessages.put(ProtectTestConstants.POLICY_VIOLATED, policyViolated);
		responseMessages.put(ProtectTestConstants.POLICY_TYPE, policyType);
		responseMessages.put(ProtectTestConstants.MESSAGE, message);
		responseMessages.put(ProtectTestConstants.ACTIVITY_TYPE, activityType);
		responseMessages.put(ProtectTestConstants.CONTENT_CHECKS, contentChecks);
		responseMessages.put(ProtectTestConstants.CONTENT_VULNERABILITY, _source);
	}
	
	
	
	public Map<String, String> getSecurletPolicyViolationLogs(Client restClient, String appName, String filename, String policyname, List<NameValuePair> requestHeader, TestSuiteDTO suiteData) throws Exception{
		Map<String, String> responseMessages = new HashMap<String, String>();
		HttpResponse response = null;
		ElasticSearchLogs esLogs = new ElasticSearchLogs();
		ESQueryBuilder esQueryBuilder = new ESQueryBuilder();
		String csfrToken = requestHeader.get(0).getValue(); //cookie.substring(cookie.lastIndexOf('=')+1, cookie.lastIndexOf(';'));
		String sessionID = requestHeader.get(4).getValue(); //cookie.substring(cookie.indexOf('d')+2, cookie.indexOf(';'));
		/*String payload = "{\"source\":{\"query\":{\"filtered\":{\"query\":{\"bool\":{\"must\":[{\"range\":{\"created_timestamp\":{\"from\":\""+getDateFromCurrent(-1)+"T12:35:27\",\"to\":\""+getDateFromCurrent(2)+"T12:35:27\"}}},{\"query_string\":{\"query\":\"" 
				+ filename
				+ "\",\"default_operator\":\"AND\",\"analyzer\":\"custom_search_analyzer\",\"allow_leading_wildcard\":\"false\"}},{\"term\":{\"facility\":\""
				+ appName
				+ "\"}}]}},\"filter\":{}}},\"from\":0,\"size\":160,\"sort\":{\"created_timestamp\":{\"order\":\"desc\",\"ignore_unmapped\":\"true\"}}},\"sourceName\":\"investigate\",\"apiServerUrl\":\"http://"+suiteData.getApiserverHostName()+"/\",\"csrftoken\":\""
				+ csfrToken + "\",\"sessionid\":\"" + sessionID + "\",\"userid\":\"" + suiteData.getUsername()
				+ "\"}";*/
		String tsfrom = getDateFromCurrent(-1)+"T12:35:27";
		String tsto = getDateFromCurrent(2)+"T12:35:27";
		String email = suiteData.getUsername();
		String apiServerUrl = suiteData.getApiserverHostName();
		//String payload = esQueryBuilder.getESQuery(tsfrom, tsto, appName, filename, email, apiServerUrl, csfrToken, sessionID, from, size);
		String payload = esQueryBuilder.getSearchQueryForDisplayLogs(tsfrom, tsto, appName, filename, email, apiServerUrl, csfrToken, sessionID);
		StringEntity entity = new StringEntity(payload, "UTF-8");
		response = esLogs.getDisplayLogs(restClient, requestHeader, suiteData.getApiserverHostName(), entity);
		
		String responseBody = ClientUtil.getResponseBody(response);
		String hits = ClientUtil.getJSONValue(ClientUtil.getJSONValue(responseBody, "hits"), "hits");
		JSONArray jArray = (JSONArray) new JSONTokener(hits).nextValue();
		String message = "";
		for (int i = 0; i < jArray.size(); i++) {
			String source = ClientUtil.getJSONValue(jArray.getJSONObject(i).toString(), "_source");
			message = ClientUtil.getJSONValue(source, "message");
			if((message.contains("[ALERT] User shared")) && (message.contains(filename)) && (message.contains(policyname))){
				populateResponseMap(responseMessages, message, source);
				break;
			}
		}
		return responseMessages;
	}
	
	public Map<String, String> getSecurletCIPolicyViolationLogs(Client restClient, String appName, String filename, String policyname, List<NameValuePair> requestHeader, TestSuiteDTO suiteData) throws Exception{
		Map<String, String> responseMessages = new HashMap<String, String>();
		HttpResponse response = null;
		ElasticSearchLogs esLogs = new ElasticSearchLogs();
		ESQueryBuilder esQueryBuilder = new ESQueryBuilder();
		String csfrToken = requestHeader.get(0).getValue(); //cookie.substring(cookie.lastIndexOf('=')+1, cookie.lastIndexOf(';'));
		String sessionID = requestHeader.get(4).getValue(); //cookie.substring(cookie.indexOf('d')+2, cookie.indexOf(';'));
		String tsfrom = getDateFromCurrent(-1)+"T12:35:27";
		String tsto = getDateFromCurrent(2)+"T12:35:27";
		String email = suiteData.getUsername();
		String apiServerUrl = suiteData.getApiserverHostName();
		/*String payload = "{\"source\":{\"query\":{\"filtered\":{\"query\":{\"bool\":{\"must\":[{\"range\":{\"created_timestamp\":{\"from\":\""+getDateFromCurrent(-1)+"T12:35:27\",\"to\":\""+getDateFromCurrent(2)+"T12:35:27\"}}},{\"query_string\":{\"query\":\"User uploaded or modified\"}},{\"query_string\":{\"query\":\"" 
				+ filename
				+ "\",\"default_operator\":\"AND\",\"analyzer\":\"custom_search_analyzer\",\"allow_leading_wildcard\":\"false\"}},{\"term\":{\"facility\":\""
				+ appName
				+ "\"}}]}},\"filter\":{}}},\"from\":0,\"size\":160,\"sort\":{\"created_timestamp\":{\"order\":\"desc\",\"ignore_unmapped\":\"true\"}}},\"sourceName\":\"investigate\",\"apiServerUrl\":\"http://"+suiteData.getApiserverHostName()+"/\",\"csrftoken\":\""
				+ csfrToken + "\",\"sessionid\":\"" + sessionID + "\",\"userid\":\"" + suiteData.getUsername()
				+ "\"}";*/
		String payload = esQueryBuilder.getSearchQueryForDisplayLogs(tsfrom, tsto, appName, filename, email, apiServerUrl, csfrToken, sessionID);
		StringEntity entity = new StringEntity(payload, "UTF-8");
		response = esLogs.getDisplayLogs(restClient, requestHeader, suiteData.getApiserverHostName(), entity);
		String responseBody = ClientUtil.getResponseBody(response);
		String hits = ClientUtil.getJSONValue(ClientUtil.getJSONValue(responseBody, "hits"), "hits");
		JSONArray jArray = (JSONArray) new JSONTokener(hits).nextValue();
		String message = "";
		for (int i = 0; i < jArray.size(); i++) {
			String source = ClientUtil.getJSONValue(jArray.getJSONObject(i).toString(), "_source");
			message = ClientUtil.getJSONValue(source, "message");
			if((message.contains("[ALERT] User")) && (message.contains(filename)) && (message.contains(policyname)) && (message.contains("content inspection"))){
				populateResponseMap(responseMessages, message, source);
				break;
			}
		}
		return responseMessages;
	}
	
	
	public Map<String, String> getInformationalDisplayLogs(Client restClient, String appName, String filename, List<NameValuePair> requestHeader, TestSuiteDTO suiteData, String expectedMessage) throws Exception{
		Map<String, String> responseMessages = new HashMap<String, String>();
		HttpResponse response = null;
		ElasticSearchLogs esLogs = new ElasticSearchLogs();
		ESQueryBuilder esQueryBuilder = new ESQueryBuilder();
		String cookie = requestHeader.get(1).toString();
		String csfrToken = cookie.substring(cookie.lastIndexOf('=')+1, cookie.lastIndexOf(';'));
		String sessionID = cookie.substring(cookie.indexOf('d')+2, cookie.indexOf(';'));
		String tsfrom = getDateFromCurrent(-1)+"T12:35:27";
		String tsto = getDateFromCurrent(2)+"T12:35:27";
		String email = suiteData.getUsername();
		String apiServerUrl = suiteData.getApiserverHostName();
		String payload = esQueryBuilder.getSearchQueryForDisplayLogs(tsfrom, tsto, appName, filename, email, apiServerUrl, csfrToken, sessionID);
		StringEntity entity = new StringEntity(payload);
		response = esLogs.getDisplayLogs(restClient, requestHeader, suiteData.getApiserverHostName(), entity);
		String responseBody = ClientUtil.getResponseBody(response);
		String hits = ClientUtil.getJSONValue(ClientUtil.getJSONValue(responseBody, "hits"), "hits");
		JSONArray jArray = (JSONArray) new JSONTokener(hits).nextValue();
		String message = "";
		for (int i = 0; i < jArray.size(); i++) {
			String source = ClientUtil.getJSONValue(jArray.getJSONObject(i).toString(), "_source");
			message = ClientUtil.getJSONValue(source, "message");
			if(message.contains(expectedMessage)){
				if(source.contains("\""+ProtectTestConstants.SEVERITY+"\"")){
					String severity = ClientUtil.getJSONValue(source, ProtectTestConstants.SEVERITY);
					severity = severity.substring(1, severity.length()-1);
					responseMessages.put(ProtectTestConstants.SEVERITY, severity);
				}
				if(source.contains("\""+ProtectTestConstants.FACILITY+"\"")){
					String facility = ClientUtil.getJSONValue(source, ProtectTestConstants.FACILITY);
					facility = facility.substring(1, facility.length()-1);
					responseMessages.put(ProtectTestConstants.FACILITY, facility);
				}
				if(source.contains("\""+ProtectTestConstants.SOURCE+"\"")){
					String __source = ClientUtil.getJSONValue(source, ProtectTestConstants.SOURCE);
					__source = __source.substring(1, __source.length()-1);
					responseMessages.put(ProtectTestConstants.SOURCE, __source);
				}
				if(source.contains("\""+ProtectTestConstants.NAME+"\"")){
					String name = ClientUtil.getJSONValue(source, ProtectTestConstants.NAME);
					name = name.substring(1, name.length()-1);
					responseMessages.put(ProtectTestConstants.NAME, name);
				}
				if(source.contains("\""+ProtectTestConstants.ACTIVITY_TYPE+"\"")){
					String activityType = ClientUtil.getJSONValue(source, ProtectTestConstants.ACTIVITY_TYPE);
					activityType = activityType.substring(1, activityType.length()-1);
					responseMessages.put(ProtectTestConstants.ACTIVITY_TYPE, activityType);
				}
				if(source.contains("message")){
					message = message.substring(1, message.length()-1);
					responseMessages.put(ProtectTestConstants.MESSAGE, message.trim());
				}
				if(source.contains("\""+ProtectTestConstants.OBJECT_TYPE+"\"")){
					String objectType = ClientUtil.getJSONValue(source, ProtectTestConstants.OBJECT_TYPE);
					objectType = objectType.substring(1, objectType.length()-1);
					responseMessages.put(ProtectTestConstants.OBJECT_TYPE, objectType);
				}
				if(source.contains("\""+ProtectTestConstants.OBJECT+"\"")){
					String object = ClientUtil.getJSONValue(source, ProtectTestConstants.OBJECT);
					object = object.substring(1, object.length()-1);
					responseMessages.put(ProtectTestConstants.OBJECT, object);
				}
				if(source.contains("\""+ProtectTestConstants.ROLE+"\"")){
					String role = ClientUtil.getJSONValue(source, ProtectTestConstants.ROLE);
					role = role.substring(1, role.length()-1);
					responseMessages.put(ProtectTestConstants.ROLE, role);
				}
				if(source.contains("\""+ProtectTestConstants.PREVIOUS_ROLE+"\"")){
					String previousRole = ClientUtil.getJSONValue(source, ProtectTestConstants.PREVIOUS_ROLE);
					previousRole = previousRole.substring(1, previousRole.length()-1);
					responseMessages.put(ProtectTestConstants.PREVIOUS_ROLE, previousRole);
				}
				if(source.contains("\""+ProtectTestConstants.UNSHARED_WITH+"\"")){
					String unsharedWith = ClientUtil.getJSONValue(source, ProtectTestConstants.UNSHARED_WITH);
					unsharedWith = unsharedWith.substring(1, unsharedWith.length()-1);
					responseMessages.put(ProtectTestConstants.UNSHARED_WITH, unsharedWith);
				}
				if(source.contains("\""+ProtectTestConstants.SHARED_WITH+"\"")){
					String sharedWith = ClientUtil.getJSONValue(source, ProtectTestConstants.SHARED_WITH);
					sharedWith = sharedWith.substring(1, sharedWith.length()-1);
					responseMessages.put(ProtectTestConstants.SHARED_WITH, sharedWith);
				}
				if(source.contains("\""+ProtectTestConstants.DOMAIN+"\"")){
					String domain = ClientUtil.getJSONValue(source, ProtectTestConstants.DOMAIN);
					domain = domain.substring(1, domain.length()-1);
					responseMessages.put(ProtectTestConstants.DOMAIN, domain);
				}
				if(source.contains("\""+ProtectTestConstants.USER+"\"")){
					String user = ClientUtil.getJSONValue(source, ProtectTestConstants.USER);
					user = user.substring(1, user.length()-1);
					responseMessages.put(ProtectTestConstants.USER, user);
				}
				if(source.contains("\""+ProtectTestConstants.EXTERNAL_RECIPIENTS+"\"")){
					String externalRecipients = ClientUtil.getJSONValue(source, ProtectTestConstants.EXTERNAL_RECIPIENTS);
					externalRecipients = externalRecipients.substring(1, externalRecipients.length()-1);
					responseMessages.put(ProtectTestConstants.EXTERNAL_RECIPIENTS, externalRecipients);
				}
				if(source.contains("\""+ProtectTestConstants.EXTERNAL_RECIPIENTS+"\"")){
					String subject = ClientUtil.getJSONValue(source, ProtectTestConstants.SUBJECT);
					subject = subject.substring(1, subject.length()-1);
					responseMessages.put(ProtectTestConstants.EXTERNAL_RECIPIENTS, subject);
				}
				break;
			}
		}
		return responseMessages;
	}
	
	/**
	 * Login to Cloud SaaS App
	 * @param appName
	 * @return
	 * @throws Exception
	 */
	//TODO: Code to be cleaned up having a single login call
	public UniversalApi loginToApp(TestSuiteDTO suiteData) throws Exception{
		UserAccount userAccount;
		UniversalApi universalApi = null;
		if(suiteData.getSaasApp().equalsIgnoreCase("Dropbox")){
			userAccount = new UserAccount("protectauto@protectbeatle.com", 
					"Elastica@123", "ADMIN", "jYjld9_6elAAAAAAAAB6gtEQGzGGmviirnoXvOed0niUos4CHqSCGiCY2i2rvntj");
			universalApi = new UniversalApi("DROPBOX", userAccount);
		}else if(suiteData.getSaasApp().equalsIgnoreCase("Box")){
			userAccount = new UserAccount(suiteData.getSaasAppUsername(), suiteData.getSaasAppPassword(), suiteData.getSaasAppUserRole());
			universalApi = new UniversalApi("Box", userAccount);
		}else if(suiteData.getSaasApp().equalsIgnoreCase("GDrive")){
	        String CLIENT_SECRET=ProtectInitializeTests.getRegressionSpecificSuitParameters("saasAppClientSecret");
	        String refreshToken=ProtectInitializeTests.getRegressionSpecificSuitParameters("saasAppRefreshToken");
	        String CLIENT_ID=ProtectInitializeTests.getRegressionSpecificSuitParameters("saasAppClientId");
	        Logger.info("Clent id: "+CLIENT_ID);
	        GDriveAuthorization gDriveAuthorization = new GDriveAuthorization(CLIENT_ID, CLIENT_SECRET);
	        String newAccessToken=gDriveAuthorization.getAceessTokenFromRefreshAccessToken(refreshToken);
			userAccount = new UserAccount("admin@protectbeatle.com", "sHIvgPada(hkJY&u0uOc", "ADMIN",newAccessToken);
			universalApi = new UniversalApi("GDRIVE", userAccount);
		}else if(suiteData.getSaasApp().equalsIgnoreCase("Office365") || suiteData.getSaasApp().equalsIgnoreCase("o365")){
			userAccount = new UserAccount(suiteData.getSaasAppUsername(), suiteData.getSaasAppPassword(), suiteData.getSaasAppUserRole(), suiteData.getEnvironmentName(), null, suiteData.getTenantName());
			universalApi = new UniversalApi("ONEDRIVEBUSINESS", userAccount);
		}if(suiteData.getSaasApp().equalsIgnoreCase("Office365Site")){
			userAccount = new UserAccount(suiteData.getSaasAppUsername(), suiteData.getSaasAppPassword(), 
					suiteData.getSaasAppUserRole(), suiteData.getEnvironmentName(), "site", suiteData.getDomainName());
			universalApi = new UniversalApi("ONEDRIVEBUSINESS", userAccount);
		}
		return universalApi;
	}
	
	public GoogleMailServices loginToGmail(){
		String CLIENT_SECRET=ProtectInitializeTests.getRegressionSpecificSuitParameters("saasAppClientSecret");
		String refreshToken=ProtectInitializeTests.getRegressionSpecificSuitParameters("saasAppRefreshToken");
		String CLIENT_ID=ProtectInitializeTests.getRegressionSpecificSuitParameters("saasAppClientId");
		System.out.println("Client Secret: "+CLIENT_SECRET);
		System.out.println("Client Id: "+CLIENT_ID);
		System.out.println("Refresh Token: "+refreshToken);
		GoogleMailServices googleMailServices = new GoogleMailServices("287386021131-l3tprddtegfkjblib53d5jocmhe5jgft.apps.googleusercontent.com", "wh3fPxSuNwwvs-fDEs3LcCDm", "1/5f5khCvf-WtYayq3KSqrYRCsDKkf09UGP-b4gOdyDAdIgOrJDtdun6zK6XiATCKT");
		googleMailServices.printLabelsInUserAccount();
		return googleMailServices;
	}
	
	public GoogleMailServices loginToGmail(String clientid, String clientsecret, String refreshtoken){
		GoogleMailServices googleMailServices = new GoogleMailServices(clientid, clientsecret, refreshtoken);
		googleMailServices.printLabelsInUserAccount();
		return googleMailServices;
	}
	
	
	public UniversalApi loginToGDrive(String clientId, String clientSecret, String refreshToken, String user, String password, String role) throws Exception{
        GDriveAuthorization gDriveAuthorization = new GDriveAuthorization(clientId, clientSecret);
        String newAccessToken=gDriveAuthorization.getAceessTokenFromRefreshAccessToken(refreshToken);
        UserAccount userAccount = new UserAccount(user, password, role, newAccessToken);
		return new UniversalApi("GDRIVE", userAccount);
	}
	
	
	//TODO: Code to be cleaned up
	public UniversalApi loginToApp(TestSuiteDTO suiteData, String username, String password, String userType) throws Exception{
		UserAccount userAccount;
		UniversalApi universalApi = null;
		 if(suiteData.getSaasApp().equalsIgnoreCase("ONEDRIVEBUSINESS")){
			//userAccount = new UserAccount("Bm.faizan@o365security.net", "Cloudsoc@123", "ADMIN",suiteData.getEnvironmentName(), null, suiteData.getDomainName());
			//userAccount = new UserAccount("qa-admin@o365security.net", "uF4$WCFj8zr@peh", "ADMIN",suiteData.getEnvironmentName(), null, suiteData.getDomainName());
			//userAccount = new UserAccount("qa-stress1@o365security.net", "Aut0mat10n#123", "ENDUSER",suiteData.getEnvironmentName(), null, suiteData.getDomainName());
			//userAccount = new UserAccount("ameer.ali@o365security.net", "n8C#cVcTCTQTJO2", "ENDUSER",suiteData.getEnvironmentName(), null, suiteData.getDomainName());
			userAccount = new UserAccount(username, password, userType,suiteData.getEnvironmentName(), null, suiteData.getDomainName());
			universalApi = new UniversalApi("ONEDRIVEBUSINESS", userAccount);
		}
		return universalApi;
	}
	
	//TODO: Code to be cleaned up
	public UniversalApi loginToApp(TestSuiteDTO suiteData, PolicyBean policyBean) throws Exception{
		UserAccount userAccount;
		UniversalApi universalApi = null;
		if(policyBean.getCloudService().equalsIgnoreCase("Dropbox")){
			//userAccount = new UserAccount("qa-admin@elasticaqa.net", "Elastica#456", "ADMIN");
			userAccount = new UserAccount("protectauto@protectbeatle.com", 
					"Elastica@123", "ADMIN", "jYjld9_6elAAAAAAAAB6gtEQGzGGmviirnoXvOed0niUos4CHqSCGiCY2i2rvntj");
			universalApi = new UniversalApi("DROPBOX", userAccount);
		}else if(policyBean.getCloudService().equalsIgnoreCase("Box")){
			//userAccount = new UserAccount("qa-admin@elasticaqa.net", "Q^0)SDi^CCU2D9Ux$h/GD", "ADMIN");
			userAccount = new UserAccount("box-admin@protectbeatle.com", "WN&VbrsV340L2^t", "ADMIN");
			universalApi = new UniversalApi("Box", userAccount);
		}else if(policyBean.getCloudService().equalsIgnoreCase("Google Drive")){
	        String CLIENT_SECRET="ZnCC0gArfy_2jnbi16S7_2U2";
	        String refreshToken="1/87lxp_CwWVlN9HfMtyMfvOydI9VEoK-rjyq-6uIHOOhIgOrJDtdun6zK6XiATCKT";
	        String CLIENT_ID="598962447624-u80bj7upppl6rdc39imgo6n1piesu9is.apps.googleusercontent.com";
	        Logger.info("Clent id: "+CLIENT_ID);
	        GDriveAuthorization gDriveAuthorization = new GDriveAuthorization(CLIENT_ID, CLIENT_SECRET);
	        String newAccessToken=gDriveAuthorization.getAceessTokenFromRefreshAccessToken(refreshToken);
			userAccount = new UserAccount("admin@protectbeatle.com", "sHIvgPada(hkJY&u0uOc", "ADMIN",newAccessToken);
			universalApi = new UniversalApi("GDRIVE", userAccount);
		}else if(policyBean.getCloudService().equalsIgnoreCase("Office 365")){
			userAccount = new UserAccount("admin@gatewayO365beatle.com", "mVh#BE%HckokLmZ", "ADMIN",suiteData.getEnvironmentName(), null, suiteData.getDomainName());
			universalApi = new UniversalApi("ONEDRIVEBUSINESS", userAccount);
		}
		return universalApi;
	}	
	
	/**
	 * TODO: Code to be cleaned up
	 * @param suiteData
	 * @param saasApp
	 * @return
	 * @throws Exception
	 */
	public UniversalApi loginToApp(TestSuiteDTO suiteData, String saasApp) throws Exception{
		UserAccount userAccount;
		UniversalApi universalApi = null;
		if(saasApp.equalsIgnoreCase("Dropbox")){
			//userAccount = new UserAccount("qa-admin@elasticaqa.net", "Elastica#456", "ADMIN");
			userAccount = new UserAccount("protectauto@protectbeatle.com", 
					"Elastica@123", "ADMIN", "jYjld9_6elAAAAAAAAB6gtEQGzGGmviirnoXvOed0niUos4CHqSCGiCY2i2rvntj");
			universalApi = new UniversalApi("DROPBOX", userAccount);
		}else if(saasApp.equalsIgnoreCase("Box")){
			//userAccount = new UserAccount("qa-admin@elasticaqa.net", "Q^0)SDi^CCU2D9Ux$h/GD", "ADMIN");
			userAccount = new UserAccount("box-admin@protectbeatle.com", "WN&VbrsV340L2^t", "ADMIN");
			universalApi = new UniversalApi("Box", userAccount);
		}else if(saasApp.equalsIgnoreCase("Google Drive")){
	        String CLIENT_SECRET="ZnCC0gArfy_2jnbi16S7_2U2";
	        String refreshToken="1/87lxp_CwWVlN9HfMtyMfvOydI9VEoK-rjyq-6uIHOOhIgOrJDtdun6zK6XiATCKT";
	        String CLIENT_ID="598962447624-u80bj7upppl6rdc39imgo6n1piesu9is.apps.googleusercontent.com";
	        Logger.info("Clent id: "+CLIENT_ID);
	        GDriveAuthorization gDriveAuthorization = new GDriveAuthorization(CLIENT_ID, CLIENT_SECRET);
	        String newAccessToken=gDriveAuthorization.getAceessTokenFromRefreshAccessToken(refreshToken);
			userAccount = new UserAccount("admin@protectbeatle.com", "sHIvgPada(hkJY&u0uOc", "ADMIN",newAccessToken);
			universalApi = new UniversalApi("GDRIVE", userAccount);
		}else if(saasApp.equalsIgnoreCase("Office 365")){
			userAccount = new UserAccount("admin@gatewayO365beatle.com", "mVh#BE%HckokLmZ", "ADMIN",suiteData.getEnvironmentName(), null, suiteData.getDomainName());
			universalApi = new UniversalApi("ONEDRIVEBUSINESS", userAccount);
		}
		return universalApi;
	}	
	
	
	/**
	 * 
	 * @param policyLogs
	 * @param appName
	 * @param fileName
	 * @param policyName
	 */
	public void assertPolicyViolation(Map<String, String> policyLogs, String appName, String fileName, String policyName){
		//Reporter.log("Retriving the Policy Alert logs for - "+policyName, true);
		
		Reporter.log("***********Expected Log Attributes****************************", true);
		//Reporter.log("Severity        - "+ProtectTestConstants.CRITICAL, true);
		Reporter.log("Policy Action   - "+ProtectTestConstants.ALERT, true);
		Reporter.log("Facility        - "+appName, true);
		Reporter.log("Source          - "+ProtectTestConstants.API, true);
		Reporter.log("Object Name     - "+fileName, true);
		Reporter.log("Policy Violated - "+policyName, true);
		Reporter.log("Policy Type     - "+ProtectTestConstants.FILE_SHARING_API, true);
		Reporter.log("Policy Log      - "+ProtectTestConstants.ALERT_MESSAGE_1+fileName+ProtectTestConstants.ALERT_MESSAGE_2+policyName, true);
		Reporter.log("Activity Type   - "+ProtectTestConstants.POLICY_VIOLATION, true);
		Reporter.log("**********************************************************", true);
		Reporter.log("***********Actual Log Attributes****************************", true);
		//Reporter.log("Severity        - "+policyLogs.get(ProtectTestConstants.SEVERITY), true);
		Reporter.log("Policy Action   - "+policyLogs.get(ProtectTestConstants.POLICY_ACTION), true);
		Reporter.log("Facility        - "+policyLogs.get(ProtectTestConstants.FACILITY), true);
		Reporter.log("Source          - "+policyLogs.get(ProtectTestConstants.SOURCE), true);
		Reporter.log("Object Name     - "+policyLogs.get(ProtectTestConstants.OBJECT_NAME), true);
		Reporter.log("Policy Violated - "+policyLogs.get(ProtectTestConstants.POLICY_VIOLATED), true);
		Reporter.log("Policy Type     - "+policyLogs.get(ProtectTestConstants.POLICY_TYPE), true);
		Reporter.log("Policy Log      - "+policyLogs.get(ProtectTestConstants.MESSAGE), true);
		Reporter.log("Activity Type   - "+policyLogs.get(ProtectTestConstants.ACTIVITY_TYPE), true);
		Reporter.log("**********************************************************", true);
		//Assert.assertEquals(policyLogs.get(ProtectTestConstants.SEVERITY), ProtectTestConstants.CRITICAL);
		Assert.assertEquals(policyLogs.get(ProtectTestConstants.POLICY_ACTION), ProtectTestConstants.ALERT);
		Assert.assertEquals(policyLogs.get(ProtectTestConstants.FACILITY), appName);
		Assert.assertEquals(policyLogs.get(ProtectTestConstants.SOURCE), ProtectTestConstants.API);
		Assert.assertEquals(policyLogs.get(ProtectTestConstants.OBJECT_NAME), fileName);
		Assert.assertEquals(policyLogs.get(ProtectTestConstants.POLICY_VIOLATED), policyName);
		Assert.assertEquals(policyLogs.get(ProtectTestConstants.POLICY_TYPE), ProtectTestConstants.FILE_SHARING_API);
		Assert.assertEquals(policyLogs.get(ProtectTestConstants.MESSAGE), ProtectTestConstants.ALERT_MESSAGE_1+fileName+ProtectTestConstants.ALERT_MESSAGE_2+policyName);
		Assert.assertEquals(policyLogs.get(ProtectTestConstants.ACTIVITY_TYPE), ProtectTestConstants.POLICY_VIOLATION);
		Reporter.log("****Verified all the parameters for policy violation/ alert logs for - "+policyName, true);
	}
	
	/**
	 * 
	 * @param policyLogs
	 * @param appName
	 * @param fileName
	 * @param policyName
	 */
	public void assertCIPolicyViolation(Map<String, String> policyLogs, String appName, String fileName, String policyName){
		Reporter.log("***********Expected Log Attributes****************************", true);
		//Reporter.log("Severity        - "+ProtectTestConstants.CRITICAL, true);
		Reporter.log("Policy Action   - "+ProtectTestConstants.ALERT, true);
		Reporter.log("Facility        - "+appName, true);
		Reporter.log("Source          - "+ProtectTestConstants.API, true);
		Reporter.log("Object Name     - "+fileName, true);
		Reporter.log("Policy Violated - "+policyName, true);
		Reporter.log("Policy Type     - "+ProtectTestConstants.FILE_SHARING_API, true);
		Reporter.log("Policy Log      - "+ProtectTestConstants.ALERT_MESSAGE_4+fileName+ProtectTestConstants.ALERT_MESSAGE_5+policyName, true);
		Reporter.log("Activity Type   - "+ProtectTestConstants.POLICY_VIOLATION, true);
		Reporter.log("**********************************************************", true);
		Reporter.log("***********Actual Log Attributes****************************", true);
		//Reporter.log("Severity        - "+policyLogs.get(ProtectTestConstants.SEVERITY), true);
		Reporter.log("Policy Action   - "+policyLogs.get(ProtectTestConstants.POLICY_ACTION), true);
		Reporter.log("Facility        - "+policyLogs.get(ProtectTestConstants.FACILITY), true);
		Reporter.log("Source          - "+policyLogs.get(ProtectTestConstants.SOURCE), true);
		Reporter.log("Object Name     - "+policyLogs.get(ProtectTestConstants.OBJECT_NAME), true);
		Reporter.log("Policy Violated - "+policyLogs.get(ProtectTestConstants.POLICY_VIOLATED), true);
		Reporter.log("Policy Type     - "+policyLogs.get(ProtectTestConstants.POLICY_TYPE), true);
		Reporter.log("Policy Log      - "+policyLogs.get(ProtectTestConstants.MESSAGE), true);
		Reporter.log("Activity Type   - "+policyLogs.get(ProtectTestConstants.ACTIVITY_TYPE), true);
		Reporter.log("**********************************************************", true);
		//Assert.assertEquals(policyLogs.get(ProtectTestConstants.SEVERITY), ProtectTestConstants.CRITICAL);
		Assert.assertEquals(policyLogs.get(ProtectTestConstants.POLICY_ACTION), ProtectTestConstants.ALERT);
		Assert.assertEquals(policyLogs.get(ProtectTestConstants.FACILITY), appName);
		Assert.assertEquals(policyLogs.get(ProtectTestConstants.SOURCE), ProtectTestConstants.API);
		Assert.assertEquals(policyLogs.get(ProtectTestConstants.OBJECT_NAME), fileName);
		Assert.assertEquals(policyLogs.get(ProtectTestConstants.POLICY_VIOLATED), policyName);
		Assert.assertEquals(policyLogs.get(ProtectTestConstants.POLICY_TYPE), ProtectTestConstants.FILE_SHARING_API);
		Assert.assertEquals(policyLogs.get(ProtectTestConstants.MESSAGE), ProtectTestConstants.ALERT_MESSAGE_4+fileName+ProtectTestConstants.ALERT_MESSAGE_5+policyName);
		Assert.assertEquals(policyLogs.get(ProtectTestConstants.ACTIVITY_TYPE), ProtectTestConstants.POLICY_VIOLATION);
		Reporter.log("****Verified all the parameters for policy violation/ alert logs for - "+policyName, true);
	}
	
	/**
	 * 
	 * @param remediationLog
	 * @param messageType
	 * @param appName
	 * @param fileName
	 * @param activityType
	 * @param message
	 */
	public void assertRemediation(Map<String, String> remediationLog, String messageType, String appName, String fileName, String activityType, String message){
		Reporter.log("Retriving log for remedial activity", true);
		Reporter.log("***********Expected Log Attributes****************************", true);
		Reporter.log("Severity      - "+messageType, true);
		Reporter.log("Facility      - "+appName, true);
		Reporter.log("Source        - "+ProtectTestConstants.API, true);
		Reporter.log("Name          - "+fileName, true);
		Reporter.log("Activity Type - "+activityType, true);
		Reporter.log("Message       - "+message, true);
		Reporter.log("**********************************************************", true);
		Reporter.log("***********Actual Log Attributes****************************", true);
		Reporter.log("Severity      - "+remediationLog.get(ProtectTestConstants.SEVERITY), true);
		Reporter.log("Facility      - "+remediationLog.get(ProtectTestConstants.FACILITY), true);
		Reporter.log("Source        - "+remediationLog.get(ProtectTestConstants.SOURCE), true);
		Reporter.log("Name          - "+remediationLog.get(ProtectTestConstants.NAME), true);
		Reporter.log("Activity Type - "+remediationLog.get(ProtectTestConstants.ACTIVITY_TYPE), true);
		Reporter.log("Message       - "+remediationLog.get(ProtectTestConstants.MESSAGE), true);
		Reporter.log("**********************************************************", true);
		Assert.assertEquals(remediationLog.get(ProtectTestConstants.SEVERITY), messageType);
		Assert.assertEquals(remediationLog.get(ProtectTestConstants.FACILITY), appName);
		Assert.assertEquals(remediationLog.get(ProtectTestConstants.SOURCE), ProtectTestConstants.API);
		//Assert.assertEquals(remediationLog.get(ProtectTestConstants.NAME), fileName);
		Assert.assertEquals(remediationLog.get(ProtectTestConstants.ACTIVITY_TYPE), activityType);
		Assert.assertEquals(remediationLog.get(ProtectTestConstants.MESSAGE), message);
		Reporter.log("Verified the Remedial activity attributes - "+remediationLog, true);
	}
	
	/**
	 * 
	 * @param remediationLog
	 * @param messageType
	 * @param appName
	 * @param fileName
	 * @param activityType
	 * @param message
	 */
	public void assertDLPRisk(Map<String, String> remediationLog, String messageType, String appName, String fileName, String activityType, String message){
		Reporter.log("Verifying Vontu DLP Appliance", true);
		Reporter.log("***********Expected Log Attributes****************************", true);
		Reporter.log("Severity      - "+messageType, true);
		Reporter.log("Facility      - "+appName, true);
		Reporter.log("Source        - "+ProtectTestConstants.API, true);
		Reporter.log("Name          - "+fileName, true);
		Reporter.log("Activity Type - "+activityType, true);
		//Reporter.log("Message       - "+message, true);
		Reporter.log("**********************************************************", true);
		Reporter.log("***********Actual Log Attributes****************************", true);
		Reporter.log("Severity      - "+remediationLog.get(ProtectTestConstants.SEVERITY), true);
		Reporter.log("Facility      - "+remediationLog.get(ProtectTestConstants.FACILITY), true);
		Reporter.log("Source        - "+remediationLog.get(ProtectTestConstants.SOURCE), true);
		Reporter.log("Name          - "+remediationLog.get(ProtectTestConstants.NAME), true);
		Reporter.log("Activity Type - "+remediationLog.get(ProtectTestConstants.ACTIVITY_TYPE), true);
		//Reporter.log("Message       - "+remediationLog.get(ProtectTestConstants.MESSAGE), true);
		Reporter.log("**********************************************************", true);
		Assert.assertEquals(remediationLog.get(ProtectTestConstants.SEVERITY), messageType);
		Assert.assertEquals(remediationLog.get(ProtectTestConstants.FACILITY), appName);
		Assert.assertEquals(remediationLog.get(ProtectTestConstants.SOURCE), ProtectTestConstants.API);
		Assert.assertEquals(remediationLog.get(ProtectTestConstants.NAME), fileName);
		Assert.assertEquals(remediationLog.get(ProtectTestConstants.ACTIVITY_TYPE), activityType);
		Logger.info("Verifying Vontu DLP Appliance");
		Assert.assertTrue(remediationLog.get(ProtectTestConstants.MESSAGE).contains("On-prem DLP"), "Vontu DLP Appliance detected any risk");
		Reporter.log("****Verified the Remedial activity attributes - "+remediationLog, true);
	}
	
	/**
	 * 
	 * @param data
	 * @return
	 */
	public PolicyBean populatePolicyBean(String[] data) {
		PolicyBean policyBean = new PolicyBean();
		
		policyBean.setPolicyName(data[0]); // Default to Policyname_ randomString
		policyBean.setPolicyDesc(data[1]); // Default to TestDesc 
		policyBean.setPolicyType(data[2]); // 
		policyBean.setCloudService(data[3]);// If value is null, return with error message
		policyBean.setExposureType(data[4]);// If value is null, default it to public

		policyBean.setFileOwnerUser(data[5]);// any
		policyBean.setFileOwnerGroup(data[6]);//any
		policyBean.setDomainName(data[7]);// any
		policyBean.setFileOwnerUserException(data[8]); // no
		policyBean.setFileOwnerGroupException(data[9]); //no
		policyBean.setSharedWithUser(data[10]);//any

		policyBean.setSharedWithGroup(data[11]);//any
		policyBean.setSharedWithUserException(data[12]);//no
		policyBean.setSharedWithGroupException(data[13]);//no
		policyBean.setFileExt(data[14]);//any
		policyBean.setFileName("no");//any
		//policyBean.setFileType(data[16]);
		policyBean.setFileType("no"); // no
		policyBean.setFileSize(data[17]); // 0.0
		policyBean.setRiskType(data[18]); // no
		policyBean.setCiqProfile(data[19]);// no
		policyBean.setRemediationActivity(data[20]); //no
		if(data.length>21 && data[21]!=null && data[21]!=""){
			policyBean.setSeverity(data[21]);
		}
		if(data.length>22 && data[22]!=null){
			policyBean.setFileFormat(data[22]);
		}
		if(data.length>23 && data[23]!=null){
			policyBean.setNotifySms(data[23]);
		}		
		if(data.length>24 && data[24]!=null){
			policyBean.setCiqProfileException(data[24]);
		}		
		

		return policyBean;
	}
	

	/**
	 * To log test description in reports
	 * @param policyBean
	 */
	public void logTestDescription (PolicyBean policyBean) {
		Reporter.log("****************Policy Description Starts*****************");
		String desc = "This test scenario verifies Protect "+policyBean.getPolicyType()+" Policy with" + "\n";
		desc = desc + "Rules :- " + "\n";
		desc = desc + "CloudService: "+policyBean.getCloudService() + "\n";
		desc = desc + "Users Shared With: "+ ((policyBean.getSharedWithUser() != "no")?policyBean.getSharedWithUser() : "any") + "\n";
		desc = desc + "User Shared With Exception: "+ ((policyBean.getSharedWithUserException() != "no")?policyBean.getSharedWithUserException() : "any") + "\n";
		desc = desc + "Group Shared With: "+((policyBean.getSharedWithGroup() != "no")?policyBean.getSharedWithGroup() : "any") + "\n";
		desc = desc + "Group Shared with exception: "+ ((policyBean.getSharedWithGroupException() != "no")?policyBean.getSharedWithGroupException() : "any") + "\n";
		desc = desc + "Exposure Types: "+((policyBean.getExposureType() != "no")?policyBean.getExposureType() : "any") + "\n";
		desc = desc + "File Name: "+((policyBean.getFileName() != "no")?policyBean.getFileName() : "any") + "\n";
		//desc = desc + "File Type: "+((policyBean.getFileType() != "no")?policyBean.getFileType() : "any") + "\n";
		desc = desc + "Risk Type: "+((policyBean.getRiskType() != "no")?policyBean.getRiskType() : "any") + "\n";
		desc = desc + "Responses:-"+ "\n";
		desc = desc + "Notify: Notify user performing activity"+ "\n";
		desc = desc + "Access Setting: " + ((policyBean.getRemediationActivity() != "no")?policyBean.getRemediationActivity() : "No Remediation Action Selected") + "\n";
		desc = desc + "****************Policy Description Ends*****************";
		
	      String[] lines = desc.split("\\r?\\n");
	      for (String line : lines) {
	         Reporter.log(line);
	      }
	      
	}
	
	public String getTestDescription (PolicyBean policyBean) {
		Reporter.log("****************Test Description Starts*****************");
		String desc = "This test scenario verifies Protect "+policyBean.getPolicyType()+" Policy with" + "\n";
		desc = desc + "Rules :- " + "\n";
		desc = desc + "CloudService: "+policyBean.getCloudService() + "\n";
		desc = desc + "Users Shared With: "+ ((policyBean.getSharedWithUser() != "no")?policyBean.getSharedWithUser() : "any") + "\n";
		desc = desc + "User Shared With Exception: "+ ((policyBean.getSharedWithUserException() != "no")?policyBean.getSharedWithUserException() : "any") + "\n";
		desc = desc + "Group Shared With: "+((policyBean.getSharedWithGroup() != "no")?policyBean.getSharedWithGroup() : "any") + "\n";
		desc = desc + "Group Shared with exception: "+ ((policyBean.getSharedWithGroupException() != "no")?policyBean.getSharedWithGroupException() : "any") + "\n";
		desc = desc + "Exposure Types: "+((policyBean.getExposureType() != "no")?policyBean.getExposureType() : "any") + "\n";
		desc = desc + "File Name: "+((policyBean.getFileName() != "no")?policyBean.getFileName() : "any") + "\n";
		//desc = desc + "File Type: "+((policyBean.getFileType() != "no")?policyBean.getFileType() : "any") + "\n";
		desc = desc + "Risk Type: "+((policyBean.getRiskType() != "no")?policyBean.getRiskType() : "any") + "\n";
		desc = desc + "Responses:-"+ "\n";
		desc = desc + "Notify: Notify user performing activity"+ "\n";
		desc = desc + "Access Setting: " + ((policyBean.getRemediationActivity() != "no")?policyBean.getRemediationActivity() : "No Remediation Action Selected") + "\n";
		desc = desc + "****************Test Description Ends*****************";
		
	      String[] lines = desc.split("\\r?\\n");
	      for (String line : lines) {
	         Reporter.log(line);
	      }
	      return lines.toString();
	}
	
	public FileEntry shareFileOnBox(UniversalApi universalApi, FileUploadResponse fileUploadResponse, BoxFolder folder, String exposureType) throws Exception {
		SharedLink sharedLink = new SharedLink();
		FileEntry fileEntry = null;
		if (exposureType.equalsIgnoreCase("public")) {
			sharedLink.setAccess("open");
			fileEntry = universalApi.createSharedLink(fileUploadResponse.getFileId(), sharedLink);
			Reporter.log("File link shared as public", true);
		} else if (exposureType.equalsIgnoreCase("external")) {
			CollaborationInput collabInput = new CollaborationInput();
			Item item = new Item();
			item.setId(folder.getId());
			item.setType("folder");
			String uniqueName = UUID.randomUUID().toString();
			AccessibleBy aby = new AccessibleBy();
			aby.setName(uniqueName);
			aby.setType("user");
			aby.setLogin("mayurbelekar@gmail.com");
			collabInput.setItem(item);
			collabInput.setAccessibleBy(aby);
			collabInput.setRole("editor");
			BoxCollaboration collaboration = universalApi.createCollaboration(collabInput);
			Logger.info("Collaboration Status:"+collaboration.getStatus());
			while (collaboration.getStatus().equals(String.valueOf(HttpStatus.SC_INTERNAL_SERVER_ERROR))) {
				Logger.info("Resharing because of 500 Internal Server Error occured on sharing in Box SaaS app.");
				collaboration = universalApi.createCollaboration(collabInput);
			
				if (collaboration.getStatus().equals(String.valueOf(HttpStatus.SC_BAD_REQUEST))) {
						Logger.info("400 Bad Request occured as the user is a collaborator already");
						universalApi.deleteCollaboration(collaboration);
						waitForMinutes(2);
						collaboration = universalApi.createCollaboration(collabInput);
				}
				Logger.info("Box Collaboration completed for file: "+folder.getName());
			}
			
			
			Logger.info(collaboration);
			Reporter.log("File link shared with the external user", true);
		} else if (exposureType.equalsIgnoreCase("internal")) {
			CollaborationInput collabInput = new CollaborationInput();
			Item item = new Item();
			item.setId(folder.getId());
			item.setType("folder");
			AccessibleBy aby = new AccessibleBy();
			String uniqueName = UUID.randomUUID().toString();
			aby.setName(uniqueName);
			aby.setType("user");
			aby.setLogin("protectauto1@protectautobeatle.com");
			collabInput.setItem(item);
			collabInput.setAccessibleBy(aby);
			collabInput.setRole("editor");
			universalApi.createCollaboration(collabInput);
			sharedLink.setAccess("company");
			universalApi.createSharedLink(fileUploadResponse.getFileId(), sharedLink);
		}
		return fileEntry;
	}
	
	/**
	 * Sharing with internal and external user on Onedrive
	 * @param itemResourse
	 * @param universalApi
	 * @param roleId
	 * @param exposureType
	 * @return
	 * @throws Exception
	 */
	public DocumentSharingResult shareFileOnOneDrive(ItemResource itemResourse, UniversalApi universalApi, int roleId, String exposureType) throws Exception{
		SharingUserRoleAssignment sharingUserRoleAssignment = new SharingUserRoleAssignment();
		ArrayList<UserRoleAssignment> userRoleAssignmentList = new ArrayList<UserRoleAssignment>();
		UserRoleAssignment userRoleAssignment = new UserRoleAssignment();
		Metadata metadata = new Metadata();
		metadata.setType("SP.Sharing.UserRoleAssignment");
		userRoleAssignment.setMetadata(metadata);
		userRoleAssignment.setRole(roleId);
		if(exposureType.equalsIgnoreCase(ProtectTestConstants.EVERYONE)){
			userRoleAssignment.setUserId("Everyone");
			//sharingUserRoleAssignment = oneDriveUtils.getPublicShareObject(itemResourse.getWebUrl(), roleId);
		} else if(exposureType.equalsIgnoreCase(ProtectTestConstants.INTERNAL)){
			userRoleAssignment.setUserId("Everyone except external users");
		}else if(exposureType.equalsIgnoreCase(ProtectTestConstants.EXTERNAL)){
			userRoleAssignment.setUserId("mayurbelekar@hotmail.com");
			//sharingUserRoleAssignment = new OneDriveUtils().getFileShareObject(itemResourse, roleId, "shriram.natarajan@elastica.co");
		}else if(exposureType.equalsIgnoreCase(ProtectTestConstants.PUBLIC)){
			userRoleAssignment.setUserId("Everyone");
		}
		//DocumentSharingResult documentSharingResult = universalApi.shareWithCollaborators(sharingUserRoleAssignment);
		//Reporter.log(MarshallingUtils.marshall(documentSharingResult), true);
		userRoleAssignmentList.add(userRoleAssignment);
		sharingUserRoleAssignment.setUserRoleAssignments(userRoleAssignmentList);
		sharingUserRoleAssignment.setResourceAddress(itemResourse.getWebUrl());
		sharingUserRoleAssignment.setValidateExistingPermissions(false);
		sharingUserRoleAssignment.setAdditiveMode(true);
		sharingUserRoleAssignment.setSendServerManagedNotification(false);
		sharingUserRoleAssignment.setCustomMessage("This is a custom message");
		if(exposureType.equalsIgnoreCase(ProtectTestConstants.PUBLIC)){
			sharingUserRoleAssignment.setIncludeAnonymousLinksInNotification(true);
		}else{
			sharingUserRoleAssignment.setIncludeAnonymousLinksInNotification(false);
		}
		DocumentSharingResult documentSharingResult = universalApi.shareWithCollaborators(sharingUserRoleAssignment);
		Logger.info(documentSharingResult);
		return documentSharingResult;
	}
	
	
	public void shareFileInODrive(UniversalApi universalApi, ItemResource itemResourse,
			String exposureType) throws Exception {
		if(exposureType.equalsIgnoreCase(ProtectTestConstants.PUBLIC)){
			SharingUserRoleAssignment sharingUserRoleAssignment = new OneDriveUtils().getPublicShareObject(itemResourse.getWebUrl(), 1);
			universalApi.shareWithCollaborators(sharingUserRoleAssignment);
		}if(exposureType.equalsIgnoreCase(ProtectTestConstants.EVERYONE)){
			shareFileOnOneDrive(itemResourse, universalApi, 1, ProtectTestConstants.EVERYONE);
		}if(exposureType.equalsIgnoreCase(ProtectTestConstants.INTERNAL)){
			shareFileOnOneDrive(itemResourse, universalApi, 1, ProtectTestConstants.INTERNAL);
		}if(exposureType.equalsIgnoreCase(ProtectTestConstants.EXTERNAL)){
			shareFileOnOneDrive(itemResourse, universalApi, 1, ProtectTestConstants.EXTERNAL);
		}
	}
	
	
	
	/**
	 * 
	 * @param itemResourse
	 * @param universalApi
	 * @param roleId
	 * @param exposureType
	 * @return
	 * @throws Exception
	 */
	public DocumentSharingResult shareFolderOnOneDrive(Folder itemResourse, UniversalApi universalApi, int roleId, String exposureType) throws Exception{
		SharingUserRoleAssignment sharingUserRoleAssignment = new SharingUserRoleAssignment();
		ArrayList<UserRoleAssignment> userRoleAssignmentList = new ArrayList<UserRoleAssignment>();
		UserRoleAssignment userRoleAssignment = new UserRoleAssignment();
		Metadata metadata = new Metadata();
		metadata.setType("SP.Sharing.UserRoleAssignment");
		userRoleAssignment.setMetadata(metadata);
		userRoleAssignment.setRole(roleId);
		if(exposureType.equalsIgnoreCase(ProtectTestConstants.EVERYONE)){
			userRoleAssignment.setUserId("Everyone");
		} else if(exposureType.equalsIgnoreCase(ProtectTestConstants.INTERNAL)){
			userRoleAssignment.setUserId("Everyone except external users");
		}else if(exposureType.equalsIgnoreCase(ProtectTestConstants.EXTERNAL) ||exposureType.equalsIgnoreCase(ProtectTestConstants.PUBLIC)){
			userRoleAssignment.setUserId("mayurbelekar@hotmail.com");
		}
		userRoleAssignmentList.add(userRoleAssignment);
		sharingUserRoleAssignment.setUserRoleAssignments(userRoleAssignmentList);
		sharingUserRoleAssignment.setResourceAddress(itemResourse.getWebUrl());
		sharingUserRoleAssignment.setValidateExistingPermissions(false);
		sharingUserRoleAssignment.setAdditiveMode(true);
		sharingUserRoleAssignment.setSendServerManagedNotification(false);
		sharingUserRoleAssignment.setCustomMessage("This is a custom message");
		if(exposureType.equalsIgnoreCase(ProtectTestConstants.PUBLIC)){
			sharingUserRoleAssignment.setIncludeAnonymousLinksInNotification(true);
		}else{
			sharingUserRoleAssignment.setIncludeAnonymousLinksInNotification(false);
		}
		DocumentSharingResult documentSharingResult = universalApi.shareWithCollaborators(sharingUserRoleAssignment);
		Logger.info(documentSharingResult);
		return documentSharingResult;
	}
	
	/**
	 * 
	 * @param suiteData
	 * @param headers
	 * @return
	 * @throws Exception
	 */
	public String getProtectPolicyAlertList(TestSuiteDTO suiteData, List<NameValuePair> headers) throws Exception{
		Client restClient = new Client();
		List<NameValuePair> queryParam = new ArrayList<NameValuePair>();	
		queryParam.add(new BasicNameValuePair("filters", "is_alerting|true"));
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(),"/controls/list?filters=is_alerting|true",queryParam);														
		HttpResponse response = restClient.doGet(dataUri, headers);
		String policyAlertsResponse = ClientUtil.getResponseBody(response);
		return policyAlertsResponse;
	}

	public Map<String, Folder> createFolderStructureFor200Files(String[][] stringArray, UniversalApi universalApi) throws Exception {
		Map<String, Folder> folderCollection = new HashMap<String, Folder>();
		String filePath = ProtectTestConstants.PROTECT_RESOURCE_PATH + "policyfiletransfer.txt";
		String mainFolderName = "MF"+UUID.randomUUID().toString();;
		Reporter.log("Creating a top level folder in O365 : " + mainFolderName, true);
		Folder folder = universalApi.createFolder(mainFolderName);
		folderCollection.put(mainFolderName, folder);
		for(int i=0;i<stringArray.length;i++){
			int count = 0;
			int level = Integer.parseInt(stringArray[i][1].toString());
			Logger.info("Creating folder for :"+ stringArray[i][0]);
			count++;
			String folderName = stringArray[i][0]+String.valueOf(count);
			folder = universalApi.createFolder(folderName, folderCollection.get(mainFolderName).getId());
			folderCollection.put(folderName, folder);
			List<String> folderArray = new ArrayList<String>();
			folderArray.add(folderName);
			for(int j=2;j<=level;j++){
				List<String> previousFolderArray = new ArrayList<String>();
				for(int k=0;k<folderArray.size();k++){
					for(int l=1;l<=Integer.parseInt(stringArray[i][2].toString());l++){
						count++;
						folderName = stringArray[i][0]+String.valueOf(count);
						Reporter.log("Creating folder: "+folderName+" - inside folder "+folderArray.get(k), true);
						folder = universalApi.createFolder(folderName, folderCollection.get(folderArray.get(k)).getId());
						folderCollection.put(folderName, folder);
						previousFolderArray.add(folderName);
					}
				}
			folderArray = previousFolderArray;
			}
		}
		return folderCollection;
	}
	
	
	public Map<String, String> createFolderStructureFor200Files(String[][] stringArray, UniversalApi universalApi, GDrive gDrive, String parentFolderId) throws Exception {
		Map<String, String> folderCollection = new HashMap<String, String>();
        List<String> parentFolderIdList = new ArrayList<String>();
        parentFolderIdList.add(parentFolderId);

		for(int i=0;i<stringArray.length;i++){
			int count = 0;
			int level = Integer.parseInt(stringArray[i][1].toString());
			Logger.info("Creating folder for :"+ stringArray[i][0]);
			count++;
			String folderName = stringArray[i][0]+String.valueOf(count);
			
            String folderId_newFolder = gDrive.createFolder(folderName, parentFolderIdList);
			
			folderCollection.put(folderName, folderId_newFolder);
			List<String> folderArray = new ArrayList<String>();
			folderArray.add(folderName);
			for(int j=2;j<=level;j++){
				List<String> previousFolderArray = new ArrayList<String>();
				for(int k=0;k<folderArray.size();k++){
					for(int l=1;l<=Integer.parseInt(stringArray[i][2].toString());l++){
						count++;
						folderName = stringArray[i][0]+String.valueOf(count);
						Reporter.log("Creating folder: "+folderName+" - inside folder "+folderArray.get(k), true);
						
						
				        List<String> pFolderIdList = new ArrayList<String>();
				        pFolderIdList.add(folderId_newFolder);
						String childFolderId = gDrive.createFolder(folderName, pFolderIdList);
						
						
						folderCollection.put(folderName, childFolderId);
						previousFolderArray.add(folderName);
					}
				}
			folderArray = previousFolderArray;
			}
		}
		return folderCollection;
	}
	
	public ArrayList<String> createFolderStructure(String[][] stringArray, UniversalApi universalApi, int noOfFiles) throws Exception {
		Map<String, Folder> folderCollection = new HashMap<String, Folder>();
		List<String> folderList = new ArrayList<String>();
		String filePath = ProtectTestConstants.PROTECT_RESOURCE_PATH + "policyfiletransfer.txt";
		String mainFolderName = "MF"+UUID.randomUUID().toString();;
		Reporter.log("Creating a top level folder in O365 : " + mainFolderName, true);
		Folder folder = universalApi.createFolder(mainFolderName);
		folderCollection.put(mainFolderName, folder);
		for(int i=0;i<stringArray.length;i++){
			int count = 0;
			int level = Integer.parseInt(stringArray[i][1].toString());
			Logger.info("Creating folder for :"+ stringArray[i][0]);
			count++;
			String folderName = stringArray[i][0]+String.valueOf(count);
			folder = universalApi.createFolder(folderName, folderCollection.get(mainFolderName).getId());
			folderCollection.put(folderName, folder);
			folderList.add(folderName);
			List<String> folderArray = new ArrayList<String>();
			folderArray.add(folderName);
			for(int j=2;j<=level;j++){
				List<String> previousFolderArray = new ArrayList<String>();
				for(int k=0;k<folderArray.size();k++){
					for(int l=1;l<=Integer.parseInt(stringArray[i][2].toString());l++){
						count++;
						folderName = stringArray[i][0]+String.valueOf(count);
						Reporter.log("Creating folder: "+folderName+" - inside folder "+folderArray.get(k), true);
						folder = universalApi.createFolder(folderName, folderCollection.get(folderArray.get(k)).getId());
						folderCollection.put(folderName, folder);
						folderList.add(folderName);
						previousFolderArray.add(folderName);
					}
				}
			folderArray = previousFolderArray;
			}
		}
		ArrayList<String> fileList = new ArrayList<String>();
		/*Reporter.log("Start uploading files to all the folders", true);
		ArrayList<String> fileList = new ArrayList<String>();
		for(int i=0;i<folderList.size();i++){
			for(int j=0;j<noOfFiles;j++){
				File file = this.createDynamicFile(ProtectTestConstants.PROTECT_RESOURCE_PATH, new File(filePath),"txt");
				Reporter.log("Upload file "+file.getName()+" to "+folderCollection.get(folderList.get(i)).getName(), true);
				ItemResource itemResource = universalApi.uploadSimpleFile(folderCollection.get(folderList.get(i)).getId(), file.getAbsolutePath(), file.getName());
				//Thread.sleep(10000);
				for(int k=0;k<stringArray.length;k++){
					if(folderCollection.get(folderList.get(i)).getName().startsWith(stringArray[k][0].toString())){
						this.shareFileOnOneDrive(itemResource, universalApi, Integer.parseInt(stringArray[k][4].toString()), stringArray[k][3].toString());
						//Thread.sleep(5000);
					}
				}
				fileList.add(file.getName());
			}
		}
		try {
			String directoryName = ProtectTestConstants.PROTECT_RESOURCE_PATH + "newFiles";
			File directory = new File(directoryName);
			Logger.info(directory.exists());
			if (directory.exists()) {
				FileUtils.deleteDirectory(directory);
			}
		} catch (IOException ioex) {
			ioex.printStackTrace();
		}*/
		return fileList;
	}
	
	public Office365MailActivities sendMailWithAttachment(String saasType, String username, String password, File file, String recipientEmail) {
		Logger.info("Sending email "+file.getName()+" as attachment in saas app is in progress");
		SaasType stype = SaasType.getSaasType(saasType);
		Office365MailActivities objMail=null;
		String fileName = file.getName();
		ArrayList<String> attachment = new ArrayList<String>();
		attachment.add(file.toString());

		try {
			switch (stype) {
			case Office365MailAttachment: {
				Logger.info("Sending email "+fileName+" as attachment in saas app is in progress");
				objMail = new Office365MailActivities(username,password); 
				objMail.sendMail(recipientEmail, fileName+" Mail With Attachment", "This is test mail body", attachment, true);
				Logger.info("Sending email "+file.getName()+" as attachment in saas app is completed");
				break;
			}
			case Office365MailBody: {
				Logger.info("Sending email "+fileName+" as body in saas app is in progress");
				objMail = new Office365MailActivities(username,password); 
				String mailBody = readFile(file.getAbsolutePath());
				objMail.sendMail(recipientEmail, fileName+" Mail With Body", mailBody, "text", null, true);
				Logger.info("Sending email "+fileName+" as body in saas app is completed");
				break;
			}		
			
			
			default: {

				break;
			}
			}
			waitForMinutes(0.5);
		} catch (Exception ex) {
			org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.INFO);

		}

		return objMail;
	}
	
	/**
	 * 
	 * @param remediationLog
	 * @param messageType
	 * @param appName
	 * @param fileName
	 * @param activityType
	 * @param message
	 */
	public void assertRemediationForO365Mail(Map<String, String> remediationLog, String messageType, String appName, String fileName, String activityType, String message){
		Logger.info("Remediation Log Printing....."+remediationLog);
		String remMessage = formatDeleteRemediationMessageForO365Mail(remediationLog.get(ProtectTestConstants.MESSAGE)); 
		Reporter.log("Get log for remedial activity", true);
		Reporter.log("***********Expected Log Attributes****************************", true);
		Reporter.log("Severity      - "+messageType, true);
		Reporter.log("Facility      - "+appName, true);
		Reporter.log("Source        - "+ProtectTestConstants.API, true);
		Reporter.log("Name          - "+fileName, true);
		Reporter.log("Activity Type - "+activityType, true);
		Reporter.log("Object Type - "+"Email Message", true);
		Reporter.log("Message       - "+message, true);
		Reporter.log("**********************************************************", true);
		Reporter.log("***********Actual Log Attributes****************************", true);
		Reporter.log("Severity      - "+remediationLog.get(ProtectTestConstants.SEVERITY), true);
		Reporter.log("Facility      - "+remediationLog.get(ProtectTestConstants.FACILITY), true);
		Reporter.log("Source        - "+remediationLog.get(ProtectTestConstants.SOURCE), true);
		Reporter.log("Name          - "+remediationLog.get(ProtectTestConstants.NAME), true);
		Reporter.log("Activity Type - "+remediationLog.get(ProtectTestConstants.ACTIVITY_TYPE), true);
		Reporter.log("Message       - "+remMessage, true);
		Reporter.log("**********************************************************", true);
		Assert.assertEquals(remediationLog.get(ProtectTestConstants.SEVERITY), messageType);
		Assert.assertEquals(remediationLog.get(ProtectTestConstants.FACILITY), appName);
		Assert.assertEquals(remediationLog.get(ProtectTestConstants.SOURCE), ProtectTestConstants.API);
		Assert.assertEquals(remediationLog.get(ProtectTestConstants.NAME), fileName);
		Assert.assertEquals(remediationLog.get(ProtectTestConstants.ACTIVITY_TYPE), activityType);
		Assert.assertEquals(remMessage, message);
		Reporter.log("****Verified the Remedial activity attributes - "+remediationLog, true);
	}
	
	
	/**
	 * Get Impact details for the policy
	 * 
	 * @param restClient
	 * @return
	 * @throws Exception
	 */
	public HttpResponse calculateImpact(Client restClient, List<NameValuePair> requestHeader, TestSuiteDTO suiteData, PolicyBean policyBean) throws Exception{
		String restAPI = replaceGenericParams(suiteData.getAPIMap().get("calculateImpact"), suiteData);
		StringEntity entity = new StringEntity(getImpactReqBody(policyBean));	
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(),restAPI);		
		return restClient.doPost(dataUri, requestHeader, null, entity);	
	}
	
	
	/**
	 * Get Collabarator details in Impact tab
	 * 
	 * @param restClient
	 * @return
	 * @throws Exception
	 */
	public HttpResponse getCollabDetailsForImpact(Client restClient, List<NameValuePair> requestHeader, TestSuiteDTO suiteData, PolicyBean policyBean, String identification) throws Exception{
		String restAPI = replaceGenericParams(suiteData.getAPIMap().get("getCollabDtlsForImpact"), suiteData);
		List<NameValuePair> queryParams = new ArrayList<NameValuePair>();
		queryParams.add(new BasicNameValuePair("requestType", "detail_collabs"));
		if(suiteData.getSaasApp().equalsIgnoreCase("Google Drive"))
			suiteData.setSaasApp("Google Apps");
		String postBody = "{\"source\":{\"identification\":\""+identification.substring(1, identification.length()-1)+"\",\"app\":\""+suiteData.getSaasApp()+"\"}}";
		Logger.info("collab details post body...."+postBody);
		StringEntity entity = new StringEntity(postBody);	

		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(),restAPI, queryParams);	
		
		return restClient.doPost(dataUri, requestHeader, null, entity);	
	}
	
	
	/**
	 * Get Collabarator details in Impact tab
	 * 
	 * @param restClient
	 * @return
	 * @throws Exception
	 */
	public HttpResponse getCollabDetailsForImpactEoE(Client restClient, List<NameValuePair> requestHeader, TestSuiteDTO suiteData, PolicyBean policyBean, String identification) throws Exception{
		String restAPI = replaceGenericParams(suiteData.getAPIMap().get("getCollabDtlsForImpact"), suiteData);
		List<NameValuePair> queryParams = new ArrayList<NameValuePair>();
		queryParams.add(new BasicNameValuePair("requestType", "detail_collabs"));
		if(suiteData.getSaasApp().equalsIgnoreCase("Google Drive"))
			suiteData.setSaasApp("Google Apps");
		String postBody = "{\"source\":{\"identification\":\""+identification+"\",\"app\":\""+suiteData.getSaasApp()+"\"}}";
		Logger.info("collab details post body...."+postBody);
		StringEntity entity = new StringEntity(postBody);	

		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(),restAPI, queryParams);	
		
		return restClient.doPost(dataUri, requestHeader, null, entity);	
	}
	/**
	 * 
	 * @param remediationMessage
	 * @return
	 */
	private String formatDeleteRemediationMessageForO365Mail(String remediationMessage) {
		// TODO Auto-generated method stub
		Logger.info("Rem message....."+remediationMessage);
		String remMsg1 = remediationMessage.substring(0, remediationMessage.indexOf("email") + "email".length() + 1);
		String remMsg2 = remediationMessage.substring(remediationMessage.indexOf("with"), remediationMessage.indexOf("subject") + "subject".length() + 1);
		String remMsg3 = remediationMessage.substring(remediationMessage.indexOf("\""), remediationMessage.lastIndexOf("\"")-1)+("\"");
		return remMsg1 + remMsg2 + remMsg3;
	}
	
	/**
	  * Generate random string of known characters
	  * @param characters
	  * @param length
	  * @return String
	  */
	 private String generateString(String characters, int length){
	     char[] text = new char[length];
	     for (int i = 0; i < length; i++){
	         text[i] = characters.charAt(new Random().nextInt(characters.length()));
	     }
	     return new String(text);
	 }

	 /**
	  * Generate alphanumeric n characters string
	  * @param length
	  * @return String
	  */
	 public String generateAlphaNumericString(int length) {
		 return generateString(ProtectTestConstants.ALPHA_LOWER_CASE + ProtectTestConstants.ALPHA_UPPER_CASE+ ProtectTestConstants.NUMERIC, length);
	 }
	
	 /**
	  * Generate string with Alphabet number and special character
	  * @param length
	  * @return
	  */
	 public String generateAlphaNumericSpecialCharacterString(int length) {
		 return generateString(ProtectTestConstants.ALPHA_LOWER_CASE + ProtectTestConstants.ALPHA_UPPER_CASE+ ProtectTestConstants.NUMERIC + ProtectTestConstants.SPECIAL_CHARACTER, length);
	 }
	 
	private String getImpactReqBody(PolicyBean policyBean) {
		String isExposed = null;
		String isPublic = null;
		String fileSize = "";
		String contentIq = null;
		String cloudApp = null;
		
		if (policyBean.getFileSize().length() > 0 && policyBean.getFileSize() != "any" && policyBean.getFileSize() != "0,0") {
			fileSize = policyBean.getFileSize();
			String[] tokens = fileSize.split(","); 
			fileSize = "\"larger_than\":"+tokens[0]+",\"smaller_than\":"+tokens[1];
		} 
		
		if (policyBean.getExposureType().equalsIgnoreCase("external")) {
			isExposed = "\"is_exposed\":true,";
		} else {
			isExposed = "\"is_exposed\":false,";
		}
		
		if (policyBean.getExposureType().equalsIgnoreCase("public")) {
			isPublic = "\"is_public\":true,";
		} else{
			isPublic = "\"is_public\":false,";
		}
		
		
/*		if (policyBean.getRiskType().equalsIgnoreCase("no")) {
			contentCheck = "";
		} else{
			contentCheck = "\"content_checks\":[\""+policyBean.getRiskType().toLowerCase()+"\"],";
		}*/
		
		if (policyBean.getRiskType().equalsIgnoreCase("no")) {
			contentIq = "";
		} else{
			contentIq = "\"content_iq_profiles\":[\""+policyBean.getRiskType()+"\"],";
		}		
		
		
		if (policyBean.getCloudService().equalsIgnoreCase("Google Drive")) 
			cloudApp = "\"cloud_app\":\"Google Apps\",\"object_type\":\"Drive\",";
		else if (policyBean.getCloudService().equalsIgnoreCase("Office 365"))
			cloudApp = "\"cloud_app\":\"Office 365\",\"object_type\":\"OneDrive\",";
		else 
			cloudApp = "\"cloud_app\":\""+policyBean.getCloudService()+"\",";
		

		String impactBody = "{\"owned_by\":[],"
				+ "\"owned_by_exclude\":false,"
				+ cloudApp + isExposed + isPublic
				+ "\"exposures\":[\""+policyBean.getExposureType()+"\"],"
				+ "\"collabs\":[],"
				+ "\"collabs_exclude\":false,"
				+ "\"name\":[\""+policyBean.getFileName()+"\"],"
				+ "\"format\":[],"
				+ "\"format_exclude\":false,"
				+ contentIq
				+ "\"content_checks\":[],"
				+ "\"file_size\":{"+fileSize+"},"
				+ "\"limit\":20,"
				+ "\"offset\":0,"
				+ "\"policyType\":\"documentshareapi\","
				+ "\"is_any_exposure\":\"any\","
				+ "\"search_text\":\"\"}";
		Logger.info("Impact Body......:"+impactBody);
		
		return impactBody;
		 
	}
	
	/**
	 * 
	 * @param policyBean
	 * @return
	 */
	private boolean validateDataExposureBeanAndSetDefaultValues(PolicyBean policyBean) {
		if (!StringUtils.isNotEmpty(policyBean.getPolicyName())) 
			policyBean.setPolicyName("PolicyName_"+generateAlphaNumericString(5));
		else if (!StringUtils.isNotEmpty(policyBean.getPolicyDesc())) 
			policyBean.setPolicyDesc("Policy Description");
		else if (!StringUtils.isNotEmpty(policyBean.getCloudService())) 
			return false;
		else if (!StringUtils.isNotEmpty(policyBean.getExposureType()))
			policyBean.setExposureType("public");
		else if (!StringUtils.isNotEmpty(policyBean.getFileOwnerUser()))
			policyBean.setFileOwnerUser("any");
		else if (!StringUtils.isNotEmpty(policyBean.getFileOwnerGroup()))
			policyBean.setFileOwnerGroup("any");
		else if (!StringUtils.isNotEmpty(policyBean.getDomainName()))
			policyBean.setDomainName("any");
		else if (!StringUtils.isNotEmpty(policyBean.getFileOwnerUserException())) 
			policyBean.setFileOwnerUserException("no");
		else if (!StringUtils.isNotEmpty(policyBean.getFileOwnerGroupException())) 	
			policyBean.setFileOwnerGroupException("no");
		else if (!StringUtils.isNotEmpty(policyBean.getSharedWithUser()))
			policyBean.setSharedWithUser("any");
		else if (!StringUtils.isNotEmpty(policyBean.getSharedWithGroup()))
			policyBean.setSharedWithGroup("any");
		else if (!StringUtils.isNotEmpty(policyBean.getSharedWithUserException()))
			policyBean.setSharedWithUserException("no");
		else if (!StringUtils.isNotEmpty(policyBean.getSharedWithGroupException()))
			policyBean.setSharedWithGroupException("no");
		else if (!StringUtils.isNotEmpty(policyBean.getFileExt()))
			policyBean.setFileExt("any");
		else if (!StringUtils.isNotEmpty(policyBean.getFileName()))
			policyBean.setFileName("any");
		else if (!StringUtils.isNotEmpty(policyBean.getFileType()))
			policyBean.setFileType("no");
		else if (!StringUtils.isNotEmpty(policyBean.getFileSize()))
			policyBean.setFileSize("0.0");
		else if (!StringUtils.isNotEmpty(policyBean.getRiskType()))
			policyBean.setRiskType("no");
		else if (!StringUtils.isNotEmpty(policyBean.getCiqProfile()))
			policyBean.setCiqProfile("no");
		else if (!StringUtils.isNotEmpty(policyBean.getRemediationActivity()))
			policyBean.setRemediationActivity("no");
		
		return true;
	}
	
	/**
	 * Get date from the current date based on the number of days
	 * 
	 * @param noOfDays
	 * @return
	 */
	public String getDateFromCurrent(int noOfDays) {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, noOfDays);  // number of days to add
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String date = dateFormat.format(c.getTime());
		return date;
		
	}
	
	
	public String readFile(String filePath) {
		String body="";

		if(filePath.contains(".pdf")){
			try{
				PDDocument document = null; 
				document = PDDocument.load(new File(filePath));
				document.getClass();
				if( !document.isEncrypted() ){
					PDFTextStripperByArea stripper = new PDFTextStripperByArea();
					stripper.setSortByPosition( true );
					PDFTextStripper Tstripper = new PDFTextStripper();
					String st = Tstripper.getText(document);
					body+=st;body+=System.getProperty("line.separator");
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}else if(filePath.contains(".xls")){
			try {
	            FileInputStream file = new FileInputStream(new File(filePath));
	            XSSFWorkbook workbook = new XSSFWorkbook(file);
	            XSSFSheet sheet = workbook.getSheetAt(0);
	            Iterator<Row> rowIterator = sheet.iterator();
	            while (rowIterator.hasNext()){
	                Row row = rowIterator.next();
	                Iterator<Cell> cellIterator = row.cellIterator();
	                while (cellIterator.hasNext()){
	                    Cell cell = cellIterator.next();
	                    switch (cell.getCellType()){
	                        case Cell.CELL_TYPE_NUMERIC:
	                        	body+=cell.getNumericCellValue();body+=System.getProperty("line.separator");
	                            break;
	                        case Cell.CELL_TYPE_STRING:
	                        	body+=cell.getStringCellValue();body+=System.getProperty("line.separator");
	                            break;
	                    }
	                }
	            }
	            file.close();
	        }
	        catch (Exception e)
	        {
	            e.printStackTrace();
	        }
		}else{

			try (BufferedReader br = new BufferedReader(new FileReader(filePath))){

				String sCurrentLine;
				while ((sCurrentLine = br.readLine()) != null) {
					body+=sCurrentLine;body+=System.getProperty("line.separator");
				}

			} catch (IOException e) {
				e.printStackTrace();
			} 

		}

		return body;
	}
	
	/**
	 * Verify impact details
	 * 
	 * @param policyBean
	 * @param fileName
	 * @param responseBody
	 * @return
	 * @throws JsonProcessingException
	 * @throws IOException
	 */
	public String verifyImpactDetails(PolicyBean policyBean, String fileName, String responseBody) throws JsonProcessingException, IOException {
/*		String extCount = null;
		String intCount = null;
		String external = null;
		String expState = null;*/
		String identification = null;
		String impactedFileName = null;
		String ownedBy = null;
		String name = null;
		String files = ClientUtil.getJSONValue(responseBody, "files");
		JSONArray jArray = (JSONArray) new JSONTokener(files).nextValue();
		for (int i = 0; i < jArray.size(); i++) {
			String id = jArray.getJSONObject(i).get("_id").toString();
			// get identification and name from id
			identification = ClientUtil.getJSONValue(id, "identification");
			Logger.info("Identification..." + identification);

			impactedFileName = ClientUtil.getJSONValue(id, "name");
			Logger.info("Impacted File Name..." + impactedFileName);
		    name = jArray.getJSONObject(i).get("name").toString();

			String dataJson = jArray.getJSONObject(i).get("data").toString();

			JSONArray datajArray = (JSONArray) new JSONTokener(dataJson).nextValue();
			for (int j = 0; j < datajArray.size(); j++) {
				ownedBy = datajArray.getJSONObject(i).get("owned_by").toString();

/*				String exposures = datajArray.getJSONObject(i).get("exposures").toString();
				extCount = ClientUtil.getJSONValue(exposures, "ext_count");
				isAllInternal = ClientUtil.getJSONValue(exposures, "all_internal");
				intCount = ClientUtil.getJSONValue(exposures, "int_count");
				internal = ClientUtil.getJSONValue(exposures, "internal");
				external = ClientUtil.getJSONValue(exposures, "external");
				isPublic = ClientUtil.getJSONValue(exposures, "public");*/
			}

		}

		String ownedByExpected = getOwnedBy(policyBean.getCloudService());
		Logger.info("##########################################");
		Logger.info("Impacted File name Verification");
		Logger.info("##########################################");
		Logger.info("*************Expected*********************");
		Logger.info("File Name:"+fileName);
		Logger.info("Number of files: 1");
		Logger.info("Owned By:"+ownedByExpected);
		Logger.info("*************Actual***********************");
		Logger.info("File Name:"+ impactedFileName);
		Logger.info("Number of files:"+ name);
		Logger.info("Owned By:"+ ownedBy);
		Logger.info("##########################################");

		if (impactedFileName == null || name == null || ownedBy == null) {
			Assert.assertTrue(false, "Test case failed because Policy Impact Details are not populated as expected");
			return null;
		}
		
		Assert.assertTrue(impactedFileName.contains(fileName));
		Assert.assertEquals(ownedBy, ownedByExpected);
		
/*		if (policyBean.getCloudService().equalsIgnoreCase("Box")) {
			if (policyBean.getExposureType().equals("external")) {
				Assert.assertEquals(Integer.parseInt(extCount), 1);
				Assert.assertEquals(Integer.parseInt(intCount), 0);
				if (collaborator != null)
					Assert.assertTrue(external.contains(collaborator));
			}
		} */
		
		return identification;
	}
	
	public String getImpactTabCollabDetails(PolicyBean policyBean, String collabResponseBody) throws JsonProcessingException, IOException {
		String detailCollabs = ClientUtil.getJSONValue(collabResponseBody, "detail_collabs");
		Logger.info("====================================");
		Logger.info("Collab Details: "+detailCollabs);
		Logger.info("====================================");
		String userDetails = ClientUtil.getJSONValue(detailCollabs, "collaborators");
		Logger.info("====================================");
		Logger.info("Users Details: "+userDetails);
		Logger.info("====================================");
		return userDetails;
	}
	
	/**
	 * 
	 * @param policyBean
	 * @param collabResponseBody
	 * @return
	 * @throws JsonProcessingException
	 * @throws IOException
	 */
	public String assertCollaboratorDetails(PolicyBean policyBean, String collabResponseBody) throws JsonProcessingException, IOException {
		String detailCollabs = ClientUtil.getJSONValue(collabResponseBody, "detail_collabs");
		String collaborators = ClientUtil.getJSONValue(detailCollabs, "collaborators");
		
		Logger.info("##########################################");
		Logger.info("Collaborator Details Verification");
		Logger.info("##########################################");
		Logger.info("SaaS app: "+policyBean.getCloudService());
		
		if (policyBean.getCloudService().equalsIgnoreCase("Box")) {
			if (policyBean.getRemediationActivity().equalsIgnoreCase("extcollab")) {
			String collabDtls = ClientUtil.getJSONValue(collaborators, "mayurbelekar@gmail.com");
			String userName = ClientUtil.getJSONValue(collabDtls, "username");
			String role = ClientUtil.getJSONValue(collabDtls, "role");
			Logger.info("*************Expected*********************");
			Logger.info("Collaborator User: mitthan@elastica.co");
			Logger.info("Role: editor");
			Logger.info("*************Actual***********************");
			Logger.info("Collaborator User: "+userName.substring(1, userName.length()-1));
			Logger.info("Role: "+role.substring(1, role.length()-1));
			Logger.info("##########################################");
			Assert.assertEquals(userName.substring(1, userName.length()-1), "mayurbelekar@gmail.com");
			Assert.assertEquals(role.substring(1, role.length()-1), "editor");
			} else if (policyBean.getRemediationActivity().equalsIgnoreCase("intcollab")) {
				String collabDtls = ClientUtil.getJSONValue(collaborators, "protectauto1@protectautobeatle.com");
				String userName = ClientUtil.getJSONValue(collabDtls, "username");
				String role = ClientUtil.getJSONValue(collabDtls, "role");
				Logger.info("*************Expected*********************");
				Logger.info("Collaborator User: protectauto1@protectautobeatle.com");
				Logger.info("Role: editor");
				Logger.info("*************Actual***********************");
				Logger.info("Collaborator User: "+userName.substring(1, userName.length()-1));
				Logger.info("Role: "+role.substring(1, role.length()-1));
				Logger.info("##########################################");
				Assert.assertEquals(userName.substring(1, userName.length()-1), "protectauto1@protectautobeatle.com");
				Assert.assertEquals(role.substring(1, role.length()-1), "editor");
			}
		} else if (policyBean.getCloudService().equalsIgnoreCase("Google Drive")) {
			String collabDtls = ClientUtil.getJSONValue(collaborators, "mayurbelekar@gmail.com");
			String userName = ClientUtil.getJSONValue(collabDtls, "username");
			String role = ClientUtil.getJSONValue(collabDtls, "role");
			Logger.info("*************Expected*********************");
			Logger.info("Collaborator User: mayur belekar");
			Logger.info("Role: reader");
			Logger.info("*************Actual***********************");
			Logger.info("Collaborator User: "+userName.substring(1, userName.length()-1));
			Logger.info("Role: "+role.substring(1, role.length()-1));
			Logger.info("##########################################");
			Assert.assertEquals(role.substring(1, role.length()-1), "reader");
			Assert.assertEquals(userName.substring(1, userName.length()-1), "mayur belekar");
			
		} else if (policyBean.getCloudService().equalsIgnoreCase("Office 365")) {
			String collabDtls = ClientUtil.getJSONValue(collaborators, "mayurbelekar@hotmail.com");
			String userName = ClientUtil.getJSONValue(collabDtls, "username");
			String role = ClientUtil.getJSONValue(collabDtls, "role");
			Logger.info("*************Expected*********************");
			Logger.info("Collaborator User: mayurbelekar");
			Logger.info("Role: Read");
			Logger.info("*************Actual***********************");
			Logger.info("Collaborator User: "+userName.substring(1, userName.length()-1));
			Logger.info("Role: "+role.substring(1, role.length()-1));
			Logger.info("##########################################");
			Assert.assertEquals(role.substring(1, role.length()-1), "Read");
			Assert.assertEquals(userName.substring(1, userName.length()-1), "mayurbelekar");
			
		}
		return detailCollabs;
	}
	
	/**
	 * 
	 * @param cloudService
	 * @return
	 */
	private String getOwnedBy(String cloudService) {
		String ownedBy = null;
		
		if (cloudService.equalsIgnoreCase("Box")) {
			ownedBy = "box-admin@protectautobeatle.com";
		} else if (cloudService.equalsIgnoreCase("Dropbox")) {
			ownedBy = "protectauto@protectautobeatle.com";
		} else if (cloudService.equalsIgnoreCase("Google Drive")) {
			ownedBy = "admin@protectautobeatle.com";
		} else if (cloudService.equalsIgnoreCase("Office 365")) {
			ownedBy = "admin@protecto365autobeatle.com";
		}else if (cloudService.equalsIgnoreCase("Salesforce")) {
			ownedBy = "admin@securletbeatle.com";
		}
		return ownedBy;
	}
	
	/**
	 * 
	 * @param policyLogs
	 * @param appName
	 * @param fileName
	 * @param policyName
	 */
	public void assertCIPolicyViolationForO365MailBody(Map<String, String> policyLogs, String appName, String fileName, String policyName){
		//Reporter.log("Retriving the Policy Alert logs for - "+policyName, true);
		Reporter.log("***********Expected Log Attributes****************************", true);
		//Reporter.log("Severity        - "+ProtectTestConstants.CRITICAL, true);
		Reporter.log("Policy Action   - "+ProtectTestConstants.ALERT, true);
		Reporter.log("Facility        - "+appName, true);
		Reporter.log("Source          - "+ProtectTestConstants.API, true);
		Reporter.log("Object Name     - "+fileName+ " Mail With Body", true);
		Reporter.log("Policy Violated - "+policyName, true);
		Reporter.log("Policy Type     - "+ProtectTestConstants.FILE_SHARING_API, true);
		Reporter.log("Policy Log      - "+ProtectTestConstants.ALERT_MESSAGE_4+fileName+ProtectTestConstants.ALERT_MESSAGE_3+policyName, true);
		Reporter.log("Activity Type   - "+ProtectTestConstants.POLICY_VIOLATION, true);
		Reporter.log("**********************************************************", true);
		Reporter.log("***********Actual Log Attributes****************************", true);
		//Reporter.log("Severity        - "+policyLogs.get(ProtectTestConstants.SEVERITY), true);
		Reporter.log("Policy Action   - "+policyLogs.get(ProtectTestConstants.POLICY_ACTION), true);
		Reporter.log("Facility        - "+policyLogs.get(ProtectTestConstants.FACILITY), true);
		Reporter.log("Source          - "+policyLogs.get(ProtectTestConstants.SOURCE), true);
		Reporter.log("Object Name     - "+policyLogs.get(ProtectTestConstants.OBJECT_NAME), true);
		Reporter.log("Policy Violated - "+policyLogs.get(ProtectTestConstants.POLICY_VIOLATED), true);
		Reporter.log("Policy Type     - "+policyLogs.get(ProtectTestConstants.POLICY_TYPE), true);
		Reporter.log("Policy Log      - "+policyLogs.get(ProtectTestConstants.MESSAGE), true);
		Reporter.log("Activity Type   - "+policyLogs.get(ProtectTestConstants.ACTIVITY_TYPE), true);
		Reporter.log("**********************************************************", true);
		//Assert.assertEquals(policyLogs.get(ProtectTestConstants.SEVERITY), ProtectTestConstants.CRITICAL);
		Assert.assertEquals(policyLogs.get(ProtectTestConstants.POLICY_ACTION), ProtectTestConstants.ALERT);
		Assert.assertEquals(policyLogs.get(ProtectTestConstants.FACILITY), appName);
		Assert.assertEquals(policyLogs.get(ProtectTestConstants.SOURCE), ProtectTestConstants.API);
		Assert.assertEquals(policyLogs.get(ProtectTestConstants.OBJECT_NAME), fileName+" Mail With Body");
		Assert.assertEquals(policyLogs.get(ProtectTestConstants.POLICY_VIOLATED), policyName);
		Assert.assertEquals(policyLogs.get(ProtectTestConstants.POLICY_TYPE), ProtectTestConstants.FILE_SHARING_API);
		Assert.assertEquals(policyLogs.get(ProtectTestConstants.MESSAGE), ProtectTestConstants.ALERT_MESSAGE_4+fileName+ProtectTestConstants.ALERT_MESSAGE_3+policyName);
		Assert.assertEquals(policyLogs.get(ProtectTestConstants.ACTIVITY_TYPE), ProtectTestConstants.POLICY_VIOLATION);
		Reporter.log("****Verified all the parameters for policy violation/ alert logs for - "+policyName, true);
	}
	
	public void assertPolicyViolationForO365MailBody(Map<String, String> policyLogs, String appName, String fileName, String policyName){
		Reporter.log("Retriving the Policy Alert logs for - "+policyName, true);
		Reporter.log("***********Expected Log Attributes****************************", true);
		//Reporter.log("Severity        - "+ProtectTestConstants.CRITICAL, true);
		Reporter.log("Policy Action   - "+ProtectTestConstants.ALERT, true);
		Reporter.log("Facility        - "+appName, true);
		Reporter.log("Source          - "+ProtectTestConstants.API, true);
		Reporter.log("Object Name     - "+fileName+ " Mail With Body", true);
		Reporter.log("Policy Violated - "+policyName, true);
		Reporter.log("Policy Type     - "+ProtectTestConstants.FILE_SHARING_API, true);
		Reporter.log("Policy Log      - "+ProtectTestConstants.ALERT_MESSAGE_1+fileName+ProtectTestConstants.ALERT_MESSAGE_2+policyName, true);
		Reporter.log("Activity Type   - "+ProtectTestConstants.POLICY_VIOLATION, true);
		Reporter.log("**********************************************************", true);
		Reporter.log("***********Actual Log Attributes****************************", true);
		Reporter.log("Severity        - "+policyLogs.get(ProtectTestConstants.SEVERITY), true);
		Reporter.log("Policy Action   - "+policyLogs.get(ProtectTestConstants.POLICY_ACTION), true);
		Reporter.log("Facility        - "+policyLogs.get(ProtectTestConstants.FACILITY), true);
		Reporter.log("Source          - "+policyLogs.get(ProtectTestConstants.SOURCE), true);
		Reporter.log("Object Name     - "+policyLogs.get(ProtectTestConstants.OBJECT_NAME), true);
		Reporter.log("Policy Violated - "+policyLogs.get(ProtectTestConstants.POLICY_VIOLATED), true);
		Reporter.log("Policy Type     - "+policyLogs.get(ProtectTestConstants.POLICY_TYPE), true);
		Reporter.log("Policy Log      - "+policyLogs.get(ProtectTestConstants.MESSAGE), true);
		Reporter.log("Activity Type   - "+policyLogs.get(ProtectTestConstants.ACTIVITY_TYPE), true);
		Reporter.log("**********************************************************", true);
		//Assert.assertEquals(policyLogs.get(ProtectTestConstants.SEVERITY), ProtectTestConstants.CRITICAL);
		Assert.assertEquals(policyLogs.get(ProtectTestConstants.POLICY_ACTION), ProtectTestConstants.ALERT);
		Assert.assertEquals(policyLogs.get(ProtectTestConstants.FACILITY), appName);
		Assert.assertEquals(policyLogs.get(ProtectTestConstants.SOURCE), ProtectTestConstants.API);
		Assert.assertEquals(policyLogs.get(ProtectTestConstants.OBJECT_NAME), fileName+" Mail With Body");
		Assert.assertEquals(policyLogs.get(ProtectTestConstants.POLICY_VIOLATED), policyName);
		Assert.assertEquals(policyLogs.get(ProtectTestConstants.POLICY_TYPE), ProtectTestConstants.FILE_SHARING_API);
		Assert.assertEquals(policyLogs.get(ProtectTestConstants.MESSAGE), ProtectTestConstants.ALERT_MESSAGE_1+fileName+ProtectTestConstants.ALERT_MESSAGE_2+policyName);
		Assert.assertEquals(policyLogs.get(ProtectTestConstants.ACTIVITY_TYPE), ProtectTestConstants.POLICY_VIOLATION);
		Reporter.log("Verified all the parameters for policy violation/ alert logs for - "+policyName, true);
	}
	
	public void googleDriveRemediationVerification(Client restClient, List<NameValuePair> requestHeader, TestSuiteDTO suiteData, PolicyBean policyBean, String fileName, String fileId) throws Exception{
		Reporter.log("Retriving remediation logs for "+policyBean.getPolicyName(), true);
		UniversalApi universalApi = this.loginToApp(suiteData);
		GDrive gDrive = universalApi.getgDrive();
		Map<String, String> messagesLogs = new HashMap<String, String>();
		String[] remActivities = policyBean.getRemediationActivity().split(",");

		for (String remediation : remActivities) {
		
		if(remediation.contains("unshare")){
			String unshareMessage = "User unshared " + fileName;
			messagesLogs = this.getInformationalDisplayLogs(restClient, policyBean.getCloudService(),
					fileName, requestHeader, suiteData, unshareMessage);
			
			if (messagesLogs.get(ProtectTestConstants.MESSAGE) == null) {
				Logger.info("Remediation Logs is not found in the portal, hence test case failed");
				checkSplunkLogs(suiteData, fileName, false);
				Assert.assertTrue(false);
			}
			
			this.assertRemediation(messagesLogs, ProtectTestConstants.INFORMATIONAL, policyBean.getCloudService(),
					fileName, ProtectTestConstants.UNSHARE, unshareMessage);
			//Assert.assertEquals(gDrive.retrievePermissionList(fileId).getItems().size(), 1);
			
		}
		if(remediation.contains("reader") || remediation.contains("writer")){
			String roleChangeMessage = "User changed permission on file " + fileName;
			messagesLogs = this.getInformationalDisplayLogs(restClient, policyBean.getCloudService(),
					fileName, requestHeader, suiteData, roleChangeMessage);
			if (messagesLogs.get(ProtectTestConstants.MESSAGE) == null) {
				Logger.info("Remediation Logs is not found in the portal, hence test case failed");
				checkSplunkLogs(suiteData, fileName, false);
				Assert.assertTrue(false);
			}
			this.assertRemediation(messagesLogs, ProtectTestConstants.INFORMATIONAL, policyBean.getCloudService(), fileName,
					ProtectTestConstants.ROLE_CHANGE, roleChangeMessage);
			Assert.assertEquals(messagesLogs.get(ProtectTestConstants.ROLE), policyBean.getRemediationActivity());
			Assert.assertEquals(messagesLogs.get(ProtectTestConstants.PREVIOUS_ROLE), policyBean.getPolicyType());
/*			Reporter.log("Gdrive SaasApp Remediation Role : "+gDrive.retrievePermissionList(fileId).getItems().get(1).getRole(), true);
			Assert.assertEquals(gDrive.retrievePermissionList(fileId).getItems().get(1).getRole()
					, policyBean.getRemediationActivity());*/
		}
		if(remediation.contains("commenter")){
			String roleChangeMessage = "User changed permission on file " + fileName;
			messagesLogs = this.getInformationalDisplayLogs(restClient, policyBean.getCloudService(),
					fileName, requestHeader, suiteData, roleChangeMessage);
			if (messagesLogs.get(ProtectTestConstants.MESSAGE) == null) {
				Logger.info("Remediation Logs is not found in the portal, hence test case failed");
				checkSplunkLogs(suiteData, fileName, false);
				Assert.assertTrue(false);
			}
			this.assertRemediation(messagesLogs, ProtectTestConstants.INFORMATIONAL, policyBean.getCloudService(), fileName,
					ProtectTestConstants.ROLE_CHANGE, roleChangeMessage);
			Assert.assertEquals(messagesLogs.get(ProtectTestConstants.ROLE), policyBean.getRemediationActivity());
			Assert.assertEquals(messagesLogs.get(ProtectTestConstants.PREVIOUS_ROLE), policyBean.getPolicyType());
/*			Reporter.log("Gdrive SaasApp Remediation Role : "+gDrive.retrievePermissionList(fileId).getItems().get(1).getAdditionalRoles().get(0), true);
			Assert.assertEquals(gDrive.retrievePermissionList(fileId).getItems().get(1).getAdditionalRoles().get(0)
					, policyBean.getRemediationActivity());*/
		}
		if(remediation.contains("preventWritersCanShare")){
			String remediationMessage = "Prevent writers from sharing " + fileName;
			messagesLogs = this.getInformationalDisplayLogs(restClient, policyBean.getCloudService(),
					fileName, requestHeader, suiteData, remediationMessage);
			if (messagesLogs.get(ProtectTestConstants.MESSAGE) == null) {
				Logger.info("Remediation Logs is not found in the portal, hence test case failed");
				checkSplunkLogs(suiteData, fileName, false);
				Assert.assertTrue(false);
			}
			this.assertRemediation(messagesLogs, ProtectTestConstants.INFORMATIONAL, policyBean.getCloudService(),
					fileName, ProtectTestConstants.RESTRICT, remediationMessage);
			//SaaS App Side remediation is pending 
/*			String clientId = ProtectInitializeTests.getRegressionSpecificSuitParameters("externalgdriveClientId");
			String clientSecret = ProtectInitializeTests.getRegressionSpecificSuitParameters("externalgdriveClientSecret");
			String refreshtoken = ProtectInitializeTests.getRegressionSpecificSuitParameters("externalgdriveRefreshToken");
			String externalUser = ProtectInitializeTests.getRegressionSpecificSuitParameters("externalgdriveUser");
			universalApi = this.loginToGDrive(clientId, clientSecret, refreshtoken, externalUser, "******", "USER");
			gDrive = universalApi.getgDrive();
			String permissionResponse;
			try{
				Permission permission =  gDrive.insertPermission(gDrive.getDriveService(), fileId, externalUser, "user", policyBean.getPolicyType());
				System.out.println(permission.getValue());
				permissionResponse=permission.toPrettyString();
			}catch(Exception e){
				permissionResponse=e.getMessage();
			}
			Assert.assertTrue(permissionResponse.contains("You do not have permission to share these item(s)"));*/
		}
		if(remediation.contains("preventCopyPrintDownload")){
			String remediationMessage = "Prevent copy, print and download of " + fileName;
			messagesLogs = this.getInformationalDisplayLogs(restClient, policyBean.getCloudService(),
					fileName, requestHeader, suiteData, remediationMessage);
			if (messagesLogs.get(ProtectTestConstants.MESSAGE) == null) {
				Logger.info("Remediation Logs is not found in the portal, hence test case failed");
				checkSplunkLogs(suiteData, fileName, false);
				Assert.assertTrue(false);
			}
			this.assertRemediation(messagesLogs, ProtectTestConstants.INFORMATIONAL, policyBean.getCloudService(),
					fileName, ProtectTestConstants.RESTRICT, remediationMessage);
		}
		if (remediation.contains("removeCollab")) {
			String unshareMessage = "User unshared " + fileName;
			messagesLogs = this.getInformationalDisplayLogs(restClient, policyBean.getCloudService(), fileName, requestHeader,
					suiteData, unshareMessage);
			if (messagesLogs.get(ProtectTestConstants.MESSAGE) == null) {
				Logger.info("Remediation Logs is not found in the portal, hence test case failed");
				checkSplunkLogs(suiteData, fileName, false);
				Assert.assertTrue(false);
			}
			this.assertRemediation(messagesLogs, ProtectTestConstants.INFORMATIONAL, policyBean.getCloudService(), fileName,
					ProtectTestConstants.UNSHARE, unshareMessage);
		}
		if (remediation.contains("updateWriter") || remediation.contains("updateReader") || remediation.contains("updateCommenter")) {
			String updatePermissionMessage = "User changed permission on file " + fileName;
			messagesLogs = this.getInformationalDisplayLogs(restClient, policyBean.getCloudService(),
					fileName, requestHeader, suiteData, updatePermissionMessage);
			if (messagesLogs.get(ProtectTestConstants.MESSAGE) == null) {
				Logger.info("Remediation Logs is not found in the portal, hence test case failed");
				checkSplunkLogs(suiteData, fileName, false);
				Assert.assertTrue(false);
			}
			this.assertRemediation(messagesLogs, ProtectTestConstants.INFORMATIONAL, policyBean.getCloudService(),
					fileName, ProtectTestConstants.ROLE_CHANGE, updatePermissionMessage);
/*			if(remediation.equals("updateWriter")){
				Reporter.log("Gdrive SaasApp Remediation Role : "+gDrive.retrievePermissionList(fileId).getItems().get(1).getRole(), true);
				Assert.assertEquals(gDrive.retrievePermissionList(fileId).getItems().get(1).getRole(), "writer");
			}
			if(remediation.equals("updateReader")){
				Reporter.log("Gdrive SaasApp Remediation Role : "+gDrive.retrievePermissionList(fileId).getItems().get(1).getRole(), true);
				Assert.assertEquals(gDrive.retrievePermissionList(fileId).getItems().get(1).getRole(), "reader");
			}
			if(remediation.equals("updateCommenter")){
				Reporter.log("Gdrive SaasApp Remediation Role : "+gDrive.retrievePermissionList(fileId).getItems().get(1).getAdditionalRoles().get(0), true);
				Assert.assertEquals(gDrive.retrievePermissionList(fileId).getItems().get(1).getAdditionalRoles().get(0), "commenter");
			}*/
			
		}
		if (remediation.equals("linkWriter") || remediation.equals("linkReader") || remediation.equals("linkCommenter")
				|| remediation.equals("companyWriter") || remediation.equals("companyReader") || remediation.equals("companyCommenter")
				|| remediation.equals("companyLinkWriter") || remediation.equals("companyLinkReader") || remediation.equals("companyLinkCommenter")) {
			
			String queryMessage = null;
			String shareMessage = "User shared " + fileName;
			
			if (remediation.equals("linkWriter") || remediation.equals("linkReader") || remediation.equals("linkCommenter")) 
				queryMessage = shareMessage + " AND Public(link)";				
			else if (remediation.equals("companyWriter") || remediation.equals("companyReader") || remediation.equals("companyCommenter"))
				queryMessage = shareMessage + " All Internal";				
			else if (remediation.equals("companyLinkWriter") || remediation.equals("companyLinkReader") || remediation.equals("companyLinkCommenter"))
				queryMessage = shareMessage + " All Internal(with link)";			


			messagesLogs = this.getInformationalDisplayLogs(restClient, policyBean.getCloudService(),
					queryMessage, requestHeader, suiteData, shareMessage);
			if (messagesLogs.get(ProtectTestConstants.MESSAGE) == null) {
				Logger.info("Remediation Logs is not found in the portal, hence test case failed");
				checkSplunkLogs(suiteData, fileName, false);
				Assert.assertTrue(false);
			}
			this.assertRemediation(messagesLogs, ProtectTestConstants.INFORMATIONAL, policyBean.getCloudService(), fileName,
					ProtectTestConstants.SHARE, shareMessage);
		 }
		}
	}
	
	public void o365RemediationVerification(Client restClient, String fileName, List<NameValuePair> requestHeader, TestSuiteDTO suiteData, PolicyBean policyBean) throws Exception{
		Reporter.log("Retriving remediation logs for "+policyBean.getPolicyName(), true);
		String[] remActivities = policyBean.getRemediationActivity().split(",");
		
		for (String remediation : remActivities) {
		
		if(remediation.equalsIgnoreCase("unshare")){
			String remediationMessage = "User unshared '"+fileName+"'";
			String unsharedWithMessage = "";
			if(policyBean.getExposureType().equals(ProtectTestConstants.INTERNAL)){
				unsharedWithMessage = ProtectTestConstants.EVERYONE_EXCEPT_EXTERNAL_USERS;
			}
			if(policyBean.getExposureType().equals(ProtectTestConstants.EXTERNAL)){
				unsharedWithMessage = ProtectTestConstants.EXTERNAL_USER_O365;
			}
			if(policyBean.getPolicyType().equalsIgnoreCase("1")){
				unsharedWithMessage = unsharedWithMessage + "(Read)";
			} else if(policyBean.getPolicyType().equalsIgnoreCase("2")){
				unsharedWithMessage = unsharedWithMessage + "(Contribute)";
			}
			remediationMessage = remediationMessage + " with '" + unsharedWithMessage + "'."; 
			Map<String, String> remediationLogs = this.getInformationalDisplayLogs(restClient, policyBean.getCloudService(),
					policyBean.getFileName(), requestHeader, suiteData, remediationMessage);
			
			if (remediationLogs.get(ProtectTestConstants.MESSAGE) == null) {
				Logger.info("Remediation Logs is not found in the portal, hence test case failed");
				checkSplunkLogs(suiteData, fileName, false);
				Assert.assertTrue(false);
			}
			
			Reporter.log("Remove Link remediation parameters: "+remediationLogs, true);
			Assert.assertEquals(remediationLogs.get(ProtectTestConstants.MESSAGE), remediationMessage);
			Assert.assertEquals(remediationLogs.get(ProtectTestConstants.FACILITY), policyBean.getCloudService());
			Assert.assertEquals(remediationLogs.get(ProtectTestConstants.SEVERITY), ProtectTestConstants.INFORMATIONAL);
			Assert.assertEquals(remediationLogs.get(ProtectTestConstants.SOURCE), ProtectTestConstants.API);
			Assert.assertEquals(remediationLogs.get(ProtectTestConstants.ACTIVITY_TYPE), ProtectTestConstants.UNSHARE);
			Assert.assertEquals(remediationLogs.get(ProtectTestConstants.UNSHARED_WITH), unsharedWithMessage);
		}
		if(remediation.equalsIgnoreCase("removeCollab")){
			String remediationMessage = "User unshared '"+fileName+"'";
			
			if (!policyBean.getRemediationActivity().contains(",")) {
				String removeCollabMessage = "";
				if(policyBean.getExposureType().equals(ProtectTestConstants.EXTERNAL)){
					removeCollabMessage = ProtectTestConstants.EXTERNAL_USER_O365;
				}
				if(policyBean.getPolicyType().equalsIgnoreCase("1")){
					removeCollabMessage = removeCollabMessage + "(Read)";
				} else if(policyBean.getPolicyType().equalsIgnoreCase("2")){
					removeCollabMessage = removeCollabMessage + "(Contribute)";
				}
				remediationMessage = remediationMessage + " with '" + removeCollabMessage + "'."; 
			}
			Map<String, String> remediationLogs = this.getInformationalDisplayLogs(restClient, policyBean.getCloudService(),
					policyBean.getFileName(), requestHeader, suiteData, remediationMessage);
			if (remediationLogs.get(ProtectTestConstants.MESSAGE) == null) {
				Logger.info("Remediation Logs is not found in the portal, hence test case failed");
				checkSplunkLogs(suiteData, fileName, false);
				Assert.assertTrue(false);
			}
			Reporter.log("Remove Collaborator remediation parameters: "+remediationLogs, true);
			Assert.assertTrue(remediationLogs.get(ProtectTestConstants.MESSAGE).contains(remediationMessage));
			Assert.assertEquals(remediationLogs.get(ProtectTestConstants.FACILITY), policyBean.getCloudService());
			Assert.assertEquals(remediationLogs.get(ProtectTestConstants.SEVERITY), ProtectTestConstants.INFORMATIONAL);
			Assert.assertEquals(remediationLogs.get(ProtectTestConstants.SOURCE), ProtectTestConstants.API);
			Assert.assertEquals(remediationLogs.get(ProtectTestConstants.ACTIVITY_TYPE), ProtectTestConstants.UNSHARE);
			//Assert.assertEquals(remediationLogs.get(ProtectTestConstants.UNSHARED_WITH), removeCollabMessage);
		}
		if(remediation.equalsIgnoreCase("deleteUniquePermissions")){
			String remediationMessage = "User unshared '"+fileName+"' with '";
			String deletePermissions = "";
			if(policyBean.getExposureType().equals(ProtectTestConstants.INTERNAL)){
				deletePermissions = ProtectTestConstants.EVERYONE_EXCEPT_EXTERNAL_USERS;
			}
			if(policyBean.getExposureType().equals(ProtectTestConstants.EVERYONE)){
				deletePermissions = "qa-admin@o365security.net,"+ProtectTestConstants.EVERYONE;
			}
			if(policyBean.getPolicyType().equalsIgnoreCase("1")){
				deletePermissions = deletePermissions + "(Read)";
			} else if(policyBean.getPolicyType().equalsIgnoreCase("2")){
				deletePermissions = deletePermissions + "(Contribute)";
			}
			remediationMessage = remediationMessage + deletePermissions + "'."; 
			Map<String, String> remediationLogs = this.getInformationalDisplayLogs(restClient, policyBean.getCloudService(),
					policyBean.getFileName(), requestHeader, suiteData, remediationMessage);
			if (remediationLogs.get(ProtectTestConstants.MESSAGE) == null) {
				Logger.info("Remediation Logs is not found in the portal, hence test case failed");
				checkSplunkLogs(suiteData, fileName, false);
				Assert.assertTrue(false);
			}
			Reporter.log("Delete Unique Permissions remediation parameters: "+remediationLogs, true);
			Assert.assertEquals(remediationLogs.get(ProtectTestConstants.MESSAGE), remediationMessage);
			Assert.assertEquals(remediationLogs.get(ProtectTestConstants.FACILITY), policyBean.getCloudService());
			Assert.assertEquals(remediationLogs.get(ProtectTestConstants.SEVERITY), ProtectTestConstants.INFORMATIONAL);
			Assert.assertEquals(remediationLogs.get(ProtectTestConstants.SOURCE), ProtectTestConstants.API);
			Assert.assertEquals(remediationLogs.get(ProtectTestConstants.ACTIVITY_TYPE), ProtectTestConstants.UNSHARE);
			Assert.assertEquals(remediationLogs.get(ProtectTestConstants.UNSHARED_WITH), deletePermissions);
		}
		if(remediation.equalsIgnoreCase("updateCollabRead") || remediation.equalsIgnoreCase("updateCollabContribute")){
			String remediationMessage = "User shared '"+fileName+"'";
			String updateCollabMessage = "";
			
				if (!policyBean.getRemediationActivity().contains(",") || !policyBean.getRemediationActivity().contains("everyoneRead")) {
					if (policyBean.getExposureType().equals(ProtectTestConstants.EXTERNAL)) {
						updateCollabMessage = ProtectTestConstants.EXTERNAL_USER_O365;
					}
					if (remediation.equalsIgnoreCase("updateCollabRead")) {
						updateCollabMessage = updateCollabMessage + "(Read)";
					} else if (remediation.equalsIgnoreCase("updateCollabContribute")) {
						updateCollabMessage = updateCollabMessage + "(Contribute)";
					} else if (remediation.equalsIgnoreCase("updateCollabDesign")) {
						updateCollabMessage = updateCollabMessage + "(Design)";
					} else if (remediation.equalsIgnoreCase("updateCollabEdit")) {
						updateCollabMessage = updateCollabMessage + "(Edit)";
					} else if (remediation.equalsIgnoreCase("updateCollabFullControl")) {
						updateCollabMessage = updateCollabMessage + "(Full Control)";
					}

					remediationMessage = remediationMessage + " with '" + updateCollabMessage + "'.";
				} else {
					remediationMessage = remediationMessage + " with 'Everyone(Read)";
				}
			Map<String, String> remediationLogs = this.getInformationalDisplayLogs(restClient, policyBean.getCloudService(),
					policyBean.getFileName(), requestHeader, suiteData, remediationMessage);
			if (remediationLogs.get(ProtectTestConstants.MESSAGE) == null) {
				Logger.info("Remediation Logs is not found in the portal, hence test case failed");
				checkSplunkLogs(suiteData, fileName, false);
				Assert.assertTrue(false);
			}
			Reporter.log("Update Collaborator remediation parameters: "+remediationLogs, true);
			Assert.assertTrue(remediationLogs.get(ProtectTestConstants.MESSAGE).contains(remediationMessage));
			Assert.assertEquals(remediationLogs.get(ProtectTestConstants.FACILITY), policyBean.getCloudService());
			Assert.assertEquals(remediationLogs.get(ProtectTestConstants.SEVERITY), ProtectTestConstants.INFORMATIONAL);
			Assert.assertEquals(remediationLogs.get(ProtectTestConstants.SOURCE), ProtectTestConstants.API);
			Assert.assertEquals(remediationLogs.get(ProtectTestConstants.ACTIVITY_TYPE), ProtectTestConstants.SHARE);
			//Assert.assertEquals(remediationLogs.get(ProtectTestConstants.SHARED_WITH), updateCollabMessage);
		}
		if(remediation.equalsIgnoreCase("everyoneRead") || remediation.equalsIgnoreCase("readExceptExternal") ||
				remediation.equalsIgnoreCase("everyoneContribute") || remediation.equalsIgnoreCase("contributeExceptExternal") ||
				remediation.equalsIgnoreCase("everyoneDesign") || remediation.equalsIgnoreCase("designExceptExternal") ||
				remediation.equalsIgnoreCase("everyoneEdit") || remediation.equalsIgnoreCase("editExceptExternal") ||
				remediation.equalsIgnoreCase("everyoneFullControl") || remediation.equalsIgnoreCase("fullcontrolExceptExternal")){
			
			String remediationMessage = "User shared '"+fileName+"'";
			String unsharedWithMessage = "";
			if(policyBean.getRemediationActivity().contains(ProtectTestConstants.EVERYONE.toLowerCase())){
				unsharedWithMessage = ProtectTestConstants.EVERYONE;
			}else{
				unsharedWithMessage = ProtectTestConstants.EVERYONE_EXCEPT_EXTERNAL_USERS;
			}
			if(remediation.toLowerCase().contains("read")){
				unsharedWithMessage = unsharedWithMessage + "(Read)";
			} else if(remediation.toLowerCase().contains("contribute")){
				unsharedWithMessage = unsharedWithMessage + "(Contribute)";
			} else if(remediation.toLowerCase().contains("design")){
				unsharedWithMessage = unsharedWithMessage + "(Design)";
			} else if(remediation.toLowerCase().contains("fullcontrol")){
				unsharedWithMessage = unsharedWithMessage + "(Full Control)";
			} else if(remediation.toLowerCase().contains("edit")){
				unsharedWithMessage = unsharedWithMessage + "(Edit)";
			}
			remediationMessage = remediationMessage + " with '" + unsharedWithMessage;
			Reporter.log("Expected Remediation log: "+remediationMessage, true);
			Map<String, String> remediationLogs = this.getInformationalDisplayLogs(restClient, policyBean.getCloudService(),
					fileName, requestHeader, suiteData, remediationMessage);
			if (remediationLogs.get(ProtectTestConstants.MESSAGE) == null) {
				Logger.info("Remediation Logs is not found in the portal, hence test case failed");
				checkSplunkLogs(suiteData, fileName, false);
				Assert.assertTrue(false);
			}
			Reporter.log(policyBean.getPolicyName() + " - " + remediationLogs, true);
			Assert.assertEquals(remediationLogs.get(ProtectTestConstants.SEVERITY), ProtectTestConstants.INFORMATIONAL);
			Assert.assertEquals(remediationLogs.get(ProtectTestConstants.OBJECT_TYPE), ProtectTestConstants.FILE);
			Assert.assertEquals(remediationLogs.get(ProtectTestConstants.ACTIVITY_TYPE), ProtectTestConstants.SHARE);
			Assert.assertEquals(remediationLogs.get(ProtectTestConstants.SOURCE), ProtectTestConstants.API);
			Assert.assertTrue(remediationLogs.get(ProtectTestConstants.MESSAGE).contains(remediationMessage));
			Assert.assertEquals(remediationLogs.get(ProtectTestConstants.FACILITY), policyBean.getCloudService());
			Assert.assertTrue(remediationLogs.get(ProtectTestConstants.SHARED_WITH).contains(unsharedWithMessage));
			Assert.assertEquals(remediationLogs.get(ProtectTestConstants.USER), suiteData.getSaasAppUsername());
		}
		}
		Reporter.log("Completed remediation logs verification for "+policyBean.getPolicyName(), true);
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
	public HttpResponse deActivatePolicyByName(Client restClient, String policyName, List<NameValuePair> requestHeader, TestSuiteDTO suiteData) throws Exception{
		Map<String, String> policyDetails = this.getPolicyDetailsByName(restClient, policyName, requestHeader, suiteData);
		String policySubId = policyDetails.get(ProtectTestConstants.POLICY_ID);
		String policyType = policyDetails.get(ProtectTestConstants.POLICY_TYPE);
		String restAPI = replaceGenericParams(suiteData.getAPIMap().get("activatePolicy"), suiteData);
		StringEntity entity = new StringEntity(
				"{\"policy_type\":\""+policyType+"\",\"status\":false,\"sub_id\":\""+policySubId+"\"}");	
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(),restAPI);		
		return restClient.doPost(dataUri, requestHeader, null, entity);	
	}
	
	/**
	 *  Validate CI Vulnerabilities Parameter
	 *  
	 * @param riskType
	 * @param expected
	 * @throws IOException 
	 * @throws JsonProcessingException 
	 */
	public void validateCIJson(String riskType, String sourceJson, String expectedJson) throws JsonProcessingException, IOException {
		String contentJson = ClientUtil.getJSONValue(sourceJson, "content_checks");
		String piiJson = ClientUtil.getJSONValue(contentJson, "pii");
		String expressionsJson = ClientUtil.getJSONValue(piiJson, "expressions");

		Logger.info(riskType +"....json...."+piiJson);

		JSONArray jArray = (JSONArray) new JSONTokener(expressionsJson).nextValue();
		for (int i = 0; i < jArray.size(); i++) {
			String valuesJson = jArray.getJSONObject(i).get("values").toString();
			String name = jArray.getJSONObject(i).get("name").toString();

		//	validationMessage += verifyNameExistsJson(expectedJson,name,"PII");

			JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
			for (int j = 0; j < valuesArray.size(); j++) {
				String key = valuesArray.getJSONObject(j).get("key").toString();
				String value = valuesArray.getJSONObject(j).get("value").toString();

		//		validationMessage += verifyKeyValueExistsJson(expectedJson, name, key, value,"PII");
			}
		}
}
	
	
	public String getExpectedRisk(String riskType, String fileName) {
		Properties prop = new Properties();
		InputStream input = null;
		String risks = null;
		try {

			String file = "protect/risks/"+riskType+".properties";
			input = getClass().getClassLoader().getResourceAsStream(file);
			if (input == null) {
				Logger.info("Sorry, unable to find " + file);
			}

			prop.load(input);

			Enumeration<?> e = prop.propertyNames();
			while (e.hasMoreElements()) {
				String key = (String) e.nextElement();

				if (key.contains(fileName)) {
					String value = prop.getProperty(key);
					risks = value;
					break;
				}
				
			}

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		System.out.println("Risks printing....."+risks);
		return risks;
	}

	/**
	 * @param restClient
	 * @param policyName
	 * @param requestHeaders
	 * @param suiteData
	 * @return
	 * @throws Exception
	 */
	public HttpResponse createGenericFileTransferPolicyGatelet(Client restClient, String policyName, List<NameValuePair> requestHeaders, TestSuiteDTO suiteData) throws Exception {
		String payload = FileHandlingUtils.readDataFromFile(ProtectTestConstants.GENERIC_FILE_TRANSFER_POLICY);
		payload = payload.replace("{POLICY_NAME}", policyName);
		URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(),replaceGenericParams(suiteData.getAPIMap().get("createPolicy"), suiteData));
		return restClient.doPost(uri, requestHeaders, null, new StringEntity(payload));
	}

	/**
	 * @param restClient
	 * @param policyName
	 * @param requestHeaders
	 * @param suiteData
	 * @return
	 * @throws Exception
	 */
	public HttpResponse createGenericAccessEnforcementGatelet(Client restClient, String policyName, List<NameValuePair> requestHeaders, TestSuiteDTO suiteData) throws Exception {
		String payload = FileHandlingUtils.readDataFromFile(ProtectTestConstants.GENERIC_ACCESS_ENFORCEMENT_POLICY);
		payload = payload.replace("{POLICY_NAME}", policyName);
		URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(),replaceGenericParams(suiteData.getAPIMap().get("createPolicy"), suiteData));
		return restClient.doPost(uri, requestHeaders, null, new StringEntity(payload));
	}
	
	/**
	 * @param restClient
	 * @param policyName
	 * @param requestHeaders
	 * @param suiteData
	 * @return
	 * @throws Exception
	 */
	public HttpResponse createGenericFileSharingPolicyGatelet(Client restClient, String policyName, List<NameValuePair> requestHeaders, TestSuiteDTO suiteData) throws Exception{
		String payload = FileHandlingUtils.readDataFromFile(ProtectTestConstants.GENERIC_FILE_SHARING_POLICY);
		payload = payload.replace("{POLICY_NAME}", policyName);
		URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(),replaceGenericParams(suiteData.getAPIMap().get("createPolicy"), suiteData));
		return restClient.doPost(uri, requestHeaders, null, new StringEntity(payload));
	}
	
	/**
	 * createPolicyTemplate
	 * @param restClient
	 * @param requestHeader
	 * @param suiteData
	 * @param tempName
	 * @return
	 * @throws Exception
	 */
	public HttpResponse createPolicyTemplate(Client restClient, List<NameValuePair> requestHeader, TestSuiteDTO suiteData, String tempName, String policyType) throws Exception{
		String payload = FileHandlingUtils.readDataFromFile(ProtectTestConstants.GENERIC_CREATE_POLICY_TEMPLATE);
		payload = payload.replace("PolicyTemplateName", tempName);
		payload = payload.replace("PolicyType", policyType);
		URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(),replaceGenericParams(suiteData.getAPIMap().get("createRespTemplate"), suiteData));
		return restClient.doPost(uri, requestHeader, null, new StringEntity(payload));
	}
	
	public void deleteAllPolicyTemplate(Client restClient, List<NameValuePair> requestHeader, TestSuiteDTO suiteData) throws Exception{
		String payloadList = "{\"url\":\"responsetemplates\",\"id\":null,\"action\":\"list\",\"params\":{\"limit\":0}}";
		URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(),replaceGenericParams(suiteData.getAPIMap().get("listRespTemplate"), suiteData));
		HttpResponse response = restClient.doPost(uri, requestHeader, null, new StringEntity(payloadList));
		String responseBody = ClientUtil.getResponseBody(response);
		String templates = ClientUtil.getJSONValue(ClientUtil.getJSONValue(responseBody, "data"), "objects");
		JSONArray jArray = (JSONArray) new JSONTokener(templates).nextValue();
		for (int i = 0; i < jArray.size(); i++) {
			String name = ClientUtil.getJSONValue(jArray.getJSONObject(i).toString(), "template_name");
			name = name.substring(1, name.length()-1);
			String id = ClientUtil.getJSONValue(jArray.getJSONObject(i).toString(), "id");
			id = id.substring(1, id.length()-1);
			deletePolicyTemplate(restClient, requestHeader, suiteData, name, id);
		}
	}
	
	public String getResponseTemplateParameter(Client restClient, List<NameValuePair> requestHeader, TestSuiteDTO suiteData, String templateName, String key) throws Exception{
		String templateId = null;
		//listRespTemplate
		String payload = "{\"url\":\"responsetemplates\",\"id\":null,\"action\":\"list\",\"params\":{\"limit\":0}}";
		URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(),replaceGenericParams(suiteData.getAPIMap().get("listRespTemplate"), suiteData));
		HttpResponse response = restClient.doPost(uri, requestHeader, null, new StringEntity(payload));
		String responseBody = ClientUtil.getResponseBody(response);
		String templates = ClientUtil.getJSONValue(ClientUtil.getJSONValue(responseBody, "data"), "objects");
		JSONArray jArray = (JSONArray) new JSONTokener(templates).nextValue();
		for (int i = 0; i < jArray.size(); i++) {
			String name = ClientUtil.getJSONValue(jArray.getJSONObject(i).toString(), "template_name");
			name = name.substring(1, name.length()-1);
			if(name.equals(templateName)){
				templateId = ClientUtil.getJSONValue(jArray.getJSONObject(i).toString(), key);
				templateId = templateId.substring(1, templateId.length()-1);
				break;
			}
		}
		return templateId;
	}
	
	public HttpResponse editPolicyTemplate(Client restClient, List<NameValuePair> requestHeader, TestSuiteDTO suiteData, String tempName, String tempId, String policyType) throws Exception{
		String payload = FileHandlingUtils.readDataFromFile(ProtectTestConstants.GENERIC_EDIT_POLICY_TEMPLATE);
		payload = payload.replace("PolicyTemplateName", tempName);
		payload = payload.replace("TemplateID", tempId);
		payload = payload.replace("PolicyType", policyType);
		URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(),replaceGenericParams(suiteData.getAPIMap().get("createRespTemplate"), suiteData));
		return restClient.doPost(uri, requestHeader, null, new StringEntity(payload));
	}
	
	public HttpResponse deletePolicyTemplate(Client restClient, List<NameValuePair> requestHeader, TestSuiteDTO suiteData, String tempName, String tempId) throws Exception{
		String payload = "{\"url\":\"responsetemplates\",\"id\":\""+tempId+"\",\"action\":\"delete\"}";
		URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(),replaceGenericParams(suiteData.getAPIMap().get("listRespTemplate"), suiteData));
		return restClient.doPost(uri, requestHeader, null, new StringEntity(payload));
	}
	
	public void verifyResponseTemplateCreated(String response) throws JsonProcessingException, IOException{		
		String status = ClientUtil.getJSONValue(response, "action_status");
		Assert.assertTrue(Boolean.valueOf(status));
	}
	
	public HttpResponse editExistingPolicy(Client restClient, List<NameValuePair> requestHeaders, TestSuiteDTO suiteData,String payload) throws Exception {
		URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(),replaceGenericParams(suiteData.getAPIMap().get("editPolicy"), suiteData));
		Logger.info("Edit policy Post Body:"+payload);
		return restClient.doPost(uri, requestHeaders, null, new StringEntity(payload));
	}
	
	public String createSampleFileType(String filePath, String fileName, 
			String fileExtension) {
		String fileNameActual ="";
		String uniqueId = UUID.randomUUID().toString();
		try {
			File dir = new File(ProtectTestConstants.PROTECT_FILE_TEMP_PATH);
			if (!dir.exists()) {
				if (!dir.mkdir()) {
					Logger.info("Failed to create temp directory!");
				}
			}

			File src = new File(filePath+ File.separator + fileName);
			fileNameActual = FilenameUtils.getBaseName(filePath+ File.separator + fileName);

			File dest = new File(
					ProtectTestConstants.PROTECT_FILE_TEMP_PATH + File.separator + uniqueId + "_" + fileNameActual+"."+fileExtension);

			if (!dest.exists()) {
				dest.createNewFile();
			}
			FileUtils.copyFile(src, dest);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return uniqueId + "_" + fileNameActual+"."+fileExtension;
	}
	
	/**
	 * @param restClient
	 * @param requestHeader
	 * @param suiteData
	 * @throws Exception
	 */
	public void deleteAllPolicies(Client restClient, List<NameValuePair> requestHeader, TestSuiteDTO suiteData) throws Exception{
		String restAPI = replaceGenericParams(suiteData.getAPIMap().get("policyList"), suiteData);
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(),restAPI);
		HttpResponse policyListResponse = restClient.doGet(dataUri, requestHeader);
		String policyListResponseBody = ClientUtil.getResponseBody(policyListResponse);
		String policiesList = ClientUtil.getJSONValue(policyListResponseBody, "policies"); 
		JSONArray jArray = (JSONArray) new JSONTokener(policiesList).nextValue();
		for (int i = 0; i < jArray.size(); i++) {
			String name = ClientUtil.getJSONValue(jArray.getJSONObject(i).toString(), "policy_name");
			name = name.substring(1, name.length()-1);
			deletePolicy(restClient, name, requestHeader, suiteData);
		}		
	}
	
	/**
	 * shareTheFolderPubliclyOrWithCollaborators
	 * @param universalApi
	 * @param folderId
	 * @param sourceFile
	 * @param destinationFile
	 * @param access
	 * @param collaborators
	 * @return
	 * @throws Exception
	 */
	public BoxFolder shareTheFolderPubliclyOrWithCollaborators(UniversalApi universalApi,String folderId, String sourceFile, String destinationFile, String access, String[] collaborators) throws Exception {
		String uniqueId = UUID.randomUUID().toString();
		FileUploadResponse uploadResponse = universalApi.uploadFile(folderId, sourceFile, destinationFile);
		Reporter.log(uploadResponse.getFileName() + " " + uploadResponse.getResponseMessage());
		for (String collaborator : collaborators) {
			Reporter.log("Creating the collaborators : "+collaborator, true);
			CollaborationInput collabInput = new CollaborationInput();
			Item item = new Item();
			item.setId(folderId);
			item.setType("folder");
			AccessibleBy aby = new AccessibleBy();
			aby.setName(uniqueId);
			aby.setType("user");
			aby.setLogin(collaborator);
			collabInput.setItem(item);
			collabInput.setAccessibleBy(aby);
			collabInput.setRole("editor");
			universalApi.createCollaboration(collabInput);
		}
		SharedLink sharedLink = new SharedLink();
		sharedLink.setAccess(access);

		BoxFolder sharedFolder = universalApi.createSharedLinkForFolder(folderId, sharedLink); 
		Reporter.log("Shared file access:" + sharedFolder.getSharedLink().getAccess(), true);
		Reporter.log("Shared file effective access:" + sharedFolder.getSharedLink().getEffectiveAccess(), true);

		return sharedFolder;
	}
	
	/**
	 * shareTheFolderPubliclyOrWithCollaborators with role
	 * @param universalApi
	 * @param folderId
	 * @param sourceFile
	 * @param destinationFile
	 * @param access
	 * @param collaborators
	 * @param role
	 * @return
	 * @throws Exception
	 */
	public BoxFolder shareTheFolderPubliclyOrWithCollaborators(UniversalApi universalApi,String folderId, String sourceFile, String destinationFile, String access, String[] collaborators, String role) throws Exception {
		String uniqueId = UUID.randomUUID().toString();
		
		for (String collaborator : collaborators) {
			Reporter.log("Creating the collaborators : "+collaborator, true);
			CollaborationInput collabInput = new CollaborationInput();
			Item item = new Item();
			item.setId(folderId);
			item.setType("folder");
			AccessibleBy aby = new AccessibleBy();
			aby.setName(uniqueId);
			aby.setType("user");
			aby.setLogin(collaborator);
			collabInput.setItem(item);
			collabInput.setAccessibleBy(aby);
			collabInput.setRole(role);
			BoxCollaboration collaboration = universalApi.createCollaboration(collabInput);
			
			Logger.info("Collaboration Status:"+collaboration.getStatus());
			Logger.info("File Name:"+destinationFile);
			
			while (collaboration.getStatus().equals(String.valueOf(HttpStatus.SC_INTERNAL_SERVER_ERROR))) {
				Logger.info("Resharing because of 500 Internal Server Error occured on sharing in Box SaaS app.");
				collaboration = universalApi.createCollaboration(collabInput);
			
				if (collaboration.getStatus().equals(String.valueOf(HttpStatus.SC_BAD_REQUEST))) {
						Logger.info("400 Bad Request occured as the user is a collaborator already");
						universalApi.deleteCollaboration(collaboration);
						waitForMinutes(2);
						collaboration = universalApi.createCollaboration(collabInput);
				}
				Logger.info("Box Collaboration completed for file: "+destinationFile);
			}
		}
		
		SharedLink sharedLink = new SharedLink();
		sharedLink.setAccess(access);
		BoxFolder sharedFolder = universalApi.createSharedLinkForFolder(folderId, sharedLink); 
		Reporter.log("Shared file access:" + sharedFolder.getSharedLink().getAccess(), true);
		Reporter.log("Shared file effective access:" + sharedFolder.getSharedLink().getEffectiveAccess(), true);

		return sharedFolder;
	}
	
	/**
	 * 
	 * @param universalApi
	 * @param folderId
	 * @param sourceFile
	 * @param destinationFile
	 * @param access
	 * @param collaborators
	 * @param role
	 * @return
	 * @throws Exception
	 */
	public FileEntry shareTheFilePublicly(UniversalApi universalApi,String fileId, String sourceFile, String destinationFile, String access) throws Exception {
		SharedLink sharedLink = new SharedLink();
		sharedLink.setAccess(access);
		FileEntry fileEntry = universalApi.createSharedLink(fileId, sharedLink); 
		Reporter.log("Shared file access:" + fileEntry.getSharedLink().getAccess(), true);
		Reporter.log("Shared file effective access:" + fileEntry.getSharedLink().getEffectiveAccess(), true);
		return fileEntry;
	}
	
	/**
	 * 
	 * @param restClient
	 * @param appName
	 * @param requestHeader
	 * @param suiteData
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> getAllActivityCount(Client restClient, String appName, List<NameValuePair> requestHeader, TestSuiteDTO suiteData) throws Exception{
		Map<String, String> activityCounts = new HashMap<String, String>();
		HttpResponse response = null;
		ProtectQueryBuilder protectQueryBuilder = new ProtectQueryBuilder();
		ElasticSearchLogs esLogs = new ElasticSearchLogs();
		String csrfToken = requestHeader.get(0).getValue(); 
		String sessionID = requestHeader.get(4).getValue(); 
		String tsfrom = getDateFromCurrent(-1)+"T12:35:27";
		String tsto = getDateFromCurrent(2)+"T12:35:27";
		String email = suiteData.getUsername();
		String apiServerUrl = suiteData.getApiserverHostName();
		String payload = protectQueryBuilder.getESQueryForActivityCount(tsfrom, tsto, appName, apiServerUrl, csrfToken, sessionID, email);
		StringEntity entity = new StringEntity(payload);
		response = esLogs.getDisplayLogs(restClient, requestHeader, suiteData.getApiserverHostName(), entity);
		String responseBody = ClientUtil.getResponseBody(response);
		activityCounts = getCount(responseBody);
		return activityCounts;
	}
	
	/**
	 * 
	 * @param jsonResponse
	 * @return
	 */
	public static Map<String, String> getCount(String jsonResponse) {
       List<Map<String, Object>> fetchAllKeys = RawJsonParser.fetchAllKeys(jsonResponse, "$.facets.Activity_type.terms[*]");
       Map<String, String> result = new HashMap<String, String>();
       
       for (Map<String, Object> fetchAllKey : fetchAllKeys) {
           result.put(fetchAllKey.get("term").toString(), fetchAllKey.get("count").toString());
       }
       return result;
   }
	
	public String createContentIQProfile(Client restClient, List<NameValuePair> requestHeader, TestSuiteDTO suiteData, String ciqName, String source, List<String> risks) throws Exception{
		ProtectQueryBuilder protectQueryBuilder = new ProtectQueryBuilder();
		String payload = protectQueryBuilder.getESQueryForContentIQ(ciqName, source, risks);
		Reporter.log("**************************************", true);
		Reporter.log("Content IQ Profile Payload: "+payload, true);
		Reporter.log("**************************************", true);
		String restAPI = replaceGenericParams(suiteData.getAPIMap().get("createContentIQ"), suiteData);
		StringEntity entity = new StringEntity(payload);
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(),restAPI);		
		HttpResponse response = restClient.doPost(dataUri, requestHeader, null, entity);
		String responseBody = ClientUtil.getResponseBody(response);
		Reporter.log("**************************************", true);
		Reporter.log("Content IQ Profile Response Body: "+responseBody, true);
		Reporter.log("**************************************", true);
		return responseBody;
	}
	//listContentIQ
	public void deleteCIQProfiles(Client restClient, List<NameValuePair> requestHeader, TestSuiteDTO suiteData, String ciqName) throws Exception{
		ProtectQueryBuilder protectQueryBuilder = new ProtectQueryBuilder();
		String ciqid = null;
		String ciqListResponseBody = this.listCIQProfiles(restClient, requestHeader, suiteData);
		Map<String, String>ciqDetails = this.getCIQPolicyDetails(ciqListResponseBody, ciqName);
		ciqid = ciqDetails.get(ProtectTestConstants.PROFILE_ID);
		String restAPI = replaceGenericParams(suiteData.getAPIMap().get("listContentIQ"), suiteData);
		String payload = protectQueryBuilder.generatePayloadForDeleteCIQProfile(ciqid);
		StringEntity entity = new StringEntity(payload);	
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(),restAPI);		
		HttpResponse response = restClient.doPost(dataUri, requestHeader, null, entity);
		String responseBody = ClientUtil.getResponseBody(response);
		String statusCode = ClientUtil.getJSONValue(responseBody, "status");
		int deleteStatusCode = Integer.parseInt(statusCode);
		Assert.assertEquals(deleteStatusCode, 204);
	}
	
	public String listCIQProfiles(Client restClient, List<NameValuePair> requestHeader, TestSuiteDTO suiteData) throws Exception{
		ProtectQueryBuilder protectQueryBuilder = new ProtectQueryBuilder();
		String restAPI = replaceGenericParams(suiteData.getAPIMap().get("listContentIQ"), suiteData);
		String payload = protectQueryBuilder.generatePayloadForListCIQProfile();
		StringEntity entity = new StringEntity(payload);	
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(),restAPI);		
		HttpResponse response = restClient.doPost(dataUri, requestHeader, null, entity);
		String responseBody = ClientUtil.getResponseBody(response);
		return responseBody;
	}
	
	public Map<String, String> getCIQPolicyDetails(String ciqResponseBody, String ciqName) throws JsonProcessingException, IOException{
		Map<String, String> ciqDetails = new HashMap<String, String>();
		boolean flag = false;
		String data = ClientUtil.getJSONValue(ciqResponseBody, "data");
		String objects = ClientUtil.getJSONValue(data, "objects");
		JSONArray jArray = (JSONArray) new JSONTokener(objects).nextValue();
		String profileName = null;
		for (int i = 0; i < jArray.size(); i++) {
			profileName = ClientUtil.getJSONValue(jArray.getJSONObject(i).toString(), "profile_name");
			profileName = profileName.substring(1, profileName.length()-1);
			if(profileName.equals(ciqName)){
				String profileId = ClientUtil.getJSONValue(jArray.getJSONObject(i).toString(), ProtectTestConstants.PROFILE_ID);
				profileId = profileId.substring(1, profileId.length()-1);
				ciqDetails.put(ProtectTestConstants.PROFILE_ID, profileId);
				flag = true;
				break;
			}
		}
		return ciqDetails;
	}
	
	public HttpResponse createContentIQProfileByRiskType(Client restClient, String riskType, List<NameValuePair> requestHeader,
			TestSuiteDTO suiteData) throws Exception {

		String restAPI = replaceGenericParams(suiteData.getAPIMap().get("createContentIQ"), suiteData);
		String riskTypePayLoadValue = riskType;

		if (!(riskTypePayLoadValue.equalsIgnoreCase("no") || riskTypePayLoadValue.length() == 0 || riskTypePayLoadValue == null)) {

			if (riskType.equalsIgnoreCase("VBA Macros"))
				riskTypePayLoadValue = "vba_macros";

			if (riskTypePayLoadValue.contains(","))
				return null;
			else {

				StringEntity entity = new StringEntity(
						"{\"threshold\":0,\"description\":\""
								+ riskType
								+ "\",\"name\":\""
								+ riskType
								+ "\",\"groups\":[{\"operator\":\"AND\",\"operands\":[{\"name\":\"\",\"weight\":1,\"is_not\":false,\"value\":[\""
								+ riskTypePayLoadValue.toLowerCase()
								+ "\"],\"source\":\"DCI_RISK\",\"min_count\":1}]}],\"appliesToSecurlets\":1,\"contentTypeSelected\":[],\"api_enabled\":true,\"domains\":[\"__ALL_EL__\"]}");
				URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(), restAPI);
				return restClient.doPost(dataUri, requestHeader, null, entity);
			}
		}
		return null;
	}
	
	public HttpResponse createContentIQProfileByContentType(Client restClient, String contentType, List<NameValuePair> requestHeader,
			TestSuiteDTO suiteData) throws Exception {
		String restAPI = replaceGenericParams(suiteData.getAPIMap().get("createContentIQ"), suiteData);
		String contentTypePayLoadValue = "source_code";

		if (contentType.equalsIgnoreCase("Encryption"))
			contentTypePayLoadValue = "encryption";

		if (!(contentTypePayLoadValue.equalsIgnoreCase("no") || contentTypePayLoadValue.length() == 0 || contentTypePayLoadValue == null)) {

			StringEntity entity = new StringEntity(
					"{\"threshold\":0,\"description\":\""
							+ contentType
							+ "\",\"name\":\""
							+ contentType
							+ "\",\"groups\":[{\"operator\":\"AND\",\"operands\":[{\"name\":\"\",\"weight\":1,\"is_not\":false,\"value\":[\""
							+ contentTypePayLoadValue
							+ "\"],\"source\":\"DCI_CONTENT\",\"min_count\":1}]}],\"appliesToSecurlets\":1,\"contentTypeSelected\":[],\"api_enabled\":true,\"domains\":[\"__ALL_EL__\"]}");
			URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(), restAPI);
			return restClient.doPost(dataUri, requestHeader, null, entity);
		}
		return null;
	}
	
	public HttpResponse createContentIQProfileForFileFormat(Client restClient, List<NameValuePair> requestHeader, TestSuiteDTO suiteData,
			String fileFormat) throws Exception {
		String restAPI = replaceGenericParams(suiteData.getAPIMap().get("createContentIQ"), suiteData);

		if (!(fileFormat.equalsIgnoreCase("no") || fileFormat.length() == 0 || fileFormat == null)) {
			StringEntity entity = new StringEntity(
					"{\"threshold\":0,\"description\":\""
							+ fileFormat
							+ "\",\"name\":\""
							+ fileFormat
							+ "\",\"groups\":[{\"operator\":\"AND\",\"operands\":[{\"name\":\"\",\"weight\":1,\"is_not\":false,\"value\":[\"class:"
							+ fileFormat.toUpperCase()
							+ "\"],\"source\":\"DCI_FILE_FORMAT\",\"min_count\":1}]}],\"appliesToSecurlets\":1,\"api_enabled\":true,\"domains\":[\"__ALL_EL__\"]}");
			URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(), restAPI);
			return restClient.doPost(dataUri, requestHeader, null, entity);
		}
		return null;
	}
	
	public String getCIQId(Client restClient, String ciqName, List<NameValuePair> requestHeader, TestSuiteDTO suiteData) throws Exception{
		String ciqListResponseBody = this.listCIQProfiles(restClient, requestHeader, suiteData);
		Map<String, String>ciqDetails = this.getCIQPolicyDetails(ciqListResponseBody, ciqName);
		return ciqDetails.get(ProtectTestConstants.PROFILE_ID);
	}
	
	public void checkSplunkLogs(TestSuiteDTO suiteData, String fileName, boolean verifyJobId) throws JsonProcessingException, IOException {
		Logger.info("=============================================================================================");
		Logger.info("*****************Splunk Log Validations********************************************");
		Logger.info("=============================================================================================");
		Logger.info("***********Verifying request params are sent from Connector to Policy Evaluator***************");
		Logger.info("=============================================================================================");		
		
		String saasApp = suiteData.getSaasApp();
		if (saasApp.equalsIgnoreCase("GDrive") || saasApp.equalsIgnoreCase("GmailSanity") || saasApp.equalsIgnoreCase("Gmail"))
			saasApp = "google_apps";
		else if (saasApp.contains("Office365") || saasApp.contains("office365"))
			saasApp = "Office365";
		String tenantName = suiteData.getTenantName();
		String envName = suiteData.getEnvironmentName();
		
		SplunkQueryResult result = SplunkQueries.lookForPolicyWrapperLogs(envName, saasApp+"_"+tenantName, fileName, "-2d");
		JSONObject splunkResult = result.getQueryResult();
		Logger.info("====================================================================================================================");
		Logger.info("**************************1.) Connector Logs - To retrieve Doc Id**************************");
		Logger.info("====================================================================================================================");
		Logger.info(splunkResult.toString());
		Logger.info("====================================================================================================================");
		String rawBody = ClientUtil.getJSONValue(splunkResult.toString(), "results");
		if (rawBody.equalsIgnoreCase("[]")) {
			Logger.info("Not able to fetch Connector logs for file name:"+fileName);
			return;
		}
		String docId = getDocId(rawBody, saasApp);
		Logger.info("=======================DocId/ ResourceId: " + docId + "=============================================================");
		Logger.info("====================================================================================================================");
		Logger.info("**************************2.) Policy Evaluator Logs based on DocId retrieved from Connector Logs**************************");
		Logger.info("====================================================================================================================");		
		// Based on docId, get the Policy Eval jobId
		Logger.info("****Fetch the jobId based on the docId to pull policy evaluator and policy enforcement logs********************");
		SplunkQueryResult polEvalResult = SplunkQueries.lookForPolicyEvalLogs(envName, docId, "-2d");
		String polEvalQueryResult = polEvalResult.getQueryResult().toString();
		String resultArray = ClientUtil.getJSONValue(polEvalQueryResult.toString(), "results");
		List<String> pEnforceJobIdList = new ArrayList<String>();

		JSONArray jArray = (JSONArray) new JSONTokener(resultArray).nextValue();

		if (jArray.size() == 0)
			Logger.info("***************Policy Evaluator Logs are not found for docId/ resourceId: " + docId
					+ "****************************");
		if (verifyJobId)
			Assert.assertTrue(jArray.size() > 0, "No JobId founs for the given docId: "+docId);
		
		
		for (int i = 0; i < jArray.size(); i++) {
			String source = ClientUtil.getJSONValue(jArray.getJSONObject(i).toString(), "_raw");
			String jobId = source.substring(source.indexOf("job_id=") + 7, source.indexOf(" with body="));
			
			if (verifyJobId)
				Assert.assertTrue(jobId.length() > 0, "No JobId exist");
			
			// Print full job details and check for es_logId
			// Based on docId, get the Policy Evaluator jobId
			SplunkQueryResult polEvalJobResult = SplunkQueries.lookForPEvalLogs(envName, jobId, "-2d");
			String polEvalJobQueryResult = polEvalJobResult.getQueryResult().toString();
			String polEvalJobQueryResultArray = ClientUtil.getJSONValue(polEvalJobQueryResult.toString(), "results");
			JSONArray jpEvalArray = (JSONArray) new JSONTokener(polEvalJobQueryResultArray).nextValue();
			
			Logger.info("=============================================================================================");
			Logger.info("***************************Policy Evalutor Logs for jobId: " + jobId +"**********************");
			Logger.info("=============================================================================================");				
			
			for (int j = 0; j < jpEvalArray.size(); j++) {
				Logger.info(jpEvalArray.getString(j));
				Logger.info("====================================================================================================================");

				if (jpEvalArray.getJSONObject(j).toString().contains("eslog_id")) {
					String esLogString = jpEvalArray.getJSONObject(j).toString()
							.substring(jpEvalArray.getJSONObject(j).toString().indexOf("eslog_id"));

					Logger.info("****Remediation/ Policy Enforcement Job Id from Policy Evaluator: "
							+ esLogString.substring(esLogString.indexOf("eslog_id") + 15, esLogString.indexOf(",") - 2)+"***********");
					
					pEnforceJobIdList.add(esLogString.substring(esLogString.indexOf("eslog_id") + 15, esLogString.indexOf(",") - 2));
				}
			}
			
		}

		
		Logger.info("=============================================================================================");
		Logger.info("***************************Policy Enforcement/ Remediation logs Verification**********************");
		Logger.info("=============================================================================================");				

		if (pEnforceJobIdList.size() == 0){
			Logger.info("************No remediation logs/ esLogIds are found in policy_enforcement.log************");
			if (verifyJobId)
				Assert.assertTrue(pEnforceJobIdList.size() > 0, "No remediation logs/ esLogIds are found in policy_enforcement.log");
		}
		else {
			
			for (String remJobId : pEnforceJobIdList) {
				Logger.info("====================================================================================================================");
				Logger.info("esLogId : "+remJobId);
				Logger.info("====================================================================================================================");				
				SplunkQueryResult polRemediationResult = SplunkQueries.lookForPolicyRemediationLogs(envName, remJobId, "-2d");
				Logger.info("====================================================================================================================");					
				Logger.info("**************************Policy Enforcement Logs based on esLogid: "+remJobId+ " to verify remediation details sent to connector **************************");
				Logger.info("====================================================================================================================");
				Logger.info(polRemediationResult.getQueryResult());
				Logger.info("====================================================================================================================");

			}
		}
	}

	private String getDocId(String rawBody, String saasApp) {
		String docId = null;
		Logger.info("SaaS app...."+saasApp);
		docId = rawBody.substring(rawBody.indexOf("\\\"file_id\\\":") + 15, rawBody.indexOf("\\\"file_size\\\":")-4);
		return docId;
	}
	
	
	public String getPolicyAlerts(Client restClient, List<NameValuePair> requestHeader, TestSuiteDTO suiteData, ProtectAlertFilter protectAlertFilter, int size) throws Exception{
		HttpResponse response = null;
		ElasticSearchLogs esLogs = new ElasticSearchLogs();
		String csfrToken = requestHeader.get(0).getValue();
		String sessionID = requestHeader.get(4).getValue();
		
		
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
	    sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		Logger.info(sdf.format(new Date()));
		
		
		String payLoad = "{\"source\":{\"query\":{\"bool\":{\"must\":[{\"range\":{\"created_timestamp\":{\"from\":\""
				+ getDateFromCurrent(-1)
				+ "T"
				+ sdf.format(new Date())
				+ "\",\"to\":\""
				+ getDateFromCurrent(0)
				+ "T"
				+ sdf.format(new Date())
				+ "\"}}},{\"term\":{\"Activity_type\":\"Policy Violation\"}},{\"term\":{\"policy_action\":\"ALERT\"}},{\"filtered\":{\"filter\":{\"not\":{\"and\":[{\"term\":{\"severity\":\"informational\"}},{\"term\":{\"severity_old\":\"critical\"}}]}}}}";

				if (protectAlertFilter != null && protectAlertFilter.isSearchFilter()) {
					
					if (protectAlertFilter.getUser() != null) {
						payLoad  = payLoad + ",{\"query_string\":{\"query\":\""+protectAlertFilter.getUser()+"\"}}";
					}
					if (protectAlertFilter.getService() != null) {
						payLoad  = payLoad + ",{\"query_string\":{\"query\":\""+protectAlertFilter.getService()+"\"}}";
					}
					if (protectAlertFilter.getPolicy() != null) {
						payLoad  = payLoad + ",{\"query_string\":{\"query\":\""+protectAlertFilter.getPolicy()+"\"}}";
					}
					if (protectAlertFilter.getFileName() != null) {
						payLoad  = payLoad + ",{\"query_string\":{\"query\":\""+protectAlertFilter.getFileName()+"\"}}";
					}

				}
				
				
				payLoad = payLoad + "],\"must_not\":{\"term\":{\"alert_cleared\":\"true\"}}}},"
				+ "\"filter\":{";
				
				if (protectAlertFilter != null && !protectAlertFilter.isSearchFilter()) {
		
					if (protectAlertFilter.getService() != null) {
							payLoad = payLoad + "\"bool\":{\"must\":[{\"bool\":{\"should\":[{\"term\":{\"facility\":\""
									+ protectAlertFilter.getService() + "\"}}]}}";
					}
		
					if (protectAlertFilter.getUser() != null) {
						if (protectAlertFilter.getService() == null)
							payLoad = payLoad + "\"bool\":{\"must\":[{\"bool\":{\"should\":[{\"term\":{\"user\":\""+ protectAlertFilter.getUser() + "\"}}]}}";
						else
							payLoad = payLoad + ",{\"bool\":{\"should\":[{\"term\":{\"user\":\"" + protectAlertFilter.getUser() + "\"}}]}}";
					}
		
					if (protectAlertFilter.getPolicy() != null) {
						if (protectAlertFilter.getService() == null && protectAlertFilter.getUser() == null)
							payLoad = payLoad + "\"bool\":{\"must\":[{\"bool\":{\"should\":[{\"term\":{\"_PolicyViolated\":\""+ protectAlertFilter.getPolicy() + "\"}}]}}";
						else
							payLoad = payLoad + ",{\"bool\":{\"should\":[{\"term\":{\"_PolicyViolated\":\"" + protectAlertFilter.getPolicy() + "\"}}]}}";
					}
		
					if (protectAlertFilter.getPolicyType() != null) {
						if (protectAlertFilter.getService() == null && protectAlertFilter.getUser() == null && protectAlertFilter.getPolicy() == null)
							payLoad = payLoad + "\"bool\":{\"must\":[{\"bool\":{\"should\":[{\"term\":{\"policy_type\":\""+ protectAlertFilter.getPolicyType() + "\"}}]}}";
						else	
							payLoad = payLoad + ",{\"bool\":{\"should\":[{\"term\":{\"policy_type\":\"" + protectAlertFilter.getPolicyType() + "\"}}]}}";
					}
		
					if (protectAlertFilter.getSeverity() != null) {
						if (protectAlertFilter.getService() == null && protectAlertFilter.getUser() == null && protectAlertFilter.getPolicy() == null && protectAlertFilter.getPolicyType() == null)
							payLoad = payLoad + "\"bool\":{\"must\":[{\"bool\":{\"should\":[{\"term\":{\"severity\":\""+ protectAlertFilter.getSeverity() + "\"}}]}}";
						else
							payLoad = payLoad + ",{\"bool\":{\"should\":[{\"term\":{\"severity\":\"" + protectAlertFilter.getSeverity() + "\"}}]}}";
					}
					payLoad = payLoad + "]}},";
				} else {
						payLoad = payLoad + "},";
				}
				payLoad = payLoad + "\"facets\":{\"policy_type\":{\"terms\":{\"field\":\"policy_type\",\"size\":1000}},\"user\":{\"terms\":{\"field\":\"user\",\"size\":1000}},\"_PolicyViolated\":{\"terms\":{\"field\":\"_PolicyViolated\",\"size\":1000}},\"facility\":{\"terms\":{\"field\":\"facility\",\"size\":1000}},\"severity\":{\"terms\":{\"field\":\"severity\",\"size\":1000}}},\"from\":0,\"size\":"+size+",\"sort\":{\"created_timestamp\":{\"order\":\"desc\"}}},\"app\":\"PROTECT\",\"sourceName\":\"PROTECT\",\"";
				payLoad = payLoad + "apiServerUrl\":\"http://" + suiteData.getApiserverHostName() + "/\",\"csrftoken\":\"" + csfrToken
				+ "\",\"sessionid\":\"" + sessionID + "\",\"userid\":\"" + suiteData.getUsername() + "\"}";
		Logger.info("Payload..."+payLoad);
		
		StringEntity entity = new StringEntity(payLoad);
		response = esLogs.getDisplayLogs(restClient, requestHeader, suiteData.getApiserverHostName(), entity);
		
		String responseBody = ClientUtil.getResponseBody(response);
		//Logger.info("Alerts Response Body..."+responseBody);
		return responseBody;

	}
	
	
	public String getPolicyInfoByName(Client restClient, String policyName, List<NameValuePair> requestHeader, TestSuiteDTO suiteData, Map<String, String> additionalParams) throws Exception{
		
		List<NameValuePair> qparams = new ArrayList<NameValuePair>(); 
		for(String key : additionalParams.keySet()) {
			qparams.add(new BasicNameValuePair(key,  additionalParams.get(key)));
		}
		
		String path = suiteData.getAPIMap().get("getPolicy");
		URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(), path, qparams);
		HttpResponse response =  restClient.doGet(uri, requestHeader);
		String responseBody = ClientUtil.getResponseBody(response);
		
		
		return responseBody;
	}
	
	/**
	 *Common method which will be replaced in other login calls for protect test cases
	 * 
	 * @param suiteData
	 * @return
	 * @throws Exception
	 */
	public UniversalApi loginToSaaSApp(TestSuiteDTO suiteData, String saasApp) throws Exception{
		UserAccount userAccount;
		UniversalApi universalApi = null;
		if(suiteData.getSaasApp().equalsIgnoreCase(ProtectTestConstants.DROPBOX)){
		}else if(suiteData.getSaasApp().equalsIgnoreCase(ProtectTestConstants.BOX)){
			userAccount = new UserAccount(suiteData.getSaasAppUsername(), suiteData.getSaasAppPassword(), suiteData.getSaasAppUserRole());
			universalApi = new UniversalApi(ProtectTestConstants.BOX, userAccount);
		}else if(suiteData.getSaasApp().equalsIgnoreCase(ProtectTestConstants.GDRIVE)){
		}else if(suiteData.getSaasApp().equalsIgnoreCase(ProtectTestConstants.OFFICE365)){
		}
		return universalApi;
	}
	
	/*	public HttpResponse clearAlert(Client restClient, List<NameValuePair> requestHeader, String apiServer, StringEntity se) throws Exception {
	
		String requestUri = "/controls/unblock";
	
		URI dataUri = ClientUtil.BuidURI("https", apiServer, requestUri);
		String payLoad = "{\"user\":\"admin@protectbeatle.com\",\"policy\":\"GDUPDATECOLLAB2\",\"blocked_apps\":\"Google Drive\",\"app_action\":\"NOALERT\"}";
	
		return restClient.doPost(dataUri, requestHeader, null, se);
	}*/
	
	public Salesforce salesforceLogin(TestSuiteDTO suiteData) throws Exception{
		UserAccount userAccount;
		UniversalApi universalApi = null;
		HashMap<String, String> instanceParams = new HashMap<String, String>();
		if (suiteData.getSaasAppUsername() != null && suiteData.getSaasAppPassword() !=null && 		
			suiteData.getSaasAppUserRole() !=null && suiteData.getSaasApp() !=null) {
			
			userAccount = new UserAccount(suiteData.getSaasAppUsername(), suiteData.getSaasAppPassword(), 
				suiteData.getSaasAppUserRole(), suiteData.getEnvironmentName(), null, suiteData.getDomainName());
			
		
		    if(suiteData.getSaasAppClientId() !=null) {
		    	instanceParams.put("clientId", suiteData.getSaasAppClientId());
		    }
		    
		    if(suiteData.getSaasAppClientSecret() !=null) {
		    	instanceParams.put("clientSecret", suiteData.getSaasAppClientSecret());
		    }
		    
		    if(suiteData.getSaasAppToken() !=null) {
		    	instanceParams.put("token", suiteData.getSaasAppToken());
		    }
			
			if(suiteData.getSaasAppLoginHost() !=null) {
				instanceParams.put("apiHost", suiteData.getSaasAppLoginHost());
				userAccount = new UserAccount(suiteData.getSaasAppUsername(), suiteData.getSaasAppPassword(), 
						suiteData.getSaasAppUserRole(), instanceParams); 
				universalApi = new UniversalApi(suiteData.getSaasApp(), userAccount);
			} else {
				universalApi = new UniversalApi(suiteData.getSaasApp(), userAccount);
			}
		}
		return universalApi.getSalesforce();
	}
	
	public String createCIQProtectProfiles(Client restClient, List<NameValuePair> requestHeader, TestSuiteDTO suiteData, PolicyBean policyBean) throws Exception{
		String source = null;
		List<String> risks = new ArrayList<String>();
		String ciqRule = policyBean.getRiskType().toLowerCase();
		ProtectQueryBuilder protectQueryBuilder = new ProtectQueryBuilder();
		if(ciqRule.contains("dlp") ||
				ciqRule.contains("ferpa") ||
				ciqRule.contains("glba") ||
				ciqRule.contains("hipaa") ||
				ciqRule.contains("pci") ||
				ciqRule.contains("pii") ||
				ciqRule.contains("vba_macros") ||
				ciqRule.contains("virus")){
				source = "DCI_RISK";
		}
		
		if(ciqRule.contains("business") ||
				ciqRule.contains("computing") ||
				ciqRule.contains("cryptographic_keys") ||
				ciqRule.contains("design doc") ||
				ciqRule.contains("encryption") ||
				ciqRule.contains("engineering") ||
				ciqRule.contains("health") ||
				ciqRule.contains("legal") ||
				ciqRule.contains("source_code")){
				source = "DCI_CONTENT";
		}
		if(policyBean.getRiskType().contains(",")){
			String[] values = policyBean.getRiskType().split(",");
			for(int j=0; j<values.length;j++){
				risks.add(values[j].toLowerCase());
			}
		}else{
			risks.add(policyBean.getRiskType().toLowerCase());
		}
		String responseBody = this.createContentIQProfile(restClient, requestHeader, suiteData, policyBean.getCiqProfile(), source, risks);
		/*String payload = protectQueryBuilder.getESQueryForContentIQ(policyBean.getCiqProfile(), source, risks);
		Reporter.log("**************************************", true);
		Reporter.log("Content IQ Profile Payload: "+payload, true);
		Reporter.log("**************************************", true);
		String restAPI = replaceGenericParams(suiteData.getAPIMap().get("createContentIQ"), suiteData);
		StringEntity entity = new StringEntity(payload);
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(),restAPI);		
		HttpResponse response = restClient.doPost(dataUri, requestHeader, null, entity);
		String responseBody = ClientUtil.getResponseBody(response);
		Reporter.log("**************************************", true);
		Reporter.log("Content IQ Profile Response Body: "+responseBody, true);
		Reporter.log("**************************************", true);*/
		return responseBody;
	}
	
	public Map<String, String> getSalesforceGroupIds(){
		Map<String, String> groupIds = new HashMap<String, String>();
		groupIds.put("External Group", "0F9Q00000000EeAKAU");
		groupIds.put("Internal Group", "0F9Q00000000Ee5KAE");
		groupIds.put("Internal Group 1", "0F9Q00000000EeFKAU");
		groupIds.put("Internal Group 2", "0F9Q00000000EeKKAU");
		return groupIds;
	}
	
	/*public SiteFileResource o365SiteUploadAndSharing(UniversalApi universalApi, PolicyBean policyBean, File file, String siteUrl, HashMap<String, Long> usermap, HashMap<String, Long> rolemap, TestSuiteDTO suiteData) throws Exception{
		SiteFileResource fileResource = universalApi.uploadFileToRootSite(siteUrl, file.getAbsolutePath(), "/", file.getName());
		ListItemAllFields listitemfields = universalApi.getRootSiteListItemAllFieldsByUrl(siteUrl, "Shared Documents", file.getName());
		universalApi.breakRoleInheritanceForRootSiteListItem(siteUrl, listitemfields.getOdataEditLink(), false, false);
		waitForMinutes(0.5);
		String[] sharing = policyBean.getPolicyType().split("-");
		String userSharing = sharing[0];
		String userRole = sharing[1];
		universalApi.addRootSiteRoleAssignmentForListItem(siteUrl, listitemfields.getOdataEditLink(),String.valueOf(usermap.get(userSharing)), String.valueOf(rolemap.get(userRole)));
		return fileResource;
	}*/
	
	public ListItemAllFields o365SiteExposureSharing(UniversalApi universalApi, PolicyBean policyBean, File file, String siteUrl, HashMap<String, Long> usermap, HashMap<String, Long> rolemap) throws Exception{
		ListItemAllFields listitemfields = universalApi.getRootSiteListItemAllFieldsByUrl(siteUrl, "Shared Documents", file.getName());
		universalApi.breakRoleInheritanceForRootSiteListItem(siteUrl, listitemfields.getOdataEditLink(), false, false);
		waitForMinutes(0.5);
		if(policyBean.getPolicyType().contains(",")){
			String[] sharing = policyBean.getPolicyType().split(",");
			for(int i=0; i<sharing.length; i++){
				String[] sharings = sharing[i].split("-");
				String userSharing = sharings[0];
				String userRole = sharings[1];
				universalApi.addRootSiteRoleAssignmentForListItem(siteUrl, listitemfields.getOdataEditLink(),String.valueOf(usermap.get(userSharing)), String.valueOf(rolemap.get(userRole)));
			}
		}else{
			String[] sharing = policyBean.getPolicyType().split("-");
			String userSharing = sharing[0];
			String userRole = sharing[1];
			universalApi.addRootSiteRoleAssignmentForListItem(siteUrl, listitemfields.getOdataEditLink(),String.valueOf(usermap.get(userSharing)), String.valueOf(rolemap.get(userRole)));
		}
		return listitemfields;
	}
	
	public void o365SiteUpdateCollaboratorRemediationVerification(Client restClient, List<NameValuePair> requestHeader, TestSuiteDTO suiteData, PolicyBean policyBean, String fileName) throws Exception{
		String[] sharing = policyBean.getPolicyType().split("-");
		String userSharing = sharing[0];
		String userRole = sharing[1];
		String remediationMessage = null;
		String shareWith = null;
		String email = "mayurbelekar@hotmail.com";
		if(policyBean.getRemediationActivity().equals("updateCollabRead")){
			shareWith = email+"(Read)";
		}
		if(policyBean.getRemediationActivity().equals("updateCollabContribute")){
			shareWith = email+"(Contribute)";
		}
		if(policyBean.getRemediationActivity().equals("updateCollabDesign")){
			shareWith = email+"(Design)";
		}
		if(policyBean.getRemediationActivity().equals("updateCollabEdit")){
			shareWith = email+"(Edit)";
		}
		if(policyBean.getRemediationActivity().equals("updateCollabFullControl")){
			shareWith = email+"(Full Control)";
		}
		remediationMessage = "User shared '"+fileName+"' with '"+shareWith+"'.";
		Map<String, String> remediationLogs = this.getInformationalDisplayLogs(restClient, policyBean.getCloudService(),
				policyBean.getFileName(), requestHeader, suiteData, remediationMessage);
		Reporter.log("=====================================", true);
		Reporter.log("Remediation parameters: "+remediationLogs, true);
		Reporter.log("=====================================", true);
		Reporter.log("**Remediation Message**", true);
		Reporter.log("Expected Message: "+remediationMessage, true);
		Reporter.log("Actual Message: "+remediationLogs.get(ProtectTestConstants.MESSAGE), true);
		Reporter.log("Expected Facility: "+policyBean.getCloudService(), true);
		Reporter.log("Actual Facility: "+remediationLogs.get(ProtectTestConstants.FACILITY), true);
		Reporter.log("Expected Severity: "+ProtectTestConstants.INFORMATIONAL, true);
		Reporter.log("Actual Severity: "+remediationLogs.get(ProtectTestConstants.SEVERITY), true);
		Reporter.log("Expected Source: "+ProtectTestConstants.API, true);
		Reporter.log("Actual Source: "+remediationLogs.get(ProtectTestConstants.SOURCE), true);
		Reporter.log("Expected Activity: "+ProtectTestConstants.SHARE, true);
		Reporter.log("Actual Activity: "+remediationLogs.get(ProtectTestConstants.ACTIVITY_TYPE), true);
		Reporter.log("Expected UnshareWith: "+shareWith, true);
		Reporter.log("Actual UnshareWith: "+remediationLogs.get(ProtectTestConstants.SHARED_WITH), true);
		Reporter.log("=====================================", true);
		Assert.assertTrue(remediationLogs.get(ProtectTestConstants.MESSAGE).contains(remediationMessage));
		Assert.assertEquals(remediationLogs.get(ProtectTestConstants.FACILITY), policyBean.getCloudService());
		Assert.assertEquals(remediationLogs.get(ProtectTestConstants.SEVERITY), ProtectTestConstants.INFORMATIONAL);
		Assert.assertEquals(remediationLogs.get(ProtectTestConstants.SOURCE), ProtectTestConstants.API);
		Assert.assertEquals(remediationLogs.get(ProtectTestConstants.ACTIVITY_TYPE), ProtectTestConstants.SHARE);
		Assert.assertEquals(remediationLogs.get(ProtectTestConstants.SHARED_WITH), shareWith);
	}
	
	public void o365SiteRemoveCollaboratorRemediationVerification(Client restClient, List<NameValuePair> requestHeader, TestSuiteDTO suiteData, PolicyBean policyBean, String fileName) throws Exception{
		String[] sharing = policyBean.getPolicyType().split("-");
		String userSharing = sharing[0];
		String userRole = sharing[1];
		String remediationMessage = null;
		String unshareWith = null;
		String email = "mayurbelekar@hotmail.com";
		if(userSharing.equals("Mayur Belekar")){
			unshareWith = email+"("+userRole+")";
		}
		remediationMessage = "User unshared '"+fileName+"' with '"+unshareWith+"'.";
		Map<String, String> remediationLogs = this.getInformationalDisplayLogs(restClient, policyBean.getCloudService(),
				policyBean.getFileName(), requestHeader, suiteData, remediationMessage);
		Reporter.log("=====================================", true);
		Reporter.log("Remediation parameters: "+remediationLogs, true);
		Reporter.log("=====================================", true);
		Reporter.log("**Remediation Message**", true);
		Reporter.log("Expected Message: "+remediationMessage, true);
		Reporter.log("Actual Message: "+remediationLogs.get(ProtectTestConstants.MESSAGE), true);
		Reporter.log("Expected Facility: "+policyBean.getCloudService(), true);
		Reporter.log("Actual Facility: "+remediationLogs.get(ProtectTestConstants.FACILITY), true);
		Reporter.log("Expected Severity: "+ProtectTestConstants.INFORMATIONAL, true);
		Reporter.log("Actual Severity: "+remediationLogs.get(ProtectTestConstants.SEVERITY), true);
		Reporter.log("Expected Source: "+ProtectTestConstants.API, true);
		Reporter.log("Actual Source: "+remediationLogs.get(ProtectTestConstants.SOURCE), true);
		Reporter.log("Expected Activity: "+ProtectTestConstants.UNSHARE, true);
		Reporter.log("Actual Activity: "+remediationLogs.get(ProtectTestConstants.ACTIVITY_TYPE), true);
		Reporter.log("Expected UnshareWith: "+unshareWith, true);
		Reporter.log("Actual UnshareWith: "+remediationLogs.get(ProtectTestConstants.UNSHARED_WITH), true);
		Reporter.log("=====================================", true);
		Assert.assertTrue(remediationLogs.get(ProtectTestConstants.MESSAGE).contains(remediationMessage));
		Assert.assertEquals(remediationLogs.get(ProtectTestConstants.FACILITY), policyBean.getCloudService());
		Assert.assertEquals(remediationLogs.get(ProtectTestConstants.SEVERITY), ProtectTestConstants.INFORMATIONAL);
		Assert.assertEquals(remediationLogs.get(ProtectTestConstants.SOURCE), ProtectTestConstants.API);
		Assert.assertEquals(remediationLogs.get(ProtectTestConstants.ACTIVITY_TYPE), ProtectTestConstants.UNSHARE);
		Assert.assertEquals(remediationLogs.get(ProtectTestConstants.UNSHARED_WITH), unshareWith);
	}
	
	public void o365SiteDeletePermissionRemediationVerification(Client restClient, List<NameValuePair> requestHeader, TestSuiteDTO suiteData, PolicyBean policyBean, String fileName) throws Exception{
		String[] sharing = policyBean.getPolicyType().split("-");
		String userSharing = sharing[0];
		String userRole = sharing[1];
		String remediationMessage = null;
		String unshareWith = null;
		String email = "mayurbelekar@hotmail.com";
		if(userSharing.equals("Everyone")){
			unshareWith = email+","+userSharing+"("+userRole+")";
		}if(userSharing.equals("Everyone except external users")){
			unshareWith = userSharing+"("+userRole+")";
		}
		remediationMessage = "User unshared '"+fileName+"' with '"+unshareWith;
		Map<String, String> remediationLogs = this.getInformationalDisplayLogs(restClient, policyBean.getCloudService(),
				policyBean.getFileName(), requestHeader, suiteData, remediationMessage);
		Reporter.log("=====================================", true);
		Reporter.log("Remediation parameters: "+remediationLogs, true);
		Reporter.log("=====================================", true);
		Reporter.log("**Remediation Message**", true);
		Reporter.log("Expected Message: "+remediationMessage, true);
		Reporter.log("Actual Message: "+remediationLogs.get(ProtectTestConstants.MESSAGE), true);
		Reporter.log("Expected Facility: "+policyBean.getCloudService(), true);
		Reporter.log("Actual Facility: "+remediationLogs.get(ProtectTestConstants.FACILITY), true);
		Reporter.log("Expected Severity: "+ProtectTestConstants.INFORMATIONAL, true);
		Reporter.log("Actual Severity: "+remediationLogs.get(ProtectTestConstants.SEVERITY), true);
		Reporter.log("Expected Source: "+ProtectTestConstants.API, true);
		Reporter.log("Actual Source: "+remediationLogs.get(ProtectTestConstants.SOURCE), true);
		Reporter.log("Expected Activity: "+ProtectTestConstants.UNSHARE, true);
		Reporter.log("Actual Activity: "+remediationLogs.get(ProtectTestConstants.ACTIVITY_TYPE), true);
		Reporter.log("Expected UnshareWith: "+unshareWith+","+suiteData.getSaasAppUsername()+"(Full Control)", true);
		Reporter.log("Actual UnshareWith: "+remediationLogs.get(ProtectTestConstants.UNSHARED_WITH), true);
		Reporter.log("=====================================", true);
		Assert.assertTrue(remediationLogs.get(ProtectTestConstants.MESSAGE).contains(remediationMessage));
		Assert.assertEquals(remediationLogs.get(ProtectTestConstants.FACILITY), policyBean.getCloudService());
		Assert.assertEquals(remediationLogs.get(ProtectTestConstants.SEVERITY), ProtectTestConstants.INFORMATIONAL);
		Assert.assertEquals(remediationLogs.get(ProtectTestConstants.SOURCE), ProtectTestConstants.API);
		Assert.assertEquals(remediationLogs.get(ProtectTestConstants.ACTIVITY_TYPE), ProtectTestConstants.UNSHARE);
		Assert.assertEquals(remediationLogs.get(ProtectTestConstants.UNSHARED_WITH), unshareWith+","+suiteData.getSaasAppUsername()+"(Full Control)");
	}
	
	public void o365SiteUpdateshareRemediationVerification(Client restClient, List<NameValuePair> requestHeader, TestSuiteDTO suiteData, PolicyBean policyBean, String fileName) throws Exception{
		String shareWith = null;
		String email = "mayurbelekar@hotmail.com";
		String remediationMessage = null;
		String[] sharing = policyBean.getPolicyType().split("-");
		String userSharing = sharing[0];
		String userRole = sharing[1];
		String unshareWith = userSharing+"("+userRole+")";
		if(policyBean.getRemediationActivity().equals("everyoneRead") && policyBean.getPolicyType().contains("Everyone except external users")){
			shareWith = "Everyone(Read),mayurbelekar@hotmail.com";
		}else if(policyBean.getRemediationActivity().equals("everyoneRead")) {
			shareWith = "Everyone(Read)";
		}
		if(policyBean.getRemediationActivity().equals("everyoneContribute") && policyBean.getPolicyType().contains("Everyone except external users")){
			shareWith = "Everyone(Contribute),mayurbelekar@hotmail.com";
		}else if(policyBean.getRemediationActivity().equals("everyoneContribute")) {
			shareWith = "Everyone(Contribute)";
		}
		if(policyBean.getRemediationActivity().equals("everyoneEdit") && policyBean.getPolicyType().contains("Everyone except external users")){
			shareWith = "Everyone(Edit),mayurbelekar@hotmail.com";
		}else if(policyBean.getRemediationActivity().equals("everyoneEdit")) {
			shareWith = "Everyone(Edit)";
		}
		if(policyBean.getRemediationActivity().equals("everyoneFullControl") && policyBean.getPolicyType().contains("Everyone except external users")){
			shareWith = "Everyone(Full Control),mayurbelekar@hotmail.com";
		}else if(policyBean.getRemediationActivity().equals("everyoneFullControl")) {
			shareWith = "Everyone(Full Control)";
		}
		if(policyBean.getRemediationActivity().equals("everyoneDesign") && policyBean.getPolicyType().contains("Everyone except external users")){
			shareWith = "Everyone(Design),mayurbelekar@hotmail.com";
		}else if(policyBean.getRemediationActivity().equals("everyoneDesign")) {
			shareWith = "Everyone(Design)";
		}
		
		if(policyBean.getRemediationActivity().equals("everyoneExceptExternalUserRead") && policyBean.getPolicyType().contains("Everyone-")){
			shareWith = "Everyone except external users(Read)";
			unshareWith = email+","+userSharing+"("+userRole+")";
		}else if(policyBean.getRemediationActivity().equals("everyoneExceptExternalUserRead")){
			shareWith = "Everyone except external users(Read)";
		}
		if(policyBean.getRemediationActivity().equals("everyoneExceptExternalUserContribute") && policyBean.getPolicyType().contains("Everyone-")){
			unshareWith = email+","+userSharing+"("+userRole+")";
			shareWith = "Everyone except external users(Contribute)";
		}else if(policyBean.getRemediationActivity().equals("everyoneExceptExternalUserContribute")){
			shareWith = "Everyone except external users(Contribute)";
		}
		if(policyBean.getRemediationActivity().equals("everyoneExceptExternalUserDesign") && policyBean.getPolicyType().contains("Everyone-")){
			unshareWith = email+","+userSharing+"("+userRole+")";
			shareWith = "Everyone except external users(Design)";
		}else if(policyBean.getRemediationActivity().equals("everyoneExceptExternalUserDesign")){
			shareWith = "Everyone except external users(Design)";
		}
		if(policyBean.getRemediationActivity().equals("everyoneExceptExternalUserEdit") && policyBean.getPolicyType().contains("Everyone-")){
			unshareWith = email+","+userSharing+"("+userRole+")";
			shareWith = "Everyone except external users(Edit)";
		}else if(policyBean.getRemediationActivity().equals("everyoneExceptExternalUserEdit")){
			shareWith = "Everyone except external users(Edit)";
		}
		if(policyBean.getRemediationActivity().equals("everyoneExceptExternalUserFullControl") && policyBean.getPolicyType().contains("Everyone-")){
			unshareWith = email+","+userSharing+"("+userRole+")";
			shareWith = "Everyone except external users(Full Control)";
		}else if(policyBean.getRemediationActivity().equals("everyoneExceptExternalUserFullControl")){
			shareWith = "Everyone except external users(Full Control)";
		}
		remediationMessage = "User shared '"+fileName+"' with '"+shareWith+"'.";
		Map<String, String> remediationLogs = this.getInformationalDisplayLogs(restClient, policyBean.getCloudService(),
				policyBean.getFileName(), requestHeader, suiteData, remediationMessage);
		Reporter.log("=====================================", true);
		Reporter.log("Remediation parameters: "+remediationLogs, true);
		Reporter.log("=====================================", true);
		Reporter.log("**Remediation Message**", true);
		Reporter.log("Expected Message: "+remediationMessage, true);
		Reporter.log("Actual Message: "+remediationLogs.get(ProtectTestConstants.MESSAGE), true);
		Reporter.log("Expected Facility: "+policyBean.getCloudService(), true);
		Reporter.log("Actual Facility: "+remediationLogs.get(ProtectTestConstants.FACILITY), true);
		Reporter.log("Expected Severity: "+ProtectTestConstants.INFORMATIONAL, true);
		Reporter.log("Actual Severity: "+remediationLogs.get(ProtectTestConstants.SEVERITY), true);
		Reporter.log("Expected Source: "+ProtectTestConstants.API, true);
		Reporter.log("Actual Source: "+remediationLogs.get(ProtectTestConstants.SOURCE), true);
		Reporter.log("Expected Activity: "+ProtectTestConstants.SHARE, true);
		Reporter.log("Actual Activity: "+remediationLogs.get(ProtectTestConstants.ACTIVITY_TYPE), true);
		Reporter.log("Expected ShareWith: "+shareWith, true);
		Reporter.log("Actual ShareWith: "+remediationLogs.get(ProtectTestConstants.SHARED_WITH), true);
		Reporter.log("Expected UnshareWith: "+unshareWith, true);
		Reporter.log("Actual UnshareWith: "+remediationLogs.get(ProtectTestConstants.UNSHARED_WITH), true);
		Reporter.log("=====================================", true);
		Assert.assertEquals(remediationLogs.get(ProtectTestConstants.MESSAGE), remediationMessage);
		Assert.assertEquals(remediationLogs.get(ProtectTestConstants.FACILITY), policyBean.getCloudService());
		Assert.assertEquals(remediationLogs.get(ProtectTestConstants.SEVERITY), ProtectTestConstants.INFORMATIONAL);
		Assert.assertEquals(remediationLogs.get(ProtectTestConstants.SOURCE), ProtectTestConstants.API);
		Assert.assertEquals(remediationLogs.get(ProtectTestConstants.ACTIVITY_TYPE), ProtectTestConstants.SHARE);
		Assert.assertEquals(remediationLogs.get(ProtectTestConstants.SHARED_WITH), shareWith);
		Assert.assertEquals(remediationLogs.get(ProtectTestConstants.UNSHARED_WITH), unshareWith);
	}
	
	public void o365SiteUnshareRemediationVerification(Client restClient, List<NameValuePair> requestHeader, TestSuiteDTO suiteData, PolicyBean policyBean, String fileName) throws Exception{
		String[] sharing = policyBean.getPolicyType().split("-");
		String userSharing = sharing[0];
		String userRole = sharing[1];
		String remediationMessage = null;
		String unshareWith = null;
		String email = "mayurbelekar@hotmail.com";
		if(policyBean.getRemediationActivity().equals("removeShare")){
			if(userSharing.equals("Everyone")){
				unshareWith = userSharing+"("+userRole+"),"+email+"'.";
			}if(userSharing.equals("Everyone except external users")){
				unshareWith = userSharing+"("+userRole+")";
			}
			remediationMessage = "User unshared '"+fileName+"' with '"+unshareWith+"'.";
		}
		Map<String, String> remediationLogs = this.getInformationalDisplayLogs(restClient, policyBean.getCloudService(),
				policyBean.getFileName(), requestHeader, suiteData, remediationMessage);
		if (remediationLogs.get(ProtectTestConstants.MESSAGE) == null) {
			if(userSharing.equals("Everyone")){
				unshareWith = email+","+userSharing+"("+userRole+")";
			}
			remediationMessage = "User unshared '"+fileName+"' with '"+unshareWith+"'.";
			remediationLogs = this.getInformationalDisplayLogs(restClient, policyBean.getCloudService(),
					policyBean.getFileName(), requestHeader, suiteData, remediationMessage);
			if (remediationLogs.get(ProtectTestConstants.MESSAGE) == null) {
				Logger.info("Remediation Logs is not found in the portal, hence test case failed");
				checkSplunkLogs(suiteData, fileName, false);
				Assert.assertTrue(false);
			}
		}
		
		Reporter.log("Remediation parameters: "+remediationLogs, true);
		Assert.assertEquals(remediationLogs.get(ProtectTestConstants.MESSAGE), remediationMessage);
		Assert.assertEquals(remediationLogs.get(ProtectTestConstants.FACILITY), policyBean.getCloudService());
		Assert.assertEquals(remediationLogs.get(ProtectTestConstants.SEVERITY), ProtectTestConstants.INFORMATIONAL);
		Assert.assertEquals(remediationLogs.get(ProtectTestConstants.SOURCE), ProtectTestConstants.API);
		Assert.assertEquals(remediationLogs.get(ProtectTestConstants.ACTIVITY_TYPE), ProtectTestConstants.UNSHARE);
		Assert.assertEquals(remediationLogs.get(ProtectTestConstants.UNSHARED_WITH), unshareWith);
	}
	
	public void salesforceSaasAppRemediation(PolicyBean policyBean, Salesforce sfapi, Map<String, String> fileIdCollection) throws Exception{
		String policyName = policyBean.getPolicyName();
		Logger.info("==============================");
		Logger.info("Salesforce Remediation Json");
		FileShares fileShares = sfapi.getFileShares(fileIdCollection.get(policyName));
		Logger.info("==============================");
		Logger.info("==============================");
		if(policyBean.getRemediationActivity().contains("deleteSharedLink")){
			Logger.info("Verifing remediate remove link");
			Logger.info("==============================");
			Logger.info("Shared Link: "+fileShares.getLinkShare());
			Logger.info("==============================");
			Assert.assertNull(fileShares.getLinkShare(), "Public share link not remediated");
		}
		if(policyBean.getRemediationActivity().contains("internalUserCollaborate")){
			Logger.info("==============================");
			Logger.info("Verifing remediate company write");
			for(int i=0; i<fileShares.getShares().size(); i++){
				if(fileShares.getShares().get(i).getEntity().getName().equals("00DQ000000GKWxSMAX")){
					Logger.info("==============================");
					Logger.info("Sharing Level: "+fileShares.getShares().get(i).getEntity().getType());
					Logger.info("Sharing Type: " +fileShares.getShares().get(i).getSharingType());
					Assert.assertEquals(fileShares.getShares().get(i).getEntity().getType(), "Organization");
					Assert.assertEquals(fileShares.getShares().get(i).getSharingType(), "C");
					Logger.info("==============================");
				}
			}
		}
		if(policyBean.getRemediationActivity().contains("internalUserView")){
			Logger.info("==============================");
			Logger.info("Verifing remediate company view");
			for(int i=0; i<fileShares.getShares().size(); i++){
				if(fileShares.getShares().get(i).getEntity().getId().equals("00DQ000000GKWxSMAX")){
					Logger.info("==============================");
					Logger.info("Sharing Level: "+fileShares.getShares().get(i).getEntity().getType());
					Logger.info("Sharing Type: " +fileShares.getShares().get(i).getSharingType());
					Assert.assertEquals(fileShares.getShares().get(i).getEntity().getType(), "Organization");
					Assert.assertEquals(fileShares.getShares().get(i).getSharingType(), "V");
					Logger.info("==============================");
				}
			}
		}
		if(policyBean.getRemediationActivity().contains("removeCollaborator")){
			boolean flag = true;
			Logger.info("==============================");
			Logger.info("Verifing remediate remove collaborator");
			for(int i=0; i<fileShares.getShares().size(); i++){
				if(fileShares.getShares().get(i).getEntity().getId().equals("005Q000000P0ykKIAR")){
					flag = false;
					break;
				}
			}
			Logger.info("==============================");
			Logger.info("Collaboration Removed: "+true);
			Logger.info("==============================");
			Assert.assertTrue(flag, "Collaboration not removed");
		}
		if(policyBean.getRemediationActivity().contains("005Q000000P0ykKIAR")){
			Logger.info("==============================");
			Logger.info("Verifing remediate collaborator write");
			for(int i=0; i<fileShares.getShares().size(); i++){
				if(fileShares.getShares().get(i).getEntity().getId().equals("005Q000000P0ykKIAR")){
					Logger.info("==============================");
					Logger.info("Sharing Level: "+fileShares.getShares().get(i).getEntity().getType());
					Logger.info("Sharing Type: " +fileShares.getShares().get(i).getSharingType());
					Assert.assertEquals(fileShares.getShares().get(i).getEntity().getType(), "User");
					Assert.assertEquals(fileShares.getShares().get(i).getSharingType(), "C");
					Logger.info("==============================");
				}
			}
		}
		if(policyBean.getRemediationActivity().contains("updateCollaboratorView")){
			Logger.info("==============================");
			Logger.info("Verifing remediate collaborator read");
			for(int i=0; i<fileShares.getShares().size(); i++){
				if(fileShares.getShares().get(i).getEntity().getId().equals("005Q000000P0ykKIAR")){
					Logger.info("==============================");
					Logger.info("Sharing Level: "+fileShares.getShares().get(i).getEntity().getType());
					Logger.info("Sharing Type: " +fileShares.getShares().get(i).getSharingType());
					Assert.assertEquals(fileShares.getShares().get(i).getEntity().getType(), "User");
					Assert.assertEquals(fileShares.getShares().get(i).getSharingType(), "V");
					Logger.info("==============================");
				}
			}
		}
	}
}