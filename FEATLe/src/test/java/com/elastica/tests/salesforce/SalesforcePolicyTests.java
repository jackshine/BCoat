package com.elastica.tests.salesforce;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.elastica.action.protect.ProtectDTO;
import com.elastica.common.GWCommonTest;
import com.elastica.gateway.GatewayTestConstants;
import com.elastica.listeners.Priority;

public class SalesforcePolicyTests extends GWCommonTest{
	Map <String, String> data = new HashMap<String, String>();
	String fromTime=null;
	WebDriver driver = null;
	List<String> policyList = new ArrayList<String>();

	@Priority(1)
	@Test(groups ={"Regression", "Sanity"})
	public void performingActivitiesOnSaasAppo365() throws Exception {
		driver = getWebDriver();
		fromTime=backend.getCurrentTime();
		Reporter.log("Started performing activities on saas app", true);
		login.loginCloudSocPortal(driver, suiteData);
		salesforceLogin.login(getWebDriver(), suiteData);
		policyList.add("POLICY_FILETAB_UPLOAD_BLOCK_SALESFORCE");
		policyList.add("POLICY_HOMETAB_UPLOAD_BLOCK_SALESFORCE");
		policyList.add("POLICY_CHATTERTAB_UPLOAD_BLOCK_SALESFORCE");
		policyList.add("POLICY_HOMETAB_PUBLIC_SHARING_BLOCK_SALESFORCE");
		policyList.add("POLICY_CHATTERTAB_PUBLIC_SHARING_BLOCK_SALESFORCE");
		policyList.add("POLICY_FILETAB_DOWNLOAD_BLOCK_SALESFORCE");
		policyList.add("POLICY_HOMETAB_DOWNLOAD_BLOCK_SALESFORCE");
		policyList.add("POLICY_CHATTERTAB_DOWNLOAD_BLOCK_SALESFORCE");
		policyList.add("POLICY_ENCRYPTION_SALESFORCE");
		policyList.add("POLICY_DECRYPTION_SALESFORCE");
		policyList.add("POLICY_BLOCK_ENCRYPTION_SALESFORCE");
		Reporter.log("Finished login activities on cloudSoc", true);
	}
	
	@Test(groups ={"Regression"}, priority=2)
	public void testFilesTabBlockUploadPolicy() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		protect.deletePolicyList(client, suiteData, policyList);
		ProtectDTO protectData = new ProtectDTO();
		String policyName = "POLICY_FILETAB_UPLOAD_BLOCK_SALESFORCE";
		List<String> appList = new ArrayList<String>();
		List<String> userList = new ArrayList<String>();
		List<String> transferType = new ArrayList<String>();
		List<String> actionList = new ArrayList<String>();
		appList.add(suiteData.getSaasAppName());
		userList.add(suiteData.getSaasAppUsername());
		transferType.add("Upload");
		actionList.add("BLOCK_SHARE");
		protectData.setApplicationList(appList);
		protectData.setUserList(userList);
		protectData.setTransferType(transferType);
		protectData.setActionList(actionList);
		protectData.setPolicyName(policyName);
		protectData.setStatus(true);
		protect.createFileTransferPolicy(client, suiteData, protectData);
		
		//Upload file and verify the block
		String filename = salesforceHomeAction.getTimestamp();
		File file = salesforceHomeAction.createFile(filename, "txt");
		salesforceHomeAction.gotoFilesTab(driver);
		salesforceHomeAction.uploadFileFromFilesTab(driver, file, suiteData);
		protect.deletePolicy(client, suiteData, policyName);
		salesforceHomeAction.gotoFilesTab(driver);
		salesforceHomeAction.uploadFileFromFilesTab(driver, file, suiteData);
		Thread.sleep(5000);
		
		//Verification
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "[ALERT] " + suiteData.getTestUsername() + 
				" attempted to upload content:" + file.getName() +  " violating policy:" + policyName);
		expectedDataMap.put(GatewayTestConstants.POLICY_TYPE, "FileTransfer");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "high");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Policy Violation");
		expectedDataMap.put(GatewayTestConstants.ACTION_TAKEN, "block,");
		expectedDataMap.put(GatewayTestConstants.POLICY_ACTION, "ALERT");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		
		expectedDataMap.clear();
		String uploadFile = "User uploaded file named "+file.getName();
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
	public void testHomeTabBlockUploadPolicy() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		protect.deletePolicyList(client, suiteData, policyList);
		ProtectDTO protectData = new ProtectDTO();
		String policyName = "POLICY_HOMETAB_UPLOAD_BLOCK_SALESFORCE";
		List<String> appList = new ArrayList<String>();
		List<String> userList = new ArrayList<String>();
		List<String> transferType = new ArrayList<String>();
		List<String> actionList = new ArrayList<String>();
		appList.add(suiteData.getSaasAppName());
		userList.add(suiteData.getSaasAppUsername());
		transferType.add("Upload");
		actionList.add("BLOCK_SHARE");
		protectData.setApplicationList(appList);
		protectData.setUserList(userList);
		protectData.setTransferType(transferType);
		protectData.setActionList(actionList);
		protectData.setPolicyName(policyName);
		protectData.setStatus(true);
		protect.createFileTransferPolicy(client, suiteData, protectData);
		
		//Upload file and verify the block
		String filename = salesforceHomeAction.getTimestamp();
		File file = salesforceHomeAction.createFile(filename, "txt");
		salesforceHomeAction.gotoHomeTab(driver);
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

		//Verification
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "[ALERT] " + suiteData.getTestUsername() + 
				" attempted to upload content:" + file.getName() +  " violating policy:" + policyName);
		expectedDataMap.put(GatewayTestConstants.POLICY_TYPE, "FileTransfer");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "high");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Policy Violation");
		expectedDataMap.put(GatewayTestConstants.ACTION_TAKEN, "block,");
		expectedDataMap.put(GatewayTestConstants.POLICY_ACTION, "ALERT");
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
	
	@Test(groups ={"Regression"}, priority=4)
	public void testChatterTabBlockUploadPolicy() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		protect.deletePolicyList(client, suiteData, policyList);
		ProtectDTO protectData = new ProtectDTO();
		String policyName = "POLICY_CHATTERTAB_UPLOAD_BLOCK_SALESFORCE";
		List<String> appList = new ArrayList<String>();
		List<String> userList = new ArrayList<String>();
		List<String> transferType = new ArrayList<String>();
		List<String> actionList = new ArrayList<String>();
		appList.add(suiteData.getSaasAppName());
		userList.add(suiteData.getSaasAppUsername());
		transferType.add("Upload");
		actionList.add("BLOCK_SHARE");
		protectData.setApplicationList(appList);
		protectData.setUserList(userList);
		protectData.setTransferType(transferType);
		protectData.setActionList(actionList);
		protectData.setPolicyName(policyName);
		protectData.setStatus(true);
		protect.createFileTransferPolicy(client, suiteData, protectData);
		
		//Upload file and verify the block
		String filename = salesforceHomeAction.getTimestamp();
		File file = salesforceHomeAction.createFile(filename, "txt");
		salesforceHomeAction.gotoChatterTab(driver);
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

		//Verification
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "[ALERT] " + suiteData.getTestUsername() + 
				" attempted to upload content:" + file.getName() +  " violating policy:" + policyName);
		expectedDataMap.put(GatewayTestConstants.POLICY_TYPE, "FileTransfer");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "high");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Policy Violation");
		expectedDataMap.put(GatewayTestConstants.ACTION_TAKEN, "block,");
		expectedDataMap.put(GatewayTestConstants.POLICY_ACTION, "ALERT");
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
	
	@Test(groups ={"Regression"}, priority=5)
	public void testHomeTabBlockPublicSharingPolicy() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		protect.deletePolicyList(client, suiteData, policyList);
		ProtectDTO protectData = new ProtectDTO();
		String policyName = "POLICY_HOMETAB_PUBLIC_SHARING_BLOCK_SALESFORCE";
		List<String> appList = new ArrayList<String>();
		List<String> actionList = new ArrayList<String>();
		appList.add(suiteData.getSaasAppName());
		actionList.add("BLOCK_SHARE");
		protectData.setApplicationList(appList);
		protectData.setActionList(actionList);
		protectData.setPolicyName(policyName);
		protectData.setStatus(true);
		protect.createFileSharingPolicy(client, suiteData, protectData);
		
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
		
		//Verification
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "[ALERT] " + suiteData.getTestUsername() + 
						" attempted to share content:" + file.getName() +  "with external user(s):ALL_EL__ violating policy:" + policyName);
		expectedDataMap.put(GatewayTestConstants.POLICY_TYPE, "FileSharingGateway");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "high");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Policy Violation");
		expectedDataMap.put(GatewayTestConstants.ACTION_TAKEN, "block,");
		expectedDataMap.put(GatewayTestConstants.POLICY_ACTION, "ALERT");
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
	
	@Test(groups ={"Regression"}, priority=6)
	public void testChatterTabBlockPublicSharingPolicy() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		protect.deletePolicyList(client, suiteData, policyList);
		ProtectDTO protectData = new ProtectDTO();
		String policyName = "POLICY_CHATTERTAB_PUBLIC_SHARING_BLOCK_SALESFORCE";
		List<String> appList = new ArrayList<String>();
		List<String> actionList = new ArrayList<String>();
		appList.add(suiteData.getSaasAppName());
		actionList.add("BLOCK_SHARE");
		protectData.setApplicationList(appList);
		protectData.setActionList(actionList);
		protectData.setPolicyName(policyName);
		protectData.setStatus(true);
		protect.createFileSharingPolicy(client, suiteData, protectData);
		
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
		
		//Verification
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "[ALERT] " + suiteData.getTestUsername() + 
						" attempted to share content:" + file.getName() +  "with external user(s):ALL_EL__ violating policy:" + policyName);
		expectedDataMap.put(GatewayTestConstants.POLICY_TYPE, "FileSharingGateway");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "high");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Policy Violation");
		expectedDataMap.put(GatewayTestConstants.ACTION_TAKEN, "block,");
		expectedDataMap.put(GatewayTestConstants.POLICY_ACTION, "ALERT");
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
	public void testFilesTabBlockDownloadPolicy() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		protect.deletePolicyList(client, suiteData, policyList);
		ProtectDTO protectData = new ProtectDTO();
		String policyName = "POLICY_FILETAB_DOWNLOAD_BLOCK_SALESFORCE";
		List<String> appList = new ArrayList<String>();
		List<String> userList = new ArrayList<String>();
		List<String> transferType = new ArrayList<String>();
		List<String> actionList = new ArrayList<String>();
		appList.add(suiteData.getSaasAppName());
		userList.add(suiteData.getSaasAppUsername());
		transferType.add("Download");
		actionList.add("BLOCK_SHARE");
		protectData.setApplicationList(appList);
		protectData.setUserList(userList);
		protectData.setTransferType(transferType);
		protectData.setActionList(actionList);
		protectData.setPolicyName(policyName);
		protectData.setStatus(true);
		protect.createFileTransferPolicy(client, suiteData, protectData);
		
		//Download file and verify the block
		String filename = salesforceHomeAction.getTimestamp();
		File file = salesforceHomeAction.createFile(filename, "txt");
		salesforceHomeAction.gotoFilesTab(driver);
		salesforceHomeAction.uploadFileFromFilesTab(driver, file, suiteData);
		salesforceHomeAction.gotoFilesTab(driver);
		salesforceHomeAction.viewAndDownloadFileFromFilesTab(driver, filename);
		Thread.sleep(5000);
		boolean flag = clickOkInPopup();
		Assert.assertTrue(flag, "Popup not appeared");
		protect.deletePolicy(client, suiteData, policyName);
		Thread.sleep(5000);
		salesforceHomeAction.gotoFilesTab(driver);
		salesforceHomeAction.viewAndDownloadFileFromFilesTab(driver, filename);
		
		//verification
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "[ALERT] " + suiteData.getTestUsername() + 
				" attempted to download content:" + file.getName() +  " violating policy:" + policyName);
		expectedDataMap.put(GatewayTestConstants.POLICY_TYPE, "FileTransfer");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "high");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Policy Violation");
		expectedDataMap.put(GatewayTestConstants.ACTION_TAKEN, "block,");
		expectedDataMap.put(GatewayTestConstants.POLICY_ACTION, "ALERT");
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
	
	@Test(groups ={"Regression"}, priority=8)
	public void testHomeTabBlockDownloadPolicy() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		protect.deletePolicyList(client, suiteData, policyList);
		ProtectDTO protectData = new ProtectDTO();
		String policyName = "POLICY_HOMETAB_DOWNLOAD_BLOCK_SALESFORCE";
		List<String> appList = new ArrayList<String>();
		List<String> userList = new ArrayList<String>();
		List<String> transferType = new ArrayList<String>();
		List<String> actionList = new ArrayList<String>();
		appList.add(suiteData.getSaasAppName());
		userList.add(suiteData.getSaasAppUsername());
		transferType.add("Download");
		actionList.add("BLOCK_SHARE");
		protectData.setApplicationList(appList);
		protectData.setUserList(userList);
		protectData.setTransferType(transferType);
		protectData.setActionList(actionList);
		protectData.setPolicyName(policyName);
		protectData.setStatus(true);
		protect.createFileTransferPolicy(client, suiteData, protectData);
		
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
		
		//Verification
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "[ALERT] " + suiteData.getTestUsername() + 
				" attempted to download content:" + file.getName() +  " violating policy:" + policyName);
		expectedDataMap.put(GatewayTestConstants.POLICY_TYPE, "FileTransfer");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "high");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Policy Violation");
		expectedDataMap.put(GatewayTestConstants.ACTION_TAKEN, "block,");
		expectedDataMap.put(GatewayTestConstants.POLICY_ACTION, "ALERT");
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
	
	@Test(groups ={"Regression"}, priority=9)
	public void testChatterTabBlockDownloadPolicy() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		protect.deletePolicyList(client, suiteData, policyList);
		ProtectDTO protectData = new ProtectDTO();
		String policyName = "POLICY_CHATTERTAB_DOWNLOAD_BLOCK_SALESFORCE";
		List<String> appList = new ArrayList<String>();
		List<String> userList = new ArrayList<String>();
		List<String> transferType = new ArrayList<String>();
		List<String> actionList = new ArrayList<String>();
		appList.add(suiteData.getSaasAppName());
		userList.add(suiteData.getSaasAppUsername());
		transferType.add("Download");
		actionList.add("BLOCK_SHARE");
		protectData.setApplicationList(appList);
		protectData.setUserList(userList);
		protectData.setTransferType(transferType);
		protectData.setActionList(actionList);
		protectData.setPolicyName(policyName);
		protectData.setStatus(true);
		protect.createFileTransferPolicy(client, suiteData, protectData);
		
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
		
		//Verification
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "[ALERT] " + suiteData.getTestUsername() + 
				" attempted to download content:" + file.getName() +  " violating policy:" + policyName);
		expectedDataMap.put(GatewayTestConstants.POLICY_TYPE, "FileTransfer");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "high");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Policy Violation");
		expectedDataMap.put(GatewayTestConstants.ACTION_TAKEN, "block,");
		expectedDataMap.put(GatewayTestConstants.POLICY_ACTION, "ALERT");
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
	
	@Test(groups ={"Regression"}, priority=10)
	public void testFilesTabBlockUploadFileSizePolicy() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		protect.deletePolicyList(client, suiteData, policyList);
		ProtectDTO protectData = new ProtectDTO();
		String policyName = "POLICY_UPLOAD_BLOCK_SALESFORCE";
		List<String> appList = new ArrayList<String>();
		List<String> userList = new ArrayList<String>();
		List<String> transferType = new ArrayList<String>();
		List<String> actionList = new ArrayList<String>();
		appList.add(suiteData.getSaasAppName());
		userList.add(suiteData.getSaasAppUsername());
		transferType.add("Upload");
		actionList.add("BLOCK_SHARE");
		protectData.setApplicationList(appList);
		protectData.setUserList(userList);
		protectData.setTransferType(transferType);
		protectData.setActionList(actionList);
		protectData.setPolicyName(policyName);
		protectData.setStatus(true);
		protectData.setLargerSize(0);
		protectData.setSmallerSize(1048576);
		protect.createFileTransferPolicy(client, suiteData, protectData);
		
		//Upload file and verify the block
		String filename = salesforceHomeAction.getTimestamp();
		File file = salesforceHomeAction.createFile(filename, "txt");
		salesforceHomeAction.gotoFilesTab(driver);
		salesforceHomeAction.uploadFileFromFilesTab(driver, file, suiteData);
		protect.deletePolicy(client, suiteData, policyName);
		salesforceHomeAction.gotoFilesTab(driver);
		salesforceHomeAction.uploadFileFromFilesTab(driver, file, suiteData);
		Thread.sleep(5000);
		
		//Verification
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "[ALERT] " + suiteData.getTestUsername() + 
				" attempted to upload content:" + file.getName() +  " violating policy:" + policyName);
		expectedDataMap.put(GatewayTestConstants.POLICY_TYPE, "FileTransfer");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "high");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Policy Violation");
		expectedDataMap.put(GatewayTestConstants.ACTION_TAKEN, "block,");
		expectedDataMap.put(GatewayTestConstants.POLICY_ACTION, "ALERT");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		
		expectedDataMap.clear();
		String uploadFile = "User uploaded file named "+file.getName();
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Upload");
		expectedDataMap.put(GatewayTestConstants.FACILITY, suiteData.getSaasAppName());
		expectedDataMap.put(GatewayTestConstants.MESSAGE, uploadFile);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	@Test(groups ={"Regression"}, priority=11)
	public void testFileEncryptionPolicy() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		protect.deletePolicyList(client, suiteData, policyList);
		ProtectDTO protectData = new ProtectDTO();
		String policyName = "POLICY_ENCRYPTION_SALESFORCE";
		List<String> appList = new ArrayList<String>();
		List<String> userList = new ArrayList<String>();
		List<String> transferType = new ArrayList<String>();
		List<String> actionList = new ArrayList<String>();
		appList.add(suiteData.getSaasAppName());
		userList.add(suiteData.getSaasAppUsername());
		transferType.add("Upload");
		actionList.add("ENCRYPT");
		protectData.setApplicationList(appList);
		protectData.setUserList(userList);
		protectData.setTransferType(transferType);
		protectData.setActionList(actionList);
		protectData.setPolicyName(policyName);
		protectData.setStatus(true);
		protect.createFileTransferPolicy(client, suiteData, protectData);
		
		//Upload file and verify the block
		String filename = salesforceHomeAction.getTimestamp();
		File file = salesforceHomeAction.createFile(filename, "txt");
		salesforceHomeAction.gotoHomeTab(driver);
		salesforceHomeAction.uploadFilesForHomeChatter(driver, file);
		salesforceHomeAction.shareFileForHomeChatter(driver);
		protect.deletePolicy(client, suiteData, policyName);
		Thread.sleep(5000);
		
		//Verification
		filename = file.getName()+".eef";
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "[ALERT] " + suiteData.getTestUsername() + 
				" attempted to upload content:" + filename +  " violating policy:" + policyName);
		expectedDataMap.put(GatewayTestConstants.POLICY_TYPE, "FileTransfer");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "high");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Policy Violation");
		expectedDataMap.put(GatewayTestConstants.ACTION_TAKEN, "encrypt,");
		expectedDataMap.put(GatewayTestConstants.POLICY_ACTION, "ALERT");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		
		expectedDataMap.clear();
		String uploadFile = "User uploaded file named "+filename+".";
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Upload");
		expectedDataMap.put(GatewayTestConstants.FACILITY, suiteData.getSaasAppName());
		expectedDataMap.put(GatewayTestConstants.MESSAGE, uploadFile);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		
		expectedDataMap.clear();
		String encryptFile = "File "+file.getName()+" encrypted  on upload for user "+suiteData.getSaasAppUsername()+".";
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "File Encryption");
		expectedDataMap.put(GatewayTestConstants.FACILITY, suiteData.getSaasAppName());
		expectedDataMap.put(GatewayTestConstants.MESSAGE, encryptFile);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		expectedDataMap.put(GatewayTestConstants.POLICY_TYPE, "FileTransfer");
		expectedDataMap.put(GatewayTestConstants.POLICY_ACTION, "ALERT");
		expectedDataMap.put(GatewayTestConstants.ACTION_TAKEN, "encrypt");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	@Test(groups ={"Regression"}, priority=12)
	public void testFileEncryptionDecryptionPolicy() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		protect.deletePolicyList(client, suiteData, policyList);
		ProtectDTO protectData = new ProtectDTO();
		String policyName = "POLICY_DECRYPTION_SALESFORCE";
		List<String> appList = new ArrayList<String>();
		List<String> userList = new ArrayList<String>();
		List<String> transferType = new ArrayList<String>();
		List<String> actionList = new ArrayList<String>();
		appList.add(suiteData.getSaasAppName());
		userList.add(suiteData.getSaasAppUsername());
		transferType.add("Upload");
		transferType.add("Download");
		actionList.add("ENCRYPT");
		actionList.add("DECRYPT");
		protectData.setApplicationList(appList);
		protectData.setUserList(userList);
		protectData.setTransferType(transferType);
		protectData.setActionList(actionList);
		protectData.setPolicyName(policyName);
		protectData.setStatus(true);
		protect.createFileTransferPolicy(client, suiteData, protectData);
		
		//Upload file and verify the block
		String filename = salesforceHomeAction.getTimestamp();
		File file = salesforceHomeAction.createFile(filename, "txt");
		salesforceHomeAction.gotoHomeTab(driver);
		salesforceHomeAction.uploadFilesForHomeChatter(driver, file);
		salesforceHomeAction.shareFileForHomeChatter(driver);
		salesforceHomeAction.downloadFileFromHomeChatterTab(driver, filename);
		protect.deletePolicy(client, suiteData, policyName);
		Thread.sleep(5000);
		
		//Verification
		filename = file.getName()+".eef";
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "[ALERT] " + suiteData.getTestUsername() + 
				" attempted to upload content:" + filename +  " violating policy:" + policyName);
		expectedDataMap.put(GatewayTestConstants.POLICY_TYPE, "FileTransfer");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "high");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Policy Violation");
		expectedDataMap.put(GatewayTestConstants.ACTION_TAKEN, "encrypt,");
		expectedDataMap.put(GatewayTestConstants.POLICY_ACTION, "ALERT");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		
		expectedDataMap.clear();
		String uploadFile = "User uploaded file named "+filename+".";
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Upload");
		expectedDataMap.put(GatewayTestConstants.FACILITY, suiteData.getSaasAppName());
		expectedDataMap.put(GatewayTestConstants.MESSAGE, uploadFile);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		
		expectedDataMap.clear();
		String encryptFile = "File "+file.getName()+" encrypted  on upload for user "+suiteData.getSaasAppUsername()+".";
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "File Encryption");
		expectedDataMap.put(GatewayTestConstants.FACILITY, suiteData.getSaasAppName());
		expectedDataMap.put(GatewayTestConstants.MESSAGE, encryptFile);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		expectedDataMap.put(GatewayTestConstants.POLICY_TYPE, "FileTransfer");
		expectedDataMap.put(GatewayTestConstants.POLICY_ACTION, "ALERT");
		expectedDataMap.put(GatewayTestConstants.ACTION_TAKEN, "encrypt");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		
		filename = filename+".eef";
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "[ALERT] " + suiteData.getTestUsername() + 
				" attempted to download content:" + filename +  " violating policy:" + policyName);
		expectedDataMap.put(GatewayTestConstants.POLICY_TYPE, "FileTransfer");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "high");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Policy Violation");
		expectedDataMap.put(GatewayTestConstants.ACTION_TAKEN, "encrypt,");
		expectedDataMap.put(GatewayTestConstants.POLICY_ACTION, "ALERT");
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
		
		expectedDataMap.clear();
		String decryptFile = "File "+filename+" decrypted on download for user "+suiteData.getSaasAppUsername()+".";
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "File Encryption");
		expectedDataMap.put(GatewayTestConstants.FACILITY, suiteData.getSaasAppName());
		expectedDataMap.put(GatewayTestConstants.MESSAGE, decryptFile);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		expectedDataMap.put(GatewayTestConstants.POLICY_TYPE, "FileTransfer");
		expectedDataMap.put(GatewayTestConstants.POLICY_ACTION, "ALERT");
		expectedDataMap.put(GatewayTestConstants.ACTION_TAKEN, "encrypt");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	@Test(groups ={"Regression", "Sanity"}, priority=136)
	public void salesforces_Test_TestLogout() throws Exception {
		data.clear();
		data.put("message", "User logged out");
		protect.deletePolicyList(client, suiteData, policyList);
		salesforceLogin.logout(getWebDriver(), suiteData);
	}
}