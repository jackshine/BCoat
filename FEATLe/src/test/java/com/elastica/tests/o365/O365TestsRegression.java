package com.elastica.tests.o365;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.elastica.common.GWCommonTest;
import com.elastica.gateway.GatewayTestConstants;
import com.elastica.listeners.Priority;
import com.elastica.logger.Logger;

public class O365TestsRegression extends GWCommonTest{
	Map <String, Object> expectedDataMap = new HashMap<String, Object>();
	Map<String, String>policyDataMap= new HashMap<String, String>(); 
	String fromTime=backend.getCurrentTime();
	
	@BeforeMethod()
	public void clearDataMap(){
		expectedDataMap.clear();
	}

	@Priority(1)
	@Test(groups ={"Regression"})
	public void performingActivitiesOnSaasAppo365() throws Exception {
		fromTime=backend.getCurrentTime();
		printCredentials();
		Logger.info("Started performing activities on saas app");
		login.loginCloudSocPortal(getWebDriver(), suiteData);
		Logger.info("Finished login activities on cloudSoc");
	}
	
	@Priority(2)
	@Test(groups ={"Regression", "REACH"})  //
	public void o365_Test_01_ValidateLogingActivityEvent() throws Exception {
		Logger.info("Verifying the login event");
		printCredentials();
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Login");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User logged in"); 
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Session");
		expectedDataMap.put(GatewayTestConstants.REQ_URI, "https://login.microsoftonline.com/common/login");
		o365Login.login(getWebDriver(), suiteData);
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("Login Event Successfull");
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getGWHeaders(suiteData));
	}

	@Priority(3)
	@Test(groups ={"Regression", "REACH"})
	public void o365_Test_06_ValidateShareActivity() throws Exception {
		Logger.info("Verifying share activity event");
		printCredentials();
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getGWHeaders(suiteData));
		//o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		//o365HomeAction.loadOnedriveApp(getWebDriver());
		o365HomeAction.loadOnedriveAppViaLink(getWebDriver(), getOneDriveBusinessUrl());
		o365HomeAction.uploadItemFile(getWebDriver(),  System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+
				"resources"+File.separator+"meta"+File.separator + GatewayTestConstants.O365_UPLOAD_FILE);
		//o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		//o365HomeAction.loadOnedriveApp(getWebDriver());
		o365HomeAction.loadOnedriveAppViaLink(getWebDriver(), getOneDriveBusinessUrl());
		o365HomeAction.shareItemViaEmail(getWebDriver(), GatewayTestConstants.O365_UPLOAD_FILE, suiteData.getTestUsername());
		//o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		//o365HomeAction.loadOnedriveApp(getWebDriver());
		o365HomeAction.loadOnedriveAppViaLink(getWebDriver(), getOneDriveBusinessUrl());
		o365HomeAction.deleteFile(getWebDriver(), GatewayTestConstants.O365_UPLOAD_FILE);
	
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Share");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User sent email invitation(s) to " 
				+ suiteData.getTestUsername() + " for " + GatewayTestConstants.O365_UPLOAD_FILE + ".");
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Document");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("Download Event Successfull");
	}
	
	@Priority(4)
	@Test(groups ={"Regression", "REACH"})
	public void o365_Test_Rename_File() throws Exception {
		Logger.info("File Rename");
		printCredentials();
		//o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		//o365HomeAction.loadOnedriveApp(getWebDriver());
		o365HomeAction.loadOnedriveAppViaLink(getWebDriver(), getOneDriveBusinessUrl());
		o365HomeAction.deleteFile(getWebDriver(), GatewayTestConstants.O365_UPLOAD_FILE);
		o365HomeAction.uploadItemFile(getWebDriver(),  System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+
				"resources"+File.separator+"meta"+File.separator + GatewayTestConstants.O365_UPLOAD_FILE);
		//o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		//o365HomeAction.loadOnedriveApp(getWebDriver());
		o365HomeAction.loadOnedriveAppViaLink(getWebDriver(), getOneDriveBusinessUrl());
		o365HomeAction.renameItem(getWebDriver(), GatewayTestConstants.O365_UPLOAD_FILE, GatewayTestConstants.O365_RENAME);
		o365HomeAction.renameItem(getWebDriver(), GatewayTestConstants.O365_RENAME, GatewayTestConstants.O365_UPLOAD_FILE.replace(".txt", ""));
		
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User renamed item from " +  
				GatewayTestConstants.O365_UPLOAD_FILE + " to " + GatewayTestConstants.O365_RENAME);
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("File Rename Successful");
	}
	
	@Priority(5)
	@Test(groups ={"Regression", "REACH"})
	public void o365_Test_Move_File() throws Exception {
		Logger.info("File Move");
		printCredentials();
		//o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		//o365HomeAction.loadOnedriveApp(getWebDriver());
		o365HomeAction.loadOnedriveAppViaLink(getWebDriver(), getOneDriveBusinessUrl());
		o365HomeAction.moveItem(getWebDriver(), GatewayTestConstants.O365_UPLOAD_FILE, GatewayTestConstants.O365_MOVE_TO_FOLDER);
		Logger.info("Go To Folder File got Moved");
		o365HomeAction.goToFolder(getWebDriver(),  GatewayTestConstants.O365_MOVE_TO_FOLDER);
		Logger.info("Go To Parent Folder");
		o365HomeAction.moveItem(getWebDriver(), GatewayTestConstants.O365_UPLOAD_FILE,  GatewayTestConstants.O365_ROOT_FOLDER);
		
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User moved item " + GatewayTestConstants.O365_UPLOAD_FILE + " to " + GatewayTestConstants.O365_MOVE_TO_FOLDER);
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("File Move Successful");
	}

	@Priority(6)
	@Test(groups ={"Regression", "REACH"})
	public void o365_Test_Copy_File() throws Exception {
		Logger.info("File Copy");
		printCredentials();
		//o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		//o365HomeAction.loadOnedriveApp(getWebDriver());
		o365HomeAction.loadOnedriveAppViaLink(getWebDriver(), getOneDriveBusinessUrl());
		o365HomeAction.copyItem(getWebDriver(), GatewayTestConstants.O365_UPLOAD_FILE, GatewayTestConstants.O365_MOVE_TO_FOLDER);
		o365HomeAction.goToFolder(getWebDriver(), GatewayTestConstants.O365_MOVE_TO_FOLDER);
		o365HomeAction.deleteFile(getWebDriver(), GatewayTestConstants.O365_UPLOAD_FILE);
		
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User copied item " + GatewayTestConstants.O365_UPLOAD_FILE + " to " + GatewayTestConstants.O365_MOVE_TO_FOLDER);
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		o365HomeAction.loadOnedriveApp(getWebDriver());
		o365HomeAction.deleteFile(getWebDriver(), GatewayTestConstants.O365_UPLOAD_FILE);
		Logger.info("File Copy Successful");
	}
	
	@Priority(7)
	@Test(groups ={"Regression", "REACH"})
	public void o365_Test_deleteFile() throws Exception {
		Logger.info("Delete File");
		printCredentials();
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User sent "+GatewayTestConstants.O365_UPLOAD_FILE+" to the Recycle Bin");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("Delete File Successful");
	}

	@Priority(8)
	@Test(groups ={"Regression", "REACH"}) 
	public void o365_Test_Restore_File() throws Exception {
		Logger.info("Restore Item");
		printCredentials();
		//o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		//o365HomeAction.loadOnedriveApp(getWebDriver());
		o365HomeAction.loadOnedriveAppViaLink(getWebDriver(), getOneDriveBusinessUrl());
		o365HomeAction.goToFolder(getWebDriver(),  GatewayTestConstants.O365_MOVE_TO_DOC);
		o365HomeAction.deleteFile(getWebDriver(),   GatewayTestConstants.O365_UPLOAD_FILE);
		o365HomeAction.uploadItemFile(getWebDriver(),  System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+
				"resources"+File.separator+"meta"+File.separator + GatewayTestConstants.O365_UPLOAD_FILE);
		o365HomeAction.hardWait(10);
		//o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		//o365HomeAction.loadOnedriveApp(getWebDriver());
		o365HomeAction.loadOnedriveAppViaLink(getWebDriver(), getOneDriveBusinessUrl());
		o365HomeAction.goToFolder(getWebDriver(),  GatewayTestConstants.O365_MOVE_TO_DOC);
		o365HomeAction.deleteFile(getWebDriver(),   GatewayTestConstants.O365_UPLOAD_FILE);
		o365HomeAction.hardWait(10);
		
		o365HomeAction.restore(getWebDriver());
		Logger.info("File Restored");
		
		Logger.info("Start Delete from Recyle");
		o365HomeAction.hardWait(10);
		Logger.info("Deleted from SAAS app");
		//o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		//o365HomeAction.loadOnedriveApp(getWebDriver());
		o365HomeAction.loadOnedriveAppViaLink(getWebDriver(), getOneDriveBusinessUrl());
		o365HomeAction.goToFolder(getWebDriver(),  GatewayTestConstants.O365_MOVE_TO_DOC);
		o365HomeAction.deleteFile(getWebDriver(), GatewayTestConstants.O365_UPLOAD_FILE);
		Logger.info("Deleted Last file");
		o365HomeAction.deleteLastFileFromRecyle(getWebDriver());
		
		Logger.info("Start Delete All Files For Empty Trash");
		//o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		//o365HomeAction.loadOnedriveApp(getWebDriver());
		o365HomeAction.loadOnedriveAppViaLink(getWebDriver(), getOneDriveBusinessUrl());
		o365HomeAction.goToFolder(getWebDriver(),  GatewayTestConstants.O365_MOVE_TO_DOC);
		o365HomeAction.uploadItemFile(getWebDriver(),  System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+
				"resources"+File.separator+"meta"+File.separator + GatewayTestConstants.O365_UPLOAD_FILE);
		o365HomeAction.deleteFile(getWebDriver(), GatewayTestConstants.O365_UPLOAD_FILE);
		Logger.info("Delete all files from Recycle Bin");
		o365HomeAction.deleteAllFilesFromRecycle(getWebDriver());
		
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User restored item " + GatewayTestConstants.O365_UPLOAD_FILE + " from Recycle Bin");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("Restore Item Successful");
		
	}
	
	@Priority(9)
	@Test(groups ={"Regression", "REACH"}) 
	public void o365_Test_RecycleBin() throws Exception {
		Logger.info("Delete Item Recycle Bin");
		printCredentials();
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User deleted item " + GatewayTestConstants.O365_UPLOAD_FILE + " from Recycle Bin");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("Delete Item Recycle Bin Successful");
	}
	
	@Priority(10)
	@Test(groups ={"Regression", "REACH"}) 
	public void o365_Test_EmptyTrash() throws Exception {
		Logger.info("Emptry Trash");
		printCredentials();
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User emptied Recycle Bin");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("Emptry Trash Successful");
	}
	
	@Priority(11)
	@Test(groups ={"Regression", "REACH"})
	public void o365_Test_09_ValidateLogoutActivityEvent() throws Exception {
		printCredentials();
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365Login.logout(getWebDriver());
		Logger.info("Verifying Logout");
		
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User logged out");
		expectedDataMap.put(GatewayTestConstants.REFERER_URI, "");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("Logout Event Successful");
	}
	
	@Priority(12)
	@Test(groups ={"Regression", "REACH"})
	public void o365_Test_InvalidLogin() throws Exception {
		printCredentials();
		o365Login.reloginInvalidAndValid(getWebDriver(), suiteData);
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.remove(GatewayTestConstants.FACILITY);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User login failed!");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("Invalid Login Successful");
	}
	
	
	public String getSaasAppUserName(){
		return suiteData.getSaasAppUsername().replaceAll("@", "_");
	}
	
}