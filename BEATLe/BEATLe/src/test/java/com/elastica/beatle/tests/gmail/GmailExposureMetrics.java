package com.elastica.beatle.tests.gmail;

import java.io.File;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;


import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.elasticsearch.client.AdminClient;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import static org.testng.Assert.*;

import com.elastica.beatle.DateUtils;
import com.elastica.beatle.Retry;
import com.elastica.beatle.RetryAnalyzer;
import com.elastica.beatle.RestClient.ClientUtil;
import com.elastica.beatle.logger.Logger;
import com.elastica.beatle.securlets.CIQValidator;
import com.elastica.beatle.securlets.DocumentValidator;
import com.elastica.beatle.securlets.ESQueryBuilder;
import com.elastica.beatle.securlets.LogUtils;
import com.elastica.beatle.securlets.LogValidator;
import com.elastica.beatle.securlets.SecurletUtils;
import com.elastica.beatle.securlets.SecurletsConstants;
import com.elastica.beatle.securlets.dto.CiqProfile;
import com.elastica.beatle.securlets.dto.ExposureTotals;
import com.elastica.beatle.securlets.dto.ForensicSearchResults;
import com.elastica.beatle.securlets.dto.SecurletDocument;
import com.elastica.beatle.securlets.dto.TotalObject;
import com.elastica.beatle.securlets.dto.VlType;
import com.elastica.beatle.securlets.dto.VulnerabilityTypes;
import com.elastica.beatle.tests.securlets.CustomAssertion;
import com.google.api.services.gmail.model.Message;
import com.universal.common.GoogleMailServices;
import com.universal.common.Office365MailActivities;
import com.universal.constants.CommonConstants;
import microsoft.exchange.webservices.data.core.enumeration.service.DeleteMode;



public class GmailExposureMetrics extends GmailUtils {

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
	public static final int EXPOSURE_WAIT_TIME_IN_MINS	= 25;
	
	String clientId;
	String clientSecret;
	String refreshToken;
	String userDir;
	String externalUser1;
	String externalUser2;
	static String testUser1;
	static String testUser2;
	String externalUser1Pwd;
	String externalUser2Pwd;
	String testUser1Pwd;
	String testUser2Pwd;
	static String adminUser;
	String adminPwd;
	Office365MailActivities objMailExternalUser2;
	
	
	public GmailExposureMetrics() throws Exception {
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
				
	}
	
	public void renewTokenTestUser1(){
		this.objMailTestUser1 = new GoogleMailServices(getRegressionSpecificSuitParameters("saasAppEndUser1ClientId"), getRegressionSpecificSuitParameters("saasAppEndUser1ClientSecret"), getRegressionSpecificSuitParameters("saasAppEndUser1Token"),testUser1);
	}
	public void renewTokenTestUser2(){
		this.objMailTestUser2 = new GoogleMailServices(getRegressionSpecificSuitParameters("saasAppEndUser2ClientId"), getRegressionSpecificSuitParameters("saasAppEndUser2ClientSecret"), getRegressionSpecificSuitParameters("saasAppEndUser2Token"),testUser2);
	}
	

	@AfterClass(alwaysRun=true)
	public void cleanup() throws Exception {

			Reporter.log("Inside cleanup",true);
			deleteFiles(filesToDelete);
//			deleteMails(cleanupListSent,"SENT",objMailTestUser1);
//			deleteMails(cleanupListReceived,"INBOX",objMailTestUser2);
			
			
	}
	
	 @DataProvider(name = "metricsExposuresTotal" )
	    public static Object[][] dataProviderForMetricsExposuresTotal() {
	    	return new Object[][] { 
	    		//riskContentLocation, 	 exposure type
	    		{ "body", 					"external" },
	    		{ "attachment", 	 		"external" },
	    		{ "body and attachment",    "external" },
	    	};
	    }
	
	/**
	 * Test 1
	 *This test check the metrics as depicted in venn diagram ",
	 *1. Send a mail with Risk content in mail,body and both.",
	 *2. Verify the exposure totals get incremented after the exposure."
	 *3. Delete the mail and verify the exposure totals get decremented."
	 * 
	 */
	@Test(dataProvider = "metricsExposuresTotal", groups={"DASHBOARD", "INTERNAL", "EXPOSED_FILES"},retryAnalyzer=RetryAnalyzer.class)
	public void verify_ExposuresMetricsForInternalSwitchExternalCountChanges(String riskContentLocation,String exposureType) throws Exception {
		
		try {
 			String steps[] = {
 					"This test check the metrics as depicted in venn diagram ",
 					"1. Send a mail with Risk content in" + riskContentLocation + ".",
 					"2. Verify the exposure totals get incremented after the exposure." +
 					"3. Delete the mail and verify the exposure totals get decremented."
 					};
			
			LogUtils.logTestDescription(steps);
			
			String myUniqueId = UUID.randomUUID().toString();
			String fileName1="PCI_Test.txt";
			ArrayList<String> myAttachment = new ArrayList<String>();
			File sourceFile1 = new File(userDir +fileName1);
			sourceFile1 = new File(userDir +fileName1);
			String newFileName1 = myUniqueId+fileName1;
			File destFile1 = new File(userDir +newFileName1);
			String myMailSubject = null, mailBody = null;
			List<String> to	= new ArrayList<>();

			//Get the exposure count
			long countPublic   = 0;
			long countInternal = 0;
			long countExternal = 0;
			long countChange =0;
			long expectedExternalCount =0;
			boolean success=false;
			String searchName="";
			
			
			
			List<NameValuePair> qparams = new ArrayList<NameValuePair>(); 
			qparams.add(new BasicNameValuePair(SecurletsConstants.IS_INTERNAL,  Boolean.TRUE.toString()));
//			qparams.add(new BasicNameValuePair(SecurletsConstants.APP,  facility.Box.name()));
			
			Reporter.log("Internal exposure metrics totals before the exposure", true);
			ExposureTotals exposureTotals = getExposuresMetricsTotal(elapp.el_google_apps.name(), qparams);
			countPublic 	= exposureTotals.getPublicExposouresCount();
			countInternal 	= exposureTotals.getInternalExposouresCount();
			countExternal 	= exposureTotals.getExternalExposouresCount();

			Reporter.log("Public count  :"+countPublic, true);
			Reporter.log("Internal count:"+countInternal, true);
			Reporter.log("External count:"+countExternal, true);
		
			if(riskContentLocation.equals("attachment")){
				myMailSubject = myUniqueId+"ExposureInAttachment";
				//Creating file with unique id name for upload
				copyFileUsingFileChannels(sourceFile1, destFile1);	
				filesToDelete.add(destFile1);
				myAttachment.add(destFile1.toString());
				mailBody= "This is test mail body";
				countChange=1;
				searchName=newFileName1;

			}
			else if(riskContentLocation.equals("body")){
				countChange =1;
				searchName = myMailSubject = myUniqueId+"ExposureInMailBody";
				mailBody =readFile(sourceFile1.toString());
			}
			else if(riskContentLocation.equals("body and attachment")){
				countChange = 2;
				searchName = myMailSubject = myUniqueId+"ExposureInBodyAndAttachment";

				//Creating file with unique id name for upload
				copyFileUsingFileChannels(sourceFile1, destFile1);
				filesToDelete.add(destFile1);
				myAttachment.add(destFile1.toString());
				mailBody =readFile(sourceFile1.toString());

			}
			
			expectedExternalCount= countExternal+countChange;
			
			synchronized (this){
				renewTokenTestUser1();
				to.add(externalUser1);
				if(riskContentLocation.contains("attachment")){
					success = objMailTestUser1.sendMessageWithAttachment(to, null, null, myMailSubject, mailBody,sourceFile1.toString(),newFileName1);
				}
				else{
					 Message message = objMailTestUser1.sendMailWithBody(to, null, null, myMailSubject, mailBody);
					 if(message!=null) success=true;
					 
				}
				assertTrue(success, "Failed sending mail with subject:"+myMailSubject+".");
				success=false;
			}
			
			cleanupListSent.add(myMailSubject);
			//waiting for exposure change
			boolean result =checkExposure("docs", "true",  searchName,   1, EXPOSURE_WAIT_TIME_IN_MINS);
			assertTrue(result, "File not exposed even after waiting for "+EXPOSURE_WAIT_TIME_IN_MINS+" mins.");
			sleep(180000);

			//After  exposure
			Reporter.log("Internal exposure metrics totals after the exposure", true);
			exposureTotals = getExposuresMetricsTotal(elapp.el_google_apps.name(), qparams);

			Reporter.log("Public count  :"+exposureTotals.getPublicExposouresCount(), true);
			Reporter.log("Internal count:"+exposureTotals.getInternalExposouresCount(), true);
			Reporter.log("External count:"+exposureTotals.getExternalExposouresCount(), true);
			
			if (exposureType.equals(ExposureTypes.PUBLIC.name().toLowerCase())) {
				CustomAssertion.assertEquals( exposureTotals.getPublicExposouresCount(),countPublic+1, "Public exposure count not incremented");
				CustomAssertion.assertEquals( exposureTotals.getInternalExposouresCount(),countInternal, "Internal exposure shouldn't change");
				CustomAssertion.assertEquals( exposureTotals.getExternalExposouresCount(),countExternal, "External exposure shouldn't change");
			
			} else if(exposureType.equals(ExposureTypes.EXTERNAL.name().toLowerCase())) {
				//As it is shared with collaborators on file basis, it should not get incremented
				CustomAssertion.assertTrue(exposureTotals.getExternalExposouresCount() >= expectedExternalCount, "External exposure. Actual:"+exposureTotals.getExternalExposouresCount()+" Expected : >="+expectedExternalCount);
//				CustomAssertion.assertEquals(exposureTotals.getPublicExposouresCount(),countPublic,    "Public exposure shouldn't change");
//				CustomAssertion.assertEquals(exposureTotals.getInternalExposouresCount(),countInternal,  "Internal exposure shouldn't change");
			
			} else if(exposureType.equals(ExposureTypes.INTERNAL.name().toLowerCase())) {
				CustomAssertion.assertEquals( exposureTotals.getPublicExposouresCount(),countPublic, "Public exposure count shouldn't change");
				CustomAssertion.assertEquals( exposureTotals.getInternalExposouresCount(),countInternal+1, "Internal exposure count not incremented");
				CustomAssertion.assertEquals( exposureTotals.getExternalExposouresCount(),countExternal, "External exposure shouldn't change");
			}
			
			
			synchronized (this){
			//Delete the exposed mail
			success=objMailTestUser1.trashMailFromLabel(myMailSubject,"SENT");
			assertTrue(success, "Failed deleting mail with subject:"+myMailSubject+".");
			success=false;
			}
			
			//3 mins sleep
//			sleep(EXPOSURE_WAIT_TIME);
			//waiting for exposure change
			result=checkExposure("docs", "true",  searchName,   0, EXPOSURE_WAIT_TIME_IN_MINS);
			assertTrue(result, "File not unexposed even after waiting for "+EXPOSURE_WAIT_TIME_IN_MINS+" mins.");
			sleep(180000);
			Reporter.log("Internal exposure metrics totals after removing the exposure", true);
			exposureTotals = getExposuresMetricsTotal(elapp.el_google_apps.name(), qparams);
			
			
			
			
			Reporter.log("Public count  :"+exposureTotals.getPublicExposouresCount(), true);
			Reporter.log("Internal count:"+exposureTotals.getInternalExposouresCount(), true);
			Reporter.log("External count:"+exposureTotals.getExternalExposouresCount(), true);
			
			CustomAssertion.assertTrue(exposureTotals.getExternalExposouresCount() < expectedExternalCount, "External exposure. Actual:"+exposureTotals.getExternalExposouresCount()+" Expected : <"+expectedExternalCount);
			CustomAssertion.assertEquals( exposureTotals.getPublicExposouresCount(),  countPublic, "Public exposure shouldn't change");
			CustomAssertion.assertEquals( exposureTotals.getInternalExposouresCount(),countInternal, "Internal exposure shouldn't change");
			
		}
		finally{
		}
	}
	
	/**
	 * Test 2
	 * 
	 * Check the external switch external count increment on receiving mail from external user, and decrements on deleting the same from inbox
	 * @throws Exception
	 */
	
	@Test( groups={"DASHBOARD", "EXTERNAL", "EXPOSED_FILES"},retryAnalyzer=RetryAnalyzer.class)
	public void verify_ExposuresMetricsForExternalSwitchExternalCountChanges() throws Exception {
		LogUtils.logTestDescription("Check the external switch external count increment on receiving mail from external user, and decrements on deleting the same from inbox");
		boolean success = false;

		String myUniqueId = UUID.randomUUID().toString();
		String fileName1="PCI_Test.txt";
		ArrayList<String> myAttachment = new ArrayList<String>();
		File sourceFile1 = new File(userDir +fileName1);
		sourceFile1 = new File(userDir +fileName1);
		String newFileName1 = myUniqueId+fileName1;
		File destFile1 = new File(userDir +newFileName1);
		String myMailSubject = null, mailBody = null;

		ArrayList<NameValuePair> additionalParams = new ArrayList<NameValuePair>();
		additionalParams.add(new BasicNameValuePair("file_exposures", "external"));
		ExposureTotals exposureTotals = getExposuresMetricsTotal("el_google_apps", additionalParams);
		
		long externalCount = exposureTotals.getExternalExposouresCount();
		long internalCount = exposureTotals.getInternalExposouresCount();
		long publicCount = exposureTotals.getPublicExposouresCount();
		
		myMailSubject = myUniqueId+"ExposureInMailBody";
		mailBody =readFile(sourceFile1.toString());
		synchronized (this){
			success = objMailExternalUser2.sendMail(testUser2, myMailSubject, mailBody, myAttachment, false);
			assertTrue(success, "Failed sending mail with subject:"+myMailSubject+".");
			success=false;
		}
		cleanupListReceived.add(myMailSubject);

		//Wait for three mins
//		sleep(EXPOSURE_WAIT_TIME);
		//waiting for exposure change
		boolean result =checkExposure("docs", "false",  myMailSubject,   1, EXPOSURE_WAIT_TIME_IN_MINS);
		assertTrue(result, "File not exposed even after waiting for "+EXPOSURE_WAIT_TIME_IN_MINS+" mins.");
		sleep(120000);
		exposureTotals = getExposuresMetricsTotal("el_google_apps", additionalParams);
		
		CustomAssertion.assertTrue(exposureTotals.getExternalExposouresCount() >= externalCount+1, "External exposure value. Actual: "+exposureTotals.getExternalExposouresCount()+" Expected: >="+(externalCount+1));
		CustomAssertion.assertEquals(exposureTotals.getInternalExposouresCount(), internalCount, "Internally exposed document count mismatch");
		CustomAssertion.assertEquals(exposureTotals.getPublicExposouresCount(), publicCount, "Public exposed document count mismatch");
		
		long expectedExternalCount = exposureTotals.getExternalExposouresCount()-1; // reducing the exposed count from latest available count
		
		synchronized (this){
		//Delete the exposed mail
		success=objMailTestUser2.trashMailFromLabel(myMailSubject,"INBOX");
		assertTrue(success, "Failed deleting mail with subject:"+myMailSubject+".");
		success=false;
		}
		
		//3 mins sleep
//		sleep(EXPOSURE_WAIT_TIME);
		//waiting for exposure change
		result=checkExposure("docs", "false",  myMailSubject,   0, EXPOSURE_WAIT_TIME_IN_MINS);
		assertTrue(result, "File not unexposed even after waiting for "+EXPOSURE_WAIT_TIME_IN_MINS+" mins.");
		sleep(120000);
		Reporter.log("External exposure metrics totals after removing the exposure", true);
		exposureTotals = getExposuresMetricsTotal(elapp.el_google_apps.name(), additionalParams);
		
		
		
		
		Reporter.log("Public count  :"+exposureTotals.getPublicExposouresCount(), true);
		Reporter.log("Internal count:"+exposureTotals.getInternalExposouresCount(), true);
		Reporter.log("External count:"+exposureTotals.getExternalExposouresCount(), true);
		
		CustomAssertion.assertTrue(exposureTotals.getExternalExposouresCount() <= externalCount, "External exposure. Actual:"+exposureTotals.getExternalExposouresCount()+" Expected : <="+externalCount);
//		CustomAssertion.assertEquals( exposureTotals.getPublicExposouresCount(),  publicCount, "Public exposure shouldn't change");
//		CustomAssertion.assertEquals( exposureTotals.getInternalExposouresCount(),internalCount, "Internal exposure shouldn't change");
		
	}
	
	
	
	 @DataProvider(name = "riskFileCheck", parallel=true)
	    public static Object[][] dataProviderRiskFileCheck() {
	    	return new Object[][] { 
	    		//riskContentLocation, 	 exposure type
	    		{ "body", 					"external" },
	    		{ "attachment", 	 		"external" },
	    	};
	    }
	
	
	/**
	 * Test 3
	 * Send mail with risk to external user and verify the exposure , check that it appears in exposure api and not risk document api
	 * @throws Exception
	 */
	@Test(dataProvider = "riskFileCheck", groups={"DASHBOARD", "INTERNAL", "EXPOSED_FILES", "OTHER_RISKS"},retryAnalyzer=RetryAnalyzer.class)
	public void exposeRiskFileAndCheckExposedFilesAndOtherRisks(String riskContentLocation,String exposureType) throws Exception {
		
		String steps[] = {
				"Send mail with risk to external user and verify the exposure , check that it appears in exposure api and not risk document api",
				"1. Send mail with risk body/attachment to external users ",
				"2. Verify the mail/attachment is present in exposed files tab.",
				"3. Verify the mail/attachment is not present in the other risks tab.",
				"4. Delete the mail.",
				"5. Verify the mail/attachment is not present in exposed files tab.",
				"6. Verify the mail/attachment is not present in the other risks tab.",
		};
	
		LogUtils.logTestDescription(steps);
		
		try {
			
			String myUniqueId = UUID.randomUUID().toString();
			String fileName1="PCI_Test.txt";
			ArrayList<String> myAttachment = new ArrayList<String>();
			File sourceFile1 = new File(userDir +fileName1);
			sourceFile1 = new File(userDir +fileName1);
			String newFileName1 = myUniqueId+fileName1;
			File destFile1 = new File(userDir +newFileName1);
			String myMailSubject = null, mailBody = null;
			String searchName = null ;
			boolean success= false;
			
			
			
	
		
			if(riskContentLocation.equals("attachment")){
				myMailSubject = myUniqueId+"ExposureInAttachment";
				//Creating file with unique id name for upload
				copyFileUsingFileChannels(sourceFile1, destFile1);	
				filesToDelete.add(destFile1);
				myAttachment.add(destFile1.toString());
				mailBody= "This is test mail body";
				searchName = newFileName1;

			}
			else if(riskContentLocation.equals("body")){
				myMailSubject = myUniqueId+"ExposureInMailBody";
				mailBody =readFile(sourceFile1.toString());
				searchName = myMailSubject;
			}
			
			ArrayList<String> to = new ArrayList<String>();
			synchronized (this){
				to.add(externalUser1);
				success = objMailTestUser1.sendMessageWithAttachment(to, null, null, myMailSubject, mailBody,sourceFile1.toString(),newFileName1);
				assertTrue(success, "Failed sending mail with subject:"+myMailSubject+".");
				success=false;
			}
			cleanupListSent.add(myMailSubject);

//			sleep(EXPOSURE_WAIT_TIME);
			//waiting for exposure change
			boolean result=checkExposure("docs", "true",  searchName,   1, EXPOSURE_WAIT_TIME_IN_MINS);
			assertTrue(result, "File not exposed even after waiting for "+EXPOSURE_WAIT_TIME_IN_MINS+" mins.");
			sleep(60000);
			
			//Get the exposed documents and check the document is publicly exposed
			List<NameValuePair> docparams = new ArrayList<NameValuePair>(); 
			docparams.add(new BasicNameValuePair(SecurletsConstants.IS_INTERNAL,  Boolean.TRUE.toString()));
//			docparams.add(new BasicNameValuePair(SecurletsConstants.APP,  facility.Office365.name()));
			docparams.add(new BasicNameValuePair("name",  searchName));
			SecurletDocument documents = getExposedDocuments(elapp.el_google_apps.name(), docparams);
			
			Reporter.log("After exposure, checking document is present in the exposed files tab...", true);
			CustomAssertion.assertEquals(documents.getMeta().getTotalCount(), 1, "Exposed doc count doesn't match.");
			docValidator = new DocumentValidator();
			docValidator.validateExposedDocuments(documents, true);
			
			//Get the other risk and check the document it is not listed
			Reporter.log("After exposure, checking the document is present in the other risks tab...", true);
			SecurletDocument riskyDocuments = getRiskyDocuments(elapp.el_google_apps.name(), docparams);
			CustomAssertion.assertEquals(riskyDocuments.getMeta().getTotalCount(), 0, "Other risk is not zero");

			renewTokenTestUser1();
			Reporter.log("Deleting the exposed mail", true);
			synchronized (this){
			//Delete the exposed mail
			success=objMailTestUser1.trashMailFromLabel(myMailSubject,"SENT");
			assertTrue(success, "Failed deleting mail with subject:"+myMailSubject+".");
			success=false;
			}
			
			
			//3 mins sleep
//			sleep(CommonConstants.FIVE_MINUTES_SLEEP);
			//waiting for exposure change
			result=checkExposure("docs", "true",  searchName,   0, EXPOSURE_WAIT_TIME_IN_MINS);
			assertTrue(result, "File not unexposed even after waiting for "+EXPOSURE_WAIT_TIME_IN_MINS+" mins.");
			
			sleep(60000);
			Reporter.log("After removing exposure, checking the publicly shared document is still present in the exposed files tab...", true);
			documents = getExposedDocuments(elapp.el_google_apps.name(), docparams);
			CustomAssertion.assertEquals(documents.getMeta().getTotalCount(), 0, "Exposed files count is not zero");
			
			Reporter.log("After removing exposure, checking the exposure moved to other risk...", true);
			riskyDocuments = getRiskyDocuments(elapp.el_google_apps.name(), docparams);
			CustomAssertion.assertEquals(riskyDocuments.getMeta().getTotalCount(), 0, "Other risk is not zero");
		}
		
		finally{
		}
	}	
	
	
	@DataProvider(name = "riskFileInternalMail", parallel=true)
	public static Object[][] dataProviderRiskFileInternalMail() {
		return new Object[][] { 
			//riskContentLocation, 	 exposure type
			{ "body", 					"internal" },
			{ "attachment", 	 		"internal" },
		};
	}
	
	
	/**
	 * Test 4
	 * Send mail with risk to internal user and verify the exposure , check that it  appear in risk document  api and NOT in exposure api.
	 * @throws Exception
	 */
	@Test(dataProvider = "riskFileInternalMail", groups={"DASHBOARD","EXPOSED_FILES", "OTHER_RISKS"},retryAnalyzer=RetryAnalyzer.class)
	public void sendRiskFileToInternalAndCheckExposedFilesAndOtherRisks(String riskContentLocation,String exposureType) throws Exception {
		
		String steps[] = {
				"Send mail with risk to internal user and verify the exposure , check that it  appear in risk document  api and NOT in exposure api.",
				"1. Send mail with risky content in body/attachment to internal users ",
				"2. Verify the mail/attachment is not present in exposed files tab.",
				"3. Verify the mail/attachment is present in the other risks tab.",
				"4. Remove the mail.",
				"5. Verify the mail/attachment is not present in exposed files tab.",
				"6. Verify the mail/attachment is not present in the other risks tab.",
		};
		
		LogUtils.logTestDescription(steps);
		
		try {
			
			String myUniqueId = UUID.randomUUID().toString();
			String fileName1="PCI_Test.txt";
			ArrayList<String> myAttachment = new ArrayList<String>();
			File sourceFile1 = new File(userDir +fileName1);
			sourceFile1 = new File(userDir +fileName1);
			String newFileName1 = myUniqueId+fileName1;
			File destFile1 = new File(userDir +newFileName1);
			String myMailSubject = null, mailBody = null;
			String searchName = null ;
			boolean  success = false;
			
			
			
			
			
			
			if(riskContentLocation.equals("attachment")){
				myMailSubject = myUniqueId+"ExposureInAttachment";
				//Creating file with unique id name for upload
				copyFileUsingFileChannels(sourceFile1, destFile1);	
				filesToDelete.add(destFile1);
				myAttachment.add(destFile1.toString());
				mailBody= "This is test mail body";
				searchName = newFileName1;
				
			}
			else if(riskContentLocation.equals("body")){
				myMailSubject = myUniqueId+"ExposureInMailBody";
				mailBody =readFile(sourceFile1.toString());
				searchName = myMailSubject;
			}
			
			ArrayList<String> to = new ArrayList<String>();
			synchronized (this){
				to.add(testUser2);
				renewTokenTestUser1();
				success = objMailTestUser1.sendMessageWithAttachment(to, null, null, myMailSubject, mailBody,sourceFile1.toString(),newFileName1);
				assertTrue(success, "Failed sending mail with subject:"+myMailSubject+".");
				success=false;
			}
			
			cleanupListSent.add(myMailSubject);
			cleanupListReceived.add(myMailSubject);
			
//			sleep(EXPOSURE_WAIT_TIME);
			//waiting for exposure change
			boolean result=checkExposure("risky_docs", "true",  searchName,   1, EXPOSURE_WAIT_TIME_IN_MINS);
			assertTrue(result, "File not exposed even after waiting for "+EXPOSURE_WAIT_TIME_IN_MINS+" mins.");
			sleep(60000);
			//Get the exposed documents and check the document is publicly exposed
			List<NameValuePair> docparams = new ArrayList<NameValuePair>(); 
			docparams.add(new BasicNameValuePair(SecurletsConstants.IS_INTERNAL,  Boolean.TRUE.toString()));
			docparams.add(new BasicNameValuePair(SecurletsConstants.APP,  facility.Office365.name()));
			docparams.add(new BasicNameValuePair("name",  searchName));
			
			//Get the other risk and check the document it is listed
			Reporter.log("After sending mail, checking if the risky document is present in the other risks tab...", true);
			SecurletDocument riskyDocuments = getRiskyDocuments(elapp.el_google_apps.name(), docparams);
			CustomAssertion.assertEquals(riskyDocuments.getMeta().getTotalCount(), 1, "Other risk is not zero");
			
			Reporter.log("After sending mail, checking document is not present in the exposed files tab...", true);
			SecurletDocument documents = getExposedDocuments(elapp.el_google_apps.name(), docparams);
			CustomAssertion.assertEquals(documents.getMeta().getTotalCount(), 0, "Exposed doc count doesn't match.");
			docValidator = new DocumentValidator();
			docValidator.validateExposedDocuments(documents, true);
			
			
			
			Reporter.log("Deleting the exposed mail", true);
			synchronized (this){
			//Delete the exposed mail
				renewTokenTestUser1();
				renewTokenTestUser2();
			success=objMailTestUser1.trashMailFromLabel(myMailSubject,"SENT");
			assertTrue(success, "Failed deleting mail with subject:"+myMailSubject+".");
			success=false;
			success= objMailTestUser2.trashMailFromLabel(myMailSubject,"INBOX");
			assertTrue(success, "Failed deleting mail with subject:"+myMailSubject+".");
			}
			
			
			//3 mins sleep
//			sleep(CommonConstants.FIVE_MINUTES_SLEEP);
			//waiting for exposure change
			result=checkExposure("risky_docs", "true",  searchName,   0, EXPOSURE_WAIT_TIME_IN_MINS);
			assertTrue(result, "File not unexposed even after waiting for "+EXPOSURE_WAIT_TIME_IN_MINS+" mins.");
			sleep(60000);
			Reporter.log("After removing exposure, checking the publicly shared document is still present in the exposed files tab...", true);
			documents = getExposedDocuments(elapp.el_google_apps.name(), docparams);
			CustomAssertion.assertEquals(documents.getMeta().getTotalCount(), 0, "Exposed files count is not zero");
			
			Reporter.log("After removing exposure, checking the exposure moved to other risk...", true);
			riskyDocuments = getRiskyDocuments(elapp.el_google_apps.name(), docparams);
			CustomAssertion.assertEquals(riskyDocuments.getMeta().getTotalCount(), 0, "Other risk is not zero");
		}
		
		finally{
		}
	}	
	
	 @DataProvider(name = "nonRiskFileCheck", parallel=true)
	    public static Object[][] dataProviderNonRiskFileCheck() {
	    	return new Object[][] { 
	    		//riskContentLocation, 	 exposure type
	    		{ "body", 					"external" },
	    		{ "attachment", 	 		"external" },
	    	};
	    }
	
	
	/**
	 * Test 5
	 * Send mail without risk to external user and verify the exposure , check that it doesn't appear in risk document  api and  exposure api.
	 * @throws Exception
	 */
	@Test(dataProvider = "nonRiskFileCheck", groups={"DASHBOARD", "INTERNAL", "EXPOSED_FILES", "OTHER_RISKS"},retryAnalyzer=RetryAnalyzer.class)
	public void exposeNonRiskFileAndCheckExposedFilesAndOtherRisks(String riskContentLocation,String exposureType) throws Exception {
		
		String steps[] = {
				"Send mail without risk to external user and verify the exposure , check that it doesn't appear in risk document  api and  exposure api.",
				"1. Send mail with non-risky content in body/attachment to external users ",
				"2. Verify the mail/attachment is not present in exposed files tab.",
				"3. Verify the mail/attachment is not present in the other risks tab.",
		};
	
		LogUtils.logTestDescription(steps);
		
		try {
			
			String myUniqueId = UUID.randomUUID().toString();
			String fileName1="simpleTextFile.txt";
			ArrayList<String> myAttachment = new ArrayList<String>();
			File sourceFile1 = new File(userDir +fileName1);
			sourceFile1 = new File(userDir +fileName1);
			String newFileName1 = myUniqueId+fileName1;
			File destFile1 = new File(userDir +newFileName1);
			String myMailSubject = null, mailBody = null;
			String searchName = null ;
			boolean success = false;

			
			
			
	
		
			if(riskContentLocation.equals("attachment")){
				myMailSubject = myUniqueId+"ExposureInAttachment";
				//Creating file with unique id name for upload
				copyFileUsingFileChannels(sourceFile1, destFile1);	
				filesToDelete.add(destFile1);
				myAttachment.add(destFile1.toString());
				mailBody= "This is test mail body";
				searchName = newFileName1;

			}
			else if(riskContentLocation.equals("body")){
				myMailSubject = myUniqueId+"ExposureInMailBody";
				mailBody =readFile(sourceFile1.toString());
				searchName = myMailSubject;
			}
			
			ArrayList<String> to = new ArrayList<String>();
			synchronized (this){
				to.add(externalUser1);
				success = objMailTestUser1.sendMessageWithAttachment(to, null, null, myMailSubject, mailBody,sourceFile1.toString(),newFileName1);
				assertTrue(success, "Failed sending mail with subject:"+myMailSubject+".");
				success=false;
			}
			
			cleanupListSent.add(myMailSubject);

			sleep(60000);
			
			//Get the exposed documents and check the document is publicly exposed
			List<NameValuePair> docparams = new ArrayList<NameValuePair>(); 
			docparams.add(new BasicNameValuePair(SecurletsConstants.IS_INTERNAL,  Boolean.TRUE.toString()));
			docparams.add(new BasicNameValuePair(SecurletsConstants.APP,  facility.Office365.name()));
			docparams.add(new BasicNameValuePair("name",  searchName));
			SecurletDocument documents = getExposedDocuments(elapp.el_google_apps.name(), docparams);
			
			Reporter.log("After sending mail, checking document is not present in the exposed files tab...", true);
			CustomAssertion.assertEquals(documents.getMeta().getTotalCount(), 0, "Exposed doc count doesn't match.");
			docValidator = new DocumentValidator();
			docValidator.validateExposedDocuments(documents, true);
			
			//Get the other risk and check the document it is listed
			Reporter.log("After sending mail, checking if the non-risky document is present in the other risks tab...", true);
			SecurletDocument riskyDocuments = getRiskyDocuments(elapp.el_google_apps.name(), docparams);
			CustomAssertion.assertEquals(riskyDocuments.getMeta().getTotalCount(), 0, "Other risk is not zero");
			
		}
		
		finally{
		}
	}	
	
	
	/**
	 * Test 6
	 * 
	 * List all internal-switch external exposed files and validate all documents
	 * @throws Exception
	 */
	@Test(groups={"DASHBOARD", "INTERNAL", "EXPOSED_FILES", "FILTER", "DATA_CHECK"},retryAnalyzer=RetryAnalyzer.class)
	public void listAllInternalSwitchExternallyExposedFiles() throws Exception {
		LogUtils.logTestDescription("Retrieve all internal-switch external exposed files and verify each and every field as per schema for all documents.");
		
		ArrayList<NameValuePair> additionalParams = new ArrayList<NameValuePair>();
		additionalParams.add(new BasicNameValuePair("is_internal", "true"));
		additionalParams.add(new BasicNameValuePair("exposures.types", "all_external"));
		
		Reporter.log("Fetching the exposed documents to find the total count.", true);
		SecurletDocument documents = getExposedDocuments( elapp.el_google_apps.name(), additionalParams);
		
		//Fetch all the documents in one query
		int limit = documents.getMeta().getTotalCount();
		additionalParams.add(new BasicNameValuePair("offset", "0"));
		additionalParams.add(new BasicNameValuePair("limit",  String.valueOf(limit)));
		Reporter.log("Fetching all the exposed documents", true);
		documents = getExposedDocuments( elapp.el_google_apps.name(), additionalParams);
		
		//Verify all internally owned documents
		Reporter.log("Validating all the externally exposed documents...", true);
		docValidator = new DocumentValidator();
		docValidator.validateExposedDocuments(documents, true);
	}
	
	/**
	 * Test 7
	 * 
	 * List all external-switch external exposed files and validate all documents
	 * @throws Exception
	 */
	@Test(groups={"DASHBOARD", "EXTERNAL", "EXPOSED_FILES", "FILTER", "DATA_CHECK"},retryAnalyzer=RetryAnalyzer.class)
	public void listAllExternalSwitchExternallyExposedFiles() throws Exception {
		LogUtils.logTestDescription("Retrieve all external-switch external exposed files and verify each and every field as per schema for all documents.");
		
		ArrayList<NameValuePair> additionalParams = new ArrayList<NameValuePair>();
		additionalParams.add(new BasicNameValuePair("is_internal", "false"));
		additionalParams.add(new BasicNameValuePair("exposures.types", "all_external"));
		
		Reporter.log("Fetching the exposed documents to find the total count.", true);
		SecurletDocument documents = getExposedDocuments( elapp.el_google_apps.name(), additionalParams);
		
		//Fetch all the documents in one query
		int limit = documents.getMeta().getTotalCount();
		additionalParams.add(new BasicNameValuePair("offset", "0"));
		additionalParams.add(new BasicNameValuePair("limit",  String.valueOf(limit)));
		Reporter.log("Fetching all the exposed documents", true);
		documents = getExposedDocuments( elapp.el_google_apps.name(), additionalParams);
		
		//Verify all internally owned documents
		Reporter.log("Validating all the externally exposed documents...", true);
		docValidator = new DocumentValidator();
		docValidator.validateExposedDocuments(documents, false);
	}
	
	
	
	
	
	/**
	 * Test 8
	 * 
	 * Verify the top 10 exposed file types and check count is always greater than zero
	 * 
	 * 
	 */
	@Test(groups={"DASHBOARD", "INTERNAL","EXTERNAL", "EXPOSED_FILES", "FILTER", "DATA_CHECK"}, dataProvider = "dataProviderExposureType")
	public void verifyTopExposedFileTypes(String isInternal) throws Exception {

		LogUtils.logTestDescription("Get top exposed file types");

		//After external exposure
		ExposureTotals exposedFileTypes = getExposedFileTypes("el_google_apps",isInternal, "10");
		
		CustomAssertion.assertTrue(exposedFileTypes.getObjects().size() >= 0, "File type exposure is less than zero");
		
		for(TotalObject totalObject : exposedFileTypes.getObjects()) {
			CustomAssertion.assertTrue(totalObject.getId()!= null, "Id is null");
			CustomAssertion.assertTrue(totalObject.getTerm()!= null, "Term is null");
			
			CustomAssertion.assertTrue(totalObject.getTotal() >= 0, "Count is less than or equal to zero");
			CustomAssertion.assertTrue(totalObject.getCount() >= 0, "Count is less than or equal to zero");
		}

	}
	
	  
    // File type exposure total
    @DataProvider(name = "fileTypesExposuresTotal" , parallel=true)
    public static Object[][] dataProviderForFileTypesExposuresTotal() {
    	return new Object[][] { 
    		//testname, 										folder, filename, 	shared access, 	term,  remedialRoles, expectedRole, Service to call
    		{ "Expose a text file and verify the filetype count", "false", "PCI_Test.txt",     				"txt", 		"APIServer" },
    		{ "Expose a pdf file and verify the filetype count",  "false", "ferpa.pdf",   					"pdf", 		"APIServer" },
//    		{ "Expose a java file and verify the filetype count", "true", "Hello.java", 					"java", 	"APIServer" },
    		{ "Expose a text file and verify the filetype count", "true", "PCI_Test.txt",     				"txt", 		"APIServer" },
    		{ "Expose a pdf file and verify the filetype count",  "true", "ferpa.pdf",   					"pdf", 		"APIServer" },
    		{ "Expose a rtf file and verify the filetype count",  "true", "PII.rtf",						"rtf", 		"APIServer" },
    		{ "Expose a xls  file and verify the filetype count", "true", "vba_macro.xls",					"xls", 		"APIServer" },
    		
    		
    		
    	//	{ "Expose a text file and verify the filetype count", "/", "BE.txt",     "company", "internal", "APIServer" }
    	};
    }
    
	
	/**
	 * Test 9
	 * 
	 * Expose a specific file and check the exposure count is incremented for the file type, unexpose the same and check if the count is decremented.
	 * 
	 * 
	 */
	@Test (dataProvider = "fileTypesExposuresTotal", groups={"DASHBOARD", "INTERNAL", "EXPOSED_FILES"},retryAnalyzer=RetryAnalyzer.class)
	public void exposeAFileTypeAndVerifyExposedFileTypes(String testname, String isInternal, String filename,  String filetype, String server) throws Exception {

		LogUtils.logTestDescription("Expose a specific file and check the exposure count is incremented for the file type, unexpose the same and check if the count is decremented.");

		//before exposure
		ExposureTotals exposedFileTypes = getExposedFileTypes("el_google_apps", isInternal, "10");
		HashMap<String, Integer> beforeMap = new HashMap<String, Integer>();
		boolean success = false;
		for(TotalObject totalObject : exposedFileTypes.getObjects()) {
			beforeMap.put(totalObject.getId(), totalObject.getCount());
		}
		int currValue = 0;
		
		String myUniqueId = UUID.randomUUID().toString();
		ArrayList<String> myAttachment = new ArrayList<String>();
		File sourceFile1 = new File(userDir +filename);
		sourceFile1 = new File(userDir +filename);
		String newFileName1 = myUniqueId+filename;
		File destFile1 = new File(userDir +newFileName1);
		String myMailSubject = null, mailBody = null;

		ArrayList<NameValuePair> additionalParams = new ArrayList<NameValuePair>();
		
		
		
		myMailSubject = myUniqueId+"FileAttachment";
		copyFileUsingFileChannels(sourceFile1, destFile1);	
		filesToDelete.add(destFile1);
		myAttachment.add(destFile1.toString());
		mailBody= "This is test mail body";
		
		ArrayList<String> to = new ArrayList<String>();
		synchronized (this){
			if(isInternal.equals("true")){
				to.add(externalUser1);
				success = objMailTestUser1.sendMessageWithAttachment(to, null, null, myMailSubject, mailBody,sourceFile1.toString(),newFileName1);
				cleanupListSent.add(myMailSubject);
			}
			else if(isInternal.equals("false")){
				success=  objMailExternalUser2.sendMail(testUser2, myMailSubject, mailBody, myAttachment, false);
				cleanupListReceived.add(myMailSubject);
			}
			assertTrue(success, "Failed sending mail with subject:"+myMailSubject+".");
			success=false;
		}
		
		//waiting for exposure change
		boolean result=checkExposure("docs", isInternal,  newFileName1,   1, EXPOSURE_WAIT_TIME_IN_MINS);
		assertTrue(result, "File not exposed even after waiting for "+EXPOSURE_WAIT_TIME_IN_MINS+" mins.");
		sleep(60000);
		//After the exposure get the count
		exposedFileTypes = getExposedFileTypes("el_google_apps",isInternal, "10");
		HashMap<String, Integer> afterMap = new HashMap<String, Integer>();
		
		for(TotalObject totalObject : exposedFileTypes.getObjects()) {
			afterMap.put(totalObject.getId(), totalObject.getCount());
		}
		
		for(String key : afterMap.keySet()) {
			Reporter.log("Verifying the type:"+key, true );
			if(key.equals(filetype)) {
//				int expectedValue = beforeMap.containsKey(key) ? beforeMap.get(key) + 1 : 0;
				
				currValue = afterMap.get(key);
				CustomAssertion.assertTrue( beforeMap.get(key) < afterMap.get(key), key + "Actual :"+currValue+" Expected: >"+ (beforeMap.get(key)));
				
			} 
//			else {
//				CustomAssertion.assertEquals(afterMap.get(key), beforeMap.get(key),    key + " count should not get incremented for no expsoure");
//			}
		}
		
		
		synchronized (this){
			if(isInternal.equals("true")){
				success=  objMailTestUser1.trashMailFromLabel(myMailSubject,"SENT");
			}
			else if(isInternal.equals("false")){
				success=  objMailTestUser2.trashMailFromLabel(myMailSubject,"INBOX");
			}
			assertTrue(success, "Failed deleting mail with subject:"+myMailSubject+".");
			success=false;
		}
		
		
		//waiting for exposure change
		result=checkExposure("docs", isInternal,  newFileName1,   0, EXPOSURE_WAIT_TIME_IN_MINS);
		assertTrue(result, "File not unexposed even after waiting for "+EXPOSURE_WAIT_TIME_IN_MINS+" mins.");
		sleep(60000);
		//After the exposure get the count
		exposedFileTypes = getExposedFileTypes("el_google_apps", isInternal, "10");
		afterMap = new HashMap<String, Integer>();

		for(TotalObject totalObject : exposedFileTypes.getObjects()) {
			afterMap.put(totalObject.getId(), totalObject.getCount());
		}

		for(String key : afterMap.keySet()) {
			if(key.equals(filetype)) {
			Reporter.log("Verifying the count after removing exposure :"+key, true );
			CustomAssertion.assertTrue(  afterMap.get(key) <  currValue, "Expected : <"+currValue+" Actual: "+ (afterMap.get(key)));
			}
		}
	}
	
	
	
	/**
	 * Test 10
	 * 
	 * Get all internally exposed content type filter
	 * like legal, executable, health, image, business, video, design
	 * @throws Exception
	 */
	@Test(groups={"DASHBOARD", "INTERNAL", "EXPOSED_FILES", "EXTERNAL", "DATA_CHECK"}, dataProvider = "dataProviderExposureType")
	public void getAllInternallyExposedContentTypes(String isInternal) throws Exception {
		LogUtils.logTestDescription("Get all internally exposed content types.");
		
		HashMap<String, String> additionalParams = new HashMap<String, String>();
		additionalParams.put("is_internal", isInternal);
		
		//ExposureTotals expsoureTotals = 
		ExposureTotals exposedContentTypes =  getExposedContentTypes(elapp.el_google_apps.name(), additionalParams);
		
		CustomAssertion.assertTrue(exposedContentTypes.getObjects().size() >= 0, "File type exposure is less than zero");
		
		for(TotalObject totalObject : exposedContentTypes.getObjects()) {
			CustomAssertion.assertTrue(totalObject.getId()!= null, "Id is null");
			CustomAssertion.assertTrue(totalObject.getTotal() >= 0, "Count is less than or equal to zero");
		}
	}
	
	
	 // contentTypesExposuresTotal
    @DataProvider(name = "contentTypesExposuresTotal", parallel=true)
    public static Object[][] dataProviderForContentTypesExposuresTotal() {
    	return new Object[][] { 
    		//testname, 			folder, filename, 	shared access, 	term,  remedialRoles, expectedRole, Service to call
    		{ "Exposure-Internal Switch:Expose a pii with business file and verify the count change for content type business.", "true", "Business_PII.txt",				 	"business", 		"APIServer" },
    		{ "Exposure-External Switch:Expose a pii with business file and verify the count change for content type business.", "false", "Business_PII.txt",			 	"business", 		"APIServer" },
//    		{ "Expose a jpg  file and verify the  count", "/", "Sample.jpg",				"open", 	"image", 		"APIServer" },
//    		{ "Expose a png  file and verify the  count", "/", "Sample.png",				"open", 	"image", 		"APIServer" },
//    		{ "Expose a design doc and verify the  count", "/", "Design.pdf",				"open", 	"design", 		"APIServer" },
//    		{ "Expose a health doc and verify the  count", "/", "Health_Sexual_rights.pdf",	"open", 	"health", 		"APIServer" },
//    		{ "Expose a legal doc and verify the  count", "/", "Legal-Divorce2.pdf",		"open", 	"legal", 		"APIServer" },
//    		{ "Expose a computing doc and verify the  count", "/", "SC_GitHubJava.docx",	"open", 	"computing", 	"APIServer" },
//    		{ "Expose a business doc and verify the  count", "/", "Confidential_DocumentSales-Order.pdf",	"open", 	"business", 	"APIServer" },
    		
    	};
    }
	/**
	 * Test 11
	 * 
	 * Expose a specific content type and check the exposure count is incremented for the content type, unexpose the same and check if the count is decremented.
	 * 
	 * 
	 */
	@SuppressWarnings("null")
	@Test ( dataProvider = "contentTypesExposuresTotal", groups={"DASHBOARD", "INTERNAL","EXTERNAL", "EXPOSED_FILES"},retryAnalyzer=RetryAnalyzer.class)
	public void exposeAFileTypeAndVerifyExposedContentTypes(String testname, String isInternal, String filename,String filetype, String server) throws Exception {
		
		LogUtils.logTestDescription("Expose a content type and check exposure count is incremented for the content type, unexpose the same and check if the count is decremented.");
		
		Reporter.log("Started "+testname, true);
		
		HashMap<String, String> additionalParams = new HashMap<String, String>();
		additionalParams.put("is_internal", isInternal);
		boolean success = false;
		int currValue= 0;
		//before exposure
		ExposureTotals exposedContentTypes = getExposedContentTypes("el_google_apps", additionalParams);
		HashMap<String, Integer> beforeMap = new HashMap<String, Integer>();
		
		for(TotalObject totalObject : exposedContentTypes.getObjects()) {
			beforeMap.put(totalObject.getId(), totalObject.getTotal());
		}
		
		String myUniqueId = UUID.randomUUID().toString();
		ArrayList<String> myAttachment = new ArrayList<String>();
		File sourceFile1 = new File(userDir +filename);
		String newFileName1 = myUniqueId+filename;
		File destFile1 = new File(userDir +newFileName1);
		String myMailSubject = null, mailBody = null;
		
		
		
		copyFileUsingFileChannels(sourceFile1, destFile1);	
		myMailSubject = myUniqueId+"FileAttachment";
		filesToDelete.add(destFile1);
		myAttachment.add(destFile1.toString());
		mailBody= "This is test mail body";
		
		ArrayList<String> to = new ArrayList<String>();
		synchronized (this){
			if(isInternal.equals("true")){
 				to.add(externalUser1);
				success = objMailTestUser1.sendMessageWithAttachment(to, null, null, myMailSubject, mailBody,sourceFile1.toString(),newFileName1);
				cleanupListSent.add(myMailSubject);
			}
			else if(isInternal.equals("false")){
				success=  objMailExternalUser2.sendMail(testUser2, myMailSubject, mailBody, myAttachment, false);
				cleanupListReceived.add(myMailSubject);
			}
			assertTrue(success, "Failed sending mail with subject:"+myMailSubject+".");
			success=false;
		}
		
		
//		sleep(EXPOSURE_WAIT_TIME);
		//waiting for exposure change
		boolean result=checkExposure("docs", isInternal,  newFileName1,   1, EXPOSURE_WAIT_TIME_IN_MINS);
		assertTrue(result, "File not exposed even after waiting for "+EXPOSURE_WAIT_TIME_IN_MINS+" mins.");
		sleep(60000);
		//After the exposure get the count
		exposedContentTypes = getExposedContentTypes("el_google_apps", additionalParams);
		HashMap<String, Integer> afterMap = new HashMap<String, Integer>();
		
		for(TotalObject totalObject : exposedContentTypes.getObjects()) {
			afterMap.put(totalObject.getId(), totalObject.getTotal());
		}
		
		for(String key : afterMap.keySet()) {
			Reporter.log("Verifying the type:"+key, true );
			if(key.equals(filetype)) {
				currValue = afterMap.get(key);
				CustomAssertion.assertTrue( beforeMap.get(key) < afterMap.get(key), key + ". Actual :"+currValue+" Expected: >="+ (beforeMap.get(key)+1));
			} 
//			else {
//				int expectedValue = (beforeMap.get(key) == null) ? 0: beforeMap.get(key); 
//				CustomAssertion.assertEquals(afterMap.get(key), expectedValue,    key + " count should not get incremented for no expsoure");
//			}
		}
		
		synchronized (this){
			if(isInternal.equals("true")){
				success=  objMailTestUser1.trashMailFromLabel(myMailSubject,"SENT");
			}
			else if(isInternal.equals("false")){
				success=  objMailTestUser2.trashMailFromLabel(myMailSubject,"INBOX");
			}
			assertTrue(success, "Failed deleting mail with subject:"+myMailSubject+".");
			success=false;
		}
//		sleep(EXPOSURE_WAIT_TIME);
		//waiting for exposure change
		result=checkExposure("docs", isInternal,  newFileName1,   0, EXPOSURE_WAIT_TIME_IN_MINS);
		assertTrue(result, "File not unexposed even after waiting for "+EXPOSURE_WAIT_TIME_IN_MINS+" mins.");
		sleep(60000);
		//After the exposure get the count
		exposedContentTypes = getExposedContentTypes("el_google_apps", additionalParams);
		afterMap = new HashMap<String, Integer>();

		for(TotalObject totalObject : exposedContentTypes.getObjects()) {
			afterMap.put(totalObject.getId(), totalObject.getTotal());
		}

		for(String key : afterMap.keySet()) {
			if(key.equals(filetype)) {
			Reporter.log("Verifying the type:"+key, true );
			CustomAssertion.assertTrue(  afterMap.get(key) <  currValue, "Expected : <"+currValue+" Actual: "+ (afterMap.get(key)));
			}
		}
	}
	
    @DataProvider(name = "dataProviderExposureType",parallel=true)
    public static Object[][] dataProviderExposureType() {
    	return new Object[][] { 
    		 
    		{"true"},
    		{"false"}
    		
    	};
    }
	/**
	 * Test 12
	 * 
	 * Check the vulnerability types and ciq profile filters
	 * dataProvider = "vulnerabilityTypesExposuresTotal"
	 * 
	 */
	@Test(groups={"DASHBOARD", "INTERNAL", "EXPOSED_FILES", "EXTERNAL", "DATA_CHECK"}, dataProvider = "dataProviderExposureType")
	public void verifyRiskTypesForExposedFiles(String isInternal) throws Exception {
		
		LogUtils.logTestDescription("Get the vulnerability types and verify them");
		Logger.info( "Getting vulnerability types ...");
		
		HashMap<String, String> additionalParams = new HashMap<String, String>();
		additionalParams.put("is_internal", isInternal);
		additionalParams.put("vl_types", 	"all");
		
		//After external exposure
		VulnerabilityTypes vulnerabilityTypes = getVulnerabilityTypes("el_google_apps", additionalParams);

		String vulnerabilities[] = {"pci", "pii", "hipaa",  "source_code", "virus", "dlp", "encryption", "vba_macros", "glba", "ferpa"};

		for (String vl : vulnerabilities) {
			Reporter.log("Verifying the vulnerability type:"+vl, true);
			Reporter.log("Vulnerability:" + vl + "::Count:" + vulnerabilityTypes.getObjects().getVulnerabilityCount(vl), true );
			CustomAssertion.assertTrue(vulnerabilityTypes.getObjects().getVulnerabilityCount(vl) >= 0, "Count is less than zero");
		}
		
		for(CiqProfile ciqprofile : vulnerabilityTypes.getObjects().getCiqProfiles()) {
			Reporter.log("Verifying the CIQ profile name:"+ciqprofile.getId(), true);
			Reporter.log("Ciq Profile name:" +ciqprofile.getId() + ":: Count:"+ciqprofile.getTotal(), true);
			CustomAssertion.assertTrue(ciqprofile.getId()!=null, "CIQ profile id is null");
			CustomAssertion.assertTrue(ciqprofile.getTotal() >= 0, "CIQ profile total is null");
		}
	}
	
	
	 // vulnerabilityTypesExposuresTotal
    @DataProvider(name = "vulnerabilityTypesExposuresTotal",parallel=true)
    public static Object[][] dataProviderForVulnerabilityTypesExposuresTotal() {
    	return new Object[][] { 
    		//testname, 				   exposureType, filename , risk type, 
    		{ "Expose a pci file", "true", "PCI_Test.txt",		 	"pci", 		"APIServer" },
    		{ "Expose a pci file", "false", "PCI_Test.txt",		 	"pci", 		"APIServer" },
    		
    	};
    }
	
	/**
	 * Test 13
	 * Expose a file of particular vulnerability and check the exposure count , unexpose the same and check if the count is decremented.
	 * @param testname
	 * @param isInternal
	 * @param filename
	 * @param access
	 * @param riskType
	 * @param server
	 * @throws Exception
	 */
	
	
	@Test ( dataProvider = "vulnerabilityTypesExposuresTotal", groups={"DASHBOARD", "INTERNAL", "EXPOSED_FILES", "EXTERNAL"}, retryAnalyzer=RetryAnalyzer.class)
	public void exposeAFileTypeAndVerifyRiskTypes(String testname, String isInternal, String filename,  String riskType, String server) throws Exception {

		LogUtils.logTestDescription("Expose a file of particular vulnerability and check the exposure count gets incremented, unexpose the same and check if the count is decremented.");
		
		Reporter.log("Started "+testname, true);
		boolean success = false;
		HashMap<String, String> additionalParams = new HashMap<String, String>();
		additionalParams.put("is_internal", isInternal);
		
		//before exposure
		VulnerabilityTypes vulnerabilityTypes = getVulnerabilityTypes("el_google_apps", additionalParams);
		HashMap<String, Integer> beforeMap = new HashMap<String, Integer>();
		
		for(VlType totalObject : vulnerabilityTypes.getObjects().getVlTypes()) {
			beforeMap.put(totalObject.getId(), totalObject.getTotal());
		}
		
		String myUniqueId = UUID.randomUUID().toString();
		ArrayList<String> myAttachment = new ArrayList<String>();
		File sourceFile1 = new File(userDir +filename);
		String newFileName1 = myUniqueId+filename;
		File destFile1 = new File(userDir +newFileName1);
		String myMailSubject = null, mailBody = null;
		
		
		copyFileUsingFileChannels(sourceFile1, destFile1);	
		myMailSubject = myUniqueId+"FileAttachment";
		filesToDelete.add(destFile1);
		myAttachment.add(destFile1.toString());
		mailBody= "This is test mail body";
		
		
		ArrayList<String> to = new ArrayList<String>();
		synchronized (this){
			if(isInternal.equals("true")){
				to.add(externalUser1);
				success = objMailTestUser1.sendMessageWithAttachment(to, null, null, myMailSubject, mailBody,sourceFile1.toString(),newFileName1);
				cleanupListSent.add(myMailSubject);
			}
			else if(isInternal.equals("false")){
				success=  objMailExternalUser2.sendMail(testUser2, myMailSubject, mailBody, myAttachment, false);
				cleanupListReceived.add(myMailSubject);
			}
			assertTrue(success, "Failed sending mail with subject:"+myMailSubject+".");
			success=false;
		}
		
//		sleep(EXPOSURE_WAIT_TIME);
		//waiting for exposure change
		boolean result=checkExposure("docs", isInternal,  newFileName1,   1, EXPOSURE_WAIT_TIME_IN_MINS);
		assertTrue(result, "File not exposed even after waiting for "+EXPOSURE_WAIT_TIME_IN_MINS+" mins.");
		
		sleep(60000);
		//After the exposure get the count
		vulnerabilityTypes = getVulnerabilityTypes("el_google_apps", additionalParams);
		HashMap<String, Integer> afterMap = new HashMap<String, Integer>();
		
		for(VlType totalObject : vulnerabilityTypes.getObjects().getVlTypes()) {
			afterMap.put(totalObject.getId(), totalObject.getTotal());
		}
		
		Integer currValue=0;
		for(String key : afterMap.keySet()) {
			Reporter.log("Verifying the type:"+key, true );
			if(key.equals(riskType)) {
				currValue = afterMap.get(key);
				CustomAssertion.assertTrue( beforeMap.get(key) < afterMap.get(key), key + ". Actual :"+currValue+" Expected: >"+ (beforeMap.get(key)));
			} 
//			else {
//				CustomAssertion.assertEquals(afterMap.get(key), beforeMap.get(key),    key + " count should not get incremented for no expsoure");
//			}
		}
		
		synchronized (this){
			if(isInternal.equals("true")){
				success=  objMailTestUser1.trashMailFromLabel(myMailSubject,"SENT");
			}
			else if(isInternal.equals("false")){
				success=  objMailTestUser2.trashMailFromLabel(myMailSubject,"INBOX");
			}
			assertTrue(success, "Failed deleting mail with subject:"+myMailSubject+".");
			success=false;
		}
//		sleep(EXPOSURE_WAIT_TIME);
		//waiting for exposure change
		 result=checkExposure("docs", isInternal,  newFileName1,   0, EXPOSURE_WAIT_TIME_IN_MINS);
		 assertTrue(result, "File not unexposed even after waiting for "+EXPOSURE_WAIT_TIME_IN_MINS+" mins.");
		sleep(60000);
		//After the exposure get the count
		vulnerabilityTypes = getVulnerabilityTypes("el_google_apps", additionalParams);
		afterMap = new HashMap<String, Integer>();

		for(VlType totalObject : vulnerabilityTypes.getObjects().getVlTypes()) {
			afterMap.put(totalObject.getId(), totalObject.getTotal());
		}

		for(String key : afterMap.keySet()) {
			if(key.equals(riskType)) {
			Reporter.log("Verifying the type:"+key, true );
			CustomAssertion.assertTrue(  afterMap.get(key) <  currValue, "Expected : <"+currValue+" Actual: "+ (afterMap.get(key)));
			}
		}
	}
	
	
	
	/**
	 * Test 14
	 * 
	 * Check the vulnerability types and ciq profile filters
	 * 
	 * 
	 */
	@Test(groups={"DASHBOARD", "INTERNAL", "EXPOSED_FILES", "FILTER", "DATA_CHECK"},retryAnalyzer=RetryAnalyzer.class)
	public void verifyRiskTypesWithFilters() throws Exception {
		LogUtils.logTestDescription("Check the vulnerability types and ciq profile filters");
		Logger.info( "Getting vulnerability types ...");
		
		HashMap<String, String> additionalParams = new HashMap<String, String>();
		additionalParams.put("is_internal", "true");
		additionalParams.put("vl_types", 	"all");
		
		HashMap<String, Integer> vlmap = new HashMap<String, Integer>();
		
		//getVulnerabilityTypes
		VulnerabilityTypes vulnerabilityTypes = getVulnerabilityTypes("el_google_apps", additionalParams);

		String vulnerabilities[] = {"pci", "pii", "hipaa",  "source_code", "virus", "dlp", "encryption", "vba_macros", "glba", "ferpa"};
		
		for (String vl : vulnerabilities) {
			vlmap.put(vl, vulnerabilityTypes.getObjects().getVulnerabilityCount(vl));
		}
		
		for(CiqProfile ciqprofile : vulnerabilityTypes.getObjects().getCiqProfiles()) {
			vlmap.put(ciqprofile.getId(), ciqprofile.getTotal());
		}
		
		//Filter it
		for (String vl : vulnerabilities) {
			additionalParams.clear();
			additionalParams.put("is_internal", "true");
			additionalParams.put("vl_types", 	vl);
			vulnerabilityTypes = getVulnerabilityTypes("el_google_apps", additionalParams);
			CustomAssertion.assertEquals(vulnerabilityTypes.getObjects().getVulnerabilityCount(vl), vlmap.get(vl),   vl + " count should be equal");
		}
	}
	
	/**
	 * Test 15
	 * @throws Exception
	 */
	
	@Test(groups={"DASHBOARD", "INTERNAL", "USER", "DATA_CHECK"},retryAnalyzer=RetryAnalyzer.class)
	public void verifyUserTotalsAsInVennDiagram_InternalSwitch() throws Exception {
		LogUtils.logTestDescription("This test verify the user totals internal switch as in venn diagram");
		Logger.info( "Getting user totals ...");
		List<NameValuePair> qparams = new ArrayList<NameValuePair>(); 
		qparams.add(new BasicNameValuePair(SecurletsConstants.IS_INTERNAL,  Boolean.TRUE.toString()));
		//qparams.add(new BasicNameValuePair(SecurletsConstants.APP,  facility.Box.name()));
		
		ExposureTotals exposureTotals = getUserTotals(elapp.el_google_apps.name(), qparams);
		
		Reporter.log("Verifying the internal user totals is greater than zero", true);
		for(TotalObject totalObject : exposureTotals.getObjects()) {
			CustomAssertion.assertTrue(totalObject.getTotal() >=0 , totalObject.getId()  + " user total >= 0" , totalObject.getId()  + " user total < 0");
		}
	}
	
	/**
	 * Test 16
	 * @throws Exception
	 */
	@Test(groups={"DASHBOARD", "EXTERNAL", "USER", "DATA_CHECK"},retryAnalyzer=RetryAnalyzer.class)
	public void verifyUserTotalsAsInVennDiagram_ExternalSwitch() throws Exception {
		LogUtils.logTestDescription("This test verify the user totals external switch as in venn diagram");
		Logger.info( "Getting user totals ...");
		List<NameValuePair> qparams = new ArrayList<NameValuePair>(); 
		qparams.add(new BasicNameValuePair(SecurletsConstants.IS_INTERNAL,  Boolean.FALSE.toString()));
		//qparams.add(new BasicNameValuePair(SecurletsConstants.APP,  facility.Box.name()));
		
		ExposureTotals exposureTotals = getUserTotals(elapp.el_google_apps.name(), qparams);
		
		Reporter.log("Verifying the internal user totals is greater than zero", true);
		for(TotalObject totalObject : exposureTotals.getObjects()) {
			CustomAssertion.assertTrue(totalObject.getTotal() >=0 , totalObject.getId()  + " user total >= 0" , totalObject.getId()  + " user total < 0");
		}
	}
	
	
	/**
	 * Test 17
	 * @throws Exception
	 */
	@Test(groups={"DASHBOARD", "INTERNAL", "USER", "DATA_CHECK"},retryAnalyzer=RetryAnalyzer.class)
	public void verifyUserDocumentExposures() throws Exception {
		LogUtils.logTestDescription("This test verify the user documemt exposures");
		Logger.info( "Getting user totals ...");
		List<NameValuePair> qparams = new ArrayList<NameValuePair>(); 
		qparams.add(new BasicNameValuePair(SecurletsConstants.IS_INTERNAL,  Boolean.TRUE.toString()));
//		qparams.add(new BasicNameValuePair(SecurletsConstants.APP,  facility.Box.name()));
		
		SecurletDocument documents = getUserExposures(elapp.el_google_apps.name(), qparams);
		for (com.elastica.beatle.securlets.dto.Object object : documents.getObjects()) {
			CustomAssertion.assertTrue(object.getTotal() >=0, "User document exposure total can't be null");
			Reporter.log(String.valueOf(object.getTotal()), true);
		}
	}
	
	/**
	 * Test 18
	 * @throws Exception
	 */
	@Test(groups={"DASHBOARD", "INTERNAL", "USER", "DATA_CHECK"},retryAnalyzer=RetryAnalyzer.class)
	public void verifyUserVulnerabilities() throws Exception {
		LogUtils.logTestDescription("This test verify the user vulnerabilities");
		
		List<NameValuePair> qparams = new ArrayList<NameValuePair>(); 
		qparams.add(new BasicNameValuePair(SecurletsConstants.IS_INTERNAL,  Boolean.TRUE.toString()));
//		qparams.add(new BasicNameValuePair(SecurletsConstants.APP,  facility.Box.name()));
		
		SecurletDocument documents = getUserVulnerabilities(elapp.el_google_apps.name(), qparams);
		for (com.elastica.beatle.securlets.dto.Object object : documents.getObjects()) {
			CustomAssertion.assertTrue(object.getTotal() >=0, "User vulnerability exposure total can't be null");
			Reporter.log(String.valueOf(object.getTotal()), true);
		}
	}
	
    @DataProvider(name = "dataProviderUserCollabsInternalExternal", parallel=true)
    public static Object[][] dataProviderUserCollabsInternalExternal() {
    	return new Object[][] { 
    		{ "internal", "users"},
    		{ "internal", "collabs"},
    		{ "external", "users"},
    		{ "external", "collabs"}
    		
    	};
    }
	
	/**
	 * Test 19
	 * @throws Exception
	 */
	@Test (dataProvider="dataProviderUserCollabsInternalExternal",groups={"DASHBOARD", "INTERNAL", "USER", "EXTERNAL", "DATA_CHECK"},retryAnalyzer=RetryAnalyzer.class)
	public void verifyUserDetails(String exposureType, String userType) throws Exception {
		LogUtils.logTestDescription("This test verify the user/collab list");
		List<NameValuePair> qparams = new ArrayList<NameValuePair>(); 
		Logger.info( "Getting user details ...");
		Boolean isInternal =null;
		SecurletDocument documents = null;
		
		if(exposureType.equals("internal")){
			isInternal = true;
		}
		else{
			isInternal = false;
		}
		
		qparams.add(new BasicNameValuePair(SecurletsConstants.IS_INTERNAL, isInternal.toString()));
		
		if(userType.equals("users")){
			 documents = getExposedUsers(elapp.el_google_apps.name(), qparams);
		}
		else if (userType.equals("collabs")){
			documents = getCollaborators(elapp.el_google_apps.name(), qparams);
		}
		
		docValidator = new DocumentValidator();
		docValidator.validateUsers(documents,  isInternal);
	}
	
	/**
	 * Test 20
	 * 
	 * Check the top vulnerability types other risks doc 
	 * dataProvider = "vulnerabilityTypesExposuresTotal"
	 * 
	 */
	@Test(groups={"DASHBOARD", "OTHER_RISKS_FILES", "FILTER", "DATA_CHECK"},retryAnalyzer=RetryAnalyzer.class)
	public void verifyRiskTypesForOtherRiskFiles() throws Exception {
		
		LogUtils.logTestDescription("Get the risks types count in other risks files and verify them");
		Logger.info( "Getting vulnerability types ...");
		
		HashMap<String, String> additionalParams = new HashMap<String, String>();
		additionalParams.put("exposed", "false");
		
		//After external exposure
		VulnerabilityTypes vulnerabilityTypes = getVulnerabilityTypes("el_google_apps", additionalParams);

		String vulnerabilities[] = {"pci", "pii", "hipaa",  "source_code", "virus", "dlp", "encryption", "vba_macros", "glba", "ferpa"};

		for (String vl : vulnerabilities) {
			Reporter.log("Verifying the vulnerability type:"+vl, true);
			Reporter.log("Vulnerability:" + vl + "::Count:" + vulnerabilityTypes.getObjects().getVulnerabilityCount(vl), true );
			CustomAssertion.assertTrue(vulnerabilityTypes.getObjects().getVulnerabilityCount(vl) >= 0, "Count is less than zero");
		}
		
		for(CiqProfile ciqprofile : vulnerabilityTypes.getObjects().getCiqProfiles()) {
			Reporter.log("Verifying the CIQ profile name:"+ciqprofile.getId(), true);
			Reporter.log("Ciq Profile name:" +ciqprofile.getId() + ":: Count:"+ciqprofile.getTotal(), true);
			CustomAssertion.assertTrue(ciqprofile.getId()!=null, "CIQ profile id is null");
			CustomAssertion.assertTrue(ciqprofile.getTotal() >= 0, "CIQ profile total is null");
		}
	}
	
    @DataProvider(name = "dataProviderOtherRisksVerifyChangesInRiskTypeCount")
    public static Object[][] dataProviderOtherRisksVerifyChangesInRiskTypeCount() {
    	return new Object[][] { 
    		//testname, 				   exposureType, filename , risk type, 
    		{ "Send Internal mail with a pci file", "true", "PCI_Test.txt",		 	"pci", 		"APIServer" },
    		
    	};
    }
	
    /**
	 * Test 21
	 * 
	 * Send mail with a particular risk type to internal user and check the other risks - risk types count gets incremented, and on deleting it from mail box the count is decremented.
	 * 
	 * 
	 */
	
	@Test ( dataProvider = "dataProviderOtherRisksVerifyChangesInRiskTypeCount", groups={"DASHBOARD","OTHER_RISKS_FILES"},retryAnalyzer=RetryAnalyzer.class)
	public void otherRisksVerifyChangesInRiskTypeCount(String testname, String isInternal, String filename,  String riskType, String server) throws Exception {

		LogUtils.logTestDescription("Send mail with a particular risk type to internal user and check the other risks - risk types count gets incremented, and on deleting it from mail box the count is decremented.");
		
		Reporter.log("Started "+testname, true);
		boolean success = false;
		HashMap<String, String> additionalParams = new HashMap<String, String>();
		additionalParams.put("exposed", "false");
		
		//before exposure
		VulnerabilityTypes vulnerabilityTypes = getVulnerabilityTypes("el_google_apps", additionalParams);
		HashMap<String, Integer> beforeMap = new HashMap<String, Integer>();
		
		for(VlType totalObject : vulnerabilityTypes.getObjects().getVlTypes()) {
			beforeMap.put(totalObject.getId(), totalObject.getTotal());
		}
		
		String myUniqueId = UUID.randomUUID().toString();
		ArrayList<String> myAttachment = new ArrayList<String>();
		File sourceFile1 = new File(userDir +filename);
		String newFileName1 = myUniqueId+filename;
		File destFile1 = new File(userDir +newFileName1);
		String myMailSubject = null, mailBody = null;
		
		
		
		copyFileUsingFileChannels(sourceFile1, destFile1);	
		myMailSubject = myUniqueId+"FileAttachment";
		filesToDelete.add(destFile1);
		myAttachment.add(destFile1.toString());
		mailBody= "This is test mail body";
		
		ArrayList<String> to = new ArrayList<String>();
		synchronized (this){
			to.add(testUser2);
			renewTokenTestUser1();
			success = objMailTestUser1.sendMessageWithAttachment(to, null, null, myMailSubject, mailBody,sourceFile1.toString(),newFileName1);
			assertTrue(success, "Failed sending mail with subject:"+myMailSubject+".");
			success=false;
		}
		cleanupListSent.add(myMailSubject);
		cleanupListReceived.add(myMailSubject);
//		sleep(EXPOSURE_WAIT_TIME);
		
		//waiting for exposure change
		boolean result=checkExposure("risky_docs", isInternal,  newFileName1,   1, EXPOSURE_WAIT_TIME_IN_MINS);
		assertTrue(result, "File not exposed even after waiting for "+EXPOSURE_WAIT_TIME_IN_MINS+" mins.");
		sleep(60000);
		//After the exposure get the count
		vulnerabilityTypes = getVulnerabilityTypes("el_google_apps", additionalParams);
		HashMap<String, Integer> afterMap = new HashMap<String, Integer>();
		
		for(VlType totalObject : vulnerabilityTypes.getObjects().getVlTypes()) {
			afterMap.put(totalObject.getId(), totalObject.getTotal());
		}
		
		Integer currValue=0;
		for(String key : afterMap.keySet()) {
			Reporter.log("Verifying the type:"+key, true );
			if(key.equals(riskType)) {
				currValue = afterMap.get(key);
				CustomAssertion.assertTrue( beforeMap.get(key) < afterMap.get(key), key + ". Actual :"+currValue+" Expected: >"+ (beforeMap.get(key)));
			} 
//			else {
//				CustomAssertion.assertEquals(afterMap.get(key), beforeMap.get(key),    key + " count should not get incremented for no expsoure");
//			}
		}
		
		renewTokenTestUser1();
		renewTokenTestUser2();
		synchronized (this){
			success=  objMailTestUser1.trashMailFromLabel(myMailSubject,"SENT");
			assertTrue(success, "Failed deleting mail with subject:"+myMailSubject+".");
			success=false;
			success=  objMailTestUser2.trashMailFromLabel(myMailSubject,"INBOX");
			assertTrue(success, "Failed deleting mail with subject:"+myMailSubject+".");
			success=false;
		}
//		sleep(EXPOSURE_WAIT_TIME);
		//waiting for exposure change
		 result=checkExposure("risky_docs",isInternal,  newFileName1,   0, EXPOSURE_WAIT_TIME_IN_MINS);
		 assertTrue(result, "File not unexposed even after waiting for "+EXPOSURE_WAIT_TIME_IN_MINS+" mins.");
		sleep(60000);
		//After the exposure get the count
		vulnerabilityTypes = getVulnerabilityTypes("el_google_apps", additionalParams);
		afterMap = new HashMap<String, Integer>();

		for(VlType totalObject : vulnerabilityTypes.getObjects().getVlTypes()) {
			afterMap.put(totalObject.getId(), totalObject.getTotal());
		}

		for(String key : afterMap.keySet()) {
			if(key.equals(riskType)) {
			Reporter.log("Verifying the type:"+key, true );
			CustomAssertion.assertTrue(  afterMap.get(key) <  currValue, "Expected : <"+currValue+" Actual: "+ (afterMap.get(key)));
			}
		}
	}
	
	@DataProvider(name = "dataProviderOtherRisksVerifyChangesInContentTypeCount")
	public static Object[][] dataProviderOtherRisksVerifyChangesInContentTypeCount() {
		return new Object[][] { 
			//testname, 				   exposureType, filename , risk type, 
			{ "Other risks:Expose a pii with business file and verify the count change for content type business.", "true", "Business_PII.txt",				 	"business", 		"APIServer" },
			
		};
	}
	
	/**
	 * Test 22
	 * 
	 * end mail with a particular risk type to internal user and check the other risks - content types count gets incremented, and on deleting it from mail box the count is decremented.
	 * 
	 * 
	 */
	@Test ( dataProvider = "dataProviderOtherRisksVerifyChangesInContentTypeCount", groups={"DASHBOARD", "OTHER_RISKS_FILES", "FILTER"},retryAnalyzer=RetryAnalyzer.class)
	public void otherRisksVerifyChangesInContentTypeCount(String testname, String isInternal, String filename,String filetype, String server) throws Exception {
		
		LogUtils.logTestDescription("Send mail with a particular risk type to internal user and check the other risks - content types count gets incremented, and on deleting it from mail box the count is decremented.");
		
		Reporter.log("Started "+testname, true);
		
		HashMap<String, String> additionalParams = new HashMap<String, String>();
		additionalParams.put("exposed", "false");
		boolean success = false;
		int currValue= 0;
		//before exposure
		ExposureTotals exposedContentTypes = getExposedContentTypes("el_google_apps", additionalParams);
		HashMap<String, Integer> beforeMap = new HashMap<String, Integer>();
		
		for(TotalObject totalObject : exposedContentTypes.getObjects()) {
			beforeMap.put(totalObject.getId(), totalObject.getTotal());
		}
		
		String myUniqueId = UUID.randomUUID().toString();
		ArrayList<String> myAttachment = new ArrayList<String>();
		File sourceFile1 = new File(userDir +filename);
		String newFileName1 = myUniqueId+filename;
		File destFile1 = new File(userDir +newFileName1);
		String myMailSubject = null, mailBody = null;
		
		
		
		copyFileUsingFileChannels(sourceFile1, destFile1);	
		myMailSubject = myUniqueId+"FileAttachment";
		filesToDelete.add(destFile1);
		myAttachment.add(destFile1.toString());
		mailBody= "This is test mail body";
		
		ArrayList<String> to = new ArrayList<String>();
		synchronized (this){
			to.add(testUser2);
			success = objMailTestUser1.sendMessageWithAttachment(to, null, null, myMailSubject, mailBody,sourceFile1.toString(),newFileName1);
			assertTrue(success, "Failed sending mail with subject:"+myMailSubject+".");
			success=false;
		}
		
		cleanupListSent.add(myMailSubject);
		cleanupListReceived.add(myMailSubject);
		
//		sleep(EXPOSURE_WAIT_TIME);

		//waiting for exposure change
		boolean result=checkExposure("risky_docs", isInternal,  newFileName1,   1, EXPOSURE_WAIT_TIME_IN_MINS);
		assertTrue(result, "File not exposed even after waiting for "+EXPOSURE_WAIT_TIME_IN_MINS+" mins.");
		sleep(60000);
		//After the exposure get the count
		exposedContentTypes = getExposedContentTypes("el_google_apps", additionalParams);
		HashMap<String, Integer> afterMap = new HashMap<String, Integer>();
		
		for(TotalObject totalObject : exposedContentTypes.getObjects()) {
			afterMap.put(totalObject.getId(), totalObject.getTotal());
		}
		
		for(String key : afterMap.keySet()) {
			Reporter.log("Verifying the type:"+key, true );
			if(key.equals(filetype)) {
				currValue = afterMap.get(key);
				CustomAssertion.assertTrue( beforeMap.get(key) < afterMap.get(key), key + "Actual :"+currValue+" Expected: >="+ (beforeMap.get(key)+1));
			} 
//			else {
//				int expectedValue = (beforeMap.get(key) == null) ? 0: beforeMap.get(key); 
//				CustomAssertion.assertEquals(afterMap.get(key), expectedValue,    key + " count should not get incremented for no expsoure");
//			}
		}
		renewTokenTestUser1();
		renewTokenTestUser2();
		
		synchronized (this){
			success=  objMailTestUser1.trashMailFromLabel(myMailSubject,"SENT");
			assertTrue(success, "Failed deleting mail with subject:"+myMailSubject+".");
			success=false;
			success=  objMailTestUser2.trashMailFromLabel(myMailSubject,"INBOX");
			assertTrue(success, "Failed deleting mail with subject:"+myMailSubject+".");
			success=false;
		}
//		sleep(EXPOSURE_WAIT_TIME);
		//waiting for exposure change
		 result=checkExposure("risky_docs", isInternal,  newFileName1,   0, EXPOSURE_WAIT_TIME_IN_MINS);
		assertTrue(result, "File not unexposed even after waiting for "+EXPOSURE_WAIT_TIME_IN_MINS+" mins.");
		sleep(60000);
		//After the exposure get the count
		exposedContentTypes = getExposedContentTypes("el_google_apps", additionalParams);
		afterMap = new HashMap<String, Integer>();

		for(TotalObject totalObject : exposedContentTypes.getObjects()) {
			afterMap.put(totalObject.getId(), totalObject.getTotal());
		}

		for(String key : afterMap.keySet()) {
			if(key.equals(filetype)) {
			Reporter.log("Verifying the type:"+key, true );
			CustomAssertion.assertTrue(  afterMap.get(key) <  currValue, "Expected : <"+currValue+" Actual: "+ (afterMap.get(key)));
			}
		}
	}
	
	@DataProvider(name = "dataProviderOtherRisksVerifyChangesInFileTypeCount")
	public static Object[][] dataProviderOtherRisksVerifyChangesInFileTypeCount() {
		return new Object[][] { 
			//testname, 				   exposureType, filename , risk type, 
			{ "Send Internal mail with a source code file", "true", "Hello.java",		 	"java", 		"APIServer" },
			
		};
	}
	
	    
		
		/**
		 * Test 23
		 * 
		 * end mail with a particular risk type to internal user and check the other risks - file types count gets incremented, and on deleting it from mail box the count is decremented.
		 * 
		 * 
		 */
		//@Test (dataProvider = "dataProviderOtherRisksVerifyChangesInFileTypeCount", groups={"DASHBOARD", "OTHER_RISKS_FILES", "FILTER"},retryAnalyzer=RetryAnalyzer.class)
		public void otherRisksVerifyChangesInFileTypeCount(String testname, String isInternal, String filename,  String filetype, String server) throws Exception {

			LogUtils.logTestDescription("Send mail with a particular risk type to internal user and check the other risks - file types count gets incremented, and on deleting it from mail box the count is decremented.");

			//before exposure
			HashMap<String, String> additionalParams = new HashMap<String, String>();
			additionalParams.put("exposed", "false");
			
			ExposureTotals exposedFileTypes = getFileTypes("el_google_apps", additionalParams);
			HashMap<String, Integer> beforeMap = new HashMap<String, Integer>();
			boolean success = false;
			for(TotalObject totalObject : exposedFileTypes.getObjects()) {
				beforeMap.put(totalObject.getId(), totalObject.getCount());
			}
			int currValue = 0;
			
			String myUniqueId = UUID.randomUUID().toString();
			ArrayList<String> myAttachment = new ArrayList<String>();
			File sourceFile1 = new File(userDir +filename);
			sourceFile1 = new File(userDir +filename);
			String newFileName1 = myUniqueId+filename;
			File destFile1 = new File(userDir +newFileName1);
			String myMailSubject = null, mailBody = null;

			
			
			
			myMailSubject = myUniqueId+"FileAttachment";
			copyFileUsingFileChannels(sourceFile1, destFile1);	
			filesToDelete.add(destFile1);
			myAttachment.add(destFile1.toString());
			mailBody= "This is test mail body";
			
			ArrayList<String> to = new ArrayList<String>();
			synchronized (this){
				to.add(testUser2);
				success = objMailTestUser1.sendMessageWithAttachment(to, null, null, myMailSubject, mailBody,sourceFile1.toString(),newFileName1);
				assertTrue(success, "Failed sending mail with subject:"+myMailSubject+".");
				success=false;
			}
			
			cleanupListSent.add(myMailSubject);
			cleanupListReceived.add(myMailSubject);
			
//			sleep(EXPOSURE_WAIT_TIME);
			//waiting for exposure change
			boolean result=checkExposure("risky_docs", isInternal,  newFileName1,   1, EXPOSURE_WAIT_TIME_IN_MINS);
			assertTrue(result, "File not exposed even after waiting for "+EXPOSURE_WAIT_TIME_IN_MINS+" mins.");
			sleep(60000);
			//After the exposure get the count
			exposedFileTypes = getFileTypes("el_google_apps", additionalParams);
			HashMap<String, Integer> afterMap = new HashMap<String, Integer>();
			
			for(TotalObject totalObject : exposedFileTypes.getObjects()) {
				afterMap.put(totalObject.getId(), totalObject.getCount());
			}
			
			for(String key : afterMap.keySet()) {
				Reporter.log("Verifying the type:"+key, true );
				if(key.equals(filetype)) {
//					int expectedValue = beforeMap.containsKey(key) ? beforeMap.get(key) + 1 : 0;
					
					currValue = afterMap.get(key);
					CustomAssertion.assertTrue( beforeMap.get(key) < afterMap.get(key), key + ". Actual :"+currValue+" Expected: >"+ (beforeMap.get(key)));
					
				} 
//				else {
//					CustomAssertion.assertEquals(afterMap.get(key), beforeMap.get(key),    key + " count should not get incremented for no expsoure");
//				}
			}
			
			
			synchronized (this){
				success=  objMailTestUser1.trashMailFromLabel(myMailSubject,"SENT");
				assertTrue(success, "Failed deleting mail with subject:"+myMailSubject+".");
				success=false;
				success=  objMailTestUser2.trashMailFromLabel(myMailSubject,"INBOX");
				assertTrue(success, "Failed deleting mail with subject:"+myMailSubject+".");
				success=false;
			}
			
			
//			sleep(EXPOSURE_WAIT_TIME);
			//waiting for exposure change
			
			result=checkExposure("risky_docs", isInternal,  newFileName1,   0, EXPOSURE_WAIT_TIME_IN_MINS);
			assertTrue(result, "File not unexposed even after waiting for "+EXPOSURE_WAIT_TIME_IN_MINS+" mins.");
			sleep(60000);
			//After the exposure get the count
			exposedFileTypes = getFileTypes("el_google_apps", additionalParams);
			afterMap = new HashMap<String, Integer>();

			for(TotalObject totalObject : exposedFileTypes.getObjects()) {
				afterMap.put(totalObject.getId(), totalObject.getCount());
			}

			for(String key : afterMap.keySet()) {
				if(key.equals(filetype)) {
				Reporter.log("Verifying the count after removing exposure :"+key, true );
				CustomAssertion.assertTrue(  afterMap.get(key) <  currValue, "Expected : <"+currValue+" Actual: "+ (afterMap.get(key)));
				}
			}
		}
		
		/**
		 * Test 24
		 * 
		 * List all other risks tab files and validate all documents
		 * @throws Exception
		 */
		@Test(groups={"DASHBOARD", "OTHER_RISKS_FILES", "FILTER", "DATA_CHECK"},retryAnalyzer=RetryAnalyzer.class)
		public void listAllOtherRisksFiles() throws Exception {
			LogUtils.logTestDescription("List all other risks tab files and validate all documents.");
			
			ArrayList<NameValuePair> additionalParams = new ArrayList<NameValuePair>();
			
			Reporter.log("Fetching the exposed documents to find the total count.", true);
			SecurletDocument documents = getRiskyDocuments( elapp.el_google_apps.name(), additionalParams);
			
			//Fetch all the documents in one query
			int limit = documents.getMeta().getTotalCount();
			additionalParams.add(new BasicNameValuePair("offset", "0"));
			additionalParams.add(new BasicNameValuePair("limit",  String.valueOf(limit)));
			Reporter.log("Fetching all the exposed documents", true);
			documents = getExposedDocuments( elapp.el_google_apps.name(), additionalParams);
			
			//Verify all internally owned documents
			Reporter.log("Validating all the other risks documents...", true);
			docValidator = new DocumentValidator();
			docValidator.validateExposedDocuments(documents, true);
		}
		
		@DataProvider(name = "ActivityTypeFilter")
	    public static Object[][] ActivityTypeFilter() {
	    	return new Object[][] { 
	    		
	    		 {"Create"}, {"Trash"}, {"Delete"}, {"Content Inspection"}, {"Receive"}, {"Send"} //{"Remove"},
	    		
	    	};
	    }
	    
	    
	    
	    @DataProvider(name = "ObjectTypeFilter")
	    public static Object[][] ObjectTypeFilter() {
	    	return new Object[][] { 
	    		{"Email_Message"}
	    	};
	    }
	    
	    @DataProvider(name = "SeverityFilter")
	    public static Object[][] SeverityFilter() {
	    	return new Object[][] { 
	    		{"informational"}, {"critical"}
	    	};
	    }
	    
	    @DataProvider(name = "UserFilter")
	    public static Object[][] UserFilter() {
	    	return new Object[][] { 
	    		{testUser1}, {testUser2}, {adminUser}
	    	};
	    }
		
		@Test(dataProvider = "ObjectTypeFilter", groups={"FILTERS", },retryAnalyzer=RetryAnalyzer.class)
		public void dashboardObjectTypeFilters(String objType) throws Exception {
			ForensicSearchResults logs;
			HashMap<String, String> termmap = new HashMap<String, String>();
			termmap.put("facility", "Gmail");
			termmap.put("Object_type", objType);
			LogUtils.logTestDescription("Retrieve the objecttype and filter them by name:"+ objType);
				for (int retry = 0; retry < 1; retry++) {

					try{
						String apiHost = suiteData.getScheme() + "://" + suiteData.getApiserverHostName();
						//Fetch the activity logs from yesterday to tomorrow and limited to 500
						//Get file related logs
						logs = getInvestigateLogs(-18000, 10, "Gmail", termmap, suiteData.getUsername().toLowerCase(), 
								apiHost, suiteData.getCSRFToken(), suiteData.getSessionID(), 0, 500, "Gmail");

						this.logValidator = new LogValidator(logs);

						logValidator.verifyMetadata();
						logValidator.verifyObjectType(objType);
						assertTrue(logs.getHits().getTotal() > 0, "ObjectType " + objType + " related messages are not present");
						}
					catch(Exception e) {
						//if any exception, please retry after waiting for 20 secs 
						sleep(20);
					}
			}
				
		}
		
		
		@Test(dataProvider = "ActivityTypeFilter",groups={"FILTERS", },retryAnalyzer=RetryAnalyzer.class)
		public void dashboardActivityTypeFilters(String activityType) throws Exception {
			ForensicSearchResults logs;
			LogUtils.logTestDescription("Retrieve the activities and filter them by name:"+ activityType);
			HashMap<String, String> termmap = new HashMap<String, String>();
			termmap.put("facility", "Gmail");
			termmap.put("Activity_type", activityType);
			
			for (int retry = 0; retry < 1; retry++) {

				try{
					String apiHost = suiteData.getScheme() + "://" + suiteData.getApiserverHostName();
					//Fetch the activity logs from yesterday to tomorrow and limited to 500
					//Get file related logs
					logs = getInvestigateLogs(-18000, 10, "Gmail", termmap, suiteData.getUsername().toLowerCase(), 
							apiHost, suiteData.getCSRFToken(), suiteData.getSessionID(), 0, 500, "Gmail");

					this.logValidator = new LogValidator(logs);

					logValidator.verifyMetadata();
					logValidator.verifyActivityType(activityType);
					assertTrue(logs.getHits().getTotal() > 0, "ActivityType " + activityType + " related messages are not present");
				}
				catch(Exception e) {}
			}
		}
		
		
		@Test(dataProvider = "SeverityFilter",groups={"FILTERS", },retryAnalyzer=RetryAnalyzer.class)
		public void dashboardSeverityTypeFilters(String severityType) throws Exception {
			ForensicSearchResults logs;
			LogUtils.logTestDescription("Retrieve the activities and filter them by name:"+ severityType);
			HashMap<String, String> termmap = new HashMap<String, String>();
			termmap.put("facility", "Gmail");
			termmap.put("severity", severityType);
			termmap.put("__source", "API");
			//termmap.put("user", this.saasAppUserAccount.getUsername());
			
			for (int retry = 0; retry < 1; retry++) {

				try{
					String apiHost = suiteData.getScheme() + "://" + suiteData.getApiserverHostName();
					//Fetch the activity logs from yesterday to tomorrow and limited to 500
					//Get file related logs
					logs = getInvestigateLogs(-180000, 10, "Gmail", termmap, suiteData.getUsername().toLowerCase(), 
							apiHost, suiteData.getCSRFToken(), suiteData.getSessionID(), 0, 500, "Gmail");

					this.logValidator = new LogValidator(logs);

					logValidator.verifyMetadata();
					logValidator.verifySeverityType(severityType);
					assertTrue(logs.getHits().getTotal() > 0, "SeverityType " + severityType + " related messages are not present");
				}
				catch(Exception e) {}
			}
		}
		
		@Test(dataProvider = "UserFilter",groups={"FILTERS"},retryAnalyzer=RetryAnalyzer.class)
		public void dashboardUserFilters(String userEmail) throws Exception {
			ForensicSearchResults logs;
			LogUtils.logTestDescription("Retrieve the activities and filter them by user:"+ userEmail);
			HashMap<String, String> termmap = new HashMap<String, String>();
			termmap.put("facility", "Gmail");
			termmap.put("user", userEmail);
			
			for (int retry = 0; retry < 1; retry++) {

				try{
					String apiHost = suiteData.getScheme() + "://" + suiteData.getApiserverHostName();
					//Fetch the activity logs from yesterday to tomorrow and limited to 500
					//Get file related logs
					logs = getInvestigateLogs(-18000, 10, "Gmail", termmap, suiteData.getUsername().toLowerCase(), 
							apiHost, suiteData.getCSRFToken(), suiteData.getSessionID(), 0, 500, "Gmail");

					this.logValidator = new LogValidator(logs);

					logValidator.verifyMetadata();
					logValidator.verifyUser(userEmail);
					assertTrue(logs.getHits().getTotal() > 0, "User " + userEmail + " related messages are not present");
				}
				catch(Exception e) {}
			}
		}
		

}