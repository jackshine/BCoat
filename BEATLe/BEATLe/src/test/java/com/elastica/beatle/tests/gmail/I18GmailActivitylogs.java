package com.elastica.beatle.tests.gmail;

import static org.testng.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.UUID;

import javax.mail.internet.MimeMessage;

import org.apache.commons.io.FilenameUtils;
import org.testng.Reporter;
import org.testng.SkipException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.elastica.beatle.DateUtils;
import com.elastica.beatle.MarshallingUtils;
import com.elastica.beatle.dci.DCIConstants;
import com.elastica.beatle.securlets.LogUtils;
import com.elastica.beatle.securlets.LogValidator;
import com.elastica.beatle.securlets.SecurletUtils.ActivityType;
import com.elastica.beatle.securlets.SecurletUtils.ObjectType;
import com.elastica.beatle.securlets.SecurletUtils.Severity;
import com.elastica.beatle.securlets.SecurletUtils.facility;
import com.elastica.beatle.securlets.dto.ForensicSearchResults;
import com.elastica.beatle.tests.infra.InfraConstants;
import com.google.api.services.gmail.model.Draft;
import com.google.api.services.gmail.model.Message;
import com.universal.common.GoogleMailServices;
import com.universal.common.Office365MailActivities;
import com.universal.constants.CommonConstants;

public class I18GmailActivitylogs extends GmailUtils{
	GoogleMailServices gmailApi ;
	GoogleMailServices gmailApiTest1 ;
	GmailActivityLog gmailActivityLog;
	HashMap<String, GmailActivity> gmailActivities = new HashMap<String, GmailActivity>();
	LogValidator logValidator;
	protected ForensicSearchResults sentMessageLogs, receiveMessageLogs, createMessageLogs, 
	deleteMessageLogs, trashMessageLogs, contentInspectionLogs;
	long maxWaitTime = 15;
	long minWaitTime = 6;
    String sender="testuser1@securletbeatle.com";
    String sender1="testuser2@securletbeatle.com";
	String clientId;
	String clientSecret;
	String refreshToken;
	String clientIdTest1;
	String clientSecretTest1;
	String refreshTokenTest1;
	String createdTime;
	String testUser1;
	String testUser1Pwd;
	Office365MailActivities objMailTestUser1;
	
	public I18GmailActivitylogs() throws Exception {
		gmailActivityLog = new GmailActivityLog();
		logValidator = new LogValidator(); 
	}

	@BeforeClass(alwaysRun=true)
	public void initGmail() throws Exception {
		this.clientId 		= getRegressionSpecificSuitParameters("saasAppClientId");
		this.clientSecret 	= getRegressionSpecificSuitParameters("saasAppClientSecret");
		this.refreshToken 	= getRegressionSpecificSuitParameters("saasAppToken");
		this.gmailApi 		= new GoogleMailServices(clientId, clientSecret, refreshToken);
		Reporter.log("Gmail api initialized", true);
		//initialize for recieve event

		this.clientIdTest1 		= getRegressionSpecificSuitParameters("saasAppClientIdTest1");
		this.clientSecretTest1 	= getRegressionSpecificSuitParameters("saasAppClientSecretTest1");
		this.refreshTokenTest1 	= getRegressionSpecificSuitParameters("saasAppTokenTest1");
		this.gmailApiTest1 		= new GoogleMailServices(clientIdTest1,clientSecretTest1,refreshTokenTest1);
		Reporter.log("Gmail api initialized ", true);
		testUser1 = suiteData.getSaasAppEndUser1Name();
		testUser1Pwd = suiteData.getSaasAppEndUser1Password();
		objMailTestUser1 = new Office365MailActivities(testUser1,testUser1Pwd);
	}


	@Test(priority= -10, groups={"SEND","GMAIL", "I18","REGRESSSION"}, dataProvider = "sendPlainMailI18", dataProviderClass = GmailDataProvider.class)
	public void sendMailI18(String key, String testDesc,String sub, String messageBody,ArrayList<String> to, ArrayList<String> cc, ArrayList<String> bcc, ArrayList<String> inFolder, ArrayList<String> internalRecipients, ArrayList<String> externalRecipients,String email) throws Exception {

		LogUtils.logTestDescription(testDesc);

		String uniqueId = new String (UUID.randomUUID().toString());
		String subject = new String(sub + uniqueId);
		//String messageBody = "Please find the status of gmail securlet testing";

		//Send the mail
		gmailApi.sendPlainMessage(to, cc, bcc, subject, messageBody);

		//frame the verification object
		createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");

		//Get the expected log and save it otherwise placeholder won't be there
		gmailActivityLog = new GmailActivityLog();


		gmailActivityLog.setSendPlainMessageLog( gmailActivityLog.getSendPlainMessageLog().replace("{subject}", subject));	
		gmailActivityLog.setSendPlainMessageLog( gmailActivityLog.getSendPlainMessageLog().replace("EMAIL", email));
		System.out.println(gmailActivityLog.getSendPlainMessageLog());

		if(internalRecipients!= null)		
		{
			for(int i=0; i < internalRecipients.size(); i++) {

				internalRecipients.set(i, internalRecipients.get(i).toLowerCase());
			}
		}

		GmailActivity gmailActivity = new GmailActivity(gmailActivityLog.getSendPlainMessageLog(), createdTime, Severity.informational.name(), 
				ObjectType.Email_Message.name(), ActivityType.Send.name(), internalRecipients, externalRecipients, inFolder,  null, subject, sender);

		gmailActivities.put(key, gmailActivity);

	}



	@Test(groups={"SEND", "RECEIVE","CREATE","DELETE","TRASH", "GMAIL", "SANITY", "CONTENT_INSPECTION", "REGRESSSION","I18"})
	public void fetchActivityLogs() throws Exception {
		//Fetch the logs

		try {

			for (int i = 0; i <= (minWaitTime * 60 * 1000); i+=60000 ) {
				sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
				System.out.println("waiting for fetch logs");
				//sleep(CommonConstants.TEN_MINUTES_SLEEP);

				HashMap<String, String> termmap = new HashMap<String, String>();
				termmap.put("facility", facility.Gmail.name());
				termmap.put("Object_type", ObjectType.Email_Message.name());
				termmap.put("Activity_type", ActivityType.Send.name());
				//Get Send related logs
				this.sentMessageLogs = this.getInvestigateLogs(-30, 10, facility.Gmail.name(), termmap, suiteData.getUsername(), suiteData.getApiserverHostName(), 
						suiteData.getCSRFToken(), suiteData.getSessionID(), 0, 200, "Google Apps");

				//Get Receive related logs
				termmap = new HashMap<String, String>();
				termmap.put("facility", facility.Gmail.name());
				termmap.put("Object_type", ObjectType.Email_Message.name());
				termmap.put("Activity_type", ActivityType.Receive.name());
				this.receiveMessageLogs = this.getInvestigateLogs(-30, 10, facility.Gmail.name(), termmap, suiteData.getUsername(), suiteData.getApiserverHostName(), 
						suiteData.getCSRFToken(), suiteData.getSessionID(), 0, 200, "Google Apps");

				//Get Create related logs
				termmap = new HashMap<String, String>();
				termmap.put("facility", facility.Gmail.name());
				termmap.put("Object_type", ObjectType.Email_Message.name());
				termmap.put("Activity_type", ActivityType.Create.name());
				this.createMessageLogs = this.getInvestigateLogs(-30, 10, facility.Gmail.name(), termmap, suiteData.getUsername(), suiteData.getApiserverHostName(), 
						suiteData.getCSRFToken(), suiteData.getSessionID(), 0, 200, "Google Apps");

				//Get Delete related logs
				termmap = new HashMap<String, String>();
				termmap.put("facility", facility.Gmail.name());
				termmap.put("Object_type", ObjectType.Email_Message.name());
				termmap.put("Activity_type", ActivityType.Delete.name());
				this.deleteMessageLogs = this.getInvestigateLogs(-30, 10, facility.Gmail.name(), termmap, suiteData.getUsername(), suiteData.getApiserverHostName(), 
						suiteData.getCSRFToken(), suiteData.getSessionID(), 0, 200, "Google Apps");

				//Get Trash related logs
				termmap = new HashMap<String, String>();
				termmap.put("facility", facility.Gmail.name());
				termmap.put("Object_type", ObjectType.Email_Message.name());
				termmap.put("Activity_type", ActivityType.Trash.name());
				this.trashMessageLogs = this.getInvestigateLogs(-30, 10, facility.Gmail.name(), termmap, suiteData.getUsername(), suiteData.getApiserverHostName(), 
						suiteData.getCSRFToken(), suiteData.getSessionID(), 0, 200, "Google Apps");

				//Get CI related logs
				termmap = new HashMap<String, String>();
				termmap.put("facility", facility.Gmail.name());
				termmap.put("Activity_type", "Content Inspection");
				this.contentInspectionLogs = this.getInvestigateLogs(-20, 10, facility.Gmail.name(), termmap, suiteData.getUsername(), suiteData.getApiserverHostName(), 
						suiteData.getCSRFToken(), suiteData.getSessionID(), 0, 200, "Google Apps");

			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}

		//Reporter.log(MarshallingUtils.marshall(sentMessageLogs), true);
	//	Reporter.log(MarshallingUtils.marshall(contentInspectionLogs), true);
		long total = sentMessageLogs.getHits().getTotal();
		Reporter.log("Total sent messages logs count:"+ total, true);

		//If total number of logs is less than zero, no logs are seen so skip the tests
		if (total <= 0) {
			throw new SkipException("Skipping the dependent tests..No logs are seen for sent messages in gmail");
		}
	}

	@Test(dependsOnMethods={"fetchActivityLogs"}, dataProvider = "sendPlainMailKPI18", groups={"SEND", "GMAIL", "SANITY",  "I18"}, dataProviderClass = GmailDataProvider.class)
	public void verifySendPlainI18(String key) throws Exception {
		LogUtils.logTestDescription("After sending the plain message, check the activity logs.");
		logValidator.verifyGmailActivityLog(sentMessageLogs, gmailActivities.get(key));
	}

	@Test(dependsOnMethods={"fetchActivityLogs"}, dataProvider = "sendAttachmentMailKeyDataProvider", groups={"SEND", "GMAIL", "SANITY", "REGRESSSION"}, dataProviderClass = GmailDataProvider.class)
	public void verifySendAttachmentMessageActivity(String key) {
		LogUtils.logTestDescription("After sending the  message with attachment, verify the sent message activity in the activity logs.");
		logValidator.verifyGmailActivityLog(sentMessageLogs, gmailActivities.get(key));
	}
	@Test( dataProvider = "sendAttachmentMailSanityKeyDataProvider", groups={"SEND", "GMAIL", "SANITY"}, dataProviderClass = GmailDataProvider.class)
	public void verifySendAttachmentMessageActivitySanity(String key) {
		try {
			sleep(CommonConstants.TEN_MINUTES_SLEEP);
			this.fetchActivityLogs();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LogUtils.logTestDescription("After sending the  message with attachment, verify the sent message activity in the activity logs.");
		logValidator.verifyGmailActivityLog(sentMessageLogs, gmailActivities.get(key));
	}

	@Test(dependsOnMethods={"fetchActivityLogs"}, dataProvider = "sendMultiAttachmentMailKeyDataProvider", groups={"SEND", "GMAIL", "SANITY", "REGRESSSION"}, dataProviderClass = GmailDataProvider.class)
	public void verifySendMultiAttachmentMessageActivity(String key) {
		LogUtils.logTestDescription("After sending the  message with attachment, verify the sent message activity in the activity logs.");
		logValidator.verifyGmailActivityLog(sentMessageLogs, gmailActivities.get(key));
	}


	@Test(dependsOnMethods={"fetchActivityLogs"}, dataProvider = "recievePlainMailKeyDataProvider", groups={"RECEIVE", "GMAIL", "SANITY", "REGRESSSION"}, dataProviderClass = GmailDataProvider.class)
	public void verifyRecievePlainMailActivity(String key) throws Exception {
		LogUtils.logTestDescription("After Recieving the plain message, check the activity logs.");
		logValidator.verifyGmailActivityLog(receiveMessageLogs, gmailActivities.get(key));
	}

	@Test(dependsOnMethods={"fetchActivityLogs"}, dataProvider = "recieveAttachmentMailKeyDataProvider",groups={"RECEIVE", "GMAIL", "SANITY", "REGRESSSION"}, dataProviderClass = GmailDataProvider.class)
	public void verifyRecieveMailWithAttachmentActivity(String key) throws Exception {
		LogUtils.logTestDescription("After recieving  the message with Attachment, check the activity logs.");
		logValidator.verifyGmailActivityLog(receiveMessageLogs, gmailActivities.get(key));
	}
	@Test(dependsOnMethods={"fetchActivityLogs"}, dataProvider = "recieveMailExternalUserKeyDataProvider",groups={"RECEIVE", "GMAIL", "SANITY", "REGRESSSION"}, dataProviderClass = GmailDataProvider.class)
	public void verifyRecieveMailWithExternUserAttachmentActivity(String key) throws Exception {
		LogUtils.logTestDescription("After recieving  the message with Attachment, check the activity logs.");
		logValidator.verifyGmailActivityLog(receiveMessageLogs, gmailActivities.get(key));
	}


	@Test(groups={"DELETE", "GMAIL", "SANITY", "REGRESSSION"}, dependsOnMethods={"fetchActivityLogs"})
	public void verifyDeletMessageActivity() throws Exception {
		LogUtils.logTestDescription("After deleting the plain message , check the activity logs.");
		Reporter.log("All messages:"+MarshallingUtils.marshall(sentMessageLogs), true);
		logValidator.verifyGmailActivityLog(deleteMessageLogs, gmailActivities.get("deleteMessageActivity"));

	}

	@Test(groups={"TRASH", "GMAIL", "SANITY", "REGRESSSION"}, dependsOnMethods={"fetchActivityLogs"})
	public void verifyTrashMessageActivity() throws Exception {
		LogUtils.logTestDescription("After trashing mail, check the activity logs.");
		Reporter.log("All messages:"+MarshallingUtils.marshall(trashMessageLogs), true);		
		logValidator.verifyGmailActivityLog(trashMessageLogs, gmailActivities.get("trashMailActivity"));
	}

//	@Test(groups={"CREATE", "GMAIL", "SANITY", "REGRESSSION"}, dependsOnMethods={"fetchActivityLogs"})
	public void verifyunTrashMessageActivity() throws Exception {
		LogUtils.logTestDescription("After trashing mail, check the activity logs.");
		Reporter.log("All messages:"+MarshallingUtils.marshall(sentMessageLogs), true);		
		logValidator.verifyGmailActivityLog(sentMessageLogs, gmailActivities.get("untrashMailActivity"));
	}

	@Test(groups={"CREATE", "GMAIL", "SANITY", "REGRESSSION"}, dataProvider = "sendDraftKeyDataProvider",dependsOnMethods={"fetchActivityLogs"}, dataProviderClass = GmailDataProvider.class)
	public void verifySendDraftMessageActivity(String key) throws Exception {
		LogUtils.logTestDescription("After Creating draft , check the activity logs.");
		Reporter.log("All messages:"+MarshallingUtils.marshall(createMessageLogs), true);		
		logValidator.verifyGmailActivityLog(sentMessageLogs, gmailActivities.get(key));
	}

	@Test(groups={"CREATE", "GMAIL", "SANITY", "REGRESSSION"}, dataProvider = "createMultiAttachemntDraftKeyDP",dependsOnMethods={"fetchActivityLogs"}, dataProviderClass = GmailDataProvider.class)
	public void verifyCreateDraftWithMutliAttachmentMessageActivity(String key) throws Exception {
		LogUtils.logTestDescription("After Creating draft , check the activity logs.");
		Reporter.log("All messages:"+MarshallingUtils.marshall(createMessageLogs), true);		
		logValidator.verifyGmailActivityLog(createMessageLogs, gmailActivities.get(key));
	}
	@Test(groups={"CREATE", "GMAIL", "SANITY", "REGRESSSION"}, dataProvider = "createDraftDataKeyProvider",dependsOnMethods={"fetchActivityLogs"}, dataProviderClass = GmailDataProvider.class)
	public void verifyCreateDraftMessageActivity(String key) throws Exception {
		LogUtils.logTestDescription("After Creating draft , check the activity logs.");
		Reporter.log("All messages:"+MarshallingUtils.marshall(createMessageLogs), true);		
		logValidator.verifyGmailActivityLog(createMessageLogs, gmailActivities.get(key));
	}
	@Test(groups={"CREATE", "GMAIL", "SANITY", "REGRESSSION"}, dataProvider = "draftWithMIMEVerificationDataProvider",dependsOnMethods={"fetchActivityLogs"}, dataProviderClass = GmailDataProvider.class)
	public void verifyMIMEDraftMessageActivity(String key) throws Exception {
		LogUtils.logTestDescription("After Creating MIMEdraft , check the activity logs.");
		Reporter.log("All messages:"+MarshallingUtils.marshall(createMessageLogs), true);		
		logValidator.verifyGmailActivityLog(createMessageLogs, gmailActivities.get(key));
	}

//	@Test(groups={"DELETE","CREATE", "GMAIL", "SANITY", "REGRESSSION"}, dependsOnMethods={"fetchActivityLogs"})
	public void verifyDeleteDraftMessageActivity() throws Exception {
		LogUtils.logTestDescription("After deleting the draft, check the activity logs.");
		Reporter.log("All messages:"+MarshallingUtils.marshall(sentMessageLogs), true);		
		logValidator.verifyGmailActivityLog(sentMessageLogs, gmailActivities.get("deleteDraft1"));
	}



	@Test(priority= -9, groups={"DELETE","GMAIL", "SANITY", "REGRESSSION"}, dataProviderClass = GmailDataProvider.class, dataProvider = "deleteGmailDataProvider")
	public void deleteMail(String testDesc, ArrayList<String> to, ArrayList<String> cc, ArrayList<String> bcc,  ArrayList<String> inFolder) throws Exception {

		LogUtils.logTestDescription(testDesc);	
		//Send the mail

		String uniqueId = new String (UUID.randomUUID().toString());
		String subject = "Trash Mail without any exposures "+uniqueId;
		String messageBody = "Please find the status of gmail securlet testing";
		gmailApi.sendPlainMessage(to, cc, bcc, subject, messageBody);
		sleep(CommonConstants.THREE_MINUTES_SLEEP);
		Reporter.log("sent mail subject :"+subject, true);
		gmailActivityLog = new GmailActivityLog();
		createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
		gmailActivityLog.setTrashMessageLog( gmailActivityLog.getDeletePlainMessageLog().replace("{subject}", subject));

		// search for email id thread
		Message message=gmailApi.getLatestMail(subject);
		String threadId=message.getThreadId();
		//it normal mail deletion is a trash activity
		gmailApi.deleteThread(threadId);
		//frame the verification object

		//gmailActivityLog = new GmailActivityLog();
		//createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
		//gmailActivityLog.setSendPlainMessageLog( gmailActivityLog.getSendPlainMessageLog().replace("{subject}", "\""+subject+"\""));


		GmailActivity sendPlainMessageActivity = new GmailActivity(gmailActivityLog.getTrashMessageLog(), createdTime, Severity.informational.name(), 
				ObjectType.Email_Message.name(), ActivityType.Delete.name(), null, null, inFolder,  null, subject, sender);

		gmailActivities.put("deleteMessageActivity", sendPlainMessageActivity);
		sleep(CommonConstants.SIXTY_SECONDS_SLEEP);

	}

	@Test(priority= -10, groups={"TRASH","GMAIL", "SANITY", "REGRESSSION"},dataProviderClass = GmailDataProvider.class, dataProvider = "trashMailDataProvider")
	public void trashMail(String testDesc, ArrayList<String> to, ArrayList<String> cc, ArrayList<String> bcc,  ArrayList<String> inFolder,ArrayList<String> internalRecipients) throws Exception {

		LogUtils.logTestDescription(testDesc);

		String uniqueId = new String (UUID.randomUUID().toString());
		String subject = new String ( "Securlet status testing "+uniqueId);
		String messageBody = "Please find the status of gmail securlet testing";

		//Send the mail
		gmailApi.sendPlainMessage(to, cc, bcc, subject, messageBody);

		// search for email id thread
		Message message=gmailApi.getLatestMail(subject);
		String messageId=message.getId();
		sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
		gmailApi.trashMessage(messageId);
		//frame the verification object
		gmailActivityLog = new GmailActivityLog();
		createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
		gmailActivityLog.setTrashMessageLog( gmailActivityLog.getTrashMessageLog().replace("{subject}", subject));

		GmailActivity sendPlainMessageActivity = new GmailActivity(gmailActivityLog.getTrashMessageLog(), createdTime, Severity.informational.name(), 
				ObjectType.Email_Message.name(), ActivityType.Trash.name(), internalRecipients, null, inFolder,  null, subject, sender);

		gmailActivities.put("trashMailActivity", sendPlainMessageActivity);

	}

	@Test(priority= -10, groups={"CREATE","GMAIL", "SANITY", "REGRESSSION"},dataProviderClass = GmailDataProvider.class, dataProvider = "trashMailDataProvider")
	public void untrashMail(String testDesc, ArrayList<String> to, ArrayList<String> cc, ArrayList<String> bcc,  ArrayList<String> inFolder,ArrayList<String> internalRecipients) throws Exception {

		LogUtils.logTestDescription(testDesc);

		String uniqueId = new String (UUID.randomUUID().toString());
		String subject = new String("Untrash Securlet status testing " + uniqueId);
		String messageBody = "Please find the status of gmail securlet testing";

		//Send the mail
		gmailApi.sendPlainMessage(to, cc, bcc, subject, messageBody);

		// search for email id thread
		Message message=gmailApi.getLatestMail(subject);
		String messageId=message.getId();
		gmailApi.trashMessage(messageId);
		// untrash the message
		gmailApi.untrashMessage(messageId);

		//frame the verification object

		gmailActivityLog = new GmailActivityLog();
		createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
		gmailActivityLog.setSendPlainMessageLog( gmailActivityLog.getSendPlainMessageLog().replace("{subject}", subject));

		GmailActivity sendPlainMessageActivity = new GmailActivity(gmailActivityLog.getSendPlainMessageLog(), createdTime, Severity.informational.name(), 
				ObjectType.Email_Message.name(), ActivityType.Send.name(), internalRecipients, to, inFolder,  null, subject, sender);
		//internalRecipients, externalRecipients, inFolder,  null, subject, suiteData.getUsername());

		gmailActivities.put("UntrashMailActivity", sendPlainMessageActivity);

	}



	@Test(priority= -10, groups={"CREATE","GMAIL", "SANITY", "REGRESSSION"}, dataProviderClass = GmailDataProvider.class, dataProvider = "createDraftDataProvider")
	public void createDraftMail(String key,String testDesc, ArrayList<String> to, ArrayList<String> cc, ArrayList<String> bcc, ArrayList<String> inFolder,ArrayList<String> internalRecipients, ArrayList<String> externalRecipients) throws Exception {

		LogUtils.logTestDescription(testDesc);

		String uniqueId = new String (UUID.randomUUID().toString());
		String subject = new String("Draft creation Securlet status testing " + uniqueId);
		String messageBody = "Please find the status of gmail securlet testing";
		gmailApi.createDraft(to, cc, bcc, subject, messageBody);

		// search for email id thread

		//frame the verification object
		gmailActivityLog = new GmailActivityLog();
		createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
		gmailActivityLog.setDraftMessageLog( gmailActivityLog.getDraftMessageLog().replace("{subject}", subject));

		GmailActivity sendPlainMessageActivity = new GmailActivity(gmailActivityLog.getDraftMessageLog(), createdTime, Severity.informational.name(), 
				ObjectType.Email_Message.name(), ActivityType.Create.name(), null, externalRecipients, inFolder,  null, subject, sender);

		gmailActivities.put(key, sendPlainMessageActivity);

	}

	@Test(priority= -10, groups={"CREATE","GMAIL", "SANITY", "REGRESSSION"}, dataProviderClass = GmailDataProvider.class, dataProvider = "sendDraftDataProvider")
	public void sendDraftMail(String key,String testDesc, ArrayList<String> to, ArrayList<String> cc, ArrayList<String> bcc, ArrayList<String> inFolder,ArrayList<String> internalRecipients, ArrayList<String> externalRecipients,String email) throws Exception {

		LogUtils.logTestDescription(testDesc);

		String uniqueId = new String (UUID.randomUUID().toString());
		String subject = new String("Sending Draft for securlet testing " + uniqueId);
		String messageBody = "Please find the status of gmail securlet testing";
		Draft draft=gmailApi.createDraft(to, cc, bcc, subject, messageBody);

		//send draft
		//boolean status=gmailApi.sendDraft(draft);
		
		Message messageWithSubject = gmailApi.getMessageWithSubject(subject, "DRAFT");
	    System.out.println("===>"+messageWithSubject.getId());
	    gmailApi.sendMessage(gmailApi.getMimeMessage(messageWithSubject.getId()));
		
		
		
		
		
		

		//frame the verification object
		gmailActivityLog = new GmailActivityLog();
		createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
		gmailActivityLog.setSendPlainMessageLog( gmailActivityLog.getSendPlainMessageLog().replace("{subject}", subject));
		gmailActivityLog.setSendPlainMessageLog( gmailActivityLog.getSendPlainMessageLog().replace("EMAIL", email));

		GmailActivity sendPlainMessageActivity = new GmailActivity(gmailActivityLog.getSendPlainMessageLog(), createdTime, Severity.informational.name(), 
				ObjectType.Email_Message.name(), ActivityType.Send.name(), to,null, inFolder,  null, subject, sender);

		gmailActivities.put(key, sendPlainMessageActivity);

	}

	//@Test(priority= -10, groups={"CREATE","GMAIL", "SANITY", "REGRESSSION"},dataProvider = "createDraftDataProvider", dataProviderClass = GmailDataProvider.class )
	public void createDraftWithAttachment(String key,String testDesc, ArrayList<String> to, ArrayList<String> cc, ArrayList<String> bcc, ArrayList<String> inFolder,ArrayList<String> internalRecipients, ArrayList<String> externalRecipients) throws Exception {

		LogUtils.logTestDescription(testDesc);
		String uniqueId = new String (UUID.randomUUID().toString());
		String subject = new String("Sending Draft for securlet testing " + uniqueId);
		String messageBody = "Please find the status of gmail securlet testing";
		gmailApi.createDraft(to, cc, bcc, subject, messageBody);

		// search for email id thread

		//frame the verification object
		gmailActivityLog = new GmailActivityLog();
		createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
		gmailActivityLog.setSendPlainMessageLog( gmailActivityLog.getSendPlainMessageLog().replace("{subject}", subject));

		GmailActivity sendPlainMessageActivity = new GmailActivity(gmailActivityLog.getSendPlainMessageLog(), createdTime, Severity.informational.name(), 
				ObjectType.Email_Message.name(), ActivityType.Create.name(), null, to, inFolder,  null, subject, suiteData.getUsername());

		gmailActivities.put(key, sendPlainMessageActivity);

	}


	@Test(priority= -10, groups={"CREATE","GMAIL", "SANITY", "REGRESSSION"},dataProvider = "createDraftWithMultiAttachmentDataProvider", dataProviderClass = GmailDataProvider.class )
	public void createDraftWithMultiAttachment(String key,String testDesc, ArrayList<String> to, ArrayList<String> cc, ArrayList<String> bcc, ArrayList<String> inFolder,ArrayList<String> internalRecipients, ArrayList<String> externalRecipients,ArrayList<String>fileLocations) throws Exception {

		LogUtils.logTestDescription(testDesc);
		String uniqueId = new String (UUID.randomUUID().toString());
		String subject = new String("Creating  Draft for securlet testing " + uniqueId);
		String messageBody = "Please find the status of gmail securlet testing";
		MimeMessage mutimsg=	gmailApi.createEmailWithMultipleAttachment(to, cc, bcc, subject, messageBody, fileLocations);
		gmailApi.createDraft(mutimsg);

		// search for email id thread

		//frame the verification object
		gmailActivityLog = new GmailActivityLog();
		createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
		gmailActivityLog.setDraftMessageLog( gmailActivityLog.getDraftMessageLog().replace("{subject}", subject));
		//gmailActivityLog.setDraftMessageLog( gmailActivityLog.getDraftMessageLog().replace("EMAIL", email));

		GmailActivity sendPlainMessageActivity = new GmailActivity(gmailActivityLog.getDraftMessageLog(), createdTime, Severity.informational.name(), 
				ObjectType.Email_Message.name(), ActivityType.Create.name(), internalRecipients, null, inFolder,  null, subject, sender);

		gmailActivities.put(key, sendPlainMessageActivity);

	}


	@Test(priority= -10, groups={"CREATE","GMAIL", "SANITY", "REGRESSSION"}, dataProviderClass = GmailDataProvider.class, dataProvider = "crateDraftMIMEAttachmentDataProvider")
	public void createDraftWithMIMEAttachment(String key,String testDesc, ArrayList<String> to, ArrayList<String> cc, ArrayList<String> bcc, ArrayList<String> inFolder,ArrayList<String> internalRecipients, ArrayList<String> externalRecipients,String filename,String email) throws Exception {

		LogUtils.logTestDescription(testDesc);
		String uniqueId = new String (UUID.randomUUID().toString());
		String subject = new String("Sending Draft for securlet testing " + uniqueId);
		String messageBody = "Please find the status of gmail securlet testing";


		//gmailApi.sendMessageWithAttachment(to,to,to, "Testing Sending to Multiple", "I am Body", "/Users/rahulkumar/NetBeansProjects/BackendAutomation/BeatleElastica/BEATLe/BEATLe/pom.xml","TestAttachment.xml");
		String filepath = InfraConstants.INFRA_DATA_LOC + filename;
		//  String   LocalFileLocation = DCIConstants.DCI_FILE_UPLOAD_PATH + File.separator + actualFileNameToUpload;
		//check the filename provided is absolute or not
		File uploadFile = new java.io.File(FilenameUtils.separatorsToSystem(filepath).trim());
		if(!uploadFile.exists()) {
			System.out.println("Sorry file not exists in the folder"); 
		}
		Reporter.log("Sending mail to "+ to, true);



		gmailApi.createDraftWithMediaContent(to, cc, bcc, subject, messageBody, filepath);
		sleep(CommonConstants.TWO_MINUTES_SLEEP);
		// search for email id thread

		//frame the verification object
		gmailActivityLog = new GmailActivityLog();
		createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
		gmailActivityLog.setDraftMessageLog( gmailActivityLog.getDraftMessageLog().replace("{subject}", subject));
		//gmailActivityLog.setSendPlainMessageLog( gmailActivityLog.getSendPlainMessageLog().replace("EMAIL", email));
		GmailActivity sendPlainMessageActivity = new GmailActivity(gmailActivityLog.getDraftMessageLog(), createdTime, Severity.informational.name(), 
				ObjectType.Email_Message.name(), ActivityType.Create.name(), null, externalRecipients, inFolder,  null, subject, sender);

		gmailActivities.put(key, sendPlainMessageActivity);

	}

	@Test(priority= -10, groups={"DELETE","CREATE","GMAIL", "SANITY", "REGRESSSION"},dataProvider = "deleteDraftDataProvider", dataProviderClass = GmailDataProvider.class)
	public void deleteDraftMail(String key,String testDesc, ArrayList<String> to, ArrayList<String> cc, ArrayList<String> bcc, ArrayList<String> inFolder, ArrayList<String> internalRecipients, ArrayList<String> externalRecipients) throws Exception {

		LogUtils.logTestDescription(testDesc);
		String uniqueId = new String (UUID.randomUUID().toString());
		String subject = new String("Sending Draft for securlet testing " + uniqueId);
		String messageBody = "Please find the status of gmail securlet testing";

		Draft draft=gmailApi.createDraft(to, cc, bcc, subject, messageBody);

		// search for email id thread
		String draftId=draft.getId();
		gmailApi.deleteDraft(draftId);
		//frame the verification object
		createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
		gmailActivityLog = new GmailActivityLog();

		gmailActivityLog.setTrashMessageLog( gmailActivityLog.getTrashMessageLog().replace("{subject}", subject));


		GmailActivity sendPlainMessageActivity = new GmailActivity(gmailActivityLog.getTrashMessageLog(), createdTime, Severity.informational.name(), 
				ObjectType.Email_Message.name(), ActivityType.Send.name(), internalRecipients, externalRecipients, inFolder,  null, subject, sender1);


		gmailActivities.put(key, sendPlainMessageActivity);

	}






	@Test(priority= -10, groups={"SEND","GMAIL", "SANITY", "REGRESSSION"}, dataProvider = "sendMailWithAttachmentDataProvider", dataProviderClass = GmailDataProvider.class)
	public void sendMailwithAttachment(String key, String testDesc, ArrayList<String> to, ArrayList<String> cc, ArrayList<String> bcc, ArrayList<String> inFolder, ArrayList<String> internalRecipients, ArrayList<String> externalRecipients,String filename,String email) throws Exception {
		LogUtils.logTestDescription(testDesc);

		String uniqueId = UUID.randomUUID().toString();
		//String filename="test.txt";

		String subject = "Securlet status testing with attachment" + uniqueId;
		String messageBody = "Please find the status of gmail securlet testing";

		//gmailApi.sendMessageWithAttachment(to,to,to, "Testing Sending to Multiple", "I am Body", "/Users/rahulkumar/NetBeansProjects/BackendAutomation/BeatleElastica/BEATLe/BEATLe/pom.xml","TestAttachment.xml");
		String filepath = InfraConstants.INFRA_DATA_LOC + filename;
		//  String   LocalFileLocation = DCIConstants.DCI_FILE_UPLOAD_PATH + File.separator + actualFileNameToUpload;
		//check the filename provided is absolute or not
		File uploadFile = new java.io.File(FilenameUtils.separatorsToSystem(filepath).trim());
		if(!uploadFile.exists()) {
			System.out.println("Sorry file not exists in the folder"); 
		}
		Reporter.log("Sending mail to "+ to, true);
		// gmailApi.sendPlainMessage(to, cc, bcc, subject, body);
		gmailApi.sendMessageWithAttachment(to, cc, bcc, subject, messageBody, filepath,"TestAttachment.xml");

		// wait for attachment to upload
		sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
		//Get the expected log and save it otherwise placeholder won't be there
		gmailActivityLog = new GmailActivityLog();
		// create the verification params
		gmailActivityLog.setSendAttachmentMessageLog( gmailActivityLog.getSendAttachmentMessageLog().replace("{subject}", subject));
		gmailActivityLog.setSendAttachmentMessageLog( gmailActivityLog.getSendAttachmentMessageLog().replace("EMAIL", email));

		createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");

		//frame the verification object

		//gmailActivityLog.setSendPlainMessageLog( gmailActivityLog.getSendPlainMessageLog().replace("{subject}", subject));


		GmailActivity gmailActivity = new GmailActivity(gmailActivityLog.getSendAttachmentMessageLog(), createdTime, Severity.informational.name(), 
				ObjectType.Email_Message.name(), ActivityType.Send.name(), internalRecipients, externalRecipients, inFolder,  null, subject, sender);

		gmailActivities.put(key, gmailActivity);

	}

	@Test(priority= -10, groups={"SEND","GMAIL", "SANITY"}, dataProvider = "sendMailWithAttachmentDataProviderSanity", dataProviderClass = GmailDataProvider.class)
	public void sendMailwithAttachmentSanity(String key, String testDesc, ArrayList<String> to, ArrayList<String> cc, ArrayList<String> bcc, ArrayList<String> inFolder, ArrayList<String> internalRecipients, ArrayList<String> externalRecipients,String filename,String email) throws Exception {
		LogUtils.logTestDescription(testDesc);

		String uniqueId = UUID.randomUUID().toString();
		//String filename="test.txt";

		String subject = "Securlet status testing with attachment" + uniqueId;
		String messageBody = "Please find the status of gmail securlet testing";

		//gmailApi.sendMessageWithAttachment(to,to,to, "Testing Sending to Multiple", "I am Body", "/Users/rahulkumar/NetBeansProjects/BackendAutomation/BeatleElastica/BEATLe/BEATLe/pom.xml","TestAttachment.xml");
		String filepath = InfraConstants.INFRA_DATA_LOC + filename;
		//  String   LocalFileLocation = DCIConstants.DCI_FILE_UPLOAD_PATH + File.separator + actualFileNameToUpload;
		//check the filename provided is absolute or not
		File uploadFile = new java.io.File(FilenameUtils.separatorsToSystem(filepath).trim());
		if(!uploadFile.exists()) {
			System.out.println("Sorry file not exists in the folder"); 
		}
		Reporter.log("Sending mail to "+ to, true);
		// gmailApi.sendPlainMessage(to, cc, bcc, subject, body);
		gmailApiTest1.sendMessageWithAttachment(to, cc, bcc, subject, messageBody, filepath,filename);

		// wait for attachment to upload
		sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
		//Get the expected log and save it otherwise placeholder won't be there
		gmailActivityLog = new GmailActivityLog();
		// create the verification params
		gmailActivityLog.setSendAttachmentMessageLog( gmailActivityLog.getSendAttachmentMessageLog().replace("{subject}", subject));
		gmailActivityLog.setSendAttachmentMessageLog( gmailActivityLog.getSendAttachmentMessageLog().replace("EMAIL", email));

		createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");

		//frame the verification object

		//gmailActivityLog.setSendPlainMessageLog( gmailActivityLog.getSendPlainMessageLog().replace("{subject}", subject));


		GmailActivity gmailActivity = new GmailActivity(gmailActivityLog.getSendAttachmentMessageLog(), createdTime, Severity.informational.name(), 
				ObjectType.Email_Message.name(), ActivityType.Send.name(), internalRecipients, externalRecipients, inFolder,  null, subject, sender1);

		gmailActivities.put(key, gmailActivity);

	}

	@Test(priority= -10, groups={"SEND","GMAIL", "SANITY", "REGRESSSION"}, dataProvider = "sendMailWithMultiAttachmentDataProvider", dataProviderClass = GmailDataProvider.class)
	public void sendMailwithMultiAttachment(String key, String testDesc, ArrayList<String> to, ArrayList<String> cc, ArrayList<String> bcc, ArrayList<String> inFolder, ArrayList<String> internalRecipients, ArrayList<String> externalRecipients,ArrayList<String> filename,String email) throws Exception {
		LogUtils.logTestDescription(testDesc);

		String uniqueId = UUID.randomUUID().toString();
		//String filename="test.txt";

		String subject = "Securlet status testing with multi attachment" + uniqueId;
		String messageBody = "Please find the status of gmail securlet testing";

		//		//gmailApi.sendMessageWithAttachment(to,to,to, "Testing Sending to Multiple", "I am Body", "/Users/rahulkumar/NetBeansProjects/BackendAutomation/BeatleElastica/BEATLe/BEATLe/pom.xml","TestAttachment.xml");
		//		String filepath = InfraConstants.INFRA_DATA_LOC + filename;
		//		//  String   LocalFileLocation = DCIConstants.DCI_FILE_UPLOAD_PATH + File.separator + actualFileNameToUpload;
		//		//check the filename provided is absolute or not
		//		File uploadFile = new java.io.File(FilenameUtils.separatorsToSystem(filepath).trim());
		//		if(!uploadFile.exists()) {
		//			System.out.println("Sorry file not exists in the folder"); 
		//		}

		Reporter.log("Sending mail to "+ to, true);
		// gmailApi.sendPlainMessage(to, cc, bcc, subject, body);
		gmailApi.sendMessageWithMultipleAttachment(to, cc, bcc, subject, messageBody, filename);//sendsendMessageWithAttachment(to, cc, bcc, subject, messageBody, filepath);

		// wait for attachment to upload
		sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
		//Get the expected log and save it otherwise placeholder won't be there
		gmailActivityLog = new GmailActivityLog();
		// create the verification params
		gmailActivityLog.setSendPlainMessageLog( gmailActivityLog.getSendPlainMessageLog().replace("{subject}", subject));
		gmailActivityLog.setSendPlainMessageLog( gmailActivityLog.getSendPlainMessageLog().replace("EMAIL", email));

		createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");

		//frame the verification object

		gmailActivityLog.setSendPlainMessageLog( gmailActivityLog.getSendPlainMessageLog().replace("{subject}", subject));


		GmailActivity gmailActivity = new GmailActivity(gmailActivityLog.getSendPlainMessageLog(), createdTime, Severity.informational.name(), 
				ObjectType.Email_Message.name(), ActivityType.Send.name(), internalRecipients, externalRecipients, inFolder,  null, subject, sender);

		gmailActivities.put(key, gmailActivity);

	}



	@Test(priority= -10, groups={"RECEIVE","GMAIL", "SANITY", "REGRESSSION"}, dataProvider = "recievePlainMailDataProvider", dataProviderClass = GmailDataProvider.class)
	public void recieveMail(String key, String testDesc, ArrayList<String> to, ArrayList<String> cc, ArrayList<String> bcc, ArrayList<String> inFolder, ArrayList<String> internalRecipients, ArrayList<String> externalRecipients,String email) throws Exception {

		LogUtils.logTestDescription(testDesc);

		String uniqueId = new String (UUID.randomUUID().toString());
		String subject = new String("Securlet status testing " + uniqueId);
		String messageBody = "Please find the status of gmail securlet testing";

		//Send the mail
		gmailApiTest1.sendPlainMessage(to, cc, bcc, subject, messageBody);

		//frame the verification object
		createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");

		//Get the expected log and save it otherwise placeholder won't be there
		gmailActivityLog = new GmailActivityLog();

		gmailActivityLog.setReceivePlainMessageLog( gmailActivityLog.getReceivePlainMessageLog().replace("{subject}", subject));
		gmailActivityLog.setReceivePlainMessageLog( gmailActivityLog.getReceivePlainMessageLog().replace("EMAIL", email));

		GmailActivity gmailActivity = new GmailActivity(gmailActivityLog.getReceivePlainMessageLog(), createdTime, Severity.informational.name(), 
				ObjectType.Email_Message.name(), ActivityType.Receive.name(), internalRecipients, externalRecipients, inFolder,  null, subject, sender);

		gmailActivities.put(key, gmailActivity);

	}
	
	@Test(priority= -10, groups={"RECEIVE","GMAIL", "SANITY", "REGRESSSION"}, dataProvider = "recieveMailExternalUserDataProvider", dataProviderClass = GmailDataProvider.class)
	public void recieveMailfromExternalUser(String Recieve,String key, String testDesc, ArrayList<String> to, ArrayList<String> cc, ArrayList<String> bcc, ArrayList<String> inFolder, ArrayList<String> internalRecipients, ArrayList<String> externalRecipients,String filename,String email) throws Exception {

		LogUtils.logTestDescription(testDesc);
		boolean success=false;
		String uniqueId = new String (UUID.randomUUID().toString());
		String subject = new String("Securlet status testing recieve mail from external user " + uniqueId);
		String messageBody = "Please find the status of gmail securlet testing";
		String filepath = InfraConstants.INFRA_DATA_LOC + filename;
		ArrayList<String> myAttachment = new ArrayList<String>();
		myAttachment.add(filepath);
		//Send the mail
		synchronized (this){
			success=objMailTestUser1.sendMail(Recieve, subject, messageBody, myAttachment, true);
			assertTrue(success, "Failed sending mail with subject:"+subject+".");
			success=false;
		}
		//gmailApiTest1.sendPlainMessage(to, cc, bcc, subject, messageBody);

		//frame the verification object
		createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");

		//Get the expected log and save it otherwise placeholder won't be there
		gmailActivityLog = new GmailActivityLog();

		gmailActivityLog.setReceivePlainMessageLog( gmailActivityLog.getReceivePlainMessageLog().replace("{subject}", subject));
		gmailActivityLog.setReceivePlainMessageLog( gmailActivityLog.getReceivePlainMessageLog().replace("EMAIL", email));

		GmailActivity gmailActivity = new GmailActivity(gmailActivityLog.getReceivePlainMessageLog(), createdTime, Severity.informational.name(), 
				ObjectType.Email_Message.name(), ActivityType.Receive.name(), internalRecipients, externalRecipients, inFolder,  null, subject, Recieve);

		gmailActivities.put(key, gmailActivity);

	}

	@Test(priority= -10, groups={"RECEIVE","GMAIL", "SANITY", "REGRESSSION"}, dataProvider = "receiveMailWithAttachmentDataProvider", dataProviderClass = GmailDataProvider.class)
	public void recieveMailWithAttachment(String key, String testDesc, ArrayList<String> to, ArrayList<String> cc, ArrayList<String> bcc, ArrayList<String> inFolder, ArrayList<String> internalRecipients, ArrayList<String> externalRecipients,String filename,String email) throws Exception {

		LogUtils.logTestDescription(testDesc);

		String uniqueId = new String (UUID.randomUUID().toString());
		String subject = new String("Securlet status testing " + uniqueId);
		String messageBody = "Please find the status of gmail securlet testing";

		//String filename="test.txt";


		//gmailApi.sendMessageWithAttachment(to,to,to, "Testing Sending to Multiple", "I am Body", "/Users/rahulkumar/NetBeansProjects/BackendAutomation/BeatleElastica/BEATLe/BEATLe/pom.xml","TestAttachment.xml");
		String filepath = InfraConstants.INFRA_DATA_LOC + filename;
		//  String   LocalFileLocation = DCIConstants.DCI_FILE_UPLOAD_PATH + File.separator + actualFileNameToUpload;
		//check the filename provided is absolute or not
		File uploadFile = new java.io.File(FilenameUtils.separatorsToSystem(filepath).trim());
		if(!uploadFile.exists()) {
			System.out.println("Sorry file not exists in the folder"); 
		}
		Reporter.log("Sending mail to "+ to, true);

		//Send the mail
		gmailApiTest1.sendMessageWithAttachment(to, cc, bcc, subject, messageBody, filepath,filename);
		//frame the verification object
		createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");

		//Get the expected log and save it otherwise placeholder won't be there
		gmailActivityLog = new GmailActivityLog();

		gmailActivityLog.setReceivePlainMessageLog( gmailActivityLog.getReceivePlainMessageLog().replace("{subject}", subject));
		gmailActivityLog.setReceivePlainMessageLog( gmailActivityLog.getReceivePlainMessageLog().replace("EMAIL", email));

		GmailActivity gmailActivity = new GmailActivity(gmailActivityLog.getReceivePlainMessageLog(), createdTime, Severity.informational.name(), 
				ObjectType.Email_Message.name(), ActivityType.Receive.name(), internalRecipients, externalRecipients, inFolder,  null, subject, sender);

		gmailActivities.put(key, gmailActivity);

	}


	@Test(priority= -10, groups={"CONTENT_INSPECTION", "GMAIL", "SANITY", "REGRESSSION"}, dataProvider = "CIDataProvider", dataProviderClass = GmailDataProvider.class)
	public void sendMailWithRisksInline(String key, String testDesc, String filename, String riskType, ArrayList<String> to, ArrayList<String> cc, ArrayList<String> bcc, ArrayList<String> inFolder, ArrayList<String> internalRecipients, ArrayList<String> externalRecipients) throws Exception {

		LogUtils.logTestDescription(testDesc);

		String uniqueId = new String (UUID.randomUUID().toString());
		String filePath = DCIConstants.DCI_FILE_UPLOAD_RISK_TYPES_PATH + File.separator + filename;

		String subject = new String("Securlet gmail testing with inline risk:" + uniqueId);
		String messageBody = "Please find the status of gmail securlet testing";
		messageBody += getFileContents(filePath);

		//Send the mail
		gmailApi.sendPlainMessage(to, cc, bcc, subject, messageBody);

		//frame the verification object
		createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");

		//Get the expected log and save it otherwise placeholder won't be there
		String message = "File "+subject +" has risk(s) - "+ riskType;

		GmailActivity gmailActivity = new GmailActivity(facility.Gmail.name(), message, createdTime, Severity.critical.name(), "Content Inspection", 
				String.valueOf(messageBody.length()), subject, suiteData.getUsername(), riskType, null) ;

		gmailActivities.put(key, gmailActivity);

	}


	@Test(groups={"CONTENT_INSPECTION", "SEND", "GMAIL", "SANITY", "REGRESSSION"}, dependsOnMethods={"fetchActivityLogs"}, dataProvider = "CIVerificationDataProvider", dataProviderClass = GmailDataProvider.class)
	public void verifySendMailWithRisksInline(String key) throws Exception {
		LogUtils.logTestDescription("After sending mail with inline risks, verify the Content Inspection logs.");
		logValidator.verifyGmailCIActivityLog(this.contentInspectionLogs, gmailActivities.get(key));
	}


	@Test(priority= -10, groups={"SEND", "CONTENT_INSPECTION", "GMAIL", "SANITY", "REGRESSSION"}, dataProvider = "sendMailWithRiskAttachmentDataProvider", dataProviderClass = GmailDataProvider.class)
	public void sendMailwithRisksAsAttachment(String key, String testDesc, String filename, String riskType, ArrayList<String> to, ArrayList<String> cc, ArrayList<String> bcc, ArrayList<String> inFolder, ArrayList<String> internalRecipients, ArrayList<String> externalRecipients) throws Exception {
		LogUtils.logTestDescription(testDesc);

		String uniqueId = UUID.randomUUID().toString();
		String subject = "Gmail Content Inspection check with risks("+filename+") as attachment:" + uniqueId;
		String messageBody = "Please find the attached risks "+ filename;

		String filepath = DCIConstants.DCI_FILE_UPLOAD_RISK_TYPES_PATH + File.separator + filename;

		//check the filename provided is absolute or not
		File uploadFile = new java.io.File(FilenameUtils.separatorsToSystem(filepath).trim());
		if(!uploadFile.exists()) {
			Reporter.log("Expected risk file is not available in specific location", true); 
		}
		Reporter.log("Sending mail to "+ to, true);

		// gmailApi.sendPlainMessage(to, cc, bcc, subject, body);
		gmailApi.sendMessageWithAttachment(to, cc, bcc, subject, messageBody, filepath, filename);


		String message = "File "+filename+" has risk(s) - "+riskType;

		createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");

		GmailActivity gmailActivity = new GmailActivity(facility.Gmail.name(), message, createdTime, Severity.critical.name(), "Content Inspection", 
				String.valueOf(uploadFile.length()), filename, suiteData.getUsername(), riskType, null) ;

		gmailActivities.put(key, gmailActivity);

	}


	@Test(groups={"CONTENT_INSPECTION", "GMAIL", "SANITY", "REGRESSSION"}, dataProvider = "sendMailWithRiskAttachmentKeyDataProvider", dataProviderClass = GmailDataProvider.class, dependsOnMethods={"fetchActivityLogs"})
	public void verifySendMailwithRisksAsAttachment(String key) throws Exception {
		LogUtils.logTestDescription("After sending the message with attached risks, verify the CI logs");
		logValidator.verifyGmailCIActivityLog(contentInspectionLogs, gmailActivities.get(key));
	}


	//	@Test(groups={"MIME","GMAIL"})
	public void sendSimpleMIMEMail() throws Exception {
		List<String> to	=new ArrayList<>();

		to.add("rahul.embeddedsystem@gmail.com");
		to.add("rahulsky.java@gmail.com");

		String uniqueId = UUID.randomUUID().toString();

		String subject = "Mail without any exposures "+ uniqueId;
		String body  = " <html> <head><title> html - simple page </title></head><body></body></html> ";

		///gmailApi.sendMessage(email)l

		Reporter.log("Sending mail to "+ to, true);
		gmailApi.sendPlainMessage(to, null, null, subject, body);

		// create the verification params
		gmailActivityLog.setSendPlainMessageLog( gmailActivityLog.getSendPlainMessageLog().replace("{subject}", subject));


		createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");

		ArrayList<String> internalRecipients = new ArrayList<String>();
		internalRecipients.add("testuser1@securletbeatle.com");

		ArrayList<String> externalRecipients = new ArrayList<String>();
		externalRecipients.add("");

		ArrayList<String> inFolder = new ArrayList<String>();
		inFolder.add("");

		//Prepare the verification object
		/*
		GmailActivity sendPlainMessageActivity = new GmailActivity(gmailActivityLog.getSendPlainMessageLog(), createdTime, "informational", 
				"Email Message", "Receive", internalRecipients, externalRecipients, inFolder,  "", "", "");

		gmailActivities.put("sendPlainMessageActivity", sendPlainMessageActivity);*/

	}

	//	@Test(groups={"MIME","GMAIL"})
	public void sendSimpleMail() throws Exception {
		List<String> to	=new ArrayList<>();

		to.add("rahul.embeddedsystem@gmail.com");
		to.add("rahulsky.java@gmail.com");

		String uniqueId = UUID.randomUUID().toString();

		String subject = "Mail without any exposures "+ uniqueId;
		String body  = "this is the body";

		///gmailApi.sendMessage(email)l

		Reporter.log("Sending mail to "+ to, true);
		gmailApi.sendPlainMessage(to, null, null, subject, body);

		// create the verification params
		gmailActivityLog.setSendPlainMessageLog( gmailActivityLog.getSendPlainMessageLog().replace("{subject}", subject));


		createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");

		ArrayList<String> internalRecipients = new ArrayList<String>();
		internalRecipients.add("testuser1@securletbeatle.com");

		ArrayList<String> externalRecipients = new ArrayList<String>();
		externalRecipients.add("");

		ArrayList<String> inFolder = new ArrayList<String>();
		inFolder.add("");

		//Prepare the verification object
		/*
		GmailActivity sendPlainMessageActivity = new GmailActivity(gmailActivityLog.getSendPlainMessageLog(), createdTime, "informational", 
				"Email Message", "Receive", internalRecipients, externalRecipients, inFolder,  "", "", "");

		gmailActivities.put("sendPlainMessageActivity", sendPlainMessageActivity);*/

	}




	//@Test(dependsOnMethods="fetchActivityLogs", groups={"SENT_MESSAGES"})
	public void verifySendPlainMessageActivity() {

		LogUtils.logTestDescription("After sending the plain message, verify the sent message activity in the activity logs.");
		logValidator.verifyGmailActivityLog(sentMessageLogs, gmailActivities.get("sendPlainMessageActivity"));
	}


	public static void replace(List<String> strings)
	{
	    ListIterator<String> iterator = strings.listIterator();
	    while (iterator.hasNext())
	    {
	        iterator.set(iterator.next().toLowerCase());
	    }
	}



}
