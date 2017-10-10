package com.elastica.beatle.tests.securlets;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.testng.Reporter;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

import com.elastica.beatle.DateUtils;
import com.elastica.beatle.MarshallingUtils;
import com.elastica.beatle.RestClient.ClientUtil;
import com.elastica.beatle.logger.Logger;
import com.elastica.beatle.securlets.CIQValidator;
import com.elastica.beatle.securlets.DocumentValidator;
import com.elastica.beatle.securlets.ESQueryBuilder;
import com.elastica.beatle.securlets.LogUtils;
import com.elastica.beatle.securlets.LogValidator;
import com.elastica.beatle.securlets.O365DataProvider;
import com.elastica.beatle.securlets.SecurletUtils;
import com.elastica.beatle.securlets.SecurletUtils.ActivityType;
import com.elastica.beatle.securlets.SecurletUtils.ObjectType;
import com.elastica.beatle.securlets.SecurletUtils.Severity;
import com.elastica.beatle.securlets.dto.ForensicSearchResults;
import com.elastica.beatle.securlets.dto.Hit;
import com.elastica.beatle.securlets.dto.RemedialAction;
import com.elastica.beatle.securlets.dto.RemediationMetaInfo;
import com.elastica.beatle.securlets.dto.SecurletRemediation;
import com.elastica.beatle.securlets.dto.Source;
import com.universal.constants.CommonConstants;
import com.universal.dtos.onedrive.DocumentSharingResult;
import com.universal.dtos.onedrive.ItemResource;
import com.universal.dtos.onedrive.ItemRoleAssignment;
import com.universal.dtos.onedrive.Metadata;
import com.universal.dtos.onedrive.PasswordProfile;
import com.universal.dtos.onedrive.Result;
import com.universal.dtos.onedrive.SharingUserRoleAssignment;
import com.universal.dtos.onedrive.SiteUserList;
import com.universal.dtos.onedrive.UserInput;
import com.universal.dtos.onedrive.UserList;
import com.universal.dtos.onedrive.UserResult;
import com.universal.dtos.onedrive.UserRoleAssignment;
import com.universal.dtos.onedrive.Value;



public class OneDriveRemediationTests extends SecurletUtils {
	ESQueryBuilder esQueryBuilder = null;
	LogValidator logValidator;
	CIQValidator ciqValidator;
	
	
	String resourceId;
	private String destinationFile;
	
	
	//String filename;
	private String uniqueFilename = "HIPAA_Test2.txt";
	private String uniqueFolder1  = "Securlets_Automation";
	
	OneDriveActivityLog onedrivelog;
	OneDriveUtils onedriveUtils;
	
	String createdTime;
	HashMap<String, OneDriveBusinessActivity> activityMap = new HashMap<String, OneDriveBusinessActivity>();
	String appName = "Office 365";
	
	ForensicSearchResults fileLogs, folderLogs, contentInspectionLogs, file18nLogs;
	
	
	
	public OneDriveRemediationTests() throws Exception {
		super();
		esQueryBuilder = new ESQueryBuilder();
		logValidator = new LogValidator();
		onedriveUtils = new OneDriveUtils();
		
	}
	
	//@Test
	public void getSPLists() throws Exception {
		Result result = universalApi.getSPDocumentList();
		System.out.println(MarshallingUtils.marshall(result));
	}
	
	
	public void getSPDocumentList() throws Exception {
		Result result = universalApi.getSPDocumentList();
	}
	
	//@Test
	public void listUsers() throws Exception {
		UserList users = this.universalGraphApi.listUser();
		
		
		for (Value uservalue : users.getValue()) {
			Reporter.log("Display name:" + uservalue.getDisplayName(), true);
			Reporter.log("Email       :" + uservalue.getMail(), true);
			
		}
	}
	
	
	@Test(dataProviderClass = O365DataProvider.class, dataProvider = "sharedLinkDataProvider")
	public void verifyPolicyRemediationForSourceCodeExposure(String testname, int role, String sharedWithUsers, String currentLink, String[] remedialAction, String[] metaInfo,  String server) throws Exception {
		Reporter.log("Started the test :"+testname, true);
		
		String steps[] = 
		{	"1. This test uploads the file and expose it.", 
			"2. With remediation API, applying the remediation and check remediation applied successfully or not."};

		LogUtils.logTestDescription(steps);
		
		//Prepare the remediation object
		String uniqueId = String.valueOf(System.currentTimeMillis());//UUID.randomUUID().toString();
		String sourceFile = "Hello.java";
		String destinationFile = uniqueId+"_"+sourceFile;
		Reporter.log("1. Uploaded the file :"+destinationFile, true);

		//Upload a source code file to box root folder
		ItemResource itemResource = universalApi.uploadSimpleFile("/", sourceFile, destinationFile);
		Reporter.log(itemResource.getName() + " " + itemResource.getId());
		Reporter.log(MarshallingUtils.marshall(itemResource), true);
		
		universalApi.getFileInfo(itemResource.getId());
		
		//SharingUserRoleAssignment shareObject = onedriveUtils.getFileShareObject(itemResource, 1, "Everyone except external users");
		SharingUserRoleAssignment shareObject = onedriveUtils.getFileShareObject(itemResource, role, sharedWithUsers);
		Reporter.log("2. Shared the file with the role(" + role + ") sharedWithUsers",  true);
		DocumentSharingResult docSharingResult = universalApi.shareWithCollaborators(shareObject);
		
		ItemRoleAssignment rolesBeforeRemediation = universalApi.getSharePointItemRolesAssignments(destinationFile);
		
		sleep(CommonConstants.THREE_MINUTES_SLEEP);
		
		Reporter.log(MarshallingUtils.marshall(rolesBeforeRemediation), true);
		
		String etagAsDocId = getETagAsDocumentId(itemResource.getETag());
		
		Reporter.log("3. Applying the remediation..",  true);
		
		//Now apply the remedial action thro' UI server or API server call
		SecurletRemediation onedriveRemediation = getOneDriveRemediationObject(itemResource.getCreatedBy().getUser().getId(), 
																itemResource.getType(), etagAsDocId, currentLink, remedialAction, metaInfo);
		
		remediateExposureWithAPI(onedriveRemediation);

		sleep(CommonConstants.THREE_MINUTES_SLEEP);
		ItemRoleAssignment rolesAfterRemediation = universalApi.getSharePointItemRolesAssignments(destinationFile);
		
		Reporter.log(MarshallingUtils.marshall(rolesAfterRemediation), true);
		
		Reporter.log("4. Verifying the remediation..",  true);
		
		int beforeRemediation = rolesBeforeRemediation.getValue().size();
		int afterRemediation  = rolesAfterRemediation.getValue().size();
		
		if(remedialAction[0].equals("SHARE_ACCESS")) {
			CustomAssertion.assertTrue(afterRemediation > 0, "Remediation is successful", "Remediation not happened it seems");
		} else {
			CustomAssertion.assertTrue(beforeRemediation != afterRemediation, "Remediation is successful", "Remediation not happened it seems");
		}
		
		Reporter.log("5. Remediation applied successfully..",  true);
		Reporter.log("Cleaning the files...", true);
		
		universalApi.deleteFile(itemResource.getId(), itemResource.getETag());
		
		/*
		onedrivelog.setFileShareLog(onedrivelog.getFileShareLog().replace("{filename}", destinationFile)
				.replace("{username}", "Everyone except external users")
				.replace("{permission}", "Read")
				);
		onedrivelog.setFileScopeChangeLog(onedrivelog.getFileScopeChangeLog().replace("{filename}", destinationFile));

		createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
		OneDriveBusinessActivity fileShareLog = new OneDriveBusinessActivity(onedrivelog.getFileShareLog(), createdTime, Severity.informational.name(),  
				ObjectType.File.name(), ActivityType.Share.name(), suiteData.getSaasAppUsername(), 
				socUserName, getETagAsDocumentId(itemResource.getETag()), itemResource.getSize(),  "Root:Documents", "Office 365");

		activityMap.put("fileShareLog", fileShareLog);

		OneDriveBusinessActivity fileScopeChangeLog = new OneDriveBusinessActivity(onedrivelog.getFileScopeChangeLog(), createdTime, Severity.informational.name(),  
				ObjectType.File.name(), ActivityType.ScopeAdd.name(), suiteData.getSaasAppUsername(), 
				socUserName, getETagAsDocumentId(itemResource.getETag()), itemResource.getSize(),  "Root:Documents", "Office 365");

		activityMap.put("fileScopeChangeLog", fileScopeChangeLog);

		Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);

		//Disable sharing
		universalApi.disableSharing(destinationFile);
		onedrivelog.setFileUnShareLog(onedrivelog.getFileUnShareLog().replace("{filename}", destinationFile)
				.replace("{username}", "Everyone except external users")
				.replace("{permission}", "Read")
				);

		createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
		OneDriveBusinessActivity fileUnshareLog = new OneDriveBusinessActivity(onedrivelog.getFileUnShareLog(), createdTime, Severity.informational.name(),  
				ObjectType.File.name(), ActivityType.Unshare.name(), suiteData.getSaasAppUsername(), 
				socUserName, getETagAsDocumentId(itemResource.getETag()), itemResource.getSize(),  "Root:Documents", "Office 365");

		activityMap.put("fileUnshareLog", fileUnshareLog);
		*/
	}
	
	
	
	@Test
	public void verifyExternalCollaboratorSharingRemediation(/*String testname, String[] remedialAction, String[] metaInfo, String server*/) throws Exception {
		Logger.info( "Starting Policy remediation for source code exposure...");
		
		//Reporter.log("Started the test :"+testname, true);
		
		//Prepare the remediation object
		String uniqueId = UUID.randomUUID().toString();
		String sourceFile = "Hello.java";
		String destinationFile = uniqueId+"_"+sourceFile;

		//Upload a source code file to box root folder
		ItemResource itemResource = universalApi.uploadSimpleFile("/", uniqueFilename, destinationFile);
		Reporter.log(itemResource.getName() + " " + itemResource.getId());
		Reporter.log(MarshallingUtils.marshall(itemResource), true);
		
		universalApi.getFileInfo(itemResource.getId());
		
		//share the file with external collaborator
		
		
		SharingUserRoleAssignment shareObject = new SharingUserRoleAssignment();
		
		ArrayList<UserRoleAssignment> alist = new ArrayList<UserRoleAssignment>();
		UserRoleAssignment userRoleAssignment = new UserRoleAssignment();
		
		Metadata metadata = new Metadata();
		metadata.setType("SP.Sharing.UserRoleAssignment");
		
		userRoleAssignment.setMetadata(metadata);
		userRoleAssignment.setRole(1);
		userRoleAssignment.setUserId("pushpan@gmail.com");
		alist.add(userRoleAssignment);
		
		shareObject.setUserRoleAssignments(alist);
		shareObject.setResourceAddress(itemResource.getWebUrl());
		shareObject.setValidateExistingPermissions(false);
		shareObject.setAdditiveMode(true);
		shareObject.setSendServerManagedNotification(false);
		shareObject.setCustomMessage("Hi Pls. look at the following document");
		shareObject.setIncludeAnonymousLinksInNotification(false);
		
		DocumentSharingResult docSharingResult = universalApi.shareWithCollaborators(shareObject);
		System.out.println(MarshallingUtils.marshall(docSharingResult));
		
				
		//String[] remedialAction = {"UNSHARE", "UNSHARE", "SHARE_ACCESS"};
		//String[] currentLink	= {"", ""};
		//String[] metaInfo	= {"open-edit", "open-view", "Everyone-Read"};
		
		//String[] remedialAction = {"UNSHARE", "UNSHARE"};
		//String[] metaInfo		= {"open-edit", "open-view"};
		
//		String etagAsDocId = getETagAsDocumentId(itemResource.getETag());
//		
//		//Now apply the remedial action thro' UI server or API server call
//		SecurletRemediation onedriveRemediation = getOneDriveRemediationObject(itemResource.getCreatedBy().getUser().getId(), 
//																itemResource.getType(), etagAsDocId, remedialAction, metaInfo);
//		
//		remediateExposureWithAPI(onedriveRemediation);
	}
	
	
	
	
	@Test
	public void verifySiteUsers() throws Exception {
		
		SiteUserList sulist = universalApi.getSPUserList();
		
		for (UserResult uv: sulist.getD().getResults()) {
			System.out.println(uv.getId() + "::" +uv.getEmail());
		}
		
	}
	
	
	/*public String getETagAsDocumentId(String etag) {
		Pattern pattern = Pattern.compile("\\{(.*?)\\}");
		Matcher matchPattern = pattern.matcher(etag);
		matchPattern.find();
		Reporter.log("Document Id as Etag:" + matchPattern.group(1), true);
		//System.out.println(matchPattern.group(1));
		return matchPattern.group(1).toLowerCase();
	}*/
	
	
	
	//@Test()
	public void performGraphOperations() throws Exception {
		
		UserInput userInput =  new UserInput();
		
		userInput.setAccountEnabled(true);
		userInput.setDisplayName("Securlet Automation User");
		userInput.setMailNickname("AutomationUser");
		userInput.setUserPrincipalName("pushpan@o365security.net");
		
		PasswordProfile passProfile = new PasswordProfile();
		passProfile.setPassword("Elastica#123");
		passProfile.setForceChangePasswordNextLogin(false);
		
		userInput.setPasswordProfile(passProfile);
		
		Value userValue = universalGraphApi.createUser(userInput);
		
		Reporter.log("Display name:" + userValue.getDisplayName(), true);
		Reporter.log("Email       :" + userValue.getMail(), true);
		
		UserList users = this.universalGraphApi.listUser();
		
		
		for (Value uservalue : users.getValue()) {
			Reporter.log("Display name:" + uservalue.getDisplayName(), true);
			Reporter.log("Email       :" + uservalue.getMail(), true);
			
		}
		
		
		
	}
	
	
	//@Test(priority = -5)
	public void performFileShareOperations() throws Exception {
		
		ItemResource itemResource = null ;
		try {

			//Upload a file
			itemResource = universalApi.uploadSimpleFile("/", uniqueFilename, destinationFile);
			Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);

			//Create the shared link
			//Get the filename without the extension to share
			String sharefile = StringUtils.split(destinationFile, ".")[0];

			universalApi.createSharedLink(suiteData.getSaasAppUsername(), suiteData.getSaasAppPassword(), sharefile);
			Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);

			universalApi.getFileInfo(itemResource.getId());

			System.out.println("Item Resource:"+ itemResource.getId());
			System.out.println("Item Name:"+ itemResource.getName());

			//Unshare the file
			universalApi.disableSharedLink(suiteData.getSaasAppUsername(), suiteData.getSaasAppPassword(), sharefile);


		}
		
		finally{
			itemResource = universalApi.getFileInfo(itemResource.getId());
			//clean up the file
			universalApi.deleteFile(itemResource.getId(), itemResource.getETag());
		}
		
	}
	
	
	
	/**
	 * This is the utility method to remediate the exposure thro' api. 
	 * @param tenant
	 * @param facility
	 * @param user
	 * @param documentId
	 * @param userId
	 * @param action
	 * @throws Exception
	 */
	public void remediateExposureWithAPI(SecurletRemediation remediationObject) throws Exception {

		List<NameValuePair> headers = getHeaders();

		headers.add(new BasicNameValuePair(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON));

		String payload = "{\"objects\":[" + MarshallingUtils.marshall(remediationObject) + "]}";

		Reporter.log("Request body:" + payload, true);
		StringEntity stringEntity = new StringEntity(payload);
		String path = suiteData.getAPIMap().get("getOnedriveRemediation")
				.replace("{tenant}", suiteData.getTenantName())
				.replace("{version}", suiteData.getBaseVersion());

		URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(), path, null);
		HttpResponse response =  restClient.doPatch(uri, headers, null, stringEntity);
		String responseBody = ClientUtil.getResponseBody(response);
		CustomAssertion.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_ACCEPTED, "Remediation failed with the error "+response.getStatusLine().getStatusCode());
		
		Reporter.log("Response body:"+ responseBody, true);
		Reporter.log("Response code:"+ response.getStatusLine().getReasonPhrase(), true);
	}
	
	
	private SecurletRemediation getOneDriveRemediationObject(String userId, String docType, String docId, String currentLink, String[] remedialAction, String[] metaInfo) {
		SecurletRemediation remediation = new SecurletRemediation();

		remediation.setDbName(suiteData.getTenantName().toLowerCase());
		remediation.setUser(suiteData.getSaasAppUsername());
		remediation.setUserId("");
		remediation.setDocType(docType);
		remediation.setDocId(docId);
		remediation.setInstance(suiteData.getDomainName().toLowerCase());

		List<String> possibleValues = new ArrayList<String>();
		possibleValues.add("Everyone-Read"); 
		possibleValues.add("Everyone except external users-Read");
		possibleValues.add("Everyone-Contribute");
		possibleValues.add("Everyone except external users-Contribute");
		
		List<String> readonlyValues = new ArrayList<String>();
		readonlyValues.add("open-view"); 
		readonlyValues.add("open-edit"); 
		
		List<RemedialAction> actions = new ArrayList<RemedialAction>();
		
		for(int i=0; i<remedialAction.length; i++) {
			RemedialAction remedAction = new RemedialAction();
			
			if(remedialAction[i].equals("UNSHARE")) {

				remedAction.setCode(remedialAction[i]);
				remedAction.setReadonlyValues(readonlyValues);
				//remedAction.setPossibleValues(possibleValues);
				
				RemediationMetaInfo remedMetaInfo = new RemediationMetaInfo();
				remedMetaInfo.setCollabs(null);
				remedMetaInfo.setCurrentLink(metaInfo[i]);
				
				remedAction.setMetaInfo(remedMetaInfo);
				actions.add(remedAction);
				
			}
			
			if(remedialAction[i].equals("SHARE_ACCESS")) {
				
				remedAction.setCode(remedialAction[i]);
				//remedAction.setReadonlyValues(readonlyValues);
				remedAction.setPossibleValues(possibleValues);
				
				RemediationMetaInfo remedMetaInfo = new RemediationMetaInfo();
				remedMetaInfo.setCollabs(null);
				remedMetaInfo.setAccess(metaInfo[i]);
				remedMetaInfo.setCurrentLink(currentLink);
				
				remedAction.setMetaInfo(remedMetaInfo);
				actions.add(remedAction);
			} 
		}
	
		remediation.setActions(actions);
		return remediation;
	}
	
}