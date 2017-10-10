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

public class GWGDriveSanityTest extends CommonConfiguration {

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
	
	
	
	 @Test(groups ={"SANITY"},dataProvider = "_createDocFile")
		public void verify_User_Create_Docs_File(String activityType, String objectType, String fileName, String DestiName, String severity, String logFile) throws Exception{
			Reporter.log("Validate User created docs file", true);
			String expectedMsg="User Created new "+fileName+" in "+DestiName;
			replayLogs(logFile);
			data.put("message", expectedMsg);
			data.put("account_type", "Internal");
			assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
			
		}
		
		@DataProvider
		public Object[][] _createDocFile() {
			return new Object[][]{
				//Activity type  Object Type  Object Name  Severity  message
				{ "Create", "File/Folder","Document","root", "informational", "File,Admin_2,Create_Document_root.log"},
				
			};
		}
		
		 @Test(groups ={"SANITY"},dataProvider = "_openFileFolderApp")
			public void verify_User_Open_with_App(String activityType, String objectType, String App_name, String severity, String logFile) throws Exception{
				Reporter.log("Validate User open folder with App", true);
				String expectedMsg="User connected app named "+App_name+" amp";
				replayLogs(logFile);
				data.put("message", expectedMsg);
				data.put("account_type", "Internal");
				assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
				
			}
			
			@DataProvider
			public Object[][] _openFileFolderApp() {
				return new Object[][]{
					//Activity type  Object Type  Object Name  Severity  message
					{ "Connect", "Application", "SMART", "informational", "Google Drive,Folder_2,Open_with_App.log"},
					
				};
			}
			
			@Test(groups ={"SANITY"},dataProvider = "_shareFileFolder")
			public void verify_User_Share_Folder_Edit(String activityType, String objectType, String folder_name, String email, String severity, String logFile) throws Exception{
				Reporter.log("Validate User shared folder with email", true);
				String expectedMsg="User shared file(s)/folder(s) "+folder_name+" with "+email;
				replayLogsEPDV3(logFile);
				data.put("message", expectedMsg);
				data.put("account_type", "Internal");
				assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
				
			}
			
			@DataProvider
			public Object[][] _shareFileFolder() {
				return new Object[][]{
					//Activity type  Object Type  Object Name  Severity  message
					{ "Share", "File/Folder","", "Mohd Afjal &lt;mohd.afjal@elastica.co&gt;", "informational", "FolderGD,Share_Folder_Mail_Edit.log"},

				};
			}
			
			@Test(groups ={"SANITY"},dataProvider = "_unshareFileFolder")
			public void verify_User_Unshare_Folder(String activityType, String objectType, String email, String itemName, String severity, String logFile) throws Exception{
				Reporter.log("Validate User unshare folder", true);
				String expectedMsg="User removed "+email+" from collaborators list for item "+itemName;
				replayLogsEPDV3(logFile);
				data.put("message", expectedMsg);
				data.put("account_type", "Internal");
				assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
				
			}
			
			@DataProvider
			public Object[][] _unshareFileFolder() {
				return new Object[][]{
					//Activity type  Object Type  Object Name  Severity  message
					{ "Unshare", "File/Folder", "admin@gatewaybeatle.com, mohd.afjal@elastica.co", "", "informational", "FolderGD,Unshare_Folder.log"},
					
				};
			}
			
			@Test(groups ={"SANITY"},dataProvider = "_updateCollaborator")
			public void verify_User_Update_Collaborator_View_Folder(String activityType, String objectType, String email, String itemName, String severity, String logFile) throws Exception{
				Reporter.log("Validate User share folder", true);
				String expectedMsg="User added "+email+" as collaborators who can View item "+itemName;
				replayLogsEPDV3(logFile);
				data.put("message", expectedMsg);
				data.put("account_type", "Internal");
				assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
				
			}
			
			@DataProvider
			public Object[][] _updateCollaborator() {
				return new Object[][]{
					//Activity type  Object Type  Object Name  Severity  message
					{ "Share", "File/Folder", "", "", "informational", "FolderGD,Update_Folder_Collaborator_View.log"},
					
				};
			}
			
			@Test(groups ={"SANITY"},dataProvider = "_downloadFolder")
			public void verify_User_Download_Folder(String activityType, String objectType, String ObjectName,  String severity, String logFile) throws Exception{
				Reporter.log("Validate User share the link", true);
				String expectedMsg="[ERROR] The following activity failed:  User downloaded contents of folder as filename "+ObjectName;
				replayLogs(logFile);
				data.put("message", expectedMsg);
				data.put("account_type", "Internal");
				assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
				
			}
			
			@DataProvider
			public Object[][] _downloadFolder() {
				return new Object[][]{
					//Activity type  Object Type  Object Name  Severity  message
					{ "Download", "File/Folder", "AFolder-2016-05-13.zip", "informational", "Google Drive,Folder_2,Download1.log"},
					
				};
			}
			
			@Test(groups ={"SANITY"},dataProvider = "_deleteForever")
			public void verify_User_Delete_Forever(String activityType, String objectType, String ObjectName,  String severity, String logFile) throws Exception{
				Reporter.log("Validate User deleted forever folder", true);
				String expectedMsg="User deleted item(s) "+ObjectName+" permanantly";
				replayLogs(logFile);
				data.put("message", expectedMsg);
				data.put("account_type", "Internal");
				assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
				
			}
			
			@DataProvider
			public Object[][] _deleteForever() {
				return new Object[][]{
					//Activity type  Object Type  Object Name  Severity  message
					{ "Delete", "File", "ARFolder", "informational", "Google Drive,Folder_2,Detete_Forever.log"},
					
				};
			}
			
//			@Test(groups ={"SANITY", "ACCESS_ENFORCEMENT_FILE_POLICY"},dataProvider = "_sanityPolicyAccessDocEnabled")
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
			public Object[][] _sanityPolicyAccessDocEnabled() {
				return new Object[][]{
					//Policy Name                 App     Test User   admin   Object Access Activity Access      Activity Type
				
					{ "ACCESS_ENFORCE_DOCUMENT",   "Google Drive",   "testuser4",  "admin",   "Document",  "Create", "Policy Violation",   "untitled document", "critical", "File,Admin_2,Create_Document_root.log" },
			
				};
			}
			
//			@Test(groups ={"SANITY"},dataProvider = "_sanityPolicyFileSharing")
			public void verifyFileSharingPolicyLinkEnabled(String policyName, String saasApps,  String sharedBy,   String shareWith, String notifyEmailId, String fileType, String fileName,  String activityType, String objectType, String severity, String logFile) throws Exception{
				Reporter.log("Validate User Upload the file/folder to folder with Policy Enabled", true);
				testUserName=sharedBy+"@"+suiteData.getTenantDomainName();
				String expectedMsg="[ALERT] "+testUserName+ " attempted to share content:"+fileName.toLowerCase()+"."+fileType+"with external user(s):mohd afjal &lt;mohd.afjal@elastica.co&gt;, mohd.afjal@elastica.co violating policy:"+policyName;
				policyDataMap.clear();
				policyDataMap.put(GatewayTestConstants.POLICY_NAME, policyName);
				policyDataMap.put(GatewayTestConstants.APPLICATIONS, saasApps);
				policyDataMap.put(GatewayTestConstants.SHARED_BY,testUserName );
				policyDataMap.put(GatewayTestConstants.NOTIFY_EMAILID, notifyEmailId+"@"+suiteData.getTenantDomainName());
				policyDataMap.put(GatewayTestConstants.SHARE_WITH, shareWith);
				policyDataMap.put(GatewayTestConstants.FILE_TYPE, fileType);
				policyDataMap.put(GatewayTestConstants.FILE_NAME, fileName);
				PolicyAccessEnforcement.fileSharingPolicyCreateEnable(suiteData, requestHeader, policyDataMap);
				replayLogs(logFile);
				PolicyAccessEnforcement.deletePolicy(suiteData, requestHeader, policyDataMap);

				data.put("message", expectedMsg);
				data.put("account_type", "Internal");
				data.put("policy_type", "FileSharingGateway");
				data.put("policy_action", "ALERT");
				data.put("action_taken", "block,");
				data.put("_PolicyViolated", policyName);
				assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), "Logs does not match" ); 

				
			}
			
			@DataProvider
			public Object[][] _sanityPolicyFileSharing() {
				return new Object[][]{
					//Policy Name        App     Shared by  Share with  						admin     File Type    File Name      			Activity Type
					
					{ "FILE_SHARING",  "Google Drive",   "testuser4",  "__ALL_EL__", 	"admin",   "pdf",       "test", "Policy Violation",  "File", "critical", "Google Drive,Admin_2,Share_File_pdf.log" },
					
				};
			}
			
//			@Test(groups ={"SANITY"}, dataProvider = "_sanityPolicyFileupload")
			public void verifyEmailFileTransferPolicyEnabledUpload(String policyName, String saasApps,  String testUserName,  String notifyEmailId, String transferType, String fileType, String fileName,  String activityType, String objectType, String severity, String logFile) throws Exception{
				Reporter.log("Validate User Upload the file/folder to folder with Policy Enabled", true);
				testUserName=testUserName+"@"+suiteData.getTenantDomainName();
				String expectedMsg="[ALERT] "+testUserName+ " attempted to "+transferType+" content:"+fileName.toLowerCase()+"."+fileType+" violating policy:"+policyName;
				System.out.println("expectedMsg: "+expectedMsg);
				policyDataMap.clear();
				policyDataMap.put(GatewayTestConstants.POLICY_NAME, policyName);
				policyDataMap.put(GatewayTestConstants.APPLICATIONS, saasApps);
				policyDataMap.put(GatewayTestConstants.TARGET_USER, testUserName);
				policyDataMap.put(GatewayTestConstants.NOTIFY_EMAILID, notifyEmailId+"@"+suiteData.getTenantDomainName());
				policyDataMap.put(GatewayTestConstants.TRANSFER_TYPE, transferType);
				policyDataMap.put(GatewayTestConstants.FILE_TYPE, fileType);
				policyDataMap.put(GatewayTestConstants.FILE_NAME, fileName);
				PolicyAccessEnforcement.fileTransferPolicyCreateEnable(suiteData, requestHeader, policyDataMap);
				replayLogs(logFile);
				PolicyAccessEnforcement.deletePolicy(suiteData, requestHeader, policyDataMap);
				data.put("message", expectedMsg);
				data.put("account_type", "Internal");
				data.put("policy_type", "FileTransfer");
				data.put("policy_action", "ALERT");
				data.put("action_taken", "block,email,");
				data.put("_PolicyViolated", policyName);
				assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), "Logs does not match" );
				
				}
			
			@DataProvider
			public Object[][] _sanityPolicyFileupload() {
				return new Object[][]{
					//Policy Name                 App           Test User      admin   Object Access Activity Access  Activity Type     Object Type               Severity
					{ "FILE_TRANSFER_UPLOAD",   "Google Drive", "testuser4",  "admin",  "upload",  "doc",   "Test", "Policy Violation",  "Upload", "critical", "File,GDrive_2,Upload_Test_doc.log" },
					
				};
			}	
			
	
	
}
		
	