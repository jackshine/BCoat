package com.elastica.tests.google;

import static org.testng.Assert.assertTrue;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.elastica.common.GWCommonTest;
import com.elastica.gateway.GWCommonUtils;
import com.elastica.gateway.GatewayTestConstants;
import com.elastica.listeners.Priority;
import com.elastica.logger.Logger;

public class GDriveTests extends GWCommonTest {
	Map <String, Object> expectedDataMap = new HashMap<String, Object>();
	String fromTime=backend.getCurrentTime();
	String title;
	
	

	@Priority(1)
	@Test(groups ={"SANITY", "MINI_SANITY", "SANITY1", "EXTERNAL"})
	public void loginToCloudSocAppAndSetupSSO() throws Exception {
		printCredentials();
		fromTime=backend.getCurrentTime();
		Logger.info("==================================================================================");
		Logger.info("Login into CloudSoc");
		Logger.info("==================================================================================");
		login.loginCloudSocPortal(getWebDriver(), suiteData);
		Logger.info("==================================================================================");
		Logger.info("Loging into CloudSoc done");
		Logger.info("==================================================================================");
	}
	
	
	@Priority(2)
	@Test(groups ={"SANITY", "REACH_AGENT", "SANITY1", "EXTERNAL", "REACH", "MINI_SANITY", "REACH-EXTERNAL"})  
	public void gDrive_Test_001_ValidateLogin() throws Exception {
		printCredentials();
		fromTime=backend.getCurrentTime();
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
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("==================================================================================");
		Logger.info("Verify login successful");
		Logger.info("==================================================================================");
	}
	
	
	@Priority(3)
	@Test(groups ={"SANITY", "REACH_AGENT", "EXTERNAL", "REACH", "REACH-EXTERNAL"})
	public void gDrive_Test_002_Download_File() throws Exception {
		Logger.info("==================================================================================");
		Logger.info("Verifying the Download event");
		Logger.info("==================================================================================");
		printCredentials();
		fromTime=backend.getCurrentTime();
		gda.homepage(getWebDriver(), suiteData);
		expectedDataMap.clear();
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME + suiteData.getSaasAppName() , suiteData, backend.getHeaders(suiteData));
		gda.download(getWebDriver(), GatewayTestConstants.GDRIVE_UPLOAD_ORIGINAL_FILE);
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Download");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User downloaded contents of file " + GatewayTestConstants.GDRIVE_UPLOAD_ORIGINAL_FILE + ".");
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File");
		expectedDataMap.put(GatewayTestConstants.FILE_SIZE, "57");
		expectedDataMap.put(GatewayTestConstants.CONTENT_INLINE, "1");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("==================================================================================");
		Logger.info("Download File Event Verification Successfull");
		Logger.info("==================================================================================");
	}
	
	@Priority(4)
	@Test(groups ={"SANITY","REACH_AGENT", "REACH"})
	public void gDrive_Test_003_Download_Decryption() throws Exception {
		Logger.info("==================================================================================");
		Logger.info("Verifying the Download Decrypt");
		Logger.info("==================================================================================");
		printCredentials();
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME + suiteData.getSaasAppName(), suiteData, backend.getGWHeaders(suiteData));
		policy.createEnableEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME+ suiteData.getSaasAppName(), suiteData, backend.getGWHeaders(suiteData));
		gda.homepage(getWebDriver(), suiteData);
		//gda.selectFolder(getWebDriver(), "sanity");
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		gda.downloadWithIE(getWebDriver(), GatewayTestConstants.GDRIVE_DOWNLOAD_DECRYPTED_FILE, suiteData.getBrowser());
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Download");
		expectedDataMap.put(GatewayTestConstants.FILE_SIZE, "58");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User downloaded contents of file " +GatewayTestConstants.GDRIVE_DOWNLOAD_DECRYPTED_FILE+ ".");
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.remove(GatewayTestConstants.COUNTRY);
		expectedDataMap.remove(GatewayTestConstants.USER_NAME);
		//Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");

		Logger.info("==================================================================================");
		Logger.info("Download Decrypted File");
		Logger.info("==================================================================================");
		
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTION_TAKEN, "decrypt");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "File Decryption");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "File " + 
		GatewayTestConstants.GDRIVE_DOWNLOAD_DECRYPTED_FILE +" decrypted on download for user " + suiteData.getTestUsername() +".");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.remove(GatewayTestConstants.COUNTRY);
		expectedDataMap.remove(GatewayTestConstants.USER_NAME);
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME + suiteData.getSaasAppName(), suiteData, backend.getGWHeaders(suiteData));
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("==================================================================================");
		Logger.info("Alert Log Verification Successfull");
		Logger.info("==================================================================================");
	}
	
	@Priority(5)
	@Test(groups ={"SANITY","REACH_AGENT", "EXTERNAL", "REACH", "REACH-EXTERNAL"})
	public void gDrive_Test_004_Share_File_Email() throws Exception {
		Logger.info("==================================================================================");
		Logger.info("Share Email");
		Logger.info("==================================================================================");
		printCredentials();
		gda.homepage(getWebDriver(), suiteData);
		gda.selectFolder(getWebDriver(), "sanity");
		if (!gda.isExist(getWebDriver(), GatewayTestConstants.GDRIVE_FILE_EMAIL_LINK)) {
			//gda.deleteFile(getWebDriver(), GatewayTestConstants.GDRIVE_FILE_EMAIL_LINK);
			gda.createDocFile(getWebDriver(), GatewayTestConstants.GDRIVE_FILE_EMAIL_LINK);
		}
		//gda.homepage(getWebDriver(), suiteData);
		//gda.selectFolder(getWebDriver(), "sanity");
		gda.shareActivity(getWebDriver(), suiteData.getUsername(), GatewayTestConstants.GDRIVE_FILE_EMAIL_LINK);
		Logger.info("File Share");
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Share");
		expectedDataMap.put(GatewayTestConstants.FILE_TYPE_GENERIC, "");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User shared file(s)/folder(s) " + GatewayTestConstants.GDRIVE_FILE_EMAIL_LINK + " with QA Admin &lt;" + suiteData.getUsername()  +"&gt;");
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File/Folder");
		expectedDataMap.put(GatewayTestConstants.OBJECT_NAME, GatewayTestConstants.GDRIVE_FILE_EMAIL_LINK);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("==================================================================================");
		Logger.info("Completed Share Email ");
		Logger.info("==================================================================================");
	}
	
	@Priority(6)
	@Test(groups ={"SANITY","REACH_AGENT", "REACH"})
	public void gDrive_Test_005_Share_Via_Link() throws Exception {
		Logger.info("==================================================================================");
		Logger.info(" Share Link");
		Logger.info("==================================================================================");
		printCredentials();
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User turned on link sharing for file/folder " + GatewayTestConstants.GDRIVE_FILE_EMAIL_LINK);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Share");
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File/Folder");
		expectedDataMap.put(GatewayTestConstants.SHARE__WITH, "ALL_EL__");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("==================================================================================");
		Logger.info("Completed Share Link");
		Logger.info("==================================================================================");
	}
	
	@Priority(6)
	@Test(groups ={"EXTERNAL", "REACH-EXTERNAL"})
	public void gDrive_Test_005__Share_Via_Link() throws Exception {
		Logger.info("==================================================================================");
		Logger.info(" Share Link");
		Logger.info("==================================================================================");
		printCredentials();
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Update Link Sharing");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User has made the item " +GatewayTestConstants.GDRIVE_FILE_EMAIL_LINK +" accessible to Shared with 1 person");
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File/Folder");
		//expectedDataMap.put(GatewayTestConstants.SHARE__WITH, "ALL_EL__");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("==================================================================================");
		Logger.info("Completed Share Link");
		Logger.info("==================================================================================");
	}
	
	
	@Priority(7)
	@Test(groups ={"SANITY","REACH_AGENT", "EXTERNAL", "REACH", "REACH-EXTERNAL"})
	public void gDrive_Test_006_Upload_File_Normal() throws Exception {
		fromTime=backend.getCurrentTime();
		Logger.info("==================================================================================");
		Logger.info("Upload File");
		Logger.info("==================================================================================");
		printCredentials();
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME+ suiteData.getSaasAppName(), suiteData, backend.getHeaders(suiteData));
		//GWCommonUtils.getFileSize(GatewayTestConstants.GDRIVE_ORGINAL_FILE);
		gda.homepage(getWebDriver(), suiteData);
		//gda.selectFolder(getWebDriver(), "sanity");
		gda.deleteFile(getWebDriver(), GatewayTestConstants.GDRIVE_ORGINAL_FILE);
//		gda.uploadFile(getWebDriver(), GatewayTestConstants.UPLOAD_FILE_PATH+ GatewayTestConstants.GDRIVE_ORGINAL_FILE);
		gda.clickUploadFile(getWebDriver());
		uploadSingleFile(System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+
			"resources"+File.separator+"meta"+File.separator + GatewayTestConstants.GDRIVE_ORGINAL_FILE, suiteData);
		expectedDataMap.clear(); 
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Upload");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User uploaded file " + GatewayTestConstants.GDRIVE_ORGINAL_FILE); 
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File");
		expectedDataMap.put(GatewayTestConstants.FILE_SIZE, GWCommonUtils.getFileSize(GatewayTestConstants.GDRIVE_ORGINAL_FILE));
		expectedDataMap.put(GatewayTestConstants.OBJECT_NAME, GatewayTestConstants.GDRIVE_ORGINAL_FILE);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("==================================================================================");
		Logger.info("Upload File Successful");
		Logger.info("==================================================================================");
	}
	
	@Priority(8)
	@Test(groups ={"SANITY","REACH_AGENT", "EXTERNAL", "REACH", "REACH-EXTERNAL"})
	public void gDriveUploadFilesCIQ_PII() throws Exception {
		fromTime=backend.getCurrentTime();
		Logger.info("==================================================================================");
		Logger.info("Upload File");
		Logger.info("==================================================================================");
		printCredentials();
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME+ suiteData.getSaasAppName(), suiteData, backend.getHeaders(suiteData));
		policy.deletePolicy(GatewayTestConstants.CIQ_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData));
		policy.createCIQPolicy(GatewayTestConstants.CIQ_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData));
		gda.homepage(getWebDriver(), suiteData);
		//gda.selectFolder(getWebDriver(), "sanity");
		gda.deleteFile(getWebDriver(), GatewayTestConstants.CIQ_FILE);
//		gda.uploadFile(getWebDriver(), System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+
//				"resources"+File.separator+"ciq"+File.separator + GatewayTestConstants.CIQ_FILE);
		
		gda.clickUploadFile(getWebDriver());
		uploadSingleFile(System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+
			"resources"+File.separator+"ciq"+File.separator + GatewayTestConstants.CIQ_FILE, suiteData);
		
		expectedDataMap.clear(); 
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "File pii.txt upload has risk(s) - PII");
		gda.hardWait(50);
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("==================================================================================");
		Logger.info("Upload File Successful");
		Logger.info("==================================================================================");
	}
	
	
	@Priority(9)
	@Test(groups ={"SANITY","REACH_AGENT", "EXTERNAL", "REACH", "REACH-EXTERNAL"})
	public void gDrive_Test_007_Big_File_Upload_File_Normal() throws Exception {
		fromTime=backend.getCurrentTime();
		Logger.info("==================================================================================");
		Logger.info("Upload File");
		Logger.info("==================================================================================");
		printCredentials();
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME+ suiteData.getSaasAppName(), suiteData, backend.getHeaders(suiteData));
		gda.homepage(getWebDriver(), suiteData);
		//gda.selectFolder(getWebDriver(), "sanity");
		gda.deleteFile(getWebDriver(), GatewayTestConstants.GDRIVE_1MB_FILE_UPLOAD);
//		gda.uploadFile(getWebDriver(), System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+
//				"resources"+File.separator+"meta"+File.separator + GatewayTestConstants.GDRIVE_1MB_FILE_UPLOAD);
		gda.clickUploadFile(getWebDriver());
		uploadSingleFile(System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+
			"resources"+File.separator+"meta"+File.separator + GatewayTestConstants.GDRIVE_1MB_FILE_UPLOAD, suiteData);
		
		expectedDataMap.clear(); 
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Upload");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User uploaded file " + GatewayTestConstants.GDRIVE_1MB_FILE_UPLOAD); 
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File");
		expectedDataMap.put(GatewayTestConstants.FILE_SIZE, "1277");
		expectedDataMap.put(GatewayTestConstants.OBJECT_NAME, GatewayTestConstants.GDRIVE_1MB_FILE_UPLOAD);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("==================================================================================");
		Logger.info("Upload File Successful");
		Logger.info("==================================================================================");
	}
	
	@Priority(10)
	@Test(groups ={"SANITY","REACH_AGENT", "REACH"})
	public void gDrive_Test_008_Upload_And_Encrypt() throws Exception {
		Logger.info("==================================================================================");
		Logger.info("Upload File And Encrypt ");
		Logger.info("==================================================================================");
		printCredentials();
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME + suiteData.getSaasAppName(), suiteData, backend.getGWHeaders(suiteData));
		policy.createEnableEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME + suiteData.getSaasAppName(), suiteData, backend.getGWHeaders(suiteData));

		gda.homepage(getWebDriver(), suiteData);
		//gda.selectFolder(getWebDriver(), "sanity");
		gda.deleteFile(getWebDriver(), GatewayTestConstants.GDRIVE_ORGINAL_FILE_UPLOAD_ENCRPT + ".eef");
		
//		gda.uploadFile(getWebDriver(), System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+
//				"resources"+File.separator+"meta"+File.separator + GatewayTestConstants.GDRIVE_ORGINAL_FILE_UPLOAD_ENCRPT);
		gda.clickUploadFile(getWebDriver());
		uploadSingleFile(System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+
			"resources"+File.separator+"meta"+File.separator + GatewayTestConstants.GDRIVE_ORGINAL_FILE_UPLOAD_ENCRPT, suiteData);
		expectedDataMap.clear(); 
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "File Encryption");
		expectedDataMap.put(GatewayTestConstants.ACTION_TAKEN, "encrypt");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, 
				"File " + GatewayTestConstants.GDRIVE_ORGINAL_FILE_UPLOAD_ENCRPT + " encrypted on upload for user " + suiteData.getSaasAppUsername()); 
		expectedDataMap.put(GatewayTestConstants.POLICY_TYPE, "FileTransfer");
		expectedDataMap.put(GatewayTestConstants.FILE_SIZE, "78");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.remove(GatewayTestConstants.COUNTRY);
		expectedDataMap.remove(GatewayTestConstants.USER_NAME);
		gda.hardWait(15);
		gda.homepage(getWebDriver(), suiteData);
		gda.hardWait(10);
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME + suiteData.getSaasAppName(), suiteData, backend.getGWHeaders(suiteData));
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");//gda.selectFolder(getWebDriver(), "sanity");
		if (!gda.isExist(getWebDriver(), GatewayTestConstants.GDRIVE_ORGINAL_FILE_UPLOAD_ENCRPT +".eef")) {
			Assert.assertTrue(false, "File " + GatewayTestConstants.GDRIVE_ORGINAL_FILE_UPLOAD_ENCRPT + ".eef not found in "+ suiteData.getSaasAppName() + ".");
		}
		Logger.info("==================================================================================");
		Logger.info("Upload File And Encrypted Successful");
		Logger.info("==================================================================================");
	}
	
	@Priority(11)
	@Test(groups ={"SANITY", "REACH_AGENT", "P1_SANITY", "EXTERNAL", "REACH", "REACH-EXTERNAL"})
	public void gDrive_Test_009_Logout() throws Exception {
		Logger.info("==================================================================================");
		Logger.info("Verifying the logout event");
		Logger.info("==================================================================================");
		printCredentials();
		gda.logout(getWebDriver());
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.remove(GatewayTestConstants.FACILITY);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Logout");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User logged out");
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Session");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.REFERER_URI,"");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("==================================================================================");
		Logger.info("Logout Event verification successfull");
		Logger.info("==================================================================================");
	}
	
	
	
	@Priority(11)
//	@Test(groups ={"SPLUNK"})
	public void gDrive_Test_007_ValidateSplunkLog() throws Exception {
		assertTrue(backend.validateSplunkLog(suiteData), "Host is not found");
	}
	
	@BeforeClass(groups ={"SANITY", "P1_SANITY", "EXTERNAL", "REACH", "REACH-EXTERNAL"})
	public void doBeforeClass() throws Exception {
		Logger.info("Delete Policy Before Test ");
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME+ suiteData.getSaasAppName(), suiteData, backend.getGWHeaders(suiteData));
	}
	
	@AfterClass(groups ={"SANITY", "P1_SANITY", "EXTERNAL", "REACH", "REACH-EXTERNAL"})
	public void doAfterClass() throws Exception {
		Logger.info("Delete Policy After Test ");
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME+ suiteData.getSaasAppName(), suiteData, backend.getGWHeaders(suiteData));
	}
	
	public String getSaasAppUserName(){
		return suiteData.getSaasAppUsername().replaceAll("@", "_");
	}
	
	
}