package com.elastica.beatle.tests.gateway.salesforce;

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
import com.elastica.beatle.replayTool.EPDV1SampleTest;


/*******************Author**************
 * 
 * @author usman
 */

public class AccessEnforcementPolicyTest extends CommonConfiguration {

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
	String platform=" using Platform: Mac OS X, Version: 10.10 and Browser : Firefox and Version : 40.0 violating policy:ACCESS_ENFORCE_FOLDER2";
	Map <String, String> data = new HashMap<String, String>();
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
	
	@BeforeMethod(alwaysRun=true)
	public void beforMethod(Method method) throws Exception {
		//deleteAllPolicies();
		Reporter.log("--------------------------------------------", true);
		Reporter.log("Deleting all policies", true);
		Reporter.log("--------------------------------------------", true);
	}
	
	
	@Test(groups ={"TEST", "REGRESSION","SESSION_POLICY", "P1"},dataProvider = "_1")
	public void verifyAccessEnforcementPolicyForSessionSelectiveAppSelectiveUserActionBlock(String policyName, String action, String saasApps,  String testUserName,  String notifyEmailId, String objectAccess, String activityAccess,   String activityType, String objectType, String severity, String logFile) throws Exception{
		Reporter.log("Validate User Session with policy enablement ", true);
		testUserName=testUserName+"@"+suiteData.getTenantDomainName();
		String expectedMsg="[ALERT] "+testUserName+ " attempted Activity: "+activityAccess+" on Object type: Session";
		policyDataMap.clear();
		policyDataMap.put(GatewayTestConstants.POLICY_NAME, policyName);
		policyDataMap.put(GatewayTestConstants.ACTIONS, action);
		policyDataMap.put(GatewayTestConstants.APPLICATIONS, saasApps);
		policyDataMap.put(GatewayTestConstants.TARGET_USER, testUserName);
		policyDataMap.put(GatewayTestConstants.NOTIFY_EMAILID, notifyEmailId+"@"+suiteData.getTenantDomainName());
		policyDataMap.put(GatewayTestConstants.OBJECT_ACCESS, objectAccess);
		policyDataMap.put(GatewayTestConstants.ACTIVITY_ACCESS, activityAccess);
		PolicyAccessEnforcement.policyAccessEnforcement(suiteData, requestHeader, policyDataMap);
		replayLogs(logFile);
		PolicyAccessEnforcement.deletePolicy(suiteData, requestHeader, policyDataMap);
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		data.put("policy_type", "AccessEnforcement");
		data.put("policy_action", "ALERT");
		data.put("action_taken", "block,");
		data.put("_PolicyViolated", policyName);
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), "Logs does not match" );
		
		//protectFunctions.getProtectPolicyViolationAlertLog(restClient, policyName, requestHeader, suiteData, fromTime );
	}
	
	@DataProvider
	public Object[][] _1() {
		return new Object[][]{
			//Policy Name                 App     Test User   admin   Object Access Activity Access      Activity Type     Object Type  Severity
//			{ "ACCESS_ENFORCE_SESSION_BLOCK1", "BLOCK_SHARE", "Salesforce",   "testuser1",  "admin",   "Session",    "Login",        "Policy Violation",  "SESSION", "critical", "App,Salesforce,login.log"        },
//			{ "ACCESS_ENFORCE_SESSION_BLOCK2", "BLOCK_SHARE", "Salesforce",   "testuser1",  "admin",   "Session",    "Logout",       "Policy Violation",  "SESSION", "critical", "App,Salesforce,logout.log"       },
//			{ "ACCESS_ENFORCE_SESSION_BLOCK3", "BLOCK_SHARE", "Salesforce",   "testuser1",  "admin",   "Session",    "InvalidLogin", "Policy Violation",  "SESSION", "critical", "App,Salesforce,invalidLogin.log" }
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
	
	//@Test(groups ={"TEST", "ACCESS_ENFORCEMENT_COMMENTS_POLICY"},dataProvider = "_Comment")
	public void verifyUserAccessEnforcementPolicyComments(String policyName, String saasApps,  String testUserName,  String notifyEmailId, String objectAccess, String activityAccess,   String activityType,  String severity, String logFile) throws Exception{
		Reporter.log("Validate User Upload the file/folder to folder with Policy Enabled", true);
		testUserName=testUserName+"@"+suiteData.getTenantDomainName();
		String expectedMsg="[ALERT] "+testUserName+ " attempted Activity: "+activityAccess+" on Object type: "+objectAccess;
		policyDataMap.clear();
		policyDataMap.put(GatewayTestConstants.POLICY_NAME, policyName);
		policyDataMap.put(GatewayTestConstants.APPLICATIONS, saasApps);
		policyDataMap.put(GatewayTestConstants.TARGET_USER, testUserName);
		policyDataMap.put(GatewayTestConstants.NOTIFY_EMAILID, notifyEmailId+"@"+suiteData.getTenantDomainName());
		policyDataMap.put(GatewayTestConstants.OBJECT_ACCESS, objectAccess);
		policyDataMap.put(GatewayTestConstants.ACTIVITY_ACCESS, activityAccess);
		//PolicyAccessEnforcement.deletePolicy(suiteData, requestHeader, policyDataMap);
		PolicyAccessEnforcement.accessEnforcementPolicyCreateEnable(suiteData, requestHeader, policyDataMap);
		replayLogs(logFile);
		PolicyAccessEnforcement.deletePolicy(suiteData, requestHeader, policyDataMap);
		
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		data.put("policy_type", "AccessEnforcement");
		data.put("policy_action", "ALERT");
		data.put("action_taken", "block,email,");
		data.put("_PolicyViolated", policyName);
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), "Logs does not match" ); 
	}
	
	@DataProvider
	public Object[][] _Comment() {
		return new Object[][]{
			//Policy Name                 App     Test User   admin   Object Access Activity Access      Activity Type
		
			{ "ACCESS_ENFORCE_FILE2",   "Salesforce",   "testuser1",  "admin",   "Comment",    	"Post",   "Policy Violation",   "critical", "Comment,Salesforce,post.log" },//need to record new logs
		};
	}
	
	
	@Test(groups ={"TEST", "ACCESS_ENFORCEMENT_COMMENTS_POLICY"},dataProvider = "_Accounts")
	public void verifyUserAccessEnforcementPolicyAccounts(String policyName, String saasApps,  String testUserName,  String notifyEmailId, String objectAccess, String activityAccess,   String activityType,  String severity, String objectName, String logFile) throws Exception{
		Reporter.log("Validate User Upload the file/folder to folder with Policy Enabled", true);
		testUserName=testUserName+"@"+suiteData.getTenantDomainName();
		String expectedMsg="[ALERT] "+testUserName+ " attempted Activity: "+activityAccess+" on Object type: "+objectAccess+" name: "+objectName+" using Platform";
		
		System.out.println("1Msg: "+expectedMsg);
		
		policyDataMap.clear();
		policyDataMap.put(GatewayTestConstants.POLICY_NAME, policyName);
		policyDataMap.put(GatewayTestConstants.APPLICATIONS, saasApps);
		policyDataMap.put(GatewayTestConstants.TARGET_USER, testUserName);
		policyDataMap.put(GatewayTestConstants.NOTIFY_EMAILID, notifyEmailId+"@"+suiteData.getTenantDomainName());
		policyDataMap.put(GatewayTestConstants.OBJECT_ACCESS, objectAccess);
		policyDataMap.put(GatewayTestConstants.ACTIVITY_ACCESS, activityAccess);
		//PolicyAccessEnforcement.deletePolicy(suiteData, requestHeader, policyDataMap);
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
	public Object[][] _Accounts() {
		return new Object[][]{
			//Policy Name                 App     Test User   admin   Object Access Activity Access      Activity Type
		
			{ "ACCESS_ENFORCE_ACCOUNT_CREATE",  "Salesforce",   "testuser1",  "admin",   "Account",    	"Create",      			"Policy Violation",   "critical", "testelastica", "Account,Salesforce,create.log" },
			{ "ACCESS_ENFORCE_ACCOUNT_VIEW",   	"Salesforce",   "testuser1",  "admin",   "Account",    	"View",      			"Policy Violation",   "critical", "testelastica", "Account,Salesforce,view.log" },
			{ "ACCESS_ENFORCE_ACCOUNT_DELETE",  "Salesforce",   "testuser1",  "admin",   "Account",    	"Delete",      			"Policy Violation",   "critical", "", "Account,Salesforce,delete.log" },
		};
	}
	
	
	
	@Test(groups ={"TEST", "ACCESS_ENFORCEMENT_COMMENTS_POLICY"},dataProvider = "_AccountEdit")
	public void verifyUserAccessEnforcementPolicyAccountEdit(String policyName, String saasApps,  String testUserName,  String notifyEmailId, String objectAccess, String activityAccess,   String activityType,  String severity, String logFile) throws Exception{
		Reporter.log("Validate User Upload the file/folder to folder with Policy Enabled", true);
		testUserName=testUserName+"@"+suiteData.getTenantDomainName();
		String expectedMsg="[ALERT] "+testUserName+ " attempted Activity: "+activityAccess+" on Object type: "+objectAccess+"  using Platform";
		
		System.out.println("1Msg: "+expectedMsg);
		
		policyDataMap.clear();
		policyDataMap.put(GatewayTestConstants.POLICY_NAME, policyName);
		policyDataMap.put(GatewayTestConstants.APPLICATIONS, saasApps);
		policyDataMap.put(GatewayTestConstants.TARGET_USER, testUserName);
		policyDataMap.put(GatewayTestConstants.NOTIFY_EMAILID, notifyEmailId+"@"+suiteData.getTenantDomainName());
		policyDataMap.put(GatewayTestConstants.OBJECT_ACCESS, objectAccess);
		policyDataMap.put(GatewayTestConstants.ACTIVITY_ACCESS, activityAccess);
		//PolicyAccessEnforcement.deletePolicy(suiteData, requestHeader, policyDataMap);
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
	public Object[][] _AccountEdit() {
		return new Object[][]{
			//Policy Name                 App     Test User   admin   Object Access Activity Access      Activity Type
		
     		{ "ACCESS_ENFORCE_ACCOUNT_EDIT",   	"Salesforce",   "testuser1",  "admin",   "Account",    	"Edit",      			"Policy Violation",   "critical", "Account,Salesforce,edit.log" },
		};
	}
	
	
	
	@Test(groups ={"TEST", "ACCESS_ENFORCEMENT_CONNECT_POLICY"},dataProvider = "_Contacts")
	public void verifyUserAccessEnforcementPolicyContacts(String policyName, String saasApps,  String testUserName,  String notifyEmailId, String objectAccess, String activityAccess,   String activityType, String severity, String objectName, String logFile) throws Exception{
		Reporter.log("Validate User Upload the file/folder to folder with Policy Enabled", true);
		testUserName=testUserName+"@"+suiteData.getTenantDomainName();
		String expectedMsg="[ALERT] "+testUserName+ " attempted Activity: "+activityAccess+" on Object type: "+objectAccess+" name: "+objectName+" using Platform";
		System.out.println("1Msg: "+expectedMsg);
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
	public Object[][] _Contacts() {
		return new Object[][]{
			//Policy Name                 App     Test User   admin   Object Access Activity Access      Activity Type
		
	    	{ "ACCESS_ENFORCE_CONTACT_CREATE",  "Salesforce",   "testuser1",  "admin",   "Contact",    	"Create",    "Policy Violation",   "critical", "testcase",  "Contact,Salesforce,create.log" },
//      	{ "ACCESS_ENFORCE_CONTACT_EDIT",   	"Salesforce",   "testuser1",  "admin",   "Contact",    	"Edit",      "Policy Violation",   "critical", "", "Contact,Salesforce,edit.log" },
//			{ "ACCESS_ENFORCE_CONTACT_VIEW",   	"Salesforce",   "testuser1",  "admin",   "Contact",    	"View",      "Policy Violation",   "critical", "", "Contact,Salesforce,view.log" },
			{ "ACCESS_ENFORCE_CONTACT_DELETE",  "Salesforce",   "testuser1",  "admin",   "Contact",    	"Delete",    "Policy Violation",   "critical", "", "Contact,Salesforce,delete.log" },
		};
	}
	
	
	
	@Test(groups ={"TEST", "ACCESS_ENFORCEMENT_CONNECT_POLICY"},dataProvider = "_File")
	public void verifyUserAccessEnforcementPolicyFile(String policyName, String saasApps,  String testUserName,  String notifyEmailId, String objectAccess, String activityAccess,   String activityType,  String severity, String objectName, String logFile) throws Exception{
		Reporter.log("Validate User Upload the file/folder to folder with Policy Enabled", true);
		testUserName=testUserName+"@"+suiteData.getTenantDomainName();
		String expectedMsg="[ALERT] "+testUserName+ " attempted Activity: "+activityAccess+" on Object type: "+objectAccess+" name: "+objectName+" using Platform";
		System.out.println("1Msg: "+expectedMsg);
		policyDataMap.clear();
		policyDataMap.put(GatewayTestConstants.POLICY_NAME, policyName);
		policyDataMap.put(GatewayTestConstants.APPLICATIONS, saasApps);
		policyDataMap.put(GatewayTestConstants.TARGET_USER, testUserName);
		policyDataMap.put(GatewayTestConstants.NOTIFY_EMAILID, notifyEmailId+"@"+suiteData.getTenantDomainName());
		policyDataMap.put(GatewayTestConstants.OBJECT_ACCESS, objectAccess);
		policyDataMap.put(GatewayTestConstants.ACTIVITY_ACCESS, activityAccess);
		//PolicyAccessEnforcement.deletePolicy(suiteData, requestHeader, policyDataMap);
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
	public Object[][] _File() {
		return new Object[][]{
			//Policy Name                 App     Test User   admin   Object Access Activity Access      Activity Type
		
			{ "ACCESS_ENFORCE_FILE_SHARE",   	"Salesforce",   "testuser1",  "admin",   "File",  "Share",       "Policy Violation",   "critical", "test", "File,Salesforce,share_wth_link.log" },
			{ "ACCESS_ENFORCE_FILE_UPLOAD",   	"Salesforce",   "testuser1",  "admin",   "File",  "Upload",      "Policy Violation",   "critical", "test.pdf", "File,Salesforce,upload.log" },
//			{ "ACCESS_ENFORCE_FILE_VIEW",       "Salesforce",   "testuser1",  "admin",   "File",  "View",        "Policy Violation",   "critical", "", "File,Salesforce,view.log" },// need to record new log
		};
	}
	
	
	@Test(groups ={"TEST", "ACCESS_ENFORCEMENT_CONNECT_POLICY"},dataProvider = "_downloadFile")
	public void verifyUserAccessEnforcementPolicyFileDownload(String policyName, String saasApps,  String testUserName,  String notifyEmailId, String objectAccess, String activityAccess,   String activityType,  String severity, String objectName, String logFile) throws Exception{
		Reporter.log("Validate User Upload the file/folder to folder with Policy Enabled", true);
		testUserName=testUserName+"@"+suiteData.getTenantDomainName();
		String expectedMsg="[ALERT]  attempted Activity: "+activityAccess+" on Object type: "+objectAccess+"  using Platform";
		System.out.println("1Msg: "+expectedMsg);
		policyDataMap.clear();
		policyDataMap.put(GatewayTestConstants.POLICY_NAME, policyName);
		policyDataMap.put(GatewayTestConstants.APPLICATIONS, saasApps);
		policyDataMap.put(GatewayTestConstants.TARGET_USER, testUserName);
		policyDataMap.put(GatewayTestConstants.NOTIFY_EMAILID, notifyEmailId+"@"+suiteData.getTenantDomainName());
		policyDataMap.put(GatewayTestConstants.OBJECT_ACCESS, objectAccess);
		policyDataMap.put(GatewayTestConstants.ACTIVITY_ACCESS, activityAccess);
		//PolicyAccessEnforcement.deletePolicy(suiteData, requestHeader, policyDataMap);
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
	public Object[][] _downloadFile() {
		return new Object[][]{
			//Policy Name                 App     Test User   admin   Object Access Activity Access      Activity Type
		
			{ "ACCESS_ENFORCE_FILE_DOWNLOAD",   "Salesforce",   "testuser1",  "admin",   "File",  "Download",    "Policy Violation",   "critical", "test",  "File,Salesforce,download.log" },
		};
	}
	
	
	
	@Test(groups ={"TEST", "ACCESS_ENFORCEMENT_CONNECT_POLICY"},dataProvider = "_Lead")
	public void verifyUserAccessEnforcementPolicyLead(String policyName, String saasApps,  String testUserName,  String notifyEmailId, String objectAccess, String activityAccess,   String activityType,  String severity, String objectName, String logFile) throws Exception{
		Reporter.log("Validate User Upload the file/folder to folder with Policy Enabled", true);
		testUserName=testUserName+"@"+suiteData.getTenantDomainName();
		String expectedMsg="[ALERT] "+testUserName+ " attempted Activity: "+activityAccess+" on Object type: "+objectAccess+" name: "+objectName+" using Platform";
		policyDataMap.clear();
		policyDataMap.put(GatewayTestConstants.POLICY_NAME, policyName);
		policyDataMap.put(GatewayTestConstants.APPLICATIONS, saasApps);
		policyDataMap.put(GatewayTestConstants.TARGET_USER, testUserName);
		policyDataMap.put(GatewayTestConstants.NOTIFY_EMAILID, notifyEmailId+"@"+suiteData.getTenantDomainName());
		policyDataMap.put(GatewayTestConstants.OBJECT_ACCESS, objectAccess);
		policyDataMap.put(GatewayTestConstants.ACTIVITY_ACCESS, activityAccess);
		//PolicyAccessEnforcement.deletePolicy(suiteData, requestHeader, policyDataMap);
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
	public Object[][] _Lead() {
		return new Object[][]{
			//Policy Name                 App     Test User   admin   Object Access Activity Access      Activity Type
		
//			{ "ACCESS_ENFORCE_LEAD_CREATE",     "Salesforce",   "testuser1",  "admin",   "Lead",  "Create",    "Policy Violation",   "critical", "testlead",   "Lead,Salesforce,create.log" },
			{ "ACCESS_ENFORCE_LEAD_DELETE",   	"Salesforce",   "testuser1",  "admin",   "Lead",  "Delete",       "Policy Violation",   "critical", "", "Lead,Salesforce,delete.log" },
//			{ "ACCESS_ENFORCE_LEAD_EDIT",   	"Salesforce",   "testuser1",  "admin",   "Lead",  "Edit",      "Policy Violation",   "critical",  "",  "Lead,Salesforce,edit.log" },
//			{ "ACCESS_ENFORCE_LEAD_VIEW",       "Salesforce",   "testuser1",  "admin",   "Lead",  "View",        "Policy Violation",   "critical", "test1 testlead", "Lead,Salesforce,view.log" },
		};
	}
	
	
	@Test(groups ={"TEST", "ACCESS_ENFORCEMENT_CONNECT_POLICY"},dataProvider = "_Opportunity")
	public void verifyUserAccessEnforcementPolicyOpportunity(String policyName, String saasApps,  String testUserName,  String notifyEmailId, String objectAccess, String activityAccess,   String activityType,  String severity, String objectName, String logFile) throws Exception{
		Reporter.log("Validate User Upload the file/folder to folder with Policy Enabled", true);
		testUserName=testUserName+"@"+suiteData.getTenantDomainName();
		String expectedMsg="[ALERT] "+testUserName+ " attempted Activity: "+activityAccess+" on Object type: "+objectAccess+" name: "+objectName+" using Platform";
		policyDataMap.clear();
		policyDataMap.put(GatewayTestConstants.POLICY_NAME, policyName);
		policyDataMap.put(GatewayTestConstants.APPLICATIONS, saasApps);
		policyDataMap.put(GatewayTestConstants.TARGET_USER, testUserName);
		policyDataMap.put(GatewayTestConstants.NOTIFY_EMAILID, notifyEmailId+"@"+suiteData.getTenantDomainName());
		policyDataMap.put(GatewayTestConstants.OBJECT_ACCESS, objectAccess);
		policyDataMap.put(GatewayTestConstants.ACTIVITY_ACCESS, activityAccess);
		//PolicyAccessEnforcement.deletePolicy(suiteData, requestHeader, policyDataMap);
		PolicyAccessEnforcement.accessEnforcementPolicyCreateEnable(suiteData, requestHeader, policyDataMap);
		replayLogsWithDelay(logFile);
		PolicyAccessEnforcement.deletePolicy(suiteData, requestHeader, policyDataMap);
        data.clear();
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		data.put("policy_type", "AccessEnforcement");
		data.put("policy_action", "ALERT");
		data.put("action_taken", "block,");
		data.put("_PolicyViolated", policyName);
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), "Logs does not match" );
		
	}
	
	@DataProvider
	public Object[][] _Opportunity() {
		return new Object[][]{
			//Policy Name                 App     Test User   admin   Object Access Activity Access      Activity Type
		
			{ "ACCESS_ENFORCE_LEAD_CREATE",     "Salesforce",   "testuser1",  "admin",   "Opportunity",  "Create",    "Policy Violation",   "critical",  "test1",  "Opportunity,Salesforce,create.log" },
			{ "ACCESS_ENFORCE_LEAD_DELETE",   	"Salesforce",   "testuser1",  "admin",   "Opportunity",  "Delete",    "Policy Violation",   "critical",  "",  "Opportunity,Salesforce,delete.log" },
			{ "ACCESS_ENFORCE_LEAD_VIEW",       "Salesforce",   "testuser1",  "admin",   "Opportunity",  "View",      "Policy Violation",   "critical", "test1",   "Opportunity,Salesforce,view.log" },
		};
	}
	
	
	@Test(groups ={"TEST", "ACCESS_ENFORCEMENT_CONNECT_POLICY"},dataProvider = "_OpportunityEdit")
	public void verifyUserAccessEnforcementPolicyOpportunityEdit(String policyName, String saasApps,  String testUserName,  String notifyEmailId, String objectAccess, String activityAccess,   String activityType,  String severity, String objectName, String logFile) throws Exception{
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
		//PolicyAccessEnforcement.deletePolicy(suiteData, requestHeader, policyDataMap);
		PolicyAccessEnforcement.accessEnforcementPolicyCreateEnable(suiteData, requestHeader, policyDataMap);
		replayLogsWithDelay(logFile);
		PolicyAccessEnforcement.deletePolicy(suiteData, requestHeader, policyDataMap);
        data.clear();
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		data.put("policy_type", "AccessEnforcement");
		data.put("policy_action", "ALERT");
		data.put("action_taken", "block,");
		data.put("_PolicyViolated", policyName);
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), "Logs does not match" );
		
	}
	
	@DataProvider
	public Object[][] _OpportunityEdit() {
		return new Object[][]{
			//Policy Name                 App     Test User   admin   Object Access Activity Access      Activity Type
		
			{ "ACCESS_ENFORCE_LEAD_EDIT",   	"Salesforce",   "testuser1",  "admin",   "Opportunity",  "Edit",      "Policy Violation",   "critical",  "",  "Opportunity,Salesforce,edit.log" },
		};
	}
	
	
	@Test(groups ={"TEST", "ACCESS_ENFORCEMENT_CONNECT_POLICY"},dataProvider = "_Report")
	public void verifyUserAccessEnforcementPolicyReport(String policyName, String saasApps,  String testUserName,  String notifyEmailId, String objectAccess, String activityAccess,   String activityType,  String severity, String objectName, String logFile) throws Exception{
		Reporter.log("Validate User Upload the file/folder to folder with Policy Enabled", true);
		testUserName=testUserName+"@"+suiteData.getTenantDomainName();
		String expectedMsg="[ALERT] "+testUserName+ " attempted Activity: "+activityAccess+" on Object type: "+objectAccess+" name: "+objectName+" using Platform";
		policyDataMap.clear();
		policyDataMap.put(GatewayTestConstants.POLICY_NAME, policyName);
		policyDataMap.put(GatewayTestConstants.APPLICATIONS, saasApps);
		policyDataMap.put(GatewayTestConstants.TARGET_USER, testUserName);
		policyDataMap.put(GatewayTestConstants.NOTIFY_EMAILID, notifyEmailId+"@"+suiteData.getTenantDomainName());
		policyDataMap.put(GatewayTestConstants.OBJECT_ACCESS, objectAccess);
		policyDataMap.put(GatewayTestConstants.ACTIVITY_ACCESS, activityAccess);
		//PolicyAccessEnforcement.deletePolicy(suiteData, requestHeader, policyDataMap);
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
	public Object[][] _Report() {
		return new Object[][]{
			//Policy Name                 App     Test User   admin   Object Access Activity Access      Activity Type
		
			{ "ACCESS_ENFORCE_REPORT_DELETE",   	"Salesforce",   "testuser1",  "admin",   "Report",  "Delete",    "Policy Violation",   "critical", "gwautomationtest",   "Report,Salesforce,delete.log" },
			{ "ACCESS_ENFORCE_REPORT_EDIT",   	"Salesforce",   "testuser1",  "admin",   "Report",  "Edit",      "Policy Violation",   "critical", "gwautomationtest",   "Report,Salesforce,edit.log" },
			{ "ACCESS_ENFORCE_REPORT_VIEW",       "Salesforce",   "testuser1",  "admin",   "Report",  "View",      "Policy Violation",   "critical", "gwautomation",   "Report,Salesforce,view.log" },
			{ "ACCESS_ENFORCE_REPORT_EXPORT",       "Salesforce",   "testuser1",  "admin",   "Report",  "Export",      "Policy Violation",   "critical", "gwautomationtest.xls",    "Report,Salesforce,export.log" },
			
		};
	}
	
	
	@Test(groups ={"TEST", "ACCESS_ENFORCEMENT_CONNECT_POLICY"},dataProvider = "_Report1")
	public void verifyUserAccessEnforcementPolicyReport1(String policyName, String saasApps,  String testUserName,  String notifyEmailId, String objectAccess, String activityAccess,   String activityType,  String severity, String objectName, String logFile) throws Exception{
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
		//PolicyAccessEnforcement.deletePolicy(suiteData, requestHeader, policyDataMap);
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
	public Object[][] _Report1() {
		return new Object[][]{
			//Policy Name                 App     Test User   admin   Object Access Activity Access      Activity Type
		
			{ "ACCESS_ENFORCE_REPORT_CREATE",     "Salesforce",   "testuser1",  "admin",   "Report",  "Create",    "Policy Violation",   "critical", "",   "Report,Salesforce,create.log" },
			{ "ACCESS_ENFORCE_REPORT_RUN",       "Salesforce",   "testuser1",  "admin",   "Report",  "Run",      "Policy Violation",   "critical", "",   "Report,Salesforce,run.log" },
			
		};
	}
	
	@Test(groups ={"TEST", "ACCESS_ENFORCEMENT_FILE_POLICY"},dataProvider = "_policyAccessEnabledFileGeneric")
	public void verifyUserAccessEnforcementPolicyEnabledForFileGeneric(String policyName, String saasApps,  String testUserName,  String notifyEmailId, String objectAccess, String activityAccess,  String activity, String activityType,  String severity, String logFile) throws Exception{
		Reporter.log("Validate User Upload the file/folder to folder with Policy Enabled", true);
		testUserName=testUserName+"@"+suiteData.getTenantDomainName();
		String expectedMsg="[ALERT] "+testUserName+ " attempted access to cloud apps using Platform";
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
	public Object[][] _policyAccessEnabledFileGeneric() {
		return new Object[][]{
			//Policy Name                 App     Test User   admin   Object Access Activity Access      Activity Type
			{ "ACCESS_ENFORCE_FILE1",  "Salesforce",   "testuser1",  "admin",   "__ALL_EL__",    "__ALL_EL__",	 "Upload",  "Policy Violation",   "critical", "File,Salesforce,upload.log" },
			{ "ACCESS_ENFORCE_FILE2",  "Salesforce",   "testuser1",  "admin",   "__ALL_EL__",    "__ALL_EL__",	 "Download",  "Policy Violation",   "critical", "File,Salesforce,download.log" },
			{ "ACCESS_ENFORCE_FILE3",  "Salesforce",   "testuser1",  "admin",   "__ALL_EL__",    "__ALL_EL__",	 "Edit",  "Policy Violation",   "critical", "file,Salesforce,edit.log" },
    		{ "ACCESS_ENFORCE_FILE4",  "Salesforce",   "testuser1",  "admin",   "__ALL_EL__",    "__ALL_EL__",	 "Search",  "Policy Violation",   "critical", "file,salesforce,search.log" },
			{ "ACCESS_ENFORCE_FILE5",  "Salesforce",   "testuser1",  "admin",   "__ALL_EL__",    "__ALL_EL__",	 "View",  "Policy Violation",   "critical", "File,Salesforce,view.log" },
		};
	}

	
	

	@Test(groups ={"DELETE_ALL"})
	public void deleteAllPolicies() throws Exception{
		Reporter.log("Validate User Upload the file/folder to folder with Policy Enabled", true);
		PolicyAccessEnforcement.deleteAllPolicy(suiteData,  requestHeader);
	}
	
}
