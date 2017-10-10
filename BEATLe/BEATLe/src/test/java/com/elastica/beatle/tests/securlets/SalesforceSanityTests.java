package com.elastica.beatle.tests.securlets;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.entity.StringEntity;
import org.json.JSONObject;
import org.testng.Reporter;
import org.testng.SkipException;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

import com.elastica.beatle.CommonTest;
import com.elastica.beatle.DateUtils;
import com.elastica.beatle.MarshallingUtils;
import com.elastica.beatle.RestClient.ClientUtil;
import com.elastica.beatle.dci.DCIConstants;
import com.elastica.beatle.securlets.CIQValidator;
import com.elastica.beatle.securlets.ESQueryBuilder;
import com.elastica.beatle.securlets.LogUtils;
import com.elastica.beatle.securlets.LogValidator;
import com.elastica.beatle.securlets.dto.ForensicSearchResults;
import com.elastica.beatle.securlets.dto.Hit;
import com.elastica.beatle.securlets.dto.SalesforceHit;
import com.elastica.beatle.securlets.dto.SalesforceLogs;
import com.elastica.beatle.securlets.dto.SalesforceSource;
import com.elastica.beatle.securlets.dto.Source;
import com.universal.constants.CommonConstants;
import com.universal.dtos.box.BoxUserInfo;
import com.universal.dtos.salesforce.ChatterFile;



public class SalesforceSanityTests extends CommonTest {
	ESQueryBuilder esQueryBuilder = null;
	LogValidator logValidator;
	CIQValidator ciqValidator;
	private String destinationFile;
	private String filetitle;
	private String query;
	ArrayList<String> actualFileMessages = new ArrayList<String>();
	String resourceId;
	BoxUserInfo userInfo;
	

	public SalesforceSanityTests() throws Exception {
		super();
		esQueryBuilder = new ESQueryBuilder();
		logValidator = new LogValidator();
	}	
	
	@Test(groups={"FILE", "P1"})
	public void performSaasFileActivities() throws Exception {
		
		String steps[] = {	"1. This test perform the saas operations and wait for 3 mins for the logs to reach our portal.", 
		  					"2. Call Investigate API and retrieve the file related investigate logs."};

		LogUtils.logTestDescription(steps);
		try {
			
			Reporter.log("Starting to perform the operations in salesforce ", true);
			String randomId = String.valueOf(System.currentTimeMillis());
			
			String sourceFile = DCIConstants.DCI_FILE_UPLOAD_RISK_TYPES_PATH + File.separator + "pci.txt";
			filetitle = "pci";
			destinationFile = randomId + "_" + "pci.txt";
			query = randomId;

			//Upload the file
			ChatterFile chatterfile = sfapi.uploadFileToChatter(sourceFile, destinationFile);
			Reporter.log("File "+ chatterfile.getName() +" uploaded to salesforce chatter."+ destinationFile, true);
			sleep(CommonConstants.THREE_MINUTES_SLEEP);
			
			/*			
			//Download the file
			universalApi.downloadFile(fileId, destinationFile);
			Reporter.log("File downloaded from box:"+ destinationFile, true);
			sleep(CommonConstants.SIXTY_SECONDS_SLEEP);

			//Share the file
			universalApi.createDefaultSharedLink(fileId);
			Reporter.log("File shared with public access:"+ destinationFile, true);
			sleep(CommonConstants.SIXTY_SECONDS_SLEEP);

			//Unshare the link
			FileEntry unsharedfile = universalApi.disableSharedLink(fileId);
			Reporter.log("File sharing disabled on box:"+ destinationFile, true);
			sleep(CommonConstants.SIXTY_SECONDS_SLEEP);

			//delete the file
			universalApi.deleteFile(unsharedfile.getId(), unsharedfile.getEtag());
			Reporter.log("File deleted from onedrive:"+ destinationFile, true);
			*/
			
			Reporter.log("Salesforce sanity operations for sanity test completed ... ", true);
			Reporter.log("------------------------------------------------------------------------------- ", true);
		}
		catch(Exception e) {
			Reporter.log("Exception caught ... so skipping the test..."+ e.getStackTrace(), true);
			throw new SkipException("Skipping this test");
		}
		
		sleep(CommonConstants.FIVE_MINUTES_SLEEP);
		ArrayList<String> logs;
		
		try {
			for (int i = 0; i <= (3 * 60 * 1000); i+=60000 ) {
				sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
				String apiHost = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(), null).toString();
				logs = searchDisplayLogs(-40, 20, "Salesforce", query, suiteData.getUsername(), apiHost, suiteData.getCSRFToken(), suiteData.getSessionID() );
				actualFileMessages.addAll(logs);
				Set<String> hs = new HashSet<>();
				hs.addAll(actualFileMessages);
				actualFileMessages.clear();
				actualFileMessages.addAll(hs);

				Reporter.log("Actual file messages1:" + actualFileMessages, true);
				if (actualFileMessages.size() >= 5) {break;}
			}
		}
		catch(Exception e) {
			Reporter.log("Exception caught ... so skipping the test..."+ e.getStackTrace(), true);
			//throw new SkipException("Skipping this exception");
		}
	}
	
	
	@Test(dependsOnMethods = "performSaasFileActivities", groups={"FILE", "P1"})
	public void verifyContentDocumentUpdateActivity() {
		LogUtils.logTestDescription("After uploading the file, verify the upload activity in the activity logs.");
		//User updated 'Content Document' with Title 'Hello'
		String expectedLog = "User updated 'Content Document' with Title '"+destinationFile+"'";
		//String expectedLog = "User created 'Content Document' with Title '"+destinationFile+"'";
		logValidator.verifyLog(actualFileMessages, expectedLog);
	}
	
	//Commented this test as it is not applicable
	//@Test(dependsOnMethods = "performSaasFileActivities", groups={"FILE", "P1"})
	public void verifyContentVersionUpdateActivity() {
		LogUtils.logTestDescription("After uploading the file, verify for the content version update activity in the activity logs.");
		String expectedLog = "User updated 'Content Version' with Title '"+destinationFile+"'";
		//String expectedLog = "User created 'Content Version' with Title '"+destinationFile+"'";
		//Reporter.log("Expected log:" + expectedLog, true);
		logValidator.verifyLog(actualFileMessages, expectedLog);
	}

	//@Test(dependsOnMethods = "performSaasFileActivities", groups={"FILE", "P1"})
	public void verifyContentVersionHistoryUpdateActivity() {
		LogUtils.logTestDescription("After uploading the file, verify the content version history in the activity logs.");
		String expectedLog = "User updated 'Content Version History' with Title '"+destinationFile+"'";
		//Reporter.log("Expected log:" + expectedLog, true);
		logValidator.verifyLog(actualFileMessages, expectedLog);
	}
	
	//@Test(dependsOnMethods = "performSaasFileActivities", groups={"FILE", "P1"})
	public void verifyContentVersionHistoryDownloadActivity() {
		LogUtils.logTestDescription("After uploading the file, verify the content version history download activity in the activity logs.");
		String expectedLog = "User downloaded 'Content Version History' with Title '"+destinationFile+"'";
		//Reporter.log("Expected log:" + expectedLog, true);
		logValidator.verifyLog(actualFileMessages, expectedLog);
	}

	@Test(dependsOnMethods = "performSaasFileActivities", groups={"FILE", "P1"})
	public void verifyContentInspectionActivity() {
		LogUtils.logTestDescription("After uploading the risk file, verify the content inspection activity in the activity logs.");
		//String expectedLog = "Java "+ destinationFile +" has risk(s) - Source Code";
		String expectedLog = "File "+ destinationFile +" has risk(s) - PCI";
		//Reporter.log("Expected log:" + expectedLog, true);
		logValidator.verifyLog(actualFileMessages, expectedLog);
	}
	
	//Utilities
	
	public ArrayList<String> searchDisplayLogs(int from, int to, String facilty, String query, String email, String apiServerUrl, String csrfToken, String sessionId) throws Exception {
		
		Reporter.log("Retrieving the logs from Elastic Search ...", true);
		String tsfrom = DateUtils.getMinutesFromCurrentTime(from);
		String tsto   = DateUtils.getMinutesFromCurrentTime(to);

		//Get headers
		List<NameValuePair> headers = getHeaders();

		String payload = esQueryBuilder.getSearchQueryForDisplayLogs(tsfrom, tsto, facilty, query, email, apiServerUrl, csrfToken, sessionId);
		Reporter.log("Request body:"+ payload, true);

		//HttpRequest
		String path = suiteData.getAPIMap().get("getInvestigateLogs") ;
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(), path);
		Reporter.log("URI ::"+dataUri.toString(), true);
		
		HttpResponse response =  restClient.doPost(dataUri, headers, null, new StringEntity(payload));
		String responseBody = ClientUtil.getResponseBody(response);
		Reporter.log("==============================================================================");
		Reporter.log("Response body:"+ responseBody, true);
		Reporter.log("==============================================================================");
		
		
		SalesforceLogs fsr = null;
		try {
		 fsr = MarshallingUtils.unmarshall(responseBody, SalesforceLogs.class);
		}
		catch(Exception e){}
		assertEquals(getResponseStatusCode(response), HttpStatus.SC_OK, "Response code verification failed");
		return this.retrieveActualMessages(fsr);
		
	}	

	private ArrayList<String> retrieveActualMessages(SalesforceLogs fsr) {
		ArrayList<String> alist = new ArrayList<String>();

		for (SalesforceHit hit : fsr.getHits().getHits()) {
			SalesforceSource source  = hit.getSource();
			alist.add(source.getMessage());
		}
		return alist;
	}
	
		
}