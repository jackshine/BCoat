package com.elastica.tests.google;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.elastica.common.CommonTest;
import com.elastica.gateway.GatewayTestConstants;
import com.elastica.listeners.Priority;
import com.elastica.listeners.RetryAnalyzer;
import com.elastica.logger.Logger;

public class GDriveTestsDCI extends CommonTest {

	Map <String, Object> expectedDataMap = new HashMap<String, Object>();
	String fromTime=null;
	String title;

	@Priority(1)
	@Test(groups ={"DCI","SANITY", "MINI_SANITY"})
	public void loginToCloudSocAppAndSetupSSO() throws Exception {
		fromTime=backend.getCurrentTime();
		Logger.info("==================================================================================");
		Logger.info("Login into CloudSoc");
		Logger.info("==================================================================================");
		login.loginCloudSocPortal(getWebDriver(), suiteData);
		Logger.info("==================================================================================");
		Logger.info("Loging into CloudSoc done");
		Logger.info("==================================================================================");
	}
	
	public String getOS() {
		Logger.info("OS " +  System.getProperty("os.name"));
		return System.getProperty("os.name");
	}
	
	@Priority(2)
	@Test(groups ={"CIQ","SANITY", "MINI_SANITY", "SANITY1", "REACH"})  
	public void gDriveValidateLogin() throws Exception {
		Logger.info("==================================================================================");
		Logger.info("Verifying the login event");
		Logger.info("==================================================================================");
		expectedDataMap.clear();
		gda.login(getWebDriver(), suiteData);
		setCommonFieldsInExpectedDataMap();
		setLocationFieldsInExpectedDataMap();
		expectedDataMap.remove(GatewayTestConstants.FACILITY);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Login");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User logged in"); 
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Session");
		expectedDataMap.put(GatewayTestConstants.REQ_URI, GatewayTestConstants.GDRIVE_REQ_URI_LOGIN);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.remove("region");
		expectedDataMap.remove("location");
		expectedDataMap.remove("city");

		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("==================================================================================");
		Logger.info("Verify login successful");
		Logger.info("==================================================================================");
	}
		
	
	
	@Priority(3)
	@Test(groups ={"SANITY","DCI", "REACH"}) 
	public void gDriveUploadFilesDCI() throws Exception {
		Logger.info("==================================================================================");
		Logger.info("Upload CIQ File operation - Start ");
		Logger.info("==================================================================================");

		
		String [] ciqfiles={"pci.txt", "PII_TXT.rtf", "pii.txt", "glba.txt", "hipaa.txt", "vba_macro.xls"};
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData));
		policy.createCIQPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData));
		gda.homepage(getWebDriver(), suiteData);
		gda.selectFolder(getWebDriver(), "CIQ");
		//gda.MultipleFileupload(getWebDriver(), ciqfiles);
		for(int i=0; i<ciqfiles.length;i++)
		{
			gda.deleteFile(getWebDriver(), ciqfiles[i]);
		}
	//	gda.deleteFile(getWebDriver(), GatewayTestConstants.GDRIVE_ORGINAL_FILE);
		for(int i=0; i<ciqfiles.length;i++){
			//gda.login(getWebDriver(), suiteData);
			gda.uploadFile1(getWebDriver(),  System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+
				"resources"+File.separator+"ciq"+File.separator + ciqfiles[i]);
		
		}
		gda.homepage(getWebDriver(), suiteData);
		
		Logger.info("==================================================================================");
		Logger.info("Upload CIQ File operation - complete ");
		Logger.info("==================================================================================");
		}

	
	
	@Priority(4)
	@Test(groups ={"CIQ", "REACH_AGENT", "REACH"},retryAnalyzer=RetryAnalyzer.class) 
	public void gDriveValidatePII_CIQ() throws Exception {
		Reporter.log("Verifying decrypt download activity event with policy", true);
		//fromTime=backend.getCurrentTime();
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "uploaded file PII_TXT.rtf in CIQ fo");
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Upload");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("Decrypt download actvity event verification successfull", true);
	}
	
	
	@Priority(5)
	@Test(groups ={"CIQ", "REACH_AGENT", "REACH"},retryAnalyzer=RetryAnalyzer.class) 
	public void gDriveValidatePCI_CIQ() throws Exception {
		Reporter.log("Verifying decrypt download activity event with policy", true);
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "uploaded file pci.txt in CIQ folder");
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Upload");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("Decrypt download actvity event verification successfull", true);
	}
	
	
	@Priority(6)
	@Test(groups ={"CIQ", "REACH_AGENT", "REACH"},retryAnalyzer=RetryAnalyzer.class) 
	public void gDriveValidateGLBA_CIQ() throws Exception {
		Reporter.log("Verifying decrypt download activity event with policy", true);
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "uploaded file glba.txt in CIQ folder");
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Upload");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("Decrypt download actvity event verification successfull", true);
	}
	
	
	@Priority(7)
	@Test(groups ={"CIQ", "REACH_AGENT", "REACH"},retryAnalyzer=RetryAnalyzer.class) 
	public void gDriveValidateHIPPA_CIQ() throws Exception {
		Reporter.log("Verifying decrypt download activity event with policy", true);
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "uploaded file PII_TXT.rtf in CIQ folder");
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Upload");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("Decrypt download actvity event verification successfull", true);
	}
	
	
	@Priority(8)
	//@Test(groups ={"CIQ", "REACH_AGENT"},retryAnalyzer=RetryAnalyzer.class) 
	public void gDriveValidateVirus_CIQ() throws Exception {
		Reporter.log("Verifying decrypt download activity event with policy", true);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "File virus.zip upload has risk(s) - ContentIQ Violations, Virus / Malwar");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("Decrypt download actvity event verification successfull", true);
	}
	
	@Priority(9)
	@Test(groups ={"CIQ", "REACH_AGENT", "REACH"},retryAnalyzer=RetryAnalyzer.class) 
	public void gDriveValidateVBA_MACRO_CIQ() throws Exception {
		Reporter.log("Verifying decrypt download activity event with policy", true);
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "uploaded file vba_macro.xls in CIQ folder");
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Upload");
		
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("Decrypt download actvity event verification successfull", true);
	}
	
	@Priority(10)
	//@Test(groups ={"CIQ", "REACH_AGENT"},retryAnalyzer=RetryAnalyzer.class) //,dependsOnMethods = { "o365_Test_01_ValidateLogingActivityEvent" }
	public void gDriveValidateSOURCE_CODE_CIQ() throws Exception {
		Reporter.log("Verifying decrypt download activity event with policy", true);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "File source_code.xls upload has risk(s) - Source Code");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Reporter.log("Decrypt download actvity event verification successfull", true);
	}
		

	@Priority(9)
	//@Test(groups ={"UPLOAD4", "SANITY", "P1_SANITY"})
	public void gDrive_Test_006_UploadFile() throws Exception {
		fromTime=backend.getCurrentTime();
		Logger.info("==================================================================================");
		Logger.info("Upload File");
		Logger.info("==================================================================================");
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData));
		gda.homepage(getWebDriver(), suiteData);
		gda.selectFolder(getWebDriver(), "sanity");
		gda.deleteFile(getWebDriver(), GatewayTestConstants.GDRIVE_ORGINAL_FILE);
		gda.uploadFile1(getWebDriver(), System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+
				"resources"+File.separator+"meta"+File.separator + GatewayTestConstants.GDRIVE_ORGINAL_FILE);
		expectedDataMap.clear(); 
		setCommonFieldsInExpectedDataMap();
		setLocationFieldsInExpectedDataMap();
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Upload");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User uploaded file " + GatewayTestConstants.GDRIVE_ORGINAL_FILE); 
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File");
		expectedDataMap.put(GatewayTestConstants.FILE_SIZE, "59210");
		expectedDataMap.put(GatewayTestConstants.OBJECT_NAME, GatewayTestConstants.GDRIVE_ORGINAL_FILE);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("==================================================================================");
		Logger.info("Upload File Successful");
		Logger.info("==================================================================================");
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
		//expectedDataMap.put(GatewayTestConstants.REQ_SIZE, "NOT_EMPTY");
		expectedDataMap.put(GatewayTestConstants.REQ_URI, "NOT_EMPTY");
		//expectedDataMap.put(GatewayTestConstants.RESP_SIZE, "NOT_EMPTY");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.TIME_ZONE, "NOT_EMPTY");
		expectedDataMap.put(GatewayTestConstants.USER, suiteData.getSaasAppUsername());
		//expectedDataMap.put(GatewayTestConstants.USER_NAME, "NOT_EMPTY");
		expectedDataMap.put(GatewayTestConstants.USER_AGENT, "NOT_EMPTY");
		//expectedDataMap.put(GatewayTestConstants.USER_NAME, "NOT_EMPTY");
		expectedDataMap.put(GatewayTestConstants.VERSION, "NOT_EMPTY");
		//expectedDataMap.put(GatewayTestConstants.RESP_CODE, "NOT_EMPTY");
		expectedDataMap.put(GatewayTestConstants.SOURCE, "GW");
		
	}
	
	public void setLocationFieldsInExpectedDataMap(){
		expectedDataMap.put(GatewayTestConstants.CITY, "NOT_EMPTY");
		//expectedDataMap.put(GatewayTestConstants.COUNTRY, "NOT_EMPTY");
		expectedDataMap.put(GatewayTestConstants.LATITUDE, "NOT_EMPTY");
		expectedDataMap.put(GatewayTestConstants.LONGITUDE, "NOT_EMPTY");
		expectedDataMap.put(GatewayTestConstants.LOCATION, "NOT_EMPTY");
		expectedDataMap.put(GatewayTestConstants.REGION, "NOT_EMPTY");
	}	
	
	@BeforeClass(groups ={"SANITY", "P1_SANITY", "REACH"})
	public void doBeforeClass() throws Exception {
		Logger.info("Delete Policy Before Test ");
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME+ suiteData.getSaasAppName(), suiteData, backend.getGWHeaders(suiteData));
		policy.createCIQPolicy2(GatewayTestConstants.DCI_POLICY_NAME+ suiteData.getSaasAppName(), suiteData, backend.getGWHeaders(suiteData));
	}
	
	@AfterClass(groups ={"SANITY", "P1_SANITY", "REACH"})
	public void doAfterClass() throws Exception {
		Logger.info("Delete Policy After Test ");
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME+ suiteData.getSaasAppName(), suiteData, backend.getGWHeaders(suiteData));
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.DCI_POLICY_NAME+ suiteData.getSaasAppName(), suiteData, backend.getGWHeaders(suiteData));
	}
	public String getSaasAppUserName(){
		return suiteData.getSaasAppUsername().replaceAll("@", "_");
	}
	


}
