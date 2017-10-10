package com.elastica.tests.o365;
import static org.testng.Assert.assertTrue;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import org.testng.Assert;
import org.testng.Reporter;
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


public class O365Tests extends GWCommonTest{
	Map <String, Object> expectedDataMap = new HashMap<String, Object>();
	Map<String, String>policyDataMap= new HashMap<String, String>(); 
	String fromTime=backend.getCurrentTime();
	SoftAssert softAssert = new SoftAssert();
	CiqUtils utils=new CiqUtils();
	@BeforeMethod()
	public void clearDataMap(){
		expectedDataMap.clear();
	}

	@Priority(1)
	@Test(groups ={"SANITY", "SANITY1", "P1_SANITY", "EXTERNAL", "IMPERSONAL"}, retryAnalyzer=RetryAnalyzer.class)
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
	
	@Priority(1)
	@Test(groups ={"OKTA"}, retryAnalyzer=RetryAnalyzer.class)
	public void loginToOKTASSO() throws Exception {
		Logger.info("==================================================================================");
		Logger.info(" Login to CloudSOC Portal");
		Logger.info("==================================================================================");
		printCredentials();
		okta.loginPortal(getWebDriver(), suiteData);
		Logger.info("==================================================================================");
		Logger.info(" Login to cloudSoc portal done");
		Logger.info("==================================================================================");
	}
	
	@Priority(2)
	@Test(groups ={"SANITY", "REACH_AGENT", "SANITY1", "IMPERSONAL", "OKTA", "REACH", "IE"},retryAnalyzer=RetryAnalyzer.class)  //,dependsOnMethods = { "loginToCloudSocAppAndSetupSSO" }
	public void o365_Test_01_ValidateLoginActivityEvent() throws Exception {
		Logger.info("==================================================================================");
		Logger.info(" Verifying the login event to Saas App "+suiteData.getSaasAppName());
		Logger.info("==================================================================================");
		printCredentials();
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
	
	@Priority(2)
	@Test(groups ={"EXTERNAL"},retryAnalyzer=RetryAnalyzer.class)  //,dependsOnMethods = { "loginToCloudSocAppAndSetupSSO" }
	public void o365_Test_01_ExternalLoginEvent() throws Exception {
		Logger.info("==================================================================================");
		Logger.info(" Login into One Drive");
		Logger.info("==================================================================================");
		printCredentials();
		o365Login.onedriveLogin(getWebDriver(), suiteData);
		o365HomeAction.selectView(getWebDriver());
		expectedDataMap.clear();
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
	@Test(groups ={"SANITY", "REACH_AGENT","OKTA", "REACH"}, retryAnalyzer=RetryAnalyzer.class) //dependsOnMethods = { "o365_Test_01_ValidateLogingActivityEvent" }
	public void o365_Test_02_ValidateSendEmailActivityEvent() throws Exception {
		Logger.info("==================================================================================" );
		Logger.info(" Verifying email send event in Outlook");
		Logger.info("==================================================================================" );
		printCredentials();
		fromTime=backend.getCurrentTime();
		//o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
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
	@Test(groups ={"SANITY", "REACH_AGENT", "P1_SANITY", "EXTERNAL", "REACH"},retryAnalyzer=RetryAnalyzer.class) //,dependsOnMethods = { "o365_Test_01_ValidateLogingActivityEvent" }
	public void o365_Test_03_ValidateDownloadActivity() throws Exception {
		Logger.info("==================================================================================" );
		Logger.info(" Verifying download activity event");
		Logger.info("==================================================================================" );
		printCredentials();
		fromTime=backend.getCurrentTime();
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getGWHeaders(suiteData));
		//o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.loadOnedriveAppViaLink(getWebDriver(), getOneDriveBusinessUrl());
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
	@Test(groups ={"SANITY","REACH_AGENT", "EXTERNAL", "REACH"},retryAnalyzer=RetryAnalyzer.class) //,dependsOnMethods = { "o365_Test_01_ValidateLogingActivityEvent" }
	public void o365_Test_04_ValidateDecryptedDownload() throws Exception {
		String fileDownloaded="readme.pdf";
		Logger.info("==================================================================================" );
		Logger.info(" Verifying decrypt download activity event with policy");
		Logger.info("==================================================================================" );
		printCredentials();
		fromTime=backend.getCurrentTime();
		deleteFileInDownloadFolder(GatewayTestConstants.O365_DOWNLOAD_ENCRYPTED_FILE);
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getGWHeaders(suiteData));
		policy.createEnableEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getGWHeaders(suiteData));
		//o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.loadOnedriveAppViaLink(getWebDriver(), getOneDriveBusinessUrl());
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
	@Test(groups ={"SANITY", "REACH_AGENT", "EXTERNAL", "OKTA", "REACH"},retryAnalyzer=RetryAnalyzer.class) //,dependsOnMethods = { "o365_Test_01_ValidateLogingActivityEvent" }
	public void o365_Test_05_ValidateUploadActivity() throws Exception {
		Logger.info("==================================================================================");
		Logger.info(" Verifying upload activity event for One Drive business");
		Logger.info("==================================================================================");
		printCredentials();
		expectedDataMap.clear();
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getGWHeaders(suiteData));
		//o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.loadOnedriveAppViaLink(getWebDriver(), getOneDriveBusinessUrl());
		//o365HomeAction.loadOnedriveApp(getWebDriver());
		o365HomeAction.deleteFile(getWebDriver(), GatewayTestConstants.O365_UPLOAD_FILE);
		//o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.loadOnedriveAppViaLink(getWebDriver(), getOneDriveBusinessUrl());
		//o365HomeAction.loadOnedriveApp(getWebDriver());
		o365HomeAction.uploadItemFile(getWebDriver(),  System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+
				"resources"+File.separator+"meta"+File.separator + GatewayTestConstants.O365_UPLOAD_FILE);
		//o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.loadOnedriveAppViaLink(getWebDriver(), getOneDriveBusinessUrl());
		//o365HomeAction.loadOnedriveApp(getWebDriver());
		o365HomeAction.deleteFile(getWebDriver(), GatewayTestConstants.O365_UPLOAD_FILE);
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
	
	@Priority(7)
	@Test(groups ={"SANITY", "REACH_AGENT", "EXTERNAL", "OKTA", "REACH"}) //,dependsOnMethods = { "o365_Test_01_ValidateLogingActivityEvent" }
	public void o365_Test_06_ValidateCIQ_PII() throws Exception {
		Logger.info("Verifying CIQ for the file "+GatewayTestConstants.CIQ_FILE);
		printCredentials();
		//o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.loadOnedriveAppViaLink(getWebDriver(), getOneDriveBusinessUrl());
		//o365HomeAction.loadOnedriveApp(getWebDriver());
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData));
		policy.deletePolicy(GatewayTestConstants.CIQ_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData));
		utils.createCIQProfile( CIQConstants.ZERO,suiteData, "CIQ_FE_GW_DontDelete","CIQ_FE_GW_DontDelete",CIQConstants.SOURCE_DCI,CIQConstants.DEFAUTRISKVALUE);
		policy.createCIQPolicy(GatewayTestConstants.CIQ_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData));
		
		o365HomeAction.deleteFile(getWebDriver(), GatewayTestConstants.CIQ_FILE);
		o365HomeAction.uploadItemFile(getWebDriver(),  System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+
				"resources"+File.separator+"ciq"+File.separator + GatewayTestConstants.CIQ_FILE );
		o365HomeAction.hardWait(50);
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "File "+GatewayTestConstants.CIQ_FILE+" upload has risk(s) - PII");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("CIQ tests execution done");
		
	}
	
	
	
	@Priority(8)
	@Test(groups ={"SANITY", "REACH_AGENT", "EXTERNAL", "REACH"}) 
	public void o365_Test_06_ValidateEncryptedUploadActivity() throws Exception {
		Logger.info("==================================================================================");
		Logger.info(" Verifying encrypt upload activity event");
		Logger.info("==================================================================================");
		printCredentials();
		fromTime=backend.getCurrentTime();
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData));
		policy.createEnableEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData));
		//o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.loadOnedriveAppViaLink(getWebDriver(), getOneDriveBusinessUrl());
		//o365HomeAction.loadOnedriveApp(getWebDriver());
		//o365HomeAction.deleteFile(getWebDriver(), GatewayTestConstants.O365_DELETE_ENCRYPTED_FILE);
		o365HomeAction.deleteFile(getWebDriver(), GatewayTestConstants.O365_UPLOAD_ENCRYPT_FILE);
		//o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.loadOnedriveAppViaLink(getWebDriver(), getOneDriveBusinessUrl());
		//o365HomeAction.loadOnedriveApp(getWebDriver());
		o365HomeAction.uploadItemFile(getWebDriver(),  System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+
				"resources"+File.separator+"meta"+File.separator + GatewayTestConstants.O365_UPLOAD_ENCRYPT_FILE);
		//o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.loadOnedriveAppViaLink(getWebDriver(), getOneDriveBusinessUrl());
		//o365HomeAction.loadOnedriveApp(getWebDriver());
		o365HomeAction.deleteFile(getWebDriver(), GatewayTestConstants.O365_UPLOAD_ENCRYPT_FILE);
		/*Log Fields to check*/
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "File readme1.pdf encrypted on upload for user "+suiteData.getSaasAppUsername()+".");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "File Encryption" );
		expectedDataMap.put(GatewayTestConstants.POLICY_TYPE, GatewayTestConstants.FILE_TRANSFER_POLICY);
		expectedDataMap.put(GatewayTestConstants.FILE_SIZE, "57");
		expectedDataMap.put(GatewayTestConstants.POLICY_ACTION, GatewayTestConstants.POLICY_ACTION_ALERT);
		expectedDataMap.put(GatewayTestConstants.ACTION_TAKEN, GatewayTestConstants.ACTION_TAKEN_ENCRYPT );
		expectedDataMap.put(GatewayTestConstants.CRYPTO_KEY_VERSION, GatewayTestConstants.CRYPTO_KEY_VERSION_USED);
		expectedDataMap.put(GatewayTestConstants.OBJECT_NAME, "readme1.pdf");
		expectedDataMap.put(GatewayTestConstants.CRYPTO_KEY_SERVER, GatewayTestConstants.CRYPTO_KEY_SERVER_USED);
		expectedDataMap.put(GatewayTestConstants.CRYPTO_KEY_NAME, GatewayTestConstants.CRYPTO_KEY_NAME_USED);
		expectedDataMap.remove(GatewayTestConstants.USER_NAME);
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Expected log: "+expectedDataMap.get(GatewayTestConstants.MESSAGE)+" does not exist");
		Logger.info("==================================================================================");
		Logger.info(" Encrypt upload actvity event verification successful");
		Logger.info("==================================================================================");
	}
	
	@Priority(9)
	@Test(groups ={"SANITY", "REACH_AGENT", "EXTERNAL", "REACH"}) 
	public void o365_Test_Share_Via_Email() throws Exception {
		Logger.info("==================================================================================");
		Logger.info(" Verifying Share folder via Email");
		Logger.info("==================================================================================");
		printCredentials();
		//o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		//o365HomeAction.loadOnedriveApp(getWebDriver());
		o365HomeAction.loadOnedriveAppViaLink(getWebDriver(), getOneDriveBusinessUrl());
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
	
	@Priority(10)
	@Test(groups ={"SANITY", "REACH_AGENT", "EXTERNAL", "REACH"}) 
	public void o365_Test_Share_Link() throws Exception {
		Logger.info("==================================================================================");
		Logger.info(" Verifying share viw link event");
		Logger.info("==================================================================================");
		printCredentials();
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
	
	@Priority(11)
	@Test(groups ={"SANITY", "REACH_AGENT", "EXTERNAL", "OKTA", "REACH"}) //,dependsOnMethods = { "o365_Test_01_ValidateLogingActivityEvent" }
	public void o365_Test_10_ValidateLogoutActivityEvent() throws Exception {
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
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Expected log: "+expectedDataMap.get(GatewayTestConstants.MESSAGE)+" does not exist");
		Logger.info("==================================================================================");
		Logger.info(" Logout event verification successful");
		Logger.info("==================================================================================");
	}
	
	
	@Priority(10)
//	@Test(groups ={"SANITY"})
	public void o365_Test_01_ValidateSplunkLog() throws Exception {
		assertTrue(backend.validateSplunkLog(suiteData), "Host is not found");
	}
	
	
	public String getSaasAppUserName(){
		return suiteData.getSaasAppUsername().replaceAll("@", "_");
	}
	
	@BeforeClass(groups ={"SANITY", "REACH_AGENT", "EXTERNAL", "REACH"})
	public void doBeforeClass() throws Exception {
		Logger.info("**************************************************************************");
		Logger.info("Cleaning up all policies before the tests start... ");
		Logger.info("**************************************************************************");
		printCredentials();
		policy.deletePolicy(GatewayTestConstants.CIQ_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData));
		policy.deletePolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData));
		Logger.info("All Polices got cleaned up");
		Logger.info("**************************************************************************");
	}
	
	@Priority(4)
	@Test(groups ={"IE"},retryAnalyzer=RetryAnalyzer.class) //,dependsOnMethods = { "o365_Test_01_ValidateLogingActivityEvent" }
	public void o365_Test_03_ValidateDownloadIEActivity() throws Exception {
		Logger.info("==================================================================================" );
		Logger.info(" Verifying download activity event");
		Logger.info("==================================================================================" );
		printCredentials();
		fromTime=backend.getCurrentTime();
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getGWHeaders(suiteData));
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.loadOnedriveApp(getWebDriver());
		o365HomeAction.downloadItemFileByNameIE(getWebDriver(), GatewayTestConstants.O365_FILE);
		ieDownloadFile();
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
	
	@AfterClass(groups ={"SANITY", "REACH_AGENT", "EXTERNAL", "REACH"})
	public void doAfterClass() throws Exception {Logger.info("**************************************************************************");
		Logger.info("Cleaning up all policies after the tests completed... ");
		printCredentials();
		policy.deletePolicy(GatewayTestConstants.CIQ_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData));
		policy.deletePolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData));
		Logger.info("**************************************************************************");
	}
	
	
}