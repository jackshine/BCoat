package com.elastica.tests.box;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.elastica.common.CommonTest;
import com.elastica.common.GWCommonTest;
import com.elastica.gateway.GatewayTestConstants;
import com.elastica.gateway.ProtectTestConstants;
import com.elastica.listeners.Priority;
import com.elastica.listeners.RetryAnalyzer;
import com.elastica.logger.Logger;
import com.elastica.tests.o365.CIQConstants;
import com.elastica.tests.o365.CiqUtils;

public class BoxTestCIQ extends CommonTest {

	Map <String, Object> expectedDataMap = new HashMap<String, Object>();
	Map<String, String>policyDataMap= new HashMap<String, String>();
	CiqUtils utils=new CiqUtils();
	String fromTime= "";
	String filePath = GatewayTestConstants.BOX_ORIGINAL_FILE_PATH;
	SoftAssert softAsst = new SoftAssert();
	GWCommonTest gwtest=new GWCommonTest();
	
	@BeforeClass(alwaysRun= true)
	public void clearDataMap() throws Exception{
		fromTime=backend.getCurrentTime();
		expectedDataMap.clear();	
		//SuiteData suiteData;
		Map<String,String> fileDetails=new HashMap<String,String>();
		fileDetails.put("fileName", "divorce.txt");
		fileDetails.put("filePath", "FEATLE/src/test/resources/ciq/contentType/divorce.txt");
		List<String> keywords= new ArrayList<String>();
		keywords.add("Divorce");
	   
		// create ciq risk profile
		utils.createCIQProfile( CIQConstants.ZERO,suiteData, "GW_CIQ_Dictionary","GW_CIQ_Dictionary",CIQConstants.SOURCE_RISK,CIQConstants.RISKVALUE);
		utils.createCIQProfile(CIQConstants.ONE, suiteData, "GW_CIQ_Divorce","GW_CIQ_Divorce",CIQConstants.SOURCE_DIVORCE,CIQConstants.VALUE_DIVORCE);
		utils.createCIQProfile(CIQConstants.ONE, suiteData, "GW_CIQ_GamblingDictionaryProfile","GW_CIQ_GamblingDictionaryProfile",CIQConstants.SOURCE_DRUG_GAMBLE,CIQConstants.VALUE_GAMBLE);
		utils.createCIQProfile(CIQConstants.ONE, suiteData, "GW_CIQ_DrugsDictionaryProfile","GW_CIQ_DrugsDictionaryProfile",CIQConstants.SOURCE_DRUG_GAMBLE,CIQConstants.VALUE_DRUG);
		utils.createCIQProfile(CIQConstants.ONE,suiteData, "GW_CIQ_UssnLiscencePlate","GW_CIQ_UssnLiscencePlate",CIQConstants.SOURCE_USLISCENCE,CIQConstants.VALUE_US_LISCENCE_PLT);
		utils.createCIQProfile(CIQConstants.ONE,suiteData, "GW_CIQ_HDFCProfile","GW_CIQ_HDFCProfile",CIQConstants.SOURCE_HDFC,CIQConstants.VALUE_HDFC);		
		Logger.info("Creating CIQ profile with only content is completed");		
		//createCIQProfileWithContentTypes( restClient,suiteData,ciqProfileName);
		//Delete old policies
		policy.deletePolicy(suiteData.getSaasAppName()+GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData));
		policy.deletePolicy(suiteData.getSaasAppName()+GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+"DictionaryProfile", suiteData, backend.getHeaders(suiteData));
		policy.deletePolicy(suiteData.getSaasAppName()+GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+"DivorcePolicy", suiteData, backend.getHeaders(suiteData));
		policy.deletePolicy(suiteData.getSaasAppName()+GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+"GamblingPolicy", suiteData, backend.getHeaders(suiteData));
		policy.deletePolicy(suiteData.getSaasAppName()+GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+"DrugsPolicy", suiteData, backend.getHeaders(suiteData));	
		policy.deletePolicy(suiteData.getSaasAppName()+GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+"USLincencPlate", suiteData, backend.getHeaders(suiteData));
		//Delete old policies
		policy.deletePolicy(suiteData.getSaasAppName()+GatewayTestConstants.CIQ_DOWNLOAD_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData));
		policy.deletePolicy(suiteData.getSaasAppName()+GatewayTestConstants.CIQ_DOWNLOAD_POLICY_NAME+"DictionaryProfile", suiteData, backend.getHeaders(suiteData));
		policy.deletePolicy(suiteData.getSaasAppName()+GatewayTestConstants.CIQ_DOWNLOAD_POLICY_NAME+"DivorcePolicy", suiteData, backend.getHeaders(suiteData));
		policy.deletePolicy(suiteData.getSaasAppName()+GatewayTestConstants.CIQ_DOWNLOAD_POLICY_NAME+"GamblingPolicy", suiteData, backend.getHeaders(suiteData));
		policy.deletePolicy(suiteData.getSaasAppName()+GatewayTestConstants.CIQ_DOWNLOAD_POLICY_NAME+"DrugsPolicy", suiteData, backend.getHeaders(suiteData));	
		policy.deletePolicy(suiteData.getSaasAppName()+GatewayTestConstants.CIQ_DOWNLOAD_POLICY_NAME+"USLincencPlate", suiteData, backend.getHeaders(suiteData));	

		
	}
	
	@Priority(1)
	@Test(groups ={"SANITY","REGRESSION","P1","P2","CIQ"}, retryAnalyzer=RetryAnalyzer.class)
	public void loginToCloudSocAppAndSetupSSO() throws Exception {
		Logger.info(" delete any old encryption policy");
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME+suiteData.getSaasAppUsername(), suiteData, backend.getHeaders(suiteData));		
		fromTime =new org.joda.time.DateTime( org.joda.time.DateTimeZone.UTC ).minusMinutes(1).toString();
		Logger.info("Start Login to cloudSoc");
		
		login.loginCloudSocPortal(getWebDriver(), suiteData);
		Logger.info("Completed Login to cloudSoc");
		box.login(getWebDriver(), suiteData);
	}
	
	@Priority(1)
	@Test(groups ={"SANITY","REGRESSION","P1","P2","CIQ","REACH_AGENT"}) 
	public void validatePolicyCreation() throws Exception {
		Logger.info("Verifying the Login Event");
		
		box.login(getWebDriver(), suiteData);
		policy.createCIQPolicy(suiteData.getSaasAppName()+GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+"DivorcePolicy", suiteData, backend.getHeaders(suiteData),"GW_CIQ_Divorce","upload");	
		policy.createCIQPolicy(suiteData.getSaasAppName()+GatewayTestConstants.CIQ_DOWNLOAD_POLICY_NAME+"DivorcePolicy", suiteData, backend.getHeaders(suiteData),"GW_CIQ_Divorce","download");	
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Login");
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Session");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User logged in"); 
		expectedDataMap.put(GatewayTestConstants.REFERER_URI, "NOT_EMPTY");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("Log verification successful for method:"+Thread.currentThread().getStackTrace()[1].getMethodName(), true);

	}
	
	
	@Priority(2)
	@Test(groups ={"SANITY","REGRESSION","P1","CIQ","REACH_AGENT"})
	public void uploadCIQFiles() throws Exception {
		Logger.info("Verifying the File Upload Event");
		Thread.sleep(5000);
		//create new policies
		
		Logger.info("Start -- Creating new upload policies");
		policy.createCIQPolicy(suiteData.getSaasAppName()+GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData));
		policy.createCIQPolicy(suiteData.getSaasAppName()+GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+"DictionaryProfile", suiteData, backend.getHeaders(suiteData),"GW_CIQ_Dictionary","upload");
		policy.createCIQPolicy(suiteData.getSaasAppName()+GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+"HDFCPolicy", suiteData, backend.getHeaders(suiteData),"GW_CIQ_HDFCProfile","upload");
		policy.createCIQPolicy(suiteData.getSaasAppName()+GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+"GamblingPolicy", suiteData, backend.getHeaders(suiteData),"GW_CIQ_GamblingDictionaryProfile","upload");
		policy.createCIQPolicy(suiteData.getSaasAppName()+GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+"DrugsPolicy", suiteData, backend.getHeaders(suiteData),"GW_CIQ_Drugs_GamblingDictionaryProfile","upload");
		//policy.createCIQPolicy(GatewayTestConstants.CIQ_POLICY_NAME+"CombinedPolicy", suiteData, backend.getHeaders(suiteData),"GW_CIQ_DictionaryProfile,GW_CIQ_Divorce,GW_CIQ_GamblingDictionaryProfile,GW_CIQ_Drugs_GamblingDictionaryProfile,GW_CIQ_UssnLiscencePlate","upload,download");
		policy.createCIQPolicy(suiteData.getSaasAppName()+GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+"USLincencPlate", suiteData, backend.getHeaders(suiteData),"GW_CIQ_UssnLiscencePlate","upload");
		Logger.info("Completed -- Creating new upload policies");
		box.gotoHomePage(getWebDriver(),CIQConstants.BOX_HOME);
		box.openItem(getWebDriver(),"RISK",15);
		box.deleteOpenedFolder(getWebDriver());
		Thread.sleep(15000);
		box.createFolder(getWebDriver(),"RISK");
		Thread.sleep(15000);
		box.openItem(getWebDriver(),"RISK",15);
		String [] ciqfiles={"pci.txt", "pii.txt", "glba.txt", "hipaa.txt","FERPA_BaileyDoxed.txt","Gambling.txt","Illegal_Drugs.txt","US_Social_Security_Number.txt", "vba_macro.xls"};
		String absLocation=System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+
				"resources"+File.separator+"ciq"+File.separator;// + ciqfiles[i]
		for(int i=0; i<ciqfiles.length;i++){

			try
			{
				Logger.info(" performaing  File Upload operation for file "+ciqfiles[i]);
				box.uploadFile(getWebDriver(),absLocation+ciqfiles[i] , 15);
			}
			catch(Exception e) {
				
				Logger.info("Error in Upload of file "+ciqfiles[i]);
			}
			Thread.sleep(5000);
		}

		box.gotoHomePage(getWebDriver(),CIQConstants.BOX_HOME);
		//box.gotoHomePage(getWebDriver(),suiteData);
		box.openItem(getWebDriver(),"CI",15);
		box.deleteOpenedFolder(getWebDriver());
		Thread.sleep(15000);
		box.createFolder(getWebDriver(),"CI");
		Thread.sleep(15000);
		box.openItem(getWebDriver(),"CI",15);
		// upload contentIQ  file
		String [] contentfiles={"hdfc.txt","divorce.txt","US_License_Plate_Number.txt","health.txt","Java.txt","legal.html","design.pdf"};
		absLocation=System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+
				"resources"+File.separator+"ciq"+File.separator+"contentType"+File.separator;// + ciqfiles[i]

		for(int i=0; i<contentfiles.length;i++){
			try
			{
				Logger.info("performaing  File Upload operation for file "+contentfiles[i]);
				box.uploadFile(getWebDriver(),absLocation +contentfiles[i] , 15);
				Thread.sleep(5000);
			}
			catch(Exception e) {
				//Logger.info(" performaing  File Upload operation for file "+ciqfiles[i]);
				Logger.info("Error in Upload of file "+contentfiles[i]);
			}
			Thread.sleep(5000);	
		}
		Logger.info("Delete old policies ");
		Thread.sleep(10000);	
		policy.deletePolicy(suiteData.getSaasAppName()+GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData));
		policy.deletePolicy(suiteData.getSaasAppName()+GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+"DictionaryProfile", suiteData, backend.getHeaders(suiteData));
		policy.deletePolicy(suiteData.getSaasAppName()+GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+"DivorcePolicy", suiteData, backend.getHeaders(suiteData));
		policy.deletePolicy(suiteData.getSaasAppName()+GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+"GamblingPolicy", suiteData, backend.getHeaders(suiteData));
		policy.deletePolicy(suiteData.getSaasAppName()+GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+"DrugsPolicy", suiteData, backend.getHeaders(suiteData));	
		policy.deletePolicy(suiteData.getSaasAppName()+GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+"USLincencPlate", suiteData, backend.getHeaders(suiteData));
		policy.deletePolicy(suiteData.getSaasAppName()+GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+"HDFCPolicy", suiteData, backend.getHeaders(suiteData));
		Logger.info("completed --Delete old policies ");

	}
	
	@Priority(4)
	@Test(groups ={"SANITY","REGRESSION","P1","CIQ","REACH_AGENT","P2"})
	public void downloadCIQFiles() throws Exception {
		Logger.info("Verifying the File Download Event");
			Thread.sleep(5000);
			
		//create new policies
		policy.createCIQPolicy(suiteData.getSaasAppName()+GatewayTestConstants.CIQ_DOWNLOAD_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData),"CIQ_FE_GW_DontDelete","download");
		policy.createCIQPolicy(suiteData.getSaasAppName()+GatewayTestConstants.CIQ_DOWNLOAD_POLICY_NAME+"DictionaryProfile", suiteData, backend.getHeaders(suiteData),"GW_CIQ_Dictionary","download");
		policy.createCIQPolicy(suiteData.getSaasAppName()+GatewayTestConstants.CIQ_DOWNLOAD_POLICY_NAME+"HDFCPolicy", suiteData, backend.getHeaders(suiteData),"GW_CIQ_HDFCProfile","download");
		policy.createCIQPolicy(suiteData.getSaasAppName()+GatewayTestConstants.CIQ_DOWNLOAD_POLICY_NAME+"GamblingPolicy", suiteData, backend.getHeaders(suiteData),"GW_CIQ_GamblingDictionaryProfile","download");
		policy.createCIQPolicy(suiteData.getSaasAppName()+GatewayTestConstants.CIQ_DOWNLOAD_POLICY_NAME+"DrugsPolicy", suiteData, backend.getHeaders(suiteData),"GW_CIQ_Drugs_GamblingDictionaryProfile","download");
		//policy.createCIQPolicy(GatewayTestConstants.CIQ_POLICY_NAME+"CombinedPolicy", suiteData, backend.getHeaders(suiteData),"GW_CIQ_DictionaryProfile,GW_CIQ_Divorce,GW_CIQ_GamblingDictionaryProfile,GW_CIQ_Drugs_GamblingDictionaryProfile,GW_CIQ_UssnLiscencePlate","upload,download");
		policy.createCIQPolicy(suiteData.getSaasAppName()+GatewayTestConstants.CIQ_DOWNLOAD_POLICY_NAME+"USLincencPlate", suiteData, backend.getHeaders(suiteData),"GW_CIQ_UssnLiscencePlate","download");
		box.gotoHomePage(getWebDriver(),CIQConstants.BOX_HOME);

		box.openItem(getWebDriver(),"RISK",15);
		//;openItem(getWebDriver(),"CIQ",15);
		String [] ciqfiles={"pci.txt", "pii.txt", "glba.txt", "hipaa.txt","FERPA_BaileyDoxed.txt","Gambling.txt","Illegal_Drugs.txt","US_Social_Security_Number.txt", "vba_macro.xls"};
		String absLocation=System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+
				"resources"+File.separator+"ciq"+File.separator;// + ciqfiles[i]
		for(int i=0; i<ciqfiles.length;i++){
			Logger.info(" performaing  File Download operation for file "+ciqfiles[i]);
			try
			{
				Logger.info("downloading file " + ciqfiles[i]);
			box.downloadSelectedItem(getWebDriver(), ciqfiles[i], 15,suiteData.getBrowser());
			}
		 catch(Exception e) {
			 Logger.info("Error in login " + e.getMessage());
		 }
			Thread.sleep(5000);
		}
		
		box.gotoHomePage(getWebDriver(),CIQConstants.BOX_HOME);

		box.openItem(getWebDriver(),"CI",15);
		// upload contentIQ  file
		String [] contentfiles={"hdfc.txt","divorce.txt","US_License_Plate_Number.txt","health.txt","Java.txt","legal.html","design.pdf"};
		absLocation=System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+
				"resources"+File.separator+"ciq"+File.separator+"contentType"+File.separator;// + ciqfiles[i]

		for(int i=0; i<contentfiles.length;i++){
			Logger.info("performaing  File Download operation for file "+contentfiles[i]);
		//	box.uploadFile(getWebDriver(),absLocation +contentfiles[i] , 15);
			Logger.info(" performaing  File Download operation for file "+contentfiles[i]);
			try
			{
			Logger.info("downloading file " + contentfiles[i]);
			box.downloadSelectedItem(getWebDriver(), contentfiles[i], 15,suiteData.getBrowser());
			}
		 catch(Exception e) {
			 Logger.info("Error in login " + e.getMessage());
		 }
			Thread.sleep(15000);
		}
		// delete old policies
		Thread.sleep(100000);
		policy.deletePolicy(suiteData.getSaasAppName()+GatewayTestConstants.CIQ_DOWNLOAD_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData));
		policy.deletePolicy(suiteData.getSaasAppName()+GatewayTestConstants.CIQ_DOWNLOAD_POLICY_NAME+"DictionaryProfile", suiteData, backend.getHeaders(suiteData));
		policy.deletePolicy(suiteData.getSaasAppName()+GatewayTestConstants.CIQ_DOWNLOAD_POLICY_NAME+"DivorcePolicy", suiteData, backend.getHeaders(suiteData));
		policy.deletePolicy(suiteData.getSaasAppName()+GatewayTestConstants.CIQ_DOWNLOAD_POLICY_NAME+"GamblingPolicy", suiteData, backend.getHeaders(suiteData));
		policy.deletePolicy(suiteData.getSaasAppName()+GatewayTestConstants.CIQ_DOWNLOAD_POLICY_NAME+"DrugsPolicy", suiteData, backend.getHeaders(suiteData));	
		policy.deletePolicy(suiteData.getSaasAppName()+GatewayTestConstants.CIQ_DOWNLOAD_POLICY_NAME+"USLincencPlate", suiteData, backend.getHeaders(suiteData));
		policy.deletePolicy(suiteData.getSaasAppName()+GatewayTestConstants.CIQ_DOWNLOAD_POLICY_NAME+"HDFCPolicy", suiteData, backend.getHeaders(suiteData));
	
		
		Reporter.log("Log verification successful for method:"+Thread.currentThread().getStackTrace()[1].getMethodName(), true);

	}
	
	@Priority(5)
	@Test(groups ={"CIQ", "REACH_AGENT","SANITY"},retryAnalyzer=RetryAnalyzer.class) 
	public void boxValidatePCIUploadRisk() throws Exception {
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
	@Test(groups ={"CIQ", "REACH_AGENT","SANITY"},retryAnalyzer=RetryAnalyzer.class) 
	public void boxValidatePCIDownloadRisk() throws Exception {
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
	@Test(groups ={"CIQ", "REACH_AGENT","SANITY"},retryAnalyzer=RetryAnalyzer.class) 
	public void boxValidateHIPPAUploadRisk() throws Exception {
		Reporter.log("Verifying CIQ Upload activity event ", true);
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "File hipaa.txt upload has risk(s) - PII, ContentIQ Violations, HIPAA");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Content Inspection");
		expectedDataMap.put(GatewayTestConstants.RISK, "PII, ContentIQ Violations");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "critical");
		expectedDataMap.put("content_checks", "CIQ_FE_GW_DontDelete");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("Decrypt download actvity event verification successfull", true);
	}
	
	@Priority(5)
	@Test(groups ={"CIQ", "REACH_AGENT","SANITY"},retryAnalyzer=RetryAnalyzer.class) 
	public void boxValidateHIPPADownloadRisk() throws Exception {
		Reporter.log("Verifying CIQ Upload activity event ", true);
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "File hipaa.txt download has risk(s) - PII, ContentIQ Violations, HIPAA");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Content Inspection");
		expectedDataMap.put(GatewayTestConstants.RISK, "PII, ContentIQ Violations");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "critical");
		expectedDataMap.put("content_checks", "CIQ_FE_GW_DontDelete");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("Decrypt download actvity event verification successfull", true);
	}
	@Priority(5)
	@Test(groups ={"CIQ", "REACH_AGENT","SANITY"},retryAnalyzer=RetryAnalyzer.class) 
	public void boxValidatePIIUploadRiskQ() throws Exception {
		Logger.info("==================================================================================");
		Logger.info("CIQ Log Verification started");
		Logger.info("==================================================================================");
		expectedDataMap.clear();
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
	@Test(groups ={"CIQ", "REACH_AGENT","SANITY"},retryAnalyzer=RetryAnalyzer.class) 
	public void boxValidatePIIDownloadRiskQ() throws Exception {
		Logger.info("==================================================================================");
		Logger.info("CIQ Log Verification started");
		Logger.info("==================================================================================");
		expectedDataMap.clear();
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
	@Test(groups ={"CIQ", "REACH_AGENT"},retryAnalyzer=RetryAnalyzer.class) 
	public void boxValidateSourceCodeDownloadCI() throws Exception {
		Logger.info("==================================================================================");
		Logger.info("CIQ Log Verification started");
		Logger.info("==================================================================================");
		
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Content Inspection");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "File java.txt download has risk(s) - ContentIQ Violations");
		expectedDataMap.put(GatewayTestConstants.RISK, "ContentIQ Violations");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "critical");
	//	expectedDataMap.remove(GatewayTestConstants.USER_NAME);
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");

		Logger.info("==================================================================================");
		Logger.info("CIQ Log Verification Successfull");
		Logger.info("==================================================================================");
		}
	@Priority(5)
	@Test(groups ={"CIQ", "REACH_AGENT"},retryAnalyzer=RetryAnalyzer.class) 
	public void boxValidateUploadSourceCode() throws Exception {
		Logger.info("==================================================================================");
		Logger.info("CIQ Log Verification started");
		Logger.info("==================================================================================");
		
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Content Inspection");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "File java.txt upload has risk(s) - ContentIQ Violations");
		expectedDataMap.put(GatewayTestConstants.RISK, "ContentIQ Violations");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "critical");
	//	expectedDataMap.remove(GatewayTestConstants.USER_NAME);
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");

		Logger.info("==================================================================================");
		Logger.info("CIQ Log Verification Successfull");
		Logger.info("==================================================================================");
		}
	
	@Priority(6)
	@Test(groups ={"CIQ", "REACH_AGENT","SANITY"},retryAnalyzer=RetryAnalyzer.class) 
	public void boxValidateVBA_MACRODownloadRisk() throws Exception {
		Reporter.log("Verifying CIQ download activity event ", true);
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "File vba_macro.xls download has risk(s) - ContentIQ Violations, VBA Macros");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Content Inspection");
		expectedDataMap.put(GatewayTestConstants.RISK, "ContentIQ Violations, VBA Macros");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "critical");
		expectedDataMap.put("content_checks", "CIQ_FE_GW_DontDelete");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("Verifying CIQ download activity event  successfull", true);
	}
	@Priority(6)
	@Test(groups ={"CIQ", "REACH_AGENT","SANITY"},retryAnalyzer=RetryAnalyzer.class) 
	public void boxValidateVBA_MACROUploadRisk() throws Exception {
		Reporter.log("Verifying CIQ Upload activity event ", true);
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "File vba_macro.xls upload has risk(s) - ContentIQ Violations, VBA Macros");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Content Inspection");
		expectedDataMap.put(GatewayTestConstants.RISK, "ContentIQ Violations, VBA Macros");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "critical");
		expectedDataMap.put("content_checks", "CIQ_FE_GW_DontDelete");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("Verifying CIQ Upload activity event  successfull", true);
	}
	@Priority(6)
	@Test(groups ={"CIQ", "REACH_AGENT","SANITY"},retryAnalyzer=RetryAnalyzer.class) 
	public void boxValidateFERPAUploadRisk() throws Exception {
		Reporter.log("Verifying CIQ Upload activity event ", true);
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "File ferpa_baileydoxed.txt upload has risk(s) - PII, ContentIQ Violations, FERPA");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Content Inspection");
		expectedDataMap.put(GatewayTestConstants.RISK, "PII, ContentIQ Violations, FERPA");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "critical");
		expectedDataMap.put("content_checks", "CIQ_FE_GW_DontDelete");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("Verifying CIQ Upload activity event  successfull", true);
	}
	@Priority(6)
	@Test(groups ={"CIQ", "REACH_AGENT","SANITY"},retryAnalyzer=RetryAnalyzer.class) 
	public void boxValidateFERPADownloadRisk() throws Exception {
		Reporter.log("Verifying CIQ download activity event ", true);
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "File ferpa_baileydoxed.txt download has risk(s) - PII, ContentIQ Violations, FERPA");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Content Inspection");
		expectedDataMap.put(GatewayTestConstants.RISK, "PII, ContentIQ Violations, FERPA");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "critical");
		expectedDataMap.put("content_checks", "CIQ_FE_GW_DontDelete");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("Verifying CIQ download activity event  successfull", true);
	}


	@Priority(8)
	//@Test(groups ={"CIQ", "REACH_AGENT"},retryAnalyzer=RetryAnalyzer.class) 
	public void boxValidateImageUploadCI() throws Exception {
		Reporter.log("Verifying CIQ Upload activity event ", true);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "File image.jpeg upload has risk(s) - ContentIQ Violations");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Content Inspection");
		expectedDataMap.put(GatewayTestConstants.RISK, "ContentIQ Violations");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "critical");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("Verifying CIQ Upload activity event  successfull", true);
	}
	@Priority(8)
	//@Test(groups ={"CIQ", "REACH_AGENT"},retryAnalyzer=RetryAnalyzer.class) 
	public void boxValidateImageDownloadCI() throws Exception {
		Reporter.log("Verifying CIQ download activity event ", true);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "File image.jpeg download has risk(s) - ContentIQ Violations");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Content Inspection");
		expectedDataMap.put(GatewayTestConstants.RISK, "ContentIQ Violations");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "critical");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("Verifying CIQ download activity event  successfull", true);
	}
	
	@Priority(8)
	@Test(groups ={"CIQ", "REACH_AGENT"},retryAnalyzer=RetryAnalyzer.class) 
	public void boxValidateDrugsUploadCI() throws Exception {
		Reporter.log("Verifying CIQ Upload activity event ", true);
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "File illegal_drugs.txt upload has risk(s) - ContentIQ Violations");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Content Inspection");
		expectedDataMap.put(GatewayTestConstants.RISK, "ContentIQ Violations");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "critical");
		expectedDataMap.put("content_checks", "GW_CIQ_Drugs_GamblingDictionaryProfile");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("Verifying CIQ Upload activity event  successfull", true);
	}
	@Priority(8)
	@Test(groups ={"CIQ", "REACH_AGENT"},retryAnalyzer=RetryAnalyzer.class) 
	public void boxValidateDrugsDownloadCI() throws Exception {
		Reporter.log("Verifying CIQ download activity event ", true);
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "File illegal_drugs.txt download has risk(s) - ContentIQ Violations");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Content Inspection");
		expectedDataMap.put(GatewayTestConstants.RISK, "ContentIQ Violations");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "critical");
		expectedDataMap.put("content_checks", "GW_CIQ_Drugs_GamblingDictionaryProfile");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("Verifying CIQ download activity event  successfull", true);
	}
	@Priority(8)
	@Test(groups ={"CIQ", "REACH_AGENT"},retryAnalyzer=RetryAnalyzer.class) 
	public void boxValidateGamblingUploadCI() throws Exception {
		Reporter.log("Verifying CIQ Upload activity event ", true);
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "File gambling.txt upload has risk(s) - ContentIQ Violations");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Content Inspection");
		expectedDataMap.put(GatewayTestConstants.RISK, "ContentIQ Violations");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "critical");
		expectedDataMap.put("content_checks", "GW_CIQ_GamblingDictionaryProfile");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("Verifying CIQ Upload activity event  successfull", true);
	}
	@Priority(8)
	@Test(groups ={"CIQ", "REACH_AGENT"},retryAnalyzer=RetryAnalyzer.class) 
	public void boxValidateGamblingDownloadCI() throws Exception {
		Reporter.log("Verifying CIQ download activity event ", true);
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "File gambling.txt download has risk(s) - ContentIQ Violations");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Content Inspection");
		expectedDataMap.put(GatewayTestConstants.RISK, "ContentIQ Violations");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "critical");
		expectedDataMap.put("content_checks", "GW_CIQ_GamblingDictionaryProfile");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("Verifying CIQ download activity event  successfull", true);
	}
	
	@Priority(9)
	@Test(groups ={"CIQ", "REACH_AGENT"},retryAnalyzer=RetryAnalyzer.class) 
	public void boxValidateCustomTermDivorceUpload() throws Exception {
		Reporter.log("Verifying CIQ Upload divorce.txt ", true);
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "File divorce.txt upload has risk(s) - ContentIQ Violations");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Content Inspection");
		expectedDataMap.put(GatewayTestConstants.RISK, "ContentIQ Violations");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "critical");
		expectedDataMap.put("content_checks", "GW_CIQ_Divorce");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("Verifying CIQ Upload activity event  successfull", true);
	}
	@Priority(9)
	@Test(groups ={"CIQ", "REACH_AGENT"},retryAnalyzer=RetryAnalyzer.class) 
	public void boxValidateCustomTermDivorceDownload() throws Exception {
		Reporter.log("Verifying CIQ download divorce.txt ", true);
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "File divorce.txt download has risk(s) - ContentIQ Violations");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Content Inspection");
		expectedDataMap.put(GatewayTestConstants.RISK, "ContentIQ Violations");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "critical");
		expectedDataMap.put("content_checks", "GW_CIQ_Divorce");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("Verifying CIQ download activity event  successfull", true);
	}
	
	@Priority(9)
	@Test(groups ={"CIQ", "REACH_AGENT"},retryAnalyzer=RetryAnalyzer.class) 
	public void boxValidateCustomTermSSNUpload() throws Exception {
		Reporter.log("Verifying CIQ Upload US_SSN.txt ", true);
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "File us_social_security_number.txt upload has risk(s) - PII, ContentIQ Violations");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Content Inspection");
		expectedDataMap.put(GatewayTestConstants.RISK, "ContentIQ Violations");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "critical");
		expectedDataMap.put("content_checks", "CIQ_FE_GW_DontDelete");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("Verifying CIQ Upload activity event  successfull", true);
	}
	@Priority(9)
	@Test(groups ={"CIQ", "REACH_AGENT"},retryAnalyzer=RetryAnalyzer.class) 
	public void boxValidateCustomTermSSNDownload() throws Exception {
		Reporter.log("Verifying CIQ download US_SSN.txt ", true);
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "File us_social_security_number.txt download has risk(s) - PII, ContentIQ Violations");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Content Inspection");
		expectedDataMap.put(GatewayTestConstants.RISK, "ContentIQ Violations");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "critical");
		expectedDataMap.put("content_checks", "CIQ_FE_GW_DontDelete");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("Verifying CIQ download activity event  successfull", true);
	}
	
	@Priority(9)
//	@Test(groups ={"CIQ", "REACH_AGENT"},retryAnalyzer=RetryAnalyzer.class) 
	public void boxValidateCustomTermEncryptionUpload() throws Exception {
		Reporter.log("Verifying CIQ Upload encrypt.bint ", true);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "File encryption.bin upload has risk(s) - ContentIQ Violations");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Content Inspection");
		expectedDataMap.put(GatewayTestConstants.RISK, "ContentIQ Violations");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "critical");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("Verifying CIQ Upload activity event  successfull", true);
	}
	
	@Priority(9)
	//@Test(groups ={"CIQ", "REACH_AGENT"},retryAnalyzer=RetryAnalyzer.class) 
	public void boxValidateCustomTermEncryptionDownload() throws Exception {
		Reporter.log("Verifying CIQ download encrypt.bin ", true);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "File encryption.bin download has risk(s) - ContentIQ Violations");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Content Inspection");
		expectedDataMap.put(GatewayTestConstants.RISK, "ContentIQ Violations");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "critical");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("Verifying CIQ download activity event  successfull", true);
	}
	
	@Priority(9)
//	@Test(groups ={"CIQ", "REACH_AGENT"},retryAnalyzer=RetryAnalyzer.class) 
	public void boxValidateVirusUpload() throws Exception {
		Reporter.log("Verifying CIQ Upload virus.zip ", true);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "File virus.zip upload has risk(s) - ContentIQ Violations, Virus / Malware");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Content Inspection");
		expectedDataMap.put(GatewayTestConstants.RISK, "ContentIQ Violations, Virus / Malware");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "critical");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("Verifying CIQ Upload activity event  successfull", true);
	}
	
	@Priority(9)
//	@Test(groups ={"CIQ", "REACH_AGENT"},retryAnalyzer=RetryAnalyzer.class) 
	public void boxValidateVirusDownload() throws Exception {
		Reporter.log("Verifying CIQ download virus.zip ", true);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "File virus.zip download has risk(s) - ContentIQ Violations, Virus / Malware");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Content Inspection");
		expectedDataMap.put(GatewayTestConstants.RISK, "ContentIQ Violations, Virus / Malware");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "critical");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("Verifying CIQ download activity event  successfull", true);
	}
	
	@Priority(9)
	@Test(groups ={"CIQ", "REACH_AGENT","CIQ_REGRESSION"}, dataProvider = "ContentInspectionUpload",retryAnalyzer=RetryAnalyzer.class) 
	public void boxValidateUploadCI(String file,String Message,String Profile) throws Exception {
		Reporter.log("Verifying CIQ Upload file :"+file, true);
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.MESSAGE, Message);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Content Inspection");
		expectedDataMap.put(GatewayTestConstants.RISK, "ContentIQ Violations");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "critical");
		expectedDataMap.put("content_checks", Profile);
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("Verifying CIQ Upload activity event  successfull", true);
	}
	
	
	
	@DataProvider
	public Object[][] ContentInspectionUpload() {
		return new Object[][]{
			// File type    Message	   		         
//			{ "audio.mp3", "File audio.mp3 upload has risk(s) - ContentIQ Violations","GW_CIQ_DictionaryProfile"},
//			{ "business.txt", "File business.txt upload has risk(s) - ContentIQ Violations","GW_CIQ_DictionaryProfile"},
//			{ "computing.txt", "File computing.txt upload has risk(s) - ContentIQ Violations","GW_CIQ_DictionaryProfile"},
//			{ "digital_certificate.pem", "File digital_certificate.pem upload has risk(s) - ContentIQ Violations","GW_CIQ_DictionaryProfile"},
//			{ "encryption.bin", "File encryption.bin upload has risk(s) - ContentIQ Violations","GW_CIQ_DictionaryProfile"},
//			{ "engineering.txt", "File engineering.txt upload has risk(s) - ContentIQ Violations","GW_CIQ_DictionaryProfile"},
//			{ "health.txt", "File health.txt upload has risk(s) - ContentIQ Violations","GW_CIQ_DictionaryProfile"},
	//		{ "image.jpeg", "File image.jpeg upload has risk(s) - ContentIQ Violations","GW_CIQ_DictionaryProfile"},
	//		{ "legal.html", "File legal.html upload has risk(s) - ContentIQ Violations","GW_CIQ_Dictionary"},
//			{ "video.mp4", "File video.mp4 upload has risk(s) - ContentIQ Violations","GW_CIQ_DictionaryProfile"},
			{ "us_license_plate_number.txt", "File us_license_plate_number.txt upload has risk(s) - ContentIQ Violations","GW_CIQ_UssnLiscencePlate"},
			{ "hdfc.txt", "File hdfc.txt upload has risk(s) - ContentIQ Violations","GW_CIQ_HDFCProfile"},
			{ "design.pdf", "File design.pdf upload has risk(s) - ContentIQ Violations","GW_CIQ_Dictionary"},
		};
	}
	
	@Priority(9)
	@Test(groups ={"CIQ", "REACH_AGENT"}, dataProvider = "ContentInspectionDownload",retryAnalyzer=RetryAnalyzer.class) 
	public void boxCIValidationDownload(String file,String Message,String Profile) throws Exception {
		Reporter.log("Verifying CIQ Download "+file, true);
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.MESSAGE, Message);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Content Inspection");
		expectedDataMap.put(GatewayTestConstants.RISK, "ContentIQ Violations");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "critical");
		expectedDataMap.put("content_checks", Profile);
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("Verifying CIQ Download activity event  successfull", true);
	}
	
	
	
	@DataProvider
	public Object[][] ContentInspectionDownload() {
		return new Object[][]{
			// File type    Message	   		         
//			{ "audio.mp3", "File audio.mp3 download has risk(s) - ContentIQ Violations","GW_CIQ_DictionaryProfile"},
//			{ "business.txt", "File business.txt download has risk(s) - ContentIQ Violations","GW_CIQ_DictionaryProfile"},
//			{ "computing.txt", "File computing.txt download has risk(s) - ContentIQ Violations","GW_CIQ_DictionaryProfile"},
//			{ "digital_certificate.pem", "File digital_certificate.pem download has risk(s) - ContentIQ Violations","GW_CIQ_DictionaryProfile"},
//			{ "encryption.bin", "File encryption.bin download has risk(s) - ContentIQ Violations","GW_CIQ_DictionaryProfile"},
//			{ "engineering.txt", "File engineering.txt download has risk(s) - ContentIQ Violations","GW_CIQ_DictionaryProfile"},
//			{ "health.txt", "File health.txt download has risk(s) - ContentIQ Violations","GW_CIQ_DictionaryProfile"},
//			{ "image.jpeg", "File image.jpeg download has risk(s) - ContentIQ Violations","GW_CIQ_DictionaryProfile"},
//			{ "legal.html", "File legal.html download has risk(s) - ContentIQ Violations","GW_CIQ_Dictionary"},
//			{ "video.mp4", "File video.mp4 download has risk(s) - ContentIQ Violations","GW_CIQ_DictionaryProfile"},
			{ "us_license_plate_number.txt", "File us_license_plate_number.txt download has risk(s) - ContentIQ Violations","GW_CIQ_UssnLiscencePlate"},
			{ "hdfc.txt", "File hdfc.txt download has risk(s) - ContentIQ Violations","GW_CIQ_HDFCProfile"},
			{ "design.pdf", "File design.pdf download has risk(s) - ContentIQ Violations","GW_CIQ_Dictionary"},
			
		};
	}
	
	
	@AfterClass(alwaysRun= true)
	public void Cleanup() throws Exception {
		Logger.info("start cleanup activities on cloudSoc");
		policy.deletePolicy(GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+"HDFCPolicy", suiteData, backend.getHeaders(suiteData));
		policy.deletePolicy(GatewayTestConstants.CIQ_DOWNLOAD_POLICY_NAME+"HDFCPolicy", suiteData, backend.getHeaders(suiteData));
		//Delete old policies
		policy.deletePolicy(suiteData.getSaasAppName()+GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData));
		policy.deletePolicy(suiteData.getSaasAppName()+GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+"DictionaryProfile", suiteData, backend.getHeaders(suiteData));
		policy.deletePolicy(suiteData.getSaasAppName()+GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+"DivorcePolicy", suiteData, backend.getHeaders(suiteData));
		policy.deletePolicy(suiteData.getSaasAppName()+GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+"GamblingPolicy", suiteData, backend.getHeaders(suiteData));
		policy.deletePolicy(suiteData.getSaasAppName()+GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+"DrugsPolicy", suiteData, backend.getHeaders(suiteData));	
		policy.deletePolicy(suiteData.getSaasAppName()+GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+"USLincencPlate", suiteData, backend.getHeaders(suiteData));
		policy.deletePolicy(suiteData.getSaasAppName()+GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+"HDFCPolicy", suiteData, backend.getHeaders(suiteData));
		//Delete old policies
		policy.deletePolicy(suiteData.getSaasAppName()+GatewayTestConstants.CIQ_DOWNLOAD_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData));
		policy.deletePolicy(suiteData.getSaasAppName()+GatewayTestConstants.CIQ_DOWNLOAD_POLICY_NAME+"DictionaryProfile", suiteData, backend.getHeaders(suiteData));
		policy.deletePolicy(suiteData.getSaasAppName()+GatewayTestConstants.CIQ_DOWNLOAD_POLICY_NAME+"DivorcePolicy", suiteData, backend.getHeaders(suiteData));
		policy.deletePolicy(suiteData.getSaasAppName()+GatewayTestConstants.CIQ_DOWNLOAD_POLICY_NAME+"GamblingPolicy", suiteData, backend.getHeaders(suiteData));
		policy.deletePolicy(suiteData.getSaasAppName()+GatewayTestConstants.CIQ_DOWNLOAD_POLICY_NAME+"DrugsPolicy", suiteData, backend.getHeaders(suiteData));	
		policy.deletePolicy(suiteData.getSaasAppName()+GatewayTestConstants.CIQ_DOWNLOAD_POLICY_NAME+"USLincencPlate", suiteData, backend.getHeaders(suiteData));
		policy.deletePolicy(suiteData.getSaasAppName()+GatewayTestConstants.CIQ_DOWNLOAD_POLICY_NAME+"HDFCPolicy", suiteData, backend.getHeaders(suiteData));

		Reporter.log("Finished cleanup activities on cloudSoc", true);
		
	}
	public void setCommonFieldsInExpectedDataMap(){
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		expectedDataMap.put(GatewayTestConstants.ACCOUNT_TYPE,suiteData.getAccountType());
		expectedDataMap.put(GatewayTestConstants.BROWSER, suiteData.getBrowser());
		expectedDataMap.put(GatewayTestConstants.DEVICE, "NOT_EMPTY");
		expectedDataMap.put(GatewayTestConstants.ELASTICA_USER, suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.FACILITY, suiteData.getSaasAppName());
//		expectedDataMap.put(GatewayTestConstants.HOST, "NOT_EMPTY");
		expectedDataMap.put(GatewayTestConstants.CREATED_TIME_STAMP, "NOT_EMPTY");
		expectedDataMap.put(GatewayTestConstants.INSERTED_TIME_STAMP, "NOT_EMPTY");
		expectedDataMap.put(GatewayTestConstants.IS_ANONYMOUS_PROXY, GatewayTestConstants.IS_ANONYMOUS_PROXY_FALSE);
		expectedDataMap.put(GatewayTestConstants.REQ_SIZE, "NOT_EMPTY");
		expectedDataMap.put(GatewayTestConstants.REQ_URI, "NOT_EMPTY");
		expectedDataMap.put(GatewayTestConstants.RESP_SIZE, "NOT_EMPTY");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
//		expectedDataMap.put(GatewayTestConstants.TIME_ZONE, "NOT_EMPTY");
		expectedDataMap.put(GatewayTestConstants.USER, suiteData.getSaasAppUsername());
		expectedDataMap.put(GatewayTestConstants.USER_NAME, "NOT_EMPTY");
		expectedDataMap.put(GatewayTestConstants.VERSION, "NOT_EMPTY");
	}
	
	
	public String getSaasAppUserName(){
		return suiteData.getSaasAppUsername().replaceAll("@", "_");
	}

	

	
}
