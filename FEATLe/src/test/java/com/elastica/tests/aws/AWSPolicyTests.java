package com.elastica.tests.aws;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.Alert;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.elastica.action.protect.ProtectDTO;
import com.elastica.common.GWCommonTest;
import com.elastica.constants.aws.AwsConstants;
import com.elastica.gateway.GatewayTestConstants;
import com.elastica.logger.Logger;

public class AWSPolicyTests extends GWCommonTest{

	String regionName = null;
	String regionCode = null;
	String bucketName = null;
	String folderName = null;
	String fileName = null;
	String region = null;
	String fromTime = null;
	String platform = null;
	String platformVersion = null;
	String browserVersion = null;
	File file = null;
	List<String> policyList = new ArrayList<String>();
	
	@Parameters({"regionName", "regionCode"})
	@Test(groups={"Regression", "Sanity"}, priority=1)
	public void loginToPortal(String name, String code) throws Exception {
		regionName = name;
		regionCode = code;
		Reporter.log("Started performing activities on saas app", true);
		login.loginCloudSocPortal(getWebDriver(), suiteData);
		Thread.sleep(10000);
		policyList.add("POLICY_UPLOAD_BLOCK_AWS");
		policyList.add("POLICY_DOWNLOAD_BLOCK_AWS");
		policyList.add("POLICY_SHARE_BLOCK_AWS");
		policyList.add("POLICY_CREATE_BUCKET_BLOCK_AWS");
		policyList.add("POLICY_EMPTY_BUCKET_BLOCK_AWS");
		policyList.add("POLICY_DELETE_BUCKET_BLOCK_AWS");
		policyList.add("POLICY_CREATE_FOLDER_BLOCK_AWS");
		policyList.add("POLICY_DELETE_FOLDER_BLOCK_AWS");
		fromTime=backend.getCurrentTime();
		awsAction.loginAWS(getWebDriver(), suiteData);
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
		Reporter.log("Finished login activities on cloudSoc", true);
	}
	
	@Test(groups={"Regression", "Sanity"}, priority=2)
	public void testBlockUploadPolicy() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		protect.deletePolicyList(client, suiteData, policyList);
		ProtectDTO protectData = new ProtectDTO();
		String policyName = "POLICY_UPLOAD_BLOCK_AWS";
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
		String timeStamp = salesforceHomeAction.getTimestamp();
		bucketName = "bucket"+timeStamp;
		folderName = "folder"+timeStamp;
		fileName = "f"+timeStamp;
		region = awsAction.createBucket(getWebDriver(), bucketName, regionName);
		awsAction.createFolder(getWebDriver(), bucketName, folderName, region);
		file = salesforceHomeAction.createFile(fileName, "txt");
		awsAction.uploadFile(getWebDriver(), file, suiteData);
		Thread.sleep(15000);
		boolean flag = clickOkInPopup();
		boolean flag1 = clickOkInPopup();
		Assert.assertTrue(flag, "Popup not appeared");
		Assert.assertTrue(flag1, "Popup not appeared");
		Thread.sleep(5000);
		protect.deletePolicy(client, suiteData, policyName);
		Thread.sleep(10000);
		awsAction.uploadFile(getWebDriver(), file, suiteData);
		Thread.sleep(10000);
		salesforceHomeAction.deleteFile(file);
		getWebDriver().navigate().to(AwsConstants.AWS_S3_URL);
		try{
			Thread.sleep(5000);
			Logger.info("Alert present: "+getWebDriver().switchTo().alert().getText());
			getWebDriver().switchTo().alert().accept();
		}catch(Exception e){
			Logger.info("Alert not present");
		}
		Thread.sleep(5000);
		awsAction.deletebucket(getWebDriver(), bucketName, region);
		
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
		String uploadFile = "User uploaded file "+file.getName();
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Upload");
		expectedDataMap.put(GatewayTestConstants.FACILITY, suiteData.getSaasAppName());
		expectedDataMap.put(GatewayTestConstants.MESSAGE, uploadFile);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}

	@Test(groups={"Regression"}, priority=3)
	public void testBlockUploadFilePatternPolicy() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		String timeStamp = salesforceHomeAction.getTimestamp();
		protect.deletePolicyList(client, suiteData, policyList);
		ProtectDTO protectData = new ProtectDTO();
		String policyName = "POLICY_UPLOAD_BLOCK_AWS";
		List<String> appList = new ArrayList<String>();
		List<String> userList = new ArrayList<String>();
		List<String> transferType = new ArrayList<String>();
		List<String> actionList = new ArrayList<String>();
		List<String> fileList = new ArrayList<String>();
		appList.add(suiteData.getSaasAppName());
		userList.add(suiteData.getSaasAppUsername());
		transferType.add("Upload");
		actionList.add("BLOCK_SHARE");
		fileList.add(timeStamp);
		protectData.setApplicationList(appList);
		protectData.setUserList(userList);
		protectData.setTransferType(transferType);
		protectData.setActionList(actionList);
		protectData.setPolicyName(policyName);
		protectData.setStatus(true);
		protectData.setFilenamePattern(fileList);
		protect.createFileTransferPolicy(client, suiteData, protectData);
		
		//Upload file and verify the block
		bucketName = "bucket"+timeStamp;
		folderName = "folder"+timeStamp;
		fileName = "f"+timeStamp;
		region = awsAction.createBucket(getWebDriver(), bucketName, regionName);
		awsAction.createFolder(getWebDriver(), bucketName, folderName, region);
		file = salesforceHomeAction.createFile(fileName, "txt");
		awsAction.uploadFile(getWebDriver(), file, suiteData);
		Thread.sleep(15000);
		boolean flag = clickOkInPopup();
		boolean flag1 = clickOkInPopup();
		Assert.assertTrue(flag, "Popup not appeared");
		Assert.assertTrue(flag1, "Popup not appeared");
		Thread.sleep(5000);
		protect.deletePolicy(client, suiteData, policyName);
		Thread.sleep(10000);
		awsAction.uploadFile(getWebDriver(), file, suiteData);
		Thread.sleep(10000);
		salesforceHomeAction.deleteFile(file);
		getWebDriver().navigate().to(AwsConstants.AWS_S3_URL);
		try{
			Thread.sleep(5000);
			Logger.info("Alert present: "+getWebDriver().switchTo().alert().getText());
			getWebDriver().switchTo().alert().accept();
		}catch(Exception e){
			Logger.info("Alert not present");
		}
		Thread.sleep(5000);
		awsAction.deletebucket(getWebDriver(), bucketName, region);
		
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
		String uploadFile = "User uploaded file "+file.getName();
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Upload");
		expectedDataMap.put(GatewayTestConstants.FACILITY, suiteData.getSaasAppName());
		expectedDataMap.put(GatewayTestConstants.MESSAGE, uploadFile);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	@Test(groups={"Regression"}, priority=4)
	public void testBlockUploadFileSizePolicy() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		protect.deletePolicyList(client, suiteData, policyList);
		ProtectDTO protectData = new ProtectDTO();
		String policyName = "POLICY_UPLOAD_BLOCK_AWS";
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
		String timeStamp = salesforceHomeAction.getTimestamp();
		bucketName = "bucket"+timeStamp;
		folderName = "folder"+timeStamp;
		fileName = "f"+timeStamp;
		region = awsAction.createBucket(getWebDriver(), bucketName, regionName);
		awsAction.createFolder(getWebDriver(), bucketName, folderName, region);
		file = salesforceHomeAction.createFile(fileName, "txt");
		awsAction.uploadFile(getWebDriver(), file, suiteData);
		Thread.sleep(15000);
		boolean flag = clickOkInPopup();
		boolean flag1 = clickOkInPopup();
		Assert.assertTrue(flag, "Popup not appeared");
		Assert.assertTrue(flag1, "Popup not appeared");
		Thread.sleep(5000);
		protect.deletePolicy(client, suiteData, policyName);
		Thread.sleep(10000);
		awsAction.uploadFile(getWebDriver(), file, suiteData);
		Thread.sleep(10000);
		salesforceHomeAction.deleteFile(file);
		getWebDriver().navigate().to(AwsConstants.AWS_S3_URL);
		try{
			Thread.sleep(5000);
			Logger.info("Alert present: "+getWebDriver().switchTo().alert().getText());
			getWebDriver().switchTo().alert().accept();
		}catch(Exception e){
			Logger.info("Alert not present");
		}
		Thread.sleep(5000);
		awsAction.deletebucket(getWebDriver(), bucketName, region);
		
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
		String uploadFile = "User uploaded file "+file.getName();
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Upload");
		expectedDataMap.put(GatewayTestConstants.FACILITY, suiteData.getSaasAppName());
		expectedDataMap.put(GatewayTestConstants.MESSAGE, uploadFile);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	@Test(groups={"Regression", "Sanity"}, priority=5)
	public void testBlockDownloadPolicy() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		protect.deletePolicyList(client, suiteData, policyList);
		ProtectDTO protectData = new ProtectDTO();
		String policyName = "POLICY_DOWNLOAD_BLOCK_AWS";
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
		
		//Upload file and verify the block
		String timeStamp = salesforceHomeAction.getTimestamp();
		bucketName = "bucket"+timeStamp;
		folderName = "folder"+timeStamp;
		fileName = "f"+timeStamp;
		region = awsAction.createBucket(getWebDriver(), bucketName, regionName);
		awsAction.createFolder(getWebDriver(), bucketName, folderName, region);
		file = salesforceHomeAction.createFile(fileName, "txt");
		awsAction.uploadFile(getWebDriver(), file, suiteData);
		awsAction.downloadFileAndHandleBlockPopup(getWebDriver(), file, region, suiteData);
		Thread.sleep(5000);
		protect.deletePolicy(client, suiteData, policyName);
		salesforceHomeAction.deleteFile(file);
		getWebDriver().navigate().to(AwsConstants.AWS_S3_URL);
		Thread.sleep(5000);
		awsAction.deletebucket(getWebDriver(), bucketName, region);
		
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "[ALERT] " + suiteData.getTestUsername() + 
				" attempted to download content:" + file.getName() +  " violating policy:" + policyName);
		expectedDataMap.put(GatewayTestConstants.POLICY_TYPE, "FileTransfer");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "high");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Policy Violation");
		expectedDataMap.put(GatewayTestConstants.ACTION_TAKEN, "block,");
		expectedDataMap.put(GatewayTestConstants.POLICY_ACTION, "ALERT");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		
		/*expectedDataMap.clear();
		String uploadFile = "User uploaded file "+file.getName();
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Upload");
		expectedDataMap.put(GatewayTestConstants.FACILITY, suiteData.getSaasAppName());
		expectedDataMap.put(GatewayTestConstants.MESSAGE, uploadFile);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");*/
	}
	
	@Test(groups={"Regression", "Sanity"}, priority=8)
	public void testBlockDownloadFilePatternPolicy() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		protect.deletePolicyList(client, suiteData, policyList);
		String timeStamp = salesforceHomeAction.getTimestamp();
		ProtectDTO protectData = new ProtectDTO();
		String policyName = "POLICY_DOWNLOAD_BLOCK_AWS";
		List<String> appList = new ArrayList<String>();
		List<String> userList = new ArrayList<String>();
		List<String> transferType = new ArrayList<String>();
		List<String> actionList = new ArrayList<String>();
		List<String> fileList = new ArrayList<String>();
		appList.add(suiteData.getSaasAppName());
		userList.add(suiteData.getSaasAppUsername());
		transferType.add("Download");
		actionList.add("BLOCK_SHARE");
		fileList.add(timeStamp);
		protectData.setApplicationList(appList);
		protectData.setUserList(userList);
		protectData.setTransferType(transferType);
		protectData.setActionList(actionList);
		protectData.setPolicyName(policyName);
		protectData.setFilenamePattern(fileList);
		protectData.setStatus(true);
		protect.createFileTransferPolicy(client, suiteData, protectData);
		
		//Upload file and verify the block
		bucketName = "bucket"+timeStamp;
		folderName = "folder"+timeStamp;
		fileName = "f"+timeStamp;
		region = awsAction.createBucket(getWebDriver(), bucketName, regionName);
		awsAction.createFolder(getWebDriver(), bucketName, folderName, region);
		file = salesforceHomeAction.createFile(fileName, "txt");
		awsAction.uploadFile(getWebDriver(), file, suiteData);
		awsAction.downloadFileAndHandleBlockPopup(getWebDriver(), file, region, suiteData);
		Thread.sleep(5000);
		protect.deletePolicy(client, suiteData, policyName);
		salesforceHomeAction.deleteFile(file);
		getWebDriver().navigate().to(AwsConstants.AWS_S3_URL);
		Thread.sleep(5000);
		awsAction.deletebucket(getWebDriver(), bucketName, region);
		
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "[ALERT] " + suiteData.getTestUsername() + 
				" attempted to download content:" + file.getName() +  " violating policy:" + policyName);
		expectedDataMap.put(GatewayTestConstants.POLICY_TYPE, "FileTransfer");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "high");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Policy Violation");
		expectedDataMap.put(GatewayTestConstants.ACTION_TAKEN, "block,");
		expectedDataMap.put(GatewayTestConstants.POLICY_ACTION, "ALERT");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	@Test(groups={"Regression", "Sanity"}, priority=7)
	public void testBlockDownloadFileSizePolicy() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		protect.deletePolicyList(client, suiteData, policyList);
		ProtectDTO protectData = new ProtectDTO();
		String policyName = "POLICY_DOWNLOAD_BLOCK_AWS";
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
		protectData.setLargerSize(0);
		protectData.setSmallerSize(1048576);
		protect.createFileTransferPolicy(client, suiteData, protectData);
		
		//Upload file and verify the block
		String timeStamp = salesforceHomeAction.getTimestamp();
		bucketName = "bucket"+timeStamp;
		folderName = "folder"+timeStamp;
		fileName = "f"+timeStamp;
		region = awsAction.createBucket(getWebDriver(), bucketName, regionName);
		awsAction.createFolder(getWebDriver(), bucketName, folderName, region);
		file = salesforceHomeAction.createFile(fileName, "txt");
		awsAction.uploadFile(getWebDriver(), file, suiteData);
		awsAction.downloadFileAndHandleBlockPopup(getWebDriver(), file, region, suiteData);
		Thread.sleep(5000);
		protect.deletePolicy(client, suiteData, policyName);
		salesforceHomeAction.deleteFile(file);
		getWebDriver().navigate().to(AwsConstants.AWS_S3_URL);
		Thread.sleep(5000);
		awsAction.deletebucket(getWebDriver(), bucketName, region);
		
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "[ALERT] " + suiteData.getTestUsername() + 
				" attempted to download content:" + file.getName() +  " violating policy:" + policyName);
		expectedDataMap.put(GatewayTestConstants.POLICY_TYPE, "FileTransfer");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "high");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Policy Violation");
		expectedDataMap.put(GatewayTestConstants.ACTION_TAKEN, "block,");
		expectedDataMap.put(GatewayTestConstants.POLICY_ACTION, "ALERT");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	@Test(groups={"Regression", "Sanity"}, priority=8)
	public void testBlockSharePolicy() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		protect.deletePolicyList(client, suiteData, policyList);
		ProtectDTO protectData = new ProtectDTO();
		String policyName = "POLICY_SHARE_BLOCK_AWS";
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
		protect.createFileSharingPolicy(client, suiteData, protectData);
		
		//Share file and verify the block
		String timeStamp = salesforceHomeAction.getTimestamp();
		bucketName = "bucket"+timeStamp;
		folderName = "folder"+timeStamp;
		fileName = "f"+timeStamp;
		region = awsAction.createBucket(getWebDriver(), bucketName, regionName);
		awsAction.createFolder(getWebDriver(), bucketName, folderName, region);
		file = salesforceHomeAction.createFile(fileName, "txt");
		awsAction.uploadFile(getWebDriver(), file, suiteData);
		salesforceHomeAction.deleteFile(file);
		awsAction.shareFile(getWebDriver(), file.getName(), region);
		Thread.sleep(5000);
		boolean flag = clickOkInPopup();
		Assert.assertTrue(flag, "Popup not appeared");
		boolean flag1 = clickOkInPopup();
		Assert.assertTrue(flag1, "Popup not appeared");
		protect.deletePolicy(client, suiteData, policyName);
		awsAction.shareFile(getWebDriver(), file.getName(), region);
		getWebDriver().navigate().to(AwsConstants.AWS_S3_URL);
		try{
			Thread.sleep(5000);
			Logger.info("Alert present: "+getWebDriver().switchTo().alert().getText());
			getWebDriver().switchTo().alert().accept();
		}catch(Exception e){
			Logger.info("Alert not present");
		}
		Thread.sleep(5000);
		awsAction.deletebucket(getWebDriver(), bucketName, region);
		
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "[ALERT] " + suiteData.getTestUsername() + 
				" attempted to share content:" + file.getName() +  "with external user(s):ALL_EL__ violating policy:" + policyName);
		expectedDataMap.put(GatewayTestConstants.POLICY_TYPE, "FileSharingGateway");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, protectData.getSeverity());
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Policy Violation");
		expectedDataMap.put(GatewayTestConstants.ACTION_TAKEN, "block,");
		expectedDataMap.put(GatewayTestConstants.POLICY_ACTION, GatewayTestConstants.POLICY_ACTION_ALERT);
		expectedDataMap.put(GatewayTestConstants.POLICY_VIOLATED, policyName);
		expectedDataMap.put(GatewayTestConstants.FACILITY, suiteData.getSaasAppName());
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		
		expectedDataMap.clear();
		String message = "User shared file "+file.getName()+" from bucket "+bucketName;
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Share");
		expectedDataMap.put(GatewayTestConstants.FACILITY, suiteData.getSaasAppName());
		expectedDataMap.put(GatewayTestConstants.MESSAGE, message);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	@Test(groups={"Regression", "Sanity"}, priority=9)
	public void testBlockCreateBucket() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		protect.deletePolicyList(client, suiteData, policyList);
		ProtectDTO protectData = new ProtectDTO();
		String policyName = "POLICY_CREATE_BUCKET_BLOCK_AWS";
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
		protect.createAccessEnforcementPolicy(client, suiteData, protectData, "Bucket:Create");
		
		//Create bucket and verify the block
		String timeStamp = salesforceHomeAction.getTimestamp();
		bucketName = "bucket"+timeStamp;
		region = awsAction.createBucket(getWebDriver(), bucketName, regionName);
		Thread.sleep(5000);
		boolean flag = clickOkInPopup();
		Assert.assertTrue(flag, "Popup not appeared");
		boolean flag1 = clickOkInPopup();
		Assert.assertTrue(flag1, "Popup not appeared");
		getWebDriver().navigate().refresh();
		protect.deletePolicy(client, suiteData, policyName);
		Thread.sleep(5000);
		awsAction.createBucket(getWebDriver(), bucketName, regionName);
		Thread.sleep(5000);
		awsAction.deletebucket(getWebDriver(), bucketName, region);
		
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "[ALERT] " + suiteData.getTestUsername() + 
				" attempted Activity: Create on Object type: Bucket name: " + bucketName +" using Platform: "+ platform+ ", Version: "
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
		String message = "User created a bucket "+bucketName+" in region "+regionCode;
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Bucket");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Create");
		expectedDataMap.put(GatewayTestConstants.AWS_REGION, regionCode);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, message);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	@Test(groups={"Regression", "Sanity"}, priority=10)
	public void testBlockEmptyBucket() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		protect.deletePolicyList(client, suiteData, policyList);
		ProtectDTO protectData = new ProtectDTO();
		String policyName = "POLICY_CREATE_BUCKET_BLOCK_AWS";
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
		protect.createAccessEnforcementPolicy(client, suiteData, protectData, "Bucket:Empty");
		
		//Empty bucket and verify the block
		String timeStamp = salesforceHomeAction.getTimestamp();
		bucketName = "bucket"+timeStamp;
		region = awsAction.createBucket(getWebDriver(), bucketName, regionName);
		Thread.sleep(5000);
		awsAction.emptybucket(getWebDriver(), bucketName, region);
		Thread.sleep(5000);
		boolean flag = clickOkInPopup();
		Assert.assertTrue(flag, "Popup not appeared");
		getWebDriver().navigate().refresh();
		Thread.sleep(5000);
		protect.deletePolicy(client, suiteData, policyName);
		Thread.sleep(5000);
		awsAction.emptybucket(getWebDriver(), bucketName, region);
		Thread.sleep(5000);
		awsAction.deletebucket(getWebDriver(), bucketName, region);
		
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "[ALERT] " + suiteData.getTestUsername() + 
				" attempted Activity: Empty on Object type: Bucket name: " + bucketName +" using Platform: "+ platform+ ", Version: "
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
		String message = "User emptied a bucket "+bucketName;
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Bucket");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Empty");
		expectedDataMap.put(GatewayTestConstants.FACILITY, suiteData.getSaasAppName());
		expectedDataMap.put(GatewayTestConstants.MESSAGE, message);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	@Test(groups={"Regression", "Sanity"}, priority=11)
	public void testBlockDeteleBucket() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		protect.deletePolicyList(client, suiteData, policyList);
		ProtectDTO protectData = new ProtectDTO();
		String policyName = "POLICY_CREATE_BUCKET_BLOCK_AWS";
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
		protect.createAccessEnforcementPolicy(client, suiteData, protectData, "Bucket:Delete");
		
		//Delete bucket and verify the block
		String timeStamp = salesforceHomeAction.getTimestamp();
		bucketName = "bucket"+timeStamp;
		region = awsAction.createBucket(getWebDriver(), bucketName, regionName);
		Thread.sleep(5000);
		awsAction.deletebucket(getWebDriver(), bucketName, region);
		Thread.sleep(5000);
		boolean flag = clickOkInPopup();
		Assert.assertTrue(flag, "Popup not appeared");
		getWebDriver().navigate().refresh();
		Thread.sleep(5000);
		protect.deletePolicy(client, suiteData, policyName);
		Thread.sleep(5000);
		awsAction.deletebucket(getWebDriver(), bucketName, region);
		
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "[ALERT] " + suiteData.getTestUsername() + 
				" attempted Activity: Delete on Object type: Bucket name: " + bucketName +" using Platform: "+ platform+ ", Version: "
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
		String message = "User deleted a bucket "+bucketName;
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Bucket");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Delete");
		expectedDataMap.put(GatewayTestConstants.FACILITY, suiteData.getSaasAppName());
		expectedDataMap.put(GatewayTestConstants.MESSAGE, message);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}

	@Test(groups={"Regression", "Sanity"}, priority=12)
	public void testBlockCreateFolder() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		protect.deletePolicyList(client, suiteData, policyList);
		ProtectDTO protectData = new ProtectDTO();
		String policyName = "POLICY_CREATE_FOLDER_BLOCK_AWS";
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
		protect.createAccessEnforcementPolicy(client, suiteData, protectData, "Folder:Create");
		
		//Create Folder and verify the block
		String timeStamp = salesforceHomeAction.getTimestamp();
		bucketName = "bucket"+timeStamp;
		folderName = "folder"+timeStamp;
		region = awsAction.createBucket(getWebDriver(), bucketName, regionName);
		awsAction.createFolder(getWebDriver(), bucketName, folderName, region);
		Thread.sleep(5000);
		boolean flag = clickOkInPopup();
		Assert.assertTrue(flag, "Popup not appeared");
		boolean flag1 = clickOkInPopup();
		Assert.assertTrue(flag1, "Popup not appeared");
		getWebDriver().navigate().refresh();
		Thread.sleep(5000);
		protect.deletePolicy(client, suiteData, policyName);
		getWebDriver().navigate().to(AwsConstants.AWS_S3_URL);
		Thread.sleep(5000);
		awsAction.switchtoFrame(getWebDriver(), region);
		awsAction.createFolder(getWebDriver(), bucketName, folderName, region);
		Thread.sleep(5000);
		getWebDriver().navigate().to(AwsConstants.AWS_S3_URL);
		awsAction.deletebucket(getWebDriver(), bucketName, region);
		
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "[ALERT] " + suiteData.getTestUsername() + 
				" attempted Activity: Create on Object type: Folder name: " + bucketName+"/"+folderName +" using Platform: "+ platform+ ", Version: "
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
		String message = "User created a folder "+bucketName+"/"+folderName;
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Folder");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Create");
		expectedDataMap.put(GatewayTestConstants.FACILITY, suiteData.getSaasAppName());
		expectedDataMap.put(GatewayTestConstants.MESSAGE, message);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	//@Test(groups={"Regression", "Sanity"}, priority=13)
	public void testBlockDeleteFolder() throws Exception{
		Map<String, Object> expectedDataMap = new HashMap<String, Object>();
		protect.deletePolicyList(client, suiteData, policyList);
		ProtectDTO protectData = new ProtectDTO();
		String policyName = "POLICY_DELETE_FOLDER_BLOCK_AWS";
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
		protect.createAccessEnforcementPolicy(client, suiteData, protectData, "Folder:Delete");
		
		//Delete Folder and verify the block
		String timeStamp = salesforceHomeAction.getTimestamp();
		bucketName = "bucket"+timeStamp;
		folderName = "folder"+timeStamp;
		region = awsAction.createBucket(getWebDriver(), bucketName, regionName);
		awsAction.createFolder(getWebDriver(), bucketName, folderName, region);
		awsAction.deleteFolder(getWebDriver(), folderName, suiteData);
		Thread.sleep(5000);
		protect.deletePolicy(client, suiteData, policyName);
		Thread.sleep(5000);
		awsAction.clickBucket(getWebDriver(), bucketName, region);
		awsAction.deleteFolder(getWebDriver(), folderName, suiteData);
		awsAction.deletebucket(getWebDriver(), bucketName, region);
		
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "[ALERT] " + suiteData.getTestUsername() + 
				" attempted Activity: Delete on Object type: Folder name: " + folderName +" using Platform: "+ platform+ ", Version: "
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
		String message = "User deleted folder(s) "+folderName;
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Folder");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Delete");
		expectedDataMap.put(GatewayTestConstants.FACILITY, suiteData.getSaasAppName());
		expectedDataMap.put(GatewayTestConstants.MESSAGE, message);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	@Test(groups={"Regression", "Sanity"}, priority=14, alwaysRun=true)
	public void deletePolicyList() throws Exception{
		protect.deletePolicyList(client, suiteData, policyList);
	}
}
