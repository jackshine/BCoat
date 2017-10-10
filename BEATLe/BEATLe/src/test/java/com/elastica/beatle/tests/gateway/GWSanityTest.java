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
import com.elastica.beatle.protect.ProtectFunctions;


/*******************Author**************
 * 
 * @author usman
 * Every test has an id which is inherited from Elastica-ADB
 */

public class GWSanityTest extends CommonConfiguration {

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
	ProtectFunctions protectFunctions = new ProtectFunctions();
	
	
	/******************************************************************
	 * Validate the login,logout and invalid login functionality in Box
	 * @param activityType
	 * @param objectType
	 * @param severity
	 * @param expectedMsg
	 * @throws IOException
	 * @throws Exception
	 */
	
	
	
	

	@Test(groups ={"SANITY"},dataProvider = "_348_SANITY")
	public void verify_User_uploaded_file_to_folder_348_Sanity(String activityType, String objectType, String objectName,String destinationFolder, String severity, String logFile) throws Exception{
		Reporter.log("Validate User Upload the file/folder to folder", true);
		String expectedMsg="User uploaded file "+objectName+" to folder "+destinationFolder;
		replayLogs(logFile);
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(activityType, severity), data)), activityType+" Logs does not match" ); 
		
	}
	
	@DataProvider
	public Object[][] _348_SANITY() {
		return new Object[][]{
			//Activity type  Object Type  Object Name, DestinationDolder  Severity  message
			{ "Upload",    "File", "Test.txt", 		"", "informational", "Test,AFolder,upload_txt.log"},
		};
	}
	
	
	
	@Test(groups ={"SANITY"},dataProvider = "_354_SANITY")
	public void verify_User_invited_email_for_collaboration_with_permissions_354_Sanity(String activityType, String objectType, String email, String severity, String objectName, String permission, String logFile) throws IOException, Exception{
		Reporter.log("Validate User invited email X for collaboration of folder Y with permission Z", true);
		String expectedLog="User invited "+email+" for collaboration of folder "+objectName+" with permissions "+permission;
		replayLogsEPDV3(logFile);
		data.put("message", expectedLog);
		data.put("account_type", "Internal");
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(activityType, severity), data)), activityType+" Logs does not match" ); 
		
	}
	
	@DataProvider
	public Object[][] _354_SANITY() {
		return new Object[][]{
			//Activity type  Object Type  Object Name Severity   Shared with   message
			{ "Share",  "Folder",      "usman.kec@gmail.com", "informational", "5812269e-6200-45ca-ba07-7b30bf7261e2", "Previewer Uploader", "FolderBox,Invite_collaborator_with_previewer_uploader.log"}
		};
	}

	@Test(groups ={"SANITY"},dataProvider = "_385_SANITY")
	public void verify_User_downloaded_File_385_Sanity(String activityType, String objectType, String objectName, String severity, String logFile) throws IOException, Exception{
		Reporter.log("Validate User downlaod a file", true);
		String expectedLog="User downloaded "+objectName;
		replayLogs(logFile);
		data.put("message", expectedLog);
		data.put("account_type", "Internal");
		
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(activityType, severity), data)), activityType+" Logs does not match" );
	}

	@DataProvider
	public Object[][] _385_SANITY() {
		return new Object[][]{
				//Activity type  Object Type node Object Name  Severity
				{ "Download",  "File/Folder", "Test.txt", 	"informational", 	"TestFile,AllFiles,download_txt.log"  },
		};
	}

		@Test(groups ={"TEST", "FAIL1","REGRESSION","BOX", "SHARE",  "P1", "SANITY"},dataProvider = "_387")
		public void verify_User_deleted_objects_Files_Folders_387(String activityType, String objectType, String objectName, String severity, String logFile) throws IOException, Exception{
			Reporter.log("Validate User delete files objects", true);
			String expectedLog="User deleted object(s) "+objectName;
			replayLogsEPDV3(logFile);
			data.put("message", expectedLog);
			data.put("account_type", "Internal");
			assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(activityType, severity), data)), activityType+" Logs does not match" ); 
			
		}
		
		@DataProvider
		public Object[][] _387() {
			return new Object[][]{
				//Activity type  Object Type node Object Name  Severity
				{ "Delete",  "Folder", "GWAuto", "informational" , "FolderBox,Deleted_folder.log"},
				
			};
		}
		
		@Test(groups ={"TEST","REGRESSION","BOX", "MOVE","QAVPC","P1", "SANITY"},dataProvider = "_393")
		public void verify_User_moved_items_to_Folder_393(String activityType, String objectType, String objectName, String severity, String targetFolder, String logFile) throws IOException, Exception{
			Reporter.log("Validate User moved items to folder", true);
			String expectedLog="User moved item(s) "+objectName+ " to "+targetFolder;
			replayLogsEPDV3(logFile);
			data.put("message", expectedLog);
			data.put("account_type", "Internal");
			assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(activityType, severity), data)), activityType+" Logs does not match" ); 
			
		}
		
		@DataProvider
		public Object[][] _393() {
			return new Object[][]{
				//Activity type  Object Type node Object Name  Severity
				{ "Move",  "File", "Test.ott", "informational", "f4a2b168-8e63-4649-a7f9-f1fd62cfde21", "FileBox,Move_file1.log"} 
			};
		}
		
		
		@Test(groups ={"TEST","REGRESSION","BOX", "DOWNLOAD", "QAVPC", "P1", "SANITY"},dataProvider = "_417")
		public void verify_User_created_new_boxnote_in_Folder_417(String activityType, String objectType, String objectName, String severity, String targetFolder, String logFile) throws IOException, Exception{
			Reporter.log("Validate User created a boxnote in folder", true);
			String expectedLog="User created new boxnote "+objectName+" in "+targetFolder;
			replayLogsEPDV3(logFile);
			data.put("message", expectedLog);
			data.put("account_type", "Internal");
			assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(activityType, severity), data)), activityType+" Logs does not match" ); 
			
		}
		
		@DataProvider
		public Object[][] _417() {
			return new Object[][]{
				//Activity type  Object Type  Object Name  severity       
				{ "Create",   "File", "AutoTest.boxnote",  "informational", "All Files", "FileBox,create_BoxNote.log"} //targetFolder
			};
		}
		
		
		@Test(groups ={"SANITY", "ACCESS_ENFORCEMENT_FILE_POLICY"},dataProvider = "_policyAccessEnabledFile_Sanity")
		public void verifyUserAccessEnforcementPolicyForFileOperations_Sanity(String policyName, String saasApps,  String testUserName,  String notifyEmailId, String objectAccess, String activityAccess,   String activityType,  String severity, String ObjectName, String logFile) throws Exception{
			Reporter.log("Validate User Upload the file/folder to folder with Policy Enabled", true);
			testUserName=testUserName+"@"+suiteData.getTenantDomainName();
			String expectedMsg="[ALERT] "+testUserName+ " attempted Activity: "+activityAccess+" on Object type: "+objectAccess+" name: "+ObjectName+" using Platform";
			policyDataMap.clear();
			policyDataMap.put(GatewayTestConstants.POLICY_NAME, policyName);
			policyDataMap.put(GatewayTestConstants.APPLICATIONS, saasApps);
			policyDataMap.put(GatewayTestConstants.TARGET_USER, testUserName);
			policyDataMap.put(GatewayTestConstants.NOTIFY_EMAILID, notifyEmailId+"@"+suiteData.getTenantDomainName());
			policyDataMap.put(GatewayTestConstants.OBJECT_ACCESS, objectAccess);
			policyDataMap.put(GatewayTestConstants.ACTIVITY_ACCESS, activityAccess);
			//PolicyAccessEnforcement.deletePolicy(suiteData, requestHeader, policyDataMap);
			PolicyAccessEnforcement.accessEnforcementPolicyCreateEnable(suiteData, requestHeader, policyDataMap);
			replayLogsEPDV3(logFile);
			PolicyAccessEnforcement.deletePolicy(suiteData, requestHeader, policyDataMap);
			data.put("message", expectedMsg);
			data.put("account_type", "Internal");
			assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(activityType, severity), data)), activityType+" Logs does not match" ); 
			
		}
		
		@DataProvider
		public Object[][] _policyAccessEnabledFile_Sanity() {
			return new Object[][]{
				//Policy Name                 App     Test User   admin   Object Access Activity Access      Activity Type
			
				{ "ACCESS_ENFORCE_FILE2",   "Box",   "testuser1",  "admin",   "File",    	"Create",      			"Policy Violation",   "critical","autotest.boxnote", "FileBox,create_BoxNote.log" },
			};
		}
		
		
		@Test(groups ={"SANITY"},dataProvider = "_policySharingFileSanity")
		public void verifyFileSharingPolicyEnabled_Sanity(String policyName, String saasApps,  String sharedBy,   String shareWith, String notifyEmailId, String fileType, String fileName,  String activityType, String objectType, String severity, String logFile) throws Exception{
			Reporter.log("Validate User Upload the file/folder to folder with Policy Enabled", true);
			testUserName=sharedBy+"@"+suiteData.getTenantDomainName();
			String expectedMsg="[ALERT] "+testUserName+" attempted to share content:"+fileName.toLowerCase()+"."+fileType+"with external user(s):ALL_EL__ violating policy:"+policyName;
			policyDataMap.clear();
			policyDataMap.put(GatewayTestConstants.POLICY_NAME, policyName);
			policyDataMap.put(GatewayTestConstants.APPLICATIONS, saasApps);
			policyDataMap.put(GatewayTestConstants.SHARED_BY,testUserName );
			policyDataMap.put(GatewayTestConstants.NOTIFY_EMAILID, notifyEmailId+"@"+suiteData.getTenantDomainName());
			policyDataMap.put(GatewayTestConstants.SHARE_WITH, shareWith);
			policyDataMap.put(GatewayTestConstants.FILE_TYPE, fileType);
			policyDataMap.put(GatewayTestConstants.FILE_NAME, fileName);
			//PolicyAccessEnforcement.deletePolicy(suiteData, requestHeader, policyDataMap);
			PolicyAccessEnforcement.fileSharingPolicyCreateEnable(suiteData, requestHeader, policyDataMap);
			replayLogsEPDV3(logFile);
			PolicyAccessEnforcement.deletePolicy(suiteData, requestHeader, policyDataMap);
			data.put("message", expectedMsg);
			data.put("account_type", "Internal");
			assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
			
		}
		
		@DataProvider
		public Object[][] _policySharingFileSanity() {
			return new Object[][]{
				//Policy Name        App     Shared by  Share with  						admin     File Type    File Name      			Activity Type
				
				{ "FILE_SHARING1",   "Box",   "testuser1",  "__ALL_EL__", 	"admin",   "pdf",    	"Test", "Policy Violation",  "File", "critical", "BoxFile,share_pdf.log"}
			};
		}
		
		
		@Test(groups ={"SANITY"}, dataProvider = "_policyFileTransferupload_Sanity")
		public void verifyFileTransferPolicyEnabledUpload_Sanity(String policyName, String saasApps,  String testUserName,  String notifyEmailId, String transferType, String fileType, String fileName,  String activityType, String objectType, String severity, String logFile) throws Exception{
			Reporter.log("Validate User Upload the file/folder to folder with Policy Enabled", true);
			testUserName=testUserName+"@"+suiteData.getTenantDomainName();
			String expectedMsg="[ALERT] "+testUserName+ " attempted to "+transferType+" content:"+fileName.toLowerCase()+"."+fileType+" violating policy:"+policyName;
			policyDataMap.clear();
			policyDataMap.put(GatewayTestConstants.POLICY_NAME, policyName);
			policyDataMap.put(GatewayTestConstants.APPLICATIONS, saasApps);
			policyDataMap.put(GatewayTestConstants.TARGET_USER, testUserName);
			policyDataMap.put(GatewayTestConstants.NOTIFY_EMAILID, notifyEmailId+"@"+suiteData.getTenantDomainName());
			policyDataMap.put(GatewayTestConstants.TRANSFER_TYPE, transferType);
			policyDataMap.put(GatewayTestConstants.FILE_TYPE, fileType);
			policyDataMap.put(GatewayTestConstants.FILE_NAME, fileName);
			//PolicyAccessEnforcement.deletePolicy(suiteData, requestHeader, policyDataMap);
			PolicyAccessEnforcement.fileTransferPolicyCreateEnable(suiteData, requestHeader, policyDataMap);
			replayLogs(logFile);
			PolicyAccessEnforcement.deletePolicy(suiteData, requestHeader, policyDataMap);
			data.put("message", expectedMsg);
			data.put("account_type", "Internal");
			assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(activityType, severity), data)), activityType+" Logs does not match" ); 
		
		}
		
		@DataProvider
		public Object[][] _policyFileTransferupload_Sanity() {
			return new Object[][]{
				//Policy Name                 App     Test User   admin     Object Access Activity Access      Activity Type     Object Type  Severity
				{ "FILE_TRANSFER_UPLOAD1",   "Box",   "testuser1",  "admin",  "upload",  "txt",    		"Test",        "Policy Violation",  "Upload", "critical", "Test,AFolder,upload_txt.log" },
			};
		}

		@Test(groups ={"SANITY"}, dataProvider = "_policyFileTransferDownload_Sanity")
		public void verifyFileTransferPolicyEnabled_Sanity(String policyName, String saasApps,  String testUserName,  String notifyEmailId, String transferType, String fileType, String fileName,  String activityType, String objectType, String severity, String logFile) throws Exception{
			Reporter.log("Validate User Upload the file/folder to folder with Policy Enabled", true);

			testUserName=testUserName+"@"+suiteData.getTenantDomainName();
			String expectedMsg="[ALERT] "+testUserName+ " attempted to "+transferType+" content:"+fileName.toLowerCase()+"."+fileType+" violating policy:"+policyName;
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
			assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(activityType, severity), data)), activityType+" Logs does not match" ); 
		}

		@DataProvider
		public Object[][] _policyFileTransferDownload_Sanity() {
			return new Object[][] {
				//Policy Name                 App     Test User   admin     Object Access Activity Access      Activity Type     Object Type  Severity
				{ "FILE_TRANSFER_DOWNLOAD12",  "Box",   "testuser1",  "admin",  "download", "txt",    "Test",      "Policy Violation",  "Download", "critical","TestFile,AllFiles,download_txt.log" },
			};
		}
}
