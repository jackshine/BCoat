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

public class GoogleMailEpdTest extends CommonConfiguration {

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
	
	
	
	 @Test(groups ={"TEST"},dataProvider = "_1978")
	public void verify_User_GmailSendEmail(String activityType, String objectType, String subject, String recipient, String severity, String logFile) throws Exception{
		Reporter.log("Validate User sent an email with subject This is a test mail to ", true);
		String expectedMsg="User sent an email with subject "+subject+" to "+recipient;
		replayLogsEPDV3(logFile);
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
		
	}
	
	@DataProvider
	public Object[][] _1978() {
		return new Object[][]{
			//Activity type  Object Type  Object Name  Severity  message
			{ "Share", "Email","This is a test mail", "interactsaqiub@gmail.com","informational", "Any,Gmail,DCompose,Compose.log"},
			
		};
	}
	
	
	
	 @Test(groups ={"TEST"},dataProvider = "_1977")
		public void verify_User_GmailSendEmailWithoutSubject(String activityType, String objectType, String recipient, String severity, String logFile) throws Exception{
			Reporter.log("Validate User sent an email with no subject to ", true);
			String expectedMsg="User sent an email with no subject to "+recipient;
			replayLogsEPDV3(logFile);
			data.put("message", expectedMsg);
			data.put("account_type", "Internal");
			assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
			
		}
		
		@DataProvider
		public Object[][] _1977() {
			return new Object[][]{
				//Activity type  Object Type  Object Name  Severity  message
				{ "Share", "Email","mohd.afjal@elastica.co","informational", "Mail,Send.log"},
				
			};
		}
	
	
	 @Test(groups ={"TEST"},dataProvider = "_1984")
		public void verify_User_DeleteFromTrash(String activityType, String objectType, String severity, String logFile) throws Exception{
			Reporter.log("Validate User emptied Trash ", true);
			String expectedMsg="User emptied Trash";
			replayLogsEPDV3(logFile);
			data.put("message", expectedMsg);
			data.put("account_type", "Internal");
			assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
			
		}
		
		@DataProvider
		public Object[][] _1984() {
			return new Object[][]{
				//Activity type  Object Type  Object Name  Severity  message
				{ "Delete", "Email","informational", "Any,Gmail,Trash.log"},
				
			};
		}
		
		
		@Test(groups ={"TEST"},dataProvider = "_1979_1980")
		public void verify_User_UploadAttachment1and2(String activityType, String objectType, String object_name, String severity, String logFile) throws Exception{
			Reporter.log("Validate User sent an email with subject This is a test mail to ", true);
			String expectedMsg="User uploaded file "+object_name+" as an attachment";
			replayLogsEPDV3(logFile);
			data.put("message", expectedMsg);
			data.put("account_type", "Internal");
			assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
			
		}
		
		@DataProvider
		public Object[][] _1979_1980() {
			return new Object[][]{
				//Activity type  Object Type  Object Name  Severity  message
				{ "Upload", "File","Test.txt","informational", "Mail,EmailUploadMultiFile_googleMail.log"},
				{ "Upload", "File","pii.rtf","informational", "Mail,EmailUploadMultiFile_googleMail.log"},
				
			};
		}
		
		
		@Test(groups ={"TEST"},dataProvider = "_1973")
		public void verify_User_GmailLoggedIn(String activityType, String objectType, String severity, String logFile) throws Exception{
			Reporter.log("Validate User logged in ", true);
			String expectedMsg="User logged in";
			replayLogsEPDV3(logFile);
			data.put("message", expectedMsg);
			data.put("account_type", "Internal");
			assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(activityType, severity), data)), activityType+" Logs does not match" ); 
			
		}
		
		@DataProvider
		public Object[][] _1973() {
			return new Object[][]{
				//Activity type  Object Type  Object Name  Severity  message
				{ "Login", "Session","informational", "Session,Login_google.log"},
				
			};
		}
		
		
		@Test(groups ={"TEST"},dataProvider = "_1974")
		public void verify_User_GmailLoginFailed(String activityType, String objectType, String severity, String logFile) throws Exception{
			Reporter.log("Validate User Login Failed!", true);
			String expectedMsg="User Login Failed!";
			data.clear();
			replayLogsEPDV3(logFile);
			data.put("message", expectedMsg);
			data.put("account_type", "Internal");
			assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(activityType, severity), data)), activityType+" Logs does not match" );
		}
		
		@DataProvider 
		public Object[][] _1974() {
			return new Object[][]{
				//Activity type  Object Type  Object Name  Severity  message
				{ "InvalidLogin", "Session","informational", "Session,Login_Failed.log"},
				
			};
		}
		
		
		@Test(groups ={"TEST"},dataProvider = "_1976")
		public void verify_User_GmailLogout(String activityType, String objectType, String severity, String logFile) throws Exception{
			Reporter.log("Validate User logged out ", true);
			String expectedMsg="User logged out";
			replayLogsEPDV3(logFile);
			data.put("message", expectedMsg);
			data.put("account_type", "Internal");
			assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
			
		}
		
		@DataProvider
		public Object[][] _1976() {
			return new Object[][]{
				//Activity type  Object Type  Object Name  Severity  message
				{ "Logout", "Session","informational", "Session,LogOut_google.log"},
				
			};
		}
	
		
		@Test(groups ={"TEST"},dataProvider = "_1981")
		public void verify_User_DownloadAttachment(String activityType, String objectType, String filename, String severity, String logFile) throws Exception{
			Reporter.log("Validate User logged out ", true);
			String expectedMsg="User downloaded attachment "+filename;
			replayLogsEPDV3(logFile);
			data.put("message", expectedMsg);
			data.put("account_type", "Internal");
			assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(activityType, severity), data)), activityType+" Logs does not match" ); 
			
		}
		
		@DataProvider
		public Object[][] _1981() {
			return new Object[][]{
				//Activity type  Object Type  Object Name  Severity  message
				{ "Download", "File","Hostel","informational", "File,Download_attachment.log"},
				
			};
		}
		
		
		@Test(groups ={"TEST"},dataProvider = "_1983")
		public void verify_User_DeleteFromTrash(String activityType, String objectType, String sheet, String severity, String logFile) throws Exception{
			Reporter.log("Validate User logged out ", true);
			String expectedMsg="User deleted email(s) "+sheet+" from Trash";
			replayLogsEPDV3(logFile);
			data.put("message", expectedMsg);
			data.put("account_type", "Internal");
			assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
			
		}
		
		@DataProvider
		public Object[][] _1983() {
			return new Object[][]{
				//Activity type  Object Type  Object Name  Severity  message
				{ "Delete", "Email","sheet","informational", "Email,Delete_from_trash.log"},
				
			};
			
		}
		
		
	
		@Test(groups ={"TEST"},dataProvider = "_1985")
		public void verify_User_MoveEmail(String activityType, String objectType, String emails, String fileNmae,  String severity, String logFile) throws Exception{
			Reporter.log("Validate user moved email ", true);
			String expectedMsg="User moved email(s) "+emails+" to "+fileNmae;
			replayLogsEPDV3(logFile);
			data.put("message", expectedMsg);
			data.put("account_type", "Internal");
			assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
			
		}
		
		@DataProvider
		public Object[][] _1985() {
			return new Object[][]{
				//Activity type  Object Type  Object Name  Severity  message
				{ "Move", "Email","","","informational", "Email,Move_to_social.log"},
				
			};
		}
		
		@Test(groups ={"TEST"},dataProvider = "_1986")
		public void verify_User_MoveEmail2(String activityType, String objectType, String emails, String fileNmae,  String severity, String logFile) throws Exception{
			Reporter.log("Validate user moved email ", true);
			String expectedMsg="User moved email(s) "+emails+" to "+fileNmae;
			replayLogsEPDV3(logFile);
			data.put("message", expectedMsg);
			data.put("account_type", "Internal");
			assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
			
		}
		
		@DataProvider
		public Object[][] _1986() {
			return new Object[][]{
				//Activity type  Object Type  Object Name  Severity  message
				{ "Move", "Email","","","informational", "Email,Move_to_promotions.log"},
				
			};
		}
		
		
		@Test(groups ={"TEST"},dataProvider = "_1987")
		public void verify_User_MoveEmail3(String activityType, String objectType, String emails,  String severity, String logFile) throws Exception{
			Reporter.log("Validate user moved email ", true);
			String expectedMsg="User moved email(s) "+emails+" to Updates";
			replayLogsEPDV3(logFile);
			data.put("message", expectedMsg);
			data.put("account_type", "Internal");
			assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
			
		}
		
		@DataProvider
		public Object[][] _1987() {
			return new Object[][]{
				//Activity type  Object Type  Object Name  Severity  message
				{ "Move", "Email","admin@gatewaybeatle.com", "informational", "Email,Move_to_update.log"},
				
			};
		}
		
		
		@Test(groups ={"TEST"},dataProvider = "_1988")
		public void verify_User_MoveEmail4(String activityType, String objectType, String emails,  String severity, String logFile) throws Exception{
			Reporter.log("Validate user moved email ", true);
			String expectedMsg="User moved email(s) "+emails+" to Forums";
			replayLogsEPDV3(logFile);
			data.put("message", expectedMsg);
			data.put("account_type", "Internal");
			assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
			
		}
		
		@DataProvider
		public Object[][] _1988() {
			return new Object[][]{
				//Activity type  Object Type  Object Name  Severity  message
				{ "Move", "Email","admin@gatewaybeatle.com", "informational", "Email,Move_to_Forums.log"},
				
			};
		}
		
		
		@Test(groups ={"TEST"},dataProvider = "_2156")
		public void verify_User_UploadAttachment0(String activityType, String objectType, String objectName,  String severity, String logFile) throws Exception{
			Reporter.log("Validate User uploaded file ", true);
			String expectedMsg="[ERROR] The following activity failed:  User uploaded file "+objectName+" as an attachment";
			replayLogsEPDV3(logFile);
			data.put("message", expectedMsg);
			data.put("account_type", "Internal");
			assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(activityType, severity), data)), activityType+" Logs does not match" ); 
			
		}
		
		@DataProvider
		public Object[][] _2156() {
			return new Object[][]{
				//Activity type  Object Type  Object Name  Severity  message
				{ "Upload", "File","Hostel", "informational", "File,Upload_attachment0.log"},
				
			};
		}
		
		
		
		@Test(groups ={"TEST"},dataProvider = "_uploadFile")
		public void verify_User_draftMailwithAttach(String activityType, String objectType, String fileName, String severity, String logFile) throws Exception{
			Reporter.log("Validate User uploaded file Screen_Shot_2016-02-02_at_8.32.46_PM.png as an attachment ", true);
			String expectedMsg="User uploaded file "+fileName;
			replayLogsEPDV3(logFile);
			data.put("message", expectedMsg);
			data.put("account_type", "Internal");
			assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
			
		}
		
		@DataProvider
		public Object[][] _uploadFile() {
			return new Object[][]{
				//Activity type  Object Type  Object Name  Severity  message
				{ "Upload", "File","Screen_Shot_2016-02-02_at_8.32.46_PM.png as an attachment","informational", "Session,DraftMailWithAttach_googleMail.log"},
				
			};
		}

		
		
		@Test(groups ={"TEST"},dataProvider = "_1982")
		public void verify_User_DeleteEmail(String activityType, String objectType, String fileName, String severity, String logFile) throws Exception{
			Reporter.log("Validate User deleted email(s) ", true);
			String expectedMsg="User deleted email(s) "+fileName;
			replayLogsEPDV3(logFile);
			data.put("message", expectedMsg);
			data.put("account_type", "Internal");
			assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
			
		}
		
		@DataProvider
		public Object[][] _1982() {
			return new Object[][]{
				//Activity type  Object Type  Object Name  Severity  message
				{ "Delete", "Email","DetectNewtest.java","informational", "Any,Gmail,DDelete,Delete.log"},
				{ "Delete", "Email","Test User1 has accepted the invitation to your  '75c03469-3954-4a68-8945-df541dca5bd1' folder on Box","informational", "Mail,Delete.log"},
				
			};
		}
	
		
		//@Test(groups ={"TEST"},dataProvider = "_searchEmail")
		public void verify_User_searchEmail(String activityType, String objectType, String fileName, String severity, String logFile) throws Exception{
			Reporter.log("Validate User searched email(s) ", true);
			String expectedMsg="User searched email(s) "+fileName;
			replayLogsEPDV3(logFile);
			data.put("message", expectedMsg);
			data.put("account_type", "Internal");
			assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
			
		}
		
		@DataProvider
		public Object[][] _searchEmail() {
			return new Object[][]{
				//Activity type  Object Type  Object Name  Severity  message
				{ "Search", "Email","","informational", "Session,Search_googleMail.log"},
				
			};
		}
		
		//@Test(groups ={"TEST"},dataProvider = "_draftEmail")
		public void verify_User_draftEmail(String activityType, String objectType, String fileName, String severity, String logFile) throws Exception{
			Reporter.log("Validate User searched email(s) ", true);
			String expectedMsg="User sent email(s) to draft "+fileName;
			replayLogsEPDV3(logFile);
			data.put("message", expectedMsg);
			data.put("account_type", "Internal");
			assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
			
		}
		
		@DataProvider
		public Object[][] _draftEmail() {
			return new Object[][]{
				//Activity type  Object Type  Object Name  Severity  message
				{ "Draft", "Email","","informational", "Any,Gmail,DDraft,Draft.log"},
				
			};
		}
	
}
		

	