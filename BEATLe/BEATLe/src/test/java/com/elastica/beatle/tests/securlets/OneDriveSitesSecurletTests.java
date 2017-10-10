package com.elastica.beatle.tests.securlets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.testng.Reporter;
import org.testng.SkipException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.Test;

import com.elastica.beatle.DateUtils;
import com.elastica.beatle.MarshallingUtils;
import com.elastica.beatle.Authorization.AuthorizationHandler;
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
import com.elastica.beatle.securlets.dto.RemedialAction;
import com.elastica.beatle.securlets.dto.RemediationMetaInfo;
import com.elastica.beatle.securlets.dto.SecurletDocument;
import com.elastica.beatle.securlets.dto.SecurletRemediation;
import com.elastica.beatle.securlets.dto.UIRemediationInnerObject;
import com.elastica.beatle.securlets.dto.UIRemediationObject;
import com.elastica.beatle.securlets.dto.UIRemediationSource;
import com.universal.common.UniversalApi;
import com.universal.constants.CommonConstants;
import com.universal.dtos.onedrive.DocumentSharingResult;
import com.universal.dtos.onedrive.ItemRoleAssignment;
import com.universal.dtos.onedrive.ListItemAllFields;
import com.universal.dtos.onedrive.Metadata;
import com.universal.dtos.onedrive.Parameters;
import com.universal.dtos.onedrive.RecycleItem;
import com.universal.dtos.onedrive.Result;
import com.universal.dtos.onedrive.RoleDefinitions;
import com.universal.dtos.onedrive.SharingUserRoleAssignment;
import com.universal.dtos.onedrive.SiteFileResource;
import com.universal.dtos.onedrive.SiteFolderInput;
import com.universal.dtos.onedrive.SiteFolderUpdateInput;
import com.universal.dtos.onedrive.SiteInput;
import com.universal.dtos.onedrive.SiteResponse;
import com.universal.dtos.onedrive.SiteUserList;
import com.universal.dtos.onedrive.UpdateList;
import com.universal.dtos.onedrive.UserResult;
import com.universal.dtos.onedrive.UserValue;



public class OneDriveSitesSecurletTests extends SecurletUtils {
	ESQueryBuilder esQueryBuilder = null;
	LogValidator logValidator;
	CIQValidator ciqValidator;
	OneDriveActivityLog onedrivelog;
	OneDriveUtils onedriveUtils;
	
	String createdTime;
	String resourceId;
	private String destinationFile;
	private String destinationFolder;
	private String renamedFile = "HIPAA_Test2_rename.txt";
	private String query;
	private String uniqueShareId;
	DocumentValidator docValidator;
	HashMap<String, OneDriveBusinessActivity> activityMap = new HashMap<String, OneDriveBusinessActivity>();
	HashMap<String, OneDriveSiteActivity> siteActivityMap = new HashMap<String, OneDriveSiteActivity>();
	//String filename;
	private String uniqueFilename = "HIPAA_Test2.txt";
	private String uniqueFolder1  = "Securlets_Automation";
	private String uniqueRemediationFolder = "SiteFolderRemediation";
	
	List<String> possibleValues = new ArrayList<String>();
	List<String> readonlyValues = new ArrayList<String>();
	List<String> possibleCollabUpdateValues = new ArrayList<String>();
	
	String appName = "Office 365";
	ForensicSearchResults fileLogs, folderLogs, contentInspectionLogs, siteLogs;
	ForensicSearchResults siteCreateLogs, siteUpdateLogs, siteDeleteLogs, siteFileLogs, siteFolderLogs;
	
	HashMap<String, Long> usermap = new HashMap<String, Long>();
	HashMap<String, Long> rolemap = new HashMap<String, Long>();
	
	
	String rootsitename = "securletautomationsite";
	
	long waitTime = 2;
	
	public OneDriveSitesSecurletTests() throws Exception {
		esQueryBuilder = new ESQueryBuilder();
		logValidator = new LogValidator();
		onedrivelog = new OneDriveActivityLog();
		onedriveUtils = new OneDriveUtils();
		docValidator = new DocumentValidator();
		
		String uniqueId = String.valueOf(System.currentTimeMillis());
		query =  uniqueId;
		destinationFile = uniqueId + "_" + uniqueFilename;
		destinationFolder = uniqueId + "_" + uniqueFolder1;
		renamedFile = uniqueId + "_" + renamedFile;
		uniqueShareId = UUID.randomUUID().toString();
		
		
		//Populate possible values
		possibleValues.add("Everyone-Read"); 
		possibleValues.add("Everyone except external users-Read");
		possibleValues.add("Everyone-Contribute");
		possibleValues.add("Everyone except external users-Contribute");
		possibleValues.add("Everyone-Edit"); 
		possibleValues.add("Everyone except external users-Edit");
		possibleValues.add("Everyone-Design");
		possibleValues.add("Everyone except external users-Design");
		possibleValues.add("Everyone-Full Control");
		possibleValues.add("Everyone except external users-Full Control");


		//Populate readonly values
		readonlyValues.add("open-view"); 
		readonlyValues.add("open-edit");
		
		//Possible collab update values
		possibleCollabUpdateValues.add("Read");
		possibleCollabUpdateValues.add("Contribute");
		possibleCollabUpdateValues.add("Design");
		possibleCollabUpdateValues.add("Edit");
		possibleCollabUpdateValues.add("Full Control");
	}
	
	@BeforeClass(alwaysRun=true)
	public void initOffice() throws Exception {
		
		universalSiteApi = new UniversalApi(suiteData.getSaasApp(), saasAppSiteUserAccount);
		
		AuthorizationHandler.disableAnonymization(suiteData);
		
		SiteUserList splist = universalSiteApi.getRootSiteUserList(rootsitename);
		for (UserResult ur : splist.getD().getResults()){
			usermap.put(ur.getTitle(), ur.getId());
		}

		RoleDefinitions roledefs = universalSiteApi.getRootSiteRolesDefinitions(rootsitename);
		for (UserValue ur : roledefs.getValue()){
			rolemap.put(ur.getName(), ur.getId());
		}
	}
	
	
	
	@BeforeGroups(groups = {"ROOTSITEFOLDER"})
	public void performRootSiteFolderOperations() throws Exception {
		
		//add a folder
		//{ "ServerRelativeUrl": "/AutoTeamSite/Shared%20Documents/SampleFolder2"}
		SiteFolderInput sfi = new SiteFolderInput();
		sfi.setServerRelativeUrl("/Sites/"+rootsitename+"/Shared Documents/"+destinationFolder);
		SiteFileResource folderResource = universalSiteApi.createRootSiteFolder(rootsitename, sfi);
		
		onedrivelog.setFolderUploadLog(onedrivelog.getFolderUploadLog().replace("{foldername}", destinationFolder));
		String rootsiteurl = folderResource.getOdataMetadata().substring(0, folderResource.getOdataMetadata().indexOf("_") - 1);
		
		createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
		OneDriveSiteActivity rootsiteFolderUploadLog = new OneDriveSiteActivity(onedrivelog.getFolderUploadLog(), createdTime, Severity.informational.name(),  
														ObjectType.Folder.name(), ActivityType.Upload.name(), suiteData.getSaasAppUsername(), 
														folderResource.getUniqueId(), null,  "Root:Documents", 
														"Shared_x0020_Documents", rootsiteurl,  appName, suiteData.getDomainName(), null, null);
		siteActivityMap.put("rootsiteFolderUploadLog", rootsiteFolderUploadLog);
		
		sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
		
		//break the inherited permissions
		ListItemAllFields listitemfields = universalSiteApi.getRootSiteFolderListItemAllFields(rootsitename, folderResource.getServerRelativeUrl());
		universalSiteApi.breakRoleInheritanceForRootSiteListItem(rootsitename, listitemfields.getOdataEditLink(), false, false);
		
		createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
		onedrivelog.setFolderScopeChangeLog(onedrivelog.getFolderScopeChangeLog().replace("{foldername}", destinationFolder));
		
		OneDriveSiteActivity rootsiteFolderScopeChangeLog = new OneDriveSiteActivity(onedrivelog.getFolderScopeChangeLog(), createdTime, Severity.informational.name(),  
														ObjectType.Folder.name(), ActivityType.ScopeAdd.name(), suiteData.getSaasAppUsername(), 
														folderResource.getUniqueId(), null,  "Root:Documents", 
														"Shared_x0020_Documents", rootsiteurl,  appName, suiteData.getDomainName(), null, null);
		siteActivityMap.put("rootsiteFolderScopeChangeLog", rootsiteFolderScopeChangeLog);
		
		//share the folder
		universalSiteApi.addRootSiteRoleAssignmentForListItem(rootsitename, listitemfields.getOdataEditLink(), 
														String.valueOf(usermap.get("Pushparaj Thangaraj")), String.valueOf(rolemap.get("Contribute")));
		
		onedrivelog.setSiteFolderShareLog(onedrivelog.getSiteFolderShareLog().replace("{foldername}", destinationFolder));
		createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
		OneDriveSiteActivity rootsiteFolderShareLog = new OneDriveSiteActivity(onedrivelog.getSiteFolderShareLog(), createdTime, Severity.informational.name(),  
														ObjectType.Folder.name(), ActivityType.Share.name(), suiteData.getSaasAppUsername(), 
														folderResource.getUniqueId(), null,  "Root:Documents", 
														"Shared_x0020_Documents", rootsiteurl,  appName, suiteData.getDomainName(), null, null);
		siteActivityMap.put("rootsiteFolderShareLog", rootsiteFolderShareLog);
		
		//Unshare the folder
		sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
		universalSiteApi.disableRootSiteItemSharing(rootsitename, listitemfields.getOdataEditLink());
		
		onedrivelog.setSiteFolderUnShareLog(onedrivelog.getSiteFolderUnShareLog().replace("{foldername}", destinationFolder));
		createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
		OneDriveSiteActivity rootsiteFolderUnShareLog = new OneDriveSiteActivity(onedrivelog.getSiteFolderUnShareLog(), createdTime, Severity.informational.name(),  
														ObjectType.Folder.name(), ActivityType.Share.name(), suiteData.getSaasAppUsername(), 
														folderResource.getUniqueId(), null,  "Root:Documents", 
														"Shared_x0020_Documents", rootsiteurl,  appName, suiteData.getDomainName(), null, null);
		siteActivityMap.put("rootsiteFolderUnShareLog", rootsiteFolderUnShareLog);
		
		
		sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
		
		//Rename the folder
		
		String renamedFolder = destinationFolder+"_Renamed";
		ListItemAllFields folderItemfields = universalSiteApi.getRootSiteFolderListItemAllFields(rootsitename, sfi.getServerRelativeUrl());
		
		SiteFolderUpdateInput sfuinput = new SiteFolderUpdateInput();
		Metadata metadata = new Metadata();
		sfuinput.setFileLeafRef(renamedFolder);
		metadata.setType(folderItemfields.getOdataType());
		sfuinput.setMetadata(metadata);
		
		System.out.println(MarshallingUtils.marshall(sfuinput));
		
		universalSiteApi.renameRootSiteFolder(rootsitename, sfi.getServerRelativeUrl(), sfuinput);
		onedrivelog.setFolderRenameLog(onedrivelog.getFolderRenameLog().replace("{foldername}", renamedFolder));
		createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
		OneDriveSiteActivity rootsiteFolderRenameLog = new OneDriveSiteActivity(onedrivelog.getFolderRenameLog(), createdTime, Severity.informational.name(),  
														ObjectType.Folder.name(), ActivityType.Rename.name(), suiteData.getSaasAppUsername(), 
														folderResource.getUniqueId(), null,  "Root:Documents", 
														"Shared_x0020_Documents", rootsiteurl,  appName, suiteData.getDomainName(), null, null);
		siteActivityMap.put("rootsiteFolderRenameLog", rootsiteFolderRenameLog);
		
		sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
		
		//Delete the folder
		sfi.setServerRelativeUrl(sfi.getServerRelativeUrl().replace(destinationFolder, renamedFolder));
		universalSiteApi.deleteRootSiteFolder(rootsitename, sfi.getServerRelativeUrl());
		
		onedrivelog.setFolderDeleteLog(onedrivelog.getFolderDeleteLog().replace("{foldername}", renamedFolder));
		createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
		OneDriveSiteActivity rootsiteFolderDeleteLog = new OneDriveSiteActivity(onedrivelog.getFolderDeleteLog(), createdTime, Severity.informational.name(),  
														ObjectType.Folder.name(), ActivityType.Delete.name(), suiteData.getSaasAppUsername(), 
														folderResource.getUniqueId(), null,  null, 
														"Shared_x0020_Documents", rootsiteurl,  appName, suiteData.getDomainName(), null, null);
		siteActivityMap.put("rootsiteFolderDeleteLog", rootsiteFolderDeleteLog);
		
		sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
	}
	
	
	@Test(groups={"ROOTSITEFOLDER", "ONEDRIVE", "P1"})
	public void fetchRootSiteFolderLogs() throws Exception {
		//Fetch the logs
		try {

			for (int i = 1; i <= (3 * 60 * 1000); i+=60000 ) {
				sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
				HashMap<String, String> termmap = new HashMap<String, String>();
				termmap.put("facility", appName);
				termmap.put("Object_type", ObjectType.Folder.name());

				//Get folder related logs
				siteFolderLogs = this.getInvestigateLogs(-60, 10, appName, termmap, suiteData.getUsername(), suiteData.getApiserverHostName(), 
						suiteData.getCSRFToken(), suiteData.getSessionID(), 0, 100, appName);

			}
		}

		catch(Exception e) {}
		Reporter.log(MarshallingUtils.marshall(siteFolderLogs), true);
		long total = siteFolderLogs.getHits().getTotal();
		Reporter.log("Total folder logs count:"+ total, true);

		//If total number of logs is less than zero, no logs are seen so skip the tests
		if (total <= 0) {
			throw new SkipException("Skipping the dependent tests..No logs are seen for folder in site");
		}
	}
	
	@Test(dependsOnMethods ="fetchRootSiteFolderLogs", groups={"ROOTSITEFOLDER", "ONEDRIVE", "P1"})
	public void verifyRootSiteFolderUploadActivity() {
		LogUtils.logTestDescription("After creating a folder in the rootsite, verify the activity in the activity logs.");
		logValidator.verifyOnedriveSiteActivityLog(siteFolderLogs, siteActivityMap.get("rootsiteFolderUploadLog"));
	}
	
	@Test(dependsOnMethods ="fetchRootSiteFolderLogs", groups={"ROOTSITEFOLDER", "ONEDRIVE", "P1"})
	public void verifyRootSiteFolderRenameActivity() {
		LogUtils.logTestDescription("After renaming a folder in the rootsite, verify the activity in the activity logs.");
		logValidator.verifyOnedriveSiteActivityLog(siteFolderLogs, siteActivityMap.get("rootsiteFolderRenameLog"));
	}
	
	@Test(dependsOnMethods ="fetchRootSiteFolderLogs", groups={"ROOTSITEFOLDER", "ONEDRIVE", "P1"})
	public void verifyRootSiteFolderDeleteActivity() {
		LogUtils.logTestDescription("After deleting a folder in the rootsite, verify the activity in the activity logs.");
		logValidator.verifyOnedriveSiteActivityLog(siteFolderLogs, siteActivityMap.get("rootsiteFolderDeleteLog"));
	}
	
	@Test(dependsOnMethods ="fetchRootSiteFolderLogs", groups={"ROOTSITEFOLDER", "ONEDRIVE", "P1"})
	public void verifyRootSiteFolderScopeChangeActivity() {
		LogUtils.logTestDescription("After breaking the inherited permissions, verify the activity in the activity logs.");
		logValidator.verifyOnedriveSiteActivityLog(siteFolderLogs, siteActivityMap.get("rootsiteFolderScopeChangeLog"));
	}
	
	@Test(dependsOnMethods ="fetchRootSiteFolderLogs", groups={"ROOTSITEFOLDER", "ONEDRIVE", "P1"})
	public void verifyRootSiteFolderShareActivity() {
		LogUtils.logTestDescription("After sharing a folder in the rootsite, verify the activity in the activity logs.");
		logValidator.verifyOnedriveSiteActivityLog(siteFolderLogs, siteActivityMap.get("rootsiteFolderShareLog"));
	}
	
	@Test(dependsOnMethods ="fetchRootSiteFolderLogs", groups={"ROOTSITEFOLDER", "ONEDRIVE", "P1"})
	public void verifyRootSiteFolderUnshareActivity() {
		LogUtils.logTestDescription("After unsharing a folder in the rootsite, verify the activity in the activity logs.");
		logValidator.verifyOnedriveSiteActivityLog(siteFolderLogs, siteActivityMap.get("rootsiteFolderUnShareLog"));
	}
	
	
	@BeforeGroups(groups = {"ROOTSITEFILE"})
	public void performRootSiteFileOperations() throws Exception {
		//Add a file and check the logs.
		//Upload a file
		SiteFileResource fileResource = universalSiteApi.uploadFileToRootSite(rootsitename, uniqueFilename, "/", destinationFile);
		Reporter.log("File uploaded:"+fileResource.getName() , true);
		onedrivelog.setFileUploadLog(onedrivelog.getFileUploadLog().replace("{filename}", destinationFile));
		String rootsiteurl = fileResource.getOdataMetadata().substring(0, fileResource.getOdataMetadata().indexOf("_") - 1);
		
		createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
		OneDriveSiteActivity fileUploadLog = new OneDriveSiteActivity(onedrivelog.getFileUploadLog(), createdTime, Severity.informational.name(),  
														ObjectType.File.name(), ActivityType.Upload.name(), suiteData.getSaasAppUsername(), 
														fileResource.getUniqueId(), fileResource.getLength(),  "Root:Documents", 
														"Shared_x0020_Documents", rootsiteurl,  appName, suiteData.getDomainName(), null, null);
		siteActivityMap.put("fileUploadLog", fileUploadLog);
		
		Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
		
		//Update the file and check the logs
		fileResource = universalSiteApi.uploadFileToRootSite(rootsitename, uniqueFilename, "/", destinationFile);
		Reporter.log("File Version updated:"+fileResource.getName() , true);
		onedrivelog.setFileEditLog(onedrivelog.getFileEditLog().replace("{filename}", destinationFile));
		
		
		createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
		OneDriveSiteActivity fileEditLog = new OneDriveSiteActivity(onedrivelog.getFileEditLog(), createdTime, Severity.informational.name(),  
														ObjectType.File.name(), ActivityType.Edit.name(), suiteData.getSaasAppUsername(), 
														fileResource.getUniqueId(), fileResource.getLength(),  "Root:Documents", 
														"Shared_x0020_Documents", rootsiteurl,  appName, suiteData.getDomainName(), null, null);
		siteActivityMap.put("fileEditLog", fileEditLog);
		
		
		//Share the file
		
		//Change the file scope by breaking the inherited permissions
		onedrivelog.setFileScopeChangeLog(onedrivelog.getFileScopeChangeLog().replace("{filename}", destinationFile));
		
		ListItemAllFields listitemfields = universalSiteApi.getRootSiteListItemAllFieldsByUrl(rootsitename, "Shared Documents", destinationFile);
		universalSiteApi.breakRoleInheritanceForRootSiteListItem(rootsitename, listitemfields.getOdataEditLink(), false, false);
		
		
		createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
		OneDriveSiteActivity fileScopeChangeActivity = new OneDriveSiteActivity(onedrivelog.getFileScopeChangeLog(), createdTime, Severity.informational.name(),  
														ObjectType.File.name(), ActivityType.ScopeAdd.name(), suiteData.getSaasAppUsername(), 
														fileResource.getUniqueId(), fileResource.getLength(),  "Root:Documents", 
														"Shared_x0020_Documents", rootsiteurl,  appName, suiteData.getDomainName(), null, null);
		siteActivityMap.put("fileScopeChangeLog", fileScopeChangeActivity);
		
		
		Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
		
		
		//Share the file with everyone contribute permissions
		onedrivelog.setFileShareLogPartial(onedrivelog.getFileShareLogPartial().replace("{filename}", destinationFile));
		universalSiteApi.addRootSiteRoleAssignmentForListItem(rootsitename, listitemfields.getOdataEditLink(), 
																String.valueOf(usermap.get("Everyone")), String.valueOf(rolemap.get("Contribute")));
		createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
		OneDriveSiteActivity fileShareActivity = new OneDriveSiteActivity(onedrivelog.getFileShareLogPartial(), createdTime, Severity.informational.name(),  
														ObjectType.File.name(), ActivityType.Share.name(), suiteData.getSaasAppUsername(), 
														fileResource.getUniqueId(), fileResource.getLength(),  "Root:Documents", 
														"Shared_x0020_Documents", rootsiteurl,  appName, suiteData.getDomainName(), "Everyone", null);
		
		siteActivityMap.put("fileShareLog", fileShareActivity);
		
		Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
		
		
		//Unshare the file
		//onedrivelog.setFileUnShareNoUserLog(onedrivelog.getFileUnShareNoUserLog().replace("{filename}", destinationFile));
		
		onedrivelog.setFileUnShareLogPartial(onedrivelog.getFileUnShareLogPartial().replace("{filename}", destinationFile));
		universalSiteApi.disableRootSiteItemSharing(rootsitename, listitemfields.getOdataEditLink());
		createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
		OneDriveSiteActivity fileUnShareActivity = new OneDriveSiteActivity(onedrivelog.getFileUnShareLogPartial(), createdTime, Severity.informational.name(),  
														ObjectType.File.name(), ActivityType.Unshare.name(), suiteData.getSaasAppUsername(), 
														fileResource.getUniqueId(), fileResource.getLength(),  "Root:Documents", 
														"Shared_x0020_Documents", rootsiteurl,  appName, suiteData.getDomainName(), null, null);
		
		siteActivityMap.put("fileUnShareActivity", fileUnShareActivity);
		
		Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
		
		//Recycle the file
		onedrivelog.setFileDeleteLog(onedrivelog.getFileDeleteLog().replace("{filename}", destinationFile));
		
		RecycleItem recycledItem = universalSiteApi.recycleRootSiteFileItem(rootsitename, fileResource.getServerRelativeUrl(), false);
		createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
		OneDriveSiteActivity fileDeleteActivity = new OneDriveSiteActivity(onedrivelog.getFileDeleteLog(), createdTime, Severity.informational.name(),  
														ObjectType.File.name(), ActivityType.Delete.name(), suiteData.getSaasAppUsername(), 
														fileResource.getUniqueId(), fileResource.getLength(),  null, 
														"Shared_x0020_Documents", rootsiteurl,  appName, suiteData.getDomainName(), null, null);
		siteActivityMap.put("fileDeleteActivity", fileDeleteActivity);
		
		Thread.sleep(CommonConstants.TWO_MINUTES_SLEEP);
		
		//Restore the file 
		
		onedrivelog.setFileRestoreLog(onedrivelog.getFileRestoreLog().replace("{filename}", destinationFile));
		createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
		OneDriveSiteActivity fileRestoreActivity = new OneDriveSiteActivity(onedrivelog.getFileRestoreLog(), createdTime, Severity.informational.name(),  
														ObjectType.File.name(), ActivityType.Restore.name(), suiteData.getSaasAppUsername(), 
														fileResource.getUniqueId(), fileResource.getLength(),  "Root:Documents", 
														"Shared_x0020_Documents", rootsiteurl,  appName, suiteData.getDomainName(), null, null);
		siteActivityMap.put("fileRestoreActivity", fileRestoreActivity);
		
		universalSiteApi.restoreRootSiteRecyleBinItem(rootsitename, recycledItem.getValue());
		Thread.sleep(CommonConstants.TWO_MINUTES_SLEEP);
		
		
	}
	
	
	@Test(groups={"ROOTSITEFILE", "ONEDRIVE", "P1"})
	public void fetchRootSiteFileLogs() throws Exception {
		//Fetch the logs
		try {

			for (int i = 1; i <= (3 * 60 * 1000); i+=60000 ) {
				sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
				HashMap<String, String> termmap = new HashMap<String, String>();
				termmap.put("facility", appName);
				termmap.put("Object_type", ObjectType.File.name());

				//Get file related logs
				siteFileLogs = this.getInvestigateLogs(-50, 10, appName, termmap, suiteData.getUsername(), suiteData.getApiserverHostName(), 
						suiteData.getCSRFToken(), suiteData.getSessionID(), 0, 100, appName);

			}
		}

		catch(Exception e) {}
		Reporter.log(MarshallingUtils.marshall(siteFileLogs), true);
		long total = siteFileLogs.getHits().getTotal();
		Reporter.log("Total file logs count:"+ total, true);

		//If total number of logs is less than zero, no logs are seen so skip the tests
		if (total <= 0) {
			throw new SkipException("Skipping the dependent tests..No logs are seen for file in site");
		}
	}
	
	@Test(dependsOnMethods ="fetchRootSiteFileLogs", groups={"ROOTSITEFILE", "ONEDRIVE", "P1"})
	public void verifyRootSiteFileUploadActivity() {
		LogUtils.logTestDescription("After uploading a file to the rootsite, verify the activity in the activity logs.");
		logValidator.verifyOnedriveSiteActivityLog(siteFileLogs, siteActivityMap.get("fileUploadLog"));
	}
	
	@Test(dependsOnMethods ="fetchRootSiteFileLogs", groups={"ROOTSITEFILE", "ONEDRIVE", "P1"})
	public void verifyRootSiteFileSharingActivity() {
		LogUtils.logTestDescription("After sharing a file, verify the activity in the activity logs.");
		logValidator.verifyOnedriveSiteActivityLog(siteFileLogs, siteActivityMap.get("fileShareLog"));
	}
	
	@Test(dependsOnMethods ="fetchRootSiteFileLogs", groups={"ROOTSITEFILE", "ONEDRIVE", "P1"})
	public void verifyRootSiteFilePermissionsScopeChangeActivity() {
		LogUtils.logTestDescription("After changing the scope of a file, verify the activity in the activity logs.");
		logValidator.verifyOnedriveSiteActivityLog(siteFileLogs, siteActivityMap.get("fileScopeChangeLog"));
	}
	
	@Test(dependsOnMethods ="fetchRootSiteFileLogs", groups={"ROOTSITEFILE", "ONEDRIVE", "P1"})
	public void verifyRootSiteFileEditActivity() {
		LogUtils.logTestDescription("After updating the file contents of a file in the rootsite, verify the activity in the activity logs.");
		logValidator.verifyOnedriveSiteActivityLog(siteFileLogs, siteActivityMap.get("fileEditLog"));
	}
	
	@Test(dependsOnMethods ="fetchRootSiteFileLogs", groups={"ROOTSITEFILE", "ONEDRIVE", "P1"})
	public void verifyRootSiteFileUnShareActivity() {
		LogUtils.logTestDescription("After unsharing the file, verify the activity in the activity logs.");
		logValidator.verifyOnedriveSiteActivityLog(siteFileLogs, siteActivityMap.get("fileUnShareActivity"));
	}
	
	@Test(dependsOnMethods ="fetchRootSiteFileLogs", groups={"ROOTSITEFILE", "ONEDRIVE", "P1"})
	public void verifyRootSiteFileDeleteActivity() {
		LogUtils.logTestDescription("After deleting the file, verify the activity in the activity logs.");
		logValidator.verifyOnedriveSiteActivityLog(siteFileLogs, siteActivityMap.get("fileDeleteActivity"));
	}
	
	@Test(dependsOnMethods ="fetchRootSiteFileLogs", groups={"ROOTSITEFILE", "ONEDRIVE", "P1"})
	public void verifyRootSiteFileRestoreActivity() {
		LogUtils.logTestDescription("After restoring the file, verify the activity in the activity logs.");
		logValidator.verifyOnedriveSiteActivityLog(siteFileLogs, siteActivityMap.get("fileRestoreActivity"));
	}
	
	
	
	
	@Test(dataProviderClass = O365DataProvider.class, groups={"REMEDIATION", "ONEDRIVE", "P1"}, dataProvider = "ExposureRemediationProvider")
	public void exposeAndPerformUnshareRemediation(String currentUser, String currentRole, String remediationRole) throws Exception {
		String steps[] = 
			{	"1. This test expose the file with  "+ currentRole +" permission to " + currentUser, 
				"2. With UI remediation API, applying the remediation and check remediation applied successfully or not."};

		LogUtils.logTestDescription(steps);
		
		String uniqueId = String.valueOf(System.currentTimeMillis());
		destinationFile = uniqueId + "_" + uniqueFilename;
		
		SiteFileResource fileResource = universalSiteApi.uploadFileToRootSite(rootsitename, uniqueFilename, "/", destinationFile);
		Reporter.log("File uploaded:"+fileResource.getName() , true);
		onedrivelog.setFileUploadLog(onedrivelog.getFileUploadLog().replace("{filename}", destinationFile));
		String rootsiteurl = fileResource.getOdataMetadata().substring(0, fileResource.getOdataMetadata().indexOf("_") - 1);
		
		Reporter.log("1. File uploaded and itemlink is ready." + destinationFile, true);
		createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
		ListItemAllFields listitemfields = universalSiteApi.getRootSiteListItemAllFieldsByUrl(rootsitename, "Shared Documents", destinationFile);
		universalSiteApi.breakRoleInheritanceForRootSiteListItem(rootsitename, listitemfields.getOdataEditLink(), false, false);
		
		//Assign role inheritance
		Reporter.log("2. Assign the file " + currentRole +" permissions to " +currentUser, true);
		universalSiteApi.addRootSiteRoleAssignmentForListItem(rootsitename, listitemfields.getOdataEditLink(), 
										String.valueOf(usermap.get(currentUser)), String.valueOf(rolemap.get(currentRole)));
		
		ItemRoleAssignment rolesBeforeRemediation = universalSiteApi.getRoleAssignmentForRootSiteItem(rootsitename, listitemfields.getOdataEditLink());
		
		Reporter.log("3. Roles before remediation:"+MarshallingUtils.marshall(rolesBeforeRemediation), true);
		
		sleep(CommonConstants.TWO_MINUTES_SLEEP);
		
		
		CustomAssertion.assertTrue(checkDocumentInDB(elapp.el_office_365.name(), fileResource.getUniqueId()), "Exposed document is available in db", "Exposed document is not available in db");
		
		//Prepare the UI remediation object
		String[] code     = { remediationRole };
		String[] metaInfo = null;
		String publicPermission = (currentRole.equals("Read")) ? "view" : "edit";  
		if (currentUser.contains("Guest")) { 
			metaInfo = new String[] { "open" + "-" + publicPermission };
		} else {
			metaInfo = new String[] { currentUser + "-" + currentRole };	
		}
		UIRemediationObject UIRemedObject =  getUIRemediationObject(suiteData.getSaasAppUsername(), "File",
																		suiteData.getDomainName(), fileResource.getUniqueId(),  code, metaInfo, "Sites", null);
		Reporter.log("Request body:" + MarshallingUtils.marshall(UIRemedObject), true);

		Reporter.log("4. Applying the remediation..",  true);
		//Remediate the exposure
		remediateExposureWithUIServer(UIRemedObject);
		
		//Prepare the log for unshare activity verification
		String unshare = currentUser+"("+currentRole+")";
		createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
		
		String users  = currentUser;
		if (currentUser.equals("Everyone")) {
			users = suiteData.getSaasAppExternalUser1Name() + "," + currentUser;
		}
		
		sleep(CommonConstants.TWO_MINUTES_SLEEP);
		
		ItemRoleAssignment rolesAfterRemediation = universalSiteApi.getRoleAssignmentForRootSiteItem(rootsitename, listitemfields.getOdataEditLink());
		Reporter.log("5. Roles after remediation:"+MarshallingUtils.marshall(rolesAfterRemediation), true);
		
		CustomAssertion.assertTrue(!checkDocumentInDB(elapp.el_office_365.name(), fileResource.getUniqueId()), "Exposed document is not available in db", "Exposed document is available in db");

		Reporter.log("6. Verifying the remediation..",  true);

		int beforeRemediation = rolesBeforeRemediation.getValue().size();
		int afterRemediation  = rolesAfterRemediation.getValue().size();
		
		CustomAssertion.assertTrue(beforeRemediation != afterRemediation, "Remediation is successful", "Remediation not happened it seems");
		
		Reporter.log("7. Remediation applied successfully..",  true);
		
	}
	
	@Test(dataProviderClass = O365DataProvider.class, groups={"REMEDIATION", "ONEDRIVE", "P1"}, dataProvider = "PublicInternalExposureRemediationProvider")
	public void exposePubliclyInternallyAndPerformUnshareRemediation(String[] exposedTo, String[] exposedPermissions, String[] remedialAction, String[] metaInfo) throws Exception {
		String steps[] = 
			{	"1. This test expose the file publicly and internally", 
				"2. With UI remediation API, applying the remediation and check remediation applied successfully or not."};

		LogUtils.logTestDescription(steps);
		
		String uniqueId = String.valueOf(System.currentTimeMillis());
		destinationFile = uniqueId + "_" + uniqueFilename;
		
		SiteFileResource fileResource = universalSiteApi.uploadFileToRootSite(rootsitename, uniqueFilename, "/", destinationFile);
		Reporter.log("File uploaded:"+fileResource.getName() , true);
		onedrivelog.setFileUploadLog(onedrivelog.getFileUploadLog().replace("{filename}", destinationFile));
		String rootsiteurl = fileResource.getOdataMetadata().substring(0, fileResource.getOdataMetadata().indexOf("_") - 1);
		
		Reporter.log("1. File uploaded and itemlink is ready." + destinationFile, true);
		createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
		ListItemAllFields listitemfields = universalSiteApi.getRootSiteListItemAllFieldsByUrl(rootsitename, "Shared Documents", destinationFile);
		universalSiteApi.breakRoleInheritanceForRootSiteListItem(rootsitename, listitemfields.getOdataEditLink(), false, false);
		
		for (int i=0, j=exposedTo.length; i<j; i++) {
		//Assign role inheritance
		Reporter.log("2. Assign the file " + exposedTo[i] +" permissions to " +exposedPermissions[i], true);
			universalSiteApi.addRootSiteRoleAssignmentForListItem(rootsitename, listitemfields.getOdataEditLink(), 
					String.valueOf(usermap.get(exposedTo[i])), String.valueOf(rolemap.get(exposedPermissions[i])));
		
		}
		
		ItemRoleAssignment rolesBeforeRemediation = universalSiteApi.getRoleAssignmentForRootSiteItem(rootsitename, listitemfields.getOdataEditLink());
		
		Reporter.log("3. Roles before remediation:"+MarshallingUtils.marshall(rolesBeforeRemediation), true);
		
		sleep(CommonConstants.TWO_MINUTES_SLEEP);
		
		CustomAssertion.assertTrue(checkDocumentInDB(elapp.el_office_365.name(), fileResource.getUniqueId()), "Exposed document is available in db", "Exposed document is not available in db");
		
		
		UIRemediationObject UIRemedObject =  getUIRemediationObject(suiteData.getSaasAppUsername(), "File",
																		suiteData.getDomainName(), fileResource.getUniqueId(),  remedialAction, metaInfo, "Sites", null);
		Reporter.log("Request body:" + MarshallingUtils.marshall(UIRemedObject), true);

		Reporter.log("4. Applying the remediation..",  true);

		//Remediate the exposure
		remediateExposureWithUIServer(UIRemedObject);
		
		sleep(CommonConstants.TWO_MINUTES_SLEEP);
		
		ItemRoleAssignment rolesAfterRemediation = universalSiteApi.getRoleAssignmentForRootSiteItem(rootsitename, listitemfields.getOdataEditLink());
		Reporter.log("5. Roles after remediation:"+MarshallingUtils.marshall(rolesAfterRemediation), true);
		
		CustomAssertion.assertTrue(!checkDocumentInDB(elapp.el_office_365.name(), fileResource.getUniqueId()), "Exposed document is not available in db", "Exposed document is available in db");

		Reporter.log("6. Verifying the remediation..",  true);

		int beforeRemediation = rolesBeforeRemediation.getValue().size();
		int afterRemediation  = rolesAfterRemediation.getValue().size();
		
		CustomAssertion.assertTrue(beforeRemediation != afterRemediation, "Remediation is successful", "Remediation not happened it seems");
		
		Reporter.log("7. Remediation applied successfully..",  true);
		
	}
	
	
	@Test(dataProviderClass = O365DataProvider.class, groups={"REMEDIATION", "ONEDRIVE", "P1"}, dataProvider = "ShareAccessRemediationProvider")
	public void exposePubliclyInternallyAndPerformShareAccessRemediation(String[] exposedTo, String[] exposedPermissions, String[] remedialAction, String[] metaInfo, 
																								String[] expectedExposureAfterRemediation) throws Exception {
		String steps[] = 
			{	"1. This test expose the file publicly and internally", 
				"2. With UI remediation API, applying the remediation and check remediation applied successfully or not."};

		LogUtils.logTestDescription(steps);
		
		String uniqueId = String.valueOf(System.currentTimeMillis());
		destinationFile = uniqueId + "_" + uniqueFilename;
		
		SiteFileResource fileResource = universalSiteApi.uploadFileToRootSite(rootsitename, uniqueFilename, "/", destinationFile);
		Reporter.log("File uploaded:"+fileResource.getName() , true);
		onedrivelog.setFileUploadLog(onedrivelog.getFileUploadLog().replace("{filename}", destinationFile));
		String rootsiteurl = fileResource.getOdataMetadata().substring(0, fileResource.getOdataMetadata().indexOf("_") - 1);
		
		Reporter.log("1. File uploaded and itemlink is ready." + destinationFile, true);
		createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
		ListItemAllFields listitemfields = universalSiteApi.getRootSiteListItemAllFieldsByUrl(rootsitename, "Shared Documents", destinationFile);
		universalSiteApi.breakRoleInheritanceForRootSiteListItem(rootsitename, listitemfields.getOdataEditLink(), false, false);
		
		for (int i=0, j=exposedTo.length; i<j; i++) {
		//Assign role inheritance
		Reporter.log("2. Assign the file " + exposedTo[i] +" permissions to " +exposedPermissions[i], true);
			universalSiteApi.addRootSiteRoleAssignmentForListItem(rootsitename, listitemfields.getOdataEditLink(), 
					String.valueOf(usermap.get(exposedTo[i])), String.valueOf(rolemap.get(exposedPermissions[i])));
		
		}
		
		ItemRoleAssignment rolesBeforeRemediation = universalSiteApi.getRoleAssignmentForRootSiteItem(rootsitename, listitemfields.getOdataEditLink());
		
		Reporter.log("3. Roles before remediation:"+MarshallingUtils.marshall(rolesBeforeRemediation), true);
		
		//sleep(CommonConstants.THREE_MINUTES_SLEEP);
		sleep(CommonConstants.TWO_MINUTES_SLEEP);
		
		CustomAssertion.assertTrue(checkDocumentInDB(elapp.el_office_365.name(), fileResource.getUniqueId()), "Exposed document is available in db", "Exposed document is not available in db");
		
		
		UIRemediationObject UIRemedObject =  getUIRemediationObject(suiteData.getSaasAppUsername(), "File",
																		suiteData.getDomainName(), fileResource.getUniqueId(),  remedialAction, metaInfo, "Sites", null);
		Reporter.log("Request body:" + MarshallingUtils.marshall(UIRemedObject), true);
		
		Reporter.log("4. Applying the remediation..",  true);

		//Remediate the exposure
		remediateExposureWithUIServer(UIRemedObject);
		
		//sleep(CommonConstants.THREE_MINUTES_SLEEP);
		sleep(CommonConstants.TWO_MINUTES_SLEEP);
		
		ItemRoleAssignment rolesAfterRemediation = universalSiteApi.getRoleAssignmentForRootSiteItem(rootsitename, listitemfields.getOdataEditLink());
		Reporter.log("5. Roles after remediation:"+MarshallingUtils.marshall(rolesAfterRemediation), true);
		
		SecurletDocument document = this.getDocuments(elapp.el_office_365.name(), fileResource.getUniqueId());
		System.out.println(MarshallingUtils.marshall(document));
		
		CustomAssertion.assertTrue(checkDocumentInDB(elapp.el_office_365.name(), fileResource.getUniqueId()), "Exposed document is available in db", "Exposed document is not available in db");

		Reporter.log("6. Verifying the remediation..",  true);
		document = this.getDocuments(elapp.el_office_365.name(), fileResource.getUniqueId());
		
		ArrayList<String> als = new ArrayList<String>(Arrays.asList(expectedExposureAfterRemediation));
		
		if(als.contains("internal")) {
			CustomAssertion.assertTrue(document.getObjects().get(0).getExposures().getAllInternal(), "Internal Exposure is present", "Internal Exposure is not present");
		}
		
		if(als.contains("nointernal")) {
			CustomAssertion.assertTrue(!document.getObjects().get(0).getExposures().getAllInternal(), "Internal Exposure is not present", "Internal Exposure is present");
		}
		
		if(als.contains("public")) {
			CustomAssertion.assertTrue(document.getObjects().get(0).getExposures().getPublic(), "Public exposure is present", "Public exposure is not present");
		}
		
		if(als.contains("nopublic")) {
			CustomAssertion.assertTrue(!document.getObjects().get(0).getExposures().getPublic(), "Public exposure is remediated", "Public exposure is not remediated");
		}
		
		if(als.contains("external")) {
			CustomAssertion.assertTrue(document.getObjects().get(0).getExposures().getExtCount() > 0, "External exposure is present", "No External exposure");
		}
		
		if(als.contains("noexternal")) {
			CustomAssertion.assertTrue(document.getObjects().get(0).getExposures().getExtCount() == 0, "No External exposure", "External exposure is present");
		}
		
		Reporter.log("7. Remediation applied successfully..",  true);
		
	}
	
	
	@Test(dataProviderClass = O365DataProvider.class, groups={"REMEDIATION", "ONEDRIVE", "P1"}, dataProvider = "CollabUpdateRemediationProvider")
	public void exposeAndPerformCollabUpdateRemediation(String[] exposedTo, String[] exposedPermissions, String[] remedialAction, String[] metaInfo, 
																								String[] collabUpdateRole, String[] expectedExposureAfterRemediation) throws Exception {
		String steps[] = 
			{	"1. This test expose the file publicly and internally", 
				"2. With UI remediation API, applying the remediation and check remediation applied successfully or not."};

		LogUtils.logTestDescription(steps);
		
		HashMap<String, OneDriveSiteActivity> siteActivityMap = new HashMap<String, OneDriveSiteActivity>();
		
		String uniqueId = String.valueOf(System.currentTimeMillis());
		destinationFile = uniqueId + "_" + uniqueFilename;
		
		SiteFileResource fileResource = universalSiteApi.uploadFileToRootSite(rootsitename, uniqueFilename, "/", destinationFile);
		Reporter.log("File uploaded:"+fileResource.getName() , true);
		onedrivelog.setFileUploadLog(onedrivelog.getFileUploadLog().replace("{filename}", destinationFile));
		String rootsiteurl = fileResource.getOdataMetadata().substring(0, fileResource.getOdataMetadata().indexOf("_") - 1);
		
		Reporter.log("1. File uploaded and itemlink is ready." + destinationFile, true);
		createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
		ListItemAllFields listitemfields = universalSiteApi.getRootSiteListItemAllFieldsByUrl(rootsitename, "Shared Documents", destinationFile);
		universalSiteApi.breakRoleInheritanceForRootSiteListItem(rootsitename, listitemfields.getOdataEditLink(), false, false);
		
		for (int i=0, j=exposedTo.length; i<j; i++) {
		//Assign role inheritance
		Reporter.log("2. Assign the file " + exposedTo[i] +" permissions to " +exposedPermissions[i], true);
			universalSiteApi.addRootSiteRoleAssignmentForListItem(rootsitename, listitemfields.getOdataEditLink(), 
					String.valueOf(usermap.get(exposedTo[i])), String.valueOf(rolemap.get(exposedPermissions[i])));		
		}
		
		
		ItemRoleAssignment rolesBeforeRemediation = universalSiteApi.getRoleAssignmentForRootSiteItem(rootsitename, listitemfields.getOdataEditLink());
		
		Reporter.log("3. Roles before remediation:"+MarshallingUtils.marshall(rolesBeforeRemediation), true);
		
		sleep(CommonConstants.TWO_MINUTES_SLEEP);
		
		CustomAssertion.assertTrue(checkDocumentInDB(elapp.el_office_365.name(), fileResource.getUniqueId()), "Exposed document is available in db", "Exposed document is not available in db");
		
		
		UIRemediationObject UIRemedObject =  getUIRemediationObject(suiteData.getSaasAppUsername(), "File",
																		suiteData.getDomainName(), fileResource.getUniqueId(),  remedialAction, metaInfo,  "Sites", collabUpdateRole);
		Reporter.log("Request body:" + MarshallingUtils.marshall(UIRemedObject), true);
		
		Reporter.log("4. Applying the remediation..",  true);

		//Remediate the exposure
		remediateExposureWithUIServer(UIRemedObject);
		
		sleep(CommonConstants.TWO_MINUTES_SLEEP);
		
		ItemRoleAssignment rolesAfterRemediation = universalSiteApi.getRoleAssignmentForRootSiteItem(rootsitename, listitemfields.getOdataEditLink());
		Reporter.log("5. Roles after remediation:"+MarshallingUtils.marshall(rolesAfterRemediation), true);
		
		SecurletDocument document = this.getDocuments(elapp.el_office_365.name(), fileResource.getUniqueId());
		System.out.println(MarshallingUtils.marshall(document));
		
		CustomAssertion.assertTrue(checkDocumentInDB(elapp.el_office_365.name(), fileResource.getUniqueId()), "Exposed document is available in db", "Exposed document is not available in db");

		
		Reporter.log("6. Verifying the remediation..",  true);
		document = this.getDocuments(elapp.el_office_365.name(), fileResource.getUniqueId());
		
		ArrayList<String> als = new ArrayList<String>(Arrays.asList(expectedExposureAfterRemediation));
		
		if(als.contains("internal")) {
			CustomAssertion.assertTrue(document.getObjects().get(0).getExposures().getAllInternal(), "Internal Exposure is present", "Internal Exposure is not present");
		}
		
		if(als.contains("nointernal")) {
			CustomAssertion.assertTrue(!document.getObjects().get(0).getExposures().getAllInternal(), "Internal Exposure is not present", "Internal Exposure is present");
		}
		
		if(als.contains("public")) {
			CustomAssertion.assertTrue(document.getObjects().get(0).getExposures().getPublic(), "Public exposure is present", "Public exposure is not present");
		}
		
		if(als.contains("nopublic")) {
			CustomAssertion.assertTrue(!document.getObjects().get(0).getExposures().getPublic(), "Public exposure is remediated", "Public exposure is not remediated");
		}
		
		if(als.contains("external")) {
			CustomAssertion.assertTrue(document.getObjects().get(0).getExposures().getExtCount() > 0, "External exposure is present", "No External exposure");
		}
		
		if(als.contains("noexternal")) {
			CustomAssertion.assertTrue(document.getObjects().get(0).getExposures().getExtCount() == 0, "No External exposure", "External exposure is present");
		}
		
		Reporter.log("7. Remediation applied successfully..",  true);
		
		/*
		OneDriveSiteActivity fileShareActivity = null, fileUnShareActivity = null;
		
		for (int i=0, j=exposedTo.length; i<j; i++) {

			createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
			onedrivelog = new OneDriveActivityLog();
			
			
			onedrivelog.setFileUnShareLog(onedrivelog.getFileUnShareLog().replace("{filename}", destinationFile)
					.replace("{username}", metaInfo[i])
					.replace("{permission}", exposedPermissions[i]));

			onedrivelog.setFileShareLog(onedrivelog.getFileShareLog().replace("{filename}", destinationFile)
					.replace("{username}", metaInfo[i])
					.replace("{permission}", collabUpdateRole[i]));
			
			fileShareActivity = new OneDriveSiteActivity(onedrivelog.getFileShareLog(), createdTime, Severity.informational.name(),  
					ObjectType.File.name(), ActivityType.Share.name(), suiteData.getSaasAppUsername(), 
					fileResource.getUniqueId(), fileResource.getLength(),  "Root:Documents", 
					"Shared_x0020_Documents", rootsiteurl,  appName, suiteData.getDomainName(), metaInfo[i], null);
			
			fileUnShareActivity = new OneDriveSiteActivity(onedrivelog.getFileUnShareLog(), createdTime, Severity.informational.name(),  
					ObjectType.File.name(), ActivityType.Unshare.name(), suiteData.getSaasAppUsername(), 
					fileResource.getUniqueId(), fileResource.getLength(),  "Root:Documents", 
					"Shared_x0020_Documents", rootsiteurl,  appName, suiteData.getDomainName(), metaInfo[i], null);

			

		}
		
		Reporter.log("8. Verifying share and unshare logs ..", true);
		
		HashMap<String, String> termmap = new HashMap<String, String>();
		termmap.put("facility", "Office 365");
		termmap.put("Object_type", ObjectType.File.name());
		termmap.put("query", uniqueId);
		
		//Get file related logs
		fileLogs = this.getInvestigateLogs(-15, 5, "Office 365", termmap, suiteData.getUsername(), suiteData.getApiserverHostName(), 
													suiteData.getCSRFToken(), suiteData.getSessionID(), 0, 100, "Office 365");

		//Verify share log
		logValidator.verifyOnedriveSiteActivityLog(fileLogs, fileShareActivity);

		//Verify unshare log
		logValidator.verifyOnedriveSiteActivityLog(fileLogs, fileUnShareActivity);*/
		
	}
	
	
	@Test(dataProviderClass = O365DataProvider.class, groups={"REMEDIATION", "ONEDRIVE", "P1"}, dataProvider = "CollabRemoveRemediationProvider")
	public void exposeAndPerformCollabRemoveRemediation(String[] exposedTo, String[] exposedPermissions, String[] remedialAction, String[] metaInfo) throws Exception {
		String steps[] = 
			{	"1. This test expose the file publicly and internally", 
				"2. With UI remediation API, applying the remediation and check remediation applied successfully or not."};

		LogUtils.logTestDescription(steps);
		
		String uniqueId = String.valueOf(System.currentTimeMillis());
		destinationFile = uniqueId + "_" + uniqueFilename;
		
		SiteFileResource fileResource = universalSiteApi.uploadFileToRootSite(rootsitename, uniqueFilename, "/", destinationFile);
		Reporter.log("File uploaded:"+fileResource.getName() , true);
		onedrivelog.setFileUploadLog(onedrivelog.getFileUploadLog().replace("{filename}", destinationFile));
		String rootsiteurl = fileResource.getOdataMetadata().substring(0, fileResource.getOdataMetadata().indexOf("_") - 1);
		
		Reporter.log("1. File uploaded and itemlink is ready." + destinationFile, true);
		createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
		ListItemAllFields listitemfields = universalSiteApi.getRootSiteListItemAllFieldsByUrl(rootsitename, "Shared Documents", destinationFile);
		universalSiteApi.breakRoleInheritanceForRootSiteListItem(rootsitename, listitemfields.getOdataEditLink(), false, false);
		
		for (int i=0, j=exposedTo.length; i<j; i++) {
		//Assign role inheritance
		Reporter.log("2. Assign the file " + exposedTo[i] +" permissions to " +exposedPermissions[i], true);
			universalSiteApi.addRootSiteRoleAssignmentForListItem(rootsitename, listitemfields.getOdataEditLink(), 
					String.valueOf(usermap.get(exposedTo[i])), String.valueOf(rolemap.get(exposedPermissions[i])));
		
		}
		
		ItemRoleAssignment rolesBeforeRemediation = universalSiteApi.getRoleAssignmentForRootSiteItem(rootsitename, listitemfields.getOdataEditLink());
		
		Reporter.log("3. Roles before remediation:"+MarshallingUtils.marshall(rolesBeforeRemediation), true);
		
		//sleep(CommonConstants.THREE_MINUTES_SLEEP);
		sleep(CommonConstants.TWO_MINUTES_SLEEP);
		
		CustomAssertion.assertTrue(checkDocumentInDB(elapp.el_office_365.name(), fileResource.getUniqueId()), "Exposed document is available in db", "Exposed document is not available in db");
		
		
		UIRemediationObject UIRemedObject =  getUIRemediationObject(suiteData.getSaasAppUsername(), "File",
																		suiteData.getDomainName(), fileResource.getUniqueId(),  remedialAction, metaInfo, "Sites", null);
		Reporter.log("Request body:" + MarshallingUtils.marshall(UIRemedObject), true);
		
		Reporter.log("4. Applying the remediation..",  true);

		//Remediate the exposure
		remediateExposureWithUIServer(UIRemedObject);
		
		//sleep(CommonConstants.THREE_MINUTES_SLEEP);
		sleep(CommonConstants.TWO_MINUTES_SLEEP);
		
		ItemRoleAssignment rolesAfterRemediation = universalSiteApi.getRoleAssignmentForRootSiteItem(rootsitename, listitemfields.getOdataEditLink());
		Reporter.log("5. Roles after remediation:"+MarshallingUtils.marshall(rolesAfterRemediation), true);
		
		SecurletDocument document = this.getDocuments(elapp.el_office_365.name(), fileResource.getUniqueId());
		System.out.println(MarshallingUtils.marshall(document));
		
		Reporter.log("6. Verifying the remediation..",  true);
		CustomAssertion.assertTrue(!checkDocumentInDB(elapp.el_office_365.name(), fileResource.getUniqueId()), "Exposed document is not available in db", "Exposed document is available in db");

		Reporter.log("7. Remediation applied successfully..",  true);
		
	}
	
	
	@Test(dataProviderClass = O365DataProvider.class, groups={"REMEDIATION", "ONEDRIVE", "P1"}, dataProvider = "AllAccessRemediationProvider")
	public void exposeAllWaysAndPerformAllRemediation(String[] exposedTo, String[] exposedPermissions, String[] remedialAction, String[] metaInfo, 
																								String[] expectedExposureAfterRemediation) throws Exception {
		String steps[] = 
			{	"1. This test expose the file publicly and internally", 
				"2. With UI remediation API, applying the remediation and check remediation applied successfully or not."};

		LogUtils.logTestDescription(steps);
		
		String uniqueId = String.valueOf(System.currentTimeMillis());
		destinationFile = uniqueId + "_" + uniqueFilename;
		
		SiteFileResource fileResource = universalSiteApi.uploadFileToRootSite(rootsitename, uniqueFilename, "/", destinationFile);
		Reporter.log("File uploaded:"+fileResource.getName() , true);
		onedrivelog.setFileUploadLog(onedrivelog.getFileUploadLog().replace("{filename}", destinationFile));
		String rootsiteurl = fileResource.getOdataMetadata().substring(0, fileResource.getOdataMetadata().indexOf("_") - 1);
		
		Reporter.log("1. File uploaded and itemlink is ready." + destinationFile, true);
		createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
		ListItemAllFields listitemfields = universalSiteApi.getRootSiteListItemAllFieldsByUrl(rootsitename, "Shared Documents", destinationFile);
		universalSiteApi.breakRoleInheritanceForRootSiteListItem(rootsitename, listitemfields.getOdataEditLink(), false, false);
		
		for (int i=0, j=exposedTo.length; i<j; i++) {
		//Assign role inheritance
		Reporter.log("2. Assign the file " + exposedTo[i] +" permissions to " +exposedPermissions[i], true);
			universalSiteApi.addRootSiteRoleAssignmentForListItem(rootsitename, listitemfields.getOdataEditLink(), 
					String.valueOf(usermap.get(exposedTo[i])), String.valueOf(rolemap.get(exposedPermissions[i])));
		
		}
		
		ItemRoleAssignment rolesBeforeRemediation = universalSiteApi.getRoleAssignmentForRootSiteItem(rootsitename, listitemfields.getOdataEditLink());
		
		Reporter.log("3. Roles before remediation:"+MarshallingUtils.marshall(rolesBeforeRemediation), true);
		
		//sleep(CommonConstants.THREE_MINUTES_SLEEP);
		sleep(CommonConstants.TWO_MINUTES_SLEEP);
		
		CustomAssertion.assertTrue(checkDocumentInDB(elapp.el_office_365.name(), fileResource.getUniqueId()), "Exposed document is available in db", "Exposed document is not available in db");
		
		
		UIRemediationObject UIRemedObject =  getUIRemediationObject(suiteData.getSaasAppUsername(), "File",
																		suiteData.getDomainName(), fileResource.getUniqueId(),  remedialAction, metaInfo, "Sites", null);
		Reporter.log("Request body:" + MarshallingUtils.marshall(UIRemedObject), true);
		
		Reporter.log("4. Applying the remediation..",  true);

		//Remediate the exposure
		remediateExposureWithUIServer(UIRemedObject);
		
		//sleep(CommonConstants.THREE_MINUTES_SLEEP);
		sleep(CommonConstants.TWO_MINUTES_SLEEP);
		
		ItemRoleAssignment rolesAfterRemediation = universalSiteApi.getRoleAssignmentForRootSiteItem(rootsitename, listitemfields.getOdataEditLink());
		Reporter.log("5. Roles after remediation:"+MarshallingUtils.marshall(rolesAfterRemediation), true);
		
		SecurletDocument document = this.getDocuments(elapp.el_office_365.name(), fileResource.getUniqueId());
		System.out.println(MarshallingUtils.marshall(document));
		
		CustomAssertion.assertTrue(checkDocumentInDB(elapp.el_office_365.name(), fileResource.getUniqueId()), "Exposed document is available in db", "Exposed document is not available in db");

		Reporter.log("6. Verifying the remediation..",  true);
		document = this.getDocuments(elapp.el_office_365.name(), fileResource.getUniqueId());
		
		ArrayList<String> als = new ArrayList<String>(Arrays.asList(expectedExposureAfterRemediation));
		
		if(als.contains("internal")) {
			CustomAssertion.assertTrue(document.getObjects().get(0).getExposures().getAllInternal(), "Internal Exposure is present", "Internal Exposure is not present");
		}
		
		if(als.contains("nointernal")) {
			CustomAssertion.assertTrue(!document.getObjects().get(0).getExposures().getAllInternal(), "Internal Exposure is not present", "Internal Exposure is present");
		}
		
		if(als.contains("public")) {
			CustomAssertion.assertTrue(document.getObjects().get(0).getExposures().getPublic(), "Public exposure is present", "Public exposure is not present");
		}
		
		if(als.contains("nopublic")) {
			CustomAssertion.assertTrue(!document.getObjects().get(0).getExposures().getPublic(), "Public exposure is remediated", "Public exposure is not remediated");
		}
		
		if(als.contains("external")) {
			CustomAssertion.assertTrue(document.getObjects().get(0).getExposures().getExtCount() > 0, "External exposure is present", "No External exposure");
		}
		
		if(als.contains("noexternal")) {
			CustomAssertion.assertTrue(document.getObjects().get(0).getExposures().getExtCount() == 0, "No External exposure", "External exposure is present");
		}
		
		Reporter.log("7. Remediation applied successfully..",  true);
		
	}
	
	
	
	
//Folder remediation tests
	@Test(dataProviderClass = O365DataProvider.class, groups={"FOLDER_REMEDIATION", "ONEDRIVE", "P1"}, dataProvider = "ExposureRemediationProvider")
	public void exposeFolderAndPerformUnshareRemediation(String currentUser, String currentRole, String remediationRole) throws Exception {
		String steps[] = 
			{	"1. This test expose the folder with  "+ currentUser +" permission to " + currentRole, 
				"2. With UI remediation API, applying the remediation and check remediation applied successfully or not."};

		LogUtils.logTestDescription(steps);
		
		String uniqueId = String.valueOf(System.currentTimeMillis());
		destinationFolder = uniqueId + "_" + uniqueRemediationFolder;
		destinationFile = uniqueId + "_" + uniqueFilename;
		
		SiteFolderInput sfi = new SiteFolderInput();
		sfi.setServerRelativeUrl("/Sites/"+rootsitename+"/Shared Documents/"+destinationFolder);
		SiteFileResource folderResource = universalSiteApi.createRootSiteFolder(rootsitename, sfi);
		SiteFileResource fileResource 	= universalSiteApi.uploadFileToRootSite(rootsitename, uniqueFilename, "Shared Documents/"+destinationFolder, destinationFile);
		
		String rootsiteurl = folderResource.getOdataMetadata().substring(0, folderResource.getOdataMetadata().indexOf("_") - 1);
		Reporter.log("1. Folder created and file uploaded and itemlink is ready." + destinationFolder, true);
		
		//break the inherited permissions
		ListItemAllFields listitemfields = universalSiteApi.getRootSiteFolderListItemAllFields(rootsitename, folderResource.getServerRelativeUrl());
		universalSiteApi.breakRoleInheritanceForRootSiteListItem(rootsitename, listitemfields.getOdataEditLink(), false, false);
		//Assign role inheritance
		Reporter.log("2. Assign the folder " + currentRole +" permissions to " +currentUser, true);
		universalSiteApi.addRootSiteRoleAssignmentForListItem(rootsitename, listitemfields.getOdataEditLink(), 
												String.valueOf(usermap.get(currentUser)), String.valueOf(rolemap.get(currentRole)));

		ItemRoleAssignment rolesBeforeRemediation = universalSiteApi.getRoleAssignmentForRootSiteItem(rootsitename, listitemfields.getOdataEditLink());
		
		Reporter.log("3. Roles before remediation:"+MarshallingUtils.marshall(rolesBeforeRemediation), true);
		
		sleep(CommonConstants.THREE_MINUTES_SLEEP);
		
		CustomAssertion.assertTrue(checkDocumentInDB(elapp.el_office_365.name(), folderResource.getUniqueId()), "Exposed document is available in db", "Exposed document is not available in db");
		
		//Prepare the UI remediation object
		String[] code     = { remediationRole };
		String[] metaInfo = null;
		String publicPermission = (currentRole.equals("Read")) ? "view" : "edit";  
		if (currentUser.contains("Guest")) { 
			metaInfo = new String[] { "open" + "-" + publicPermission };
		} else {
			metaInfo = new String[] { currentUser + "-" + currentRole };	
		}
		UIRemediationObject UIRemedObject =  getUIRemediationObject(suiteData.getSaasAppUsername(), "Folder",
																		suiteData.getDomainName(), fileResource.getUniqueId(),  code, metaInfo, "Sites", null);
		Reporter.log("Request body:" + MarshallingUtils.marshall(UIRemedObject), true);

		Reporter.log("4. Applying the remediation..",  true);
		//Remediate the exposure
		remediateExposureWithUIServer(UIRemedObject);
		
		//Prepare the log for unshare activity verification
		String unshare = currentUser+"("+currentRole+")";
		createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
		
		String users  = currentUser;
		if (currentUser.equals("Everyone")) {
			users = suiteData.getSaasAppExternalUser1Name() + "," + currentUser;
		}
		
		sleep(CommonConstants.THREE_MINUTES_SLEEP);
		
		ItemRoleAssignment rolesAfterRemediation = universalSiteApi.getRoleAssignmentForRootSiteItem(rootsitename, listitemfields.getOdataEditLink());
		Reporter.log("5. Roles after remediation:"+MarshallingUtils.marshall(rolesAfterRemediation), true);
		
		CustomAssertion.assertTrue(!checkDocumentInDB(elapp.el_office_365.name(), fileResource.getUniqueId()), "Exposed document is not available in db", "Exposed document is available in db");

		Reporter.log("6. Verifying the remediation..",  true);

		int beforeRemediation = rolesBeforeRemediation.getValue().size();
		int afterRemediation  = rolesAfterRemediation.getValue().size();
		
		CustomAssertion.assertTrue(beforeRemediation != afterRemediation, "Remediation is successful", "Remediation not happened it seems");
		
		Reporter.log("7. Remediation applied successfully..",  true);
		
	}
	
	
	@Test(dataProviderClass = O365DataProvider.class, groups={"FOLDER_REMEDIATION", "ONEDRIVE", "P1"}, dataProvider = "ShareAccessRemediationProvider")
	public void exposeFolderPubliclyInternallyAndPerformShareAccessRemediation(String[] exposedTo, String[] exposedPermissions, String[] remedialAction, String[] metaInfo, 
																								String[] expectedExposureAfterRemediation) throws Exception {
		String steps[] = 
			{	"1. This test expose the folder publicly and internally", 
				"2. With UI remediation API, applying the remediation and check remediation applied successfully or not."};

		LogUtils.logTestDescription(steps);
		
		HashMap<String, OneDriveSiteActivity> siteActivityMap = new HashMap<String, OneDriveSiteActivity>();
		String uniqueId = String.valueOf(System.currentTimeMillis());
		destinationFolder = uniqueId + "_" + uniqueRemediationFolder;
		destinationFile = uniqueId + "_" + uniqueFilename;
		
		SiteFolderInput sfi = new SiteFolderInput();
		sfi.setServerRelativeUrl("/Sites/"+rootsitename+"/Shared Documents/"+destinationFolder);
		SiteFileResource folderResource = universalSiteApi.createRootSiteFolder(rootsitename, sfi);
		SiteFileResource fileResource 	= universalSiteApi.uploadFileToRootSite(rootsitename, uniqueFilename, "Shared Documents/"+destinationFolder, destinationFile);
		
		String rootsiteurl = folderResource.getOdataMetadata().substring(0, folderResource.getOdataMetadata().indexOf("_") - 1);
		Reporter.log("1. Folder created and file uploaded and itemlink is ready." + destinationFolder, true);
		
		//break the inherited permissions
		ListItemAllFields listitemfields = universalSiteApi.getRootSiteFolderListItemAllFields(rootsitename, folderResource.getServerRelativeUrl());
		universalSiteApi.breakRoleInheritanceForRootSiteListItem(rootsitename, listitemfields.getOdataEditLink(), false, false);
		
		
		for (int i=0, j=exposedTo.length; i<j; i++) {

			//Assign role inheritance
			Reporter.log("2. Assign the folder " + exposedTo[i] +" permissions to " +exposedPermissions[i], true);
			universalSiteApi.addRootSiteRoleAssignmentForListItem(rootsitename, listitemfields.getOdataEditLink(), 
					String.valueOf(usermap.get(exposedTo[i])), String.valueOf(rolemap.get(exposedPermissions[i])));

		}
		
		ItemRoleAssignment rolesBeforeRemediation = universalSiteApi.getRoleAssignmentForRootSiteItem(rootsitename, listitemfields.getOdataEditLink());
		
		Reporter.log("3. Roles before remediation:"+MarshallingUtils.marshall(rolesBeforeRemediation), true);
		
		//sleep(CommonConstants.THREE_MINUTES_SLEEP);
		sleep(CommonConstants.TWO_MINUTES_SLEEP);
		
		CustomAssertion.assertTrue(checkDocumentInDB(elapp.el_office_365.name(), fileResource.getUniqueId()), "Exposed document is available in db", "Exposed document is not available in db");
		
		
		UIRemediationObject UIRemedObject =  getUIRemediationObject(suiteData.getSaasAppUsername(), "Folder",
																		suiteData.getDomainName(), fileResource.getUniqueId(),  remedialAction, metaInfo, "Sites", null);
		Reporter.log("Request body:" + MarshallingUtils.marshall(UIRemedObject), true);
		
		Reporter.log("4. Applying the remediation..",  true);

		//Remediate the exposure
		remediateExposureWithUIServer(UIRemedObject);
		
		//sleep(CommonConstants.THREE_MINUTES_SLEEP);
		sleep(CommonConstants.TWO_MINUTES_SLEEP);
		
		ItemRoleAssignment rolesAfterRemediation = universalSiteApi.getRoleAssignmentForRootSiteItem(rootsitename, listitemfields.getOdataEditLink());
		Reporter.log("5. Roles after remediation:"+MarshallingUtils.marshall(rolesAfterRemediation), true);
		
		SecurletDocument document = this.getDocuments(elapp.el_office_365.name(), fileResource.getUniqueId());
		System.out.println(MarshallingUtils.marshall(document));
		
		CustomAssertion.assertTrue(checkDocumentInDB(elapp.el_office_365.name(), fileResource.getUniqueId()), "Exposed document is available in db", "Exposed document is not available in db");

		Reporter.log("6. Verifying the remediation..",  true);
		document = this.getDocuments(elapp.el_office_365.name(), fileResource.getUniqueId());
		
		ArrayList<String> als = new ArrayList<String>(Arrays.asList(expectedExposureAfterRemediation));
		
		if(als.contains("internal")) {
			CustomAssertion.assertTrue(document.getObjects().get(0).getExposures().getAllInternal(), "Internal Exposure is present", "Internal Exposure is not present");
		}
		
		if(als.contains("nointernal")) {
			CustomAssertion.assertTrue(!document.getObjects().get(0).getExposures().getAllInternal(), "Internal Exposure is not present", "Internal Exposure is present");
		}
		
		if(als.contains("public")) {
			CustomAssertion.assertTrue(document.getObjects().get(0).getExposures().getPublic(), "Public exposure is present", "Public exposure is not present");
		}
		
		if(als.contains("nopublic")) {
			CustomAssertion.assertTrue(!document.getObjects().get(0).getExposures().getPublic(), "Public exposure is remediated", "Public exposure is not remediated");
		}
		
		if(als.contains("external")) {
			CustomAssertion.assertTrue(document.getObjects().get(0).getExposures().getExtCount() > 0, "External exposure is present", "No External exposure");
		}
		
		if(als.contains("noexternal")) {
			CustomAssertion.assertTrue(document.getObjects().get(0).getExposures().getExtCount() == 0, "No External exposure", "External exposure is present");
		}
		
		Reporter.log("7. Remediation applied successfully..",  true);
		
	}
	
	
	@Test(dataProviderClass = O365DataProvider.class, groups={"FOLDER_REMEDIATION", "ONEDRIVE", "P1"}, dataProvider = "CollabUpdateRemediationProvider")
	public void exposeFolderAndPerformCollabUpdateRemediation(String[] exposedTo, String[] exposedPermissions, String[] remedialAction, String[] metaInfo, 
																								String[] collabUpdateRole, String[] expectedExposureAfterRemediation) throws Exception {
		String steps[] = 
			{	"1. This test expose the folder to external collab and internal collab and update the roles", 
				"2. With UI remediation API, applying the remediation and check remediation applied successfully or not."};

		LogUtils.logTestDescription(steps);
		
		HashMap<String, OneDriveSiteActivity> siteActivityMap = new HashMap<String, OneDriveSiteActivity>();
		String uniqueId = String.valueOf(System.currentTimeMillis());
		destinationFolder = uniqueId + "_" + uniqueRemediationFolder;
		destinationFile = uniqueId + "_" + uniqueFilename;
		
		SiteFolderInput sfi = new SiteFolderInput();
		sfi.setServerRelativeUrl("/Sites/"+rootsitename+"/Shared Documents/"+destinationFolder);
		SiteFileResource folderResource = universalSiteApi.createRootSiteFolder(rootsitename, sfi);
		SiteFileResource fileResource 	= universalSiteApi.uploadFileToRootSite(rootsitename, uniqueFilename, "Shared Documents/"+destinationFolder, destinationFile);
		
		String rootsiteurl = folderResource.getOdataMetadata().substring(0, folderResource.getOdataMetadata().indexOf("_") - 1);
		Reporter.log("1. Folder created and file uploaded and itemlink is ready." + destinationFolder, true);
		
		//break the inherited permissions
		ListItemAllFields listitemfields = universalSiteApi.getRootSiteFolderListItemAllFields(rootsitename, folderResource.getServerRelativeUrl());
		universalSiteApi.breakRoleInheritanceForRootSiteListItem(rootsitename, listitemfields.getOdataEditLink(), false, false);
		
		
		for (int i=0, j=exposedTo.length; i<j; i++) {

			//Assign role inheritance
			Reporter.log("2. Assign the folder " + exposedTo[i] +" permissions to " +exposedPermissions[i], true);
			universalSiteApi.addRootSiteRoleAssignmentForListItem(rootsitename, listitemfields.getOdataEditLink(), 
					String.valueOf(usermap.get(exposedTo[i])), String.valueOf(rolemap.get(exposedPermissions[i])));

		}
		
		
		ItemRoleAssignment rolesBeforeRemediation = universalSiteApi.getRoleAssignmentForRootSiteItem(rootsitename, listitemfields.getOdataEditLink());
		
		Reporter.log("3. Roles before remediation:"+MarshallingUtils.marshall(rolesBeforeRemediation), true);
		
		sleep(CommonConstants.TWO_MINUTES_SLEEP);
		
		CustomAssertion.assertTrue(checkDocumentInDB(elapp.el_office_365.name(), fileResource.getUniqueId()), "Exposed document is available in db", "Exposed document is not available in db");
		
		
		UIRemediationObject UIRemedObject =  getUIRemediationObject(suiteData.getSaasAppUsername(), "File",
																		suiteData.getDomainName(), fileResource.getUniqueId(),  remedialAction, metaInfo,  "Sites", collabUpdateRole);
		Reporter.log("Request body:" + MarshallingUtils.marshall(UIRemedObject), true);
		
		Reporter.log("4. Applying the remediation..",  true);

		//Remediate the exposure
		remediateExposureWithUIServer(UIRemedObject);
		
		sleep(CommonConstants.TWO_MINUTES_SLEEP);
		
		ItemRoleAssignment rolesAfterRemediation = universalSiteApi.getRoleAssignmentForRootSiteItem(rootsitename, listitemfields.getOdataEditLink());
		Reporter.log("5. Roles after remediation:"+MarshallingUtils.marshall(rolesAfterRemediation), true);
		
		SecurletDocument document = this.getDocuments(elapp.el_office_365.name(), fileResource.getUniqueId());
		System.out.println(MarshallingUtils.marshall(document));
		
		CustomAssertion.assertTrue(checkDocumentInDB(elapp.el_office_365.name(), fileResource.getUniqueId()), "Exposed document is available in db", "Exposed document is not available in db");

		
		Reporter.log("6. Verifying the remediation..",  true);
		document = this.getDocuments(elapp.el_office_365.name(), fileResource.getUniqueId());
		
		ArrayList<String> als = new ArrayList<String>(Arrays.asList(expectedExposureAfterRemediation));
		
		if(als.contains("internal")) {
			CustomAssertion.assertTrue(document.getObjects().get(0).getExposures().getAllInternal(), "Internal Exposure is present", "Internal Exposure is not present");
		}
		
		if(als.contains("nointernal")) {
			CustomAssertion.assertTrue(!document.getObjects().get(0).getExposures().getAllInternal(), "Internal Exposure is not present", "Internal Exposure is present");
		}
		
		if(als.contains("public")) {
			CustomAssertion.assertTrue(document.getObjects().get(0).getExposures().getPublic(), "Public exposure is present", "Public exposure is not present");
		}
		
		if(als.contains("nopublic")) {
			CustomAssertion.assertTrue(!document.getObjects().get(0).getExposures().getPublic(), "Public exposure is remediated", "Public exposure is not remediated");
		}
		
		if(als.contains("external")) {
			CustomAssertion.assertTrue(document.getObjects().get(0).getExposures().getExtCount() > 0, "External exposure is present", "No External exposure");
		}
		
		if(als.contains("noexternal")) {
			CustomAssertion.assertTrue(document.getObjects().get(0).getExposures().getExtCount() == 0, "No External exposure", "External exposure is present");
		}
		
		Reporter.log("7. Remediation applied successfully..",  true);
		
		/*
		OneDriveSiteActivity fileShareActivity = null, fileUnShareActivity = null;
		
		for (int i=0, j=exposedTo.length; i<j; i++) {

			createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
			onedrivelog = new OneDriveActivityLog();
			
			
			onedrivelog.setFileUnShareLog(onedrivelog.getFileUnShareLog().replace("{filename}", destinationFile)
					.replace("{username}", metaInfo[i])
					.replace("{permission}", exposedPermissions[i]));

			onedrivelog.setFileShareLog(onedrivelog.getFileShareLog().replace("{filename}", destinationFile)
					.replace("{username}", metaInfo[i])
					.replace("{permission}", collabUpdateRole[i]));
			
			fileShareActivity = new OneDriveSiteActivity(onedrivelog.getFileShareLog(), createdTime, Severity.informational.name(),  
					ObjectType.File.name(), ActivityType.Share.name(), suiteData.getSaasAppUsername(), 
					fileResource.getUniqueId(), fileResource.getLength(),  "Root:Documents", 
					"Shared_x0020_Documents", rootsiteurl,  appName, suiteData.getDomainName(), metaInfo[i], null);
			
			fileUnShareActivity = new OneDriveSiteActivity(onedrivelog.getFileUnShareLog(), createdTime, Severity.informational.name(),  
					ObjectType.File.name(), ActivityType.Unshare.name(), suiteData.getSaasAppUsername(), 
					fileResource.getUniqueId(), fileResource.getLength(),  "Root:Documents", 
					"Shared_x0020_Documents", rootsiteurl,  appName, suiteData.getDomainName(), metaInfo[i], null);

			

		}
		
		Reporter.log("8. Verifying share and unshare logs ..", true);
		
		HashMap<String, String> termmap = new HashMap<String, String>();
		termmap.put("facility", "Office 365");
		termmap.put("Object_type", ObjectType.File.name());
		termmap.put("query", uniqueId);
		
		//Get file related logs
		fileLogs = this.getInvestigateLogs(-15, 5, "Office 365", termmap, suiteData.getUsername(), suiteData.getApiserverHostName(), 
													suiteData.getCSRFToken(), suiteData.getSessionID(), 0, 100, "Office 365");

		//Verify share log
		logValidator.verifyOnedriveSiteActivityLog(fileLogs, fileShareActivity);

		//Verify unshare log
		logValidator.verifyOnedriveSiteActivityLog(fileLogs, fileUnShareActivity);*/
		
	}
	
	
	@Test(dataProviderClass = O365DataProvider.class, groups={"FOLDER_REMEDIATION", "ONEDRIVE", "P1"}, dataProvider = "AllAccessRemediationProvider")
	public void exposeFolderAllWaysAndPerformAllRemediation(String[] exposedTo, String[] exposedPermissions, String[] remedialAction, String[] metaInfo, 
																								String[] expectedExposureAfterRemediation) throws Exception {
		String steps[] = 
			{	"1. This test expose the file publicly and internally", 
				"2. With UI remediation API, applying the remediation and check remediation applied successfully or not."};

		LogUtils.logTestDescription(steps);
		
		String uniqueId = String.valueOf(System.currentTimeMillis());
		destinationFolder = uniqueId + "_" + uniqueRemediationFolder;
		destinationFile = uniqueId + "_" + uniqueFilename;
		
		SiteFolderInput sfi = new SiteFolderInput();
		sfi.setServerRelativeUrl("/Sites/"+rootsitename+"/Shared Documents/"+destinationFolder);
		SiteFileResource folderResource = universalSiteApi.createRootSiteFolder(rootsitename, sfi);
		SiteFileResource fileResource 	= universalSiteApi.uploadFileToRootSite(rootsitename, uniqueFilename, "Shared Documents/"+destinationFolder, destinationFile);
		
		String rootsiteurl = folderResource.getOdataMetadata().substring(0, folderResource.getOdataMetadata().indexOf("_") - 1);
		Reporter.log("1. Folder created and file uploaded and itemlink is ready." + destinationFolder, true);
		
		//break the inherited permissions
		ListItemAllFields listitemfields = universalSiteApi.getRootSiteFolderListItemAllFields(rootsitename, folderResource.getServerRelativeUrl());
		universalSiteApi.breakRoleInheritanceForRootSiteListItem(rootsitename, listitemfields.getOdataEditLink(), false, false);
		
		
		for (int i=0, j=exposedTo.length; i<j; i++) {

			//Assign role inheritance
			Reporter.log("2. Assign the folder " + exposedTo[i] +" permissions to " +exposedPermissions[i], true);
			universalSiteApi.addRootSiteRoleAssignmentForListItem(rootsitename, listitemfields.getOdataEditLink(), 
					String.valueOf(usermap.get(exposedTo[i])), String.valueOf(rolemap.get(exposedPermissions[i])));

		}
		
		ItemRoleAssignment rolesBeforeRemediation = universalSiteApi.getRoleAssignmentForRootSiteItem(rootsitename, listitemfields.getOdataEditLink());
		
		Reporter.log("3. Roles before remediation:"+MarshallingUtils.marshall(rolesBeforeRemediation), true);
		
		//sleep(CommonConstants.THREE_MINUTES_SLEEP);
		sleep(CommonConstants.TWO_MINUTES_SLEEP);
		
		CustomAssertion.assertTrue(checkDocumentInDB(elapp.el_office_365.name(), fileResource.getUniqueId()), "Exposed document is available in db", "Exposed document is not available in db");
		
		
		UIRemediationObject UIRemedObject =  getUIRemediationObject(suiteData.getSaasAppUsername(), "Folder",
																		suiteData.getDomainName(), fileResource.getUniqueId(),  remedialAction, metaInfo, "Sites", null);
		Reporter.log("Request body:" + MarshallingUtils.marshall(UIRemedObject), true);
		
		Reporter.log("4. Applying the remediation..",  true);

		//Remediate the exposure
		remediateExposureWithUIServer(UIRemedObject);
		
		//sleep(CommonConstants.THREE_MINUTES_SLEEP);
		sleep(CommonConstants.TWO_MINUTES_SLEEP);
		
		ItemRoleAssignment rolesAfterRemediation = universalSiteApi.getRoleAssignmentForRootSiteItem(rootsitename, listitemfields.getOdataEditLink());
		Reporter.log("5. Roles after remediation:"+MarshallingUtils.marshall(rolesAfterRemediation), true);
		
		SecurletDocument document = this.getDocuments(elapp.el_office_365.name(), fileResource.getUniqueId());
		System.out.println(MarshallingUtils.marshall(document));
		
		CustomAssertion.assertTrue(checkDocumentInDB(elapp.el_office_365.name(), fileResource.getUniqueId()), "Exposed document is available in db", "Exposed document is not available in db");

		Reporter.log("6. Verifying the remediation..",  true);
		document = this.getDocuments(elapp.el_office_365.name(), fileResource.getUniqueId());
		
		ArrayList<String> als = new ArrayList<String>(Arrays.asList(expectedExposureAfterRemediation));
		
		if(als.contains("internal")) {
			CustomAssertion.assertTrue(document.getObjects().get(0).getExposures().getAllInternal(), "Internal Exposure is present", "Internal Exposure is not present");
		}
		
		if(als.contains("nointernal")) {
			CustomAssertion.assertTrue(!document.getObjects().get(0).getExposures().getAllInternal(), "Internal Exposure is not present", "Internal Exposure is present");
		}
		
		if(als.contains("public")) {
			CustomAssertion.assertTrue(document.getObjects().get(0).getExposures().getPublic(), "Public exposure is present", "Public exposure is not present");
		}
		
		if(als.contains("nopublic")) {
			CustomAssertion.assertTrue(!document.getObjects().get(0).getExposures().getPublic(), "Public exposure is remediated", "Public exposure is not remediated");
		}
		
		if(als.contains("external")) {
			CustomAssertion.assertTrue(document.getObjects().get(0).getExposures().getExtCount() > 0, "External exposure is present", "No External exposure");
		}
		
		if(als.contains("noexternal")) {
			CustomAssertion.assertTrue(document.getObjects().get(0).getExposures().getExtCount() == 0, "No External exposure", "External exposure is present");
		}
		
		Reporter.log("7. Remediation applied successfully..",  true);
		
	}
	
	
	private UIRemediationObject getUIRemediationObject(String userId, String docType, String instance, String docId, String code[], String[] metaInfo, String objectType, String[] collabUpdateRole) {
		SecurletRemediation remediationObject =  getOneDriveRemediationObject(userId, docType, 
				instance, docId,  code, metaInfo, objectType, collabUpdateRole );
		
		UIRemediationObject UIRemedObject = new UIRemediationObject();
		UIRemediationSource UISource = new UIRemediationSource();
		UIRemediationInnerObject UIInnerObject = new UIRemediationInnerObject();
		ArrayList<SecurletRemediation> remedList = new ArrayList<SecurletRemediation>();
		remedList.add(remediationObject);
		UIInnerObject.setObjects(remedList);
		UISource.setObjects(UIInnerObject);
		UISource.setApp("Office 365");
		UIRemedObject.setSource(UISource);
		return UIRemedObject;
	}
	
	
	private SecurletRemediation getOneDriveRemediationObject(String userId, String docType, String instance, String docId, String code[], String[] metaInfo, String objectType, String[] collabUpdateRole ) {
		SecurletRemediation remediation = new SecurletRemediation();

		remediation.setDbName(suiteData.getTenantName().toLowerCase());
		remediation.setUser(suiteData.getSaasAppUsername());
		remediation.setUserId("");
		remediation.setDocType(docType);
		remediation.setDocId(docId);
		remediation.setInstance(instance);
		remediation.setObjectType(objectType);
		
		List<RemedialAction> actions = new ArrayList<RemedialAction>();
		
		
		for (int i = 0; i < code.length; i++) {
			RemedialAction remedAction = new RemedialAction();
			if (code[i].equals("COLLAB_UPDATE")) {
				remedAction.setCode(code[i]);
				remedAction.setReadonlyValues(null);
				remedAction.setPossibleValues(possibleCollabUpdateValues);
				RemediationMetaInfo remedMetaInfo = new RemediationMetaInfo();
				String[] collabs = StringUtils.split(metaInfo[i], ",");
				remedMetaInfo.setRole(collabUpdateRole[i]);
				remedMetaInfo.setCollabs(Arrays.asList(collabs));
				remedAction.setMetaInfo(remedMetaInfo);
				actions.add(remedAction);
			}
			
			if (code[i].equals("COLLAB_REMOVE")) {
				remedAction.setCode(code[i]);
				remedAction.setReadonlyValues(null);
				RemediationMetaInfo remedMetaInfo = new RemediationMetaInfo();
				String[] collabs = StringUtils.split(metaInfo[i], ",");
				remedMetaInfo.setCollabs(Arrays.asList(collabs));
				remedAction.setMetaInfo(remedMetaInfo);
				actions.add(remedAction);
			}
			
			if (code[i].equals("UNSHARE")) {
				remedAction.setCode(code[i]);
				remedAction.setReadonlyValues(readonlyValues);
				RemediationMetaInfo remedMetaInfo = new RemediationMetaInfo();
				remedMetaInfo.setCurrentLink(metaInfo[i]);
				remedAction.setMetaInfo(remedMetaInfo);
				remedMetaInfo.setCollabs(null);
				actions.add(remedAction);
			}
			
			if (code[i].equals("SHARE_ACCESS")) {
				remedAction.setCode(code[i]);
				remedAction.setPossibleValues(possibleValues);
				RemediationMetaInfo remedMetaInfo = new RemediationMetaInfo();
				//remedMetaInfo.setCurrentLink(metaInfo[i]);
				remedMetaInfo.setAccess(metaInfo[i]);
				remedMetaInfo.setCollabs(null);
				remedAction.setMetaInfo(remedMetaInfo);
				actions.add(remedAction);
			}
			
			
		}
		
		remediation.setActions(actions);
		return remediation;
		
	}
	
	
	@Test(priority = -10, groups={"LIST", "ONEDRIVE", "P1"})
	public void performSPListOperations() throws Exception {

		//Update the Document list title
		Result result = universalApi.getSPDocumentList();
		Metadata metadata = new Metadata();
		metadata.setType(result.getMetadata().getType());
		UpdateList ulist = new UpdateList();
		ulist.setTitle(query + "_AutomationList");
		ulist.setMetadata(metadata);
		universalApi.updateSPList(result.getId(), ulist);
		onedrivelog.setDocumentListEditLog(onedrivelog.getDocumentListEditLog());
		Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
		
		//After update, get the document list again
		//result = universalApi.getSPDocumentList();
		//CustomAssertion.assertEquals(result.getTitle(), ulist.getTitle(), "Title " + ulist.getTitle() +" not updated correctly in onedrive ");
		
		//break the permission scope
		universalApi.breakRoleInheritanceForList(result.getId(), true, true);
		Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
		
		//Add list edit permissions to domain user
		String domainuser = "testuser1";
		String username = domainuser +"@"+ StringUtils.split(this.saasAppUserAccount.getUsername(), "@")[1].toLowerCase();
		String permission = "Edit";
		addListPermissionsToDomainUser(domainuser, permission);
		onedrivelog.setDocumentListShareLog(onedrivelog.getDocumentListShareLog().replace("{username}", username).replace("{permission}", permission));
		Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
		
		//Disable sharing
		Result documentResult = universalApi.getSPDocumentList();
		universalApi.disableDocumentListSharing(documentResult);
		onedrivelog.setDocumentListUnShareLog(onedrivelog.getDocumentListUnShareLog());
		onedrivelog.setDocumentListUserUnShareLog(onedrivelog.getDocumentListUserUnShareLog().replace("{username}", username).replace("{permission}", permission));
		Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
		Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
		
		//Share the document list
		SharingUserRoleAssignment shareObject = onedriveUtils.getDocumentShareObject(documentResult, 1, "Everyone");
		DocumentSharingResult docSharingResult = universalApi.shareWithCollaborators(shareObject);
		Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
		//Thread.sleep(CommonConstants.THREE_MINUTES_SLEEP);		
	}
	
	
	
	
	
	
	
	//Site related tests
	
	//This test uploads the files to root level site
	@Test(groups={"SITEFILEUPLOAD", "ONEDRIVE", "P1"})
	public void uploadFileToTheRootSite() throws Exception {
		//String rootsiteUrl, String filename, String foldername, String destinationFilename
		
		
		SiteFolderInput sfi = new SiteFolderInput();
		sfi.setServerRelativeUrl("/Sites/"+rootsitename+"/Shared Documents/"+destinationFolder);
		SiteFileResource folderResource = universalSiteApi.createRootSiteFolder(rootsitename, sfi);
		
		ListItemAllFields listitemfields = universalSiteApi.getRootSiteFolderListItemAllFields(rootsitename, folderResource.getServerRelativeUrl());
		
		universalSiteApi.breakRoleInheritanceForRootSiteListItem(rootsitename, listitemfields.getOdataEditLink(), false, false);
		
		universalSiteApi.addRootSiteRoleAssignmentForListItem(rootsitename, listitemfields.getOdataEditLink(), 
				String.valueOf(usermap.get("Everyone")), String.valueOf(rolemap.get("Design")));
		
		
		sleep(CommonConstants.FIVE_MINUTES_SLEEP);
		universalSiteApi.disableRootSiteItemSharing(rootsitename, listitemfields.getOdataEditLink());
		
		/*
		 * //Share the folder
		SharingUserRoleAssignment shareObject = onedriveUtils.getFolderShareObject(folder, 1, "Everyone except external users");
		DocumentSharingResult docSharingResult = universalApi.shareWithCollaborators(shareObject);
		onedrivelog.setFolderShareLog(onedrivelog.getFolderShareLog().replace("{foldername}", destinationFolder)
																	.replace("{username}", "Everyone except external users")
																	.replace("{permission}", "Read"));
		onedrivelog.setFolderScopeChangeLog(onedrivelog.getFolderScopeChangeLog().replace("{foldername}", destinationFolder));
		Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
		
		//Disable sharing
		universalApi.disableFolderSharing(destinationFolder);
		onedrivelog.setFolderUnShareLog(onedrivelog.getFolderUnShareLog().replace("{foldername}", destinationFolder)
																	.replace("{username}", "Everyone except external users")
																	.replace("{permission}", "Read"));
		Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
		 */
		
		/*
		SiteFileResource fileResource = universalSiteApi.uploadFileToRootSite(rootsiteUrl, "HIPAA_Test2.txt", "/", "MayurExample.txt");
		ListItemAllFields listitemfields = universalSiteApi.getRootSiteListItemAllFieldsByUrl(rootsiteUrl, "Shared Documents", "MayurExample.txt");
		
		universalSiteApi.breakRoleInheritanceForRootSiteListItem(rootsiteUrl, listitemfields.getOdataEditLink(), false, false);
		
		universalSiteApi.addRootSiteRoleAssignmentForListItem(rootsiteUrl, listitemfields.getOdataEditLink(), 
				String.valueOf(usermap.get("Everyone except external users")), String.valueOf(rolemap.get("Read")));
		*/
		
		
		/*
		SharingUserRoleAssignment shareObject  = onedriveUtils.getRootSiteFileShareObject(fileUrl, 2, "Everyone");
		DocumentSharingResult docSharingResult =  universalSiteApi.getRootSiteFileSharedLink(rootsiteUrl, shareObject);
		Reporter.log(MarshallingUtils.marshall(docSharingResult), true);
		 */
		/*
		SiteFolderInput sfi = new SiteFolderInput();
		//{ "ServerRelativeUrl": "/AutoTeamSite/Shared%20Documents/SampleFolder2"}
		sfi.setServerRelativeUrl("/Sites/"+rootsiteUrl+"/Shared Documents/SecurletFolder");
		
		SiteFileResource folderResource = universalSiteApi.createRootSiteFolder(rootsiteUrl, sfi);
		//SiteFileResource fileResource 	= universalSiteApi.uploadFileToRootSite(rootsiteUrl, "HIPAA_Test2.txt", "Shared Documents/SecurletFolder", "File4.txt");
		
		Thread.sleep(CommonConstants.THREE_MINUTES_SLEEP);
		
		ListItemAllFields folderItemfields = universalSiteApi.getRootSiteFolderListItemAllFields(rootsiteUrl, sfi.getServerRelativeUrl());
		
		SiteFolderUpdateInput sfuinput = new SiteFolderUpdateInput();
		Metadata metadata = new Metadata();
		sfuinput.setFileLeafRef("SecurletFolderRenamed");
		metadata.setType(folderItemfields.getOdataType());
		sfuinput.setMetadata(metadata);
		
		System.out.println(MarshallingUtils.marshall(sfuinput));
		universalSiteApi.renameRootSiteFolder(rootsiteUrl, sfi.getServerRelativeUrl(), sfuinput);
		
		Thread.sleep(CommonConstants.THREE_MINUTES_SLEEP);
		sfi.setServerRelativeUrl(sfi.getServerRelativeUrl().replace("SecurletFolder", "SecurletFolderRenamed"));
		universalSiteApi.deleteRootSiteFolder(rootsiteUrl, folderResource.getServerRelativeUrl());
		*/
		/*
		SiteFileResource fileResource = universalSiteApi.uploadFileToRootSite(rootsiteUrl, "HIPAA_Test2.txt", "/", "File4.txt");
		ListItemAllFields listitemfields = universalSiteApi.getRootSiteListItemAllFieldsByUrl(rootsiteUrl, "Shared Documents", "File4.txt");
		universalSiteApi.breakRoleInheritanceForRootSiteListItem(rootsiteUrl, listitemfields.getOdataEditLink(), false, false);
		
		//Share the file
		universalSiteApi.addRootSiteRoleAssignmentForListItem(rootsiteUrl, listitemfields.getOdataEditLink(),String.valueOf(usermap.get("Everyone")), String.valueOf(rolemap.get("Contribute")));
		
		
		
		universalSiteApi.disableRootSiteItemSharing(rootsiteUrl, listitemfields.getOdataEditLink());
		
		
		RecycleItem recycledItem = universalSiteApi.recycleRootSiteFileItem(rootsiteUrl, fileResource.getServerRelativeUrl(), false);
		Thread.sleep(180000);
		
		universalSiteApi.restoreRootSiteRecyleBinItem(rootsiteUrl, recycledItem.getValue());
		*/
		/*
		String fileUrl = "https://securletdddo365beatle.sharepoint.com/sites/securletautomationsite/Shared Documents/shareFile3.txt";
		SharingUserRoleAssignment shareObject  = onedriveUtils.getRootSiteFileShareObject(fileUrl, 2, "Everyone");
		DocumentSharingResult docSharingResult =  universalSiteApi.getRootSiteFileSharedLink(rootsiteUrl, shareObject);
		Reporter.log(MarshallingUtils.marshall(docSharingResult), true);
		
		
		//Preauthorized access url
		AccessUrl accessUrl = universalSiteApi.getRootSiteFilePreAuthorizedAccessUrl(rootsiteUrl, fileResource.getServerRelativeUrl(), 24);
		*/
		
		
		
		
		/*
		ListItemAllFields listitemfields = universalSiteApi.getRootSiteListItemAllFieldsByUrl(rootsiteUrl, "Shared Documents", "exposeHIPAA_Test2.txt");
		
		universalSiteApi.breakRoleInheritanceForRootSiteListItem(rootsiteUrl, listitemfields.getOdataEditLink(), false, false);
		universalSiteApi.addRootSiteRoleAssignmentForListItem(rootsiteUrl, listitemfields.getOdataEditLink(),String.valueOf(usermap.get("Everyone")), String.valueOf(rolemap.get("Contribute")));
		*/
		
		
		
		//Web/Lists(guid'dc94ca7d-d09e-4916-b746-d596d4820cf4')/Items(3)
		//Web/Lists(guid'dc94ca7d-d09e-4916-b746-d596d4820cf4')/Items(3)
		/*
		shareObject = onedriveUtils.getPublicShareObject(itemResource.getWebUrl(), role);
		DocumentSharingResult docSharingResult = universalApi.shareWithCollaborators(shareObject);
		*/
		/*
		universalSiteApi.listAllFilesForRootSiteFolder(rootsiteUrl, "/");
		
		ItemCollection itemlist = universalSiteApi.getRootSiteDocumentListItems(rootsiteUrl);
		
		for (ListItemValue lv : itemlist.getValue()) {
			Reporter.log(lv.getGUID(), true);
			Reporter.log(lv.getOdataEditLink(), true);
		}
		*/
		/*
		//universalSiteApi.uploadFileToSite(uniqueFilename, "Documents", "AutoTeamSite", "Hello.txt");
		SiteFileResource fileResource = universalSiteApi.uploadFileToRootSite(rootsiteUrl, "HIPAA_Test2.txt", "/", "HIPAA_Test2.txt");
		Reporter.log("FileResource:"+MarshallingUtils.marshall(fileResource), true);
		addRootSiteRoleAssignmentForListItem
		*
		*/
	}
	
	
	@Test(priority = -6, dataProviderClass = O365DataProvider.class, dataProvider = "SubSiteDataProvider", groups={"SITE", "ONEDRIVE", "P1"})
	public void createAndVerifySPSiteOperations(boolean isThisLastRow, Integer language, String siteUrl, String siteTitle, String siteDescription, String webtemplate, boolean setUseUniquePermissions) throws Exception {
		
		String rootSiteName =  suiteData.getScheme() +"://" + this.suiteData.getDomainName() +".sharepoint.com";
		String steps[] = 
			{	"1. This test will create a site with url "+ siteUrl+ " under the site collection "+rootSiteName, 
				"2. After creating the site incrementally wait upto 60 mins and verify site creation logs."};

		LogUtils.logTestDescription(steps);
		
		LogUtils.logStep("1. Deleting the site if already exists. Ignore if the response is 404 ");
		universalSiteApi.deleteSite(siteUrl);
		
		LogUtils.logStep("2. Creating the site with url "+siteUrl+ " and with title "+ siteTitle);
		Parameters siteParams = new Parameters(language, siteUrl, siteTitle, siteDescription, webtemplate, setUseUniquePermissions);
		SiteInput si =  new SiteInput();
		si.setParameters(siteParams);
		SiteResponse siteResponse = universalSiteApi.createSite(si);
		
		String subSiteUrl = rootSiteName + "/" + siteUrl;
		onedrivelog = new OneDriveActivityLog();
		onedrivelog.setSiteCreateLog(onedrivelog.getSiteCreateLog().replace("{sitename}", subSiteUrl));
		
		createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
		OneDriveBusinessActivity subsiteCreateLog = new OneDriveBusinessActivity(onedrivelog.getSiteCreateLog(), createdTime, Severity.informational.name(),  
														ObjectType.Site.name(), ActivityType.SubSiteCreated.name(), subSiteUrl, 
														siteTitle, "Sites", rootSiteName,  null, null, "Office 365");
		
		activityMap.put(siteUrl+"createLog", subsiteCreateLog);
		LogUtils.logStep("3. Response from site creation ::"+ MarshallingUtils.marshall(siteResponse));
		
		
		
		if (isThisLastRow) { 
		LogUtils.logStep("4. Trying to fetch the logs after site creation...");
		LogUtils.logStep("5. Will wait max 60 mins till I get the logs...");
			//Fetch the logs
			try {

				for (int i = 1; i <= (60 * 60 * 1000); i+=300000 ) {
					sleep(CommonConstants.FIVE_MINUTES_SLEEP);
					HashMap<String, String> termmap = new HashMap<String, String>();
					termmap.put("facility", "Office 365");
					termmap.put("Object_type", ObjectType.Site.name());
					termmap.put("Activity_type", ActivityType.SubSiteCreated.name());

					//Get site related logs
					siteCreateLogs = this.getInvestigateLogs(-60, 10, "Office 365", termmap, suiteData.getUsername(), suiteData.getApiserverHostName(), 
							suiteData.getCSRFToken(), suiteData.getSessionID(), 0, 100, "Office 365");

					if (siteCreateLogs.getHits().getTotal() > 0) {
						LogUtils.logStep("5. Site create logs available now. Breaking the loop...");
						break;
					}
				}
			}
			catch(Exception e) {}
			Reporter.log(MarshallingUtils.marshall(siteCreateLogs), true);
			long total = siteCreateLogs.getHits().getTotal();
			Reporter.log("Total Site Create logs count:"+ total, true);
		}
	}
	
	
	@Test(priority = -5, dataProviderClass = O365DataProvider.class, dataProvider = "SubSiteDataProvider", groups={"SITE", "ONEDRIVE", "P1"})
	public void verifySiteCreateActivity(boolean isThisLastRow, Integer language, String siteUrl, String siteTitle, String siteDescription, String webtemplate, boolean setUseUniquePermissions) throws Exception {
		LogUtils.logTestDescription("Verify the site creation activity for the site created with the url "+ siteUrl);
		logValidator.verifySiteActivityLog(siteCreateLogs, activityMap.get(siteUrl+"createLog"));
	}
	
	
	
	//@Test(priority = -4, dataProviderClass = O365DataProvider.class, dataProvider = "SubSiteDataProvider", groups={"SITE", "ONEDRIVE", "P1"})
	public void updateAndVerifySPSiteOperations(boolean isThisLastRow, Integer language, String siteUrl, String siteTitle, String siteDescription, String webtemplate, boolean setUseUniquePermissions) throws Exception {
		String rootSiteName =  suiteData.getScheme() +"://" + this.suiteData.getDomainName() +".sharepoint.com";
		String steps[] = 
			{	"1. This test will update the description and title for the site with url "+ siteUrl+ " under the site collection "+rootSiteName, 
				"2. After updating the site incrementally wait for 5 mins and verify site updation logs."};
		
		LogUtils.logTestDescription(steps);
		
		Parameters siteUpdateParams = new Parameters();
		siteUpdateParams.setDescription(siteDescription + " Updated");
		siteUpdateParams.setTitle(siteTitle + " Updated");
		siteUpdateParams.setUrl(siteUrl);
		universalSiteApi.updateSite(siteUpdateParams);
		
		String subSiteUrl = rootSiteName + "/" + siteUrl;
		onedrivelog = new OneDriveActivityLog();
		onedrivelog.setSiteUpdateLog(onedrivelog.getSiteUpdateLog().replace("{sitename}", subSiteUrl));
		
		createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
		OneDriveBusinessActivity subsiteUpdateLog = new OneDriveBusinessActivity(onedrivelog.getSiteUpdateLog(), createdTime, Severity.informational.name(),  
														ObjectType.Site.name(), ActivityType.SubSiteCreated.name(), subSiteUrl, 
														siteTitle + " Updated", "Sites", rootSiteName,  null, null, "Office 365");
		
		activityMap.put(siteUrl+"updateLog", subsiteUpdateLog);
		
		
		if (isThisLastRow) { 
			LogUtils.logStep("4. Trying to fetch the logs after site updation...");
			LogUtils.logStep("5. Will wait max 5 mins till I get the logs...");
				//Fetch the logs
				try {

					for (int i = 1; i <= (5 * 60 * 1000); i+=60000 ) {
						sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
						HashMap<String, String> termmap = new HashMap<String, String>();
						termmap.put("facility", "Office 365");
						termmap.put("Object_type", ObjectType.Site.name());
						termmap.put("Activity_type", ActivityType.Edit.name());

						//Get site related logs
						siteUpdateLogs = this.getInvestigateLogs(-60, 10, "Office 365", termmap, suiteData.getUsername(), suiteData.getApiserverHostName(), 
								suiteData.getCSRFToken(), suiteData.getSessionID(), 0, 100, "Office 365");

						if (siteUpdateLogs.getHits().getTotal() > 0) {
							LogUtils.logStep("5. Site update logs available now. Breaking the loop...");
							break;
						}
					}
				}
				catch(Exception e) {}
				Reporter.log(MarshallingUtils.marshall(siteUpdateLogs), true);
				long total = siteUpdateLogs.getHits().getTotal();
				Reporter.log("Total Site Update logs count:"+ total, true);
			}
		
	}
	
	
	
	//@Test(priority = -3, dataProviderClass = O365DataProvider.class, dataProvider = "SubSiteDataProvider", groups={"SITE", "ONEDRIVE", "P1"})
	public void verifySiteUpdateActivity(boolean isThisLastRow, Integer language, String siteUrl, String siteTitle, String siteDescription, String webtemplate, boolean setUseUniquePermissions) throws Exception {
		LogUtils.logTestDescription("Verify the site updation activity for the site updated with the url "+ siteUrl);
		logValidator.verifySiteActivityLog(siteUpdateLogs, activityMap.get(siteUrl+"updateLog"));
	}
	
	
	
	@Test(priority = -2, dataProviderClass = O365DataProvider.class, dataProvider = "SubSiteDataProvider", groups={"SITE", "ONEDRIVE", "P1"})
	public void deleteAndVerifySiteOperations(boolean isThisLastRow, Integer language, String siteUrl, String siteTitle, String siteDescription, String webtemplate, boolean setUseUniquePermissions) throws Exception {
		String rootSiteName =  suiteData.getScheme() +"://" + this.suiteData.getDomainName() +".sharepoint.com";
		String steps[] = 
			{	"1. This test will delete the site with url "+ siteUrl+ " under the site collection "+rootSiteName, 
				"2. After delete the site incrementally wait for 5 mins and verify site deletion logs."};
		
		LogUtils.logTestDescription(steps);
		
		universalSiteApi.deleteSite(siteUrl);
		
		String subSiteUrl = rootSiteName + "/" + siteUrl;
		onedrivelog = new OneDriveActivityLog();
		onedrivelog.setSiteDeleteLog(onedrivelog.getSiteDeleteLog().replace("{sitename}", subSiteUrl));
		
		createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
		OneDriveBusinessActivity subsiteDeleteLog = new OneDriveBusinessActivity(onedrivelog.getSiteDeleteLog(), createdTime, Severity.informational.name(),  
														ObjectType.Site.name(), ActivityType.SubSiteDeleted.name(), subSiteUrl, 
														siteTitle + " Updated", "Sites", rootSiteName,  null, null, "Office 365");
		
		activityMap.put(siteUrl+"deleteLog", subsiteDeleteLog);
		
		
		if (isThisLastRow) { 
			LogUtils.logStep("4. Trying to fetch the logs after site deletion...");
			LogUtils.logStep("5. Will wait max 60 mins till I get the logs...");
				//Fetch the logs
				try {

					for (int i = 1; i <= (60 * 60 * 1000); i+=60000 ) {
						sleep(CommonConstants.FIVE_MINUTES_SLEEP);
						HashMap<String, String> termmap = new HashMap<String, String>();
						termmap.put("facility", "Office 365");
						termmap.put("Object_type", ObjectType.Site.name());
						termmap.put("Activity_type", ActivityType.SubSiteDeleted.name());

						//Get site related logs
						siteDeleteLogs = this.getInvestigateLogs(-60, 10, "Office 365", termmap, suiteData.getUsername(), suiteData.getApiserverHostName(), 
								suiteData.getCSRFToken(), suiteData.getSessionID(), 0, 100, "Office 365");

						if (siteDeleteLogs.getHits().getTotal() > 0) {
							LogUtils.logStep("5. Site delete logs available now. Breaking the loop...");
							break;
						}
					}
				}
				catch(Exception e) {}
				Reporter.log(MarshallingUtils.marshall(siteDeleteLogs), true);
				long total = siteDeleteLogs.getHits().getTotal();
				Reporter.log("Total Site Delete logs count:"+ total, true);
			}
		
	}
	
	
	@Test(priority = -1, dataProviderClass = O365DataProvider.class, dataProvider = "SubSiteDataProvider", groups={"SITE", "ONEDRIVE", "P1"})
	public void verifySiteDeleteActivity(boolean isThisLastRow, Integer language, String siteUrl, String siteTitle, String siteDescription, String webtemplate, boolean setUseUniquePermissions) throws Exception {
		LogUtils.logTestDescription("Verify the site deletion activity for the site deleted with the url "+ siteUrl);
		logValidator.verifySiteActivityLog(siteDeleteLogs, activityMap.get(siteUrl+"deleteLog"));
	}
	
	
	
	//Site related tests
	
	//@Test(priority = -10, dataProviderClass = O365DataProvider.class, dataProvider = "SubSiteDataProvider", groups={"SITE", "ONEDRIVE", "P1"})
	public void performSPSiteOperations(Integer language, String siteUrl, String siteTitle, String siteDescription, String webtemplate, boolean setUseUniquePermissions) throws Exception {
		
		Parameters siteParams = new Parameters(language, siteUrl, siteTitle, siteDescription, webtemplate, setUseUniquePermissions);
		universalSiteApi.deleteSite(siteUrl);
		
		SiteInput si =  new SiteInput();
		si.setParameters(siteParams);
		String siteResponse = universalSiteApi.createSite(si);
		
		//Update Site
		Parameters siteUpdateParams = new Parameters();
		siteUpdateParams.setDescription(siteDescription + " Updated");
		siteUpdateParams.setTitle(siteTitle + " Updated");
		siteUpdateParams.setUrl(siteUrl);
		universalSiteApi.updateSite(siteUpdateParams);
		
	}
	
	
	@Test(priority = -10, groups={"SITE1", "ONEDRIVE", "P1"})
	public void FileUploadToSite() throws Exception {
		universalSiteApi.uploadFileToSite(uniqueFilename, "Documents", "AutoTeamSite", "Hello.txt");
	}
	
	//Add permission to a list
	public void addListPermissionsToDomainUser(String username, String permissionname) throws Exception {
		//Get the document list
		Result documentResult  = universalApi.getSPDocumentList();
		//Reporter.log("Document list result:" + marshall(documentResult), true);
		
		String principalId = "";
		String roleDefId = "";
		SiteUserList sulist = universalApi.getSPUserList();
		
		for(UserResult userResult : sulist.getD().getResults()) {
			if (userResult.getEmail().contains(username)) {
				principalId = String.valueOf(userResult.getId());
			}
		}
		
		//Permission name can be Edit/Contribute/Read
		RoleDefinitions roleDefinitions = universalApi.getSharePointRolesDefinitions();
		for (UserValue roleValue : roleDefinitions.getValue()) {
			if (roleValue.getName().equals("Edit")) {
				roleDefId = String.valueOf(roleValue.getId());
			}
		}
		universalApi.addSharePointListRolesAssignments(principalId, roleDefId);
	}
	
}