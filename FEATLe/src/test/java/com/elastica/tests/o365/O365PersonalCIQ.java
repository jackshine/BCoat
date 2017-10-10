package com.elastica.tests.o365;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.elastica.common.GWCommonTest;
import com.elastica.gateway.GatewayTestConstants;
import com.elastica.listeners.Priority;
import com.elastica.logger.Logger;

public class O365PersonalCIQ extends GWCommonTest{
	Map <String, Object> expectedDataMap = new HashMap<String, Object>();
	Map<String, String>policyDataMap= new HashMap<String, String>(); 
	String fromTime=backend.getCurrentTime();
	CiqUtils utils=new CiqUtils();
	@BeforeMethod(alwaysRun= true)
	public void initialSetup(){
		//fromTime=backend.getCurrentTime();
		try {
			policy.createCIQPolicy(GatewayTestConstants.CIQ_DOWNLOAD_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData),"CIQ_FE_GW_DontDelete","download");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		expectedDataMap.clear();
	}

	@Priority(1)
	@Test(groups ={"EXTERNAL"})
	public void loginToCloudSocAppAndSetupSSO() throws Exception {
		//fromTime=backend.getCurrentTime();
		System.out.println(TimeZone.getDefault().getID());
		Logger.info("==================================================================================");
		Logger.info(" Logging to CloudSOC app");
		Logger.info("==================================================================================");
		login.loginCloudSocPortal(getWebDriver(), suiteData);
		Logger.info("==================================================================================");
		Logger.info(" Loging to cloudSoc done");
		Logger.info("==================================================================================");
	}
	
	@Priority(2)
	@Test(groups ={"EXTERNAL", "REACH"})  
	public void o365_Test_01_ExternalLoginEvent() throws Exception {
		Logger.info("==================================================================================");
		Logger.info(" Login into One Drive");
		Logger.info("==================================================================================");
		o365Login.onedriveLogin(getWebDriver(), suiteData);
		o365HomeAction.selectView(getWebDriver());
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.remove(GatewayTestConstants.FACILITY);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Login");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User logged in"); 
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Session");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Expected log: "+expectedDataMap.get(GatewayTestConstants.MESSAGE)+" does not exist on investigate");
		Logger.info("==================================================================================");
		Logger.info(" Login event verification successful");
		Logger.info("==================================================================================");
	}
	 
	@Priority(6)
	@Test(groups ={"EXTERNAL",  "REACH"}) 
	public void o365_Test_06_ValidateEncryptedUploadActivity() throws Exception {
		Logger.info("==================================================================================");
		Logger.info(" Verifying encrypt upload activity event");
		Logger.info("==================================================================================");
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
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "File readme1.pdf encrypted on upload for user "+ suiteData.getSaasAppUsername()+".");
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
		Logger.info("==================================================================================");
		Logger.info(" Encrypt upload actvity event verification successful");
		Logger.info("==================================================================================");
	}
	
	@Priority(7)
	@Test(groups ={"EXTERNAL", "REACH"}) 
	public void o365_Test_Share_Via_Email() throws Exception {
		Logger.info("==================================================================================");
		Logger.info(" Verifying Share folder via Email");
		Logger.info("==================================================================================");
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		//o365HomeAction.loadOnedriveAppPersonal(getWebDriver());
		o365HomeAction.deleteFile(getWebDriver(), GatewayTestConstants.O365_CREATE_FOLDER);
		o365HomeAction.createFolder(getWebDriver(),  GatewayTestConstants.O365_CREATE_FOLDER);
		o365HomeAction.shareItemViaEmailPersonal(getWebDriver(), GatewayTestConstants.O365_CREATE_FOLDER, suiteData.getTestUsername());
		o365HomeAction.deleteFile(getWebDriver(), GatewayTestConstants.O365_CREATE_FOLDER);
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Share");
		expectedDataMap.put(GatewayTestConstants.MESSAGE,  "User shared an item named " + GatewayTestConstants.O365_CREATE_FOLDER + " with email " + suiteData.getTestUsername()); 
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Folder");
		expectedDataMap.put(GatewayTestConstants.OBJECT_NAME, "NewFolder");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("==================================================================================");
		Logger.info(" Share event via email validation done");
		Logger.info("==================================================================================");
	}
	
	@Priority(8)
	@Test(groups ={"EXTERNAL", "REACH"}) 
	public void o365_Test_Share_Link() throws Exception {
		Logger.info("==================================================================================");
		Logger.info(" Verifying share viw link event");
		Logger.info("==================================================================================");
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.shareItemByLinkPerson(getWebDriver(), GatewayTestConstants.GDRIVE_ONE_DRIVE_FILE_ATTACH_PERSONAL);
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Share");
		expectedDataMap.put(GatewayTestConstants.MESSAGE,  "User generated link for an item named " + GatewayTestConstants.GDRIVE_ONE_DRIVE_FILE_ATTACH_PERSONAL); 
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File/Folder");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.OBJECT_NAME, GatewayTestConstants.GDRIVE_ONE_DRIVE_FILE_ATTACH_PERSONAL);
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("==================================================================================");
		Logger.info(" Share via link verification done");
		Logger.info("==================================================================================");
		
	}
	
	@Priority(9)
	@Test(groups ={"SANITY", "REACH", "EXTERNAL"}) 
	public void o365ValidatePIIUpload() throws Exception {
		Logger.info("Verifying CIQ for the file "+GatewayTestConstants.CIQ_FILE);
		//o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME+getTestUsername(), suiteData, backend.getHeaders(suiteData));
		policy.deletePolicy(GatewayTestConstants.CIQ_POLICY_NAME+getTestUsername(), suiteData, backend.getHeaders(suiteData));
		policy.createCIQPolicy(GatewayTestConstants.CIQ_POLICY_NAME+getTestUsername(), suiteData, backend.getHeaders(suiteData));
		
		o365HomeAction.deleteFile(getWebDriver(), GatewayTestConstants.CIQ_FILE);
		o365HomeAction.uploadItemFile(getWebDriver(),  System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+
				"resources"+File.separator+"ciq"+File.separator + GatewayTestConstants.CIQ_FILE );
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "File "+GatewayTestConstants.CIQ_FILE+" upload has risk(s) - PII");
		policy.deletePolicy(GatewayTestConstants.CIQ_POLICY_NAME+getTestUsername(), suiteData, backend.getHeaders(suiteData));
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("CIQ tests execution done");
	}
	
	@Priority(9)
	@Test(groups ={"SANITY", "REACH", "EXTERNAL"}) 
	public void o365ValidatePIIDownload() throws Exception {
		Logger.info("Verifying CIQ for the file "+GatewayTestConstants.CIQ_FILE);
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME+getTestUsername(), suiteData, backend.getHeaders(suiteData));
		policy.deletePolicy(GatewayTestConstants.CIQ_POLICY_NAME+getTestUsername(), suiteData, backend.getHeaders(suiteData));
		policy.createCIQPolicy(GatewayTestConstants.CIQ_DOWNLOAD_POLICY_NAME+getTestUsername(), suiteData, backend.getHeaders(suiteData));
		
		o365HomeAction.downloadItemFile(getWebDriver(), GatewayTestConstants.CIQ_FILE);
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "File "+GatewayTestConstants.CIQ_FILE+" download has risk(s) - PII");
		policy.deletePolicy(GatewayTestConstants.CIQ_DOWNLOAD_POLICY_NAME+getTestUsername(), suiteData, backend.getHeaders(suiteData));
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("CIQ tests execution done");
	}
	
	@Priority(3)
	@Test(groups ={"MAIL_CIQ", "REACH"}) 
	public void o365SendEmailAttachedWithPiiFile() throws Exception {

		Logger.info("Email Attached via One Drive");
		utils.createCIQProfile( CIQConstants.ZERO,suiteData, "CIQ_FE_GW_DontDelete","CIQ_FE_GW_DontDelete",CIQConstants.SOURCE_DCI,CIQConstants.DEFAUTRISKVALUE);
		utils.createCIQProfile( CIQConstants.ZERO,suiteData, "GW_CIQ_DictionaryProfile","GW_CIQ_DictionaryProfile",CIQConstants.SOURCE_RISK,CIQConstants.RISKVALUE);

		fromTime=backend.getCurrentTime();
		policy.createCIQPolicy(GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData));
		policy.createCIQPolicy(GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+"DictionaryProfile", suiteData, backend.getHeaders(suiteData),"GW_CIQ_DictionaryProfile","upload");

		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.loadEmailApp(getWebDriver());
		// upload risk files
		String [] ciqfiles={"pii.txt"};
		for(int i=0; i<ciqfiles.length;i++){
			try{

				String filepath=System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+
						"resources"+File.separator+"ciq"+File.separator + ciqfiles[i];
				
				o365HomeAction.sendEmailWithAttach(getWebDriver(), ciqfiles[i]);
				Thread.sleep(5000);
				o365HomeAction.refresh(getWebDriver(), 5);
			}

			catch(Exception e) {

				Reporter.log("error in uploading  the file "+ciqfiles[i], true);
			}
		}
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "File "+GatewayTestConstants.CIQ_FILE+" upload has risk(s) - PII");
		policy.deletePolicy(GatewayTestConstants.CIQ_POLICY_NAME+getTestUsername(), suiteData, backend.getHeaders(suiteData));
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("Email email validation tests execution done");
	}
	
	
	@Priority(6)
	@Test(groups ={"MAIL_CIQ", "REACH"})  //,dependsOnMethods = { "loginToCloudSocAppAndSetupSSO" }
	public void o365EmailRecieveCIQfile() throws Exception {
		Logger.info("Email Attached via One Drive");
		fromTime=backend.getCurrentTime();
	
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.loadEmailApp(getWebDriver());
		// upload risk files
				String [] ciqfiles={"pii.txt"};
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
				policy.deletePolicy(GatewayTestConstants.CIQ_DOWNLOAD_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData));			
				expectedDataMap.clear();
				expectedDataMap.put(GatewayTestConstants.MESSAGE, "File "+GatewayTestConstants.CIQ_FILE+" download has risk(s) - PII");
				policy.deletePolicy(GatewayTestConstants.CIQ_POLICY_NAME+getTestUsername(), suiteData, backend.getHeaders(suiteData));
				Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
				Logger.info("Email email validation tests execution done");
				
	}

	
	
		
	public String getSaasAppUserName(){
		return suiteData.getSaasAppUsername().replaceAll("@", "_");
	}
	
	public String getTestUsername(){
		return suiteData.getTestUsername().replaceAll("@", "_");
	}
	
	@BeforeClass(groups ={"SANITY", "REACH", "EXTERNAL"})
	public void doBeforeClass() throws Exception {
		Logger.info("Delete Policy Before Test ");
		utils.createCIQProfile( CIQConstants.ZERO,suiteData, "CIQ_FE_GW_DontDelete","CIQ_FE_GW_DontDelete",CIQConstants.SOURCE_DCI,CIQConstants.DEFAUTRISKVALUE);
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME+getSaasAppUserName(), 
				suiteData, backend.getHeaders(suiteData));
	}
	
	@AfterClass(groups ={"SANITY", "REACH", "EXTERNAL"})
	public void doAfterClass() throws Exception {
		Logger.info("Delete Policy After Test ");
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME+getSaasAppUserName(), 
				suiteData, backend.getHeaders(suiteData));
	}
	
	


}