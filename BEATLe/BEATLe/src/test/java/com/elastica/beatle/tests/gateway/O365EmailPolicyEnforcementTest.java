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
 * @author usman
 * Every test has an id which is inherited from Elastica-ADB
 */

public class O365EmailPolicyEnforcementTest extends CommonConfiguration {

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
			{ "ACCESS_ENFORCE_SESSION1",  "Box",   "user1",  "admin",   "SESSION",    "Login",        "Policy Violation",  "File", "critical", "OneDrive,Login_Session.log"},
//		    { "ACCESS_ENFORCE_SESSION2",  "Box",   "user1",  "admin",   "SESSION",    "Logout",       "Policy Violation",  "File", "critical" },
			
		};
	}
	
	
	@Test(groups ={"TEST", "ACCESS_ENFORCEMENT_FILE_POLICY"},dataProvider = "_policyAccessEmailFolderEnabled")
	public void verifyUserAccessEnforcementPolicyEmailForFolderOperations(String policyName, String saasApps,  String testUserName,  String notifyEmailId, String objectAccess, String activityAccess,   String activityType, String fileName, String severity, String logFile) throws Exception{
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
		data.put("policy_type", "AccessEnforcement");
		data.put("policy_action", "ALERT");
		data.put("action_taken", "block,");
		data.put("_PolicyViolated", policyName);
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), "Logs does not match" ); 
		
	}
	
	@DataProvider
	public Object[][] _policyAccessEmailFolderEnabled() {
		return new Object[][]{
			//Policy Name                 App     Test User   admin   Object Access Activity Access      Activity Type
		
			{ "ACCESS_ENFORCE_FOLDER1",   "Office 365",   "testuser1",  "admin",   "Folder",    	"Create",   "Policy Violation",   "sfolder", "critical",  "Folder,Office 365,Create.log" },
			{ "ACCESS_ENFORCE_FOLDER2",   "Office 365",   "testuser1",  "admin",   "Folder",    	"Delete",   "Policy Violation",   "sfolder", "critical",  "Folder,Office 365,Delete.log" },
			{ "ACCESS_ENFORCE_FOLDER3",   "Office 365",   "testuser1",  "admin",   "Folder",    	"Move",     "Policy Violation",   "",         "critical", "SubFolder,Admin,Move.log" },
		
		};
	}
	
	
	
	@Test(groups ={"TEST", "ACCESS_ENFORCEMENT_FILE_POLICY"},dataProvider = "_policyAccessEmailFileEnabled")
	public void verifyUserAccessEnforcementPolicyForDocumentFolderOperations(String policyName, String saasApps,  String testUserName,  String notifyEmailId, String objectAccess, String activityAccess,   String activityType, String fileName, String severity, String logFile) throws Exception{
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
		data.put("policy_type", "AccessEnforcement");
		data.put("policy_action", "ALERT");
		data.put("action_taken", "block,");
		data.put("_PolicyViolated", policyName);
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), "Logs does not match" ); 
		
	}
	
	@DataProvider
	public Object[][] _policyAccessEmailFileEnabled() {
		return new Object[][]{
			//Policy Name                 App     Test User   admin   Object Access Activity Access      Activity Type
		
//			{ "ACCESS_ENFORCE_FILE1",   "Office 365",   "testuser1",  "admin",   "Document",    	"Upload",     "Policy Violation",   "test.doc",  "critical",  "Group_Files,admin,Upload.log" },
			{ "ACCESS_ENFORCE_FILE2",   "Office 365",   "testuser1",  "admin",   "Document",    	"Download",   "Policy Violation",   "test.doc",  "critical",  "Group_Files,admin,Download.log" },
		
		};
	}
	
	
	@Test(groups ={"TEST", "ACCESS_ENFORCEMENT_FILE_POLICY"},dataProvider = "_policyAccessEmailEnabled")
	public void verifyUserAccessEnforcementEmailPolicyEnabled(String policyName, String saasApps,  String testUserName,  String notifyEmailId, String objectAccess, String activityAccess,   String activityType, String objectType, String severity, String logFile) throws Exception{
		Reporter.log("Validate User Upload the file/folder to folder with Policy Enabled", true);
		testUserName=testUserName+"@"+suiteData.getTenantDomainName();
		String expectedMsg="[ALERT] "+testUserName+ " attempted Activity: "+activityAccess+" on Object type: "+objectAccess+" name: "+objectType+" using Platform";
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
		data.put("policy_type", "AccessEnforcement");
		data.put("policy_action", "ALERT");
		data.put("action_taken", "block,");
		data.put("_PolicyViolated", policyName);
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), "Logs does not match" ); 
		
	
	}
	
	@DataProvider
	public Object[][] _policyAccessEmailEnabled() {
		return new Object[][]{
			//Policy Name                	    App     Test User admin   Object Access 		Activity Access      Activity Type
			{ "ACCESS_ENFORCE_Email1",   "Office 365",   "testuser1",  "admin",   "Email",    	"Send",        "Policy Violation",   "data", "critical",  "Mail,Send_Mail_With_External_UserE.log" },
		
		};
	}
	
	
	
	@Test(groups ={"TEST", "ACCESS_ENFORCEMENT_FILE_POLICY"},dataProvider = "_policyAccessRecycleBinEnabled")
	public void verifyUserAccessEnforcementPolicyForRecycleBinOperations(String policyName, String saasApps,  String testUserName,  String notifyEmailId, String objectAccess, String activityAccess,   String activityType, String fileName, String severity, String logFile) throws Exception{
		Reporter.log("Validate User Upload the file/folder to folder with Policy Enabled", true);
		testUserName=testUserName+"@"+suiteData.getTenantDomainName();
		String expectedMsg="[ALERT] "+testUserName+ " attempted Activity: "+activityAccess+" on Object type: "+objectAccess+"  using Platform";
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
		data.put("policy_type", "AccessEnforcement");
		data.put("policy_action", "ALERT");
		data.put("action_taken", "block,");
		data.put("_PolicyViolated", policyName);
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), "Logs does not match" ); 
		
	}
	
	@DataProvider
	public Object[][] _policyAccessRecycleBinEnabled() {
		return new Object[][]{
			//Policy Name                 App     Test User   admin   Object Access Activity Access      Activity Type
		
			{ "ACCESS_ENFORCE_EMPTY_RECYCLE",   "Office 365",   "testuser1",  "admin",   "Recycle Bin",    	"Empty",   "Policy Violation",   "gwtest1", "critical",  "RecycleBin,Empty_OneNote.log" },
		
		};
	}

	
	
	
}
