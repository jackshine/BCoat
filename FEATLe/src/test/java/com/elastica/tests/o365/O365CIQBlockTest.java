package com.elastica.tests.o365;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.elastica.common.GWCommonTest;
import com.elastica.gateway.GatewayTestConstants;
import com.elastica.listeners.Priority;
import com.elastica.listeners.RetryAnalyzer;
import com.elastica.logger.Logger;

public class O365CIQBlockTest extends GWCommonTest{

	Map <String, Object> expectedDataMap = new HashMap<String, Object>();
	Map<String, String>policyDataMap= new HashMap<String, String>(); 
	String fromTime=null;
	SoftAssert softAssert = new SoftAssert();
	CiqUtils utils=new CiqUtils();

	@Priority(1)
	@Test(groups ={"REGRESSION"})
	public void loginToCloudSocAppAndSetupSSO() throws Exception {
		fromTime=backend.getCurrentTime();
		Reporter.log("Started performing activities on saas app", true);
		login.loginCloudSocPortal(getWebDriver(), suiteData);
		Reporter.log("Finished login activities on cloudSoc", true);
	}
	
	@Priority(2)
	@Test(groups ={"REGRESSION", "REACH"})  //,dependsOnMethods = { "loginToCloudSocAppAndSetupSSO" }
	public void o365_Test_01_ValidateLoginActivityEvent() throws Exception {
		Reporter.log("Verifying the login event", true);
		o365Login.login(getWebDriver(), suiteData);
		//expectedDataMap.clear();
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.loadOnedriveApp(getWebDriver());
		
		//create profile
		utils.createCIQProfile( CIQConstants.ZERO,suiteData, "CIQ_FE_GW_DontDelete","CIQ_FE_GW_DontDelete",CIQConstants.SOURCE_DCI,CIQConstants.DEFAUTRISKVALUE);
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Login");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User logged in"); 
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Session");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Expected log: "+expectedDataMap.get(GatewayTestConstants.MESSAGE)+" does not exist on investigate");
		Logger.info("==================================================================================");
		Logger.info(" Login event verification successful");
		Logger.info("==================================================================================");
		
	}
	
		
	@Priority(3)
	@Test(groups ={"REGRESSION", "REACH"})
	public void o365DownloadBlockCIQfiles() throws Exception {
		//create new policies
		String ciqfiles="pci.txt";
		//delete old policy
		boolean status=o365HomeAction.selectFileName(getWebDriver(), ciqfiles);
		if (!status){
			policy.deletePolicy(GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+"Block", suiteData, backend.getHeaders(suiteData));
			o365HomeAction.uploadItemFile(getWebDriver(),  
					System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+
					"resources"+File.separator+"ciq"+File.separator + ciqfiles);
			
		}
		
		o365HomeAction.refresh(getWebDriver(), 15);
			policy.createCIQPolicyBlock(GatewayTestConstants.CIQ_DOWNLOAD_POLICY_NAME+"Block", suiteData, backend.getHeaders(suiteData),"CIQ_FE_GW_DontDelete","download");
		// download files
			try{
				//o365HomeAction.downloadItemFileForBlock(getWebDriver(), ciqfiles);
				//o365HomeAction.refresh(getWebDriver(), 5);
				o365HomeAction.downloadItemFileForBlock(getWebDriver(), ciqfiles);
				try {
					boolean value = clickOkInPopup();
					//Assert.assertTrue(value, "Blocker Popup Not Found");
				} catch (Exception e) {
					Logger.info("Error " + e.getMessage());
				}
			}
			catch(Exception e) {

				Reporter.log("error in downlaoding  the file "+ciqfiles, true);
			}
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Policy Violation");
		expectedDataMap.put(GatewayTestConstants.ACTION_TAKEN, "block");
		expectedDataMap.put(GatewayTestConstants.POLICY_ACTION, "ALERT");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "[ALERT] File " +ciqfiles+ 
				" download violated policy - " + GatewayTestConstants.CIQ_DOWNLOAD_POLICY_NAME+"Block");
		expectedDataMap.put(GatewayTestConstants.POLICY_TYPE, "FileTransfer");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, GatewayTestConstants.SEVERITY_HIGH);		
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),
				"Expected log: "+expectedDataMap.get(GatewayTestConstants.MESSAGE)+" does not exist");
		Logger.info("==================================================================================");
		Logger.info(" Download file with policy severity high successful");
		Logger.info("==================================================================================");
		Reporter.log("Log verification successful for method:"+Thread.currentThread().getStackTrace()[1].getMethodName(), true);
	}
	

	@Priority(4)
	@Test(groups ={"REGRESSION", "REACH"}) //,dependsOnMethods = { "o365_Test_01_ValidateLogingActivityEvent" }
	public void o365UploadCIQFiles() throws Exception {

		
		o365HomeAction.getPortal(getWebDriver(), suiteData);
		o365HomeAction.loadOnedriveApp(getWebDriver());
		//create new policies
		policy.createCIQPolicyBlock(GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+"Block", suiteData, backend.getHeaders(suiteData),"CIQ_FE_GW_DontDelete","upload");

		String  ciqfiles="pii.txt";
		o365HomeAction.deleteFile(getWebDriver(), ciqfiles);
		o365HomeAction.uploadItemFileWithBlock(getWebDriver(),  System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+
				"resources"+File.separator+"ciq"+File.separator + ciqfiles);

		try {
			boolean value = clickOkInPopup();
			Assert.assertTrue(value, "Blocker Popup Not Found");
		} catch (Exception e) {
			Logger.info("Error " + e.getMessage());
		}		
		Assert.assertFalse(o365HomeAction.selectFileName(getWebDriver(), ciqfiles),"file gets uploaded");	
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Policy Violation");
		expectedDataMap.put(GatewayTestConstants.ACTION_TAKEN, "block");
		expectedDataMap.put(GatewayTestConstants.POLICY_ACTION, "ALERT");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "[ALERT] File " +ciqfiles+ 
				" upload violated policy - " + GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+"Block");
		expectedDataMap.put(GatewayTestConstants.POLICY_TYPE, "FileTransfer");

		expectedDataMap.put(GatewayTestConstants.SEVERITY, "high");
		expectedDataMap.remove(GatewayTestConstants.COUNTRY);
		expectedDataMap.remove(GatewayTestConstants.USER_NAME);
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),
				"Expected log: "+expectedDataMap.get(GatewayTestConstants.MESSAGE)+" does not exist");
		Logger.info("==================================================================================");
		Logger.info(" Upload file with policy severity high successful");
		Logger.info("==================================================================================");


	}
	

	public String getSaasAppUserName(){
		return suiteData.getSaasAppUsername().replaceAll("@", "_");
	}
	
	
	
	@AfterClass(alwaysRun= true)
	public void Cleanup() throws Exception {
		
		policy.deletePolicy(GatewayTestConstants.CIQ_DOWNLOAD_POLICY_NAME+"Block", suiteData, backend.getHeaders(suiteData));
		policy.deletePolicy(GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+"Block", suiteData, backend.getHeaders(suiteData));
		Reporter.log("Finished cleanup activities on cloudSoc", true);
	}


}
