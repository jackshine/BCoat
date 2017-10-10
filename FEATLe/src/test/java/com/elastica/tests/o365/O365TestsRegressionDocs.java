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


public class O365TestsRegressionDocs extends GWCommonTest{
	Map <String, Object> expectedDataMap = new HashMap<String, Object>();
	Map<String, String>policyDataMap= new HashMap<String, String>(); 
	String fromTime=backend.getCurrentTime();
	
	@BeforeMethod()
	public void clearDataMap(){
		expectedDataMap.clear();
	}

	@Priority(1)
	@Test(groups ={"Regression2"}, retryAnalyzer=RetryAnalyzer.class)
	public void performingActivitiesOnSaasAppo365() throws Exception {
		fromTime=backend.getCurrentTime();
		printCredentials();
		Logger.info("Started performing activities on O365 Powerpoint and Word");
		login.loginCloudSocPortal(getWebDriver(), suiteData);
		Logger.info("Finished login activities on cloudSoc");
	}
	
	@Priority(2)
	@Test(groups ={"Regression2", "REACH"},retryAnalyzer=RetryAnalyzer.class)  //
	public void o365_Test_01_ValidateLogingActivityEvent() throws Exception {
		Logger.info("Verifying the login event");
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
		Logger.info("Login Event Successful");
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getGWHeaders(suiteData));
	}

		
	@Priority(3)
	@Test(groups ={"Regression2", "REACH"}) 
	public void o365_Test_PowerPointFile_Create() throws Exception {
		fromTime=backend.getCurrentTime();
		printCredentials();
		String appName = "PowerPoint presentation";
		String fileName = "Presentation.pptx";
		String extension = ".pptx"; 
		Logger.info("App Name " + appName + "File Name " + fileName + "extension" + extension);
		Logger.info("Create Powerpoint");
		
		//o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		//o365HomeAction.loadOnedriveApp(getWebDriver());
		o365HomeAction.loadOnedriveAppViaLink(getWebDriver(), getOneDriveBusinessUrl());
		o365HomeAction.goToFolder(getWebDriver(),  GatewayTestConstants.O365_MOVE_TO_DOC);
		if (o365HomeAction.verifyFolderSelected(getWebDriver(), GatewayTestConstants.O365_MOVE_TO_DOC)) {
			o365HomeAction.deleteAllFiles(getWebDriver());
		}
		
		o365HomeAction.createItem(getWebDriver(), appName, fileName);
		String createdName = "presentation";
		
		//o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		//o365HomeAction.loadOnedriveApp(getWebDriver());
		o365HomeAction.loadOnedriveAppViaLink(getWebDriver(), getOneDriveBusinessUrl());
		o365HomeAction.goToFolder(getWebDriver(),  GatewayTestConstants.O365_MOVE_TO_DOC);
		
		if (o365HomeAction.verifyFolderSelected(getWebDriver(), GatewayTestConstants.O365_MOVE_TO_DOC)) {
			Logger.info("Inside the folder " + GatewayTestConstants.O365_MOVE_TO_DOC);
		}
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Create");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User created new "+ createdName + " " +fileName + " in browser");
		expectedDataMap.put(GatewayTestConstants.OBJECT_NAME, fileName);
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "PowerPoint presentation");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("Create Powerpoint Successful");
	}
	
	@Priority(4)
	@Test(groups ={"Regression2", "REACH"} )   //dependsOnMethods = { "o365_Test_PowerPointFile_Create" }
	public void o365_Test_PowerPointFile_Download() throws Exception {
		fromTime=backend.getCurrentTime();
		printCredentials();
		String fileName = "Presentation.pptx";
		Logger.info("Download File");
		o365HomeAction.downloadItemFileByName(getWebDriver(), fileName);
		
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.FILE_SIZE, "2");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User downloaded " + fileName);
		expectedDataMap.put(GatewayTestConstants.OBJECT_NAME, fileName);
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Document");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("Powerpoint Download File Successful");
	}
	
	@Priority(5)
	@Test(groups ={"Regression2", "REACH"})  //dependsOnMethods = { "o365_Test_PowerPointFile_Create" } 
	public void o365_Test_PowerPointFile_Rename() throws Exception {
		fromTime=backend.getCurrentTime();
		printCredentials();
		String fileName = "Presentation.pptx";
		String extension = ".pptx"; 
		String renameFile = "powerpointrename"; 
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
		Logger.info("Powerpoint Rename File Successful");
	}
	
	@Priority(6)
	@Test(groups ={"Regression2", "REACH"})  //dependsOnMethods = { "o365_Test_PowerPointFile_Create" }
	public void o365_Test_PowerPointFile_Move() throws Exception {
		fromTime=backend.getCurrentTime();
		printCredentials();
		String fileName = "Presentation.pptx";
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
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Document/Folder");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("Powerpoint Move File Successful");
	}
	
	@Priority(7)
	@Test(groups ={"Regression2", "REACH"})    //dependsOnMethods = { "o365_Test_PowerPointFile_Create" }
	public void o365_Test_PowerPointFile_Copy() throws Exception {
		fromTime=backend.getCurrentTime();
		printCredentials();
		String fileName = "Presentation.pptx";
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
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Document/Folder");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("Powerpoint Copy File Successful");
	}
	
	@Priority(8)
	@Test(groups ={"Regression2", "REACH"})    //dependsOnMethods = { "o365_Test_PowerPointFile_Create" }
	public void o365_Test_PowerPointFile_Share() throws Exception {
		fromTime=backend.getCurrentTime();
		printCredentials();
		String fileName = "Presentation.pptx";
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
		Logger.info("Powerpoint Share Successful");
	}
	
	@Priority(9)
	@Test(groups ={"Regression2", "REACH"}) 
	public void o365_Test_PowerPointFile_Delete() throws Exception {
		fromTime=backend.getCurrentTime();
		printCredentials();
		String fileName = "Presentation.pptx";
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
		Logger.info("Powerpoint Delete File Successful");
	}
	
	@Priority(10)
	@Test(groups ={"Regression2", "REACH"}) 
	public void o365_Test_WordDocumentFile_Create() throws Exception {
		fromTime=backend.getCurrentTime();
		printCredentials();
		String appName = "Word document";
		String fileName = "Document.docx";
		String extension = ".docx"; 
		Logger.info("App Name " + appName + "File Name " + fileName + "extension" + extension);
		Logger.info("Create WordDocument");
		//o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		//o365HomeAction.loadOnedriveApp(getWebDriver());
		o365HomeAction.loadOnedriveAppViaLink(getWebDriver(), getOneDriveBusinessUrl());
		o365HomeAction.goToFolder(getWebDriver(),  GatewayTestConstants.O365_MOVE_TO_DOC);
		o365HomeAction.createItem(getWebDriver(), appName, fileName);

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
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User created new word document named " +fileName + " in browser");
		expectedDataMap.put(GatewayTestConstants.OBJECT_NAME, fileName);
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Document");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("Create WordDocument Successful");
	}
	
	@Priority(11)
	@Test(groups ={"Regression2", "REACH"}) 
	public void o365_Test_WordDocumentFile_Download() throws Exception {
		fromTime=backend.getCurrentTime();
		printCredentials();
		String fileName = "Document.docx";
		Logger.info("Download File");
		o365HomeAction.downloadItemFileByName(getWebDriver(), fileName);
		
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Download");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User downloaded " + fileName);
		expectedDataMap.put(GatewayTestConstants.OBJECT_NAME, fileName);
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Document");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("WordDocument Download File Successful");
	}
	
	@Priority(12)
	@Test(groups ={"Regression2", "REACH"}) 
	public void o365_Test_WordDocumentFile_Rename() throws Exception {
		fromTime=backend.getCurrentTime();
		printCredentials();
		String fileName = "Document.docx";
		String extension = ".docx"; 
		String renameFile = "worddocumentrename"; 
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
		Logger.info("WordDocument Rename File Succesful");
	}
	
	@Priority(13)
	@Test(groups ={"Regression2", "REACH"}) 
	public void o365_Test_WordDocumentFile_Move() throws Exception {
		fromTime=backend.getCurrentTime();
		printCredentials();
		String fileName = "Document.docx";
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
		
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Move");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User moved item " + GatewayTestConstants.O365_MOVE_TO_FOLDER + 
				"/" + fileName +" to " + GatewayTestConstants.O365_MOVE_TO_DOC);
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Document/Folder");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("WordDocument Move File Successful");
	}
	
	@Priority(14)
	@Test(groups ={"Regression2", "REACH"}) 
	public void o365_Test_WordDocumentFile_Copy() throws Exception {
		fromTime=backend.getCurrentTime();
		printCredentials();
		String fileName = "Document.docx";
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
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Document/Folder");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("WordDocument Copy Successful");
	}
	
	@Priority(15)
	@Test(groups ={"Regression2", "REACH"}) 
	public void o365_Test_WordDocumentFile_Share() throws Exception {
		fromTime=backend.getCurrentTime();
		String fileName = "Document.docx";
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
		Logger.info("Download activity event verification successfull");
		Logger.info("WordDocument Share Successful");
	}
	
	@Priority(16)
	@Test(groups ={"Regression2", "REACH"}) 
	public void o365_Test_WordDocumentFile_Delete() throws Exception {
		fromTime=backend.getCurrentTime();
		printCredentials();
		String fileName = "Document.docx";
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
		Logger.info("Logout event verification successfull");
		Logger.info("WordDocument Delete Successful");
	}
	
	
	public String getSaasAppUserName(){
		return suiteData.getSaasAppUsername().replaceAll("@", "_");
	}
	
}