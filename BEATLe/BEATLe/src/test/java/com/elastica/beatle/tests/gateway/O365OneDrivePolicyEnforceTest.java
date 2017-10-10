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

public class O365OneDrivePolicyEnforceTest extends CommonConfiguration {

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

	
	//@Test(groups ={"POLICY"},dataProvider = "_policyAccessEnabledSession")
	public void verifyUserAccessEnforcementPolicyEnabledSession(String policyName, String saasApps,  String testUserName,  String notifyEmailId, String objectAccess, String activityAccess,   String activityType, String objectType, String severity) throws Exception{
		Reporter.log("Validate User Upload the file/folder to folder with Policy Enabled", true);
		String expectedMsg="[ALERT] "+testUserName+ " attempted to upload content:"+fileName+" violating policy:"+policyName;
		policyDataMap.clear();
		policyDataMap.put(GatewayTestConstants.POLICY_NAME, policyName);
		policyDataMap.put(GatewayTestConstants.APPLICATIONS, saasApps);
		policyDataMap.put(GatewayTestConstants.TARGET_USER, testUserName+"@"+suiteData.getTenantDomainName());
		policyDataMap.put(GatewayTestConstants.NOTIFY_EMAILID, notifyEmailId+"@"+suiteData.getTenantDomainName());
		policyDataMap.put(GatewayTestConstants.OBJECT_ACCESS, objectAccess);
		policyDataMap.put(GatewayTestConstants.ACTIVITY_ACCESS, activityAccess);
		
		PolicyAccessEnforcement.accessEnforcementPolicyCreateEnable(suiteData, requestHeader, policyDataMap);
	
	}
	
	@DataProvider
	public Object[][] _policyAccessEnabledSession() {
		return new Object[][]{
			//Policy Name                 App     Test User   admin   Object Access Activity Access      Activity Type
			{ "ACCESS_ENFORCE_SESSION1",  "Box",   "user1",  "admin",   "SESSION",    "Login",        "Policy Violation",  "File", "critical" },
			{ "ACCESS_ENFORCE_SESSION2",  "Box",   "user1",  "admin",   "SESSION",    "Logout",       "Policy Violation",  "File", "critical" },
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
	
	@Test(groups ={"TEST", "ACCESS_ENFORCEMENT_FILE_POLICY"},dataProvider = "_policyAccessEnabledFolder")
	public void verifyUserAccessEnforcementPolicyForFolderOperations(String policyName, String saasApps,  String testUserName,  String notifyEmailId, String objectAccess, String activityAccess,   String activityType, String fileName, String severity, String logFile) throws Exception{
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
		
			{ "ACCESS_ENFORCE_FOLDER1",   "Office 365",   "testuser1",  "admin",   "Folder",    	"Create",   "Policy Violation",   "gwtest1", "critical",  "OneDrive,Create_folder.log" },
			{ "ACCESS_ENFORCE_FOLDER2",   "Office 365",   "testuser1",  "admin",   "Folder/OneNote notebook",    	"Delete",   "Policy Violation",   "",         "critical", "OneDrive,Delete_folder.log" },
		
		};
	}
	
	
	
	@Test(groups ={"TEST", "ACCESS_ENFORCEMENT_FILE_POLICY"},dataProvider = "_policyAccessEnabledDocumentFolder")
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
	public Object[][] _policyAccessEnabledDocumentFolder() {
		return new Object[][]{
			//Policy Name                 App     Test User   admin   Object Access Activity Access      Activity Type
		
			{ "ACCESS_ENFORCE_DOCUMENT_FOLDER3",   "Office 365",   "testuser1",  "admin",   "Document/Folder",    	"Copy",     "Policy Violation",   "u3",         "critical", "OneDrive,Copy_folder.log" },
			{ "ACCESS_ENFORCE_DOCUMENT_FOLDER4",   "Office 365",   "testuser1",  "admin",   "Document/Folder",    	"Move",     "Policy Violation",   "agwfolder",         "critical", "OneDrive,Move_folder.log" },
			{ "ACCESS_ENFORCE_DOCUMENT_FOLDER5",   "Office 365",   "testuser1",  "admin",   "Document/Folder",    	"Rename",   "Policy Violation",   "agwfolder",         "critical", "OneDrive,Rename_folder.log" },
		
		};
	}
	
	
    
	
	
	
	/******************************************
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
	
	@Test(groups ={"TEST", "ACCESS_ENFORCEMENT_FILE_POLICY"},dataProvider = "_policyAccessEnabledDocument")
	public void verifyUserAccessEnforcementDocumentPolicyEnabled(String policyName, String saasApps,  String testUserName,  String notifyEmailId, String objectAccess, String activityAccess,   String activityType, String objectType, String severity, String logFile) throws Exception{
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
	public Object[][] _policyAccessEnabledDocument() {
		return new Object[][]{
			//Policy Name                	    App     Test User admin   Object Access 		Activity Access      Activity Type
			{ "ACCESS_ENFORCE_DOCUMENT2",   "Office 365",   "testuser1",  "admin",   "Document",    	"Delete",      "Policy Violation",   "gwautomation.docx", "critical",  "DocumentOneD,Delete_doc.log" },
			{ "ACCESS_ENFORCE_DOCUMENT3",   "Office 365",   "testuser1",  "admin",   "Document",    	"Download",    "Policy Violation",   "gwautomation.docx", "critical",  "DocumentOneD,Download_doc.log" },
			{ "ACCESS_ENFORCE_DOCUMENT7",   "Office 365",   "testuser1",  "admin",   "Document",    	"Upload",      "Policy Violation",   "gwautomation.docx", "critical",  "DocumentOneD,Upload_docx.log" },
		
		};
	}
	
	@Test(groups ={"TEST", "ACCESS_ENFORCEMENT_FILE_POLICY"},dataProvider = "_policyAccessEnabledDocumentEdit")
	public void verifyUserAccessEnforcementDocumentEditPolicyEnabled(String policyName, String saasApps,  String testUserName,  String notifyEmailId, String objectAccess, String activityAccess,   String activityType, String objectType, String severity, String logFile) throws Exception{
		Reporter.log("Validate User Upload the file/folder to folder with Policy Enabled", true);
		testUserName=testUserName+"@"+suiteData.getTenantDomainName();
		String expectedMsg="[ALERT]  attempted Activity: "+activityAccess+" on Object type: "+objectAccess+"  using Platform";
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
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(activityType, severity), data)), "Logs does not match" ); 
		
	
	}
	
	@DataProvider
	public Object[][] _policyAccessEnabledDocumentEdit() {
		return new Object[][]{
			//Policy Name                	    App     Test User admin   Object Access 		Activity Access      Activity Type
			{ "ACCESS_ENFORCE_DOCUMENT6",   "Office 365",   "testuser1",  "admin",   "Document",    	"Edit",       "Policy Violation",   "",   "critical",  "WordDocument,Edit_IN_Word_Online.log" },
		
		};
	}
	
	
	
	@Test(groups ={"TEST", "ACCESS_ENFORCEMENT_FILE_POLICY"},dataProvider = "_policyAccessEnabledRecycleBinEmpty")
	public void verifyUserAccessEnforcementPolicyForRecycleBinEmptyOperations(String policyName, String saasApps,  String testUserName,  String notifyEmailId, String objectAccess, String activityAccess,   String activityType, String fileName, String severity, String logFile) throws Exception{
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
	public Object[][] _policyAccessEnabledRecycleBinEmpty() {
		return new Object[][]{
			//Policy Name                 App     Test User   admin   Object Access Activity Access      Activity Type
		
			{ "ACCESS_ENFORCE_EMPTY_RECYCLE",   "Office 365",   "testuser1",  "admin",   "Recycle Bin",    	"Empty",   "Policy Violation",   "gwtest1", "critical",  "RecycleBin,Empty_OneNote.log" },
		
		};
	}

	
	
	
}
