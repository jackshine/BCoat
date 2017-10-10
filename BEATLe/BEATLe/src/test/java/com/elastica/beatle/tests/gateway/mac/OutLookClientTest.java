package com.elastica.beatle.tests.gateway.mac;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import org.testng.annotations.DataProvider;

import static org.testng.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.Reporter;

import com.elastica.beatle.S3Utils.S3ActionHandler;
import com.elastica.beatle.dci.DCIConstants;
import com.elastica.beatle.gateway.CommonConfiguration;
import com.elastica.beatle.gateway.GatewayTestConstants;
import com.elastica.beatle.gateway.LogValidator;
import com.elastica.beatle.gateway.PolicyAccessEnforcement;
import com.elastica.beatle.gateway.dto.GWForensicSearchResults;
import com.elastica.beatle.logger.Logger;
import com.elastica.beatle.replayTool.LogReplayConstant;


/*******************Author**************
 * 
 * @author Rocky
 *
 */

public class OutLookClientTest extends CommonConfiguration {

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
	
	/**
	 * 
	 * @param severity
	 * @param logFile
	 * @param expectedMsg
	 * @throws IOException
	 * @throws Exception
	 */
	 @Test(groups={"TEST"},dataProvider = "outlook_actions")
	public void verifyOutLookClientTests(String logFile, String expectedMsg) throws Exception{
		Reporter.log("Validate OutLook client for Mac P1 scenarios", true);
		
		String severity = "informational";
		
		replayLogs(logFile);
		
		data.clear();
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), " Logs does not match" ); 
		
	}
	
	@DataProvider
	public Object[][] outlook_actions() {
		return new Object[][]{
			//Severity  logFile	expectedMsg
			{"testuser1,admin,sendmail.log", "User sent an email to admin@gatewayO365beatle.com with subject test mail - sendmail without attachment"},
			{"testuser1,admin,sendmailAtt.log", "User sent an email to admin@gatewayO365beatle.com with subject test mail - sendmail with attachment and attachment(s) Test.pdf"},	//TODO: Issue: 1. Expected Logs NOT generated.
			{"admin,testuser1,receivemail.log", ""},	//Issue: 1. Expected Logs NOT generated.
			{"admin,testuser1,receivemailAtt.log", ""},	//Issue: 1. Expected Logs NOT generated.
			{"Inbox,AFolder,MoveMail.log", "User moved email(s) Inbox to AFolder"},	//Issue: 1. Expected Logs NOT generated.
			{"Inbox,AFolder,CopyMail.log", "User copied email test mail - receive mail with attachment to folder "},
			{"Inbox,DeletedItems,DeleteMail.log", ""},	//Issue: 1. Expected Logs NOT generated.
			{"DeletedItems,Inbox,MoveDeleteToInbox.log", ""},	//Issue: 1. Expected Logs NOT generated.
			{"DeletedItems,DeletePermanentlyMail.log", "User permanently deleted email(s) "},
			{"DeletedItems,Inbox,MoveDeletedToInbox.log", "User moved email(s)  to "},	//Issue: 1. Expected Logs NOT generated.
				
			{"Outlook,Mac,CreateFolder.log", ""},	//Issue: 1. no logs generated.
			{"Outlook,Mac,DeleteFolder.log", "User moved folder(s) RFolder to " },	//Issue: 1. Object Name missing. 2. one more log created.
			{"Outlook,Mac,EmptyFolder.log", "User emptied Recycle Bin"},	//Issue: 1. Creates more logs for Open Recycle Bin.
			{"Outlook,Mac,MoveFolder.log", "User moved folder(s) SFolder to " },	//Issue: 1. Object Name missing. 2. one more log created.
			{"Outlook,Mac,RenameFolder.log", "User renamed folder SFolder to SFolder"},	//Issue: 1. Expected logs NOT generated. 2. one more log created.
			
			{"Outlook,Mac,CreateSubFolder.log", ""},	//Issue: 1. Expected logs NOT generated.
			{"Outlook,Mac,DeleteSubFolder.log", "User moved folder(s) 2Folder to "},	//Issue: 1. Object Name missing. 2. one more log created.
			{"Outlook,Mac,EmptySubFolder.log", ""},	//Issue: 1. Expected logs NOT generated.
			{"Outlook,Mac,MoveSubFolder.log", "User moved folder(s) SubFolder to "},	//Issue: 1. Object Name missing. 2. one more log created.
			{"Outlook,Mac,RenameSubFolder.log", "User renamed folder 2Folder to 2Folder"},	//Issue: 1. Expected logs NOT generated. 2. one more log created.
			
			{"Outlook,Mac,SendMailAttached1MB.log", "User sent an email to admin@gatewayO365beatle.com with subject SendMail 1MB and attachment(s) GreaterThan1MB.txt"},	//Issue: one more log created for attachment. verify that.
			{"Outlook,Mac,SendMailAttached5MB.log", "User sent an email to admin@gatewayO365beatle.com with subject SendMail 5MB and attachment(s) GeaterThan5MB.txt"},	//Issue: one more log created for attachment. verify that.
			{"Outlook,Mac,SendMailAttached10MB.log", "User sent an email to admin@gatewayO365beatle.com with subject Send Mail 10MB and attachment(s) GreaterThan10MB.txt"},	//Issue: one more log created for attachment. verify that.
			{"Outlook,Mac,SendMailAttached15MB.log", "User sent an email to admin@gatewayO365beatle.com with subject Send Mail 15MB and attachment(s) GreaterThan15MB.txt"},	//Issue: one more log created for attachment. verify that.
			{"Outlook,Mac,DeleteDraftMail.log", "User copied email Discard Mail to folder "},	//Issue: 1. Object Name missing. 
			{"Outlook,Mac,DiscardMail.log", "" },	//Issue: 1. Expected logs NOT generated.
			{"Outlook,Mac,EmptyDeletedItems.log", "User permanently deleted folder(s) "},	//Issue: 1. Object name missing.
			{"Outlook,Mac,ReceiveMail.log", "User sent email invitation(s) to admin@gatewayO365beatle.com for Test.docx."},	//Issue: 1. Expected logs NOT generated.
			{"Outlook,Mac,ReceiveMailFromExternalUser.log", ""},	//Issue: 1. Expected logs NOT generated.
			
			{"Outlook,Mac,CreateCalenderEvent.log", "User created an event todo. Start time: 2016-05-02T09:30:00 05:30, End time: 2016-05-02T10:00:00 05:30, Location: on call"},
			{"Outlook,Mac,DeleteCalenderEvent.log", ""},	//Issue: 1. Expected logs NOT generated.
			
		};
	}	
}
