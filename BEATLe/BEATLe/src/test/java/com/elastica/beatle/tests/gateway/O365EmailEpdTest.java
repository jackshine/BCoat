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
 * @author mohd afjal
 *
 */

public class O365EmailEpdTest extends CommonConfiguration {

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
	
	
	
	 @Test(groups ={"TEST"},dataProvider = "_101")
	public void verify_User_DeleteEmail(String activityType, String objectType, String subject,  String objectName, String severity, String logFile) throws Exception{
		Reporter.log("Validate User deleted email(s) no subject and sender(s) ", true);
		String expectedMsg="User deleted email(s) with subject(s) "+subject+" and sender(s) "+objectName;
		replayLogsEPDV3(logFile);
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
		
	}
	
	@DataProvider
	public Object[][] _101() {
		return new Object[][]{
			//Activity type  Object Type  Object Name  Severity  message
			{ "Delete", "Email", "Test User1 has shared 'Document'", "testuser1@gatewayO365beatle.com", "informational", "EmailO365,Delete.log"},
			
		};
	}
	
	
	
	 @Test(groups ={"TEST"},dataProvider = "_102")
		public void verify_User_MoveToArchive(String activityType, String objectType, String subject, String email, String severity, String logFile) throws Exception{
			Reporter.log("Validate User moved email(s) with subject(s) and sender(s) ", true);
			String expectedMsg="User moved email(s) with subject(s) "+subject+" and sender(s) "+email;
			replayLogsEPDV3(logFile);
			data.put("message", expectedMsg);
			data.put("account_type", "Internal");
			assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
			
		}
		
		@DataProvider
		public Object[][] _102() {
			return new Object[][]{
				//Activity type  Object Type  Object Name  Severity  message
				{ "Move", "Email","Test User1 has shared 'NewFolder'","testuser1@gatewayO365beatle.com.","informational", "EmailO365,MoveToArchive.log"},
				
			};
		}
	
	
		 @Test(groups ={"TEST"},dataProvider = "_103")
			public void verify_User_MoveToDraft(String activityType, String objectType, String subject, String email, String severity, String logFile) throws Exception{
				Reporter.log("Validate User moved email(s) with subject(s) and sender(s) ", true);
				String expectedMsg="User moved email(s) with subject(s) "+subject+" and sender(s) "+email+".";
				replayLogsEPDV3(logFile);
				data.put("message", expectedMsg);
				data.put("account_type", "Internal");
				assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
				
			}
			
			@DataProvider
			public Object[][] _103() {
				return new Object[][]{
					//Activity type  Object Type  Object Name  Severity  message
					{ "Move", "Email","Test User1 has shared 'ghgh'","testuser1@gatewayO365beatle.com","informational", "EmailO365,MoveToDraft.log"},
					
				};
			}
		
		
			 @Test(groups ={"TEST"},dataProvider = "_104")
				public void verify_User_MoveToJunk(String activityType, String objectType, String subject, String email, String severity, String logFile) throws Exception{
					Reporter.log("Validate User moved email(s) with subject(s) and sender(s) ", true);
					String expectedMsg="User moved email(s) with subject(s) "+subject+" and sender(s) "+email+".";
					replayLogsEPDV3(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _104() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Move", "Email","","","informational", "EmailO365,MoveToJunk.log"},
						
					};
				}
		
		
		@Test(groups ={"TEST"},dataProvider = "_105")
		public void verify_User_MoveToNewFolder(String activityType, String objectType, String subject, String email, String severity, String logFile) throws Exception{
			Reporter.log("Validate User moved email(s) with subject(s) and sender(s) ", true);
			String expectedMsg="User moved email(s) with subject(s) "+subject+" and sender(s) "+email+".";
			replayLogsEPDV3(logFile);
			data.put("message", expectedMsg);
			data.put("account_type", "Internal");
			assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
			
		}
		
		@DataProvider
		public Object[][] _105() {
			return new Object[][]{
				//Activity type  Object Type  Object Name  Severity  message
				{ "Move", "Email","","", "informational", "EmailO365,MoveToNewFolder.log"},
				
			};
		}
		
		
		@Test(groups ={"TEST"},dataProvider = "_106")
		public void verify_User_SendEmailWithNoSub(String activityType, String objectType, String email, String severity, String logFile) throws Exception{
			Reporter.log("Validate User sent an email ", true);
			String expectedMsg="User sent an email to "+email+" with no subject";
			replayLogsEPDV3(logFile);
			data.put("message", expectedMsg);
			data.put("account_type", "Internal");
			assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
			
		}
		
		@DataProvider
		public Object[][] _106() {
			return new Object[][]{
				//Activity type  Object Type  Object Name  Severity  message
				{ "Send", "Email", "mohammad.usman@elastica.co", "informational", "EmailO365,Send.log"},
				
			};
		}
	
	
		@Test(groups ={"TEST"},dataProvider = "_107")
		public void verify_User_SendEmailWithAttach(String activityType, String objectType, String email, String objectName,  String severity, String logFile) throws Exception{
			Reporter.log("Validate User sent an email ", true);
			String expectedMsg="User sent an email to "+email+" with subject "+objectName;
			replayLogsEPDV3(logFile);
			data.put("message", expectedMsg);
			data.put("account_type", "Internal");
			assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
			
		}
		
		@DataProvider
		public Object[][] _107() {
			return new Object[][]{
				//Activity type  Object Type  Object Name  Severity  message
				{ "Send", "Email", "mohd.afjal@elastica.co", "Message", "informational", "EmailO365,SendWithAttach.log"},
				
			};
		}

		
		
		@Test(groups ={"TEST"},dataProvider = "_108")
		public void verify_User_SendEmailWithMultiAttach(String activityType, String objectType, String email, String severity, String logFile) throws Exception{
			Reporter.log("Validate User sent an email ", true);
			String expectedMsg="User sent an email to "+email+" with subject Files";
			replayLogsEPDV3(logFile);
			data.put("message", expectedMsg);
			data.put("account_type", "Internal");
			assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
			
		}
		
		@DataProvider
		public Object[][] _108() {
			return new Object[][]{
				//Activity type  Object Type  Object Name  Severity  message
				{ "Send", "Email", "mohd.afjal@elastica.co", "informational", "EmailO365,SendWithMulti.log"},
				
			};
		}
		
		
		@Test(groups ={"TEST"},dataProvider = "_109")
		public void verify_User_SendEmail(String activityType, String objectType, String email, String subject, String severity, String logFile) throws Exception{
			Reporter.log("Validate User sent an email ", true);
			String expectedMsg="User sent an email to "+email+" with subject "+subject;
			replayLogsEPDV3(logFile);
			data.put("message", expectedMsg);
			data.put("account_type", "Internal");
			assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
			
		}
		
		@DataProvider
		public Object[][] _109() {
			return new Object[][]{
				//Activity type  Object Type  Object Name  Severity  message
				{ "Send", "Email","mohd.afjal@elastica.co","Message", "informational", "EmailO365,SendWithAttach.log"},
				
			};
		}
	
		
		@Test(groups ={"TEST"},dataProvider = "_110")
		public void verify_User_createFolder(String activityType, String objectType, String folderName, String severity, String logFile) throws Exception{
			Reporter.log("Validate User created folder ", true);
			String expectedMsg="User created a folder named "+folderName;
			replayLogsWithDelay(logFile);
			data.put("message", expectedMsg);
			data.put("account_type", "Internal");
			assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
			
		}
		
		@DataProvider
		public Object[][] _110() {
			return new Object[][]{
				//Activity type  Object Type  Object Name  Severity  message
				{ "Create", "Folder", "SFolder", "informational", "Folder,Office 365,Create.log"},
				
			};
		}
		
		@Test(groups ={"TEST"},dataProvider = "_111")
		public void verify_User_deleteFolder(String activityType, String objectType, String folderName, String severity, String logFile) throws Exception{
			Reporter.log("Validate User deleted folder ", true);
			String expectedMsg="User deleted folder "+folderName;
			replayLogsWithDelay(logFile);
			data.put("message", expectedMsg);
			data.put("account_type", "Internal");
			assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
			
		}
		
		@DataProvider
		public Object[][] _111() {
			return new Object[][]{
				//Activity type  Object Type  Object Name  Severity  message
				{ "Delete", "Folder", "SFolder", "informational", "Folder,Office 365,Delete.log"},
				
			};
		}
		
		@Test(groups ={"TEST"},dataProvider = "_112")
		public void verify_User_renameFolder(String activityType, String objectType, String folderName, String renameFolder, String severity, String logFile) throws Exception{
			Reporter.log("Validate User renamed folder ", true);
			String expectedMsg="User renamed folder "+folderName+" to "+renameFolder;
			replayLogsWithDelay(logFile);
			data.put("message", expectedMsg);
			data.put("account_type", "Internal");
			assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
			
		}
		
		@DataProvider
		public Object[][] _112() {
			return new Object[][]{
				//Activity type  Object Type  Object Name  Severity  message
				{ "Rename", "Folder", "SFolder", "SSFolder", "informational", "Folder,Office 365,Rename.log"},
				
			};
		}
		
		@Test(groups ={"TEST"},dataProvider = "_113")
		public void verify_User_emptyFolder(String activityType, String objectType, String folderName, String severity, String logFile) throws Exception{
			Reporter.log("Validate User emptied folder ", true);
			String expectedMsg="User emptied folder "+folderName;
			replayLogsWithDelay(logFile);
			data.put("message", expectedMsg);
			data.put("account_type", "Internal");
			assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
			
		}
		
		@DataProvider
		public Object[][] _113() {
			return new Object[][]{
				//Activity type  Object Type  Object Name  Severity  message
				{ "Empty", "Folder", "SFolder", "informational", "Folder,Office 365,Empty.log"},
				
			};
		}
		
		@Test(groups ={"TEST"},dataProvider = "_114")
		public void verify_User_moveFolder(String activityType, String objectType, String folderName, String severity, String logFile) throws Exception{
			Reporter.log("Validate User moved folder ", true);
			String expectedMsg="User moved folder "+folderName;
			replayLogsWithDelay(logFile);
			data.put("message", expectedMsg);
			data.put("account_type", "Internal");
			assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
			
		}
		
		@DataProvider
		public Object[][] _114() {
			return new Object[][]{
				//Activity type  Object Type  Object Name  Severity  message
				{ "Move", "Folder", "SSFolder", "informational", "SubFolder,Admin,Move.log"},
				
			};
		}
		
		@Test(groups ={"TEST"},dataProvider = "_115")
		public void verify_User_createCalenderEvent(String activityType, String objectType, String folderName, String severity, String logFile) throws Exception{
			Reporter.log("Validate User created calendar event ", true);
			String expectedMsg="User created calendar event "+folderName;
			replayLogsWithDelay(logFile);
			data.put("message", expectedMsg);
			data.put("account_type", "Internal");
			assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
			
		}
		
		@DataProvider
		public Object[][] _115() {
			return new Object[][]{
				//Activity type  Object Type  Object Name  Severity  message
				{ "create", "Calendar", "LabourDay", "informational", "Calendar,Office 365,Create_Event.log"},
				
			};
		}
		
		
		
	
}
		

	