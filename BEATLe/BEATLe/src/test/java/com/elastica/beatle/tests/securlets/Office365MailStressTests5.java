package com.elastica.beatle.tests.securlets;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
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
import org.testng.SkipException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

import com.elastica.beatle.CommonTest;
import com.elastica.beatle.DateUtils;
import com.elastica.beatle.MarshallingUtils;
import com.elastica.beatle.Authorization.AuthorizationHandler;
import com.elastica.beatle.RestClient.ClientUtil;
import com.elastica.beatle.ciq.dto.ContentChecks;
import com.elastica.beatle.ciq.dto.ESResults;
import com.elastica.beatle.logger.Logger;
import com.elastica.beatle.securlets.CIQValidator;
import com.elastica.beatle.securlets.ESQueryBuilder;
import com.elastica.beatle.securlets.LogValidator;
import com.elastica.beatle.securlets.dto.ForensicSearchResults;
import com.elastica.beatle.securlets.dto.Hit;
import com.elastica.beatle.securlets.dto.MailAction;
import com.elastica.beatle.securlets.dto.O365Document;
import com.elastica.beatle.securlets.dto.MailRemediation;
import com.elastica.beatle.securlets.dto.Source;
import com.gargoylesoftware.htmlunit.util.UrlUtils;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.universal.common.Office365MailActivities;
import com.universal.constants.CommonConstants;

import microsoft.exchange.webservices.data.core.enumeration.service.DeleteMode;
import microsoft.exchange.webservices.data.core.exception.service.local.ServiceLocalException;
import microsoft.exchange.webservices.data.core.service.item.EmailMessage;
import microsoft.exchange.webservices.data.property.complex.EmailAddressCollection;

import org.testng.asserts.SoftAssert;

import com.elastica.beatle.tests.securlets.SecurletsUtils;

import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;





/**
 * @author Nissar
 *
 */
public class Office365MailStressTests5 extends CommonTest {
	ESQueryBuilder esQueryBuilder = null;
	LogValidator logValidator;
	CIQValidator ciqValidator;
	Office365MailActivities objMailAdmin;
	Office365MailActivities objMailTestUser1;
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
	private String adminMailId;
	private String externalRecepient;
	private String testUser1;
	private String testUser2;
	private String testUser1Pwd;
	private String testUser2Pwd;
	private String externalUser;
	private String externalUserPwd;

	// Unique Id used across testcases
	String uniqueId1;
	String uniqueId2;
	String uniqueId3;
	String uniqueId4;
	String uniqueId5;
	String uniqueId6;
	String userDir ;

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
	public static final long MAX_EXPOSURE_WAIT_TIME_IN_MINUTES		= 10;
	public static final long MAX_REMEDIATION_WAIT_TIME_IN_MINUTES	= 10;


	public Office365MailStressTests5() throws Exception {
		super();
		//Generating Unique Id to use across test cases
		uniqueId1 = UUID.randomUUID().toString();
		uniqueId2 = UUID.randomUUID().toString();
		uniqueId3 = UUID.randomUUID().toString();
		uniqueId4 = UUID.randomUUID().toString();
		uniqueId5 = UUID.randomUUID().toString();
		uniqueId6 = UUID.randomUUID().toString();
		
		esQueryBuilder = new ESQueryBuilder();
		logValidator = new LogValidator();
		
		//super.initSuiteConfigurations(suiteConfigurations, suiteData);
		
		//setting upload file path
		userDir = System.getProperty("user.dir")+"/src/test/resources/uploads/office365/";
		
		
	}
	
	@BeforeClass(alwaysRun=true)
	public void setup() throws Exception {
	
			//User Credentials from suite file
//			userName =  suiteData.getSaasAppUsername();		
//			userPwd = suiteData.getSaasAppPassword();		
//			adminMailId = userName;
//			
//			testUser1 = suiteData.getSaasAppEndUser1Name();
//			testUser1Pwd = suiteData.getSaasAppEndUser1Password();
//			
//			testUser2 = suiteData.getSaasAppEndUser2Name();
//			testUser2Pwd = suiteData.getSaasAppEndUser2Password();
//			
//			externalRecepient =  suiteData.getSaasAppExternalUser();
//			
//			//creating mail object with admin user credentials
//			objMailAdmin = new Office365MailActivities(userName,userPwd); 
//			
//			externalUser="qa-admin@elasticaqa.com";
//			externalUserPwd="6guK$IgNo2tE";
//			
//			//creating mail object with testuser1 credentials
//			objMailTestUser1 = new Office365MailActivities(testUser1,testUser1Pwd); 
//			objMailExternalUser = new Office365MailActivities(externalUser,externalUserPwd); 
	}
	
	@AfterClass(alwaysRun=true)
	public void cleanup() throws Exception {

			Reporter.log("Inside cleanup",true);

//			deleteMails(cleanupListSent,"SentItems",testUser1,testUser1Pwd);
//			deleteMails(cleanupListReceived,"Inbox",testUser1,testUser1Pwd);
//			deleteMails(cleanupListDrafts,"Drafts",testUser1,testUser1Pwd);
//			deleteMails(cleanupListSent,"Inbox",testUser2,testUser2Pwd);
			
			deleteFiles(filesToDelete);
			
	}
	
	@DataProvider(parallel = true)
	public Object[][] dataProviderEmptyFolders() {
		
		return new Object[][]{
			//user_starting_range,   user_ending_range	empty_folder_name
//			{	1 ,					100,					"SentItems"},
//			{ 2501,					2600,					"Inbox"},
//			{ 1,					25,					"Drafts"},
//			{ 5001,					5100,				"Inbox"},
//			{ 10000,				10000,				"SentItems"},
//			{ 2,						2,						"Drafts"},
//			{ 2,						2,						"SentItems"}
		};
	}
	
	
	@Test( dataProvider="dataProviderEmptyFolders")
	public void emptyFolder(int userStartingRange, int userEndingRange, String folderName) throws IOException, InterruptedException {
		
		
		String userPrefix ="qa-stress";
		String userSuffix ="@o365security.net";
		String userName;
		String userPassword = "Aut0mat10n#123";
		String identifier = null;
		boolean success = false;
		//generating unique id
		Date curDate = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy_MM_dd");
		String DateString = format.format(curDate);	
		String LogFile=DateString+"emptyFolderResult.txt";
		String result;
		
		
		for(int currUserNumber=userStartingRange;currUserNumber<=userEndingRange; currUserNumber++ ){ // for total number of users
			
			//forming the user email id
			userName = userPrefix+Integer.toString(currUserNumber)+userSuffix;
			Reporter.log("==============================================",true);
			Reporter.log("Started Processing for user:"+userName,true);
			Reporter.log("==============================================",true);
			
			//creating mail object with admin user credentials
			Office365MailActivities objMail = new Office365MailActivities(userName,userPassword); 
			
				try {
					//forming identifier 
					identifier = userPrefix.replace("-", "_")+Integer.toString(currUserNumber)+"_";
					Reporter.log("Identifier="+identifier,true);
					
					
//					Thread.sleep(10000);
					//cleanupListSent.add(mailSubject);
					success = objMail.emptyFolder(folderName);
					
					if(success){
						result="Passed";
					}
					else{
						result="Failed";
					}
					success=false;
					Reporter.log(userName+ " "+ folderName + " "+ result,true);
					writeToFile(LogFile,userName+ " "+ folderName + " "+ result);
					//assertTrue(success, "Failed sending mail with subject:"+mailSubject);
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					Reporter.log(e.getMessage());
					e.printStackTrace();
				}
		}
		
	}
	
	public EmailAddressCollection getInternalRecepientList(int userStartingRange, int userEndingRange)
	{
		String userPrefix ="qa-stress";
		String userSuffix ="@o365security.net";
		String userEmail;
		EmailAddressCollection mailIds = new EmailAddressCollection();
		
		for(int currUserNumber=userStartingRange;currUserNumber<=userEndingRange; currUserNumber++ ){ // for total number of users
			
			//forming the sender email id
			userEmail = userPrefix+Integer.toString(currUserNumber)+userSuffix;
			Reporter.log(userEmail,true);
			mailIds.add(userEmail);
		}
		
		
		return mailIds;
		
	}
	public EmailAddressCollection getExternalRecepientList(int userStartingRange, int userEndingRange)
	{
		String userPrefix ="mitthan.meena+";
		String userSuffix ="@elasticaqa.net";
		String userEmail;
		EmailAddressCollection mailIds = new EmailAddressCollection();
		
		for(int currUserNumber=userStartingRange;currUserNumber<=userEndingRange; currUserNumber++ ){ // for total number of users
			
			//forming the sender email id
			userEmail = userPrefix+Integer.toString(currUserNumber)+userSuffix;
			Reporter.log(userEmail,true);
			mailIds.add(userEmail);
		}
		
		
		return mailIds;
		
	}
	
	@SuppressWarnings("null")
	@DataProvider(parallel = true)
	public Object[][] dataProviderMailDetails() {
		
		ArrayList<String> mailAttachments = new ArrayList<String>() ;
		EmailAddressCollection interalIds = new EmailAddressCollection();
		EmailAddressCollection externalIds = new EmailAddressCollection();
		EmailAddressCollection groupIds =new EmailAddressCollection();;		
		
		//getting internal recipient list
//		interalIds = getInternalRecepientList(1, 25);
		
		//getting external recipient list
		externalIds= getExternalRecepientList(1, 2);
//		externalIds.add("abdul.nissar@elastica.co");
		
		//adding distribution recipient list
		groupIds.add("DistGroup5001-5100@o365security.net");
		
		
//		toEmailIds.add("qa-stress2@o365security.net"); //
//		toEmailIds.add("abdul.nissar@elastica.co");
//		ccEmailIds.add("wrong@elastica@co");
		
		mailAttachments.add("PII.rtf");
		mailAttachments.add("PCI_Test.txt");
//		mailAttachments.add("ferpa.pdf");
		mailAttachments.add("hipaa.txt");
//		mailAttachments.add("Hello.java");
//		mailAttachments.add("encryption.bin");
		int range1 = 7000;
		int range2 = 8000;
		int sender1 =9920;
		int sender2 =9910;
		
		return new Object[][]{
			//mail_action, 		identifier_keyword,	user_starting_range,   	user_ending_range,	contentLocation,	attachmentList, 		number_of_mails_per_sender, 	to_email_list,	cc_email_list,	bcc_email_list
			
			{"send_mail",		"internal", 		sender1+1,						sender1+1,				"attachment",		mailAttachments, 		25,						   	getInternalRecepientList(range1+1, range1+200), 	null, 			null},
			{"send_mail",		"external", 		range1+1,						range1+200,				"attachment",		mailAttachments, 		25,						  	 externalIds, 	null, 			null},
			{"send_mail",		"group", 			range1+1,						range1+200,				"attachment",		mailAttachments, 		25,						  	 groupIds, 	null, 			null},
			{"save_indrafts",	"draft", 			range1+1,						range1+200,				"attachment",		mailAttachments, 		25,						  	 null, 	null, 			null},
			
			{"send_mail",		"internal", 		sender1+2,						sender1+2,				"attachment",		mailAttachments, 		25,						   	getInternalRecepientList(range1+201, range1+400), 	null, 			null},
			{"send_mail",		"external", 		range1+201,						range1+400,				"attachment",		mailAttachments, 		25,						  	 externalIds, 	null, 			null},
			{"send_mail",		"group", 			range1+201,						range1+400,				"attachment",		mailAttachments, 		25,						  	 groupIds, 	null, 			null},
			{"save_indrafts",	"draft", 			range1+201,						range1+400,				"attachment",		mailAttachments, 		25,						  	 null, 	null, 			null},
			
			{"send_mail",		"internal", 		sender1+3,						sender1+3,				"attachment",		mailAttachments, 		25,						   	getInternalRecepientList(range1+401,range1+600), 	null, 			null},
			{"send_mail",		"external", 		range1+401,						range1+600,				"attachment",		mailAttachments, 		25,						  	 externalIds, 	null, 			null},
			{"send_mail",		"group", 			range1+401,						range1+600,				"attachment",		mailAttachments, 		25,						  	 groupIds, 	null, 			null},
			{"save_indrafts",	"draft", 			range1+401,						range1+600,				"attachment",		mailAttachments, 		25,						  	 null, 	null, 			null},
			
			{"send_mail",		"internal", 		sender1+4,						sender1+4,				"attachment",		mailAttachments, 		25,						   	getInternalRecepientList(range1+601, range1+800), 	null, 			null},
			{"send_mail",		"external", 		range1+601,						range1+800,				"attachment",		mailAttachments, 		25,						  	 externalIds, 	null, 			null},
			{"send_mail",		"group", 			range1+601,						range1+800,				"attachment",		mailAttachments, 		25,						  	 groupIds, 	null, 			null},
			{"save_indrafts",	"draft", 			range1+601,						range1+800,				"attachment",		mailAttachments, 		25,						  	 null, 	null, 			null},
			
			{"send_mail",		"internal", 		sender1+5,						sender1+5,				"attachment",		mailAttachments, 		25,						   	getInternalRecepientList(range1+801, range1+1000), 	null, 			null},
			{"send_mail",		"external", 		range1+801,						range1+1000,			"attachment",		mailAttachments, 		25,						  	 externalIds, 	null, 			null},
			{"send_mail",		"group", 			range1+801,						range1+1000,			"attachment",		mailAttachments, 		25,						  	 groupIds, 	null, 			null},
			{"save_indrafts",	"draft", 			range1+801,						range1+1000,			"attachment",		mailAttachments, 		25,						  	 null, 	null, 			null},
			
			
			{"send_mail",		"internal", 		sender1+6,						sender1+6,				"attachment",		mailAttachments, 		25,						   	getInternalRecepientList(range2+1, range2+200), 	null, 			null},
			{"send_mail",		"external", 		range2+1,						range2+200,				"attachment",		mailAttachments, 		25,						  	 externalIds, 	null, 			null},
			{"send_mail",		"group", 			range2+1,						range2+200,				"attachment",		mailAttachments, 		25,						  	 groupIds, 	null, 			null},
			{"save_indrafts",	"draft", 			range2+1,						range2+200,				"attachment",		mailAttachments, 		25,						  	 null, 	null, 			null},
		
			{"send_mail",		"internal", 		sender1+7,						sender1+7,				"attachment",		mailAttachments, 		25,						   	getInternalRecepientList(range2+201, range2+400), 	null, 			null},
			{"send_mail",		"external", 		range2+201,						range2+400,				"attachment",		mailAttachments, 		25,						  	 externalIds, 	null, 			null},
			{"send_mail",		"group", 			range2+201,						range2+400,				"attachment",		mailAttachments, 		25,						  	 groupIds, 	null, 			null},
			{"save_indrafts",	"draft", 			range2+201,						range2+400,				"attachment",		mailAttachments, 		25,						  	 null, 	null, 			null},
			
			{"send_mail",		"internal", 		sender1+8,						sender1+8,				"attachment",		mailAttachments, 		25,						   	getInternalRecepientList(range2+401,range2+600), 	null, 			null},
			{"send_mail",		"external", 		range2+401,						range2+600,				"attachment",		mailAttachments, 		25,						  	 externalIds, 	null, 			null},
			{"send_mail",		"group", 			range2+401,						range2+600,				"attachment",		mailAttachments, 		25,						  	 groupIds, 	null, 			null},
			{"save_indrafts",	"draft", 			range2+401,						range2+600,				"attachment",		mailAttachments, 		25,						  	 null, 	null, 			null},
			
			{"send_mail",		"internal", 		sender1+9,						sender1+9,				"attachment",		mailAttachments, 		25,						   	getInternalRecepientList(range2+601, range2+800), 	null, 			null},
			{"send_mail",		"external", 		range2+601,						range2+800,				"attachment",		mailAttachments, 		25,						  	 externalIds, 	null, 			null},
			{"send_mail",		"group", 			range2+601,						range2+800,				"attachment",		mailAttachments, 		25,						  	 groupIds, 	null, 			null},
			{"save_indrafts",	"draft", 			range2+601,						range2+800,				"attachment",		mailAttachments, 		25,						  	 null, 	null, 			null},
			
			{"send_mail",		"internal", 		sender1+10,						sender1+10,				"attachment",		mailAttachments, 		25,						   	getInternalRecepientList(range2+801, range2+1000), 	null, 			null},
			{"send_mail",		"external", 		range2+801,						range2+1000,			"attachment",		mailAttachments, 		25,						  	 externalIds, 	null, 			null},
			{"send_mail",		"group", 			range2+801,						range2+1000,			"attachment",		mailAttachments, 		25,						  	 groupIds, 	null, 			null},
			{"save_indrafts",	"draft", 			range2+801,						range2+1000,			"attachment",		mailAttachments, 		25,						  	 null, 	null, 			null},
			
			
			
			
//			{"send_mail",		"internal", 		1,						2500,				"attachment",		mailAttachments, 		25,						   	groupIds, 	null, 			null},
//			{"save_indrafts",	"draft", 			1,						2500,				"attachment",		mailAttachments, 		25,						   	groupIds, 		null, 			null},
//			{"send_mail",		"external", 		10000,					10000,				"attachment",		mailAttachments, 		100,						   	externalIds, 	null, 			null},
//			{"send_mail",		"group", 			10000,					10000,				"attachment",		mailAttachments, 		100,						   	groupIds, 		null, 			null},
		};
	}


	@Test( dataProvider="dataProviderMailDetails")
	public void sendMail( String mailAction, String identifierKeyword, int userStartingRange, int userEndingRange, String contentLocation, ArrayList<String> mailAttachments, int noOfMailsPerSender,EmailAddressCollection toEmailIds, EmailAddressCollection ccEmaiIds, EmailAddressCollection bccEmailIds ) throws IOException, InterruptedException {


		String userPrefix ="qa-stress";
		String userSuffix ="@o365security.net";
		String fileForMailBody="PCI_Test.txt";
		String contentLocationBothAttachment="PII.rtf";
		String contentLocationBothBody="PCI_Test.txt";
		String userName;
		String userPassword = "Aut0mat10n#123";
		String identifier = null;
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
		ArrayList<String> renamedAttachments = new ArrayList<String>() ;
		String mailSubject=null;
		ArrayList<String> logs =null;
		ArrayList<String> logs2 =null;
		
		Date curDate = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy_MM_dd");
		String DateString = format.format(curDate);	
		String LogFile=null;
		String changeMailSubject = null;




		for(int currUserNumber=userStartingRange;currUserNumber<=userEndingRange; currUserNumber++ ){ // for total number of users
		
			//forming the sender email id
			userName = userPrefix+Integer.toString(currUserNumber)+userSuffix;
//			Thread t = Thread.currentThread();
//			Reporter.log("Thread="+t.getName(),true);
			Reporter.log("==============================================",true);
			Reporter.log("Started Processing for user:"+userName,true);
			Reporter.log("==============================================",true);

			//creating mail object with admin user credentials
			Office365MailActivities objMail = new Office365MailActivities(userName,userPassword); 
			
			
			for(int repeat = 1; repeat <=noOfMailsPerSender; repeat ++){  // repetition of sending mail
				try {
					renamedAttachments = new ArrayList<String>();
					//forming identifier for mail subject and attachment name
					identifier = identifierKeyword+"_"+userPrefix.replace("-", "_")+Integer.toString(currUserNumber)+"_"+repeat+"_";
					Reporter.log("Identifier="+identifier,true);

					if(contentLocation.equals("attachment")){
						if(mailAttachments!=null){
							for (String eachFile : mailAttachments) {
								sourceFile1 = new File(userDir +eachFile);
								newFileName = identifier+eachFile;
								destFile1 = new File(userDir +newFileName);
								copyFileUsingFileChannels(sourceFile1, destFile1);
								renamedAttachments.add(destFile1.toString());
								filesToDelete.add(destFile1);
							}
						}
						mailSubject = identifier+"with_attachments";
						changeMailSubject = "Please find your documents";
						mailBody= "This is test mail body";
						LogFile=DateString+"_attachment";

					}
					else if(contentLocation.equals("body")){
						mailSubject = identifier+"body";
						mailBody =readFile(userDir+fileForMailBody, Charset.defaultCharset());
						LogFile=DateString+"_body";
					}
					else if(contentLocation.equals("both")){
						sourceFile1 = new File(userDir +contentLocationBothAttachment);
						newFileName = identifier+contentLocationBothAttachment;
						destFile1 = new File(userDir +newFileName);
						copyFileUsingFileChannels(sourceFile1, destFile1);
						renamedAttachments.add(destFile1.toString());
						filesToDelete.add(destFile1);
						
						mailSubject = identifier+"both";
						mailBody =readFile(userDir+contentLocationBothBody, Charset.defaultCharset());
						LogFile=DateString+"_both";
					}
					
					Reporter.log("mail subject="+mailSubject,true);
					Reporter.log("attachments="+renamedAttachments.toString(),true);

//					Thread.sleep(10000);
					//cleanupListSent.add(mailSubject);
					if(mailAction.equals("send_mail")){
						success = objMail.sendMailWithCCAndBCC(toEmailIds, ccEmaiIds, bccEmailIds, changeMailSubject, mailBody, renamedAttachments, true);
					}
					else if(mailAction.equals("save_indrafts")){
						changeMailSubject = "Draft";
						mailSubject +="Draft";
						success = objMail.addAttachmentAndSaveInDraft(changeMailSubject, mailBody,renamedAttachments);
					}
					String result;
					if(success){
						LogFile=LogFile+"_passed";
						result = "Passed";
					}
					else{
						LogFile=LogFile+"_failed"; 
						result = "Failed";
					}
					Reporter.log(mailSubject + " "+result, true);
					//writeToFile(LogFile,mailSubject);
					//assertTrue(success, "Failed sending mail with subject:"+mailSubject);
					success=false;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					Reporter.log(e.getMessage());
					e.printStackTrace();
				}
			}
		}
		deleteFiles(filesToDelete);

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
		assertEquals(HttpStatus.SC_OK, getResponseStatusCode(response), "Response code verification failed");
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
	public ArrayList<String> searchDisplayLogs(int from, int to, String facilty,String termType, String termValue, String query, String email, String csrfToken, String sessionId, String searchForUser) throws Exception {

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

		HttpResponse response =  restClient.doPost(dataUri, headers, null, new StringEntity(payload));
		String responseBody = ClientUtil.getResponseBody(response);
		Reporter.log("Request payload:"+ payload, true);
		Reporter.log("================");
		Reporter.log("Response code:"+ response.getStatusLine().getStatusCode()+ " "+ response.getStatusLine().getReasonPhrase(), true);
		Reporter.log("Response body:"+ responseBody, true);

		assertEquals( getResponseStatusCode(response),HttpStatus.SC_OK, "Response code verification failed");
		ForensicSearchResults fsr = MarshallingUtils.unmarshall(responseBody, ForensicSearchResults.class);
		return this.retrieveActualMessages(fsr);
	}


	//@Test(dependsOnMethods = "performSaasActivities")
	public void fetchElasticSearchLogsForCIQ() throws Exception {
		Reporter.log("Retrieving the logs from Elastic Search ...");
		String tsfrom = DateUtils.getMinutesFromCurrentTime(-20);
		String tsto   = DateUtils.getMinutesFromCurrentTime(20);

		//Get headers
		List<NameValuePair> headers = getHeaders();

		String payload = esQueryBuilder.getSearchQueryForCIQ(tsfrom, tsto, "Box", query);
		

		//HttpRequest
		String path = suiteData.getAPIMap().get("getForensicsLogs") ;
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(), path);

		HttpResponse response =  restClient.doPost(dataUri, headers, null, new StringEntity(payload));
		String responseBody = ClientUtil.getResponseBody(response);
		Reporter.log("Request payload:"+ payload, true);
		Reporter.log("============================", true);
		Reporter.log("Response body:"+ responseBody, true);

		ESResults esresults = MarshallingUtils.unmarshall(responseBody, ESResults.class);
		assertEquals(HttpStatus.SC_OK, getResponseStatusCode(response), "Response code verification failed");
		//Add the assertions for forensic logs
		ciqValidator = new CIQValidator(esresults);

		//Validate all code
		ciqValidator.validateAll(prepareExpectedSource());
	}



	//@Test
	public void getExposedDocuments() throws Exception {

		List<NameValuePair> headers = getHeaders();

		List<NameValuePair> qparams = new ArrayList<NameValuePair>(); 
		qparams.add(new BasicNameValuePair("is_internal", "true"));
		qparams.add(new BasicNameValuePair("owned_by",  URLEncoder.encode(suiteData.getUsername(), "UTF-8")));
		qparams.add(new BasicNameValuePair("content_checks.vl_types",  URLEncoder.encode("hipaa,pii", "UTF-8")));
		//qparams.add(new BasicNameValuePair("content_checks.vk_content_iq_violations",  "test_ciq_profile"));

		String path = suiteData.getAPIMap().get("getBoxDocuments").
				replace("{tenant}", suiteData.getTenantName()).
				replace("{version}", suiteData.getBaseVersion());

		System.out.println("Path:" + path);
		URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(), path, qparams);
		System.out.println("Path:" + uri.toString());
		HttpResponse response =  restClient.doGet(uri, headers);
		String responseBody = ClientUtil.getResponseBody(response);

		System.out.println("Response body:"+ responseBody);
	}

	//Get expected source
	public com.elastica.beatle.ciq.dto.Source prepareExpectedSource() throws Exception {

		com.elastica.beatle.ciq.dto.Source source = new com.elastica.beatle.ciq.dto.Source();
		source.setSeverity("critical");
		source.setFacility("Box");
		source.setObjectName("/All Files/"+destinationFile);
		source.setResourceId(resourceId);
		source.setRisks("PII, ContentIQ Violations, HIPAA");
		source.setSource("API");

		String filepath = System.getProperty("user.dir") + "/src/test/resources/securlets/securletsData/" + "ContentChecksHipaa.json";
		FileInputStream inStream = new FileInputStream(FilenameUtils.separatorsToSystem(filepath));
		String contentChecksJson = IOUtils.toString(inStream);
		ContentChecks contentChecks = MarshallingUtils.unmarshall(contentChecksJson, ContentChecks.class);

		source.setContentChecks(contentChecks);
		source.setUser(suiteData.getUsername());
		source.setMessage("File "+ destinationFile +" has risk(s) - PII, ContentIQ Violations, HIPAA");
		source.setActivityType("Content Inspection");
		source.setName(destinationFile);
		return source;

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

	private static void copyFileUsingFileChannels(File source, File dest)
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

	
	//this is test method for trying out codes
	@Test( groups={"test"})
	public void query1() throws Exception {	
		EmailAddressCollection x = getExternalRecepientList(1, 25);
		Reporter.log(x.getItems().toString(),true);
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

				logs = searchDisplayLogs(-30, 30, "Office 365",filterByKey, filterByValue, searchQuery, suiteData.getUsername(),suiteData.getCSRFToken(), suiteData.getSessionID(),searchForUser);
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
	

	public void writeToFile(String fileName, String data){
    		
			String myfile = userDir+fileName;
    		try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(myfile, true)))) {
    		    out.println(data);
    		}catch (IOException e) {
    			Reporter.log("FileOperation Failed",true);
    		    //exception handling left as an exercise for the reader
    		}
    		
	        
	}
	
	
	
}