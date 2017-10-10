package com.elastica.tests.salesforce;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.elastica.action.protect.ProtectDTO;
import com.elastica.common.GWCommonTest;
import com.elastica.constants.salesforce.SalesforceConstants;
import com.elastica.gateway.GatewayTestConstants;
import com.elastica.listeners.Priority;

public class SalesforceAccessEnforcementPolicyTests extends GWCommonTest{
	Map <String, String> data = new HashMap<String, String>();
	String fromTime=null;
	WebDriver driver = null;
	String browserVersion = null;
	String platform = null;
	String platformVersion = null;
	String contactName = null;
	List<String> policyList = new ArrayList<String>();
	Map<String, String> contactDetails = new HashMap<String, String>();

	@Priority(1)
	@Test(groups ={"Regression", "Sanity"})
	public void performingActivitiesOnSaasAppo365() throws Exception {
		driver = getWebDriver();
		fromTime=backend.getCurrentTime();
		Reporter.log("Started performing activities on saas app", true);
		login.loginCloudSocPortal(driver, suiteData);
		salesforceLogin.login(getWebDriver(), suiteData);
		policyList.add("AEPOLICY_HOMETAB_FILE_UPLOAD_BLOCK_SALESFORCE");
		policyList.add("AEPOLICY_HOMETAB_FILE_SHARE_BLOCK_SALESFORCE");
		policyList.add("AEPOLICY_HOMETAB_FILE_DOWNLOAD_BLOCK_SALESFORCE");
		policyList.add("AEPOLICY_CHARTERTAB_FILE_UPLOAD_BLOCK_SALESFORCE");
		policyList.add("AEPOLICY_CHATTERTAB_FILE_SHARE_BLOCK_SALESFORCE");
		policyList.add("AEPOLICY_CHATTERTAB_FILE_DOWNLOAD_BLOCK_SALESFORCE");
		//policyList.add("POLICY_ACCOUNT_CREATE_BLOCK_SALESFORCE");
		//policyList.add("POLICY_ACCOUNT_EDIT_BLOCK_SALESFORCE");
		//policyList.add("POLICY_ACCOUNT_VIEW_BLOCK_SALESFORCE");
		//policyList.add("POLICY_ACCOUNT_DELETE_BLOCK_SALESFORCE");
		//policyList.add("POLICY_CONTACT_CREATE_BLOCK_SALESFORCE");
		//policyList.add("POLICY_CONTACT_EDIT_BLOCK_SALESFORCE");
		//policyList.add("POLICY_CONTACT_VIEW_BLOCK_SALESFORCE");
		//policyList.add("POLICY_CONTACT_DELETE_BLOCK_SALESFORCE");
		Capabilities capabilities = ((RemoteWebDriver)getWebDriver()).getCapabilities();
		browserVersion = capabilities.getVersion();
		//browserVersion = browserVersion.substring(0, browserVersion.lastIndexOf("."));
		if(System.getProperty("os.name").contains("Mac")){
			platform = "Mac OS X";
			platformVersion = "10.10";
		} else if(System.getProperty("os.name").contains("Window")){
			platform = "Windows";
			platformVersion = "8.1";
		}
		//contact Details
		contactDetails.put(SalesforceConstants.SALUTATION, "Mr.");
		contactDetails.put(SalesforceConstants.FIRST_NAME, "FN");
		contactDetails.put(SalesforceConstants.MIDDLE_NAME, "MN");
		contactDetails.put(SalesforceConstants.LAST_NAME, "LN");
		contactDetails.put(SalesforceConstants.EMAIL, "fn.ln@email.com");
		contactDetails.put(SalesforceConstants.PHONE, "1553287340");
		contactDetails.put(SalesforceConstants.TITLE, "Title");
		contactDetails.put(SalesforceConstants.MOBILE, "9834512388");
		Reporter.log("Finished login activities on cloudSoc", true);
	}
	
	@Test(groups ={"Regression"}, priority=2)
	public void testBlockUploadFileHomeTabPolicy() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		protect.deletePolicyList(client, suiteData, policyList);
		ProtectDTO protectData = new ProtectDTO();
		String policyName = "AEPOLICY_HOMETAB_FILE_UPLOAD_BLOCK_SALESFORCE";
		List<String> appList = new ArrayList<String>();
		List<String> userList = new ArrayList<String>();
		List<String> actionList = new ArrayList<String>();
		appList.add(suiteData.getSaasAppName());
		userList.add(suiteData.getSaasAppUsername());
		actionList.add("BLOCK_SHARE");
		protectData.setApplicationList(appList);
		protectData.setUserList(userList);
		protectData.setActionList(actionList);
		protectData.setPolicyName(policyName);
		protectData.setStatus(true);
		protect.createAccessEnforcementPolicy(client, suiteData, protectData, "File:Upload");
		
		//Upload file and verify
		String filename = salesforceHomeAction.getTimestamp();
		File file = salesforceHomeAction.createFile(filename, "txt");
		salesforceHomeAction.gotoHomeTab(driver);
		salesforceHomeAction.uploadFilesForHomeChatter(driver, file);
		salesforceHomeAction.shareFileForHomeChatter(driver);
		Thread.sleep(10000);
		boolean flag = clickOkInPopup();
		Assert.assertTrue(flag, "Popup not appeared");
		protect.deletePolicy(client, suiteData, policyName);
		salesforceHomeAction.gotoHomeTab(driver);
		salesforceHomeAction.uploadFilesForHomeChatter(driver, file);
		salesforceHomeAction.shareFileForHomeChatter(driver);
		Thread.sleep(5000);
		
		//verification
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "[ALERT] " + suiteData.getTestUsername() + 
				" attempted Activity: Upload on Object type: File name: " + file.getName() +" using Platform: "+ platform+ ", Version: "
				+platformVersion+" and Browser : "+suiteData.getBrowser()+" and Version : "+browserVersion +" violating policy:"+policyName);
		expectedDataMap.put(GatewayTestConstants.POLICY_TYPE, "AccessEnforcement");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, protectData.getSeverity());
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Policy Violation");
		expectedDataMap.put(GatewayTestConstants.ACTION_TAKEN, "block,");
		expectedDataMap.put(GatewayTestConstants.POLICY_ACTION, GatewayTestConstants.POLICY_ACTION_ALERT);
		expectedDataMap.put(GatewayTestConstants.POLICY_VIOLATED, policyName);
		expectedDataMap.put(GatewayTestConstants.FACILITY, suiteData.getSaasAppName());
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		
		expectedDataMap.clear();
		String uploadFile = "User uploaded file named "+file.getName()+".";
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Upload");
		expectedDataMap.put(GatewayTestConstants.FACILITY, suiteData.getSaasAppName());
		expectedDataMap.put(GatewayTestConstants.MESSAGE, uploadFile);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	@Test(groups ={"Regression"}, priority=3)
	public void testBlockShareFileHomeTabPolicy() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		protect.deletePolicyList(client, suiteData, policyList);
		ProtectDTO protectData = new ProtectDTO();
		String policyName = "AEPOLICY_HOMETAB_FILE_SHARE_BLOCK_SALESFORCE";
		List<String> appList = new ArrayList<String>();
		List<String> userList = new ArrayList<String>();
		List<String> actionList = new ArrayList<String>();
		appList.add(suiteData.getSaasAppName());
		userList.add(suiteData.getSaasAppUsername());
		actionList.add("BLOCK_SHARE");
		protectData.setApplicationList(appList);
		protectData.setUserList(userList);
		protectData.setActionList(actionList);
		protectData.setPolicyName(policyName);
		protectData.setStatus(true);
		protect.createAccessEnforcementPolicy(client, suiteData, protectData, "File:Share");
		
		//Share file and verify the block
		String filename = salesforceHomeAction.getTimestamp();
		File file = salesforceHomeAction.createFile(filename, "txt");
		salesforceHomeAction.gotoHomeTab(driver);
		salesforceHomeAction.uploadFilesForHomeChatter(driver, file);
		salesforceHomeAction.shareFileForHomeChatter(driver);
		boolean flag = clickOkInPopup();
		Assert.assertTrue(flag, "Popup not appeared");
		protect.deletePolicy(client, suiteData, policyName);
		salesforceHomeAction.gotoHomeTab(driver);
		salesforceHomeAction.uploadFilesForHomeChatter(driver, file);
		salesforceHomeAction.shareFileForHomeChatter(driver);
		
		//verification
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "[ALERT] " + suiteData.getTestUsername() + 
				" attempted Activity: Share on Object type: File name: " + file.getName() +" using Platform: "+ platform+ ", Version: "
				+platformVersion+" and Browser : "+suiteData.getBrowser()+" and Version : "+browserVersion +" violating policy:"+policyName);
		expectedDataMap.put(GatewayTestConstants.POLICY_TYPE, "AccessEnforcement");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, protectData.getSeverity());
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Policy Violation");
		expectedDataMap.put(GatewayTestConstants.ACTION_TAKEN, "block,");
		expectedDataMap.put(GatewayTestConstants.POLICY_ACTION, GatewayTestConstants.POLICY_ACTION_ALERT);
		expectedDataMap.put(GatewayTestConstants.POLICY_VIOLATED, policyName);
		expectedDataMap.put(GatewayTestConstants.FACILITY, suiteData.getSaasAppName());
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		
		expectedDataMap.clear();
		String ShareFile = "User shared file named "+file.getName();
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Share");
		expectedDataMap.put(GatewayTestConstants.FACILITY, suiteData.getSaasAppName());
		expectedDataMap.put(GatewayTestConstants.MESSAGE, ShareFile);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	@Test(groups ={"Regression"}, priority=4)
	public void testBlockDownloadFileHomeTabPolicy() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		protect.deletePolicyList(client, suiteData, policyList);
		ProtectDTO protectData = new ProtectDTO();
		String policyName = "AEPOLICY_HOMETAB_FILE_DOWNLOAD_BLOCK_SALESFORCE";
		List<String> appList = new ArrayList<String>();
		List<String> userList = new ArrayList<String>();
		List<String> actionList = new ArrayList<String>();
		appList.add(suiteData.getSaasAppName());
		userList.add(suiteData.getSaasAppUsername());
		actionList.add("BLOCK_SHARE");
		protectData.setApplicationList(appList);
		protectData.setUserList(userList);
		protectData.setActionList(actionList);
		protectData.setPolicyName(policyName);
		protectData.setStatus(true);
		protect.createAccessEnforcementPolicy(client, suiteData, protectData, "File:Download");
		
		//Download File and verify the block
		String filename = salesforceHomeAction.getTimestamp();
		File file = salesforceHomeAction.createFile(filename, "txt");
		salesforceHomeAction.gotoHomeTab(driver);
		salesforceHomeAction.uploadFilesForHomeChatter(driver, file);
		salesforceHomeAction.shareFileForHomeChatter(driver);
		salesforceHomeAction.gotoHomeTab(driver);
		salesforceHomeAction.downloadFileFromHomeChatterTab(driver, filename);
		boolean flag = clickOkInPopup();
		Assert.assertTrue(flag, "Popup not appeared");
		protect.deletePolicy(client, suiteData, policyName);
		driver.get(suiteData.getSaasAppBaseUrl());
		salesforceHomeAction.gotoHomeTab(driver);
		salesforceHomeAction.downloadFileFromHomeChatterTab(driver, filename);
		
		//verification
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "[ALERT] " + suiteData.getTestUsername() + 
				" attempted Activity: Download on Object type: File name: " + file.getName() +" using Platform: "+ platform+ ", Version: "
				+platformVersion+" and Browser : "+suiteData.getBrowser()+" and Version : "+browserVersion +" violating policy:"+policyName);
		expectedDataMap.put(GatewayTestConstants.POLICY_TYPE, "AccessEnforcement");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, protectData.getSeverity());
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Policy Violation");
		expectedDataMap.put(GatewayTestConstants.ACTION_TAKEN, "block,");
		expectedDataMap.put(GatewayTestConstants.POLICY_ACTION, GatewayTestConstants.POLICY_ACTION_ALERT);
		expectedDataMap.put(GatewayTestConstants.POLICY_VIOLATED, policyName);
		expectedDataMap.put(GatewayTestConstants.FACILITY, suiteData.getSaasAppName());
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		
		expectedDataMap.clear();
		String downloadFile = "User downloaded a file named "+filename;
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Download");
		expectedDataMap.put(GatewayTestConstants.FACILITY, suiteData.getSaasAppName());
		expectedDataMap.put(GatewayTestConstants.MESSAGE, downloadFile);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	@Test(groups ={"Regression"}, priority=5)
	public void testBlockUploadFileChatterTabPolicy() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		protect.deletePolicyList(client, suiteData, policyList);
		ProtectDTO protectData = new ProtectDTO();
		String policyName = "AEPOLICY_CHATTERTAB_FILE_UPLOAD_BLOCK_SALESFORCE";
		List<String> appList = new ArrayList<String>();
		List<String> userList = new ArrayList<String>();
		List<String> actionList = new ArrayList<String>();
		appList.add(suiteData.getSaasAppName());
		userList.add(suiteData.getSaasAppUsername());
		actionList.add("BLOCK_SHARE");
		protectData.setApplicationList(appList);
		protectData.setUserList(userList);
		protectData.setActionList(actionList);
		protectData.setPolicyName(policyName);
		protectData.setStatus(true);
		protect.createAccessEnforcementPolicy(client, suiteData, protectData, "File:Upload");
		
		//Upload file and verify
		String filename = salesforceHomeAction.getTimestamp();
		File file = salesforceHomeAction.createFile(filename, "txt");
		salesforceHomeAction.gotoChatterTab(driver);;
		salesforceHomeAction.uploadFilesForHomeChatter(driver, file);
		salesforceHomeAction.shareFileForHomeChatter(driver);
		Thread.sleep(10000);
		boolean flag = clickOkInPopup();
		Assert.assertTrue(flag, "Popup not appeared");
		protect.deletePolicy(client, suiteData, policyName);
		salesforceHomeAction.gotoChatterTab(driver);
		salesforceHomeAction.uploadFilesForHomeChatter(driver, file);
		salesforceHomeAction.shareFileForHomeChatter(driver);
		Thread.sleep(5000);
		
		//verification
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "[ALERT] " + suiteData.getTestUsername() + 
				" attempted Activity: Upload on Object type: File name: " + file.getName() +" using Platform: "+ platform+ ", Version: "
				+platformVersion+" and Browser : "+suiteData.getBrowser()+" and Version : "+browserVersion +" violating policy:"+policyName);
		expectedDataMap.put(GatewayTestConstants.POLICY_TYPE, "AccessEnforcement");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, protectData.getSeverity());
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Policy Violation");
		expectedDataMap.put(GatewayTestConstants.ACTION_TAKEN, "block,");
		expectedDataMap.put(GatewayTestConstants.POLICY_ACTION, GatewayTestConstants.POLICY_ACTION_ALERT);
		expectedDataMap.put(GatewayTestConstants.POLICY_VIOLATED, policyName);
		expectedDataMap.put(GatewayTestConstants.FACILITY, suiteData.getSaasAppName());
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		
		expectedDataMap.clear();
		String uploadFile = "User uploaded file named "+file.getName()+".";
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Upload");
		expectedDataMap.put(GatewayTestConstants.FACILITY, suiteData.getSaasAppName());
		expectedDataMap.put(GatewayTestConstants.MESSAGE, uploadFile);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	@Test(groups ={"Regression"}, priority=6)
	public void testBlockShareFileChatterTabPolicy() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		protect.deletePolicyList(client, suiteData, policyList);
		ProtectDTO protectData = new ProtectDTO();
		String policyName = "AEPOLICY_CHATTERTAB_FILE_SHARE_BLOCK_SALESFORCE";
		List<String> appList = new ArrayList<String>();
		List<String> userList = new ArrayList<String>();
		List<String> actionList = new ArrayList<String>();
		appList.add(suiteData.getSaasAppName());
		userList.add(suiteData.getSaasAppUsername());
		actionList.add("BLOCK_SHARE");
		protectData.setApplicationList(appList);
		protectData.setUserList(userList);
		protectData.setActionList(actionList);
		protectData.setPolicyName(policyName);
		protectData.setStatus(true);
		protect.createAccessEnforcementPolicy(client, suiteData, protectData, "File:Share");
		
		//Share file and verify the block
		String filename = salesforceHomeAction.getTimestamp();
		File file = salesforceHomeAction.createFile(filename, "txt");
		salesforceHomeAction.gotoChatterTab(driver);
		salesforceHomeAction.uploadFilesForHomeChatter(driver, file);
		salesforceHomeAction.shareFileForHomeChatter(driver);
		boolean flag = clickOkInPopup();
		Assert.assertTrue(flag, "Popup not appeared");
		protect.deletePolicy(client, suiteData, policyName);
		salesforceHomeAction.gotoChatterTab(driver);
		salesforceHomeAction.uploadFilesForHomeChatter(driver, file);
		salesforceHomeAction.shareFileForHomeChatter(driver);
		
		//verification
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "[ALERT] " + suiteData.getTestUsername() + 
				" attempted Activity: Share on Object type: File name: " + file.getName() +" using Platform: "+ platform+ ", Version: "
				+platformVersion+" and Browser : "+suiteData.getBrowser()+" and Version : "+browserVersion +" violating policy:"+policyName);
		expectedDataMap.put(GatewayTestConstants.POLICY_TYPE, "AccessEnforcement");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, protectData.getSeverity());
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Policy Violation");
		expectedDataMap.put(GatewayTestConstants.ACTION_TAKEN, "block,");
		expectedDataMap.put(GatewayTestConstants.POLICY_ACTION, GatewayTestConstants.POLICY_ACTION_ALERT);
		expectedDataMap.put(GatewayTestConstants.POLICY_VIOLATED, policyName);
		expectedDataMap.put(GatewayTestConstants.FACILITY, suiteData.getSaasAppName());
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		
		expectedDataMap.clear();
		String ShareFile = "User shared file named "+file.getName();
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Share");
		expectedDataMap.put(GatewayTestConstants.FACILITY, suiteData.getSaasAppName());
		expectedDataMap.put(GatewayTestConstants.MESSAGE, ShareFile);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	@Test(groups ={"Regression"}, priority=7)
	public void testBlockDownloadFileChatterTabPolicy() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		protect.deletePolicyList(client, suiteData, policyList);
		ProtectDTO protectData = new ProtectDTO();
		String policyName = "AEPOLICY_CHATTERTAB_FILE_DOWNLOAD_BLOCK_SALESFORCE";
		List<String> appList = new ArrayList<String>();
		List<String> userList = new ArrayList<String>();
		List<String> actionList = new ArrayList<String>();
		appList.add(suiteData.getSaasAppName());
		userList.add(suiteData.getSaasAppUsername());
		actionList.add("BLOCK_SHARE");
		protectData.setApplicationList(appList);
		protectData.setUserList(userList);
		protectData.setActionList(actionList);
		protectData.setPolicyName(policyName);
		protectData.setStatus(true);
		protect.createAccessEnforcementPolicy(client, suiteData, protectData, "File:Download");
		
		//Download File and verify the block
		String filename = salesforceHomeAction.getTimestamp();
		File file = salesforceHomeAction.createFile(filename, "txt");
		salesforceHomeAction.gotoChatterTab(driver);
		salesforceHomeAction.uploadFilesForHomeChatter(driver, file);
		salesforceHomeAction.shareFileForHomeChatter(driver);
		salesforceHomeAction.gotoChatterTab(driver);
		salesforceHomeAction.downloadFileFromHomeChatterTab(driver, filename);
		boolean flag = clickOkInPopup();
		Assert.assertTrue(flag, "Popup not appeared");
		protect.deletePolicy(client, suiteData, policyName);
		driver.get(suiteData.getSaasAppBaseUrl());
		salesforceHomeAction.gotoChatterTab(driver);
		salesforceHomeAction.downloadFileFromHomeChatterTab(driver, filename);
		
		//verification
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "[ALERT] " + suiteData.getTestUsername() + 
				" attempted Activity: Download on Object type: File name: " + file.getName() +" using Platform: "+ platform+ ", Version: "
				+platformVersion+" and Browser : "+suiteData.getBrowser()+" and Version : "+browserVersion +" violating policy:"+policyName);
		expectedDataMap.put(GatewayTestConstants.POLICY_TYPE, "AccessEnforcement");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, protectData.getSeverity());
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Policy Violation");
		expectedDataMap.put(GatewayTestConstants.ACTION_TAKEN, "block,");
		expectedDataMap.put(GatewayTestConstants.POLICY_ACTION, GatewayTestConstants.POLICY_ACTION_ALERT);
		expectedDataMap.put(GatewayTestConstants.POLICY_VIOLATED, policyName);
		expectedDataMap.put(GatewayTestConstants.FACILITY, suiteData.getSaasAppName());
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		
		expectedDataMap.clear();
		String downloadFile = "User downloaded a file named "+filename;
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Download");
		expectedDataMap.put(GatewayTestConstants.FACILITY, suiteData.getSaasAppName());
		expectedDataMap.put(GatewayTestConstants.MESSAGE, downloadFile);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	//@Test(groups ={"Regression"}, priority=2)
	public void testBlockCreateAccountPolicy() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		protect.deletePolicyList(client, suiteData, policyList);
		ProtectDTO protectData = new ProtectDTO();
		String policyName = "POLICY_ACCOUNT_CREATE_BLOCK_SALESFORCE";
		List<String> appList = new ArrayList<String>();
		List<String> userList = new ArrayList<String>();
		List<String> actionList = new ArrayList<String>();
		appList.add(suiteData.getSaasAppName());
		userList.add(suiteData.getSaasAppUsername());
		actionList.add("BLOCK_SHARE");
		protectData.setApplicationList(appList);
		protectData.setUserList(userList);
		protectData.setActionList(actionList);
		protectData.setPolicyName(policyName);
		protectData.setStatus(true);
		protect.createAccessEnforcementPolicy(client, suiteData, protectData, "Account:Create");
		
		fromTime=backend.getCurrentTime();
		String accountName = "Account"+salesforceHomeAction.getTimestamp();
		Map<String, String> accountDetails = new HashMap<String, String>();
		accountDetails.put(SalesforceConstants.ACCOUNT_NAME, accountName);
		accountDetails.put(SalesforceConstants.ACCOUNT_DESCRIPTION, SalesforceConstants.ACCOUNT_DESCRIPTION);
		accountDetails.put(SalesforceConstants.WEBSITE, SalesforceConstants.WEBSITE_NAME);
		salesforceHomeAction.createAccountOnAccountsPage(driver, accountDetails);
		boolean flag = clickOkInPopup();
		Assert.assertTrue(flag, "Popup not appeared");
		protect.deletePolicy(client, suiteData, policyName);
		Thread.sleep(5000);
		driver.get(suiteData.getSaasAppBaseUrl());
		salesforceHomeAction.createAccountOnAccountsPage(driver, accountDetails);
		Thread.sleep(5000);
		salesforceHomeAction.deleteAccountInSalesForce(driver, accountName);
		
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "[ALERT] " + suiteData.getTestUsername() + 
				" attempted Activity: Create on Object type: Account name: " + accountName +" using Platform: "+ platform+ ", Version: "
				+platformVersion+" and Browser : "+suiteData.getBrowser()+" and Version : "+browserVersion +" violating policy:"+policyName);
		expectedDataMap.put(GatewayTestConstants.POLICY_TYPE, "AccessEnforcement");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, protectData.getSeverity());
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Policy Violation");
		expectedDataMap.put(GatewayTestConstants.ACTION_TAKEN, "block,");
		expectedDataMap.put(GatewayTestConstants.POLICY_ACTION, GatewayTestConstants.POLICY_ACTION_ALERT);
		expectedDataMap.put(GatewayTestConstants.POLICY_VIOLATED, policyName);
		expectedDataMap.put(GatewayTestConstants.FACILITY, suiteData.getSaasAppName());
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		
		expectedDataMap.clear();
		String createMessage = "User created a new account with name "+accountName;
		expectedDataMap.put(GatewayTestConstants.MESSAGE, createMessage);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Account");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Create");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	//@Test(groups ={"Regression"}, priority=3)
	public void testBlockEditAccountPolicy() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		protect.deletePolicyList(client, suiteData, policyList);
		ProtectDTO protectData = new ProtectDTO();
		String policyName = "POLICY_ACCOUNT_EDIT_BLOCK_SALESFORCE";
		List<String> appList = new ArrayList<String>();
		List<String> userList = new ArrayList<String>();
		List<String> actionList = new ArrayList<String>();
		appList.add(suiteData.getSaasAppName());
		userList.add(suiteData.getSaasAppUsername());
		actionList.add("BLOCK_SHARE");
		protectData.setApplicationList(appList);
		protectData.setUserList(userList);
		protectData.setActionList(actionList);
		protectData.setPolicyName(policyName);
		protectData.setStatus(true);
		protect.createAccessEnforcementPolicy(client, suiteData, protectData, "Account:Edit");
		
		fromTime=backend.getCurrentTime();
		String accountName = "Account"+salesforceHomeAction.getTimestamp();
		Map<String, String> accountDetails = new HashMap<String, String>();
		accountDetails.put(SalesforceConstants.ACCOUNT_NAME, accountName);
		accountDetails.put(SalesforceConstants.ACCOUNT_DESCRIPTION, SalesforceConstants.ACCOUNT_DESCRIPTION);
		accountDetails.put(SalesforceConstants.WEBSITE, SalesforceConstants.WEBSITE_NAME);
		salesforceHomeAction.createAccountOnAccountsPage(driver, accountDetails);
		salesforceHomeAction.editAccountInSalesforce(driver, accountName, SalesforceConstants.ACCOUNT_NEW_DESCRIPTION);
		boolean flag = clickOkInPopup();
		Assert.assertTrue(flag, "Popup not appeared");
		protect.deletePolicy(client, suiteData, policyName);
		driver.get(suiteData.getSaasAppBaseUrl());
		Thread.sleep(5000);
		salesforceHomeAction.editAccountInSalesforce(driver, accountName, SalesforceConstants.ACCOUNT_NEW_DESCRIPTION);
		Thread.sleep(5000);
		salesforceHomeAction.deleteAccountInSalesForce(driver, accountName);
		
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "[ALERT] " + suiteData.getTestUsername() + 
				" attempted Activity: Edit on Object type: Account name: " + accountName +" using Platform: "+ platform+ ", Version: "
				+platformVersion+" and Browser : "+suiteData.getBrowser()+" and Version : "+browserVersion +" violating policy:"+policyName);
		expectedDataMap.put(GatewayTestConstants.POLICY_TYPE, "AccessEnforcement");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, protectData.getSeverity());
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Policy Violation");
		expectedDataMap.put(GatewayTestConstants.ACTION_TAKEN, "block,");
		expectedDataMap.put(GatewayTestConstants.POLICY_ACTION, GatewayTestConstants.POLICY_ACTION_ALERT);
		expectedDataMap.put(GatewayTestConstants.POLICY_VIOLATED, policyName);
		expectedDataMap.put(GatewayTestConstants.FACILITY, suiteData.getSaasAppName());
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		
		expectedDataMap.clear();
		String editMessage = "User edited an account named "+accountName;
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Account");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Edit");
		expectedDataMap.put(GatewayTestConstants.FACILITY, suiteData.getSaasAppName());
		expectedDataMap.put(GatewayTestConstants.MESSAGE, editMessage);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	//@Test(groups ={"Regression"}, priority=4)
	public void testBlockDeleteAccountPolicy() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		protect.deletePolicyList(client, suiteData, policyList);
		ProtectDTO protectData = new ProtectDTO();
		String policyName = "POLICY_ACCOUNT_DELETE_BLOCK_SALESFORCE";
		List<String> appList = new ArrayList<String>();
		List<String> userList = new ArrayList<String>();
		List<String> actionList = new ArrayList<String>();
		appList.add(suiteData.getSaasAppName());
		userList.add(suiteData.getSaasAppUsername());
		actionList.add("BLOCK_SHARE");
		protectData.setApplicationList(appList);
		protectData.setUserList(userList);
		protectData.setActionList(actionList);
		protectData.setPolicyName(policyName);
		protectData.setStatus(true);
		protect.createAccessEnforcementPolicy(client, suiteData, protectData, "Account:Delete");
		
		fromTime=backend.getCurrentTime();
		String accountName = "Account"+salesforceHomeAction.getTimestamp();
		Map<String, String> accountDetails = new HashMap<String, String>();
		accountDetails.put(SalesforceConstants.ACCOUNT_NAME, accountName);
		accountDetails.put(SalesforceConstants.ACCOUNT_DESCRIPTION, SalesforceConstants.ACCOUNT_DESCRIPTION);
		accountDetails.put(SalesforceConstants.WEBSITE, SalesforceConstants.WEBSITE_NAME);
		salesforceHomeAction.createAccountOnAccountsPage(driver, accountDetails);
		salesforceHomeAction.deleteAccountInSalesForce(driver, accountName);
		Thread.sleep(5000);
		boolean flag = clickOkInPopup();
		Assert.assertTrue(flag, "Popup not appeared");
		protect.deletePolicy(client, suiteData, policyName);
		driver.get(suiteData.getSaasAppBaseUrl());
		Thread.sleep(5000);
		salesforceHomeAction.deleteAccountInSalesForce(driver, accountName);
		
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "[ALERT] " + suiteData.getTestUsername() + 
				" attempted Activity: Delete on Object type: Account name: " + accountName +" using Platform: "+ platform+ ", Version: "
				+platformVersion+" and Browser : "+suiteData.getBrowser()+" and Version : "+browserVersion +" violating policy:"+policyName);
		expectedDataMap.put(GatewayTestConstants.POLICY_TYPE, "AccessEnforcement");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, protectData.getSeverity());
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Policy Violation");
		expectedDataMap.put(GatewayTestConstants.ACTION_TAKEN, "block,");
		expectedDataMap.put(GatewayTestConstants.POLICY_ACTION, GatewayTestConstants.POLICY_ACTION_ALERT);
		expectedDataMap.put(GatewayTestConstants.POLICY_VIOLATED, policyName);
		expectedDataMap.put(GatewayTestConstants.FACILITY, suiteData.getSaasAppName());
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		
		expectedDataMap.clear();
		String deleteMessage = "User deleted the account "+accountName;
		expectedDataMap.put(GatewayTestConstants.MESSAGE, deleteMessage);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Account");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Delete");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	//@Test(groups ={"Regression"}, priority=5)
	public void testBlockViewAccountPolicy() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		protect.deletePolicyList(client, suiteData, policyList);
		ProtectDTO protectData = new ProtectDTO();
		String policyName = "POLICY_ACCOUNT_VIEW_BLOCK_SALESFORCE";
		List<String> appList = new ArrayList<String>();
		List<String> userList = new ArrayList<String>();
		List<String> actionList = new ArrayList<String>();
		appList.add(suiteData.getSaasAppName());
		userList.add(suiteData.getSaasAppUsername());
		actionList.add("BLOCK_SHARE");
		protectData.setApplicationList(appList);
		protectData.setUserList(userList);
		protectData.setActionList(actionList);
		protectData.setPolicyName(policyName);
		protectData.setStatus(true);
		protect.createAccessEnforcementPolicy(client, suiteData, protectData, "Account:View");
		
		fromTime=backend.getCurrentTime();
		String accountName = "Account"+salesforceHomeAction.getTimestamp();
		Map<String, String> accountDetails = new HashMap<String, String>();
		accountDetails.put(SalesforceConstants.ACCOUNT_NAME, accountName);
		accountDetails.put(SalesforceConstants.ACCOUNT_DESCRIPTION, SalesforceConstants.ACCOUNT_DESCRIPTION);
		accountDetails.put(SalesforceConstants.WEBSITE, SalesforceConstants.WEBSITE_NAME);
		salesforceHomeAction.gotoAccountsTab(driver);
		Thread.sleep(5000);
		boolean flag = clickOkInPopup();
		Assert.assertTrue(flag, "Popup not appeared");
		protect.deletePolicy(client, suiteData, policyName);
		Thread.sleep(5000);
		salesforceHomeAction.gotoAccountsTab(driver);
		
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "[ALERT] " + suiteData.getTestUsername() + 
				" attempted Activity: View on Object type: Account name: " + accountName +" using Platform: "+ platform+ ", Version: "
				+platformVersion+" and Browser : "+suiteData.getBrowser()+" and Version : "+browserVersion +" violating policy:"+policyName);
		expectedDataMap.put(GatewayTestConstants.POLICY_TYPE, "AccessEnforcement");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, protectData.getSeverity());
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Policy Violation");
		expectedDataMap.put(GatewayTestConstants.ACTION_TAKEN, "block,");
		expectedDataMap.put(GatewayTestConstants.POLICY_ACTION, GatewayTestConstants.POLICY_ACTION_ALERT);
		expectedDataMap.put(GatewayTestConstants.POLICY_VIOLATED, policyName);
		expectedDataMap.put(GatewayTestConstants.FACILITY, suiteData.getSaasAppName());
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	//@Test(groups ={"Regression"}, priority=6)
	public void testBlockCreateContactPolicy() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		protect.deletePolicyList(client, suiteData, policyList);
		ProtectDTO protectData = new ProtectDTO();
		String policyName = "POLICY_CONTACT_VIEW_BLOCK_SALESFORCE";
		List<String> appList = new ArrayList<String>();
		List<String> userList = new ArrayList<String>();
		List<String> actionList = new ArrayList<String>();
		appList.add(suiteData.getSaasAppName());
		userList.add(suiteData.getSaasAppUsername());
		actionList.add("BLOCK_SHARE");
		protectData.setApplicationList(appList);
		protectData.setUserList(userList);
		protectData.setActionList(actionList);
		protectData.setPolicyName(policyName);
		protectData.setStatus(true);
		protect.createAccessEnforcementPolicy(client, suiteData, protectData, "Contact:Create");
		
		fromTime=backend.getCurrentTime();
		String timeStamp = salesforceHomeAction.getTimestamp();
		String accountName = "Account"+timeStamp;
		Map<String, String> accountDetails = new HashMap<String, String>();
		accountDetails.put(SalesforceConstants.ACCOUNT_NAME, accountName);
		accountDetails.put(SalesforceConstants.ACCOUNT_DESCRIPTION, SalesforceConstants.ACCOUNT_DESCRIPTION);
		accountDetails.put(SalesforceConstants.WEBSITE, SalesforceConstants.WEBSITE_NAME);
		salesforceHomeAction.createAccountOnAccountsPage(driver, accountDetails);
		contactDetails.put(SalesforceConstants.ACCOUNT_NAME, accountName);
		contactDetails.put(SalesforceConstants.SUFFIX, timeStamp);
		salesforceHomeAction.editAndCreateContact(driver, contactDetails, "no");
		boolean flag = clickOkInPopup();
		Assert.assertTrue(flag, "Popup not appeared");
		protect.deletePolicy(client, suiteData, policyName);
		driver.navigate().refresh();
		salesforceHomeAction.editAndCreateContact(driver, contactDetails, "no");
		salesforceHomeAction.deleteContact(driver, contactDetails);
		salesforceHomeAction.deleteAccountInSalesForce(driver, accountName);
		
		expectedDataMap.clear();
		contactName = contactDetails.get(SalesforceConstants.FIRST_NAME)+" "+contactDetails.get(SalesforceConstants.LAST_NAME);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "[ALERT] " + suiteData.getTestUsername() + 
				" attempted Activity: Create on Object type: Contact name: " + contactName +" using Platform: "+ platform+ ", Version: "
				+platformVersion+" and Browser : "+suiteData.getBrowser()+" and Version : "+browserVersion +" violating policy:"+policyName);
		expectedDataMap.put(GatewayTestConstants.POLICY_TYPE, "AccessEnforcement");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, protectData.getSeverity());
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Policy Violation");
		expectedDataMap.put(GatewayTestConstants.ACTION_TAKEN, "block,");
		expectedDataMap.put(GatewayTestConstants.POLICY_ACTION, GatewayTestConstants.POLICY_ACTION_ALERT);
		expectedDataMap.put(GatewayTestConstants.POLICY_VIOLATED, policyName);
		expectedDataMap.put(GatewayTestConstants.FACILITY, suiteData.getSaasAppName());
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		expectedDataMap.clear();
		String createContact = "User created a new contact with name "+contactName;
		expectedDataMap.put(GatewayTestConstants.MESSAGE, createContact);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Contact");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Create");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	@Test(groups ={"Regression", "Sanity"}, priority=136)
	public void salesforces_Test_TestLogout() throws Exception {
		data.clear();
		data.put("message", "User logged out");
		salesforceLogin.logout(getWebDriver(), suiteData);
		protect.deletePolicyList(client, suiteData, policyList);
		//backend.assertAndValidateLog(client, suiteData, fromTime, data);
	}
}