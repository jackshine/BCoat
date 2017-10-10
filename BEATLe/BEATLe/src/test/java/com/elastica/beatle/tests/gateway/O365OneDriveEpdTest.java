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

public class O365OneDriveEpdTest extends CommonConfiguration {

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
		public void verify_User_SessionLogin(String activityType, String objectType,  String severity, String logFile) throws Exception{
			Reporter.log("Validate User login", true);
			String expectedMsg="User logged in";
			replayLogsEPDV3(logFile);
			data.put("message", expectedMsg);
			data.put("account_type", "Internal");
			assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
			
		}
		
		@DataProvider
		public Object[][] _sessionLogin() {
			return new Object[][]{
				//Activity type  Object Type  Object Name  Severity  message
				{ "Login", "Session", "informational", "OneDrive,Login_Session.log"},
				
			};
		}
		
		
		 @Test(groups ={"TEST"},dataProvider = "_sessionLogout")
			public void verify_User_SessionLogout(String activityType, String objectType,  String severity, String logFile) throws Exception{
				Reporter.log("Validate User logged out", true);
				String expectedMsg="User logged out";
				replayLogsEPDV3(logFile);
				data.put("message", expectedMsg);
				data.put("account_type", "Internal");
				assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
				
			}
			
			@DataProvider
			public Object[][] _sessionLogout() {
				return new Object[][]{
					//Activity type  Object Type  Object Name  Severity  message
					{ "Logout", "Session", "informational", "OneDrive,LogOut_Session.log"},
					
				};
			}
	
	
	 @Test(groups ={"TEST"},dataProvider = "_sessionInvalidLogin")
		public void verify_User_SessionInvalidLogin(String activityType, String objectType,  String severity, String logFile) throws Exception{
			Reporter.log("Validate User login Failed!", true);
			String expectedMsg="User Login Failed!";
			replayLogsEPDV3(logFile);
			data.put("message", expectedMsg);
			data.put("account_type", "Internal");
			assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
			
		}
		
		@DataProvider
		public Object[][] _sessionInvalidLogin() {
			return new Object[][]{
				//Activity type  Object Type  Object Name  Severity  message
				{ "InvalidLogin", "Session", "informational", "OneDrive,Invalid_Login_Session.log"},
				
			};
		}
	
	 @Test(groups ={"TEST"},dataProvider = "_676")
	public void verify_User_copyFiletoFolder(String activityType, String objectType, String object_name, String AFolder, String severity, String logFile) throws Exception{
		Reporter.log("Validate User browsed Home at Microsoft Office 365 Portal ", true);
		String expectedMsg="User copied item "+object_name+" to "+AFolder;
		replayLogs(logFile);
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
	
	
	

	 @Test(groups ={"TEST"},dataProvider = "_677")
	public void verify_User_createNewFolder(String activityType, String objectType, String AFolder, String severity, String logFile) throws Exception{
		Reporter.log("Validate User browsed Home at Microsoft Office 365 Portal ", true);
		String expectedMsg="User created a new folder "+AFolder;
		replayLogs(logFile);
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
	
	

	 @Test(groups ={"TEST"},dataProvider = "_678")
	public void verify_User_moveFileToFolder(String activityType, String objectType, String object_name, String AFolder, String severity, String logFile) throws Exception{
		Reporter.log("Validate User browsed Home at Microsoft Office 365 Portal ", true);
		String expectedMsg="User moved item "+object_name+" to "+AFolder;
		replayLogs(logFile);
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
	
	
	// @Test(groups ={"TEST1"},dataProvider = "_679")
	public void verify_User_renameFile(String activityType, String objectType, String object_name, String severity, String logFile) throws Exception{
		Reporter.log("Validate User browsed Home at Microsoft Office 365 Portal ", true);
		String expectedMsg="User is editing properties for "+object_name;
		replayLogs(logFile);
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
		
	}
	
	@DataProvider
	public Object[][] _679() {
		return new Object[][]{
			//Activity type  Object Type  Object Name  Severity  message
			{ "Edit Properties", "Document","Test1.txt","informational", "O365,AFolder,move_Test_txt.log"},
			
		};
		
	}
		
		
	
	 @Test(groups ={"TEST"},dataProvider = "_680")
		public void verify_deleteItem(String activityType, String objectType, String object_name, String severity, String logFile) throws Exception{
			Reporter.log("Validate User browsed Home at Microsoft Office 365 Portal ", true);
			String expectedMsg="[ERROR] The following activity failed:  User sent "+object_name+" to the Recycle Bin";
			replayLogsEPDV3(logFile);
			data.put("message", expectedMsg);
			data.put("account_type", "Internal");
			assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
			
		}
		
		@DataProvider
		public Object[][] _680() {
			return new Object[][]{
				//Activity type  Object Type  Object Name  Severity  message
				{ "Delete", "Document/Folder","upload.pdf","informational", "OneDrive,DeleteItem.log"},
				
			};
			
		}
		
		
		
	
		//@Test(groups ={"TEST1"},dataProvider = "_681")
		public void verify_login(String activityType, String objectType, String file_name, String severity, String logFile) throws Exception{
			Reporter.log("Validate User browsed Home at Microsoft Office 365 Portal ", true);
			String expectedMsg="";
			replayLogsDebug(logFile);
			data.put("message", expectedMsg);
			data.put("account_type", "Internal");
			assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
			
		}
		
		@DataProvider
		public Object[][] _681() {
			return new Object[][]{
				//Activity type  Object Type  Object Name  Severity  message
				{ "Logout", "Session","informational", "O365,login.log"},
				
			};
			
		}
		
		
		

		@Test(groups ={"TEST"},dataProvider = "_682")
		public void verify_viewOneDrive(String activityType, String objectType, String severity, String logFile) throws Exception{
			Reporter.log("Validate User browsed Home at Microsoft Office 365 Portal ", true);
			String expectedMsg="User viewed home page";
			replayLogs(logFile);
			data.put("message", expectedMsg);
			data.put("account_type", "Internal");
			assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
			
		}
		
		@DataProvider
		public Object[][] _682() {
			return new Object[][]{
				//Activity type  Object Type  Object Name  Severity  message
				{ "View", "HomePage", "informational", "O365,OneDrive,viewOneDrive.log"},
				
			};
			
		}
		
		

		@Test(groups ={"TEST"},dataProvider = "_683")
		public void verify_userDownloadPDF(String activityType, String objectType, String object_name, String severity, String logFile) throws Exception{
			Reporter.log("Validate User browsed Home at Microsoft Office 365 Portal ", true);
			String expectedMsg="User downloaded "+object_name;
			replayLogs(logFile);
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
		
		
		
		

		@Test(groups ={"TEST"},dataProvider = "_684")
		public void verify_userDownloadXls(String activityType, String objectType, String object_name, String severity, String logFile) throws Exception{
			Reporter.log("Validate User browsed Home at Microsoft Office 365 Portal ", true);
			String expectedMsg="User downloaded "+object_name;
			replayLogs(logFile);
			data.put("message", expectedMsg);
			data.put("account_type", "Internal");
			assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
			
		}
		
		@DataProvider
		public Object[][] _684() {
			return new Object[][]{
				//Activity type  Object Type  Object Name  Severity  message
				{ "Download", "Document","Test.xls","informational", "O365,Test,download_txt.log"},
				
			};
			
		}
		
		
		
		@Test(groups ={"TEST"},dataProvider = "_685")
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
		
		@Test(groups ={"TEST"},dataProvider = "_685P")
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
		
		
		@Test(groups ={"TEST"},dataProvider = "_686")
		public void verify_UserShareTxtFile(String activityType, String objectType, String email_name, String object_name, String severity, String logFile) throws Exception{
			Reporter.log("Validate User browsed Home at Microsoft Office 365 Portal ", true);
			String expectedMsg="User sent email invitation(s) to "+email_name+"  for "+object_name;
			replayLogs(logFile);
			data.put("message", expectedMsg);
			data.put("account_type", "Internal");
			assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
			
		}
		
		@DataProvider
		public Object[][] _686() {
			return new Object[][]{
				//Activity type  Object Type  Object Name  Severity  message
				{ "Share", "Document/Folder","mohammad.usman@elastica.co","Test.txt.","informational", "O365,Test,share_txt.log"},
				
			};
			
		}
		
		
		//@Test(groups ={"TEST1"},dataProvider = "_687")
		public void verify_UserShare_via_link(String activityType, String objectType, String severity, String logFile) throws Exception{
			Reporter.log("Validate User browsed Home at Microsoft Office 365 Portal ", true);
			String expectedMsg="";
			replayLogsDebug(logFile);
			data.put("message", expectedMsg);
			data.put("account_type", "Internal");
			assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
			
		}
		
		@DataProvider
		public Object[][] _687() {
			return new Object[][]{
				//Activity type  Object Type  Object Name  Severity  message
				{ "Share", "Document/Folder","informational", "O365,Test,share_via_link_txt.log"},
				
			};
			
		}
		
		//@Test(groups ={"TEST"},dataProvider = "_688")
		public void verify_UserUploadDocFile(String activityType, String objectType, String file_name, String severity, String logFile) throws Exception{
			Reporter.log("Validate User browsed Home at Microsoft Office 365 Portal ", true);
			String expectedMsg="User browsed Home at Microsoft Office 365 Portal ";
			replayLogsDebug(logFile);
			data.put("message", expectedMsg);
			data.put("account_type", "Internal");
			assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
			
		}
		
		@DataProvider
		public Object[][] _688() {
			return new Object[][]{
				//Activity type  Object Type  Object Name  Severity  message
				{ "Logout", "Session","informational", "O365,Test,upload_doc.log"},
				
			};
			
		}
		
	//	@Test(groups ={"TEST"},dataProvider = "_689")
		public void verify_UserUploadDocxFile(String activityType, String objectType, String file_name, String severity, String logFile) throws Exception{
			Reporter.log("Validate User browsed Home at Microsoft Office 365 Portal ", true);
			String expectedMsg="User browsed Home at Microsoft Office 365 Portal ";
			replayLogsDebug(logFile);
			data.put("message", expectedMsg);
			data.put("account_type", "Internal");
			assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
			
		}
		
		@DataProvider
		public Object[][] _689() {
			return new Object[][]{
				//Activity type  Object Type  Object Name  Severity  message
				{ "Logout", "Session","informational", "O365,Test,upload_docx.log"},
				
			};
			
		}
		
	//	@Test(groups ={"TEST"},dataProvider = "_690")
		public void verify_UserUploadPdfFile(String activityType, String objectType, String file_name, String severity, String logFile) throws Exception{
			Reporter.log("Validate User browsed Home at Microsoft Office 365 Portal ", true);
			String expectedMsg="User browsed Home at Microsoft Office 365 Portal ";
			replayLogsDebug(logFile);
			data.put("message", expectedMsg);
			data.put("account_type", "Internal");
			assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
			
		}
		
		@DataProvider
		public Object[][] _690() {
			return new Object[][]{
				//Activity type  Object Type  Object Name  Severity  message
				{ "Logout", "Session","informational", "O365,Test,upload_pdf.log"},
				
			};
			
		}
		
		
	//	@Test(groups ={"TEST"},dataProvider = "_691")
		public void verify_UserUploadTxtFile(String activityType, String objectType, String file_name, String severity, String logFile) throws Exception{
			Reporter.log("Validate User browsed Home at Microsoft Office 365 Portal ", true);
			String expectedMsg="User browsed Home at Microsoft Office 365 Portal ";
			replayLogsDebug(logFile);
			data.put("message", expectedMsg);
			data.put("account_type", "Internal");
			assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
			
		}
		
		@DataProvider
		public Object[][] _691() {
			return new Object[][]{
				//Activity type  Object Type  Object Name  Severity  message
				{ "Logout", "Session","informational", "O365,Test,upload_txt.log"},
				
			};
			
		}
		
		
	//	@Test(groups ={"TEST"},dataProvider = "_692")
		public void verify_UserUploadXlsFile(String activityType, String objectType, String file_name, String severity, String logFile) throws Exception{
			Reporter.log("Validate User browsed Home at Microsoft Office 365 Portal ", true);
			String expectedMsg="User browsed Home at Microsoft Office 365 Portal ";
			replayLogsDebug(logFile);
			data.put("message", expectedMsg);
			data.put("account_type", "Internal");
			assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
			
		}
		
		@DataProvider
		public Object[][] _692() {
			return new Object[][]{
				//Activity type  Object Type  Object Name  Severity  message
				{ "Logout", "Session","informational", "O365,Test,upload_xls.log"},
				
			};
		}
		
		@Test(groups ={"TEST"},dataProvider = "_693")
			public void verify_UserUploadFiles(String activityType, String objectType, String file_name, String severity, String logFile) throws Exception{
				Reporter.log("User uploaded file named file_name", true);
				String expectedMsg="[ERROR] The following activity failed:  User uploaded file named "+file_name;
				replayLogsEPDV3(logFile);
				data.put("message", expectedMsg);
				data.put("account_type", "Internal");
				assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
				
			}
			
			@DataProvider
			public Object[][] _693() {
				return new Object[][]{
					//Activity type  Object Type  Object Name  Severity  message
					{ "Upload", "File","Test.7z", "informational", "Folder,Upload_file_test_7z.log"},
//					{ "Upload", "File","test.avi", "testuser1@gatewayo365beatle.com.", "informational", "Folder,Upload_file_test_avi.log"},
//					{ "Upload", "File","test.bin", "testuser1@gatewayo365beatle.com.", "informational", "Folder,Upload_file_test_bin.log"},
//					{ "Upload", "File","test.bmp", "testuser1@gatewayo365beatle.com.", "informational", "Folder,Upload_file_test_bmp.log"},
//					{ "Upload", "File","test.bz2", "testuser1@gatewayo365beatle.com.", "informational", "Folder,Upload_file_test_bz2.log"},
//					{ "Upload", "File","test.cs", "testuser1@gatewayo365beatle.com.", "informational",  "Folder,Upload_file_test_cs.log"},
//					{ "Upload", "File","test.csv", "testuser1@gatewayo365beatle.com.", "informational", "Folder,Upload_file_test_csv.log"},
//					{ "Upload", "File","test.dmg", "testuser1@gatewayo365beatle.com.", "informational", "Folder,Upload_file_test_dmg.log"},
//					{ "Upload", "File","test.doc", "testuser1@gatewayo365beatle.com.", "informational", "Folder,Upload_file_test_doc.log"},
//					{ "Upload", "File","test.docx", "testuser1@gatewayo365beatle.com.", "informational", "Folder,Upload_file_test_docx.log"},
//					{ "Upload", "File","test.exe", "testuser1@gatewayo365beatle.com.", "informational", "Folder,Upload_file_test_exe.log"},
//					{ "Upload", "File","test.flac", "testuser1@gatewayo365beatle.com.", "informational", "Folder,Upload_file_test_flac.log"},
//					{ "Upload", "File","test.flv", "testuser1@gatewayo365beatle.com.", "informational", "Folder,Upload_file_test_flv.log"},
//					{ "Upload", "File","test.gif", "testuser1@gatewayo365beatle.com.", "informational", "Folder,Upload_file_test_gif.log"},
//					{ "Upload", "File","test.gz", "testuser1@gatewayo365beatle.com.", "informational", "Folder,Upload_file_test_gz.log"},
//					{ "Upload", "File","test.html", "testuser1@gatewayo365beatle.com.", "informational", "Folder,Upload_file_test_html.log"},
//					{ "Upload", "File","test.java", "testuser1@gatewayo365beatle.com.", "informational", "Folder,Upload_file_test_java.log"},
//					{ "Upload", "File","test.jpeg", "testuser1@gatewayo365beatle.com.", "informational", "Folder,Upload_file_test_jpeg.log"},
//					{ "Upload", "File","test.jpg", "testuser1@gatewayo365beatle.com.", "informational", "Folder,Upload_file_test_jpg.log"},
//					{ "Upload", "File","test.js",  "testuser1@gatewayo365beatle.com.", "informational", "Folder,Upload_file_test_js.log"},
//					{ "Upload", "File","test.json", "testuser1@gatewayo365beatle.com.", "informational", "Folder,Upload_file_test_json.log"},
//					{ "Upload", "File","test.key", "testuser1@gatewayo365beatle.com.", "informational", "Folder,Upload_file_test_key.log"},
//					{ "Upload", "File","test.mov", "testuser1@gatewayo365beatle.com.", "informational", "Folder,Upload_file_test_mov.log"},
//					{ "Upload", "File","test.mp3", "testuser1@gatewayo365beatle.com.", "informational", "Folder,Upload_file_test_mp3.log"},
//					{ "Upload", "File","test.mp4", "testuser1@gatewayo365beatle.com.", "informational", "Folder,Upload_file_test_mp4.log"},
//					{ "Upload", "File","test.mpg", "testuser1@gatewayo365beatle.com.", "informational", "Folder,Upload_file_test_mpg.log"},
//					{ "Upload", "File","test.numbers", "testuser1@gatewayo365beatle.com.", "informational", "Folder,Upload_file_test_numbers.log"},
//					{ "Upload", "File","test.odg", "testuser1@gatewayo365beatle.com.", "informational", "Folder,Upload_file_test_odg.log"},
//					{ "Upload", "File","test.odp", "testuser1@gatewayo365beatle.com.", "informational", "Folder,Upload_file_test_odp.log"},
//					{ "Upload", "File","test.ods", "testuser1@gatewayo365beatle.com.", "informational", "Folder,Upload_file_test_ods.log"},
//					{ "Upload", "File","test.odt", "testuser1@gatewayo365beatle.com.", "informational", "Folder,Upload_file_test_odt.log"},
//					{ "Upload", "File","test.pdf", "testuser1@gatewayo365beatle.com.", "informational", "Folder,Upload_file_test_pdf.log"},
//					{ "Upload", "File","test.pem", "testuser1@gatewayo365beatle.com.", "informational", "Folder,Upload_file_test_pem.log"},
//					{ "Upload", "File","Test.ppt", "testuser1@gatewayo365beatle.com.", "informational", "Folder,Upload_file_test_ppt.log"},
//					{ "Upload", "File","test.xls", "testuser1@gatewayo365beatle.com.", "informational", "Folder,Upload_file_test_xml.log"},
//					{ "Upload", "File","test.zip","testuser1@gatewayo365beatle.com.", "informational", "Folder,Upload_file_test_zip.log"},
					
				};
			}
			
			
			@Test(groups ={"TEST"},dataProvider = "_694")
			public void verify_UserDownloadFiles(String activityType, String objectType, String file_name, String severity, String logFile) throws Exception{
				Reporter.log("Validate User downloaded files ", true);
				String expectedMsg="User downloaded "+file_name;
				replayLogsEPDV3(logFile);
				data.put("message", expectedMsg);
				data.put("account_type", "Internal");
				assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
				
			}
			
			@DataProvider
			public Object[][] _694() {
				return new Object[][]{
					//Activity type  Object Type  Object Name  Severity  message
					{ "Download", "File", "Test.7z", "informational", "Folder,Download_file_test_7z.log"},
					{ "Download", "File", "Test.avi", "informational", "Folder,Download_file_test_avi.log"},
					{ "Download", "File", "Test.doc", "informational", "Folder,Download_file_test_doc.log"},
					{ "Download", "File", "Test.docx", "informational", "Folder,Download_file_test_docx.log"},
					{ "Download", "File", "Test.exe", "informational", "Folder,Download_file_test_exe.log"},
					{ "Download", "File", "Test.java", "informational", "Folder,Download_file_test_java.log"},
					{ "Download", "File", "Test.json", "informational", "Folder,Download_file_test_json.log"},
					{ "Download", "File", "Test.pdf", "informational", "Folder,Download_file_test_pdf.log"},
					{ "Download", "File", "Test.txt", "informational", "Folder,Download_file_test_txt.log"},
					
				};
			}
			
			
			@Test(groups ={"TEST"},dataProvider = "_695")
			public void verify_UserDeleteFiles(String activityType, String objectType, String file_name, String severity, String logFile) throws Exception{
				Reporter.log("Validate User deleted files", true);
				String expectedMsg="User sent "+file_name+" to the Recycle Bin";
				replayLogsEPDV3(logFile);
				data.put("message", expectedMsg);
				data.put("account_type", "Internal");
				assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
				
			}
			
			@DataProvider
			public Object[][] _695() {
				return new Object[][]{
					//Activity type  Object Type  Object Name  Severity  message
					{ "Delete", "File", "Test.flv", "informational", "FileOne,Delete_file.log"},
					{ "Delete", "File", "Test.csv", "informational", "FileOne,Delete_test_csv.log"},
					
				};
			}
			
			
			@Test(groups ={"TEST"},dataProvider = "_696")
			public void verify_UserCopyDoc(String activityType, String objectType, String file_name, String FolderName, String severity, String logFile) throws Exception{
				Reporter.log("Validate User copied item", true);
				String expectedMsg="User copied item "+file_name+" to "+FolderName;
				replayLogsEPDV3(logFile);
				data.put("message", expectedMsg);
				data.put("account_type", "Internal");
				assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
				
			}
			
			@DataProvider
			public Object[][] _696() {
				return new Object[][]{
					//Activity type  Object Type  Object Name  Severity  message
					{ "Copy", "Document/Folder", "GWAuto.docx", "AFolder", "informational", "DocumentOneD,Copy_doc.log"},
					
				};
			}
			
			
			@Test(groups ={"TEST"},dataProvider = "_697")
			public void verify_UserDeleteDoc(String activityType, String objectType, String file_name, String severity, String logFile) throws Exception{
				Reporter.log("Validate User sent file_name to the recycle Bin", true);
				String expectedMsg="User sent "+file_name+" to the Recycle Bin";
				replayLogsEPDV3(logFile);
				data.put("message", expectedMsg);
				data.put("account_type", "Internal");
				assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
				
			}
			
			@DataProvider
			public Object[][] _697() {
				return new Object[][]{
					//Activity type  Object Type  Object Name  Severity  message
					{ "Delete", "Document", "GWAutomation.docx", "informational", "DocumentOneD,Delete_doc.log"},
					
				};
			}
			
			
			@Test(groups ={"TEST"},dataProvider = "_698")
			public void verify_UserDownloadDoc(String activityType, String objectType, String file_name, String severity, String logFile) throws Exception{
				Reporter.log("Validate User downloaded file_name", true);
				String expectedMsg="User downloaded "+file_name;
				replayLogsEPDV3(logFile);
				data.put("message", expectedMsg);
				data.put("account_type", "Internal");
				assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
				
			}
			
			@DataProvider
			public Object[][] _698() {
				return new Object[][]{
					//Activity type  Object Type  Object Name  Severity  message
					{ "Download", "Document", "GWAutomation.docx", "informational", "DocumentOneD,Download_doc.log"},
					
				};
			}
			
			
			@Test(groups ={"TEST"},dataProvider = "_699")
			public void verify_UserMoveDoc(String activityType, String objectType, String file_name, String FolderName, String severity, String logFile) throws Exception{
				Reporter.log("Validate User moved item file_name to root folder", true);
				String expectedMsg="User moved item "+file_name+" to "+FolderName;
				replayLogsEPDV3(logFile);
				data.put("message", expectedMsg);
				data.put("account_type", "Internal");
				assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
				
			}
			
			@DataProvider
			public Object[][] _699() {
				return new Object[][]{
					//Activity type  Object Type  Object Name  Severity  message
					{ "Move", "Document/Folder", "GWAutomation.docx","AFolder", "informational", "DocumentOneD,Move_doc.log"},
					
				};
			}
			
			
			@Test(groups ={"TEST"},dataProvider = "_700")
			public void verify_UserOpenDoc(String activityType, String objectType, String doc_name, String file_name, String severity, String logFile) throws Exception{
				Reporter.log("Validate User moved item file_name to root folder", true);
				String expectedMsg="User viewed a "+doc_name+" named "+file_name+".";
				replayLogsEPDV3(logFile);
				data.put("message", expectedMsg);
				data.put("account_type", "Internal");
				assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
				
			}
			
			@DataProvider
			public Object[][] _700() {
				return new Object[][]{
					//Activity type  Object Type  Object Name  Severity  message
					{ "View", "Document", "Word Document", "GWAutomation.docx", "informational", "DocumentOneD,Open_doc.log"},
					
				};
			}
			
			//@Test(groups ={"TEST1"},dataProvider = "_701")
			public void verify_UserRenameDoc(String activityType, String objectType, String file_name, String severity, String logFile) throws Exception{
				Reporter.log("Validate User moved item file_name to root folder", true);
				String expectedMsg="User moved item "+file_name+" to root folder";
				replayLogsEPDV3(logFile);
				data.put("message", expectedMsg);
				data.put("account_type", "Internal");
				assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
				
			}
			
			@DataProvider
			public Object[][] _701() {
				return new Object[][]{
					//Activity type  Object Type  Object Name  Severity  message
					{ "Rename", "Document", "GWAutomation.docx", "informational", "DocumentOneD,Rename_doc.log"},
					
				};
			}
			
			
			@Test(groups ={"TEST"},dataProvider = "_702")
			public void verify_UserShareDoc(String activityType, String objectType, String email, String file_name, String severity, String logFile) throws Exception{
				Reporter.log("Validate User sent email invitation(s) to email  for file_name", true);
				String expectedMsg="User sent email invitation(s) to "+email+" for "+file_name;
				replayLogsEPDV3(logFile);
				data.put("message", expectedMsg);
				data.put("account_type", "Internal");
				assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
				
			}
			
			@DataProvider
			public Object[][] _702() {
				return new Object[][]{
					//Activity type  Object Type  Object Name  Severity  message
					{ "Share", "Document/Folder", "mohd.afjal@elastica.co", "Dial-FAQ.pdf.", "informational", "DocumentOneD,Share_doc.log"},
					
				};
			}
			
			
			//@Test(groups ={"TEST"},dataProvider = "_703")
			public void verify_UserUploadDoc(String activityType, String objectType, String file_name, String severity, String logFile) throws Exception{
				Reporter.log("Validate User uploaded file named file_name", true);
				String expectedMsg="User uploaded file named "+file_name;
				replayLogsEPDV3(logFile);
				data.put("message", expectedMsg);
				data.put("account_type", "Internal");
				assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
				
			}
			
			@DataProvider
			public Object[][] _703() {
				return new Object[][]{
					//Activity type  Object Type  Object Name  Severity  message
					{ "Upload", "Document", "GWAutomation.docx", "informational", "DocumentOneD,Upload_docx.log"},
					{ "Upload", "Document", "GWAutomation.docx", "informational", "DocumentOneD,Upload_doc.log"},
					
				};
			}
			
			
			@Test(groups ={"TEST"},dataProvider = "_704")
			public void verify_UserCopyExcelWorkBook(String activityType, String objectType, String file_name, String FolderName, String severity, String logFile) throws Exception{
				Reporter.log("Validate User copied item "+file_name+" to root folder", true);
				String expectedMsg="User copied item "+file_name+" to "+FolderName;
				replayLogsEPDV3(logFile);
				data.put("message", expectedMsg);
				data.put("account_type", "Internal");
				assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
				
			}
			
			@DataProvider
			public Object[][] _704() {
				return new Object[][]{
					//Activity type  Object Type  Object Name  Severity  message
					{ "Copy", "Document/Folder", "ExcelBook.xlsx","GFolder", "informational", "ExcelWorkBook,Copy_Excel.log"},
					
				};
			}
			
			
			@Test(groups ={"TEST"},dataProvider = "_705")
			public void verify_UserDeleteExcelWorkBook(String activityType, String objectType, String file_name, String severity, String logFile) throws Exception{
				Reporter.log("Validate User sent "+file_name+" to the Recycle Bin", true);
				String expectedMsg="User sent "+file_name+" to the Recycle Bin";
				replayLogsEPDV3(logFile);
				data.put("message", expectedMsg);
				data.put("account_type", "Internal");
				assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
				
			}
			
			@DataProvider
			public Object[][] _705() {
				return new Object[][]{
					//Activity type  Object Type  Object Name  Severity  message
					{ "Delete", "Document", "ExcelBooksheet.xlsx", "informational", "ExcelWorkBook,Delete_Excel.log"},
					
				};
			}
			
			
			@Test(groups ={"TEST"},dataProvider = "_706")
			public void verify_UserDownloadExcelWorkBook(String activityType, String objectType, String file_name, String severity, String logFile) throws Exception{
				Reporter.log("Validate User downloaded file_name", true);
				String expectedMsg="User downloaded "+file_name;
				replayLogsEPDV3(logFile);
				data.put("message", expectedMsg);
				data.put("account_type", "Internal");
				assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
				
			}
			
			@DataProvider
			public Object[][] _706() {
				return new Object[][]{
					//Activity type  Object Type  Object Name  Severity  message
					{ "Download", "Document", "ExcelBook.xlsx", "informational", "ExcelWorkBook,Download_Excel.log"},
					
				};
			}
			
			
			//@Test(groups ={"TEST1"},dataProvider = "_707")
			public void verify_UserEditExcelWorkBook(String activityType, String objectType, String file_name, String severity, String logFile) throws Exception{
				Reporter.log("Validate User downloaded file_name", true);
				String expectedMsg="User downloaded "+file_name;
				replayLogsEPDV3(logFile);
				data.put("message", expectedMsg);
				data.put("account_type", "Internal");
				assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
				
			}
			
			@DataProvider
			public Object[][] _707() {
				return new Object[][]{
					//Activity type  Object Type  Object Name  Severity  message
					{ "Edit", "Document", "ExcelBook.xlsx", "informational", "ExcelWorkBook,Edit_In_Excel_Online.log"},
					
				};
			}
			
			
			@Test(groups ={"TEST"},dataProvider = "_708")
			public void verify_UserMoveExcelWorkBook(String activityType, String objectType, String file_name, String FolderName, String severity, String logFile) throws Exception{
				Reporter.log("Validate User moved item file_name to root folder", true);
				String expectedMsg="User moved item "+file_name+" to "+FolderName;
				replayLogsEPDV3(logFile);
				data.put("message", expectedMsg);
				data.put("account_type", "Internal");
				assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
				
			}
			
			@DataProvider
			public Object[][] _708() {
				return new Object[][]{
					//Activity type  Object Type  Object Name  Severity  message
					{ "Move", "Document/Folder", "ExcelBooksheet.xlsx", "AFolder", "informational", "ExcelWorkBook,Move_Excel.log"},
					
				};
			}
			
			
			@Test(groups ={"TEST"},dataProvider = "_709")
			public void verify_UserOpenOnlineExcelWorkBook(String activityType, String objectType, String doc_name, String file_name, String severity, String logFile) throws Exception{
				Reporter.log("Validate User viewed a "+doc_name+" named file_name", true);
				String expectedMsg="User viewed a "+doc_name+" named "+file_name+".";
				replayLogsEPDV3(logFile);
				data.put("message", expectedMsg);
				data.put("account_type", "Internal");
				assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
				
			}
			
			@DataProvider
			public Object[][] _709() {
				return new Object[][]{
					//Activity type  Object Type  Object Name  Severity  message
					{ "View", "Document", "Excel Workbook", "ExcelBooksheet.xlsx", "informational", "ExcelWorkBook,Open_In_Excel_Online.log"},
					
				};
			}
			
			
			//@Test(groups ={"TEST"},dataProvider = "_710")
			public void verify_UserOpenExcelWorkBook(String activityType, String objectType, String doc_name, String file_name, String severity, String logFile) throws Exception{
				Reporter.log("Validate User viewed a "+doc_name+" named file_name", true);
				String expectedMsg="User viewed a "+doc_name+" named "+file_name+".";
				replayLogsEPDV3(logFile);
				data.put("message", expectedMsg);
				data.put("account_type", "Internal");
				assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
				
			}
			
			@DataProvider
			public Object[][] _710() {
				return new Object[][]{
					//Activity type  Object Type  Object Name  Severity  message
					{ "View", "Document", "Excel Workbook", "ExcelBooksheet.xlsx", "informational", "ExcelWorkBook,Open1_In_Excel.log"},
					
				};
			}
			
			
			@Test(groups ={"TEST"},dataProvider = "_711")
			public void verify_UserPrintExcelWorkBook(String activityType, String objectType, String file_name, String severity, String logFile) throws Exception{
				Reporter.log("Validate User printed item file_name", true);
				String expectedMsg="User printed item "+file_name;
				replayLogsEPDV3(logFile);
				data.put("message", expectedMsg);
				data.put("account_type", "Internal");
				assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
				
			}
			
			@DataProvider
			public Object[][] _711() {
				return new Object[][]{
					//Activity type  Object Type  Object Name  Severity  message
					{ "Print", "Document/Folder", "ExcelBooksheet.xlsx", "informational", "ExcelWorkBook,Print_Excel.log"},
					
				};
			}
			
			
			
			@Test(groups ={"TEST"},dataProvider = "_712")
			public void verify_UserRenameExcelWorkBook(String activityType, String objectType, String file_name, String rename_name, String severity, String logFile) throws Exception{
				Reporter.log("Validate User renamed item", true);
				String expectedMsg="User renamed item from "+file_name+" to "+rename_name;
				replayLogsEPDV3(logFile);
				data.put("message", expectedMsg);
				data.put("account_type", "Internal");
				assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
				
			}
			
			@DataProvider
			public Object[][] _712() {
				return new Object[][]{
					//Activity type  Object Type  Object Name  Severity  message
					{ "Rename", "Document/Folder", "Gambling.txt", "ExcelBooksheet.xlsx", "informational", "ExcelWorkBook,Rename_Excel.log"},
					
				};
			}
			
			
			@Test(groups ={"TEST"},dataProvider = "_713")
			public void verify_UserShareExcelWorkBook(String activityType, String objectType, String email, String file_name, String severity, String logFile) throws Exception{
				Reporter.log("Validate User sent email invitation(s) to "+email+"  for file_name", true);
				String expectedMsg="[ERROR] The following activity failed:  User sent email invitation(s) to "+email+" for "+file_name;
				replayLogsEPDV3(logFile);
				data.put("message", expectedMsg);
				data.put("account_type", "Internal");
				assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
				
			}
			
			@DataProvider
			public Object[][] _713() {
				return new Object[][]{
					//Activity type  Object Type  Object Name  Severity  message
					{ "Share", "Document", "mohd.afjal@elastica.co", "source_code.xls.", "informational", "OneDrive,Share_ExcelSheet_xls.log"},
					
				};
			}
			
			
			@Test(groups ={"TEST"},dataProvider = "_714")
			public void verify_UserCopyOneNote(String activityType, String objectType, String file_name, String FolderName, String severity, String logFile) throws Exception{
				Reporter.log("Validate User copied item "+file_name+" to root folder", true);
				String expectedMsg="User copied item "+file_name+" to "+FolderName;
				replayLogsEPDV3(logFile);
				data.put("message", expectedMsg);
				data.put("account_type", "Internal");
				assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
				
			}
			
			@DataProvider
			public Object[][] _714() {
				return new Object[][]{
					//Activity type  Object Type  Object Name  Severity  message
					{ "Copy", "Document/Folder", "GWAutoNoteBook","GFolder", "informational", "OneNote,Copy_OneNote.log"},
					
				};
			}
			
			
			//@Test(groups ={"TEST"},dataProvider = "_715")
			public void verify_UserCreateOneNote(String activityType, String objectType, String file_name, String severity, String logFile) throws Exception{
				Reporter.log("Validate User copied item "+file_name+" to root folder", true);
				String expectedMsg="User copied item "+file_name+" to root folder";
				replayLogsEPDV3(logFile);
				data.put("message", expectedMsg);
				data.put("account_type", "Internal");
				assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
				
			}
			
			@DataProvider
			public Object[][] _715() {
				return new Object[][]{
					//Activity type  Object Type  Object Name  Severity  message
					{ "Create", "Document/Folder", "GWNoteBook", "informational", "OneNote,Create_OneNote.log"},
					
				};
			}
			
			
			@Test(groups ={"TEST"},dataProvider = "_716")
			public void verify_UserDeleteOneNote(String activityType, String objectType, String file_name, String severity, String logFile) throws Exception{
				Reporter.log("Validate User sent "+file_name+" to the Recycle Bin", true);
				String expectedMsg="User sent "+file_name+" to the Recycle Bin";
				replayLogsEPDV3(logFile);
				data.put("message", expectedMsg);
				data.put("account_type", "Internal");
				assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
				
			}
			
			@DataProvider
			public Object[][] _716() {
				return new Object[][]{
					//Activity type  Object Type  Object Name  Severity  message
					{ "Delete", "Document", "", "informational", "OneNote,Delete_OneNote.log"},
					
				};
			}
			
			
			@Test(groups ={"TEST"},dataProvider = "_717")
			public void verify_UserMoveOneNote(String activityType, String objectType, String file_name, String FolderName, String severity, String logFile) throws Exception{
				Reporter.log("Validate User moved item file_name to root folder", true);
				String expectedMsg="User moved item "+file_name+" to "+FolderName;
				replayLogsEPDV3(logFile);
				data.put("message", expectedMsg);
				data.put("account_type", "Internal");
				assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
				
			}
			
			@DataProvider
			public Object[][] _717() {
				return new Object[][]{
					//Activity type  Object Type  Object Name  Severity  message
					{ "Move", "Document/Folder", "GWAutoNoteBook", "AFolder", "informational", "OneNote,Move_OneNote.log"},
					
				};
			}
			
			
			
			//@Test(groups ={"TEST"},dataProvider = "_718")
			public void verify_UserOpenOnlineOneNote(String activityType, String objectType, String doc_name, String file_name, String severity, String logFile) throws Exception{
				Reporter.log("Validate User viewed a "+doc_name+" named file_name", true);
				String expectedMsg="User viewed a "+doc_name+" named "+file_name+".";
				replayLogsEPDV3(logFile);
				data.put("message", expectedMsg);
				data.put("account_type", "Internal");
				assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
				
			}
			
			@DataProvider
			public Object[][] _718() {
				return new Object[][]{
					//Activity type  Object Type  Object Name  Severity  message
					{ "View", "Document", "Excel Workbook", "GWAutoNoteBook", "informational", "OneNote,Open_In_OneNote_Online.log"},
					
				};
			}
			
			
			//@Test(groups ={"TEST"},dataProvider = "_719")
			public void verify_UserOpenOneNote(String activityType, String objectType, String doc_name, String file_name, String severity, String logFile) throws Exception{
				Reporter.log("Validate User viewed a "+doc_name+" named file_name", true);
				String expectedMsg="User viewed a "+doc_name+" named "+file_name+".";
				replayLogsEPDV3(logFile);
				data.put("message", expectedMsg);
				data.put("account_type", "Internal");
				assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
				
			}
			
			@DataProvider
			public Object[][] _719() {
				return new Object[][]{
					//Activity type  Object Type  Object Name  Severity  message
					{ "View", "Document", "Excel Workbook", "GWAutoNoteBook", "informational", "OneNote,Open_In_OneNote.log"},
					
				};
			}
			
			
			//@Test(groups ={"TEST"},dataProvider = "_720")
			public void verify_UserPrintOneNote(String activityType, String objectType, String file_name, String severity, String logFile) throws Exception{
				Reporter.log("Validate User moved item file_name to root folder", true);
				String expectedMsg="User moved item "+file_name+" to root folder";
				replayLogsEPDV3(logFile);
				data.put("message", expectedMsg);
				data.put("account_type", "Internal");
				assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
				
			}
			
			@DataProvider
			public Object[][] _720() {
				return new Object[][]{
					//Activity type  Object Type  Object Name  Severity  message
					{ "Print", "Document/Folder", "GWAutoNoteBook", "informational", "OneNote,Print_OneNote.log"},
					
				};
			}
			
			
			@Test(groups ={"TEST"},dataProvider = "_721")
			public void verify_UserRenameOneNote(String activityType, String objectType, String RenameName, String Org_name, String severity, String logFile) throws Exception{
				Reporter.log("Validate User renamed item from to file_name", true);
				String expectedMsg="User renamed item from "+RenameName+" to "+Org_name;
				replayLogsEPDV3(logFile);
				data.put("message", expectedMsg);
				data.put("account_type", "Internal");
				assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
				
			}
			
			@DataProvider
			public Object[][] _721() {
				return new Object[][]{
					//Activity type  Object Type  Object Name  Severity  message
					{ "Rename", "Document/Folder","FERPA_BaileyDoxed.txt", "GWAutoNoteBook", "informational", "OneNote,Rename_OneNote.log"},
					
				};
			}
			
			
			@Test(groups ={"TEST"},dataProvider = "_722")
			public void verify_UserShareOneNote(String activityType, String objectType, String email, String file_name, String severity, String logFile) throws Exception{
				Reporter.log("Validate User sent email invitation(s) to email  for file_name", true);
				String expectedMsg="User sent email invitation(s) to "+email+"  for "+file_name+".";
				replayLogsEPDV3(logFile);
				data.put("message", expectedMsg);
				data.put("account_type", "Internal");
				assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
				
			}
			
			@DataProvider
			public Object[][] _722() {
				return new Object[][]{
					//Activity type  Object Type  Object Name  Severity  message
					{ "Share", "Document/Folder", "mohd.afjal@elastica.co", "", "informational", "OneNote,Share_OneNote.log"},
					
				};
			}
			
			
			@Test(groups ={"TEST"},dataProvider = "_723")
			public void verify_UserCopyPowerPoint(String activityType, String objectType, String file_name, String FolderName, String severity, String logFile) throws Exception{
				Reporter.log("Validate User copied item "+file_name+" to root folder", true);
				String expectedMsg="User copied item "+file_name+" to "+FolderName;
				replayLogsEPDV3(logFile);
				data.put("message", expectedMsg);
				data.put("account_type", "Internal");
				assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
				
			}
			
			@DataProvider
			public Object[][] _723() {
				return new Object[][]{
					//Activity type  Object Type  Object Name  Severity  message
					{ "Copy", "Document/Folder", "NewFolder/GWPresent.pptx","test", "informational", "PowerPointOne,Copy_PowerPoint.log"},
					
				};
			}
			
			
			@Test(groups ={"TEST"},dataProvider = "_724")
			public void verify_UserEditedPowerPoint(String activityType, String objectType, String file_name, String severity, String logFile) throws Exception{
				Reporter.log("Validate User edited item file_name", true);
				String expectedMsg="User edited "+file_name+" in browser";
				replayLogsEPDV3(logFile);
				data.put("message", expectedMsg);
				data.put("account_type", "Internal");
				assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
				
			}
			
			@DataProvider
			public Object[][] _724() {
				return new Object[][]{
					//Activity type  Object Type  Object Name  Severity  message
					{ "Edit", "Document/Folder", "GWPresent.pptx", "informational", "PowerPointOne,Create_PowerPoint.log"},
					
				};
			}
			
			
			@Test(groups ={"TEST"},dataProvider = "_725")
			public void verify_UserDeletePowerPoint(String activityType, String objectType, String file_name, String severity, String logFile) throws Exception{
				Reporter.log("Validate User sent "+file_name+" to the Recycle Bin", true);
				String expectedMsg="User sent "+file_name+" to the Recycle Bin";
				replayLogsEPDV3(logFile);
				data.put("message", expectedMsg);
				data.put("account_type", "Internal");
				assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
				
			}
			
			@DataProvider
			public Object[][] _725() {
				return new Object[][]{
					//Activity type  Object Type  Object Name  Severity  message
					{ "Delete", "Document", "GWPresent.pptx", "informational", "PowerPointOne,Delete_PowerPoint.log"},
					
				};
			}
			
			
			@Test(groups ={"TEST"},dataProvider = "_726")
			public void verify_UserDownloadPowerPoint(String activityType, String objectType, String file_name, String severity, String logFile) throws Exception{
				Reporter.log("Validate User downloaded file_name", true);
				String expectedMsg="User downloaded "+file_name;
				replayLogsEPDV3(logFile);
				data.put("message", expectedMsg);
				data.put("account_type", "Internal");
				assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
				
			}
			
			@DataProvider
			public Object[][] _726() {
				return new Object[][]{
					//Activity type  Object Type  Object Name  Severity  message
					{ "Download", "Document", "Presentation.pptx", "informational", "PowerPointOne,Download_PowerPoint.log"},
					
				};
			}
			
			
			@Test(groups ={"TEST"},dataProvider = "_727")
			public void verify_UserEditPowerPoint(String activityType, String objectType, String file_name, String severity, String logFile) throws Exception{
				Reporter.log("Validate User edited item file_name", true);
				String expectedMsg="User edited a document named "+file_name+" in Powerpoint Web App";
				replayLogsEPDV3(logFile);
				data.put("message", expectedMsg);
				data.put("account_type", "Internal");
				assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
				
			}
			
			@DataProvider
			public Object[][] _727() {
				return new Object[][]{
					//Activity type  Object Type  Object Name  Severity  message
					{ "Edit", "Document", "Presentation1.pptx", "informational", "OneDrive,Edit_PowerPoint.log"},
					
				};
			}
			
			
			@Test(groups ={"TEST"},dataProvider = "_728")
			public void verify_UserMovePowerPoint(String activityType, String objectType, String file_name, String FolderName, String severity, String logFile) throws Exception{
				Reporter.log("Validate User moved item file_name to root folder", true);
				String expectedMsg="[ERROR] The following activity failed:  User moved item "+file_name+" to "+FolderName;
				replayLogsEPDV3(logFile);
				data.put("message", expectedMsg);
				data.put("account_type", "Internal");
				assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
				
			}
			
			@DataProvider
			public Object[][] _728() {
				return new Object[][]{
					//Activity type  Object Type  Object Name  Severity  message
					{ "Move", "Document/Folder", "Presentation1.pptx","AFolder", "informational", "OneDrive,Move_PowerPoint.log"},
					
				};
			}
			
			
			
			@Test(groups ={"TEST"},dataProvider = "_729")
			public void verify_UserRenameOnePowerPoint(String activityType, String objectType, String RenameName, String Org_name, String severity, String logFile) throws Exception{
				Reporter.log("Validate User renamed item from to file_name", true);
				String expectedMsg="[ERROR] The following activity failed:  User renamed item from "+RenameName+" to "+Org_name;
				replayLogsEPDV3(logFile);
				data.put("message", expectedMsg);
				data.put("account_type", "Internal");
				assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
				
			}
			
			@DataProvider
			public Object[][] _729() {
				return new Object[][]{
					//Activity type  Object Type  Object Name  Severity  message
					{ "Rename", "Document/Folder","", "Document1.docx", "informational", "OneDrive,Rename_Doc.log"},
					
				};
			}
			
			
			
			@Test(groups ={"TEST"},dataProvider = "_730")
			public void verify_UserSharePowerPoint(String activityType, String objectType, String email, String file_name, String severity, String logFile) throws Exception{
				Reporter.log("Validate User sent email invitation(s) to email for file_name", true);
				String expectedMsg="[ERROR] The following activity failed:  User sent email invitation(s) to "+email+" for "+file_name;
				replayLogsEPDV3(logFile);
				data.put("message", expectedMsg);
				data.put("account_type", "Internal");
				assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
				
			}
			
			@DataProvider
			public Object[][] _730() {
				return new Object[][]{
					//Activity type  Object Type  Object Name  Severity  message
					{ "Share", "Document/Folder", "mohd.afjal@elastica.co", "Presentation1.pptx.", "informational", "OneDrive,Share_PowerPoint_ppt.log"},
					
				};
			}
			
			
			
			@Test(groups ={"TEST"},dataProvider = "_731")
			public void verify_UserCopyWordDocument(String activityType, String objectType, String file_name, String FolderName, String severity, String logFile) throws Exception{
				Reporter.log("Validate User copied item "+file_name+" to root folder", true);
				String expectedMsg="User copied item "+file_name+" to "+FolderName;
				replayLogsEPDV3(logFile);
				data.put("message", expectedMsg);
				data.put("account_type", "Internal");
				assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
				
			}
			
			@DataProvider
			public Object[][] _731() {
				return new Object[][]{
					//Activity type  Object Type  Object Name  Severity  message
					{ "Copy", "Document/Folder", "WordDocument.docx","GFolder", "informational", "WordDocument,Copy_word.log"},
					
				};
			}
			
			

			@Test(groups ={"TEST"},dataProvider = "_732")
			public void verify_UserRenameWordDocument(String activityType, String objectType, String RenameName, String Org_name, String severity, String logFile) throws Exception{
				Reporter.log("Validate User renamed item from to file_name", true);
				String expectedMsg="User renamed item from "+RenameName+" to "+Org_name;
				replayLogsEPDV3(logFile);
				data.put("message", expectedMsg);
				data.put("account_type", "Internal");
				assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
				
			}
			
			@DataProvider
			public Object[][] _732() {
				return new Object[][]{
					//Activity type  Object Type  Object Name  Severity  message
					{ "Rename", "Document/Folder","US_Passport_Number.txt", "WordDocument.docx", "informational", "WordDocument,Rename_word.log"},
					
				};
			}
			
			
			@Test(groups ={"TEST"},dataProvider = "_733")
			public void verify_UserShareWordDocument(String activityType, String objectType, String email, String file_name, String severity, String logFile) throws Exception{
				Reporter.log("Validate User sent email invitation(s) to email  for file_name", true);
				String expectedMsg="User sent email invitation(s) to "+email+"  for "+file_name;
				replayLogsEPDV3(logFile);
				data.put("message", expectedMsg);
				data.put("account_type", "Internal");
				assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
				
			}
			
			@DataProvider
			public Object[][] _733() {
				return new Object[][]{
					//Activity type  Object Type  Object Name  Severity  message
					{ "Share", "Document/Folder", "mohd.afjal@elastica.co", "WordDoc.docx.", "informational", "WordDocument,Share_External_User_word.log"},
					{ "Share", "Document/Folder", "admin@gatewayO365beatle.com admin@gatewayo365beatle.com", "hipaa.txt.", "informational", "WordDocument,Share_word.log"},
					
				};
			}
			
			
			@Test(groups ={"TEST"},dataProvider = "_734")
			public void verify_UserMoveWordDocument(String activityType, String objectType, String file_name, String FolderName, String severity, String logFile) throws Exception{
				Reporter.log("Validate User moved item file_name to root folder", true);
				String expectedMsg="User moved item "+file_name+" to "+FolderName;
				replayLogsEPDV3(logFile);
				data.put("message", expectedMsg);
				data.put("account_type", "Internal");
				assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
				
			}
			
			@DataProvider
			public Object[][] _734() {
				return new Object[][]{
					//Activity type  Object Type  Object Name  Severity  message
					{ "Move", "Document/Folder", "WordDocument.docx", "test", "informational", "WordDocument,Move_word.log"},
					
				};
			}
			
			
			@Test(groups ={"TEST"},dataProvider = "_735")
			public void verify_UserEmbedWordDocument(String activityType, String objectType, String file_name, String severity, String logFile) throws Exception{
				Reporter.log("Validate User viewed a Word Document named", true);
				String expectedMsg="User viewed a Word Document named "+file_name;
				replayLogsEPDV3(logFile);
				data.put("message", expectedMsg);
				data.put("account_type", "Internal");
				assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
				
			}
			
			@DataProvider
			public Object[][] _735() {
				return new Object[][]{
					//Activity type  Object Type  Object Name  Severity  message
					{ "View", "Document/Folder", "GWAuto.docx.", "informational", "WordDocument,Embed_Word.log"},
					
				};
			}
			
			
			@Test(groups ={"TEST"},dataProvider = "_736")
			public void verify_UserDownloadWordDocument(String activityType, String objectType, String file_name, String severity, String logFile) throws Exception{
				Reporter.log("Validate User downloaded file_name", true);
				String expectedMsg="User downloaded "+file_name;
				replayLogsEPDV3(logFile);
				data.put("message", expectedMsg);
				data.put("account_type", "Internal");
				assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
				
			}
			
			@DataProvider
			public Object[][] _736() {
				return new Object[][]{
					//Activity type  Object Type  Object Name  Severity  message
					{ "Download", "Document", "WordDoc.docx", "informational", "WordDocument,Download_word.log"},
					
				};
			}
			
			
			//@Test(groups ={"TEST2"},dataProvider = "_737")
			public void verify_UserDeleteOneDrive(String activityType, String objectType, String file_name, String severity, String logFile) throws Exception{
				Reporter.log("Validate User deleted file_name", true);
				String expectedMsg="User deleted "+file_name+" from the recycle Bin";
				replayLogsEPDV3(logFile);
				data.put("message", expectedMsg);
				data.put("account_type", "Internal");
				assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
				
			}
			
			@DataProvider
			public Object[][] _737() {
				return new Object[][]{
					//Activity type  Object Type  Object Name  Severity  message
					{ "Delete", "Document", "ExcelBook.xlsx", "informational", "RecycleBin,Delete_OneNote.log"},
					
				};
			}
			
			@Test(groups ={"TEST"},dataProvider = "_738")
			public void verify_UserEmptyOneDrive(String activityType, String objectType, String severity, String logFile) throws Exception{
				Reporter.log("Validate User emptied Recycle Bin", true);
				String expectedMsg="User emptied Recycle Bin";
				replayLogsEPDV3(logFile);
				data.put("message", expectedMsg);
				data.put("account_type", "Internal");
				assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
				
			}
			
			@DataProvider
			public Object[][] _738() {
				return new Object[][]{
					//Activity type  Object Type  Object Name  Severity  message
					{ "Empty", "Recycle Bin", "informational", "RecycleBin,Empty_OneNote.log"},
					
				};
			}
			
			
			@Test(groups ={"TEST"},dataProvider = "_739")
			public void verify_UserRestoreOneDrive(String activityType, String objectType, String severity, String logFile) throws Exception{
				Reporter.log("Validate User emptied Recycle Bin", true);
				String expectedMsg="User opened Recycle Bin";
				replayLogsEPDV3(logFile);
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
	
}
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
	
	
	
	
	
	