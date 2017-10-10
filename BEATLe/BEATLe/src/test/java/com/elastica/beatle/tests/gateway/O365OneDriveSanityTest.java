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
 * @author mohd afjal
 *
 */

public class O365OneDriveSanityTest extends CommonConfiguration {

	String currentTimeInJodaFormat;
	GWForensicSearchResults fsr;
	ArrayList<String> messages = new ArrayList<String>();
	ArrayList<String> objectTypeList = new ArrayList<String>();
	ArrayList<String> objectNameList = new ArrayList<String>();
	ArrayList<String> severityList = new ArrayList<String>();
	LogValidator logValidator;
	String userLitral="User";
	Map <String, String> data = new HashMap<String, String>();
	String policyName="PolicyFT_FileType";
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
	
	
	
	 @Test(groups ={"SANITY"},dataProvider = "_676")
	public void verify_User_copyFiletoFolder(String activityType, String objectType, String object_name, String AFolder, String severity, String logFile) throws Exception{
		Reporter.log("Validate User browsed Home at Microsoft Office 365 Portal ", true);
		String expectedMsg="User copied item "+object_name+" to "+AFolder;
		replayLogs(logFile);
		data.clear();
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
		
	}
	
	@DataProvider
	public Object[][] _676() {
		return new Object[][]{
			//Activity type  Object Type  Object Name  Severity  message
			{ "Copy", "Document","Test.txt","root folder","informational", "O365,AFolder,copy_Test_txt.log"},
			
		};
	}
	
	
	

	 @Test(groups ={"SANITY"},dataProvider = "_677")
	public void verify_User_createNewFolder(String activityType, String objectType, String AFolder, String severity, String logFile) throws Exception{
		Reporter.log("Validate User browsed Home at Microsoft Office 365 Portal ", true);
		String expectedMsg="User created a new folder "+AFolder;
		replayLogs(logFile);
		data.clear();
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
		
	}
	
	@DataProvider
	public Object[][] _677() {
		return new Object[][]{
			//Activity type  Object Type  Object Name  Severity  message
			{ "Create", "Folder","AFolder","informational", "O365,AFolder,create_new_folder.log"},
			
		};
	}
	
	

	 @Test(groups ={"SANITY"},dataProvider = "_678")
	public void verify_User_moveFileToFolder(String activityType, String objectType, String object_name, String AFolder, String severity, String logFile) throws Exception{
		Reporter.log("Validate User browsed Home at Microsoft Office 365 Portal ", true);
		String expectedMsg="User moved item "+object_name+" to "+AFolder;
		replayLogs(logFile);
		data.clear();
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
		
	}
	
	@DataProvider
	public Object[][] _678() {
		return new Object[][]{
			//Activity type  Object Type  Object Name  Severity  message
			{ "Move", "Document","Test.txt","root folder","informational", "O365,AFolder,move_Test_txt.log"},
			
		};
	}
		
		

		@Test(groups ={"SANITY"},dataProvider = "_683")
		public void verify_userDownloadPDF(String activityType, String objectType, String object_name, String severity, String logFile) throws Exception{
			Reporter.log("Validate User browsed Home at Microsoft Office 365 Portal ", true);
			String expectedMsg="User downloaded "+object_name;
			replayLogs(logFile);
			data.clear();
			data.put("message", expectedMsg);
			data.put("account_type", "Internal");
			assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
			
		}
		
		@DataProvider
		public Object[][] _683() {
			return new Object[][]{
				//Activity type  Object Type  Object Name  Severity  message
				{ "Download", "Document","Test.pdf","informational", "O365,Test,download_pdf.log"}
				
			};
			
		}
		
		
		
		
		@Test(groups ={"SANITY"},dataProvider = "_685")
		public void verify_UserShareExternalUserWithElasticaDomain(String activityType, String objectType, String email_name, String object_name, String severity, String logFile) throws Exception{
			Reporter.log("Validate User browsed Home at Microsoft Office 365 Portal ", true);
			String expectedMsg="User sent email invitation(s) to "+email_name+" for "+object_name;
			replayLogsEPDV3(logFile);
			data.put("message", expectedMsg);
			data.put("account_type", "Internal");
			assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
			
		}
		
		@DataProvider
		public Object[][] _685() {
			return new Object[][]{
				//Activity type  Object Type  Object Name  Severity  message
				{ "Share", "Document/Folder","mohd.afjal@elastica.co","Test.docx.","informational", "OneDrive,Share_File_With_External_User.log"},
				
			};
			
		}
		
		@Test(groups ={"SANITY"},dataProvider = "_685P")
		public void verify_UserShareExternalUserWithPersonal(String activityType, String objectType, String email_name, String object_name, String severity, String logFile) throws Exception{
			Reporter.log("Validate User browsed Home at Microsoft Office 365 Portal ", true);
			String expectedMsg="User sent email invitation(s) to "+email_name+" for "+object_name;
			replayLogsEPDV3(logFile);
			data.put("message", expectedMsg);
			data.put("account_type", "Internal");
			assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
			
		}
		
		@DataProvider
		public Object[][] _685P() {
			return new Object[][]{
				//Activity type  Object Type  Object Name  Severity  message
				{ "Share", "Document/Folder","mdafjaldoon@gmail.com","Test.exe.","informational", "OneDrive,Share_File_With_External_UserP.log"},
				
			};
			
		}
		
		
		@Test(groups ={"SANITY"},dataProvider = "_739")
		public void verify_UserRestoreOneDrive(String activityType, String objectType, String severity, String logFile) throws Exception{
			Reporter.log("Validate User emptied Recycle Bin", true);
			String expectedMsg="User opened Recycle Bin";
			replayLogsEPDV3(logFile);
			data.clear();
			data.put("message", expectedMsg);
			data.put("account_type", "Internal");
			assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
			
		}
		
		@DataProvider
		public Object[][] _739() {
			return new Object[][]{
				//Activity type  Object Type  Object Name  Severity  message
				{ "Restore", "Recycle Bin", "informational", "RecycleBin,Restore_OneNote.log"},
				
			};
		}
		
		
	//	@Test(groups ={"SANITY", "ACCESS_ENFORCEMENT_FILE_POLICY"},dataProvider = "_policyAccessEnabledFolder")
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
		public Object[][] _policyAccessEnabledFolder() {
			return new Object[][]{
				//Policy Name                 App     Test User   admin   Object Access Activity Access      Activity Type
			
				{ "ACCESS_ENFORCE_FOLDER1",   "Office 365",   "testuser1",  "admin",   "Folder",    	"Create",   "Policy Violation",   "gwtest1", "critical",  "OneDrive,Create_folder.log" },
			
			};
		}
		
		
		//@Test(groups ={"SANITY", "ACCESS_ENFORCEMENT_FILE_POLICY"},dataProvider = "_policyAccessEnabledDocument")
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
		public Object[][] _policyAccessEnabledDocument() {
			return new Object[][]{
				//Policy Name                	    App     Test User admin   Object Access 		Activity Access      Activity Type
				{ "ACCESS_ENFORCE_DOCUMENT2",   "Office 365",   "testuser1",  "admin",   "Document",    	"Delete",      "Policy Violation",   "gwautomation.docx", "critical",  "DocumentOneD,Delete_doc.log" },
			
			};
		}
	
}
		
		
		
		
	
	
	
	
	
	