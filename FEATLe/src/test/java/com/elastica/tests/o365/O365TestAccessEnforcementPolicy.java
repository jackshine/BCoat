package com.elastica.tests.o365;

import java.util.HashMap;
import java.util.Map;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import com.elastica.action.protect.ProtectAction;
import com.elastica.action.protect.ProtectDTO;
import com.elastica.common.GWCommonTest;
import com.elastica.gateway.GatewayTestConstants;
import com.elastica.listeners.Priority;
import com.elastica.listeners.RetryAnalyzer;
import com.elastica.logger.Logger;
import com.elastica.tests.protect.FileTransferPolicyTests;

public class O365TestAccessEnforcementPolicy extends GWCommonTest{
	Map <String, Object> expectedDataMap = new HashMap<String, Object>();
	Map<String, String>policyDataMap= new HashMap<String, String>(); 
	String fromTime=backend.getCurrentTime();
	SoftAssert softAssert = new SoftAssert();
	ProtectAction protectAction = new ProtectAction();
	ProtectDTO protectData = new ProtectDTO();
	String upperCase;
	String upperCaseTestUser;
	
	@BeforeMethod()
	public void clearDataMap(){
		expectedDataMap.clear();
	}

	@Priority(1)
	@Test(groups ={"Regression"}, retryAnalyzer=RetryAnalyzer.class)
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
	
	
	@Priority(2)
	@Test(groups ={"Regression",  "REACH"}) 
	public void o365_Test_Login_AccessEnforcement() throws Exception {
		printCredentials();
		upperCaseTestUser = suiteData.getTestUsername();
		suiteData.setTestUsername(upperCaseTestUser.toLowerCase());
		protectAction.createEnableAccessEnforcementPolicy(client, suiteData, 
				GatewayTestConstants.O365_POLICY_NAME + getSaasAppUserName(), "Session:Login", "high");
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		o365Login.loginWithBlock(getWebDriver(), suiteData);
		try {
			boolean value = clickOkInPopup();
			Assert.assertTrue(value, "Blocker Popup Not Found");
		} catch (Exception e) {
			Logger.info("Error " + e.getMessage());
		}
		
		upperCase = suiteData.getSaasAppUsername();
	
		suiteData.setSaasAppUsername(upperCase.toLowerCase());
		
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Policy Violation");
		expectedDataMap.put(GatewayTestConstants.ACTION_TAKEN, "");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "[ALERT] " + suiteData.getTestUsername() + 
				" attempted Activity: Login on Object type: "); 
		expectedDataMap.put(GatewayTestConstants.POLICY_ACTION, "ALERT");
		expectedDataMap.put(GatewayTestConstants.POLICY_TYPE, "AccessEnforcement");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "high");
		expectedDataMap.remove(GatewayTestConstants.COUNTRY);
		expectedDataMap.remove(GatewayTestConstants.USER_NAME);
		expectedDataMap.put(GatewayTestConstants.USER, upperCase.toLowerCase());
		

		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),
				"Expected log: "+expectedDataMap.get(GatewayTestConstants.MESSAGE)+" does not exist on investigate");
	}
	
	@Priority(3)
	@Test(groups ={"Regression", "REACH"})  
	public void o365_Test_Deactivate_Policy_And_Relogin() throws Exception {
		fromTime=backend.getCurrentTime();
		printCredentials();
		Logger.info("==================================================================================");
		Logger.info(" Verifying  Relogin event to Saas App after disable policy"+suiteData.getSaasAppName());
		Logger.info("==================================================================================");
		suiteData.setSaasAppUsername(upperCase);
		protectAction.deActivatePolicy(client, suiteData, 
				GatewayTestConstants.O365_POLICY_NAME + getSaasAppUserName());
		
		o365Login.login(getWebDriver(), suiteData);
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Login");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User logged in"); 
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Session");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER, upperCase.toLowerCase());
		suiteData.setSaasAppUsername(upperCase.toLowerCase());
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),
				"Expected log: "+expectedDataMap.get(GatewayTestConstants.MESSAGE)+" does not exist on investigate");
		Logger.info("==================================================================================");
		Logger.info(" Verifying  Relogin event to Saas App after disable policy successful");
		Logger.info("==============================");
	}
	
	
	@Priority(4)
	@Test(groups ={"Regression", "REACH"})  
	public void o365_Test_Delete_Policy_And_Relogin() throws Exception {
		fromTime=backend.getCurrentTime();
		printCredentials();
		Logger.info("==================================================================================");
		Logger.info(" Verifying  Relogin event to Saas App "+suiteData.getSaasAppName());
		Logger.info("==================================================================================");
		suiteData.setSaasAppUsername(upperCase);
		protectAction.deletePolicy(client, suiteData, GatewayTestConstants.O365_POLICY_NAME + getSaasAppUserName());
		o365Login.logout(getWebDriver());
		o365Login.relogin(getWebDriver(), suiteData);
		//o365Login.login(getWebDriver(), suiteData);
		
		
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Login");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User logged in"); 
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Session");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER, upperCase.toLowerCase());
		suiteData.setSaasAppUsername(upperCase.toLowerCase());
		
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),
				"Expected log: "+expectedDataMap.get(GatewayTestConstants.MESSAGE)+" does not exist on investigate");
		Logger.info("==================================================================================");
		Logger.info(" Login event verification successful");
		Logger.info("==============================");
	}
	
	public String getSaasAppUserName(){
		return suiteData.getSaasAppUsername().replaceAll("@", "_");
	}
	
	@BeforeClass(groups ={"Regression", "REACH"})
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