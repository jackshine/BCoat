package com.elastica.beatle.tests.gmail;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.mail.internet.MimeMessage;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.elastica.beatle.MarshallingUtils;
import com.elastica.beatle.RawJsonParser;
import com.elastica.beatle.RetryAnalyzer;
import com.elastica.beatle.securlets.DocumentValidator;
import com.elastica.beatle.securlets.LogValidator;
import com.elastica.beatle.securlets.SecurletsConstants;
import com.elastica.beatle.securlets.dto.ForensicSearchResults;
import com.elastica.beatle.securlets.dto.O365Document;
import com.elastica.beatle.securlets.dto.MailRemediation;
import com.elastica.beatle.securlets.dto.SecurletDocument;
import com.elastica.beatle.tests.securlets.CustomAssertion;
import com.elastica.beatle.tests.securlets.SecurletsCommonUtils;
import com.google.api.services.gmail.model.Draft;
import com.google.api.services.gmail.model.Message;
import com.universal.common.GoogleMailServices;
import com.universal.common.Office365MailActivities;
import com.universal.constants.CommonConstants;

import microsoft.exchange.webservices.data.property.complex.EmailAddressCollection;

public class GmailRemediationTests extends GmailUtils{
	

	GoogleMailServices objMailTestUser1 ;
	GoogleMailServices objMailTestUser2 ;
	GoogleMailServices objMailAdmin ;
	GmailActivityLog gmailActivityLog;
	HashMap<String, GmailActivity> gmailActivities = new HashMap<String, GmailActivity>();
	LogValidator logValidator;
	DocumentValidator docValidator;
	ForensicSearchResults sentMessageLogs;
	ArrayList<String> cleanupListSent = new ArrayList<String>();
	ArrayList<String> cleanupListReceived = new ArrayList<String>();
	ArrayList<File> filesToDelete  = new ArrayList<File>();
	public static final long MAX_LOG_WAIT_TIME_IN_MINUTES 		= 10;
	public static final long MAX_EXPOSURE_WAIT_TIME_IN_MINUTES		= 15;
	public static final long MAX_REMEDIATION_WAIT_TIME_IN_MINUTES	= 15;
	public static final long MAX_EXPOSURE_WAIT_TIME_AFTER_REMEDIATION	= 15;
	
	String clientId;
	String clientSecret;
	String refreshToken;
	String userDir;
	String externalUser1;
	String externalUser2;
	String testUser1;
	String testUser2;
	String externalUser1Pwd;
	String externalUser2Pwd;
	String testUser1Pwd;
	String testUser2Pwd;
	String adminUser;
	String adminPwd;
	String groupMailId;
	Office365MailActivities objMailExternalUser2;
	
	
	public GmailRemediationTests() throws Exception {
		super();
		// TODO Auto-generated constructor stub
	}
	
	@BeforeClass
	public void initGmail() throws Exception {
		
		adminUser =  suiteData.getSaasAppUsername();		
		adminPwd = suiteData.getSaasAppPassword();	
		
		externalUser1=suiteData.getSaasAppExternalUser1Name();
		externalUser1Pwd=suiteData.getSaasAppExternalUser1Password();
		
		externalUser2=suiteData.getSaasAppExternalUser2Name();
		externalUser2Pwd=suiteData.getSaasAppExternalUser2Password();
		
		testUser1 = suiteData.getSaasAppEndUser1Name();
		testUser1Pwd = suiteData.getSaasAppEndUser1Password();

		testUser2 = suiteData.getSaasAppEndUser2Name();
		testUser2Pwd = suiteData.getSaasAppEndUser2Password();
		
		
		this.clientId = getRegressionSpecificSuitParameters("saasAppClientId");
		this.clientSecret = getRegressionSpecificSuitParameters("saasAppClientSecret");
		this.refreshToken = getRegressionSpecificSuitParameters("saasAppToken");
		this.objMailAdmin = new GoogleMailServices(clientId, clientSecret, refreshToken,adminUser);
		this.objMailTestUser1 = new GoogleMailServices(getRegressionSpecificSuitParameters("saasAppEndUser1ClientId"), getRegressionSpecificSuitParameters("saasAppEndUser1ClientSecret"), getRegressionSpecificSuitParameters("saasAppEndUser1Token"),testUser1);
		this.objMailTestUser2 = new GoogleMailServices(getRegressionSpecificSuitParameters("saasAppEndUser2ClientId"), getRegressionSpecificSuitParameters("saasAppEndUser2ClientSecret"), getRegressionSpecificSuitParameters("saasAppEndUser2Token"),testUser2);
		gmailActivityLog = new GmailActivityLog();
		logValidator = new LogValidator(); 
		docValidator = new DocumentValidator();
		
	
		objMailExternalUser2 = new Office365MailActivities(externalUser2,externalUser2Pwd);
		
		//setting upload file path
		userDir = System.getProperty("user.dir")+"/src/test/resources/uploads/office365/";
		
		groupMailId ="group@securletbeatle.com";
				
	}
	
	@AfterClass(alwaysRun=true)
	public void cleanup() throws Exception {

			Reporter.log("Inside cleanup",true);

			deleteFiles(filesToDelete);
//			deleteMails(cleanupListSent,"SentItems",testUser1,testUser1Pwd);
//			deleteMails(cleanupListReceived,"Inbox",testUser1,testUser1Pwd);
//			deleteMails(cleanupListDrafts,"Drafts",testUser1,testUser1Pwd);
//			deleteMails(cleanupListSent,"Inbox",testUser2,testUser2Pwd);
			
			
	}
	
	@Test(groups={"SANITY"})
	public void verifyMailExposureAndRemediationDeleteMail() throws Exception
	{
		verifyRemediationForMailOrAttachmentExposure("true", 	"open", 	"pii", 			"PII.rtf", 	   		"PII" ,						"attachment",   "ITEM_TRASH_MAIL");
		
	}
	
	@DataProvider(parallel=true)
	public Object[][] dataProviderRemediationMailOrAttachment() {
		return new Object[][]{
			/* is_internal  
			 * exposure type 
			 * violation type for API
			 * filename
			 * violation name for log comparison
			 * content location
			 * remedial action 
			 *    
			 *  */
			
			{"true", 	"open", 	"pii", 			"PII.rtf", 	   		"PII" ,						"attachment",   "ITEM_TRASH_MAIL"},
			{"true", 	"open",		"pci", 			"PCI_Test.txt", 	"PCI" , 					"attachment",   "ITEM_TRASH_MAIL"},
//			{"true", 	"open", 	"source_code",	"Hello.java",     	"Source Code", 				"attachment",   "ITEM_TRASH_MAIL"},
			{"true", 	"open", 	"hipaa", 		"hipaa.txt",      	"HIPAA", 					"attachment",   "ITEM_TRASH_MAIL"},
//			{"true", 	"open", 	"encryption", 	"encryption.bin", 	"ENCRYPTION", 				"attachment",   "ITEM_TRASH_MAIL"},
			{"true", 	"open", 	"ferpa", 		"ferpa.pdf",     	"FERPA", 					"attachment",   "ITEM_TRASH_MAIL"},
			{"true", 	"open", 	"glba", 		"glba.txt",     	"GLBA", 					"attachment",   "ITEM_TRASH_MAIL"},
			{"true", 	"open", 	"vba_macros", 	"vba_macro.xls", 	"VBA Macros, Source Code", 	"attachment",   "ITEM_TRASH_MAIL"},
			{"true", 	"open", 	"virus", 		"virus.zip",   		"Virus / Malware", 			"attachment",   "ITEM_TRASH_MAIL"},
			{"true", 	"open", 	"pii", 			"PII.rtf", 	   		"PII" ,						"body" ,	    "ITEM_TRASH_MAIL"},
			{"true", 	"open", 	"pci", 			"PCI_Test.txt",  	"PCI" , 					"body", 		"ITEM_TRASH_MAIL"},
//			{"true", 	"open", 	"source_code",	"Hello.java",     	"Source Code", 				"body", 		"ITEM_TRASH_MAIL"},
			{"true", 	"open", 	"hipaa", 		"hipaa.txt",     	"HIPAA", 					"body",			"ITEM_TRASH_MAIL"}
		};
	}
	
	/**
	 * Send an email with content violation
	 * After that check with  exposure api (API call), apply the remedial actions and check file exposure changed or not.
	 * @param isInternal
	 * @param exposuretype
	 * @param violationType
	 * @param fileName
	 * @param violationName
	 * @param contentPlace
	 * @param remedialAction
	 * @throws Exception
	 */
	@Test(groups={"P1"},dataProvider = "dataProviderRemediationMailOrAttachment")
	public void verifyRemediationForMailOrAttachmentExposure(
			String isInternal, 
			String exposuretype,
			String violationType, 
			String fileName,
			String violationName,
			String contentPlace, 
			String remedialAction
			) throws Exception {
		
		Reporter.log("****************Test Case Description****************",true);
		Reporter.log("Test Case Description: Send an email with content violation "+violationName+" in mail "+contentPlace+" to external user. Verify the exposure and remediate the same, ",true);
		Reporter.log("1)Send Mail with "+violationName+" information to external user",true);
		Reporter.log("2)Verify that the mail/attachment is exposed and remediate the same using "+remedialAction,true);
		Reporter.log("3)Verify that the remediation is successful and mail is deleted from sentItems folder of sender",true);
		Reporter.log("4)Verify that the mail or attachment is removed from exposure",true);
		Reporter.log("5)Verify that the remediation message also appears in the logs",true);
		Reporter.log("*****************************************************",true);
		
		
		//Prepare the remediation object
		Message emailObj = null;
		String myUniqueId = UUID.randomUUID().toString();
		ArrayList<String> myAttachment = new ArrayList<String>() ;
		String newFileName = myUniqueId+fileName;
		File sourceFile1 =null;
		File destFile1 = null;
		String myMailSubject =null;
		String mailBody = null;
		boolean success =false;
		String docTypeForRemediation = null;
		String searchFileName =null;
		int countAfterExposure =0;
		int responseCode = 0;
		List<String> to	= new ArrayList<>();
		List<NameValuePair> qparams = new ArrayList<NameValuePair>(); 
		qparams.add(new BasicNameValuePair(SecurletsConstants.IS_INTERNAL,  "true"));
		SecurletDocument exposureDoc = new SecurletDocument();
		String docId;

		
		//Prepare the violations
		ArrayList<String> violations = new ArrayList<String>();
		violations.add(violationType);
		
		
		sourceFile1 = new File(userDir +fileName);
		destFile1 = new File(userDir +newFileName);
		
		Reporter.log("------------------------------------------------------------------------",true );
		Reporter.log("Sending mail to external user",true);
		Reporter.log("------------------------------------------------------------------------",true );
		
		
		if(contentPlace.equals("attachment")){
			
			docTypeForRemediation = "Email_File_Attachment";
			myMailSubject = myUniqueId+"ViolationForExposureInAttachment"+fileName;
			searchFileName=newFileName;
			myAttachment.add(destFile1.toString());
			mailBody= "This is test mail body";
			
			synchronized(this){ 
				//sending mail
				to.add(externalUser1);
				success = objMailTestUser1.sendMessageWithAttachment(to, null, null, myMailSubject, mailBody,sourceFile1.toString(),newFileName);
			}
			
		}
		else if(contentPlace.equals("body")){
			docTypeForRemediation = "Email_Message";
			myMailSubject = myUniqueId+"ViolationForExposureInMailBody"+violationType;
			mailBody =readFile(sourceFile1.toString());
			searchFileName=myMailSubject;
			
			synchronized(this){ 
				//sending mail
				to.add(externalUser1);
				success = objMailTestUser1.sendPlainMessage(to, null, null, myMailSubject, mailBody);
			}
		}
		cleanupListSent.add(myMailSubject);
		assertTrue(success, "Failed sending mail with subject:"+myMailSubject+".");
		success=false;
		qparams.add(new BasicNameValuePair("name",  searchFileName));
		
		
		//wait for 1 minutes for the exposure to be applied
		//		Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
		
		Reporter.log("------------------------------------------------------------------------",true );
		
		int i = 60000;
		for (; i <= (MAX_EXPOSURE_WAIT_TIME_IN_MINUTES * 60 * 1000); i+=60000 ) {
			Reporter.log("Checking exposure after :"+TimeUnit.MILLISECONDS.toMinutes(i)+" minute(s)",true );
			Reporter.log("------------------------------------------------------------------------",true );
			Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
			
			
			//Get the exposure count
			exposureDoc = this.getExposedDocuments(elapp.el_google_apps.name(), qparams);
			countAfterExposure = exposureDoc.getMeta().getTotalCount();
			Reporter.log("Exposure Count ="+countAfterExposure, true);
			
			if (countAfterExposure >= 1) {break;}
		}
		
		assertEquals(countAfterExposure,1,"File not exposed even after waiting for "+TimeUnit.MILLISECONDS.toMinutes(i-60000)+" minutes.");
		docId = exposureDoc.getObjects().get(0).getIdentification();
		
		Reporter.log("----------------Remediating the file--------------------",true );
		
		
		//creating the remediation object
		MailRemediation objRemediation = getRemediationObject(adminUser, docTypeForRemediation, docId, remedialAction);
		
		/*//Now apply the remedial action thro' UI server or API server call
		if(service.equals(Server.UIServer.name())) {
			remediateExposure(suiteData.getTenantName(), facility.Office365.name(), suiteData.getUsername(), docId, null, remedialAction);

		} else if(service.equals(Server.APIServer.name())) {
			remediateExposureWithAPI(O365Remediation);
		} */
		
		//Now apply the remedial action through API server call
		responseCode = remediateExposureWithAPI(objRemediation);
		Assert.assertEquals(responseCode, 202, "Remediation call failed.");
		
		//Wait for remedial action
		Reporter.log("---------------------Waiting for the remedial action in SAAS APP-----------------------------",true );
		
		i = 60000;
		boolean remediationSuccess = false;
		for (; i <= (MAX_REMEDIATION_WAIT_TIME_IN_MINUTES * 60 * 1000); i+=60000 ) {
			Reporter.log("Checking if mail deleted after :"+TimeUnit.MILLISECONDS.toMinutes(i)+" minute(s)",true );
			Reporter.log("------------------------------------------------------------------------",true );
			Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
			
			synchronized (this) {
				//Searching for email in mailbox sent items
				emailObj = objMailTestUser1.getMessageWithSubject(myMailSubject, "SENT");
			}
			
			//set remediationSuccess as true if mail or attachment deleted based on remediation type
//			if (remedialAction.equals(Remediation.ITEM_DELETE_MAIL_BY_ATTACHMENT.name()) || remedialAction.equals(Remediation.ITEM_TRASH_MAIL.name())) {
			if ( remedialAction.equals(Remediation.ITEM_TRASH_MAIL.name())) {
				
				if(emailObj==null){
					remediationSuccess=true;
					
				}
				
			} 
//			else if(remedialAction.equals(Remediation.ITEM_DELETE_ATTACHMENT.name())) {
//				if(emailObj!=null){
//					//searching if attachments available
//					synchronized (this) {
//						//TODO:ADD subject
//						//emailObj = objMailTestUser1New.findItemInFolderReturnWithAttachment(myMailSubject, "SentItems");
//					}
//					if(emailObj.getHasAttachments()==false){
//						remediationSuccess=true;
//					}
//
//				}
//
//			}
			
			
			if (remediationSuccess == true) {
				Reporter.log("Remediation is successful as the Email is deleted.",true);
				break;
			}
			else{
				Reporter.log("Mail/Attachment not yet deleted after remediation",true);
			}
			
		}
		
		
		String expectedLog = null;
		//Verify the remediation thro' o365 API
//		if (remedialAction.equals(Remediation.ITEM_DELETE_MAIL_BY_ATTACHMENT.name()) || remedialAction.equals(Remediation.ITEM_TRASH_MAIL.name())) {
		if (remedialAction.equals(Remediation.ITEM_TRASH_MAIL.name())) {
//			expectedLog = "QA Admin has remediated/deleted an email with subject \""+myMailSubject+"\"";
			expectedLog = "User trashed email with subject \""+myMailSubject+"\"";
			assertNull(emailObj, "Remediation "+ remedialAction + " didn't work. Mail is not deleted even after waiting for "+TimeUnit.MILLISECONDS.toMinutes(i-60000)+" minutes.");
			
		}
//		else if(remedialAction.equals(Remediation.ITEM_DELETE_ATTACHMENT.name())) {
//			expectedLog = "QA Admin has remediated/deleted an attachment \""+newFileName+"\" from an email with subject \""+myMailSubject+"\"";
//			assertNotNull(emailObj, "Mail Not Found");
//			assertFalse(emailObj.getHasAttachments(), "Remediation "+ remedialAction + ". Attachment is not deleted even after waiting for "+TimeUnit.MILLISECONDS.toMinutes(i-60000)+" minutes.");
//		}
		
//		Reporter.log("Remediation successful at GMAIL");
		
		//gather the forensic logs again 
		//logs = gatherForensicLogMessages(uniqueId, facility.Box.name());
		
		Reporter.log("------------------------------------------------------------------------",true );
		
		int countAfterRemediation = countAfterExposure ;
		i = 60000;
		for (; i <= (MAX_EXPOSURE_WAIT_TIME_AFTER_REMEDIATION * 60 * 1000); i+=60000 ) {
			Reporter.log("Wait for "+TimeUnit.MILLISECONDS.toMinutes(i)+" minutes, and check if the file is removed from exposure.",true);
			Reporter.log("------------------------------------------------------------------------",true );
			Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
			exposureDoc = this.getExposedDocuments(elapp.el_google_apps.name(), qparams);
			countAfterRemediation = exposureDoc.getMeta().getTotalCount();
			
			if (countAfterRemediation == 0) {
				break;
			}
			
		}
		
		Reporter.log("######" + violationType + " exposure count after remediation::"+countAfterRemediation, true);
		assertEquals(countAfterRemediation, 0, "Exposure count has not reduced after remediation,even after waiting for "+TimeUnit.MILLISECONDS.toMinutes(i-60000)+" minutes.");
		
		//Verify the activity logs
		Reporter.log("Expected log:" + expectedLog, true);
		ForensicSearchResults availableLogs = searchLogsWithWaitTime("Activity_type", "Trash", myUniqueId, testUser1, 1, 2);
		assertNotNull(availableLogs,"Remediation successful, but delete activity log not found after remediation");
		Reporter.log("            ",true);
		CustomAssertion.assertEquals(availableLogs.getHits().getHits().get(0).getSource().getMessage(),expectedLog,	"Remediation successful, but delete activity log not matching.");
		
		
		
	}
	@DataProvider(parallel=true)
	public Object[][] dataProviderRemediationMailAndAttachment() {
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
			
			{"true", 	"open", 	"pii", 			"PII.rtf", 	   		"PII" ,						"attachment"},
			{"true", 	"open",		"pci", 			"PCI_Test.txt", 	"PCI" , 					"attachment"},
//			{"true", 	"open", 	"source_code",	"Hello.java",     	"Source Code", 				"attachment"},
			{"true", 	"open", 	"hipaa", 		"hipaa.txt",      	"HIPAA", 					"attachment"},
//			{"true", 	"open", 	"encryption", 	"encryption.bin", 	"ENCRYPTION", 				"attachment"},
			{"true", 	"open", 	"ferpa", 		"ferpa.pdf",     	"FERPA", 					"attachment"},
			{"true", 	"open", 	"glba", 		"glba.txt",     	"GLBA", 					"attachment"},
			{"true", 	"open", 	"vba_macros", 	"vba_macro.xls", 	"VBA Macros, Source Code", 	"attachment"},
			{"true", 	"open", 	"virus", 		"virus.zip",   		"Virus / Malware", 			"attachment"},
			{"true", 	"open", 	"pii", 			"PII.rtf", 	   		"PII" ,						"mail"},
			{"true", 	"open",		"pci", 			"PCI_Test.txt", 	"PCI" , 					"mail"},
//			{"true", 	"open", 	"source_code",	"Hello.java",     	"Source Code", 				"mail"},
			{"true", 	"open", 	"hipaa", 		"hipaa.txt",      	"HIPAA", 					"mail"},
//			{"true", 	"open", 	"encryption", 	"encryption.bin", 	"ENCRYPTION", 				"mail"},
			{"true", 	"open", 	"ferpa", 		"ferpa.pdf",     	"FERPA", 					"mail"},
			{"true", 	"open", 	"glba", 		"glba.txt",     	"GLBA", 					"mail"},
			{"true", 	"open", 	"vba_macros", 	"vba_macro.xls", 	"VBA Macros, Source Code", 	"mail"},
			{"true", 	"open", 	"virus", 		"virus.zip",   		"Virus / Malware", 			"mail"},
		};
	}
    
	/**
	 * Send an email with content violation in mailbody and attachment
	 * After that check with  exposure api (API call), apply the remedial actions and check file exposure changed or not.
	 * @param isInternal
	 * @param exposuretype
	 * @param violationType
	 * @param fileName
	 * @param violationName
	 * @param remediationType
	 * @throws Exception
	 */
	@Test(groups={"P1"},dataProvider = "dataProviderRemediationMailAndAttachment")
	public void verifyRemediationForMailAndAttachmentExposure(
			String isInternal, 
			String exposuretype,
			String violationType, 
			String fileName,
			String violationName,
			String remediationType
			) throws Exception {

		Reporter.log("****************Test Case Description****************",true);
		Reporter.log("Test Case Description: Send an email with content violation "+violationName+" in mail and attachment to external user. Verify the exposure and remediate the same, ",true);
		Reporter.log("1)Send Mail with "+violationName+" information to external user",true);
		Reporter.log("2)Verify that the mail and attachment are exposed and remediate the "+remediationType,true);
		Reporter.log("3)Verify that the remediation is successful and mail is deleted from sentItems folder of sender",true);
		Reporter.log("4)Verify that the mail and attachment are removed from exposure",true);
		Reporter.log("5)Verify that the remediation message also appears in the logs",true);
		Reporter.log("*****************************************************",true);


		//Prepare the remediation object
		Message emailObj = null;
		String myUniqueId = UUID.randomUUID().toString();
		ArrayList<String> myAttachment = new ArrayList<String>() ;
		String newFileName = myUniqueId+fileName;
		File sourceFile1 =null;
		File destFile1 = null;
		String myMailSubject =null;
		String mailBody = null;
		boolean success =false;
		String docTypeForRemediation = null;
		String searchFileName1 =null;
		String searchFileName2 =null;
		int countAfterExposure =0;
		int responseCode = 0;
		String expectedLog = null;
		List<String> to	= new ArrayList<>();
		List<NameValuePair> qparamsForFile1 = new ArrayList<NameValuePair>(); 
		List<NameValuePair> qparamsForFile2 = new ArrayList<NameValuePair>(); 
		qparamsForFile1.add(new BasicNameValuePair(SecurletsConstants.IS_INTERNAL,  "true"));
		qparamsForFile2.add(new BasicNameValuePair(SecurletsConstants.IS_INTERNAL,  "true"));
		SecurletDocument exposureDoc1 = new SecurletDocument();
		SecurletDocument exposureDoc2 = new SecurletDocument();
		String remedialAction ="ITEM_TRASH_MAIL";
		//Prepare the violations
		ArrayList<String> violations = new ArrayList<String>();
		violations.add(violationType);
		String docId=null;


		String MailBodyFile ="PCI_Test.txt";
		sourceFile1 = new File(userDir +fileName);
		destFile1 = new File(userDir +newFileName);

		Reporter.log("------------------------------------------------------------------------",true );
		Reporter.log("Sending mail to external user",true);
		Reporter.log("------------------------------------------------------------------------",true );
		
		myMailSubject = myUniqueId+"ViolationInMailAndAttachment"+MailBodyFile+"And"+fileName;
		searchFileName1=myMailSubject;
		searchFileName2=newFileName;
		myAttachment.add(destFile1.toString());
		mailBody= readFile(userDir +MailBodyFile);
		

		qparamsForFile1.add(new BasicNameValuePair("name",  searchFileName1));
		qparamsForFile2.add(new BasicNameValuePair("name",  searchFileName2));

		
		synchronized(this){ 
			//sending mail
			to.add(externalUser1);
			success = objMailTestUser1.sendMessageWithAttachment(to, null, null, myMailSubject, mailBody,sourceFile1.toString(),newFileName);
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
			exposureDoc1 = this.getExposedDocuments(elapp.el_google_apps.name(), qparamsForFile1);
			countAfterExposure = exposureDoc1.getMeta().getTotalCount();
			Reporter.log("Exposure Count ="+countAfterExposure, true);
			
			if (countAfterExposure >= 1) {break;}
		}
		assertEquals(countAfterExposure,1,"File:"+searchFileName1 +" not exposed even after waiting for "+TimeUnit.MILLISECONDS.toMinutes(i-60000)+" minutes.");
		
		Reporter.log("------------------------------------------------------------------------",true );

		for (; i <= (MAX_EXPOSURE_WAIT_TIME_IN_MINUTES * 60 * 1000); i+=60000 ) {
			Reporter.log("Checking exposure after :"+TimeUnit.MILLISECONDS.toMinutes(i)+" minute(s)",true );
			Reporter.log("------------------------------------------------------------------------",true );
			Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);


			//Get the exposure count
			exposureDoc2 = this.getExposedDocuments(elapp.el_google_apps.name(), qparamsForFile2);
			countAfterExposure = exposureDoc2.getMeta().getTotalCount();
			Reporter.log("Exposure Count ="+countAfterExposure, true);

			if (countAfterExposure >= 1) {break;}
		}
		assertEquals(countAfterExposure,1,"File:"+searchFileName2 +" not exposed even after waiting for "+TimeUnit.MILLISECONDS.toMinutes(i-60000)+" minutes.");



		//		Reporter.log("----------------Making API call to get documented Id--------------------",true );
		 if(remediationType.equals("mail")){
				docTypeForRemediation = "Email_Message";
				docId = exposureDoc1.getObjects().get(0).getIdentification();
		}
		 else if(remediationType.equals("attachment")){

			docTypeForRemediation = "Email_File_Attachment";
			docId = exposureDoc2.getObjects().get(0).getIdentification();
		}

		Reporter.log("----------------Remediating the file--------------------",true );


		//creating the remediation object
		MailRemediation objRemediation = getRemediationObject(adminUser, docTypeForRemediation, docId, remedialAction);


		//Now apply the remedial action through API server call
		responseCode = remediateExposureWithAPI(objRemediation);
		//Assert.assertEquals(responseCode, 202, "Remediation call failed.");

		//Wait for remedial action
		Reporter.log("---------------------Waiting for the remedial action in SAAS APP-----------------------------",true );

		i = 60000;
		boolean remediationSuccess = false;
		for (; i <= (MAX_REMEDIATION_WAIT_TIME_IN_MINUTES * 60 * 1000); i+=60000 ) {
			Reporter.log("Checking if mail deleted after :"+TimeUnit.MILLISECONDS.toMinutes(i)+" minute(s)",true );
			Reporter.log("------------------------------------------------------------------------",true );
			Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);

			synchronized (this) {
				//Searching for email in mailbox sent items
				emailObj = objMailTestUser1.getMessageWithSubject(myMailSubject, "SENT");
			}

			//set remediationSuccess as true if mail or attachment deleted based on remediation type
//			if (remedialAction.equals(Remediation.ITEM_DELETE_MAIL_BY_ATTACHMENT.name()) || remedialAction.equals(Remediation.ITEM_TRASH_MAIL.name())) {
			if ( remedialAction.equals(Remediation.ITEM_TRASH_MAIL.name())) {

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
//		if (remedialAction.equals(Remediation.ITEM_DELETE_MAIL_BY_ATTACHMENT.name()) || remedialAction.equals(Remediation.ITEM_TRASH_MAIL.name())) {
		if (remedialAction.equals(Remediation.ITEM_TRASH_MAIL.name())) {
			expectedLog = "User trashed email with subject \""+myMailSubject+"\"";//expectedLog = "QA Admin has remediated/deleted an email with subject \""+myMailSubject+"\"";
			assertNull(emailObj, "Remediation "+ remedialAction + " didn't work. Mail is not deleted even after waiting for "+TimeUnit.MILLISECONDS.toMinutes(i-60000)+" minutes.");

		}

		Reporter.log("Remediation successful at GMAIL");

		//gather the forensic logs again 
		//logs = gatherForensicLogMessages(uniqueId, facility.Box.name());

		Reporter.log("------------------------------------------------------------------------",true );

		int countAfterRemediation = countAfterExposure ;
		i = 60000;
		for (; i <= (MAX_EXPOSURE_WAIT_TIME_AFTER_REMEDIATION * 60 * 1000); i+=60000 ) {
			Reporter.log("Wait for "+TimeUnit.MILLISECONDS.toMinutes(i)+" minutes, and check if the file:"+searchFileName1 +" is removed from exposure.",true);
			Reporter.log("------------------------------------------------------------------------",true );
			Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
			exposureDoc1 = this.getExposedDocuments(elapp.el_google_apps.name(), qparamsForFile1);
			countAfterRemediation = exposureDoc1.getMeta().getTotalCount();
			
			if (countAfterRemediation == 0) {
				break;
			}
			
		}
		Reporter.log("######" + violationType + " exposure count after remediation::"+countAfterRemediation, true);
		assertEquals(countAfterRemediation, 0, "Exposure count has not reduced after remediation.");
		
		for (; i <= (MAX_EXPOSURE_WAIT_TIME_AFTER_REMEDIATION * 60 * 1000); i+=60000 ) {
			Reporter.log("Wait for "+TimeUnit.MILLISECONDS.toMinutes(i)+" minutes, and check if the file:"+searchFileName2 +" is removed from exposure.",true);
			Reporter.log("------------------------------------------------------------------------",true );
			Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
			exposureDoc2 = this.getExposedDocuments(elapp.el_google_apps.name(), qparamsForFile2);
			 countAfterRemediation = exposureDoc2.getMeta().getTotalCount();
			
			if (countAfterRemediation == 0) {
				break;
			}

		}
		Reporter.log("######" + violationType + " exposure count after remediation::"+countAfterRemediation, true);
		assertEquals(countAfterRemediation, 0, "Exposure count has not reduced after remediation.");

		//Verify the activity logs
		Reporter.log("Expected log:" + expectedLog, true);
		ForensicSearchResults availableLogs = searchLogsWithWaitTime("Activity_type", "Trash", myUniqueId, testUser1, 1, 2);
		assertNotNull(availableLogs,"Delete log not found after remediation");
		assertEquals(availableLogs.getHits().getHits().get(0).getSource().getMessage(),expectedLog,	"Test failed, as expected log is not found after remediation by admin.");



	}
	
	
	/**
	 * Receive an email with content violation in mailbody and multiple attachments from external user
	 * After that check with  exposure api (API call), delete the email from inbox and check file exposure changed or not.
	 * @param isInternal
	 * @param exposuretype
	 * @throws Exception
	 */
	@Test(groups={"P1"},retryAnalyzer=RetryAnalyzer.class)
	public void verifyExposureWhenMultipleAttachmentMailDeletedExternalSingle(
			) throws Exception {
		
		Reporter.log("****************Test Case Description****************",true);
		Reporter.log("Test Case Description: Receive an email with content violation in mail and attachment from external user. Verify the exposure and delete the same, ",true);
		Reporter.log("1)Receive a Mail with risk in body and mulitple attachments from external user",true);
		Reporter.log("2)Verify that the mail and attachment are exposed, sender and receiver appears in Users, Collabs list accordingly",true);
		Reporter.log("3)Delete mail from inbox.",true);
		Reporter.log("4)Verify that the mail and attachment are removed from exposure",true);
		Reporter.log("*****************************************************",true);
		
		
		String isInternal ="false";
		Message emailObj = null;
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
		String mailBody = readFile(sourceFile2.toString());
		boolean success =false;
		String docTypeForRemediation = null;
		int countAfterExposure =0;
		int responseCode = 0;
		String expectedLog = null;
		List<String> to	= new ArrayList<>();
		List<NameValuePair> qparamsForMail = new ArrayList<NameValuePair>(); 
		List<NameValuePair> qparamsForFile1 = new ArrayList<NameValuePair>(); 
		List<NameValuePair> qparamsForFile2 = new ArrayList<NameValuePair>(); 
		SecurletDocument mailExpoDoc = new SecurletDocument();
		SecurletDocument exposureDoc1 = new SecurletDocument();
		SecurletDocument exposureDoc2 = new SecurletDocument();
		SecurletDocument usersDoc1 = new SecurletDocument();
		SecurletDocument usersDoc2 = new SecurletDocument();
		SecurletDocument usersDoc3 = new SecurletDocument();
		String remedialAction ="ITEM_TRASH_MAIL";
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
		
		
		synchronized(this){ 
			if(isInternal.equals("true")){
				//sending mail
				to.add(externalUser1);
				success = objMailTestUser1.sendMessageWithMultipleAttachment(to, null, null, myMailSubject, mailBody,myAttachments);
			}
			else if(isInternal.equals("false")){
				success = objMailExternalUser2.sendMail(testUser1, myMailSubject,mailBody ,myAttachments , false);
			}
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
			mailExpoDoc = this.getExposedDocuments(elapp.el_google_apps.name(), qparamsForMail);
			countAfterExposure = mailExpoDoc.getMeta().getTotalCount();
			Reporter.log("Exposure Count ="+countAfterExposure, true);
			
			if (countAfterExposure >= 1) {break;}
		}
		assertEquals(countAfterExposure,1,"File:"+myMailSubject +" not exposed even after waiting for "+TimeUnit.MILLISECONDS.toMinutes(i-60000)+" minutes.");
		//Check the user and collaborator list
		usersDoc1 = getCollaborators(elapp.el_google_apps.name(), qparamsForMail);
		assertTrue(usersDoc1.getObjects().get(0).getEmail().contains(testUser1),"User email not found in collaborators list:"+testUser1 +".");		
//		usersDoc2 = getExposedUsers(elapp.el_google_apps.name(), qparamsForMail);
//		assertTrue(usersDoc2.getObjects().get(0).getEmail().contains(testUser1),"User email not found in owner list:"+testUser1 +".");
		
		for (; i <= (MAX_EXPOSURE_WAIT_TIME_IN_MINUTES * 60 * 1000); i+=60000 ) {
			Reporter.log("Checking exposure after :"+TimeUnit.MILLISECONDS.toMinutes(i)+" minute(s)",true );
			Reporter.log("------------------------------------------------------------------------",true );
			Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
			
			
			//Get the exposure count
			exposureDoc1 = this.getExposedDocuments(elapp.el_google_apps.name(), qparamsForFile1);
			countAfterExposure = exposureDoc1.getMeta().getTotalCount();
			Reporter.log("Exposure Count ="+countAfterExposure, true);
			
			if (countAfterExposure >= 1) {break;}
		}
		assertEquals(countAfterExposure,1,"File:"+searchFileName1 +" not exposed even after waiting for "+TimeUnit.MILLISECONDS.toMinutes(i-60000)+" minutes.");
		
		//Check the user and collaborator list
		usersDoc1 = getCollaborators(elapp.el_google_apps.name(), qparamsForFile1);
		assertTrue(usersDoc1.getObjects().get(0).getEmail().contains(testUser1),"User email not found in collaborators list:"+testUser1 +".");		
//		usersDoc2 = getExposedUsers(elapp.el_google_apps.name(), qparamsForFile1);
//		assertTrue(usersDoc2.getObjects().get(0).getEmail().contains(testUser1),"User email not found in owner list:"+testUser1 +".");
		
		Reporter.log("------------------------------------------------------------------------",true );
		
		for (; i <= (MAX_EXPOSURE_WAIT_TIME_IN_MINUTES * 60 * 1000); i+=60000 ) {
			Reporter.log("Checking exposure after :"+TimeUnit.MILLISECONDS.toMinutes(i)+" minute(s)",true );
			Reporter.log("------------------------------------------------------------------------",true );
			Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
			
			
			//Get the exposure count
			exposureDoc2 = this.getExposedDocuments(elapp.el_google_apps.name(), qparamsForFile2);
			countAfterExposure = exposureDoc2.getMeta().getTotalCount();
			Reporter.log("Exposure Count ="+countAfterExposure, true);
			
			if (countAfterExposure >= 1) {break;}
		}
		assertEquals(countAfterExposure,1,"File:"+searchFileName2 +" not exposed even after waiting for "+TimeUnit.MILLISECONDS.toMinutes(i-60000)+" minutes.");
		
		//Check the user and collaborator list
		usersDoc1 = getCollaborators(elapp.el_google_apps.name(), qparamsForFile2);
		assertTrue(usersDoc1.getObjects().get(0).getEmail().contains(testUser1),"User email not found in collaborators list:"+testUser1 +".");		
//		usersDoc2 = getExposedUsers(elapp.el_google_apps.name(), qparamsForFile2);
//		assertTrue(usersDoc2.getObjects().get(0).getEmail().contains(testUser1),"User email not found in owner list:"+testUser1 +".");
		
		
		Reporter.log("----------------Delete the mail from inbox--------------------",true );
		
		
		Message message=objMailTestUser1.getLatestMail(myMailSubject);
		String threadId=message.getThreadId();
		success = objMailTestUser1.trashMessage(threadId);
		Assert.assertTrue(success, "Mail deletion failed.");
		
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
				emailObj = objMailTestUser1.getMessageWithSubject(myMailSubject, "INBOX");
			}
			
			//set remediationSuccess as true if mail or attachment deleted based on remediation type
//			if (remedialAction.equals(Remediation.ITEM_DELETE_MAIL_BY_ATTACHMENT.name()) || remedialAction.equals(Remediation.ITEM_TRASH_MAIL.name())) {
			if ( remedialAction.equals(Remediation.ITEM_TRASH_MAIL.name())) {
				
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
//		if (remedialAction.equals(Remediation.ITEM_DELETE_MAIL_BY_ATTACHMENT.name()) || remedialAction.equals(Remediation.ITEM_TRASH_MAIL.name())) {
		if (remedialAction.equals(Remediation.ITEM_TRASH_MAIL.name())) {
			expectedLog = "User trashed email with subject \""+myMailSubject+"\"";//expectedLog = "QA Admin has remediated/deleted an email with subject \""+myMailSubject+"\"";
			assertNull(emailObj, "Remediation "+ remedialAction + " didn't work. Mail is not deleted even after waiting for "+TimeUnit.MILLISECONDS.toMinutes(i-60000)+" minutes.");
			
		}
		
		Reporter.log("Remediation successful at GMAIL");
		
		
		Reporter.log("------------------------------------------------------------------------",true );
		
		int countAfterRemediation = countAfterExposure ;
		i = 60000;
		for (; i <= (MAX_EXPOSURE_WAIT_TIME_AFTER_REMEDIATION * 60 * 1000); i+=60000 ) {
			Reporter.log("Wait for "+TimeUnit.MILLISECONDS.toMinutes(i)+" minutes, and check if the file:"+myMailSubject +" is removed from exposure.",true);
			Reporter.log("------------------------------------------------------------------------",true );
			Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
			mailExpoDoc = this.getExposedDocuments(elapp.el_google_apps.name(), qparamsForMail);
			countAfterRemediation = mailExpoDoc.getMeta().getTotalCount();
			
			if (countAfterRemediation == 0) {
				break;
			}
			
		}
		Reporter.log("###### exposure count after remediation::"+countAfterRemediation, true);
		assertEquals(countAfterRemediation, 0, "Exposure count has not reduced after remediation.");
		//Check the user and collaborator list
		usersDoc1 = getCollaborators(elapp.el_google_apps.name(), qparamsForMail);
		assertEquals(usersDoc1.getMeta().getTotalCount(), 0, "Collaborator list is still not empty after deletion of email.");			
//		usersDoc2 = getExposedUsers(elapp.el_google_apps.name(), qparamsForMail);
//		assertEquals(usersDoc2.getMeta().getTotalCount(), 0,"Owner is still exposed after deletion of email:"+testUser1 +".");
		
		for (; i <= (MAX_EXPOSURE_WAIT_TIME_AFTER_REMEDIATION * 60 * 1000); i+=60000 ) {
			Reporter.log("Wait for "+TimeUnit.MILLISECONDS.toMinutes(i)+" minutes, and check if the file:"+searchFileName1 +" is removed from exposure.",true);
			Reporter.log("------------------------------------------------------------------------",true );
			Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
			exposureDoc1 = this.getExposedDocuments(elapp.el_google_apps.name(), qparamsForFile1);
			countAfterRemediation = exposureDoc1.getMeta().getTotalCount();
			
			if (countAfterRemediation == 0) {
				break;
			}
			
		}
		Reporter.log("###### exposure count after remediation::"+countAfterRemediation, true);
		assertEquals(countAfterRemediation, 0, "Exposure count has not reduced after remediation.");
		//Check the user and collaborator list
		usersDoc1 = getCollaborators(elapp.el_google_apps.name(), qparamsForFile1);
		assertEquals(usersDoc1.getMeta().getTotalCount(), 0,"Collaborator list is still not empty after deletion of email.");		
//		usersDoc2 = getExposedUsers(elapp.el_google_apps.name(), qparamsForFile1);
//		assertEquals(usersDoc2.getMeta().getTotalCount(), 0,"Owner is still exposed after deletion of email:"+testUser1 +".");
		
		for (; i <= (MAX_EXPOSURE_WAIT_TIME_AFTER_REMEDIATION * 60 * 1000); i+=60000 ) {
			Reporter.log("Wait for "+TimeUnit.MILLISECONDS.toMinutes(i)+" minutes, and check if the file:"+searchFileName2 +" is removed from exposure.",true);
			Reporter.log("------------------------------------------------------------------------",true );
			Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
			exposureDoc2 = this.getExposedDocuments(elapp.el_google_apps.name(), qparamsForFile2);
			countAfterRemediation = exposureDoc2.getMeta().getTotalCount();
			
			if (countAfterRemediation == 0) {
				break;
			}
			
		}
		Reporter.log("###### exposure count after remediation::"+countAfterRemediation, true);
		assertEquals(countAfterRemediation, 0, "Exposure count has not reduced after remediation.");
		//Check the user and collaborator list
		usersDoc1 = getCollaborators(elapp.el_google_apps.name(), qparamsForFile2);
		assertEquals(usersDoc1.getMeta().getTotalCount(), 0, "Collaborator list is still not empty after deletion of email.");		
//		usersDoc2 = getExposedUsers(elapp.el_google_apps.name(), qparamsForFile2);
//		assertEquals(usersDoc2.getMeta().getTotalCount(), 0,"Owner is still exposed after deletion of email:"+testUser1 +".");
		
		//Verify the activity logs
		Reporter.log("Expected log:" + expectedLog, true);
		ForensicSearchResults availableLogs = searchLogsWithWaitTime("Activity_type", "Trash", myUniqueId, testUser1, 1, 2);
		assertNotNull(availableLogs,"Delete log not found after remediation");
		assertEquals(availableLogs.getHits().getHits().get(0).getSource().getMessage(),expectedLog,	"Test failed, as expected log is not found after remediation by admin.");
		
		
		
	}
	
	@DataProvider(parallel=true)
	public Object[][] dataProviderRemediationWhenMultipleAttachmentExposureInternal() {
		return new Object[][]{
		/* is_internal  remediationType deletionType
		 *    
		 *  */
			
			{"true", 	"attachment", "by remediation"},
			{"true", 	"mail",		  "by remediation"},
			{"true", 	"mail",		  "from mailbox"},
		};
	}
	/**
	 * Send an email with content violation in mailbody and multiple attachments
	 * After that check with  exposure api (API call), apply the remedial actions and check file exposure changed or not.
	 * @param isInternal
	 * @param exposuretype
	 * @throws Exception
	 */
	@Test(groups={"P1"},dataProvider = "dataProviderRemediationWhenMultipleAttachmentExposureInternal")
	public void verifyExposureWhenMultipleAttachmentMailDeletedOrRemediatedInternal(
			String isInternal, 
			String remediationType,
			String deletionType
			) throws Exception {
		
		Reporter.log("****************Test Case Description****************",true);
		Reporter.log("Test Case Description: Send an email with content violation in mail and attachment to external user. Verify the exposure and remediate the same, ",true);
		Reporter.log("1)Send Mail with risk in body and mulitple attachments to external user",true);
		Reporter.log("2)Verify that the mail and attachment are exposed, sender and receiver appears in Users, Collabs list accordingly",true);
		Reporter.log("3)Delete mail "+deletionType+" .Verify that the action is successful and mail is deleted from sentItems folder of sender",true);
		Reporter.log("4)Verify that the mail and attachment are removed from exposure",true);
		Reporter.log("5)Verify that the deletion message also appears in the logs",true);
		Reporter.log("*****************************************************",true);
		
		
		//Prepare the remediation object
		Message emailObj = null;
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
		String mailBody= readFile(sourceFile2.toString());
		boolean success =false;
		String docTypeForRemediation = null;
		int countAfterExposure =0;
		int responseCode = 0;
		String expectedLog = null;
		List<String> to	= new ArrayList<>();
		List<NameValuePair> qparamsForMail = new ArrayList<NameValuePair>(); 
		List<NameValuePair> qparamsForFile1 = new ArrayList<NameValuePair>(); 
		List<NameValuePair> qparamsForFile2 = new ArrayList<NameValuePair>(); 
		SecurletDocument mailExpoDoc = new SecurletDocument();
		SecurletDocument exposureDoc1 = new SecurletDocument();
		SecurletDocument exposureDoc2 = new SecurletDocument();
		SecurletDocument usersDoc1 = new SecurletDocument();
		SecurletDocument usersDoc2 = new SecurletDocument();
		SecurletDocument usersDoc3 = new SecurletDocument();
		String remedialAction ="ITEM_TRASH_MAIL";
		//Prepare the violations
		String docId=null;
		
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
				//sending mail
				to.add(externalUser1);
				//TODO: call with multiple attachments
				success = objMailTestUser1.sendMessageWithMultipleAttachment(to, null, null, myMailSubject, mailBody,myAttachments);
			}
			else if(isInternal.equals("false")){
				success = objMailExternalUser2.sendMail(testUser1, myMailSubject,mailBody ,myAttachments , false);
			}
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
			mailExpoDoc = this.getExposedDocuments(elapp.el_google_apps.name(), qparamsForMail);
			countAfterExposure = mailExpoDoc.getMeta().getTotalCount();
			Reporter.log("Exposure Count ="+countAfterExposure, true);
			
			if (countAfterExposure >= 1) {break;}
		}
		assertEquals(countAfterExposure,1,"File:"+myMailSubject +" not exposed even after waiting for "+TimeUnit.MILLISECONDS.toMinutes(i-60000)+" minutes.");
		Thread.sleep(CommonConstants.TWO_MINUTES_SLEEP);
		//Check the user and collaborator list
		usersDoc1 = getCollaborators(elapp.el_google_apps.name(), qparamsForMail);
		assertTrue(usersDoc1.getObjects().get(0).getEmail().contains(externalUser1),"User email not found in collaborators list:"+externalUser1 +".");		
		usersDoc2 = getExposedUsers(elapp.el_google_apps.name(), qparamsForMail);
		assertTrue(usersDoc2.getObjects().get(0).getEmail().contains(testUser1),"User email not found in owner list:"+testUser1 +".");
		
		for (; i <= (MAX_EXPOSURE_WAIT_TIME_IN_MINUTES * 60 * 1000); i+=60000 ) {
			Reporter.log("Checking exposure after :"+TimeUnit.MILLISECONDS.toMinutes(i)+" minute(s)",true );
			Reporter.log("------------------------------------------------------------------------",true );
			Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
			
			
			//Get the exposure count
			exposureDoc1 = this.getExposedDocuments(elapp.el_google_apps.name(), qparamsForFile1);
			countAfterExposure = exposureDoc1.getMeta().getTotalCount();
			Reporter.log("Exposure Count ="+countAfterExposure, true);
			
			if (countAfterExposure >= 1) {break;}
		}
		assertEquals(countAfterExposure,1,"File:"+searchFileName1 +" not exposed even after waiting for "+TimeUnit.MILLISECONDS.toMinutes(i-60000)+" minutes.");
		
		//Check the user and collaborator list
		usersDoc1 = getCollaborators(elapp.el_google_apps.name(), qparamsForFile1);
		assertTrue(usersDoc1.getObjects().get(0).getEmail().contains(externalUser1),"User email not found in collaborators list:"+externalUser1 +".");		
		usersDoc2 = getExposedUsers(elapp.el_google_apps.name(), qparamsForFile1);
		assertTrue(usersDoc2.getObjects().get(0).getEmail().contains(testUser1),"User email not found in owner list:"+testUser1 +".");
		
		Reporter.log("------------------------------------------------------------------------",true );
		
		for (; i <= (MAX_EXPOSURE_WAIT_TIME_IN_MINUTES * 60 * 1000); i+=60000 ) {
			Reporter.log("Checking exposure after :"+TimeUnit.MILLISECONDS.toMinutes(i)+" minute(s)",true );
			Reporter.log("------------------------------------------------------------------------",true );
			Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
			
			
			//Get the exposure count
			exposureDoc2 = this.getExposedDocuments(elapp.el_google_apps.name(), qparamsForFile2);
			countAfterExposure = exposureDoc2.getMeta().getTotalCount();
			Reporter.log("Exposure Count ="+countAfterExposure, true);
			
			if (countAfterExposure >= 1) {break;}
		}
		assertEquals(countAfterExposure,1,"File:"+searchFileName2 +" not exposed even after waiting for "+TimeUnit.MILLISECONDS.toMinutes(i-60000)+" minutes.");
		
		//Check the user and collaborator list
		usersDoc1 = getCollaborators(elapp.el_google_apps.name(), qparamsForFile2);
		assertTrue(usersDoc1.getObjects().get(0).getEmail().contains(externalUser1),"User email not found in collaborators list:"+externalUser1 +".");		
		usersDoc2 = getExposedUsers(elapp.el_google_apps.name(), qparamsForFile2);
		assertTrue(usersDoc2.getObjects().get(0).getEmail().contains(testUser1),"User email not found in owner list:"+testUser1 +".");
		
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
		MailRemediation objRemediation = getRemediationObject(adminUser, docTypeForRemediation, docId, remedialAction);
		
		
		if(deletionType.equals("by remediation")){
			//Now apply the remedial action through API server call
			responseCode = remediateExposureWithAPI(objRemediation);
			//Assert.assertEquals(responseCode, 202, "Remediation call failed.");
		}
		else if (deletionType.equals("from mailbox")){
			Message message=objMailTestUser1.getMessageWithSubject(myMailSubject, "SENT");
			String threadId=message.getThreadId();
			success = objMailTestUser1.trashMessage(threadId);
			Assert.assertTrue(success, "Mail deletion failed.");
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
				emailObj = objMailTestUser1.getMessageWithSubject(myMailSubject, "SENT");
			}
			
			//set remediationSuccess as true if mail or attachment deleted based on remediation type
//			if (remedialAction.equals(Remediation.ITEM_DELETE_MAIL_BY_ATTACHMENT.name()) || remedialAction.equals(Remediation.ITEM_TRASH_MAIL.name())) {
			if ( remedialAction.equals(Remediation.ITEM_TRASH_MAIL.name())) {
				
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
//		if (remedialAction.equals(Remediation.ITEM_DELETE_MAIL_BY_ATTACHMENT.name()) || remedialAction.equals(Remediation.ITEM_TRASH_MAIL.name())) {
		if (remedialAction.equals(Remediation.ITEM_TRASH_MAIL.name())) {
			expectedLog = "User trashed email with subject \""+myMailSubject+"\"";//expectedLog = "QA Admin has remediated/deleted an email with subject \""+myMailSubject+"\"";
			assertNull(emailObj, "Remediation "+ remedialAction + " didn't work. Mail is not deleted even after waiting for "+TimeUnit.MILLISECONDS.toMinutes(i-60000)+" minutes.");
			
		}
		
		Reporter.log("Remediation successful at GMAIL");
		
		//gather the forensic logs again 
		//logs = gatherForensicLogMessages(uniqueId, facility.Box.name());
		
		Reporter.log("------------------------------------------------------------------------",true );
		
		int countAfterRemediation = countAfterExposure ;
		i = 60000;
		for (; i <= (MAX_EXPOSURE_WAIT_TIME_AFTER_REMEDIATION * 60 * 1000); i+=60000 ) {
			Reporter.log("Wait for "+TimeUnit.MILLISECONDS.toMinutes(i)+" minutes, and check if the file:"+myMailSubject +" is removed from exposure.",true);
			Reporter.log("------------------------------------------------------------------------",true );
			Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
			mailExpoDoc = this.getExposedDocuments(elapp.el_google_apps.name(), qparamsForMail);
			countAfterRemediation = mailExpoDoc.getMeta().getTotalCount();
			
			if (countAfterRemediation == 0) {
				break;
			}
			
		}
		Reporter.log("###### exposure count after remediation::"+countAfterRemediation, true);
		assertEquals(countAfterRemediation, 0, "Exposure count has not reduced after remediation.");
		//Check the user and collaborator list
		usersDoc1 = getCollaborators(elapp.el_google_apps.name(), qparamsForMail);
		assertEquals(usersDoc1.getMeta().getTotalCount(), 0, "Collaborator is still exposed after deletion of email:"+externalUser1 +".");			
		usersDoc2 = getExposedUsers(elapp.el_google_apps.name(), qparamsForMail);
		assertEquals(usersDoc2.getMeta().getTotalCount(), 0,"Owner is still exposed after deletion of email:"+testUser1 +".");
		
		for (; i <= (MAX_EXPOSURE_WAIT_TIME_AFTER_REMEDIATION * 60 * 1000); i+=60000 ) {
			Reporter.log("Wait for "+TimeUnit.MILLISECONDS.toMinutes(i)+" minutes, and check if the file:"+searchFileName1 +" is removed from exposure.",true);
			Reporter.log("------------------------------------------------------------------------",true );
			Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
			exposureDoc1 = this.getExposedDocuments(elapp.el_google_apps.name(), qparamsForFile1);
			countAfterRemediation = exposureDoc1.getMeta().getTotalCount();
			
			if (countAfterRemediation == 0) {
				break;
			}
			
		}
		Reporter.log("###### exposure count after remediation::"+countAfterRemediation, true);
		assertEquals(countAfterRemediation, 0, "Exposure count has not reduced after remediation.");
		//Check the user and collaborator list
		usersDoc1 = getCollaborators(elapp.el_google_apps.name(), qparamsForFile1);
		assertEquals(usersDoc1.getMeta().getTotalCount(), 0, "Collaborator is still exposed after deletion of email:"+externalUser1 +".");		
		usersDoc2 = getExposedUsers(elapp.el_google_apps.name(), qparamsForFile1);
		assertEquals(usersDoc2.getMeta().getTotalCount(), 0,"Owner is still exposed after deletion of email:"+testUser1 +".");
		
		for (; i <= (MAX_EXPOSURE_WAIT_TIME_AFTER_REMEDIATION * 60 * 1000); i+=60000 ) {
			Reporter.log("Wait for "+TimeUnit.MILLISECONDS.toMinutes(i)+" minutes, and check if the file:"+searchFileName2 +" is removed from exposure.",true);
			Reporter.log("------------------------------------------------------------------------",true );
			Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
			exposureDoc2 = this.getExposedDocuments(elapp.el_google_apps.name(), qparamsForFile2);
			countAfterRemediation = exposureDoc2.getMeta().getTotalCount();
			
			if (countAfterRemediation == 0) {
				break;
			}
			
		}
		Reporter.log("###### exposure count after remediation::"+countAfterRemediation, true);
		assertEquals(countAfterRemediation, 0, "Exposure count has not reduced after remediation.");
		//Check the user and collaborator list
		usersDoc1 = getCollaborators(elapp.el_google_apps.name(), qparamsForFile2);
		assertEquals(usersDoc1.getMeta().getTotalCount(), 0, "Collaborator is still exposed after deletion of email:"+externalUser1 +".");		
		usersDoc2 = getExposedUsers(elapp.el_google_apps.name(), qparamsForFile2);
		assertEquals(usersDoc2.getMeta().getTotalCount(), 0,"Owner is still exposed after deletion of email:"+testUser1 +".");
		
		//Verify the activity logs
		Reporter.log("Expected log:" + expectedLog, true);
		ForensicSearchResults availableLogs = searchLogsWithWaitTime("Activity_type", "Trash", myUniqueId, testUser1, 1, 2);
		assertNotNull(availableLogs,"Delete log not found after remediation");
		assertEquals(availableLogs.getHits().getHits().get(0).getSource().getMessage(),expectedLog,	"Test failed, as expected log is not found after remediation by admin.");
		
		
		
	}
	/**
	 * Send an email with content violation in mail and attachment to multiple users including internal and external users. Verify the exposure and remediate the same
	 * @param isInternal
	 * @param exposuretype
	 * @throws Exception
	 */
	@Test(groups={"P1"},dataProvider = "dataProviderRemediationWhenMultipleAttachmentExposureInternal")
	public void verifyExposureWhenMultipleAttachmentMailRemediatedOrDeletedInternalMultipleUsers(
			String isInternal, 
			String remediationType,
			String deletionType
			) throws Exception {
		
		Reporter.log("****************Test Case Description****************",true);
		Reporter.log("Send an email with content violation in mail and attachment to multiple users including internal and external users. Verify the exposure and remediate the same. ",true);
		Reporter.log("1)Send Mail with risk in body and mulitple attachments to multiple internal and external users",true);
		Reporter.log("2)Verify that the mail and attachment are exposed, sender and receiver appears in Users, Collabs list accordingly",true);
		Reporter.log("3)Delete mail "+deletionType+" .Verify that the action is successful and mail is deleted from sentItems folder of sender and inbox of internal users",true);
		Reporter.log("4)Verify that the mail and attachment are removed from exposure",true);
		Reporter.log("5)Verify that the deletion message also appears in the logs",true);
		Reporter.log("*****************************************************",true);
		
		
		//Prepare the remediation object
		Message emailObj = null;
		String myUniqueId = UUID.randomUUID().toString();
		List<String> myAttachments = new ArrayList<String>() ;
		String fileName1 = "PII.rtf";
		String fileName2 = "PCI_Test.txt";
		String searchFileName1 =myUniqueId+fileName1;
		String searchFileName2 =myUniqueId+fileName2;
		File sourceFile1 =new File(userDir+fileName1);
		File sourceFile2 =new File(userDir+fileName2);
		File destnFile1 =new File(userDir+searchFileName1);
		File destnFile2 =new File(userDir+searchFileName2);
		
		String myMailSubject =null;
		String mailBody = readFile(sourceFile2.toString());
		boolean success =false;
		String docTypeForRemediation = null;
		int countAfterExposure =0;
		int responseCode = 0;
		String expectedLog = null;
		List<String> to	= new ArrayList<>();
		List<String> cc	= new ArrayList<>();
		List<NameValuePair> qparamsForMail = new ArrayList<NameValuePair>(); 
		List<NameValuePair> qparamsForFile1 = new ArrayList<NameValuePair>(); 
		List<NameValuePair> qparamsForFile2 = new ArrayList<NameValuePair>(); 
		SecurletDocument mailExpoDoc = new SecurletDocument();
		SecurletDocument exposureDoc1 = new SecurletDocument();
		SecurletDocument exposureDoc2 = new SecurletDocument();
		SecurletDocument usersDoc1 = new SecurletDocument();
		SecurletDocument usersDoc2 = new SecurletDocument();
		SecurletDocument usersDoc3 = new SecurletDocument();
		String remedialAction ="ITEM_TRASH_MAIL";
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
				to.add(testUser2);
				cc.add(adminUser);
				cc.add(externalUser2);
				success= objMailTestUser1.sendMessageWithMultipleAttachment(to, cc,null,myMailSubject,mailBody,myAttachments);
			}
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
			mailExpoDoc = this.getExposedDocuments(elapp.el_google_apps.name(), qparamsForMail);
			countAfterExposure = mailExpoDoc.getMeta().getTotalCount();
			Reporter.log("Exposure Count ="+countAfterExposure, true);
			
			if (countAfterExposure >= 1) {break;}
		}
		assertEquals(countAfterExposure,1,"File:"+myMailSubject +" not exposed even after waiting for "+TimeUnit.MILLISECONDS.toMinutes(i-60000)+" minutes.");
		//Check the user and collaborator list
		usersDoc1 = getCollaborators(elapp.el_google_apps.name(), qparamsForMail);
		myResponse = MarshallingUtils.marshall(usersDoc1);
		assertTrue(myResponse.contains(externalUser1),"User email not found in collaborators list:"+externalUser1 +".");		
		assertTrue(myResponse.contains(externalUser2),"User email not found in collaborators list:"+externalUser2 +".");			
		usersDoc2 = getExposedUsers(elapp.el_google_apps.name(), qparamsForMail);
		assertTrue(usersDoc2.getObjects().get(0).getEmail().contains(testUser1),"User email not found in owner list:"+testUser1 +".");
		
		for (; i <= (MAX_EXPOSURE_WAIT_TIME_IN_MINUTES * 60 * 1000); i+=60000 ) {
			Reporter.log("Checking exposure after :"+TimeUnit.MILLISECONDS.toMinutes(i)+" minute(s)",true );
			Reporter.log("------------------------------------------------------------------------",true );
			Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
			
			
			//Get the exposure count
			exposureDoc1 = this.getExposedDocuments(elapp.el_google_apps.name(), qparamsForFile1);
			countAfterExposure = exposureDoc1.getMeta().getTotalCount();
			Reporter.log("Exposure Count ="+countAfterExposure, true);
			
			if (countAfterExposure >= 1) {break;}
		}
		assertEquals(countAfterExposure,1,"File:"+searchFileName1 +" not exposed even after waiting for "+TimeUnit.MILLISECONDS.toMinutes(i-60000)+" minutes.");
		
		//Check the user and collaborator list
		usersDoc1 = getCollaborators(elapp.el_google_apps.name(), qparamsForFile1);
		myResponse = MarshallingUtils.marshall(usersDoc1);
		assertTrue(myResponse.contains(externalUser1),"User email not found in collaborators list:"+externalUser1 +".");		
		assertTrue(myResponse.contains(externalUser2),"User email not found in collaborators list:"+externalUser2 +".");			
		usersDoc2 = getExposedUsers(elapp.el_google_apps.name(), qparamsForFile1);
		assertTrue(usersDoc2.getObjects().get(0).getEmail().contains(testUser1),"User email not found in owner list:"+testUser1 +".");
		
		Reporter.log("------------------------------------------------------------------------",true );
		
		for (; i <= (MAX_EXPOSURE_WAIT_TIME_IN_MINUTES * 60 * 1000); i+=60000 ) {
			Reporter.log("Checking exposure after :"+TimeUnit.MILLISECONDS.toMinutes(i)+" minute(s)",true );
			Reporter.log("------------------------------------------------------------------------",true );
			Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
			
			
			//Get the exposure count
			exposureDoc2 = this.getExposedDocuments(elapp.el_google_apps.name(), qparamsForFile2);
			countAfterExposure = exposureDoc2.getMeta().getTotalCount();
			Reporter.log("Exposure Count ="+countAfterExposure, true);
			
			if (countAfterExposure >= 1) {break;}
		}
		assertEquals(countAfterExposure,1,"File:"+searchFileName2 +" not exposed even after waiting for "+TimeUnit.MILLISECONDS.toMinutes(i-60000)+" minutes.");
		
		//Check the user and collaborator list
		usersDoc1 = getCollaborators(elapp.el_google_apps.name(), qparamsForFile2);
		myResponse = MarshallingUtils.marshall(usersDoc1);
		assertTrue(myResponse.contains(externalUser1),"User email not found in collaborators list:"+externalUser1 +".");		
		assertTrue(myResponse.contains(externalUser2),"User email not found in collaborators list:"+externalUser2 +".");
		usersDoc2 = getExposedUsers(elapp.el_google_apps.name(), qparamsForFile2);
		assertTrue(usersDoc2.getObjects().get(0).getEmail().contains(testUser1),"User email not found in owner list:"+testUser1 +".");
		
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
		MailRemediation objRemediation = getRemediationObject(adminUser, docTypeForRemediation, docId, remedialAction);
		
		
		if(deletionType.equals("by remediation")){
			//Now apply the remedial action through API server call
			responseCode = remediateExposureWithAPI(objRemediation);
			//Assert.assertEquals(responseCode, 202, "Remediation call failed.");
		}
		else if (deletionType.equals("from mailbox")){
			
			success= objMailTestUser1.trashMailFromLabel(myMailSubject, "SENT");
			Assert.assertTrue(success, "Mail deletion failed from SENT of user1.");
			
			
			success= objMailTestUser2.trashMailFromLabel(myMailSubject, "INBOX");
			Assert.assertTrue(success, "Mail deletion failed from INBOX of user2.");
			
			success= objMailAdmin.trashMailFromLabel(myMailSubject, "INBOX");
			Assert.assertTrue(success, "Mail deletion failed from INBOX of admin.");
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
				emailObj = objMailTestUser1.getMessageWithSubject(myMailSubject, "SENT");
			}
			
			//set remediationSuccess as true if mail or attachment deleted based on remediation type
//			if (remedialAction.equals(Remediation.ITEM_DELETE_MAIL_BY_ATTACHMENT.name()) || remedialAction.equals(Remediation.ITEM_TRASH_MAIL.name())) {
			if ( remedialAction.equals(Remediation.ITEM_TRASH_MAIL.name())) {
				
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
		
		assertTrue(remediationSuccess, "Remediation "+ remedialAction + " didn't work. Mail is not deleted from SENT folder of"+testUser1+" even after waiting for "+TimeUnit.MILLISECONDS.toMinutes(i-60000)+" minutes.");
		//Verify the remediation thro' o365 API
//		if (remedialAction.equals(Remediation.ITEM_DELETE_MAIL_BY_ATTACHMENT.name()) || remedialAction.equals(Remediation.ITEM_TRASH_MAIL.name())) {
		
//		if(deletionType.equals("by remediation")){
			expectedLog = "User trashed email with subject \""+myMailSubject+"\"";
//		}
//		else if (deletionType.equals("from mailbox")){
//			expectedLog = "User deleted with subject \""+myMailSubject+"\"";
//		}
		

		//verify that mails are deleted from inbox for internal users
		Message message=objMailTestUser2.getMessageWithSubject(myMailSubject, "INBOX");
		Assert.assertTrue(message==null, "Mail was not deleted from the inbox of user:"+testUser2);
		message=objMailAdmin.getMessageWithSubject(myMailSubject, "INBOX");
		Assert.assertTrue(message==null, "Mail was not deleted from the inbox of user:"+adminUser);
		
		
		
		Reporter.log("Remediation successful at GMAIL");
		
		//gather the forensic logs again 
		//logs = gatherForensicLogMessages(uniqueId, facility.Box.name());
		
		Reporter.log("------------------------------------------------------------------------",true );
		
		int countAfterRemediation = countAfterExposure ;
		i = 60000;
		for (; i <= (MAX_EXPOSURE_WAIT_TIME_AFTER_REMEDIATION * 60 * 1000); i+=60000 ) {
			Reporter.log("Wait for "+TimeUnit.MILLISECONDS.toMinutes(i)+" minutes, and check if the file:"+myMailSubject +" is removed from exposure.",true);
			Reporter.log("------------------------------------------------------------------------",true );
			Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
			mailExpoDoc = this.getExposedDocuments(elapp.el_google_apps.name(), qparamsForMail);
			countAfterRemediation = mailExpoDoc.getMeta().getTotalCount();
			
			if (countAfterRemediation == 0) {
				break;
			}
			
		}
		Reporter.log("###### exposure count after remediation::"+countAfterRemediation, true);
		assertEquals(countAfterRemediation, 0, "Exposure count has not reduced after remediation.");
		//Check the user and collaborator list
		usersDoc1 = getCollaborators(elapp.el_google_apps.name(), qparamsForMail);
		myResponse = MarshallingUtils.marshall(usersDoc1);
		assertFalse(myResponse.contains(externalUser1),"Collaborator is still exposed after deletion of email:"+externalUser1 +".");		
		assertFalse(myResponse.contains(externalUser2),"Collaborator is still exposed after deletion of email:"+externalUser2 +".");
		usersDoc2 = getExposedUsers(elapp.el_google_apps.name(), qparamsForMail);
		assertEquals(usersDoc2.getMeta().getTotalCount(), 0,"Owner is still exposed after deletion of email:"+testUser1 +".");
		
		for (; i <= (MAX_EXPOSURE_WAIT_TIME_AFTER_REMEDIATION * 60 * 1000); i+=60000 ) {
			Reporter.log("Wait for "+TimeUnit.MILLISECONDS.toMinutes(i)+" minutes, and check if the file:"+searchFileName1 +" is removed from exposure.",true);
			Reporter.log("------------------------------------------------------------------------",true );
			Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
			exposureDoc1 = this.getExposedDocuments(elapp.el_google_apps.name(), qparamsForFile1);
			countAfterRemediation = exposureDoc1.getMeta().getTotalCount();
			
			if (countAfterRemediation == 0) {
				break;
			}
			
		}
		Reporter.log("###### exposure count after remediation::"+countAfterRemediation, true);
		assertEquals(countAfterRemediation, 0, "Exposure count has not reduced after remediation.");
		//Check the user and collaborator list
		usersDoc1 = getCollaborators(elapp.el_google_apps.name(), qparamsForFile1);
		myResponse = MarshallingUtils.marshall(usersDoc1);
		assertFalse(myResponse.contains(externalUser1),"Collaborator is still exposed after deletion of email:"+externalUser1 +".");		
		assertFalse(myResponse.contains(externalUser2),"Collaborator is still exposed after deletion of email:"+externalUser2 +".");		
		usersDoc2 = getExposedUsers(elapp.el_google_apps.name(), qparamsForFile1);
		assertEquals(usersDoc2.getMeta().getTotalCount(), 0,"Owner is still exposed after deletion of email:"+testUser1 +".");
		
		for (; i <= (MAX_EXPOSURE_WAIT_TIME_AFTER_REMEDIATION * 60 * 1000); i+=60000 ) {
			Reporter.log("Wait for "+TimeUnit.MILLISECONDS.toMinutes(i)+" minutes, and check if the file:"+searchFileName2 +" is removed from exposure.",true);
			Reporter.log("------------------------------------------------------------------------",true );
			Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
			exposureDoc2 = this.getExposedDocuments(elapp.el_google_apps.name(), qparamsForFile2);
			countAfterRemediation = exposureDoc2.getMeta().getTotalCount();
			
			if (countAfterRemediation == 0) {
				break;
			}
			
		}
		Reporter.log("###### exposure count after remediation::"+countAfterRemediation, true);
		assertEquals(countAfterRemediation, 0, "Exposure count has not reduced after remediation.");
		//Check the user and collaborator list
		usersDoc1 = getCollaborators(elapp.el_google_apps.name(), qparamsForFile2);
		myResponse = MarshallingUtils.marshall(usersDoc1);
		assertFalse(myResponse.contains(externalUser1),"Collaborator is still exposed after deletion of email:"+externalUser1 +".");		
		assertFalse(myResponse.contains(externalUser2),"Collaborator is still exposed after deletion of email:"+externalUser2 +".");		
		usersDoc2 = getExposedUsers(elapp.el_google_apps.name(), qparamsForFile2);
		assertEquals(usersDoc2.getMeta().getTotalCount(), 0,"Owner is still exposed after deletion of email:"+testUser1 +".");
		
		
	
		//Verify the activity logs
		Reporter.log("Expected log:" + expectedLog, true);
		ForensicSearchResults availableLogs = searchLogsWithWaitTime("Activity_type", "Trash", myUniqueId, testUser1, 3, 5);
		assertNotNull(availableLogs,"Delete log not found after remediation");
		assertEquals(availableLogs.getHits().getHits().get(0).getSource().getMessage(),expectedLog,	"Test failed, as expected log is not found after remediation by admin.");
		
		
		
	}
	/**
	 * Send an email with content violation in mail and attachment to multiple users including internal and external users. Verify the exposure and delete the of internal users one by one and verify exposure.
	 * @param isInternal
	 * @param exposuretype
	 * @throws Exception
	 */
	@Test(groups={"P1"},retryAnalyzer=RetryAnalyzer.class)
	public void verifyExposureWhenMultipleAttachmentMailDeletedInternalMultipleUsers(
			
			) throws Exception {

		Reporter.log("****************Test Case Description****************",true);
		Reporter.log("Send an email with content violation in mail and attachment to multiple users including internal and external users. Verify the exposure and remediate the same. ",true);
		Reporter.log("1)Send Mail with risk in body and mulitple attachments to multiple internal and external users",true);
		Reporter.log("2)Verify that the mail and attachment are exposed, sender and receiver appears in Users, Collabs list accordingly",true);
		Reporter.log("3)Delete mail from inbox of each internal users and sentItems of sender, verify exposure and user list after each deletion",true);
		Reporter.log("4)Verify that the mail and attachment are removed from exposure",true);
		Reporter.log("*****************************************************",true);


		String isInternal="true"; 
		Message emailObj = null;
		String myUniqueId = UUID.randomUUID().toString();
		List<String> myAttachments = new ArrayList<String>() ;
		String fileName1 = "PII.rtf";
		String fileName2 = "PCI_Test.txt";
		String searchFileName1 =myUniqueId+fileName1;
		String searchFileName2 =myUniqueId+fileName2;
		File sourceFile1 =new File(userDir+fileName1);
		File sourceFile2 =new File(userDir+fileName2);
		File destnFile1 =new File(userDir+searchFileName1);
		File destnFile2 =new File(userDir+searchFileName2);
		
		String myMailSubject =null;
		String mailBody= readFile(sourceFile2.toString());
		boolean success =false;
		String docTypeForRemediation = null;
		int countAfterExposure =0;
		int responseCode = 0;
		String expectedLog = null;
		List<String> to	= new ArrayList<>();
		List<String> cc	= new ArrayList<>();
		List<NameValuePair> qparamsForMail = new ArrayList<NameValuePair>(); 
		List<NameValuePair> qparamsForFile1 = new ArrayList<NameValuePair>(); 
		List<NameValuePair> qparamsForFile2 = new ArrayList<NameValuePair>(); 
		SecurletDocument mailExpoDoc = new SecurletDocument();
		SecurletDocument exposureDoc1 = new SecurletDocument();
		SecurletDocument exposureDoc2 = new SecurletDocument();
		SecurletDocument usersDoc1 = new SecurletDocument();
		SecurletDocument usersDoc2 = new SecurletDocument();
		SecurletDocument usersDoc3 = new SecurletDocument();
		String remedialAction ="ITEM_TRASH_MAIL";
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
				to.add(testUser2);
				cc.add(adminUser);
				cc.add(externalUser2);
				success= objMailTestUser1.sendMessageWithMultipleAttachment(to, cc,null,myMailSubject,mailBody,myAttachments);
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
			mailExpoDoc = this.getExposedDocuments(elapp.el_google_apps.name(), qparamsForMail);
			countAfterExposure = mailExpoDoc.getMeta().getTotalCount();
			Reporter.log("Exposure Count ="+countAfterExposure, true);
			
			if (countAfterExposure >= 1) {break;}
		}
		assertEquals(countAfterExposure,1,"File:"+myMailSubject +" not exposed even after waiting for "+TimeUnit.MILLISECONDS.toMinutes(i-60000)+" minutes.");
		//Verifying the users in the exposure
		assertTrue(mailExpoDoc.getObjects().get(0).getExposures().getExternal().toString().contains(externalUser1),"User not found in exposure external user list: "+externalUser1);
		assertTrue(mailExpoDoc.getObjects().get(0).getExposures().getExternal().toString().contains(externalUser2),"User not found in exposure external user list: "+externalUser2);
		assertTrue(mailExpoDoc.getObjects().get(0).getExposures().getInternal().toString().contains(adminUser),"User not found in exposure internal user list: "+adminUser);
		assertTrue(mailExpoDoc.getObjects().get(0).getExposures().getInternal().toString().contains(testUser2),"User not found in exposure internal user list: "+testUser2);
		
		//Check the user and collaborator list
		usersDoc1 = getCollaborators(elapp.el_google_apps.name(), qparamsForMail);
		 myResponse = MarshallingUtils.marshall(usersDoc1);
		 assertTrue(myResponse.contains(externalUser1),"User email not found in collaborators list:"+externalUser1 +".");		
		 assertTrue(myResponse.contains(externalUser2),"User email not found in collaborators list:"+externalUser2 +".");			
		usersDoc2 = getExposedUsers(elapp.el_google_apps.name(), qparamsForMail);
		assertTrue(usersDoc2.getObjects().get(0).getEmail().contains(testUser1),"User email not found in owner list:"+testUser1 +".");
		
		
		/****Exposure & collaborator check file1*****/
		
		for (; i <= (MAX_EXPOSURE_WAIT_TIME_IN_MINUTES * 60 * 1000); i+=60000 ) {
			Reporter.log("Checking exposure after :"+TimeUnit.MILLISECONDS.toMinutes(i)+" minute(s)",true );
			Reporter.log("------------------------------------------------------------------------",true );
			Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
			
			
			//Get the exposure count
			exposureDoc1 = this.getExposedDocuments(elapp.el_google_apps.name(), qparamsForFile1);
			countAfterExposure = exposureDoc1.getMeta().getTotalCount();
			Reporter.log("Exposure Count ="+countAfterExposure, true);
			
			if (countAfterExposure >= 1) {break;}
		}
		assertEquals(countAfterExposure,1,"File:"+searchFileName1 +" not exposed even after waiting for "+TimeUnit.MILLISECONDS.toMinutes(i-60000)+" minutes.");
		//Verifying the users in the exposure
		assertTrue(exposureDoc1.getObjects().get(0).getExposures().getExternal().toString().contains(externalUser1),"User not found in exposure external user list: "+externalUser1);
		assertTrue(exposureDoc1.getObjects().get(0).getExposures().getExternal().toString().contains(externalUser2),"User not found in exposure external user list: "+externalUser2);
		assertTrue(exposureDoc1.getObjects().get(0).getExposures().getInternal().toString().contains(adminUser),"User not found in exposure internal user list: "+adminUser);
		assertTrue(exposureDoc1.getObjects().get(0).getExposures().getInternal().toString().contains(testUser2),"User not found in exposure internal user list: "+testUser2);
		
		//Check the user and collaborator list
		usersDoc1 = getCollaborators(elapp.el_google_apps.name(), qparamsForFile1);
		myResponse = MarshallingUtils.marshall(usersDoc1);
		assertTrue(myResponse.contains(externalUser1),"User email not found in collaborators list:"+externalUser1 +".");		
		assertTrue(myResponse.contains(externalUser2),"User email not found in collaborators list:"+externalUser2 +".");			
		usersDoc2 = getExposedUsers(elapp.el_google_apps.name(), qparamsForFile1);
		assertTrue(usersDoc2.getObjects().get(0).getEmail().contains(testUser1),"User email not found in owner list:"+testUser1 +".");
		
		/****Exposure & collaborator check file2*****/
		Reporter.log("------------------------------------------------------------------------",true );

		for (; i <= (MAX_EXPOSURE_WAIT_TIME_IN_MINUTES * 60 * 1000); i+=60000 ) {
			Reporter.log("Checking exposure after :"+TimeUnit.MILLISECONDS.toMinutes(i)+" minute(s)",true );
			Reporter.log("------------------------------------------------------------------------",true );
			Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);


			//Get the exposure count
			exposureDoc2 = this.getExposedDocuments(elapp.el_google_apps.name(), qparamsForFile2);
			countAfterExposure = exposureDoc2.getMeta().getTotalCount();
			Reporter.log("Exposure Count ="+countAfterExposure, true);

			if (countAfterExposure >= 1) {break;}
		}
		assertEquals(countAfterExposure,1,"File:"+searchFileName2 +" not exposed even after waiting for "+TimeUnit.MILLISECONDS.toMinutes(i-60000)+" minutes.");
		//Verifying the users in the exposure
		assertTrue(exposureDoc2.getObjects().get(0).getExposures().getExternal().toString().contains(externalUser1),"User not found in exposure external user list: "+externalUser1);
		assertTrue(exposureDoc2.getObjects().get(0).getExposures().getExternal().toString().contains(externalUser2),"User not found in exposure external user list: "+externalUser2);
		assertTrue(exposureDoc2.getObjects().get(0).getExposures().getInternal().toString().contains(adminUser),"User not found in exposure internal user list: "+adminUser);
		assertTrue(exposureDoc2.getObjects().get(0).getExposures().getInternal().toString().contains(testUser2),"User not found in exposure internal user list: "+testUser2);

		//Check the user and collaborator list
		usersDoc1 = getCollaborators(elapp.el_google_apps.name(), qparamsForFile2);
		myResponse = MarshallingUtils.marshall(usersDoc1);
		assertTrue(myResponse.contains(externalUser1),"User email not found in collaborators list:"+externalUser1 +".");		
		assertTrue(myResponse.contains(externalUser2),"User email not found in collaborators list:"+externalUser2 +".");
		usersDoc2 = getExposedUsers(elapp.el_google_apps.name(), qparamsForFile2);
		assertTrue(usersDoc2.getObjects().get(0).getEmail().contains(testUser1),"User email not found in owner list:"+testUser1 +".");

		Reporter.log("----------------Delete the mail from inbox of "+testUser2+" --------------------",true );
		Message message=objMailTestUser2.getMessageWithSubject(myMailSubject, "INBOX");
		String threadId=message.getThreadId();
		success = objMailTestUser2.trashMessage(threadId);
		Assert.assertTrue(success, "Mail deletion failed.");
		
		//Wait for remedial action
		Reporter.log("---------------------Waiting for the exposure change----------------------------",true );
		
		Reporter.log("Waiting for "+MAX_EXPOSURE_WAIT_TIME_AFTER_REMEDIATION+" minutes to check user in exposure after deletion of email from inbox",true);
		Thread.sleep(TimeUnit.MINUTES.toMillis(5));
		mailExpoDoc = this.getExposedDocuments(elapp.el_google_apps.name(), qparamsForMail);
		int countAfterRemediation = mailExpoDoc.getMeta().getTotalCount();
		
		/****Exposure & collaborator check mail after internal user1 mail deletion from inbox *****/
		Reporter.log("###### exposure count after mail deletion from inbox of "+testUser2+" is:"+countAfterRemediation, true);
		assertEquals(countAfterRemediation, 1, "Exposure count shouldn't be reduced after mail deletion from inbox of "+testUser2+".");
		//Verifying the users in the exposure
		assertTrue(mailExpoDoc.getObjects().get(0).getExposures().getExternal().toString().contains(externalUser1),"User not found in exposure external user list: "+externalUser1);
		assertTrue(mailExpoDoc.getObjects().get(0).getExposures().getExternal().toString().contains(externalUser2),"User not found in exposure external user list: "+externalUser2);
		assertTrue(mailExpoDoc.getObjects().get(0).getExposures().getInternal().toString().contains(adminUser),"User not found in exposure internal user list: "+adminUser);
		assertFalse(mailExpoDoc.getObjects().get(0).getExposures().getInternal().toString().contains(testUser2),"User still found in exposure internal user list: "+testUser2);
		//Check the user and collaborator list
		usersDoc1 = getCollaborators(elapp.el_google_apps.name(), qparamsForMail);
		myResponse = MarshallingUtils.marshall(usersDoc1);
		assertTrue(myResponse.contains(externalUser1),"Collaborator "+externalUser1 +" is unexposed after deletion of email from inbox of:"+testUser2 +".");		
		assertTrue(myResponse.contains(externalUser2),"Collaborator "+externalUser1 +" is unexposed after deletion of email from inbox of:"+testUser2 +".");
		usersDoc2 = getExposedUsers(elapp.el_google_apps.name(), qparamsForMail);
		assertTrue(usersDoc2.getObjects().get(0).getEmail().contains(testUser1),"User email not found in owner list:"+testUser1 +".");
		
		/****Exposure & collaborator check file1 after internal user1 mail deletion from inbox *****/
		exposureDoc1 = this.getExposedDocuments(elapp.el_google_apps.name(), qparamsForFile1);
		countAfterRemediation = exposureDoc1.getMeta().getTotalCount();
		Reporter.log("###### exposure count after remediation::"+countAfterRemediation, true);
		assertEquals(countAfterRemediation, 1, "Exposure count shouldn't be reduced after mail deletion from inbox of "+testUser2+".");
		//Verifying the users in the exposure
		assertTrue(exposureDoc1.getObjects().get(0).getExposures().getExternal().toString().contains(externalUser1),"User not found in exposure external user list: "+externalUser1);
		assertTrue(exposureDoc1.getObjects().get(0).getExposures().getExternal().toString().contains(externalUser2),"User not found in exposure external user list: "+externalUser2);
		assertTrue(exposureDoc1.getObjects().get(0).getExposures().getInternal().toString().contains(adminUser),"User not found in exposure internal user list: "+adminUser);
		assertFalse(exposureDoc1.getObjects().get(0).getExposures().getInternal().toString().contains(testUser2),"User still found in exposure internal user list: "+testUser2);
		//Check the user and collaborator list
		usersDoc1 = getCollaborators(elapp.el_google_apps.name(), qparamsForFile1);
		myResponse = MarshallingUtils.marshall(usersDoc1);
		assertTrue(myResponse.contains(externalUser1),"Collaborator "+externalUser1 +" is unexposed after deletion of email from inbox of:"+testUser2 +".");		
		assertTrue(myResponse.contains(externalUser2),"Collaborator "+externalUser1 +" is unexposed after deletion of email from inbox of:"+testUser2 +".");		
		usersDoc2 = getExposedUsers(elapp.el_google_apps.name(), qparamsForFile1);
		assertTrue(usersDoc2.getObjects().get(0).getEmail().contains(testUser1),"User email not found in owner list:"+testUser1 +".");
		
		/****Exposure & collaborator check file2 after internal user1 mail deletion from inbox *****/
		exposureDoc2 = this.getExposedDocuments(elapp.el_google_apps.name(), qparamsForFile2);
		countAfterRemediation = exposureDoc2.getMeta().getTotalCount();
		Reporter.log("###### exposure count after remediation::"+countAfterRemediation, true);
		assertEquals(countAfterRemediation, 1, "Exposure count shouldn't be reduced after mail deletion from inbox of "+testUser2+".");
		assertTrue(exposureDoc2.getObjects().get(0).getExposures().getExternal().toString().contains(externalUser1),"User not found in exposure external user list: "+externalUser1);
		assertTrue(exposureDoc2.getObjects().get(0).getExposures().getExternal().toString().contains(externalUser2),"User not found in exposure external user list: "+externalUser2);
		assertTrue(exposureDoc2.getObjects().get(0).getExposures().getInternal().toString().contains(adminUser),"User not found in exposure internal user list: "+adminUser);
		assertFalse(exposureDoc2.getObjects().get(0).getExposures().getInternal().toString().contains(testUser2),"User still found in exposure internal user list: "+testUser2);
		//Check the user and collaborator list
		usersDoc1 = getCollaborators(elapp.el_google_apps.name(), qparamsForFile2);
		myResponse = MarshallingUtils.marshall(usersDoc1);
		assertTrue(myResponse.contains(externalUser1),"Collaborator "+externalUser1 +" is unexposed after deletion of email from inbox of:"+testUser2 +".");		
		assertTrue(myResponse.contains(externalUser2),"Collaborator "+externalUser1 +" is unexposed after deletion of email from inbox of:"+testUser2 +".");	
		usersDoc2 = getExposedUsers(elapp.el_google_apps.name(), qparamsForFile2);
		assertTrue(usersDoc2.getObjects().get(0).getEmail().contains(testUser1),"User email not found in owner list:"+testUser1 +".");
		
		Reporter.log("----------------Delete the mail from inbox of "+adminUser+" --------------------",true );
		success= objMailAdmin.trashMailFromLabel(myMailSubject, "INBOX");
		Assert.assertTrue(success, "Mail deletion failed from INBOX of admin.");

		//Wait for remedial action
		Reporter.log("---------------------Waiting for the exposure change----------------------------",true );

		Reporter.log("Waiting for "+MAX_EXPOSURE_WAIT_TIME_AFTER_REMEDIATION+" minutes to check user in exposure after deletion of email from inbox",true);
		Thread.sleep(TimeUnit.MINUTES.toMillis(5));
		mailExpoDoc = this.getExposedDocuments(elapp.el_google_apps.name(), qparamsForMail);
		 countAfterRemediation = mailExpoDoc.getMeta().getTotalCount();
			
		/****Exposure & collaborator check mail after internal user2 mail deletion from inbox *****/
		Reporter.log("###### exposure count after mail deletion from inbox of "+adminUser+" is:"+countAfterRemediation, true);
		assertEquals(countAfterRemediation, 1, "Exposure count shouldn't be reduced after mail deletion from inbox of "+adminUser+".");
		//Verifying the users in the exposure
		assertTrue(mailExpoDoc.getObjects().get(0).getExposures().getExternal().toString().contains(externalUser1),"User not found in exposure external user list: "+externalUser1);
		assertTrue(mailExpoDoc.getObjects().get(0).getExposures().getExternal().toString().contains(externalUser2),"User not found in exposure external user list: "+externalUser2);
		assertFalse(mailExpoDoc.getObjects().get(0).getExposures().getInternal().toString().contains(adminUser),"User still found in exposure internal user list: "+adminUser);
		assertFalse(mailExpoDoc.getObjects().get(0).getExposures().getInternal().toString().contains(testUser2),"User still found in exposure internal user list: "+testUser2);
		//Check the user and collaborator list
		usersDoc1 = getCollaborators(elapp.el_google_apps.name(), qparamsForMail);
		myResponse = MarshallingUtils.marshall(usersDoc1);
		assertTrue(myResponse.contains(externalUser1),"Collaborator "+externalUser1 +" is unexposed after deletion of email from inbox of:"+testUser2 +".");		
		assertTrue(myResponse.contains(externalUser2),"Collaborator "+externalUser1 +" is unexposed after deletion of email from inbox of:"+testUser2 +".");
		usersDoc2 = getExposedUsers(elapp.el_google_apps.name(), qparamsForMail);
		assertTrue(usersDoc2.getObjects().get(0).getEmail().contains(testUser1),"User email not found in owner list:"+testUser1 +".");
		
		/****Exposure & collaborator check file1 after internal user2 mail deletion from inbox *****/
		exposureDoc1 = this.getExposedDocuments(elapp.el_google_apps.name(), qparamsForFile1);
		countAfterRemediation = exposureDoc1.getMeta().getTotalCount();
		Reporter.log("###### exposure count after remediation::"+countAfterRemediation, true);
		assertEquals(countAfterRemediation, 1, "Exposure count shouldn't be reduced after mail deletion from inbox of "+adminUser+".");
		//Verifying the users in the exposure
		assertTrue(exposureDoc1.getObjects().get(0).getExposures().getExternal().toString().contains(externalUser1),"User not found in exposure external user list: "+externalUser1);
		assertTrue(exposureDoc1.getObjects().get(0).getExposures().getExternal().toString().contains(externalUser2),"User not found in exposure external user list: "+externalUser2);
		assertFalse(exposureDoc1.getObjects().get(0).getExposures().getInternal().toString().contains(adminUser),"User still found in exposure internal user list: "+adminUser);
		assertFalse(exposureDoc1.getObjects().get(0).getExposures().getInternal().toString().contains(testUser2),"User still found in exposure internal user list: "+testUser2);
		//Check the user and collaborator list
		usersDoc1 = getCollaborators(elapp.el_google_apps.name(), qparamsForFile1);
		myResponse = MarshallingUtils.marshall(usersDoc1);
		assertTrue(myResponse.contains(externalUser1),"Collaborator "+externalUser1 +" is unexposed after deletion of email from inbox of:"+testUser2 +".");		
		assertTrue(myResponse.contains(externalUser2),"Collaborator "+externalUser1 +" is unexposed after deletion of email from inbox of:"+testUser2 +".");		
		usersDoc2 = getExposedUsers(elapp.el_google_apps.name(), qparamsForFile1);
		assertTrue(usersDoc2.getObjects().get(0).getEmail().contains(testUser1),"User email not found in owner list:"+testUser1 +".");
		
		/****Exposure & collaborator check file2 after internal user2 mail deletion from inbox *****/
		exposureDoc2 = this.getExposedDocuments(elapp.el_google_apps.name(), qparamsForFile2);
		countAfterRemediation = exposureDoc2.getMeta().getTotalCount();
		Reporter.log("###### exposure count after remediation::"+countAfterRemediation, true);
		assertEquals(countAfterRemediation, 1, "Exposure count shouldn't be reduced after mail deletion from inbox of "+adminUser+".");
		assertTrue(exposureDoc2.getObjects().get(0).getExposures().getExternal().toString().contains(externalUser1),"User not found in exposure external user list: "+externalUser1);
		assertTrue(exposureDoc2.getObjects().get(0).getExposures().getExternal().toString().contains(externalUser2),"User not found in exposure external user list: "+externalUser2);
		assertFalse(exposureDoc2.getObjects().get(0).getExposures().getInternal().toString().contains(adminUser),"User still found in exposure internal user list: "+adminUser);
		assertFalse(exposureDoc2.getObjects().get(0).getExposures().getInternal().toString().contains(testUser2),"User still found in exposure internal user list: "+testUser2);
		//Check the user and collaborator list
		usersDoc1 = getCollaborators(elapp.el_google_apps.name(), qparamsForFile2);
		myResponse = MarshallingUtils.marshall(usersDoc1);
		assertTrue(myResponse.contains(externalUser1),"Collaborator "+externalUser1 +" is unexposed after deletion of email from inbox of:"+testUser2 +".");		
		assertTrue(myResponse.contains(externalUser2),"Collaborator "+externalUser1 +" is unexposed after deletion of email from inbox of:"+testUser2 +".");	
		usersDoc2 = getExposedUsers(elapp.el_google_apps.name(), qparamsForFile2);
		assertTrue(usersDoc2.getObjects().get(0).getEmail().contains(testUser1),"User email not found in owner list:"+testUser1 +".");
		


		Reporter.log("----------------Delete the mail from sentItems of "+testUser1+" --------------------",true );
		message=objMailTestUser1.getMessageWithSubject(myMailSubject, "SENT");
		threadId=message.getThreadId();
		success = objMailTestUser1.trashMessage(threadId);
		Assert.assertTrue(success, "Mail deletion failed.");
		
		Reporter.log("------------------------------------------------------------------------",true );
		
		i = 60000;
		for (; i <= (MAX_EXPOSURE_WAIT_TIME_AFTER_REMEDIATION * 60 * 1000); i+=60000 ) {
			Reporter.log("Wait for "+TimeUnit.MILLISECONDS.toMinutes(i)+" minutes, and check if the file:"+myMailSubject +" is removed from exposure.",true);
			Reporter.log("------------------------------------------------------------------------",true );
			Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
			mailExpoDoc = this.getExposedDocuments(elapp.el_google_apps.name(), qparamsForMail);
			countAfterRemediation = mailExpoDoc.getMeta().getTotalCount();
			
			if (countAfterRemediation == 0) {
				break;
			}
			
		}
		Reporter.log("###### exposure count after remediation::"+countAfterRemediation, true);
		assertEquals(countAfterRemediation, 0, "Exposure count has not reduced after remediation.");
		//Check the user and collaborator list
		usersDoc1 = getCollaborators(elapp.el_google_apps.name(), qparamsForMail);
		myResponse = MarshallingUtils.marshall(usersDoc1);
		assertFalse(myResponse.contains(externalUser1),"Collaborator is still exposed after deletion of email:"+externalUser1 +".");		
		assertFalse(myResponse.contains(externalUser2),"Collaborator is still exposed after deletion of email:"+externalUser2 +".");
		usersDoc2 = getExposedUsers(elapp.el_google_apps.name(), qparamsForMail);
		assertEquals(usersDoc2.getMeta().getTotalCount(), 0,"Owner is still exposed after deletion of email:"+testUser1 +".");
		
		for (; i <= (MAX_EXPOSURE_WAIT_TIME_AFTER_REMEDIATION * 60 * 1000); i+=60000 ) {
			Reporter.log("Wait for "+TimeUnit.MILLISECONDS.toMinutes(i)+" minutes, and check if the file:"+searchFileName1 +" is removed from exposure.",true);
			Reporter.log("------------------------------------------------------------------------",true );
			Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
			exposureDoc1 = this.getExposedDocuments(elapp.el_google_apps.name(), qparamsForFile1);
			countAfterRemediation = exposureDoc1.getMeta().getTotalCount();
			
			if (countAfterRemediation == 0) {
				break;
			}
			
		}
		Reporter.log("###### exposure count after remediation::"+countAfterRemediation, true);
		assertEquals(countAfterRemediation, 0, "Exposure count has not reduced after remediation.");
		//Check the user and collaborator list
		usersDoc1 = getCollaborators(elapp.el_google_apps.name(), qparamsForFile1);
		myResponse = MarshallingUtils.marshall(usersDoc1);
		assertFalse(myResponse.contains(externalUser1),"Collaborator is still exposed after deletion of email:"+externalUser1 +".");		
		assertFalse(myResponse.contains(externalUser2),"Collaborator is still exposed after deletion of email:"+externalUser2 +".");		
		usersDoc2 = getExposedUsers(elapp.el_google_apps.name(), qparamsForFile1);
		assertEquals(usersDoc2.getMeta().getTotalCount(), 0,"Owner is still exposed after deletion of email:"+testUser1 +".");
		
		for (; i <= (MAX_EXPOSURE_WAIT_TIME_AFTER_REMEDIATION * 60 * 1000); i+=60000 ) {
			Reporter.log("Wait for "+TimeUnit.MILLISECONDS.toMinutes(i)+" minutes, and check if the file:"+searchFileName2 +" is removed from exposure.",true);
			Reporter.log("------------------------------------------------------------------------",true );
			Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
			exposureDoc2 = this.getExposedDocuments(elapp.el_google_apps.name(), qparamsForFile2);
			countAfterRemediation = exposureDoc2.getMeta().getTotalCount();
			
			if (countAfterRemediation == 0) {
				break;
			}
			
		}
		Reporter.log("###### exposure count after remediation::"+countAfterRemediation, true);
		assertEquals(countAfterRemediation, 0, "Exposure count has not reduced after remediation.");
		//Check the user and collaborator list
		usersDoc1 = getCollaborators(elapp.el_google_apps.name(), qparamsForFile2);
		myResponse = MarshallingUtils.marshall(usersDoc1);
		assertFalse(myResponse.contains(externalUser1),"Collaborator is still exposed after deletion of email:"+externalUser1 +".");		
		assertFalse(myResponse.contains(externalUser2),"Collaborator is still exposed after deletion of email:"+externalUser2 +".");		
		usersDoc2 = getExposedUsers(elapp.el_google_apps.name(), qparamsForFile2);
		assertEquals(usersDoc2.getMeta().getTotalCount(), 0,"Owner is still exposed after deletion of email:"+testUser1 +".");
		

		

	}
	
	/**
	 * External user sends content violation in mailbody and multiple attachments to multiple internal user
	 * Verify the exposure and then delete the email from inbox of each internal users one by one  and check file exposure changed or not.
	 * @param isInternal
	 * @param exposuretype
	 * @throws Exception
	 */
	@Test(groups={"P1"},retryAnalyzer=RetryAnalyzer.class)
	public void verifyExposureWhenMultipleAttachmentMailDeletedExternalMultiple(
			) throws Exception {
		
		Reporter.log("****************Test Case Description****************",true);
		Reporter.log("External user sends content violation in mailbody and multiple attachments to multiple internal user. Verify the exposure and delete email from each user inbox and verify the exposure. ",true);
		Reporter.log("1)Two internal users receive a Mail with risk in body and mulitple attachments from external user",true);
		Reporter.log("2)Verify that the mail and attachment are exposed, sender and receiver appears in Users, Collabs list accordingly",true);
		Reporter.log("3)Delete mail from inbox of user1.",true);
		Reporter.log("4)Verify that the mail and attachment remain exposed but user1 is removed from exposure",true);
		Reporter.log("5)Delete mail from inbox of user2.",true);
		Reporter.log("6)Verify that the mail and attachment are removed from exposure",true);
		Reporter.log("*****************************************************",true);
		
		
		String isInternal = "false";
		Message emailObj = null;
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
		String mailBody = readFile(sourceFile2.toString());
		boolean success =false;
		String docTypeForRemediation = null;
		int countAfterExposure =0;
		int responseCode = 0;
		String expectedLog = null;
		List<String> to	= new ArrayList<>();
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
		String remedialAction ="ITEM_TRASH_MAIL";
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
		
		recipientsTo.add(testUser1);
		recipientsCc.add(testUser2);
		
		synchronized(this){ 
			success = objMailExternalUser2.sendMailWithCCAndBCC(recipientsTo,recipientsCc,null, myMailSubject,mailBody ,myAttachments , false);
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
			mailExpoDoc = this.getExposedDocuments(elapp.el_google_apps.name(), qparamsForMail);
			countAfterExposure = mailExpoDoc.getMeta().getTotalCount();
			Reporter.log("Exposure Count ="+countAfterExposure, true);
			
			if (countAfterExposure >= 1) {break;}
		}
		assertEquals(countAfterExposure,1,"File:"+myMailSubject +" not exposed even after waiting for "+TimeUnit.MILLISECONDS.toMinutes(i-60000)+" minutes.");
		assertTrue(mailExpoDoc.getObjects().get(0).getExposures().getInternal().toString().contains(testUser1),"User not found in exposure internal user list: "+testUser1);
		assertTrue(mailExpoDoc.getObjects().get(0).getExposures().getInternal().toString().contains(testUser2),"User not found in exposure internal user list: "+testUser2);
		
		//Check the user and collaborator list
		usersDoc1 = getCollaborators(elapp.el_google_apps.name(), qparamsForMail);
		myResponse = MarshallingUtils.marshall(usersDoc1);
		assertTrue(myResponse.contains(testUser1),"User email not found in collaborators list:"+testUser1 +".");		
		assertTrue(myResponse.contains(testUser2),"User email not found in collaborators list:"+testUser2 +".");		
		usersDoc1 = getExposedUsers(elapp.el_google_apps.name(), qparamsForMail);
		myResponse = MarshallingUtils.marshall(usersDoc1);
		assertTrue(myResponse.contains(externalUser2),"User email not found in owner list:"+externalUser2 +".");
		
		for (; i <= (MAX_EXPOSURE_WAIT_TIME_IN_MINUTES * 60 * 1000); i+=60000 ) {
			Reporter.log("Checking exposure after :"+TimeUnit.MILLISECONDS.toMinutes(i)+" minute(s)",true );
			Reporter.log("------------------------------------------------------------------------",true );
			Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
			
			
			//Get the exposure count
			exposureDoc1 = this.getExposedDocuments(elapp.el_google_apps.name(), qparamsForFile1);
			countAfterExposure = exposureDoc1.getMeta().getTotalCount();
			Reporter.log("Exposure Count ="+countAfterExposure, true);
			
			if (countAfterExposure >= 1) {break;}
		}
		assertEquals(countAfterExposure,1,"File:"+searchFileName1 +" not exposed even after waiting for "+TimeUnit.MILLISECONDS.toMinutes(i-60000)+" minutes.");
		assertTrue(exposureDoc1.getObjects().get(0).getExposures().getInternal().toString().contains(testUser1),"User not found in exposure internal user list: "+testUser1);
		assertTrue(exposureDoc1.getObjects().get(0).getExposures().getInternal().toString().contains(testUser2),"User not found in exposure internal user list: "+testUser2);
		//Check the user and collaborator list
		usersDoc1 = getCollaborators(elapp.el_google_apps.name(), qparamsForFile1);
		myResponse = MarshallingUtils.marshall(usersDoc1);
		assertTrue(myResponse.contains(testUser1),"User email not found in collaborators list:"+testUser1 +".");		
		assertTrue(myResponse.contains(testUser2),"User email not found in collaborators list:"+testUser2 +".");		
		usersDoc1 = getExposedUsers(elapp.el_google_apps.name(), qparamsForFile1);
		myResponse = MarshallingUtils.marshall(usersDoc1);
		assertTrue(myResponse.contains(externalUser2),"User email not found in owner list:"+externalUser2 +".");
		
		Reporter.log("------------------------------------------------------------------------",true );
		
		for (; i <= (MAX_EXPOSURE_WAIT_TIME_IN_MINUTES * 60 * 1000); i+=60000 ) {
			Reporter.log("Checking exposure after :"+TimeUnit.MILLISECONDS.toMinutes(i)+" minute(s)",true );
			Reporter.log("------------------------------------------------------------------------",true );
			Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
			
			
			//Get the exposure count
			exposureDoc2 = this.getExposedDocuments(elapp.el_google_apps.name(), qparamsForFile2);
			countAfterExposure = exposureDoc2.getMeta().getTotalCount();
			Reporter.log("Exposure Count ="+countAfterExposure, true);
			
			if (countAfterExposure >= 1) {break;}
		}
		assertEquals(countAfterExposure,1,"File:"+searchFileName2 +" not exposed even after waiting for "+TimeUnit.MILLISECONDS.toMinutes(i-60000)+" minutes.");
		assertTrue(exposureDoc1.getObjects().get(0).getExposures().getInternal().toString().contains(testUser1),"User not found in exposure internal user list: "+testUser1);
		assertTrue(exposureDoc1.getObjects().get(0).getExposures().getInternal().toString().contains(testUser2),"User not found in exposure internal user list: "+testUser2);
		//Check the user and collaborator list
		usersDoc1 = getCollaborators(elapp.el_google_apps.name(), qparamsForFile2);
		myResponse = MarshallingUtils.marshall(usersDoc1);
		assertTrue(myResponse.contains(testUser1),"User email not found in collaborators list:"+testUser1 +".");		
		assertTrue(myResponse.contains(testUser2),"User email not found in collaborators list:"+testUser2 +".");		
		usersDoc1 = getExposedUsers(elapp.el_google_apps.name(), qparamsForFile2);
		myResponse = MarshallingUtils.marshall(usersDoc1);
		assertTrue(myResponse.contains(externalUser2),"User email not found in owner list:"+externalUser2 +".");
		
		
		Reporter.log("----------------Delete the mail from inbox of "+testUser1+" --------------------",true );
		Message message=objMailTestUser1.getLatestMail(myMailSubject);
		String threadId=message.getThreadId();
		success = objMailTestUser1.trashMessage(threadId);
		Assert.assertTrue(success, "Mail deletion failed.");
		
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
				emailObj = objMailTestUser1.getMessageWithSubject(myMailSubject, "INBOX");
			}
			
			//set remediationSuccess as true if mail or attachment deleted based on remediation type
//			if (remedialAction.equals(Remediation.ITEM_DELETE_MAIL_BY_ATTACHMENT.name()) || remedialAction.equals(Remediation.ITEM_TRASH_MAIL.name())) {
			if ( remedialAction.equals(Remediation.ITEM_TRASH_MAIL.name())) {
				
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
		for (; i <= (MAX_EXPOSURE_WAIT_TIME_AFTER_REMEDIATION * 60 * 1000); i+=60000 ) {
			Reporter.log("Wait for "+TimeUnit.MILLISECONDS.toMinutes(i)+" minutes, and check if the file:"+myMailSubject +" is removed from exposure.",true);
			Reporter.log("------------------------------------------------------------------------",true );
			Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
			mailExpoDoc = this.getExposedDocuments(elapp.el_google_apps.name(), qparamsForMail);
			countAfterRemediation = mailExpoDoc.getObjects().get(0).getExposures().getInternal().size();
			
			if (countAfterRemediation == 1) {
				break;
			}
			
		}
		
		assertEquals(countAfterExposure,1,"User:"+testUser1+" is not removed from the from file exposure for :"+myMailSubject +" after deleting mail from inbox and waiting for "+TimeUnit.MILLISECONDS.toMinutes(i-60000)+" minutes.");
		assertFalse(mailExpoDoc.getObjects().get(0).getExposures().getInternal().toString().contains(testUser1),"User is found in exposure internal user list: "+testUser1);
		assertTrue(mailExpoDoc.getObjects().get(0).getExposures().getInternal().toString().contains(testUser2),"User not found in exposure internal user list: "+testUser2);
		
		//Check the user and collaborator list
		usersDoc1 = getCollaborators(elapp.el_google_apps.name(), qparamsForMail);
		myResponse = MarshallingUtils.marshall(usersDoc1);
		assertFalse(myResponse.contains(testUser1),"User email is found in collaborators list:"+testUser1 +".");		
		assertTrue(myResponse.contains(testUser2),"User email not found in collaborators list:"+testUser2 +".");		
		usersDoc1 = getExposedUsers(elapp.el_google_apps.name(), qparamsForMail);
		myResponse = MarshallingUtils.marshall(usersDoc1);
		assertTrue(myResponse.contains(externalUser2),"User email not found in owner list:"+externalUser2 +".");
		
		for (; i <= (MAX_EXPOSURE_WAIT_TIME_IN_MINUTES * 60 * 1000); i+=60000 ) {
			Reporter.log("Checking exposure after :"+TimeUnit.MILLISECONDS.toMinutes(i)+" minute(s)",true );
			Reporter.log("------------------------------------------------------------------------",true );
			Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
			
			
			//Get the exposure count
			exposureDoc1 = this.getExposedDocuments(elapp.el_google_apps.name(), qparamsForFile1);
			countAfterExposure = exposureDoc1.getObjects().get(0).getExposures().getInternal().size();
			Reporter.log("Exposure Count ="+countAfterExposure, true);
			
			if (countAfterExposure >= 1) {break;}
		}
		assertEquals(countAfterExposure,1,"User:"+testUser1+" is not removed from the from file exposure for :"+searchFileName1 +" after deleting mail from inbox and waiting for "+TimeUnit.MILLISECONDS.toMinutes(i-60000)+" minutes.");
		assertFalse(exposureDoc1.getObjects().get(0).getExposures().getInternal().toString().contains(testUser1),"User is found in exposure internal user list: "+testUser1);
		assertTrue(exposureDoc1.getObjects().get(0).getExposures().getInternal().toString().contains(testUser2),"User not found in exposure internal user list: "+testUser2);
		
		//Check the user and collaborator list
		usersDoc1 = getCollaborators(elapp.el_google_apps.name(), qparamsForFile1);
		myResponse = MarshallingUtils.marshall(usersDoc1);
		assertFalse(myResponse.contains(testUser1),"User email is found in collaborators list:"+testUser1 +".");		
		assertTrue(myResponse.contains(testUser2),"User email not found in collaborators list:"+testUser2 +".");		
		usersDoc1 = getExposedUsers(elapp.el_google_apps.name(), qparamsForFile1);
		myResponse = MarshallingUtils.marshall(usersDoc1);
		assertTrue(myResponse.contains(externalUser2),"User email not found in owner list:"+externalUser2 +".");
		
		Reporter.log("------------------------------------------------------------------------",true );
		
		for (; i <= (MAX_EXPOSURE_WAIT_TIME_IN_MINUTES * 60 * 1000); i+=60000 ) {
			Reporter.log("Checking exposure after :"+TimeUnit.MILLISECONDS.toMinutes(i)+" minute(s)",true );
			Reporter.log("------------------------------------------------------------------------",true );
			Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
			
			
			//Get the exposure count
			exposureDoc2 = this.getExposedDocuments(elapp.el_google_apps.name(), qparamsForFile2);
			countAfterExposure = exposureDoc2.getObjects().get(0).getExposures().getInternal().size();
			Reporter.log("Exposure Count ="+countAfterExposure, true);
			
			if (countAfterExposure >= 1) {break;}
		}
		assertEquals(countAfterExposure,1,"User:"+testUser1+" is not removed from the from file exposure for :"+searchFileName2 +" after deleting mail from inbox and waiting for "+TimeUnit.MILLISECONDS.toMinutes(i-60000)+" minutes.");
		assertFalse(exposureDoc1.getObjects().get(0).getExposures().getInternal().toString().contains(testUser1),"User is found in exposure internal user list: "+testUser1);
		assertTrue(exposureDoc1.getObjects().get(0).getExposures().getInternal().toString().contains(testUser2),"User not found in exposure internal user list: "+testUser2);
		
		//Check the user and collaborator list
		usersDoc1 = getCollaborators(elapp.el_google_apps.name(), qparamsForFile2);
		myResponse = MarshallingUtils.marshall(usersDoc1);
		assertFalse(myResponse.contains(testUser1),"User email is found in collaborators list:"+testUser1 +".");		
		assertTrue(myResponse.contains(testUser2),"User email not found in collaborators list:"+testUser2 +".");		
		usersDoc1 = getExposedUsers(elapp.el_google_apps.name(), qparamsForFile2);
		myResponse = MarshallingUtils.marshall(usersDoc1);
		assertTrue(myResponse.contains(externalUser2),"User email not found in owner list:"+externalUser2 +".");
		
		
		Reporter.log("----------------Delete the mail from inbox of "+testUser2+" --------------------",true );
		message=objMailTestUser2.getLatestMail(myMailSubject);
		threadId=message.getThreadId();
		success = objMailTestUser2.trashMessage(threadId);
		Assert.assertTrue(success, "Mail deletion failed.");
		
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
				emailObj = objMailTestUser2.getMessageWithSubject(myMailSubject, "INBOX");
			}
			
			//set remediationSuccess as true if mail or attachment deleted based on remediation type
//			if (remedialAction.equals(Remediation.ITEM_DELETE_MAIL_BY_ATTACHMENT.name()) || remedialAction.equals(Remediation.ITEM_TRASH_MAIL.name())) {
			if ( remedialAction.equals(Remediation.ITEM_TRASH_MAIL.name())) {
				
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
		for (; i <= (MAX_EXPOSURE_WAIT_TIME_AFTER_REMEDIATION * 60 * 1000); i+=60000 ) {
			Reporter.log("Wait for "+TimeUnit.MILLISECONDS.toMinutes(i)+" minutes, and check if the file:"+myMailSubject +" is removed from exposure.",true);
			Reporter.log("------------------------------------------------------------------------",true );
			Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
			mailExpoDoc = this.getExposedDocuments(elapp.el_google_apps.name(), qparamsForMail);
			countAfterRemediation = mailExpoDoc.getMeta().getTotalCount();
			
			if (countAfterRemediation == 0) {
				break;
			}
			
		}
		
		
		Reporter.log("###### exposure count after remediation::"+countAfterRemediation, true);
		assertEquals(countAfterRemediation, 0, "Exposure count has not reduced after remediation.");
		//Check the user and collaborator list
		usersDoc1 = getCollaborators(elapp.el_google_apps.name(), qparamsForMail);
		assertEquals(usersDoc1.getMeta().getTotalCount(), 0, "Collaborator list is still not empty after deletion of email.");			
		usersDoc2 = getExposedUsers(elapp.el_google_apps.name(), qparamsForMail);
		assertEquals(usersDoc2.getMeta().getTotalCount(), 0,"Owner list is still not empty after deletion of email.");
		
		for (; i <= (MAX_EXPOSURE_WAIT_TIME_AFTER_REMEDIATION * 60 * 1000); i+=60000 ) {
			Reporter.log("Wait for "+TimeUnit.MILLISECONDS.toMinutes(i)+" minutes, and check if the file:"+searchFileName1 +" is removed from exposure.",true);
			Reporter.log("------------------------------------------------------------------------",true );
			Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
			exposureDoc1 = this.getExposedDocuments(elapp.el_google_apps.name(), qparamsForFile1);
			countAfterRemediation = exposureDoc1.getMeta().getTotalCount();
			
			if (countAfterRemediation == 0) {
				break;
			}
			
		}
		Reporter.log("###### exposure count after remediation::"+countAfterRemediation, true);
		assertEquals(countAfterRemediation, 0, "Exposure count has not reduced after remediation.");
		//Check the user and collaborator list
		usersDoc1 = getCollaborators(elapp.el_google_apps.name(), qparamsForFile1);
		assertEquals(usersDoc1.getMeta().getTotalCount(), 0,"Collaborator list is still not empty after deletion of email.");		
		usersDoc2 = getExposedUsers(elapp.el_google_apps.name(), qparamsForFile1);
		assertEquals(usersDoc2.getMeta().getTotalCount(), 0,"Owner list is still not empty after deletion of email.");
		
		for (; i <= (MAX_EXPOSURE_WAIT_TIME_AFTER_REMEDIATION * 60 * 1000); i+=60000 ) {
			Reporter.log("Wait for "+TimeUnit.MILLISECONDS.toMinutes(i)+" minutes, and check if the file:"+searchFileName2 +" is removed from exposure.",true);
			Reporter.log("------------------------------------------------------------------------",true );
			Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
			exposureDoc2 = this.getExposedDocuments(elapp.el_google_apps.name(), qparamsForFile2);
			countAfterRemediation = exposureDoc2.getMeta().getTotalCount();
			
			if (countAfterRemediation == 0) {
				break;
			}
			
		}
		
		Reporter.log("###### exposure count after remediation::"+countAfterRemediation, true);
		assertEquals(countAfterRemediation, 0, "Exposure count has not reduced after remediation.");
		//Check the user and collaborator list
		usersDoc1 = getCollaborators(elapp.el_google_apps.name(), qparamsForFile2);
		assertEquals(usersDoc1.getMeta().getTotalCount(), 0, "Collaborator list is still not empty after deletion of email.");		
		usersDoc2 = getExposedUsers(elapp.el_google_apps.name(), qparamsForFile2);
		assertEquals(usersDoc2.getMeta().getTotalCount(), 0,"Owner list is still not empty after deletion of email.");
		
		
		
	}
	/**
	 * External user sends content violation in mailbody and multiple attachments to multiple internal user
	 * Verify the exposure and then delete the email from inbox of each internal users one by one  and check file exposure changed or not.
	 * @param isInternal
	 * @param exposuretype
	 * @throws Exception
	 */
	@Test(groups={"P1"},retryAnalyzer=RetryAnalyzer.class)
	public void verifyRiskyDocsWhenMultipleAttachmentMailDeletedMultiple(
			) throws Exception {
		
		Reporter.log("****************Test Case Description****************",true);
		Reporter.log("Admin user sends content violation in mailbody and multiple attachments to multiple internal user. Verify the riskydocs and delete email from each user inbox and verify the risky docs. ",true);
		Reporter.log("1)Two internal users receive a Mail with risk in body and mulitple attachments from admin user",true);
		Reporter.log("2)Verify that the mail and attachment are appearing in risky docs",true);
		Reporter.log("3)Delete mail from inbox of user1.",true);
		Reporter.log("4)Verify that the mail and attachment remain in risky docs but user1 is removed from risky docs details",true);
		Reporter.log("5)Delete mail from inbox of user2.",true);
		Reporter.log("6)Verify that the mail and attachment are removed from risky docs",true);
		Reporter.log("*****************************************************",true);
		
		
		String isInternal = "true";
		Message emailObj = null;
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
		String mailBody = readFile(sourceFile2.toString());
		boolean success =false;
		String docTypeForRemediation = null;
		int countAfterExposure =0;
		int responseCode = 0;
		String expectedLog = null;
		List<String> to	= new ArrayList<>();
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
		String remedialAction ="ITEM_TRASH_MAIL";
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
		
		List<String> recipientsCc = new ArrayList<String>();
		List<String> recipientsTo = new ArrayList<String>();
		
		recipientsTo.add(testUser1);
		recipientsCc.add(testUser2);
		
		synchronized(this){ 
				success  = objMailAdmin.sendMessageWithMultipleAttachment(recipientsTo, recipientsCc, null, myMailSubject, mailBody,myAttachments);
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
			mailExpoDoc = this.getRiskyDocuments(elapp.el_google_apps.name(), qparamsForMail);
			countAfterExposure = mailExpoDoc.getMeta().getTotalCount();
			Reporter.log("Exposure Count ="+countAfterExposure, true);
			
			if (countAfterExposure >= 1) {break;}
		}
		assertEquals(countAfterExposure,1,"File:"+myMailSubject +" not appearing in risky docs even after waiting for "+TimeUnit.MILLISECONDS.toMinutes(i-60000)+" minutes.");
		assertTrue(mailExpoDoc.getObjects().get(0).getExposures().getInternal().toString().contains(testUser1),"User not found in risky docs internal user list: "+testUser1);
		assertTrue(mailExpoDoc.getObjects().get(0).getExposures().getInternal().toString().contains(testUser2),"User not found in risky docs internal user list: "+testUser2);
		 
		
		for (; i <= (MAX_EXPOSURE_WAIT_TIME_IN_MINUTES * 60 * 1000); i+=60000 ) {
			Reporter.log("Checking exposure after :"+TimeUnit.MILLISECONDS.toMinutes(i)+" minute(s)",true );
			Reporter.log("------------------------------------------------------------------------",true );
			Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
			
			
			//Get the exposure count
			exposureDoc1 = this.getRiskyDocuments(elapp.el_google_apps.name(), qparamsForFile1);
			countAfterExposure = exposureDoc1.getMeta().getTotalCount();
			Reporter.log("Exposure Count ="+countAfterExposure, true);
			
			if (countAfterExposure >= 1) {break;}
		}
		assertEquals(countAfterExposure,1,"File:"+searchFileName1 +" not appearing in risky docs even after waiting for "+TimeUnit.MILLISECONDS.toMinutes(i-60000)+" minutes.");
		assertTrue(exposureDoc1.getObjects().get(0).getExposures().getInternal().toString().contains(testUser1),"User not found in risky docs internal user list: "+testUser1);
		assertTrue(exposureDoc1.getObjects().get(0).getExposures().getInternal().toString().contains(testUser2),"User not found in risky docs internal user list: "+testUser2);
//		
		Reporter.log("------------------------------------------------------------------------",true );
		
		for (; i <= (MAX_EXPOSURE_WAIT_TIME_IN_MINUTES * 60 * 1000); i+=60000 ) {
			Reporter.log("Checking exposure after :"+TimeUnit.MILLISECONDS.toMinutes(i)+" minute(s)",true );
			Reporter.log("------------------------------------------------------------------------",true );
			Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
			
			
			//Get the exposure count
			exposureDoc2 = this.getRiskyDocuments(elapp.el_google_apps.name(), qparamsForFile2);
			countAfterExposure = exposureDoc2.getMeta().getTotalCount();
			Reporter.log("Exposure Count ="+countAfterExposure, true);
			
			if (countAfterExposure >= 1) {break;}
		}
		assertEquals(countAfterExposure,1,"File:"+searchFileName2 +" not appearing in risky docs even after waiting for "+TimeUnit.MILLISECONDS.toMinutes(i-60000)+" minutes.");
		assertTrue(exposureDoc1.getObjects().get(0).getExposures().getInternal().toString().contains(testUser1),"User not found in risky docs internal user list: "+testUser1);
		assertTrue(exposureDoc1.getObjects().get(0).getExposures().getInternal().toString().contains(testUser2),"User not found in risky docs internal user list: "+testUser2);
		
		
		Reporter.log("----------------Delete the mail from inbox of "+testUser1+" --------------------",true );
		Message message=objMailTestUser1.getLatestMail(myMailSubject);
		String threadId=message.getThreadId();
		success = objMailTestUser1.trashMessage(threadId);
		Assert.assertTrue(success, "Mail deletion failed.");
		
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
				emailObj = objMailTestUser1.getMessageWithSubject(myMailSubject, "INBOX");
			}
			
			//set remediationSuccess as true if mail or attachment deleted based on remediation type
//			if (remedialAction.equals(Remediation.ITEM_DELETE_MAIL_BY_ATTACHMENT.name()) || remedialAction.equals(Remediation.ITEM_TRASH_MAIL.name())) {
			if ( remedialAction.equals(Remediation.ITEM_TRASH_MAIL.name())) {
				
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
		for (; i <= (MAX_EXPOSURE_WAIT_TIME_AFTER_REMEDIATION * 60 * 1000); i+=60000 ) {
			Reporter.log("Wait for "+TimeUnit.MILLISECONDS.toMinutes(i)+" minutes, and check if the file:"+myMailSubject +" is removed from exposure.",true);
			Reporter.log("------------------------------------------------------------------------",true );
			Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
			mailExpoDoc = this.getRiskyDocuments(elapp.el_google_apps.name(), qparamsForMail);
			countAfterRemediation = mailExpoDoc.getMeta().getTotalCount();
			
			if (mailExpoDoc.getObjects().get(0).getExposures().getInternal().toString().contains(testUser1)==false) {
				break;
			}
			
		}
		
		assertFalse(mailExpoDoc.getObjects().get(0).getExposures().getInternal().toString().contains(testUser1),"User: "+testUser1+" is found in risky docs internal user listeven after waiting for \"+TimeUnit.MILLISECONDS.toMinutes(i-60000)+\" minutes.\"");
		assertTrue(mailExpoDoc.getObjects().get(0).getExposures().getInternal().toString().contains(testUser2),"User not found in risky docs internal user list: "+testUser2);
		
		
		for (; i <= (MAX_EXPOSURE_WAIT_TIME_IN_MINUTES * 60 * 1000); i+=60000 ) {
			Reporter.log("Checking exposure after :"+TimeUnit.MILLISECONDS.toMinutes(i)+" minute(s)",true );
			Reporter.log("------------------------------------------------------------------------",true );
			Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
			
			
			//Get the exposure count
			exposureDoc1 = this.getRiskyDocuments(elapp.el_google_apps.name(), qparamsForFile1);
			countAfterExposure = exposureDoc1.getMeta().getTotalCount();
			Reporter.log("Exposure Count ="+countAfterExposure, true);
			
			if (exposureDoc1.getObjects().get(0).getExposures().getInternal().toString().contains(testUser1)==false) {
				break;
			}
			
		}
		
		assertFalse(exposureDoc1.getObjects().get(0).getExposures().getInternal().toString().contains(testUser1),"User: "+testUser1+" is found in risky docs internal user listeven after waiting for \"+TimeUnit.MILLISECONDS.toMinutes(i-60000)+\" minutes.\"");
		assertTrue(exposureDoc1.getObjects().get(0).getExposures().getInternal().toString().contains(testUser2),"User not found in risky docs internal user list: "+testUser2);
		
		
		Reporter.log("------------------------------------------------------------------------",true );
		
		for (; i <= (MAX_EXPOSURE_WAIT_TIME_IN_MINUTES * 60 * 1000); i+=60000 ) {
			Reporter.log("Checking exposure after :"+TimeUnit.MILLISECONDS.toMinutes(i)+" minute(s)",true );
			Reporter.log("------------------------------------------------------------------------",true );
			Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
			
			
			//Get the exposure count
			exposureDoc2 = this.getRiskyDocuments(elapp.el_google_apps.name(), qparamsForFile2);
			if (exposureDoc2.getObjects().get(0).getExposures().getInternal().toString().contains(testUser1)==false) {
				break;
			}
			
		}
		
		assertFalse(exposureDoc2.getObjects().get(0).getExposures().getInternal().toString().contains(testUser1),"User: "+testUser1+" is found in risky docs internal user listeven after waiting for \"+TimeUnit.MILLISECONDS.toMinutes(i-60000)+\" minutes.\"");
		assertTrue(exposureDoc2.getObjects().get(0).getExposures().getInternal().toString().contains(testUser2),"User not found in risky docs internal user list: "+testUser2);
		
		
		
		Reporter.log("----------------Delete the mail from inbox of "+testUser2+" --------------------",true );
		success = objMailTestUser2.trashMailFromLabel(myMailSubject, "INBOX");
		Assert.assertTrue(success, "Mail deletion failed.");
		
		Reporter.log("----------------Delete the mail from Sent Items of sender --------------------",true );
		success= objMailAdmin.trashMailFromLabel(myMailSubject, "SENT");
		Assert.assertTrue(success, "Mail deletion failed.");
		
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
				emailObj = objMailTestUser2.getMessageWithSubject(myMailSubject, "INBOX");
			}
			
			//set remediationSuccess as true if mail or attachment deleted based on remediation type
//			if (remedialAction.equals(Remediation.ITEM_DELETE_MAIL_BY_ATTACHMENT.name()) || remedialAction.equals(Remediation.ITEM_TRASH_MAIL.name())) {
			if ( remedialAction.equals(Remediation.ITEM_TRASH_MAIL.name())) {
				
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
		for (; i <= (MAX_EXPOSURE_WAIT_TIME_AFTER_REMEDIATION * 60 * 1000); i+=60000 ) {
			Reporter.log("Wait for "+TimeUnit.MILLISECONDS.toMinutes(i)+" minutes, and check if the file:"+myMailSubject +" is removed from exposure.",true);
			Reporter.log("------------------------------------------------------------------------",true );
			Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
			mailExpoDoc = this.getRiskyDocuments(elapp.el_google_apps.name(), qparamsForMail);
			countAfterRemediation = mailExpoDoc.getMeta().getTotalCount();
			
			if (countAfterRemediation == 0) {
				break;
			}
			
		}
		
		
		Reporter.log("###### exposure count after remediation::"+countAfterRemediation, true);
		assertEquals(countAfterRemediation, 0, "Exposure count has not reduced after remediation.");
		
		for (; i <= (MAX_EXPOSURE_WAIT_TIME_AFTER_REMEDIATION * 60 * 1000); i+=60000 ) {
			Reporter.log("Wait for "+TimeUnit.MILLISECONDS.toMinutes(i)+" minutes, and check if the file:"+searchFileName1 +" is removed from exposure.",true);
			Reporter.log("------------------------------------------------------------------------",true );
			Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
			exposureDoc1 = this.getRiskyDocuments(elapp.el_google_apps.name(), qparamsForFile1);
			countAfterRemediation = exposureDoc1.getMeta().getTotalCount();
			
			if (countAfterRemediation == 0) {
				break;
			}
			
		}
		Reporter.log("###### exposure count after remediation::"+countAfterRemediation, true);
		assertEquals(countAfterRemediation, 0, "Exposure count has not reduced after remediation.");
		
		for (; i <= (MAX_EXPOSURE_WAIT_TIME_AFTER_REMEDIATION * 60 * 1000); i+=60000 ) {
			Reporter.log("Wait for "+TimeUnit.MILLISECONDS.toMinutes(i)+" minutes, and check if the file:"+searchFileName2 +" is removed from exposure.",true);
			Reporter.log("------------------------------------------------------------------------",true );
			Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
			exposureDoc2 = this.getRiskyDocuments(elapp.el_google_apps.name(), qparamsForFile2);
			countAfterRemediation = exposureDoc2.getMeta().getTotalCount();
			
			if (countAfterRemediation == 0) {
				break;
			}
			
		}
		
		Reporter.log("###### exposure count after remediation::"+countAfterRemediation, true);
		assertEquals(countAfterRemediation, 0, "Exposure count has not reduced after remediation.");
		
		
		
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
		Message emailObj = null;
		String myUniqueId = UUID.randomUUID().toString();
		List<String> myAttachments = new ArrayList<String>() ;
		String fileName1 = "PII.rtf";
		String fileName2 = "PCI_Test.txt";
		String searchFileName1 =myUniqueId+fileName1;
		String searchFileName2 =myUniqueId+fileName2;
		File sourceFile1 =new File(userDir+fileName1);
		File sourceFile2 =new File(userDir+fileName2);
		File destnFile1 =new File(userDir+searchFileName1);
		File destnFile2 =new File(userDir+searchFileName2);
		
		String myMailSubject =null;
		String mailBody = readFile(sourceFile2.toString());
		boolean success =false;
		String docTypeForRemediation = null;
		int countAfterExposure =0;
		int responseCode = 0;
		String expectedLog = null;
		List<String> to	= new ArrayList<>();
		List<String> cc	= new ArrayList<>();
		List<NameValuePair> qparamsForMail = new ArrayList<NameValuePair>(); 
		List<NameValuePair> qparamsForFile1 = new ArrayList<NameValuePair>(); 
		List<NameValuePair> qparamsForFile2 = new ArrayList<NameValuePair>(); 
		SecurletDocument mailExpoDoc = new SecurletDocument();
		SecurletDocument exposureDoc1 = new SecurletDocument();
		SecurletDocument exposureDoc2 = new SecurletDocument();
		SecurletDocument usersDoc1 = new SecurletDocument();
		SecurletDocument usersDoc2 = new SecurletDocument();
		SecurletDocument usersDoc3 = new SecurletDocument();
		String remedialAction ="ITEM_TRASH_MAIL";
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
				cc.add(adminUser);
				cc.add(externalUser2);
				success= objMailTestUser1.sendMessageWithMultipleAttachment(to, cc,null,myMailSubject,mailBody,myAttachments);
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
			mailExpoDoc = this.getExposedDocuments(elapp.el_google_apps.name(), qparamsForMail);
			countAfterExposure = mailExpoDoc.getMeta().getTotalCount();
			Reporter.log("Exposure Count ="+countAfterExposure, true);
			
			if (countAfterExposure >= 1) {break;}
		}
		assertEquals(countAfterExposure,1,"File:"+myMailSubject +" not exposed even after waiting for "+TimeUnit.MILLISECONDS.toMinutes(i-60000)+" minutes.");
		//Verifying the users in the exposure
		
		docValidator.validateExposedDocuments(mailExpoDoc, true);
		softAssert.assertEquals(mailExpoDoc.getObjects().get(0).getCreatedBy(),testUser1, "Value doesn't match for the Field: created_by.");
		softAssert.assertEquals(mailExpoDoc.getObjects().get(0).getDocType(),"Email_Message", "Value doesn't match for the Field: doc_type.");
		softAssert.assertEquals(mailExpoDoc.getObjects().get(0).getFormat(),"emailbody", "Value doesn't match for the Field: format.");
		softAssert.assertEquals(mailExpoDoc.getObjects().get(0).getObjectType(),"Mail", "Value doesn't match for the Field: object_type.");
		softAssert.assertEquals(mailExpoDoc.getObjects().get(0).getOwnedBy(),testUser1, "Value doesn't match for the Field: owned_by.");
		softAssert.assertTrue(mailExpoDoc.getObjects().get(0).getSize().intValue()>0, "Field: size value is not > 0");
		softAssert.assertEquals(mailExpoDoc.getObjects().get(0).getExposures().getAllInternal().booleanValue(),false, "Value doesn't match for the Field: exposures->all_internal.");
		softAssert.assertEquals(mailExpoDoc.getObjects().get(0).getExposures().getExtCount().intValue(),2, "Value doesn't match for the Field: exposures->ext_count.");
		softAssert.assertEquals(mailExpoDoc.getObjects().get(0).getExposures().getIntCount().intValue(),2, "Value doesn't match for the Field: exposures->int_count.");
		softAssert.assertEquals(mailExpoDoc.getObjects().get(0).getExposures().getPublic().booleanValue(),false, "Value doesn't match for the Field: exposures->public.");
		softAssert.assertTrue(mailExpoDoc.getObjects().get(0).getExposures().getExternal().toString().contains(externalUser1),"User not found in exposure external user list: "+externalUser1);
		softAssert.assertTrue(mailExpoDoc.getObjects().get(0).getExposures().getExternal().toString().contains(externalUser2),"User not found in exposure external user list: "+externalUser2);
		softAssert.assertTrue(mailExpoDoc.getObjects().get(0).getExposures().getInternal().toString().contains(adminUser),"User not found in exposure internal user list: "+adminUser);
		softAssert.assertTrue(mailExpoDoc.getObjects().get(0).getExposures().getInternal().toString().contains(testUser2),"User not found in exposure internal user list: "+testUser2);
		softAssert.assertAll();
		
		
		
		/****Exposure & collaborator check file1*****/
		
		for (; i <= (MAX_EXPOSURE_WAIT_TIME_IN_MINUTES * 60 * 1000); i+=60000 ) {
			Reporter.log("Checking exposure after :"+TimeUnit.MILLISECONDS.toMinutes(i)+" minute(s)",true );
			Reporter.log("------------------------------------------------------------------------",true );
			Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
			
			
			//Get the exposure count
			exposureDoc1 = this.getExposedDocuments(elapp.el_google_apps.name(), qparamsForFile1);
			countAfterExposure = exposureDoc1.getMeta().getTotalCount();
			Reporter.log("Exposure Count ="+countAfterExposure, true);
			
			if (countAfterExposure >= 1) {break;}
		}
		assertEquals(countAfterExposure,1,"File:"+searchFileName1 +" not exposed even after waiting for "+TimeUnit.MILLISECONDS.toMinutes(i-60000)+" minutes.");
		//Verifying the users in the exposure
		docValidator.validateExposedDocuments(exposureDoc1, true);
		softAssert.assertEquals(exposureDoc1.getObjects().get(0).getCreatedBy(),testUser1, "Value doesn't match for the Field: created_by.");
		softAssert.assertEquals(exposureDoc1.getObjects().get(0).getDocType(),"Email_File_Attachment", "Value doesn't match for the Field: doc_type.");
		softAssert.assertEquals(exposureDoc1.getObjects().get(0).getFormat(),"rtf", "Value doesn't match for the Field: format.");
		softAssert.assertEquals(exposureDoc1.getObjects().get(0).getObjectType(),"Mail", "Value doesn't match for the Field: object_type.");
		softAssert.assertEquals(exposureDoc1.getObjects().get(0).getOwnedBy(),testUser1, "Value doesn't match for the Field: owned_by.");
		softAssert.assertTrue(exposureDoc1.getObjects().get(0).getSize().intValue()>0, "Field: size value is not > 0");
		softAssert.assertEquals(exposureDoc1.getObjects().get(0).getExposures().getAllInternal().booleanValue(),false, "Value doesn't match for the Field: exposures->all_internal.");
		softAssert.assertEquals(exposureDoc1.getObjects().get(0).getExposures().getExtCount().intValue(),2, "Value doesn't match for the Field: exposures->ext_count.");
		softAssert.assertEquals(exposureDoc1.getObjects().get(0).getExposures().getIntCount().intValue(),2, "Value doesn't match for the Field: exposures->int_count.");
		softAssert.assertEquals(exposureDoc1.getObjects().get(0).getExposures().getPublic().booleanValue(),false, "Value doesn't match for the Field: exposures->public.");
		softAssert.assertTrue(exposureDoc1.getObjects().get(0).getExposures().getExternal().toString().contains(externalUser1),"User not found in exposure external user list: "+externalUser1);
		softAssert.assertTrue(exposureDoc1.getObjects().get(0).getExposures().getExternal().toString().contains(externalUser2),"User not found in exposure external user list: "+externalUser2);
		softAssert.assertTrue(exposureDoc1.getObjects().get(0).getExposures().getInternal().toString().contains(adminUser),"User not found in exposure internal user list: "+adminUser);
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
		Message emailObj = null;
		String myUniqueId = UUID.randomUUID().toString();
		List<String> myAttachments = new ArrayList<String>() ;
		String fileName1 = "PII.rtf";
		String fileName2 = "PCI_Test.txt";
		String searchFileName1 =myUniqueId+fileName1;
		String searchFileName2 =myUniqueId+fileName2;
		File sourceFile1 =new File(userDir+fileName1);
		File sourceFile2 =new File(userDir+fileName2);
		File destnFile1 =new File(userDir+searchFileName1);
		File destnFile2 =new File(userDir+searchFileName2);
		
		String myMailSubject =null;
		String mailBody = readFile(sourceFile2.toString());
		boolean success =false;
		String docTypeForRemediation = null;
		int countAfterExposure =0;
		int responseCode = 0;
		String expectedLog = null;
		List<String> to	= new ArrayList<>();
		List<String> cc	= new ArrayList<>();
		List<NameValuePair> qparamsForMail = new ArrayList<NameValuePair>(); 
		List<NameValuePair> qparamsForFile1 = new ArrayList<NameValuePair>(); 
		List<NameValuePair> qparamsForFile2 = new ArrayList<NameValuePair>(); 
		SecurletDocument mailExpoDoc = new SecurletDocument();
		SecurletDocument exposureDoc1 = new SecurletDocument();
		SecurletDocument exposureDoc2 = new SecurletDocument();
		SecurletDocument usersDoc1 = new SecurletDocument();
		SecurletDocument usersDoc2 = new SecurletDocument();
		SecurletDocument usersDoc3 = new SecurletDocument();
		String remedialAction ="ITEM_TRASH_MAIL";
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
				cc.add(adminUser);
				success= objMailTestUser1.sendMessageWithMultipleAttachment(to, cc,null,myMailSubject,mailBody,myAttachments);
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
			mailExpoDoc = this.getRiskyDocuments(elapp.el_google_apps.name(), qparamsForMail);
			countAfterExposure = mailExpoDoc.getMeta().getTotalCount();
			Reporter.log("Exposure Count ="+countAfterExposure, true);
			
			if (countAfterExposure >= 1) {break;}
		}
		assertEquals(countAfterExposure,1,"File:"+myMailSubject +" not appearing in risky docs even after waiting for "+TimeUnit.MILLISECONDS.toMinutes(i-60000)+" minutes.");
		//Verifying the users in the exposure
		
		docValidator.validateExposedDocuments(mailExpoDoc, true);
		softAssert.assertEquals(mailExpoDoc.getObjects().get(0).getCreatedBy(),testUser1, "Value doesn't match for the Field: created_by.");
		softAssert.assertEquals(mailExpoDoc.getObjects().get(0).getDocType(),"Email_Message", "Value doesn't match for the Field: doc_type.");
		softAssert.assertEquals(mailExpoDoc.getObjects().get(0).getFormat(),"emailbody", "Value doesn't match for the Field: format.");
		softAssert.assertEquals(mailExpoDoc.getObjects().get(0).getObjectType(),"Mail", "Value doesn't match for the Field: object_type.");
		softAssert.assertEquals(mailExpoDoc.getObjects().get(0).getOwnedBy(), testUser1, "Value doesn't match for the Field: owned_by.");
		softAssert.assertTrue(mailExpoDoc.getObjects().get(0).getSize().intValue()>0, "Field: size value is not > 0");
		softAssert.assertEquals(mailExpoDoc.getObjects().get(0).getExposures().getAllInternal().booleanValue(),false, "Value doesn't match for the Field: exposures->all_internal.");
		softAssert.assertEquals(mailExpoDoc.getObjects().get(0).getExposures().getExtCount().intValue(),0, "Value doesn't match for the Field: exposures->ext_count.");
		softAssert.assertEquals(mailExpoDoc.getObjects().get(0).getExposures().getIntCount().intValue(),2, "Value doesn't match for the Field: exposures->int_count.");
		softAssert.assertEquals(mailExpoDoc.getObjects().get(0).getExposures().getPublic().booleanValue(),false, "Value doesn't match for the Field: exposures->public.");
		softAssert.assertTrue(mailExpoDoc.getObjects().get(0).getExposures().getInternal().toString().contains(adminUser),"User not found in exposure internal user list: "+adminUser);
		softAssert.assertTrue(mailExpoDoc.getObjects().get(0).getExposures().getInternal().toString().contains(testUser2),"User not found in exposure internal user list: "+testUser2);
		softAssert.assertAll();
		
		
		
		/****Exposure & collaborator check file1*****/
		
		for (; i <= (MAX_EXPOSURE_WAIT_TIME_IN_MINUTES * 60 * 1000); i+=60000 ) {
			Reporter.log("Checking exposure after :"+TimeUnit.MILLISECONDS.toMinutes(i)+" minute(s)",true );
			Reporter.log("------------------------------------------------------------------------",true );
			Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
			
			
			//Get the exposure count
			exposureDoc1 = this.getRiskyDocuments(elapp.el_google_apps.name(), qparamsForFile1);
			countAfterExposure = exposureDoc1.getMeta().getTotalCount();
			Reporter.log("Exposure Count ="+countAfterExposure, true);
			
			if (countAfterExposure >= 1) {break;}
		}
		assertEquals(countAfterExposure,1,"File:"+searchFileName1 +" not appearing in risky docs even after waiting for "+TimeUnit.MILLISECONDS.toMinutes(i-60000)+" minutes.");
		//Verifying the users in the exposure
		docValidator.validateExposedDocuments(exposureDoc1, true);
		softAssert.assertEquals(exposureDoc1.getObjects().get(0).getCreatedBy(),testUser1, "Value doesn't match for the Field: created_by.");
		softAssert.assertEquals(exposureDoc1.getObjects().get(0).getDocType(),"Email_File_Attachment", "Value doesn't match for the Field: doc_type.");
		softAssert.assertEquals(exposureDoc1.getObjects().get(0).getFormat(),"rtf", "Value doesn't match for the Field: format.");
		softAssert.assertEquals(exposureDoc1.getObjects().get(0).getObjectType(),"Mail", "Value doesn't match for the Field: object_type.");
		softAssert.assertEquals(exposureDoc1.getObjects().get(0).getOwnedBy(),testUser1, "Value doesn't match for the Field: owned_by.");
		softAssert.assertTrue(exposureDoc1.getObjects().get(0).getSize().intValue()>0, "Field: size value is not > 0");
		softAssert.assertEquals(exposureDoc1.getObjects().get(0).getExposures().getAllInternal().booleanValue(),false, "Value doesn't match for the Field: exposures->all_internal.");
		softAssert.assertEquals(exposureDoc1.getObjects().get(0).getExposures().getExtCount().intValue(),0, "Value doesn't match for the Field: exposures->ext_count.");
		softAssert.assertEquals(exposureDoc1.getObjects().get(0).getExposures().getIntCount().intValue(),2, "Value doesn't match for the Field: exposures->int_count.");
		softAssert.assertEquals(exposureDoc1.getObjects().get(0).getExposures().getPublic().booleanValue(),false, "Value doesn't match for the Field: exposures->public.");
		softAssert.assertTrue(exposureDoc1.getObjects().get(0).getExposures().getInternal().toString().contains(adminUser),"User not found in exposure internal user list: "+adminUser);
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
		Message emailObj = null;
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
		String mailBody= readFile(sourceFile2.toString());
		boolean success =false;
		String docTypeForRemediation = null;
		int countAfterExposure =0;
		int responseCode = 0;
		String expectedLog = null;
		List<String> to	= new ArrayList<>();
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
		String remedialAction ="ITEM_TRASH_MAIL";
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
		
		List<String> recipientsCc = new ArrayList<String>();
		List<String> recipientsTo = new ArrayList<String>();
		
		recipientsTo.add(groupMailId);
		
		synchronized(this){ 
				success  = objMailAdmin.sendMessageWithMultipleAttachment(recipientsTo, null, null, myMailSubject, mailBody,myAttachments);
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
			mailExpoDoc = this.getRiskyDocuments(elapp.el_google_apps.name(), qparamsForMail);
			countAfterExposure = mailExpoDoc.getMeta().getTotalCount();
			Reporter.log("Exposure Count ="+countAfterExposure, true);
			
			if (countAfterExposure >= 1) {break;}
		}
		assertEquals(countAfterExposure,1,"File:"+myMailSubject +" not appearing in risky docs even after waiting for "+TimeUnit.MILLISECONDS.toMinutes(i-60000)+" minutes.");
		assertTrue(mailExpoDoc.getObjects().get(0).getExposures().getInternal().toString().contains(testUser1),"Group member not found in risky docs internal user list: "+testUser1);
		assertTrue(mailExpoDoc.getObjects().get(0).getExposures().getInternal().toString().contains(testUser2),"Group member not found in risky docs internal user list: "+testUser2);
		 
		
		for (; i <= (MAX_EXPOSURE_WAIT_TIME_IN_MINUTES * 60 * 1000); i+=60000 ) {
			Reporter.log("Checking exposure after :"+TimeUnit.MILLISECONDS.toMinutes(i)+" minute(s)",true );
			Reporter.log("------------------------------------------------------------------------",true );
			Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
			
			
			//Get the exposure count
			exposureDoc1 = this.getRiskyDocuments(elapp.el_google_apps.name(), qparamsForFile1);
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
			exposureDoc2 = this.getRiskyDocuments(elapp.el_google_apps.name(), qparamsForFile2);
			countAfterExposure = exposureDoc2.getMeta().getTotalCount();
			Reporter.log("Exposure Count ="+countAfterExposure, true);
			
			if (countAfterExposure >= 1) {break;}
		}
		assertEquals(countAfterExposure,1,"File:"+searchFileName2 +" not appearing in risky docs even after waiting for "+TimeUnit.MILLISECONDS.toMinutes(i-60000)+" minutes.");
		assertTrue(exposureDoc2.getObjects().get(0).getExposures().getInternal().toString().contains(testUser1),"Group member not found in risky docs internal user list: "+testUser1);
		assertTrue(exposureDoc2.getObjects().get(0).getExposures().getInternal().toString().contains(testUser2),"Group member not found in risky docs internal user list: "+testUser2);
		
		
		Reporter.log("----------------Delete the mail from inbox of "+testUser1+" --------------------",true );
		Message message=objMailTestUser1.getLatestMail(myMailSubject);
		String threadId=message.getThreadId();
		success = objMailTestUser1.trashMessage(threadId);
		Assert.assertTrue(success, "Mail deletion failed.");
		
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
				emailObj = objMailTestUser1.getMessageWithSubject(myMailSubject, "INBOX");
			}
			
			//set remediationSuccess as true if mail or attachment deleted based on remediation type
//			if (remedialAction.equals(Remediation.ITEM_DELETE_MAIL_BY_ATTACHMENT.name()) || remedialAction.equals(Remediation.ITEM_TRASH_MAIL.name())) {
			if ( remedialAction.equals(Remediation.ITEM_TRASH_MAIL.name())) {
				
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
		for (; i <= (MAX_EXPOSURE_WAIT_TIME_AFTER_REMEDIATION * 60 * 1000); i+=60000 ) {
			Reporter.log("After "+TimeUnit.MILLISECONDS.toMinutes(i)+" minutes, and check if the file:"+myMailSubject +" is removed from exposure.",true);
			Reporter.log("------------------------------------------------------------------------",true );
			Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
			mailExpoDoc = this.getRiskyDocuments(elapp.el_google_apps.name(), qparamsForMail);
			countAfterRemediation = mailExpoDoc.getMeta().getTotalCount();
			
			if (mailExpoDoc.getObjects().get(0).getExposures().getInternal().toString().contains(testUser1) == false) {
				break;
			}
			
		}
		
		assertFalse(mailExpoDoc.getObjects().get(0).getExposures().getInternal().toString().contains(testUser1),"User is found in risky docs internal user list: "+testUser1);
		assertTrue(mailExpoDoc.getObjects().get(0).getExposures().getInternal().toString().contains(testUser2),"User not found in risky docs internal user list: "+testUser2);
//		assertTrue(mailExpoDoc.getObjects().get(0).getExposures().getInternal().toString().contains(groupMailId),"GroupId not found in risky docs internal user list: "+groupMailId);
		
		//Check the user and collaborator list
//		usersDoc1 = getCollaborators(elapp.el_google_apps.name(), qparamsForMail);
//		myResponse = MarshallingUtils.marshall(usersDoc1);
//		assertFalse(myResponse.contains(testUser1),"User email is found in collaborators list:"+testUser1 +".");		
//		assertTrue(myResponse.contains(testUser2),"User email not found in collaborators list:"+testUser2 +".");		
//		usersDoc1 = getExposedUsers(elapp.el_google_apps.name(), qparamsForMail);
//		myResponse = MarshallingUtils.marshall(usersDoc1);
//		assertTrue(myResponse.contains(externalUser2),"User email not found in owner list:"+externalUser2 +".");
		
		for (; i <= (MAX_EXPOSURE_WAIT_TIME_IN_MINUTES * 60 * 1000); i+=60000 ) {
			Reporter.log("Checking exposure after :"+TimeUnit.MILLISECONDS.toMinutes(i)+" minute(s)",true );
			Reporter.log("------------------------------------------------------------------------",true );
			Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
			
			
			//Get the exposure count
			exposureDoc1 = this.getRiskyDocuments(elapp.el_google_apps.name(), qparamsForFile1);
			countAfterExposure = exposureDoc1.getMeta().getTotalCount();
			Reporter.log("Exposure Count ="+countAfterExposure, true);
			
			if (exposureDoc1.getObjects().get(0).getExposures().getInternal().toString().contains(testUser1) == false) {break;}
		}
		assertFalse(exposureDoc1.getObjects().get(0).getExposures().getInternal().toString().contains(testUser1),"User is found in risky docs internal user list: "+testUser1);
		assertTrue(exposureDoc1.getObjects().get(0).getExposures().getInternal().toString().contains(testUser2),"User not found in risky docs internal user list: "+testUser2);
		
		//Check the user and collaborator list
//		usersDoc1 = getCollaborators(elapp.el_google_apps.name(), qparamsForFile1);
//		myResponse = MarshallingUtils.marshall(usersDoc1);
//		assertFalse(myResponse.contains(testUser1),"User email is found in collaborators list:"+testUser1 +".");		
//		assertTrue(myResponse.contains(testUser2),"User email not found in collaborators list:"+testUser2 +".");		
//		usersDoc1 = getExposedUsers(elapp.el_google_apps.name(), qparamsForFile1);
//		myResponse = MarshallingUtils.marshall(usersDoc1);
//		assertTrue(myResponse.contains(externalUser2),"User email not found in owner list:"+externalUser2 +".");
		
		Reporter.log("------------------------------------------------------------------------",true );
		
		for (; i <= (MAX_EXPOSURE_WAIT_TIME_IN_MINUTES * 60 * 1000); i+=60000 ) {
			Reporter.log("Checking exposure after :"+TimeUnit.MILLISECONDS.toMinutes(i)+" minute(s)",true );
			Reporter.log("------------------------------------------------------------------------",true );
			Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
			
			
			//Get the exposure count
			exposureDoc2 = this.getRiskyDocuments(elapp.el_google_apps.name(), qparamsForFile2);
			countAfterExposure = exposureDoc2.getMeta().getTotalCount();
			Reporter.log("Exposure Count ="+countAfterExposure, true);
			
			if (exposureDoc2.getObjects().get(0).getExposures().getInternal().toString().contains(testUser1) == false) {break;}
		}
		assertFalse(exposureDoc2.getObjects().get(0).getExposures().getInternal().toString().contains(testUser1),"User is found in risky docs internal user list: "+testUser1);
		assertTrue(exposureDoc2.getObjects().get(0).getExposures().getInternal().toString().contains(testUser2),"User not found in risky docs internal user list: "+testUser2);
//		assertTrue(exposureDoc1.getObjects().get(0).getExposures().getInternal().toString().contains(testUser2),"User not found in risky docs internal user list: "+testUser2);
		
		//Check the user and collaborator list
//		usersDoc1 = getCollaborators(elapp.el_google_apps.name(), qparamsForFile2);
//		myResponse = MarshallingUtils.marshall(usersDoc1);
//		assertFalse(myResponse.contains(testUser1),"User email is found in collaborators list:"+testUser1 +".");		
//		assertTrue(myResponse.contains(testUser2),"User email not found in collaborators list:"+testUser2 +".");		
//		usersDoc1 = getExposedUsers(elapp.el_google_apps.name(), qparamsForFile2);
//		myResponse = MarshallingUtils.marshall(usersDoc1);
//		assertTrue(myResponse.contains(externalUser2),"User email not found in owner list:"+externalUser2 +".");
		
		
		Reporter.log("----------------Delete the mail from inbox of "+testUser2+" --------------------",true );
		success = objMailTestUser2.trashMailFromLabel(myMailSubject, "INBOX");
		Assert.assertTrue(success, "Mail deletion failed.");
		
		Reporter.log("----------------Delete the mail from Sent Items of sender --------------------",true );
		success= objMailAdmin.trashMailFromLabel(myMailSubject, "SENT");
		Assert.assertTrue(success, "Mail deletion failed.");
		
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
				emailObj = objMailTestUser2.getMessageWithSubject(myMailSubject, "INBOX");
			}
			
			//set remediationSuccess as true if mail or attachment deleted based on remediation type
//			if (remedialAction.equals(Remediation.ITEM_DELETE_MAIL_BY_ATTACHMENT.name()) || remedialAction.equals(Remediation.ITEM_TRASH_MAIL.name())) {
			if ( remedialAction.equals(Remediation.ITEM_TRASH_MAIL.name())) {
				
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
		for (; i <= (MAX_EXPOSURE_WAIT_TIME_AFTER_REMEDIATION * 60 * 1000); i+=60000 ) {
			Reporter.log("Wait for "+TimeUnit.MILLISECONDS.toMinutes(i)+" minutes, and check if the file:"+myMailSubject +" is removed from exposure.",true);
			Reporter.log("------------------------------------------------------------------------",true );
			Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
			mailExpoDoc = this.getRiskyDocuments(elapp.el_google_apps.name(), qparamsForMail);
			countAfterRemediation = mailExpoDoc.getMeta().getTotalCount();
			
			if (countAfterRemediation == 0) {
				break;
			}
			
		}
		
		
		Reporter.log("###### exposure count after mail deletion::"+countAfterRemediation, true);
		assertEquals(countAfterRemediation, 0, "Exposure count has not reduced after mail deletion.");
		//Check the user and collaborator list
//		usersDoc1 = getCollaborators(elapp.el_google_apps.name(), qparamsForMail);
//		assertEquals(usersDoc1.getMeta().getTotalCount(), 0, "Collaborator list is still not empty after deletion of email.");			
//		usersDoc2 = getExposedUsers(elapp.el_google_apps.name(), qparamsForMail);
//		assertEquals(usersDoc2.getMeta().getTotalCount(), 0,"Owner list is still not empty after deletion of email.");
		
		for (; i <= (MAX_EXPOSURE_WAIT_TIME_AFTER_REMEDIATION * 60 * 1000); i+=60000 ) {
			Reporter.log("Wait for "+TimeUnit.MILLISECONDS.toMinutes(i)+" minutes, and check if the file:"+searchFileName1 +" is removed from exposure.",true);
			Reporter.log("------------------------------------------------------------------------",true );
			Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
			exposureDoc1 = this.getRiskyDocuments(elapp.el_google_apps.name(), qparamsForFile1);
			countAfterRemediation = exposureDoc1.getMeta().getTotalCount();
			
			if (countAfterRemediation == 0) {
				break;
			}
			
		}
		Reporter.log("###### exposure count after mail deletion::"+countAfterRemediation, true);
		assertEquals(countAfterRemediation, 0, "Exposure count has not reduced after mail deletion.");
		//Check the user and collaborator list
//		usersDoc1 = getCollaborators(elapp.el_google_apps.name(), qparamsForFile1);
//		assertEquals(usersDoc1.getMeta().getTotalCount(), 0,"Collaborator list is still not empty after deletion of email.");		
//		usersDoc2 = getExposedUsers(elapp.el_google_apps.name(), qparamsForFile1);
//		assertEquals(usersDoc2.getMeta().getTotalCount(), 0,"Owner list is still not empty after deletion of email.");
		
		for (; i <= (MAX_EXPOSURE_WAIT_TIME_AFTER_REMEDIATION * 60 * 1000); i+=60000 ) {
			Reporter.log("Wait for "+TimeUnit.MILLISECONDS.toMinutes(i)+" minutes, and check if the file:"+searchFileName2 +" is removed from exposure.",true);
			Reporter.log("------------------------------------------------------------------------",true );
			Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
			exposureDoc2 = this.getRiskyDocuments(elapp.el_google_apps.name(), qparamsForFile2);
			countAfterRemediation = exposureDoc2.getMeta().getTotalCount();
			
			if (countAfterRemediation == 0) {
				break;
			}
			
		}
		
		Reporter.log("###### exposure count after mail deletion::"+countAfterRemediation, true);
		assertEquals(countAfterRemediation, 0, "Exposure count has not reduced after mail deletion.");
		//Check the user and collaborator list
//		usersDoc1 = getCollaborators(elapp.el_google_apps.name(), qparamsForFile2);
//		assertEquals(usersDoc1.getMeta().getTotalCount(), 0, "Collaborator list is still not empty after deletion of email.");		
//		usersDoc2 = getExposedUsers(elapp.el_google_apps.name(), qparamsForFile2);
//		assertEquals(usersDoc2.getMeta().getTotalCount(), 0,"Owner list is still not empty after deletion of email.");
		
		
		
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
		Message emailObj = null;
		String myUniqueId = UUID.randomUUID().toString();
		List<String> myAttachments = new ArrayList<String>() ;
		String fileName1 = "PII.rtf";
		String fileName2 = "PCI_Test.txt";
		String searchFileName1 =myUniqueId+fileName1;
		String searchFileName2 =myUniqueId+fileName2;
		File sourceFile1 =new File(userDir+fileName1);
		File sourceFile2 =new File(userDir+fileName2);
		File destnFile1 =new File(userDir+searchFileName1);
		File destnFile2 =new File(userDir+searchFileName2);
		
		String myMailSubject =null;
		String mailBody = readFile(sourceFile2.toString());
		boolean success =false;
		String docTypeForRemediation = null;
		int countAfterExposure =0;
		int responseCode = 0;
		String expectedLog = null;
		List<String> to	= new ArrayList<>();
		List<String> cc	= new ArrayList<>();
		List<NameValuePair> qparamsForMail = new ArrayList<NameValuePair>(); 
		List<NameValuePair> qparamsForFile1 = new ArrayList<NameValuePair>(); 
		List<NameValuePair> qparamsForFile2 = new ArrayList<NameValuePair>(); 
		SecurletDocument mailExpoDoc = new SecurletDocument();
		SecurletDocument exposureDoc1 = new SecurletDocument();
		SecurletDocument exposureDoc2 = new SecurletDocument();
		SecurletDocument usersDoc1 = new SecurletDocument();
		SecurletDocument usersDoc2 = new SecurletDocument();
		SecurletDocument usersDoc3 = new SecurletDocument();
		String remedialAction ="ITEM_TRASH_MAIL";
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
				success= objMailAdmin.sendMessageWithMultipleAttachment(to, cc,null,myMailSubject,mailBody,myAttachments);
			}
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
			mailExpoDoc = this.getExposedDocuments(elapp.el_google_apps.name(), qparamsForMail);
			countAfterExposure = mailExpoDoc.getMeta().getTotalCount();
			Reporter.log("Exposure Count ="+countAfterExposure, true);
			
			if (countAfterExposure >= 1) {break;}
		}
		assertEquals(countAfterExposure,1,"File:"+myMailSubject +" not exposed even after waiting for "+TimeUnit.MILLISECONDS.toMinutes(i-60000)+" minutes.");
		//Check the user and collaborator list
		usersDoc1 = getCollaborators(elapp.el_google_apps.name(), qparamsForMail);
		myResponse = MarshallingUtils.marshall(usersDoc1);
		assertTrue(myResponse.contains(externalUser1),"User email not found in collaborators list:"+externalUser1 +".");		
//		assertTrue(myResponse.contains(groupMailId),"User email not found in collaborators list:"+groupMailId +".");		
		usersDoc2 = getExposedUsers(elapp.el_google_apps.name(), qparamsForMail);
		assertTrue(usersDoc2.getObjects().get(0).getEmail().contains(adminUser),"User email not found in owner list:"+adminUser +".");
		
		for (; i <= (MAX_EXPOSURE_WAIT_TIME_IN_MINUTES * 60 * 1000); i+=60000 ) {
			Reporter.log("Checking exposure after :"+TimeUnit.MILLISECONDS.toMinutes(i)+" minute(s)",true );
			Reporter.log("------------------------------------------------------------------------",true );
			Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
			
			
			//Get the exposure count
			exposureDoc1 = this.getExposedDocuments(elapp.el_google_apps.name(), qparamsForFile1);
			countAfterExposure = exposureDoc1.getMeta().getTotalCount();
			Reporter.log("Exposure Count ="+countAfterExposure, true);
			
			if (countAfterExposure >= 1) {break;}
		}
		assertEquals(countAfterExposure,1,"File:"+searchFileName1 +" not exposed even after waiting for "+TimeUnit.MILLISECONDS.toMinutes(i-60000)+" minutes.");
		
		//Check the user and collaborator list
		usersDoc1 = getCollaborators(elapp.el_google_apps.name(), qparamsForFile1);
		myResponse = MarshallingUtils.marshall(usersDoc1);
		assertTrue(myResponse.contains(externalUser1),"User email not found in collaborators list:"+externalUser1 +".");	
//		assertTrue(myResponse.contains(groupMailId),"User email not found in collaborators list:"+groupMailId +".");
		usersDoc2 = getExposedUsers(elapp.el_google_apps.name(), qparamsForFile1);
		assertTrue(usersDoc2.getObjects().get(0).getEmail().contains(adminUser),"User email not found in owner list:"+adminUser +".");
		
		Reporter.log("------------------------------------------------------------------------",true );
		
		for (; i <= (MAX_EXPOSURE_WAIT_TIME_IN_MINUTES * 60 * 1000); i+=60000 ) {
			Reporter.log("Checking exposure after :"+TimeUnit.MILLISECONDS.toMinutes(i)+" minute(s)",true );
			Reporter.log("------------------------------------------------------------------------",true );
			Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
			
			
			//Get the exposure count
			exposureDoc2 = this.getExposedDocuments(elapp.el_google_apps.name(), qparamsForFile2);
			countAfterExposure = exposureDoc2.getMeta().getTotalCount();
			Reporter.log("Exposure Count ="+countAfterExposure, true);
			
			if (countAfterExposure >= 1) {break;}
		}
		assertEquals(countAfterExposure,1,"File:"+searchFileName2 +" not exposed even after waiting for "+TimeUnit.MILLISECONDS.toMinutes(i-60000)+" minutes.");
		
		Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
		//Check the user and collaborator list
		usersDoc1 = getCollaborators(elapp.el_google_apps.name(), qparamsForFile2);
		myResponse = MarshallingUtils.marshall(usersDoc1);
		assertTrue(myResponse.contains(externalUser1),"User email not found in collaborators list:"+externalUser1 +".");		
//		assertTrue(myResponse.contains(groupMailId),"User email not found in collaborators list:"+groupMailId +".");
		usersDoc2 = getExposedUsers(elapp.el_google_apps.name(), qparamsForFile2);
		assertTrue(usersDoc2.getObjects().get(0).getEmail().contains(adminUser),"User email not found in owner list:"+adminUser +".");
		
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
		MailRemediation objRemediation = getRemediationObject(adminUser, docTypeForRemediation, docId, remedialAction);
		
		
		if(deletionType.equals("by remediation")){
			//Now apply the remedial action through API server call
			responseCode = remediateExposureWithAPI(objRemediation);
			//Assert.assertEquals(responseCode, 202, "Remediation call failed."); 
		}
		else if (deletionType.equals("from mailbox")){
			
			success= objMailAdmin.trashMailFromLabel(myMailSubject, "SENT");
			Assert.assertTrue(success, "Mail deletion failed from Sent folder of admin.");
			
			success= objMailTestUser1.trashMailFromLabel(myMailSubject, "INBOX");
			Assert.assertTrue(success, "Mail deletion failed from Inbox of user1.");
			
			success= objMailTestUser2.trashMailFromLabel(myMailSubject, "INBOX");
			Assert.assertTrue(success, "Mail deletion failed from Inbox of user2.");
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
				emailObj = objMailAdmin.getMessageWithSubject(myMailSubject, "SENT");
			}
			
			//set remediationSuccess as true if mail or attachment deleted based on remediation type
//			if (remedialAction.equals(Remediation.ITEM_DELETE_MAIL_BY_ATTACHMENT.name()) || remedialAction.equals(Remediation.ITEM_TRASH_MAIL.name())) {
			if ( remedialAction.equals(Remediation.ITEM_TRASH_MAIL.name())) {
				
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
//		if (remedialAction.equals(Remediation.ITEM_DELETE_MAIL_BY_ATTACHMENT.name()) || remedialAction.equals(Remediation.ITEM_TRASH_MAIL.name())) {
		if (remedialAction.equals(Remediation.ITEM_TRASH_MAIL.name())) {
			expectedLog = "User trashed email with subject \""+myMailSubject+"\"";//expectedLog = "QA Admin has remediated/deleted an email with subject \""+myMailSubject+"\"";
			assertNull(emailObj, "Remediation "+ remedialAction + " didn't work. Mail is not deleted from sent folder of "+adminUser+" even after waiting for "+TimeUnit.MILLISECONDS.toMinutes(i-60000)+" minutes.");
			
		}
		
		Thread.sleep(CommonConstants.TWO_MINUTES_SLEEP);
		
		//verify that mails are deleted from inbox for internal users
		Message message=objMailTestUser2.getMessageWithSubject(myMailSubject, "INBOX");
		Assert.assertTrue(message==null, "Mail was not deleted from the inbox of user:"+testUser2);
		message=objMailTestUser1.getMessageWithSubject(myMailSubject, "INBOX");
		Assert.assertTrue(message==null, "Mail was not deleted from the inbox of user:"+testUser1);
		
		
	
		
		Reporter.log("Remediation successful at GMAIL");
		
		//gather the forensic logs again 
		//logs = gatherForensicLogMessages(uniqueId, facility.Box.name());
		
		Reporter.log("------------------------------------------------------------------------",true );
		
		int countAfterRemediation = countAfterExposure ;
		i = 60000;
		for (; i <= (MAX_EXPOSURE_WAIT_TIME_AFTER_REMEDIATION * 60 * 1000); i+=60000 ) {
			Reporter.log("Wait for "+TimeUnit.MILLISECONDS.toMinutes(i)+" minutes, and check if the file:"+myMailSubject +" is removed from exposure.",true);
			Reporter.log("------------------------------------------------------------------------",true );
			Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
			mailExpoDoc = this.getExposedDocuments(elapp.el_google_apps.name(), qparamsForMail);
			countAfterRemediation = mailExpoDoc.getMeta().getTotalCount();
			
			if (countAfterRemediation == 0) {
				break;
			}
			
		}
		
		
		Reporter.log("###### exposure count after remediation::"+countAfterRemediation, true);
		assertEquals(countAfterRemediation, 0, "Exposure count has not reduced after remediation.");
		
		Thread.sleep(CommonConstants.TWO_MINUTES_SLEEP);
		
		//Check the user and collaborator list
		usersDoc1 = getCollaborators(elapp.el_google_apps.name(), qparamsForMail);
		myResponse = MarshallingUtils.marshall(usersDoc1);
		assertFalse(myResponse.contains(externalUser1),"Collaborator is still exposed after deletion of email:"+externalUser1 +".");		
//		assertFalse(myResponse.contains(groupMailId),"Collaborator is still exposed after deletion of email:"+groupMailId +".");		
		usersDoc2 = getExposedUsers(elapp.el_google_apps.name(), qparamsForMail);
		assertEquals(usersDoc2.getMeta().getTotalCount(), 0,"Owner is still exposed after deletion of email:"+adminUser +".");
		
		for (; i <= (MAX_EXPOSURE_WAIT_TIME_AFTER_REMEDIATION * 60 * 1000); i+=60000 ) {
			Reporter.log("Wait for "+TimeUnit.MILLISECONDS.toMinutes(i)+" minutes, and check if the file:"+searchFileName1 +" is removed from exposure.",true);
			Reporter.log("------------------------------------------------------------------------",true );
			Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
			exposureDoc1 = this.getExposedDocuments(elapp.el_google_apps.name(), qparamsForFile1);
			countAfterRemediation = exposureDoc1.getMeta().getTotalCount();
			
			if (countAfterRemediation == 0) {
				break;
			}
			
		}
		Reporter.log("###### exposure count after remediation::"+countAfterRemediation, true);
		assertEquals(countAfterRemediation, 0, "Exposure count has not reduced after remediation.");
		//Check the user and collaborator list
		usersDoc1 = getCollaborators(elapp.el_google_apps.name(), qparamsForFile1);
		myResponse = MarshallingUtils.marshall(usersDoc1);
		assertFalse(myResponse.contains(externalUser1),"Collaborator is still exposed after deletion of email:"+externalUser1 +".");		
//		assertFalse(myResponse.contains(externalUser2),"Collaborator is still exposed after deletion of email:"+externalUser2 +".");		
		usersDoc2 = getExposedUsers(elapp.el_google_apps.name(), qparamsForFile1);
		assertEquals(usersDoc2.getMeta().getTotalCount(), 0,"Owner is still exposed after deletion of email:"+testUser1 +".");
		
		for (; i <= (MAX_EXPOSURE_WAIT_TIME_AFTER_REMEDIATION * 60 * 1000); i+=60000 ) {
			Reporter.log("Wait for "+TimeUnit.MILLISECONDS.toMinutes(i)+" minutes, and check if the file:"+searchFileName2 +" is removed from exposure.",true);
			Reporter.log("------------------------------------------------------------------------",true );
			Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
			exposureDoc2 = this.getExposedDocuments(elapp.el_google_apps.name(), qparamsForFile2);
			countAfterRemediation = exposureDoc2.getMeta().getTotalCount();
			
			if (countAfterRemediation == 0) {
				break;
			}
			
		}
		Reporter.log("###### exposure count after remediation::"+countAfterRemediation, true);
		assertEquals(countAfterRemediation, 0, "Exposure count has not reduced after remediation.");
		//Check the user and collaborator list
		usersDoc1 = getCollaborators(elapp.el_google_apps.name(), qparamsForFile2);
		myResponse = MarshallingUtils.marshall(usersDoc1);
		assertFalse(myResponse.contains(externalUser1),"Collaborator is still exposed after deletion of email:"+externalUser1 +".");		
//		assertFalse(myResponse.contains(externalUser2),"Collaborator is still exposed after deletion of email:"+externalUser2 +".");		
		usersDoc2 = getExposedUsers(elapp.el_google_apps.name(), qparamsForFile2);
		assertEquals(usersDoc2.getMeta().getTotalCount(), 0,"Owner is still exposed after deletion of email:"+testUser1 +".");
		
	
//		//Verify the activity logs
//		Reporter.log("Expected log:" + expectedLog, true);
//		ForensicSearchResults availableLogs = searchLogsWithWaitTime("Activity_type", "Trash", myUniqueId, testUser1, 1, 2);
//		assertNotNull(availableLogs,"Delete log not found after remediation");
//		assertEquals(availableLogs.getHits().getHits().get(0).getSource().getMessage(),expectedLog,	"Test failed, as expected log is not found after remediation by admin.");
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
		Message emailObj = null;
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
		String mailBody = readFile(sourceFile2.toString());
		boolean success =false;
		String docTypeForRemediation = null;
		int countAfterExposure =0;
		int responseCode = 0;
		String expectedLog = null;
		List<String> to	= new ArrayList<>();
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
		String remedialAction ="ITEM_TRASH_MAIL";
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
			success = objMailExternalUser2.sendMailWithCCAndBCC(recipientsTo,null,null, myMailSubject,mailBody ,myAttachments , false);
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
			mailExpoDoc = this.getExposedDocuments(elapp.el_google_apps.name(), qparamsForMail);
			countAfterExposure = mailExpoDoc.getMeta().getTotalCount();
			Reporter.log("Exposure Count ="+countAfterExposure, true);
			
			if (countAfterExposure >= 1) {break;}
		}
		assertEquals(countAfterExposure,1,"File:"+myMailSubject +" not exposed even after waiting for "+TimeUnit.MILLISECONDS.toMinutes(i-60000)+" minutes.");
//		assertTrue(mailExpoDoc.getObjects().get(0).getExposures().getInternal().toString().contains(groupMailId),"User not found in exposure internal user list: "+groupMailId);
		assertTrue(mailExpoDoc.getObjects().get(0).getExposures().getInternal().toString().contains(testUser1),"User not found in exposure internal user list: "+testUser1);
		assertTrue(mailExpoDoc.getObjects().get(0).getExposures().getInternal().toString().contains(testUser2),"User not found in exposure internal user list: "+testUser2);
		
		//Check the user and collaborator list
		usersDoc1 = getCollaborators(elapp.el_google_apps.name(), qparamsForMail);
		myResponse = MarshallingUtils.marshall(usersDoc1);
//		assertTrue(myResponse.contains(groupMailId),"User email not found in collaborators list:"+groupMailId +".");		
		assertTrue(myResponse.contains(testUser1),"User email not found in collaborators list:"+testUser1 +".");		
		assertTrue(myResponse.contains(testUser2),"User email not found in collaborators list:"+testUser2 +".");		
		usersDoc1 = getExposedUsers(elapp.el_google_apps.name(), qparamsForMail);
		myResponse = MarshallingUtils.marshall(usersDoc1);
		assertTrue(myResponse.contains(externalUser2),"User email not found in owner list:"+externalUser2 +".");
		
		for (; i <= (MAX_EXPOSURE_WAIT_TIME_IN_MINUTES * 60 * 1000); i+=60000 ) {
			Reporter.log("Checking exposure after :"+TimeUnit.MILLISECONDS.toMinutes(i)+" minute(s)",true );
			Reporter.log("------------------------------------------------------------------------",true );
			Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
			
			
			//Get the exposure count
			exposureDoc1 = this.getExposedDocuments(elapp.el_google_apps.name(), qparamsForFile1);
			countAfterExposure = exposureDoc1.getMeta().getTotalCount();
			Reporter.log("Exposure Count ="+countAfterExposure, true);
			
			if (countAfterExposure >= 1) {break;}
		}
		assertEquals(countAfterExposure,1,"File:"+searchFileName1 +" not exposed even after waiting for "+TimeUnit.MILLISECONDS.toMinutes(i-60000)+" minutes.");
//		assertTrue(exposureDoc1.getObjects().get(0).getExposures().getInternal().toString().contains(groupMailId),"User not found in exposure internal user list: "+groupMailId);
		assertTrue(exposureDoc1.getObjects().get(0).getExposures().getInternal().toString().contains(testUser1),"User not found in exposure internal user list: "+testUser1);
		assertTrue(exposureDoc1.getObjects().get(0).getExposures().getInternal().toString().contains(testUser2),"User not found in exposure internal user list: "+testUser2);
		//Check the user and collaborator list
		usersDoc1 = getCollaborators(elapp.el_google_apps.name(), qparamsForFile1);
		myResponse = MarshallingUtils.marshall(usersDoc1);
//		assertTrue(myResponse.contains(groupMailId),"User email not found in collaborators list:"+groupMailId +".");		
		assertTrue(myResponse.contains(testUser1),"User email not found in collaborators list:"+testUser1 +".");		
		assertTrue(myResponse.contains(testUser2),"User email not found in collaborators list:"+testUser2 +".");		
		usersDoc1 = getExposedUsers(elapp.el_google_apps.name(), qparamsForFile1);
		myResponse = MarshallingUtils.marshall(usersDoc1);
		assertTrue(myResponse.contains(externalUser2),"User email not found in owner list:"+externalUser2 +".");
		
		Reporter.log("------------------------------------------------------------------------",true );
		
		for (; i <= (MAX_EXPOSURE_WAIT_TIME_IN_MINUTES * 60 * 1000); i+=60000 ) {
			Reporter.log("Checking exposure after :"+TimeUnit.MILLISECONDS.toMinutes(i)+" minute(s)",true );
			Reporter.log("------------------------------------------------------------------------",true );
			Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
			
			
			//Get the exposure count
			exposureDoc2 = this.getExposedDocuments(elapp.el_google_apps.name(), qparamsForFile2);
			countAfterExposure = exposureDoc2.getMeta().getTotalCount();
			Reporter.log("Exposure Count ="+countAfterExposure, true);
			
			if (countAfterExposure >= 1) {break;}
		}
		assertEquals(countAfterExposure,1,"File:"+searchFileName2 +" not exposed even after waiting for "+TimeUnit.MILLISECONDS.toMinutes(i-60000)+" minutes.");
//		assertTrue(exposureDoc1.getObjects().get(0).getExposures().getInternal().toString().contains(groupMailId),"User not found in exposure internal user list: "+groupMailId);
		assertTrue(exposureDoc1.getObjects().get(0).getExposures().getInternal().toString().contains(testUser1),"User not found in exposure internal user list: "+testUser1);
		assertTrue(exposureDoc1.getObjects().get(0).getExposures().getInternal().toString().contains(testUser2),"User not found in exposure internal user list: "+testUser2);
		//Check the user and collaborator list
		usersDoc1 = getCollaborators(elapp.el_google_apps.name(), qparamsForFile2);
		myResponse = MarshallingUtils.marshall(usersDoc1);
//		assertTrue(myResponse.contains(groupMailId),"User email not found in collaborators list:"+groupMailId +".");		
		assertTrue(myResponse.contains(testUser1),"User email not found in collaborators list:"+testUser1 +".");		
		assertTrue(myResponse.contains(testUser2),"User email not found in collaborators list:"+testUser2 +".");		
		usersDoc1 = getExposedUsers(elapp.el_google_apps.name(), qparamsForFile2);
		myResponse = MarshallingUtils.marshall(usersDoc1);
		assertTrue(myResponse.contains(externalUser2),"User email not found in owner list:"+externalUser2 +".");
		
		
		Reporter.log("----------------Delete the mail from inbox of "+testUser1+" --------------------",true );
		Message message=objMailTestUser1.getLatestMail(myMailSubject);
		String threadId=message.getThreadId();
		success = objMailTestUser1.trashMessage(threadId);
		Assert.assertTrue(success, "Mail deletion failed.");
		
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
				emailObj = objMailTestUser1.getMessageWithSubject(myMailSubject, "INBOX");
			}
			
			//set remediationSuccess as true if mail or attachment deleted based on remediation type
//			if (remedialAction.equals(Remediation.ITEM_DELETE_MAIL_BY_ATTACHMENT.name()) || remedialAction.equals(Remediation.ITEM_TRASH_MAIL.name())) {
			if ( remedialAction.equals(Remediation.ITEM_TRASH_MAIL.name())) {
				
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
		for (; i <= (MAX_EXPOSURE_WAIT_TIME_AFTER_REMEDIATION * 60 * 1000); i+=60000 ) {
			Reporter.log("Wait for "+TimeUnit.MILLISECONDS.toMinutes(i)+" minutes, and check if the file:"+myMailSubject +" is removed from exposure.",true);
			Reporter.log("------------------------------------------------------------------------",true );
			Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
			mailExpoDoc = this.getExposedDocuments(elapp.el_google_apps.name(), qparamsForMail);
			countAfterRemediation = mailExpoDoc.getMeta().getTotalCount();
			
			if (mailExpoDoc.getObjects().get(0).getExposures().getInternal().toString().contains(testUser1)==false) {
				break;
			}
			
		}
		
		assertFalse(mailExpoDoc.getObjects().get(0).getExposures().getInternal().toString().contains(testUser1),"User is found in exposure internal user list: "+testUser1);
		assertTrue(mailExpoDoc.getObjects().get(0).getExposures().getInternal().toString().contains(testUser2),"User not found in exposure internal user list: "+testUser2);
//		assertTrue(mailExpoDoc.getObjects().get(0).getExposures().getInternal().toString().contains(groupMailId),"User not found in exposure internal user list: "+groupMailId);
		
		//Check the user and collaborator list
		usersDoc1 = getCollaborators(elapp.el_google_apps.name(), qparamsForMail);
		myResponse = MarshallingUtils.marshall(usersDoc1);
		assertFalse(myResponse.contains(testUser1),"User email is found in collaborators list:"+testUser1 +".");		
		assertTrue(myResponse.contains(testUser2),"User email not found in collaborators list:"+testUser2 +".");		
//		assertTrue(myResponse.contains(groupMailId),"User email is found in collaborators list:"+groupMailId +".");		
		usersDoc1 = getExposedUsers(elapp.el_google_apps.name(), qparamsForMail);
		myResponse = MarshallingUtils.marshall(usersDoc1);
		assertTrue(myResponse.contains(externalUser2),"User email not found in owner list:"+externalUser2 +".");
		
		for (; i <= (MAX_EXPOSURE_WAIT_TIME_IN_MINUTES * 60 * 1000); i+=60000 ) {
			Reporter.log("Checking exposure after :"+TimeUnit.MILLISECONDS.toMinutes(i)+" minute(s)",true );
			Reporter.log("------------------------------------------------------------------------",true );
			Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
			
			
			//Get the exposure count
			exposureDoc1 = this.getExposedDocuments(elapp.el_google_apps.name(), qparamsForFile1);
			countAfterExposure = exposureDoc1.getMeta().getTotalCount();
			Reporter.log("Exposure Count ="+countAfterExposure, true);
			
			if (exposureDoc1.getObjects().get(0).getExposures().getInternal().toString().contains(testUser1)==false) {break;}
		}
		assertFalse(exposureDoc1.getObjects().get(0).getExposures().getInternal().toString().contains(testUser1),"User is found in exposure internal user list: "+testUser1);
		assertTrue(exposureDoc1.getObjects().get(0).getExposures().getInternal().toString().contains(testUser2),"User not found in exposure internal user list: "+testUser2);
//		assertTrue(exposureDoc1.getObjects().get(0).getExposures().getInternal().toString().contains(groupMailId),"User not found in exposure internal user list: "+groupMailId);
		
		//Check the user and collaborator list
		usersDoc1 = getCollaborators(elapp.el_google_apps.name(), qparamsForFile1);
		myResponse = MarshallingUtils.marshall(usersDoc1);
		assertFalse(myResponse.contains(testUser1),"User email is found in collaborators list:"+testUser1 +".");		
		assertTrue(myResponse.contains(testUser2),"User email not found in collaborators list:"+testUser2 +".");		
//		assertTrue(myResponse.contains(groupMailId),"User email not found in collaborators list:"+groupMailId +".");		
		usersDoc1 = getExposedUsers(elapp.el_google_apps.name(), qparamsForFile1);
		myResponse = MarshallingUtils.marshall(usersDoc1);
		assertTrue(myResponse.contains(externalUser2),"User email not found in owner list:"+externalUser2 +".");
		
		Reporter.log("------------------------------------------------------------------------",true );
		
		for (; i <= (MAX_EXPOSURE_WAIT_TIME_IN_MINUTES * 60 * 1000); i+=60000 ) {
			Reporter.log("Checking exposure after :"+TimeUnit.MILLISECONDS.toMinutes(i)+" minute(s)",true );
			Reporter.log("------------------------------------------------------------------------",true );
			Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
			
			
			//Get the exposure count
			exposureDoc2 = this.getExposedDocuments(elapp.el_google_apps.name(), qparamsForFile2);
			countAfterExposure = exposureDoc2.getMeta().getTotalCount();
			Reporter.log("Exposure Count ="+countAfterExposure, true);
			
			if (exposureDoc2.getObjects().get(0).getExposures().getInternal().toString().contains(testUser1)==false) {break;}
		}
		assertFalse(exposureDoc2.getObjects().get(0).getExposures().getInternal().toString().contains(testUser1),"User is found in exposure internal user list: "+testUser1);
		assertTrue(exposureDoc2.getObjects().get(0).getExposures().getInternal().toString().contains(testUser2),"User not found in exposure internal user list: "+testUser2);
//		assertTrue(exposureDoc1.getObjects().get(0).getExposures().getInternal().toString().contains(groupMailId),"User not found in exposure internal user list: "+groupMailId);
		
		//Check the user and collaborator list
		usersDoc1 = getCollaborators(elapp.el_google_apps.name(), qparamsForFile2);
		myResponse = MarshallingUtils.marshall(usersDoc1);
		assertFalse(myResponse.contains(testUser1),"User email is found in collaborators list:"+testUser1 +".");		
		assertTrue(myResponse.contains(testUser2),"User email not found in collaborators list:"+testUser2 +".");		
//		assertTrue(myResponse.contains(groupMailId),"User email not found in collaborators list:"+groupMailId +".");		
		usersDoc1 = getExposedUsers(elapp.el_google_apps.name(), qparamsForFile2);
		myResponse = MarshallingUtils.marshall(usersDoc1);
		assertTrue(myResponse.contains(externalUser2),"User email not found in owner list:"+externalUser2 +".");
		
		
		Reporter.log("----------------Delete the mail from inbox of "+testUser2+" --------------------",true );
		message=objMailTestUser2.getLatestMail(myMailSubject);
		threadId=message.getThreadId();
		success = objMailTestUser2.trashMessage(threadId);
		Assert.assertTrue(success, "Mail deletion failed.");
		
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
				emailObj = objMailTestUser2.getMessageWithSubject(myMailSubject, "INBOX");
			}
			
			//set remediationSuccess as true if mail or attachment deleted based on remediation type
//			if (remedialAction.equals(Remediation.ITEM_DELETE_MAIL_BY_ATTACHMENT.name()) || remedialAction.equals(Remediation.ITEM_TRASH_MAIL.name())) {
			if ( remedialAction.equals(Remediation.ITEM_TRASH_MAIL.name())) {
				
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
		for (; i <= (MAX_EXPOSURE_WAIT_TIME_AFTER_REMEDIATION * 60 * 1000); i+=60000 ) {
			Reporter.log("Wait for "+TimeUnit.MILLISECONDS.toMinutes(i)+" minutes, and check if the file:"+myMailSubject +" is removed from exposure.",true);
			Reporter.log("------------------------------------------------------------------------",true );
			Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
			mailExpoDoc = this.getExposedDocuments(elapp.el_google_apps.name(), qparamsForMail);
			countAfterRemediation = mailExpoDoc.getMeta().getTotalCount();
			
			if (countAfterRemediation == 0) {
				break;
			}
			
		}
		
		
		Reporter.log("###### exposure count after mail deletion::"+countAfterRemediation, true);
		assertEquals(countAfterRemediation, 0, "Exposure count has not reduced after mail deletion from both users inbox..");
		//Check the user and collaborator list
		usersDoc1 = getCollaborators(elapp.el_google_apps.name(), qparamsForMail);
		assertEquals(usersDoc1.getMeta().getTotalCount(), 0, "Collaborator list is still not empty after deletion of email.");			
		usersDoc2 = getExposedUsers(elapp.el_google_apps.name(), qparamsForMail);
		assertEquals(usersDoc2.getMeta().getTotalCount(), 0,"Owner list is still not empty after deletion of email.");
		
		for (; i <= (MAX_EXPOSURE_WAIT_TIME_AFTER_REMEDIATION * 60 * 1000); i+=60000 ) {
			Reporter.log("Wait for "+TimeUnit.MILLISECONDS.toMinutes(i)+" minutes, and check if the file:"+searchFileName1 +" is removed from exposure.",true);
			Reporter.log("------------------------------------------------------------------------",true );
			Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
			exposureDoc1 = this.getExposedDocuments(elapp.el_google_apps.name(), qparamsForFile1);
			countAfterRemediation = exposureDoc1.getMeta().getTotalCount();
			
			if (countAfterRemediation == 0) {
				break;
			}
			
		}
		Reporter.log("###### exposure count after mail deletion::"+countAfterRemediation, true);
		assertEquals(countAfterRemediation, 0, "Exposure count has not reduced after mail deletion from both users inbox..");
		//Check the user and collaborator list
		usersDoc1 = getCollaborators(elapp.el_google_apps.name(), qparamsForFile1);
		assertEquals(usersDoc1.getMeta().getTotalCount(), 0,"Collaborator list is still not empty after deletion of email.");		
		usersDoc2 = getExposedUsers(elapp.el_google_apps.name(), qparamsForFile1);
		assertEquals(usersDoc2.getMeta().getTotalCount(), 0,"Owner list is still not empty after deletion of email.");
		
		for (; i <= (MAX_EXPOSURE_WAIT_TIME_AFTER_REMEDIATION * 60 * 1000); i+=60000 ) {
			Reporter.log("Wait for "+TimeUnit.MILLISECONDS.toMinutes(i)+" minutes, and check if the file:"+searchFileName2 +" is removed from exposure.",true);
			Reporter.log("------------------------------------------------------------------------",true );
			Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
			exposureDoc2 = this.getExposedDocuments(elapp.el_google_apps.name(), qparamsForFile2);
			countAfterRemediation = exposureDoc2.getMeta().getTotalCount();
			
			if (countAfterRemediation == 0) {
				break;
			}
			
		}
		
		Reporter.log("###### exposure count after mail deletion::"+countAfterRemediation, true);
		assertEquals(countAfterRemediation, 0, "Exposure count has not reduced after mail deletion from both users inbox..");
		//Check the user and collaborator list
		usersDoc1 = getCollaborators(elapp.el_google_apps.name(), qparamsForFile2);
		assertEquals(usersDoc1.getMeta().getTotalCount(), 0, "Collaborator list is still not empty after deletion of email.");		
		usersDoc2 = getExposedUsers(elapp.el_google_apps.name(), qparamsForFile2);
		assertEquals(usersDoc2.getMeta().getTotalCount(), 0,"Owner list is still not empty after deletion of email.");
		
		
		
	}
	
	/**	
	 * When a risky doc saved in draft , verify it appears in riksy docs, and when it is sent to external user verify that files are moved from riskydocs to exposure 
	 * @throws Exception
	 */
	@Test(groups={"P1"},retryAnalyzer=RetryAnalyzer.class)
	public void verifyRiskyDocsAndExposureWhenDraftSavedWithAttachmentAndSentLater(
			) throws Exception {
		
		Reporter.log("****************Test Case Description****************",true);
		Reporter.log("Test Case Description: Verify the 'Exposure', 'Other Risk'  when an email with risk in body and attachment is saved first in Drafts and sent later to an external user",true);
		Reporter.log("1)Create a mail with risk in body and attachment and save in Drafts",true);
		Reporter.log("2)Verify that  both body file and attachment file appears in Other risks files",true);
		Reporter.log("2)Now send the mail which was in draft",true);
		Reporter.log("3)Verify that the mail file and attachment file appears in exposure tab and removed from other tisks files",true);
		Reporter.log("*****************************************************",true);
		
		
		String isInternal = "true";
		Message emailObj = null;
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
		String mailBody = readFile(sourceFile2.toString());
		boolean success =false;
		String docTypeForRemediation = null;
		int countAfterExposure =0;
		int responseCode = 0;
		String expectedLog = null;
		List<String> to	= new ArrayList<>();
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
		String remedialAction ="ITEM_TRASH_MAIL";
		//Prepare the violations
		String docId=null;
		MimeMessage msg ;
		Draft msgDraft;
		
		Reporter.log("------------------------------------------------------------------------",true );
		Reporter.log("internal user creates  mail with external user as recipient",true);
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
		
		List<String> recipientsCc = new ArrayList<String>();
		List<String> recipientsTo = new ArrayList<String>();
		
		recipientsTo.add(externalUser1);
		recipientsCc.add(testUser1);
		
		
		synchronized(this){ 
			msg = objMailAdmin.createEmailWithMultipleAttachment(recipientsTo, recipientsCc, null, myMailSubject, mailBody,myAttachments);
			 msgDraft = objMailAdmin.createDraft(msg);
			
		}
		
		Assert.assertNotNull(msgDraft, "Unable to save mail in  Draft.");
		
		
		
		cleanupListSent.add(myMailSubject);
//		assertTrue(success, "Failed sending mail with subject:"+myMailSubject+".");
//		success=false;
		
		
		Reporter.log("------------------------------------------------------------------------",true );
		
		int i = 60000;
		for (; i <= (MAX_EXPOSURE_WAIT_TIME_IN_MINUTES * 60 * 1000); i+=60000 ) {
			Reporter.log("Checking exposure after :"+TimeUnit.MILLISECONDS.toMinutes(i)+" minute(s)",true );
			Reporter.log("------------------------------------------------------------------------",true );
			Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
			
			
			//Get the exposure count
			mailExpoDoc = this.getRiskyDocuments(elapp.el_google_apps.name(), qparamsForMail);
			countAfterExposure = mailExpoDoc.getMeta().getTotalCount();
			Reporter.log("Exposure Count ="+countAfterExposure, true);
			
			if (countAfterExposure >= 1) {break;}
		}
		assertEquals(countAfterExposure,1,"File:"+myMailSubject +" not appearing in risky docs even after waiting for "+TimeUnit.MILLISECONDS.toMinutes(i-60000)+" minutes.");
//		assertTrue(mailExpoDoc.getObjects().get(0).getExposures().getInternal().toString().contains(testUser1),"User not found in risky docs internal user list: "+testUser1);
//		assertTrue(mailExpoDoc.getObjects().get(0).getExposures().getInternal().toString().contains(testUser2),"User not found in risky docs internal user list: "+testUser2);
		 
		
		for (; i <= (MAX_EXPOSURE_WAIT_TIME_IN_MINUTES * 60 * 1000); i+=60000 ) {
			Reporter.log("Checking exposure after :"+TimeUnit.MILLISECONDS.toMinutes(i)+" minute(s)",true );
			Reporter.log("------------------------------------------------------------------------",true );
			Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
			
			
			//Get the exposure count
			exposureDoc1 = this.getRiskyDocuments(elapp.el_google_apps.name(), qparamsForFile1);
			countAfterExposure = exposureDoc1.getMeta().getTotalCount();
			Reporter.log("Exposure Count ="+countAfterExposure, true);
			
			if (countAfterExposure >= 1) {break;}
		}
		assertEquals(countAfterExposure,1,"File:"+searchFileName1 +" not appearing in risky docs even after waiting for "+TimeUnit.MILLISECONDS.toMinutes(i-60000)+" minutes.");
//		assertTrue(exposureDoc1.getObjects().get(0).getExposures().getInternal().toString().contains(testUser1),"User not found in risky docs internal user list: "+testUser1);
//		assertTrue(exposureDoc1.getObjects().get(0).getExposures().getInternal().toString().contains(testUser2),"User not found in risky docs internal user list: "+testUser2);
//		
		Reporter.log("------------------------------------------------------------------------",true );
		
		for (; i <= (MAX_EXPOSURE_WAIT_TIME_IN_MINUTES * 60 * 1000); i+=60000 ) {
			Reporter.log("Checking exposure after :"+TimeUnit.MILLISECONDS.toMinutes(i)+" minute(s)",true );
			Reporter.log("------------------------------------------------------------------------",true );
			Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
			
			
			//Get the exposure count
			exposureDoc2 = this.getRiskyDocuments(elapp.el_google_apps.name(), qparamsForFile2);
			countAfterExposure = exposureDoc2.getMeta().getTotalCount();
			Reporter.log("Exposure Count ="+countAfterExposure, true);
			
			if (countAfterExposure >= 1) {break;}
		}
		assertEquals(countAfterExposure,1,"File:"+searchFileName2 +" not appearing in risky docs even after waiting for "+TimeUnit.MILLISECONDS.toMinutes(i-60000)+" minutes.");
//		assertTrue(exposureDoc1.getObjects().get(0).getExposures().getInternal().toString().contains(testUser1),"User not found in risky docs internal user list: "+testUser1);
//		assertTrue(exposureDoc1.getObjects().get(0).getExposures().getInternal().toString().contains(testUser2),"User not found in risky docs internal user list: "+testUser2);
		
		
		Reporter.log("----------------Send Mail from Draft --------------------",true );
		success = objMailAdmin.sendDraft(msgDraft,myMailSubject);
		Assert.assertTrue(success, "Unable to send mail from Draft.");
		
		
		
		 int countAfterRemediation = countAfterExposure ;
		i = 60000;
		for (; i <= (MAX_EXPOSURE_WAIT_TIME_AFTER_REMEDIATION * 60 * 1000); i+=60000 ) {
			Reporter.log("Wait for "+TimeUnit.MILLISECONDS.toMinutes(i)+" minutes, and check if the file:"+myMailSubject +" is removed from risky_doc.",true);
			Reporter.log("------------------------------------------------------------------------",true );
			Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
			mailExpoDoc = this.getRiskyDocuments(elapp.el_google_apps.name(), qparamsForMail);
			countAfterRemediation = mailExpoDoc.getMeta().getTotalCount();
			
			if (countAfterRemediation == 0) {
				break;
			}
			
		}
		
		
		Reporter.log("###### exposure count after mail sent from draft::"+countAfterRemediation, true);
		assertEquals(countAfterRemediation, 0, "risky_doc count has not reduced after mail sent from draft.");
		
		for (; i <= (MAX_EXPOSURE_WAIT_TIME_AFTER_REMEDIATION * 60 * 1000); i+=60000 ) {
			Reporter.log("Wait for "+TimeUnit.MILLISECONDS.toMinutes(i)+" minutes, and check if the file:"+searchFileName1 +" is removed from risky_doc.",true);
			Reporter.log("------------------------------------------------------------------------",true );
			Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
			exposureDoc1 = this.getRiskyDocuments(elapp.el_google_apps.name(), qparamsForFile1);
			countAfterRemediation = exposureDoc1.getMeta().getTotalCount();
			
			if (countAfterRemediation == 0) {
				break;
			}
			
		}
		Reporter.log("###### exposure count after mail sent from draft::"+countAfterRemediation, true);
		assertEquals(countAfterRemediation, 0, "risky_doc count has not reduced after mail sent from draft.");
		
		for (; i <= (MAX_EXPOSURE_WAIT_TIME_AFTER_REMEDIATION * 60 * 1000); i+=60000 ) {
			Reporter.log("Wait for "+TimeUnit.MILLISECONDS.toMinutes(i)+" minutes, and check if the file:"+searchFileName2 +" is removed from risky_doc.",true);
			Reporter.log("------------------------------------------------------------------------",true );
			Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
			exposureDoc2 = this.getRiskyDocuments(elapp.el_google_apps.name(), qparamsForFile2);
			countAfterRemediation = exposureDoc2.getMeta().getTotalCount();
			
			if (countAfterRemediation == 0) {
				break;
			}
			
		}
		
		Reporter.log("###### exposure count after mail sent from draft::"+countAfterRemediation, true);
		assertEquals(countAfterRemediation, 0, "risky_doc count has not reduced after mail sent from draft.");
		
		
		 i = 60000;
		for (; i <= (MAX_EXPOSURE_WAIT_TIME_IN_MINUTES * 60 * 1000); i+=60000 ) {
			Reporter.log("Checking exposure after :"+TimeUnit.MILLISECONDS.toMinutes(i)+" minute(s)",true );
			Reporter.log("------------------------------------------------------------------------",true );
			Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
			
			
			//Get the exposure count
			mailExpoDoc = this.getExposedDocuments(elapp.el_google_apps.name(), qparamsForMail);
			countAfterExposure = mailExpoDoc.getMeta().getTotalCount();
			Reporter.log("Exposure Count ="+countAfterExposure, true);
			
			if (countAfterExposure >= 1) {break;}
		}
		assertEquals(countAfterExposure,1,"File:"+myMailSubject +" not appearing in exposed docs even after waiting for "+TimeUnit.MILLISECONDS.toMinutes(i-60000)+" minutes.");
		assertTrue(mailExpoDoc.getObjects().get(0).getExposures().getInternal().toString().contains(testUser1),"User not found in exposed docs internal user list: "+testUser1);
		assertTrue(mailExpoDoc.getObjects().get(0).getExposures().getExternal().toString().contains(externalUser1),"User not found in exposed docs external user list: "+externalUser1);
		 
		
		for (; i <= (MAX_EXPOSURE_WAIT_TIME_IN_MINUTES * 60 * 1000); i+=60000 ) {
			Reporter.log("Checking exposure after :"+TimeUnit.MILLISECONDS.toMinutes(i)+" minute(s)",true );
			Reporter.log("------------------------------------------------------------------------",true );
			Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
			
			
			//Get the exposure count
			exposureDoc1 = this.getExposedDocuments(elapp.el_google_apps.name(), qparamsForFile1);
			countAfterExposure = exposureDoc1.getMeta().getTotalCount();
			Reporter.log("Exposure Count ="+countAfterExposure, true);
			
			if (countAfterExposure >= 1) {break;}
		}
		assertEquals(countAfterExposure,1,"File:"+searchFileName1 +" not appearing in exposed docs even after waiting for "+TimeUnit.MILLISECONDS.toMinutes(i-60000)+" minutes.");
		assertTrue(exposureDoc1.getObjects().get(0).getExposures().getInternal().toString().contains(testUser1),"User not found in exposed docs internal user list: "+testUser1);
		assertTrue(exposureDoc1.getObjects().get(0).getExposures().getExternal().toString().contains(externalUser1),"User not found in exposed docs external user list: "+externalUser1);
//		
		Reporter.log("------------------------------------------------------------------------",true );
		
		for (; i <= (MAX_EXPOSURE_WAIT_TIME_IN_MINUTES * 60 * 1000); i+=60000 ) {
			Reporter.log("Checking exposure after :"+TimeUnit.MILLISECONDS.toMinutes(i)+" minute(s)",true );
			Reporter.log("------------------------------------------------------------------------",true );
			Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
			
			
			//Get the exposure count
			exposureDoc2 = this.getExposedDocuments(elapp.el_google_apps.name(), qparamsForFile2);
			countAfterExposure = exposureDoc2.getMeta().getTotalCount();
			Reporter.log("Exposure Count ="+countAfterExposure, true);
			
			if (countAfterExposure >= 1) {break;}
		}
		assertEquals(countAfterExposure,1,"File:"+searchFileName2 +" not appearing in exposed docs even after waiting for "+TimeUnit.MILLISECONDS.toMinutes(i-60000)+" minutes.");
		assertTrue(exposureDoc2.getObjects().get(0).getExposures().getInternal().toString().contains(testUser1),"User not found in exposed docs internal user list: "+testUser1);
		assertTrue(exposureDoc2.getObjects().get(0).getExposures().getExternal().toString().contains(externalUser1),"User not found in exposed docs external user list: "+externalUser1);
		
		
		
		
	}
	
	

//	@Test
	public void query1() throws Exception {

		String myMailSubject="56220632-007c-4579-9bea-5223d87f11efExposureInMailBody";
		boolean success = objMailTestUser2.trashMailFromLabel(myMailSubject,"INBOX");
		assertTrue(success, "Failed deleting mail with subject:"+myMailSubject+".");
		
//		Message emailObj = objMailTestUser1.getMessageWithSubject("Securlet status testing 07038209-23bc-4234-92b0-d4f3e2eb66f5", "INBOX");
//		Message emailObj = objMailTestUser1.getMessageWithSubject("NissarSubject", "INBOX");
//		System.out.println("nissar"+emailObj.getId());
//		 emailObj = objMailTestUser1.getMessageWithSubject("NissarSubject", "DRAFT");
//		System.out.println("nissar"+emailObj.getRaw());
		
//		String searchResponse = "{\"meta\": {\"limit\": 20,\"next\": null,\"offset\": 0,\"previous\": null,\"total_count\": 1},\"objects\": [{\"_domain\": \"securletbeatle.com\",\"activity_count\": 6,\"changeset_id\": null,\"content_checks\": {\"content_iq_violations\": null,\"dlp\": null,\"doc_class\": [],\"encryption\": null,\"ferpa\": null,\"glba\": null,\"hipaa\": null,\"pci\": null,\"pii\": {\"expressions\": [{\"name\": \"Personally Identifiable Information\",\"values\": [{\"key\": \"Person's Email Address, Date of Birth\",\"value\": 1},{\"key\": \"Person's Email Address, Person Name, Date of Birth\",\"value\": 1}]}],\"updated_timestamp\": \"1451535109473\"},\"vba_macros\": {},\"violations\": true,\"virus\": {},\"vk_content_iq_violations\": [],\"vk_dlp\": 0,\"vk_encryption\": 0,\"vk_ferpa\": 0,\"vk_glba\": 0,\"vk_hipaa\": 0,\"vk_pci\": 0,\"vk_pii\": 1,\"vk_source_code\": 0,\"vk_vba_macros\": 0,\"vk_virus\": 0},\"created_by\": \"testuser1@securletbeatle.com\",\"doc_type\": \"Email_File_Attachment\",\"exp_state\": 4,\"exposed\": true,\"exposures\": {\"all_internal\": false,\"ext_count\": 2,\"external\": [\"user1@gatewaybeatle.com\",\"admin@protecto365autobeatle.com\"],\"int_count\": 2,\"internal\": [\"testuser2@securletbeatle.com\",\"admin@securletbeatle.com\"],\"public\": false},\"format\": \"rtf\",\"identification\": \"8f9f61bde574eee6a08c445aaf424183fdd489939fd0c624f157082c2d34a8b8\",\"is_internal\": true,\"md5\": null,\"name\": \"509bd706-4d24-43e8-8bda-8ccda6c856a7PII.rtf\",\"object_type\": \"Mail\",\"owned_by\": \"testuser1@securletbeatle.com\",\"owner_id\": null,\"parent_id\": \"2c5f06f8cc49ad75142b6c32df539388311791880cd51462da349468729408c2\",\"path\": \"multiple\",\"scan_state\": 0,\"scan_ts\": 1451535109,\"shared\": true,\"size\": 455,\"url\": null}]}";
//		
//		
//		Map<String, Object> expectedFieldsForReceived =  new HashMap();
//		
//		String[] tempName =new  String[2];
//		
//		
//		
//		expectedFieldsForReceived.put("domain", "securletbeatle.com");
//		expectedFieldsForReceived.put("activity_count", "NOT_EMPTY");
//		expectedFieldsForReceived.put("changeset_id", null);
//		expectedFieldsForReceived.put("created_by", testUser1);
//		expectedFieldsForReceived.put("doc_type", "Email_File_Attachment");
//		expectedFieldsForReceived.put("exp_state", "NOT_EMPTY");
//		expectedFieldsForReceived.put("exposed", true);
//		expectedFieldsForReceived.put("format", "rtf");
//		expectedFieldsForReceived.put("identification", "NOT_EMPTY");
//		expectedFieldsForReceived.put("is_internal", true);
//		expectedFieldsForReceived.put("md5", null);
//		expectedFieldsForReceived.put("object_type", "Mail");
//		expectedFieldsForReceived.put("owned_by", testUser1);
//		expectedFieldsForReceived.put("owner_id", null);
//		expectedFieldsForReceived.put("parent_id", "NOT_EMPTY");
//		expectedFieldsForReceived.put("path", "NOT_EMPTY");
//		expectedFieldsForReceived.put("scan_state", 0);
//		expectedFieldsForReceived.put("scan_ts", "NOT_EMPTY");
//		expectedFieldsForReceived.put("shared", true);
//		expectedFieldsForReceived.put("size", 455);
//		expectedFieldsForReceived.put("url", null);
//		
//		Assert softAssert = null;
//		boolean result =RawJsonParser.findExpectedKeysAndValues(searchResponse, "$.objects.[*]", expectedFieldsForReceived,"size");
//		softAssert.assertTrue(result, "Expected field combination not found in Received activity log.");

		
	}


}
