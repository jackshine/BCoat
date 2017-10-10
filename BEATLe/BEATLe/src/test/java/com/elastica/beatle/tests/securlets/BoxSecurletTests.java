package com.elastica.beatle.tests.securlets;

import java.io.File;
import java.io.FileInputStream;
import java.net.URI;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
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
import static org.testng.Assert.*;

import com.elastica.beatle.DateUtils;
import com.elastica.beatle.MarshallingUtils;
import com.elastica.beatle.Authorization.AuthorizationHandler;
import com.elastica.beatle.RestClient.ClientUtil;
import com.elastica.beatle.ciq.dto.ContentChecks;
import com.elastica.beatle.ciq.dto.ESResults;
import com.elastica.beatle.constants.FrameworkConstants;
import com.elastica.beatle.dci.DCIConstants;
import com.elastica.beatle.securlets.BoxDataProvider;
import com.elastica.beatle.securlets.CIQValidator;
import com.elastica.beatle.securlets.ESQueryBuilder;
import com.elastica.beatle.securlets.LogUtils;
import com.elastica.beatle.securlets.LogValidator;
import com.elastica.beatle.securlets.SecurletUtils;
import com.elastica.beatle.securlets.SecurletsConstants;
import com.elastica.beatle.securlets.SecurletUtils.elapp;
import com.elastica.beatle.securlets.SecurletUtils.facility;
import com.elastica.beatle.securlets.dto.ForensicSearchResults;
import com.elastica.beatle.securlets.dto.Hit;
import com.elastica.beatle.securlets.dto.SecurletDocument;
import com.elastica.beatle.securlets.dto.Source;
import com.elastica.beatle.securlets.dto.UIExposedDoc;
import com.universal.constants.CommonConstants;
import com.universal.dtos.box.AccessibleBy;
import com.universal.dtos.box.BoxCollaboration;
import com.universal.dtos.box.BoxFolder;
import com.universal.dtos.box.BoxGroup;
import com.universal.dtos.box.BoxMembership;
import com.universal.dtos.box.BoxUserInfo;
import com.universal.dtos.box.BoxWeblink;
import com.universal.dtos.box.CollaborationInput;
import com.universal.dtos.box.Entry;
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



public class BoxSecurletTests extends SecurletUtils {
	ESQueryBuilder esQueryBuilder = null;
	LogValidator logValidator;
	CIQValidator ciqValidator;
	BoxActivityLog boxActivityLog, quickBoxActivityLog;
	private String destinationFile, destinationI18NFile;
	private String renamedFile, renamedI18NFile;
	private String query;
	ArrayList<String> messages = new ArrayList<String>();
	
	ArrayList<String> actualUserMessages = new ArrayList<String>();
	ArrayList<String> actualFileMessages = new ArrayList<String>();
	ArrayList<String> actualFolderMessages = new ArrayList<String>();
	ArrayList<String> actualGroupMessages = new ArrayList<String>();
	ArrayList<String> actualWeblinkMessages = new ArrayList<String>();
	ArrayList<String> actualCollaborationMessages = new ArrayList<String>();
	ForensicSearchResults userLogs, groupUserLogs, weblinkLogs, collaboratorLogs, fileLogs, file18nLogs, folderLogs, contentInspectionLogs, quickFileLogs;
	HashMap<String, BoxActivity> activityMap = new HashMap<String, BoxActivity>();
	HashMap<String, BoxActivity> activityMapQuick = new HashMap<String, BoxActivity>();
	String resourceId;

	//String filename;
	private String uniqueFilename;
	private String uniqueFolder1;
	private String uniqueFolder2;
	private String uniqueFolder3;
	//Group related info
	private String groupName;
	//weblink related information
	private String weblinkName;
	private String collaborationName;
	BoxUserInfo userInfo;
	
	String createdTime;
	long waitTime = 5;

	public BoxSecurletTests() throws Exception {
		super();
		esQueryBuilder = new ESQueryBuilder();
		logValidator = new LogValidator();
		boxActivityLog = new BoxActivityLog();
		quickBoxActivityLog = new BoxActivityLog();
	}
	
	@BeforeClass(alwaysRun=true)
	public void initBox() throws Exception {
		AuthorizationHandler.disableAnonymization(suiteData);
	}
	
	
	
	@Test(groups={"QUICKSANITY"})
	public void performSaasActivitiesForQuickSanity() throws Exception {
		String randomId = UUID.randomUUID().toString();
		String sourceFile = "Design.pdf";
		String destFile = randomId + "_" + sourceFile;
		String renFile     = randomId + "_renamed_" + sourceFile;
		
		//Upload the file
		FileUploadResponse uploadResponse = universalApi.uploadFile("/", sourceFile, destFile);
		Reporter.log(uploadResponse.getFileName() + " " + uploadResponse.getResponseMessage(), true);
		String fileId = uploadResponse.getFileId();
		
		createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
		quickBoxActivityLog.setFileUploadLog(quickBoxActivityLog.getFileUploadLog().replace("{filename}", destFile));
		BoxActivity fileUploadActivity = new BoxActivity(quickBoxActivityLog.getFileUploadLog(), createdTime, Severity.informational.name(),  
					ObjectType.File.name(), ActivityType.Upload.name(), suiteData.getSaasAppUsername(), socUserName, ipInfo, fileId, destFile, "All Files");
		activityMapQuick.put("FileUploadActivity", fileUploadActivity);
		
		
		sleep(CommonConstants.THIRTY_SECONDS_SLEEP);
		
		//Rename the file
		universalApi.renameFile(fileId, renFile);
		createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
		quickBoxActivityLog.setFileRenameLog(quickBoxActivityLog.getFileRenameLog().replace("{filename}", renFile));
		BoxActivity fileRenameActivity = new BoxActivity(quickBoxActivityLog.getFileRenameLog(), createdTime, Severity.informational.name(),  
					ObjectType.File.name(), ActivityType.Rename.name(), suiteData.getSaasAppUsername(), socUserName, ipInfo, fileId, renFile, "0");
		activityMapQuick.put("FileRenameActivity", fileRenameActivity);
		
		sleep(CommonConstants.THIRTY_SECONDS_SLEEP);
		
		//Create shared link
		FileEntry sharedfile = universalApi.createDefaultSharedLink(fileId);
		createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
		quickBoxActivityLog.setFileShareLog(quickBoxActivityLog.getFileShareLog().replace("{filename}", renFile));
		BoxActivity fileShareActivity = new BoxActivity(quickBoxActivityLog.getFileShareLog(), createdTime, Severity.informational.name(),  
					ObjectType.File.name(), ActivityType.Share.name(), suiteData.getSaasAppUsername(), socUserName, ipInfo, fileId, renFile, "0");
		activityMapQuick.put("FileShareActivity", fileShareActivity);
		
		sleep(CommonConstants.THIRTY_SECONDS_SLEEP);

		//Unshare the link
		FileEntry unsharedfile = universalApi.disableSharedLink(fileId);
		createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
		quickBoxActivityLog.setFileUnshareLog(quickBoxActivityLog.getFileUnshareLog().replace("{filename}", renFile));
		BoxActivity fileUnshareActivity = new BoxActivity(quickBoxActivityLog.getFileUnshareLog(), createdTime, Severity.informational.name(),  
					ObjectType.File.name(), ActivityType.Unshare.name(), suiteData.getSaasAppUsername(), socUserName, ipInfo, fileId, renFile, "0");
		activityMapQuick.put("FileUnshareActivity", fileUnshareActivity);
		
		sleep(CommonConstants.THIRTY_SECONDS_SLEEP);

		//delete the file
		universalApi.deleteFile(unsharedfile.getId(), unsharedfile.getEtag());
		createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
		quickBoxActivityLog.setFileDeleteLog(quickBoxActivityLog.getFileDeleteLog().replace("{filename}", renFile));
		BoxActivity fileDeleteActivity = new BoxActivity(quickBoxActivityLog.getFileDeleteLog(), createdTime, Severity.informational.name(),  
					ObjectType.File.name(), ActivityType.Delete.name(), suiteData.getSaasAppUsername(), socUserName, ipInfo, fileId, renFile, "0");
		activityMapQuick.put("FileDeleteActivity", fileDeleteActivity);
				
		try {

			for (int i = 1; i <= (3 * 60 * 1000); i+=60000 ) {
				sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
				
				HashMap<String, String> termmap = new HashMap<String, String>();
				termmap.put("facility", facility.Box.name());
				termmap.put("Object_type", ObjectType.File.name());
				termmap.put("__source", "API");

				//Get quick file related logs
				quickFileLogs = this.getInvestigateLogs(-10, 5, facility.Box.name(), termmap, suiteData.getUsername(), suiteData.getApiserverHostName(), 
						suiteData.getCSRFToken(), suiteData.getSessionID(), 0, 100, facility.Box.name());			}
		}
		catch(Exception e) {}
		
		Reporter.log(MarshallingUtils.marshall(quickFileLogs), true);
		long total = quickFileLogs.getHits().getTotal();
		Reporter.log("Total file logs count:"+ total, true);
		
		//If total number of logs is less than zero, no logs are seen so skip the tests
		if (total <= 0) {
			throw new SkipException("Skipping the dependent tests..No file related logs are seen in quick sanity.");
		}
	}
	
	
	@Test(dependsOnMethods = "performSaasActivitiesForQuickSanity", groups={"QUICKSANITY"})
	public void verifyFileUploadFileActivity() {
		LogUtils.logTestDescription("After uploading the file, verify the upload activity in the activity logs.");
		logValidator.verifyActivityLog(quickFileLogs, activityMapQuick.get("FileUploadActivity"));
	}
	
	@Test(dependsOnMethods = "performSaasActivitiesForQuickSanity", groups={"QUICKSANITY"})
	public void verifyFileRenameActivity() {
		LogUtils.logTestDescription("After renaming the file, verify the rename activity in the activity logs.");
		logValidator.verifyActivityLog(quickFileLogs, activityMapQuick.get("FileRenameActivity"));
	}

	@Test(dependsOnMethods = "performSaasActivitiesForQuickSanity", groups={"QUICKSANITY"})
	public void verifyFileSharingActivity() {
		LogUtils.logTestDescription("After sharing the file, verify the share activity in the activity logs.");
		logValidator.verifyActivityLog(quickFileLogs, activityMapQuick.get("FileShareActivity"));
	}

	@Test(dependsOnMethods = "performSaasActivitiesForQuickSanity", groups={"QUICKSANITY"})
	public void verifyFileUnsharingActivity() {
		LogUtils.logTestDescription("After Unsharing the file, verify the unshare activity in the activity logs.");
		logValidator.verifyActivityLog(quickFileLogs, activityMapQuick.get("FileUnshareActivity"));
	}

	@Test(dependsOnMethods = "performSaasActivitiesForQuickSanity", groups={"QUICKSANITY"})
	public void verifyFileDeleteActivity() {
		LogUtils.logTestDescription("After deleting the file, verify the delete activity in the activity logs.");
		logValidator.verifyActivityLog(quickFileLogs, activityMapQuick.get("FileDeleteActivity"));
	}
	
	
	//Collaboration related tests
	@Test(priority=-10, groups={"COLLABORATION", "P1"})
	public void performCollaborationRelatedActivities() throws Exception {
		String uniqueId = UUID.randomUUID().toString();
		String targetFile = uniqueId +".txt";
		collaborationName = uniqueId + "_collaboration";
		BoxFolder collaborationFolder  = universalApi.createFolder(collaborationName);
		sleep(CommonConstants.THIRTY_SECONDS_SLEEP);
		
		FileUploadResponse uploadResponse = universalApi.uploadFile(collaborationFolder.getId(), "Box_HIPAA_Test2.txt", targetFile);
		sleep(CommonConstants.THIRTY_SECONDS_SLEEP);
		
		//create collaboration
		CollaborationInput collabInput = new CollaborationInput();
		Item item = new Item();
		item.setId(collaborationFolder.getId());
		item.setType("folder");
		
		AccessibleBy aby = new AccessibleBy();
		aby.setName(uniqueId);
		aby.setType("user");
		aby.setLogin("pushpan@gmail.com");
		
		collabInput.setItem(item);
		collabInput.setAccessibleBy(aby);
		collabInput.setRole("editor");
		
		
		BoxCollaboration collaboration = universalApi.createCollaboration(collabInput);
		boxActivityLog.setInviteCollaborator(boxActivityLog.getInviteCollaborator());
		Reporter.log("Collaboration Id:" + collaboration.getId(), true);
		
		createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
		BoxActivity inviteCollaboratorActivity = new BoxActivity(boxActivityLog.getInviteCollaborator(), createdTime, Severity.informational.name(),  
																ObjectType.Folder.name(), ActivityType.Share.name(), suiteData.getSaasAppUsername(), 
																socUserName, ipInfo, collaborationFolder.getId(), collaborationName, "0");
		
		activityMap.put("InviteCollaboratorActivity", inviteCollaboratorActivity);
		
		sleep(CommonConstants.THREE_MINUTES_SLEEP);
		//After wait time invitee should accept the invite
		
		boxActivityLog.setAcceptCollaboration(boxActivityLog.getAcceptCollaboration()); 
		
		createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
		BoxActivity acceptCollaborationActivity = new BoxActivity(boxActivityLog.getAcceptCollaboration(), createdTime, Severity.informational.name(),  
																ObjectType.Folder.name(), "Accept Collaboration", suiteData.getSaasAppUsername(), 
																socUserName, ipInfo, collaboration.getId(), collaborationName, null);
		
		activityMap.put("AcceptCollaborationActivity", acceptCollaborationActivity);
		
		//Delete the collaboration
		universalApi.deleteCollaboration(collaboration);
		boxActivityLog.setRemoveCollaborator(boxActivityLog.getRemoveCollaborator());
		
		createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
		BoxActivity removeCollaboratorActivity = new BoxActivity(boxActivityLog.getRemoveCollaborator(), createdTime, Severity.informational.name(),  
																ObjectType.Folder.name(), ActivityType.Unshare.name(), suiteData.getSaasAppUsername(), 
																socUserName, ipInfo, collaborationFolder.getId(), collaborationName, "0");
		
		activityMap.put("RemoveCollaboratorActivity", removeCollaboratorActivity);
		
		sleep(CommonConstants.THREE_MINUTES_SLEEP);
		
		//create a group
		GroupInput ginput = new GroupInput();
		
		ginput.setName(uniqueId);
		ginput.setProvenance("Elastica");
		ginput.setDescription("Bangalore Team");
		
		BoxGroup bgroup = universalApi.createGroup(ginput);
		
		collabInput = new CollaborationInput();
		item = new Item();
		item.setId(collaborationFolder.getId());
		item.setType("folder");
		
		aby = new AccessibleBy();
		aby.setId(bgroup.getId());
		aby.setType("group");
		
		collabInput.setItem(item);
		collabInput.setAccessibleBy(aby);
		collabInput.setRole("editor");
		
		//Create a collaboration for a group
		collaboration = universalApi.createCollaboration(collabInput);
		boxActivityLog.setAddGroupCollaboration(boxActivityLog.getAddGroupCollaboration().replace("{foldername}", collaborationName)
															.replace("{groupname}", uniqueId)); 
		
		
		createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
		BoxActivity addGroupCollaborationActivity = new BoxActivity(boxActivityLog.getAddGroupCollaboration(), createdTime, Severity.informational.name(),  
																ObjectType.Folder.name(), ActivityType.Share.name(), suiteData.getSaasAppUsername(), 
																socUserName, ipInfo, collaborationFolder.getId());
		
		activityMap.put("AddGroupCollaborationActivity", addGroupCollaborationActivity);
		
		sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
		
		//Update the collaboration with expiry time
		//collaboration.setExpiresAt(this.getDaysFromCurrentTime(3));
		//Set the collaboration expiration to 3 mins
		collaboration.setExpiresAt(DateUtils.getMinutesFromCurrentTime(3));
		//Change the collaborator role
		collaboration.setRole("co-owner");
		universalApi.updateCollaboration(collaboration);
		boxActivityLog.setCollaborationRoleChange(boxActivityLog.getCollaborationRoleChange()); 
		
		BoxActivity collaborationRoleChangeActivity = new BoxActivity(boxActivityLog.getCollaborationRoleChange(), createdTime, Severity.informational.name(),  
				ObjectType.Folder.name(), "Change Collaboration Role", suiteData.getSaasAppUsername(), 
				socUserName, ipInfo, collaborationFolder.getId(), collaborationName, "0");

		activityMap.put("CollaborationRoleChangeActivity", collaborationRoleChangeActivity);
		
		sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
		
		//Delete the collaboration
		universalApi.deleteCollaboration(collaboration);
		boxActivityLog.setDeleteCollaboration(boxActivityLog.getDeleteCollaboration().replace("{collaborationname}", collaborationName));
		boxActivityLog.setRemoveGroupCollaboration(boxActivityLog.getRemoveGroupCollaboration().replace("{foldername}", collaborationName)
																						.replace("{groupname}", uniqueId)); 
		
		

		BoxActivity deleteCollaborationActivity = new BoxActivity(boxActivityLog.getDeleteCollaboration(), createdTime, Severity.informational.name(),  
																		ObjectType.User.name(), ActivityType.Remove.name(), suiteData.getSaasAppUsername(), 
																		socUserName, ipInfo, collaborationFolder.getId());

		activityMap.put("DeleteCollaborationActivity", deleteCollaborationActivity);
		
		
		BoxActivity deleteGroupCollaborationActivity = new BoxActivity(boxActivityLog.getRemoveGroupCollaboration(), createdTime, Severity.informational.name(),  
																		ObjectType.Folder.name(), ActivityType.Unshare.name(), suiteData.getSaasAppUsername(), 
																		socUserName, ipInfo, collaborationFolder.getId(), collaborationName, "0");

		activityMap.put("DeleteGroupCollaborationActivity", deleteGroupCollaborationActivity);
		
		
		
		sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
		
		String anotherUserId = "";
		UserCollection users = universalApi.listUser();
		for(BoxUserInfo user : users.getEntries()) {
			
			String userDomain = StringUtils.split(this.saasAppUserAccount.getUsername(), "@")[1];
			if (!user.getId().equals(collaborationFolder.getOwnedBy().getId()) && user.getLogin().contains(userDomain)) {
				anotherUserId = user.getId();
				break;
			}
		}
		
		//Transfer owner
		BoxFolder transferredFolder = universalApi.transferOwner(collaborationFolder.getId(), collaborationFolder.getOwnedBy().getId(), anotherUserId);
		sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
		
		//Cleanup
		//Delete folder
		universalApi.deleteFolder(collaborationFolder.getId(), true, collaborationFolder.getEtag());
		sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
		
		universalApi.purgeFolder(collaborationFolder.getId());
		
		
		ForensicSearchResults fsr = null;
		//Fetch the logs
		try {

			for (int i = 1; i <= (waitTime * 60 * 1000); i+=60000 ) {
				sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
				HashMap<String, String> termmap = new HashMap<String, String>();
				termmap.put("facility", facility.Box.name());
				termmap.put("Object_type", ObjectType.Folder.name());
				termmap.put("__source", "API");
				
				//Get user related logs
				fsr = this.getInvestigateLogs(-30, 10, facility.Box.name(), termmap, suiteData.getUsername(), suiteData.getApiserverHostName(), 
						suiteData.getCSRFToken(), suiteData.getSessionID(), 0, 100, facility.Box.name());

				termmap.put("Object_type", ObjectType.User.name());

				//Get user related logs
				collaboratorLogs = this.getInvestigateLogs(-30, 10, facility.Box.name(), termmap, suiteData.getUsername(), suiteData.getApiserverHostName(), 
						suiteData.getCSRFToken(), suiteData.getSessionID(), 0, 100, facility.Box.name());

				//merge both the logs
				collaboratorLogs.getHits().getHits().addAll(fsr.getHits().getHits());
				
			}
		}
		catch(Exception e) {}
		Reporter.log(MarshallingUtils.marshall(collaboratorLogs), true);
		long total = fsr.getHits().getTotal();
		Reporter.log("Total collaborator logs count:"+ total, true);
		
		//If total number of logs is less than zero, no logs are seen so skip the tests
		if (total <= 0) {
			throw new SkipException("Skipping the dependent tests..No logs are seen for collaborator in Box");
		}
		
	}
	
	@Test(groups={"COLLABORATION", "P1"})
	public void verifyAddUserCollaborationActivity() {
		LogUtils.logTestDescription("After inviting the collaborator, verify the invite collaborator activity in the activity logs.");
		logValidator.verifyActivityLog(collaboratorLogs, activityMap.get("InviteCollaboratorActivity"));
		
	}

	@Test(groups={"COLLABORATION", "P1"})
	public void verifyAddGroupCollaborationActivity() {
		LogUtils.logTestDescription("After adding the group collaboration, verify the add group collaboration activity in the activity logs.");
		logValidator.verifyActivityLog(collaboratorLogs, activityMap.get("AddGroupCollaborationActivity"));
	}

	@Test(groups={"COLLABORATION", "P1"})
	public void verifyRemoveUserCollaborationActivity() {
		LogUtils.logTestDescription("After removing the collaborator, verify the remove collaborator activity in the activity logs.");
		logValidator.verifyActivityLog(collaboratorLogs, activityMap.get("RemoveCollaboratorActivity"));
		
	}
	
	@Test(groups={"COLLABORATION", "P1"})
	public void verifyRemoveGroupCollaborationActivity() {
		LogUtils.logTestDescription("After removing the group collaboration, verify the delete group collaboration activity in the activity logs.");
		logValidator.verifyActivityLog(collaboratorLogs, activityMap.get("DeleteGroupCollaborationActivity"));
		
	}
	
	//@Test(groups={"COLLABORATION"})
	public void verifyAcceptInviteCollaborationActivity() {
		LogUtils.logTestDescription("After accepting the invite, verify the invite accepted activity in the activity logs.");
		logValidator.verifyActivityLog(collaboratorLogs, activityMap.get("AcceptCollaborationActivity"));
	}
	
	//@Test(dependsOnMethods="performCollaborationRelatedActivities", groups={"COLLABORATION"})
	public void verifyTransferOfOwnership() {
		String expectedLog = "Ownership of "+ collaborationName + " has been transferred to external user";
		//Reporter.log("Expected log:" + expectedLog, true);
		assertTrue(actualCollaborationMessages.contains(expectedLog), expectedLog + " not present in the logs");
	}
	
	@Test(groups={"COLLABORATION", "P1"})
	public void verifyRoleChangeCollaborationActivity() {
		LogUtils.logTestDescription("After changing the role of collaborator, verify the activity in the activity logs.");
		logValidator.verifyActivityLog(collaboratorLogs, activityMap.get("CollaborationRoleChangeActivity"));
	}
	
	
	
	//Weblink related tests
	@Test(priority = -10, groups={"WEBLINK"})
	public void performWeblinkRelatedActivities() throws Exception {
		String uniqueId = UUID.randomUUID().toString();
		//String uniqueId = "3fc4c4e3-3e5a-4bd0-b134-f2936efdcc6f";
		weblinkName = uniqueId + "_weblink";
		
		//Parent folder to create the weblink
		Parent parent = new Parent();
		parent.setId("0");
		
		WeblinkInput wlinput = new WeblinkInput();
		wlinput.setUrl("https://www.elastica.net");
		wlinput.setName(weblinkName);
		wlinput.setDescription("Cloud security experts");
		wlinput.setParent(parent);
		BoxWeblink bwl = universalApi.createWeblink(wlinput);
		boxActivityLog.setWeblinkCreateLog(boxActivityLog.getWeblinkCreateLog().replace("{weblinkname}", weblinkName)
				 									.replace("{foldername}", "All Files"));
		
		createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
		BoxActivity createWeblinkActivity = new BoxActivity(boxActivityLog.getWeblinkCreateLog(), createdTime, Severity.informational.name(),  
																ObjectType.Web_Link.name(), ActivityType.Upload.name(), suiteData.getSaasAppUsername(), 
																socUserName, ipInfo, bwl.getId(), weblinkName, "All Files");
		activityMap.put("CreateWeblinkActivity", createWeblinkActivity);
		sleep(CommonConstants.THIRTY_SECONDS_SLEEP);
		// Commented temporarily as box api is throwing error
		//Update weblink name
		bwl.setName(weblinkName +"_updated");
		BoxWeblink updatedbwl = universalApi.updateWeblink(bwl);
		sleep(CommonConstants.THIRTY_SECONDS_SLEEP);
		
		//update it again 
		updatedbwl.setName(weblinkName);
		bwl = universalApi.updateWeblink(updatedbwl);
		boxActivityLog.setWeblinkUpdateLog(boxActivityLog.getWeblinkUpdateLog().replace("{weblinkname}", weblinkName));
		
		createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
		BoxActivity updateWeblinkActivity = new BoxActivity(boxActivityLog.getWeblinkUpdateLog(), createdTime, Severity.informational.name(),  
																ObjectType.Web_Link.name(), ActivityType.Rename.name(), suiteData.getSaasAppUsername(), 
																socUserName, ipInfo, bwl.getId(), weblinkName, null);
		activityMap.put("UpdateWeblinkActivity", updateWeblinkActivity);
		
		sleep(CommonConstants.THIRTY_SECONDS_SLEEP);
		
		//Share weblink
		SharedLink sharedLink = new SharedLink();
		sharedLink.setAccess("open"); //Allowed values open, company, collaborators, or null
		universalApi.createSharedLinkForWeblink(bwl.getId(), sharedLink);
		boxActivityLog.setWeblinkShareLog(boxActivityLog.getWeblinkShareLog().replace("{weblinkname}", weblinkName));
		boxActivityLog.setWeblinkSharePermissionsLog(boxActivityLog.getWeblinkSharePermissionsLog().replace("{weblinkname}", weblinkName));
		
		createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
		BoxActivity shareWeblinkActivity = new BoxActivity(boxActivityLog.getWeblinkShareLog(), createdTime, Severity.informational.name(),  
																ObjectType.Web_Link.name(), ActivityType.Share.name(), suiteData.getSaasAppUsername(), 
																socUserName, ipInfo, bwl.getId(), weblinkName, null);
		activityMap.put("ShareWeblinkActivity", shareWeblinkActivity);
		
		BoxActivity shareWeblinkPermissionsActivity = new BoxActivity(boxActivityLog.getWeblinkSharePermissionsLog(), createdTime, Severity.informational.name(),  
																ObjectType.Web_Link.name(), ActivityType.Share.name(), suiteData.getSaasAppUsername(), 
																socUserName, ipInfo, bwl.getId(), weblinkName, null);
		activityMap.put("ShareWeblinkPermissionsActivity", shareWeblinkPermissionsActivity);
		
		sleep(CommonConstants.THIRTY_SECONDS_SLEEP);
		
		//Set the expiration
		sharedLink.setUnsharedAt(DateUtils.getDateFromCurrentTime(2));
		sharedLink.setAccess("company");
		universalApi.updateSharedLinkForWeblink(bwl.getId(), sharedLink);
		boxActivityLog.setWeblinkShareExpirationLog(boxActivityLog.getWeblinkShareExpirationLog().replace("{weblinkname}", weblinkName));
		
		createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
		BoxActivity shareWeblinkExpirationActivity = new BoxActivity(boxActivityLog.getWeblinkSharePermissionsLog(), createdTime, Severity.informational.name(),  
																			ObjectType.Web_Link.name(), "Expire Sharing", suiteData.getSaasAppUsername(), 
																			socUserName, ipInfo, bwl.getId(), weblinkName, null);
		activityMap.put("ShareWeblinkExpirationActivity", shareWeblinkExpirationActivity);
		
		sleep(CommonConstants.THIRTY_SECONDS_SLEEP);
		
		//disable the sharing
		universalApi.disableSharedLinkForWeblink(bwl.getId());
		boxActivityLog.setWeblinkUnshareLog(boxActivityLog.getWeblinkUnshareLog().replace("{weblinkname}", weblinkName));
		createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
		BoxActivity unshareWeblinkActivity = new BoxActivity(boxActivityLog.getWeblinkUnshareLog(), createdTime, Severity.informational.name(),  
																			ObjectType.Web_Link.name(), ActivityType.Unshare.name(), suiteData.getSaasAppUsername(), 
																			socUserName, ipInfo, bwl.getId(), weblinkName, null);
		activityMap.put("UnshareWeblinkActivity", unshareWeblinkActivity);
		
		sleep(CommonConstants.THIRTY_SECONDS_SLEEP);
		
		//copy weblink
		BoxFolder bfolder = universalApi.createFolder(uniqueId+"_Weblink Folder");
		BoxWeblink cwl = universalApi.copyWeblink(bwl.getId(), bfolder.getId());
		boxActivityLog.setWeblinkCopyLog(boxActivityLog.getWeblinkCopyLog().replace("{weblinkname}", weblinkName));
		createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
		BoxActivity copyWeblinkActivity = new BoxActivity(boxActivityLog.getWeblinkCopyLog(), createdTime, Severity.informational.name(),  
																			ObjectType.Web_Link.name(), ActivityType.Copy.name(), suiteData.getSaasAppUsername(), 
																			socUserName, ipInfo, cwl.getId(), weblinkName, bfolder.getName());
		activityMap.put("CopyWeblinkActivity", copyWeblinkActivity);
		sleep(CommonConstants.THIRTY_SECONDS_SLEEP);
		
		
		//create one more folder and move the weblink
		BoxFolder bfolder1 = universalApi.createFolder(uniqueId+"_Weblink Folder1");
		bwl.getParent().setId(bfolder1.getId());
		universalApi.updateWeblink(bwl);
		boxActivityLog.setWeblinkMoveLog(boxActivityLog.getWeblinkMoveLog().replace("{weblinkname}", weblinkName));
		
		createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
		BoxActivity moveWeblinkActivity = new BoxActivity(boxActivityLog.getWeblinkMoveLog(), createdTime, Severity.informational.name(),  
																			ObjectType.Web_Link.name(), ActivityType.Move.name(), suiteData.getSaasAppUsername(), 
																			socUserName, ipInfo, bwl.getId(), weblinkName, bfolder1.getName());
		activityMap.put("MoveWeblinkActivity", moveWeblinkActivity);
		
		sleep(CommonConstants.THIRTY_SECONDS_SLEEP);
		
		//Delete weblink
		universalApi.deleteWeblink(bwl);
		boxActivityLog.setWeblinkDeleteLog(boxActivityLog.getWeblinkDeleteLog().replace("{weblinkname}", weblinkName));
		createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
		BoxActivity deleteWeblinkActivity = new BoxActivity(boxActivityLog.getWeblinkDeleteLog(), createdTime, Severity.informational.name(),  
																			ObjectType.Web_Link.name(), ActivityType.Delete.name(), suiteData.getSaasAppUsername(), 
																			socUserName, ipInfo, bwl.getId(), weblinkName, null);
		activityMap.put("DeleteWeblinkActivity", deleteWeblinkActivity);
		
		sleep(CommonConstants.THIRTY_SECONDS_SLEEP);
		
		//Restore weblink
		universalApi.restoreWeblink(bwl);
		boxActivityLog.setWeblinkRestoreLog(boxActivityLog.getWeblinkRestoreLog());
		createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
		BoxActivity restoreWeblinkActivity = new BoxActivity(boxActivityLog.getWeblinkRestoreLog(), createdTime, Severity.informational.name(),  
																			ObjectType.Web_Link.name(), ActivityType.Undelete.name(), suiteData.getSaasAppUsername(), 
																			socUserName, ipInfo, bwl.getId(), weblinkName, null);
		activityMap.put("RestoreWeblinkActivity", restoreWeblinkActivity);
		sleep(CommonConstants.THIRTY_SECONDS_SLEEP);
		
		//Delete weblink
		universalApi.deleteWeblink(bwl);
		
		try {

			for (int i = 0; i <= (waitTime * 60 * 1000); i+=60000 ) {
				sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
				
				HashMap<String, String> termmap = new HashMap<String, String>();
				termmap.put("facility", facility.Box.name());
				termmap.put("Object_type", ObjectType.Web_Link.name());
				termmap.put("__source", "API");

				//Get user related logs
				weblinkLogs = this.getInvestigateLogs(-30, 10, facility.Box.name(), termmap, suiteData.getUsername(), suiteData.getApiserverHostName(), 
						suiteData.getCSRFToken(), suiteData.getSessionID(), 0, 100, facility.Box.name());
				
			}
		}
		catch(Exception e) {}
		Reporter.log(MarshallingUtils.marshall(weblinkLogs), true);
		long total = weblinkLogs.getHits().getTotal();
		Reporter.log("Total weblink logs count:"+ total, true);
		
		//If total number of logs is less than zero, no logs are seen so skip the tests
		if (total <= 0) {
			throw new SkipException("Skipping the dependent tests..No logs are seen for weblink in Box");
		}
		
		// clean the web link folders if there are no exceptions
		universalApi.deleteFolder(bfolder.getId(), true, (String) bfolder.getEtag());
		universalApi.deleteFolder(bfolder1.getId(), true, (String) bfolder1.getEtag());
		
	}
	
	
	@Test(groups={"WEBLINK"})
	public void weblinkCreateActivity() {
		LogUtils.logTestDescription("After creating the weblink, verify the weblink creation activity in the activity logs.");
		logValidator.verifyActivityLog(weblinkLogs, activityMap.get("CreateWeblinkActivity"));
	}

	@Test(groups={"WEBLINK"})
	public void weblinkUpdateActivity() {
		LogUtils.logTestDescription("After updating the weblink, verify the weblink updation activity in the activity logs.");
		logValidator.verifyActivityLog(weblinkLogs, activityMap.get("UpdateWeblinkActivity"));
	}

	
	@Test(groups={"WEBLINK"})
	public void weblinkDeleteActivity() {
		LogUtils.logTestDescription("After deleting the weblink, verify the weblink deletion activity in the activity logs.");
		logValidator.verifyActivityLog(weblinkLogs, activityMap.get("DeleteWeblinkActivity"));
	}
	
	@Test(groups={"WEBLINK"})
	public void weblinkRestoreActivity() {
		LogUtils.logTestDescription("After restoring the weblink, verify the weblink restore activity in the activity logs.");
		logValidator.verifyActivityLog(weblinkLogs, activityMap.get("RestoreWeblinkActivity"));
	}
	
	
	@Test(groups={"WEBLINK"})
	public void weblinkCopyActivity() {
		LogUtils.logTestDescription("After copying the weblink, verify the weblink copy activity in the activity logs.");
		logValidator.verifyActivityLog(weblinkLogs, activityMap.get("CopyWeblinkActivity"));
	}
	
	@Test(groups={"WEBLINK"})
	public void weblinkMoveActivity() {
		LogUtils.logTestDescription("After moving the weblink, verify the weblink move activity in the activity logs.");
		logValidator.verifyActivityLog(weblinkLogs, activityMap.get("MoveWeblinkActivity"));
	}
	
	
	@Test(groups={"WEBLINK"})
	public void weblinkShareActivity() {
		LogUtils.logTestDescription("After sharing the weblink, verify the weblink share activity in the activity logs.");
		logValidator.verifyActivityLog(weblinkLogs, activityMap.get("ShareWeblinkActivity"));
	}
	
	@Test(groups={"WEBLINK"})
	public void weblinkSharePermissionsActivity() {
		LogUtils.logTestDescription("After sharing the weblink, verify the weblink share permissions in the activity logs.");
		logValidator.verifyActivityLog(weblinkLogs, activityMap.get("ShareWeblinkPermissionsActivity"));
	}
	
	@Test(groups={"WEBLINK"})
	public void weblinkShareExpirationActivity() {
		LogUtils.logTestDescription("After share expiration of the weblink, verify the activity in the activity logs.");
		logValidator.verifyActivityLog(weblinkLogs, activityMap.get("ShareWeblinkPermissionsActivity"));
	}
	
	@Test(groups={"WEBLINK"})
	public void weblinkDisableShareActivity() {
		LogUtils.logTestDescription("After unsharing the weblink, verify the weblink unshare activity in the activity logs.");
		logValidator.verifyActivityLog(weblinkLogs, activityMap.get("UnshareWeblinkActivity"));
	}
	
	
	
	
	
	
	@Test(groups={"GROUP"})
	public void performGroupRelatedActivities() throws Exception {
		
		
		//Clean up if the user already exists
		UserCollection usersList = universalApi.listUser();
		
		for(BoxUserInfo boxUser : usersList.getEntries()) {
			if (boxUser.getLogin().equals("box-admin+4@securletbeatle.com")) {
				universalApi.deleteUser(boxUser);
			}
		}
		
		//Sleep time introduced as user delete immediately not reflected in box
		sleep(CommonConstants.THIRTY_SECONDS_SLEEP);
		
		//Test starts from this point
		String uniqueId = UUID.randomUUID().toString();
		groupName = uniqueId + "_blr_group";
		
		query = uniqueId; 
		
		//Create a user
		userInfo = new BoxUserInfo();
		userInfo.setLogin("box-admin+4@securletbeatle.com");
		userInfo.setName(uniqueId +"_securlets");
		userInfo.setRole("coadmin");
		
		userInfo.getName();
		
		//Create the user
		Reporter.log("User Creation Input:"+userInfo, true);
		BoxUserInfo user = universalApi.createUser(userInfo);
		Reporter.log("created user object:"+MarshallingUtils.marshall(user), true);
		
				
		//Create the group
		GroupInput input = new GroupInput();
		input.setName(groupName);
		input.setProvenance("Elastica");
		BoxGroup boxGroup = universalApi.createGroup(input);
		
		createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
		boxActivityLog.setGroupCreateLog(boxActivityLog.getGroupCreateLog().replace("{groupname}", groupName));
		BoxActivity createGroupActivity = new BoxActivity(boxActivityLog.getGroupCreateLog(), createdTime, Severity.informational.name(),  
				ObjectType.Group.name(), ActivityType.Create.name(), suiteData.getSaasAppUsername(), socUserName, ipInfo, boxGroup.getId());
		activityMap.put("CreateGroupActivity", createGroupActivity);
		
		
		sleep(CommonConstants.THIRTY_SECONDS_SLEEP);
		
		//Update the group
		boxGroup.setName(uniqueId +"_blr_team");
		boxGroup = universalApi.updateGroup(boxGroup);
		
		createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
		boxActivityLog.setGroupEditLog(boxActivityLog.getGroupEditLog().replace("{groupname}", boxGroup.getName()));
		BoxActivity editGroupActivity = new BoxActivity(boxActivityLog.getGroupEditLog(), createdTime, Severity.informational.name(),  
				ObjectType.Group.name(), ActivityType.Edit.name(), suiteData.getSaasAppUsername(), socUserName, ipInfo, boxGroup.getId());
		activityMap.put("UpdateGroupActivity", editGroupActivity);
		
		
		sleep(CommonConstants.THIRTY_SECONDS_SLEEP);
		//Add the user to the group
		//Update the group name again
		boxGroup.setName(groupName);
		boxGroup = universalApi.updateGroup(boxGroup);
		
		//Add the user to the created group thro membership
		
		MembershipInput membershipInput = new MembershipInput();
		membershipInput.setGroup(boxGroup);
		membershipInput.setUser(user);
		BoxMembership boxMembership = universalApi.createMembership(membershipInput);
		
		createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
		boxActivityLog.setGroupUserAddLog(boxActivityLog.getGroupUserAddLog().replace("{username}", user.getName()));
		BoxActivity groupUserAddLog = new BoxActivity(boxActivityLog.getGroupUserAddLog(), createdTime, Severity.informational.name(), ObjectType.User.name(), 
																ActivityType.Add.name(), suiteData.getSaasAppUsername(), socUserName, ipInfo, user.getId());
		activityMap.put("AddUserGroupActivity", groupUserAddLog);
		
		
		sleep(CommonConstants.THIRTY_SECONDS_SLEEP);
		
		
		//Remove the membership of the user in box
		universalApi.deleteMembership(boxMembership);
		
		//Prepare the verification object
		createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
		boxActivityLog.setGroupUserDeleteLog(boxActivityLog.getGroupUserDeleteLog().replace("{username}", user.getName()));
		BoxActivity groupUserDeleteLog = new BoxActivity(boxActivityLog.getGroupUserDeleteLog(), createdTime, Severity.informational.name(),  
				ObjectType.User.name(), ActivityType.Remove.name(), suiteData.getSaasAppUsername(), socUserName, ipInfo, user.getId());
		activityMap.put("DeleteUserGroupActivity", groupUserDeleteLog);
		
		sleep(CommonConstants.THIRTY_SECONDS_SLEEP);
		
		//Delete the group
		universalApi.deleteGroup(boxGroup);
		
		boxActivityLog.setGroupDeleteLog(boxActivityLog.getGroupDeleteLog().replace("{groupname}", groupName));
		BoxActivity deleteGroupActivity = new BoxActivity(boxActivityLog.getGroupDeleteLog(), createdTime, Severity.informational.name(),  
				ObjectType.Group.name(), ActivityType.Delete.name(), suiteData.getSaasAppUsername(), socUserName, ipInfo, boxGroup.getId());
		activityMap.put("DeleteGroupActivity", deleteGroupActivity);
		
		sleep(CommonConstants.THIRTY_SECONDS_SLEEP);
		
		//delete the user
		universalApi.deleteUser(user);
		
		try {

			for (int i = 0; i <= (waitTime * 60 * 1000); i+=60000 ) {
				
				sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
				HashMap<String, String> termmap = new HashMap<String, String>();
				termmap.put("facility", facility.Box.name());
				termmap.put("Object_type", ObjectType.User.name());
				termmap.put("__source", "API");

				//Get user related logs
				ForensicSearchResults fsr = this.getInvestigateLogs(-30, 10, facility.Box.name(), termmap, suiteData.getUsername(), suiteData.getApiserverHostName(), 
						suiteData.getCSRFToken(), suiteData.getSessionID(), 0, 100, facility.Box.name());

				termmap.put("Object_type", ObjectType.Group.name());



				//Get group related logs
				groupUserLogs = this.getInvestigateLogs(-30, 10, facility.Box.name(), termmap, suiteData.getUsername(), suiteData.getApiserverHostName(), 
						suiteData.getCSRFToken(), suiteData.getSessionID(), 0, 100, facility.Box.name());

				//merge both the logs
				groupUserLogs.getHits().getHits().addAll(fsr.getHits().getHits());
			}
		}
		catch(Exception e) {}
		
		Reporter.log(MarshallingUtils.marshall(groupUserLogs), true);
		long total = groupUserLogs.getHits().getTotal();
		Reporter.log("Total user logs count:"+ total, true);
		
		//If total number of logs is less than zero, no logs are seen so skip the tests
		if (total <= 0) {
			throw new SkipException("Skipping the dependent tests..No logs are seen for groups in Box");
		}
	}

	//Bug
	@Test(dependsOnMethods="performGroupRelatedActivities", groups={"GROUP"})
	public void groupCreateActivity() {
		LogUtils.logTestDescription("After creating the group, verify the group creation activity in the activity logs.");
		logValidator.verifyActivityLog(groupUserLogs, activityMap.get("CreateGroupActivity"));
	}

	@Test(dependsOnMethods="performGroupRelatedActivities", groups={"GROUP"})
	public void groupUpdateActivity() {
		LogUtils.logTestDescription("After editing the group, verify the group updation activity in the activity logs.");
		logValidator.verifyActivityLog(groupUserLogs, activityMap.get("UpdateGroupActivity"));
	}

	@Test(dependsOnMethods="performGroupRelatedActivities", groups={"GROUP"})
	public void groupDeleteActivity() {
		LogUtils.logTestDescription("After deleting the group, verify the group deletion activity in the activity logs.");
		logValidator.verifyActivityLog(groupUserLogs, activityMap.get("DeleteGroupActivity"));
	}
	
	@Test(dependsOnMethods="performGroupRelatedActivities", groups={"GROUP"})
	public void groupMemberAddActivity() {
		LogUtils.logTestDescription("After adding a member to the group, verify the activity in the activity logs.");
		logValidator.verifyActivityLog(groupUserLogs, activityMap.get("AddUserGroupActivity"));
	}
	
	@Test(dependsOnMethods="performGroupRelatedActivities", groups={"GROUP"})
	public void groupMemberDeleteActivity() {
		LogUtils.logTestDescription("After deleting a member from the group, verify the activity in the activity logs.");
		logValidator.verifyActivityLog(groupUserLogs, activityMap.get("DeleteUserGroupActivity"));
	}
	
	


	@Test(groups={"USER", "REGRESSION", "SANITY"})
	public void performUserRelatedActivities() throws Exception {
		
		//Delete the user if already exists
		UserCollection usersList = universalApi.listUser();
		
		for(BoxUserInfo boxUser : usersList.getEntries()) {
			if (boxUser.getLogin().equals("box-admin+5@securletbeatle.com")) {
				universalApi.deleteUser(boxUser);
			}
		}
		
		//Sleep time introduced as user delete immediately not reflected in box
		sleep(CommonConstants.THIRTY_SECONDS_SLEEP);
		
		String uniqueId = UUID.randomUUID().toString();
		userInfo = new BoxUserInfo();
		userInfo.setLogin("box-admin+5@securletbeatle.com");
		userInfo.setName(uniqueId +"_securlet_user");
		userInfo.setRole("coadmin");
		Reporter.log("user object:"+MarshallingUtils.marshall(userInfo), true);

		//Create the user in saas
		BoxUserInfo createdUser = universalApi.createUser(userInfo);
		createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
		Reporter.log("created user object:"+MarshallingUtils.marshall(createdUser), true);
		
		
		//Prepare the verification object
		boxActivityLog.setUserCreateLog(boxActivityLog.getUserCreateLog().replace("{username}", userInfo.getName()));
		BoxActivity createUserActivity = new BoxActivity(boxActivityLog.getUserCreateLog(), createdTime, Severity.informational.name(),  
				ObjectType.User.name(), ActivityType.Add.name(), suiteData.getSaasAppUsername(), socUserName, ipInfo, createdUser.getId());
		activityMap.put("CreateUserActivity", createUserActivity);
		
		
		sleep(CommonConstants.THIRTY_SECONDS_SLEEP);
		
		
		//Update the user role and job title in saas app
		createdUser.setJobTitle("QA Engineer");
		BoxUserInfo updatedUser = universalApi.updateUser(createdUser);
		Reporter.log("updated user object:"+MarshallingUtils.marshall(updatedUser), true);
		
		//Prepare the verification object
		createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
		boxActivityLog.setUserUpdateLog(boxActivityLog.getUserUpdateLog().replace("{username}", userInfo.getName()));
		BoxActivity updateUserActivity = new BoxActivity(boxActivityLog.getUserUpdateLog(), createdTime, Severity.informational.name(),  
				ObjectType.User.name(), ActivityType.Edit.name(), suiteData.getSaasAppUsername(), socUserName, ipInfo, createdUser.getId());
		
		activityMap.put("UpdateUserActivity", updateUserActivity);
		
		//Delete the user in saas
		universalApi.deleteUser(updatedUser);
		
		//Prepare the verification object
		createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
		boxActivityLog.setUserDeleteLog(boxActivityLog.getUserDeleteLog().replace("{username}", userInfo.getName()));
		BoxActivity deleteUserActivity = new BoxActivity(boxActivityLog.getUserDeleteLog(), createdTime, Severity.informational.name(),  
				ObjectType.User.name(), ActivityType.Delete.name(), suiteData.getSaasAppUsername(), socUserName, ipInfo, createdUser.getId());
		
		activityMap.put("DeleteUserActivity", deleteUserActivity);
		
		try {

			for (int i = 1; i <= (waitTime * 60 * 1000); i+=60000 ) {
				sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
				HashMap<String, String> termmap = new HashMap<String, String>();
				termmap.put("facility", facility.Box.name());
				termmap.put("Object_type", ObjectType.User.name());
				termmap.put("__source", "API");
				userLogs = this.getInvestigateLogs(-30, 10, facility.Box.name(), termmap, suiteData.getUsername(), suiteData.getApiserverHostName(), 
						suiteData.getCSRFToken(), suiteData.getSessionID(), 0, 100, facility.Box.name());				
			}
		}
		
		catch(Exception e) {}
		
		long total = userLogs.getHits().getTotal();
		Reporter.log("Total user logs count:"+ total, true);
		Reporter.log(MarshallingUtils.marshall(userLogs), true);
		
		//If total number of logs is less than zero, no logs are seen so skip the tests
		if (total <= 0) {
			throw new SkipException("Skipping the dependent tests..No User logs are seen for Box");
		}			
	}

	@Test(dependsOnMethods="performUserRelatedActivities", groups={"USER"})
	public void userCreateActivity() {
		LogUtils.logTestDescription("After creating the user, verify the user creation activity in the activity logs.");
		logValidator.verifyActivityLog(userLogs, activityMap.get("CreateUserActivity"));
	}

	@Test(dependsOnMethods="performUserRelatedActivities", groups={"USER"})
	public void userUpdateActivity() {
		LogUtils.logTestDescription("After updating the user, verify the user updation activity in the activity logs.");
		logValidator.verifyActivityLog(userLogs, activityMap.get("UpdateUserActivity"));	
	}

	@Test(dependsOnMethods="performUserRelatedActivities", groups={"USER"})
	public void userDeleteActivity() {
		LogUtils.logTestDescription("After deleting the user, verify the user deletion activity in the activity logs.");
		logValidator.verifyActivityLog(userLogs, activityMap.get("DeleteUserActivity"));
	}

	//Test is disabled since user role change is not implementable in CloudSoc
	//@Test(dependsOnMethods="performUserRelatedActivities", groups={"USER"})
	public void userRoleChangeActivity() {
		String message = "User changed admin role for "+userInfo.getName();
		assertTrue(actualUserMessages.contains(message), message + " not present in the logs");
	}


	@Test(groups={"FILE", "P1"})
	public void performSaasFileActivities() throws Exception {
		String randomId = UUID.randomUUID().toString();
		String sourceFile = "Box_HIPAA_Test2.txt";
		String updateFile = "BE.txt";
		destinationFile = randomId + "_" + sourceFile;
		renamedFile     = randomId + "_renamed_" + sourceFile;
		query = randomId;

		//Upload the file
		FileUploadResponse uploadResponse = universalApi.uploadFile("/", sourceFile, destinationFile);
		Reporter.log(uploadResponse.getFileName() + " " + uploadResponse.getResponseMessage(), true);
		String fileId = uploadResponse.getFileId();
		
		createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
		boxActivityLog.setFileUploadLog(boxActivityLog.getFileUploadLog().replace("{filename}", destinationFile));
		BoxActivity fileUploadActivity = new BoxActivity(boxActivityLog.getFileUploadLog(), createdTime, Severity.informational.name(),  
					ObjectType.File.name(), ActivityType.Upload.name(), suiteData.getSaasAppUsername(), socUserName, ipInfo, fileId, destinationFile, "All Files");
		activityMap.put("FileUploadActivity", fileUploadActivity);
		
		
		sleep(CommonConstants.THIRTY_SECONDS_SLEEP);
		
		//Download the file
		universalApi.downloadFile(fileId, destinationFile);
		
		createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
		boxActivityLog.setFileDownloadLog(boxActivityLog.getFileDownloadLog().replace("{filename}", destinationFile));
		BoxActivity fileDownloadActivity = new BoxActivity(boxActivityLog.getFileDownloadLog(), createdTime, Severity.informational.name(),  
					ObjectType.File.name(), ActivityType.Download.name(), suiteData.getSaasAppUsername(), socUserName, ipInfo, fileId, destinationFile, "All Files");
		activityMap.put("FileDownloadActivity", fileDownloadActivity);
		
		sleep(CommonConstants.THIRTY_SECONDS_SLEEP);
		
		//Update the file
		universalApi.updateFile(uploadResponse.getFileId(), updateFile, uploadResponse.getEtag(), destinationFile);
		
		createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
		boxActivityLog.setFileEditLog(boxActivityLog.getFileEditLog().replace("{filename}", destinationFile));
		BoxActivity fileEditActivity = new BoxActivity(boxActivityLog.getFileEditLog(), createdTime, Severity.informational.name(),  
					ObjectType.File.name(), ActivityType.Edit.name(), suiteData.getSaasAppUsername(), socUserName, ipInfo, fileId, destinationFile, "All Files");
		activityMap.put("FileEditActivity", fileEditActivity);
		
		sleep(CommonConstants.THIRTY_SECONDS_SLEEP);
		
		// Commented as Box rename api is throwing 500 error
		//Rename the file
		universalApi.renameFile(fileId, renamedFile);
		createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
		boxActivityLog.setFileRenameLog(boxActivityLog.getFileRenameLog().replace("{filename}", renamedFile));
		BoxActivity fileRenameActivity = new BoxActivity(boxActivityLog.getFileRenameLog(), createdTime, Severity.informational.name(),  
					ObjectType.File.name(), ActivityType.Rename.name(), suiteData.getSaasAppUsername(), socUserName, ipInfo, fileId, renamedFile, "0");
		activityMap.put("FileRenameActivity", fileRenameActivity);
		
		sleep(CommonConstants.THIRTY_SECONDS_SLEEP);
		
		
		//Create shared link
		FileEntry sharedfile = universalApi.createDefaultSharedLink(fileId);
		createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
		boxActivityLog.setFileShareLog(boxActivityLog.getFileShareLog().replace("{filename}", renamedFile));
		BoxActivity fileShareActivity = new BoxActivity(boxActivityLog.getFileShareLog(), createdTime, Severity.informational.name(),  
					ObjectType.File.name(), ActivityType.Share.name(), suiteData.getSaasAppUsername(), socUserName, ipInfo, fileId, renamedFile, "0");
		activityMap.put("FileShareActivity", fileShareActivity);
		
		sleep(CommonConstants.THIRTY_SECONDS_SLEEP);

		//Unshare the link
		FileEntry unsharedfile = universalApi.disableSharedLink(fileId);
		createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
		boxActivityLog.setFileUnshareLog(boxActivityLog.getFileUnshareLog().replace("{filename}", renamedFile));
		BoxActivity fileUnshareActivity = new BoxActivity(boxActivityLog.getFileUnshareLog(), createdTime, Severity.informational.name(),  
					ObjectType.File.name(), ActivityType.Unshare.name(), suiteData.getSaasAppUsername(), socUserName, ipInfo, fileId, renamedFile, "0");
		activityMap.put("FileUnshareActivity", fileUnshareActivity);
		
		sleep(CommonConstants.THIRTY_SECONDS_SLEEP);

		//delete the file
		universalApi.deleteFile(unsharedfile.getId(), unsharedfile.getEtag());
		createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
		boxActivityLog.setFileDeleteLog(boxActivityLog.getFileDeleteLog().replace("{filename}", renamedFile));
		BoxActivity fileDeleteActivity = new BoxActivity(boxActivityLog.getFileDeleteLog(), createdTime, Severity.informational.name(),  
					ObjectType.File.name(), ActivityType.Delete.name(), suiteData.getSaasAppUsername(), socUserName, ipInfo, fileId, renamedFile, "0");
		activityMap.put("FileDeleteActivity", fileDeleteActivity);
		
		sleep(CommonConstants.THIRTY_SECONDS_SLEEP);

		//restore the file
		FileEntry bfile = universalApi.restoreFileFromTrash(fileId, renamedFile);
		createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
		boxActivityLog.setFileRestoreLog(boxActivityLog.getFileRestoreLog());
		BoxActivity fileRestoreActivity = new BoxActivity(boxActivityLog.getFileRestoreLog(), createdTime, Severity.informational.name(),  
					ObjectType.File.name(), ActivityType.Undelete.name(), suiteData.getSaasAppUsername(), socUserName, ipInfo, fileId, renamedFile, "0");
		activityMap.put("FileRestoreActivity", fileRestoreActivity);
		
		sleep(CommonConstants.THIRTY_SECONDS_SLEEP);

		//lock the file
		universalApi.lockFile(fileId, false);
		createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
		boxActivityLog.setFileLockLog(boxActivityLog.getFileLockLog().replace("{filename}", renamedFile));
		BoxActivity fileLockActivity = new BoxActivity(boxActivityLog.getFileLockLog(), createdTime, Severity.informational.name(),  
					ObjectType.File.name(), ActivityType.Lock.name(), suiteData.getSaasAppUsername(), socUserName, ipInfo, fileId, renamedFile, "0");
		activityMap.put("FileLockActivity", fileLockActivity);
		
		sleep(CommonConstants.THIRTY_SECONDS_SLEEP);

		//download the file after lock
		universalApi.downloadFile(fileId, destinationFile);
		
		sleep(CommonConstants.THIRTY_SECONDS_SLEEP);

		//unlock the file
		universalApi.unlockFile(fileId);
		createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
		boxActivityLog.setFileUnlockLog(boxActivityLog.getFileUnlockLog().replace("{filename}", renamedFile));
		BoxActivity fileUnlockActivity = new BoxActivity(boxActivityLog.getFileUnlockLog(), createdTime, Severity.informational.name(),  
					ObjectType.File.name(), ActivityType.Unlock.name(), suiteData.getSaasAppUsername(), socUserName, ipInfo, fileId, renamedFile, "0");
		activityMap.put("FileUnlockActivity", fileUnlockActivity);
		
		sleep(CommonConstants.THIRTY_SECONDS_SLEEP);

		//delete the file after restore
		universalApi.deleteFile(bfile.getId(), bfile.getEtag());
		
		sleep(CommonConstants.THIRTY_SECONDS_SLEEP);
		
		//purge the file
		universalApi.purgeFile(fileId);
		createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
		boxActivityLog.setFilePurgeLog(boxActivityLog.getFilePurgeLog().replace("{filename}", renamedFile));
		BoxActivity filePurgeActivity = new BoxActivity(boxActivityLog.getFilePurgeLog(), createdTime, Severity.informational.name(),  
					ObjectType.File.name(), ActivityType.Delete.name(), suiteData.getSaasAppUsername(), socUserName, ipInfo, fileId, renamedFile, "0");
		activityMap.put("FilePurgeActivity", filePurgeActivity);
		
		try {

			for (int i = 1; i <= (waitTime * 60 * 1000); i+=60000 ) {
				sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
				
				HashMap<String, String> termmap = new HashMap<String, String>();
				termmap.put("facility", facility.Box.name());
				termmap.put("Object_type", ObjectType.File.name());
				termmap.put("__source", "API");

				//Get user related logs
				fileLogs = this.getInvestigateLogs(-30, 10, facility.Box.name(), termmap, suiteData.getUsername(), suiteData.getApiserverHostName(), 
						suiteData.getCSRFToken(), suiteData.getSessionID(), 0, 500, facility.Box.name());			}
		}
		catch(Exception e) {}
		
		Reporter.log(MarshallingUtils.marshall(fileLogs), true);
		long total = fileLogs.getHits().getTotal();
		Reporter.log("Total file logs count:"+ total, true);
		
		//If total number of logs is less than zero, no logs are seen so skip the tests
		if (total <= 0) {
			throw new SkipException("Skipping the dependent tests..No logs are seen for files in Box");
		}
	}
	
	//@Test
	public void checkUpdateFile() throws Exception {
		String randomId = UUID.randomUUID().toString();
		String sourceFile = "Box_HIPAA_Test2.txt";
		String updateFile = "BE.txt";
		destinationFile = randomId + "_" + sourceFile;
		renamedFile     = randomId + "_renamed_" + sourceFile;
		query = randomId;

		//Upload the file
		FileUploadResponse uploadResponse = universalApi.uploadFile("/", sourceFile, destinationFile);
		Reporter.log(uploadResponse.getFileName() + " " + uploadResponse.getResponseMessage(), true);
		String fileId = uploadResponse.getFileId();

		sleep(CommonConstants.THIRTY_SECONDS_SLEEP);

		//Download the file
		universalApi.downloadFile(fileId, destinationFile);

		sleep(CommonConstants.THIRTY_SECONDS_SLEEP);

		//Update the file
		universalApi.updateFile(uploadResponse.getFileId(), updateFile, uploadResponse.getEtag(), destinationFile);
	}
	

	@Test(dependsOnMethods = "performSaasFileActivities", groups={"FILE", "P1"})
	public void verifyUploadFileActivity() {
		LogUtils.logTestDescription("After uploading the file, verify the upload activity in the activity logs.");
		logValidator.verifyActivityLog(fileLogs, activityMap.get("FileUploadActivity"));
	}
	
	@Test(dependsOnMethods = "performSaasFileActivities", groups={"FILE", "P1"})
	public void verifyUpdateFileActivity() {
		LogUtils.logTestDescription("After updating the file, verify the edit activity in the activity logs.");
		logValidator.verifyActivityLog(fileLogs, activityMap.get("FileEditActivity"));
	}
	
	@Test(dependsOnMethods = "performSaasFileActivities", groups={"FILE", "P1"})
	public void verifyDownloadActivity() {
		LogUtils.logTestDescription("After downloading the file, verify the download activity in the activity logs.");
		logValidator.verifyActivityLog(fileLogs, activityMap.get("FileDownloadActivity"));
	}

	@Test(dependsOnMethods = "performSaasFileActivities", groups={"FILE", "P1"})
	public void verifyRenameActivity() {
		LogUtils.logTestDescription("After renaming the file, verify the rename activity in the activity logs.");
		logValidator.verifyActivityLog(fileLogs, activityMap.get("FileRenameActivity"));
	}

	@Test(dependsOnMethods = "performSaasFileActivities", groups={"FILE", "P1"})
	public void verifyLockActivity() {
		LogUtils.logTestDescription("After locking the file, verify the lock activity in the activity logs.");
		logValidator.verifyActivityLog(fileLogs, activityMap.get("FileLockActivity"));
	}

	@Test(dependsOnMethods = "performSaasFileActivities", groups={"FILE", "P1"})
	public void verifyUnlockActivity() {
		LogUtils.logTestDescription("After unlocking the file, verify the unlock activity in the activity logs.");
		logValidator.verifyActivityLog(fileLogs, activityMap.get("FileUnlockActivity"));
	}

	@Test(dependsOnMethods = "performSaasFileActivities", groups={"FILE", "P1"})
	public void verifyFileSharedActivity() {
		LogUtils.logTestDescription("After sharing the file, verify the share activity in the activity logs.");
		logValidator.verifyActivityLog(fileLogs, activityMap.get("FileShareActivity"));
	}

	@Test(dependsOnMethods = "performSaasFileActivities", groups={"FILE", "P1"})
	public void verifyFileUnsharedActivity() {
		LogUtils.logTestDescription("After Unsharing the file, verify the unshare activity in the activity logs.");
		logValidator.verifyActivityLog(fileLogs, activityMap.get("FileUnshareActivity"));
	}

	@Test(dependsOnMethods = "performSaasFileActivities", groups={"FILE", "P1"})
	public void verifyDeleteActivity() {
		LogUtils.logTestDescription("After deleting the file, verify the delete activity in the activity logs.");
		logValidator.verifyActivityLog(fileLogs, activityMap.get("FileDeleteActivity"));
	}

	@Test(dependsOnMethods = "performSaasFileActivities", groups={"FILE", "P1"})
	public void verifyRestoreFromTrashActivity() {
		LogUtils.logTestDescription("After restoring the file, verify the restore activity in the activity logs.");
		logValidator.verifyActivityLog(fileLogs, activityMap.get("FileRestoreActivity"));
	}

	@Test(dependsOnMethods = "performSaasFileActivities")
	public void verifyPurgeActivity() {
		LogUtils.logTestDescription("After purging the file, verify the purge activity in the activity logs.");
		logValidator.verifyActivityLog(fileLogs, activityMap.get("FilePurgeActivity"));
	}


	@Test(priority = -10, groups={"FOLDER", "P1"})
	public void performSaasFolderActivities() throws Exception {
		String randomId = UUID.randomUUID().toString();
		String sourceFile = "test.pdf";
		uniqueFilename = randomId + "_" + sourceFile;
		uniqueFolder1   = randomId + "_folder1";
		uniqueFolder2   = randomId + "_folder2";
		uniqueFolder3   = randomId + "_folder3";
		String uniqueFolder4 = randomId + "_folder4";
		
		
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
		
		createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
		BoxActivity folderUploadActivity = new BoxActivity(boxActivityLog.getFolderUploadLog(), createdTime, Severity.informational.name(),  
															ObjectType.Folder.name(), ActivityType.Upload.name(), suiteData.getSaasAppUsername(), socUserName, 
															ipInfo, folderObj1.getId(), uniqueFolder1, "All Files");
		activityMap.put("FolderUploadActivity", folderUploadActivity);
		
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
		
		createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
		BoxActivity folderFileUploadActivity = new BoxActivity(boxActivityLog.getFolderFileUploadLog(), createdTime, Severity.informational.name(),  
															ObjectType.File.name(), ActivityType.Upload.name(), suiteData.getSaasAppUsername(), socUserName, 
															ipInfo, fileId, uniqueFilename, uniqueFolder1, folderObj1.getId(), "Document",  uploadResponse.getSize());
		activityMap.put("FolderFileUploadActivity", folderFileUploadActivity);
		sleep(CommonConstants.THIRTY_SECONDS_SLEEP);

		//download the file again
		universalApi.downloadFile(fileId, uniqueFilename);
		sleep(CommonConstants.THIRTY_SECONDS_SLEEP);
		
		//copy file to folder2
		Entry copiedFile = universalApi.copyFile(uploadResponse.getFileId(), folderId2);
		boxActivityLog.setFolderToFileCopyLog(boxActivityLog.getFolderToFileCopyLog().replace("{filename}", uploadResponse.getFileName()));
		
		createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
		BoxActivity folderFileCopyActivity = new BoxActivity(boxActivityLog.getFolderToFileCopyLog(), createdTime, Severity.informational.name(),  
															ObjectType.File.name(), ActivityType.Copy.name(), suiteData.getSaasAppUsername(), socUserName, 
															ipInfo, copiedFile.getId(), uniqueFilename, uniqueFolder2, folderObj2.getId(), "Document",  uploadResponse.getSize());
		activityMap.put("FolderFileCopyActivity", folderFileCopyActivity);
		
		sleep(CommonConstants.THIRTY_SECONDS_SLEEP);
		
		//create one more folder
		BoxFolder folderObj3 = universalApi.createFolder(uniqueFolder3);
		String folderId3 = folderObj3.getId();
		sleep(CommonConstants.THIRTY_SECONDS_SLEEP);
		
		//copy folder
		BoxFolder copiedFolder = universalApi.copyFolder(folderId2, folderId3);
		boxActivityLog.setFolderCopyLog(boxActivityLog.getFolderCopyLog().replace("{foldername}", uniqueFolder2));
		createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
		BoxActivity folderCopyActivity = new BoxActivity(boxActivityLog.getFolderCopyLog(), createdTime, Severity.informational.name(),  
															ObjectType.Folder.name(), ActivityType.Copy.name(), suiteData.getSaasAppUsername(), socUserName, 
															ipInfo, copiedFolder.getId(), uniqueFolder2, uniqueFolder3, folderObj3.getId(), null,  uploadResponse.getSize());
		activityMap.put("FolderCopyActivity", folderCopyActivity);
		
		sleep(CommonConstants.THIRTY_SECONDS_SLEEP);
		
		//update folder or renaming the folder
		
		BoxFolder folderObj4 = universalApi.createFolder(uniqueFolder4);
		String folderId4 = folderObj4.getId();
		sleep(CommonConstants.FIVE_MINUTES_SLEEP);
		
		String updatedFolderName4 = randomId +"_updated_folder4";
		universalApi.updateFolder(folderId4, updatedFolderName4);
		boxActivityLog.setFolderRenameLog(boxActivityLog.getFolderRenameLog().replace("{foldername}", uniqueFolder4)
																			 .replace("{updatedfoldername}", updatedFolderName4));
		createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
		BoxActivity folderRenameActivity = new BoxActivity(boxActivityLog.getFolderRenameLog(), createdTime, Severity.informational.name(),  
															ObjectType.Folder.name(), ActivityType.Rename.name(), suiteData.getSaasAppUsername(), socUserName, 
															ipInfo, folderId4, updatedFolderName4, null);
		activityMap.put("FolderRenameActivity", folderRenameActivity);
		
		sleep(CommonConstants.THIRTY_SECONDS_SLEEP);
		//Ten minutes time is introduced as delete after rename log message is not consistent
		
		String updatedFolderName3 = uniqueFolder3;
		
		//delete folder
		universalApi.deleteFolder(folderId3, true, null);
		boxActivityLog.setFolderDeleteLog(boxActivityLog.getFolderDeleteLog().replace("{foldername}", updatedFolderName3));
		createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
		BoxActivity folderDeleteActivity = new BoxActivity(boxActivityLog.getFolderDeleteLog(), createdTime, Severity.informational.name(),  
															ObjectType.Folder.name(), ActivityType.Delete.name(), suiteData.getSaasAppUsername(), socUserName, 
															ipInfo, folderId3, updatedFolderName3, null, null, null,  uploadResponse.getSize());
		activityMap.put("FolderDeleteActivity", folderDeleteActivity);
		
		sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
		
		//create shared link for folder
		BoxFolder sharedFolder = universalApi.createSharedLinkForFolder(folderId2);
		SharedLink folderSharedLink =  sharedFolder.getSharedLink();
		boxActivityLog.setFolderShareLog(boxActivityLog.getFolderShareLog().replace("{foldername}", uniqueFolder2));
		
		createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
		BoxActivity folderShareActivity = new BoxActivity(boxActivityLog.getFolderShareLog(), createdTime, Severity.informational.name(),  
															ObjectType.Folder.name(), ActivityType.Share.name(), suiteData.getSaasAppUsername(), socUserName, 
															ipInfo, folderId2, uniqueFolder2, "0");
		activityMap.put("FolderShareActivity", folderShareActivity);
		
		sleep(CommonConstants.THIRTY_SECONDS_SLEEP);
		
		//Set expiration date for sharing
		folderSharedLink.setUnsharedAt(DateUtils.getDaysFromCurrentTime(2));
		universalApi.createSharedLinkForFolder(folderId2, folderSharedLink);
		boxActivityLog.setFolderSharePermissionsLog(boxActivityLog.getFolderSharePermissionsLog().replace("{foldername}", uniqueFolder2));
		
		createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
		BoxActivity folderSharePermissionsActivity = new BoxActivity(boxActivityLog.getFolderSharePermissionsLog(), createdTime, Severity.informational.name(),  
															ObjectType.Folder.name(), ActivityType.Share.name(), suiteData.getSaasAppUsername(), socUserName, 
															ipInfo, folderId2, uniqueFolder2, "0");
		activityMap.put("FolderSharePermissionsActivity", folderSharePermissionsActivity);
		
		sleep(CommonConstants.THIRTY_SECONDS_SLEEP);

		//disable shared link for folder
		universalApi.disableSharedLinkForFolder(folderId2);
		boxActivityLog.setFolderUnshareLog(boxActivityLog.getFolderUnshareLog().replace("{foldername}", uniqueFolder2));
		createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
		BoxActivity folderUnshareActivity = new BoxActivity(boxActivityLog.getFolderUnshareLog(), createdTime, Severity.informational.name(),  
															ObjectType.Folder.name(), ActivityType.Unshare.name(), suiteData.getSaasAppUsername(), socUserName, 
															ipInfo, folderId2, uniqueFolder2, "0");
		activityMap.put("FolderUnshareActivity", folderUnshareActivity);
		
		sleep(CommonConstants.THIRTY_SECONDS_SLEEP);
		
		//restore folder from trash
		universalApi.restoreFolder(folderId3, updatedFolderName3);
		boxActivityLog.setFolderRestoreLog(boxActivityLog.getFolderRestoreLog().replace("{foldername}", uniqueFolder3));
		createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
		BoxActivity folderRestoreActivity = new BoxActivity(boxActivityLog.getFolderRestoreLog(), createdTime, Severity.informational.name(),  
															ObjectType.Folder.name(), ActivityType.Undelete.name(), suiteData.getSaasAppUsername(), socUserName, 
															ipInfo, folderId3, updatedFolderName3, "0");
		activityMap.put("FolderRestoreActivity", folderRestoreActivity);
		
		sleep(CommonConstants.THIRTY_SECONDS_SLEEP);
		
		//move folder
		universalApi.moveFolder(folderId1, folderId3);
		sleep(CommonConstants.THIRTY_SECONDS_SLEEP);
		boxActivityLog.setFolderMoveLog(boxActivityLog.getFolderMoveLog().replace("{foldername}", uniqueFolder1));
		
		createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
		BoxActivity folderMoveActivity = new BoxActivity(boxActivityLog.getFolderMoveLog(), createdTime, Severity.informational.name(),  
															ObjectType.Folder.name(), ActivityType.Move.name(), suiteData.getSaasAppUsername(), socUserName, 
															ipInfo, folderId1, uniqueFolder1, updatedFolderName3, "0", null, 0);
		
		activityMap.put("FolderMoveActivity", folderMoveActivity);
		
		boxActivityLog.setFolderUnsyncLog(boxActivityLog.getFolderUnsyncLog().replace("{foldername}", uniqueFolder1));
		BoxActivity folderUnsyncActivity = new BoxActivity(boxActivityLog.getFolderUnsyncLog(), createdTime, Severity.informational.name(),  
													ObjectType.Folder.name(), ActivityType.Unsync.name(), suiteData.getSaasAppUsername(), socUserName, 
													ipInfo, folderId1, uniqueFolder1, updatedFolderName3);
		activityMap.put("FolderUnsyncActivity", folderUnsyncActivity);
		
		try {

			for (int i = 1; i <= (waitTime * 60 * 1000); i+=60000 ) {
				sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
				
				HashMap<String, String> termmap = new HashMap<String, String>();
				termmap.put("facility", facility.Box.name());
				termmap.put("Object_type", ObjectType.File.name());
				termmap.put("__source", "API");
				
				//Get file related logs
				ForensicSearchResults fsr = this.getInvestigateLogs(-25, 10, facility.Box.name(), termmap, suiteData.getUsername(), suiteData.getApiserverHostName(), 
						suiteData.getCSRFToken(), suiteData.getSessionID(), 0, 100, facility.Box.name());

				termmap.put("Object_type", ObjectType.Folder.name());

				//Get folder related logs
				folderLogs = this.getInvestigateLogs(-25, 5, facility.Box.name(), termmap, suiteData.getUsername(), suiteData.getApiserverHostName(), 
						suiteData.getCSRFToken(), suiteData.getSessionID(), 0, 200, facility.Box.name());

				//merge both the logs
				folderLogs.getHits().getHits().addAll(fsr.getHits().getHits());
			}
		}
		catch(Exception e) {}
		
		Reporter.log(MarshallingUtils.marshall(folderLogs), true);
		long total = folderLogs.getHits().getTotal();
		Reporter.log("Total folder logs count:"+ total, true);
		
		//If total number of logs is less than zero, no logs are seen so skip the tests
		if (total <= 0) {
			throw new SkipException("Skipping the dependent tests..No logs are seen for folders in Box");
		}
		
		//Cleanup
		folderObj1 = universalApi.getFolderInfo(folderObj1.getId());
		//universalApi.deleteFolder(folderObj1.getId(), true, folderObj1.getEtag());
		universalApi.deleteFolder(folderObj1.getId(), true, "*");


		folderObj2 = universalApi.getFolderInfo(folderObj2.getId());
		//universalApi.deleteFolder(folderObj2.getId(), true, folderObj2.getEtag());
		universalApi.deleteFolder(folderObj2.getId(), true, "*");

		folderObj3 = universalApi.getFolderInfo(folderObj3.getId());
		//universalApi.deleteFolder(folderObj3.getId(), true, folderObj3.getEtag());
		universalApi.deleteFolder(folderObj3.getId(), true, "*");
	}
	

	//Folder related operations

	@Test(groups={"FOLDER", "P1"})
	public void verifyCreateFolder() {
		LogUtils.logTestDescription("After creating the folder, verify the folder upload activity in the activity logs.");
		logValidator.verifyActivityLog(folderLogs, activityMap.get("FolderUploadActivity"));
	}

	@Test(groups={"FOLDER", "P1"})
	public void verifySharedFolder() {
		LogUtils.logTestDescription("After sharing the folder, verify the folder share activity in the activity logs.");
		logValidator.verifyActivityLog(folderLogs, activityMap.get("FolderShareActivity"));
	}

	@Test(groups={"FOLDER", "P1"})
	public void verifyDisableSharedFolder() {
		LogUtils.logTestDescription("After unsharing the folder, verify the folder unshare activity in the activity logs.");
		logValidator.verifyActivityLog(folderLogs, activityMap.get("FolderUnshareActivity"));
	}

	@Test(groups={"FOLDER", "P1"})
	public void verifyDeleteFolder() {
		LogUtils.logTestDescription("After deleting the folder, verify the folder delete activity in the activity logs.");
		logValidator.verifyActivityLog(folderLogs, activityMap.get("FolderDeleteActivity"));
	}

	@Test(groups={"FOLDER", "P1"})
	public void verifyRestoreFolder() {
		LogUtils.logTestDescription("After restoring the folder, verify the folder restore activity in the activity logs.");
		logValidator.verifyActivityLog(folderLogs, activityMap.get("FolderRestoreActivity"));
	}
	
	@Test(groups={"FOLDER", "P1"})
	public void verifyCopyFolder() {
		LogUtils.logTestDescription("After copying the folder, verify the folder copy activity in the activity logs.");
		logValidator.verifyActivityLog(folderLogs, activityMap.get("FolderCopyActivity"));
	}
	

	@Test(groups={"FOLDER", "P1"})
	public void verifyCopyFilesToFolder() {
		LogUtils.logTestDescription("After copying the folder, verify the folder file copy activity in the activity logs.");
		logValidator.verifyActivityLog(folderLogs, activityMap.get("FolderFileCopyActivity"));
	}

	@Test(groups={"FOLDER", "P1"})
	public void verifyUploadFileToFolder() {
		LogUtils.logTestDescription("After uploading file to the folder, verify the folder file upload activity in the activity logs.");
		logValidator.verifyActivityLog(folderLogs, activityMap.get("FolderFileUploadActivity"));
		
		
	}


	@Test(groups={"FOLDER", "P1"})
	public void verifyMoveFolder() {
		LogUtils.logTestDescription("After moving the folder, verify the folder move activity in the activity logs.");
		logValidator.verifyActivityLog(folderLogs, activityMap.get("FolderMoveActivity"));
	}

	@Test(groups={"FOLDER", "P1"})
	public void verifyUnsyncFolder() {
		LogUtils.logTestDescription("After unsync the folder, verify the folder unsync activity in the activity logs.");
		logValidator.verifyActivityLog(folderLogs, activityMap.get("FolderUnsyncActivity"));
	}
	
	@Test(groups={"FOLDER", "P1"})
	public void verifyRenameFolder() {
		LogUtils.logTestDescription("After renaming the folder, verify the folder rename activity in the activity logs.");
		logValidator.verifyActivityLog(folderLogs, activityMap.get("FolderRenameActivity"));
	}
	
	
	@Test(dataProviderClass = BoxDataProvider.class, dataProvider = "I18NString", priority=-1, groups={"I18N", "P3", "I18N_FILE"})
	public void performSaasI18NFileActivities(String locale, String i18nString) throws Exception {
		BoxActivityLog boxActivityLogI18N = new BoxActivityLog(); 
		Reporter.log("Upload a file with the language::"+locale, true );
		String randomId = UUID.randomUUID().toString();
		String sourceFile = "Box_HIPAA_Test2.txt";
		String updateFile = "BE.txt";
		this.destinationI18NFile = i18nString + "_" + randomId + "_" + sourceFile;
		this.renamedI18NFile     = i18nString + "_renamed_" + randomId + "_" +sourceFile;
		query = randomId;

		//Upload the file
		FileUploadResponse uploadResponse = universalApi.uploadFile("/", sourceFile, destinationI18NFile);
		Reporter.log(uploadResponse.getFileName() + " " + uploadResponse.getResponseMessage(), true);
		String fileId = uploadResponse.getFileId();
		
		createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
		boxActivityLogI18N.setI18NFileUploadLog(boxActivityLogI18N.getI18NFileUploadLog().replace("{filename}", destinationI18NFile));
		BoxActivity fileUploadActivity = new BoxActivity(boxActivityLogI18N.getI18NFileUploadLog(), createdTime, Severity.informational.name(),  
					ObjectType.File.name(), ActivityType.Upload.name(), suiteData.getSaasAppUsername(), socUserName, ipInfo, fileId, destinationI18NFile, "All Files");
		activityMap.put(locale + "I18NFileUploadActivity", fileUploadActivity);
		
		
		sleep(CommonConstants.THIRTY_SECONDS_SLEEP);
		
		//Download the file
		universalApi.downloadFile(fileId, destinationI18NFile);
		
		createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
		boxActivityLogI18N.setI18NFileDownloadLog(boxActivityLogI18N.getI18NFileDownloadLog().replace("{filename}", destinationI18NFile));
		BoxActivity fileDownloadActivity = new BoxActivity(boxActivityLogI18N.getI18NFileDownloadLog(), createdTime, Severity.informational.name(),  
					ObjectType.File.name(), ActivityType.Download.name(), suiteData.getSaasAppUsername(), socUserName, ipInfo, fileId, destinationI18NFile, "All Files");
		activityMap.put(locale +"I18NFileDownloadActivity", fileDownloadActivity);
		
		sleep(CommonConstants.THIRTY_SECONDS_SLEEP);
		
		//Create a public shared link
		universalApi.createDefaultSharedLink(fileId);
		
	}
	
	@Test(dependsOnMethods = "performSaasI18NFileActivities", priority=-1, groups={"I18N_FILE", "I18N", "P3"})
	public void fetchI18NLogs() throws Exception {
		
		try {

			for (int i = 1; i <= (waitTime * 60 * 1000); i+=60000 ) {
				sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
				
				HashMap<String, String> termmap = new HashMap<String, String>();
				termmap.put("facility", facility.Box.name());
				termmap.put("Object_type", ObjectType.File.name());
				termmap.put("__source", "API");

				//Get user related logs
				file18nLogs = this.getInvestigateLogs(-30, 10, facility.Box.name(), termmap, suiteData.getUsername(), suiteData.getApiserverHostName(), 
						suiteData.getCSRFToken(), suiteData.getSessionID(), 0, 400, facility.Box.name());			}
		}
		catch(Exception e) {}
		
		Reporter.log(MarshallingUtils.marshall(fileLogs), true);
		long total = file18nLogs.getHits().getTotal();
		Reporter.log("Total file logs count:"+ total, true);
		
		//If total number of logs is less than zero, no logs are seen so skip the tests
		if (total <= 0) {
			throw new SkipException("Skipping the dependent tests..No logs are seen for files in Box");
		}
	}
	
	
	@Test(dependsOnMethods = "fetchI18NLogs", priority=-1, dataProviderClass = BoxDataProvider.class, dataProvider = "I18NString", groups={"I18N_FILE", "I18N", "P3"})
	public void verifyI18NUploadFileActivity(String locale, String i18nString) {
		LogUtils.logTestDescription("After uploading the file, verify the upload activity in the activity logs.");
		logValidator.verifyActivityLog(file18nLogs, activityMap.get(locale+ "I18NFileUploadActivity"));
	}
	
	@Test(dependsOnMethods = "fetchI18NLogs", priority=-1, dataProviderClass = BoxDataProvider.class, dataProvider = "I18NString", groups={"I18N_FILE", "I18N", "P3"})
	public void verifyI18NDownloadActivity(String locale, String i18nString) {
		LogUtils.logTestDescription("After downloading the file, verify the download activity in the activity logs.");
		logValidator.verifyActivityLog(file18nLogs, activityMap.get(locale + "I18NFileDownloadActivity"));
	}
	
	@Test(dependsOnMethods = "fetchI18NLogs", priority=-1, dataProviderClass = BoxDataProvider.class, dataProvider = "I18NString", groups={"I18N_FILE", "I18N", "P3"})
	public void verifyI18NActivitySearch(String locale, String i18nString) throws Exception {
		LogUtils.logTestDescription("Search activity with I18N string.");
		ArrayList<String> logs = new ArrayList<String>();
		String apiHost = suiteData.getScheme() + "://" + suiteData.getApiserverHostName();
		logs = searchDisplayLogs(-30, 5, "Box", i18nString, suiteData.getUsername(), apiHost, suiteData.getCSRFToken(), suiteData.getSessionID());
		
		String message = activityMap.get(locale + "I18NFileDownloadActivity").getMessage();
		CustomAssertion.assertTrue(logs.contains(message), message + " is present", message + " is not present");
	}
	
	@Test(dependsOnMethods = "fetchI18NLogs", priority=-1, dataProviderClass = BoxDataProvider.class, dataProvider = "I18NString", groups={"I18N_FILE", "I18N", "P3"})
	public void verifyExposedFileSearch(String locale, String i18nString) throws Exception {
		UIExposedDoc payload = this.getUIPayload("Box", null, null, null, 0, 0, true, URLEncoder.encode(i18nString, "UTF-8"), "name");
		SecurletDocument exposedDocs = getUIExposedDocuments(MarshallingUtils.marshall(payload), null);
		CustomAssertion.assertTrue(exposedDocs.getMeta().getTotalCount() > 0, "Expected doc count is greater than one ", "Expected doc count is zero");
	}
	
		
	
	@Test(priority= -10, groups={"FILE", "CONTENT_INSPECTION", "BOX", "SANITY", "REGRESSSION"})
	public void uploadRiskFilesCheckTheLogs() throws Exception {
		LogUtils.logTestDescription("Upload PII, PCI and HIPAA risks and check the activity logs for CI logs");
		
		//filename
		String randomId = UUID.randomUUID().toString();
		
		String pciFile = "pci.txt";
		String piiFile = "pii.rtf";
		String hipaaFile = "hipaa.txt";
		
		String pciFilepath = DCIConstants.DCI_FILE_UPLOAD_RISK_TYPES_PATH + File.separator + pciFile;
		String piiFilepath = DCIConstants.DCI_FILE_UPLOAD_RISK_TYPES_PATH + File.separator + piiFile;
		String hipaaFilepath = DCIConstants.DCI_FILE_UPLOAD_RISK_TYPES_PATH + File.separator + hipaaFile;
		
		String pciDestFile 		= randomId + "_" + "pci.txt";
		String piiDestFile 		= randomId + "_" + "pii.rtf";
		String hipaaDestFile 	= randomId + "_" + "hipaa.txt";
		
		//Upload the file
		FileUploadResponse pciUploadResponse = universalApi.uploadFile("/", 	pciFilepath, 	pciDestFile);
		FileUploadResponse piiUploadResponse = universalApi.uploadFile("/", 	piiFilepath, 	piiDestFile);
		FileUploadResponse hipaaUploadResponse = universalApi.uploadFile("/", 	hipaaFilepath, 	hipaaDestFile);
		
		createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
		boxActivityLog.setPiiRiskLog(boxActivityLog.getPiiRiskLog().replace("{filename}", piiDestFile)
																		.replace("{risktype}", "PII"));
		BoxActivity PIIRiskLog = new BoxActivity(boxActivityLog.getPiiRiskLog(), createdTime, Severity.critical.name(),  
				ObjectType.File.name(), "Content Inspection", suiteData.getSaasAppUsername(), socUserName, ipInfo, piiUploadResponse.getFileId(), destinationFile, "All Files");
		activityMap.put("PIIRiskLog", PIIRiskLog);


		boxActivityLog.setPciRiskLog(boxActivityLog.getPciRiskLog().replace("{filename}", pciDestFile)
																		.replace("{risktype}", "PCI"));
		BoxActivity PCIRiskLog = new BoxActivity(boxActivityLog.getPciRiskLog(), createdTime, Severity.critical.name(),  
				ObjectType.File.name(), "Content Inspection", suiteData.getSaasAppUsername(), socUserName, ipInfo, pciUploadResponse.getFileId(), destinationFile, "All Files");
		activityMap.put("PCIRiskLog", PCIRiskLog);

		boxActivityLog.setHipaaRiskLog(boxActivityLog.getHipaaRiskLog().replace("{filename}", hipaaDestFile)
																		.replace("{risktype}", "PII, HIPAA"));
		BoxActivity HIPAARiskLog = new BoxActivity(boxActivityLog.getHipaaRiskLog(), createdTime, Severity.critical.name(),  
				ObjectType.File.name(), "Content Inspection", suiteData.getSaasAppUsername(), socUserName, ipInfo, hipaaUploadResponse.getFileId(), destinationFile, "All Files");
		activityMap.put("HIPAARiskLog", HIPAARiskLog);

			
		//Wait for 5 mins
		sleep(CommonConstants.FIVE_MINUTES_SLEEP);
		
		try {

			for (int i = 1; i <= (waitTime * 60 * 1000); i+=60000 ) {
				sleep(CommonConstants.SIXTY_SECONDS_SLEEP);

				HashMap<String, String> termmap = new HashMap<String, String>();
				termmap.put("facility", facility.Box.name());
				termmap.put("Activity_type", "Content Inspection");
				this.contentInspectionLogs = this.getInvestigateLogs(-20, 10, facility.Box.name(), termmap, suiteData.getUsername(), suiteData.getApiserverHostName(), 
						suiteData.getCSRFToken(), suiteData.getSessionID(), 0, 200, facility.Box.name());						
			}
		}
		catch(Exception e) {}

		Reporter.log(MarshallingUtils.marshall(contentInspectionLogs), true);
		long total = contentInspectionLogs.getHits().getTotal();
		Reporter.log("Total file logs count:"+ total, true);

		//If total number of logs is less than zero, no logs are seen so skip the tests
		if (total <= 0) {
			throw new SkipException("Skipping the dependent tests..No logs are seen for content inspection");
		}

		//purge the file
		universalApi.purgeFile(pciUploadResponse.getFileId());
		universalApi.purgeFile(piiUploadResponse.getFileId());
		universalApi.purgeFile(hipaaUploadResponse.getFileId());
		
	}

	@Test(groups={"FILE", "CONTENT_INSPECTION", "P1"})
	public void verifyPIIRisk() {
		LogUtils.logTestDescription("After uploading the PII risk, verify the pii risk activity in the activity logs.");
		logValidator.verifyCIActivityLog(contentInspectionLogs, activityMap.get("PIIRiskLog"));
	}
	
	@Test(groups={"FILE", "CONTENT_INSPECTION", "P1"})
	public void verifyPCIRisk() {
		LogUtils.logTestDescription("After uploading the PCI risk, verify the pii risk activity in the activity logs.");
		logValidator.verifyCIActivityLog(contentInspectionLogs, activityMap.get("PCIRiskLog"));
	}
	
	@Test(groups={"FILE", "CONTENT_INSPECTION", "P1"})
	public void verifyHIPAARisk() {
		LogUtils.logTestDescription("After uploading the PII risk, verify the pii risk activity in the activity logs.");
		logValidator.verifyCIActivityLog(contentInspectionLogs, activityMap.get("HIPAARiskLog"));
	}
	
	@Test(groups={"DEDUPE", "P1"})
	public void verifyDuplicateActivities() throws Exception {
		String randomId = UUID.randomUUID().toString();
		String sourceFile = "test.pdf";
		String uploadedFile = randomId + "_" + sourceFile;
		String uploadedFolder   = randomId + "_dupefolder";
		String folderQuery = randomId;

		//Upload the file
		FileUploadResponse uploadResponse = universalApi.uploadFile("/", sourceFile, uploadedFile);
		Reporter.log(uploadResponse.getFileName() + " " + uploadResponse.getResponseMessage(), true);
		sleep(CommonConstants.THIRTY_SECONDS_SLEEP);
		
		//create folder
		BoxFolder folderObj1 = universalApi.createFolder(uploadedFolder);
		String folderId1 = folderObj1.getId();
		sleep(CommonConstants.THIRTY_SECONDS_SLEEP);
		
		//upload the file to folder
		uploadResponse = universalApi.uploadFile(folderId1, sourceFile, uploadedFile);
		String fileId = uploadResponse.getFileId();
		sleep(CommonConstants.THIRTY_SECONDS_SLEEP);
		
		//Cleanup
		folderObj1 = universalApi.getFolderInfo(folderObj1.getId());
		universalApi.deleteFolder(folderObj1.getId(), true, folderObj1.getEtag());
		
		sleep(CommonConstants.FIVE_MINUTES_SLEEP);
		
		ArrayList<String> logs;
		String apiHost = suiteData.getScheme() + "://" + suiteData.getApiserverHostName();
		logs = searchDisplayLogs(-30, 20, "Box", folderQuery, suiteData.getSaasAppUsername(), apiHost, suiteData.getCSRFToken(), suiteData.getSessionID());
		
		//User uploaded ef131277-2b13-4d94-a936-b4d54a47ff30_test.pdf to All Files
		//User uploaded ef131277-2b13-4d94-a936-b4d54a47ff30_dupefolder to All Files,
		String uploadFileActivity = "User uploaded " + uploadedFile + " to All Files";
		String uploadFolderActivity = "User uploaded " + uploadedFolder + " to All Files";
		String uploadFileToFolder = "User uploaded " + uploadedFile + " to " + uploadedFolder;
		
		Set<String> uniqueSet = new HashSet<String>(logs);
		for (String activity : uniqueSet) {
			Reporter.log(activity + "::" + Collections.frequency(logs, activity), true);
			if(activity.equals(uploadFileActivity)) {
				CustomAssertion.assertEquals(Collections.frequency(logs, activity), 1, "Duplicate file upload activity found");
			}
			
			if(activity.equals(uploadFolderActivity)) {
				CustomAssertion.assertEquals(Collections.frequency(logs, activity), 1, "Duplicate folder upload activity found");
			}
			
			if(activity.equals(uploadFileToFolder)) {
				CustomAssertion.assertEquals(Collections.frequency(logs, activity), 1, "Duplicate file upload to folder activity found");
			}
		}
	}
	
	
	@Test(dataProviderClass = BoxDataProvider.class, dataProvider = "ObjectTypeFilter", groups={"FILTERS", "BOX", "REGRESSION", "P1"})
	public void dashboardObjectTypeFilters(String objType) throws Exception {
		ForensicSearchResults logs;
		HashMap<String, String> termmap = new HashMap<String, String>();
		termmap.put("facility", "Box");
		termmap.put("Object_type", objType);
		LogUtils.logTestDescription("Retrieve the objecttype and filter them by name:"+ objType);
			for (int retry = 0; retry < 1; retry++) {

				try{
					String apiHost = suiteData.getScheme() + "://" + suiteData.getApiserverHostName();
					//Fetch the activity logs from yesterday to tomorrow and limited to 500
					//Get file related logs
					logs = getInvestigateLogs(-18000, 10, "Box", termmap, suiteData.getUsername().toLowerCase(), 
							apiHost, suiteData.getCSRFToken(), suiteData.getSessionID(), 0, 500, "Box");

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
	
	
	@Test(dataProviderClass = BoxDataProvider.class, dataProvider = "ActivityTypeFilter",groups={"FILTERS", "BOX", "REGRESSION", "P1"})
	public void dashboardActivityTypeFilters(String activityType) throws Exception {
		ForensicSearchResults logs;
		LogUtils.logTestDescription("Retrieve the activities and filter them by name:"+ activityType);
		HashMap<String, String> termmap = new HashMap<String, String>();
		termmap.put("facility", "Box");
		termmap.put("Activity_type", activityType);
		
		for (int retry = 0; retry < 1; retry++) {

			try{
				String apiHost = suiteData.getScheme() + "://" + suiteData.getApiserverHostName();
				//Fetch the activity logs from yesterday to tomorrow and limited to 500
				//Get file related logs
				logs = getInvestigateLogs(-18000, 10, "Box", termmap, suiteData.getUsername().toLowerCase(), 
						apiHost, suiteData.getCSRFToken(), suiteData.getSessionID(), 0, 500, "Box");

				this.logValidator = new LogValidator(logs);

				logValidator.verifyMetadata();
				logValidator.verifyActivityType(activityType);
				assertTrue(logs.getHits().getTotal() > 0, "ActivityType " + activityType + " related messages are not present");
			}
			catch(Exception e) {}
		}
	}
	
	
	@Test(dataProviderClass = BoxDataProvider.class, dataProvider = "SeverityFilter",groups={"FILTERS", "BOX", "REGRESSION", "P1"})
	public void dashboardSeverityTypeFilters(String severityType) throws Exception {
		ForensicSearchResults logs;
		LogUtils.logTestDescription("Retrieve the activities and filter them by name:"+ severityType);
		HashMap<String, String> termmap = new HashMap<String, String>();
		termmap.put("facility", "Box");
		termmap.put("severity", severityType);
		termmap.put("__source", "API");
		//termmap.put("user", this.saasAppUserAccount.getUsername());
		
		for (int retry = 0; retry < 1; retry++) {

			try{
				String apiHost = suiteData.getScheme() + "://" + suiteData.getApiserverHostName();
				//Fetch the activity logs from yesterday to tomorrow and limited to 500
				//Get file related logs
				logs = getInvestigateLogs(-180000, 10, "Box", termmap, suiteData.getUsername().toLowerCase(), 
						apiHost, suiteData.getCSRFToken(), suiteData.getSessionID(), 0, 500, "Box");

				this.logValidator = new LogValidator(logs);

				logValidator.verifyMetadata();
				logValidator.verifySeverityType(severityType);
				assertTrue(logs.getHits().getTotal() > 0, "SeverityType " + severityType + " related messages are not present");
			}
			catch(Exception e) {}
		}
	}
	
	
	@Test(dataProviderClass = BoxDataProvider.class, dataProvider = "LocationFilter",groups={"FILTERS", "BOX", "REGRESSION", "P1"})
	public void dashboardLocationTypeFilters(String severityType, String location) throws Exception {
		ForensicSearchResults logs;
		LogUtils.logTestDescription("Retrieve the activities and filter them by name:"+ severityType +" and location:"+location);
		HashMap<String, String> termmap = new HashMap<String, String>();
		termmap.put("facility", "Box");
		termmap.put("severity", severityType);
		termmap.put("__source", "API");
		termmap.put("location", location);
		
		termmap.put("user", this.saasAppUserAccount.getUsername());
		
		for (int retry = 0; retry < 1; retry++) {

			try{
				String apiHost = suiteData.getScheme() + "://" + suiteData.getApiserverHostName();
				//Fetch the activity logs from yesterday to tomorrow and limited to 500
				//Get file related logs
				logs = getInvestigateLogs(-18000, 10, "Box", termmap, suiteData.getUsername().toLowerCase(), 
						apiHost, suiteData.getCSRFToken(), suiteData.getSessionID(), 0, 500, "Box");

				this.logValidator = new LogValidator(logs);

				logValidator.verifyMetadata();
				logValidator.verifySeverityType(severityType);
				assertTrue(logs.getHits().getTotal() > 0, "SeverityType " + severityType + " related messages are not present");
			}
			catch(Exception e) {}
		}
	}
	
	
	@Test(groups={"FILTERS", "BOX", "REGRESSION", "P1", "ANONYMIZATION"})
	public void verifyAnonymizedLogs() throws Exception {
		String[] steps = { "1. Turn on the anonymization and get the investigate logs.",
		           "2. Verify the username in message is anonymized.",
		           "3. Turn off the anonymizarion." };
		LogUtils.logTestDescription(steps);
		
		String tenantAcctId = getTenantAccountId();
		String payload = "{\"userAnonymization\":true,\"dpoName\": \"\", \"dpoPassword\": \"\",\"id\":\""+tenantAcctId+"\"}";
		Reporter.log("Payload:"+ payload, true);
		String responseBody = this.updateUserAnonymization(payload);
		
		//Invalidate the session after anonymization turned
		Reporter.log("Regenerate the session after anonymization turned on...:", true);
		this.regenerateSession();
		
		ForensicSearchResults logs;
		
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
		
		//Remove anonymization
		payload = "{\"userAnonymization\":false,\"dpoName\":\""+ suiteData.getDpoUsername() +"\",\"dpoPassword\":\""+ suiteData.getDpoPassword() +"\", \"id\":\""+tenantAcctId+"\"}";
		Reporter.log("Payload:"+ payload, true);
		responseBody = this.updateUserAnonymization(payload);
		Reporter.log("Response body:"+ responseBody, true);
		
		Reporter.log("Regenerate the session after anonymization turned off...:", true);
		this.regenerateSession();
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
	
	
	@Test(groups={"FILTERS", "HISTORY", "BOX", "REGRESSION", "P1"})
	public void verifyHistoryLogs() throws Exception {
		String[] steps = 
				 { "1. Retrieve the history logs for the term elastica.",
		           "2. Verify the login and logout."
				 };
		LogUtils.logTestDescription(steps);
		//Invalidate the user session and do login
		universalApi.logoutUser();
		//Invalidate the user session and do login again
		universalApi.logoutUser();
		
		ForensicSearchResults logs;
		HashMap<String, String> termmap = new HashMap<String, String>();
		termmap.put("facility", "Elastica");
		
		//Sleep for 5 mins for the logs to be available
		sleep(CommonConstants.THREE_MINUTES_SLEEP);
		
		for (int retry = 0; retry < 1; retry++) {

			try{
				String apiHost = suiteData.getScheme() + "://" + suiteData.getApiserverHostName();
				//Fetch the history activity logs from yesterday to tomorrow and limited to 500
				logs = getInvestigateLogs(-5, 5, "Elastica", termmap, suiteData.getUsername().toLowerCase(), 
						apiHost, suiteData.getCSRFToken(), suiteData.getSessionID(), 0, 20, "history");

				this.logValidator = new LogValidator(logs);

				logValidator.verifyMetadata();
				
				String expectedMessage = "User '"+ suiteData.getUsername().toLowerCase() +"' logged in";
				logValidator.verifyHistoryLogs(expectedMessage);
				
			}
			catch(Exception e) {}
		}
	}
	
	@Test(groups={"EXPORT", "BOX", "REGRESSION", "P1"})
	public void checkCSVExportOfExposedFiles() throws Exception {
		LogUtils.logTestDescription("Export the exposed files to user email and check");
		List<NameValuePair> headers = getHeaders();
		
		String path = suiteData.getAPIMap().get("getUIExportCSV") ;
		//String path = "/admin/application/list/export_exposures_data"; 
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(), path);
		Reporter.log("URI ::"+dataUri.toString(), true);

		String payload = "{\"source\":{\"limit\":20,\"offset\":0,\"isInternal\":true,\"searchTextFromTable\":\"\",\"orderBy\":\"name\",\"exportType\":\"docs\",\"app\":\"Box\"}}";
		HttpResponse response =  restClient.doPost(dataUri, headers, null, new StringEntity(payload));

		String responseBody = ClientUtil.getResponseBody(response);
		Reporter.log("Response body:"+ responseBody, true);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
		CustomAssertion.assertTrue(responseBody.contains("true"), "Filelink sent to email", "File not sent");
		CustomAssertion.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_OK, "Not exported");

		sleep(CommonConstants.THIRTY_SECONDS_SLEEP);
		
		String query = "Box Securlet Data Export";
		
		String expectedAttachment = "Box_securlet_docs_" + DateUtils.getCurrentDate();
		
		Map<String, byte[]> attachments = this.getEmailAttachment(query, null, null, null);
		for (Map.Entry<String, byte[]> entrySet : attachments.entrySet()) {
        	String key = entrySet.getKey();
        	byte[] value = entrySet.getValue();
        	Reporter.log("Attachment name:"+ key, true);
        	Reporter.log("Attachment size:"+ value.length, true);
        	CustomAssertion.assertTrue(value.length > 0, key + " size is "+value.length, key + "size is zero" );
        	CustomAssertion.assertTrue(key.startsWith(expectedAttachment), key+ " Starts with "+ expectedAttachment, "Attachment name don't match");
        }
	}
	
	@Test(groups={"EXPORT", "BOX", "REGRESSION", "P1"})
	public void checkCSVExportOfExposedUsers() throws Exception {
		LogUtils.logTestDescription("Export the exposed users to user email and check");
		List<NameValuePair> headers = getHeaders();
		String path = suiteData.getAPIMap().get("getUIExportCSV") ;
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(), path);
		Reporter.log("URI ::"+dataUri.toString(), true);

		String payload = "{\"source\":{\"limit\":100,\"offset\":0,\"isInternal\":true,\"searchTextFromTable\":\"\",\"exportType\":\"users\",\"requestType\":\"users\",\"app\":\"Box\"}}";
		HttpResponse response =  restClient.doPost(dataUri, headers, null, new StringEntity(payload));

		String responseBody = ClientUtil.getResponseBody(response);
		Reporter.log("Response body:"+ responseBody, true);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
		CustomAssertion.assertTrue(responseBody.contains("true"), "Filelink sent to email", "File not sent");
		CustomAssertion.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_OK, "Not exported");
		
		sleep(CommonConstants.THIRTY_SECONDS_SLEEP);
		
		String query = "Box Securlet Data Export";
		
		String expectedAttachment = "Box_securlet_users_" + DateUtils.getCurrentDate();
		
		Map<String, byte[]> attachments = this.getEmailAttachment(query, null, null, null);
		for (Map.Entry<String, byte[]> entrySet : attachments.entrySet()) {
        	String key = entrySet.getKey();
        	byte[] value = entrySet.getValue();
        	Reporter.log("Attachment name:"+ key, true);
        	Reporter.log("Attachment size:"+ value.length, true);
        	CustomAssertion.assertTrue(value.length > 0, key + " size is "+value.length, key + "size is zero" );
        	CustomAssertion.assertTrue(key.startsWith(expectedAttachment), key+ " Starts with "+ expectedAttachment, "Attachment name don't match");
        }
	}
	
	
	@Test(groups={"EXPORT", "BOX", "REGRESSION", "P1"})
	public void checkCSVExportOfOtherRisks() throws Exception {
		LogUtils.logTestDescription("Export the other risks files to user email and check");
		List<NameValuePair> headers = getHeaders();
		String path = suiteData.getAPIMap().get("getUIExportCSV") ;
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(), path);
		Reporter.log("URI ::"+dataUri.toString(), true);

		String payload = "{\"source\":{\"limit\":20,\"offset\":0,\"isInternal\":true,\"requestType\":\"risky_docs\",\"searchTextFromTable\":\"\",\"orderBy\":\"name\",\"app\":\"Box\",\"exportType\":\"risky_docs\"}}";
		HttpResponse response =  restClient.doPost(dataUri, headers, null, new StringEntity(payload));

		String responseBody = ClientUtil.getResponseBody(response);
		Reporter.log("Response body:"+ responseBody, true);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
		CustomAssertion.assertTrue(responseBody.contains("true"), "Filelink sent to email", "File not sent");
		CustomAssertion.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_OK, "Not exported");
		
		sleep(CommonConstants.THIRTY_SECONDS_SLEEP);
		
		String query = "Box Securlet Data Export";
		
		String expectedAttachment = "Box_securlet_risky_docs_" + DateUtils.getCurrentDate();
		
		Map<String, byte[]> attachments = this.getEmailAttachment(query, null, null, null);
		for (Map.Entry<String, byte[]> entrySet : attachments.entrySet()) {
        	String key = entrySet.getKey();
        	byte[] value = entrySet.getValue();
        	Reporter.log("Attachment name:"+ key, true);
        	Reporter.log("Attachment size:"+ value.length, true);
        	CustomAssertion.assertTrue(value.length > 0, key + " size is "+value.length, key + "size is zero" );
        	CustomAssertion.assertTrue(key.startsWith(expectedAttachment), key+ " Starts with "+ expectedAttachment, "Attachment name don't match");
        }
		
	}
	
	
	@Test(dataProviderClass = BoxDataProvider.class, dataProvider = "ExportFormat", groups={"EXPORT", "BOX", "REGRESSION", "P1"})
	public void checkActivityLogExport(String format) throws Exception {
		
		
		LogUtils.logTestDescription("Export the Activity log export in "+ format + " format and check the email");
		 
		String tsfrom = DateUtils.getDaysFromCurrentTime(-8);
		String tsto   = DateUtils.getDaysFromCurrentTime(1);	
		
		HashMap<String, String> hmap = new HashMap<String, String>();
		hmap.put("facility", "Box");
		
		//Get headers
		List<NameValuePair> headers = getHeaders();
		String payload = esQueryBuilder.getESQuery(tsfrom, tsto, "Box", hmap, this.suiteData.getSaasAppUsername(), suiteData.getApiserverHostName(), 
							suiteData.getCSRFToken(), suiteData.getSessionID(), 0, 50000, "investigate", format, tsfrom, tsto);
		
		String path = suiteData.getAPIMap().get("getActivityLogExport") ;
		
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(), path);

		HttpResponse response =  restClient.doPost(dataUri, headers, null, new StringEntity(payload));

		String responseBody = ClientUtil.getResponseBody(response);
		Reporter.log("Response body:"+ responseBody, true);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
		CustomAssertion.assertTrue(responseBody.contains("true"), "Filelink sent to email", "File not sent");
		CustomAssertion.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_OK, "Not exported");
		
		sleep(CommonConstants.THIRTY_SECONDS_SLEEP);
		
		//Verify the mail has reached
		String query = format.toUpperCase() +" Log Export Request";
		String link = getEmailDownloadLink(query, null, null, null);
		
		Reporter.log("Download link:"+link, true);
	}
	
	@Test(dataProviderClass = BoxDataProvider.class, dataProvider = "ExportFormat", groups={"EXPORT", "BOX", "REGRESSION", "P1", "CUSTOMER_DATA_SEPARATION"})
	public void cusomterDataSeparationActivityLogExport(String format) throws Exception {
		LogUtils.logTestDescription("Export the Activity log export in "+ format + " format and check the email");
		 
		String tsfrom = DateUtils.getDaysFromCurrentTime(-8);
		String tsto   = DateUtils.getDaysFromCurrentTime(1);	
		
		HashMap<String, String> hmap = new HashMap<String, String>();
		hmap.put("facility", "Box");
		
		//Get headers
		List<NameValuePair> headers = getHeaders();
		
		ListIterator<NameValuePair> iter = headers.listIterator();
		while(iter.hasNext()){
		    if(iter.next().getValue().equals(suiteData.getUsername().toLowerCase())){
		    	iter.remove();
		    }
		}
		//modify the x-user to some other value
		headers.add(new BasicNameValuePair("X-User", "admin@protectbeatle.com"));	
		String payload = esQueryBuilder.getESQuery(tsfrom, tsto, "Box", hmap, this.suiteData.getSaasAppUsername(), suiteData.getApiserverHostName(), 
							suiteData.getCSRFToken(), suiteData.getSessionID(), 0, 50000, "investigate", format, tsfrom, tsto);
		
		String path = suiteData.getAPIMap().get("getActivityLogExport") ;
		
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(), path);

		HttpResponse response =  restClient.doPost(dataUri, headers, null, new StringEntity(payload));

		String responseBody = ClientUtil.getResponseBody(response);
		Reporter.log("Response body:"+ responseBody, true);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_BAD_REQUEST);
	}
	
	@Test(groups={"EXPORT", "BOX", "REGRESSION", "P1", "CUSTOMER_DATA_SEPARATION"})
	public void cusomterDataSeparationGetExposedDocs() throws Exception {

		List<NameValuePair> docparams = new ArrayList<NameValuePair>(); 
		docparams.add(new BasicNameValuePair(SecurletsConstants.IS_INTERNAL,  Boolean.TRUE.toString()));
		docparams.add(new BasicNameValuePair(SecurletsConstants.APP,  facility.Box.name()));
		
		List<NameValuePair> headers = getHeaders();
		
		String path = suiteData.getAPIMap().get("getExposedDocuments")
								.replace(SecurletsConstants.ELAPPNAME_PLACEHOLDER, elapp.el_box.name())
								.replace(SecurletsConstants.TENANT_PLACEHOLDER, "protectbeatlecom")
								.replace(SecurletsConstants.VERSION_PLACEHOLDER, suiteData.getBaseVersion());

		List<NameValuePair> qparams = new ArrayList<NameValuePair>(); 
		docparams.add(new BasicNameValuePair(SecurletsConstants.IS_INTERNAL,  Boolean.TRUE.toString()));
		docparams.add(new BasicNameValuePair(SecurletsConstants.APP,  facility.Box.name()));
		
		URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(), path, qparams);
		HttpResponse response =  restClient.doGet(uri, headers);
		assertEquals(getResponseStatusCode(response), HttpStatus.SC_UNAUTHORIZED, "Response code verification failed");
	}
	
	@Test(groups={"EXPORT", "BOX", "REGRESSION", "P1", "CUSTOMER_DATA_SEPARATION"})
	public void cusomterDataSeparationGetActivityLogs() throws Exception {

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
		headers.add(new BasicNameValuePair("X-User", "admin@protectbeatle.com"));
		
		String payload = esQueryBuilder.getESQuery(tsfrom, tsto, facility.Box.name(), termmap, "admin@protectbeatle.com", suiteData.getApiserverHostName(), 
				suiteData.getCSRFToken(), suiteData.getSessionID(), 0, 200, "Box");

		Reporter.log("Request body:"+ payload, true);

		//HttpRequest
		String path = suiteData.getAPIMap().get("getInvestigateLogs") ;
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(), path);
		Reporter.log("URI ::"+dataUri.toString(), true);

		HttpResponse response =  restClient.doPost(dataUri, headers, null, new StringEntity(payload));
		assertEquals(getResponseStatusCode(response), HttpStatus.SC_FORBIDDEN, "Response code verification failed");
	}
	
	@Test(groups={"EXPORT", "BOX", "REGRESSION", "P1", "CUSTOMER_DATA_SEPARATION"})
	public void cusomterDataSeparationUpdateAnonymization() throws Exception {
		String payload = "{\"userAnonymization\":true,\"dpoName\": \"\", \"dpoPassword\": \"\",\"id\":\"55dfffa19dfa5113940076a3\"}";
		Reporter.log("Payload:"+ payload, true);
		String path = suiteData.getAPIMap().get("getUIUserAnonymizationInfo") ;
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(), path);
		Reporter.log("URI ::"+dataUri.toString(), true);
		List<NameValuePair> headers = getHeaders();
		HttpResponse response =  restClient.doPost(dataUri, headers, null, new StringEntity(payload));
		String responseBody = ClientUtil.getResponseBody(response);
		Reporter.log("Response body:"+ responseBody, true);
		assertEquals(responseBody, "{\"message\": \"fail\"}", "Response verification failed");
	}
	
	
	
	
	//@Test //Method to clean up the unwanted groups
	public void listGroups() throws Exception {
		
		GroupList grouplist = universalApi.listGroup();
		
		for (BoxGroup group : grouplist.getEntries()) {
			Reporter.log(group.getName(), true);
			universalApi.deleteGroup(group);
		}
	}
	
	
	
	
	
	
	

	public ArrayList<String> searchDisplayLogs(int from, int to, String facilty, String query, String email, 
			String apiServerUrl, String csrfToken, String sessionId, int offset, int limit) throws Exception {

		Reporter.log("Retrieving the logs from Elastic Search ...", true);
	
		String tsfrom = DateUtils.getMinutesFromCurrentTime(-90);
		String tsto   = DateUtils.getMinutesFromCurrentTime(20);	
		
		//Get headers
		List<NameValuePair> headers = getHeaders();
		String payload = esQueryBuilder.getESQuery(tsfrom, tsto, facilty, query, email, apiServerUrl, csrfToken, sessionId, offset, limit);

		Reporter.log("Request body:"+ payload, true);

		//HttpRequest
		String path = suiteData.getAPIMap().get("getInvestigateLogs") ;
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(), path);
		Reporter.log("URI ::"+dataUri.toString(), true);

		HttpResponse response =  restClient.doPost(dataUri, headers, null, new StringEntity(payload, "UTF-8"));

		String responseBody = ClientUtil.getResponseBody(response);
		Reporter.log("==============================================================================");
		Reporter.log("Response body:"+ responseBody, true);


		ForensicSearchResults fsr = MarshallingUtils.unmarshall(responseBody, ForensicSearchResults.class);
		assertEquals(HttpStatus.SC_OK, getResponseStatusCode(response), "Response code verification failed");
		return retrieveActualMessages(fsr);

	}
	
	
	public ArrayList<String> searchDisplayLogs(int from, int to, String facilty, String query, String email, String apiServerUrl, String csrfToken, String sessionId) throws Exception {

		Reporter.log("Retrieving the logs from Elastic Search ...", true);
		String tsfrom = DateUtils.getMinutesFromCurrentTime(from);
		String tsto   = DateUtils.getMinutesFromCurrentTime(to);

		//Get headers
		List<NameValuePair> headers = getHeaders();
		headers.add(new BasicNameValuePair(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON +"; charset=UTF-8"));

		String payload = esQueryBuilder.getSearchQueryForDisplayLogs(tsfrom, tsto, facilty, query, email, apiServerUrl, csrfToken, sessionId);

		Reporter.log("Request body:"+ payload, true);

		//HttpRequest
		String path = suiteData.getAPIMap().get("getInvestigateLogs") ;
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(), path);
		Reporter.log("URI ::"+dataUri.toString(), true);
		
		HttpResponse response =  restClient.doPost(dataUri, headers, null, new StringEntity(payload, "UTF-8"));
		String responseBody = ClientUtil.getResponseBody(response);
		Reporter.log("==============================================================================");
		Reporter.log("Response body:"+ responseBody, true);

		ForensicSearchResults fsr = MarshallingUtils.unmarshall(responseBody, ForensicSearchResults.class);
		assertEquals(HttpStatus.SC_OK, getResponseStatusCode(response), "Response code verification failed");
		return this.retrieveActualMessages(fsr);
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

		HttpResponse response =  restClient.doPost(dataUri, headers, null, new StringEntity(payload, "UTF-8"));
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

	
	//@Test(dependsOnMethods="performUserRelatedActivities", groups={"USER"})
	public void test() throws Exception {
		
		try {
			for (int i = 0; i <= (1 * 60 * 1000); i+=60000 ) {
				sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
				String apiHost = suiteData.getApiserverHostName();   //suiteData.getScheme() + "://" + suiteData.getApiserverHostName();
				
				HashMap<String, String> termmap = new HashMap<String, String>();
				termmap.put("facility", "Box");
				termmap.put("Object_type", "User");
				
				ForensicSearchResults fsr = this.getInvestigateLogs(-300, 10, "Box", termmap, suiteData.getUsername(), apiHost, suiteData.getCSRFToken(), suiteData.getSessionID(), 0, 40, "Box");
				
				Reporter.log(MarshallingUtils.marshall(fsr), true);
				
				long total = fsr.getHits().getTotal();
				Reporter.log("Total count:"+ total, true);
				
				CustomAssertion.assertTrue(total> 0 , "Box logs should be seen", "No logs are seen for Box");
			}
		}
		catch(Exception e) {}
	}
	
	
	

	//@Test
	public void getExposedDocuments() throws Exception {

		List<NameValuePair> headers = getHeaders();

		List<NameValuePair> qparams = new ArrayList<NameValuePair>(); 
		qparams.add(new BasicNameValuePair("is_internal", "true"));
		qparams.add(new BasicNameValuePair("owned_by",  URLEncoder.encode(suiteData.getUsername(), "UTF-8")));
		qparams.add(new BasicNameValuePair("content_checks.vl_types",  URLEncoder.encode("hipaa,pii", "UTF-8")));
		//qparams.add(new BasicNameValuePair("content_checks.vk_content_iq_violations",  "test_ciq_profile"));

		String path = suiteData.getAPIMap().get("getBoxDocuments").
				replace("{tenant}", suiteData.getTenantName()).
				replace("{version}", suiteData.getBaseVersion());

		System.out.println("Path:" + path);
		URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(), path, qparams);
		System.out.println("Path:" + uri.toString());
		HttpResponse response =  restClient.doGet(uri, headers);
		String responseBody = ClientUtil.getResponseBody(response);

		System.out.println("Response body:"+ responseBody);
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

	private ArrayList<String> retrieveActualMessages(ForensicSearchResults fsr) {
		ArrayList<String> alist = new ArrayList<String>();

		for (Hit hit : fsr.getHits().getHits()) {
			Source source  = hit.getSource();
			alist.add(source.getMessage());
		}
		return alist;
	}
	
}