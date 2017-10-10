package com.elastica.beatle.tests.gateway.box;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;

import static org.testng.Assert.assertTrue;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.Reporter;
import com.elastica.beatle.gateway.CommonConfiguration;
import com.elastica.beatle.gateway.GatewayTestConstants;
import com.elastica.beatle.gateway.LogValidator;
import com.elastica.beatle.gateway.PolicyAccessEnforcement;
import com.elastica.beatle.gateway.dto.GWForensicSearchResults;
import com.elastica.beatle.protect.ProtectFunctions;


/*******************Author**************
 * 
 * @author usman
 */



public class FileTransferInlineInspectionPolicyTest extends CommonConfiguration {

	String currentTimeInJodaFormat;
	GWForensicSearchResults fsr;
	ArrayList<String> messages = new ArrayList<String>();
	ArrayList<String> objectTypeList = new ArrayList<String>();
	ArrayList<String> objectNameList = new ArrayList<String>();
	ArrayList<String> severityList = new ArrayList<String>();
	LogValidator logValidator;
	String userLitral="User";
	String fileName="Test.pdf";
	String policyName="PolicyFT_FileType";
	Map<String, String>policyDataMap= new HashMap<String, String>(); 
	ProtectFunctions protectFunctions = new ProtectFunctions();
	Map <String, String>expectedFields= new HashMap<String, String>();
	
	@BeforeMethod(alwaysRun=true)
	public void beforMethod(Method method) throws Exception {
		//deleteAllPolicies();
		Reporter.log("--------------------------------------------", true);
		Reporter.log("Deleting all policies", true);
		Reporter.log("--------------------------------------------", true);
	}
	
	
	
	@Test(groups ={"TEST1"}, dataProvider = "_policyFileTransferupload")
	public void verifyFileTransferPolicyEnabledUpload(String policyName, String saasApps,  String testUserName,  String notifyEmailId, String transferType, String ContentIQProfile, String fileType, String fileName,  String activityType, String objectType, String severity, String riskFile, String logFile) throws Exception{
		Reporter.log("Validate User Upload the file/folder to folder with Policy Enabled", true);
		String contentIQFilePath=GatewayTestConstants.CONTENTIQ_FILE_PATH+"/Terms/"+riskFile;
		testUserName=testUserName+"@"+suiteData.getTenantDomainName();
		String expectedMsg="[ALERT] "+testUserName+ " attempted to "+transferType+" content:"+fileName.toLowerCase()+"."+fileType+" violating policy:"+policyName;
		policyDataMap.clear();
		policyDataMap.put(GatewayTestConstants.POLICY_NAME, policyName);
		policyDataMap.put(GatewayTestConstants.APPLICATIONS, saasApps);
		policyDataMap.put(GatewayTestConstants.TARGET_USER, testUserName);
		policyDataMap.put(GatewayTestConstants.NOTIFY_EMAILID, notifyEmailId+"@"+suiteData.getTenantDomainName());
		policyDataMap.put(GatewayTestConstants.TRANSFER_TYPE, transferType);
		policyDataMap.put(GatewayTestConstants.FILE_TYPE, fileType);
		policyDataMap.put(GatewayTestConstants.FILE_NAME, fileName);
		policyDataMap.put(GatewayTestConstants.CONTENT_IQ, ContentIQProfile);
		//PolicyAccessEnforcement.deletePolicy(suiteData, requestHeader, policyDataMap);
		PolicyAccessEnforcement.fileTransferPolicyCreateEnable(suiteData, requestHeader, policyDataMap);
		replayInlineInspectionLogs(logFile, contentIQFilePath);
		PolicyAccessEnforcement.deletePolicy(suiteData, requestHeader, policyDataMap);
		
		expectedFields.put("message", expectedMsg);
		expectedFields.put("Activity_type", "Policy Violation");
		expectedFields.put("policy_type", "FileTransfer");
		expectedFields.put("policy_action", "ALERT");
		expectedFields.put("severity", severity);
		//boolean check=validateLogsFields(responce,expectedFields );
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), expectedFields)), "Logs does not match" ); 
	}
	
	@DataProvider
	public Object[][] _policyFileTransferupload() {
		return new Object[][]{
			//Policy Name                 App     Test User   admin     Object Access Activity Access      Activity Type     Object Type  Severity
			{ "FILE_TRANSFER_UPLOAD1",   "Box",   "testuser1",  "admin",  "upload",  "CIP", "__ALL_EL__",   "ANY",         "Policy Violation",  "Upload", "critical", "India_PAN.txt",  "Test,AFolder,upload_txt.log" },
			
		};
	}
	
	
	
	
	@Test(groups ={"TEST"}, dataProvider = "_policyFileTransferDownload")
	public void verifyFileTransferPolicyEnabled(String policyName, String saasApps,  String testUserName,  String notifyEmailId, String transferType, String fileType, String fileName,  String activityType, String objectType, String severity, String logFile) throws Exception{
		Reporter.log("Validate User Upload the file/folder to folder with Policy Enabled", true);
		testUserName=testUserName+"@"+suiteData.getTenantDomainName();
		String expectedMsg="[ALERT] "+testUserName+ " attempted to "+transferType+" content:"+fileName.toLowerCase()+"."+fileType+" violating policy:"+policyName;
		policyDataMap.clear();
		policyDataMap.put(GatewayTestConstants.POLICY_NAME, policyName);
		policyDataMap.put(GatewayTestConstants.APPLICATIONS, saasApps);
		policyDataMap.put(GatewayTestConstants.TARGET_USER, testUserName);
		policyDataMap.put(GatewayTestConstants.NOTIFY_EMAILID, notifyEmailId+"@"+suiteData.getTenantDomainName());
		policyDataMap.put(GatewayTestConstants.TRANSFER_TYPE, transferType);
		policyDataMap.put(GatewayTestConstants.FILE_TYPE, fileType);
		policyDataMap.put(GatewayTestConstants.FILE_NAME, fileName);
		//PolicyAccessEnforcement.deletePolicy(suiteData, requestHeader, policyDataMap);
		PolicyAccessEnforcement.fileTransferPolicyCreateEnable(suiteData, requestHeader, policyDataMap);
		replayLogs(logFile);
		PolicyAccessEnforcement.deletePolicy(suiteData, requestHeader, policyDataMap);
		expectedFields.put("message", expectedMsg);
		expectedFields.put("Activity_type", "Policy Violation");
		expectedFields.put("policy_type", "FileTransfer");
		expectedFields.put("policy_action", "ALERT");
		expectedFields.put("severity", severity);
		//boolean check=validateLogsFields(responce,expectedFields );
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), expectedFields)), "Logs does not match" ); 
	
	}
	
	@DataProvider
	public Object[][] _policyFileTransferDownload() {
		return new Object[][]{
			//Policy Name                 App     Test User   admin     Object Access Activity Access      Activity Type     Object Type  Severity
			{ "FILE_TRANSFER_DOWNLOAD1",   "Box",   "testuser1",  "admin",  "download",  "7z",         "Test",      "Policy Violation",  "Download", "critical","TestFile,AllFiles,download_7z.log" },
		};
	}
	

	
}
