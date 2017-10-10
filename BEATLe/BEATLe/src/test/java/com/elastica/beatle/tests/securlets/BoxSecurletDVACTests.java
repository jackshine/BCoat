package com.elastica.beatle.tests.securlets;

import java.io.FileInputStream;
import java.net.URI;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import org.testng.Reporter;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

import com.elastica.beatle.CommonTest;
import com.elastica.beatle.DateUtils;
import com.elastica.beatle.MarshallingUtils;
import com.elastica.beatle.RestClient.ClientUtil;
import com.elastica.beatle.ciq.dto.ContentChecks;
import com.elastica.beatle.ciq.dto.ESResults;
import com.elastica.beatle.securlets.BoxDataProvider;
import com.elastica.beatle.securlets.CIQValidator;
import com.elastica.beatle.securlets.ESQueryBuilder;
import com.elastica.beatle.securlets.LogUtils;
import com.elastica.beatle.securlets.LogValidator;
import com.elastica.beatle.securlets.SecurletUtils;
import com.elastica.beatle.securlets.SecurletUtils.facility;
import com.elastica.beatle.securlets.dto.ForensicSearchResults;
import com.elastica.beatle.securlets.dto.Hit;
import com.elastica.beatle.securlets.dto.Source;
import com.universal.common.UniversalApi;
import com.universal.constants.CommonConstants;
import com.universal.dtos.UserAccount;
import com.universal.dtos.box.AccessibleBy;
import com.universal.dtos.box.BoxCollaboration;
import com.universal.dtos.box.BoxFolder;
import com.universal.dtos.box.BoxGroup;
import com.universal.dtos.box.BoxMembership;
import com.universal.dtos.box.BoxUserInfo;
import com.universal.dtos.box.BoxWeblink;
import com.universal.dtos.box.CollaborationInput;
import com.universal.dtos.box.FileEntry;
import com.universal.dtos.box.FileUploadResponse;
import com.universal.dtos.box.GroupInput;
import com.universal.dtos.box.GroupList;
import com.universal.dtos.box.Item;
import com.universal.dtos.box.MembershipInput;
import com.universal.dtos.box.Parent;
import com.universal.dtos.box.SharedLink;
import com.universal.dtos.box.UserCollection;
import com.universal.dtos.box.WeblinkInput;



public class BoxSecurletDVACTests extends SecurletUtils {
	ESQueryBuilder esQueryBuilder = null;
	LogValidator logValidator;
	CIQValidator ciqValidator;
	BoxActivityLog boxActivityLog;
	private String destinationFile;
	private String renamedFile;
	private String query;
	ArrayList<String> messages = new ArrayList<String>();
	
	

	String resourceId;

	//String filename;
	private String uniqueFilename;
	private String uniqueFolder1;
	private String uniqueFolder2;
	private String uniqueFolder3;
	

	BoxUserInfo userInfo;


	public BoxSecurletDVACTests() throws Exception {
		super();
		esQueryBuilder = new ESQueryBuilder();
		logValidator = new LogValidator();
		boxActivityLog = new BoxActivityLog();
	}
	
	


	@Test(priority = -10, groups={"FILE", "P1", "DVAC", "RBAC", "REGRESSION", "BOX"})
	public void performSaasFileActivitiesAsEndUser() throws Exception {
		
		saasAppUserAccount = new UserAccount(suiteData.getSaasAppEndUser1Name(), suiteData.getSaasAppEndUser1Password(), 
				"ENDUSER", suiteData.getEnvironmentName(), null, suiteData.getDomainName());
			
		UniversalApi endUserUniversalApi = new UniversalApi(suiteData.getSaasApp(), saasAppUserAccount); 
		
		String randomId = UUID.randomUUID().toString();
		String sourceFile = "Box_HIPAA_Test2.txt";
		String updateFile = "BE.txt";
		destinationFile = randomId + "_" + sourceFile;
		renamedFile     = randomId + "_renamed_" + sourceFile;
		query = randomId;

		//Upload the file
		FileUploadResponse uploadResponse = endUserUniversalApi.uploadFile("/", sourceFile, destinationFile);
		Reporter.log(uploadResponse.getFileName() + " " + uploadResponse.getResponseMessage(), true);
		String fileId = uploadResponse.getFileId();
		
		sleep(CommonConstants.THIRTY_SECONDS_SLEEP);
		
		//Download the file
		endUserUniversalApi.downloadFile(fileId, destinationFile);
		
		sleep(CommonConstants.THIRTY_SECONDS_SLEEP);
		
		//Update the file
		endUserUniversalApi.updateFile(uploadResponse.getFileId(), updateFile, uploadResponse.getEtag(), destinationFile);
		
		sleep(CommonConstants.THIRTY_SECONDS_SLEEP);
		
		//Rename the file
		endUserUniversalApi.renameFile(fileId, renamedFile);
		
		sleep(CommonConstants.THIRTY_SECONDS_SLEEP);

		//Create shared link
		FileEntry sharedfile = endUserUniversalApi.createDefaultSharedLink(fileId);
		
		sleep(CommonConstants.THIRTY_SECONDS_SLEEP);

		//Unshare the link
		FileEntry unsharedfile = endUserUniversalApi.disableSharedLink(fileId);
		
		sleep(CommonConstants.THIRTY_SECONDS_SLEEP);

		//delete the file
		endUserUniversalApi.deleteFile(unsharedfile.getId(), unsharedfile.getEtag());
		
		sleep(CommonConstants.THIRTY_SECONDS_SLEEP);

		//restore the file
		FileEntry bfile = endUserUniversalApi.restoreFileFromTrash(fileId, renamedFile);
		
		sleep(CommonConstants.THIRTY_SECONDS_SLEEP);

		//lock the file
		endUserUniversalApi.lockFile(fileId, false);
		
		sleep(CommonConstants.THIRTY_SECONDS_SLEEP);

		//download the file after lock
		endUserUniversalApi.downloadFile(fileId, destinationFile);
		
		sleep(CommonConstants.THIRTY_SECONDS_SLEEP);

		//unlock the file
		endUserUniversalApi.unlockFile(fileId);
		
		sleep(CommonConstants.THIRTY_SECONDS_SLEEP);

		//delete the file after restore
		endUserUniversalApi.deleteFile(bfile.getId(), bfile.getEtag());
		
		sleep(CommonConstants.THIRTY_SECONDS_SLEEP);
		
		//purge the file
		endUserUniversalApi.purgeFile(fileId);		
	}


	@Test(priority = -10, groups={"FILE", "P1", "DVAC", "RBAC", "REGRESSION", "BOX"})
	public void performSaasFolderActivitiesAsAdmin() throws Exception {
		String randomId = UUID.randomUUID().toString();
		String sourceFile = "test.pdf";
		uniqueFilename = randomId + "_" + sourceFile;
		uniqueFolder1   = randomId + "_folder1";
		uniqueFolder2   = randomId + "_folder2";
		uniqueFolder3   = randomId + "_folder3";

		String folderQuery = randomId;

		//Upload the file
		FileUploadResponse uploadResponse = universalApi.uploadFile("/", sourceFile, uniqueFilename);
		Reporter.log(uploadResponse.getFileName() + " " + uploadResponse.getResponseMessage(), true);
		String fileId = uploadResponse.getFileId();
		sleep(CommonConstants.THIRTY_SECONDS_SLEEP);
		
		//Download the file
		universalApi.downloadFile(fileId, uniqueFilename);
		sleep(CommonConstants.THIRTY_SECONDS_SLEEP);

		//create folder1
		BoxFolder folderObj1 = universalApi.createFolder(uniqueFolder1);
		boxActivityLog.setFolderUploadLog(boxActivityLog.getFolderUploadLog().replace("{foldername}", uniqueFolder1)
																			 .replace("{destinationfolder}", "All Files"));
		
		String folderId1 = folderObj1.getId();
		sleep(CommonConstants.THIRTY_SECONDS_SLEEP);

		//create folder2
		BoxFolder folderObj2 = universalApi.createFolder(uniqueFolder2);
		String folderId2 = folderObj2.getId();
		sleep(CommonConstants.THIRTY_SECONDS_SLEEP);

		//Perform folder operations
		//upload the file to folder1
		uploadResponse = universalApi.uploadFile(folderId1, sourceFile, uniqueFilename);
		fileId = uploadResponse.getFileId();
		boxActivityLog.setFolderFileUploadLog(boxActivityLog.getFolderFileUploadLog().replace("{foldername}", uniqueFolder1)
				 .replace("{filename}", uniqueFilename));
		sleep(CommonConstants.THIRTY_SECONDS_SLEEP);

		//download the file again
		universalApi.downloadFile(fileId, uniqueFilename);
		sleep(CommonConstants.THIRTY_SECONDS_SLEEP);
		
		//copy file to folder2
		universalApi.copyFile(uploadResponse.getFileId(), folderId2);
		boxActivityLog.setFolderToFileCopyLog(boxActivityLog.getFolderToFileCopyLog().replace("{filename}", uploadResponse.getFileName()));
		sleep(CommonConstants.THIRTY_SECONDS_SLEEP);
		
		//create one more folder
		BoxFolder folderObj3 = universalApi.createFolder(uniqueFolder3);
		String folderId3 = folderObj3.getId();
		sleep(CommonConstants.THIRTY_SECONDS_SLEEP);
		
		//copy folder
		universalApi.copyFolder(folderId2, folderId3);
		boxActivityLog.setFolderCopyLog(boxActivityLog.getFolderCopyLog().replace("{foldername}", uniqueFolder2));
		
		sleep(CommonConstants.THIRTY_SECONDS_SLEEP);
		
		//update folder or renaming the folder
		String updatedFolderName3 = randomId +"_updated_folder3";
		universalApi.updateFolder(folderId3, updatedFolderName3);
		boxActivityLog.setFolderRenameLog(boxActivityLog.getFolderRenameLog().replace("{foldername}", updatedFolderName3));
		sleep(CommonConstants.THIRTY_SECONDS_SLEEP);

		//delete folder
		universalApi.deleteFolder(folderId3, true, null);
		boxActivityLog.setFolderDeleteLog(boxActivityLog.getFolderDeleteLog().replace("{foldername}", updatedFolderName3));
		sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
		
		//create shared link for folder
		BoxFolder sharedFolder = universalApi.createSharedLinkForFolder(folderId2);
		SharedLink folderSharedLink =  sharedFolder.getSharedLink();
		boxActivityLog.setFolderShareLog(boxActivityLog.getFolderShareLog().replace("{foldername}", uniqueFolder2));
		
		sleep(CommonConstants.THIRTY_SECONDS_SLEEP);
		
		//Set expiration date for sharing
		folderSharedLink.setUnsharedAt(DateUtils.getDaysFromCurrentTime(2));
		universalApi.createSharedLinkForFolder(folderId2, folderSharedLink);
		boxActivityLog.setFolderShareLog(boxActivityLog.getFolderShareLog().replace("{foldername}", uniqueFolder2));
		sleep(CommonConstants.THIRTY_SECONDS_SLEEP);

		//disable shared link for folder
		universalApi.disableSharedLinkForFolder(folderId2);
		boxActivityLog.setFolderUnshareLog(boxActivityLog.getFolderUnshareLog().replace("{foldername}", uniqueFolder2));
		sleep(CommonConstants.THIRTY_SECONDS_SLEEP);
		
		//restore folder from trash
		universalApi.restoreFolder(folderId3, updatedFolderName3);
		boxActivityLog.setFolderRestoreLog(boxActivityLog.getFolderRestoreLog().replace("{foldername}", uniqueFolder3));
		sleep(CommonConstants.THIRTY_SECONDS_SLEEP);
		
		//move folder
		universalApi.moveFolder(folderId1, folderId3);
		sleep(CommonConstants.THIRTY_SECONDS_SLEEP);
		boxActivityLog.setFolderMoveLog(boxActivityLog.getFolderMoveLog().replace("{foldername}", uniqueFolder1));
		boxActivityLog.setFolderUnsyncLog(boxActivityLog.getFolderUnsyncLog().replace("{foldername}", uniqueFolder1));
		//purge folder
		//universalApi.purgeFolder(anotherFolderObj.getId());
		
		//Cleanup
		folderObj1 = universalApi.getFolderInfo(folderObj1.getId());
		universalApi.deleteFolder(folderObj1.getId(), true, folderObj1.getEtag());
		
		folderObj2 = universalApi.getFolderInfo(folderObj2.getId());
		universalApi.deleteFolder(folderObj2.getId(), true, folderObj2.getEtag());
		
		folderObj3 = universalApi.getFolderInfo(folderObj3.getId());
		universalApi.deleteFolder(folderObj3.getId(), true, folderObj3.getEtag());		
	}
	
	
	@Test(priority = -5, groups={"FILE", "RBAC", "DVAC", "BOX", "REGRESSION", "P1"})
	public void fetchAndVerifyDVACActivityLogs() throws Exception {
		
		ForensicSearchResults logs;
		
		for (int retry = 0; retry < 2; retry++) {
			sleep(CommonConstants.THIRTY_SECONDS_SLEEP);
			try{
				
				LogUtils.logTestDescription("Retrieve the activities by the admin user assigned to a domain access profile ");
				HashMap<String, String> termmap = new HashMap<String, String>();
				termmap.put("facility", "Box");
				termmap.put("__source", "API");
				
				String apiHost = suiteData.getScheme() + "://" + suiteData.getApiserverHostName();
				//Fetch the activity logs from yesterday to tomorrow and limited to 500
				//Get file related logs
				logs = getInvestigateLogs(-180000, 10, "Box", termmap, suiteData.getUsername().toLowerCase(), 
						apiHost, suiteData.getCSRFToken(), suiteData.getSessionID(), 0, 500, "Box");
				
				this.logValidator = new LogValidator(logs);

				logValidator.verifyMetadata();
				logValidator.verifyDomainAdminLogs();
				
			}
			catch(Exception e) {}
		}
		Reporter.log("All messages:" + messages, true);
	}
	
	@Test(groups={"DVAC", "BOX", "REGRESSION", "P1", "CUSTOMER_DATA_SEPARATION"})
	public void cusomterDataSeparationGetActivityLogsForOtherDomain() throws Exception {

		HashMap<String, String> termmap = new HashMap<String, String>();
		termmap.put("facility", facility.Box.name());
		termmap.put("Activity_type", "File");
		
		Reporter.log("Retrieving the logs from Elastic Search ...", true);
		ESQueryBuilder esQueryBuilder = new ESQueryBuilder();

		String tsfrom = DateUtils.getMinutesFromCurrentTime(30);
		String tsto   = DateUtils.getMinutesFromCurrentTime(5);	

		//Get headers
		List<NameValuePair> headers = getHeaders();
		ListIterator<NameValuePair> iter = headers.listIterator();
		while(iter.hasNext()){
		    if(iter.next().getValue().equals(suiteData.getUsername().toLowerCase())){
		    	iter.remove();
		    }
		}
		//modify the x-user to some other value
		headers.add(new BasicNameValuePair("X-User", "admin@securletbeatle.com"));
		
		String payload = esQueryBuilder.getESQuery(tsfrom, tsto, facility.Box.name(), termmap, "admin@securletbeatle.com", suiteData.getApiserverHostName(), 
				suiteData.getCSRFToken(), suiteData.getSessionID(), 0, 200, "Box");

		Reporter.log("Request body:"+ payload, true);

		//HttpRequest
		String path = suiteData.getAPIMap().get("getInvestigateLogs") ;
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(), path);
		Reporter.log("URI ::"+dataUri.toString(), true);

		HttpResponse response =  restClient.doPost(dataUri, headers, null, new StringEntity(payload));
		assertEquals(getResponseStatusCode(response), HttpStatus.SC_FORBIDDEN, "Response code verification failed");
	}
	
	
	
	@Test(groups={"FILTERS", "BOX", "REGRESSION", "P1"})
	public void verifyAnonymizedLogs() throws Exception {
		
		String payload = "{\"userAnonymization\":true,\"id\":\"55d7002e9dfa5168e5905c9b\"}";
		String responseBody = this.updateUserAnonymization(payload);
		
		ForensicSearchResults logs;
		String[] steps = { "1. Turn on the anonymization and get the investigate logs.",
				           "2. Verify the username in message is anonymized.",
				           "3. Turn off the anonymizarion." };
		LogUtils.logTestDescription(steps);
		HashMap<String, String> termmap = new HashMap<String, String>();
		termmap.put("facility", "Box");
		termmap.put("__source", "API");
		
		for (int retry = 0; retry < 1; retry++) {

			try{
				String apiHost = suiteData.getScheme() + "://" + suiteData.getApiserverHostName();
				//Fetch the activity logs from yesterday to tomorrow and limited to 500
				logs = getInvestigateLogs(-18000, 10, "Box", termmap, suiteData.getUsername().toLowerCase(), 
						apiHost, suiteData.getCSRFToken(), suiteData.getSessionID(), 0, 500, "investigate");

				this.logValidator = new LogValidator(logs);

				logValidator.verifyMetadata();
				//Add the method and update it
				logValidator.verifyAnonymizedLogs();
				
			}
			catch(Exception e) {}
		}
		
		
		payload = "{\"userAnonymization\":false,\"id\":\"55d7002e9dfa5168e5905c9b\"}";
		responseBody = this.updateUserAnonymization(payload);
		Reporter.log("Response body:"+ responseBody, true);
		for (int retry = 0; retry < 1; retry++) {

			try{
				String apiHost = suiteData.getScheme() + "://" + suiteData.getApiserverHostName();
				//Fetch the activity logs from yesterday to tomorrow and limited to 500
				logs = getInvestigateLogs(-18000, 10, "Box", termmap, suiteData.getUsername().toLowerCase(), 
						apiHost, suiteData.getCSRFToken(), suiteData.getSessionID(), 0, 500, "investigate");

				this.logValidator = new LogValidator(logs);

				logValidator.verifyMetadata();
				//Add the method and update it
				logValidator.verifyUnAnonymizedLogs();
				
			}
			catch(Exception e) {}
		}
	}
	
	
	
	public ForensicSearchResults getInvestigateLogs(int from, int to, String facility, HashMap<String, String> hmap, String email, 
			String apiServerUrl, String csrfToken, String sessionId, int offset, int limit, String sourceName) throws Exception {

		Reporter.log("Retrieving the logs from Elastic Search ...", true);
	
		String tsfrom = DateUtils.getMinutesFromCurrentTime(from);
		String tsto   = DateUtils.getMinutesFromCurrentTime(to);	
		
		//Get headers
		List<NameValuePair> headers = getHeaders();
		String payload = "";
		payload = esQueryBuilder.getESQuery(tsfrom, tsto, facility, hmap, email, apiServerUrl, csrfToken, sessionId, offset, limit, sourceName);
		
		Reporter.log("Request body:"+ payload, true);

		//HttpRequest
		String path = suiteData.getAPIMap().get("getInvestigateLogs") ;
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(), path);
		Reporter.log("URI ::"+dataUri.toString(), true);

		HttpResponse response =  restClient.doPost(dataUri, headers, null, new StringEntity(payload));

		String responseBody = ClientUtil.getResponseBody(response);
		Reporter.log("==============================================================================");
		Reporter.log("Response body:"+ responseBody, true);
		ForensicSearchResults fsr = MarshallingUtils.unmarshall(responseBody, ForensicSearchResults.class);
		return fsr;

	}

}