package com.elastica.tests.google;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import com.elastica.action.infra.InfraUtils;
import com.elastica.common.GWCommonTest;
import com.elastica.gateway.GWCommonUtils;
import com.elastica.gateway.GatewayTestConstants;
import com.elastica.listeners.Priority;
import com.elastica.logger.Logger;
import com.elastica.restClient.Client;

public class GDriveMultiUploadTests extends GWCommonTest{
	Map <String, Object> expectedDataMap = new HashMap<String, Object>();
	String fromTime=backend.getCurrentTime();
	String title;
	List<NameValuePair> headers;
	String blockUser = "Block user";
	String userId = null;
	protected Client restClient=new Client();
	InfraUtils Infrautils= new InfraUtils();


	@Priority(1)
	@Test(groups ={"Regression"})
	public void gDriveOperations() throws Exception {
		fromTime=backend.getCurrentTime();
		Reporter.log("Starting performing activities on GDrive saas app", true);
		login.loginCloudSocPortal(getWebDriver(), suiteData);
		gda.login(getWebDriver(), suiteData);
		Reporter.log("Finished performing activities on GDrive saas app", true);
	}

	@Priority(2)
	@Test(groups ={"Regression", "REACH"})  
	public void google_Test_Validate_Loging() throws Exception {
		Reporter.log("Login", true);
		gda.login(getWebDriver(), suiteData);
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
	@Test(groups ={"Regression", "REACH"})
	public void gDrive_Test_Multiple_Files_Upload() throws Exception {
		fromTime=backend.getCurrentTime();
		
		Logger.info("==================================================================================");
		Logger.info("Upload File");
		Logger.info("==================================================================================");
		gda.homepage(getWebDriver(), suiteData);
		gda.deleteFile(getWebDriver(), GatewayTestConstants.O365_MULTI_UPLOAD_FILE1);
		gda.deleteFile(getWebDriver(), GatewayTestConstants.O365_MULTI_UPLOAD_FILE2);
		gda.deleteFile(getWebDriver(), GatewayTestConstants.O365_MULTI_UPLOAD_FILE3);
		gda.clickUploadFile(getWebDriver());
		uploadMultipleFiles_Firefox(GatewayTestConstants.MULTIPLE_FILE_PATH);
		
		expectedDataMap.clear(); 
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Upload");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User uploaded file " + GatewayTestConstants.O365_MULTI_UPLOAD_FILE1); 
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File");
		expectedDataMap.put(GatewayTestConstants.FILE_SIZE, GWCommonUtils.getMultiFile(GatewayTestConstants.O365_MULTI_UPLOAD_FILE1));
		expectedDataMap.put(GatewayTestConstants.OBJECT_NAME, GatewayTestConstants.O365_MULTI_UPLOAD_FILE1);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("==================================================================================");
		Logger.info("Upload File Successful");
		Logger.info("==================================================================================");
	}
	
	@Priority(4)
	@Test(groups ={"Regression", "REACH"})
	public void gDrive_Test_Encrypt_Multiple_Files_Upload() throws Exception {
		fromTime=backend.getCurrentTime();
		Logger.info("==================================================================================");
		Logger.info("Upload File");
		Logger.info("==================================================================================");
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME + suiteData.getSaasAppName(), suiteData, backend.getGWHeaders(suiteData));
		policy.createEnableEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME+ suiteData.getSaasAppName(), suiteData, backend.getGWHeaders(suiteData));
		
		gda.homepage(getWebDriver(), suiteData);
		gda.deleteFile(getWebDriver(), GatewayTestConstants.O365_MULTI_UPLOAD_FILE1);
		gda.deleteFile(getWebDriver(), GatewayTestConstants.O365_MULTI_UPLOAD_FILE2);
		gda.deleteFile(getWebDriver(), GatewayTestConstants.O365_MULTI_UPLOAD_FILE3);
		gda.clickUploadFile(getWebDriver());
		uploadMultipleFiles_Firefox(GatewayTestConstants.MULTIPLE_FILE_PATH);
		
		expectedDataMap.clear(); 
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "File Encryption");
		expectedDataMap.put(GatewayTestConstants.ACTION_TAKEN, "encrypt");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, 
				"File " + GatewayTestConstants.O365_MULTI_UPLOAD_FILE1 + " encrypted on upload for user " + suiteData.getSaasAppUsername()); 
		expectedDataMap.put(GatewayTestConstants.POLICY_TYPE, "FileTransfer");
		expectedDataMap.put(GatewayTestConstants.FILE_SIZE, "78");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.remove(GatewayTestConstants.COUNTRY);
		expectedDataMap.remove(GatewayTestConstants.USER_NAME);
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME + suiteData.getSaasAppName(), suiteData, backend.getGWHeaders(suiteData));
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}

	@Priority(5)
	@Test(groups ={"Regression", "REACH"})
	public void gMail_Test_Multiple_Files_Upload() throws Exception {
		Logger.info("Verifying the login event");
		getWebDriver().get("https://mail.google.com");
		gda.sendMailWithAttachmentFileLocal(getWebDriver(), "Subject", "testuser1@gatewaybeatle.com");
		uploadMultipleFiles_Firefox(GatewayTestConstants.MULTIPLE_FILE_PATH);
		gda.sentEmail(getWebDriver());

		expectedDataMap.clear(); 
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.remove(GatewayTestConstants.FACILITY);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Upload");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User uploaded file " + GatewayTestConstants.O365_MULTI_UPLOAD_FILE1); 
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File");
		expectedDataMap.put(GatewayTestConstants.FILE_SIZE, GWCommonUtils.getMultiFile(GatewayTestConstants.O365_MULTI_UPLOAD_FILE1));
		expectedDataMap.put(GatewayTestConstants.OBJECT_NAME, GatewayTestConstants.O365_MULTI_UPLOAD_FILE1);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("==================================================================================");
		Logger.info("Upload File Successful");
		Logger.info("==================================================================================");
	}

}
