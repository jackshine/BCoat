package com.elastica.tests.protect;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.elastica.action.dropbox.DropboxAction;
import com.elastica.action.protect.ProtectAction;
import com.elastica.action.protect.ProtectDTO;
import com.elastica.action.salesforce.SalesforceHomeAction;
import com.elastica.common.CommonTest;
import com.elastica.gateway.GatewayTestConstants;
import com.jacob.com.LibraryLoader;

import autoitx4java.AutoItX;

public class FileTransferPolicyTests extends CommonTest{

	ProtectAction protectAction = new ProtectAction();
	DropboxAction dropboxAction = new DropboxAction();
	SalesforceHomeAction salesforceHomeAction = new SalesforceHomeAction();
	WebDriver driver = null;
	
	@BeforeClass(groups ={"Regression", "Sanity"})
	public void initialization(){
		driver = getWebDriver();
	}
	
	
	@Test
	public void autoitTest() throws Exception{
		ProtectDTO protectData = new ProtectDTO();
		List<String> actionList = new ArrayList<String>();
		actionList.add("LOGOUT");  //Session logout
		//actionList.add("BLOCK_ALL");  //Block all service
		//actionList.add("BLOCK_SERVICE");   //Block specific service
		protectData.setActionList(actionList);
		protectData.setThreadScore(15);   //If you dont pass the threatscore, default it will take 10
		protectData.setStatus(true);  //Create a active policy
		protectData.setPolicyName("Pol123");   //Policy name
		
		//If you dont pass the saas app then it will take from suite data
		protect.createThreatScorePolicy(client, suiteData, protectData);
		
		/*ProtectDTO protectData = new ProtectDTO();
		List<String> actionList = new ArrayList<String>();
		List<String> transferType = new ArrayList<String>();
		List<String> applicationList = new ArrayList<String>();
		actionList.add("BLOCK_SHARE");
		transferType.add("Upload");
		applicationList.add(suiteData.getSaasAppName());
		protectData.setActionList(actionList);
		protectData.setTransferType(transferType);
		protectData.setApplicationList(applicationList);
		protectData.setStatus(true);
		protectData.setPolicyName("Pol123");
		protect.createFileTransferPolicy(client, suiteData, protectData);*/
		
	}
	
	/*@Test(groups ={"Regression", "Sanity"})
	public void verifyPolicyViolateAndFileUploadBlock() throws Exception{
		//String activities = "Session:Login/Logout,File:Upload/Delete,Folder:Create";
		String fromTime=backend.getCurrentTime();
		String filename = salesforceHomeAction.getTimestamp();
		File file = salesforceHomeAction.createFile(filename, "txt");
		
		ProtectDTO protectData = new ProtectDTO();
		protectData.setStatus(true);
		String activities1 = "File:Upload/Download,Session:Login/Logout,Folder:Create/Delete/Download";
		protectData.setPolicyName("27042016044443");
		
		List<String> userList = new ArrayList<String>();
		userList.add("admin@protectbeatle.com");
		userList.add("protectauto@protectbeatle.com");
		protectData.setUserList(userList);
		
		protectAction.createFileSharingPolicy(client, suiteData, protectData);
		//protectAction.createAccessEnforcementPolicy(client, suiteData, protectData, activities1);
		//protectAction.createFileSharingPolicy(client, suiteData, protectData);
		login.loginCloudSocPortal(driver, suiteData);
		protectAction.activatePolicy(client, suiteData, protectData.getPolicyName());
		dropboxAction.login(driver, suiteData);
		
		
		
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		String message = "[ALERT] protectauto@protectbeatle.com attempted to upload content:"+file.getName()+" violating policy:"+protectData.getPolicyName();
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Policy Violation");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, message);
		expectedDataMap.put(GatewayTestConstants.POLICY_TYPE, "FileTransfer");
		expectedDataMap.put(GatewayTestConstants.POLICY_ACTION, GatewayTestConstants.POLICY_ACTION_ALERT);
		expectedDataMap.put(GatewayTestConstants.POLICY_VIOLATED, protectData.getPolicyName());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		expectedDataMap.put(GatewayTestConstants.USER, suiteData.getSaasAppUsername());
		expectedDataMap.put(GatewayTestConstants.ELASTICA_USER, suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.FACILITY, suiteData.getSaasAppName());
		expectedDataMap.put(GatewayTestConstants.SEVERITY, protectData.getSeverity());
		expectedDataMap.put(GatewayTestConstants.ACTION_TAKEN, "");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}*/
	
	
	/*@Test(groups ={"Regression", "Sanity"})
	public void verifyPolicyViolateAndFileDownloadBlock() throws Exception{
		String fromTime=backend.getCurrentTime();
		List<String> userList = new ArrayList<String>();
		userList.add("protectauto@protectbeatle.com");
		userList.add("box-admin@protectbeatle.com");
		userList.add("admin@protectbeatle.com");
		
		List<String> appList = new ArrayList<String>();
		appList.add("Dropbox");
		
		List<String> transferType = new ArrayList<String>();
		transferType.add("Download");

		ProtectDTO protectData = new ProtectDTO();
		String filename = salesforceHomeAction.getTimestamp();
		protectData.setPolicyName(filename);
		protectData.setUserList(userList);
		protectData.setApplicationList(appList);
		protectData.setTransferType(transferType);
		protectData.setActionList();
		
		login.loginCloudSocPortal(driver, suiteData);
		protectAction.createFileTransferPolicy(client, suiteData, protectData);
		protectAction.activatePolicy(client, suiteData, protectData.getPolicyName());
		dropboxAction.login(driver, suiteData);
		File file = salesforceHomeAction.createFile(filename, "txt");
		dropboxAction.uploadFile(driver, file.getAbsolutePath());
		dropboxAction.download(driver, filename);
		Thread.sleep(10000);
		dropboxAction.logout(driver);
		protectAction.deletePolicy(client, suiteData, protectData.getPolicyName());
		salesforceHomeAction.deleteFile(file);
		
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		String message = "[ALERT] protectauto@protectbeatle.com attempted to upload content:"+file.getName()+" violating policy:"+protectData.getPolicyName();
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Policy Violation");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, message);
		expectedDataMap.put(GatewayTestConstants.POLICY_TYPE, "FileTransfer");
		expectedDataMap.put(GatewayTestConstants.POLICY_ACTION, GatewayTestConstants.POLICY_ACTION_ALERT);
		expectedDataMap.put(GatewayTestConstants.POLICY_VIOLATED, protectData.getPolicyName());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		expectedDataMap.put(GatewayTestConstants.USER, suiteData.getSaasAppUsername());
		expectedDataMap.put(GatewayTestConstants.ELASTICA_USER, suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.FACILITY, suiteData.getSaasAppName());
		expectedDataMap.put(GatewayTestConstants.SEVERITY, protectData.getSeverity());
		expectedDataMap.put(GatewayTestConstants.ACTION_TAKEN, "");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}*/
}
