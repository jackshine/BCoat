package com.elastica.beatle.tests.gateway;


import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.entity.StringEntity;
import org.json.JSONObject;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;

import static org.testng.Assert.assertTrue;
import static org.testng.Assert.assertFalse;

import java.lang.reflect.Method;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.Reporter;

import com.elastica.beatle.Authorization.AuthorizationHandler;
import com.elastica.beatle.RestClient.ClientUtil;
import com.elastica.beatle.gateway.CommonConfiguration;
import com.elastica.beatle.gateway.GatewayTestConstants;
import com.elastica.beatle.gateway.LogValidator;
import com.elastica.beatle.gateway.PolicyAccessEnforcement;
import com.elastica.beatle.gateway.dto.GWForensicSearchResults;
import com.elastica.beatle.protect.ProtectFunctions;


/*******************Author**************
 * 
 * @author Rocky
 */

public class UsersGroupsAnonymizationO365OneDriveFileTransferPolicyTest extends CommonConfiguration {

	String currentTimeInJodaFormat;
	GWForensicSearchResults fsr;
	ArrayList<String> messages = new ArrayList<String>();
	ArrayList<String> objectTypeList = new ArrayList<String>();
	ArrayList<String> objectNameList = new ArrayList<String>();
	ArrayList<String> severityList = new ArrayList<String>();
	LogValidator logValidator;
	String userLitral="User";
	String policyName="PolicyFT_FileType";
	Map<String, String>policyDataMap= new HashMap<String, String>(); 
	ProtectFunctions protectFunctions = new ProtectFunctions();
	Map <String, String> data = new HashMap<String, String>();
	
	@BeforeClass
	public void AddAnonymization() throws Exception {
		Reporter.log("Add Anonymization to Tenant", true);
		
		String tenantAcctId = getTenantAccountId();
		String payload = "{\"userAnonymization\":true,\"dpoName\": \"\", \"dpoPassword\": \"\",\"id\":\""+tenantAcctId+"\"}";
		Reporter.log("Payload:"+ payload, true);
		String responseBody = updateUserAnonymization(payload);
		
		//Invalidate the session after anonymization turned
		Reporter.log("Regenerate the session after anonymization turned on...:", true);
		HttpResponse CSRFHeader = AuthorizationHandler.getCSRFHeaders(suiteData.getUsername(),suiteData.getPassword(), suiteData);
		suiteData.setCSRFToken(AuthorizationHandler.getCSRFToken(CSRFHeader));
		suiteData.setSessionID(AuthorizationHandler.getUserSessionID(CSRFHeader));
		
	}
	
	@AfterClass
	public void RemoveAnonymization() throws Exception {
		Reporter.log("Remove Anonymization from Tenant", true);
		
		String tenantAcctId = getTenantAccountId();
		String payload = "{\"userAnonymization\":false,\"dpoName\":\""+ suiteData.getDpoUsername() +"\",\"dpoPassword\":\""+ suiteData.getDpoPassword() +"\", \"id\":\""+tenantAcctId+"\"}";
		Reporter.log("Payload:"+ payload, true);
		String responseBody = updateUserAnonymization(payload);
		Reporter.log("Response body:"+ responseBody, true);

	}
	
	@Test(dataProvider = "FileTransfer")
	public void verify_FileTransferPolicy(String testUser, String testUserGroup, String userAction, String policyType, String policyUser, String policyGroup, Boolean verifyBlocked, String tcId) throws Exception{
		Reporter.log("Validate User Upload the file/folder to folder with Policy Enabled", true);
		
		String policyName = "FILE_TRANSFER_UPLOAD";
		String saasApps = "Office 365";
		String fileName = "Test.txt";
		String severity = "critical";
		String objectType = "Upload";
		testUser=testUser+"@"+suiteData.getTenantDomainName();
		String logFile;
		if (userAction.equalsIgnoreCase("upload")) {
			logFile = "Folder,Upload_file_test_txt.log";
		} else {
			logFile = "Folder,Download_file_test_txt.log";
		}
		
		addUserToGroup(testUser, testUserGroup);
		//the below is special case for c016 and c020 tests.
		if (policyUser.startsWith("admin")) {
			addUserToGroup(policyUser, testUserGroup);
		}
		
		policyDataMap.clear();
		policyDataMap.put(GatewayTestConstants.POLICY_NAME, policyName);
		policyDataMap.put(GatewayTestConstants.APPLICATIONS, saasApps);
		policyDataMap.put(GatewayTestConstants.TARGET_USER, policyUser);
		policyDataMap.put(GatewayTestConstants.TARGET_GROUP, policyGroup);
		policyDataMap.put(GatewayTestConstants.TRANSFER_TYPE, policyType);
		PolicyAccessEnforcement.fileTransferPolicyCreateEnable(suiteData, requestHeader, policyDataMap);
		
		replayLogsEPDV3(logFile);
		
		removeUserFromGroup(testUser, testUserGroup);
		PolicyAccessEnforcement.deletePolicy(suiteData, requestHeader, policyDataMap);
		
		String expectedMsg="[ALERT] "+testUser+ " attempted to "+policyType+" content:"+fileName.toLowerCase()+" violating policy:"+policyName;
		Reporter.log("expectedMsg: "+expectedMsg, true);
		data.clear();
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		data.put("policy_type", "FileTransfer");
		data.put("policy_action", "ALERT");
		data.put("action_taken", "block,email,");
		data.put("_PolicyViolated", policyName);
		
		boolean validationResult = validateLogsFields(fetchElasticSearchLogsUniversal(), data);
		if (verifyBlocked) {
			assertTrue(validationResult, "Investigate log for Blocking " + userAction + " is NOT available or Activity is NOT Blocked!!");
		} else {
			assertFalse(validationResult, "Investigate log for Blocking " + userAction + " is available or Activity is Blocked!!");
		}
		
	}

	@DataProvider
	public Object[][] FileTransfer() {
		return new Object[][]{
				//Test User, Test User Group, policyUser, policyGroup, verifyBlocked, tcId
				{"testuser1", "GroupA", "Upload", "upload", "", "GroupA", true, "C00100"},
				{"testuser1", "GroupA", "Download", "upload", "", "GroupA", false, "C00200"},
				{"testuser1", "", "Upload", "upload", "", "GroupA", false, "C00300"},
				{"testuser1", "", "Download", "upload", "", "GroupA", false, "C00400"},
				{"testuser1", "GroupB", "Upload", "upload", "", "GroupA", false, "C00500"},
				{"testuser1", "GroupB", "Download", "upload", "", "GroupA", false, "C00600"},
				{"testuser1", "GroupA", "Download", "download", "", "GroupA", true, "C00700"},
				{"testuser1", "GroupA", "Upload", "download", "", "GroupA", false, "C00800"},
				{"testuser1", "", "Download", "download", "", "GroupA", false, "C00900"},
				{"testuser1", "", "Upload", "download", "", "GroupA", false, "C01000"},
				{"testuser1", "GroupB", "Upload", "download", "", "GroupA", false, "C01100"},
				{"testuser1", "GroupB", "Download", "download", "", "GroupA", false, "C01200"},
				{"testuser1", "", "Upload", "upload", "testuser1@gatewayo365beatle.com", "", true, "C01300"},
				{"testuser1", "", "Download", "upload", "testuser1@gatewayo365beatle.com", "", false, "C01400"},
				{"testuser1", "GroupA", "Upload", "upload", "testuser1@gatewayo365beatle.com", "", true, "C01500"},
				{"testuser1", "GroupA", "Upload", "upload", "admin@gatewayo365beatle.com", "", false, "C01600"},
				{"testuser1", "", "Download", "download", "testuser1@gatewayo365beatle.com", "", true, "C01700"},
				{"testuser1", "", "Upload", "download", "testuser1@gatewayo365beatle.com", "", false, "C01800"},
				{"testuser1", "GroupA", "Download", "download", "testuser1@gatewayo365beatle.com", "", true, "C01900"},
				{"testuser1", "GroupA", "Download", "download", "admin@gatewayo365beatle.com", "", false, "C02000"},
		};
	}	

	@Test(dataProvider = "FileTransferUpDown")
	public void verify_FileTransferPolicyUpDown(String testUser, String testUserGroup, String userAction, String policyUser, String policyGroup, Boolean verifyBlocked, String tcId) throws Exception{
		Reporter.log("Validate User Upload the file/folder to folder with Policy Enabled", true);
		
		String policyName = "FILE_TRANSFER_UPDOWN";
		String saasApps = "Office 365";
		String fileName = "Test.txt";
		String severity = "critical";
		String objectType = "Upload";
		String policyType = "upload\", \"download";
		testUser=testUser+"@"+suiteData.getTenantDomainName();
		String logFile;
		if (userAction.equalsIgnoreCase("upload")) {
			logFile = "Folder,Upload_file_test_txt.log";
		} else {
			logFile = "Folder,Download_file_test_txt.log";
		}
		
		addUserToGroup(testUser, testUserGroup);
		
		policyDataMap.clear();
		policyDataMap.put(GatewayTestConstants.POLICY_NAME, policyName);
		policyDataMap.put(GatewayTestConstants.APPLICATIONS, saasApps);
		policyDataMap.put(GatewayTestConstants.TARGET_USER, policyUser);
		policyDataMap.put(GatewayTestConstants.TARGET_GROUP, policyGroup);
		policyDataMap.put(GatewayTestConstants.TRANSFER_TYPE, policyType);
		PolicyAccessEnforcement.fileTransferPolicyCreateEnable(suiteData, requestHeader, policyDataMap);
		
		replayLogsEPDV3(logFile);
		
		removeUserFromGroup(testUser, testUserGroup);
		PolicyAccessEnforcement.deletePolicy(suiteData, requestHeader, policyDataMap);
		
		String expectedMsg="[ALERT] "+testUser+ " attempted to "+userAction.toLowerCase()+" content:"+fileName.toLowerCase()+" violating policy:"+policyName;
		Reporter.log("expectedMsg: "+expectedMsg, true);
		data.clear();
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		data.put("policy_type", "FileTransfer");
		data.put("policy_action", "ALERT");
		data.put("action_taken", "block,email,");
		data.put("_PolicyViolated", policyName);
		
		boolean validationResult = validateLogsFields(fetchElasticSearchLogsUniversal(), data);
		if (verifyBlocked) {
			assertTrue(validationResult, "Investigate log for Blocking " + userAction + " is NOT available or Activity is NOT Blocked!!");
		} else {
			assertFalse(validationResult, "Investigate log for Blocking " + userAction + " is available or Activity is Blocked!!");
		}
		
	}

	@DataProvider
	public Object[][] FileTransferUpDown() {
		return new Object[][]{
				//Test User, Test User Group, policyUser, policyGroup, verifyBlocked, tcId
				{"testuser1", "GroupA", "Upload", "", "GroupA", true, "C02100"},
				{"testuser1", "GroupA", "Download", "", "GroupA", true, "C02200"},
				{"testuser1", "", "Upload", "", "GroupA", false, "C02300"},
				{"testuser1", "", "Download", "", "GroupA", false, "C02400"},
				{"testuser1", "", "Upload", "testuser1@gatewayo365beatle.com", "", true, "C02500"},
				{"testuser1", "", "Download", "testuser1@gatewayo365beatle.com", "", true, "C02600"},
				{"testuser1", "", "Upload", "admin@gatewayo365beatle.com", "", false, "C02700"},
				{"testuser1", "", "Download", "admin@gatewayo365beatle.com", "", false, "C02800"},
				{"testuser1", "GroupA", "Upload", "admin@gatewayo365beatle.com", "GroupA", true, "C02900"},
				{"testuser1", "GroupA", "Download", "admin@gatewayo365beatle.com", "GroupA", true, "C03100"},
				{"testuser1", "", "Upload", "testuser1@gatewayo365beatle.com", "GroupA", true, "C03300"},
				{"testuser1", "", "Download", "testuser1@gatewayo365beatle.com", "GroupA", true, "C03400"},
		};
	}
	
	@Test(dataProvider = "FileTransferUpDownRepeat")
	public void verify_FileTransferPolicyUpDownRepeat(String testUser, String testUserGroup, String userAction, String policyUser, String policyGroup, Boolean verifyBlocked, String tcId) throws Exception{
		Reporter.log("Validate User Upload the file/folder to folder with Policy Enabled", true);
		
		String policyName = "FILE_TRANSFER_UPDOWN";
		String saasApps = "Office 365";
		String fileName = "Test.txt";
		String severity = "critical";
		String objectType = "Upload";
		String policyType = "upload\", \"download";
		testUser=testUser+"@"+suiteData.getTenantDomainName();
		String logFile;
		if (userAction.equalsIgnoreCase("upload")) {
			logFile = "Folder,Upload_file_test_txt.log";
		} else {
			logFile = "Folder,Download_file_test_txt.log";
		}
		
		addUserToGroup(testUser, testUserGroup);
		
		policyDataMap.clear();
		policyDataMap.put(GatewayTestConstants.POLICY_NAME, policyName);
		policyDataMap.put(GatewayTestConstants.APPLICATIONS, saasApps);
		policyDataMap.put(GatewayTestConstants.TARGET_USER, policyUser);
		policyDataMap.put(GatewayTestConstants.TARGET_GROUP, policyGroup);
		policyDataMap.put(GatewayTestConstants.TRANSFER_TYPE, policyType);
		PolicyAccessEnforcement.fileTransferPolicyCreateEnable(suiteData, requestHeader, policyDataMap);
		
		replayLogsEPDV3(logFile);
		
		String expectedMsg="[ALERT] "+testUser+ " attempted to "+userAction.toLowerCase()+" content:"+fileName.toLowerCase()+" violating policy:"+policyName;
		Reporter.log("expectedMsg: "+expectedMsg, true);
		data.clear();
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		data.put("policy_type", "FileTransfer");
		data.put("policy_action", "ALERT");
		data.put("action_taken", "block,email,");
		data.put("_PolicyViolated", policyName);
		
		boolean validationResult = validateLogsFields(fetchElasticSearchLogsUniversal(), data);
		if (verifyBlocked) {
			assertTrue(validationResult, "Investigate log for Blocking " + userAction + " is NOT available or Activity is NOT Blocked!!");
		} else {
			assertFalse(validationResult, "Investigate log for Blocking " + userAction + " is available or Activity is Blocked!!");
		}
		
		//Repeat Activity Once Again.
		replayLogsEPDV3(logFile);
		
		removeUserFromGroup(testUser, testUserGroup);
		PolicyAccessEnforcement.deletePolicy(suiteData, requestHeader, policyDataMap);
		
		expectedMsg="[ALERT] "+testUser+ " attempted to "+userAction.toLowerCase()+" content:"+fileName.toLowerCase()+" violating policy:"+policyName;
		Reporter.log("expectedMsg: "+expectedMsg, true);
		data.clear();
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		data.put("policy_type", "FileTransfer");
		data.put("policy_action", "ALERT");
		data.put("action_taken", "block,email,");
		data.put("_PolicyViolated", policyName);
		
		validationResult = validateLogsFields(fetchElasticSearchLogsUniversal(), data);
		if (verifyBlocked) {
			assertTrue(validationResult, "Investigate log for Blocking " + userAction + " is NOT available or Activity is NOT Blocked!!");
		} else {
			assertFalse(validationResult, "Investigate log for Blocking " + userAction + " is available or Activity is Blocked!!");
		}
		
	}

	@DataProvider
	public Object[][] FileTransferUpDownRepeat() {
		return new Object[][]{
				//Test User, Test User Group, policyUser, policyGroup, verifyBlocked, tcId
				{"testuser1", "GroupA", "Upload", "admin@gatewayo365beatle.com", "GroupA", true, "C03000"},
				{"testuser1", "GroupA", "Download", "admin@gatewayo365beatle.com", "GroupA", true, "C03200"},
		};
	}
}
