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

public class DropboxPolicyEnforcementTest extends CommonConfiguration {

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
	public void verifyUserAccessEnforcementPolicyEnabledSession(String policyName,  String testUserName,  String notifyEmailId, String objectAccess, String activityAccess,   String activityType, String objectType, String severity, String logFile) throws Exception{
		String expectedMsg="[ALERT] "+testUserName+ " attempted Activity: "+activityAccess+" on Object type: "+objectAccess+"  using Platform";
		policyDataMap.clear();
		policyDataMap.put(GatewayTestConstants.POLICY_NAME, policyName);
		policyDataMap.put(GatewayTestConstants.APPLICATIONS, "Dropbox");
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
			{ "ACCESS_ENFORCE_SESSION1",   "admin@gatewaybeatle.com",  "admin",   "Session",    "Login",         "Policy Violation",  "Session", "critical", "Session,Dropbox_2,Login.log"},
			{ "ACCESS_ENFORCE_SESSION2",   "admin@gatewaybeatle.com",  "admin",   "Session",    "InvalidLogin",  "Policy Violation",  "Session", "critical", "Session,Dropbox_2,Invalid_Login.log"},
			
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
	
	@Test(groups ={"TEST", "ACCESS_ENFORCEMENT_FILE_POLICY"},dataProvider = "_dbPolicyAccessEnabledFile")
	public void verifyDropboxUserAccessEnforcementPolicyForFile(String policyName,  String testUserName,  String notifyEmailId, String objectAccess, String activityAccess,   String activityType, String fileName, String severity, String logFile) throws Exception{
		Reporter.log("Validate User Upload the file/folder to folder with Policy Enabled", true);
		//testUserName=testUserName+"@"+suiteData.getTenantDomainName();
		String expectedMsg="[ALERT] "+testUserName+ " attempted Activity: "+activityAccess+" on Object type: "+objectAccess+" name: "+fileName+" using Platform";
		policyDataMap.clear();
		policyDataMap.put(GatewayTestConstants.POLICY_NAME, policyName);
		policyDataMap.put(GatewayTestConstants.APPLICATIONS, "Dropbox");
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
	public Object[][] _dbPolicyAccessEnabledFile() {
		return new Object[][]{
			//Policy Name                 App     Test User   admin   Object Access Activity Access      Activity Type
		
			{ "ACCESS_ENFORCE_FILE1",  "admin@gatewaybeatle.com",  "admin",   "File",  "Upload",   "Policy Violation",   "test.doc", "critical", "File,Dropbox_2,Upload_Test_doc.log" },
			{ "ACCESS_ENFORCE_FILE2",  "admin@gatewaybeatle.com",  "admin",   "File",  "Download", "Policy Violation",   "gwfolder/test1.doc", "critical", "File,Dropbox_2,Download_Test1_doc.log" },
			

		};
	}
	
	@Test(groups ={"TEST", "ACCESS_ENFORCEMENT_FILE_POLICY"},dataProvider = "_dbPolicyAccessEnabledFileFolder")
	public void verifyDropboxUserAccessEnforcementPolicyForFileFolder(String policyName, String testUserName,  String notifyEmailId, String objectAccess, String activityAccess,   String activityType, String fileName, String severity, String logFile) throws Exception{
		Reporter.log("Validate User Upload the file/folder to folder with Policy Enabled", true);
//		testUserName=testUserName+"@"+suiteData.getTenantDomainName();
		String expectedMsg="[ALERT] "+testUserName+ " attempted Activity: "+activityAccess+" on Object type: "+objectAccess+" name: "+fileName+" using Platform";
		policyDataMap.clear();
		policyDataMap.put(GatewayTestConstants.POLICY_NAME, policyName);
		policyDataMap.put(GatewayTestConstants.APPLICATIONS, "Dropbox");
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
	public Object[][] _dbPolicyAccessEnabledFileFolder() {
		return new Object[][]{
			//Policy Name                 App     Test User   admin   Object Access Activity Access      Activity Type
		
			{ "ACCESS_ENFORCE_FILE_FOLDER1",  "admin@gatewaybeatle.com",  "admin",   "File/Folder",  "Move",   "Policy Violation",  "/test1.doc",  "critical", "File,Dropbox_2,Move_Test_doc.log" },
			{ "ACCESS_ENFORCE_FILE_FOLDER2",  "admin@gatewaybeatle.com",  "admin",   "File/Folder",  "Rename", "Policy Violation",  "test1.doc",  "critical", "File,Dropbox_2,Rename_Test_doc.log" },
			{ "ACCESS_ENFORCE_FILE_FOLDER3",  "admin@gatewaybeatle.com",  "admin",   "File/Folder",  "Copy",   "Policy Violation",  "/gwfolder/test1.doc",  "critical", "File,Dropbox_2,Copy_Test_doc.log" },
			{ "ACCESS_ENFORCE_FILE_FOLDER4",  "admin@gatewaybeatle.com",  "admin",   "File/Folder",  "Delete", "Policy Violation",  "gwfolder/test1.doc", "critical", "File,Dropbox_2,Delete_Test1_doc.log" },
			{ "ACCESS_ENFORCE_FILE_FOLDER5",  "admin@gatewaybeatle.com",  "admin",   "File/Folder",  "Share",  "Policy Violation",   "test.doc", "critical", "File,Dropbox_2,Share_File_doc.log" },
			
		};
	}
	
	@Test(groups ={"TEST", "ACCESS_ENFORCEMENT_FILE_POLICY"},dataProvider = "_dbPolicyAccessEnabledFolder")
	public void verifyDropboxUserAccessEnforcementPolicyForFolder(String policyName,  String testUserName,  String notifyEmailId, String objectAccess, String activityAccess,   String activityType, String fileName, String severity, String logFile) throws Exception{
		Reporter.log("Validate User Upload the file/folder to folder with Policy Enabled", true);
//		testUserName=testUserName+"@"+suiteData.getTenantDomainName();
		String expectedMsg="[ALERT] "+testUserName+ " attempted Activity: "+activityAccess+" on Object type: "+objectAccess+" name: "+fileName+" using Platform";
		policyDataMap.clear();
		policyDataMap.put(GatewayTestConstants.POLICY_NAME, policyName);
		policyDataMap.put(GatewayTestConstants.APPLICATIONS, "Dropbox");
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
	public Object[][] _dbPolicyAccessEnabledFolder() {
		return new Object[][]{
			//Policy Name                 App     Test User   admin   Object Access Activity Access      Activity Type
		
			{ "ACCESS_ENFORCE_FOLDER1",   "admin@gatewaybeatle.com",  "admin",  "Folder",  "Create",   "Policy Violation",   "gwauto", "critical", "Folder,Dropbox_2,Create.log" },
			{ "ACCESS_ENFORCE_FOLDER2",   "admin@gatewaybeatle.com",  "admin",  "Folder",  "Share",    "Policy Violation",   "", "critical", "Folder,Dropbox_2,Share.log" },
			{ "ACCESS_ENFORCE_FOLDER3",   "admin@gatewaybeatle.com",  "admin",  "Folder",  "Share",    "Policy Violation",   "", "critical", "Folder,Dropbox_2,Invite.log" },
			{ "ACCESS_ENFORCE_FOLDER4",   "admin@gatewaybeatle.com",  "admin",  "Folder",  "Unshare",  "Policy Violation",   "", "critical", "Folder,Dropbox_2,Uninvite.log" },
	
		};
	}
	
	
	@Test(groups ={"TEST", "ACCESS_ENFORCEMENT_FILE_POLICY"},dataProvider = "_dbPolicyAccessEnabledFileRequest")
	public void verifyDropboxUserAccessEnforcementPolicyForFileRequest(String policyName,  String testUserName,  String notifyEmailId, String objectAccess, String activityAccess,   String activityType, String fileName, String severity, String logFile) throws Exception{
		Reporter.log("Validate User Upload the file/folder to folder with Policy Enabled", true);
//		testUserName=testUserName+"@"+suiteData.getTenantDomainName();
		String expectedMsg="[ALERT] "+testUserName+ " attempted Activity: "+activityAccess+" on Object type: "+objectAccess+" name: "+fileName+" using Platform";
		policyDataMap.clear();
		policyDataMap.put(GatewayTestConstants.POLICY_NAME, policyName);
		policyDataMap.put(GatewayTestConstants.APPLICATIONS, "Dropbox");
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
	public Object[][] _dbPolicyAccessEnabledFileRequest() {
		return new Object[][]{
			//Policy Name                 App     Test User   admin   Object Access Activity Access      Activity Type
	
			{ "ACCESS_ENFORCE_FILE_REQUEST1",   "admin@gatewaybeatle.com",  "admin",  "File Request",  "Create",  "Policy Violation",   "birthday", "critical", "File_Request,Dropbox_2,Create.log" },
			{ "ACCESS_ENFORCE_FILE_REQUEST2",   "admin@gatewaybeatle.com",  "admin",  "File Request",  "Reopen",  "Policy Violation",   "birthday", "critical", "File_Request,Dropbox_2,Reopen.log" },
//			{ "ACCESS_ENFORCE_FILE_REQUEST3",   "admin@gatewaybeatle.com",  "admin",  "File Request",  "Send",    "Policy Violation",   "File requests/Birthday", "critical", "File_Request,Dropbox_2,Send.log" },
	
		};
	}
	
	
	@Test(groups ={"TEST", "ACCESS_ENFORCEMENT_FILE_POLICY"},dataProvider = "_dbPolicyAccessEnabledPhoto")
	public void verifyDropboxUserAccessEnforcementPolicyForPhoto(String policyName,  String testUserName,  String notifyEmailId, String objectAccess, String activityAccess,   String activityType, String fileName, String severity, String logFile) throws Exception{
		Reporter.log("Validate User Upload the file/folder to folder with Policy Enabled", true);
//		testUserName=testUserName+"@"+suiteData.getTenantDomainName();
		String expectedMsg="[ALERT] "+testUserName+ " attempted Activity: "+activityAccess+" on Object type: "+objectAccess+" name: "+fileName+" using Platform";
		policyDataMap.clear();
		policyDataMap.put(GatewayTestConstants.POLICY_NAME, policyName);
		policyDataMap.put(GatewayTestConstants.APPLICATIONS, "Dropbox");
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
	public Object[][] _dbPolicyAccessEnabledPhoto() {
		return new Object[][]{
			//Policy Name                 App     Test User   admin   Object Access Activity Access      Activity Type
	
			{ "ACCESS_ENFORCE_PHOTO1",   "admin@gatewaybeatle.com",  "admin",  "Photo",  "Share",  "Policy Violation",   "img_20151122_152756_hdr.jpg", "critical", "Album,Dropbox_2,Share.log" },
	
		};
	}
	
	
	@Test(groups ={"TEST", "ACCESS_ENFORCEMENT_FILE_POLICY"},dataProvider = "_dbPolicyAccessEnabledAlbum")
	public void verifyDropboxUserAccessEnforcementPolicyForAlbum(String policyName,  String testUserName,  String notifyEmailId, String objectAccess, String activityAccess,   String activityType, String fileName, String severity, String logFile) throws Exception{
		Reporter.log("Validate User Upload the file/folder to folder with Policy Enabled", true);
//		testUserName=testUserName+"@"+suiteData.getTenantDomainName();
		String expectedMsg="[ALERT] "+testUserName+ " attempted Activity: "+activityAccess+" on Object type: "+objectAccess+" name: "+fileName+" using Platform";
		policyDataMap.clear();
		policyDataMap.put(GatewayTestConstants.POLICY_NAME, policyName);
		policyDataMap.put(GatewayTestConstants.APPLICATIONS, "Dropbox");
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
	public Object[][] _dbPolicyAccessEnabledAlbum() {
		return new Object[][]{
			//Policy Name                 App     Test User   admin   Object Access Activity Access      Activity Type
	
			{ "ACCESS_ENFORCE_ALBUM1",   "admin@gatewaybeatle.com",  "admin",  "Album",  "Create",  "Policy Violation",   "gateway",     "critical", "Album,Dropbox_2,Create.log" },
			{ "ACCESS_ENFORCE_ALBUM2",   "admin@gatewaybeatle.com",  "admin",  "Album",  "Delete",  "Policy Violation",   "gwt",         "critical", "Album,Dropbox_2,Delete.log" },
			{ "ACCESS_ENFORCE_ALBUM3",   "admin@gatewaybeatle.com",  "admin",  "Album",  "Rename",  "Policy Violation",   "gatewayauto", "critical", "Album,Dropbox_2,Rename.log" },
	
		};
	}
	
}
