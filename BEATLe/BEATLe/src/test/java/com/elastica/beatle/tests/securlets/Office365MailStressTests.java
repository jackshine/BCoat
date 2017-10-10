package com.elastica.beatle.tests.securlets;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

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
import org.apache.velocity.runtime.directive.Foreach;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.SkipException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterGroups;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

import com.elastica.beatle.CommonTest;
import com.elastica.beatle.DateUtils;
import com.elastica.beatle.MarshallingUtils;
import com.elastica.beatle.TestSuiteDTO;
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
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.DriveScopes;
import com.google.gdata.client.authn.oauth.GoogleOAuthParameters;
import com.google.gdata.client.authn.oauth.OAuthException;
import com.google.gdata.client.authn.oauth.OAuthHmacSha1Signer;
import com.google.gdata.client.authn.oauth.OAuthParameters.OAuthType;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.client.spreadsheet.SpreadsheetService.*;
import com.google.gdata.data.spreadsheet.*;
import com.google.gdata.util.ServiceException;
import com.universal.common.GDriveAuthorization;
import com.universal.common.GExcelDataProvider;
import com.universal.common.Office365MailActivities;
import com.universal.constants.CommonConstants;

import microsoft.exchange.webservices.data.core.enumeration.service.DeleteMode;
import microsoft.exchange.webservices.data.core.exception.service.local.ServiceLocalException;
import microsoft.exchange.webservices.data.core.service.item.EmailMessage;
import microsoft.exchange.webservices.data.property.complex.EmailAddress;
import microsoft.exchange.webservices.data.property.complex.EmailAddressCollection;

import org.testng.asserts.SoftAssert;

import com.elastica.beatle.tests.securlets.SecurletsUtils;

import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import org.testng.annotations.Parameters;

/**
 * @author Nissar
 *
 */
public class Office365MailStressTests extends CommonTest {
	ESQueryBuilder esQueryBuilder = null;
	LogValidator logValidator;
	CIQValidator ciqValidator;
	Office365MailActivities objMailAdmin;
	Office365MailActivities objMailTestUser1;
	Office365MailActivities objMailExternalUser;
	private String destinationFile;
	private String renamedFile;
	private String query;
	ArrayList<String> cleanupListSent = new ArrayList<String>();
	ArrayList<String> cleanupListReceived = new ArrayList<String>();
	ArrayList<String> cleanupListDrafts = new ArrayList<String>();
	ArrayList<File> filesToDelete = new ArrayList<File>();

	protected enum Remediation {
		ITEM_DELETE_ATTACHMENT, ITEM_DELETE_MAIL_BY_ATTACHMENT, ITEM_DELETE_MAIL
	};

	SoftAssert softAssert = new SoftAssert();

	String resourceId;
	String userDir;
	String logDir;

	// Filenames, mail subjects used across test cases
	public static final long MAX_LOG_WAIT_TIME_IN_MINUTES = 25;
	public static final long MAX_EXPOSURE_WAIT_TIME_IN_MINUTES = 10;
	public static final long MAX_REMEDIATION_WAIT_TIME_IN_MINUTES = 10;

	public long draftCount = 0;
	public long inboxCount = 0;
	public long sentItemsCount = 0;
	public long bouncedCount = 0;
	public long totalCount = 0;
	public long passedCount = 0;
	public long passedCountDraft = 0;
	public long passedCountMail = 0;
	public long failedCount = 0;
	public long failedCountDraft = 0;
	public long failedCountMail = 0;
	public int userRangeStart = 0;
	public int userRangeEnd = 0;
	public int mailsPerUser = 0;
	public int draftsPerUser = 0;
	public SpreadsheetEntry spreadsheetEntry = null;
	public SpreadsheetService service = null;

	TreeMap<String, Long> resultGlobal = new TreeMap<String, Long>();

	String userPrefix = null;
	String userSuffix = null;
	String fileForMailBody = null;
	String contentLocationBothBody = null;
	String userPassword = null;
	String changeMailSubject = null;
	String changeDraftSubject = null;
	String jobName = "";
	int jobNumber = 0;
	int mailAttachmentCount = 0, draftAttachmentCount = 0;
	ArrayList<String> mailAttachments = new ArrayList<String>();
	ArrayList<String> draftAttachments = new ArrayList<String>();
	String bouncedMailSubject = "";
	String mailIdTo = "", mailIdCC = "", mailIdBCC = "", fromTime="", toTime="";

	EmailAddressCollection toEmailIds = new EmailAddressCollection();
	EmailAddressCollection ccEmailIds = new EmailAddressCollection();
	EmailAddressCollection bccEmailIds = new EmailAddressCollection();

	Date curDate = new Date();
	SimpleDateFormat format = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
	String DateString = format.format(curDate);
	String jobStatus = "";

	public Office365MailStressTests() throws Exception {
		super();

		esQueryBuilder = new ESQueryBuilder();
		logValidator = new LogValidator();
		// setting upload file path
		userDir = System.getProperty("user.dir") + "/src/test/resources/uploads/office365/";
		logDir = System.getProperty("user.dir") + "/target/logs/";

	}

	@BeforeClass
	public void setup() throws Exception {

		jobName = super.getRegressionSpecificSuitParameters("sheetName");
		
		GExcelDataProvider excelDataProvider_input = new GExcelDataProvider(
				"1LLOt60I_XQZfXHXUOiMZXn-ux_ZblhuzSJSDQXLgFnQ");// 1Mj9DMUgA9mReXft880Fla2EwNDniakPbG5u6ZnFLY7k
		List<Map<String, Object>> stressInput = excelDataProvider_input.getDataAsMapList(jobName, "Param Name",
				"Param Value");

		for (Map<String, Object> sheetData : stressInput) {
			String stressParam = sheetData.get("Param Name").toString();

			if (stressParam.contentEquals("UserRangeStart")) {
				userRangeStart = Integer.parseInt(sheetData.get("Param Value").toString());
			} else if (stressParam.contentEquals("UserRangeEnd")) {
				userRangeEnd = Integer.parseInt(sheetData.get("Param Value").toString());
			} else if (stressParam.contentEquals("MailsPerUser")) {
				mailsPerUser = Integer.parseInt(sheetData.get("Param Value").toString());
			} else if (stressParam.contentEquals("DraftsPerUser")) {
				draftsPerUser = Integer.parseInt(sheetData.get("Param Value").toString());
			} else if (stressParam.contentEquals("UserPrefix")) {
				userPrefix = sheetData.get("Param Value").toString();
			} else if (stressParam.contentEquals("UserSuffix")) {
				userSuffix = sheetData.get("Param Value").toString();
			} else if (stressParam.contentEquals("UserPassword")) {
				userPassword = sheetData.get("Param Value").toString();
			} else if (stressParam.contentEquals("MailSubject")) {
				changeMailSubject = sheetData.get("Param Value").toString();
			} else if (stressParam.contentEquals("DraftSubject")) {
				changeDraftSubject = sheetData.get("Param Value").toString();
			} else if (stressParam.contentEquals("BouncedMailSubject")) {
				bouncedMailSubject = sheetData.get("Param Value").toString();
			} else if (stressParam.contentEquals("FileForMailBody")) {
				fileForMailBody = sheetData.get("Param Value").toString();
			} else if (stressParam.contentEquals("MailAttachmentCount")) {
				mailAttachmentCount = Integer.parseInt(sheetData.get("Param Value").toString());
			} else if (stressParam.contentEquals("DraftAttachmentCount")) {
				draftAttachmentCount = Integer.parseInt(sheetData.get("Param Value").toString());
			} else if (stressParam.contentEquals("ToEmailIds")) {
				mailIdTo = sheetData.get("Param Value").toString();
			} else if (stressParam.contentEquals("CCEmailIds")) {
				mailIdCC = sheetData.get("Param Value").toString();
			} else if (stressParam.contentEquals("BCCEmailIds")) {
				mailIdBCC = sheetData.get("Param Value").toString();
			} else if (stressParam.contentEquals("fromTime")) {
				fromTime = sheetData.get("Param Value").toString();
			} else if (stressParam.contentEquals("toTime")) {
				toTime = sheetData.get("Param Value").toString();
			}

		}

		// getting mail attachments

		if (mailAttachmentCount > 0) {

			for (Map<String, Object> sheetData : stressInput) {
				String stressParam = sheetData.get("Param Name").toString();

				for (int i = 1; i <= mailAttachmentCount; i++) {
					if (stressParam.contains("MailAttachment" + i)) {
						mailAttachments.add(sheetData.get("Param Value").toString());
					}
				}

			}
		}

		// getting draft attachments
		if (draftAttachmentCount > 0) {

			for (Map<String, Object> sheetData : stressInput) {
				String stressParam = sheetData.get("Param Name").toString();

				for (int i = 1; i <= draftAttachmentCount; i++) {
					if (stressParam.contains("DraftAttachment" + i)) {
						draftAttachments.add(sheetData.get("Param Value").toString());
					}
				}

			}
		}

		if (mailIdTo.equals("self") || mailIdTo.equals("") || mailIdTo.equals(null)) {
			toEmailIds = null;
		} else {
			String[] tempArray = mailIdTo.split(",");
			for (String mailid1 : tempArray) {
				Logger.info(mailid1);
				toEmailIds.add(mailid1);
			}

		}

		if (mailIdCC.equals(null) || mailIdCC.equals("")) {
			ccEmailIds = null;
		} else {
			String[] tempArray = mailIdCC.split(",");
			for (String mailid2 : tempArray) {
				Logger.info(mailid2);
				ccEmailIds.add(mailid2);
			}

		}

		if (mailIdBCC.equals(null) || mailIdBCC.equals("")) {
			bccEmailIds = null;
		} else {
			String[] tempArray = mailIdBCC.split(",");
			for (String mailid3 : tempArray) {
				Logger.info(mailid3);
				bccEmailIds.add(mailid3);
			}

		}

		if (jobName.equals("CloudSocMailCount")==false && ((userRangeStart == 0) || (userRangeEnd == 0))) {
			Reporter.log("Please pass the parameter userRangeStart and userRangeEnd values", true);
			System.exit(0);
		}

		if (userRangeStart > userRangeEnd) {
			Reporter.log("The parameter userRangeEnd should be greater than  userRangeStart value", true);
			System.exit(0);
		}

		if (jobName.equals("TestAccountCleanupJob") == false && jobName.equals("TestAccountMailCount") == false && jobName.equals("CloudSocMailCount") == false) {
			
			jobNumber = Integer.parseInt(jobName.split("StressTestJob")[1]);
			Reporter.log("jobName=" + jobName + "jobNumber=" + jobNumber, true);
			
			if ((mailsPerUser == 0)) {
				Reporter.log("Please pass the parameter mailsPerUser values", true);
				System.exit(0);
			}
		}

		Logger.info("jobName=" + jobName + " userRangeStart=" + userRangeStart + " userRangeEnd=" + userRangeEnd
				+ " mailsPerUser=" + mailsPerUser + " draftsPerUser=" + draftsPerUser);
		Logger.info("userPrefix=" + userPrefix + " userSuffix=" + userSuffix + " userPassword=" + userPassword
				+ " changeMailSubject=" + changeMailSubject + " changeDraftSubject=" + changeDraftSubject
				+ " bouncedMailSubject=" + bouncedMailSubject);
		Logger.info("mailAttachmentCount=" + mailAttachmentCount + " draftAttachmentCount=" + draftAttachmentCount
				+ " fileForMailBody=" + fileForMailBody);

		for (int i = 0; i < mailAttachments.size(); i++) {
			Logger.info(mailAttachments.get(i));

		}
		for (int i = 0; i < draftAttachments.size(); i++) {
			Logger.info(draftAttachments.get(i));

		}
		
		if(jobName.equals("TestAccountMailCount")){
			
		}

		initializeGoogleSheet();

		// userPrefix ="qa-stress"; //This is the prefix part of the user email
		// id e.g <qa-stress>1@o365security.net
		// userSuffix ="@o365security.net"; //This is the suffix part(domain) of
		// the user email id e.g qa-stress1<@o365security.net>
		// userPassword = "Aut0mat10n#123"; //Password of the user
		// Below is the file which contains the risk content which needs to be
		// included in the body of email.
		// file path /BEATLe/src/test/resources/uploads/office365/
		// fileForMailBody="PCI_Test.txt";

		// changeMailSubject = "Please find your documents";
		// changeDraftSubject = "Draft";

		// Below array contains the list of attachments to be included in the
		// mail to be sent.
		// File path /BEATLe/src/test/resources/uploads/office365/
		// mailAttachments.add("PII.rtf");
		// mailAttachments.add("PCI_Test.txt");
		// mailAttachments.add("hipaa.txt");
		// mailAttachments.add("ferpa.pdf");
		// mailAttachments.add("Hello.java");
		// mailAttachments.add("encryption.bin");
		// mailAttachments.add("vba_macro.xls");
		// mailAttachments.add("glba.txt");
		// mailAttachments.add("virus.zip");

		// Below array contains the list of attachments to be included in the
		// mail to be saved in draft.
		// File path /BEATLe/src/test/resources/uploads/office365/
		// draftAttachments.add("PII.rtf");
		// draftAttachments.add("PCI_Test.txt");
		// draftAttachments.add("hipaa.txt");
		// draftAttachments.add("Hello.java");
		// draftAttachments.add("encryption.bin");
		// draftAttachments.add("vba_macro.xls");
		// draftAttachments.add("glba.txt");
		// draftAttachments.add("virus.zip");

		// getting internal recipient list
		// toEmailIds = null; //If the value for toEmailIds is mentioned as
		// null, then each user will send mail to self. This option is only for
		// toEmailIds and not for cc, bcc
		// ccEmailIds.add("invalidEmailId@elasticaqa.net"); // sending to
		// external tenant, mentioning invalid id
		// bccEmailIds=null;

		// Way to mention other users in recipient list
		// But be cautious to use this as it might hit recipients daily mail
		// limit in minutes, as all will send mail to this id.
		// toEmailIds.add("qa-stress1@o365security.net");//if we want to send to
		// other email ids we can mention it like this
		// toEmailIds = getInternalRecepientList(1, 5); //if we want a range of
		// users in recipient list, use this way.

		// example for getting external recipient list in range
		// ccEmailIds= getExternalRecepientList(1, 2);
		// ccEmailIds.add("DistGroup5001-5100@o365security.net");

		// Below variable stores the bounced mail subject for the mails sent to
		// invalidIds (for external user), which needs to be deleted from mail
		// box
		// bouncedMailSubject = "Delivery Status Notification (Failure)"; //for
		// google recipient
		// bouncedMailSubject = "Undeliverable"; // for microsoft recipient

	}

	@Test
	public void temp() throws IOException, ServiceException, OAuthException, GeneralSecurityException {

//		for (int i = 0; i <= 10; i++) {
//
//			saveInGoogleSheet(passFailCount(true, "send_mail"), jobNumber);
//		}
	}

	public void initializeGoogleSheet() throws IOException, ServiceException, OAuthException, GeneralSecurityException {

		final String CLIENT_ID = "812119916773-hb47rkktb3p8appsc93cr8tokau0r1gs.apps.googleusercontent.com";
		final String CLIENT_SECRET = "iSZQfwpG9RP8dKdusaevG1y6";
		final String REFRESH_TOCKEN = "1/BQvlCPButN8CWo4PKvLMeOHeaNS0i_EFJg_s6MfF_zM";
		String googleSheetName = "O365_StressTest_Results.xls";

		// OAuth2Sample oauth = new OAuth2Sample();
		// oauth.loginOAuth2(CLIENT_ID, CLIENT_SECRET);

		final String REDIRECT_URI = "urn:ietf:wg:oauth:2.0:oob";
		GDriveAuthorization gsheet = new GDriveAuthorization(CLIENT_ID, CLIENT_SECRET);
		service = new SpreadsheetService("SecurletQAMail");
		service.setOAuth2Credentials(gsheet.getCredentialsFromRefreshAccessToken(REFRESH_TOCKEN));

		// Define the URL to request. This should never change.
		URL SPREADSHEET_FEED_URL = new URL("https://spreadsheets.google.com/feeds/spreadsheets/private/full");

		// Make a request to the API and get all spreadsheets.
		SpreadsheetFeed feed = service.getFeed(SPREADSHEET_FEED_URL, SpreadsheetFeed.class);
		List<SpreadsheetEntry> spreadsheets = feed.getEntries();

		// Iterate through all of the spreadsheets returned
		for (SpreadsheetEntry spreadsheet : spreadsheets) {
			// Print the title of this spreadsheet to the screen
			if (spreadsheet.getTitle().getPlainText().equals(googleSheetName)) {
				System.out.println(spreadsheet.getTitle().getPlainText());
				spreadsheetEntry = spreadsheet;

			}
		}
	}
	
	public void saveInGoogleSheet(HashMap<String, Long> progressCount, int jobNum)
			throws IOException, ServiceException, OAuthException, GeneralSecurityException {
		
		URL worksheetFeedUrl = spreadsheetEntry.getWorksheetFeedUrl();
		WorksheetFeed worksheetFeed = service.getFeed(worksheetFeedUrl, WorksheetFeed.class);
		
		List<WorksheetEntry> worksheets = worksheetFeed.getEntries();
		WorksheetEntry worksheet = worksheets.get(0);
		
		// Fetch the list feed of the worksheet.
		URL listFeedUrl = worksheet.getListFeedUrl();
		ListFeed listFeed = service.getFeed(listFeedUrl, ListFeed.class);
		
		// TODO: Choose a row more intelligently based on your app's needs.
		ListEntry row = listFeed.getEntries().get(jobNum - 1);
		// ListEntry row = new ListEntry();
		
		Reporter.log("==============================================", true);
		Iterator it = progressCount.entrySet().iterator();
		while (it.hasNext()) {
			
			Map.Entry pair = (Map.Entry) it.next();
			Reporter.log(pair.getKey() + " = " + pair.getValue());
			row.getCustomElements().setValueLocal(pair.getKey().toString(), pair.getValue().toString());
		}
		Reporter.log("==============================================", true);
		
		// Update the row's data.
		// row.getCustomElements().setValueLocal("lastname", "Hunt");
		// row.getCustomElements().setValueLocal("age", "32");
		// row.getCustomElements().setValueLocal("height", "154");
		
		// Save the row using the API.
		row.update();
		
		// row = service.insert(listFeedUrl, row);
		
		// URL worksheetFeedUrl= spreadsheetEntry.getWorksheetFeedUrl ();
		// WorksheetFeed worksheetFeed= service.getFeed (
		// worksheetFeedUrl, WorksheetFeed.class);
		//
		//
		// List <WorksheetEntry> worksheetEntrys= worksheetFeed.getEntries ();
		// WorksheetEntry worksheetEntry= worksheetEntrys.get (0);
		//
		// // Write header line into Spreadsheet
		// URL cellFeedUrl= worksheetEntry.getCellFeedUrl ();
		// CellFeed cellFeed= service.getFeed (cellFeedUrl,
		// CellFeed.class);
		//
		// CellEntry cellEntry= new CellEntry (1, 1, "headline1");
		// cellFeed.insert (cellEntry);
		// cellEntry= new CellEntry (1, 2, "headline2");
		// cellFeed.insert (cellEntry);
	}

	public void saveInGoogleSheetCloudsocCount(HashMap<String, Long> progressCount)
			throws IOException, ServiceException, OAuthException, GeneralSecurityException {

		URL worksheetFeedUrl = spreadsheetEntry.getWorksheetFeedUrl();
		WorksheetFeed worksheetFeed = service.getFeed(worksheetFeedUrl, WorksheetFeed.class);

		List<WorksheetEntry> worksheets = worksheetFeed.getEntries();
		WorksheetEntry worksheet = worksheets.get(1);

		// Fetch the list feed of the worksheet.
		URL listFeedUrl = worksheet.getListFeedUrl();
		ListFeed listFeed = service.getFeed(listFeedUrl, ListFeed.class);

		// TODO: Choose a row more intelligently based on your app's needs.
		ListEntry row = listFeed.getEntries().get(1);
		// ListEntry row = new ListEntry();

		Reporter.log("==============================================", true);
		Iterator it = progressCount.entrySet().iterator();
		while (it.hasNext()) {

			Map.Entry pair = (Map.Entry) it.next();
			Reporter.log(pair.getKey() + " = " + pair.getValue());
			row.getCustomElements().setValueLocal(pair.getKey().toString(), pair.getValue().toString());
		}
		Reporter.log("==============================================", true);

		// Update the row's data.
		// row.getCustomElements().setValueLocal("lastname", "Hunt");
		// row.getCustomElements().setValueLocal("age", "32");
		// row.getCustomElements().setValueLocal("height", "154");

		// Save the row using the API.
		row.update();

		// row = service.insert(listFeedUrl, row);

		// URL worksheetFeedUrl= spreadsheetEntry.getWorksheetFeedUrl ();
		// WorksheetFeed worksheetFeed= service.getFeed (
		// worksheetFeedUrl, WorksheetFeed.class);
		//
		//
		// List <WorksheetEntry> worksheetEntrys= worksheetFeed.getEntries ();
		// WorksheetEntry worksheetEntry= worksheetEntrys.get (0);
		//
		// // Write header line into Spreadsheet
		// URL cellFeedUrl= worksheetEntry.getCellFeedUrl ();
		// CellFeed cellFeed= service.getFeed (cellFeedUrl,
		// CellFeed.class);
		//
		// CellEntry cellEntry= new CellEntry (1, 1, "headline1");
		// cellFeed.insert (cellEntry);
		// cellEntry= new CellEntry (1, 2, "headline2");
		// cellFeed.insert (cellEntry);
	}

	@SuppressWarnings("null")
	@DataProvider(parallel = true)
	public Object[][] dataProviderMailDetails() {

		int threadCount = 0, arrStartRange[], arrEndRange[];
		Object[] result = createUserRangeForThreads();

		threadCount = (int) result[0];
		arrStartRange = (int[]) result[1];
		arrEndRange = (int[]) result[2];

		Object obj[][];
		obj = new Object[(threadCount * 2)][];

		for (int i = 0; i < (threadCount * 2); i += 2) {
			// mail_action, identifier_keyword, user_starting_range,
			// user_ending_range, contentLocation, attachmentList,
			// number_of_mails_per_sender(or drafts), to_email_list,
			// cc_email_list, bcc_email_list
			obj[i] = new Object[] { "send_mail", "all", arrStartRange[(i / 2)], arrEndRange[(i / 2)], "both",
					mailAttachments, mailsPerUser, toEmailIds, ccEmailIds, bccEmailIds };
			obj[i + 1] = new Object[] { "save_indrafts", "draft", arrStartRange[(i / 2)], arrEndRange[(i / 2)], "both",
					draftAttachments, draftsPerUser, null, null, null };
		}

		return obj;
		// return new Object[][]{
		// mail_action, identifier_keyword, user_starting_range,
		// user_ending_range, contentLocation, attachmentList,
		// number_of_mails_per_sender, to_email_list, cc_email_list,
		// bcc_email_list

		// {"send_mail", "all", 9999, 10000, "both", mailAttachments, 40, null,
		// getExternalRecepientList(1, 1), null},
		// {"save_indrafts", "draft", 9999, 10000, "both", draftAttachments, 25,
		// null, null, null},
		// };
	}

	@SuppressWarnings("null")
	@Test(dataProvider = "dataProviderMailDetails", groups = { "mails" })
	public void sendMail(String mailAction, String identifierKeyword, int userStartingRange, int userEndingRange,
			String contentLocation, ArrayList<String> mailAttachments, int noOfMailsPerSender,
			EmailAddressCollection toEmailIds, EmailAddressCollection ccEmaiIds, EmailAddressCollection bccEmailIds)
					throws IOException, InterruptedException {

		String userName;
		String recipientType = "";
		String identifier = null;
		String fileName = null;
		String testRailNo;
		String violationName;
		String contentPlace;
		boolean success = false;
		// generating unique id
		String newFileName = null;
		File sourceFile1;
		File destFile1 = null;
		String mailBody = null;
		ArrayList<String> renamedAttachments = new ArrayList<String>();
		String mailSubject = null;
		ArrayList<String> logs = null;
		ArrayList<String> logs2 = null;

		String LogFile = null;
		EmailAddressCollection toRecipientList = null;

		for (int currUserNumber = userStartingRange; currUserNumber <= userEndingRange; currUserNumber++) { // for
																											// total
																											// number
																											// of
																											// users

			// forming the sender email id
			userName = userPrefix + Integer.toString(currUserNumber) + userSuffix;
			// Thread t = Thread.currentThread();
			// Reporter.log("Thread="+t.getName(),true);
			Reporter.log("==============================================", true);
			Reporter.log("Started Processing for user:" + userName, true);
			Reporter.log("==============================================", true);

			// creating mail object with user credentials
			Office365MailActivities objMail = new Office365MailActivities(userName, userPassword);

			for (int repeat = 1; repeat <= noOfMailsPerSender; repeat++) { // repetition
																			// of
																			// sending
																			// mail
																			// (
																			// how
																			// many
																			// times
																			// the
																			// same
																			// mail
																			// should
																			// be
																			// sent)
				try {
					renamedAttachments = new ArrayList<String>();
					// forming identifier for mail subject and attachment name
					identifier = identifierKeyword + "_" + userPrefix.replace("-", "_")
							+ Integer.toString(currUserNumber) + "_" + repeat + "_";
					Reporter.log("Identifier=" + identifier, true);

					if (contentLocation.equals("attachment")) {
						if (mailAttachments != null) {
							for (String eachFile : mailAttachments) {

								// code to create attachment with a unique name
								sourceFile1 = new File(userDir + eachFile);
								newFileName = identifier + eachFile;
								destFile1 = new File(userDir + newFileName);
								copyFileUsingFileChannels(sourceFile1, destFile1);
								renamedAttachments.add(destFile1.toString());
								filesToDelete.add(destFile1);

								// code to for attachment without changing name
								renamedAttachments.add(userDir + eachFile);
							}
						}
						mailSubject = identifier + "with_attachments";
						mailBody = "This is test mail body";
						LogFile = DateString + "_attachment";

					} else if (contentLocation.equals("body")) {
						mailSubject = identifier + "body";
						mailBody = readFile(userDir + fileForMailBody, Charset.defaultCharset());
						LogFile = DateString + "_body";
					} else if (contentLocation.equals("both")) {

						if (mailAttachments != null) {
							for (String eachFile : mailAttachments) {

								// code to create attachment with a unique name
								sourceFile1 = new File(userDir + eachFile);
								newFileName = identifier + eachFile;
								destFile1 = new File(userDir + newFileName);
								copyFileUsingFileChannels(sourceFile1, destFile1);
								renamedAttachments.add(destFile1.toString());
								filesToDelete.add(destFile1);

								// code to for attachment without changing name
								renamedAttachments.add(userDir + eachFile);
							}
						}

						mailSubject = identifier + "both";
						mailBody = readFile(userDir + fileForMailBody, Charset.defaultCharset());
						LogFile = DateString + "_both";

					}

					// Reporter.log("mail subject="+mailSubject,true);
					// Reporter.log("attachments="+renamedAttachments.toString(),true);

					// Thread.sleep(10000);
					// cleanupListSent.add(mailSubject);
					if (mailAction.equals("send_mail")) {

						// if toEmailIds is null, send mail to self
						if (toEmailIds == null) {
							toRecipientList = new EmailAddressCollection();
							toRecipientList.add(userName);
						} else {
							toRecipientList = toEmailIds;
						}
						 success = objMail.sendMailWithCCAndBCC(toRecipientList,ccEmaiIds, bccEmailIds, changeMailSubject, mailBody, renamedAttachments, true);
						Thread.sleep(5000);
					} else if (mailAction.equals("save_indrafts")) {
						mailSubject += "Draft";
						success = objMail.addAttachmentAndSaveInDraft(changeDraftSubject, mailBody,renamedAttachments);
					}
					String result;
					if (success) {
						LogFile = LogFile + "_passed";
						result = "Passed";
						// passFailCount(true);
						passFailCount(true, mailAction);
					} else {
						LogFile = LogFile + "_failed";
						result = "Failed";
						// passFailCount(false);
						passFailCount(false, mailAction);
					}
					Reporter.log(mailSubject + " " + result, true);
					writeToFile(LogFile, mailSubject);
					// assertTrue(success, "Failed sending mail with
					// subject:"+mailSubject);
					success = false;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					Reporter.log(e.getMessage());
					e.printStackTrace();
				}
			}
			// Thread.sleep(20000);
			// Delete all the bounced mails
			objMail.deleteAllMailsWithSubject(bouncedMailSubject, "Inbox", DeleteMode.HardDelete);
			objMail = null;
		}

	}

	@AfterTest
	public void deleteFiles() throws InterruptedException {
		deleteFiles(filesToDelete);
	}

	@DataProvider(parallel = true)
	public Object[][] dataProviderCleanupAccountEmails() {

		int threadCount = 0, arrStartRange[], arrEndRange[];
		Object[] result = createUserRangeForThreads();

		threadCount = (int) result[0];
		arrStartRange = (int[]) result[1];
		arrEndRange = (int[]) result[2];

		Object obj[][];
		obj = new Object[(threadCount)][];

		for (int i = 0; i < (threadCount); i++) {
			// user_starting_range, user_ending_range
			obj[i] = new Object[] { arrStartRange[(i)], arrEndRange[(i)] };
		}

		return obj;

		// return new Object[][]{
		// user_starting_range, user_ending_range empty_folder_name
		// { 1 , 100, "SentItems"},
		// { 2501, 2600, "Inbox"},
		// { 1, 25, "Drafts"},
		// { 5001, 5100, "Inbox"},
		// { 10000, 10000, "SentItems"},
		// { 2, 2, "Drafts"},
		// { 2, 2, "SentItems"}
		// };
	}

	@Test(dataProvider = "dataProviderCleanupAccountEmails", groups = { "mails" })
	public void cleanupAccountEmails(int userStartingRange, int userEndingRange)
			throws IOException, InterruptedException {

		// String userPrefix ="qa-stress";
		// String userSuffix ="@o365security.net";
		String userName;
		// String userPassword = "Aut0mat10n#123";
		String identifier = null;
		boolean success = false;
		// generating unique id
		String LogFile = DateString + "AccountCleanupResult";
		String result;

		for (int currUserNumber = userStartingRange; currUserNumber <= userEndingRange; currUserNumber++) { // for
																											// total
																											// number
																											// of
																											// users

			// forming the user email id
			userName = userPrefix + Integer.toString(currUserNumber) + userSuffix;
			Reporter.log("==============================================", true);
			Reporter.log("Started Processing for user:" + userName, true);
			Reporter.log("==============================================", true);

			// creating mail object with admin user credentials
			Office365MailActivities objMail = new Office365MailActivities(userName, userPassword);

			try {
				// forming identifier
				identifier = userPrefix.replace("-", "_") + Integer.toString(currUserNumber) + "_";
				Reporter.log("Identifier=" + identifier, true);

				// Thread.sleep(10000);
				// cleanupListSent.add(mailSubject);
				synchronized (this) {
					success = objMail.cleanupAccountMails();
				}

				if (success) {
					result = "Passed";
					passFailCount(true);
				} else {
					result = "Failed";
					passFailCount(false);
				}
				success = false;
				Reporter.log(userName + " " + result, true);
				writeToFile(LogFile, userName + " " + result);
				// assertTrue(success, "Failed sending mail with
				// subject:"+mailSubject);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				Reporter.log(e.getMessage());
				e.printStackTrace();
			}
		}

	}

	Object[] createUserRangeForThreads() {

		int userCount = (userRangeEnd - userRangeStart) + 1;

		int threadCount = 0;
		Object[] resultObj;

		HashMap<Long, Long> range = new HashMap<Long, Long>();

		Reporter.log("start=" + userRangeStart, true);
		Reporter.log("end=" + userRangeEnd, true);
		Reporter.log("user count=" + userCount, true);
		Reporter.log("mode=" + userCount % 10, true);
		Reporter.log("range=" + userCount / 10, true);

		int increment = userCount / 10;
		int mod = userCount % 10;

		int arrStartRange[];
		int arrEndRange[];
		int startRange = 0, endRange = 0;

		if (userCount < 10) {
			increment = 1;
			threadCount = (int) userCount;
			arrStartRange = new int[threadCount];
			arrEndRange = new int[threadCount];
			// System.out.println(userRangeStart+"-"+userRangeEnd);

		} else {
			threadCount = 10;
			arrStartRange = new int[10];
			arrEndRange = new int[10];
		}

		for (int i = 1; i <= threadCount; i++) {
			if (i == 1) {
				startRange = userRangeStart;
			} else {
				startRange += increment;
			}

			endRange = startRange + (increment - 1);

			if (i == threadCount && userCount > 10) {
				endRange += mod;
			}
			// count[i] = userRangeStart+temp ;
			System.out.println(startRange + "-" + endRange);

			arrStartRange[i - 1] = startRange;
			arrEndRange[i - 1] = endRange;
		}
		resultObj = new Object[] { threadCount, arrStartRange, arrEndRange };
		return resultObj;

	}

	@SuppressWarnings("null")
	@DataProvider(parallel = true)
	public Object[][] dataProviderCount() {

		int threadCount = 0, arrStartRange[], arrEndRange[];
		Object[] result = createUserRangeForThreads();

		threadCount = (int) result[0];
		arrStartRange = (int[]) result[1];
		arrEndRange = (int[]) result[2];

		Object obj[][];
		obj = new Object[(threadCount)][];

		for (int i = 0; i < (threadCount); i++) {
			// mail subject user_starting_range, user_ending_range
			obj[i] = new Object[] { changeMailSubject, arrStartRange[(i)], arrEndRange[(i)] };
		}

		return obj;

		// return new Object[][]{

		// {changeMailSubject, 1544, 1544 },
		// {changeMailSubject, 1798, 1805 },
		// {changeMailSubject, 1889, 1889 },
		// {changeMailSubject, 2678, 2678 },
		// {changeMailSubject, 2761, 2761 },
		// {changeMailSubject, 3157, 3157 },
		// {changeMailSubject, 3162, 3162 },
		// {changeMailSubject, 3363, 3363 },
		// {changeMailSubject, 3370, 3370 },
		// {changeMailSubject, 3396, 3396 },
		// {changeMailSubject, 3775, 3775 },
		// {changeMailSubject, 3890, 3890 },
		// {changeMailSubject, 3981, 3981 },
		// {changeMailSubject, 4093, 4093 },
		// {changeMailSubject, 4268, 4268 },
		// {changeMailSubject, 4579, 4579 },
		// {changeMailSubject, 4797, 4797 },
		// {changeMailSubject, 5738, 5738 },
		// {changeMailSubject, 6656, 6656 },
		// {changeMailSubject, 8509, 8509 },
		// {changeMailSubject, 9249, 9249 },
		// {changeMailSubject, 9715, 9715 },
		// {changeMailSubject, 9785, 9785 },
		// {changeMailSubject, 9807, 9807 }

		// {changeMailSubject, 5274, 5274 },
		// {changeMailSubject, 5100, 5100 },

		// };
	}

	@Test(dataProvider = "dataProviderCount", groups = { "count" })
	public void searchInFolder(String subject, int userStartingRange, int userEndingRange)
			throws IOException, InterruptedException {

		// String userPrefix ="qa-stress";
		// String userSuffix ="@o365security.net";
		String userName;
		// String userPassword = "Aut0mat10n#123";

		long countInbox = 0;
		long countSent = 0;
		long countDrafts = 0;
		long countBouncedMails = 0;

		for (int currUserNumber = userStartingRange; currUserNumber <= userEndingRange; currUserNumber++) { // for
																											// total
																											// number
																											// of
																											// users

			userName = userPrefix + Integer.toString(currUserNumber) + userSuffix;

			try {
				Office365MailActivities objMail = new Office365MailActivities(userName, userPassword);

				countInbox = objMail.searchItemAndReturnCount(changeMailSubject, "Inbox");
				countSent = objMail.searchItemAndReturnCount(changeMailSubject, "SentItems");
				countDrafts = objMail.searchItemAndReturnCount(changeDraftSubject, "Drafts");
				countBouncedMails = objMail.searchItemAndReturnCount(bouncedMailSubject, "Inbox");

				showCount(countInbox, countSent, countDrafts, countBouncedMails);
				objMail = null;

			} catch (Exception e) {
				// TODO Auto-generated catch block
				Reporter.log(e.getLocalizedMessage(), true);
			}
		}

	}

	public synchronized void passFailCount(boolean pass) {
		if (pass) {
			passedCount += 1;
		} else {
			failedCount += 1;
		}
		Reporter.log("==============================================", true);
		Reporter.log("Passed sofar=" + passedCount, true);
		Reporter.log("Failed sofar=" + failedCount, true);
		Reporter.log("==============================================", true);

	}

	public synchronized HashMap<String, Long> passFailCount(boolean pass, String typeOfMail)
			throws IOException, ServiceException, OAuthException, GeneralSecurityException {

		HashMap<String, Long> progressCount = new HashMap<String, Long>();
		long inboxMailCount = 0, sentItemMailCount = 0, inboxAttachmentCount = 0, sentItemAttachmentCount = 0,
				draftItemMailCount = 0, draftItemAttachmentCount;
		if (pass) {
			if (typeOfMail.equals("send_mail")) {
				passedCountMail += 1;
			} else {
				passedCountDraft += 1;
			}
		} else {
			if (typeOfMail.equals("send_mail")) {
				failedCountMail += 1;
			} else {
				failedCountDraft += 1;
			}
		}

		inboxMailCount = passedCountMail;
		sentItemMailCount = passedCountMail;
		draftItemMailCount = passedCountDraft;
		inboxAttachmentCount = passedCountMail * mailAttachmentCount;
		sentItemAttachmentCount = passedCountMail * mailAttachmentCount;
		draftItemAttachmentCount = passedCountDraft * draftAttachmentCount;

		progressCount.put("PassedMailSofar", passedCountMail);
		progressCount.put("PassedDrafSofar", passedCountDraft);
		progressCount.put("FailedMailSofar", failedCountMail);
		progressCount.put("FailedDraftSofar", failedCountDraft);
		progressCount.put("InboxMailCount", inboxMailCount);
		progressCount.put("InboxAttachmentCount", inboxAttachmentCount);
		progressCount.put("SentItemMailCount", sentItemMailCount);
		progressCount.put("SentItemAttachmentCount", sentItemAttachmentCount);
		progressCount.put("DraftItemMailCount", draftItemMailCount);
		progressCount.put("DraftItemAttachmentCount", draftItemAttachmentCount);
		progressCount.put("TotalMailboxItemCount", (inboxMailCount + inboxAttachmentCount + sentItemMailCount
				+ sentItemAttachmentCount + draftItemMailCount + draftItemAttachmentCount));

		Reporter.log("==============================================", true);
		Iterator it = progressCount.entrySet().iterator();
		while (it.hasNext()) {

			Map.Entry pair = (Map.Entry) it.next();
			Reporter.log(pair.getKey() + " = " + pair.getValue(),true);
		}
		Reporter.log("==============================================", true);

		saveInGoogleSheet(progressCount, jobNumber);

		// Reporter.log("==============================================",true);
		// Reporter.log("PassedMailSofar="+passedCountMail, true);
		// Reporter.log("PassedDrafSofar="+passedCountDraft, true);
		// Reporter.log("FailedMailSofar="+failedCountMail, true);
		// Reporter.log("FailedDraftSofar="+failedCountDraft, true);
		// Reporter.log("==============================================",true);
		// Reporter.log("InboxMailCount="+inboxMailCount, true);
		// Reporter.log("InboxAttachmentCount="+inboxAttachmentCount, true);
		// Reporter.log("SentItemMailCount="+sentItemMailCount, true);
		// Reporter.log("SentItemAttachmentCount="+sentItemAttachmentCount,
		// true);
		// Reporter.log("DraftItemMailCount="+draftItemMailCount, true);
		// Reporter.log("DraftItemAttachmentCount="+draftItemAttachmentCount,
		// true);
		// Reporter.log("TotalMailboxItemCount="+(inboxMailCount+inboxAttachmentCount+sentItemMailCount+sentItemAttachmentCount+draftItemMailCount+draftItemAttachmentCount),
		// true);
		// Reporter.log("==============================================",true);

		return progressCount;

	}

	public synchronized void showCount(long countInbox, long countSent, long countDrafts, long bouncedMails) {
		inboxCount += countInbox;
		sentItemsCount += countSent;
		draftCount += countDrafts;
		bouncedCount += bouncedMails;
		totalCount = inboxCount + sentItemsCount + draftCount + bouncedCount;
		Reporter.log("==============================================", true);
		Reporter.log("Total Inbox mails sofar=" + inboxCount, true);
		Reporter.log("Total SentItems sofar=" + sentItemsCount, true);
		Reporter.log("Total Draft mails sofar=" + draftCount, true);
		Reporter.log("Total bounced mails in inbox sofar=" + bouncedCount, true);
		Reporter.log("==============================================", true);
		// Reporter.log("Total mails sofar="+totalCount, true);

	}

	// @Test( dataProvider="dataProviderCount")
	public void totalCountInFolders(String subject, int userStartingRange, int userEndingRange)
			throws IOException, InterruptedException {

		// String userPrefix ="qa-stress";
		// String userSuffix ="@o365security.net";
		String userName;
		// String userPassword = "Aut0mat10n#123";

		long countInbox = 0;
		long countSent = 0;
		long countDrafts = 0;
		long bouncedMails = 0;

		for (int currUserNumber = userStartingRange; currUserNumber <= userEndingRange; currUserNumber++) { // for
																											// total
																											// number
																											// of
																											// users

			userName = userPrefix + Integer.toString(currUserNumber) + userSuffix;

			try {
				Office365MailActivities objMail = new Office365MailActivities(userName, userPassword);

				ArrayList<Long> countArray = objMail.totalMailSentToExternal("@o365security.net");
				countSent = countArray.get(1);
				countInbox = countArray.get(0);
				// countInbox = objMail.totalCountInFolder("Inbox");
				// countSent = objMail.totalCountInFolder("SentItems");
				// countDrafts = objMail.totalCountInFolder("Drafts");

				showCount(countInbox, countSent, countDrafts, bouncedMails);
				objMail = null;

			} catch (Exception e) {
				// TODO Auto-generated catch block
				Reporter.log(e.getLocalizedMessage(), true);
			}
		}

	}

	@Test(dataProvider = "dataProviderCount", groups = { "countAll" })
	public void getCountFromSelectedMails(String subject, int userStartingRange, int userEndingRange)
			throws IOException, InterruptedException {

		// String userPrefix ="qa-stress";
		// String userSuffix ="@o365security.net";
		String userName;
		// String userPassword = "Aut0mat10n#123";
		TreeMap<String, Long> results = new TreeMap<String, Long>();

		for (int currUserNumber = userStartingRange; currUserNumber <= userEndingRange; currUserNumber++) { // for
																											// total
																											// number
																											// of
																											// users

			userName = userPrefix + Integer.toString(currUserNumber) + userSuffix;

			try {
				Reporter.log("Trying for user:" + userName, true);
				Office365MailActivities objMail = new Office365MailActivities(userName, userPassword);

				results = objMail.getSelectedMailCount(changeMailSubject, changeDraftSubject);

				writeToFile(DateString + "_MailCount", userName + "=" + results.toString());

				showCountFromTreeMap(userName, results);
				objMail = null;

			} catch (Exception e) {
				// TODO Auto-generated catch block
				Reporter.log(e.getLocalizedMessage(), true);
			}
		}

	}

	@Test(dataProvider = "dataProviderCount", groups = { "countAll" })
	public void getCountFromAllMails(String subject, int userStartingRange, int userEndingRange)
			throws IOException, InterruptedException {

		// String userPrefix ="qa-stress";
		// String userSuffix ="@o365security.net";
		String userName;
		// String userPassword = "Aut0mat10n#123";
		TreeMap<String, Long> results = new TreeMap<String, Long>();

		for (int currUserNumber = userStartingRange; currUserNumber <= userEndingRange; currUserNumber++) { // for
																											// total
																											// number
																											// of
																											// users

			userName = userPrefix + Integer.toString(currUserNumber) + userSuffix;

			try {
				Reporter.log("Trying for user:" + userName, true);
				Office365MailActivities objMail = new Office365MailActivities(userName, userPassword);

				results = objMail.getMailAndAttachmentCount(changeMailSubject, changeDraftSubject);

				writeToFile(DateString + "_MailCount", userName + "=" + results.toString());

				showCountFromTreeMap(userName, results);
				objMail = null;

			} catch (Exception e) {
				// TODO Auto-generated catch block
				Reporter.log(e.getLocalizedMessage(), true);
			}
		}

	}
	
	@SuppressWarnings("null")
	@Test
	public void getCountFromCloudsoc()
			throws IOException, InterruptedException {

		
		
		HashMap<String, String> terms = new HashMap<>() ;
		HashMap<String, Long> results = new HashMap<>() ;
		Logger.info("here now ..........1");
		fromTime="2016-06-10T09 :12:51.033Z";
		toTime="2016-07-10T09:12:51.033Z";
		
		Reporter actualMailMessages1;
//		actualMailMessages1.clear();
		ForensicSearchResults logs;
		long emailReceived =0, attachmentReceived=0, emailSent =0, attachmentSent=0, ciLogs =0;
		try {
			int waitedTime =0;
//			Thread.sleep(waitedTime);
//			for (int i = waitedTime; i <= ( MAX_LOG_WAIT_TIME_IN_MINUTES * 60 * 1000); i+=60000 ) {
				
//				Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
				Reporter.log("------------------------------------------------------------------------",true );
//				Reporter.log("Searching for logs after :"+TimeUnit.MILLISECONDS.toMinutes(i)+" minutes",true );
				Reporter.log("------------------------------------------------------------------------",true );
				terms.put("facility","Office 365");
				terms.put("Object_type", "Email_Message");
				terms.put("Activity_type", "Send");
				
				Logger.info("here now ..........1");
				
				logs = getCloudsocCount(fromTime, toTime, "Office 365",terms, suiteData.getUsername(), suiteData.getCSRFToken(), suiteData.getSessionID());
				emailSent=logs.getHits().getTotal();
				results.put("EmailSent", emailSent);
				
				Logger.info("here now ..........2");
//				terms.clear();
				terms.put("Object_type", "Email_Message");
				terms.put("Activity_type", "Receive");

				logs = getCloudsocCount(fromTime, toTime, "Office 365",terms, suiteData.getUsername(), suiteData.getCSRFToken(), suiteData.getSessionID());
				emailReceived=logs.getHits().getTotal();
				results.put("EmailReceived", emailReceived);
//				getCloudsocCount(int from, int to, String facilty, HashMap<String, String> terms, String user, String csrfToken, String sessionId)
				
				Logger.info("here now ..........3");
//				terms.clear();
				terms.put("Object_type", "Email_File_Attachment");
				terms.put("Activity_type", "Send");
				
				logs = getCloudsocCount(fromTime, toTime, "Office 365",terms, suiteData.getUsername(), suiteData.getCSRFToken(), suiteData.getSessionID());
				attachmentSent=logs.getHits().getTotal();
				
				results.put("AttachmentSent", attachmentSent);
//				terms.clear();
				terms.put("Object_type", "Email_File_Attachment");
				terms.put("Activity_type", "Receive");
				
				logs = getCloudsocCount(fromTime, toTime, "Office 365",terms, suiteData.getUsername(), suiteData.getCSRFToken(), suiteData.getSessionID());
				attachmentReceived=logs.getHits().getTotal();
				
				results.put("AttachmentReceived", attachmentReceived);
				
				saveInGoogleSheetCloudsocCount(results);
				
//				terms.put("Object_type", "Email_File_Attachment");
				
//				Set<String> hs = new HashSet<>();
//				hs.addAll(actualMailMessages1);
//				actualMailMessages1.clear();
//				actualMailMessages1.addAll(hs);
				
//			}
		}
		catch(Exception e) {}

	}

	public synchronized void showCountFromTreeMap(String myUserName, TreeMap<String, Long> results1) {

		Reporter.log("===================Count So Far===========================", true);

		Iterator it = results1.entrySet().iterator();
		while (it.hasNext()) {

			Map.Entry pair = (Map.Entry) it.next();

			if (resultGlobal.get(pair.getKey()) == null) { // first time storing
				resultGlobal.put(pair.getKey().toString(), Long.parseLong(pair.getValue().toString()));
			} else {
				resultGlobal.put(pair.getKey().toString(),
						resultGlobal.get(pair.getKey()) + Long.parseLong(pair.getValue().toString()));
			}
			Reporter.log(pair.getKey() + " = " + resultGlobal.get(pair.getKey()), true);
			// it.remove(); // avoids a ConcurrentModificationException
		}

		Reporter.log("==============================================", true);
		// Reporter.log("Total mails sofar="+totalCount, true);

	}

	@AfterGroups("countAll")
	public void afterGroups3() {

		writeToFile(DateString + "_MailCount", "Final Count" + "=" + resultGlobal.toString());
		Reporter.log("*****************Final Count***************************", true);
		Iterator it = resultGlobal.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();
			Reporter.log(pair.getKey() + " = " + pair.getValue(), true);
			// it.remove(); // avoids a ConcurrentModificationException
		}
		Reporter.log("*******************************************************", true);
	}

	@AfterGroups("count")
	public void afterGroups1() {
		Reporter.log("==============================================", true);
		Reporter.log("Total Inbox mails final=" + inboxCount, true);
		Reporter.log("Total SentItems final=" + sentItemsCount, true);
		Reporter.log("Total Draft mails final=" + draftCount, true);
		Reporter.log("Total bounced mails in inbox final=" + bouncedCount, true);
		Reporter.log("==============================================", true);
	}

	@AfterGroups("mails")
	public void afterGroups2() {
		Reporter.log("==============================================", true);
		Reporter.log("Passed final=" + passedCount, true);
		Reporter.log("Failed final=" + failedCount, true);
		Reporter.log("==============================================", true);
	}

	@AfterClass(alwaysRun = true)
	public void cleanup() throws Exception {

		// Reporter.log("==============After class=====================",true);
		// Reporter.log("==============================================",true);
		// Reporter.log("Final inbox mails ="+inboxCount, true);
		// Reporter.log("Final external mails ="+inboxCount, true);
		// Reporter.log("Final external counts ="+sentItemsCount, true);
		// Reporter.log("Final sent mails ="+sentItemsCount, true);
		// Reporter.log("Final draft mails ="+draftCount, true);
		// Reporter.log("==============================================",true);
		// Reporter.log("Final total mails ="+totalCount, true);
		// Reporter.log("==============================================",true);

	}

	public EmailAddressCollection getInternalRecepientList(int userStartingRange, int userEndingRange) {
		String userPrefix = "qa-stress";
		String userSuffix = "@o365security.net";
		String userEmail;
		EmailAddressCollection mailIds = new EmailAddressCollection();

		for (int currUserNumber = userStartingRange; currUserNumber <= userEndingRange; currUserNumber++) { // for
																											// total
																											// number
																											// of
																											// users

			// forming the sender email id
			userEmail = userPrefix + Integer.toString(currUserNumber) + userSuffix;
			Reporter.log(userEmail, true);
			mailIds.add(userEmail);
		}

		return mailIds;

	}

	public EmailAddressCollection getExternalRecepientList(int userStartingRange, int userEndingRange) {
		String userPrefix = "mitthan.meena+";
		String userSuffix = "@elasticaqa.net";
		String userEmail;
		EmailAddressCollection mailIds = new EmailAddressCollection();

		for (int currUserNumber = userStartingRange; currUserNumber <= userEndingRange; currUserNumber++) { // for
																											// total
																											// number
																											// of
																											// users

			// forming the sender email id
			userEmail = userPrefix + Integer.toString(currUserNumber) + userSuffix;
			Reporter.log(userEmail, true);
			mailIds.add(userEmail);
		}

		return mailIds;

	}

	/**
	 * @param subjectList
	 * @param fromFolder
	 * @param userName
	 * @param userPassword
	 * @throws InterruptedException
	 */
	void deleteMails(ArrayList<String> subjectList, String fromFolder, String userName, String userPassword)
			throws InterruptedException {

		if (subjectList.size() >= 1) {
			DeleteMode delMode = DeleteMode.HardDelete;
			// creating mail object with testuser1 credentials
			Office365MailActivities objmyMail = new Office365MailActivities(userName, userPassword);

			Reporter.log("-----------------------------", true);
			Reporter.log("Deleting Mails in " + fromFolder + " of " + userName, true);
			Reporter.log("-----------------------------", true);
			int i = 1;
			for (String subject : subjectList) {
				Reporter.log(i + ") " + subject, true);
				objmyMail.deleteMailFromFolder(subject, fromFolder, delMode);
				i++;
			}

			Reporter.log("-----------------------------", true);
		}

	}

	/**
	 * @param myFiles
	 * @throws InterruptedException
	 */
	void deleteFiles(ArrayList<File> myFiles) throws InterruptedException {

		if (myFiles.size() >= 1) {

			Reporter.log("-----------------------------", true);
			Reporter.log("Deleting temp files", true);
			Reporter.log("-----------------------------", true);
			int i = 1;
			for (File file : myFiles) {
				file.delete();

				Reporter.log(i + ") " + file.getName(), true);
				i++;
			}

			Reporter.log("-----------------------------", true);
		}

	}

	public ArrayList<String> searchElasticLogs(int from, int to, String facilty, String query, String email)
			throws Exception {

		Reporter.log("Retrieving the logs from Elastic Search ...", true);
		String tsfrom = DateUtils.getMinutesFromCurrentTime(from);
		String tsto = DateUtils.getMinutesFromCurrentTime(to);

		// Get headers
		List<NameValuePair> headers = getHeaders();

		String payload = esQueryBuilder.getSearchQuery(tsfrom, tsto, facilty, query, email);

		Reporter.log("Request body:" + payload, true);

		// HttpRequest
		String path = suiteData.getAPIMap().get("getForensicsLogs");
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(), path);

		HttpResponse response = restClient.doPost(dataUri, headers, null, new StringEntity(payload));
		String responseBody = ClientUtil.getResponseBody(response);
		Reporter.log("==============================================================================");
		Reporter.log("Response body:" + responseBody, true);

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
	public ForensicSearchResults getCloudsocCount(String tsfrom, String tsto, String facilty, HashMap<String, String> terms,
			 String user, String csrfToken, String sessionId) throws Exception {
		
		String apiServerUrl = suiteData.getScheme() + "://" + suiteData.getApiserverHostName();
		
		DateTime currentTime = DateTime.now(DateTimeZone.UTC);
		
		Reporter.log("==============================================================================");
		Reporter.log("UTC time:" + currentTime);
		
		Reporter.log("Retrieving the logs from Elastic Search ...", true);
//		String tsfrom = DateUtils.getMinutesFromCurrentTime(from);
//		String tsto = DateUtils.getMinutesFromCurrentTime(to);
		
		// Get headers
		List<NameValuePair> headers = getHeaders();
		
		
		String payload = esQueryBuilder.getESQueryForInvestigate( tsfrom,  tsto,  facilty,  terms,  user,  apiServerUrl,  csrfToken,  sessionId, 0, 10);
		
		// HttpRequest
		String path = suiteData.getAPIMap().get("getInvestigateLogs");
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(), path);
		
		HttpResponse response = restClient.doPost(dataUri, headers, null, new StringEntity(payload));
		String responseBody = ClientUtil.getResponseBody(response);
		Reporter.log("Request payload:" + payload, true);
		Reporter.log("================");
		Reporter.log("Response code:" + response.getStatusLine().getStatusCode() + " "
				+ response.getStatusLine().getReasonPhrase(), true);
		Reporter.log("Response body:" + responseBody, true);
		
		assertEquals(getResponseStatusCode(response), HttpStatus.SC_OK, "Response code verification failed");
		ForensicSearchResults fsr = MarshallingUtils.unmarshall(responseBody, ForensicSearchResults.class);
		return fsr;
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
	public ArrayList<String> searchDisplayLogs(int from, int to, String facilty, String termType, String termValue,
			String query, String email, String csrfToken, String sessionId, String searchForUser) throws Exception {

		String apiServerUrl = suiteData.getScheme() + "://" + suiteData.getApiserverHostName();

		DateTime currentTime = DateTime.now(DateTimeZone.UTC);

		Reporter.log("==============================================================================");
		Reporter.log("UTC time:" + currentTime);

		Reporter.log("Retrieving the logs from Elastic Search ...", true);
		String tsfrom = DateUtils.getMinutesFromCurrentTime(from);
		String tsto = DateUtils.getMinutesFromCurrentTime(to);

		// Get headers
		List<NameValuePair> headers = getHeaders();

		String payload = esQueryBuilder.getSearchQueryForDisplayLogsOffice365(tsfrom, tsto, facilty, termType,
				termValue, query, email, apiServerUrl, csrfToken, sessionId, searchForUser);

		// HttpRequest
		String path = suiteData.getAPIMap().get("getInvestigateLogs");
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(), path);

		HttpResponse response = restClient.doPost(dataUri, headers, null, new StringEntity(payload));
		String responseBody = ClientUtil.getResponseBody(response);
		Reporter.log("Request payload:" + payload, true);
		Reporter.log("================");
		Reporter.log("Response code:" + response.getStatusLine().getStatusCode() + " "
				+ response.getStatusLine().getReasonPhrase(), true);
		Reporter.log("Response body:" + responseBody, true);

		assertEquals(getResponseStatusCode(response), HttpStatus.SC_OK, "Response code verification failed");
		ForensicSearchResults fsr = MarshallingUtils.unmarshall(responseBody, ForensicSearchResults.class);
		return this.retrieveActualMessages(fsr);
	}

	// @Test(dependsOnMethods = "performSaasActivities")
	public void fetchElasticSearchLogsForCIQ() throws Exception {
		Reporter.log("Retrieving the logs from Elastic Search ...");
		String tsfrom = DateUtils.getMinutesFromCurrentTime(-20);
		String tsto = DateUtils.getMinutesFromCurrentTime(20);

		// Get headers
		List<NameValuePair> headers = getHeaders();

		String payload = esQueryBuilder.getSearchQueryForCIQ(tsfrom, tsto, "Box", query);

		// HttpRequest
		String path = suiteData.getAPIMap().get("getForensicsLogs");
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(), path);

		HttpResponse response = restClient.doPost(dataUri, headers, null, new StringEntity(payload));
		String responseBody = ClientUtil.getResponseBody(response);
		Reporter.log("Request payload:" + payload, true);
		Reporter.log("============================", true);
		Reporter.log("Response body:" + responseBody, true);

		ESResults esresults = MarshallingUtils.unmarshall(responseBody, ESResults.class);
		assertEquals(HttpStatus.SC_OK, getResponseStatusCode(response), "Response code verification failed");
		// Add the assertions for forensic logs
		ciqValidator = new CIQValidator(esresults);

		// Validate all code
		ciqValidator.validateAll(prepareExpectedSource());
	}

	// @Test
	public void getExposedDocuments() throws Exception {

		List<NameValuePair> headers = getHeaders();

		List<NameValuePair> qparams = new ArrayList<NameValuePair>();
		qparams.add(new BasicNameValuePair("is_internal", "true"));
		qparams.add(new BasicNameValuePair("owned_by", URLEncoder.encode(suiteData.getUsername(), "UTF-8")));
		qparams.add(new BasicNameValuePair("content_checks.vl_types", URLEncoder.encode("hipaa,pii", "UTF-8")));
		// qparams.add(new
		// BasicNameValuePair("content_checks.vk_content_iq_violations",
		// "test_ciq_profile"));

		String path = suiteData.getAPIMap().get("getBoxDocuments").replace("{tenant}", suiteData.getTenantName())
				.replace("{version}", suiteData.getBaseVersion());

		System.out.println("Path:" + path);
		URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(), path, qparams);
		System.out.println("Path:" + uri.toString());
		HttpResponse response = restClient.doGet(uri, headers);
		String responseBody = ClientUtil.getResponseBody(response);

		System.out.println("Response body:" + responseBody);
	}

	// Get expected source
	public com.elastica.beatle.ciq.dto.Source prepareExpectedSource() throws Exception {

		com.elastica.beatle.ciq.dto.Source source = new com.elastica.beatle.ciq.dto.Source();
		source.setSeverity("critical");
		source.setFacility("Box");
		source.setObjectName("/All Files/" + destinationFile);
		source.setResourceId(resourceId);
		source.setRisks("PII, ContentIQ Violations, HIPAA");
		source.setSource("API");

		String filepath = System.getProperty("user.dir") + "/src/test/resources/securlets/securletsData/"
				+ "ContentChecksHipaa.json";
		FileInputStream inStream = new FileInputStream(FilenameUtils.separatorsToSystem(filepath));
		String contentChecksJson = IOUtils.toString(inStream);
		ContentChecks contentChecks = MarshallingUtils.unmarshall(contentChecksJson, ContentChecks.class);

		source.setContentChecks(contentChecks);
		source.setUser(suiteData.getUsername());
		source.setMessage("File " + destinationFile + " has risk(s) - PII, ContentIQ Violations, HIPAA");
		source.setActivityType("Content Inspection");
		source.setName(destinationFile);
		return source;

	}

	private ArrayList<String> retrieveActualMessages(ForensicSearchResults fsr) {
		ArrayList<String> alist = new ArrayList<String>();
		// Reporter.log("Messages List");
		// Reporter.log("----------------------");
		String msg = "";
		for (Hit hit : fsr.getHits().getHits()) {
			Source source = hit.getSource();
			msg = source.getMessage();// .replace("\\", "");
			alist.add(msg);
			// Reporter.log(msg,true);
		}
		return alist;
	}

	private static void copyFileUsingFileChannels(File source, File dest) throws IOException {
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

	void displayMessageContent(ArrayList<String> myMessages) {
		if (myMessages.size() >= 1) {
			Iterator itr = null;

			itr = myMessages.iterator();
			int i = 1;
			Reporter.log("Available logs", true);
			Reporter.log("-----------------------------", true);
			while (itr.hasNext()) {
				Reporter.log(i + ") " + itr.next().toString(), true);
				i++;
			}
			Reporter.log("-----------------------------", true);
		} else {
			Reporter.log("----------No logs found----------", true);
		}
	}

	static String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}

	public O365Document getExposures(String isInternal, String ownedBy, ArrayList<String> vltypes, String fileName)
			throws Exception {
		List<NameValuePair> headers = getHeaders();

		List<NameValuePair> qparams = new ArrayList<NameValuePair>();
		qparams.add(new BasicNameValuePair("is_internal", isInternal));
		// qparams.add(new BasicNameValuePair("owned_by", ownedBy ));
		// //UrlUtils.decode(suiteData.getUsername())));
		qparams.add(new BasicNameValuePair("content_checks.vl_types", UrlUtils.decode(StringUtils.join(vltypes, ","))));
		qparams.add(new BasicNameValuePair("name", fileName));
		// qparams.add(new
		// BasicNameValuePair("content_checks.vk_content_iq_violations",
		// "test_ciq_profile"));

		String path = suiteData.getAPIMap().get("getO365Documents").replace("{tenant}", suiteData.getTenantName())
				.replace("{version}", suiteData.getBaseVersion());

		// System.out.println("Path:" + path);
		URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(), path, qparams);
		HttpResponse response = restClient.doGet(uri, headers);
		String responseBody = ClientUtil.getResponseBody(response);

		Logger.info("Response body:" + responseBody);
		O365Document o365Document = MarshallingUtils.unmarshall(responseBody, O365Document.class);
		return o365Document;
	}

	public O365Document getRiskyDocs(String isInternal, String ownedBy, ArrayList<String> vltypes, String fileName)
			throws Exception {
		List<NameValuePair> headers = getHeaders();

		List<NameValuePair> qparams = new ArrayList<NameValuePair>();
		qparams.add(new BasicNameValuePair("is_internal", isInternal));
		// qparams.add(new BasicNameValuePair("owned_by", ownedBy ));
		// //UrlUtils.decode(suiteData.getUsername())));
		qparams.add(new BasicNameValuePair("content_checks.vl_types", UrlUtils.decode(StringUtils.join(vltypes, ","))));
		qparams.add(new BasicNameValuePair("name", fileName));
		// qparams.add(new
		// BasicNameValuePair("content_checks.vk_content_iq_violations",
		// "test_ciq_profile"));

		String path = suiteData.getAPIMap().get("getRiskyDocuments").replace("{tenant}", suiteData.getTenantName())
				.replace("{version}", suiteData.getBaseVersion()).replace("{elappname}", "el_office_365");

		// System.out.println("Path:" + path);
		URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(), path, qparams);
		HttpResponse response = restClient.doGet(uri, headers);
		String responseBody = ClientUtil.getResponseBody(response);

		Logger.info("Response body:" + responseBody);
		O365Document o365Document = MarshallingUtils.unmarshall(responseBody, O365Document.class);
		return o365Document;
	}

	public void remediateExposure(String tenant, String facility, String user, String documentId, String userId,
			String action) throws Exception {

		List<NameValuePair> headers = getHeaders();
		String payload = "";

		if (action.equals(Remediation.ITEM_DELETE_MAIL_BY_ATTACHMENT.name())) {
			payload = "{\"source\":{\"objects\":{\"objects\":[{\"db_name\":\"" + tenant + "\",\"user\":\"" + user
					+ "\",\"user_id\":" + userId + ",\"doc_id\":\"" + documentId
					+ "\",\"doc_type\":\"Email_File_Attachment\"," + "\"actions\":[{\"code\":\"" + action
					+ "\",\"object_type\": \"Mail\",\"possible_values\":[],\"meta_info\":{}}], \"object_type\": \"Mail\"}]},\"app\":\"Office 365\"}}";
		} else {

			payload = "{\"source\":{\"objects\":{\"objects\":[{\"db_name\":\"" + tenant + "\",\"user\":\"" + user
					+ "\",\"user_id\":\"" + userId + "\",\"doc_id\":\"" + documentId + "\",\"doc_type\":\"file\","
					+ "\"actions\":[{\"code\":\"" + action
					+ "\",\"possible_values\":[],\"meta_info\":{\"current_link\":\"open\"}}]}]},\"app\":\"" + facility
					+ "\"}}";
		}

		Reporter.log("headers=" + headers.toString(), true);
		Reporter.log("payload=" + payload, true);

		// System.exit(0);

		StringEntity stringEntity = new StringEntity(payload);
		String path = suiteData.getAPIMap().get("getBoxUIRemediation").replace("{tenant}", suiteData.getTenantName())
				.replace("{version}", suiteData.getBaseVersion());

		// suiteData.getApiserverHostName()
		URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(), path, null);
		HttpResponse response = restClient.doPost(uri, headers, null, stringEntity);
		String responseBody = ClientUtil.getResponseBody(response);

		Logger.info("Response body:" + responseBody);
	}

	/**
	 * This is the utility method to remediate the exposure thro' api.
	 * 
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
		String path = suiteData.getAPIMap().get("getO365Remediation").replace("{tenant}", suiteData.getTenantName())
				.replace("{version}", suiteData.getBaseVersion());

		URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(), path, null);
		HttpResponse response = restClient.doPatch(uri, headers, null, stringEntity);
		String responseBody = ClientUtil.getResponseBody(response);
		Reporter.log("Request body:" + payload, true);
		Reporter.log("************************************************");
		Reporter.log("Response status:" + response.getStatusLine().getStatusCode() + " "
				+ response.getStatusLine().getReasonPhrase(), true);
		Reporter.log("Response body:" + responseBody, true);

		return response.getStatusLine().getStatusCode();
	}

	private MailRemediation getRemediationObject(String user, String docType, String docId, String remedialAction) {
		MailRemediation o365Remediation = new MailRemediation();

		o365Remediation.setDbName(suiteData.getTenantName());
		o365Remediation.setUser(user);
		// o365Remediation.setUserId(userId);
		o365Remediation.setDocType(docType);
		o365Remediation.setDocId(docId);
		o365Remediation.setObjectType("Mail");

		List<String> possibleValues = new ArrayList<String>();
		// if(remedialAction.equals("UNSHARE")) {
		// possibleValues.add("open"); possibleValues.add("company");
		// possibleValues.add("collaborators");
		// }

		// Meta Info
		// BoxMetaInfo boxMetaInfo = new BoxMetaInfo();
		//
		// if (remedy != null) {
		// if(remedialAction.equals("SHARE_EXPIRE")) {
		// boxMetaInfo.setCurrentLink(null);
		// boxMetaInfo.setCollabs(null);
		// boxMetaInfo.setExpireOn(remedy);
		// } else {
		// boxMetaInfo.setAccess(remedy);
		// }
		// }
		//
		// if(currentLink != null) {
		// boxMetaInfo.setCurrentLink(currentLink);
		// }

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

		String docId = null;
		List<NameValuePair> qparams = new ArrayList<NameValuePair>();
		qparams.add(new BasicNameValuePair("name", fileName));

		SecurletsUtils securletUtils = new SecurletsUtils();
		String path = suiteData.getAPIMap().get("getO365Documents").replace("{tenant}", suiteData.getTenantName())
				.replace("{version}", suiteData.getBaseVersion());

		URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(), path, qparams);
		HttpResponse response = restClient.doGet(uri, getHeaders());
		String responseBody = ClientUtil.getResponseBody(response);

		Reporter.log("Response status:" + response.getStatusLine().getStatusCode() + " "
				+ response.getStatusLine().getReasonPhrase(), true);
		Reporter.log("Response body:" + responseBody, true);

		O365Document o365Document = MarshallingUtils.unmarshall(responseBody, O365Document.class);

		if (o365Document.getMeta().getTotalCount() >= 1) {
			docId = o365Document.getObjects().get(0).getIdentification();
		}
		return docId;

		// Reporter.log(response.getStatusLine().toString(),true);
		// Reporter.log(responseBody,true);
		// String query = "$..identification";
		//
		// String filterSingleResultFromResponse =
		// securletUtils.filterSingleResultFromResponse(responseBody, query);
		// return filterSingleResultFromResponse;
	}

	public List<NameValuePair> getCookieHeaders(TestSuiteDTO suiteData) throws Exception {
		List<NameValuePair> requestHeader = new ArrayList<NameValuePair>();
		requestHeader.add(new BasicNameValuePair("X-CSRFToken", suiteData.getCSRFToken()));
		requestHeader.add(new BasicNameValuePair("Cookie",
				"sessionid=" + suiteData.getSessionID() + "; csrftoken=" + suiteData.getCSRFToken() + ";"));
		requestHeader.add(new BasicNameValuePair(HttpHeaders.AUTHORIZATION,
				AuthorizationHandler.getAuthParam(suiteData.getUsername(), suiteData.getPassword())));
		requestHeader.add(new BasicNameValuePair("Referer", "https://" + suiteData.getHost()));
		requestHeader.add(new BasicNameValuePair("X-Session", suiteData.getSessionID()));
		requestHeader.add(new BasicNameValuePair("X-TenantToken", suiteData.getTenantToken()));
		requestHeader.add(new BasicNameValuePair("X-User", suiteData.getUsername()));
		return requestHeader;
	}

	// this is test method for trying out codes
	@Test(groups = { "test" })
	public void query1() throws Exception {
		System.out.println("inside query");
		// Office365MailActivities objmyMail = new
		// Office365MailActivities("qa-stress1@o365security.net","Aut0mat10n#123");

		// objmyMail.getMailAndAttachmentCount("Please find your
		// documents","Draft");
		// boolean result = objmyMail.deleteAllMailsWithSubject("Delivery Status
		// Notification (Failure)","Inbox", DeleteMode.HardDelete);
		// System.out.println("result:"+ result);
		// System.out.println("End:"+ System.getProperty("end"));
		// Office365MailActivities objMail = new
		// Office365MailActivities("qa-stress1@o365security.net","Aut0mat10n#123");
		// objMail.totalAttachmentsInMails("Inbox");
		// objMail.totalMailSentToExternal("@o365security.net");
	}

	/**
	 * @param expectedLogs
	 * @param ActualLogs
	 * @return
	 */
	public boolean compareResult(ArrayList<String> expectedLogs, ArrayList<String> ActualLogs) {
		String logStatus = null;
		boolean testPassed = true;

		Reporter.log("Expected logs", true);
		Reporter.log("-----------------------------", true);

		int i = 0;
		for (String expectedLog : expectedLogs) {
			i++;
			logStatus = (ActualLogs.contains(expectedLog)) ? "Passed" : "Failed, log not found";
			Reporter.log(i + ") " + expectedLog + " : " + logStatus, true);

			if (logStatus.equals("Failed, log not found")) {
				testPassed = false;
			}
		}

		return testPassed;

	}

	public synchronized void writeToFile(String fileName, String data) {

		String myfile = logDir + "stress_" + fileName + ".txt";
		try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(myfile, true)))) {
			out.println(data);
		} catch (IOException e) {
			Reporter.log("FileOperation Failed", true);
			// exception handling left as an exercise for the reader
		}

	}

}