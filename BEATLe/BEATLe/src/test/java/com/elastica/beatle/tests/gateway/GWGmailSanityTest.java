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

public class GWGmailSanityTest extends CommonConfiguration {

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
	
	
	
	 @Test(groups ={"SANITY"},dataProvider = "_sendEmail")
	public void verify_User_GmailSendEmail(String activityType, String objectType, String subject, String recipient, String severity, String logFile) throws Exception{
		Reporter.log("Validate User sent an email with subject This is a test mail to ", true);
		String expectedMsg="User sent an email with subject "+subject+" to "+recipient;
		replayLogsEPDV3(logFile);
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
		
	}
	
	@DataProvider
	public Object[][] _sendEmail() {
		return new Object[][]{
			//Activity type  Object Type  Object Name  Severity  message
			{ "Share", "Email","This is a test mail", "interactsaqiub@gmail.com","informational", "Any,Gmail,DCompose,Compose.log"},
			
		};
	}
	
	@Test(groups ={"SANITY"},dataProvider = "_uploadAttachment")
	public void verify_User_UploadAttachment(String activityType, String objectType, String object_name, String severity, String logFile) throws Exception{
		Reporter.log("Validate User sent an email with subject This is a test mail to ", true);
		String expectedMsg="User uploaded file "+object_name+" as an attachment";
		replayLogsEPDV3(logFile);
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
		
	}
	
	@DataProvider
	public Object[][] _uploadAttachment() {
		return new Object[][]{
			//Activity type  Object Type  Object Name  Severity  message
			{ "Upload", "File","Test.txt","informational", "Mail,EmailUploadMultiFile_googleMail.log"},
			
		};
	}
	
	@Test(groups ={"SANITY"},dataProvider = "_downloadAttachment")
	public void verify_User_DownloadAttachment(String activityType, String objectType, String filename, String severity, String logFile) throws Exception{
		Reporter.log("Validate User logged out ", true);
		String expectedMsg="User downloaded attachment "+filename;
		replayLogsEPDV3(logFile);
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(activityType, severity), data)), activityType+" Logs does not match" ); 
		
	}
	
	@DataProvider
	public Object[][] _downloadAttachment() {
		return new Object[][]{
			//Activity type  Object Type  Object Name  Severity  message
			{ "Download", "File","Hostel","informational", "File,Download_attachment.log"},
			
		};
	}
	
	
	@Test(groups ={"SANITY"},dataProvider = "_deleteTrash")
	public void verify_User_DeleteFromTrash(String activityType, String objectType, String sheet, String severity, String logFile) throws Exception{
		Reporter.log("Validate User logged out ", true);
		String expectedMsg="User deleted email(s) "+sheet+" from Trash";
		replayLogsEPDV3(logFile);
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
		
	}
	
	@DataProvider
	public Object[][] _deleteTrash() {
		return new Object[][]{
			//Activity type  Object Type  Object Name  Severity  message
			{ "Delete", "Email","sheet","informational", "Email,Delete_from_trash.log"},
			
		};
		
	}
	
	
	@Test(groups ={"SANITY"},dataProvider = "_deleteEmail")
	public void verify_User_DeleteEmail(String activityType, String objectType, String fileName, String severity, String logFile) throws Exception{
		Reporter.log("Validate User deleted email(s) ", true);
		String expectedMsg="User deleted email(s) "+fileName;
		replayLogsEPDV3(logFile);
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
		
	}
	
	@DataProvider
	public Object[][] _deleteEmail() {
		return new Object[][]{
			//Activity type  Object Type  Object Name  Severity  message
			{ "Delete", "Email","DetectNewtest.java","informational", "Any,Gmail,DDelete,Delete.log"},
			
		};
	}
	
}
		
	