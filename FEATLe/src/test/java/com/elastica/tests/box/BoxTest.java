package com.elastica.tests.box;
import static org.testng.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.elastica.action.protect.ProtectAction;
import com.elastica.action.protect.ProtectDTO;
import com.elastica.common.GWCommonTest;
import com.elastica.common.SuiteData;
import com.elastica.gateway.GatewayTestConstants;
import com.elastica.listeners.Priority;
import com.elastica.listeners.RetryAnalyzer;
import com.elastica.logger.Logger;
import com.elastica.restClient.Client;


public class BoxTest extends GWCommonTest{
	Map <String, Object> expectedDataMap = new HashMap<String, Object>();
	Map<String, String>policyDataMap= new HashMap<String, String>(); 
	String fromTime= "";
	String folderNameFolderOper ="";
	String folderNameFileOper ="";
	String filePath = GatewayTestConstants.BOX_ORIGINAL_FILE_PATH;
	String fileNameBoxFile ="";
	String fileNameEncrypt ="";
	String encryptPolicyName ="";
	String fileuploadPolicyName ="";
	String accessEnforcementPolicyName ="";
	String fileSharingPolicyName ="";
	String fileNameFileOper ="";
	String fileName_6mb ="";
	String fileName_10mb ="";
	String fileName_20mb ="";
	String fileNameFolderOper ="";
	String fileCopied ="";
	String folderCopied ="";
	String bookmarkUrl ="http://yahoo.com";
	String homePageUrl ="";
	String shareWithUser="admin@gatewaybeatle.com";
	String collaborator="box-admin@gatewaybeatle.com";
	String uploadFile= "upload.pdf";
	String uploadFile_6mb= "6MB_Upload.zip";
	String uploadFile_10mb= "10MB_Upload.zip";
	String uploadFile_20mb= "20MB_Upload.dmg";
	String tagName= "important, mydocs";
	String commentOnFile= "This is my comment";
	String userName= "";
	String customerSeparationLogFolderName= "";
	ArrayList<File> filesToDeleteFromWorkspace  = new ArrayList<File>();
	ArrayList<String> filesToDeleteFromSaasApp  = new ArrayList<String>();
	SoftAssert softAsst = new SoftAssert();
	
	ProtectAction protectAction = new ProtectAction();
	ProtectDTO protectData = new ProtectDTO();
	
//	@Priority(50)
	@AfterSuite(alwaysRun=true)
	public void cleanup() throws InterruptedException {
//		delete the temp file from framework upload folder
		box.deleteFiles(filesToDeleteFromWorkspace);
		
		//delete test created files and folders one by one
//		if(filesToDeleteFromSaasApp.size()>0){
//			box.gotoHomePage(getWebDriver(),homePageUrl);
//			box.deleteMultipleItems(getWebDriver(), filesToDeleteFromSaasApp) ;
//		}
		
		
	} 
	
	public void setup() throws Exception{
		
		Reporter.log("Inside Setup Method", true);
		if(suiteData.getAccountType().equals("External")){
			userName = suiteData.getTestUsername();
			homePageUrl=suiteData.getSaasAppBaseUrl();
		}
		else{
			userName = suiteData.getSaasAppUsername();
			homePageUrl="https://elasticaqa1.app.box.com/files/";
		}
		Reporter.log("============userName="+userName,true);
		fileuploadPolicyName = "FileUploadPolicy_"+suiteData.getTestUsername().replaceAll("@", "_");
		accessEnforcementPolicyName = "AccessEnforcementPolicy_";
		fileSharingPolicyName = "FileSharingPolicy_";
		//encryption policy name and logs
		encryptPolicyName = GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME+suiteData.getTestUsername().replaceAll("@", "_");
		Reporter.log("============policname="+encryptPolicyName,true);
		policy.deleteEncryptionDecryptionPolicy(encryptPolicyName, suiteData, backend.getHeaders(suiteData));
		
		customerSeparationLogFolderName=getSuiteParameter("customerSeparationLogFolderName");
	
		folderNameFolderOper=box.getTimestamp()+"folder";
		filesToDeleteFromSaasApp.add(folderNameFolderOper);
		
		fileNameFileOper=box.createFileForUpload(uploadFile);
		filesToDeleteFromSaasApp.add(fileNameFileOper);
		filesToDeleteFromWorkspace.add(new File(filePath+fileNameFileOper));
		
		folderNameFileOper=box.getTimestamp()+"fileOper";
		filesToDeleteFromSaasApp.add(folderNameFileOper);
		
		fileNameBoxFile=box.getTimestamp();
		
		Logger.info("Foldername="+folderNameFolderOper);
		Logger.info("Filename="+folderNameFolderOper);
		
		fromTime =new org.joda.time.DateTime( org.joda.time.DateTimeZone.UTC ).minusMinutes(1).toString();
	}
	
	@Priority(1)
	@Test(groups ={"SANITY","REGRESSION","P1","P2","CLOUDSOC"}, retryAnalyzer=RetryAnalyzer.class)
	public void loginToCloudSocAppAndSetupSSO() throws Exception {
		
		Logger.info("Start Login to cloudSoc");
		printCredentials();
		login.loginCloudSocPortal(getWebDriver(), suiteData);
		Logger.info("Completed Login to cloudSoc");
	}
	
	@Priority(2)
	@Test(groups ={"SANITY","REGRESSION","P1", "REACH"} , retryAnalyzer=RetryAnalyzer.class)
	public void validateLoginEvent() throws Exception {
		
		setup();
		Logger.info("Verifying the Login Event");
		printCredentials();
		box.login(getWebDriver(), suiteData);
		
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Login");
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Session");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User logged in"); 
		expectedDataMap.put(GatewayTestConstants.REFERER_URI, "NOT_EMPTY");
//		expectedDataMap.put(GatewayTestConstants.REQ_URI, "https://app.box.com/login");
//		expectedDataMap.put(GatewayTestConstants.RESP_CODE, "302");
		
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("Log verification successful for method:"+Thread.currentThread().getStackTrace()[1].getMethodName(), true);
		
		box.deleteAllItems(getWebDriver());
		
	}
	@Priority(3)
	@Test(groups ={"REGRESSION","P2"}, dependsOnMethods = { "validateLoginEvent" }, retryAnalyzer=RetryAnalyzer.class)
	public void validateHomeFolderView() throws Exception {
		Logger.info("Verifying the Home Folder View event");
		printCredentials();
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "View");
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Folder");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User viewed Files and Folders page"); 
		expectedDataMap.put(GatewayTestConstants.REFERER_URI, "NOT_EMPTY");
//		expectedDataMap.put(GatewayTestConstants.RESP_CODE, "200");
		
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("Log verification successful for method:"+Thread.currentThread().getStackTrace()[1].getMethodName(), true);
		
	}
	

	@Priority(4)
	@Test(groups ={"REGRESSION","SANITY","P1","REACH"}, dependsOnMethods = { "validateLoginEvent" }, retryAnalyzer=RetryAnalyzer.class)
	public void validateFolderCreate() throws Exception {
		Logger.info("Verifying the Folder Create Event");
		printCredentials();
		box.gotoHomePage(getWebDriver(),homePageUrl);
		box.createFolder(getWebDriver(),folderNameFolderOper);
		box.openItem(getWebDriver(),folderNameFolderOper,15);
//		box.uploadFile(getWebDriver(), filePath+fileNameFolderOper, 15);
		box.gotoHomePage(getWebDriver(),homePageUrl);
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Create");
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Folder");
		expectedDataMap.put(GatewayTestConstants.OBJECT_NAME, folderNameFolderOper);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User created new folder "+folderNameFolderOper); 
		expectedDataMap.put(GatewayTestConstants.REFERER_URI, "");
//		expectedDataMap.put(GatewayTestConstants.RESP_CODE, "200");
		
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("Log verification successful for method:"+Thread.currentThread().getStackTrace()[1].getMethodName(), true);
		
	}
	
	@Priority(5)
	@Test(groups ={"REGRESSION","P2"}, dependsOnMethods = { "validateFolderCreate" }, retryAnalyzer=RetryAnalyzer.class)
	public void validateFolderView() throws Exception {
		Logger.info("Verifying the Created Folder View Event");
		expectedDataMap.clear();
		printCredentials();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "View");
//		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Folder");
		expectedDataMap.put(GatewayTestConstants.OBJECT_NAME, folderNameFolderOper);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User viewed contents of folder "+folderNameFolderOper); 
		expectedDataMap.put(GatewayTestConstants.REFERER_URI, "");
//		expectedDataMap.put(GatewayTestConstants.RESP_CODE, "200");
		
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("Log verification successful for method:"+Thread.currentThread().getStackTrace()[1].getMethodName(), true);
		
	}
	@Priority(6)
	@Test(groups ={"SANITY","REGRESSION","P1", "REACH"},  dependsOnMethods = { "validateLoginEvent" }, retryAnalyzer=RetryAnalyzer.class)
	public void validateFolderSharePeopleWithLink() throws Exception {
		Logger.info("Verifying the Folder Share Event");
		printCredentials();
		box.gotoHomePage(getWebDriver(),homePageUrl);
		box.createFolderIfNotPresent(getWebDriver(),folderNameFolderOper);
		box.shareItem(getWebDriver(), folderNameFolderOper,"people_with_link", shareWithUser, 5);
		box.unshareItem(getWebDriver(), folderNameFolderOper, 5);
		
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Share");
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File/Folder");
		expectedDataMap.put(GatewayTestConstants.OBJECT_NAME, folderNameFolderOper);
		expectedDataMap.put(GatewayTestConstants.SHARED_WITH, shareWithUser);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User shared "+folderNameFolderOper+" with "+shareWithUser); 
		expectedDataMap.put(GatewayTestConstants.REFERER_URI, "");
		
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("Log verification successful for method:"+Thread.currentThread().getStackTrace()[1].getMethodName(), true);
	}

	@Priority(7)
	@Test(groups ={"REGRESSION","P1"}, dependsOnMethods = { "validateFolderSharePeopleWithLink" }, retryAnalyzer=RetryAnalyzer.class)
	public void validateFolderShareCreatedLink() throws Exception {
		Logger.info("Verifying the Folder Share Got Link Event");
		printCredentials();
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Share");
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Folder");
		expectedDataMap.put(GatewayTestConstants.OBJECT_NAME, folderNameFolderOper);
		expectedDataMap.put(GatewayTestConstants.SHARED_WITH, "NOT_EMPTY");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User got link of folder "+folderNameFolderOper); 
		expectedDataMap.put(GatewayTestConstants.REFERER_URI, "");
		
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("Log verification successful for method:"+Thread.currentThread().getStackTrace()[1].getMethodName(), true);
	}

	@Priority(8)
	@Test(groups ={"REGRESSION","P"}, dependsOnMethods = { "validateFolderSharePeopleWithLink" })
	public void validateFolderUnshare() throws Exception {
		Logger.info("Verifying the Folder Unshare Event");
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Unshare");
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Folder");
		expectedDataMap.put(GatewayTestConstants.OBJECT_NAME, folderNameFolderOper);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User removed link of folder "+folderNameFolderOper); 
		expectedDataMap.put(GatewayTestConstants.REFERER_URI, "");
		
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("Log verification successful for method:"+Thread.currentThread().getStackTrace()[1].getMethodName(), true);
	}

	@Priority(9)
	@Test(groups ={"SANITY","REGRESSION","P1","BUSINESS","REACH"}, dependsOnMethods = { "validateLoginEvent"  }, retryAnalyzer=RetryAnalyzer.class)
	public void validateFolderSharePeopleInCompany() throws Exception {
		Logger.info("Verifying the Folder Share Event - with people in company");
		printCredentials();
		box.gotoHomePage(getWebDriver(),homePageUrl);
		box.createFolderIfNotPresent(getWebDriver(),folderNameFolderOper);
		box.shareItem(getWebDriver(), folderNameFolderOper,"people_in_your_company", shareWithUser, 5);
		box.unshareItem(getWebDriver(), folderNameFolderOper, 5);
		
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Share");
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Folder");
		expectedDataMap.put(GatewayTestConstants.OBJECT_NAME, folderNameFolderOper);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User shared link of "+folderNameFolderOper+" with the people of company"); 
		expectedDataMap.put(GatewayTestConstants.REFERER_URI, "");
		
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("Log verification successful for method:"+Thread.currentThread().getStackTrace()[1].getMethodName(), true);
	}

	@Priority(10)
	@Test(groups ={"REGRESSION","P1"}, dependsOnMethods = {  "validateLoginEvent" }, retryAnalyzer=RetryAnalyzer.class)
	public void validateFolderSharePeopleInFolder() throws Exception {
		Logger.info("Verifying the Folder Share Event - with people in Folder");
		printCredentials();
		box.gotoHomePage(getWebDriver(),homePageUrl);
		box.createFolderIfNotPresent(getWebDriver(),folderNameFolderOper);
		box.shareWithCollaborators(getWebDriver(), folderNameFolderOper, collaborator, 10);
		box.shareItem(getWebDriver(), folderNameFolderOper,"people_in_this_folder", collaborator, 5);
		
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Share");
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Folder");
		expectedDataMap.put(GatewayTestConstants.OBJECT_NAME, folderNameFolderOper);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User shared link of "+folderNameFolderOper+" with people in this folder"); 
		expectedDataMap.put(GatewayTestConstants.REFERER_URI, "");
		
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("Log verification successful for method:"+Thread.currentThread().getStackTrace()[1].getMethodName(), true);
	}

	@Priority(11)
	@Test(groups ={"REGRESSION","P1"},  dependsOnMethods = {  "validateLoginEvent" }, retryAnalyzer=RetryAnalyzer.class)
	public void validateFolderShareEmail() throws Exception {
		Logger.info("Verifying the Folder Share by mail Event: Folder");
		
		box.gotoHomePage(getWebDriver(),homePageUrl);
		box.createFolderIfNotPresent(getWebDriver(),folderNameFolderOper);
		box.shareSelectedItemByMail(getWebDriver(), folderNameFolderOper, shareWithUser, 5);
		box.refresh(getWebDriver(), 10);
		
		
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Share");
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File/Folder");
		expectedDataMap.put(GatewayTestConstants.OBJECT_NAME, folderNameFolderOper);
		expectedDataMap.put(GatewayTestConstants.SHARED_WITH, shareWithUser);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User shared "+folderNameFolderOper+" with "+shareWithUser); 
		expectedDataMap.put(GatewayTestConstants.REFERER_URI, "NOT_EMPTY");
//		expectedDataMap.put(GatewayTestConstants.STATUS_CODE, "200");
		
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("Log verification successful for method:"+Thread.currentThread().getStackTrace()[1].getMethodName(), true);
	}

	@Priority(12)
	@Test(groups ={"REGRESSION","P1"}, dependsOnMethods = { "validateLoginEvent" }, retryAnalyzer=RetryAnalyzer.class)
	public void validateFolderCreateTag() throws Exception {
		Logger.info("Verifying the Folder Create Tag Event");
		Logger.info("ADB_ID:366");
		
		box.gotoHomePage(getWebDriver(),homePageUrl);
		box.createFolderIfNotPresent(getWebDriver(),folderNameFolderOper);
		//tag and untag file
		box.tagUntagSelectedItem(getWebDriver(), folderNameFolderOper, tagName, 10);
		box.tagUntagSelectedItem(getWebDriver(), folderNameFolderOper, "", 10);
		
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Create Tag");
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File/Folder");
		expectedDataMap.put(GatewayTestConstants.OBJECT_NAME, folderNameFolderOper);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User added tags "+tagName+" to "+folderNameFolderOper); 
		expectedDataMap.put(GatewayTestConstants.REFERER_URI, "NOT_EMPTY");
		
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("Log verification successful for method:"+Thread.currentThread().getStackTrace()[1].getMethodName(), true);
	}

	@Priority(13)
	@Test(groups ={"REGRESSION","P1"}, dependsOnMethods = { "validateFolderCreateTag" }, retryAnalyzer=RetryAnalyzer.class)
	public void validateFolderDeleteTag() throws Exception {
		Logger.info("Verifying the Folder Delete Tag Event");
		Logger.info("ADB_ID:367");
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Delete Tag");
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File/Folder");
		expectedDataMap.put(GatewayTestConstants.OBJECT_NAME, folderNameFolderOper);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User deleted tags from "+folderNameFolderOper); 
		expectedDataMap.put(GatewayTestConstants.REFERER_URI, "NOT_EMPTY");
		
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("Log verification successful for method:"+Thread.currentThread().getStackTrace()[1].getMethodName(), true);
	}

	@Priority(14)
	@Test(groups ={"REGRESSION","P1"}, dependsOnMethods={"validateLoginEvent"}, retryAnalyzer=RetryAnalyzer.class)
	public void validateFolderCopy() throws Exception {
		Logger.info("Verifying the File/Folder Copy Event: Folder");
		
		box.gotoHomePage(getWebDriver(),homePageUrl);
		box.createFolderIfNotPresent(getWebDriver(),folderNameFolderOper);
		box.copySelectedItem(getWebDriver(), folderNameFolderOper, 5);
		folderCopied=folderNameFolderOper+" (1)"; //forming copied foldername
		filesToDeleteFromSaasApp.add(folderCopied);
		box.moveSelectedItem(getWebDriver(), folderCopied,folderNameFolderOper, 5);
		
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Copy");
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File/Folder");
		expectedDataMap.put(GatewayTestConstants.OBJECT_NAME, folderNameFolderOper);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User copied item(s) "+folderNameFolderOper+" to All Files"); 
		expectedDataMap.put(GatewayTestConstants.REFERER_URI, "NOT_EMPTY");
		
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("Log verification successful for method:"+Thread.currentThread().getStackTrace()[1].getMethodName(), true);
	}

	@Priority(15)
	@Test(groups ={"REGRESSION","P1"}, dependsOnMethods={"validateFolderCopy"}, retryAnalyzer=RetryAnalyzer.class)
	public void validateFolderMove() throws Exception {
		Logger.info("Verifying the File/Folder Move Event: Folder");
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Move");
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File/Folder");
		expectedDataMap.put(GatewayTestConstants.OBJECT_NAME, folderNameFolderOper);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User moved item(s) "+folderCopied+" to "+folderNameFolderOper); 
		
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("Log verification successful for method:"+Thread.currentThread().getStackTrace()[1].getMethodName(), true);
	}

	@Priority(16)
	@Test(groups ={"REGRESSION","P1"}, dependsOnMethods = { "validateLoginEvent" })
	public void validateFolderDelete() throws Exception {
		Logger.info("Verifying the Folder Delete Event");
		
		box.gotoHomePage(getWebDriver(),homePageUrl);
		box.createFolderIfNotPresent(getWebDriver(),folderNameFolderOper);
		box.deleteSelectedItem(getWebDriver(),folderNameFolderOper);
		
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Delete");
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File/Folder");
		expectedDataMap.put(GatewayTestConstants.OBJECT_NAME, folderNameFolderOper);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User deleted object(s) "+folderNameFolderOper); 
		expectedDataMap.put(GatewayTestConstants.REFERER_URI, "");
//		expectedDataMap.put(GatewayTestConstants.RESP_CODE, "200");
//		expectedDataMap.put(GatewayTestConstants.STATUS_CODE, "200");
		
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("Log verification successful for method:"+Thread.currentThread().getStackTrace()[1].getMethodName(), true);
		
	}
	

	@Priority(17)
	@Test(groups ={"SANITY","REGRESSION","P1", "REACH"}, dependsOnMethods = {"validateLoginEvent"}, retryAnalyzer=RetryAnalyzer.class)
	public void validateFileUpload() throws Exception {
		Logger.info("Verifying the File Upload Event");
		printCredentials();
		box.gotoHomePage(getWebDriver(),homePageUrl);
		box.uploadFile(getWebDriver(),filePath+fileNameFileOper , 15);
		
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Upload");
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File");
		expectedDataMap.put(GatewayTestConstants.OBJECT_NAME, fileNameFileOper);
		expectedDataMap.put(GatewayTestConstants.FILE_SIZE, "57");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User uploaded file "+fileNameFileOper+" to folder All Files"); 
		expectedDataMap.put(GatewayTestConstants.REFERER_URI, "");
		
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("Log verification successful for method:"+Thread.currentThread().getStackTrace()[1].getMethodName(), true);
		
	}
	
	@Priority(18)
	@Test(groups ={"REGRESSION","P2"}, dependsOnMethods = { "validateLoginEvent" }, retryAnalyzer=RetryAnalyzer.class)
	public void validateFileView() throws Exception {
		Logger.info("Verifying the File View Event");
		
		box.gotoHomePage(getWebDriver(),homePageUrl);
		box.uploadFileIfNotPresent(getWebDriver(),filePath+fileNameFileOper , 15);
		box.openItem(getWebDriver(), fileNameFileOper,15);
		box.closePreview(getWebDriver());

		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "View");
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File/Folder");
		expectedDataMap.put(GatewayTestConstants.OBJECT_NAME, fileNameFileOper);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User previewed "+fileNameFileOper); 
		expectedDataMap.put(GatewayTestConstants.REFERER_URI, "");
		
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("Log verification successful for method:"+Thread.currentThread().getStackTrace()[1].getMethodName(), true);
	}
	@Priority(19)
	@Test(groups ={"SANITY","REGRESSION","P1", "REACH"}, dependsOnMethods = { "validateLoginEvent" }, retryAnalyzer=RetryAnalyzer.class)
	public void validateFileDownload() throws Exception {
		Logger.info("Verifying the File Download Event");
		printCredentials();
		box.gotoHomePage(getWebDriver(),homePageUrl);
		box.uploadFileIfNotPresent(getWebDriver(),filePath+fileNameFileOper , 15);
		box.downloadSelectedItem(getWebDriver(), fileNameFileOper, 10,suiteData.getBrowser());
		
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Download");
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File");
		expectedDataMap.put(GatewayTestConstants.OBJECT_NAME, fileNameFileOper);
		expectedDataMap.put(GatewayTestConstants.FILE_SIZE, "57");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User downloaded "+fileNameFileOper); 
		expectedDataMap.put(GatewayTestConstants.REFERER_URI, "");
		
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("Log verification successful for method:"+Thread.currentThread().getStackTrace()[1].getMethodName(), true);
	}
	
	@Priority(20)
	@Test(groups ={"SANITY","REGRESSION","P1", "REACH"}, dependsOnMethods = { "validateLoginEvent" }, retryAnalyzer=RetryAnalyzer.class)
	public void validateFileSharePeopleWithLink() throws Exception {
		Logger.info("Verifying the File/Folder Share Event");
		printCredentials();
		box.gotoHomePage(getWebDriver(),homePageUrl);
		box.uploadFileIfNotPresent(getWebDriver(),filePath+fileNameFileOper , 15);
		box.shareItem(getWebDriver(), fileNameFileOper,"people_with_link", shareWithUser, 10);
		box.unshareItem(getWebDriver(), fileNameFileOper, 10);
		
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Share");
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File/Folder");
		expectedDataMap.put(GatewayTestConstants.OBJECT_NAME, fileNameFileOper);
		expectedDataMap.put(GatewayTestConstants.SHARED_WITH, shareWithUser);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User shared "+fileNameFileOper+" with "+shareWithUser); 
		expectedDataMap.put(GatewayTestConstants.REFERER_URI, "");
		
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("Log verification successful for method:"+Thread.currentThread().getStackTrace()[1].getMethodName(), true);
	}

	@Priority(21)
	@Test(groups ={"REGRESSION","P1"}, dependsOnMethods = { "validateFileSharePeopleWithLink" })
	public void validateFileShareCreatedLink() throws Exception {
		Logger.info("Verifying the File Share Got Link Event");
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Share");
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File");
		expectedDataMap.put(GatewayTestConstants.OBJECT_NAME, fileNameFileOper);
		expectedDataMap.put(GatewayTestConstants.SHARED_WITH, "NOT_EMPTY");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User got link of file "+fileNameFileOper); 
		expectedDataMap.put(GatewayTestConstants.REFERER_URI, "");
		
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("Log verification successful for method:"+Thread.currentThread().getStackTrace()[1].getMethodName(), true);
	}

	
	@Priority(22)
	@Test(groups ={"REGRESSION","P1"}, dependsOnMethods = { "validateFileSharePeopleWithLink" })
	public void validateFileUnshare() throws Exception {
		Logger.info("Verifying the File Unshare Event");
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Unshare");
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File");
		expectedDataMap.put(GatewayTestConstants.OBJECT_NAME, fileNameFileOper);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User removed link of file "+fileNameFileOper); 
		expectedDataMap.put(GatewayTestConstants.REFERER_URI, "");
		
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("Log verification successful for method:"+Thread.currentThread().getStackTrace()[1].getMethodName(), true);
	}

	
	
	@Priority(23)
	@Test(groups ={"SANITY","REGRESSION","P1","BUSINESS", "REACH"}, dependsOnMethods = { "validateLoginEvent" }, retryAnalyzer=RetryAnalyzer.class)
	public void validateFileSharePeopleInCompany() throws Exception {
		Logger.info("Verifying the File Share Event - with people in company");
		printCredentials();
		box.gotoHomePage(getWebDriver(),homePageUrl);
		box.uploadFileIfNotPresent(getWebDriver(),filePath+fileNameFileOper , 15);
		box.shareItem(getWebDriver(), fileNameFileOper,"people_in_your_company", shareWithUser, 10);
		
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Share");
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File");
		expectedDataMap.put(GatewayTestConstants.OBJECT_NAME, fileNameFileOper);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User shared link of "+fileNameFileOper+" with the people of company"); 
		expectedDataMap.put(GatewayTestConstants.REFERER_URI, "");
		
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("Log verification successful for method:"+Thread.currentThread().getStackTrace()[1].getMethodName(), true);
	}

	@Priority(24)
	@Test(groups ={"REGRESSION","P1"}, dependsOnMethods = { "validateLoginEvent"}, retryAnalyzer=RetryAnalyzer.class)
	public void validateFileSharePeopleInFolder() throws Exception {
		Logger.info("Verifying the File Share Event - with people in Folder");
		
		
		box.gotoHomePage(getWebDriver(),homePageUrl);
		String myFolderName=box.getTimestamp()+"ToShare";;
		filesToDeleteFromSaasApp.add(myFolderName);
		box.createFolderIfNotPresent(getWebDriver(),myFolderName);
		box.shareWithCollaborators(getWebDriver(), myFolderName, collaborator, 10);
		box.openItem(getWebDriver(), myFolderName, 5);
		box.uploadFileIfNotPresent(getWebDriver(),filePath+fileNameFileOper , 15);
		box.shareItem(getWebDriver(), fileNameFileOper,"people_in_this_folder", collaborator, 10);
		
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Share");
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File");
		expectedDataMap.put(GatewayTestConstants.OBJECT_NAME, fileNameFileOper);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User shared link of "+fileNameFileOper+" with people in this folder"); 
		expectedDataMap.put(GatewayTestConstants.REFERER_URI, "");
		
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("Log verification successful for method:"+Thread.currentThread().getStackTrace()[1].getMethodName(), true);
	}

	
	
	@Priority(25)
	@Test(groups ={"REGRESSION","P1"},  dependsOnMethods = { "validateLoginEvent"}, retryAnalyzer=RetryAnalyzer.class)
	public void validateFileShareEmail() throws Exception {
		Logger.info("Verifying the File/Folder Share by mail Event:File");
		
		box.gotoHomePage(getWebDriver(),homePageUrl);
		box.uploadFileIfNotPresent(getWebDriver(),filePath+fileNameFileOper , 15);
		box.shareSelectedItemByMail(getWebDriver(), fileNameFileOper, shareWithUser, 5);
		box.refresh(getWebDriver(), 10);
		
		
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Share");
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File/Folder");
		expectedDataMap.put(GatewayTestConstants.OBJECT_NAME, fileNameFileOper);
		expectedDataMap.put(GatewayTestConstants.SHARED_WITH, shareWithUser);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User shared "+fileNameFileOper+" with "+shareWithUser); 
		expectedDataMap.put(GatewayTestConstants.REFERER_URI, "NOT_EMPTY");
//		expectedDataMap.put(GatewayTestConstants.STATUS_CODE, "200");
		
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("Log verification successful for method:"+Thread.currentThread().getStackTrace()[1].getMethodName(), true);
	}

	@Priority(26)
	@Test(groups ={"REGRESSION","P1","BUSINESS"}, dependsOnMethods = { "validateLoginEvent" }, retryAnalyzer=RetryAnalyzer.class)
	public void validateFileLock() throws Exception {
		Logger.info("ADB_ID:3778");
		Logger.info("Verifying the File Unlock Event");
		
		box.gotoHomePage(getWebDriver(),homePageUrl);
		box.uploadFileIfNotPresent(getWebDriver(),filePath+fileNameFileOper , 15);
		box.lockItem(getWebDriver(), fileNameFileOper, 10);
		box.unlockItem(getWebDriver(), fileNameFileOper, 10);
		
		String myfileName=fileNameFileOper;
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Lock");
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File");
		expectedDataMap.put(GatewayTestConstants.OBJECT_NAME, myfileName);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User locked "+myfileName +" for 0 seconds"); 
		expectedDataMap.put(GatewayTestConstants.REFERER_URI, "NOT_EMPTY");
		//expectedDataMap.put(GatewayTestConstants.STATUS_CODE, 200);
		
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("Log verification successful for method:"+Thread.currentThread().getStackTrace()[1].getMethodName(), true);
	}

	@Priority(27)
	@Test(groups ={"REGRESSION","P1","BUSINESS"}, dependsOnMethods = { "validateFileLock"})
	public void validateFileUnlock() throws Exception {
		Logger.info("Verifying the File Unlock Event");
		Logger.info("ADB_ID:3779");
		String myfileName=fileNameFileOper;
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Unlock");
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File");
		expectedDataMap.put(GatewayTestConstants.OBJECT_NAME, myfileName);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User unlocked "+myfileName); 
		expectedDataMap.put(GatewayTestConstants.REFERER_URI, "NOT_EMPTY");
		//expectedDataMap.put(GatewayTestConstants.STATUS_CODE, 200);
		
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("Log verification successful for method:"+Thread.currentThread().getStackTrace()[1].getMethodName(), true);
	}

	@Priority(28)
	@Test(groups ={"REGRESSION","P1"}, dependsOnMethods = { "validateLoginEvent" }, retryAnalyzer=RetryAnalyzer.class)
	public void validateFileCreateTag() throws Exception {
		Logger.info("Verifying the File/Folder Create Tag Event");
		Logger.info("ADB_ID:366");
		
		box.gotoHomePage(getWebDriver(),homePageUrl);
		box.uploadFileIfNotPresent(getWebDriver(),filePath+fileNameFileOper , 15);
		
		//tag and untag file
		box.tagUntagSelectedItem(getWebDriver(), fileNameFileOper, tagName, 10);
		box.tagUntagSelectedItem(getWebDriver(), fileNameFileOper, "", 10);
		
		
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Create Tag");
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File/Folder");
		expectedDataMap.put(GatewayTestConstants.OBJECT_NAME, fileNameFileOper);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User added tags "+tagName+" to "+fileNameFileOper); 
		expectedDataMap.put(GatewayTestConstants.REFERER_URI, "NOT_EMPTY");
		
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("Log verification successful for method:"+Thread.currentThread().getStackTrace()[1].getMethodName(), true);
	}

	@Priority(29)
	@Test(groups ={"REGRESSION","P1"}, dependsOnMethods = { "validateFileCreateTag" })
	public void validateFileDeleteTag() throws Exception {
		Logger.info("Verifying the File/Folder Delete Tag Event");
		Logger.info("ADB_ID:367");
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Delete Tag");
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File/Folder");
		expectedDataMap.put(GatewayTestConstants.OBJECT_NAME, fileNameFileOper);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User deleted tags from "+fileNameFileOper); 
		expectedDataMap.put(GatewayTestConstants.REFERER_URI, "NOT_EMPTY");
		
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("Log verification successful for method:"+Thread.currentThread().getStackTrace()[1].getMethodName(), true);
	}

	@Priority(30)
	@Test(groups ={"REGRESSION","P1"}, dependsOnMethods={"validateLoginEvent"}, retryAnalyzer=RetryAnalyzer.class)
	public void validateFileCopy() throws Exception {
		Logger.info("Verifying the File/Folder Copy Event: File");
		
		box.gotoHomePage(getWebDriver(),homePageUrl);
		box.uploadFileIfNotPresent(getWebDriver(),filePath+fileNameFileOper , 15);
		box.copySelectedItem(getWebDriver(), fileNameFileOper, 5);
		filesToDeleteFromSaasApp.add(fileNameFileOper.replace(".", " (1)."));
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Copy");
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File/Folder");
		expectedDataMap.put(GatewayTestConstants.OBJECT_NAME, fileNameFileOper);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User copied item(s) "+fileNameFileOper+" to All Files"); 
		expectedDataMap.put(GatewayTestConstants.REFERER_URI, "NOT_EMPTY");
		
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("Log verification successful for method:"+Thread.currentThread().getStackTrace()[1].getMethodName(), true);
		
	}

	@Priority(31)
	@Test(groups ={"REGRESSION","P1"}, dependsOnMethods={"validateLoginEvent"})
	public void validateFileMove() throws Exception {
		Logger.info("Verifying the File/Folder Move Event: File");


			box.gotoHomePage(getWebDriver(),homePageUrl);
			fileCopied=box.createFileForUpload(uploadFile);
			filesToDeleteFromSaasApp.add(fileCopied);
			filesToDeleteFromWorkspace.add(new File(filePath+fileCopied));
			box.uploadFile(getWebDriver(),filePath+fileCopied , 15);
			box.createFolder(getWebDriver(),folderNameFileOper);
			box.moveSelectedItem(getWebDriver(), fileCopied, folderNameFileOper, 5);

			expectedDataMap.clear();
			setCommonFieldsInExpectedDataMap(expectedDataMap);
			setLocationFieldsInExpectedDataMap(expectedDataMap);
			expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Move");
			expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File/Folder");
			expectedDataMap.put(GatewayTestConstants.OBJECT_NAME, fileCopied);
			expectedDataMap.put(GatewayTestConstants.MESSAGE, "User moved item(s) "+fileCopied+" to "+folderNameFileOper); 
			expectedDataMap.put(GatewayTestConstants.REFERER_URI, "NOT_EMPTY");

			Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
			Reporter.log("Log verification successful for method:"+Thread.currentThread().getStackTrace()[1].getMethodName(), true);

	}

	@Priority(32)
	@Test(groups ={"REGRESSION","P1","SANITY", "REACH"}, dependsOnMethods = { "validateLoginEvent" })
	public void performEncryptionDecryption() throws Exception {
		Logger.info("Performing encryption, decryption, upload and download");
		printCredentials();
		try{
	
			Logger.info("============policname="+encryptPolicyName);
	
//			policy.deleteEncryptionDecryptionPolicy(encryptPolicyName, suiteData, backend.getHeaders(suiteData));
			policy.createEnableEncryptionDecryptionPolicy(encryptPolicyName, suiteData, backend.getHeaders(suiteData));
	
			fileNameEncrypt=box.createFileForUpload(uploadFile);
			filesToDeleteFromWorkspace.add(new File(filePath+fileNameEncrypt));
			filesToDeleteFromSaasApp.add(fileNameEncrypt+".eef");
			
			box.gotoHomePage(getWebDriver(), homePageUrl);
//			box.refresh(getWebDriver(), 10);
			box.uploadFile(getWebDriver(),filePath+fileNameEncrypt , 15);
			fileNameEncrypt+=".eef";
			box.downloadSelectedItem(getWebDriver(), fileNameEncrypt, 15,suiteData.getBrowser());
		}
		finally{
			policy.deleteEncryptionDecryptionPolicy(encryptPolicyName, suiteData, backend.getHeaders(suiteData));
			box.deleteSelectedItem(getWebDriver(), fileNameEncrypt);
		}
		
	}

	@Priority(33)
	@Test(groups ={"SANITY","REGRESSION","P1", "REACH"}, dependsOnMethods={"performEncryptionDecryption"}, retryAnalyzer=RetryAnalyzer.class)
	public void validateFileUploadEncryptionLog() throws Exception {
		Logger.info("Verifying the Encryption File Upload Event");
		printCredentials();
		
		
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.remove(GatewayTestConstants.RESP_SIZE);
		expectedDataMap.remove(GatewayTestConstants.USER_NAME);
		expectedDataMap.remove(GatewayTestConstants.COUNTRY);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "File Encryption");
		expectedDataMap.put(GatewayTestConstants.OBJECT_NAME, fileNameEncrypt.replace(".eef", ""));
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "File "+fileNameEncrypt.replace(".eef", "")+" encrypted on upload for user "+userName); 
		expectedDataMap.put(GatewayTestConstants.REFERER_URI, "NOT_EMPTY");
		expectedDataMap.put(GatewayTestConstants.CRYPTO_KEY_NAME, "NOT_EMPTY");
		expectedDataMap.put(GatewayTestConstants.CRYPTO_KEY_SERVER, "NOT_EMPTY");
		expectedDataMap.put(GatewayTestConstants.CRYPTO_KEY_VERSION, "NOT_EMPTY");
		expectedDataMap.put(GatewayTestConstants.ACTION_TAKEN, "encrypt");
		assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"File Encryption Log not found or fields does not match.");
		
		Reporter.log("Log verification successful for method:"+Thread.currentThread().getStackTrace()[1].getMethodName(), true);
		
	}
	@Priority(34)
	@Test(groups ={"REGRESSION","P1"}, dependsOnMethods={"performEncryptionDecryption"})
	public void validateEncryptionFileUploadEvent() throws Exception {
		Logger.info("Verifying the Encryption File Upload Event");
		printCredentials();
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Upload");
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File");
		expectedDataMap.put(GatewayTestConstants.FILE_SIZE, "58");
		expectedDataMap.put(GatewayTestConstants.OBJECT_NAME, fileNameEncrypt);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User uploaded file "+fileNameEncrypt+" to folder All Files"); 
		expectedDataMap.put(GatewayTestConstants.REFERER_URI, "NOT_EMPTY");
		assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"File Upload Log not found or fields does not match.");
		
		Reporter.log("Log verification successful for method:"+Thread.currentThread().getStackTrace()[1].getMethodName(), true);
		
	}
	@Priority(35)
	@Test(groups ={"REGRESSION","P1"}, dependsOnMethods={"performEncryptionDecryption"})
	public void validateEncryptionFileUploadPolicyViolation() throws Exception {
		Logger.info("Verifying the Encryption File Upload Event");
		
	
		
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.remove(GatewayTestConstants.RESP_SIZE);
		expectedDataMap.remove(GatewayTestConstants.COUNTRY);
		expectedDataMap.remove(GatewayTestConstants.USER_NAME);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, GatewayTestConstants.SEVERITY_CRITICAL);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Policy Violation");
		expectedDataMap.put(GatewayTestConstants.FILE_SIZE, "58");
		expectedDataMap.put(GatewayTestConstants.ACTION_TAKEN, "encrypt,email");
		expectedDataMap.put(GatewayTestConstants.OBJECT_NAME, fileNameEncrypt);
		expectedDataMap.put(GatewayTestConstants.POLICY_VIOLATED, "");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "[ALERT] "+suiteData.getSaasAppUsername()+" attempted to upload content:"+fileNameEncrypt+" violating policy:"+encryptPolicyName); 
		expectedDataMap.put(GatewayTestConstants.REFERER_URI, "NOT_EMPTY");
		assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"File Upload Encryption Policy Violation log not found or fields does not match.");
		
		Reporter.log("Log verification successful for method:"+Thread.currentThread().getStackTrace()[1].getMethodName(), true);
		
	}

	@Priority(36)
	@Test(groups ={"SANITY","REGRESSION","P1", "REACH"},dependsOnMethods={"performEncryptionDecryption"})
	public void validateDecryptionFileDownloadDecryptionLog() throws Exception {
		Logger.info("Verifying the Decryption File Download Event");
		printCredentials();
		
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.remove(GatewayTestConstants.RESP_SIZE);
		expectedDataMap.remove(GatewayTestConstants.USER_NAME);
		expectedDataMap.remove(GatewayTestConstants.COUNTRY);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "File Decryption");
		expectedDataMap.put(GatewayTestConstants.OBJECT_NAME, fileNameEncrypt);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "File "+fileNameEncrypt+" decrypted on download for user "+userName); 
		expectedDataMap.put(GatewayTestConstants.CRYPTO_KEY_NAME, "NOT_EMPTY");
		expectedDataMap.put(GatewayTestConstants.CRYPTO_KEY_SERVER, "NOT_EMPTY");
		expectedDataMap.put(GatewayTestConstants.CRYPTO_KEY_VERSION, "NOT_EMPTY");
		expectedDataMap.put(GatewayTestConstants.ACTION_TAKEN, "decrypt");
		expectedDataMap.put(GatewayTestConstants.REFERER_URI, "NOT_EMPTY");
		assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"File Decryption Log not found or fields does not match.");
		
		Reporter.log("Log verification successful for method:"+Thread.currentThread().getStackTrace()[1].getMethodName(), true);
		
	}
	@Priority(37)
	@Test(groups ={"REGRESSION","P1"},dependsOnMethods={"performEncryptionDecryption"})
	public void validateDecryptionFileDownloadEvent() throws Exception {
		Logger.info("Verifying the Decryption File Download Event");
		
		
		
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Download");
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File");
		expectedDataMap.put(GatewayTestConstants.FILE_SIZE, "58");
		expectedDataMap.put(GatewayTestConstants.OBJECT_NAME, fileNameEncrypt);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User downloaded "+fileNameEncrypt); 
		expectedDataMap.put(GatewayTestConstants.REFERER_URI, "NOT_EMPTY");
		assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"File Download Log not found or fields does not match.");
		
		Reporter.log("Log verification successful for method:"+Thread.currentThread().getStackTrace()[1].getMethodName(), true);
		
	}

	@Priority(38)
	@Test(groups ={"REGRESSION","P1"},dependsOnMethods={"performEncryptionDecryption"})
	public void validateDecryptionFileDownloadPolicyViolation() throws Exception {
		Logger.info("Verifying the Decryption File Download Event");
		
		
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.remove(GatewayTestConstants.RESP_SIZE);
		expectedDataMap.remove(GatewayTestConstants.COUNTRY);
		expectedDataMap.remove(GatewayTestConstants.USER_NAME);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, GatewayTestConstants.SEVERITY_CRITICAL);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Policy Violation");
		expectedDataMap.put(GatewayTestConstants.FILE_SIZE, "58");
		expectedDataMap.put(GatewayTestConstants.ACTION_TAKEN, "decrypt,email");
		expectedDataMap.put(GatewayTestConstants.OBJECT_NAME, fileNameEncrypt);
		expectedDataMap.put(GatewayTestConstants.POLICY_VIOLATED, "");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "[ALERT] "+suiteData.getSaasAppUsername()+" attempted to download content:"+fileNameEncrypt+" violating policy:"+encryptPolicyName); 
		expectedDataMap.put(GatewayTestConstants.REFERER_URI, "NOT_EMPTY");
		assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"File Download Decryption Policy Violation log not found or fields does not match.");
		
		Reporter.log("Log verification successful for method:"+Thread.currentThread().getStackTrace()[1].getMethodName(), true);
		
	}

	
	@Priority(39)
	@Test(groups ={"REGRESSION","P1"}, dependsOnMethods = { "validateLoginEvent" }, retryAnalyzer=RetryAnalyzer.class)
	public void validateFileDelete() throws Exception {
		Logger.info("Verifying the File Delete Event");
		
		box.gotoHomePage(getWebDriver(),homePageUrl);
		box.uploadFileIfNotPresent(getWebDriver(),filePath+fileNameFileOper , 15);
		box.deleteSelectedItem(getWebDriver(), fileNameFileOper);
		
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Delete");
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File/Folder");
		expectedDataMap.put(GatewayTestConstants.OBJECT_NAME, fileNameFileOper);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User deleted object(s) "+fileNameFileOper); 
		expectedDataMap.put(GatewayTestConstants.REFERER_URI, "");
		
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("Log verification successful for method:"+Thread.currentThread().getStackTrace()[1].getMethodName(), true);
	}


	
	@Priority(41)
	@Test(groups ={"REGRESSION","P1"}, dependsOnMethods = { "validateLoginEvent"})
	public void validateFileCreateBoxNote() throws Exception {
		Logger.info("Verifying the File Create Event For File BoxNote");
		Logger.info("ADB_ID:417");
		box.gotoHomePage(getWebDriver(),homePageUrl);
		fileNameBoxFile=box.getTimestamp();
		box.createDoc(getWebDriver(), "boxnote", 	fileNameBoxFile+"_boxnote", "My Description");
		
		String myfileName=fileNameBoxFile+"_boxnote.boxnote";
		filesToDeleteFromSaasApp.add(myfileName);
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Create");
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File");
		expectedDataMap.put(GatewayTestConstants.OBJECT_NAME, myfileName);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User created new boxnote "+myfileName+" in All Files"); 
		expectedDataMap.put(GatewayTestConstants.REFERER_URI, "NOT_EMPTY");
		//expectedDataMap.put(GatewayTestConstants.STATUS_CODE, 200);
		
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("Log verification successful for method:"+Thread.currentThread().getStackTrace()[1].getMethodName(), true);
	}
	
	@Priority(42)
	@Test(groups ={"REGRESSION","P1"}, dependsOnMethods = { "validateLoginEvent"})
	public void validateFileCreateWord() throws Exception {
		Logger.info("Verifying the File Create Event For File Word");
		Logger.info("ADB_ID:3563");
		
		box.gotoHomePage(getWebDriver(),homePageUrl);
		fileNameBoxFile=box.getTimestamp();
		box.createDoc(getWebDriver(), "word", 		fileNameBoxFile+"_word", "My Description");
		
		String myfileName=fileNameBoxFile+"_word.docx";
		filesToDeleteFromSaasApp.add(myfileName);
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Create");
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File");
		expectedDataMap.put(GatewayTestConstants.OBJECT_NAME, myfileName);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User created new document "+myfileName+" in folder All Files"); 
		expectedDataMap.put(GatewayTestConstants.REFERER_URI, "NOT_EMPTY");
		//expectedDataMap.put(GatewayTestConstants.STATUS_CODE, 200);
		
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("Log verification successful for method:"+Thread.currentThread().getStackTrace()[1].getMethodName(), true);
	}
	@Priority(43)
	@Test(groups ={"REGRESSION","P1"}, dependsOnMethods = { "validateLoginEvent"})
	public void validateFileCreatePowerPoint() throws Exception {
		Logger.info("Verifying the File Create Event For File PowerPoint");
		Logger.info("ADB_ID:3563");
		
		box.gotoHomePage(getWebDriver(),homePageUrl);
		fileNameBoxFile=box.getTimestamp();
		box.createDoc(getWebDriver(), "powerpoint", fileNameBoxFile+"_powerpoint", "My Description");
//		box.lockItem(getWebDriver(), fileNameBoxFile+"_powerpoint", 10);
//		box.unlockItem(getWebDriver(), fileNameBoxFile+"_powerpoint", 10);
		
		String myfileName=fileNameBoxFile+"_powerpoint.pptx";
		filesToDeleteFromSaasApp.add(myfileName);
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Create");
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File");
		expectedDataMap.put(GatewayTestConstants.OBJECT_NAME, myfileName);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User created new document "+myfileName+" in folder All Files"); 
		expectedDataMap.put(GatewayTestConstants.REFERER_URI, "NOT_EMPTY");
		
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("Log verification successful for method:"+Thread.currentThread().getStackTrace()[1].getMethodName(), true);
	}
	@Priority(44)
	@Test(groups ={"REGRESSION","P1"}, dependsOnMethods = { "validateLoginEvent"})
	public void validateFileCreateExcel() throws Exception {
		Logger.info("Verifying the File Create Event For File Excel");
		Logger.info("ADB_ID:3563");
		
		box.gotoHomePage(getWebDriver(),homePageUrl);
		fileNameBoxFile=box.getTimestamp();
		box.createDoc(getWebDriver(), "excel",		fileNameBoxFile+"_excel", "My Description");
		
		String myfileName=fileNameBoxFile+"_excel.xlsx";
		filesToDeleteFromSaasApp.add(myfileName);
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Create");
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File");
		expectedDataMap.put(GatewayTestConstants.OBJECT_NAME, myfileName);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User created new document "+myfileName+" in folder All Files"); 
		expectedDataMap.put(GatewayTestConstants.REFERER_URI, "NOT_EMPTY");
		
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("Log verification successful for method:"+Thread.currentThread().getStackTrace()[1].getMethodName(), true);
	}
	@Priority(45)
	@Test(groups ={"REGRESSION","P1"}, dependsOnMethods = { "validateLoginEvent"})
	public void validateFileCreateGoogleDoc() throws Exception {
		Logger.info("Verifying the File Create Event For File Google Doc");
		Logger.info("ADB_ID:359");
		
		box.gotoHomePage(getWebDriver(),homePageUrl);
		fileNameBoxFile=box.getTimestamp();
		box.createDoc(getWebDriver(), "googledoc",  fileNameBoxFile+"_googledoc", "My Description");
		
		String myfileName=fileNameBoxFile+"_googledoc.gdoc";
		filesToDeleteFromSaasApp.add(myfileName);
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Create");
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File");
		expectedDataMap.put(GatewayTestConstants.OBJECT_NAME, myfileName);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User created new google doc "+myfileName+" in folder All Files"); 
		expectedDataMap.put(GatewayTestConstants.REFERER_URI, "NOT_EMPTY");
		
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("Log verification successful for method:"+Thread.currentThread().getStackTrace()[1].getMethodName(), true);
	}
	@Priority(46)
	@Test(groups ={"REGRESSION","P1"}, dependsOnMethods = { "validateLoginEvent"})
	public void validateFileCreateGoogleSheet() throws Exception {
		Logger.info("Verifying the File Create Event For File Google Sheet");
		Logger.info("ADB_ID:360");
		
		box.gotoHomePage(getWebDriver(),homePageUrl);
		fileNameBoxFile=box.getTimestamp();
		box.createDoc(getWebDriver(), "googlesheet",fileNameBoxFile+"_googlesheet", "My Description");
		
		String myfileName=fileNameBoxFile+"_googlesheet.gsheet";
		filesToDeleteFromSaasApp.add(myfileName);
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Create");
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File");
		expectedDataMap.put(GatewayTestConstants.OBJECT_NAME, myfileName);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User created new google spreadsheet "+myfileName+" in folder All Files"); 
		expectedDataMap.put(GatewayTestConstants.REFERER_URI, "NOT_EMPTY");
		//expectedDataMap.put(GatewayTestConstants.STATUS_CODE, 200);
		
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("Log verification successful for method:"+Thread.currentThread().getStackTrace()[1].getMethodName(), true);
	}
	@Priority(47)
	@Test(groups ={"REGRESSION","P2"}, dependsOnMethods = { "validateLoginEvent"})
	public void validateBookmarkCreate() throws Exception {
		Logger.info("Verifying the Bookmark Create Event  For Bookmark");
		Logger.info("ADB_ID:357");
		
		box.gotoHomePage(getWebDriver(),homePageUrl);
		fileNameBoxFile=box.getTimestamp();
		box.createBookmark(getWebDriver(), bookmarkUrl,fileNameBoxFile+"_bookmark", "My Description");
		
		String myfileName=fileNameBoxFile+"_bookmark";
		filesToDeleteFromSaasApp.add(myfileName);
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Create");
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Bookmark");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User created new web link "+bookmarkUrl+" with name "+myfileName+" in folder All Files"); 
		expectedDataMap.put(GatewayTestConstants.REFERER_URI, "NOT_EMPTY");
		
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("Log verification successful for method:"+Thread.currentThread().getStackTrace()[1].getMethodName(), true);
	}
	@Priority(48)
	@Test(groups ={"REGRESSION","P1"}, dependsOnMethods = {"validateLoginEvent"}, retryAnalyzer=RetryAnalyzer.class)
	public void validateFileUpload_6mb() throws Exception {
		Logger.info("Verifying the File Upload Event");
		
		fileName_6mb=box.createFileForUpload(uploadFile_6mb);
		filesToDeleteFromSaasApp.add(fileName_6mb);
		filesToDeleteFromWorkspace.add(new File(filePath+fileName_6mb));
		
		box.gotoHomePage(getWebDriver(),homePageUrl);
		box.uploadFile(getWebDriver(),filePath+fileName_6mb , 90);
		
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Upload");
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File");
		expectedDataMap.put(GatewayTestConstants.OBJECT_NAME, fileName_6mb);
		expectedDataMap.put(GatewayTestConstants.FILE_SIZE, "6220");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User uploaded file "+fileName_6mb+" to folder All Files"); 
		expectedDataMap.put(GatewayTestConstants.REFERER_URI, "");
		
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("Log verification successful for method:"+Thread.currentThread().getStackTrace()[1].getMethodName(), true);
		
	}
	@Priority(49)
	@Test(groups ={"REGRESSION","P1"}, dependsOnMethods = {"validateLoginEvent"}, retryAnalyzer=RetryAnalyzer.class)
	public void validateFileUpload_10mb() throws Exception {
		Logger.info("Verifying the File Upload Event");
		
		fileName_10mb=box.createFileForUpload(uploadFile_10mb);
		filesToDeleteFromSaasApp.add(fileName_10mb);
		filesToDeleteFromWorkspace.add(new File(filePath+fileName_10mb));
		
		box.gotoHomePage(getWebDriver(),homePageUrl);
		box.uploadFile(getWebDriver(),filePath+fileName_10mb , 120);
		
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Upload");
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File");
		expectedDataMap.put(GatewayTestConstants.OBJECT_NAME, fileName_10mb);
		expectedDataMap.put(GatewayTestConstants.FILE_SIZE, "10385");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User uploaded file "+fileName_10mb+" to folder All Files"); 
		expectedDataMap.put(GatewayTestConstants.REFERER_URI, "");
		
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("Log verification successful for method:"+Thread.currentThread().getStackTrace()[1].getMethodName(), true);
		
	}
	@Priority(50)
	@Test(groups ={"REGRESSION","P1"}, dependsOnMethods = {"validateLoginEvent"}, retryAnalyzer=RetryAnalyzer.class)
	public void validateFileUpload_20mb() throws Exception {
		Logger.info("Verifying the File Upload Event");
		
		fileName_20mb=box.createFileForUpload(uploadFile_20mb);
		filesToDeleteFromSaasApp.add(fileName_20mb);
		filesToDeleteFromWorkspace.add(new File(filePath+fileName_20mb));
		
		box.gotoHomePage(getWebDriver(),homePageUrl);
		box.uploadFile(getWebDriver(),filePath+fileName_20mb , 180);
		
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Upload");
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File");
		expectedDataMap.put(GatewayTestConstants.OBJECT_NAME, fileName_20mb);
		expectedDataMap.put(GatewayTestConstants.FILE_SIZE, "19893");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User uploaded file "+fileName_20mb+" to folder All Files"); 
		expectedDataMap.put(GatewayTestConstants.REFERER_URI, "");
		
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("Log verification successful for method:"+Thread.currentThread().getStackTrace()[1].getMethodName(), true);
		
	}
	
	@Priority(51)
	@Test(groups ={"REGRESSION","P1"}, dependsOnMethods = {"validateLoginEvent"})
	public void performFileUploadPolicy() throws Exception {
		Logger.info("Verifying the File Upload Event");
//		try{
			policy.createAndEnableFileTransferPolicyUpload(fileuploadPolicyName, suiteData, backend.getHeaders(suiteData));
			box.gotoHomePage(getWebDriver(),homePageUrl);
			box.uploadFile(getWebDriver(),filePath+fileNameFileOper , 15);
			
			try {
				boolean value = clickOkInPopup();
				Assert.assertTrue(value, "Blocker Popup Not Found");
				if(suiteData.getBrowser().equals("ie")){
					value = clickOkInPopup();
					Assert.assertTrue(value, "Blocker Popup Not Found");
				}
			} catch (Exception e) {
				Logger.info("Error " + e.getMessage());
			}
			
			
//		}
//		finally{
//			policy.deletePolicy(fileuploadPolicyName, suiteData, backend.getHeaders(suiteData));
//		}
		
	}
	@Priority(52)
	@Test(groups ={"REGRESSION","P1"}, dependsOnMethods = {"validateLoginEvent"})
	public void deleteFileUploadPolicy() throws Exception {
		Logger.info("Deleting the file upload policy created in the test:performFileUploadPolicy");
			policy.deletePolicy(fileuploadPolicyName, suiteData, backend.getHeaders(suiteData));
		
	}
	@Priority(53)
	@Test(groups ={"REGRESSION","P1"}, dependsOnMethods = {"validateLoginEvent","performFileUploadPolicy"}, retryAnalyzer=RetryAnalyzer.class)
	public void validateFileUploadPolicy() throws Exception {
		Logger.info("Verifying the File Upload Event");
		try{
		
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.remove(GatewayTestConstants.RESP_SIZE);
		expectedDataMap.remove(GatewayTestConstants.COUNTRY);
		expectedDataMap.remove(GatewayTestConstants.USER_NAME);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, GatewayTestConstants.SEVERITY_CRITICAL);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Policy Violation");
		expectedDataMap.put(GatewayTestConstants.POLICY_ACTION, "ALERT");
		expectedDataMap.put(GatewayTestConstants.FILE_SIZE, "57");
		expectedDataMap.put(GatewayTestConstants.ACTION_TAKEN, "block,email");
		expectedDataMap.put(GatewayTestConstants.OBJECT_NAME, uploadFile);
		expectedDataMap.put(GatewayTestConstants.POLICY_VIOLATED, fileuploadPolicyName);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "[ALERT] "+suiteData.getSaasAppUsername()+" attempted to upload content:"+fileNameFileOper+" violating policy:"+fileuploadPolicyName); 
		expectedDataMap.put(GatewayTestConstants.REFERER_URI, "NOT_EMPTY");
		assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"File Upload Encryption Policy Violation log not found or fields does not match.");
		
		
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("Log verification successful for method:"+Thread.currentThread().getStackTrace()[1].getMethodName(), true);
		
		}
		finally{
			policy.deletePolicy(fileuploadPolicyName, suiteData, backend.getHeaders(suiteData));
		}
		
	}
	
	@Priority(54)
	@Test(groups ={"ACCESS_ENFC_POLICY"}) 
	public void createAccessEnforcementPolicy() throws Exception {
		fromTime=backend.getCurrentTime();
		String upperCaseTestUser = suiteData.getTestUsername();
		suiteData.setTestUsername(upperCaseTestUser.toLowerCase());
		protectAction.createEnableAccessEnforcementPolicy(client, suiteData, accessEnforcementPolicyName + getTestUserName(), "Session:Login", "high");
		
//		box.login(getWebDriver(), suiteData);
//		try {
//			boolean value = clickOkInPopup();
//			Assert.assertTrue(value, "Blocker Popup Not Found");
//		} catch (Exception e) {
//			Logger.info("Error " + e.getMessage());
//		}
		
		
	}
	
	@Priority(55)
	@Test(groups ={"ACCESS_ENFC_POLICY"}) 
	public void verifyAccessEnforcementPolicy() throws Exception {
		
		String upperCase = suiteData.getSaasAppUsername();
		
		suiteData.setSaasAppUsername(upperCase.toLowerCase());
		
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Policy Violation");
		expectedDataMap.put(GatewayTestConstants.ACTION_TAKEN, "block,");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "[ALERT] " + suiteData.getTestUsername() + 
				" attempted Activity: Login on Object type: "); 
		expectedDataMap.put(GatewayTestConstants.POLICY_ACTION, "ALERT");
		expectedDataMap.put(GatewayTestConstants.POLICY_TYPE, "AccessEnforcement");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "high");
		expectedDataMap.remove(GatewayTestConstants.COUNTRY);
		expectedDataMap.remove(GatewayTestConstants.USER_NAME);
		expectedDataMap.put(GatewayTestConstants.USER, upperCase.toLowerCase());
		
		
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),
				"Expected log: "+expectedDataMap.get(GatewayTestConstants.MESSAGE)+" does not exist on investigate");
		
		
	}
	
	@Priority(56)
	@Test(groups ={"ACCESS_ENFC_POLICY"})  
	public void deleteAccessEnforcementPolicy() throws Exception {
		Logger.info("==================================================================================");
		Logger.info(" Verifying  Relogin event to Saas App "+suiteData.getSaasAppName());
		Logger.info("==================================================================================");
		protectAction.deletePolicy(client, suiteData, accessEnforcementPolicyName + getTestUserName());
	}
	
	@Priority(57)
	@Test(groups ={"FILE_SHARE_POLICY"}) 
	public void createFileSharingPolicy() throws Exception {
		fromTime=backend.getCurrentTime();
		String upperCaseTestUser = suiteData.getTestUsername();
		suiteData.setTestUsername(upperCaseTestUser.toLowerCase());
		protectAction.deletePolicy(client, suiteData, fileSharingPolicyName + getTestUserName());
		Logger.info("Creating File Shareing Policy, User " +suiteData.getTestUsername() + " Receipt " +  suiteData.getTestUsername());
		protectAction.createEnableFileSharingPolicyByShare(client, suiteData, fileSharingPolicyName + getTestUserName(),  suiteData.getSaasAppUsername(), "high");
		
		
		
	}
	
	@Priority(58)
	@Test(groups ={"FILE_SHARE_POLICY"}) 
	public void verifyFileSharingPolicy() throws Exception {
		
		
		box.uploadFileIfNotPresent(getWebDriver(),filePath+fileNameFileOper , 15);
		box.shareItem(getWebDriver(), fileNameFileOper,"people_with_link", shareWithUser, 10);
		
//		try {
//			boolean value = clickOkInPopup();
//			Assert.assertTrue(value, "Blocker Popup Not Found");
//		} catch (Exception e) {
//			Logger.info("Error " + e.getMessage());
//		}
		
		String upperCase = suiteData.getSaasAppUsername();
		
		suiteData.setSaasAppUsername(upperCase.toLowerCase());
		
		expectedDataMap.clear();
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Policy Violation");
		expectedDataMap.put(GatewayTestConstants.ACTION_TAKEN, "");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "[ALERT] " + suiteData.getTestUsername() + 
				" attempted to share content:newfolderwith external user(s):"+ suiteData.getUsername() 
				+ " violating policy:" + fileSharingPolicyName + getTestUserName()); 
		expectedDataMap.put(GatewayTestConstants.POLICY_ACTION, "ALERT");
		expectedDataMap.put(GatewayTestConstants.POLICY_TYPE, "FileSharingGateway");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "high");
		expectedDataMap.remove(GatewayTestConstants.COUNTRY);
		expectedDataMap.remove(GatewayTestConstants.USER_NAME);
		
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),
				"Expected log: "+expectedDataMap.get(GatewayTestConstants.MESSAGE)+" does not exist on investigate");
		
		
	}
	
	@Priority(59)
	@Test(groups ={"FILE_SHARE_POLICY"})  
	public void deleteFileSharingPolicy() throws Exception {
		Logger.info("==================================================================================");
		Logger.info(" Verifying  Relogin event to Saas App "+suiteData.getSaasAppName());
		Logger.info("==================================================================================");
		protectData.setPolicyName(fileSharingPolicyName + getTestUserName());
		protectAction.deletePolicy(client, suiteData, fileSharingPolicyName + getTestUserName());
	}
	
	
	@Priority(60)
	@Test(groups ={"CUSTOMER_SEP"}, dependsOnMethods = { "validateLoginEvent" }, retryAnalyzer=RetryAnalyzer.class)
	public void customerSeparationCreateFolder() throws Exception {
		Logger.info("Verifying the Folder Create Event");
		printCredentials();
		box.gotoHomePage(getWebDriver(),homePageUrl);
		box.createFolder(getWebDriver(),customerSeparationLogFolderName);
		box.openItem(getWebDriver(),customerSeparationLogFolderName,15);
//		box.uploadFile(getWebDriver(), filePath+fileNameFolderOper, 15);
		box.gotoHomePage(getWebDriver(),homePageUrl);
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Create");
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Folder");
		expectedDataMap.put(GatewayTestConstants.OBJECT_NAME, customerSeparationLogFolderName);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User created new folder "+customerSeparationLogFolderName); 
		expectedDataMap.put(GatewayTestConstants.REFERER_URI, "");
//		expectedDataMap.put(GatewayTestConstants.RESP_CODE, "200");
		
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("Log verification successful for method:"+Thread.currentThread().getStackTrace()[1].getMethodName(), true);
		
	}
	@Priority(61)
	@Test(groups ={"CUSTOMER_SEP"}, retryAnalyzer=RetryAnalyzer.class)
	public void customerSeparationVerifyLog() throws Exception {
		Logger.info("Verifying the Folder Create Event");
		printCredentials();
		expectedDataMap.clear();
		String expMsg ="User created new folder "+customerSeparationLogFolderName;
		
		fromTime =new org.joda.time.DateTime( org.joda.time.DateTimeZone.UTC ).minusMinutes(15).toString();
		String resp = backend.fetchElasticSearchLogsUniversal(client,suiteData, fromTime);
		
		if(!expMsg.isEmpty())
			Assert.assertFalse(resp.contains(expMsg),"Other tenant's log is found in this tenant.");
		else
			Reporter.log("No logs found");
		Reporter.log("Log verification successful for method:"+Thread.currentThread().getStackTrace()[1].getMethodName(), true);
		
	}
	
	@Priority(62)
	@Test(groups ={"REGRESSION","P1","SANITY", "REACH"}, dependsOnMethods ={"validateLoginEvent"})
	public void validateLogoutEvent() throws Exception {
		Logger.info("Verifying the Logout Event");
		printCredentials();
		box.clickLogout(getWebDriver());
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Logout");
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Session");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User logged out"); 
		expectedDataMap.put(GatewayTestConstants.REFERER_URI, "");
//		expectedDataMap.put(GatewayTestConstants.STATUS_CODE, "204");
//		expectedDataMap.put(GatewayTestConstants.RESP_CODE, "302");
		
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("Log verification successful for method:"+Thread.currentThread().getStackTrace()[1].getMethodName(), true);
	}
	
	
	
	@Priority(63)
	@Test(groups ={"REGRESSION","P1"}, dependsOnMethods = { "validateLogoutEvent" })
	public void validateInvalidLoginEvent() throws Exception {
		Logger.info("Verifying the InvalidLogin Event");
		box.invalidLogin(getWebDriver(), suiteData);
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "InvalidLogin");
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Session");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User Login Failed!"); 
		expectedDataMap.put(GatewayTestConstants.REFERER_URI, "");
		expectedDataMap.put(GatewayTestConstants.REQ_URI, "NOT_EMPTY");
//		expectedDataMap.put(GatewayTestConstants.STATUS_CODE, "204");
		
		
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("Log verification successful for method:"+Thread.currentThread().getStackTrace()[1].getMethodName(), true);
		
	}
	
	public String getTestUserName(){
		return suiteData.getTestUsername().replaceAll("@", "_");
	}
	
	
	@Test(groups ={"REGRESSION3"})
	public void temp() throws Exception {
		
		encryptPolicyName= GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME+suiteData.getSaasAppUsername().replaceAll("@", "_");
		Reporter.log("============policname="+encryptPolicyName,true);
		policy.deleteEncryptionDecryptionPolicy(encryptPolicyName, suiteData, backend.getHeaders(suiteData));
		policy.createEnableEncryptionDecryptionPolicy(encryptPolicyName, suiteData, backend.getHeaders(suiteData));
		
//		fileNameEncrypt=box.createFileForUpload(uploadFile);
//		filesToDeleteFromWorkspace.add(new File(filePath+fileNameEncrypt));
//		filesToDeleteFromSaasApp.add(fileNameEncrypt+".eef");
//		
//		box.gotoHomePage(getWebDriver(), suiteData);
////		box.refresh(getWebDriver(), 10);
//		box.uploadFile(getWebDriver(),filePath+fileNameEncrypt , 15);
//		fileNameEncrypt+=".eef";
//		box.downloadSelectedItem(getWebDriver(), fileNameEncrypt, 15);
				
		
	}
	

	
	/*@Priority(33)
	@Test(groups ={"REGRESSION","P"}, dependsOnMethods = { "temp" })
	public void validateFilePostComment() throws Exception {
		Logger.info("ADB_ID:3774");
		Logger.info("Verifying the File Post Comment");
		String myfileName=fileNameBoxFile+".pptx";
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Post Comment");
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File");
		expectedDataMap.put(GatewayTestConstants.OBJECT_NAME, myfileName);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User posted new comment "+commentOnFile +" on "+myfileName); 
		expectedDataMap.put(GatewayTestConstants.REFERER_URI, "NOT_EMPTY");
		//expectedDataMap.put(GatewayTestConstants.STATUS_CODE, 200);
		
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("Log verification successful for method:"+Thread.currentThread().getStackTrace()[1].getMethodName(), true);
	}
	
	@Priority(34)
	@Test(groups ={"REGRESSION","P"}, dependsOnMethods = { "temp" })
	public void validateFileDeleteComment() throws Exception {
		Logger.info("ADB_ID:2626");
		Logger.info("Verifying the File Delete Comment");
		String myfileName=fileNameBoxFile+".pptx";
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Delete Comment");
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File");
		expectedDataMap.put(GatewayTestConstants.OBJECT_NAME, myfileName);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User delete a comment on "+myfileName); 
		expectedDataMap.put(GatewayTestConstants.REFERER_URI, "NOT_EMPTY");
		//expectedDataMap.put(GatewayTestConstants.STATUS_CODE, 200);
		
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("Log verification successful for method:"+Thread.currentThread().getStackTrace()[1].getMethodName(), true);
	}
	*/
	
	
}
	