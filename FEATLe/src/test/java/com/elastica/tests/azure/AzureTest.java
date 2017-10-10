package com.elastica.tests.azure;
import static org.testng.Assert.assertTrue;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterGroups;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.elastica.common.CommonTest;
import com.elastica.common.GWCommonTest;
import com.elastica.gateway.GatewayTestConstants;
import com.elastica.listeners.Priority;
import com.elastica.listeners.RetryAnalyzer;
import com.elastica.logger.Logger;


public class AzureTest extends GWCommonTest{
	Map <String, Object> expectedDataMap = new HashMap<String, Object>();
	Map<String, String>policyDataMap= new HashMap<String, String>(); 
	String fromTime= "";
	String vmPermanent ="AutomationOnly";
	String cloudService ="AutomationOnly";
	String vmTemp ="AutomationVM";
	String vmUserName ="AutomationUser1";
	String vmPassword ="AutomationUser1";
	String vmLocation ="northcentralus";
	String folderNameFileOper ="";
	String filePath = GatewayTestConstants.BOX_ORIGINAL_FILE_PATH;
	String fileNameBoxFile ="";
	String fileNameEncrypt ="";
	String encryptPolicyName ="";
	String fileuploadPolicyName ="";
	String fileNameFileOper ="";
	String fileName_6mb ="";
	String fileName_10mb ="";
	String fileName_20mb ="";
	String fileNameFolderOper ="";
	String fileCopied ="";
	String folderCopied ="";
	String bookmarkUrl ="http://yahoo.com";
	String shareWithUser="admin@gatewaybeatle.com";
	String collaborator="box-admin@gatewaybeatle.com";
	String uploadFile= "upload.pdf";
	String uploadFile_6mb= "6MB_Upload.zip";
	String uploadFile_10mb= "10MB_Upload.zip";
	String uploadFile_20mb= "20MB_Upload.dmg";
	String uploadFile_certificate= "elastica.crt";
	String tagName= "important, mydocs";
	String commentOnFile= "This is my comment";
	String userName= "";
	boolean vmDeleted = false;
	boolean vmStopped = false;
	ArrayList<File> filesToDeleteFromWorkspace  = new ArrayList<File>();
	ArrayList<String> filesToDeleteFromSaasApp  = new ArrayList<String>();
	SoftAssert softAsst = new SoftAssert();
	
	
//	@Priority(50)
	@AfterSuite(alwaysRun=true)
	public void cleanup() throws InterruptedException {
//		delete the temp file from framework upload folder
		box.deleteFiles(filesToDeleteFromWorkspace);
		
		//delete test created files and folders one by one
//		if(filesToDeleteFromSaasApp.size()>0){
//			box.gotoHomePage(getWebDriver(),suiteData);
//			box.deleteMultipleItems(getWebDriver(), filesToDeleteFromSaasApp) ;
//		}
		
		
	} 
	
	public void setup()
	{
		if(suiteData.getAccountType().equals("External")){
			userName = suiteData.getTestUsername();
		}
		else{
			userName = suiteData.getSaasAppUsername();
		}
		
		fromTime =new org.joda.time.DateTime( org.joda.time.DateTimeZone.UTC ).minusMinutes(1).toString();
	}
	
	
	@Priority(1)
	@Test(groups ={"SANITY","REGRESSION","P1","P2","CLOUDSOC"}, retryAnalyzer=RetryAnalyzer.class)
	public void loginToCloudSocAppAndSetupSSO() throws Exception {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		
		Logger.info("Start Login to cloudSoc");
		login.loginCloudSocPortal(getWebDriver(), suiteData);
		Logger.info("Completed Login to cloudSoc");
	}
	
	@Priority(2)
	@Test(groups ={"SANITY","REGRESSION","P1"} , retryAnalyzer=RetryAnalyzer.class)
	public void validateLoginEvent() throws Exception {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		
		setup();
		
		azure.login(getWebDriver(), suiteData);
		
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.remove(GatewayTestConstants.FACILITY);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Login");
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Session");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User logged in"); 
		expectedDataMap.put(GatewayTestConstants.REFERER_URI, "NOT_EMPTY");
		expectedDataMap.put(GatewayTestConstants.REQ_URI, "NOT_EMPTY");
		
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("Log verification successful for method:"+Thread.currentThread().getStackTrace()[1].getMethodName(), true);
		
		
	}
	@Priority(3)
	@Test(groups ={"SANITY","REGRESSION","P1"} , dependsOnMethods = { "validateLoginEvent" }, retryAnalyzer=RetryAnalyzer.class)
	public void validateCertificateUpload() throws Exception {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		
		azure.hardWait(30);
		azure.certificateOperations(getWebDriver(), cloudService, "Upload", 10, filePath+uploadFile_certificate);
		
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Upload");
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Certificate");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User uploaded a certificate"); 
		expectedDataMap.put(GatewayTestConstants.REFERER_URI, "NOT_EMPTY");
		expectedDataMap.put(GatewayTestConstants.REQ_URI, "NOT_EMPTY");
		
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("Log verification successful for method:"+Thread.currentThread().getStackTrace()[1].getMethodName(), true);
	}

	@Priority(4)
	@Test(groups ={"SANITY","REGRESSION","P1"} , dependsOnMethods = { "validateLoginEvent" }, retryAnalyzer=RetryAnalyzer.class)
	public void validateCertificateDelete() throws Exception {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		
		azure.hardWait(30);
		azure.certificateOperations(getWebDriver(), cloudService, "Delete", 10, filePath+uploadFile_certificate);
		
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Delete");
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Certificate");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User deleted a certificate of service "+cloudService); 
		expectedDataMap.put(GatewayTestConstants.REFERER_URI, "NOT_EMPTY");
		expectedDataMap.put(GatewayTestConstants.REQ_URI, "NOT_EMPTY");
		
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("Log verification successful for method:"+Thread.currentThread().getStackTrace()[1].getMethodName(), true);
	}

	@Priority(5)
	@Test(groups ={"SANITY","REGRESSION","P1"} , dependsOnMethods = { "validateLoginEvent" }, retryAnalyzer=RetryAnalyzer.class)
	public void validateVirtualMachineStart() throws Exception {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		
//		Logger.info("Waiting for page load, for 30 seconds");
//		azure.hardWait(30);
		azure.vmOperations(getWebDriver(), vmPermanent, "Start", 10);
		
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Start");
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Virtual Machine");
		expectedDataMap.put(GatewayTestConstants.OBJECT_NAME, vmPermanent);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User started a virtual-machine "+vmPermanent); 
		expectedDataMap.put(GatewayTestConstants.REFERER_URI, "NOT_EMPTY");
		expectedDataMap.put(GatewayTestConstants.REQ_URI, "NOT_EMPTY");
		
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("Log verification successful for method:"+Thread.currentThread().getStackTrace()[1].getMethodName(), true);
	}
	@Priority(6)
	@Test(groups ={"SANITY","REGRESSION","P1"} , dependsOnMethods = { "validateLoginEvent" }, retryAnalyzer=RetryAnalyzer.class)
	public void validateVirtualMachineStop() throws Exception {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		
		Logger.info("Waiting for page load, for 30 seconds");
		azure.hardWait(30);
		
		azure.vmOperations(getWebDriver(), vmPermanent, "Stop", 10);
		
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Shutdown");
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Virtual Machine");
		expectedDataMap.put(GatewayTestConstants.OBJECT_NAME, vmPermanent);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User stopped a virtual-machine "+vmPermanent); 
		expectedDataMap.put(GatewayTestConstants.REFERER_URI, "NOT_EMPTY");
		expectedDataMap.put(GatewayTestConstants.REQ_URI, "NOT_EMPTY");
		
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("Log verification successful for method:"+Thread.currentThread().getStackTrace()[1].getMethodName(), true);
	}
	
	@Priority(7)
	@Test(groups ={"SANITY","REGRESSION","P1"} , dependsOnMethods = { "validateLoginEvent" }, retryAnalyzer=RetryAnalyzer.class)
	public void validateVirtualMachineCreate() throws Exception {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		
		azure.clickOnSideBarLink(getWebDriver(), "Virtual machines", 20);
		if((azure.checkServiceExists(getWebDriver(), vmTemp)==true) && (vmDeleted ==false)){
			azure.vmOperations(getWebDriver(), vmTemp, "Delete", 10);
			vmDeleted =true;
		}
		
		azure.createVM(getWebDriver(), vmTemp, vmUserName, vmPassword, 30);
		
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Create");
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Virtual Machine");
		expectedDataMap.put(GatewayTestConstants.OBJECT_NAME, vmTemp);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User created a virtual machine "+vmTemp+", virtual-machine username "+vmUserName+" at location "+vmLocation); 
		expectedDataMap.put(GatewayTestConstants.REFERER_URI, "NOT_EMPTY");
		expectedDataMap.put(GatewayTestConstants.REQ_URI, "NOT_EMPTY");
		
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("Log verification successful for method:"+Thread.currentThread().getStackTrace()[1].getMethodName(), true);
	}
	
	@Priority(8)
	@Test(groups ={"SANITY","REGRESSION","P1"} , dependsOnMethods = { "validateLoginEvent" }, retryAnalyzer=RetryAnalyzer.class)
	public void validateVirtualMachineDelete() throws Exception {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		
		azure.clickOnSideBarLink(getWebDriver(), "Virtual machines", 20);
		if(vmDeleted ==false){
			azure.vmOperations(getWebDriver(), vmTemp, "Delete", 10);
			vmDeleted =true;
		}
		
		
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Delete");
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Virtual Machine");
		expectedDataMap.put(GatewayTestConstants.OBJECT_NAME, vmTemp);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User deleted a virtual-machine "+vmTemp); 
		expectedDataMap.put(GatewayTestConstants.REFERER_URI, "NOT_EMPTY");
		expectedDataMap.put(GatewayTestConstants.REQ_URI, "NOT_EMPTY");
		
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("Log verification successful for method:"+Thread.currentThread().getStackTrace()[1].getMethodName(), true);
	}
	
	@Priority(9)
	@Test(groups ={"REGRESSION","P1","SANITY"}, dependsOnMethods ={"validateLoginEvent"})
	public void validateLogoutEvent() throws Exception {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		azure.logout(getWebDriver());
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Logout");
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Session");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User logged out"); 
		expectedDataMap.put(GatewayTestConstants.REFERER_URI, "");
//		expectedDataMap.put(GatewayTestConstants.STATUS_CODE, "204");
//		expectedDataMap.put(GatewayTestConstants.RESP_CODE, "302");
		
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("Log verification successful for method:"+Thread.currentThread().getStackTrace()[1].getMethodName(), true);
	}
	
	@Priority(10)
	@Test(groups ={"REGRESSION","P1","SANITY"} , dependsOnMethods ={"validateLoginEvent"})
	public void validateInvalidLogin() throws Exception {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		azure.invalidLogin(getWebDriver(), suiteData);
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.remove(GatewayTestConstants.FACILITY);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "InvalidLogin");
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Session");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User login failed!"); 
		expectedDataMap.put(GatewayTestConstants.REFERER_URI, "");
		expectedDataMap.put(GatewayTestConstants.REQ_URI, "NOT_EMPTY");
//		expectedDataMap.put(GatewayTestConstants.STATUS_CODE, "204");
//		expectedDataMap.put(GatewayTestConstants.RESP_CODE, "302");
		
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("Log verification successful for method:"+Thread.currentThread().getStackTrace()[1].getMethodName(), true);
	}

	
	public void setCommonFieldsInExpectedDataMap(){
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		expectedDataMap.put(GatewayTestConstants.ACCOUNT_TYPE,suiteData.getAccountType());
		expectedDataMap.put(GatewayTestConstants.BROWSER, suiteData.getBrowser());
		expectedDataMap.put(GatewayTestConstants.DEVICE, "NOT_EMPTY");
		expectedDataMap.put(GatewayTestConstants.ELASTICA_USER, suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.FACILITY, suiteData.getSaasAppName());
		expectedDataMap.put(GatewayTestConstants.HOST, "NOT_EMPTY");
		expectedDataMap.put(GatewayTestConstants.CREATED_TIME_STAMP, "NOT_EMPTY");
		expectedDataMap.put(GatewayTestConstants.INSERTED_TIME_STAMP, "NOT_EMPTY");
		expectedDataMap.put(GatewayTestConstants.IS_ANONYMOUS_PROXY, GatewayTestConstants.IS_ANONYMOUS_PROXY_FALSE);
		expectedDataMap.put(GatewayTestConstants.REQ_SIZE, "NOT_EMPTY");
		expectedDataMap.put(GatewayTestConstants.REQ_URI, "NOT_EMPTY");
		expectedDataMap.put(GatewayTestConstants.RESP_SIZE, "NOT_EMPTY");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.TIME_ZONE, "NOT_EMPTY");
		expectedDataMap.put(GatewayTestConstants.USER, suiteData.getSaasAppUsername());
		expectedDataMap.put(GatewayTestConstants.USER_NAME, "NOT_EMPTY");
		expectedDataMap.put(GatewayTestConstants.VERSION, "NOT_EMPTY");
	}
	public void setLocationFieldsInExpectedDataMap(){
//		expectedDataMap.put(GatewayTestConstants.CITY, "NOT_EMPTY");
		expectedDataMap.put(GatewayTestConstants.COUNTRY, "NOT_EMPTY");
		expectedDataMap.put(GatewayTestConstants.LATITUDE, "NOT_EMPTY");
		expectedDataMap.put(GatewayTestConstants.LONGITUDE, "NOT_EMPTY");
//		expectedDataMap.put(GatewayTestConstants.LOCATION, "NOT_EMPTY");
		expectedDataMap.put(GatewayTestConstants.REGION, "NOT_EMPTY");
	}

	
	
}
	