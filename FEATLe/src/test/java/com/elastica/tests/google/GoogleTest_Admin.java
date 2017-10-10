package com.elastica.tests.google;

import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;
import com.elastica.logger.Logger;
import com.elastica.common.CommonTest;
import com.elastica.common.GWCommonTest;
import com.elastica.gateway.GatewayTestConstants;
import com.elastica.listeners.Priority;

public class GoogleTest_Admin extends GWCommonTest{
	Map <String, Object> expectedDataMap = new HashMap<String, Object>();
	String fromTime=backend.getCurrentTime();
	
	@Priority(1)
	@Test(groups ={"Regression"})
	public void googleAdmin_Login() throws Exception {
		fromTime=backend.getCurrentTime();
		printCredentials();
		Logger.info("Starting performing activities on Google Admin SAAS app");
		login.loginCloudSocPortal(getWebDriver(), suiteData);
		
		ga.login(getWebDriver(), suiteData);
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.remove(GatewayTestConstants.FACILITY);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Login");
		expectedDataMap.put(GatewayTestConstants.ACCOUNT_TYPE,suiteData.getAccountType());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		expectedDataMap.put(GatewayTestConstants.ELASTICA_USER, suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.IS_ANONYMOUS_PROXY, "false");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User logged in"); 
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Session");
		expectedDataMap.put(GatewayTestConstants.RESP_CODE, "302");
		//expectedDataMap.put(GatewayTestConstants.REQ_URI, GatewayTestConstants.GDRIVE_REQ_URI_LOGIN);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		//expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("Logged in Google Admin SAAS app");
	}
	
	@Priority(1)
	@Test(groups ={"REACH"})
	public void googleAdmin_Login_Reach() throws Exception {
		fromTime=backend.getCurrentTime();
		printCredentials();
		Logger.info("Starting performing activities on Google Admin SAAS app");
		ga.login(getWebDriver(), suiteData);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Login");
		expectedDataMap.put(GatewayTestConstants.ACCOUNT_TYPE,suiteData.getAccountType());
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		expectedDataMap.put(GatewayTestConstants.ELASTICA_USER, suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.IS_ANONYMOUS_PROXY, "false");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User logged in"); 
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Session");
		expectedDataMap.put(GatewayTestConstants.RESP_CODE, "302");
		//expectedDataMap.put(GatewayTestConstants.REQ_URI, GatewayTestConstants.GDRIVE_REQ_URI_LOGIN);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		//expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("Logged in Google Admin SAAS app");
		
	}
/*	@Priority(2)
	@Test(groups ={"Regression"})
	public void googleAdmin_Create_User() throws Exception {
		Logger.info("Create User");
		ga.addUser(getWebDriver(), "testgoogle");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "Added user"); 
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}*/

	@Priority(2)
	@Test(groups ={"Regression", "REACH"})
	public void googleAdmin_Edit_Company() throws Exception {
		Logger.info("Edit Company");
		printCredentials();
		ga.editCompanyProfile(getWebDriver(), suiteData);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "Edit Company Profile"); 
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	@Priority(3)
	@Test(groups ={"Regression", "REACH"})
	public void googleAdmin_Edit_CommunicationPreference() throws Exception {
		Logger.info("Edit Company");
		ga.editCommunicationPreference(getWebDriver(), suiteData);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "Communication preference"); 
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	@Priority(4)
	@Test(groups ={"Regression", "REACH"})
	public void googleAdmin_Edit_Personalize() throws Exception {
		Logger.info("Edit Peronalize");
		printCredentials();
		ga.editPersonalization(getWebDriver(), suiteData);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "Personalized"); 
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
/*	@Priority(5)
//	@Test(groups ={"Regression"})
	public void googleAdmin_Reset_Password() throws Exception {
		Logger.info("Reset Password");
		ga.homepage(getWebDriver(), suiteData);
		ga.resetPassword(getWebDriver(), suiteData);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "Reset password"); 
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap), "Logs does not match");
	}*/
	
	@Priority(6)
	@Test(groups ={"Regression", "REACH"})
	public void googleAdmin_Rename() throws Exception {
		printCredentials();
		try {
			Logger.info("Reset Password");
			ga.homepage(getWebDriver(), suiteData);
			ga.renameUser(getWebDriver(), suiteData);
		} catch (Exception e) {
			Logger.info("Error " + e);
		}
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "Rename user"); 
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap), "Logs does not match");

	}
	
	@Priority(7)
	@Test(groups ={"Regression", "REACH"})
	public void googleAdmin_GDrive_Share() throws Exception {
		Logger.info("Share");
		printCredentials();
		ga.homepage(getWebDriver(), suiteData);
		ga.sharingAndLinking(getWebDriver(), suiteData);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "Share link"); 
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap), "Logs does not match");

	}
	
}

