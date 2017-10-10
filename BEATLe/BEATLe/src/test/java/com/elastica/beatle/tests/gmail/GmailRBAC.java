package com.elastica.beatle.tests.gmail;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

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
import com.universal.common.GoogleMailServices;
import com.universal.constants.CommonConstants;

public class GmailRBAC extends GmailUtils{
	
	GoogleMailServices gmailApi ;
	GoogleMailServices gmailApiTest1 ;
	GmailActivityLog gmailActivityLog;
	HashMap<String, GmailActivity> gmailActivities = new HashMap<String, GmailActivity>();
	LogValidator logValidator;
	protected ForensicSearchResults sentMessageLogs, receiveMessageLogs, createMessageLogs, 
	deleteMessageLogs, trashMessageLogs, contentInspectionLogs;
	long maxWaitTime = 20;
	long minWaitTime = 5;

	String clientId;
	String clientSecret;
	String refreshToken;
	String clientIdTest1;
	String clientSecretTest1;
	String refreshTokenTest1;
	String createdTime;
	String Sender1="admin@securletbeatle.com";
	String Sender2="testuser1@securletbeatle.com";
	public GmailRBAC() throws Exception {
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
	}

	@Test(priority= -10, groups={"SEND","GMAIL", "SANITY","REGRESSSION"}, dataProvider = "RBACSendMailDP", dataProviderClass = GmailRBACDataProvider.class)
	public void sendMailRBAC(String Sendkey,String recievekey, String testDesc, ArrayList<String> to, ArrayList<String> cc, ArrayList<String> bcc, ArrayList<String> inFolder, ArrayList<String> internalRecipients, ArrayList<String> externalRecipients,String email) throws Exception {

		LogUtils.logTestDescription(testDesc);

		String uniqueId = new String (UUID.randomUUID().toString());
		String subject = new String("Securlet status testing " + uniqueId);
		String messageBody = "Please find the status of gmail securlet testing";

		//Send the mail
		gmailApi.sendPlainMessage(to, cc, bcc, subject, messageBody);

		//frame the verification object
		createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");

		//Get the expected log and save it otherwise placeholder won't be there
		gmailActivityLog = new GmailActivityLog();
		GmailActivityLog gmailActivityLog1 = new GmailActivityLog();


		gmailActivityLog.setSendPlainMessageLog( gmailActivityLog.getSendPlainMessageLog().replace("{subject}", subject));	
		gmailActivityLog.setSendPlainMessageLog( gmailActivityLog.getSendPlainMessageLog().replace("EMAIL", email));
		System.out.println(gmailActivityLog.getSendPlainMessageLog());
		GmailActivity gmailActivity = new GmailActivity(gmailActivityLog.getSendPlainMessageLog(), createdTime, Severity.informational.name(), 
				ObjectType.Email_Message.name(), ActivityType.Send.name(), internalRecipients, externalRecipients, inFolder,  null, subject, Sender1);
		gmailActivities.put(Sendkey, gmailActivity);
		// recieve logs setting
		gmailActivityLog.setReceivePlainMessageLog( gmailActivityLog.getReceivePlainMessageLog().replace("{subject}", subject));	
		gmailActivityLog.setReceivePlainMessageLog( gmailActivityLog.getReceivePlainMessageLog().replace("EMAIL", Sender1));

		
		ArrayList<String> inFolderRec=new ArrayList();
		inFolderRec.add("Inbox");
		GmailActivity gmailActivityReceive = new GmailActivity(gmailActivityLog.getReceivePlainMessageLog(), createdTime, Severity.informational.name(), 
				ObjectType.Email_Message.name(), ActivityType.Receive.name(), internalRecipients, externalRecipients, inFolderRec,  null, subject, Sender2);

		gmailActivities.put(recievekey, gmailActivityReceive);

	}
	
	
	

	@Test(priority= -10, groups={"SEND", "CONTENT_INSPECTION", "GMAIL", "SANITY", "REGRESSSION"}, dataProvider = "sendMailWithRiskAttachmentDataProvider", dataProviderClass = GmailRBACDataProvider.class)
	public void sendMailwithRisksAsAttachmentRBAC(String key, String testDesc, String filename, String riskType, ArrayList<String> to, ArrayList<String> cc, ArrayList<String> bcc, ArrayList<String> inFolder, ArrayList<String> internalRecipients, ArrayList<String> externalRecipients) throws Exception {
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
		sleep(CommonConstants.TWO_MINUTES_SLEEP);

	}
	

	@Test(groups={"CONTENT_INSPECTION", "GMAIL", "SANITY", "REGRESSSION"}, dataProvider = "verifyMailWithRiskAttachmentDataProvider", dataProviderClass = GmailRBACDataProvider.class, dependsOnMethods={"fetchActivityLogs"})
	public void verifySendMailwithRisksAsAttachment(String key) throws Exception {
		LogUtils.logTestDescription("After sending the message with attached risks, verify the CI logs");
		logValidator.verifyGmailCIActivityLog(contentInspectionLogs, gmailActivities.get(key));
	}

	@Test(priority= -10, groups={"CREATE","GMAIL", "SANITY", "REGRESSSION"}, dataProviderClass = GmailRBACDataProvider.class, dataProvider = "createDraftDataProvider")
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
				ObjectType.Email_Message.name(), ActivityType.Create.name(), null, externalRecipients, inFolder,  null, subject, Sender1);

		gmailActivities.put(key, sendPlainMessageActivity);

	}
	
	@Test(groups={"CREATE", "GMAIL", "SANITY", "REGRESSSION"}, dependsOnMethods={"fetchActivityLogs"})
	public void verifyCreateDraftMessageActivity() throws Exception {
		LogUtils.logTestDescription("After Creating draft , check the activity logs.");
		Reporter.log("All messages:"+MarshallingUtils.marshall(createMessageLogs), true);		
		logValidator.verifyGmailActivityLog(createMessageLogs, gmailActivities.get("createDraftMessageRBAC1"));
	}

	
	@Test(groups={"SEND", "RECEIVE","CREATE","DELETE","TRASH", "GMAIL", "SANITY", "CONTENT_INSPECTION", "REGRESSSION"})
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

//		//If total number of logs is less than zero, no logs are seen so skip the tests
//		if (total <= 0) {
//			throw new SkipException("Skipping the dependent tests..No logs are seen for sent messages in gmail");
//		}
	}

	@Test(dependsOnMethods={"fetchActivityLogs"}, dataProvider = "RBACSendMailkey", groups={"SEND", "GMAIL", "SANITY", "REGRESSSION"}, dataProviderClass = GmailRBACDataProvider.class)
	public void verifySendPlainMailActivityRBAC(String key) throws Exception {
		LogUtils.logTestDescription("After sending the plain message, check the activity logs.");
		logValidator.verifyGmailActivityLog(sentMessageLogs, gmailActivities.get(key));
	}
	

	@Test(dependsOnMethods={"fetchActivityLogs"}, dataProvider = "RBACRecieveMailkey", groups={"RECEIVE", "GMAIL", "SANITY", "REGRESSSION"}, dataProviderClass = GmailRBACDataProvider.class)
	public void verifyRecievePlainMailActivityRBAC(String key) throws Exception {
		LogUtils.logTestDescription("After Recieving the plain message, check the activity logs.");
		logValidator.verifyGmailActivityLog(receiveMessageLogs, gmailActivities.get(key));
	}
	
}
