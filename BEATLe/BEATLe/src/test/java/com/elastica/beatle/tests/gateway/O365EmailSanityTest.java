package com.elastica.beatle.tests.gateway;

import org.testng.annotations.Test;
import org.testng.annotations.DataProvider;
import static org.testng.Assert.assertTrue;
import java.io.IOException;
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


/*******************Author**************
 * 
 * @author Afjal
 *
 */

public class O365EmailSanityTest extends CommonConfiguration {

	String currentTimeInJodaFormat;
	GWForensicSearchResults fsr;
	ArrayList<String> messages = new ArrayList<String>();
	ArrayList<String> objectTypeList = new ArrayList<String>();
	ArrayList<String> objectNameList = new ArrayList<String>();
	ArrayList<String> severityList = new ArrayList<String>();
	LogValidator logValidator;
	String userLitral="User";
	Map <String, String> data = new HashMap<String, String>();
	Map<String, String>policyDataMap= new HashMap<String, String>(); 
	
	
	
	
	/******************************************************************
	 * Validate the login,logout and invalid login functionality in Box
	 * @param activityType
	 * @param objectType
	 * @param severity
	 * @param expectedMsg
	 * @throws IOException
	 * @throws Exception
	 */
	
	@Test(groups ={"SANITY"},dataProvider = "_sendEmail")
	public void verify_User_SendEmail(String activityType, String objectType, String email, String subject, String severity, String logFile) throws Exception{
		Reporter.log("Validate User sent an email ", true);
		String expectedMsg="User sent an email to "+email+" with subject "+subject;
		replayLogsEPDV3(logFile);
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
		
	}
	
	@DataProvider
	public Object[][] _sendEmail() {
		return new Object[][]{
			//Activity type  Object Type  Object Name  Severity  message
			{ "Send", "Email", "mohd.afjal@elastica.co", "Message", "informational", "EmailO365,SendWithAttach.log"},
			
		};
	}
	
	
	@Test(groups ={"SANITY"},dataProvider = "_sendEmailWithAttach")
	public void verify_User_SendEmailWithAttach(String activityType, String objectType, String email, String objectName,  String severity, String logFile) throws Exception{
		Reporter.log("Validate User sent an email ", true);
		String expectedMsg="User sent an email to "+email+" with subject "+objectName;
		replayLogsEPDV3(logFile);
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
		
	}
	
	@DataProvider
	public Object[][] _sendEmailWithAttach() {
		return new Object[][]{
			//Activity type  Object Type  Object Name  Severity  message
			{ "Send", "Email", "mohd.afjal@elastica.co", "Message", "informational", "EmailO365,SendWithAttach.log"},
			
		};
	}
	
	@Test(groups ={"SANITY"},dataProvider = "_createFolder")
	public void verify_User_createFolder(String activityType, String objectType, String folderName, String severity, String logFile) throws Exception{
		Reporter.log("Validate User created folder ", true);
		String expectedMsg="User created a folder named "+folderName;
		replayLogsWithDelay(logFile);
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
		
	}
	
	@DataProvider
	public Object[][] _createFolder() {
		return new Object[][]{
			//Activity type  Object Type  Object Name  Severity  message
			{ "Create", "Folder", "SFolder", "informational", "Folder,Office 365,Create.log"},
			
		};
	}
	
	@Test(groups ={"SANITY"},dataProvider = "_deleteFolder")
	public void verify_User_deleteFolder(String activityType, String objectType, String folderName, String severity, String logFile) throws Exception{
		Reporter.log("Validate User deleted folder ", true);
		String expectedMsg="User deleted folder "+folderName;
		replayLogsWithDelay(logFile);
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
		
	}
	
	@DataProvider
	public Object[][] _deleteFolder() {
		return new Object[][]{
			//Activity type  Object Type  Object Name  Severity  message
			{ "Delete", "Folder", "SFolder", "informational", "Folder,Office 365,Delete.log"},
			
		};
	}
	
	@Test(groups ={"SANITY1"},dataProvider = "_emptyFolder")
	public void verify_User_emptyFolder(String activityType, String objectType, String folderName, String severity, String logFile) throws Exception{
		Reporter.log("Validate User emptied folder ", true);
		String expectedMsg="User emptied folder "+folderName;
		replayLogsWithDelay(logFile);
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
		
	}
	
	@DataProvider
	public Object[][] _emptyFolder() {
		return new Object[][]{
			//Activity type  Object Type  Object Name  Severity  message
			{ "Empty", "Folder", "SFolder", "informational", "Folder,Office 365,Empty.log"},
			
		};
	}
	
//	@Test(groups ={"SANITY", "ACCESS_ENFORCEMENT_FILE_POLICY"},dataProvider = "_sanitySandPolicyAccessEnabled")
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
	public Object[][] _sanitySandPolicyAccessEnabled() {
		return new Object[][]{
			//Policy Name                	    App     Test User admin   Object Access 		Activity Access      Activity Type
			{ "ACCESS_ENFORCE_EMAIL",   "Office 365",   "testuser1",  "admin",   "Email",    	"Send",        "Policy Violation",   "data", "critical",  "Mail,Send_Mail_With_External_UserE.log" },
		
		};
	}
	
	
	
}
			