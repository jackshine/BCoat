package com.elastica.tests.o365;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.elastica.common.GWCommonTest;
import com.elastica.common.SuiteData;
import com.elastica.gateway.GatewayTestConstants;
import com.elastica.listeners.Priority;
import com.elastica.logger.Logger;
import com.elastica.restClient.Client;

public class O365CIQExceptionTest extends GWCommonTest {
	
	Map <String, Object> expectedDataMap = new HashMap<String, Object>();
	Map<String, String>policyDataMap= new HashMap<String, String>(); 
	String fromTime=null;
	SoftAssert softAssert = new SoftAssert();
	Client restClient= new Client();
	CiqUtils utils=new CiqUtils();
	
	public String getSaasAppUserName(){
		return suiteData.getSaasAppUsername().replaceAll("@", "_");
	}
	@BeforeClass(alwaysRun= true)
	public void clearDataMap() throws Exception{
		
		expectedDataMap.clear();	
		
		List<String> keywords= new ArrayList<String>();
		keywords.add("Divorce");
			Logger.info("Creating CIQ profile with only content is completed");	
		policy.deletePolicy(GatewayTestConstants.ENC_DEC_CIQ_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData));
		//createCIQProfileWithContentTypes( restClient,suiteData,ciqProfileName);
	}
	
	@Priority(1)
	@Test(groups ={"REGRESSION","DOWNLOAD","UPLOAD","CIQ_EXCEPTION"})
	public void loginToCloudSocAppAndSetupSSO() throws Exception {
 		fromTime=backend.getCurrentTime();
		Reporter.log("Started performing activities on saas app", true);
		login.loginCloudSocPortal(getWebDriver(), suiteData);
		Reporter.log("Finished login activities on cloudSoc", true);
	}
	
	@Priority(2)
	@Test(groups ={"REGRESSION","DOWNLOAD","UPLOAD","CIQ_EXCEPTION","REACH_AGENT"})  //,dependsOnMethods = { "loginToCloudSocAppAndSetupSSO" }
	public void o365_Test_01_ValidateLoginActivityEvent() throws Exception {
		Reporter.log("Verifying the login event", true);
		o365Login.login(getWebDriver(), suiteData);
		//expectedDataMap.clear();
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.loadOnedriveApp(getWebDriver());
		Reporter.log("Login event verification successfull", true);
	}
	
	@Priority(3)
	@Test(groups ={"SANITY", "CIQ_EXCEPTION", "UPLOAD","REACH_AGENT"}) 
	public void o365ValidateCIQRiskExceptionPciPiiUpload() throws Exception {
		Logger.info("==================================================================================");
		Logger.info(" Verifying encrypt upload activity event");
		Logger.info("==================================================================================");
		fromTime=backend.getCurrentTime();
		//String encryptFileForUpload="pii.txt";
		String [] ciqfiles={"pci.txt", "pii.txt" };
		// delete old polices
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENC_DEC_CIQ_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData));
		policy.deletePolicy(GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+"DictionaryProfile", suiteData, backend.getHeaders(suiteData));
		policy.deletePolicy(GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+"DivorcePolicy", suiteData, backend.getHeaders(suiteData));
		policy.deletePolicy(GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+"GamblingPolicy", suiteData, backend.getHeaders(suiteData));
		policy.deletePolicy(GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+"DrugsPolicy", suiteData, backend.getHeaders(suiteData));	
		policy.deletePolicy(GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+"USLincencPlate", suiteData, backend.getHeaders(suiteData));
		policy.deletePolicy(GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+"HDFCPolicy", suiteData, backend.getHeaders(suiteData));
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData));
		
		// create ciq risk profile
		utils.createCIQProfile( CIQConstants.ZERO,suiteData, "GW_CIQ_PIIProfile","GW_CIQ_PIIProfile",CIQConstants.SOURCE_DCI,"pii");
		utils.createCIQProfile( CIQConstants.ZERO,suiteData, "GW_CIQ_PCIProfile","GW_CIQ_PCIProfile",CIQConstants.SOURCE_DCI,"pci");
		//created ciq excepting policy
		Logger.info("==================================================================================");
		Logger.info(" create CIQ exception policy");
		Logger.info("==================================================================================");

		policy.createCIQPolicyWhitelist(GatewayTestConstants.CIQ_EXCEPTION_NAME+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData),"GW_CIQ_PCIProfile","upload","GW_CIQ_PIIProfile");
		Logger.info("==================================================================================");
		Logger.info(" create CIQ exception policy-- completed");
		try
		{
			for(int i=0;i<ciqfiles.length;i++)
			{
				o365HomeAction.deleteFile(getWebDriver(), ciqfiles[i]);
			}
		
		}
		catch(Exception e)
		{
			Logger.info("error file not found");
		}
		for(int i=0;i<ciqfiles.length;i++)
		{
		o365HomeAction.uploadItemFile(getWebDriver(),  System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+
				"resources"+File.separator+"ciq"+File.separator + ciqfiles[i]);
		o365HomeAction.refresh(getWebDriver(), 5);
		}
		// delete policy after use
		Thread.sleep(15000);
		policy.deletePolicy(GatewayTestConstants.CIQ_EXCEPTION_NAME+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData));
		/*Log Fields to check*/
		expectedDataMap.clear();
		//setCommonFieldsInExpectedDataMap(expectedDataMap);
		//	pci.txt file is include and pii.txt file is exclueded
		/*
		 * [ALERT] File glba.txt upload violated policy - GW_CIQ_UPLOAD_POLICY_gwadmin_securletautoo365featle.com
		 */
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "[ALERT] File "+ciqfiles[0]+" upload violated policy - "+GatewayTestConstants.CIQ_EXCEPTION_NAME+getSaasAppUserName());
		//File "test_enc.txt" encrypted on upload for user gwadmin@securletautoo365featle.com
		boolean status1=backend.validateLog(client, suiteData, fromTime, expectedDataMap);
		Assert.assertTrue(status1,"Expected log: "+expectedDataMap.get(GatewayTestConstants.MESSAGE)+" does not exist");
		Map <String, String> expectedDataMap1 = new HashMap<String, String>();
		expectedDataMap1.clear();
		expectedDataMap1.put(GatewayTestConstants.MESSAGE, "[ALERT] File "+ciqfiles[1]+" upload violated policy - "+GatewayTestConstants.CIQ_EXCEPTION_NAME+getSaasAppUserName());
		//delete used policy
		// check for the ciq logs absence
		/*Log Fields to check*/
		//boolean status=backend.validateLog(client, suiteData, fromTime, expectedDataMap1);
		backend.assertAndValidateLogNotPresent(client, suiteData,fromTime, expectedDataMap1);
	//	expectedDataMap.put(GatewayTestConstants.SEVERITY, "critical");
//		Assert.assertFalse(status,"Policy log have triggered ");
		
		Logger.info("==================================================================================");
		Logger.info(" Encrypt upload actvity event verification successful");
		Logger.info("==================================================================================");
	}
	
	@Priority(4)
	//@Test(groups ={"SANITY", "CIQ_EXCEPTION", "EXTERNAL","REACH_AGENT"}) 
	public void o365ValidateCIQRiskExceptionPciPiiDownload() throws Exception {
		Logger.info("==================================================================================");
		Logger.info(" Verifying encrypt upload activity event");
		Logger.info("==================================================================================");
		fromTime=backend.getCurrentTime();
		String encryptFileForUpload="pii.txt";
		String [] ciqfiles={"pci.txt",  "pii.txt" };
		// upload file 
		for(int i=0;i<ciqfiles.length;i++)
		{
			o365HomeAction.uploadItemFile(getWebDriver(),  System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+
					"resources"+File.separator+"ciq"+File.separator + ciqfiles[i]);
		}
		// delete old polices
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENC_DEC_CIQ_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData));
		policy.deletePolicy(GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+"DictionaryProfile", suiteData, backend.getHeaders(suiteData));
		policy.deletePolicy(GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+"DivorcePolicy", suiteData, backend.getHeaders(suiteData));
		policy.deletePolicy(GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+"GamblingPolicy", suiteData, backend.getHeaders(suiteData));
		policy.deletePolicy(GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+"DrugsPolicy", suiteData, backend.getHeaders(suiteData));	
		policy.deletePolicy(GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+"USLincencPlate", suiteData, backend.getHeaders(suiteData));
		policy.deletePolicy(GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+"HDFCPolicy", suiteData, backend.getHeaders(suiteData));
		policy.deletePolicy(GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+"HDFCPolicy", suiteData, backend.getHeaders(suiteData));
		policy.deletePolicy(GatewayTestConstants.CIQ_EXCEPTION_NAME+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData));

		// upload file to be donwloaded
		for(int i=0;i<ciqfiles.length;i++)
		{
			o365HomeAction.uploadItemFile(getWebDriver(),  System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+
					"resources"+File.separator+"ciq"+File.separator + ciqfiles[i]);
		}

		// create ciq risk profile
		utils.createCIQProfile( CIQConstants.ZERO,suiteData, "GW_CIQ_PIIProfile","GW_CIQ_PIIProfile",CIQConstants.SOURCE_DCI,"pii");
		utils.createCIQProfile( CIQConstants.ZERO,suiteData, "GW_CIQ_PCIProfile","GW_CIQ_PCIProfile",CIQConstants.SOURCE_DCI,"pci");
		//created ciq excepting policy
		Logger.info("==================================================================================");
		Logger.info(" create CIQ exception policy");
		Logger.info("==================================================================================");

		policy.createCIQPolicyWhitelist(GatewayTestConstants.CIQ_EXCEPTION_NAME+"download"+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData),"GW_CIQ_PCIProfile","download","GW_CIQ_PIIProfile");
		Logger.info("==================================================================================");
		Logger.info(" create CIQ exception policy-- completed");

		//	o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		//	o365HomeAction.loadOnedriveApp(getWebDriver());
		//o365HomeAction.deleteFile(getWebDriver(), GatewayTestConstants.O365_DELETE_ENCRYPTED_FILE);
		for(int i=0;i<ciqfiles.length;i++)
		{
			o365HomeAction.downloadItemFileByName(getWebDriver(), ciqfiles[i]);
		}
		// delete policy after user
		Thread.sleep(15000);
		policy.deletePolicy(GatewayTestConstants.CIQ_EXCEPTION_NAME+"download"+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData));
		//	o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		/*Log Fields to check*/
		expectedDataMap.clear();
		//setCommonFieldsInExpectedDataMap(expectedDataMap);
		//	pci.txt file is include and pii.txt file is exclueded
		/*
		 * [ALERT] File glba.txt upload violated policy - GW_CIQ_UPLOAD_POLICY_gwadmin_securletautoo365featle.com
		 */
		Logger.info("[ALERT] File "+ciqfiles[0]+" download violated policy - "+GatewayTestConstants.CIQ_EXCEPTION_NAME+getSaasAppUserName());
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "[ALERT] File "+ciqfiles[0]+" download violated policy - "+GatewayTestConstants.CIQ_EXCEPTION_NAME+"download"+getSaasAppUserName());
		//File "test_enc.txt" encrypted on upload for user gwadmin@securletautoo365featle.com
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Expected log: "+expectedDataMap.get(GatewayTestConstants.MESSAGE)+" does not exist");
		// check for the ciq logs absence
		/*Log Fields to check*/
		Map <String, String> expectedDataMap1 = new HashMap<String, String>();
		expectedDataMap1.clear();
		expectedDataMap1.put(GatewayTestConstants.MESSAGE, "[ALERT] File "+ciqfiles[1]+" download violated policy - "+GatewayTestConstants.CIQ_EXCEPTION_NAME+"download"+getSaasAppUserName());
		//delete used policy
		// check for the ciq logs absence
		/*Log Fields to check*/
		//boolean status=backend.validateLog(client, suiteData, fromTime, expectedDataMap1);
		backend.assertAndValidateLogNotPresent(client, suiteData,fromTime, expectedDataMap1);
		Logger.info("==================================================================================");
		Logger.info(" Encrypt upload actvity event verification successful");
		Logger.info("==================================================================================");
	}
	@Priority(5)
	@Test(groups ={"SANITY", "CIQ_EXCEPTION", "UPLOAD","REACH_AGENT"}) 
	public void o365ValidateCIQRiskExceptionComboUpload() throws Exception {
		Logger.info("==================================================================================");
		Logger.info(" 1.Test data setup ");
		Logger.info("  Inculded risk pii,pci");
		Logger.info(" Exculded risk ferpa,glba,vba_macross");
		Logger.info("==================================================================================");
		fromTime=backend.getCurrentTime();
		
		String [] ciqfiles={"pci.txt",  "pii.txt", "glba.txt", "vba_macro.xls"};
		// delete old polices
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENC_DEC_CIQ_POLICY_NAME+"combo"+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData));
		// create ciq risk profile
		utils.createCIQProfile( CIQConstants.ZERO,suiteData, "GW_CIQ_P1Profile","GW_CIQ_P1Profile",CIQConstants.SOURCE_DCI,"hipaa\",\"pci\",\"pii");
		utils.createCIQProfile( CIQConstants.ZERO,suiteData, "GW_CIQ_P2Profile","GW_CIQ_P2Profile",CIQConstants.SOURCE_DCI,"ferpa\",\"glba\",\"vba_macros");
		//created ciq excepting policy
		Logger.info("==================================================================================");
		Logger.info(" create CIQ exception policy");
		Logger.info("==================================================================================");

		policy.createCIQPolicyWhitelist(GatewayTestConstants.CIQ_EXCEPTION_NAME+"PciPii-glba_upload"+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData),"GW_CIQ_P1Profile","upload","GW_CIQ_P2Profile");
		Logger.info("==================================================================================");
		Logger.info(" create CIQ exception policy-- completed");

		try
		{
			for(int i=0;i<ciqfiles.length;i++)
			{
				o365HomeAction.deleteFile(getWebDriver(), ciqfiles[i]);
			}
		
		}
		catch(Exception e)
		{
			Logger.info("error file not found");
		}
		for(int i=0;i<ciqfiles.length;i++)
		{
		o365HomeAction.uploadItemFile(getWebDriver(),  System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+
				"resources"+File.separator+"ciq"+File.separator + ciqfiles[i]);
		o365HomeAction.refresh(getWebDriver(), 5);
		Thread.sleep(15000);
		}
	
		//delete policy after use
		policy.deletePolicy(GatewayTestConstants.CIQ_EXCEPTION_NAME+"PciPii-glba_upload"+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData));
		/*Log Fields to check*/
		expectedDataMap.clear();		
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "[ALERT] File "+ciqfiles[0]+" upload violated policy - "+GatewayTestConstants.CIQ_EXCEPTION_NAME+"PciPii-glba_upload"+getSaasAppUserName());		
		//File "pci.txt" 
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Expected log: "+expectedDataMap.get(GatewayTestConstants.MESSAGE)+" does not exist");
		/*Log Fields to check*/
		expectedDataMap.clear();		
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "[ALERT] File "+ciqfiles[1]+" upload violated policy - "+GatewayTestConstants.CIQ_EXCEPTION_NAME+"PciPii-glba_upload"+getSaasAppUserName());	
		//File "pii.txt" 
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Expected log: "+expectedDataMap.get(GatewayTestConstants.MESSAGE)+" does not exist");
		/*Log Fields to check*/
		// check for the ciq logs absence
		/*Log Fields to check*/
			
		Map <String, String> expectedDataMap1 = new HashMap<String, String>();
		expectedDataMap1.clear();
		expectedDataMap1.put(GatewayTestConstants.MESSAGE, "[ALERT] File "+ciqfiles[3]+" upload violated policy - "+GatewayTestConstants.CIQ_EXCEPTION_NAME+"PciPii-glba_upload"+getSaasAppUserName());
		backend.assertAndValidateLogNotPresent(client, suiteData,fromTime, expectedDataMap1);	
		
		//Map <String, String> expectedDataMap1 = new HashMap<String, String>();
		expectedDataMap1.clear();
		expectedDataMap1.put(GatewayTestConstants.MESSAGE, "[ALERT] File "+ciqfiles[2]+" upload violated policy - "+GatewayTestConstants.CIQ_EXCEPTION_NAME+"PciPii-glba_upload"+getSaasAppUserName());
		backend.assertAndValidateLogNotPresent(client, suiteData,fromTime, expectedDataMap1);		

		Logger.info("==================================================================================");
		Logger.info(" exception combo  upload actvity event verification successful");
		Logger.info("==================================================================================");
	}

	@Priority(5)
	//@Test(groups ={"SANITY", "CIQ_EXCEPTION", "UPLOAD","REACH_AGENT"}) 
	public void o365ValidateCIQRiskExceptionComboDownload() throws Exception {
		Logger.info("==================================================================================");
		Logger.info(" 1.Test data setup ");
		Logger.info("  Inculded risk pii,pci,hippa");
		Logger.info(" Exculded risk ferpa,glba,vba_macross");
		Logger.info("==================================================================================");
		fromTime=backend.getCurrentTime();
		
		String [] ciqfiles={"pci.txt",  "pii.txt", "hipaa.txt","glba.txt", "vba_macro.xls","FERPA_BaileyDoxed.txt"};
		// upload file to be donwloaded
		Logger.info(" upload file to be donwloaded ");
		for(int i=0;i<ciqfiles.length;i++)
		{
		o365HomeAction.uploadItemFile(getWebDriver(),  System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+
				"resources"+File.separator+"ciq"+File.separator + ciqfiles[i]);
		}
		Logger.info(" upload file completed");
		// delete old polices
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENC_DEC_CIQ_POLICY_NAME+"combo"+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData));
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENC_DEC_CIQ_POLICY_NAME+"PciPii-glba_upload"+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData));
		// create ciq risk profile
		utils.createCIQProfile( CIQConstants.ZERO,suiteData, "GW_CIQ_P1Profile","GW_CIQ_P1Profile",CIQConstants.SOURCE_DCI,"hipaa\",\"pci\",\"pii");
		utils.createCIQProfile( CIQConstants.ZERO,suiteData, "GW_CIQ_P2Profile","GW_CIQ_P2Profile",CIQConstants.SOURCE_DCI,"ferpa\",\"glba\",\"vba_macros");
		//created ciq excepting policy
		Logger.info("==================================================================================");
		Logger.info(" create CIQ exception policy");
		Logger.info("==================================================================================");
		policy.createCIQPolicyWhitelist(GatewayTestConstants.CIQ_EXCEPTION_NAME+"PciPii-glba_download"+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData),"GW_CIQ_P1Profile","download","GW_CIQ_P2Profile");
		Logger.info("==================================================================================");
		Logger.info(" create CIQ exception policy-- completed");
		try
		{
			for(int i=0;i<ciqfiles.length;i++)
			{
				o365HomeAction.downloadItemFileByName(getWebDriver(), ciqfiles[i]);
				Thread.sleep(15000);
			}
		
		}
		catch(Exception e)
		{
			Logger.info("error file not found");
		}
		//delete policy after use
		policy.deletePolicy(GatewayTestConstants.CIQ_EXCEPTION_NAME+"PciPii-glba_download"+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData));
		/*Log Fields to check*/
		expectedDataMap.clear();	
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "[ALERT] File "+ciqfiles[0]+" upload violated policy - "+GatewayTestConstants.CIQ_EXCEPTION_NAME+"PciPii-glba_download"+getSaasAppUserName());		
		//File "pci.txt" 
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Expected log: "+expectedDataMap.get(GatewayTestConstants.MESSAGE)+" does not exist");
		/*Log Fields to check*/
		expectedDataMap.clear();		
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "[ALERT] File "+ciqfiles[1]+" upload violated policy - "+GatewayTestConstants.CIQ_EXCEPTION_NAME+"PciPii-glba_download"+getSaasAppUserName());		
		//File "pii.txt" 
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Expected log: "+expectedDataMap.get(GatewayTestConstants.MESSAGE)+" does not exist");
		/*Log Fields to check*/
		expectedDataMap.clear();		
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "[ALERT] File "+ciqfiles[2]+" upload violated policy - "+GatewayTestConstants.CIQ_EXCEPTION_NAME+"PciPii-glba_download"+getSaasAppUserName());		
		//File "hippa.txt" 
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Expected log: "+expectedDataMap.get(GatewayTestConstants.MESSAGE)+" does not exist");		
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "[ALERT] File "+ciqfiles[3]+" upload violated policy - "+GatewayTestConstants.CIQ_EXCEPTION_NAME+"PciPii-glba_download"+getSaasAppUserName());
		// check for the ciq logs absence
		/*Log Fields to check*/
		Map <String, String> expectedDataMap1 = new HashMap<String, String>();
		expectedDataMap1.clear();
		expectedDataMap1.put(GatewayTestConstants.MESSAGE, "[ALERT] File "+ciqfiles[3]+" upload violated policy - "+GatewayTestConstants.CIQ_EXCEPTION_NAME+"PciPii-glba_download"+getSaasAppUserName());
		backend.assertAndValidateLogNotPresent(client, suiteData,fromTime, expectedDataMap1);		
		
		Logger.info("==================================================================================");
		Logger.info(" exception combo  upload actvity event verification successful");
		Logger.info("==================================================================================");
	}
	@Priority(5)
	//@Test(groups ={"SANITY", "CIQ_EXCEPTION", "UPLOAD","REACH_AGENT"}) 
	public void o365ValidateCIQRiskExceptionRiskContentUpload() throws Exception {
		Logger.info("==================================================================================");
		Logger.info(" 1.Test data setup ");
		Logger.info("  Inculded risk pii,pci,hippa");
		Logger.info(" Exculded risk ferpa,glba,vba_macross");
		Logger.info("==================================================================================");
		fromTime=backend.getCurrentTime();
		
		String [] ciqfiles={ "pii.txt","pci.txt","hippa.txt"};
		String [] riskrifle={ "Java.txt","health.txt","legal.txt",};
		// delete file to be uploaded 
		try
		{
			for(int i=0;i<ciqfiles.length;i++)
			{
				o365HomeAction.deleteFile(getWebDriver(), ciqfiles[i]);
			}
			for(int i=0;i<riskrifle.length;i++)
			{
				o365HomeAction.deleteFile(getWebDriver(), riskrifle[i]);
			}
		
		}
		catch(Exception e)
		{
			Logger.info("error file not found");
		}
		
		// delete old polices
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENC_DEC_CIQ_POLICY_NAME+"combo"+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData));
	
		// create ciq risk profile
		utils.createCIQProfile( CIQConstants.ZERO,suiteData, "GW_CIQ_PIIProfile","GW_CIQ_PIIProfile",CIQConstants.SOURCE_DCI,"pii");
		utils.createCIQProfile( CIQConstants.ZERO,suiteData, "GW_CIQ_RiskProfile","GW_CIQ_RiskProfile",CIQConstants.SOURCE_RISK,CIQConstants.RISKVALUE);
		//created ciq excepting policy
		Logger.info("==================================================================================");
		Logger.info(" create CIQ exception policy");
		Logger.info("==================================================================================");

		policy.createCIQPolicyWhitelist(GatewayTestConstants.CIQ_EXCEPTION_NAME+"riskci"+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData),"GW_CIQ_PIIProfile","upload","GW_CIQ_RiskProfile");
		Logger.info("==================================================================================");
		Logger.info(" create CIQ exception policy-- completed");

		try
		{
			for(int i=0;i<ciqfiles.length;i++)
			{
				o365HomeAction.deleteFile(getWebDriver(), ciqfiles[i]);
			}
			for(int i=0;i<riskrifle.length;i++)
			{
				o365HomeAction.deleteFile(getWebDriver(), riskrifle[i]);
			}
		
		}
		catch(Exception e)
		{
			Logger.info("error file not found");
		}
		for(int i=0;i<ciqfiles.length;i++)
		{
		o365HomeAction.uploadItemFile(getWebDriver(),  System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+
				"resources"+File.separator+"ciq"+File.separator + ciqfiles[i]);
		o365HomeAction.refresh(getWebDriver(), 5);
		Thread.sleep(15000);
		}
		for(int i=0;i<riskrifle.length;i++)
		{
		o365HomeAction.uploadItemFile(getWebDriver(),  System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+
				"resources"+File.separator+"ciq"+File.separator + riskrifle[i]);
		o365HomeAction.refresh(getWebDriver(), 5);
		Thread.sleep(15000);
		}
		// delete policy after use
		policy.deletePolicy(GatewayTestConstants.CIQ_EXCEPTION_NAME+"riskci"+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData));
		/*Log Fields to check*/
		Logger.info("Start:Vadidate absence of log for file "+ciqfiles[0]);
		expectedDataMap.clear();		
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "[ALERT] File "+ciqfiles[0]+" upload violated policy - "+GatewayTestConstants.CIQ_EXCEPTION_NAME+"riskci"+getSaasAppUserName());		
		//File "pci.txt" 
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Expected log: "+expectedDataMap.get(GatewayTestConstants.MESSAGE)+" does not exist");
		
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "[ALERT] File "+ciqfiles[3]+" upload violated policy - "+GatewayTestConstants.CIQ_EXCEPTION_NAME+"riskci"+getSaasAppUserName());
		// check for the ciq logs absence
		Map <String, String> expectedDataMap1 = new HashMap<String, String>();
		expectedDataMap1.clear();
		expectedDataMap1.put(GatewayTestConstants.MESSAGE, "[ALERT] File "+ciqfiles[3]+" upload violated policy - "+GatewayTestConstants.CIQ_EXCEPTION_NAME+getSaasAppUserName());
		backend.assertAndValidateLogNotPresent(client, suiteData,fromTime, expectedDataMap1);		
		Logger.info("Start:Vadidate absence of log for file "+ciqfiles[0]);
		expectedDataMap1.clear();
		expectedDataMap1.put(GatewayTestConstants.MESSAGE, "[ALERT] File "+ciqfiles[0]+" upload violated policy - "+GatewayTestConstants.CIQ_EXCEPTION_NAME+"riskci"+getSaasAppUserName());
		backend.assertAndValidateLogNotPresent(client, suiteData,fromTime, expectedDataMap1);		
		Logger.info("Completed :Vadidate absence of log for file "+ciqfiles[0]);
		Logger.info("Start:Vadidate absence of log for file "+ciqfiles[1]);
		expectedDataMap1.clear();
		expectedDataMap1.put(GatewayTestConstants.MESSAGE, "[ALERT] File "+ciqfiles[1]+" upload violated policy - "+GatewayTestConstants.CIQ_EXCEPTION_NAME+"riskci"+getSaasAppUserName());
		backend.assertAndValidateLogNotPresent(client, suiteData,fromTime, expectedDataMap1);	
		Logger.info("Completed :Vadidate absence of log for file "+ciqfiles[1]);
		// delete user policy		
	
		Logger.info("==================================================================================");
		Logger.info(" exception combo  upload actvity event verification successful");
		Logger.info("==================================================================================");
	}

	@Priority(5)
//	@Test(groups ={"SANITY", "CIQ_EXCEPTION", "UPLOAD","REACH_AGENT"}) 
	public void o365ValidateCIQRiskExceptionRiskContentDownload() throws Exception {
		Logger.info("==================================================================================");
		Logger.info(" 1.Test data setup ");
		Logger.info("  Inculded risk pii,pci,hippa");
		Logger.info(" Exculded risk ferpa,glba,vba_macross");
		Logger.info("==================================================================================");
		fromTime=backend.getCurrentTime();

		String [] ciqfiles={ "pii.txt","pci.txt","Java.txt","health.txt","legal.txt","engineering.txt"};
		// upload file to be deleted
		for(int i=0;i<ciqfiles.length;i++)
		{
		o365HomeAction.uploadItemFile(getWebDriver(),  System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+
				"resources"+File.separator+"ciq"+File.separator + ciqfiles[i]);
		}
		// create ciq risk profile
		utils.createCIQProfile( CIQConstants.ZERO,suiteData, "GW_CIQ_PIIProfile","GW_CIQ_PIIProfile",CIQConstants.SOURCE_DCI,"pii");
		utils.createCIQProfile( CIQConstants.ZERO,suiteData, "GW_CIQ_RiskProfile","GW_CIQ_RiskProfile",CIQConstants.SOURCE_RISK,CIQConstants.RISKVALUE);
		//created ciq excepting policy
		Logger.info("==================================================================================");
		Logger.info(" create CIQ exception policy");
		Logger.info("==================================================================================");

		policy.createCIQPolicyWhitelist(GatewayTestConstants.CIQ_EXCEPTION_NAME+"riskci_download"+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData),"GW_CIQ_PIIProfile","download","GW_CIQ_RiskProfile");
		Logger.info("==================================================================================");
		Logger.info(" create CIQ exception policy-- completed");
		for(int i=0;i<ciqfiles.length;i++)
		{
			o365HomeAction.downloadItemFileByName(getWebDriver(), ciqfiles[i]);
		}
		Thread.sleep(15000);	
	//	delete policy after use
		policy.deletePolicy(GatewayTestConstants.CIQ_EXCEPTION_NAME+"riskci_download"+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData));
		/*Log Fields to check*/

		expectedDataMap.clear();		
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "[ALERT] File "+ciqfiles[0]+" upload violated policy - "+GatewayTestConstants.CIQ_EXCEPTION_NAME+"riskci_download"+getSaasAppUserName());		
		//File "pci.txt" 
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Expected log: "+expectedDataMap.get(GatewayTestConstants.MESSAGE)+" does not exist");
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "[ALERT] File "+ciqfiles[1]+" upload violated policy - "+GatewayTestConstants.CIQ_EXCEPTION_NAME+"riskci_download"+getSaasAppUserName());
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Expected log: "+expectedDataMap.get(GatewayTestConstants.MESSAGE)+" does not exist");
		// check for the ciq logs absence
		/*Log Fields to check*/
		Map <String, String> expectedDataMap1 = new HashMap<String, String>();
		Logger.info("Start:Vadidate absence of log for file "+ciqfiles[1]);
		expectedDataMap1.clear();
		expectedDataMap1.put(GatewayTestConstants.MESSAGE, "[ALERT] File "+ciqfiles[2]+" upload violated policy - "+GatewayTestConstants.CIQ_EXCEPTION_NAME+"riskci"+getSaasAppUserName());
		backend.assertAndValidateLogNotPresent(client, suiteData,fromTime, expectedDataMap1);	

		// check for the ciq logs absence
		/*Log Fields to check*/
		Logger.info("Start:Vadidate absence of log for file "+ciqfiles[1]);
		expectedDataMap1.clear();
		expectedDataMap1.put(GatewayTestConstants.MESSAGE, "[ALERT] File "+ciqfiles[3]+" upload violated policy - "+GatewayTestConstants.CIQ_EXCEPTION_NAME+"riskci"+getSaasAppUserName());
		backend.assertAndValidateLogNotPresent(client, suiteData,fromTime, expectedDataMap1);	
		
		Logger.info("==================================================================================");
		Logger.info(" exception combo  upload actvity event verification successful");
		Logger.info("==================================================================================");
	}
// depricated
	@Priority(5)
//	@Test(groups ={"SANITY", "CIQ_EXCEPTION", "UPLOAD","REACH_AGENT"}) 
	public void o365ValidateCIQRiskExceptionContentUploadRisk() throws Exception {
		Logger.info("==================================================================================");
		Logger.info(" 1.Test data setup ");
		Logger.info("  Inculded risk pii,pci,hippa");
		Logger.info(" Exculded risk ferpa,glba,vba_macross");
		Logger.info("==================================================================================");
		fromTime=backend.getCurrentTime();
		
		String [] ciqfiles={ "pii.txt","pci.txt"};
		String [] riskrifle={ "Java.txt","health.txt","legal.txt","engineering.txt"};
		// delete old polices
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENC_DEC_CIQ_POLICY_NAME+"combo"+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData));
		policy.deletePolicy(GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+"DictionaryProfile", suiteData, backend.getHeaders(suiteData));
		policy.deletePolicy(GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+"DivorcePolicy", suiteData, backend.getHeaders(suiteData));
		policy.deletePolicy(GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+"GamblingPolicy", suiteData, backend.getHeaders(suiteData));
		policy.deletePolicy(GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+"DrugsPolicy", suiteData, backend.getHeaders(suiteData));	
		policy.deletePolicy(GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+"USLincencPlate", suiteData, backend.getHeaders(suiteData));
		policy.deletePolicy(GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+"HDFCPolicy", suiteData, backend.getHeaders(suiteData));
	
		// create ciq risk profile
		utils.createCIQProfile( CIQConstants.ZERO,suiteData, "GW_CIQ_PIIProfile","GW_CIQ_PIIProfile",CIQConstants.SOURCE_DCI,"pii");
		utils.createCIQProfile( CIQConstants.ZERO,suiteData, "GW_CIQ_RiskProfile","GW_CIQ_RiskProfile",CIQConstants.SOURCE_RISK,CIQConstants.RISKVALUE);
		//created ciq excepting policy
		Logger.info("==================================================================================");
		Logger.info(" create CIQ exception policy");
		Logger.info("==================================================================================");

		policy.createCIQPolicyWhitelist(GatewayTestConstants.CIQ_EXCEPTION_NAME+"riskci_upload"+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData),"GW_CIQ_RiskProfile","upload","GW_CIQ_PIIProfile");
		Logger.info("==================================================================================");
		Logger.info(" create CIQ exception policy-- completed");

//		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
//		o365HomeAction.loadOnedriveApp(getWebDriver());
		//o365HomeAction.deleteFile(getWebDriver(), GatewayTestConstants.O365_DELETE_ENCRYPTED_FILE);
		try
		{
			for(int i=0;i<ciqfiles.length;i++)
			{
				o365HomeAction.deleteFile(getWebDriver(), ciqfiles[i]);
			}
			for(int i=0;i<riskrifle.length;i++)
			{
				o365HomeAction.deleteFile(getWebDriver(), riskrifle[i]);
			}
		
		}
		catch(Exception e)
		{
			Logger.info("error file not found");
		}
		for(int i=0;i<ciqfiles.length;i++)
		{
		o365HomeAction.uploadItemFile(getWebDriver(),  System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+
				"resources"+File.separator+"ciq"+File.separator + ciqfiles[i]);
		o365HomeAction.refresh(getWebDriver(), 5);
		}
		for(int i=0;i<riskrifle.length;i++)
		{
		o365HomeAction.uploadItemFile(getWebDriver(),  System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+
				"resources"+File.separator+"ciq"+File.separator + riskrifle[i]);
		o365HomeAction.refresh(getWebDriver(), 5);
		}
		Thread.sleep(15000);
		policy.deletePolicy(GatewayTestConstants.CIQ_EXCEPTION_NAME+"riskci_upload"+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData));
		/*Log Fields to check*/
		expectedDataMap.clear();		
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "[ALERT] File "+ciqfiles[0]+" upload violated policy - "+GatewayTestConstants.CIQ_EXCEPTION_NAME+"riskci_upload"+getSaasAppUserName());		
		//File "pci.txt" 
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Expected log: "+expectedDataMap.get(GatewayTestConstants.MESSAGE)+" does not exist");
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "[ALERT] File "+ciqfiles[1]+" upload violated policy - "+GatewayTestConstants.CIQ_EXCEPTION_NAME+"riskci_upload"+getSaasAppUserName());
		// check for the ciq logs absence
		/*Log Fields to check*/
		Map <String, String> expectedDataMap1 = new HashMap<String, String>();
		Logger.info("Start:Vadidate absence of log for file "+riskrifle[0]);
		expectedDataMap1.clear();
		expectedDataMap1.put(GatewayTestConstants.MESSAGE, "[ALERT] File "+riskrifle[0]+" upload violated policy - "+GatewayTestConstants.CIQ_EXCEPTION_NAME+"riskci_upload"+getSaasAppUserName());
		backend.assertAndValidateLogNotPresent(client, suiteData,fromTime, expectedDataMap1);	

		// check for the ciq logs absence
		/*Log Fields to check*/
		Logger.info("Start:Vadidate absence of log for file "+riskrifle[1]);
		expectedDataMap1.clear();
		expectedDataMap1.put(GatewayTestConstants.MESSAGE, "[ALERT] File "+riskrifle[1]+" upload violated policy - "+GatewayTestConstants.CIQ_EXCEPTION_NAME+"riskci_upload"+getSaasAppUserName());
		backend.assertAndValidateLogNotPresent(client, suiteData,fromTime, expectedDataMap1);	
		Logger.info("Start:Vadidate absence of log for file "+riskrifle[3]);
		expectedDataMap1.clear();
		expectedDataMap1.put(GatewayTestConstants.MESSAGE, "[ALERT] File "+riskrifle[3]+" upload violated policy - "+GatewayTestConstants.CIQ_EXCEPTION_NAME+"riskci_upload"+getSaasAppUserName());
		backend.assertAndValidateLogNotPresent(client, suiteData,fromTime, expectedDataMap1);	
		Logger.info("==================================================================================");
		Logger.info(" exception combo  upload actvity event verification successful");
		Logger.info("==================================================================================");
	}

	@Priority(5)
	//@Test(groups ={"SANITY", "CIQ_EXCEPTION", "UPLOAD","REACH_AGENT"}) 
	public void o365ValidateCIQRiskExceptionContentRiskDownload() throws Exception {
		Logger.info("==================================================================================");
		Logger.info(" 1.Test data setup ");
		Logger.info("  Inculded risk pii,pci,hippa");
		Logger.info(" Exculded risk ferpa,glba,vba_macross");
		Logger.info("==================================================================================");
		fromTime=backend.getCurrentTime();

		String [] ciqfiles={ "pii.txt","Java.txt","health.txt","legal.txt","engineering.txt"};
		// upload file to be downloaded
		for(int i=0;i<ciqfiles.length;i++)
		{
			o365HomeAction.uploadItemFile(getWebDriver(),  System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+
					"resources"+File.separator+"ciq"+File.separator + ciqfiles[i]);
		}
		// create ciq risk profile
		utils.createCIQProfile( CIQConstants.ZERO,suiteData, "GW_CIQ_PIIProfile","GW_CIQ_PIIProfile",CIQConstants.SOURCE_DCI,"pii");
		utils.createCIQProfile( CIQConstants.ZERO,suiteData, "GW_CIQ_RiskProfile","GW_CIQ_RiskProfile",CIQConstants.SOURCE_RISK,CIQConstants.RISKVALUE);
		//created ciq excepting policy
		Logger.info("==================================================================================");
		Logger.info(" create CIQ exception policy");
		Logger.info("==================================================================================");

		policy.createCIQPolicyWhitelist(GatewayTestConstants.CIQ_EXCEPTION_NAME+"riskci_download"+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData),"GW_CIQ_RiskProfile","download","GW_CIQ_PIIProfile");
		Logger.info("==================================================================================");
		Logger.info(" create CIQ exception policy-- completed");
		for(int i=0;i<ciqfiles.length;i++)
		{
			o365HomeAction.downloadItemFileByName(getWebDriver(), ciqfiles[i]);
		}
		policy.deletePolicy(GatewayTestConstants.CIQ_EXCEPTION_NAME+"riskci_download"+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData));
		/*Log Fields to check*/
		expectedDataMap.clear();		
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "[ALERT] File "+ciqfiles[0]+" upload violated policy - "+GatewayTestConstants.CIQ_EXCEPTION_NAME+getSaasAppUserName());		
		//File "pci.txt" 
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Expected log: "+expectedDataMap.get(GatewayTestConstants.MESSAGE)+" does not exist");
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "[ALERT] File "+ciqfiles[1]+" upload violated policy - "+GatewayTestConstants.CIQ_EXCEPTION_NAME+getSaasAppUserName());
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Expected log: "+expectedDataMap.get(GatewayTestConstants.MESSAGE)+" does not exist");
		// check for the ciq logs absence
		/*Log Fields to check*/
		Map <String, String> expectedDataMap1 = new HashMap<String, String>();
		Logger.info("Start:Vadidate absence of log for file "+ciqfiles[2]);
		expectedDataMap1.clear();
		expectedDataMap1.put(GatewayTestConstants.MESSAGE, "[ALERT] File "+ciqfiles[2]+" upload violated policy - "+GatewayTestConstants.CIQ_EXCEPTION_NAME+"riskci_upload"+getSaasAppUserName());
		backend.assertAndValidateLogNotPresent(client, suiteData,fromTime, expectedDataMap1);	
		Logger.info("Start:Vadidate absence of log for file "+ciqfiles[2]);
		// check for the ciq logs absence
		/*Log Fields to check*/
		Logger.info("Start:Vadidate absence of log for file "+ciqfiles[3]);
		expectedDataMap1.clear();
		expectedDataMap1.put(GatewayTestConstants.MESSAGE, "[ALERT] File "+ciqfiles[3]+" upload violated policy - "+GatewayTestConstants.CIQ_EXCEPTION_NAME+"riskci_upload"+getSaasAppUserName());
		backend.assertAndValidateLogNotPresent(client, suiteData,fromTime, expectedDataMap1);	
		Logger.info("Start:Vadidate absence of log for file "+ciqfiles[3]);
		Logger.info("==================================================================================");
		Logger.info(" exception combo  upload actvity event verification successful");
		Logger.info("==================================================================================");
	}

	@Priority(5)
	@Test(groups ={"SANITY", "CIQ_EXCEPTION", "UPLOAD","REACH_AGENT"}) 
	public void o365ValidateCIQContentExclueRiskUpload() throws Exception {
		Logger.info("==================================================================================");
		Logger.info(" 1.Test data setup ");
		Logger.info("  Inculded content profile source code,design,computing,image files");
		Logger.info(" Exculded risks ferpa,glba,vba_macross");
		Logger.info("==================================================================================");
		fromTime=backend.getCurrentTime();
		
		String [] riskrifle={ "pii.txt","pci.txt"};
		String [] ciqfiles={ "Java.txt","health.txt"};
		// delete old polices
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENC_DEC_CIQ_POLICY_NAME+"combo"+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData));
		// create ciq risk profile
	//	utils.createCIQProfile( CIQConstants.ZERO,suiteData, "GW_CIQ_PIIProfile","GW_CIQ_PIIProfile",CIQConstants.SOURCE_DCI,"pii");
		utils.createCIQProfile( CIQConstants.ZERO,suiteData, "GW_CIQ_RiskProfile","GW_CIQ_RiskProfile",CIQConstants.SOURCE_RISK,CIQConstants.RISKVALUE);
		//created ciq excepting policy
		Logger.info("==================================================================================");
		Logger.info(" create CIQ exception policy");
		Logger.info("==================================================================================");

		policy.createCIQPolicyWhitelist(GatewayTestConstants.CIQ_EXCEPTION_NAME+"CI_EXCLUDE_RISK_UPLOAD"+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData),"GW_CIQ_RiskProfile","upload","CIQ_FE_GW_DontDelete");
		Logger.info("==================================================================================");
		Logger.info(" create CIQ exception policy-- completed");
		try
		{
			for(int i=0;i<ciqfiles.length;i++)
			{
				o365HomeAction.deleteFile(getWebDriver(), ciqfiles[i]);
			}
			for(int i=0;i<riskrifle.length;i++)
			{
				o365HomeAction.deleteFile(getWebDriver(), riskrifle[i]);
			}
		
		}
		catch(Exception e)
		{
			Logger.info("error file not found");
		}
		for(int i=0;i<ciqfiles.length;i++)
		{
		o365HomeAction.uploadItemFile(getWebDriver(),  System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+
				"resources"+File.separator+"ciq"+File.separator+ciqfiles[i]);
		o365HomeAction.refresh(getWebDriver(), 5);
		}
		for(int i=0;i<riskrifle.length;i++)
		{
		o365HomeAction.uploadItemFile(getWebDriver(),  System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+
				"resources"+File.separator+"ciq"+File.separator + riskrifle[i]);
		o365HomeAction.refresh(getWebDriver(), 5);
		}
		Thread.sleep(15000);
		policy.deletePolicy(GatewayTestConstants.CIQ_EXCEPTION_NAME+"CI_EXCLUDE_RISK_UPLOAD"+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData));

		/*Log Fields to check*/
		expectedDataMap.clear();		
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "[ALERT] File "+ciqfiles[0].toLowerCase()+" upload violated policy - "+GatewayTestConstants.CIQ_EXCEPTION_NAME+"CI_EXCLUDE_RISK_UPLOAD"+getSaasAppUserName());		
		//File "pci.txt" 
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Expected log: "+expectedDataMap.get(GatewayTestConstants.MESSAGE)+" does not exist");
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "[ALERT] File "+ciqfiles[1].toLowerCase()+" upload violated policy - "+GatewayTestConstants.CIQ_EXCEPTION_NAME+"CI_EXCLUDE_RISK_UPLOAD"+getSaasAppUserName());
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Expected log: "+expectedDataMap.get(GatewayTestConstants.MESSAGE)+" does not exist");
		// check for the ciq logs absence

		Map <String, String> expectedDataMap1 = new HashMap<String, String>();
		expectedDataMap1.clear();
		expectedDataMap1.put(GatewayTestConstants.MESSAGE, "[ALERT] File "+riskrifle[0].toLowerCase()+" upload violated policy - "+GatewayTestConstants.CIQ_EXCEPTION_NAME+"CI_EXCLUDE_RISK_UPLOAD"+getSaasAppUserName());
		backend.assertAndValidateLogNotPresent(client, suiteData,fromTime, expectedDataMap1);		
		expectedDataMap1.clear();
		expectedDataMap1.put(GatewayTestConstants.MESSAGE, "[ALERT] File "+riskrifle[1].toLowerCase()+" upload violated policy - "+GatewayTestConstants.CIQ_EXCEPTION_NAME+"CI_EXCLUDE_RISK_UPLOAD"+getSaasAppUserName());
		backend.assertAndValidateLogNotPresent(client, suiteData,fromTime, expectedDataMap1);
		
		Logger.info("==================================================================================");
		Logger.info(" exception combo  upload actvity event verification successful");
		Logger.info("==================================================================================");
	}
	@Priority(5)
	//@Test(groups ={"SANITY", "CIQ_EXCEPTION", "UPLOAD","REACH_AGENT"}) 
	public void o365ValidateRiskExculedeCIUpload() throws Exception {
		Logger.info("==================================================================================");
		Logger.info(" 1.Test data setup ");
		Logger.info("  Inculded content profile source code,design,computing,image files");
		Logger.info(" Exculded risks ferpa,glba,vba_macross");
		Logger.info("==================================================================================");
		fromTime=backend.getCurrentTime();
		
		String [] riskrifle={ "pii.txt","pci.txt"};
		String [] ciqfiles={ "Java.txt","health.txt"};
		// delete old polices
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENC_DEC_CIQ_POLICY_NAME+"combo"+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData));
		// create ciq risk profile
	//	utils.createCIQProfile( CIQConstants.ZERO,suiteData, "GW_CIQ_PIIProfile","GW_CIQ_PIIProfile",CIQConstants.SOURCE_DCI,"pii");
		utils.createCIQProfile( CIQConstants.ZERO,suiteData, "GW_CIQ_RiskProfile","GW_CIQ_RiskProfile",CIQConstants.SOURCE_RISK,CIQConstants.RISKVALUE);
		//created ciq excepting policy
		Logger.info("==================================================================================");
		Logger.info(" create CIQ exception policy");
		Logger.info("==================================================================================");

		policy.createCIQPolicyWhitelist(GatewayTestConstants.CIQ_EXCEPTION_NAME+"RISK_EXCLUDE_CI_UPLOAD"+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData),"CIQ_FE_GW_DontDelete","upload","GW_CIQ_RiskProfile");
		Logger.info("==================================================================================");
		Logger.info(" create CIQ exception policy-- completed");
		try
		{
			for(int i=0;i<ciqfiles.length;i++)
			{
				o365HomeAction.deleteFile(getWebDriver(), ciqfiles[i]);
			}
			for(int i=0;i<riskrifle.length;i++)
			{
				o365HomeAction.deleteFile(getWebDriver(), riskrifle[i]);
			}
		
		}
		catch(Exception e)
		{
			Logger.info("error file not found");
		}
		for(int i=0;i<ciqfiles.length;i++)
		{
		o365HomeAction.uploadItemFile(getWebDriver(),  System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+
				"resources"+File.separator+"ciq"+File.separator+ ciqfiles[i]);
		o365HomeAction.refresh(getWebDriver(), 5);
		}
		for(int i=0;i<riskrifle.length;i++)
		{
		o365HomeAction.uploadItemFile(getWebDriver(),  System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+
				"resources"+File.separator+"ciq"+File.separator+ riskrifle[i]);
		o365HomeAction.refresh(getWebDriver(), 5);
		}
		policy.deletePolicy(GatewayTestConstants.CIQ_EXCEPTION_NAME+"RISK_EXCLUDE_CI_UPLOAD"+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData));
		/*Log Fields to check*/
		expectedDataMap.clear();		
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "[ALERT] File "+riskrifle[0].toLowerCase()+" upload violated policy - "+GatewayTestConstants.CIQ_EXCEPTION_NAME+"CI_EXCLUDE_RISK_UPLOAD"+getSaasAppUserName());		
		//File "pci.txt" 
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Expected log: "+expectedDataMap.get(GatewayTestConstants.MESSAGE)+" does not exist");
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "[ALERT] File "+riskrifle[1].toLowerCase()+" upload violated policy - "+GatewayTestConstants.CIQ_EXCEPTION_NAME+"CI_EXCLUDE_RISK_UPLOAD"+getSaasAppUserName());
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Expected log: "+expectedDataMap.get(GatewayTestConstants.MESSAGE)+" does not exist");
		// check for the ciq logs absence
		Map <String, String> expectedDataMap1 = new HashMap<String, String>();
		expectedDataMap1.clear();
		expectedDataMap1.put(GatewayTestConstants.MESSAGE, "[ALERT] File "+riskrifle[0].toLowerCase()+" upload violated policy - "+GatewayTestConstants.CIQ_EXCEPTION_NAME+"CI_EXCLUDE_RISK_UPLOAD"+getSaasAppUserName());
		backend.assertAndValidateLogNotPresent(client, suiteData,fromTime, expectedDataMap1);		
		expectedDataMap1.clear();
		expectedDataMap1.put(GatewayTestConstants.MESSAGE, "[ALERT] File "+ciqfiles[1].toLowerCase()+" upload violated policy - "+GatewayTestConstants.CIQ_EXCEPTION_NAME+"CI_EXCLUDE_RISK_UPLOAD"+getSaasAppUserName());
		backend.assertAndValidateLogNotPresent(client, suiteData,fromTime, expectedDataMap1);		

		Logger.info("==================================================================================");
		Logger.info(" exception combo  upload actvity event verification successful");
		Logger.info("==================================================================================");
	}

	
	@Priority(17)
	//@Test(groups ={"SANITY", "ENC_DEC_CIQ", "EXTERNAL","REACH_AGENT"}) 
	public void o365ValidateCIQEncryption() throws Exception {
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
			o365HomeAction.refresh(getWebDriver(), 5);
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		/*Log Fields to check*/
		Thread.sleep(150000);
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENC_DEC_CIQ_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData));
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "File "+encryptFileForUpload+" encrypted on upload for user "+suiteData.getSaasAppUsername()+".");
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


}
