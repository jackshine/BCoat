package com.elastica.action.protect;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.Reporter;

import com.elastica.action.Action;
import com.elastica.action.backend.BEAction;
import com.elastica.common.SuiteData;
import com.elastica.gateway.GatewayTestConstants;
import com.elastica.logger.Logger;
import com.elastica.pagefactory.AdvancedPageFactory;
import com.elastica.pageobjects.protect.ProtectPage;
import com.elastica.pageobjects.securlets.SecurletDashboardPage;
import com.elastica.restClient.Client;
import com.elastica.restClient.ClientUtil;


/**
 * Protect Action class
 * @author Eldo Rajan
 *
 */
public class ProtectAction extends Action{

	BEAction action = new BEAction();

	public String getNoDataHeadertext(WebDriver driver) {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		ProtectPage pp =  AdvancedPageFactory.getPageObject(driver, ProtectPage.class);
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());	

		return pp.noDataHeader(driver).getText().trim();		
	}
	
	
	public String getAllActivitiesLogsText(WebDriver driver) {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		ProtectPage pp =  AdvancedPageFactory.getPageObject(driver,ProtectPage.class);
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());	
		return pp.activityLogCount(driver).getText().trim();
	}
	
	
	public HttpResponse createPolicy(Client restClient, SuiteData suiteData, String payload){
		HttpResponse policyResponse = null;
		StringEntity entity;
		List<NameValuePair> headers = action.getHeaders(suiteData);
		try{
			String policyUrl = "/controls/addpolicy";
			URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(), policyUrl);
			Logger.info("========================================");
			Logger.info("Payload for Policy: "+payload);
			Logger.info("========================================");
			entity = new StringEntity(payload);
			policyResponse = restClient.doPost(uri, headers, null, entity);
		}catch(Exception e){
			e.printStackTrace();
		}
		return policyResponse;
	}
	
	public void createFileSharingPolicy(Client restClient, SuiteData suiteData, ProtectDTO protectData) throws Exception{
		if(protectData.getActionList() == null){
			List<String> actionList = new ArrayList<String>();
			actionList.add("NOTIFY_USER");
			protectData.setActionList(actionList);
		}else{
			protectData.getActionList().add("NOTIFY_USER");
		}
		List<NameValuePair> headers = action.getHeaders(suiteData);
		if(!getPolicyDetails(restClient, suiteData, headers, protectData.getPolicyName()).isEmpty()){
			deletePolicy(restClient, suiteData, protectData.getPolicyName());
		}
		protectData.setPolicytype("FileSharing");
		logPolicyDetails(protectData, suiteData);
		ProtectQueryBuilder protectQueryBuilder = new ProtectQueryBuilder();
		String payload = protectQueryBuilder.getFileSharingPolicyESQuery(protectData);
		HttpResponse response = createPolicy(restClient, suiteData, payload);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_OK,"Response status doesn't match");
		Logger.info("========================================");
		Logger.info("Create Policy Response: "+ClientUtil.getResponseBody(response));
		Logger.info("========================================");
	}
	
	public void createEnableFileSharingPolicy(Client restClient, SuiteData suiteData, 
			String policyName, String severity) throws Exception{
		ProtectDTO protectData = new ProtectDTO();
		List<String> userList = new ArrayList<String>();
		userList.add(suiteData.getTestUsername());	
		protectData.setUserList(userList);
		List<String> appList = new ArrayList<String>();
		appList.add(suiteData.getSaasAppName());
		protectData.setApplicationList(appList);
		protectData.setPolicyName(policyName);
		protectData.setSeverity(severity);
		protectData.setStatus(true);
		createFileSharingPolicy(restClient, suiteData, protectData);
	}
	
	public void createEnableFileSharingPolicyWithBlock(Client restClient, SuiteData suiteData, 
			String policyName, String severity) throws Exception{
		ProtectDTO protectData = new ProtectDTO();
		List<String> userList = new ArrayList<String>();
		userList.add(suiteData.getTestUsername());	
		protectData.setUserList(userList);
		List<String> appList = new ArrayList<String>();
		appList.add(suiteData.getSaasAppName());
		List<String> actionList = new ArrayList<String>();
	    actionList.add("BLOCK_SHARE");
	    protectData.setActionList(actionList);
		protectData.setApplicationList(appList);
		protectData.setPolicyName(policyName);
		protectData.setSeverity(severity);
		protectData.setStatus(true);
		createFileSharingPolicy(restClient, suiteData, protectData);
	}
	
	
	public void createEnableFileSharingPolicyByShare(Client restClient, SuiteData suiteData,
			String policyName, String recipentname, String severity) throws Exception {
		ProtectDTO protectData = new ProtectDTO();
        List<String> userList = new ArrayList<String>();
        userList.add(suiteData.getTestUsername());
        protectData.setUserList(userList);
        List<String> receipentList = new ArrayList<String>();
        receipentList.add(recipentname);
        protectData.setUserRecipientList(receipentList);
        List<String> actionList = new ArrayList<String>();
        actionList.add("BLOCK_SHARE");
        protectData.setActionList(actionList);
        protectData.setPolicyName(policyName);
        protectData.setStatus(true);
        protectData.setSeverity(severity);
        createFileSharingPolicy(restClient, suiteData, protectData);
	}
	
	public void logPolicyDetails(ProtectDTO protectData, SuiteData suiteData){
		Reporter.log("========================================");
		Reporter.log("POLICY DETAILS");
		Reporter.log("========================================");
		Reporter.log("Policy Name: "+protectData.getPolicyName());
		Reporter.log("Policy Type: "+protectData.getPolicyType());
		Reporter.log("SaaS Apps: "+protectData.getApplicationList());
		if(protectData.getPolicyType().equals("FileTransfer")){
			Reporter.log("Transfer Type: "+protectData.getTransferType());
		}else if(protectData.getPolicyType().equals("AccessEnforcement")){
			Reporter.log("Object Activities : "+protectData.getActivities());
		}
		
		if(protectData.getUserList() == null){
			Reporter.log("Users: Any");
		}else{
			Reporter.log("Users: "+protectData.getUserList());
		}
		
		if(protectData.getUserList() == null){
			if(protectData.getGroupList() == null){
				Reporter.log("Groups: Any");
			}else{
				Reporter.log("Groups: Empty");
			}
		}else{
			Reporter.log("Groups: "+protectData.getGroupList());
		}
		
		if(protectData.getDomainList() == null){
			Reporter.log("Domain: Any");
		}else{
			Reporter.log("Domain: "+protectData.getDomainList());
		}
		
		Reporter.log("Users Exception: "+protectData.getUserWhiteList());
		Reporter.log("Groups Exception: "+protectData.getGroupWhiteList());
		Reporter.log("ThreatScore: "+protectData.getThreadScore());
		Reporter.log("Browser: "+suiteData.getBrowser());
		Reporter.log("Filename: "+protectData.getFilenamePattern());
		Reporter.log("FileSize: Larger="+protectData.getLargerSize()+"/Smaller="+protectData.getSmallerSize());
		Reporter.log("Severity: "+protectData.getSeverity());
		Reporter.log("========================================");
	}
	
	public void createFileTransferPolicy(Client restClient, SuiteData suiteData, ProtectDTO protectData) throws Exception{
		if(protectData.getActionList() == null){
			List<String> actionList = new ArrayList<String>();
			actionList.add("NOTIFY_USER");
			protectData.setActionList(actionList);
		}else{
			protectData.getActionList().add("NOTIFY_USER");
		}
		List<NameValuePair> headers = action.getHeaders(suiteData);
		if(!getPolicyDetails(restClient, suiteData, headers, protectData.getPolicyName()).isEmpty()){
			deletePolicy(restClient, suiteData, protectData.getPolicyName());
		}
		protectData.setPolicytype("FileTransfer");
		logPolicyDetails(protectData, suiteData);
		ProtectQueryBuilder protectQueryBuilder = new ProtectQueryBuilder();
		String payload = protectQueryBuilder.getFileTransferPolicyESQuery(protectData);
		HttpResponse response = createPolicy(restClient, suiteData, payload);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_OK,"Response status doesn't match");
		Logger.info("========================================");
		Logger.info("Create Policy Response: "+ClientUtil.getResponseBody(response));
		Logger.info("========================================");
	}
	
	
	public void createEnableFileTransferPolicyWithUploadAndDownload(Client restClient, SuiteData suiteData,
		String policyName, String severity) throws Exception {
		ProtectDTO protectData = new ProtectDTO();
		List<String> userList = new ArrayList<String>();
		userList.add(suiteData.getTestUsername());	
		protectData.setUserList(userList);
		List<String> appList = new ArrayList<String>();
		appList.add(suiteData.getSaasAppName());
		protectData.setApplicationList(appList);
		List<String> transferType = new ArrayList<String>();
		transferType.add("Upload");
		transferType.add("Download");
		protectData.setTransferType(transferType);
		protectData.setPolicyName(policyName);
		protectData.setSeverity(severity);
		protectData.setStatus(true);
		createFileTransferPolicy(restClient, suiteData, protectData);
	}
	
	public void createEnableFileTransferPolicyWithUploadAndDownloadBlock(Client restClient, SuiteData suiteData,
		String policyName, String severity) throws Exception {
		ProtectDTO protectData = new ProtectDTO();
		List<String> userList = new ArrayList<String>();
		userList.add(suiteData.getTestUsername());	
		protectData.setUserList(userList);
		List<String> appList = new ArrayList<String>();
		appList.add(suiteData.getSaasAppName());
		protectData.setApplicationList(appList);

        List<String> actionList = new ArrayList<String>();
        actionList.add("BLOCK_SHARE");
        protectData.setActionList(actionList);
	        
		List<String> transferType = new ArrayList<String>();
		transferType.add("Upload");
		transferType.add("Download");
		protectData.setTransferType(transferType);
		protectData.setPolicyName(policyName);
		protectData.setSeverity(severity);
		protectData.setStatus(true);
		createFileTransferPolicy(restClient, suiteData, protectData);
	}
	
	
	public void createEnableFileTransferPolicyWithUploadAndDownloadBlockByFileFormatProfile(Client restClient, 
			SuiteData suiteData, String policyName, String severity, String profileName) throws Exception {
		ProtectDTO protectData = new ProtectDTO();
		List<String> userList = new ArrayList<String>();
		userList.add(suiteData.getTestUsername());	
		protectData.setUserList(userList);
		List<String> appList = new ArrayList<String>();
		appList.add(suiteData.getSaasAppName());
		protectData.setApplicationList(appList);

        List<String> actionList = new ArrayList<String>();
        actionList.add("BLOCK_SHARE");
        protectData.setActionList(actionList);
        
        
        List<String> contentProfiles = new ArrayList<String>();
        contentProfiles.add(profileName);
        protectData.setContentProfiles(contentProfiles);
		List<String> transferType = new ArrayList<String>();
		transferType.add("Upload");
		transferType.add("Download");
		protectData.setTransferType(transferType);
		protectData.setPolicyName(policyName);
		protectData.setSeverity(severity);
		protectData.setStatus(true);
		createFileTransferPolicy(restClient, suiteData, protectData);
	}
	
	
	public void createEnableFileTransferPolicyWithUploadAndDownloadBlockWithFileSize(Client restClient, SuiteData suiteData,
		String policyName, String severity, int minimum, int maxmimum) throws Exception {
		ProtectDTO protectData = new ProtectDTO();
		List<String> userList = new ArrayList<String>();
		userList.add(suiteData.getTestUsername());	
		protectData.setUserList(userList);
		List<String> appList = new ArrayList<String>();
		appList.add(suiteData.getSaasAppName());
		protectData.setApplicationList(appList);

        List<String> actionList = new ArrayList<String>();
        actionList.add("BLOCK_SHARE");
        protectData.setActionList(actionList);
	       
		List<String> transferType = new ArrayList<String>();
		transferType.add("Upload");
		transferType.add("Download");
		protectData.setTransferType(transferType);
		protectData.setPolicyName(policyName);
		protectData.setSeverity(severity);
		protectData.setStatus(true);
		protectData.setLargerSize(minimum);
		protectData.setSmallerSize(maxmimum );
		createFileTransferPolicy(restClient, suiteData, protectData);
	}
	
	public HttpResponse deletePolicy(Client restClient, SuiteData suiteData, String policyName) throws Exception{
		List<NameValuePair> headers = action.getHeaders(suiteData);
		Map<String, String> policyDetails = getPolicyDetails(restClient, suiteData, headers, policyName);
		String payload = "{\"policy_type\":\""+policyDetails.get("policyType")+"\",\"action\":true,\"sub_id\":\""+policyDetails.get("subId")+"\"}";
		Logger.info("========================================");
		Logger.info("Delete Policy Payload: "+payload);
		Logger.info("========================================");
		String policyUrl = "/controls/archive";
		URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(), policyUrl);
		StringEntity entity = new StringEntity(payload);
		return restClient.doPost(uri, headers, null, entity);
	}
	
	public void createAccessEnforcementPolicy(Client restClient, SuiteData suiteData, ProtectDTO protectData, String activityString) throws Exception{
		if(protectData.getActionList() == null){
			List<String> actionList = new ArrayList<String>();
			actionList.add("BLOCK_SHARE");
			protectData.setActionList(actionList);
		}
		List<NameValuePair> headers = action.getHeaders(suiteData);
		if(!getPolicyDetails(restClient, suiteData, headers, protectData.getPolicyName()).isEmpty()){
			deletePolicy(restClient, suiteData, protectData.getPolicyName());
		}
		protectData.setPolicytype("AccessEnforcement");
		logPolicyDetails(protectData, suiteData);
		ProtectQueryBuilder protectQueryBuilder = new ProtectQueryBuilder();
		String activityScope = getActivitiesScope(activityString);
		String payload = protectQueryBuilder.getAccessEnforcementPolicyESQuery(protectData);
		payload = payload.replace("\"ActivityObjects\"", activityScope);
		HttpResponse response = createPolicy(restClient, suiteData, payload);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_OK,"Response status doesn't match");
		Logger.info("========================================");
		Logger.info("Create Policy Response: "+ClientUtil.getResponseBody(response));
		Logger.info("========================================");
	}
	
	public void createEnableAccessEnforcementPolicy(Client restClient, SuiteData suiteData,
		String policyName, String activity, String severity) throws Exception {
		ProtectDTO protectData = new ProtectDTO();
        List<String> userList = new ArrayList<String>();
        userList.add(suiteData.getTestUsername());    
        protectData.setUserList(userList);
        List<String> appList = new ArrayList<String>();
        appList.add(suiteData.getSaasAppName());
        protectData.setApplicationList(appList);
        protectData.setStatus(true);
        protectData.setPolicyName(policyName);
        protectData.setSeverity(severity);
        createAccessEnforcementPolicy(restClient, suiteData, protectData, activity);
	}
	
	public void createEnableAccessEnforcementPolicyWithSAASApp(Client restClient, SuiteData suiteData,
			String policyName, String activity, String severity, List<String>  testuserNames,  List<String> saasAppNames ) throws Exception {
			ProtectDTO protectData = new ProtectDTO();
	        List<String> userList = new ArrayList<String>();
	        userList.addAll(testuserNames);    
	        protectData.setUserList(userList);
	        List<String> appList = new ArrayList<String>();
	        appList.addAll(saasAppNames);
	        protectData.setApplicationList(appList);
	        protectData.setStatus(true);
	        protectData.setPolicyName(policyName);
	        protectData.setSeverity(severity);
	        createAccessEnforcementPolicy(restClient, suiteData, protectData, activity);
	}
		
	public String getActivitiesScope(String scope){
		String returnScope = "";
		if(scope.contains(",")){
			String[] objects = scope.split(",");
			for(int i=0;i<objects.length;i++){
				String[] object = objects[i].split(":");
				String objectType = object[0];
				String objectActivity = object[1];
				returnScope = returnScope+"{\""+objectType+"\":[";
				if(objectActivity.contains("/")){
					String[] objectActivities = objectActivity.split("/");
					for(int j=0;j<objectActivities.length;j++){
						returnScope = returnScope+"\""+objectActivities[j]+"\",";
					}
				}else{
					returnScope = returnScope+"\""+objectActivity+"\",";
				}
				returnScope = returnScope.substring(0, returnScope.length()-1);
				returnScope = returnScope+"]},";
			}
		}else{
			String[] activity = scope.split(":");
			returnScope = "{\""+activity[0]+"\":[\""+activity[1]+"\"]},";
		}
		returnScope = returnScope.substring(0, returnScope.length()-1);
		return returnScope;
	}
	
	public void activatePolicy(Client restClient, SuiteData suiteData, String policyName) throws Exception{
		List<NameValuePair> headers = action.getHeaders(suiteData);
		Map<String, String> policyDetails = getPolicyDetails(restClient, suiteData, headers, policyName);
		String payload = "{\"policy_type\":\""+policyDetails.get("policyType")+"\",\"status\":true,\"sub_id\":\""+policyDetails.get("subId")+"\"}";
		Logger.info("========================================");
		Logger.info("Activate Policy Payload: "+payload);
		Logger.info("========================================");
		String policyUrl = "/controls/update";
		URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(), policyUrl);
		StringEntity entity = new StringEntity(payload);
		HttpResponse response = restClient.doPost(uri, headers, null, entity);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_OK,"Response status doesn't match");
		String responseBody = ClientUtil.getResponseBody(response);
		Logger.info("========================================");
		Logger.info("Activate Policy responsebody: "+responseBody);
		Logger.info("========================================");
		String updateStatus = ClientUtil.getJSONValue(responseBody, "updated_status");
		String actionStatus = ClientUtil.getJSONValue(responseBody, "action_status");
		actionStatus = actionStatus.substring(1, actionStatus.length()-1);
		Assert.assertEquals(actionStatus, "success");
		Assert.assertTrue(Boolean.valueOf(updateStatus));
	}
	
	public void deActivatePolicy(Client restClient, SuiteData suiteData, String policyName) throws Exception{
		List<NameValuePair> headers = action.getHeaders(suiteData);
		Map<String, String> policyDetails = getPolicyDetails(restClient, suiteData, headers, policyName);
		String payload = "{\"policy_type\":\""+policyDetails.get("policyType")+"\",\"status\":false,\"sub_id\":\""+policyDetails.get("subId")+"\"}";
		Logger.info("========================================");
		Logger.info("Deactivate Policy Payload: "+payload);
		Logger.info("========================================");
		String policyUrl = "/controls/update";
		URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(), policyUrl);
		StringEntity entity = new StringEntity(payload);
		HttpResponse response = restClient.doPost(uri, headers, null, entity);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_OK,"Response status doesn't match");
		String responseBody = ClientUtil.getResponseBody(response);
		Logger.info("========================================");
		Logger.info("Deactivate Policy responsebody: "+responseBody);
		Logger.info("========================================");
		String updateStatus = ClientUtil.getJSONValue(responseBody, "updated_status");
		String actionStatus = ClientUtil.getJSONValue(responseBody, "action_status");
		actionStatus = actionStatus.substring(1, actionStatus.length()-1);
		Assert.assertEquals(actionStatus, "success");
		Assert.assertFalse(Boolean.valueOf(updateStatus));
	}
	
	public Map<String, String> getPolicyDetails(Client restClient, SuiteData suiteData, List<NameValuePair> headers, String policyName) throws Exception{
		Map<String, String> policyDetails = new HashMap<String, String>();
		String policyUrl = "/controls/list";
		URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(),policyUrl);
		HttpResponse policyListResponse = restClient.doGet(uri, headers);
		String policyListResponseBody = ClientUtil.getResponseBody(policyListResponse);
		String policiesList = ClientUtil.getJSONValue(policyListResponseBody, "policies");
		JSONArray policyListArray = new JSONArray(policiesList);
		if(policyListArray.length()>0){
			for(int i=0;i<policyListArray.length();i++){
				JSONObject policyObject = policyListArray.getJSONObject(i);
				if(policyObject.getString("policy_name").equals(policyName)){
					String subId = policyObject.getString("sub_id");
					String id = policyObject.getString("id");
					String policyType = policyObject.getString("policy_type");
					policyDetails.put("subId", subId);
					policyDetails.put("id", id);
					policyDetails.put("policyType", policyType);
					break;
				}
			}
		}
		return policyDetails;
	}
	
	public String testAddNewPolicyWithMediaFilesSupport_Any(WebDriver driver) {

		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		String message="";
		AdvancedPageFactory.getPageObject(driver, SecurletDashboardPage.class);

		try {
			ProtectPage ip =  AdvancedPageFactory.getPageObject(driver, ProtectPage.class);
			hardWait(15);
			ip.newTab(driver).click();
			ip.policy(driver).click();
			hardWait(10);
			String savePolicyinfo = ip.savePolicy(driver).getText();
			System.out.println("savePolicy : "+savePolicyinfo);
			String cancelinfo =ip.cancel(driver).getText();
			System.out.println("cancel : "+cancelinfo);
			Assert.assertEquals(savePolicyinfo, "Save Policy", "Verify Save Policy is not Present");
			Assert.assertEquals(cancelinfo, "Cancel", "Verify Cancel is not Present");
			Random rnd = new Random();
			int n = 100000 + rnd.nextInt(900000);
			ip.policyName(driver).type("PolicyDataExposureviaSecurlets"+ n);
			ip.clickpolicydropdown(driver).click();
			ip.selectDataExposureviaSecurlets(driver).click();
			ip.cloudServiceDropdownlist(driver).click();
			hardWait(2);
			ip.selectBox(driver).click();
			ip.selectinternalCheckBox(driver).click();
			String strfirstpolicyTypedropdownTest = ip.firstpolicydropdowntest(driver).getText();
			System.out.println("FirstPolicyTypeDropdownText : "+ strfirstpolicyTypedropdownTest);
			String strlogviolationtext = ip.logviolationtext(driver).getText();
			System.out.println("LogViolationText : "+ strlogviolationtext);
			Assert.assertEquals(strlogviolationtext, "Policy violations are logged under Protect:Alerts and Investigate.", "Verify Log Violation is not Matched");
			ip.fileOwnerSelective(driver).click();
			ip.fileOwnerSelectiveText(driver).type("test");
			ip.clickFileOwnerName(driver).click();
			ip.savePolicyButton(driver).click();
			String successAlert = ip.successAlert(driver).getText();
			System.out.println("successAlert : "+successAlert);
			Assert.assertEquals(successAlert, "Your request has been processed successfully.", "Verify successAlert is not Present");
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return message;

	}
	
	public String testAddNewPolicyWithMediaFilesSupport(WebDriver driver) {

		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		String message="";
		AdvancedPageFactory.getPageObject(driver, SecurletDashboardPage.class);

		try {
			ProtectPage ip =  AdvancedPageFactory.getPageObject(driver, ProtectPage.class);
			hardWait(15);
			ip.newTab(driver).click();
			ip.policy(driver).click();
			hardWait(10);
			String savePolicyinfo = ip.savePolicy(driver).getText();
			System.out.println("savePolicy : "+savePolicyinfo);
			String cancelinfo =ip.cancel(driver).getText();
			System.out.println("cancel : "+cancelinfo);
			Assert.assertEquals(savePolicyinfo, "Save Policy", "Verify Save Policy is not Present");
			Assert.assertEquals(cancelinfo, "Cancel", "Verify Cancel is not Present");
			Random rnd = new Random();
			int n = 100000 + rnd.nextInt(900000);
			ip.policyName(driver).type("PolicyDataExposureviaSecurlets"+ n);
			ip.clickpolicydropdown(driver).click();
			ip.selectDataExposureviaSecurlets(driver).click();
			ip.cloudServiceDropdownlist(driver).click();
			ip.selectBox(driver).click();
			ip.selectinternalCheckBox(driver).click();
			String strfirstpolicyTypedropdownTest = ip.firstpolicydropdowntest(driver).getText();
			System.out.println("FirstPolicyTypeDropdownText : "+ strfirstpolicyTypedropdownTest);
			String strlogviolationtext = ip.logviolationtext(driver).getText();
			System.out.println("LogViolationText : "+ strlogviolationtext);
			Assert.assertEquals(strlogviolationtext, "Policy violations are logged under Protect:Alerts and Investigate.", "Verify Log Violation is not Matched");
			ip.fileOwnerSelective(driver).click();
			ip.fileOwnerSelectiveText(driver).type("test");
			ip.clickFileOwnerName(driver).click();
			ip.filetypeSelectiveText(driver).type("jpf");
			ip.filetypeSelectiveText(driver).click();
			ip.savePolicyButton(driver).click();
			String successAlert = ip.successAlert(driver).getText();
			System.out.println("successAlert : "+successAlert);
			Assert.assertEquals(successAlert, "Your request has been processed successfully.", "Verify successAlert is not Present");
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return message;

	}
	
	public String testAddNewPolicyDefaulySizeAny(WebDriver driver) {

		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		String message="";
		AdvancedPageFactory.getPageObject(driver, SecurletDashboardPage.class);

		try {
			ProtectPage ip =  AdvancedPageFactory.getPageObject(driver, ProtectPage.class);
			hardWait(15);
			ip.newTab(driver).click();
			ip.policy(driver).click();
			hardWait(10);
			String savePolicyinfo = ip.savePolicy(driver).getText();
			System.out.println("savePolicy : "+savePolicyinfo);
			String cancelinfo =ip.cancel(driver).getText();
			System.out.println("cancel : "+cancelinfo);
			Assert.assertEquals(savePolicyinfo, "Save Policy", "Verify Save Policy is not Present");
			Assert.assertEquals(cancelinfo, "Cancel", "Verify Cancel is not Present");
			Random rnd = new Random();
			int n = 100000 + rnd.nextInt(900000);
			ip.policyName(driver).type("PolicyDataExposureviaSecurlets"+ n);
			ip.clickpolicydropdown(driver).click();
			ip.selectDataExposureviaSecurlets(driver).click();
			ip.cloudServiceDropdownlist(driver).click();
			ip.selectBox(driver).click();
			ip.selectinternalCheckBox(driver).click();
			String strfirstpolicyTypedropdownTest = ip.firstpolicydropdowntest(driver).getText();
			System.out.println("FirstPolicyTypeDropdownText : "+ strfirstpolicyTypedropdownTest);
			String strlogviolationtext = ip.logviolationtext(driver).getText();
			System.out.println("LogViolationText : "+ strlogviolationtext);
			Assert.assertEquals(strlogviolationtext, "Policy violations are logged under Protect:Alerts and Investigate.", "Verify Log Violation is not Matched");
			String selectFileSizeAny = ip.selectFileSizeAny(driver).getText();
			System.out.println("selectFileSizeAny : "+ selectFileSizeAny);
			Assert.assertEquals(selectFileSizeAny, "Any", "Any is not active");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return message;
	}
	public String verifySizeRangeSlider(WebDriver driver) {

		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		String message="";
		AdvancedPageFactory.getPageObject(driver, SecurletDashboardPage.class);

		try {
			ProtectPage ip =  AdvancedPageFactory.getPageObject(driver, ProtectPage.class);
			hardWait(15);
			ip.newTab(driver).click();
			ip.policy(driver).click();
			hardWait(10);
			String savePolicyinfo = ip.savePolicy(driver).getText();
			System.out.println("savePolicy : "+savePolicyinfo);
			String cancelinfo =ip.cancel(driver).getText();
			System.out.println("cancel : "+cancelinfo);
			Assert.assertEquals(savePolicyinfo, "Save Policy", "Verify Save Policy is not Present");
			Assert.assertEquals(cancelinfo, "Cancel", "Verify Cancel is not Present");
			Random rnd = new Random();
			int n = 100000 + rnd.nextInt(900000);
			ip.policyName(driver).type("PolicyDataExposureviaSecurlets"+ n);
			ip.clickpolicydropdown(driver).click();
			ip.selectDataExposureviaSecurlets(driver).click();
			ip.cloudServiceDropdownlist(driver).click();
			ip.selectBox(driver).click();
			ip.selectinternalCheckBox(driver).click();
			String strfirstpolicyTypedropdownTest = ip.firstpolicydropdowntest(driver).getText();
			System.out.println("FirstPolicyTypeDropdownText : "+ strfirstpolicyTypedropdownTest);
			String strlogviolationtext = ip.logviolationtext(driver).getText();
			System.out.println("LogViolationText : "+ strlogviolationtext);
			Assert.assertEquals(strlogviolationtext, "Policy violations are logged under Protect:Alerts and Investigate.", "Verify Log Violation is not Matched");
			String selectFileSizeAny = ip.selectFileSizeAny(driver).getText();
			System.out.println("selectFileSizeAny : "+ selectFileSizeAny);
			Assert.assertEquals(selectFileSizeAny, "Any", "Any is not active");
			ip.selectFileSizeSelective(driver).click();
			Assert.assertTrue(ip.FileSizeBar(driver).isElementVisible(), "FileSizeBar is not visible");
		
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return message;

	}
	public String verifySizeRangeSliderDefaultLarger_Smaller(WebDriver driver) {

		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		String message="";
		AdvancedPageFactory.getPageObject(driver, SecurletDashboardPage.class);

		try {
			ProtectPage ip =  AdvancedPageFactory.getPageObject(driver, ProtectPage.class);
			hardWait(15);
			ip.newTab(driver).click();
			ip.policy(driver).click();
			hardWait(10);
			String savePolicyinfo = ip.savePolicy(driver).getText();
			System.out.println("savePolicy : "+savePolicyinfo);
			String cancelinfo =ip.cancel(driver).getText();
			System.out.println("cancel : "+cancelinfo);
			Assert.assertEquals(savePolicyinfo, "Save Policy", "Verify Save Policy is not Present");
			Assert.assertEquals(cancelinfo, "Cancel", "Verify Cancel is not Present");
			Random rnd = new Random();
			int n = 100000 + rnd.nextInt(900000);
			ip.policyName(driver).type("PolicyDataExposureviaSecurlets"+ n);
			ip.clickpolicydropdown(driver).click();
			ip.selectDataExposureviaSecurlets(driver).click();
			ip.cloudServiceDropdownlist(driver).click();
			ip.selectBox(driver).click();
			ip.selectinternalCheckBox(driver).click();
			String strfirstpolicyTypedropdownTest = ip.firstpolicydropdowntest(driver).getText();
			System.out.println("FirstPolicyTypeDropdownText : "+ strfirstpolicyTypedropdownTest);
			String strlogviolationtext = ip.logviolationtext(driver).getText();
			System.out.println("LogViolationText : "+ strlogviolationtext);
			Assert.assertEquals(strlogviolationtext, "Policy violations are logged under Protect:Alerts and Investigate.", "Verify Log Violation is not Matched");
			String selectFileSizeAny = ip.selectFileSizeAny(driver).getText();
			System.out.println("selectFileSizeAny : "+ selectFileSizeAny);
			Assert.assertEquals(selectFileSizeAny, "Any", "Any is not active");
			ip.selectFileSizeSelective(driver).click();
			Assert.assertTrue(ip.FileSizeBar(driver).isElementVisible(), "FileSizeBar is not visible");
			String barLargerThan = ip.barLargerThan(driver).getText();
			System.out.println("barLargerThan : "+ barLargerThan);
			Assert.assertEquals(barLargerThan,"Larger Than", "Verify barLargerThan is not Matched");
			String barSmallerThan = ip.barSmallerThan(driver).getText();
			System.out.println("barSmallerThan : "+ barSmallerThan);
			Assert.assertEquals(barSmallerThan,"Smaller Than", "Verify barLargerThan is not Matched");
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return message;

	}
	
	public void createThreatScorePolicy(Client restClient, SuiteData suiteData, ProtectDTO protectData) throws Exception{
		if(protectData.getThreadScore() == 0){
			protectData.setThreadScore(10);
		}
		List<NameValuePair> headers = action.getHeaders(suiteData);
		if(!getPolicyDetails(restClient, suiteData, headers, protectData.getPolicyName()).isEmpty()){
			deletePolicy(restClient, suiteData, protectData.getPolicyName());
		}
		ProtectQueryBuilder protectQueryBuilder = new ProtectQueryBuilder();
		String payload = protectQueryBuilder.getThreatScorePolicyESQuery(protectData, suiteData);
		HttpResponse response = createPolicy(restClient, suiteData, payload);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_OK,"Response status doesn't match");
		Logger.info("========================================");
		Logger.info("Create Policy Response: "+ClientUtil.getResponseBody(response));
		Logger.info("========================================");
	}
	
	public Map<String, String> getProtectBlockDetails(Client restClient, SuiteData suiteData, ProtectDTO protectData) throws Exception{
		Map<String, String> policyBlockDetails = new HashMap<String, String>();
		List<NameValuePair> headers = action.getHeaders(suiteData);
		String policyUrl = "/controls/get_blocks";
		URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(), policyUrl);
		HttpResponse policyBlockResponse = restClient.doGet(uri, headers);
		String policyBlockResponseBody = ClientUtil.getResponseBody(policyBlockResponse);
		Logger.info("========================================");
		Logger.info("Response body for Block details: "+policyBlockResponseBody);
		Logger.info("========================================");
		String blocks = ClientUtil.getJSONValue(ClientUtil.getJSONValue(policyBlockResponseBody, "blocks"), "blocks");
		JSONArray blocksListArray = new JSONArray(blocks);
		if(blocksListArray.length()>0){
			for(int i=0;i<blocksListArray.length();i++){
				JSONObject blockObject = blocksListArray.getJSONObject(i);
				String block = blockObject.getString("block");
				String blockedApps = ClientUtil.getJSONValue(block, "blocked_apps");
				if(blockedApps.contains(protectData.getPolicyName())){
					String email = ClientUtil.getJSONValue(block, "email");
					String id = ClientUtil.getJSONValue(block, "id");
					String isBlocked = ClientUtil.getJSONValue(block, "is_blocked");
					Logger.info("Email: "+email);
					Logger.info("Id: "+ id);
					Logger.info("Is Blocked: "+isBlocked);
					Logger.info("========================================");
					policyBlockDetails.put("email", email);
					policyBlockDetails.put("id", id);
					policyBlockDetails.put("is_blocked", isBlocked);
					break;
				}
			}
		}
		return policyBlockDetails;
	}
	
	public void clearBlock(Client restClient, SuiteData suiteData, ProtectDTO protectData, Map<String, String> blockData) throws Exception{
		String payload = "{\"user_id\":\""+blockData.get("id")+"\",\"email\":\""+suiteData.getSaasAppUsername()+"\",\"app_action\":\"UNBLOCK\",\"unblock_app\":\"__ALL_EL__\"}";
		Logger.info("========================================");
		Logger.info("Payload for Clear Block: "+payload);
		Logger.info("========================================");
		StringEntity entity = new StringEntity(payload);
		List<NameValuePair> headers = action.getHeaders(suiteData);
		String policyUrl = "/controls/clear_block";
		URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(), policyUrl);
		HttpResponse clearBlockResponse = restClient.doPost(uri, headers, null, entity);
		String clearBlockResponseBody = ClientUtil.getResponseBody(clearBlockResponse);
		Logger.info("========================================");
		Logger.info("Clear Block Response Body: "+clearBlockResponseBody);
		Logger.info("========================================");
		String clearBlockStatus = ClientUtil.getJSONValue(clearBlockResponseBody, GatewayTestConstants.ACTION_STATUS);
		clearBlockStatus = clearBlockStatus.substring(1, clearBlockStatus.length() - 1);
		Assert.assertEquals(clearBlockStatus, GatewayTestConstants.SUCCESS);
	}
	
	public void deletePolicyList(Client restClient, SuiteData suiteData, List<String> policyList) throws Exception{
		List<NameValuePair> headers = action.getHeaders(suiteData);
		for(String policyName: policyList){
			if(!getPolicyDetails(restClient, suiteData, headers, policyName).isEmpty()){
				deletePolicy(restClient, suiteData, policyName);
			}
		}
	}
}



