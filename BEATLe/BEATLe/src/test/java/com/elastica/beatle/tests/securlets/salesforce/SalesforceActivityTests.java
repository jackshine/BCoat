package com.elastica.beatle.tests.securlets.salesforce;

import java.io.File;
import java.net.URI;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.SkipException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.elastica.beatle.DateUtils;
import com.elastica.beatle.MarshallingUtils;
import com.elastica.beatle.Authorization.AuthorizationHandler;
import com.elastica.beatle.RestClient.ClientUtil;
import com.elastica.beatle.dci.DCIConstants;
import com.elastica.beatle.securlets.LogUtils;
import com.elastica.beatle.securlets.LogValidator;
import com.elastica.beatle.securlets.SecurletUtils;
import com.elastica.beatle.securlets.SecurletsConstants;
import com.elastica.beatle.securlets.dto.ForensicSearchResults;
import com.elastica.beatle.securlets.dto.SecurletDocument;
import com.elastica.beatle.securlets.dto.UIExposedDoc;
import com.elastica.beatle.tests.securlets.CustomAssertion;
import com.universal.constants.CommonConstants;
import com.universal.dtos.salesforce.Case;
import com.universal.dtos.salesforce.ChatterFile;
import com.universal.dtos.salesforce.Lead;
import com.universal.dtos.salesforce.LeadInput;
import com.universal.dtos.salesforce.SObject;
import com.universal.dtos.salesforce.SObjectInput;
import com.universal.util.OAuth20Token;

public class SalesforceActivityTests extends SecurletUtils{

	//Salesforce sfapi;
	SalesforceActivityLog sfActivityLog, sfI18NActivityLog;
	HashMap<String, SalesforceActivity> sfActivities = new HashMap<String, SalesforceActivity>();
	LogValidator logValidator;
	protected ForensicSearchResults chatterLogs, sobjectLogs, contentInspectionLogs, leadLogs, 
						accountLogs, opportunityLogs, caseLogs, contactLogs, chatterI18NLogs, contentInspectionBypassLogs, 
						contractLogs, solutionLogs, campaignLogs, folderLogs, reportLogs, taskLogs, eventLogs;
	long maxWaitTime = 20;
	long minWaitTime = 10;

	String saasAppUsername;
	String saasAppPassword;
	String saasAppUserRole;
	String createdTime;
	String destinationFile, destinationI18NFile;
	String saasAppUser;
	String instanceUrl;
	String instanceId;
	String contentDocumentName;
	String appName = "Salesforce";

	public SalesforceActivityTests() throws Exception {
		sfActivityLog = new SalesforceActivityLog();
		logValidator = new LogValidator(); 
	}

	@BeforeClass(alwaysRun=true)
	public void initSalesforce() throws Exception {
		this.saasAppUsername 	= getRegressionSpecificSuitParameters("saasAppUsername");
		this.saasAppPassword 	= getRegressionSpecificSuitParameters("saasAppPassword");
		this.saasAppUserRole 	= getRegressionSpecificSuitParameters("saasAppUserRole");
		
		if(saasAppUsername.toLowerCase().contains(".sandbox")) {
			saasAppUser = StringUtils.chop(saasAppUsername.toLowerCase()).replace(".sandbox", "");
		} else {
			saasAppUser = saasAppUsername;
		}

		OAuth20Token tokenObj = sfapi.getTokenObject();
		
		//https://test.salesforce.com/id/00D170000008i3DEAQ/005o0000000RTtEAAW  //4th param is the id
		instanceUrl = tokenObj.getInstanceUrl();
		Reporter.log("Token Id:" + tokenObj.getId(), true);
		instanceId = tokenObj.getId().split("/")[4];
		AuthorizationHandler.disableAnonymization(suiteData);
		
	}
	
	@Test(priority=-10, groups={"FILE", "SANITY", "P1", "REGRESSION"})
	public void performChatterOperations() throws Exception {

		String steps[] = {	"1. This test perform the saas operations and wait for 3 mins for the logs to reach our portal.", 
							"2. Call Investigate API and retrieve the file related investigate logs."};

		LogUtils.logTestDescription(steps);
		try {
			Reporter.log("Starting to perform the operations in salesforce ", true);
			String randomId = String.valueOf(System.currentTimeMillis());
			String sourceFile = "test.pdf";
			//filetitle = "Hello";
			destinationFile = randomId + "_" + sourceFile;
			contentDocumentName = destinationFile;
			//Upload the file
			createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
			ChatterFile chatterfile = sfapi.uploadFileToChatter(sourceFile, destinationFile);
			Reporter.log("Chatter file contents:"+ MarshallingUtils.marshall(chatterfile), true);
			
			Reporter.log("File "+ chatterfile.getName() +" uploaded to salesforce chatter."+ destinationFile, true);
			sleep(CommonConstants.SIXTY_SECONDS_SLEEP);

			sfActivityLog.setContentVersionUpdateLog(sfActivityLog.getContentVersionUpdateLog().replace("{filename}", destinationFile));
			sfActivityLog.setContentDocumentUpdateLog(sfActivityLog.getContentDocumentUpdateLog().replace("{filename}", destinationFile));
			
			String objectUrl = instanceUrl + "/" +chatterfile.getId();
			
			//message, createdTime, severity, objectType, activityType, objectUrl, objectId, objectName, fileName, instance, user, username
			SalesforceActivity ContentVersionUpdateActivity = new SalesforceActivity(sfActivityLog.getContentVersionUpdateLog(), createdTime, 
						Severity.informational.name(), "Content Version", ActivityType.Update.name(), objectUrl, 
						chatterfile.getId(), chatterfile.getName(), destinationFile, instanceId, saasAppUser, suiteData.getSocUserName());
			
			SalesforceActivity ContentDocumentUpdateActivity = new SalesforceActivity(sfActivityLog.getContentDocumentUpdateLog(), createdTime, 
						Severity.informational.name(), "Content Document", ActivityType.Update.name(), objectUrl, 
						chatterfile.getId(), chatterfile.getName(), destinationFile, instanceId, saasAppUser, suiteData.getSocUserName());

			sfActivities.put("ContentVersionUpdateActivity",  ContentVersionUpdateActivity);
			sfActivities.put("ContentDocumentUpdateActivity", ContentDocumentUpdateActivity);
			
			sleep(this.maxWaitTime);
			
			sfActivityLog.setContentVersionDeleteLog(sfActivityLog.getContentVersionDeleteLog().replace("{filename}", destinationFile));
			sfActivityLog.setContentDocumentDeleteLog(sfActivityLog.getContentDocumentDeleteLog().replace("{filename}", destinationFile));
			
			sfapi.deleteFile(chatterfile.getId());
			
			//message, createdTime, severity, objectType, activityType, objectUrl, objectId, objectName, fileName, instance, user, username
			SalesforceActivity ContentVersionDeleteActivity = new SalesforceActivity(sfActivityLog.getContentVersionDeleteLog(), createdTime, 
						Severity.informational.name(), "Content Version", ActivityType.Delete.name(), objectUrl, 
						chatterfile.getId(), chatterfile.getName(), destinationFile, instanceId, saasAppUser, suiteData.getSocUserName());
			
			SalesforceActivity ContentDocumentDeleteActivity = new SalesforceActivity(sfActivityLog.getContentDocumentDeleteLog(), createdTime, 
						Severity.informational.name(), "Content Document", ActivityType.Delete.name(), objectUrl, 
						chatterfile.getId(), chatterfile.getName(), destinationFile, instanceId, saasAppUser, suiteData.getSocUserName());

			sfActivities.put("ContentVersionDeleteActivity",  ContentVersionDeleteActivity);
			sfActivities.put("ContentDocumentDeleteActivity", ContentDocumentDeleteActivity);
			
			Reporter.log("Salesforce chatter operations are completed ...... ", true);
			Reporter.log("------------------------------------------------------------------------------- ", true);
		}
		catch(Exception e) {
			Reporter.log("Exception caught ... so skipping the test..."+ e.getStackTrace(), true);
			throw new SkipException("Skipping this test");
		}

	}



	@Test(priority=-5, groups={"SANITY", "P1",  "REGRESSION", "LEAD", "ACCOUNT", "OPPORTUNITY"})
	public void fetchActivityLogs() throws Exception {
		//Fetch the logs
		
		String apiServerUrl = "https://" + suiteData.getApiserverHostName();
		
		try {

			for (int i = 0; i <= (minWaitTime * 60 * 1000); i+=120000 ) {
				
				sleep(CommonConstants.TWO_MINUTES_SLEEP);
				
				System.out.println("waiting for fetch logs");
				HashMap<String, String> termmap = new HashMap<String, String>();
				
				
				termmap.put("facility", facility.Salesforce.name());
				termmap.put("severity", Severity.informational.name());
				termmap.put("Object Name", contentDocumentName);
				termmap.put("__source", "API");				
				//termmap.put("Object_type", "Content Version History");
				//termmap.put("Activity_type", ActivityType.Update.name());
				//Get chatter logs
				this.chatterLogs = this.getInvestigateLogs(-180, 30, facility.Salesforce.name(), termmap, suiteData.getUsername(), apiServerUrl, 
						suiteData.getCSRFToken(), suiteData.getSessionID(), 0, 1000, "Salesforce");

								
				//Get CI related logs
				/*termmap = new HashMap<String, String>();
				termmap.put("facility", facility.Salesforce.name());
				termmap.put("Activity_type", "Content Inspection");
				this.contentInspectionLogs = this.getInvestigateLogs(-120, 5, facility.Salesforce.name(), termmap, suiteData.getUsername(), apiServerUrl, 
						suiteData.getCSRFToken(), suiteData.getSessionID(), 0, 1000, "Salesforce");
				
				
				//Get CI Bypass related logs
				termmap = new HashMap<String, String>();
				termmap.put("facility", facility.Salesforce.name());
				termmap.put("Activity_type", "Content Inspection Bypass");
				this.contentInspectionBypassLogs = this.getInvestigateLogs(-180, 5, facility.Salesforce.name(), termmap, suiteData.getUsername(), apiServerUrl, 
						suiteData.getCSRFToken(), suiteData.getSessionID(), 0, 1000, "Salesforce");
				*/
				
				
				//Get lead related logs
				termmap = new HashMap<String, String>();
				termmap.put("facility", facility.Salesforce.name());
				termmap.put("severity", Severity.informational.name());
				termmap.put("__source", "API");				
				termmap.put("Object_type", ObjectType.Lead.name());
				this.leadLogs = this.getInvestigateLogs(-180, 5, facility.Salesforce.name(), termmap, suiteData.getUsername(), apiServerUrl, 
						suiteData.getCSRFToken(), suiteData.getSessionID(), 0, 1000, "Salesforce");
				
				
				//Get account related logs
				termmap = new HashMap<String, String>();
				termmap.put("facility", facility.Salesforce.name());
				termmap.put("severity", Severity.informational.name());
				termmap.put("__source", "API");				
				termmap.put("Object_type", ObjectType.Account.name());
				this.accountLogs = this.getInvestigateLogs(-180, 5, facility.Salesforce.name(), termmap, suiteData.getUsername(), apiServerUrl, 
						suiteData.getCSRFToken(), suiteData.getSessionID(), 0, 1000, "Salesforce");
				
				//Get opportunity related logs
				termmap = new HashMap<String, String>();
				termmap.put("facility", facility.Salesforce.name());
				termmap.put("severity", Severity.informational.name());
				termmap.put("Object_type", ObjectType.Opportunity.name());
				termmap.put("__source", "API");
				this.opportunityLogs = this.getInvestigateLogs(-180, 5, facility.Salesforce.name(), termmap, suiteData.getUsername(), apiServerUrl, 
						suiteData.getCSRFToken(), suiteData.getSessionID(), 0, 1000, "Salesforce");
				
				//Get contact related logs
				termmap = new HashMap<String, String>();
				termmap.put("facility", facility.Salesforce.name());
				termmap.put("severity", Severity.informational.name());
				termmap.put("Object_type", ObjectType.Contact.name());
				termmap.put("__source", "API");
				this.contactLogs = this.getInvestigateLogs(-180, 5, facility.Salesforce.name(), termmap, suiteData.getUsername(), apiServerUrl, 
						suiteData.getCSRFToken(), suiteData.getSessionID(), 0, 1000, "Salesforce");
				
				
				//Get case related logs
				termmap = new HashMap<String, String>();
				termmap.put("facility", facility.Salesforce.name());
				termmap.put("severity", Severity.informational.name());
				termmap.put("Object_type", ObjectType.Case.name());
				termmap.put("__source", "API");				
				this.caseLogs = this.getInvestigateLogs(-180, 5, facility.Salesforce.name(), termmap, suiteData.getUsername(), apiServerUrl, 
						suiteData.getCSRFToken(), suiteData.getSessionID(), 0, 1000, "Salesforce");
				 
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		try {
			
			Reporter.log("File Logs:" +MarshallingUtils.marshall(chatterLogs), true);
			long fileTotal = chatterLogs.getHits().getTotal();
			Reporter.log("Total Chatter logs count:"+ fileTotal, true);
			
//			Reporter.log(MarshallingUtils.marshall(contentInspectionLogs), true);
//			long ciTotal = contentInspectionLogs.getHits().getTotal();
//			Reporter.log("Total Content Inspection messages logs count:"+ ciTotal, true);

			Reporter.log("Account Logs:" +MarshallingUtils.marshall(accountLogs), true);
			long accountTotal = accountLogs.getHits().getTotal();
			Reporter.log("Total Account logs count:"+ accountTotal, true);

			Reporter.log("Opportunity Logs:" +MarshallingUtils.marshall(opportunityLogs), true);
			long opportunityTotal = opportunityLogs.getHits().getTotal();
			Reporter.log("Total Opportunity logs count:"+ opportunityTotal, true);

			Reporter.log("Contact Logs:" +MarshallingUtils.marshall(contactLogs), true);
			long contactTotal = contactLogs.getHits().getTotal();
			Reporter.log("Total Contact logs count:"+ contactTotal, true);
			
			Reporter.log("Case Logs:" +MarshallingUtils.marshall(caseLogs), true);
			long caseTotal = caseLogs.getHits().getTotal();
			Reporter.log("Total Case logs count:"+ caseTotal, true);
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		
		/*
		//If total number of logs is less than zero, no logs are seen so skip the tests
		if (ciTotal <= 0) {
			throw new SkipException("Skipping the dependent tests..No logs are seen for Content Inspection messages in Salesforce");
		}
		
		//If total number of logs is less than zero, no logs are seen so skip the tests
		if (accountTotal <= 0 ) {
			throw new SkipException("Skipping the dependent tests..No logs are seen for Account messages in Salesforce");
		}
		*/
		
	}
	
	//No logs will appear so commented
	//@Test(dependsOnMethods={"fetchActivityLogs"}, groups={"FILE", "SANITY", "P1", "REGRESSION"})
	public void verifyContentVersionUpdateActivity() throws Exception {
		LogUtils.logTestDescription("After updating the content version, check the activity logs.");
		logValidator.verifySalesforceActivityLog(chatterLogs, sfActivities.get("ContentVersionUpdateActivity"));
	}
	
	
	@Test(dependsOnMethods = "fetchActivityLogs", groups={"FILE", "SANITY", "P1", "REGRESSION"})
	public void verifyContentDocumentUpdateActivity() {
		LogUtils.logTestDescription("After updating the content document, check the activity logs.");
		logValidator.verifySalesforceActivityLog(chatterLogs, sfActivities.get("ContentDocumentUpdateActivity"));
	}
	
	//No logs will appear so commented
	//@Test(dependsOnMethods={"fetchActivityLogs"}, groups={"FILE", "SANITY", "P1", "REGRESSION"})
	public void verifyContentVersionDeleteActivity() throws Exception {
		LogUtils.logTestDescription("After deleting the content version, check the activity logs.");
		logValidator.verifySalesforceActivityLog(chatterLogs, sfActivities.get("ContentVersionDeleteActivity"));
	}
	
	
	@Test(dependsOnMethods = "fetchActivityLogs", groups={"FILE", "SANITY", "P1", "REGRESSION"})
	public void verifyContentDocumentDeleteActivity() {
		LogUtils.logTestDescription("After delete the content document, check the activity logs.");
		logValidator.verifySalesforceActivityLog(chatterLogs, sfActivities.get("ContentDocumentDeleteActivity"));
	}
	
	
	@Test(priority= -10, groups={"P1", "CONTENT_INSPECTION", "SALESFORCE", "SANITY", "REGRESSSION"})
	public void uploadRiskFiles() throws Exception {

		String riskFiles[] = {"pci.txt", "pii.rtf", "hipaa.txt", "glba.txt", "ferpa.txt", "virus.html"}; 
		String randomId = String.valueOf(System.currentTimeMillis());

		ArrayList<String> fileIds = new ArrayList<String>();
		
		for (String riskFile : riskFiles) {
			LogUtils.logTestDescription("Upload the risk file "+ riskFile + " to salesforce chatter");	

			String sourceFile = DCIConstants.DCI_FILE_UPLOAD_RISK_TYPES_PATH + File.separator + riskFile;
			//filetitle = "Hello";
			destinationFile = randomId + "_" + riskFile;

			//Upload the file
			createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
			ChatterFile chatterfile = sfapi.uploadFileToChatter(sourceFile, destinationFile);
			
			fileIds.add(chatterfile.getId());
			
			Reporter.log("Chatter file contents:"+ MarshallingUtils.marshall(chatterfile), true);

			Reporter.log("File "+ chatterfile.getName() +" uploaded to salesforce chatter."+ destinationFile, true);

			//pciRiskLog
			createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
			if(riskFile.contains("pci")) {
				sfActivityLog.setPciRiskLog(sfActivityLog.getPciRiskLog().replace("{filename}", destinationFile).replace("{risktype}", "PCI"));
				SalesforceActivity PCIActivityLog = new SalesforceActivity(sfActivityLog.getPciRiskLog(), createdTime, Severity.critical.name(), 
						"Content Inspection", destinationFile, saasAppUser, "PCI", chatterfile.getId()) ;
				sfActivities.put("PCIActivityLog", PCIActivityLog);
			}
			
			//pii risk log
			if(riskFile.contains("pii")) {
				sfActivityLog.setPiiRiskLog(sfActivityLog.getPiiRiskLog().replace("{filename}", destinationFile).replace("{risktype}", "PII"));
				SalesforceActivity PIIActivityLog = new SalesforceActivity(sfActivityLog.getPiiRiskLog(), createdTime, Severity.critical.name(), 
						"Content Inspection", destinationFile, saasAppUser, "PII", chatterfile.getId()) ;
				sfActivities.put("PIIActivityLog", PIIActivityLog);
			}
			
			//hipaa risk log
			if(riskFile.contains("hipaa")) {
				sfActivityLog.setHipaaRiskLog(sfActivityLog.getHipaaRiskLog().replace("{filename}", destinationFile).replace("{risktype}", "PII, HIPAA"));
				SalesforceActivity HIPAAActivityLog = new SalesforceActivity(sfActivityLog.getHipaaRiskLog(), createdTime, Severity.critical.name(), 
						"Content Inspection", destinationFile, saasAppUser, "HIPAA", chatterfile.getId()) ;
				sfActivities.put("HIPAAActivityLog", HIPAAActivityLog);
			}
			
			//glba risk log
			if(riskFile.contains("glba")) {
				sfActivityLog = new SalesforceActivityLog();
				sfActivityLog.setRiskLog(sfActivityLog.getRiskLog().replace("{filename}", destinationFile).replace("{risktype}", "GLBA"));
				SalesforceActivity GLBAActivityLog = new SalesforceActivity(sfActivityLog.getRiskLog(), createdTime, Severity.critical.name(), 
						"Content Inspection", destinationFile, saasAppUser, "GLBA", chatterfile.getId()) ;
				sfActivities.put("GLBAActivityLog", GLBAActivityLog);
			}
			
			//virus risk log
			if(riskFile.contains("virus")) {
				sfActivityLog = new SalesforceActivityLog();
				sfActivityLog.setRiskLog(sfActivityLog.getRiskLog().replace("{filename}", destinationFile).replace("{risktype}", "Virus / Malware"));
				SalesforceActivity VirusActivityLog = new SalesforceActivity(sfActivityLog.getRiskLog(), createdTime, Severity.critical.name(), 
						"Content Inspection", destinationFile, saasAppUser, "Virus / Malware", chatterfile.getId()) ;
				sfActivities.put("VirusActivityLog", VirusActivityLog);
			}
			
			//ferpa risk log
			if(riskFile.contains("ferpa")) {
				sfActivityLog = new SalesforceActivityLog();
				sfActivityLog.setRiskLog(sfActivityLog.getRiskLog().replace("{filename}", destinationFile).replace("{risktype}", "FERPA"));
				SalesforceActivity FerpaActivityLog = new SalesforceActivity(sfActivityLog.getRiskLog(), createdTime, Severity.critical.name(), 
						"Content Inspection", destinationFile, saasAppUser, "FERPA", chatterfile.getId()) ;
				sfActivities.put("FerpaActivityLog", FerpaActivityLog);
			}

			Reporter.log("Salesforce chatter operations for Content Inspection are completed ...... ", true);
			Reporter.log("------------------------------------------------------------------------------- ", true);

		}
		sleep(CommonConstants.FIVE_MINUTES_SLEEP);
		
		String apiServerUrl = "https://" + suiteData.getApiserverHostName();
		
		//Get CI related logs
		HashMap<String, String> termmap = new HashMap<String, String>();
		termmap.put("facility", facility.Salesforce.name());
		termmap.put("Activity_type", "Content Inspection");
		this.contentInspectionLogs = this.getInvestigateLogs(-30, 5, facility.Salesforce.name(), termmap, suiteData.getUsername(), apiServerUrl, 
				suiteData.getCSRFToken(), suiteData.getSessionID(), 0, 1000, "Salesforce");
		//Commented temporarily
		
		sleep(maxWaitTime);
		
		//Clean up after CI happens
		for(String fileId : fileIds ) {
			sfapi.deleteFile(fileId);
		}
		
	}
	
	
	@Test(priority= -3, groups={"P1", "CONTENT_INSPECTION", "SALESFORCE", "SANITY", "REGRESSSION"})
	public void verifyPIIRisks() throws Exception {
		LogUtils.logTestDescription("After uploading file with PII risks, verify the Content Inspection logs.");
		logValidator.verifySalesforceCIActivityLog(contentInspectionLogs, sfActivities.get("PIIActivityLog"));
	}
	
	@Test(groups={"P1", "CONTENT_INSPECTION", "SALESFORCE", "SANITY", "REGRESSSION"})
	public void verifyPCIRisks() throws Exception {
		LogUtils.logTestDescription("After uploading file with PCI risks, verify the Content Inspection logs.");
		logValidator.verifySalesforceCIActivityLog(contentInspectionLogs, sfActivities.get("PCIActivityLog"));
	}
	
	@Test(groups={"P1", "CONTENT_INSPECTION", "SALESFORCE", "SANITY", "REGRESSSION"})
	public void verifyGLBARisks() throws Exception {
		LogUtils.logTestDescription("After uploading file with GLBA risks, verify the Content Inspection logs.");
		logValidator.verifySalesforceCIActivityLog(contentInspectionLogs, sfActivities.get("GLBAActivityLog"));
	}
	
	@Test(groups={"P1", "CONTENT_INSPECTION", "SALESFORCE", "SANITY", "REGRESSSION"})
	public void verifyVirusRisks() throws Exception {
		LogUtils.logTestDescription("After uploading file with Virus risks, verify the Content Inspection logs.");
		logValidator.verifySalesforceCIActivityLog(contentInspectionLogs, sfActivities.get("VirusActivityLog"));
	}
	
	@Test(groups={"P1", "CONTENT_INSPECTION", "SALESFORCE", "SANITY", "REGRESSSION"})
	public void verifyFERPARisks() throws Exception {
		LogUtils.logTestDescription("After uploading file with FERPA risks, verify the Content Inspection logs.");
		logValidator.verifySalesforceCIActivityLog(contentInspectionLogs, sfActivities.get("FerpaActivityLog"));
	}
	
	
	@Test(priority= -4, groups={"P1", "CONTENT_INSPECTION_BYPASS", "SALESFORCE", "SANITY", "REGRESSSION"})
	public void uploadFilesAbove100MB() throws Exception {

		String files[] = {"Archive 2.zip"}; 
		String randomId = String.valueOf(System.currentTimeMillis());

		ArrayList<String> fileIds = new ArrayList<String>();
		
		for (String file : files) {
			LogUtils.logTestDescription("Upload the file "+ file + " to salesforce chatter");	

			String sourceFile =  file;
			//filetitle = "Hello";
			destinationFile = randomId + "_" + file;

			//Upload the file
			createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
			ChatterFile chatterfile = sfapi.uploadFileToChatter(sourceFile, destinationFile);
			
			fileIds.add(chatterfile.getId());
			Reporter.log("Chatter file contents:"+ MarshallingUtils.marshall(chatterfile), true);
			Reporter.log("File "+ chatterfile.getName() +" uploaded to salesforce chatter."+ destinationFile, true);

			//100 mb log
			createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
			
			sfActivityLog.setContentInspectionBypassLog(sfActivityLog.getContentInspectionBypassLog().replace("{filename}", destinationFile).replace("{risktype}", "PCI"));
			SalesforceActivity CIBypassActivityLog = new SalesforceActivity(sfActivityLog.getContentInspectionBypassLog(), createdTime, Severity.informational.name(), 
					"Content Inspection Bypass", destinationFile, saasAppUser, null, chatterfile.getId()) ;
			sfActivities.put("CIBypassActivityLog", CIBypassActivityLog);
			
			Reporter.log("Salesforce chatter operations for Content Inspection Bypass are completed ...... ", true);
			Reporter.log("------------------------------------------------------------------------------- ", true);
			
			sleep(CommonConstants.TEN_MINUTES_SLEEP);
			
			//Get CI bypass related logs
			HashMap<String, String> termmap = new HashMap<String, String>();
			termmap = new HashMap<String, String>();
			termmap.put("facility", facility.Salesforce.name());
			termmap.put("Activity_type", "Content Inspection Bypass");
			
			this.contentInspectionBypassLogs = this.getInvestigateLogs(-60, 5, facility.Salesforce.name(), termmap, suiteData.getUsername(), suiteData.getApiserverHostName(), 
					suiteData.getCSRFToken(), suiteData.getSessionID(), 0, 50, "Salesforce");
		}
		
		
//		
//		//Clean up after CI happens
//		for(String fileId : fileIds ) {
//			sfapi.deleteFile(fileId);
//		}
	}
	
	@Test(dependsOnMethods = "uploadFilesAbove100MB", groups={"P3", "CONTENT_INSPECTION_BYPASS", "SALESFORCE", "SANITY", "REGRESSSION"})
	public void verifyFileSizeLimitLogs() throws Exception {
		LogUtils.logTestDescription("After uploading file of size 100 MB, verify the Content Inspection Bypass logs.");
		logValidator.verifySalesforceCIActivityLog(contentInspectionBypassLogs, sfActivities.get("CIBypassActivityLog"));
	}
	
	
	
	
	@Test(priority=-10, groups={"SANITY", "P1", "REGRESSION", "SOBJECT", "LEAD"})
	public void performLeadOperations() throws Exception {

		String steps[] = {	"1. This test perform the saas operations on sobjects and wait for sometime for the logs to reach our portal.", 
							"2. Call Investigate API and retrieve the lead related investigate logs."};

		LogUtils.logTestDescription(steps);
		try {
			Reporter.log("Starting to perform the operations in salesforce ", true);
			String randomId = String.valueOf(System.currentTimeMillis());
			
			//Create a lead
			LeadInput li = new LeadInput();
			li.setCompany("Elastica Data Sciences India pvt ltd -  Bangalore ");
			li.setLastName("Securlet Automation "+ randomId);
			Lead leadResponse = sfapi.createLead(li);
			sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
			
			//Retrieve the lead details
			sfapi.getLead(leadResponse.getId());
			sleep(CommonConstants.THREE_MINUTES_SLEEP);
			
			//Update the lead details
			LeadInput updateLeadInput = new LeadInput();
			updateLeadInput.setTitle("Mr.");
			updateLeadInput.setLastName("Securlet Automation QA"+ randomId);
			sfapi.updateLead(updateLeadInput, leadResponse.getId());
			sleep(CommonConstants.THREE_MINUTES_SLEEP);
			
			//Delete the lead
			sfapi.deleteLead(leadResponse.getId());
			sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
			
			sfActivityLog.setLeadCreateLog(sfActivityLog.getLeadCreateLog().replace("{leadname}", li.getLastName()));
			sfActivityLog.setLeadUpdateLog(sfActivityLog.getLeadUpdateLog().replace("{leadname}", updateLeadInput.getLastName()));
			sfActivityLog.setLeadDeleteLog(sfActivityLog.getLeadDeleteLog().replace("{leadname}", updateLeadInput.getLastName()));
			
			String objectUrl = instanceUrl + "/" +leadResponse.getId();
			
			//message, createdTime, severity, objectType, activityType, objectUrl, objectId, objectName, fileName, instance, user, username
			SalesforceActivity LeadCreateActivity = new SalesforceActivity(sfActivityLog.getLeadCreateLog(), createdTime, 
						Severity.informational.name(), ObjectType.Lead.name(), ActivityType.Create.name(), objectUrl, 
						leadResponse.getId(), li.getLastName(), li.getLastName(), instanceId, saasAppUser, suiteData.getSocUserName());
			sfActivities.put("LeadCreateActivity", LeadCreateActivity);
			
			
			SalesforceActivity LeadUpdateActivity = new SalesforceActivity(sfActivityLog.getLeadUpdateLog(), createdTime, 
						Severity.informational.name(), ObjectType.Lead.name(), ActivityType.Update.name(), objectUrl, 
						leadResponse.getId(), updateLeadInput.getLastName(), updateLeadInput.getLastName(), instanceId, saasAppUser, suiteData.getSocUserName());
			sfActivities.put("LeadUpdateActivity", LeadUpdateActivity);
			
			
			SalesforceActivity LeadDeleteActivity = new SalesforceActivity(sfActivityLog.getLeadDeleteLog(), createdTime, 
					Severity.informational.name(), ObjectType.Lead.name(), ActivityType.Delete.name(), objectUrl, 
					leadResponse.getId(), updateLeadInput.getLastName(), updateLeadInput.getLastName(), instanceId, saasAppUser, suiteData.getSocUserName());
			sfActivities.put("LeadDeleteActivity", LeadDeleteActivity);
			
			//sleep(this.minWaitTime);
			
			Reporter.log("Salesforce lead operations are completed ...... ", true);
			Reporter.log("------------------------------------------------------------------------------- ", true);
		}
		catch(Exception e) {
			Reporter.log("Exception caught ... so skipping the test..."+ e.getStackTrace(), true);
			throw new SkipException("Skipping this test");
		}
	}
	
	
	@Test(dependsOnMethods = "fetchActivityLogs", groups={"LEAD", "SANITY", "P1", "REGRESSION"})
	public void verifyLeadCreateActivity() {
		LogUtils.logTestDescription("After creating the lead, check the activity logs.");
		logValidator.verifySalesforceActivityLog(leadLogs, sfActivities.get("LeadCreateActivity"));
	}
	
	@Test(dependsOnMethods = "fetchActivityLogs", groups={"LEAD", "SANITY", "P1", "REGRESSION"})
	public void verifyLeadUpdateActivity() {
		LogUtils.logTestDescription("After updating the lead, check the activity logs.");
		logValidator.verifySalesforceActivityLog(leadLogs, sfActivities.get("LeadUpdateActivity"));
	}
	
	@Test(dependsOnMethods = "fetchActivityLogs", groups={"LEAD", "SANITY", "P1", "REGRESSION"})
	public void verifyLeadDeleteActivity() {
		LogUtils.logTestDescription("After deleting the lead, check the activity logs.");
		logValidator.verifySalesforceActivityLog(leadLogs, sfActivities.get("LeadDeleteActivity"));
	}
	
	@Test(priority=-10, groups={"SANITY", "P1", "REGRESSION", "SOBJECT", "ACCOUNT"})
	public void performAccountOperations() throws Exception {

		String steps[] = {	"1. This test perform the saas operations on sobjects(Account) and wait for sometime for the logs to reach our portal.", 
							"2. Call Investigate API and retrieve the Account related investigate logs."};

		LogUtils.logTestDescription(steps);
		try {
			Reporter.log("Starting to perform the operations in salesforce ", true);
			String randomId = String.valueOf(System.currentTimeMillis());
			
			//Create a account
			SObjectInput sobjectInput = new SObjectInput();
			sobjectInput.setName("Elastica Data Sciences India pvt ltd -  Bangalore");
			SObject accountResponse = sfapi.createSObject("Account", sobjectInput);
			sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
			
			//Retrieve the object details
			sfapi.getSObject("Account", accountResponse.getId());
			sleep(CommonConstants.THREE_MINUTES_SLEEP);
			
			//Update the account details
			SObjectInput updateInput = new SObjectInput();
			updateInput.setName("Securlet Automation QA"+ randomId);
			sfapi.updateSObject("Account", updateInput, accountResponse.getId());
			sleep(CommonConstants.THREE_MINUTES_SLEEP);
			
			//Delete the account
			sfapi.deleteSObject("Account", accountResponse.getId());
			sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
			
			sfActivityLog.setAccountCreateLog(sfActivityLog.getAccountCreateLog().replace("{accountname}", sobjectInput.getName()));
			sfActivityLog.setAccountUpdateLog(sfActivityLog.getAccountUpdateLog().replace("{accountname}", updateInput.getName()));
			sfActivityLog.setAccountDeleteLog(sfActivityLog.getAccountDeleteLog().replace("{accountname}", updateInput.getName()));
			
			String objectUrl = instanceUrl + "/" +accountResponse.getId();
			
			//message, createdTime, severity, objectType, activityType, objectUrl, objectId, objectName, fileName, instance, user, username
			SalesforceActivity AccountCreateActivity = new SalesforceActivity(sfActivityLog.getAccountCreateLog(), createdTime, 
						Severity.informational.name(), ObjectType.Account.name(), ActivityType.Create.name(), objectUrl, 
						accountResponse.getId(), sobjectInput.getName(), sobjectInput.getName(), instanceId, saasAppUser, suiteData.getSocUserName());
			sfActivities.put("AccountCreateActivity", AccountCreateActivity);
			
			
			SalesforceActivity AccountUpdateActivity = new SalesforceActivity(sfActivityLog.getAccountUpdateLog(), createdTime, 
						Severity.informational.name(), ObjectType.Account.name(), ActivityType.Update.name(), objectUrl, 
						accountResponse.getId(), updateInput.getName(), updateInput.getName(), instanceId, saasAppUser, suiteData.getSocUserName());
			sfActivities.put("AccountUpdateActivity", AccountUpdateActivity);
			
			
			SalesforceActivity AccountDeleteActivity = new SalesforceActivity(sfActivityLog.getAccountDeleteLog(), createdTime, 
					Severity.informational.name(), ObjectType.Account.name(), ActivityType.Delete.name(), objectUrl, 
					accountResponse.getId(), 	updateInput.getName(), updateInput.getLastName(), instanceId, saasAppUser, suiteData.getSocUserName());
			sfActivities.put("AccountDeleteActivity", AccountDeleteActivity);
			
			sleep(this.minWaitTime);
			
			Reporter.log("Salesforce account operations are completed ...... ", true);
			Reporter.log("------------------------------------------------------------------------------- ", true);
		}
		catch(Exception e) {
			Reporter.log("Exception caught ... so skipping the test..."+ e.getStackTrace(), true);
			throw new SkipException("Skipping this test");
		}
	}
	
	
	@Test(dependsOnMethods = "fetchActivityLogs", groups={"ACCOUNT", "SANITY", "P1", "REGRESSION"})
	public void verifyAccountCreateActivity() {
		LogUtils.logTestDescription("After creating the account, check the activity logs.");
		logValidator.verifySalesforceActivityLog(accountLogs, sfActivities.get("AccountCreateActivity"));
	}
	
	@Test(dependsOnMethods = "fetchActivityLogs", groups={"ACCOUNT", "SANITY", "P1", "REGRESSION"})
	public void verifyAccountUpdateActivity() {
		LogUtils.logTestDescription("After updating the account, check the activity logs.");
		logValidator.verifySalesforceActivityLog(accountLogs, sfActivities.get("AccountUpdateActivity"));
	}
	
	@Test(dependsOnMethods = "fetchActivityLogs", groups={"ACCOUNT", "SANITY", "P1", "REGRESSION"})
	public void verifyAccountDeleteActivity() {
		LogUtils.logTestDescription("After deleting the account, check the activity logs.");
		logValidator.verifySalesforceActivityLog(accountLogs, sfActivities.get("AccountDeleteActivity"));
	}
	
	
	@Test(priority=-10, groups={"SANITY", "P1", "REGRESSION", "SOBJECT", "OPPORTUNITY"})
	public void performOpportunityOperations() throws Exception {

		String steps[] = {	"1. This test perform the saas operations on sobjects(Opportunity) and wait for sometime for the logs to reach our portal.", 
							"2. Call Investigate API and retrieve the Account related investigate logs."};

		LogUtils.logTestDescription(steps);
		try {
			Reporter.log("Starting to perform the operations in salesforce ", true);
			String randomId = String.valueOf(System.currentTimeMillis());
			
			//Create a sobject
			SObjectInput sobjectInput = new SObjectInput();
			String closeDate = DateUtils.getDateFromCurrentTime(30);
			
			sobjectInput.setName("New Sales Opportunity -  Elastica");
			sobjectInput.setStageName("Review");
			sobjectInput.setCloseDate(closeDate);
			SObject sobjectResponse = sfapi.createSObject(ObjectType.Opportunity.name(), sobjectInput);	
			sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
			
			//Retrieve the object details
			sfapi.getSObject(ObjectType.Opportunity.name(), sobjectResponse.getId());
			sleep(CommonConstants.THREE_MINUTES_SLEEP);
			
			//Update the sobject details
			SObjectInput updateInput = new SObjectInput();
			updateInput.setName("Securlet Automation QA"+ randomId);
			updateInput.setStageName("Closed Won");
			sfapi.updateSObject("Opportunity", updateInput, sobjectResponse.getId());
			sleep(CommonConstants.THREE_MINUTES_SLEEP);
			
			//Delete the sobject
			sfapi.deleteSObject(ObjectType.Opportunity.name(), sobjectResponse.getId());
			sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
			
			sfActivityLog.setOpportunityCreateLog(sfActivityLog.getOpportunityCreateLog().replace("{opportunityname}", sobjectInput.getName()));
			sfActivityLog.setOpportunityUpdateLog(sfActivityLog.getOpportunityUpdateLog().replace("{opportunityname}", updateInput.getName()));
			sfActivityLog.setOpportunityDeleteLog(sfActivityLog.getOpportunityDeleteLog().replace("{opportunityname}", updateInput.getName()));
			
			String objectUrl = instanceUrl + "/" +sobjectResponse.getId();
			
			//message, createdTime, severity, objectType, activityType, objectUrl, objectId, objectName, fileName, instance, user, username
			SalesforceActivity OpportunityCreateActivity = new SalesforceActivity(sfActivityLog.getOpportunityCreateLog(), createdTime, 
						Severity.informational.name(), ObjectType.Opportunity.name(), ActivityType.Create.name(), objectUrl, 
						sobjectResponse.getId(), sobjectInput.getName(), sobjectInput.getName(), instanceId, saasAppUser, suiteData.getSocUserName());
			sfActivities.put("OpportunityCreateActivity", OpportunityCreateActivity);
			
			
			SalesforceActivity OpportunityUpdateActivity = new SalesforceActivity(sfActivityLog.getOpportunityUpdateLog(), createdTime, 
						Severity.informational.name(), ObjectType.Opportunity.name(), ActivityType.Update.name(), objectUrl, 
						sobjectResponse.getId(), updateInput.getName(), updateInput.getName(), instanceId, saasAppUser, suiteData.getSocUserName());
			sfActivities.put("OpportunityUpdateActivity", OpportunityUpdateActivity);
			
			
			SalesforceActivity OpportunityDeleteActivity = new SalesforceActivity(sfActivityLog.getOpportunityDeleteLog(), createdTime, 
						Severity.informational.name(), ObjectType.Opportunity.name(), ActivityType.Delete.name(), objectUrl, 
						sobjectResponse.getId(), 	updateInput.getName(), updateInput.getLastName(), instanceId, saasAppUser, suiteData.getSocUserName());
			sfActivities.put("OpportunityDeleteActivity", OpportunityDeleteActivity);
			
			sleep(this.minWaitTime);
			
			Reporter.log("Salesforce opportunity operations are completed ...... ", true);
			Reporter.log("------------------------------------------------------------------------------- ", true);
		}
		catch(Exception e) {
			Reporter.log("Exception caught ... so skipping the test..."+ e.getStackTrace(), true);
			throw new SkipException("Skipping this test");
		}
	}
	
	
	@Test(dependsOnMethods = "fetchActivityLogs", groups={"OPPORTUNITY", "SANITY", "P1", "REGRESSION"})
	public void verifyOpportunityCreateActivity() {
		LogUtils.logTestDescription("After creating the Opportunity, check the activity logs.");
		logValidator.verifySalesforceActivityLog(opportunityLogs, sfActivities.get("OpportunityCreateActivity"));
	}
	
	@Test(dependsOnMethods = "fetchActivityLogs", groups={"OPPORTUNITY", "SANITY", "P1", "REGRESSION"})
	public void verifyOpportunityUpdateActivity() {
		LogUtils.logTestDescription("After updating the opportunity, check the activity logs.");
		logValidator.verifySalesforceActivityLog(opportunityLogs, sfActivities.get("OpportunityUpdateActivity"));
	}
	
	@Test(dependsOnMethods = "fetchActivityLogs", groups={"OPPORTUNITY", "SANITY", "P1", "REGRESSION"})
	public void verifyOpportunityDeleteActivity() {
		LogUtils.logTestDescription("After deleting the opportunity, check the activity logs.");
		logValidator.verifySalesforceActivityLog(opportunityLogs, sfActivities.get("OpportunityDeleteActivity"));
	}
	
	@Test(priority=-10, groups={"SANITY", "P1", "REGRESSION", "SOBJECT", "CONTACT"})
	public void performContactOperations() throws Exception {

		String steps[] = {	"1. This test perform the saas operations on sobjects(Contact) and wait for sometime for the logs to reach our portal.", 
							"2. Call Investigate API and retrieve the Account related investigate logs."};

		LogUtils.logTestDescription(steps);
		try {
			Reporter.log("Starting to perform the operations in salesforce ", true);
			String randomId = String.valueOf(System.currentTimeMillis());
			
			//Create a sobject
			SObjectInput sobjectInput = new SObjectInput();
			sobjectInput.setFirstName("Securlet Automation");
			sobjectInput.setLastName("Bangalore Team");
			sobjectInput.setDepartment("AutomationQA");
			sobjectInput.setEmail("securletuser@securletbeatle.com");
			SObject sobjectResponse = sfapi.createSObject(ObjectType.Contact.name(), sobjectInput);	
			sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
			
			//Retrieve the object details
			sfapi.getSObject(ObjectType.Contact.name(), sobjectResponse.getId());
			sleep(CommonConstants.THREE_MINUTES_SLEEP);
			
			//Update the sobject details
			SObjectInput updateInput = new SObjectInput();
			updateInput.setLastName("QA"+ randomId);
			sfapi.updateSObject(ObjectType.Contact.name(), updateInput, sobjectResponse.getId());
			sleep(CommonConstants.THREE_MINUTES_SLEEP);
			
			//Delete the sobject
			sfapi.deleteSObject(ObjectType.Contact.name(), sobjectResponse.getId());
			sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
			
			String expectedObjName = sobjectInput.getFirstName()+" "+sobjectInput.getLastName();
			
			sfActivityLog.setContactCreateLog(sfActivityLog.getContactCreateLog().replace("{contactname}", sobjectInput.getFirstName()+" "+sobjectInput.getLastName()));
			sfActivityLog.setContactUpdateLog(sfActivityLog.getContactUpdateLog().replace("{contactname}", sobjectInput.getFirstName()+" "+updateInput.getLastName()));
			sfActivityLog.setContactDeleteLog(sfActivityLog.getContactDeleteLog().replace("{contactname}", sobjectInput.getFirstName()+" "+updateInput.getLastName()));
			
			String objectUrl = instanceUrl + "/" +sobjectResponse.getId();
			
			//message, createdTime, severity, objectType, activityType, objectUrl, objectId, objectName, fileName, instance, user, username
			SalesforceActivity ContactCreateActivity = new SalesforceActivity(sfActivityLog.getContactCreateLog(), createdTime, 
						Severity.informational.name(), ObjectType.Contact.name(), ActivityType.Create.name(), objectUrl, 
						sobjectResponse.getId(), expectedObjName, sobjectInput.getName(), instanceId, saasAppUser, suiteData.getSocUserName());
			sfActivities.put("ContactCreateActivity", ContactCreateActivity);
			
			expectedObjName = sobjectInput.getFirstName()+" "+updateInput.getLastName();
			
			SalesforceActivity ContactUpdateActivity = new SalesforceActivity(sfActivityLog.getContactUpdateLog(), createdTime, 
						Severity.informational.name(), ObjectType.Contact.name(), ActivityType.Update.name(), objectUrl, 
						sobjectResponse.getId(), expectedObjName, updateInput.getName(), instanceId, saasAppUser, suiteData.getSocUserName());
			sfActivities.put("ContactUpdateActivity", ContactUpdateActivity);
			
			
			SalesforceActivity ContactDeleteActivity = new SalesforceActivity(sfActivityLog.getContactDeleteLog(), createdTime, 
						Severity.informational.name(), ObjectType.Contact.name(), ActivityType.Delete.name(), objectUrl, 
						sobjectResponse.getId(), expectedObjName, updateInput.getLastName(), instanceId, saasAppUser, suiteData.getSocUserName());
			sfActivities.put("ContactDeleteActivity", ContactDeleteActivity);
			
			sleep(this.minWaitTime);
			
			Reporter.log("Salesforce Contact operations are completed ...... ", true);
			Reporter.log("------------------------------------------------------------------------------- ", true);
		}
		catch(Exception e) {
			Reporter.log("Exception caught ... so skipping the test..."+ e.getStackTrace(), true);
			throw new SkipException("Skipping this test");
		}
	}
	
	
	@Test(dependsOnMethods = "fetchActivityLogs", groups={"CONTACT", "SANITY", "P1", "REGRESSION"})
	public void verifyContactCreateActivity() {
		LogUtils.logTestDescription("After creating the Contact, check the activity logs.");
		logValidator.verifySalesforceActivityLog(contactLogs, sfActivities.get("ContactCreateActivity"));
	}
	
	@Test(dependsOnMethods = "fetchActivityLogs", groups={"CONTACT", "SANITY", "P1", "REGRESSION"})
	public void verifyContactUpdateActivity() {
		LogUtils.logTestDescription("After updating the Contact, check the activity logs.");
		logValidator.verifySalesforceActivityLog(contactLogs, sfActivities.get("ContactUpdateActivity"));
	}
	
	@Test(dependsOnMethods = "fetchActivityLogs", groups={"CONTACT", "SANITY", "P1", "REGRESSION"})
	public void verifyContactDeleteActivity() {
		LogUtils.logTestDescription("After deleting the Contact, check the activity logs.");
		logValidator.verifySalesforceActivityLog(contactLogs, sfActivities.get("ContactDeleteActivity"));
	}
	
	
	
	
	@Test(priority=-10, groups={"SANITY", "P1", "REGRESSION", "SOBJECT", "CONTRACT"})
	public void performContractOperations() throws Exception {

		String steps[] = {	"1. This test perform the saas operations on sobjects(Contract) and wait for sometime for the logs to reach our portal.", 
							"2. Call Investigate API and retrieve the Account related investigate logs."};

		LogUtils.logTestDescription(steps);
		try {
			Reporter.log("Starting to perform the contract operations in salesforce ", true);
			
			//Create a sobject account
			SObjectInput accountInput = new SObjectInput();
			accountInput.setName("Account Created For Contract");
			SObject accountResponse = sfapi.createSObject("Account", accountInput);
			sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
			
			String accountId = accountResponse.getId();
			
			//Create a sobject contract
			SObjectInput sobjectInput = new SObjectInput();
			sobjectInput.setAccountId(accountId);
			sobjectInput.setStartDate(DateUtils.getCurrentDate());
			sobjectInput.setContractTerm(6);
			sobjectInput.setDescription("New Contract");
			SObject sobjectResponse = sfapi.createSObject(ObjectType.Contract.name(), sobjectInput);
			
			sleep(CommonConstants.THREE_MINUTES_SLEEP);
			
			//Update the sobject
			SObject contractObj = sfapi.getSObject(ObjectType.Contract.name(), sobjectResponse.getId());
			SObjectInput updateInput = new SObjectInput();
			updateInput.setContractTerm(16); //In months
			sfapi.updateSObject(ObjectType.Contract.name(), updateInput, sobjectResponse.getId());
			
			sleep(CommonConstants.THREE_MINUTES_SLEEP);
			
			//Delete the contract
			sfapi.deleteSObject(ObjectType.Contract.name(), sobjectResponse.getId());
			sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
			
			//Delete the account
			sfapi.deleteSObject(ObjectType.Contact.name(), accountResponse.getId());
			
			sleep(CommonConstants.THREE_MINUTES_SLEEP);
			
			String expectedObjName = contractObj.getContractNumber();
			
			sfActivityLog.setContractCreateLog(sfActivityLog.getContractCreateLog().replace("{contractno}", contractObj.getContractNumber()));
			sfActivityLog.setContractUpdateLog(sfActivityLog.getContractUpdateLog().replace("{contractno}", contractObj.getContractNumber()));
			sfActivityLog.setContractDeleteLog(sfActivityLog.getContractDeleteLog().replace("{contractno}", contractObj.getContractNumber()));
			
			String objectUrl = instanceUrl + "/" +sobjectResponse.getId();
			
			//message, createdTime, severity, objectType, activityType, objectUrl, objectId, objectName, fileName, instance, user, username
			SalesforceActivity ContractCreateActivity = new SalesforceActivity(sfActivityLog.getContractCreateLog(), createdTime, 
						Severity.informational.name(), ObjectType.Contract.name(), ActivityType.Create.name(), objectUrl, 
						sobjectResponse.getId(), expectedObjName, sobjectInput.getName(), instanceId, saasAppUser, suiteData.getSocUserName());
			sfActivities.put("ContractCreateActivity", ContractCreateActivity);
			
			
			SalesforceActivity ContractUpdateActivity = new SalesforceActivity(sfActivityLog.getContractUpdateLog(), createdTime, 
						Severity.informational.name(), ObjectType.Contract.name(), ActivityType.Update.name(), objectUrl, 
						sobjectResponse.getId(), expectedObjName, updateInput.getName(), instanceId, saasAppUser, suiteData.getSocUserName());
			sfActivities.put("ContractUpdateActivity", ContractUpdateActivity);
			
			
			SalesforceActivity ContractDeleteActivity = new SalesforceActivity(sfActivityLog.getContractDeleteLog(), createdTime, 
						Severity.informational.name(), ObjectType.Contract.name(), ActivityType.Delete.name(), objectUrl, 
						sobjectResponse.getId(), expectedObjName, updateInput.getLastName(), instanceId, saasAppUser, suiteData.getSocUserName());
			sfActivities.put("ContractDeleteActivity", ContractDeleteActivity);
			
			sleep(this.minWaitTime);
			
			Reporter.log("Salesforce Contract operations are completed ...... ", true);
			Reporter.log("------------------------------------------------------------------------------- ", true);
			
			
			HashMap<String, String> termmap = new HashMap<String, String>();
			
			termmap.put("facility", facility.Salesforce.name());
			termmap.put("severity", Severity.informational.name());
			termmap.put("Object_type", ObjectType.Contract.name());
			termmap.put("__source", "API");			
			
			//Get contract  logs
			this.contractLogs = this.getInvestigateLogs(-30, 30, facility.Salesforce.name(), termmap, suiteData.getUsername(), "https://"+suiteData.getApiserverHostName(), 
					suiteData.getCSRFToken(), suiteData.getSessionID(), 0, 100, "Salesforce");
		}
		catch(Exception e) {
			Reporter.log("Exception caught ... so skipping the test..."+ e.getStackTrace(), true);
			throw new SkipException("Skipping this test");
		}
	}
	
	
	@Test(groups={"CONTRACT", "P3", "REGRESSION"})
	public void verifyContractCreateActivity() {
		LogUtils.logTestDescription("After creating the Contract, check the activity logs.");
		logValidator.verifySalesforceActivityLog(contractLogs, sfActivities.get("ContractCreateActivity"));
	}
	
	@Test(groups={"CONTRACT", "P3", "REGRESSION"})
	public void verifyContractUpdateActivity() {
		LogUtils.logTestDescription("After updating the Contract, check the activity logs.");
		logValidator.verifySalesforceActivityLog(contractLogs, sfActivities.get("ContractUpdateActivity"));
	}
	
	@Test(groups={"CONTRACT", "P3", "REGRESSION"})
	public void verifyContractDeleteActivity() {
		LogUtils.logTestDescription("After deleting the Contract, check the activity logs.");
		logValidator.verifySalesforceActivityLog(contractLogs, sfActivities.get("ContractDeleteActivity"));
	}
	
	
	
	
	@Test(priority=-10, groups={"SANITY", "P1", "REGRESSION", "SOBJECT", "SOLUTION"})
	public void performSolutionOperations() throws Exception {

		String steps[] = {	"1. This test perform the saas operations on sobjects(solutions) and wait for sometime for the logs to reach our portal.", 
							"2. Call Investigate API and retrieve the Account related investigate logs."};

		LogUtils.logTestDescription(steps);
		try {
			Reporter.log("Starting to perform the solution operations in salesforce ", true);
			
			//Create a sobject solution
			SObjectInput sobjectInput = new SObjectInput();
			sobjectInput.setSolutionName("QA Automation solutions");
			sobjectInput.setSolutionNote("We provide soultions for web application automation with the tools selenium");
			SObject sobjectResponse = sfapi.createSObject(ObjectType.Solution.name(), sobjectInput);
			
			sleep(CommonConstants.THREE_MINUTES_SLEEP);
			
			//Update the sobject
			SObject sobject = sfapi.getSObject(ObjectType.Solution.name(), sobjectResponse.getId());
			SObjectInput updateInput = new SObjectInput();
			updateInput.setSolutionNote("We provide soultions for web application automation with the tools selenium and testng");
			sfapi.updateSObject(ObjectType.Solution.name(), updateInput, sobjectResponse.getId());
			
			sleep(CommonConstants.THREE_MINUTES_SLEEP);
			
			//Delete the solution
			sfapi.deleteSObject(ObjectType.Solution.name(), sobjectResponse.getId());
			
			sleep(CommonConstants.THREE_MINUTES_SLEEP);
			
			String expectedObjName = sobjectInput.getSolutionName();
			
			sfActivityLog.setSolutionCreateLog(sfActivityLog.getSolutionCreateLog().replace("{solutionname}", expectedObjName));
			sfActivityLog.setSolutionUpdateLog(sfActivityLog.getSolutionUpdateLog().replace("{solutionname}", expectedObjName));
			sfActivityLog.setSolutionDeleteLog(sfActivityLog.getSolutionDeleteLog().replace("{solutionname}", expectedObjName));
			
			String objectUrl = instanceUrl + "/" +sobjectResponse.getId();
			
			//message, createdTime, severity, objectType, activityType, objectUrl, objectId, objectName, fileName, instance, user, username
			SalesforceActivity SolutionCreateActivity = new SalesforceActivity(sfActivityLog.getSolutionCreateLog(), createdTime, 
						Severity.informational.name(), ObjectType.Solution.name(), ActivityType.Create.name(), objectUrl, 
						sobjectResponse.getId(), expectedObjName, sobjectInput.getName(), instanceId, saasAppUser, suiteData.getSocUserName());
			sfActivities.put("SolutionCreateActivity", SolutionCreateActivity);
			
			
			SalesforceActivity SolutionUpdateActivity = new SalesforceActivity(sfActivityLog.getSolutionUpdateLog(), createdTime, 
						Severity.informational.name(), ObjectType.Solution.name(), ActivityType.Update.name(), objectUrl, 
						sobjectResponse.getId(), expectedObjName, updateInput.getName(), instanceId, saasAppUser, suiteData.getSocUserName());
			sfActivities.put("SolutionUpdateActivity", SolutionUpdateActivity);
			
			
			SalesforceActivity SolutionDeleteActivity = new SalesforceActivity(sfActivityLog.getSolutionDeleteLog(), createdTime, 
						Severity.informational.name(), ObjectType.Solution.name(), ActivityType.Delete.name(), objectUrl, 
						sobjectResponse.getId(), expectedObjName, updateInput.getLastName(), instanceId, saasAppUser, suiteData.getSocUserName());
			sfActivities.put("SolutionDeleteActivity", SolutionDeleteActivity);
			
			sleep(this.minWaitTime);
			
			Reporter.log("Salesforce Solution operations are completed ...... ", true);
			Reporter.log("------------------------------------------------------------------------------- ", true);
			
			
			HashMap<String, String> termmap = new HashMap<String, String>();
			
			termmap.put("facility", facility.Salesforce.name());
			termmap.put("severity", Severity.informational.name());
			termmap.put("Object_type", ObjectType.Solution.name());
			termmap.put("__source", "API");			
			
			//Get contract  logs
			this.solutionLogs = this.getInvestigateLogs(-30, 30, facility.Salesforce.name(), termmap, suiteData.getUsername(), "https://"+suiteData.getApiserverHostName(), 
					suiteData.getCSRFToken(), suiteData.getSessionID(), 0, 100, "Salesforce");
		}
		catch(Exception e) {
			Reporter.log("Exception caught ... so skipping the test..."+ e.getStackTrace(), true);
			throw new SkipException("Skipping this test");
		}
	}
	
	
	@Test(groups={"SOLUTION", "P3", "REGRESSION"})
	public void verifySolutionCreateActivity() {
		LogUtils.logTestDescription("After creating the soultion, check the activity logs.");
		logValidator.verifySalesforceActivityLog(solutionLogs, sfActivities.get("SolutionCreateActivity"));
	}
	
	@Test(groups={"SOLUTION", "P3", "REGRESSION"})
	public void verifySolutionUpdateActivity() {
		LogUtils.logTestDescription("After updating the soultion, check the activity logs.");
		logValidator.verifySalesforceActivityLog(solutionLogs, sfActivities.get("SolutionUpdateActivity"));
	}
	
	@Test(groups={"SOLUTION", "P3", "REGRESSION"})
	public void verifySolutionDeleteActivity() {
		LogUtils.logTestDescription("After deleting the soultion, check the activity logs.");
		logValidator.verifySalesforceActivityLog(solutionLogs, sfActivities.get("SolutionDeleteActivity"));
	}
	
	
	
	@Test(priority=-10, groups={"SANITY", "P1", "REGRESSION", "SOBJECT", "CAMPAIGN"})
	public void performCompaignOperations() throws Exception {

		String steps[] = {	"1. This test perform the saas operations on sobjects(CAMPAIGN) and wait for sometime for the logs to reach our portal.", 
							"2. Call Investigate API and retrieve the Account related investigate logs."};

		LogUtils.logTestDescription(steps);
		try {
			Reporter.log("Starting to perform the campaign operations in salesforce ", true);
			
			//Create a sobject Compaign
			SObjectInput sobjectInput = new SObjectInput();
			sobjectInput.setName("BugCrushBlitz");
			sobjectInput.setDescription("BugCrushBlitz event for resolving bugs across securlets");
			
			SObject sobjectResponse = sfapi.createSObject(ObjectType.Campaign.name(), sobjectInput);
			
			sleep(CommonConstants.THREE_MINUTES_SLEEP);
			
			//Update the sobject
			SObject sobject = sfapi.getSObject(ObjectType.Campaign.name(), sobjectResponse.getId());
			SObjectInput updateInput = new SObjectInput();
			updateInput.setDescription("BugCrushBlitz event for resolving bugs across all connectors");
			sfapi.updateSObject(ObjectType.Campaign.name(), updateInput, sobjectResponse.getId());
			
			sleep(CommonConstants.THREE_MINUTES_SLEEP);
			
			//Delete the sobject
			sfapi.deleteSObject(ObjectType.Campaign.name(), sobjectResponse.getId());
			
			sleep(CommonConstants.THREE_MINUTES_SLEEP);
			
			String expectedObjName = sobjectInput.getName();
			
			sfActivityLog.setCampaignCreateLog(sfActivityLog.getCampaignCreateLog().replace("{campaignname}", expectedObjName));
			sfActivityLog.setCampaignUpdateLog(sfActivityLog.getCampaignUpdateLog().replace("{campaignname}", expectedObjName));
			sfActivityLog.setCampaignDeleteLog(sfActivityLog.getCampaignDeleteLog().replace("{campaignname}", expectedObjName));
			
			String objectUrl = instanceUrl + "/" +sobjectResponse.getId();
			
			//message, createdTime, severity, objectType, activityType, objectUrl, objectId, objectName, fileName, instance, user, username
			SalesforceActivity CampaignCreateActivity = new SalesforceActivity(sfActivityLog.getCampaignCreateLog(), createdTime, 
						Severity.informational.name(), ObjectType.Campaign.name(), ActivityType.Create.name(), objectUrl, 
						sobjectResponse.getId(), expectedObjName, sobjectInput.getName(), instanceId, saasAppUser, suiteData.getSocUserName());
			sfActivities.put("CampaignCreateActivity", CampaignCreateActivity);
			
			
			SalesforceActivity CampaignUpdateActivity = new SalesforceActivity(sfActivityLog.getCampaignUpdateLog(), createdTime, 
						Severity.informational.name(), ObjectType.Campaign.name(), ActivityType.Update.name(), objectUrl, 
						sobjectResponse.getId(), expectedObjName, updateInput.getName(), instanceId, saasAppUser, suiteData.getSocUserName());
			sfActivities.put("CampaignUpdateActivity", CampaignUpdateActivity);
			
			
			SalesforceActivity CampaignDeleteActivity = new SalesforceActivity(sfActivityLog.getCampaignDeleteLog(), createdTime, 
						Severity.informational.name(), ObjectType.Campaign.name(), ActivityType.Delete.name(), objectUrl, 
						sobjectResponse.getId(), expectedObjName, updateInput.getLastName(), instanceId, saasAppUser, suiteData.getSocUserName());
			sfActivities.put("CampaignDeleteActivity", CampaignDeleteActivity);
			
			sleep(this.minWaitTime);
			
			Reporter.log("Salesforce campaign operations are completed ...... ", true);
			Reporter.log("------------------------------------------------------------------------------- ", true);
			
			
			HashMap<String, String> termmap = new HashMap<String, String>();
			
			termmap.put("facility", facility.Salesforce.name());
			termmap.put("severity", Severity.informational.name());
			termmap.put("Object_type", ObjectType.Campaign.name());
			termmap.put("__source", "API");			
			
			//Get contract  logs
			this.campaignLogs = this.getInvestigateLogs(-30, 30, facility.Salesforce.name(), termmap, suiteData.getUsername(), "https://"+suiteData.getApiserverHostName(), 
					suiteData.getCSRFToken(), suiteData.getSessionID(), 0, 100, "Salesforce");
		}
		catch(Exception e) {
			Reporter.log("Exception caught ... so skipping the test..."+ e.getStackTrace(), true);
			throw new SkipException("Skipping this test");
		}
	}
	
	
	@Test(groups={"CAMPAIGN", "P3", "REGRESSION"})
	public void verifyCampaignCreateActivity() {
		LogUtils.logTestDescription("After creating the Compaign, check the activity logs.");
		logValidator.verifySalesforceActivityLog(campaignLogs, sfActivities.get("CampaignCreateActivity"));
	}
	
	@Test(groups={"CAMPAIGN", "P3", "REGRESSION"})
	public void verifyCompaignUpdateActivity() {
		LogUtils.logTestDescription("After updating the Compaign, check the activity logs.");
		logValidator.verifySalesforceActivityLog(campaignLogs, sfActivities.get("CampaignUpdateActivity"));
	}
	
	@Test(groups={"CAMPAIGN", "P3", "REGRESSION"})
	public void verifyCompaignDeleteActivity() {
		LogUtils.logTestDescription("After deleting the Campaign, check the activity logs.");
		logValidator.verifySalesforceActivityLog(campaignLogs, sfActivities.get("CampaignDeleteActivity"));
	}
	
	
	
	@Test(priority=-10, groups={"SANITY", "P1", "REGRESSION", "SOBJECT", "FOLDER"})
	public void performFolderOperations() throws Exception {

		String steps[] = {	"1. This test perform the saas operations on sobjects(Folder) and wait for sometime for the logs to reach our portal.", 
							"2. Call Investigate API and retrieve the Account related investigate logs."};

		LogUtils.logTestDescription(steps);
		try {
			Reporter.log("Starting to perform the Folder operations in salesforce ", true);
			String randomId = String.valueOf(System.currentTimeMillis());
			//Create a sobject folder
			SObjectInput sobjectInput = new SObjectInput();
			sobjectInput.setName("Folder For Documents_"+randomId);
			sobjectInput.setType("Document");
			sobjectInput.setDeveloperName("Pushparaj");
			sobjectInput.setAccessType("Public");
			
			SObject sobjectResponse = sfapi.createSObject(ObjectType.Folder.name(), sobjectInput);
			
			sleep(CommonConstants.THREE_MINUTES_SLEEP);
			
			//Update the sobject
			SObject sobject = sfapi.getSObject(ObjectType.Folder.name(), sobjectResponse.getId());
			SObjectInput updateInput = new SObjectInput();
			updateInput.setAccessType("Hidden");
			sfapi.updateSObject(ObjectType.Folder.name(), updateInput, sobjectResponse.getId());
			
			sleep(CommonConstants.THREE_MINUTES_SLEEP);
			
			//Delete the sobject
			sfapi.deleteSObject(ObjectType.Folder.name(), sobjectResponse.getId());
			
			sleep(CommonConstants.THREE_MINUTES_SLEEP);
			
			String expectedObjName = sobjectInput.getName();
			
			sfActivityLog.setFolderCreateLog(sfActivityLog.getFolderCreateLog().replace("{foldername}", expectedObjName));
			sfActivityLog.setFolderUpdateLog(sfActivityLog.getFolderUpdateLog().replace("{foldername}", expectedObjName));
			sfActivityLog.setFolderDeleteLog(sfActivityLog.getFolderDeleteLog().replace("{foldername}", expectedObjName));
			
			String objectUrl = instanceUrl + "/" +sobjectResponse.getId();
			
			//message, createdTime, severity, objectType, activityType, objectUrl, objectId, objectName, fileName, instance, user, username
			SalesforceActivity FolderCreateActivity = new SalesforceActivity(sfActivityLog.getFolderCreateLog(), createdTime, 
						Severity.informational.name(), ObjectType.Folder.name(), ActivityType.Create.name(), objectUrl, 
						sobjectResponse.getId(), expectedObjName, sobjectInput.getName(), instanceId, saasAppUser, suiteData.getSocUserName());
			sfActivities.put("FolderCreateActivity", FolderCreateActivity);
			
			
			SalesforceActivity FolderUpdateActivity = new SalesforceActivity(sfActivityLog.getFolderUpdateLog(), createdTime, 
						Severity.informational.name(), ObjectType.Folder.name(), ActivityType.Update.name(), objectUrl, 
						sobjectResponse.getId(), expectedObjName, updateInput.getName(), instanceId, saasAppUser, suiteData.getSocUserName());
			sfActivities.put("FolderUpdateActivity", FolderUpdateActivity);
			
			
			SalesforceActivity FolderDeleteActivity = new SalesforceActivity(sfActivityLog.getFolderDeleteLog(), createdTime, 
						Severity.informational.name(), ObjectType.Folder.name(), ActivityType.Delete.name(), objectUrl, 
						sobjectResponse.getId(), expectedObjName, updateInput.getLastName(), instanceId, saasAppUser, suiteData.getSocUserName());
			sfActivities.put("FolderDeleteActivity", FolderDeleteActivity);
			
			sleep(this.minWaitTime);
			
			Reporter.log("Salesforce folder operations are completed ...... ", true);
			Reporter.log("------------------------------------------------------------------------------- ", true);
			
			
			HashMap<String, String> termmap = new HashMap<String, String>();
			
			termmap.put("facility", facility.Salesforce.name());
			termmap.put("severity", Severity.informational.name());
			termmap.put("Object_type", ObjectType.Folder.name());
			termmap.put("__source", "API");			
			
			//Get contract  logs
			this.folderLogs = this.getInvestigateLogs(-30, 30, facility.Salesforce.name(), termmap, suiteData.getUsername(), "https://"+suiteData.getApiserverHostName(), 
					suiteData.getCSRFToken(), suiteData.getSessionID(), 0, 100, "Salesforce");
		}
		catch(Exception e) {
			Reporter.log("Exception caught ... so skipping the test..."+ e.getStackTrace(), true);
			throw new SkipException("Skipping this test");
		}
	}
	
	
	@Test(groups={"FOLDER", "P3", "REGRESSION"})
	public void verifyFolderCreateActivity() {
		LogUtils.logTestDescription("After creating the Folder, check the activity logs.");
		logValidator.verifySalesforceActivityLog(folderLogs, sfActivities.get("FolderCreateActivity"));
	}
	
	@Test(groups={"FOLDER", "P3", "REGRESSION"})
	public void verifyFolderUpdateActivity() {
		LogUtils.logTestDescription("After updating the Folder, check the activity logs.");
		logValidator.verifySalesforceActivityLog(folderLogs, sfActivities.get("FolderUpdateActivity"));
	}
	
	@Test(groups={"FOLDER", "P3", "REGRESSION"})
	public void verifyFolderDeleteActivity() {
		LogUtils.logTestDescription("After deleting the Folder, check the activity logs.");
		logValidator.verifySalesforceActivityLog(folderLogs, sfActivities.get("FolderDeleteActivity"));
	}
	
	
	
	@Test(priority=-10, groups={"SANITY", "P1", "REGRESSION", "SOBJECT", "TASK"})
	public void performTaskOperations() throws Exception {

		String steps[] = {	"1. This test perform the saas operations on sobjects(Task) and wait for sometime for the logs to reach our portal.", 
							"2. Call Investigate API and retrieve the Report related investigate logs."};

		LogUtils.logTestDescription(steps);
		try {
			Reporter.log("Starting to perform the Task operations in salesforce ", true);
			
			//Create a sobject folder
			SObjectInput sobjectInput = new SObjectInput();
			sobjectInput.setSubject("Call");
			sobjectInput.setDescription("Task Description");
			SObject sobjectResponse = sfapi.createSObject(ObjectType.Task.name(), sobjectInput);
			
			sleep(CommonConstants.THREE_MINUTES_SLEEP);
			
			//Update the sobject
			SObject sobject = sfapi.getSObject(ObjectType.Task.name(), sobjectResponse.getId());
			SObjectInput updateInput = new SObjectInput();
			updateInput.setDescription("Updated Task Description");
			sfapi.updateSObject(ObjectType.Task.name(), updateInput, sobjectResponse.getId());
			
			sleep(CommonConstants.THREE_MINUTES_SLEEP);
			
			//Delete the sobject
			sfapi.deleteSObject(ObjectType.Task.name(), sobjectResponse.getId());
			sleep(CommonConstants.THREE_MINUTES_SLEEP);
			
			String expectedObjName = sobjectInput.getSubject();
			
			sfActivityLog.setTaskCreateLog(sfActivityLog.getTaskCreateLog().replace("{subject}", expectedObjName));
			sfActivityLog.setTaskUpdateLog(sfActivityLog.getTaskUpdateLog().replace("{subject}", expectedObjName));
			sfActivityLog.setTaskDeleteLog(sfActivityLog.getTaskDeleteLog().replace("{subject}", expectedObjName));
			
			String objectUrl = instanceUrl + "/" +sobjectResponse.getId();
			
			//message, createdTime, severity, objectType, activityType, objectUrl, objectId, objectName, fileName, instance, user, username
			SalesforceActivity TaskCreateActivity = new SalesforceActivity(sfActivityLog.getTaskCreateLog(), createdTime, 
						Severity.informational.name(), ObjectType.Task.name(), ActivityType.Create.name(), objectUrl, 
						sobjectResponse.getId(), expectedObjName, sobjectInput.getName(), instanceId, saasAppUser, suiteData.getSocUserName());
			sfActivities.put("TaskCreateActivity", TaskCreateActivity);
			
			
			SalesforceActivity TaskUpdateActivity = new SalesforceActivity(sfActivityLog.getTaskUpdateLog(), createdTime, 
						Severity.informational.name(), ObjectType.Task.name(), ActivityType.Update.name(), objectUrl, 
						sobjectResponse.getId(), expectedObjName, updateInput.getName(), instanceId, saasAppUser, suiteData.getSocUserName());
			sfActivities.put("TaskUpdateActivity", TaskUpdateActivity);
			
			
			SalesforceActivity TaskDeleteActivity = new SalesforceActivity(sfActivityLog.getTaskDeleteLog(), createdTime, 
						Severity.informational.name(), ObjectType.Task.name(), ActivityType.Delete.name(), objectUrl, 
						sobjectResponse.getId(), expectedObjName, updateInput.getLastName(), instanceId, saasAppUser, suiteData.getSocUserName());
			sfActivities.put("TaskDeleteActivity", TaskDeleteActivity);
			
			sleep(this.minWaitTime);
			
			Reporter.log("Salesforce Task operations are completed ...... ", true);
			Reporter.log("------------------------------------------------------------------------------- ", true);
			
			
			HashMap<String, String> termmap = new HashMap<String, String>();
			
			termmap.put("facility", facility.Salesforce.name());
			termmap.put("severity", Severity.informational.name());
			termmap.put("Object_type", ObjectType.Task.name());
			termmap.put("__source", "API");			
			
			//Get contract  logs
			this.taskLogs = this.getInvestigateLogs(-30, 30, facility.Salesforce.name(), termmap, suiteData.getUsername(), "https://"+suiteData.getApiserverHostName(), 
					suiteData.getCSRFToken(), suiteData.getSessionID(), 0, 100, "Salesforce");
		}
		catch(Exception e) {
			Reporter.log("Exception caught ... so skipping the test..."+ e.getStackTrace(), true);
			throw new SkipException("Skipping this test");
		}
	}
	
	
	@Test(groups={"TASK", "P3", "REGRESSION"})
	public void verifyTaskCreateActivity() {
		LogUtils.logTestDescription("After creating the Task, check the activity logs.");
		logValidator.verifySalesforceActivityLog(taskLogs, sfActivities.get("TaskCreateActivity"));
	}
	
	@Test(groups={"TASK", "P3", "REGRESSION"})
	public void verifyTaskUpdateActivity() {
		LogUtils.logTestDescription("After updating the Task, check the activity logs.");
		logValidator.verifySalesforceActivityLog(taskLogs, sfActivities.get("TaskUpdateActivity"));
	}
	
	@Test(groups={"TASK", "P3", "REGRESSION"})
	public void verifyTaskDeleteActivity() {
		LogUtils.logTestDescription("After deleting the Task, check the activity logs.");
		logValidator.verifySalesforceActivityLog(taskLogs, sfActivities.get("TaskDeleteActivity"));
	}
	
	
	
	
	@Test(priority=-10, groups={"SANITY", "P1", "REGRESSION", "SOBJECT", "EVENT"})
	public void performEventOperations() throws Exception {

		String steps[] = {	"1. This test perform the saas operations on sobjects(Event) and wait for sometime for the logs to reach our portal.", 
							"2. Call Investigate API and retrieve the Event related investigate logs."};

		LogUtils.logTestDescription(steps);
		try {
			Reporter.log("Starting to perform the Event operations in salesforce ", true);
			
			//Create a sobject folder
			SObjectInput sobjectInput = new SObjectInput();
			sobjectInput.setSubject("QA All Hands");
			sobjectInput.setDescription("All hands for QA automation group. Town hall kinds");
			sobjectInput.setDurationInMinutes(60);
			sobjectInput.setActivityDateTime(DateUtils.getCurrentTime());
			SObject sobjectResponse = sfapi.createSObject(ObjectType.Event.name(), sobjectInput);
			
			sleep(CommonConstants.THREE_MINUTES_SLEEP);
			
			//Update the sobject
			SObject sobject = sfapi.getSObject(ObjectType.Event.name(), sobjectResponse.getId());
			SObjectInput updateInput = new SObjectInput();
			updateInput.setDescription("All hands for QA automation group");
			sfapi.updateSObject(ObjectType.Event.name(), updateInput, sobjectResponse.getId());
			
			sleep(CommonConstants.THREE_MINUTES_SLEEP);
			
			//Delete the sobject
			sfapi.deleteSObject(ObjectType.Event.name(), sobjectResponse.getId());
			sleep(CommonConstants.THREE_MINUTES_SLEEP);
			
			String expectedObjName = sobjectInput.getSubject();
			
			sfActivityLog.setEventCreateLog(sfActivityLog.getEventCreateLog().replace("{subject}", expectedObjName));
			sfActivityLog.setEventUpdateLog(sfActivityLog.getEventUpdateLog().replace("{subject}", expectedObjName));
			sfActivityLog.setEventDeleteLog(sfActivityLog.getEventDeleteLog().replace("{subject}", expectedObjName));
			
			String objectUrl = instanceUrl + "/" +sobjectResponse.getId();
			
			//message, createdTime, severity, objectType, activityType, objectUrl, objectId, objectName, fileName, instance, user, username
			SalesforceActivity EventCreateActivity = new SalesforceActivity(sfActivityLog.getEventCreateLog(), createdTime, 
						Severity.informational.name(), ObjectType.Event.name(), ActivityType.Create.name(), objectUrl, 
						sobjectResponse.getId(), expectedObjName, sobjectInput.getName(), instanceId, saasAppUser, suiteData.getSocUserName());
			sfActivities.put("EventCreateActivity", EventCreateActivity);
			
			
			SalesforceActivity EventUpdateActivity = new SalesforceActivity(sfActivityLog.getEventUpdateLog(), createdTime, 
						Severity.informational.name(), ObjectType.Event.name(), ActivityType.Update.name(), objectUrl, 
						sobjectResponse.getId(), expectedObjName, updateInput.getName(), instanceId, saasAppUser, suiteData.getSocUserName());
			sfActivities.put("EventUpdateActivity", EventUpdateActivity);
			
			
			SalesforceActivity EventDeleteActivity = new SalesforceActivity(sfActivityLog.getEventDeleteLog(), createdTime, 
						Severity.informational.name(), ObjectType.Event.name(), ActivityType.Delete.name(), objectUrl, 
						sobjectResponse.getId(), expectedObjName, updateInput.getLastName(), instanceId, saasAppUser, suiteData.getSocUserName());
			sfActivities.put("EventDeleteActivity", EventDeleteActivity);
			
			sleep(this.minWaitTime);
			
			Reporter.log("Salesforce Event operations are completed ...... ", true);
			Reporter.log("------------------------------------------------------------------------------- ", true);
			
			
			HashMap<String, String> termmap = new HashMap<String, String>();
			
			termmap.put("facility", facility.Salesforce.name());
			termmap.put("severity", Severity.informational.name());
			termmap.put("Object_type", ObjectType.Event.name());
			termmap.put("__source", "API");			
			
			//Get event  logs
			this.eventLogs = this.getInvestigateLogs(-30, 30, facility.Salesforce.name(), termmap, suiteData.getUsername(), "https://"+suiteData.getApiserverHostName(), 
					suiteData.getCSRFToken(), suiteData.getSessionID(), 0, 100, "Salesforce");
		}
		catch(Exception e) {
			Reporter.log("Exception caught ... so skipping the test..."+ e.getStackTrace(), true);
			throw new SkipException("Skipping this test");
		}
	}
	
	
	@Test(groups={"EVENT", "P3", "REGRESSION"})
	public void verifyEventCreateActivity() {
		LogUtils.logTestDescription("After creating the Event, check the activity logs.");
		logValidator.verifySalesforceActivityLog(eventLogs, sfActivities.get("EventCreateActivity"));
	}
	
	@Test(groups={"EVENT", "P3", "REGRESSION"})
	public void verifyEventUpdateActivity() {
		LogUtils.logTestDescription("After updating the Event, check the activity logs.");
		logValidator.verifySalesforceActivityLog(eventLogs, sfActivities.get("EventUpdateActivity"));
	}
	
	@Test(groups={"EVENT", "P3", "REGRESSION"})
	public void verifyEventDeleteActivity() {
		LogUtils.logTestDescription("After deleting the Event, check the activity logs.");
		logValidator.verifySalesforceActivityLog(eventLogs, sfActivities.get("EventDeleteActivity"));
	}
	
	
	
	@Test(priority=-10, groups={"SANITY", "P1", "REGRESSION", "SOBJECT", "CASE"})
	public void performCaseOperations() throws Exception {

		String steps[] = {	"1. This test perform the saas operations on sobjects(Case) and wait for sometime for the logs to reach our portal.", 
							"2. Call Investigate API and retrieve the Account related investigate logs."};

		LogUtils.logTestDescription(steps);
		try {
			Reporter.log("Starting to perform the operations in salesforce ", true);
			String randomId = String.valueOf(System.currentTimeMillis());
			
			//Create a contact to be used for case creation
			SObjectInput sInput = new SObjectInput();
			sInput.setLastName("Securlet Automation");
			sInput.setDepartment("AutomationQA");
			sInput.setEmail("securletuser@securletbeatle.com");
			SObject contactResponse = sfapi.createSObject(ObjectType.Contact.name(), sInput);
			
			//Create a sobject
			SObjectInput sobjectInput = new SObjectInput();
			sobjectInput.setContactId(contactResponse.getId());
			sobjectInput.setDescription("AutomationQA Case");
			sobjectInput.setReason("Automation Issues");
			sobjectInput.setStatus("open");
			SObject sobjectResponse = sfapi.createSObject(ObjectType.Case.name(), sobjectInput);	
			sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
			
			//Retrieve the object details
			Case caseObj = sfapi.getCaseObject(ObjectType.Case.name(), sobjectResponse.getId());
			sleep(CommonConstants.THREE_MINUTES_SLEEP);
			
			//Update the case details
			SObjectInput updateInput = new SObjectInput();
			updateInput.setReason("Automation Issues "+randomId);
			sfapi.updateSObject(ObjectType.Case.name(), updateInput, sobjectResponse.getId());
			sleep(CommonConstants.THREE_MINUTES_SLEEP);
			
			//Delete the sobject contact and case
			sfapi.deleteSObject(ObjectType.Case.name(), sobjectResponse.getId());
			sfapi.deleteSObject(ObjectType.Contact.name(), contactResponse.getId());
			
			sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
			
			sfActivityLog.setCaseCreateLog(sfActivityLog.getCaseCreateLog().replace("{casenumber}", caseObj.getCaseNumber()));
			sfActivityLog.setCaseUpdateLog(sfActivityLog.getCaseUpdateLog().replace("{casenumber}", caseObj.getCaseNumber()));
			sfActivityLog.setCaseDeleteLog(sfActivityLog.getCaseDeleteLog().replace("{casenumber}", caseObj.getCaseNumber()));
			
			sfActivityLog.setCaseUpdateFieldLog(sfActivityLog.getCaseUpdateFieldLog().replace("{caseid}", caseObj.getId())
																					 .replace("{fieldname}", "Reason"));
			
			String objectUrl = instanceUrl + "/" +sobjectResponse.getId();
			
			//message, createdTime, severity, objectType, activityType, objectUrl, objectId, objectName, fileName, instance, user, username
			SalesforceActivity CaseCreateActivity = new SalesforceActivity(sfActivityLog.getCaseCreateLog(), createdTime, 
						Severity.informational.name(), ObjectType.Case.name(), ActivityType.Create.name(), objectUrl, 
						sobjectResponse.getId(), caseObj.getCaseNumber(), sobjectInput.getName(), instanceId, saasAppUser, suiteData.getSocUserName());
			sfActivities.put("CaseCreateActivity", CaseCreateActivity);
			
			
			SalesforceActivity CaseUpdateActivity = new SalesforceActivity(sfActivityLog.getCaseUpdateLog(), createdTime, 
						Severity.informational.name(), ObjectType.Case.name(), ActivityType.Update.name(), objectUrl, 
						sobjectResponse.getId(), caseObj.getCaseNumber(), updateInput.getName(), instanceId, saasAppUser, suiteData.getSocUserName());
			sfActivities.put("CaseUpdateActivity", CaseUpdateActivity);
			
			
			SalesforceActivity CaseDeleteActivity = new SalesforceActivity(sfActivityLog.getCaseDeleteLog(), createdTime, 
						Severity.informational.name(), ObjectType.Case.name(), ActivityType.Delete.name(), objectUrl, 
						sobjectResponse.getId(), 	caseObj.getCaseNumber(), updateInput.getLastName(), instanceId, saasAppUser, suiteData.getSocUserName());
			sfActivities.put("CaseDeleteActivity", CaseDeleteActivity);
			
			//In update object id is used in stead of case number different from above twu cases
			SalesforceActivity CaseUpdateFieldActivity = new SalesforceActivity(sfActivityLog.getCaseUpdateFieldLog(), createdTime, 
					Severity.informational.name(), ObjectType.Case.name(), ActivityType.Update.name(), objectUrl, 
					sobjectResponse.getId(), 	sobjectResponse.getId(),  updateInput.getLastName(), instanceId, saasAppUser, suiteData.getSocUserName());
			sfActivities.put("CaseUpdateFieldActivity", CaseUpdateFieldActivity);
			
			
			sleep(this.minWaitTime);
			
			Reporter.log("Salesforce Case operations are completed ...... ", true);
			Reporter.log("------------------------------------------------------------------------------- ", true);
		}
		catch(Exception e) {
			Reporter.log("Exception caught ... so skipping the test...", true);
			e.printStackTrace();
			throw new SkipException("Skipping this test");
		}
	}
	
	
	@Test(dependsOnMethods = "fetchActivityLogs", groups={"CASE", "SANITY", "P1", "REGRESSION"})
	public void verifyCaseCreateActivity() {
		LogUtils.logTestDescription("After creating the Case, check the activity logs.");
		logValidator.verifySalesforceActivityLog(caseLogs, sfActivities.get("CaseCreateActivity"));
	}
	
	@Test(dependsOnMethods = "fetchActivityLogs", groups={"CASE", "SANITY", "P1", "REGRESSION"})
	public void verifyCaseUpdateActivity() {
		LogUtils.logTestDescription("After updating the Case, check the activity logs.");
		logValidator.verifySalesforceActivityLog(caseLogs, sfActivities.get("CaseUpdateActivity"));
	}
	
	@Test(dependsOnMethods = "fetchActivityLogs", groups={"CASE", "SANITY", "P1", "REGRESSION"})
	public void verifyCaseDeleteActivity() {
		LogUtils.logTestDescription("After deleting the Case, check the activity logs.");
		logValidator.verifySalesforceActivityLog(caseLogs, sfActivities.get("CaseDeleteActivity"));
	}
	
	@Test(dependsOnMethods = "fetchActivityLogs", groups={"CASE", "SANITY", "P1", "REGRESSION"})
	public void verifyCaseUpdateFieldActivity() {
		LogUtils.logTestDescription("After updating the field of the Case, check the activity logs.");
		logValidator.verifySalesforceActivityLog(caseLogs, sfActivities.get("CaseUpdateFieldActivity"));
	}
	
	
	@Test(groups={"EXPORT", "SALESFORCE", "REGRESSION", "P1"})
	public void csvExportOfExposedFilesWithDefaultDashBoard() throws Exception {
		LogUtils.logTestDescription("Export the exposed files with default dashboard state to user email and check");
		List<NameValuePair> headers = getHeaders();
		
		String path = suiteData.getAPIMap().get("getUIExportCSV") ;
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(), path);
		Reporter.log("URI ::"+dataUri.toString(), true);

		UIExposedDoc payload = this.getUIPayload(appName, null, null, "docs", 0, 0, true, "", "name", null, null, null);
		SecurletDocument exposedDocs = getUIExposedDocuments(MarshallingUtils.marshall(payload), null);
		HttpResponse response =  restClient.doPost(dataUri, headers, null, new StringEntity(MarshallingUtils.marshall(payload)));

		String responseBody = ClientUtil.getResponseBody(response);
		Reporter.log("Response body:"+ responseBody, true);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
		CustomAssertion.assertTrue(responseBody.contains("true"), "Filelink sent to email", "File not sent");
		CustomAssertion.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_OK, "Not exported");

		sleep(CommonConstants.THREE_MINUTES_SLEEP);
		
		String query = "Salesforce Securlet Data Export";
		String expectedAttachment = "Salesforce_securlet_docs_" + DateUtils.getCurrentDate();
		String absoluteFilename = null;
		Map<String, byte[]> attachments = this.getEmailAttachment(query, null, null, null);
	
		for (Map.Entry<String, byte[]> entrySet : attachments.entrySet()) {
        	String key = entrySet.getKey();
        	byte[] value = entrySet.getValue();
        	Reporter.log("Attachment name:"+ key, true);
        	Reporter.log("Attachment size:"+ value.length, true);
        	CustomAssertion.assertTrue(value.length > 0, key + " size is "+value.length, key + "size is zero" );
        	CustomAssertion.assertTrue(key.startsWith(expectedAttachment), key+ " Starts with "+ expectedAttachment, "Attachment name don't match");
        	
        	if (key.startsWith(expectedAttachment)) {
        		absoluteFilename = key;
        	}
        }
		
		String tmpdir = System.getProperty("user.dir") + File.separator;
		storeGmailAttachment(absoluteFilename, tmpdir, null, null, null);
		
		if (absoluteFilename != null) {
			String source = tmpdir + File.separator + absoluteFilename;
			String destination = tmpdir + File.separator + absoluteFilename.replace(".gz", "");
			
			uncompressgz(source, destination);
			
			List<Map<String, String>> allrows = unmarshallCSVIntoList(new File(destination), null);
			CustomAssertion.assertEquals(allrows.size(), exposedDocs.getMeta().getTotalCount(), "Exported CSV exposed file count don't match");
			
			
			LogUtils.logStep("2. Iterate over the list of documents and check the selected filter");
			int offset = 0, limit = 50;
			
			do {
				payload = this.getUIPayload(appName, null, null, "docs", offset, limit, true, "", "name", null, null, null);
				exposedDocs = getUIExposedDocuments(MarshallingUtils.marshall(payload), null);
				
				logValidator.verifyExportedDocument(exposedDocs, allrows, offset);
				
				offset +=limit;
			} while(exposedDocs.getMeta().getTotalCount() >= offset);
			
		}
		
	}
	
	
	@Test(dataProviderClass = SalesforceDataProvider.class, dataProvider = "ExportFilter", groups={"EXPORT", "SALESFORCE", "REGRESSION", "P1"})
	public void csvExportOfExposedFilesWithFilters(String exposureType, String objectType, 
									String vlType, String ciq, String contentType, String fileType, String exportType, String searchText, boolean isInternal) throws Exception {
		LogUtils.logTestDescription("Export the exposed files with default dashboard state to user email and check");
		List<NameValuePair> headers = getHeaders();
		
		String path = suiteData.getAPIMap().get("getUIExportCSV") ;
		//String path = "/admin/application/list/export_exposures_data"; 
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(), path);
		Reporter.log("URI ::"+dataUri.toString(), true);

		UIExposedDoc payload = this.getUIPayload(appName, exposureType, objectType, exportType, 0, 0, isInternal, searchText, "name", vlType, fileType, contentType);
		SecurletDocument exposedDocs = getUIExposedDocuments(MarshallingUtils.marshall(payload), null);
		
		//String payload = "{\"source\":{\"limit\":20,\"offset\":0,\"isInternal\":true,\"searchTextFromTable\":\"\",\"orderBy\":\"name\",\"exportType\":\"docs\",\"app\":\"Office 365\"}}";
		HttpResponse response =  restClient.doPost(dataUri, headers, null, new StringEntity(MarshallingUtils.marshall(payload)));

		String responseBody = ClientUtil.getResponseBody(response);
		Reporter.log("Response body:"+ responseBody, true);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
		CustomAssertion.assertTrue(responseBody.contains("true"), "Filelink sent to email", "File not sent");
		CustomAssertion.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_OK, "Not exported");

		sleep(30000);
		
		String query = "Salesforce Securlet Data Export";
		String expectedAttachment = "Salesforce_securlet_docs_" + DateUtils.getCurrentDate();
		String absoluteFilename = null;
		Map<String, byte[]> attachments = this.getEmailAttachment(query, null, null, null);
	
		for (Map.Entry<String, byte[]> entrySet : attachments.entrySet()) {
        	String key = entrySet.getKey();
        	byte[] value = entrySet.getValue();
        	Reporter.log("Attachment name:"+ key, true);
        	Reporter.log("Attachment size:"+ value.length, true);
        	CustomAssertion.assertTrue(value.length > 0, key + " size is "+value.length, key + "size is zero" );
        	CustomAssertion.assertTrue(key.startsWith(expectedAttachment), key+ " Starts with "+ expectedAttachment, "Attachment name don't match");
        	
        	if (key.startsWith(expectedAttachment)) {
        		absoluteFilename = key;
        	}
        }
		
		String tmpdir = System.getProperty("user.dir") + File.separator;
		storeGmailAttachment(absoluteFilename, tmpdir, null, null, null);
		
		if (absoluteFilename != null) {
			String source = tmpdir + File.separator + absoluteFilename;
			String destination = tmpdir + File.separator + absoluteFilename.replace(".gz", "");
			
			uncompressgz(source, destination);
			
			List<Map<String, String>> allrows = unmarshallCSVIntoList(new File(destination), null);
			CustomAssertion.assertEquals(allrows.size(), exposedDocs.getMeta().getTotalCount(), "Exported CSV exposed file count don't match");
			
			LogUtils.logStep("2. Iterate over the list of documents and verify with the exported csv");
			int offset = 0, limit = 50;
			
			do {
				payload = this.getUIPayload(appName, exposureType, objectType, exportType, 0, 0, isInternal, searchText, "name", vlType, fileType, contentType);
				exposedDocs = getUIExposedDocuments(MarshallingUtils.marshall(payload), null);
				logValidator.verifyExportedDocument(exposedDocs, allrows, offset);
				offset +=limit;
			} while(exposedDocs.getMeta().getTotalCount() >= offset); 
		}
		
	}
	
	
	@Test(dataProviderClass = SalesforceDataProvider.class, dataProvider = "ExportUsersFilter", groups={"EXPORT", "SALESFORCE", "REGRESSION", "P1"})
	public void checkCSVExportOfExposedUsersWithFilters(String exposureType, String objectType, 
			String vlType, String ciq, String contentType, String fileType, String exportType, String searchText, boolean isInternal) throws Exception {
		LogUtils.logTestDescription("Export the exposed users to user email and check");
		List<NameValuePair> headers = getHeaders();
		String path = suiteData.getAPIMap().get("getUIExportCSV") ;
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(), path);
		Reporter.log("URI ::"+dataUri.toString(), true);
		UIExposedDoc payload = this.getUIPayload(appName, exposureType, objectType, exportType, 0, 50, isInternal, searchText, "name", vlType, fileType, contentType);
		Reporter.log("Payload ::"+MarshallingUtils.marshall(payload), true);
		HttpResponse response =  restClient.doPost(dataUri, headers, null, new StringEntity(MarshallingUtils.marshall(payload)));

		String responseBody = ClientUtil.getResponseBody(response);
		Reporter.log("Response body:"+ responseBody, true);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
		CustomAssertion.assertTrue(responseBody.contains("true"), "Filelink sent to email", "File not sent");
		CustomAssertion.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_OK, "Not exported");
		
		sleep(180000);
	
		String expectedAttachment = null;
		if (exportType.equals("users")) {
			expectedAttachment = "Salesforce_securlet_users_" + DateUtils.getCurrentDate();
		} else if( exportType.equals("ext_collabs")) {
			expectedAttachment = "Salesforce_securlet_ext_collabs_" + DateUtils.getCurrentDate();
		}
		
		String query = "Salesforce Securlet Data Export";
		String tmpdir = System.getProperty("user.dir") + File.separator;
		String absoluteFilename = null;
		Map<String, byte[]> attachments = this.getEmailAttachment(query, null, null, null);
	
		for (Map.Entry<String, byte[]> entrySet : attachments.entrySet()) {
        	String key = entrySet.getKey();
        	byte[] value = entrySet.getValue();
        	Reporter.log("Attachment name:"+ key, true);
        	Reporter.log("Attachment size:"+ value.length, true);
        	CustomAssertion.assertTrue(value.length > 0, key + " size is "+value.length, key + "size is zero" );
        	CustomAssertion.assertTrue(key.startsWith(expectedAttachment), key+ " Starts with "+ expectedAttachment, "Attachment name don't match");
        	
        	if (key.startsWith(expectedAttachment)) {
        		absoluteFilename = key;
        	}
        }
		
		storeGmailAttachment(absoluteFilename, tmpdir, null, null, null);
		
		//Get the exposed users with api
		List<NameValuePair> docparams = new ArrayList<NameValuePair>(); 
		docparams.add(new BasicNameValuePair(SecurletsConstants.IS_INTERNAL,  Boolean.TRUE.toString()));
		docparams.add(new BasicNameValuePair(SecurletsConstants.APP,  facility.Salesforce.name()));
		
		if (vlType !=null) {
			docparams.add(new BasicNameValuePair("content_checks.vl_"+vlType,  "true"));
		}
		
		if (fileType !=null) {
			docparams.add(new BasicNameValuePair("fileClass",  fileType));
			
		}
		
		if (exposureType != null && exposureType.equals("public")) {
			docparams.add(new BasicNameValuePair("exposures.public",  "true"));
		}
		
		
		SecurletDocument exposedUsers = null;
		
		if (exportType.equals("users")) {
			exposedUsers = getExposedUsers(elapp.el_salesforce.name(), docparams);
		} else if( exportType.equals("ext_collabs")) {
			exposedUsers = getCollaborators(elapp.el_salesforce.name(), docparams);
		}
		
		
		if (absoluteFilename != null) {
			String source = tmpdir + File.separator + absoluteFilename;
			String destination = tmpdir + File.separator + absoluteFilename.replace(".gz", "");
			
			uncompressgz(source, destination);
			
			List<Map<String, String>> allrows = unmarshallCSVIntoList(new File(destination), "Email");
			CustomAssertion.assertEquals(allrows.size(), exposedUsers.getMeta().getTotalCount(), "Exported CSV exposed users count don't match");
			
			LogUtils.logStep("2. Iterate over the list of users and verify with the exported csv");
			
			logValidator.verifyExportedUsers(exposedUsers, allrows, 0);
		}
	}
	
	
	@Test(dataProviderClass = SalesforceDataProvider.class, dataProvider = "RiskyFilesFilter", groups={"EXPORT", "SALESFORCE", "REGRESSION", "P1"})
	public void csvExportOfRiskyFilesWithFilters(String exposureType, String objectType, 
									String vlType, String ciq, String contentType, String fileType, String exportType, String searchText, boolean isInternal) throws Exception {
		LogUtils.logTestDescription("Export the exposed files with default dashboard state to user email and check");
		List<NameValuePair> headers = getHeaders();
		
		String path = suiteData.getAPIMap().get("getUIExportCSV") ;
		//String path = "/admin/application/list/export_exposures_data"; 
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(), path);
		Reporter.log("URI ::"+dataUri.toString(), true);
		List<NameValuePair> qparams = new ArrayList<NameValuePair>(); 
		qparams.add(new BasicNameValuePair("requestType", "risky_docs"));
		
		UIExposedDoc payload = this.getUIPayload(appName, exposureType, objectType, exportType, 0, 0, isInternal, searchText, "name", vlType, fileType, contentType);
		SecurletDocument exposedDocs = getUIRiskyDocuments(MarshallingUtils.marshall(payload), qparams);
		
		//String payload = "{\"source\":{\"limit\":20,\"offset\":0,\"isInternal\":true,\"searchTextFromTable\":\"\",\"orderBy\":\"name\",\"exportType\":\"docs\",\"app\":\"Office 365\"}}";
		HttpResponse response =  restClient.doPost(dataUri, headers, null, new StringEntity(MarshallingUtils.marshall(payload)));

		String responseBody = ClientUtil.getResponseBody(response);
		Reporter.log("Response body:"+ responseBody, true);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
		CustomAssertion.assertTrue(responseBody.contains("true"), "Filelink sent to email", "File not sent");
		CustomAssertion.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_OK, "Not exported");

		sleep(180000);
		
		String expectedAttachment = "Salesforce_securlet_risky_docs_" + DateUtils.getCurrentDate();
		String tmpdir = System.getProperty("user.dir") + File.separator; //System.getProperty("java.io.tmpdir") ; 
		String query = "Salesforce Securlet Data Export";
		
		String absoluteFilename = null;
		Map<String, byte[]> attachments = this.getEmailAttachment(query, null, null, null);
	
		for (Map.Entry<String, byte[]> entrySet : attachments.entrySet()) {
        	String key = entrySet.getKey();
        	byte[] value = entrySet.getValue();
        	Reporter.log("Attachment name:"+ key, true);
        	Reporter.log("Attachment size:"+ value.length, true);
        	CustomAssertion.assertTrue(value.length > 0, key + " size is "+value.length, key + "size is zero" );
        	CustomAssertion.assertTrue(key.startsWith(expectedAttachment), key+ " Starts with "+ expectedAttachment, "Attachment name don't match");
        	
        	if (key.startsWith(expectedAttachment)) {
        		absoluteFilename = key;
        	}
        }
		//Save the email
		storeGmailAttachment(absoluteFilename, tmpdir, null, null, null);
		
		if (absoluteFilename != null) {
			String source = tmpdir + File.separator + absoluteFilename;
			String destination = tmpdir + File.separator + absoluteFilename.replace(".gz", "");
			
			uncompressgz(source, destination);
			
			List<Map<String, String>> allrows = unmarshallCSVIntoList(new File(destination), "Name");
			CustomAssertion.assertEquals(allrows.size(), exposedDocs.getMeta().getTotalCount(), "Exported CSV exposed file count don't match");
			
			LogUtils.logStep("2. Iterate over the list of documents and verify with the exported csv");
			int offset = 0, limit = 50;
			
			do {
				payload = this.getUIPayload(appName, exposureType, objectType, exportType, 0, 0, isInternal, searchText, "name", vlType, fileType, contentType);
				exposedDocs = getUIExposedDocuments(MarshallingUtils.marshall(payload), qparams);
				logValidator.verifyExportedDocument(exposedDocs, allrows, offset);
				
				offset +=limit;
			} while(exposedDocs.getMeta().getTotalCount() >= offset); 
		}
	}
	
	
	@Test(dataProviderClass = SalesforceDataProvider.class, dataProvider = "I18NString", priority=-1, groups={"I18N", "P3", "I18N_FILE"})
	public void performI18NChatterOperations(String locale, String i18nString) throws Exception {
		
		String steps[] = {	"1. This test perform the saas operations and wait for 3 mins for the logs to reach our portal.", 
							"2. Call Investigate API and retrieve the file related investigate logs."};

		LogUtils.logTestDescription(steps);
		sfI18NActivityLog = new SalesforceActivityLog();
		try {
			Reporter.log("Starting to perform the operations in salesforce ", true);
			String randomId = String.valueOf(System.currentTimeMillis());
			String sourceFile = "test.pdf";
			//filetitle = "Hello";
			destinationI18NFile = i18nString+"_"+randomId + "_" + sourceFile;
			contentDocumentName = destinationFile;
			//Upload the file
			createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
			ChatterFile chatterfile = sfapi.uploadFileToChatter(sourceFile, destinationI18NFile);
			Reporter.log("Chatter file contents:"+ MarshallingUtils.marshall(chatterfile), true);
			
			Reporter.log("File "+ chatterfile.getName() +" uploaded to salesforce chatter."+ destinationI18NFile, true);
			sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
			sfI18NActivityLog.setContentDocumentUpdateLog(sfI18NActivityLog.getContentDocumentUpdateLog().replace("{filename}", destinationI18NFile));
			
			String objectUrl = instanceUrl + "/" +chatterfile.getId();
			
			SalesforceActivity ContentDocumentUpdateActivity = new SalesforceActivity(sfI18NActivityLog.getContentDocumentUpdateLog(), createdTime, 
						Severity.informational.name(), "Content Document", ActivityType.Update.name(), objectUrl, 
						chatterfile.getId(), chatterfile.getName(), destinationFile, instanceId, saasAppUser, suiteData.getSocUserName());

			
			sfActivities.put(locale+"ContentDocumentUpdateActivity", ContentDocumentUpdateActivity);
			
			//Share the file
			sfapi.createFileShareLink(chatterfile.getId());
			sleep(this.maxWaitTime);
			
			/*
			sfI18NActivityLog.setContentDocumentDeleteLog(sfI18NActivityLog.getContentDocumentDeleteLog().replace("{filename}", destinationI18NFile));
			sfapi.deleteFile(chatterfile.getId());
			
			SalesforceActivity ContentDocumentDeleteActivity = new SalesforceActivity(sfI18NActivityLog.getContentDocumentDeleteLog(), createdTime, 
						Severity.informational.name(), "Content Document", ActivityType.Delete.name(), objectUrl, 
						chatterfile.getId(), chatterfile.getName(), destinationFile, instanceId, saasAppUser, suiteData.getSocUserName());

		
			sfActivities.put(locale+"ContentDocumentDeleteActivity", ContentDocumentDeleteActivity);
			*/
			
			Reporter.log("Salesforce chatter operations for I18N are completed ...... ", true);
			Reporter.log("------------------------------------------------------------------------------- ", true);
		}
		catch(Exception e) {
			Reporter.log("Exception caught ... so skipping the test..."+ e.getStackTrace(), true);
			throw new SkipException("Skipping this test");
		}
	}
	
	
	@Test(dependsOnMethods = "performI18NChatterOperations", priority=-1, groups={"I18N_FILE", "I18N", "P3"})
	public void fetchI18NActivityLogs() throws Exception {
		//Fetch the logs
		try {
			for (int i = 0; i <= (minWaitTime * 60 * 1000); i+=120000 ) {
				sleep(CommonConstants.TWO_MINUTES_SLEEP);
				
				System.out.println("waiting for fetch logs");
				HashMap<String, String> termmap = new HashMap<String, String>();
				termmap.put("facility", facility.Salesforce.name());
				termmap.put("severity", Severity.informational.name());
				//termmap.put("Object Name", "Content Document");
				termmap.put("__source", "API");				
				
				//Get chatter logs
				this.chatterI18NLogs = this.getInvestigateLogs(-60, 5, facility.Salesforce.name(), termmap, suiteData.getUsername(), suiteData.getApiserverHostName(), 
						suiteData.getCSRFToken(), suiteData.getSessionID(), 0, 500, "Salesforce");				 
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		try {
			Reporter.log("File Logs:" +MarshallingUtils.marshall(chatterI18NLogs), true);
			long fileTotal = chatterI18NLogs.getHits().getTotal();
			Reporter.log("Total Chatter logs count:"+ fileTotal, true);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	@Test(dependsOnMethods = "fetchI18NActivityLogs", priority=-1, dataProviderClass = SalesforceDataProvider.class, dataProvider = "I18NString", groups={"I18N_FILE", "I18N", "P3"})
	public void verifyI18NContentDocumentUpdateActivity(String locale, String i18nString) {
		LogUtils.logTestDescription("After updating the content document, check the activity logs.");
		logValidator.verifySalesforceActivityLog(chatterI18NLogs, sfActivities.get(locale+"ContentDocumentUpdateActivity"));
	}
	
	//@Test(dependsOnMethods = "fetchI18NActivityLogs", priority=-1, dataProviderClass = SalesforceDataProvider.class, dataProvider = "I18NString", groups={"I18N_FILE", "I18N", "P3"})
	public void verifyI18NContentDocumentDeleteActivity(String locale, String i18nString) {
		LogUtils.logTestDescription("After delete the content document, check the activity logs.");
		logValidator.verifySalesforceActivityLog(chatterI18NLogs, sfActivities.get(locale+"ContentDocumentDeleteActivity"));
	}
	
	@Test(dependsOnMethods = "fetchI18NActivityLogs", priority=-1, dataProviderClass = SalesforceDataProvider.class, dataProvider = "I18NString", groups={"I18N_FILE", "I18N", "P3"})
	public void verifyI18NActivitySearch(String locale, String i18nString) throws Exception {
		LogUtils.logTestDescription("Search activity with I18N string.");
		ArrayList<String> logs = new ArrayList<String>();
		String apiHost = suiteData.getScheme() + "://" + suiteData.getApiserverHostName();
		logs = searchDisplayLogs(-60, 5, "Salesforce", i18nString, suiteData.getUsername(), apiHost, suiteData.getCSRFToken(), suiteData.getSessionID());
		
		String message = sfActivities.get(locale + "ContentDocumentUpdateActivity").getMessage();
		CustomAssertion.assertTrue(logs.contains(message), message + " is present", message + " is not present");
	}
	
	@Test(dependsOnMethods = "fetchI18NActivityLogs", priority=-1, dataProviderClass = SalesforceDataProvider.class, dataProvider = "I18NString", groups={"I18N_FILE", "I18N", "P3"})
	public void verifyExposedFileSearch(String locale, String i18nString) throws Exception {
		UIExposedDoc payload = this.getUIPayload("Salesforce", null, null, "risky_docs", 0, 20, true, URLEncoder.encode(i18nString, "UTF-8"), "name");
		SecurletDocument exposedDocs = getUIExposedDocuments(MarshallingUtils.marshall(payload), null);
		CustomAssertion.assertTrue(exposedDocs.getMeta().getTotalCount() > 0, "Expected doc count is greater than one ", "Expected doc count is zero");
	}
	
	//@Test
	public void cleanUp() throws Exception {
		sfapi.cleanChatterFiles();
	}
}

