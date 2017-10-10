package com.elastica.tests.google;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.NameValuePair;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.elastica.common.GWCommonTest;
import com.elastica.gateway.GWCommonUtils;
import com.elastica.gateway.GatewayTestConstants;
import com.elastica.listeners.Priority;
import com.elastica.logger.Logger;
import com.elastica.restClient.Client;
import com.elastica.action.infra.InfraUtils;

public class GDriveTest_Large_Files extends GWCommonTest{
	Map <String, Object> expectedDataMap = new HashMap<String, Object>();
	String fromTime=backend.getCurrentTime();
	String title;
	List<NameValuePair> headers;
	String blockUser = "Block user";
	String userId = null;
	protected Client restClient=new Client();
	InfraUtils Infrautils= new InfraUtils();

	@Priority(1)
	@Test(groups ={"Regression_Large_File", "Regression_Large_File1"})
	public void gDriveOperations() throws Exception {
		fromTime=backend.getCurrentTime();
		Reporter.log("Starting performing activities on GDrive saas app", true);
		login.loginCloudSocPortal(getWebDriver(), suiteData);
		//gda.login(getWebDriver(), suiteData);
		Reporter.log("Finished performing activities on GDrive saas app", true);
	}

	
	@Priority(2)
	@Test(groups ={"Regression_Large_File","Regression_Large_File1", "REACH"})  
	public void gDrive_Test_ValidateLoging() throws Exception {
		Reporter.log("Starting performing activities on GDrive saas app", true);
		gda.login(getWebDriver(), suiteData);
		
		Reporter.log("Login", true);
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.remove(GatewayTestConstants.FACILITY);
		expectedDataMap.put(GatewayTestConstants.ACCOUNT_TYPE,suiteData.getAccountType());
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Login");
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		expectedDataMap.put(GatewayTestConstants.ELASTICA_USER, suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.IS_ANONYMOUS_PROXY, "false");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User logged in"); 
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Session");
		//expectedDataMap.put(GatewayTestConstants.REQ_URI, GatewayTestConstants.GDRIVE_REQ_URI_LOGIN);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		//expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("Login Event Verification Successfull", true);
		Reporter.log("Completed Login", true);
		Reporter.log("Finished performing activities on GDrive saas app", true);
	}

	@Priority(3)
	@Test(groups ={ "Regression_Large_File", "REACH"})
	public void gDrive_Test_Download_6MBNormal() throws Exception {
		Reporter.log("Download Normal 6MB File", true);
		gda.homepage(getWebDriver(), suiteData);
		gda.download(getWebDriver(), GatewayTestConstants.GDRIVE_DOWNLOAD_FILE_6MB);
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Download");
		expectedDataMap.put(GatewayTestConstants.IS_ANONYMOUS_PROXY, "false");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User downloaded contents of file " + GatewayTestConstants.GDRIVE_DOWNLOAD_FILE_6MB + ".");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("Completed Download Normal 6MB File", true);
	}
	
	@Priority(4)
	@Test(groups ={"Regression_Large_File", "REACH"})
	public void gDrive_Test_Download_10MBNormal() throws Exception {
		Reporter.log("Download Normal 10MB File", true);
		gda.homepage(getWebDriver(), suiteData);
		expectedDataMap.clear();
		gda.download(getWebDriver(), GatewayTestConstants.GDRIVE_DOWNLOAD_FILE_10MB);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Download");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User downloaded contents of file " + GatewayTestConstants.GDRIVE_DOWNLOAD_FILE_10MB + ".");
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("Completed Download Normal 6MB File", true);
	}
	
	@Priority(5)
	@Test(groups ={"Regression_Large_File", "REACH"})
	public void gDrive_Test_Download_20MBNormal() throws Exception {
		Reporter.log("Download Normal 20MB File", true);
		gda.homepage(getWebDriver(), suiteData);
		expectedDataMap.clear();
		gda.download(getWebDriver(), GatewayTestConstants.GDRIVE_DOWNLOAD_FILE_20MB);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Download");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User downloaded contents of file " + GatewayTestConstants.GDRIVE_DOWNLOAD_FILE_20MB + ".");
		// Activate  Policy for decryption
		policy.createEnableEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME + suiteData.getSaasAppName(), suiteData, backend.getGWHeaders(suiteData));
		policy.activateEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME + suiteData.getSaasAppName(), suiteData, backend.getGWHeaders(suiteData));
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("Download Normal 20MB File", true);
	}
	
	@Priority(7)
	@Test(groups ={"Regression_Large_File", "REACH"})
	public void gDrive_Test_Download_Decryption_For6MB() throws Exception {
		Reporter.log("Download Decrypted 6MB File", true);
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME + suiteData.getSaasAppName(), suiteData, backend.getGWHeaders(suiteData));
		policy.createEnableEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME+ suiteData.getSaasAppName(), suiteData, backend.getGWHeaders(suiteData));
		expectedDataMap.clear();
		gda.homepage(getWebDriver(), suiteData);
		gda.download(getWebDriver(), GatewayTestConstants.GDRIVE_DECRYPT_FILE_6MB);
		
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.ACCOUNT_TYPE, suiteData.getAccountType());
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "File Decryption");
		expectedDataMap.put(GatewayTestConstants.IS_ANONYMOUS_PROXY, "false");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "File " + 
				GatewayTestConstants.GDRIVE_DECRYPT_FILE_6MB +" decrypted on download for user " + suiteData.getSaasAppUsername() +".");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("Completed Download Decrypted 6MB File", true);
	}
		
	@Priority(8)
	@Test(groups ={"Regression_Large_File", "REACH"})
	public void gDrive_Test_Download_Decryption_For10MB() throws Exception {
		Reporter.log("Download Decrypted 10MB File", true);
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME + suiteData.getSaasAppName(), suiteData, backend.getGWHeaders(suiteData));
		policy.createEnableEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME+ suiteData.getSaasAppName(), suiteData, backend.getGWHeaders(suiteData));
		gda.homepage(getWebDriver(), suiteData);
		gda.download(getWebDriver(), GatewayTestConstants.GDRIVE_DECRYPT_FILE_10MB);
		
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.ACCOUNT_TYPE, suiteData.getAccountType());
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "File Decryption");
		expectedDataMap.put(GatewayTestConstants.IS_ANONYMOUS_PROXY, "false");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "File " + 
				GatewayTestConstants.GDRIVE_DECRYPT_FILE_10MB +" decrypted on download for user " + suiteData.getSaasAppUsername() +".");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		
		Reporter.log("Completed Download Decrypted 6MB File", true);
	}
	
	@Priority(9)
	@Test(groups ={"Regression_Large_File", "REACH"})
	public void gDrive_Test_Download_Decryption_For20MB() throws Exception {
		Reporter.log("Download Decrypted 20MB File", true);
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME + suiteData.getSaasAppName(), suiteData, backend.getGWHeaders(suiteData));
		policy.createEnableEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME+ suiteData.getSaasAppName(), suiteData, backend.getGWHeaders(suiteData));
		gda.homepage(getWebDriver(), suiteData);
		gda.download(getWebDriver(), GatewayTestConstants.GDRIVE_DECRYPT_FILE_20MB);
		
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.ACCOUNT_TYPE, suiteData.getAccountType());
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "File Decryption");
		expectedDataMap.put(GatewayTestConstants.IS_ANONYMOUS_PROXY, "false");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "File " + 
				GatewayTestConstants.GDRIVE_DECRYPT_FILE_20MB +" decrypted on download for user " + suiteData.getSaasAppUsername() +".");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		
		Reporter.log("Completed Download Decrypted 6MB File", true);
	}
	
	@Priority(10)
	@Test(groups ={"Regression_Large_File", "REACH"})
	public void gDrive_Test_Upload_For6MB() throws Exception {
		fromTime=backend.getCurrentTime();
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME + suiteData.getSaasAppName(), suiteData, backend.getGWHeaders(suiteData));
		Logger.info("==================================================================================");
		Logger.info("Upload File");
		Logger.info("==================================================================================");
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME+ suiteData.getSaasAppName(), suiteData, backend.getHeaders(suiteData));
		gda.homepage(getWebDriver(), suiteData);
		gda.selectFolder(getWebDriver(), "sanity");
		gda.deleteFile(getWebDriver(), GatewayTestConstants.GDRIVE_UPLOAD_FILE_6MB);
		//gda.uploadFile(getWebDriver(), System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+
		//		"resources"+File.separator+"meta"+File.separator + GatewayTestConstants.GDRIVE_UPLOAD_FILE_6MB);
		gda.clickUploadFile(getWebDriver());
		uploadSingleFile(System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+
						"resources"+File.separator+"meta"+File.separator + GatewayTestConstants.GDRIVE_UPLOAD_FILE_6MB, suiteData);
		
		expectedDataMap.clear(); 
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Upload");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User uploaded file " + GatewayTestConstants.GDRIVE_UPLOAD_FILE_6MB); 
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File");
		expectedDataMap.put(GatewayTestConstants.FILE_SIZE, GWCommonUtils.getFileSize(GatewayTestConstants.GDRIVE_UPLOAD_FILE_6MB));
		expectedDataMap.put(GatewayTestConstants.OBJECT_NAME, GatewayTestConstants.GDRIVE_UPLOAD_FILE_6MB);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("==================================================================================");
		Logger.info("Upload File Successful");
		Logger.info("==================================================================================");
	}
	
	@Priority(11)
	@Test(groups ={"Regression_Large_File", "REACH"})
	public void gDrive_Test_Upload_For10MB() throws Exception {
		fromTime=backend.getCurrentTime();
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME + suiteData.getSaasAppName(), suiteData, backend.getGWHeaders(suiteData));
		Logger.info("==================================================================================");
		Logger.info("Upload File");
		Logger.info("==================================================================================");
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME+ suiteData.getSaasAppName(), suiteData, backend.getHeaders(suiteData));
		gda.homepage(getWebDriver(), suiteData);
		gda.selectFolder(getWebDriver(), "sanity");
		gda.deleteFile(getWebDriver(), GatewayTestConstants.GDRIVE_UPLOAD_FILE_10MB);
//		gda.uploadFile(getWebDriver(), System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+
//				"resources"+File.separator+"meta"+File.separator + GatewayTestConstants.GDRIVE_UPLOAD_FILE_10MB);
		gda.clickUploadFile(getWebDriver());
		uploadSingleFile(System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+
						"resources"+File.separator+"meta"+File.separator + GatewayTestConstants.GDRIVE_UPLOAD_FILE_10MB, suiteData);

		expectedDataMap.clear(); 
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Upload");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User uploaded file " + GatewayTestConstants.GDRIVE_UPLOAD_FILE_10MB); 
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File");
		expectedDataMap.put(GatewayTestConstants.FILE_SIZE, GWCommonUtils.getFileSize(GatewayTestConstants.GDRIVE_UPLOAD_FILE_10MB));
		expectedDataMap.put(GatewayTestConstants.OBJECT_NAME, GatewayTestConstants.GDRIVE_UPLOAD_FILE_10MB);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("==================================================================================");
		Logger.info("Upload File Successful");
		Logger.info("==================================================================================");
	}
	
	@Priority(12)
	@Test(groups ={"Regression_Large_File", "REACH"})
	public void gDrive_Test_Upload_For20MB() throws Exception {
		fromTime=backend.getCurrentTime();
		Logger.info("==================================================================================");
		Logger.info("Upload File");
		Logger.info("==================================================================================");
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME+ suiteData.getSaasAppName(), suiteData, backend.getHeaders(suiteData));
		gda.homepage(getWebDriver(), suiteData);
		gda.selectFolder(getWebDriver(), "sanity");
		gda.deleteFile(getWebDriver(), GatewayTestConstants.GDRIVE_UPLOAD_FILE_20MB);
//		gda.uploadFile(getWebDriver(), System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+
//				"resources"+File.separator+"meta"+File.separator + GatewayTestConstants.GDRIVE_UPLOAD_FILE_20MB);
		gda.clickUploadFile(getWebDriver());
		uploadSingleFile(System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+
						"resources"+File.separator+"meta"+File.separator + GatewayTestConstants.GDRIVE_UPLOAD_FILE_20MB, suiteData);
		gda.hardWait(20);
		expectedDataMap.clear(); 
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Upload");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User uploaded file " + GatewayTestConstants.GDRIVE_UPLOAD_FILE_20MB); 
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File");
		expectedDataMap.put(GatewayTestConstants.FILE_SIZE, GWCommonUtils.getFileSize(GatewayTestConstants.GDRIVE_UPLOAD_FILE_20MB));
		expectedDataMap.put(GatewayTestConstants.OBJECT_NAME, GatewayTestConstants.GDRIVE_UPLOAD_FILE_20MB);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("==================================================================================");
		Logger.info("Upload File Successful");
		Logger.info("==================================================================================");
	}
	
	@Priority(13)
	@Test(groups ={"Regression_Large_File", "REACH"})
	public void gDrive_Test_Upload_Encrypt_For6MB() throws Exception {
		Logger.info("==================================================================================");
		Logger.info("Upload File " + GatewayTestConstants.GDRIVE_UPLOAD_FILE_6MB + " And Encrypt ");
		Logger.info("==================================================================================");
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME + suiteData.getSaasAppName(), suiteData, backend.getGWHeaders(suiteData));
		policy.createEnableEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME + suiteData.getSaasAppName(), suiteData, backend.getGWHeaders(suiteData));

		gda.homepage(getWebDriver(), suiteData);
		gda.selectFolder(getWebDriver(), "sanity");
		gda.deleteFile(getWebDriver(), GatewayTestConstants.GDRIVE_UPLOAD_FILE_6MB + ".eef");
		
//		gda.uploadFile(getWebDriver(), System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+
//				"resources"+File.separator+"meta"+File.separator + GatewayTestConstants.GDRIVE_UPLOAD_FILE_6MB);
		gda.clickUploadFile(getWebDriver());
		uploadSingleFile(System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+
						"resources"+File.separator+"meta"+File.separator + GatewayTestConstants.GDRIVE_UPLOAD_FILE_6MB, suiteData);

		gda.hardWait(20);
		expectedDataMap.clear(); 
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "File Encryption");
		expectedDataMap.put(GatewayTestConstants.ACTION_TAKEN, "encrypt");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, 
				"File " + GatewayTestConstants.GDRIVE_UPLOAD_FILE_6MB.toLowerCase() + " encrypted on upload for user " + suiteData.getSaasAppUsername()); 
		expectedDataMap.put(GatewayTestConstants.POLICY_TYPE, "FileTransfer");
		expectedDataMap.put(GatewayTestConstants.FILE_SIZE, GWCommonUtils.getFileSize(GatewayTestConstants.GDRIVE_UPLOAD_FILE_6MB));
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.remove(GatewayTestConstants.COUNTRY);
		expectedDataMap.remove(GatewayTestConstants.USER_NAME);
		
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME + suiteData.getSaasAppName(), suiteData, backend.getGWHeaders(suiteData));
		//gda.homepage(getWebDriver(), suiteData);
		if (!gda.isExist(getWebDriver(), GatewayTestConstants.GDRIVE_UPLOAD_FILE_6MB +".eef")) {
			Assert.assertTrue(false, "File " + GatewayTestConstants.GDRIVE_UPLOAD_FILE_6MB + ".eef not found in "+ suiteData.getSaasAppName() + ".");
		}
		Logger.info("==================================================================================");
		Logger.info("Upload File And Encrypt Successful");
		Logger.info("==================================================================================");
	}
	
	@Priority(14)
	@Test(groups ={"Regression_Large_File", "REACH"})
	public void gDrive_Test_Upload_Encrypt_For10MB() throws Exception {
		Logger.info("==================================================================================");
		Logger.info("Upload File " + GatewayTestConstants.GDRIVE_UPLOAD_FILE_10MB + " And Encrypt ");
		Logger.info("==================================================================================");
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME + suiteData.getSaasAppName(), suiteData, backend.getGWHeaders(suiteData));
		policy.createEnableEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME + suiteData.getSaasAppName(), suiteData, backend.getGWHeaders(suiteData));

		gda.homepage(getWebDriver(), suiteData);
		gda.selectFolder(getWebDriver(), "sanity");
		gda.deleteFile(getWebDriver(), GatewayTestConstants.GDRIVE_UPLOAD_FILE_10MB + ".eef");
//		gda.uploadFile(getWebDriver(), System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+
//				"resources"+File.separator+"meta"+File.separator + GatewayTestConstants.GDRIVE_UPLOAD_FILE_10MB);
		gda.clickUploadFile(getWebDriver());
		uploadSingleFile(System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+
						"resources"+File.separator+"meta"+File.separator + GatewayTestConstants.GDRIVE_UPLOAD_FILE_10MB, suiteData);

		gda.hardWait(20);
		expectedDataMap.clear(); 
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "File Encryption");
		expectedDataMap.put(GatewayTestConstants.ACTION_TAKEN, "encrypt");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, 
				"File " + GatewayTestConstants.GDRIVE_UPLOAD_FILE_10MB.toLowerCase() + " encrypted on upload for user " + suiteData.getSaasAppUsername()); 
		expectedDataMap.put(GatewayTestConstants.POLICY_TYPE, "FileTransfer");
		expectedDataMap.put(GatewayTestConstants.FILE_SIZE, GWCommonUtils.getFileSize(GatewayTestConstants.GDRIVE_UPLOAD_FILE_10MB));
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.remove(GatewayTestConstants.COUNTRY);
		expectedDataMap.remove(GatewayTestConstants.USER_NAME);
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.GDRIVE_UPLOAD_FILE_10MB + suiteData.getSaasAppName(), suiteData, backend.getGWHeaders(suiteData));
		
		//gda.homepage(getWebDriver(), suiteData);
		if (!gda.isExist(getWebDriver(), GatewayTestConstants.GDRIVE_UPLOAD_FILE_10MB +".eef")) {
			Assert.assertTrue(false, "File " + GatewayTestConstants.GDRIVE_UPLOAD_FILE_10MB + ".eef not found in "+ suiteData.getSaasAppName() + ".");
		}
		Logger.info("==================================================================================");
		Logger.info("Upload File And Encrypt Successful");
		Logger.info("==================================================================================");
	}
	
	@Priority(15)
	@Test(groups ={"Regression_Large_File", "REACH"})
	public void gDrive_Test_Upload_Encrypt_For20MB() throws Exception {
		Logger.info("==================================================================================");
		Logger.info("Upload File " + GatewayTestConstants.GDRIVE_UPLOAD_FILE_20MB  + " And Encrypt ");
		Logger.info("==================================================================================");
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME + suiteData.getSaasAppName(), suiteData, backend.getGWHeaders(suiteData));
		policy.createEnableEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME + suiteData.getSaasAppName(), suiteData, backend.getGWHeaders(suiteData));
		gda.homepage(getWebDriver(), suiteData);
		gda.selectFolder(getWebDriver(), "sanity");
		gda.deleteFile(getWebDriver(), GatewayTestConstants.GDRIVE_UPLOAD_FILE_20MB + ".eef");
//		gda.uploadFile(getWebDriver(), System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+
//				"resources"+File.separator+"meta"+File.separator + GatewayTestConstants.GDRIVE_UPLOAD_FILE_20MB);
		gda.clickUploadFile(getWebDriver());
		uploadSingleFile(System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+
						"resources"+File.separator+"meta"+File.separator + GatewayTestConstants.GDRIVE_UPLOAD_FILE_20MB, suiteData);

		gda.hardWait(30);
		expectedDataMap.clear(); 
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "File Encryption");
		expectedDataMap.put(GatewayTestConstants.ACTION_TAKEN, "encrypt");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, 
				"File " + GatewayTestConstants.GDRIVE_UPLOAD_FILE_20MB.toLowerCase() + " encrypted on upload for user " + suiteData.getSaasAppUsername()); 
		expectedDataMap.put(GatewayTestConstants.POLICY_TYPE, "FileTransfer");
		expectedDataMap.put(GatewayTestConstants.FILE_SIZE, GWCommonUtils.getFileSize(GatewayTestConstants.GDRIVE_UPLOAD_FILE_20MB));
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.remove(GatewayTestConstants.COUNTRY);
		expectedDataMap.remove(GatewayTestConstants.USER_NAME);
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.GDRIVE_UPLOAD_FILE_20MB + suiteData.getSaasAppName(), suiteData, backend.getGWHeaders(suiteData));
		
		//gda.homepage(getWebDriver(), suiteData);
		//gda.selectFolder(getWebDriver(), "sanity");
		if (!gda.isExist(getWebDriver(), GatewayTestConstants.GDRIVE_UPLOAD_FILE_20MB +".eef")) {
			Assert.assertTrue(false, "File " + GatewayTestConstants.GDRIVE_UPLOAD_FILE_20MB + ".eef not found in "+ suiteData.getSaasAppName() + ".");
		}
		Logger.info("==================================================================================");
		Logger.info("Upload File And Encrypt Successful");
		Logger.info("==================================================================================");
	}
	
	@BeforeClass(groups ={"Regression_Large_File", "REACH"})
	public void doBeforeClass() throws Exception {
		Logger.info("Delete Policy Before Test ");
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME + suiteData.getSaasAppName(), 
				suiteData, backend.getGWHeaders(suiteData));
	}
	
	@AfterClass(groups ={"Regression_Large_File", "REACH"})
	public void doAfterClass() throws Exception {
		Logger.info("Delete Policy After Test ");
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME + suiteData.getSaasAppName(),
				suiteData, backend.getGWHeaders(suiteData));
	}
	
}