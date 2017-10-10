package com.elastica.tests.o365;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.elastica.common.GWCommonTest;
import com.elastica.gateway.GatewayTestConstants;
import com.elastica.listeners.Priority;
import com.elastica.logger.Logger;

public class O365Tests_Email_Personal_Regression extends GWCommonTest{
	Map <String, Object> expectedDataMap = new HashMap<String, Object>();
	Map<String, String>policyDataMap= new HashMap<String, String>(); 
	String fromTime=backend.getCurrentTime();
	String subject = "This is subject for personal";
	
	@Priority(1)
	@Test(groups ={"EXTERNAL"})
	public void loginToCloudSocAppAndSetupSSO() throws Exception {
		printCredentials();
		fromTime=backend.getCurrentTime();
		Logger.info("Started performing activities on saas app");
		login.loginCloudSocPortal(getWebDriver(), suiteData);
		Logger.info("Finished login activities on cloudSoc");
	}
	
	@Priority(2)
	@Test(groups ={"EXTERNAL", "REACH"}) 
	public void o365_Test_01_ValidateLoginActivityEvent() throws Exception {
		Logger.info("==================================================================================");
		Logger.info(" Login event ");
		Logger.info("==================================================================================");
		printCredentials();
		o365Login.onedriveLogin(getWebDriver(), suiteData);
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.remove(GatewayTestConstants.FACILITY);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Login");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User logged in"); 
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Session");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Expected log: "+expectedDataMap.get(GatewayTestConstants.MESSAGE)+" does not exist on investigate");
		Logger.info("==================================================================================");
		Logger.info(" Login event verification successful");
		Logger.info("==================================================================================");
		Logger.info("Login Event Successfull");
	}
	
	@Priority(3)
	@Test(groups ={"EXTERNAL", "REACH"}) 
	public void o365_Test_02_ValidateSendEmailActivityEvent() throws Exception {
		Logger.info("==================================================================================");
		Logger.info(" Verifying email send event in Outlook");
		Logger.info("==================================================================================");
		printCredentials();
		
		o365HomeAction.loadgeneralhomepage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.sendEmailPersonal(getWebDriver(), suiteData.getSaasAppUsername(), subject);
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User sent an email to " + suiteData.getSaasAppUsername());
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Send");
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Email");
		expectedDataMap.put(GatewayTestConstants.REFERER_URI, "");
		expectedDataMap.put(GatewayTestConstants.SHARE_WITH, suiteData.getSaasAppUsername());
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Expected log: "+expectedDataMap.get(GatewayTestConstants.MESSAGE)+" does not exist");
		Logger.info("==================================================================================");
		Logger.info(" Verifying email send event in Outlook successful");
		Logger.info("==================================================================================");
	}
	
//	@Priority(4)
//	@Test(groups ={"EXTERNAL", "REACH"})  #42439 
	public void o365_Test_03_ValidateReceiveEmailActivityEvent() throws Exception {
		Logger.info("==================================================================================");
		Logger.info(" Verifying email receive event in Outlook");
		Logger.info("==================================================================================");
		printCredentials();
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Receive");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User received an email with subject "+ subject + " from " + suiteData.getSaasAppUsername() );
		expectedDataMap.put(GatewayTestConstants.OBJECT_NAME, subject);
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Email");
		expectedDataMap.put(GatewayTestConstants.REFERER_URI, "");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("Email Receive email Successful");
	}
	
	@Priority(5)
	@Test(groups ={"EXTERNAL", "REACH"}) 
	public void o365_Test_04_ValidateDraftEmailActivityEvent() throws Exception {
		Logger.info("==================================================================================");
		Logger.info(" Verifying email draft event in Outlook");
		Logger.info("==================================================================================");
		printCredentials();
		fromTime=backend.getCurrentTime();
		o365HomeAction.loadgeneralhomepage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.draftEmailPersonal(getWebDriver());
		
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User saved an email as draft with subject This is subject");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Save");
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Email");
		expectedDataMap.put(GatewayTestConstants.RESP_CODE, "200");
		expectedDataMap.put(GatewayTestConstants.REFERER_URI, "");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		Logger.info("==================================================================================");
		Logger.info(" Verifying email draft event in Outlook successful");
		Logger.info("==================================================================================");
	}
	
	@Priority(6)
//	@Test(groups ={ "EXTERNAL", "REACH"})
	public void o365_Test_Discard() throws Exception {
		Logger.info("Discard Email");
		printCredentials();
		fromTime=backend.getCurrentTime();
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.loadEmailApp(getWebDriver());
		o365HomeAction.discardEmailPersonal(getWebDriver());
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Discard");
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Email");
		expectedDataMap.put(GatewayTestConstants.REFERER_URI, "");
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User discarded an email with subject ");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("Discard Email Successful");
	}
	
	@Priority(7)
	@Test(groups ={"EXTERNAL", "REACH"}) 
	public void o365_Test_EmailAttachedWithFile() throws Exception {
		Logger.info("Email Attached via One Drive");
		fromTime=backend.getCurrentTime();
		printCredentials();
		o365HomeAction.loadgeneralhomepage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.createEmailClickAttach(getWebDriver(), GatewayTestConstants.GDRIVE_ONE_DRIVE_FILE_ATTACH);
		uploadSingleFile(System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+
				"resources"+File.separator+"meta"+File.separator + GatewayTestConstants.GDRIVE_ORGINAL_FILE, suiteData);
		o365HomeAction.sendEmailButtonPersonal(getWebDriver());
		
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Upload");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User uploaded file "+ GatewayTestConstants.GDRIVE_ORGINAL_FILE +" as an attachment");
		expectedDataMap.put(GatewayTestConstants.OBJECT_NAME, GatewayTestConstants.GDRIVE_ORGINAL_FILE);
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File");
		expectedDataMap.put(GatewayTestConstants.REFERER_URI, "");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("Email Attached via One Drive Successful");
	}
	
	@Priority(8)
	@Test(groups ={ "EXTERNAL", "REACH"})
	public void o365_Test_04_ValidateOutLookLogoutEvent() throws Exception {
		o365HomeAction.oneDriveLogout(getWebDriver());
		Logger.info("==================================================================================");
		Logger.info(" Verifying logout event");
		Logger.info("==================================================================================");
		printCredentials();
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.remove(GatewayTestConstants.FACILITY);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User logged out");
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Session");
		expectedDataMap.put(GatewayTestConstants.REFERER_URI, "");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Expected log: "+expectedDataMap.get(GatewayTestConstants.MESSAGE)+" does not exist");
		Logger.info("==================================================================================");
		Logger.info(" Logout event verification successfull");	
		Logger.info("==================================================================================");
	}
	
}