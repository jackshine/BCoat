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
 * @author usman
 *
 */

public class SalesforceTest extends CommonConfiguration {

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
	
	@Test(groups ={"TEST", "SESSION","P1","SANITY","QAVPC", "REGRESSION"},dataProvider = "_1024_1025_1026")
	public void verify_User_logged_in_1024_1025_1026(String activityType, String objectType, String severity, String expectedMsg, String logFile) throws IOException, Exception{
		Reporter.log("Validate login, logout and Invalid login in Box", true);
		replayLogs(logFile);
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(activityType, severity), data)), activityType+" Logs does not match" ); 
		
		
	}
	
	@DataProvider
	public Object[][] _1024_1025_1026() {
		return new Object[][]{
			//Activity type    Object Type    Severity           message
			{ "Login",     		"Session",  "informational",  "User logged in" 	                   ,"user,Salesforce,login.log"  },
			{ "Logout",    		"Session",  "informational",  "User logged out" ,"user,Salesforce,logout.log" },
			//{ "InvalidLogin",   "Session",  "informational",  "User Login Failed!"                 , "InvalidLogin.log"}
		};
	}
	
	
	
	
	
	//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_Posted")
	public void verify_User_PostedAComment_(String activityType, String objectType, String objectName, String severity, String logFile) throws Exception{
		Reporter.log("Validate User posted a comment", true);
		String expectedMsg="User posted a comment "+objectName;
		replayLogs(logFile);
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(activityType, severity), data)), activityType+" Logs does not match" ); 
		
	}
	
	@DataProvider
	public Object[][] _Posted() {
		return new Object[][]{
			//Activity type  Object Type  Object Name, DestinationDolder  Severity  message
			{ "Post",    "Comment", "This is test",  "informational", "Comment,Salesforce,post.log"},
			
		};
	}

	
	//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1052")
	public void verify_User_deleteComment_1052(String activityType, String objectType, String commentText, String severity, String logFile) throws Exception{
		Reporter.log("Validate User delete the comment", true);
		String expectedMsg="User deleted "+commentText;
		replayLogs(logFile);
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(activityType, severity), data)), activityType+" Logs does not match" ); 
		
	}
	
	@DataProvider
	public Object[][] _1052() {
		return new Object[][]{
			//Activity type  Object Type  Object Name, DestinationDolder  Severity  message
			{ "Social",  "Chatter", "", "informational", "chatter,Salesforce,comment_delete.log"},
			
		};
	}
	
	

     // @Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1056") //logs problem, will record again
	public void verify_User_sharedComment_1056(String activityType, String objectType, String commentText, String severity, String logFile) throws Exception{
		Reporter.log("Validate User shared a comment", true);
		String expectedMsg="User shared a comment "+commentText;
		replayLogs(logFile);
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(activityType, severity), data)), activityType+" Logs does not match" ); 
		
	}
	
	@DataProvider
	public Object[][] _1056() {
		return new Object[][]{
			//Activity type  Object Type  Object Name, DestinationDolder  Severity  message
			{ "Share",    "Comment", "", "informational", "chatter,Salesforce,comment_share.log"},
		};
	}

	@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1147")
	public void verify_User_createAccount_1147(String activityType, String objectType, String objectName, String severity, String logFile) throws Exception{
		Reporter.log("Validate User created a new account with name", true);
		String expectedMsg="User created a new account with name "+objectName;
		replayLogs(logFile);
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(activityType, severity), data)), activityType+" Logs does not match" ); 
		
	}
	
	@DataProvider
	public Object[][] _1147() {
		return new Object[][]{
			//Activity type  Object Type  Object Name, DestinationDolder  Severity  message
			{ "Create",    "Account", "Testelastica",  "informational", "Account,Salesforce,create.log"},
		};
	}

	
	@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1148")
	public void verify_User_editAccount_1148(String activityType, String objectType, String objectName, String severity, String logFile) throws Exception{
		Reporter.log("Validate User edited an account named", true);
		String expectedMsg="User edited an account named "+objectName;
		replayLogs(logFile);
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(activityType, severity), data)), activityType+" Logs does not match" ); 
		
	}
	
	@DataProvider
	public Object[][] _1148() {
		return new Object[][]{
			//Activity type  Object Type  Object Name, DestinationDolder  Severity  message
			{ "Edit",    "Account", "Testelastica",  "informational", "Account,Salesforce,edit.log"},
			
		};
	}
	
	@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1150")
	public void verify_User_viewAccount_1150(String activityType, String objectType, String objectName, String severity, String logFile) throws Exception{
		Reporter.log("Validate User viewed Accounts", true);
		String expectedMsg="User viewed Account named "+objectName;
		replayLogs(logFile);
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(activityType, severity), data)), activityType+" Logs does not match" ); 
		
	}
	
	@DataProvider
	public Object[][] _1150() {
		return new Object[][]{
			//Activity type  Object Type  Object Name, DestinationDolder  Severity  message
			{ "View",    "Account", "Testelastica", "informational", "Account,Salesforce,view.log"},
			
		};
	}
	
	@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1151")
	public void verify_User_deleteAccount_1151(String activityType, String objectType, String objectName, String severity, String logFile) throws Exception{
		Reporter.log("Validate User deleted the account", true);
		String expectedMsg="User deleted the account "+objectName;
		replayLogsWithDelay(logFile);
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(activityType, severity), data)), activityType+" Logs does not match" ); 
		
	}
	
	@DataProvider
	public Object[][] _1151() {
		return new Object[][]{
			//Activity type  Object Type  Object Name, DestinationDolder  Severity  message
			{ "Delete",    "Account", "", "informational", "Salesforce,Delete_Account.log"},
		};
	}
	
	@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1596")
	public void verify_User_createContact_1596(String activityType, String objectType, String objectName, String severity, String logFile) throws Exception{
		Reporter.log("Validate User created contact named", true);
		String expectedMsg="User created a new contact with name "+objectName;
		replayLogs(logFile);
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(activityType, severity), data)), activityType+" Logs does not match" ); 
		
	}
	
	@DataProvider
	public Object[][] _1596() {
		return new Object[][]{
			//Activity type  Object Type  Object Name, DestinationDolder  Severity  message
			{ "Create",    "Contact", "Test Testcase",  "informational", "Contact,Salesforce,create.log"},
		};
	}
	
	@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1597")
	public void verify_User_editContact_1597(String activityType, String objectType, String commentText, String severity, String logFile) throws Exception{
		Reporter.log("Validate User edited contact named", true);
		String expectedMsg="User edited  contact "+commentText;
		replayLogs(logFile);
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(activityType, severity), data)), activityType+" Logs does not match" ); 
		
	}
	
	@DataProvider
	public Object[][] _1597() {
		return new Object[][]{
			//Activity type  Object Type  Object Name, DestinationDolder  Severity  message
			{ "Edit",    "Contact", "Test Testcase",  "informational", "Contact,Salesforce,edit.log"},
		};
	}
	
	
	

	@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1110")
	public void verify_User_ViewContact_1110(String activityType, String objectType, String commentText, String severity, String logFile) throws Exception{
		Reporter.log("Validate User viewed Contacts", true);
		String expectedMsg="User viewed Contact named "+ commentText;
		replayLogs(logFile);
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(activityType, severity), data)), activityType+" Logs does not match" ); 
		
	}
	
	@DataProvider
	public Object[][] _1110() {
		return new Object[][]{
			//Activity type  Object Type  Object Name, DestinationDolder  Severity  message
			{ "View",    "Contact", "Test Testcase",  "informational", "Contact,Salesforce,view.log"},
		};
	}
	
	@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1111")
	public void verify_User_deleteContact_1111(String activityType, String objectType, String objectName, String severity, String logFile) throws Exception{
		Reporter.log("Validate User deleted the contact", true);
		String expectedMsg="User deleted the contact "+objectName;
		replayLogsEPDV3(logFile);
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(activityType, severity), data)), activityType+" Logs does not match" ); 
		
	}
	
	@DataProvider
	public Object[][] _1111() {
		return new Object[][]{
			//Activity type  Object Type  Object Name, DestinationDolder  Severity  message
			{ "Delete",    "Contact", "",  "informational", "Salesforce,Delete_Contact.log"},
		};
	}
	
	
	@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1075")
	public void verify_User_downloadFile_1075(String activityType, String objectType, String file_name, String severity, String logFile) throws Exception{
		Reporter.log("Validate User downloaded a file named", true);
		String expectedMsg="User downloaded a file named "+file_name;
		replayLogs(logFile);
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(activityType, severity), data)), activityType+" Logs does not match" ); 
		
	}
	
	@DataProvider
	public Object[][] _1075() {
		return new Object[][]{
			//Activity type  Object Type  Object Name, DestinationDolder  Severity  message
//			{ "Download",    "File", "Test.7z", "informational", "File,Salesforce_2,Download_Test_7z.log"},//file,Salesforce,download.log
//			{ "Download",    "File", "Test.avi", "informational",  "File,Salesforce_2,Download_Test_avi.log"},
			{ "Download",    "File", "Test.doc", "informational",  "File,Salesforce_2,Download_Test_doc.log"},
//			{ "Download",    "File", "Test.docx","informational",  "File,Salesforce_2,Download_Test_docx.log"},
//			{ "Download",    "File", "Test.exe", "informational",  "File,Salesforce_2,Download_Test_exe.log"},
//			{ "Download",    "File", "Test.flac", "informational",  "File,Salesforce_2,Download_Test_flac.log"},
//			{ "Download",    "File", "Test.gif", "informational",  "File,Salesforce_2,Download_Test_gif.log"},
//			{ "Download",    "File", "Test.html", "informational",  "File,Salesforce_2,Download_Test_html.log"},
//			{ "Download",    "File", "Test.java", "informational",  "File,Salesforce_2,Download_Test_java.log"},
//			{ "Download",    "File", "Test.jpeg", "informational",  "File,Salesforce_2,Download_Test_jpeg.log"},
//			{ "Download",    "File", "Test.json", "informational",  "File,Salesforce_2,Download_Test_json.log"},
//			{ "Download",    "File", "Test.key", "informational",  "File,Salesforce_2,Download_Test_key.log"},
//			{ "Download",    "File", "Test.mp3", "informational",  "File,Salesforce_2,Download_Test_mp3.log"},
//			{ "Download",    "File", "Test.mp4", "informational",  "File,Salesforce_2,Download_Test_mp4.log"},
//			{ "Download",    "File", "Test.pdf", "informational",  "File,Salesforce_2,Download_Test_pdf.log"},
//			{ "Download",    "File", "Test.pem", "informational",  "File,Salesforce_2,Download_Test_pem.log"},
//			{ "Download",    "File", "Test.png", "informational",  "File,Salesforce_2,Download_Test_png.log"},
//			{ "Download",    "File", "Test.ppt", "informational",  "File,Salesforce_2,Download_Test_ppt.log"},
//			{ "Download",    "File", "Test.txt", "informational",  "File,Salesforce_2,Download_Test_txt.log"},
//			{ "Download",    "File", "Test.xls", "informational",  "File,Salesforce_2,Download_Test_xls.log"},
//			{ "Download",    "File", "Test.zip", "informational",  "File,Salesforce_2,Download_Test_zip.log"},
		};
	}
	

	@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1093Lead")
	public void verify_User_createdLead_1093(String activityType, String objectType, String commentText, String severity, String logFile) throws Exception{
		Reporter.log("Validate User created a new lead", true);
	    String expectedMsg="User created a new lead with name "+commentText;
		replayLogs(logFile);
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
		
	}
	
	@DataProvider
	public Object[][] _1093Lead() {
		return new Object[][]{
			//Activity type  Object Type  Object Name   Severity  message
			{ "Create",    "Lead", "Test1 Testlead", "informational", "Lead,Salesforce,create.log"},
			
		};
	}
	
	
	
	
	@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1093")
	public void verify_User_editLead_1093(String activityType, String objectType, String commentText, String severity, String logFile) throws Exception{
		Reporter.log("Validate User edited a lead", true);
		String expectedMsg="User edited a lead "+commentText;
		replayLogs(logFile);
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
		
	}
	
	@DataProvider
	public Object[][] _1093() {
		return new Object[][]{
			//Activity type  Object Type  Object Name,  Severity  message
			{ "Edit",    "Lead", "Test1 Testlead", "informational", "Lead,Salesforce,edit.log"},
			
		};
	}
	
	

	@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1094")
	public void verify_User_viewLead_1094(String activityType, String objectType, String commentText, String severity, String logFile) throws Exception{
		Reporter.log("Validate User viewed leads", true);
		String expectedMsg="User viewed "+objectType+" named "+commentText;
		replayLogs(logFile);
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
		
	}
	
	@DataProvider
	public Object[][] _1094() {
		return new Object[][]{
			//Activity type  Object Type  Object Name, Severity  message
			{ "View",    "Lead", "Test1 Testlead", "informational", "Lead,Salesforce,view.log"},
			
		};
	}
	
	@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1095")
	public void verify_User_deleteLead_1095(String activityType, String objectType, String commentText, String severity, String logFile) throws Exception{
		Reporter.log("Validate User deleted the lead", true);
		String expectedMsg="User deleted the lead "+commentText;
		replayLogsWithDelay(logFile);
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
		
	}
	
	@DataProvider
	public Object[][] _1095() {
		return new Object[][]{
			//Activity type  Object Type  Object Name,  Severity  message
			{ "Delete",    "Lead", "", "informational", "Lead,Salesforce,delete.log"},
			
		};
	}
	
	
	@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1120Create")
	public void verify_User_createOpportunity_1120(String activityType, String objectType, String commentText, String severity, String logFile) throws Exception{
		Reporter.log("Validate User created a new opportunity with name", true);
		String expectedMsg="User created a new opportunity with name "+commentText;
		replayLogs(logFile);
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
		
	}
	
	@DataProvider
	public Object[][] _1120Create() {
		return new Object[][]{
			//Activity type  Object Type  Object Name,  Severity  message
			{ "Create",    "Opportunity", "Test1", "informational", "Opportunity,Salesforce,create.log"},
			
		};
	}
	
	
	@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1120Edited")
	public void verify_User_editedOpportunity_1120(String activityType, String objectType, String commentText, String closeDate, String severity, String logFile) throws Exception{
		Reporter.log("Validate User edited an Opportunities", true);
		String expectedMsg="User edited an opportunity "+commentText+" close date "+closeDate;
		replayLogs(logFile);
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
		
	}
	
	@DataProvider
	public Object[][] _1120Edited() {
		return new Object[][]{
			//Activity type  Object Type  Object Name,  Severity  message
			{ "Edit",    "Opportunity", "Test1,","10/7/2015", "informational", "Opportunity,Salesforce,edit.log"},
			
		};
	}
	
	
	@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1120")
	public void verify_User_viewOpportunity_1120(String activityType, String objectType, String opportunity, String severity, String logFile) throws Exception{
		Reporter.log("Validate User viewed Opportunity", true);
		String expectedMsg="User viewed Opportunity named "+opportunity;
		replayLogs(logFile);
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
		
	}
	
	@DataProvider
	public Object[][] _1120() {
		return new Object[][]{
			//Activity type  Object Type  Object Name, DestinationDolder  Severity  message
			{ "View",    "Opportunity", "Test1", "informational", "Opportunity,Salesforce,view.log"},
			
		};
	}
	
	
	
	@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1121")
	public void verify_User_deleteOpportunity_1121(String activityType, String objectType, String commentText, String severity, String logFile) throws Exception{
		Reporter.log("Validate User deleted the opportunity", true);
		String expectedMsg="User deleted the opportunity ";
		replayLogs(logFile);
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
		
	}
	
	@DataProvider
	public Object[][] _1121() {
		return new Object[][]{
			//Activity type  Object Type  Object Name, DestinationDolder  Severity  message
			{ "Delete",    "Opportunity", "Test1", "informational", "Opportunity,Salesforce,delete.log"},
			
		};
	}
	
	
	@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_post")
	public void verify_User_postComment_post(String activityType, String objectType, String commentText, String severity, String logFile) throws Exception{
		Reporter.log("Validate User posted the Comment", true);
		String expectedMsg="User posted a comment ";
		replayLogs(logFile);
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
		
	}
	
	@DataProvider
	public Object[][] _post() {
		return new Object[][]{
			//Activity type  Object Type  Object Name, DestinationDolder  Severity  message
			{ "Post",    "Comment", "", "informational", "Comment,Salesforce,post.log"},
			
		};
	}
	
	
	@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_upload")
	public void verify_User_uploadFile_upload(String activityType, String objectType, String objectName, String severity, String logFile) throws Exception{
		Reporter.log("Validate User uploaded the file", true);
		String expectedMsg="User uploaded file named "+objectName;
		replayLogs(logFile);
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
		
	}
	
	@DataProvider
	public Object[][] _upload() {
		return new Object[][]{
			//Activity type  Object Type  Object Name, DestinationDolder  Severity  message
			{ "Upload",    "File", "Test.pdf", "informational", "File,Salesforce,upload.log"},
			{ "Upload",    "File", "FoothillSchedule.rtf", "informational", "Salesforce,Files,upload_FoothillSchedule.rtf.log"},
			{ "Upload",    "File", "glba.txt", "informational", "Salesforce,Files,upload_glba_txt.log"},
			{ "Upload",    "File", "hipaa.txt", "informational", "Salesforce,Files,upload_hipaa_txt.log"},
			{ "Upload",    "File", "pci.txt", "informational", "Salesforce,Files,upload_pci_txt.log"},
			{ "Upload",    "File", "pii.rtf", "informational", "Salesforce,Files,upload_pii.rtf.log"},
			{ "Upload",    "File", "Sample.pdf", "informational", "Salesforce,Files,upload_Sample_pdf.log"},
			{ "Upload",    "File", "Sample.py", "informational", "Salesforce,Files,upload_Sample_py.log"},
			{ "Upload",    "File", "Test.7z", "informational", "Salesforce,Files,upload_Test_7z.log"},
			{ "Upload",    "File", "Test.base64", "informational", "Salesforce,Files,upload_Test_base64.log"},
			{ "Upload",    "File", "Test.boxnote", "informational", "Salesforce,Files,upload_Test_boxnote.log"},
			{ "Upload",    "File", "Test.c", "informational", "Salesforce,Files,upload_Test_c.log"},
			{ "Upload",    "File", "Test.datatable", "informational", "Salesforce,Files,upload_Test_datatable.log"},
			{ "Upload",    "File", "Test.doc", "informational", "Salesforce,Files,upload_Test_doc.log"},
			{ "Upload",    "File", "Test.docx", "informational", "Salesforce,Files,upload_Test_docx.log"},
			{ "Upload",    "File", "Test.exe", "informational", "Salesforce,Files,upload_Test_exe.log"},
			{ "Upload",    "File", "Test.html", "informational", "Salesforce,Files,upload_Test_html.log"},
			{ "Upload",    "File", "Test.java", "informational", "Salesforce,Files,upload_Test_java.log"},
			{ "Upload",    "File", "Test.jpg", "informational", "Salesforce,Files,upload_Test_jpg.log"},
			{ "Upload",    "File", "Test.js", "informational", "Salesforce,Files,upload_Test_js.log"},
			{ "Upload",    "File", "Test.json", "informational", "Salesforce,Files,upload_Test_json.log"},
			{ "Upload",    "File", "Test.mp3", "informational", "Salesforce,Files,upload_Test_mp3.log"},
			{ "Upload",    "File", "Test.mp4", "informational", "Salesforce,Files,upload_Test_mp4.log"},
			{ "Upload",    "File", "Test.ods", "informational", "Salesforce,Files,upload_Test_ods.log"},
			{ "Upload",    "File", "Test.odt", "informational", "Salesforce,Files,upload_Test_odt.log"},
			{ "Upload",    "File", "Test.pdf", "informational", "Salesforce,Files,upload_Test_pdf.log"},
			{ "Upload",    "File", "Test.pem", "informational", "Salesforce,Files,upload_Test_pem.log"},
			{ "Upload",    "File", "Test.png", "informational", "Salesforce,Files,upload_Test_png.log"},
			{ "Upload",    "File", "Test.properties", "informational", "Salesforce,Files,upload_Test_properties.log"},
			{ "Upload",    "File", "Test.py", "informational", "Salesforce,Files,upload_Test_py.log"},
			{ "Upload",    "File", "Test.rar", "informational", "Salesforce,Files,upload_Test_rar.log"},
			{ "Upload",    "File", "Test.rtf", "informational", "Salesforce,Files,upload_Test_rtf.log"},
			{ "Upload",    "File", "Test.tbz2", "informational", "Salesforce,Files,upload_Test_tbz2.log"},
			{ "Upload",    "File", "Test.tgz", "informational", "Salesforce,Files,upload_Test_tgz.log"},
			{ "Upload",    "File", "Test.txt", "informational", "Salesforce,Files,upload_Test_txt.log"},
			{ "Upload",    "File", "Test.vb", "informational", "Salesforce,Files,upload_Test_vb.log"},
			{ "Upload",    "File", "Test.xls", "informational", "Salesforce,Files,upload_Test_xls.log"},
			{ "Upload",    "File", "Test.xlsm", "informational", "Salesforce,Files,upload_Test_xlsm.log"},
			{ "Upload",    "File", "Test.xlsx", "informational", "Salesforce,Files,upload_Test_xlsx.log"},
			{ "Upload",    "File", "Test.xml", "informational", "Salesforce,Files,upload_Test_xml.log"},
		    { "Upload",    "File", "Test.zip", "informational", "Salesforce,Files,upload_Test_zip.log"},
			
			
		};
	}
	

	@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_share")
	public void verify_User_uploadFile_share(String activityType, String objectType, String objectName, String severity, String logFile) throws Exception{
		Reporter.log("Validate User shared the file", true);
		String expectedMsg="User shared a file named "+objectName+" via public link";
		replayLogs(logFile);
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
		
	}
	
	@DataProvider
	public Object[][] _share() {
		return new Object[][]{
			//Activity type  Object Type  Object Name, DestinationDolder  Severity  message
			{ "Share",    "File", "Test", "informational", "File,Salesforce,share_wth_link.log"},
		};
	}
	
	@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_shareFile")
	public void verify_User_uploadFile_shareFile(String activityType, String objectType, String objectName, String recp_name, String expectedMsg, String severity, String logFile) throws Exception{
		Reporter.log("Validate User shared the file", true);
		//String expectedMsg="User shared a file named "+objectName+" via public link";
		replayLogsEPDV3(logFile);
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
		
	}
	
	@DataProvider
	public Object[][] _shareFile() {
		return new Object[][]{
			//Activity type  Object Type  Object Name, recp_name message  Severity  Logfile
			{ "Share",    "File", "Test","","[ERROR] The following activity failed:  User shared a file named all4 via public link", "informational", "file,share_via_link.log"},
			{ "Share",    "File", "", "Test group","User shared file  with group named Test group" , "informational", "file,share_with_group.log"},
			{ "Share",    "File", "", "Chatter Expert","User shared file  with recipient named Chatter Expert", "informational", "file,share_with_people.log"},
			
			
		};
	}
	
	
	@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_homeAccount")
	public void verify_User_createHomeAccount_homeAccount(String activityType, String objectType, String objectName, String severity, String logFile) throws Exception{
		Reporter.log("Validate User created the new home account", true);
		String expectedMsg="User created a new account with name "+objectName;
		replayLogs(logFile);
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
		
	}
	
	@DataProvider
	public Object[][] _homeAccount() {
		return new Object[][]{
			//Activity type  Object Type  Object Name, DestinationDolder  Severity  message
			{ "Create",    "Account", "Testautomation", "informational", "Home,create_new_account.log"},
			
		};
	}

	
	@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_homeContact")
	public void verify_User_createHomeContact_homeContact(String activityType, String objectType, String objectName, String severity, String logFile) throws Exception{
		Reporter.log("Validate User created the new home contact", true);
		String expectedMsg="User created a new contact with name "+objectName;
		replayLogs(logFile);
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
		
	}
	
	@DataProvider
	public Object[][] _homeContact() {
		return new Object[][]{
			//Activity type  Object Type  Object Name, DestinationDolder  Severity  message
			{ "Create",    "Contact", "Test Test", "informational", "Home,create_new_contact.log"},
			
		};
	}

	
	
	@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_homeDocument")///////
	public void verify_User_createHomeDocument_homeDocument(String activityType, String objectType, String objectName, String severity, String logFile) throws Exception{
		Reporter.log("Validate User created the new home document", true);
		String expectedMsg="User created a new document named  and file name "+objectName;
		replayLogs(logFile);
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
		
	}
	
	@DataProvider
	public Object[][] _homeDocument() {
		return new Object[][]{
			//Activity type  Object Type  Object Name, DestinationDolder  Severity  message
			{ "Create",    "Document", "Test.pdf", "informational", "Home,create_new_document.log"},
			
		};
	}

	
	
	@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_homeEvent")//////
	public void verify_User_createHomeEvent_homeEvent(String activityType, String objectType, String objectName, String severity, String logFile) throws Exception{
		Reporter.log("Validate User created the new home event", true);
		String expectedMsg="User created a new event Test and assigned it to "+objectName;
		replayLogs(logFile);
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
		
	}
	
	@DataProvider
	public Object[][] _homeEvent() {
		return new Object[][]{
			//Activity type  Object Type  Object Name, DestinationDolder  Severity  message
			{ "Create",    "Event", "QA Admin", "informational", "Home,create_new_event.log"},
			
		};
	}

	
	
	@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_homeFile")///////
	public void verify_User_createHomeFile_homeFile(String activityType, String objectType, String objectName, String severity, String logFile) throws Exception{
		Reporter.log("Validate User created the new home file", true);
		String expectedMsg="User uploaded file named "+objectName;
		replayLogs(logFile);
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
		
	}
	
	@DataProvider
	public Object[][] _homeFile() {
		return new Object[][]{
			//Activity type  Object Type  Object Name, DestinationDolder  Severity  message
			{ "Upload",    "File", "Test.pdf", "informational", "Home,create_new_file.log"},
			
		};
	}
	
	
	@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_homeLink")/////
	public void verify_User_createHomeLink_homeLink(String activityType, String objectType, String link, String name, String severity, String logFile) throws Exception{
		Reporter.log("Validate User created the new home link", true);
		String expectedMsg="User shared a url "+link+" named "+name;
		replayLogs(logFile);
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
		
	}
	
	@DataProvider
	public Object[][] _homeLink() {
		return new Object[][]{
			//Activity type  Object Type  Object Name, DestinationDolder  Severity  message
			{ "Share",    "Chatter", "http://google.com", "google", "informational", "Home,create_new_link.log"},
			
		};
	}
	

 

	@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_homeCall")////////
	public void verify_User_createHomeCall_homeCall(String activityType, String objectType, String objectName, String RecieverName, String severity, String logFile) throws Exception{
		Reporter.log("Validate User created the new home log a call", true);
		String expectedMsg="User created task with name "+objectName+", assigned to "+RecieverName;
		replayLogs(logFile);
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
		
	}
	
	@DataProvider
	public Object[][] _homeCall() {
		return new Object[][]{
			//Activity type  Object Type  Object Name, DestinationDolder  Severity  message
			{ "Create",    "Task", "Call", "", "informational", "Home,create_new_log_a_call.log"},
			
		};
	}
	
	
	
	@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_homePoll")
	public void verify_User_createHomePoll_homePoll(String activityType, String objectType, String objectName, String objectName2, String severity, String logFile) throws Exception{
		Reporter.log("Validate User created the new home poll", true);
		String expectedMsg="User created a poll with question "+objectName+" and choices "+objectName2;
		replayLogs(logFile);
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
		
	}
	
	@DataProvider
	public Object[][] _homePoll() {
		return new Object[][]{
			//Activity type  Object Type  Object Name, DestinationDolder  Severity  message
			{ "Create",    "Poll", "how to test", "Test1, Test2", "informational", "Home,create_new_poll.log"},
			
		};
	}
	
	
	//@Test(groups ={"TEST", "P1"},dataProvider = "_homePost")///////// need to record log
	public void verify_User_createHomePost_homePost(String activityType, String objectType, String commentText, String severity, String logFile) throws Exception{
		Reporter.log("Validate User created the new home post", true);
		String expectedMsg="User posted a comment "+commentText;
		replayLogs(logFile);
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
		
	}
	
	@DataProvider
	public Object[][] _homePost() {
		return new Object[][]{
			//Activity type  Object Type  Object Name, DestinationDolder  Severity  message
			{ "Post",    "Comment", "This is my test", "informational", "Home,create_new_post.log"},
			
		};
	}
	
	
	@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_homeReport")
	public void verify_User_createHomeReport_homeReport(String activityType, String objectType, String objectName, String severity, String logFile) throws Exception{
		Reporter.log("Validate User created the new report", true);
		String expectedMsg="User created a new report "+objectName; 
		replayLogs(logFile);
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
		
	}
	
	@DataProvider
	public Object[][] _homeReport() {
		return new Object[][]{
			//Activity type  Object Type  Object Name, DestinationDolder  Severity  message
			{ "Create",    "Report", "Unsaved Report", "informational", "Home,create_new_report.log"},
			
		};
	}
	
	
	
	
	
	//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_homeTask")//////
	public void verify_User_createHomeReport_homeTask(String activityType, String objectType, String commentText, String severity, String logFile) throws Exception{
		Reporter.log("Validate User created the new home task", true);
		String expectedMsg="User created a new task ";
		replayLogs(logFile);
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
		
	}
	
	@DataProvider
	public Object[][] _homeTask() {
		return new Object[][]{
			//Activity type  Object Type  Object Name, DestinationDolder  Severity  message
			{ "Create",    "Task", "", "informational", "Home,create_new_task.log"},
			
		};
	}
	
	
	@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_reportCreate")
	public void verify_User_createReport_report(String activityType, String objectType, String reportName, String severity, String logFile) throws Exception{
		Reporter.log("Validate User created the report", true);
		String expectedMsg="User created a new report "+reportName;
		replayLogs(logFile);
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
		
	}
	
	@DataProvider
	public Object[][] _reportCreate() {
		return new Object[][]{
			//Activity type  Object Type  Object Name, DestinationDolder  Severity  message
			{ "Create",    "Report", "Unsaved Report",  "informational", "Report,Salesforce,create.log"}
			
		};
	}
	
	
	
	@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_reportView")
	public void verify_User_ViewReport_report(String activityType, String objectType, String reportName, String severity, String logFile) throws Exception{
		Reporter.log("Validate User created the report", true);
		String expectedMsg="User viewed "+objectType.toLowerCase()+" named "+reportName;
		replayLogs(logFile);
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
		
	}
	
	@DataProvider
	public Object[][] _reportView() {
		return new Object[][]{
			//Activity type  Object Type  Object Name, DestinationDolder  Severity  message
			{ "View",    "Report", "GWAutomation", "informational", "Report,Salesforce,view.log"},
			
		};
	}
	
	

	@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_reportEdit")
	public void verify_User_EditReport_report(String activityType, String objectType, String reportName, String severity, String logFile) throws Exception{
		Reporter.log("Validate User created the report", true);
		String expectedMsg="User edited report "+reportName;
		replayLogs(logFile);
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
		
	}
	
	@DataProvider
	public Object[][] _reportEdit() {
		return new Object[][]{
			//Activity type  Object Type  Object Name, DestinationDolder  Severity  message
			{ "Edit",    "Report", "GWAutomationTest", "informational", "Report,Salesforce,edit.log"},
			
		};
	}
	
	@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_reportExport")
	public void verify_User_ExportReport_report(String activityType, String objectType, String reportName, String severity, String logFile) throws Exception{
		Reporter.log("Validate User created the report", true);
		String expectedMsg="User downloaded a report "+reportName+" as xls";
		replayLogs(logFile);
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
		
	}
	
	@DataProvider
	public Object[][] _reportExport() {
		return new Object[][]{
			//Activity type  Object Type  Object Name, DestinationDolder  Severity  message
			{ "Export",    "Report", "GWAutomationTest", "informational", "Report,Salesforce,export.log"},
			
		};
	}
	
	//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1183")
	public void verify_User_DeleteReport_1183(String activityType, String objectType, String report_name, String severity, String logFile) throws Exception{
		Reporter.log("Validate User deleted report named", true);
		String expectedMsg="User deleted report named "+report_name;
		replayLogs(logFile);
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
		
	}
	
	@DataProvider
	public Object[][] _1183() {
		return new Object[][]{
			//Activity type  Object Type  Object Name, DestinationDolder  Severity  message
			{ "Delete",    "Report", "GWAutomation", "informational", "Report,Salesforce,delete.log"},
			
		};
	}
	
	@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_reportRun")
	public void verify_User_RunReport_report(String activityType, String objectType, String reportName, String severity, String logFile) throws Exception{
		Reporter.log("Validate User created the report", true);
		String expectedMsg="User ran a report named "+reportName;
		replayLogs(logFile);
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
		
	}
	
	@DataProvider
	public Object[][] _reportRun() {
		return new Object[][]{
			//Activity type  Object Type  Object Name, DestinationDolder  Severity  message
			{ "Run",    "Report", "GWAutomationTest", "informational", "Report,Salesforce,run.log"},
			
		};
	}
	
	
	@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1028")
	public void verify_User_ViewChatter_1028(String activityType, String objectType, String severity, String logFile) throws Exception{
		Reporter.log("Validate User viewed the chatter page", true);
		String expectedMsg="User viewed the chatter page";
		replayLogsEPDV3(logFile);
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
		
	}
	
	@DataProvider
	public Object[][] _1028() {
		return new Object[][]{
			//Activity type  Object Type  Object Name, DestinationDolder  Severity  message
			{ "View",    "Chatter", "informational", "Chatter,Salesforce,GviewPage,viewPage.log"},
			
		};
	}
	
	@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1027")
	public void verify_User_SearchTopLevel_1027(String activityType, String objectType, String search_text, String severity, String logFile) throws Exception{
		Reporter.log("Validate User searched for search_text on the homepage", true);
		String expectedMsg="User searched for "+search_text+" on the homepage";
		replayLogs(logFile);
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
		
	}
	
	@DataProvider
	public Object[][] _1027() {
		return new Object[][]{
			//Activity type  Object Type  Object Name, DestinationDolder  Severity  message
			{ "Search",    "TopLevelSearch", "Hello", "informational", "Homepage,TopLevelSearch.log"},
			
		};
	}
	
	//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1029")
	public void verify_User_ChatterFilePreview_1029(String activityType, String objectType, String file_name, String severity, String logFile) throws Exception{
		Reporter.log("Validate User previewed a file named file_name", true);
		String expectedMsg="User previewed a file named "+file_name;
		replayLogs(logFile);
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
		
	}
	
	@DataProvider
	public Object[][] _1029() {
		return new Object[][]{
			//Activity type  Object Type  Object Name,   Severity  message
			{ "View",    "File", "", "informational", "file,Salesforce,chatter_preview.log"},
			
		};
	}
	
	@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1030")
	public void verify_User_FileShareLink_1030(String activityType, String objectType, String object_name, String severity, String logFile) throws Exception{
		Reporter.log("Validate User shared  a file named file_name via public link", true);
		String expectedMsg="User shared a file named "+object_name+" via public link";
		replayLogs(logFile);
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
		
	}
	
	@DataProvider
	public Object[][] _1030() {
		return new Object[][]{
			//Activity type  Object Type  Object Name, DestinationDolder  Severity  message
			{ "Share",    "File", "Hipa", "informational", "file,Salesforce,share_link.log"},
			
		};
	}
	
	//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1031")
	public void verify_User_UniversalComment_1031(String activityType, String objectType, String comment_text, String severity, String logFile) throws Exception{
		Reporter.log("Validate User posted a comment comment_text", true);
		String expectedMsg="User posted a comment "+comment_text;
		replayLogs(logFile);
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
		
	}
	
	@DataProvider
	public Object[][] _1031() {
		return new Object[][]{
			//Activity type  Object Type  Object Name, DestinationDolder  Severity  message
			{ "Post",    "Comment", "comment_text", "informational", ""},
			
		};
	}
	
	@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1032")
	public void verify_User_ChatterFileUploadAndCommentSF_1032(String activityType, String objectType, String file_name, String severity, String logFile) throws Exception{
		Reporter.log("Validate User shared file named "+file_name+" ", true);
		String expectedMsg="User shared file named "+file_name;
		replayLogsWithDelay(logFile);
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
		
	}
	
	@DataProvider
	public Object[][] _1032() {
		return new Object[][]{
			//Activity type  Object Type  Object Name, DestinationDolder  Severity  message
			{ "Share",    "Chatter", "Screen Shot 2015-09-22 at 10.09.52 pm.png", "informational", "chatter,Salesforce,file_upload_and_commentSF.log"},
			
		};
	}
	
	@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1033")
	public void verify_User_ChatterFileUploadSF_1033(String activityType, String objectType, String file_name, String severity, String logFile) throws Exception{
		Reporter.log("Validate User shared file named "+file_name+" ", true);
		String expectedMsg="User shared file named "+file_name;
		replayLogs(logFile);
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
		
	}
	
	@DataProvider
	public Object[][] _1033() {
		return new Object[][]{
			//Activity type  Object Type  Object Name, DestinationDolder  Severity  message
			{ "Share",    "Chatter", "Screen Shot 2015-10-19 at 7.11.00 pm.png", "informational", "chatter,Salesforce,file_uploadSF.log"},
			
		};
	}

	//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1034")
	public void verify_User_UniversalURLWithText_1034(String activityType, String objectType, String url_link, String url_name,String comment, String severity, String logFile) throws Exception{
		Reporter.log("Validate User shared a url named with comment", true);
		String expectedMsg="User shared a url "+url_link+" named "+url_name+" with comment"+comment;
		replayLogs(logFile);
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
		
	}
	
	@DataProvider
	public Object[][] _1034() {
		return new Object[][]{
			//Activity type  Object Type  Object Name, DestinationDolder  Severity  message
			{ "Share",    "Chatter", "url_link", "url_name", "comment", "informational", ""},
			
		};
	}
	
	//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1035")
	public void verify_User_UniversalURLWithText_1035(String activityType, String objectType, String url_link, String comment, String severity, String logFile) throws Exception{
		Reporter.log("Validate User shared a url with comment", true);
		String expectedMsg="User shared a url "+url_link+" with comment"+comment;
		replayLogs(logFile);
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
		
	}
	
	@DataProvider
	public Object[][] _1035() {
		return new Object[][]{
			//Activity type  Object Type  Object Name, DestinationDolder  Severity  message
			{ "Share",    "Chatter", "url_link", "comment", "informational", ""},
			
		};
	}
	
	//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1036")
	public void verify_User_UniversalURLWithText_1036(String activityType, String objectType, String url_link, String url_name, String severity, String logFile) throws Exception{
		Reporter.log("Validate User shared a url named", true);
		String expectedMsg="User shared a url "+url_link+" named "+url_name;
		replayLogs(logFile);
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
		
	}
	
	@DataProvider
	public Object[][] _1036() {
		return new Object[][]{
			//Activity type  Object Type  Object Name, DestinationDolder  Severity  message
			{ "Share",    "Chatter", "url_link", "url_name", "informational", ""},
			
		};
	}
	
	//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1037")
	public void verify_User_UniversalURL_1037(String activityType, String objectType, String url_link, String severity, String logFile) throws Exception{
		Reporter.log("Validate User shared a url ", true);
		String expectedMsg="User shared a url "+url_link;
		replayLogs(logFile);
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
		
	}
	
	@DataProvider
	public Object[][] _1037() {
		return new Object[][]{
			//Activity type  Object Type  Object Name, DestinationDolder  Severity  message
			{ "Share",    "Chatter", "url_link", "informational", ""},
			
		};
	}
	
	@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1038")
	public void verify_User_ChatterInvitation_1038(String activityType, String objectType, String recp, String severity, String logFile) throws Exception{
		Reporter.log("Validate User sent an invitation to ", true);
		String expectedMsg="User sent an invitation to "+recp;
		replayLogs(logFile);
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
		
	}
	
	@DataProvider
	public Object[][] _1038() {
		return new Object[][]{
			//Activity type  Object Type  Object Name, DestinationDolder  Severity  message
			{ "Social",    "Chatter", "mohammad.usman@elastica.co", "informational", "email,Salesforce,chatter_invite.log"},
			
		};
	}
	
	//@Test(groups ={"TEST1","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1039")
	public void verify_User_ChatterMessageSearch_1039(String activityType, String objectType, String search_string, String severity, String logFile) throws Exception{
		Reporter.log("Validate User searched a message in chatter ", true);
		String expectedMsg="User searched a message "+search_string+" in chatter";
		replayLogsEPDV3(logFile);
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
		
	}
	
	@DataProvider
	public Object[][] _1039() {
		return new Object[][]{
			//Activity type  Object Type  Object Name, DestinationDolder  Severity  message
			{ "Search",    "Chatter", "", "informational", "chatter,message_search.log"},
			
		};
	}
	
	//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1040")
		public void verify_User_ChatterSingleMessageSearch_1040(String activityType, String objectType, String search_string,String message, String severity, String logFile) throws Exception{
			Reporter.log("Validate User searched a string in message ", true);
			String expectedMsg="User searched a string "+search_string+" in message"+message;
			replayLogs(logFile);
			data.put("message", expectedMsg);
			data.put("account_type", "Internal");
			assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
			
		}
		
		@DataProvider
		public Object[][] _1040() {
			return new Object[][]{
				//Activity type  Object Type  Object Name, DestinationDolder  Severity  message
				{ "Search",    "Chatter", "search_string","message", "informational", ""},
				
			};
		}
	
		//@Test(groups ={"TEST1","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1041")
				public void verify_User_ChatterViewSingleMessage_1041(String activityType, String objectType, String message, String severity, String logFile) throws Exception{
					Reporter.log("Validate User viewed message ", true);
					String expectedMsg="User viewed message "+message;
					replayLogsDebug(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1041() {
					return new Object[][]{
						//Activity type  Object Type  Object Name, DestinationDolder  Severity  message
						{ "View",    "Chatter", "", "informational", "chatter,single_message_view.log"},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1042")
				public void verify_User_ChatterMessages_1042(String activityType, String objectType, String message, String severity, String logFile) throws Exception{
					Reporter.log("Validate User viewed messages in chatter ", true);
					String expectedMsg="User viewed messages in chatter";
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1042() {
					return new Object[][]{
						//Activity type  Object Type  Object Name, DestinationDolder  Severity  message
						{ "View",    "Chatter", "", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1109")
				public void verify_User_ContactsEdit_1109(String activityType, String objectType, String user_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User edited contact ", true);
					String expectedMsg="User edited contact "+user_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1109() {
					return new Object[][]{
						//Activity type  Object Type  Object Name, DestinationDolder  Severity  message
						{ "Edit",    "Contact", "user_name", "informational", ""},
						
					};
				}
				
				@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1079")
				public void verify_User_FileShare_1079(String activityType, String objectType, String object_name, String recp_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User shared file with group named ", true);
					String expectedMsg="User shared file "+object_name+" with group named "+recp_name;
					replayLogsEPDV3(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1079() {
					return new Object[][]{
						//Activity type  Object Type  Object Name, DestinationDolder  Severity  message
						{ "Share", "File", "","Test group", "informational", "file,share_with_group.log"},
						
					};
				}
				
				//@Test(groups ={"TEST2","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1078")
				public void verify_User_FileShare_1078(String activityType, String objectType, String object_name, String recp_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User shared file with recipient named ", true);
					String expectedMsg="User shared file "+object_name+" with recipient named "+recp_name;
					replayLogsEPDV3(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1078() {
					return new Object[][]{
						//Activity type  Object Type  Object Name, DestinationDolder  Severity  message
						{ "Share", "File", "Test.doc","recp_name", "informational", "file,share_with_recipient.log"},
						
					};
				}
				
				@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1045")
				public void verify_User_ChatterCommentOnComment_1045(String activityType, String objectType, String comment_text, String parent_comment_text, String severity, String logFile) throws Exception{
					Reporter.log("Validate User posted comment text on original parent comment text ", true);
					String expectedMsg="User posted "+comment_text+" on original "+parent_comment_text;
					replayLogsEPDV3(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1045() {
					return new Object[][]{
						//Activity type  Object Type  Object Name, DestinationDolder  Severity  message
						{ "Post", "Comment", "hello dear","allinone", "informational", "chatter,comment_on_comment.log"},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1046")
				public void verify_User_ChatterCommentOnComment_1046(String activityType, String objectType, String uploaded_file, String parent_comment_text, String severity, String logFile) throws Exception{
					Reporter.log("Validate User posted comment text on original parent comment text ", true);
					String expectedMsg="User posted attachment "+uploaded_file+" on original "+parent_comment_text;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1046() {
					return new Object[][]{
						//Activity type  Object Type  Object Name, DestinationDolder  Severity  message
						{ "Post", "Comment", "","", "informational", ""},
						
					};
				}
				
				@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1043")
				public void verify_User_ChatterMessageSent_1043(String activityType, String objectType, String recp_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User sent a message ", true);
					String expectedMsg="User sent an invitation to "+recp_name;
					replayLogsEPDV3(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1043() {
					return new Object[][]{
						//Activity type  Object Type  Object Name, DestinationDolder  Severity  message
						{ "Social", "Chatter","mohd.afjal@elastica.co", "informational", "chatter,sent_message.log"},
						
					};
				}
				
				@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1044")
				public void verify_User_ChatterMessageReply_1044(String activityType, String objectType, String message, String orig_msg, String severity, String logFile) throws Exception{
					Reporter.log("Validate User replied with message to original message ", true);
					String expectedMsg="User posted "+message+ "\n"+" on original "+orig_msg;
					replayLogsEPDV3(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1044() {
					return new Object[][]{
						//Activity type  Object Type  Object Name, DestinationDolder  Severity  message
						{ "Post", "Comment", "Hi","pci", "informational", "chatter,reply_message.log"},
						
					};
				}
				
				@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1047")
				public void verify_User_CommentLike_1047(String activityType, String objectType, String comment_text, String severity, String logFile) throws Exception{
					Reporter.log("Validate User liked comment text ", true);
					String expectedMsg="User liked "+comment_text;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1047() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Social", "Chatter", "Screen Shot 2015-10-19 at 7.11.00 pm.png", "informational", "chatter,Salesforce,comment_like.log"},
						
					};
				}
				
				@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1048")
				public void verify_User_CommentUnLike_1048(String activityType, String objectType, String comment_text, String severity, String logFile) throws Exception{
					Reporter.log("Validate User unliked comment text ", true);
					String expectedMsg="User unliked "+comment_text;
					replayLogsWithDelay(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1048() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Social", "Chatter", "Screen Shot 2015-10-19 at 7.11.00 pm.png", "informational", "chatter,Salesforce,comment_unlike.log"},
						
					};
				}
				
			//	@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1049")// logs problem we wll rerecorded again
				public void verify_User_CommentBookmark_1049(String activityType, String objectType, String comment_text, String severity, String logFile) throws Exception{
					Reporter.log("Validate User bookmarked comment text ", true);
					String expectedMsg="User bookmarked "+comment_text;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1049() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Social", "Chatter", "comment_text", "informational", "chatter,Salesforce,comment_bookmark.log"},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1050")
				public void verify_User_CommentUnBookmark_1050(String activityType, String objectType, String comment_text, String severity, String logFile) throws Exception{
					Reporter.log("Validate User deleted the bookmark on comment text ", true);
					String expectedMsg="User deleted the bookmark on "+comment_text;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1050() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Social", "Chatter", "", "informational", "chatter,Salesforce,comment_unBookmark.log"},
						
					};
				}
				
				@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1051")
				public void verify_User_CommentOnCommentDel_1051(String activityType, String objectType, String comment_text, String severity, String logFile) throws Exception{
					Reporter.log("Validate User deleted comment text ", true);
					String expectedMsg="User deleted "+comment_text;
					replayLogsEPDV3(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1051() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Social", "Chatter", "", "informational", "chatter,comment_on_comment_del.log"},
						
					};
				}
				
				//@Test(groups ={"TEST1","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1053")
				public void verify_User_SubCommentUnLike_1053(String activityType, String objectType, String comment_text,String parent_comment_text, String severity, String logFile) throws Exception{
					Reporter.log("Validate User unliked comment text on original parent comment text ", true);
					String expectedMsg="User unliked "+comment_text+" on original "+parent_comment_text;
					replayLogsEPDV3(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1053() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Social", "Chatter", "","", "informational", "chatter,sub_comment_unlike.log"},
						
					};
				}
				
				@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1054")
				public void verify_User_SubCommentLike_1054(String activityType, String objectType, String comment_text,String parent_comment_text, String severity, String logFile) throws Exception{
					Reporter.log("Validate User liked comment text on original parent comment text ", true);
					String expectedMsg="User liked "+comment_text+" on original "+parent_comment_text;
					replayLogsEPDV3(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1054() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Social", "Chatter", "","", "informational", "chatter,sub_comment_like.log"},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1055")
				public void verify_User_SubCommentDelete_1055(String activityType, String objectType, String comment_text,String parent_comment_text, String severity, String logFile) throws Exception{
					Reporter.log("Validate User deleted comment text on original parent comment text ", true);
					String expectedMsg="User deleted "+comment_text+" on original "+parent_comment_text;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1055() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Social", "Chatter", "comment_text","parent_comment_text", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1057")
				public void verify_User_HomePage_1057(String activityType, String objectType, String severity, String logFile) throws Exception{
					Reporter.log("Validate User viewed homepage ", true);
					String expectedMsg="User viewed contents of All Files and Folder";
					replayLogsWithDelay(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1057() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "View", "Homepage", "informational", "homePage,Salesforce,view.log"},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1058")
				public void verify_User_EventCreate_1058(String activityType, String objectType, String subject,String assigned_to, String severity, String logFile) throws Exception{
					Reporter.log("Validate User created a new event and assigned it ", true);
					String expectedMsg="User created a new event "+subject+" and assigned it to "+assigned_to;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1058() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Create", "Event", "subject","assigned_to", "informational", ""},
						
					};
				}
				

				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1059")
				public void verify_User_EventEdit_1059(String activityType, String objectType, String subject,String assigned_to, String severity, String logFile) throws Exception{
					Reporter.log("Validate User edited event and assigned it ", true);
					String expectedMsg="User edited event "+subject+" and assigned it to "+assigned_to;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1059() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Edit", "Event", "subject","assigned_to", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1060")
				public void verify_User_Profile_1060(String activityType, String objectType, String subject,String assigned_to, String severity, String logFile) throws Exception{
					Reporter.log("Validate User viewed profile ", true);
					String expectedMsg="User viewed profile";
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1060() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "View", "Profile", "", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1061")
				public void verify_User_UserProfile_1061(String activityType, String objectType, String user_title, String severity, String logFile) throws Exception{
					Reporter.log("Validate User viewed profile for user ", true);
					String expectedMsg="User viewed profile for user"+user_title;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1061() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "View", "Profile", "user_title", "informational", ""},
						
					};
				}
				
				@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1062")
				public void verify_User_GruopList_1062(String activityType, String objectType, String severity, String logFile) throws Exception{
					Reporter.log("Validate User browsed groups ", true);
					String expectedMsg="User browsed groups";
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1062() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "View", "Group", "informational", "group,Salesforce,browse_list.log"},
						
					};
				}
				
				@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1063")
				public void verify_User_GruopCreate_1063(String activityType, String objectType,String group_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User created a group with name ", true);
					String expectedMsg="User created a group with name "+group_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1063() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Create", "Group", "TestCase", "informational", "group,Salesforce,create.log"},
						
					};
				}
				
				@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1064")
				public void verify_User_GruopEdit_1064(String activityType, String objectType,String group_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User edited group ", true);
					String expectedMsg="User edited group "+group_name;
					replayLogsEPDV3(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1064() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Edit", "Group", "Test1 group", "informational", "group,edit.log"},
						
					};
				}
				
				@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1065")
				public void verify_User_GruopSearch_1065(String activityType, String objectType,String all, String test, String severity, String logFile) throws Exception{
					Reporter.log("Validate User searched a group ", true);
					String expectedMsg="User viewed "+all+" groups and searched for "+test;
					replayLogsWithDelay(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1065() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Search", "Group", "all","test", "informational", "group,Salesforce,search.log"},
						
					};
				}

				@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1066")
				public void verify_User_GruopSearch_1066(String activityType, String objectType, String all, String severity, String logFile) throws Exception{
					Reporter.log("Validate User viewed groups ", true);
					String expectedMsg="User viewed "+all+" groups";
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1066() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Filter", "Group", "all", "informational", "group,Salesforce,search_filter.log"},
						
					};
				}
				

			//	@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1067")
				public void verify_User_GruopSearch_1067(String activityType, String objectType,String all, String test, String severity, String logFile) throws Exception{
					Reporter.log("Validate User viewed group and searched for ", true);
					String expectedMsg="User viewed "+all+" groups and searched for "+test;
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1067() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Search", "Group","","","informational", "group,Salesforce,filter_searchWithName.log"},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1068")
				public void verify_User_GruopDelete_1068(String activityType, String objectType, String group_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User deleted the group ", true);
					String expectedMsg="User deleted the group "+group_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1068() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Delete", "Group","","informational", "group,Salesforce,delete.log"},
						
					};
				}
				
				@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1070")
				public void verify_User_FileSearch_1070(String activityType, String objectType, String file_search, String severity, String logFile) throws Exception{
					Reporter.log("Validate User searched a file ", true);
					String expectedMsg="User searched a file "+file_search;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1070() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Search", "File","FileUpload","informational", "file,salesforce,search.log"},
						
					};
				}
				
			//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1071")logs problem
				public void verify_User_FilesSearch_1071(String activityType, String objectType, String filter, String severity, String logFile) throws Exception{
					Reporter.log("Validate User viewed files ", true);
					String expectedMsg="User viewed "+filter+" files ";
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1071() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Filter", "File","Followed","informational", "file,salesforce,search_filter.log"},
						
					};
				}
				
				@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1072")
				public void verify_User_FileDelete_1072(String activityType, String objectType, String file_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User deleted the file ", true);
					String expectedMsg="User deleted the file "+file_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1072() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Delete", "File","","informational", "file,salesforce,delete.log"},
						
					};
				}
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1073")
				public void verify_User_SolutionsDownloadAttachment_1073(String activityType, String objectType, String file_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User downloded file from solution ", true);
					String expectedMsg="User downloaded file "+file_name+" from solution";
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1073() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Download", "File","file_name","informational", ""},
						
					};
				}
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1074")
				public void verify_User_FileDownloadSSO_1074(String activityType, String objectType, String file_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User downloaded a file named ", true);
					String expectedMsg="User downloaded a file named "+file_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1074() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Download", "File","file_name","informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST1","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1076")
				public void verify_User_FileDownload2SSO_1076(String activityType, String objectType, String file_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User downloaded a file named", true);
					String expectedMsg="User downloaded a file named "+file_name;
					replayLogsEPDV3(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1076() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Download", "File","Test.txt","informational", "file,download.log"},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1077")
				public void verify_User_FileDownload2_1077(String activityType, String objectType, String file_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User downloaded a file named ", true);
					String expectedMsg="User downloaded a file named "+file_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1077() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Download", "File","file_name","informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1080")
				public void verify_User_UnFileShare_1080(String activityType, String objectType, String file_name, String recp_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User unshared file ", true);
					String expectedMsg="User unshared file "+file_name+" from"+recp_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1080() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Unshare", "File","file_name","recp_name","informational", ""},
						
					};
				}
				

				@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1081")
				public void verify_User_FileEdit_1081(String activityType, String objectType, String filename, String severity, String logFile) throws Exception{
					Reporter.log("Validate User edited a file named ", true);
					String expectedMsg="User edited a file named "+filename;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1081() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Edit", "File","Hello","informational", "file,Salesforce,edit.log"},
						
					};
				}
				
				@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1082")
				public void verify_User_Document_1082(String activityType, String objectType, String severity, String logFile) throws Exception{
					Reporter.log("Validate User viewed documents ", true);
					String expectedMsg="User viewed documents";
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1082() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "View", "Document","informational", "documents,salesforce,view_document.log"},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1083")
				public void verify_User_DocumentEmail_1083(String activityType, String objectType, String doc_name, String to, String subject, String severity, String logFile) throws Exception{
					Reporter.log("Validate User emailed a document named with subject ", true);
					String expectedMsg="User emailed a document named "+doc_name+" to "+to+" with subject"+subject;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1083() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Share", "Document","doc_name","to","subject","informational", ""},
						
					};
				}
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1084")
				public void verify_User_DocumentEmail_1084(String activityType, String objectType, String doc_name, String to, String subject, String severity, String logFile) throws Exception{
					Reporter.log("Validate User emailed to with subject ", true);
					String expectedMsg="User emailed to "+to+" with subject"+subject;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1084() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Share", "Email","to","subject","informational", ""},
						
					};
				}
				
				@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1085")
				public void verify_User_DocumentView_1085(String activityType, String objectType, String severity, String logFile) throws Exception{
					Reporter.log("Validate User viewed documents ", true);
					String expectedMsg="User viewed documents";
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1085() {
					return new Object[][]{
					//Activity type  Object Type  Object Name  Severity  message
						{ "View", "Document","informational", "documents,salesforce,view_document.log"},
						
					};
				}
				
				@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1086")
				public void verify_User_DocumentSearch_1086(String activityType, String objectType, String search_string, String severity, String logFile) throws Exception{
					Reporter.log("Validate User searched a document ", true);
					String expectedMsg="User searched a document "+search_string;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1086() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Search", "Document","MultiPart1497005100493955210","informational", "document,salesforce,search.log"},
						
					};
				}
				
				@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1087") // logs problem
				public void verify_User_DocumentDelete_1087(String activityType, String objectType, String doc_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User deleted the document ", true);
					String expectedMsg="User deleted the document "+doc_name;
					replayLogsWithDelay(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1087() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Delete", "Document","","informational", "document,salesforce,delete.log"},
						
					};
				}
				
				@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1088")
				public void verify_User_NewDocument_1088(String activityType, String objectType, String file_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User created a new document named and file name ", true);
					String expectedMsg="User created a new document named  and file name "+file_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1088() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Create", "Document", "Screen Shot 2015-10-19 at 7.11.00 pm.png", "informational", "document,Salesforce,create.log"},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1089")
				public void verify_User_DocumentReplace_1089(String activityType, String objectType, String doc_name, String file_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User replaced document and new file name ", true);
					String expectedMsg="User replaced document "+doc_name+" and new file name is"+file_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1089() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Edit", "Document","", "", "informational", "document,Salesforce,replace.log"},
						
					};
				}
				
			@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1090")
				public void verify_User_DocumentEdit_1090(String activityType, String objectType, String doc_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User edited document ", true);
					String expectedMsg="User edited document "+doc_name;
					replayLogsEPDV3(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1090() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Edit", "Document","Testdoc", "informational", "document,edit.log"},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1091")
				public void verify_User_Leads_1091(String activityType, String objectType, String severity, String logFile) throws Exception{
					Reporter.log("Validate User viewed Leads ", true);
					String expectedMsg="User viewed Leads ";
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1091() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "View", "Lead", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1092")
				public void verify_User_Leads_1092(String activityType, String objectType, String first_name, String last_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User created a new lead with name ", true);
					String expectedMsg="User created a new lead with name "+first_name+""+last_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1092() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Create", "Lead","","", "informational", ""},
						
					};
				}
				
				@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1096")
				public void verify_User_LeadsNewView_1096(String activityType, String objectType, String view_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User created a new view for leads named ", true);
					String expectedMsg="User created a new view for leads named "+view_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1096() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Create", "View","Test5", "informational", "Leads,NewView.log"},
						
					};
				}
				
				@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1097")
				public void verify_User_LeadsCloneView_1097(String activityType, String objectType, String object_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User created a new lead with name ", true);
					String expectedMsg="User created a new lead with name "+object_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1097() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Create", "Lead","Test2 test2", "informational", "Leads,CloneView.log"},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1098")//logs problem
				public void verify_User_LeadConvert_1098(String activityType, String objectType, String object_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User converted a lead ", true);
					String expectedMsg="User converted a lead "+object_name;
					replayLogsEPDV3(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1098() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Convert", "Lead","", "informational", "lead,convert.log"},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1099")
				public void verify_User_LeadFindDuplicates_1099(String activityType, String objectType, String object_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User find duplicates of lead ", true);
					String expectedMsg="User find duplicates of lead "+object_name;
					replayLogsWithDelay(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1099() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Search", "Lead","", "informational", "Leads,FindDuplicate.log"},
						
					};
				}
				
				@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1200")
				public void verify_User_CampaignsCreate_1200(String activityType, String objectType, String campaign_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User created a campaign ", true);
					String expectedMsg="User created a campaign "+campaign_name;
					replayLogsEPDV3(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1200() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Create", "Campaign","GatewayTest", "informational", "campaigns,clone_view.log"},
						
					};
				}
				
				@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1201")
				public void verify_User_CampaignEdit_1201(String activityType, String objectType, String campaign_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User edited campaign ", true);
					String expectedMsg="User edited campaign "+campaign_name;
					replayLogsEPDV3(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1201() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Edit", "Campaign","GatewayTest", "informational", "campaigns,edit.log"},
						
					};
				}
				
				@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1202")
				public void verify_User_CampaignsView_1202(String activityType, String objectType, String severity, String logFile) throws Exception{
					Reporter.log("Validate User viewed Campaigns ", true);
					String expectedMsg="User viewed Campaigns";
					replayLogsEPDV3(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1202() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "View", "Campaign", "informational", "campaigns,view.log"},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1203")
				public void verify_User_CampaignDelete_1203(String activityType, String objectType, String campaign_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User deleted the campaign ", true);
					String expectedMsg="User deleted the campaign "+campaign_name;
					replayLogsEPDV3(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1203() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Delete", "Campaign","", "informational", "campaigns,delete.log"},
						
					};
				}
				

				//@Test(groups ={"TEST1","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1204")
				public void verify_User_CampaignsNewView_1204(String activityType, String objectType, String view_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User created new campaign view named ", true);
					String expectedMsg="User created new campaign view named "+view_name;
					replayLogsEPDV3(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1204() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Create", "View","", "informational", "campaigns,new_view.log"},
						
					};
				}
				
				//@Test(groups ={"TEST1","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1205")
				public void verify_User_CampaignsCloneView_1205(String activityType, String objectType, String view_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User cloned campaign view ", true);
					String expectedMsg="User cloned campaign view ";
					replayLogsEPDV3(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1205() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Create", "Campaign","", "informational", "campaigns,clone_view.log"},
						
					};
				}
				
				//@Test(groups ={"TEST1","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1206")
				public void verify_User_CampaignAdvanceSetup_1206(String activityType, String objectType, String object_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User viewed advanced setup for campaign named ", true);
					String expectedMsg="User viewed advanced setup for campaign named "+object_name;
					replayLogsEPDV3(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1206() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "View", "Campaign","", "informational", "campaigns,advance_setup.log"},
						
					};
				}
				
				//@Test(groups ={"TEST1","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1207")
				public void verify_User_CampaignAdvanceSetupReplace_1207(String activityType, String objectType, String old_value, String new_value, String campaign_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User replaced campaign member status in advanced setup for campaign named ", true);
					String expectedMsg="User replaced campaign member status from "+old_value+" to "+new_value+" in advanced setup for campaign named "+campaign_name;
					replayLogsEPDV3(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1207() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "View", "Campaign","","","", "informational", "campaigns,advance_setup_replace.log"},
						
					};
}
				
				//@Test(groups ={"TEST1","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1100")
				public void verify_User_NewNote_1100(String activityType, String objectType, String object_name, String note_title, String descp, String severity, String logFile) throws Exception{
					Reporter.log("Validate User created a note for object with title and description ", true);
					String expectedMsg="User created a note for object "+object_name+" with title "+note_title+" and description "+descp;
					replayLogsEPDV3(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1100() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Create", "Note","","","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1101")
				public void verify_User_NoteEdit_1101(String activityType, String objectType, String note_title, String severity, String logFile) throws Exception{
					Reporter.log("Validate User edited the note with title ", true);
					String expectedMsg="User edited the note with title "+note_title;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1101() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Edit", "Note","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1102")
				public void verify_User_NoteAttachmentViewAll_1102(String activityType, String objectType, String object_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User viewed all attachments for product ", true);
					String expectedMsg="User viewed all attachments for product "+object_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1102() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "View", "Attachment","", "informational", ""},
						
					};
				}
				
				@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1103")
				public void verify_User_AddCampaignLead_1103(String activityType, String objectType, String severity, String logFile) throws Exception{
					Reporter.log("Validate User created a campaign Test ", true);
					String expectedMsg="User created a campaign Test";
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1103() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Create", "CampaignLead", "informational", "Campaign,AddLead.log"},
						
					};
				}
				
				@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1104")
				public void verify_User_EditCampaignLead_1104(String activityType, String objectType, String object_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User edited campaign Test1 ", true);
					String expectedMsg="User edited campaign "+object_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1104() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Edit", "CampaignLead","Test1", "informational", "Campaign,EditLead.log"},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1105")
				public void verify_User_LeadsMerge_1105(String activityType, String objectType, String lead_name, String company_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User merged leads from company ", true);
					String expectedMsg="User merged leads "+lead_name+" from company "+company_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1105() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Merge", "Lead","","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1106")
				public void verify_User_LeadDuplicateSearch_1106(String activityType, String objectType, String object_name, String company, String severity, String logFile) throws Exception{
					Reporter.log("Validate User searched for duplicates for lead "+object_name+" belonging to company"+company , true);
					String expectedMsg="User searched for duplicates for lead "+object_name+" belonging to company "+company;
					replayLogsEPDV3(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1106() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Search", "Lead","Test test test","test.com", "informational", "lead,duplicate_search.log"},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1107")
				public void verify_User_Contacts_1107(String activityType, String objectType, String severity, String logFile) throws Exception{
					Reporter.log("Validate User viewed contacts" , true);
					String expectedMsg="User viewed contacts";
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1107() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "View", "Contact", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1108")
				public void verify_User_ContactsCreate_1108(String activityType, String objectType, String first_name, String last_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User created a new contact with name"+first_name+""+last_name , true);
					String expectedMsg="User created a new contact with name"+first_name+""+last_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1108() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Create", "Contact", "", "", "informational", ""},
						
					};
				}
				

				@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1112")
				public void verify_User_ContactsNewView_1112(String activityType, String objectType, String severity, String logFile) throws Exception{
					Reporter.log("Validate User created a new view Tests for contacts" , true);
					String expectedMsg="User created a new view Tests for contacts";
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1112() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Create", "View", "informational", "Contacts,NewView.log"},
						
					};
				}
				
				@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1113")
				public void verify_User_ContactsCloneView_1113(String activityType, String objectType, String severity, String logFile) throws Exception{
					Reporter.log("Validate User cloned contacts view " , true);
					String expectedMsg="User viewed Contacts";
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1113() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "View", "Contact", "informational", "Contacts,CloneView.log"},
						
					};
				}
				
				@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1114")
				public void verify_User_OrdersView_1114(String activityType, String objectType, String severity, String logFile) throws Exception{
					Reporter.log("Validate User viewed Orders " , true);
					String expectedMsg="User viewed Orders";
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1114() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "View", "Order", "informational", "Orders,View.log"},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1115")
				public void verify_User_Opportunity_1115(String activityType, String objectType, String severity, String logFile) throws Exception{
					Reporter.log("Validate User viewed Opportunities " , true);
					String expectedMsg="User viewed Opportunities ";
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1115() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "View", "Opportunity", "informational", ""},
						
					};
				}
				
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1118")
				public void verify_User_OpportunityEdit_1118(String activityType, String objectType, String opportunity_name, String ammount, String close_date, String severity, String logFile) throws Exception{
					Reporter.log("Validate User edited an "+opportunity_name+", ammount "+ammount+", close date "+close_date+" " , true);
					String expectedMsg="User edited an "+opportunity_name+", ammount "+ammount+", close date "+close_date;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1118() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Edit", "Opportunity","","","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1119")
				public void verify_User_OpportunityEdit_1119(String activityType, String objectType, String opportunity_name, String close_date, String severity, String logFile) throws Exception{
					Reporter.log("Validate User edited an "+opportunity_name+", close date "+close_date+" " , true);
					String expectedMsg="User edited an "+opportunity_name+", close date "+close_date;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1119() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Edit", "Opportunity","","", "informational", ""},
						
					};
				}
				
				@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1122")
				public void verify_User_OpportunitiesNewView_1122(String activityType, String objectType, String view_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User created a new view named "+view_name+" for opportunity"  , true);
					String expectedMsg="User created a new view named "+view_name+" for opportunity";
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1122() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "View", "Opportunity","Test", "informational", "Opportunities,NewView.log"},
						
					};
				}
				
				@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1123")
				public void verify_User_OpportunitiesCloneView_1123(String activityType, String objectType, String object_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User created a new opportunity with name " , true);
					String expectedMsg="User created a new opportunity with name "+object_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1123() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Create", "Opportunity","test.com-", "informational", "Opportunities,CloneView2.log"},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1124")
				public void verify_User_OpportunityActivityViewAll_1124(String activityType, String objectType, String object_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User viewed all activity for opportunity "+object_name+" " , true);
					String expectedMsg="User viewed all activity for opportunity "+object_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1124() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "View", "Opportunity","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1125")
				public void verify_User_CompetitorCreate_1125(String activityType, String objectType, String comp_name, String object_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User created a new competitor "+comp_name+" for opportunity "+object_name+" " , true);
					String expectedMsg="User created a new competitor "+comp_name+" for opportunity "+object_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1125() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Create", "Competitor","","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1126")
				public void verify_User_CompetitorEdit_1126(String activityType, String objectType, String object_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User edited a competitor named "+object_name+" " , true);
					String expectedMsg="User edited a competitor named "+object_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1126() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Edit", "Competitor","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1127")
				public void verify_User_LogCall_1127(String activityType, String objectType, String object_name, String assigned_to, String status, String severity, String logFile) throws Exception{
					Reporter.log("Validate User logged a call for object "+object_name+" and assigned to "+assigned_to+" with status "+status+" " , true);
					String expectedMsg="User logged a call for object "+object_name+" and assigned to "+assigned_to+" with status "+status;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1127() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Create", "Call","","","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1128")
				public void verify_User_TaskEdit_1128(String activityType, String objectType, String subject, String object_name, String assigned_to, String status, String severity, String logFile) throws Exception{
					Reporter.log("Validate User edited a task named "+subject+" for object "+object_name+" and assigned to "+assigned_to+" with status "+status+" " , true);
					String expectedMsg="User edited a task named "+subject+" for object "+object_name+" and assigned to "+assigned_to+" with status "+status;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1128() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Edit", "Task","","","","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1129")
				public void verify_User_TaskClose_1129(String activityType, String objectType, String object_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User closed a task for opportunity "+object_name+" " , true);
					String expectedMsg="User closed a task for opportunity "+object_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1129() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Create", "Task","", "informational", ""},
						
					};
				}
				
				@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1130")
				public void verify_User_AddProductSearch_1130(String activityType, String objectType, String severity, String logFile) throws Exception{
					Reporter.log("Validate User searched a product with name test " , true);
					String expectedMsg="User searched a product with name test";
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1130() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Search", "Opportunity", "informational", "Product,AddSearch.log"},
						
					};
				}
				
				@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1131")
				public void verify_User_OpportunityAddProduct_1131(String activityType, String objectType, String severity, String logFile) throws Exception{
					Reporter.log("Validate User added a product to opportunity " , true);
					String expectedMsg="User viewed books price";
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1131() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "View", "Opportunity", "informational", "Opportunity,AddProduct.log"},
						
					};
				}
				
				@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1132")
				public void verify_User_OpportunityAddPriceBook_1132(String activityType, String objectType, String severity, String logFile) throws Exception{
					Reporter.log("Validate User viewed books price " , true);
					String expectedMsg="User viewed books price";
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1132() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "View", "Opportunity", "informational", "Opportunity,AddPriceBook.log"},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1133")
				public void verify_User_OpportunitySort_1133(String activityType, String objectType,  String object_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User sorted products for opportunity "+object_name+" " , true);
					String expectedMsg="User changed price book for opportunity "+object_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1133() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Sort", "Opportunity","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1134")
				public void verify_User_OpportunityProductEdit_1134(String activityType, String objectType,  String object_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User edited the products attached to opportunity "+object_name+" " , true);
					String expectedMsg="User edited the products attached to opportunity "+object_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1134() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Edit", "Opportunity","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1135")
				public void verify_User_PartnerRoleCreate_1135(String activityType, String objectType, String partner_name,  String opp_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User added partners "+partner_name+" to opportunity "+opp_name+" " , true);
					String expectedMsg="User added partners "+partner_name+" to opportunity "+opp_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1135() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Create", "Opportunity","","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1136")
				public void verify_User_ContactRoleAdd_1136(String activityType, String objectType, String contact_name,  String opp_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User added contacts "+contact_name+" to opportunity "+opp_name+" " , true);
					String expectedMsg="User added contacts "+contact_name+" to opportunity "+opp_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1136() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Create", "Contact","","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1137")
				public void verify_User_PartnerRoleDel_1137(String activityType, String objectType, String prod_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User deleted a partner for opportunity "+prod_name+" " , true);
					String expectedMsg="User deleted a partner for opportunity "+prod_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1137() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Delete", "Opportunity","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1138")
				public void verify_User_CompetitorDel_1138(String activityType, String objectType, String prod_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User deleted a competitor for opportunity "+prod_name+" " , true);
					String expectedMsg="User deleted a competitor for opportunity "+prod_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1138() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Delete", "Opportunity","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1139")
				public void verify_User_ContactRoleDel_1139(String activityType, String objectType, String prod_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User deleted a contact for opportunity "+prod_name+" " , true);
					String expectedMsg="User deleted a contact for opportunity "+prod_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1139() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Delete", "Opportunity","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1140")
				public void verify_User_NoteDel_1140(String activityType, String objectType, String prod_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User deleted a note for opportunity "+prod_name+" " , true);
					String expectedMsg="User deleted a note for opportunity "+prod_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1140() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Delete", "Opportunity","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1141")
				public void verify_User_AttachmentDel_1141(String activityType, String objectType, String prod_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User deleted an attachment for "+prod_name+" " , true);
					String expectedMsg="User deleted an attachment for "+prod_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1141() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Delete", "Attachment","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1142")
				public void verify_User_ProductDel_1142(String activityType, String objectType, String prod_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User deleted a product for opportunity "+prod_name+" " , true);
					String expectedMsg="User deleted a product for opportunity "+prod_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1142() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Delete", "Opportunity","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST1","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1143")
				public void verify_User_TaskDel_1143(String activityType, String objectType, String prod_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User deleted a task for opportunity "+prod_name+" " , true);
					String expectedMsg="User deleted a task for opportunity "+prod_name;
					replayLogsEPDV3(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1143() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Delete", "Opportunity","", "informational", "Task,Delete1.log"},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1144")
				public void verify_User_EventDel_1144(String activityType, String objectType, String object_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User deleted an event for opportunity "+object_name+" " , true);
					String expectedMsg="User deleted an event for opportunity "+object_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1144() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Delete", "Opportunity","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1145")
				public void verify_User_CampaignAddDel_1145(String activityType, String objectType, String prod_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User deleted a campaign for lead "+prod_name+" " , true);
					String expectedMsg="User deleted a campaign for lead "+prod_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1145() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Delete", "Campaign","", "informational", ""},
						
					};
				}
				
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1146") // this is matching with _1150
				public void verify_User_Accounts_1146(String activityType, String objectType, String prod_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User viewed accounts " , true);
					String expectedMsg="User viewed accounts";
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1146() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "View", "Account", "informational", ""},
						
					};
				}
				
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1149") 
				public void verify_User_AccountShare_1149(String activityType, String objectType, String account_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User shared the account "+account_name+"" , true);
					String expectedMsg="User shared the account "+account_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1149() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Share", "Account","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1152") 
				public void verify_User_AccountNewView_1152(String activityType, String objectType, String severity, String logFile) throws Exception{
					Reporter.log("Validate User created a new view for accounts " , true);
					String expectedMsg="User created a new view for accounts ";
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1152() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Create", "Account", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1153") 
				public void verify_User_AccountCloneView_1153(String activityType, String objectType, String severity, String logFile) throws Exception{
					Reporter.log("Validate User cloned accounts view " , true);
					String expectedMsg="User cloned accounts view ";
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1153() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Create", "Account", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1154") 
				public void verify_User_DashBoard_1154(String activityType, String objectType, String severity, String logFile) throws Exception{
					Reporter.log("Validate User viewed Dashboards " , true);
					String expectedMsg="User viewed Dashboards ";
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1154() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "View", "Dashboard", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1155") 
				public void verify_User_ReportView_1155(String activityType, String objectType, String severity, String logFile) throws Exception{
					Reporter.log("Validate User viewed folders in reports " , true);
					String expectedMsg="User viewed folders in reports ";
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1155() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "View", "Report", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1156") 
				public void verify_User_ReportSubFolderView_1156(String activityType, String objectType, String severity, String logFile) throws Exception{
					Reporter.log("Validate User viewed sub folder in reports " , true);
					String expectedMsg="User viewed sub folder in reports ";
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1156() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "View", "Report", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1157") 
				public void verify_User_DashboardNewReportFolder_1157(String activityType, String objectType, String folder, String severity, String logFile) throws Exception{
					Reporter.log("Validate User created a new folder with name "+folder+" " , true);
					String expectedMsg="User created a new folder with name "+folder;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1157() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Create", "Folder","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1159") 
				public void verify_User_DashboadNew_1159(String activityType, String objectType, String name, String dev_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User created a new dashboard named "+name+" developer name "+dev_name+" " , true);
					String expectedMsg="User created a new dashboard named "+name+" developer name "+dev_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1159() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Create", "Dashboard","","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1160") 
				public void verify_User_DashboadEdit_1160(String activityType, String objectType, String name, String dev_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User edited a dashboard named "+name+" developer name "+dev_name+" " , true);
					String expectedMsg="User edited a dashboard named "+name+" developer name "+dev_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1160() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Edit", "Dashboard","","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1161") 
				public void verify_User_Reports_1161(String activityType, String objectType, String severity, String logFile) throws Exception{
					Reporter.log("Validate User viewed Reports " , true);
					String expectedMsg="User viewed Reports";
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1161() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "View", "Report", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1162") 
				public void verify_User_DashboardSearch_1162(String activityType, String objectType, String search_string, String severity, String logFile) throws Exception{
					Reporter.log("Validate User searched "+search_string+" in dashboard " , true);
					String expectedMsg="User searched "+search_string+" in dashboard";
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1162() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Search", "Dashboard","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1163") 
				public void verify_User_DashboardPreview_1163(String activityType, String objectType, String object_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User previewed dashboard "+object_name+" " , true);
					String expectedMsg="User previewed dashboard "+object_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1163() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "View", "Dashboard","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1164") 
				public void verify_User_DashboardList_1164(String activityType, String objectType, String severity, String logFile) throws Exception{
					Reporter.log("Validate User viewed Dashboard list " , true);
					String expectedMsg="User viewed Dashboard List ";
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1164() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "View", "Dashboard", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1165") 
				public void verify_User_Products_1165(String activityType, String objectType, String severity, String logFile) throws Exception{
					Reporter.log("Validate User viewed Products " , true);
					String expectedMsg="User viewed Products ";
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1165() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "View", "Product", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1166") 
				public void verify_User_ProductCreate_1166(String activityType, String objectType, String prod_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User created a product with name "+prod_name+" " , true);
					String expectedMsg="User created a product with name "+prod_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1166() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Create", "Product", "", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1167") 
				public void verify_User_ProductAddPrice_1167(String activityType, String objectType, String product_name, String price, String severity, String logFile) throws Exception{
					Reporter.log("Validate User added price for a product "+product_name+" and the price is "+price+" " , true);
					String expectedMsg="User added price for a product "+product_name+" and the price is "+price;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1167() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Create", "Price", "","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1168") 
				public void verify_User_ProductEdit_1168(String activityType, String objectType, String prod_name, String price, String severity, String logFile) throws Exception{
					Reporter.log("Validate User edited a product "+prod_name+" " , true);
					String expectedMsg="User edited a product "+prod_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1168() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Edit", "Product", "", "informational", ""},
						
					};
				}
				

				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1169") 
				public void verify_User_ProductDelete_1169(String activityType, String objectType, String product_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User deleted the product "+product_name+" " , true);
					String expectedMsg="User deleted the product "+product_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1169() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Delete", "Product", "", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1170") 
				public void verify_User_ProductPriceDelete_1170(String activityType, String objectType, String product_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User deleted price of product "+product_name+" " , true);
					String expectedMsg="User deleted price of product "+product_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1170() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Delete", "Price", "", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "1171") 
				public void verify_User_ProductPriceEditCustom_1171(String activityType, String objectType, String product_name, String price, String severity, String logFile) throws Exception{
					Reporter.log("Validate User edited custom price for product "+product_name+" and price is "+price+" " , true);
					String expectedMsg="User edited custom price for product "+product_name+" and price is "+price;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1171() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Edit", "Price", "", "", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "1172") 
				public void verify_User_ProductPriceEditCustom_1172(String activityType, String objectType, String product_name, String orig_price, String severity, String logFile) throws Exception{
					Reporter.log("Validate User edited custom price for product "+product_name+" and price is "+orig_price+" " , true);
					String expectedMsg="User edited custom price for product "+product_name+" and price is "+orig_price;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1172() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Edit", "Price", "", "", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "1173") 
				public void verify_User_ProductsSearch_1173(String activityType, String objectType, String product_search, String severity, String logFile) throws Exception{
					Reporter.log("Validate User searched a product with name "+product_search+"  " , true);
					String expectedMsg="User searched a product with name "+product_search;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1173() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Search", "Product", "", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "1174") 
				public void verify_User_ProductsView_1174(String activityType, String objectType, String severity, String logFile) throws Exception{
					Reporter.log("Validate User viewed products" , true);
					String expectedMsg="User viewed products";
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1174() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "View", "Product", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "1175") 
				public void verify_User_ProductsPriceBooksView_1175(String activityType, String objectType, String severity, String logFile) throws Exception{
					Reporter.log("Validate User viewed books price" , true);
					String expectedMsg="User viewed books price";
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1175() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "View", "Product", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "1176") 
				public void verify_User_AssetsView_1176(String activityType, String objectType, String severity, String logFile) throws Exception{
					Reporter.log("Validate User viewed assets" , true);
					String expectedMsg="User viewed assets";
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1176() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "View", "Product", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "1177") 
				public void verify_User_ProductNewBookView_1177(String activityType, String objectType, String view_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User created a new book view named "+view_name+" " , true);
					String expectedMsg="User created a new book view named "+view_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1177() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Create", "View", "", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "1178") 
				public void verify_User_ProductNewView_1178(String activityType, String objectType, String view_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User created a new product view named "+view_name+" " , true);
					String expectedMsg="User created a new product view named "+view_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1178() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Create", "View", "", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "1179") 
				public void verify_User_ProductNewAssetView_1179(String activityType, String objectType, String view_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User created a new asset view named "+view_name+" " , true);
					String expectedMsg="User created a new asset view named "+view_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1179() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Create", "View", "", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "1180") 
				public void verify_User_ProductAttachFile_1180(String activityType, String objectType, String file_name, String prod_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User attached a file "+file_name+" to the solution "+prod_name+" " , true);
					String expectedMsg="User attached a file "+file_name+" to the solution "+prod_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1180() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Attach", "File", "","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "1181") 
				public void verify_User_ProductAttachmentViewAll_1181(String activityType, String objectType, String object_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User viewed all attachments for object "+object_name+" " , true);
					String expectedMsg="User viewed all attachments for object "+object_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1181() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "View", "Product", "", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "1182") 
				public void verify_User_ReportEdit_1182(String activityType, String objectType, String report_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User edited report "+report_name+" " , true);
					String expectedMsg="User edited report "+report_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1182() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Edit", "Report", "", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "1184") 
				public void verify_User_Ideas_1184(String activityType, String objectType, String severity, String logFile) throws Exception{
					Reporter.log("Validate User viewed ideas " , true);
					String expectedMsg="User viewed ideas ";
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1184() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "View", "Idea", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "1185") 
				public void verify_User_IdeasPost_1185(String activityType, String objectType, String idea_title, String severity, String logFile) throws Exception{
					Reporter.log("Validate User posted an idea with title "+idea_title+" " , true);
					String expectedMsg="User posted an idea with title "+idea_title;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1185() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Post", "Ideas","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "1186") 
				public void verify_User_IdeasViewPopular_1186(String activityType, String objectType, String severity, String logFile) throws Exception{
					Reporter.log("Validate User viewed popular ideas " , true);
					String expectedMsg="User viewed popular ideas ";
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1186() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "View", "Idea", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "1187") 
				public void verify_User_IdeasViewRecent_1187(String activityType, String objectType, String severity, String logFile) throws Exception{
					Reporter.log("Validate User viewed recent ideas " , true);
					String expectedMsg="User viewed recent ideas ";
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1187() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "View", "Idea", "informational", ""},
						
					};
				}
				
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "1188") 
				public void verify_User_IdeasViewTop_1188(String activityType, String objectType, String severity, String logFile) throws Exception{
					Reporter.log("Validate User viewed top ideas " , true);
					String expectedMsg="User viewed top ideas ";
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1188() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "View", "Idea", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "1189") 
				public void verify_User_IdeasViewComments_1189(String activityType, String objectType, String severity, String logFile) throws Exception{
					Reporter.log("Validate User viewed idea comments " , true);
					String expectedMsg="User viewed idea comments ";
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1189() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "View", "Idea", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "1190") 
				public void verify_User_IdeaView_1190(String activityType, String objectType, String idea_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User viewed an idea "+idea_name+" " , true);
					String expectedMsg="User viewed an idea "+idea_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1190() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "View", "Idea","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "1191") 
				public void verify_User_IdeasPostComments_1191(String activityType, String objectType, String comments, String idea_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User posted comments "+comments+" for an idea "+idea_name+" " , true);
					String expectedMsg="User posted comments "+comments+" for an idea "+idea_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1191() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Social", "Idea","","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "1192") 
				public void verify_User_IdeaEditComments_1192(String activityType, String objectType, String comments, String severity, String logFile) throws Exception{
					Reporter.log("Validate User edited an idea comments "+"."+" The edited comment is "+comments+" " , true);
					String expectedMsg="User edited an idea comments "+"."+" The edited comment is "+comments;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1192() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Edit", "Comment","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "1193") 
				public void verify_User_IdeaEdit_1193(String activityType, String objectType, String title, String severity, String logFile) throws Exception{
					Reporter.log("Validate User edited the idea "+title+" " , true);
					String expectedMsg="User edited the idea "+title;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1193() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Edit", "Idea","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "1194") 
				public void verify_User_IdeaDeleteComment_1194(String activityType, String objectType, String idea_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User deleted the idea/Comment "+idea_name+" " , true);
					String expectedMsg="User deleted the idea/Comment "+idea_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1194() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Delete", "Idea","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "1195") 
				public void verify_User_IdeaFindDuplicateSearch_1195(String activityType, String objectType, String object_name, String search_title, String severity, String logFile) throws Exception{
					Reporter.log("Validate User searched the idea "+object_name+" for duplicate with search string "+search_title+" " , true);
					String expectedMsg="User searched the idea "+object_name+" for duplicate with search string "+search_title;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1195() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Search", "Idea","","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "1196") 
				public void verify_User_IdeaFindDuplicate_1196(String activityType, String objectType, String object_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User searched the idea "+object_name+" for duplicate " , true);
					String expectedMsg="User searched the idea "+object_name+" for duplicate";
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1196() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Search", "Idea","", "informational", ""},
						
					};
				}
				
				@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1197") 
				public void verify_User_DataCom_1197(String activityType, String objectType, String severity, String logFile) throws Exception{
					Reporter.log("Validate User viewed DataCom " , true);
					String expectedMsg="[ERROR] The following activity failed:  User viewed DataCom";
					replayLogsEPDV3(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1197() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "View", "DataCom", "informational", "dataCom,Salesforce,view.log"},
						
					};
				}
				
				@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1198") 
				public void verify_User_DataComSearch_1198(String activityType, String objectType, String search, String severity, String logFile) throws Exception{
					Reporter.log("Validate User searched "+search+" in DataCom " , true);
					String expectedMsg="[ERROR] The following activity failed:  User searched "+search+" in DataCom";
					replayLogsEPDV3(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1198() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Search", "DataCom","Elastica", "informational", "dataCom,Salesforce,search.log"},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "1199") 
				public void verify_User_Campaigns_1199(String activityType, String objectType, String severity, String logFile) throws Exception{
					Reporter.log("Validate User viewed campaigns " , true);
					String expectedMsg="User viewed campaigns ";
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1199() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "View", "Campaign", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "1208") 
				public void verify_User_CampaignAdvanceSetupEdit_1208(String activityType, String objectType, String object_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User edited advanced setup for campaign named "+object_name+" " , true);
					String expectedMsg="User edited advanced setup for campaign named "+object_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1208() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Edit", "Campaign","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "1209") 
				public void verify_User_CampaignMembersAdd_1209(String activityType, String objectType, String type, String camp_name, String object_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User added "+type+" to campaign "+camp_name+" having IDs "+object_name+" "   , true);
					String expectedMsg="User added "+type+" to campaign "+camp_name+" having IDs "+object_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1209() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Create", "Campaign","","","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "1210") 
				public void verify_User_CampaignMassMembersRemove_1210(String activityType, String objectType, String type, String camp_name, String object_id, String severity, String logFile) throws Exception{
					Reporter.log("Validate User removed "+type+" from campaign "+camp_name+" having IDs "+object_id+" " , true);
					String expectedMsg="User removed "+type+" from campaign "+camp_name+" having IDs "+object_id;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1210() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Delete", "Campaign","","","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "1211") 
				public void verify_User_Cases_1211(String activityType, String objectType, String severity, String logFile) throws Exception{
					Reporter.log("Validate User viewed Cases " , true);
					String expectedMsg="User viewed Cases";
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1211() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "View", "Case", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "1212") 
				public void verify_User_CasesCreate_1212(String activityType, String objectType, String contact_name, String status, String priority, String severity, String logFile) throws Exception{
					Reporter.log("Validate User created case for "+contact_name+" status,"+status+" and priority "+priority+" " , true);
					String expectedMsg="User created case for "+contact_name+" status, "+status+" and priority "+priority;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1212() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Create", "Case","","","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "1213") 
				public void verify_User_CasesCreate_1213(String activityType, String objectType, String status, String priority, String severity, String logFile) throws Exception{
					Reporter.log("Validate User created case, status "+status+" and priority "+priority+" " , true);
					String expectedMsg="User created case, status "+status+" and priority "+priority;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1213() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Create", "Case","","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "1214") 
				public void verify_User_CasesEdit_1214(String activityType, String objectType, String case_name, String contact_name, String status, String priority, String severity, String logFile) throws Exception{
					Reporter.log("Validate User edited case "+case_name+" for contact "+contact_name+" status, "+status+" and priority "+priority+" " , true);
					String expectedMsg="User edited case "+case_name+" for contact "+contact_name+" status, "+status+" and priority "+priority;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1214() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Edit", "Case","","","","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "1215") 
				public void verify_User_CasesEdit_1215(String activityType, String objectType, String case_name, String status, String priority, String severity, String logFile) throws Exception{
					Reporter.log("Validate User edited case "+case_name+", status "+status+" and priority "+priority+" " , true);
					String expectedMsg="User edited case "+case_name+", status "+status+" and priority "+priority;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1215() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Edit", "Case","","","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "1216") 
				public void verify_User_CasesView_1216(String activityType, String objectType, String severity, String logFile) throws Exception{
					Reporter.log("Validate User viewed Cases " , true);
					String expectedMsg="User viewed Cases";
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1216() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "View", "Case", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "1217") 
				public void verify_User_CasesNewView_1217(String activityType, String objectType, String view_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User created new view for cases named "+view_name+" " , true);
					String expectedMsg="User created new view for cases named "+view_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1217() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Create", "View","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "1218") 
				public void verify_User_CasesCloneView_1218(String activityType, String objectType, String severity, String logFile) throws Exception{
					Reporter.log("Validate User cloned cases view " , true);
					String expectedMsg="User cloned cases view";
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1218() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Create", "Case", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST1","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1219") 
				public void verify_User_CaseDelete_1219(String activityType, String objectType, String case_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User deleted the case "+case_name+" " , true);
					String expectedMsg="User deleted the case "+case_name;
					replayLogsEPDV3(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1219() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Delete", "Case","", "informational", "Case,Delete.log"},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "1220") 
				public void verify_User_CaseClose_1220(String activityType, String objectType, String case_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User closed the case "+case_name+" " , true);
					String expectedMsg="User closed the case "+case_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1220() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Delete", "Case","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "1221") 
				public void verify_User_Contracts_1221(String activityType, String objectType, String case_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User viewed contracts " , true);
					String expectedMsg="User closed the case "+case_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1221() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "View", "Contract", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "1222") 
				public void verify_User_ContractsCreate_1222(String activityType, String objectType, String account_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User created a contract for account "+account_name+" " , true);
					String expectedMsg="User created a contract for account "+account_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1222() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Create", "Contract","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "1223") 
				public void verify_User_ContractsEdit_1223(String activityType, String objectType, String account_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User edited a contract for account "+account_name+" " , true);
					String expectedMsg="User edited a contract for account "+account_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1223() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Edit", "Contract","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "1224") 
				public void verify_User_ContractsView_1224(String activityType, String objectType, String account_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User viewed contracts " , true);
					String expectedMsg="User viewed contracts ";
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1224() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "View", "Contract", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "1225") 
				public void verify_User_ContractsDelete_1225(String activityType, String objectType, String contract_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User deleted the contract "+contract_name+" " , true);
					String expectedMsg="User deleted the contract "+contract_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1225() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Delete", "Contract","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "1226") 
				public void verify_User_ContractsNewView_1226(String activityType, String objectType, String view_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User created new contract view named "+view_name+" " , true);
					String expectedMsg="User created new contract view named "+view_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1226() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Create", "View","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "1227") 
				public void verify_User_ContractsCloneView_1227(String activityType, String objectType, String severity, String logFile) throws Exception{
					Reporter.log("Validate User cloned contracts view " , true);
					String expectedMsg="User cloned contracts view ";
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1227() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Create", "Contract", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "1228") 
				public void verify_User_ContractActivate_1228(String activityType, String objectType, String object_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User activated contract "+object_name+" " , true);
					String expectedMsg="User activated contract "+object_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1228() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Edit", "Contract","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "1229") 
				public void verify_User_ContractItemsApprove_1229(String activityType, String objectType, String object_name, String account_name, String descp, String severity, String logFile) throws Exception{
					Reporter.log("Validate User created an item for contract "+object_name+" and assigned it to "+account_name+" and description "+descp+" " , true);
					String expectedMsg="User created an item for contract "+object_name+" and assigned it to "+account_name+" and decription "+descp;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1229() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Create", "Contract","","","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "1230") 
				public void verify_User_ContractItemsEdit_1230(String activityType, String objectType, String object_name, String descp, String severity, String logFile) throws Exception{
					Reporter.log("Validate User edited an item "+object_name+" for approval with description "+descp+" " , true);
					String expectedMsg="User edited an item "+object_name+" for approval with decription "+descp;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1230() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Edit", "Contract","","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "1231") 
				public void verify_User_ContractViewAllItems_1231(String activityType, String objectType, String object_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User viewed all items for contract "+object_name+" " , true);
					String expectedMsg="User viewed all items for contract "+object_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
				}
				
				@DataProvider
				public Object[][] _1231() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "View", "Contract","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "1232") 
				public void verify_User_People_1232(String activityType, String objectType, String object_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User viewed People " , true);
					String expectedMsg="User viewed People ";
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1232() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "View", "People","informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "1233") 
				public void verify_User_PeopleSearch_1233(String activityType, String objectType, String search_text, String severity, String logFile) throws Exception{
					Reporter.log("Validate User search people "+search_text+" " , true);
					String expectedMsg="User search people "+search_text;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1233() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Search", "People","","informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "1234") 
				public void verify_User_PeopleFollow_1234(String activityType, String objectType, String person_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User started following "+person_name+" " , true);
					String expectedMsg="User started following "+person_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1234() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Follow", "Item","","informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "1235") 
				public void verify_User_PeopleUnfollow_1235(String activityType, String objectType, String person_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User unfollowed "+person_name+" " , true);
					String expectedMsg="User unfollowed "+person_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1235() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Unfollow", "Item","","informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "1236") 
				public void verify_User_Portal_1236(String activityType, String objectType, String severity, String logFile) throws Exception{
					Reporter.log("Validate User viewed portal " , true);
					String expectedMsg="User viewed portal";
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1236() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "View", "Portal", "informational", ""},
						
					};
				}
				
				@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1237") 
				public void verify_User_Solutions_1237(String activityType, String objectType, String severity, String logFile) throws Exception{
					Reporter.log("Validate User viewed solutions " , true);
					String expectedMsg="User viewed solutions";
					replayLogsEPDV3(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1237() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "View", "Solution", "informational", "solutions,view.log"},
						
					};
				}
				
				@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1238") 
				public void verify_User_SolutionsCreate_1238(String activityType, String objectType, String solution_name, String solution_desc, String severity, String logFile) throws Exception{
					Reporter.log("Validate User created solution with name "+solution_name+" and description "+solution_desc+" " , true);
					String expectedMsg="User created solution with name "+solution_name+" and description "+solution_desc;
					replayLogsEPDV3(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1238() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Create", "Solutions","GTest","Testing", "informational", "solutions,create_with_desc.log"},
						
					};
				}
				
				@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1239") 
				public void verify_User_SolutionsCreate_1239(String activityType, String objectType, String solution_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User created solution with name "+solution_name+" " , true);
					String expectedMsg="User created solution with name "+solution_name;
					replayLogsEPDV3(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1239() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Create", "Solution","TestDoc", "informational", "solutions,create.log"},
						
					};
				}
				
				@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1240") 
				public void verify_User_SolutionEdit_1240(String activityType, String objectType, String solution_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User edited solution "+solution_name+" " , true);
					String expectedMsg="User edited solution "+solution_name;
					replayLogsEPDV3(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1240() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Edit", "Solution","TestDoc1", "informational", "solutions,edit.log"},
						
					};
				}
				
				@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1242") 
				public void verify_User_SolutionSearch_1242(String activityType, String objectType, String search_field, String severity, String logFile) throws Exception{
					Reporter.log("Validate User searched solutions "+search_field+" " , true);
					String expectedMsg="User searched solutions "+search_field;
					replayLogsEPDV3(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1242() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Search", "Solution","GTest", "informational", "solutions,search.log"},
						
					};
				}
				
				//@Test(groups ={"TEST1","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1243") 
				public void verify_User_SolutionsDelete_1243(String activityType, String objectType, String sol_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User deleted the solution "+sol_name+" " , true);
					String expectedMsg="User deleted the solution "+sol_name;
					replayLogsEPDV3(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1243() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Delete", "Solution","", "informational", "solutions,delete.log"},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "1244") 
				public void verify_User_SolutionsNewView_1244(String activityType, String objectType, String view_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User craeted new solutions view named "+view_name+" " , true);
					String expectedMsg="User created new solutions view named "+view_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1244() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Create", "View","", "informational", ""},
						
					};
				}
			
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "1245") 
				public void verify_User_SolutionsCloneView_1245(String activityType, String objectType, String severity, String logFile) throws Exception{
					Reporter.log("Validate User cloned solutions view " , true);
					String expectedMsg="User cloned solutions view ";
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1245() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Create", "Solution", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "1246") 
				public void verify_User_GmailIntegration_1246(String activityType, String objectType, String to, String severity, String logFile) throws Exception{
					Reporter.log("Validate User sent a Gmail to "+to+" " , true);
					String expectedMsg="User sent a Gmail to "+to;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1246() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Email", "Send","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "1247") 
				public void verify_User_SalesforceObjectView_1247(String activityType, String objectType, String object_type, String name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User viewed "+object_type+" named "+name+" " , true);
					String expectedMsg="User viewed "+object_type+" named "+name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1247() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "View", "object_type","","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "1248") 
				public void verify_User_SalesforceObjectView_1248(String activityType, String objectType, String object_type, String severity, String logFile) throws Exception{
					Reporter.log("Validate User viewed attachment for the solution named "+object_type+" " , true);
					String expectedMsg="User viewed attachment for the solution named "+object_type;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1248() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "View", "Attachment","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "1249") 
				public void verify_User_SalesforceObjectView_1249(String activityType, String objectType, String report, String object_type, String severity, String logFile) throws Exception{
					Reporter.log("Validate User viewed "+report+" named "+object_type+" " , true);
					String expectedMsg="User viewed "+report+" named "+object_type;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1249() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "View", "Report","","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "1250") 
				public void verify_User_GroupsObjectView_1250(String activityType, String objectType, String grp_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User viewed a group named "+grp_name+" " , true);
					String expectedMsg="User viewed a group named "+grp_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1250() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "View", "Group","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "1251") 
				public void verify_User_RemoveFromBriefcase_1251(String activityType, String objectType, String object_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User removed an object "+object_name+" from briefcase  " , true);
					String expectedMsg="User removed an object "+object_name+" from briefcase";
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1251() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Delete", "Briefcase","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "1252") 
				public void verify_User_AddToBriefCase_1252(String activityType, String objectType, String object_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User added "+object_name+" to briefcase  " , true);
					String expectedMsg="User added "+object_name+" to briefcase";
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1252() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Create", "Briefcase","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "1253") 
				public void verify_User_EditBriefcase_1253(String activityType, String objectType, String object_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User edited briefcase of accounts  " , true);
					String expectedMsg="User edited briefcase of accounts";
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1253() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Edit", "Briefcase","informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "1254") 
				public void verify_User_ObjectViewHomepage_1254(String activityType, String objectType, String object_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User viewed objects from its homepage " , true);
					String expectedMsg="User viewed objects from its homepage";
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1254() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "View", "Homepage","informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "1255") 
				public void verify_User_RightPaneObjectViewFields_1255(String activityType, String objectType, String object_type, String severity, String logFile) throws Exception{
					Reporter.log("Validate User viewed fields of object "+object_type+" " , true);
					String expectedMsg="User viewed fields of object "+object_type;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1255() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "View", "object_type","","informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "1256") 
				public void verify_User_RightPaneNewFields_1256(String activityType, String objectType, String type, String severity, String logFile) throws Exception{
					Reporter.log("Validate User created new fields for object "+type+" " , true);
					String expectedMsg="User created new fields for object "+type;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1256() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Create", "type","","informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "1257") 
				public void verify_User_RightPaneObjectView_1257(String activityType, String objectType, String object_type, String severity, String logFile) throws Exception{
					Reporter.log("Validate User viewed object"+object_type+" " , true);
					String expectedMsg="User viewed object "+object_type;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1257() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "View", "object_type","","informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "1258") 
				public void verify_User_RightPaneObjectCreate_1258(String activityType, String objectType, String record_name, String descp, String severity, String logFile) throws Exception{
					Reporter.log("Validate User created a new object named "+record_name+" with description "+descp+"  " , true);
					String expectedMsg="User created a new object named "+record_name+" with description "+descp;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1258() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Create", "RightPane","","","informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "1259") 
				public void verify_User_RightPaneViewRecordTypes_1259(String activityType, String objectType, String object_type, String severity, String logFile) throws Exception{
					Reporter.log("Validate User viewed record types of object "+object_type+"  " , true);
					String expectedMsg="User viewed record types of object "+object_type;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1259() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "View", "object_type","","informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "1260") 
				public void verify_User_RightPaneNewRecordTypes_1260(String activityType, String objectType, String record_name, String descp, String severity, String logFile) throws Exception{
					Reporter.log("Validate User created a new record type named "+record_name+" with description "+descp+" " , true);
					String expectedMsg="User created a new record type named "+record_name+" with description "+descp;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1260() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Create", "RightPane","","","informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "1261") 
				public void verify_User_RightPaneViewValidationRules_1261(String activityType, String objectType, String object_type, String severity, String logFile) throws Exception{
					Reporter.log("Validate User viewed validation rules of object "+object_type+" " , true);
					String expectedMsg="User viewed validation rules of object "+object_type;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1261() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "View", "object_type","","informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "1262") 
				public void verify_User_RightPaneNewValidationRules_1262(String activityType, String objectType, String name, String descp, String severity, String logFile) throws Exception{
					Reporter.log("Validate User created a new validation rule named "+name+" with description "+descp+" " , true);
					String expectedMsg="User created a new validation rule named "+name+" with description "+descp;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1262() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Create", "RightPane","","","informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "1263") 
				public void verify_User_RightPaneViewApprovals_1263(String activityType, String objectType, String object_type, String severity, String logFile) throws Exception{
					Reporter.log("Validate User viewed approvals for object "+object_type+" " , true);
					String expectedMsg="User viewed approvals for object "+object_type;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1263() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "View", "object_type","","informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "1264") 
				public void verify_User_RightPaneNewApprovals_1264(String activityType, String objectType, String name, String unique_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User created a new approval named "+name+" and unique name being "+unique_name+" " , true);
					String expectedMsg="User created a new approval named "+name+" and unique name being "+unique_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1264() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Create", "RightPane","","","informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "1265") 
				public void verify_User_ProductAttachmentDelete_1265(String activityType, String objectType, String object_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User deleted the object "+object_name+" " , true);
					String expectedMsg="User deleted the object "+object_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1265() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Delete", "Product","","informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "1266") 
				public void verify_User_ContactsCreateQuick_1266(String activityType, String objectType, String first_name, String last_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User created a new contact with name "+first_name+" "+last_name+" " , true);
					String expectedMsg="User created a new contact with name "+first_name+" "+last_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1266() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Create", "Contact","","","informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "1267") 
				public void verify_User_AccountCreateQuick_1267(String activityType, String objectType, String account_name, String phone, String website, String severity, String logFile) throws Exception{
					Reporter.log("Validate User created a new account with name "+account_name+", Phone: "+phone+", Website: "+website+" " , true);
					String expectedMsg="User created a new account with name "+account_name+", Phone: "+phone+", Website: "+website;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1267() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Create", "Account","","","","informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "1268") 
				public void verify_User_ReportCreate_1268(String activityType, String objectType, String report_name, String report_type, String severity, String logFile) throws Exception{
					Reporter.log("Validate User created a new report "+report_name+" of type "+report_type+" " , true);
					String expectedMsg="User created a new report "+report_name+" of type "+report_type;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1268() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Create", "Report","","","informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "1269") 
				public void verify_User_ReportSave1_1269(String activityType, String objectType, String report_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User saved a report named "+report_name+" " , true);
					String expectedMsg="User saved a report named "+report_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1269() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Save", "Report","","informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "1270") 
				public void verify_User_ReportSave2_1270(String activityType, String objectType, String report_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User saved a report named "+report_name+" " , true);
					String expectedMsg="User saved a report named "+report_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1270() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Save", "Report","","informational", ""},
						
					};
				}
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "1271") 
				public void verify_User_RunReport_1271(String activityType, String objectType, String report_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User run a report named "+report_name+" " , true);
					String expectedMsg="User run a report named "+report_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1271() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Run", "Report","","informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "1272") 
				public void verify_User_ChatterFile_1272(String activityType, String objectType, String file_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User uploaded file named "+file_name+" " , true);
					String expectedMsg="User uploaded file named "+file_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1272() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Upload", "File","","informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "1273") 
				public void verify_User_ChatterFile_1273(String activityType, String objectType, String file_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User shared file named "+file_name+" " , true);
					String expectedMsg="User uploaded file named "+file_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1273() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Share", "File","","informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "1274") 
				public void verify_User_DeleteReport_1274(String activityType, String objectType, String report_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User deleted a report named "+report_name+" " , true);
					String expectedMsg="User deleted a report named "+report_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1274() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Delete", "Report","","informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "1275") 
				public void verify_User_ReportEdit2_1275(String activityType, String objectType, String unsaved, String severity, String logFile) throws Exception{
					Reporter.log("Validate User edited an "+unsaved+" report " , true);
					String expectedMsg="User edited an "+unsaved+" report ";
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1275() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Edit", "Report","","informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST1","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_1295") 
				public void verify_User_TaskCreateFromHomePage_1295(String activityType, String objectType, String subject, String assignee, String severity, String logFile) throws Exception{
					Reporter.log("Validate User created task with name "+subject+", assigned to "+assignee+" " , true);
					String expectedMsg="User created task with name "+subject+", assigned to "+assignee;
					replayLogsEPDV3(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _1295() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Create", "Task","","","informational", "Task,Create1.log"},
						
					};
				}
				
				
				
}
