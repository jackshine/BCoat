package com.elastica.tests.google;

import static org.testng.Assert.assertTrue;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.elastica.common.GWCommonTest;
import com.elastica.gateway.GWCommonUtils;
import com.elastica.gateway.GatewayTestConstants;
import com.elastica.listeners.Priority;
import com.elastica.logger.Logger;

public class GMailTests extends GWCommonTest{
	Map <String, Object> expectedDataMap = new HashMap<String, Object>();
	String fromTime=backend.getCurrentTime();
	String title;
	String uniqueId = UUID.randomUUID().toString();
	String subject=uniqueId;
	String sender="testuser3@gatewaybeatle.com";
	String reciever="testuser1@gatewaybeatle.com";

	@Priority(1)
	@Test(groups ={"SANITY"})
	public void loginToCloudSocAppAndSetupSSO() throws Exception {
		printCredentials();
		fromTime=backend.getCurrentTime();
		Logger.info("Starting performing activities on Gmail saas app");
		login.loginCloudSocPortal(getWebDriver(), suiteData);
		Logger.info("Finished performing activities on Gmail saas app");
	}
	
	@Priority(2)
	@Test(groups ={"SANITY", "REACH"})  
	public void gMail_Test_001_ValidateLogin() throws Exception {
		Logger.info("Verifying Login Event");
		printCredentials();
		expectedDataMap.clear();
		gda.login(getWebDriver(), suiteData);
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.remove(GatewayTestConstants.FACILITY);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Login");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User logged in"); 
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Session");
		//expectedDataMap.put(GatewayTestConstants.REQ_URI, GatewayTestConstants.GDRIVE_REQ_URI_LOGIN);
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("Login Event Verification Successful");
	}
	
	@Priority(3)
	@Test(groups ={"SANITY", "REACH"})  
	public void gMail_Test_002_SendMail_Test() throws Exception {
		Logger.info("Send Email");
		fromTime=backend.getCurrentTime();
		printCredentials();
		gda.sendMail(getWebDriver(), subject, suiteData.getSaasAppUsername());
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.remove(GatewayTestConstants.FACILITY);
		expectedDataMap.put(GatewayTestConstants.SHARE__WITH, "");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Send");
//		expectedDataMap.put(GatewayTestConstants.MESSAGE,"User sent an email with subject "+uniqueId); 
		expectedDataMap.put(GatewayTestConstants.MESSAGE,"User sent an email with ");
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Email");
		expectedDataMap.put(GatewayTestConstants.REQ_URI, "https://mail.google.com");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("Send Email Successful");
	}
	
	
//	@Priority(4)
//	@Test(groups ={"SANITY", "REACH"})   #42440
	public void gMail_Test_003_ReceiveMail_Test() throws Exception {
		Logger.info("Verifying Receive Email Event");
		fromTime=backend.getCurrentTime();
		printCredentials();
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Receive");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User received an email with subject");
		expectedDataMap.put(GatewayTestConstants.OBJECT_NAME, subject);
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Email");
		expectedDataMap.put(GatewayTestConstants.REFERER_URI, "");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("Receive Email Event Successful");
	}
	
	
	@Priority(4)
	@Test(groups ={"SANITY", "REACH"})  
	public void gMail_Test_003_DeleteMailTest() throws Exception {
		Logger.info("Delete Email");
		fromTime=backend.getCurrentTime();
		printCredentials();
		gda.gmailHome(getWebDriver(), suiteData);
		gda.deleteMail(getWebDriver());
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.remove(GatewayTestConstants.FACILITY);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Delete");
		expectedDataMap.put(GatewayTestConstants.MESSAGE,"User deleted email(s)"); 
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Email");
		expectedDataMap.put(GatewayTestConstants.REQ_URI, "https://mail.google.com");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("Delete Mail Successful");
	}
	
	
	//@Priority(5)
	//@Test(groups ={"SANITY", "MINI_SANITY", "UPLOAD"})  
	public void gMail_Test_004_CreateDraft_Test() throws Exception {
		Logger.info("Verifying Draft Test");
		printCredentials();
		expectedDataMap.clear();
		gda.createDraft(getWebDriver(), subject,reciever);
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Send");
//		expectedDataMap.put(GatewayTestConstants.MESSAGE,"User sent an email with subject "+uniqueId);//User sent an email with subject "+"\""+\""+subject+"to"+"\""+reciever+"\"");// "User sent an email with subject "+subject+" to "+reciever); 
		expectedDataMap.put(GatewayTestConstants.MESSAGE,"User sent an email with ");
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Email");
		expectedDataMap.put(GatewayTestConstants.RESP_CODE, "200");
		expectedDataMap.put(GatewayTestConstants.REQ_URI, "https://mail.google.com");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  sender);
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("Draft Test Successful");
	}

	@Priority(6)
	@Test(groups ={"SANITY", "REACH"})  
	public void gMail_Test_005_SendMailAttach_Via_GDrive_File_Test() throws Exception {
		Logger.info("Verifying Send Email");
		fromTime=backend.getCurrentTime();
		printCredentials();
		gda.sendMailWithAttachment(getWebDriver(), subject,reciever, GatewayTestConstants.GMAIL_ATTACH);
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.remove(GatewayTestConstants.FACILITY);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Send");
//		expectedDataMap.put(GatewayTestConstants.MESSAGE,"User sent an email with subject "+uniqueId );
		expectedDataMap.put(GatewayTestConstants.MESSAGE,"User sent an email with ");
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Email");
		expectedDataMap.put(GatewayTestConstants.REQ_URI, "https://mail.google.com");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("Send Email Successful");
	}
	

	@Priority(7)
	@Test(groups ={"SANITY", "REACH"})
	public void gMail_Test_006_SendMailAttach_Local_File_Test() throws Exception {
		Logger.info("Verifying Upload File Locally");
		fromTime=backend.getCurrentTime();
		printCredentials();
		getWebDriver().get("https://mail.google.com");
		gda.sendMailWithAttachmentFileLocal(getWebDriver(), "Subject", "testuser1@gatewaybeatle.com");
		uploadSingleFile(System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+
				"resources"+File.separator+"meta"+File.separator + GatewayTestConstants.GDRIVE_ORGINAL_FILE, suiteData);
		gda.sentEmail(getWebDriver());

		expectedDataMap.clear(); 
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.remove(GatewayTestConstants.FACILITY);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Upload");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User uploaded file " +  GatewayTestConstants.GDRIVE_ORGINAL_FILE); 
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File");
		expectedDataMap.put(GatewayTestConstants.FILE_SIZE, GWCommonUtils.getFileSize( GatewayTestConstants.GDRIVE_ORGINAL_FILE));
		expectedDataMap.put(GatewayTestConstants.OBJECT_NAME,  GatewayTestConstants.GDRIVE_ORGINAL_FILE);
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("==================================================================================");
		Logger.info("Verified Upload File Locally Successful");
		Logger.info("==================================================================================");
	}
	
	
	@Priority(8)
//	@Test(groups ={"SANITY"})  
	public void gMail_Test_006_SendMailAttachCIQ_GDrive_File_Test() throws Exception {
		Logger.info("Verify Send Email CIQ");
		fromTime=backend.getCurrentTime();
		printCredentials();
		gda.sendMailWithAttachment(getWebDriver(), subject,reciever, GatewayTestConstants.GMAIL_ATTACH);
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.remove(GatewayTestConstants.FACILITY);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Send");
//		expectedDataMap.put(GatewayTestConstants.MESSAGE,"User sent an email with subject "+uniqueId );
		expectedDataMap.put(GatewayTestConstants.MESSAGE,"User sent an email with ");
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Email");
		expectedDataMap.put(GatewayTestConstants.REQ_URI, "https://mail.google.com");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("Send Email CIQ Successfull");
	}
	
	@Priority(9)
	@Test(groups ={"SANITY", "REACH"})  
	public void gMail_Test_007_Download_File_From_Email_Test() throws Exception {
		Logger.info("Download File From Email");
		fromTime=backend.getCurrentTime();
		printCredentials();
		expectedDataMap.clear();
		gda.searchdownloadEmailFileAttached(getWebDriver(), "Normal Download New");
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.remove(GatewayTestConstants.FACILITY);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Download");
		expectedDataMap.put(GatewayTestConstants.MESSAGE,"User downloaded attachment " + GatewayTestConstants.GDRIVE_UPLOAD_ORIGINAL_FILE);
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("Download File From Email Successfull");
	}
	
	@Priority(10)
//	@Test(groups ={"SANITY"})    // #38866 - No Encryption and Decryption supported for Gmail
	public void gMail_Test_008_Download_Decrypted_File_From_Email_Test() throws Exception {
		Logger.info("Download Decrypted File");
		fromTime=backend.getCurrentTime();
		printCredentials();
		expectedDataMap.clear();
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME + suiteData.getSaasAppName(), suiteData, backend.getGWHeaders(suiteData));
		policy.createEnableEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME + suiteData.getSaasAppName(), suiteData, backend.getGWHeaders(suiteData));
		gda.searchdownloadEmailFileAttached(getWebDriver(), "Encrypted File New");
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "File Decryption");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "File " + GatewayTestConstants.GDRIVE_UPLOAD_ORIGINAL_FILE + ".eef decrypted on download for user " + suiteData.getSaasAppName());
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File");
		expectedDataMap.remove(GatewayTestConstants.COUNTRY);
		expectedDataMap.remove(GatewayTestConstants.USER_NAME);
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("==================================================================================");
		Logger.info("Download Decrypted File Successful");
		Logger.info("==================================================================================");
	}
	
	
	@Priority(11)
	@Test(groups ={"SANITY", "REACH"})  
	public void gMail_Test_Calender_Download() throws Exception {
		Logger.info("Calender Download");
		fromTime=backend.getCurrentTime();
		printCredentials();
		gda.calenderDownloadAndUpload(getWebDriver(), "Upload Download Calender",  GatewayTestConstants.GMAIL_ATTACH);
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.remove(GatewayTestConstants.FACILITY);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Download");
		expectedDataMap.put(GatewayTestConstants.MESSAGE,"User downloaded contents of file " +  GatewayTestConstants.GMAIL_ATTACH);
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("Calender Download Successful");
	}
	
	@Priority(12)
	@Test(groups ={"SANITY", "REACH"})  
	public void gMail_Test_Calender_Upload() throws Exception {
		Logger.info("Calender Upload");
		printCredentials();
		expectedDataMap.clear(); 
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.remove(GatewayTestConstants.FACILITY);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Upload");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User attached file(s) readme1.pdf from Google Drive"); 
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File");
		expectedDataMap.put(GatewayTestConstants.OBJECT_NAME, "readme1.pdf");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("Calender Upload Successful");
	}
	
	
	@Priority(13)
	@Test(groups ={"SANITY", "REACH"})  
	public void gMail_Test_008_LogoutValidationTest() throws Exception {
		Logger.info("Verifying Logout Event");
		fromTime=backend.getCurrentTime();
		printCredentials();
		gda.logout(getWebDriver());
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.remove(GatewayTestConstants.FACILITY);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Logout");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User logged out");
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Session");
		expectedDataMap.put(GatewayTestConstants.REQ_URI, GatewayTestConstants.GDRIVE_REQ_URI_LOGOUT);
		expectedDataMap.put(GatewayTestConstants.REFERER_URI, "");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("Logout Successfull");
	}
	
	
	@Priority(14)
	@Test(groups ={"SANITY", "REACH"})
	public void gMail_Test_009_Invalid_Login() throws Exception {
		Logger.info("Invalid Login");
		printCredentials();
		String password = suiteData.getSaasAppPassword();
		suiteData.setSaasAppPassword("incorrect");
		gda.reloginGmail(getWebDriver(), suiteData);
		suiteData.setSaasAppPassword(password);
		assertTrue(gda.reloginGmail(getWebDriver(), suiteData));
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.remove(GatewayTestConstants.FACILITY);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "InvalidLogin");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User Login Failed!");
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Session");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("Completed Invalid Login");
	}
	
	@BeforeClass(groups ={"SANITY", "P1_SANITY", "EXTERNAL", "REACH"})
	public void doBeforeClass() throws Exception {
		Logger.info("Delete Policy Before Test ");
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME+ suiteData.getSaasAppName(), suiteData, backend.getGWHeaders(suiteData));
	}
	
	@AfterClass(groups ={"SANITY", "P1_SANITY", "EXTERNAL", "REACH"})
	public void doAfterClass() throws Exception {
		Logger.info("Delete Policy After Test ");
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME+ suiteData.getSaasAppName(), suiteData, backend.getGWHeaders(suiteData));
	}
	
	
}