package com.elastica.beatle.tests.securlets;

import static org.testng.Assert.assertEquals;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.testng.Reporter;
import org.testng.annotations.Test;

import com.elastica.beatle.CommonTest;
import com.elastica.beatle.MarshallingUtils;
import com.elastica.beatle.RestClient.ClientUtil;
import com.elastica.beatle.logger.Logger;
import com.elastica.beatle.securlets.BoxDataProvider;
import com.elastica.beatle.securlets.CIQValidator;
import com.elastica.beatle.securlets.ESQueryBuilder;
import com.elastica.beatle.securlets.LogValidator;
import com.elastica.beatle.securlets.SecurletUtils;
import com.elastica.beatle.securlets.SecurletUtils.ExposureTypes;
import com.elastica.beatle.securlets.SecurletUtils.facility;
import com.elastica.beatle.securlets.dto.BoxAction;
import com.elastica.beatle.securlets.dto.BoxDocument;
import com.elastica.beatle.securlets.dto.BoxMetaInfo;
import com.elastica.beatle.securlets.dto.BoxRemediation;
import com.elastica.beatle.securlets.dto.ExposureTotals;
import com.universal.common.UniversalApi;
import com.universal.constants.CommonConstants;
import com.universal.dtos.UserAccount;
import com.universal.dtos.box.AccessibleBy;
import com.universal.dtos.box.BoxCollaboration;
import com.universal.dtos.box.BoxFolder;
import com.universal.dtos.box.BoxUserInfo;
import com.universal.dtos.box.CollaborationInput;
import com.universal.dtos.box.Collaborations;
import com.universal.dtos.box.FileEntry;
import com.universal.dtos.box.FileUploadResponse;
import com.universal.dtos.box.Item;
import com.universal.dtos.box.SharedLink;
import com.universal.dtos.box.UserCollection;



public class BoxBCBTests extends SecurletUtils {
	ESQueryBuilder esQueryBuilder = null;
	LogValidator logValidator;
	CIQValidator ciqValidator;
	String resourceId;
	BoxUserInfo userInfo;
	String uniqueId;
	UserAccount externalUserAccount;
	UniversalApi externalUniversalApi;
	

	public BoxBCBTests() throws Exception {
		super();
		esQueryBuilder = new ESQueryBuilder();
		logValidator = new LogValidator();
		uniqueId = UUID.randomUUID().toString();
		externalUserAccount = new UserAccount("pushpan@gmail.com", "Avayam#123", "ADMIN", 2);
		externalUniversalApi = new UniversalApi("BOX", externalUserAccount);
	}

	
	
	/**
	 * Test 1
	 * External user invites the internal user as collborator. Check the exposure count for files increased by 1 after collaboration.
	 * Also verify exposure count decremented by 1 after removing the collaboration.
	 * @throws Exception
	 */
	
	@Test(groups={"EXTERNAL_USER_DASHBOARD", "EXTERNAL", "REGRESSION"})
	public void verifyExternalUserExposuresCountMetrics() throws Exception {
		BoxFolder folder1 = null;
		String internalUser = "box-admin@securletbeatle.com";
		String uniqueId = UUID.randomUUID().toString();
		String sourceFile = "Hello.java";
		String destinationFile = uniqueId+"_Hello.java";
		
		try {

			Logger.info( "External user "+ this.externalUserAccount.getUsername() + " collaborating with internal user");

			//Get the exposure count
			long countExternal = 0;

			ExposureTotals expsoureTotals = getExposuresMetricsTotal("false", facility.Box.name());
			
			//Get the external exposure count
			countExternal = expsoureTotals.getExternalExposouresCount();
			
			Reporter.log("External count:"+countExternal, true);
			
			//create a temporary folder
			folder1  = externalUniversalApi.createFolder(uniqueId + "_CollabWithInternalUser");
			FileUploadResponse uploadedFile = externalUniversalApi.uploadFile(folder1.getId(), sourceFile,  destinationFile);
			
			Thread.sleep(CommonConstants.THIRTY_SECONDS_SLEEP);
			
			SharedLink sharedLink =  new SharedLink();
			sharedLink.setAccess("collaborators");
			
			folder1 = externalUniversalApi.createSharedLinkForFolder(folder1.getId(), sharedLink);
			
			//External user collaborate the user1 as editor 
			BoxCollaboration bcollab = createCollaboration(folder1.getId(), "folder", internalUser, "editor", true);
			Thread.sleep(CommonConstants.THREE_MINUTES_SLEEP);
			
			universalApi.downloadFile(uploadedFile.getFileId(), uploadedFile.getFileName());
			
			//Check the count 
			ExposureTotals expsoureTotalsAfter = getExposuresMetricsTotal("false", facility.Box.name());
			CustomAssertion.assertEquals(expsoureTotalsAfter.getExternalExposouresCount(), (countExternal+1), "External exposure metrics not incremented");
			
			//Delete the collaboration
			externalUniversalApi.deleteCollaboration(bcollab);
			
			//externalUniversalApi.deleteFolder(folder1.getId(), true, folder1.getEtag());
			Thread.sleep(CommonConstants.THREE_MINUTES_SLEEP);
			
			//Check the count 
			expsoureTotals = getExposuresMetricsTotal("false", facility.Box.name());
			CustomAssertion.assertEquals(expsoureTotals.getExternalExposouresCount(), countExternal, 
																"External exposure metrics not decremented once the file deleted");
			
		}
				
		finally{
			externalUniversalApi.deleteFolder(folder1.getId(), true, folder1.getEtag());
		}
	}
	
	
	/**
	 * Test 2
	 * 
	 * External user invites the internal user as collborator. Check the document count for internal collaborator owned by external user.
	 * Also verify exposure count decremented by 1 after removing the collaboration.
	 * @throws Exception
	 */
	 
	@Test(groups={"EXTERNAL_USER_DASHBOARD", "EXTERNAL", "REGRESSION"})
	public void verifyDocumentCollaboratorsOwnedByExternalUser() throws Exception {
		BoxFolder folder1 = null;
		String internalUser = "box-admin@securletbeatle.com";
		String uniqueId = UUID.randomUUID().toString();
		String sourceFile = "Hello.java";
		String destinationFile = uniqueId+"_Hello.java";
		
		try {

			Logger.info( "External user "+ this.externalUserAccount.getUsername() + " collaborating with internal user");

			//Get the internal collaborator for externally owned files
			long countInternal = 0;
			
			BoxDocument collaboratedDocuments = getDocumentCollaborators("false", facility.Box.name(), elapp.el_box.name());
			
			//Get the internal collaborator for externally owned files
			countInternal = collaboratedDocuments.getObjects().get(0).getDocsExposed();
			
			Reporter.log("Internal count:"+countInternal, true);
			
			//create a temporary folder
			folder1  = externalUniversalApi.createFolder(uniqueId + "_CollabWithInternalUser");
			FileUploadResponse uploadedFile = externalUniversalApi.uploadFile(folder1.getId(), sourceFile,  destinationFile);
			
			Thread.sleep(CommonConstants.THIRTY_SECONDS_SLEEP);
			
			SharedLink sharedLink =  new SharedLink();
			sharedLink.setAccess("collaborators");
			
			folder1 = externalUniversalApi.createSharedLinkForFolder(folder1.getId(), sharedLink);
			
			//External user collaborate the user1 as editor 
			BoxCollaboration bcollab = createCollaboration(folder1.getId(), "folder", internalUser, "editor", true);
			Thread.sleep(CommonConstants.THREE_MINUTES_SLEEP);
			
			universalApi.downloadFile(uploadedFile.getFileId(), uploadedFile.getFileName());
			
			//Check the count 
			BoxDocument collaboratedDocumentsAfter = getDocumentCollaborators("false", facility.Box.name(), elapp.el_box.name());
			CustomAssertion.assertEquals(collaboratedDocumentsAfter.getObjects().get(0).getDocsExposed(), (countInternal+1), "Internally collaborated Document count not incremented");
			//check the internal user email
			CustomAssertion.assertEquals(collaboratedDocumentsAfter.getObjects().get(0).getEmail(), internalUser, "Internal user email is not correct");
			
			//check the internal user
			CustomAssertion.assertTrue(collaboratedDocumentsAfter.getObjects().get(0).getIsInternal().booleanValue(), "Internal user should be true");
			
			//Delete the collaboration
			externalUniversalApi.deleteCollaboration(bcollab);
			
			//externalUniversalApi.deleteFolder(folder1.getId(), true, folder1.getEtag());
			Thread.sleep(CommonConstants.THREE_MINUTES_SLEEP);
			
			//Check the count 
			collaboratedDocuments = getDocumentCollaborators("false", facility.Box.name(), elapp.el_box.name());
			CustomAssertion.assertEquals(collaboratedDocuments.getObjects().get(0).getDocsExposed(), countInternal, 
																	"Internally collaborated Document count not decremented once the file deleted");
			
		}
				
		finally{
			externalUniversalApi.deleteFolder(folder1.getId(), true, folder1.getEtag());
		}
	}
	
	
	
	
	
	/**
	 * Test 3
	 * 
	 * Get all the exposed files after exposing the document
	 * @throws Exception
	 */
	
	@Test 
	public void getExternallyExposedDocuments() throws Exception {
		
		BoxFolder folder1 = null;
		String internalUser = "box-admin@securletbeatle.com";
		String uniqueId = UUID.randomUUID().toString();
		String sourceFile = "Hello.java";
		String destinationFile = uniqueId+"_Hello.java";
		
		try {

			//Get the internal collaborator for externally owned files
			long countBefore = 0;
			
			BoxDocument documents = getBoxDocuments("false", facility.Box.name(), null);
			
			//get the count of exposed documents by external user
			countBefore = documents.getMeta().getTotalCount();
			
			Reporter.log("countBefore:"+countBefore, true);
			
			//create a temporary folder
			folder1  = externalUniversalApi.createFolder(uniqueId + "_CollabWithInternalUser");
			FileUploadResponse uploadedFile = externalUniversalApi.uploadFile(folder1.getId(), sourceFile,  destinationFile);
			
			Thread.sleep(CommonConstants.THIRTY_SECONDS_SLEEP);
			
			SharedLink sharedLink =  new SharedLink();
			sharedLink.setAccess("collaborators");
			
			folder1 = externalUniversalApi.createSharedLinkForFolder(folder1.getId(), sharedLink);
			
			//External user collaborate the user1 as editor 
			BoxCollaboration bcollab = createCollaboration(folder1.getId(), "folder", internalUser, "editor", true);
			Thread.sleep(CommonConstants.THREE_MINUTES_SLEEP);
			
			
			//Check the count after exposure
			BoxDocument documentsAfterExpsoure = getBoxDocuments("false", facility.Box.name(), null);
			CustomAssertion.assertEquals(documentsAfterExpsoure.getMeta().getTotalCount(), (countBefore+1), "Exposed Document count not incremented");
			
			//#####
			//****TO do verify all the fields for exposed doc ****
			CustomAssertion.assertTrue(MarshallingUtils.marshall(documentsAfterExpsoure).contains(folder1.getId()), "Exposed Document not returned in the response");
			
			//Delete the collaboration
			externalUniversalApi.deleteCollaboration(bcollab);
			
			//externalUniversalApi.deleteFolder(folder1.getId(), true, folder1.getEtag());
			Thread.sleep(CommonConstants.THREE_MINUTES_SLEEP);
			
			//Check the count 
			documents = getBoxDocuments("false", facility.Box.name(), null);
			CustomAssertion.assertEquals(documents.getMeta().getTotalCount(), countBefore, "Deleted document still returned in the exposed document list");
			
		}
				
		finally{
			externalUniversalApi.deleteFolder(folder1.getId(), true, folder1.getEtag());
		}
		
		
		
		
		//int countBedocuments.getMeta().getTotalCount()
		
		
		
	}
	
	
	
	/**
	 * Test 4
	 * 
	 * Get all the exposed files 
	 * @throws Exception
	 */
	
	@Test 
	public void getExternallyExposedDocumentsByUser() throws Exception {
		
		HashMap<String, String> additionalParams = new HashMap<String, String>();
		additionalParams.put("search", externalUserAccount.getUsername());
		
		BoxDocument documents = getBoxDocuments("false", facility.Box.name(),additionalParams);
		
		
		
		
		
	}
	
	

	/**
	 * External User creates the files and shared it publically. 
	 * Internal User downloads the file
	 * @throws Exception
	 */
	@Test
	public void externalUserCreateFileAndSharedPublicInternalUserDownload() throws Exception {
		BoxFolder folder1 = null;
		String internalUser = "box-admin@securletbeatle.com";
		String uniqueId = UUID.randomUUID().toString();
		String sourceFile = "Hello.java";
		String destinationFile = uniqueId+"_Hello.java";

		try {
			
			//create a temporary folder
			folder1  = externalUniversalApi.createFolder(uniqueId + "_ExternalUserSharing");
			FileUploadResponse uploadedFile = externalUniversalApi.uploadFile(folder1.getId(), sourceFile,  destinationFile);
			
			Thread.sleep(CommonConstants.THIRTY_SECONDS_SLEEP);
			
			FileEntry sharedFile = externalUniversalApi.createSharedLinkForFolder(folder1.getId());
			
			
			
			
			//As internal user download the file
			universalApi.downloadFile(sharedFile.getId(), destinationFile );
			System.out.println("Shared URL:" + sharedFile.getSharedLink().getDownloadUrl());
		}
		
		finally {
			
		}
	}
	
	

	
	
	@Test
	public void createFolderAndShareAsExternalUser() throws Exception {
		BoxFolder folder1 = null;
		String internalUser = "box-admin@securletbeatle.com";
		String uniqueId = UUID.randomUUID().toString();
		String sourceFile = "Hello.java";
		String destinationFile = uniqueId+"_Hello.java";

		try {
			
			//create a temporary folder
			folder1  = externalUniversalApi.createFolder(uniqueId + "_ExternalUserSharing");
			externalUniversalApi.uploadFile(folder1.getId(), sourceFile,  destinationFile);

			Thread.sleep(CommonConstants.THIRTY_SECONDS_SLEEP);
			
			//External user collaborate the user1 as editor 
			createCollaboration(folder1.getId(), "folder", internalUser, "editor", true);
			Thread.sleep(CommonConstants.THREE_MINUTES_SLEEP);
		}
		
		finally {
			
		}
	}
	
	
	
	@Test
	public void fileOwnershipTransferToAnotherUser() throws Exception {

		BoxFolder folder1 = null;
		BoxUserInfo firstOwner = null;
		String uniqueId = UUID.randomUUID().toString();
		String sourceFile = "Hello.java";
		String destinationFile = uniqueId+"_Hello.java";

		try {
			//create the user
			firstOwner = this.createUser("box-admin+3@securletbeatle.com", "Securlet Enduser", "user");
			String futureOwnerLogin = "box-admin+1@securletbeatle.com";

			//create a temporary folder
			folder1  = universalApi.createFolder(uniqueId + "_OwnershipTransferTest");
			universalApi.uploadFile(folder1.getId(), sourceFile,  destinationFile);

			Thread.sleep(CommonConstants.THIRTY_SECONDS_SLEEP);
			
			//Collaborate the user1 as editor
			createCollaboration(folder1.getId(), "folder", firstOwner.getLogin(), "editor", false);
			
			Thread.sleep(CommonConstants.THREE_MINUTES_SLEEP);
			
			//Change the editor into owner of this file
			BoxRemediation remedObject = getRemediationObjectForCollaborators(firstOwner.getId(), "folder", folder1.getId(), 
																				firstOwner.getLogin(), "COLLAB_UPDATE", "owner");
			
			remediateExposureWithAPI(remedObject);
			Thread.sleep(CommonConstants.TWO_MINUTES_SLEEP);
			
			String futureOwnerId = "";
			
			UserCollection users = universalApi.listUser();
			for(BoxUserInfo user : users.getEntries()) {
				if (user.getLogin().equals(futureOwnerLogin)) {
					futureOwnerId = user.getId();
					futureOwnerLogin = user.getLogin();
					break;
				}
			}
			
			//Only root folder tranfer is allowed by box. so passing 0
			BoxFolder transferredFolder = universalApi.transferOwner("0", firstOwner.getId(), futureOwnerId);
			CustomAssertion.assertEquals(transferredFolder.getOwnedBy().getLogin(), futureOwnerLogin, "Ownership not transferred");
			Thread.sleep(CommonConstants.TWO_MINUTES_SLEEP);
			
			//delete the user
			universalApi.deleteUser(firstOwner);
			Thread.sleep(CommonConstants.TWO_MINUTES_SLEEP);
			
			//list the users and check deleted user removed from cloudsoc or not
			String allUsers = listUsers();
			
			//Bug #### 27109
			//CustomAssertion.assertTrue(allUsers.contains(firstOwner.getLogin()), "Deleted user not removed from CloudSOC");
			
		}

		finally {
			folder1 = universalApi.getFolderInfo(folder1.getId());
			//Delete the folder
			universalApi.deleteFolder(folder1.getId(), true, folder1.getEtag());
			
			//Delete the user created
			universalApi.deleteUser(firstOwner);
		}
	}
	
	
	@Test
	public void fileOwnershipTransferToAnotherUserAndCheckSharingIsIntact() throws Exception {

		BoxFolder folder1 = null;
		BoxUserInfo firstOwner = null;
		String uniqueId = UUID.randomUUID().toString();
		String sourceFile = "Hello.java";
		String destinationFile = uniqueId+"_Hello.java";

		try {
			//create the user
			firstOwner = this.createUser("box-admin+3@securletbeatle.com", "Securlet Enduser", "user");
			String futureOwnerLogin = "box-admin+1@securletbeatle.com";

			//create a temporary folder
			folder1  = universalApi.createFolder(uniqueId + "_OwnershipTransferTest");
			universalApi.uploadFile(folder1.getId(), sourceFile,  destinationFile);

			Thread.sleep(CommonConstants.THIRTY_SECONDS_SLEEP);
			
			
			//Prepare the share link
			SharedLink sharedLink = new SharedLink();
			sharedLink.setAccess("company");
			universalApi.createSharedLinkForFolder(folder1.getId(), sharedLink);
			
			//Collaborate the user1 as editor
			createCollaboration(folder1.getId(), "folder", firstOwner.getLogin(), "editor", false);
			
			Thread.sleep(CommonConstants.THREE_MINUTES_SLEEP);
			
			//Change the editor into owner of this file
			BoxRemediation remedObject = getRemediationObjectForCollaborators(firstOwner.getId(), "folder", folder1.getId(), 
																				firstOwner.getLogin(), "COLLAB_UPDATE", "owner");
			
			remediateExposureWithAPI(remedObject);
			Thread.sleep(CommonConstants.TWO_MINUTES_SLEEP);
			
			String futureOwnerId = "";
			
			UserCollection users = universalApi.listUser();
			for(BoxUserInfo user : users.getEntries()) {
				if (user.getLogin().equals(futureOwnerLogin)) {
					futureOwnerId = user.getId();
					futureOwnerLogin = user.getLogin();
					break;
				}
			}
			
			//Only root folder tranfer is allowed by box. so passing 0
			BoxFolder transferredFolder = universalApi.transferOwner("0", firstOwner.getId(), futureOwnerId);
			CustomAssertion.assertEquals(transferredFolder.getOwnedBy().getLogin(), futureOwnerLogin, "Ownership not transferred");
			Thread.sleep(CommonConstants.TWO_MINUTES_SLEEP);
			
			//delete the user
			universalApi.deleteUser(firstOwner);
			Thread.sleep(CommonConstants.TWO_MINUTES_SLEEP);
			
			folder1 = universalApi.getFolderInfo(folder1.getId());
			
			//list the users and check deleted user removed from cloudsoc or not
			String allUsers = listUsers();
			
			//Bug #### 27109
			//CustomAssertion.assertTrue(allUsers.contains(firstOwner.getLogin()), "Deleted user not removed from CloudSOC");
			
		}

		finally {
			folder1 = universalApi.getFolderInfo(folder1.getId());
			//Delete the folder
			universalApi.deleteFolder(folder1.getId(), true, folder1.getEtag());
			
			//Delete the user created
			universalApi.deleteUser(firstOwner);
		}
	}
	
	
	
	public String listUsers() throws Exception {

		//	https://eoe.elastica-inc.com/admin/user/ng/list/0?limit=30&offset=0&order_by=first_name&query=
		List<NameValuePair> headers = getHeaders();
		headers.add(new BasicNameValuePair(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON));
		
		List<NameValuePair> queryparams = new ArrayList<NameValuePair>();
		queryparams.add(new BasicNameValuePair("limit", "10"));
		queryparams.add(new BasicNameValuePair("offset", "0"));
		queryparams.add(new BasicNameValuePair("order_by", "first_name"));
		
		String path = "/admin/user/ng/list/0";

		URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(), path, queryparams);
		HttpResponse response =  restClient.doGet(uri, headers);
		String responseBody = ClientUtil.getResponseBody(response);

		Reporter.log("Response body:"+ responseBody, true);
		Reporter.log("Response code:"+ response.getStatusLine().getReasonPhrase(), true);
		return responseBody;
	}
	
	
	
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
	
	
	
	public BoxUserInfo createUser(String login, String name, String role) throws Exception {

		userInfo = new BoxUserInfo();
		userInfo.setLogin(login);
		userInfo.setName(name);
		userInfo.setTrackingCodes(null);
		//userInfo.setRole(role);
		//Create the user
		Reporter.log("Input user info:"+MarshallingUtils.marshall(userInfo), true);
		BoxUserInfo createdUser = universalApi.createUser(userInfo);
		Reporter.log("Created user info:"+MarshallingUtils.marshall(createdUser), true);
		return createdUser;
	}
	
	
	public BoxCollaboration createCollaboration(String id, String type, String login, String role, boolean asExternalUser) throws Exception {
		//Transfer owner
		//Make the first user as owner of the folder
		//create collaboration
		CollaborationInput collabInput2 = new CollaborationInput();
		Item item2 = new Item();
		item2.setId(id);
		item2.setType(type);

		AccessibleBy aby2 = new AccessibleBy();
		aby2.setName(uniqueId);
		aby2.setType("user");
		aby2.setLogin(login);

		collabInput2.setItem(item2);
		collabInput2.setAccessibleBy(aby2);
		collabInput2.setRole(role);
		BoxCollaboration collab = null;
		
		if (asExternalUser) {
			collab = externalUniversalApi.createCollaboration(collabInput2);
		} else {
			collab = universalApi.createCollaboration(collabInput2);
		}
		return collab;
	}
	
	private BoxRemediation getRemediationObjectForCollaborators(String userId, String docType, String docId, String collaborator, 
			String remedialAction, String remedialRole) {
		BoxRemediation boxRemediation = new BoxRemediation();

		boxRemediation.setDbName(suiteData.getTenantName());
		boxRemediation.setUser(suiteData.getSaasAppUsername());
		boxRemediation.setUserId(userId);
		boxRemediation.setDocType(docType);
		boxRemediation.setDocId(docId);

		List<String> possibleValues = new ArrayList<String>();
		if(remedialAction.equals("COLLAB_UPDATE")) {
			possibleValues.add("editor"); possibleValues.add("viewer"); possibleValues.add("previewer"); 
			possibleValues.add("co-owner"); possibleValues.add("owner");
			possibleValues.add("uploader"); possibleValues.add("previewer uploader"); possibleValues.add("viewer uploader");  
		}

		//Meta Info
		ArrayList<String> collabs = new ArrayList<String>();
		collabs.add(collaborator);

		BoxMetaInfo boxMetaInfo = new BoxMetaInfo();
		if(remedialAction.equals("COLLAB_UPDATE")) {
			boxMetaInfo.setRole(remedialRole);
			boxMetaInfo.setCollabs(collabs);
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

}