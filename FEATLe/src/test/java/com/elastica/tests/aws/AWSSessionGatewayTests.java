package com.elastica.tests.aws;

import java.util.HashMap;
import java.util.Map;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.elastica.common.GWCommonTest;
import com.elastica.gateway.GatewayTestConstants;
import com.elastica.listeners.Priority;


public class AWSSessionGatewayTests extends GWCommonTest{
	Map<String, Object> expectedDataMap = new HashMap<String, Object>();
	String fromTime=backend.getCurrentTime();

	@Priority(1)
	@Test(groups={"Regression", "Sanity"})
	public void loginToPortal() throws Exception {
		Reporter.log("Login to cloudSoc", true);
		printCredentials();
		login.loginCloudSocPortal(getWebDriver(), suiteData);
		Thread.sleep(60000);
		Reporter.log("Login to cloudsoc portal successful", true);
	}
	
	@Priority(2)
	@Test(groups={"Regression", "Sanity", "Reach"})
	public void verifyLogin() throws Exception{
		Reporter.log("Login to AWS saas App", true);
		expectedDataMap.clear();
		printCredentials();
		awsAction.loginAWS(getWebDriver(), suiteData);
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Session");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Login");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User logged in.");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("Login to AWS saas App Successful", true);
	}
	
	
	@Priority(3)
	@Test(groups ={"Regression", "Sanity", "Reach"})
	public void verifyLogout() throws Exception {
		Reporter.log("Logging out of App", true);
		printCredentials();
		expectedDataMap.clear();
		awsAction.logoutAWS(getWebDriver());
		//setCommonFieldsInExpectedDataMap(expectedDataMap);
		//setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Session");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Logout");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User logged out.");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("Logging out successful", true);
	}
	
	@Priority(4)
	@Test(groups ={"Regression", "Reach"})
	public void verifyInvalidateLoginMessage() throws Exception {
		Reporter.log("Invalid Login Session", true);
		expectedDataMap.clear();
		//setCommonFieldsInExpectedDataMap(expectedDataMap);
		//setLocationFieldsInExpectedDataMap(expectedDataMap);
		awsAction.invalidateLoginAWS(getWebDriver(), suiteData);
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Session");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "InvalidLogin");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User login failed");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("Invalid Login Session check succuessful", true);
	}
}