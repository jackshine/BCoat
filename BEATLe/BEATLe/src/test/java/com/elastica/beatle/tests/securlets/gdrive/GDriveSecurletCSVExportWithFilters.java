package com.elastica.beatle.tests.securlets.gdrive;

import com.elastica.beatle.tests.securlets.*;
import com.elastica.beatle.tests.securlets.salesforce.*;
import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.elastica.beatle.DateUtils;
import com.elastica.beatle.MarshallingUtils;
import com.elastica.beatle.RestClient.ClientUtil;
import com.elastica.beatle.securlets.LogUtils;
import com.elastica.beatle.securlets.LogValidator;
import com.elastica.beatle.securlets.SecurletUtils;
import com.elastica.beatle.securlets.SecurletsConstants;
import com.elastica.beatle.securlets.SecurletUtils.elapp;
import com.elastica.beatle.securlets.SecurletUtils.facility;
import com.elastica.beatle.securlets.dto.ForensicSearchResults;
import com.elastica.beatle.securlets.dto.SecurletDocument;
import com.elastica.beatle.securlets.dto.UIExposedDoc;
import com.universal.util.OAuth20Token;
import org.testng.annotations.AfterClass;


public class GDriveSecurletCSVExportWithFilters extends SecurletUtils{

	//Salesforce sfapi;
	SalesforceActivityLog sfActivityLog;
	HashMap<String, SalesforceActivity> sfActivities = new HashMap<String, SalesforceActivity>();
	LogValidator logValidator;
	protected ForensicSearchResults chatterLogs, sobjectLogs, contentInspectionLogs, leadLogs, 
						accountLogs, opportunityLogs, caseLogs, contactLogs;
	long maxWaitTime = 20;
	long minWaitTime = 10;

	String saasAppUsername;
	String saasAppPassword;
	String saasAppUserRole;
	String createdTime;
	String destinationFile;
	String saasAppUser;
	String instanceUrl;
	String instanceId;
	String contentDocumentName;
	String appName = "Google Apps";

	public GDriveSecurletCSVExportWithFilters() throws Exception {
		logValidator = new LogValidator(); 
	}
        
        @AfterClass
        public void oneTimeTearDown() {
            filesCleanUp();
        }
        
	@Test(groups={"EXPORT", "GOOGLEAPPS", "REGRESSION", "P1"})
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

		sleep(30000);
		
		String query = "GoogleApps Securlet Data Export";
		String expectedAttachment = "GoogleApps_securlet_docs_" + DateUtils.getCurrentDate();
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
			System.out.println("Destination File Location <UnCompressed FileLocation> :"+destination);
                        File file = new File(destination);
                        
			List<Map<String, String>> allrows = unmarshallCSVIntoList(file, null);
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
	
	
	@Test(dataProviderClass = SalesforceDataProvider.class, dataProvider = "ExportFilter", groups={"EXPORT", "GOOGLEAPPS", "REGRESSION", "P1"})
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
		
		String query = "GoogleApps Securlet Data Export";
		String expectedAttachment = "GoogleApps_securlet_docs_" + DateUtils.getCurrentDate();
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
	
	
	@Test(dataProviderClass = SalesforceDataProvider.class, dataProvider = "ExportUsersFilter", groups={"EXPORT", "GOOGLEAPPS", "REGRESSION", "P1"})
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
			expectedAttachment = "GoogleApps_securlet_users_" + DateUtils.getCurrentDate();
		} else if( exportType.equals("ext_collabs")) {
			expectedAttachment = "GoogleApps_securlet_ext_collabs_" + DateUtils.getCurrentDate();
		}
		
		String query = "GoogleApps Securlet Data Export";
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
		docparams.add(new BasicNameValuePair(SecurletsConstants.APP, "GoogleApps"));
		
		if (vlType !=null) {
			docparams.add(new BasicNameValuePair("content_checks.vl_"+vlType,  "true"));
		}
		
		if (fileType !=null) {
			docparams.add(new BasicNameValuePair("format",  fileType));
			
		}
		
		if (exposureType != null && exposureType.equals("public")) {
			docparams.add(new BasicNameValuePair("exposures.public",  "true"));
		}
		
		
		SecurletDocument exposedUsers = null;
		
		if (exportType.equals("users")) {
			exposedUsers = getExposedUsers(elapp.el_google_apps.name(), docparams);
		} else if( exportType.equals("ext_collabs")) {
			exposedUsers = getCollaborators(elapp.el_google_apps.name(), docparams);
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
	
	
	@Test(dataProviderClass = SalesforceDataProvider.class, dataProvider = "RiskyFilesFilter", groups={"EXPORT", "GOOGLEAPPS", "REGRESSION", "P1"})
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
		
		String expectedAttachment = "GoogleApps_securlet_risky_docs_" + DateUtils.getCurrentDate();
		String tmpdir = System.getProperty("user.dir") + File.separator; //System.getProperty("java.io.tmpdir") ; 
		String query = "GoogleApps Securlet Data Export";
		
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
			
			List<Map<String, String>> allrows = unmarshallCSVIntoList(new File(destination), null);
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
        
     public void filesCleanUp() {
        File f = new File(System.getProperty("user.dir"));
        File[] listFiles = f.listFiles();
        for (File listFile : listFiles) {
            String fileName = listFile.getName();
            Reporter.log("File <Deleted> :" + fileName);
            if (fileName.contains("Google")) {
                listFile.delete();
            }
        }

    }
}
