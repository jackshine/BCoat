package com.elastica.tests.common;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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

public class LoginAccessEnforcementAllSAASApp extends GWCommonTest{
	Map <String, Object> expectedDataMap = new HashMap<String, Object>();
	Map<String, String>policyDataMap= new HashMap<String, String>(); 
	String fromTime=backend.getCurrentTime();
	SoftAssert softAssert = new SoftAssert();
	ProtectAction protectAction = new ProtectAction();
	ProtectDTO protectData = new ProtectDTO();
	List<String> testUserNames;
//	List<String> saasAppUsersName = Arrays.asList("Box", "Google Drive");
	String saasAppName;
	List<String> saasAppUsersName = Arrays.asList("__ALL_EL__");
	
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
	@Test(groups ={"Regression", "REACH"}) 
	public void GDrive_Test_Login_AccessEnforcement() throws Exception {
		fromTime=backend.getCurrentTime();
		saasAppName = suiteData.getSaasAppUsername();
		testUserNames = Arrays.asList(suiteData.getTestUsername());
		
		printCredentials();
		protectAction.createEnableAccessEnforcementPolicyWithSAASApp(client, suiteData, 
				GatewayTestConstants.COMMON_AEP + getSaasAppUserName(), "__ALL_EL__:__ALL_EL__",
				"high",testUserNames,  saasAppUsersName);

		boolean accessEnabled = false;
		for (int i = 0; i < 3; i++) {
			getWebDriver().get(suiteData.getSaasAppBaseUrl());
			gda.hardWait(15);
			accessEnabled = getWebDriver().getPageSource().contains("Access to this URL/Service is Blocked due to an Access Enforcement Policy!");
			if (accessEnabled == true) {
				break;
			}
		}
		Assert.assertTrue(accessEnabled, "Accesss Enforcement policy didnt affect the SAAS App Gdrive");
//		gda.loginBlock(getWebDriver(), suiteData);
//		try {
//			boolean value = clickOkInPopup();
//			
//		} catch (Exception e) {
//			Logger.info("Error " + e.getMessage());
//		}
		Logger.info("Popup displayed ");
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Policy Violation");
		expectedDataMap.put(GatewayTestConstants.ACTION_TAKEN, "");
		//expectedDataMap.put(GatewayTestConstants.FACILITY, "Google Drive");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "[ALERT] " +  suiteData.getTestUsername() + 
				" attempted access to cloud apps using Platform"); 
		expectedDataMap.put(GatewayTestConstants.POLICY_ACTION, "ALERT");
		expectedDataMap.put(GatewayTestConstants.POLICY_TYPE, "AccessEnforcement");
		expectedDataMap.put(GatewayTestConstants.REFERER_URI, "");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "high");
		expectedDataMap.remove(GatewayTestConstants.COUNTRY);
		expectedDataMap.remove(GatewayTestConstants.FACILITY);
		expectedDataMap.remove(GatewayTestConstants.USER_NAME);
		suiteData.setSaasAppUsername(suiteData.getTestUsername());
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),
				"Expected log: "+expectedDataMap.get(GatewayTestConstants.MESSAGE)+" does not exist on investigate");
		
	}
	
	
	@Priority(3)
	@Test(groups ={"Regression", "REACH"}) 
	public void Box_Test_Login_AccessEnforcement() throws Exception {
		suiteData.setSaasAppUsername(saasAppName);
		fromTime=backend.getCurrentTime();
		suiteData.setSaasAppBaseUrl("https://app.box.com/login");
		suiteData.setSaasAppName("Box");
		printCredentials();
		//box.loginBlock(getWebDriver(), suiteData);
		
		getWebDriver().get(suiteData.getSaasAppBaseUrl());
		box.hardWait(10);
		getWebDriver().get(suiteData.getSaasAppBaseUrl());
		box.hardWait(15);
		Assert.assertTrue(getWebDriver().getPageSource().contains("Access to this URL/Service is Blocked due to an Access Enforcement Policy!"),
				"Accesss Enforcement policy didnt affect the SAAS App Box");
//		try {
//			boolean value = clickOkInPopup();
//			Assert.assertTrue(value, "Blocker Popup Not Found");
//		} catch (Exception e) {
//			Logger.info("Error " + e.getMessage());
//		}
		
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Policy Violation");
		expectedDataMap.put(GatewayTestConstants.ACTION_TAKEN, "");
		expectedDataMap.put(GatewayTestConstants.FACILITY, "Box");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "[ALERT] " + suiteData.getTestUsername() + 
				" attempted access to cloud apps using Platform"); 
		expectedDataMap.put(GatewayTestConstants.POLICY_ACTION, "ALERT");
		expectedDataMap.put(GatewayTestConstants.POLICY_TYPE, "AccessEnforcement");
		expectedDataMap.put(GatewayTestConstants.REFERER_URI, "");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "high");
		expectedDataMap.remove(GatewayTestConstants.COUNTRY);
		expectedDataMap.remove(GatewayTestConstants.USER_NAME);
		suiteData.setSaasAppUsername(suiteData.getTestUsername());
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),
				"Expected log: "+expectedDataMap.get(GatewayTestConstants.MESSAGE)+" does not exist on investigate");
		suiteData.setSaasAppUsername(saasAppName);
		protectAction.deActivatePolicy(client, suiteData, 
				GatewayTestConstants.COMMON_AEP + getSaasAppUserName());
		protectAction.deletePolicy(client, suiteData, GatewayTestConstants.COMMON_AEP + getSaasAppUserName());
	}
	
	
	public String getSaasAppUserName(){
		return suiteData.getSaasAppUsername().replaceAll("@", "_");
	}
	
	@BeforeClass(groups ={"Regression", "REACH"})
	public void doBeforeClass() throws Exception {
		try {
			Logger.info("Delete Policy Before Test ");
			protectData.setPolicyName(GatewayTestConstants.COMMON_AEP + getSaasAppUserName());
			protectAction.deletePolicy(client, suiteData, GatewayTestConstants.COMMON_AEP + getSaasAppUserName());
			Logger.info(" policy to delete");
		} catch(Exception e) {
			Logger.info("No policy to delete");
		}
	}
	
	@AfterClass(groups ={"Regression", "REACH"})
	public void doAfterClass() throws Exception {
		try {
			Logger.info("Delete Policy After Test ");
			protectData.setPolicyName(GatewayTestConstants.COMMON_AEP + getSaasAppUserName());
			protectAction.deletePolicy(client, suiteData, GatewayTestConstants.COMMON_AEP + getSaasAppUserName());
			Logger.info(" policy to delete");
		} catch(Exception e) {
			Logger.info("No policy to delete");
		}
	}

}
