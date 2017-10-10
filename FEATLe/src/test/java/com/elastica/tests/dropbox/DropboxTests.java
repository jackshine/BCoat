package com.elastica.tests.dropbox;

import static org.testng.Assert.assertTrue;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;

import com.elastica.common.CommonTest;
import com.elastica.common.GWCommonTest;
import com.elastica.gateway.GatewayTestConstants;
import com.elastica.listeners.Priority;
import com.elastica.listeners.RetryAnalyzer;
import com.elastica.logger.Logger;
import com.elastica.tests.o365.CIQConstants;
import com.elastica.tests.o365.CiqUtils;

public class DropboxTests extends GWCommonTest {
	Map <String, Object> expectedDataMap = new HashMap<String, Object>();
	String fromTime=backend.getCurrentTime();
	String title;
	CiqUtils utils=new CiqUtils();

	@Priority(1)
	@Test(groups ={"SANITY","SANITY1","P1"},retryAnalyzer=RetryAnalyzer.class)
	public void loginToCloudSocAppAndSetupSSO() throws Exception {
		fromTime=backend.getCurrentTime();
		Reporter.log("Starting performing SSO setup", true);
		printCredentials();
		login.loginCloudSocPortal(getWebDriver(), suiteData);
		Thread.sleep(30000);
		Reporter.log("Finished performing SSO setup", true);
	}
	
	@Priority(2)
	@Test(groups ={"SANITY","SANITY1", "REACH","P1"},retryAnalyzer=RetryAnalyzer.class) 
	public void ValidateDropboxLogin() throws Exception {
		fromTime=backend.getCurrentTime();
		Logger.info("==================================================================================");
		Logger.info("Verifying the login event");
		Logger.info("==================================================================================");
		printCredentials();
		expectedDataMap.clear();
		Logger.info("==================================================================================");
		Logger.info("Perfroming dropbox SaasApp login operation");
		Logger.info("==================================================================================");

		dba.login(getWebDriver(), suiteData);
		String fileName=GatewayTestConstants.DROPBOX_FILESHARE;
		Reporter.log(" completed SaasApp login operation ",true);
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Login");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User logged in"); 
		expectedDataMap.put(GatewayTestConstants.ACCOUNT_TYPE,suiteData.getAccountType());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		expectedDataMap.put(GatewayTestConstants.ELASTICA_USER, suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.IS_ANONYMOUS_PROXY, "false");
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Session");
		expectedDataMap.put(GatewayTestConstants.REQ_URI, GatewayTestConstants.DROPBOX_REQ_URI_LOGIN);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER, suiteData.getSaasAppUsername());
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("==================================================================================");
		Logger.info("Verify login event -successful");
		Logger.info("==================================================================================");

	}
	
	@Priority(3)
	@Test(groups ={"SANITY", "REACH","P1"}) 
	public void ValidatePublicFileShare() throws Exception {
		fromTime=backend.getCurrentTime();
		printCredentials();
		Logger.info("Verifying the Public file share event");

		Reporter.log("Verifying the public share event", true);
		String fileName=GatewayTestConstants.DROPBOX_FILESHARE;
		expectedDataMap.clear();
		Logger.info("==================================================================================");
		Logger.info("Perfroming dropbox SaasApp Public file share operation");
		Logger.info("==================================================================================");

		dba.publicShare(getWebDriver(),  fileName);
		Logger.info("==================================================================================");
		Logger.info("Completed dropbox SaasApp Public file share operation");
		Logger.info("==================================================================================");
		Thread.sleep(5000);
		dba.publicUnshare(getWebDriver(),  fileName);
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Share");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User clicked on get link for "+fileName); 
		expectedDataMap.put(GatewayTestConstants.ACCOUNT_TYPE,suiteData.getAccountType());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		expectedDataMap.put(GatewayTestConstants.ELASTICA_USER, suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.IS_ANONYMOUS_PROXY, "false");
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File/Folder");
		expectedDataMap.put(GatewayTestConstants.REQ_URI, GatewayTestConstants.DROPBOX_REQ_URI_SHARE);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getSaasAppUsername());
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("==================================================================================");
		Logger.info("Verifying the Public file share event -successful");
		Logger.info("==================================================================================");

	}
	
	@Priority(3)
	@Test(groups ={"REGRESSION", "REACH","P1"})  
	public void ValidatePublicFileUnshare() throws Exception {
		fromTime=backend.getCurrentTime();
		Logger.info("==================================================================================");
		Logger.info("Verifying the Public file Unshare event");
		Logger.info("==================================================================================");
		printCredentials();
		expectedDataMap.clear();
		String fileName=GatewayTestConstants.DROPBOX_FILESHARE;
		// share the file and then unshare
		Logger.info("==================================================================================");
		Logger.info("Perfroming dropbox SaasApp Public file share operation");
		Logger.info("==================================================================================");
		dba.publicShare(getWebDriver(),  fileName);
		Logger.info("==================================================================================");
		Logger.info("Completed file share operation");
		Logger.info("==================================================================================");
		Logger.info("==================================================================================");
		Logger.info("Perfroming dropbox SaasApp Public file Unshare operation");
		Logger.info("==================================================================================");
		dba.publicUnshare(getWebDriver(),  fileName);
		Logger.info("==================================================================================");
		Logger.info("Completed dropbox SaasApp Public file Unshare operation");
		Logger.info("==================================================================================");
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Unshare");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User removed link for "+fileName); 
		expectedDataMap.put(GatewayTestConstants.ACCOUNT_TYPE,suiteData.getAccountType());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		expectedDataMap.put(GatewayTestConstants.ELASTICA_USER, suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.IS_ANONYMOUS_PROXY, "false");
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Link");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getSaasAppUsername());
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("==================================================================================");
		Logger.info("Verifying the Public file Unshare event -successful");
		Logger.info("==================================================================================");
	}
	
	
	@Priority(4)
	@Test(groups ={"SANITY", "REACH","P1"})  
	public void ValidateDownload() throws Exception {
		fromTime=backend.getCurrentTime();
		Logger.info("==================================================================================");
		Logger.info("Verifying the file Download event");
		Logger.info("==================================================================================");
		printCredentials();
		String fileName=GatewayTestConstants.DROPBOX_FILESHARE;
		Logger.info("==================================================================================");
		Logger.info("Perfroming dropbox SaasApp Public file share operation");
		Logger.info("==================================================================================");
		dba.download(getWebDriver(), fileName);
		Logger.info("==================================================================================");
		Logger.info("Completed dropbox SaasApp Public file share operation");
		Logger.info("==================================================================================");
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Download");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User downloaded file "+fileName); 
		expectedDataMap.put(GatewayTestConstants.ACCOUNT_TYPE,suiteData.getAccountType());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		expectedDataMap.put(GatewayTestConstants.ELASTICA_USER, suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.IS_ANONYMOUS_PROXY, "false");
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File");
		expectedDataMap.put(GatewayTestConstants.FILE_SIZE, "675");

		//expectedDataMap.put(GatewayTestConstants.RESP_CODE, "200");
		expectedDataMap.put(GatewayTestConstants.REQ_URI, GatewayTestConstants.DROPBOX_REQ_URI_DOWNLOAD);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getSaasAppUsername());
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("==================================================================================");
		Logger.info("Verifying the file download event -successful");
		Logger.info("==================================================================================");
	}
	@Priority(7)
	@Test(groups ={"SANITY", "REACH","CIQ","P1"})  
	public void ValidateCIQDownload() throws Exception {
		fromTime=backend.getCurrentTime();
		Logger.info("==================================================================================");
		Logger.info("Verifying the CIQ risk file Download event");
		Logger.info("==================================================================================");
		printCredentials();
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData));
		utils.createCIQProfile( CIQConstants.ZERO,suiteData, "CIQ_FE_GW_DontDelete","CIQ_FE_GW_DontDelete",CIQConstants.SOURCE_DCI,CIQConstants.DEFAUTRISKVALUE);
		policy.createCIQPolicy(GatewayTestConstants.CIQ_DOWNLOAD_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData),"CIQ_FE_GW_DontDelete","download");

		String fileName=GatewayTestConstants.CIQ_FILE;
			
		Logger.info("==================================================================================");
		Logger.info("Perfroming dropbox SaasApp download file  operation");
		Logger.info("==================================================================================");
		dba.download(getWebDriver(), fileName);
		dba.deleteFile(getWebDriver(),GatewayTestConstants.CIQ_FILE );
		Logger.info("==================================================================================");
		Logger.info("Completed dropbox SaasApp download file  operation");
		Logger.info("==================================================================================");
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Content Inspection");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "File pii.txt download has risk(s) - PII, ContentIQ Violations");
		expectedDataMap.put(GatewayTestConstants.RISK, "PII, ContentIQ Violations");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "critical");
		expectedDataMap.put("content_checks", "CIQ_FE_GW_DontDelete");
		Thread.sleep(150000);
		policy.deletePolicy(GatewayTestConstants.CIQ_DOWNLOAD_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData));

		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("==================================================================================");
		Logger.info("Verifying the CIQ risk file download event -successful");
		Logger.info("==================================================================================");
	}
	@Priority(8)
	@Test(groups ={"REACH","P1"})  
	public void ValidateFolderShare() throws Exception {
		fromTime=backend.getCurrentTime();
		Logger.info("==================================================================================");
		Logger.info("Verifying the Folder Share event");
		Logger.info("==================================================================================");
		String fileName=GatewayTestConstants.FOLDER_SHARE;
		dba.CreateFolder(getWebDriver(), fileName);
		Thread.sleep(30000);
		dba.publicShare(getWebDriver(),  fileName);
		//dba.deleteFile(getWebDriver(),"GTEST" );
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Share");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User clicked on get link for "+fileName); 
		expectedDataMap.put(GatewayTestConstants.ACCOUNT_TYPE,suiteData.getAccountType());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		expectedDataMap.put(GatewayTestConstants.ELASTICA_USER, suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.IS_ANONYMOUS_PROXY, "false");
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File/Folder");
		expectedDataMap.put(GatewayTestConstants.REQ_URI, GatewayTestConstants.DROPBOX_REQ_URI_SHARE);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getSaasAppUsername());
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("==================================================================================");
		Logger.info("Verifying the folder share event -successful");
		Logger.info("==================================================================================");
	}
	@Priority(9)
	@Test(groups ={"REACH","P1"})  
	public void ValidateFolderUnshare() throws Exception {
		fromTime=backend.getCurrentTime();
		Logger.info("==================================================================================");
		Logger.info("Verifying the Folder Unshare  event");
		Logger.info("==================================================================================");
		String fileName=GatewayTestConstants.FOLDER_SHARE;
		//dba.CreateFolder(getWebDriver(), fileName);
		dba.publicUnshare(getWebDriver(),  fileName);
		dba.deleteFile(getWebDriver(),fileName );
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Unshare");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User removed link for "+fileName); 
		expectedDataMap.put(GatewayTestConstants.ACCOUNT_TYPE,suiteData.getAccountType());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		expectedDataMap.put(GatewayTestConstants.ELASTICA_USER, suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.IS_ANONYMOUS_PROXY, "false");
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Link");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getSaasAppUsername());
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("==================================================================================");
		Logger.info("Verifying the Folder Unshare event -successful");
		Logger.info("==================================================================================");
	}

	
	@Priority(6)
	@Test(groups ={"SANITY", "REACH","CIQ","P1"})  
	public void ValidateCIQUpload() throws Exception {
		fromTime=backend.getCurrentTime();
		Logger.info("==================================================================================");
		Logger.info("Verifying the CIQ risk file upload event");
		Logger.info("==================================================================================");	
		printCredentials();
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData));
		utils.createCIQProfile( CIQConstants.ZERO,suiteData, "CIQ_FE_GW_DontDelete","CIQ_FE_GW_DontDelete",CIQConstants.SOURCE_DCI,CIQConstants.DEFAUTRISKVALUE);
		policy.createCIQPolicy(GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData),"CIQ_FE_GW_DontDelete","upload");
		Logger.info("==================================================================================");
		Logger.info("Perfroming dropbox SaasApp file upload operation");
		Logger.info("==================================================================================");
		String fileName=GatewayTestConstants.CIQ_FILE;
		String path=System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+
				"resources"+File.separator+"ciq"+File.separator;

		dba.uploadFile(getWebDriver(), path + fileName );
		dba.hardWait(15);
		Logger.info("==================================================================================");
		Logger.info("Completed dropbox SaasApp file upload operation");
		Logger.info("==================================================================================");
		Thread.sleep(5000);
		
		// delete policy after user
		Thread.sleep(150000);
		policy.deletePolicy(GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData));
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Content Inspection");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "File pii.txt upload has risk(s) - PII, ContentIQ Violations");
		expectedDataMap.put(GatewayTestConstants.RISK, "PII, ContentIQ Violations");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "critical");
		expectedDataMap.put("content_checks", "CIQ_FE_GW_DontDelete");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");

		Logger.info("==================================================================================");
		Logger.info("Verifying the CIQ risk file upload event -successful");
		Logger.info("==================================================================================");
	}
	@Priority(5)
	@Test(groups ={"SANITY", "REACH","P1"})  
	public void ValidateUpload() throws Exception {
		fromTime=backend.getCurrentTime();
		Logger.info("==================================================================================");
		Logger.info("Verifying the file upload event");
		Logger.info("==================================================================================");	
		printCredentials();
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData));
		Logger.info("==================================================================================");
		Logger.info("Perfroming dropbox SaasApp file upload operation");
		Logger.info("==================================================================================");
		dba.uploadFile(getWebDriver(), GatewayTestConstants.GDRIVE_ORIGINAL_FILE_PATH + GatewayTestConstants.GDRIVE_UPLOAD_ORIGINAL_FILE );
		dba.hardWait(15);
		Logger.info("==================================================================================");
		Logger.info("Completed dropbox SaasApp file upload operation");
		Logger.info("==================================================================================");
		Thread.sleep(5000);
		dba.deleteFile(getWebDriver(),GatewayTestConstants.GDRIVE_UPLOAD_ORIGINAL_FILE );
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Upload");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User uploaded file "+GatewayTestConstants.GDRIVE_UPLOAD_ORIGINAL_FILE); 
		expectedDataMap.put(GatewayTestConstants.ACCOUNT_TYPE,suiteData.getAccountType());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		expectedDataMap.put(GatewayTestConstants.ELASTICA_USER, suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.IS_ANONYMOUS_PROXY, "false");
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File");
		expectedDataMap.put(GatewayTestConstants.REQ_URI, GatewayTestConstants.DROPBOX_REQ_URI_UPLOAD);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.FILE_SIZE, "58");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getSaasAppUsername());
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("==================================================================================");
		Logger.info("Verifying the file upload event -successful");
		Logger.info("==================================================================================");
	}
	
	
	
	@Priority(16)
	@Test(groups ={"SANITY", "REACH","P1"})  
	public void ValidateLogout() throws Exception {
		fromTime=backend.getCurrentTime();
		Logger.info("==================================================================================");
		Logger.info("Verifying the logout event");
		Logger.info("==================================================================================");		
		Logger.info("==================================================================================");
		Logger.info("Perfroming dropbox SaasApp logout operation");
		Logger.info("==================================================================================");
		dba.logout(getWebDriver());		
		Logger.info("==================================================================================");
		Logger.info("Completed dropbox SaasApp logout operation");
		Logger.info("==================================================================================");		
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Logout");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User logged out"); 
		expectedDataMap.put(GatewayTestConstants.ACCOUNT_TYPE,suiteData.getAccountType());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		expectedDataMap.put(GatewayTestConstants.ELASTICA_USER, suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.IS_ANONYMOUS_PROXY, "false");
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Session");
		expectedDataMap.put(GatewayTestConstants.RESP_CODE, "302");
		//expectedDataMap.put(GatewayTestConstants.REQ_URI, GatewayTestConstants.DROPBOX_REQ_URI_HOME);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getSaasAppUsername());
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("==================================================================================");
		Logger.info("Verifying the logout event -successful");
		Logger.info("==================================================================================");
	}
	
	@AfterTest
	public void cleanup() throws Exception {
		Reporter.log("inside cleanup method", true);
		dba.deleteFile(getWebDriver(),GatewayTestConstants.GDRIVE_UPLOAD_ORIGINAL_FILE );
	}
	
	public String getSaasAppUserName(){
		return suiteData.getSaasAppUsername().replaceAll("@", "_");
	}
	
	
	@Priority(5)
	//@Test(groups ={"SANITY", "MINI_SANITY", "UPLOAD1"})  
	public void ValidateDecryptDownload() throws Exception {
		fromTime=backend.getCurrentTime();
		Logger.info("==================================================================================");
		Logger.info("Verifying the file decrypt Download event");
		Logger.info("==================================================================================");
		printCredentials();
		String fileName=GatewayTestConstants.DROPBOX_DOWNLOAD_DECRYPTED_FILE;
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData));
		policy.createEnableEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData));
		Logger.info("==================================================================================");
		Logger.info("Perfroming dropbox SaasApp decrypt Download operation");
		Logger.info("==================================================================================");
		dba.download(getWebDriver(), fileName);
		Logger.info("==================================================================================");
		Logger.info("Completed dropbox SaasApp decrypt Download operation");
		Logger.info("==================================================================================");
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "File Decryption");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "File "+fileName+" decrypted on download for user "+suiteData.getTestUsername()); 
		expectedDataMap.put(GatewayTestConstants.ACCOUNT_TYPE,suiteData.getAccountType());
		expectedDataMap.put(GatewayTestConstants.POLICY_TYPE, GatewayTestConstants.FILE_TRANSFER_POLICY);
		expectedDataMap.put(GatewayTestConstants.FILE_SIZE, "60234");
		expectedDataMap.put(GatewayTestConstants.POLICY_ACTION, GatewayTestConstants.POLICY_ACTION_ALERT);
		expectedDataMap.put(GatewayTestConstants.ACTION_TAKEN, GatewayTestConstants.ACTION_TAKEN_DECRYPT );
		expectedDataMap.put(GatewayTestConstants.CRYPTO_KEY_VERSION, GatewayTestConstants.CRYPTO_KEY_VERSION_USED);
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		expectedDataMap.put(GatewayTestConstants.ELASTICA_USER, suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.IS_ANONYMOUS_PROXY, "false");
		expectedDataMap.put(GatewayTestConstants.REQ_URI, GatewayTestConstants.DROPBOX_REQ_URI_DOWNLOAD+"/"+fileName);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getSaasAppUsername());
		expectedDataMap.put(GatewayTestConstants.BROWSER, GatewayTestConstants.BROWSER_NAME);
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("==================================================================================");
		Logger.info("Verifying the file decryped download event -successful");
		Logger.info("==================================================================================");
	}
	
	

	@Priority(5)
	//@Test(groups ={"SANITY", "MINI_SANITY", "UPLOAD"})  
	public void ValidateEncryptUpload() throws Exception {
		fromTime=backend.getCurrentTime();
		Logger.info("==================================================================================");
		Logger.info("Verifying the encryped file upload event");
		Logger.info("==================================================================================");	
		printCredentials();
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData));
		policy.createEnableEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData));
		Logger.info("==================================================================================");
		Logger.info("Perfroming dropbox SaasApp encryped file upload operation");
		Logger.info("==================================================================================");
		dba.uploadFile(getWebDriver(), GatewayTestConstants.GDRIVE_ORIGINAL_FILE_PATH + GatewayTestConstants.GDRIVE_ORGINAL_FILE );
		Thread.sleep(5000);
		Logger.info("==================================================================================");
		Logger.info("Completed dropbox SaasApp encryped file upload operation");
		Logger.info("==================================================================================");
		dba.deleteFile(getWebDriver(),GatewayTestConstants.GDRIVE_ORGINAL_FILE+".eef" );
		expectedDataMap.clear();		
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "File "+GatewayTestConstants.GDRIVE_ORGINAL_FILE +" encrypted on upload for user "+suiteData.getSaasAppUsername()+".");
		expectedDataMap.put(GatewayTestConstants.POLICY_ACTION, GatewayTestConstants.POLICY_ACTION_ALERT);
		expectedDataMap.put(GatewayTestConstants.ACTION_TAKEN, GatewayTestConstants.ACTION_TAKEN_ENCRYPT );
		expectedDataMap.put(GatewayTestConstants.CRYPTO_KEY_VERSION, GatewayTestConstants.CRYPTO_KEY_VERSION_USED);
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		expectedDataMap.put(GatewayTestConstants.ELASTICA_USER, suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.IS_ANONYMOUS_PROXY, "false");
		expectedDataMap.put(GatewayTestConstants.FILE_SIZE, "60030");
		expectedDataMap.put(GatewayTestConstants.REQ_URI, GatewayTestConstants.DROPBOX_REQ_URI_UPLOAD);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getSaasAppUsername());
		expectedDataMap.put(GatewayTestConstants.BROWSER, GatewayTestConstants.BROWSER_NAME);
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Expected log: "+expectedDataMap.get(GatewayTestConstants.MESSAGE)+" does not exist");
		
		Logger.info("==================================================================================");
		Logger.info("Verifying the encryped file upload event -successful");
		Logger.info("==================================================================================");
	}
}
