package com.elastica.beatle.tests.securlets;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

import com.elastica.beatle.DateUtils;
import com.elastica.beatle.MarshallingUtils;
import com.elastica.beatle.RawJsonParser;
import com.elastica.beatle.Retry;
import com.elastica.beatle.RetryAnalyzer;
import com.elastica.beatle.RestClient.ClientUtil;
import com.elastica.beatle.ciq.dto.ContentChecks;
import com.elastica.beatle.ciq.dto.ESResults;
import com.elastica.beatle.dci.DCIFunctions;
import com.elastica.beatle.i18n.I18N;
import com.elastica.beatle.logger.Logger;
import com.elastica.beatle.securlets.CIQValidator;
import com.elastica.beatle.securlets.DocumentValidator;
import com.elastica.beatle.securlets.ESQueryBuilder;
import com.elastica.beatle.securlets.LogUtils;
import com.elastica.beatle.securlets.LogValidator;
import com.elastica.beatle.securlets.SecurletUtils;
import com.elastica.beatle.securlets.SecurletsConstants;
import com.elastica.beatle.securlets.dto.ForensicSearchResults;
import com.elastica.beatle.securlets.dto.Hit;
import com.elastica.beatle.securlets.dto.MailAction;
import com.elastica.beatle.securlets.dto.O365Document;
import com.elastica.beatle.securlets.dto.MailRemediation;
import com.elastica.beatle.securlets.dto.SecurletDocument;
import com.elastica.beatle.securlets.dto.Source;
import com.gargoylesoftware.htmlunit.util.UrlUtils;
import com.google.api.services.gmail.model.Message;
import com.universal.common.Office365MailActivities;
import com.universal.constants.CommonConstants;
import com.elastica.beatle.securlets.SecurletUtils.*;

import microsoft.exchange.webservices.data.core.enumeration.service.DeleteMode;
import microsoft.exchange.webservices.data.core.exception.service.local.ServiceLocalException;
import microsoft.exchange.webservices.data.core.service.item.Item;
import microsoft.exchange.webservices.data.property.complex.EmailAddress;
import microsoft.exchange.webservices.data.property.complex.EmailAddressCollection;

import org.testng.asserts.SoftAssert;


import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;





/**
 * @author Nissar
 *
 */
public class Office365MailSecurletTests extends SecurletUtils {
	ESQueryBuilder esQueryBuilder = null;
	LogValidator logValidator;
	CIQValidator ciqValidator;
	DocumentValidator docValidator;
	Office365MailActivities objMailSysAdmin;
	Office365MailActivities objMailTestUser1;
	Office365MailActivities objMailTestUser2;
	Office365MailActivities objMailExternalUser;
	private String destinationFile;
	private String renamedFile;
	private String query;
	ArrayList<String> messages = new ArrayList<String>();
	ArrayList<String> actualMailMessages1 = new ArrayList<String>();
	ArrayList<String> actualMailMessages2 = new ArrayList<String>();
	ArrayList<String> actualMailMessages3 = new ArrayList<String>();
	ArrayList<String> actualMailMessages4 = new ArrayList<String>();
	ArrayList<String> actualMailMessages5 = new ArrayList<String>();
	ArrayList<String> actualMailMessages6 = new ArrayList<String>();
	ArrayList<String> cleanupListSent = new ArrayList<String>();
	ArrayList<String> cleanupListReceived = new ArrayList<String>();
	ArrayList<String> cleanupListDrafts = new ArrayList<String>();
	ArrayList<File> filesToDelete  = new ArrayList<File>();
	protected enum Remediation {ITEM_DELETE_ATTACHMENT, ITEM_DELETE_MAIL_BY_ATTACHMENT, ITEM_DELETE_MAIL};
	SoftAssert softAssert = new SoftAssert();

	String resourceId;

	//User related info
	private String sysAdminUserName;
	private String sysAdminUserPwd;
	private String adminMailId;
	private String groupMailId;
	private String testUser1;
	private String testUser2;
	private String groupUser1;
	private String groupUser2;
	private String groupUser1Pwd;
	private String groupUser2Pwd;
	private String testUser1Pwd;
	private String testUser2Pwd;
	private String externalUser1;
	private String externalUser1Pwd;
	private String externalUser2;
	private String externalUser2Pwd;
	

	// Unique Id used across testcases
	String uniqueId1;
	String uniqueId2;
	String uniqueId3;
	String uniqueId4;
	String uniqueId5;
	String uniqueId6;
	String uniqueId7;
	String userDir ;
	String fromTime ;

	//Filenames, mail subjects used across test cases
	private String newFileName1;
	private String newFileName2;
	private String newFileName3;
	private String newFileName4;
	private String newFileName5;
	private String mailSubject1;
	private String mailSubject2;
	private String mailSubject3;
	public static final long MAX_LOG_WAIT_TIME_IN_MINUTES 		= 25;
	public static final long MAX_EXPOSURE_WAIT_TIME_IN_MINUTES		= 25;
	public static final long MAX_REMEDIATION_WAIT_TIME_IN_MINUTES	= 25;
	public static final long EXPOSURE_WAIT_TIME_AFTER_REMEDIATION	= 600000;
	
	public static final long SANITY_MAX_LOG_WAIT_TIME_IN_MINUTES 		= 20;
	public static final long SANITY_MAX_EXPOSURE_WAIT_TIME_IN_MINUTES		= 12;
	public static final long SANITY_MAX_REMEDIATION_WAIT_TIME_IN_MINUTES	= 12;
	public static final long SANITY_EXPOSURE_WAIT_TIME_AFTER_REMEDIATION	= 300000;


	public Office365MailSecurletTests() throws Exception {
		super();
		//Generating Unique Id to use across test cases
		uniqueId1 = UUID.randomUUID().toString();
		uniqueId2 = UUID.randomUUID().toString();
		uniqueId3 = UUID.randomUUID().toString();
		uniqueId4 = UUID.randomUUID().toString();
		uniqueId5 = UUID.randomUUID().toString();
		uniqueId6 = UUID.randomUUID().toString();
		uniqueId7 = UUID.randomUUID().toString();
		
		esQueryBuilder = new ESQueryBuilder();
		logValidator = new LogValidator();
		docValidator = new DocumentValidator();
		
		//super.initSuiteConfigurations(suiteConfigurations, suiteData);
		
		//setting upload file path
		userDir = System.getProperty("user.dir")+"/src/test/resources/uploads/office365/";
		
		fromTime=DateUtils.getCurrentTime();
		
		
	}
	
	@BeforeClass(alwaysRun=true)
	public void setup() throws Exception {
		
			Logger.info("BeforeClass: Initializing saas app login");
	
			//User Credentials from suite file
			sysAdminUserName =  suiteData.getSaasAppUsername();		
			sysAdminUserPwd = suiteData.getSaasAppPassword();
			
			adminMailId = sysAdminUserName;
			testUser1 = suiteData.getSaasAppEndUser1Name();
			testUser1Pwd = suiteData.getSaasAppEndUser1Password();

			testUser2 = suiteData.getSaasAppEndUser2Name();
			testUser2Pwd = suiteData.getSaasAppEndUser2Password();
			
			externalUser1 =  suiteData.getSaasAppExternalUser1Name();
			
			externalUser2=suiteData.getSaasAppExternalUser2Name();
			externalUser2Pwd=suiteData.getSaasAppExternalUser2Password();
			
			groupMailId=suiteData.getSaasAppGroupMailId();
			//creating mail object with admin user credentials
			objMailSysAdmin = new Office365MailActivities(sysAdminUserName,sysAdminUserPwd); 

			//creating mail object with testuser1 credentials
			objMailTestUser1 = new Office365MailActivities(testUser1,testUser1Pwd); 
			objMailTestUser2 = new Office365MailActivities(testUser2,testUser2Pwd);
			objMailExternalUser = new Office365MailActivities(externalUser2,externalUser2Pwd); 
			
			
	}
	
	@AfterClass(alwaysRun=true)
	public void cleanup() throws Exception {

			Reporter.log("Inside cleanup",true);

			deleteFiles(filesToDelete);
			deleteMails(cleanupListSent,"SentItems",testUser1,testUser1Pwd);
			deleteMails(cleanupListReceived,"Inbox",testUser1,testUser1Pwd);
			deleteMails(cleanupListDrafts,"Drafts",testUser1,testUser1Pwd);
			deleteMails(cleanupListSent,"Inbox",testUser2,testUser2Pwd);
			
			
	}
	
	//This method performs different mail operations listed in the description part below
	@Test( groups={"MAIL1","P1"})
	public void performMailActivities() throws Exception {

		Reporter.log("****************Test Case Description****************",true);
		Reporter.log("Test Case Description:This testcase is doing following mail operations. The verification of logs are done in the testcases starting with verifyMail..  ",true);
		Reporter.log("1)Send Mail",true);
		Reporter.log("2)Receive Mail",true);
		Reporter.log("3)Send Mail to external recepient",true);
		Reporter.log("4)Send Mail with attachment",true);
		Reporter.log("5)Forward Mail",true);
		Reporter.log("6)Delete Mail from inbox",true);
		Reporter.log("7)Save mail in Drafts",true);
		Reporter.log("8)Send Mail to external and internal recepient together",true);
		Reporter.log("*****************************************************",true);
		
		String subject = null;
		boolean success = false;
		//send mail to check following things
		//C702678 Send an Email to anyone and verify that activity is recorded. 
		subject = uniqueId1+"Mail Without Attachment";
		cleanupListSent.add(subject);
		synchronized(this){ success = objMailTestUser1.sendMail(testUser2,subject, "This is test mail body",null, true);}
		assertTrue(success, "Failed sending mail with subject:"+subject+".");
		success=false;
		
		subject = uniqueId1+"MailToSelf";
		cleanupListReceived.add(subject);
		cleanupListSent.add(subject);
		synchronized(this){ success = objMailTestUser1.sendMail(testUser1,subject, "This is test mail body",null, true);}
		assertTrue(success, "Failed sending mail with subject:"+subject+".");
		success=false;
	
		//send mail to external recipient
		subject = uniqueId1+"MaiToExternal";
		cleanupListSent.add(subject);
		synchronized(this){ success = objMailTestUser1.sendMail(externalUser1,subject , "This is test mail body",null, true);}
		assertTrue(success, "Failed sending mail with subject:"+subject+".");
		success=false;

		//send mail to external and internal recipient
		subject = uniqueId1+"MaiToExternalAndInternal";
		cleanupListSent.add(subject);
		EmailAddressCollection emailIds = new EmailAddressCollection();
		emailIds.add(testUser2);
		emailIds.add(externalUser1);	
		synchronized(this){ success = objMailTestUser1.sendMail(emailIds,subject , "This is test mail body",null, true);}
		assertTrue(success, "Failed sending mail with subject:"+subject+".");
		success=false;

		//C702682 Send an Email to any internal user and verify that activity is recorded. 
		//send mail with attachment
		subject = uniqueId1+"Mail With Attachment";
		cleanupListSent.add(subject);
		ArrayList<String> myAttachment = new ArrayList<String>() ;
		myAttachment.add(userDir +"textFile.txt");
		synchronized(this){ success = objMailTestUser1.sendMail(testUser2,subject, "This is test mail body", myAttachment, true);}
		assertTrue(success, "Failed sending mail with subject:"+subject+".");
		success=false;


		//save mail in draft
		//C702684	Save an Email to Draft and verify if the activity has been recorded.
		subject =uniqueId1+"MailSavedInDraft";
		synchronized(this){ success = objMailTestUser1.saveToDraft(subject, "This is test mail body");}
		assertTrue(success, "Failed saving mail in draft with subject:"+subject+".");
		success=false;
		cleanupListDrafts.add(subject);
		
		//Forward mail test
		//Send a mail as admin to testuser1 and Forward that mail
		//check mail received
		//C878691 Verify the activity for receiving an email should be recorded.  

		subject =uniqueId1+"MailForForwardAndDelete";
		cleanupListReceived.add(subject);
		synchronized(this){ success = objMailSysAdmin.sendMail(testUser1, subject, "This is test mail body",null, false);}
		assertTrue(success, "Failed sending mail with subject:"+subject+".");
		success=false;
		Thread.sleep(CommonConstants.THIRTY_SECONDS_SLEEP);
		
		synchronized(this){ success = objMailTestUser1.forwardMail( subject,testUser2);}
		assertTrue(success, "Failed forwarding mail with subject:"+subject+".");
		success=false;
		Thread.sleep(CommonConstants.THIRTY_SECONDS_SLEEP);
		cleanupListSent.add("FW: "+subject);
		
		Reporter.log("Waiting for 5 minutes before deleting the email",true );
//		Thread.sleep(180000);
		int waitedTime =300000;
		Thread.sleep(waitedTime);
		

		//Delete mail
		//C702687	Delete an Email and verify that the activity has been logged.
		//C702688	Delete an Email and verify that it has been removed from mail box and
		DeleteMode delMode = DeleteMode.MoveToDeletedItems;		
		synchronized(this){success =  objMailTestUser1.deleteMail(subject,delMode );}
		assertTrue(success, "Failed deleting mail with subject:"+subject+".");
		success=false;
		

		
		
		actualMailMessages1.clear();
		ArrayList<String> logs;
		try {
			for (int i = waitedTime; i <= ( MAX_LOG_WAIT_TIME_IN_MINUTES * 60 * 1000); i+=60000 ) {
				
				Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
				Reporter.log("------------------------------------------------------------------------",true );
				Reporter.log("Searching for logs after :"+TimeUnit.MILLISECONDS.toMinutes(i)+" minutes",true );
				Reporter.log("------------------------------------------------------------------------",true );
				
				logs = searchDisplayLogs(fromTime, 60, "Office 365","Object_type", "Email_Message", uniqueId1, suiteData.getUsername(), suiteData.getCSRFToken(), suiteData.getSessionID(),testUser1);
				actualMailMessages1.addAll(logs);
				
				Thread.sleep(2000);
				logs = searchDisplayLogs(fromTime, 60, "Office 365","Object_type", "Email_File_Attachment", uniqueId1, suiteData.getUsername(), suiteData.getCSRFToken(), suiteData.getSessionID(),testUser1);
				actualMailMessages1.addAll(logs);
				
				Thread.sleep(2000);
				logs = searchDisplayLogs(fromTime, 60, "Office 365","Object_type", "Email_File_Attachment", uniqueId1, suiteData.getUsername(), suiteData.getCSRFToken(), suiteData.getSessionID(),testUser2);
				actualMailMessages1.addAll(logs);
				
				Set<String> hs = new HashSet<>();
				hs.addAll(actualMailMessages1);
				actualMailMessages1.clear();
				actualMailMessages1.addAll(hs);
				
				displayMessageContent(actualMailMessages1);

				//Reporter.log("Actual file messages:" + actualMailMessages, true);
				if (actualMailMessages1.size() >= 10) {break;}
			}
		}
		catch(Exception e) {}

	}
	
	@Test(dependsOnMethods="performMailActivities", groups={"MAIL1","P1"})
	public void verifyMailSentWithoutAttachment_TR_702678() {
		Reporter.log("****************Test Case Description****************",true);
		Reporter.log("Mail actions are done in the dependent test - performMailActivities",true);
		Reporter.log("Test Rail 702678: Send an Email to anyone and verify that activity is recorded.", true);
		Reporter.log("Test Rail 702678: C702682 Send an Email to any internal user and verify that activity is recorded.", true);
		Reporter.log("*****************************************************",true);
		displayMessageContent(actualMailMessages1);
		String expectedLog = "User sent an email  to "+testUser2+" with subject \""+uniqueId1+"Mail Without Attachment\"";
		Reporter.log("Expected log:" + expectedLog, true);
		assertTrue(actualMailMessages1.contains(expectedLog), expectedLog + " not present in the logs.");
		
	}
	@Test(dependsOnMethods="performMailActivities", groups={"MAIL1","P1"})
	public void verifyMailToSelf_Ticket_27573() {
		Reporter.log("****************Test Case Description****************",true);
		Reporter.log("Mail actions are done in the dependent test - performMailActivities",true);
		Reporter.log("Test Rail 27573 Verify logs for email sent to self", true);
		Reporter.log("*****************************************************",true);
		displayMessageContent(actualMailMessages1);
		
		ArrayList<String> ExpectedLogs = new ArrayList<String>();
//		ExpectedLogs.add("User received an email  from "+testUser1+" with subject \""+uniqueId1+"MailToSelf\"");
		ExpectedLogs.add("User sent an email  with subject \""+uniqueId1+"MailToSelf\"");
	
		
		assertTrue(compareResult(ExpectedLogs, actualMailMessages1),"Test failed, expected log is not found.");
		
	}
	
//	@Test(dependsOnMethods="performMailActivities", groups={"MAIL1","P1"})
	public void verifyMailReceived_TR_878691() {
		Reporter.log("****************Test Case Description****************",true);
		Reporter.log("Mail actions are done in the dependent test - performMailActivities",true);
		Reporter.log("Test Rail 878691 Verify the activity for receiving an email should be recorded.", true);
		Reporter.log("*****************************************************",true);
		displayMessageContent(actualMailMessages1);
		
		String expectedLog = "User received an email  from "+testUser1+" with subject \""+uniqueId1+"MailToSelf\"";
		Reporter.log("Expected log:" + expectedLog, true);
		assertTrue(actualMailMessages1.contains(expectedLog), expectedLog + " not present in the logs.");
	}
	@Test(dependsOnMethods="performMailActivities", groups={"MAIL1","P1"})
	public void verifyMailSentExternalUser_TR_702683() {
		Reporter.log("****************Test Case Description****************",true);
		Reporter.log("Mail actions are done in the dependent test - performMailActivities",true);
		Reporter.log("Test Rail 702683	Send an Email to any External user and verify that activity is recorded.", true);
		Reporter.log("*****************************************************",true);
		displayMessageContent(actualMailMessages1);
		String expectedLog = "User sent an email  to "+externalUser1+" with subject \""+uniqueId1+"MaiToExternal\"";
		Reporter.log("Expected log:" + expectedLog, true);
		assertTrue(actualMailMessages1.contains(expectedLog), expectedLog + " not present in the logs.");
	}
	@Test(dependsOnMethods="performMailActivities", groups={"MAIL1","P1"})
	public void verifyMailSentExternalAndInternalUser_TR_702683() {
		Reporter.log("****************Test Case Description****************",true);
		Reporter.log("Mail actions are done in the dependent test - performMailActivities",true);
		Reporter.log("Test Rail 878782	Send an Email to Both External and Internal user and verify that activity is recorded.", true);
		Reporter.log("*****************************************************",true);
		displayMessageContent(actualMailMessages1);
		String expectedLog = "User sent an email  to 2 users  with subject \""+uniqueId1+"MaiToExternalAndInternal\"";
		Reporter.log("Expected log:" + expectedLog, true);
		assertTrue(actualMailMessages1.contains(expectedLog), expectedLog + " not present in the logs.");
	}
	@Test(dependsOnMethods="performMailActivities", groups={"MAIL1","P1"})
	public void verifyMailSentWithAttachment() {
		Reporter.log("****************Test Case Description****************",true);
		Reporter.log("Mail actions are done in the dependent test - performMailActivities",true);
		Reporter.log("Verify if send mail with attachment is appearing in logs", true);
		Reporter.log("*****************************************************",true);
		displayMessageContent(actualMailMessages1);
		
		softAssert = new SoftAssert();
		ArrayList<String> ExpectedLogs = new ArrayList<String>();
		ExpectedLogs.add("User sent an email  to "+testUser2+" with subject \""+uniqueId1+"Mail With Attachment\"");
		ExpectedLogs.add("User sent an email  to "+testUser2+" with subject \""+uniqueId1+"Mail With Attachment\" with attachment \"textFile.txt\"");
//		ExpectedLogs.add("User received an email  from "+testUser1+" with subject \""+uniqueId1+"Mail With Attachment\" with attachment \"textFile.txt\"");
		int i =1;
		for (String expectedLog : ExpectedLogs) {
			Reporter.log(i++ +") " +expectedLog, true);
			softAssert.assertTrue(actualMailMessages1.contains(expectedLog), expectedLog + " not present in the logs.");
			
		}
		softAssert.assertAll();
		
	}

	@Test(dependsOnMethods="performMailActivities", groups={"MAIL1","P1"})
	public void verifyMailForwarded() {
		Reporter.log("****************Test Case Description****************",true);
		Reporter.log("Mail actions are done in the dependent test - performMailActivities",true);
		Reporter.log("Verify if forward mail is appearing in logs", true);
		Reporter.log("*****************************************************",true);
		displayMessageContent(actualMailMessages1);
		String expectedLog = "User sent an email  to "+testUser2+" with subject \"FW: "+uniqueId1+"MailForForwardAndDelete\"";
		Reporter.log("Expected log:" + expectedLog, true);
		assertTrue(actualMailMessages1.contains(expectedLog), expectedLog + " not present in the logs.");
	}
	@Test(dependsOnMethods="performMailActivities", groups={"MAIL1","P1"})
	public void verifyMailDeleted() throws ServiceLocalException, Exception {
		//C702687	Delete an Email and verify that the activity has been logged.
		//C702688	Delete an Email and verify that it has been removed from mail box and
		Reporter.log("****************Test Case Description****************",true);
		Reporter.log("Mail actions are done in the dependent test - performMailActivities",true);
		Reporter.log("Test Rail:702687	Delete an Email and verify that the activity has been logged.", true);
		Reporter.log("Test Rail:702688	Delete an Email and verify that it has been removed from mail box", true);
		Reporter.log("*****************************************************",true);
		displayMessageContent(actualMailMessages1);
		
		String mailSubject = uniqueId1+"MailForForwardAndDelete";
		microsoft.exchange.webservices.data.core.service.item.Item mailFound;
		synchronized(this){ mailFound = objMailTestUser1.findItemInFolder(mailSubject, "Inbox");}
		
		assertEquals(mailFound, null, "Mail is not deleted from Inbox.");
		
		String expectedLog = "User deleted an email  with subject \""+mailSubject+"\"";
		Reporter.log("Expected log:" + expectedLog, true);
		assertTrue(actualMailMessages1.contains(expectedLog), expectedLog + " not present in the logs.");
	}
//	@Test(dependsOnMethods="performMailActivities", groups={"MAIL1"})
	public void verifyMailSavedInDraft_TR_702684() {
		Reporter.log("****************Test Case Description****************",true);
		Reporter.log("Mail actions are done in the dependent test - performMailActivities",true);
		Reporter.log("Test Rail:702684	Save an Email to Draft and verify if the activity has been recorded.", true);
		Reporter.log("*****************************************************",true);
		displayMessageContent(actualMailMessages1);
		String expectedLog = "User saved an email  in Drafts  with subject \""+uniqueId1+"MailSavedInDraft\"";
		Reporter.log("Expected log:" + expectedLog, true);
		assertTrue(actualMailMessages1.contains(expectedLog), expectedLog + " not present in the logs.");
	}
	
	@SuppressWarnings("null")
	@Test(groups={"PROD"})
	public void verifyMailActivities() throws ServiceLocalException, Exception {

		Reporter.log("****************Test Case Description****************",true);
		Reporter.log("Test Case Description:This testcase is doing following mail operations.",true);
		Reporter.log("1)Send Mail and verify the logs",true);
		Reporter.log("2)Receive Mail and verify the logs",true);
		Reporter.log("3)Send Mail with PCI in body and PII in attachment, verify that it has been caught by Content Inspection and appears in logs",true);
		Reporter.log("*****************************************************",true);

		String myUniqueId = UUID.randomUUID().toString();
		boolean success = false;


		//send mail to check following things
		//C702678 Send an Email to anyone and verify that activity is recorded. 
		//C878691 Verify the activity for receiving an email should be recorded.  
		//C702682 Send an Email to any internal user and verify that activity is recorded. 
		//send mail without attachment // check the receipt
		
		String subject = myUniqueId+"Mail Without Attachment";
//		cleanupListReceived.add(subject); // as test user sends to himself
		synchronized(this){success= objMailTestUser1.sendMail(testUser1,subject, "This is test mail body",null, false);}
		assertTrue(success, "Failed sending mail with subject:"+subject+".");
		success=false;

		String attachmentFile= "PII.rtf";
		String bodyFile= "PCI_Test.txt";
		String violationName1 ="PII";
		String violationName2 ="PCI";

		File sourceFile1 = new File(userDir +attachmentFile);
		String readFile = userDir  + bodyFile;


		String myMailSubject = myUniqueId+"CIViolationMailAttachmentAndBody";
		String newFileName = myUniqueId+attachmentFile;
//		cleanupListSent.add(myMailSubject);

		File destFile1 = new File(userDir +newFileName);

		//Creating file with unique id name for upload
		copyFileUsingFileChannels(sourceFile1, destFile1);
		filesToDelete.add(destFile1);
		ArrayList<String> myAttachment = new ArrayList<String>();
		myAttachment.add(destFile1.toString());

		String mailBody = readFile(readFile.toString(), Charset.defaultCharset());

		synchronized(this){ success = objMailTestUser1.sendMail(testUser2, myMailSubject,mailBody ,myAttachment , true);}
		assertTrue(success, "Failed sending mail with subject:"+myMailSubject+".");
		success=false;

		Thread.sleep(120000);

		//Forward mail test
		//Send a mail to self and Forward that mail

		ArrayList<String> myMailMessages = new ArrayList<String>();
		ArrayList<String> logs;
		int i = 180000;
		try {
			for (; i <= ( SANITY_MAX_LOG_WAIT_TIME_IN_MINUTES * 60 * 1000); i+=60000 ) {
				Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
				Reporter.log("------------------------------------------------------------------------",true );
				Reporter.log("Searching for logs after :"+TimeUnit.MILLISECONDS.toMinutes(i)+" minutes",true );
				Reporter.log("------------------------------------------------------------------------" ,true);
				logs = searchDisplayLogs(fromTime, 60, "Office 365","Object_type", "Email_Message", myUniqueId, suiteData.getUsername(), suiteData.getCSRFToken(), suiteData.getSessionID(),testUser1);
				myMailMessages.addAll(logs);
				logs = searchDisplayLogs(fromTime, 60, "Office 365","Object_type", "Email_File_Attachment", myUniqueId, suiteData.getUsername(), suiteData.getCSRFToken(), suiteData.getSessionID() ,testUser1);
				myMailMessages.addAll(logs);
//				logs = searchDisplayLogs(fromTime, 60, "Office 365","Activity_type", "Content Inspection", "File "+myUniqueId, suiteData.getUsername(), suiteData.getCSRFToken(), suiteData.getSessionID() ,testUser2);
//				myMailMessages.addAll(logs);
//				logs = searchDisplayLogs(fromTime, 60, "Office 365","Activity_type", "Content Inspection", "File "+myUniqueId, suiteData.getUsername(), suiteData.getCSRFToken(), suiteData.getSessionID() ,testUser1);
//				myMailMessages.addAll(logs);
				
//				logs = searchDisplayLogs(fromTime, 60, "Office 365","Activity_type", "Content Inspection", "Email file attachment "+myUniqueId, suiteData.getUsername(), suiteData.getCSRFToken(), suiteData.getSessionID() ,testUser2);
//				myMailMessages.addAll(logs);
//				logs = searchDisplayLogs(fromTime, 60, "Office 365","Activity_type", "Content Inspection", "Email message "+myUniqueId, suiteData.getUsername(), suiteData.getCSRFToken(), suiteData.getSessionID() ,testUser2);
//				myMailMessages.addAll(logs);
				logs = searchDisplayLogs(fromTime, 60, "Office 365","Activity_type", "Content Inspection", myUniqueId, suiteData.getUsername(), suiteData.getCSRFToken(), suiteData.getSessionID() ,testUser1);
				myMailMessages.addAll(logs);
				logs = searchDisplayLogs(fromTime, 60, "Office 365","Activity_type", "Content Inspection", myUniqueId, suiteData.getUsername(), suiteData.getCSRFToken(), suiteData.getSessionID() ,testUser2);
				myMailMessages.addAll(logs);
				
				Set<String> hs = new HashSet<>();
				hs.addAll(myMailMessages);
				myMailMessages.clear();
				myMailMessages.addAll(hs);

				displayMessageContent(myMailMessages);

				//Reporter.log("Actual file messages:" + actualMailMessages, true);
				if (myMailMessages.size() >= 4) {break;}
			}
		}
		catch(Exception e) {}

		Reporter.log("****************Test Case Description****************",true);
		Reporter.log("Test Case Description:This testcase is doing following mail operations.",true);
		Reporter.log("1)Send Mail and verify the logs",true);
		Reporter.log("2)Receive Mail and verify the logs",true);
		Reporter.log("3)Send Mail with PCI information in body and PII in attachment, verify that it has been caught by Content Inspection and appears in logs",true);
		Reporter.log("*****************************************************",true);
		displayMessageContent(myMailMessages);
		
		
		ArrayList<String> ExpectedLogs = new ArrayList<String>();
		softAssert = new SoftAssert();
		
		ExpectedLogs.add("User sent an email  to "+testUser2+" with subject \""+myMailSubject+"\"");
		ExpectedLogs.add("User sent an email  to "+testUser2+" with subject \""+myMailSubject+"\" with attachment \""+newFileName+"\"");
//		ExpectedLogs.add("User received an email  from "+testUser1+" with subject \""+myUniqueId+"Mail Without Attachment\"");
		ExpectedLogs.add("Email file attachment "+newFileName+" has risk(s) - "+violationName1);
		ExpectedLogs.add("Email message "+myMailSubject+" has risk(s) - "+violationName2);
		
		assertTrue(compareResult(ExpectedLogs, myMailMessages),"Test failed, expected log is not found even after waiting for "+TimeUnit.MILLISECONDS.toMinutes(i)+" minutes.");
		 


	}

	@DataProvider(parallel=true)
	public Object[][] dataProviderContentViolation() {
		return new Object[][]{
			//Filename   	     violation name   content location , TestRailNo      
			{ "PII.rtf", 	   	"PII" ,			"attachment",   "TestRailNo.702679 Send an Email having PII level information and verify it has been caught"  },
			{ "PCI_Test.txt", 	"PCI" , 		"attachment",   "TestRailNo.702680 Send an Email having PCI level information and verify it has been caught in Content Inspection"},
//			{ "Hello.java",     "Source Code", 	"attachment",   "TestRailNo.702681 Send an Email having Source Code level information and verify it has been "  },
			{ "hipaa.txt",      "PII, HIPAA", 	"attachment",   "TestRailNo.702685 Send an Email having multiple attachments having multiple risk type and verify it in logs."  },
//			{ "encryption.bin", "ENCRYPTION", 	"attachment",   "Send an Email having ENCRYPTION level information and verify it has been caught"  },
			{ "ferpa.pdf",     	"FERPA, GLBA", 	"attachment",   "Send an Email having FERPA level information and verify it has been caught"  },
			{ "glba.txt",     	"GLBA", 		"attachment",   "Send an Email having GLBA level information and verify it has been caught"  },
			{ "vba_macro.xls",  "VBA Macros", 	"attachment",   "Send an Email having VBA Macros level information and verify it has been caught"  },
			{ "virus.zip",   	"Virus / Malware", 	"attachment",   "Send an Email having virus  and verify it has been caught"  },
			{ "PII.rtf", 	   	"PII" ,			"body" ,	    "TestRailNo.878694 Send/Receive an Email having PII level information in the email body and verify it has been caught in Content Inspection." },
			{ "PCI_Test.txt",  	"PCI" , 		"body", 		"TestRailNo.878695 Send/Receive an Email having PCI level information in the email body and verify it has been caught in Content Inspection."  },
//			{ "Hello.java",     "Source Code", 	"body", 		"TestRailNo.918546 Send/Receive an Email having Source Code level information in the email body and verify it has been caught in Content Inspection."  },
			{ "hipaa.txt",     	"PII, HIPAA", 	"body",			"TestRailNo.878696 Send/Receive an Email having HIPPA level information in the email body and verify it has been caught in Content Inspection.\n TestRailNo.878699 Send/Receive an Email having multiple risk type in the email body and verify it in logs."  }
		};
	}

	
	


	@Test(  groups={"MAIL2","P2"})
	public void performSendMailWithContentViolation() throws IOException, InterruptedException {

		Reporter.log("****************Test Case Description****************",true);
		Reporter.log("This method sends  mails with attachment/body containing risk information "
				+ "The verification method checks if these violations are caught in by DCI ", true);
		Reporter.log("*****************************************************",true);

		String fileName = null;
		String testRailNo;
		String violationName;
		String myUniqueId ;
		String contentPlace;
		boolean success = false;
		//generating unique id
		myUniqueId = uniqueId2 ;
		String newFileName =null;
		File sourceFile1;
		File destFile1 = null;
		String mailBody=null;
		ArrayList<String> myAttachment = new ArrayList<String>() ;
		String myMailSubject=null;
		ArrayList<String> logs =null;
		ArrayList<String> logs2 =null;


		deleteAllCIQProfiles();
		
		Object[][] myFileList =dataProviderContentViolation();

		for (Object[] earchRow : myFileList) {

			try {
				myAttachment.clear();
				fileName=     earchRow[0].toString();
				violationName=earchRow[1].toString();
				contentPlace= earchRow[2].toString();
				testRailNo=   earchRow[3].toString();

				Reporter.log("Violation at:"+contentPlace+". Testrail:" +testRailNo,true);
				sourceFile1 = new File(userDir +fileName);

				if(contentPlace.equals("attachment")){

					myMailSubject = myUniqueId+"CIViolationMailAttachment"+fileName;
					newFileName = myUniqueId+fileName;

					destFile1 = new File(userDir +newFileName);

					//Creating file with unique id name for upload
					copyFileUsingFileChannels(sourceFile1, destFile1);
					filesToDelete.add(destFile1);
					myAttachment.add(destFile1.toString());
					mailBody= "This is test mail body";

				}
				else if(contentPlace.equals("body")){
					myMailSubject = myUniqueId+"CIViolationMailBody"+violationName;
					mailBody =readFile(sourceFile1.toString(), Charset.defaultCharset());
				}
				Thread.sleep(10000);
				cleanupListSent.add(myMailSubject);
				synchronized(this){ success = objMailTestUser1.sendMail(testUser2, myMailSubject,mailBody ,myAttachment , true);}
				assertTrue(success, "Failed sending mail with subject:"+myMailSubject+".");
				success=false;


			} catch (Exception e) {
				// TODO Auto-generated catch block
				Reporter.log(e.getMessage());
				e.printStackTrace();
			}


		}

		try {
			// C878783 Send/Receive an Email having both attachments and content based risks
			// verification at verifySendMailWithContentViolationBoth
			myAttachment.clear();
			mailBody= "This is test mail body";
			String attachmentFileName ="hipaa.txt";
			String bodyFileName ="PCI_Test.txt";
			myMailSubject = myUniqueId+"CIViolationMailInBodyAndAttachment";
			cleanupListSent.add(myMailSubject);
			sourceFile1 = new File(userDir +attachmentFileName);
			destFile1 = new File(userDir +myMailSubject+attachmentFileName);
			copyFileUsingFileChannels(sourceFile1, destFile1);
			filesToDelete.add(destFile1);
			Thread.sleep(20000);
			myAttachment.add(destFile1.toString());
			mailBody =readFile(userDir +bodyFileName, Charset.defaultCharset());
			synchronized(this){ success = objMailTestUser1.sendMail(testUser2, myMailSubject,mailBody ,myAttachment , true);}
			assertTrue(success, "Failed sending mail with subject:"+myMailSubject+".");
			success=false;
			actualMailMessages2.clear();

			Thread.sleep(120000);

			for (int i = 180000; i <= (MAX_LOG_WAIT_TIME_IN_MINUTES * 60 * 1000); i+=60000 ) {
				Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
				Reporter.log("Searching for logs after :"+TimeUnit.MILLISECONDS.toMinutes(i)+" minutes",true );
				Reporter.log("------------------------------------------------------------------------",true );
//				logs = searchDisplayLogs(fromTime, 60, "Office 365","Object_type", "Email_Message", myUniqueId, suiteData.getUsername(), suiteData.getCSRFToken(), suiteData.getSessionID() ,testUser1);
//				actualMailMessages2.addAll(logs);
//				Thread.sleep(3000);
//				logs = searchDisplayLogs(fromTime, 60, "Office 365","Object_type", "Email_File_Attachment", myUniqueId, suiteData.getUsername(), suiteData.getCSRFToken(), suiteData.getSessionID() ,testUser1);
//				actualMailMessages2.addAll(logs);
//				Thread.sleep(3000);
				logs2 = searchDisplayLogs(fromTime, 60, "Office 365","Activity_type", "Content Inspection", "Email file attachment "+myUniqueId, suiteData.getUsername(), suiteData.getCSRFToken(), suiteData.getSessionID() ,testUser1); // this is for virus file scanning in sent items
				actualMailMessages2.addAll(logs2);
				Thread.sleep(3000);
				logs2 = searchDisplayLogs(fromTime, 60, "Office 365","Activity_type", "Content Inspection", "Email file attachment "+myUniqueId, suiteData.getUsername(), suiteData.getCSRFToken(), suiteData.getSessionID() ,testUser2);
				actualMailMessages2.addAll(logs2);
				Thread.sleep(3000);
				logs = searchDisplayLogs(fromTime, 60, "Office 365","Activity_type", "Content Inspection", "Email message "+myUniqueId, suiteData.getUsername(), suiteData.getCSRFToken(), suiteData.getSessionID() ,testUser2);
				actualMailMessages2.addAll(logs);
				Thread.sleep(3000);
				logs = searchDisplayLogs(fromTime, 60, "Office 365","Activity_type", "Content Inspection", "Email message "+myUniqueId, suiteData.getUsername(), suiteData.getCSRFToken(), suiteData.getSessionID() ,testUser1);
				actualMailMessages2.addAll(logs);
				Set<String> hs = new HashSet<>();
				hs.addAll(actualMailMessages2);
				actualMailMessages2.clear();
				actualMailMessages2.addAll(hs);

				displayMessageContent(actualMailMessages2);
				if (actualMailMessages2.size() >= 15) {break;}  
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Reporter.log(e.getMessage());
			e.printStackTrace();
		}



	}
	
	

	@Test( dependsOnMethods="performSendMailWithContentViolation",groups={"MAIL2","P2"},dataProvider="dataProviderContentViolation")
	public void verifySendMailWithContentViolation(String fileName, String violationName ,String contentPlace, String testRailDetails ) {
		
		Reporter.log("****************Test Case Description****************",true);
		Reporter.log("Send Mail actions are done in the dependent test - performSendMailWithContentViolation",true);
		Reporter.log("This method checks the logs for Content Inspection Risk for risk files  sent in mail attachment or body "
				+ "Also verifies if the mail sending logs are generated ", true);
		Reporter.log(testRailDetails,true);
		Reporter.log("*****************************************************",true);
		
		String myUniqueId = uniqueId2 ;
		String myMailSubject =null, expectedLog =null; 
		ArrayList<String> ExpectedLogs = new ArrayList<String>();
		softAssert = new SoftAssert();
		
		try {
			displayMessageContent(actualMailMessages2);

			if(contentPlace.equals("attachment")){

		
				myMailSubject = myUniqueId+"CIViolationMailAttachment"+fileName;
				
				fileName=myUniqueId+fileName;

				//ExpectedLogs.add("User sent an email  to "+testUser2+" with subject \""+myMailSubject+"\" with attachment \""+fileName+"\"");
				ExpectedLogs.add("Email file attachment "+fileName+" has risk(s) - "+violationName);
			}
			else if(contentPlace.equals("body")){
				myMailSubject = myUniqueId+"CIViolationMailBody"+violationName;
				//ExpectedLogs.add("User sent an email  to "+testUser2+" with subject \""+myMailSubject+"\"");
				ExpectedLogs.add("Email message "+myMailSubject+" has risk(s) - "+violationName);
			}
			
			assertTrue(compareResult(ExpectedLogs, actualMailMessages2),"Test failed, expected log is not found.");
			 


		}
		catch(Exception e) {}

	}
	
	@Test( dependsOnMethods="performSendMailWithContentViolation",groups={"MAIL2","P2"})
	public void verifySendMailWithContentViolationInAttachmentAndBody( ) {
		
		Reporter.log("****************Test Case Description****************",true);
		Reporter.log("Send Mail actions are done in the dependent test - performSendMailWithContentViolation",true);
		Reporter.log("This method checks the logs for  content violation which was sent as part of mail body and attachment "
				+ "Also verifies if the mail sending logs are generated ", true);
		Reporter.log("Test Rail:878783 Send/Receive an Email having both attachments and content based risks",true);
		Reporter.log("*****************************************************",true);
		
		ArrayList<String> ExpectedLogs = new ArrayList<String>();
		
		String myUniqueId = uniqueId2 ;
		String attachmentFileName ="hipaa.txt";
		String bodyFileName ="PCI_Test.txt";
		String myMailSubject = myUniqueId+"CIViolationMailInBodyAndAttachment";
		String violationName ="PII, HIPAA";
		
		
		try {
			displayMessageContent(actualMailMessages2);

				
				String fileName=myMailSubject+attachmentFileName;

//				 expectedLog = "User sent an email  to "+testUser2+" with subject \""+myMailSubject+"\" with attachment \""+fileName+"\"";
//				Reporter.log("Expected log:" + expectedLog, true);
//				assertTrue(actualMailMessages2.contains(expectedLog), expectedLog + " not present in the logs.");

				
				//Reporter.log("Verify that "+violationName+" level information has been caught",true);
				ExpectedLogs.add("Email file attachment "+fileName+" has risk(s) - "+violationName);
			
			
				
				//Reporter.log("Verify that "+violationName+" level information has been caught",true);
				ExpectedLogs.add("Email message "+myMailSubject+" has risk(s) - PCI");

			
				assertTrue(compareResult(ExpectedLogs, actualMailMessages2),"Test failed, expected log is not found.");

		}
		catch(Exception e) {}

	}
	

	
	//@Test(  groups={"MAIL3"})
	public void performSaveMailInDraftWithContentViolationAttachmentMultiple( ) throws Exception {

		Reporter.log("****************Test Case Description****************",true);
		Reporter.log("This method saves a mail in draft which has multiple attachment containing PII and PCI. "
				+ "The verification method checks if these violations are caught in by DCI ", true);
		Reporter.log("*****************************************************",true);
		
		String myUniqueId = uniqueId3;
		ArrayList<String> logs =null;
		ArrayList<String> logs2 =null;
		boolean success = false;

		mailSubject2 = myUniqueId+"InDraftWithContentViolationInAttachment";
		cleanupListSent.add(mailSubject2);
		String fileName1 = "PII.rtf";
		newFileName1 = myUniqueId+fileName1;

		String fileName2 = "PCI_Test.txt";
		newFileName2 = myUniqueId+fileName2;

		File sourceFile1 = new File(userDir +fileName1);
		File destFile1 = new File(userDir +newFileName1);

		File sourceFile2 = new File(userDir +fileName2);
		File destFile2 = new File(userDir +newFileName2);

		//Creating file with unique id name for upload
		copyFileUsingFileChannels(sourceFile1, destFile1);
		copyFileUsingFileChannels(sourceFile2, destFile2);
		filesToDelete.add(destFile1);
		filesToDelete.add(destFile2);


		//save mail with attachment
		ArrayList<String> myAttachment = new ArrayList<String>() ;
		myAttachment.add(destFile1.toString());
		myAttachment.add(destFile2.toString());
		synchronized(this){ success = objMailTestUser1.addAttachmentAndSaveInDraft(mailSubject2, "This is test mail body", myAttachment);}
		assertTrue(success, "Failed to save mail in draft with subject:"+mailSubject2+".");
		success=false;

		actualMailMessages3.clear();
		
		try {
			for (int i = 180000; i <= (MAX_LOG_WAIT_TIME_IN_MINUTES * 60 * 1000); i+=60000 ) {
				Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
				Reporter.log("Searching for logs after :"+TimeUnit.MILLISECONDS.toMinutes(i)+" minutes",true );
				Reporter.log("------------------------------------------------------------------------",true );
				logs = searchDisplayLogs(fromTime, 60, "Office 365","Object_type", "Email_File_Attachment", myUniqueId, suiteData.getUsername(), suiteData.getCSRFToken(), suiteData.getSessionID(),testUser1);
				logs2 = searchDisplayLogs(fromTime, 60, "Office 365","Activity_type", "Content Inspection", "Email file attachment "+myUniqueId, suiteData.getUsername(), suiteData.getCSRFToken(), suiteData.getSessionID(), testUser1 );
				actualMailMessages3.addAll(logs);
				actualMailMessages3.addAll(logs2);

				Set<String> hs = new HashSet<>();
				hs.addAll(actualMailMessages3);
				actualMailMessages3.clear();
				actualMailMessages3.addAll(hs);

				displayMessageContent(actualMailMessages3);
						
				//Reporter.log("Actual file messages:" + actualMailMessages, true);
				if (actualMailMessages3.size() >= 4) {break;}
			}
		}
		catch(Exception e) {}


	}
	
//	@Test( dependsOnMethods="performSaveMailInDraftWithContentViolationAttachmentMultiple", groups={"MAIL3"})
	public void veriftySaveMailWithContentViolationAttachmentMultiple() {
		
		Reporter.log("****************Test Case Description****************",true);
		Reporter.log("Mail actions are done in the dependent test - performSaveMailInDraftWithContentViolationAttachmentMultiple",true);
		Reporter.log("This method checks the logs for  voilations of  PII, PCI in saved mail attachment. "
				+ "Also verifies if the mail saving logs are generated ", true);
		Reporter.log("*****************************************************",true);
		displayMessageContent(actualMailMessages3);

		Reporter.log("Save an Email to Draft and verify if the activity has been recorded.", true);
		String expectedLog = "User saved an email  in Drafts  with subject \""+mailSubject2+"\" with attachment \""+newFileName1+"\"";
		Reporter.log("Expected log:" + expectedLog, true);
		assertTrue(actualMailMessages3.contains(expectedLog), expectedLog + " not present in the logs.");

		expectedLog = "User saved an email  in Drafts  with subject \""+mailSubject2+"\" with attachment \""+newFileName2+"\"";
		Reporter.log("Expected log:" + expectedLog, true);
		assertTrue(actualMailMessages3.contains(expectedLog), expectedLog + " not present in the logs.");

		expectedLog = "Email file attachment "+newFileName1+" has risk(s) - PII";
		Reporter.log("Expected log:" + expectedLog, true);
		assertTrue(actualMailMessages3.contains(expectedLog), expectedLog + " not present in the logs.");

		expectedLog = "Email file attachment "+newFileName2+" has risk(s) - PCI";
		Reporter.log("Expected log:" + expectedLog, true);
		assertTrue(actualMailMessages3.contains(expectedLog), expectedLog + " not present in the logs.");


	}

	@Test(  groups={"MAIL4","P1"})
	public void performSendMailWithContentViolationAttachmentMultiple_TR_702685( ) throws IOException, InterruptedException {
		
		Reporter.log("****************Test Case Description****************",true);
		Reporter.log("This method sends a mail which has multiple attachment containing PII and PCI. "
				+ "The verification method checks if these violations are caught in by DCI ", true);
		Reporter.log("*****************************************************",true);
		

		String myUniqueId = uniqueId4;
		ArrayList<String> logs =null;
		ArrayList<String> logs2 =null;
		boolean success = false;

		mailSubject3 = myUniqueId+"MailSentWithContentViolationInAttachment";
		String fileName1 = "PII.rtf";
		newFileName4 = myUniqueId+fileName1;

		String fileName2 = "PCI_Test.txt";
		newFileName5 = myUniqueId+fileName2;

		File sourceFile1 = new File(userDir +fileName1);
		File destFile1 = new File(userDir +newFileName4);

		File sourceFile2 = new File(userDir +fileName2);
		File destFile2 = new File(userDir +newFileName5);

		//Creating file with unique id name for upload
		copyFileUsingFileChannels(sourceFile1, destFile1);
		copyFileUsingFileChannels(sourceFile2, destFile2);
		filesToDelete.add(destFile1);
		filesToDelete.add(destFile2);


		//save mail with attachment
		cleanupListSent.add(mailSubject3);
		ArrayList<String> myAttachment = new ArrayList<String>() ;
		myAttachment.add(destFile1.toString());
		myAttachment.add(destFile2.toString());
		synchronized(this){success= objMailTestUser1.sendMail(testUser2, mailSubject3, "This is test mail body", myAttachment,true);}
		assertTrue(success, "Failed sending mail with subject:"+mailSubject2+".");
		success=false;

		actualMailMessages4.clear();	
		Thread.sleep(120000);
		try {
			for (int i = 180000; i <= (MAX_LOG_WAIT_TIME_IN_MINUTES * 60 * 1000); i+=60000 ) {
				Reporter.log("Searching for logs after :"+TimeUnit.MILLISECONDS.toMinutes(i)+" minutes",true );
				Reporter.log("------------------------------------------------------------------------",true );
				Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
				logs = searchDisplayLogs(fromTime, 60, "Office 365","all_messages", null, myUniqueId, suiteData.getUsername(), suiteData.getCSRFToken(), suiteData.getSessionID(),testUser1 );
//				logs2 = searchDisplayLogs(fromTime, 60, "Office 365","Activity_type", "Content Inspection", "Email file attachment "+myUniqueId, suiteData.getUsername(), suiteData.getCSRFToken(), suiteData.getSessionID(),testUser2 );
				actualMailMessages4.addAll(logs);
//				actualMailMessages4.addAll(logs2);

				Set<String> hs = new HashSet<>();
				hs.addAll(actualMailMessages4);
				actualMailMessages4.clear();
				actualMailMessages4.addAll(hs);

				displayMessageContent(actualMailMessages4);
				//Reporter.log("Actual file messages:" + actualMailMessages, true);
				if (actualMailMessages4.size() >= 5) {break;}
			}
		}
		catch(Exception e) {}


	}

	@Test(dependsOnMethods="performSendMailWithContentViolationAttachmentMultiple_TR_702685", groups={"MAIL4","P1"})
	public void verifySendMailWithContentViolationAttachmentMultiple_TR_702685() {
		

		Reporter.log("****************Test Case Description****************",true);
		Reporter.log("Mail actions are done in the dependent test - performSendMailWithContentViolationAttachmentMultiple_TR_702685",true);
		Reporter.log("This method checks the logs for  voilations of  PII, PCI in sent mail attachment. "
				+ "Also verifies if the mail sending logs are generated ", true);
		Reporter.log("Test Rail:702685	Send an Email having multiple attachments having multiple risk type and verify it in logs.",true);
		Reporter.log("*****************************************************",true);

		displayMessageContent(actualMailMessages4);
		
		ArrayList<String> ExpectedLogs = new ArrayList<String>();
		softAssert = new SoftAssert();
		
		
		
//		Reporter.log("Send an Email having multiple attachments having multiple risk type and verify it in logs", true);
		ExpectedLogs.add("User sent an email  to "+testUser2+" with subject \""+mailSubject3+"\"");
		ExpectedLogs.add("User sent an email  to "+testUser2+" with subject \""+mailSubject3+"\" with attachment \""+newFileName4+"\"");
		ExpectedLogs.add("User sent an email  to "+testUser2+" with subject \""+mailSubject3+"\" with attachment \""+newFileName5+"\"");
//		ExpectedLogs.add("User received an email  from "+testUser1+" with subject \""+mailSubject3+"\"");
//		ExpectedLogs.add("User received an email  from "+testUser1+" with subject \""+mailSubject3+"\" with attachment \""+newFileName4+"\"");
//		ExpectedLogs.add("User received an email  from "+testUser1+" with subject \""+mailSubject3+"\" with attachment \""+newFileName5+"\"");
		ExpectedLogs.add("Email file attachment "+newFileName4+" has risk(s) - PII");
		ExpectedLogs.add("Email file attachment "+newFileName5+" has risk(s) - PCI");
		
		assertTrue(compareResult(ExpectedLogs, actualMailMessages4),"Test failed as some of the expected logs are not found.");


	}
	
	
//	@Test(groups={"PROD"})
//	public void verifyExposureAndRemediationDeleteMailByAttachment() throws Exception
//	{
//		verifyRemediationForOfficeMailExposure("true", 	"open", 	"pii", 			"PII.rtf", 	   		"PII" ,		"attachment",   "ITEM_DELETE_MAIL_BY_ATTACHMENT", 		"Send an Email having PII level information and verify it in exposure and remediate"  );
//		
//	}
	@Test(groups={"PROD","P1"})
	public void verifyMailExposureAndRemediationDeleteMail() throws Exception
	{
		verifyRemediationForOfficeMailExposure("sanity", "true", 	"open", 	"pii", 			"PII.rtf", 	   		"PII" ,		"body" ,	    "ITEM_DELETE_MAIL", 					"Send/Receive an Email having PII level information in the email body and verify it in exposure and remediate" );
		
	}
	@Test(groups={"PROD","P1"})
	public void verifyMailExposureAndRemediationDeleteAttachment() throws Exception
	{
		verifyRemediationForOfficeMailExposure("sanity", "true", 	"open", 	"pii", 			"PII.rtf", 	   		"PII" ,		"attachment",   "ITEM_DELETE_ATTACHMENT", 		"Send an Email having PII level information and verify it in exposure and remediate" );
		
	}
	  
	  @DataProvider(parallel=true)
		public Object[][] dataProviderExposureAndRemediation() {
			return new Object[][]{
			/* is_internal  
			 * exposure type 
			 * violation type for API
			 * filename
			 * violation name for log comparison
			 * content location
			 * remedial action 
			 * test description
			 *    
			 *  */
				
				{"regression", "true", 	"open", 	"pii", 			"PII.rtf", 	   		"PII" ,						"attachment",   "ITEM_DELETE_MAIL_BY_ATTACHMENT", 		"Send an Email having PII level information and verify it in exposure and remediate"  },
				{"regression", "true", 	"open",		"pci", 			"PCI_Test.txt", 	"PCI" , 					"attachment",   "ITEM_DELETE_MAIL_BY_ATTACHMENT", 		"Send an Email having PCI level information and verify it in exposure and remediate"},
//				{"regression", "true", 	"open", 	"source_code",	"Hello.java",     	"Source Code", 				"attachment",   "ITEM_DELETE_MAIL_BY_ATTACHMENT", 		"Send an Email having Source Code level information"  },
				{"regression", "true", 	"open", 	"hipaa", 		"hipaa.txt",      	"HIPAA", 					"attachment",   "ITEM_DELETE_MAIL_BY_ATTACHMENT", 		"Send an Email having HIPAA level information and verify it in exposure and remediate"  },
//				{"regression", "true", 	"open", 	"encryption", 	"encryption.bin", 	"ENCRYPTION", 				"attachment",   "ITEM_DELETE_MAIL_BY_ATTACHMENT", 		"Send an Email having ENCRYPTION level information and verify it in exposure and remediate"  },
				{"regression", "true", 	"open", 	"ferpa", 		"ferpa.pdf",     	"FERPA", 					"attachment",   "ITEM_DELETE_MAIL_BY_ATTACHMENT", 		"Send an Email having FERPA level information and verify it in exposure and remediate"  },
				{"regression", "true", 	"open", 	"glba", 		"glba.txt",     	"GLBA", 					"attachment",   "ITEM_DELETE_MAIL_BY_ATTACHMENT", 		"Send an Email having GLBA level information and verify it in exposure and remediate"  },
				{"regression", "true", 	"open", 	"vba_macros", 	"vba_macro.xls", 	"VBA Macros, Source Code", 	"attachment",   "ITEM_DELETE_MAIL_BY_ATTACHMENT", 		"Send an Email having VBA Macros level information and verify it in exposure and remediate"  },
				{"regression", "true", 	"open", 	"virus", 		"virus.zip",   		"Virus / Malware", 			"attachment",   "ITEM_DELETE_MAIL_BY_ATTACHMENT", 		"Send an Email having virus  and verify it in exposure and remediate"  },
				{"regression", "true", 	"open", 	"pii", 			"PII.rtf", 	   		"PII" ,						"body" ,	    "ITEM_DELETE_MAIL", 					"Send/Receive an Email having PII level information in the email body and verify it in exposure and remediate" },
				{"regression", "true", 	"open", 	"pci", 			"PCI_Test.txt",  	"PCI" , 					"body", 		"ITEM_DELETE_MAIL", 					"Send/Receive an Email having PCI level information in the email body and verify it in exposure and remediate"  },
//				{"regression", "true", 	"open", 	"source_code",	"Hello.java",     	"Source Code", 				"body", 		"ITEM_DELETE_MAIL", 					"Send/Receive an Email having Source Code level information in the email body and verify it in exposure and remediate"  },
				{"regression", "true", 	"open", 	"hipaa", 		"hipaa.txt",     	"HIPAA", 					"body",			"ITEM_DELETE_MAIL", 					"Send/Receive an Email having HIPPA level information in the email body and verify it in exposure and remediate"  },
				{"regression", "true", 	"open", 	"pii", 			"PII.rtf", 	   		"PII" ,						"attachment",   "ITEM_DELETE_ATTACHMENT", 		"Send an Email having PII level information and verify it in exposure and remediate"  },
				{"regression", "true", 	"open",		"pci", 			"PCI_Test.txt", 	"PCI" , 					"attachment",   "ITEM_DELETE_ATTACHMENT", 		"Send an Email having PCI level information and verify it in exposure and remediate"},
//				{"regression", "true", 	"open", 	"source_code",	"Hello.java",     	"Source Code", 				"attachment",   "ITEM_DELETE_ATTACHMENT", 		"Send an Email having Source Code level information"  },
				{"regression", "true", 	"open", 	"hipaa", 		"hipaa.txt",      	"HIPAA", 					"attachment",   "ITEM_DELETE_ATTACHMENT", 		"Send an Email having HIPAA level information and verify it in exposure and remediate"  },
//				{"regression", "true", 	"open", 	"encryption", 	"encryption.bin", 	"ENCRYPTION", 				"attachment",   "ITEM_DELETE_ATTACHMENT", 		"Send an Email having ENCRYPTION level information and verify it in exposure and remediate"  },
				{"regression", "true", 	"open", 	"ferpa", 		"ferpa.pdf",     	"FERPA", 					"attachment",   "ITEM_DELETE_ATTACHMENT", 		"Send an Email having FERPA level information and verify it in exposure and remediate"  },
				{"regression", "true", 	"open", 	"glba", 		"glba.txt",     	"GLBA", 					"attachment",   "ITEM_DELETE_ATTACHMENT", 		"Send an Email having GLBA level information and verify it in exposure and remediate"  },
				{"regression", "true", 	"open", 	"vba_macros", 	"vba_macro.xls", 	"VBA Macros, Source Code", 	"attachment",   "ITEM_DELETE_ATTACHMENT", 		"Send an Email having VBA Macros level information and verify it in exposure and remediate"  },
				{"regression", "true", 	"open", 	"virus", 		"virus.zip",   		"Virus / Malware", 			"attachment",   "ITEM_DELETE_ATTACHMENT", 		"Send an Email having virus  and verify it in exposure and remediate"  }
			};
		}
	    
		/**
		 * Send an email with content violation
		 * After that check with  remediation api (UI call/API call), apply the remedial actions and check file exposure changed or not.
		 * @param isInternal
		 * @param exposuretype
		 * @param violationType
		 * @param fileName
		 * @param violationName
		 * @param contentPlace
		 * @param remedialAction
		 * @param testDescription
		 * @throws Exception
		 */
		@Test(groups={"MAIL4","P2"},dataProvider = "dataProviderExposureAndRemediation")
		public void verifyRemediationForOfficeMailExposure(
				String testType, 
				String isInternal, 
				String exposuretype,
				String violationType, 
				String fileName,
				String violationName,
				String contentPlace, 
				String remedialAction, 
				String testDescription
				) throws Exception {
			
			Reporter.log("****************Test Case Description****************",true);
			Reporter.log("Test Case Description: Send an email with content violation "+violationName+" in mail "+contentPlace+" to external user. Verify the exposure and remediate the same, ",true);
			Reporter.log("1)Send Mail with "+violationName+" information to external user",true);
			Reporter.log("2)Verify that the mail/attachment is exposed and remediate the same using "+remedialAction,true);
			Reporter.log("3)Verify that the remediation is successful and mail/attachment is deleted from sentItems folder of sender",true);
			Reporter.log("4)Verify that the remediation message also appears in the logs",true);
			Reporter.log("*****************************************************",true);
			

			//Prepare the remediation object
			microsoft.exchange.webservices.data.core.service.item.Item emailObj = null;
			String myUniqueId = UUID.randomUUID().toString();
			ArrayList<String> myAttachment = new ArrayList<String>() ;
			String newFileName = myUniqueId+fileName;
			File sourceFile1 =null;
			File destFile1 = null;
			String myMailSubject =null;
			String mailBody = null;
			boolean success =false;
			String docType = null;
			String searchFileName =null;
			int countAfterExposure =0;
			int responseCode = 0;
			String expectedLog = null;
			
			long maxLogWaitTimeInMinutes =0,maxExposureWaitTimeInMinutes=0,	maxRemediationWaitTimeInMinutes=0,exposureWaitTimeAfterRemediation=0;

			if(testType.equals("sanity")){
				maxLogWaitTimeInMinutes = SANITY_MAX_LOG_WAIT_TIME_IN_MINUTES;
				maxExposureWaitTimeInMinutes = SANITY_MAX_EXPOSURE_WAIT_TIME_IN_MINUTES;
				maxRemediationWaitTimeInMinutes = SANITY_MAX_REMEDIATION_WAIT_TIME_IN_MINUTES;
				exposureWaitTimeAfterRemediation = SANITY_EXPOSURE_WAIT_TIME_AFTER_REMEDIATION;
			}
			else{
				maxLogWaitTimeInMinutes = MAX_LOG_WAIT_TIME_IN_MINUTES;
				maxExposureWaitTimeInMinutes = MAX_EXPOSURE_WAIT_TIME_IN_MINUTES;
				maxRemediationWaitTimeInMinutes = MAX_REMEDIATION_WAIT_TIME_IN_MINUTES;
				exposureWaitTimeAfterRemediation = EXPOSURE_WAIT_TIME_AFTER_REMEDIATION;
			}
			
			//Prepare the violations
			ArrayList<String> violations = new ArrayList<String>();
			violations.add(violationType);
			

			//creating mail object with testuser1 credentials
			Office365MailActivities objMailTestUser1New ; 
			synchronized(this){objMailTestUser1New = new Office365MailActivities(testUser1,testUser1Pwd); }

			//Get the exposure count
			O365Document o365Document =null;
//			o365Document = getExposures(isInternal, suiteData.getUsername(), violations,newFileName);
//			int beforeCount = o365Document.getMeta().getTotalCount();
			//
//			Reporter.log("######" + violationType + " exposure count before test::"+beforeCount, true);

			sourceFile1 = new File(userDir +fileName);
			destFile1 = new File(userDir +newFileName);


			if(contentPlace.equals("attachment")){

				docType = "Email_File_Attachment";
				myMailSubject = myUniqueId+"ViolationForExposureInAttachment"+fileName;
				searchFileName=newFileName;

				//Creating file with unique id name for upload
				copyFileUsingFileChannels(sourceFile1, destFile1);
				filesToDelete.add(destFile1);
				myAttachment.add(destFile1.toString());
				mailBody= "This is test mail body";

			}
			else if(contentPlace.equals("body")){
				docType = "Email_Message";
				myMailSubject = myUniqueId+"ViolationForExposureInMailBody"+violationType;
				mailBody =readFile(sourceFile1.toString(), Charset.defaultCharset());
				searchFileName=myMailSubject;
			}
			
			Reporter.log("------------------------------------------------------------------------",true );
			Reporter.log("Sending mail to external user",true);
			Reporter.log("------------------------------------------------------------------------",true );

			cleanupListSent.add(myMailSubject);
			synchronized(this){ success = objMailTestUser1New.sendMail(externalUser1, myMailSubject,mailBody ,myAttachment , true);}
			assertTrue(success, "Failed sending mail with subject:"+mailSubject2+".");
			success=false;


			//wait for 1 minutes for the exposure to be applied
//			Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
			
			Reporter.log("------------------------------------------------------------------------",true );
			
			int i = 60000;
			for (; i <= ( maxExposureWaitTimeInMinutes * 60 * 1000); i+=60000 ) {
				Reporter.log("Checking exposure after :"+TimeUnit.MILLISECONDS.toMinutes(i)+" minute(s)",true );
				Reporter.log("------------------------------------------------------------------------",true );
				Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);

				//Get the exposure count
				o365Document = getExposures(isInternal, testUser1, violations,searchFileName);
				countAfterExposure = o365Document.getMeta().getTotalCount();
				Reporter.log("Exposure Count ="+countAfterExposure, true);

				if (countAfterExposure >= 1) {break;}
			}
			
			assertEquals(countAfterExposure,1,"File not exposed even after waiting for "+TimeUnit.MILLISECONDS.toMinutes(i-60000)+" minutes.");


			String docId;
			
			Reporter.log("----------------Making API call to get documented Id--------------------",true );
			
			docId = getDocID(searchFileName);

			Reporter.log("----------------Remediating the file--------------------",true );
			
			//Now apply the remedial action thro' API server call
			MailRemediation O365Remediation = getRemediationObject(testUser1, docType, docId, remedialAction);

			/*//Now apply the remedial action thro' UI server or API server call
			if(service.equals(Server.UIServer.name())) {
				remediateExposure(suiteData.getTenantName(), facility.Office365.name(), suiteData.getUsername(), docId, null, remedialAction);
	
			} else if(service.equals(Server.APIServer.name())) {
				remediateExposureWithAPI(O365Remediation);
			} */
			
			//remediate
			responseCode = remediateExposureWithAPI(O365Remediation);
//			Assert.assertEquals(responseCode, 202, "Remediation call failed.");
			
			//Wait for remedial action
			Reporter.log("---------------------Waiting for the remedial action in SAAS APP-----------------------------",true );
			
			i = 60000;
			boolean remediationSuccess = false;
			for (; i <= ( maxRemediationWaitTimeInMinutes * 60 * 1000); i+=60000 ) {
				Reporter.log("Checking if mail or attachment deleted after :"+TimeUnit.MILLISECONDS.toMinutes(i)+" minute(s)",true );
				Reporter.log("------------------------------------------------------------------------",true );
				Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);

				synchronized (this) {
				//Searching for email in mailbox sent items
				emailObj = objMailTestUser1New.findItemInFolder(myMailSubject, "SentItems");
				}
				
				//set remediationSuccess as true if mail or attachment deleted based on remediation type
				if (remedialAction.equals(Remediation.ITEM_DELETE_MAIL_BY_ATTACHMENT.name()) || remedialAction.equals(Remediation.ITEM_DELETE_MAIL.name())) {
					
					if(emailObj==null){
							remediationSuccess=true;
						
					}

				} else if(remedialAction.equals(Remediation.ITEM_DELETE_ATTACHMENT.name())) {
					if(emailObj!=null){
						//searching if attachments available
						synchronized (this) {
							emailObj = objMailTestUser1New.findItemInFolderReturnWithAttachment(myMailSubject, "SentItems");
						}
						if(emailObj.getHasAttachments()==false){
							remediationSuccess=true;
						}
						
					}
					
				}
				

				if (remediationSuccess == true) {
					break;
				}
				else{
					Reporter.log("Mail/Attachment not yet deleted",true);
				}
				
			}
			
			//Verify the remediation thro' o365 API
			if (remedialAction.equals(Remediation.ITEM_DELETE_MAIL_BY_ATTACHMENT.name()) || remedialAction.equals(Remediation.ITEM_DELETE_MAIL.name())) {
				 expectedLog = "QA Admin has remediated/deleted an email with subject \""+myMailSubject+"\"";
				assertNull(emailObj, "Remediation "+ remedialAction + " didn't work. Mail is not deleted even after waiting for "+TimeUnit.MILLISECONDS.toMinutes(i-60000)+" minutes.");

			} else if(remedialAction.equals(Remediation.ITEM_DELETE_ATTACHMENT.name())) {
				 expectedLog = "QA Admin has remediated/deleted an attachment \""+newFileName+"\" from an email with subject \""+myMailSubject+"\"";
				assertNotNull(emailObj, "Mail Not Found");
				assertFalse(emailObj.getHasAttachments(), "Remediation "+ remedialAction + ". Attachment is not deleted even after waiting for "+TimeUnit.MILLISECONDS.toMinutes(i-60000)+" minutes.");
			}
			
			Reporter.log("Remediation successful at Office365 mail");

			//gather the forensic logs again 
			//logs = gatherForensicLogMessages(uniqueId, facility.Box.name());
			
			Reporter.log("------------------------------------------------------------------------",true );
			
			
			Reporter.log("Wait for "+TimeUnit.MILLISECONDS.toMinutes(exposureWaitTimeAfterRemediation)+" minutes, and check if the file is removed from exposure.",true);
			Thread.sleep(exposureWaitTimeAfterRemediation);

			o365Document = getExposures("true", suiteData.getUsername(), violations,searchFileName);
			int countAfterRemediation = o365Document.getMeta().getTotalCount();
			Reporter.log("######" + violationType + " exposure count after remediation::"+countAfterRemediation, true);
			assertEquals(countAfterRemediation, 0, "Exposure count has not reduced after remediation.");
			
			//Verify the activity logs
			Reporter.log("Expected log:" + expectedLog, true);
			ArrayList<String> availableLogs = searchLogs("Activity_type", "Delete", myUniqueId, testUser1, 1);
			
			ArrayList<String> ExpectedLogs = new ArrayList<String>();
			softAssert = new SoftAssert();
			
			ExpectedLogs.add(expectedLog);
			
			assertTrue(compareResult(ExpectedLogs, availableLogs),"Test failed, expected log is not found after remediation by admin.");
			
			

		}
		
		
		/**
		 * @throws Exception
		 */
		@Test(groups={"MAIL4","P1"},retryAnalyzer=RetryAnalyzer.class)
		public void verifyRemediationWhenMultipleAttachmentDeleteAttachment_BCB_20() throws Exception {
			
			Reporter.log("****************Test Case Description****************",true);
			Reporter.log("Test Case Description: Verify that on remediating and deleting an attachment of a mail with multiple attachment, the other attachment is not deleted",true);
			Reporter.log("1)Send Mail with two risk files attachment to external user",true);
			Reporter.log("2)Verify that files are exposed and delete 1 of the exposed file using remediation - ITEM_DELETE_ATTACHMENT",true);
			Reporter.log("3)Verify that the remediated attachment is removed from mail and the other one is still attached in the email",true);
			Reporter.log("*****************************************************",true);
			
			
			String isInternal =   "true";
			String exposuretype = "open";
			String violationType1 ="pii";
			String violationType2 ="pci";
			String fileName1 = "PII.rtf";
			String fileName2 = "PCI_Test.txt";
			String contentPlace ="attachment";
			String remedialAction ="ITEM_DELETE_ATTACHMENT";
			
			//Prepare the remediation object
			microsoft.exchange.webservices.data.core.service.item.Item emailObj = null;
			String myUniqueId = UUID.randomUUID().toString();
			ArrayList<String> myAttachment = new ArrayList<String>() ;
			String newFileName1 = myUniqueId+fileName1;
			String newFileName2 = myUniqueId+fileName2;
			File sourceFile1 =null;
			File destFile1 = null;
			File sourceFile2 =null;
			File destFile2 = null;
			String myMailSubject =null;
			String mailBody = null;
			boolean success =false;
			String docType = null;
			String searchFileName1 =null;
			String searchFileName2 =null;
			int countAfterExposure1 =0;
			int countAfterExposure2 =0;
			int responseCode = 0;
			
			//Prepare the violations
			ArrayList<String> violations = new ArrayList<String>();
			
			
			//Get the exposure count
			O365Document o365Document =null;
//			o365Document = getExposures(isInternal, suiteData.getUsername(), violations,newFileName);
//			int beforeCount = o365Document.getMeta().getTotalCount();
			//
//			Reporter.log("######" + violationType + " exposure count before test::"+beforeCount, true);
			
			sourceFile1 = new File(userDir +fileName1);
			destFile1 = new File(userDir +newFileName1);
			sourceFile2 = new File(userDir +fileName2);
			destFile2 = new File(userDir +newFileName2);
			
			
			if(contentPlace.equals("attachment")){
				docType = "Email_File_Attachment";
				myMailSubject = myUniqueId+"ExposureInMultipleAttachment";
				searchFileName1=newFileName1;
				searchFileName2=newFileName2;
				
				//Creating file with unique id name for upload
				copyFileUsingFileChannels(sourceFile1, destFile1);	
				copyFileUsingFileChannels(sourceFile2, destFile2);	
				filesToDelete.add(destFile1);
				filesToDelete.add(destFile2);
				myAttachment.add(destFile1.toString());
				myAttachment.add(destFile2.toString());
				mailBody= "This is test mail body";
				
			}
			else if(contentPlace.equals("body")){
				docType = "Email_Message";
				myMailSubject = myUniqueId+"ViolationForExposureInMailBody"+violationType1;
				mailBody =readFile(sourceFile1.toString(), Charset.defaultCharset());
				searchFileName1=myMailSubject;
			}
			
			cleanupListSent.add(myMailSubject);
			synchronized(this){ success = objMailTestUser1.sendMail(externalUser1, myMailSubject,mailBody ,myAttachment , true);}
			assertTrue(success, "Failed sending mail with subject:"+mailSubject2+".");
			success=false;
			
			
			//wait for 1 minutes for the exposure to be applied
//			Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
			
			violations.add(violationType1);
			int i = 120000;
			for (; i <= (MAX_EXPOSURE_WAIT_TIME_IN_MINUTES * 60 * 1000); i+=60000 ) {
				Reporter.log("Checking exposure after :"+TimeUnit.MILLISECONDS.toMinutes(i)+" minute(s)",true );
				Reporter.log("------------------------------------------------------------------------",true );
				Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
				
				//Get the exposure count
				o365Document = getExposures(isInternal, testUser1, violations,searchFileName1);
				countAfterExposure1 = o365Document.getMeta().getTotalCount();
				Reporter.log("Exposure Count ="+countAfterExposure1, true);
				
				if (countAfterExposure1 >= 1) {break;}
			}
			assertEquals(countAfterExposure1,1,"File not exposed even after waiting for "+TimeUnit.MILLISECONDS.toMinutes(i-60000)+" minutes.");
			violations.clear();
			violations.add(violationType2);
			for (; i <= (MAX_EXPOSURE_WAIT_TIME_IN_MINUTES * 60 * 1000); i+=60000 ) {
				Reporter.log("Checking exposure after :"+TimeUnit.MILLISECONDS.toMinutes(i)+" minute(s)",true );
				Reporter.log("------------------------------------------------------------------------",true );
				Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
				
				//Get the exposure count
				o365Document = getExposures(isInternal, testUser2, violations,searchFileName2);
				countAfterExposure2 = o365Document.getMeta().getTotalCount();
				Reporter.log("Exposure Count ="+countAfterExposure2, true);
				
				if (countAfterExposure2 >= 1) {break;}
			}
			
			
			assertEquals(countAfterExposure2,1,"File not exposed even after waiting for "+TimeUnit.MILLISECONDS.toMinutes(i-60000)+" minutes.");
			
			
			String docId1,docId2;
			
			docId1 = getDocID(searchFileName1);
			docId2 = getDocID(searchFileName2);
			
			//Now apply the remedial action thro' API server call
			MailRemediation O365Remediation = getRemediationObject(testUser1, docType, docId1, remedialAction);
			
			/*//Now apply the remedial action thro' UI server or API server call
			if(service.equals(Server.UIServer.name())) {
				remediateExposure(suiteData.getTenantName(), facility.Office365.name(), suiteData.getUsername(), docId, null, remedialAction);
	
			} else if(service.equals(Server.APIServer.name())) {
				remediateExposureWithAPI(O365Remediation);
			} */
			
			//remediate
			responseCode = remediateExposureWithAPI(O365Remediation);
//			Assert.assertEquals(responseCode, 202, "Remediation call failed.");
			
			
			//Wait for remedial action
			Reporter.log("Waiting for the remedial action", true);
			
			i = 60000;
			boolean remediationSuccess = false;
			for (; i <= (MAX_REMEDIATION_WAIT_TIME_IN_MINUTES * 60 * 1000); i+=60000 ) {
				Reporter.log("Checking if mail deleted after :"+TimeUnit.MILLISECONDS.toMinutes(i)+" minute(s)",true );
				Reporter.log("------------------------------------------------------------------------",true );
				Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
				
				//Searching for email in mailbox sent items
				synchronized(this){emailObj = objMailTestUser1.findItemInFolder(myMailSubject, "SentItems");}
				
				//set remediationSuccess as true if mail or attachment deleted based on remediation type
				if (remedialAction.equals(Remediation.ITEM_DELETE_MAIL_BY_ATTACHMENT.name()) || remedialAction.equals(Remediation.ITEM_DELETE_MAIL.name())) {
					
					if(emailObj==null){
						remediationSuccess=true;
						
					}
					
				} else if(remedialAction.equals(Remediation.ITEM_DELETE_ATTACHMENT.name())) {
					if(emailObj!=null){
						//searching if attachments available
						synchronized(this){emailObj = objMailTestUser1.findItemInFolderReturnWithAttachment(myMailSubject, "SentItems");}
						if(emailObj.getAttachments().getCount()==1){
							remediationSuccess=true;
						}
						
					}
					
				}
				
				
				if (remediationSuccess == true) {
					break;
				}
				else{
					Reporter.log("Mail/Attachment not yet deleted",true);
				}
				
			}
			
			//Verify the remediation thro' o365 API
			if (remedialAction.equals(Remediation.ITEM_DELETE_MAIL_BY_ATTACHMENT.name()) || remedialAction.equals(Remediation.ITEM_DELETE_MAIL.name())) {
				assertNull(emailObj, "Remediation "+ remedialAction + " didn't work. Mail is not deleted even after waiting for "+TimeUnit.MILLISECONDS.toMinutes(i-60000)+" minutes");
				
			} else if(remedialAction.equals(Remediation.ITEM_DELETE_ATTACHMENT.name())) {
				assertNotNull(emailObj, "Mail Not Found");
				assertTrue(emailObj.getAttachments().getCount() !=0 , "Test failed as both the attachments were deleted");
				assertTrue(emailObj.getAttachments().getCount() == 1 , "Test failed as attachment was not deleted");
			}
			
			Reporter.log("Remediation successful at Office365 mail");
			
			//gather the forensic logs again 
			//logs = gatherForensicLogMessages(uniqueId, facility.Box.name());
			
			Reporter.log("Waiting for "+TimeUnit.MILLISECONDS.toMinutes(EXPOSURE_WAIT_TIME_AFTER_REMEDIATION)+" minutes to check exposure count after remediation");
			Thread.sleep(EXPOSURE_WAIT_TIME_AFTER_REMEDIATION);
			
			violations.clear();
			violations.add(violationType1);
			
			o365Document = getExposures("true",testUser1, violations,searchFileName1);
			int countAfterRemediation = o365Document.getMeta().getTotalCount();
			Reporter.log("###### " + violationType1 + " exposure count after remediation::"+countAfterRemediation, true);
			assertEquals(countAfterRemediation, 0, "Exposure count has not reduced after remediation.");
			
			violations.clear();
			violations.add(violationType2);
			o365Document = getExposures("true", testUser1, violations,searchFileName2);
			countAfterRemediation = o365Document.getMeta().getTotalCount();
			Reporter.log("###### " + violationType2 + " exposure count after remediation::"+countAfterRemediation, true);
			assertEquals(countAfterRemediation, 1, "Exposure count has changed for non-deleted file after remediation.");
			
			//Verify the activity logs
			ArrayList<String> availableLogs = searchLogs("Activity_type", "Delete", myUniqueId, testUser1, 1);
			
			String expectedLog = "QA Admin has remediated/deleted an attachment \""+newFileName1+"\" from an email with subject \""+myMailSubject+"\"";
			Reporter.log("Expected log:" + expectedLog, true);
			assertTrue(availableLogs.contains(expectedLog), expectedLog + " not present in the logs.");
			
			//cleanup file if everything goes well
			//objMail.deleteMail(myMailSubject,DeleteMode.MoveToDeletedItems);
		}
		
		/**
		 * @throws Exception
		 */
		@Test(groups={"MAIL4","P1"},retryAnalyzer=RetryAnalyzer.class)
		public void verifyRemediationWhenMultipleAttachmentDeleteMail_BCB_21() throws Exception {
			
			Reporter.log("****************Test Case Description****************",true);
			Reporter.log("Test Case Description: Verify that on remediating and deleting an email with multiple attachment, the other attachments are removed from exposure",true);
			Reporter.log("1)Send Mail with two risk files attachment to external user",true);
			Reporter.log("2)Verify that files are exposed and delete mail using - ITEM_DELETE_MAIL_BY_ATTACHMENT",true);
			Reporter.log("3)Verify that the mail is deleted and attachments are removed from exposure tab",true);
			Reporter.log("*****************************************************",true);

			
			String isInternal =   "true";
			String exposuretype = "open";
			String violationType1 ="pii";
			String violationType2 ="pci";
			String fileName1 = "PII.rtf";
			String fileName2 = "PCI_Test.txt";
			String contentPlace ="attachment";
			String remedialAction ="ITEM_DELETE_MAIL_BY_ATTACHMENT";

			//Prepare the remediation object
			microsoft.exchange.webservices.data.core.service.item.Item emailObj = null;
			String myUniqueId = UUID.randomUUID().toString();
			ArrayList<String> myAttachment = new ArrayList<String>() ;
			String newFileName1 = myUniqueId+fileName1;
			String newFileName2 = myUniqueId+fileName2;
			File sourceFile1 =null;
			File destFile1 = null;
			File sourceFile2 =null;
			File destFile2 = null;
			String myMailSubject =null;
			String mailBody = null;
			boolean success =false;
			String docType = null;
			String searchFileName1 =null;
			String searchFileName2 =null;
			int countAfterExposure1 =0;
			int countAfterExposure2 =0;
			int responseCode = 0;

			//Prepare the violations
			ArrayList<String> violations = new ArrayList<String>();
		

			//Get the exposure count
			O365Document o365Document =null;
//			o365Document = getExposures(isInternal, suiteData.getUsername(), violations,newFileName);
//			int beforeCount = o365Document.getMeta().getTotalCount();
			//
//			Reporter.log("######" + violationType + " exposure count before test::"+beforeCount, true);

			sourceFile1 = new File(userDir +fileName1);
			destFile1 = new File(userDir +newFileName1);
			sourceFile2 = new File(userDir +fileName2);
			destFile2 = new File(userDir +newFileName2);


			if(contentPlace.equals("attachment")){
				docType = "Email_File_Attachment";
				myMailSubject = myUniqueId+"ExposureInMultipleAttachment";
				searchFileName1=newFileName1;
				searchFileName2=newFileName2;

				//Creating file with unique id name for upload
				copyFileUsingFileChannels(sourceFile1, destFile1);	
				copyFileUsingFileChannels(sourceFile2, destFile2);	
				filesToDelete.add(destFile1);
				filesToDelete.add(destFile2);
				myAttachment.add(destFile1.toString());
				myAttachment.add(destFile2.toString());
				mailBody= "This is test mail body";

			}
			else if(contentPlace.equals("body")){
				docType = "Email_Message";
				myMailSubject = myUniqueId+"ViolationForExposureInMailBody"+violationType1;
				mailBody =readFile(sourceFile1.toString(), Charset.defaultCharset());
				searchFileName1=myMailSubject;
			}
			
			cleanupListSent.add(myMailSubject);
			synchronized(this){ success = objMailTestUser1.sendMail(externalUser1, myMailSubject,mailBody ,myAttachment , true);}
			assertTrue(success, "Failed sending mail with subject:"+mailSubject2+".");
			success=false;


			//wait for 1 minutes for the exposure to be applied
//			Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
			
			violations.add(violationType1);
			int i = 120000;
			for (; i <= (MAX_EXPOSURE_WAIT_TIME_IN_MINUTES * 60 * 1000); i+=60000 ) {
				Reporter.log("Checking exposure after :"+TimeUnit.MILLISECONDS.toMinutes(i)+" minute(s)",true );
				Reporter.log("------------------------------------------------------------------------",true );
				Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);

				//Get the exposure count
				o365Document = getExposures(isInternal, testUser1, violations,searchFileName1);
				countAfterExposure1 = o365Document.getMeta().getTotalCount();
				Reporter.log("Exposure Count ="+countAfterExposure1, true);

				if (countAfterExposure1 >= 1) {break;}
			}
			assertEquals(countAfterExposure1,1,"File not exposed even after waiting for "+TimeUnit.MILLISECONDS.toMinutes(i-60000)+" minutes.");
			violations.clear();
			violations.add(violationType2);
			for (; i <= (MAX_EXPOSURE_WAIT_TIME_IN_MINUTES * 60 * 1000); i+=60000 ) {
				Reporter.log("Checking exposure after :"+TimeUnit.MILLISECONDS.toMinutes(i)+" minute(s)",true );
				Reporter.log("------------------------------------------------------------------------",true );
				Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);

				//Get the exposure count
				o365Document = getExposures(isInternal, testUser2, violations,searchFileName2);
				countAfterExposure2 = o365Document.getMeta().getTotalCount();
				Reporter.log("Exposure Count ="+countAfterExposure2, true);

				if (countAfterExposure2 >= 1) {break;}
			}
			
			
			assertEquals(countAfterExposure2,1,"File not exposed even after waiting for "+TimeUnit.MILLISECONDS.toMinutes(i-60000)+" minutes.");


			String docId1,docId2;
			
			docId1 = getDocID(searchFileName1);
			docId2 = getDocID(searchFileName2);

			//Now apply the remedial action thro' API server call
			MailRemediation O365Remediation = getRemediationObject(testUser1, docType, docId1, remedialAction);

			/*//Now apply the remedial action thro' UI server or API server call
			if(service.equals(Server.UIServer.name())) {
				remediateExposure(suiteData.getTenantName(), facility.Office365.name(), suiteData.getUsername(), docId, null, remedialAction);
	
			} else if(service.equals(Server.APIServer.name())) {
				remediateExposureWithAPI(O365Remediation);
			} */
			
			//remediate
			responseCode = remediateExposureWithAPI(O365Remediation);
//			Assert.assertEquals(responseCode, 202, "Remediation call failed.");
			

			//Wait for remedial action
			Reporter.log("Waiting for the remedial action", true);
			
			i = 60000;
			boolean remediationSuccess = false;
			for (; i <= (MAX_REMEDIATION_WAIT_TIME_IN_MINUTES * 60 * 1000); i+=60000 ) {
				Reporter.log("Checking if mail deleted after :"+TimeUnit.MILLISECONDS.toMinutes(i)+" minute(s)",true );
				Reporter.log("------------------------------------------------------------------------",true );
				Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);

				//Searching for email in mailbox sent items
				synchronized(this){emailObj = objMailTestUser1.findItemInFolder(myMailSubject, "SentItems");}
				
				//set remediationSuccess as true if mail or attachment deleted based on remediation type
				if (remedialAction.equals(Remediation.ITEM_DELETE_MAIL_BY_ATTACHMENT.name()) || remedialAction.equals(Remediation.ITEM_DELETE_MAIL.name())) {
					
					if(emailObj==null){
							remediationSuccess=true;
						
					}

				} else if(remedialAction.equals(Remediation.ITEM_DELETE_ATTACHMENT.name())) {
					if(emailObj!=null){
						//searching if attachments available
						synchronized(this){emailObj = objMailTestUser1.findItemInFolderReturnWithAttachment(myMailSubject, "SentItems");}
						if(emailObj.getAttachments().getCount()==1){
							remediationSuccess=true;
						}
						
					}
					
				}
				

				if (remediationSuccess == true) {
					break;
				}
				else{
					Reporter.log("Mail/Attachment not yet deleted",true);
				}
				
			}
			
			//Verify the remediation thro' o365 API
			if (remedialAction.equals(Remediation.ITEM_DELETE_MAIL_BY_ATTACHMENT.name()) || remedialAction.equals(Remediation.ITEM_DELETE_MAIL.name())) {
				assertNull(emailObj, "Remediation "+ remedialAction + " didn't work. Mail is not deleted even after waiting for "+TimeUnit.MILLISECONDS.toMinutes(i-60000)+" minutes.");

			} else if(remedialAction.equals(Remediation.ITEM_DELETE_ATTACHMENT.name())) {
				assertNotNull(emailObj, "Mail Not Found.");
				assertTrue(emailObj.getAttachments().getCount() !=0 , "Test failed as both the attachments were deleted.");
				assertTrue(emailObj.getAttachments().getCount() == 1 , "Test failed as attachment was not deleted.");
			}
			
			Reporter.log("Remediation successful at Office365 mail");

			//gather the forensic logs again 
			//logs = gatherForensicLogMessages(uniqueId, facility.Box.name());
			
			Reporter.log("Waiting for "+TimeUnit.MILLISECONDS.toMinutes(EXPOSURE_WAIT_TIME_AFTER_REMEDIATION)+" minutes to check exposure count after remediation");
			Thread.sleep(EXPOSURE_WAIT_TIME_AFTER_REMEDIATION);

			violations.clear();
			violations.add(violationType1);
			
			o365Document = getExposures("true",testUser1, violations,searchFileName1);
			int countAfterRemediation = o365Document.getMeta().getTotalCount();
			Reporter.log("###### " + violationType1 + " exposure count after remediation::"+countAfterRemediation, true);
			assertEquals(countAfterRemediation, 0, "Attachment is still exposed after deletion of emai.l");
		
			violations.clear();
			violations.add(violationType2);
			o365Document = getExposures("true", testUser1, violations,searchFileName2);
			countAfterRemediation = o365Document.getMeta().getTotalCount();
			Reporter.log("###### " + violationType2 + " exposure count after remediation::"+countAfterRemediation, true);
			assertEquals(countAfterRemediation, 0, "Attachment is still exposed after deletion of email.");

			//Verify the activity logs
			ArrayList<String> availableLogs = searchLogs("Activity_type", "Delete", myUniqueId, testUser1, 1);
			
			String expectedLog = "QA Admin has remediated/deleted an email with subject \""+myMailSubject+"\"";
			Reporter.log("Expected log:" + expectedLog, true);
			assertTrue(availableLogs.contains(expectedLog), expectedLog + " not present in the logs.");
			
			//cleanup file if everything goes well
			//objMail.deleteMail(myMailSubject,DeleteMode.MoveToDeletedItems);
		}
		
		/**
		 * @throws Exception
		 */
		@Test(groups={"MAIL4","P1"},retryAnalyzer=RetryAnalyzer.class)
		public void verifyRemediationWhenMailAndAttachmentDeleteMail_BCB_22() throws Exception {
			
			Reporter.log("****************Test Case Description****************",true);
			Reporter.log("Test Case Description: Verify that on remediating and deleting an email which has risk and has  attachment,the mail and attachment is removed from exposure",true);
			Reporter.log("1)Send Mail risk in body and a risk file attachment to external user",true);
			Reporter.log("2)Verify that mail and files are exposed and delete mail using - ITEM_DELETE_MAIL",true);
			Reporter.log("3)Verify that the mail is deleted and attachments are removed from exposure tab",true);
			Reporter.log("*****************************************************",true);

			
			String isInternal =   "true";
			String exposuretype = "open";
			String violationType1 ="pii";
			String violationType2 ="pci";
			String fileName1 = "PII.rtf";
			String fileName2 = "PCI_Test.txt";
			String contentPlace ="both";
			String remedialAction ="ITEM_DELETE_MAIL";

			//Prepare the remediation object
			microsoft.exchange.webservices.data.core.service.item.Item emailObj = null;
			String myUniqueId = UUID.randomUUID().toString();
			ArrayList<String> myAttachment = new ArrayList<String>() ;
			String newFileName1 = myUniqueId+fileName1;
			String newFileName2 = myUniqueId+fileName2;
			File sourceFile1 =null;
			File destFile1 = null;
			File sourceFile2 =null;
			File destFile2 = null;
			String myMailSubject =null;
			String mailBody = null;
			boolean success =false;
			String docType = null;
			String searchFileName1 =null;
			String searchFileName2 =null;
			int countAfterExposure1 =0;
			int countAfterExposure2 =0;
			int responseCode = 0;

			//Prepare the violations
			ArrayList<String> violations = new ArrayList<String>();
		

			//Get the exposure count
			O365Document o365Document =null;
//			o365Document = getExposures(isInternal, suiteData.getUsername(), violations,newFileName);
//			int beforeCount = o365Document.getMeta().getTotalCount();
			//
//			Reporter.log("######" + violationType + " exposure count before test::"+beforeCount, true);

			sourceFile1 = new File(userDir +fileName1);
			destFile1 = new File(userDir +newFileName1);
			sourceFile2 = new File(userDir +fileName2);
			destFile2 = new File(userDir +newFileName2);


			if(contentPlace.equals("attachment")){
				docType = "Email_File_Attachment";
				myMailSubject = myUniqueId+"ExposureInMultipleAttachment";
				searchFileName1=newFileName1;
				searchFileName2=newFileName2;

				//Creating file with unique id name for upload
				copyFileUsingFileChannels(sourceFile1, destFile1);	
				copyFileUsingFileChannels(sourceFile2, destFile2);	
				filesToDelete.add(destFile1);
				filesToDelete.add(destFile2);
				myAttachment.add(destFile1.toString());
				myAttachment.add(destFile2.toString());
				mailBody= "This is test mail body";

			}
			else if(contentPlace.equals("body")){
				docType = "Email_Message";
				myMailSubject = myUniqueId+"ViolationForExposureInMailBody"+violationType1;
				mailBody =readFile(sourceFile1.toString(), Charset.defaultCharset());
				searchFileName1=myMailSubject;
			}
			else if(contentPlace.equals("both")){
				docType = "Email_Message";
				myMailSubject = myUniqueId+"ExposureBodyAndAttachment";
				searchFileName1=newFileName1;
				searchFileName2=myMailSubject;

				//Creating file with unique id name for upload
				copyFileUsingFileChannels(sourceFile1, destFile1);
				filesToDelete.add(destFile1);
				myAttachment.add(destFile1.toString());
				mailBody =readFile(sourceFile2.toString(), Charset.defaultCharset());

			}
			
			cleanupListSent.add(myMailSubject);
			synchronized(this){ success = objMailTestUser1.sendMail(externalUser1, myMailSubject,mailBody ,myAttachment , true);}
			assertTrue(success, "Failed sending mail with subject:"+mailSubject2+".");
			success=false;


			//wait for 1 minutes for the exposure to be applied
//			Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
			
			violations.add(violationType1);
			int i = 120000;
			for (; i <= (MAX_EXPOSURE_WAIT_TIME_IN_MINUTES * 60 * 1000); i+=60000 ) {
				Reporter.log("Checking exposure after :"+TimeUnit.MILLISECONDS.toMinutes(i)+" minute(s)",true );
				Reporter.log("------------------------------------------------------------------------",true );
				Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);

				//Get the exposure count
				o365Document = getExposures(isInternal, testUser1, violations,searchFileName1);
				countAfterExposure1 = o365Document.getMeta().getTotalCount();
				Reporter.log("Exposure Count ="+countAfterExposure1, true);

				if (countAfterExposure1 >= 1) {break;}
			}
			assertEquals(countAfterExposure1,1,"File not exposed even after waiting for "+TimeUnit.MILLISECONDS.toMinutes(i-60000)+" minutes.");
			o365Document = getExposedUsers("collab",isInternal,testUser1, violations,searchFileName1);
			assertTrue(o365Document.getObjects().get(0).getEmail().contains(externalUser1),"User email not found in collaborators list:"+externalUser1 +".");		
			o365Document = getExposedUsers("users",isInternal,testUser1, violations,searchFileName1);
			assertTrue(o365Document.getObjects().get(0).getEmail().contains(testUser1),"User email not found in owner list:"+testUser1 +".");
			
			
			violations.clear();
			violations.add(violationType2);
			for (; i <= (MAX_EXPOSURE_WAIT_TIME_IN_MINUTES * 60 * 1000); i+=60000 ) {
				Reporter.log("Checking exposure after :"+TimeUnit.MILLISECONDS.toMinutes(i)+" minute(s)",true );
				Reporter.log("------------------------------------------------------------------------",true );
				Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);

				//Get the exposure count
				o365Document = getExposures(isInternal, testUser2, violations,searchFileName2);
				countAfterExposure2 = o365Document.getMeta().getTotalCount();
				Reporter.log("Exposure Count ="+countAfterExposure2, true);

				if (countAfterExposure2 >= 1) {break;}
			}
			
			
			assertEquals(countAfterExposure2,1,"File not exposed even after waiting for "+TimeUnit.MILLISECONDS.toMinutes(i-60000)+" minutes");
			o365Document = getExposedUsers("collab",isInternal,testUser1, violations,searchFileName2);
			assertTrue(o365Document.getObjects().get(0).getEmail().contains(externalUser1),"User email not found in collaborators list:"+externalUser1 +".");		
			o365Document = getExposedUsers("users",isInternal,testUser1, violations,searchFileName2);
			assertTrue(o365Document.getObjects().get(0).getEmail().contains(testUser1),"User email not found in owner list:"+testUser1 +".");

			String docId1,docId2;
			
			docId1 = getDocID(searchFileName1);
			docId2 = getDocID(searchFileName2);

			//Now apply the remedial action thro' API server call
			MailRemediation O365Remediation = getRemediationObject(testUser1, docType, docId2, remedialAction);

			
			//remediate
			responseCode = remediateExposureWithAPI(O365Remediation);
//			Assert.assertEquals(responseCode, 202, "Remediation call failed.");
			

			//Wait for remedial action
			Reporter.log("Waiting for the remedial action", true);
			
			i = 60000;
			boolean remediationSuccess = false;
			for (; i <= (MAX_REMEDIATION_WAIT_TIME_IN_MINUTES * 60 * 1000); i+=60000 ) {
				Reporter.log("Checking if mail deleted after :"+TimeUnit.MILLISECONDS.toMinutes(i)+" minute(s)",true );
				Reporter.log("------------------------------------------------------------------------",true );
				Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);

				//Searching for email in mailbox sent items
				synchronized(this){emailObj = objMailTestUser1.findItemInFolder(myMailSubject, "SentItems");}
				
				//set remediationSuccess as true if mail or attachment deleted based on remediation type
				if (remedialAction.equals(Remediation.ITEM_DELETE_MAIL_BY_ATTACHMENT.name()) || remedialAction.equals(Remediation.ITEM_DELETE_MAIL.name())) {
					
					if(emailObj==null){
							remediationSuccess=true;
						
					}

				} else if(remedialAction.equals(Remediation.ITEM_DELETE_ATTACHMENT.name())) {
					if(emailObj!=null){
						//searching if attachments available
						synchronized(this){emailObj = objMailTestUser1.findItemInFolderReturnWithAttachment(myMailSubject, "SentItems");}
						if(emailObj.getAttachments().getCount()==1){
							remediationSuccess=true;
						}
						
					}
					
				}
				

				if (remediationSuccess == true) {
					break;
				}
				else{
					Reporter.log("Mail/Attachment not yet deleted",true);
				}
				
			}
			
			//Verify the remediation thro' o365 API
			if (remedialAction.equals(Remediation.ITEM_DELETE_MAIL_BY_ATTACHMENT.name()) || remedialAction.equals(Remediation.ITEM_DELETE_MAIL.name())) {
				assertNull(emailObj, "Remediation "+ remedialAction + " didn't work. Mail is not deleted even after waiting for "+TimeUnit.MILLISECONDS.toMinutes(i-60000)+" minutes.");

			} else if(remedialAction.equals(Remediation.ITEM_DELETE_ATTACHMENT.name())) {
				assertNotNull(emailObj, "Mail Not Found.");
				assertTrue(emailObj.getAttachments().getCount() !=0 , "Test failed as both the attachments were deleted.");
				assertTrue(emailObj.getAttachments().getCount() == 1 , "Test failed as attachment was not deleted.");
			}
			
			Reporter.log("Remediation successful at Office365 mail");

			//gather the forensic logs again 
			//logs = gatherForensicLogMessages(uniqueId, facility.Box.name());
			
			Reporter.log("Waiting for "+TimeUnit.MILLISECONDS.toMinutes(EXPOSURE_WAIT_TIME_AFTER_REMEDIATION)+" minutes to check exposure count after remediation");
			Thread.sleep(EXPOSURE_WAIT_TIME_AFTER_REMEDIATION);

			violations.clear();
			violations.add(violationType1);
			
			o365Document = getExposures("true",testUser1, violations,searchFileName1);
			int countAfterRemediation = o365Document.getMeta().getTotalCount();
			Reporter.log("###### " + violationType1 + " exposure count after remediation::"+countAfterRemediation, true);
			assertEquals(countAfterRemediation, 0, "Attachment is still exposed after deletion of email.");
		
			violations.clear();
			violations.add(violationType2);
			o365Document = getExposures("true", testUser1, violations,searchFileName2);
			countAfterRemediation = o365Document.getMeta().getTotalCount();
			Reporter.log("###### " + violationType2 + " exposure count after remediation::"+countAfterRemediation, true);
			assertEquals(countAfterRemediation, 0, "Attachment is still exposed after deletion of email.");
			
			o365Document = getExposedUsers("collab",isInternal,testUser1, violations,searchFileName1);
			assertEquals(o365Document.getMeta().getTotalCount(), 0, "Collaborator is still exposed after deletion of email:"+externalUser1 +".");		
			o365Document = getExposedUsers("users",isInternal,testUser1, violations,searchFileName1);
			assertEquals(o365Document.getMeta().getTotalCount(), 0,"Owner is still exposed after deletion of email:"+testUser1 +".");
			
			o365Document = getExposedUsers("collab",isInternal,testUser1, violations,searchFileName2);
			assertEquals(o365Document.getMeta().getTotalCount(), 0, "Collaborator is still exposed after deletion of email:"+externalUser1 +".");		
			o365Document = getExposedUsers("users",isInternal,testUser1, violations,searchFileName2);
			assertEquals(o365Document.getMeta().getTotalCount(), 0,"Owner is still exposed after deletion of email:"+testUser1 +".");


			//Verify the activity logs
			ArrayList<String> availableLogs = searchLogs("Activity_type", "Delete", myUniqueId, testUser1, 1);
			
			String expectedLog = "QA Admin has remediated/deleted an email with subject \""+myMailSubject+"\"";
			Reporter.log("Expected log:" + expectedLog, true);
			assertTrue(availableLogs.contains(expectedLog), expectedLog + " not present in the logs.");
			
			//cleanup file if everything goes well
			//objMail.deleteMail(myMailSubject,DeleteMode.MoveToDeletedItems);
		}
		
		/**
					 * @throws Exception
					 */
					@Test(groups={"MAIL4","P1"},retryAnalyzer=RetryAnalyzer.class)
					public void verifyRemediationWhenMailAndAttachmentSentToMultipleUsers() throws Exception {
						
						Reporter.log("****************Test Case Description****************",true);
						Reporter.log("Test Case Description: Verify that on remediating and deleting an email which was sent to multiple users internal,external,the mail and attachment is removed from exposure and the user also",true);
						Reporter.log("1)Send Mail risk in body and a risk file attachment to multiple users, including external and internal users",true);
						Reporter.log("2)Verify that mail and files are exposed and delete mail using - ITEM_DELETE_MAIL",true);
						Reporter.log("3)Verify that the mail is deleted and attachments are removed from exposure tab for all internal users",true);
						Reporter.log("*****************************************************",true);
		
						
						String isInternal =   "true";
						String exposuretype = "open";
						String violationType1 ="pii";
						String violationType2 ="pci";
						String fileName1 = "PII.rtf";
						String fileName2 = "PCI_Test.txt";
						String contentPlace ="both";
						String remedialAction ="ITEM_DELETE_MAIL";
		
						//Prepare the remediation object
						microsoft.exchange.webservices.data.core.service.item.Item emailObj = null;
						String myUniqueId = UUID.randomUUID().toString();
						ArrayList<String> myAttachment = new ArrayList<String>() ;
						String newFileName1 = myUniqueId+fileName1;
						String newFileName2 = myUniqueId+fileName2;
						File sourceFile1 =null;
						File destFile1 = null;
						File sourceFile2 =null;
						File destFile2 = null;
						String myMailSubject =null;
						String mailBody = null;
						boolean success =false;
						String docType = null;
						String searchFileName1 =null;
						String searchFileName2 =null;
						int countAfterExposure1 =0;
						int countAfterExposure2 =0;
						int responseCode = 0;
		
						//Prepare the violations
						ArrayList<String> violations = new ArrayList<String>();
					
		
						//Get the exposure count
						O365Document o365Document =null;
		//				o365Document = getExposures(isInternal, suiteData.getUsername(), violations,newFileName);
		//				int beforeCount = o365Document.getMeta().getTotalCount();
						//
		//				Reporter.log("######" + violationType + " exposure count before test::"+beforeCount, true);
		
						sourceFile1 = new File(userDir +fileName1);
						destFile1 = new File(userDir +newFileName1);
						sourceFile2 = new File(userDir +fileName2);
						destFile2 = new File(userDir +newFileName2);
		
		
						if(contentPlace.equals("attachment")){
							docType = "Email_File_Attachment";
							myMailSubject = myUniqueId+"ExposureInMultipleAttachment";
							searchFileName1=newFileName1;
							searchFileName2=newFileName2;
		
							//Creating file with unique id name for upload
							copyFileUsingFileChannels(sourceFile1, destFile1);	
							copyFileUsingFileChannels(sourceFile2, destFile2);	
							filesToDelete.add(destFile1);
							filesToDelete.add(destFile2);
							myAttachment.add(destFile1.toString());
							myAttachment.add(destFile2.toString());
							mailBody= "This is test mail body";
		
						}
						else if(contentPlace.equals("body")){
							docType = "Email_Message";
							myMailSubject = myUniqueId+"ViolationForExposureInMailBody"+violationType1;
							mailBody =readFile(sourceFile1.toString(), Charset.defaultCharset());
							searchFileName1=myMailSubject;
						}
						else if(contentPlace.equals("both")){
							docType = "Email_Message";
							myMailSubject = myUniqueId+"ExposureBodyAndAttachment";
							searchFileName1=newFileName1;
							searchFileName2=myMailSubject;
		
							//Creating file with unique id name for upload
							copyFileUsingFileChannels(sourceFile1, destFile1);
							filesToDelete.add(destFile1);
							myAttachment.add(destFile1.toString());
							mailBody =readFile(sourceFile2.toString(), Charset.defaultCharset());
		
						}
						
						cleanupListSent.add(myMailSubject);
						EmailAddressCollection recipientsCc = new EmailAddressCollection();
						 EmailAddressCollection recipientsTo = new EmailAddressCollection();
						//2 internal and 2 external users
						 recipientsTo.add(externalUser1);
						 recipientsTo.add(adminMailId);
						recipientsCc.add(externalUser2);
						 recipientsCc.add(testUser2);
		
						 cleanupListSent.add(myMailSubject);
						 synchronized(this){ success = objMailTestUser1.sendMailWithCCAndBCC(recipientsTo,recipientsCc,null, myMailSubject,mailBody ,myAttachment , true);}
						 assertTrue(success, "Failed sending mail with subject:"+myMailSubject+".");
						 success=false;
		
		
						//wait for 1 minutes for the exposure to be applied
		//				Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
						
						violations.add(violationType1);
						int i = 120000;
						for (; i <= (MAX_EXPOSURE_WAIT_TIME_IN_MINUTES * 60 * 1000); i+=60000 ) {
							Reporter.log("Checking exposure after :"+TimeUnit.MILLISECONDS.toMinutes(i)+" minute(s)",true );
							Reporter.log("------------------------------------------------------------------------",true );
							Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
		
							//Get the exposure count
							o365Document = getExposures(isInternal, testUser1, violations,searchFileName1);
							countAfterExposure1 = o365Document.getMeta().getTotalCount();
							Reporter.log("Exposure Count ="+countAfterExposure1, true);
		
							if (countAfterExposure1 >= 1) {break;}
						}
						assertEquals(countAfterExposure1,1,"File not exposed even after waiting for "+TimeUnit.MILLISECONDS.toMinutes(i-60000)+" minutes.");
						 String myResponse;
						 o365Document = getExposedUsers("collab",isInternal,testUser1, violations,searchFileName1);
						 myResponse = MarshallingUtils.marshall(o365Document);
						 assertTrue(myResponse.contains(externalUser1),"User email not found in collaborators list:"+externalUser1 +".");		
						 assertTrue(myResponse.contains(externalUser2),"User email not found in collaborators list:"+externalUser2 +".");		
						 o365Document = getExposedUsers("users",isInternal,testUser1, violations,searchFileName1);
						 myResponse = MarshallingUtils.marshall(o365Document);
						 assertTrue(myResponse.contains(testUser1),"User email not found in owner list:"+testUser1 +".");
						
						violations.clear();
						violations.add(violationType2);
						for (; i <= (MAX_EXPOSURE_WAIT_TIME_IN_MINUTES * 60 * 1000); i+=60000 ) {
							Reporter.log("Checking exposure after :"+TimeUnit.MILLISECONDS.toMinutes(i)+" minute(s)",true );
							Reporter.log("------------------------------------------------------------------------",true );
							Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
		
							//Get the exposure count
							o365Document = getExposures(isInternal, testUser2, violations,searchFileName2);
							countAfterExposure2 = o365Document.getMeta().getTotalCount();
							Reporter.log("Exposure Count ="+countAfterExposure2, true);
		
							if (countAfterExposure2 >= 1) {break;}
						}
						
						
						 o365Document = getExposedUsers("collab",isInternal,testUser1, violations,searchFileName2);
						 myResponse = MarshallingUtils.marshall(o365Document);
						 assertTrue(myResponse.contains(externalUser1),"User email not found in collaborators list:"+externalUser1 +".");		
						 assertTrue(myResponse.contains(externalUser2),"User email not found in collaborators list:"+externalUser2 +".");		
						 o365Document = getExposedUsers("users",isInternal,testUser1, violations,searchFileName2);
						 myResponse = MarshallingUtils.marshall(o365Document);
						 assertTrue(myResponse.contains(testUser1),"User email not found in owner list:"+testUser1 +".");
		
						String docId1,docId2;
						
						docId1 = getDocID(searchFileName1);
						docId2 = getDocID(searchFileName2);
		
						//Now apply the remedial action thro' API server call
						MailRemediation O365Remediation = getRemediationObject(testUser1, docType, docId2, remedialAction);
		
						
						//remediate
						responseCode = remediateExposureWithAPI(O365Remediation);
//						Assert.assertEquals(responseCode, 202, "Remediation call failed.");
						
		
						//Wait for remedial action
						Reporter.log("Waiting for the remedial action", true);
						
						i = 60000;
						boolean remediationSuccess = false;
						for (; i <= (MAX_REMEDIATION_WAIT_TIME_IN_MINUTES * 60 * 1000); i+=60000 ) {
							Reporter.log("Checking if mail deleted after :"+TimeUnit.MILLISECONDS.toMinutes(i)+" minute(s)",true );
							Reporter.log("------------------------------------------------------------------------",true );
							Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
		
							//Searching for email in mailbox sent items
							synchronized(this){emailObj = objMailTestUser1.findItemInFolder(myMailSubject, "SentItems");}
							
							//set remediationSuccess as true if mail or attachment deleted based on remediation type
							if (remedialAction.equals(Remediation.ITEM_DELETE_MAIL_BY_ATTACHMENT.name()) || remedialAction.equals(Remediation.ITEM_DELETE_MAIL.name())) {
								
								if(emailObj==null){
										remediationSuccess=true;
									
								}
		
							} else if(remedialAction.equals(Remediation.ITEM_DELETE_ATTACHMENT.name())) {
								if(emailObj!=null){
									//searching if attachments available
									synchronized(this){emailObj = objMailTestUser1.findItemInFolderReturnWithAttachment(myMailSubject, "SentItems");}
									if(emailObj.getAttachments().getCount()==1){
										remediationSuccess=true;
									}
									
								}
								
							}
							
		
							if (remediationSuccess == true) {
								break;
							}
							else{
								Reporter.log("Mail/Attachment not yet deleted",true);
							}
							
						}
						
						//Verify the remediation thro' o365 API
						if (remedialAction.equals(Remediation.ITEM_DELETE_MAIL_BY_ATTACHMENT.name()) || remedialAction.equals(Remediation.ITEM_DELETE_MAIL.name())) {
							assertNull(emailObj, "Remediation "+ remedialAction + " didn't work. Mail is not deleted even after waiting for "+TimeUnit.MILLISECONDS.toMinutes(i-60000)+" minutes.");
		
						} else if(remedialAction.equals(Remediation.ITEM_DELETE_ATTACHMENT.name())) {
							assertNotNull(emailObj, "Mail Not Found.");
							assertTrue(emailObj.getAttachments().getCount() !=0 , "Test failed as both the attachments were deleted.");
							assertTrue(emailObj.getAttachments().getCount() == 1 , "Test failed as attachment was not deleted.");
						}
						
						
						synchronized(this){
							emailObj=objMailTestUser2.findItemInFolder(myMailSubject, "Inbox");
							assertEquals(emailObj, null, "Mail is not deleted from Inbox.");
							assertTrue( emailObj==null, "Mail is not deleted on remediation from Inbox of user:"+testUser2);
							emailObj=objMailSysAdmin.findItemInFolder(myMailSubject, "Inbox");
							assertTrue( emailObj==null, "Mail is not deleted on remediation from Inbox of user:"+objMailSysAdmin);
						}
						
						Reporter.log("Remediation successful at Office365 mail");
		
						//gather the forensic logs again 
						//logs = gatherForensicLogMessages(uniqueId, facility.Box.name());
						
						Reporter.log("Waiting for "+TimeUnit.MILLISECONDS.toMinutes(EXPOSURE_WAIT_TIME_AFTER_REMEDIATION)+" minutes to check exposure count after remediation");
						Thread.sleep(EXPOSURE_WAIT_TIME_AFTER_REMEDIATION);
		
						violations.clear();
						violations.add(violationType1);
						
						o365Document = getExposures("true",testUser1, violations,searchFileName1);
						int countAfterRemediation = o365Document.getMeta().getTotalCount();
						Reporter.log("###### " + violationType1 + " exposure count after remediation::"+countAfterRemediation, true);
						assertEquals(countAfterRemediation, 0, "Attachment is still exposed after deletion of email.");
					
						violations.clear();
						violations.add(violationType2);
						o365Document = getExposures("true", testUser1, violations,searchFileName2);
						countAfterRemediation = o365Document.getMeta().getTotalCount();
						Reporter.log("###### " + violationType2 + " exposure count after remediation::"+countAfterRemediation, true);
						assertEquals(countAfterRemediation, 0, "Attachment is still exposed after deletion of email.");
						
						o365Document = getExposedUsers("collab",isInternal,testUser1, violations,searchFileName1);
						assertEquals(o365Document.getMeta().getTotalCount(), 0, "Collaborator is still exposed after deletion of email:"+externalUser1 +".");		
						o365Document = getExposedUsers("users",isInternal,testUser1, violations,searchFileName1);
						assertEquals(o365Document.getMeta().getTotalCount(), 0,"Owner is still exposed after deletion of email:"+testUser1 +".");
						
						o365Document = getExposedUsers("collab",isInternal,testUser1, violations,searchFileName2);
						assertEquals(o365Document.getMeta().getTotalCount(), 0, "Collaborator is still exposed after deletion of email:"+externalUser1 +".");		
						o365Document = getExposedUsers("users",isInternal,testUser1, violations,searchFileName2);
						assertEquals(o365Document.getMeta().getTotalCount(), 0,"Owner is still exposed after deletion of email:"+testUser1 +".");
		
		
						//Verify the activity logs
						ArrayList<String> availableLogs = searchLogs("Activity_type", "Delete", myUniqueId, testUser1, 1);
						
						String expectedLog = "QA Admin has remediated/deleted an email with subject \""+myMailSubject+"\"";
						Reporter.log("Expected log:" + expectedLog, true);
						assertTrue(availableLogs.contains(expectedLog), expectedLog + " not present in the logs.");
						
						//cleanup file if everything goes well
						//objMail.deleteMail(myMailSubject,DeleteMode.MoveToDeletedItems);
					}

		/**
					 * @throws Exception
					 */
					@Test(groups={"MAIL4","P2"},retryAnalyzer=RetryAnalyzer.class)
					public void verifyRemediationWhenMultipleAttachmentDeleteAttachmentMultipleUsers_BCB_20() throws Exception {
						
						Reporter.log("****************Test Case Description****************",true);
						Reporter.log("Test Case Description: Verify that on remediating and deleting an attachment of a mail with multiple attachment which was sent to multiple users including internal and external users, the other attachment is not deleted from inbox of interna users",true);
						Reporter.log("1)Send Mail with two risk files attachment to external and internal users",true);
						Reporter.log("2)Verify that files are exposed and delete 1 of the exposed file using remediation - ITEM_DELETE_ATTACHMENT",true);
						Reporter.log("3)Verify that the remediated attachment is removed from inbox of internal users and the other one is still attached in the email",true);
						Reporter.log("*****************************************************",true);
						
						
						String isInternal =   "true";
						String exposuretype = "open";
						String violationType1 ="pii";
						String violationType2 ="pci";
						String fileName1 = "PII.rtf";
						String fileName2 = "PCI_Test.txt";
						String contentPlace ="attachment";
						String remedialAction ="ITEM_DELETE_ATTACHMENT";
						
						//Prepare the remediation object
						microsoft.exchange.webservices.data.core.service.item.Item emailObj = null;
						String myUniqueId = UUID.randomUUID().toString();
						ArrayList<String> myAttachment = new ArrayList<String>() ;
						String newFileName1 = myUniqueId+fileName1;
						String newFileName2 = myUniqueId+fileName2;
						File sourceFile1 =null;
						File destFile1 = null;
						File sourceFile2 =null;
						File destFile2 = null;
						String myMailSubject =null;
						String mailBody = null;
						boolean success =false;
						String docType = null;
						String searchFileName1 =null;
						String searchFileName2 =null;
						int countAfterExposure1 =0;
						int countAfterExposure2 =0;
						int responseCode = 0;
						
						//Prepare the violations
						ArrayList<String> violations = new ArrayList<String>();
						
						
						//Get the exposure count
						O365Document o365Document =null;
		//				o365Document = getExposures(isInternal, suiteData.getUsername(), violations,newFileName);
		//				int beforeCount = o365Document.getMeta().getTotalCount();
						//
		//				Reporter.log("######" + violationType + " exposure count before test::"+beforeCount, true);
						
						sourceFile1 = new File(userDir +fileName1);
						destFile1 = new File(userDir +newFileName1);
						sourceFile2 = new File(userDir +fileName2);
						destFile2 = new File(userDir +newFileName2);
						
						
						if(contentPlace.equals("attachment")){
							docType = "Email_File_Attachment";
							myMailSubject = myUniqueId+"ExposureInMultipleAttachment";
							searchFileName1=newFileName1;
							searchFileName2=newFileName2;
							
							//Creating file with unique id name for upload
							copyFileUsingFileChannels(sourceFile1, destFile1);	
							copyFileUsingFileChannels(sourceFile2, destFile2);	
							filesToDelete.add(destFile1);
							filesToDelete.add(destFile2);
							myAttachment.add(destFile1.toString());
							myAttachment.add(destFile2.toString());
							mailBody= "This is test mail body";
							
						}
						else if(contentPlace.equals("body")){
							docType = "Email_Message";
							myMailSubject = myUniqueId+"ViolationForExposureInMailBody"+violationType1;
							mailBody =readFile(sourceFile1.toString(), Charset.defaultCharset());
							searchFileName1=myMailSubject;
						}
						
						EmailAddressCollection recipientsCc = new EmailAddressCollection();
						 EmailAddressCollection recipientsTo = new EmailAddressCollection();
						//2 internal and 2 external users
						 recipientsTo.add(externalUser1);
						 recipientsTo.add(adminMailId);
						recipientsCc.add(externalUser2);
						 recipientsCc.add(testUser2);
		
						 cleanupListSent.add(myMailSubject);
						 synchronized(this){ success = objMailTestUser1.sendMailWithCCAndBCC(recipientsTo,recipientsCc,null, myMailSubject,mailBody ,myAttachment , true);}
						 assertTrue(success, "Failed sending mail with subject:"+myMailSubject+".");
						 success=false;
						
						
						//wait for 1 minutes for the exposure to be applied
		//				Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
						
						violations.add(violationType1);
						int i = 120000;
						for (; i <= (MAX_EXPOSURE_WAIT_TIME_IN_MINUTES * 60 * 1000); i+=60000 ) {
							Reporter.log("Checking exposure after :"+TimeUnit.MILLISECONDS.toMinutes(i)+" minute(s)",true );
							Reporter.log("------------------------------------------------------------------------",true );
							Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
							
							//Get the exposure count
							o365Document = getExposures(isInternal, testUser1, violations,searchFileName1);
							countAfterExposure1 = o365Document.getMeta().getTotalCount();
							Reporter.log("Exposure Count ="+countAfterExposure1, true);
							
							if (countAfterExposure1 >= 1) {break;}
						}
						assertEquals(countAfterExposure1,1,"File not exposed even after waiting for "+TimeUnit.MILLISECONDS.toMinutes(i-60000)+" minutes.");
						violations.clear();
						violations.add(violationType2);
						for (; i <= (MAX_EXPOSURE_WAIT_TIME_IN_MINUTES * 60 * 1000); i+=60000 ) {
							Reporter.log("Checking exposure after :"+TimeUnit.MILLISECONDS.toMinutes(i)+" minute(s)",true );
							Reporter.log("------------------------------------------------------------------------",true );
							Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
							
							//Get the exposure count
							o365Document = getExposures(isInternal, testUser2, violations,searchFileName2);
							countAfterExposure2 = o365Document.getMeta().getTotalCount();
							Reporter.log("Exposure Count ="+countAfterExposure2, true);
							
							if (countAfterExposure2 >= 1) {break;}
						}
						
						
						assertEquals(countAfterExposure2,1,"File not exposed even after waiting for "+TimeUnit.MILLISECONDS.toMinutes(i-60000)+" minutes.");
						
						
						String docId1,docId2;
						
						docId1 = getDocID(searchFileName1);
						docId2 = getDocID(searchFileName2);
						
						//Now apply the remedial action thro' API server call
						MailRemediation O365Remediation = getRemediationObject(testUser1, docType, docId1, remedialAction);
						
						/*//Now apply the remedial action thro' UI server or API server call
						if(service.equals(Server.UIServer.name())) {
							remediateExposure(suiteData.getTenantName(), facility.Office365.name(), suiteData.getUsername(), docId, null, remedialAction);
				
						} else if(service.equals(Server.APIServer.name())) {
							remediateExposureWithAPI(O365Remediation);
						} */
						
						//remediate
						responseCode = remediateExposureWithAPI(O365Remediation);
//						Assert.assertEquals(responseCode, 202, "Remediation call failed.");
						
						
						//Wait for remedial action
						Reporter.log("Waiting for the remedial action", true);
						
						i = 60000;
						boolean remediationSuccess = false;
						for (; i <= (MAX_REMEDIATION_WAIT_TIME_IN_MINUTES * 60 * 1000); i+=60000 ) {
							Reporter.log("Checking if mail deleted after :"+TimeUnit.MILLISECONDS.toMinutes(i)+" minute(s)",true );
							Reporter.log("------------------------------------------------------------------------",true );
							Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
							
							//Searching for email in mailbox sent items
							synchronized(this){emailObj = objMailTestUser1.findItemInFolder(myMailSubject, "SentItems");}
							
							//set remediationSuccess as true if mail or attachment deleted based on remediation type
							if (remedialAction.equals(Remediation.ITEM_DELETE_MAIL_BY_ATTACHMENT.name()) || remedialAction.equals(Remediation.ITEM_DELETE_MAIL.name())) {
								
								if(emailObj==null){
									remediationSuccess=true;
									
								}
								
							} else if(remedialAction.equals(Remediation.ITEM_DELETE_ATTACHMENT.name())) {
								if(emailObj!=null){
									//searching if attachments available
									synchronized(this){emailObj = objMailTestUser1.findItemInFolderReturnWithAttachment(myMailSubject, "SentItems");}
									if(emailObj.getAttachments().getCount()==1){
										remediationSuccess=true;
									}
									
								}
								
							}
							
							
							if (remediationSuccess == true) {
								break;
							}
							else{
								Reporter.log("Mail/Attachment not yet deleted",true);
							}
							
						}
						
						//Verify the remediation thro' o365 API
						if (remedialAction.equals(Remediation.ITEM_DELETE_MAIL_BY_ATTACHMENT.name()) || remedialAction.equals(Remediation.ITEM_DELETE_MAIL.name())) {
							assertNull(emailObj, "Remediation "+ remedialAction + " didn't work. Mail is not deleted even after waiting for "+TimeUnit.MILLISECONDS.toMinutes(i-60000)+" minutes");
							
						} else if(remedialAction.equals(Remediation.ITEM_DELETE_ATTACHMENT.name())) {
							assertNotNull(emailObj, "Mail Not Found");
							assertTrue(emailObj.getAttachments().getCount() !=0 , "Test failed as both the attachments were deleted");
							assertTrue(emailObj.getAttachments().getCount() == 1 , "Test failed as attachment was not deleted");
						}
						
						Reporter.log("Remediation successful at Office365 mail");
						
						//gather the forensic logs again 
						//logs = gatherForensicLogMessages(uniqueId, facility.Box.name());
						
						Reporter.log("Waiting for "+TimeUnit.MILLISECONDS.toMinutes(EXPOSURE_WAIT_TIME_AFTER_REMEDIATION)+" minutes to check exposure count after remediation");
						Thread.sleep(EXPOSURE_WAIT_TIME_AFTER_REMEDIATION);
						
						violations.clear();
						violations.add(violationType1);
						
						o365Document = getExposures("true",testUser1, violations,searchFileName1);
						int countAfterRemediation = o365Document.getMeta().getTotalCount();
						Reporter.log("###### " + violationType1 + " exposure count after remediation::"+countAfterRemediation, true);
						assertEquals(countAfterRemediation, 0, "Exposure count has not reduced after remediation.");
						
						violations.clear();
						violations.add(violationType2);
						o365Document = getExposures("true", testUser1, violations,searchFileName2);
						countAfterRemediation = o365Document.getMeta().getTotalCount();
						Reporter.log("###### " + violationType2 + " exposure count after remediation::"+countAfterRemediation, true);
						assertEquals(countAfterRemediation, 1, "Exposure count has changed for non-deleted file after remediation.");
						
						//searching if attachments available
						synchronized(this){
							emailObj = objMailTestUser2.findItemInFolderReturnWithAttachment(myMailSubject, "Inbox");
							assertEquals(emailObj.getAttachments().getCount(), 1, "On remediation, attachment is not deleted from Inbox of user:"+testUser2);
							emailObj = objMailSysAdmin.findItemInFolderReturnWithAttachment(myMailSubject, "Inbox");
							assertEquals(emailObj.getAttachments().getCount(), 1, "On remediation, attachment is not deleted from Inbox of user:"+adminMailId);
						}
						
						//Verify the activity logs
						ArrayList<String> availableLogs = searchLogs("Activity_type", "Delete", myUniqueId, testUser1, 1);
						
						String expectedLog = "QA Admin has remediated/deleted an attachment \""+newFileName1+"\" from an email with subject \""+myMailSubject+"\"";
						Reporter.log("Expected log:" + expectedLog, true);
						assertTrue(availableLogs.contains(expectedLog), expectedLog + " not present in the logs.");
						
						//cleanup file if everything goes well
						//objMail.deleteMail(myMailSubject,DeleteMode.MoveToDeletedItems);
					}

		/**
				 * @throws Exception
				 */
				//@Test(groups={"MAIL4"},retryAnalyzer=RetryAnalyzer.class)
				public void verifyRemediationWhenMultipleBigAttachmentsDeleteAttachment_BCB_48() throws Exception {
					
					Reporter.log("****************Test Case Description****************",true);
					Reporter.log("Test Case Description: Verify that on remediating and deleting an attachment of a mail with multiple attachment, the other attachment is not deleted",true);
					Reporter.log("1)Send Mail with two risk files attachment to external user",true);
					Reporter.log("2)Verify that files are exposed and delete 1 of the exposed file using remediation - ITEM_DELETE_ATTACHMENT",true);
					Reporter.log("3)Verify that the remediated attachment is removed from mail and the other one is still attached in the email",true);
					Reporter.log("*****************************************************",true);
					
					
					String isInternal =   "true";
					String exposuretype = "open";
					String violationType1 ="pii";
					String violationType2 ="pci";
					String fileName1 = "PII.rtf";
					String fileName2 = "PCI_Test.txt";
					String contentPlace ="attachment";
					String remedialAction ="ITEM_DELETE_ATTACHMENT";
					
					//Prepare the remediation object
					microsoft.exchange.webservices.data.core.service.item.Item emailObj = null;
					String myUniqueId = UUID.randomUUID().toString();
					ArrayList<String> myAttachment = new ArrayList<String>() ;
					String newFileName1 = null;
					File sourceFile1 =null;
					File destFile1 = null;
					String myMailSubject =null;
					String mailBody = null;
					boolean success =false;
					String docType = null;
					String searchFileName1 =null;
					String searchFileName2 =null;
					int countAfterExposure1 =0;
					int countAfterExposure2 =0;
					int responseCode = 0;
					
					//Prepare the violations
					ArrayList<String> violations = new ArrayList<String>();
					
					
					//Get the exposure count
					O365Document o365Document =null;
		//			o365Document = getExposures(isInternal, suiteData.getUsername(), violations,newFileName);
		//			int beforeCount = o365Document.getMeta().getTotalCount();
					//
		//			Reporter.log("######" + violationType + " exposure count before test::"+beforeCount, true);
					
					
					ArrayList<String> myAttachments = new ArrayList<String> ();
					ArrayList<String> renamedAttachment = new ArrayList<String>();
					ArrayList<String> attachmentLogs = new ArrayList<String>();
					myAttachments.add("PII.rtf"); 	   	
					myAttachments.add("PCI_Test.txt"); 
		//			myAttachments.add("Hello.java");    
		//			myAttachments.add("hipaa.txt");     
		//			myAttachments.add("encryption.bin");
		//			myAttachments.add("ferpa.pdf");     
		//			myAttachments.add("glba.txt");     
		//			myAttachments.add("vba_macro.xls"); 
		//			myAttachments.add("virus.zip");   	
		
		
		
					String bodyFile= "PCI_Test.txt";
		
		
					for(int i=0;i<=myAttachments.size()-1;i++){
						sourceFile1 = new File(userDir +myAttachments.get(i));
						newFileName1 = myUniqueId+myAttachments.get(i);
						destFile1 = new File(userDir +newFileName1);
						copyFileUsingFileChannels(sourceFile1, destFile1);
						renamedAttachment.add(destFile1.toString());
						filesToDelete.add(destFile1);
					}
		
		
		
					docType = "Email_File_Attachment";
					myMailSubject = myUniqueId+"ExposureInMailBodyAndMultipleAttachments"+violationType1;
		
					mailBody =readFile(userDir+bodyFile, Charset.defaultCharset());
		
		
					cleanupListSent.add(myMailSubject);
					synchronized(this){ success = objMailTestUser1.sendMail(externalUser1, myMailSubject,mailBody ,renamedAttachment , true);}
					assertTrue(success, "Failed sending mail with subject:"+mailSubject2+".");
					success=false;
		
		
					
					
					//wait for 1 minutes for the exposure to be applied
					Thread.sleep(CommonConstants.TWO_MINUTES_SLEEP);
					
					violations.clear();
					violations.add("pci");			
		
					
					
					int i = 120000;
					for (; i <= (MAX_EXPOSURE_WAIT_TIME_IN_MINUTES * 60 * 1000); i+=60000 ) {
						Reporter.log("Checking exposure after :"+TimeUnit.MILLISECONDS.toMinutes(i)+" minute(s)",true );
						Reporter.log("------------------------------------------------------------------------",true );
						
						//Get the exposure count
						o365Document = getExposures(isInternal, testUser2, violations,myMailSubject);
						countAfterExposure2 = o365Document.getMeta().getTotalCount();
						Reporter.log("Exposure Count ="+countAfterExposure2, true);
						
						if (countAfterExposure2 >= 1) {break;}
						Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
					}
					
					
					assertEquals(countAfterExposure2,1,"File not exposed even after waiting for "+TimeUnit.MILLISECONDS.toMinutes(i-60000)+" minutes.");
					
					
					violations.add("pii");		
					violations.add("pci");			
					violations.add("source_code");	
					violations.add("hipaa");		
					violations.add("encryption");	 
					violations.add("ferpa");	 	
					violations.add("glba");	 	
					violations.add("vba_macros");	
					violations.add("virus");
					int attachmentCount =myAttachments.size();
					int attachmentIteration =0;
		
					for(String filename : myAttachments) {
						++attachmentIteration; // iteration tracker
						searchFileName1=myUniqueId+filename;
						 i = 120000;
						for (; i <= (MAX_EXPOSURE_WAIT_TIME_IN_MINUTES * 60 * 1000); i+=60000 ) {
							Reporter.log("Checking exposure after :"+TimeUnit.MILLISECONDS.toMinutes(i)+" minute(s)",true );
							Reporter.log("------------------------------------------------------------------------",true );
							Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
		
							//Get the exposure count
							o365Document = getExposures(isInternal, testUser1, violations,searchFileName1);
							countAfterExposure1 = o365Document.getMeta().getTotalCount();
							Reporter.log("Exposure Count ="+countAfterExposure1, true);
		
							if (countAfterExposure1 >= 1) {break;}
						}
						assertEquals(countAfterExposure1,1,"File not exposed even after waiting for "+TimeUnit.MILLISECONDS.toMinutes(i-60000)+" minutes.");
		
					
				
					
					String docId1,docId2;
					
					docId1 = getDocID(searchFileName1);
					
					//Now apply the remedial action thro' API server call
					MailRemediation O365Remediation = getRemediationObject(testUser1, docType, docId1, remedialAction);
					
					/*//Now apply the remedial action thro' UI server or API server call
					if(service.equals(Server.UIServer.name())) {
						remediateExposure(suiteData.getTenantName(), facility.Office365.name(), suiteData.getUsername(), docId, null, remedialAction);
			
					} else if(service.equals(Server.APIServer.name())) {
						remediateExposureWithAPI(O365Remediation);
					} */
					
					//remediate
					responseCode = remediateExposureWithAPI(O365Remediation);
//					Assert.assertEquals(responseCode, 202, "Remediation call failed.");
					
					
					//Wait for remedial action
					Reporter.log("Waiting for the remedial action", true);
					
					i = 60000;
					boolean remediationSuccess = false;
					for (; i <= (MAX_REMEDIATION_WAIT_TIME_IN_MINUTES * 60 * 1000); i+=60000 ) {
						Reporter.log("Checking if mail deleted after :"+TimeUnit.MILLISECONDS.toMinutes(i)+" minute(s)",true );
						Reporter.log("------------------------------------------------------------------------",true );
						Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
						
						//Searching for email in mailbox sent items
						emailObj = objMailTestUser1.findItemInFolder(myMailSubject, "SentItems");
						
						//set remediationSuccess as true if mail or attachment deleted based on remediation type
						if (remedialAction.equals(Remediation.ITEM_DELETE_MAIL_BY_ATTACHMENT.name()) || remedialAction.equals(Remediation.ITEM_DELETE_MAIL.name())) {
							
							if(emailObj==null){
								remediationSuccess=true;
								
							}
							
						} else if(remedialAction.equals(Remediation.ITEM_DELETE_ATTACHMENT.name())) {
							if(emailObj!=null){
								//searching if attachments available
								emailObj = objMailTestUser1.findItemInFolderReturnWithAttachment(myMailSubject, "SentItems");
								if(emailObj.getAttachments().getCount()==(attachmentCount-attachmentIteration)){
									remediationSuccess=true;
								}
								
							}
							
						}
						
						
						if (remediationSuccess == true) {
							break;
						}
						else{
							Reporter.log("Mail/Attachment not yet deleted",true);
						}
						
					}
					
					//Verify the remediation thro' o365 API
					if (remedialAction.equals(Remediation.ITEM_DELETE_MAIL_BY_ATTACHMENT.name()) || remedialAction.equals(Remediation.ITEM_DELETE_MAIL.name())) {
						assertNull(emailObj, "Remediation "+ remedialAction + " didn't work. Mail is not deleted even after waiting for "+TimeUnit.MILLISECONDS.toMinutes(i-60000)+" minutes");
						
					} else if(remedialAction.equals(Remediation.ITEM_DELETE_ATTACHMENT.name())) {
						assertNotNull(emailObj, "Mail Not Found");
		//				assertTrue(emailObj.getAttachments().getCount() !=0 , "Test failed as both the attachments were deleted");
						assertTrue(emailObj.getAttachments().getCount() == (attachmentCount-attachmentIteration) , "Test failed as attachment was not deleted");
					}
					
					Reporter.log("Remediation successful at Office365 mail");
					
					//gather the forensic logs again 
					//logs = gatherForensicLogMessages(uniqueId, facility.Box.name());
					
					Reporter.log("Waiting for "+TimeUnit.MILLISECONDS.toMinutes(EXPOSURE_WAIT_TIME_AFTER_REMEDIATION)+" minutes to check exposure count after remediation");
					Thread.sleep(EXPOSURE_WAIT_TIME_AFTER_REMEDIATION);
					
					violations.clear();
					violations.add(violationType1);
					
					o365Document = getExposures("true",testUser1, violations,searchFileName1);
					int countAfterRemediation = o365Document.getMeta().getTotalCount();
					Reporter.log("###### " + violationType1 + " exposure count after remediation::"+countAfterRemediation, true);
					assertEquals(countAfterRemediation, 0, "Exposure count has not reduced after remediation.");
					
		//			violations.clear();
		//			violations.add(violationType2);
		//			o365Document = getExposures("true", testUser1, violations,searchFileName2);
		//			countAfterRemediation = o365Document.getMeta().getTotalCount();
		//			Reporter.log("###### " + violationType2 + " exposure count after remediation::"+countAfterRemediation, true);
		//			assertEquals(countAfterRemediation, 1, "Exposure count has changed for non-deleted file after remediation.");
		//			
		//			//Verify the activity logs
		//			ArrayList<String> availableLogs = searchLogs("Activity_type", "Delete", myUniqueId, testUser1, 1);
		//			
		//			String expectedLog = "QA Admin has remediated/deleted an attachment \""+newFileName1+"\" from an email with subject \""+myMailSubject+"\"";
		//			Reporter.log("Expected log:" + expectedLog, true);
		//			assertTrue(availableLogs.contains(expectedLog), expectedLog + " not present in the logs.");
					
					}
					//cleanup file if everything goes well
					//objMail.deleteMail(myMailSubject,DeleteMode.MoveToDeletedItems);
				}

		/**
		 * @throws Exception
		 */
		@Test(groups={"MAIL4","P1"},retryAnalyzer=RetryAnalyzer.class)
		public void verifyExposureOnDeleteMailFromMailboxSentToExternalUser_BCB_23() throws Exception {
			
			Reporter.log("****************Test Case Description****************",true);
			Reporter.log("Test Case Description: Verify that on deleting an email from sentItems which has risk in mail body and attachment,the mail and attachment is removed from exposure",true);
			Reporter.log("1)Send Mail risk in body and a risk file attachment to external user",true);
			Reporter.log("2)Verify that mail and files are exposed and delete from sentItem (without redemidation)",true);
			Reporter.log("3)Verify that the mail is deleted and attachments are removed from exposure tab",true);
			Reporter.log("*****************************************************",true);
			
			
			String isInternal =   "true";
			String exposuretype = "open";
			String violationType1 ="pii";
			String violationType2 ="pci";
			String fileName1 = "PII.rtf";
			String fileName2 = "PCI_Test.txt";
			String contentPlace ="both";
			String remedialAction ="ITEM_DELETE_MAIL";
			
			//Prepare the remediation object
			microsoft.exchange.webservices.data.core.service.item.Item emailObj = null;
			String myUniqueId = UUID.randomUUID().toString();
			ArrayList<String> myAttachment = new ArrayList<String>() ;
			String newFileName1 = myUniqueId+fileName1;
			String newFileName2 = myUniqueId+fileName2;
			File sourceFile1 =null;
			File destFile1 = null;
			File sourceFile2 =null;
			File destFile2 = null;
			String myMailSubject =null;
			String mailBody = null;
			boolean success =false;
			String docType = null;
			String searchFileName1 =null;
			String searchFileName2 =null;
			int countAfterExposure1 =0;
			int countAfterExposure2 =0;
			int responseCode = 0;
			
			//Prepare the violations
			ArrayList<String> violations = new ArrayList<String>();
			
			
			//Get the exposure count
			O365Document o365Document =null;
//			o365Document = getExposures(isInternal, suiteData.getUsername(), violations,newFileName);
//			int beforeCount = o365Document.getMeta().getTotalCount();
			//
//			Reporter.log("######" + violationType + " exposure count before test::"+beforeCount, true);
			
			sourceFile1 = new File(userDir +fileName1);
			destFile1 = new File(userDir +newFileName1);
			sourceFile2 = new File(userDir +fileName2);
			destFile2 = new File(userDir +newFileName2);
			
			
			if(contentPlace.equals("attachment")){
				docType = "Email_File_Attachment";
				myMailSubject = myUniqueId+"ExposureInMultipleAttachment";
				searchFileName1=newFileName1;
				searchFileName2=newFileName2;
				
				//Creating file with unique id name for upload
				copyFileUsingFileChannels(sourceFile1, destFile1);	
				copyFileUsingFileChannels(sourceFile2, destFile2);
				filesToDelete.add(destFile1);
				filesToDelete.add(destFile2);
				myAttachment.add(destFile1.toString());
				myAttachment.add(destFile2.toString());
				mailBody= "This is test mail body";
				
			}
			else if(contentPlace.equals("body")){
				docType = "Email_Message";
				myMailSubject = myUniqueId+"ViolationForExposureInMailBody"+violationType1;
				mailBody =readFile(sourceFile1.toString(), Charset.defaultCharset());
				searchFileName1=myMailSubject;
			}
			else if(contentPlace.equals("both")){
				docType = "Email_Message";
				myMailSubject = myUniqueId+"ExposureBodyAndAttachment";
				searchFileName1=newFileName1;
				searchFileName2=myMailSubject;
				
				//Creating file with unique id name for upload
				copyFileUsingFileChannels(sourceFile1, destFile1);
				filesToDelete.add(destFile1);
				myAttachment.add(destFile1.toString());
				mailBody =readFile(sourceFile2.toString(), Charset.defaultCharset());
				
			}
			
			cleanupListSent.add(myMailSubject);
			synchronized(this){ success = objMailTestUser1.sendMail(externalUser1, myMailSubject,mailBody ,myAttachment , true);}
			assertTrue(success, "Failed sending mail with subject:"+mailSubject2+".");
			success=false;
			
			
			//wait for 1 minutes for the exposure to be applied
//			Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
			
			violations.add(violationType1);
			int i = 120000;
			for (; i <= (MAX_EXPOSURE_WAIT_TIME_IN_MINUTES * 60 * 1000); i+=60000 ) {
				Reporter.log("Checking exposure after :"+TimeUnit.MILLISECONDS.toMinutes(i)+" minute(s)",true );
				Reporter.log("------------------------------------------------------------------------",true );
				Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
				
				//Get the exposure count
				o365Document = getExposures(isInternal, testUser1, violations,searchFileName1);
				countAfterExposure1 = o365Document.getMeta().getTotalCount();
				Reporter.log("Exposure Count ="+countAfterExposure1, true);
				
				if (countAfterExposure1 >= 1) {break;}
			}
			assertEquals(countAfterExposure1,1,"File not exposed even after waiting for "+TimeUnit.MILLISECONDS.toMinutes(i-60000)+" minutes.");
			violations.clear();
			violations.add(violationType2);
			for (; i <= (MAX_EXPOSURE_WAIT_TIME_IN_MINUTES * 60 * 1000); i+=60000 ) {
				Reporter.log("Checking exposure after :"+TimeUnit.MILLISECONDS.toMinutes(i)+" minute(s)",true );
				Reporter.log("------------------------------------------------------------------------",true );
				Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
				
				//Get the exposure count
				o365Document = getExposures(isInternal, testUser2, violations,searchFileName2);
				countAfterExposure2 = o365Document.getMeta().getTotalCount();
				Reporter.log("Exposure Count ="+countAfterExposure2, true);
				
				if (countAfterExposure2 >= 1) {break;}
			}
			
			
			assertEquals(countAfterExposure2,1,"File not exposed even after waiting for "+TimeUnit.MILLISECONDS.toMinutes(i-60000)+" minutes.");
			
			
			String docId1,docId2;
			
			docId1 = getDocID(searchFileName1);
			docId2 = getDocID(searchFileName2);
			
			Reporter.log("Deleting the email from SentItems of sender",true);
			
			synchronized(this){assertTrue(objMailTestUser1.deleteMailFromFolder(myMailSubject,"SentItems", DeleteMode.MoveToDeletedItems), "Delete mail from SentItems failed.");}
			
			i = 60000;
			boolean remediationSuccess = false;
			for (; i <= (MAX_REMEDIATION_WAIT_TIME_IN_MINUTES * 60 * 1000); i+=60000 ) {
				Reporter.log("Checking if mail deleted after :"+TimeUnit.MILLISECONDS.toMinutes(i)+" minute(s)",true );
				Reporter.log("------------------------------------------------------------------------",true );
				Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
				
				//Searching for email in mailbox sent items
				synchronized(this){emailObj = objMailTestUser1.findItemInFolder(myMailSubject, "SentItems");}
				
				
				if(emailObj==null){
					break;
				}
				else{
					Reporter.log("Mail/Attachment not yet deleted",true);
				}
				
			}
			
			Reporter.log("Waiting for "+TimeUnit.MILLISECONDS.toMinutes(EXPOSURE_WAIT_TIME_AFTER_REMEDIATION)+" minutes to check exposure count after deletion of email",true);
			Thread.sleep(EXPOSURE_WAIT_TIME_AFTER_REMEDIATION);
			
			violations.clear();
			violations.add(violationType1);
			
			o365Document = getExposures("true",testUser1, violations,searchFileName1);
			int countAfterRemediation = o365Document.getMeta().getTotalCount();
			Reporter.log("###### " + violationType1 + " exposure count after deletion of mail::"+countAfterRemediation, true);
			assertEquals(countAfterRemediation, 0, "Attachment is still exposed after deletion of email.");
			
			violations.clear();
			violations.add(violationType2);
			o365Document = getExposures("true", testUser1, violations,searchFileName2);
			countAfterRemediation = o365Document.getMeta().getTotalCount();
			Reporter.log("###### " + violationType2 + " exposure count after deletion of mail::"+countAfterRemediation, true);
			assertEquals(countAfterRemediation, 0, "Attachment is still exposed after deletion of email.");
			
			//Verify the activity logs
			//ArrayList<String> availableLogs = searchLogs("Activity_type", "Delete", myUniqueId, testUser1, 1);
			
//			String expectedLog = "QA Admin has remediated/deleted an email with subject \""+myMailSubject+"\"";
//			Reporter.log("Expected log:" + expectedLog, true);
//			assertTrue(availableLogs.contains(expectedLog), expectedLog + " not present in the logs.");
			
			//cleanup file if everything goes well
			//objMail.deleteMail(myMailSubject,DeleteMode.MoveToDeletedItems);
		}
		
		/**
		 * @throws Exception
		 */
		@Test(groups={"MAIL4","P1"},retryAnalyzer=RetryAnalyzer.class)
		public void verifyExposureOnDeleteMailFromMailboxReceivedFromExternalUser_BCB_23() throws Exception {
			
			Reporter.log("****************Test Case Description****************",true);
			Reporter.log("Test Case Description: Verify that on deleting a risky email from inbox which was received from external user,the mail and attachment is removed from exposure",true);
			Reporter.log("1)Send receive a mail with risk in body and a risk file attachment from external user",true);
			Reporter.log("2)Verify that mail and files are exposed and delete from Inbox (without redemidation)",true);
			Reporter.log("3)Verify that the mail is deleted and attachments are removed from exposure tab",true);
			Reporter.log("*****************************************************",true);

			
			String isInternal =   "false";
			String exposuretype = "open";
			String violationType1 ="pii";
			String violationType2 ="pci";
			String fileName1 = "PII.rtf";
			String fileName2 = "PCI_Test.txt";
			String contentPlace ="both";
			String remedialAction ="ITEM_DELETE_MAIL";

			//Prepare the remediation object
			microsoft.exchange.webservices.data.core.service.item.Item emailObj = null;
			String myUniqueId = UUID.randomUUID().toString();
			ArrayList<String> myAttachment = new ArrayList<String>() ;
			String newFileName1 = myUniqueId+fileName1;
			String newFileName2 = myUniqueId+fileName2;
			File sourceFile1 =null;
			File destFile1 = null;
			File sourceFile2 =null;
			File destFile2 = null;
			String myMailSubject =null;
			String mailBody = null;
			boolean success =false;
			String docType = null;
			String searchFileName1 =null;
			String searchFileName2 =null;
			int countAfterExposure1 =0;
			int countAfterExposure2 =0;
			int responseCode = 0;

			//Prepare the violations
			ArrayList<String> violations = new ArrayList<String>();
		

			//Get the exposure count
			O365Document o365Document =null;
//			o365Document = getExposures(isInternal, suiteData.getUsername(), violations,newFileName);
//			int beforeCount = o365Document.getMeta().getTotalCount();
			//
//			Reporter.log("######" + violationType + " exposure count before test::"+beforeCount, true);

			sourceFile1 = new File(userDir +fileName1);
			destFile1 = new File(userDir +newFileName1);
			sourceFile2 = new File(userDir +fileName2);
			destFile2 = new File(userDir +newFileName2);


			if(contentPlace.equals("attachment")){
				docType = "Email_File_Attachment";
				myMailSubject = myUniqueId+"ExposureInMultipleAttachment";
				searchFileName1=newFileName1;
				searchFileName2=newFileName2;

				//Creating file with unique id name for upload
				copyFileUsingFileChannels(sourceFile1, destFile1);	
				copyFileUsingFileChannels(sourceFile2, destFile2);
				filesToDelete.add(destFile1);
				filesToDelete.add(destFile2);
				myAttachment.add(destFile1.toString());
				myAttachment.add(destFile2.toString());
				mailBody= "This is test mail body";

			}
			else if(contentPlace.equals("body")){
				docType = "Email_Message";
				myMailSubject = myUniqueId+"ViolationForExposureInMailBody"+violationType1;
				mailBody =readFile(sourceFile1.toString(), Charset.defaultCharset());
				searchFileName1=myMailSubject;
			}
			else if(contentPlace.equals("both")){
				docType = "Email_Message";
				myMailSubject = myUniqueId+"ExposureBodyAndAttachment";
				searchFileName1=newFileName1;
				searchFileName2=myMailSubject;

				//Creating file with unique id name for upload
				copyFileUsingFileChannels(sourceFile1, destFile1);
				filesToDelete.add(destFile1);
				myAttachment.add(destFile1.toString());
				mailBody =readFile(sourceFile2.toString(), Charset.defaultCharset());

			}
			
			cleanupListSent.add(myMailSubject);
			synchronized(this){ success = objMailExternalUser.sendMail(testUser1, myMailSubject,mailBody ,myAttachment , false);}
			assertTrue(success, "Failed sending mail with subject:"+mailSubject2+".");
			success=false;


			//wait for 1 minutes for the exposure to be applied
//			Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
			
			violations.add(violationType1);
			int i = 120000;
			for (; i <= (MAX_EXPOSURE_WAIT_TIME_IN_MINUTES * 60 * 1000); i+=60000 ) {
				Reporter.log("Checking exposure after :"+TimeUnit.MILLISECONDS.toMinutes(i)+" minute(s)",true );
				Reporter.log("------------------------------------------------------------------------",true );
				Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);

				//Get the exposure count
				o365Document = getExposures(isInternal, testUser1, violations,searchFileName1);
				countAfterExposure1 = o365Document.getMeta().getTotalCount();
				Reporter.log("Exposure Count ="+countAfterExposure1, true);

				if (countAfterExposure1 >= 1) {break;}
			}
			assertEquals(countAfterExposure1,1,"File not exposed even after waiting for "+TimeUnit.MILLISECONDS.toMinutes(i-60000)+" minutes.");
			violations.clear();
			violations.add(violationType2);
			for (; i <= (MAX_EXPOSURE_WAIT_TIME_IN_MINUTES * 60 * 1000); i+=60000 ) {
				Reporter.log("Checking exposure after :"+TimeUnit.MILLISECONDS.toMinutes(i)+" minute(s)",true );
				Reporter.log("------------------------------------------------------------------------",true );
				Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);

				//Get the exposure count
				o365Document = getExposures(isInternal, testUser2, violations,searchFileName2);
				countAfterExposure2 = o365Document.getMeta().getTotalCount();
				Reporter.log("Exposure Count ="+countAfterExposure2, true);

				if (countAfterExposure2 >= 1) {break;}
			}
			
			
			assertEquals(countAfterExposure2,1,"File not exposed even after waiting for "+TimeUnit.MILLISECONDS.toMinutes(i-60000)+" minutes.");


			String docId1,docId2;
			
			docId1 = getDocID(searchFileName1);
			docId2 = getDocID(searchFileName2);
			
			//delete mail from inbox

			synchronized(this){assertTrue(objMailTestUser1.deleteMailFromFolder(myMailSubject,"Inbox", DeleteMode.MoveToDeletedItems), "Delete mail from SentItems failed.");}
			
			i = 60000;
			boolean remediationSuccess = false;
			for (; i <= (MAX_REMEDIATION_WAIT_TIME_IN_MINUTES * 60 * 1000); i+=60000 ) {
				Reporter.log("Checking if mail deleted after :"+TimeUnit.MILLISECONDS.toMinutes(i)+" minute(s)",true );
				Reporter.log("------------------------------------------------------------------------",true );
				Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);

				//Searching for email in mailbox sent items
				synchronized(this){emailObj = objMailTestUser1.findItemInFolder(myMailSubject, "Inbox");}


				if(emailObj==null){
					break;
				}
				else{
					Reporter.log("Mail/Attachment not yet deleted",true);
				}

			}
			
			Reporter.log("Waiting for "+TimeUnit.MILLISECONDS.toMinutes(EXPOSURE_WAIT_TIME_AFTER_REMEDIATION)+" minutes to check exposure count after deletion of mail",true);
			Thread.sleep(EXPOSURE_WAIT_TIME_AFTER_REMEDIATION);

			violations.clear();
			violations.add(violationType1);
			
			o365Document = getExposures("false",testUser1, violations,searchFileName1);
			int countAfterRemediation = o365Document.getMeta().getTotalCount();
			Reporter.log("###### " + violationType1 + " exposure count after deletion of mail::"+countAfterRemediation, true);
			assertEquals(countAfterRemediation, 0, "Attachment is still exposed after deletion of email.");
		
			violations.clear();
			violations.add(violationType2);
			o365Document = getExposures("false", testUser1, violations,searchFileName2);
			countAfterRemediation = o365Document.getMeta().getTotalCount();
			Reporter.log("###### " + violationType2 + " exposure count after deletion of mail::"+countAfterRemediation, true);
			assertEquals(countAfterRemediation, 0, "Attachment is still exposed after deletion of email.");

			//Verify the activity logs
			//ArrayList<String> availableLogs = searchLogs("Activity_type", "Delete", myUniqueId, testUser1, 1);
			
//			String expectedLog = "QA Admin has remediated/deleted an email with subject \""+myMailSubject+"\"";
//			Reporter.log("Expected log:" + expectedLog, true);
//			assertTrue(availableLogs.contains(expectedLog), expectedLog + " not present in the logs.");
			
			//cleanup file if everything goes well
			//objMail.deleteMail(myMailSubject,DeleteMode.MoveToDeletedItems);
		}
		
		/**
				 * @throws Exception
				 */
//				@Test(groups={"MAIL4"},retryAnalyzer=RetryAnalyzer.class)
				public void verifyExposureWhenMailAndAttachmentWithRiskSavedInDraftAndSentLater_BCB_24() throws Exception {
					
					Reporter.log("****************Test Case Description****************",true);
					Reporter.log("Test Case Description: Verify the 'Exposure', 'Other Risk' and CI logs when an email with risk in body and attachment is saved first in Drafts and sent later to an external user",true);
					Reporter.log("1)Create a mail with risk in body and attachment and save in Drafts",true);
					Reporter.log("2)Verify that the CI risk is detected and both body file and attachment file appears in Other risks files",true);
					Reporter.log("2)Now send the mail which was in draft",true);
					Reporter.log("3)Verify that the mail file and attachment file appears in exposure tab and removed from other tisks files",true);
					Reporter.log("*****************************************************",true);
		
					
					String isInternal =   "true";
					String exposuretype = "open";
					String violationType1 ="pii";
					String violationType2 ="pci";
					String fileName1 = "PII.rtf";
					String fileName2 = "PCI_Test.txt";
					String contentPlace ="both";
					String remedialAction ="ITEM_DELETE_MAIL";
		
					//Prepare the remediation object
					microsoft.exchange.webservices.data.core.service.item.Item emailObj = null;
					String myUniqueId = UUID.randomUUID().toString();
					ArrayList<String> myAttachment = new ArrayList<String>() ;
					String newFileName1 = myUniqueId+fileName1;
					String newFileName2 = myUniqueId+fileName2;
					File sourceFile1 =null;
					File destFile1 = null;
					File sourceFile2 =null;
					File destFile2 = null;
					String myMailSubject =null;
					String mailBody = null;
					boolean success =false;
					String docType = null;
					String searchFileName1 =null;
					String searchFileName2 =null;
					int countAfterExposure1 =0;
					int countAfterExposure2 =0;
					int responseCode = 0;
		
					//Prepare the violations
					ArrayList<String> violations = new ArrayList<String>();
					
					EmailAddressCollection externalEmailId = new EmailAddressCollection();
					externalEmailId.add(externalUser1);
				
		
					//Get the exposure count
					O365Document o365Document =null;
		//			o365Document = getExposures(isInternal, suiteData.getUsername(), violations,newFileName);
		//			int beforeCount = o365Document.getMeta().getTotalCount();
					//
		//			Reporter.log("######" + violationType + " exposure count before test::"+beforeCount, true);
		
					sourceFile1 = new File(userDir +fileName1);
					destFile1 = new File(userDir +newFileName1);
					sourceFile2 = new File(userDir +fileName2);
					destFile2 = new File(userDir +newFileName2);
		
		
					if(contentPlace.equals("attachment")){
						docType = "Email_File_Attachment";
						myMailSubject = myUniqueId+"SaveInDraftAndSendLaterExposureAttachment";
						searchFileName1=newFileName1;
						searchFileName2=newFileName2;
		
						//Creating file with unique id name for upload
						copyFileUsingFileChannels(sourceFile1, destFile1);	
						copyFileUsingFileChannels(sourceFile2, destFile2);
						filesToDelete.add(destFile1);
						filesToDelete.add(destFile2);
						myAttachment.add(destFile1.toString());
						myAttachment.add(destFile2.toString());
						mailBody= "This is test mail body";
		
					}
					else if(contentPlace.equals("body")){
						docType = "Email_Message";
						myMailSubject = myUniqueId+"SaveInDraftAndSendLaterExpsoureBody"+violationType1;
						mailBody =readFile(sourceFile1.toString(), Charset.defaultCharset());
						searchFileName1=myMailSubject;
					}
					else if(contentPlace.equals("both")){
						docType = "Email_Message";
						myMailSubject = myUniqueId+"SaveInDraftAndSendLaterExposureBoth";
						searchFileName1=newFileName1;
						searchFileName2=myMailSubject;
		
						//Creating file with unique id name for upload
						copyFileUsingFileChannels(sourceFile1, destFile1);	
						filesToDelete.add(destFile1);
						myAttachment.add(destFile1.toString());
						mailBody =readFile(sourceFile2.toString(), Charset.defaultCharset());
		
					}
					
					cleanupListSent.add(myMailSubject);
					synchronized(this){ success = objMailTestUser1.addAttachmentAndSaveInDraft(myMailSubject,mailBody ,myAttachment);}
					assertTrue(success, "Failed saving mail in Draft with subject:"+mailSubject2+".");
					success=false;
					
		
					
					//Verify the activity logs
					ArrayList<String> availableLogs = searchLogs("Activity_type", "Content Inspection", myUniqueId, testUser1, 2);
					
					String expectedLog = "Email file attachment "+newFileName1+" has risk(s) - "+violationType1.toUpperCase();
					Reporter.log("Expected log:" + expectedLog, true);
					assertTrue(availableLogs.contains(expectedLog), expectedLog + " File not exposed as CI message is not available now.");
					
		
					//wait for 1 minutes for the exposure to be applied
		//			Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
					
					violations.add(violationType1);
					int i = 60000;
					for (; i <= (MAX_EXPOSURE_WAIT_TIME_IN_MINUTES * 60 * 1000); i+=60000 ) {
						Reporter.log("Checking risky documents after :"+TimeUnit.MILLISECONDS.toMinutes(i)+" minute(s)",true );
						Reporter.log("------------------------------------------------------------------------",true );
						Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
		
						//Get the exposure count
						o365Document = getRiskyDocs(isInternal, testUser1, violations,searchFileName1);
						countAfterExposure1 = o365Document.getMeta().getTotalCount();
						Reporter.log("Risk Doc Count ="+countAfterExposure1, true);
		
						if (countAfterExposure1 >= 1) {break;}
					}
					assertEquals(countAfterExposure1,1,"File not appearing in risky documents even after waiting for "+TimeUnit.MILLISECONDS.toMinutes(i-60000)+" minutes.");
					violations.clear();
					violations.add(violationType2);
					for (; i <= (MAX_EXPOSURE_WAIT_TIME_IN_MINUTES * 60 * 1000); i+=60000 ) {
						Reporter.log("Checking risky documents after :"+TimeUnit.MILLISECONDS.toMinutes(i)+" minute(s)",true );
						Reporter.log("------------------------------------------------------------------------",true );
						Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
		
						//Get the exposure count
						o365Document = getRiskyDocs(isInternal, testUser2, violations,searchFileName2);
						countAfterExposure2 = o365Document.getMeta().getTotalCount();
						Reporter.log("Risk Doc Count ="+countAfterExposure2, true);
		
						if (countAfterExposure2 >= 1) {break;}
					}
					
					
					assertEquals(countAfterExposure2,1,"File not appearing in risky documents even after waiting for "+TimeUnit.MILLISECONDS.toMinutes(i-60000)+" minutes.");
		
		
					String docId1,docId2;
					
					docId1 = getDocID(searchFileName1);
					docId2 = getDocID(searchFileName2);
		
					cleanupListSent.add(myMailSubject);
					synchronized(this){ success = objMailTestUser1.sendMailFromDraft(myMailSubject,externalEmailId);}
					assertTrue(success, "Failed sending mail from draft with subject:"+myMailSubject+".");
					success=false;
		
		
					//Wait for remedial action
					Reporter.log("Waiting for mail and attachment exposure.", true);
					
					violations.add(violationType1);
					 i = 60000;
					for (; i <= (MAX_EXPOSURE_WAIT_TIME_IN_MINUTES * 60 * 1000); i+=60000 ) {
						Reporter.log("Checking risky documents after :"+TimeUnit.MILLISECONDS.toMinutes(i)+" minute(s)",true );
						Reporter.log("------------------------------------------------------------------------",true );
						Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
		
						//Get the exposure count
						o365Document = getRiskyDocs(isInternal, testUser1, violations,searchFileName1);
						countAfterExposure1 = o365Document.getMeta().getTotalCount();
						Reporter.log("Risk Doc Count ="+countAfterExposure1, true);
		
						if (countAfterExposure1 == 0) {break;}
					}
					assertEquals(countAfterExposure1,0,"File still appearing in risky documents even after waiting for "+TimeUnit.MILLISECONDS.toMinutes(i-60000)+" minutes.");
					violations.clear();
					violations.add(violationType2);
					for (; i <= (MAX_EXPOSURE_WAIT_TIME_IN_MINUTES * 60 * 1000); i+=60000 ) {
						Reporter.log("Checking risky documents after :"+TimeUnit.MILLISECONDS.toMinutes(i)+" minute(s)",true );
						Reporter.log("------------------------------------------------------------------------",true );
						Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
		
						//Get the exposure count
						o365Document = getRiskyDocs(isInternal, testUser2, violations,searchFileName2);
						countAfterExposure2 = o365Document.getMeta().getTotalCount();
						Reporter.log("Risk Doc Count ="+countAfterExposure2, true);
		
						if (countAfterExposure2 == 0) {break;}
					}
					assertEquals(countAfterExposure2,0,"File still appearing in risky documents even after waiting for "+TimeUnit.MILLISECONDS.toMinutes(i-60000)+" minutes.");
					
					
		
			
					violations.clear();
					violations.add(violationType1);
					
					o365Document = getExposures("true",testUser1, violations,searchFileName1);
					int countAfterRemediation = o365Document.getMeta().getTotalCount();
					Reporter.log("###### " + violationType1 + " exposure count ::"+countAfterRemediation, true);
					assertEquals(countAfterRemediation, 1, "Attachment is still not exposed after sending of email.");
				
					violations.clear();
					violations.add(violationType2);
					o365Document = getExposures("true", testUser1, violations,searchFileName2);
					countAfterRemediation = o365Document.getMeta().getTotalCount();
					Reporter.log("###### " + violationType2 + " exposure count::"+countAfterRemediation, true);
					assertEquals(countAfterRemediation,1 , "Attachment is still not exposed after sending of email.");
		
					//Verify the activity logs
					availableLogs=null;
					availableLogs = searchLogs("Object_type", "Email_File_Attachment", myUniqueId, testUser1, 1);
					
					expectedLog = "User sent an email  to "+externalUser1+" with subject \""+myMailSubject+"\" with attachment \""+newFileName1+"\"";
					Reporter.log("Expected log:" + expectedLog, true);
					assertTrue(availableLogs.contains(expectedLog), expectedLog + " not present in the logs.");
					
					//cleanup file if everything goes well
					//objMail.deleteMail(myMailSubject,DeleteMode.MoveToDeletedItems);
				}

		@DataProvider(name = "dataProviderContentLocation",parallel=true)
		public static Object[][] dataProviderContentLocation() {
			return new Object[][] { 
				//riskContentLocation
				{ "body" },
//				{ "attachment" },
//				{ "both" }
			};
		}

		/**
					  * @throws Exception
					  */
					 @Test(groups={"MAIL4","P2"}, dataProvider = "dataProviderContentLocation")
					 public void verifyExposureOnDeleteMailFromMailboxMultipleUsers(String contentPlace) throws Exception {
		
						 Reporter.log("****************Test Case Description****************",true);
						 Reporter.log("Verify that on deleting risk email/attachment from inbox of multiple users ,the mail and attachment is removed from exposure only on deleting from all internal users",true);
						 Reporter.log("1)Send Mail risk in body and a risk file attachment to multiple internal and external users",true);
						 Reporter.log("2)Verify that mail and files are exposed and they don't appear in others risks files",true);
						 Reporter.log("3)Delete the mail from inbox of internal user, verify exposed files. Verify the file is in exposed state but internal user was removed from details. ",true);
						 Reporter.log("4)Delete the mail from sentItem  of sender, Verify that the file gets removed from exposure .",true);
						 Reporter.log("*****************************************************",true);
		
		
						 String isInternal =   "true";
						 String exposuretype = "open";
						 String violationType1 ="pii";
						 String violationType2 ="pci";
						 String fileName1 = "PII.rtf";
						 String fileName2 = "PCI_Test.txt";
						 String remedialAction ="ITEM_DELETE_MAIL";
		
						 EmailAddressCollection recipientsTo = new EmailAddressCollection();
						 EmailAddressCollection recipientsCc = new EmailAddressCollection();
		
						 //Prepare the remediation object
						 microsoft.exchange.webservices.data.core.service.item.Item emailObj = null;
						 String myUniqueId = UUID.randomUUID().toString();
						 ArrayList<String> myAttachment = new ArrayList<String>() ;
						 String newFileName1 = myUniqueId+fileName1;
						 String newFileName2 = myUniqueId+fileName2;
						 File sourceFile1 =null;
						 File destFile1 = null;
						 File sourceFile2 =null;
						 File destFile2 = null;
						 String myMailSubject =null;
						 String mailBody = null;
						 boolean success =false;
						 String docType = null;
						 String searchFileName1 =null;
						 String searchFileName2 =null;
						 int countAfterExposure1 =0;
						 int countAfterExposure2 =0;
						 int responseCode = 0;
		
						 //Prepare the violations
						 ArrayList<String> violations = new ArrayList<String>();
		
		
						 //Get the exposure count
						 O365Document o365Document =null;
						 //				o365Document = getExposures(isInternal, suiteData.getUsername(), violations,newFileName);
						 //				int beforeCount = o365Document.getMeta().getTotalCount();
						 //
						 //				Reporter.log("######" + violationType + " exposure count before test::"+beforeCount, true);
		
						 sourceFile1 = new File(userDir +fileName1);
						 destFile1 = new File(userDir +newFileName1);
						 sourceFile2 = new File(userDir +fileName2);
						 destFile2 = new File(userDir +newFileName2);
		
		
						 if(contentPlace.equals("attachment")){
							 docType = "Email_File_Attachment";
							 myMailSubject = myUniqueId+"ExposureInAttachment";
							 searchFileName1=newFileName1;
							 searchFileName2=newFileName2;
		
							 //Creating file with unique id name for upload
							 copyFileUsingFileChannels(sourceFile1, destFile1);	
							 copyFileUsingFileChannels(sourceFile2, destFile2);
							 filesToDelete.add(destFile1);
							 filesToDelete.add(destFile2);
							 myAttachment.add(destFile1.toString());
							 myAttachment.add(destFile2.toString());
							 mailBody= "This is test mail body";
		
						 }
						 else if(contentPlace.equals("body")){
							 docType = "Email_Message";
							 myMailSubject = myUniqueId+"ViolationForExposureInMailBody"+violationType1;
							 mailBody =readFile(sourceFile1.toString(), Charset.defaultCharset());
							 searchFileName1=myMailSubject;
						 }
						 else if(contentPlace.equals("both")){
							 docType = "Email_Message";
							 myMailSubject = myUniqueId+"ExposureBodyAndAttachment";
							 searchFileName1=newFileName1;
							 searchFileName2=myMailSubject;
		
							 //Creating file with unique id name for upload
							 copyFileUsingFileChannels(sourceFile1, destFile1);
							 filesToDelete.add(destFile1);
							 myAttachment.add(destFile1.toString());
							 mailBody =readFile(sourceFile2.toString(), Charset.defaultCharset());
		
						 }
		
						 //2 internal and 2 external users
						 recipientsTo.add(externalUser1);
						 recipientsTo.add(adminMailId);
						 recipientsCc.add(externalUser2);
						 recipientsCc.add(testUser2);
		
						 cleanupListSent.add(myMailSubject);
						 synchronized(this){ success = objMailTestUser1.sendMailWithCCAndBCC(recipientsTo,recipientsCc,null, myMailSubject,mailBody ,myAttachment , true);}
						 assertTrue(success, "Failed sending mail with subject:"+myMailSubject+".");
						 success=false;
		
		
						 //wait for 2 minutes for the exposure to be applied
						 Thread.sleep(CommonConstants.TWO_MINUTES_SLEEP);
						 
						 ArrayList<String> myMailMessages = new ArrayList<String>();
							ArrayList<String> logs;
							int i = 180000;
							try {
								for (; i <= ( MAX_LOG_WAIT_TIME_IN_MINUTES * 60 * 1000); i+=60000 ) {
									Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
									Reporter.log("------------------------------------------------------------------------",true );
									Reporter.log("Searching for logs after :"+TimeUnit.MILLISECONDS.toMinutes(i)+" minutes",true );
									Reporter.log("------------------------------------------------------------------------" ,true);
									logs = searchDisplayLogs(fromTime, 60, "Office 365","Object_type", "Email_Message", myUniqueId, suiteData.getUsername(), suiteData.getCSRFToken(), suiteData.getSessionID(),"");
									myMailMessages.addAll(logs);
									

									displayMessageContent(myMailMessages);

									//Reporter.log("Actual file messages:" + actualMailMessages, true);
									if (myMailMessages.size() >= 1) {break;}
									else{myMailMessages.clear();}
								}
							}
							catch(Exception e) {}
							assertEquals(myMailMessages.size(),1,"All Recieved/sent logs not found even after waiting for "+TimeUnit.MILLISECONDS.toMinutes(i-60000)+" minutes.");
		
						 violations.add(violationType1);
						  i = 120000;
						 for (; i <= (MAX_EXPOSURE_WAIT_TIME_IN_MINUTES * 60 * 1000); i+=60000 ) {
							 Reporter.log("Checking exposure after :"+TimeUnit.MILLISECONDS.toMinutes(i)+" minute(s)",true );
							 Reporter.log("------------------------------------------------------------------------",true );
							 Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
		
							 //Get the exposure count
							 o365Document = getExposures(isInternal, testUser1, violations,searchFileName1);
							 countAfterExposure1 = o365Document.getMeta().getTotalCount();
							 Reporter.log("Exposure Count ="+countAfterExposure1, true);
		
							 if (countAfterExposure1 >= 1) {break;}
						 }
						 //Verify that only 1 exposure record is created though there were multiple users cced in the email
						 assertEquals(countAfterExposure1,1,"File not exposed even after waiting for "+TimeUnit.MILLISECONDS.toMinutes(i-60000)+" minutes.");
		
		
						 //Verifying the users in the exposure
						 assertTrue(o365Document.getObjects().get(0).getExposures().getInternal().toString().contains(testUser2),"User not found in exposure internal user list: "+testUser2);
						 assertTrue(o365Document.getObjects().get(0).getExposures().getInternal().toString().contains(adminMailId),"User not found in exposure in internal user list: "+adminMailId);
						 assertTrue(o365Document.getObjects().get(0).getExposures().getExternal().toString().contains(externalUser1),"User not found in exposure in external user list: "+externalUser1);
						 assertTrue(o365Document.getObjects().get(0).getExposures().getExternal().toString().contains(externalUser2),"User not found in exposure in external user list: "+externalUser2);
		
						 String myResponse;
						 o365Document = getExposedUsers("collab",isInternal,testUser1, violations,searchFileName1);
						 myResponse = MarshallingUtils.marshall(o365Document);
		//				 assertTrue(myResponse.contains(testUser2),"User email not found in collaborators list:"+testUser2 +".");		
		//				 assertTrue(myResponse.contains(adminMailId),"User email not found in collaborators list:"+adminMailId +".");		
						 assertTrue(myResponse.contains(externalUser1),"User email not found in collaborators list:"+externalUser1 +".");		
						 assertTrue(myResponse.contains(externalUser2),"User email not found in collaborators list:"+externalUser2 +".");		
						 o365Document = getExposedUsers("users",isInternal,testUser1, violations,searchFileName1);
						 myResponse = MarshallingUtils.marshall(o365Document);
						 assertTrue(myResponse.contains(testUser1),"User email not found in owner list:"+testUser1 +".");
		
		
		
						 if(contentPlace.equals("body")==false){
							 violations.clear();
							 violations.add(violationType2);
							 for (; i <= (MAX_EXPOSURE_WAIT_TIME_IN_MINUTES * 60 * 1000); i+=60000 ) {
								 Reporter.log("Checking exposure after :"+TimeUnit.MILLISECONDS.toMinutes(i)+" minute(s)",true );
								 Reporter.log("------------------------------------------------------------------------",true );
								 Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
		
								 //Get the exposure count
								 o365Document = getExposures(isInternal, testUser2, violations,searchFileName2);
								 countAfterExposure2 = o365Document.getMeta().getTotalCount();
								 Reporter.log("Exposure Count ="+countAfterExposure2, true);
		
								 if (countAfterExposure2 >= 1) {break;}
							 }
		
		
							 assertEquals(countAfterExposure2,1,"File not exposed even after waiting for "+TimeUnit.MILLISECONDS.toMinutes(i-60000)+" minutes.");
		
							 //Verifying the users in the exposure
							 assertTrue(o365Document.getObjects().get(0).getExposures().getInternal().toString().contains(testUser2),"User not found in exposure internal user list: "+testUser2);
							 assertTrue(o365Document.getObjects().get(0).getExposures().getInternal().toString().contains(adminMailId),"User not found in exposure in internal user list: "+adminMailId);
							 assertTrue(o365Document.getObjects().get(0).getExposures().getExternal().toString().contains(externalUser1),"User not found in exposure in external user list: "+externalUser1);
							 assertTrue(o365Document.getObjects().get(0).getExposures().getExternal().toString().contains(externalUser2),"User not found in exposure in external user list: "+externalUser2);
		
							 o365Document = getExposedUsers("collab",isInternal,testUser1, violations,searchFileName2);
							 myResponse = MarshallingUtils.marshall(o365Document);
		//					 assertTrue(myResponse.contains(testUser2),"User email not found in collaborators list:"+testUser2 +".");		
		//					 assertTrue(myResponse.contains(adminMailId),"User email not found in collaborators list:"+adminMailId +".");		
							 assertTrue(myResponse.contains(externalUser1),"User email not found in collaborators list:"+externalUser1 +".");		
							 assertTrue(myResponse.contains(externalUser2),"User email not found in collaborators list:"+externalUser2 +".");		
							 o365Document = getExposedUsers("users",isInternal,testUser1, violations,searchFileName2);
							 myResponse = MarshallingUtils.marshall(o365Document);
							 assertTrue(myResponse.contains(testUser1),"User email not found in owner list:"+testUser1 +".");
						 }
						 //check riskyDocs section
						 O365Document riskyDocs = getRiskyDocs(isInternal, testUser1, violations,searchFileName1);
						 assertEquals(riskyDocs.getMeta().getTotalCount(),0,"Exposed Mail or attachment shouldn't be present in risky docs. But found for the file:"+searchFileName1+".");
						 riskyDocs = getRiskyDocs(isInternal, testUser1, violations,searchFileName2);
						 assertEquals(riskyDocs.getMeta().getTotalCount(),0,"Exposed Mail or attachment shouldn't be present in risky docs. But found for the file:"+searchFileName2+".");
		
		
		
						 String docId1,docId2;
		
						 docId1 = getDocID(searchFileName1);
		
						 if(contentPlace.equals("body")==false){
							 docId2 = getDocID(searchFileName2);
						 }
		
						 //==================================================================================
						 Reporter.log("Deleting the email from Inbox of internal recipient:"+testUser2,true);
						 synchronized(this){objMailTestUser2 = new Office365MailActivities(testUser2,testUser2Pwd);}
						 assertTrue(objMailTestUser2.deleteMailFromFolder(myMailSubject,"Inbox", DeleteMode.MoveToDeletedItems), "Delete mail from Inbox failed for user:"+testUser2);
		
		
						 Reporter.log("Waiting for "+TimeUnit.MILLISECONDS.toMinutes(EXPOSURE_WAIT_TIME_AFTER_REMEDIATION)+" minutes to check exposure count after deletion of email",true);
						 Thread.sleep(EXPOSURE_WAIT_TIME_AFTER_REMEDIATION);
		
						 violations.clear();
						 violations.add(violationType1);				
						 o365Document = getExposures("true",testUser1, violations,searchFileName1);
						 int countAfterRemediation = o365Document.getMeta().getTotalCount();
						 Reporter.log("Verifying Exposure count after deletion of mail from Inbox of internal recipient:"+testUser2, true);
						 assertEquals(countAfterRemediation, 1, "File :"+searchFileName1+" shouldn't be removed from exposure after deletion of mail from Inbox of one internal recipient:"+testUser2+ " as two more internal users has this email in mailbox:");
		
						 //Verifying the users in the exposure for searchFileName1
						 assertFalse(o365Document.getObjects().get(0).getExposures().getInternal().toString().contains(testUser2),"After deletion of mail from Inbox of internal user:"+testUser2+" the user is still present in the internal user list for the exposed file"+searchFileName1+".");
						 assertTrue(o365Document.getObjects().get(0).getExposures().getInternal().toString().contains(adminMailId),"After deletion of mail from Inbox of internal user:"+testUser2+", User:"+adminMailId+" not found in the internal user list for the exposed file"+searchFileName1+".");
						 assertTrue(o365Document.getObjects().get(0).getExposures().getExternal().toString().contains(externalUser1),"After deletion of mail from Inbox of internal user:"+testUser2+", User:"+externalUser1+" not found in the external user list for the exposed file"+searchFileName1+".");
						 assertTrue(o365Document.getObjects().get(0).getExposures().getExternal().toString().contains(externalUser2),"After deletion of mail from Inbox of internal user:"+testUser2+", User:"+externalUser2+" not found in the external user list for the exposed file"+searchFileName1+".");
						 assertEquals(o365Document.getObjects().get(0).getPath(),"multiple","Field path is not multiple.");
		
						 o365Document = getExposedUsers("collab",isInternal,testUser1, violations,searchFileName1);
						 myResponse = MarshallingUtils.marshall(o365Document);
		//				 assertFalse(myResponse.contains(testUser2),"After deletion of mail from Inbox of internal user:"+testUser2+" the user is still present in the collaborator for the exposed file"+searchFileName1+".");		
		//				 assertTrue(myResponse.contains(adminMailId),"User email not found in collaborators list:"+adminMailId +".");		
						 assertTrue(myResponse.contains(externalUser1),"User email not found in collaborators list:"+externalUser1 +".");		
						 assertTrue(myResponse.contains(externalUser2),"User email not found in collaborators list:"+externalUser2 +".");		
						 o365Document = getExposedUsers("users",isInternal,testUser1, violations,searchFileName1);
						 myResponse = MarshallingUtils.marshall(o365Document);
						 assertTrue(myResponse.contains(testUser1),"User email not found in owner list:"+testUser1 +".");
		
						 if(contentPlace.equals("body")==false){
							 violations.clear();
							 violations.add(violationType2);
							 o365Document = getExposures("true",testUser1, violations,searchFileName2);
							 countAfterRemediation = o365Document.getMeta().getTotalCount();
							 Reporter.log("Verifying Exposure count after deletion of mail from Inbox of internal recipient:"+testUser2, true);
							 assertEquals(countAfterRemediation, 1, "File :"+searchFileName2+" shouldn't be removed from exposure after deletion of mail from Inbox of one internal recipient:"+testUser2+ " as two more internal users has this email in mailbox:");
		
							 //Verifying the users in the exposure for searchFileName2
							 assertFalse(o365Document.getObjects().get(0).getExposures().getInternal().toString().contains(testUser2),"After deletion of mail from Inbox of internal user:"+testUser2+" the user is still present in the internal user list for the exposed file"+searchFileName2+".");
							 assertTrue(o365Document.getObjects().get(0).getExposures().getInternal().toString().contains(adminMailId),"After deletion of mail from Inbox of internal user:"+testUser2+", User:"+adminMailId+" not found in the internal user list for the exposed file"+searchFileName2+".");
							 assertTrue(o365Document.getObjects().get(0).getExposures().getExternal().toString().contains(externalUser1),"After deletion of mail from Inbox of internal user:"+testUser2+", User:"+externalUser1+" not found in the external user list for the exposed file"+searchFileName2+".");
							 assertTrue(o365Document.getObjects().get(0).getExposures().getExternal().toString().contains(externalUser2),"After deletion of mail from Inbox of internal user:"+testUser2+", User:"+externalUser2+" not found in the external user list for the exposed file"+searchFileName2+".");
							 assertEquals(o365Document.getObjects().get(0).getPath(),"multiple","Field path is not multiple.");
		
							 o365Document = getExposedUsers("collab",isInternal,testUser1, violations,searchFileName2);
							 myResponse = MarshallingUtils.marshall(o365Document);
		//					 assertTrue(myResponse.contains(testUser2),"After deletion of mail from Inbox of internal user:"+testUser2+" the user is not present in the collaborator for the exposed file"+searchFileName2+".");		
		//					 assertTrue(myResponse.contains(adminMailId),"User email not found in collaborators list:"+adminMailId +".");		
							 assertTrue(myResponse.contains(externalUser1),"User email not found in collaborators list:"+externalUser1 +".");		
							 assertTrue(myResponse.contains(externalUser2),"User email not found in collaborators list:"+externalUser2 +".");		
							 o365Document = getExposedUsers("users",isInternal,testUser1, violations,searchFileName2);
							 myResponse = MarshallingUtils.marshall(o365Document);
							 assertTrue(myResponse.contains(testUser1),"User email not found in owner list:"+testUser1 +".");
						 }
						 //check riskyDocs section
						 riskyDocs = getRiskyDocs(isInternal, testUser1, violations,searchFileName1);
						 assertEquals(riskyDocs.getMeta().getTotalCount(),0,"Exposed Mail or attachment shouldn't be present in risky docs. But found for the file:"+searchFileName1+".");
						 riskyDocs = getRiskyDocs(isInternal, testUser1, violations,searchFileName2);
						 assertEquals(riskyDocs.getMeta().getTotalCount(),0,"Exposed Mail or attachment shouldn't be present in risky docs. But found for the file:"+searchFileName2+".");
		
						 //==================================================================================
						 Reporter.log("Deleting the email from SentItems of sender:"+testUser1,true);
						 synchronized(this){objMailTestUser2 = new Office365MailActivities(testUser2,testUser2Pwd);}
						 assertTrue(objMailTestUser1.deleteMailFromFolder(myMailSubject,"SentItems", DeleteMode.MoveToDeletedItems), "Delete mail from SentItems failed for user:"+testUser1);
		
		
						 Reporter.log("Waiting for "+TimeUnit.MILLISECONDS.toMinutes(EXPOSURE_WAIT_TIME_AFTER_REMEDIATION)+" minutes to check exposure count after deletion of email",true);
						 Thread.sleep(EXPOSURE_WAIT_TIME_AFTER_REMEDIATION);
		
						 violations.clear();
						 violations.add(violationType1);				
						 o365Document = getExposures("true",testUser1, violations,searchFileName1);
						 countAfterRemediation = o365Document.getMeta().getTotalCount();
						 Reporter.log("Verifying Exposure count after deletion of mail from SentItems of sender:"+testUser1, true);
						 assertEquals(countAfterRemediation, 1, "File :"+searchFileName1+" shouldn't be removed from exposure after deletion of mail from SentItems of sender "+testUser1+ " as one more internal user has this email in inbox:");
		
						 //Verifying the users in the exposure for searchFileName1
						 assertFalse(o365Document.getObjects().get(0).getExposures().getInternal().toString().contains(testUser2),"After deletion of mail from SentItems of sender:"+testUser1+" the user is still present in the internal user list for the exposed file"+searchFileName1+".");
						 assertTrue(o365Document.getObjects().get(0).getExposures().getInternal().toString().contains(adminMailId),"After deletion of mail from SentItems of sender:"+testUser1+", User:"+adminMailId+" not found in the internal user list for the exposed file"+searchFileName1+".");
						 assertTrue(o365Document.getObjects().get(0).getExposures().getExternal().toString().contains(externalUser1),"After deletion of mail from SentItems of sender:"+testUser1+", User:"+externalUser1+" not found in the external user list for the exposed file"+searchFileName1+".");
						 assertTrue(o365Document.getObjects().get(0).getExposures().getExternal().toString().contains(externalUser2),"After deletion of mail from SentItems of sender:"+testUser1+", User:"+externalUser2+" not found in the external user list for the exposed file"+searchFileName1+".");
						 assertEquals(o365Document.getObjects().get(0).getPath(),"multiple","Field path is not multiple.");
		
						 o365Document = getExposedUsers("collab",isInternal,testUser1, violations,searchFileName1);
						 myResponse = MarshallingUtils.marshall(o365Document);
		//				 assertTrue(myResponse.contains(testUser2),"After deletion of mail from SentItems of internal user:"+testUser1+" the user is not present in the collaborator for the exposed file"+searchFileName1+".");		
		//				 assertTrue(myResponse.contains(adminMailId),"User email not found in collaborators list:"+adminMailId +".");		
						 assertTrue(myResponse.contains(externalUser1),"User email not found in collaborators list:"+externalUser1 +".");		
						 assertTrue(myResponse.contains(externalUser2),"User email not found in collaborators list:"+externalUser2 +".");		
						 o365Document = getExposedUsers("users",isInternal,testUser1, violations,searchFileName1);
						 myResponse = MarshallingUtils.marshall(o365Document);
						 assertTrue(myResponse.contains(testUser1),"User email not found in owner list:"+testUser1 +".");
		
						 if(contentPlace.equals("body")==false){
							 violations.clear();
							 violations.add(violationType2);
							 o365Document = getExposures("true",testUser1, violations,searchFileName2);
							 countAfterRemediation = o365Document.getMeta().getTotalCount();
							 Reporter.log("Verifying Exposure count after deletion of mail from Inbox of internal recipient:"+testUser2, true);
							 assertEquals(countAfterRemediation, 1, "File :"+searchFileName2+" shouldn't be removed from exposure after deletion of mail from SentItems of one internal recipient:"+testUser1+ " as one more internal user has this email in inbox:");
		
							 //Verifying the users in the exposure for searchFileName2
							 assertFalse(o365Document.getObjects().get(0).getExposures().getInternal().toString().contains(testUser2),"After deletion of mail from SentItems of sender:"+testUser1+" the user is still present in the internal user list for the exposed file"+searchFileName2+".");
							 assertTrue(o365Document.getObjects().get(0).getExposures().getInternal().toString().contains(adminMailId),"After deletion of mail from SentItems of sender:"+testUser1+", User:"+adminMailId+" not found in the internal user list for the exposed file"+searchFileName2+".");
							 assertTrue(o365Document.getObjects().get(0).getExposures().getExternal().toString().contains(externalUser1),"After deletion of mail from SentItems of sender:"+testUser1+", User:"+externalUser1+" not found in the external user list for the exposed file"+searchFileName2+".");
							 assertTrue(o365Document.getObjects().get(0).getExposures().getExternal().toString().contains(externalUser2),"After deletion of mail from SentItems of sender:"+testUser1+", User:"+externalUser2+" not found in the external user list for the exposed file"+searchFileName2+".");
							 assertEquals(o365Document.getObjects().get(0).getPath(),"multiple","Field path is not multiple.");
		
							 o365Document = getExposedUsers("collab",isInternal,testUser1, violations,searchFileName2);
							 myResponse = MarshallingUtils.marshall(o365Document);
		//					 assertTrue(myResponse.contains(testUser2),"After deletion of mail from SentItems of internal user:"+testUser1+" the user is not present in the collaborator for the exposed file"+searchFileName1+".");		
		//					 assertTrue(myResponse.contains(adminMailId),"User email not found in collaborators list:"+adminMailId +".");		
							 assertTrue(myResponse.contains(externalUser1),"User email not found in collaborators list:"+externalUser1 +".");		
							 assertTrue(myResponse.contains(externalUser2),"User email not found in collaborators list:"+externalUser2 +".");		
							 o365Document = getExposedUsers("users",isInternal,testUser1, violations,searchFileName2);
							 myResponse = MarshallingUtils.marshall(o365Document);
							 assertTrue(myResponse.contains(testUser1),"User email not found in owner list:"+testUser1 +".");
						 }
						 riskyDocs = getRiskyDocs(isInternal, testUser1, violations,searchFileName1);
						 assertEquals(riskyDocs.getMeta().getTotalCount(),0,"Exposed Mail or attachment shouldn't be present in risky docs. But found for the file:"+searchFileName1+".");
						 riskyDocs = getRiskyDocs(isInternal, testUser1, violations,searchFileName2);
						 assertEquals(riskyDocs.getMeta().getTotalCount(),0,"Exposed Mail or attachment shouldn't be present in risky docs. But found for the file:"+searchFileName2+".");
		
						 //==================================================================================
						 Reporter.log("Final:Deleting the email from Inbox of internal recipient:"+adminMailId,true);
						 synchronized(this){assertTrue(objMailSysAdmin.deleteMailFromFolder(myMailSubject,"Inbox", DeleteMode.MoveToDeletedItems), "Delete mail from Inbox failed for user:"+adminMailId);}
		
		
						 Reporter.log("Waiting for "+TimeUnit.MILLISECONDS.toMinutes(EXPOSURE_WAIT_TIME_AFTER_REMEDIATION)+" minutes to check exposure count after deletion of email",true);
						 Thread.sleep(EXPOSURE_WAIT_TIME_AFTER_REMEDIATION);
		
						 Reporter.log("Verifying Exposure count after deletion of mail from Inbox of internal recipient:"+adminMailId, true);
						 violations.clear();
						 violations.add(violationType1);				
						 o365Document = getExposures("true",testUser1, violations,searchFileName1);
						 countAfterRemediation = o365Document.getMeta().getTotalCount();
						 assertEquals(countAfterRemediation, 0, "File :"+searchFileName1+" should be removed from exposure as all internal user mails have been deleted");
		
						 o365Document = getExposedUsers("collab",isInternal,testUser1, violations,searchFileName1);
						 assertEquals(o365Document.getMeta().getTotalCount(),0,"Collaborator list should be zero for the file:"+searchFileName1+",  as all internal user mails have been deleted .");		
						 o365Document = getExposedUsers("users",isInternal,testUser1, violations,searchFileName1);
						 myResponse = MarshallingUtils.marshall(o365Document);
						 assertEquals(o365Document.getMeta().getTotalCount(),0,"Owner list should be zero for the file:"+searchFileName1+",  as all internal user mails have been deleted .");
		
						 if(contentPlace.equals("body")==false){
							 violations.clear();
							 violations.add(violationType2);
							 o365Document = getExposures("true",testUser1, violations,searchFileName2);
							 countAfterRemediation = o365Document.getMeta().getTotalCount();
							 Reporter.log("Verifying Exposure count after deletion of mail from Inbox of internal recipient:"+adminMailId, true);
							 assertEquals(countAfterRemediation, 0,"File :"+searchFileName2+" should be removed from exposure as all internal user mails have been deleted");
		
							 o365Document = getExposedUsers("collab",isInternal,testUser1, violations,searchFileName2);
							 assertEquals(o365Document.getMeta().getTotalCount(),0,"Collaborator list should be zero for the file:"+searchFileName2+",  as all internal user mails have been deleted .");		
							 o365Document = getExposedUsers("users",isInternal,testUser1, violations,searchFileName2);
							 myResponse = MarshallingUtils.marshall(o365Document);
							 assertEquals(o365Document.getMeta().getTotalCount(),0,"Owner list should be zero for the file:"+searchFileName2+",  as all internal user mails have been deleted .");
						 }
		
						 //check riskyDocs section
						 riskyDocs = getRiskyDocs(isInternal, testUser1, violations,searchFileName1);
						 assertEquals(riskyDocs.getMeta().getTotalCount(),0,"Exposed Mail or attachment shouldn't be present in risky docs. But found for the file:"+searchFileName1+".");
						 riskyDocs = getRiskyDocs(isInternal, testUser1, violations,searchFileName2);
						 assertEquals(riskyDocs.getMeta().getTotalCount(),0,"Exposed Mail or attachment shouldn't be present in risky docs. But found for the file:"+searchFileName2+".");
		
					 }

		/**
					 * @throws Exception
					 */
					@Test(groups={"MAIL4","P2"}, dataProvider = "dataProviderContentLocation")
					public void verifyExposureOnDeleteMailFromMailboxMultipleUsersReceivedFromExternal(String contentPlace) throws Exception {
						
						Reporter.log("****************Test Case Description****************",true);
						Reporter.log("Verify that on deleting risk email/attachment received from external user, from  the inbox of multiple users ,the mail and attachment is removed from exposure only on deleting from all internal users",true);
						Reporter.log("1)External user sends Mail with risk in mail to 2 internal users",true);
						Reporter.log("2)Verify that mail and files are exposed and they don't appear in others risks files",true);
						Reporter.log("3)Delete the mail from inbox of first internal user, verify exposed files. Verify the file is in exposed state but that internal user was removed from details. ",true);
						Reporter.log("4)Delete the mail from inbox of second internal user, Verify that the file gets removed from exposure .",true);
						Reporter.log("*****************************************************",true);
						
						
						String isInternal =   "false";
						String exposuretype = "open";
						String violationType1 ="pii";
						String violationType2 ="pci";
						String fileName1 = "PII.rtf";
						String fileName2 = "PCI_Test.txt";
						String remedialAction ="ITEM_DELETE_MAIL";
						
						EmailAddressCollection recipientsTo = new EmailAddressCollection();
						EmailAddressCollection recipientsCc = new EmailAddressCollection();
						
						//Prepare the remediation object
						microsoft.exchange.webservices.data.core.service.item.Item emailObj = null;
						String myUniqueId = UUID.randomUUID().toString();
						ArrayList<String> myAttachment = new ArrayList<String>() ;
						String newFileName1 = myUniqueId+fileName1;
						String newFileName2 = myUniqueId+fileName2;
						File sourceFile1 =null;
						File destFile1 = null;
						File sourceFile2 =null;
						File destFile2 = null;
						String myMailSubject =null;
						String mailBody = null;
						boolean success =false;
						String docType = null;
						String searchFileName1 =null;
						String searchFileName2 =null;
						int countAfterExposure1 =0;
						int countAfterExposure2 =0;
						int responseCode = 0;
						
						//Prepare the violations
						ArrayList<String> violations = new ArrayList<String>();
						
						
						//Get the exposure count
						O365Document o365Document =null;
		//				o365Document = getExposures(isInternal, suiteData.getUsername(), violations,newFileName);
		//				int beforeCount = o365Document.getMeta().getTotalCount();
						//
		//				Reporter.log("######" + violationType + " exposure count before test::"+beforeCount, true);
						
						sourceFile1 = new File(userDir +fileName1);
						destFile1 = new File(userDir +newFileName1);
						sourceFile2 = new File(userDir +fileName2);
						destFile2 = new File(userDir +newFileName2);
						
						
						if(contentPlace.equals("attachment")){
							docType = "Email_File_Attachment";
							myMailSubject = myUniqueId+"ExposureInAttachment";
							searchFileName1=newFileName1;
							searchFileName2=newFileName2;
							
							//Creating file with unique id name for upload
							copyFileUsingFileChannels(sourceFile1, destFile1);	
							copyFileUsingFileChannels(sourceFile2, destFile2);
							filesToDelete.add(destFile1);
							filesToDelete.add(destFile2);
							myAttachment.add(destFile1.toString());
							myAttachment.add(destFile2.toString());
							mailBody= "This is test mail body";
							
						}
						else if(contentPlace.equals("body")){
							docType = "Email_Message";
							myMailSubject = myUniqueId+"ViolationForExposureInMailBody"+violationType1;
							mailBody =readFile(sourceFile1.toString(), Charset.defaultCharset());
							searchFileName1=myMailSubject;
						}
						else if(contentPlace.equals("both")){
							docType = "Email_Message";
							myMailSubject = myUniqueId+"ExposureBodyAndAttachment";
							searchFileName1=newFileName1;
							searchFileName2=myMailSubject;
							
							//Creating file with unique id name for upload
							copyFileUsingFileChannels(sourceFile1, destFile1);
							filesToDelete.add(destFile1);
							myAttachment.add(destFile1.toString());
							mailBody =readFile(sourceFile2.toString(), Charset.defaultCharset());
							
						}
						
						//2 internal and 2 external users
						recipientsTo.add(testUser1);
						recipientsCc.add(testUser2);
						
						cleanupListSent.add(myMailSubject);
						synchronized(this){ success = objMailExternalUser.sendMailWithCCAndBCC(recipientsTo,recipientsCc,null, myMailSubject,mailBody ,myAttachment , false);}
						assertTrue(success, "Failed sending mail with subject:"+myMailSubject+".");
						success=false;
						
						
						
						//wait for 2 minutes for the exposure to be applied
						 Thread.sleep(CommonConstants.TWO_MINUTES_SLEEP);
						 
						 ArrayList<String> myMailMessages = new ArrayList<String>();
							ArrayList<String> logs;
							int i = 180000;
							try {
								for (; i <= ( MAX_LOG_WAIT_TIME_IN_MINUTES * 60 * 1000); i+=60000 ) {
									Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
									Reporter.log("------------------------------------------------------------------------",true );
									Reporter.log("Searching for logs after :"+TimeUnit.MILLISECONDS.toMinutes(i)+" minutes",true );
									Reporter.log("------------------------------------------------------------------------" ,true);
									logs = searchDisplayLogs(fromTime, 60, "Office 365","Object_type", "Email_Message", myUniqueId, suiteData.getUsername(), suiteData.getCSRFToken(), suiteData.getSessionID(),"");
									myMailMessages.addAll(logs);
									

									displayMessageContent(myMailMessages);

									//Reporter.log("Actual file messages:" + actualMailMessages, true);
									if (myMailMessages.size() >= 2) {break;}
									else{myMailMessages.clear();}
								}
							}
							catch(Exception e) {}
							
							assertEquals(myMailMessages.size(),2,"All Recieved/sent logs not found even after waiting for "+TimeUnit.MILLISECONDS.toMinutes(i-60000)+" minutes.");
						
						violations.add(violationType1);
						 i = 120000;
						for (; i <= (MAX_EXPOSURE_WAIT_TIME_IN_MINUTES * 60 * 1000); i+=60000 ) {
							Reporter.log("Checking exposure after :"+TimeUnit.MILLISECONDS.toMinutes(i)+" minute(s)",true );
							Reporter.log("------------------------------------------------------------------------",true );
							Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
							
							//Get the exposure count
							o365Document = getExposures(isInternal, testUser1, violations,searchFileName1);
							countAfterExposure1 = o365Document.getMeta().getTotalCount();
							Reporter.log("Exposure Count ="+countAfterExposure1, true);
							
							if (countAfterExposure1 >= 1) {break;}
						}
						//Verify that only 1 exposure record is created though there were multiple users cced in the email
						assertEquals(countAfterExposure1,1,"File not exposed even after waiting for "+TimeUnit.MILLISECONDS.toMinutes(i-60000)+" minutes.");
						
						
						//Verifying the users in the exposure
						assertTrue(o365Document.getObjects().get(0).getExposures().getInternal().toString().contains(testUser1),"User not found in exposure internal user list: "+testUser1);
						assertTrue(o365Document.getObjects().get(0).getExposures().getInternal().toString().contains(testUser2),"User not found in exposure internal user list: "+testUser2);
						
						String myResponse;
						o365Document = getExposedUsers("collab",isInternal,testUser1, violations,searchFileName1);
						myResponse = MarshallingUtils.marshall(o365Document);
						assertTrue(myResponse.contains(testUser1),"User email not found in collaborators list:"+testUser1 +".");		
						assertTrue(myResponse.contains(testUser2),"User email not found in collaborators list:"+testUser2 +".");		
						o365Document = getExposedUsers("users",isInternal,testUser1, violations,searchFileName1);
						myResponse = MarshallingUtils.marshall(o365Document);
						assertTrue(myResponse.contains(externalUser2),"User email not found in owner list:"+externalUser2 +".");
					
						//check riskyDocs section
						 O365Document riskyDocs = getRiskyDocs(isInternal, testUser1, violations,searchFileName1);
						 assertEquals(riskyDocs.getMeta().getTotalCount(),0,"Exposed Mail or attachment shouldn't be present in risky docs. But found for the file:"+searchFileName1+".");
						
						if(contentPlace.equals("body")==false){
							violations.clear();
							violations.add(violationType2);
							for (; i <= (MAX_EXPOSURE_WAIT_TIME_IN_MINUTES * 60 * 1000); i+=60000 ) {
								Reporter.log("Checking exposure after :"+TimeUnit.MILLISECONDS.toMinutes(i)+" minute(s)",true );
								Reporter.log("------------------------------------------------------------------------",true );
								Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
		
								//Get the exposure count
								o365Document = getExposures(isInternal, testUser2, violations,searchFileName2);
								countAfterExposure2 = o365Document.getMeta().getTotalCount();
								Reporter.log("Exposure Count ="+countAfterExposure2, true);
		
								if (countAfterExposure2 >= 1) {break;}
							}
		
		
							assertEquals(countAfterExposure2,1,"File not exposed even after waiting for "+TimeUnit.MILLISECONDS.toMinutes(i-60000)+" minutes.");
		
							//Verifying the users in the exposure
							assertTrue(o365Document.getObjects().get(0).getExposures().getInternal().toString().contains(testUser1),"User not found in exposure internal user list: "+testUser1);
							assertTrue(o365Document.getObjects().get(0).getExposures().getInternal().toString().contains(testUser2),"User not found in exposure internal user list: "+testUser2);
							
							o365Document = getExposedUsers("collab",isInternal,testUser1, violations,searchFileName2);
							myResponse = MarshallingUtils.marshall(o365Document);
							assertTrue(myResponse.contains(testUser1),"User email not found in collaborators list:"+testUser1 +".");		
							assertTrue(myResponse.contains(testUser2),"User email not found in collaborators list:"+testUser2 +".");		
							o365Document = getExposedUsers("users",isInternal,testUser1, violations,searchFileName2);
							myResponse = MarshallingUtils.marshall(o365Document);
							assertTrue(myResponse.contains(externalUser2),"User email not found in owner list:"+externalUser2 +".");
		
							riskyDocs = getRiskyDocs(isInternal, testUser1, violations,searchFileName2);
							assertEquals(riskyDocs.getMeta().getTotalCount(),0,"Exposed Mail or attachment shouldn't be present in risky docs. But found for the file:"+searchFileName2+".");
						}
						
						 
						
						String docId1,docId2;
						
						docId1 = getDocID(searchFileName1);
						
						if(contentPlace.equals("body")==false){
							docId2 = getDocID(searchFileName2);
						}
						
						//==================================================================================
						Reporter.log("Deleting the email from Inbox of internal recipient:"+testUser2,true);
						synchronized(this){objMailTestUser2 = new Office365MailActivities(testUser2,testUser2Pwd);}
						assertTrue(objMailTestUser2.deleteMailFromFolder(myMailSubject,"Inbox", DeleteMode.MoveToDeletedItems), "Delete mail from Inbox failed for user:"+testUser2);
						
						
						Reporter.log("Waiting for "+TimeUnit.MILLISECONDS.toMinutes(EXPOSURE_WAIT_TIME_AFTER_REMEDIATION)+" minutes to check exposure count after deletion of email",true);
						Thread.sleep(EXPOSURE_WAIT_TIME_AFTER_REMEDIATION);
						Thread.sleep(120000);
						violations.clear();
						violations.add(violationType1);				
						o365Document = getExposures(isInternal,testUser1, violations,searchFileName1);
						int countAfterRemediation = o365Document.getMeta().getTotalCount();
						Reporter.log("Verifying Exposure count after deletion of mail from Inbox of internal recipient:"+testUser2, true);
						assertEquals(countAfterRemediation, 1, "File :"+searchFileName1+" shouldn't be removed from exposure after deletion of mail from Inbox of one internal recipient:"+testUser2+ " as two more internal users has this email in mailbox:");
						
						//Verifying the users in the exposure for searchFileName1
						assertFalse(o365Document.getObjects().get(0).getExposures().getInternal().toString().contains(testUser2),"After deletion of mail from Inbox of internal user:"+testUser2+" the user is still present in the internal user list for the exposed file"+searchFileName1+".");
						assertTrue(o365Document.getObjects().get(0).getExposures().getInternal().toString().contains(testUser1),"After deletion of mail from Inbox of internal user:"+testUser2+", User:"+testUser1+" not found in the internal user list for the exposed file"+searchFileName1+".");
						assertEquals(o365Document.getObjects().get(0).getPath(),"multiple","Field path is not multiple.");
						
						Thread.sleep(120000);
						o365Document = getExposedUsers("collab",isInternal,testUser1, violations,searchFileName1);
						myResponse = MarshallingUtils.marshall(o365Document);
						assertTrue(myResponse.contains(testUser1),"User email is not found in collaborators list:"+testUser1 +".");		
						assertFalse(myResponse.contains(testUser2),"User email is still found in collaborators list:"+testUser2 +".");		
						o365Document = getExposedUsers("users",isInternal,testUser1, violations,searchFileName1);
						myResponse = MarshallingUtils.marshall(o365Document);
						assertTrue(myResponse.contains(externalUser2),"User email not found in owner list:"+externalUser2 +".");
		
						//check riskyDocs section
						riskyDocs = getRiskyDocs(isInternal, testUser2, violations,searchFileName1);
						assertEquals(riskyDocs.getMeta().getTotalCount(),0,"Exposed Mail or attachment shouldn't be present in risky docs. But found for the file:"+searchFileName1+".");
						
						if(contentPlace.equals("body")==false){
							violations.clear();
							violations.add(violationType2);
							o365Document = getExposures(isInternal,testUser1, violations,searchFileName2);
							countAfterRemediation = o365Document.getMeta().getTotalCount();
							Reporter.log("Verifying Exposure count after deletion of mail from Inbox of internal recipient:"+testUser2, true);
							assertEquals(countAfterRemediation, 1, "File :"+searchFileName2+" shouldn't be removed from exposure after deletion of mail from Inbox of one internal recipient:"+testUser2+ " as two more internal users has this email in mailbox:");
		
							//Verifying the users in the exposure for searchFileName2
							assertFalse(o365Document.getObjects().get(0).getExposures().getInternal().toString().contains(testUser2),"After deletion of mail from Inbox of internal user:"+testUser2+" the user is still present in the internal user list for the exposed file"+searchFileName2+".");
							assertTrue(o365Document.getObjects().get(0).getExposures().getInternal().toString().contains(testUser1),"After deletion of mail from Inbox of internal user:"+testUser2+", User:"+testUser1+" not found in the internal user list for the exposed file"+searchFileName2+".");
							assertEquals(o365Document.getObjects().get(0).getPath(),"multiple","Field path is not multiple.");
							
							o365Document = getExposedUsers("collab",isInternal,testUser1, violations,searchFileName2);
							myResponse = MarshallingUtils.marshall(o365Document);
							assertFalse(myResponse.contains(testUser2),"User email still found in collaborators list:"+testUser2 +".");		
							assertTrue(myResponse.contains(testUser1),"User email is not found in collaborators list:"+testUser1 +".");		
							o365Document = getExposedUsers("users",isInternal,testUser1, violations,searchFileName2);
							myResponse = MarshallingUtils.marshall(o365Document);
							assertTrue(myResponse.contains(externalUser2),"User email not found in owner list:"+externalUser2 +".");
		
							riskyDocs = getRiskyDocs(isInternal, testUser2, violations,searchFileName2);
							assertEquals(riskyDocs.getMeta().getTotalCount(),0,"Exposed Mail or attachment shouldn't be present in risky docs. But found for the file:"+searchFileName2+".");
						}
						 
						//==================================================================================
						Reporter.log("Deleting the email from Inbox of internal recipient:"+testUser1,true);
						synchronized(this){
							Thread.sleep(5000);
							assertTrue(objMailTestUser1.deleteMailFromFolder(myMailSubject,"Inbox", DeleteMode.MoveToDeletedItems), "Delete mail from Inbox failed for user:"+testUser1);
							}
						
						
						Reporter.log("Waiting for "+TimeUnit.MILLISECONDS.toMinutes(EXPOSURE_WAIT_TIME_AFTER_REMEDIATION)+" minutes to check exposure count after deletion of email",true);
						Thread.sleep(EXPOSURE_WAIT_TIME_AFTER_REMEDIATION+60000);
						
						Thread.sleep(60000);
						violations.clear();
						violations.add(violationType1);				
						o365Document = getExposures(isInternal,testUser1, violations,searchFileName1);
						countAfterRemediation = o365Document.getMeta().getTotalCount();
						Reporter.log("Verifying Exposure count after deletion of mail from Inbox of internal recipient:"+testUser1, true);
						assertEquals(countAfterRemediation, 0, "File :"+searchFileName1+" should be removed from exposure as all internal user mails have been deleted");
						
						o365Document = getExposedUsers("collab",isInternal,testUser1, violations,searchFileName1);
						assertEquals(o365Document.getMeta().getTotalCount(),0,"Collaborator list should be zero for the file:"+searchFileName1+",  as all internal user mails have been deleted .");		
						o365Document = getExposedUsers("users",isInternal,testUser1, violations,searchFileName1);
						myResponse = MarshallingUtils.marshall(o365Document);
						assertEquals(o365Document.getMeta().getTotalCount(),0,"Owner list should be zero for the file:"+searchFileName1+",  as all internal user mails have been deleted .");
						
						//check riskyDocs section
						riskyDocs = getRiskyDocs(isInternal, testUser1, violations,searchFileName1);
						assertEquals(riskyDocs.getMeta().getTotalCount(),0,"Exposed Mail or attachment shouldn't be present in risky docs. But found for the file:"+searchFileName1+".");
						
						if(contentPlace.equals("body")==false){
							violations.clear();
							violations.add(violationType2);
							o365Document = getExposures(isInternal,testUser1, violations,searchFileName2);
							countAfterRemediation = o365Document.getMeta().getTotalCount();
							Reporter.log("Verifying Exposure count after deletion of mail from Inbox of internal recipient:"+testUser1, true);
							assertEquals(countAfterRemediation, 0,"File :"+searchFileName2+" should be removed from exposure as all internal user mails have been deleted");
							
							o365Document = getExposedUsers("collab",isInternal,testUser1, violations,searchFileName2);
							assertEquals(o365Document.getMeta().getTotalCount(),0,"Collaborator list should be zero for the file:"+searchFileName2+",  as all internal user mails have been deleted .");		
							o365Document = getExposedUsers("users",isInternal,testUser1, violations,searchFileName2);
							myResponse = MarshallingUtils.marshall(o365Document);
							assertEquals(o365Document.getMeta().getTotalCount(),0,"Owner list should be zero for the file:"+searchFileName2+",  as all internal user mails have been deleted .");
							
							riskyDocs = getRiskyDocs(isInternal, testUser1, violations,searchFileName2);
							assertEquals(riskyDocs.getMeta().getTotalCount(),0,"Exposed Mail or attachment shouldn't be present in risky docs. But found for the file:"+searchFileName2+".");
						}
						
						 
						
					}

		/**
					 * @throws Exception
					 */
					@Test(groups={"MAIL4","P1"}, dataProvider = "dataProviderContentLocation")
					public void verifyRiskyDocsOnDeleteMailFromMailboxInternalUsers(String contentPlace) throws Exception {
						
						Reporter.log("****************Test Case Description****************",true);
						Reporter.log("Verify that on deleting risk email/attachment from inbox of multiple internal users ,the mail and attachment is removed from risky docs only on deleting from all internal users",true);
						Reporter.log("1)Send Mail risk in body and a risk file attachment to multiple internal users",true);
						Reporter.log("2)Verify that mail and files appearing in risky docs and they don't appear in exposed files",true);
						Reporter.log("3)Delete the mail from inbox each of internal user, verify risky docs. Verify the file present in risky docs but internal user was removed from details. ",true);
						Reporter.log("4)Delete the mail from sentItem of sender, Verify that the file gets removed from risky docs .",true);
						Reporter.log("*****************************************************",true);
						
						
						String isInternal =   "true";
						String exposuretype = "open";
						String violationType1 ="pii";
						String violationType2 ="pci";
						String fileName1 = "PII.rtf";
						String fileName2 = "PCI_Test.txt";
						String remedialAction ="ITEM_DELETE_MAIL";
						
						EmailAddressCollection recipientsTo = new EmailAddressCollection();
						EmailAddressCollection recipientsCc = new EmailAddressCollection();
						
						//Prepare the remediation object
						microsoft.exchange.webservices.data.core.service.item.Item emailObj = null;
						String myUniqueId = UUID.randomUUID().toString();
						ArrayList<String> myAttachment = new ArrayList<String>() ;
						String newFileName1 = myUniqueId+fileName1;
						String newFileName2 = myUniqueId+fileName2;
						File sourceFile1 =null;
						File destFile1 = null;
						File sourceFile2 =null;
						File destFile2 = null;
						String myMailSubject =null;
						String mailBody = null;
						boolean success =false;
						String docType = null;
						String searchFileName1 =null;
						String searchFileName2 =null;
						int countAfterExposure1 =0;
						int countAfterExposure2 =0;
						int responseCode = 0;
						
						//Prepare the violations
						ArrayList<String> violations = new ArrayList<String>();
						
						
						//Get the exposure count
						O365Document o365Document =null;
		//				o365Document = getExposures(isInternal, suiteData.getUsername(), violations,newFileName);
		//				int beforeCount = o365Document.getMeta().getTotalCount();
						//
		//				Reporter.log("######" + violationType + " exposure count before test::"+beforeCount, true);
						
						sourceFile1 = new File(userDir +fileName1);
						destFile1 = new File(userDir +newFileName1);
						sourceFile2 = new File(userDir +fileName2);
						destFile2 = new File(userDir +newFileName2);
						
						
						if(contentPlace.equals("attachment")){
							docType = "Email_File_Attachment";
							myMailSubject = myUniqueId+"ExposureInMultipleAttachment";
							searchFileName1=newFileName1;
							searchFileName2=newFileName2;
							
							//Creating file with unique id name for upload
							copyFileUsingFileChannels(sourceFile1, destFile1);	
							copyFileUsingFileChannels(sourceFile2, destFile2);
							filesToDelete.add(destFile1);
							filesToDelete.add(destFile2);
							myAttachment.add(destFile1.toString());
							myAttachment.add(destFile2.toString());
							mailBody= "This is test mail body";
							
						}
						else if(contentPlace.equals("body")){
							docType = "Email_Message";
							myMailSubject = myUniqueId+"ViolationForExposureInMailBody"+violationType1;
							mailBody =readFile(sourceFile1.toString(), Charset.defaultCharset());
							searchFileName1=myMailSubject;
						}
						else if(contentPlace.equals("both")){
							docType = "Email_Message";
							myMailSubject = myUniqueId+"ExposureBodyAndAttachment";
							searchFileName1=newFileName1;
							searchFileName2=myMailSubject;
							
							//Creating file with unique id name for upload
							copyFileUsingFileChannels(sourceFile1, destFile1);
							filesToDelete.add(destFile1);
							myAttachment.add(destFile1.toString());
							mailBody =readFile(sourceFile2.toString(), Charset.defaultCharset());
							
						}
						
						//2 internal users
						recipientsTo.add(adminMailId);
						recipientsCc.add(testUser2);
						
						cleanupListSent.add(myMailSubject);
						synchronized(this){ success = objMailTestUser1.sendMailWithCCAndBCC(recipientsTo,recipientsCc,null, myMailSubject,mailBody ,myAttachment , true);}
						assertTrue(success, "Failed sending mail with subject:"+myMailSubject+".");
						success=false;
						
						
						//wait for 1 minutes for the exposure to be applied
						Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
						
						violations.add(violationType1);
						int i = 120000;
						for (; i <= (MAX_EXPOSURE_WAIT_TIME_IN_MINUTES * 60 * 1000); i+=60000 ) {
							Reporter.log("Checking risky_docs after :"+TimeUnit.MILLISECONDS.toMinutes(i)+" minute(s)",true );
							Reporter.log("------------------------------------------------------------------------",true );
							Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
							
							//Get the risky_docs count
							o365Document = getRiskyDocs(isInternal, testUser1, violations,searchFileName1);
							countAfterExposure1 = o365Document.getMeta().getTotalCount();
							Reporter.log("Exposure Count ="+countAfterExposure1, true);
							
							if (countAfterExposure1 >= 1) {break;}
						}
						//Verify that only 1 risky_docs record is created though there were multiple users cced in the email
						assertEquals(countAfterExposure1,1,"File not present in risky docs even after waiting for "+TimeUnit.MILLISECONDS.toMinutes(i-60000)+" minutes.");
						
						//Verifying the users in the risky docs
						assertTrue(o365Document.getObjects().get(0).getExposures().getInternal().toString().contains(testUser2),"User not found in risky_docs internal user list: "+testUser1);
						assertTrue(o365Document.getObjects().get(0).getExposures().getInternal().toString().contains(adminMailId),"User not found in risky_docs in internal user list: "+adminMailId);
		//				assertEquals(o365Document.getObjects().get(0).getPath(),"multiple","Field path is not multiple.");
						//check Exposure section
						O365Document riskyDocs = getExposures(isInternal, testUser1, violations,searchFileName1);
						assertEquals(riskyDocs.getMeta().getTotalCount(),0," Risky Mail or attachment shouldn't be present in exposed docs. But found for Searched keyword:"+searchFileName1+".");
						
						
						if(contentPlace.equals("body")==false){
							violations.clear();
							violations.add(violationType2);
							for (; i <= (MAX_EXPOSURE_WAIT_TIME_IN_MINUTES * 60 * 1000); i+=60000 ) {
								Reporter.log("Checking risky_docs after :"+TimeUnit.MILLISECONDS.toMinutes(i)+" minute(s)",true );
								Reporter.log("------------------------------------------------------------------------",true );
								Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
		
								//Get the risky_docs count
								o365Document = getRiskyDocs(isInternal, testUser2, violations,searchFileName2);
								countAfterExposure2 = o365Document.getMeta().getTotalCount();
								Reporter.log("Exposure Count ="+countAfterExposure2, true);
		
								if (countAfterExposure2 >= 1) {break;}
							}
		
		
							//Verify that only 1 risky_docs record is created though there were multiple users were copied in the email
							assertEquals(countAfterExposure2,1,"File not present in risky docs even after waiting for "+TimeUnit.MILLISECONDS.toMinutes(i-60000)+" minutes.");
		
							//Verifying the users in the risky_docs
							assertTrue(o365Document.getObjects().get(0).getExposures().getInternal().toString().contains(testUser2),"User not found in risky_docs internal user list: "+testUser1);
							assertTrue(o365Document.getObjects().get(0).getExposures().getInternal().toString().contains(adminMailId),"User not found in risky_docs in internal user list: "+adminMailId);
							assertEquals(o365Document.getObjects().get(0).getPath(),"multiple","Field path is not multiple.");
							//check Exposure section
							riskyDocs = getExposures(isInternal, testUser1, violations,searchFileName2);
							assertEquals(riskyDocs.getMeta().getTotalCount(),0," Risky Mail or attachment shouldn't be present in exposed docs. But found for Searched keyword:"+searchFileName2+".");
						}
						
						
						String docId1,docId2;
						
						docId1 = getDocID(searchFileName1);
						docId2 = getDocID(searchFileName2);
						
						//==================================================================================
						Reporter.log("Deleting the email from Inbox of internal recipient:"+testUser2,true);
						synchronized(this){objMailTestUser2 = new Office365MailActivities(testUser2,testUser2Pwd);}
						assertTrue(objMailTestUser2.deleteMailFromFolder(myMailSubject,"Inbox", DeleteMode.MoveToDeletedItems), "Delete mail from Inbox failed for user:"+testUser2);
						
						
						Reporter.log("Waiting for "+TimeUnit.MILLISECONDS.toMinutes(EXPOSURE_WAIT_TIME_AFTER_REMEDIATION)+" minutes to check risky_docs count after deletion of email",true);
						Thread.sleep(EXPOSURE_WAIT_TIME_AFTER_REMEDIATION);
						
						violations.clear();
						violations.add(violationType1);				
						o365Document = getRiskyDocs("true",testUser1, violations,searchFileName1);
						int countAfterRemediation = o365Document.getMeta().getTotalCount();
						Reporter.log("Verifying risky_docs count after deletion of mail from Inbox of internal recipient:"+testUser2, true);
						assertEquals(countAfterRemediation, 1, "File :"+searchFileName1+" shouldn't be removed from risky_docs after deletion of mail from Inbox of one internal recipient:"+testUser2+ " as two more internal users has this email in mailbox:");
						
						//Verifying the users in the risky_docs for searchFileName1
						assertFalse(o365Document.getObjects().get(0).getExposures().getInternal().toString().contains(testUser2),"After deletion of mail from Inbox of internal user:"+testUser2+" the user is still present in the internal user list for the exposed file"+searchFileName1+".");
						assertTrue(o365Document.getObjects().get(0).getExposures().getInternal().toString().contains(adminMailId),"After deletion of mail from Inbox of internal user:"+testUser2+", User:"+adminMailId+" not found in the internal user list for the exposed file"+searchFileName1+".");
						assertEquals(o365Document.getObjects().get(0).getPath(),"multiple","Field path is not multiple.");
						
						//check Exposure section
						 riskyDocs = getExposures(isInternal, testUser1, violations,searchFileName1);
						assertEquals(riskyDocs.getMeta().getTotalCount(),0," Risky Mail or attachment shouldn't be present in exposed docs. But found for Searched keyword:"+searchFileName1+".");
						
						if(contentPlace.equals("body")==false){
							violations.clear();
							violations.add(violationType2);
							o365Document = getRiskyDocs("true",testUser1, violations,searchFileName2);
							countAfterRemediation = o365Document.getMeta().getTotalCount();
							Reporter.log("Verifying risky_docs count after deletion of mail from Inbox of internal recipient:"+testUser2, true);
							assertEquals(countAfterRemediation, 1, "File :"+searchFileName2+" shouldn't be removed from risky_docs after deletion of mail from Inbox of one internal recipient:"+testUser2+ " as two more internal users has this email in mailbox:");
		
							//Verifying the users in the risky_docs for searchFileName2
							assertFalse(o365Document.getObjects().get(0).getExposures().getInternal().toString().contains(testUser2),"After deletion of mail from Inbox of internal user:"+testUser2+" the user is still present in the internal user list for the exposed file"+searchFileName2+".");
							assertTrue(o365Document.getObjects().get(0).getExposures().getInternal().toString().contains(adminMailId),"After deletion of mail from Inbox of internal user:"+testUser2+", User:"+adminMailId+" not found in the internal user list for the exposed file"+searchFileName2+".");
							assertEquals(o365Document.getObjects().get(0).getPath(),"multiple","Field path is not multiple.");
							
							//check Exposure section
							 riskyDocs = getExposures(isInternal, testUser1, violations,searchFileName2);
							assertEquals(riskyDocs.getMeta().getTotalCount(),0," Risky Mail or attachment shouldn't be present in exposed docs. But found for Searched keyword:"+searchFileName2+".");
							
						}
		
						//==================================================================================
						Reporter.log("Deleting the email from SentItems of sender:"+testUser1,true);
						synchronized(this){objMailTestUser2 = new Office365MailActivities(testUser2,testUser2Pwd);}
						assertTrue(objMailTestUser1.deleteMailFromFolder(myMailSubject,"SentItems", DeleteMode.MoveToDeletedItems), "Delete mail from SentItems failed for user:"+testUser1);
						
						
						Reporter.log("Waiting for "+TimeUnit.MILLISECONDS.toMinutes(EXPOSURE_WAIT_TIME_AFTER_REMEDIATION)+" minutes to check risky_docs count after deletion of email",true);
						Thread.sleep(EXPOSURE_WAIT_TIME_AFTER_REMEDIATION);
						
						violations.clear();
						violations.add(violationType1);				
						o365Document = getRiskyDocs("true",testUser1, violations,searchFileName1);
						 countAfterRemediation = o365Document.getMeta().getTotalCount();
						Reporter.log("Verifying risky_docs count after deletion of mail from SentItems of sender:"+testUser1, true);
						assertEquals(countAfterRemediation, 1, "File :"+searchFileName1+" shouldn't be removed from risky_docs after deletion of mail from SentItems of sender "+testUser1+ " as one more internal user has this email in inbox:");
						
						//Verifying the users in the risky_docs for searchFileName1
						assertFalse(o365Document.getObjects().get(0).getExposures().getInternal().toString().contains(testUser2),"After deletion of mail from SentItems of sender:"+testUser1+" the user is still present in the internal user list for the exposed file"+searchFileName1+".");
						assertTrue(o365Document.getObjects().get(0).getExposures().getInternal().toString().contains(adminMailId),"After deletion of mail from SentItems of sender:"+testUser1+", User:"+adminMailId+" not found in the internal user list for the exposed file"+searchFileName1+".");
						assertEquals(o365Document.getObjects().get(0).getPath(),"multiple","Field path is not multiple.");
						
						//check Exposure section
						 riskyDocs = getExposures(isInternal, testUser1, violations,searchFileName1);
						assertEquals(riskyDocs.getMeta().getTotalCount(),0," Risky Mail or attachment shouldn't be present in exposed docs. But found for Searched keyword:"+searchFileName1+".");
						
		
						if(contentPlace.equals("body")==false){
							violations.clear();
							violations.add(violationType2);
							o365Document = getRiskyDocs("true",testUser1, violations,searchFileName2);
							countAfterRemediation = o365Document.getMeta().getTotalCount();
							Reporter.log("Verifying risky_docs count after deletion of mail from Inbox of internal recipient:"+testUser2, true);
							assertEquals(countAfterRemediation, 1, "File :"+searchFileName2+" shouldn't be removed from risky_docs after deletion of mail from SentItems of one internal recipient:"+testUser1+ " as one more internal user has this email in inbox:");
		
							//Verifying the users in the risky_docs for searchFileName2
							assertFalse(o365Document.getObjects().get(0).getExposures().getInternal().toString().contains(testUser2),"After deletion of mail from SentItems of sender:"+testUser1+" the user is still present in the internal user list for the exposed file"+searchFileName2+".");
							assertTrue(o365Document.getObjects().get(0).getExposures().getInternal().toString().contains(adminMailId),"After deletion of mail from SentItems of sender:"+testUser1+", User:"+adminMailId+" not found in the internal user list for the exposed file"+searchFileName2+".");
							assertEquals(o365Document.getObjects().get(0).getPath(),"multiple","Field path is not multiple.");
							
							//check Exposure section
							 riskyDocs = getExposures(isInternal, testUser1, violations,searchFileName2);
							assertEquals(riskyDocs.getMeta().getTotalCount(),0," Risky Mail or attachment shouldn't be present in exposed docs. But found for Searched keyword:"+searchFileName2+".");
							
						}
						
						//==================================================================================
						Reporter.log("Deleting the email from Inbox of internal recipient:"+adminMailId,true);
						synchronized(this){assertTrue(objMailSysAdmin.deleteMailFromFolder(myMailSubject,"Inbox", DeleteMode.MoveToDeletedItems), "Delete mail from Inbox failed for user:"+adminMailId);}
						
						
						Reporter.log("Waiting for "+TimeUnit.MILLISECONDS.toMinutes(EXPOSURE_WAIT_TIME_AFTER_REMEDIATION)+" minutes to check risky_docs count after deletion of email",true);
						Thread.sleep(EXPOSURE_WAIT_TIME_AFTER_REMEDIATION);
						
						violations.clear();
						violations.add(violationType1);				
						o365Document = getRiskyDocs("true",testUser1, violations,searchFileName1);
						countAfterRemediation = o365Document.getMeta().getTotalCount();
						Reporter.log("Verifying risky_docs count after deletion of mail from Inbox of internal recipient:"+adminMailId, true);
						assertEquals(countAfterRemediation, 0, "File :"+searchFileName1+" should be removed from risky_docs as all internal user mails have been deleted");
						
						//check Exposure section
						 riskyDocs = getExposures(isInternal, testUser1, violations,searchFileName1);
						assertEquals(riskyDocs.getMeta().getTotalCount(),0," Risky Mail or attachment shouldn't be present in exposed docs. But found for Searched keyword:"+searchFileName1+".");
						
						
						if(contentPlace.equals("body")==false){
							violations.clear();
							violations.add(violationType2);
							o365Document = getRiskyDocs("true",testUser1, violations,searchFileName2);
							countAfterRemediation = o365Document.getMeta().getTotalCount();
							Reporter.log("Verifying risky_docs count after deletion of mail from Inbox of internal recipient:"+adminMailId, true);
							assertEquals(countAfterRemediation, 0,"File :"+searchFileName2+" should be removed from risky_docs as all internal user mails have been deleted");
							
							//check Exposure section
							 riskyDocs = getExposures(isInternal, testUser1, violations,searchFileName2);
							assertEquals(riskyDocs.getMeta().getTotalCount(),0," Risky Mail or attachment shouldn't be present in exposed docs. But found for Searched keyword:"+searchFileName2+".");
							
						}
						
					}

		/**
					 * This method sends mail with different user combinations
					 * @param testCaseName
					 * @param testCaseDescription
					 * @param toEmail
					 * @param ccEmail
					 * @param bccEmail
					 * @param attachments
					 * @param subject
					 * @param expectedLogs
					 * @param expectedFields
					 * @throws Exception
					 */
					@Test(groups={"MAIL3","P1"},retryAnalyzer=RetryAnalyzer.class)
					public void verifyUserExposureInternalSwitch_BCB_34_BCB_46() throws Exception 
					{
						Reporter.log("****************Test Case Description****************",true);
						Reporter.log("Test Case Description: Verify the user exposure on sending mail to external users",true);
						Reporter.log("1)Send Mail with risk in body and a risk file attachment in TO/CC/BCC external user. Include some external users with their alias like testuser+1@,testuser+2.",true);
						Reporter.log("2)Verify that the user gets exposed, appears in collabs, then delete the mail from sentItem by user (without redemidation)",true);
						Reporter.log("3)Verify user is unexposed",true);
						Reporter.log("*****************************************************",true);
						
						
						String isInternal =   "true";
						String exposuretype = "open";
						String violationType1 ="pii";
						String violationType2 ="pci";
						String fileName1 = "PII.rtf";
						String fileName2 = "PCI_Test.txt";
						String contentPlace ="both";
						String remedialAction ="ITEM_DELETE_MAIL";
						
						//Prepare the remediation object
						microsoft.exchange.webservices.data.core.service.item.Item emailObj = null;
						String myUniqueId = UUID.randomUUID().toString();
						ArrayList<String> myAttachment = new ArrayList<String>() ;
						String newFileName1 = myUniqueId+fileName1;
						String newFileName2 = myUniqueId+fileName2;
						File sourceFile1 =null;
						File destFile1 = null;
						File sourceFile2 =null;
						File destFile2 = null;
						String myMailSubject =null;
						String mailBody = null;
						boolean success =false;
						String docType = null;
						String searchFileName1 =null;
						String searchFileName2 =null;
						int countAfterExposure1 =0;
						int countAfterExposure2 =0;
						int responseCode = 0;
						
						//Prepare the violations
						ArrayList<String> violations = new ArrayList<String>();
						
						
						//Get the exposure count
						O365Document o365Document =null;
		//				o365Document = getExposures(isInternal, suiteData.getUsername(), violations,newFileName);
		//				int beforeCount = o365Document.getMeta().getTotalCount();
						//
		//				Reporter.log("######" + violationType + " exposure count before test::"+beforeCount, true);
						
						sourceFile1 = new File(userDir +fileName1);
						destFile1 = new File(userDir +newFileName1);
						sourceFile2 = new File(userDir +fileName2);
						destFile2 = new File(userDir +newFileName2);
						
						
						if(contentPlace.equals("attachment")){
							docType = "Email_File_Attachment";
							myMailSubject = myUniqueId+"ExposureInMultipleAttachment";
							searchFileName1=newFileName1;
							searchFileName2=newFileName2;
							
							//Creating file with unique id name for upload
							copyFileUsingFileChannels(sourceFile1, destFile1);	
							copyFileUsingFileChannels(sourceFile2, destFile2);
							filesToDelete.add(destFile1);
							filesToDelete.add(destFile2);
							myAttachment.add(destFile1.toString());
							myAttachment.add(destFile2.toString());
							mailBody= "This is test mail body";
							
						}
						else if(contentPlace.equals("body")){
							docType = "Email_Message";
							myMailSubject = myUniqueId+"ViolationForExposureInMailBody"+violationType1;
							mailBody =readFile(sourceFile1.toString(), Charset.defaultCharset());
							searchFileName1=myMailSubject;
						}
						else if(contentPlace.equals("both")){
							docType = "Email_Message";
							myMailSubject = myUniqueId+"ExposureBodyAndAttachment";
							searchFileName1=newFileName1;
							searchFileName2=myMailSubject;
							
							//Creating file with unique id name for upload
							copyFileUsingFileChannels(sourceFile1, destFile1);
							filesToDelete.add(destFile1);
							myAttachment.add(destFile1.toString());
							mailBody =readFile(sourceFile2.toString(), Charset.defaultCharset());
							
						}
						
						EmailAddressCollection toEmail = new EmailAddressCollection();
						EmailAddressCollection ccEmail = new EmailAddressCollection();
						EmailAddressCollection bccEmail = new EmailAddressCollection();
						
						String externalEmailAlias1 = "farhan.jaleel+1@elasticaqa.net";
						String externalEmailAlias2 = "farhan.jaleel+2@elasticaqa.net";
						String externalEmailAlias3 = "farhan.jaleel+3@elasticaqa.net";
						toEmail.add(externalUser2);
						toEmail.add(externalEmailAlias1);
						ccEmail.add(externalUser1);
						ccEmail.add(externalEmailAlias2);
						bccEmail.add(externalEmailAlias3);
						
						cleanupListSent.add(myMailSubject);
						synchronized(this){ success = objMailTestUser1.sendMailWithCCAndBCC(toEmail, ccEmail, bccEmail, myMailSubject, mailBody, myAttachment, true);}
						assertTrue(success, "Failed sending mail with subject:"+myMailSubject+".");
						success=false;
						
						
						//wait for 1 minutes for the exposure to be applied
						Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
						
						violations.add(violationType1);
						int i = 60000;
						for (; i <= (MAX_EXPOSURE_WAIT_TIME_IN_MINUTES * 60 * 1000); i+=60000 ) {
							Reporter.log("Checking user exposure after :"+TimeUnit.MILLISECONDS.toMinutes(i)+" minute(s)",true );
							Reporter.log("------------------------------------------------------------------------",true );
							
							//Get the exposure count
							o365Document = getExposedUsers("collab", isInternal, testUser1, violations,searchFileName1);
							countAfterExposure1 = o365Document.getMeta().getTotalCount();
							Reporter.log("Exposed User Count ="+countAfterExposure1, true);
							
							if (countAfterExposure1 >= 5) {break;}
							Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
						}
						assertEquals(countAfterExposure1,5,"Users not exposed even after waiting for "+TimeUnit.MILLISECONDS.toMinutes(i)+" minutes.");
						
						String myResponse = MarshallingUtils.marshall(o365Document);				
						assertTrue(myResponse.contains(externalUser1),"User email not found in exposure"+externalUser1 +".");
						assertTrue(myResponse.contains(externalUser2),"User email not found in exposure"+externalUser1 +".");
						assertTrue(myResponse.contains(externalEmailAlias1),"User email not found in exposure"+externalEmailAlias1 +".");
						assertTrue(myResponse.contains(externalEmailAlias2),"User email not found in exposure"+externalEmailAlias2 +".");
						assertTrue(myResponse.contains(externalEmailAlias3),"User email not found in exposure"+externalEmailAlias3 +".");
						
						o365Document = getExposedUsers("users",isInternal, testUser2, violations,searchFileName1);
						assertEquals(o365Document.getObjects().get(0).getEmail(),testUser1,"Sender email not found in exposure user.");
						
						violations.clear();
						violations.add(violationType2);
						for (; i <= (MAX_EXPOSURE_WAIT_TIME_IN_MINUTES * 60 * 1000); i+=60000 ) {
							Reporter.log("Checking user exposure after :"+TimeUnit.MILLISECONDS.toMinutes(i)+" minute(s)",true );
							Reporter.log("------------------------------------------------------------------------",true );
							
							//Get the exposure count
							o365Document = getExposedUsers("collab",isInternal, testUser2, violations,searchFileName2);
							countAfterExposure2 = o365Document.getMeta().getTotalCount();
							Reporter.log("Exposed User Count ="+countAfterExposure2, true);
							
							if (countAfterExposure2 >= 5) {break;}
							Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
						}
						
						
						assertEquals(countAfterExposure2,5,"User not exposed even after waiting for "+TimeUnit.MILLISECONDS.toMinutes(i)+" minutes.");
						myResponse = MarshallingUtils.marshall(o365Document);				
						assertTrue(myResponse.contains(externalUser1),"User email not found in exposure"+externalUser1 +".");
						assertTrue(myResponse.contains(externalUser2),"User email not found in exposure"+externalUser1 +".");
						assertTrue(myResponse.contains(externalEmailAlias1),"User email not found in exposure"+externalEmailAlias1 +".");
						assertTrue(myResponse.contains(externalEmailAlias2),"User email not found in exposure"+externalEmailAlias2 +".");
						assertTrue(myResponse.contains(externalEmailAlias3),"User email not found in exposure"+externalEmailAlias3 +".");
						
						o365Document = getExposedUsers("users",isInternal, testUser2, violations,searchFileName2);
						assertEquals(o365Document.getObjects().get(0).getEmail(),testUser1,"Sender email not found in exposure user.");
						
						String docId1,docId2;
						
						docId1 = getDocID(searchFileName1);
						docId2 = getDocID(searchFileName2);
						
						Reporter.log("Deleting email from SentItems folder of sender",true);
						
						assertTrue(objMailTestUser1.deleteMailFromFolder(myMailSubject,"SentItems", DeleteMode.MoveToDeletedItems), "Delete mail from SentItems failed.");
						
						i = 60000;
						boolean remediationSuccess = false;
						for (; i <= (MAX_REMEDIATION_WAIT_TIME_IN_MINUTES * 60 * 1000); i+=60000 ) {
							Reporter.log("Checking if mail deleted after :"+TimeUnit.MILLISECONDS.toMinutes(i)+" minute(s)",true );
							Reporter.log("------------------------------------------------------------------------",true );
							Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
							
							//Searching for email in mailbox sent items
							emailObj = objMailTestUser1.findItemInFolder(myMailSubject, "SentItems");
							
							
							if(emailObj==null){
								break;
							}
							else{
								Reporter.log("Mail/Attachment not yet deleted",true);
							}
							
						}
						
						Reporter.log("Waiting for "+TimeUnit.MILLISECONDS.toMinutes(EXPOSURE_WAIT_TIME_AFTER_REMEDIATION)+" minutes to check exposure count after deletion of mail",true);
						Thread.sleep(EXPOSURE_WAIT_TIME_AFTER_REMEDIATION);
						
						violations.clear();
						violations.add(violationType1);
						
						o365Document = getExposedUsers("collab","true",testUser1, violations,searchFileName1);
						int countAfterRemediation = o365Document.getMeta().getTotalCount();
						Reporter.log("###### " + violationType1 + " exposure count after deletion of mail::"+countAfterRemediation, true);
						assertEquals(countAfterRemediation, 0, "User is still exposed after deletion of email.");
						
						violations.clear();
						violations.add(violationType2);
						o365Document = getExposedUsers("collab","true", testUser1, violations,searchFileName2);
						countAfterRemediation = o365Document.getMeta().getTotalCount();
						Reporter.log("###### " + violationType2 + " exposure count after deletion of mail::"+countAfterRemediation, true);
						assertEquals(countAfterRemediation, 0, "User is still exposed after deletion of email.");
						
						Map<String, Object> expectedFieldsForSent = new HashMap<>();
						expectedFieldsForSent.put("Activity_type", "Send");
						expectedFieldsForSent.put("external_recipients", externalUser2 + ", "+ externalUser1);
						
						String searchResponse = searchInvestigateLogs(-60, 60, "Office 365","all_messages", null, myUniqueId, suiteData.getUsername(), suiteData.getCSRFToken(), suiteData.getSessionID(),testUser1);
						softAssert.assertTrue(checkFieldInResponse(searchResponse, "$.hits.hits[*].source", expectedFieldsForSent), "Expected field combination not found in Sent activity log.");
						
					}

		/**
					 * @throws Exception
					 */
					@Test(groups={"MAIL4","P2"},retryAnalyzer=RetryAnalyzer.class)
					public void verifyUserExposureExternalSwitch_BCB_35() throws Exception {
						Reporter.log("****************Test Case Description****************",true);
						Reporter.log("Test Case Description: Verify the user exposure on receiving mail from external users",true);
						Reporter.log("1)Receive a Mail with risk in body and a risk file attachment in TO/CC/BCC from external user",true);
						Reporter.log("2)Verify that the user gets exposed and appears in collabs, then delete the mail from inbox by user (without redemidation)",true);
						Reporter.log("3)Verify user is unexposed",true);
						Reporter.log("*****************************************************",true);
						
						String isInternal =   "false";
						String exposuretype = "open";
						String violationType1 ="pii";
						String violationType2 ="pci";
						String fileName1 = "PII.rtf";
						String fileName2 = "PCI_Test.txt";
						String contentPlace ="both";
						String remedialAction ="ITEM_DELETE_MAIL";
		
						//Prepare the remediation object
						microsoft.exchange.webservices.data.core.service.item.Item emailObj = null;
						String myUniqueId = UUID.randomUUID().toString();
						ArrayList<String> myAttachment = new ArrayList<String>() ;
						String newFileName1 = myUniqueId+fileName1;
						String newFileName2 = myUniqueId+fileName2;
						File sourceFile1 =null;
						File destFile1 = null;
						File sourceFile2 =null;
						File destFile2 = null;
						String myMailSubject =null;
						String mailBody = null;
						boolean success =false;
						String docType = null;
						String searchFileName1 =null;
						String searchFileName2 =null;
						int countAfterExposure1 =0;
						int countAfterExposure2 =0;
						int responseCode = 0;
		
						//Prepare the violations
						ArrayList<String> violations = new ArrayList<String>();
					
		
						//Get the exposure count
						O365Document o365Document =null;
		
						sourceFile1 = new File(userDir +fileName1);
						destFile1 = new File(userDir +newFileName1);
						sourceFile2 = new File(userDir +fileName2);
						destFile2 = new File(userDir +newFileName2);
		
		
						if(contentPlace.equals("attachment")){
							docType = "Email_File_Attachment";
							myMailSubject = myUniqueId+"ExposureInMultipleAttachment";
							searchFileName1=newFileName1;
							searchFileName2=newFileName2;
		
							//Creating file with unique id name for upload
							copyFileUsingFileChannels(sourceFile1, destFile1);	
							copyFileUsingFileChannels(sourceFile2, destFile2);
							filesToDelete.add(destFile1);
							filesToDelete.add(destFile2);
							myAttachment.add(destFile1.toString());
							myAttachment.add(destFile2.toString());
							mailBody= "This is test mail body";
		
						}
						else if(contentPlace.equals("body")){
							docType = "Email_Message";
							myMailSubject = myUniqueId+"ViolationForExposureInMailBody"+violationType1;
							mailBody =readFile(sourceFile1.toString(), Charset.defaultCharset());
							searchFileName1=myMailSubject;
						}
						else if(contentPlace.equals("both")){
							docType = "Email_Message";
							myMailSubject = myUniqueId+"ExposureBodyAndAttachment";
							searchFileName1=newFileName1;
							searchFileName2=myMailSubject;
		
							//Creating file with unique id name for upload
							copyFileUsingFileChannels(sourceFile1, destFile1);
							filesToDelete.add(destFile1);
							myAttachment.add(destFile1.toString());
							mailBody =readFile(sourceFile2.toString(), Charset.defaultCharset());
		
						}
						
						EmailAddressCollection toEmail = new EmailAddressCollection();
						EmailAddressCollection ccEmail = new EmailAddressCollection();
						EmailAddressCollection bccEmail = new EmailAddressCollection();
						
						toEmail.add(testUser1);
						ccEmail.add(externalUser1);
						ccEmail.add(testUser2);
						bccEmail.add(adminMailId);
						
						cleanupListSent.add(myMailSubject);
						synchronized(this){ success = objMailExternalUser.sendMailWithCCAndBCC(toEmail, ccEmail, bccEmail, myMailSubject, mailBody, myAttachment, false);}
						
						assertTrue(success, "Failed sending mail with subject:"+mailSubject2+".");
						success=false;
		
		
						//wait for 1 minutes for the exposure to be applied
						Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
						
						violations.add(violationType1);
						int i = 60000;
						for (; i <= (MAX_EXPOSURE_WAIT_TIME_IN_MINUTES * 60 * 1000); i+=60000 ) {
							Reporter.log("Checking user exposure after :"+TimeUnit.MILLISECONDS.toMinutes(i)+" minute(s)",true );
							Reporter.log("------------------------------------------------------------------------",true );
							
							//Get the exposure count
							o365Document = getExposedUsers("users", isInternal, testUser1, violations,searchFileName1);
							countAfterExposure1 = o365Document.getMeta().getTotalCount();
							Reporter.log("Exposed User Count ="+countAfterExposure1, true);
							
							if (countAfterExposure1 >= 1) {break;}
							Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
						}
						assertEquals(countAfterExposure1,1,"Users not exposed even after waiting for "+TimeUnit.MILLISECONDS.toMinutes(i-60000)+" minutes.");
						assertEquals(o365Document.getObjects().get(0).getEmail(),externalUser2,"External user id is not found in userlist:"+externalUser2);
						
						Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
						
						String myResponse = MarshallingUtils.marshall(o365Document);				
						assertTrue(myResponse.contains(externalUser2),"User email not found in exposure"+externalUser2 +".");
						
						o365Document = getExposedUsers("collab","false",testUser1, violations,searchFileName1);
						myResponse = MarshallingUtils.marshall(o365Document);				
								
						assertTrue(myResponse.contains(testUser1),"User email not found in collaborators list:"+testUser1 +".");
						assertTrue(myResponse.contains(testUser2),"User email not found in collaborators list:"+testUser2 +".");
						assertTrue(myResponse.contains(adminMailId),"User email not found in collaborators list:"+adminMailId +".");
						assertFalse(myResponse.contains(externalUser1),"External User email is found in collaborators list:"+externalUser1 +".");
						
						
						violations.clear();
						violations.add(violationType2);
						for (; i <= (MAX_EXPOSURE_WAIT_TIME_IN_MINUTES * 60 * 1000); i+=60000 ) {
							Reporter.log("Checking user exposure after :"+TimeUnit.MILLISECONDS.toMinutes(i)+" minute(s)",true );
							Reporter.log("------------------------------------------------------------------------",true );
							
							//Get the exposure count
							o365Document = getExposedUsers("users",isInternal, testUser2, violations,searchFileName2);
							countAfterExposure2 = o365Document.getMeta().getTotalCount();
							Reporter.log("Exposed User Count ="+countAfterExposure2, true);
							
							if (countAfterExposure2 >= 1) {break;}
							Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
						}
						
						
						assertEquals(countAfterExposure2,1,"User not exposed even after waiting for "+TimeUnit.MILLISECONDS.toMinutes(i-60000)+" minutes.");
						myResponse = MarshallingUtils.marshall(o365Document);				
						assertTrue(myResponse.contains(externalUser2),"User email not found in exposure"+externalUser1 +".");
						
						o365Document = getExposedUsers("collab","false",testUser1, violations,searchFileName2);
						myResponse = MarshallingUtils.marshall(o365Document);				
						
						assertTrue(myResponse.contains(testUser1),"User email not found in collaborators list:"+testUser1 +".");
						assertTrue(myResponse.contains(testUser2),"User email not found in collaborators list:"+testUser2 +".");
						assertTrue(myResponse.contains(adminMailId),"User email not found in collaborators list:"+adminMailId +".");
						assertFalse(myResponse.contains(externalUser1),"External User email is found in collaborators list:"+externalUser1 +".");
						
						
						
						String docId1,docId2;
						
						docId1 = getDocID(searchFileName1);
						docId2 = getDocID(searchFileName2);
						
						Reporter.log("Deleting email from Inbox folder of receiver",true);
						
						assertTrue(objMailTestUser1.deleteMailFromFolder(myMailSubject,"Inbox", DeleteMode.MoveToDeletedItems), "Delete mail from inbox failed.");
						
						i = 60000;
						boolean remediationSuccess = false;
						for (; i <= (MAX_REMEDIATION_WAIT_TIME_IN_MINUTES * 60 * 1000); i+=60000 ) {
							Reporter.log("Checking if mail deleted after :"+TimeUnit.MILLISECONDS.toMinutes(i)+" minute(s)",true );
							Reporter.log("------------------------------------------------------------------------",true );
							Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
							
							//Searching for email in mailbox sent items
							emailObj = objMailTestUser1.findItemInFolder(myMailSubject, "Inbox");
							
							
							if(emailObj==null){
								break;
							}
							else{
								Reporter.log("Mail/Attachment not yet deleted",true);
							}
							
						}
						assertTrue(objMailTestUser2.deleteMailFromFolder(myMailSubject,"Inbox", DeleteMode.MoveToDeletedItems), "Delete mail from inbox failed.");
						
						i = 60000;
						remediationSuccess = false;
						for (; i <= (MAX_REMEDIATION_WAIT_TIME_IN_MINUTES * 60 * 1000); i+=60000 ) {
							Reporter.log("Checking if mail deleted after :"+TimeUnit.MILLISECONDS.toMinutes(i)+" minute(s)",true );
							Reporter.log("------------------------------------------------------------------------",true );
							Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
							
							//Searching for email in mailbox sent items
							emailObj = objMailTestUser2.findItemInFolder(myMailSubject, "Inbox");
							
							
							if(emailObj==null){
								break;
							}
							else{
								Reporter.log("Mail/Attachment not yet deleted",true);
							}
							
						}
						
						assertTrue(objMailSysAdmin.deleteMailFromFolder(myMailSubject,"Inbox", DeleteMode.MoveToDeletedItems), "Delete mail from inbox failed.");
						i = 60000;
						 remediationSuccess = false;
						 
						for (; i <= (MAX_REMEDIATION_WAIT_TIME_IN_MINUTES * 60 * 1000); i+=60000 ) {
							Reporter.log("Checking if mail deleted after :"+TimeUnit.MILLISECONDS.toMinutes(i)+" minute(s)",true );
							Reporter.log("------------------------------------------------------------------------",true );
							Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
							
							//Searching for email in mailbox sent items
							emailObj = objMailSysAdmin.findItemInFolder(myMailSubject, "Inbox");
							
							
							if(emailObj==null){
								break;
							}
							else{
								Reporter.log("Mail/Attachment not yet deleted",true);
							}
							
						}
						
						Reporter.log("Waiting for "+TimeUnit.MILLISECONDS.toMinutes(EXPOSURE_WAIT_TIME_AFTER_REMEDIATION)+" minutes to check exposure count after deletion of mail", true);
						Thread.sleep(EXPOSURE_WAIT_TIME_AFTER_REMEDIATION);
						
						violations.clear();
						violations.add(violationType1);
						
						o365Document = getExposedUsers("users","false",testUser1, violations,searchFileName1);
						int countAfterRemediation = o365Document.getMeta().getTotalCount();
						Reporter.log("###### " + violationType1 + " exposure count after deletion of mail::"+countAfterRemediation, true);
						assertEquals(countAfterRemediation, 0, "User is still exposed after deletion of email.");
						
						o365Document = getExposedUsers("collab","false",testUser1, violations,searchFileName1);
						assertEquals(o365Document.getMeta().getTotalCount(),0,"Collaborators list is not empty");
						
						
						violations.clear();
						violations.add(violationType2);
						o365Document = getExposedUsers("users","false", testUser1, violations,searchFileName2);
						countAfterRemediation = o365Document.getMeta().getTotalCount();
						Reporter.log("###### " + violationType2 + " exposure count after deletion of mail::"+countAfterRemediation, true);
						assertEquals(countAfterRemediation, 0, "User is still exposed after deletion of email.");
						
						
						o365Document = getExposedUsers("collab","false",testUser1, violations,searchFileName2);
						assertEquals(o365Document.getMeta().getTotalCount(),0,"Collaborators list is not empty");
						
						
					
					}

		/**
				 * This method sends mail with different user combinations
				 * @param testCaseName
				 * @param testCaseDescription
				 * @param toEmail
				 * @param ccEmail
				 * @param bccEmail
				 * @param attachments
				 * @param subject
				 * @param expectedLogs
				 * @param expectedFields
				 * @throws Exception
				 */
				@Test(groups={"MAIL3","P2"},retryAnalyzer=RetryAnalyzer.class)
				public void verifyUserExposureValidInvalidInternalSwitch_BCB_47() throws Exception 
				{
					Reporter.log("****************Test Case Description****************",true);
					Reporter.log("Send an email to a combo of valid and Invalid Email addresses.Verify the user exposure.",true);
					Reporter.log("1)Send Mail with risk in body and a risk file attachment in valid and invalid external user.",true);
					Reporter.log("2)Verify that the user gets exposed, then delete the mail from sentItem by user (without redemidation)",true);
					Reporter.log("3)Verify user is unexposed",true);
					Reporter.log("*****************************************************",true);
					
					
					String isInternal =   "true";
					String exposuretype = "open";
					String violationType1 ="pii";
					String violationType2 ="pci";
					String fileName1 = "PII.rtf";
					String fileName2 = "PCI_Test.txt";
					String contentPlace ="both";
					String remedialAction ="ITEM_DELETE_MAIL";
					
					//Prepare the remediation object
					microsoft.exchange.webservices.data.core.service.item.Item emailObj = null;
					String myUniqueId = UUID.randomUUID().toString();
					ArrayList<String> myAttachment = new ArrayList<String>() ;
					String newFileName1 = myUniqueId+fileName1;
					String newFileName2 = myUniqueId+fileName2;
					File sourceFile1 =null;
					File destFile1 = null;
					File sourceFile2 =null;
					File destFile2 = null;
					String myMailSubject =null;
					String mailBody = null;
					boolean success =false;
					String docType = null;
					String searchFileName1 =null;
					String searchFileName2 =null;
					int countAfterExposure1 =0;
					int countAfterExposure2 =0;
					int responseCode = 0;
					
					//Prepare the violations
					ArrayList<String> violations = new ArrayList<String>();
					
					
					//Get the exposure count
					O365Document o365Document =null;
		//			o365Document = getExposures(isInternal, suiteData.getUsername(), violations,newFileName);
		//			int beforeCount = o365Document.getMeta().getTotalCount();
					//
		//			Reporter.log("######" + violationType + " exposure count before test::"+beforeCount, true);
					
					sourceFile1 = new File(userDir +fileName1);
					destFile1 = new File(userDir +newFileName1);
					sourceFile2 = new File(userDir +fileName2);
					destFile2 = new File(userDir +newFileName2);
					
					
					if(contentPlace.equals("attachment")){
						docType = "Email_File_Attachment";
						myMailSubject = myUniqueId+"ExposureInMultipleAttachment";
						searchFileName1=newFileName1;
						searchFileName2=newFileName2;
						
						//Creating file with unique id name for upload
						copyFileUsingFileChannels(sourceFile1, destFile1);	
						copyFileUsingFileChannels(sourceFile2, destFile2);
						filesToDelete.add(destFile1);
						filesToDelete.add(destFile2);
						myAttachment.add(destFile1.toString());
						myAttachment.add(destFile2.toString());
						mailBody= "This is test mail body";
						
					}
					else if(contentPlace.equals("body")){
						docType = "Email_Message";
						myMailSubject = myUniqueId+"ViolationForExposureInMailBody"+violationType1;
						mailBody =readFile(sourceFile1.toString(), Charset.defaultCharset());
						searchFileName1=myMailSubject;
					}
					else if(contentPlace.equals("both")){
						docType = "Email_Message";
						myMailSubject = myUniqueId+"ValidInvalidUserExposureBodyAndAttachment";
						searchFileName1=newFileName1;
						searchFileName2=myMailSubject;
						
						//Creating file with unique id name for upload
						copyFileUsingFileChannels(sourceFile1, destFile1);
						filesToDelete.add(destFile1);
						myAttachment.add(destFile1.toString());
						mailBody =readFile(sourceFile2.toString(), Charset.defaultCharset());
						
					}
					
					EmailAddressCollection toEmail = new EmailAddressCollection();
					EmailAddressCollection ccEmail = new EmailAddressCollection();
					EmailAddressCollection bccEmail = new EmailAddressCollection();
					
					String externalEmailInvalid = "invaliduser@elasticaqa.net";
					String internalEmailInvalid = "invaliduser@securleto365beatle.com";
					toEmail.add(externalUser2);
					toEmail.add(externalEmailInvalid);
		//			toEmail.add(testUser2);
					ccEmail.add(externalUser1);
					ccEmail.add(internalEmailInvalid);
		//			ccEmail.add(adminMailId);
					
					cleanupListSent.add(myMailSubject);
					synchronized(this){ success = objMailTestUser1.sendMailWithCCAndBCC(toEmail, ccEmail, bccEmail, myMailSubject, mailBody, myAttachment, true);}
					assertTrue(success, "Failed sending mail with subject:"+myMailSubject+".");
					success=false;
					
					
					//wait for 1 minutes for the exposure to be applied
					Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
					
					violations.add(violationType1);
					int i = 60000;
					for (; i <= (MAX_EXPOSURE_WAIT_TIME_IN_MINUTES * 60 * 1000); i+=60000 ) {
						Reporter.log("Checking user exposure after :"+TimeUnit.MILLISECONDS.toMinutes(i)+" minute(s)",true );
						Reporter.log("------------------------------------------------------------------------",true );
						
						//Get the exposure count
						o365Document = getExposedUsers("collab", isInternal, testUser1, violations,searchFileName1);
						countAfterExposure1 = o365Document.getMeta().getTotalCount();
						Reporter.log("Exposed User Count ="+countAfterExposure1, true);
						
						if (countAfterExposure1 >= 1) {break;}
						Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
					}
					assertEquals(countAfterExposure1,3,"Users not exposed even after waiting for "+TimeUnit.MILLISECONDS.toMinutes(i-60000)+" minutes.");
					
					String myResponse = MarshallingUtils.marshall(o365Document);				
					assertTrue(myResponse.contains(externalUser1),"User email not found in exposure: "+externalUser1 +".");
					assertTrue(myResponse.contains(externalUser2),"User email not found in exposure: "+externalUser1 +".");
					assertTrue(myResponse.contains(externalEmailInvalid),"User email not found in exposure: "+externalEmailInvalid +".");
					violations.clear();
					violations.add(violationType2);
					for (; i <= (MAX_EXPOSURE_WAIT_TIME_IN_MINUTES * 60 * 1000); i+=60000 ) {
						Reporter.log("Checking user exposure after :"+TimeUnit.MILLISECONDS.toMinutes(i)+" minute(s)",true );
						Reporter.log("------------------------------------------------------------------------",true );
						
						//Get the exposure count
						o365Document = getExposedUsers("collab",isInternal, testUser2, violations,searchFileName2);
						countAfterExposure2 = o365Document.getMeta().getTotalCount();
						Reporter.log("Exposed User Count ="+countAfterExposure2, true);
						
						if (countAfterExposure2 >= 1) {break;}
						Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
					}
					
					
					assertEquals(countAfterExposure2,3,"User not exposed even after waiting for "+TimeUnit.MILLISECONDS.toMinutes(i-60000)+" minutes.");
					myResponse = MarshallingUtils.marshall(o365Document);				
					assertTrue(myResponse.contains(externalUser1),"User email not found in exposure"+externalUser1 +".");
					assertTrue(myResponse.contains(externalUser2),"User email not found in exposure"+externalUser1 +".");
					assertTrue(myResponse.contains(externalEmailInvalid),"User email not found in exposure"+externalEmailInvalid +".");
					
					
					String docId1,docId2;
					
					docId1 = getDocID(searchFileName1);
					docId2 = getDocID(searchFileName2);
					
					Reporter.log("Deleting email from SentItems folder of sender",true);
					
					assertTrue(objMailTestUser1.deleteMailFromFolder(myMailSubject,"SentItems", DeleteMode.MoveToDeletedItems), "Delete mail from SentItems failed.");
					
					i = 60000;
					boolean remediationSuccess = false;
					for (; i <= (MAX_REMEDIATION_WAIT_TIME_IN_MINUTES * 60 * 1000); i+=60000 ) {
						Reporter.log("Checking if mail deleted after :"+TimeUnit.MILLISECONDS.toMinutes(i)+" minute(s)",true );
						Reporter.log("------------------------------------------------------------------------",true );
						Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
						
						//Searching for email in mailbox sent items
						emailObj = objMailTestUser1.findItemInFolder(myMailSubject, "SentItems");
						
						
						if(emailObj==null){
							break;
						}
						else{
							Reporter.log("Mail/Attachment not yet deleted",true);
						}
						
					}
					
					Reporter.log("Waiting for "+TimeUnit.MILLISECONDS.toMinutes(EXPOSURE_WAIT_TIME_AFTER_REMEDIATION)+" minutes to check exposure count after deletion of mail",true);
					Thread.sleep(EXPOSURE_WAIT_TIME_AFTER_REMEDIATION);
					
					violations.clear();
					violations.add(violationType1);
					
					o365Document = getExposedUsers("collab","true",testUser1, violations,searchFileName1);
					int countAfterRemediation = o365Document.getMeta().getTotalCount();
					Reporter.log("###### " + violationType1 + " exposure count after deletion of mail::"+countAfterRemediation, true);
					assertEquals(countAfterRemediation, 0, "User is still exposed after deletion of email.");
					
					violations.clear();
					violations.add(violationType2);
					o365Document = getExposedUsers("collab","true", testUser1, violations,searchFileName2);
					countAfterRemediation = o365Document.getMeta().getTotalCount();
					Reporter.log("###### " + violationType2 + " exposure count after deletion of mail::"+countAfterRemediation, true);
					assertEquals(countAfterRemediation, 0, "User is still exposed after deletion of email.");
					
					Map<String, Object> expectedFieldsForSent = new HashMap<>();
					expectedFieldsForSent.put("Activity_type", "Send");
					expectedFieldsForSent.put("external_recipients", externalUser2 + ", "+ externalUser1);
					
					String searchResponse = searchInvestigateLogs(-60, 60, "Office 365","all_messages", null, myUniqueId, suiteData.getUsername(), suiteData.getCSRFToken(), suiteData.getSessionID(),testUser1);
					softAssert.assertTrue(checkFieldInResponse(searchResponse, "$.hits.hits[*].source", expectedFieldsForSent), "Expected field combination not found in Sent activity log.");
					
				}

		@DataProvider(parallel=true)
			public Object[][] dataProviderMailCombinations() {
			 
			 	EmailAddressCollection internalUser = new EmailAddressCollection();
				EmailAddressCollection externalUser = new EmailAddressCollection();
				EmailAddressCollection externalAndInternal = new EmailAddressCollection();
				ArrayList<String> attachments = new ArrayList<String>();
				ArrayList<String> expectedLogs1 = new ArrayList<String>();
				ArrayList<String> expectedLogs2 = new ArrayList<String>();
				ArrayList<String> expectedLogs3 = new ArrayList<String>();
				ArrayList<String> expectedLogs4 = new ArrayList<String>();
				ArrayList<String> expectedLogs5 = new ArrayList<String>();
				ArrayList<String> expectedLogs6 = new ArrayList<String>();
				ArrayList<String> expectedLogs7  = new ArrayList<String>();
				ArrayList<String> expectedLogs8  = new ArrayList<String>();
				Map<Integer, String> testcaseNames =  new HashMap();
				Map<Integer, String> testcaseDescription =  new HashMap();
				Map<String, String> expectedFieldsForSent1 =  new HashMap();
				Map<String, String> expectedFieldsForSent2 =  new HashMap();
				Map<String, String> expectedFieldsForSent3 =  new HashMap();
				Map<String, String> expectedFieldsForSent4 =  new HashMap();
				Map<String, String> expectedFieldsForSent5 =  new HashMap();
				Map<String, String> expectedFieldsForSent6 =  new HashMap();
				Map<String, String> expectedFieldsForSent7 =  new HashMap();
				Map<String, String> expectedFieldsForSent8 =  new HashMap();
				
				Map<String, String> expectedFieldsForReceived1 =  new HashMap();
				Map<String, String> expectedFieldsForReceived2 =  new HashMap();
				Map<String, String> expectedFieldsForReceived3 =  new HashMap();
				Map<String, String> expectedFieldsForReceived4 =  new HashMap();
				Map<String, String> expectedFieldsForReceived5 =  new HashMap();
				Map<String, String> expectedFieldsForReceived6 =  new HashMap();
				Map<String, String> expectedFieldsForReceived7 =  new HashMap();
				Map<String, String> expectedFieldsForReceived8 =  new HashMap();
				
				String messageSent = null;
				String messageReceived = "User received an email  from "+testUser1+" with subject \"<mailsubject>\"";
//				attachments.add("textFile.txt");
				internalUser.add(testUser2);
				internalUser.add(adminMailId);
				internalUser.add(testUser1);
				
				externalUser.add("farhan.jaleel+1@elasticaqa.net");
				externalUser.add("farhan.jaleel+2@elasticaqa.net");
				externalUser.add("farhan.jaleel+3@elasticaqa.net");
				
				externalAndInternal.add(adminMailId);
//				bccEmail.add("abdul.nissar@elastica.co");
				
				
				//AllRecepientInternal
				testcaseNames.put(1,"AllRecepientInternal");	
				testcaseDescription.put(1,"Send a mail with internal users in all To, CC and BCC. Verify that mail messages appears properly and user ids appear in message details. "  );
				messageSent = "User sent an email  to 3 users  with subject \""+"<mailsubject>\"";
				expectedLogs1.add(messageSent);
				expectedFieldsForSent1.put("message", messageSent);
				//expectedLogs1.add("User received an email  from "+testUser1+" with subject \""+"<mailsubject>\"");
				expectedFieldsForSent1.put("internal_recipients", internalUser.getPropertyAtIndex(0)+", "+internalUser.getPropertyAtIndex(1));
				expectedFieldsForSent1.put("external_recipients","");
				expectedFieldsForSent1.put("In_Folder","multiple");
				expectedFieldsForReceived1.put("internal_recipients", internalUser.getPropertyAtIndex(0)+", "+internalUser.getPropertyAtIndex(1));
				expectedFieldsForReceived1.put("In_Folder","multiple");
				expectedFieldsForReceived1.put("external_recipients","");
				expectedFieldsForReceived1.put("message",messageReceived);
			
				
				//AllRecepientExternal - activity log available only for sent mails, not for received
				testcaseNames.put(2,"AllRecepientExternal");	
				testcaseDescription.put(2,"Send a mail with External users in all To, CC and BCC. Verify that mail messages appears properly and user ids appear in message details. " );
				messageSent="User sent an email  to 3 users  with subject \""+"<mailsubject>\"";
				expectedLogs2.add(messageSent);
				expectedFieldsForSent2.put("message", messageSent);
				expectedFieldsForSent2.put("external_recipients", externalUser.getPropertyAtIndex(1)+", "+externalUser.getPropertyAtIndex(0));
				expectedFieldsForSent2.put("In_Folder","Sent Items");
				expectedFieldsForSent2.put("internal_recipients","");
				
				
				//ToExternalCCInternalBCCInternal
				testcaseNames.put(3,"ToExternalCCInternalBCCInternal");	
				testcaseDescription.put(3,"Send a mail with To: ExternalUser CC: InternalUser BCC: InternalUser. Verify that mail messages appears properly and user ids appear in message details. ");
				messageSent="User sent an email  to 3 users  with subject \""+"<mailsubject>\"";
				expectedLogs3.add(messageSent);
				expectedFieldsForSent3.put("message", messageSent);
				expectedFieldsForSent3.put("internal_recipients", internalUser.getPropertyAtIndex(0).toString());
				expectedFieldsForSent3.put("external_recipients", externalUser.getPropertyAtIndex(0).toString());
				expectedFieldsForSent3.put("In_Folder","multiple");
				expectedFieldsForReceived3.put("internal_recipients", internalUser.getPropertyAtIndex(0).toString());
				expectedFieldsForReceived3.put("external_recipients", externalUser.getPropertyAtIndex(0).toString());
				expectedFieldsForReceived3.put("In_Folder","Inbox");
				expectedFieldsForReceived3.put("message",messageReceived);
				
				//ToInternalCCExternalBCCInternal
				testcaseNames.put(4,"ToInternalCCExternalBCCInternal");	
				testcaseDescription.put(4,"Send a mail with To: InternalUser CC: ExternalUser BCC: InternalUser. Verify that mail messages appears properly and user ids appear in message details. ");
				messageSent="User sent an email  to 3 users  with subject \""+"<mailsubject>\"";
				expectedLogs4.add(messageSent);
				expectedFieldsForSent4.put("message", messageSent);
				expectedFieldsForSent4.put("internal_recipients", internalUser.getPropertyAtIndex(0).toString());
				expectedFieldsForSent4.put("external_recipients", externalUser.getPropertyAtIndex(0).toString());
				expectedFieldsForSent4.put("In_Folder","multiple");
				expectedFieldsForReceived4.put("internal_recipients", internalUser.getPropertyAtIndex(0).toString());
				expectedFieldsForReceived4.put("external_recipients", externalUser.getPropertyAtIndex(0).toString());
				expectedFieldsForReceived4.put("In_Folder","Inbox");
				expectedFieldsForReceived4.put("message",messageReceived);
				
				//ToInternalCCInternalBCCExternal
				testcaseNames.put(5,"ToInternalCCInternalBCCExternal");	
				testcaseDescription.put(5,"Send a mail with To: InternalUser CC: InternalUser BCC: ExternalUser. Verify that mail messages appears properly and user ids appear in message details. " );
				messageSent="User sent an email  to 3 users  with subject \""+"<mailsubject>\"";
				expectedLogs5.add(messageSent);
				expectedFieldsForSent5.put("message", messageSent);
				expectedFieldsForSent5.put("internal_recipients", internalUser.getPropertyAtIndex(0)+", "+internalUser.getPropertyAtIndex(1));
				expectedFieldsForSent5.put("In_Folder","multiple");
				expectedFieldsForSent5.put("external_recipients", externalUser.getPropertyAtIndex(0).toString());
				expectedFieldsForReceived5.put("internal_recipients", internalUser.getPropertyAtIndex(0)+", "+internalUser.getPropertyAtIndex(1));
				//expectedFieldsForSent5.put("external_recipients", externalUser.getPropertyAtIndex(0).toString());
				expectedFieldsForReceived5.put("In_Folder","multiple");
				expectedFieldsForReceived5.put("message",messageReceived);
				
				//ToExternalCCExternalBCCInternal
				testcaseNames.put(6,"ToExternalCCExternalBCCInternal");	
				testcaseDescription.put(6,"Send a mail with To: ExternalUser CC: ExternalUser BCC: Internal. Verify that mail messages appears properly and user ids appear in message details. " );
				messageSent="User sent an email  to 3 users  with subject \""+"<mailsubject>\"";
				expectedLogs6.add(messageSent);
				expectedFieldsForSent6.put("message", messageSent);
				expectedFieldsForSent6.put("external_recipients", externalUser.getPropertyAtIndex(1)+", "+externalUser.getPropertyAtIndex(0));
				expectedFieldsForSent6.put("In_Folder","Sent Items");
				expectedFieldsForSent6.put("internal_recipients", internalUser.getPropertyAtIndex(0).toString());
//				expectedFieldsForReceived6.put("external_recipients", externalUser.getPropertyAtIndex(1)+", "+externalUser.getPropertyAtIndex(0));
//				expectedFieldsForReceived6.put("internal_recipients", internalUser.getPropertyAtIndex(0).toString());
				
				//ToExternalCCInternalBCCExternal
				testcaseNames.put(7,"ToExternalCCInternalBCCExternal");	
				testcaseDescription.put(7,"Send a mail with To: ExternalUser CC: Internal BCC: ExternalUser. Verify that mail messages appears properly and user ids appear in message details. ");
				messageSent="User sent an email  to 3 users  with subject \""+"<mailsubject>\"";
				expectedLogs7.add(messageSent);
				expectedFieldsForSent7.put("message", messageSent);
				expectedFieldsForSent7.put("external_recipients", externalUser.getPropertyAtIndex(0).toString());//+", "+externalUser.getPropertyAtIndex(1));
				expectedFieldsForSent7.put("internal_recipients", internalUser.getPropertyAtIndex(0).toString());
				expectedFieldsForSent7.put("In_Folder","multiple");
				expectedFieldsForReceived7.put("external_recipients", externalUser.getPropertyAtIndex(0).toString());//+", "+externalUser.getPropertyAtIndex(1));
				expectedFieldsForReceived7.put("internal_recipients", internalUser.getPropertyAtIndex(0).toString());
				expectedFieldsForReceived7.put("In_Folder","Inbox");
				expectedFieldsForReceived7.put("message",messageReceived);
				
				//ToInternalCCExternalBCCExternal
				testcaseNames.put(8,"ToInternalCCExternalBCCExternal");	
				testcaseDescription.put(8,"Send a mail with To: Internal CC:ExternalUser  BCC: ExternalUser. Verify that mail messages appears properly and user ids appear in message details. ");
				messageSent="User sent an email  to 3 users  with subject \""+"<mailsubject>\"";
				expectedLogs8.add(messageSent);
				expectedFieldsForSent8.put("message", messageSent);
				expectedFieldsForSent8.put("external_recipients", externalUser.getPropertyAtIndex(0).toString());//+", "+externalUser.getPropertyAtIndex(1));
				expectedFieldsForSent8.put("internal_recipients", internalUser.getPropertyAtIndex(0).toString());
				expectedFieldsForSent8.put("In_Folder","multiple");
				expectedFieldsForReceived8.put("external_recipients", externalUser.getPropertyAtIndex(0).toString());//+", "+externalUser.getPropertyAtIndex(1));
				expectedFieldsForReceived8.put("internal_recipients", internalUser.getPropertyAtIndex(0).toString());
				expectedFieldsForReceived8.put("In_Folder","Inbox");
				expectedFieldsForReceived8.put("message",messageReceived);

				
				return new Object[][]{
				/* 
				 * @param testCaseName
				 * @param testCaseDescription
				 * @param toEmail
				 * @param ccEmail
				 * @param bccEmail
				 * @param attachments
				 * @param expectedLogs
				 * @param expectedFieldsForSent
				 * @param expectedFieldsForReceived
				 *    
				 *  */
					
					{
						testcaseNames.get(1),	
						testcaseDescription.get(1),
						changeToEmailCollection(internalUser.getPropertyAtIndex(0)), 	
						changeToEmailCollection(internalUser.getPropertyAtIndex(1)),  	
						changeToEmailCollection(internalUser.getPropertyAtIndex(2)),  		
						null,	   	 
						expectedLogs1,  
						expectedFieldsForSent1,
						expectedFieldsForReceived1
					},
					{
						testcaseNames.get(2),	
						testcaseDescription.get(2),
						changeToEmailCollection(externalUser.getPropertyAtIndex(0)), 	
						changeToEmailCollection(externalUser.getPropertyAtIndex(1)),  	
						changeToEmailCollection(externalUser.getPropertyAtIndex(2)),  
						null,	   	 
						expectedLogs2,  
						expectedFieldsForSent2,
						expectedFieldsForReceived2
					},
					{
						
						testcaseNames.get(3),	
						testcaseDescription.get(3),
						changeToEmailCollection(externalUser.getPropertyAtIndex(0)),  	
						changeToEmailCollection(internalUser.getPropertyAtIndex(0)), 	
						changeToEmailCollection(internalUser.getPropertyAtIndex(1)), 
						null,
						expectedLogs3,  
						expectedFieldsForSent3,
						expectedFieldsForReceived3
					},
					{
						testcaseNames.get(4),	
						testcaseDescription.get(4),
						  
						changeToEmailCollection(internalUser.getPropertyAtIndex(0)), 	
						changeToEmailCollection(externalUser.getPropertyAtIndex(0)),  	
						changeToEmailCollection(internalUser.getPropertyAtIndex(1)), 
						null,
						expectedLogs4,  
						expectedFieldsForSent4,
						expectedFieldsForReceived4
					},
					{
						testcaseNames.get(5),	
						testcaseDescription.get(5),
						changeToEmailCollection(internalUser.getPropertyAtIndex(0)), 	
						changeToEmailCollection(internalUser.getPropertyAtIndex(1)), 
						changeToEmailCollection(externalUser.getPropertyAtIndex(0)),  	
						null,
						expectedLogs5,  
						expectedFieldsForSent5,
						expectedFieldsForReceived5
					},
					{
						testcaseNames.get(6),	
						testcaseDescription.get(6),
						changeToEmailCollection(externalUser.getPropertyAtIndex(0)),  	
						changeToEmailCollection(externalUser.getPropertyAtIndex(1)), 	
						changeToEmailCollection(internalUser.getPropertyAtIndex(0)), 
						null,
						expectedLogs6,  
						expectedFieldsForSent6,
						expectedFieldsForReceived6
					},
					{
						testcaseNames.get(7),	
						testcaseDescription.get(7),
						changeToEmailCollection(externalUser.getPropertyAtIndex(0)),  	
						changeToEmailCollection(internalUser.getPropertyAtIndex(0)), 
						changeToEmailCollection(externalUser.getPropertyAtIndex(1)), 	
						null,
						expectedLogs7,  
						expectedFieldsForSent7,
						expectedFieldsForReceived7
					},
					{
						testcaseNames.get(8),	
						testcaseDescription.get(8),
						changeToEmailCollection(internalUser.getPropertyAtIndex(0)), 
						changeToEmailCollection(externalUser.getPropertyAtIndex(0)),  	
						changeToEmailCollection(externalUser.getPropertyAtIndex(1)), 	
						null,
						expectedLogs8,  
						expectedFieldsForSent8,
						expectedFieldsForReceived8
					},
					
				};
			}
		    
			/**
			 * @param testCaseName
			 * @param testCaseDescription
			 * @param toEmail
			 * @param ccEmail
			 * @param bccEmail
			 * @param attachments
			 * @param subject
			 * @param expectedLogs
			 * @param expectedFieldsForSent
			 * @param expectedFieldsForReceived
			 * @throws Exception
			 */
			@SuppressWarnings("null")
			@Test(groups={"MAIL3","P1"},dataProvider = "dataProviderMailCombinations")
			public void verifyMailCombinations(
					String testCaseName,
					String testCaseDescription,
					EmailAddressCollection toEmail, 	
					EmailAddressCollection ccEmail, 	
					EmailAddressCollection bccEmail, 		
					ArrayList<String> attachments, 	   	 
					ArrayList<String> expectedLogs,   
					Map<String,Object> expectedFieldsForSent,
					Map<String,Object> expectedFieldsForReceived
					) throws Exception {
				
				Reporter.log("****************Test Case Description****************",true);
				Reporter.log("Test Case Name:"+testCaseName,true);
				Reporter.log("Test Case Description:"+testCaseDescription,true);
				Reporter.log("*****************************************************",true);
				

				//Prepare the remediation object
				microsoft.exchange.webservices.data.core.service.item.Item emailObj = null;
				String myUniqueId = UUID.randomUUID().toString();
				ArrayList<String> renamedAttachment = new ArrayList<String>() ;
				String newFileName = null;
				File sourceFile1 =null;
				File destFile1 = null;
				String myMailSubject =myUniqueId+testCaseName;
				String mailBody = null;
				boolean success =false;
				String docType = null;
				String searchFileName =null;
				int countAfterExposure =0;
				int responseCode = 0;
				String messageBody ="This is test message";
				
				if(attachments!=null){
					for(int i=0;i<=attachments.size()-1;i++){
						sourceFile1 = new File(userDir +attachments.get(i));
						newFileName = myUniqueId+attachments.get(i);
						destFile1 = new File(userDir +newFileName);
						copyFileUsingFileChannels(sourceFile1, destFile1);
						renamedAttachment.add(destFile1.toString());
						filesToDelete.add(destFile1);
					}
				}

				

				//creating mail object with testuser1 credentials
				Office365MailActivities objMailTestUser1New ;
				synchronized(this){objMailTestUser1New = new Office365MailActivities(testUser1,testUser1Pwd);}


				Reporter.log("------------------------------------------------------------------------",true );
				Reporter.log("Sending mail",true);
				Reporter.log("------------------------------------------------------------------------",true );

				cleanupListSent.add(myMailSubject);
				synchronized(this){ success = objMailTestUser1New.sendMailWithCCAndBCC(toEmail, ccEmail, bccEmail, myMailSubject, messageBody, renamedAttachment, true);}
				assertTrue(success, "Failed sending mail with subject:"+myMailSubject+".");
				success=false;

				ArrayList<String> myMailMessages = new ArrayList<String>();
				Reporter.log("Waiting for 1 minutes for logs to appear.",true);
				
				Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
				myMailMessages=searchLogs("all_messages", null, myUniqueId, suiteData.getUsername(),2);
				
				
				
				softAssert = new SoftAssert();
				
				for(int i=0;i<=(expectedLogs.size()-1);i++){
					expectedLogs.set(i, expectedLogs.get(i).replace("<mailsubject>", myMailSubject));
				}
//				displayMessageContent(myMailMessages);
				assertTrue(compareResult(expectedLogs, myMailMessages),"Test failed, expected log is not found.");
				
				String searchResponse = searchInvestigateLogs(-60, 60, "Office 365","all_messages", null, myUniqueId, suiteData.getUsername(), suiteData.getCSRFToken(), suiteData.getSessionID(),testUser1);
				ForensicSearchResults fsr = MarshallingUtils.unmarshall(searchResponse, ForensicSearchResults.class);
			
				
				String[] internalRecepientsForSent = new String[]{};
				String[] externalRecepientsForSent = new String[]{};
				
				String[] internalRecepientsForReceived = new String[]{};
				String[] externalRecepientsForReceived = new String[]{};
				
				//preparing internal and external recipient for Sent log
				if(expectedFieldsForSent.containsKey("internal_recipients")){
					if(expectedFieldsForSent.get("internal_recipients").toString().contains(", ")){
						internalRecepientsForSent = expectedFieldsForSent.get("internal_recipients").toString().split(", ");
					}
					else{
						internalRecepientsForSent = new String[1];
						internalRecepientsForSent[0] = expectedFieldsForSent.get("internal_recipients").toString();
					}
				}
				if(expectedFieldsForSent.containsKey("external_recipients")){
					if(expectedFieldsForSent.get("external_recipients").toString().contains(", ")){
						externalRecepientsForSent = expectedFieldsForSent.get("external_recipients").toString().split(", ");
					}
					else{
						externalRecepientsForSent = new String[1];
						externalRecepientsForSent[0] = expectedFieldsForSent.get("external_recipients").toString();
					}
				}
				//preparing internal and external recipient for Received log
				if(expectedFieldsForReceived.containsKey("internal_recipients")){
					if(expectedFieldsForReceived.get("internal_recipients").toString().contains(", ")){
						internalRecepientsForReceived = expectedFieldsForReceived.get("internal_recipients").toString().split(", ");
					}
					else{
						internalRecepientsForReceived = new String[1];
						internalRecepientsForReceived[0] = expectedFieldsForReceived.get("internal_recipients").toString();
					}
				}
				if(expectedFieldsForReceived.containsKey("external_recipients")){
					if(expectedFieldsForReceived.get("external_recipients").toString().contains(", ")){
						externalRecepientsForReceived = expectedFieldsForReceived.get("external_recipients").toString().split(", ");
					}
					else{
						externalRecepientsForReceived = new String[1];
						externalRecepientsForReceived[0] = expectedFieldsForReceived.get("external_recipients").toString();
					}
				}
				
			
				
				//Checking each recipients in sent logs
				for(int i=0;i<=( fsr.getHits().getTotal()-1);i++){
					
					if(fsr.getHits().getHits().get(i).getSource().getActivityType().equals("Send"))
					{
						for (String internal : internalRecepientsForSent) {
							softAssert.assertTrue(fsr.getHits().getHits().get(i).getSource().getInternalRecipients().contains(internal), "User:"+internal+" is not found in internal_recipients field of Sent Activlty Log.");
						}
						for (String external : externalRecepientsForSent) {
							softAssert.assertTrue(fsr.getHits().getHits().get(i).getSource().getExternalRecipients().contains(external), "User:"+external+" is not found in external_recipients field of Sent Activlty Log.");
						}
					}
					
					//Checking each recipients in received logs
					if(fsr.getHits().getHits().get(i).getSource().getActivityType().equals("Receive"))
					{
						for (String internal : internalRecepientsForReceived) {
							softAssert.assertTrue(fsr.getHits().getHits().get(i).getSource().getInternalRecipients().contains(internal), "User:"+internal+" is not found in internal_recipients field of Sent Activlty Log.");
						}
						for (String external : externalRecepientsForReceived) {
							softAssert.assertTrue(fsr.getHits().getHits().get(i).getSource().getExternalRecipients().contains(external), "User:"+external+" is not found in external_recipients field of Sent Activlty Log.");
						}
					}
					
				}
				
				if(expectedFieldsForSent.get("message") != null){
					expectedFieldsForSent.put("message",expectedFieldsForSent.get("message").toString().replace("<mailsubject>", myMailSubject));
				}
//				if(expectedFieldsForReceived.get("message") != null){
//					expectedFieldsForReceived.put("message",expectedFieldsForReceived.get("message").toString().replace("<mailsubject>", myMailSubject));
//				}
				
//				expectedFieldsForSent.put("Activity_type", "Send");
////				expectedFieldsForSent.remove("external_recipients");
////				expectedFieldsForSent.remove("internal_recipients");
////				softAssert.assertTrue(checkFieldInResponse(searchResponse, "$.hits.hits[*].source", expectedFieldsForSent), "Expected field combination not found in Sent activity log.");
////				
////				expectedFieldsForSent.put("Activity_type", "Receive");
//				expectedFieldsForReceived.remove("external_recipients");
//				expectedFieldsForReceived.remove("internal_recipients");
//				softAssert.assertTrue(checkFieldInResponse(searchResponse, "$.hits.hits[*].source", expectedFieldsForReceived), "Expected field combination not found in Received activity log.");
				softAssert.assertAll();
				
				Reporter.log("Internal/External user count matches", true);

			}
			
			@SuppressWarnings("null")
			@Test(groups={"MAIL3","P2"},retryAnalyzer=RetryAnalyzer.class)
			public void verifyMailWithInlineImagesSignature() throws ServiceLocalException, Exception {
				
				Reporter.log("****************Test Case Description****************",true);
				Reporter.log("Test Case Description:This testcase sends mail with multiple images (logos) as inline attachment, and verifies the logs",true);
				Reporter.log("1)Send Mail with multiple inline attachment",true);
				Reporter.log("2)Verify that multiple image upload logs are not generated",true);
				Reporter.log("*****************************************************",true);
				
				String myUniqueId = UUID.randomUUID().toString();
				boolean success = false;
				
				ArrayList<String> myAttachments = new ArrayList<String> ();
				ArrayList<String> renamedAttachment = new ArrayList<String>();
				ArrayList<String> attachmentLogs = new ArrayList<String>();
				myAttachments.add("LogoAudit.bmp");
				myAttachments.add("LogoDetect.gif");
				myAttachments.add("LogoInvestigate.png");
				myAttachments.add("LogoProtect.jpg");
				myAttachments.add("LogoElastica.png");
				myAttachments.add("LogoCloudSoc.png");
				
				
				String bodyFile= "mailWithLogo.txt";
				String newFileName1 = null;
				File sourceFile1 = null;
				File destFile1 = null;
				
				for(int i=0;i<=myAttachments.size()-1;i++){
					sourceFile1 = new File(userDir +myAttachments.get(i));
					newFileName1 = myUniqueId+myAttachments.get(i);
					destFile1 = new File(userDir +newFileName1);
					copyFileUsingFileChannels(sourceFile1, destFile1);
					renamedAttachment.add(destFile1.toString());
					filesToDelete.add(destFile1);
				}
				
				String readFile = userDir  + bodyFile;
				String myMailSubject = myUniqueId+"_MailWithInlineImage";
				cleanupListSent.add(myMailSubject);
				
				EmailAddressCollection toEmail = new EmailAddressCollection();
				toEmail.add(testUser2);
				
				Thread.sleep(30000);
				success =objMailTestUser1.sendMailWithInLineAttachment(toEmail, null, null, myMailSubject, renamedAttachment, true);
				
				assertTrue(success, "Failed sending mail with subject:"+myMailSubject+".");
				success=false;
				
				Thread.sleep(120000);
				
				//Forward mail test
				//Send a mail to self and Forward that mail
				
				ArrayList<String> myMailMessages = new ArrayList<String>();
				ArrayList<String> logs = new ArrayList<String>();
				try {
					for (int i = 180000; i <= ( MAX_LOG_WAIT_TIME_IN_MINUTES * 60 * 1000); i+=60000 ) {
						Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
						Reporter.log("------------------------------------------------------------------------",true );
						Reporter.log("Searching for logs after :"+TimeUnit.MILLISECONDS.toMinutes(i)+" minutes",true );
						Reporter.log("------------------------------------------------------------------------" ,true);
						logs = searchDisplayLogs(fromTime, 60, "Office 365","all_messages", null, myUniqueId, suiteData.getUsername(), suiteData.getCSRFToken(), suiteData.getSessionID(),testUser1);
						myMailMessages.addAll(logs);
						
						Set<String> hs = new HashSet<>();
						hs.addAll(myMailMessages);
						myMailMessages.clear();
						myMailMessages.addAll(hs);
						
						displayMessageContent(myMailMessages);
						
						//Reporter.log("Actual file messages:" + actualMailMessages, true);
						if (myMailMessages.size() >= 2) {break;}
					}
				}
				catch(Exception e) {}
				
				logs.clear();
				logs = searchDisplayLogs(fromTime, 60, "Office 365","Object_type", "Email_File_Attachment", myUniqueId, suiteData.getUsername(), suiteData.getCSRFToken(), suiteData.getSessionID(),testUser1);
				attachmentLogs.addAll(logs);
				
				displayMessageContent(myMailMessages);
				
				ArrayList<String> ExpectedLogs = new ArrayList<String>();
				softAssert = new SoftAssert();
				
				ExpectedLogs.add("User sent an email  to "+testUser2+" with subject \""+myMailSubject+"\"");
//				ExpectedLogs.add("User received an email  from "+testUser1+" with subject \""+myMailSubject+"\"");
				
				
				
				
				softAssert.assertEquals(attachmentLogs.size(),0,"Test failed, Inline images appeared as attachments.");
				softAssert.assertTrue(compareResult(ExpectedLogs, myMailMessages),"Test failed, expected log is not found.");
				
				softAssert.assertAll();
				
			}
			
			@SuppressWarnings("null")
			@Test(groups={"MAIL3"},retryAnalyzer=RetryAnalyzer.class)
			public void verifyMailWithSameFileAttachmentMultipleTimes_BCB_40() throws ServiceLocalException, Exception {
				
				Reporter.log("****************Test Case Description****************",true);
				Reporter.log("Test Case Description:This testcase sends mail with same file attached multiple times,  verifies the logs. Ref: Assembla #26945",true);
				Reporter.log("1)Send Mail, with same file attached multiple times",true);
				Reporter.log("2)Verify email is sent and log is generated separately for each of the file attchment",true);
				Reporter.log("*****************************************************",true);
				
				String myUniqueId = UUID.randomUUID().toString();
				boolean success = false;
				
				ArrayList<String> myAttachments = new ArrayList<String> ();
				ArrayList<String> renamedAttachment = new ArrayList<String>();
				ArrayList<String> attachmentLogs = new ArrayList<String>();
				myAttachments.add("LogoInvestigate.png");
				myAttachments.add("LogoInvestigate.png");
				myAttachments.add("LogoInvestigate.png");
				myAttachments.add("LogoInvestigate.png");
				myAttachments.add("LogoInvestigate.png");
				
				
				String bodyFile= "mailWithLogo.txt";
				String newFileName1 = null;
				File sourceFile1 = null;
				File destFile1 = null;
				
				for(int i=0;i<=myAttachments.size()-1;i++){
					sourceFile1 = new File(userDir +myAttachments.get(i));
					newFileName1 = myUniqueId+myAttachments.get(i);
					destFile1 = new File(userDir +newFileName1);
					copyFileUsingFileChannels(sourceFile1, destFile1);
					renamedAttachment.add(destFile1.toString());
					filesToDelete.add(destFile1);
				}
				
				String readFile = userDir  + bodyFile;
				String myMailSubject = myUniqueId+"_DuplicateAttachments";
				cleanupListSent.add(myMailSubject);
				
				EmailAddressCollection toEmail = new EmailAddressCollection();
				toEmail.add(testUser2);
				
				success =objMailTestUser1.sendMail(testUser2, myMailSubject, "Test Mail", renamedAttachment, true);
				
				assertTrue(success, "Failed sending mail with subject:"+myMailSubject+".");
				success=false;
				
				Thread.sleep(180000);
				
				//Forward mail test
				//Send a mail to self and Forward that mail
				
				ArrayList<String> myMailMessages = new ArrayList<String>();
				ArrayList<String> logs = new ArrayList<String>();
				try {
					for (int i = 180000; i <= ( MAX_LOG_WAIT_TIME_IN_MINUTES * 60 * 1000); i+=60000 ) {
						Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
						Reporter.log("------------------------------------------------------------------------",true );
						Reporter.log("Searching for logs after :"+TimeUnit.MILLISECONDS.toMinutes(i)+" minutes",true );
						Reporter.log("------------------------------------------------------------------------" ,true);
						logs = searchDisplayLogs(fromTime, 60, "Office 365","Object_type", "Email_File_Attachment", myUniqueId, suiteData.getUsername(), suiteData.getCSRFToken(), suiteData.getSessionID(),testUser1);
						myMailMessages.addAll(logs);
						
						if (myMailMessages.size() >= 4) {break;}
//						
//						Set<String> hs = new HashSet<>();
//						hs.addAll(myMailMessages);
						myMailMessages.clear();
//						myMailMessages.addAll(hs);
//						
//						displayMessageContent(myMailMessages);
						
						//Reporter.log("Actual file messages:" + actualMailMessages, true);
						
					}
				}
				catch(Exception e) {}
				
//				myMailMessages=searchLogs("Object_type", "Email_File_Attachment", myUniqueId, testUser1,5);
				
				
				displayMessageContent(myMailMessages);
				
//				ArrayList<String> ExpectedLogs = new ArrayList<String>();
				softAssert = new SoftAssert();
				
//				ExpectedLogs.add("User sent an email  to "+testUser2+" with subject \""+myMailSubject+"\"");
//				ExpectedLogs.add("User received an email  from "+testUser1+" with subject \""+myMailSubject+"\"");
				
				softAssert.assertEquals(myMailMessages.size(),5,"Test failed, email attachment activity log count doesn't match.");
//				softAssert.assertTrue(compareResult(ExpectedLogs, myMailMessages),"Test failed, expected log is not found.");
				
				softAssert.assertAll();
				
			}
		@SuppressWarnings("null")
		@Test(groups={"MAIL3","P1"},retryAnalyzer=RetryAnalyzer.class)  // Related bug for reference #30435
		public void verifyMailSentToGroup() throws ServiceLocalException, Exception {

			Reporter.log("****************Test Case Description****************",true);
			Reporter.log("Test Case Description:Send an email to a group having multiple users, verify that receive mail log is generated for all users in the group.",true);
			Reporter.log("1)Send Mail to a group containing 2 internal users",true);
			Reporter.log("2)Verify that mail received message is generated for all users of group",true);
			Reporter.log("*****************************************************",true);

			String myUniqueId = UUID.randomUUID().toString();
			boolean success = false;
			
			ArrayList<String> myAttachments = new ArrayList<String> ();
			ArrayList<String> renamedAttachment = new ArrayList<String>();
			ArrayList<String> attachmentLogs = new ArrayList<String>();
			
			myAttachments.add("mailWithLogo.txt");
			
//			String bodyFile= "mailWithLogo.txt";
			String newFileName1 = null;
			File sourceFile1 = null;
			File destFile1 = null;
			
			for(int i=0;i<=myAttachments.size()-1;i++){
				sourceFile1 = new File(userDir +myAttachments.get(i));
				newFileName1 = myUniqueId+myAttachments.get(i);
				destFile1 = new File(userDir +newFileName1);
				copyFileUsingFileChannels(sourceFile1, destFile1);
				renamedAttachment.add(destFile1.toString());
				filesToDelete.add(destFile1);
			}

//			String readFile = userDir  + bodyFile;
			String myMailSubject = myUniqueId+"_MailToGroup";
			cleanupListSent.add(myMailSubject);

			synchronized(this){success =objMailExternalUser.sendMail(groupMailId, myMailSubject, "Test Mail", renamedAttachment, true);}

			assertTrue(success, "Failed sending mail with subject:"+myMailSubject+".");
			success=false;

			Reporter.log("Wait for 2 mins before checking for logs", true);
			Thread.sleep(120000);

			//Forward mail test
			//Send a mail to self and Forward that mail
			
			
			softAssert = new SoftAssert();
			
			ArrayList<String> ExpectedLogs = new ArrayList<String>();
			// Related bug for reference #30435
//			String emailSentMsg = "User sent an email  to 2 users  with subject \""+myMailSubject+"\"";
//			String emailSentAttachmentMsg = "User sent an email  to 2 users  with subject \""+myMailSubject+"\" with attachment \""+newFileName1+"\"";
			String emailRcvdMsg = "User received an email  from "+externalUser2+" with subject \""+myMailSubject+"\"";
			String emailRcvdAttachmentMsg = "User received an email  from "+externalUser2+" with subject \""+myMailSubject+"\" with attachment \""+newFileName1+"\"";
//			ExpectedLogs.add(emailSentMsg);
//			ExpectedLogs.add(emailSentAttachmentMsg);
			ExpectedLogs.add(emailRcvdMsg);
			ExpectedLogs.add(emailRcvdAttachmentMsg);
			
			ArrayList<String> availableLogs1 = searchLogs("Activity_type", "Receive", myUniqueId, testUser1, 2);
			softAssert.assertTrue(compareResult(ExpectedLogs, availableLogs1),"Test failed, as mail received log is not found for user:"+testUser1);
			
			ArrayList<String> availableLogs2 = searchLogs("Activity_type", "Receive", myUniqueId, testUser2, 2);
			softAssert.assertTrue(compareResult(ExpectedLogs, availableLogs2),"Test failed, as mail received log is not found for user:"+testUser2);
			
			softAssert.assertAll();
			
		}
		
		
		/**
		 * @param subjectList
		 * @param fromFolder
		 * @param userName
		 * @param userPassword
		 * @throws InterruptedException
		 */
		void deleteMails(ArrayList<String> subjectList , String fromFolder, String userName, String userPassword) throws InterruptedException
		{
			
			if(subjectList.size()>=1){
				DeleteMode delMode = DeleteMode.HardDelete;	
				//creating mail object with testuser1 credentials
				Office365MailActivities objmyMail = new Office365MailActivities(userName,userPassword); 
				
				Reporter.log("-----------------------------",true);
				Reporter.log("Deleting Mails in "+fromFolder+" of "+userName,true);
				Reporter.log("-----------------------------",true);
				int i=1;
				for (String subject : subjectList) {
					Reporter.log(i+") "+ subject,true);
					objmyMail.deleteMailFromFolder(subject,fromFolder, delMode);
					i++;
				}
				
				Reporter.log("-----------------------------",true);
			}
			
		}
		/**
		 * @param myFiles
		 * @throws InterruptedException
		 */
		void deleteFiles(ArrayList<File> myFiles) throws InterruptedException
		{
			
			if(myFiles.size()>=1){
			
				Reporter.log("-----------------------------",true);
				Reporter.log("Deleting temp files",true);
				Reporter.log("-----------------------------",true);
				int i=1;
			   for (File file : myFiles) {
					file.delete();

				    Reporter.log(i+") "+ file.getName(),true);
					i++;
			   }
				
				Reporter.log("-----------------------------",true);
			}
			
		}
		
		@DataProvider(parallel=true)
			public Object[][] dataProviderMailFieldVerification() {
			 
			 	EmailAddressCollection internalUser = new EmailAddressCollection();
				EmailAddressCollection externalUser = new EmailAddressCollection();
				EmailAddressCollection externalAndInternal = new EmailAddressCollection();
				ArrayList<String> attachments = new ArrayList<String>();
				ArrayList<String> expectedLogs1 = new ArrayList<String>();
				ArrayList<String> expectedLogs2 = new ArrayList<String>();
				ArrayList<String> expectedLogs3 = new ArrayList<String>();
				ArrayList<String> expectedLogs4 = new ArrayList<String>();
				ArrayList<String> expectedLogs5 = new ArrayList<String>();
				ArrayList<String> expectedLogs6 = new ArrayList<String>();
				ArrayList<String> expectedLogs7  = new ArrayList<String>();
				ArrayList<String> expectedLogs8  = new ArrayList<String>();
				Map<String, Object> expectedFieldsForSent1 =  new HashMap();
				Map<String, Object> expectedFieldsForSent2 =  new HashMap();
				Map<String, Object> expectedFieldsForSent3 =  new HashMap();
				Map<String, Object> expectedFieldsForSent4 =  new HashMap();
				Map<String, Object> expectedFieldsForSent5 =  new HashMap();
				Map<String, Object> expectedFieldsForSent6 =  new HashMap();
				Map<String, Object> expectedFieldsForSent7 =  new HashMap();
				Map<String, Object> expectedFieldsForSent8 =  new HashMap();
				
				Map<String, Object> expectedFieldsForReceived1 =  new HashMap();
				Map<String, Object> expectedFieldsForReceived2 =  new HashMap();
				Map<String, Object> expectedFieldsForReceived3 =  new HashMap();
				Map<String, Object> expectedFieldsForReceived4 =  new HashMap();
				Map<String, Object> expectedFieldsForReceived5 =  new HashMap();
				Map<String, Object> expectedFieldsForReceived6 =  new HashMap();
				Map<String, Object> expectedFieldsForReceived7 =  new HashMap();
				Map<String, Object> expectedFieldsForReceived8 =  new HashMap();
				Map<Integer, String> mailAction =  new HashMap();
				Map<Integer, Integer> noOfLogsToWaitFor =  new HashMap();
				Map<Integer, String> testCaseName =  new HashMap();
				Map<Integer, String> testCaseDescription =  new HashMap();
				Map<Integer, Object> emailTo =  new HashMap();
				Map<Integer, Object> emailCc =  new HashMap();
				Map<Integer, Object> emailBcc =  new HashMap();
				Map<Integer, Object> allAttachments =  new HashMap();
				
				String subject = null;
				attachments.add("simpleTextFile.txt");
				internalUser.add(testUser2);
				internalUser.add(adminMailId);
				internalUser.add(testUser1);
				
				externalUser.add("farhan.jaleel+1@elasticaqa.net");
				externalUser.add("farhan.jaleel+2@elasticaqa.net");
				externalUser.add("farhan.jaleel+3@elasticaqa.net");
				
				externalAndInternal.add(adminMailId);
//				bccEmail.add("abdul.nissar@elastica.co");
				
				//Testcase 1: InternalMailWithoutAttachment check Email_Message
				mailAction.put(1, "SendMail");
				noOfLogsToWaitFor.put(1, 1);
				testCaseName.put(1, "InternalMailWithoutAttachmentCheckEmail_Message");
				testCaseDescription.put(1, "Send a mail to an internal user. Verify that mail messages appears properly and verify the fields in send and received messages. " );
				emailTo.put(1, changeToEmailCollection(internalUser.getPropertyAtIndex(0)));
				emailCc.put(1, null);
				emailBcc.put(1, null);	
				allAttachments.put(1, null);
				expectedLogs1.add("User sent an email  to "+testUser2+" with subject \""+"<mailsubject>\"");
				//send fields
				expectedFieldsForSent1.put("message", "User sent an email  to "+testUser2+" with subject \""+"<mailsubject>\"");
				expectedFieldsForSent1.put("internal_recipients",internalUser.getPropertyAtIndex(0).getAddress());
				expectedFieldsForSent1.put("external_recipients","");
				expectedFieldsForSent1.put("File_Size",0);
				expectedFieldsForSent1.put("Object_type","Email_Message");
				expectedFieldsForSent1.put("Activity_type", "Send");
//				expectedFieldsForSent1.put("user", testUser1);
				expectedFieldsForSent1.put("user_name", "NOT_EMPTY");
				expectedFieldsForSent1.put("In_Folder", "NOT_EMPTY");
				
				//received fields	
				expectedFieldsForReceived1=null;
//				expectedFieldsForReceived1.put("message", "User received an email  from <fromUser> with subject \""+"<mailsubject>\"");
//				expectedFieldsForReceived1.put("internal_recipients",internalUser.getPropertyAtIndex(0).getAddress());
//				expectedFieldsForReceived1.put("external_recipients","");
//				expectedFieldsForReceived1.put("File_Size",0);
//				expectedFieldsForReceived1.put("Object_type","Email_Message");
//				expectedFieldsForReceived1.put("Activity_type", "Receive");
//				expectedFieldsForReceived1.put("user", testUser2);
//				expectedFieldsForReceived1.put("user_name", testUser2.split("@")[0]);
//				expectedFieldsForReceived1.put("In_Folder", "NOT_EMPTY");

				
				//Testcase 2: ExternalMailWithoutAttachment check Email_Message
				mailAction.put(2, "SendMailByExternal");
				noOfLogsToWaitFor.put(2, 1);
				testCaseName.put(2, "ExternalMailWithoutAttachmentCheckEmail_Message");
				testCaseDescription.put(2, "Receive a mail from external user. Verify that mail messages appears properly and verify the fields in received messages. " );
				emailTo.put(2, changeToEmailCollection(internalUser.getPropertyAtIndex(0)));
				emailCc.put(2, null);
				emailBcc.put(2, null);	
				allAttachments.put(2, null);
				expectedLogs2.add("User received an email  from "+externalUser2+" with subject \""+"<mailsubject>\"");
				
				//sent logs
				expectedFieldsForSent2=null;
				//received fields				
				expectedFieldsForReceived2.put("message", "User received an email  from "+externalUser2+" with subject \""+"<mailsubject>\"");
				expectedFieldsForReceived2.put("internal_recipients",internalUser.getPropertyAtIndex(0).getAddress());
				expectedFieldsForReceived2.put("external_recipients","");
				expectedFieldsForReceived2.put("File_Size",0);
				expectedFieldsForReceived2.put("Object_type","Email_Message");
				expectedFieldsForReceived2.put("Activity_type", "Receive");
				expectedFieldsForReceived2.put("user", testUser2);
				expectedFieldsForReceived2.put("user_name", testUser2.split("@")[0]);
				expectedFieldsForReceived2.put("In_Folder", "NOT_EMPTY");
				
				
				//Testcase 2: SaveInDraftWithoutAttachment
//				mailAction.put(2, "SaveInDrafts");
//				noOfLogsToWaitFor.put(2, 1);
//				testCaseName.put(2, "SaveInDraftWithoutAttachment");
//				testCaseDescription.put(2,"Save mail in draft and verify that  messages appears properly and verify the fields for the same. "  );
//				emailTo.put(2, changeToEmailCollection(internalUser.getPropertyAtIndex(0)));
//				emailCc.put(2, null);
//				emailBcc.put(2, null);
//				allAttachments.put(2, null);
//				expectedLogs2.add("User saved an email  in Drafts  with subject \""+"<mailsubject>\"");
//				//send fields
//				expectedFieldsForSent2.put("message","User saved an email  in Drafts  with subject \""+"<mailsubject>\"");
//				expectedFieldsForSent2.put("internal_recipients","");
//				expectedFieldsForSent2.put("external_recipients","");
//				expectedFieldsForSent2.put("File_Size",0);
//				expectedFieldsForSent2.put("Object_type","Email_Message");
//				expectedFieldsForSent2.put("Activity_type", "Save");
////				expectedFieldsForSent2.put("user", testUser1);
//				expectedFieldsForSent2.put("user_name",  "NOT_EMPTY");
//				expectedFieldsForSent2.put("In_Folder", "Drafts");
//				
//				//received fields
//				expectedFieldsForReceived2=null;
				
				//Testcase 3: SaveInDraftWithAttachment
//				mailAction.put(3, "SaveInDrafts");
//				noOfLogsToWaitFor.put(3,2 );
//				testCaseName.put(3, "SaveInDraftWithAttachmentCheckEmail_File_Attachment");
//				testCaseDescription.put(3, "Save mail in draft and verify that  messages appears properly and verify the fields for the same. "  );
//				emailTo.put(3, changeToEmailCollection(internalUser.getPropertyAtIndex(0)));
//				emailCc.put(3, null);
//				emailBcc.put(3, null);	
//				allAttachments.put(3, attachments);
//				expectedLogs3.add("User saved an email  in Drafts  with subject \""+"<mailsubject>\" with attachment \"<attachmentName>\"");
//				//send fields
//				expectedFieldsForSent3.put("message","User saved an email  in Drafts  with subject \""+"<mailsubject>\" with attachment \"<attachmentName>\"");
//				expectedFieldsForSent3.put("internal_recipients","");
//				expectedFieldsForSent3.put("external_recipients","");
//				expectedFieldsForSent3.put("File_Size",355);
//				expectedFieldsForSent3.put("Object_type","Email_File_Attachment");
//				expectedFieldsForSent3.put("Activity_type", "Save");
////				expectedFieldsForSent3.put("user", testUser1);
//				expectedFieldsForSent3.put("user_name", "NOT_EMPTY");
//				expectedFieldsForSent3.put("In_Folder", "Drafts");
//				//received fields
//				expectedFieldsForReceived3=null;
				
				//Testcase 3: ExternalMailWithAttachment Check fields of Email_File_Attachment message
				mailAction.put(3, "SendMailByExternal");
				noOfLogsToWaitFor.put(3,2 );
				testCaseName.put(3, "ExternalMailWithAttachmentCheckEmail_File_Attachment");
				testCaseDescription.put(3,  "Receive a mail from external user with attachment. Verify that mail messages appears properly and verify the fields in received message for objec_type Email_File_Attachment. ");
				emailTo.put(3, changeToEmailCollection(internalUser.getPropertyAtIndex(0)));
				emailCc.put(3, null);
				emailBcc.put(3, null);	
				allAttachments.put(3, attachments);
				expectedLogs3.add("User received an email  from "+externalUser2+" with subject \""+"<mailsubject>\" with attachment \"<attachmentName>\"");
				//send fields
				expectedFieldsForSent3=null;
				//received fields
				expectedFieldsForReceived3.put("message","User received an email  from "+externalUser2+" with subject \""+"<mailsubject>\" with attachment \"<attachmentName>\"");
				expectedFieldsForReceived3.put("internal_recipients",internalUser.getPropertyAtIndex(0).getAddress());
				expectedFieldsForReceived3.put("external_recipients","");
				expectedFieldsForReceived3.put("File_Size",485);
				expectedFieldsForReceived3.put("Object_type","Email_File_Attachment");
				expectedFieldsForReceived3.put("Activity_type", "Receive");
				expectedFieldsForReceived3.put("user", testUser2);
				expectedFieldsForReceived3.put("user_name", testUser2.split("@")[0]);
				expectedFieldsForReceived3.put("In_Folder", "NOT_EMPTY");
				
				//Testcase 4: InternalMailWithAttachment Check fields of Email_File_Attachment message
				mailAction.put(4, "SendMail");
				noOfLogsToWaitFor.put(4,4 );
				testCaseName.put(4, "InternalMailWithAttachmentCheckEmail_File_Attachment");
				testCaseDescription.put(4,  "Send a mail to an internal user with attachment. Verify that mail messages appears properly and verify the fields in send and received message for objec_type Email_File_Attachment. ");
				emailTo.put(4, changeToEmailCollection(internalUser.getPropertyAtIndex(0)));
				emailCc.put(4, null);
				emailBcc.put(4, null);	
				allAttachments.put(4, attachments);
				expectedLogs4.add("User sent an email  to "+testUser2+" with subject \""+"<mailsubject>\" with attachment \"<attachmentName>\"");
				//send fields
				expectedFieldsForSent4.put("message","User sent an email  to "+testUser2+" with subject \""+"<mailsubject>\" with attachment \"<attachmentName>\"");
				expectedFieldsForSent4.put("internal_recipients",internalUser.getPropertyAtIndex(0).getAddress());
				expectedFieldsForSent4.put("external_recipients","");
				expectedFieldsForSent4.put("File_Size",355);
				expectedFieldsForSent4.put("Object_type","Email_File_Attachment");
				expectedFieldsForSent4.put("Activity_type", "Send");
//				expectedFieldsForSent4.put("user", testUser1);
				expectedFieldsForSent4.put("user_name",  "NOT_EMPTY");
				expectedFieldsForSent4.put("In_Folder", "NOT_EMPTY");
				//received fields
				expectedFieldsForReceived4=null;
//				expectedFieldsForReceived4.put("message","User received an email  from <fromUser> with subject \""+"<mailsubject>\" with attachment \"<attachmentName>\"");
//				expectedFieldsForReceived4.put("internal_recipients",internalUser.getPropertyAtIndex(0).getAddress());
//				expectedFieldsForReceived4.put("external_recipients","");
//				expectedFieldsForReceived4.put("File_Size",355);
//				expectedFieldsForReceived4.put("Object_type","Email_File_Attachment");
//				expectedFieldsForReceived4.put("Activity_type", "Receive");
//				expectedFieldsForReceived4.put("user", testUser2);
//				expectedFieldsForReceived4.put("user_name", testUser2.split("@")[0]);
//				expectedFieldsForReceived4.put("In_Folder", "NOT_EMPTY");
				
				//Testcase 5: InternalMailWithAttachment Check fields of  Email_Message message
				mailAction.put(5, "SendMail");
				noOfLogsToWaitFor.put(5,4 );
				testCaseName.put(5, "InternalMailWithAttachmentCheckEmail_Message");
				testCaseDescription.put(5,  "Send a mail to an internal user with attachment. Verify that mail messages appears properly and verify the fields in send and received messages for object_type Email_Message.");
				emailTo.put(5, changeToEmailCollection(internalUser.getPropertyAtIndex(0)));
				emailCc.put(5, null);
				emailBcc.put(5, null);	
				allAttachments.put(5, attachments);
				expectedLogs5.add("User sent an email  to "+internalUser.getPropertyAtIndex(0).getAddress()+" with subject \""+"<mailsubject>\"");
				//send fields
				expectedFieldsForSent5.put("message","User sent an email  to "+internalUser.getPropertyAtIndex(0).getAddress()+" with subject \""+"<mailsubject>\"");
				expectedFieldsForSent5.put("internal_recipients",internalUser.getPropertyAtIndex(0).getAddress());
				expectedFieldsForSent5.put("external_recipients","");
				expectedFieldsForSent5.put("File_Size",0);
				expectedFieldsForSent5.put("Object_type","Email_Message");
				expectedFieldsForSent5.put("Activity_type", "Send");
//				expectedFieldsForSent5.put("user", testUser1);
				expectedFieldsForSent5.put("user_name",  "NOT_EMPTY");
				expectedFieldsForSent5.put("In_Folder", "NOT_EMPTY");
				//received fields	
				expectedFieldsForReceived5=null;
//				expectedFieldsForReceived5.put("message","User received an email  from <fromUser> with subject \""+"<mailsubject>\"");
//				expectedFieldsForReceived5.put("internal_recipients",internalUser.getPropertyAtIndex(0).getAddress());
//				expectedFieldsForReceived5.put("external_recipients","");
//				expectedFieldsForReceived5.put("File_Size",0);
//				expectedFieldsForReceived5.put("Object_type","Email_Message");
//				expectedFieldsForReceived5.put("Activity_type", "Receive");
//				expectedFieldsForReceived5.put("user", testUser2);
//				expectedFieldsForReceived5.put("user_name", testUser2.split("@")[0]);
//				expectedFieldsForReceived5.put("In_Folder", "NOT_EMPTY");
				
				//Testcase 6: SaveInDraftWithAttachment check Email_Message
//				mailAction.put(6, "SaveInDrafts");
//				noOfLogsToWaitFor.put(6,2 );
//				testCaseName.put(6, "SaveInDraftWithAttachmentCheckEmail_Message");
//				testCaseDescription.put(6, "Save mail in draft and verify that  messages appears properly and verify the fields for the same. "  );
//				emailTo.put(6, changeToEmailCollection(internalUser.getPropertyAtIndex(0)));
//				emailCc.put(6, null);
//				emailBcc.put(6, null);	
//				allAttachments.put(6, attachments);
//				expectedLogs2.add("User saved an email  in Drafts  with subject \""+"<mailsubject>\"");
//				//send fields
//				expectedFieldsForSent6.put("message","User saved an email  in Drafts  with subject \""+"<mailsubject>\"");
//				expectedFieldsForSent6.put("internal_recipients","");
//				expectedFieldsForSent6.put("external_recipients","");
//				expectedFieldsForSent6.put("File_Size",0);
//				expectedFieldsForSent6.put("Object_type","Email_Message");
//				expectedFieldsForSent6.put("Activity_type", "Save");
////				expectedFieldsForSent6.put("user", testUser1);
//				expectedFieldsForSent6.put("user_name",  "NOT_EMPTY");
//				expectedFieldsForSent6.put("In_Folder", "Drafts");
//				//received fields
//				expectedFieldsForReceived6=null;
				
				//Testcase 7: DeleteMail
				mailAction.put(7, "DeleteMail");
				noOfLogsToWaitFor.put(7, 3);
				testCaseName.put(7, "DeleteMail");
				testCaseDescription.put(7,"Delete an email and verify the logs. "  );
				emailTo.put(7, changeToEmailCollection(internalUser.getPropertyAtIndex(0)));
				emailCc.put(7, null);
				emailBcc.put(7, null);
				allAttachments.put(7, null);
				expectedLogs7.add("User deleted an email  with subject \"<mailsubject>\"");
				//send fields
				expectedFieldsForSent7.put("message","User deleted an email  with subject \"<mailsubject>\"");
				expectedFieldsForSent7.put("internal_recipients",internalUser.getPropertyAtIndex(0).getAddress());
				expectedFieldsForSent7.put("external_recipients","");
				expectedFieldsForSent7.put("File_Size",0);
				expectedFieldsForSent7.put("Object_type","Email_Message");
				expectedFieldsForSent7.put("Activity_type", "Delete");
//				expectedFieldsForSent7.put("user", testUser1);
				expectedFieldsForSent7.put("user_name",  "NOT_EMPTY");
				expectedFieldsForSent7.put("In_Folder", "NOT_EMPTY");
				//received fields
				expectedFieldsForReceived7=null;
				

				return new Object[][]{
				/* 
				 * @param testCaseName
				 * @param testCaseDescription
				 * @param toEmail
				 * @param ccEmail
				 * @param bccEmail
				 * @param attachments
				 * @param subject
				 * @param expectedLogs
				 * @param expectedFieldsForSent
				 * @param expectedFieldsForReceived
				 *    
				 *  */
					
					{
						
						//Testcase 1: InternalMailWithoutAttachment
						mailAction.get(1),
						noOfLogsToWaitFor.get(1),
						testCaseName.get(1),
						testCaseDescription.get(1),
						emailTo.get(1),
						emailCc.get(1),
						emailBcc.get(1),	
						allAttachments.get(1),
						expectedLogs1,  
						expectedFieldsForSent1,
						expectedFieldsForReceived1
					},
					{
//						//Testcase 2: ExternalMailWithoutAttachment
//						"EndUser",
						mailAction.get(2),
						noOfLogsToWaitFor.get(2),
						testCaseName.get(2),
						testCaseDescription.get(2),
						emailTo.get(2),
						emailCc.get(2),
						emailBcc.get(2),
						allAttachments.get(2),
						expectedLogs2,  
						expectedFieldsForSent2,
						expectedFieldsForReceived2
					},
					{
						//Testcase 3: ExternalMailWithAttachment
//						"EndUser",
						mailAction.get(3),
						noOfLogsToWaitFor.get(3),
						testCaseName.get(3),
						testCaseDescription.get(3),
						emailTo.get(3),
						emailCc.get(3),
						emailBcc.get(3),
						allAttachments.get(3),
						expectedLogs3,  
						expectedFieldsForSent3,
						expectedFieldsForReceived3
					},
					{
						
						//Testcase 4: InternalMailWithAttachment
						mailAction.get(4),
						noOfLogsToWaitFor.get(4),
						testCaseName.get(4),
						testCaseDescription.get(4),
						emailTo.get(4),
						emailCc.get(4),
						emailBcc.get(4),
						allAttachments.get(4),
						expectedLogs4,  
						expectedFieldsForSent4,
						expectedFieldsForReceived4
					},
					{
						
						//Testcase 5: InternalMailWithAttachment Check fields of  Email_Message message
						mailAction.get(5),
						noOfLogsToWaitFor.get(5),
						testCaseName.get(5),
						testCaseDescription.get(5),
						emailTo.get(5),
						emailCc.get(5),
						emailBcc.get(5),
						allAttachments.get(5),
						expectedLogs5,  
						expectedFieldsForSent5,
						expectedFieldsForReceived5
					},
//					{
//						
//						//Testcase 6: SaveInDraftWithAttachment check Email_Message
//						"EndUser",
//						mailAction.get(6),
//						noOfLogsToWaitFor.get(6),
//						testCaseName.get(6),
//						testCaseDescription.get(6),
//						emailTo.get(6),
//						emailCc.get(6),
//						emailBcc.get(6),
//						allAttachments.get(6),
//						expectedLogs6,  
//						expectedFieldsForSent6,
//						expectedFieldsForReceived6
//					},
					{
						
						//Testcase 7: DeleteMail
						mailAction.get(7),
						noOfLogsToWaitFor.get(7),
						testCaseName.get(7),
						testCaseDescription.get(7),
						emailTo.get(7),
						emailCc.get(7),
						emailBcc.get(7),
						allAttachments.get(7),
						expectedLogs7,  
						expectedFieldsForSent7,
						expectedFieldsForReceived7
					}
					
				};
			}
		    
			
		/**
		 * @param mailAction
		 * @param noOfLogsToWaitFor
		 * @param testCaseName
		 * @param testCaseDescription
		 * @param toEmail
		 * @param ccEmail
		 * @param bccEmail
		 * @param attachments
		 * @param expectedLogsNew
		 * @param expectedFieldsForSentNew
		 * @param expectedFieldsForReceivedNew
		 * @throws Exception
		 */
		@SuppressWarnings("null")
		@Test(groups={"MAIL3","P1"},dataProvider = "dataProviderMailFieldVerification")
		public void verifyMailFieldsEndUser(
				String mailAction,
				int noOfLogsToWaitFor,
				String testCaseName,
				String testCaseDescription,
				EmailAddressCollection toEmail, 	
				EmailAddressCollection ccEmail, 	
				EmailAddressCollection bccEmail, 		
				ArrayList<String> attachments, 	   	 
				ArrayList<String> expectedLogs,   
				Map<String,Object> expectedFieldsForSent,
				Map<String,Object> expectedFieldsForReceived
				) throws Exception {
			
			Reporter.log("****************Test Case Description****************",true);
			Reporter.log("Test Case Name:"+testCaseName,true);
			Reporter.log("Test Case Description:"+testCaseDescription,true);
			Reporter.log("*****************************************************",true);
			
			
			ArrayList<String> expectedLogsNew = expectedLogs;
			Map<String, Object>  expectedFieldsForSentNew=expectedFieldsForSent;
			Map<String, Object> expectedFieldsForReceivedNew = expectedFieldsForReceived;
			microsoft.exchange.webservices.data.core.service.item.Item emailObj = null;
			String myUniqueId = UUID.randomUUID().toString();
			ArrayList<String> renamedAttachment = new ArrayList<String>() ;
			String newFileName = null;
			File sourceFile1 =null;
			File destFile1 = null;
			String myMailSubject =myUniqueId+testCaseName;
			String mailBody = null, senderEmailId="";
			boolean success =false;
			String docType = null;
			String searchFileName =null;
			int countAfterExposure =0;
			int responseCode = 0;
			String messageBody ="This is test message";
			String domainName;
			String[] tempName =new  String[2];
			Office365MailActivities objMailMailUser = null ;
			
			
			tempName = 	suiteData.getSaasAppUsername().split("@");
			domainName=tempName[1];
			
			if(attachments!=null){
				for(int i=0;i<=attachments.size()-1;i++){
					sourceFile1 = new File(userDir +attachments.get(i));
					newFileName = myUniqueId+attachments.get(i);
					destFile1 = new File(userDir +newFileName);
					copyFileUsingFileChannels(sourceFile1, destFile1);
					renamedAttachment.add(destFile1.toString());
					filesToDelete.add(destFile1);
				}
			}
			
			
			//creating mail object with user credentials
				objMailMailUser = objMailTestUser1;
				senderEmailId=testUser1;
			
			
			Reporter.log("------------------------------------------------------------------------",true );
			Reporter.log("Performing mail action",true);
			Reporter.log("------------------------------------------------------------------------",true );
			
			cleanupListSent.add(myMailSubject);
			synchronized (this) {
				if(mailAction.equals("SendMail")){
					success = objMailMailUser.sendMailWithCCAndBCC(toEmail, ccEmail, bccEmail, myMailSubject, messageBody, renamedAttachment, true);
				}
				else if(mailAction.equals("SendMailByExternal")){
					success = objMailExternalUser.sendMailWithCCAndBCC(toEmail, ccEmail, bccEmail, myMailSubject, messageBody, renamedAttachment, true);
				}
				else if(mailAction.equals("SaveInDrafts")){
					success = objMailMailUser.addAttachmentAndSaveInDraft(myMailSubject, messageBody, renamedAttachment);
				}
				else if(mailAction.equals("DeleteMail")){
					success = objMailMailUser.sendMailWithCCAndBCC(toEmail, ccEmail, bccEmail, myMailSubject, messageBody, renamedAttachment, true);
					assertTrue(success, "Failed "+mailAction+" with subject:"+myMailSubject+".");
					success=false;
					Thread.sleep(CommonConstants.TEN_MINUTES_SLEEP);
					success = objMailMailUser.deleteMailFromFolder(myMailSubject,"SentItems", DeleteMode.MoveToDeletedItems);
				}
				
			}
			assertTrue(success, "Failed "+mailAction+" with subject:"+myMailSubject+".");
			success=false;
			
			ArrayList<String> myMailMessages = new ArrayList<String>();
			Reporter.log("Waiting for 1 minutes for logs to appear.",true);
			
			Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
			myMailMessages=searchLogs("all_messages", null, myUniqueId, suiteData.getUsername(),noOfLogsToWaitFor);
			
			
			
			String searchResponse = searchInvestigateLogs(-60, 60, "Office 365","all_messages", null, myUniqueId, suiteData.getUsername(), suiteData.getCSRFToken(), suiteData.getSessionID(),testUser1);
			ForensicSearchResults fsr = MarshallingUtils.unmarshall(searchResponse, ForensicSearchResults.class);
			
			
			softAssert = new SoftAssert();
			
			for(int i=0;i<=(expectedLogsNew.size()-1);i++){
				expectedLogsNew.set(i, expectedLogsNew.get(i).replace("<mailsubject>", myMailSubject));
				expectedLogsNew.set(i, expectedLogsNew.get(i).replace("<attachmentName>", myUniqueId+"simpleTextFile.txt"));
			}
//				displayMessageContent(myMailMessages);
			assertTrue(compareResult(expectedLogsNew, myMailMessages),"Test failed, expected log is not found.");
			
			
			if(expectedFieldsForSentNew!=null){
				if(expectedFieldsForSentNew.get("Object_type").equals("Email_File_Attachment")){
					expectedFieldsForSentNew.put("name",  myUniqueId+"simpleTextFile.txt");
				}
				else{
					expectedFieldsForSentNew.put("name", myMailSubject);
				}

				expectedFieldsForSentNew.put("message", expectedFieldsForSentNew.get("message").toString().replace("<mailsubject>", myMailSubject).replace("<attachmentName>", myUniqueId+"simpleTextFile.txt"));
				expectedFieldsForSentNew.put("user", senderEmailId);
				expectedFieldsForSentNew.put("subject", myMailSubject);
				expectedFieldsForSentNew.put("domain", domainName);
				expectedFieldsForSentNew.put("source", "API");
				expectedFieldsForSentNew.put("facility", "Office 365");
				expectedFieldsForSentNew.put("severity", "informational");
				expectedFieldsForSentNew.put("activity_count", "NOT_EMPTY");
				expectedFieldsForSentNew.put("inserted_timestamp", "NOT_EMPTY");
				expectedFieldsForSentNew.put("created_timestamp", "NOT_EMPTY");
				expectedFieldsForSentNew.put("mail_group_id", "NOT_EMPTY");
				expectedFieldsForSentNew.put("latency", "NOT_EMPTY");
				expectedFieldsForSentNew.put("Resource_Id", "NOT_EMPTY");
				softAssert.assertTrue(checkFieldInResponse(searchResponse, "$.hits.hits[*].source", expectedFieldsForSentNew), "Expected field combination not found in Sent activity log.");
			}
			
			if(expectedFieldsForReceivedNew!=null){
				
				if(expectedFieldsForReceivedNew.get("Object_type").equals("Email_File_Attachment")){
					expectedFieldsForReceivedNew.put("name",  myUniqueId+"simpleTextFile.txt");
				}
				else{
					expectedFieldsForReceivedNew.put("name", myMailSubject);
				}
				//common fields
				expectedFieldsForReceivedNew.put("subject", myMailSubject);
				expectedFieldsForReceivedNew.put("domain", domainName);
				expectedFieldsForReceivedNew.put("source", "API");
				expectedFieldsForReceivedNew.put("facility", "Office 365");
				expectedFieldsForReceivedNew.put("severity", "informational");
				expectedFieldsForReceivedNew.put("message", expectedFieldsForReceivedNew.get("message").toString().replace("<fromUser>", senderEmailId).toString().replace("<mailsubject>", myMailSubject).replace("<attachmentName>", myUniqueId+"simpleTextFile.txt"));
				
				expectedFieldsForReceivedNew.put("activity_count", "NOT_EMPTY");
				expectedFieldsForReceivedNew.put("inserted_timestamp", "NOT_EMPTY");
				expectedFieldsForReceivedNew.put("created_timestamp", "NOT_EMPTY");
				expectedFieldsForReceivedNew.put("mail_group_id", "NOT_EMPTY");
				expectedFieldsForReceivedNew.put("latency", "NOT_EMPTY");
				expectedFieldsForReceivedNew.put("Resource_Id", "NOT_EMPTY");
				
				
				softAssert.assertTrue(checkFieldInResponse(searchResponse, "$.hits.hits[*].source", expectedFieldsForReceivedNew), "Expected field combination not found in Received activity log.");
			}
			softAssert.assertAll();
			
		}
		
		/**
		 * @param mailAction
		 * @param noOfLogsToWaitFor
		 * @param testCaseName
		 * @param testCaseDescription
		 * @param toEmail
		 * @param ccEmail
		 * @param bccEmail
		 * @param attachments
		 * @param expectedLogsNew
		 * @param expectedFieldsForSentNew
		 * @param expectedFieldsForReceivedNew
		 * @throws Exception
		 */
		@SuppressWarnings("null")
		@Test(groups={"MAIL3","P2"},dataProvider = "dataProviderMailFieldVerification")
		public void verifyMailFieldsSysAdmin(
				String mailAction,
				int noOfLogsToWaitFor,
				String testCaseName,
				String testCaseDescription,
				EmailAddressCollection toEmail, 	
				EmailAddressCollection ccEmail, 	
				EmailAddressCollection bccEmail, 		
				ArrayList<String> attachments, 	   	 
				ArrayList<String> expectedLogs,   
				Map<String,Object> expectedFieldsForSent,
				Map<String,Object> expectedFieldsForReceived
				) throws Exception {
			
			Reporter.log("****************Test Case Description****************",true);
			Reporter.log("Test Case Name:"+testCaseName,true);
			Reporter.log("Test Case Description:"+testCaseDescription,true);
			Reporter.log("*****************************************************",true);
			
			
			ArrayList<String> expectedLogsNew = expectedLogs;
			Map<String, Object>  expectedFieldsForSentNew=expectedFieldsForSent;
			Map<String, Object> expectedFieldsForReceivedNew = expectedFieldsForReceived;
			microsoft.exchange.webservices.data.core.service.item.Item emailObj = null;
			String myUniqueId = UUID.randomUUID().toString();
			ArrayList<String> renamedAttachment = new ArrayList<String>() ;
			String newFileName = null;
			File sourceFile1 =null;
			File destFile1 = null;
			String myMailSubject =myUniqueId+testCaseName;
			String mailBody = null, senderEmailId="";
			boolean success =false;
			String docType = null;
			String searchFileName =null;
			int countAfterExposure =0;
			int responseCode = 0;
			String messageBody ="This is test message";
			String domainName;
			String[] tempName =new  String[2];
			Office365MailActivities objMailMailUser = null ;
			
			
			tempName = 	suiteData.getSaasAppUsername().split("@");
			domainName=tempName[1];
			
			if(attachments!=null){
				for(int i=0;i<=attachments.size()-1;i++){
					sourceFile1 = new File(userDir +attachments.get(i));
					newFileName = myUniqueId+attachments.get(i);
					destFile1 = new File(userDir +newFileName);
					copyFileUsingFileChannels(sourceFile1, destFile1);
					renamedAttachment.add(destFile1.toString());
					filesToDelete.add(destFile1);
				}
			}
			
			
			//creating mail object with user credentials
				objMailMailUser = objMailSysAdmin;
				senderEmailId=sysAdminUserName;
			
			Reporter.log("------------------------------------------------------------------------",true );
			Reporter.log("Performing mail action",true);
			Reporter.log("------------------------------------------------------------------------",true );
			
			cleanupListSent.add(myMailSubject);
			synchronized (this) {
				if(mailAction.equals("SendMail")){
					success = objMailMailUser.sendMailWithCCAndBCC(toEmail, ccEmail, bccEmail, myMailSubject, messageBody, renamedAttachment, true);
				}
				else if(mailAction.equals("SendMailByExternal")){
					success = objMailExternalUser.sendMailWithCCAndBCC(toEmail, ccEmail, bccEmail, myMailSubject, messageBody, renamedAttachment, true);
				}
				else if(mailAction.equals("SaveInDrafts")){
					success = objMailMailUser.addAttachmentAndSaveInDraft(myMailSubject, messageBody, renamedAttachment);
				}
				else if(mailAction.equals("DeleteMail")){
					success = objMailMailUser.sendMailWithCCAndBCC(toEmail, ccEmail, bccEmail, myMailSubject, messageBody, renamedAttachment, true);
					assertTrue(success, "Failed "+mailAction+" with subject:"+myMailSubject+".");
					success=false;
					Thread.sleep(CommonConstants.TEN_MINUTES_SLEEP);
					success = objMailMailUser.deleteMailFromFolder(myMailSubject,"SentItems", DeleteMode.MoveToDeletedItems);
				}
				
			}
			assertTrue(success, "Failed "+mailAction+" with subject:"+myMailSubject+".");
			success=false;
			
			ArrayList<String> myMailMessages = new ArrayList<String>();
			Reporter.log("Waiting for 1 minutes for logs to appear.",true);
			
			Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
			myMailMessages=searchLogs("all_messages", null, myUniqueId, suiteData.getUsername(),noOfLogsToWaitFor);
			
			
			
			String searchResponse = searchInvestigateLogs(-60, 60, "Office 365","all_messages", null, myUniqueId, suiteData.getUsername(), suiteData.getCSRFToken(), suiteData.getSessionID(),testUser1);
			ForensicSearchResults fsr = MarshallingUtils.unmarshall(searchResponse, ForensicSearchResults.class);
			
			
			softAssert = new SoftAssert();
			
			for(int i=0;i<=(expectedLogsNew.size()-1);i++){
				expectedLogsNew.set(i, expectedLogsNew.get(i).replace("<mailsubject>", myMailSubject));
				expectedLogsNew.set(i, expectedLogsNew.get(i).replace("<attachmentName>", myUniqueId+"simpleTextFile.txt"));
			}
//				displayMessageContent(myMailMessages);
			assertTrue(compareResult(expectedLogsNew, myMailMessages),"Test failed, expected log is not found.");
			
			if(expectedFieldsForSentNew!=null){
			if(expectedFieldsForSentNew.get("Object_type").equals("Email_File_Attachment")){
				expectedFieldsForSentNew.put("name",  myUniqueId+"simpleTextFile.txt");
			}
			else{
				expectedFieldsForSentNew.put("name", myMailSubject);
			}
			
			
				expectedFieldsForSentNew.put("message", expectedFieldsForSentNew.get("message").toString().replace("<mailsubject>", myMailSubject).replace("<attachmentName>", myUniqueId+"simpleTextFile.txt"));
				expectedFieldsForSentNew.put("user", senderEmailId);
				expectedFieldsForSentNew.put("subject", myMailSubject);
				expectedFieldsForSentNew.put("domain", domainName);
				expectedFieldsForSentNew.put("source", "API");
				expectedFieldsForSentNew.put("facility", "Office 365");
				expectedFieldsForSentNew.put("severity", "informational");
				expectedFieldsForSentNew.put("activity_count", "NOT_EMPTY");
				expectedFieldsForSentNew.put("inserted_timestamp", "NOT_EMPTY");
				expectedFieldsForSentNew.put("created_timestamp", "NOT_EMPTY");
				expectedFieldsForSentNew.put("mail_group_id", "NOT_EMPTY");
				expectedFieldsForSentNew.put("latency", "NOT_EMPTY");
				expectedFieldsForSentNew.put("Resource_Id", "NOT_EMPTY");
				softAssert.assertTrue(checkFieldInResponse(searchResponse, "$.hits.hits[*].source", expectedFieldsForSentNew), "Expected field combination not found in Sent activity log.");
			}
			if(expectedFieldsForReceivedNew!=null){
				
				if(expectedFieldsForReceivedNew.get("Object_type").equals("Email_File_Attachment")){
					expectedFieldsForReceivedNew.put("name",  myUniqueId+"simpleTextFile.txt");
				}
				else{
					expectedFieldsForReceivedNew.put("name", myMailSubject);
				}
				//common fields
				expectedFieldsForReceivedNew.put("subject", myMailSubject);
				expectedFieldsForReceivedNew.put("domain", domainName);
				expectedFieldsForReceivedNew.put("source", "API");
				expectedFieldsForReceivedNew.put("facility", "Office 365");
				expectedFieldsForReceivedNew.put("severity", "informational");
				expectedFieldsForReceivedNew.put("message", expectedFieldsForReceivedNew.get("message").toString().replace("<fromUser>", senderEmailId).toString().replace("<mailsubject>", myMailSubject).replace("<attachmentName>", myUniqueId+"simpleTextFile.txt"));
				
				expectedFieldsForReceivedNew.put("activity_count", "NOT_EMPTY");
				expectedFieldsForReceivedNew.put("inserted_timestamp", "NOT_EMPTY");
				expectedFieldsForReceivedNew.put("created_timestamp", "NOT_EMPTY");
				expectedFieldsForReceivedNew.put("mail_group_id", "NOT_EMPTY");
				expectedFieldsForReceivedNew.put("latency", "NOT_EMPTY");
				expectedFieldsForReceivedNew.put("Resource_Id", "NOT_EMPTY");
				
				
				softAssert.assertTrue(checkFieldInResponse(searchResponse, "$.hits.hits[*].source", expectedFieldsForReceivedNew), "Expected field combination not found in Received activity log.");
			}
			softAssert.assertAll();
			
		}
		
		@Test(groups={"FILTERS", "BOX", "REGRESSION", "P1", "ANONYMIZATION"})
		public void verifyAnonymizedLogs() throws Exception {
			String[] steps = { "1. Turn on the anonymization and get the investigate logs.",
			           "2. Verify the username in message is anonymized.",
			           "3. Turn off the anonymizarion." };
			LogUtils.logTestDescription(steps);
			
			String tenantAcctId = getTenantAccountId();
			String payload = "{\"userAnonymization\":true,\"dpoName\": \"\", \"dpoPassword\": \"\",\"id\":\""+tenantAcctId+"\"}";
			Reporter.log("Payload:"+ payload, true);
			
			String responseBody = this.updateUserAnonymization(payload);
			Reporter.log(payload,true);
			
			//Invalidate the session after anonymization turned
			Reporter.log("Regenerate the session after anonymization turned on...:", true);
			this.regenerateSession();
			
			ForensicSearchResults logs;
			
			HashMap<String, String> termmap = new HashMap<String, String>();
			termmap.put("facility", "Office 365");
			termmap.put("__source", "API");
			termmap.put("severity", "informational");
			
			for (int retry = 0; retry < 1; retry++) {

				try{
					String apiHost = suiteData.getScheme() + "://" + suiteData.getApiserverHostName();
					//Fetch the activity logs from yesterday to tomorrow and limited to 500
					logs = getInvestigateLogs(-18000, 10, "Office 365"	, termmap, suiteData.getUsername().toLowerCase(), 
							apiHost, suiteData.getCSRFToken(), suiteData.getSessionID(), 0, 100, "investigate");

					this.logValidator = new LogValidator(logs);

					logValidator.verifyMetadata();
					//Add the method and update it
					logValidator.verifyAnonymizedLogs();
					
				}
				catch(Exception e) {}
			}
//			
			//Remove anonymization
			payload = "{\"userAnonymization\":false,\"dpoName\":\""+ suiteData.getDpoUsername() +"\",\"dpoPassword\":\""+ suiteData.getDpoPassword() +"\", \"id\":\""+tenantAcctId+"\"}";
			Reporter.log("Payload:"+ payload, true);
			responseBody = this.updateUserAnonymization(payload);
			Reporter.log("Response body:"+ responseBody, true);
			Reporter.log("Regenerate the session after anonymization turned off...:", true);
			this.regenerateSession();
			for (int retry = 0; retry < 1; retry++) {

				try{
					String apiHost = suiteData.getScheme() + "://" + suiteData.getApiserverHostName();
					//Fetch the activity logs from yesterday to tomorrow and limited to 500
					logs = getInvestigateLogs(-18000, 10,  "Office 365", termmap, suiteData.getUsername().toLowerCase(), 
							apiHost, suiteData.getCSRFToken(), suiteData.getSessionID(), 0, 100, "investigate");

					this.logValidator = new LogValidator(logs);

					logValidator.verifyMetadata();
					//Add the method and update it
					logValidator.verifyUnAnonymizedLogs();
					Assert.assertTrue(true);
				}
				catch(Exception e) {
					Reporter.log(e.getMessage(),true);
					Assert.assertTrue(false);
				}
			}
		}
		
		
		/**
		 * Send an email with content violation in mail and attachment to multiple users including internal and external users, verify the exposure document fields
		 * @param isInternal
		 * @param exposuretype
		 * @throws Exception
		 */
		@Test(groups={"P1"},retryAnalyzer=RetryAnalyzer.class)
		public void verifyExposureFields(
				
				) throws Exception {
			
			Reporter.log("****************Test Case Description****************",true);
			Reporter.log("Send an email with content violation in mail and attachment to multiple users including internal and external users. verify the exposure document fields. ",true);
			Reporter.log("*****************************************************",true);
			
			String isInternal="true"; 
			Item emailObj = null;
			String myUniqueId = UUID.randomUUID().toString();
			ArrayList<String> myAttachments = new ArrayList<String>() ;
			String fileName1 = "PII.rtf";
			String fileName2 = "PCI_Test.txt";
			String searchFileName1 =myUniqueId+fileName1;
			String searchFileName2 =myUniqueId+fileName2;
			File sourceFile1 =new File(userDir+fileName1);
			File sourceFile2 =new File(userDir+fileName2);
			File destnFile1 =new File(userDir+searchFileName1);
			File destnFile2 =new File(userDir+searchFileName2);
			
			String myMailSubject =null;
			String mailBody = readFile(sourceFile2.toString(), Charset.defaultCharset());
			boolean success =false;
			String docTypeForRemediation = null;
			int countAfterExposure =0;
			int responseCode = 0;
			String expectedLog = null;
			EmailAddressCollection to = new EmailAddressCollection();
			EmailAddressCollection cc = new EmailAddressCollection();
			List<NameValuePair> qparamsForMail = new ArrayList<NameValuePair>(); 
			List<NameValuePair> qparamsForFile1 = new ArrayList<NameValuePair>(); 
			List<NameValuePair> qparamsForFile2 = new ArrayList<NameValuePair>(); 
			SecurletDocument mailExpoDoc = new SecurletDocument();
			SecurletDocument exposureDoc1 = new SecurletDocument();
			SecurletDocument exposureDoc2 = new SecurletDocument();
			SecurletDocument usersDoc1 = new SecurletDocument();
			SecurletDocument usersDoc2 = new SecurletDocument();
			SecurletDocument usersDoc3 = new SecurletDocument();
			String remedialAction ="ITEM_DELETE_MAIL";
			//Prepare the violations
			String docId=null;
			String myResponse;
			String domainName=suiteData.getSaasAppUsername().split("@")[1];
			
			SecurletsCommonUtils securletsUtils=new SecurletsCommonUtils(); 
			
			
			SoftAssert softAssert = new SoftAssert();
			
			Reporter.log("------------------------------------------------------------------------",true );
			Reporter.log("Sending mail to external user",true);
			Reporter.log("------------------------------------------------------------------------",true );
			
			myMailSubject = myUniqueId+"MultipleAttachments";
			myAttachments.add(destnFile1.toString());
			myAttachments.add(destnFile2.toString());
			
			
			//copying file for external user - office 365
			copyFileUsingFileChannels(sourceFile1, destnFile1);
			copyFileUsingFileChannels(sourceFile2, destnFile2);
			filesToDelete.add(destnFile1);
			filesToDelete.add(destnFile2);
			
			
			qparamsForMail.add(new BasicNameValuePair(SecurletsConstants.IS_INTERNAL,  isInternal));
			qparamsForFile1.add(new BasicNameValuePair(SecurletsConstants.IS_INTERNAL,  isInternal));
			qparamsForFile2.add(new BasicNameValuePair(SecurletsConstants.IS_INTERNAL,  isInternal));
			qparamsForMail.add(new BasicNameValuePair("name",  myMailSubject));
			qparamsForFile1.add(new BasicNameValuePair("name",  searchFileName1));
			qparamsForFile2.add(new BasicNameValuePair("name",  searchFileName2));
			
			
			synchronized(this){ 
				if(isInternal.equals("true")){
					//sending mail to multipel users
					to.add(externalUser1);
					to.add(testUser2);
					cc.add(adminMailId);
					cc.add(externalUser2);
					
					success= objMailTestUser1.sendMailWithCCAndBCC(to, cc, null, myMailSubject,mailBody,myAttachments, true);
				}
			}
			
			cleanupListSent.add(myMailSubject);
			assertTrue(success, "Failed sending mail with subject:"+myMailSubject+".");
			success=false;
			
			/****Exposure & collaborator check mail*****/
			
			Reporter.log("------------------------------------------------------------------------",true );
			
			int i = 60000;
			for (; i <= (MAX_EXPOSURE_WAIT_TIME_IN_MINUTES * 60 * 1000); i+=60000 ) {
				Reporter.log("Checking exposure after :"+TimeUnit.MILLISECONDS.toMinutes(i)+" minute(s)",true );
				Reporter.log("------------------------------------------------------------------------",true );
				Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
				
				
				//Get the exposure count
				mailExpoDoc = this.getExposedDocuments(elapp.el_office_365.name(), qparamsForMail);
				countAfterExposure = mailExpoDoc.getMeta().getTotalCount();
				Reporter.log("Exposure Count ="+countAfterExposure, true);
				
				if (countAfterExposure >= 1) {break;}
			}
			assertEquals(countAfterExposure,1,"File:"+myMailSubject +" not exposed even after waiting for "+TimeUnit.MILLISECONDS.toMinutes(i-60000)+" minutes.");
			//Verifying the users in the exposure
			
			docValidator.validateExposedDocuments(mailExpoDoc, true);
			softAssert.assertNotNull(mailExpoDoc.getObjects().get(0).getCreatedBy(), "Null value found for the Field: created_by.");
			softAssert.assertEquals(mailExpoDoc.getObjects().get(0).getDocType(),"Email_Message", "Value doesn't match for the Field: doc_type.");
			softAssert.assertEquals(mailExpoDoc.getObjects().get(0).getFormat(),"emailbody", "Value doesn't match for the Field: format.");
			softAssert.assertEquals(mailExpoDoc.getObjects().get(0).getObjectType(),"Mail", "Value doesn't match for the Field: object_type.");
			softAssert.assertEquals(mailExpoDoc.getObjects().get(0).getOwnedBy(),testUser1, "Value doesn't match for the Field: owned_by.");
			softAssert.assertTrue(mailExpoDoc.getObjects().get(0).getSize().intValue()>=-1, "Size field value is not >= -1");
			softAssert.assertEquals(mailExpoDoc.getObjects().get(0).getExposures().getAllInternal().booleanValue(),false, "Value doesn't match for the Field: exposures->all_internal.");
			softAssert.assertEquals(mailExpoDoc.getObjects().get(0).getExposures().getExtCount().intValue(),2, "Value doesn't match for the Field: exposures->ext_count.");
			softAssert.assertEquals(mailExpoDoc.getObjects().get(0).getExposures().getIntCount().intValue(),2, "Value doesn't match for the Field: exposures->int_count.");
			softAssert.assertEquals(mailExpoDoc.getObjects().get(0).getExposures().getPublic().booleanValue(),false, "Value doesn't match for the Field: exposures->public.");
			softAssert.assertTrue(mailExpoDoc.getObjects().get(0).getExposures().getExternal().toString().contains(externalUser1),"User not found in exposure external user list: "+externalUser1);
			softAssert.assertTrue(mailExpoDoc.getObjects().get(0).getExposures().getExternal().toString().contains(externalUser2),"User not found in exposure external user list: "+externalUser2);
			softAssert.assertTrue(mailExpoDoc.getObjects().get(0).getExposures().getInternal().toString().contains(adminMailId),"User not found in exposure internal user list: "+adminMailId);
			softAssert.assertTrue(mailExpoDoc.getObjects().get(0).getExposures().getInternal().toString().contains(testUser2),"User not found in exposure internal user list: "+testUser2);
			softAssert.assertAll();
			
			
			
			/****Exposure & collaborator check file1*****/
			
			for (; i <= (MAX_EXPOSURE_WAIT_TIME_IN_MINUTES * 60 * 1000); i+=60000 ) {
				Reporter.log("Checking exposure after :"+TimeUnit.MILLISECONDS.toMinutes(i)+" minute(s)",true );
				Reporter.log("------------------------------------------------------------------------",true );
				Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
				
				
				//Get the exposure count
				exposureDoc1 = this.getExposedDocuments(elapp.el_office_365.name(), qparamsForFile1);
				countAfterExposure = exposureDoc1.getMeta().getTotalCount();
				Reporter.log("Exposure Count ="+countAfterExposure, true);
				
				if (countAfterExposure >= 1) {break;}
			}
			assertEquals(countAfterExposure,1,"File:"+searchFileName1 +" not exposed even after waiting for "+TimeUnit.MILLISECONDS.toMinutes(i-60000)+" minutes.");
			//Verifying the users in the exposure
			docValidator.validateExposedDocuments(exposureDoc1, true);
			softAssert.assertNotNull(exposureDoc1.getObjects().get(0).getCreatedBy(), "Null value found for the Field: created_by.");
			softAssert.assertEquals(exposureDoc1.getObjects().get(0).getDocType(),"Email_File_Attachment", "Value doesn't match for the Field: doc_type.");
			softAssert.assertEquals(exposureDoc1.getObjects().get(0).getFormat(),"rtf", "Value doesn't match for the Field: format.");
			softAssert.assertEquals(exposureDoc1.getObjects().get(0).getObjectType(),"Mail", "Value doesn't match for the Field: object_type.");
			softAssert.assertEquals(exposureDoc1.getObjects().get(0).getOwnedBy(),testUser1, "Value doesn't match for the Field: owned_by.");
			softAssert.assertTrue(exposureDoc1.getObjects().get(0).getSize().intValue()>=-1, "Size field value is not >= -1");
			softAssert.assertEquals(exposureDoc1.getObjects().get(0).getExposures().getAllInternal().booleanValue(),false, "Value doesn't match for the Field: exposures->all_internal.");
			softAssert.assertEquals(exposureDoc1.getObjects().get(0).getExposures().getExtCount().intValue(),2, "Value doesn't match for the Field: exposures->ext_count.");
			softAssert.assertEquals(exposureDoc1.getObjects().get(0).getExposures().getIntCount().intValue(),2, "Value doesn't match for the Field: exposures->int_count.");
			softAssert.assertEquals(exposureDoc1.getObjects().get(0).getExposures().getPublic().booleanValue(),false, "Value doesn't match for the Field: exposures->public.");
			softAssert.assertTrue(exposureDoc1.getObjects().get(0).getExposures().getExternal().toString().contains(externalUser1),"User not found in exposure external user list: "+externalUser1);
			softAssert.assertTrue(exposureDoc1.getObjects().get(0).getExposures().getExternal().toString().contains(externalUser2),"User not found in exposure external user list: "+externalUser2);
			softAssert.assertTrue(exposureDoc1.getObjects().get(0).getExposures().getInternal().toString().contains(adminMailId),"User not found in exposure internal user list: "+adminMailId);
			softAssert.assertTrue(exposureDoc1.getObjects().get(0).getExposures().getInternal().toString().contains(testUser2),"User not found in exposure internal user list: "+testUser2);
			softAssert.assertAll();
			
			
			
			
		}
		/**
		 * Send an email with content violation in mail and attachment to multiple internal users, verify the risky document fields
		 * @param isInternal
		 * @param exposuretype
		 * @throws Exception
		 */
		@Test(groups={"P1"},retryAnalyzer=RetryAnalyzer.class)
		public void verifyRiskyDocsFields(
				
				) throws Exception {

			Reporter.log("****************Test Case Description****************",true);
			Reporter.log("Send an email with content violation in mail and attachment to multiple internal users, verify the risky document fields. ",true);
			Reporter.log("*****************************************************",true);

			String isInternal="true"; 
			Item emailObj = null;
			String myUniqueId = UUID.randomUUID().toString();
			ArrayList<String> myAttachments = new ArrayList<String>() ;
			String fileName1 = "PII.rtf";
			String fileName2 = "PCI_Test.txt";
			String searchFileName1 =myUniqueId+fileName1;
			String searchFileName2 =myUniqueId+fileName2;
			File sourceFile1 =new File(userDir+fileName1);
			File sourceFile2 =new File(userDir+fileName2);
			File destnFile1 =new File(userDir+searchFileName1);
			File destnFile2 =new File(userDir+searchFileName2);
			
			String myMailSubject =null;
			String mailBody = readFile(sourceFile2.toString(), Charset.defaultCharset());
			boolean success =false;
			String docTypeForRemediation = null;
			int countAfterExposure =0;
			int responseCode = 0;
			String expectedLog = null;
			EmailAddressCollection to = new EmailAddressCollection();
			EmailAddressCollection cc = new EmailAddressCollection();
			List<NameValuePair> qparamsForMail = new ArrayList<NameValuePair>(); 
			List<NameValuePair> qparamsForFile1 = new ArrayList<NameValuePair>(); 
			List<NameValuePair> qparamsForFile2 = new ArrayList<NameValuePair>(); 
			SecurletDocument mailExpoDoc = new SecurletDocument();
			SecurletDocument exposureDoc1 = new SecurletDocument();
			SecurletDocument exposureDoc2 = new SecurletDocument();
			SecurletDocument usersDoc1 = new SecurletDocument();
			SecurletDocument usersDoc2 = new SecurletDocument();
			SecurletDocument usersDoc3 = new SecurletDocument();
			String remedialAction ="ITEM_DELETE_MAIL";
			//Prepare the violations
			String docId=null;
			String myResponse;
			String domainName=suiteData.getSaasAppUsername().split("@")[1];
			
			   SecurletsCommonUtils securletsUtils=new SecurletsCommonUtils(); 

			
			SoftAssert softAssert = new SoftAssert();
			
			Reporter.log("------------------------------------------------------------------------",true );
			Reporter.log("Sending mail to internal user",true);
			Reporter.log("------------------------------------------------------------------------",true );
			
			myMailSubject = myUniqueId+"MultipleAttachments";
			myAttachments.add(destnFile1.toString());
			myAttachments.add(destnFile2.toString());
			
			
			//copying file for external user - office 365
			copyFileUsingFileChannels(sourceFile1, destnFile1);
			copyFileUsingFileChannels(sourceFile2, destnFile2);
			filesToDelete.add(destnFile1);
			filesToDelete.add(destnFile2);
			

			qparamsForMail.add(new BasicNameValuePair(SecurletsConstants.IS_INTERNAL,  isInternal));
			qparamsForFile1.add(new BasicNameValuePair(SecurletsConstants.IS_INTERNAL,  isInternal));
			qparamsForFile2.add(new BasicNameValuePair(SecurletsConstants.IS_INTERNAL,  isInternal));
			qparamsForMail.add(new BasicNameValuePair("name",  myMailSubject));
			qparamsForFile1.add(new BasicNameValuePair("name",  searchFileName1));
			qparamsForFile2.add(new BasicNameValuePair("name",  searchFileName2));

			
			synchronized(this){ 
				if(isInternal.equals("true")){
					//sending mail to multipel users
					to.add(testUser2);
					cc.add(adminMailId);
					success= objMailTestUser1.sendMailWithCCAndBCC(to, cc, null, myMailSubject,mailBody,myAttachments, true);
				}
			}

			cleanupListSent.add(myMailSubject);
			assertTrue(success, "Failed sending mail with subject:"+myMailSubject+".");
			success=false;
			
			/****Exposure & collaborator check mail*****/

			Reporter.log("------------------------------------------------------------------------",true );
			
			int i = 60000;
			for (; i <= (MAX_EXPOSURE_WAIT_TIME_IN_MINUTES * 60 * 1000); i+=60000 ) {
				Reporter.log("Checking exposure after :"+TimeUnit.MILLISECONDS.toMinutes(i)+" minute(s)",true );
				Reporter.log("------------------------------------------------------------------------",true );
				Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
				
				
				//Get the exposure count
				mailExpoDoc = this.getRiskyDocuments(elapp.el_office_365.name(), qparamsForMail);
				countAfterExposure = mailExpoDoc.getMeta().getTotalCount();
				Reporter.log("Exposure Count ="+countAfterExposure, true);
				
				if (countAfterExposure >= 1) {break;}
			}
			assertEquals(countAfterExposure,1,"File:"+myMailSubject +" not appearing in risky docs even after waiting for "+TimeUnit.MILLISECONDS.toMinutes(i-60000)+" minutes.");
			//Verifying the users in the exposure
			
			docValidator.validateExposedDocuments(mailExpoDoc, true);
			softAssert.assertNotNull(mailExpoDoc.getObjects().get(0).getCreatedBy(), "Null value found for the Field: created_by.");
			softAssert.assertEquals(mailExpoDoc.getObjects().get(0).getDocType(),"Email_Message", "Value doesn't match for the Field: doc_type.");
			softAssert.assertEquals(mailExpoDoc.getObjects().get(0).getFormat(),"emailbody", "Value doesn't match for the Field: format.");
			softAssert.assertEquals(mailExpoDoc.getObjects().get(0).getObjectType(),"Mail", "Value doesn't match for the Field: object_type.");
			softAssert.assertEquals(mailExpoDoc.getObjects().get(0).getOwnedBy(), testUser1, "Value doesn't match for the Field: owned_by.");
			softAssert.assertTrue(mailExpoDoc.getObjects().get(0).getSize().intValue()>=-1, "Size field value is not >= -1");
			softAssert.assertEquals(mailExpoDoc.getObjects().get(0).getExposures().getAllInternal().booleanValue(),false, "Value doesn't match for the Field: exposures->all_internal.");
			softAssert.assertEquals(mailExpoDoc.getObjects().get(0).getExposures().getExtCount().intValue(),0, "Value doesn't match for the Field: exposures->ext_count.");
			softAssert.assertEquals(mailExpoDoc.getObjects().get(0).getExposures().getIntCount().intValue(),2, "Value doesn't match for the Field: exposures->int_count.");
			softAssert.assertEquals(mailExpoDoc.getObjects().get(0).getExposures().getPublic().booleanValue(),false, "Value doesn't match for the Field: exposures->public.");
			softAssert.assertTrue(mailExpoDoc.getObjects().get(0).getExposures().getInternal().toString().contains(adminMailId),"User not found in exposure internal user list: "+adminMailId);
			softAssert.assertTrue(mailExpoDoc.getObjects().get(0).getExposures().getInternal().toString().contains(testUser2),"User not found in exposure internal user list: "+testUser2);
			softAssert.assertAll();
			
			
			
			/****Exposure & collaborator check file1*****/
			
			for (; i <= (MAX_EXPOSURE_WAIT_TIME_IN_MINUTES * 60 * 1000); i+=60000 ) {
				Reporter.log("Checking exposure after :"+TimeUnit.MILLISECONDS.toMinutes(i)+" minute(s)",true );
				Reporter.log("------------------------------------------------------------------------",true );
				Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
				
				
				//Get the exposure count
				exposureDoc1 = this.getRiskyDocuments(elapp.el_office_365.name(), qparamsForFile1);
				countAfterExposure = exposureDoc1.getMeta().getTotalCount();
				Reporter.log("Exposure Count ="+countAfterExposure, true);
				
				if (countAfterExposure >= 1) {break;}
			}
			assertEquals(countAfterExposure,1,"File:"+searchFileName1 +" not appearing in risky docs even after waiting for "+TimeUnit.MILLISECONDS.toMinutes(i-60000)+" minutes.");
			//Verifying the users in the exposure
			docValidator.validateExposedDocuments(exposureDoc1, true);
			softAssert.assertNotNull(exposureDoc1.getObjects().get(0).getCreatedBy(), "Null value found for the Field: created_by.");
			softAssert.assertEquals(exposureDoc1.getObjects().get(0).getDocType(),"Email_File_Attachment", "Value doesn't match for the Field: doc_type.");
			softAssert.assertEquals(exposureDoc1.getObjects().get(0).getFormat(),"rtf", "Value doesn't match for the Field: format.");
			softAssert.assertEquals(exposureDoc1.getObjects().get(0).getObjectType(),"Mail", "Value doesn't match for the Field: object_type.");
			softAssert.assertEquals(exposureDoc1.getObjects().get(0).getOwnedBy(),testUser1, "Value doesn't match for the Field: owned_by.");
			softAssert.assertTrue(exposureDoc1.getObjects().get(0).getSize().intValue()>=-1, "Size field value is not >= -1");
			softAssert.assertEquals(exposureDoc1.getObjects().get(0).getExposures().getAllInternal().booleanValue(),false, "Value doesn't match for the Field: exposures->all_internal.");
			softAssert.assertEquals(exposureDoc1.getObjects().get(0).getExposures().getExtCount().intValue(),0, "Value doesn't match for the Field: exposures->ext_count.");
			softAssert.assertEquals(exposureDoc1.getObjects().get(0).getExposures().getIntCount().intValue(),2, "Value doesn't match for the Field: exposures->int_count.");
			softAssert.assertEquals(exposureDoc1.getObjects().get(0).getExposures().getPublic().booleanValue(),false, "Value doesn't match for the Field: exposures->public.");
			softAssert.assertTrue(exposureDoc1.getObjects().get(0).getExposures().getInternal().toString().contains(adminMailId),"User not found in exposure internal user list: "+adminMailId);
			softAssert.assertTrue(exposureDoc1.getObjects().get(0).getExposures().getInternal().toString().contains(testUser2),"User not found in exposure internal user list: "+testUser2);
			softAssert.assertAll();
			
		}
		
		/**
		 * Admin user sends content violation in mailbody and multiple attachments to mailgroup of internal user. 
		 * Verify the riskydocs and delete email from each user inbox and verify the risky docs.
		 * @param isInternal
		 * @param exposuretype
		 * @throws Exception
		 */
		@Test(groups={"P1"},retryAnalyzer=RetryAnalyzer.class)
		public void verifyRiskyDocsWhenMultipleAttachmentMailDeletedGroupMail(
				) throws Exception {
			
			Reporter.log("****************Test Case Description****************",true);
			Reporter.log("Admin user sends content violation in mailbody and multiple attachments to group mail id of internal user. Verify the riskydocs and delete email from each user inbox and verify the risky docs. ",true);
			Reporter.log("1)Two internal users who are part of mailgroup receive a Mail with risk in body and mulitple attachments from admin user",true);
			Reporter.log("2)Verify that the mail and attachment are appearing in risky docs",true);
			Reporter.log("3)Delete mail from inbox of user1.",true);
			Reporter.log("4)Verify that the mail and attachment remain in risky docs but user1 is removed from risky docs details",true);
			Reporter.log("5)Delete mail from inbox of user2.",true);
			Reporter.log("6)Verify that the mail and attachment are removed from risky docs",true);
			Reporter.log("*****************************************************",true);
			
			
			String isInternal = "true";
			Item emailObj = null;
			String myUniqueId = UUID.randomUUID().toString();
			ArrayList<String> myAttachments = new ArrayList<String>() ;
			String fileName1 = "PII.rtf";
			String fileName2 = "PCI_Test.txt";
			String searchFileName1 =myUniqueId+fileName1;
			String searchFileName2 =myUniqueId+fileName2;
			File sourceFile1 =new File(userDir+fileName1);
			File sourceFile2 =new File(userDir+fileName2);
			File destnFile1 =new File(userDir+searchFileName1);
			File destnFile2 =new File(userDir+searchFileName2);
			
			String myMailSubject =null;
			String mailBody= readFile(sourceFile2.toString(), Charset.defaultCharset());
			boolean success =false;
			String docTypeForRemediation = null;
			int countAfterExposure =0;
			int responseCode = 0;
			String expectedLog = null;
			EmailAddressCollection to = new EmailAddressCollection();
			List<NameValuePair> qparamsForMail = new ArrayList<NameValuePair>(); 
			List<NameValuePair> qparamsForFile1 = new ArrayList<NameValuePair>(); 
			List<NameValuePair> qparamsForFile2 = new ArrayList<NameValuePair>(); 
			SecurletDocument mailExpoDoc = new SecurletDocument();
			SecurletDocument exposureDoc1 = new SecurletDocument();
			SecurletDocument exposureDoc2 = new SecurletDocument();
			SecurletDocument usersDoc1 = new SecurletDocument();
			SecurletDocument usersDoc2 = new SecurletDocument();
			SecurletDocument usersDoc3 = new SecurletDocument();
			String myResponse;
			String remedialAction ="ITEM_DELETE_MAIL";
			//Prepare the violations
			String docId=null;
			
			Reporter.log("------------------------------------------------------------------------",true );
			Reporter.log("External user sends mail to internal user",true);
			Reporter.log("------------------------------------------------------------------------",true );
			
			myMailSubject = myUniqueId+"MultipleAttachments";
			myAttachments.add(destnFile1.toString());
			myAttachments.add(destnFile2.toString());
			
			//copying file for external user - office 365
			copyFileUsingFileChannels(sourceFile1, destnFile1);
			copyFileUsingFileChannels(sourceFile2, destnFile2);
			filesToDelete.add(destnFile1);
			filesToDelete.add(destnFile2);
			
			
			qparamsForMail.add(new BasicNameValuePair(SecurletsConstants.IS_INTERNAL,  isInternal));
			qparamsForFile1.add(new BasicNameValuePair(SecurletsConstants.IS_INTERNAL,  isInternal));
			qparamsForFile2.add(new BasicNameValuePair(SecurletsConstants.IS_INTERNAL,  isInternal));
			qparamsForMail.add(new BasicNameValuePair("name",  myMailSubject));
			qparamsForFile1.add(new BasicNameValuePair("name",  searchFileName1));
			qparamsForFile2.add(new BasicNameValuePair("name",  searchFileName2));
			
			EmailAddressCollection recipientsCc = new EmailAddressCollection();
			EmailAddressCollection recipientsTo = new EmailAddressCollection();
			
			recipientsTo.add(groupMailId);
			
			synchronized(this){ 
					success  = objMailSysAdmin.sendMailWithCCAndBCC(recipientsTo, null, null, myMailSubject, mailBody,myAttachments,true);
			}
			
			cleanupListSent.add(myMailSubject);
			assertTrue(success, "Failed sending mail with subject:"+myMailSubject+".");
			success=false;
			
			Thread.sleep(CommonConstants.THREE_MINUTES_SLEEP);
			
			Reporter.log("------------------------------------------------------------------------",true );
			
			int i = 240000;
			for (; i <= (MAX_EXPOSURE_WAIT_TIME_IN_MINUTES * 60 * 1000); i+=60000 ) {
				Reporter.log("Checking exposure after :"+TimeUnit.MILLISECONDS.toMinutes(i)+" minute(s)",true );
				Reporter.log("------------------------------------------------------------------------",true );
				Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
				
				
				//Get the exposure count
				mailExpoDoc = this.getRiskyDocuments(elapp.el_office_365.name(), qparamsForMail);
				countAfterExposure = mailExpoDoc.getMeta().getTotalCount();
				Reporter.log("Exposure Count ="+countAfterExposure, true);
				
				if (countAfterExposure >= 1) {
					
					if(mailExpoDoc.getObjects().get(0).getExposures().getInternal().size()>=3){
						break;
					}
					
				}
			}
			assertEquals(countAfterExposure,1,"File:"+myMailSubject +" not appearing in risky docs even after waiting for "+TimeUnit.MILLISECONDS.toMinutes(i-60000)+" minutes.");
			assertTrue(mailExpoDoc.getObjects().get(0).getExposures().getInternal().toString().contains(testUser1),"Group member not found in risky docs internal user list: "+testUser1);
			assertTrue(mailExpoDoc.getObjects().get(0).getExposures().getInternal().toString().contains(testUser2),"Group member not found in risky docs internal user list: "+testUser2);
			 
			
			for (; i <= (MAX_EXPOSURE_WAIT_TIME_IN_MINUTES * 60 * 1000); i+=60000 ) {
				Reporter.log("Checking exposure after :"+TimeUnit.MILLISECONDS.toMinutes(i)+" minute(s)",true );
				Reporter.log("------------------------------------------------------------------------",true );
				Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
				
				
				//Get the exposure count
				exposureDoc1 = this.getRiskyDocuments(elapp.el_office_365.name(), qparamsForFile1);
				countAfterExposure = exposureDoc1.getMeta().getTotalCount();
				Reporter.log("Exposure Count ="+countAfterExposure, true);
				
				if (countAfterExposure >= 1) {break;}
			}
			assertEquals(countAfterExposure,1,"File:"+searchFileName1 +" not appearing in risky docs even after waiting for "+TimeUnit.MILLISECONDS.toMinutes(i-60000)+" minutes.");
			assertTrue(exposureDoc1.getObjects().get(0).getExposures().getInternal().toString().contains(testUser1),"Group member not found in risky docs internal user list: "+testUser1);
			assertTrue(exposureDoc1.getObjects().get(0).getExposures().getInternal().toString().contains(testUser2),"Group member not found in risky docs internal user list: "+testUser2);
			Reporter.log("------------------------------------------------------------------------",true );
			
			for (; i <= (MAX_EXPOSURE_WAIT_TIME_IN_MINUTES * 60 * 1000); i+=60000 ) {
				Reporter.log("Checking exposure after :"+TimeUnit.MILLISECONDS.toMinutes(i)+" minute(s)",true );
				Reporter.log("------------------------------------------------------------------------",true );
				Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
				
				
				//Get the exposure count
				exposureDoc2 = this.getRiskyDocuments(elapp.el_office_365.name(), qparamsForFile2);
				countAfterExposure = exposureDoc2.getMeta().getTotalCount();
				Reporter.log("Exposure Count ="+countAfterExposure, true);
				
				if (countAfterExposure >= 1) {break;}
			}
			assertEquals(countAfterExposure,1,"File:"+searchFileName2 +" not appearing in risky docs even after waiting for "+TimeUnit.MILLISECONDS.toMinutes(i-60000)+" minutes.");
			assertTrue(exposureDoc2.getObjects().get(0).getExposures().getInternal().toString().contains(testUser1),"Group member not found in risky docs internal user list: "+testUser1);
			assertTrue(exposureDoc2.getObjects().get(0).getExposures().getInternal().toString().contains(testUser2),"Group member not found in risky docs internal user list: "+testUser2);
			
			
			Reporter.log("----------------Delete the mail from inbox of "+testUser1+" --------------------",true );
			synchronized(this){objMailTestUser1 = new Office365MailActivities(testUser1,testUser1Pwd);}
			assertTrue(objMailTestUser1.deleteMailFromFolder(myMailSubject,"Inbox", DeleteMode.MoveToDeletedItems), "Delete mail from Inbox failed for user:"+testUser1);
			
			
			
			Reporter.log("------------------------------------------------------------------------",true );
			
			int countAfterRemediation = countAfterExposure ;
			i = 60000;
			for (; i <= (MAX_REMEDIATION_WAIT_TIME_IN_MINUTES * 60 * 1000); i+=60000 ) {
				Reporter.log("After "+TimeUnit.MILLISECONDS.toMinutes(i)+" minutes, and check if the file:"+myMailSubject +" is removed from exposure.",true);
				Reporter.log("------------------------------------------------------------------------",true );
				Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
				mailExpoDoc = this.getRiskyDocuments(elapp.el_office_365.name(), qparamsForMail);
				countAfterRemediation = mailExpoDoc.getMeta().getTotalCount();
				
				if (mailExpoDoc.getObjects().get(0).getExposures().getInternal().toString().contains(testUser1) == false) {
					break;
				}
				
			}
			
			assertFalse(mailExpoDoc.getObjects().get(0).getExposures().getInternal().toString().contains(testUser1),"User is found in risky docs internal user list: "+testUser1);
			assertTrue(mailExpoDoc.getObjects().get(0).getExposures().getInternal().toString().contains(testUser2),"User not found in risky docs internal user list: "+testUser2);
//			assertTrue(mailExpoDoc.getObjects().get(0).getExposures().getInternal().toString().contains(groupMailId),"GroupId not found in risky docs internal user list: "+groupMailId);
			
			//Check the user and collaborator list
//			usersDoc1 = getCollaborators(elapp.el_office_365.name(), qparamsForMail);
//			myResponse = MarshallingUtils.marshall(usersDoc1);
//			assertFalse(myResponse.contains(testUser1),"User email is found in collaborators list:"+testUser1 +".");		
//			assertTrue(myResponse.contains(testUser2),"User email not found in collaborators list:"+testUser2 +".");		
//			usersDoc1 = getExposedUsers(elapp.el_office_365.name(), qparamsForMail);
//			myResponse = MarshallingUtils.marshall(usersDoc1);
//			assertTrue(myResponse.contains(externalUser2),"User email not found in owner list:"+externalUser2 +".");
			
			for (; i <= (MAX_EXPOSURE_WAIT_TIME_IN_MINUTES * 60 * 1000); i+=60000 ) {
				Reporter.log("Checking exposure after :"+TimeUnit.MILLISECONDS.toMinutes(i)+" minute(s)",true );
				Reporter.log("------------------------------------------------------------------------",true );
				Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
				
				
				//Get the exposure count
				exposureDoc1 = this.getRiskyDocuments(elapp.el_office_365.name(), qparamsForFile1);
				countAfterExposure = exposureDoc1.getMeta().getTotalCount();
				Reporter.log("Exposure Count ="+countAfterExposure, true);
				
				if (exposureDoc1.getObjects().get(0).getExposures().getInternal().toString().contains(testUser1) == false) {break;}
			}
			assertFalse(exposureDoc1.getObjects().get(0).getExposures().getInternal().toString().contains(testUser1),"User is found in risky docs internal user list: "+testUser1);
			assertTrue(exposureDoc1.getObjects().get(0).getExposures().getInternal().toString().contains(testUser2),"User not found in risky docs internal user list: "+testUser2);
			
			//Check the user and collaborator list
//			usersDoc1 = getCollaborators(elapp.el_office_365.name(), qparamsForFile1);
//			myResponse = MarshallingUtils.marshall(usersDoc1);
//			assertFalse(myResponse.contains(testUser1),"User email is found in collaborators list:"+testUser1 +".");		
//			assertTrue(myResponse.contains(testUser2),"User email not found in collaborators list:"+testUser2 +".");		
//			usersDoc1 = getExposedUsers(elapp.el_office_365.name(), qparamsForFile1);
//			myResponse = MarshallingUtils.marshall(usersDoc1);
//			assertTrue(myResponse.contains(externalUser2),"User email not found in owner list:"+externalUser2 +".");
			
			Reporter.log("------------------------------------------------------------------------",true );
			
			for (; i <= (MAX_EXPOSURE_WAIT_TIME_IN_MINUTES * 60 * 1000); i+=60000 ) {
				Reporter.log("Checking exposure after :"+TimeUnit.MILLISECONDS.toMinutes(i)+" minute(s)",true );
				Reporter.log("------------------------------------------------------------------------",true );
				Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
				
				
				//Get the exposure count
				exposureDoc2 = this.getRiskyDocuments(elapp.el_office_365.name(), qparamsForFile2);
				countAfterExposure = exposureDoc2.getMeta().getTotalCount();
				Reporter.log("Exposure Count ="+countAfterExposure, true);
				
				if (exposureDoc2.getObjects().get(0).getExposures().getInternal().toString().contains(testUser1) == false) {break;}
			}
			assertFalse(exposureDoc2.getObjects().get(0).getExposures().getInternal().toString().contains(testUser1),"User is found in risky docs internal user list: "+testUser1);
			assertTrue(exposureDoc2.getObjects().get(0).getExposures().getInternal().toString().contains(testUser2),"User not found in risky docs internal user list: "+testUser2);
//			assertTrue(exposureDoc1.getObjects().get(0).getExposures().getInternal().toString().contains(testUser2),"User not found in risky docs internal user list: "+testUser2);
			
			//Check the user and collaborator list
//			usersDoc1 = getCollaborators(elapp.el_office_365.name(), qparamsForFile2);
//			myResponse = MarshallingUtils.marshall(usersDoc1);
//			assertFalse(myResponse.contains(testUser1),"User email is found in collaborators list:"+testUser1 +".");		
//			assertTrue(myResponse.contains(testUser2),"User email not found in collaborators list:"+testUser2 +".");		
//			usersDoc1 = getExposedUsers(elapp.el_office_365.name(), qparamsForFile2);
//			myResponse = MarshallingUtils.marshall(usersDoc1);
//			assertTrue(myResponse.contains(externalUser2),"User email not found in owner list:"+externalUser2 +".");
			
			
			Reporter.log("----------------Delete the mail from inbox of "+testUser2+" --------------------",true );
			synchronized(this){objMailTestUser2 = new Office365MailActivities(testUser2,testUser2Pwd);}
			assertTrue(objMailTestUser2.deleteMailFromFolder(myMailSubject,"Inbox", DeleteMode.MoveToDeletedItems), "Delete mail from Inbox failed for user:"+testUser2);
			
			Reporter.log("----------------Delete the mail from Sent Items of sender --------------------",true );
			synchronized(this){
			assertTrue(objMailSysAdmin.deleteMailFromFolder(myMailSubject,"SentItems", DeleteMode.MoveToDeletedItems), "Delete mail from SentItems failed for user:"+adminMailId);
			}
			//Wait for remedial action
			Reporter.log("---------------------Waiting for the mail deletion action in SAAS APP-----------------------------",true );
			
			i = 60000;
			
			
			 countAfterRemediation = countAfterExposure ;
			i = 60000;
			for (; i <= (MAX_REMEDIATION_WAIT_TIME_IN_MINUTES * 60 * 1000); i+=60000 ) {
				Reporter.log("Wait for "+TimeUnit.MILLISECONDS.toMinutes(i)+" minutes, and check if the file:"+myMailSubject +" is removed from exposure.",true);
				Reporter.log("------------------------------------------------------------------------",true );
				Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
				mailExpoDoc = this.getRiskyDocuments(elapp.el_office_365.name(), qparamsForMail);
				countAfterRemediation = mailExpoDoc.getMeta().getTotalCount();
				
				if (countAfterRemediation == 0) {
					break;
				}
				
			}
			
			
			Reporter.log("###### exposure count after mail deletion::"+countAfterRemediation, true);
			assertEquals(countAfterRemediation, 0, "Exposure count has not reduced after mail deletion.");
			//Check the user and collaborator list
//			usersDoc1 = getCollaborators(elapp.el_office_365.name(), qparamsForMail);
//			assertEquals(usersDoc1.getMeta().getTotalCount(), 0, "Collaborator list is still not empty after deletion of email.");			
//			usersDoc2 = getExposedUsers(elapp.el_office_365.name(), qparamsForMail);
//			assertEquals(usersDoc2.getMeta().getTotalCount(), 0,"Owner list is still not empty after deletion of email.");
			
			for (; i <= (MAX_REMEDIATION_WAIT_TIME_IN_MINUTES * 60 * 1000); i+=60000 ) {
				Reporter.log("Wait for "+TimeUnit.MILLISECONDS.toMinutes(i)+" minutes, and check if the file:"+searchFileName1 +" is removed from exposure.",true);
				Reporter.log("------------------------------------------------------------------------",true );
				Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
				exposureDoc1 = this.getRiskyDocuments(elapp.el_office_365.name(), qparamsForFile1);
				countAfterRemediation = exposureDoc1.getMeta().getTotalCount();
				
				if (countAfterRemediation == 0) {
					break;
				}
				
			}
			Reporter.log("###### exposure count after mail deletion::"+countAfterRemediation, true);
			assertEquals(countAfterRemediation, 0, "Exposure count has not reduced after mail deletion.");
			//Check the user and collaborator list
//			usersDoc1 = getCollaborators(elapp.el_office_365.name(), qparamsForFile1);
//			assertEquals(usersDoc1.getMeta().getTotalCount(), 0,"Collaborator list is still not empty after deletion of email.");		
//			usersDoc2 = getExposedUsers(elapp.el_office_365.name(), qparamsForFile1);
//			assertEquals(usersDoc2.getMeta().getTotalCount(), 0,"Owner list is still not empty after deletion of email.");
			
			for (; i <= (MAX_REMEDIATION_WAIT_TIME_IN_MINUTES * 60 * 1000); i+=60000 ) {
				Reporter.log("Wait for "+TimeUnit.MILLISECONDS.toMinutes(i)+" minutes, and check if the file:"+searchFileName2 +" is removed from exposure.",true);
				Reporter.log("------------------------------------------------------------------------",true );
				Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
				exposureDoc2 = this.getRiskyDocuments(elapp.el_office_365.name(), qparamsForFile2);
				countAfterRemediation = exposureDoc2.getMeta().getTotalCount();
				
				if (countAfterRemediation == 0) {
					break;
				}
				
			}
			
			Reporter.log("###### exposure count after mail deletion::"+countAfterRemediation, true);
			assertEquals(countAfterRemediation, 0, "Exposure count has not reduced after mail deletion.");
			//Check the user and collaborator list
//			usersDoc1 = getCollaborators(elapp.el_office_365.name(), qparamsForFile2);
//			assertEquals(usersDoc1.getMeta().getTotalCount(), 0, "Collaborator list is still not empty after deletion of email.");		
//			usersDoc2 = getExposedUsers(elapp.el_office_365.name(), qparamsForFile2);
//			assertEquals(usersDoc2.getMeta().getTotalCount(), 0,"Owner list is still not empty after deletion of email.");
			
			
			
		}
			
		@DataProvider(parallel=true)
		public Object[][] dataProviderRemediationWhenMultipleAttachmentExposureInternal() {
			return new Object[][]{
			/* is_internal  remediationType deletionType
			 *    
			 */ 
				
				{"true", 	"attachment", "by remediation"},
				{"true", 	"mail",		  "by remediation"},
				{"true", 	"mail",		  "from mailbox"},
			};
		}
		
		/**
		 * Send an email with content violation in mail and attachment to multiple users including internal and external users. Verify the exposure and remediate the same
		 * @param isInternal
		 * @param exposuretype
		 * @throws Exception
		 */
		@Test(groups={"P1"},dataProvider = "dataProviderRemediationWhenMultipleAttachmentExposureInternal")
		public void verifyExposureWhenMultipleAttachmentMailRemediatedInternalWithMailGroup(
				String isInternal, 
				String remediationType,
				String deletionType
				) throws Exception {
			
			Reporter.log("****************Test Case Description****************",true);
			Reporter.log("Send an email with content violation in mail and attachment to multiple users including internal group id and external users. Verify the exposure and remediate the same. ",true);
			Reporter.log("1)Send Mail with risk in body and mulitple attachments to  internal group id and external users",true);
			Reporter.log("2)Verify that the mail and attachment are exposed, sender and receiver appears in Users, Collabs list accordingly",true);
			Reporter.log("3)Delete mail "+deletionType+" .Verify that the action is successful and mail is deleted from sentItems folder of sender and inbox of internal users",true);
			Reporter.log("4)Verify that the mail and attachment are removed from exposure",true);
			Reporter.log("5)Verify that the deletion message also appears in the logs",true);
			Reporter.log("*****************************************************",true);
			
			
			//Prepare the remediation object
			Item emailObj = null;
			String myUniqueId = UUID.randomUUID().toString();
			ArrayList<String> myAttachments = new ArrayList<String>() ;
			String fileName1 = "PII.rtf";
			String fileName2 = "PCI_Test.txt";
			String searchFileName1 =myUniqueId+fileName1;
			String searchFileName2 =myUniqueId+fileName2;
			File sourceFile1 =new File(userDir+fileName1);
			File sourceFile2 =new File(userDir+fileName2);
			File destnFile1 =new File(userDir+searchFileName1);
			File destnFile2 =new File(userDir+searchFileName2);
			
			String myMailSubject =null;
			String mailBody = readFile(sourceFile2.toString(), Charset.defaultCharset());
			boolean success =false;
			String docTypeForRemediation = null;
			int countAfterExposure =0;
			int responseCode = 0;
			String expectedLog = null;
			EmailAddressCollection to = new EmailAddressCollection();
			EmailAddressCollection cc = new EmailAddressCollection();
			List<NameValuePair> qparamsForMail = new ArrayList<NameValuePair>(); 
			List<NameValuePair> qparamsForFile1 = new ArrayList<NameValuePair>(); 
			List<NameValuePair> qparamsForFile2 = new ArrayList<NameValuePair>(); 
			SecurletDocument mailExpoDoc = new SecurletDocument();
			SecurletDocument exposureDoc1 = new SecurletDocument();
			SecurletDocument exposureDoc2 = new SecurletDocument();
			SecurletDocument usersDoc1 = new SecurletDocument();
			SecurletDocument usersDoc2 = new SecurletDocument();
			SecurletDocument usersDoc3 = new SecurletDocument();
			String remedialAction ="ITEM_DELETE_MAIL";
			//Prepare the violations
			String docId=null;
			String myResponse;
			
			Reporter.log("------------------------------------------------------------------------",true );
			Reporter.log("Sending mail to external user",true);
			Reporter.log("------------------------------------------------------------------------",true );
			
			myMailSubject = myUniqueId+"MultipleAttachments";
			myAttachments.add(destnFile1.toString());
			myAttachments.add(destnFile2.toString());
			
			
			//copying file for external user - office 365
			copyFileUsingFileChannels(sourceFile1, destnFile1);
			copyFileUsingFileChannels(sourceFile2, destnFile2);
			filesToDelete.add(destnFile1);
			filesToDelete.add(destnFile2);
			
			
			qparamsForMail.add(new BasicNameValuePair(SecurletsConstants.IS_INTERNAL,  isInternal));
			qparamsForFile1.add(new BasicNameValuePair(SecurletsConstants.IS_INTERNAL,  isInternal));
			qparamsForFile2.add(new BasicNameValuePair(SecurletsConstants.IS_INTERNAL,  isInternal));
			qparamsForMail.add(new BasicNameValuePair("name",  myMailSubject));
			qparamsForFile1.add(new BasicNameValuePair("name",  searchFileName1));
			qparamsForFile2.add(new BasicNameValuePair("name",  searchFileName2));
			
			
			synchronized(this){ 
				if(isInternal.equals("true")){
					//sending mail to multipel users
					to.add(externalUser1);
					cc.add(groupMailId);
					success= objMailSysAdmin.sendMailWithCCAndBCC(to, cc,null,myMailSubject,mailBody,myAttachments,true);
				}
			}
			
			cleanupListSent.add(myMailSubject);
			assertTrue(success, "Failed sending mail with subject:"+myMailSubject+".");
			success=false;
			
			//wait for 2 minutes for the exposure to be applied
			 Thread.sleep(CommonConstants.TWO_MINUTES_SLEEP);
			 
			 ArrayList<String> myMailMessages = new ArrayList<String>();
				ArrayList<String> logs;
				int i = 180000;
				try {
					for (; i <= ( MAX_LOG_WAIT_TIME_IN_MINUTES * 60 * 1000); i+=60000 ) {
						Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
						Reporter.log("------------------------------------------------------------------------",true );
						Reporter.log("Searching for logs after :"+TimeUnit.MILLISECONDS.toMinutes(i)+" minutes",true );
						Reporter.log("------------------------------------------------------------------------" ,true);
						logs = searchDisplayLogs(fromTime, 60, "Office 365","Object_type", "Email_Message", myUniqueId, suiteData.getUsername(), suiteData.getCSRFToken(), suiteData.getSessionID(),"");
						myMailMessages.addAll(logs);
						

						displayMessageContent(myMailMessages);

						//Reporter.log("Actual file messages:" + actualMailMessages, true);
						if (myMailMessages.size() >= 1) {break;}
						else{myMailMessages.clear();}
					}
				}
				catch(Exception e) {}
				
				assertEquals(myMailMessages.size(),1,"All Recieved/sent logs not found even after waiting for "+TimeUnit.MILLISECONDS.toMinutes(i-60000)+" minutes.");
			
			Reporter.log("------------------------------------------------------------------------",true );
			
			 i = 60000;
			for (; i <= (MAX_EXPOSURE_WAIT_TIME_IN_MINUTES * 60 * 1000); i+=60000 ) {
				Reporter.log("Checking exposure after :"+TimeUnit.MILLISECONDS.toMinutes(i)+" minute(s)",true );
				Reporter.log("------------------------------------------------------------------------",true );
				Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
				
				
				//Get the exposure count
				mailExpoDoc = this.getExposedDocuments(elapp.el_office_365.name(), qparamsForMail);
				countAfterExposure = mailExpoDoc.getMeta().getTotalCount();
				Reporter.log("Exposure Count ="+countAfterExposure, true);
				
				if (countAfterExposure >= 1) {break;}
			}
			assertEquals(countAfterExposure,1,"File:"+myMailSubject +" not exposed even after waiting for "+TimeUnit.MILLISECONDS.toMinutes(i-60000)+" minutes.");
			//Check the user and collaborator list
			usersDoc1 = getCollaborators(elapp.el_office_365.name(), qparamsForMail);
			myResponse = MarshallingUtils.marshall(usersDoc1);
			assertTrue(myResponse.contains(externalUser1),"User email not found in collaborators list:"+externalUser1 +".");		
//			assertTrue(myResponse.contains(groupMailId),"User email not found in collaborators list:"+groupMailId +".");		
			usersDoc2 = getExposedUsers(elapp.el_office_365.name(), qparamsForMail);
			assertTrue(usersDoc2.getObjects().get(0).getEmail().contains(adminMailId),"User email not found in owner list:"+adminMailId +".");
			
			for (; i <= (MAX_EXPOSURE_WAIT_TIME_IN_MINUTES * 60 * 1000); i+=60000 ) {
				Reporter.log("Checking exposure after :"+TimeUnit.MILLISECONDS.toMinutes(i)+" minute(s)",true );
				Reporter.log("------------------------------------------------------------------------",true );
				Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
				
				
				//Get the exposure count
				exposureDoc1 = this.getExposedDocuments(elapp.el_office_365.name(), qparamsForFile1);
				countAfterExposure = exposureDoc1.getMeta().getTotalCount();
				Reporter.log("Exposure Count ="+countAfterExposure, true);
				
				if (countAfterExposure >= 1) {break;}
			}
			assertEquals(countAfterExposure,1,"File:"+searchFileName1 +" not exposed even after waiting for "+TimeUnit.MILLISECONDS.toMinutes(i-60000)+" minutes.");
			
			//Check the user and collaborator list
			usersDoc1 = getCollaborators(elapp.el_office_365.name(), qparamsForFile1);
			myResponse = MarshallingUtils.marshall(usersDoc1);
			assertTrue(myResponse.contains(externalUser1),"User email not found in collaborators list:"+externalUser1 +".");	
//			assertTrue(myResponse.contains(groupMailId),"User email not found in collaborators list:"+groupMailId +".");
			usersDoc2 = getExposedUsers(elapp.el_office_365.name(), qparamsForFile1);
			assertTrue(usersDoc2.getObjects().get(0).getEmail().contains(adminMailId),"User email not found in owner list:"+adminMailId +".");
			
			Reporter.log("------------------------------------------------------------------------",true );
			
			for (; i <= (MAX_EXPOSURE_WAIT_TIME_IN_MINUTES * 60 * 1000); i+=60000 ) {
				Reporter.log("Checking exposure after :"+TimeUnit.MILLISECONDS.toMinutes(i)+" minute(s)",true );
				Reporter.log("------------------------------------------------------------------------",true );
				Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
				
				
				//Get the exposure count
				exposureDoc2 = this.getExposedDocuments(elapp.el_office_365.name(), qparamsForFile2);
				countAfterExposure = exposureDoc2.getMeta().getTotalCount();
				Reporter.log("Exposure Count ="+countAfterExposure, true);
				
				if (countAfterExposure >= 1) {break;}
			}
			assertEquals(countAfterExposure,1,"File:"+searchFileName2 +" not exposed even after waiting for "+TimeUnit.MILLISECONDS.toMinutes(i-60000)+" minutes.");
			
			//Check the user and collaborator list
			usersDoc1 = getCollaborators(elapp.el_office_365.name(), qparamsForFile2);
			myResponse = MarshallingUtils.marshall(usersDoc1);
			assertTrue(myResponse.contains(externalUser1),"User email not found in collaborators list:"+externalUser1 +".");		
//			assertTrue(myResponse.contains(groupMailId),"User email not found in collaborators list:"+groupMailId +".");
			usersDoc2 = getExposedUsers(elapp.el_office_365.name(), qparamsForFile2);
			assertTrue(usersDoc2.getObjects().get(0).getEmail().contains(adminMailId),"User email not found in owner list:"+adminMailId +".");
			
			//		Reporter.log("----------------Making API call to get documented Id--------------------",true );
			if(remediationType.equals("mail")){
				docTypeForRemediation = "Email_Message";
				docId = mailExpoDoc.getObjects().get(0).getIdentification();
			}
			else if(remediationType.equals("attachment")){
				
				docTypeForRemediation = "Email_File_Attachment";
				docId = exposureDoc1.getObjects().get(0).getIdentification();
			}
			
			Reporter.log("----------------Delete the file "+deletionType+"--------------------",true );
			
			
			//creating the remediation object
			MailRemediation objRemediation = getRemediationObject(adminMailId, docTypeForRemediation, docId, remedialAction);
			
			
			if(deletionType.equals("by remediation")){
				//Now apply the remedial action through API server call
				responseCode = remediateExposureWithAPI(objRemediation);
//				Assert.assertEquals(responseCode, 202, "Remediation call failed.");
			}
			else if (deletionType.equals("from mailbox")){
				synchronized(this){
					assertTrue(objMailSysAdmin.deleteMailFromFolder(myMailSubject,"SentItems", DeleteMode.MoveToDeletedItems), "Delete mail from SentItems failed for user:"+adminMailId);
					}
				
				synchronized(this){objMailTestUser1 = new Office365MailActivities(testUser1,testUser1Pwd);
				assertTrue(objMailTestUser1.deleteMailFromFolder(myMailSubject,"Inbox", DeleteMode.MoveToDeletedItems), "Delete mail from Inbox failed for user:"+testUser1);
				}
				
				synchronized(this){objMailTestUser2 = new Office365MailActivities(testUser2,testUser2Pwd);
				assertTrue(objMailTestUser2.deleteMailFromFolder(myMailSubject,"Inbox", DeleteMode.MoveToDeletedItems), "Delete mail from Inbox failed for user:"+testUser2);
				}
			}
			
			//Wait for remedial action
			Reporter.log("---------------------Waiting for the action in SAAS APP-----------------------------",true );
			
			i = 60000;
			boolean remediationSuccess = false;
			for (; i <= (MAX_REMEDIATION_WAIT_TIME_IN_MINUTES * 60 * 1000); i+=60000 ) {
				Reporter.log("Checking if mail deleted after :"+TimeUnit.MILLISECONDS.toMinutes(i)+" minute(s)",true );
				Reporter.log("------------------------------------------------------------------------",true );
				Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
				
				synchronized (this) {
					//Searching for email in mailbox sent items
					emailObj = objMailSysAdmin.findItemInFolder(myMailSubject, "SentItems");
					
				}
				
				//set remediationSuccess as true if mail or attachment deleted based on remediation type
//				if (remedialAction.equals(Remediation.ITEM_DELETE_MAIL_BY_ATTACHMENT.name()) || remedialAction.equals(Remediation.ITEM_DELETE_MAIL.name())) {
				if ( remedialAction.equals(Remediation.ITEM_DELETE_MAIL.name())) {
					
					if(emailObj==null){
						remediationSuccess=true;
						
					}
					
				} 
				
				
				if (remediationSuccess == true) {
					break;
				}
				else{
					Reporter.log("Mail/Attachment not yet deleted",true);
				}
				
			}
			
			//Verify the remediation thro' o365 API
//			if (remedialAction.equals(Remediation.ITEM_DELETE_MAIL_BY_ATTACHMENT.name()) || remedialAction.equals(Remediation.ITEM_DELETE_MAIL.name())) {
			if (remedialAction.equals(Remediation.ITEM_DELETE_MAIL.name())) {
				expectedLog = "User trashed email with subject \""+myMailSubject+"\"";//expectedLog = "QA Admin has remediated/deleted an email with subject \""+myMailSubject+"\"";
				assertNull(emailObj, "Remediation "+ remedialAction + " didn't work. Mail is not deleted from sent folder of "+adminMailId+" even after waiting for "+TimeUnit.MILLISECONDS.toMinutes(i-60000)+" minutes.");
				
			}
			
			//verify that mails are deleted from inbox for internal users
			Item message=objMailTestUser2.findItemInFolder(myMailSubject, "Inbox");
			Assert.assertTrue(message==null, "Mail was not deleted from the inbox of user:"+testUser2);
			message=objMailTestUser1.findItemInFolder(myMailSubject, "Inbox");
			Assert.assertTrue(message==null, "Mail was not deleted from the inbox of user:"+testUser1);
			
			
		
			
			Reporter.log("Remediation successful at GMAIL");
			
			//gather the forensic logs again 
			//logs = gatherForensicLogMessages(uniqueId, facility.Box.name());
			
			Reporter.log("------------------------------------------------------------------------",true );
			
			int countAfterRemediation = countAfterExposure ;
			i = 60000;
			for (; i <= (MAX_REMEDIATION_WAIT_TIME_IN_MINUTES * 60 * 1000); i+=60000 ) {
				Reporter.log("Wait for "+TimeUnit.MILLISECONDS.toMinutes(i)+" minutes, and check if the file:"+myMailSubject +" is removed from exposure.",true);
				Reporter.log("------------------------------------------------------------------------",true );
				Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
				mailExpoDoc = this.getExposedDocuments(elapp.el_office_365.name(), qparamsForMail);
				countAfterRemediation = mailExpoDoc.getMeta().getTotalCount();
				
				if (countAfterRemediation == 0) {
					break;
				}
				
			}
			
			
			Reporter.log("###### exposure count after remediation::"+countAfterRemediation, true);
			assertEquals(countAfterRemediation, 0, "Exposure count has not reduced after remediation.");
			//Check the user and collaborator list
			usersDoc1 = getCollaborators(elapp.el_office_365.name(), qparamsForMail);
			myResponse = MarshallingUtils.marshall(usersDoc1);
			assertFalse(myResponse.contains(externalUser1),"Collaborator is still exposed after deletion of email:"+externalUser1 +".");		
//			assertFalse(myResponse.contains(groupMailId),"Collaborator is still exposed after deletion of email:"+groupMailId +".");		
			usersDoc2 = getExposedUsers(elapp.el_office_365.name(), qparamsForMail);
			assertEquals(usersDoc2.getMeta().getTotalCount(), 0,"Owner is still exposed after deletion of email:"+adminMailId +".");
			
			for (; i <= (MAX_REMEDIATION_WAIT_TIME_IN_MINUTES * 60 * 1000); i+=60000 ) {
				Reporter.log("Wait for "+TimeUnit.MILLISECONDS.toMinutes(i)+" minutes, and check if the file:"+searchFileName1 +" is removed from exposure.",true);
				Reporter.log("------------------------------------------------------------------------",true );
				Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
				exposureDoc1 = this.getExposedDocuments(elapp.el_office_365.name(), qparamsForFile1);
				countAfterRemediation = exposureDoc1.getMeta().getTotalCount();
				
				if (countAfterRemediation == 0) {
					break;
				}
				
			}
			Reporter.log("###### exposure count after remediation::"+countAfterRemediation, true);
			assertEquals(countAfterRemediation, 0, "Exposure count has not reduced after remediation.");
			//Check the user and collaborator list
			usersDoc1 = getCollaborators(elapp.el_office_365.name(), qparamsForFile1);
			myResponse = MarshallingUtils.marshall(usersDoc1);
			assertFalse(myResponse.contains(externalUser1),"Collaborator is still exposed after deletion of email:"+externalUser1 +".");		
//			assertFalse(myResponse.contains(externalUser2),"Collaborator is still exposed after deletion of email:"+externalUser2 +".");		
			usersDoc2 = getExposedUsers(elapp.el_office_365.name(), qparamsForFile1);
			assertEquals(usersDoc2.getMeta().getTotalCount(), 0,"Owner is still exposed after deletion of email:"+testUser1 +".");
			
			for (; i <= (MAX_REMEDIATION_WAIT_TIME_IN_MINUTES * 60 * 1000); i+=60000 ) {
				Reporter.log("Wait for "+TimeUnit.MILLISECONDS.toMinutes(i)+" minutes, and check if the file:"+searchFileName2 +" is removed from exposure.",true);
				Reporter.log("------------------------------------------------------------------------",true );
				Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
				exposureDoc2 = this.getExposedDocuments(elapp.el_office_365.name(), qparamsForFile2);
				countAfterRemediation = exposureDoc2.getMeta().getTotalCount();
				
				if (countAfterRemediation == 0) {
					break;
				}
				
			}
			Reporter.log("###### exposure count after remediation::"+countAfterRemediation, true);
			assertEquals(countAfterRemediation, 0, "Exposure count has not reduced after remediation.");
			//Check the user and collaborator list
			usersDoc1 = getCollaborators(elapp.el_office_365.name(), qparamsForFile2);
			myResponse = MarshallingUtils.marshall(usersDoc1);
			assertFalse(myResponse.contains(externalUser1),"Collaborator is still exposed after deletion of email:"+externalUser1 +".");		
//			assertFalse(myResponse.contains(externalUser2),"Collaborator is still exposed after deletion of email:"+externalUser2 +".");		
			usersDoc2 = getExposedUsers(elapp.el_office_365.name(), qparamsForFile2);
			assertEquals(usersDoc2.getMeta().getTotalCount(), 0,"Owner is still exposed after deletion of email:"+testUser1 +".");
			
		
//			//Verify the activity logs
//			Reporter.log("Expected log:" + expectedLog, true);
//			ForensicSearchResults availableLogs = searchLogsWithWaitTime("Activity_type", "Trash", myUniqueId, testUser1, 1, 2);
//			assertNotNull(availableLogs,"Delete log not found after remediation");
//			assertEquals(availableLogs.getHits().getHits().get(0).getSource().getMessage(),expectedLog,	"Test failed, as expected log is not found after remediation by admin.");
//			
//			
			
		}
		
		/**
		 * External user sends content violation in mailbody and multiple attachments to  internal mail group which contains multiple internal user
		 * Verify the exposure and then delete the email from inbox of each internal users one by one  and check file exposure changed or not.
		 * @param isInternal
		 * @param exposuretype
		 * @throws Exception
		 */
		@Test(groups={"P1"},retryAnalyzer=RetryAnalyzer.class)
		public void verifyExposureWhenMultipleAttachmentMailDeletedExternalMailGroup(
				) throws Exception {
			
			Reporter.log("****************Test Case Description****************",true);
			Reporter.log("External user sends content violation in mailbody and multiple attachments to internal mail group which contains multiple internal user. Verify the exposure and delete email from each user inbox and verify the exposure. ",true);
			Reporter.log("1)External user sends mail to an internal email group id.( So Two internal users user1 and user2 receive a Mail with risk in body and mulitple attachments from external user)",true);
			Reporter.log("2)Verify that the mail and attachment are exposed, sender and receiver appears in Users, Collabs list accordingly",true);
			Reporter.log("3)Delete mail from inbox of user1.",true);
			Reporter.log("4)Verify that the mail and attachment remain exposed but user1 is removed from exposure",true);
			Reporter.log("5)Delete mail from inbox of user2.",true);
			Reporter.log("6)Verify that the mail and attachment are removed from exposure",true);
			Reporter.log("*****************************************************",true);
			
			
			String isInternal = "false";
			Item emailObj = null;
			String myUniqueId = UUID.randomUUID().toString();
			ArrayList<String> myAttachments = new ArrayList<String>() ;
			String fileName1 = "PII.rtf";
			String fileName2 = "PCI_Test.txt";
			String searchFileName1 =myUniqueId+fileName1;
			String searchFileName2 =myUniqueId+fileName2;
			File sourceFile1 =new File(userDir+fileName1);
			File sourceFile2 =new File(userDir+fileName2);
			File destnFile1 =new File(userDir+searchFileName1);
			File destnFile2 =new File(userDir+searchFileName2);
			
			String myMailSubject =null;
			String mailBody = readFile(sourceFile2.toString(), Charset.defaultCharset());
			boolean success =false;
			String docTypeForRemediation = null;
			int countAfterExposure =0;
			int responseCode = 0;
			String expectedLog = null;
			EmailAddressCollection to = new EmailAddressCollection();
			List<NameValuePair> qparamsForMail = new ArrayList<NameValuePair>(); 
			List<NameValuePair> qparamsForFile1 = new ArrayList<NameValuePair>(); 
			List<NameValuePair> qparamsForFile2 = new ArrayList<NameValuePair>(); 
			SecurletDocument mailExpoDoc = new SecurletDocument();
			SecurletDocument exposureDoc1 = new SecurletDocument();
			SecurletDocument exposureDoc2 = new SecurletDocument();
			SecurletDocument usersDoc1 = new SecurletDocument();
			SecurletDocument usersDoc2 = new SecurletDocument();
			SecurletDocument usersDoc3 = new SecurletDocument();
			String myResponse;
			String remedialAction ="ITEM_DELETE_MAIL";
			//Prepare the violations
			String docId=null;
			
			Reporter.log("------------------------------------------------------------------------",true );
			Reporter.log("External user sends mail to internal mailgroup",true);
			Reporter.log("------------------------------------------------------------------------",true );
			
			myMailSubject = myUniqueId+"MultipleAttachments";
			myAttachments.add(destnFile1.toString());
			myAttachments.add(destnFile2.toString());
			
			//copying file for external user - office 365
			copyFileUsingFileChannels(sourceFile1, destnFile1);
			copyFileUsingFileChannels(sourceFile2, destnFile2);
			filesToDelete.add(destnFile1);
			filesToDelete.add(destnFile2);
			
			
			qparamsForMail.add(new BasicNameValuePair(SecurletsConstants.IS_INTERNAL,  isInternal));
			qparamsForFile1.add(new BasicNameValuePair(SecurletsConstants.IS_INTERNAL,  isInternal));
			qparamsForFile2.add(new BasicNameValuePair(SecurletsConstants.IS_INTERNAL,  isInternal));
			qparamsForMail.add(new BasicNameValuePair("name",  myMailSubject));
			qparamsForFile1.add(new BasicNameValuePair("name",  searchFileName1));
			qparamsForFile2.add(new BasicNameValuePair("name",  searchFileName2));
			
			EmailAddressCollection recipientsCc = new EmailAddressCollection();
			EmailAddressCollection recipientsTo = new EmailAddressCollection();
			
			recipientsTo.add(groupMailId);
			
			synchronized(this){ 
				success = objMailExternalUser.sendMailWithCCAndBCC(recipientsTo,null,null, myMailSubject,mailBody ,myAttachments , false);
			}
			
			cleanupListSent.add(myMailSubject);
			assertTrue(success, "Failed sending mail with subject:"+myMailSubject+".");
			success=false;
			
			
			Reporter.log("------------------------------------------------------------------------",true );
			
			int i = 60000;
			for (; i <= (MAX_EXPOSURE_WAIT_TIME_IN_MINUTES * 60 * 1000); i+=60000 ) {
				Reporter.log("Checking exposure after :"+TimeUnit.MILLISECONDS.toMinutes(i)+" minute(s)",true );
				Reporter.log("------------------------------------------------------------------------",true );
				Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
				
				
				//Get the exposure count
				mailExpoDoc = this.getExposedDocuments(elapp.el_office_365.name(), qparamsForMail);
				countAfterExposure = mailExpoDoc.getMeta().getTotalCount();
				Reporter.log("Exposure Count ="+countAfterExposure, true);
				
				if (countAfterExposure>=1){
					if(mailExpoDoc.getObjects().get(0).getExposures().getInternal().size() == 3) {break;}
				}
			}
			assertEquals(countAfterExposure,1,"File:"+myMailSubject +" not exposed even after waiting for "+TimeUnit.MILLISECONDS.toMinutes(i-60000)+" minutes.");
//			assertTrue(mailExpoDoc.getObjects().get(0).getExposures().getInternal().toString().contains(groupMailId),"User not found in exposure internal user list: "+groupMailId);
			assertTrue(mailExpoDoc.getObjects().get(0).getExposures().getInternal().toString().contains(testUser1),"User not found in exposure internal user list: "+testUser1);
			assertTrue(mailExpoDoc.getObjects().get(0).getExposures().getInternal().toString().contains(testUser2),"User not found in exposure internal user list: "+testUser2);
			
			//Check the user and collaborator list
			usersDoc1 = getCollaborators(elapp.el_office_365.name(), qparamsForMail);
			myResponse = MarshallingUtils.marshall(usersDoc1);
//			assertTrue(myResponse.contains(groupMailId),"User email not found in collaborators list:"+groupMailId +".");		
			assertTrue(myResponse.contains(testUser1),"User email not found in collaborators list:"+testUser1 +".");		
			assertTrue(myResponse.contains(testUser2),"User email not found in collaborators list:"+testUser2 +".");		
			usersDoc1 = getExposedUsers(elapp.el_office_365.name(), qparamsForMail);
			myResponse = MarshallingUtils.marshall(usersDoc1);
			assertTrue(myResponse.contains(externalUser2),"User email not found in owner list:"+externalUser2 +".");
			
			for (; i <= (MAX_EXPOSURE_WAIT_TIME_IN_MINUTES * 60 * 1000); i+=60000 ) {
				Reporter.log("Checking exposure after :"+TimeUnit.MILLISECONDS.toMinutes(i)+" minute(s)",true );
				Reporter.log("------------------------------------------------------------------------",true );
				Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
				
				
				//Get the exposure count
				exposureDoc1 = this.getExposedDocuments(elapp.el_office_365.name(), qparamsForFile1);
				countAfterExposure = exposureDoc1.getMeta().getTotalCount();
				Reporter.log("Exposure Count ="+countAfterExposure, true);
				
				if (countAfterExposure >= 1) {break;}
			}
			assertEquals(countAfterExposure,1,"File:"+searchFileName1 +" not exposed even after waiting for "+TimeUnit.MILLISECONDS.toMinutes(i-60000)+" minutes.");
//			assertTrue(exposureDoc1.getObjects().get(0).getExposures().getInternal().toString().contains(groupMailId),"User not found in exposure internal user list: "+groupMailId);
			assertTrue(exposureDoc1.getObjects().get(0).getExposures().getInternal().toString().contains(testUser1),"User not found in exposure internal user list: "+testUser1);
			assertTrue(exposureDoc1.getObjects().get(0).getExposures().getInternal().toString().contains(testUser2),"User not found in exposure internal user list: "+testUser2);
			//Check the user and collaborator list
			usersDoc1 = getCollaborators(elapp.el_office_365.name(), qparamsForFile1);
			myResponse = MarshallingUtils.marshall(usersDoc1);
//			assertTrue(myResponse.contains(groupMailId),"User email not found in collaborators list:"+groupMailId +".");		
			assertTrue(myResponse.contains(testUser1),"User email not found in collaborators list:"+testUser1 +".");		
			assertTrue(myResponse.contains(testUser2),"User email not found in collaborators list:"+testUser2 +".");		
			usersDoc1 = getExposedUsers(elapp.el_office_365.name(), qparamsForFile1);
			myResponse = MarshallingUtils.marshall(usersDoc1);
			assertTrue(myResponse.contains(externalUser2),"User email not found in owner list:"+externalUser2 +".");
			
			Reporter.log("------------------------------------------------------------------------",true );
			
			for (; i <= (MAX_EXPOSURE_WAIT_TIME_IN_MINUTES * 60 * 1000); i+=60000 ) {
				Reporter.log("Checking exposure after :"+TimeUnit.MILLISECONDS.toMinutes(i)+" minute(s)",true );
				Reporter.log("------------------------------------------------------------------------",true );
				Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
				
				
				//Get the exposure count
				exposureDoc2 = this.getExposedDocuments(elapp.el_office_365.name(), qparamsForFile2);
				countAfterExposure = exposureDoc2.getMeta().getTotalCount();
				Reporter.log("Exposure Count ="+countAfterExposure, true);
				
				if (countAfterExposure >= 1) {break;}
			}
			assertEquals(countAfterExposure,1,"File:"+searchFileName2 +" not exposed even after waiting for "+TimeUnit.MILLISECONDS.toMinutes(i-60000)+" minutes.");
//			assertTrue(exposureDoc1.getObjects().get(0).getExposures().getInternal().toString().contains(groupMailId),"User not found in exposure internal user list: "+groupMailId);
			assertTrue(exposureDoc1.getObjects().get(0).getExposures().getInternal().toString().contains(testUser1),"User not found in exposure internal user list: "+testUser1);
			assertTrue(exposureDoc1.getObjects().get(0).getExposures().getInternal().toString().contains(testUser2),"User not found in exposure internal user list: "+testUser2);
			//Check the user and collaborator list
			usersDoc1 = getCollaborators(elapp.el_office_365.name(), qparamsForFile2);
			myResponse = MarshallingUtils.marshall(usersDoc1);
//			assertTrue(myResponse.contains(groupMailId),"User email not found in collaborators list:"+groupMailId +".");		
			assertTrue(myResponse.contains(testUser1),"User email not found in collaborators list:"+testUser1 +".");		
			assertTrue(myResponse.contains(testUser2),"User email not found in collaborators list:"+testUser2 +".");		
			usersDoc1 = getExposedUsers(elapp.el_office_365.name(), qparamsForFile2);
			myResponse = MarshallingUtils.marshall(usersDoc1);
			assertTrue(myResponse.contains(externalUser2),"User email not found in owner list:"+externalUser2 +".");
			
			
			Reporter.log("----------------Delete the mail from inbox of "+testUser1+" --------------------",true );
			synchronized(this){objMailTestUser1 = new Office365MailActivities(testUser1,testUser1Pwd);}
			assertTrue(objMailTestUser1.deleteMailFromFolder(myMailSubject,"Inbox", DeleteMode.MoveToDeletedItems), "Delete mail from Inbox failed for user:"+testUser1);
			
			//Wait for remedial action
			Reporter.log("---------------------Waiting for the mail deletion action in SAAS APP-----------------------------",true );
			
			i = 60000;
			boolean remediationSuccess = false;
			for (; i <= (MAX_REMEDIATION_WAIT_TIME_IN_MINUTES * 60 * 1000); i+=60000 ) {
				Reporter.log("Checking if mail deleted after :"+TimeUnit.MILLISECONDS.toMinutes(i)+" minute(s)",true );
				Reporter.log("------------------------------------------------------------------------",true );
				Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
				
				synchronized (this) {
					//Searching for email in mailbox sent items
					emailObj = objMailTestUser1.findItemInFolder(myMailSubject, "Inbox");
				}
				
				//set remediationSuccess as true if mail or attachment deleted based on remediation type
//				if (remedialAction.equals(Remediation.ITEM_DELETE_MAIL_BY_ATTACHMENT.name()) || remedialAction.equals(Remediation.ITEM_DELETE_MAIL.name())) {
				if ( remedialAction.equals(Remediation.ITEM_DELETE_MAIL.name())) {
					
					if(emailObj==null){
						remediationSuccess=true;
						
					}
					
				} 
				
				
				if (remediationSuccess == true) {
					break;
				}
				else{
					Reporter.log("Mail/Attachment not yet deleted",true);
				}
				
			}
			
			Reporter.log("Mail deletion successful at GMAIL");
			
			//gather the forensic logs again 
			//logs = gatherForensicLogMessages(uniqueId, facility.Box.name());
			
			Reporter.log("------------------------------------------------------------------------",true );
			
			int countAfterRemediation = countAfterExposure ;
			i = 60000;
			for (; i <= (MAX_REMEDIATION_WAIT_TIME_IN_MINUTES * 60 * 1000); i+=60000 ) {
				Reporter.log("Wait for "+TimeUnit.MILLISECONDS.toMinutes(i)+" minutes, and check if the file:"+myMailSubject +" is removed from exposure.",true);
				Reporter.log("------------------------------------------------------------------------",true );
				Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
				mailExpoDoc = this.getExposedDocuments(elapp.el_office_365.name(), qparamsForMail);
				countAfterRemediation = mailExpoDoc.getMeta().getTotalCount();
				
				if (countAfterRemediation == 1) {
					break;
				}
				
			}
			
			assertEquals(countAfterRemediation,1,"File:"+myMailSubject +" not exposed even after waiting for "+TimeUnit.MILLISECONDS.toMinutes(i-60000)+" minutes.");
			assertFalse(mailExpoDoc.getObjects().get(0).getExposures().getInternal().toString().contains(testUser1),"User is found in exposure internal user list: "+testUser1);
			assertTrue(mailExpoDoc.getObjects().get(0).getExposures().getInternal().toString().contains(testUser2),"User not found in exposure internal user list: "+testUser2);
//			assertTrue(mailExpoDoc.getObjects().get(0).getExposures().getInternal().toString().contains(groupMailId),"User not found in exposure internal user list: "+groupMailId);
			
			//Check the user and collaborator list
			usersDoc1 = getCollaborators(elapp.el_office_365.name(), qparamsForMail);
			myResponse = MarshallingUtils.marshall(usersDoc1);
			assertFalse(myResponse.contains(testUser1),"User email is found in collaborators list:"+testUser1 +".");		
			assertTrue(myResponse.contains(testUser2),"User email not found in collaborators list:"+testUser2 +".");		
//			assertTrue(myResponse.contains(groupMailId),"User email is found in collaborators list:"+groupMailId +".");		
			usersDoc1 = getExposedUsers(elapp.el_office_365.name(), qparamsForMail);
			myResponse = MarshallingUtils.marshall(usersDoc1);
			assertTrue(myResponse.contains(externalUser2),"User email not found in owner list:"+externalUser2 +".");
			
			for (; i <= (MAX_EXPOSURE_WAIT_TIME_IN_MINUTES * 60 * 1000); i+=60000 ) {
				Reporter.log("Checking exposure after :"+TimeUnit.MILLISECONDS.toMinutes(i)+" minute(s)",true );
				Reporter.log("------------------------------------------------------------------------",true );
				Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
				
				
				//Get the exposure count
				exposureDoc1 = this.getExposedDocuments(elapp.el_office_365.name(), qparamsForFile1);
				countAfterExposure = exposureDoc1.getMeta().getTotalCount();
				Reporter.log("Exposure Count ="+countAfterExposure, true);
				
				if (countAfterExposure >= 1) {break;}
			}
			assertEquals(countAfterExposure,1,"File:"+searchFileName1 +" not exposed even after waiting for "+TimeUnit.MILLISECONDS.toMinutes(i-60000)+" minutes.");
			assertFalse(exposureDoc1.getObjects().get(0).getExposures().getInternal().toString().contains(testUser1),"User is found in exposure internal user list: "+testUser1);
			assertTrue(exposureDoc1.getObjects().get(0).getExposures().getInternal().toString().contains(testUser2),"User not found in exposure internal user list: "+testUser2);
//			assertTrue(exposureDoc1.getObjects().get(0).getExposures().getInternal().toString().contains(groupMailId),"User not found in exposure internal user list: "+groupMailId);
			
			//Check the user and collaborator list
			usersDoc1 = getCollaborators(elapp.el_office_365.name(), qparamsForFile1);
			myResponse = MarshallingUtils.marshall(usersDoc1);
			assertFalse(myResponse.contains(testUser1),"User email is found in collaborators list:"+testUser1 +".");		
			assertTrue(myResponse.contains(testUser2),"User email not found in collaborators list:"+testUser2 +".");		
//			assertTrue(myResponse.contains(groupMailId),"User email not found in collaborators list:"+groupMailId +".");		
			usersDoc1 = getExposedUsers(elapp.el_office_365.name(), qparamsForFile1);
			myResponse = MarshallingUtils.marshall(usersDoc1);
			assertTrue(myResponse.contains(externalUser2),"User email not found in owner list:"+externalUser2 +".");
			
			Reporter.log("------------------------------------------------------------------------",true );
			
			for (; i <= (MAX_EXPOSURE_WAIT_TIME_IN_MINUTES * 60 * 1000); i+=60000 ) {
				Reporter.log("Checking exposure after :"+TimeUnit.MILLISECONDS.toMinutes(i)+" minute(s)",true );
				Reporter.log("------------------------------------------------------------------------",true );
				Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
				
				
				//Get the exposure count
				exposureDoc2 = this.getExposedDocuments(elapp.el_office_365.name(), qparamsForFile2);
				countAfterExposure = exposureDoc2.getMeta().getTotalCount();
				Reporter.log("Exposure Count ="+countAfterExposure, true);
				
				if (countAfterExposure >= 1) {break;}
			}
			assertEquals(countAfterExposure,1,"File:"+searchFileName2 +" not exposed even after waiting for "+TimeUnit.MILLISECONDS.toMinutes(i-60000)+" minutes.");
			assertFalse(exposureDoc1.getObjects().get(0).getExposures().getInternal().toString().contains(testUser1),"User is found in exposure internal user list: "+testUser1);
			assertTrue(exposureDoc1.getObjects().get(0).getExposures().getInternal().toString().contains(testUser2),"User not found in exposure internal user list: "+testUser2);
//			assertTrue(exposureDoc1.getObjects().get(0).getExposures().getInternal().toString().contains(groupMailId),"User not found in exposure internal user list: "+groupMailId);
			
			//Check the user and collaborator list
			usersDoc1 = getCollaborators(elapp.el_office_365.name(), qparamsForFile2);
			myResponse = MarshallingUtils.marshall(usersDoc1);
			assertFalse(myResponse.contains(testUser1),"User email is found in collaborators list:"+testUser1 +".");		
			assertTrue(myResponse.contains(testUser2),"User email not found in collaborators list:"+testUser2 +".");		
//			assertTrue(myResponse.contains(groupMailId),"User email not found in collaborators list:"+groupMailId +".");		
			usersDoc1 = getExposedUsers(elapp.el_office_365.name(), qparamsForFile2);
			myResponse = MarshallingUtils.marshall(usersDoc1);
			assertTrue(myResponse.contains(externalUser2),"User email not found in owner list:"+externalUser2 +".");
			
			
			Reporter.log("----------------Delete the mail from inbox of "+testUser2+" --------------------",true );
			synchronized(this){objMailTestUser2 = new Office365MailActivities(testUser2,testUser2Pwd);
			assertTrue(objMailTestUser2.deleteMailFromFolder(myMailSubject,"Inbox", DeleteMode.MoveToDeletedItems), "Delete mail from Inbox failed for user:"+testUser2);
			}

			
			//Wait for remedial action
			Reporter.log("---------------------Waiting for the mail deletion action in SAAS APP-----------------------------",true );
			
			i = 60000;
			remediationSuccess = false;
			for (; i <= (MAX_REMEDIATION_WAIT_TIME_IN_MINUTES * 60 * 1000); i+=60000 ) {
				Reporter.log("Checking if mail deleted after :"+TimeUnit.MILLISECONDS.toMinutes(i)+" minute(s)",true );
				Reporter.log("------------------------------------------------------------------------",true );
				Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
				
				synchronized (this) {
					//Searching for email in mailbox sent items
					emailObj = objMailTestUser2.findItemInFolder(myMailSubject, "Inbox");
				}
				
				//set remediationSuccess as true if mail or attachment deleted based on remediation type
//				if (remedialAction.equals(Remediation.ITEM_DELETE_MAIL_BY_ATTACHMENT.name()) || remedialAction.equals(Remediation.ITEM_DELETE_MAIL.name())) {
				if ( remedialAction.equals(Remediation.ITEM_DELETE_MAIL.name())) {
					
					if(emailObj==null){
						remediationSuccess=true;
						
					}
					
				} 
				
				
				if (remediationSuccess == true) {
					break;
				}
				else{
					Reporter.log("Mail/Attachment not yet deleted",true);
				}
				
			}
			
			Reporter.log("Mail deletion successful at GMAIL");
			
			countAfterRemediation = countAfterExposure ;
			i = 60000;
			for (; i <= (MAX_REMEDIATION_WAIT_TIME_IN_MINUTES * 60 * 1000); i+=60000 ) {
				Reporter.log("Wait for "+TimeUnit.MILLISECONDS.toMinutes(i)+" minutes, and check if the file:"+myMailSubject +" is removed from exposure.",true);
				Reporter.log("------------------------------------------------------------------------",true );
				Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
				mailExpoDoc = this.getExposedDocuments(elapp.el_office_365.name(), qparamsForMail);
				countAfterRemediation = mailExpoDoc.getMeta().getTotalCount();
				
				if (countAfterRemediation == 0) {
					break;
				}
				
			}
			
			
			Reporter.log("###### exposure count after mail deletion::"+countAfterRemediation, true);
			assertEquals(countAfterRemediation, 0, "Exposure count has not reduced after mail deletion from both users inbox..");
			//Check the user and collaborator list
			usersDoc1 = getCollaborators(elapp.el_office_365.name(), qparamsForMail);
			assertEquals(usersDoc1.getMeta().getTotalCount(), 0, "Collaborator list is still not empty after deletion of email.");			
			usersDoc2 = getExposedUsers(elapp.el_office_365.name(), qparamsForMail);
			assertEquals(usersDoc2.getMeta().getTotalCount(), 0,"Owner list is still not empty after deletion of email.");
			
			for (; i <= (MAX_REMEDIATION_WAIT_TIME_IN_MINUTES * 60 * 1000); i+=60000 ) {
				Reporter.log("Wait for "+TimeUnit.MILLISECONDS.toMinutes(i)+" minutes, and check if the file:"+searchFileName1 +" is removed from exposure.",true);
				Reporter.log("------------------------------------------------------------------------",true );
				Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
				exposureDoc1 = this.getExposedDocuments(elapp.el_office_365.name(), qparamsForFile1);
				countAfterRemediation = exposureDoc1.getMeta().getTotalCount();
				
				if (countAfterRemediation == 0) {
					break;
				}
				
			}
			Reporter.log("###### exposure count after mail deletion::"+countAfterRemediation, true);
			assertEquals(countAfterRemediation, 0, "Exposure count has not reduced after mail deletion from both users inbox..");
			//Check the user and collaborator list
			usersDoc1 = getCollaborators(elapp.el_office_365.name(), qparamsForFile1);
			assertEquals(usersDoc1.getMeta().getTotalCount(), 0,"Collaborator list is still not empty after deletion of email.");		
			usersDoc2 = getExposedUsers(elapp.el_office_365.name(), qparamsForFile1);
			assertEquals(usersDoc2.getMeta().getTotalCount(), 0,"Owner list is still not empty after deletion of email.");
			
			for (; i <= (MAX_REMEDIATION_WAIT_TIME_IN_MINUTES * 60 * 1000); i+=60000 ) {
				Reporter.log("Wait for "+TimeUnit.MILLISECONDS.toMinutes(i)+" minutes, and check if the file:"+searchFileName2 +" is removed from exposure.",true);
				Reporter.log("------------------------------------------------------------------------",true );
				Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
				exposureDoc2 = this.getExposedDocuments(elapp.el_office_365.name(), qparamsForFile2);
				countAfterRemediation = exposureDoc2.getMeta().getTotalCount();
				
				if (countAfterRemediation == 0) {
					break;
				}
				
			}
			
			Reporter.log("###### exposure count after mail deletion::"+countAfterRemediation, true);
			assertEquals(countAfterRemediation, 0, "Exposure count has not reduced after mail deletion from both users inbox..");
			//Check the user and collaborator list
			usersDoc1 = getCollaborators(elapp.el_office_365.name(), qparamsForFile2);
			assertEquals(usersDoc1.getMeta().getTotalCount(), 0, "Collaborator list is still not empty after deletion of email.");		
			usersDoc2 = getExposedUsers(elapp.el_office_365.name(), qparamsForFile2);
			assertEquals(usersDoc2.getMeta().getTotalCount(), 0,"Owner list is still not empty after deletion of email.");
			
			
			
		}
		
		@DataProvider()
		public Object[][] dataProviderI18N() {
			return new Object[][]{
				//Language, locale, 	 i18n String  
				{ "French",  	"FR_FR",  I18N.getString("language", "fr_fr")}, 
				{ "Spanish", 	"ES_ES",  I18N.getString("language", "es_es")}, 
				{ "German",  	"DE_DE",  I18N.getString("language", "de_de")}, 
				{ "Portuguese", "PT_BR",  I18N.getString("language", "pt_br")}, 
				{ "Japanese", 	"JA_JP",  I18N.getString("language", "ja_jp")},
				{ "Chinese",	"ZH_CN",  I18N.getString("language", "zh_cn")},
				{ "Tamil",		"TA_IN",  I18N.getString("language", "ta_in")},
				{ "Urdu", 		"UR_IN",  I18N.getString("language", "ur_in")},
			};
		}
		
		@Test(  groups={"i18n","P1"})
		public void performSendMailWithI18n() throws IOException, InterruptedException {

			Reporter.log("****************Test Case Description****************",true);
			Reporter.log("This method sends  mails with file name containing i18n strings "
					+ "The verification method checks if the logs appear with i18n strings.", true);
			Reporter.log("*****************************************************",true);

			String fileName = "simpleTextFile.txt"; 
			String language, locale;
			String myUniqueId ;
			String i18nString;
			boolean success = false;
			//generating unique id
			myUniqueId = uniqueId7 ;
			String newFileName =null;
			File sourceFile1;
			File destFile1 = null;
			String mailBody=null;
			ArrayList<String> myAttachment = new ArrayList<String>() ;
			String myMailSubject=null;
			ArrayList<String> logs =null;
			ArrayList<String> logs2 =null;
			int expectedLogCount=0;


			deleteAllCIQProfiles();
			
			Object[][] myFileList =dataProviderI18N();
			expectedLogCount= myFileList.length * 2;
			Logger.info("expectedLogCount======"+expectedLogCount);

			for (Object[] earchRow : myFileList) {

				try {
					myAttachment.clear();
					language=earchRow[0].toString();
					locale=earchRow[1].toString();
					i18nString=earchRow[2].toString();

					Reporter.log("Sending mail with language:"+language+". locale:"+locale+". i18n string:" +i18nString,true);
					sourceFile1 = new File(userDir +fileName);

					myMailSubject = myUniqueId+i18nString;
					newFileName = myUniqueId;//+i18nString; Disabled i18n attachment name temporarily

					destFile1 = new File(userDir +newFileName);

					//Creating file with unique id name for upload
					copyFileUsingFileChannels(sourceFile1, destFile1);
					filesToDelete.add(destFile1);
					myAttachment.add(destFile1.toString());
					mailBody= "This is test mail body"+i18nString;
					
					//Update the attachment with i18n text
					Files.write(destFile1.toPath(), i18nString.getBytes(), StandardOpenOption.APPEND);

					Thread.sleep(10000);
					cleanupListSent.add(myMailSubject);
					synchronized(this){ success = objMailTestUser1.sendMail(testUser2, myMailSubject,mailBody ,myAttachment , true);}
					assertTrue(success, "Failed sending mail with subject:"+myMailSubject+".");
					success=false;


				} catch (Exception e) {
					// TODO Auto-generated catch block
					Reporter.log(e.getMessage());
					e.printStackTrace();
				}


			}

			try {
				actualMailMessages5.clear();

				Thread.sleep(120000);

				for (int i = 180000; i <= (MAX_LOG_WAIT_TIME_IN_MINUTES * 60 * 1000); i+=60000 ) {
					Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
					Reporter.log("Searching for logs after :"+TimeUnit.MILLISECONDS.toMinutes(i)+" minutes",true );
					Reporter.log("------------------------------------------------------------------------",true );

					logs2 = searchDisplayLogs(fromTime, 60, "Office 365","Object_type", "Email_Message", myUniqueId, suiteData.getUsername(), suiteData.getCSRFToken(), suiteData.getSessionID() ,testUser1); // this is for virus file scanning in sent items
					actualMailMessages5.addAll(logs2);
					Thread.sleep(3000);
//					logs2 = searchDisplayLogs(fromTime, 60, "Office 365","Object_type", "Email_Message", myUniqueId, suiteData.getUsername(), suiteData.getCSRFToken(), suiteData.getSessionID() ,testUser2); // this is for virus file scanning in sent items
//					actualMailMessages5.addAll(logs2);
//					Thread.sleep(3000);
					logs2 = searchDisplayLogs(fromTime, 60, "Office 365","Object_type", "Email_File_Attachment", myUniqueId, suiteData.getUsername(), suiteData.getCSRFToken(), suiteData.getSessionID() ,testUser1);
					actualMailMessages5.addAll(logs2);
					Thread.sleep(3000);
//					logs2 = searchDisplayLogs(fromTime, 60, "Office 365","Object_type", "Email_File_Attachment", myUniqueId, suiteData.getUsername(), suiteData.getCSRFToken(), suiteData.getSessionID() ,testUser2);
//					actualMailMessages5.addAll(logs2);
//					Thread.sleep(3000);
//					logs = searchDisplayLogs(fromTime, 60, "Office 365","Activity_type", "Content Inspection", "Email message "+myUniqueId, suiteData.getUsername(), suiteData.getCSRFToken(), suiteData.getSessionID() ,testUser2);
//					actualMailMessages2.addAll(logs);
//					Thread.sleep(3000);
//					logs = searchDisplayLogs(fromTime, 60, "Office 365","Activity_type", "Content Inspection", "Email message "+myUniqueId, suiteData.getUsername(), suiteData.getCSRFToken(), suiteData.getSessionID() ,testUser1);
//					actualMailMessages2.addAll(logs);
					Set<String> hs = new HashSet<>();
					hs.addAll(actualMailMessages5);
					actualMailMessages5.clear();
					actualMailMessages5.addAll(hs);

					displayMessageContent(actualMailMessages5);
					if (actualMailMessages5.size() >= expectedLogCount) {break;}  
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Reporter.log(e.getMessage());
				e.printStackTrace();
			}



		}
		
		

		@Test( dependsOnMethods="performSendMailWithI18n",groups={"i18n","P1"},dataProvider="dataProviderI18N")
		public void verifySendMailWithI18n(String language, String locale, String i18nString) {
			
			Reporter.log("****************Test Case Description****************",true);
			Reporter.log("Send Mail actions are done in the dependent test - performSendMailWithI18n",true);
			Reporter.log("The verification method checks if the logs appear with i18n strings in mail subject and attachment name. ");
			Reporter.log("Verifying for language:"+ language + " and i18n string:"+i18nString, true);
			Reporter.log("*****************************************************",true);
			
			String myUniqueId = uniqueId7 ;
			String myMailSubject  = myUniqueId+i18nString, expectedLog =null, mailAttachmentName=myUniqueId+i18nString; 
			ArrayList<String> ExpectedLogs = new ArrayList<String>();
			
			try {
				displayMessageContent(actualMailMessages5);


				ExpectedLogs.add("User sent an email  to "+testUser2+" with subject \""+myMailSubject+"\"");
				ExpectedLogs.add("User sent an email  to "+testUser2+" with subject \""+myMailSubject+"\" with attachment \""+mailAttachmentName+"\"");
//				ExpectedLogs.add("User sent an email  to 2 users  with subject \""+myMailSubject+"\"");
//				ExpectedLogs.add("User sent an email  to 2 users  with subject \""+myMailSubject+"\" with attachment \""+mailAttachmentName+"\"");
				
//				ExpectedLogs.add("User received an email  from "+testUser1+" with subject \""+myMailSubject+"\"");
//				ExpectedLogs.add("User received an email  from "+testUser1+" with subject \""+myMailSubject+"\" with attachment \""+mailAttachmentName+"\"");
				
				assertTrue(compareResult(ExpectedLogs, actualMailMessages5),"Test failed, expected log is not found.");
				 


			}
			catch(Exception e) {}

		}
		
		@SuppressWarnings("null")
//		@Test(groups={"STRESS_TENANT"})
		public void verifyMailLogsFor20PlusUsers_40277() throws ServiceLocalException, Exception {

			Reporter.log("****************Test Case Description****************",true);
			Reporter.log("1)Send Mail a mail with attachment to 20+ internal users and 1 external user",true);
			Reporter.log("2)Verify that Only 20 received mail and received attachment logs are generated",true);
			Reporter.log("3)Verify that only 1 Sent mail and set attachment logs are generated.",true);
			Reporter.log("*****************************************************",true);

			String myUniqueId = UUID.randomUUID().toString();
			boolean success = false;
			
			
			
			EmailAddressCollection recipientsCc = new EmailAddressCollection();
			EmailAddressCollection recipientsTo = new EmailAddressCollection();
			
			for(int i=1; i<=25;i++){
				recipientsTo.add("qa-stress"+i+"@o365security.net");
			}
			
//			recipientsTo.add("qa-stress1@o365security.net");
			recipientsCc.add(externalUser1);
//			 recipientsTo.add(adminMailId);
//			 recipientsCc.add(externalUser2);
//			 recipientsCc.add(testUser2);


			//send mail to check following things
			//C702678 Send an Email to anyone and verify that activity is recorded. 
			//C878691 Verify the activity for receiving an email should be recorded.  
			//C702682 Send an Email to any internal user and verify that activity is recorded. 
			//send mail without attachment // check the receipt
			
			String subject = myUniqueId+"MailTo20plusUsers";

			String attachmentFile= "PII.rtf";
			String bodyFile= "PCI_Test.txt";
			String violationName1 ="PII";
			String violationName2 ="PCI";

			File sourceFile1 = new File(userDir +attachmentFile);
			String readFile = userDir  + bodyFile;


			String newFileName = myUniqueId+attachmentFile;
//			cleanupListSent.add(myMailSubject);

			File destFile1 = new File(userDir +newFileName);

			//Creating file with unique id name for upload
			copyFileUsingFileChannels(sourceFile1, destFile1);
			filesToDelete.add(destFile1);
			ArrayList<String> myAttachment = new ArrayList<String>();
			myAttachment.add(destFile1.toString());

			String mailBody = readFile(readFile.toString(), Charset.defaultCharset());

			synchronized(this){success= objMailSysAdmin.sendMailWithCCAndBCC(recipientsTo, recipientsCc, null, subject, "This is test mail body", myAttachment, true);}
			assertTrue(success, "Failed sending mail with subject:"+subject+".");
			success=false;

//			Thread.sleep(120000);

			//Forward mail test
			//Send a mail to self and Forward that mail

			ArrayList<String> allMessages = new ArrayList<String>();
			ArrayList<String> logs;
			String receivedMessage ="User received an email  from "+adminMailId+" with subject \""+subject+"\"";
			int i = 180000, receivedCount=0, sentCount=0;
			try {
				for (; i <= ( MAX_LOG_WAIT_TIME_IN_MINUTES * 60 * 1000); i+=60000 ) {
//					Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
					Reporter.log("------------------------------------------------------------------------",true );
					Reporter.log("Searching for logs after :"+TimeUnit.MILLISECONDS.toMinutes(i)+" minutes",true );
					Reporter.log("------------------------------------------------------------------------" ,true);
					logs = searchDisplayLogs(fromTime, 60, "Office 365","Object_type", "Email_Message", myUniqueId, suiteData.getUsername(), suiteData.getCSRFToken(), suiteData.getSessionID(),"");
					allMessages.addAll(logs);
//					logs = searchDisplayLogs(fromTime, 60, "Office 365","Activity_type", "Received", myUniqueId, suiteData.getUsername(), suiteData.getCSRFToken(), suiteData.getSessionID(),null);
//					receivedMessage.addAll(logs);
//					logs = searchDisplayLogs(fromTime, 60, "Office 365","Object_type", "Email_File_Attachment", myUniqueId, suiteData.getUsername(), suiteData.getCSRFToken(), suiteData.getSessionID() ,null);
//					myMailMessages.addAll(logs);
//					logs = searchDisplayLogs(fromTime, 60, "Office 365","Activity_type", "Content Inspection", "File "+myUniqueId, suiteData.getUsername(), suiteData.getCSRFToken(), suiteData.getSessionID() ,testUser2);
//					myMailMessages.addAll(logs);
//					logs = searchDisplayLogs(fromTime, 60, "Office 365","Activity_type", "Content Inspection", "File "+myUniqueId, suiteData.getUsername(), suiteData.getCSRFToken(), suiteData.getSessionID() ,testUser1);
//					myMailMessages.addAll(logs);
					
//					logs = searchDisplayLogs(fromTime, 60, "Office 365","Activity_type", "Content Inspection", "Email file attachment "+myUniqueId, suiteData.getUsername(), suiteData.getCSRFToken(), suiteData.getSessionID() ,testUser2);
//					myMailMessages.addAll(logs);
//					logs = searchDisplayLogs(fromTime, 60, "Office 365","Activity_type", "Content Inspection", "Email message "+myUniqueId, suiteData.getUsername(), suiteData.getCSRFToken(), suiteData.getSessionID() ,testUser2);
//					myMailMessages.addAll(logs);
//					logs = searchDisplayLogs(fromTime, 60, "Office 365","Activity_type", "Content Inspection", myUniqueId, suiteData.getUsername(), suiteData.getCSRFToken(), suiteData.getSessionID() ,null);
//					myMailMessages.addAll(logs);
//					logs = searchDisplayLogs(fromTime, 60, "Office 365","Activity_type", "Content Inspection", myUniqueId, suiteData.getUsername(), suiteData.getCSRFToken(), suiteData.getSessionID() ,null);
//					myMailMessages.addAll(logs);
					
//					Set<String> hs = new HashSet<>();
//					hs.addAll(myMailMessages);
//					myMailMessages.clear();
//					myMailMessages.addAll(hs);
					
					
					

					displayMessageContent(allMessages);

					//Reporter.log("Actual file messages:" + actualMailMessages, true);
					if (allMessages.size() >= 21 ) //&& receivedMessage.size() >=20
						{break;}
					else 
						{allMessages.clear();}
						//receivedMessage.clear();
				}
			}
			catch(Exception e) {}
			
			
			for(int j=0;j<=allMessages.size();i++){
				if(allMessages.get(i).equals(receivedMessage)){
					++receivedCount;
				}
			}

			displayMessageContent(allMessages);
			
			ArrayList<String> ExpectedLogs = new ArrayList<String>();
			
			ExpectedLogs.add("User sent an email  25 users with subject \""+subject+"\"");
			ExpectedLogs.add(receivedMessage);
//			ExpectedLogs.add("User received an email  from "+adminMailId+" with subject \""+subject+"\" with attachment \""+newFileName+"\"");
//			ExpectedLogs.add("User received an email  from "+testUser1+" with subject \""+myUniqueId+"Mail Without Attachment\"");
//			ExpectedLogs.add("Email file attachment "+newFileName+" has risk(s) - "+violationName1);
//			ExpectedLogs.add("Email message "+subject+" has risk(s) - "+violationName2);
//			
			assertTrue(compareResult(ExpectedLogs, allMessages),"Test failed, expected log is not found even after waiting for "+TimeUnit.MILLISECONDS.toMinutes(i)+" minutes.");
			assertEquals(receivedCount,20,"Test failed, as received message count doesn't match");

//			assertTrue(compareResult(ExpectedLogs, receivedMessage),"Test failed, expected log is not found even after waiting for "+TimeUnit.MILLISECONDS.toMinutes(i)+" minutes.");
			 


		}
		
			
				
			
			
	public ArrayList<String> searchElasticLogs(int from, int to, String facilty, String query, String email) throws Exception {

		Reporter.log("Retrieving the logs from Elastic Search ...", true);
		String tsfrom = DateUtils.getMinutesFromCurrentTime(from);
		String tsto   = DateUtils.getMinutesFromCurrentTime(to);

		//Get headers
		List<NameValuePair> headers = getHeaders();

		String payload = esQueryBuilder.getSearchQuery(tsfrom, tsto, facilty, query, email);

		Reporter.log("Request body:"+ payload, true);

		//HttpRequest
		String path = suiteData.getAPIMap().get("getForensicsLogs") ;
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(), path);

		HttpResponse response =  restClient.doPost(dataUri, headers, null, new StringEntity(payload));
		String responseBody = ClientUtil.getResponseBody(response);
		Reporter.log("==============================================================================");
		Reporter.log("Response body:"+ responseBody, true);

		ForensicSearchResults fsr = MarshallingUtils.unmarshall(responseBody, ForensicSearchResults.class);
		assertEquals(HttpStatus.SC_OK, getResponseStatusCode(response), "Response code verification failed.");
		return this.retrieveActualMessages(fsr);
	}


	/**
	 * @param from
	 * @param to
	 * @param facilty
	 * @param termType
	 * @param termValue
	 * @param query
	 * @param email
	 * @param csrfToken
	 * @param sessionId
	 * @param searchForUser
	 * @return
	 * @throws Exception
	 */
	public ArrayList<String> searchDisplayLogs(String from, int to, String facilty,String termType, String termValue, String query, String email, String csrfToken, String sessionId, String searchForUser) throws Exception {

		String apiServerUrl = suiteData.getScheme() + "://" + suiteData.getApiserverHostName();
		
		DateTime currentTime = DateTime.now(DateTimeZone.UTC);
		
		Reporter.log("==============================================================================");
		Reporter.log("UTC time:"+currentTime);

		Reporter.log("Retrieving the logs from Elastic Search ...", true);
		String tsfrom = from;//DateUtils.getMinutesFromCurrentTime(from);
		String tsto   = DateUtils.getMinutesFromCurrentTime(to);

		//Get headers
		List<NameValuePair> headers = getHeaders();

		String payload = esQueryBuilder.getSearchQueryForDisplayLogsOffice365(tsfrom, tsto, facilty, termType,  termValue, query, email, apiServerUrl, csrfToken, sessionId, searchForUser);


		//HttpRequest
		String path = suiteData.getAPIMap().get("getInvestigateLogs") ;
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(), path);

//		Reporter.log("My url =========="+dataUri.getHost()+dataUri.getPath(),true);
		
		HttpResponse response =  restClient.doPost(dataUri, headers, null, new StringEntity(payload));
		String responseBody = ClientUtil.getResponseBody(response);
		Reporter.log("Request payload:"+ payload, true);
		Reporter.log("================");
		Reporter.log("Response code:"+ response.getStatusLine().getStatusCode()+ " "+ response.getStatusLine().getReasonPhrase(), true);
		Reporter.log("Response body:"+ responseBody, true);

		assertEquals( getResponseStatusCode(response),HttpStatus.SC_OK, "Response code verification failed.");
		ForensicSearchResults fsr = MarshallingUtils.unmarshall(responseBody, ForensicSearchResults.class);
		return this.retrieveActualMessages(fsr);
	}
	
	/**
	 * @param from
	 * @param to
	 * @param facilty
	 * @param termType
	 * @param termValue
	 * @param query
	 * @param email
	 * @param csrfToken
	 * @param sessionId
	 * @param searchForUser
	 * @return
	 * @throws Exception
	 */
	public String searchInvestigateLogs(int from, int to, String facilty,String termType, String termValue, String query, String email, String csrfToken, String sessionId, String searchForUser) throws Exception {

		String apiServerUrl = suiteData.getScheme() + "://" + suiteData.getApiserverHostName();
		
		DateTime currentTime = DateTime.now(DateTimeZone.UTC);
		
		Reporter.log("==============================================================================");
		Reporter.log("UTC time:"+currentTime);

		Reporter.log("Retrieving the logs from Elastic Search ...", true);
		String tsfrom = DateUtils.getMinutesFromCurrentTime(from);
		String tsto   = DateUtils.getMinutesFromCurrentTime(to);

		//Get headers
		List<NameValuePair> headers = getHeaders();

		String payload = esQueryBuilder.getSearchQueryForDisplayLogsOffice365(tsfrom, tsto, facilty, termType,  termValue, query, email, apiServerUrl, csrfToken, sessionId, searchForUser);


		//HttpRequest
		String path = suiteData.getAPIMap().get("getInvestigateLogs") ;
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(), path);

//		Reporter.log("My url =========="+dataUri.getHost()+dataUri.getPath(),true);
		
		HttpResponse response =  restClient.doPost(dataUri, headers, null, new StringEntity(payload));
		String responseBody = ClientUtil.getResponseBody(response);
		Reporter.log("Request payload:"+ payload, true);
		Reporter.log("================");
		Reporter.log("Response code:"+ response.getStatusLine().getStatusCode()+ " "+ response.getStatusLine().getReasonPhrase(), true);
		Reporter.log("Response body:"+ responseBody, true);

		assertEquals( getResponseStatusCode(response),HttpStatus.SC_OK, "Response code verification failed.");
		return responseBody;
	}


	private ArrayList<String> retrieveActualMessages(ForensicSearchResults fsr) {
		ArrayList<String> alist = new ArrayList<String>();
		//Reporter.log("Messages List");
		//Reporter.log("----------------------");
		String msg = "";
		for (Hit hit : fsr.getHits().getHits()) {
			Source source  = hit.getSource();
			msg = source.getMessage();//.replace("\\", "");
			alist.add(msg);
			//Reporter.log(msg,true);
		}
		return alist;
	}

	public static void copyFileUsingFileChannels(File source, File dest)
			throws IOException {
		FileChannel inputChannel = null;
		FileChannel outputChannel = null;
		try {
			inputChannel = new FileInputStream(source).getChannel();
			outputChannel = new FileOutputStream(dest).getChannel();
			outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
		} finally {
			inputChannel.close();
			outputChannel.close();
		}
	}
	void displayMessageContent(ArrayList<String> myMessages)
	{
		if(myMessages.size()>=1){
			Iterator itr = null;

			itr = myMessages.iterator();
			int i=1;
			Reporter.log("Available logs",true);
			Reporter.log("-----------------------------",true);
			while(itr.hasNext())
			{
				Reporter.log(i+") "+itr.next().toString(),true);
				i++;
			}
			Reporter.log("-----------------------------",true);
		}
		else{
			Reporter.log("----------No logs found----------",true);
		}
	}
	
	static String readFile(String path, Charset encoding) 
			  throws IOException 
			{
			  byte[] encoded = Files.readAllBytes(Paths.get(path));
			  return new String(encoded, encoding);
			}
	
	public O365Document getExposures(String isInternal, String ownedBy, ArrayList<String> vltypes,String fileName) throws Exception {
		List<NameValuePair> headers = getHeaders();
		
		List<NameValuePair> qparams = new ArrayList<NameValuePair>(); 
		qparams.add(new BasicNameValuePair("is_internal", isInternal));
		//qparams.add(new BasicNameValuePair("owned_by",  ownedBy )); //UrlUtils.decode(suiteData.getUsername())));
		qparams.add(new BasicNameValuePair("content_checks.vl_types",  UrlUtils.decode(StringUtils.join(vltypes, ","))));
		qparams.add(new BasicNameValuePair("name", fileName));
		//qparams.add(new BasicNameValuePair("content_checks.vk_content_iq_violations",  "test_ciq_profile"));
		
		String path = suiteData.getAPIMap().get("getO365Documents").
				replace("{tenant}", suiteData.getTenantName()).
				replace("{version}", suiteData.getBaseVersion());
		
		//System.out.println("Path:" + path);
		URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(), path, qparams);
		HttpResponse response =  restClient.doGet(uri, headers);
		String responseBody = ClientUtil.getResponseBody(response);
		
		Logger.info("Response body:"+ responseBody);
		O365Document o365Document = MarshallingUtils.unmarshall(responseBody, O365Document.class);
		return o365Document;
	}
	public O365Document searchExposures(String searchKeyword) throws Exception {
		List<NameValuePair> headers = getHeaders();

		List<NameValuePair> qparams = new ArrayList<NameValuePair>(); 
//		qparams.add(new BasicNameValuePair("is_internal", isInternal));
		//qparams.add(new BasicNameValuePair("owned_by",  ownedBy )); //UrlUtils.decode(suiteData.getUsername())));
//		qparams.add(new BasicNameValuePair("content_checks.vl_types",  UrlUtils.decode(StringUtils.join(vltypes, ","))));
		qparams.add(new BasicNameValuePair("search", searchKeyword));
		//qparams.add(new BasicNameValuePair("content_checks.vk_content_iq_violations",  "test_ciq_profile"));

		String path = suiteData.getAPIMap().get("getO365Documents").
				replace("{tenant}", suiteData.getTenantName()).
				replace("{version}", suiteData.getBaseVersion());

		//System.out.println("Path:" + path);
		URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(), path, qparams);
		HttpResponse response =  restClient.doGet(uri, headers);
		String responseBody = ClientUtil.getResponseBody(response);

		Logger.info("Response body:"+ responseBody);
		O365Document o365Document = MarshallingUtils.unmarshall(responseBody, O365Document.class);
		return o365Document;
	}
	
	public O365Document getRiskyDocs(String isInternal, String ownedBy, ArrayList<String> vltypes,String fileName) throws Exception {
		List<NameValuePair> headers = getHeaders();
		
		List<NameValuePair> qparams = new ArrayList<NameValuePair>(); 
		qparams.add(new BasicNameValuePair("is_internal", isInternal));
		//qparams.add(new BasicNameValuePair("owned_by",  ownedBy )); //UrlUtils.decode(suiteData.getUsername())));
		qparams.add(new BasicNameValuePair("content_checks.vl_types",  UrlUtils.decode(StringUtils.join(vltypes, ","))));
		qparams.add(new BasicNameValuePair("name", fileName));
		//qparams.add(new BasicNameValuePair("content_checks.vk_content_iq_violations",  "test_ciq_profile"));
		
		String path = suiteData.getAPIMap().get("getRiskyDocuments")
				.replace("{tenant}", suiteData.getTenantName())
				.replace("{version}", suiteData.getBaseVersion())
				.replace("{elappname}", "el_office_365");
		
		//System.out.println("Path:" + path);
		URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(), path, qparams);
		HttpResponse response =  restClient.doGet(uri, headers);
		String responseBody = ClientUtil.getResponseBody(response);
		
		Logger.info("Response body:"+ responseBody);
		O365Document o365Document = MarshallingUtils.unmarshall(responseBody, O365Document.class);
		return o365Document;
	}
	public O365Document searchRiskyDocs(String searchKeyword) throws Exception {
		List<NameValuePair> headers = getHeaders();

		List<NameValuePair> qparams = new ArrayList<NameValuePair>(); 
//		qparams.add(new BasicNameValuePair("is_internal", isInternal));
		//qparams.add(new BasicNameValuePair("owned_by",  ownedBy )); //UrlUtils.decode(suiteData.getUsername())));
//		qparams.add(new BasicNameValuePair("content_checks.vl_types",  UrlUtils.decode(StringUtils.join(vltypes, ","))));
		qparams.add(new BasicNameValuePair("search", searchKeyword));
		//qparams.add(new BasicNameValuePair("content_checks.vk_content_iq_violations",  "test_ciq_profile"));

		String path = suiteData.getAPIMap().get("getRiskyDocuments")
				.replace("{tenant}", suiteData.getTenantName())
				.replace("{version}", suiteData.getBaseVersion())
				.replace("{elappname}", "el_office_365");

		//System.out.println("Path:" + path);
		URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(), path, qparams);
		HttpResponse response =  restClient.doGet(uri, headers);
		String responseBody = ClientUtil.getResponseBody(response);

		Logger.info("Response body:"+ responseBody);
		O365Document o365Document = MarshallingUtils.unmarshall(responseBody, O365Document.class);
		return o365Document;
	}
	
	public O365Document getExposedUsers(String userType, String isInternal, String ownedBy, ArrayList<String> vltypes,String fileName) throws Exception {
		List<NameValuePair> headers = getHeaders();

		List<NameValuePair> qparams = new ArrayList<NameValuePair>(); 
		qparams.add(new BasicNameValuePair("is_internal", isInternal));
		//qparams.add(new BasicNameValuePair("owned_by",  ownedBy )); //UrlUtils.decode(suiteData.getUsername())));
		qparams.add(new BasicNameValuePair("content_checks.vl_types",  UrlUtils.decode(StringUtils.join(vltypes, ","))));
		qparams.add(new BasicNameValuePair("name", fileName));
		//qparams.add(new BasicNameValuePair("content_checks.vk_content_iq_violations",  "test_ciq_profile"));
		String path = null;
		if(userType.equals("users")){
			path = suiteData.getAPIMap().get("getExposedUsers").
					replace("{tenant}", suiteData.getTenantName()).
					replace("{version}", suiteData.getBaseVersion()).
					replace("{elappname}", "el_office_365");
		}
		else if(userType.equals("collab")){
		 path = suiteData.getAPIMap().get("getCollaborators").
				replace("{tenant}", suiteData.getTenantName()).
				replace("{version}", suiteData.getBaseVersion()).
				replace("{elappname}", "el_office_365");
		}

		//System.out.println("Path:" + path);
		URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(), path, qparams);
		HttpResponse response =  restClient.doGet(uri, headers);
		String responseBody = ClientUtil.getResponseBody(response);

		Logger.info("Response body:"+ responseBody);
		O365Document o365Document = MarshallingUtils.unmarshall(responseBody, O365Document.class);
		return o365Document;
	}
	
	public void remediateExposure(String tenant, String facility,  String user, String documentId, String userId, String action) throws Exception {

		List<NameValuePair> headers = getHeaders();
		String payload = "";

		if(action.equals(Remediation.ITEM_DELETE_MAIL_BY_ATTACHMENT.name())) {
			payload="{\"source\":{\"objects\":{\"objects\":[{\"db_name\":\""+tenant+"\",\"user\":\""+user+"\",\"user_id\":"+userId+",\"doc_id\":\""+documentId+"\",\"doc_type\":\"Email_File_Attachment\","
					+ "\"actions\":[{\"code\":\""+action+"\",\"object_type\": \"Mail\",\"possible_values\":[],\"meta_info\":{}}], \"object_type\": \"Mail\"}]},\"app\":\"Office 365\"}}";
		} else {

			payload="{\"source\":{\"objects\":{\"objects\":[{\"db_name\":\""+tenant+"\",\"user\":\""+user+"\",\"user_id\":\""+userId+"\",\"doc_id\":\""+documentId+"\",\"doc_type\":\"file\","
					+ "\"actions\":[{\"code\":\""+action+"\",\"possible_values\":[],\"meta_info\":{\"current_link\":\"open\"}}]}]},\"app\":\""+facility+"\"}}";
		}
		
		Reporter.log("headers="+headers.toString(),true);
		Reporter.log("payload="+payload, true);
		
		//System.exit(0);

		StringEntity stringEntity = new StringEntity(payload);
		String path = suiteData.getAPIMap().get("getBoxUIRemediation").
				replace("{tenant}", suiteData.getTenantName()).
				replace("{version}", suiteData.getBaseVersion());

		//suiteData.getApiserverHostName()
		URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(), path, null);
		HttpResponse response =  restClient.doPost(uri, headers, null, stringEntity);
		String responseBody = ClientUtil.getResponseBody(response);

		Logger.info("Response body:"+ responseBody);		
	}

	/**
	 * This is the utility method to remediate the exposure thro' api. 
	 * @param tenant
	 * @param facility
	 * @param user
	 * @param documentId
	 * @param userId
	 * @param action
	 * @throws Exception
	 */
	public int remediateExposureWithAPI(MailRemediation remediationObject) throws Exception {

		List<NameValuePair> headers = getHeaders();

		headers.add(new BasicNameValuePair(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON));

		String payload = "{\"objects\":[" + MarshallingUtils.marshall(remediationObject) + "]}";

		StringEntity stringEntity = new StringEntity(payload);
		String path = suiteData.getAPIMap().get("getO365Remediation")
				.replace("{tenant}", suiteData.getTenantName())
				.replace("{version}", suiteData.getBaseVersion());

		URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(), path, null);
		HttpResponse response =  restClient.doPatch(uri, headers, null, stringEntity);
		String responseBody = ClientUtil.getResponseBody(response);
		Reporter.log("Request body:" + payload, true);
		Reporter.log("************************************************");
		Reporter.log("Response status:"+ response.getStatusLine().getStatusCode() + " "+ response.getStatusLine().getReasonPhrase(), true);
		Reporter.log("Response body:"+ responseBody, true);
		
		return response.getStatusLine().getStatusCode();
	}
	
	private MailRemediation getRemediationObject(String user, String docType, String docId, String remedialAction) {
		MailRemediation o365Remediation = new MailRemediation();

		o365Remediation.setDbName(suiteData.getTenantName());
		o365Remediation.setUser(user);
//		o365Remediation.setUserId(userId);
		o365Remediation.setDocType(docType);
		o365Remediation.setDocId(docId);
		o365Remediation.setObjectType("Mail");
		

		List<String> possibleValues = new ArrayList<String>();
//		if(remedialAction.equals("UNSHARE")) {
//			possibleValues.add("open"); possibleValues.add("company"); possibleValues.add("collaborators");
//		}

		//Meta Info
//		BoxMetaInfo boxMetaInfo = new BoxMetaInfo();
//
//		if (remedy != null) {
//			if(remedialAction.equals("SHARE_EXPIRE")) {
//				boxMetaInfo.setCurrentLink(null);
//				boxMetaInfo.setCollabs(null);
//				boxMetaInfo.setExpireOn(remedy);
//			} else {
//				boxMetaInfo.setAccess(remedy);
//			}
//		}
//
//		if(currentLink != null) {
//			boxMetaInfo.setCurrentLink(currentLink);
//		}
		
		
		
		List<MailAction> actions = new ArrayList<MailAction>();
		MailAction o365Action = new MailAction();
		o365Action.setCode(remedialAction);
		o365Action.setObjectType("Mail");
		o365Action.setPossibleValues(possibleValues);

		o365Action.setMetaInfo(null);
		actions.add(o365Action);
		o365Remediation.setActions(actions);
		return o365Remediation;
	}
	
	public String getDocID(String fileName) throws Exception {
		
		String docId =null;
		List<NameValuePair> qparams = new ArrayList<NameValuePair>(); 
		qparams.add(new BasicNameValuePair("name", fileName));
		
		SecurletsUtils securletUtils = new SecurletsUtils();
		String path = suiteData.getAPIMap().get("getO365Documents")
				.replace("{tenant}", suiteData.getTenantName())
				.replace("{version}", suiteData.getBaseVersion());

		URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(), path, qparams);
		HttpResponse response =  restClient.doGet(uri, getHeaders());
		String responseBody = ClientUtil.getResponseBody(response);
		
		Reporter.log("Response status:"+ response.getStatusLine().getStatusCode() + " "+ response.getStatusLine().getReasonPhrase(), true);
		Reporter.log("Response body:"+ responseBody, true);
		
		O365Document o365Document = MarshallingUtils.unmarshall(responseBody, O365Document.class);
		
		if(o365Document.getMeta().getTotalCount()>=1){
			docId = o365Document.getObjects().get(0).getIdentification();
		}
		return docId;
		

//		Reporter.log(response.getStatusLine().toString(),true);
//		Reporter.log(responseBody,true);
//		String query = "$..identification";
//
//		String filterSingleResultFromResponse = securletUtils.filterSingleResultFromResponse(responseBody, query);
//		return filterSingleResultFromResponse;
	}

//	@Test()
	public void retestVerifyRemediationForOfficeMailExposure() throws Exception
	{
		verifyRemediationForOfficeMailExposure("sanity","true", 	"open", 	"source_code",	"Hello.java",     	"Source Code", 				"attachment",   "ITEM_DELETE_ATTACHMENT", 		"Send an Email having Source Code level information"  );
		
	}
	
	//this is test method for trying out codes
//	@Test( groups={"test"})
	public void query1() throws Exception {	
		
		EmailAddressCollection toEmail = new EmailAddressCollection();
//		EmailAddressCollection ccEmail = new EmailAddressCollection();
//		EmailAddressCollection bccEmail = new EmailAddressCollection();
//		toEmail.add(testUser2);
//		ccEmail.add(userName);
		toEmail.add("abdul.nissar@elastica.co");
//		
//		assertTrue(objMailTestUser1.sendMail(toEmail, ccEmail, bccEmail, "Test mail", "body", null, true));
//		
//		File file = new File(userDir + "hipaa.txt");
//		FileReader fileReader = new FileReader(file);
//		BufferedReader bufferedReader = new BufferedReader(fileReader);
//		StringBuffer stringBuffer = new StringBuffer();
//		String line;
//		while ((line = bufferedReader.readLine()) != null) {
//			stringBuffer.append(line);
//			stringBuffer.append("\n");
//		}
//		fileReader.close();
//		System.out.println("Contents of file:");
		ArrayList<String> myAttachments = new ArrayList<String> ();
		myAttachments.add(userDir+"LogoElastica.png");
		myAttachments.add(userDir+"LogoCloudSoc.png");
		
		File sourceFile1 = new File(userDir +"mailWithLogo.txt");
		String mailBody =readFile(sourceFile1.toString(), Charset.defaultCharset());
		System.out.println(mailBody);
		
		synchronized(this){objMailTestUser1.sendMailWithInLineAttachment(toEmail, null, null, "mailwithlogo", myAttachments, true);}
		
//		 objMailTestUser1.sendMail("abdul.nissar@elastica.co", "11111",mailBody ,null , true);
		//Reporter.log(readFile(file.toString(), Charset.defaultCharset()),true);
		
//		 objMailTestUser1.sendMail(testUser2, "2222",readFile(file.toString(), Charset.defaultCharset()) ,null , true);
		
//		ArrayList<String> ExpectedLogs = new ArrayList<String>(); 
//		ArrayList<String> myMailMessages = new ArrayList<String>(); 
//		ExpectedLogs.add("number 1");
//		ExpectedLogs.add("number 2");
//		ExpectedLogs.add("number 3");
//		
//		myMailMessages.add("number 1");
//		myMailMessages.add("number 2");
//		myMailMessages.add("number 4");
// 
//		assertTrue(compareResult(ExpectedLogs, myMailMessages),"Test failed, expected log is not found");
		
	}
	
	//this is test method for trying out codes
//	@Test
	public void query2() throws Exception {	
		String subject = null;
		boolean success = false;
		//send mail to check following things
		//C702678 Send an Email to anyone and verify that activity is recorded. 
		subject = uniqueId1+"Mail Without Attachment";
		
		for(int i=1;i<=1200;i++)
		{
		//save mail in draft
		//C702684	Save an Email to Draft and verify if the activity has been recorded.
		subject =uniqueId1+"MailSavedInDraft";
		synchronized(this){ success = objMailSysAdmin.saveToDraft(subject, "This is test mail body");}
		assertTrue(success, "Failed saving mail in draft with subject:"+subject+".");
		success=false;
		cleanupListDrafts.add(subject);
		}
		
	
	}
	
	/**
	 * @param expectedLogs
	 * @param ActualLogs
	 * @return
	 */
	public boolean compareResult(ArrayList<String> expectedLogs, ArrayList<String> ActualLogs){
		String logStatus =null;
		boolean testPassed = true;
		
		Reporter.log("Expected logs",true);
		Reporter.log("-----------------------------",true);
		
		int i=0;
		for(String expectedLog : expectedLogs){
			i++;
			logStatus = (ActualLogs.contains(expectedLog)) ? "Passed" : "Failed, log not found";
			Reporter.log(i + ") " +expectedLog + " : "+ logStatus, true);
			
			if(logStatus.equals("Failed, log not found")){
				testPassed = false;
			}
		}
		
		return testPassed;
		
	}
	/**
	 * @param expectedLogs
	 * @param ActualLogs
	 * @return
	 */
	public EmailAddressCollection	changeToEmailCollection(EmailAddress mailAddress){
		EmailAddressCollection tempColl = new EmailAddressCollection();
		
		tempColl.add(mailAddress);
		return tempColl;
		
	}
	/**
	 * @param expectedLogs
	 * @param ActualLogs
	 * @return
	 */
	public boolean checkFieldInResponse(String response, String parentNode, Map<String, Object> expectedFields){
		boolean testPassed = false;
		
		Reporter.log("-----------------------------",true);
		
		   SecurletsCommonUtils securletsUtils=new SecurletsCommonUtils(); 
//	       testPassed = securletsUtils.filterFieldsFromResponse(response, parentNode, expectedFields);
		   testPassed=  RawJsonParser.findExpectedKeysAndValues(response, parentNode, expectedFields);
		
		
		return testPassed;
		
	}
	

	
	/**
	 * @param expectedLogs
	 * @param ActualLogs
	 * @return
	 */
	@SuppressWarnings("null")
	public ArrayList<String> searchLogs(String filterByKey,String filterByValue, String searchQuery, String searchForUser, int countExpected){

		ArrayList<String> logs = new ArrayList<String>();
		ArrayList<String> resultsArray = new ArrayList<String>();
		Set<String> hs = new HashSet<>();
		try {
			for (int i = 60000; i <= ( MAX_LOG_WAIT_TIME_IN_MINUTES * 60 * 1000); i+=60000 ) {

				Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
				Reporter.log("------------------------------------------------------------------------",true );
				Reporter.log("Searching for logs after :"+TimeUnit.MILLISECONDS.toMinutes(i)+" minutes",true );
				Reporter.log("------------------------------------------------------------------------",true );

				logs = searchDisplayLogs(fromTime, 60, "Office 365",filterByKey, filterByValue, searchQuery, suiteData.getUsername(), suiteData.getCSRFToken(), suiteData.getSessionID(),searchForUser);
				resultsArray.addAll(logs);
				hs = new HashSet<>();
				hs.addAll(resultsArray);
				resultsArray.clear();
				resultsArray.addAll(hs);

				displayMessageContent(resultsArray);

				//Reporter.log("Actual file messages:" + actualMailMessages, true);
				if (resultsArray.size() >= countExpected) {break;}
			}
			


		}
		catch(Exception e) {}

		if(resultsArray.size()==0){
			resultsArray =null;
		}
		return resultsArray;
	}
	
	public void deleteAllCIQProfiles() {
        try {
        	DCIFunctions  dciFunctions = new  DCIFunctions();
            HttpResponse listContentIQResponse = dciFunctions.listContentIQProfile(restClient, dciFunctions.getCookieHeaders(suiteData), 
                    suiteData.getScheme(), suiteData.getHost());
            dciFunctions.deleteAllContentIQProfiles(restClient, listContentIQResponse, dciFunctions.getCookieHeaders(suiteData), 
                    suiteData.getScheme(), suiteData.getHost());
            
        } catch (Exception ex) {
            Logger.info("Issue with Deleting of all ContentIQ Profiles" + ex.getLocalizedMessage());
        }
    }


	
	
	
	
}