package com.elastica.tests.o365;

import java.util.HashMap;
import java.util.Map;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.elastica.common.GWCommonTest;
import com.elastica.gateway.GatewayTestConstants;
import com.elastica.listeners.Priority;
import com.elastica.logger.Logger;

public class O365TestsRegressionFolder extends GWCommonTest{
	Map <String, Object> expectedDataMap = new HashMap<String, Object>();
	Map<String, String>policyDataMap= new HashMap<String, String>(); 
	String fromTime=backend.getCurrentTime();
	
	@BeforeMethod()
	public void clearDataMap(){
		expectedDataMap.clear();
	}

	@Priority(1)
	@Test(groups ={"Regression1"})
	public void performingActivitiesOnSaasAppo365() throws Exception {
		fromTime=backend.getCurrentTime();
		printCredentials();
		Logger.info("Started performing activities on saas app");
		login.loginCloudSocPortal(getWebDriver(), suiteData);
		Logger.info("Finished login activities on cloudSoc");
	}
	
	@Priority(2)
	@Test(groups ={"Regression1", "REACH"})  //
	public void o365_Test_01_ValidateLogingActivityEvent() throws Exception {
		Logger.info("Verifying the login event");
		printCredentials();
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Login");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User logged in"); 
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Session");
		expectedDataMap.put(GatewayTestConstants.REQ_URI, "https://");
		o365Login.login(getWebDriver(), suiteData);
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("Login Event Successful");
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getGWHeaders(suiteData));
	}

	@Priority(3)
	@Test(groups ={"Regression1", "REACH"})
	public void o365_Test_Create_Folder() throws Exception {
		Logger.info("Create Folder");
		printCredentials();
		//o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		//o365HomeAction.loadOnedriveApp(getWebDriver());
		o365HomeAction.loadOnedriveAppViaLink(getWebDriver(), getOneDriveBusinessUrl());
		o365HomeAction.deleteFile(getWebDriver(), GatewayTestConstants.O365_CREATE_FOLDER);
		o365HomeAction.deleteFile(getWebDriver(), GatewayTestConstants.O365_RENAME_FOLDER);
		o365HomeAction.createFolder(getWebDriver(),  GatewayTestConstants.O365_CREATE_FOLDER);
		
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Create");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User created a new folder " + GatewayTestConstants.O365_CREATE_FOLDER);
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Folder");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("Create Folder Successful");
	}
	
	@Priority(4)
	@Test(groups ={"Regression1", "REACH"})
	public void o365_Test_Rename_Folder() throws Exception {
		fromTime=backend.getCurrentTime();
		Logger.info("Rename Folder");
		printCredentials();
		o365HomeAction.renameFolder(getWebDriver(), GatewayTestConstants.O365_CREATE_FOLDER,  GatewayTestConstants.O365_RENAME_FOLDER);
		
		//o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		//o365HomeAction.loadOnedriveApp(getWebDriver());
		o365HomeAction.loadOnedriveAppViaLink(getWebDriver(), getOneDriveBusinessUrl());
		o365HomeAction.renameFolder(getWebDriver(), GatewayTestConstants.O365_RENAME_FOLDER, GatewayTestConstants.O365_CREATE_FOLDER);
		
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Rename");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User renamed item from " +  GatewayTestConstants.O365_CREATE_FOLDER + 
				" to " + GatewayTestConstants.O365_RENAME_FOLDER);
		expectedDataMap.put(GatewayTestConstants.OBJECT_NAME, GatewayTestConstants.O365_RENAME_FOLDER);
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Document/Folder");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("Rename Folder Successful");
	}
	
	@Priority(5)
	@Test(groups ={"Regression1", "REACH"})
	public void o365_Test_Move_Folder() throws Exception {
		fromTime=backend.getCurrentTime();
		Logger.info("Move Folder");
		printCredentials();
		o365HomeAction.moveItem(getWebDriver(), GatewayTestConstants.O365_CREATE_FOLDER, GatewayTestConstants.O365_MOVE_TO_FOLDER);
		Logger.info("Go To Folder File got Moved");
		o365HomeAction.goToFolder(getWebDriver(), GatewayTestConstants.O365_MOVE_TO_FOLDER);
		Logger.info("Go To Parent Folder");
		o365HomeAction.moveItem(getWebDriver(), GatewayTestConstants.O365_CREATE_FOLDER, GatewayTestConstants.O365_ROOT_FOLDER);
		
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Move");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User moved item " + GatewayTestConstants.O365_CREATE_FOLDER + 
				" to " + GatewayTestConstants.O365_MOVE_TO_FOLDER);
		expectedDataMap.put(GatewayTestConstants.OBJECT_NAME, GatewayTestConstants.O365_CREATE_FOLDER);
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Document/Folder");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("Folder Move Successful");
	}
	
	@Priority(6)
	@Test(groups ={"Regression1", "REACH"})
	public void o365_Test_Copy_Folder() throws Exception {
		fromTime=backend.getCurrentTime();
		Logger.info("Folder Copy");
		printCredentials();
		//o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		//o365HomeAction.loadOnedriveApp(getWebDriver());
		o365HomeAction.loadOnedriveAppViaLink(getWebDriver(), getOneDriveBusinessUrl());
		o365HomeAction.copyItem(getWebDriver(), GatewayTestConstants.O365_CREATE_FOLDER, GatewayTestConstants.O365_MOVE_TO_FOLDER);
		o365HomeAction.goToFolder(getWebDriver(), GatewayTestConstants.O365_MOVE_TO_FOLDER);
		o365HomeAction.deleteFile(getWebDriver(), GatewayTestConstants.O365_CREATE_FOLDER);
		
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Copy");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User copied item " + GatewayTestConstants.O365_CREATE_FOLDER + 
				" to " + GatewayTestConstants.O365_MOVE_TO_FOLDER);
		expectedDataMap.put(GatewayTestConstants.OBJECT_NAME, GatewayTestConstants.O365_CREATE_FOLDER);
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Document/Folder");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("Folder Move Successful");
	}
	
	@Priority(7)
	@Test(groups ={"Regression1", "REACH"})
	public void o365_Test_Share_Folder() throws Exception {
		fromTime=backend.getCurrentTime();
		Logger.info("Folder Share");
		printCredentials();
		//o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		//o365HomeAction.loadOnedriveApp(getWebDriver());
		o365HomeAction.loadOnedriveAppViaLink(getWebDriver(), getOneDriveBusinessUrl());
		o365HomeAction.shareItemViaEmail(getWebDriver(), GatewayTestConstants.O365_CREATE_FOLDER, suiteData.getTestUsername());
		o365HomeAction.deleteFile(getWebDriver(), GatewayTestConstants.O365_CREATE_FOLDER);
		
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Share");
		expectedDataMap.put(GatewayTestConstants.MESSAGE,  "User sent email invitation(s) to " + 
				 suiteData.getTestUsername() + " for " + GatewayTestConstants.O365_CREATE_FOLDER + ".");
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Document/Folder");
		expectedDataMap.put(GatewayTestConstants.OBJECT_NAME, GatewayTestConstants.O365_CREATE_FOLDER);
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("Folder Share Successful");
	}
	
	@Priority(7)
	@Test(groups ={"Regression1", "REACH"})
	public void o365_Test_deleteFolder() throws Exception {
		fromTime=backend.getCurrentTime();
		Logger.info("Folder Delete");
		printCredentials();
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Delete");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User sent "+ GatewayTestConstants.O365_CREATE_FOLDER+" to the Recycle Bin");
		expectedDataMap.put(GatewayTestConstants.OBJECT_NAME, GatewayTestConstants.O365_CREATE_FOLDER);
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Folder");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("Folder Delete Successful");
	}
	
	
	public String getSaasAppUserName(){
		return suiteData.getSaasAppUsername().replaceAll("@", "_");
	}
	
	
	
}