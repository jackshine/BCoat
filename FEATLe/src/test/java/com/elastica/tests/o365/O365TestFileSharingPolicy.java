package com.elastica.tests.o365;

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


public class O365TestFileSharingPolicy extends GWCommonTest{
	Map <String, Object> expectedDataMap = new HashMap<String, Object>();
	Map<String, String>policyDataMap= new HashMap<String, String>(); 
	String fromTime=backend.getCurrentTime();
	SoftAssert softAssert = new SoftAssert();
	ProtectAction protectAction = new ProtectAction();
	ProtectDTO protectData = new ProtectDTO();
	
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
	public void o365_Test_ValidateLoginActivityEvent() throws Exception {
		Logger.info("==================================================================================");
		Logger.info(" Verifying the login event to Saas App "+suiteData.getSaasAppName());
		Logger.info("==================================================================================");
		printCredentials();
		o365Login.login(getWebDriver(), suiteData);
		expectedDataMap.clear();
		/*Fields to check*/
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Login");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User logged in"); 
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Session");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Expected log: "+expectedDataMap.get(GatewayTestConstants.MESSAGE)+" does not exist on investigate");
		Logger.info("==================================================================================");
		Logger.info(" Login event verification successful");
		Logger.info("==================================================================================");
	}
	
	@Priority(3)
	@Test(groups ={"Regression", "REACH"}) 
	public void o365_Test_File_Sharing_Via_Email_Without_Block() throws Exception {
		Logger.info("==================================================================================");
		Logger.info(" Verifying Share folder via Email");
		Logger.info("==================================================================================");
		printCredentials();
		protectAction.deletePolicy(client, suiteData, GatewayTestConstants.O365_SHARING_POLICY + getSaasAppUserName());
		protectAction.createEnableFileSharingPolicy(client, suiteData, 
				GatewayTestConstants.O365_SHARING_POLICY + getSaasAppUserName(), "high");

		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.loadOnedriveApp(getWebDriver());
		o365HomeAction.deleteFile(getWebDriver(), GatewayTestConstants.O365_CREATE_FOLDER);
		o365HomeAction.createFolder(getWebDriver(),  GatewayTestConstants.O365_CREATE_FOLDER);
		o365HomeAction.shareItemViaEmail(getWebDriver(), GatewayTestConstants.O365_CREATE_FOLDER, suiteData.getUsername());
		o365HomeAction.deleteFile(getWebDriver(), GatewayTestConstants.O365_CREATE_FOLDER);

		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Policy Violation");
		expectedDataMap.put(GatewayTestConstants.ACTION_TAKEN, "");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "[ALERT] " + suiteData.getTestUsername() + 
				" attempted to share content:newfolderwith external user(s):"+ suiteData.getUsername() 
				+ " violating policy:" + GatewayTestConstants.O365_SHARING_POLICY + getSaasAppUserName()); 
		expectedDataMap.put(GatewayTestConstants.POLICY_ACTION, "ALERT");
		expectedDataMap.put(GatewayTestConstants.POLICY_TYPE, "FileSharingGateway");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "high");
		expectedDataMap.remove(GatewayTestConstants.COUNTRY);
		expectedDataMap.remove(GatewayTestConstants.USER_NAME);
		
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),
				"Expected log: "+expectedDataMap.get(GatewayTestConstants.MESSAGE)+" does not exist on investigate");
		
		Logger.info("==================================================================================");
		Logger.info(" Share event via email validation done");
		Logger.info("==================================================================================");
		
	}
	
	@Priority(4)
	@Test(groups ={"Regression", "REACH"})  
	public void o365_Test_File_Sharing_Via_Email_Block_Policy() throws Exception {
		Logger.info("==================================================================================");
		Logger.info(" Verifying Share folder via Email");
		Logger.info("==================================================================================");
		printCredentials();
		protectAction.deletePolicy(client, suiteData, 
				GatewayTestConstants.O365_SHARING_POLICY + getSaasAppUserName());
		Logger.info("Creating File Shareing Policy, User " +suiteData.getTestUsername() + " Receipt " +  suiteData.getUsername());
		protectAction.createEnableFileSharingPolicyByShare(client, suiteData, 
				GatewayTestConstants.O365_SHARING_POLICY + getSaasAppUserName(),  suiteData.getUsername(), "high");
		
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.loadOnedriveApp(getWebDriver());
		o365HomeAction.deleteFile(getWebDriver(), GatewayTestConstants.O365_CREATE_FOLDER);
		o365HomeAction.createFolder(getWebDriver(),  GatewayTestConstants.O365_CREATE_FOLDER);
		
		o365HomeAction.shareItemViaEmail(getWebDriver(), GatewayTestConstants.O365_CREATE_FOLDER, suiteData.getUsername());
		try {
			boolean value = clickOkInPopup();
			Assert.assertTrue(value, "Blocker Popup Not Found");
		} catch (Exception e) {
			Logger.info("Error " + e.getMessage());
		}
		
		o365HomeAction.deleteFile(getWebDriver(), GatewayTestConstants.O365_CREATE_FOLDER);

		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Policy Violation");
		expectedDataMap.put(GatewayTestConstants.ACTION_TAKEN, "");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "[ALERT] " + suiteData.getTestUsername() + 
				" attempted to share content:newfolderwith external user(s):"+ suiteData.getUsername() 
				+ " violating policy:" + GatewayTestConstants.O365_SHARING_POLICY + getSaasAppUserName()); 
		expectedDataMap.put(GatewayTestConstants.POLICY_ACTION, "ALERT");
		expectedDataMap.put(GatewayTestConstants.POLICY_TYPE, "FileSharingGateway");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "high");
		expectedDataMap.remove(GatewayTestConstants.COUNTRY);
		expectedDataMap.remove(GatewayTestConstants.USER_NAME);
		
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),
				"Expected log: "+expectedDataMap.get(GatewayTestConstants.MESSAGE)+" does not exist on investigate");
		
		Logger.info("==================================================================================");
		Logger.info(" Share event via email validation done");
		Logger.info("==================================================================================");
	}
	
	@Priority(5)
	@Test(groups ={"Regression", "REACH"}) 
	public void o365_Test_File_Sharing_Via_Email_Deactivate_Policy() throws Exception {
		Logger.info("==================================================================================");
		Logger.info(" Verifying Share folder via Email");
		Logger.info("==================================================================================");
		printCredentials();
		protectAction.deActivatePolicy(client, suiteData, 
				GatewayTestConstants.O365_SHARING_POLICY + getSaasAppUserName());

		Logger.info("==================================================================================");
		Logger.info(" Verifying Share folder via Email");
		Logger.info("==================================================================================");
		
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.loadOnedriveApp(getWebDriver());
		o365HomeAction.deleteFile(getWebDriver(), GatewayTestConstants.O365_CREATE_FOLDER);
		o365HomeAction.createFolder(getWebDriver(),  GatewayTestConstants.O365_CREATE_FOLDER);
		o365HomeAction.shareItemViaEmail(getWebDriver(), GatewayTestConstants.O365_CREATE_FOLDER, suiteData.getTestUsername());
		o365HomeAction.deleteFile(getWebDriver(), GatewayTestConstants.O365_CREATE_FOLDER);
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Share");
		expectedDataMap.put(GatewayTestConstants.MESSAGE,  "User sent email invitation(s) to " 
		+ suiteData.getTestUsername() + " for " + GatewayTestConstants.O365_CREATE_FOLDER + ".");
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Folder");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.OBJECT_NAME, "NewFolder");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("==================================================================================");
		Logger.info(" Share event via email validation done");
		Logger.info("==================================================================================");
		
	}
	
	@Priority(6)
	@Test(groups ={"Regression", "REACH"})  
	public void o365_Test_File_Sharing_Via_Email_Reactivate_Block_Policy() throws Exception {
		Logger.info("==================================================================================");
		Logger.info(" Verifying Share folder via Email");
		Logger.info("==================================================================================");
		printCredentials();
		protectAction.activatePolicy(client, suiteData, 
				GatewayTestConstants.O365_SHARING_POLICY + getSaasAppUserName());
		
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.loadOnedriveApp(getWebDriver());
		o365HomeAction.deleteFile(getWebDriver(), GatewayTestConstants.O365_CREATE_FOLDER);
		o365HomeAction.createFolder(getWebDriver(),  GatewayTestConstants.O365_CREATE_FOLDER);
		
		o365HomeAction.shareItemViaEmail(getWebDriver(), GatewayTestConstants.O365_CREATE_FOLDER, suiteData.getUsername());
		try {
			boolean value = clickOkInPopup();
			Assert.assertTrue(value, "Blocker Popup Not Found");
		} catch (Exception e) {
			Logger.info("Error " + e.getMessage());
		}
		
		o365HomeAction.deleteFile(getWebDriver(), GatewayTestConstants.O365_CREATE_FOLDER);
		protectAction.deletePolicy(client, suiteData, 
				GatewayTestConstants.O365_SHARING_POLICY + getSaasAppUserName());
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Policy Violation");
		expectedDataMap.put(GatewayTestConstants.ACTION_TAKEN, "");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "[ALERT] " + suiteData.getTestUsername() + 
				" attempted to share content:newfolderwith external user(s):"+ suiteData.getUsername() 
				+ " violating policy:" + GatewayTestConstants.O365_SHARING_POLICY + getSaasAppUserName()); 
		expectedDataMap.put(GatewayTestConstants.POLICY_ACTION, "ALERT");
		expectedDataMap.put(GatewayTestConstants.POLICY_TYPE, "FileSharingGateway");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "high");
		expectedDataMap.remove(GatewayTestConstants.COUNTRY);
		expectedDataMap.remove(GatewayTestConstants.USER_NAME);
		
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),
				"Expected log: "+expectedDataMap.get(GatewayTestConstants.MESSAGE)+" does not exist on investigate");
		
		Logger.info("==================================================================================");
		Logger.info(" Share event via email validation done");
		Logger.info("==================================================================================");
	}
	
	
	@Priority(7)
	@Test(groups ={"Regression", "REACH"}) 
	public void o365_Test_Share_Link_Without_Block_Policy() throws Exception {
		Logger.info("==================================================================================" );
		Logger.info(" Share policy severity high ");
		Logger.info("==================================================================================" );
		printCredentials();
		String fromTime=backend.getCurrentTime();
		protectAction.createEnableFileSharingPolicy(client, suiteData, 
				GatewayTestConstants.O365_POLICY_NAME + getSaasAppUserName(), "high");
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.loadOnedriveApp(getWebDriver());
		o365HomeAction.shareItemByLink(getWebDriver(), GatewayTestConstants.GDRIVE_ONE_DRIVE_FILE_ATTACH);

		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Policy Violation");
		expectedDataMap.put(GatewayTestConstants.ACTION_TAKEN, "");
		expectedDataMap.put(GatewayTestConstants.POLICY_ACTION, "ALERT");
		expectedDataMap.put(GatewayTestConstants.MESSAGE,  "[ALERT] "+ suiteData.getTestUsername() + 
				" attempted to share content:"+  GatewayTestConstants.GDRIVE_ONE_DRIVE_FILE_ATTACH.toLowerCase() + 
				"with external user(s):ALL_EL__ violating policy:" + GatewayTestConstants.O365_POLICY_NAME + getSaasAppUserName()); 

		expectedDataMap.put(GatewayTestConstants.POLICY_TYPE, "FileSharingGateway");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "high");
		expectedDataMap.remove(GatewayTestConstants.COUNTRY);
		expectedDataMap.remove(GatewayTestConstants.USER_NAME);
		protectAction.deletePolicy(client, suiteData, GatewayTestConstants.O365_POLICY_NAME + getSaasAppUserName());
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("==================================================================================" );
		Logger.info("Share policy severity high successful ");
		Logger.info("==================================================================================" );
	}
	
	@Priority(8)
	@Test(groups ={"Regression", "REACH"})  
	public void o365_Test_File_Sharing_Public_Block_Policy() throws Exception {
		Logger.info("==================================================================================");
		Logger.info(" Verifying Share folder via Email");
		Logger.info("==================================================================================");
		printCredentials();
		protectAction.deletePolicy(client, suiteData, 
				GatewayTestConstants.O365_SHARING_POLICY + getSaasAppUserName());
		Logger.info("Creating File Shareing Policy, User " +suiteData.getTestUsername() + " Receipt " +  suiteData.getUsername());
		protectAction.createEnableFileSharingPolicyWithBlock(client, suiteData, 
				GatewayTestConstants.O365_SHARING_POLICY + getSaasAppUserName(), "high");
		
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.loadOnedriveApp(getWebDriver());
		o365HomeAction.shareItemClick(getWebDriver(), GatewayTestConstants.GDRIVE_ONE_DRIVE_FILE_ATTACH);
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
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "[ALERT] " + suiteData.getTestUsername() + 
				" attempted to share content:dial-faq.pdfwith external user(s):ALL_EL__ violating policy:" + GatewayTestConstants.O365_SHARING_POLICY + getSaasAppUserName()); 
		expectedDataMap.put(GatewayTestConstants.POLICY_ACTION, "ALERT");
		expectedDataMap.put(GatewayTestConstants.POLICY_TYPE, "FileSharingGateway");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "high");
		expectedDataMap.remove(GatewayTestConstants.COUNTRY);
		expectedDataMap.remove(GatewayTestConstants.USER_NAME);
		
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),
				"Expected log: "+expectedDataMap.get(GatewayTestConstants.MESSAGE)+" does not exist on investigate");
		
		Logger.info("==================================================================================");
		Logger.info(" Share event via email validation done");
		Logger.info("==================================================================================");
	}
	
	@Priority(9)
	@Test(groups ={"Regression", "REACH"})  
	public void o365_Test_File_Sharing_Public_Deactivate_Policy() throws Exception {
		Logger.info("==================================================================================");
		Logger.info(" Verifying share viw link event");
		Logger.info("==================================================================================");
		printCredentials();
		protectAction.deActivatePolicy(client, suiteData, 
				GatewayTestConstants.O365_SHARING_POLICY + getSaasAppUserName());

		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.loadOnedriveApp(getWebDriver());
		o365HomeAction.shareItemByLink(getWebDriver(), GatewayTestConstants.GDRIVE_ONE_DRIVE_FILE_ATTACH);
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Share");
		expectedDataMap.put(GatewayTestConstants.MESSAGE,  "User got link of " + GatewayTestConstants.GDRIVE_ONE_DRIVE_FILE_ATTACH); 
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Document/Folder");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.OBJECT_NAME, "Dial-FAQ.pdf");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("==================================================================================");
		Logger.info(" Share via link verification done");
		Logger.info("==================================================================================");

	}
	
	@Priority(10)
	@Test(groups ={"Regression", "REACH"})  
	public void o365_Test_File_Sharing_Public_Reactivate_Block_Policy() throws Exception {
		Logger.info("==================================================================================");
		Logger.info(" Verifying Share folder via Email");
		Logger.info("==================================================================================");
		printCredentials();
		protectAction.activatePolicy(client, suiteData, 
				GatewayTestConstants.O365_SHARING_POLICY + getSaasAppUserName());
		
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.loadOnedriveApp(getWebDriver());
		o365HomeAction.shareItemClick(getWebDriver(), GatewayTestConstants.GDRIVE_ONE_DRIVE_FILE_ATTACH);
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
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "[ALERT] " + suiteData.getTestUsername() + 
				" attempted to share content:dial-faq.pdfwith external user(s):ALL_EL__ violating policy:" + GatewayTestConstants.O365_SHARING_POLICY + getSaasAppUserName()); 
		expectedDataMap.put(GatewayTestConstants.POLICY_ACTION, "ALERT");
		expectedDataMap.put(GatewayTestConstants.POLICY_TYPE, "FileSharingGateway");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "high");
		expectedDataMap.remove(GatewayTestConstants.COUNTRY);
		expectedDataMap.remove(GatewayTestConstants.USER_NAME);
		
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),
				"Expected log: "+expectedDataMap.get(GatewayTestConstants.MESSAGE)+" does not exist on investigate");
		
		Logger.info("==================================================================================");
		Logger.info(" Share event via email validation done");
		Logger.info("==================================================================================");
	}
	
	public String getSaasAppUserName(){
		return suiteData.getSaasAppUsername().replaceAll("@", "_");
	}
	
	@BeforeClass(groups ={"Regression", "REACH"})
	public void doBeforeClass() throws Exception {
		try {
			Logger.info("Delete Policy Before Test ");
			protectData.setPolicyName(GatewayTestConstants.O365_SHARING_POLICY + getSaasAppUserName());
			protectAction.deletePolicy(client, suiteData, GatewayTestConstants.O365_SHARING_POLICY + getSaasAppUserName());
			Logger.info(" policy to delete");
		} catch(Exception e) {
			Logger.info("No policy to delete");
		}
	}
	
	@AfterClass(groups ={"Regression", "REACH"})
	public void doAfterClass() throws Exception {
		try {
			Logger.info("Delete Policy After Test ");
			protectData.setPolicyName(GatewayTestConstants.O365_SHARING_POLICY + getSaasAppUserName());
			protectAction.deletePolicy(client, suiteData, GatewayTestConstants.O365_SHARING_POLICY + getSaasAppUserName());
			Logger.info(" policy to delete");
		} catch(Exception e) {
			Logger.info("No policy to delete");
		}
	}

}