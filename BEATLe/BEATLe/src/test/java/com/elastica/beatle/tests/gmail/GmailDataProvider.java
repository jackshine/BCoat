package com.elastica.beatle.tests.gmail;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.testng.annotations.DataProvider;

import com.elastica.beatle.i18n.I18N;
import com.elastica.beatle.tests.infra.InfraConstants;
public class GmailDataProvider {
	//static String uniqueSubject="Gmail QA Test-"+new Date().toString();
	static String uniqueIdInternal=UUID.randomUUID().toString();
	static String uniqueIdexternal=UUID.randomUUID().toString();
	static String uniqueIdSelf=UUID.randomUUID().toString();
	static String uniqueIdMixed=UUID.randomUUID().toString();
	static String uniqueIDelete=UUID.randomUUID().toString();
	public static final String TESTUSER1="testuser1@securletbeatle.com";
	public static final String TESTUSER2="testuser2@securletbeatle.com";
	@DataProvider
	public static Object[][] sendPlainMailDataProvider()
	{	
		ArrayList<String> to = new ArrayList<String>();
		to.add("testuser1@securletbeatle.com");

		ArrayList<String> internalRecipients = new ArrayList<String>();
		internalRecipients.add("testuser1@securletbeatle.com");

		ArrayList<String> externalRecipients = new ArrayList<String>();
		externalRecipients.add("rahulsky.java@gmail.com");
		ArrayList<String> groupRecipients = new ArrayList<String>();
		groupRecipients.add("group@securletbeatle.com");
		ArrayList<String> ExternalGroupRecipients = new ArrayList<String>();
		ExternalGroupRecipients.add("external@infrabeatle.com");
		
		ArrayList<String> internalRecipientsCapitalLetter = new ArrayList<String>();
		internalRecipientsCapitalLetter.add("TESTUSER1@SECURLETBEATLE.COM");
		ArrayList<String> internalRecipientsCapitalLetterMix = new ArrayList<String>();
		internalRecipientsCapitalLetterMix.add("TESTUSER1@securletbeatle.com");
		ArrayList<String> inFolder1 = new ArrayList<String>();
		inFolder1.add("Sent Mail");
		

		return new Object[][] { 
			//Key, Desc,  to, cc,	bcc, infolder, internalrecipients, externalrecipients
			{ "sendPlainMailMessage1",  "Send email to single internal recipient and check the activity logs", 
				internalRecipients, null, null, inFolder1, internalRecipients, null,"testuser1@securletbeatle.com"},

			{ "sendPlainMailMessage2",  "Send email to single external recipient and check the activity logs",
					externalRecipients, null, null, inFolder1, null, externalRecipients,"rahulsky.java@gmail.com"},

			{ "sendPlainMailMessage3",  "Send email to one single internal and external recipient and check the activity logs", 
						internalRecipients, externalRecipients, null, inFolder1, internalRecipients, externalRecipients,"2 users"},
			
			{ "sendPlainMailMessage4",  "Send email to one single internal and external recipient(bcc) and check the activity logs", 
							internalRecipients, null, externalRecipients, inFolder1, internalRecipients, externalRecipients,"2 users"},
			
			{ "sendPlainMailMessage5",  "Send email to one single external and internal recipient(bcc) and check the activity logs", 
								externalRecipients, null, internalRecipients, inFolder1, internalRecipients, externalRecipients,"2 users"},
			{ "sendPlainMailMessagegroup",  "Send email to one  internal group recipient(bcc) and check the activity logs", 
									groupRecipients, null, null, inFolder1, groupRecipients, null,"group@securletbeatle.com"},
			{ "sendPlainMailMessagegroupExternal",  "Send email to one  internal group recipient(bcc) and check the activity logs", 
										ExternalGroupRecipients, null, null, inFolder1, null, ExternalGroupRecipients,"external@infrabeatle.com"},
			{ "sendPlainMailCapitalLetters",  "Send email to one  with email id in Capital letter and check the activity logs", 
											internalRecipientsCapitalLetter, null, null, inFolder1, internalRecipientsCapitalLetter, null,"testuser1@securletbeatle.com"},
			{ "sendPlainMailCapitalLettersMix",  "Send email to one  with email id in Capital letter and check the activity logs", 
												internalRecipientsCapitalLetterMix, null, null, inFolder1, internalRecipientsCapitalLetterMix, null,"testuser1@securletbeatle.com"},


		};
	}
	
	@DataProvider
	public static Object[][] sendPlainMailI18()
	{	
		ArrayList<String> to = new ArrayList<String>();
		to.add("testuser1@securletbeatle.com");

		ArrayList<String> internalRecipients = new ArrayList<String>();
		internalRecipients.add("testuser1@securletbeatle.com");
		//internalRecipients.add("testuser2@securletbeatle.com");

//		ArrayList<String> externalRecipients = new ArrayList<String>();
//		externalRecipients.add("rahulsky.java@gmail.com");
//		ArrayList<String> groupRecipients = new ArrayList<String>();
//		groupRecipients.add("group@securletbeatle.com");
//		ArrayList<String> ExternalGroupRecipients = new ArrayList<String>();
//		ExternalGroupRecipients.add("external@infrabeatle.com");
//		
//		ArrayList<String> internalRecipientsCapitalLetter = new ArrayList<String>();
//		internalRecipientsCapitalLetter.add("TESTUSER1@SECURLETBEATLE.COM");
//		ArrayList<String> internalRecipientsCapitalLetterMix = new ArrayList<String>();
//		internalRecipientsCapitalLetterMix.add("TESTUSER1@securletbeatle.com");
		ArrayList<String> inFolder1 = new ArrayList<String>();
		inFolder1.add("Sent Mail");
		

		return new Object[][] { 
			//Key, Desc,  to, cc,	bcc, infolder, internalrecipients, externalrecipients
			{ "Tamil", " subject and mail body",  I18N.getString("language", "ta_in"), I18N.getString("language", "ta_in"),
				internalRecipients, null, null, inFolder1, internalRecipients, null,"testuser1@securletbeatle.com"},

			{ "Urdu", " subject and mail body",  I18N.getString("language", "ur_in"), I18N.getString("language", "ur_in"),
					internalRecipients, null, null, inFolder1, internalRecipients, null,"testuser1@securletbeatle.com"},

			{ "Chinese", " subject and mail body", I18N.getString("language", "zh_cn"), I18N.getString("language", "zh_cn"),
						internalRecipients, null, null, inFolder1, internalRecipients, null,"testuser1@securletbeatle.com"},
			
			{ "Japanese", " subject and mail body", I18N.getString("language", "ja_jp"), I18N.getString("language", "ja_jp"),
							internalRecipients, null, null, inFolder1, internalRecipients, null,"testuser1@securletbeatle.com"},
			
			{ "French",  " subject and mail body",I18N.getString("language", "fr_fr"),I18N.getString("language", "fr_fr") ,
								internalRecipients, null, null, inFolder1, internalRecipients, null,"testuser1@securletbeatle.com"},
			{ "German", " subject and mail body", I18N.getString("language", "de_de"),I18N.getString("language", "de_de"),
									internalRecipients, null, null, inFolder1, internalRecipients, null,"testuser1@securletbeatle.com"},
			{ "Mexican", " subject and mail body", "Fiestas Patrias de Chile", "Fiestas Patrias de Chile",
										internalRecipients, null, null, inFolder1, internalRecipients, null,"testuser1@securletbeatle.com"},
			{ "Spanish", " subject and mail body",I18N.getString("language", "es_es"), I18N.getString("language", "es_es"),
											internalRecipients, null, null, inFolder1, internalRecipients, null,"testuser1@securletbeatle.com"},
			{ "Portuguese", " subject and mail body", I18N.getString("language", "pt_br"), I18N.getString("language", "pt_br"),
												internalRecipients, null, null, inFolder1, internalRecipients, null,"testuser1@securletbeatle.com"},


		};
	}
	@DataProvider
	public static Object[][] sendPlainMailKPI18()
	{	
		return new Object[][] {
			{ "Tamil"},
			{ "Urdu"},
			{ "Chinese"},
			{ "Japanese"},
			{ "French"},
			{ "German"},
			{ "Mexican"},
			{ "Spanish"},
			{ "Portuguese"},
			
			
		};
	}

	
	@DataProvider
	public static Object[][] CIDataProvider()
	{	
		ArrayList<String> to = new ArrayList<String>();
		to.add("testuser1@securletbeatle.com");

		ArrayList<String> internalRecipients = new ArrayList<String>();
		internalRecipients.add("testuser1@securletbeatle.com");

		ArrayList<String> externalRecipients = new ArrayList<String>();
		externalRecipients.add("rahulsky.java@gmail.com");


		ArrayList<String> inFolder1 = new ArrayList<String>();
		inFolder1.add("Sent Mail");


		return new Object[][] { 
			//Key, Desc,  to, cc,	bcc, infolder, internalrecipients, externalrecipients
			{ "sendMailWithPIIRisk",  "Send email with PII information to single internal recipient and check the activity logs", 
				"pci.txt", "PCI", internalRecipients, null, null, inFolder1, internalRecipients, null},
		};
	}
	
	
	@DataProvider
	public static Object[][] CIVerificationDataProvider()
	{	
		return new Object[][] {
			{ "sendMailWithPIIRisk"},
		};
	}
	
	
	@DataProvider
	public static Object[][] sendMailWithAttachmentDataProvider()
	{	
		ArrayList<String> to = new ArrayList<String>();
		to.add("testuser1@securletbeatle.com");

		ArrayList<String> internalRecipients = new ArrayList<String>();
		internalRecipients.add("testuser1@securletbeatle.com");

		ArrayList<String> externalRecipients = new ArrayList<String>();
		externalRecipients.add("rahulsky.java@gmail.com");


		ArrayList<String> inFolder1 = new ArrayList<String>();
		inFolder1.add("Sent Mail");


		return new Object[][] { 
			//Key, Desc,  to, cc,	bcc, infolder, internalrecipients, externalrecipients filename
			{ "sendTextAttachmentMailMessage1",  "Send email to single internal recipient and check the activity logs", 
				internalRecipients, null, null, inFolder1, null, null,"test.txt","testuser1@securletbeatle.com"},

			{ "sendTextAttachmentMailMessage2",  "Send email to single external recipient(to) and check the activity logs",
					externalRecipients, null, null, inFolder1, null, externalRecipients,"test.txt","rahulsky.java@gmail.com"},

			{ "sendTextAttachmentMailMessage3",  "Send email to one single internal(to) and external recipient(cc) and check the activity logs", 
						internalRecipients, externalRecipients, null, inFolder1, internalRecipients, externalRecipients,"test.txt","2 users"},
//		{ "sendImageAttachmentMessage1",  "Send email to single external recipient and check the activity logs",
//							externalRecipients, null, null, inFolder1, null, externalRecipients,"desktop.png","rahulsky.java@gmail.com"},
//
//			{ "sendImageAttachmentMessage2",  "Send email to one single internal and external recipient and check the activity logs", 
//								internalRecipients, externalRecipients, null, inFolder1, internalRecipients, externalRecipients,"desktop.png"},

		};
	}
	
	@DataProvider
	public static Object[][] sendMailWithAttachmentDataProviderSanity()
	{	
		ArrayList<String> to = new ArrayList<String>();
		to.add("testuser1@securletbeatle.com");

		ArrayList<String> internalRecipients = new ArrayList<String>();
		internalRecipients.add("admin@securletbeatle.com");
	//	internalRecipients.add("testuser2@securletbeatle.com");

		ArrayList<String> externalRecipients = new ArrayList<String>();
		externalRecipients.add("rahulsky.java@gmail.com");


		ArrayList<String> inFolder1 = new ArrayList<String>();
		inFolder1.add("Sent Mail");


		return new Object[][] { 
			//Key, Desc,  to, cc,	bcc, infolder, internalrecipients, externalrecipients filename
			{ "sendTextAttachmentMailMessageSanity1",  "Send email to single internal recipient and check the activity logs", 
				internalRecipients, null, null, inFolder1, null, null,"test.txt","admin@securletbeatle.com"},

		};
	}
	
	@DataProvider
	public static Object[][] sendMailWithMultiAttachmentDataProvider()
	{	
		ArrayList<String> to = new ArrayList<String>();
		to.add("testuser1@securletbeatle.com");

		ArrayList<String> internalRecipients = new ArrayList<String>();
		internalRecipients.add("testuser1@securletbeatle.com");

		ArrayList<String> externalRecipients = new ArrayList<String>();
		externalRecipients.add("rahulsky.java@gmail.com");


		ArrayList<String> inFolder1 = new ArrayList<String>();
		inFolder1.add("Sent Mail");

		ArrayList<String> filename = new ArrayList<String>();
		String filename1="test.txt";
		String filename2="desktop.png";
		//gmailApi.sendMessageWithAttachment(to,to,to, "Testing Sending to Multiple", "I am Body", "/Users/rahulkumar/NetBeansProjects/BackendAutomation/BeatleElastica/BEATLe/BEATLe/pom.xml","TestAttachment.xml");
		String filepath = InfraConstants.INFRA_DATA_LOC + filename1;
		String filepath2 = InfraConstants.INFRA_DATA_LOC + filename2;
		//  String   LocalFileLocation = DCITestConstants.DCI_FILE_UPLOAD_PATH + File.separator + actualFileNameToUpload;
		//check the filename provided is absolute or not
		File uploadFile = new java.io.File(FilenameUtils.separatorsToSystem(filepath).trim());
		if(!uploadFile.exists()) {
			System.out.println("Sorry file not exists in the folder"+uploadFile); 
		}

		filename.add(filepath);
		filename.add(filepath2);
		return new Object[][] { 
			//Key, Desc,  to, cc,	bcc, infolder, internalrecipients, externalrecipients filename
			{ "sendMultiAttachmentMailMessage1",  "Send email to single internal recipient and check the activity logs", 
				internalRecipients, null, null, inFolder1, null, null,filename,"testuser1@securletbeatle.com"},

			{ "sendMultiAttachmentMailMessage2",  "Send email to single external recipient(to) and check the activity logs",
					externalRecipients, null, null, inFolder1, null, externalRecipients,filename,"rahulsky.java@gmail.com"},

			{ "sendMultiAttachmentMailMessage3",  "Send email to one single internal(to) and external recipient(cc) and check the activity logs", 
						internalRecipients, externalRecipients, null, inFolder1, internalRecipients, externalRecipients,filename,"2 users"},
			//			{ "sendImageAttachmentMessage1",  "Send email to single external recipient and check the activity logs",
			//							externalRecipients, null, null, inFolder1, null, externalRecipients,"desktop.png"},
			//
			//			{ "sendImageAttachmentMessage2",  "Send email to one single internal and external recipient and check the activity logs", 
			//								internalRecipients, externalRecipients, null, inFolder1, internalRecipients, externalRecipients,"desktop.png"},

		};
	}
	@DataProvider
	public static Object[][] sendMultiAttachmentMailKeyDataProvider()
	{	
		return new Object[][] {
			{ "sendMultiAttachmentMailMessage1"},
			{ "sendMultiAttachmentMailMessage2"},
			{ "sendMultiAttachmentMailMessage3"},

		};
	}
	
	@DataProvider
	public static Object[][] crateDraftMIMEAttachmentDataProvider()
	{	
		ArrayList<String> to = new ArrayList<String>();
		to.add("testuser1@securletbeatle.com");

		ArrayList<String> internalRecipients = new ArrayList<String>();
		internalRecipients.add("testuser1@securletbeatle.com");

		ArrayList<String> externalRecipients = new ArrayList<String>();
		externalRecipients.add("rahulsky.java@gmail.com");


		ArrayList<String> inFolder1 = new ArrayList<String>();
		inFolder1.add("Drafts");


		return new Object[][] { 
			//Key, Desc,  to, cc,	bcc, infolder, internalrecipients, externalrecipients filename
			
			{ "createImageAttachmentMessage2",  "create draft with MIME Attachment and check the activity logs", 
								internalRecipients, externalRecipients, null, inFolder1, internalRecipients, externalRecipients,"desktop.png","rahulsky.java@gmail.com"},

		};
	}

	@DataProvider
	public static Object[][] createDraftDataProvider()
	{	
		ArrayList<String> to = new ArrayList<String>();
		to.add("testuser1@securletbeatle.com");

		ArrayList<String> internalRecipients = new ArrayList<String>();
		internalRecipients.add("testuser1@securletbeatle.com");

		ArrayList<String> externalRecipients = new ArrayList<String>();
		externalRecipients.add("rahulsky.java@gmail.com");


		ArrayList<String> inFolder1 = new ArrayList<String>();
		inFolder1.add("Drafts");


		return new Object[][] { 
			//Key, Desc,  to, cc,	bcc, infolder, internalrecipients, externalrecipients filename
			
			{ "createDraftMessage1",  "create draft with MIME Attachment and check the activity logs", 
								internalRecipients, externalRecipients, null, inFolder1, internalRecipients, externalRecipients},

		};
	}
	@DataProvider
	public static Object[][] createDraftDataKeyProvider()
	{	
		return new Object[][] {
			{ "createDraftMessage1"},
				
		};
	}
	@DataProvider
	public static Object[][] draftWithMIMEVerificationDataProvider()
	{	
		return new Object[][] {
			{ "createImageAttachmentMessage2"},
		};
	}
	

	@DataProvider
	public static Object[][] sendMailWithRiskAttachmentDataProvider()
	{	
		ArrayList<String> to = new ArrayList<String>();
		to.add("testuser1@securletbeatle.com");

		ArrayList<String> internalRecipients = new ArrayList<String>();
		internalRecipients.add("testuser1@securletbeatle.com");

		ArrayList<String> externalRecipients = new ArrayList<String>();
		externalRecipients.add("rahulsky.java@gmail.com");


		ArrayList<String> inFolder1 = new ArrayList<String>();
		inFolder1.add("Sent Mail");


		return new Object[][] { 
			//Key, Desc,  to, cc,	bcc, infolder, internalrecipients, externalrecipients filename
			{ "sendAttachmentMailMessage1",  "Send email to single internal recipient with PCI risks as attachment and check the activity logs", 
				"pci.txt", "PCI", internalRecipients, null, null, inFolder1, null, null},
			{ "sendAttachmentMailMessage2",  "Send email to single internal recipient with PII risks as attachment and check the activity logs", 
					"pii.rtf", "PII", internalRecipients, null, null, inFolder1, null, null},
			{ "sendAttachmentMailMessage3",  "Send email to single internal recipient with HIPAA risks as attachment and check the activity logs", 
						"hipaa.txt", "PII, HIPAA", internalRecipients, null, null, inFolder1, null, null},
						
		};
	}
	
	@DataProvider
	public static Object[][] sendMailWithRiskAttachmentKeyDataProvider()
	{	
		return new Object[][] {
			{ "sendAttachmentMailMessage1"},
			{ "sendAttachmentMailMessage2"},
			{ "sendAttachmentMailMessage3"},
		};
	}
	
	
	@DataProvider
	public static Object[][] trashMailDataProvider()
	{	
		ArrayList<String> to = new ArrayList<String>();
		to.add("testuser1@securletbeatle.com");

		ArrayList<String> internalRecipients = new ArrayList<String>();
		internalRecipients.add("testuser1@securletbeatle.com");

		ArrayList<String> externalRecipients = new ArrayList<String>();
		externalRecipients.add("rahulsky.java@gmail.com");


		ArrayList<String> inFolder1 = new ArrayList<String>();
		inFolder1.add("Sent Mail");


		return new Object[][] { 
			//Key, Desc,  to, cc,	bcc, infolder, internalrecipients, externalrecipients filename
			{   "trash the email and check the activity logs", 
									to, internalRecipients, null, null,internalRecipients},
					
		};
	}


	@DataProvider
	public static Object[][] deleteDraftDataProvider()
	{	
		ArrayList<String> to = new ArrayList<String>();
		to.add("testuser1@securletbeatle.com");

		ArrayList<String> internalRecipients = new ArrayList<String>();
		internalRecipients.add("testuser1@securletbeatle.com");

		ArrayList<String> externalRecipients = new ArrayList<String>();
		externalRecipients.add("rahulsky.java@gmail.com");


		ArrayList<String> inFolder1 = new ArrayList<String>();
		inFolder1.add("Delete");


		return new Object[][] { 
			//Key, Desc,  to, cc,	bcc, infolder, internalrecipients, externalrecipients filename
			{ "deleteDraft1",  "delete draft ", 
				internalRecipients, null, null, inFolder1,internalRecipients,null},


		};
	}

	@DataProvider
	public static Object[][] sendDraftDataProvider()
	{	
		ArrayList<String> to = new ArrayList<String>();
		to.add("testuser1@securletbeatle.com");

		ArrayList<String> internalRecipients = new ArrayList<String>();
		internalRecipients.add("testuser1@securletbeatle.com");

		ArrayList<String> externalRecipients = new ArrayList<String>();
		externalRecipients.add("rahulsky.java@gmail.com");


		ArrayList<String> inFolder1 = new ArrayList<String>();
		inFolder1.add("Sent Mail");


		return new Object[][] { 
			//Key, Desc,  to, cc,	bcc, infolder, internalrecipients, externalrecipients
			{ "createDraftMailMessage1",  "Send draft email to single internal recipient and check the activity logs", 
				internalRecipients, null, null, inFolder1, internalRecipients, null,"testuser1@securletbeatle.com"
			},

			//			{ "sendAttachmentMailMessage2",  "Send email to single external recipient and check the activity logs",
			//									externalRecipients, null, null, inFolder1, null, externalRecipients},
			//			
			//			{ "sendAttachmentMailMessage3",  "Send email to one single internal and external recipient and check the activity logs", 
			//									internalRecipients, externalRecipients, null, inFolder1, internalRecipients, externalRecipients},
		};
	}

	@DataProvider
	public static Object[][] createDraftWithMultiAttachmentDataProvider()
	{	
		ArrayList<String> to = new ArrayList<String>();
		to.add("testuser1@securletbeatle.com");

		ArrayList<String> internalRecipients = new ArrayList<String>();
		internalRecipients.add("testuser1@securletbeatle.com");

		ArrayList<String> externalRecipients = new ArrayList<String>();
		externalRecipients.add("rahulsky.java@gmail.com");


		ArrayList<String> inFolder1 = new ArrayList<String>();
		inFolder1.add("Drafts");
		String filename1="test.txt";
		String filename2="desktop.png";
		//gmailApi.sendMessageWithAttachment(to,to,to, "Testing Sending to Multiple", "I am Body", "/Users/rahulkumar/NetBeansProjects/BackendAutomation/BeatleElastica/BEATLe/BEATLe/pom.xml","TestAttachment.xml");
				String filepath = InfraConstants.INFRA_DATA_LOC + filename1;
				String filepath2 = InfraConstants.INFRA_DATA_LOC + filename2;
				//  String   LocalFileLocation = DCITestConstants.DCI_FILE_UPLOAD_PATH + File.separator + actualFileNameToUpload;
				//check the filename provided is absolute or not
				File uploadFile = new java.io.File(FilenameUtils.separatorsToSystem(filepath).trim());
				if(!uploadFile.exists()) {
					System.out.println("Sorry file not exists in the folder"); 
				}
		
		ArrayList<String> filename = new ArrayList<String>();
		filename.add(filepath);
		filename.add(filepath2);
		return new Object[][] { 
			//Key, Desc,  to, cc,	bcc, infolder, internalrecipients, externalrecipients filename
			{ "createDraftWithMultiAttachmentMessage1",  "Create Draft with multi attachment for  email to single internal recipient and check the activity logs", 
				internalRecipients, null, null, inFolder1, internalRecipients, null,filename},

	
		};
	}
	@DataProvider
	public static Object[][] createMultiAttachemntDraftKeyDP()
	{	
		return new Object[][] {
			{ "createDraftWithMultiAttachmentMessage1"},
		//	{ "createDraftWithMultiAttachmentMessage1"},
			};
	}
	
	@DataProvider
	public static Object[][] sendPlainMailKeyDataProvider()
	{	
		return new Object[][] {
			{ "sendPlainMailMessage1"},
			{ "sendPlainMailMessage2"},
			{ "sendPlainMailMessage3"},
			{ "sendPlainMailMessage4"},
			{ "sendPlainMailMessage5"},
			{ "sendPlainMailMessagegroup"},
			{ "sendPlainMailMessagegroupExternal"},
			{ "sendPlainMailCapitalLetters"},
			{ "sendPlainMailCapitalLettersMix"},
			
			
		};
	}


	@DataProvider
	public static Object[][] sendAttachmentMailKeyDataProvider()
	{	
		return new Object[][] {
			{ "sendTextAttachmentMailMessage1"},
			{ "sendTextAttachmentMailMessage2"},
			{ "sendTextAttachmentMailMessage3"},
//			{ "sendImageAttachmentMessage1"},
//			{ "sendImageAttachmentMessage2"},

		};
	}
	
	@DataProvider
	public static Object[][] sendAttachmentMailSanityKeyDataProvider()
	{	
		return new Object[][] {
			{ "sendTextAttachmentMailMessageSanity1"},
	
		};
	}
	@DataProvider
	public static Object[][] sendDraftKeyDataProvider()
	{	
		return new Object[][] {
			{ "createDraftMailMessage1"},
		//	{ "sendCreateddraft"},
			//{ "sendAttachmentMailMessage3"},
		};
	}


	@DataProvider
	public static Object[][] deleteGmailDataProvider()
	{	
		//String subject = "Securlet status testing " + uniqueIdInternal;
		//String messageBody = "Please find the status of gmail securlet testing";

		

		ArrayList<String> internalRecipient = new ArrayList<String>();
		internalRecipient.add("testuser1@securletbeatle.com");

		ArrayList<String> externalRecipient = new ArrayList<String>();
		externalRecipient.add("rahulsky.java@gmail.com");



		ArrayList<String> ccInternal = new ArrayList<String>();
		ccInternal.add("testuser2@securletbeatle.com");

		ArrayList<String> bccInternal = new ArrayList<String>();
		bccInternal.add("securletuser@securletbeatle.com ");

		ArrayList<String> toExternal = new ArrayList<String>();
		toExternal.add("rahul.embeddedsystem@gmail.com");

		ArrayList<String> ccExternal = new ArrayList<String>();
		ccExternal.add("admin@infrabeatle.com");

		ArrayList<String> bccExternal = new ArrayList<String>();
		bccExternal.add("vijay.gangwar@infrabeatle.com ");

		ArrayList<String> toMixed = new ArrayList<String>();
		toMixed.add("rahul.embeddedsystem@gmail.com");
		toMixed.add("testuser1@securletbeatle.com");

		ArrayList<String> ccMixed = new ArrayList<String>();
		ccMixed.add("admin@infrabeatle.com");
		ccMixed.add("testuser2@securletbeatle.com");

		ArrayList<String> bccMixed = new ArrayList<String>();
		bccMixed.add("vijay.gangwar@infrabeatle.com ");
		bccMixed.add("securletuser@securletbeatle.com ");

		ArrayList<String> toself = new ArrayList<String>();
		toself.add("admin@securletbeatle.com");


		ArrayList<String> inFolder1 = new ArrayList<String>();
		inFolder1.add("Sent Mail");


		return new Object[][] { 
			//Action,  		to, 		cc,			 bcc,		 subject 					email body
			{ "Send email to single external recipient and check the activity logs", 
				externalRecipient, null, null, inFolder1},
		};
	}

	@DataProvider
	public static Object[][] recievePlainMailDataProvider()
	{	
		ArrayList<String> to = new ArrayList<String>();
		to.add("testuser1@securletbeatle.com");

		ArrayList<String> internalRecipients = new ArrayList<String>();
		internalRecipients.add("testuser1@securletbeatle.com");

		ArrayList<String> MixedRecipients = new ArrayList<String>();
		MixedRecipients.add("rahulsky.java@gmail.com");
		MixedRecipients.add("testuser1@securletbeatle.com");
		
		ArrayList<String> externalRecipients = new ArrayList<String>();
		externalRecipients.add("rahulsky.java@gmail.com");
	//	externalRecipients.add("admin@securletbeatle.com");
		
		ArrayList<String> inFolder1 = new ArrayList<String>();
		inFolder1.add("Inbox");


		return new Object[][] { 
			//Key, Desc,  to, cc,	bcc, infolder, internalrecipients, externalrecipients
			{ "receivePlainMailMessage1",  "Send email to single internal recipient and check the activity logs", 
				internalRecipients, null, null, inFolder1, internalRecipients, null,"testuser2@securletbeatle.com"},

			{ "receivePlainMailMessage2",  "Send email to single external recipient and check the activity logs",
					MixedRecipients, null, null, inFolder1, null, externalRecipients,"testuser2@securletbeatle.com"},
			//			
			//			{ "recievePlainMailMessage3",  "Send email to one single internal and external recipient and check the activity logs", 
			//									internalRecipients, externalRecipients, null, inFolder1, internalRecipients, externalRecipients},
		};
	}

	@DataProvider
	public static Object[][] receiveMailWithAttachmentDataProvider()
	{	
		ArrayList<String> to = new ArrayList<String>();
		to.add("testuser1@securletbeatle.com");

		ArrayList<String> internalRecipients = new ArrayList<String>();
		internalRecipients.add("testuser1@securletbeatle.com");

		ArrayList<String> externalRecipients = new ArrayList<String>();
		externalRecipients.add("rahulsky.java@gmail.com");
		
		ArrayList<String> mixedRecipients = new ArrayList<String>();
		mixedRecipients.add("rahulsky.java@gmail.com");
		mixedRecipients.add("testuser1@securletbeatle.com");
		ArrayList<String> inFolder1 = new ArrayList<String>();
		inFolder1.add("Inbox");


		return new Object[][] { 
			//Key, Desc,  to, cc,	bcc, infolder, internalrecipients, externalrecipients
			{ "receiveAttachmentMailMessage1",  "Receive email from single internal user and check the activity logs", 
				internalRecipients, null, null, inFolder1, internalRecipients, null,"test.txt","testuser2@securletbeatle.com"},

			{ "receiveAttachmentMailMessage2",  "Receive email from single internal user and check the activity logs",
				mixedRecipients, null, null, inFolder1, internalRecipients, externalRecipients,"test.txt","testuser2@securletbeatle.com"},
//			{ "recieveImageAttachmentMailMessage1",  "Receive email from single internal user and check the activity logs", 
//										internalRecipients, null, null, inFolder1, internalRecipients, null,"desktop.png","testuser1@securletbeatle.com"},
//				
//				{ "recieveImageAttachmentMailMessage2",  "Send email to single external recipient and check the activity logs",
//										externalRecipients, null, null, inFolder1, null, externalRecipients,"desktop.png"},

		};
	}

	@DataProvider
	public static Object[][] recieveAttachmentMailKeyDataProvider()
	{	
		return new Object[][] {
			{ "receiveAttachmentMailMessage1"},
			{ "receiveAttachmentMailMessage2"},
//			{ "receiveImageAttachmentMailMessage1"},
//			{ "receiveImageAttachmentMailMessage2"},
		};
	}


	@DataProvider
	public static Object[][] recieveMailExternalUserDataProvider()
	{	
		ArrayList<String> to = new ArrayList<String>();
		to.add("testuser1@securletbeatle.com");

		ArrayList<String> internalRecipients = new ArrayList<String>();
		internalRecipients.add("testuser1@securletbeatle.com");

		ArrayList<String> externalRecipients = new ArrayList<String>();
		externalRecipients.add("rahulsky.java@gmail.com");
		
		ArrayList<String> mixedRecipients = new ArrayList<String>();
		//mixedRecipients.add("rahulsky.java@gmail.com");
		mixedRecipients.add("admin@securletbeatle.com");
		ArrayList<String> inFolder1 = new ArrayList<String>();
		inFolder1.add("Inbox");


		return new Object[][] { 
			//Key, Desc,  to, cc,	bcc, infolder, internalrecipients, externalrecipients
			{"testuser1@securletbeatle.com", "receiveAttachmentMailMessageExtrnal1",  "Receive email from single internal user and check the activity logs", 
				internalRecipients, null, null, inFolder1, internalRecipients, null,"test.txt","testuser3@securleto365beatle.com"},

			{"admin@securletbeatle.com", "receiveAttachmentMailMessageExtrnal2",  "Receive email from single internal user and check the activity logs",
				mixedRecipients, null, null, inFolder1, mixedRecipients, null,"test.txt","testuser3@securleto365beatle.com"},

		};
	}

	@DataProvider
	public static Object[][] recieveMailExternalUserKeyDataProvider()
	{	
		return new Object[][] {
			{ "receiveAttachmentMailMessageExtrnal1"},
			{ "receiveAttachmentMailMessageExtrnal2"},

		};
	}



	@DataProvider
	public static Object[][] recievePlainMailKeyDataProvider()
	{	
		return new Object[][] {
			{ "receivePlainMailMessage1"},
			{ "receivePlainMailMessage2"},
			//		{ "sendPlainMailMessage3"},
		};
	}


	@DataProvider(parallel = true)
	public static Object[][] gmailLogsValidation()
	{	




		return new Object[][] { 
			//Action,  		subject , 		boday,	

			{ "send email Internal user",uniqueIdInternal+"Internal Users :","This is body ,Internal Users" },
			{ "send email External user",  uniqueIdexternal+"External Users :","This is body,External Users" },
			{ "send email Mixed user", uniqueIdMixed+"Mixed Users :","This is body" },
			{ "send email self", uniqueIdMixed+"self :","send email to self,without any exposures" },							


		};
	}

	//	
	//	ArrayList<String> ccInternal = new ArrayList<String>();
	//	ccInternal.add("testuser2@securletbeatle.com");
	//	
	//	ArrayList<String> bccInternal = new ArrayList<String>();
	//	bccInternal.add("securletuser@securletbeatle.com ");
	//	
	//	ArrayList<String> toExternal = new ArrayList<String>();
	//	toExternal.add("rahul.embeddedsystem@gmail.com");
	//	
	//	ArrayList<String> ccExternal = new ArrayList<String>();
	//	ccExternal.add("admin@infrabeatle.com");
	//	
	//	ArrayList<String> bccExternal = new ArrayList<String>();
	//	bccExternal.add("vijay.gangwar@infrabeatle.com ");
	//	
	//	ArrayList<String> toMixed = new ArrayList<String>();
	//	toMixed.add("rahul.embeddedsystem@gmail.com");
	//	toMixed.add("testuser1@securletbeatle.com");
	//	
	//	ArrayList<String> ccMixed = new ArrayList<String>();
	//	ccMixed.add("admin@infrabeatle.com");
	//	ccMixed.add("testuser2@securletbeatle.com");
	//	
	//	ArrayList<String> bccMixed = new ArrayList<String>();
	//	bccMixed.add("vijay.gangwar@infrabeatle.com ");
	//	bccMixed.add("securletuser@securletbeatle.com ");
	//	
	//	ArrayList<String> toself = new ArrayList<String>();
	//	toself.add("admin@securletbeatle.com");
	//	

}
