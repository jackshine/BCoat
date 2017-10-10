package com.elastica.beatle.tests.gmail;

import java.util.ArrayList;

import org.testng.annotations.DataProvider;

public class GmailRBACDataProvider {
	@DataProvider
	public static Object[][] RBACSendMailDP()
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

		ArrayList<String> inFolder1 = new ArrayList<String>();
		inFolder1.add("Sent Mail");
		

		return new Object[][] { 
			//Key, Desc,  to, cc,	bcc, infolder, internalrecipients, externalrecipients
			{ "sendPlainMailMessage1", "recieveEmailMessage1", "Send email to single internal recipient and check the activity logs", 
				internalRecipients, null, null, inFolder1, internalRecipients, null,"testuser1@securletbeatle.com"},

			{ "sendPlainMailMessage2", "recieveEmailMessage2", "Send email to single external recipient and check the activity logs",
					externalRecipients, null, null, inFolder1, null, externalRecipients,"rahulsky.java@gmail.com"},

			{ "sendPlainMailMessage3", "recieveEmailMessage3", "Send email to one single internal and external recipient and check the activity logs", 
						internalRecipients, externalRecipients, null, inFolder1, internalRecipients, externalRecipients,"2 users"},
			
			{ "sendPlainMailMessage4", "recieveEmailMessage4", "Send email to one single internal and external recipient(bcc) and check the activity logs", 
							internalRecipients, null, externalRecipients, inFolder1, internalRecipients, externalRecipients,"2 users"},
			
			{ "sendPlainMailMessage5","recieveEmailMessage5",  "Send email to one single external and internal recipient(bcc) and check the activity logs", 
								externalRecipients, null, internalRecipients, inFolder1, internalRecipients, externalRecipients,"2 users"},
			{ "sendPlainMailMessagegroup","recieveEmailMessage6",  "Send email to one  internal group recipient(bcc) and check the activity logs", 
									groupRecipients, null, null, inFolder1, groupRecipients, null,"group@securletbeatle.com"},
			{ "sendPlainMailMessagegroupExternal","recieveEmailMessage7",  "Send email to one  internal group recipient(bcc) and check the activity logs", 
										ExternalGroupRecipients, null, null, inFolder1, null, ExternalGroupRecipients,"external@infrabeatle.com"},

		};
	}

	@DataProvider
	public static Object[][] RBACSendMailkey()
	{	
		return new Object[][] {
			{ "sendPlainMailMessage1"},
			{ "sendPlainMailMessage2"},
			{ "sendPlainMailMessage3"},
			{ "sendPlainMailMessage4"},
			{ "sendPlainMailMessage5"},
			{ "sendPlainMailMessagegroup"},
			{ "sendPlainMailMessagegroupExternal"},
			
		};
	}
	
	@DataProvider
	public static Object[][] RBACRecieveMailkey()
	{	
		return new Object[][] {
			{ "recieveEmailMessage1"},
	//		{ "recieveEmailMessage2"},
			{ "recieveEmailMessage3"},
			{ "recieveEmailMessage4"},
			{ "recieveEmailMessage5"},
		//	{ "recieveEmailMessage6"},
	//		{ "recieveEmailMessage7"},
			
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
						
		};
	}

	@DataProvider
	public static Object[][] verifyMailWithRiskAttachmentDataProvider()
	{	
		return new Object[][] {
			{ "sendAttachmentMailMessage1"},
			{ "sendAttachmentMailMessage2"},
			
		};
	}
	
	@DataProvider
	public static Object[][] RBACSendMailDVCDP()
	{	
		ArrayList<String> to = new ArrayList<String>();
		to.add("testuser1@securletbeatle.com");

		ArrayList<String> internalRecipients = new ArrayList<String>();
		internalRecipients.add("gangwarvijay800@rediffmail.com");

		ArrayList<String> externalRecipients = new ArrayList<String>();
		externalRecipients.add("rahulsky.java@gmail.com");
		ArrayList<String> groupRecipients = new ArrayList<String>();
		groupRecipients.add("group@securletbeatle.com");
		ArrayList<String> ExternalGroupRecipients = new ArrayList<String>();
		ExternalGroupRecipients.add("external@infrabeatle.com");

		ArrayList<String> inFolder1 = new ArrayList<String>();
		inFolder1.add("Inbox");
		

		return new Object[][] { 
			//Key, Desc,  to, cc,	bcc, infolder, internalrecipients, externalrecipients
			{ "sendPlainMailMessageDVC1", "recieveEmailMessage1", "Send email to single internal recipient and check the activity logs", 
				internalRecipients, null, null, inFolder1, internalRecipients, null,"testuser1@securletbeatle.com"},

			{ "sendPlainMailMessageDVC2", "recieveEmailMessage2", "Send email to single external recipient and check the activity logs",
					externalRecipients, null, null, inFolder1, null, externalRecipients,"rahulsky.java@gmail.com"},
		};
	}
	@DataProvider
	public static Object[][] RBACSendMailDVCkey()
	{	
		return new Object[][] {
			{ "sendPlainMailMessageDVC1"},
			{ "sendPlainMailMessageDVC2"},
				
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
			
			{ "createDraftMessageRBAC1",  "create draft with MIME Attachment and check the activity logs", 
								internalRecipients, externalRecipients, null, inFolder1, internalRecipients, externalRecipients},

		};
	}
	
	@DataProvider
	public static Object[][] sendMailDataProviderSanityRBACDVC()
	{	
		ArrayList<String> to = new ArrayList<String>();
		to.add("testuser1@cloudsoc.guru");

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
	public static Object[][] sendAttachmentMailSanityKeyDataProvider()
	{	
		return new Object[][] {
			{ "sendTextAttachmentMailMessageSanity1"},
	
		};
	}
}
