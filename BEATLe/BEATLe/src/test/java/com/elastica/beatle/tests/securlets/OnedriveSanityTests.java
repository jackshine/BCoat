package com.elastica.beatle.tests.securlets;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.entity.StringEntity;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.elastica.beatle.CommonTest;
import com.elastica.beatle.DateUtils;
import com.elastica.beatle.MarshallingUtils;
import com.elastica.beatle.Authorization.AuthorizationHandler;
import com.elastica.beatle.RestClient.ClientUtil;
import com.elastica.beatle.securlets.ESQueryBuilder;
import com.elastica.beatle.securlets.LogUtils;
import com.elastica.beatle.securlets.LogValidator;
import com.elastica.beatle.securlets.dto.ForensicSearchResults;
import com.universal.constants.CommonConstants;
import com.universal.dtos.onedrive.DocumentSharingResult;
import com.universal.dtos.onedrive.Folder;
import com.universal.dtos.onedrive.ItemResource;
import com.universal.dtos.onedrive.Metadata;
import com.universal.dtos.onedrive.SharingUserRoleAssignment;
import com.universal.dtos.onedrive.UserRoleAssignment;



public class OnedriveSanityTests extends CommonTest {
	ESQueryBuilder esQueryBuilder = null;
	LogValidator logValidator;
	OneDriveActivityLog onedrivelog;
	OneDriveUtils onedriveUtils;

	ArrayList<String> messages = new ArrayList<String>();


	private String destinationFile;
	private String destinationFolder;
	private String query;

	//String filename;
	private String uniqueFilename = "HIPAA_Test2.txt";
	private String uniqueFolder1  = "Securlets_Automation";
	public OnedriveSanityTests() throws Exception {
		esQueryBuilder = new ESQueryBuilder();
		logValidator = new LogValidator();
		onedrivelog = new OneDriveActivityLog();
		onedriveUtils = new OneDriveUtils();

		String uniqueId = UUID.randomUUID().toString();
		query = uniqueId;
		destinationFile = uniqueId + "_" + uniqueFilename;
		destinationFolder = uniqueId + "_" + uniqueFolder1;
	}

	@BeforeClass(alwaysRun=true)
	public void initOffice() throws Exception {
		AuthorizationHandler.disableAnonymization(suiteData);
	}

	@Test(priority = -10, groups={"SANITY", "ONEDRIVE", "P1"})
	public void performSaasOperations() throws Exception {
		LogUtils.logTestDescription("This test perform the saas operations and wait for 5 mins for the logs to reach our portal");
		
		Reporter.log("Starting to perform the operations in onedrive ", true);
		//Upload file 
		ItemResource itemResource = universalApi.uploadSimpleFile("/", uniqueFilename, destinationFile);
		Reporter.log("Item Resource:"+ itemResource.getId(), true);
		Reporter.log("Item Name:"+ itemResource.getName(), true);
		onedrivelog.setFileUploadLog(onedrivelog.getFileUploadLog().replace("{filename}", destinationFile));
		Reporter.log("File uploaded to onedrive:"+ destinationFile, true);
		sleep(CommonConstants.SIXTY_SECONDS_SLEEP);

		//Delete file
		universalApi.deleteFile(itemResource.getId(), itemResource.getETag());
		onedrivelog.setFileDeleteLog(onedrivelog.getFileDeleteLog().replace("{filename}", destinationFile));
		Reporter.log("File deleted from onedrive:"+ destinationFile, true);
		sleep(CommonConstants.SIXTY_SECONDS_SLEEP);

		//Create folder
		Folder folder = universalApi.createFolderV2(destinationFolder);
		onedrivelog.setFolderUploadLog(onedrivelog.getFolderUploadLog().replace("{foldername}", destinationFolder));
		Reporter.log("Folder created in onedrive:"+ destinationFolder, true);
		sleep(CommonConstants.SIXTY_SECONDS_SLEEP);

		//Delete folder
		universalApi.deleteFolderV2(folder.getId(), folder.getETag());
		onedrivelog.setFolderDeleteLog(onedrivelog.getFolderDeleteLog().replace("{foldername}", destinationFolder));
		Reporter.log("Folder deleted in onedrive:"+ destinationFolder, true);
		sleep(CommonConstants.SIXTY_SECONDS_SLEEP);

		//Upload the file again and sharing
		itemResource = universalApi.uploadSimpleFile("/", uniqueFilename, destinationFile);
		
		//Share the file
		SharingUserRoleAssignment shareObject = this.getFileShareObject(itemResource, 1, "Everyone except external users");
		universalApi.shareWithCollaborators(shareObject);
		onedrivelog.setFileShareLog(onedrivelog.getFileShareLog().replace("{filename}", destinationFile)
																.replace("{username}", "Everyone except external users")
																.replace("{permission}", "Read")
																);
		onedrivelog.setFileShareNoUserLog(onedrivelog.getFileShareNoUserLog().replace("{filename}", destinationFile));
		onedrivelog.setFileScopeChangeLog(onedrivelog.getFileScopeChangeLog().replace("{filename}", destinationFile));
		Reporter.log("File " + destinationFile + " uploaded  and shared with all internal users", true);
		sleep(CommonConstants.SIXTY_SECONDS_SLEEP);

		//Disable sharing
		universalApi.disableSharing(destinationFile);
		Reporter.log("Sharing for the file " + destinationFile + " disabled", true);
		onedrivelog.setFileUnShareLog(onedrivelog.getFileUnShareLog().replace("{filename}", destinationFile)
																.replace("{username}", "Everyone except external users")
																.replace("{permission}", "Read")
																);
/*		sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
		
		//clean up Delete the file
		itemResource = universalApi.getFileInfo(itemResource.getId());
		universalApi.deleteFile(itemResource.getId(), itemResource.getETag());
		Reporter.log("File " + destinationFile + " deleted", true);*/
		Reporter.log("************************** Operations on onedrive completed and waiting started ****************************** ", true);
		//Wait for 5 mins for the logs to come to our portal
		sleep(CommonConstants.FIVE_MINUTES_SLEEP);
		sleep(CommonConstants.FIVE_MINUTES_SLEEP);
		sleep(CommonConstants.FIVE_MINUTES_SLEEP);
		sleep(CommonConstants.FIVE_MINUTES_SLEEP);
	}


	@Test(priority = -5, groups={"SANITY", "ONEDRIVE", "P1"})
	public void fetchActivityLogs() throws Exception {
		String steps[] = {"1. Call Investigate API and retrieve the file related investigate logs", 
						  "2. Call Investigate API and retrieve the folder related investigate logs"};
		
		LogUtils.logTestDescription(steps);
		
		ArrayList<String> logs;

		for (int retry = 0; retry < 2; retry++) {
			sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
			try{
				String apiHost = suiteData.getScheme() + "://" + suiteData.getApiserverHostName();
				//Fetch the activity logs from yesterday to tomorrow and limited to 500
				//Get file related logs
				Reporter.log("Searching for File related logs...", true);
				logs = searchDisplayLogs("Office 365", "File", suiteData.getUsername().toLowerCase(), 
						apiHost, suiteData.getCSRFToken(), suiteData.getSessionID(), 0, 500);
				messages.addAll(logs);
				Set<String> hs = new HashSet<>();
				hs.addAll(messages);
				messages.clear();
				messages.addAll(hs);

				sleep(10000);

				//Get folder related logs
				Reporter.log("Searching for Folder related logs...", true);
				logs = searchDisplayLogs("Office 365", "Folder", suiteData.getUsername().toLowerCase(), 
						apiHost, suiteData.getCSRFToken(), suiteData.getSessionID(), 0, 500);
				messages.addAll(logs);
				hs.addAll(messages);
				messages.clear();
				messages.addAll(hs);
			}
			catch(Exception e) {
				Reporter.log("--------------------------------------------------------------------------------------------------- ", true);
				Reporter.log("Exception caught:" + e.getStackTrace(), true);
				Reporter.log("--------------------------------------------------------------------------------------------------- ", true);
			}
			Reporter.log("--------------------------------------------------------------------------------------------------- ", true);
			Reporter.log("All messages:" + messages, true);
			Reporter.log("--------------------------------------------------------------------------------------------------- ", true);
		}
	}

	//Tests

	@Test(groups={"SANITY", "ONEDRIVE", "P1"})
	public void verifyFileUploadActivity() {
		LogUtils.logTestDescription("After uploading the file, verify the upload activity is present in the logs.");
		String expectedLog = onedrivelog.getFileUploadLog();
		logValidator.verifyLog(messages, expectedLog);
	}


	@Test(groups={"SANITY", "ONEDRIVE", "P1"})
	public void verifyFileDeleteActivity() {
		LogUtils.logTestDescription("After deleting the file, verify the delete activity is present in the logs.");
		String expectedLog = onedrivelog.getFileDeleteLog();
		logValidator.verifyLog(messages, expectedLog);
	}

	@Test(groups={"SANITY", "ONEDRIVE", "P1"})
	public void verifyFolderCreateActivity() {
		LogUtils.logTestDescription("After creating the folder, verify the create folder activity is present in the logs.");
		String expectedLog = onedrivelog.getFolderUploadLog();
		logValidator.verifyLog(messages, expectedLog);
	}

	@Test(groups={"SANITY", "ONEDRIVE", "P1"})
	public void verifyFolderDeleteActivity() {
		LogUtils.logTestDescription("After deleting the folder, verify the delete folder activity is present in the logs.");
		String expectedLog = onedrivelog.getFolderDeleteLog();
		logValidator.verifyLog(messages, expectedLog);
	}

	@Test(groups={"SANITY", "ONEDRIVE", "P1"})
	public void verifyFileSharingActivity() {
		LogUtils.logTestDescription("After sharing the file, verify the file share activity is present in the logs.");
		String expectedLog = onedrivelog.getFileShareNoUserLog();
		logValidator.verifyLog(messages, expectedLog);
	}

	@Test(groups={"SANITY", "ONEDRIVE", "P1"})
	public void verifyFileUnSharingActivity() {
		LogUtils.logTestDescription("After unsharing the file, verify the file unshare activity is present in the logs.");
		String expectedLog = onedrivelog.getFileUnShareLog();
		logValidator.verifyLog(messages, expectedLog);
	}



	//Utility methods
	public ArrayList<String> searchDisplayLogs(String facilty, String query, String email, 
			String apiServerUrl, String csrfToken, String sessionId, int offset, int limit) throws Exception {
		
		Reporter.log("Retrieving the logs from Elastic Search ...", true);

		
		String tsfrom = DateUtils.getMinutesFromCurrentTime(-90);
		String tsto   = DateUtils.getMinutesFromCurrentTime(90);	
		
		//Get headers
		List<NameValuePair> headers = getHeaders();
		String payload = esQueryBuilder.getESQuery(tsfrom, tsto, facilty, query, email, apiServerUrl, csrfToken, sessionId, offset, limit);
		Reporter.log("--------------------------------------------------------------------------------------------------- ", true);
		Reporter.log("Request body:"+ payload, true);
		Reporter.log("--------------------------------------------------------------------------------------------------- ", true);
		//HttpRequest
		String path = suiteData.getAPIMap().get("getInvestigateLogs") ;
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(), path);
		Reporter.log("URI ::"+dataUri.toString(), true);

		HttpResponse response =  restClient.doPost(dataUri, headers, null, new StringEntity(payload));

		String responseBody = ClientUtil.getResponseBody(response);
		
		if(getResponseStatusCode(response) != HttpStatus.SC_OK) {
			Reporter.log("Display log API server call failed with the error code:"+ getResponseStatusCode(response), true);
		}
		Reporter.log("--------------------------------------------------------------------------------------------------- ", true);
		Reporter.log("Response body:"+ responseBody, true);
		Reporter.log("--------------------------------------------------------------------------------------------------- ", true);
		ForensicSearchResults fsr = MarshallingUtils.unmarshall(responseBody, ForensicSearchResults.class);
		return onedriveUtils.retrieveActualMessages(fsr);
	}	
	
	public SharingUserRoleAssignment getFileShareObject(ItemResource itemResource, int role, String sharedWithUsers) throws Exception{
		//share the file with Everyone
		SharingUserRoleAssignment shareObject = new SharingUserRoleAssignment();

		ArrayList<UserRoleAssignment> alist = new ArrayList<UserRoleAssignment>();
		UserRoleAssignment userRoleAssignment = new UserRoleAssignment();

		Metadata metadata = new Metadata();
		metadata.setType("SP.Sharing.UserRoleAssignment");

		userRoleAssignment.setMetadata(metadata);
		userRoleAssignment.setRole(role);
		userRoleAssignment.setUserId(sharedWithUsers);
		alist.add(userRoleAssignment);

		shareObject.setUserRoleAssignments(alist);
		shareObject.setResourceAddress(itemResource.getWebUrl());
		shareObject.setValidateExistingPermissions(false);
		shareObject.setAdditiveMode(true);
		shareObject.setSendServerManagedNotification(false);
		shareObject.setCustomMessage("Hi Pls. look at the following document");
		shareObject.setIncludeAnonymousLinksInNotification(false);

		return shareObject;
	}
}