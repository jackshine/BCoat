package com.elastica.tests.google;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import com.elastica.common.GWCommonTest;
import com.elastica.gateway.GWCommonUtils;
import com.elastica.gateway.GatewayTestConstants;
import com.elastica.listeners.Priority;
import com.elastica.logger.Logger;

public class GDriveTest_Upload_Big  extends GWCommonTest{
	Map <String, Object> expectedDataMap = new HashMap<String, Object>();
	String fromTime=backend.getCurrentTime();
	String filepath="D:\\largerfiles\\";

	@Priority(1)
	@Test(groups ={"Upload", "Download"})
	public void gDriveOperations() throws Exception {
		fromTime=backend.getCurrentTime();
		Reporter.log("Starting performing activities on GDrive saas app", true);
		login.loginCloudSocPortal(getWebDriver(), suiteData);
		gda.login(getWebDriver(), suiteData);
		Reporter.log("Finished performing activities on GDrive saas app", true);
	}

	@Priority(1)
	@Test(groups ={"REACH_UPLOAD", "REACH_DOWNLOAD"})
	public void gDriveOperations_Login() throws Exception {
		filepath="C:\\largerfiles\\";
		fromTime=backend.getCurrentTime();
		gda.login(getWebDriver(), suiteData);
		Reporter.log("Finished performing activities on GDrive saas app", true);
	}
	
	@Priority(2)
	@Test(groups ={"Upload", "Download", "REACH_UPLOAD", "REACH_DOWNLOAD"})  
	public void gDrive_Test_ValidateLoging() throws Exception {
		Reporter.log("Login", true);
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.ACCOUNT_TYPE,suiteData.getAccountType());
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Login");
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		expectedDataMap.put(GatewayTestConstants.ELASTICA_USER, suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.IS_ANONYMOUS_PROXY, "false");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User logged in"); 
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Session");
		//expectedDataMap.put(GatewayTestConstants.REQ_URI, GatewayTestConstants.GDRIVE_REQ_URI_LOGIN);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("Login Event Verification Successfull", true);
		Reporter.log("Completed Login", true);
	}
	
	@Priority(3)
	@Test(groups ={"Download", "REACH_DOWNLOAD"})
	public void gDrive_Test_Big_Files_01_Download_100MB() throws Exception {
		Logger.info("==================================================================================");
		Logger.info("Verifying the Download event");
		Logger.info("==================================================================================");
		fromTime=backend.getCurrentTime();
		gda.homepage(getWebDriver(), suiteData);
		expectedDataMap.clear();
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME + suiteData.getSaasAppName() , suiteData, backend.getHeaders(suiteData));
		gda.downloadBigFile(getWebDriver(), GatewayTestConstants.GDRIVE_DOWNLOAD_FILE_100MB);
		gda.hardWait(120);
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Download");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User downloaded contents of file " + GatewayTestConstants.GDRIVE_DOWNLOAD_FILE_100MB + ".");
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File");
		expectedDataMap.put(GatewayTestConstants.FILE_SIZE ,98224 );
		expectedDataMap.put(GatewayTestConstants.CONTENT_INLINE, "1");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("==================================================================================");
		Logger.info("Download File Event Verification Successfull");
		Logger.info("==================================================================================");
	}
	
	@Priority(4)
	@Test(groups ={"Download", "REACH_DOWNLOAD"})
	public void gDrive_Test_Big_Files_01_Download_200MB() throws Exception {
		Logger.info("==================================================================================");
		Logger.info("Verifying the Download event");
		Logger.info("==================================================================================");
		fromTime=backend.getCurrentTime();
		gda.homepage(getWebDriver(), suiteData);
		expectedDataMap.clear();
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME + suiteData.getSaasAppName() , suiteData, backend.getHeaders(suiteData));
		gda.downloadBigFile(getWebDriver(), GatewayTestConstants.GDRIVE_DOWNLOAD_FILE_200MB);
		gda.hardWait(120);
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Download");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User downloaded contents of file " + GatewayTestConstants.GDRIVE_DOWNLOAD_FILE_200MB + ".");
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File");
		expectedDataMap.put(GatewayTestConstants.FILE_SIZE, 196449);
		expectedDataMap.put(GatewayTestConstants.CONTENT_INLINE, "1");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("==================================================================================");
		Logger.info("Download File Event Verification Successfull");
		Logger.info("==================================================================================");
	}
	
	@Priority(5)
	@Test(groups ={"Download", "REACH_DOWNLOAD"})
	public void gDrive_Test_Big_Files_01_Download_500MB() throws Exception {
		Logger.info("==================================================================================");
		Logger.info("Verifying the Download event");
		Logger.info("==================================================================================");
		fromTime=backend.getCurrentTime();
		gda.homepage(getWebDriver(), suiteData);
		expectedDataMap.clear();
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME + suiteData.getSaasAppName() , suiteData, backend.getHeaders(suiteData));
		gda.downloadBigFile(getWebDriver(), GatewayTestConstants.GDRIVE_DOWNLOAD_FILE_500MB);
		gda.hardWait(120);
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Download");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User downloaded contents of file " + GatewayTestConstants.GDRIVE_DOWNLOAD_FILE_500MB + ".");
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File");
		expectedDataMap.put(GatewayTestConstants.FILE_SIZE, 589348);
		expectedDataMap.put(GatewayTestConstants.CONTENT_INLINE, "1");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("==================================================================================");
		Logger.info("Download File Event Verification Successfull");
		Logger.info("==================================================================================");
	}
	
	@Priority(6)
	@Test(groups ={"Upload", "REACH_UPLOAD"})
	public void gDrive_Test_Big_Files_Upload_Normal_100MB() throws Exception {
		fromTime=backend.getCurrentTime();
		Logger.info("==================================================================================");
		Logger.info("Upload File");
		Logger.info("==================================================================================");
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME+ suiteData.getSaasAppName(), 
				suiteData, backend.getHeaders(suiteData));
		gda.homepage(getWebDriver(), suiteData);
		gda.selectFolder(getWebDriver(), "sanity");
		gda.deleteFile(getWebDriver(), GatewayTestConstants.GDRIVE_UPLOAD_FILE_100MB);
		
//		gda.uploadFile(getWebDriver(),  filepath+GatewayTestConstants.GDRIVE_UPLOAD_FILE_100MB);
		gda.clickUploadFile(getWebDriver());
		uploadSingleFile(filepath+GatewayTestConstants.GDRIVE_UPLOAD_FILE_100MB, suiteData);

		gda.waitForFileUpload(getWebDriver(), GatewayTestConstants.GDRIVE_UPLOAD_FILE_100MB, 20);

		expectedDataMap.clear(); 
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Upload");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User uploaded file " + GatewayTestConstants.GDRIVE_UPLOAD_FILE_100MB); 
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File");
		expectedDataMap.put(GatewayTestConstants.FILE_SIZE, GWCommonUtils.getFileSizeBig(GatewayTestConstants.GDRIVE_UPLOAD_FILE_100MB));
		expectedDataMap.put(GatewayTestConstants.OBJECT_NAME, GatewayTestConstants.GDRIVE_UPLOAD_FILE_100MB);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		
		gda.homepage(getWebDriver(), suiteData);
		gda.selectFolder(getWebDriver(), "sanity");
		if (!gda.isExist(getWebDriver(), GatewayTestConstants.GDRIVE_UPLOAD_FILE_100MB)) {
			Assert.assertTrue(false, "File " + GatewayTestConstants.GDRIVE_UPLOAD_FILE_100MB + " not found in "+ suiteData.getSaasAppName() + ".");
		}
		
		
		Logger.info("==================================================================================");
		Logger.info("Upload File Successful");
		Logger.info("==================================================================================");
	}
	
	@Priority(7)
	@Test(groups ={"Upload", "REACH_UPLOAD"})
	public void gDrive_Test_Big_Files_Upload_Normal_200MB() throws Exception {
		fromTime=backend.getCurrentTime();
		Logger.info("==================================================================================");
		Logger.info("Upload File");
		Logger.info("==================================================================================");
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME+ suiteData.getSaasAppName(), 
				suiteData, backend.getHeaders(suiteData));
		gda.homepage(getWebDriver(), suiteData);
		gda.selectFolder(getWebDriver(), "sanity");
		gda.deleteFile(getWebDriver(),  GatewayTestConstants.GDRIVE_UPLOAD_FILE_200MB);
		
//		gda.uploadFile(getWebDriver(), filepath+GatewayTestConstants.GDRIVE_UPLOAD_FILE_200MB);
		
		gda.clickUploadFile(getWebDriver());
		uploadSingleFile(filepath+GatewayTestConstants.GDRIVE_UPLOAD_FILE_200MB, suiteData);

		
		gda.waitForFileUpload(getWebDriver(), GatewayTestConstants.GDRIVE_UPLOAD_FILE_200MB, 80);

		expectedDataMap.clear(); 
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Upload");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User uploaded file " +  GatewayTestConstants.GDRIVE_UPLOAD_FILE_200MB); 
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File");
		expectedDataMap.put(GatewayTestConstants.FILE_SIZE, GWCommonUtils.getFileSizeBig( GatewayTestConstants.GDRIVE_UPLOAD_FILE_200MB));
		expectedDataMap.put(GatewayTestConstants.OBJECT_NAME,  GatewayTestConstants.GDRIVE_UPLOAD_FILE_200MB);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		
		gda.homepage(getWebDriver(), suiteData);
		gda.selectFolder(getWebDriver(), "sanity");
		if (!gda.isExist(getWebDriver(), GatewayTestConstants.GDRIVE_UPLOAD_FILE_200MB)) {
			Assert.assertTrue(false, "File " + GatewayTestConstants.GDRIVE_UPLOAD_FILE_200MB + " not found in "+ suiteData.getSaasAppName() + ".");
		}
		
		Logger.info("==================================================================================");
		Logger.info("Upload File Successful");
		Logger.info("==================================================================================");
	}
	
	@Priority(8)
	@Test(groups ={"Upload", "REACH_UPLOAD"})
	public void gDrive_Test_Big_Files_Upload_Normal_500MB() throws Exception {
		fromTime=backend.getCurrentTime();
		Logger.info("==================================================================================");
		Logger.info("Upload File");
		Logger.info("==================================================================================");
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME+ suiteData.getSaasAppName(), 
				suiteData, backend.getHeaders(suiteData));
		gda.homepage(getWebDriver(), suiteData);
		gda.selectFolder(getWebDriver(), "sanity");
		gda.deleteFile(getWebDriver(), GatewayTestConstants.GDRIVE_UPLOAD_FILE_500MB);
//		gda.uploadFile(getWebDriver(), filepath+GatewayTestConstants.GDRIVE_UPLOAD_FILE_500MB);
		
		gda.clickUploadFile(getWebDriver());
		uploadSingleFile(filepath+GatewayTestConstants.GDRIVE_UPLOAD_FILE_500MB, suiteData);
		
		gda.waitForFileUpload(getWebDriver(), GatewayTestConstants.GDRIVE_UPLOAD_FILE_500MB, 100);
		expectedDataMap.clear(); 
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Upload");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User uploaded file " + GatewayTestConstants.GDRIVE_UPLOAD_FILE_500MB); 
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File");
		expectedDataMap.put(GatewayTestConstants.FILE_SIZE, GWCommonUtils.getFileSizeBig(GatewayTestConstants.GDRIVE_UPLOAD_FILE_500MB));
		expectedDataMap.put(GatewayTestConstants.OBJECT_NAME, GatewayTestConstants.GDRIVE_UPLOAD_FILE_500MB);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		
		gda.homepage(getWebDriver(), suiteData);
		gda.selectFolder(getWebDriver(), "sanity");
		if (!gda.isExist(getWebDriver(), GatewayTestConstants.GDRIVE_UPLOAD_FILE_500MB)) {
			Assert.assertTrue(false, "File " + GatewayTestConstants.GDRIVE_UPLOAD_FILE_500MB + " not found in "+ suiteData.getSaasAppName() + ".");
		}
		
		Logger.info("==================================================================================");
		Logger.info("Upload File Successful");
		Logger.info("==================================================================================");
	}
	

}