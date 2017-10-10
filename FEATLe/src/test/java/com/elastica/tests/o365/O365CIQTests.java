package com.elastica.tests.o365;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.entity.StringEntity;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;
import org.testng.Reporter;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.elastica.common.GWCommonTest;
import com.elastica.common.SuiteData;
import com.elastica.constants.dci.DCIConstants;
import com.elastica.gateway.GatewayTestConstants;
import com.elastica.listeners.Priority;
import com.elastica.listeners.RetryAnalyzer;
import com.elastica.logger.Logger;
import com.elastica.restClient.Client;
import com.elastica.restClient.ClientUtil;


public class O365CIQTests extends GWCommonTest{
	Map <String, Object> expectedDataMap = new HashMap<String, Object>();
	Map<String, String>policyDataMap= new HashMap<String, String>(); 
	String fromTime=null;
	SoftAssert softAssert = new SoftAssert();
	Client restClient= new Client();
	CiqUtils utils=new CiqUtils();
	@BeforeClass(alwaysRun= true)
	public void clearDataMap() throws Exception{
		fromTime=backend.getCurrentTime();
		expectedDataMap.clear();	
		
		Map<String,String> fileDetails=new HashMap<String,String>();
		fileDetails.put("fileName", "divorce.txt");
		fileDetails.put("filePath", "FEATLE/src/test/resources/ciq/contentType/divorce.txt");
		List<NameValuePair> headers=dci.getCookieHeaders(suiteData);
		List<String> keywords= new ArrayList<String>();
		keywords.add("Divorce");
		// create ciq risk profile
		utils.createCIQProfile( CIQConstants.ZERO,suiteData, "CIQ_FE_GW_DontDelete","CIQ_FE_GW_DontDelete",CIQConstants.SOURCE_DCI,CIQConstants.DEFAUTRISKVALUE);
		utils.createCIQProfile( CIQConstants.ZERO,suiteData, "GW_CIQ_DictionaryProfile","GW_CIQ_DictionaryProfile",CIQConstants.SOURCE_RISK,CIQConstants.RISKVALUE);
		utils.createCIQProfile(CIQConstants.ONE, suiteData, "GW_CIQ_Divorce","GW_CIQ_Divorce",CIQConstants.SOURCE_DIVORCE,CIQConstants.VALUE_DIVORCE);
		utils.createCIQProfile(CIQConstants.ONE, suiteData, "GW_CIQ_GamblingDictionaryProfile","GW_CIQ_GamblingDictionaryProfile",CIQConstants.SOURCE_DRUG_GAMBLE,CIQConstants.VALUE_GAMBLE);
		utils.createCIQProfile(CIQConstants.ONE, suiteData, "GW_CIQ_DrugsDictionaryProfile","GW_CIQ_DrugsDictionaryProfile",CIQConstants.SOURCE_DRUG_GAMBLE,CIQConstants.VALUE_DRUG);
		utils.createCIQProfile(CIQConstants.ONE,suiteData, "GW_CIQ_UssnLiscencePlate","GW_CIQ_UssnLiscencePlate",CIQConstants.SOURCE_USLISCENCE,CIQConstants.VALUE_US_LISCENCE_PLT);
		utils.createCIQProfile(CIQConstants.ONE,suiteData, "GW_CIQ_HDFCProfile","GW_CIQ_HDFCProfile",CIQConstants.SOURCE_HDFC,CIQConstants.VALUE_HDFC);		
		Logger.info("Creating CIQ profile with only content is completed");	
		policy.deletePolicy(GatewayTestConstants.ENC_DEC_CIQ_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData));
		//createCIQProfileWithContentTypes( restClient,suiteData,ciqProfileName);
	}
	
	
	
		

	@Priority(1)
	@Test(groups ={"CIQ","REGRESSION","DOWNLOAD","UPLOAD","500mbCIQ","ENC_DEC_CIQ","CIQ_EXCEPTION"})
	public void loginToCloudSocAppAndSetupSSO() throws Exception {
 		fromTime=backend.getCurrentTime();
		Reporter.log("Started performing activities on saas app", true);
		login.loginCloudSocPortal(getWebDriver(), suiteData);
		Reporter.log("Finished login activities on cloudSoc", true);
	}
	
	@Priority(2)
	@Test(groups ={"CIQ","REGRESSION","DOWNLOAD","UPLOAD","500mbCIQ","ENC_DEC_CIQ","CIQ_EXCEPTION","REACH_AGENT"})  //,dependsOnMethods = { "loginToCloudSocAppAndSetupSSO" }
	public void o365_Test_01_ValidateLoginActivityEvent() throws Exception {
		Reporter.log("Verifying the login event", true);
		o365Login.login(getWebDriver(), suiteData);
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.loadOnedriveApp(getWebDriver());
		Thread.sleep(15000);
		policy.createCIQPolicy(GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+"DivorcePolicy", suiteData, backend.getHeaders(suiteData),"GW_CIQ_Divorce","upload");
		policy.createCIQPolicy(GatewayTestConstants.CIQ_DOWNLOAD_POLICY_NAME+"DivorcePolicy", suiteData, backend.getHeaders(suiteData),"GW_CIQ_Divorce","download");
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Login");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User logged in"); 
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Session");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Expected log: "+expectedDataMap.get(GatewayTestConstants.MESSAGE)+" does not exist on investigate");
		Logger.info("==================================================================================");
		Logger.info(" Login event verification successful");
		Logger.info("==================================================================================");

		Reporter.log("Login event verification successfull", true);
	}
	
	
	@Priority(3)
	//@Test(groups ={"CIQ"}, dataProvider = "_ciq") //,dependsOnMethods = { "o365_Test_01_ValidateLogingActivityEvent" }
	public void o365_Test_06_ValidateCIQ(String fileName) throws Exception {
		Reporter.log("Verifying CIQ for the file "+fileName, true);
		Reporter.log("Uploading the file in progress "+fileName, true);
		o365HomeAction.uploadItemFile(getWebDriver(),  System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+
				"resources"+File.separator+"ciq"+File.separator + fileName);
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.loadOnedriveApp(getWebDriver());
		Reporter.log("File upload completed" +fileName, true);
	}
	
		
	
	@Priority(3)
	@Test(groups ={"CIQ","REGRESSION","UPLOAD","REACH_AGENT"}) //,dependsOnMethods = { "o365_Test_01_ValidateLogingActivityEvent" }
	public void o365UploadCIQFiles() throws Exception {
		
		o365HomeAction.deleteAllFiles(getWebDriver());
		//create new policies
		try{
		policy.createCIQPolicy(GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData));
		policy.createCIQPolicy(GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+"DictionaryProfile", suiteData, backend.getHeaders(suiteData),"GW_CIQ_DictionaryProfile","upload");
		policy.createCIQPolicy(GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+"GamblingPolicy", suiteData, backend.getHeaders(suiteData),"GW_CIQ_GamblingDictionaryProfile","upload");
		policy.createCIQPolicy(GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+"DrugsPolicy", suiteData, backend.getHeaders(suiteData),"GW_CIQ_DrugsDictionaryProfile","upload");
		policy.createCIQPolicy(GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+"USLincencPlate", suiteData, backend.getHeaders(suiteData),"GW_CIQ_UssnLiscencePlate","upload");		
		policy.createCIQPolicy(GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+"HDFCPolicy", suiteData, backend.getHeaders(suiteData),"GW_CIQ_HDFCProfile","upload");
	
		}
		catch(Exception e) {

			 Reporter.log("error in policy creation", true);
		 }
		// upload risk files
		String [] ciqfiles={"pci.txt",  "pii.txt", "glba.txt", "hipaa.txt", "vba_macro.xls","Illegal_Drugs.txt","Gambling.txt","FERPA_BaileyDoxed.txt","pii_risks.rtf"}; //source_code.xls
		for(int i=0; i<ciqfiles.length;i++){
			try{
			o365HomeAction.uploadItemFile(getWebDriver(),  System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+
					"resources"+File.separator+"ciq"+File.separator + ciqfiles[i]);
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
			o365HomeAction.uploadItemFile(getWebDriver(),  absLocation + contentfiles[i]);
			Thread.sleep(5000);
			o365HomeAction.refresh(getWebDriver(), 5);
			}
			catch(Exception e) {

				 Reporter.log("error in uploading  the file "+contentfiles[i], true);
			 }
		}

		//o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		//Delete old policies
		Thread.sleep(1500000);
		policy.deletePolicy(GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData));
		policy.deletePolicy(GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+"DictionaryProfile", suiteData, backend.getHeaders(suiteData));
		policy.deletePolicy(GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+"DivorcePolicy", suiteData, backend.getHeaders(suiteData));
		policy.deletePolicy(GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+"GamblingPolicy", suiteData, backend.getHeaders(suiteData));
		policy.deletePolicy(GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+"DrugsPolicy", suiteData, backend.getHeaders(suiteData));	
		policy.deletePolicy(GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+"USLincencPlate", suiteData, backend.getHeaders(suiteData));
		policy.deletePolicy(GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+"HDFCPolicy", suiteData, backend.getHeaders(suiteData));
		
		

		}
	@Priority(17)
	@Test(groups ={"SANITY", "CIQ_EXCEPTION", "EXTERNAL"}) 
	public void o365ValidateCIQRiskEncryption() throws Exception {
		Logger.info("==================================================================================");
		Logger.info(" Verifying encrypt upload activity event");
		Logger.info("==================================================================================");
		fromTime=backend.getCurrentTime();
		String encryptFileForUpload="pii.txt";
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENC_DEC_CIQ_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData));
		policy.deletePolicy(GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+"DictionaryProfile", suiteData, backend.getHeaders(suiteData));
		policy.deletePolicy(GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+"DivorcePolicy", suiteData, backend.getHeaders(suiteData));
		policy.deletePolicy(GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+"GamblingPolicy", suiteData, backend.getHeaders(suiteData));
		policy.deletePolicy(GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+"DrugsPolicy", suiteData, backend.getHeaders(suiteData));	
		policy.deletePolicy(GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+"USLincencPlate", suiteData, backend.getHeaders(suiteData));
		policy.deletePolicy(GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+"HDFCPolicy", suiteData, backend.getHeaders(suiteData));
		
		policy.createCIQPolicyWhitelist(GatewayTestConstants.CIQ_EXCEPTION_NAME+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData),"ABA_Routing_Profile","upload","CIQ_FE_GW_DontDelete");
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.loadOnedriveApp(getWebDriver());
		//o365HomeAction.deleteFile(getWebDriver(), GatewayTestConstants.O365_DELETE_ENCRYPTED_FILE);
		try
		{
		o365HomeAction.deleteFile(getWebDriver(), encryptFileForUpload+".eef");
		o365HomeAction.deleteFile(getWebDriver(), encryptFileForUpload);
		}
		catch(Exception e)
		{
			Logger.info("error file not found");
		}
			o365HomeAction.uploadItemFile(getWebDriver(),  System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+
				"resources"+File.separator+"ciq"+File.separator + encryptFileForUpload);
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
//		o365HomeAction.loadOnedriveApp(getWebDriver());
		//	o365HomeAction.deleteFile(getWebDriver(), GatewayTestConstants.O365_UPLOAD_ENCRYPT_FILE);
		/*Log Fields to check*/
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		//	setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "File "+encryptFileForUpload+" encrypted on upload for user "+suiteData.getSaasAppUsername()+".");
		//File "test_enc.txt" encrypted on upload for user gwadmin@securletautoo365featle.com
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "File Encryption" );
		expectedDataMap.put(GatewayTestConstants.POLICY_TYPE, GatewayTestConstants.FILE_TRANSFER_POLICY);
		//	expectedDataMap.put(GatewayTestConstants.FILE_SIZE, "455");
		expectedDataMap.put(GatewayTestConstants.POLICY_ACTION, GatewayTestConstants.POLICY_ACTION_ALERT);
		expectedDataMap.put(GatewayTestConstants.ACTION_TAKEN, GatewayTestConstants.ACTION_TAKEN_ENCRYPT );
		expectedDataMap.put(GatewayTestConstants.CRYPTO_KEY_VERSION, GatewayTestConstants.CRYPTO_KEY_VERSION_USED);
		expectedDataMap.put(GatewayTestConstants.OBJECT_NAME, encryptFileForUpload);
		expectedDataMap.put(GatewayTestConstants.CRYPTO_KEY_SERVER, GatewayTestConstants.CRYPTO_KEY_SERVER_USED);
		expectedDataMap.put(GatewayTestConstants.CRYPTO_KEY_NAME, GatewayTestConstants.CRYPTO_KEY_NAME_USED);
		expectedDataMap.remove(GatewayTestConstants.USER_NAME);
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Expected log: "+expectedDataMap.get(GatewayTestConstants.MESSAGE)+" does not exist");
		// check for the ciq logs
		/*Log Fields to check*/
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "File "+encryptFileForUpload+" upload has risk(s) - PII, ContentIQ Violations");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Content Inspection");
		expectedDataMap.put(GatewayTestConstants.RISK, "PII, ContentIQ Violations");
	//	expectedDataMap.put(GatewayTestConstants.SEVERITY, "critical");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		
		Logger.info("==================================================================================");
		Logger.info(" Encrypt upload actvity event verification successful");
		Logger.info("==================================================================================");
	}

	@Priority(17)
	@Test(groups ={"SANITY", "ENC_DEC_CIQ", "EXTERNAL"}) 
	public void o365ValidateCIQException() throws Exception {
		Logger.info("==================================================================================");
		Logger.info(" Verifying encrypt upload activity event");
		Logger.info("==================================================================================");
		fromTime=backend.getCurrentTime();
		String encryptFileForUpload="pii.txt";
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENC_DEC_CIQ_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData));
		policy.activateEncryptionDecryptionCIQPolicy(GatewayTestConstants.ENC_DEC_CIQ_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData),"CIQ_FE_GW_DontDelete","upload");
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.loadOnedriveApp(getWebDriver());
		//o365HomeAction.deleteFile(getWebDriver(), GatewayTestConstants.O365_DELETE_ENCRYPTED_FILE);
		try
		{
		o365HomeAction.deleteFile(getWebDriver(), encryptFileForUpload+".eef");
		o365HomeAction.deleteFile(getWebDriver(), encryptFileForUpload);
		}
		catch(Exception e)
		{
			Logger.info("error file not found");
		}
			o365HomeAction.uploadItemFile(getWebDriver(),  System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+
				"resources"+File.separator+"ciq"+File.separator + encryptFileForUpload);
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
//		o365HomeAction.loadOnedriveApp(getWebDriver());
		//	o365HomeAction.deleteFile(getWebDriver(), GatewayTestConstants.O365_UPLOAD_ENCRYPT_FILE);
		/*Log Fields to check*/
		Thread.sleep(150000);
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENC_DEC_CIQ_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData));
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		//	setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "File "+encryptFileForUpload+" encrypted on upload for user "+suiteData.getSaasAppUsername()+".");
		//File "test_enc.txt" encrypted on upload for user gwadmin@securletautoo365featle.com
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "File Encryption" );
		expectedDataMap.put(GatewayTestConstants.POLICY_TYPE, GatewayTestConstants.FILE_TRANSFER_POLICY);
		//	expectedDataMap.put(GatewayTestConstants.FILE_SIZE, "455");
		expectedDataMap.put(GatewayTestConstants.POLICY_ACTION, GatewayTestConstants.POLICY_ACTION_ALERT);
		expectedDataMap.put(GatewayTestConstants.ACTION_TAKEN, GatewayTestConstants.ACTION_TAKEN_ENCRYPT );
		expectedDataMap.put(GatewayTestConstants.CRYPTO_KEY_VERSION, GatewayTestConstants.CRYPTO_KEY_VERSION_USED);
		expectedDataMap.put(GatewayTestConstants.OBJECT_NAME, encryptFileForUpload);
		expectedDataMap.put(GatewayTestConstants.CRYPTO_KEY_SERVER, GatewayTestConstants.CRYPTO_KEY_SERVER_USED);
		expectedDataMap.put(GatewayTestConstants.CRYPTO_KEY_NAME, GatewayTestConstants.CRYPTO_KEY_NAME_USED);
		expectedDataMap.remove(GatewayTestConstants.USER_NAME);
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Expected log: "+expectedDataMap.get(GatewayTestConstants.MESSAGE)+" does not exist");
		// check for the ciq logs
		/*Log Fields to check*/
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "File "+encryptFileForUpload+" upload has risk(s) - PII, ContentIQ Violations");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Content Inspection");
		expectedDataMap.put(GatewayTestConstants.RISK, "PII, ContentIQ Violations");
	//	expectedDataMap.put(GatewayTestConstants.SEVERITY, "critical");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		
		Logger.info("==================================================================================");
		Logger.info(" Encrypt upload actvity event verification successful");
		Logger.info("==================================================================================");
	}

	
	@Priority(4)
	@Test(groups ={"REGRESSION","DOWNLOAD","REACH_AGENT"}) //,dependsOnMethods = { "o365_Test_01_ValidateLogingActivityEvent" }
	public void o365DownloadCIQfiles() throws Exception {
		try{
		//create new policies
		policy.createCIQPolicy(GatewayTestConstants.CIQ_DOWNLOAD_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData),"CIQ_FE_GW_DontDelete","download");
		policy.createCIQPolicy(GatewayTestConstants.CIQ_DOWNLOAD_POLICY_NAME+"DictionaryProfile", suiteData, backend.getHeaders(suiteData),"GW_CIQ_DictionaryProfile","download");
		
		policy.createCIQPolicy(GatewayTestConstants.CIQ_DOWNLOAD_POLICY_NAME+"GamblingPolicy", suiteData, backend.getHeaders(suiteData),"GW_CIQ_GamblingDictionaryProfile","download");
		policy.createCIQPolicy(GatewayTestConstants.CIQ_DOWNLOAD_POLICY_NAME+"DrugsPolicy", suiteData, backend.getHeaders(suiteData),"GW_CIQ_DrugsDictionaryProfile","download");
		policy.createCIQPolicy(GatewayTestConstants.CIQ_DOWNLOAD_POLICY_NAME+"USLincencPlate", suiteData, backend.getHeaders(suiteData),"GW_CIQ_UssnLiscencePlate","download");
		policy.createCIQPolicy(GatewayTestConstants.CIQ_DOWNLOAD_POLICY_NAME+"HDFCPolicy", suiteData, backend.getHeaders(suiteData),"GW_CIQ_HDFCProfile","download");
		}
		catch(Exception e) {
			 Reporter.log("error in policy creation ", true);
		 }
		// download files
		String [] ciqfiles={"pci.txt", "pii.txt", "glba.txt", "hipaa.txt", "vba_macro.xls","divorce.txt","US_License_Plate_Number.txt","Java.txt","design.pdf","hdfc.txt","Illegal_Drugs.txt","Gambling.txt","FERPA_BaileyDoxed.txt", "pii_risks.rtf"}; //source_code.xls

			for(int i=0; i<ciqfiles.length;i++){
			try{
				o365HomeAction.downloadItemFileByName(getWebDriver(), ciqfiles[i]);

				o365HomeAction.refresh(getWebDriver(), 5);
			}
			catch(Exception e) {

				Reporter.log("error in uploading  the file "+ciqfiles[i], true);
			}
			
	
		}

		String [] contentfiles={"divorce.txt","US_License_Plate_Number.txt","Java.txt","design.pdf","hdfc.txt","us_social_security_number.txt"};

		for(int i=0; i<contentfiles.length;i++){
			try{
				o365HomeAction.downloadItemFileByName(getWebDriver(),  contentfiles[i]);

				o365HomeAction.refresh(getWebDriver(), 5);
			}
			catch(Exception e) {

				Reporter.log("error in uploading  the file "+ciqfiles[i], true);
			}
		}

		//Delete old policies
		Thread.sleep(1500000);
		policy.deletePolicy(GatewayTestConstants.CIQ_DOWNLOAD_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData));
		policy.deletePolicy(GatewayTestConstants.CIQ_DOWNLOAD_POLICY_NAME+"DictionaryProfile", suiteData, backend.getHeaders(suiteData));
		policy.deletePolicy(GatewayTestConstants.CIQ_DOWNLOAD_POLICY_NAME+"DivorcePolicy", suiteData, backend.getHeaders(suiteData));
		policy.deletePolicy(GatewayTestConstants.CIQ_DOWNLOAD_POLICY_NAME+"GamblingPolicy", suiteData, backend.getHeaders(suiteData));
		policy.deletePolicy(GatewayTestConstants.CIQ_DOWNLOAD_POLICY_NAME+"DrugsPolicy", suiteData, backend.getHeaders(suiteData));	
		policy.deletePolicy(GatewayTestConstants.CIQ_DOWNLOAD_POLICY_NAME+"USLincencPlate", suiteData, backend.getHeaders(suiteData));
		policy.deletePolicy(GatewayTestConstants.CIQ_DOWNLOAD_POLICY_NAME+"HDFCPolicy", suiteData, backend.getHeaders(suiteData));
				
		Reporter.log("Log verification successful for method:"+Thread.currentThread().getStackTrace()[1].getMethodName(), true);
	}

	@Priority(3)
	@Test(groups ={"500mbCIQ"}) 
	public void o365Upload500mbCIQFile() throws Exception {
	//	o365HomeAction.deleteAllFiles(getWebDriver());
		//create new policies
		String path="D:\\largerfiles\\";
		policy.createCIQPolicyBlock(GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+"500mbProfile", suiteData, backend.getHeaders(suiteData),"CIQ_FE_GW_DontDelete","upload");
		// upload risk files
		String [] ciqfiles={"525mb_pii_risk.txt"};
		for(int i=0; i<ciqfiles.length;i++){
			try{
				o365HomeAction.createFolder(getWebDriver(), "500");
				o365HomeAction.deleteAllFiles(getWebDriver());
				o365HomeAction.goToFolder(getWebDriver(), "500");
			//	o365HomeAction.uploadItemFile(getWebDriver(),  System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+
			//			"resources"+File.separator+"ciq"+File.separator + ciqfiles[i]);
				o365HomeAction.uploadItemFile(getWebDriver(),  path + ciqfiles[i]);

				try {
					boolean value = clickOkInPopup();
					Assert.assertTrue(value, "Blocker Popup Not Found");
				} catch (Exception e) {
					Logger.info("Error " + e.getMessage());
				}
				Thread.sleep(5000);
				o365HomeAction.refresh(getWebDriver(), 5);
				
			}

			catch(Exception e) {

				Reporter.log("error in uploading  the file "+ciqfiles[i], true);
			}

		}
		
		policy.deletePolicy(GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+"500mbProfile", suiteData, backend.getHeaders(suiteData));
		Reporter.log("Verifying pii upload activity event with policy", true);
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "File 525mb_pii_risk.txt upload has risk(s) - PII");
		expectedDataMap.put(GatewayTestConstants.RISK, "PII, ContentIQ Violations");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "critical");
		expectedDataMap.put("content_checks", "CIQ_FE_GW_DontDelete");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log(" upload actvity event verification successfull", true);
		

	}
	@Priority(4)
	@Test(groups ={"500mbCIQ"}) //,dependsOnMethods = { "o365_Test_01_ValidateLogingActivityEvent" }
	public void o365Download500mbCIQfile() throws Exception {
		//create new policies
		policy.createCIQPolicy(GatewayTestConstants.CIQ_DOWNLOAD_POLICY_NAME+"500mbProfile", suiteData, backend.getHeaders(suiteData),"CIQ_FE_GW_DontDelete","download");
		// download files
		String [] ciqfiles={"525mb_pii_risk.txt"};

		for(int i=0; i<ciqfiles.length;i++){
			try{
				o365HomeAction.goToFolder(getWebDriver(), "500");
				o365HomeAction.downloadItemFileByName(getWebDriver(), ciqfiles[i]);
				try {
					boolean value = clickOkInPopup();
					Assert.assertTrue(value, "Blocker Popup Not Found");
				} catch (Exception e) {
					Logger.info("Error " + e.getMessage());
				}
				o365HomeAction.refresh(getWebDriver(), 5);
			}
			catch(Exception e) {

				Reporter.log("error in uploading  the file "+ciqfiles[i], true);
			}
		}
		policy.deletePolicy(GatewayTestConstants.CIQ_DOWNLOAD_POLICY_NAME+"500mbProfile", suiteData, backend.getHeaders(suiteData));
		Reporter.log("Verifying pii upload activity event with policy", true);
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "File 525mb_pii_risk.txt download has risk(s) - PII");
		expectedDataMap.put(GatewayTestConstants.RISK, "PII, ContentIQ Violations");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "critical");
		expectedDataMap.put("content_checks", "CIQ_FE_GW_DontDelete");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("pii download actvity event verification successfull", true);

	}

	@Priority(18)
	@Test(groups ={"SANITY", "ENC_DEC_CIQ", "EXTERNAL"}) 
	public void o365ValidateEncryptedUploadCIQActivity() throws Exception {
		Logger.info("==================================================================================");
		Logger.info(" Verifying encrypt upload activity event");
		Logger.info("==================================================================================");
		fromTime=backend.getCurrentTime();
		String encryptFileForUpload="Test_enc.txt";
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENC_DEC_CIQ_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData));
		policy.activateEncryptionDecryptionCIQPolicy(GatewayTestConstants.ENC_DEC_CIQ_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData),"CIQ_FE_GW_DontDelete","upload");
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.loadOnedriveApp(getWebDriver());
		//o365HomeAction.deleteFile(getWebDriver(), GatewayTestConstants.O365_DELETE_ENCRYPTED_FILE);
		try
		{
		o365HomeAction.deleteFile(getWebDriver(), encryptFileForUpload+".eef");
		o365HomeAction.deleteFile(getWebDriver(), encryptFileForUpload);
		}
		catch(Exception e)
		{
			Logger.info("error file not found");
		}
			o365HomeAction.uploadItemFile(getWebDriver(),  System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+
				"resources"+File.separator+"ciq"+File.separator + encryptFileForUpload);
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		Thread.sleep(150000);
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENC_DEC_CIQ_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData));		/*Log Fields to check*/
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		//	setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "File test_enc.txt encrypted on upload for user "+suiteData.getSaasAppUsername()+".");
		//File "test_enc.txt" encrypted on upload for user gwadmin@securletautoo365featle.com
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "File Encryption" );
		expectedDataMap.put(GatewayTestConstants.POLICY_TYPE, GatewayTestConstants.FILE_TRANSFER_POLICY);
		//	expectedDataMap.put(GatewayTestConstants.FILE_SIZE, "455");
		expectedDataMap.put(GatewayTestConstants.POLICY_ACTION, GatewayTestConstants.POLICY_ACTION_ALERT);
		expectedDataMap.put(GatewayTestConstants.ACTION_TAKEN, GatewayTestConstants.ACTION_TAKEN_ENCRYPT );
		expectedDataMap.put(GatewayTestConstants.CRYPTO_KEY_VERSION, GatewayTestConstants.CRYPTO_KEY_VERSION_USED);
		expectedDataMap.put(GatewayTestConstants.OBJECT_NAME, "test_enc.txt");
		expectedDataMap.put(GatewayTestConstants.CRYPTO_KEY_SERVER, GatewayTestConstants.CRYPTO_KEY_SERVER_USED);
		expectedDataMap.put(GatewayTestConstants.CRYPTO_KEY_NAME, GatewayTestConstants.CRYPTO_KEY_NAME_USED);
		expectedDataMap.remove(GatewayTestConstants.USER_NAME);
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Expected log: "+expectedDataMap.get(GatewayTestConstants.MESSAGE)+" does not exist");
		// check for the ciq logs
		/*Log Fields to check*/
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "File test_enc.txt upload has risk(s) - PII, ContentIQ Violations");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Content Inspection");
		expectedDataMap.put(GatewayTestConstants.RISK, "PII, ContentIQ Violations");
	//	expectedDataMap.put(GatewayTestConstants.SEVERITY, "critical");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		
		Logger.info("==================================================================================");
		Logger.info(" Encrypt upload actvity event verification successful");
		Logger.info("==================================================================================");
	}
	@Priority(18)
	@Test(groups ={"SANITY", "ENC_DEC_CIQ", "EXTERNAL"}) 
	public void o365ValidateEncryptedUploadCIQ2mbEnd() throws Exception {
		Logger.info("==================================================================================");
		Logger.info(" Verifying encrypt upload activity event");
		Logger.info("==================================================================================");
		fromTime=backend.getCurrentTime();
		String encryptFileForUpload="pii2mb_end.txt";
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENC_DEC_CIQ_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData));
		policy.activateEncryptionDecryptionCIQPolicy(GatewayTestConstants.ENC_DEC_CIQ_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData),"CIQ_FE_GW_DontDelete","upload");
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.loadOnedriveApp(getWebDriver());
		//o365HomeAction.deleteFile(getWebDriver(), GatewayTestConstants.O365_DELETE_ENCRYPTED_FILE);
		try
		{
		o365HomeAction.deleteFile(getWebDriver(), encryptFileForUpload+".eef");
		o365HomeAction.deleteFile(getWebDriver(), encryptFileForUpload);
		}
		catch(Exception e)
		{
			Logger.info("error file not found");
		}
			o365HomeAction.uploadItemFile(getWebDriver(),  System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+
				"resources"+File.separator+"ciq"+File.separator + encryptFileForUpload);
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
//		o365HomeAction.loadOnedriveApp(getWebDriver());
		//	o365HomeAction.deleteFile(getWebDriver(), GatewayTestConstants.O365_UPLOAD_ENCRYPT_FILE);
		/*Log Fields to check*/
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		//	setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "File "+encryptFileForUpload+" encrypted on upload for user "+suiteData.getSaasAppUsername()+".");
		//File "test_enc.txt" encrypted on upload for user gwadmin@securletautoo365featle.com
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "File Encryption" );
		expectedDataMap.put(GatewayTestConstants.POLICY_TYPE, GatewayTestConstants.FILE_TRANSFER_POLICY);
		//	expectedDataMap.put(GatewayTestConstants.FILE_SIZE, "455");
		expectedDataMap.put(GatewayTestConstants.POLICY_ACTION, GatewayTestConstants.POLICY_ACTION_ALERT);
		expectedDataMap.put(GatewayTestConstants.ACTION_TAKEN, GatewayTestConstants.ACTION_TAKEN_ENCRYPT );
		expectedDataMap.put(GatewayTestConstants.CRYPTO_KEY_VERSION, GatewayTestConstants.CRYPTO_KEY_VERSION_USED);
		expectedDataMap.put(GatewayTestConstants.OBJECT_NAME, encryptFileForUpload);
		expectedDataMap.put(GatewayTestConstants.CRYPTO_KEY_SERVER, GatewayTestConstants.CRYPTO_KEY_SERVER_USED);
		expectedDataMap.put(GatewayTestConstants.CRYPTO_KEY_NAME, GatewayTestConstants.CRYPTO_KEY_NAME_USED);
		expectedDataMap.remove(GatewayTestConstants.USER_NAME);
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Expected log: "+expectedDataMap.get(GatewayTestConstants.MESSAGE)+" does not exist");
		// check for the ciq logs
		/*Log Fields to check*/
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "File "+encryptFileForUpload+" upload has risk(s) - PII, ContentIQ Violations");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Content Inspection");
		expectedDataMap.put(GatewayTestConstants.RISK, "PII, ContentIQ Violations");
	//	expectedDataMap.put(GatewayTestConstants.SEVERITY, "critical");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		
		Logger.info("==================================================================================");
		Logger.info(" Encrypt upload actvity event verification successful");
		Logger.info("==================================================================================");
	}
	
	@Priority(17)
	@Test(groups ={"SANITY", "ENC_DEC_CIQ", "EXTERNAL"}) 
	public void o365ValidateEncryptedUploadCIQ2mbStart() throws Exception {
		Logger.info("==================================================================================");
		Logger.info(" Verifying encrypt upload activity event");
		Logger.info("==================================================================================");
		fromTime=backend.getCurrentTime();
		String encryptFileForUpload="pii2mb_start.txt";
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENC_DEC_CIQ_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData));
		policy.activateEncryptionDecryptionCIQPolicy(GatewayTestConstants.ENC_DEC_CIQ_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData),"CIQ_FE_GW_DontDelete","upload");
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.loadOnedriveApp(getWebDriver());
		//o365HomeAction.deleteFile(getWebDriver(), GatewayTestConstants.O365_DELETE_ENCRYPTED_FILE);
		try
		{
		o365HomeAction.deleteFile(getWebDriver(), encryptFileForUpload+".eef");
		o365HomeAction.deleteFile(getWebDriver(), encryptFileForUpload);
		}
		catch(Exception e)
		{
			Logger.info("error file not found");
		}
			o365HomeAction.uploadItemFile(getWebDriver(),  System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+
				"resources"+File.separator+"ciq"+File.separator + encryptFileForUpload);
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
//		o365HomeAction.loadOnedriveApp(getWebDriver());
		//	o365HomeAction.deleteFile(getWebDriver(), GatewayTestConstants.O365_UPLOAD_ENCRYPT_FILE);
		/*Log Fields to check*/
		Thread.sleep(30000);
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENC_DEC_CIQ_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData));
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		//	setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "File "+encryptFileForUpload+" encrypted on upload for user "+suiteData.getSaasAppUsername()+".");
		//File "test_enc.txt" encrypted on upload for user gwadmin@securletautoo365featle.com
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "File Encryption" );
		expectedDataMap.put(GatewayTestConstants.POLICY_TYPE, GatewayTestConstants.FILE_TRANSFER_POLICY);
		//	expectedDataMap.put(GatewayTestConstants.FILE_SIZE, "455");
		expectedDataMap.put(GatewayTestConstants.POLICY_ACTION, GatewayTestConstants.POLICY_ACTION_ALERT);
		expectedDataMap.put(GatewayTestConstants.ACTION_TAKEN, GatewayTestConstants.ACTION_TAKEN_ENCRYPT );
		expectedDataMap.put(GatewayTestConstants.CRYPTO_KEY_VERSION, GatewayTestConstants.CRYPTO_KEY_VERSION_USED);
		expectedDataMap.put(GatewayTestConstants.OBJECT_NAME, encryptFileForUpload);
		expectedDataMap.put(GatewayTestConstants.CRYPTO_KEY_SERVER, GatewayTestConstants.CRYPTO_KEY_SERVER_USED);
		expectedDataMap.put(GatewayTestConstants.CRYPTO_KEY_NAME, GatewayTestConstants.CRYPTO_KEY_NAME_USED);
		expectedDataMap.remove(GatewayTestConstants.USER_NAME);
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Expected log: "+expectedDataMap.get(GatewayTestConstants.MESSAGE)+" does not exist");
		// check for the ciq logs
		/*Log Fields to check*/
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "File "+encryptFileForUpload+" upload has risk(s) - PII, ContentIQ Violations");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Content Inspection");
		expectedDataMap.put(GatewayTestConstants.RISK, "PII, ContentIQ Violations");
	//	expectedDataMap.put(GatewayTestConstants.SEVERITY, "critical");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		
		Logger.info("==================================================================================");
		Logger.info(" Encrypt upload actvity event verification successful");
		Logger.info("==================================================================================");
	}
	
	@Priority(18)
	@Test(groups ={"SANITY", "ENC_DEC_CIQ", "EXTERNAL"}) 
	public void o365ValidateEncryptedUploadCIQ2mbMiddle() throws Exception {
		Logger.info("==================================================================================");
		Logger.info(" Verifying encrypt upload activity event");
		Logger.info("==================================================================================");
		fromTime=backend.getCurrentTime();
		String encryptFileForUpload="pii2mb_middle.txt";
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENC_DEC_CIQ_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData));
		policy.activateEncryptionDecryptionCIQPolicy(GatewayTestConstants.ENC_DEC_CIQ_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData),"CIQ_FE_GW_DontDelete","upload");
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.loadOnedriveApp(getWebDriver());
		//o365HomeAction.deleteFile(getWebDriver(), GatewayTestConstants.O365_DELETE_ENCRYPTED_FILE);
		try
		{
		o365HomeAction.deleteFile(getWebDriver(), encryptFileForUpload+".eef");
		o365HomeAction.deleteFile(getWebDriver(), encryptFileForUpload);
		}
		catch(Exception e)
		{
			Logger.info("error file not found");
		}
			o365HomeAction.uploadItemFile(getWebDriver(),  System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+
				"resources"+File.separator+"ciq"+File.separator + encryptFileForUpload);
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		/*Log Fields to check*/
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		//	setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "File "+encryptFileForUpload+" encrypted on upload for user "+suiteData.getSaasAppUsername()+".");
		//File "test_enc.txt" encrypted on upload for user gwadmin@securletautoo365featle.com
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "File Encryption" );
		expectedDataMap.put(GatewayTestConstants.POLICY_TYPE, GatewayTestConstants.FILE_TRANSFER_POLICY);
		//	expectedDataMap.put(GatewayTestConstants.FILE_SIZE, "455");
		expectedDataMap.put(GatewayTestConstants.POLICY_ACTION, GatewayTestConstants.POLICY_ACTION_ALERT);
		expectedDataMap.put(GatewayTestConstants.ACTION_TAKEN, GatewayTestConstants.ACTION_TAKEN_ENCRYPT );
		expectedDataMap.put(GatewayTestConstants.CRYPTO_KEY_VERSION, GatewayTestConstants.CRYPTO_KEY_VERSION_USED);
		expectedDataMap.put(GatewayTestConstants.OBJECT_NAME, encryptFileForUpload);
		expectedDataMap.put(GatewayTestConstants.CRYPTO_KEY_SERVER, GatewayTestConstants.CRYPTO_KEY_SERVER_USED);
		expectedDataMap.put(GatewayTestConstants.CRYPTO_KEY_NAME, GatewayTestConstants.CRYPTO_KEY_NAME_USED);
		expectedDataMap.remove(GatewayTestConstants.USER_NAME);
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Expected log: "+expectedDataMap.get(GatewayTestConstants.MESSAGE)+" does not exist");
		// check for the ciq logs
		/*Log Fields to check*/
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "File "+encryptFileForUpload+" upload has risk(s) - PII, ContentIQ Violations");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Content Inspection");
		expectedDataMap.put(GatewayTestConstants.RISK, "PII, ContentIQ Violations");
	//	expectedDataMap.put(GatewayTestConstants.SEVERITY, "critical");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		
		Logger.info("==================================================================================");
		Logger.info(" Encrypt upload actvity event verification successful");
		Logger.info("==================================================================================");
	}
	@Priority(20)
	@Test(groups ={"SANITY", "ENC_DEC_CIQ", "EXTERNAL"}) 
	public void o365ValidateEncryptedUploadCIQEmptyFileActivity() throws Exception {
		Logger.info("==================================================================================");
		Logger.info(" Verifying encrypt upload activity event");
		Logger.info("==================================================================================");
		fromTime=backend.getCurrentTime();
		String encryptFileForUpload="empty.txt";
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.loadOnedriveApp(getWebDriver());

		o365HomeAction.uploadItemFile(getWebDriver(),  System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+
				"resources"+File.separator+"ciq"+File.separator + encryptFileForUpload);
		o365HomeAction.deleteFile(getWebDriver(), encryptFileForUpload);
		/*Log Fields to check*/
		expectedDataMap.clear();
	//	setCommonFieldsInExpectedDataMap(expectedDataMap);
		policy.deletePolicy(GatewayTestConstants.ENC_DEC_CIQ_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData));		

		expectedDataMap.put(GatewayTestConstants.MESSAGE, "File empty.txt encrypted on upload for user "+suiteData.getSaasAppUsername()+".");
		Assert.assertFalse(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Expected log: "+expectedDataMap.get(GatewayTestConstants.MESSAGE)+" does not exist");
		// check for the ciq logs
		/*Log Fields to check*/
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "File empty.txt upload has risk(s) - PII, ContentIQ Violations");
		Assert.assertFalse(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");

		/*Log Fields to check*/
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User uploaded file named "+encryptFileForUpload);
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");

		Logger.info("==================================================================================");
		Logger.info(" Encrypt upload actvity event verification successful");
		Logger.info("==================================================================================");
	}

	
	@Priority(5)
	@Test(groups ={"CIQ"},retryAnalyzer=RetryAnalyzer.class) 
	public void o365_Test_06_ValidatePII_CIQ() throws Exception {
		Reporter.log("Verifying pii upload activity event with policy", true);
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "File pii.txt upload has risk(s) - PII");
		expectedDataMap.put(GatewayTestConstants.RISK, "PII, ContentIQ Violations");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "critical");
		expectedDataMap.put("content_checks", "CIQ_FE_GW_DontDelete");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("pii upload actvity event verification successfull", true);
	}
	
	
	@Priority(5)
	@Test(groups ={"CIQ"},retryAnalyzer=RetryAnalyzer.class) 
	public void o365_Test_06_ValidatePCI_CIQ() throws Exception {
		Reporter.log("Verifying pci.txt upload activity event with policy", true);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "File pci.txt upload has risk(s) - ContentIQ Violations, PCI");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Content Inspection");
		expectedDataMap.put(GatewayTestConstants.RISK, "ContentIQ Violations, PCI");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "critical");
		expectedDataMap.put("content_checks", "CIQ_FE_GW_DontDelete");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log(" pci.txt upload event verification successfull", true);
	}
	
	
	@Priority(7)
	@Test(groups ={"CIQ", },retryAnalyzer=RetryAnalyzer.class) 
	public void o365_Test_06_ValidateHIPPA_CIQ() throws Exception {
		Reporter.log("Verifying hipaa.txt upload  activity event with policy", true);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "File hipaa.txt upload has risk(s) - PII, ContentIQ Violations, HIPAA");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Content Inspection");
		expectedDataMap.put(GatewayTestConstants.RISK, "PII, ContentIQ Violations");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "critical");
		expectedDataMap.put("content_checks", "CIQ_FE_GW_DontDelete");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log(" hipaa.txt upload  event verification successfull", true);
	}
	
	
	@Priority(8)
	//@Test(groups ={"CIQ", "REACH_AGENT"},retryAnalyzer=RetryAnalyzer.class) //,dependsOnMethods = { "o365_Test_01_ValidateLogingActivityEvent" }
	public void o365_Test_06_ValidateVirus_CIQ() throws Exception {
		Reporter.log("Verifying decrypt download activity event with policy", true);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "File virus.zip upload has risk(s) - ContentIQ Violations, Virus / Malwar");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("Decrypt download actvity event verification successfull", true);
	}
	
	@Priority(9)
	@Test(groups ={"CIQ"},retryAnalyzer=RetryAnalyzer.class)
	public void o365_Test_06_ValidateVBA_MACRO_CIQ() throws Exception {
		Reporter.log("Verifying decrypt download activity event with policy", true);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "File vba_macro.xls upload has risk(s) - ContentIQ Violations, VBA Macros");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log(" download actvity event verification successfull", true);
	}
	
	@Priority(10)
	//@Test(groups ={"CIQ", "REACH_AGENT"},retryAnalyzer=RetryAnalyzer.class) //,dependsOnMethods = { "o365_Test_01_ValidateLogingActivityEvent" }
	public void o365_Test_06_ValidateSOURCE_CODE_CIQ() throws Exception {
		Reporter.log("Verifying decrypt download activity event with policy", true);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "File source_code.xls upload has risk(s) - Source Code");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("Decrypt download actvity event verification successfull", true);
	}
		
	
	public String getSaasAppUserName(){
		return suiteData.getSaasAppUsername().replaceAll("@", "_");
	}
	

	
	// REgression validation******
	
	@Priority(5)
	@Test(groups ={"REGRESSION","UPLOAD","REACH_AGENT"},retryAnalyzer=RetryAnalyzer.class) //,dependsOnMethods = { "o365_Test_01_ValidateLogingActivityEvent" }
	public void o365ValidatePCIUploadRisk() throws Exception {
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
	public void o365ValidatePCIDownloadRisk() throws Exception {
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
	public void o365ValidateHIPPAUploadRisk() throws Exception {
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
	public void o365ValidateHIPPADownloadRisk() throws Exception {
		Reporter.log("hipaa.txt download activity event ", true);
		expectedDataMap.clear();
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
	public void o365ValidatePIIUploadRisk() throws Exception {
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
	@Test(groups ={"REGRESSION","DOWNLOAD","REACH_AGENT"}, retryAnalyzer=RetryAnalyzer.class) //,dependsOnMethods = { "o365_Test_01_ValidateLogingActivityEvent" }
	public void o365ValidatePIIDownloadRisk() throws Exception {
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
//	@Test(groups ={"REGRESSION","UPLOAD"},retryAnalyzer=RetryAnalyzer.class) //,dependsOnMethods = { "o365_Test_01_ValidateLogingActivityEvent" }
	public void o365ValidatePIIRtfUploadRisk() throws Exception {
		Logger.info("==================================================================================");
		Logger.info("CIQ Log Verification started");
		Logger.info("==================================================================================");
		
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
//	@Test(groups ={"REGRESSION","DOWNLOAD"}, retryAnalyzer=RetryAnalyzer.class) //,dependsOnMethods = { "o365_Test_01_ValidateLogingActivityEvent" }
	public void o365ValidatePIIRtfDownloadRisk() throws Exception {
		Logger.info("==================================================================================");
		Logger.info("CIQ Log Verification started");
		Logger.info("==================================================================================");
		
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
	@Test(groups ={"REGRESSION","DOWNLOAD","REACH_AGENT"}, retryAnalyzer=RetryAnalyzer.class) //,dependsOnMethods = { "o365_Test_01_ValidateLogingActivityEvent" }
	public void o365ValidateSourceCodeDownloadCI() throws Exception {
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
	@Test(groups ={"REGRESSION","UPLOAD","REACH_AGENT"},retryAnalyzer=RetryAnalyzer.class) //,dependsOnMethods = { "o365_Test_01_ValidateLogingActivityEvent" }
	public void o365ValidateVBA_MACROUploadRisk() throws Exception {
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
	@Test(groups ={"REGRESSION","UPLOAD","REACH_AGENT"},retryAnalyzer=RetryAnalyzer.class) //,dependsOnMethods = { "o365_Test_01_ValidateLogingActivityEvent" }
	public void o365ValidateFERPAUploadRisk() throws Exception {
		Reporter.log("Verifying CIQ Upload activity event ", true);
		expectedDataMap.clear();
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
		expectedDataMap.clear();
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
		expectedDataMap.clear();
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
		expectedDataMap.clear();
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
		expectedDataMap.clear();
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
		expectedDataMap.clear();
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
	@Test(groups ={"REGRESSION","DOWNLOAD","REACH_AGENT"}, retryAnalyzer=RetryAnalyzer.class) 
	public void o365ValidateGamblingDownloadCI() throws Exception {
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
	public void o365ValidateCustomTermDivorceUpload() throws Exception {
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
	@Test(groups ={"REGRESSION","DOWNLOAD","REACH_AGENT"}, retryAnalyzer=RetryAnalyzer.class) 
	public void o365ValidateCustomTermDivorceDownload() throws Exception {
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
	public void o365ValidateCustomTermSSNUpload() throws Exception {
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
	@Test(groups ={"REGRESSION","DOWNLOAD","REACH_AGENT"}, retryAnalyzer=RetryAnalyzer.class) 
	public void o365ValidateCustomTermSSNDownload() throws Exception {
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
//	@Test(groups ={"REGRESSION","UPLOAD","REACH_AGENT"},retryAnalyzer=RetryAnalyzer.class) //,dependsOnMethods = { "o365_Test_01_ValidateLogingActivityEvent" }
	public void o365ValidateCustomTermEncryptionUpload() throws Exception {
		Reporter.log("Verifying CIQ Upload encrypt.bint ", true);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "File encryption.bin upload has risk(s) - ContentIQ Violations");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Content Inspection");
		expectedDataMap.put(GatewayTestConstants.RISK, "ContentIQ Violations");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "critical");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("Verifying CIQ Upload activity event  successfull", true);
	}
	
	@Priority(9)
//	@Test(groups ={"REGRESSION","DOWNLOAD"},dependsOnMethods = {"o365DownloadCIQfiles"},retryAnalyzer=RetryAnalyzer.class) //,dependsOnMethods = { "o365_Test_01_ValidateLogingActivityEvent" }
	public void o365ValidateCustomTermEncryptionDownload() throws Exception {
		Reporter.log("Verifying CIQ download encrypt.bin ", true);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "File encryption.bin download has risk(s) - ContentIQ Violations");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Content Inspection");
		expectedDataMap.put(GatewayTestConstants.RISK, "ContentIQ Violations");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "critical");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("Verifying CIQ download activity event  successfull", true);
	}
	
	@Priority(9)
	//@Test(groups ={"REGRESSION","UPLOAD"},retryAnalyzer=RetryAnalyzer.class) //,dependsOnMethods = { "o365_Test_01_ValidateLogingActivityEvent" }
	public void o365ValidateVirusUpload() throws Exception {
		Reporter.log("Verifying CIQ Upload virus.zip ", true);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "File virus.zip upload has risk(s) - ContentIQ Violations, Virus / Malware");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Content Inspection");
		expectedDataMap.put(GatewayTestConstants.RISK, "ContentIQ Violations, Virus / Malware");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "critical");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("Verifying CIQ Upload activity event  successfull", true);
	}
	
	@Priority(9)
	//@Test(groups ={"REGRESSION","DOWNLOAD"},dependsOnMethods = {"downloadCIQFiles"},retryAnalyzer=RetryAnalyzer.class) //,dependsOnMethods = { "o365_Test_01_ValidateLogingActivityEvent" }
	public void o365ValidateVirusDownload() throws Exception {
		Reporter.log("Verifying CIQ download virus.zip ", true);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "File virus.zip download has risk(s) - ContentIQ Violations, Virus / Malware");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Content Inspection");
		expectedDataMap.put(GatewayTestConstants.RISK, "ContentIQ Violations, Virus / Malware");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "critical");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("Verifying CIQ download activity event  successfull", true);
	}
	
	@Priority(9)
	@Test(groups ={"REGRESSION","UPLOAD","REACH_AGENT"}, dataProvider = "ContentInspectionUpload",retryAnalyzer=RetryAnalyzer.class) 
	public void o365ValidateUploadCI(String file,String Message,String Profile) throws Exception {
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
			//{ "health.txt", "File health.txt upload has risk(s) - ContentIQ Violations","GW_CIQ_DictionaryProfile"},
//			{ "image.jpeg", "File image.jpeg upload has risk(s) - ContentIQ Violations","GW_CIQ_DictionaryProfile"},
//			{ "legal.html", "File legal.html upload has risk(s) - ContentIQ Violations","GW_CIQ_DictionaryProfile"},
//			{ "video.mp4", "File video.mp4 upload has risk(s) - ContentIQ Violations","GW_CIQ_DictionaryProfile"},
			{ "us_license_plate_number.txt", "File us_license_plate_number.txt upload has risk(s) - ContentIQ Violations","GW_CIQ_UssnLiscencePlate"},
			{ "hdfc.txt", "File hdfc.txt upload has risk(s) - ContentIQ Violations","GW_CIQ_HDFCProfile"},
			
		};
	}
	
	@Priority(9)
	@Test(groups ={"REGRESSION","DOWNLOAD","REACH_AGENT"}, dataProvider = "ContentInspectionDownload", retryAnalyzer=RetryAnalyzer.class) //,dependsOnMethods = { "o365_Test_01_ValidateLogingActivityEvent" }
	public void o365CIValidationDownload(String file,String Message,String Profile) throws Exception {
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
			//{ "health.txt", "File health.txt download has risk(s) - ContentIQ Violations","GW_CIQ_DictionaryProfile"},
//			{ "image.jpeg", "File image.jpeg download has risk(s) - ContentIQ Violations","GW_CIQ_DictionaryProfile"},
//			{ "legal.html", "File legal.html download has risk(s) - ContentIQ Violations","GW_CIQ_DictionaryProfile"},
//			{ "video.mp4", "File video.mp4 download has risk(s) - ContentIQ Violations","GW_CIQ_DictionaryProfile"},
			{ "us_license_plate_number.txt", "File us_license_plate_number.txt download has risk(s) - ContentIQ Violations","GW_CIQ_UssnLiscencePlate"},
			{ "hdfc.txt", "File hdfc.txt download has risk(s) - ContentIQ Violations","GW_CIQ_HDFCProfile"},
			
			
		};
	}
	
	@Priority(15)
	@Test(groups ={"SANITY","REACH_AGENT", "EXTERNAL"},retryAnalyzer=RetryAnalyzer.class) //,dependsOnMethods = { "o365_Test_01_ValidateLogingActivityEvent" }
	public void o365_Test_04_ValidateDecryptedDownload() throws Exception {
		String fileDownloaded="readme.pdf";
		Reporter.log("==================================================================================",true);
		Reporter.log(" Verifying decrypt download activity event with policy", true);
		Reporter.log("==================================================================================",true);
		fromTime=backend.getCurrentTime();
		deleteFileInDownloadFolder(GatewayTestConstants.O365_DOWNLOAD_ENCRYPTED_FILE);
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getGWHeaders(suiteData));
		policy.createEnableEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getGWHeaders(suiteData));
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.loadOnedriveApp(getWebDriver());
		o365HomeAction.downloadItemFileByName(getWebDriver(), GatewayTestConstants.O365_DOWNLOAD_ENCRYPTED_FILE);
		
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getGWHeaders(suiteData));expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "File Decryption" );
		expectedDataMap.put(GatewayTestConstants.ACTION_TAKEN, "decrypt");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "File readme.pdf.eef decrypted on download for user "+suiteData.getSaasAppUsername()+".");
		expectedDataMap.put(GatewayTestConstants.POLICY_TYPE, "FileTransfer");
		expectedDataMap.put(GatewayTestConstants.OBJECT_NAME, GatewayTestConstants.O365_DOWNLOAD_ENCRYPTED_FILE );
		expectedDataMap.put(GatewayTestConstants.SCOPE, GatewayTestConstants.O365_DOWNLOAD_ENCRYPTED_FILE);
		expectedDataMap.put(GatewayTestConstants.FILE_SIZE, "58");
		expectedDataMap.put(GatewayTestConstants.POLICY_ACTION, GatewayTestConstants.POLICY_ACTION_ALERT);
		expectedDataMap.put(GatewayTestConstants.CRYPTO_KEY_SERVER, GatewayTestConstants.CRYPTO_KEY_SERVER_USED);
		expectedDataMap.put(GatewayTestConstants.CRYPTO_KEY_NAME, GatewayTestConstants.CRYPTO_KEY_NAME_USED);
		expectedDataMap.put(GatewayTestConstants.CRYPTO_KEY_VERSION, GatewayTestConstants.CRYPTO_KEY_VERSION_USED);
		expectedDataMap.remove(GatewayTestConstants.COUNTRY);
		expectedDataMap.remove(GatewayTestConstants.USER_NAME);
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Expected log: "+expectedDataMap.get(GatewayTestConstants.MESSAGE)+" does not exist");
		Reporter.log("Log get appear on investigate", true);
		Reporter.log("Checking if the file get decrypted on download and saved to download folder", true);
		softAssert.assertTrue(isFileDownloadSuccess(fileDownloaded), 
				"File " +fileDownloaded+ " not found in download folder" +" but found "+GatewayTestConstants.O365_DOWNLOAD_ENCRYPTED_FILE);
		Assert.assertTrue(isFileDownloadSuccess(fileDownloaded), "Expected File " +fileDownloaded+ " not found in download folder" );
		Reporter.log("Checking if the file get downloaded after decryption", true);
		Reporter.log("==================================================================================",true);
		Reporter.log(" Decrypt download actvity event verification successful", true);
		Reporter.log("==================================================================================",true);
	}
	
	@Priority(18)
	@Test(groups ={"SANITY", "REACH_AGENT", "EXTERNAL"}) 
	public void o365_Test_06_ValidateEncryptedUploadActivity() throws Exception {
		Reporter.log("==================================================================================",true);
		Reporter.log(" Verifying encrypt upload activity event", true);
		Reporter.log("==================================================================================",true);
		fromTime=backend.getCurrentTime();
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData));
		policy.createEnableEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData));
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.loadOnedriveApp(getWebDriver());
		//o365HomeAction.deleteFile(getWebDriver(), GatewayTestConstants.O365_DELETE_ENCRYPTED_FILE);
		o365HomeAction.deleteFile(getWebDriver(), GatewayTestConstants.O365_UPLOAD_ENCRYPT_FILE);
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.loadOnedriveApp(getWebDriver());
		o365HomeAction.uploadItemFile(getWebDriver(),  System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+
				"resources"+File.separator+"meta"+File.separator + GatewayTestConstants.O365_UPLOAD_ENCRYPT_FILE);
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.loadOnedriveApp(getWebDriver());
		o365HomeAction.deleteFile(getWebDriver(), GatewayTestConstants.O365_UPLOAD_ENCRYPT_FILE);
		/*Log Fields to check*/
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "File readme1.pdf encrypted on upload for user "+suiteData.getSaasAppUsername()+".");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "File Encryption" );
		expectedDataMap.put(GatewayTestConstants.POLICY_TYPE, GatewayTestConstants.FILE_TRANSFER_POLICY);
		expectedDataMap.put(GatewayTestConstants.FILE_SIZE, "57");
		expectedDataMap.put(GatewayTestConstants.POLICY_ACTION, GatewayTestConstants.POLICY_ACTION_ALERT);
		expectedDataMap.put(GatewayTestConstants.ACTION_TAKEN, GatewayTestConstants.ACTION_TAKEN_ENCRYPT );
		expectedDataMap.put(GatewayTestConstants.CRYPTO_KEY_VERSION, GatewayTestConstants.CRYPTO_KEY_VERSION_USED);
		expectedDataMap.put(GatewayTestConstants.OBJECT_NAME, "readme1.pdf");
		expectedDataMap.put(GatewayTestConstants.CRYPTO_KEY_SERVER, GatewayTestConstants.CRYPTO_KEY_SERVER_USED);
		expectedDataMap.put(GatewayTestConstants.CRYPTO_KEY_NAME, GatewayTestConstants.CRYPTO_KEY_NAME_USED);
		expectedDataMap.remove(GatewayTestConstants.USER_NAME);
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Expected log: "+expectedDataMap.get(GatewayTestConstants.MESSAGE)+" does not exist");
		Reporter.log("==================================================================================",true);
		Reporter.log(" Encrypt upload actvity event verification successful", true);
		Reporter.log("==================================================================================",true);
	}
	@Priority(1)
	@AfterTest
	public void Cleanup() throws Exception {
		fromTime=backend.getCurrentTime();
		policy.deletePolicy(GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+"HDFCPolicy", suiteData, backend.getHeaders(suiteData));
		policy.deletePolicy(GatewayTestConstants.CIQ_DOWNLOAD_POLICY_NAME+"HDFCPolicy", suiteData, backend.getHeaders(suiteData));
		Reporter.log("Finished cleanup activities on cloudSoc", true);
	}
}