package com.elastica.tests.google;

import static org.testng.Assert.assertTrue;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.elastica.common.CommonTest;
import com.elastica.common.GWCommonTest;
import com.elastica.gateway.GatewayTestConstants;
import com.elastica.listeners.Priority;
import com.elastica.listeners.RetryAnalyzer;
import com.elastica.logger.Logger;
import com.elastica.tests.o365.CIQConstants;
import com.elastica.tests.o365.CiqUtils;

public class GDriveTestsCIQ extends CommonTest{
	Map <String, Object> expectedDataMap = new HashMap<String, Object>();
	String fromTime=null;
	String title;
	CiqUtils utils=new CiqUtils();
	GWCommonTest gwtest=new GWCommonTest();
	

	@BeforeClass(alwaysRun= true)
	public void doBeforeClass() throws Exception {
		Logger.info("create Profile Before Test ");
		utils.createCIQProfile( CIQConstants.ZERO,suiteData, "GW_CIQ_DictionaryProfile","GW_CIQ_DictionaryProfile",CIQConstants.SOURCE_RISK,CIQConstants.RISKVALUE);
		utils.createCIQProfile(CIQConstants.ONE, suiteData, "GW_CIQ_Divorce","GW_CIQ_Divorce",CIQConstants.SOURCE_DIVORCE,CIQConstants.VALUE_DIVORCE);
		utils.createCIQProfile(CIQConstants.ONE, suiteData, "GW_CIQ_GamblingDictionaryProfile","GW_CIQ_GamblingDictionaryProfile",CIQConstants.SOURCE_DRUG_GAMBLE,CIQConstants.VALUE_GAMBLE);
		utils.createCIQProfile(CIQConstants.ONE, suiteData, "GW_CIQ_DrugsDictionaryProfile","GW_CIQ_DrugsDictionaryProfile",CIQConstants.SOURCE_DRUG_GAMBLE,CIQConstants.VALUE_DRUG);
		utils.createCIQProfile(CIQConstants.ONE,suiteData, "GW_CIQ_UssnLiscencePlate","GW_CIQ_UssnLiscencePlate",CIQConstants.SOURCE_USLISCENCE,CIQConstants.VALUE_US_LISCENCE_PLT);
		utils.createCIQProfile(CIQConstants.ONE,suiteData, "GW_CIQ_HDFCProfile","GW_CIQ_HDFCProfile",CIQConstants.SOURCE_HDFC,CIQConstants.VALUE_HDFC);		
		Logger.info("Delete Policy Before Test ");
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME+ suiteData.getSaasAppName(), suiteData, backend.getGWHeaders(suiteData));
	
		policy.deletePolicy(GatewayTestConstants.CIQ_DOWNLOAD_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData));
		policy.deletePolicy(GatewayTestConstants.CIQ_DOWNLOAD_POLICY_NAME+"DictionaryProfile", suiteData, backend.getHeaders(suiteData));
		policy.deletePolicy(GatewayTestConstants.CIQ_DOWNLOAD_POLICY_NAME+"DivorcePolicy", suiteData, backend.getHeaders(suiteData));
		policy.deletePolicy(GatewayTestConstants.CIQ_DOWNLOAD_POLICY_NAME+"GamblingPolicy", suiteData, backend.getHeaders(suiteData));
		policy.deletePolicy(GatewayTestConstants.CIQ_DOWNLOAD_POLICY_NAME+"DrugsPolicy", suiteData, backend.getHeaders(suiteData));	
		policy.deletePolicy(GatewayTestConstants.CIQ_DOWNLOAD_POLICY_NAME+"USLincencPlate", suiteData, backend.getHeaders(suiteData));
		policy.deletePolicy(GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData));
		policy.deletePolicy(GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+"DictionaryProfile", suiteData, backend.getHeaders(suiteData));
		policy.deletePolicy(GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+"DivorcePolicy", suiteData, backend.getHeaders(suiteData));
		policy.deletePolicy(GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+"GamblingPolicy", suiteData, backend.getHeaders(suiteData));
		policy.deletePolicy(GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+"DrugsPolicy", suiteData, backend.getHeaders(suiteData));	
		policy.deletePolicy(GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+"USLincencPlate", suiteData, backend.getHeaders(suiteData));

		
	}

	@Priority(1)
	@Test(groups ={"CIQ","SANITY", "MINI_SANITY", "REGRESSION","DOWNLOAD","UPLOAD"})
	public void CloudSocLoginAndSetupSSO() throws Exception {
		fromTime=backend.getCurrentTime();
		Logger.info("==================================================================================");
		Logger.info("Login into CloudSoc");
		Logger.info("==================================================================================");
		login.loginCloudSocPortal(getWebDriver(), suiteData);
		Logger.info("==================================================================================");
		Logger.info("Loging into CloudSoc done");
		Logger.info("==================================================================================");
		
	}
	
	@Priority(1)
	@Test(groups ={"CIQ","SANITY", "MINI_SANITY", "REGRESSION","DOWNLOAD","UPLOAD","REACH_AGENT"})
	public void loginSaasApp() throws Exception {
		fromTime=backend.getCurrentTime();
		Logger.info("==================================================================================");
		Logger.info("Verifying the login event");
		Logger.info("==================================================================================");
		Thread.sleep(15000);
		gda.login(getWebDriver(), suiteData);
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Login");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User logged in"); 
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Session");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("Login Event Verification Successfull", true);
		Logger.info("==================================================================================");
		Logger.info("Verify login successful");
		Logger.info("==================================================================================");
	}
	
	@Priority(2)
	@Test(groups ={"CIQ","REGRESSION","DOWNLOAD","REACH_AGENT","UPLOAD"})  
	public void gDriveValidatePolicy() throws Exception {
		Logger.info("==================================================================================");
		Logger.info("Verifying the policy creation event");
		Logger.info("==================================================================================");
		policy.deletePolicy(GatewayTestConstants.CIQ_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData));
		policy.createCIQPolicy(GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+"DivorcePolicy", suiteData, backend.getHeaders(suiteData),"GW_CIQ_Divorce","upload");
		policy.createCIQPolicy(GatewayTestConstants.CIQ_DOWNLOAD_POLICY_NAME+"DivorcePolicy", suiteData, backend.getHeaders(suiteData),"GW_CIQ_Divorce","download");
		Logger.info("==================================================================================");
		Logger.info("Verify policy creation successful");
		Logger.info("==================================================================================");
	}
		
	
	
	@Priority(4)
	@Test(groups ={"CIQ","SANITY", },retryAnalyzer=RetryAnalyzer.class )
	public void gDriveUploadFilesCIQ() throws Exception {
		Logger.info("==================================================================================");
		Logger.info("Upload CIQ File operation - Start ");
		Logger.info("==================================================================================");
		
		String [] ciqfiles={"pci.txt","pii.txt"};//, "PII_TXT.rtf", "pii.txt", "glba.txt", "hipaa.txt", "vba_macro.xls"};
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData));
		policy.createCIQPolicy(GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData));
		gda.homepage(getWebDriver(), suiteData);
		gda.selectFolder(getWebDriver(), "CIQ");	
		for(int i=0; i<ciqfiles.length;i++)
		{
			gda.deleteFile(getWebDriver(), ciqfiles[i]);
		}
		for(int i=0; i<ciqfiles.length;i++){
			//gda.login(getWebDriver(), suiteData);
			gda.uploadFile(getWebDriver(),  System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+
				"resources"+File.separator+"ciq"+File.separator + ciqfiles[i]);
		
		}
		gda.homepage(getWebDriver(), suiteData);
		Thread.sleep(150000);
		policy.deletePolicy(GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData));
		Logger.info("==================================================================================");
		Logger.info("Upload CIQ File operation - complete ");
		Logger.info("==================================================================================");
		}

	@Priority(3)
	@Test(groups ={"CIQ","SANITY"})
	public void gDrive_DownloadCIQValidation() throws Exception {
		Logger.info("==================================================================================");
		Logger.info("Verifying the Download Decrypt");
		Logger.info("==================================================================================");
		// create policy
		policy.createCIQPolicy(GatewayTestConstants.CIQ_DOWNLOAD_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData),"CIQ_FE_GW_DontDelete","download");
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME + suiteData.getSaasAppName(), suiteData, backend.getGWHeaders(suiteData));	
		String [] ciqfiles={"pci.txt","pii.txt", "glba.txt", "hipaa.txt", "vba_macro.xls","testabc.txt"};
		for(int i=0; i<ciqfiles.length;i++)
		{
			gda.homepage(getWebDriver(), suiteData);
			gda.selectFolder(getWebDriver(), "CIQ");
			//expectedDataMap.clear();
			gda.download(getWebDriver(), ciqfiles[i]);
			Logger.info("Download CIQ File successfull"+ciqfiles[i]);
			Thread.sleep(5000);
		}
		Thread.sleep(150000);
		policy.deletePolicy(GatewayTestConstants.CIQ_DOWNLOAD_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData));
		Logger.info("==================================================================================");
		Logger.info("Download CIQ File successfull");
		Logger.info("==================================================================================");
	}
	
	

	@Priority(5)
	@Test(groups ={"CIQ"},retryAnalyzer=RetryAnalyzer.class) 
	public void gDrive_Test_06_ValidatePII_CIQ() throws Exception {
		Logger.info("==================================================================================");
		Logger.info("CIQ Log Verification started");
		Logger.info("==================================================================================");
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Content Inspection");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "File "+GatewayTestConstants.GDRIVE_CIQ_DOWNLOAD_FILE+" download has risk(s) - PII, ContentIQ Violations");
		expectedDataMap.put(GatewayTestConstants.RISK, "PII, ContentIQ Violations");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "critical");
		expectedDataMap.put("content_checks", "CIQ_FE_GW_DontDelete");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");

		Logger.info("==================================================================================");
		Logger.info("CIQ Log Verification Successfull");
		Logger.info("==================================================================================");	}
	
	
	@Priority(5)
	@Test(groups ={"CIQ" },retryAnalyzer=RetryAnalyzer.class) 
	public void gDrive_Test_06_ValidatePCI_CIQ() throws Exception {
		Reporter.log("Verifying PCI_CIQ download activity event with policy", true);
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "File pci.txt download has risk(s) - ContentIQ Violations, PCI");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Content Inspection");
		expectedDataMap.put(GatewayTestConstants.RISK, "ContentIQ Violations, PCI");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "critical");
		expectedDataMap.put("content_checks", "CIQ_FE_GW_DontDelete");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("PCI_CIQ download actvity event verification successfull", true);
	}

		
		
	
	//===========================================================================
	
	@Priority(4)
	@Test(groups ={"REGRESSION","P1","UPLOAD","REACH_AGENT","REACH"})// retryAnalyzer=RetryAnalyzer.class)
	public void uploadCIQFiles() throws Exception {
		Logger.info("Verifying the File Upload Event");
		Thread.sleep(5000);
		//create new policies
		policy.createCIQPolicy(GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData));
		policy.createCIQPolicy(GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+"DictionaryProfile", suiteData, backend.getHeaders(suiteData),"GW_CIQ_DictionaryProfile","upload");
		policy.createCIQPolicy(GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+"GamblingPolicy", suiteData, backend.getHeaders(suiteData),"GW_CIQ_GamblingDictionaryProfile","upload");
		policy.createCIQPolicy(GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+"DrugsPolicy", suiteData, backend.getHeaders(suiteData),"GW_CIQ_Drugs_GamblingDictionaryProfile","upload");
		policy.createCIQPolicy(GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+"USLincencPlate", suiteData, backend.getHeaders(suiteData),"GW_CIQ_UssnLiscencePlate","upload");		
		policy.createCIQPolicy(GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+"HDFCPolicy", suiteData, backend.getHeaders(suiteData),"GW_CIQ_HDFCProfile","upload");
		gda.homepage(getWebDriver(), suiteData);
		Thread.sleep(5000);
		gda.selectFolder(getWebDriver(), "RISK");
		// upload contentIQ  file
		String [] contentfiles={"divorce.txt","US_License_Plate_Number.txt","Java.txt","hdfc.txt","design.pdf"};
		String	absLocation=System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+
				"resources"+File.separator+"ciq"+File.separator+"contentType"+File.separator;// + ciqfiles[i]

		for(int i=0; i<contentfiles.length;i++){
			try
			{
				//gda.deleteFile(getWebDriver(), contentfiles[i]);
				Logger.info("performaing  File Upload operation for file "+contentfiles[i]);
				Thread.sleep(5000);
				//	gda.uploadFile(getWebDriver(),absLocation+contentfiles[i] );
				gda.clickUploadFile(getWebDriver());
				gwtest.uploadSingleFile(absLocation+contentfiles[i], suiteData );
				Thread.sleep(5000);
			}
			catch(Exception e) {
				//Logger.info(" performaing  File Upload operation for file "+ciqfiles[i]);
				Logger.info("Error in Upload of file "+contentfiles[i]);
			}
			Thread.sleep(5000);	
		}

		String [] ciqfiles={"pci.txt","pii_risks.rtf", "pii.txt", "glba.txt", "hipaa.txt","FERPA_BaileyDoxed.txt","virus.zip","Gambling.txt","Illegal_Drugs.txt","US_Social_Security_Number.txt", "vba_macro.xls"};
		absLocation=System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+
				"resources"+File.separator+"ciq"+File.separator;// + ciqfiles[i]
		for(int i=0; i<ciqfiles.length;i++){

			try
			{
				Logger.info(" performaing  File Upload operation for file "+ciqfiles[i]);
				//gda.deleteFile(getWebDriver(), ciqfiles[i]);
				Thread.sleep(5000);
				gda.clickUploadFile(getWebDriver());
				gwtest.uploadSingleFile(absLocation+ciqfiles[i], suiteData );
				Thread.sleep(5000);
			}
			catch(Exception e) {
				//Logger.info(" performaing  File Upload operation for file "+ciqfiles[i]);
				Logger.info("Error in Upload of file "+ciqfiles[i]);
			}
			Thread.sleep(5000);
		}
		Thread.sleep(150000);
		//Delete old policies
		policy.deletePolicy(GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData));
		policy.deletePolicy(GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+"DictionaryProfile", suiteData, backend.getHeaders(suiteData));
		policy.deletePolicy(GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+"DivorcePolicy", suiteData, backend.getHeaders(suiteData));
		policy.deletePolicy(GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+"GamblingPolicy", suiteData, backend.getHeaders(suiteData));
		policy.deletePolicy(GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+"DrugsPolicy", suiteData, backend.getHeaders(suiteData));	
		policy.deletePolicy(GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+"USLincencPlate", suiteData, backend.getHeaders(suiteData));
		policy.deletePolicy(GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+"HDFCPolicy", suiteData, backend.getHeaders(suiteData));


		Reporter.log("Log verification successful for method:"+Thread.currentThread().getStackTrace()[1].getMethodName(), true);

	}
	
	@Priority(3)
	@Test(groups ={"REGRESSION","P1","DOWNLOAD","REACH_AGENT","REACH"},retryAnalyzer=RetryAnalyzer.class)
	public void downloadCIQFiles() throws Exception {
		Logger.info("create ciq risk profile");
		//create new policies
		policy.createCIQPolicy(GatewayTestConstants.CIQ_DOWNLOAD_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData),"CIQ_FE_GW_DontDelete","download");
		policy.createCIQPolicy(GatewayTestConstants.CIQ_DOWNLOAD_POLICY_NAME+"DictionaryProfile", suiteData, backend.getHeaders(suiteData),"GW_CIQ_DictionaryProfile","download");
		policy.createCIQPolicy(GatewayTestConstants.CIQ_DOWNLOAD_POLICY_NAME+"GamblingPolicy", suiteData, backend.getHeaders(suiteData),"GW_CIQ_GamblingDictionaryProfile","download");
		policy.createCIQPolicy(GatewayTestConstants.CIQ_DOWNLOAD_POLICY_NAME+"DrugsPolicy", suiteData, backend.getHeaders(suiteData),"GW_CIQ_Drugs_GamblingDictionaryProfile","download");
		policy.createCIQPolicy(GatewayTestConstants.CIQ_DOWNLOAD_POLICY_NAME+"USLincencPlate", suiteData, backend.getHeaders(suiteData),"GW_CIQ_UssnLiscencePlate","download");
		policy.createCIQPolicy(GatewayTestConstants.CIQ_DOWNLOAD_POLICY_NAME+"HDFCPolicy", suiteData, backend.getHeaders(suiteData),"GW_CIQ_HDFCProfile","download");
		// download Risk  file
		gda.homepage(getWebDriver(), suiteData);
		gda.selectFolder(getWebDriver(), "CI");
		String [] ciqfiles={"pci.txt","pii.txt","glba.txt", "hipaa.txt", "vba_macro.xls","FERPA_BaileyDoxed.txt","Gambling.txt","Illegal_Drugs.txt","US_Social_Security_Number.txt"};
		for(int i=0; i<ciqfiles.length;i++){
			
			try
			{
				Logger.info(" Start  File Download operation for file "+ciqfiles[i]);
				gda.download(getWebDriver(), ciqfiles[i]);
				Logger.info(" Completed  File Download operation for file "+ciqfiles[i]);
			}
			catch(Exception e) {
				Logger.info("Error in File Download operation for file "+ciqfiles[i]);
			}
			Thread.sleep(5000);
		}


		String [] contentfiles={"hdfc.txt","Java.txt","divorce.txt","US_License_Plate_Number.txt",};
		for(int i=0; i<contentfiles.length;i++){
			//	download content file
			try
			{
				Logger.info("downloading file " + contentfiles[i]);
				gda.download(getWebDriver(), contentfiles[i]);
			}
			catch(Exception e) {
				Logger.info("Error in login " + e.getMessage());
			}
			Thread.sleep(5000);
		}
		//Delete old policies
		Thread.sleep(150000);
		policy.deletePolicy(GatewayTestConstants.CIQ_DOWNLOAD_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData));
		policy.deletePolicy(GatewayTestConstants.CIQ_DOWNLOAD_POLICY_NAME+"DictionaryProfile", suiteData, backend.getHeaders(suiteData));
		policy.deletePolicy(GatewayTestConstants.CIQ_DOWNLOAD_POLICY_NAME+"DivorcePolicy", suiteData, backend.getHeaders(suiteData));
		policy.deletePolicy(GatewayTestConstants.CIQ_DOWNLOAD_POLICY_NAME+"GamblingPolicy", suiteData, backend.getHeaders(suiteData));
		policy.deletePolicy(GatewayTestConstants.CIQ_DOWNLOAD_POLICY_NAME+"DrugsPolicy", suiteData, backend.getHeaders(suiteData));	
		policy.deletePolicy(GatewayTestConstants.CIQ_DOWNLOAD_POLICY_NAME+"USLincencPlate", suiteData, backend.getHeaders(suiteData));	
		policy.deletePolicy(GatewayTestConstants.CIQ_DOWNLOAD_POLICY_NAME+"HDFCPolicy", suiteData, backend.getHeaders(suiteData));		
		Reporter.log("Log verification successful for method:"+Thread.currentThread().getStackTrace()[1].getMethodName(), true);

	}
	
	@Priority(5)
	@Test(groups ={"REGRESSION","UPLOAD","REACH_AGENT"},retryAnalyzer=RetryAnalyzer.class) 
	public void gDriveValidatePCIUploadRisk() throws Exception {
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
	@Test(groups ={"REGRESSION","DOWNLOAD","REACH_AGENT"},retryAnalyzer=RetryAnalyzer.class) 
	public void gDriveValidatePCIDownloadRisk() throws Exception {
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
	@Test(groups ={"REGRESSION","UPLOAD","REACH_AGENT"},retryAnalyzer=RetryAnalyzer.class) 
	public void gDriveValidateHIPPAUploadRisk() throws Exception {
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
	@Test(groups ={"REGRESSION","DOWNLOAD","REACH_AGENT"},retryAnalyzer=RetryAnalyzer.class) 
	public void gDriveValidateHIPPADownloadRisk() throws Exception {
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
	@Test(groups ={"REGRESSION","UPLOAD","REACH_AGENT"},retryAnalyzer=RetryAnalyzer.class) 
	public void gDriveValidatePIIUploadRisk() throws Exception {
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
	@Test(groups ={"REGRESSION","DOWNLOAD","REACH_AGENT"},retryAnalyzer=RetryAnalyzer.class) 
	public void gDriveValidatePIIDownloadRisk() throws Exception {
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
	//@Test(groups ={"REGRESSION","UPLOAD","REACH_AGENT"},retryAnalyzer=RetryAnalyzer.class) 
	public void gDriveValidatePIIRtfUploadRisk() throws Exception {
		Logger.info("==================================================================================");
		Logger.info("CIQ Log Verification started");
		Logger.info("==================================================================================");
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Content Inspection");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "File pii_risks.rtf upload has risk(s) - PII, ContentIQ Violations");
		expectedDataMap.put(GatewayTestConstants.RISK, "PII, ContentIQ Violations");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "critical");
		expectedDataMap.put("content_checks", "CIQ_FE_GW_DontDelete");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");

		Logger.info("==================================================================================");
		Logger.info("CIQ Log Verification Successfull");
		Logger.info("==================================================================================");
		}
	
	@Priority(5)
//	@Test(groups ={"REGRESSION","DOWNLOAD"},retryAnalyzer=RetryAnalyzer.class) 
	public void gDriveValidatePIIRtfDownloadRisk() throws Exception {
		Logger.info("==================================================================================");
		Logger.info("CIQ Log Verification started");
		Logger.info("==================================================================================");
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Content Inspection");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "File pii_risks.rtf download has risk(s) - PII, ContentIQ Violations");
		expectedDataMap.put(GatewayTestConstants.RISK, "PII, ContentIQ Violations");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "critical");
		expectedDataMap.put("content_checks", "CIQ_FE_GW_DontDelete");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");

		Logger.info("==================================================================================");
		Logger.info("CIQ Log Verification Successfull");
		Logger.info("==================================================================================");
		}
	
	@Priority(5)
	@Test(groups ={"REGRESSION","DOWNLOAD","REACH_AGENT"},retryAnalyzer=RetryAnalyzer.class) 
	public void gDriveValidateSourceCodeDownloadCI() throws Exception {
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
	@Test(groups ={"REGRESSION","UPLOAD","REACH_AGENT"},retryAnalyzer=RetryAnalyzer.class) 
	public void gDriveValidateUploadSourceCode() throws Exception {
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
	@Test(groups ={"REGRESSION","DOWNLOAD","REACH_AGENT"},retryAnalyzer=RetryAnalyzer.class) 
	public void gDriveValidateVBA_MACRODownloadRisk() throws Exception {
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
	@Test(groups ={"REGRESSION","UPLOAD","REACH_AGENT"},retryAnalyzer=RetryAnalyzer.class) 
	public void gDriveValidateVBA_MACROUploadRisk() throws Exception {
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
	@Test(groups ={"REGRESSION","UPLOAD","REACH_AGENT"},retryAnalyzer=RetryAnalyzer.class) 
	public void gDriveValidateFERPAUploadRisk() throws Exception {
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
	@Test(groups ={"REGRESSION","DOWNLOAD","REACH_AGENT"},retryAnalyzer=RetryAnalyzer.class) 
	public void gDriveValidateFERPADownloadRisk() throws Exception {
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
//	@Test(groups ={"REGRESSION","UPLOAD"},retryAnalyzer=RetryAnalyzer.class) 
	public void gDriveValidateImageUploadCI() throws Exception {
		Reporter.log("Verifying CIQ Upload activity event ", true);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "File image.jpeg upload has risk(s) - ContentIQ Violations");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Content Inspection");
		expectedDataMap.put(GatewayTestConstants.RISK, "ContentIQ Violations");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "critical");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("Verifying CIQ Upload activity event  successfull", true);
	}
	@Priority(8)
	//@Test(groups ={"REGRESSION","DOWNLOAD"},dependsOnMethods = {"downloadCIQFiles"},retryAnalyzer=RetryAnalyzer.class) 
	public void gDriveValidateImageDownloadCI() throws Exception {
		Reporter.log("Verifying CIQ download activity event ", true);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "File image.jpeg download has risk(s) - ContentIQ Violations");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Content Inspection");
		expectedDataMap.put(GatewayTestConstants.RISK, "ContentIQ Violations");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "critical");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("Verifying CIQ download activity event  successfull", true);
	}
	
	@Priority(8)
	@Test(groups ={"REGRESSION","UPLOAD","REACH_AGENT"},retryAnalyzer=RetryAnalyzer.class) 
	public void gDriveValidateDrugsUploadCI() throws Exception {
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
	@Test(groups ={"REGRESSION","DOWNLOAD","REACH_AGENT"},retryAnalyzer=RetryAnalyzer.class) 
	public void gDriveValidateDrugsDownloadCI() throws Exception {
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
	@Test(groups ={"REGRESSION","UPLOAD","REACH_AGENT"},retryAnalyzer=RetryAnalyzer.class) 
	public void gDriveValidateGamblingUploadCI() throws Exception {
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
	@Test(groups ={"REGRESSION","DOWNLOAD","REACH_AGENT"},retryAnalyzer=RetryAnalyzer.class) 
	public void gDriveValidateGamblingDownloadCI() throws Exception {
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
	@Test(groups ={"REGRESSION","UPLOAD","REACH_AGENT"},retryAnalyzer=RetryAnalyzer.class) 
	public void gDriveValidateCustomTermDivorceUpload() throws Exception {
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
	@Test(groups ={"REGRESSION","DOWNLOAD","REACH_AGENT"},retryAnalyzer=RetryAnalyzer.class) 
	public void gDriveValidateCustomTermDivorceDownload() throws Exception {
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
	@Test(groups ={"REGRESSION","UPLOAD","REACH_AGENT"},retryAnalyzer=RetryAnalyzer.class) 
	public void gDriveValidateCustomTermSSNUpload() throws Exception {
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
	@Test(groups ={"REGRESSION","DOWNLOAD","REACH_AGENT"},retryAnalyzer=RetryAnalyzer.class) 
	public void gDriveValidateCustomTermSSNDownload() throws Exception {
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
	//@Test(groups ={"REGRESSION","UPLOAD"},retryAnalyzer=RetryAnalyzer.class) 
	public void gDriveValidateCustomTermEncryptionUpload() throws Exception {
		Reporter.log("Verifying CIQ Upload encrypt.bint ", true);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "File encryption.bin upload has risk(s) - ContentIQ Violations");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Content Inspection");
		expectedDataMap.put(GatewayTestConstants.RISK, "ContentIQ Violations");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "critical");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("Verifying CIQ Upload activity event  successfull", true);
	}
	
	@Priority(9)
	//@Test(groups ={"REGRESSION","DOWNLOAD"},dependsOnMethods = {"downloadCIQFiles"},retryAnalyzer=RetryAnalyzer.class) 
	public void gDriveValidateCustomTermEncryptionDownload() throws Exception {
		Reporter.log("Verifying CIQ download encrypt.bin ", true);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "File encryption.bin download has risk(s) - ContentIQ Violations");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Content Inspection");
		expectedDataMap.put(GatewayTestConstants.RISK, "ContentIQ Violations");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "critical");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("Verifying CIQ download activity event  successfull", true);
	}
	
	@Priority(9)
	//@Test(groups ={"REGRESSION","UPLOAD"},retryAnalyzer=RetryAnalyzer.class) 
	public void gDriveValidateVirusUpload() throws Exception {
		Reporter.log("Verifying CIQ Upload virus.zip ", true);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "File virus.zip upload has risk(s) - ContentIQ Violations, Virus / Malware");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Content Inspection");
		expectedDataMap.put(GatewayTestConstants.RISK, "ContentIQ Violations, Virus / Malware");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "critical");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("Verifying CIQ Upload activity event  successfull", true);
	}
	
	@Priority(9)
	//@Test(groups ={"REGRESSION","DOWNLOAD"},dependsOnMethods = {"downloadCIQFiles"},retryAnalyzer=RetryAnalyzer.class) 
	public void gDriveValidateVirusDownload() throws Exception {
		Reporter.log("Verifying CIQ download virus.zip ", true);
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "File virus.zip download has risk(s) - ContentIQ Violations, Virus / Malware");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Content Inspection");
		expectedDataMap.put(GatewayTestConstants.RISK, "ContentIQ Violations, Virus / Malware");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "critical");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("Verifying CIQ download activity event  successfull", true);
	}
	
	@Priority(9)
	@Test(groups ={"REGRESSION","UPLOAD","REACH_AGENT"}, dataProvider = "ContentInspectionUpload",retryAnalyzer=RetryAnalyzer.class) 
	public void gDriveValidateUploadCI(String file,String Message,String Profile) throws Exception {
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
//			{ "image.jpeg", "File image.jpeg upload has risk(s) - ContentIQ Violations","GW_CIQ_DictionaryProfile"},
//			{ "legal.html", "File legal.html upload has risk(s) - ContentIQ Violations","GW_CIQ_DictionaryProfile"},
//			{ "video.mp4", "File video.mp4 upload has risk(s) - ContentIQ Violations","GW_CIQ_DictionaryProfile"},
			{ "us_license_plate_number.txt", "File us_license_plate_number.txt upload has risk(s) - ContentIQ Violations","GW_CIQ_UssnLiscencePlate"},
			{ "hdfc.txt", "File hdfc.txt upload has risk(s) - ContentIQ Violations","GW_CIQ_HDFCProfile"},
			
		};
	}
	
	@Priority(9)
	@Test(groups ={"REGRESSION","DOWNLOAD","REACH_AGENT"}, dataProvider = "ContentInspectionDownload",retryAnalyzer=RetryAnalyzer.class) 
	public void gDriveCIValidationDownload(String file,String Message,String Profile) throws Exception {
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
//			{ "legal.html", "File legal.html download has risk(s) - ContentIQ Violations","GW_CIQ_DictionaryProfile"},
//			{ "video.mp4", "File video.mp4 download has risk(s) - ContentIQ Violations","GW_CIQ_DictionaryProfile"},
			{ "us_license_plate_number.txt", "File us_license_plate_number.txt download has risk(s) - ContentIQ Violations","GW_CIQ_UssnLiscencePlate"},
			{ "hdfc.txt", "File hdfc.txt download has risk(s) - ContentIQ Violations","GW_CIQ_HDFCProfile"},
			
			
		};
	}
	public String getSaasAppUserName(){
		return suiteData.getSaasAppUsername().replaceAll("@", "_");
	}
	@AfterTest(alwaysRun= true)
	public void doAfterClass() throws Exception {
		Logger.info("Delete Policy After Test ");
		//close driver after execution
		try
		{
			getWebDriver().close();
		}
		catch(Exception e) {
			Logger.info(" driver close error ");
		}
		
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME+ suiteData.getSaasAppName(), suiteData, backend.getGWHeaders(suiteData));
		policy.deletePolicy(GatewayTestConstants.CIQ_DOWNLOAD_POLICY_NAME+"DivorcePolicy", suiteData, backend.getHeaders(suiteData));
		policy.deletePolicy(GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+"DivorcePolicy", suiteData, backend.getHeaders(suiteData));
	}
	
	@Priority(4)
//	@Test(groups ={"CIQ","SANITY", "REACH_AGENT"},retryAnalyzer=RetryAnalyzer.class )
	public void gDriveUploadFilesCIQ_stressTest() throws Exception {
		Logger.info("==================================================================================");
		Logger.info("Upload CIQ File operation - Start ");
		Logger.info("==================================================================================");
		
		String [] ciqfiles={"pci.txt","pii.txt"};
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData));
		policy.createCIQPolicy(GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData));
		gda.homepage(getWebDriver(), suiteData);
		gda.selectFolder(getWebDriver(), "CIQ");
		for(int i=0; i<ciqfiles.length;i++)
		{
			gda.deleteFile(getWebDriver(), ciqfiles[i]);
		}
		for(int i=0; i<ciqfiles.length;i++){		
			gda.uploadFile(getWebDriver(),  System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+
				"resources"+File.separator+"ciq"+File.separator + ciqfiles[i]);
		
		}
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Content Inspection");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "File pii.txt download has risk(s) - PII, ContentIQ Violations");
		expectedDataMap.put(GatewayTestConstants.RISK, "PII, ContentIQ Violations");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "critical");
		expectedDataMap.put("content_checks", "CIQ_FE_GW_DontDelete");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		gda.homepage(getWebDriver(), suiteData);
		Thread.sleep(150000);
		policy.deletePolicy(GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData));
		Logger.info("==================================================================================");
		Logger.info("Upload CIQ File operation - complete ");
		Logger.info("==================================================================================");
		}
	// depricated method
	public void stresstest() throws Exception
	{
		String [] ciqfiles={"pci.txt","pii.txt"};
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData));
		policy.createCIQPolicy(GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData));
		gda.homepage(getWebDriver(), suiteData);
		gda.selectFolder(getWebDriver(), "CIQ");
		//gda.MultipleFileupload(getWebDriver(), ciqfiles);
		for(int i=0; i<ciqfiles.length;i++)
		{
			gda.deleteFile(getWebDriver(), ciqfiles[i]);
		}
		for(int i=0; i<ciqfiles.length;i++){
			//gda.login(getWebDriver(), suiteData);
			gda.uploadFile(getWebDriver(),  System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+
				"resources"+File.separator+"ciq"+File.separator + ciqfiles[i]);
		
		}
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Content Inspection");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "File pii.txt download has risk(s) - PII, ContentIQ Violations");
		expectedDataMap.put(GatewayTestConstants.RISK, "PII, ContentIQ Violations");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "critical");
		expectedDataMap.put("content_checks", "CIQ_FE_GW_DontDelete");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");

	}

}