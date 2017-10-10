package com.elastica.beatle.tests.gateway.box;

import org.testng.annotations.Test;
import org.testng.annotations.AfterTest;
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
	String platform=" using Platform: Mac OS X, Version: 10.10 and Browser : Firefox and Version : 40.0 violating policy:";
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
		Reporter.log("--------------------------------------------", true);
		Reporter.log("Deleting all policies", true);
		deleteAllPolicies();
		Reporter.log("--------------------------------------------", true);
	}
	
	
	@Test(groups ={"TEST", "REGRESSION","SESSION_POLICY", "P1"},dataProvider = "_1")
	public void verifyAccessEnforcementPolicyForSessionSelectiveAppSelectiveUserActionBlock(String policyName, String action, String saasApps,  String testUserName,  String notifyEmailId, String objectAccess, String activityAccess,   String activityType, String objectType, String severity, String logFile) throws Exception{
		Reporter.log("Validate User Session with policy enablement ", true);
		testUserName=testUserName+"@"+suiteData.getTenantDomainName();
		String expectedMsg="[ALERT] "+testUserName+ " attempted Activity: "+activityAccess+" on Object type: Session  using Platform";
		policyDataMap.clear();
		policyDataMap.put(GatewayTestConstants.POLICY_NAME, policyName);
		policyDataMap.put(GatewayTestConstants.ACTIONS, action);
		policyDataMap.put(GatewayTestConstants.APPLICATIONS, saasApps);
		policyDataMap.put(GatewayTestConstants.TARGET_USER, testUserName);
		policyDataMap.put(GatewayTestConstants.NOTIFY_EMAILID, notifyEmailId+"@"+suiteData.getTenantDomainName());
		policyDataMap.put(GatewayTestConstants.OBJECT_ACCESS, objectAccess);
		policyDataMap.put(GatewayTestConstants.ACTIVITY_ACCESS, activityAccess);
		PolicyAccessEnforcement.policyAccessEnforcement(suiteData, requestHeader, policyDataMap);
		replayLogsEPDV3(logFile);
		PolicyAccessEnforcement.deletePolicy(suiteData, requestHeader, policyDataMap);
		
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		data.put("policy_type", "AccessEnforcement");
		data.put("policy_action", "ALERT");
		data.put("action_taken", "block,");
		data.put("_PolicyViolated", policyName);
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(activityType, severity), data)), "Logs does not match" ); 

		//protectFunctions.getProtectPolicyViolationAlertLog(restClient, policyName, requestHeader, suiteData, fromTime );
	}
	
	@DataProvider
	public Object[][] _1() {
		return new Object[][]{
			//Policy Name                 App     Test User   admin   Object Access Activity Access      Activity Type     Object Type  Severity
			{ "ACCESS_ENFORCE_SESSION_BLOCK1", "BLOCK_SHARE", "Box",   "testuser1",  "admin",   "Session",    "Login",        "Policy Violation",  "SESSION", "critical", "BoxSession,Login.log"        },
			{ "ACCESS_ENFORCE_SESSION_BLOCK2", "BLOCK_SHARE", "Box",   "testuser1",  "admin",   "Session",    "Logout",       "Policy Violation",  "SESSION", "critical", "BoxSession,LogOut.log"       },
			{ "ACCESS_ENFORCE_SESSION_BLOCK3", "BLOCK_SHARE", "Box",   "testuser1",  "admin",   "Session",    "InvalidLogin", "Policy Violation",  "SESSION", "critical", "BoxSession,InvalidLogin.log" }
		};
	}
	
	
	
	@Test(groups ={"TEST", "REGRESSION","SESSION_POLICY", "P1"},dataProvider = "_5")
	public void verifyAccessEnforcementPolicyForSessionSelectiveAppSelectiveUserActionNotifyAdmin(String policyName, String action, String saasApps,  String testUserName,  String notifyEmailId, String objectAccess, String activityAccess,   String activityType, String objectType, String severity, String logFile, String  expectedMsg1)throws Exception{
		Reporter.log("Validate User Session with policy enablement ", true);
		testUserName=testUserName+"@"+suiteData.getTenantDomainName();
		String expectedMsg="[ALERT] "+testUserName+ " attempted Activity: "+activityAccess+" on Object type: Session  using Platform";
		policyDataMap.clear();
		policyDataMap.put(GatewayTestConstants.POLICY_NAME, policyName);
		policyDataMap.put(GatewayTestConstants.ACTIONS, action);
		policyDataMap.put(GatewayTestConstants.APPLICATIONS, saasApps);
		policyDataMap.put(GatewayTestConstants.TARGET_USER, testUserName);
		policyDataMap.put(GatewayTestConstants.NOTIFY_EMAILID, notifyEmailId+"@"+suiteData.getTenantDomainName());
		policyDataMap.put(GatewayTestConstants.OBJECT_ACCESS, objectAccess);
		policyDataMap.put(GatewayTestConstants.ACTIVITY_ACCESS, activityAccess);
		PolicyAccessEnforcement.policyAccessEnforcement(suiteData, requestHeader, policyDataMap);
		replayLogsEPDV3(logFile);
		PolicyAccessEnforcement.deletePolicy(suiteData, requestHeader, policyDataMap);
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		data.put("policy_type", "AccessEnforcement");
		data.put("policy_action", "ALERT");
		data.put("action_taken", "email,");
		data.put("_PolicyViolated", policyName);
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(activityType, severity), data)), "Logs does not match" ); 
		data.clear();
		data.put("message", expectedMsg1);
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), "Logs does not match" ); 
		
	}
	
	@DataProvider
	public Object[][] _5() {
		return new Object[][]{
			//Policy Name                 App     Test User   admin   Object Access Activity Access      Activity Type     Object Type  Severity
			{ "ACCESS_ENFORCE_SESSION_BLOCK_EMAIL1", "NOTIFY_EMAIL", "Box",   "testuser1",  "admin",   "Session",    "Login",        "Policy Violation",  "SESSION", "critical", "BoxSession,Login.log" ,       "User logged in"      },
			{ "ACCESS_ENFORCE_SESSION_BLOCK_EMAIL2", "NOTIFY_EMAIL", "Box",   "testuser1",  "admin",   "Session",    "Logout",       "Policy Violation",  "SESSION", "critical", "BoxSession,LogOut.log",       "User logged out"       },
//			{ "ACCESS_ENFORCE_SESSION_BLOCK_EMAIL3", "NOTIFY_EMAIL", "Box",   "testuser1",  "admin",   "Session",    "InvalidLogin", "Policy Violation",  "SESSION", "critical", "BoxSession,InvalidLogin.log", "User Login Failed!" }
		};
	}

	@Test(groups ={"TEST", "REGRESSION","SESSION_POLICY", "P1"},dataProvider = "_6")
	public void verifyAccessEnforcementPolicyForSessionSelectiveAppSelectiveUserActionNotifyUser(String policyName, String action, String saasApps,  String testUserName,  String notifyEmailId, String objectAccess, String activityAccess,   String activityType, String objectType, String severity, String logFile, String  expectedMsg1)throws Exception{
		Reporter.log("Validate User Session with policy enablement ", true);
		testUserName=testUserName+"@"+suiteData.getTenantDomainName();
		String expectedMsg="[ALERT] "+testUserName+ " attempted Activity: "+activityAccess+" on Object type: Session  using Platform";
		policyDataMap.clear();
		policyDataMap.put(GatewayTestConstants.POLICY_NAME, policyName);
		policyDataMap.put(GatewayTestConstants.ACTIONS, action);
		policyDataMap.put(GatewayTestConstants.APPLICATIONS, saasApps);
		policyDataMap.put(GatewayTestConstants.TARGET_USER, testUserName);
		policyDataMap.put(GatewayTestConstants.NOTIFY_EMAILID, notifyEmailId+"@"+suiteData.getTenantDomainName());
		policyDataMap.put(GatewayTestConstants.OBJECT_ACCESS, objectAccess);
		policyDataMap.put(GatewayTestConstants.ACTIVITY_ACCESS, activityAccess);
		PolicyAccessEnforcement.policyAccessEnforcement(suiteData, requestHeader, policyDataMap);
		replayLogsEPDV3(logFile);
		PolicyAccessEnforcement.deletePolicy(suiteData, requestHeader, policyDataMap);
		
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		data.put("policy_type", "AccessEnforcement");
		data.put("policy_action", "ALERT");
		//data.put("action_taken", "email,");
		data.put("_PolicyViolated", policyName);
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(activityType, severity), data)), "Logs does not match" ); 
		data.clear();
		data.put("message", expectedMsg1);
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), "Logs does not match" ); 
		
		
		
		//fsr=fetchElasticSearchLogs();         		//fetch the logs from ES
		/*messages=processLogs(fsr);		
		Assert.assertTrue(validateMessgaeBody(messages, expectedMsg1), "Logs does not match" ); //Process the messages from logs
		Assert.assertTrue(validateMessgaeBody(messages, expectedMsg), "Logs does not match" );    //Validate the messages
		Assert.assertTrue(validatePolicyDetails(messages, policyName), "Policy does not match" );    //Validate policy
	*/
	}
	
	@DataProvider
	public Object[][] _6() {
		return new Object[][]{
			//Policy Name                 App     Test User   admin   Object Access Activity Access      Activity Type     Object Type  Severity
			{ "ACCESS_ENFORCE_SESSION_BLOCK_EMAIL1", "NOTIFY_USER", "Box",   "testuser1",  "admin",   "Session",    "Login",        "Policy Violation",  "SESSION", "critical", "BoxSession,Login.log" , "User logged in"      },
			{ "ACCESS_ENFORCE_SESSION_BLOCK_EMAIL2", "NOTIFY_USER", "Box",   "testuser1",  "admin",   "Session",    "Logout",       "Policy Violation",  "SESSION", "critical", "BoxSession,LogOut.log", "User logged out"       },
//			{ "ACCESS_ENFORCE_SESSION_BLOCK_EMAIL3", "NOTIFY_USER", "Box",   "testuser1",  "admin",   "Session",    "InvalidLogin", "Policy Violation",  "SESSION", "critical", "BoxSession,InvalidLogin.log", "User Login Failed!" }
		};
	}
	
	@Test(groups ={"TEST", "REGRESSION","SESSION_POLICY", "P1"},dataProvider = "_7")
	public void verifyAccessEnforcementPolicyForSessionSelectiveAppSelectiveUserActionBlockNotifyAdminNotifyUser(String policyName, String action, String saasApps,  String testUserName,  String notifyEmailId, String objectAccess, String activityAccess,   String activityType, String objectType, String severity, String logFile) throws Exception{
		Reporter.log("Validate User Session with policy enablement ", true);
		testUserName=testUserName+"@"+suiteData.getTenantDomainName();
		String expectedMsg="[ALERT] "+testUserName+ " attempted Activity: "+activityAccess+" on Object type: Session  using Platform";
		policyDataMap.clear();
		policyDataMap.put(GatewayTestConstants.POLICY_NAME, policyName);
		policyDataMap.put(GatewayTestConstants.ACTIONS, action);
		policyDataMap.put(GatewayTestConstants.APPLICATIONS, saasApps);
		policyDataMap.put(GatewayTestConstants.TARGET_USER, testUserName);
		policyDataMap.put(GatewayTestConstants.NOTIFY_EMAILID, notifyEmailId+"@"+suiteData.getTenantDomainName());
		policyDataMap.put(GatewayTestConstants.OBJECT_ACCESS, objectAccess);
		policyDataMap.put(GatewayTestConstants.ACTIVITY_ACCESS, activityAccess);
		PolicyAccessEnforcement.policyAccessEnforcement(suiteData, requestHeader, policyDataMap);
		replayLogsEPDV3(logFile);
		PolicyAccessEnforcement.deletePolicy(suiteData, requestHeader, policyDataMap);
		
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		data.put("policy_type", "AccessEnforcement");
		data.put("policy_action", "ALERT");
		data.put("action_taken", "block,");
		data.put("_PolicyViolated", policyName);
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(activityType, severity), data)), "Logs does not match" ); 
		
		
		
		
		/*fsr=fetchElasticSearchLogs(activityType);         		//fetch the logs from ES
		messages=processLogs(fsr);													//Process the messages from logs
		Assert.assertTrue(validateMessgaeBody(messages, expectedMsg), "Logs does not match" );    //Validate the messages
		Assert.assertTrue(validatePolicyDetails(messages, policyName), "Policy does not match" );    //Validate policy
	*/
	}
	
	@DataProvider
	public Object[][] _7() {
		return new Object[][]{
			//Policy Name                 App     Test User   admin   Object Access Activity Access      Activity Type     Object Type  Severity
			{ "ACCESS_ENFORCE_SESSION_BLOCK_EMAIL1", "BLOCK_SHARE_NOTIFY_EMAIL_NOTIFY_USER", "Box",   "testuser1",  "admin",   "Session",    "Login",        "Policy Violation",  "SESSION", "critical", "BoxSession,Login.log"        },
			{ "ACCESS_ENFORCE_SESSION_BLOCK_EMAIL2", "BLOCK_SHARE_NOTIFY_EMAIL_NOTIFY_USER", "Box",   "testuser1",  "admin",   "Session",    "Logout",       "Policy Violation",  "SESSION", "critical", "BoxSession,LogOut.log"       },
//			{ "ACCESS_ENFORCE_SESSION_BLOCK_EMAIL3", "BLOCK_SHARE_NOTIFY_EMAIL_NOTIFY_USER", "Box",   "testuser1",  "admin",   "Session",    "InvalidLogin", "Policy Violation",  "SESSION", "critical", "BoxSession,InvalidLogin.log" }
		};
	}
	
	@Test(groups ={"TEST", "REGRESSION","SESSION_POLICY", "P1"},dataProvider = "_2")
	public void verifyAccessEnforcementPolicyForSessionSelectiveAppSelectiveUserActionBlockNotifyAdmin(String policyName, String action, String saasApps,  String testUserName,  String notifyEmailId, String objectAccess, String activityAccess,   String activityType, String objectType, String severity, String logFile) throws Exception{
		Reporter.log("Validate User Session with policy enablement ", true);
		testUserName=testUserName+"@"+suiteData.getTenantDomainName();
		String expectedMsg="[ALERT] "+testUserName+ " attempted Activity: "+activityAccess+" on Object type: Session  using Platform";
		policyDataMap.clear();
		policyDataMap.put(GatewayTestConstants.POLICY_NAME, policyName);
		policyDataMap.put(GatewayTestConstants.ACTIONS, action);
		policyDataMap.put(GatewayTestConstants.APPLICATIONS, saasApps);
		policyDataMap.put(GatewayTestConstants.TARGET_USER, testUserName);
		policyDataMap.put(GatewayTestConstants.NOTIFY_EMAILID, notifyEmailId+"@"+suiteData.getTenantDomainName());
		policyDataMap.put(GatewayTestConstants.OBJECT_ACCESS, objectAccess);
		policyDataMap.put(GatewayTestConstants.ACTIVITY_ACCESS, activityAccess);
		PolicyAccessEnforcement.policyAccessEnforcement(suiteData, requestHeader, policyDataMap);
		replayLogsEPDV3(logFile);
		PolicyAccessEnforcement.deletePolicy(suiteData, requestHeader, policyDataMap);
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		data.put("policy_type", "AccessEnforcement");
		data.put("policy_action", "ALERT");
		data.put("action_taken", "block,email,");
		data.put("_PolicyViolated", policyName);
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(activityType, severity), data)), "Logs does not match" ); 
	
	}
	
	@DataProvider
	public Object[][] _2() {
		return new Object[][]{
			//Policy Name                 App     Test User   admin   Object Access Activity Access      Activity Type     Object Type  Severity
			{ "ACCESS_ENFORCE_SESSION_BLOCK_EMAIL1", "BLOCK_SHARE_NOTIFY_EMAIL", "Box",   "testuser1",  "admin",   "Session",    "Login",        "Policy Violation",  "SESSION", "critical", "BoxSession,Login.log"        },
			{ "ACCESS_ENFORCE_SESSION_BLOCK_EMAIL2", "BLOCK_SHARE_NOTIFY_EMAIL", "Box",   "testuser1",  "admin",   "Session",    "Logout",       "Policy Violation",  "SESSION", "critical", "BoxSession,LogOut.log"       },
//			{ "ACCESS_ENFORCE_SESSION_BLOCK_EMAIL3", "BLOCK_SHARE_NOTIFY_EMAIL", "Box",   "testuser1",  "admin",   "Session",    "InvalidLogin", "Policy Violation",  "SESSION", "critical", "BoxSession,InvalidLogin.log" }
		};
	}
	
	@Test(groups ={"TEST", "REGRESSION","SESSION_POLICY", "P1"},dataProvider = "_3")
	public void verifyAccessEnforcementPolicyForSessionSelectiveAppSelectiveUserActionNoBlock(String policyName, String action, String saasApps,  String testUserName,  String notifyEmailId, String objectAccess, String activityAccess,   String activityType, String objectType, String severity, String logFile, String expectedMsg1) throws Exception{
		Reporter.log("Validate User Session with policy enablement ", true);
		testUserName=testUserName+"@"+suiteData.getTenantDomainName();
		String expectedMsg="[ALERT] "+testUserName+ " attempted Activity: "+activityAccess+" on Object type: Session  using Platform";
		policyDataMap.clear();
		policyDataMap.put(GatewayTestConstants.POLICY_NAME, policyName);
		policyDataMap.put(GatewayTestConstants.ACTIONS, action);
		policyDataMap.put(GatewayTestConstants.APPLICATIONS, saasApps);
		policyDataMap.put(GatewayTestConstants.TARGET_USER, testUserName);
		policyDataMap.put(GatewayTestConstants.NOTIFY_EMAILID, notifyEmailId+"@"+suiteData.getTenantDomainName());
		policyDataMap.put(GatewayTestConstants.OBJECT_ACCESS, objectAccess);
		policyDataMap.put(GatewayTestConstants.ACTIVITY_ACCESS, activityAccess);
		PolicyAccessEnforcement.policyAccessEnforcement(suiteData, requestHeader, policyDataMap);
		replayLogsEPDV3(logFile);
		PolicyAccessEnforcement.deletePolicy(suiteData, requestHeader, policyDataMap);
		
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		data.put("policy_type", "AccessEnforcement");
		data.put("policy_action", "ALERT");
		//data.put("action_taken", "block,email,");
		data.put("_PolicyViolated", policyName);
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), "Logs does not match" ); 
		data.clear();
		data.put("message", expectedMsg1);
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), "Logs does not match" ); 
	}
	
	@DataProvider
	public Object[][] _3() {
		return new Object[][]{
			//Policy Name                 App     Test User   admin   Object Access Activity Access      Activity Type     Object Type  Severity
			{ "ACCESS_ENFORCE_SESSION_NOBLOCK1", "NOBLOCK", "Box",   "testuser1",  "admin",   "Session",    "Login",        "Session",  "SESSION", "critical", "BoxSession,Login.log" , "User logged in"       },
			{ "ACCESS_ENFORCE_SESSION_NOBLOCK2", "NOBLOCK", "Box",   "testuser1",  "admin",   "Session",    "Logout",       "Session",  "SESSION", "critical", "BoxSession,LogOut.log" , "User logged out"     },
//			{ "ACCESS_ENFORCE_SESSION_NOBLOCK3", "NOBLOCK", "Box",   "testuser1",  "admin",   "Session",    "InvalidLogin", "Session",  "SESSION", "critical", "BoxSession,InvalidLogin.log","User Login Failed!" }
		};
	}

	@Test(groups ={"TEST", "REGRESSION","SESSION_POLICY", "P1"},dataProvider = "_4")
	public void verifyAccessEnforcementPolicyForSessionSelectiveAppSelectivePolicyDisabled(String policyName, String action, String saasApps,  String testUserName,  String notifyEmailId, String objectAccess, String activityAccess,   String activityType, String objectType, String severity, String logFile, String expectedMsg1) throws Exception{
		Reporter.log("Validate User Session with policy enablement ", true);
		testUserName=testUserName+"@"+suiteData.getTenantDomainName();
		//String expectedMsg="[ALERT] "+testUserName+ " attempted Activity: "+activityAccess+" on Object type: Session";
		policyDataMap.clear();
		policyDataMap.put(GatewayTestConstants.POLICY_NAME, policyName);
		policyDataMap.put(GatewayTestConstants.ACTIONS, action);
		policyDataMap.put(GatewayTestConstants.APPLICATIONS, saasApps);
		policyDataMap.put(GatewayTestConstants.TARGET_USER, testUserName);
		policyDataMap.put(GatewayTestConstants.NOTIFY_EMAILID, notifyEmailId+"@"+suiteData.getTenantDomainName());
		policyDataMap.put(GatewayTestConstants.OBJECT_ACCESS, objectAccess);
		policyDataMap.put(GatewayTestConstants.ACTIVITY_ACCESS, activityAccess);
		PolicyAccessEnforcement.policyAccessEnforcement(suiteData, requestHeader, policyDataMap);
		PolicyAccessEnforcement.disablePolicy(suiteData, requestHeader, policyDataMap);
		replayLogsEPDV3(logFile);
		PolicyAccessEnforcement.deletePolicy(suiteData, requestHeader, policyDataMap);
		data.put("message", expectedMsg1);
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), "Logs does not match" ); 
	}
	
	@DataProvider
	public Object[][] _4() {
		return new Object[][]{
			//Policy Name                 App     Test User   admin   Object Access Activity Access      Activity Type     Object Type  Severity
			{ "ACCESS_ENFORCE_SESSION_NOBLOCK1", "BLOCK_SHARE", "Box",   "testuser1",  "admin",   "Session",    "Login",        "Session",  "SESSION", "informational", "BoxSession,Login.log" , "User logged in"       },
			{ "ACCESS_ENFORCE_SESSION_NOBLOCK2", "BLOCK_SHARE", "Box",   "testuser1",  "admin",   "Session",    "Logout",       "Session",  "SESSION", "informational", "BoxSession,LogOut.log" , "User logged out"     },
			{ "ACCESS_ENFORCE_SESSION_NOBLOCK3", "BLOCK_SHARE", "Box",   "testuser1",  "admin",   "Session",    "InvalidLogin", "Session",  "SESSION", "informational", "BoxSession,InvalidLogin.log","User Login Failed!" }
		};
	}
	
	//@Test(groups ={"TEST", "SESSION_POLICY"},dataProvider = "_policyAccessEnabledSessionAllApp")
	public void verifyUserAccessEnforcementPolicySessionAllAppsSelectiveUserActionBlock(String policyName, String action,  String saasApps,  String testUserName,  String notifyEmailId, String objectAccess, String activityAccess,   String activityType, String objectType, String severity, String logFile) throws Exception{
		Reporter.log("Validate User Session with policy enablement ", true);
		testUserName=testUserName+"@"+suiteData.getTenantDomainName();
		String expectedMsg="[ALERT] "+testUserName+ " attempted Activity: "+activityAccess+" on Object type: Session  using Platform";
		policyDataMap.clear();
		policyDataMap.put(GatewayTestConstants.POLICY_NAME, policyName);
		policyDataMap.put(GatewayTestConstants.APPLICATIONS, saasApps);
		policyDataMap.put(GatewayTestConstants.TARGET_USER, testUserName);
		policyDataMap.put(GatewayTestConstants.NOTIFY_EMAILID, notifyEmailId+"@"+suiteData.getTenantDomainName());
		policyDataMap.put(GatewayTestConstants.OBJECT_ACCESS, objectAccess);
		policyDataMap.put(GatewayTestConstants.ACTIVITY_ACCESS, activityAccess);
		policyDataMap.put(GatewayTestConstants.ACTIONS, action);
		PolicyAccessEnforcement.policyAccessEnforcement(suiteData, requestHeader, policyDataMap);
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
	public Object[][] _policyAccessEnabledSessionAllApp() {
		return new Object[][]{
			//Policy Name                 App     Test User   admin   Object Access Activity Access      Activity Type     Object Type  Severity
			{ "ACCESS_ENFORCE_SESSION1", "BLOCK_SHARE", "Box",   "__ALL_EL__",  "admin",   "Session",    "Logout",        "Policy Violation",  "SESSION", "critical", "BoxSession,LogOut.log"        },
		};
	}
	
	@Test(groups ={"TEST", "SESSION_POLICY"},dataProvider = "_policyAccessEnabledSessionGeneric")
	public void verifyUserAccessEnforcementPolicySessionSelectiveAppsAllUsersActionBlock(String policyName, String action, String saasApps, String allUser, String testUserName,  String notifyEmailId, String objectAccess, String activityAccess,   String activityType, String objectType, String severity, String logFile) throws Exception{
		Reporter.log("Validate User Session with policy enablement ", true);
		testUserName=testUserName+"@"+suiteData.getTenantDomainName();
		String expectedMsg="[ALERT] "+testUserName+ " attempted Activity: "+activityAccess+" on Object type: Session  using Platform";
		policyDataMap.clear();
		policyDataMap.put(GatewayTestConstants.POLICY_NAME, policyName);
		policyDataMap.put(GatewayTestConstants.APPLICATIONS, saasApps);
		policyDataMap.put(GatewayTestConstants.TARGET_USER, allUser);
		policyDataMap.put(GatewayTestConstants.NOTIFY_EMAILID, notifyEmailId+"@"+suiteData.getTenantDomainName());
		policyDataMap.put(GatewayTestConstants.OBJECT_ACCESS, objectAccess);
		policyDataMap.put(GatewayTestConstants.ACTIVITY_ACCESS, activityAccess);
		policyDataMap.put(GatewayTestConstants.ACTIONS, action);
		PolicyAccessEnforcement.policyAccessEnforcement(suiteData, requestHeader, policyDataMap);
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
	public Object[][] _policyAccessEnabledSessionGeneric() {
		return new Object[][]{
			//Policy Name                 App     Test User   admin   Object Access Activity Access      Activity Type     Object Type  Severity
			{ "ACCESS_ENFORCE_SESSION1", "BLOCK_SHARE", "Box",   "__ALL_EL__", "testuser1", "admin",   "Session",    "InvalidLogin",        "Policy Violation",  "SESSION", "critical", "BoxSession,InvalidLogin.log"        },
			
		};
	}
	
	
	@Test(groups ={"TEST","SESSION_POLICY"},dataProvider = "_policyAccessEnabledSessionAllAppsAllUsers")
	public void verifyUserAccessEnforcementPolicySessionAllAppsAllUsersActionBlock(String policyName, String action, String saasApps, String allUser, String testUserName,  String notifyEmailId, String objectAccess, String activityAccess,   String activityType, String objectType, String severity, String logFile) throws Exception{
		Reporter.log("Validate User Session with policy enablement ", true);
		testUserName=testUserName+"@"+suiteData.getTenantDomainName();
		String expectedMsg="[ALERT] "+testUserName+ " attempted Activity: "+activityAccess+" on Object type: Session  using Platform";
		policyDataMap.clear();
		policyDataMap.put(GatewayTestConstants.POLICY_NAME, policyName);
		policyDataMap.put(GatewayTestConstants.APPLICATIONS, saasApps);
		policyDataMap.put(GatewayTestConstants.TARGET_USER, allUser);
		policyDataMap.put(GatewayTestConstants.NOTIFY_EMAILID, notifyEmailId+"@"+suiteData.getTenantDomainName());
		policyDataMap.put(GatewayTestConstants.OBJECT_ACCESS, objectAccess);
		policyDataMap.put(GatewayTestConstants.ACTIVITY_ACCESS, activityAccess);
		policyDataMap.put(GatewayTestConstants.ACTIONS, action);
		PolicyAccessEnforcement.policyAccessEnforcement(suiteData, requestHeader, policyDataMap);
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
	public Object[][] _policyAccessEnabledSessionAllAppsAllUsers() {
		return new Object[][]{
			//Policy Name                 App     Test User   admin   Object Access Activity Access      Activity Type     Object Type  Severity
			{ "ACCESS_ENFORCE_SESSION1", "BLOCK_SHARE", "__ALL_EL__",   "__ALL_EL__", "testuser1", "admin",   "Session",    "InvalidLogin",        "Policy Violation",  "SESSION", "critical", "BoxSession,InvalidLogin.log" },
			
		};
	}
	
	
	@Test(groups ={"TEST","SESSION_POLICY"},dataProvider = "_policyAccessEnabledSessionSelectivePlaformMac")
	public void verifyUserAccessEnforcementPolicySessionSelectiveAppSelectiveUserActionSelectivePlatformMacBlockAndAlert(String policyName, String action, String saasApps,  String testUserName, String platform, String notifyEmailId, String objectAccess, String activityAccess,   String activityType, String objectType, String severity, String logFile) throws Exception{
		Reporter.log("Validate User Session with policy enablement ", true);
		testUserName=testUserName+"@"+suiteData.getTenantDomainName();
		String expectedMsg="[ALERT] "+testUserName+ " attempted Activity: "+activityAccess+" on Object type: Session  using Platform";
		policyDataMap.clear();
		policyDataMap.put(GatewayTestConstants.POLICY_NAME, policyName);
		policyDataMap.put(GatewayTestConstants.APPLICATIONS, saasApps);
		policyDataMap.put(GatewayTestConstants.TARGET_USER, testUserName);
		policyDataMap.put(GatewayTestConstants.NOTIFY_EMAILID, notifyEmailId+"@"+suiteData.getTenantDomainName());
		policyDataMap.put(GatewayTestConstants.OBJECT_ACCESS, objectAccess);
		policyDataMap.put(GatewayTestConstants.ACTIVITY_ACCESS, activityAccess);
		policyDataMap.put(GatewayTestConstants.PLATFORM_LIST, platform);
		policyDataMap.put(GatewayTestConstants.ACTIONS, action);
		
		PolicyAccessEnforcement.policyAccessEnforcement(suiteData, requestHeader, policyDataMap);	//PolicyAccessEnforcement.dummy(suiteData, requestHeader, policyDataMap);
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
	public Object[][] _policyAccessEnabledSessionSelectivePlaformMac() {
		return new Object[][]{
			//Policy Name                 App     Test User   admin   Object Access Activity Access      Activity Type     Object Type  Severity
			{ "ACCESS_ENFORCE_SESSION1", "BLOCK_SHARE", "Box",   "testuser1",  "Mac OS X", "admin",   "Session",    "InvalidLogin",        "Policy Violation",  "SESSION", "critical", "BoxSession,InvalidLogin.log"},
			
		};
	}
	
	
	@Test(groups ={"TEST","SESSION_POLICY"},dataProvider = "_policyAccessEnabledSessionSelectivePlaformBrowser")
	public void verifyUserAccessEnforcementPolicySessionSelectiveAppSelectiveUserActionSelectiveBrowserBlockAndAlert(String policyName, String action, String saasApps,  String testUserName, String platform, String browser, String notifyEmailId, String objectAccess, String activityAccess,   String activityType, String objectType, String severity, String logFile) throws Exception{
		Reporter.log("Validate User Session with policy enablement ", true);
		testUserName=testUserName+"@"+suiteData.getTenantDomainName();
		String expectedMsg="[ALERT] "+testUserName+ " attempted Activity: "+activityAccess+" on Object type: Session  using Platform";
		policyDataMap.clear();
		policyDataMap.put(GatewayTestConstants.POLICY_NAME, policyName);
		policyDataMap.put(GatewayTestConstants.APPLICATIONS, saasApps);
		policyDataMap.put(GatewayTestConstants.TARGET_USER, testUserName);
		policyDataMap.put(GatewayTestConstants.NOTIFY_EMAILID, notifyEmailId+"@"+suiteData.getTenantDomainName());
		policyDataMap.put(GatewayTestConstants.OBJECT_ACCESS, objectAccess);
		policyDataMap.put(GatewayTestConstants.ACTIVITY_ACCESS, activityAccess);
		policyDataMap.put(GatewayTestConstants.PLATFORM_LIST, platform);
		policyDataMap.put(GatewayTestConstants.BROWSER_LIST, browser);
		policyDataMap.put(GatewayTestConstants.ACTIONS, action);
		
		PolicyAccessEnforcement.policyAccessEnforcement(suiteData, requestHeader, policyDataMap);		
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
	public Object[][] _policyAccessEnabledSessionSelectivePlaformBrowser() {
		return new Object[][]{
			//Policy Name                 App     Test User   admin   Object Access Activity Access      Activity Type     Object Type  Severity
			{ "ACCESS_ENFORCE_SESSION1", "BLOCK_SHARE", "Box",   "testuser1",  "Mac OS X", "Firefox", "admin",   "Session",    "InvalidLogin",        "Policy Violation",  "SESSION", "critical", "BoxSession,InvalidLogin.log"        },
			
		};
	}
	
	
	@Test(groups ={"TEST","SESSION_POLICY"},dataProvider = "_policyAccessEnabledSessionSelectivePlaform")
	public void verifyUserAccessEnforcementPolicySessionSelectiveAppSelectiveUserActionSelectivePlatformBlockAndAlert1(String policyName, String action, String saasApps,  String testUserName, String platform, String browser, String notifyEmailId, String objectAccess, String activityAccess,   String activityType, String objectType, String severity, String logFile) throws Exception{
		Reporter.log("Validate User Session with policy enablement ", true);
		testUserName=testUserName+"@"+suiteData.getTenantDomainName();
		String expectedMsg="[ALERT] "+testUserName+ " attempted Activity: "+activityAccess+" on Object type: Session  using Platform";
		policyDataMap.clear();
		policyDataMap.put(GatewayTestConstants.POLICY_NAME, policyName);
		policyDataMap.put(GatewayTestConstants.APPLICATIONS, saasApps);
		policyDataMap.put(GatewayTestConstants.TARGET_USER, testUserName);
		policyDataMap.put(GatewayTestConstants.NOTIFY_EMAILID, notifyEmailId+"@"+suiteData.getTenantDomainName());
		policyDataMap.put(GatewayTestConstants.OBJECT_ACCESS, objectAccess);
		policyDataMap.put(GatewayTestConstants.ACTIVITY_ACCESS, activityAccess);
		policyDataMap.put(GatewayTestConstants.PLATFORM_LIST, platform);
		policyDataMap.put(GatewayTestConstants.BROWSER_LIST, browser);
		policyDataMap.put(GatewayTestConstants.ACTIONS, action);
		
		PolicyAccessEnforcement.policyAccessEnforcement(suiteData, requestHeader, policyDataMap);		
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
	public Object[][] _policyAccessEnabledSessionSelectivePlaform() {
		return new Object[][]{
			{ "ACCESS_ENFORCE_SESSION1", "BLOCK_SHARE", "Box",   "testuser1",  "Mac OS X", "Firefox",			 "admin",   "Session",    "Logout",        "Policy Violation",  "SESSION", "critical", "BoxSession,LogOut.log"        },
			{ "ACCESS_ENFORCE_SESSION2", "BLOCK_SHARE", "Box",   "testuser1",  "Mac OS X", "Firefox", 			 "admin",   "Session",    "Logout",        "Policy Violation",  "SESSION", "critical", "BoxSession,LogOut.log"        },
//			{ "ACCESS_ENFORCE_SESSION1", "BLOCK_SHARE", "Box",   "testuser1",  "Mac OS X", "Internet Explorer",	 "admin",   "Session",    "Logout",        "Policy Violation",  "SESSION", "critical", "BoxSession,LogOut.log"        },
//			{ "ACCESS_ENFORCE_SESSION2", "BLOCK_SHARE", "Box",   "testuser1",  "Mac OS X", "Internet Explorer",  "admin",   "Session",    "Logout",        "Policy Violation",  "SESSION", "critical", "BoxSession,LogOut.log"        },
//			{ "ACCESS_ENFORCE_SESSION1", "BLOCK_SHARE", "Box",   "testuser1",  "Mac OS X", "Chrome",		     "admin",   "Session",    "Logout",        "Policy Violation",  "SESSION", "critical", "BoxSession,LogOut.log"        },
//			{ "ACCESS_ENFORCE_SESSION2", "BLOCK_SHARE", "Box",   "testuser1",  "Mac OS X", "Chrome", 			 "admin",   "Session",    "Logout",        "Policy Violation",  "SESSION", "critical", "BoxSession,LogOut.log"        },
//			{ "ACCESS_ENFORCE_SESSION1", "BLOCK_SHARE", "Box",   "testuser1",  "Mac OS X", "Unknown",			 "admin",   "Session",    "Logout",        "Policy Violation",  "SESSION", "critical", "BoxSession,LogOut.log"        },
//			{ "ACCESS_ENFORCE_SESSION2", "BLOCK_SHARE", "Box",   "testuser1",  "Mac OS X", "Unknown", 			 "admin",   "Session",    "Logout",        "Policy Violation",  "SESSION", "critical", "BoxSession,LogOut.log"        },
			
			
		};
	}
	
	
	
	@Test(groups ={"TEST", "ACCESS_ENFORCEMENT_FILE_POLICY"},dataProvider = "_policyAccessEnabledFile")
	public void verifyUserAccessEnforcementPolicyForFileOperations(String policyName, String saasApps,  String testUserName,  String notifyEmailId, String objectAccess, String activityAccess,   String activityType, String fileName, String severity, String logFile) throws Exception{
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
	public Object[][] _policyAccessEnabledFile() {
		return new Object[][]{
			//Policy Name                 App     Test User   admin   Object Access Activity Access      Activity Type
		
			{ "ACCESS_ENFORCE_FILE2",   "Box",   "testuser1",  "admin",   "File",    	"Create",      			"Policy Violation",   "autotest.boxnote","critical", "FileBox,create_BoxNote.log" },
			{ "ACCESS_ENFORCE_FILE4",   "Box",   "testuser1",  "admin",   "File",   	"Delete Comment",   	"Policy Violation",  "test.mp3", "critical","FileBox,Delete_comment.log" },
			{ "ACCESS_ENFORCE_FILE8",   "Box",   "testuser1",  "admin",   "File",    	"Lock",        			"Policy Violation",  "test.7z", "critical","BoxFile,Locked_file_300_sec.log" },
			{ "ACCESS_ENFORCE_FILE11",  "Box",   "testuser1",  "admin",   "File",    	"Post Comment",      	"Policy Violation",   "test.mp3","critical","FileBox,Comment_on_test_mp3.log" },  
////			{ "ACCESS_ENFORCE_FILE12",  "Box",   "testuser1",  "admin",   "File",    	"Rename",        		"Policy Violation",   "new_name","critical", "1.png,2.png,renamefile.log" }, //"folder 4,folder 5,ItemRename.log"
////			{ "ACCESS_ENFORCE_FILE13",  "Box",   "testuser1",  "admin",   "File",    	"Search",        		"Policy Violation",   "","critical","searchfile1.log" },
			{ "ACCESS_ENFORCE_FILE14",  "Box",   "testuser1",  "admin",   "File",    	"Set Link Expiry",      "Policy Violation",  "test.mp3", "critical","BoxFile,Set_Expiration_shared_file.log" },
			{ "ACCESS_ENFORCE_FILE15",  "Box",   "testuser1",  "admin",   "File/Folder",    	"Share",        		"Policy Violation",   "test.pem","critical","BoxFile,share_pem.log" },
			{ "ACCESS_ENFORCE_FILE16",  "Box",   "testuser1",  "admin",   "File",    	"Unlock",        		"Policy Violation",   "test.doc","critical","BoxFile,Unlocked_file.log" },
			{ "ACCESS_ENFORCE_FILE18",  "Box",   "testuser1",  "admin",   "File",    	"Upload",       		"Policy Violation",   "test.mp3", "critical", "FileBox,Upload_test_mp3.log" },
			{ "ACCESS_ENFORCE_FILE19",  "Box",   "testuser1",  "admin",   "Folder",    	"View",        			"Policy Violation",   "all files","critical", "FileBox,View_app_page.log" },
			{ "ACCESS_ENFORCE_FILE1",   "Box",   "testuser1",  "admin",   "File/Folder", "Copy",        		"Policy Violation",  "auto.boxnote", "critical", "FileBox,copy.log" },
			{ "ACCESS_ENFORCE_FILE3",   "Box",   "testuser1",  "admin",   "File/Folder",    	"Delete",      			"Policy Violation",  "apps", "critical", "FileBox,Delete_bookmark.log" },
////			{ "ACCESS_ENFORCE_FILE9",   "Box",   "testuser1",  "admin",   "File",    	"Modify Permisions",	"Policy Violation",  "", "critical","View,Edit,EditPermission.log" },
			{ "ACCESS_ENFORCE_FILE10",  "Box",   "testuser1",  "admin",   "File/Folder", "Move",        		"Policy Violation",  "items", "critical","FileBox,Move_file1.log" },
////			{ "ACCESS_ENFORCE_FILE17",  "Box",   "testuser1",  "admin",   "File",    	"Unshare",        		"Policy Violation",  "", "critical","CFolder,Box,unshare.log"}, //"GFolder,RemoveShareLink.log" 
////			{ "ACCESS_ENFORCE_FILE7",   "Box",   "testuser1",  "admin",   "File",    	"Edit Properties",  	"Policy Violation",  "test1234.docx", "critical","File,Box,editProperties.log" },
			{ "ACCESS_ENFORCE_FILE5",   "Box",   "testuser1",  "admin",   "File",    	"Download",    			"Policy Violation",  "test.7z", "critical", "BoxFile,Download_File_Test_7z.log" },
			
		};
	}
	
	//@Test(groups ={"TEST", "ACCESS_ENFORCEMENT_FILE_POLICY"},dataProvider = "_policyAccessEnabledDisableDelete")
	public void verifyUserAccessEnforcementPolicyDeleteForFileOperations(String policyName, String saasApps,  String testUserName,  String notifyEmailId, String objectAccess, String activityAccess,   String activityType, String fileName, String severity, String logFile) throws Exception{
		Reporter.log("Validate User Upload the file/folder to folder with Policy Enabled", true);
		testUserName=testUserName+"@"+suiteData.getTenantDomainName();
		String expectedMsg="User created new google doc Googledoc.gdoc in folder All Files";
		policyDataMap.clear();
		policyDataMap.put(GatewayTestConstants.POLICY_NAME, policyName);
		policyDataMap.put(GatewayTestConstants.APPLICATIONS, saasApps);
		policyDataMap.put(GatewayTestConstants.TARGET_USER, testUserName);
		policyDataMap.put(GatewayTestConstants.NOTIFY_EMAILID, notifyEmailId+"@"+suiteData.getTenantDomainName());
		policyDataMap.put(GatewayTestConstants.OBJECT_ACCESS, objectAccess);
		policyDataMap.put(GatewayTestConstants.ACTIVITY_ACCESS, activityAccess);
		PolicyAccessEnforcement.accessEnforcementPolicyCreateEnable(suiteData, requestHeader, policyDataMap);
		PolicyAccessEnforcement.deletePolicy(suiteData, requestHeader, policyDataMap);
		replayLogs(logFile);
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), "Logs does not match" ); 
		
	}
	
	
	
	//@Test(groups ={"TEST", "ACCESS_ENFORCEMENT_FILE_POLICY"},dataProvider = "_policyAccessEnabledDisableDelete")
	public void verifyUserAccessEnforcementPolicyDisabledForFileOperations(String policyName, String saasApps,  String testUserName,  String notifyEmailId, String objectAccess, String activityAccess,   String activityType, String fileName, String severity, String logFile) throws Exception{
		Reporter.log("Validate User Upload the file/folder to folder with Policy Enabled", true);
		testUserName=testUserName+"@"+suiteData.getTenantDomainName();
		String expectedMsg="User created new google doc Googledoc.gdoc in folder All Files";
		policyDataMap.clear();
		policyDataMap.put(GatewayTestConstants.POLICY_NAME, policyName);
		policyDataMap.put(GatewayTestConstants.APPLICATIONS, saasApps);
		policyDataMap.put(GatewayTestConstants.TARGET_USER, testUserName);
		policyDataMap.put(GatewayTestConstants.NOTIFY_EMAILID, notifyEmailId+"@"+suiteData.getTenantDomainName());
		policyDataMap.put(GatewayTestConstants.OBJECT_ACCESS, objectAccess);
		policyDataMap.put(GatewayTestConstants.ACTIVITY_ACCESS, activityAccess);
		PolicyAccessEnforcement.accessEnforcementPolicyCreateEnable(suiteData, requestHeader, policyDataMap);
		PolicyAccessEnforcement.disablePolicy(suiteData, requestHeader, policyDataMap);
		replayLogs(logFile);
		PolicyAccessEnforcement.deletePolicy(suiteData, requestHeader, policyDataMap);
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), "Logs does not match" ); 
	}
	
	
	//@Test(groups ={"TEST", "ACCESS_ENFORCEMENT_FILE_POLICY"},dataProvider = "_policyAccessEnabledDisableDelete")
	public void verifyUserAccessEnforcementPolicyCreateDisableForFileOperations(String policyName, String saasApps,  String testUserName,  String notifyEmailId, String objectAccess, String activityAccess,   String activityType, String fileName, String severity, String logFile) throws Exception{
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
		PolicyAccessEnforcement.accessEnforcementPolicyCreate(suiteData, requestHeader, policyDataMap);
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
	public Object[][] _policyAccessEnabledDisableDelete() {
		return new Object[][]{
			//Policy Name                 App     Test User   admin   Object Access Activity Access      Activity Type
			{ "ACCESS_ENFORCE_FILE2",   "Box",   "testuser1",  "admin",   "File",    	"Create",      "Policy Violation",   "Googledoc.gdoc", "critical", "gdoc,CreateGDoc.log" },
		};
	}
	
	
	//@Test(groups ={"TEST", "ACCESS_ENFORCEMENT_FILE_POLICY"},dataProvider = "_policyAccessEnabledDisableDeleteOtherUser")
	public void verifyUserAccessEnforcementPolicyCreateDisableForFileOperationsForOtherUser(String policyName, String saasApps,  String testUserName, String secondUser, String notifyEmailId, String objectAccess, String activityAccess,   String activityType, String fileName, String severity, String logFile) throws Exception{
		Reporter.log("Validate User Upload the file/folder to folder with Policy Enabled", true);
		testUserName=testUserName+"@"+suiteData.getTenantDomainName();
		String expectedMsg="User created new google doc Googledoc.gdoc in folder All Files";
		policyDataMap.clear();
		policyDataMap.put(GatewayTestConstants.POLICY_NAME, policyName);
		policyDataMap.put(GatewayTestConstants.APPLICATIONS, saasApps);
		policyDataMap.put(GatewayTestConstants.TARGET_USER, secondUser);
		policyDataMap.put(GatewayTestConstants.NOTIFY_EMAILID, notifyEmailId+"@"+suiteData.getTenantDomainName());
		policyDataMap.put(GatewayTestConstants.OBJECT_ACCESS, objectAccess);
		policyDataMap.put(GatewayTestConstants.ACTIVITY_ACCESS, activityAccess);
		PolicyAccessEnforcement.accessEnforcementPolicyCreateEnable(suiteData, requestHeader, policyDataMap);
		replayLogs(logFile);
		PolicyAccessEnforcement.deletePolicy(suiteData, requestHeader, policyDataMap);
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), "Logs does not match" ); 
	}
	

	@DataProvider
	public Object[][] _policyAccessEnabledDisableDeleteOtherUser() {
		return new Object[][]{
			//Policy Name                 App     Test User   admin   Object Access Activity Access      Activity Type
			{ "ACCESS_ENFORCE_FILE1",   "Box",   "testuser1", "testuser2@gatewaybeatle.com", "admin",   "File",    	"Create",      			"Policy Violation",   "","critical", "gdoc,CreateGDoc.log" },
			{ "ACCESS_ENFORCE_FILE2",   "Box",   "testuser1", "testuser1@secondrygatewaybeatle.com", "admin",   "File",    	"Create",      			"Policy Violation",   "","critical", "gdoc,CreateGDoc.log" },
			
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
			{ "ACCESS_ENFORCE_FILE5",   "Box",   "testuser1",  "admin",   "__ALL_EL__",   "__ALL_EL__", 	"Download",    			"Policy Violation",   "critical", "TestFile,AllFiles,download_txt.log" },
			{ "ACCESS_ENFORCE_FILE18",  "Box",   "testuser1",  "admin",   "__ALL_EL__",    "__ALL_EL__",	"Upload",        		"Policy Violation",   "critical", "Test,AFolder,upload_txt.log" },
			
		};
	}
	
	
	
	
	@Test(groups ={"TEST", "ACCESS_ENFORCEMENT_FILE_POLICY"},dataProvider = "_policyAccessEnabledFileGenericV3")
	public void verifyUserAccessEnforcementPolicyEnabledForFileGenericV3(String policyName, String saasApps,  String testUserName,  String notifyEmailId, String objectAccess, String activityAccess,  String activity, String activityType,  String severity, String logFile) throws Exception{
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
	public Object[][] _policyAccessEnabledFileGenericV3() {
		return new Object[][]{
			//Policy Name                 App     Test User   admin   Object Access Activity Access      Activity Type
			{ "ACCESS_ENFORCE_FILE1",   "Box",   "testuser1",  "admin",   "__ALL_EL__",   "__ALL_EL__", 	"Copy",        			"Policy Violation",   "critical", "FileBox,copy.log" },
			{ "ACCESS_ENFORCE_FILE2",   "Box",   "testuser1",  "admin",   "__ALL_EL__",   "__ALL_EL__", 	"Create",      			"Policy Violation",   "critical", "FileBox,create_BoxNote.log" },
			{ "ACCESS_ENFORCE_FILE3",   "Box",   "testuser1",  "admin",   "__ALL_EL__",    "__ALL_EL__",	"Delete",      			"Policy Violation",   "critical", "FileBox,Delete_bookmark.log" },
			{ "ACCESS_ENFORCE_FILE4",   "Box",   "testuser1",  "admin",   "__ALL_EL__",   "__ALL_EL__",	    "Delete Comment",   	"Policy Violation",   "critical", "FileBox,Delete_comment.log" },
			{ "ACCESS_ENFORCE_FILE6",   "Box",   "testuser1",  "admin",   "__ALL_EL__",   "__ALL_EL__",	    "Edit Comment/Edit Tag",     	"Policy Violation",   "critical", "FileBox,Edit_tag.log" },
			{ "ACCESS_ENFORCE_FILE8",   "Box",   "testuser1",  "admin",   "__ALL_EL__",    "__ALL_EL__",	"Lock",        			"Policy Violation",   "critical","BoxFile,Locked_file_300_sec.log" },
			{ "ACCESS_ENFORCE_FILE9",   "Box",   "testuser1",  "admin",   "__ALL_EL__",    "__ALL_EL__",	"Modify Permisions",	"Policy Violation",   "critical","FileBox,Access_collaborator_with_viewer.log" },
			{ "ACCESS_ENFORCE_FILE10",  "Box",   "testuser1",  "admin",   "__ALL_EL__",   "__ALL_EL__", 	"Move",        			"Policy Violation",   "critical","FileBox,Move_file1.log" },
			{ "ACCESS_ENFORCE_FILE11",  "Box",   "testuser1",  "admin",   "__ALL_EL__",   "__ALL_EL__", 	"Post Comment",      	"Policy Violation",   "critical","FileBox,Comment_on_test_mp3.log" },  
			{ "ACCESS_ENFORCE_FILE12",  "Box",   "testuser1",  "admin",   "__ALL_EL__",    "__ALL_EL__",	"Rename",        		"Policy Violation",   "critical", "1.png,2.png,renamefile.log" }, //"folder 4,folder 5,ItemRename.log"
			{ "ACCESS_ENFORCE_FILE13",  "Box",   "testuser1",  "admin",   "__ALL_EL__",   "__ALL_EL__", 	"Search",        		"Policy Violation",   "critical","searchfile1.log" },
			{ "ACCESS_ENFORCE_FILE14",  "Box",   "testuser1",  "admin",   "__ALL_EL__",   "__ALL_EL__", 	"Set Link Expiry",      "Policy Violation",   "critical","BoxFile,Set_Expiration_shared_file.log" },
			{ "ACCESS_ENFORCE_FILE15",  "Box",   "testuser1",  "admin",   "__ALL_EL__",   "__ALL_EL__", 	"Share",        		"Policy Violation",   "critical","BoxFile,share_java.log" },
			{ "ACCESS_ENFORCE_FILE16",  "Box",   "testuser1",  "admin",   "__ALL_EL__",   "__ALL_EL__", 	"Unlock",        		"Policy Violation",   "critical","BoxFile,Unlocked_file.log" },
			{ "ACCESS_ENFORCE_FILE17",  "Box",   "testuser1",  "admin",   "__ALL_EL__",    "__ALL_EL__",	"Unshare",        		"Policy Violation",   "critical","GFolder,RemoveShareLink.log" },
			{ "ACCESS_ENFORCE_FILE19",  "Box",   "testuser1",  "admin",   "__ALL_EL__",    "__ALL_EL__",	"View",        			"Policy Violation",   "critical", "FileBox,View_app_page.log" }
			
			///{ "ACCESS_ENFORCE_FILE7",   "Box",   "testuser1",  "admin",   "File",    	"Edit Properties",  	"Policy Violation",   "critical","" },
		};
	}
	
	
   
	@Test(groups ={"TEST", "ACCESS_ENFORCEMENT_FOLDER_POLICY"},dataProvider = "_policyAccessEnabledFolder")
	public void verifyUserAccessEnforcementFolderPolicyEnabled(String policyName, String saasApps,  String testUserName,  String notifyEmailId, String objectAccess, String activityAccess,   String activityType, String severity, String objectName, String logFile) throws Exception{
		Reporter.log("Validate User Upload the file/folder to folder with Policy Enabled", true);
		testUserName=testUserName+"@"+suiteData.getTenantDomainName();
		policyDataMap.clear();
		policyDataMap.put(GatewayTestConstants.POLICY_NAME, policyName);
		policyDataMap.put(GatewayTestConstants.APPLICATIONS, saasApps);
		policyDataMap.put(GatewayTestConstants.TARGET_USER, testUserName);
		policyDataMap.put(GatewayTestConstants.NOTIFY_EMAILID, notifyEmailId+"@"+suiteData.getTenantDomainName());
		policyDataMap.put(GatewayTestConstants.OBJECT_ACCESS, objectAccess);
		policyDataMap.put(GatewayTestConstants.ACTIVITY_ACCESS, activityAccess);
		String expectedMsg="[ALERT] "+testUserName+ " attempted Activity: "+activityAccess+" on Object type: "+objectAccess+" name: "+objectName+" using Platform";
		PolicyAccessEnforcement.accessEnforcementPolicyCreateEnable(suiteData, requestHeader, policyDataMap);
		replayLogsWithDelay(logFile);
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
	public Object[][] _policyAccessEnabledFolder() {
		return new Object[][]{
			//Policy Name                 App     Test User   admin   Object Access Activity Access      Activity Type
    		{ "ACCESS_ENFORCE_FOLDER1",    "Box",   "testuser1",  "admin",   "Folder",    	"Create",      			"Policy Violation",   "critical",  "afolder", "AFolder,CreatePrivateFolder.log" },
			{ "ACCESS_ENFORCE_FOLDER2",    "Box",   "testuser1",  "admin",   "File/Folder",  "Download",            "Policy Violation",   "critical", "zfolder", "download,DownloadFolder.log"  },
			{ "ACCESS_ENFORCE_FOLDER3",    "Box",   "testuser1",  "admin",   "Folder",    	"Share",        		"Policy Violation",   "critical", "afolder", "AFolder,CreateFolder.log" },
			{ "ACCESS_ENFORCE_FOLDER4",    "Box",   "testuser1",  "admin",   "Folder",    	"Share",        		"Policy Violation",   "critical",  "item", "GFolder,ShareThisFolder.log" },
			{ "ACCESS_ENFORCE_FOLDER6",    "Box",   "testuser1",  "admin",   "Folder",    	"Unshare",        		"Policy Violation",   "critical", "zfolder", "Remove,ChangePermissionCollaborator.log" },
			{ "ACCESS_ENFORCE_FOLDER8",    "Box",   "testuser1",  "admin",   "Folder",    	"Rename",        		"Policy Violation",   "critical", "dfolder", "AFolder,Box,rename_Folder.log" },
			{ "ACCESS_ENFORCE_FOLDER9",    "Box",   "testuser1",  "admin",   "Folder",    	"Modify Permissions",	"Policy Violation",   "critical",  "zfolder", "Edit,View,EditPermission.log" },
			
			
			//{ "ACCESS_ENFORCE_FOLDER10",    "Box",   "testuser1",  "admin",   "Folder",    	"Copy",      			"Policy Violation",   "critical", "",  "AFolder,Box,copy_Folder.log" },
			//{ "ACCESS_ENFORCE_FOLDER11",    "Box",   "testuser1",  "admin",   "Folder",  		"Move",        			"Policy Violation",   "critical", "", "AFolder,Box,move_Folder.log" },    
			//{ "ACCESS_ENFORCE_FOLDER12",   "Box",   "testuser1",  "admin",   "Folder",  		"Delete",      			"Policy Violation",   "critical", "",  "delete,DeleteFolder.log" }, 
			//{ "ACCESS_ENFORCE_FOLDER13",   "Box",   "testuser1",  "admin",   "Folder",    	"Edit Properties",      "Policy Violation",   "critical", "",  "AFolder,Box,editProperties_Folder.log" },
			
			
//			{ "ACCESS_ENFORCE_FOLDER14",   "Box",   "testuser1",  "admin",   "Folder",   	"Delete Comment",   	"Policy Violation",   "critical", "" },
//			{ "ACCESS_ENFORCE_FOLDER15",  "Box",   "testuser1",  "admin",   "Folder",    	"Post Comment",        	"Policy Violation",   "critical","" },
//			{ "ACCESS_ENFORCE_FOLDER16",   "Box",   "testuser1",  "admin",   "Folder",   	"Edit",     			"Policy Violation",   "critical","" },
//			{ "ACCESS_ENFORCE_FOLDER17",   "Box",   "testuser1",  "admin",   "Folder",    	"Edit Comment",  		"Policy Violation",   "critical","" },
		}
		;
	}
	
	
	
	
	@Test(groups ={"TEST", "ACCESS_ENFORCEMENT_FOLDER_POLICY"},dataProvider = "_policyAccessEnforceEnabledFolder")
	public void verifyUserAccessEnforcementPolicyFolderEnabled(String policyName, String saasApps,  String testUserName,  String notifyEmailId, String objectAccess, String activityAccess,   String activityType, String severity, String objectName, String logFile) throws Exception{
		Reporter.log("Validate User Upload the file/folder to folder with Policy Enabled", true);
		testUserName=testUserName+"@"+suiteData.getTenantDomainName();
		policyDataMap.clear();
		policyDataMap.put(GatewayTestConstants.POLICY_NAME, policyName);
		policyDataMap.put(GatewayTestConstants.APPLICATIONS, saasApps);
		policyDataMap.put(GatewayTestConstants.TARGET_USER, testUserName);
		policyDataMap.put(GatewayTestConstants.NOTIFY_EMAILID, notifyEmailId+"@"+suiteData.getTenantDomainName());
		policyDataMap.put(GatewayTestConstants.OBJECT_ACCESS, objectAccess);
		policyDataMap.put(GatewayTestConstants.ACTIVITY_ACCESS, activityAccess);
		String expectedMsg="[ALERT] "+testUserName+ " attempted Activity: "+activityAccess+" on Object type: "+objectAccess+"  using Platform";
		PolicyAccessEnforcement.accessEnforcementPolicyCreateEnable(suiteData, requestHeader, policyDataMap);
		replayLogsWithDelay(logFile);
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
	public Object[][] _policyAccessEnforceEnabledFolder() {
		return new Object[][]{
			//Policy Name                 App     Test User   admin   Object Access Activity Access      Activity Type
			{ "ACCESS_ENFORCE_FOLDER5",    "Box",   "testuser1",  "admin",   "Folder",    	"Unshare",        		"Policy Violation",   "critical", "", "GFolder,RemoveShareLink.log" },
			{ "ACCESS_ENFORCE_FOLDER7",    "Box",   "testuser1",  "admin",   "Folder",    	"View",        			"Policy Violation",   "critical", "", "ViewAllFilesAndFolder.log" },
			
			
		}
		;
	}
	
	
	
	
	@Test(groups ={"TEST", "ACCESS_ENFORCEMENT_FOLDER_POLICY"},dataProvider = "_policyAccessEnabledFolderV3")
	public void verifyUserAccessEnforcementFolderPolicyEnabledV3(String policyName, String saasApps,  String testUserName,  String notifyEmailId, String objectAccess, String activityAccess,   String activityType, String severity, String objectName, String logFile) throws Exception{
		Reporter.log("Validate User Upload the file/folder to folder with Policy Enabled", true);
		testUserName=testUserName+"@"+suiteData.getTenantDomainName();
		policyDataMap.clear();
		policyDataMap.put(GatewayTestConstants.POLICY_NAME, policyName);
		policyDataMap.put(GatewayTestConstants.APPLICATIONS, saasApps);
		policyDataMap.put(GatewayTestConstants.TARGET_USER, testUserName);
		policyDataMap.put(GatewayTestConstants.NOTIFY_EMAILID, notifyEmailId+"@"+suiteData.getTenantDomainName());
		policyDataMap.put(GatewayTestConstants.OBJECT_ACCESS, objectAccess);
		policyDataMap.put(GatewayTestConstants.ACTIVITY_ACCESS, activityAccess);
		String expectedMsg="[ALERT] "+testUserName+ " attempted Activity: "+activityAccess+" on Object type: "+objectAccess+" name: "+objectName+" using Platform";
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
	public Object[][] _policyAccessEnabledFolderV3() {
		return new Object[][]{
			//Policy Name                 App     Test User   admin   Object Access Activity Access      Activity Type
			{ "ACCESS_ENFORCE_FOLDER8",    "Box",   "testuser1",  "admin",   "Folder",    	"Rename",        		"Policy Violation",   "critical", "zfolder", "FolderBox,Rename_folder.log" },
			
//			{ "ACCESS_ENFORCE_FOLDER10",    "Box",   "testuser1",  "admin",   "Folder",    	"Copy",      			"Policy Violation",   "critical", "test.pem",  "FileBox,copy.log" },
//			{ "ACCESS_ENFORCE_FOLDER11",    "Box",   "testuser1",  "admin",   "Folder",  		"Move",        			"Policy Violation",   "critical", "", "FileBox,Move_file1.log" },    
//			{ "ACCESS_ENFORCE_FOLDER12",   "Box",   "testuser1",  "admin",   "Folder",  		"Delete",      			"Policy Violation",   "critical", "gwauto",  "FolderBox,Deleted_folder.log" }, 
//			{ "ACCESS_ENFORCE_FOLDER14",   "Box",   "testuser1",  "admin",   "Folder",   	"Delete Comment",   	"Policy Violation",   "critical","test.mp3", "FileBox,Delete_comment.log" },
//			{ "ACCESS_ENFORCE_FOLDER15",   "Box",   "testuser1",  "admin",   "Folder",    	"Post Comment",        	"Policy Violation",   "critical","test.mp3","FileBox,Comment_on_test_mp3.log" },
		};
		
	}
	
	
	
	
	@Test(groups ={"TEST"},dataProvider = "_policyAccessEnabledFolderGeneric")
	public void verifyUserAccessEnforcementFolderPolicyEnabledGeneric(String policyName, String saasApps,  String testUserName,  String notifyEmailId, String objectAccess, String activityAccess, String activity,  String activityType, String severity, String logFile) throws Exception{
		Reporter.log("Validate User Upload the file/folder to folder with Policy Enabled", true);
		testUserName=testUserName+"@"+suiteData.getTenantDomainName();
		policyDataMap.clear();
		policyDataMap.put(GatewayTestConstants.POLICY_NAME, policyName);
		policyDataMap.put(GatewayTestConstants.APPLICATIONS, saasApps);
		policyDataMap.put(GatewayTestConstants.TARGET_USER, testUserName);
		policyDataMap.put(GatewayTestConstants.NOTIFY_EMAILID, notifyEmailId+"@"+suiteData.getTenantDomainName());
		policyDataMap.put(GatewayTestConstants.OBJECT_ACCESS, objectAccess);
		policyDataMap.put(GatewayTestConstants.ACTIVITY_ACCESS, activityAccess);
		String expectedMsg="[ALERT] "+testUserName+ " attempted access to cloud apps using Platform";
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
	public Object[][] _policyAccessEnabledFolderGeneric() {
		return new Object[][]{
			//Policy Name                 App     Test User   admin   Object Access Activity Access      Activity Type
			{ "ACCESS_ENFORCE_FOLDER3",   "Box",   "testuser1",  "admin",   "__ALL_EL__",    "__ALL_EL__",	"Download",    	"Policy Violation",   "critical","download,DownloadFolder.log"  },
			{ "ACCESS_ENFORCE_FOLDER6",   "Box",   "testuser1",  "admin",   "__ALL_EL__",    "__ALL_EL__",	"Unshare",      "Policy Violation",   "critical","GFolder,RemoveShareLink.log" },
			{ "ACCESS_ENFORCE_FOLDER7",   "Box",   "testuser1",  "admin",   "__ALL_EL__",    "__ALL_EL__",	"Unshare",      "Policy Violation",   "critical","Remove,ChangePermissionCollaborator.log" },
			{ "ACCESS_ENFORCE_FOLDER12",  "Box",   "testuser1",  "admin",   "__ALL_EL__",    "__ALL_EL__",  "Edit Properties",      "Policy Violation",   "critical", "AFolder,Box,editProperties_Folder.log" },
			{ "ACCESS_ENFORCE_FOLDER13",  "Box",   "testuser1",  "admin",   "__ALL_EL__",    "__ALL_EL__",  "Modify Permisions",	"Policy Violation",   "critical", "Edit,View,EditPermission.log" },
		};
	}
	
	
	
	
	@Test(groups ={"TEST"},dataProvider = "_policyAccessEnabledFolderGenericV3")
	public void verifyUserAccessEnforcementFolderPolicyEnabledGenericV3(String policyName, String saasApps,  String testUserName,  String notifyEmailId, String objectAccess, String activityAccess, String activity,  String activityType, String severity, String logFile) throws Exception{
		Reporter.log("Validate User Upload the file/folder to folder with Policy Enabled", true);
		testUserName=testUserName+"@"+suiteData.getTenantDomainName();
		policyDataMap.clear();
		policyDataMap.put(GatewayTestConstants.POLICY_NAME, policyName);
		policyDataMap.put(GatewayTestConstants.APPLICATIONS, saasApps);
		policyDataMap.put(GatewayTestConstants.TARGET_USER, testUserName);
		policyDataMap.put(GatewayTestConstants.NOTIFY_EMAILID, notifyEmailId+"@"+suiteData.getTenantDomainName());
		policyDataMap.put(GatewayTestConstants.OBJECT_ACCESS, objectAccess);
		policyDataMap.put(GatewayTestConstants.ACTIVITY_ACCESS, activityAccess);
		String expectedMsg="[ALERT] "+testUserName+ " attempted access to cloud apps using Platform";
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
	public Object[][] _policyAccessEnabledFolderGenericV3() {
		return new Object[][]{
			//Policy Name                 App     Test User   admin   Object Access Activity Access      Activity Type
			{ "ACCESS_ENFORCE_FOLDER1",   "Box",   "testuser1",  "admin",   "__ALL_EL__",    "__ALL_EL__",	"Create",      	"Policy Violation",   "critical", "FolderBox,Created_folder.log" },
			{ "ACCESS_ENFORCE_FOLDER2",   "Box",   "testuser1",  "admin",   "__ALL_EL__",    "__ALL_EL__",  "Copy",      	"Policy Violation",   "critical", "FileBox,copy.log" },
			{ "ACCESS_ENFORCE_FOLDER4",   "Box",   "testuser1",  "admin",   "__ALL_EL__",    "__ALL_EL__",	"Share",        "Policy Violation",   "critical","BoxFile,share_mp3.log" },			
			{ "ACCESS_ENFORCE_FOLDER5",   "Box",   "testuser1",  "admin",   "__ALL_EL__",    "__ALL_EL__",	"Share",        "Policy Violation",   "critical","BoxFile,share_txt.log" },
			{ "ACCESS_ENFORCE_FOLDER8",   "Box",   "testuser1",  "admin",   "__ALL_EL__",    "__ALL_EL__",	"View",        	"Policy Violation",   "critical","FileBox,View_app_page.log" },
			{ "ACCESS_ENFORCE_FOLDER9",   "Box",   "testuser1",  "admin",   "__ALL_EL__",    "__ALL_EL__", 	"Move",        			"Policy Violation",   "critical", "FileBox,Move_file1.log" },    //BFolder,AFolder,move.log
			{ "ACCESS_ENFORCE_FOLDER10",  "Box",   "testuser1",  "admin",   "__ALL_EL__",    "__ALL_EL__",  "Rename",        		"Policy Violation",   "critical", "FolderBox,Rename_folder.log" },
			{ "ACCESS_ENFORCE_FOLDER11",  "Box",   "testuser1",  "admin",   "__ALL_EL__",    "__ALL_EL__", 	"Delete",      			"Policy Violation",   "critical", "FolderBox,Deleted_folder.log" }, // //Bfolder,Afolder,deleteFolder.log
		};
	}
	
	
	
	
	
	@Test(groups ={"TEST"},dataProvider = "_policyAccessEnabledFileFolder")
	public void verifyUserAccessEnforcementFileFolderPolicyEnabled(String policyName, String saasApps,  String testUserName,  String notifyEmailId, String objectAccess, String activityAccess,   String activityType, String objectType, String severity, String objectName, String logFile) throws Exception{
		Reporter.log("Validate User Upload the file/folder to folder with Policy Enabled", true);
		testUserName=testUserName+"@"+suiteData.getTenantDomainName();
		policyDataMap.clear();
		policyDataMap.put(GatewayTestConstants.POLICY_NAME, policyName);
		policyDataMap.put(GatewayTestConstants.APPLICATIONS, saasApps);
		policyDataMap.put(GatewayTestConstants.TARGET_USER, testUserName);
		policyDataMap.put(GatewayTestConstants.NOTIFY_EMAILID, notifyEmailId+"@"+suiteData.getTenantDomainName());
		policyDataMap.put(GatewayTestConstants.OBJECT_ACCESS, objectAccess);
		policyDataMap.put(GatewayTestConstants.ACTIVITY_ACCESS, activityAccess);
		
		String expectedMsg="[ALERT] "+testUserName+ " attempted Activity: " +activityAccess+" on Object type: "+objectAccess+" name: "+objectName+" using Platform";
		
		
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
	public Object[][] _policyAccessEnabledFileFolder() {
		return new Object[][]{
			//Policy Name                	    App     Test User admin   Object Access 		Activity Access      Activity Type
    		{ "ACCESS_ENFORCE_FILE_FOLDER1",   "Box",   "testuser1",  "admin",   "File/Folder",    	"Delete",      			"Policy Violation",  "File", "critical", "gwauto", "FolderBox,Deleted_folder.log" },
			{ "ACCESS_ENFORCE_FILE_FOLDER2",  "Box",   "testuser1",  "admin",   "File/Folder",    	"Share",        		"Policy Violation",  "File", "critical", "business.txt", "BoxFile,share_txt.log" },
			{ "ACCESS_ENFORCE_FILE_FOLDER3",   "Box",   "testuser1",  "admin",   "File/Folder",    	"Copy",        			"Policy Violation",  "File", "critical", "auto.boxnote", "FileBox,copy.log" },
			{ "ACCESS_ENFORCE_FILE_FOLDER4",   "Box",   "testuser1",  "admin",   "File/Folder",    	"Create Tag",      	    "Policy Violation",  "File", "critical", "test.png", "FileBox,Add_single_tag.log" },
			{ "ACCESS_ENFORCE_FILE_FOLDER5",   "Box",   "testuser1",  "admin",   "File/Folder",   	"Delete Tag",   	    "Policy Violation",  "File", "critical", "test.png", "FileBox,Delete_tag.log"},
//			{ "ACCESS_ENFORCE_FILE_FOLDER6",   "Box",   "testuser1",  "admin",   "File",    	    "Download",    			"Policy Violation",  "File", "critical", "test.txt", "TestFile,AllFiles,download_txt.log" },			
//			{ "ACCESS_ENFORCE_FILE_FOLDER7",   "Box",   "testuser1",  "admin",   "File/Folder",    	"Restore",        	    "Policy Violation",  "File", "critical", "", "restore,RestoreFile.log" },
//			{ "ACCESS_ENFORCE_FILE_FOLDER8",   "Box",   "testuser1",  "admin",   "File/Folder",    	"Search",        	    "Policy Violation",  "File", "critical", "", "searchfile1.log" },			
			{ "ACCESS_ENFORCE_FILE_FOLDER10",  "Box",  "testuser1",  "admin",    "File/Folder",      "Move",        		"Policy Violation",  "File", "critical", "items", "FileBox,Move_file1.log" },
			{ "ACCESS_ENFORCE_FILE_FOLDER9",  "Box",  "testuser1",  "admin",    "Folder",          "Rename",        		"Policy Violation",  "File", "critical", "zfolder", "FolderBox,Rename_folder.log" },
			{ "ACCESS_ENFORCE_FILE_FOLDER11",  "Box",  "testuser1",  "admin",    "Folder",          "View",        		    "Policy Violation",  "File", "critical", "all files", "FileBox,View_app_page.log" },
		
			
			// { "ACCESS_ENFORCE_FILE_FOLDER12",   "Box",   "testuser1",  "admin",   "File/Folder",    	"Modify Permisions",	"Policy Violation",  "File", "critical", "Edit,View,EditPermission.log" },
		
		};
	}
	

	@AfterTest(groups ={"DELETE_ALL"})
	public void deleteAllPolicies() throws Exception{
		Reporter.log("Validate User Upload the file/folder to folder with Policy Enabled", true);
		PolicyAccessEnforcement.deleteAllPolicy(suiteData,  requestHeader);
	}
	
}
