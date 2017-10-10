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

import com.elastica.common.GWCommonTest;
import com.elastica.gateway.GatewayTestConstants;
import com.elastica.listeners.Priority;
import com.elastica.listeners.RetryAnalyzer;
import com.elastica.logger.Logger;

public class O365TestsChrome extends GWCommonTest{
	Map <String, Object> expectedDataMap = new HashMap<String, Object>();
	Map<String, String>policyDataMap= new HashMap<String, String>(); 
	String fromTime=backend.getCurrentTime();
	SoftAssert softAssert = new SoftAssert();
	
	@BeforeMethod()
	public void clearDataMap(){
		expectedDataMap.clear();
	}

	@Priority(1)
	@Test(groups ={"SANITY", "SANITY1"}, retryAnalyzer=RetryAnalyzer.class)
	public void loginToCloudSocAppAndSetupSSO() throws Exception {
		Logger.info("==================================================================================");
		Logger.info(" Login to CloudSOC Portal");
		Logger.info("==================================================================================");
		login.loginCloudSocPortal(getWebDriver(), suiteData);
		Logger.info("==================================================================================");
		Logger.info(" Login to cloudSoc portal done");
		Logger.info("==================================================================================");
	}
	
	@Priority(2)
	@Test(groups ={"SANITY", "SANITY1", "REACH"},retryAnalyzer=RetryAnalyzer.class)  //,dependsOnMethods = { "loginToCloudSocAppAndSetupSSO" }
	public void o365_Test_01_ValidateLoginActivityEvent() throws Exception {
		Logger.info("==================================================================================");
		Logger.info(" Verifying the login event to Saas App "+suiteData.getSaasAppName());
		Logger.info("==================================================================================");
		o365Login.login(getWebDriver(), suiteData);
		policy.deletePolicy(GatewayTestConstants.CIQ_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData));
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
	@Test(groups ={"SANITY", "REACH"}, retryAnalyzer=RetryAnalyzer.class) //dependsOnMethods = { "o365_Test_01_ValidateLogingActivityEvent" }
	public void o365_Test_02_ValidateSendEmailActivityEvent() throws Exception {
		Logger.info("==================================================================================" );
		Logger.info(" Verifying email send event in Outlook");
		Logger.info("==================================================================================" );
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
		Logger.info("==================================================================================" );
		Logger.info(" Send Email event verification successful");
		Logger.info("==================================================================================" );
	}
	
	@Priority(4)
	@Test(groups ={"SANITY", "REACH"},retryAnalyzer=RetryAnalyzer.class) //,dependsOnMethods = { "o365_Test_01_ValidateLogingActivityEvent" }
	public void o365_Test_03_ValidateDownloadActivity() throws Exception {
		Logger.info("==================================================================================" );
		Logger.info(" Verifying download activity event");
		Logger.info("==================================================================================" );
		fromTime=backend.getCurrentTime();
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getGWHeaders(suiteData));
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.loadOnedriveApp(getWebDriver());
		o365HomeAction.downloadItemFileByName(getWebDriver(), GatewayTestConstants.O365_FILE);
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

	@Priority(5)
	@Test(groups ={"SANITY", "REACH"},retryAnalyzer=RetryAnalyzer.class) //,dependsOnMethods = { "o365_Test_01_ValidateLogingActivityEvent" }
	public void o365_Test_04_ValidateDecryptedDownload() throws Exception {
		String fileDownloaded="readme.pdf";
		Logger.info("==================================================================================" );
		Logger.info(" Verifying decrypt download activity event with policy");
		Logger.info("==================================================================================" );
		fromTime=backend.getCurrentTime();
		deleteFileInDownloadFolder(GatewayTestConstants.O365_DOWNLOAD_ENCRYPTED_FILE);
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getGWHeaders(suiteData));
		policy.createEnableEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getGWHeaders(suiteData));
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.loadOnedriveApp(getWebDriver());
		o365HomeAction.downloadItemFileByName(getWebDriver(), GatewayTestConstants.O365_DOWNLOAD_ENCRYPTED_FILE);
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "File Decryption" );
		expectedDataMap.put(GatewayTestConstants.ACTION_TAKEN, "decrypt");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "File readme.pdf.eef decrypted on download for user "+suiteData.getSaasAppUsername()+".");
		expectedDataMap.put(GatewayTestConstants.POLICY_TYPE, "FileTransfer");
		expectedDataMap.put(GatewayTestConstants.OBJECT_NAME, GatewayTestConstants.O365_DOWNLOAD_ENCRYPTED_FILE );
		expectedDataMap.put(GatewayTestConstants.SCOPE, GatewayTestConstants.O365_DOWNLOAD_ENCRYPTED_FILE);
		expectedDataMap.put(GatewayTestConstants.FILE_SIZE, "58");
		expectedDataMap.put(GatewayTestConstants.POLICY_ACTION, GatewayTestConstants.POLICY_ACTION_ALERT);
		expectedDataMap.put(GatewayTestConstants.CRYPTO_KEY_SERVER, GatewayTestConstants.CRYPTO_KEY_SERVER_USED);
		expectedDataMap.put(GatewayTestConstants.CRYPTO_KEY_NAME, GatewayTestConstants.CRYPTO_KEY_NAME_USED);
		expectedDataMap.put(GatewayTestConstants.CRYPTO_KEY_VERSION, GatewayTestConstants.CRYPTO_KEY_VERSION_USED);
		expectedDataMap.remove(GatewayTestConstants.COUNTRY);
		expectedDataMap.remove(GatewayTestConstants.USER_NAME);
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Expected log: "+expectedDataMap.get(GatewayTestConstants.MESSAGE)+" does not exist");
		Logger.info("Log get appear on investigate");
		Logger.info("Checking if the file get decrypted on download and saved to download folder");
		softAssert.assertTrue(isFileDownloadSuccess(fileDownloaded), 
				"File " +fileDownloaded+ " not found in download folder" +" but found "+GatewayTestConstants.O365_DOWNLOAD_ENCRYPTED_FILE);
		Assert.assertTrue(isFileDownloadSuccess(fileDownloaded), "Expected File " +fileDownloaded+ " not found in download folder" );
		Logger.info("Checking if the file get downloaded after decryption");
		Logger.info("==================================================================================");
		Logger.info(" Decrypt download actvity event verification successful");
		Logger.info("==================================================================================");
	}
	
	@Priority(6)
	@Test(groups ={"SANITY", "REACH"}) 
	public void o365_Test_05_ValidateUploaFilesActivity() throws Exception {
		Logger.info("==================================================================================");
		Logger.info(" Verifying upload file activity event for One Drive business");
		Logger.info("==================================================================================");
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getGWHeaders(suiteData));
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.loadOnedriveApp(getWebDriver());
		o365HomeAction.deleteFile(getWebDriver(), GatewayTestConstants.O365_MULTI_UPLOAD_FILE1);
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.loadOnedriveApp(getWebDriver());
		o365HomeAction.uploadItemFileClick(getWebDriver());
		uploadSingleFiles_Chrome(GatewayTestConstants.MULTIPLE_FILE_PATH, "multi1");
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.loadOnedriveApp(getWebDriver());
		o365HomeAction.deleteFile(getWebDriver(), GatewayTestConstants.O365_MULTI_UPLOAD_FILE1);
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Upload");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User uploaded file named "+GatewayTestConstants.O365_MULTI_UPLOAD_FILE1);
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Document");
		expectedDataMap.put(GatewayTestConstants.OBJECT_NAME, GatewayTestConstants.O365_MULTI_UPLOAD_FILE1);
		expectedDataMap.put(GatewayTestConstants.FILE_SIZE, "3");
		expectedDataMap.put(GatewayTestConstants.CONTENT_INLINE, "1");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Expected log: "+expectedDataMap.get(GatewayTestConstants.MESSAGE)+" does not exist");
		Logger.info("==================================================================================");
		Logger.info(" Upload file event verification successful");
		Logger.info("==================================================================================");
	}
	
	
	@Priority(7)
	@Test(groups ={"SANITY", "REACH"}) 
	public void o365_Test_06_ValidateUploadFolderActivity() throws Exception {
		Logger.info("==================================================================================");
		Logger.info(" Verifying upload file activity event for One Drive business");
		Logger.info("==================================================================================");
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getGWHeaders(suiteData));
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.loadOnedriveApp(getWebDriver());
		o365HomeAction.deleteFile(getWebDriver(), GatewayTestConstants.O365_MULTI_UPLOAD_FOLDER);
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.loadOnedriveApp(getWebDriver());
		o365HomeAction.uploadItemFolderClick(getWebDriver());
		uploadFolder_Chrome(GatewayTestConstants.MULTIPLE_FILE_PATH, GatewayTestConstants.O365_MULTI_UPLOAD_FOLDER);
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.loadOnedriveApp(getWebDriver());
		o365HomeAction.deleteFile(getWebDriver(), GatewayTestConstants.O365_MULTI_UPLOAD_FOLDER);
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Upload");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User uploaded file named uploadfolderfile.txt");
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Document");
		expectedDataMap.put(GatewayTestConstants.OBJECT_NAME, "uploadfolderfile.txt");
		expectedDataMap.put(GatewayTestConstants.CONTENT_INLINE, "1");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Expected log: "+expectedDataMap.get(GatewayTestConstants.MESSAGE)+" does not exist");
		
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Upload");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User uploaded file named uploadfolderfile3.zip");
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Document");
		expectedDataMap.put(GatewayTestConstants.OBJECT_NAME, "uploadfolderfile3.zip");
		expectedDataMap.put(GatewayTestConstants.CONTENT_INLINE, "1");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Expected log: "+expectedDataMap.get(GatewayTestConstants.MESSAGE)+" does not exist");

		
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Upload");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User uploaded file named uploadfolderfile2.pdf");
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Document");
		expectedDataMap.put(GatewayTestConstants.OBJECT_NAME, "uploadfolderfile2.pdf");
		expectedDataMap.put(GatewayTestConstants.CONTENT_INLINE, "1");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Expected log: "+expectedDataMap.get(GatewayTestConstants.MESSAGE)+" does not exist");

		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Create");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User created a new folder " + GatewayTestConstants.O365_MULTI_UPLOAD_FOLDER);
	//	expectedDataMap.put(GatewayTestConstants.OBJECT_NAME, GatewayTestConstants.O365_MULTI_UPLOAD_FILE1);
	//	expectedDataMap.put(GatewayTestConstants.CONTENT_INLINE, "1");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Expected log: "+expectedDataMap.get(GatewayTestConstants.MESSAGE)+" does not exist");

		
		Logger.info("==================================================================================");
		Logger.info(" Upload file event verification successful");
		Logger.info("==================================================================================");
	}
	
	@Priority(8)
	@Test(groups ={"SANITY", "REACH"}) 
	public void o365_Test_07_ValidateCIQ_PII() throws Exception {
		Logger.info("Verifying CIQ for the file "+GatewayTestConstants.CIQ_FILE);
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData));
		policy.deletePolicy(GatewayTestConstants.CIQ_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData));
		policy.createCIQPolicy(GatewayTestConstants.CIQ_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData));
		
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.loadOnedriveApp(getWebDriver());
		o365HomeAction.deleteFile(getWebDriver(), GatewayTestConstants.CIQ_FILE);
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.loadOnedriveApp(getWebDriver());
		o365HomeAction.uploadItemFileClick(getWebDriver());
		uploadSingleFiles_Chrome(System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+
				"resources"+File.separator+"ciq"+File.separator , GatewayTestConstants.CIQ_FILE);
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.loadOnedriveApp(getWebDriver());
		o365HomeAction.deleteFile(getWebDriver(), GatewayTestConstants.CIQ_FILE);
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "File "+GatewayTestConstants.CIQ_FILE+" upload has risk(s) - PII");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("CIQ tests execution done");
		
	}
	
	@Priority(9)
	@Test(groups ={"SANITY", "SANITY1", "REACH"}) 
	public void o365_Test_07_ValidateEncryptedUploadActivity() throws Exception {
		Logger.info("==================================================================================");
		Logger.info(" Verifying encrypt upload activity event");
		Logger.info("==================================================================================");
		fromTime=backend.getCurrentTime();
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getGWHeaders(suiteData));
		policy.createEnableEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData));
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.loadOnedriveApp(getWebDriver());
		o365HomeAction.deleteFile(getWebDriver(), GatewayTestConstants.O365_MULTI_UPLOAD_FILE1);
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.loadOnedriveApp(getWebDriver());
		o365HomeAction.uploadItemFileClick(getWebDriver());
		uploadSingleFiles_Chrome(GatewayTestConstants.MULTIPLE_FILE_PATH, "multi1");
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.loadOnedriveApp(getWebDriver());
		o365HomeAction.deleteFile(getWebDriver(), GatewayTestConstants.O365_MULTI_UPLOAD_FILE1);
		
		/*Log Fields to check*/
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "File " + GatewayTestConstants.O365_MULTI_UPLOAD_FILE1 + " encrypted on upload for user "+suiteData.getSaasAppUsername()+".");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "File Encryption" );
		expectedDataMap.put(GatewayTestConstants.POLICY_TYPE, GatewayTestConstants.FILE_TRANSFER_POLICY);
		expectedDataMap.put(GatewayTestConstants.FILE_SIZE, "3");
		expectedDataMap.put(GatewayTestConstants.POLICY_ACTION, GatewayTestConstants.POLICY_ACTION_ALERT);
		expectedDataMap.put(GatewayTestConstants.ACTION_TAKEN, GatewayTestConstants.ACTION_TAKEN_ENCRYPT );
		expectedDataMap.put(GatewayTestConstants.CRYPTO_KEY_VERSION, GatewayTestConstants.CRYPTO_KEY_VERSION_USED);
		expectedDataMap.put(GatewayTestConstants.OBJECT_NAME, GatewayTestConstants.O365_MULTI_UPLOAD_FILE1);
		expectedDataMap.put(GatewayTestConstants.CRYPTO_KEY_SERVER, GatewayTestConstants.CRYPTO_KEY_SERVER_USED);
		expectedDataMap.put(GatewayTestConstants.CRYPTO_KEY_NAME, GatewayTestConstants.CRYPTO_KEY_NAME_USED);
		expectedDataMap.remove(GatewayTestConstants.USER_NAME);
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Expected log: "+expectedDataMap.get(GatewayTestConstants.MESSAGE)+" does not exist");
		Logger.info("==================================================================================");
		Logger.info(" Encrypt upload actvity event verification successful");
		Logger.info("==================================================================================");
	}
	
	@Priority(10)
	@Test(groups ={"SANITY", "REACH"}) 
	public void o365_Test_Share_Via_Email() throws Exception {
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
		//expectedDataMap.put(GatewayTestConstants.SHARED_WITH, "testusersjc.prod@securletautoo365featle.com");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.OBJECT_NAME, "NewFolder");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("==================================================================================");
		Logger.info(" Share event via email validation done");
		Logger.info("==================================================================================");
		
	}
	
	@Priority(11)
	@Test(groups ={"SANITY", "REACH"}) 
	public void o365_Test_Share_Link() throws Exception {
		Logger.info("==================================================================================");
		Logger.info(" Verifying share viw link event");
		Logger.info("==================================================================================");
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.loadOnedriveApp(getWebDriver());
		o365HomeAction.shareItemByLink(getWebDriver(), GatewayTestConstants.GDRIVE_ONE_DRIVE_FILE_ATTACH);
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Share");
		expectedDataMap.put(GatewayTestConstants.MESSAGE,  "User got link of " + GatewayTestConstants.GDRIVE_ONE_DRIVE_FILE_ATTACH); 
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Document/Folder");
		//expectedDataMap.put(GatewayTestConstants.SHARE_WITH, "All");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.OBJECT_NAME, "Dial-FAQ.pdf");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("==================================================================================");
		Logger.info(" Share via link verification done");
		Logger.info("==================================================================================");
		
	}
	
	@Priority(12)
	@Test(groups ={"SANITY", "REACH"}) 
	public void o365_Test_10_ValidateLogoutActivityEvent() throws Exception {
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365Login.logout(getWebDriver());
		Logger.info("==================================================================================");
		Logger.info(" Verifying the logout event");
		Logger.info("==================================================================================");
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User logged out");
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Session");
		expectedDataMap.put(GatewayTestConstants.REFERER_URI, "");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Expected log: "+expectedDataMap.get(GatewayTestConstants.MESSAGE)+" does not exist");
		Logger.info("==================================================================================");
		Logger.info(" Logout event verification successful");
		Logger.info("==================================================================================");
	}
	
	public String getSaasAppUserName(){
		return suiteData.getSaasAppUsername().replaceAll("@", "_");
	}
	
	@BeforeClass(groups ={"SANITY", "REACH"})
	public void doBeforeClass() throws Exception {
		Logger.info("**************************************************************************");
		Logger.info("Cleaning up all policies before the tests start... ");
		Logger.info("**************************************************************************");
		policy.deletePolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData));
		policy.deletePolicy(GatewayTestConstants.CIQ_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData));

		Logger.info("All Polices got cleaned up");
		Logger.info("**************************************************************************");
	}
	
	@AfterClass(groups ={"SANITY", "REACH"})
	public void doAfterClass() throws Exception {Logger.info("**************************************************************************");
		Logger.info("Cleaning up all policies after the tests completed... ");
		policy.deletePolicy(GatewayTestConstants.CIQ_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData));
		policy.deletePolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData));
		Logger.info("**************************************************************************");
	}
	
}
