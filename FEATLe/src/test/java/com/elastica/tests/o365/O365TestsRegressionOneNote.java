package com.elastica.tests.o365;

import java.util.HashMap;
import java.util.Map;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.elastica.common.GWCommonTest;
import com.elastica.gateway.GatewayTestConstants;
import com.elastica.listeners.Priority;
import com.elastica.listeners.RetryAnalyzer;
import com.elastica.logger.Logger;


public class O365TestsRegressionOneNote extends GWCommonTest{
	Map <String, Object> expectedDataMap = new HashMap<String, Object>();
	Map<String, String>policyDataMap= new HashMap<String, String>(); 
	String fromTime=backend.getCurrentTime();
	
	@BeforeMethod()
	public void clearDataMap(){
		expectedDataMap.clear();
	}

	@Priority(1)
	@Test(groups ={"Regression2"},retryAnalyzer=RetryAnalyzer.class)
	public void performingActivitiesOnSaasAppo365() throws Exception {
		fromTime=backend.getCurrentTime();
		printCredentials();
		Logger.info("Started performing activities on O365 Excel and OneNote");
		login.loginCloudSocPortal(getWebDriver(), suiteData);
		Logger.info("Finished login activities on cloudSoc");
	}
	
	@Priority(2)
	@Test(groups ={"Regression2", "REACH"}, retryAnalyzer=RetryAnalyzer.class)  //
	public void o365_Test_01_ValidateLogingActivityEvent() throws Exception {
		Logger.info("Login");
		fromTime=backend.getCurrentTime();
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
		Logger.info("Login Successful");
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getGWHeaders(suiteData));
	}
	
	@Priority(3)
	@Test(groups ={"Regression2", "REACH"}) 
	public void o365_Test_ExcelFile_Create() throws Exception {
		fromTime=backend.getCurrentTime();
		String appName = "Excel workbook";
		String fileName = "Book.xlsx";
		String extension = ".xlsx"; 
		Logger.info("App Name " + appName + "File Name " + fileName + "extension" + extension);
		Logger.info("Create Excel");
		printCredentials();
		//o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		//o365HomeAction.loadOnedriveApp(getWebDriver());
		o365HomeAction.loadOnedriveAppViaLink(getWebDriver(), getOneDriveBusinessUrl());
		o365HomeAction.goToFolder(getWebDriver(),  GatewayTestConstants.O365_MOVE_TO_DOC);
		if (o365HomeAction.verifyFolderSelected(getWebDriver(), GatewayTestConstants.O365_MOVE_TO_DOC)) {
			o365HomeAction.deleteAllFiles(getWebDriver());
			Logger.info("Deleted all the files before create");
		}
		o365HomeAction.createItem(getWebDriver(), appName, fileName);
		//o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		//o365HomeAction.loadOnedriveApp(getWebDriver());
		o365HomeAction.loadOnedriveAppViaLink(getWebDriver(), getOneDriveBusinessUrl());
		o365HomeAction.goToFolder(getWebDriver(),  GatewayTestConstants.O365_MOVE_TO_DOC);
		if (o365HomeAction.verifyFolderSelected(getWebDriver(), GatewayTestConstants.O365_MOVE_TO_DOC)) {
			Logger.info("Insdie the folder " + GatewayTestConstants.O365_MOVE_TO_DOC);
		}
		
		String createdName = "excel sheet named";
		
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Create");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User created new "+ createdName + " " +fileName + " in browser");
		expectedDataMap.put(GatewayTestConstants.OBJECT_NAME, fileName);
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Document");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("Create Excel Successful");
	}
	
	@Priority(4)
	@Test(groups ={"Regression2", "REACH"}) 
	public void o365_Test_ExcelFile_Download() throws Exception {
		fromTime=backend.getCurrentTime();
		printCredentials();
		String fileName = "Book.xlsx";
		Logger.info("Download File");
		o365HomeAction.downloadItemFileByName(getWebDriver(), fileName);
		
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.FILE_SIZE, "5");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Download");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User downloaded " + fileName);
		expectedDataMap.put(GatewayTestConstants.OBJECT_NAME, fileName);
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Document");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("Excel Download Successful");
	}
	
	@Priority(5)
	@Test(groups ={"Regression2", "REACH"}) 
	public void o365_Test_ExcelFile_Rename() throws Exception {
		printCredentials();
		fromTime=backend.getCurrentTime();
		String fileName = "Book.xlsx";
		String extension = ".xlsx"; 
		String renameFile = "excelrename"; 
		Logger.info("Rename File");
		o365HomeAction.renameItem(getWebDriver(), fileName, renameFile);
		o365HomeAction.renameItem(getWebDriver(), renameFile, fileName.replace(extension, ""));
		
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Rename");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User renamed item from " +  
				fileName + " to " + renameFile);
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Document/Folder");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("Excel Rename Successful");
	}
	
	@Priority(6)
	@Test(groups ={"Regression2", "REACH"}) 
	public void o365_Test_ExcelFile_Move() throws Exception {
		fromTime=backend.getCurrentTime();
		printCredentials();
		String fileName = "Book.xlsx";
		Logger.info("Move File");
		//o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		//o365HomeAction.loadOnedriveApp(getWebDriver());
		o365HomeAction.loadOnedriveAppViaLink(getWebDriver(), getOneDriveBusinessUrl());
		o365HomeAction.goToFolder(getWebDriver(),  "test");
		o365HomeAction.moveItem(getWebDriver(),  fileName, GatewayTestConstants.O365_MOVE_TO_FOLDER);
		Logger.info("Go To Folder File got Moved");
		//o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		//o365HomeAction.loadOnedriveApp(getWebDriver());
		o365HomeAction.loadOnedriveAppViaLink(getWebDriver(), getOneDriveBusinessUrl());
		o365HomeAction.goToFolder(getWebDriver(),  GatewayTestConstants.O365_MOVE_TO_FOLDER);
		Logger.info("Go To Parent Folder");
		o365HomeAction.moveItem(getWebDriver(), fileName,  GatewayTestConstants.O365_MOVE_TO_DOC);
		Logger.info("Move File Successful");
		
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Move");
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Document/Folder");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User moved item " + GatewayTestConstants.O365_MOVE_TO_FOLDER + 
				"/" + fileName +" to " + GatewayTestConstants.O365_MOVE_TO_DOC);
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("Excel Move File Successful");
	}
	
	@Priority(7)
	@Test(groups ={"Regression2", "REACH"}) 
	public void o365_Test_ExcelFile_Copy() throws Exception {
		fromTime=backend.getCurrentTime();
		printCredentials();
		String fileName = "Book.xlsx";
		Logger.info("Copy File");
		//o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		//o365HomeAction.loadOnedriveApp(getWebDriver());
		o365HomeAction.loadOnedriveAppViaLink(getWebDriver(), getOneDriveBusinessUrl());
		o365HomeAction.goToFolder(getWebDriver(),  GatewayTestConstants.O365_MOVE_TO_DOC);
		o365HomeAction.copyItem(getWebDriver(),  fileName, GatewayTestConstants.O365_MOVE_TO_FOLDER);
		//o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		//o365HomeAction.loadOnedriveApp(getWebDriver());
		o365HomeAction.loadOnedriveAppViaLink(getWebDriver(), getOneDriveBusinessUrl());
		o365HomeAction.goToFolder(getWebDriver(), GatewayTestConstants.O365_MOVE_TO_FOLDER);
		o365HomeAction.deleteFile(getWebDriver(), fileName);
		
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Copy");
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Document/Folder");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User copied item " + GatewayTestConstants.O365_MOVE_TO_DOC + "/" +fileName + " to " + GatewayTestConstants.O365_MOVE_TO_FOLDER);
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("Excel Copy File Successful");
	}
	
	@Priority(8)
	@Test(groups ={"Regression2", "REACH"}) 
	public void o365_Test_ExcelFile_Share() throws Exception {
		fromTime=backend.getCurrentTime();
		printCredentials();
		String fileName = "Book.xlsx";
		Logger.info("Share File");
		//o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		//o365HomeAction.loadOnedriveApp(getWebDriver());
		o365HomeAction.loadOnedriveAppViaLink(getWebDriver(), getOneDriveBusinessUrl());
		o365HomeAction.goToFolder(getWebDriver(),  GatewayTestConstants.O365_MOVE_TO_DOC);
		o365HomeAction.shareItemViaEmail(getWebDriver(), fileName, suiteData.getTestUsername());
		
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Share");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User sent email invitation(s) to " 
				+ suiteData.getTestUsername() + " for " + fileName + ".");
		expectedDataMap.put(GatewayTestConstants.OBJECT_NAME, fileName);
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Document/Folder");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("Share Excel Successful");
	}
	
	@Priority(9)
	@Test(groups ={"Regression2", "REACH"}) 
	public void o365_Test_ExcelFile_Delete() throws Exception {
		fromTime=backend.getCurrentTime();
		printCredentials();
		String fileName = "Book.xlsx";
		Logger.info("Delete File");
		//o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		//o365HomeAction.loadOnedriveApp(getWebDriver());
		o365HomeAction.loadOnedriveAppViaLink(getWebDriver(), getOneDriveBusinessUrl());
		o365HomeAction.goToFolder(getWebDriver(),  "test");
		o365HomeAction.deleteFile(getWebDriver(),  fileName);
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Delete");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User sent " + fileName + " to the Recycle Bin");
		expectedDataMap.put(GatewayTestConstants.OBJECT_NAME, fileName);
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Document");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("Delete Excel File Successful");
	}
	
	@Priority(10)
	@Test(groups ={"Regression2", "REACH"}) 
	public void o365_Test_OneNote_Create() throws Exception {
		fromTime=backend.getCurrentTime();
		printCredentials();
		String appName = "OneNote notebook";
		String fileName = "OneNoteFile";
		String extension = ""; 
		String createdName = "One Note";
		Logger.info("App Name " + appName + "File Name " + fileName + "extension" + extension);
		Logger.info("Create OneNote");
		
		//o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		//o365HomeAction.loadOnedriveApp(getWebDriver());
		o365HomeAction.loadOnedriveAppViaLink(getWebDriver(), getOneDriveBusinessUrl());
		o365HomeAction.goToFolder(getWebDriver(),  GatewayTestConstants.O365_MOVE_TO_DOC);
		o365HomeAction.deleteFile(getWebDriver(), fileName);
		o365HomeAction.createOneNote(getWebDriver(), appName, fileName);
		
		//o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		//o365HomeAction.loadOnedriveApp(getWebDriver());
		o365HomeAction.loadOnedriveAppViaLink(getWebDriver(), getOneDriveBusinessUrl());
		o365HomeAction.goToFolder(getWebDriver(),  GatewayTestConstants.O365_MOVE_TO_DOC);
		
		if (o365HomeAction.verifyFolderSelected(getWebDriver(), GatewayTestConstants.O365_MOVE_TO_DOC)) {
			Logger.info("Insdie the folder " + GatewayTestConstants.O365_MOVE_TO_DOC);
		}
		
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Create");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User created new "+ createdName + " " +fileName);
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "OneNote notebook");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("Create OneNote Successful");
	}
	
	@Priority(11)
	@Test(groups ={"Regression2", "REACH"}) 
	public void o365_Test_OneNoteFile_Rename() throws Exception {
		fromTime=backend.getCurrentTime();
		printCredentials();
		String fileName = "OneNoteFile";
		String extension = ""; 
		String renameFile = "onenoterename"; 
		Logger.info("Rename File");
		//o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		//o365HomeAction.loadOnedriveApp(getWebDriver());
		o365HomeAction.loadOnedriveAppViaLink(getWebDriver(), getOneDriveBusinessUrl());
		o365HomeAction.goToFolder(getWebDriver(),  GatewayTestConstants.O365_MOVE_TO_DOC);
		
		o365HomeAction.renameItemtopmenu(getWebDriver(), fileName, renameFile);
		o365HomeAction.renameItemtopmenu(getWebDriver(), renameFile, fileName.replace(extension, ""));
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Rename");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User renamed item from " +  
				fileName + " to " + renameFile);
		expectedDataMap.put(GatewayTestConstants.OBJECT_NAME, renameFile);
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("OneNote Rename File Successful");
	}
	
	@Priority(12)
	@Test(groups ={"Regression2", "REACH"}) 
	public void o365_Test_OneNoteFile_Move() throws Exception {
		fromTime=backend.getCurrentTime();
		printCredentials();
		String fileName = "OneNoteFile";
		Logger.info("Move File");
		//o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		//o365HomeAction.loadOnedriveApp(getWebDriver());
		o365HomeAction.loadOnedriveAppViaLink(getWebDriver(), getOneDriveBusinessUrl());
		o365HomeAction.goToFolder(getWebDriver(),  "test");
		o365HomeAction.moveItem(getWebDriver(),  fileName, GatewayTestConstants.O365_MOVE_TO_FOLDER);
		Logger.info("Go To Folder File got Moved");
		//o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		//o365HomeAction.loadOnedriveApp(getWebDriver());
		o365HomeAction.loadOnedriveAppViaLink(getWebDriver(), getOneDriveBusinessUrl());
		o365HomeAction.goToFolder(getWebDriver(),  GatewayTestConstants.O365_MOVE_TO_FOLDER);
		Logger.info("Go To Parent Folder");
		o365HomeAction.moveItem(getWebDriver(), fileName,  GatewayTestConstants.O365_MOVE_TO_DOC);
		Logger.info("Completed Move File");
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Move");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User moved item " + GatewayTestConstants.O365_MOVE_TO_FOLDER + 
				"/" + fileName +" to " + GatewayTestConstants.O365_MOVE_TO_DOC);
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("OneNote Move File Successful");
	}
	
	@Priority(13)
	@Test(groups ={"Regression2", "REACH"}) 
	public void o365_Test_OneNoteFile_Copy() throws Exception {
		fromTime=backend.getCurrentTime();
		printCredentials();
		String fileName = "OneNoteFile";
		Logger.info("Copy File");
		//o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		//o365HomeAction.loadOnedriveApp(getWebDriver());
		o365HomeAction.loadOnedriveAppViaLink(getWebDriver(), getOneDriveBusinessUrl());
		o365HomeAction.goToFolder(getWebDriver(),  GatewayTestConstants.O365_MOVE_TO_DOC);
		o365HomeAction.copyItem(getWebDriver(),  fileName, GatewayTestConstants.O365_MOVE_TO_FOLDER);
		//o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		//o365HomeAction.loadOnedriveApp(getWebDriver());
		o365HomeAction.loadOnedriveAppViaLink(getWebDriver(), getOneDriveBusinessUrl());
		o365HomeAction.goToFolder(getWebDriver(), GatewayTestConstants.O365_MOVE_TO_FOLDER);
		o365HomeAction.deleteFile(getWebDriver(), fileName);
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Copy");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User copied item " + GatewayTestConstants.O365_MOVE_TO_DOC + "/" +fileName + " to " + GatewayTestConstants.O365_MOVE_TO_FOLDER);
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("OneNote Copy File Successful");
	}
	
	@Priority(14)
	@Test(groups ={"Regression2", "REACH"}) 
	public void o365_Test_OneNoteFile_Share() throws Exception {
		fromTime=backend.getCurrentTime();
		printCredentials();
		String fileName = "OneNoteFile";
		Logger.info("Share App File");
		//o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		//o365HomeAction.loadOnedriveApp(getWebDriver());
		o365HomeAction.loadOnedriveAppViaLink(getWebDriver(), getOneDriveBusinessUrl());
		o365HomeAction.goToFolder(getWebDriver(),  GatewayTestConstants.O365_MOVE_TO_DOC);
		o365HomeAction.shareItemViaEmail(getWebDriver(), fileName, suiteData.getTestUsername());
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Share");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User sent email invitation(s) to " 
				 + suiteData.getTestUsername()+ " for " + fileName + ".");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("Share File Successful");
	}
	
	@Priority(15)
	@Test(groups ={"Regression2", "REACH"}) 
	public void o365_Test_OneNoteFile_Delete() throws Exception {
		fromTime=backend.getCurrentTime();
		printCredentials();
		String fileName = "OneNoteFile";
		Logger.info("Delete File");
		//o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		//o365HomeAction.loadOnedriveApp(getWebDriver());
		o365HomeAction.loadOnedriveAppViaLink(getWebDriver(), getOneDriveBusinessUrl());
		o365HomeAction.goToFolder(getWebDriver(),  "test");
		o365HomeAction.deleteFile(getWebDriver(),  fileName);
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Delete");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User sent " + fileName + " to the Recycle Bin");
		expectedDataMap.put(GatewayTestConstants.OBJECT_NAME, fileName);
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Folder");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("OneNote Delete File Successful");
	}
	
	
	public String getSaasAppUserName(){
		return suiteData.getSaasAppUsername().replaceAll("@", "_");
	}
	
	
	
}