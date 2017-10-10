package com.elastica.tests.google;

import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.elastica.action.protect.ProtectAction;
import com.elastica.action.protect.ProtectDTO;
import com.elastica.common.GWCommonTest;
import com.elastica.gateway.GatewayTestConstants;
import com.elastica.listeners.Priority;
import com.elastica.logger.Logger;

public class GDriveTestsFileSharingPolicy extends GWCommonTest {
	Map <String, Object> expectedDataMap = new HashMap<String, Object>();
	String fromTime=backend.getCurrentTime();
	String title;
	SoftAssert softAssert = new SoftAssert();
	ProtectAction protectAction = new ProtectAction();
	ProtectDTO protectData = new ProtectDTO();
	boolean blockerdisplay = false;

	@Priority(1)
	@Test(groups ={"Regression"})
	public void loginToCloudSocAppAndSetupSSO() throws Exception {
		fromTime=backend.getCurrentTime();
		printCredentials();
		Logger.info("==================================================================================");
		Logger.info("Login into CloudSoc");
		Logger.info("==================================================================================");
		login.loginCloudSocPortal(getWebDriver(), suiteData);
		Logger.info("==================================================================================");
		Logger.info("Loging into CloudSoc done");
		Logger.info("==================================================================================");
	}
	
	
	@Priority(2)
	@Test(groups ={"Regression", "REACH"})  
	public void gDrive_Test_001_ValidateLogin() throws Exception {
		fromTime=backend.getCurrentTime();
		printCredentials();
		Logger.info("==================================================================================");
		Logger.info("Verifying the login event");
		Logger.info("==================================================================================");
		expectedDataMap.clear();
		gda.login(getWebDriver(), suiteData);
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.remove(GatewayTestConstants.FACILITY);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Login");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User logged in"); 
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Session");
		//expectedDataMap.put(GatewayTestConstants.REQ_URI, GatewayTestConstants.GDRIVE_REQ_URI_LOGIN);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("==================================================================================");
		Logger.info("Verify login successful");
		Logger.info("==================================================================================");
	}
	
	
	@Priority(3)
	@Test(groups ={"Regression", "REACH"}) 
	public void gDrive_Test_File_Sharing_Via_Email_Without_Block() throws Exception {
		Logger.info("==================================================================================");
		Logger.info(" Verifying Share folder via Email without Block");
		Logger.info("==================================================================================");
		printCredentials();
		protectAction.deletePolicy(client, suiteData, GatewayTestConstants.O365_SHARING_POLICY + getSaasAppUserName());
		protectAction.createEnableFileSharingPolicy(client, suiteData, 
				GatewayTestConstants.O365_SHARING_POLICY + getSaasAppUserName(), "high");
		Logger.info("==================================================================================");
		Logger.info("Completed Share Email ");
		Logger.info("==================================================================================");
	
		gda.homepage(getWebDriver(), suiteData);
		if (!gda.isExist(getWebDriver(), GatewayTestConstants.GDRIVE_FILE_EMAIL_LINK)) {
			gda.deleteFile(getWebDriver(), GatewayTestConstants.GDRIVE_FILE_EMAIL_LINK);
			gda.createDocFile(getWebDriver(), GatewayTestConstants.GDRIVE_FILE_EMAIL_LINK);
		}
		gda.homepage(getWebDriver(), suiteData);
		gda.shareEmail(getWebDriver(), suiteData.getUsername(), GatewayTestConstants.GDRIVE_FILE_EMAIL_LINK);
		Logger.info("File Share");
		
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Policy Violation");
		expectedDataMap.put(GatewayTestConstants.ACTION_TAKEN, "");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "[ALERT] " + suiteData.getTestUsername() + 
				" attempted to share content:" + GatewayTestConstants.GDRIVE_FILE_EMAIL_LINK + "with external user(s):"); 
		expectedDataMap.put(GatewayTestConstants.POLICY_ACTION, "ALERT");
		expectedDataMap.put(GatewayTestConstants.POLICY_TYPE, "FileSharingGateway");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "high");
		expectedDataMap.remove(GatewayTestConstants.COUNTRY);
		expectedDataMap.remove(GatewayTestConstants.USER_NAME);
		
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),
				"Expected log: "+expectedDataMap.get(GatewayTestConstants.MESSAGE)+" does not exist on investigate");
		Logger.info("==================================================================================");
		Logger.info(" Verifying Share folder via Email without Block completed");
		Logger.info("==================================================================================");
		
	}
	
	@Priority(4)
	@Test(groups ={"Regression", "REACH"})  
	public void gDrive_Test_File_Sharing_Via_Email_Block_Policy() throws Exception {
		Logger.info("==================================================================================");
		Logger.info(" Verifying Share folder via Email with Block");
		Logger.info("==================================================================================");
		printCredentials();
		protectAction.deletePolicy(client, suiteData, 
				GatewayTestConstants.O365_SHARING_POLICY + getSaasAppUserName());
		Logger.info("Creating File Shareing Policy, User " +suiteData.getTestUsername() + " Receipt " +  suiteData.getUsername());
		protectAction.createEnableFileSharingPolicyByShare(client, suiteData, 
				GatewayTestConstants.O365_SHARING_POLICY + getSaasAppUserName(),  suiteData.getUsername(), "high");
		
		gda.homepage(getWebDriver(), suiteData);
		if (!gda.isExist(getWebDriver(), GatewayTestConstants.GDRIVE_FILE_EMAIL_LINK)) {
			gda.deleteFile(getWebDriver(), GatewayTestConstants.GDRIVE_FILE_EMAIL_LINK);
			gda.createDocFile(getWebDriver(), GatewayTestConstants.GDRIVE_FILE_EMAIL_LINK);
		}
		gda.homepage(getWebDriver(), suiteData);
		gda.shareEmailBlock(getWebDriver(), suiteData.getUsername(), GatewayTestConstants.GDRIVE_FILE_EMAIL_LINK);
		
		try {
			boolean value = clickOkInPopup();
			Assert.assertTrue(value, "Blocker Popup Not Found");
		} catch (Exception e) {
			Logger.info("Error " + e.getMessage());
		}
		
		Logger.info("File Share");
		
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Policy Violation");
		expectedDataMap.put(GatewayTestConstants.ACTION_TAKEN, "");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "[ALERT] " + suiteData.getTestUsername() + 
				" attempted to share content:" + GatewayTestConstants.GDRIVE_FILE_EMAIL_LINK + "with external user(s):"); 
		expectedDataMap.put(GatewayTestConstants.POLICY_ACTION, "ALERT");
		expectedDataMap.put(GatewayTestConstants.POLICY_TYPE, "FileSharingGateway");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "high");
		expectedDataMap.remove(GatewayTestConstants.COUNTRY);
		expectedDataMap.remove(GatewayTestConstants.USER_NAME);
		
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),
				"Expected log: "+expectedDataMap.get(GatewayTestConstants.MESSAGE)+" does not exist on investigate");
		Logger.info("==================================================================================");
		Logger.info(" Verifying Share folder via Email with Block completed");
		Logger.info("==================================================================================");
	}
	
	@Priority(5)
	@Test(groups ={"Regression", "REACH"}) 
	public void gDrive_Test_File_Sharing_Via_Email_Deactivate_Policy() throws Exception {
		Logger.info("==================================================================================");
		Logger.info(" Verifying Share folder via Email Block Deactivate");
		Logger.info("==================================================================================");
		printCredentials();
		protectAction.deActivatePolicy(client, suiteData, 
				GatewayTestConstants.O365_SHARING_POLICY + getSaasAppUserName());

		Logger.info("==================================================================================");
		Logger.info("Share Email");
		Logger.info("==================================================================================");
		gda.homepage(getWebDriver(), suiteData);
		if (!gda.isExist(getWebDriver(), GatewayTestConstants.GDRIVE_FILE_EMAIL_LINK)) {
			gda.deleteFile(getWebDriver(), GatewayTestConstants.GDRIVE_FILE_EMAIL_LINK);
			gda.createDocFile(getWebDriver(), GatewayTestConstants.GDRIVE_FILE_EMAIL_LINK);
		}
		gda.homepage(getWebDriver(), suiteData);
		gda.shareEmail(getWebDriver(), suiteData.getUsername(), GatewayTestConstants.GDRIVE_FILE_EMAIL_LINK);
		Logger.info("File Share");
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Share");
		expectedDataMap.put(GatewayTestConstants.FILE_TYPE_GENERIC, "");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User shared file(s)/folder(s) " + GatewayTestConstants.GDRIVE_FILE_EMAIL_LINK + " with QA Admin &lt;" + suiteData.getUsername()  +"&gt;");
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File/Folder");
		expectedDataMap.put(GatewayTestConstants.OBJECT_NAME, GatewayTestConstants.GDRIVE_FILE_EMAIL_LINK);
		//expectedDataMap.put(GatewayTestConstants.REQ_URI, "https://drive.google.com/sharing/commonshare");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");

		Logger.info("==================================================================================");
		Logger.info(" Verifying Share folder via Email Block Deactivate Completed");
		Logger.info("==================================================================================");
		
	}
	
	@Priority(6)
	@Test(groups ={"Regression", "REACH"})  
	public void gDrive_Test_File_Sharing_Via_Email_Reactivate_Block_Policy() throws Exception {
		Logger.info("==================================================================================");
		Logger.info(" Verifying Share folder via Email Block Reactivate");
		Logger.info("==================================================================================");
		printCredentials();
		protectAction.activatePolicy(client, suiteData, 
				GatewayTestConstants.O365_SHARING_POLICY + getSaasAppUserName());
		
		Logger.info("==================================================================================");
		Logger.info(" Verifying Share folder via Email");
		Logger.info("==================================================================================");
		protectAction.deletePolicy(client, suiteData, 
				GatewayTestConstants.O365_SHARING_POLICY + getSaasAppUserName());
		Logger.info("Creating File Shareing Policy, User " +suiteData.getTestUsername() + " Receipt " +  suiteData.getUsername());
		protectAction.createEnableFileSharingPolicyByShare(client, suiteData, 
				GatewayTestConstants.O365_SHARING_POLICY + getSaasAppUserName(),  suiteData.getUsername(), "high");
		
		gda.homepage(getWebDriver(), suiteData);
		if (!gda.isExist(getWebDriver(), GatewayTestConstants.GDRIVE_FILE_EMAIL_LINK)) {
			gda.deleteFile(getWebDriver(), GatewayTestConstants.GDRIVE_FILE_EMAIL_LINK);
			gda.createDocFile(getWebDriver(), GatewayTestConstants.GDRIVE_FILE_EMAIL_LINK);
		}
		gda.homepage(getWebDriver(), suiteData);
		gda.shareEmailBlock(getWebDriver(), suiteData.getUsername(), GatewayTestConstants.GDRIVE_FILE_EMAIL_LINK);
		
		try {
			boolean value = clickOkInPopup();
			Assert.assertTrue(value, "Blocker Popup Not Found");
		} catch (Exception e) {
			Logger.info("Error " + e.getMessage());
		}
		
		Logger.info("File Share");
		
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Policy Violation");
		expectedDataMap.put(GatewayTestConstants.ACTION_TAKEN, "");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "[ALERT] " + suiteData.getTestUsername() + 
				" attempted to share content:" + GatewayTestConstants.GDRIVE_FILE_EMAIL_LINK + "with external user(s):"); 
		expectedDataMap.put(GatewayTestConstants.POLICY_ACTION, "ALERT");
		expectedDataMap.put(GatewayTestConstants.POLICY_TYPE, "FileSharingGateway");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "high");
		expectedDataMap.remove(GatewayTestConstants.COUNTRY);
		expectedDataMap.remove(GatewayTestConstants.USER_NAME);
		
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),
				"Expected log: "+expectedDataMap.get(GatewayTestConstants.MESSAGE)+" does not exist on investigate");
		Logger.info("==================================================================================");
		Logger.info(" Verifying Share folder via Email Block Reactivate Completed");
		Logger.info("==================================================================================");
	}
	
	
	@Priority(7)
	@Test(groups ={"Regression", "REACH"}) 
	public void gDrive_Test_File_Sharing_Public_Without_Block_Policy() throws Exception {
		Logger.info("==================================================================================");
		Logger.info("Link Sharing without Block");
		Logger.info("==================================================================================");
		printCredentials();
		String fromTime=backend.getCurrentTime();
		protectAction.createEnableFileSharingPolicy(client, suiteData, 
				GatewayTestConstants.O365_SHARING_POLICY + getSaasAppUserName(), "high");

		gda.homepage(getWebDriver(), suiteData);
		gda.shareByLinkByTopMenuBlock(getWebDriver(), GatewayTestConstants.GDRIVE_FILE_PUBLIC_LINK);

		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Policy Violation");
		expectedDataMap.put(GatewayTestConstants.ACTION_TAKEN, "");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "[ALERT] " + suiteData.getTestUsername() + 
				" attempted to share content:" + GatewayTestConstants.GDRIVE_FILE_PUBLIC_LINK + "with external user(s):ALL_EL__ violating policy:" + GatewayTestConstants.O365_SHARING_POLICY + getSaasAppUserName() ); 
		expectedDataMap.put(GatewayTestConstants.POLICY_ACTION, "ALERT");
		expectedDataMap.put(GatewayTestConstants.POLICY_TYPE, "FileSharingGateway");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "high");
		expectedDataMap.remove(GatewayTestConstants.COUNTRY);
		expectedDataMap.remove(GatewayTestConstants.USER_NAME);
		
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),
				"Expected log: "+expectedDataMap.get(GatewayTestConstants.MESSAGE)+" does not exist on investigate");
		Logger.info("==================================================================================");
		Logger.info("Link Sharing without Block Completed");
		Logger.info("==================================================================================");
	
	}
	
	@Priority(8)
	@Test(groups ={"Regression", "REACH"})  
	public void gDrive_Test_File_Sharing_Public_Block_Policy() throws Exception {
		fromTime=backend.getCurrentTime();
		printCredentials();
		Logger.info("==================================================================================");
		Logger.info("Link Sharing Block");
		Logger.info("==================================================================================");
		
		protectAction.deletePolicy(client, suiteData, 
				GatewayTestConstants.O365_SHARING_POLICY + getSaasAppUserName());
		Logger.info("Creating File Shareing Policy, User " +suiteData.getTestUsername() + " Receipt " +  suiteData.getUsername());
		protectAction.createEnableFileSharingPolicyWithBlock(client, suiteData, 
				GatewayTestConstants.O365_SHARING_POLICY + getSaasAppUserName(), "high");
		gda.homepage(getWebDriver(), suiteData);
		gda.shareByLinkByTopMenuBlock(getWebDriver(), GatewayTestConstants.GDRIVE_FILE_PUBLIC_LINK);
		blockerdisplay = false;
		for (int i = 0; i < 3; i++) {
			try {
				boolean value = clickOkInPopup();
				if (value == true) {
					blockerdisplay = true;
					gda.hardWait(10);
				}
			} catch (Exception e) {
				Logger.info("Error " + e.getMessage());
			}
		}
		Assert.assertTrue(blockerdisplay, "Blocker Popup Not Found");
	
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Policy Violation");
		expectedDataMap.put(GatewayTestConstants.ACTION_TAKEN, "");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "[ALERT] " + suiteData.getTestUsername() + 
				" attempted to share content:" + GatewayTestConstants.GDRIVE_FILE_PUBLIC_LINK + "with external user(s):ALL_EL__ violating policy:" + GatewayTestConstants.O365_SHARING_POLICY + getSaasAppUserName() ); 
		expectedDataMap.put(GatewayTestConstants.POLICY_ACTION, "ALERT");
		expectedDataMap.put(GatewayTestConstants.POLICY_TYPE, "FileSharingGateway");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "high");
		expectedDataMap.remove(GatewayTestConstants.COUNTRY);
		expectedDataMap.remove(GatewayTestConstants.USER_NAME);
		
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),
				"Expected log: "+expectedDataMap.get(GatewayTestConstants.MESSAGE)+" does not exist on investigate");
		Logger.info("==================================================================================");
		Logger.info("Link Sharing Block Completed");
		Logger.info("==================================================================================");
	}
	
	@Priority(9)
	@Test(groups ={"Regression", "REACH"})  
	public void gDrive_Test_File_Sharing_Public_Deactivate_Policy() throws Exception {
		Logger.info("==================================================================================");
		Logger.info("Link Sharing Block Deactivate");
		Logger.info("==================================================================================");
		printCredentials();
		protectAction.deActivatePolicy(client, suiteData, 
				GatewayTestConstants.O365_SHARING_POLICY + getSaasAppUserName());

		gda.homepage(getWebDriver(), suiteData);
		gda.shareByLinkByTopMenuWithOutBlock(getWebDriver(), GatewayTestConstants.GDRIVE_FILE_PUBLIC_LINK);
	
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User turned on link sharing for file/folder " + GatewayTestConstants.GDRIVE_FILE_PUBLIC_LINK);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Share");
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File/Folder");
		//expectedDataMap.put(GatewayTestConstants.REQ_URI, "https://drive.google.com/sharing/commonshare");
		expectedDataMap.put(GatewayTestConstants.SHARE__WITH, "ALL_EL__");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("==================================================================================");
		Logger.info("Link Sharing Block Deactivate Completed ");
		Logger.info("==================================================================================");
	}
	
	@Priority(10)
	@Test(groups ={"Regression", "REACH"})  
	public void gDrive_Test_File_Sharing_Public_Reactivate_Block_Policy() throws Exception {
		Logger.info("==================================================================================");
		Logger.info("Link Sharing Block Reactivate");
		Logger.info("==================================================================================");
		printCredentials();
		protectAction.activatePolicy(client, suiteData, 
				GatewayTestConstants.O365_SHARING_POLICY + getSaasAppUserName());
		
		gda.homepage(getWebDriver(), suiteData);
		gda.shareByLinkByTopMenuBlock(getWebDriver(), GatewayTestConstants.GDRIVE_FILE_PUBLIC_LINK);
	
		try {
			boolean value = clickOkInPopup();
			Assert.assertTrue(value, "Blocker Popup Not Found");
		} catch (Exception e) {
			Logger.info("Error " + e.getMessage());
		}
		Logger.info("Link Share");
	
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Policy Violation");
		expectedDataMap.put(GatewayTestConstants.ACTION_TAKEN, "");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "[ALERT] " + suiteData.getTestUsername() + 
				" attempted to share content:" + GatewayTestConstants.GDRIVE_FILE_PUBLIC_LINK + "with external user(s):ALL_EL__ violating policy:" + GatewayTestConstants.O365_SHARING_POLICY + getSaasAppUserName() ); 
		expectedDataMap.put(GatewayTestConstants.POLICY_ACTION, "ALERT");
		expectedDataMap.put(GatewayTestConstants.POLICY_TYPE, "FileSharingGateway");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "high");
		expectedDataMap.remove(GatewayTestConstants.COUNTRY);
		expectedDataMap.remove(GatewayTestConstants.USER_NAME);
		
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),
				"Expected log: "+expectedDataMap.get(GatewayTestConstants.MESSAGE)+" does not exist on investigate");
		
		Logger.info("==================================================================================");
		Logger.info("Link Sharing Block Reactivate Completed");
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
