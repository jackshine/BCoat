package com.elastica.tests.salesforce;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.elastica.common.CommonTest;
import com.elastica.gateway.GatewayTestConstants;
import com.elastica.listeners.Priority;
import com.elastica.listeners.RetryAnalyzer;
import com.elastica.logger.Logger;
import com.elastica.restClient.Client;
import com.elastica.tests.o365.CIQConstants;
import com.elastica.tests.o365.CiqUtils;

public class SalesforceCIQTests extends CommonTest{

	
	Map <String, Object> expectedDataMap = new HashMap<String, Object>();
	Map<String, String>policyDataMap= new HashMap<String, String>(); 
	String fromTime=null;
	SoftAssert softAssert = new SoftAssert();
	Client restClient= new Client();
	CiqUtils utils=new CiqUtils();
	WebDriver driver = null;
	@BeforeClass(alwaysRun= true)
	public void clearDataMap() throws Exception{
		fromTime=backend.getCurrentTime();
		expectedDataMap.clear();	
	}
	
	@Priority(1)
	@Test(groups ={"CIQ1","CIQ","REGRESSION","DOWNLOAD","UPLOAD","500mbCIQ","ENC_DEC_CIQ","CIQ_EXCEPTION","REACH_AGENT"})
	public void loginToCloudSocAppAndSetupSSO() throws Exception {
 		fromTime=backend.getCurrentTime();
 		driver = getWebDriver();
		Reporter.log("Started performing activities on saas app", true);
		login.loginCloudSocPortal(getWebDriver(), suiteData);
		Reporter.log("Finished login activities on cloudSoc", true);
	}
	
	@Priority(2)
	@Test(groups ={"CIQ1","CIQ","REGRESSION","DOWNLOAD","UPLOAD","500mbCIQ","ENC_DEC_CIQ","CIQ_EXCEPTION","REACH_AGENT"})  //,dependsOnMethods = { "loginToCloudSocAppAndSetupSSO" }
	public void ValidateLoginActivityEvent() throws Exception {
		Reporter.log("Verifying the login event", true);
		salesforceLogin.login(getWebDriver(), suiteData);
	//	policy.createCIQPolicy(GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData));

		
		Reporter.log("Login event verification successfull", true);
	}
	
	
	@Priority(3)
	@Test(groups ={"CIQ1"}) //,dependsOnMethods = { "o365_Test_01_ValidateLogingActivityEvent" }
	public void o365_Test_06_ValidateCIQ(String fileName) throws Exception {
		Reporter.log("Verifying CIQ for the file "+fileName, true);
		Reporter.log("Uploading the file in progress "+fileName, true);
		salesforceHomeAction.gotoFilesTab(driver);
		String file=System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+
		"resources"+File.separator+"ciq"+File.separator + "pii.txt";
		File uploadfile = new File(file);
		salesforceHomeAction.uploadFileFromFilesTab(driver, uploadfile, suiteData);
		
		salesforceHomeAction.gotoFilesTab(driver);
		
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "File pii.txt upload has risk(s) - PII");
		expectedDataMap.put(GatewayTestConstants.RISK, "PII, ContentIQ Violations");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "critical");
		//expectedDataMap.put("content_checks", "CIQ_FE_GW_DontDelete");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		policy.deletePolicy(GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData));
	   Reporter.log("File upload completed" +fileName, true);
	}
	
		
	
	@Priority(3)
	@Test(groups ={"CIQ","REGRESSION","UPLOAD","REACH_AGENT"}) //,dependsOnMethods = { "o365_Test_01_ValidateLogingActivityEvent" }
	public void salesForceUploadCIQFiles() throws Exception {

		//create new policies
		try{
		policy.createCIQPolicy(GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData));
		policy.createCIQPolicy(GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+"DictionaryProfile", suiteData, backend.getHeaders(suiteData),"GW_CIQ_DictionaryProfile","upload");
		policy.createCIQPolicy(GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+"GamblingPolicy", suiteData, backend.getHeaders(suiteData),"GW_CIQ_GamblingDictionaryProfile","upload");
		policy.createCIQPolicy(GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+"DrugsPolicy", suiteData, backend.getHeaders(suiteData),"GW_CIQ_DrugsDictionaryProfile","upload");
		
		}
		catch(Exception e) {

			 Reporter.log("error in policy creation", true);
		 }
		// upload risk files
		String [] ciqfiles={"pci.txt",  "pii.txt", "glba.txt", "hipaa.txt", "vba_macro.xls","Illegal_Drugs.txt","Gambling.txt","FERPA_BaileyDoxed.txt","Java.txt"}; //source_code.xls
		// got to files tab
		salesforceHomeAction.gotoFilesTab(driver);
		for(int i=0; i<ciqfiles.length;i++){
			try{
				
				String file=System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+
				"resources"+File.separator+"ciq"+File.separator + ciqfiles[i];
				File uploadfile = new File(file);
				salesforceHomeAction.uploadFileFromFilesTab(driver, uploadfile, suiteData);
				Thread.sleep(10000);
				}
			
		 catch(Exception e) {

			 Reporter.log("error in uploading  the file "+ciqfiles[i], true);
		 }
			
		}

		Thread.sleep(650000);
		policy.deletePolicy(GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData));
		policy.deletePolicy(GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+"DictionaryProfile", suiteData, backend.getHeaders(suiteData));
		policy.deletePolicy(GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+"DivorcePolicy", suiteData, backend.getHeaders(suiteData));
		policy.deletePolicy(GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+"GamblingPolicy", suiteData, backend.getHeaders(suiteData));
		policy.deletePolicy(GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+"DrugsPolicy", suiteData, backend.getHeaders(suiteData));	
		
		

		}

	
	@Priority(4)
	@Test(groups ={"REGRESSION","DOWNLOAD","REACH_AGENT"}) //,dependsOnMethods = { "o365_Test_01_ValidateLogingActivityEvent" }
	public void salesForceDownloadCIQfiles() throws Exception {
		try{
		//create new policies
		policy.createCIQPolicy(GatewayTestConstants.CIQ_DOWNLOAD_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData),"CIQ_FE_GW_DontDelete","download");
		policy.createCIQPolicy(GatewayTestConstants.CIQ_DOWNLOAD_POLICY_NAME+"DictionaryProfile", suiteData, backend.getHeaders(suiteData),"GW_CIQ_DictionaryProfile","download");
		
		policy.createCIQPolicy(GatewayTestConstants.CIQ_DOWNLOAD_POLICY_NAME+"GamblingPolicy", suiteData, backend.getHeaders(suiteData),"GW_CIQ_GamblingDictionaryProfile","download");
		policy.createCIQPolicy(GatewayTestConstants.CIQ_DOWNLOAD_POLICY_NAME+"DrugsPolicy", suiteData, backend.getHeaders(suiteData),"GW_CIQ_DrugsDictionaryProfile","download");
			}
		catch(Exception e) {
			 Reporter.log("error in policy creation ", true);
		 }
		// download files
		String [] ciqfiles={"pci.txt",  "pii.txt", "glba.txt", "hipaa.txt", "vba_macro.xls","Illegal_Drugs.txt","Gambling.txt","FERPA_BaileyDoxed.txt","Java.txt"}; //source_code.xls
		//got to download file
		salesforceHomeAction.gotoFilesTab(driver);
			for(int i=0; i<ciqfiles.length;i++){
			try{
				salesforceHomeAction.viewAndDownloadFileFromFilesTab(driver, ciqfiles[i]);
				}
			catch(Exception e) {

				Reporter.log("error in uploading  the file "+ciqfiles[i], true);
			}
			
	
		}

			//Delete old policies
		Thread.sleep(650000);
		policy.deletePolicy(GatewayTestConstants.CIQ_DOWNLOAD_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData));
		policy.deletePolicy(GatewayTestConstants.CIQ_DOWNLOAD_POLICY_NAME+"DictionaryProfile", suiteData, backend.getHeaders(suiteData));
		policy.deletePolicy(GatewayTestConstants.CIQ_DOWNLOAD_POLICY_NAME+"DivorcePolicy", suiteData, backend.getHeaders(suiteData));
		policy.deletePolicy(GatewayTestConstants.CIQ_DOWNLOAD_POLICY_NAME+"GamblingPolicy", suiteData, backend.getHeaders(suiteData));
		policy.deletePolicy(GatewayTestConstants.CIQ_DOWNLOAD_POLICY_NAME+"DrugsPolicy", suiteData, backend.getHeaders(suiteData));	
		
		
				
		Reporter.log("Log verification successful for method:"+Thread.currentThread().getStackTrace()[1].getMethodName(), true);
	}
	public String getSaasAppUserName(){
		return suiteData.getSaasAppUsername().replaceAll("@", "_");
	}
	

	
	// REgression validation******
	
	@Priority(5)
	@Test(groups ={"REGRESSION","UPLOAD","REACH_AGENT"},retryAnalyzer=RetryAnalyzer.class) //,dependsOnMethods = { "o365_Test_01_ValidateLogingActivityEvent" }
	public void salesForceValidatePCIUploadRisk() throws Exception {
		Reporter.log("Verifying CIQ Upload activity event ", true);
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "File pci.txt upload has risk(s) - ContentIQ Violations, PCI");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Content Inspection");
		expectedDataMap.put(GatewayTestConstants.RISK, "ContentIQ Violations, PCI");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "critical");
		expectedDataMap.put("content_checks", "CIQ_FE_GW_DontDelete");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("Verifying CIQ Upload activity event  successfull", true);
	}
	@Priority(5)
	@Test(groups ={"REGRESSION","DOWNLOAD","REACH_AGENT"}, retryAnalyzer=RetryAnalyzer.class) //,dependsOnMethods = { "o365_Test_01_ValidateLogingActivityEvent" }
	public void salesForceValidatePCIDownloadRisk() throws Exception {
		Reporter.log("Verifying CIQ download activity event ", true);
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "File pci.txt download has risk(s) - ContentIQ Violations, PCI");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Content Inspection");
		expectedDataMap.put(GatewayTestConstants.RISK, "ContentIQ Violations, PCI");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "critical");
		expectedDataMap.put("content_checks", "CIQ_FE_GW_DontDelete");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("Verifying CIQ Upload activity event  successfull", true);
	}

	@Priority(5)
	@Test(groups ={"REGRESSION","UPLOAD","REACH_AGENT"},retryAnalyzer=RetryAnalyzer.class) //,dependsOnMethods = { "o365_Test_01_ValidateLogingActivityEvent" }
	public void salesForceValidateHIPPAUploadRisk() throws Exception {
		Reporter.log("Verifyinghipaa.txt upload activity event ", true);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "File hipaa.txt upload has risk(s) - PII, ContentIQ Violations, HIPAA");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Content Inspection");
		expectedDataMap.put(GatewayTestConstants.RISK, "PII, ContentIQ Violations");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "critical");
		expectedDataMap.put("content_checks", "CIQ_FE_GW_DontDelete");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("hipaa.txt upload actvity event verification successfull", true);
	}
	
	@Priority(5)
	@Test(groups ={"REGRESSION","DOWNLOAD","REACH_AGENT"}, retryAnalyzer=RetryAnalyzer.class) //,dependsOnMethods = { "o365_Test_01_ValidateLogingActivityEvent" }
	public void salesForceValidateHIPPADownloadRisk() throws Exception {
		Reporter.log("hipaa.txt download activity event ", true);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "File hipaa.txt download has risk(s) - PII, ContentIQ Violations, HIPAA");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Content Inspection");
		expectedDataMap.put(GatewayTestConstants.RISK, "PII, ContentIQ Violations");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "critical");
		expectedDataMap.put("content_checks", "CIQ_FE_GW_DontDelete");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("hipaa.txt download actvity event verification successfull", true);
	}
	@Priority(5)
	@Test(groups ={"REGRESSION","UPLOAD","REACH_AGENT"},retryAnalyzer=RetryAnalyzer.class) //,dependsOnMethods = { "o365_Test_01_ValidateLogingActivityEvent" }
	public void salesForceValidatePIIUploadRisk() throws Exception {
		Logger.info("==================================================================================");
		Logger.info("CIQ Log Verification started");
		Logger.info("==================================================================================");
		
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Content Inspection");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "File pii.txt upload has risk(s) - PII, ContentIQ Violations");
		expectedDataMap.put(GatewayTestConstants.RISK, "PII, ContentIQ Violations");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "critical");
		expectedDataMap.put("content_checks", "CIQ_FE_GW_DontDelete");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");

		Logger.info("==================================================================================");
		Logger.info("CIQ Log Verification Successfull");
		Logger.info("==================================================================================");
		}
	
	@Priority(5)
	@Test(groups ={"REGRESSION","DOWNLOAD","REACH_AGENT"}, retryAnalyzer=RetryAnalyzer.class) //,dependsOnMethods = { "o365_Test_01_ValidateLogingActivityEvent" }
	public void salesForceValidatePIIDownloadRisk() throws Exception {
		Logger.info("==================================================================================");
		Logger.info("CIQ Log Verification started");
		Logger.info("==================================================================================");
		
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Content Inspection");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "File pii.txt download has risk(s) - PII, ContentIQ Violations");
		expectedDataMap.put(GatewayTestConstants.RISK, "PII, ContentIQ Violations");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "critical");
		expectedDataMap.put("content_checks", "CIQ_FE_GW_DontDelete");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");

		Logger.info("==================================================================================");
		Logger.info("CIQ Log Verification Successfull");
		Logger.info("==================================================================================");
		}
	
	@Priority(5)
	@Test(groups ={"REGRESSION","DOWNLOAD","REACH_AGENT"}, retryAnalyzer=RetryAnalyzer.class) //,dependsOnMethods = { "o365_Test_01_ValidateLogingActivityEvent" }
	public void salesForceValidateSourceCodeDownloadCI() throws Exception {
		Logger.info("==================================================================================");
		Logger.info("CIQ Log Verification started");
		Logger.info("==================================================================================");
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Content Inspection");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "File java.txt download has risk(s) - ContentIQ Violations");
		expectedDataMap.put(GatewayTestConstants.RISK, "ContentIQ Violations");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "critical");
		expectedDataMap.put("content_checks", "GW_CIQ_DictionaryProfile");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");

		Logger.info("==================================================================================");
		Logger.info("CIQ Log Verification Successfull");
		Logger.info("==================================================================================");
		}
	@Priority(5)
	@Test(groups ={"REGRESSION","UPLOAD","REACH_AGENT"},retryAnalyzer=RetryAnalyzer.class) //,dependsOnMethods = { "o365_Test_01_ValidateLogingActivityEvent" }
	public void salesForceValidateUploadSourceCode() throws Exception {
		Logger.info("==================================================================================");
		Logger.info("CIQ Log Verification started");
		Logger.info("==================================================================================");
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Content Inspection");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "File java.txt upload has risk(s) - ContentIQ Violations");
		expectedDataMap.put(GatewayTestConstants.RISK, "ContentIQ Violations");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "critical");
		expectedDataMap.put("content_checks", "GW_CIQ_DictionaryProfile");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");

		Logger.info("==================================================================================");
		Logger.info("CIQ Log Verification Successfull");
		Logger.info("==================================================================================");
		}
	
	@Priority(6)
	@Test(groups ={"REGRESSION","DOWNLOAD","REACH_AGENT"}, retryAnalyzer=RetryAnalyzer.class) //,dependsOnMethods = { "o365_Test_01_ValidateLogingActivityEvent" }
	public void salesForceValidateVBA_MACRODownloadRisk() throws Exception {
		Reporter.log("Verifying CIQ download activity event ", true);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "File vba_macro.xls download has risk(s) - ContentIQ Violations, VBA Macros");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Content Inspection");
		expectedDataMap.put(GatewayTestConstants.RISK, "ContentIQ Violations, VBA Macros");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "critical");
		expectedDataMap.put("content_checks", "CIQ_FE_GW_DontDelete");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("Verifying CIQ download activity event  successfull", true);
	}
	@Priority(6)
	@Test(groups ={"REGRESSION","UPLOAD","REACH_AGENT"},retryAnalyzer=RetryAnalyzer.class) //,dependsOnMethods = { "o365_Test_01_ValidateLogingActivityEvent" }
	public void salesForceValidateVBA_MACROUploadRisk() throws Exception {
		Reporter.log("Verifying CIQ Upload activity event ", true);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "File vba_macro.xls upload has risk(s) - ContentIQ Violations, VBA Macros");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Content Inspection");
		expectedDataMap.put(GatewayTestConstants.RISK, "ContentIQ Violations, VBA Macros");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "critical");
		expectedDataMap.put("content_checks", "CIQ_FE_GW_DontDelete");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("Verifying CIQ Upload activity event  successfull", true);
	}
	@Priority(6)
	@Test(groups ={"REGRESSION","UPLOAD","REACH_AGENT"},retryAnalyzer=RetryAnalyzer.class) //,dependsOnMethods = { "o365_Test_01_ValidateLogingActivityEvent" }
	public void salesForceValidateFERPAUploadRisk() throws Exception {
		Reporter.log("Verifying CIQ Upload activity event ", true);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "File ferpa_baileydoxed.txt upload has risk(s) - PII, ContentIQ Violations, FERPA");
		expectedDataMap.put(GatewayTestConstants.RISK, "PII, ContentIQ Violations, FERPA");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "critical");
		expectedDataMap.put("content_checks", "CIQ_FE_GW_DontDelete");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("Verifying CIQ Upload activity event  successfull", true);
	}
	@Priority(6)
	@Test(groups ={"REGRESSION","DOWNLOAD","REACH_AGENT"}, retryAnalyzer=RetryAnalyzer.class) //,dependsOnMethods = { "o365_Test_01_ValidateLogingActivityEvent" }
	public void salesForceValidateFERPADownloadRisk() throws Exception {
		Reporter.log("Verifying CIQ download activity event ", true);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "File ferpa_baileydoxed.txt download has risk(s) - PII, ContentIQ Violations, FERPA");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Content Inspection");
		expectedDataMap.put(GatewayTestConstants.RISK, "PII, ContentIQ Violations, FERPA");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "critical");
		expectedDataMap.put("content_checks", "CIQ_FE_GW_DontDelete");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("Verifying CIQ download activity event  successfull", true);
	}
	@Priority(6)
	@Test(groups ={"REGRESSION","DOWNLOAD","REACH_AGENT"},retryAnalyzer=RetryAnalyzer.class) //,dependsOnMethods = { "o365_Test_01_ValidateLogingActivityEvent" }
	public void salesForceValidateGLBADownloadCIQ() throws Exception {
		Reporter.log("glba.txt download activity event with policy", true);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "File glba.txt download has risk(s) - ContentIQ Violations, GLBA");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Content Inspection");
		expectedDataMap.put(GatewayTestConstants.RISK, "ContentIQ Violations, GLBA");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "critical");
		expectedDataMap.put("content_checks", "CIQ_FE_GW_DontDelete");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("glba.txt download actvity event verification successfull", true);
	}
	
	@Priority(6)
	@Test(groups ={"REGRESSION","UPLOAD","REACH_AGENT"},retryAnalyzer=RetryAnalyzer.class) //,dependsOnMethods = { "o365_Test_01_ValidateLogingActivityEvent" }
	public void salesForceValidateGLBAUpload_CIQ() throws Exception {
		Reporter.log("Verifying glba.txt upload activity event with policy", true);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "File glba.txt upload has risk(s) - ContentIQ Violations, GLBA");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Content Inspection");
		expectedDataMap.put(GatewayTestConstants.RISK, "ContentIQ Violations, GLBA");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "critical");
		expectedDataMap.put("content_checks", "CIQ_FE_GW_DontDelete");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("glba.txt upload actvity event verification successfull", true);
	}

		
	@Priority(8)
	@Test(groups ={"REGRESSION","UPLOAD","REACH_AGENT"},retryAnalyzer=RetryAnalyzer.class) 
	public void salesForceValidateDrugsUploadCI() throws Exception {
		Reporter.log("Verifying CIQ Upload activity event ", true);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "File illegal_drugs.txt upload has risk(s) - ContentIQ Violations");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Content Inspection");
		expectedDataMap.put(GatewayTestConstants.RISK, "ContentIQ Violations");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "critical");
		expectedDataMap.put("content_checks", "GW_CIQ_DrugsDictionaryProfile");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("Verifying CIQ Upload activity event  successfull", true);
	}
	@Priority(8)
	@Test(groups ={"REGRESSION","DOWNLOAD","REACH_AGENT"}, retryAnalyzer=RetryAnalyzer.class) 
	public void salesForceValidateDrugsDownloadCI() throws Exception {
		Reporter.log("Verifying CIQ download activity event ", true);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "File illegal_drugs.txt download has risk(s) - ContentIQ Violations");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Content Inspection");
		expectedDataMap.put(GatewayTestConstants.RISK, "ContentIQ Violations");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "critical");
		expectedDataMap.put("content_checks", "GW_CIQ_DrugsDictionaryProfile");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("Verifying CIQ download activity event  successfull", true);
	}
	@Priority(8)
	@Test(groups ={"REGRESSION","UPLOAD","REACH_AGENT"},retryAnalyzer=RetryAnalyzer.class) 
	public void salesForceValidateGamblingUploadCI() throws Exception {
		Reporter.log("Verifying CIQ Upload activity event ", true);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "File gambling.txt upload has risk(s) - ContentIQ Violations");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Content Inspection");
		expectedDataMap.put(GatewayTestConstants.RISK, "ContentIQ Violations");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "critical");
		expectedDataMap.put("content_checks", "GW_CIQ_GamblingDictionaryProfile");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("Verifying CIQ Upload activity event  successfull", true);
	}
	@Priority(8)
	@Test(groups ={"REGRESSION","DOWNLOAD","REACH_AGENT"}, retryAnalyzer=RetryAnalyzer.class) 
	public void salesForceValidateGamblingDownloadCI() throws Exception {
		Reporter.log("Verifying CIQ download activity event ", true);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "File gambling.txt download has risk(s) - ContentIQ Violations");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Content Inspection");
		expectedDataMap.put(GatewayTestConstants.RISK, "ContentIQ Violations");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "critical");
		expectedDataMap.put("content_checks", "GW_CIQ_GamblingDictionaryProfile");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("Verifying CIQ download activity event  successfull", true);
	}
	

		

	@AfterTest(alwaysRun= true)
	public void Cleanup() throws Exception {

		String [] ciqfiles={"pci.txt",  "pii.txt", "glba.txt", "hipaa.txt", "vba_macro.xls","Illegal_Drugs.txt","Gambling.txt","FERPA_BaileyDoxed.txt","Java.txt"}; //source_code.xls
		//got to download file
		salesforceHomeAction.gotoFilesTab(driver);
		for(int i=0;i<ciqfiles.length;i++)
		{
			salesforceHomeAction.deleteFileFromFilesTab(driver, ciqfiles[i]);
		}

		policy.deletePolicy(GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+"HDFCPolicy", suiteData, backend.getHeaders(suiteData));
		policy.deletePolicy(GatewayTestConstants.CIQ_DOWNLOAD_POLICY_NAME+"HDFCPolicy", suiteData, backend.getHeaders(suiteData));
		getWebDriver().close();
		Reporter.log("Finished cleanup activities on cloudSoc", true);
	}
}
