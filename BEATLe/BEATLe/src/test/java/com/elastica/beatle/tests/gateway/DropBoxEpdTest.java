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

public class DropBoxEpdTest extends CommonConfiguration {

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
			replayLogs(logFile);
			data.put("message", expectedMsg);
			data.put("account_type", "Internal");
			assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
			
		}
		
		@DataProvider
		public Object[][] _sessionLogin() {
			return new Object[][]{
				//Activity type  Object Type  Object Name  Severity  message
				{ "Login",        "Session", "User logged in",     "informational", "Session,Dropbox_2,Login.log"},
				{ "Logout",       "Session", "User logged out",    "informational", "Session,Dropbox_2,Logout.log"},
				{ "InvalidLogin", "Session", "User login Failed!", "informational", "Session,Dropbox_2,Invalid_Login.log"},
				
			};
		}
		
			
			@Test(groups ={"TEST"},dataProvider = "_createFolder")
			public void verify_User_DropboxCreateFolder(String activityType, String objectType, String folderName, String Desti,  String severity, String logFile) throws Exception{
				Reporter.log("Validate User created new folder GWFolder in path ", true);
				String expectedMsg="[ERROR] The following activity failed:  User created new folder "+folderName+" in path "+Desti;
				replayLogs(logFile);
				data.put("message", expectedMsg);
				data.put("account_type", "Internal");
				assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
				
			}
			
			@DataProvider
			public Object[][] _createFolder() {
				return new Object[][]{
					//Activity type  Object Type  Object Name  Severity  message
					{ "Create", "Folder", "GWAuto", "/", "informational", "Folder,Dropbox_2,Create.log" },
					
				};
			}
			
			 @Test(groups ={"TEST"},dataProvider = "_ShareFolder")
				public void verify_User_DropboxShareFolder(String activityType, String objectType, String folderName, String Desti,  String severity, String logFile) throws Exception{
					Reporter.log("Validate User created new folder GWFolder in path ", true);
					String expectedMsg="[ERROR] The following activity failed:  User shared a folder "+folderName+" with "+Desti;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _ShareFolder() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Share", "Folder", "", "mohd.afjal@elastica.co", "informational", "Folder,Dropbox_2,Share.log" },
						
					};
				}
			
			@Test(groups ={"TEST"},dataProvider = "_deleteFolder")
			public void verify_User_DropboxDeleteFolder(String activityType, String objectType, String folderName,  String severity, String logFile) throws Exception{
				Reporter.log("Validate User deleted folder GWAuto in path ", true);
				String expectedMsg="[ERROR] The following activity failed:  User deleted "+folderName;
				replayLogs(logFile);
				data.put("message", expectedMsg);
				data.put("account_type", "Internal");
				assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
				
			}
			
			@DataProvider
			public Object[][] _deleteFolder() {
				return new Object[][]{
					//Activity type  Object Type  Object Name  Severity  message
					{ "Delete", "Folder", "GWAuto",  "informational", "Folder,Dropbox_2,Delete.log" },
					
				};
			}
			
			@Test(groups ={"TEST"},dataProvider = "_InviteFolder")
			public void verify_User_DropboxInviteFolder(String activityType, String objectType, String folderName, String UserId,  String severity, String logFile) throws Exception{
				Reporter.log("Validate User invited folder ", true);
				String expectedMsg="[ERROR] The following activity failed:  User shared a folder "+folderName+" with "+UserId;
				replayLogs(logFile);
				data.put("message", expectedMsg);
				data.put("account_type", "Internal");
				assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
				
			}
			
			@DataProvider
			public Object[][] _InviteFolder() {
				return new Object[][]{
					//Activity type  Object Type  Object Name  Severity  message
					{ "Share", "Folder", "",  "gaurav.kainth@elastica.co", "informational", "Folder,Dropbox_2,Invite.log" },
					
				};
			}
			
			
			@Test(groups ={"TEST"},dataProvider = "_UnInviteFolder")
			public void verify_User_DropboxUnInviteFolder(String activityType, String objectType, String folderName, String Desti,  String severity, String logFile) throws Exception{
				Reporter.log("Validate User created new folder GWFolder in path ", true);
				String expectedMsg="[ERROR] The following activity failed:  User unshared "+folderName+" for "+Desti;
				replayLogs(logFile);
				data.put("message", expectedMsg);
				data.put("account_type", "Internal");
				assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
				
			}
			
			@DataProvider
			public Object[][] _UnInviteFolder() {
				return new Object[][]{
					//Activity type  Object Type  Object Name  Severity  message
					{ "Share", "Folder", "", "gaurav.kainth@elastica.co", "informational", "Folder,Dropbox_2,Uninvite.log" },
					
				};
			}
			
			 @Test(groups ={"TEST"},dataProvider = "_uploadFile")
				public void verify_User_DropboxUploadFile(String activityType, String objectType, String fileName,  String severity, String logFile) throws Exception{
					Reporter.log("Validate User uploaded file ", true);
					String expectedMsg="User uploaded file "+fileName;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _uploadFile() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Upload", "File", "Test.doc", "informational", "File,Dropbox_2,Upload_Test_doc.log" },
						
					};
				}
				
				 @Test(groups ={"TEST"},dataProvider = "_downloadFile")
					public void verify_User_DropboxDownloadFile(String activityType, String objectType, String fileName,  String severity, String logFile) throws Exception{
						Reporter.log("Validate User downloaded file ", true);
						String expectedMsg="[ERROR] The following activity failed:  User downloaded file "+fileName;
						replayLogs(logFile);
						data.put("message", expectedMsg);
						data.put("account_type", "Internal");
						assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
						
					}
					
					@DataProvider
					public Object[][] _downloadFile() {
						return new Object[][]{
							//Activity type  Object Type  Object Name  Severity  message
							{ "Download", "File", "GWFolder/Test1.doc", "informational", "File,Dropbox_2,Download_Test1_doc.log" },
							
						};
					}
					
					
					 @Test(groups ={"TEST"},dataProvider = "_renameFile")
						public void verify_User_DropboxrenameFile(String activityType, String objectType, String sourceName, String destiName,  String severity, String logFile) throws Exception{
							Reporter.log("Validate User renamed file ", true);
							String expectedMsg="User renamed "+sourceName+" to "+destiName;
							replayLogs(logFile);
							data.put("message", expectedMsg);
							data.put("account_type", "Internal");
							assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
							
						}
						
						@DataProvider
						public Object[][] _renameFile() {
							return new Object[][]{
								//Activity type  Object Type  Object Name  Severity  message
								{ "Rename", "File", "Test.pdf","Test1.pdf", "informational", "File,Dropbox_2,Rename_Test_doc.log" },
								
							};
						}
						
						@Test(groups ={"TEST"},dataProvider = "_shareFile")
						public void verify_User_DropboxshareFile(String activityType, String objectType, String fileName, String UserId,  String severity, String logFile) throws Exception{
							Reporter.log("Validate User shared file ", true);
							String expectedMsg="[ERROR] The following activity failed:  User shared "+fileName+" with "+UserId;
							replayLogs(logFile);
							data.put("message", expectedMsg);
							data.put("account_type", "Internal");
							assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
							
						}
						
						@DataProvider
						public Object[][] _shareFile() {
							return new Object[][]{
								//Activity type  Object Type  Object Name  Severity  message
								{ "Share", "File", "1","mohd.afjal@elastica.co", "informational", "File,Dropbox_2,Share_Test1_pdf.log" },
								
							};
						}
						
						@Test(groups ={"TEST"},dataProvider = "_shareFolder")
						public void verify_User_DropboxshareFolder(String activityType, String objectType, String folderName, String UserId,  String severity, String logFile) throws Exception{
							Reporter.log("Validate User shared file ", true);
							String expectedMsg="[ERROR] The following activity failed:  User shared a folder "+folderName+" with "+UserId;
							replayLogs(logFile);
							data.put("message", expectedMsg);
							data.put("account_type", "Internal");
							assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
							
						}
						
						@DataProvider
						public Object[][] _shareFolder() {
							return new Object[][]{
								//Activity type  Object Type  Object Name  Severity  message
								{ "Share", "File", "", "mohd.afjal@elastica.co", "informational", "Folder,Dropbox_2,Share.log" },
								
							};
						}	
						
						
						@Test(groups ={"TEST"},dataProvider = "_deleteFile")
						public void verify_User_DropboxDeleteFile(String activityType, String objectType, String fileName,  String severity, String logFile) throws Exception{
							Reporter.log("Validate User deleted file ", true);
							String expectedMsg="[ERROR] The following activity failed:  User deleted "+fileName;
							replayLogs(logFile);
							data.put("message", expectedMsg);
							data.put("account_type", "Internal");
							assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
							
						}
						
						@DataProvider
						public Object[][] _deleteFile() {
							return new Object[][]{
								//Activity type  Object Type  Object Name  Severity  message
								{ "Delete", "File/Folder", "GWFolder/Test1.pdf", "informational", "File,Dropbox_2,Delete.log" },
								
							};
						}
						
						@Test(groups ={"TEST"},dataProvider = "_copyFile")
						public void verify_User_DropboxCopyFile(String activityType, String objectType, String fileName, String folderName,  String severity, String logFile) throws Exception{
							Reporter.log("Validate User copied file ", true);
							String expectedMsg="[ERROR] The following activity failed:  User copied "+fileName+" to "+folderName;
							replayLogs(logFile);
							data.put("message", expectedMsg);
							data.put("account_type", "Internal");
							assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
							
						}
						
						@DataProvider
						public Object[][] _copyFile() {
							return new Object[][]{
								//Activity type  Object Type  Object Name  Severity  message
								{ "Copy", "File/Folder", "/GWFolder/Test1.doc", "/AFFolder", "informational", "File,Dropbox_2,Copy_Test_doc.log" },
								
							};
						}
						
						@Test(groups ={"TEST"},dataProvider = "_moveFile")
						public void verify_User_DropboxMoveFile(String activityType, String objectType, String fileName, String folderName,  String severity, String logFile) throws Exception{
							Reporter.log("Validate User moved file ", true);
							String expectedMsg="[ERROR] The following activity failed:  User moved "+fileName+" to "+folderName;
							replayLogs(logFile);
							data.put("message", expectedMsg);
							data.put("account_type", "Internal");
							assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
							
						}
						
						@DataProvider
						public Object[][] _moveFile() {
							return new Object[][]{
								//Activity type  Object Type  Object Name  Severity  message
								{ "Move", "File/Folder", "/Test1.doc", "/GWFolder", "informational", "File,Dropbox_2,Move_Test_doc.log" },
								
							};
						}
						
						@Test(groups ={"TEST"},dataProvider = "_InviteCollaborator")
						public void verify_User_DropboxInviteCollaboratorFile(String activityType, String objectType, String UserId, String folderName,  String severity, String logFile) throws Exception{
							Reporter.log("Validate User deleted file ", true);
							String expectedMsg="[ERROR] The following activity failed:  User invited "+UserId+" as collaborators for a folder "+folderName;
							replayLogs(logFile);
							data.put("message", expectedMsg);
							data.put("account_type", "Internal");
							assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
							
						}
						
						@DataProvider
						public Object[][] _InviteCollaborator() {
							return new Object[][]{
								//Activity type  Object Type  Object Name  Severity  message
								{ "Share", "File/Folder", "mdafjaldoon@gmail.com", "", "informational", "Folder,Dropbox_2,Invite_Collaborator_AFolder.log" },
								
							};
						}
						
						@Test(groups ={"TEST"},dataProvider = "_UninviteCollaborator")
						public void verify_User_DropboxUninviteCollaboratorFile(String activityType, String objectType,  String folderName,  String severity, String logFile) throws Exception{
							Reporter.log("Validate User deleted file ", true);
							String expectedMsg="[ERROR] The following activity failed:  User uninvited a collaborator  on "+folderName;
							replayLogs(logFile);
							data.put("message", expectedMsg);
							data.put("account_type", "Internal");
							assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
							
						}
						
						@DataProvider
						public Object[][] _UninviteCollaborator() {
							return new Object[][]{
								//Activity type  Object Type  Object Name  Severity  message
								{ "Share", "File/Folder",  "", "informational", "Folder,Dropbox_2,Uninvite_Collaborator_AFolder.log" },
								
							};
						}
						
						@Test(groups ={"TEST"},dataProvider = "_createAlbum")
						public void verify_User_DropboxCreateAlbum(String activityType, String objectType,  String albumName,  String severity, String logFile) throws Exception{
							Reporter.log("Validate User created Album", true);
							String expectedMsg="User created an album named "+albumName;
							replayLogs(logFile);
							data.put("message", expectedMsg);
							data.put("account_type", "Internal");
							assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
							
						}
						
						@DataProvider
						public Object[][] _createAlbum() {
							return new Object[][]{
								//Activity type  Object Type  Object Name  Severity  message
								{ "Create", "Album",  "Gateway", "informational", "Album,Dropbox_2,Create.log" },
								
							};
						}
						
						@Test(groups ={"TEST"},dataProvider = "_addPhotoAlbum")
						public void verify_User_DropboxAddPhotoAlbum(String activityType, String objectType, String photoName,  String albumName,  String severity, String logFile) throws Exception{
							Reporter.log("Validate User added phto to Album", true);
							String expectedMsg="User added photo "+photoName+" to an album named "+albumName;
							replayLogs(logFile);
							data.put("message", expectedMsg);
							data.put("account_type", "Internal");
							assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
							
						}
						
						@DataProvider
						public Object[][] _addPhotoAlbum() {
							return new Object[][]{
								//Activity type  Object Type  Object Name  Severity  message
								{ "Add", "Photo", "20151002_193302.jpg", "GatewayAuto", "informational", "Album,Dropbox_2,Add_Photo.log" },
								
							};
						}
						
						@Test(groups ={"TEST"},dataProvider = "_renameAlbum")
						public void verify_User_DropboxRenameAlbum(String activityType, String objectType, String albumName,  String severity, String logFile) throws Exception{
							Reporter.log("Validate User renamed Album", true);
							String expectedMsg="User renamed album to "+albumName;
							replayLogs(logFile);
							data.put("message", expectedMsg);
							data.put("account_type", "Internal");
							assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
							
						}
						
						@DataProvider
						public Object[][] _renameAlbum() {
							return new Object[][]{
								//Activity type  Object Type  Object Name  Severity  message
								{ "Rename", "Album",  "GatewayAuto", "informational", "Album,Dropbox_2,Rename.log" },
								
							};
						}
						
						@Test(groups ={"TEST"},dataProvider = "_shareAlbum")
						public void verify_User_DropboxShareAlbum(String activityType, String objectType, String PhotoName,  String UserId,  String severity, String logFile) throws Exception{
							Reporter.log("Validate User shared Album", true);
							String expectedMsg="User shared photo "+PhotoName+" with "+UserId;
							replayLogs(logFile);
							data.put("message", expectedMsg);
							data.put("account_type", "Internal");
							assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
							
						}
						
						@DataProvider
						public Object[][] _shareAlbum() {
							return new Object[][]{
								//Activity type  Object Type  Object Name  Severity  message
								{ "Share", "Album", "", "mohd.afjal@elastica.co", "informational", "Album,Dropbox_2,Share.log" },
								
							};
						}
		
						@Test(groups ={"TEST"},dataProvider = "_deleteAlbum")
						public void verify_User_DropboxDeleteAlbum(String activityType, String objectType, String PhotoName,  String severity, String logFile) throws Exception{
							Reporter.log("Validate User deleted Album", true);
							String expectedMsg="User deleted album "+PhotoName;
							replayLogs(logFile);
							data.put("message", expectedMsg);
							data.put("account_type", "Internal");
							assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
							
						}
						
						@DataProvider
						public Object[][] _deleteAlbum() {
							return new Object[][]{
								//Activity type  Object Type  Object Name  Severity  message
								{ "Delete", "Album", "GWT",  "informational", "Album,Dropbox_2,Delete.log" },
								
							};
						}	
						
						@Test(groups ={"TEST"},dataProvider = "_createFileRequest")
						public void verify_User_DropboxCreateFileRequest(String activityType, String objectType, String fileName,  String severity, String logFile) throws Exception{
							Reporter.log("Validate User created File-Request", true);
							String expectedMsg="User created file request "+fileName;
							replayLogs(logFile);
							data.put("message", expectedMsg);
							data.put("account_type", "Internal");
							assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
							
						}
						
						@DataProvider
						public Object[][] _createFileRequest() {
							return new Object[][]{
								//Activity type  Object Type  Object Name  Severity  message
								{ "Create", "File_Request", "Birthday",  "informational", "File_Request,Dropbox_2,Create.log" },
								
							};
						}	
						
						@Test(groups ={"TEST"},dataProvider = "_reopenFileRequest")
						public void verify_User_DropboxReopenFileRequest(String activityType, String objectType, String fileName,  String severity, String logFile) throws Exception{
							Reporter.log("Validate User reopened File-Request", true);
							String expectedMsg="[ERROR] The following activity failed:  User reopened the File Request "+fileName;
							replayLogs(logFile);
							data.put("message", expectedMsg);
							data.put("account_type", "Internal");
							assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
							
						}
						
						@DataProvider
						public Object[][] _reopenFileRequest() {
							return new Object[][]{
								//Activity type  Object Type  Object Name  Severity  message
								{ "Reopen", "File_Request", "Birthday",  "informational", "File_Request,Dropbox_2,Reopen.log" },
								
							};
						}	
						
						@Test(groups ={"TEST"},dataProvider = "_sendFileRequest")
						public void verify_User_DropboxSendFileRequest(String activityType, String objectType, String fileName, String UserId, String severity, String logFile) throws Exception{
							Reporter.log("Validate User send File-Request", true);
							String expectedMsg="[ERROR] The following activity failed:  User shared a folder "+fileName+" with "+UserId; 
							replayLogs(logFile);
							data.put("message", expectedMsg);
							data.put("account_type", "Internal");
							assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
							
						}
						
						@DataProvider
						public Object[][] _sendFileRequest() {
							return new Object[][]{
								//Activity type  Object Type  Object Name  Severity  message
								{ "Send", "File_Request", "/File requests/Birthday", "mohd.afjal@elastica.co",  "informational", "File_Request,Dropbox_2,Send.log" },
								
							};
						}	
						
						@Test(groups ={"TEST"},dataProvider = "_closeFileRequest")
						public void verify_User_DropboxCloseFileRequest(String activityType, String objectType, String fileName, String severity, String logFile) throws Exception{
							Reporter.log("Validate User close File-Request", true);
							String expectedMsg="User close file request "+fileName;
							replayLogs(logFile);
							data.put("message", expectedMsg);
							data.put("account_type", "Internal");
							assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
							
						}
						
						@DataProvider
						public Object[][] _closeFileRequest() {
							return new Object[][]{
								//Activity type  Object Type  Object Name  Severity  message
								{ "Close", "File_Request", "Birthday",  "informational", "File_Request,Dropbox_2,Close.log" },
								
							};
						}	
				
//		 @Test(groups ={"TEST"},dataProvider = "_1978")
			public void verify_User_GmailSendEmail(String activityType, String objectType, String fileName, String UserId,  String severity) throws Exception{
				Reporter.log("Validate User uploaded a file ", true);
				String expectedMsg="User shared "+fileName+" with "+UserId;
				
				String logFile = "File,Dropbox_2,Few_Events.log";
				
				replayLogs(logFile);
				data.put("message", expectedMsg);
				data.put("account_type", "Internal");
				assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
				
			}
			
			@DataProvider
			public Object[][] _1978() {
				return new Object[][]{
					//Activity type  Object Type  Object Name  Severity  message
					{ "Share", "File", "","mohd.afjal@elastica.co", "informational" },
					
				};
			}
	
}
		

	