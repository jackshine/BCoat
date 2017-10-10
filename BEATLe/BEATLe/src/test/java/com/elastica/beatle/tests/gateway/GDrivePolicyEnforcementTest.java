package com.elastica.beatle.tests.gateway;

import org.testng.annotations.Test;
import org.testng.annotations.DataProvider;

import static org.testng.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.testng.Assert;
import org.testng.Reporter;
import com.elastica.beatle.gateway.CommonConfiguration;
import com.elastica.beatle.gateway.GatewayTestConstants;
import com.elastica.beatle.gateway.LogValidator;
import com.elastica.beatle.gateway.PolicyAccessEnforcement;
import com.elastica.beatle.gateway.dto.GWForensicSearchResults;


/*******************Author**************
 * 
 * @author Afjal
 * Every test has an id which is inherited from Elastica-ADB
 */

public class GDrivePolicyEnforcementTest extends CommonConfiguration {

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
	
	
	/********************************************
	 * 
	 * @param policyName
	 * @param saasApps
	 * @param testUserName
	 * @param notifyEmailId
	 * @param objectAccess
	 * @param activityAccess
	 * @param activityType
	 * @param objectType
	 * @param severity
	 * @throws Exception
	 */

	
	@Test(groups ={"TEST"},dataProvider = "_policyAccessEnabledSession")
	public void verifyUserAccessEnforcementPolicyEnabledSession(String policyName, String saasApps,  String testUserName,  String notifyEmailId, String objectAccess, String activityAccess,   String activityType, String objectType, String severity, String logFile) throws Exception{
		String expectedMsg="[ALERT] "+testUserName+ " attempted Activity: "+activityAccess+" on Object type: "+objectAccess+"  using Platform";
		policyDataMap.clear();
		policyDataMap.put(GatewayTestConstants.POLICY_NAME, policyName);
		policyDataMap.put(GatewayTestConstants.APPLICATIONS, saasApps);
		policyDataMap.put(GatewayTestConstants.TARGET_USER, testUserName);
		policyDataMap.put(GatewayTestConstants.NOTIFY_EMAILID, notifyEmailId+"@"+suiteData.getTenantDomainName());
		policyDataMap.put(GatewayTestConstants.OBJECT_ACCESS, objectAccess);
		policyDataMap.put(GatewayTestConstants.ACTIVITY_ACCESS, activityAccess);
		PolicyAccessEnforcement.accessEnforcementPolicyCreateEnable(suiteData, requestHeader, policyDataMap);
		replayLogs(logFile);
		PolicyAccessEnforcement.deletePolicy(suiteData, requestHeader, policyDataMap);
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		data.put("policy_type", "AccessEnforcement");
		data.put("policy_action", "ALERT");
		data.put("action_taken", "block,");
		data.put("_PolicyViolated", policyName);
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), "Logs does not match" ); 
		
	
	}
	
	@DataProvider
	public Object[][] _policyAccessEnabledSession() {
		return new Object[][]{
			//Policy Name                 App     Test User   admin   Object Access Activity Access      Activity Type
			{ "ACCESS_ENFORCE_SESSION1",  "Google Drive",   "testuser4@gatewaybeatle.com",  "admin",   "Session",    "Login",              "Policy Violation",  "Session", "critical", "Google Drive,Session_2,Login.log"},
			
		};
	}
	
	/********************************************
	 * 
	 * @param policyName
	 * @param saasApps
	 * @param testUserName
	 * @param notifyEmailId
	 * @param objectAccess
	 * @param activityAccess
	 * @param activityType
	 * @param objectType
	 * @param severity
	 * @throws Exception
	 */
	
	@Test(groups ={"TEST", "ACCESS_ENFORCEMENT_FILE_POLICY"},dataProvider = "_policyAccessEnabledFile")
	public void verifyGDUserAccessEnforcementPolicyForFile(String policyName, String saasApps,  String testUserName,  String notifyEmailId, String objectAccess, String activityAccess,   String activityType, String fileName, String severity, String logFile) throws Exception{
		Reporter.log("Validate User Upload the file/folder to folder with Policy Enabled", true);
		testUserName=testUserName+"@"+suiteData.getTenantDomainName();
		String expectedMsg="[ALERT] "+testUserName+ " attempted Activity: "+activityAccess+" on Object type: "+objectAccess+" name: "+fileName+" using Platform";
		policyDataMap.clear();
		policyDataMap.put(GatewayTestConstants.POLICY_NAME, policyName);
		policyDataMap.put(GatewayTestConstants.APPLICATIONS, saasApps);
		policyDataMap.put(GatewayTestConstants.TARGET_USER, testUserName);
		policyDataMap.put(GatewayTestConstants.NOTIFY_EMAILID, notifyEmailId+"@"+suiteData.getTenantDomainName());
		policyDataMap.put(GatewayTestConstants.OBJECT_ACCESS, objectAccess);
		policyDataMap.put(GatewayTestConstants.ACTIVITY_ACCESS, activityAccess);
		PolicyAccessEnforcement.accessEnforcementPolicyCreateEnable(suiteData, requestHeader, policyDataMap);
		replayLogs(logFile);
		PolicyAccessEnforcement.deletePolicy(suiteData, requestHeader, policyDataMap);
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		data.put("Activity_type", "Policy Violation");
		data.put("policy_type", "AccessEnforcement");
		data.put("policy_action", "ALERT");
		data.put("action_taken", "block,");
		data.put("_PolicyViolated", policyName);
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal("Policy Violation", "critical"), data)), "Logs does not match" ); 
		
	}
	
	@DataProvider
	public Object[][] _policyAccessEnabledFile() {
		return new Object[][]{
			//Policy Name                 App     Test User   admin   Object Access Activity Access      Activity Type
		
			{ "ACCESS_ENFORCE_FILE1",   "Google Drive",   "testuser4",  "admin",   "File",  "Upload",   "Policy Violation",   "", "critical", "Google Drive,Admin_2,Upload_GWFolder_root_zip.log" },

		};
	}
	
	@Test(groups ={"TEST", "ACCESS_ENFORCEMENT_FILE_POLICY"},dataProvider = "_policyAccessEnabledFileFolder")
	public void verifyGDUserAccessEnforcementPolicyForFileFolder(String policyName, String saasApps,  String testUserName,  String notifyEmailId, String objectAccess, String activityAccess,   String activityType, String fileName, String severity, String logFile) throws Exception{
		Reporter.log("Validate User Upload the file/folder to folder with Policy Enabled", true);
		testUserName=testUserName+"@"+suiteData.getTenantDomainName();
		String expectedMsg="[ALERT]  attempted Activity: "+activityAccess+" on Object type: "+objectAccess+" name: "+fileName+" using Platform";
		policyDataMap.clear();
		policyDataMap.put(GatewayTestConstants.POLICY_NAME, policyName);
		policyDataMap.put(GatewayTestConstants.APPLICATIONS, saasApps);
		policyDataMap.put(GatewayTestConstants.TARGET_USER, testUserName);
		policyDataMap.put(GatewayTestConstants.NOTIFY_EMAILID, notifyEmailId+"@"+suiteData.getTenantDomainName());
		policyDataMap.put(GatewayTestConstants.OBJECT_ACCESS, objectAccess);
		policyDataMap.put(GatewayTestConstants.ACTIVITY_ACCESS, activityAccess);
		PolicyAccessEnforcement.accessEnforcementPolicyCreateEnable(suiteData, requestHeader, policyDataMap);
		replayLogs(logFile);
		PolicyAccessEnforcement.deletePolicy(suiteData, requestHeader, policyDataMap);
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		data.put("Activity_type", "Policy Violation");
		data.put("policy_type", "AccessEnforcement");
		data.put("policy_action", "ALERT");
		data.put("action_taken", "block,");
		data.put("_PolicyViolated", policyName);
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal("Policy Violation", "critical"), data)), "Logs does not match" ); 
		
	}
	
	@DataProvider
	public Object[][] _policyAccessEnabledFileFolder() {
		return new Object[][]{
			//Policy Name                 App     Test User   admin   Object Access Activity Access      Activity Type
		
			{ "ACCESS_ENFORCE_FILE_FOLDER1",   "Google Drive",   "testuser4",  "admin",   "File/Folder",  "Move",           "Policy Violation",   "textdocument", "critical", "File,Admin_2,Move_TextDocument_root_GWFolder.log" },
			{ "ACCESS_ENFORCE_FILE_FOLDER2",   "Google Drive",   "testuser4",  "admin",   "File/Folder",  "Rename",         "Policy Violation",   "textdoc", "critical", "File,Admin_2,Rename_TextDocument_TestDoc.log" },
			{ "ACCESS_ENFORCE_FILE_FOLDER3",   "Google Drive",   "testuser4",  "admin",   "File/Folder",  "Copy",           "Policy Violation",   "textdoc", "critical", "File,Admin_2,Copy_GWFolder_root.log" },
			{ "ACCESS_ENFORCE_FILE_FOLDER5",   "Google Drive",   "testuser4",  "admin",   "File/Folder",  "Delete Forever", "Policy Violation",   "arfolder", "critical", "Google Drive,Folder_2,Detete_Forever.log" },
			{ "ACCESS_ENFORCE_FILE_FOLDER6",   "Google Drive",   "testuser4",  "admin",   "File/Folder",  "Restore",        "Policy Violation",   "arfolder", "critical", "Google Drive,Folder_2,Restore.log" },
			{ "ACCESS_ENFORCE_FILE2",          "Google Drive",   "testuser4",  "admin",   "File",         "Download",       "Policy Violation",   "test.doc", "critical", "File,Admin_2,Download_Test_doc.log" },
		};
	}
	
	@Test(groups ={"TEST", "ACCESS_ENFORCEMENT_FILE_POLICY"},dataProvider = "_policyAccessEnabledFileFolder_1")
	public void verifyGDUserAccessEnforcementPolicyForFileFolder_1(String policyName, String saasApps,  String testUserName,  String notifyEmailId, String objectAccess, String activityAccess,   String activityType, String fileName, String severity, String logFile) throws Exception{
		Reporter.log("Validate User Upload the file/folder to folder with Policy Enabled", true);
		testUserName=testUserName+"@"+suiteData.getTenantDomainName();
		String expectedMsg="[ALERT] "+testUserName+ " attempted Activity: "+activityAccess+" on Object type: "+objectAccess+" name: "+fileName+" using Platform";
		policyDataMap.clear();
		policyDataMap.put(GatewayTestConstants.POLICY_NAME, policyName);
		policyDataMap.put(GatewayTestConstants.APPLICATIONS, saasApps);
		policyDataMap.put(GatewayTestConstants.TARGET_USER, testUserName);
		policyDataMap.put(GatewayTestConstants.NOTIFY_EMAILID, notifyEmailId+"@"+suiteData.getTenantDomainName());
		policyDataMap.put(GatewayTestConstants.OBJECT_ACCESS, objectAccess);
		policyDataMap.put(GatewayTestConstants.ACTIVITY_ACCESS, activityAccess);
		PolicyAccessEnforcement.accessEnforcementPolicyCreateEnable(suiteData, requestHeader, policyDataMap);
		replayLogs(logFile);
		PolicyAccessEnforcement.deletePolicy(suiteData, requestHeader, policyDataMap);
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		data.put("Activity_type", "Policy Violation");
		data.put("policy_type", "AccessEnforcement");
		data.put("policy_action", "ALERT");
		data.put("action_taken", "block,");
		data.put("_PolicyViolated", policyName);
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal("Policy Violation", "critical"), data)), "Logs does not match" ); 
		
	}
	
	@DataProvider
	public Object[][] _policyAccessEnabledFileFolder_1() {
		return new Object[][]{
			//Policy Name                 App     Test User   admin   Object Access Activity Access      Activity Type
		
			{ "ACCESS_ENFORCE_FILE_FOLDER4",   "Google Drive",   "testuser4",  "admin",   "File/Folder",  "Remove",         "Policy Violation",   "textdoc", "critical", "File,Admin_2,Remove_TextDocument.log" },
		};
	}
	
	
	@Test(groups ={"TEST", "ACCESS_ENFORCEMENT_FILE_POLICY"},dataProvider = "_policyAccessEnabledFileFolder_V3")
	public void verifyGDUserAccessEnforcementPolicyForFileFolder_V3(String policyName, String saasApps,  String testUserName,  String notifyEmailId, String objectAccess, String activityAccess,   String activityType, String fileName, String severity, String logFile) throws Exception{
		Reporter.log("Validate User Upload the file/folder to folder with Policy Enabled", true);
		testUserName=testUserName+"@"+suiteData.getTenantDomainName();
		String expectedMsg="[ALERT] "+testUserName+ " attempted Activity: "+activityAccess+" on Object type: "+objectAccess+" name: "+fileName+" using Platform";
		policyDataMap.clear();
		policyDataMap.put(GatewayTestConstants.POLICY_NAME, policyName);
		policyDataMap.put(GatewayTestConstants.APPLICATIONS, saasApps);
		policyDataMap.put(GatewayTestConstants.TARGET_USER, testUserName);
		policyDataMap.put(GatewayTestConstants.NOTIFY_EMAILID, notifyEmailId+"@"+suiteData.getTenantDomainName());
		policyDataMap.put(GatewayTestConstants.OBJECT_ACCESS, objectAccess);
		policyDataMap.put(GatewayTestConstants.ACTIVITY_ACCESS, activityAccess);
		PolicyAccessEnforcement.accessEnforcementPolicyCreateEnable(suiteData, requestHeader, policyDataMap);
		replayLogsEPDV3(logFile);
		PolicyAccessEnforcement.deletePolicy(suiteData, requestHeader, policyDataMap);
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		data.put("Activity_type", "Policy Violation");
		data.put("policy_type", "AccessEnforcement");
		data.put("policy_action", "ALERT");
		data.put("action_taken", "block,");
		data.put("_PolicyViolated", policyName);
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal("Policy Violation", "critical"), data)), "Logs does not match" ); 
		
	}
	
	@DataProvider
	public Object[][] _policyAccessEnabledFileFolder_V3() {
		return new Object[][]{
			//Policy Name                 App     Test User   admin   Object Access Activity Access      Activity Type
		
			{ "ACCESS_ENFORCE_FILE_FOLDER7",   "Google Drive",   "testuser4",  "admin",   "File/Folder",  "Share",                    "Policy Violation",   "", "critical", "FolderGD,Share_Folder_Mail_Edit.log" },
			{ "ACCESS_ENFORCE_FILE_FOLDER8",   "Google Drive",   "testuser4",  "admin",   "File/Folder",  "Unshare",                  "Policy Violation",   "", "critical", "FolderGD,Unshare_Folder.log" },
			{ "ACCESS_ENFORCE_FILE_FOLDER9",   "Google Drive",   "testuser4",  "admin",   "File/Folder",  "Update collaborator role", "Policy Violation",   "", "critical", "FolderGD,Update_Folder_Collaborator_Edit.log" },
			{ "ACCESS_ENFORCE_FILE_FOLDER10",  "Google Drive",   "testuser4",  "admin",   "File/Folder",  "Update Link Sharing",      "Policy Violation",   "", "critical", "FolderGD,Update_Folder_Link_Share_Edit.log" },
		};
	}
	
	@Test(groups ={"TEST", "ACCESS_ENFORCEMENT_FILE_POLICY"},dataProvider = "_policyAccessEnabledFolder")
	public void verifyGDUserAccessEnforcementPolicyForFolder(String policyName, String saasApps,  String testUserName,  String notifyEmailId, String objectAccess, String activityAccess,   String activityType, String fileName, String severity, String logFile) throws Exception{
		Reporter.log("Validate User Upload the file/folder to folder with Policy Enabled", true);
		testUserName=testUserName+"@"+suiteData.getTenantDomainName();
		String expectedMsg="[ALERT] "+testUserName+ " attempted Activity: "+activityAccess+" on Object type: "+objectAccess+" name: "+fileName+" using Platform";
		policyDataMap.clear();
		policyDataMap.put(GatewayTestConstants.POLICY_NAME, policyName);
		policyDataMap.put(GatewayTestConstants.APPLICATIONS, saasApps);
		policyDataMap.put(GatewayTestConstants.TARGET_USER, testUserName);
		policyDataMap.put(GatewayTestConstants.NOTIFY_EMAILID, notifyEmailId+"@"+suiteData.getTenantDomainName());
		policyDataMap.put(GatewayTestConstants.OBJECT_ACCESS, objectAccess);
		policyDataMap.put(GatewayTestConstants.ACTIVITY_ACCESS, activityAccess);
		PolicyAccessEnforcement.accessEnforcementPolicyCreateEnable(suiteData, requestHeader, policyDataMap);
		replayLogs(logFile);
		PolicyAccessEnforcement.deletePolicy(suiteData, requestHeader, policyDataMap);
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		data.put("Activity_type", "Policy Violation");
		data.put("policy_type", "AccessEnforcement");
		data.put("policy_action", "ALERT");
		data.put("action_taken", "block,");
		data.put("_PolicyViolated", policyName);
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal("Policy Violation", "critical"), data)), "Logs does not match" ); 
		
	}
	
	@DataProvider
	public Object[][] _policyAccessEnabledFolder() {
		return new Object[][]{
			//Policy Name                 App     Test User   admin   Object Access Activity Access      Activity Type
		
			{ "ACCESS_ENFORCE_FOLDER1",   "Google Drive",   "testuser4",  "admin",   "File/Folder",  "Download", "Policy Violation",   "", "critical", "Google Drive,Folder_2,Download1.log" },
	
		};
	}
	
	@Test(groups ={"TEST", "ACCESS_ENFORCEMENT_FILE_POLICY"},dataProvider = "_policyAccessEnabledDocument")
	public void verifyGDUserAccessEnforcementPolicyForDocument(String policyName, String saasApps,  String testUserName,  String notifyEmailId, String objectAccess, String activityAccess,   String activityType, String fileName, String severity, String logFile) throws Exception{
		Reporter.log("Validate User Upload the file/folder to folder with Policy Enabled", true);
		testUserName=testUserName+"@"+suiteData.getTenantDomainName();
		String expectedMsg="[ALERT] "+testUserName+ " attempted Activity: "+activityAccess+" on Object type: "+objectAccess+" name: "+fileName+" using Platform";
		policyDataMap.clear();
		policyDataMap.put(GatewayTestConstants.POLICY_NAME, policyName);
		policyDataMap.put(GatewayTestConstants.APPLICATIONS, saasApps);
		policyDataMap.put(GatewayTestConstants.TARGET_USER, testUserName);
		policyDataMap.put(GatewayTestConstants.NOTIFY_EMAILID, notifyEmailId+"@"+suiteData.getTenantDomainName());
		policyDataMap.put(GatewayTestConstants.OBJECT_ACCESS, objectAccess);
		policyDataMap.put(GatewayTestConstants.ACTIVITY_ACCESS, activityAccess);
		PolicyAccessEnforcement.accessEnforcementPolicyCreateEnable(suiteData, requestHeader, policyDataMap);
		replayLogs(logFile);
		PolicyAccessEnforcement.deletePolicy(suiteData, requestHeader, policyDataMap);
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		data.put("Activity_type", "Policy Violation");
		data.put("policy_type", "AccessEnforcement");
		data.put("policy_action", "ALERT");
		data.put("action_taken", "block,");
		data.put("_PolicyViolated", policyName);
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal("Policy Violation", "critical"), data)), "Logs does not match" ); 
		
	}
	
	@DataProvider
	public Object[][] _policyAccessEnabledDocument() {
		return new Object[][]{
			//Policy Name                 App     Test User   admin   Object Access Activity Access      Activity Type
		
			{ "ACCESS_ENFORCE_DOCUMENT",   "Google Drive",   "testuser4",  "admin",   "Document",  "Create", "Policy Violation",   "untitled document", "critical", "File,Admin_2,Create_Document_root.log" },
	
		};
	}
	
	@Test(groups ={"TEST", "ACCESS_ENFORCEMENT_FILE_POLICY"},dataProvider = "_policyAccessEnabledExcel")
	public void verifyGDUserAccessEnforcementPolicyForExcel(String policyName, String saasApps,  String testUserName,  String notifyEmailId, String objectAccess, String activityAccess,   String activityType, String fileName, String severity, String logFile) throws Exception{
		Reporter.log("Validate User Upload the file/folder to folder with Policy Enabled", true);
		testUserName=testUserName+"@"+suiteData.getTenantDomainName();
		String expectedMsg="[ALERT] "+testUserName+ " attempted Activity: "+activityAccess+" on Object type: "+objectAccess+" name: "+fileName+" using Platform";
		policyDataMap.clear();
		policyDataMap.put(GatewayTestConstants.POLICY_NAME, policyName);
		policyDataMap.put(GatewayTestConstants.APPLICATIONS, saasApps);
		policyDataMap.put(GatewayTestConstants.TARGET_USER, testUserName);
		policyDataMap.put(GatewayTestConstants.NOTIFY_EMAILID, notifyEmailId+"@"+suiteData.getTenantDomainName());
		policyDataMap.put(GatewayTestConstants.OBJECT_ACCESS, objectAccess);
		policyDataMap.put(GatewayTestConstants.ACTIVITY_ACCESS, activityAccess);
		PolicyAccessEnforcement.accessEnforcementPolicyCreateEnable(suiteData, requestHeader, policyDataMap);
		replayLogs(logFile);
		PolicyAccessEnforcement.deletePolicy(suiteData, requestHeader, policyDataMap);
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		data.put("Activity_type", "Policy Violation");
		data.put("policy_type", "AccessEnforcement");
		data.put("policy_action", "ALERT");
		data.put("action_taken", "block,");
		data.put("_PolicyViolated", policyName);
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal("Policy Violation", "critical"), data)), "Logs does not match" ); 
		
	}
	
	@DataProvider
	public Object[][] _policyAccessEnabledExcel() {
		return new Object[][]{
			//Policy Name                 App     Test User   admin   Object Access Activity Access      Activity Type
		
			{ "ACCESS_ENFORCE_EXCEL",   "Google Drive",   "testuser4",  "admin",   "Spreadsheet",  "Create", "Policy Violation",   "untitled spreadsheet", "critical", "File,Admin_2,Create_ExcelBook_root.log" },
	
		};
	}
	
	@Test(groups ={"TEST", "ACCESS_ENFORCEMENT_FILE_POLICY"},dataProvider = "_policyAccessEnabledPresentation")
	public void verifyGDUserAccessEnforcementPolicyForPresentation(String policyName, String saasApps,  String testUserName,  String notifyEmailId, String objectAccess, String activityAccess,   String activityType, String fileName, String severity, String logFile) throws Exception{
		Reporter.log("Validate User Upload the file/folder to folder with Policy Enabled", true);
		testUserName=testUserName+"@"+suiteData.getTenantDomainName();
		String expectedMsg="[ALERT] "+testUserName+ " attempted Activity: "+activityAccess+" on Object type: "+objectAccess+" name: "+fileName+" using Platform";
		policyDataMap.clear();
		policyDataMap.put(GatewayTestConstants.POLICY_NAME, policyName);
		policyDataMap.put(GatewayTestConstants.APPLICATIONS, saasApps);
		policyDataMap.put(GatewayTestConstants.TARGET_USER, testUserName);
		policyDataMap.put(GatewayTestConstants.NOTIFY_EMAILID, notifyEmailId+"@"+suiteData.getTenantDomainName());
		policyDataMap.put(GatewayTestConstants.OBJECT_ACCESS, objectAccess);
		policyDataMap.put(GatewayTestConstants.ACTIVITY_ACCESS, activityAccess);
		PolicyAccessEnforcement.accessEnforcementPolicyCreateEnable(suiteData, requestHeader, policyDataMap);
		replayLogs(logFile);
		PolicyAccessEnforcement.deletePolicy(suiteData, requestHeader, policyDataMap);
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		data.put("Activity_type", "Policy Violation");
		data.put("policy_type", "AccessEnforcement");
		data.put("policy_action", "ALERT");
		data.put("action_taken", "block,");
		data.put("_PolicyViolated", policyName);
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal("Policy Violation", "critical"), data)), "Logs does not match" ); 
		
	}
	
	@DataProvider
	public Object[][] _policyAccessEnabledPresentation() {
		return new Object[][]{
			//Policy Name                 App     Test User   admin   Object Access Activity Access      Activity Type
		
			{ "ACCESS_ENFORCE_POWER_POINT",   "Google Drive",   "testuser4",  "admin",   "Presentation",  "Create", "Policy Violation",   "untitled presentation", "critical", "File,Admin_2,Create_Slides_root.log" },
	
		};
	}
	
	@Test(groups ={"TEST", "ACCESS_ENFORCEMENT_FILE_POLICY"},dataProvider = "_policyAccessEnabledApp")
	public void verifyGDUserAccessEnforcementPolicyForApp(String policyName, String saasApps,  String testUserName,  String notifyEmailId, String objectAccess, String activityAccess,   String activityType, String fileName, String severity, String logFile) throws Exception{
		Reporter.log("Validate User Upload the file/folder to folder with Policy Enabled", true);
		testUserName=testUserName+"@"+suiteData.getTenantDomainName();
		String expectedMsg="[ALERT] "+testUserName+ " attempted Activity: "+activityAccess+" on Object type: "+objectAccess+" name: "+fileName+" using Platform";
		policyDataMap.clear();
		policyDataMap.put(GatewayTestConstants.POLICY_NAME, policyName);
		policyDataMap.put(GatewayTestConstants.APPLICATIONS, saasApps);
		policyDataMap.put(GatewayTestConstants.TARGET_USER, testUserName);
		policyDataMap.put(GatewayTestConstants.NOTIFY_EMAILID, notifyEmailId+"@"+suiteData.getTenantDomainName());
		policyDataMap.put(GatewayTestConstants.OBJECT_ACCESS, objectAccess);
		policyDataMap.put(GatewayTestConstants.ACTIVITY_ACCESS, activityAccess);
		PolicyAccessEnforcement.accessEnforcementPolicyCreateEnable(suiteData, requestHeader, policyDataMap);
		replayLogs(logFile);
		PolicyAccessEnforcement.deletePolicy(suiteData, requestHeader, policyDataMap);
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		data.put("Activity_type", "Policy Violation");
		data.put("policy_type", "AccessEnforcement");
		data.put("policy_action", "ALERT");
		data.put("action_taken", "block,");
		data.put("_PolicyViolated", policyName);
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal("Policy Violation", "critical"), data)), "Logs does not match" ); 
		
	}
	
	@DataProvider
	public Object[][] _policyAccessEnabledApp() {
		return new Object[][]{
			//Policy Name                 App     Test User   admin   Object Access Activity Access      Activity Type
		
			{ "ACCESS_ENFORCE_CONNECT_APP",   "Google Drive",   "testuser4",  "admin",   "Application",  "Connect", "Policy Violation",   "smart amp", "critical", "Google Drive,Folder_2,Open_with_App.log" },
	
		};
	}
}
