package com.elastica.tests.o365;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.elastica.common.GWCommonTest;
import com.elastica.gateway.GatewayTestConstants;
import com.elastica.listeners.Priority;
import com.elastica.listeners.RetryAnalyzer;
import com.elastica.logger.Logger;

public class O365TestsEmailCIQ extends GWCommonTest{
	Map <String, Object> expectedDataMap = new HashMap<String, Object>();
	Map<String, String>policyDataMap= new HashMap<String, String>(); 
	String fromTime=null;
	CiqUtils utils=new CiqUtils();
	@BeforeMethod()
	public void clearDataMap(){
		fromTime=backend.getCurrentTime();
		expectedDataMap.clear();
		
	}

	@Priority(1)
	@Test(groups ={"MAIL_CIQ"})
	public void loginToCloudSocAppAndSetupSSO() throws Exception {
		fromTime=backend.getCurrentTime();
		Logger.info("Started performing activities on saas app");
		login.loginCloudSocPortal(getWebDriver(), suiteData);
		Logger.info("Finished login activities on cloudSoc");
	}
	
	@Priority(2)
	@Test(groups ={"MAIL_CIQ", "REACH"})  //,dependsOnMethods = { "loginToCloudSocAppAndSetupSSO" }
	public void o365ValidateLoginActivityEvent() throws Exception {
		Logger.info("Verifying the login event");
		o365Login.login(getWebDriver(), suiteData);
		
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Login");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User logged in"); 
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Session");
		expectedDataMap.put(GatewayTestConstants.REQ_URI, "https://login.microsoftonline.com/common/login");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("Login Event Successfull");
	}
	

	
	@Priority(3)
	@Test(groups ={"MAIL_CIQ", "REACH"})  //,dependsOnMethods = { "loginToCloudSocAppAndSetupSSO" }
	public void o365EmailAttachedWithFile() throws Exception {
		Logger.info("Email Attached via One Drive");
		utils.createCIQProfile( CIQConstants.ZERO,suiteData, "CIQ_FE_GW_DontDelete","CIQ_FE_GW_DontDelete",CIQConstants.SOURCE_DCI,CIQConstants.DEFAUTRISKVALUE);
		utils.createCIQProfile( CIQConstants.ZERO,suiteData, "GW_CIQ_DictionaryProfile","GW_CIQ_DictionaryProfile",CIQConstants.SOURCE_RISK,CIQConstants.RISKVALUE);

		fromTime=backend.getCurrentTime();
		policy.createCIQPolicy(GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData));
		policy.createCIQPolicy(GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+"DictionaryProfile", suiteData, backend.getHeaders(suiteData),"GW_CIQ_DictionaryProfile","upload");

		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.loadEmailApp(getWebDriver());
		// upload risk files
		String [] ciqfiles={"pci.txt",  "pii.txt", "glba.txt", "hipaa.txt", "vba_macro.xls","Illegal_Drugs.txt","Gambling.txt","FERPA_BaileyDoxed.txt"}; //source_code.xls
		for(int i=0; i<ciqfiles.length;i++){
			try{

				String filepath=System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+
						"resources"+File.separator+"ciq"+File.separator + ciqfiles[i];
				//o365HomeAction.loadEmailApp(getWebDriver());
				o365HomeAction.sendEmailWithAttach(getWebDriver(), ciqfiles[i]);
				Thread.sleep(5000);
				o365HomeAction.refresh(getWebDriver(), 5);
			}

			catch(Exception e) {

				Reporter.log("error in uploading  the file "+ciqfiles[i], true);
			}
		}
		String [] contentfiles={"divorce.txt","US_License_Plate_Number.txt","Java.txt","hdfc.txt","us_social_security_number.txt","design.pdf"};
		String	absLocation=System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+
				"resources"+File.separator+"ciq"+File.separator+"contentType"+File.separator;// + ciqfiles[i]

		for(int i=0; i<contentfiles.length;i++){
			try{
				o365HomeAction.loadEmailApp(getWebDriver());
				o365HomeAction.sendEmailWithAttachAsCopy(getWebDriver(), contentfiles[i]);
				Thread.sleep(5000);
				o365HomeAction.refresh(getWebDriver(), 5);
			}
			catch(Exception e) {

				Reporter.log("error in uploading  the file "+contentfiles[i], true);
			}
		}
		Thread.sleep(150000);
		policy.deletePolicy(GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData));
		policy.deletePolicy(GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+"DictionaryProfile", suiteData, backend.getHeaders(suiteData));

		//		Logger.info("Email Attached via One Drive Successful");
	}
	
	
	@Priority(3)
	@Test(groups ={"MAIL_CIQ", "REACH"})  //,dependsOnMethods = { "loginToCloudSocAppAndSetupSSO" }
	public void o365EmailRecieveCIQfile() throws Exception {
		Logger.info("Email Attached via One Drive");
		fromTime=backend.getCurrentTime();
		policy.createCIQPolicy(GatewayTestConstants.CIQ_DOWNLOAD_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData),"CIQ_FE_GW_DontDelete","download");
		policy.createCIQPolicy(GatewayTestConstants.CIQ_DOWNLOAD_POLICY_NAME+"DictionaryProfile", suiteData, backend.getHeaders(suiteData),"GW_CIQ_DictionaryProfile","download");

		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.loadEmailApp(getWebDriver());
		// upload risk files
				String [] ciqfiles={"pci.txt",  "pii.txt", "glba.txt", "hipaa.txt", "vba_macro.xls","Illegal_Drugs.txt","Gambling.txt","FERPA_BaileyDoxed.txt"}; //source_code.xls
				for(int i=0; i<ciqfiles.length;i++){
					try{
				
					String filepath=System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+
							"resources"+File.separator+"ciq"+File.separator + ciqfiles[i];
					//send mail to self
					o365HomeAction.sendEmailWithAttach(getWebDriver(), filepath);
					o365HomeAction.searchBySubjectAndDownload(getWebDriver(), ciqfiles[i], ciqfiles[i]);
					
					Thread.sleep(5000);
					o365HomeAction.refresh(getWebDriver(), 5);
					}
					
				 catch(Exception e) {

					 Reporter.log("error in uploading  the file "+ciqfiles[i], true);
				 }
				}
				String [] contentfiles={"divorce.txt","US_License_Plate_Number.txt","Java.txt","hdfc.txt","us_social_security_number.txt","design.pdf"};
				String	absLocation=System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+
						"resources"+File.separator+"ciq"+File.separator+"contentType"+File.separator;// + ciqfiles[i]

				for(int i=0; i<contentfiles.length;i++){
					try{
						o365HomeAction.loadEmailApp(getWebDriver());
						o365HomeAction.sendEmailWithAttach(getWebDriver(), absLocation+contentfiles[i]);
					Thread.sleep(5000);
					o365HomeAction.refresh(getWebDriver(), 5);
					}
					catch(Exception e) {

						 Reporter.log("error in uploading  the file "+contentfiles[i], true);
					 }
				}

				policy.deletePolicy(GatewayTestConstants.CIQ_DOWNLOAD_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData));
				policy.deletePolicy(GatewayTestConstants.CIQ_DOWNLOAD_POLICY_NAME+"DictionaryProfile", suiteData, backend.getHeaders(suiteData));

		Logger.info("Email Attached via One Drive Successful");
	}
	
	@Priority(3)
	@Test(groups ={"MAIL_CIQ", "REACH"})  //,dependsOnMethods = { "loginToCloudSocAppAndSetupSSO" }
	public void o365EmailsendEncryptedCIQfile() throws Exception {
		Logger.info("Email Attached via One Drive");
		fromTime=backend.getCurrentTime();
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENC_DEC_CIQ_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData));
		policy.activateEncryptionDecryptionCIQPolicy(GatewayTestConstants.ENC_DEC_CIQ_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData),"CIQ_FE_GW_DontDelete","upload");

		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.loadEmailApp(getWebDriver());
		// upload risk files
				String [] ciqfiles={"pci.txt",};//  "pii.txt", "glba.txt", "hipaa.txt", "vba_macro.xls","Illegal_Drugs.txt","Gambling.txt","FERPA_BaileyDoxed.txt"}; //source_code.xls
				for(int i=0; i<ciqfiles.length;i++){
					try{
				
					String filepath=System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+
							"resources"+File.separator+"ciq"+File.separator + ciqfiles[i];
					//send mail to self
					o365HomeAction.sendEmailWithAttach(getWebDriver(), filepath);
					o365HomeAction.searchBySubjectAndDownload(getWebDriver(), ciqfiles[i], ciqfiles[i]);
					
					Thread.sleep(5000);
					o365HomeAction.refresh(getWebDriver(), 5);
					}
					
				 catch(Exception e) {

					 Reporter.log("error in uploading  the file "+ciqfiles[i], true);
				 }
				}
//				String [] contentfiles={"divorce.txt","US_License_Plate_Number.txt","Java.txt","hdfc.txt","us_social_security_number.txt","design.pdf"};
//				String	absLocation=System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+
//						"resources"+File.separator+"ciq"+File.separator+"contentType"+File.separator;// + ciqfiles[i]
//
//				for(int i=0; i<contentfiles.length;i++){
//					try{
//						o365HomeAction.loadEmailApp(getWebDriver());
//						o365HomeAction.sendEmailWithAttach(getWebDriver(), absLocation+contentfiles[i]);
//					Thread.sleep(5000);
//					o365HomeAction.refresh(getWebDriver(), 5);
//					}
//					catch(Exception e) {
//
//						 Reporter.log("error in uploading  the file "+contentfiles[i], true);
//					 }
//				}

				policy.deletePolicy(GatewayTestConstants.CIQ_DOWNLOAD_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData));
				policy.deletePolicy(GatewayTestConstants.CIQ_DOWNLOAD_POLICY_NAME+"DictionaryProfile", suiteData, backend.getHeaders(suiteData));
				policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENC_DEC_CIQ_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData));
				/*Log Fields to check*/
				expectedDataMap.clear();
				setCommonFieldsInExpectedDataMap(expectedDataMap);
				//	setLocationFieldsInExpectedDataMap(expectedDataMap);
				expectedDataMap.put(GatewayTestConstants.MESSAGE, "File "+ciqfiles[0]+" encrypted on upload for user "+suiteData.getSaasAppUsername());
				//File "test_enc.txt" encrypted on upload for user gwadmin@securletautoo365featle.com
				expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "File Encryption" );
				expectedDataMap.put(GatewayTestConstants.POLICY_TYPE, GatewayTestConstants.FILE_TRANSFER_POLICY);
				//	expectedDataMap.put(GatewayTestConstants.FILE_SIZE, "455");
				expectedDataMap.put(GatewayTestConstants.POLICY_ACTION, GatewayTestConstants.POLICY_ACTION_ALERT);
				expectedDataMap.put(GatewayTestConstants.ACTION_TAKEN, GatewayTestConstants.ACTION_TAKEN_ENCRYPT );
				expectedDataMap.put(GatewayTestConstants.CRYPTO_KEY_VERSION, GatewayTestConstants.CRYPTO_KEY_VERSION_USED);
				expectedDataMap.put(GatewayTestConstants.OBJECT_NAME, ciqfiles[0]);
				expectedDataMap.put(GatewayTestConstants.CRYPTO_KEY_SERVER, GatewayTestConstants.CRYPTO_KEY_SERVER_USED);
				expectedDataMap.put(GatewayTestConstants.CRYPTO_KEY_NAME, GatewayTestConstants.CRYPTO_KEY_NAME_USED);
				expectedDataMap.remove(GatewayTestConstants.USER_NAME);
				Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Expected log: "+expectedDataMap.get(GatewayTestConstants.MESSAGE)+" does not exist");
				// check for the ciq logs
				/*Log Fields to check*/
				expectedDataMap.clear();
				expectedDataMap.put(GatewayTestConstants.MESSAGE, "File "+ciqfiles[0]+" upload has risk(s) - PII, ContentIQ Violations");
				expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Content Inspection");
				expectedDataMap.put(GatewayTestConstants.RISK, "PII, ContentIQ Violations");
			//	expectedDataMap.put(GatewayTestConstants.SEVERITY, "critical");
				Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
				
				Logger.info("==================================================================================");
				Logger.info(" Encrypt upload actvity event verification successful");
				Logger.info("==================================================================================");

		Logger.info("Email Attached via One Drive Successful");
	}
	


	
	
	@Priority(5)
	@Test(groups ={"REGRESSION","UPLOAD","REACH_AGENT"},retryAnalyzer=RetryAnalyzer.class) //,dependsOnMethods = { "o365_Test_01_ValidateLogingActivityEvent" }
	public void o365ValidateUploadSourceCode() throws Exception {
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
	public void o365ValidateVBA_MACRODownloadRisk() throws Exception {
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
	public void o365ValidateVBA_MACROUploadRisk() throws Exception {
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
	public void o365ValidateFERPAUploadRisk() throws Exception {
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
	public void o365ValidateFERPADownloadRisk() throws Exception {
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
	public void o365ValidateGLBADownloadCIQ() throws Exception {
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
	public void o365ValidateGLBAUpload_CIQ() throws Exception {
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
//	@Test(groups ={"REGRESSION","UPLOAD"},retryAnalyzer=RetryAnalyzer.class) //,dependsOnMethods = { "o365_Test_01_ValidateLogingActivityEvent" }
	public void o365ValidateImageUploadCI() throws Exception {
		Reporter.log("Verifying CIQ Upload activity event ", true);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "File image.jpeg upload has risk(s) - ContentIQ Violations");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Content Inspection");
		expectedDataMap.put(GatewayTestConstants.RISK, "ContentIQ Violations");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "critical");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("Verifying CIQ Upload activity event  successfull", true);
	}
	@Priority(8)
	//@Test(groups ={"REGRESSION","DOWNLOAD"},dependsOnMethods = {"downloadCIQFiles"},retryAnalyzer=RetryAnalyzer.class) //,dependsOnMethods = { "o365_Test_01_ValidateLogingActivityEvent" }
	public void o365ValidateImageDownloadCI() throws Exception {
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
	public void o365ValidateDrugsUploadCI() throws Exception {
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
	public void o365ValidateDrugsDownloadCI() throws Exception {
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
	public void o365ValidateGamblingUploadCI() throws Exception {
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
	public void o365ValidateGamblingDownloadCI() throws Exception {
		Reporter.log("Verifying CIQ download activity event ", true);
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
	public void o365ValidateCustomTermDivorceUpload() throws Exception {
		Reporter.log("Verifying CIQ Upload divorce.txt ", true);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "File divorce.txt upload has risk(s) - ContentIQ Violations");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Content Inspection");
		expectedDataMap.put(GatewayTestConstants.RISK, "ContentIQ Violations");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "critical");
		expectedDataMap.put("content_checks", "GW_CIQ_Divorce");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("Verifying CIQ Upload activity event  successfull", true);
	}
	@Priority(9)
	@Test(groups ={"REGRESSION","DOWNLOAD","REACH_AGENT"}, retryAnalyzer=RetryAnalyzer.class) 
	public void o365ValidateCustomTermDivorceDownload() throws Exception {
		Reporter.log("Verifying CIQ download divorce.txt ", true);
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
	public void o365ValidateCustomTermSSNUpload() throws Exception {
		Reporter.log("Verifying CIQ Upload US_SSN.txt ", true);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "File us_social_security_number.txt upload has risk(s) - PII, ContentIQ Violations");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Content Inspection");
		expectedDataMap.put(GatewayTestConstants.RISK, "ContentIQ Violations");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "critical");
		expectedDataMap.put("content_checks", "CIQ_FE_GW_DontDelete");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("Verifying CIQ Upload activity event  successfull", true);
	}
	@Priority(9)
	@Test(groups ={"REGRESSION","DOWNLOAD","REACH_AGENT"}, retryAnalyzer=RetryAnalyzer.class) 
	public void o365ValidateCustomTermSSNDownload() throws Exception {
		Reporter.log("Verifying CIQ download US_SSN.txt ", true);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "File us_social_security_number.txt download has risk(s) - PII, ContentIQ Violations");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Content Inspection");
		expectedDataMap.put(GatewayTestConstants.RISK, "ContentIQ Violations");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "critical");
		expectedDataMap.put("content_checks", "CIQ_FE_GW_DontDelete");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("Verifying CIQ download activity event  successfull", true);
	}

	
	
	@Priority(3)
	//@Test(groups ={"MAIL_CIQ"}, retryAnalyzer=RetryAnalyzer.class) //dependsOnMethods = { "o365_Test_01_ValidateLogingActivityEvent" }
	public void o365_Test_02_ValidateSendEmailActivityEvent() throws Exception {
		Reporter.log("==================================================================================",true);
		Reporter.log(" Verifying email send event in Outlook", true);
		Reporter.log("==================================================================================",true);
		fromTime=backend.getCurrentTime();
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.loadEmailApp(getWebDriver());
		o365HomeAction.sendEmail(getWebDriver());
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User sent an email to admin@gatewayO365beatle.com");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Send");
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Email");
		expectedDataMap.put(GatewayTestConstants.REFERER_URI, "");
		expectedDataMap.put(GatewayTestConstants.SHARE_WITH, "admin@gatewayO365beatle.com");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Expected log: "+expectedDataMap.get(GatewayTestConstants.MESSAGE)+" does not exist");
		Reporter.log("==================================================================================",true);
		Reporter.log(" Send Email event verification successful", true);
		Reporter.log("==================================================================================",true);
	}
	
	@Priority(4)
//	@Test(groups ={"MAIL_CIQ"}) //,dependsOnMethods = { "o365_Test_01_ValidateLogingActivityEvent" }
	public void o365_Test_02_ValidateDraftEmailActivityEvent() throws Exception {
		Logger.info("Verifying email save draft event");
		fromTime=backend.getCurrentTime();
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.loadEmailApp(getWebDriver());
		o365HomeAction.draftEmail(getWebDriver());
		
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
		Logger.info("Draft Email event verification successfull");
	}
	
	@Priority(7)
//	@Test(groups ={"MAIL_CIQ"})  
	public void o365_Test_EmailDecrypted_FileDownload() throws Exception {
		String decryptedFile = "download.pdf.eef";
		Logger.info("Email Download and Decrypt File");
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getGWHeaders(suiteData));
		policy.createEnableEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getGWHeaders(suiteData));
		fromTime=backend.getCurrentTime();
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.loadEmailApp(getWebDriver());
		o365HomeAction.searchBySubjectAndDownload(getWebDriver(), "decrypted file", decryptedFile);
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "File Decryption" );
		expectedDataMap.put(GatewayTestConstants.ACTION_TAKEN, "decrypt");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "File " + decryptedFile + " decrypted on download for user "+suiteData.getSaasAppUsername()+".");
		expectedDataMap.put(GatewayTestConstants.POLICY_TYPE, "FileTransfer");
		expectedDataMap.put(GatewayTestConstants.OBJECT_NAME, GatewayTestConstants.O365_DOWNLOAD_ENCRYPTED_FILE );
		expectedDataMap.remove(GatewayTestConstants.COUNTRY);
		expectedDataMap.remove(GatewayTestConstants.USER_NAME);
		expectedDataMap.put(GatewayTestConstants.REFERER_URI, "");
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getGWHeaders(suiteData));
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Expected log: "+expectedDataMap.get(GatewayTestConstants.MESSAGE)+" does not exist");
		Logger.info("Email Download and Decrypt File Successful");
	}
	
	@Priority(7)
//	@Test(groups ={"MAIL_CIQ"})  
	public void o365_Test_Email_FileDownload_Normal() throws Exception {
		Logger.info("Email Download File");
		fromTime=backend.getCurrentTime();
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.loadEmailApp(getWebDriver());
		o365HomeAction.searchBySubjectAndDownloadFile(getWebDriver(), "Normal download email", GatewayTestConstants.O365_FILE);
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Download");
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Document");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User downloaded attached file " + GatewayTestConstants.O365_FILE);
		expectedDataMap.put(GatewayTestConstants.FILE_SIZE, "287");
		expectedDataMap.put(GatewayTestConstants.OBJECT_NAME, "Dial-FAQ.pdf");
		expectedDataMap.put(GatewayTestConstants.CONTENT_INLINE, "1");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Expected log: "+expectedDataMap.get(GatewayTestConstants.MESSAGE)+" does not exist");
		Reporter.log("==================================================================================",true);
		Reporter.log("Download activity event verification successful", true);
		Reporter.log("==================================================================================",true);
	}
	
	@Priority(7)
	@Test(groups ={"MAIL_CIQ", "REACH"})  
	public void o365_Test_Email_FileDownload_Normal_ViaOneDrive() throws Exception {
		Logger.info("Email Download File Via One Drive");
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getGWHeaders(suiteData));
		policy.createEnableEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getGWHeaders(suiteData));
		fromTime=backend.getCurrentTime();
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.loadEmailApp(getWebDriver());
		o365HomeAction.searchBySubjectAndDownloadFile(getWebDriver(), "This is subject with attached", GatewayTestConstants.O365_FILE);
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Download");
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User downloaded attached Onedrive file "+ GatewayTestConstants.O365_FILE + " from an email");
		expectedDataMap.put(GatewayTestConstants.FILE_SIZE, "409");
		expectedDataMap.put(GatewayTestConstants.OBJECT_NAME, "Dial-FAQ.pdf");
		expectedDataMap.put(GatewayTestConstants.REFERER_URI, "");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Expected log: "+expectedDataMap.get(GatewayTestConstants.MESSAGE)+" does not exist");
		Reporter.log("==================================================================================",true);
		Reporter.log("Download activity event verification successful", true);
		Reporter.log("==================================================================================",true);
	}
	
	@Priority(8)
//	@Test(groups ={"MAIL_CIQ_ENCRYPTION"}) 
	public void o365_Test_EmailAttached_Via_System_WithFileEncryption() throws Exception {
		Logger.info("Email Attached via One Drive");
		fromTime=backend.getCurrentTime();
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.loadEmailApp(getWebDriver());
		
		o365HomeAction.sendEmailWithAttach(getWebDriver(), GatewayTestConstants.GDRIVE_ONE_DRIVE_FILE_ATTACH);
		o365HomeAction.uploadItemFile(getWebDriver(),  System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+
				"resources"+File.separator+"meta"+File.separator + GatewayTestConstants.O365_UPLOAD_ENCRYPT_FILE);
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Upload");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User attached file "+ GatewayTestConstants.GDRIVE_ONE_DRIVE_FILE_ATTACH +" to an email");
		expectedDataMap.put(GatewayTestConstants.OBJECT_NAME, GatewayTestConstants.GDRIVE_ONE_DRIVE_FILE_ATTACH);
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File");
		expectedDataMap.put(GatewayTestConstants.REFERER_URI, "");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("Email Attached via One Drive Successful");
	}
	
	@Priority(9)
	@Test(groups ={"Regression6", "REACH"}) 
	public void o365_Test_Create_Folder() throws Exception {
		fromTime=backend.getCurrentTime();
		Logger.info("Delete Folder If Exist");
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.loadEmailApp(getWebDriver());
		o365HomeAction.emailDeleteFolder(getWebDriver(), GatewayTestConstants.O365_EMAIL_NEW_FOLDER);
		Logger.info("Create Folder");
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.loadEmailApp(getWebDriver());
		o365HomeAction.emailCreateFolder(getWebDriver(), GatewayTestConstants.O365_EMAIL_NEW_FOLDER);
		
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.loadEmailApp(getWebDriver());
		o365HomeAction.removeFromFavorites(getWebDriver(), GatewayTestConstants.O365_EMAIL_NEW_FOLDER);
		
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.loadEmailApp(getWebDriver());
		o365HomeAction.emailRenameFolder(getWebDriver(), GatewayTestConstants.O365_EMAIL_NEW_FOLDER);
		
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.loadEmailApp(getWebDriver());
		o365HomeAction.emptyFolder(getWebDriver(), GatewayTestConstants.O365_EMAIL_NEW_FOLDER);
		
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.loadEmailApp(getWebDriver());
		o365HomeAction.emailDeleteFolder(getWebDriver(), GatewayTestConstants.O365_EMAIL_NEW_FOLDER);
		
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User created a folder named " + GatewayTestConstants.O365_EMAIL_NEW_FOLDER);
		expectedDataMap.put(GatewayTestConstants.REFERER_URI, "");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("Create Folder Successful");
	}
	
	@Priority(10)
	@Test(groups ={"Regression6", "REACH"}) 
	public void o365_Test_Remove_Favorites() throws Exception {
		Logger.info("Remove Favorites");
		
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "Remove favorites " + GatewayTestConstants.O365_EMAIL_NEW_FOLDER);
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("Remove Favorites Successful");
	}
	
	@Priority(11)
	@Test(groups ={"Regression6", "REACH"}) 
	public void o365_Test_Rename_Folder() throws Exception {
		Logger.info("Rename Folder");
		
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User renamed folder " + GatewayTestConstants.O365_EMAIL_NEW_FOLDER + " to RenameFolder");
		expectedDataMap.put(GatewayTestConstants.REFERER_URI, "");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("Rename Folder Successful");
	}
	
	@Priority(12)
	@Test(groups ={"Regression6", "REACH"}) 
	public void o365_Test_Empty_Folder() throws Exception {
		Logger.info("Empty Folder");
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "Empty folder " + GatewayTestConstants.O365_EMAIL_NEW_FOLDER);
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("Remove Favorites Successful");
	}
	
	@Priority(13)
	@Test(groups ={"Regression6", "REACH"}) 
	public void o365_Test_Delete_Folder() throws Exception {
		Logger.info("Remove Folder");
		
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User deleted folder " + GatewayTestConstants.O365_EMAIL_NEW_FOLDER);
		expectedDataMap.put(GatewayTestConstants.REFERER_URI, "");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("Remove Folder Successful");
	}
	
	@Priority(14)
	@Test(groups ={"Regression6", "REACH"}) 
	public void o365_Test_Sub_Folder_Create() throws Exception {
		fromTime=backend.getCurrentTime();
		Logger.info("Create Sub Folder");
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.loadEmailApp(getWebDriver());
		o365HomeAction.emailDeleteSubFolder(getWebDriver(), GatewayTestConstants.O365_EMAIL_SUB_FOLDER );
		
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.loadEmailApp(getWebDriver());
		o365HomeAction.emailCreateSubFolder(getWebDriver(), "subfolder", GatewayTestConstants.O365_EMAIL_SUB_FOLDER);
		
		Logger.info("Add to Favorite");
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.loadEmailApp(getWebDriver());
		o365HomeAction.emailAddFavoriteSubFolder(getWebDriver(), GatewayTestConstants.O365_EMAIL_SUB_FOLDER );
		
/*		Logger.info("Move To Favorite");
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.loadEmailApp(getWebDriver());
		o365HomeAction.emailMoveSubFolder(getWebDriver(), GatewayTestConstants.O365_EMAIL_SUB_FOLDER, "AFolder");*/
		
		Logger.info("Rename SubFolder");
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.loadEmailApp(getWebDriver());
		o365HomeAction.emailRenameSubFolder(getWebDriver(), GatewayTestConstants.O365_EMAIL_SUB_FOLDER );
		
		Logger.info("Empty SubFolder");
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.loadEmailApp(getWebDriver());
		o365HomeAction.emailEmptySubFolder(getWebDriver(), GatewayTestConstants.O365_EMAIL_SUB_FOLDER );
		
		Logger.info("Delete SubFolder");
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.loadEmailApp(getWebDriver());
		o365HomeAction.emailDeleteSubFolder(getWebDriver(), GatewayTestConstants.O365_EMAIL_SUB_FOLDER );
		
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.IS_ANONYMOUS_PROXY, "false");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User created a folder named " + GatewayTestConstants.O365_EMAIL_SUB_FOLDER);
		expectedDataMap.put(GatewayTestConstants.REFERER_URI, "");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("Create Sub Folder Successful");
	}
	
	@Priority(15)
	@Test(groups ={"Regression6", "REACH"}) 
	public void o365_Test_Sub_Folder_Add_Favorite() throws Exception {
		Logger.info("Sub Folder Add Favorite");
		
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.IS_ANONYMOUS_PROXY, "false");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "Add favorite " + GatewayTestConstants.O365_EMAIL_SUB_FOLDER);
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("Sub Folder Add Favorite Successful");
	}
		
	@Priority(16)
	@Test(groups ={"Regression6", "REACH"}) 
	public void o365_Test_Sub_Folder_Rename() throws Exception {
		Logger.info("Sub Folder Rename");
		
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.IS_ANONYMOUS_PROXY, "false");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User renamed folder " + GatewayTestConstants.O365_EMAIL_SUB_FOLDER);
		expectedDataMap.put(GatewayTestConstants.REFERER_URI, "");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("Sub Folder Rename Successful");
	}
	
	@Priority(17)
	@Test(groups ={"Regression6"}) 
	public void o365_Test_Sub_Folder_Empty_Folder() throws Exception {
		Logger.info("Sub Folder Empty");
		
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.IS_ANONYMOUS_PROXY, "false");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "Empty subfolder " + GatewayTestConstants.O365_EMAIL_SUB_FOLDER);
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("Sub Folder Empty Successful");
	}
	
	@Priority(18)
	@Test(groups ={"Regression6", "REACH"}) 
	public void o365_Test_Delete_Sub_Folder() throws Exception {
		Logger.info("Delete Sub Folder");
		
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.IS_ANONYMOUS_PROXY, "false");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User deleted folder " + GatewayTestConstants.O365_EMAIL_SUB_FOLDER);
		expectedDataMap.put(GatewayTestConstants.REFERER_URI, "");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("Delete Sub Folder Successful");
	}
	
	
	public String getSaasAppUserName(){
		return suiteData.getSaasAppUsername().replaceAll("@", "_");
	}
	
	@BeforeClass(groups ={"Regression6", "REACH"})
	public void doBeforeClass() throws Exception {
		Logger.info("Delete Policy Before Test ");
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME+getSaasAppUserName(),
				suiteData, backend.getGWHeaders(suiteData));
		
		policy.createCIQPolicy(GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData));
		policy.createCIQPolicy(GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+"DictionaryProfile", suiteData, backend.getHeaders(suiteData),"GW_CIQ_DictionaryProfile","upload");
		policy.createCIQPolicy(GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+"DivorcePolicy", suiteData, backend.getHeaders(suiteData),"GW_CIQ_Divorce","upload");
		policy.createCIQPolicy(GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+"GamblingPolicy", suiteData, backend.getHeaders(suiteData),"GW_CIQ_GamblingDictionaryProfile","upload");
		policy.createCIQPolicy(GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+"DrugsPolicy", suiteData, backend.getHeaders(suiteData),"GW_CIQ_Drugs_GamblingDictionaryProfile","upload");
		policy.createCIQPolicy(GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+"USLincencPlate", suiteData, backend.getHeaders(suiteData),"GW_CIQ_UssnLiscencePlate","upload");		
		policy.createCIQPolicy(GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+"HDFCPolicy", suiteData, backend.getHeaders(suiteData),"GW_CIQ_HDFCProfile","upload");
	

	}
	
	@AfterClass(groups ={"Regression6"})
	public void doAfterClass() throws Exception {
		Logger.info("Delete Policy After Test ");
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME+getSaasAppUserName(),
				suiteData, backend.getGWHeaders(suiteData));
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME+getSaasAppUserName(),
				suiteData, backend.getGWHeaders(suiteData));
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME+getSaasAppUserName(),
				suiteData, backend.getGWHeaders(suiteData));
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME+getSaasAppUserName(),
				suiteData, backend.getGWHeaders(suiteData));
		

	}
}