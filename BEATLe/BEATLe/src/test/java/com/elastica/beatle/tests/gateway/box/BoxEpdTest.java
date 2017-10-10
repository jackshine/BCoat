package com.elastica.beatle.tests.gateway.box;

import org.testng.annotations.Test;
import org.testng.annotations.DataProvider;
import static org.testng.Assert.assertTrue;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.testng.Reporter;
import com.elastica.beatle.gateway.CommonConfiguration;
import com.elastica.beatle.gateway.LogValidator;

import com.elastica.beatle.gateway.dto.GWForensicSearchResults;


/*******************Author**************
 * 
 * @author usman
 * Every test has an id which is inherited from Elastica-ADB
 */

public class BoxEpdTest extends CommonConfiguration {

	String currentTimeInJodaFormat;
	GWForensicSearchResults fsr;
	ArrayList<String> messages = new ArrayList<String>();
	ArrayList<String> objectTypeList = new ArrayList<String>();
	ArrayList<String> objectNameList = new ArrayList<String>();
	ArrayList<String> severityList = new ArrayList<String>();
	LogValidator logValidator;
	String userLitral="User";
	//Map <String, String> data = new HashMap<String, String>();
	
	
	/******************************************************************
	 * Validate the login,logout and invalid login functionality in Box
	 * @param activityType
	 * @param objectType
	 * @param severity
	 * @param expectedMsg
	 * @throws IOException
	 * @throws Exception
	 */
	
	
	
	
	
	@Test(groups ={"TEST",  "SESSION","P1"},dataProvider = "_342_343_345_")
	public void verify_User_Loggedin_Logout_Invalid_Login_342_343_345_(String activityType, String objectType, String severity, String expectedMsg, String logFile) throws IOException, Exception{
		Reporter.log("Validate Web login, logout and Invalid login in Box", true);
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		data.put("Object_type", objectType);
		data.put("Activity_type", activityType);
		//generateCsvFile(data); 
		replayLogsEPDV3(logFile);
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(activityType, severity), data)), activityType+" Logs does not match" ); 
	}
	
	@DataProvider
	public Object[][] _342_343_345_() {
		return new Object[][]{
			//Activity type    Object Type    Severity           message
			{ "Login",     		"Session",  "informational",  "User logged in" 	                   ,"BoxSession,Login.log"  },
		    { "View",     		"Folder",   "informational",  "User viewed Files and Folders page" ,"BoxSession,Login.log" },
			{ "InvalidLogin",   "Session",  "informational",  "User Login Failed!"                 , "BoxSession,InvalidLogin.log"},
			{ "Logout",    		"Session",  "informational",  "User logged out"                    ,"BoxSession,LogOut.log" }
		};
	}
	
	
	
	@Test(groups ={"ADMIN",  "IMPRESSINATION","P1"},dataProvider = "_342_343_345_Userimpersonation")
	public void verify_User_Loggedin_Logout_Invalid_Login_342_343_345_Userimpersonation(String activityType, String objectType, String severity, String expectedMsg, String logFile) throws IOException, Exception{
		Reporter.log("Validate Web login, logout and Invalid login in Box", true);
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		data.put("Object_type", objectType);
		replayLogs(logFile);
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(activityType, severity), data)), activityType+" Logs does not match" ); 
	}
	
	@DataProvider
	public Object[][] _342_343_345_Userimpersonation() {
		return new Object[][]{
			//Activity type    Object Type    Severity           message
			{ "Login",     		"Session",  "informational",  "User logged in" 	                   ,"BoxSession,Login.log"  },
			{ "InvalidLogin",   "Session",  "informational",  "User Login Failed!"                 ,"BoxSession,InvalidLogin.log"},
			{ "Logout",    		"Session",  "informational",  "User logged out"                    ,"BoxSession,LogOut.log" }
		};
	}
	
	
	
	
	
	
	
	/***********************************************
	 * Validate user scrap the page
	 * @param activityType
	 * @param objectType
	 * @param severity
	 * @param expectedMsg
	 * @throws IOException
	 * @throws Exception
	 */
	
	@Test(groups ={"TEST","FAIL","REGRESSION","BOX", "SCRAP_PAGE", "P2"},dataProvider = "_346")
	public void verify_User_viewed_Files_and_Folders_page_346(String activityType, String objectType, String severity, String expectedMsg, String logFile) throws IOException, Exception{
		Reporter.log("Validate User scrap the page", true);
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		data.put("Object_type", objectType);
		replayLogsEPDV3(logFile);
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(activityType, severity), data)), activityType+" Logs does not match" ); 
	}
	
	@DataProvider
	public Object[][] _346() {
		return new Object[][]{
			//Activity type    Object Type           Severity          message
			{ "View",     		"Folder",      "informational",  "User viewed contents of All Files and Folder", "FileBox,View_app_page.log" }
		};
	}
	
	
	/*********************************************
	 * Validate user upload the file to folder 
	 * @param activityType
	 * @param objectType
	 * @param objectName
	 * @param destinationFolder
	 * @param severity
	 * @throws Exception 
	 */

	@Test(groups ={"TEST", "ADMIN", "UPLOAD", "P1"},dataProvider = "_348")
	public void verify_User_uploaded_file_to_folder_348(String activityType, String objectType, String objectName,String destinationFolder, String severity, String logFile) throws Exception{
		Reporter.log("Validate User Upload the file/folder to folder", true);
		String expectedMsg="User uploaded file "+objectName+" to folder "+destinationFolder;
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		data.put("Object_type", objectType);
		//data.put("Object_name", objectName);
		replayLogs(logFile);
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(activityType, severity), data)), activityType+" Logs does not match" ); 
		
	}
	
	@DataProvider
	public Object[][] _348() {
		return new Object[][]{
			//Activity type  Object Type  Object Name, DestinationDolder  Severity  message
			{ "Upload",    "File", "Apidata.txt", "All Files", "informational", "apidata.txt,uploadtext.log"},
			{ "Upload",    "File", "Test.txt", 		"DFolder", "informational", "Test,AFolder,upload_txt.log"},
			{ "Upload",    "File", "Test.xml", 		"DFolder", "informational", "Test,AFolder,upload_xml.log"},  
			{ "Upload",    "File", "Test.zip", 		"DFolder", "informational", "Test,AFolder,upload_zip.log"},  
			{ "Upload",    "File", "Test.boxnote",	"DFolder", "informational", "Test,AFolder,upload_boxnote.log"},         //for subfolder
			{ "Upload",    "File", "Test.c", 		"DFolder", "informational", "Test,AFolder,upload_c.log"},  
			{ "Upload",    "File", "Test.datatable","DFolder", "informational", "Test,AFolder,upload_datatable.log"},  
			{ "Upload",    "File", "Test.doc", 		"DFolder", "informational", "Test,AFolder,upload_doc.log"},  
			{ "Upload",    "File", "Test.docx", 	"DFolder", "informational", "Test,AFolder,upload_docx.log"},  
			{ "Upload",    "File", "Test.pdf", 		"DFolder", "informational", "Test,AFolder,upload_pdf.log"},  
			{ "Upload",    "File", "Test.png", 		"DFolder", "informational", "Test,AFolder,upload_png.log"}, 
			{ "Upload",    "File", "Test.py", 		"DFolder", "informational", "Test,AFolder,upload_py.log"},  
			{ "Upload",    "File", "Test.rar", 		"DFolder", "informational", "Test,AFolder,upload_rar.log"},   
			{ "Upload",    "File", "Test.vb", 		"DFolder", "informational", "Test,AFolder,upload_vb.log"},
			{ "Upload",    "File", "Test.txt", 		"DFolder", "informational", "Test,AFolder,upload_txt.log"},
			{ "Upload",    "File", "Test.xls", 		"DFolder", "informational", "Test,AFolder,upload_xls.log"},  
			{ "Upload",    "File", "Test.rtf", 		"DFolder", "informational", "Test,AFolder,upload_rtf.log"}, 
			{ "Upload",    "File", "Test.tbz2", 	"DFolder", "informational", "Test,AFolder,upload_tbz2.log"},  
			{ "Upload",    "File", "Test.tgz", 		"DFolder", "informational", "Test,AFolder,upload_tgz.log"},  
			{ "Upload",    "File", "Test.xlsm", 	"DFolder", "informational", "Test,AFolder,upload_xlsm.log"},  
			{ "Upload",    "File", "Test.xlsx", 	"DFolder", "informational", "Test,AFolder,upload_xlsx.log"},  
			{ "Upload",    "File", "Test.7z", 		"DFolder", "informational", "Test,AFolder,upload_7z.log"},  
			{ "Upload",    "File", "Test.base64", 	"DFolder", "informational", "Test,AFolder,upload_base64.log"}, 
			{ "Upload",    "File", "Test.html", 	"DFolder", "informational", "Test,AFolder,upload_html.log"},  
			{ "Upload",    "File", "Test.java", 	"DFolder", "informational", "Test,AFolder,upload_java.log"},  
			{ "Upload",    "File", "Test.jpg", 		"DFolder", "informational", "Test,AFolder,upload_jpg.log"},  
			{ "Upload",    "File", "Test.js", 		"DFolder", "informational", "Test,AFolder,upload_js.log"},  
			{ "Upload",    "File", "Test.json", 	"DFolder", "informational", "Test,AFolder,upload_json.log"},  
			{ "Upload",    "File", "Test.ods", 		"DFolder", "informational", "Test,AFolder,upload_ods.log"},  
			{ "Upload",    "File", "Test.odt", 		"DFolder", "informational", "Test,AFolder,upload_odt.log"},  
			{ "Upload",    "File", "Test.pem", 		"DFolder", "informational", "Test,AFolder,upload_pem.log"},  
			{ "Upload",    "File", "Test.properties","DFolder", "informational", "Test,AFolder,upload_properties.log"}, 
			
		
		};
	}
	
	
	
	@Test(groups ={"TEST", "ADMIN", "UPLOAD", "P1"},dataProvider = "_348V3")
	public void verify_User_uploaded_file_to_folder_348V3(String activityType, String objectType, String objectName,String destinationFolder, String severity, String logFile) throws Exception{
		Reporter.log("Validate User Upload the file/folder to folder", true);
		String expectedMsg="User uploaded file "+objectName+" to folder "+destinationFolder;
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		data.put("Object_type", objectType);
		//data.put("Object_name", objectName);
		replayLogsEPDV3(logFile);
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(activityType, severity), data)), activityType+" Logs does not match" ); 
		
	}
	
	@DataProvider
	public Object[][] _348V3() {
		return new Object[][]{
			//Activity type  Object Type  Object Name, DestinationDolder  Severity  message
			{ "Upload",    "File", "Test.exe", "All Files", "informational", "FileBox,Upload_test_exe.log"},
			{ "Upload",    "File", "Test.mp3", "All Files", "informational", "FileBox,Upload_test_mp3.log"},
			{ "Upload",    "File", "Test.mp4", "All Files", "informational", "FileBox,Upload_test_mp4.log"},
			
		};
	}
	
	
	@Test(groups ={"TEST", "ADMIN", "UPLOAD", "P1"},dataProvider = "_348A")
	public void verify_User_uploaded_largerfileSize_to_folder_348(String activityType, String objectType, String objectName,String destinationFolder, String severity, String logFile) throws Exception{
		Reporter.log("Validate User Upload the file/folder to folder", true);
		String expectedMsg="User uploaded file "+objectName+" to folder "+destinationFolder;
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		data.put("Object_type", objectType);
		//data.put("Object_name", objectName);
		replayLogs(logFile);
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(activityType, severity), data)), activityType+" Logs does not match" ); 
		
	}
	
	@DataProvider
	public Object[][] _348A() {
		return new Object[][]{
			//Activity type  Object Type  Object Name, DestinationDolder  Severity  message
			{ "Upload",    "File", "Testlessthan1MB.pages",    "All Files", "informational", "AllFiles,Box,uploadLess1MBFile.log"},
			{ "Upload",    "File", "Testgreatethan1MB.pages",  "All Files", "informational", "AllFiles,Box,uploadLarger1MBFile.log"},  
			{ "Upload",    "File", "modifyPermissions.gif",    "All Files", "informational", "AllFiles,Box,uploadLarger2MBFile.log"} 
		};
	}
	
	
	
	
	/****************************************
	 * Validate user unshare an Item
	 * @param activityType
	 * @param objectType
	 * @param objectName
	 * @param severity
	 * @throws IOException
	 * @throws Exception
	 */
	
	@Test(groups ={"TEST", "ADMIN","REGRESSION","BOX","UNSHARE","QAVPC", "P1"},dataProvider = "_350")
	public void verify_User_disabled_link_of_item_350(String activityType, String objectType, String objectName, String severity, String logFile) throws IOException, Exception{
		Reporter.log("Validate User unshare an item", true);
		String expectedMsg="User disabled link of "+objectName;
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		data.put("Object_type", objectType);
		//data.put("Object_name", objectName);
		replayLogs(logFile);
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(activityType, severity), data)), activityType+" Logs does not match" ); 
		
	}
	
	@DataProvider
	public Object[][] _350() {
		return new Object[][]{
			//Activity type  Object Type  Object Name  Severity
			{ "Unshare",     "Folder", "GFolder", "informational", "GFolder,RemoveShareLink.log"} //AFolder
			
		};
	}
	
	
	/*********************************************
	 * Validate User shared link of item to Chatter
	 * @param activityType
	 * @param objectType
	 * @param objectName
	 * @param severity
	 * @throws IOException
	 * @throws Exception
	 */
	
	@Test(groups ={"TEST", "REGRESSION","BOX", "SHARE_CHARTTER",  "QAVPC","P1"},dataProvider = "_353")
	public void verify_User_shared_link_of_item_to_Chatter_353(String activityType, String objectType, String objectName, String severity, String logFile) throws IOException, Exception{
		Reporter.log("Validate User shared link of item to Chatter", true);
		String expectedMsg="User shared link of "+objectName+ " to Chatter.";
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		data.put("Object_type", objectType);
		//data.put("Object_name", objectName);
		replayLogs(logFile);
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(activityType, severity), data)), activityType+" Logs does not match" ); 
		
	}
	
	@DataProvider
	public Object[][] _353() {
		return new Object[][]{
			//Activity type  Object Type  Object Name Event _EventType  Severity  message
			{ "Share",     "File/Folder", "BFolder", "informational", "BFolder,chatter.log"},
		};
	}
	
	
	/***************************************************************************
	 * Validate User created folder X with collaboration permission Y to email Z
	 * @param activityType
	 * @param objectType
	 * @param objectName
	 * @param severity
	 * @param email
	 * @param permission
	 * @throws IOException
	 * @throws Exception
	 */
	
	@Test(groups ={"TEST","ADMIN","REGRESSION","BOX","SHARE", "QAVPC",  "P1"},dataProvider = "_354")
	public void verify_User_created_folder_with_collaboration_permissions_for_email_354(String activityType, String objectType, String objectName, String severity, String email, String permission, String logFile) throws IOException, Exception{
		Reporter.log("Validate User created folder X with collaboration permission Y to email Z", true);
		String expectedMsg="User created folder "+objectName+" with collaboration permissions "+permission+" for "+email;
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		data.put("Object_type", objectType);
		//data.put("Object_name", objectName);
		replayLogsEPDV3(logFile);
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(activityType, severity), data)), activityType+" Logs does not match" ); 
		
	}
	
	@DataProvider
	public Object[][] _354() {
		return new Object[][]{
			//Activity type  Object Type  Object Name Severity   Shared with   message
			{ "Share",  "Folder",      "DFolder", "informational", "mohd.afjal@elastica.co",      "Editor", "BoxFolder,Create_folder_Collaborator_Permission_Editor.log"},
			{ "Share",  "Folder",      "EFolder", "informational", "mohd.afjal@elastica.co",      "Uploader",      "BoxFolder,Create_folder_Collaborator_Permission_Uploader.log"},
			{ "Share",  "Folder",      "PFolder", "informational", "mohd.afjal@elastica.co",      "Co-owner",      "BoxFolder,Create_folder_Collaborator_Permission_Co-Owner.log"},
			{ "Share",  "Folder",      "GFolder", "informational", "mohd.afjal@elastica.co",      "Previewer",     "BoxFolder,Create_folder_Collaborator_Permission_Previewer.log"},
			{ "Share",  "Folder",      "SFolder", "informational", "mohd.afjal@elastica.co",      "Previewer Uploader", "BoxFolder,Create_folder_Collaborator_Permission_Previewer_Uploader.log"},
			{ "Share",  "Folder",      "XFolder", "informational", "mohd.afjal@elastica.co",      "Viewer",        "BoxFolder,Create_folder_Collaborator_Permission_Viewer.log"},
			{ "Share",  "Folder",      "MFolder", "informational", "mohd.afjal@elastica.co",      "Viewer Uploader", "BoxFolder,Create_folder_Collaborator_Permission_Viewer_Uploader.log"}
		};
	}
	
	
	/*************************************************************
	 * Validate User created new folder
	 * @param activityType
	 * @param objectType
	 * @param objectName
	 * @param severity
	 * @throws IOException
	 * @throws Exception
	 */
	
	@Test(groups ={"TEST","REGRESSION","BOX","CREATE", "EOE",  "P1"},dataProvider = "_355")
	public void verify_User_created_new_folder_355(String activityType, String objectType, String objectName, String severity, String logFile) throws IOException, Exception{
		Reporter.log("Validate User created new folder", true);
		String expectedMsg="User created new folder "+objectName;
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		data.put("Object_type", objectType);
		//data.put("Object_name", objectName);
		replayLogsEPDV3(logFile);
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(activityType, severity), data)), activityType+" Logs does not match" ); 
		
	}
	
	@DataProvider
	public Object[][] _355() {
		return new Object[][]{
			//Activity type  Object Type  Object Name Severity  message
			{ "Create",     "Folder", "GWAuto", "informational", "FolderBox,Created_folder.log"}
			
			
		};
	}
	
	
	@Test(groups ={"TEST","REGRESSION","BOX","CREATE", "EOE",  "P1"},dataProvider = "_355A")
	public void verify_User_rename_new_folder_355A(String activityType, String objectType, String objectName, String destination,  String severity, String logFile) throws IOException, Exception{
		Reporter.log("Validate User created new folder", true);
		String expectedMsg="User renamed a folder "+objectName+" to "+destination;
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		data.put("Object_type", objectType);
		//data.put("Object_name", objectName);
		replayLogsEPDV3(logFile);
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(activityType, severity), data)), activityType+" Logs does not match" ); 
		
	}
	
	@DataProvider
	public Object[][] _355A() {
		return new Object[][]{
			//Activity type  Object Type  Object Name Severity  message
			{ "Rename", "Folder", "f4a2b168-8e63-4649-a7f9-f1fd62cfde21","ZFolder", "informational", "FolderBox,Rename_folder.log"}
			
			
		};
	}
	
	
	
	/***********************************************************
	 * "Validate User created new folder
	 * @param activityType
	 * @param objectType
	 * @param webLink
	 * @param linkName
	 * @param targetFolder
	 * @param severity
	 * @throws IOException
	 * @throws Exception
	 */
	
	@Test(groups ={"TEST","REGRESSION","BOX","CREATE", "EOE", "P1"},dataProvider = "_357")
	public void verify_User_created_new_web_link_with_name_in_folder_357(String activityType, String objectType, String objectName, String severity, String logFile) throws IOException, Exception{
		Reporter.log("Validate User created new folder", true);
		String expectedMsg="User created new web link "+objectName+" with name saas in folder All Files";
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		data.put("Object_type", objectType);
		replayLogsEPDV3(logFile);
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(activityType, severity), data)), activityType+" Logs does not match" ); 
		
	}
	
	@DataProvider
	public Object[][] _357() {
		return new Object[][]{
			//Activity type  Object Type  web link link name  destination Severity  message
			{ "Create",   "Bookmark", "http://box.com", "informational", "FileBox,Create_bookmark.log"}
			
		};
	}
	
	
	@Test(groups ={"TEST","REGRESSION","BOX","CREATE", "EOE", "P1"},dataProvider = "_357A")
	public void verify_User_deleted_new_web_link_with_name_in_folder_357A(String activityType, String objectType, String objectName, String severity, String logFile) throws IOException, Exception{
		Reporter.log("Validate User created new folder", true);
		String expectedMsg="User deleted object(s) "+objectName;
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		data.put("Object_type", objectType);
		replayLogsEPDV3(logFile);
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(activityType, severity), data)), activityType+" Logs does not match" ); 
		
	}
	
	@DataProvider
	public Object[][] _357A() {
		return new Object[][]{
			//Activity type  Object Type  web link link name  destination Severity  message
			{ "Delete",   "File/Folder", "Apps", "informational", "FileBox,Delete_bookmark.log"}
			
		};
	}
	
	
	/****************************************************************
	 * Validate User created new google doc
	 * @param activityType
	 * @param objectType
	 * @param objectName
	 * @param targetFolder
	 * @param severity
	 * @throws IOException
	 * @throws Exception
	 */
	
	@Test(groups ={"TEST","REGRESSION","BOX","CREATE", "EOE", "P1"},dataProvider = "_359")
	public void verify_User_created_new_google_doc_in_folder_359(String activityType, String objectType, String objectName, String targetFolder, String severity, String logFile) throws IOException, Exception{
		Reporter.log("Validate User created new google doc", true);
		String expectedMsg="User created new google doc "+objectName+ " in folder "+targetFolder;
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		data.put("Object_type", objectType);
		//data.put("Object_name", objectName);
		replayLogs(logFile);
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(activityType, severity), data)), activityType+" Logs does not match" ); 
		
	}
	
	@DataProvider
	public Object[][] _359() {
		return new Object[][]{
			//Activity type  Object Type  object Name  target folder     Severity  
			{ "Create",     "File",       "Googledoc.gdoc",  "All Files",   "informational","gdoc,CreateGDoc.log"} //All Files
			
		};
	}
	
	
	@Test(groups ={"TEST","REGRESSION","BOX","CREATE", "EOE", "P1"},dataProvider = "_359A")
	public void verify_User_created_new_Excel_in_folder_359A(String activityType, String objectType, String objectName, String targetFolder, String severity, String logFile) throws IOException, Exception{
		Reporter.log("Validate User created new google doc", true);
		String expectedMsg="User created new google doc "+objectName+ " in folder "+targetFolder;
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		data.put("Object_type", objectType);
		//data.put("Object_name", objectName);
		replayLogs(logFile);
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(activityType, severity), data)), activityType+" Logs does not match" ); 
		
	}
	
	@DataProvider
	public Object[][] _359A() {
		return new Object[][]{
			//Activity type  Object Type  object Name  target folder     Severity  
			{ "Create",     "File",       "Excel.xls",  "",   "informational","AFile,AFolder,createExcel.log"} //All Files
			
		};
	}
	
	@Test(groups ={"TEST","REGRESSION","BOX","CREATE", "EOE", "P1"},dataProvider = "_359B")
	public void verify_User_created_new_Doc_in_folder_359B(String activityType, String objectType, String objectName, String targetFolder, String severity, String logFile) throws IOException, Exception{
		Reporter.log("Validate User created new google doc", true);
		String expectedMsg="User created new google doc "+objectName+ " in folder "+targetFolder;
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		data.put("Object_type", objectType);
		//data.put("Object_name", objectName);
		replayLogs(logFile);
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(activityType, severity), data)), activityType+" Logs does not match" ); 
		
	}
	
	@DataProvider
	public Object[][] _359B() {
		return new Object[][]{
			//Activity type  Object Type  object Name  target folder     Severity  
			{ "Create",     "File",       "Doc.doc",  "",   "informational","AFile,AFolder,createWordDoc.log"} //All Files
			
		};
	}
	
	
	@Test(groups ={"TEST","REGRESSION","BOX","CREATE", "EOE", "P1"},dataProvider = "_359C")
	public void verify_User_created_new_PPt_in_folder_359A(String activityType, String objectType, String objectName, String targetFolder, String severity, String logFile) throws IOException, Exception{
		Reporter.log("Validate User created new google doc", true);
		String expectedMsg="User created new google doc "+objectName+ " in folder "+targetFolder;
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		data.put("Object_type", objectType);
		//data.put("Object_name", objectName);
		replayLogs(logFile);
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(activityType, severity), data)), activityType+" Logs does not match" ); 
		
	}
	
	@DataProvider
	public Object[][] _359C() {
		return new Object[][]{
			//Activity type  Object Type  object Name  target folder     Severity  
			{ "Create",     "File",       "Ppt.ppt",  "",   "informational","AFile,AFolder,CreatePpt.log"} //All Files
			
		};
	}
	
	/*************************************************************
	 * Validate User created new google spreadsheet
	 * @param activityType
	 * @param objectType
	 * @param objectName
	 * @param targetFolder
	 * @param severity
	 * @throws IOException
	 * @throws Exception
	 */
	@Test(groups ={"TEST","REGRESSION","BOX", "CREATE", "EOE","P1"},dataProvider = "_360")
	public void verify_User_created_new_google_spreadsheet_in_folder_360(String activityType, String objectType, String objectName, String targetFolder, String severity, String logFile) throws IOException, Exception{
		Reporter.log("Validate User created new google spreadsheet ", true);
		String expectedMsg="User created new google spreadsheet "+objectName+ " in folder "+targetFolder;
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		data.put("Object_type", objectType);
		//data.put("Object_name", objectName);
		replayLogs(logFile);
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(activityType, severity), data)), activityType+" Logs does not match" ); 
		
	}
	
	@DataProvider
	public Object[][] _360() {
		return new Object[][]{
			//Activity type  Object Type  Object Name Event _EventType  Severity  message
			{ "Create",     "File",       "Googlesheet.gsheet",  "All Files",   "informational", "gsheet,CreateGSheet.log"} //All Files
			
		};
	}
	
	/**************************************************
	 * Validate User create edit and delete comment
	 * @param activityType
	 * @param objectType
	 * @param objectName
	 * @param comment
	 * @param severity
	 * @param expectedMsg
	 * @throws IOException
	 * @throws Exception
	 */
	//Edit comment is invalid scenarios
	@Test(groups ={"TEST","FAIL","REGRESSION","BOX", "COMMENTS","QAVPC", "BOX", "PROD", "P2"},dataProvider = "_361_362_363_364")
	public void verify_User_posted_new_edit_replied_delete_comment_on_File_361_362_363_364(String activityType, String objectType, String objectName, String fileName, String severity, String logFile) throws IOException, Exception{
		Reporter.log("Validate User posted new comment ", true);
		String expectedMsg="User posted new comment "+objectName+" on "+fileName;
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		data.put("Object_type", objectType);
		//data.put("Object_name", objectName);
		replayLogsEPDV3(logFile);
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(activityType, severity), data)), activityType+" Logs does not match" ); 
		
	}
	
	@DataProvider
	public Object[][] _361_362_363_364() {
		return new Object[][]{
			//Activity type  Object Type  Object Name     comment      Severity          action
			{ "Post Comment",  "File", "this is audio file", "Test.mp3", "informational","FileBox,Comment_on_test_mp3.log"},
			
		};
	}
	
	
	@Test(groups ={"TEST","FAIL","REGRESSION","BOX", "COMMENTS","QAVPC", "BOX", "PROD", "P2"},dataProvider = "_362")
	public void verify_User_replied_on_File_362(String activityType, String objectType, String objectName, String fileName, String severity, String logFile) throws IOException, Exception{
		Reporter.log("Validate User replied on comment ", true);
		String expectedMsg="User replied to a comment "+objectName+" on "+fileName;
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		data.put("Object_type", objectType);
		//data.put("Object_name", objectName);
		replayLogsEPDV3(logFile);
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(activityType, severity), data)), activityType+" Logs does not match" ); 
		
	}
	
	@DataProvider
	public Object[][] _362() {
		return new Object[][]{
			//Activity type  Object Type  Object Name     comment      Severity          action
			{ "Post Comment",  "File","@Test User1 ok", "Test.mp3", "informational","FileBox,Reply_on_comment.log"},
			
		};
	}
	
	
	@Test(groups ={"TEST","FAIL","REGRESSION","BOX", "COMMENTS","QAVPC", "BOX", "PROD", "P2"},dataProvider = "_363")
	public void verify_User_delete_comment_on_File_363(String activityType, String objectType, String fileName, String severity, String logFile) throws IOException, Exception{
		Reporter.log("Validate User deleted a comment ", true);
		String expectedMsg="User deleted a comment on "+fileName;
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		data.put("Object_type", objectType);
		//data.put("Object_name", objectName);
		replayLogsEPDV3(logFile);
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(activityType, severity), data)), activityType+" Logs does not match" ); 
		
	}
	
	@DataProvider
	public Object[][] _363() {
		return new Object[][]{
			//Activity type  Object Type  Object Name     comment      Severity          action
			{ "Delete Comment",  "File", "Test.mp3", "informational","FileBox,Delete_comment.log"},
			
		};
	}
	
	
	

	
	/*******************************************************
	 * Validate User created new task and assign to reviewer
	 * @param activityType
	 * @param objectType
	 * @param objectName
	 * @param severity
	 * @param email
	 * @throws IOException
	 * @throws Exception
	 */
	
	@Test(groups ={"TEST","REGRESSION","BOX","TASK","QAVPC", "P2"},dataProvider = "_365")
	public void verify_User_assigned_a_task_to_REVIER_against_file_365(String activityType, String objectType, String objectName, String severity,  String email, String logFile) throws IOException, Exception{
		Reporter.log("Validate User created new task and assign to reviewer ", true);
		String expectedMsg="User assigned a task to "+email+ " against file "+objectName;
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		data.put("Object_type", objectType);
		//data.put("Object_name", objectName);
		replayLogs(logFile);
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(activityType, severity), data)), activityType+" Logs does not match" ); 
		
	}
	
	@DataProvider
	public Object[][] _365() {
		return new Object[][]{
			//Activity type  Object Type  _Object Name   Severity  message
			{ "Create Task",     "File", "", "informational", "testuser2@gatewaybeatle.com,", "testgdoc,taskCreateEditDelete.log"}//User assigned a task to mohammad.usman@elastica.co against file phone.rtf _Description: description here
			//phone.rtf
		};
	}
	
	/*********************************************************
	 * Validate User Create new tags
	 * @param activityType
	 * @param objectType
	 * @param tag
	 * @param severity
	 * @param node
	 * @throws IOException
	 * @throws Exception
	 */
	
	@Test(groups ={"TEST","REGRESSION","BOX", "TAG", "QAVPC", "P2"},dataProvider = "_366")
	public void verify_User_added_tags_to_NODE_366(String activityType, String objectType, String tag, String severity, String node, String logFile) throws IOException, Exception{
		Reporter.log("Validate User Create new tags ", true);
		String expectedMsg="User added tags "+tag+ " to "+node;
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		data.put("Object_type", objectType);
		replayLogsEPDV3(logFile);
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(activityType, severity), data)), activityType+" Logs does not match" ); 
		
	}
	
	@DataProvider
	public Object[][] _366() {
		return new Object[][]{
			//Activity type  Object Type   Tag         Severity  node
			{ "Create Tag",     "File/Folder", "AddTag", "informational", "Test.png", "FileBox,Add_single_tag.log"}, 
			{ "Create Tag",     "File/Folder", "Tag1, Tag2, Tag3", "informational", "Test.pem", "FileBox,Add_multi_tag.log"},
			
		};
	}
	
	
	@Test(groups ={"TEST","REGRESSION","BOX", "TAG", "QAVPC", "P2"},dataProvider = "_366A")
	public void verify_User_delete_tags_to_NODE_366A(String activityType, String objectType, String severity, String node, String logFile) throws IOException, Exception{
		Reporter.log("Validate User Create new tags ", true);
		String expectedMsg="User deleted tags from "+node;
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		data.put("Object_type", objectType);
		replayLogsEPDV3(logFile);
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(activityType, severity), data)), activityType+" Logs does not match" ); 
		
	}
	
	@DataProvider
	public Object[][] _366A() {
		return new Object[][]{
			//Activity type  Object Type   Tag         Severity  node
			{ "Delete Tag",     "File/Folder", "informational", "Test.png", "FileBox,Delete_tag.log"}, 
			
		};
	}
	
	
	/*************************************************
	 * Validate user delete Tags from Node
	 * @param activityType
	 * @param objectType
	 * @param severity
	 * @param node
	 * @throws IOException
	 * @throws Exception
	 */
	
	@Test(groups ={"TEST","REGRESSION","BOX", "TAG", "QAVPC", "P2"},dataProvider = "_367")
	public void verify_User_delete_tags_From_Node_367(String activityType, String objectType, String objectName, String severity, String logFile) throws IOException, Exception{
		Reporter.log("Validate user delete Tags from Node ", true);
		String expectedMsg="User deleted tags from "+objectName;
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		data.put("Object_type", objectType);
		replayLogsEPDV3(logFile);
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(activityType, severity), data)), activityType+" Logs does not match" ); 
		
	}
	
	@DataProvider
	public Object[][] _367() {
		return new Object[][]{
			//Activity type  Object Type            Severity  node
			{ "Delete Tag", "File/Folder", "Test.png", "informational", "FileBox,Delete_tag.log"} //User deleted tags from node
			
		};
	}
	/*********************************************************
	 * Validate User Searched for file
	 * @param activityType
	 * @param objectType
	 * @param objectName
	 * @param _LoginType
	 * @param severity
	 * @throws IOException
	 * @throws Exception
	 */
	
	@Test(groups ={"TEST","REGRESSION","BOX", "SEARCH", "P2"},dataProvider = "_369")
	public void verify_User_searched_for_files_369(String activityType, String objectType, String objectName, String _LoginType, String severity, String logFile) throws IOException, Exception{
		Reporter.log("Validate User Searched for file ", true);
		String expectedMsg="User searched for files "+objectName;
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		data.put("Object_type", objectType);
		//data.put("Object_name", objectName);
		replayLogs(logFile);
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(activityType, severity), data)), activityType+" Logs does not match" ); 
		
	}
	
	@DataProvider
	public Object[][] _369() {
		return new Object[][]{
			//Activity type  Object Type object Name _LoginType Event _EventType  Severity  message
			{ "Search",     "File/Folder", "pc", "Web", "informational", "Item,Box,search.log"}
			
		};
	}
	
	/************************************************
	 * Validate user previewed file
	 * @param activityType
	 * @param objectType
	 * @param objectName
	 * @param severity
	 * @throws IOException
	 * @throws Exception
	 */
	
	@Test(groups ={"TEST", "P2"},dataProvider = "_370")
	public void verify_User_previewed_FILE_370(String activityType, String objectType, String objectName, String severity, String folder, String preview, String previewEvent, String logFile) throws IOException, Exception{
		Reporter.log("Validate user previewed file ", true);
		String expectedMsg="User previewed "+objectName;
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		data.put("Object_type", objectType);
		//data.put("Object_name", objectName);
		replayLogsEPDV3(logFile);
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(activityType, severity), data)), activityType+" Logs does not match" ); 
		
	}
	
	@DataProvider
	public Object[][] _370() {
		return new Object[][]{
			//Activity type  Object Type  Object Name  Severity
			{ "View",     "File/Folder", "Test.exe",  "informational", "All files", "Preview",  "Preview", "FileBox,Preview_test_exe.log"},
			{ "View",     "File/Folder", "Test.mp3", "informational", "All files", "Preview",  "Preview","FileBox,Preview_test_mp3.log"},
			{ "View",     "File/Folder", "Test.mp4", "informational", "All files", "Preview",  "Preview", "FileBox,Preview_test_mp4.log"},
			{ "View",     "File/Folder", "Test.pages", "informational", "All files", "Preview",  "Preview", "FileBox,Preview_test_pages.log"},
			{ "View",     "File/Folder", "Test.png","informational", "All files", "Preview",  "Preview", "FileBox,Preview_test_png.log"}, 
//			{ "View",     "File/Folder", "Test.txt",           "informational", "All files", "Preview",  "Preview", "Box,previewed_Test_txt.log"},    
//			
//			{ "View",     "File/Folder", "Test.boxnote",           "informational", "All files", "Preview",  "Preview", "Box,previewed_Test_boxnote.log"},// need to new record log
			
		};
	}
	
	/**********************************************
	 * Validate user contents of folder trash
	 * @throws IOException
	 * @throws Exception
	 */
	
	@Test(groups ={"TEST","REGRESSION","BOX", "TRASH","QAVPC", "P2"})
	public void verify_User_viewed_contents_of_folder_Trash_371() throws IOException, Exception{
		Reporter.log("Validate user viewed contents of folder trash", true);
		String expectedMsg="User viewed contents of folder Trash";
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		replayLogsEPDV3("BoxFile,Viewed_Items_from_Trash.log");
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), "Logs does not match" ); 
		
	}
	
	/********************************************************
	 * Validate User viewed contents of folder_X
	 * @param activityType
	 * @param objectType
	 * @param objectName
	 * @param severity
	 * @throws IOException
	 * @throws Exception
	 */
	

	
		/*******************************************************
		 * Validate User viewed contents of All Files and Folder
		 * @param activityType
		 * @param objectType
		 * @param objectName
		 * @param severity
		 * @throws IOException
		 * @throws Exception
		 */
		/***************************************************
		 * Validate User shared node X with email Y
		 * @param activityType
		 * @param objectType
		 * @param nodeName
		 * @param severity
		 * @param email
		 * @throws IOException
		 * @throws Exception
		 */
		
		//Deprecated
		@Test(groups ={"REGRESSION","BOX", "SHARE","QAVPC", "P1"},dataProvider = "_375")
		public void verify_User_shared_node_with_success_emails_list_375(String activityType, String objectType, String nodeName, String severity, String email) throws IOException, Exception{
			Reporter.log("Validate User shared node X with email Y ", true);
			String expectedMsg="User shared "+nodeName+" with "+email;
			data.put("message", expectedMsg);
			data.put("account_type", "Internal");
			data.put("Object_type", objectType);
			//replayLogs(logFile);
			assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(activityType, severity), data)), activityType+" Logs does not match" ); 
			
		}
		
		@DataProvider
		public Object[][] _375() {
			return new Object[][]{
				//Activity type  Object Type node Object Name  Severity
				{ "Share",  "File/Folder", "Boxnote.boxnote", "informational", "admin@gatewaybeatle.com" } //node
				
			};
		}
	
		
		
		/****************************************************
		 * Validate User shared node x with email y 
		 * @param activityType
		 * @param objectType
		 * @param objectName
		 * @param severity
		 * @param permission
		 * @throws IOException
		 * @throws Exception
		 */
		//
		@Test(groups ={"TEST","FAIL","REGRESSION","BOX", "SHARE", "QAVPC", "PROD","P1"},dataProvider = "_377A")
		public void verify_User_obtained_the_link_of_item_and_shared_it_with_access_level_377A(String activityType, String objectType, String objectName, String severity, String permission, String logFile) throws IOException, Exception{
			Reporter.log("Validate User shared the link of item with access level ", true);
			String expectedMsg="User shared "+objectName+" with "+permission;
			data.put("message", expectedMsg);
			data.put("account_type", "Internal");
			data.put("Object_type", objectType);
			//data.put("Object_name", objectName);
			replayLogsEPDV3(logFile);
			assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(activityType, severity), data)), activityType+" Logs does not match" ); 
			
		}
		
		
		@DataProvider
		public Object[][] _377A() {
			return new Object[][]{
				//Activity type  Object Type node Object Name  Severity
				{ "Share",  "File/Folder", "Test.doc", "informational", "testuser1@gatewaybeatle.com", "BoxFile,Shared_File_with_company.log" },//Anyone with the link
//				{ "Share",  "Folder", "GFolder", "informational", "Anyone with the links.", "GFolder,ShareThisFolder.log" }//Anyone with the link
				
				
				
			};
		}
		
		@Test(groups ={"TEST","REGRESSION","BOX", "SHARE", "QAVPC", "P1"},dataProvider = "_377B")
		public void verify_User_obtained_the_link_of_item_and_shared_it_with_access_level_377B(String activityType, String objectType, String objectName, String severity, String permission, String logFile) throws IOException, Exception{
			Reporter.log("Validate User shared the link of item with acces slevel ", true);
			String expectedMsg="User obtained the public link of "+objectName;
			data.put("message", expectedMsg);
			data.put("account_type", "Internal");
			data.put("Object_type", objectType);
			//data.put("Object_name", objectName);
			replayLogs(logFile);
			assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(activityType, severity), data)), activityType+" Logs does not match" ); 
			
		}
		
		
		@DataProvider
		public Object[][] _377B() {
			return new Object[][]{
				//Activity type  Object Type node Object Name  Severity
				{ "Share",  "File", "Test.txt", "informational", "People in this folder", "Box,shareFolder_people_with_link_Test_txt.log" } //need new log
				
			};
		}
	
		/*************************************************
		 * Validate User invited email for collaboration of folder Y with permissions
		 * @param activityType
		 * @param objectType
		 * @param objectName
		 * @param severity
		 * @param email
		 * @param permission
		 * @throws IOException
		 * @throws Exception
		 */
		
		@Test(groups ={"TEST","FAIL","REGRESSION","BOX", "SHARE",  "P1"},dataProvider = "_379")
		public void verify_User_invited_email_for_collaboration_of_folder_with_permissions_379(String activityType, String objectType, String objectName, String severity, String email, String permission, String logFile) throws IOException, Exception{
			Reporter.log("Validate User invited email for collaboration of folder Y with permissions ", true);
			String expectedMsg="User invited "+email+" for collaboration of folder "+objectName+ " with permissions "+permission;
			data.put("message", expectedMsg);
			data.put("account_type", "Internal");
			data.put("Object_type", objectType);
			//data.put("Object_name", objectName);
			replayLogsEPDV3(logFile);
			assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(activityType, severity), data)), activityType+" Logs does not match" ); 
			
		}
		
		@DataProvider
		public Object[][] _379() {
			return new Object[][]{
				//Activity type  Object Type node Object Name  Severity
				{ "Share",  "Folder", "f4a2b168-8e63-4649-a7f9-f1fd62cfde21", "informational","mohd.afjal@elastica.co", "Editor", "FolderBox,Invite_collaborator_with_editor.log"},
				{ "Share",  "Folder", "AFolder", "informational","mohd.afjal@elastica.co", "Viewer", "FileBox,Invite_collaborator_with_viewer.log"},
				{ "Share",  "Folder", "AFolder", "informational","mohammad.usman@elastica.co", "Viewer", "FileBox,Invite_collaborator_with_viewer.log"},
				{ "Share",  "Folder", "5812269e-6200-45ca-ba07-7b30bf7261e2", "informational","usman.kec@gmail.com", "Previewer Uploader", "FolderBox,Invite_collaborator_with_previewer_uploader.log"},
				{ "Share",  "Folder", "5812269e-6200-45ca-ba07-7b30bf7261e2", "informational","mohd.afjal@elastica.co", "Uploader", "FolderBox,Invite_collaborator_with_uploader.log"},
				{ "Share",  "Folder", "f4a2b168-8e63-4649-a7f9-f1fd62cfde21", "informational","mohd.afjal@elastica.co", "Previewer", "FolderBox,Invite_collaborator_with_previewer.log"},
				{ "Share",  "Folder", "5812269e-6200-45ca-ba07-7b30bf7261e2", "informational","mohammad.usman@elastica.co", "Viewer Uploader", "FolderBox,Invite_collaborator_with_viewer_uploader.log"}
				
			};
		}
	
		/******************************************************
		 * Validate User resent collaboration invitation to email
		 * @param activityType
		 * @param objectType
		 * @param severity
		 * @param email
		 * @throws IOException
		 * @throws Exception
		 */
		
		
		/*********************************************************
		 * Validate User submitted ownership on folder to email
		 * @param activityType
		 * @param objectType
		 * @param objectName
		 * @param severity
		 * @param email
		 * @throws IOException
		 * @throws Exception
		 */
		
		/*****************************************************
		 * Validate User changed the collaboration permission
		 * @param activityType
		 * @param objectType
		 * @param objectName
		 * @param severity
		 * @param email
		 * @param permission
		 * @throws IOException
		 * @throws Exception
		 */
		
		@Test(groups ={"TEST","FAIL","REGRESSION","BOX", "SHARE", "QAVPC", "P1"},dataProvider = "_382_383")
		public void verify_User_changed_access_of_collaborator_email_on_folder_Permission_to_Viewer_Editor_382_383(String activityType, String objectType, String objectName, String severity, String email, String permission, String logFile) throws IOException, Exception{
			Reporter.log("Validate User chnaged the collaboration permission", true);
			String expectedMsg="User changed access of collaborator "+email+" on folder "+objectName+ " to "+permission;
			data.put("message", expectedMsg);
			data.put("account_type", "Internal");
			data.put("Object_type", objectType);
			//data.put("_Object_name", objectName);
			replayLogsEPDV3(logFile);
			assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(activityType, severity), data)), activityType+" Logs does not match" ); 
			
		}
		
		@DataProvider
		public Object[][] _382_383() {
			return new Object[][]{
				//Activity type  Object Type node Object Name  Severity
				//External Users (External collaborator)
				{ "Modify Permissions",  "Folder", "AFolder","informational", "mohammad.usman@elastica.co", "Viewer", "FileBox,Access_collaborator_with_viewer.log" }, //AFolder
				{ "Modify Permissions",  "Folder", "AFolder","informational", "mohammad.usman@elastica.co", "Uploader", "FileBox,Access_collaborator_with_uploader.log" }, //AFolder
				{ "Modify Permissions",  "Folder", "AFolder","informational", "mohd.afjal@elastica.co", "Previewer Uploader", "FileBox,Access_collaborator_with_previewer_uploader.log" }, //AFolder
//				{ "Modify Permissions",  "Folder", "AFolder","informational", "usman.kec@gmail.com", "Owner", "FileBox,Access_collaborator_with_owner.log" }, //AFolder
				{ "Modify Permissions",  "Folder", "AFolder","informational", "mohd.afjal@elastica.co", "Viewer Uploader", "FileBox,Access_collaborator_with_viewer_uploader.log" }, 
				{ "Modify Permissions",  "Folder", "AFolder","informational", "mohd.afjal@elastica.co", "Editor", "FileBox,Access_collaborator_with_editor.log" }, 
				{ "Modify Permissions",  "Folder", "AFolder","informational", "mohd.afjal@elastica.co", "Previewer Uploader", "FileBox,Access_collaborator_with_previewer_uploader.log" }, //AFolder
				
				//Internal User (Internal collaborator)
//				{ "Modify Permissions",  "Folder", "AFolder","informational", "box-admin@gatewaybeatle.com", "Viewer", "Edit,View,EditPermission_internal_user.log" } //AFolder
				
			};
		}
		
		/**********************************************************
		 * Validate User removed collaborator from folder
		 * @param activityType
		 * @param objectType
		 * @param objectName
		 * @param severity
		 * @param email
		 * @throws IOException
		 * @throws Exception
		 */
		@Test(groups ={"TEST","FAIL","REGRESSION","BOX", "SHARE","QAVPC",  "P1"},dataProvider = "_384")
		public void verify_User_removed_collaborator_email_from_folder_384(String activityType, String objectType, String objectName,  String severity, String email, String logFile) throws IOException, Exception{
			Reporter.log("Validate User removed collaborator from folder", true);
			String expectedMsg="User removed collaborator "+email+" from folder "+objectName;
			data.put("message", expectedMsg);
			data.put("account_type", "Internal");
			data.put("Object_type", objectType);
			//data.put("Object_name", objectName);
			replayLogsEPDV3(logFile);
			assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(activityType, severity), data)), activityType+" Logs does not match" ); 
			
		}
		
		@DataProvider
		public Object[][] _384() {
			return new Object[][]{
				//Activity type  Object Type node Object Name  Severity
				//Internal user
		//		{ "Unshare",  "Folder", "AFolder", "informational", "", "BoxFolder,Removed_Collaborator_Editor.log" }, //need new log
				//External User
				{ "Unshare",  "Folder", "AFolder", "informational", "", "BoxFolder,Removed_Collaborator_Editor.log" } //need new log
				
			};
		}
		
		
		/******************************************************
		 * Validate User download a file
		 * @param activityType
		 * @param objectType
		 * @param objectName
		 * @param severity
		 * @throws IOException
		 * @throws Exception
		 */
		@Test(groups ={"TEST", "REGRESSION","BOX", "SHARE", "QAVPC",  "P1"},dataProvider = "_385")
		public void verify_User_downloaded_File_385(String activityType, String objectType, String objectName, String severity, String logFile) throws IOException, Exception{
			Reporter.log("Validate User downlaod a file", true);
			String expectedMsg="User downloaded "+objectName;
			data.put("message", expectedMsg);
			data.put("account_type", "Internal");
			data.put("Object_type", objectType);
			//data.put("Object_name", objectName);
			replayLogs(logFile);
			assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(activityType, severity), data)), activityType+" Logs does not match" ); 
			
		}
		
		@DataProvider
		public Object[][] _385() {
			return new Object[][]{
				//Activity type  Object Type node Object Name  Severity
				{ "Download",  "File",         "1.png", 		"informational", 	"downloadfile.log" },
				{ "Download",  "File",        "Test.txt", 	"informational", 	"TestFile,AllFiles,download_txt.log"  },
				{ "Download",  "File",         "Test.7z", 	"informational", 	"TestFile,AllFiles,download_7z.log"   },
				{ "Download",  "File",         "Test.doc", 	"informational", 	"TestFile,AllFiles,download_doc.log"  },
				{ "Download",  "File",         "Test.docx", 	"informational", 	"TestFile,AllFiles,download_docx.log" },
				{ "Download",  "File",         "Test.java", 	"informational", 	"TestFile,AllFiles,download_java.log" },
				{ "Download",  "File",         "Test.xlsm", 	"informational", 	"TestFile,AllFiles,download_xlsm.log" },
				{ "Download",  "File",         "Test.xls", 	"informational", 	"TestFile,AllFiles,download_xls.log"  },
				//{ "Download",  "File",         "Test.exe",   "informational",    "TestFile,AllFiles,download_exe.log" },
				{ "Download",  "File",        "Test.ods", 	"informational", 	"TestFile,AllFiles,download_ods.log"  },
				{ "Download",  "File",        "Test.pdf", 	"informational", 	"TestFile,AllFiles,download_pdf.log"  },
				{ "Download",  "File",        "Test.rar", 	"informational", 	"TestFile,AllFiles,download_rar.log"  },
				{ "Download",  "File",        "Test.rtf", 	"informational", 	"TestFile,AllFiles,download_rtf.log"  },
				{ "Download",  "File",        "Test.tgz", 	"informational", 	"TestFile,AllFiles,download_tgz.log"  },
				{ "Download",  "File",        "Test.xlsx", 	"informational", 	"TestFile,AllFiles,download_xlsx.log" },
				{ "Download",  "File",        "Test.xml", 	"informational", 	"TestFile,AllFiles,download_xml.log"  },
				{ "Download",  "File",        "Test.zip", 	"informational", 	"TestFile,AllFiles,download_zip.log"  }
				
			
			};
		}
		
		/*******************************************
		 * Validate user deleted contents of folder trash
		 * @throws IOException
		 * @throws Exception
		 */

		@Test(groups ={"TEST","FAIL1","REGRESSION", "TRASH", "P2"})
		public void verify_User_emptied_trash_386() throws IOException, Exception{
			Reporter.log("Validate user deleted contents of folder trash", true);
			String expectedMsg="User viewed contents of folder Trash";
			data.put("message", expectedMsg);
			data.put("account_type", "Internal");
			replayLogsEPDV3("FileBox,Empty_trash.log");
			assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), "Logs does not match" ); 
			
		}
		
		@DataProvider
		public Object[][] _386() {
			return new Object[][]{
				//Activity type  Object Type node Object Name  Severity
				{ "Delete", "File/Folder", "informational", "FileBox,Empty_trash.log" }
			};
		}
		
		/***************************************
		 * Validate User delete files objects
		 * @param activityType
		 * @param objectType
		 * @param objectName
		 * @param severity
		 * @throws IOException
		 * @throws Exception
		 */
		
		@Test(groups ={"TEST", "FAIL1","REGRESSION","BOX", "SHARE",  "P1", "SANITY"},dataProvider = "_387")
		public void verify_User_deleted_objects_Files_Folders_387(String activityType, String objectType, String objectName, String severity, String logFile) throws IOException, Exception{
			Reporter.log("Validate User delete files objects", true);
			String expectedMsg="User deleted object(s) "+objectName;
			data.put("message", expectedMsg);
			data.put("account_type", "Internal");
			data.put("Object_type", objectType);
			//data.put("Object_name", objectName);
			replayLogsEPDV3(logFile);
			assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(activityType, severity), data)), activityType+" Logs does not match" ); 
			
		}
		
		@DataProvider
		public Object[][] _387() {
			return new Object[][]{
				//Activity type  Object Type node Object Name  Severity
				{ "Delete",  "File/Folder", "GWAuto", "informational" , "FolderBox,Deleted_folder.log"} ,//AFolder"
				
			};
		}
		
		
		/*********************************************
		 * Validate User set expiration for the link
		 * @param activityType
		 * @param objectType
		 * @param objectName
		 * @param severity
		 * @throws IOException
		 * @throws Exception
		 */
		@Test(groups ={"TEST","REGRESSION","BOX", "SHARE", "QAVPC", "P1"},dataProvider = "_388")
		public void verify_User_set_expiration_for_link_Name_388(String activityType, String objectType, String objectName, String severity, String logFile) throws IOException, Exception{
			Reporter.log("Validate User set expiration for the link", true);
			String expectedMsg="User set expiration for link on "+objectName;
			data.put("message", expectedMsg);
			data.put("account_type", "Internal");
			data.put("Object_type", objectType);
			//data.put("Object_name", objectName);
			replayLogsEPDV3(logFile);
			assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(activityType, severity), data)), activityType+" Logs does not match" ); 
			
		}
		
		@DataProvider
		public Object[][] _388() {
			return new Object[][]{
				//Activity type  Object Type node Object Name  Severity
				{ "Set Link Expiry",  "File", "Test.mp3", "informational", "BoxFile,Set_Expiration_shared_file.log" }
			};
		}
		
		/***************************************
		 * Validate User set expiration for the link
		 * @param activityType
		 * @param objectType
		 * @param objectName
		 * @param severity
		 * @param duration
		 * @throws IOException
		 * @throws Exception
		 */
		
		@Test(groups ={"TEST","FAIL","REGRESSION","BOX", "LOCK", "P1"},dataProvider = "_389")
		public void verify_User_locked_file_for_seconds_389(String activityType, String objectType, String objectName, String severity, String duration, String logFile) throws IOException, Exception{
			Reporter.log("Validate User locked the file", true);
			String expectedMsg="User locked "+objectName+" for "+duration+" seconds";
			data.put("message", expectedMsg);
			data.put("account_type", "Internal");
			data.put("Object_type", objectType);
			//data.put("Object_name", objectName);
			replayLogsEPDV3(logFile);
			assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(activityType, severity), data)), activityType+" Logs does not match" ); 
			
		}
		
		@DataProvider
		public Object[][] _389() {
			return new Object[][]{
				//Activity type  Object Type node Object Name  Severity
				{ "Lock",  "File", "Test.7z", "informational", "300","BoxFile,Locked_file_300_sec.log" },
//				{ "Lock",  "File", "Test.docx", "informational", "0","Test,AFolder,unlockFileTestdoc.log" }
			};
		}
		
		/*************************************************
		 * Validate User unlocked the file
		 * @param activityType
		 * @param objectType
		 * @param objectName
		 * @param severity
		 * @throws IOException
		 * @throws Exception
		 */
		@Test(groups ={"TEST","REGRESSION","BOX", "UNLOCKED", "P1"},dataProvider = "_390")
		public void verify_User_unlocked_File_390(String activityType, String objectType, String objectName, String severity, String logFile) throws IOException, Exception{
			Reporter.log("Validate User unlocked the file", true);
			String expectedMsg="User unlocked "+objectName;
			data.put("message", expectedMsg);
			data.put("account_type", "Internal");
			data.put("Object_type", objectType);
			//data.put("Object_name", objectName);
			replayLogsEPDV3(logFile);
			assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(activityType, severity), data)), activityType+" Logs does not match" ); 
			
		}
		
		@DataProvider
		public Object[][] _390() {
			return new Object[][]{
				//Activity type  Object Type node Object Name  Severity
				{ "Unlock",  "File", "Test.doc", "informational", "BoxFile,Unlocked_file.log" }
			};
		}
		
		/************************************************
		 * Validate User updated the properties of file
		 * @param activityType
		 * @param objectType
		 * @param severity
		 * @param oldDescription
		 * @param newDescription
		 * @param fileName
		 * @throws IOException
		 * @throws Exception
		 */
		//@Test(groups ={"TEST2","REGRESSION","BOX", "EDIT PPRPERTIES", "QAVPC", "P1"},dataProvider = "_391")
		public void verify_User_updated_description_to_and_filename_to_File_391(String activityType, String objectType, String severity, String oldDescription, String newDescription, String fileName, String logFile) throws IOException, Exception{
			Reporter.log("Validate User updated the properties of file", true);
			String expectedMsg="User updated "+oldDescription+ " to "+newDescription+" and filename to "+fileName; 
			data.put("message", expectedMsg);
			data.put("account_type", "Internal");
			data.put("Object_type", objectType);
			replayLogs(logFile);
			assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(activityType, severity), data)), activityType+" Logs does not match" ); 
			
		}
		
		@DataProvider
		public Object[][] _391() {
			return new Object[][]{
				//Activity type  Object Type node Object Name  Severity
				{ "Edit Properties",  "File", "informational", "oldDesc", "ddd", "", "AFile,AFolder,propertiesgDoc.log"}
			};
		}
		
		
		
		/*************************************************
		 * Validate User moved items to folder
		 * @param activityType
		 * @param objectType
		 * @param objectName
		 * @param severity
		 * @param targetFolder
		 * @throws IOException
		 * @throws Exception
		 */
		
		@Test(groups ={"TEST","REGRESSION","BOX", "MOVE","QAVPC","P1", "SANITY"},dataProvider = "_393")
		public void verify_User_moved_items_to_Folder_393(String activityType, String objectType, String objectName, String severity, String targetFolder, String logFile) throws IOException, Exception{
			Reporter.log("Validate User moved items to folder", true);
			String expectedMsg="User moved item(s) "+objectName+" to "+targetFolder;
			data.put("message", expectedMsg);
			data.put("account_type", "Internal");
			data.put("Object_type", objectType);
			//data.put("Object_name", objectName);
			replayLogsEPDV3(logFile);
			assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(activityType, severity), data)), activityType+" Logs does not match" ); 
			
		}
		
		@DataProvider
		public Object[][] _393() {
			return new Object[][]{
				//Activity type  Object Type node Object Name  Severity
				{ "Move",  "File/Folder","Test.ott", "informational", "f4a2b168-8e63-4649-a7f9-f1fd62cfde21", "FileBox,Move_file1.log"} //Boxnote (1).boxnote CFolder
			};
		}
		
		/**********************************************
		 * Validate User copied items to folder
		 * @param activityType
		 * @param objectType
		 * @param objectName
		 * @param severity
		 * @param targetFolder
		 * @throws IOException
		 * @throws Exception
		 */
		@Test(groups ={"TEST","REGRESSION","BOX", "COPY", "P1"},dataProvider = "_394")
		public void verify_User_copied_item_to_Folder_394(String activityType, String objectType, String objectName, String severity, String targetFolder, String logFile) throws IOException, Exception{
			Reporter.log("Validate User copied items to folder", true);
			String expectedMsg="User copied item(s) "+objectName+ " to "+targetFolder;
			data.put("message", expectedMsg);
			data.put("account_type", "Internal");
			data.put("Object_type", objectType);
		//	data.put("Object_name", objectName);
			replayLogsEPDV3(logFile);
			assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(activityType, severity), data)), activityType+" Logs does not match" ); 
			
		}
		
		@DataProvider
		public Object[][] _394() {
			return new Object[][]{
				//Activity type  Object Type node Object Name  Severity
				{ "Copy",  "File/Folder", "Auto.boxnote", "informational", "AFolder", "FileBox,copy.log"},//Apidata.txt
				
			};
		}
		
		
		/************************************************
		 * Validate user restored All items from trash
		 * @throws IOException
		 * @throws Exception
		 */
		
		@Test(groups ={"TEST","REGRESSION", "RESTORED", "P1"})
		public void verify_User_restored_all_the_items_from_trash_395() throws IOException, Exception{
			Reporter.log("Validate user restored All items from trash", true);
			String expectedMsg=userLitral+" restored object Test.png from trash";
			replayLogsEPDV3("BoxFile,Restored_from_Trash.log");
			data.put("message", expectedMsg);
			data.put("account_type", "Internal");
			assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)),  "Logs does not match" ); 
			
		}
		
		
		/********************************************
		 * Validate User restored items from Trash
		 * @param activityType
		 * @param objectType
		 * @param objectName
		 * @param severity
		 * @throws IOException
		 * @throws Exception
		 */
		@Test(groups ={"TEST", "REGRESSION","BOX", "RESTORED", "P1"},dataProvider = "_396")
		public void verify_User_restored_object_from_trash_396(String activityType, String objectType, String objectName, String severity, String logFile) throws IOException, Exception{
			Reporter.log("Validate User restored items from Trash", true);
			String expectedMsg="User restored object "+objectName+" from trash";
			replayLogs(logFile);
			data.put("message", expectedMsg);
			data.put("account_type", "Internal");
			data.put("Object_type", objectType);
			//data.put("Object_name", objectName);
			replayLogsEPDV3(logFile);
			assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(activityType, severity), data)), activityType+" Logs does not match" ); 
			
		}
		
		@DataProvider
		public Object[][] _396() {
			return new Object[][]{
				//Activity type  Object Type node Object Name  Severity
				{ "Restore",  "File/Folder", "Test.png", "informational", "BoxFile,Restored_from_Trash.log"} //Boxnote.boxnote
			};
		}
		
		
		/***********************************************
		 * Validate User downloaded item
		 * @param activityType
		 * @param objectType
		 * @param objectName
		 * @param severity
		 * @throws IOException
		 * @throws Exception
		 */
		@Test(groups ={"TEST", "REGRESSION","BOX", "DOWNLOAD", "QAVPC",  "P1"},dataProvider = "_397")
		public void verify_User_downloaded_folder_397(String activityType, String objectType, String objectName, String severity, String logFile) throws IOException, Exception{
			Reporter.log("Validate User downloaded item", true);
			String expectedMsg=userLitral+" downloaded file(s)/folder(s) as zip named "+objectName;
			data.put("message", expectedMsg);
			data.put("account_type", "Internal");
			data.put("Object_type", objectType);
		    data.put("_ObjectName", objectName);
			replayLogs(logFile);
			assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(activityType, severity), data)), activityType+" Logs does not match" ); 
			
		}
		
		@DataProvider
		public Object[][] _397() {
			return new Object[][]{
				//Activity type  Object Type node Object Name  Severity
				{ "Download",  "File/Folder", "ZFolder", "informational", "download,DownloadFolder.log"} //Actvity type Folder
			};
		}
		
		
		/*****************************************************
		 * Validate User viewed contacts and collaborators page
		 * @throws IOException
		 * @throws Exception
		 */
		
		//@Test(groups ={"TEST","REGRESSION","BOX", "VIEW","QAVPC", "P2"})
		public void verify_User_viewed_Contacts_and_Collaborators_page_398() throws IOException, Exception{
			Reporter.log("Validate User viewed contacts and collaborators page", true);
			String expectedMsg=userLitral+" viewed Contacts and Collaborators page";
			replayLogs("AFolder,AFolder,viewCollaborator.log");
			data.put("message", expectedMsg);
			data.put("account_type", "Internal");
			assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), "Logs does not match" ); 
			
		}
		
		/****************************************************
		 * Validate User viewed Applications page
		 * @throws IOException
		 * @throws Exception
		 */
		@Test(groups ={"TEST","REGRESSION","BOX", "VIEW", "QAVPC","P2"})
		public void verify_User_viewed_Applications_page_399() throws IOException, Exception{
			Reporter.log("Validate User viewed Applications page", true);
			String expectedMsg=userLitral+" viewed Applications page";
			replayLogsEPDV3("FileBox,View_app_page.log");
			data.put("message", expectedMsg);
			data.put("account_type", "Internal");
			assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), "Logs does not match" ); 
			
		}
		
		/**************************************************
		 * Validate User viewed Acounts and Setting page
		 * @throws IOException
		 * @throws Exception
		 */
		@Test(groups ={"TEST","REGRESSION","BOX", "VIEW", "QAVPC", "P2"})
		public void verify_User_viewed_Account_Settings_page_400() throws IOException, Exception{
			Reporter.log("Validate User viewed Acounts and Setting page", true);
			String expectedMsg="User viewed Account Settings page";
			replayLogsEPDV3("FileBox,View_app_setting.log");
			data.put("message", expectedMsg);
			data.put("account_type", "Internal");
			assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), "Logs does not match" ); 
			
		}
		
		/************************************************
		 * Validate User viewed profile page
		 * @throws IOException
		 * @throws Exception
		 */
		@Test(groups ={"TEST","REGRESSION","BOX", "VIEW", "QAVPC", "P2"})
		public void verify_User_viewed_Profile_page_401() throws IOException, Exception{
			Reporter.log("Validate User viewed profile page", true);
			String expectedMsg="User viewed Applications page";
			replayLogsEPDV3("FileBox,View_app_page.log");
			data.put("message", expectedMsg);
			data.put("account_type", "Internal");
			assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), "Logs does not match" ); 
			
		}
		
		
		/*************************************************************
		 * Validate User shared folder file with permissions with email
		 * @param activityType
		 * @param objectType
		 * @param objectName
		 * @param severity
		 * @param permission
		 * @param email
		 * @throws IOException
		 * @throws Exception
		 */
		
		//@Test(groups ={"REGRESSION","BOX","SHARE", "QAVPC",  "P1"},dataProvider = "_403")
		public void verify_User_shared_folder_fileName_with_collaboration_permissions_for_email_403(String activityType, String objectType, String objectName, String severity, String permission, String email) throws IOException, Exception{
			Reporter.log("Validate User shared folder file with permissions with email", true);
			String expectedMsg=userLitral+" shared folder "+objectName+ " with collaboration permissions "+permission+" for "+email;
			data.put("message", expectedMsg);
			data.put("account_type", "Internal");
			data.put("Object_type", objectType);
			//data.put("Object_name", objectName);
			//replayLogs(logFile);
			assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(activityType, severity), data)), activityType+" Logs does not match" ); 
			
		}
		
		@DataProvider
		public Object[][] _403() {
			return new Object[][]{
				//Activity type  Object Type node Object Name  Severity  email
				{ "Share",  "Folder", "filename", "informational", "View", "90.abbasfaisal@gmail.com" }
			};
		}
		
		/*******************************************************************************
		 * Validate User edited properties of the file with new file Name and description
		 * @param activityType
		 * @param objectType
		 * @param severity
		 * @param fileName
		 * @param newFileName
		 * @param description
		 * @throws IOException
		 * @throws Exception
		 */
		//@Test(groups ={"TEST2","REGRESSION","BOX", "SHARE", "QAVPC","DEV",  "P1"},dataProvider = "_405")
		public void verify_User_edited_properties_of_the_file_with_name_and_description_405(String activityType, String objectType, String severity, String fileName, String newFileName, String description, String logFile) throws IOException, Exception{
			Reporter.log("Validate User edited properties of the file with new file Name and description", true);
			String expectedMsg="User edited properties of the file "+fileName+ " with name "+newFileName+" and description "+description;
			data.put("message", expectedMsg);
			data.put("account_type", "Internal");
			data.put("Object_type", objectType);
			replayLogs(logFile);
			assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(activityType, severity), data)), activityType+" Logs does not match" ); 
			
		}
	
		@DataProvider
		public Object[][] _405() {
			return new Object[][]{
				//Activity type  Object Type node Object Name  Severity  email
				{ "Edit Properties",  "File", "informational", "fileName", "newFileName", "description", "AFile,AFolder,propertiesgDoc.log" }
			};
		}
		
		
		/************************************************************************************
		 * Validate User edited properties of the folder file. The properties set as true are
		 * @param activityType
		 * @param objectType
		 * @param severity
		 * @param fileName
		 * @param property
		 * @throws IOException
		 * @throws Exception
		 */
		//@Test(groups ={"REGRESSION","BOX", "SHARE", "QAVPC", "P1"},dataProvider = "_406")
		public void verify_User_edited_properties_of_the_folder_FileName_The_properties_set_as_true_are_property_406(String activityType, String objectType, String severity, String fileName, String property) throws IOException, Exception{
			Reporter.log("Validate User edited properties of the folder file. The properties set as true are", true);
			String expectedMsg="User edited properties of the folder "+fileName+ ". The properties set as true are : "+property;
			data.put("message", expectedMsg);
			data.put("account_type", "Internal");
			data.put("Object_type", objectType);
			//replayLogs(logFile);
			assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(activityType, severity), data)), activityType+" Logs does not match" ); 
			
		}
	
		@DataProvider
		public Object[][] _406() {
			return new Object[][]{
				//Activity type  Object Type node Object Name  Severity  email
				{ "Edit Properties",  "Folder", "informational", "fileName", "property" }
			};
		}
		
		/*************************************************************
		 * Validate User edited properties of the folder file. The properties set as true are
		 * @param activityType
		 * @param objectType
		 * @param objectName
		 * @param severity
		 * @param olderName
		 * @throws IOException
		 * @throws Exception
		 */
		//folder -> older name
		@Test(groups ={"TEST3","REGRESSION","BOX", "RENAME",  "P1"},dataProvider = "_407")
		public void verify_User_renamed_a_file_folder_to_407(String activityType, String objectType, String objectName, String severity, String olderName, String logFile) throws IOException, Exception{
			Reporter.log("Validate User edited properties of the folder file. The properties set as true are", true);
			String expectedMsg=userLitral+" renamed a "+objectType.toLowerCase()+ " "+olderName+ " to "+objectName;
			data.put("message", expectedMsg);
			data.put("account_type", "Internal");
			data.put("Object_type", objectType);
			//data.put("Object_name", objectName);
			replayLogs(logFile);
			assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(activityType, severity), data)), activityType+" Logs does not match" ); 
			
		}
	
		@DataProvider
		public Object[][] _407() {
			return new Object[][]{
				//Activity type  Object Type Object Name  Severity  olderName
				{ "Rename",  "File", "2.png","informational", "1.png","1.png,2.png,renamefile.log" },
				{ "Rename",  "Folder", "ZFolder","informational", "AFolder", "AFolder,ZFolder,RenameFolder.log" }
			};
		}
		
		
		/*************************************************************
		 * Validate User opened Web doc
		 * @param activityType
		 * @param objectType
		 * @param severity
		 * @param webDoc
		 * @throws IOException
		 * @throws Exception
		 */
		
		
		/**************************************************
		 * Validate User download multiple files
		 * @param activityType
		 * @param objectType
		 * @param objectName
		 * @param severity
		 * @throws IOException
		 * @throws Exception
		 */

		
		/***********************************************
		 * Validate User shared file anyone, people of company, people in this folder with the lin
		 * @param activityType
		 * @param objectType
		 * @param objectName
		 * @param severity
		 * @param with
		 * @throws IOException
		 * @throws Exception
		 */

		@Test(groups ={"TEST","FAIL1","REGRESSION","BOX", "SUBFOLDER_SHARE", "QAVPC",   "P1"},dataProvider = "_414_415_416")
		public void verify_User_shared_link_of_file_with_anyone_people_in_company_with_people_infolder_with_the_link_414_415_416(String activityType, String objectType, String objectName, String severity, String with, String logFile) throws IOException, Exception{
			Reporter.log("Validate User shared file anyone, people of company, people in this folder with the link", true);
			String expectedMsg="User shared link of "+objectName+ " "+with;
			data.put("message", expectedMsg);
			data.put("account_type", "Internal");
			data.put("Object_type", objectType);
			//data.put("Object_name", objectName);
			replayLogsEPDV3(logFile);
			assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(activityType, severity), data)), activityType+" Logs does not match" ); 
			
		}
		
		@DataProvider
		public Object[][] _414_415_416() {
			return new Object[][]{
				//Activity type  Object Type Object Name  Severity with
				{ "Share",  "File", "Test.doc", "informational", "with the people of company.", "BoxFile,Shared_File_with_company.log"},
				{ "Share",  "File", "Test.doc", "informational", "with the people of company.", "BoxFile,Shared_File_with_company.log"},
				{ "Share",  "File", "Test.doc", "informational", "with the people of company.", "BoxFile,Shared_File_with_company.log"},
				{ "Share",  "File", "Test.doc", "informational", "with the people of company.", "BoxFile,Shared_File_with_company.log"},
			};
		}
		
		
		/*************************************************
		 * Validate User created a boxnote in folder
		 * @param activityType
		 * @param objectType
		 * @param objectName
		 * @param severity
		 * @param targetFolder
		 * @throws IOException
		 * @throws Exception
		 */
		@Test(groups ={"TEST","REGRESSION","BOX", "DOWNLOAD", "QAVPC", "P1", "SANITY"},dataProvider = "_417")
		public void verify_User_created_new_boxnote_in_Folder_417(String activityType, String objectType, String objectName, String severity, String targetFolder, String logFile) throws IOException, Exception{
			Reporter.log("Validate User created a boxnote in folder", true);
			String expectedMsg="User created new boxnote "+objectName+" in "+targetFolder;
			data.put("message", expectedMsg);
			data.put("account_type", "Internal");
			data.put("Object_type", objectType);
			//data.put("Object_name", objectName);
			replayLogsEPDV3(logFile);
			assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(activityType, severity), data)), activityType+" Logs does not match" ); 
			
		}
		
		@DataProvider
		public Object[][] _417() {
			return new Object[][]{
				//Activity type  Object Type  Object Name  severity       
				{ "Create",   "File", "AutoTest.boxnote",  "informational", "All Files", "FileBox,create_BoxNote.log"} //targetFolder
			};
		}
		
		/*************************************************
		 * Validate User shared file anyone, people of company, people in this folder with the link
		 * @param activityType
		 * @param objectType
		 * @param objectName
		 * @param severity
		 * @throws IOException
		 * @throws Exception
		 */
		
		@Test(groups ={"TEST","FAIL","REGRESSION","BOX", "SHARE", "QAVPC", "P1"},dataProvider = "_419")
		public void verify_User_obtained_the_link_of_box_note_fileName_419(String activityType, String objectType, String objectName, String severity, String logFile) throws IOException, Exception{
			Reporter.log("Validate User obtained the link of box note", true);
			String expectedMsg="User got link of file "+objectName;
			data.put("message", expectedMsg);
			data.put("account_type", "Internal");
			data.put("Object_type", objectType);
			//data.put("Object_name", objectName);
			replayLogsEPDV3(logFile);
			assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(activityType, severity), data)), activityType+" Logs does not match" ); 
			
		}
		
		@DataProvider
		public Object[][] _419() {
			return new Object[][]{
				//Activity type  Object Type Object Name  Severity with
				{ "Share",  "File", "Test.doc", "informational", "BoxFile,Shared_File_with_company.log"}
			};
		}
		
		//@Test(groups ={"TEST45","REGRESSION","BOX", "SHARE", "QAVPC", "P1"},dataProvider = "_419A")
		public void verify_User_obtained_the_link_of_box_note_fileName_419A(String activityType, String objectType, String objectName, String severity, String logFile) throws IOException, Exception{
			Reporter.log("Validate User obtained the link of box note", true);
			String expectedMsg="User obtained the link of "+objectName;
			data.put("message", expectedMsg);
			data.put("account_type", "Internal");
			data.put("Object_type", objectType);
			//data.put("Object_name", objectName);
			replayLogs(logFile);
			assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(activityType, severity), data)), activityType+" Logs does not match" ); 
			
		}
		
		@DataProvider
		public Object[][] _419A() {
			return new Object[][]{
				//Activity type  Object Type Object Name  Severity with
				{ "Share",  "File", "Test.boxnote", "informational", "TestFile,AllFiles,share_boxnote.log"}
			};
		}
		
		/*********************************************
		 * Validate User removed collaboratoe email
		 * @param activityType
		 * @param objectType
		 * @param severity
		 * @param email
		 * @throws IOException
		 * @throws Exception
		 */
		@Test(groups ={"TEST","REGRESSION","BOX", "REMOVE_COLLABORATOR","QAVPC", "P2"},dataProvider = "_420")
		public void verify_User_removed_email_from_Collaborators_420(String activityType, String objectType, String severity, String email, String logFile) throws IOException, Exception{
			Reporter.log("Validate User removed collaboratoe email", true);
			String expectedMsg="User removed "+email+" from Collaborators.";
			data.put("message", expectedMsg);
			data.put("account_type", "Internal");
			data.put("Object_type", objectType);
			replayLogs(logFile);
			assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(activityType, severity), data)), activityType+" Logs does not match" ); 
			
		}
		
		@DataProvider
		public Object[][] _420() {
			return new Object[][]{
				//Activity type  Object Type Object Name  Severity with
				{ "Unshare",  "Folder",  "informational", "usman.kec@gmail.com", "AFolder,AFolder,viewCollaborator.log"}
			};
		}
		
}
