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

public class O365Testi18N extends GWCommonTest{
	//                          chi Jap Ger Rus Mex Por Fre Arb hin kor
	String differentLanguage = "你好_こん_iß_др_¡h_lá_on_ہیلو_नम_안녕";
	////String differentLanguage = "你好 你好";
	
	Map <String, Object> expectedDataMap = new HashMap<String, Object>();
	Map<String, String> policyDataMap= new HashMap<String, String>(); 
	String fromTime=backend.getCurrentTime();
	
	@BeforeMethod()
	public void clearDataMap(){
		fromTime=backend.getCurrentTime();
		expectedDataMap.clear();
	}

	@Priority(1)
	@Test(groups ={"Regression"})
	public void loginToCloudSocAppAndSetupSSO() throws Exception {
		fromTime=backend.getCurrentTime();
		printCredentials();
		Logger.info("Started performing activities on saas app");
		login.loginCloudSocPortal(getWebDriver(), suiteData);
		Logger.info("Finished login activities on cloudSoc");
	}
	
	@Priority(2)
	@Test(groups ={"Regression", "REACH"})  
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
	@Test(groups ={"Regression", "REACH"}, retryAnalyzer=RetryAnalyzer.class) 
	public void o365_Test_02_ValidateSendEmailWithLanguagues() throws Exception {
		Reporter.log("==================================================================================",true);
		Reporter.log(" Verifying email send event in Outlook", true);
		Reporter.log("==================================================================================",true);
		String subject = differentLanguage;
		printCredentials();
		fromTime=backend.getCurrentTime();
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.loadEmailApp(getWebDriver());
		o365HomeAction.sendEmail(getWebDriver(), subject);
		
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User sent an email to admin@gatewayO365beatle.com with subject " + subject);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Send");
		expectedDataMap.put(GatewayTestConstants.OBJECT_NAME, subject);
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Email");
		expectedDataMap.put(GatewayTestConstants.REFERER_URI, "");
		expectedDataMap.put(GatewayTestConstants.SHARE_WITH, "admin@gatewayO365beatle.com");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),
				"Expected log: "+expectedDataMap.get(GatewayTestConstants.MESSAGE)+" does not exist");
		Reporter.log("==================================================================================",true);
		Reporter.log(" Send Email event verification successful", true);
		Reporter.log("==================================================================================",true);
	}

	@Priority(3)
	@Test(groups ={"Regression", "REACH"}) 
	public void o365_Test_03_UploadWithLanguagues() throws Exception {
		Logger.info("==================================================================================");
		Logger.info(" Verifying upload activity event for One Drive business");
		Logger.info("==================================================================================");
		printCredentials();
		//String string ="न_H__ó_你_こ_д__حبا_ε";
		String i18NFile = "你好_こん_iß_др_¡h_lá_on_ہیلو_नम_안녕.docx";
		expectedDataMap.clear();
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getGWHeaders(suiteData));
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.loadOnedriveApp(getWebDriver());
		o365HomeAction.deleteFile(getWebDriver(), i18NFile);
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.loadOnedriveApp(getWebDriver());
		o365HomeAction.uploadItemFile(getWebDriver(),  System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+
				"resources"+File.separator+"meta"+File.separator + i18NFile);
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.loadOnedriveApp(getWebDriver());
		o365HomeAction.deleteFile(getWebDriver(), i18NFile);
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Upload");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User uploaded file named "+i18NFile);
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Document");
		expectedDataMap.put(GatewayTestConstants.OBJECT_NAME, i18NFile);
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Expected log: "+expectedDataMap.get(GatewayTestConstants.MESSAGE)+" does not exist");
		Logger.info("==================================================================================");
		Logger.info(" Upload file event verification successful");
		Logger.info("==================================================================================");
	}
	
	public String getSaasAppUserName(){
		return suiteData.getSaasAppUsername().replaceAll("@", "_");
	}
	
	@BeforeClass(groups ={"Regression", "REACH"})
	public void doBeforeClass() throws Exception {
		Logger.info("**************************************************************************");
		Logger.info("Cleaning up all policies before the tests start... ");
		Logger.info("**************************************************************************");
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getGWHeaders(suiteData));
		Logger.info("All Polices got cleaned up");
		Logger.info("**************************************************************************");
	}
	
	@AfterClass(groups ={"Regression", "REACH"})
	public void doAfterClass() throws Exception {Logger.info("**************************************************************************");
		Logger.info("Cleaning up all policies after the tests completed... ");
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getGWHeaders(suiteData));
		Logger.info("**************************************************************************");
	}
	
	
}
