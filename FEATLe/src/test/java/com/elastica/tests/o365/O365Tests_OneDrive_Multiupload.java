package com.elastica.tests.o365;

import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.elastica.action.protect.ProtectAction;
import com.elastica.action.protect.ProtectDTO;
import com.elastica.common.GWCommonTest;
import com.elastica.gateway.GatewayTestConstants;
import com.elastica.listeners.Priority;
import com.elastica.listeners.RetryAnalyzer;
import com.elastica.logger.Logger;

public class O365Tests_OneDrive_Multiupload extends GWCommonTest{
	Map <String, Object> expectedDataMap = new HashMap<String, Object>();
	Map<String, String>policyDataMap= new HashMap<String, String>(); 
	String fromTime=backend.getCurrentTime();
	ProtectAction protectAction = new ProtectAction();
	ProtectDTO protectData = new ProtectDTO();
	
	
	@BeforeMethod()
	public void clearDataMap(){
		expectedDataMap.clear();
	}

	@Priority(1)
	@Test(groups ={"Regression"}, retryAnalyzer=RetryAnalyzer.class)
	public void performingActivitiesOnSaasAppo365() throws Exception {
		fromTime=backend.getCurrentTime();
		printCredentials();
		Logger.info("Started performing activities on O365 Powerpoint and Word");
		login.loginCloudSocPortal(getWebDriver(), suiteData);
		Logger.info("Finished login activities on cloudSoc");
	}
	
	@Priority(2)
	@Test(groups ={"Regression", "REACH"},retryAnalyzer=RetryAnalyzer.class)  //
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
	@Test(groups ={"Regression", "REACH"}) 
	public void o365_OneDrive_Multiple_Uplaod() throws Exception {
		fromTime=backend.getCurrentTime();
		Logger.info("Upload multi file");
		printCredentials();
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.loadOnedriveApp(getWebDriver());
		o365HomeAction.deleteFile(getWebDriver(), GatewayTestConstants.O365_MULTI_UPLOAD_FILE1);
		o365HomeAction.deleteFile(getWebDriver(), GatewayTestConstants.O365_MULTI_UPLOAD_FILE2);
		o365HomeAction.deleteFile(getWebDriver(), GatewayTestConstants.O365_MULTI_UPLOAD_FILE3);

		o365HomeAction.uploadItemFileClick(getWebDriver());
		uploadMultipleFiles_Firefox(GatewayTestConstants.MULTIPLE_FILE_PATH);
		
		expectedDataMap.clear();
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
	
	public String getSaasAppUserName(){
		return suiteData.getSaasAppUsername().replaceAll("@", "_");
	}
	
	
	@BeforeClass(groups ={"Regression"})
	public void doBeforeClass() throws Exception {
		try {
			Logger.info("Delete Policy Before Test ");
			protectData.setPolicyName(GatewayTestConstants.O365_POLICY_NAME + getSaasAppUserName());
			protectAction.deletePolicy(client, suiteData, GatewayTestConstants.O365_POLICY_NAME + getSaasAppUserName());
			Logger.info(" policy to delete");
		} catch(Exception e) {
			Logger.info("No policy to delete");
		}
	}
	
	@AfterClass(groups ={"Regression"})
	public void doAfterClass() throws Exception {
		try {
			Logger.info("Delete Policy After Test ");
			protectData.setPolicyName(GatewayTestConstants.O365_POLICY_NAME + getSaasAppUserName());
			protectAction.deletePolicy(client, suiteData, GatewayTestConstants.O365_POLICY_NAME + getSaasAppUserName());
			Logger.info(" policy to delete");
		} catch(Exception e) {
			Logger.info("No policy to delete");
		}
	}
}
