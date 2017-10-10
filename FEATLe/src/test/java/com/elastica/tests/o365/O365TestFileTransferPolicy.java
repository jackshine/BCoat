package com.elastica.tests.o365;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import com.elastica.action.protect.ProtectAction;
import com.elastica.action.protect.ProtectDTO;
import com.elastica.common.GWCommonTest;
import com.elastica.gateway.GatewayTestConstants;
import com.elastica.listeners.Priority;
import com.elastica.listeners.RetryAnalyzer;
import com.elastica.logger.Logger;

public class O365TestFileTransferPolicy extends GWCommonTest{
	Map <String, Object> expectedDataMap = new HashMap<String, Object>();
	Map<String, String>policyDataMap= new HashMap<String, String>(); 
	String fromTime=backend.getCurrentTime();
	SoftAssert softAssert = new SoftAssert();
	ProtectAction protectAction = new ProtectAction();
	ProtectDTO protectData = new ProtectDTO();
	CiqUtils utils=new CiqUtils();
	
	@BeforeMethod()
	public void clearDataMap(){
		expectedDataMap.clear();
	}

	@Priority(1)
	@Test(groups ={"Regression"}, retryAnalyzer=RetryAnalyzer.class)
	public void loginToCloudSocAppAndSetupSSO() throws Exception {
		Logger.info("==================================================================================");
		Logger.info(" Login to CloudSOC Portal");
		Logger.info("==================================================================================");
		printCredentials();
		login.loginCloudSocPortal(getWebDriver(), suiteData);
		Logger.info("==================================================================================");
		Logger.info(" Login to cloudSoc portal done");
		Logger.info("==================================================================================");
	}
	
	@Priority(2)
	@Test(groups ={"Regression", "REACH"},retryAnalyzer=RetryAnalyzer.class)  
	public void o365_Test_01_ValidateLoginActivityEvent() throws Exception {
		Logger.info("==================================================================================");
		Logger.info(" Verifying the login event to Saas App "+suiteData.getSaasAppName());
		Logger.info("==================================================================================");
		printCredentials();
		o365Login.login(getWebDriver(), suiteData);
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Login");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User logged in"); 
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Session");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),
				"Expected log: "+expectedDataMap.get(GatewayTestConstants.MESSAGE)+" does not exist on investigate");
		Logger.info("==================================================================================");
		Logger.info(" Login event verification successful");
		Logger.info("==================================================================================");
	}
	
	@Priority(3)
	@Test(groups ={"Regression", "REACH"}) 
	public void o365_Test_Upload_Policy_Without_Block() throws Exception {
		Logger.info("==================================================================================");
		Logger.info(" Upload file with  policy severity high ");
		Logger.info("==================================================================================");
		printCredentials();
		String fromTime=backend.getCurrentTime();
		protectAction.createEnableFileTransferPolicyWithUploadAndDownload(client, suiteData, 
				GatewayTestConstants.O365_POLICY_NAME + getSaasAppUserName(), "high");
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.loadOnedriveApp(getWebDriver());
		o365HomeAction.deleteFile(getWebDriver(), GatewayTestConstants.O365_UPLOAD_FILE);
		o365HomeAction.uploadItemFile(getWebDriver(),  
				System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+
				"resources"+File.separator+"meta"+File.separator + GatewayTestConstants.O365_UPLOAD_FILE);
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Policy Violation");
		expectedDataMap.put(GatewayTestConstants.ACTION_TAKEN, "");
		expectedDataMap.put(GatewayTestConstants.POLICY_ACTION, "ALERT");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "[ALERT] " + suiteData.getTestUsername() + 
				" attempted to upload content:" + GatewayTestConstants.O365_UPLOAD_FILE.toLowerCase() + 
				" violating policy:" + GatewayTestConstants.O365_POLICY_NAME + getSaasAppUserName());
		expectedDataMap.put(GatewayTestConstants.POLICY_TYPE, "FileTransfer");
		
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "high");
		expectedDataMap.remove(GatewayTestConstants.COUNTRY);
		expectedDataMap.remove(GatewayTestConstants.USER_NAME);
		protectAction.deletePolicy(client, suiteData, GatewayTestConstants.O365_POLICY_NAME + getSaasAppUserName());
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),
				"Expected log: "+expectedDataMap.get(GatewayTestConstants.MESSAGE)+" does not exist");
		Logger.info("==================================================================================");
		Logger.info(" Upload file with policy severity high successful");
		Logger.info("==================================================================================");
	}
	
	@Priority(4)
	@Test(groups ={"Regression", "REACH"}) 
	public void o365_Test_Upload_Policy_With_Block() throws Exception {
		Logger.info("==================================================================================");
		Logger.info(" Upload file with  policy severity high ");
		Logger.info("==================================================================================");
		printCredentials();
		String fromTime=backend.getCurrentTime();
		protectAction.createEnableFileTransferPolicyWithUploadAndDownloadBlock(client, suiteData, 
				GatewayTestConstants.O365_POLICY_NAME + getSaasAppUserName(), "high");
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.loadOnedriveApp(getWebDriver());
		o365HomeAction.deleteFile(getWebDriver(), GatewayTestConstants.O365_UPLOAD_FILE);
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.loadOnedriveApp(getWebDriver());
		o365HomeAction.uploadItemFileWithBlock(getWebDriver(),  
				System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+
				"resources"+File.separator+"meta"+File.separator + GatewayTestConstants.O365_UPLOAD_FILE);
		
		try {
			boolean value = clickOkInPopup();
			Assert.assertTrue(value, "Blocker Popup Not Found");
		} catch (Exception e) {
			Logger.info("Error " + e.getMessage());
		}
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Policy Violation");
		expectedDataMap.put(GatewayTestConstants.ACTION_TAKEN, "");
		expectedDataMap.put(GatewayTestConstants.POLICY_ACTION, "ALERT");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "[ALERT] " + suiteData.getTestUsername() + 
				" attempted to upload content:" + GatewayTestConstants.O365_UPLOAD_FILE.toLowerCase() + 
				" violating policy:" + GatewayTestConstants.O365_POLICY_NAME + getSaasAppUserName());
		expectedDataMap.put(GatewayTestConstants.POLICY_TYPE, "FileTransfer");
		
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "high");
		expectedDataMap.remove(GatewayTestConstants.COUNTRY);
		expectedDataMap.remove(GatewayTestConstants.USER_NAME);
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),
				"Expected log: "+expectedDataMap.get(GatewayTestConstants.MESSAGE)+" does not exist");
		Logger.info("==================================================================================");
		Logger.info(" Upload file with policy severity high successful");
		Logger.info("==================================================================================");
	}
	
	@Priority(5)
	@Test(groups ={"Regression" , "REACH"},retryAnalyzer=RetryAnalyzer.class) //,dependsOnMethods = { "o365_Test_01_ValidateLogingActivityEvent" }
	public void o365_Test_Upload_Policy_Deactivate_Policy() throws Exception {
		Logger.info("==================================================================================");
		Logger.info(" Verifying upload activity event for One Drive business");
		Logger.info("==================================================================================");
		printCredentials();
		protectAction.deActivatePolicy(client, suiteData, 
				GatewayTestConstants.O365_POLICY_NAME + getSaasAppUserName());

		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.loadOnedriveApp(getWebDriver());
		o365HomeAction.deleteFile(getWebDriver(), GatewayTestConstants.O365_UPLOAD_FILE);
		o365HomeAction.uploadItemFile(getWebDriver(),  System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+
				"resources"+File.separator+"meta"+File.separator + GatewayTestConstants.O365_UPLOAD_FILE);
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Upload");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User uploaded file named "+GatewayTestConstants.O365_UPLOAD_FILE);
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Document");
		expectedDataMap.put(GatewayTestConstants.OBJECT_NAME, "Hello.txt");
		expectedDataMap.put(GatewayTestConstants.FILE_SIZE, "3");
		expectedDataMap.put(GatewayTestConstants.CONTENT_INLINE, "1");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Expected log: "+expectedDataMap.get(GatewayTestConstants.MESSAGE)+" does not exist");
		Logger.info("==================================================================================");
		Logger.info(" Upload file event verification successful");
		Logger.info("==================================================================================");
	}
	
	
	@Priority(6)
	@Test(groups ={"Regression", "REACH"}) 
	public void o365_Test_Upload_Policy_With_Reactivate_Block() throws Exception {
		Logger.info("==================================================================================");
		Logger.info(" Upload file with  policy severity high ");
		Logger.info("==================================================================================");
		printCredentials();
		String fromTime=backend.getCurrentTime();

		protectAction.activatePolicy(client, suiteData, 
				GatewayTestConstants.O365_POLICY_NAME + getSaasAppUserName());
		
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.loadOnedriveApp(getWebDriver());
		o365HomeAction.deleteFile(getWebDriver(), GatewayTestConstants.O365_UPLOAD_FILE);
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.loadOnedriveApp(getWebDriver());
		o365HomeAction.uploadItemFileWithBlock(getWebDriver(),  
				System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+
				"resources"+File.separator+"meta"+File.separator + GatewayTestConstants.O365_UPLOAD_FILE);
		
		try {
			boolean value = clickOkInPopup();
			Assert.assertTrue(value, "Blocker Popup Not Found");
		} catch (Exception e) {
			Logger.info("Error " + e.getMessage());
		}
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Policy Violation");
		expectedDataMap.put(GatewayTestConstants.ACTION_TAKEN, "");
		expectedDataMap.put(GatewayTestConstants.POLICY_ACTION, "ALERT");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "[ALERT] " + suiteData.getTestUsername() + 
				" attempted to upload content:" + GatewayTestConstants.O365_UPLOAD_FILE.toLowerCase() + 
				" violating policy:" + GatewayTestConstants.O365_POLICY_NAME + getSaasAppUserName());
		expectedDataMap.put(GatewayTestConstants.POLICY_TYPE, "FileTransfer");
		
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "high");
		expectedDataMap.remove(GatewayTestConstants.COUNTRY);
		expectedDataMap.remove(GatewayTestConstants.USER_NAME);
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),
				"Expected log: "+expectedDataMap.get(GatewayTestConstants.MESSAGE)+" does not exist");
		Logger.info("==================================================================================");
		Logger.info(" Upload file with policy severity high successful");
		Logger.info("==================================================================================");
	}
	
	
	@Priority(7)
	@Test(groups ={"Regression", "REACH"}) 
	public void o365_Test_Upload_Policy_With_Block_For1MB_LessThanMBFile() throws Exception {
		Logger.info("==================================================================================");
		Logger.info(" Upload file with  policy severity high ");
		Logger.info("==================================================================================");
		printCredentials();
		String fromTime=backend.getCurrentTime();
		protectAction.deletePolicy(client, suiteData, GatewayTestConstants.O365_POLICY_NAME + getSaasAppUserName());
		protectAction.createEnableFileTransferPolicyWithUploadAndDownloadBlockWithFileSize(client, suiteData, 
				GatewayTestConstants.O365_POLICY_NAME + getSaasAppUserName(), "high", 0, 1048576);
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.loadOnedriveApp(getWebDriver());
		o365HomeAction.deleteFile(getWebDriver(), GatewayTestConstants.O365_UPLOAD_FILE);
		o365HomeAction.uploadItemFileWithBlock(getWebDriver(),  System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+
				"resources"+File.separator+"meta"+File.separator + GatewayTestConstants.O365_UPLOAD_FILE);
		
		try {
			boolean value = clickOkInPopup();
			Assert.assertTrue(value, "Blocker Popup Not Found");
		} catch (Exception e) {
			Logger.info("Error " + e.getMessage());
		}
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Policy Violation");
		expectedDataMap.put(GatewayTestConstants.ACTION_TAKEN, "");
		expectedDataMap.put(GatewayTestConstants.POLICY_ACTION, "ALERT");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "[ALERT] " + suiteData.getTestUsername() + 
				" attempted to upload content:" + GatewayTestConstants.O365_UPLOAD_FILE.toLowerCase() + 
				" violating policy:" + GatewayTestConstants.O365_POLICY_NAME + getSaasAppUserName());
		expectedDataMap.put(GatewayTestConstants.POLICY_TYPE, "FileTransfer");
		
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "high");
		expectedDataMap.remove(GatewayTestConstants.COUNTRY);
		expectedDataMap.remove(GatewayTestConstants.USER_NAME);
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),
				"Expected log: "+expectedDataMap.get(GatewayTestConstants.MESSAGE)+" does not exist");
		
	}
	
	@Priority(8)
	@Test(groups ={"Regression", "REACH"}) 
	public void o365_Test_Upload_Policy_With_Block_For1MB_MoreThan1MBFile() throws Exception {
		protectAction.deletePolicy(client, suiteData, GatewayTestConstants.O365_POLICY_NAME + getSaasAppUserName());
		protectAction.createEnableFileTransferPolicyWithUploadAndDownloadBlockWithFileSize(client, suiteData, 
				GatewayTestConstants.O365_POLICY_NAME + getSaasAppUserName(), "high", 0, 1048576);
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.loadOnedriveApp(getWebDriver());
		o365HomeAction.deleteFile(getWebDriver(), GatewayTestConstants.O365_UPLOAD_FILE_6MB);
		o365HomeAction.uploadItemFile(getWebDriver(),  System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+
				"resources"+File.separator+"meta"+File.separator + GatewayTestConstants.O365_UPLOAD_FILE_6MB);
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.loadOnedriveApp(getWebDriver());
		o365HomeAction.deleteFile(getWebDriver(), GatewayTestConstants.O365_UPLOAD_FILE_6MB);
		
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.remove(GatewayTestConstants.COUNTRY);
		expectedDataMap.remove(GatewayTestConstants.USER_NAME);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User uploaded file named "+GatewayTestConstants.O365_UPLOAD_FILE_6MB);
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	
	@Priority(9)
	@Test(groups ={"Regression", "REACH"}) 
	public void o365_Test_Download_Policy_Without_Block() throws Exception {
		Logger.info("==================================================================================" );
		Logger.info(" Download file with policy severity high ");
		Logger.info("==================================================================================" );
		printCredentials();
		fromTime=backend.getCurrentTime();

		protectAction.createEnableFileTransferPolicyWithUploadAndDownload(client, suiteData, 
				GatewayTestConstants.O365_POLICY_NAME + getSaasAppUserName(), "high");
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.loadOnedriveApp(getWebDriver());
		o365HomeAction.downloadItemFileForBlock(getWebDriver(), GatewayTestConstants.O365_FILE);
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Policy Violation");
		expectedDataMap.put(GatewayTestConstants.ACTION_TAKEN, "");
		expectedDataMap.put(GatewayTestConstants.POLICY_ACTION, "ALERT");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "[ALERT] " + suiteData.getTestUsername() + 
				" attempted to download content:" + GatewayTestConstants.O365_FILE.toLowerCase() + 
				" violating policy:" + GatewayTestConstants.O365_POLICY_NAME + getSaasAppUserName());
		expectedDataMap.put(GatewayTestConstants.POLICY_TYPE, "FileTransfer");
		
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "high");
		expectedDataMap.remove(GatewayTestConstants.COUNTRY);
		expectedDataMap.remove(GatewayTestConstants.USER_NAME);
		protectAction.deletePolicy(client, suiteData, GatewayTestConstants.O365_POLICY_NAME + getSaasAppUserName());
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),
				"Expected log: "+expectedDataMap.get(GatewayTestConstants.MESSAGE)+" does not exist");
		Logger.info("==================================================================================");
		Logger.info(" Download file with policy severity high successful");
		Logger.info("==================================================================================");
	}
	
	@Priority(10)
	@Test(groups ={"Regression", "REACH"}) 
	public void o365_Test_Download_Policy_With_Block() throws Exception {
		Logger.info("==================================================================================" );
		Logger.info(" Download file with policy severity high ");
		Logger.info("==================================================================================" );
		printCredentials();
		fromTime=backend.getCurrentTime();
		protectAction.deletePolicy(client, suiteData, GatewayTestConstants.O365_POLICY_NAME + getSaasAppUserName());
		protectAction.createEnableFileTransferPolicyWithUploadAndDownloadBlock(client, suiteData, 
				GatewayTestConstants.O365_POLICY_NAME + getSaasAppUserName(), "high");
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.loadOnedriveApp(getWebDriver());
		o365HomeAction.downloadItemFileForBlock(getWebDriver(), GatewayTestConstants.O365_FILE);
		try {
			boolean value = clickOkInPopup();
			Assert.assertTrue(value, "Blocker Popup Not Found");
		} catch (Exception e) {
			Logger.info("Error " + e.getMessage());
		}
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Policy Violation");
		expectedDataMap.put(GatewayTestConstants.ACTION_TAKEN, "");
		expectedDataMap.put(GatewayTestConstants.POLICY_ACTION, "ALERT");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "[ALERT] " + suiteData.getTestUsername() + 
				" attempted to download content:" + GatewayTestConstants.O365_FILE.toLowerCase() + 
				" violating policy:" + GatewayTestConstants.O365_POLICY_NAME + getSaasAppUserName());
		expectedDataMap.put(GatewayTestConstants.POLICY_TYPE, "FileTransfer");
		
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "high");
		expectedDataMap.remove(GatewayTestConstants.COUNTRY);
		expectedDataMap.remove(GatewayTestConstants.USER_NAME);
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),
				"Expected log: "+expectedDataMap.get(GatewayTestConstants.MESSAGE)+" does not exist");
		Logger.info("==================================================================================");
		Logger.info(" Download file with policy severity high successful");
		Logger.info("==================================================================================");
	}
	
	@Priority(11)
	@Test(groups ={"Regression", "REACH"}) 
	public void o365_Test_Download_Deacative_Policy() throws Exception {
		Logger.info("==================================================================================" );
		Logger.info(" Verifying download activity event");
		Logger.info("==================================================================================" );
		printCredentials();
		fromTime=backend.getCurrentTime();
		protectAction.deActivatePolicy(client, suiteData, 
				GatewayTestConstants.O365_POLICY_NAME + getSaasAppUserName());
		
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.loadOnedriveApp(getWebDriver());
		o365HomeAction.downloadItemFileForBlock(getWebDriver(), GatewayTestConstants.O365_FILE);
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Download");
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Document");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User downloaded " + GatewayTestConstants.O365_FILE);
		expectedDataMap.put(GatewayTestConstants.FILE_SIZE, "287");
		expectedDataMap.put(GatewayTestConstants.OBJECT_NAME, "Dial-FAQ.pdf");
		expectedDataMap.put(GatewayTestConstants.CONTENT_INLINE, "1");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Expected log: "+expectedDataMap.get(GatewayTestConstants.MESSAGE)+" does not exist");
		Logger.info("==================================================================================" );
		Logger.info("Download activity event verification successful");
		Logger.info("==================================================================================" );
	}
	
	@Priority(12)
	@Test(groups ={"Regression", "REACH"}) 
	public void o365_Test_Download_Policy_With_Reactivate_Block() throws Exception {
		Logger.info("==================================================================================" );
		Logger.info(" Download file with policy severity high ");
		Logger.info("==================================================================================" );
		printCredentials();
		fromTime=backend.getCurrentTime();

		protectAction.activatePolicy(client, suiteData, 
				GatewayTestConstants.O365_POLICY_NAME + getSaasAppUserName());
		
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.loadOnedriveApp(getWebDriver());
		o365HomeAction.downloadItemFileForBlock(getWebDriver(), GatewayTestConstants.O365_FILE);
		try {
			boolean value = clickOkInPopup();
			Assert.assertTrue(value, "Blocker Popup Not Found");
		} catch (Exception e) {
			Logger.info("Error " + e.getMessage());
		}
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Policy Violation");
		expectedDataMap.put(GatewayTestConstants.ACTION_TAKEN, "");
		expectedDataMap.put(GatewayTestConstants.POLICY_ACTION, "ALERT");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "[ALERT] " + suiteData.getTestUsername() + 
				" attempted to download content:" + GatewayTestConstants.O365_FILE.toLowerCase() + 
				" violating policy:" + GatewayTestConstants.O365_POLICY_NAME + getSaasAppUserName());
		expectedDataMap.put(GatewayTestConstants.POLICY_TYPE, "FileTransfer");
		
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "high");
		expectedDataMap.remove(GatewayTestConstants.COUNTRY);
		expectedDataMap.remove(GatewayTestConstants.USER_NAME);
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),
				"Expected log: "+expectedDataMap.get(GatewayTestConstants.MESSAGE)+" does not exist");
		Logger.info("==================================================================================");
		Logger.info(" Download file with policy severity high successful");
		Logger.info("==================================================================================");
	}
	
	@Priority(13)
	@Test(groups ={"Regression", "REACH"}) 
	public void o365_Test_Download_Policy_With_For1MB_LessThanMBFile() throws Exception {
		Logger.info("==================================================================================" );
		Logger.info(" Download file with policy severity high ");
		Logger.info("==================================================================================" );
		printCredentials();
		fromTime=backend.getCurrentTime();

		protectAction.deletePolicy(client, suiteData, GatewayTestConstants.O365_POLICY_NAME + getSaasAppUserName());
		protectAction.createEnableFileTransferPolicyWithUploadAndDownloadBlockWithFileSize(client, suiteData, 
				GatewayTestConstants.O365_POLICY_NAME + getSaasAppUserName(), "high", 0, 1048576);

		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.loadOnedriveApp(getWebDriver());
		o365HomeAction.downloadItemFileForBlock(getWebDriver(), GatewayTestConstants.O365_FILE);
		try {
			boolean value = clickOkInPopup();
			Assert.assertTrue(value, "Blocker Popup Not Found");
		} catch (Exception e) {
			Logger.info("Error " + e.getMessage());
		}
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Policy Violation");
		expectedDataMap.put(GatewayTestConstants.ACTION_TAKEN, "");
		expectedDataMap.put(GatewayTestConstants.POLICY_ACTION, "ALERT");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "[ALERT] " + suiteData.getTestUsername() + 
				" attempted to download content:" + GatewayTestConstants.O365_FILE.toLowerCase() + 
				" violating policy:" + GatewayTestConstants.O365_POLICY_NAME + getSaasAppUserName());
		expectedDataMap.put(GatewayTestConstants.POLICY_TYPE, "FileTransfer");
		
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "high");
		expectedDataMap.remove(GatewayTestConstants.COUNTRY);
		expectedDataMap.remove(GatewayTestConstants.USER_NAME);
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),
				"Expected log: "+expectedDataMap.get(GatewayTestConstants.MESSAGE)+" does not exist");
		Logger.info("==================================================================================");
		Logger.info(" Download file with policy severity high successful");
		Logger.info("==================================================================================");
	}
	
	@Priority(14)
	@Test(groups ={"Regression", "REACH"}) 
	public void o365_Test_Download_Policy_With_For1MB_MoreThanMBFile() throws Exception {
		printCredentials();
		protectAction.deletePolicy(client, suiteData, GatewayTestConstants.O365_POLICY_NAME + getSaasAppUserName());
		protectAction.createEnableFileTransferPolicyWithUploadAndDownloadBlockWithFileSize(client, suiteData, 
				GatewayTestConstants.O365_POLICY_NAME + getSaasAppUserName(), "high", 0, 1048576);
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.loadOnedriveApp(getWebDriver());
		o365HomeAction.deleteFile(getWebDriver(), GatewayTestConstants.O365_UPLOAD_FILE_6MB);
		o365HomeAction.uploadItemFile(getWebDriver(),  System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+
				"resources"+File.separator+"meta"+File.separator + GatewayTestConstants.O365_UPLOAD_FILE_6MB);
		
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.loadOnedriveApp(getWebDriver());
		o365HomeAction.downloadItemFileForBlock(getWebDriver(), GatewayTestConstants.O365_UPLOAD_FILE_6MB);
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User downloaded " + GatewayTestConstants.O365_UPLOAD_FILE_6MB);
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Expected log: "+expectedDataMap.get(GatewayTestConstants.MESSAGE)+" does not exist");
		Logger.info("==================================================================================" );
		Logger.info("Download activity event verification successful");
		Logger.info("==================================================================================" );
		
	}
	
	@Priority(15)
	@Test(groups ={"Regression", "REACH"}) 
	public void o365_Test_Upload_Encryption_And_Block_Policy() throws Exception {
		Logger.info("==================================================================================");
		Logger.info(" Verifying encrypt upload activity with block event");
		Logger.info("==================================================================================");
		printCredentials();
		fromTime=backend.getCurrentTime();
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME+getSaasAppUserName(),
				suiteData, backend.getHeaders(suiteData));
		protectAction.deletePolicy(client, suiteData, 
				GatewayTestConstants.O365_POLICY_NAME + getSaasAppUserName());
		
		protectAction.createEnableFileTransferPolicyWithUploadAndDownloadBlock(client, suiteData, 
				GatewayTestConstants.O365_POLICY_NAME + getSaasAppUserName(), "high");
		policy.createEnableEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME+getSaasAppUserName(),
				suiteData, backend.getHeaders(suiteData));
		
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.loadOnedriveApp(getWebDriver());
		o365HomeAction.deleteFile(getWebDriver(), GatewayTestConstants.O365_UPLOAD_FILE);
		
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.loadOnedriveApp(getWebDriver());
		o365HomeAction.uploadItemFileWithBlock(getWebDriver(),  
				System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+
				"resources"+File.separator+"meta"+File.separator + GatewayTestConstants.O365_UPLOAD_FILE);
		
		try {
			boolean value = clickOkInPopup();
			Assert.assertTrue(value, "Blocker Popup Not Found");
		} catch (Exception e) {
			Logger.info("Error " + e.getMessage());
		}
	
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Policy Violation");
		expectedDataMap.put(GatewayTestConstants.ACTION_TAKEN, "");
		expectedDataMap.put(GatewayTestConstants.POLICY_ACTION, "ALERT");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "[ALERT] " + suiteData.getTestUsername() + 
				" attempted to upload content:" + GatewayTestConstants.O365_UPLOAD_FILE.toLowerCase() + 
				" violating policy:" + GatewayTestConstants.O365_POLICY_NAME + getSaasAppUserName());
		expectedDataMap.put(GatewayTestConstants.POLICY_TYPE, "FileTransfer");
		
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "high");
		expectedDataMap.remove(GatewayTestConstants.COUNTRY);
		expectedDataMap.remove(GatewayTestConstants.USER_NAME);
		
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME+getSaasAppUserName(),
				suiteData, backend.getHeaders(suiteData));
		protectAction.deletePolicy(client,
				suiteData, GatewayTestConstants.O365_POLICY_NAME + getSaasAppUserName());

		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),
				"Expected log: "+expectedDataMap.get(GatewayTestConstants.MESSAGE)+" does not exist");
		Logger.info("==================================================================================");
		Logger.info(" Verifying encrypt upload activity with block event successful");
		Logger.info("==================================================================================");
	}
	
	@Priority(16)
	@Test(groups ={"Regression", "REACH"}) 
	public void o365_Test_Download_Decryption_And_Block_Policy() throws Exception {
		Logger.info("==================================================================================" );
		Logger.info(" Download file with Decrypton And Block policy severity high ");
		Logger.info("==================================================================================" );
		printCredentials();
		fromTime=backend.getCurrentTime();
		protectAction.deletePolicy(client, suiteData, GatewayTestConstants.O365_POLICY_NAME + getSaasAppUserName());
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME+getSaasAppUserName(),
				suiteData, backend.getHeaders(suiteData));
	
		protectAction.createEnableFileTransferPolicyWithUploadAndDownloadBlock(client, suiteData, 
				GatewayTestConstants.O365_POLICY_NAME + getSaasAppUserName(), "high");
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.loadOnedriveApp(getWebDriver());
		o365HomeAction.downloadItemFileForBlock(getWebDriver(), GatewayTestConstants.O365_FILE);
		try {
			boolean value = clickOkInPopup();
			Assert.assertTrue(value, "Blocker Popup Not Found");
		} catch (Exception e) {
			Logger.info("Error " + e.getMessage());
		}
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Policy Violation");
		expectedDataMap.put(GatewayTestConstants.ACTION_TAKEN, "");
		expectedDataMap.put(GatewayTestConstants.POLICY_ACTION, "ALERT");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "[ALERT] " + suiteData.getTestUsername() + 
				" attempted to download content:" + GatewayTestConstants.O365_FILE.toLowerCase() + 
				" violating policy:" + GatewayTestConstants.O365_POLICY_NAME + getSaasAppUserName());
		expectedDataMap.put(GatewayTestConstants.POLICY_TYPE, "FileTransfer");
		
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "high");
		expectedDataMap.remove(GatewayTestConstants.COUNTRY);
		expectedDataMap.remove(GatewayTestConstants.USER_NAME);
		
		protectAction.deletePolicy(client, suiteData, GatewayTestConstants.O365_POLICY_NAME + getSaasAppUserName());
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME+getSaasAppUserName(),
				suiteData, backend.getHeaders(suiteData));
		
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),
				"Expected log: "+expectedDataMap.get(GatewayTestConstants.MESSAGE)+" does not exist");
		Logger.info("==================================================================================");
		Logger.info(" Download file with policy severity high successful");
		Logger.info("==================================================================================");
	}
	
	@Priority(17)
	@Test(groups ={"Regression", "REACH"}) 
	public void o365_Test_Upload_File_Block_With_File_Format_Policy() throws Exception {
		utils.createCIQProfile(CIQConstants.ZERO, suiteData, GatewayTestConstants.CIQWORDBLOCK, 
				GatewayTestConstants.CIQWORDBLOCK, "DCI_FILE_FORMAT","class:WORDPROCESSOR");
		Logger.info("==================================================================================" );
		Logger.info(" Download file with Decrypton And Block policy severity high ");
		Logger.info("==================================================================================" );
		printCredentials();
		fromTime=backend.getCurrentTime();
		protectAction.deletePolicy(client, suiteData, GatewayTestConstants.O365_POLICY_NAME + getSaasAppUserName());
		protectAction.createEnableFileTransferPolicyWithUploadAndDownloadBlockByFileFormatProfile(client, suiteData, 
				GatewayTestConstants.O365_POLICY_NAME + getSaasAppUserName(), "high", GatewayTestConstants.CIQWORDBLOCK);
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.loadOnedriveApp(getWebDriver());
		o365HomeAction.deleteFile(getWebDriver(), GatewayTestConstants.O365_FILE_DOC);

		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.loadOnedriveApp(getWebDriver());
		o365HomeAction.uploadItemFileWithBlock(getWebDriver(),  
				System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+
				"resources"+File.separator+"meta"+File.separator + GatewayTestConstants.O365_FILE_DOC);
		
		try {
			boolean value = clickOkInPopup();
			Assert.assertTrue(value, "Blocker Popup Not Found");
		} catch (Exception e) {
			Logger.info("Error " + e.getMessage());
		}
	
		expectedDataMap.clear();
		//setCommonFieldsInExpectedDataMap(expectedDataMap);
		//setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Policy Violation");
		expectedDataMap.put(GatewayTestConstants.ACTION_TAKEN, "block");
		expectedDataMap.put(GatewayTestConstants.NAME, GatewayTestConstants.O365_FILE_DOC);
		expectedDataMap.put(GatewayTestConstants.POLICY_ACTION, "ALERT");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "[ALERT] File " +GatewayTestConstants.O365_FILE_DOC.toLowerCase() 
		+ " upload violated policy - " + GatewayTestConstants.O365_POLICY_NAME + getSaasAppUserName());
		expectedDataMap.put(GatewayTestConstants.POLICY_TYPE, "FileTransfer");
		
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "high");
		expectedDataMap.remove(GatewayTestConstants.COUNTRY);
		expectedDataMap.remove(GatewayTestConstants.USER_NAME);

		protectAction.deletePolicy(client,
				suiteData, GatewayTestConstants.O365_POLICY_NAME + getSaasAppUserName());

		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),
				"Expected log: "+expectedDataMap.get(GatewayTestConstants.MESSAGE)+" does not exist");
		Logger.info("==================================================================================");
		Logger.info(" Verifying encrypt upload activity with block event successful");
		Logger.info("==================================================================================");
		/* Need to check, because country code is missing */
	}
	
	
	@Priority(18)
	@Test(groups ={"Regression", "REACH"}) 
	public void o365_Test_Download_File_Block_With_File_Format_Policy() throws Exception {
		utils.createCIQProfile(CIQConstants.ZERO, suiteData, GatewayTestConstants.CIQWORDBLOCK, 
				GatewayTestConstants.CIQWORDBLOCK, "DCI_FILE_FORMAT","class:WORDPROCESSOR");
		printCredentials();
		Logger.info("==================================================================================" );
		Logger.info(" Download file with Decrypton And Block policy severity high ");
		Logger.info("==================================================================================" );
		fromTime=backend.getCurrentTime();
		protectAction.deletePolicy(client, suiteData, GatewayTestConstants.O365_POLICY_NAME + getSaasAppUserName());
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.loadOnedriveApp(getWebDriver());
		o365HomeAction.deleteFile(getWebDriver(), GatewayTestConstants.O365_FILE_DOC);
		o365HomeAction.uploadItemFileWithBlock(getWebDriver(),  
				System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+
				"resources"+File.separator+"meta"+File.separator + GatewayTestConstants.O365_FILE_DOC);
		
		protectAction.createEnableFileTransferPolicyWithUploadAndDownloadBlockByFileFormatProfile(client, suiteData, 
				GatewayTestConstants.O365_POLICY_NAME + getSaasAppUserName(), "high", GatewayTestConstants.CIQWORDBLOCK);
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.loadOnedriveApp(getWebDriver());
		o365HomeAction.downloadItemFileForBlock(getWebDriver(), GatewayTestConstants.O365_FILE_DOC);
		try {
			boolean value = clickOkInPopup();
			Assert.assertTrue(value, "Blocker Popup Not Found");
		} catch (Exception e) {
			Logger.info("Error " + e.getMessage());
		}
		expectedDataMap.clear();
		//setCommonFieldsInExpectedDataMap(expectedDataMap);
		//setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Policy Violation");
		expectedDataMap.put(GatewayTestConstants.ACTION_TAKEN, "block");
		expectedDataMap.put(GatewayTestConstants.NAME, GatewayTestConstants.O365_FILE_DOC);
		expectedDataMap.put(GatewayTestConstants.POLICY_ACTION, "ALERT");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "[ALERT] File " +GatewayTestConstants.O365_FILE_DOC.toLowerCase() + 
				" download violated policy - " + GatewayTestConstants.O365_POLICY_NAME + getSaasAppUserName());
		expectedDataMap.put(GatewayTestConstants.POLICY_TYPE, "FileTransfer");
		
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "high");
		expectedDataMap.remove(GatewayTestConstants.COUNTRY);
		expectedDataMap.remove(GatewayTestConstants.USER_NAME);
		
		protectAction.deletePolicy(client, suiteData, GatewayTestConstants.O365_POLICY_NAME + getSaasAppUserName());
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),
				"Expected log: "+expectedDataMap.get(GatewayTestConstants.MESSAGE)+" does not exist");
		Logger.info("==================================================================================");
		Logger.info(" Download file with policy severity high successful");
		Logger.info("==================================================================================");
	}
	
	@Priority(19)
	@Test(groups ={"Regression", "REACH"}) 
	public void o365_Test_ValidateLogoutActivityEvent() throws Exception {
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365Login.logout(getWebDriver());
		Logger.info("==================================================================================");
		Logger.info(" Verifying the logout event");
		Logger.info("==================================================================================");
		printCredentials();
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User logged out");
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Session");
		expectedDataMap.put(GatewayTestConstants.REFERER_URI, "");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),
				"Expected log: "+expectedDataMap.get(GatewayTestConstants.MESSAGE)+" does not exist");
		Logger.info("==================================================================================");
		Logger.info(" Logout event verification successful");
		Logger.info("==================================================================================");
	}
	
	public String getSaasAppUserName(){
		return suiteData.getSaasAppUsername().replaceAll("@", "_");
	}
	
	@BeforeClass(groups ={"Regression", "REACH"})
	public void doBeforeClass() throws Exception {
		try {
			Logger.info("Delete Policy Before Test ");
			protectData.setPolicyName(GatewayTestConstants.O365_POLICY_NAME + getSaasAppUserName());
			protectAction.deletePolicy(client, suiteData, GatewayTestConstants.O365_POLICY_NAME + getSaasAppUserName());
			Logger.info(" policy to delete");
		} catch(Exception e) {
			Logger.info("No policy to delete");
		}
	}
	
	@AfterClass(groups ={"Regression", "REACH"})
	public void doAfterClass() throws Exception {
		try {
			Logger.info("Delete Policy After Test ");
			protectData.setPolicyName(GatewayTestConstants.O365_POLICY_NAME + getSaasAppUserName());
			protectAction.deletePolicy(client, suiteData, GatewayTestConstants.O365_POLICY_NAME + getSaasAppUserName());
			Logger.info(" policy to delete");
		} catch(Exception e) {
			Logger.info("No policy to delete");
		}
	}

}