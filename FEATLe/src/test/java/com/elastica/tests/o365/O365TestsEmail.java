package com.elastica.tests.o365;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.elastica.common.GWCommonTest;
import com.elastica.gateway.GatewayTestConstants;
import com.elastica.listeners.Priority;
import com.elastica.listeners.RetryAnalyzer;
import com.elastica.logger.Logger;

public class O365TestsEmail extends GWCommonTest{
	Map <String, Object> expectedDataMap = new HashMap<String, Object>();
	Map<String, String>policyDataMap= new HashMap<String, String>(); 
	String fromTime=backend.getCurrentTime();
	
	@BeforeMethod()
	public void clearDataMap(){
		fromTime=backend.getCurrentTime();
		expectedDataMap.clear();
	}

	@Priority(1)
	@Test(groups ={"Regression6"})
	public void loginToCloudSocAppAndSetupSSO() throws Exception {
		fromTime=backend.getCurrentTime();
		printCredentials();
		Logger.info("Started performing activities on saas app");
		login.loginCloudSocPortal(getWebDriver(), suiteData);
		Logger.info("Finished login activities on cloudSoc");
	}
	
	@Priority(2)
	@Test(groups ={"Regression6", "REACH"}, retryAnalyzer=RetryAnalyzer.class)  //,dependsOnMethods = { "loginToCloudSocAppAndSetupSSO" }
	public void o365_Test_01_ValidateLoginActivityEvent() throws Exception {
		Logger.info("Verifying the login event");
		o365Login.login(getWebDriver(), suiteData);
		printCredentials();
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Login");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User logged in"); 
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Session");
		expectedDataMap.put(GatewayTestConstants.REQ_URI, "https://login.microsoftonline.com/common/login");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("Login Event Successfull");
	}
	
	@Priority(3)
	@Test(groups ={"Regression6", "REACH"}, retryAnalyzer=RetryAnalyzer.class) //dependsOnMethods = { "o365_Test_01_ValidateLogingActivityEvent" }
	public void o365_Test_02_ValidateSendEmailActivityEvent() throws Exception {
		Reporter.log("==================================================================================",true);
		Reporter.log(" Verifying email send event in Outlook", true);
		Reporter.log("==================================================================================",true);
		printCredentials();
		fromTime=backend.getCurrentTime();
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.loadEmailApp(getWebDriver());
		o365HomeAction.sendEmail(getWebDriver());
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User sent an email to admin@gatewayO365beatle.com");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Send");
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Email");
		expectedDataMap.put(GatewayTestConstants.REFERER_URI, "");
		expectedDataMap.put(GatewayTestConstants.SHARE_WITH, "admin@gatewayO365beatle.com");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Expected log: "+expectedDataMap.get(GatewayTestConstants.MESSAGE)+" does not exist");
		Reporter.log("==================================================================================",true);
		Reporter.log(" Send Email event verification successful", true);
		Reporter.log("==================================================================================",true);
	}
	
	@Priority(4)
	@Test(groups ={"Regression6", "REACH"}) //,dependsOnMethods = { "o365_Test_01_ValidateLogingActivityEvent" }
	public void o365_Test_02_ValidateDraftEmailActivityEvent() throws Exception {
		Logger.info("Verifying email save draft event");
		printCredentials();
		fromTime=backend.getCurrentTime();
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.loadEmailApp(getWebDriver());
		o365HomeAction.draftEmail(getWebDriver());
		
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User saved an email as draft with subject This is subject");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Save");
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Email");
		expectedDataMap.put(GatewayTestConstants.RESP_CODE, "200");
		expectedDataMap.put(GatewayTestConstants.REFERER_URI, "");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		Logger.info("Draft Email event verification successfull");
	}
	
	
	@Priority(5)
	@Test(groups ={"Regression6", "REACH"})  //,dependsOnMethods = { "loginToCloudSocAppAndSetupSSO" }
	public void o365_Test_Discard() throws Exception {
		Logger.info("Discard Email");
		printCredentials();
		fromTime=backend.getCurrentTime();
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.loadEmailApp(getWebDriver());
		o365HomeAction.discardEmail(getWebDriver());
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Discard");
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Email");
		expectedDataMap.put(GatewayTestConstants.REFERER_URI, "");
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User discarded an email with subject ");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("Discard Email Successful");
	}
	
	@Priority(6)
	@Test(groups ={"Regression6", "REACH"})  //,dependsOnMethods = { "loginToCloudSocAppAndSetupSSO" }
	public void o365_Test_EmailAttachedWithFile() throws Exception {
		Logger.info("Email Attached via One Drive");
		printCredentials();
		fromTime=backend.getCurrentTime();
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.loadEmailApp(getWebDriver());
		o365HomeAction.sendEmailWithAttach(getWebDriver(), GatewayTestConstants.GDRIVE_ONE_DRIVE_FILE_ATTACH);
		
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Upload");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User attached file "+ GatewayTestConstants.GDRIVE_ONE_DRIVE_FILE_ATTACH +" to an email");
		expectedDataMap.put(GatewayTestConstants.OBJECT_NAME, GatewayTestConstants.GDRIVE_ONE_DRIVE_FILE_ATTACH);
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File");
		expectedDataMap.put(GatewayTestConstants.REFERER_URI, "");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("Email Attached via One Drive Successful");
	}
	
	@Priority(7)
	@Test(groups ={"Regression6", "REACH"})  
	public void o365_Test_EmailAndReceiveMail() throws Exception {
		Logger.info("Email Recieve Email");
		printCredentials();
		String subject = "This is subject for me";
		fromTime=backend.getCurrentTime();
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.loadEmailApp(getWebDriver());
		o365HomeAction.sendEmailWithToEmail(getWebDriver(), suiteData.getSaasAppUsername(), subject);
		
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Receive");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User received an email with subject "+ subject + " from " + suiteData.getSaasAppUsername() );
		expectedDataMap.put(GatewayTestConstants.OBJECT_NAME, subject);
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Email");
		expectedDataMap.put(GatewayTestConstants.REFERER_URI, "");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("Email Recieve Email Successful");
		
	}
	
//	@Priority(7)  
//	@Test(groups ={"Regression6", "REACH"})   // Not suppported for o365 email for now
	public void o365_Test_EmailDecrypted_FileDownload() throws Exception {
		String decryptedFile = "download.pdf.eef";
		Logger.info("Email Download and Decrypt File");
		printCredentials();
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getGWHeaders(suiteData));
		policy.createEnableEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getGWHeaders(suiteData));
		fromTime=backend.getCurrentTime();
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.loadEmailApp(getWebDriver());
		o365HomeAction.searchBySubjectAndDownload(getWebDriver(), "decrypted file", decryptedFile);
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "File Decryption" );
		expectedDataMap.put(GatewayTestConstants.ACTION_TAKEN, "decrypt");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "File " + decryptedFile + " decrypted on download for user "+suiteData.getSaasAppUsername()+".");
		expectedDataMap.put(GatewayTestConstants.POLICY_TYPE, "FileTransfer");
		expectedDataMap.put(GatewayTestConstants.OBJECT_NAME, GatewayTestConstants.O365_DOWNLOAD_ENCRYPTED_FILE );
		expectedDataMap.remove(GatewayTestConstants.COUNTRY);
		expectedDataMap.remove(GatewayTestConstants.USER_NAME);
		expectedDataMap.put(GatewayTestConstants.REFERER_URI, "");
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getGWHeaders(suiteData));
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Expected log: "+expectedDataMap.get(GatewayTestConstants.MESSAGE)+" does not exist");
		Logger.info("Email Download and Decrypt File Successful");
	}
	
	@Priority(8)
	@Test(groups ={"Regression6", "REACH"})  
	public void o365_Test_Email_FileDownload_Normal() throws Exception {
		Logger.info("Email Download File");
		printCredentials();
		o365HomeAction.loadEmailApp(getWebDriver());
		o365HomeAction.searchBySubjectAndDownloadFile(getWebDriver(), "Normal download email", GatewayTestConstants.O365_FILE);
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Download");
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User downloaded attached file " + GatewayTestConstants.O365_FILE);
		expectedDataMap.put(GatewayTestConstants.FILE_SIZE, "409");
		expectedDataMap.put(GatewayTestConstants.OBJECT_NAME, "Dial-FAQ.pdf");
		expectedDataMap.put(GatewayTestConstants.CONTENT_INLINE, "1");
		expectedDataMap.put(GatewayTestConstants.REFERER_URI, "");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Expected log: "+expectedDataMap.get(GatewayTestConstants.MESSAGE)+" does not exist");
		Reporter.log("==================================================================================",true);
		Reporter.log("Download activity event verification successful", true);
		Reporter.log("==================================================================================",true);
	}
	
	@Priority(9)
	@Test(groups ={"Regression6", "REACH"})  
	public void o365_Test_Email_FileDownload_Normal_ViaOneDrive() throws Exception {
		Logger.info("Email Download File Via One Drive");
		printCredentials();
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getGWHeaders(suiteData));
		policy.createEnableEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getGWHeaders(suiteData));
		fromTime=backend.getCurrentTime();
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.loadEmailApp(getWebDriver());
		o365HomeAction.searchBySubjectAndDownloadFile(getWebDriver(), "This is subject with attached", GatewayTestConstants.O365_FILE);
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Download");
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User downloaded attached Onedrive file "+ GatewayTestConstants.O365_FILE + " from an email");
		expectedDataMap.put(GatewayTestConstants.FILE_SIZE, "409");
		expectedDataMap.put(GatewayTestConstants.OBJECT_NAME, "Dial-FAQ.pdf");
		expectedDataMap.put(GatewayTestConstants.REFERER_URI, "");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Expected log: "+expectedDataMap.get(GatewayTestConstants.MESSAGE)+" does not exist");
		Reporter.log("==================================================================================",true);
		Reporter.log("Download activity event verification successful", true);
		Reporter.log("==================================================================================",true);
	}
	
	@Priority(10)
//	@Test(groups ={"Regression6"}) // This is not supported for now
	public void o365_Test_EmailAttached_Via_System_WithFileEncryption() throws Exception {
		Logger.info("Email Attached via One Drive");
		printCredentials();
		fromTime=backend.getCurrentTime();
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.loadEmailApp(getWebDriver());
		
		o365HomeAction.sendEmailWithAttach(getWebDriver(), GatewayTestConstants.GDRIVE_ONE_DRIVE_FILE_ATTACH);
		o365HomeAction.uploadItemFile(getWebDriver(),  System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+
				"resources"+File.separator+"meta"+File.separator + GatewayTestConstants.O365_UPLOAD_ENCRYPT_FILE);
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Upload");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User attached file "+ GatewayTestConstants.GDRIVE_ONE_DRIVE_FILE_ATTACH +" to an email");
		expectedDataMap.put(GatewayTestConstants.OBJECT_NAME, GatewayTestConstants.GDRIVE_ONE_DRIVE_FILE_ATTACH);
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File");
		expectedDataMap.put(GatewayTestConstants.REFERER_URI, "");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("Email Attached via One Drive Successful");
	}
	
	@Priority(11)
	@Test(groups ={"Regression6", "REACH"}) 
	public void o365_Test_Create_Folder() throws Exception {
		fromTime=backend.getCurrentTime();
		printCredentials();
		Logger.info("Delete Folder If Exist");
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.loadEmailApp(getWebDriver());
		o365HomeAction.emailDeleteFolder(getWebDriver(), GatewayTestConstants.O365_EMAIL_NEW_FOLDER);
		Logger.info("Create Folder");
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.loadEmailApp(getWebDriver());
		o365HomeAction.emailCreateFolder(getWebDriver(), GatewayTestConstants.O365_EMAIL_NEW_FOLDER);
		
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.loadEmailApp(getWebDriver());
		o365HomeAction.removeFromFavorites(getWebDriver(), GatewayTestConstants.O365_EMAIL_NEW_FOLDER);
		
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.loadEmailApp(getWebDriver());
		o365HomeAction.emailRenameFolder(getWebDriver(), GatewayTestConstants.O365_EMAIL_NEW_FOLDER);
		
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.loadEmailApp(getWebDriver());
		o365HomeAction.emptyFolder(getWebDriver(), GatewayTestConstants.O365_EMAIL_NEW_FOLDER);
		
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.loadEmailApp(getWebDriver());
		o365HomeAction.emailDeleteFolder(getWebDriver(), GatewayTestConstants.O365_EMAIL_NEW_FOLDER);
		
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User created a folder named " + GatewayTestConstants.O365_EMAIL_NEW_FOLDER);
		expectedDataMap.put(GatewayTestConstants.REFERER_URI, "");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("Create Folder Successful");
	}
	
	@Priority(12)
	@Test(groups ={"Regression6", "REACH"}) 
	public void o365_Test_Remove_Favorites() throws Exception {
		Logger.info("Remove Favorites");
		printCredentials();
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "Remove favorites " + GatewayTestConstants.O365_EMAIL_NEW_FOLDER);
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("Remove Favorites Successful");
	}
	
	@Priority(13)
	@Test(groups ={"Regression6", "REACH"}) 
	public void o365_Test_Rename_Folder() throws Exception {
		Logger.info("Rename Folder");
		printCredentials();
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User renamed folder " + GatewayTestConstants.O365_EMAIL_NEW_FOLDER + " to RenameFolder");
		expectedDataMap.put(GatewayTestConstants.REFERER_URI, "");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("Rename Folder Successful");
	}
	
	@Priority(14)
	@Test(groups ={"Regression6", "REACH"}) 
	public void o365_Test_Empty_Folder() throws Exception {
		Logger.info("Empty Folder");
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User emptied folder " + GatewayTestConstants.O365_EMAIL_NEW_FOLDER);
		expectedDataMap.put(GatewayTestConstants.REFERER_URI, "");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("Remove Favorites Successful");
	}
	
	@Priority(15)
	@Test(groups ={"Regression6", "REACH"}) 
	public void o365_Test_Delete_Folder() throws Exception {
		Logger.info("Remove Folder");
		printCredentials();
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User deleted folder " + GatewayTestConstants.O365_EMAIL_NEW_FOLDER);
		expectedDataMap.put(GatewayTestConstants.REFERER_URI, "");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("Remove Folder Successful");
	}
	
	@Priority(16)
	@Test(groups ={"Regression6", "REACH"}) 
	public void o365_Test_Sub_Folder_Create() throws Exception {
		fromTime=backend.getCurrentTime();
		printCredentials();
		Logger.info("Create Sub Folder");
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.loadEmailApp(getWebDriver());
		o365HomeAction.emailDeleteSubFolder(getWebDriver(), GatewayTestConstants.O365_EMAIL_SUB_FOLDER );
		
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.loadEmailApp(getWebDriver());
		o365HomeAction.emailCreateSubFolder(getWebDriver(), "subfolder", GatewayTestConstants.O365_EMAIL_SUB_FOLDER);
		
		Logger.info("Add to Favorite");
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.loadEmailApp(getWebDriver());
		o365HomeAction.emailAddFavoriteSubFolder(getWebDriver(), GatewayTestConstants.O365_EMAIL_SUB_FOLDER );
		
/*		Logger.info("Move To Favorite");
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.loadEmailApp(getWebDriver());
		o365HomeAction.emailMoveSubFolder(getWebDriver(), GatewayTestConstants.O365_EMAIL_SUB_FOLDER, "AFolder");*/
		
		Logger.info("Rename SubFolder");
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.loadEmailApp(getWebDriver());
		o365HomeAction.emailRenameSubFolder(getWebDriver(), GatewayTestConstants.O365_EMAIL_SUB_FOLDER );
		
		Logger.info("Empty SubFolder");
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.loadEmailApp(getWebDriver());
		o365HomeAction.emailEmptySubFolder(getWebDriver(), GatewayTestConstants.O365_EMAIL_SUB_FOLDER );
		
		Logger.info("Delete SubFolder");
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.loadEmailApp(getWebDriver());
		o365HomeAction.emailDeleteSubFolder(getWebDriver(), GatewayTestConstants.O365_EMAIL_SUB_FOLDER );
		
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.IS_ANONYMOUS_PROXY, "false");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User created a folder named " + GatewayTestConstants.O365_EMAIL_SUB_FOLDER);
		expectedDataMap.put(GatewayTestConstants.REFERER_URI, "");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("Create Sub Folder Successful");
	}
	
	@Priority(17)
	@Test(groups ={"Regression6", "REACH"}) 
	public void o365_Test_Sub_Folder_Add_Favorite() throws Exception {
		Logger.info("Sub Folder Add Favorite");
		printCredentials();
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.IS_ANONYMOUS_PROXY, "false");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "Add favorite " + GatewayTestConstants.O365_EMAIL_SUB_FOLDER);
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("Sub Folder Add Favorite Successful");
	}
		
	@Priority(18)
	@Test(groups ={"Regression6", "REACH"}) 
	public void o365_Test_Sub_Folder_Rename() throws Exception {
		Logger.info("Sub Folder Rename");
		printCredentials();
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.IS_ANONYMOUS_PROXY, "false");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User renamed folder " + GatewayTestConstants.O365_EMAIL_SUB_FOLDER);
		expectedDataMap.put(GatewayTestConstants.REFERER_URI, "");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("Sub Folder Rename Successful");
	}
	
	@Priority(19)
	@Test(groups ={"Regression6", "REACH"}) 
	public void o365_Test_Sub_Folder_Empty_Folder() throws Exception {
		Logger.info("Sub Folder Empty");
		printCredentials();
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.IS_ANONYMOUS_PROXY, "false");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User emptied folder " + GatewayTestConstants.O365_EMAIL_SUB_FOLDER);
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("Sub Folder Empty Successful");
	}
	
	@Priority(20)
	@Test(groups ={"Regression6", "REACH"}) 
	public void o365_Test_Delete_Sub_Folder() throws Exception {
		Logger.info("Delete Sub Folder");
		printCredentials();
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.IS_ANONYMOUS_PROXY, "false");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User deleted folder " + GatewayTestConstants.O365_EMAIL_SUB_FOLDER);
		expectedDataMap.put(GatewayTestConstants.REFERER_URI, "");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("Delete Sub Folder Successful");
	}
	
	
	public String getSaasAppUserName(){
		return suiteData.getSaasAppUsername().replaceAll("@", "_");
	}
	
	@BeforeClass(groups ={"Regression6", "REACH"})
	public void doBeforeClass() throws Exception {
		Logger.info("Delete Policy Before Test ");
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME+getSaasAppUserName(),
				suiteData, backend.getGWHeaders(suiteData));

	}
	
	@AfterClass(groups ={"Regression6", "REACH"})
	public void doAfterClass() throws Exception {
		Logger.info("Delete Policy After Test ");
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME+getSaasAppUserName(),
				suiteData, backend.getGWHeaders(suiteData));

	}
}