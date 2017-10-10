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
import com.elastica.beatle.gateway.LogValidator;
import com.elastica.beatle.gateway.dto.GWForensicSearchResults;


/*******************Author**************
 * 
 * @author Afjal
 *
 */

public class O365SitesEpdTest extends CommonConfiguration {

	String currentTimeInJodaFormat;
	GWForensicSearchResults fsr;
	ArrayList<String> messages = new ArrayList<String>();
	ArrayList<String> objectTypeList = new ArrayList<String>();
	ArrayList<String> objectNameList = new ArrayList<String>();
	ArrayList<String> severityList = new ArrayList<String>();
	LogValidator logValidator;
	String userLitral="User";
	Map <String, String> data = new HashMap<String, String>();
	
	/******************************************************************
	 * Validate the login,logout and invalid login functionality in Office 365
	 * @param activityType
	 * @param objectType
	 * @param severity
	 * @param expectedMsg
	 * @throws IOException
	 * @throws Exception
	 */

	
	 @Test(groups ={"TEST"},dataProvider = "_shareSite")
	public void verify_User_ShareSite(String activityType, String objectType,  String UserId, String severity, String logFile) throws Exception{
		Reporter.log("Validate User shared site", true);
		String expectedMsg="User shared Team Site with "+UserId;
		replayLogs(logFile);
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
		
	}
	
	@DataProvider
	public Object[][] _shareSite() {
		return new Object[][]{
			//Activity type  Object Type  Object Name  Severity  message
			{ "Share", "Site", "testuser1@gatewayO365beatle.com", "informational", "site,testuser1,Share.log" },
			
		};
	}
	
	
	

	 @Test(groups ={"TEST"},dataProvider = "_createFolder")
	public void verify_User_createNewFolder(String activityType, String objectType, String AFolder, String severity, String logFile) throws Exception{
		Reporter.log("Validate User created new folder", true);
		String expectedMsg="User created Folder "+AFolder+" on Team Site";
		replayLogs(logFile);
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
		
	}
	
	@DataProvider
	public Object[][] _createFolder() {
		return new Object[][]{
			//Activity type  Object Type  Object Name  Severity  message
			{ "Create", "Folder", "GWFolder", "informational", "Folder,site_2,Create_GWFolder.log"},
			
		};
	}
	
	

	 @Test(groups ={"TEST"},dataProvider = "_shareFolder")
	public void verify_User_ShareFolderEdit(String activityType, String objectType, String fileName, String UserId, String severity, String logFile) throws Exception{
		Reporter.log("Validate User shared folder with Team site", true);
		String expectedMsg="User shared file/folder "+fileName+" with "+UserId+" of Team Site";
		replayLogs(logFile);
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
		
	}
	
	@DataProvider
	public Object[][] _shareFolder() {
		return new Object[][]{
			//Activity type  Object Type  Object Name  Severity  message
			{ "Share", "Folder", "GWAFolder", "testuser1@gatewayO365beatle.com", "informational", "Folder,site_2,Share_GWAFolder_Edit.log"},
			
		};
	}
	
	
	 @Test(groups ={"TEST"},dataProvider = "_shareFolderView")
		public void verify_User_ShareFolderView(String activityType, String objectType, String fileName, String UserId, String severity, String logFile) throws Exception{
			Reporter.log("Validate User shared folder with Team site", true);
			String expectedMsg="User shared file/folder "+fileName+" with "+UserId+" of Team Site";
			replayLogs(logFile);
			data.put("message", expectedMsg);
			data.put("account_type", "Internal");
			assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
			
		}
		
		@DataProvider
		public Object[][] _shareFolderView() {
			return new Object[][]{
				//Activity type  Object Type  Object Name  Severity  message
				{ "Share", "Folder", "GWAFolder", "testuser1@gatewayO365beatle.com", "informational", "Folder,site_2,Share_GWAFolder.log"},
				
			};
		}
	
	
	 @Test(groups ={"TEST"},dataProvider = "_renameFolder")
	public void verify_User_renameFile(String activityType, String objectType, String folder_name, String severity, String logFile) throws Exception{
		Reporter.log("Validate User renamed folder with Team site", true);
		String expectedMsg="User renamed a folder "+folder_name;
		replayLogs(logFile);
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
		
	}
	
	@DataProvider
	public Object[][] _renameFolder() {
		return new Object[][]{
			//Activity type  Object Type  Object Name  Severity  message
			{ "Rename", "Folder", "GWAFolder", "informational", "Folder,site_2,Rename_GWFolder.log"},
			
		};
		
	}
		
		
	
	 @Test(groups ={"TEST"},dataProvider = "_deleteFolder")
		public void verify_deleteItem(String activityType, String objectType, String FolderName, String severity, String logFile) throws Exception{
			Reporter.log("Validate User deleted folder from Team site", true);
			String expectedMsg="User deleted "+FolderName+" from Team Site";
			replayLogs(logFile);
			data.put("message", expectedMsg);
			data.put("account_type", "Internal");
			assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
			
		}
		
		@DataProvider
		public Object[][] _deleteFolder() {
			return new Object[][]{
				//Activity type  Object Type  Object Name  Severity  message
				{ "Delete", "Document/Folder", "", "informational", "Folder,site_2,Delete_GWAFolder.log"},
				
			};
			
		}
		
		
		
	
		@Test(groups ={"TEST"},dataProvider = "_createDocument")
		public void verify_CreateDocument(String activityType, String objectType, String file_name, String severity, String logFile) throws Exception{
			Reporter.log("Validate User browsed Home at Microsoft Office 365 Portal ", true);
			String expectedMsg="user created a document file "+file_name;
			replayLogs(logFile);
			data.put("message", expectedMsg);
			data.put("account_type", "Internal");
			assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
			
		}
		
		@DataProvider
		public Object[][] _createDocument() {
			return new Object[][]{
				//Activity type  Object Type  Object Name  Severity  message
				{ "Create", "Document", "Document", "informational", "Document,site_2,create_TestDoc.log"},
				
			};
			
		}
		
		@Test(groups ={"TEST"},dataProvider = "_RenameDocument")
		public void verify_RenameDocument(String activityType, String objectType, String file_name, String severity, String logFile) throws Exception{
			Reporter.log("Validate User browsed Home at Microsoft Office 365 Portal ", true);
			String expectedMsg="user renamed document file "+file_name;
			replayLogs(logFile);
			data.put("message", expectedMsg);
			data.put("account_type", "Internal");
			assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
			
		}
		
		@DataProvider
		public Object[][] _RenameDocument() {
			return new Object[][]{
				//Activity type  Object Type  Object Name  Severity  message
				{ "Rename", "Document", "TestDoc", "informational", "Document,site_2,Rename_WordDoc.log" },
				
			};
			
		}
		
		
		

		@Test(groups ={"TEST"},dataProvider = "_shareDocument")
		public void verify_ShareDocumentEdit(String activityType, String objectType, String fileName,  String UserId, String severity, String logFile) throws Exception{
			Reporter.log("Validate User shared team site", true);
			String expectedMsg="User shared file/folder "+fileName+" with "+UserId+" of Team Site";
			replayLogs(logFile);
			data.put("message", expectedMsg);
			data.put("account_type", "Internal");
			assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
			
		}
		
		@DataProvider
		public Object[][] _shareDocument() {
			return new Object[][]{
				//Activity type  Object Type  Object Name  Severity  message
				{ "Share", "Document", "GWAFolder", "testuser1@gatewayO365beatle.com", "informational", "Document,site_2,Share_WordDoc_Edit.log"},
				
			};
			
		}
		
		
		@Test(groups ={"TEST"},dataProvider = "_shareDocumentView")
		public void verify_ShareDocumentView(String activityType, String objectType, String fileName, String UserId, String severity, String logFile) throws Exception{
			Reporter.log("Validate User browsed Home at Microsoft Office 365 Portal ", true);
			String expectedMsg="User shared file/folder "+fileName+" with "+UserId+" of Team Site";
			replayLogs(logFile);
			data.put("message", expectedMsg);
			data.put("account_type", "Internal");
			assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
			
		}
		
		@DataProvider
		public Object[][] _shareDocumentView() {
			return new Object[][]{
				//Activity type  Object Type  Object Name  Severity  message
				{ "Share", "Document", "GWAFolder", "testuser1@gatewayO365beatle.com", "informational", "Document,site_2,Share_WordDoc.log"},
				
			};
		}
		

		@Test(groups ={"TEST"},dataProvider = "_uploadDocument")
		public void verify_userUploadDocument(String activityType, String objectType, String object_name, String severity, String logFile) throws Exception{
			Reporter.log("Validate User browsed Home at Microsoft Office 365 Portal ", true);
			String expectedMsg="User uploaded file named "+object_name+" to Shared Documents for Team Site";
			replayLogs(logFile);
			data.put("message", expectedMsg);
			data.put("account_type", "Internal");
			assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
			
		}
		
		@DataProvider
		public Object[][] _uploadDocument() {
			return new Object[][]{
				//Activity type  Object Type  Object Name  Severity  message
				{ "Upload", "Document", "WordDoc.docx", "informational", "Document,site_2,Upload_WordDoc.log"}
				
			};
			
		}
		
		 @Test(groups ={"TEST"},dataProvider = "_deleteDocument")
			public void verify_deleteDocument(String activityType, String objectType, String FolderName, String severity, String logFile) throws Exception{
				Reporter.log("Validate User deleted folder from Team site", true);
				String expectedMsg="User deleted "+FolderName+" from Team Site";
				replayLogs(logFile);
				data.put("message", expectedMsg);
				data.put("account_type", "Internal");
				assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
				
			}
			
			@DataProvider
			public Object[][] _deleteDocument() {
				return new Object[][]{
					//Activity type  Object Type  Object Name  Severity  message
					{ "Delete", "Document/Folder", "", "informational", "Document,site_2,Delete_WordDoc.log"},
					
				};
				
			}
	
}
		
	
	