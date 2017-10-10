package com.elastica.beatle.tests.securlets;

import java.io.FileInputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.testng.ITestContext;
import org.testng.Reporter;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

import com.elastica.beatle.CommonTest;
import com.elastica.beatle.DateUtils;
import com.elastica.beatle.MarshallingUtils;
import com.elastica.beatle.RestClient.ClientUtil;
import com.elastica.beatle.ciq.dto.ContentChecks;
import com.elastica.beatle.ciq.dto.ESResults;
import com.elastica.beatle.logger.Logger;
import com.elastica.beatle.securlets.BoxDataProvider;
import com.elastica.beatle.securlets.CIQValidator;
import com.elastica.beatle.securlets.ESQueryBuilder;
import com.elastica.beatle.securlets.LogUtils;
import com.elastica.beatle.securlets.LogValidator;
import com.elastica.beatle.securlets.SecurletUtils;
import com.elastica.beatle.securlets.SecurletsConstants;
import com.elastica.beatle.securlets.SecurletUtils.elapp;
import com.elastica.beatle.securlets.SecurletUtils.facility;
import com.elastica.beatle.securlets.dto.BoxAction;
import com.elastica.beatle.securlets.dto.BoxDocument;
import com.elastica.beatle.securlets.dto.BoxMetaInfo;
import com.elastica.beatle.securlets.dto.BoxRemediation;
import com.elastica.beatle.securlets.dto.ForensicSearchResults;
import com.elastica.beatle.securlets.dto.Hit;
import com.elastica.beatle.securlets.dto.SecurletDocument;
import com.elastica.beatle.securlets.dto.Source;
import com.gargoylesoftware.htmlunit.util.UrlUtils;
import com.universal.constants.CommonConstants;
import com.universal.dtos.box.AccessibleBy;
import com.universal.dtos.box.BoxCollaboration;
import com.universal.dtos.box.BoxFolder;
import com.universal.dtos.box.BoxUserInfo;
import com.universal.dtos.box.CEntry;
import com.universal.dtos.box.CollaborationInput;
import com.universal.dtos.box.Collaborations;
import com.universal.dtos.box.FileEntry;
import com.universal.dtos.box.FileUploadResponse;
import com.universal.dtos.box.Item;
import com.universal.dtos.box.SharedLink;



public class BoxSecurletExposureTests extends SecurletUtils {
	ESQueryBuilder esQueryBuilder = null;
	//SecurletUtils securletUtils;
	
	LogValidator logValidator;
	CIQValidator ciqValidator;
	private String destinationFile;
	private String query;
	ArrayList<String> messages = new ArrayList<String>();

	String resourceId;
	BoxUserInfo userInfo;
	String instanceId;

	protected enum facility {Box, Office365};
	protected enum SaasApp {ONEDRIVE_BUSINESS, BOX}; 
	protected enum Remediation {UNSHARE, SHARE_ACCESS, SHARE_EXPIRE};
	protected enum Server {UIServer, APIServer};

	String shareExpiry ;
	//Variables to hold internal collaborator change
	protected String folderId;
	protected BoxFolder folderObj;
	protected BoxCollaboration collaboration;
	protected String collaborationId;
	

	public BoxSecurletExposureTests() throws Exception {
		esQueryBuilder = new ESQueryBuilder();
		//System.out.println("Username:"+ suiteData.getSaasAppUsername());
		
		logValidator = new LogValidator();
		shareExpiry = DateUtils.getDaysFromCurrentTime(1);
		
		
		
		//securletUtils = new SecurletUtils(); 
	}
	
	
	@BeforeTest(alwaysRun=true)
	public void initInstance() throws Exception {
		this.instanceId = this.getTenantInstanceId(facility.Box.name());
		if (suiteData.getEnvironmentName().toLowerCase().contains("prod")) {
			this.instanceId = "";
		}
		if (suiteData.getEnvironmentName().toLowerCase().contains("cep")) {
			this.instanceId = null;
		}
		Reporter.log("Instance Name:"+instanceId, true);
	}

	/**
	 * 
	 * Upload a file in Saas app and share it publicly or company wide. 
	 * After that with remdial api (UI call/API call), apply the remedial actions and check file exposure changed or not.
	 * 
	 * @param violationType
	 * @param isInternal
	 * @param exposuretype
	 * @param remedialAction
	 * @param service
	 * @throws Exception
	 */
	@Test(dataProviderClass = BoxDataProvider.class, dataProvider = "sharedLinkDataProvider")
	public void verifyPolicyRemediationForSourceCodeExposure(String violationType, String isInternal, String exposuretype, String remedialAction, 
			String remedy, String expectedResult, String service) throws Exception {
		Logger.info( "Starting verifyUIPolicyRemediationForSourceCodeExposure ...");

		//Prepare the remediation object
		String uniqueId = UUID.randomUUID().toString();
		String sourceFile = "Hello.java";
		String destinationFile = uniqueId+"_Hello.java";

		//Prepare the violations
		ArrayList<String> violations = new ArrayList<String>();
		violations.add(violationType);

		//Get the exposure count
		BoxDocument boxDocument = getExposures(isInternal, suiteData.getUsername(), violations);
		int beforeCount = boxDocument.getMeta().getTotalCount();

		Reporter.log("######" + violationType + " exposure count before test::"+beforeCount, true);

		//Upload a source code file to box root folder
		FileUploadResponse uploadResponse = universalApi.uploadFile("/", sourceFile, destinationFile);
		Reporter.log(uploadResponse.getFileName() + " " + uploadResponse.getResponseMessage());

		//Prepare the share link
		FileEntry sharedFile = null;
		SharedLink sharedLink = new SharedLink();
		sharedLink.setAccess(exposuretype);
		sharedFile = universalApi.createSharedLink(uploadResponse.getFileId(), sharedLink); 
		Reporter.log("Shared file access:" + sharedFile.getSharedLink().getAccess(), true);
		Reporter.log("Shared file effective access:" + sharedFile.getSharedLink().getEffectiveAccess(), true);

		//Check the access has been applied to the file on box
		assertEquals(sharedFile.getSharedLink().getAccess(), exposuretype, "File is not exposed in Box");
		assertEquals(sharedFile.getSharedLink().getEffectiveAccess(), exposuretype, "File is not exposed in Box");

		//wait for a minute for the exposure to be applied
		Thread.sleep(CommonConstants.TWO_MINUTES_SLEEP);

		//Get the exposure count
		boxDocument = getExposures(isInternal, suiteData.getUsername(), violations);
		int countAfterExposure = boxDocument.getMeta().getTotalCount();
		Reporter.log("######" + violationType + " exposure count after the exposure::"+countAfterExposure, true);

		//Check the forensic logs
		//ArrayList<String> logs = gatherForensicLogMessages(uniqueId, facility.Box.name());

		//Now apply the remedial action thro' UI server or API server call
		BoxRemediation boxRemediation = getRemediationObject(sharedFile.getOwnedBy().getId(), sharedFile.getType(), sharedFile.getId(), 
				remedialAction, sharedFile.getSharedLink().getAccess(), remedy);

		if(service.equals(Server.UIServer.name())) {
			remediateExposure(suiteData.getTenantName(), facility.Box.name(), suiteData.getUsername(), sharedFile.getId(), sharedFile.getOwnedBy().getId(), remedialAction);

		} else if(service.equals(Server.APIServer.name())) {
			remediateExposureWithAPI(boxRemediation);
		} 

		//Wait for remedial action
		Reporter.log("###### Waiting for the remedial action ...going to sleep "+CommonConstants.SIXTY_SECONDS_SLEEP + " ms", true);
		Thread.sleep(CommonConstants.THREE_MINUTES_SLEEP);

		//Get the file info from box
		sharedFile = universalApi.getFileInfo(sharedFile.getId());

		//Verify the remediation thro' box API
		if (remedialAction.equals(Remediation.UNSHARE.name())) {
			assertNull(sharedFile.getSharedLink(), "Remediation "+ remedialAction + "didn't work it seems...");

		} else if(remedialAction.equals(Remediation.SHARE_ACCESS.name())) {
			assertEquals(sharedFile.getSharedLink().getAccess(), expectedResult, "Remediation "+ remedialAction + "didn't work it seems...");

		} else if(remedialAction.equals(Remediation.SHARE_EXPIRE.name())) {

			String unsharedAt = (String) sharedFile.getSharedLink().getUnsharedAt();
			DateTime dt = new DateTime(unsharedAt, DateTimeZone.UTC);
			DateTimeFormatter fmt = ISODateTimeFormat.dateTime();
			unsharedAt = fmt.print(dt);
			Reporter.log("Unshared At:"+ unsharedAt, true);
			
			Reporter.log("Actual:"+sharedFile.getSharedLink().getUnsharedAt() +",Expected:"+DateUtils.getDateFromCurrentTime(Integer.parseInt(expectedResult)), true);
			assertTrue(unsharedAt.startsWith(DateUtils.getDateFromCurrentTime(Integer.parseInt(expectedResult))), "Expected expiration date don't match");

		}
		
		boxDocument = getExposures("true", suiteData.getUsername(), violations);
		int countAfterRemediation = boxDocument.getMeta().getTotalCount();
		Reporter.log("######" + violationType + " exposure count after remediation::"+countAfterRemediation, true);
		//assertEquals(countAfterRemediation, beforeCount, "Policy not applied for Box source code violation");

		//Verify the activity logs
		//logValidator.verifyUploadShareUnshareLogs(logs, destinationFile,  violationType);

		//cleanup file if everything goes well
		universalApi.deleteFile(sharedFile.getId(), sharedFile.getEtag());
	}



	/**
	 * 
	 * Create a folder and Upload a file in Saas app and share it publicly or company wide. 
	 * After that with remdial api (UI call/API call), apply the remedial actions and check folder exposure changed or not.
	 * 
	 * @param violationType
	 * @param isInternal
	 * @param exposuretype
	 * @param remedialAction
	 * @param service
	 * @throws Exception
	 */
	@Test(dataProviderClass = BoxDataProvider.class, dataProvider = "sharedLinkDataProvider")
	public void verifyPolicyRemediationForFolderExposure(String violationType, String isInternal, String exposuretype, String remedialAction, 
			String remedy, String expectedResult, String service) throws Exception {
		Logger.info( "Starting verifyPolicyRemediationForFolderExposure ...");

		//Prepare the violations
		ArrayList<String> violations = new ArrayList<String>();
		violations.add(violationType);

		//Get the exposure count
		BoxDocument boxDocument = getExposures(isInternal, suiteData.getUsername(), violations);
		int beforeCount = boxDocument.getMeta().getTotalCount();

		Reporter.log("######" + violationType + " exposure count before test::"+beforeCount, true);
		
		String uniqueId = UUID.randomUUID().toString();
		String sourceFile = "Hello.java";
		String destinationFile = uniqueId+"_Hello.java";

		//create folder1
		BoxFolder folderObj = universalApi.createFolder(uniqueId);
		String folderId = folderObj.getId();

		//Perform folder operations
		//upload the file to folder1
		FileUploadResponse uploadResponse = universalApi.uploadFile(folderId, sourceFile, destinationFile);
		String fileId = uploadResponse.getFileId();

		Thread.sleep(CommonConstants.THREE_MINUTES_SLEEP);

		//Prepare the share link
		SharedLink sharedLink = new SharedLink();
		sharedLink.setAccess(exposuretype);

		BoxFolder sharedFolder = universalApi.createSharedLinkForFolder(folderObj.getId(), sharedLink); 
		Reporter.log("Shared folder access:" + sharedFolder.getSharedLink().getAccess(), true);
		Reporter.log("Shared folder effective access:" + sharedFolder.getSharedLink().getEffectiveAccess(), true);

		//Check the access has been applied to the folder on box
		assertEquals(sharedFolder.getSharedLink().getAccess(), exposuretype, "Folder is not exposed in Box");
		assertEquals(sharedFolder.getSharedLink().getEffectiveAccess(), exposuretype, "Folder is not exposed in Box");

		//wait for a minute for the exposure to be applied
		Thread.sleep(CommonConstants.THREE_MINUTES_SLEEP);

		//Get the exposure count
		boxDocument = getExposures(isInternal, suiteData.getUsername(), violations);
		int countAfterExposure = boxDocument.getMeta().getTotalCount();
		Reporter.log("######" + violationType + " exposure count after the exposure::"+countAfterExposure, true);

		//Check the forensic logs
		//ArrayList<String> logs = gatherForensicLogMessages(uniqueId, facility.Box.name());

		//Now apply the remedial action thro' UI server or API server call
		BoxRemediation boxRemediation = getRemediationObject(sharedFolder.getOwnedBy().getId(), sharedFolder.getType(), sharedFolder.getId(), 
				remedialAction, sharedFolder.getSharedLink().getAccess(), remedy);

		if(service.equals(Server.UIServer.name())) {
			remediateExposure(suiteData.getTenantName(), facility.Box.name(), suiteData.getUsername(), sharedFolder.getId(), sharedFolder.getOwnedBy().getId(), remedialAction);

		} else if(service.equals(Server.APIServer.name())) {
			remediateExposureWithAPI(boxRemediation);
		} 

		//Wait for remedial action
		Reporter.log("###### Waiting for the remedial action ...going to sleep "+CommonConstants.SIXTY_SECONDS_SLEEP + " ms", true);
		Thread.sleep(CommonConstants.THREE_MINUTES_SLEEP);

		//Get the file info from box
		sharedFolder = universalApi.getFolderInfo(sharedFolder.getId());

		//Verify the remediation thro' box API
		if (remedialAction.equals(Remediation.UNSHARE.name())) {
			assertNull(sharedFolder.getSharedLink(), "Remediation "+ remedialAction + "didn't work it seems...");

		} else if(remedialAction.equals(Remediation.SHARE_ACCESS.name())) {
			assertEquals(sharedFolder.getSharedLink().getAccess(), expectedResult, "Remediation "+ remedialAction + "didn't work it seems...");

		} else if(remedialAction.equals(Remediation.SHARE_EXPIRE.name())) {
			//this.getDateFromCurrentTime(Integer.parseInt(expectedResult));
			String unsharedAt = (String) sharedFolder.getSharedLink().getUnsharedAt();
			Reporter.log("Actual:"+sharedFolder.getSharedLink().getUnsharedAt() +",Expected:"+DateUtils.getDateFromCurrentTime(Integer.parseInt(expectedResult)), true);
			
			DateTime dt = new DateTime(unsharedAt, DateTimeZone.UTC);
			DateTimeFormatter fmt = ISODateTimeFormat.dateTime();
			unsharedAt = fmt.print(dt);
			Reporter.log("Unshared At:"+ unsharedAt, true);
			
			assertTrue(unsharedAt.startsWith(DateUtils.getDateFromCurrentTime(Integer.parseInt(expectedResult))), "Expected expiration date don't match");
			
		}

		//gather the forensic logs again 
		//logs = gatherForensicLogMessages(uniqueId, facility.Box.name());

		boxDocument = getExposures("true", suiteData.getUsername(), violations);
		int countAfterRemediation = boxDocument.getMeta().getTotalCount();
		Reporter.log("######" + violationType + " exposure count after remediation::"+countAfterRemediation, true);
		//assertEquals(countAfterRemediation, beforeCount, "Policy not applied for Box source code violation");

		//Verify the activity logs
		//logValidator.verifyUploadShareUnshareLogs(logs, destinationFile,  violationType);

		//cleanup file if everything goes well
		universalApi.deleteFolder(folderId, true, sharedFolder.getEtag());
	}	

	
	
	
	
	/**
	 * Test to change the role of internal collaborator
	 * @param testname
	 * @param collaborator
	 * @param currentRole
	 * @param remedialAction
	 * @param remedialRole
	 * @param expectedRole
	 * @param service
	 * @throws Exception
	 */
	
	@Test(dataProviderClass = BoxDataProvider.class, dataProvider = "InternalCollaborationDataProvider")
	public void verifyCollaborateFolderAndChangeInternalCollaboratorRoles(String testname, String collaborator, String currentRole, 
			String remedialAction, String remedialRole, String expectedRole, boolean resetTestData) throws Exception {
		
		//Create the folder and collaboration as a prerequisite
		if(this.collaborationId == null && folderObj == null) {
			Reporter.log("Creating the folder and collaborations...");
			createFolderAndCollaborateWithUser(collaborator, currentRole);
		}
		
		
		Reporter.log("Started the test to "+ testname + " from " + currentRole + " to " + remedialRole );
		
		//Change the collaboration role from one to another thro' remediation api
		BoxRemediation boxRemediation = this.getRemediationObjectForCollaborators(folderObj.getOwnedBy().getId(), folderObj.getType(), folderObj.getId(), 
				collaborator, remedialAction, remedialRole);

		//Apply the remediation
		remediateExposureWithAPI(boxRemediation);

		//Wait for remedial action
		Reporter.log("###### Waiting for the remedial action ...going to sleep "+ CommonConstants.TWO_MINUTES_SLEEP + " ms", true);
		Thread.sleep(CommonConstants.TWO_MINUTES_SLEEP);

		//Get the folder collaborations again
		Collaborations expectedCollaborations = universalApi.getFolderCollaborations(folderId);
		
		CustomAssertion.assertEquals(expectedCollaborations.getEntries().get(0).getRole(), expectedRole, "Remedial Role " + remedialRole +" not set correctly in SaasApp");
		
		if(remedialRole.equals("owner")) {
			CustomAssertion.assertEquals(expectedCollaborations.getEntries().get(0).getAccessibleBy().getLogin(), suiteData.getSaasAppUsername(), "Collaborator " + collaborator +" not set correctly in SaasApp ");
		} else {
			CustomAssertion.assertEquals(expectedCollaborations.getEntries().get(0).getAccessibleBy().getLogin(), collaborator, "Collaborator " + collaborator +" not set correctly in SaasApp ");
		}
		Reporter.log("******************************************************************************************************************************************************************************", true);
		
		//if reset true, clear the variables
		if (resetTestData) {
			
			System.out.println("Entering here for the reset value "+ resetTestData);
			
			//Delete the collaboration
			universalApi.deleteCollaboration(collaboration);
	
			//get the folder object
			folderObj = universalApi.getFolderInfo(folderId);
	
			//Delete the folder
			universalApi.deleteFolder(folderId, true, folderObj.getEtag());
			this.collaborationId = null ;
			this.folderObj = null;
		}
		
	}
	
	
	/**
	 * Test to change the role of external collaborator
	 * @param testname
	 * @param collaborator
	 * @param currentRole
	 * @param remedialAction
	 * @param remedialRole
	 * @param expectedRole
	 * @param service
	 * @throws Exception
	 */
	
	@Test(dataProviderClass = BoxDataProvider.class, dataProvider = "ExternalCollaborationDataProvider")
	public void verifyCollaborateFolderAndChangeExternalCollaboratorRoles(String testname, String collaborator, String currentRole, 
			String remedialAction, String remedialRole, String expectedRole, boolean resetTestData) throws Exception {
		
		//Create the folder and collaboration as a prerequisite
		if(this.collaborationId == null && folderObj == null) {
			Reporter.log("Creating the folder and collaborations...");
			createFolderAndCollaborateWithUser(collaborator, currentRole);
		}
		
		
		Reporter.log("Started the test to "+ testname + " from " + currentRole + " to " + remedialRole );
		
		//Change the collaboration role from one to another thro' remediation api
		BoxRemediation boxRemediation = this.getRemediationObjectForCollaborators(folderObj.getOwnedBy().getId(), folderObj.getType(), folderObj.getId(), 
				collaborator, remedialAction, remedialRole);

		//Apply the remediation
		remediateExposureWithAPI(boxRemediation);

		//Wait for remedial action
		Reporter.log("###### Waiting for the remedial action ...going to sleep "+CommonConstants.THREE_MINUTES_SLEEP + " ms", true);
		Thread.sleep(CommonConstants.THREE_MINUTES_SLEEP);

		//Get the folder collaborations again
		Collaborations expectedCollaborations = universalApi.getFolderCollaborations(folderId);
		
		CustomAssertion.assertEquals(expectedCollaborations.getEntries().get(0).getRole(), expectedRole, "Remedial Role " + remedialRole +" not set correctly in SaasApp");
		
		if(remedialRole.equals("owner")) {
			CustomAssertion.assertEquals(expectedCollaborations.getEntries().get(0).getAccessibleBy().getLogin(), suiteData.getSaasAppUsername(), "Collaborator " + collaborator +" not set correctly in SaasApp ");
		} else {
			CustomAssertion.assertEquals(expectedCollaborations.getEntries().get(0).getAccessibleBy().getLogin(), collaborator, "Collaborator " + collaborator +" not set correctly in SaasApp ");
		}
		Reporter.log("******************************************************************************************************************************************************************************", true);
		
		//if reset true, clear the variables
		if (resetTestData) {
			
			System.out.println("Entering here for the reset value "+ resetTestData);
			
			//Delete the collaboration
			universalApi.deleteCollaboration(collaboration);
	
			//get the folder object
			folderObj = universalApi.getFolderInfo(folderId);
	
			//Delete the folder
			universalApi.deleteFolder(folderId, true, folderObj.getEtag());
			this.collaborationId = null ;
			this.folderObj = null;
		}
		
	}
	
	

	/**
	 * Test to change the collaborator role remediation with our api
	 * 
	 * @param testname
	 * @param collaborator
	 * @param currentRole
	 * @param remedialAction
	 * @param remedialRole
	 * @param service
	 * @throws Exception
	 */

	@Test(dataProviderClass = BoxDataProvider.class, dataProvider = "SharedLinkCollaborationDataProvider")
	public void verifyRemediationOfChangeCollaboratorRolesAndSharedlinkUpdate(String testname, String exposuretype, String sharedRemedialAction, String sharedRemedy,
							String collaborator, String currentRole,  String remedialAction, String collaboratorUpdated, String remedialRole, String expectedRole, String service ) throws Exception {

		String uniqueId = UUID.randomUUID().toString();
		String sourceFile = "Hello.java";
		String destinationFile = uniqueId+"_Hello.java";

		//create folder1
		BoxFolder folderObj = universalApi.createFolder(uniqueId);
		String folderId = folderObj.getId();

		//Perform folder operations
		//upload the file to folder1
		FileUploadResponse uploadResponse = universalApi.uploadFile(folderId, sourceFile, destinationFile);
		String fileId = uploadResponse.getFileId();

		Thread.sleep(CommonConstants.THIRTY_SECONDS_SLEEP);
		
		//Prepare the share link
		SharedLink sharedLink = new SharedLink();
		sharedLink.setAccess(exposuretype);

		BoxFolder sharedFolder = universalApi.createSharedLinkForFolder(folderObj.getId(), sharedLink); 
		Reporter.log("Shared folder access:" + sharedFolder.getSharedLink().getAccess(), true);
		Reporter.log("Shared folder effective access:" + sharedFolder.getSharedLink().getEffectiveAccess(), true);

		//Check the access has been applied to the folder on box
		assertEquals(sharedFolder.getSharedLink().getAccess(), exposuretype, "Folder is not exposed in Box");
		assertEquals(sharedFolder.getSharedLink().getEffectiveAccess(), exposuretype, "Folder is not exposed in Box");
		
		String collaborators[] = collaborator.split(",");

		for (int i=0; i<collaborators.length; i++) {
			//create collaboration object for saas app
			CollaborationInput collabInput = new CollaborationInput();
			Item item = new Item();
			item.setId(folderObj.getId());
			item.setType(folderObj.getType());

			AccessibleBy aby = new AccessibleBy();
			aby.setName(uniqueId + "_" + i);
			aby.setType("user");
			aby.setLogin(collaborators[i]);

			collabInput.setItem(item);
			collabInput.setAccessibleBy(aby);
			collabInput.setRole(currentRole);

			//Create the collaboration
			BoxCollaboration collaboration = universalApi.createCollaboration(collabInput);
			System.out.println("Collaboration Id:" + collaboration.getId());
		}

		Thread.sleep(CommonConstants.THIRTY_SECONDS_SLEEP);

		//get the collaborations and assert them
		Collaborations collaborations = universalApi.getFolderCollaborations(folderId);
		for(BoxCollaboration collaborationEntry : collaborations.getEntries() ) {
			if (collaborationEntry.getAccessibleBy().getLogin().equals(collaboratorUpdated)) {
				assertEquals(collaborationEntry.getRole(), currentRole, "Current Role " + currentRole +" not set correctly in SaasApp");
			}
		}
		
		assertTrue(collaborations.getEntries().get(0).getRole().equals(currentRole), "Role " + currentRole +" not set correctly in SaasApp ");
		
		//Change the collaboration role from one to another thro' remediation api
		BoxRemediation boxRemediation = this.getRemediationObjectForSharedLinkCollaborators(folderObj.getOwnedBy().getId(), 
																				folderObj.getType(), folderObj.getId(), 
																				collaboratorUpdated,  sharedRemedialAction, sharedRemedy, exposuretype,
																				remedialAction, remedialRole);
		System.out.println("Remediation object:"+ MarshallingUtils.marshall(boxRemediation));
		
		
		//Apply the remediation
		remediateExposureWithAPI(boxRemediation);

		//Wait for remedial action
		Reporter.log("###### Waiting for the remedial action ...going to sleep "+CommonConstants.THREE_MINUTES_SLEEP + " ms", true);
		Thread.sleep(CommonConstants.THREE_MINUTES_SLEEP);
		
		//Get the folder collaborations again
		Collaborations expectedCollaborations = universalApi.getFolderCollaborations(folderId);
		
		for(BoxCollaboration collaborationEntry : expectedCollaborations.getEntries() ) {
			if (collaborationEntry.getAccessibleBy().getLogin().equals(collaboratorUpdated)) {
				assertEquals(collaborationEntry.getRole(), expectedRole, "Remedial Role " + remedialRole +" not set correctly in SaasApp");
			}
		}
		
		//get the folder object
		folderObj = universalApi.getFolderInfo(folderId);

		//Delete the folder
		universalApi.deleteFolder(folderId, true, folderObj.getEtag());

	}

	
	
	
	
	
	public void createFolderAndCollaborateWithUser(String collaborator, String currentRole) throws Exception {
		
		String uniqueId = UUID.randomUUID().toString();
		String sourceFile = "Hello.java";
		String destinationFile = uniqueId+"_Hello.java";

		//create folder1
		folderObj = universalApi.createFolder(uniqueId);
		folderId = folderObj.getId();

		//Perform folder operations
		//upload the file to folder1
		FileUploadResponse uploadResponse = universalApi.uploadFile(folderId, sourceFile, destinationFile);
		String fileId = uploadResponse.getFileId();

		Thread.sleep(CommonConstants.THIRTY_SECONDS_SLEEP);

		//create collaboration object for saas app
		CollaborationInput collabInput = new CollaborationInput();
		Item item = new Item();
		item.setId(folderObj.getId());
		item.setType(folderObj.getType());

		AccessibleBy aby = new AccessibleBy();
		aby.setName(uniqueId);
		aby.setType("user");
		aby.setLogin(collaborator);

		collabInput.setItem(item);
		collabInput.setAccessibleBy(aby);
		collabInput.setRole(currentRole);

		//Create the collaboration
		this.collaboration = universalApi.createCollaboration(collabInput);
		this.collaborationId = collaboration.getId();
		Reporter.log("Collaboration Id:" + collaboration.getId(), true);
		
		Reporter.log("Waiting for the collaboration action ...going to sleep "+CommonConstants.THREE_MINUTES_SLEEP + " ms", true);
		//Sleep time of atleast 3 mins is needed as our portal has to get the collaboration event. otherwise test will fail
		sleep(CommonConstants.THREE_MINUTES_SLEEP);
		
		//get the collaborations and assert them
		Collaborations collaborations = universalApi.getFolderCollaborations(folderId);
		
		
		
		CustomAssertion.assertTrue(collaborations.getEntries().get(0).getRole().equals(currentRole), "Role " + currentRole +" set correctly in SaasApp ", "Role " + currentRole +" not set correctly in SaasApp ");
		CustomAssertion.assertTrue(collaborations.getEntries().get(0).getAccessibleBy().getLogin().equals(collaborator),"Login " + collaborator +" set correctly in SaasApp " ,"Login " + collaborator +" not set correctly in SaasApp ");
		
	}
	


	private BoxRemediation getRemediationObject(String userId, String docType, String docId, String remedialAction, String currentLink, String remedy) {
		BoxRemediation boxRemediation = new BoxRemediation();

		boxRemediation.setDbName(suiteData.getTenantName());
		boxRemediation.setUser(suiteData.getSaasAppUsername());
		boxRemediation.setUserId(userId);
		boxRemediation.setDocType(docType);
		boxRemediation.setDocId(docId);
		boxRemediation.setInstance(instanceId);

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


	private BoxRemediation getRemediationObjectForCollaborators(String userId, String docType, String docId, String collaborator, 
			String remedialAction, String remedialRole) {
		BoxRemediation boxRemediation = new BoxRemediation();

		boxRemediation.setDbName(suiteData.getTenantName());
		boxRemediation.setUser(suiteData.getSaasAppUsername());
		boxRemediation.setUserId(userId);
		boxRemediation.setDocType(docType);
		boxRemediation.setDocId(docId);
		boxRemediation.setInstance(instanceId);

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



	private BoxRemediation getRemediationObjectForSharedLinkCollaborators(String userId, String docType, String docId, String collaborator, 
			String sharedRemedialAction, String sharedRemedy, String currentLink, String remedialAction, String remedialRole) {
		BoxRemediation boxRemediation = new BoxRemediation();

		boxRemediation.setDbName(suiteData.getTenantName());
		boxRemediation.setUser(suiteData.getSaasAppUsername());
		boxRemediation.setUserId(userId);
		boxRemediation.setDocType(docType);
		boxRemediation.setDocId(docId);
		boxRemediation.setInstance(instanceId);

		//Sharedlink remediation
		List<String> possibleValuesForSharing = new ArrayList<String>();
		if(sharedRemedialAction.equals("UNSHARE")) {
			possibleValuesForSharing.add("open"); possibleValuesForSharing.add("company"); possibleValuesForSharing.add("collaborators");
		}

		//Meta Info
		BoxMetaInfo boxMetaInfo = new BoxMetaInfo();

		if (sharedRemedy != null) {
			if(sharedRemedialAction.equals("SHARE_EXPIRE")) {
				boxMetaInfo.setCurrentLink(null);
				boxMetaInfo.setCollabs(null);
				boxMetaInfo.setExpireOn(sharedRemedy);
			} else {
				boxMetaInfo.setAccess(sharedRemedy);
			}
		}
		boxMetaInfo.setCollabs(null);
		if(currentLink != null) {
			boxMetaInfo.setCurrentLink(currentLink);
		}
		
		List<BoxAction> actions = new ArrayList<BoxAction>();
		BoxAction boxActionForSharing = new BoxAction();
		boxActionForSharing.setCode(sharedRemedialAction);
		boxActionForSharing.setPossibleValues(possibleValuesForSharing);
		boxActionForSharing.setMetaInfo(boxMetaInfo);
		actions.add(boxActionForSharing);
		
		
		
		
		
		//Collaboration remediation
		List<String> possibleValuesForCollaborations = new ArrayList<String>();
		if(remedialAction.equals("COLLAB_UPDATE")) {
			possibleValuesForCollaborations.add("editor"); possibleValuesForCollaborations.add("viewer");
			possibleValuesForCollaborations.add("previewer"); possibleValuesForCollaborations.add("co-owner"); 
			possibleValuesForCollaborations.add("owner");possibleValuesForCollaborations.add("uploader"); 
			possibleValuesForCollaborations.add("previewer uploader"); possibleValuesForCollaborations.add("viewer uploader");  
		}
		
		
		//Meta Info
		ArrayList<String> collabs = new ArrayList<String>();
		String allCollabs[] = collaborator.split(",");
		for(String collab: allCollabs) {
			collabs.add(collab);
		}

		BoxMetaInfo boxMetaInfoForCollaboration = new BoxMetaInfo();
		if(remedialAction.equals("COLLAB_UPDATE")) {
			boxMetaInfoForCollaboration.setRole(remedialRole);
			boxMetaInfoForCollaboration.setCollabs(collabs);
		}
		
		if(remedialAction.equals("COLLAB_REMOVE")) {
			boxMetaInfoForCollaboration.setCollabs(collabs);
		}
		
		
		//Add the collaboration action
		BoxAction boxActionForCollaboration = new BoxAction();
		boxActionForCollaboration.setCode(remedialAction);
		boxActionForCollaboration.setPossibleValues(possibleValuesForCollaborations);
		boxActionForCollaboration.setMetaInfo(boxMetaInfoForCollaboration);
		actions.add(boxActionForCollaboration);
		
		boxRemediation.setActions(actions);
		return boxRemediation;
	}	
	
	

	
	/*
	 * 
	 */
	@Test(dataProviderClass = BoxDataProvider.class, dataProvider = "fileExposuresProvider")
	public void verifyExposureCountForFileExposures(String filename, String violation, 
											String isInternalExposure, String access) throws Exception {
		
		Logger.info( "verifyExposureCountForFileExposures.."+filename +" violation type:"+violation);
		Logger.info(  isInternalExposure +" access:"+access);
		//
		ArrayList<String> violations = new ArrayList<String>();
		violations.add(violation);
		
		BoxDocument boxDocument = getExposures(isInternalExposure, suiteData.getUsername(), violations);
		int countBefore = boxDocument.getMeta().getTotalCount();

		Reporter.log("##### " + violation + " exposure count before test::"+countBefore, true);
		
		
		//Upload a file and share it
		FileEntry sharedLink = uploadFileAndShareit("/", filename, access);
		
		Thread.sleep(CommonConstants.THREE_MINUTES_SLEEP);
		
		//Check the exposure count after public exposure
		boxDocument = getExposures(isInternalExposure, suiteData.getUsername(), violations);
		int countAfterExposure = boxDocument.getMeta().getTotalCount();
		Reporter.log("##### " + violation + " exposure count after the exposure::"+countAfterExposure, true);
		
		//Delete the file and check exposure count
		FileEntry unsharedFile = universalApi.getFileInfo(sharedLink.getId());
		universalApi.deleteFile(unsharedFile.getId(), unsharedFile.getEtag());
		
		Thread.sleep(CommonConstants.THREE_MINUTES_SLEEP);
		
		boxDocument = getExposures("true", suiteData.getUsername(), violations);
		int countAfter = boxDocument.getMeta().getTotalCount();

		Reporter.log("##### " + violation + " exposure count after deletion of file::"+countAfter, true);
		assertEquals(countAfter, countBefore, "Exposure count mismatch even after file deletion");
		
	}


	public FileEntry uploadFileAndShareit(String folderId, String sourceFile, String access) throws Exception {
		//Upload a file and share it publicly
		String uniqueId = UUID.randomUUID().toString();
		String destinationFile = uniqueId + "_" + sourceFile;
		String destinationFolderId = (folderId == null) ? "/" : folderId;
		
		//Upload a source code file to box root folder
		FileUploadResponse uploadResponse = this.universalApi.uploadFile(destinationFolderId, sourceFile, destinationFile);
		Reporter.log(uploadResponse.getFileName() + " " + uploadResponse.getResponseMessage());

		//Share the file publicly
		SharedLink sharedLink = new SharedLink();
		sharedLink.setAccess(access);
		
		FileEntry sharedFile = universalApi.createSharedLink(uploadResponse.getFileId(), sharedLink); 
		Reporter.log("Shared file access:" + sharedFile.getSharedLink().getAccess(), true);
		Reporter.log("Shared file effective access:" + sharedFile.getSharedLink().getEffectiveAccess(), true);
		
		return sharedFile;
	}
	

	public BoxDocument getExposures(String isInternal, String ownedBy, ArrayList<String> vltypes) throws Exception {
		List<NameValuePair> headers = getHeaders();

		List<NameValuePair> qparams = new ArrayList<NameValuePair>(); 
		qparams.add(new BasicNameValuePair("is_internal", isInternal));
		//qparams.add(new BasicNameValuePair("owned_by",  ownedBy )); //UrlUtils.decode(suiteData.getUsername())));
		qparams.add(new BasicNameValuePair("content_checks.vl_types",  UrlUtils.decode(StringUtils.join(vltypes, ","))));
		//qparams.add(new BasicNameValuePair("content_checks.vk_content_iq_violations",  "test_ciq_profile"));

		String path = suiteData.getAPIMap().get("getBoxDocuments").
				replace("{tenant}", suiteData.getTenantName()).
				replace("{version}", suiteData.getBaseVersion());

		//System.out.println("Path:" + path);
		URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(), path, qparams);
		HttpResponse response =  restClient.doGet(uri, headers);
		String responseBody = ClientUtil.getResponseBody(response);

		Logger.info("Response body:"+ responseBody);
		BoxDocument boxDocument = MarshallingUtils.unmarshall(responseBody, BoxDocument.class);
		return boxDocument;
	}








	public void remediateExposure(String tenant, String facility,  String user, String documentId, String userId, String action) throws Exception {

		List<NameValuePair> headers = getHeaders();
		String payload = "";

		if(action.equals(Remediation.SHARE_EXPIRE.name())) {
			payload="{\"source\":{\"objects\":{\"objects\":[{\"db_name\":\""+tenant+"\",\"user\":\""+user+"\",\"user_id\":\""+userId+"\",\"doc_id\":\""+documentId+"\",\"doc_type\":\"file\","
					+ "\"actions\":[{\"code\":\""+action+"\",\"possible_values\":[],\"meta_info\":{\"expire_on\":\""+ this.shareExpiry +"\"}}]}]},\"app\":\""+facility+"\"}}";
		} else {

			payload="{\"source\":{\"objects\":{\"objects\":[{\"db_name\":\""+tenant+"\",\"user\":\""+user+"\",\"user_id\":\""+userId+"\",\"doc_id\":\""+documentId+"\",\"doc_type\":\"file\","
					+ "\"actions\":[{\"code\":\""+action+"\",\"possible_values\":[],\"meta_info\":{\"current_link\":\"open\"}}]}]},\"app\":\""+facility+"\"}}";
		}


		StringEntity stringEntity = new StringEntity(payload);
		String path = suiteData.getAPIMap().get("getBoxUIRemediation").
				replace("{tenant}", suiteData.getTenantName()).
				replace("{version}", suiteData.getBaseVersion());

		//suiteData.getApiserverHostName()
		URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(), path, null);
		HttpResponse response =  restClient.doPost(uri, headers, null, stringEntity);
		String responseBody = ClientUtil.getResponseBody(response);

		Logger.info("Response body:"+ responseBody);		
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

	//@Test
	public void getExposedDocuments() throws Exception {
		//Url utils

		System.out.println("Decoded String:" + UrlUtils.decode(suiteData.getUsername()));

		List<NameValuePair> headers = getHeaders();

		List<NameValuePair> qparams = new ArrayList<NameValuePair>(); 
		qparams.add(new BasicNameValuePair("is_internal", "false"));
		qparams.add(new BasicNameValuePair("owned_by",  "qa-admin@elasticaqa.net")); //UrlUtils.decode(suiteData.getUsername())));
		qparams.add(new BasicNameValuePair("content_checks.vl_types",  UrlUtils.decode("vk_source_code")));
		//qparams.add(new BasicNameValuePair("content_checks.vk_content_iq_violations",  "test_ciq_profile"));

		String path = suiteData.getAPIMap().get("getBoxDocuments").
				replace("{tenant}", suiteData.getTenantName()).
				replace("{version}", suiteData.getBaseVersion());

		System.out.println("Path:" + path);
		URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(), path, qparams);
		HttpResponse response =  restClient.doGet(uri, headers);
		String responseBody = ClientUtil.getResponseBody(response);

		System.out.println("Response body:"+ responseBody);
	}


	


	//@Test(dependsOnMethods = "performSaasActivities")
	public void fetchElasticSearchLogsForCIQ() throws Exception {
		Reporter.log("Retrieving the logs from Elastic Search ...");
		String tsfrom = DateUtils.getMinutesFromCurrentTime(-20);
		String tsto   = DateUtils.getMinutesFromCurrentTime(20);

		//Get headers
		List<NameValuePair> headers = getHeaders();

		String payload = esQueryBuilder.getSearchQueryForCIQ(tsfrom, tsto, "Box", query);
		Reporter.log("Request body:"+ payload, true);

		//HttpRequest
		String path = suiteData.getAPIMap().get("getForensicsLogs") ;
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(), path);

		HttpResponse response =  restClient.doPost(dataUri, headers, null, new StringEntity(payload));
		String responseBody = ClientUtil.getResponseBody(response);
		Reporter.log("============================", 1);
		Reporter.log("Response body:"+ responseBody, 1);

		ESResults esresults = MarshallingUtils.unmarshall(responseBody, ESResults.class);
		assertEquals(HttpStatus.SC_OK, getResponseStatusCode(response), "Response code verification failed");
		//Add the assertions for forensic logs
		ciqValidator = new CIQValidator(esresults);

		//Validate all code
		ciqValidator.validateAll(prepareExpectedSource());
	}



	//Get expected source
	public com.elastica.beatle.ciq.dto.Source prepareExpectedSource() throws Exception {

		com.elastica.beatle.ciq.dto.Source source = new com.elastica.beatle.ciq.dto.Source();
		source.setSeverity("critical");
		source.setFacility("Box");
		source.setObjectName("/All Files/"+destinationFile);
		source.setResourceId(resourceId);
		source.setRisks("PII, ContentIQ Violations, HIPAA");
		source.setSource("API");

		String filepath = System.getProperty("user.dir") + "/src/test/resources/securlets/securletsData/" + "ContentChecksHipaa.json";
		FileInputStream inStream = new FileInputStream(FilenameUtils.separatorsToSystem(filepath));
		String contentChecksJson = IOUtils.toString(inStream);
		ContentChecks contentChecks = MarshallingUtils.unmarshall(contentChecksJson, ContentChecks.class);

		source.setContentChecks(contentChecks);
		source.setUser(suiteData.getUsername());
		source.setMessage("File "+ destinationFile +" has risk(s) - PII, ContentIQ Violations, HIPAA");
		source.setActivityType("Content Inspection");
		source.setName(destinationFile);
		return source;

	}




}