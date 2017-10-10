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

public class GoogleDriveEpdTest extends CommonConfiguration {

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
	 * Validate the login,logout and invalid login functionality in Box
	 * @param activityType
	 * @param objectType
	 * @param severity
	 * @param expectedMsg
	 * @throws IOException
	 * @throws Exception
	 */
	
	
	 @Test(groups ={"TEST"},dataProvider = "_sessionLogin")
	public void verify_User_Session(String activityType, String objectType, String expectedMsg,  String severity, String logFile) throws Exception{
		Reporter.log("Validate User login ", true);
		//String expectedMsg="User logged in";
		replayLogs(logFile);
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
		
	}
	
	@DataProvider
	public Object[][] _sessionLogin() {
		return new Object[][]{
			//Activity type  Object Type  Object Name  Severity  message
			{ "Login", "Session", "User logged in",         "informational", "Google Drive,Session_2,Login.log"},
			{ "Logout", "Session","User logged out",            "informational", "Google Drive,Admin_2,Logout.log"},
			{ "InvalidLogin", "Session", "User Login Failed!", "informational", "Google Drive,Admin_2,Invalid_Login1.log"},
			
		};
	}
	
	 @Test(groups ={"TEST"},dataProvider = "_101")
	public void verify_User_Create_Folder(String activityType, String objectType, String folderName, String severity, String logFile) throws Exception{
		Reporter.log("Validate User created folder ", true);
		String expectedMsg="User created folder "+folderName+" in My Drive";
		replayLogs(logFile);
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
		
	}
	
	@DataProvider
	public Object[][] _101() {
		return new Object[][]{
			//Activity type  Object Type  Object Name  Severity  message
			{ "Create", "File/Folder","AFolder", "informational", "GoogleDrive,Admin_2,Create_Folder.log"},
			
		};
	}
	
	
	
	 @Test(groups ={"TEST"},dataProvider = "_102")
		public void verify_User_View_Folder(String activityType, String objectType,  String severity, String logFile) throws Exception{
			Reporter.log("Validate User viewed folder", true);
			String expectedMsg="User browsed folder named AFolder";
			replayLogs(logFile);
			data.put("message", expectedMsg);
			data.put("account_type", "Internal");
			assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
			
		}
		
		@DataProvider
		public Object[][] _102() {
			return new Object[][]{
				//Activity type  Object Type  Object Name  Severity  message
				{ "View", "File/Folder", "informational", "Google Drive,Folder_2,View.log"},
				
			};
		}
	
	
	 @Test(groups ={"TEST"},dataProvider = "_103")
		public void verify_User_Open_with_App(String activityType, String objectType, String App_name, String severity, String logFile) throws Exception{
			Reporter.log("Validate User open folder with App", true);
			String expectedMsg="User connected app named "+App_name;
			replayLogs(logFile);
			data.put("message", expectedMsg);
			data.put("account_type", "Internal");
			assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
			
		}
		
		@DataProvider
		public Object[][] _103() {
			return new Object[][]{
				//Activity type  Object Type  Object Name  Severity  message
				{ "Connect", "Application", "SMART amp", "informational", "Google Drive,Folder_2,Open_with_App.log"},
				
			};
		}
		
		
		@Test(groups ={"TEST"},dataProvider = "_104")
		public void verify_User_Share_Folder_Edit(String activityType, String objectType, String folder_name, String email, String severity, String logFile) throws Exception{
			Reporter.log("Validate User shared folder with email", true);
			String expectedMsg="User shared file(s)/folder(s) "+folder_name+" with "+email;
			replayLogsEPDV3(logFile);
			data.put("message", expectedMsg);
			data.put("account_type", "Internal");
			assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
			
		}
		
		@DataProvider
		public Object[][] _104() {
			return new Object[][]{
				//Activity type  Object Type  Object Name  Severity  message
				{ "Share", "File/Folder","", "Mohd Afjal &lt;mohd.afjal@elastica.co&gt;", "informational", "FolderGD,Share_Folder_Mail_Edit.log"},

			};
		}
		
		
		@Test(groups ={"TEST"},dataProvider = "_105")
		public void verify_User_Share_Folder_View(String activityType, String objectType,  String email, String itemName, String severity, String logFile) throws Exception{
			Reporter.log("Validate User shared folder with email", true);
			String expectedMsg="User added "+email+" as collaborators on item "+itemName;
			replayLogsEPDV3(logFile);
			data.put("message", expectedMsg);
			data.put("account_type", "Internal");
			assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
			
		}
		
		@DataProvider
		public Object[][] _105() {
			return new Object[][]{
				//Activity type  Object Type  Object Name  Severity  message
				{ "Share", "File/Folder", "Mohd Afjal &lt;mohd.afjal@elastica.co&gt;", "", "informational", "FolderGD,Share_Folder_Mail_View.log"},

			};
		}
		
		
		@Test(groups ={"TEST"},dataProvider = "_106")
		public void verify_User_Unshare_Folder(String activityType, String objectType, String email, String itemName, String severity, String logFile) throws Exception{
			Reporter.log("Validate User unshare folder", true);
			String expectedMsg="User removed "+email+" from collaborators list for item "+itemName;
			replayLogsEPDV3(logFile);
			data.put("message", expectedMsg);
			data.put("account_type", "Internal");
			assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
			
		}
		
		@DataProvider
		public Object[][] _106() {
			return new Object[][]{
				//Activity type  Object Type  Object Name  Severity  message
				{ "Unshare", "File/Folder", "admin@gatewaybeatle.com, mohd.afjal@elastica.co", "", "informational", "FolderGD,Unshare_Folder.log"},
				
			};
		}
		
		
		@Test(groups ={"TEST"},dataProvider = "_107")
		public void verify_User_Update_Collaborator_View_Folder(String activityType, String objectType, String email, String itemName, String severity, String logFile) throws Exception{
			Reporter.log("Validate User share folder", true);
			String expectedMsg="User added "+email+" as collaborators who can View item "+itemName;
			replayLogsEPDV3(logFile);
			data.put("message", expectedMsg);
			data.put("account_type", "Internal");
			assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
			
		}
		
		@DataProvider
		public Object[][] _107() {
			return new Object[][]{
				//Activity type  Object Type  Object Name  Severity  message
				{ "Share", "File/Folder", "", "", "informational", "FolderGD,Update_Folder_Collaborator_View.log"},
				
			};
		}
		
		
		@Test(groups ={"TEST"},dataProvider = "_108")
		public void verify_User_Update_Collaborator_Edit_Folder(String activityType, String objectType, String property,  String itemName, String severity, String logFile) throws Exception{
			Reporter.log("Validate User share folder", true);
			String expectedMsg="User changed permissions for the entire organization to "+property+" for item "+itemName;
			replayLogsEPDV3(logFile);
			data.put("message", expectedMsg);
			data.put("account_type", "Internal");
			assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
			
		}
		
		@DataProvider
		public Object[][] _108() {
			return new Object[][]{
				//Activity type  Object Type  Object Name  Severity  message
				{ "Share", "File/Folder", "&lt;can view&gt;", "", "informational", "FolderGD,Update_Folder_Collaborator_Edit.log"},
				
			};
		}
		
		@Test(groups ={"TEST"},dataProvider = "_109")
		public void verify_User_Update_Collaborator_Owner_Folder(String activityType, String objectType, String email, String property,  String itemName, String severity, String logFile) throws Exception{
			Reporter.log("Validate User share folder", true);
			String expectedMsg="User changed "+email+" permissions to "+property+" for item "+itemName;
			replayLogsEPDV3(logFile);
			data.put("message", expectedMsg);
			data.put("account_type", "Internal");
			assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
			
		}
		
		@DataProvider
		public Object[][] _109() {
			return new Object[][]{
				//Activity type  Object Type  Object Name  Severity  message
				{ "Share", "File/Folder", "admin@gatewaybeatle.com", "&lt;can edit&gt;", "", "informational", "FolderGD,Update_Folder_Collaborator_Owner.log"},
				
			};
		}
		
		
		@Test(groups ={"TEST"},dataProvider = "_110")
		public void verify_User_Move_Folder(String activityType, String objectType, String ObjectName, String source, String destination, String severity, String logFile) throws Exception{
			Reporter.log("Validate User share folder", true);
			String expectedMsg="User moved item "+ObjectName+" from "+source+" to folder "+destination;
			replayLogs(logFile);
			data.put("message", expectedMsg);
			data.put("account_type", "Internal");
			assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
			
		}
		
		@DataProvider
		public Object[][] _110() {
			return new Object[][]{
				//Activity type  Object Type  Object Name  Severity  message
				{ "Move", "File/Folder", "ARFolder", "root","new", "informational", "Google Drive,Folder_2,Move.log"},
				
			};
		}
		
		@Test(groups ={"TEST"},dataProvider = "_111")
		public void verify_User_Rename_Folder(String activityType, String objectType, String folderName,  String severity, String logFile) throws Exception{
			Reporter.log("Validate User share folder", true);
			String expectedMsg="User renamed an item "+folderName;
			replayLogs(logFile);
			data.put("message", expectedMsg);
			data.put("account_type", "Internal");
			assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
			
		}
		
		@DataProvider
		public Object[][] _111() {
			return new Object[][]{
				//Activity type  Object Type  Object Name  Severity  message
				{ "Rename", "File/Folder", "ARFolder", "informational", "Google Drive,Folder_2,Rename.log"},
				
			};
		}
	
		
		@Test(groups ={"TEST"},dataProvider = "_112")
		public void verify_User_Link_Share_Docs_Folder(String activityType, String objectType, String property, String ObjectName, String severity, String logFile) throws Exception{
			Reporter.log("Validate User share the link", true);
			String expectedMsg="User added "+property+" as collaborators on item "+ObjectName; 
			replayLogsEPDV3(logFile);
			data.put("message", expectedMsg);
			data.put("account_type", "Internal");
			assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
			
		}
		
		@DataProvider
		public Object[][] _112() {
			return new Object[][]{
				//Activity type  Object Type  Object Name  Severity  message
				{ "Update Link Sharing", "File/Folder", "Mohd Afjal &lt;mohd.afjal@elastica.co&gt;", "", "informational", "FolderGD,Update_Folder_Link_Share_View.log"},
				
			};
		}
		
		
		@Test(groups ={"TEST"},dataProvider = "_113")
		public void verify_User_Link_Share_Edit_Folder(String activityType, String objectType, String property, String ObjectName,  String severity, String logFile) throws Exception{
			Reporter.log("Validate User share the link", true);
			String expectedMsg="Use changed permissions for the entire organization to "+property+" for item "+ObjectName;
			replayLogsEPDV3(logFile);
			data.put("message", expectedMsg);
			data.put("account_type", "Internal");
			assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
			
		}
		
		@DataProvider
		public Object[][] _113() {
			return new Object[][]{
				//Activity type  Object Type  Object Name  Severity  message
				{ "Update Link Sharing", "File/Folder", "&lt;can edit&gt;", "", "informational", "FolderGD,Update_Folder_Link_Share_Edit.log"},
				
			};
		}
		
		
		@Test(groups ={"TEST"},dataProvider = "_114")
		public void verify_User_Download_Folder(String activityType, String objectType, String ObjectName,  String severity, String logFile) throws Exception{
			Reporter.log("Validate User share the link", true);
			String expectedMsg="[ERROR] The following activity failed:  User downloaded contents of folder as filename "+ObjectName;
			replayLogs(logFile);
			data.put("message", expectedMsg);
			data.put("account_type", "Internal");
			assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
			
		}
		
		@DataProvider
		public Object[][] _114() {
			return new Object[][]{
				//Activity type  Object Type  Object Name  Severity  message
				{ "Download", "File/Folder", "AFolder-2016-05-13.zip", "informational", "Google Drive,Folder_2,Download1.log"},
				
			};
		}
		
		
		@Test(groups ={"TEST"},dataProvider = "_115")
		public void verify_User_Upload_Folder(String activityType, String objectType, String ObjectName,  String severity, String logFile) throws Exception{
			Reporter.log("Validate User upload file", true);
			String expectedMsg="User uploaded contents of folder "+ObjectName;
			replayLogs(logFile);
			data.put("message", expectedMsg);
			data.put("account_type", "Internal");
			assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
			
		}
		
		@DataProvider
		public Object[][] _115() {
			return new Object[][]{
				//Activity type  Object Type  Object Name  Severity  message
			    { "Upload", "Folder", "Afjal", "informational", "Google Drive,Folder_2,Upload1.log"},
		};
		
		}	

		
		@Test(groups ={"TEST"},dataProvider = "_116")
		public void verify_User_Remove_Folder(String activityType, String objectType, String ObjectName,  String severity, String logFile) throws Exception{
			Reporter.log("Validate User remove file", true);
			String expectedMsg="User deleted item(s) "+ObjectName;
			replayLogs(logFile);
			data.put("message", expectedMsg);
			data.put("account_type", "Internal");
			assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
			
		}
		
		@DataProvider
		public Object[][] _116() {
			return new Object[][]{
				//Activity type  Object Type  Object Name  Severity  message
				{ "Remove", "Folder", "ARFolder", "informational", "Google Drive,Folder_2,Remove.log"},
				
			};
		}
		
		
		
		@Test(groups ={"TEST"},dataProvider = "_117")
		public void verify_User_Share_With_External_User(String activityType, String objectType, String ObjectName, String email,  String severity, String logFile) throws Exception{
			Reporter.log("Validate User shared file with external user", true);
			String expectedMsg="User shared file(s)/folder(s) "+ObjectName+" with "+email;
			replayLogs(logFile);
			data.put("message", expectedMsg);
			data.put("account_type", "Internal");
			assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
			
		}
		
		@DataProvider
		public Object[][] _117() {
			return new Object[][]{
				//Activity type  Object Type  Object Name  Severity  message
				{ "Share", "File/Folder", "Test.ppt",         "Mohd Afjal &lt;mohd.afjal@elastica.co&gt;", "informational", "Google Drive,Admin_2,Share_File_ppt.log"},
				{ "Share", "File/Folder", "Test.pdf",         "Mohd Afjal &lt;mohd.afjal@elastica.co&gt;", "informational", "Google Drive,Admin_2,Share_File_pdf.log"},
				{ "Share", "File/Folder", "Test.odg",         "Mohd Afjal &lt;mohd.afjal@elastica.co&gt;", "informational", "Google Drive,Admin_2,Share_File_odg.log"},
				{ "Share", "File/Folder", "Test.odp",         "Mohd Afjal &lt;mohd.afjal@elastica.co&gt;", "informational", "Google Drive,Admin_2,Share_File_odp.log"},
				{ "Share", "File/Folder", "Test.ods",         "Mohd Afjal &lt;mohd.afjal@elastica.co&gt;", "informational", "Google Drive,Admin_2,Share_File_ods.log"},
				{ "Share", "File/Folder", "Test.pages",       "Mohd Afjal &lt;mohd.afjal@elastica.co&gt;", "informational", "Google Drive,Admin_2,Share_File_pages.log"},
				{ "Share", "File/Folder", "Test.pem",         "Mohd Afjal &lt;mohd.afjal@elastica.co&gt;", "informational", "Google Drive,Admin_2,Share_File_pem.log"},
				{ "Share", "File/Folder", "Test.png",         "Mohd Afjal &lt;mohd.afjal@elastica.co&gt;", "informational", "Google Drive,Admin_2,Share_File_png.log"},
				{ "Share", "File/Folder", "Test.pptx",        "Mohd Afjal &lt;mohd.afjal@elastica.co&gt;", "informational", "Google Drive,Admin_2,Share_File_pptx.log"},
				{ "Share", "File/Folder", "Test.properties",  "Mohd Afjal &lt;mohd.afjal@elastica.co&gt;", "informational", "Google Drive,Admin_2,Share_File_properties.log"},
				{ "Share", "File/Folder", "Test.xls",         "Mohd Afjal &lt;mohd.afjal@elastica.co&gt;", "informational", "Google Drive,Admin_2,Share_File_xls.log"},
				
			};
		}
		
		@Test(groups ={"TEST"},dataProvider = "_118")
		public void verify_User_Restore(String activityType, String objectType, String ObjectName,  String severity, String logFile) throws Exception{
			Reporter.log("Validate User restored folder", true);
			String expectedMsg="User restored folder/file named "+ObjectName;
			replayLogs(logFile);
			data.put("message", expectedMsg);
			data.put("account_type", "Internal");
			assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
			
		}
		
		@DataProvider
		public Object[][] _118() {
			return new Object[][]{
				//Activity type  Object Type  Object Name  Severity  message
				{ "Restore", "Folder", "ARFolder", "informational", "Google Drive,Folder_2,Restore.log"},
				
			};
		}
		
		
		@Test(groups ={"TEST"},dataProvider = "_119")
		public void verify_User_Delete_Forever(String activityType, String objectType, String ObjectName,  String severity, String logFile) throws Exception{
			Reporter.log("Validate User deleted forever folder", true);
			String expectedMsg="User deleted item(s) "+ObjectName+" permanantly";
			replayLogs(logFile);
			data.put("message", expectedMsg);
			data.put("account_type", "Internal");
			assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
			
		}
		
		@DataProvider
		public Object[][] _119() {
			return new Object[][]{
				//Activity type  Object Type  Object Name  Severity  message
				{ "Delete", "File", "ARFolder", "informational", "Google Drive,Folder_2,Detete_Forever.log"},
				
			};
		}
		
		@Test(groups ={"TEST"},dataProvider = "_120")
		public void verify_User_File_Upload(String activityType, String objectType, String ObjectName, String folderName,  String severity, String logFile) throws Exception{
			Reporter.log("Validate User deleted forever folder", true);
			String expectedMsg="User uploaded file "+ObjectName+" in "+folderName+" folder";
			replayLogsWithDelay(logFile);
			data.put("message", expectedMsg);
			data.put("account_type", "Internal");
			assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
			
		}
		
		@DataProvider
		public Object[][] _120() {
			return new Object[][]{
				//Activity type  Object Type  Object Name  Severity  message
				{ "Upload", "File", "", "Test.exe",  "informational", "Google Drive,Admin_2,Upload_GWFolder_root_exe.log"},
				{ "Upload", "File", "", "Test.mp3",  "informational", "Google Drive,Admin_2,Upload_GWFolder_root_mp3.log"},
				{ "Upload", "File", "", "Test.mp4",  "informational", "Google Drive,Admin_2,Upload_GWFolder_root_mp4.log"},
				{ "Upload", "File", "", "Test.pdf",  "informational", "Google Drive,Admin_2,Upload_GWFolder_root_pdf.log"},
				{ "Upload", "File", "", "Test.zip",  "informational", "Google Drive,Admin_2,Upload_GWFolder_root_zip.log"},
				
			};
		}
		
		
		@Test(groups ={"TEST"},dataProvider = "_121")
		public void verify_User_File_Upload_WithDiffSize(String activityType, String objectType, String ObjectName, String folderName,  String severity, String logFile) throws Exception{
			Reporter.log("Validate User uploaded file in folder", true);
			String expectedMsg="User uploaded file "+ObjectName+" in "+folderName+" folder";
			replayLogsWithDelay(logFile);
			data.put("message", expectedMsg);
			data.put("account_type", "Internal");
			assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
			
		}
		
		@DataProvider
		public Object[][] _121() {
			return new Object[][]{
				//Activity type  Object Type  Object Name  Severity  message
				{ "Upload", "File", "","GreaterThan1MB.txt", "informational", "Google Drive,Admin_2,Upload_root_Greater1MB.log"},
				{ "Upload", "File", "","GreaterThan2MB.txt", "informational", "Google Drive,Admin_2,Upload_root_Greater2MB.log"},
				{ "Upload", "File", "","GeaterThan5MB.txt", "informational", "Google Drive,Admin_2,Upload_root_Greater5MB.log"},
				
			};
		}
		
		 @Test(groups ={"TEST"},dataProvider = "_122")
			public void verify_User_Create_Docs_File(String activityType, String objectType, String fileName, String DestiName, String severity, String logFile) throws Exception{
				Reporter.log("Validate User created docs file", true);
				String expectedMsg="User Created new "+fileName+" in "+DestiName;
				replayLogs(logFile);
				data.put("message", expectedMsg);
				data.put("account_type", "Internal");
				assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
				
			}
			
			@DataProvider
			public Object[][] _122() {
				return new Object[][]{
					//Activity type  Object Type  Object Name  Severity  message
					{ "Create", "File/Folder","Document","root", "informational", "File,Admin_2,Create_Document_root.log"},
					
				};
			}
			
			 @Test(groups ={"TEST"},dataProvider = "_123")
				public void verify_User_Create_Excel_File(String activityType, String objectType, String DestiName,  String severity, String logFile) throws Exception{
					Reporter.log("Validate User created excel file", true);
					String expectedMsg="User Created new Spreadsheet in folder "+DestiName;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _123() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Create", "File/Folder","root", "informational", "File,Admin_2,Create_ExcelBook_root.log"},
						
					};
				}
				
				 @Test(groups ={"TEST"},dataProvider = "_124")
					public void verify_User_Create_Slides_File(String activityType, String objectType, String fileName, String DestiName, String severity, String logFile) throws Exception{
						Reporter.log("Validate User created slide file", true);
						String expectedMsg="User Created new "+fileName+" in "+DestiName;
						replayLogs(logFile);
						data.put("message", expectedMsg);
						data.put("account_type", "Internal");
						assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
						
					}
					
					@DataProvider
					public Object[][] _124() {
						return new Object[][]{
							//Activity type  Object Type  Object Name  Severity  message
							{ "Create", "File/Folder","Presentation", "root", "informational", "File,Admin_2,Create_Slides_root.log"},
							
						};
					}
					
					 @Test(groups ={"TEST"},dataProvider = "_125")
						public void verify_User_Preview_Docs_File(String activityType, String objectType, String fileName, String severity, String logFile) throws Exception{
							Reporter.log("Validate User preview docs file", true);
							String expectedMsg="User Viewed document "+fileName;
							replayLogs(logFile);
							data.put("message", expectedMsg);
							data.put("account_type", "Internal");
							assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
							
						}
						
						@DataProvider
						public Object[][] _125() {
							return new Object[][]{
								//Activity type  Object Type  Object Name  Severity  message
								{ "Preview", "File/Folder","TextDocument", "informational", "File,Admin_2,Preview_Document_root.log"},
								
							};
						}
						
						 @Test(groups ={"TEST"},dataProvider = "_126")
							public void verify_User_Preview_Excel_File(String activityType, String objectType, String fileName, String severity, String logFile) throws Exception{
								Reporter.log("Validate User preview excel file", true);
								String expectedMsg="User Viewed spreadsheet "+fileName;
								replayLogs(logFile);
								data.put("message", expectedMsg);
								data.put("account_type", "Internal");
								assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
								
							}
							
							@DataProvider
							public Object[][] _126() {
								return new Object[][]{
									//Activity type  Object Type  Object Name  Severity  message
									{ "Preview", "File/Folder","ExcelBook", "informational", "File,Admin_2,Preview_ExcelBook_root.log"},
									
								};
							}
							
							 @Test(groups ={"TEST"},dataProvider = "_127")
								public void verify_User_Preview_Slide_File(String activityType, String objectType, String fileName, String severity, String logFile) throws Exception{
									Reporter.log("Validate User preview slide file", true);
									String expectedMsg="User Viewed slide "+fileName;
									replayLogs(logFile);
									data.put("message", expectedMsg);
									data.put("account_type", "Internal");
									assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
									
								}
								
								@DataProvider
								public Object[][] _127() {
									return new Object[][]{
										//Activity type  Object Type  Object Name  Severity  message
										{ "Preview", "File/Folder","Untitled presentation", "informational", "File,Admin_2,Preview_Presentation_root.log"},
										
									};
								}
								
								@Test(groups ={"TEST"},dataProvider = "_128")
								public void verify_User_Remove_File(String activityType, String objectType, String fileName, String severity, String logFile) throws Exception{
									Reporter.log("Validate User deleted file", true);
									String expectedMsg="User deleted item(s) "+fileName;
									replayLogs(logFile);
									data.put("message", expectedMsg);
									data.put("account_type", "Internal");
									assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
									
								}
								
								@DataProvider
								public Object[][] _128() {
									return new Object[][]{
										//Activity type  Object Type  Object Name  Severity  message
										{ "Delete", "File/Folder","TextDoc", "informational", "File,Admin_2,Remove_TextDocument.log"},
										
									};
								}
								
								@Test(groups ={"TEST"},dataProvider = "_129")
								public void verify_User_Rename_File(String activityType, String objectType, String fileName, String severity, String logFile) throws Exception{
									Reporter.log("Validate User renamed file", true);
									String expectedMsg="User renamed an item "+fileName;
									replayLogs(logFile);
									data.put("message", expectedMsg);
									data.put("account_type", "Internal");
									assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
									
								}
								
								@DataProvider
								public Object[][] _129() {
									return new Object[][]{
										//Activity type  Object Type  Object Name  Severity  message
										{ "Rename", "File/Folder","TextDoc",  "informational", "File,Admin_2,Rename_TextDocument_TestDoc.log"},
										
									};
								}
								
								@Test(groups ={"TEST"},dataProvider = "_130")
								public void verify_User_Copy_File(String activityType, String objectType, String fileNmae, String OrgFile, String folderName, String severity, String logFile) throws Exception{
									Reporter.log("Validate User renamed file", true);
									String expectedMsg="User made a copy of item "+fileNmae+" named "+OrgFile+" with parent "+folderName;
									replayLogs(logFile);
									data.put("message", expectedMsg);
									data.put("account_type", "Internal");
									assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
									
								}
								
								@DataProvider
								public Object[][] _130() {
									return new Object[][]{
										//Activity type  Object Type  Object Name  Severity  message
										{ "Copy", "File/Folder", "TextDoc",  "Copy of TextDocument", "GWFolder", "informational", "File,Admin_2,Copy_GWFolder_root.log"},
										
									};
								}
								
								
		
	
}
		

	