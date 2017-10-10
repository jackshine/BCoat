package com.elastica.tests.google;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.elastica.action.protect.ProtectAction;
import com.elastica.action.protect.ProtectDTO;
import com.elastica.common.GWCommonTest;
import com.elastica.gateway.GWCommonUtils;
import com.elastica.gateway.GatewayTestConstants;
import com.elastica.listeners.Priority;
import com.elastica.listeners.RetryAnalyzer;
import com.elastica.logger.Logger;
import com.elastica.tests.o365.CIQConstants;
import com.elastica.tests.o365.CiqUtils;

public class GDriveTestsFileTransferUploadPolicy extends GWCommonTest {
	Map <String, Object> expectedDataMap = new HashMap<String, Object>();
	String fromTime=backend.getCurrentTime();
	String title;
	SoftAssert softAssert = new SoftAssert();
	ProtectAction protectAction = new ProtectAction();
	ProtectDTO protectData = new ProtectDTO();
	CiqUtils utils=new CiqUtils();
	boolean blockerdisplay = false;

	@Priority(1)
	@Test(groups ={"Regression"})
	public void loginToCloudSocAppAndSetupSSO() throws Exception {
		fromTime=backend.getCurrentTime();
		printCredentials();
		Logger.info("==================================================================================");
		Logger.info("Login into CloudSoc");
		Logger.info("==================================================================================");
		login.loginCloudSocPortal(getWebDriver(), suiteData);
		Logger.info("==================================================================================");
		Logger.info("Loging into CloudSoc done");
		Logger.info("==================================================================================");
	}
	
	
	@Priority(2)
	@Test(groups ={"Regression", "REACH"})  
	public void gDrive_Test_001_ValidateLogin() throws Exception {
		fromTime=backend.getCurrentTime();
		printCredentials();
		Logger.info("==================================================================================");
		Logger.info("Verifying the login event");
		Logger.info("==================================================================================");
		expectedDataMap.clear();
		gda.login(getWebDriver(), suiteData);
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.remove(GatewayTestConstants.FACILITY);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Login");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User logged in"); 
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Session");
		//expectedDataMap.put(GatewayTestConstants.REQ_URI, GatewayTestConstants.GDRIVE_REQ_URI_LOGIN);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("==================================================================================");
		Logger.info("Verify login successful");
		Logger.info("==================================================================================");
	}
	
	@Priority(3)
	@Test(groups ={"Regression", "REACH"}) 
	public void gDrive_Test_Upload_Policy_Without_Block() throws Exception {
		Logger.info("==================================================================================");
		Logger.info(" Upload file with  policy severity high ");
		Logger.info("==================================================================================");
		printCredentials();
		String fromTime=backend.getCurrentTime();
		protectAction.createEnableFileTransferPolicyWithUploadAndDownload(client, suiteData, 
				GatewayTestConstants.GDRIVE_POLICY_NAME + getSaasAppUserName(), "high");
		
		gda.homepage(getWebDriver(), suiteData);
		gda.selectFolder(getWebDriver(), "sanity");
		gda.deleteFile(getWebDriver(), GatewayTestConstants.GDRIVE_ORGINAL_FILE);
		//gda.uploadFile(getWebDriver(), GatewayTestConstants.UPLOAD_FILE_PATH+ GatewayTestConstants.GDRIVE_ORGINAL_FILE);
		gda.clickUploadFile(getWebDriver());
		uploadSingleFile(GatewayTestConstants.UPLOAD_FILE_PATH+ GatewayTestConstants.GDRIVE_ORGINAL_FILE, suiteData);

		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Policy Violation");
		expectedDataMap.put(GatewayTestConstants.ACTION_TAKEN, "");
		expectedDataMap.put(GatewayTestConstants.POLICY_ACTION, "ALERT");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "[ALERT] " + suiteData.getTestUsername() + 
				" attempted to upload content:" + GatewayTestConstants.GDRIVE_ORGINAL_FILE.toLowerCase() + 
				" violating policy:" + GatewayTestConstants.GDRIVE_POLICY_NAME + getSaasAppUserName());
		expectedDataMap.put(GatewayTestConstants.POLICY_TYPE, "FileTransfer");
		
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "high");
		expectedDataMap.remove(GatewayTestConstants.COUNTRY);
		expectedDataMap.remove(GatewayTestConstants.USER_NAME);
		protectAction.deletePolicy(client, suiteData, GatewayTestConstants.GDRIVE_POLICY_NAME + getSaasAppUserName());
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),
				"Expected log: "+expectedDataMap.get(GatewayTestConstants.MESSAGE)+" does not exist");
		Logger.info("==================================================================================");
		Logger.info(" Upload file with policy severity high successful");
		Logger.info("==================================================================================");
	}
	
	@Priority(4)
	@Test(groups ={"Regression", "REACH"}) 
	public void gDrive_Test_Upload_Policy_With_Block() throws Exception {
		Logger.info("==================================================================================");
		Logger.info(" Upload file with  policy severity high ");
		Logger.info("==================================================================================");
		printCredentials();
		String fromTime=backend.getCurrentTime();
		protectAction.createEnableFileTransferPolicyWithUploadAndDownloadBlock(client, suiteData, 
				GatewayTestConstants.GDRIVE_POLICY_NAME + getSaasAppUserName(), "high");
		gda.homepage(getWebDriver(), suiteData);
		gda.selectFolder(getWebDriver(), "sanity");
		gda.deleteFile(getWebDriver(), GatewayTestConstants.GDRIVE_ORGINAL_FILE);
		//gda.uploadFile(getWebDriver(), GatewayTestConstants.UPLOAD_FILE_PATH+ GatewayTestConstants.GDRIVE_ORGINAL_FILE);
		gda.clickUploadFile(getWebDriver());
		uploadSingleFile(GatewayTestConstants.UPLOAD_FILE_PATH+ GatewayTestConstants.GDRIVE_ORGINAL_FILE, suiteData);

		
		blockerdisplay = false;
		for (int i = 0; i < 3; i++) {
			try {
				boolean value = clickOkInPopup();
				if (value == true) {
					blockerdisplay = true;
					gda.hardWait(10);
				}
			} catch (Exception e) {
				Logger.info("Error " + e.getMessage());
			}
		}
		Assert.assertTrue(blockerdisplay, "Blocker Popup Not Found");
	
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Policy Violation");
		expectedDataMap.put(GatewayTestConstants.ACTION_TAKEN, "");
		expectedDataMap.put(GatewayTestConstants.POLICY_ACTION, "ALERT");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "[ALERT] " + suiteData.getTestUsername() + 
				" attempted to upload content:" + GatewayTestConstants.GDRIVE_ORGINAL_FILE.toLowerCase() + 
				" violating policy:" + GatewayTestConstants.GDRIVE_POLICY_NAME + getSaasAppUserName());
		expectedDataMap.put(GatewayTestConstants.POLICY_TYPE, "FileTransfer");
		
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "high");
		expectedDataMap.remove(GatewayTestConstants.COUNTRY);
		expectedDataMap.remove(GatewayTestConstants.USER_NAME);
		protectAction.deletePolicy(client, suiteData, GatewayTestConstants.GDRIVE_POLICY_NAME + getSaasAppUserName());
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),
				"Expected log: "+expectedDataMap.get(GatewayTestConstants.MESSAGE)+" does not exist");
		Logger.info("==================================================================================");
		Logger.info(" Upload file with policy severity high successful");
		Logger.info("==================================================================================");
	}
	
	
	@Priority(5)
	@Test(groups ={"Regression", "REACH"},retryAnalyzer=RetryAnalyzer.class) 
	public void gDrive_Test_Upload_Policy_Deactivate_Policy() throws Exception {
		String fromTime=backend.getCurrentTime();
		Logger.info("==================================================================================");
		Logger.info(" Verifying upload activity event for One Drive business");
		Logger.info("==================================================================================");
		printCredentials();
		protectAction.deActivatePolicy(client, suiteData, 
				GatewayTestConstants.GDRIVE_POLICY_NAME + getSaasAppUserName());
		gda.homepage(getWebDriver(), suiteData);
		gda.selectFolder(getWebDriver(), "sanity");
		gda.deleteFile(getWebDriver(), GatewayTestConstants.GDRIVE_ORGINAL_FILE);
		//gda.uploadFile(getWebDriver(), GatewayTestConstants.UPLOAD_FILE_PATH+ GatewayTestConstants.GDRIVE_ORGINAL_FILE);
		gda.clickUploadFile(getWebDriver());
		uploadSingleFile(GatewayTestConstants.UPLOAD_FILE_PATH+ GatewayTestConstants.GDRIVE_ORGINAL_FILE, suiteData);

		expectedDataMap.clear(); 
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Upload");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User uploaded file " + GatewayTestConstants.GDRIVE_ORGINAL_FILE); 
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File");
		expectedDataMap.put(GatewayTestConstants.FILE_SIZE, GWCommonUtils.getFileSize(GatewayTestConstants.GDRIVE_ORGINAL_FILE));
		expectedDataMap.put(GatewayTestConstants.OBJECT_NAME, GatewayTestConstants.GDRIVE_ORGINAL_FILE);
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("==================================================================================");
		Logger.info("Upload File Successful");
		Logger.info("==================================================================================");
	}
	
	@Priority(6)
	@Test(groups ={"Regression", "REACH"}) 
	public void gDrive_Test_Upload_Policy_With_Reactivate_Block() throws Exception {
		String fromTime=backend.getCurrentTime();
		Logger.info("==================================================================================");
		Logger.info(" Upload file with  policy severity high ");
		Logger.info("==================================================================================");
		printCredentials();
		protectAction.activatePolicy(client, suiteData, 
				GatewayTestConstants.GDRIVE_POLICY_NAME + getSaasAppUserName());
		
		gda.homepage(getWebDriver(), suiteData);
		gda.selectFolder(getWebDriver(), "sanity");
		gda.deleteFile(getWebDriver(), GatewayTestConstants.GDRIVE_ORGINAL_FILE);
		//gda.uploadFile(getWebDriver(), GatewayTestConstants.UPLOAD_FILE_PATH+ GatewayTestConstants.GDRIVE_ORGINAL_FILE);
		gda.clickUploadFile(getWebDriver());
		uploadSingleFile(GatewayTestConstants.UPLOAD_FILE_PATH+ GatewayTestConstants.GDRIVE_ORGINAL_FILE, suiteData);

		
		blockerdisplay = false;
		for (int i = 0; i < 3; i++) {
			try {
				boolean value = clickOkInPopup();
				if (value == true) {
					blockerdisplay = true;
					gda.hardWait(10);
				}
			} catch (Exception e) {
				Logger.info("Error " + e.getMessage());
			}
		}
		Assert.assertTrue(blockerdisplay, "Blocker Popup Not Found");
	
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Policy Violation");
		expectedDataMap.put(GatewayTestConstants.ACTION_TAKEN, "");
		expectedDataMap.put(GatewayTestConstants.POLICY_ACTION, "ALERT");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "[ALERT] " + suiteData.getTestUsername() + 
				" attempted to upload content:" + GatewayTestConstants.GDRIVE_ORGINAL_FILE.toLowerCase() + 
				" violating policy:" + GatewayTestConstants.GDRIVE_POLICY_NAME + getSaasAppUserName());
		expectedDataMap.put(GatewayTestConstants.POLICY_TYPE, "FileTransfer");
		
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "high");
		expectedDataMap.remove(GatewayTestConstants.COUNTRY);
		expectedDataMap.remove(GatewayTestConstants.USER_NAME);
		protectAction.deletePolicy(client, suiteData, GatewayTestConstants.GDRIVE_POLICY_NAME + getSaasAppUserName());
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),
				"Expected log: "+expectedDataMap.get(GatewayTestConstants.MESSAGE)+" does not exist");
		Logger.info("==================================================================================");
		Logger.info(" Upload file with policy severity high successful");
		Logger.info("==================================================================================");
	}
	
	
	@Priority(7)
	@Test(groups ={"Regression", "REACH"}) 
	public void gDrive_Test_Upload_Policy_With_Block_For1MB_LessThanMBFile() throws Exception {
		Logger.info("==================================================================================");
		Logger.info(" Upload file with  policy severity high ");
		Logger.info("==================================================================================");
		printCredentials();
		String fromTime = backend.getCurrentTime();
		protectAction.deletePolicy(client, suiteData, GatewayTestConstants.GDRIVE_POLICY_NAME + getSaasAppUserName());
		protectAction.createEnableFileTransferPolicyWithUploadAndDownloadBlockWithFileSize(client, suiteData, 
				GatewayTestConstants.GDRIVE_POLICY_NAME + getSaasAppUserName(), "high", 0, 1048576);
		gda.homepage(getWebDriver(), suiteData);
		gda.selectFolder(getWebDriver(), "sanity");
		gda.deleteFile(getWebDriver(), GatewayTestConstants.GDRIVE_ORGINAL_FILE);
		//gda.uploadFile(getWebDriver(), GatewayTestConstants.UPLOAD_FILE_PATH+ GatewayTestConstants.GDRIVE_ORGINAL_FILE);
		gda.clickUploadFile(getWebDriver());
		uploadSingleFile(GatewayTestConstants.UPLOAD_FILE_PATH+ GatewayTestConstants.GDRIVE_ORGINAL_FILE, suiteData);


		blockerdisplay = false;
		for (int i = 0; i < 3; i++) {
			try {
				boolean value = clickOkInPopup();
				if (value == true) {
					blockerdisplay = true;
					gda.hardWait(10);
				}
			} catch (Exception e) {
				Logger.info("Error " + e.getMessage());
			}
		}
		Assert.assertTrue(blockerdisplay, "Blocker Popup Not Found");
	
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Policy Violation");
		expectedDataMap.put(GatewayTestConstants.ACTION_TAKEN, "");
		expectedDataMap.put(GatewayTestConstants.POLICY_ACTION, "ALERT");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "[ALERT] " + suiteData.getTestUsername() + 
				" attempted to upload content:" + GatewayTestConstants.GDRIVE_ORGINAL_FILE.toLowerCase() + 
				" violating policy:" + GatewayTestConstants.GDRIVE_POLICY_NAME + getSaasAppUserName());
		expectedDataMap.put(GatewayTestConstants.POLICY_TYPE, "FileTransfer");
		
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "high");
		expectedDataMap.remove(GatewayTestConstants.COUNTRY);
		expectedDataMap.remove(GatewayTestConstants.USER_NAME);
		protectAction.deletePolicy(client, suiteData, GatewayTestConstants.GDRIVE_POLICY_NAME + getSaasAppUserName());
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),
				"Expected log: "+expectedDataMap.get(GatewayTestConstants.MESSAGE)+" does not exist");
		Logger.info("==================================================================================");
		Logger.info(" Upload file with policy severity high successful");
		Logger.info("==================================================================================");
		
	}
	
	
	@Priority(8)
	@Test(groups ={"Regression", "REACH"}) 
	public void gDrive_Test_Upload_Policy_With_Block_For1MB_MoreThan1MBFile() throws Exception {
		printCredentials();
		protectAction.deletePolicy(client, suiteData, GatewayTestConstants.GDRIVE_POLICY_NAME + getSaasAppUserName());
		protectAction.createEnableFileTransferPolicyWithUploadAndDownloadBlockWithFileSize(client, suiteData, 
				GatewayTestConstants.GDRIVE_POLICY_NAME + getSaasAppUserName(), "high", 0, 1048576);

		gda.homepage(getWebDriver(), suiteData);
		gda.selectFolder(getWebDriver(), "sanity");
		gda.deleteFile(getWebDriver(), GatewayTestConstants.GDRIVE_1MB_FILE_UPLOAD);
		//gda.uploadFile(getWebDriver(), System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+
		//		"resources"+File.separator+"meta"+File.separator + GatewayTestConstants.GDRIVE_1MB_FILE_UPLOAD);
		gda.clickUploadFile(getWebDriver());
		uploadSingleFile(System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+
						"resources"+File.separator+"meta"+File.separator + GatewayTestConstants.GDRIVE_1MB_FILE_UPLOAD, suiteData);

		expectedDataMap.clear(); 
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Upload");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User uploaded file " + GatewayTestConstants.GDRIVE_1MB_FILE_UPLOAD); 
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File");
		expectedDataMap.put(GatewayTestConstants.FILE_SIZE, "1277");
		expectedDataMap.put(GatewayTestConstants.OBJECT_NAME, GatewayTestConstants.GDRIVE_1MB_FILE_UPLOAD);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	

	@Priority(9)
	@Test(groups ={"Regression", "REACH"}) 
	public void gDrive_Test_Upload_Encryption_And_Block_Policy() throws Exception {
		Logger.info("==================================================================================");
		Logger.info(" Verifying encrypt upload activity with block event");
		Logger.info("==================================================================================");
		printCredentials();
		fromTime=backend.getCurrentTime();
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME+getSaasAppUserName(),
				suiteData, backend.getHeaders(suiteData));
		protectAction.deletePolicy(client, suiteData, 
				GatewayTestConstants.GDRIVE_POLICY_NAME + getSaasAppUserName());
		
		protectAction.createEnableFileTransferPolicyWithUploadAndDownloadBlock(client, suiteData, 
				GatewayTestConstants.GDRIVE_POLICY_NAME + getSaasAppUserName(), "high");
		policy.createEnableEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME+getSaasAppUserName(),
				suiteData, backend.getHeaders(suiteData));
		
		gda.homepage(getWebDriver(), suiteData);
		gda.deleteFile(getWebDriver(), GatewayTestConstants.GDRIVE_ORGINAL_FILE);
		//gda.uploadFile(getWebDriver(), GatewayTestConstants.UPLOAD_FILE_PATH+ GatewayTestConstants.GDRIVE_ORGINAL_FILE);
		gda.clickUploadFile(getWebDriver());
		uploadSingleFile(GatewayTestConstants.UPLOAD_FILE_PATH+ GatewayTestConstants.GDRIVE_ORGINAL_FILE, suiteData);
		
		blockerdisplay = false;
		for (int i = 0; i < 3; i++) {
			try {
				boolean value = clickOkInPopup();
				if (value == true) {
					blockerdisplay = true;
					gda.hardWait(10);
				}
			} catch (Exception e) {
				Logger.info("Error " + e.getMessage());
			}
		}
		Assert.assertTrue(blockerdisplay, "Blocker Popup Not Found");
		
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Policy Violation");
		expectedDataMap.put(GatewayTestConstants.ACTION_TAKEN, "");
		expectedDataMap.put(GatewayTestConstants.POLICY_ACTION, "ALERT");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "[ALERT] " + suiteData.getTestUsername() + 
				" attempted to upload content:" + GatewayTestConstants.GDRIVE_ORGINAL_FILE.toLowerCase() + 
				" violating policy:" + GatewayTestConstants.GDRIVE_POLICY_NAME + getSaasAppUserName());
		expectedDataMap.put(GatewayTestConstants.POLICY_TYPE, "FileTransfer");
		
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "high");
		expectedDataMap.remove(GatewayTestConstants.COUNTRY);
		expectedDataMap.remove(GatewayTestConstants.USER_NAME);
		protectAction.deletePolicy(client, suiteData, GatewayTestConstants.GDRIVE_POLICY_NAME + getSaasAppUserName());
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),
				"Expected log: "+expectedDataMap.get(GatewayTestConstants.MESSAGE)+" does not exist");
		Logger.info("==================================================================================");
		Logger.info(" Upload file with policy severity high successful");
		Logger.info("==================================================================================");
	}
	
	
	@Priority(10)
	@Test(groups ={"Regression", "REACH"}) 
	public void o365_Test_Upload_File_Block_With_File_Format_Policy() throws Exception {
		utils.createCIQProfile(CIQConstants.ZERO, suiteData, GatewayTestConstants.CIQWORDBLOCK, 
				GatewayTestConstants.CIQWORDBLOCK, "DCI_FILE_FORMAT","class:WORDPROCESSOR");
		printCredentials();
		Logger.info("==================================================================================" );
		Logger.info(" Download file with Decrypton And Block policy severity high ");
		Logger.info("==================================================================================" );
		fromTime=backend.getCurrentTime();
		protectAction.deletePolicy(client, suiteData, GatewayTestConstants.GDRIVE_POLICY_NAME + getSaasAppUserName());
		protectAction.createEnableFileTransferPolicyWithUploadAndDownloadBlockByFileFormatProfile(client, suiteData, 
				GatewayTestConstants.GDRIVE_POLICY_NAME + getSaasAppUserName(), "high", GatewayTestConstants.CIQWORDBLOCK);

		gda.homepage(getWebDriver(), suiteData);
		gda.deleteFile(getWebDriver(), GatewayTestConstants.GDRIVE_ORGINAL_DOC);
//		gda.uploadFile(getWebDriver(), GatewayTestConstants.UPLOAD_FILE_PATH+ GatewayTestConstants.GDRIVE_ORGINAL_DOC);
		gda.clickUploadFile(getWebDriver());
		uploadSingleFile(GatewayTestConstants.UPLOAD_FILE_PATH+ GatewayTestConstants.GDRIVE_ORGINAL_DOC, suiteData);
		blockerdisplay = false;
		for (int i = 0; i < 3; i++) {
			try {
				boolean value = clickOkInPopup();
				if (value == true) {
					blockerdisplay = true;
					gda.hardWait(10);
				}
			} catch (Exception e) {
				Logger.info("Error " + e.getMessage());
			}
		}
		Assert.assertTrue(blockerdisplay, "Blocker Popup Not Found");
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Policy Violation");
		expectedDataMap.put(GatewayTestConstants.ACTION_TAKEN, "");
		expectedDataMap.put(GatewayTestConstants.POLICY_ACTION, "ALERT");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "[ALERT] " + suiteData.getTestUsername() + 
				" attempted to upload content:" + GatewayTestConstants.GDRIVE_ORGINAL_DOC.toLowerCase() + 
				" violating policy:" + GatewayTestConstants.GDRIVE_POLICY_NAME + getSaasAppUserName());
		expectedDataMap.put(GatewayTestConstants.POLICY_TYPE, "FileTransfer");
		
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "high");
		expectedDataMap.remove(GatewayTestConstants.COUNTRY);
		expectedDataMap.remove(GatewayTestConstants.USER_NAME);
		protectAction.deletePolicy(client, suiteData, GatewayTestConstants.GDRIVE_ORGINAL_DOC + getSaasAppUserName());
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),
				"Expected log: "+expectedDataMap.get(GatewayTestConstants.MESSAGE)+" does not exist");
		Logger.info("==================================================================================");
		Logger.info(" Upload file with policy severity high successful");
		Logger.info("==================================================================================");
	}
	
	public String getSaasAppUserName(){
		return suiteData.getSaasAppUsername().replaceAll("@", "_");
	}
	
	@BeforeClass(groups ={"Regression", "REACH"})
	public void doBeforeClass() throws Exception {
		try {
			Logger.info("Delete Policy Before Test ");
			protectData.setPolicyName(GatewayTestConstants.GDRIVE_POLICY_NAME + getSaasAppUserName());
			protectAction.deletePolicy(client, suiteData, GatewayTestConstants.GDRIVE_POLICY_NAME + getSaasAppUserName());
			policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME+getSaasAppUserName(),
					suiteData, backend.getHeaders(suiteData));
			Logger.info(" policy to delete");
		} catch(Exception e) {
			Logger.info("No policy to delete");
		}
	}
	
	@AfterClass(groups ={"Regression", "REACH"})
	public void doAfterClass() throws Exception {
		try {
			Logger.info("Delete Policy After Test ");
			protectData.setPolicyName(GatewayTestConstants.GDRIVE_POLICY_NAME + getSaasAppUserName());
			protectAction.deletePolicy(client, suiteData, GatewayTestConstants.GDRIVE_POLICY_NAME + getSaasAppUserName());
			policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME+getSaasAppUserName(),
					suiteData, backend.getHeaders(suiteData));
			Logger.info(" policy to delete");
		} catch(Exception e) {
			Logger.info("No policy to delete");
		}
	}
	

}
