package com.elastica.beatle.tests.securlets;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.testng.Reporter;
import org.testng.SkipException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

import com.elastica.beatle.CommonTest;
import com.elastica.beatle.DateUtils;
import com.elastica.beatle.MarshallingUtils;
import com.elastica.beatle.Authorization.AuthorizationHandler;
import com.elastica.beatle.RestClient.ClientUtil;
import com.elastica.beatle.logger.Logger;
import com.elastica.beatle.securlets.CIQValidator;
import com.elastica.beatle.securlets.ESQueryBuilder;
import com.elastica.beatle.securlets.LogUtils;
import com.elastica.beatle.securlets.LogValidator;
import com.elastica.beatle.securlets.dto.BoxAction;
import com.elastica.beatle.securlets.dto.BoxMetaInfo;
import com.elastica.beatle.securlets.dto.BoxRemediation;
import com.elastica.beatle.securlets.dto.ForensicSearchResults;
import com.elastica.beatle.securlets.dto.Hit;
import com.elastica.beatle.securlets.dto.Source;
import com.elastica.beatle.splunk.SplunkQueries;
import com.elastica.beatle.splunk.SplunkQueryHandlers;
import com.elastica.beatle.splunk.SplunkQueryResult;
import com.elastica.beatle.splunk.SplunkConstants.ServiceLogs;
import com.elastica.beatle.splunk.SplunkConstants.SplunkHosts;
import com.elastica.beatle.tests.securlets.BoxSecurletExposureTests.Remediation;
import com.universal.constants.CommonConstants;
import com.universal.dtos.box.BoxUserInfo;
import com.universal.dtos.box.FileEntry;
import com.universal.dtos.box.FileUploadResponse;
import com.universal.dtos.box.SharedLink;



public class BoxSanityTests extends CommonTest {
	ESQueryBuilder esQueryBuilder = null;
	LogValidator logValidator;
	CIQValidator ciqValidator;
	private String destinationFile;
	private String query;
	ArrayList<String> actualFileMessages = new ArrayList<String>();
	
	String resourceId;
	BoxUserInfo userInfo;

	public BoxSanityTests() throws Exception {
		super();
		esQueryBuilder = new ESQueryBuilder();
		logValidator = new LogValidator();
		
	}	
	
	@BeforeClass(alwaysRun=true)
	public void initBox() throws Exception {
		AuthorizationHandler.disableAnonymization(suiteData);
	}
	
	@Test(groups={"FILE", "P1"})
	public void performSaasFileActivities() throws Exception {
		
		
		
		String steps[] = {	"1. This test perform the saas operations and wait for 3 mins for the logs to reach our portal.", 
		  					"2. Call Investigate API and retrieve the file related investigate logs."};

		LogUtils.logTestDescription(steps);
		try {
			Reporter.log("Starting to perform the operations in box ", true);
			String randomId = UUID.randomUUID().toString();
			String sourceFile = "Box_HIPAA_Test2.txt";
			destinationFile = randomId + "_" + sourceFile;
			query = randomId;

			//Upload the file
			FileUploadResponse uploadResponse = universalApi.uploadFile("/", sourceFile, destinationFile);
			Reporter.log(uploadResponse.getFileName() + " " + uploadResponse.getResponseMessage(), true);
			String fileId = uploadResponse.getFileId();
			Reporter.log("File uploaded to box:"+ destinationFile, true);
			sleep(CommonConstants.SIXTY_SECONDS_SLEEP);

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
			Reporter.log("Box Saas operations for sanity test completed ... ", true);
			Reporter.log("------------------------------------------------------------------------------- ", true);
		}
		catch(Exception e) {
			Reporter.log("Exception caught ... so skipping the test..."+ e.getStackTrace(), true);
			throw new SkipException("Skipping this test");
		}
		
		sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
		ArrayList<String> logs;
		
		
		try {
			for (int i = 0; i <= (10 * 60 * 1000); i+=60000 ) {
				sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
				String apiHost = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(), null).toString();
				logs = searchDisplayLogs(-40, 20, "Box", query, suiteData.getUsername(), apiHost, suiteData.getCSRFToken(), suiteData.getSessionID() );
				actualFileMessages.addAll(logs);
				Set<String> hs = new HashSet<>();
				hs.addAll(actualFileMessages);
				actualFileMessages.clear();
				actualFileMessages.addAll(hs);

				Reporter.log("Actual file messages1:" + actualFileMessages, true);
				if (actualFileMessages.size() >= 6) {break;}
			}
		}
		catch(Exception e) {
			String sourceQuery = "source=\"/var/log/elastica/api/box_"+suiteData.getTenantName()+"*.log\"";
			String env = suiteData.getEnvironmentName();
			
			Reporter.log("Exception caught ... so skipping the test..."+ e.getStackTrace(), true);
			Reporter.log("Trying to get the splunk logs and adding into the report...", true);
			Reporter.log("Connector logs: ");
					
			SplunkQueryResult splunkResult = new SplunkQueryHandlers(suiteData.getEnvironmentName()).executeSplunkQuery(sourceQuery, SplunkHosts.valueOf(env.toUpperCase()), "-60m");
			Reporter.log("Number of results for the query "+splunkResult.getSearchQuery() + "::"+splunkResult.getNumberOfResults(), true);
			if (splunkResult.getNumberOfResults() == 0 ) {
				Reporter.log("Connector is not running/activated or no logs found so far..", true);
			}
			Reporter.log("Result ::"+splunkResult.getQueryResult().toString(), true);
			
			throw new SkipException("Skipping this exception");
		}
	}
	
	
	@Test(dependsOnMethods = "performSaasFileActivities", groups={"FILE", "P1"})
	public void verifyUploadFileActivity() {
		LogUtils.logTestDescription("After uploading the file, verify the upload activity in the activity logs.");
		String expectedLog = "User uploaded " + destinationFile + " to All Files";
		logValidator.verifyLog(actualFileMessages, expectedLog);
	}
	
	@Test(dependsOnMethods = "performSaasFileActivities", groups={"FILE", "P1"})
	public void verifyDownloadActivity() {
		LogUtils.logTestDescription("After downloading the file, verify for the download activity in the activity logs.");
		String expectedLog = "User downloaded "+ destinationFile;
		//Reporter.log("Expected log:" + expectedLog, true);
		logValidator.verifyLog(actualFileMessages, expectedLog);
	}

	@Test(dependsOnMethods = "performSaasFileActivities", groups={"FILE", "P1"})
	public void verifyFileSharedActivity() {
		LogUtils.logTestDescription("After sharing the file, verify the file share activity in the activity logs.");
		String expectedLog = "User shared "+ destinationFile;
		//Reporter.log("Expected log:" + expectedLog, true);
		logValidator.verifyLog(actualFileMessages, expectedLog);
	}

	@Test(dependsOnMethods = "performSaasFileActivities", groups={"FILE", "P1"})
	public void verifyFileUnsharedActivity() {
		
		LogUtils.logTestDescription("After unsharing the file, verify the file unshare activity in the activity logs.");
		String expectedLog = "User unshared "+ destinationFile;
		//Reporter.log("Expected log:" + expectedLog, true);
		logValidator.verifyLog(actualFileMessages, expectedLog);
	}

	@Test(dependsOnMethods = "performSaasFileActivities", groups={"FILE", "P1"})
	public void verifyDeleteActivity() {
		LogUtils.logTestDescription("After deleting the file, verify the file delete activity in the activity logs.");
		String expectedLog = "User deleted item "+ destinationFile;
		//Reporter.log("Expected log:" + expectedLog, true);
		logValidator.verifyLog(actualFileMessages, expectedLog);
	}

	
	//Remediation
	@Test(groups={"REMEDIATION", "P1"})
	public void verifyPolicyRemediationForPublicExposure() throws Exception {
		
		String steps[] = {	
					"1. Upload a file and expose it publically in Box.", 
					"2. Check the exposure correctly applied in Box.",
					"3. Wait for three minutes for the exposed file to be detected in our portal.",
					"4. Remediate the exposed file by removing the shared access thro' remediation API.",
					"5. Verify the remediation happened in Box and clean up the file."
		};

		LogUtils.logTestDescription(steps);
		
		//Prepare the remediation object
		String uniqueId = UUID.randomUUID().toString();
		String sourceFile = "Hello.java";
		String destinationFile = uniqueId+"_Hello.java";

		//Upload a source code file to box root folder
		FileUploadResponse uploadResponse = universalApi.uploadFile("/", sourceFile, destinationFile);
		Reporter.log(uploadResponse.getFileName() + " " + uploadResponse.getResponseMessage());
		Reporter.log("1. Uploaded the file: "+ destinationFile, true);
		
		//Prepare the share link
		FileEntry sharedFile = null;
		SharedLink sharedLink = new SharedLink();
		sharedLink.setAccess("open");
		sharedFile = universalApi.createSharedLink(uploadResponse.getFileId(), sharedLink); 
		Reporter.log("Shared file access:" + sharedFile.getSharedLink().getAccess(), true);
		Reporter.log("Shared file effective access:" + sharedFile.getSharedLink().getEffectiveAccess(), true);
		Reporter.log("2. Shared the file with public access: "+ destinationFile, true);
		
		//Check the access has been applied to the file on box
		CustomAssertion.assertEquals(sharedFile.getSharedLink().getAccess(), "open", "File is not exposed in Box");
		CustomAssertion.assertEquals(sharedFile.getSharedLink().getEffectiveAccess(), "open", "File is not exposed in Box");
		Reporter.log("3. Verified the shared public access in box: "+ destinationFile, true);
		
		//wait for a minute for the exposure to be applied
		sleep(CommonConstants.THREE_MINUTES_SLEEP);		

		//Now apply the remedial action thro' UI server or API server call
		BoxRemediation boxRemediation = getRemediationObject(sharedFile.getOwnedBy().getId(), sharedFile.getType(), sharedFile.getId(), 
				Remediation.UNSHARE.name(), sharedFile.getSharedLink().getAccess(), null);
		
		//Remediate with api
		remediateExposureWithAPI(boxRemediation);
		Reporter.log("4. Applied the remediation to remove the shared link with our API: "+ destinationFile, true);
		
		//Wait for remedial action
		Reporter.log("###### Waiting for the remedial action ...going to sleep "+CommonConstants.THREE_MINUTES_SLEEP + " ms", true);
		sleep(CommonConstants.THREE_MINUTES_SLEEP);	

		//Get the file info from box
		sharedFile = universalApi.getFileInfo(sharedFile.getId());

		//Verify the remediation thro' box API
		assertNull(sharedFile.getSharedLink(), "Remediation "+ Remediation.UNSHARE.name() + "didn't work it seems...");
		Reporter.log("5. Verified the remediation successfully: "+ destinationFile, true);
		
		//cleanup file if everything goes well
		universalApi.deleteFile(sharedFile.getId(), sharedFile.getEtag());
		Reporter.log("6. Deleted the file: "+ destinationFile, true);
	}
	
	//Utilities
	
	public void remediateExposureWithAPI(BoxRemediation remediationObject) throws Exception {

		List<NameValuePair> headers = getHeaders();
		headers.add(new BasicNameValuePair(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON));

		String payload = "{\"objects\":[" + MarshallingUtils.marshall(remediationObject) + "]}";

		Reporter.log("Request body:" + payload, true);
		StringEntity stringEntity = new StringEntity(payload);
		String path = suiteData.getAPIMap().get("getBoxRemediation")
				.replace("{tenant}", suiteData.getTenantName())
				.replace("{version}", suiteData.getBaseVersion());

		URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(), path, null);
		HttpResponse response =  restClient.doPatch(uri, headers, null, stringEntity);
		String responseBody = ClientUtil.getResponseBody(response);

		Reporter.log("Response body:"+ responseBody, true);
		Reporter.log("Response code:"+ response.getStatusLine().getReasonPhrase(), true);
	}
	
	
	private BoxRemediation getRemediationObject(String userId, String docType, String docId, String remedialAction, String currentLink, String remedy) {
		BoxRemediation boxRemediation = new BoxRemediation();

		boxRemediation.setDbName(suiteData.getTenantName());
		boxRemediation.setUser(suiteData.getSaasAppUsername());
		boxRemediation.setUserId(userId);
		boxRemediation.setDocType(docType);
		boxRemediation.setDocId(docId);

		List<String> possibleValues = new ArrayList<String>();
		if(remedialAction.equals("UNSHARE")) {
			possibleValues.add("open"); possibleValues.add("company"); possibleValues.add("collaborators");
		}

		//Meta Info
		BoxMetaInfo boxMetaInfo = new BoxMetaInfo();

		if (remedy != null) {
			if(remedialAction.equals("SHARE_EXPIRE")) {
				boxMetaInfo.setCurrentLink(null);
				boxMetaInfo.setCollabs(null);
				boxMetaInfo.setExpireOn(remedy);
			} else {
				boxMetaInfo.setAccess(remedy);
			}
		}

		if(currentLink != null) {
			boxMetaInfo.setCurrentLink(currentLink);
		}

		List<BoxAction> actions = new ArrayList<BoxAction>();
		BoxAction boxAction = new BoxAction();
		boxAction.setCode(remedialAction);
		boxAction.setPossibleValues(possibleValues);

		boxAction.setMetaInfo(boxMetaInfo);
		actions.add(boxAction);
		boxRemediation.setActions(actions);
		return boxRemediation;
	}
	
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
		ForensicSearchResults fsr = MarshallingUtils.unmarshall(responseBody, ForensicSearchResults.class);
		assertEquals(HttpStatus.SC_OK, getResponseStatusCode(response), "Response code verification failed");
		return this.retrieveActualMessages(fsr);
	}	

	private ArrayList<String> retrieveActualMessages(ForensicSearchResults fsr) {
		ArrayList<String> alist = new ArrayList<String>();

		for (Hit hit : fsr.getHits().getHits()) {
			Source source  = hit.getSource();
			alist.add(source.getMessage());
		}
		return alist;
	}
	
		
}