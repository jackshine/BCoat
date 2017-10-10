package com.elastica.beatle.tests.securlets;

import java.io.File;
import java.net.URI;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang.RandomStringUtils;
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
import com.elastica.beatle.dci.DCIConstants;
import com.elastica.beatle.logger.Logger;
import com.elastica.beatle.securlets.BoxDataProvider;
import com.elastica.beatle.securlets.CIQValidator;
import com.elastica.beatle.securlets.DocumentValidator;
import com.elastica.beatle.securlets.ESQueryBuilder;
import com.elastica.beatle.securlets.LogUtils;
import com.elastica.beatle.securlets.LogValidator;
import com.elastica.beatle.securlets.O365DataProvider;
import com.elastica.beatle.securlets.SecurletUtils;
import com.elastica.beatle.securlets.SecurletsConstants;
import com.elastica.beatle.securlets.SecurletUtils.elapp;
import com.elastica.beatle.securlets.SecurletUtils.facility;
import com.elastica.beatle.securlets.dto.CiqProfile;
import com.elastica.beatle.securlets.dto.ExposureTotals;
import com.elastica.beatle.securlets.dto.ForensicSearchResults;
import com.elastica.beatle.securlets.dto.Hit;
import com.elastica.beatle.securlets.dto.SecurletDocument;
import com.elastica.beatle.securlets.dto.Source;
import com.elastica.beatle.securlets.dto.UIExposedDoc;
import com.elastica.beatle.securlets.dto.VulnerabilityTypes;
import com.universal.common.Office365MailActivities;
import com.universal.constants.CommonConstants;
import com.universal.dtos.onedrive.DocumentSharingResult;
import com.universal.dtos.onedrive.Folder;
import com.universal.dtos.onedrive.ItemResource;
import com.universal.dtos.onedrive.ListItemAllFields;
import com.universal.dtos.onedrive.Metadata;
import com.universal.dtos.onedrive.Parameters;
import com.universal.dtos.onedrive.PasswordProfile;
import com.universal.dtos.onedrive.RecycleItem;
import com.universal.dtos.onedrive.Result;
import com.universal.dtos.onedrive.RoleDefinitions;
import com.universal.dtos.onedrive.SharingUserRoleAssignment;
import com.universal.dtos.onedrive.SiteInput;
import com.universal.dtos.onedrive.SiteUserList;
import com.universal.dtos.onedrive.UpdateList;
import com.universal.dtos.onedrive.UserInput;
import com.universal.dtos.onedrive.UserList;
import com.universal.dtos.onedrive.UserResult;
import com.universal.dtos.onedrive.UserValue;
import com.universal.dtos.onedrive.Value;

import microsoft.exchange.webservices.data.core.service.item.EmailMessage;
import microsoft.exchange.webservices.data.core.service.item.Item;
import microsoft.exchange.webservices.data.property.complex.Attachment;
import microsoft.exchange.webservices.data.property.complex.AttachmentCollection;
import microsoft.exchange.webservices.data.property.complex.FileAttachment;



public class OneDriveBusinessSecurletTests extends SecurletUtils {
	ESQueryBuilder esQueryBuilder = null;
	LogValidator logValidator;
	CIQValidator ciqValidator;
	OneDriveActivityLog onedrivelog;
	OneDriveUtils onedriveUtils;
	
	ArrayList<String> messages = new ArrayList<String>();
	ArrayList<String> actualUserMessages = new ArrayList<String>();
	ArrayList<String> actualFileMessages = new ArrayList<String>();
	ArrayList<String> actualShareMessages = new ArrayList<String>();
	ArrayList<String> actualFolderMessages = new ArrayList<String>();
	ArrayList<String> actualGroupMessages = new ArrayList<String>();
	ArrayList<String> actualListMessages = new ArrayList<String>();
	String createdTime;
	String resourceId;
	private String destinationFile, destinationDocFile, destinationPptFile, destinationExcelFile, destinationI18NFile, renamedI18NFile;
	private String destinationFolder;
	private String renamedFile = "HIPAA_Test2_rename.txt";
	private String query;
	private String uniqueShareId;
	DocumentValidator docValidator;
	HashMap<String, OneDriveBusinessActivity> activityMap = new HashMap<String, OneDriveBusinessActivity>();
	//String filename;
	private String uniqueFilename = "HIPAA_Test2.txt";
	private String uniqueFolder1  = "Securlets_Automation";
	
	private String docFile = "AutoDoc.docx";
	private String pptFile = "AutoPPT.pptx";
	private String excelFile = "AutoSurvey.xlsx";
	
	String appName = "Office 365";
	
	ForensicSearchResults fileLogs, folderLogs, contentInspectionLogs, file18nLogs, siteLogs;
	long waitTime = 2;
	
	public OneDriveBusinessSecurletTests() throws Exception {
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
		destinationDocFile = uniqueId + "_" + docFile;
		destinationPptFile = uniqueId + "_" + pptFile;
		destinationExcelFile = uniqueId + "_" + excelFile;
	}
	
	@BeforeClass(alwaysRun=true)
	public void initOffice() throws Exception {
		AuthorizationHandler.disableAnonymization(suiteData);
		
	}
	
	//Refactored tests
	
	@Test(priority = -10, groups={"FILE", "ONEDRIVE", "P1"})
	public void performOneDriveFileOperations() throws Exception {

		//Upload file 
		ItemResource itemResource = universalApi.uploadSimpleFile("/", uniqueFilename, destinationFile);
		Reporter.log("Item Resource:"+ itemResource.getId(), true);
		Reporter.log("Item Name:"+ itemResource.getName(), true);
		Reporter.log("Item Size:"+ itemResource.getSize().longValue(), true);
		
		onedrivelog.setFileUploadLog(onedrivelog.getFileUploadLog().replace("{filename}", destinationFile));
		
		createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
		OneDriveBusinessActivity fileUploadLog = new OneDriveBusinessActivity(onedrivelog.getFileUploadLog(), createdTime, Severity.informational.name(),  
														ObjectType.File.name(), ActivityType.Upload.name(), suiteData.getSaasAppUsername(), 
														socUserName, getETagAsDocumentId(itemResource.getETag()), itemResource.getSize().longValue(),  "Root:Documents", "Office 365");
		
		activityMap.put("fileUploadLog", fileUploadLog);
		
		
		
		//upload doc file
		ItemResource docResource =universalApi.uploadSimpleFile("/", docFile, destinationDocFile);
		
		onedrivelog.setDocFileUploadLog(onedrivelog.getDocFileUploadLog().replace("{filename}", destinationDocFile));
		
		createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
		OneDriveBusinessActivity docFileUploadLog = new OneDriveBusinessActivity(onedrivelog.getDocFileUploadLog(), createdTime, Severity.informational.name(),  
														ObjectType.File.name(), ActivityType.Upload.name(), suiteData.getSaasAppUsername(), 
														socUserName, getETagAsDocumentId(docResource.getETag()), docResource.getSize().longValue(),  "Root:Documents", "Office 365");
		
		activityMap.put("docFileUploadLog", docFileUploadLog);
		
		//Upload ppt file
		ItemResource pptResource =universalApi.uploadSimpleFile("/", pptFile, destinationPptFile);
		
		onedrivelog.setPptFileUploadLog(onedrivelog.getPptFileUploadLog().replace("{filename}", destinationPptFile));
		
		createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
		OneDriveBusinessActivity pptFileUploadLog = new OneDriveBusinessActivity(onedrivelog.getPptFileUploadLog(), createdTime, Severity.informational.name(),  
														ObjectType.File.name(), ActivityType.Upload.name(), suiteData.getSaasAppUsername(), 
														socUserName, getETagAsDocumentId(pptResource.getETag()), pptResource.getSize().longValue(),  "Root:Documents", "Office 365");
		
		activityMap.put("pptFileUploadLog", pptFileUploadLog);
		
		//Upload excel file
		ItemResource excelResource =universalApi.uploadSimpleFile("/", excelFile, destinationExcelFile);
		onedrivelog.setExcelFileUploadLog(onedrivelog.getExcelFileUploadLog().replace("{filename}", destinationExcelFile));
		createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
		OneDriveBusinessActivity excelFileUploadLog = new OneDriveBusinessActivity(onedrivelog.getExcelFileUploadLog(), createdTime, Severity.informational.name(),  
														ObjectType.File.name(), ActivityType.Upload.name(), suiteData.getSaasAppUsername(), 
														socUserName, getETagAsDocumentId(excelResource.getETag()), excelResource.getSize().longValue(),  "Root:Documents", "Office 365");
		
		activityMap.put("excelFileUploadLog", excelFileUploadLog);
		
		Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
		
		//Rename file
		//Rename the file 
		itemResource.setName(renamedFile);
		itemResource= universalApi.renameItem(itemResource);
		Thread.sleep(CommonConstants.THIRTY_SECONDS_SLEEP);
		
		//Rename again
		itemResource.setName(destinationFile);
		itemResource= universalApi.renameItem(itemResource);
		onedrivelog.setFileRenameLog(onedrivelog.getFileRenameLog().replace("{filename}", destinationFile));
		
		createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
		OneDriveBusinessActivity fileRenameLog = new OneDriveBusinessActivity(onedrivelog.getFileRenameLog(), createdTime, Severity.informational.name(),  
														ObjectType.File.name(), ActivityType.Rename.name(), suiteData.getSaasAppUsername(), 
														socUserName, getETagAsDocumentId(itemResource.getETag()), itemResource.getSize(),  "Root:Documents", "Office 365");
		
		activityMap.put("fileRenameLog", fileRenameLog);
		
		Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
		
		//Disable all sharing
		universalApi.disableSharing(destinationFile);
		Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
		//User unshared 'Root:Documents' with 'Everyone except external users(Read)'.
		
		//Share the file
		SharingUserRoleAssignment shareObject = onedriveUtils.getFileShareObject(itemResource, 1, "Everyone except external users");
		//DocumentSharingResult docSharingResult = 
		universalApi.shareWithCollaborators(shareObject);
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
		
		Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
		
		//Share the file with external collaborator with edit permission
		SharingUserRoleAssignment shareCollabObject = onedriveUtils.getFileShareObject(itemResource, 2, "pushpan@gmail.com");
		universalApi.shareWithCollaborators(shareCollabObject);
		onedrivelog.setFileCollaboratorEditShareLog(onedrivelog.getFileCollaboratorEditShareLog().replace("{filename}", destinationFile)
																							.replace("{username}", "pushpan@gmail.com"));
		
		createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
		OneDriveBusinessActivity fileCollaboratorEditShareLog = new OneDriveBusinessActivity(onedrivelog.getFileCollaboratorEditShareLog(), createdTime, Severity.informational.name(),  
														ObjectType.File.name(), ActivityType.Share.name(), suiteData.getSaasAppUsername(), 
														socUserName, getETagAsDocumentId(itemResource.getETag()), itemResource.getSize(),  "Root:Documents", "Office 365");
		
		activityMap.put("fileCollaboratorEditShareLog", fileCollaboratorEditShareLog);
		
		Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
		//Disable sharing
		universalApi.disableSharing(destinationFile);
		onedrivelog.setFileCollaboratorEditUnshareLog(onedrivelog.getFileCollaboratorEditUnshareLog().replace("{filename}", destinationFile)
																	.replace("{username}", "pushpan@gmail.com"));
		
		createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
		OneDriveBusinessActivity fileCollaboratorEditUnshareLog = new OneDriveBusinessActivity(onedrivelog.getFileCollaboratorEditUnshareLog(), createdTime, Severity.informational.name(),  
																		ObjectType.File.name(), ActivityType.Unshare.name(), suiteData.getSaasAppUsername(), 
																		socUserName, getETagAsDocumentId(itemResource.getETag()), itemResource.getSize(),  "Root:Documents", "Office 365");
		
		activityMap.put("fileCollaboratorEditUnshareLog", fileCollaboratorEditUnshareLog);
		Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
		
		//Share the file with external collaborator with view permission
		shareCollabObject = onedriveUtils.getFileShareObject(itemResource, 1, "pushpan@gmail.com");
		universalApi.shareWithCollaborators(shareCollabObject);
		onedrivelog.setFileCollaboratorViewShareLog(onedrivelog.getFileCollaboratorViewShareLog().replace("{filename}", destinationFile)
																							.replace("{username}", "pushpan@gmail.com"));
		
		createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
		OneDriveBusinessActivity fileCollaboratorViewShareLog = new OneDriveBusinessActivity(onedrivelog.getFileCollaboratorViewShareLog(), createdTime, Severity.informational.name(),  
																		ObjectType.File.name(), ActivityType.Share.name(), suiteData.getSaasAppUsername(), 
																		socUserName, getETagAsDocumentId(itemResource.getETag()), itemResource.getSize(),  "Root:Documents", "Office 365");

		activityMap.put("fileCollaboratorViewShareLog", fileCollaboratorViewShareLog);
		Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
		
		//Disable sharing
		universalApi.disableSharing(destinationFile);
		onedrivelog.setFileCollaboratorViewUnshareLog(onedrivelog.getFileCollaboratorViewUnshareLog().replace("{filename}", destinationFile)
																							.replace("{username}", "pushpan@gmail.com"));
		
		createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
		OneDriveBusinessActivity fileCollaboratorViewUnshareLog = new OneDriveBusinessActivity(onedrivelog.getFileCollaboratorViewUnshareLog(), createdTime, Severity.informational.name(),  
																		ObjectType.File.name(), ActivityType.Unshare.name(), suiteData.getSaasAppUsername(), 
																		socUserName, getETagAsDocumentId(itemResource.getETag()), itemResource.getSize(),  "Root:Documents", "Office 365");

		activityMap.put("fileCollaboratorViewUnshareLog", fileCollaboratorViewUnshareLog);
		Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
		Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
		
		//Recycle the file
		RecycleItem recycleItem = universalApi.recyleItem(itemResource.getName(), false);
		onedrivelog.setFileDeleteLog(onedrivelog.getFileDeleteLog().replace("{filename}", destinationFile));
		
		createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
		OneDriveBusinessActivity fileDeleteLog = new OneDriveBusinessActivity(onedrivelog.getFileDeleteLog(), createdTime, Severity.informational.name(),  
										ObjectType.File.name(), ActivityType.Delete.name(), suiteData.getSaasAppUsername(), 
										socUserName, getETagAsDocumentId(itemResource.getETag()), itemResource.getSize(),  null, "Office 365");

		activityMap.put("fileDeleteLog", fileDeleteLog);
		Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
		
		//Restore the file
		universalApi.restoreRecycleBinItem(recycleItem.getValue());
		onedrivelog.setFileRestoreLog(onedrivelog.getFileRestoreLog().replace("{filename}", destinationFile));
		
		createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
		OneDriveBusinessActivity fileRestoreLog = new OneDriveBusinessActivity(onedrivelog.getFileRestoreLog(), createdTime, Severity.informational.name(),  
										ObjectType.File.name(), ActivityType.Restore.name(), suiteData.getSaasAppUsername(), 
										socUserName, getETagAsDocumentId(itemResource.getETag()), itemResource.getSize(),  "Root:Documents", "Office 365");

		activityMap.put("fileRestoreLog", fileRestoreLog);
		
		Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
		
		//Delete file
		universalApi.deleteFile(itemResource.getId(), itemResource.getETag());
		onedrivelog.setFileDeleteLog(onedrivelog.getFileDeleteLog().replace("{filename}", destinationFile));
		Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
		
		//Create the empty file and update the content
		itemResource = universalApi.createFile(destinationFile);
		Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
		universalApi.updateFileContent(itemResource.getId(), uniqueFilename);
		onedrivelog.setFileEditLog(onedrivelog.getFileEditLog().replace("{filename}", destinationFile));
		
		createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
		OneDriveBusinessActivity fileEditLog = new OneDriveBusinessActivity(onedrivelog.getFileEditLog(), createdTime, Severity.informational.name(),  
										ObjectType.File.name(), ActivityType.Edit.name(), suiteData.getSaasAppUsername(), 
										socUserName, getETagAsDocumentId(itemResource.getETag()), itemResource.getSize(),  "Root:Documents", "Office 365");

		activityMap.put("fileEditLog", fileEditLog);
		
		Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
		
		//Create folder
		String copyfoldername = "CP" + RandomStringUtils.randomAlphabetic(3);
		String movefoldername = "MV" + RandomStringUtils.randomAlphabetic(3);
		
		Folder copyfolder = universalApi.createFolderV2(copyfoldername);
		Folder movefolder = universalApi.createFolderV2(movefoldername);
		
		Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
		
		//Copy file to copy folder
		universalApi.copyFileTo(this.getETagAsDocumentId(itemResource.getETag()), copyfolder.getName(), destinationFile, true);
		onedrivelog.setFileCopyLog(onedrivelog.getFileCopyLog().replace("{filename}", destinationFile));
		
		createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
		OneDriveBusinessActivity fileCopyLog = new OneDriveBusinessActivity(onedrivelog.getFileCopyLog(), createdTime, Severity.informational.name(),  
										ObjectType.File.name(), ActivityType.Rename.name(), suiteData.getSaasAppUsername(), 
										socUserName, getETagAsDocumentId(itemResource.getETag()), itemResource.getSize(),  "Root:Documents", "Office 365");
		activityMap.put("fileCopyLog", fileCopyLog);
		Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
		
		
		//Move file to movefolder 
		universalApi.moveFileTo(this.getETagAsDocumentId(itemResource.getETag()), movefolder.getName(), destinationFile, 1);
		onedrivelog.setFileMoveLog(onedrivelog.getFileMoveLog().replace("{filename}", destinationFile));
		
		createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
		OneDriveBusinessActivity fileMoveLog = new OneDriveBusinessActivity(onedrivelog.getFileMoveLog(), createdTime, Severity.informational.name(),  
										ObjectType.File.name(), ActivityType.Upload.name(), suiteData.getSaasAppUsername(), 
										socUserName, getETagAsDocumentId(itemResource.getETag()), itemResource.getSize(),  "Root:Documents", "Office 365");
		activityMap.put("fileMoveLog", fileMoveLog);
		Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
		
		
		// Upload microsoft files like excel, docx, ppt
		
		
		//Fetch the logs
		try {

			for (int i = 1; i <= (waitTime * 60 * 1000); i+=60000 ) {
				sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
				HashMap<String, String> termmap = new HashMap<String, String>();
				termmap.put("facility", "Office 365");
				termmap.put("Object_type", ObjectType.File.name());
				
				//Get file related logs
				fileLogs = this.getInvestigateLogs(-30, 10, "Office 365", termmap, suiteData.getUsername(), suiteData.getApiserverHostName(), 
						suiteData.getCSRFToken(), suiteData.getSessionID(), 0, 100, "Office 365");
				
			}
		}
		catch(Exception e) {}
		Reporter.log(MarshallingUtils.marshall(fileLogs), true);
		long total = fileLogs.getHits().getTotal();
		Reporter.log("Total file logs count:"+ total, true);
		
		//If total number of logs is less than zero, no logs are seen so skip the tests
		if (total <= 0) {
			throw new SkipException("Skipping the dependent tests..No logs are seen for file in onedrive");
		}
		
		//Clean the file
		universalApi.deleteFile(itemResource.getId(), "*");
		
		//clean the folder
		universalApi.deleteFolderV2(copyfolder.getId(), copyfolder.getETag());
		universalApi.deleteFolderV2(movefolder.getId(), movefolder.getETag());
	}

	
	//File log verification Tests

	@Test(groups={"FILE", "ONEDRIVE", "P1"})
	public void verifyFileCopyLog() {
		LogUtils.logTestDescription("After copying the file , verify the activity in the activity logs.");
		logValidator.verifyOnedriveActivityLog(fileLogs, activityMap.get("fileCopyLog"));
	}
	
	@Test(groups={"FILE", "ONEDRIVE", "P1"})
	public void verifyFileMoveLog() {
		LogUtils.logTestDescription("After moving the file , verify the activity in the activity logs.");
		logValidator.verifyOnedriveActivityLog(fileLogs, activityMap.get("fileMoveLog"));
	}
	
	@Test(groups={"FILE", "ONEDRIVE", "P1"})
	public void verifyFileCollaboratorEditShareLog() {
		LogUtils.logTestDescription("After sharing a file with a collaborator with Edit permission , verify the activity in the activity logs.");
		logValidator.verifyOnedriveActivityLog(fileLogs, activityMap.get("fileCollaboratorEditShareLog"));
	}
	
	@Test(groups={"FILE", "ONEDRIVE", "P1"})
	public void verifyFileCollaboratorEditUnshareLog() {
		LogUtils.logTestDescription("After unsharing a file with a collaborator with Edit permission , verify the activity in the activity logs.");
		logValidator.verifyOnedriveActivityLog(fileLogs, activityMap.get("fileCollaboratorEditUnshareLog"));
	}
	
	
	@Test(groups={"FILE", "ONEDRIVE", "P1"})
	public void verifyFileCollaboratorViewShareLog() {
		LogUtils.logTestDescription("After Sharing a file with a collaborator with View permission , verify the activity in the activity logs.");
		logValidator.verifyOnedriveActivityLog(fileLogs, activityMap.get("fileCollaboratorViewShareLog"));
	}
	
	
	@Test(groups={"FILE", "ONEDRIVE", "P1"})
	public void verifyFileCollaboratorViewUnshareLog() {
		LogUtils.logTestDescription("After unsharing a file with a collaborator with View permission , verify the activity in the activity logs.");
		logValidator.verifyOnedriveActivityLog(fileLogs, activityMap.get("fileCollaboratorViewUnshareLog"));
	}
	
	
	@Test(groups={"FILE", "ONEDRIVE", "P1"})
	public void verifyFileUploadActivity() {
		LogUtils.logTestDescription("After uploading a file, verify the activity in the activity logs.");
		logValidator.verifyOnedriveActivityLog(fileLogs, activityMap.get("fileUploadLog"));
	}

	
	@Test(groups={"FILE", "ONEDRIVE", "P1"})
	public void verifyDocFileUploadActivity() {
		LogUtils.logTestDescription("After uploading a document file, verify the activity in the activity logs.");
		logValidator.verifyOnedriveActivityLog(fileLogs, activityMap.get("docFileUploadLog"));
	}
	
	
	@Test(groups={"FILE", "ONEDRIVE", "P1"})
	public void verifyPptFileUploadActivity() {
		LogUtils.logTestDescription("After uploading a PPT file, verify the activity in the activity logs.");
		logValidator.verifyOnedriveActivityLog(fileLogs, activityMap.get("pptFileUploadLog"));
	}
	
	@Test(groups={"FILE", "ONEDRIVE", "P1"})
	public void verifyExcelFileUploadActivity() {
		LogUtils.logTestDescription("After uploading a excel file, verify the activity in the activity logs.");
		logValidator.verifyOnedriveActivityLog(fileLogs, activityMap.get("excelFileUploadLog"));
	}
	

	@Test(groups={"FILE", "ONEDRIVE", "P1"})
	public void verifyFileDeleteActivity() {
		LogUtils.logTestDescription("After deleting a file, verify the activity in the activity logs.");
		logValidator.verifyOnedriveActivityLog(fileLogs, activityMap.get("fileDeleteLog"));
	}
	

	@Test(groups={"FILE", "ONEDRIVE", "P1"})
	public void verifyFileRestoreActivity() {
		LogUtils.logTestDescription("After restoring a file, verify the activity in the activity logs.");
		logValidator.verifyOnedriveActivityLog(fileLogs, activityMap.get("fileRestoreLog"));
	}
	
	
	@Test(groups={"FILE", "ONEDRIVE", "P1"})
	public void verifyFileRenameActivity() {
		LogUtils.logTestDescription("After renaming a file, verify the activity in the activity logs.");
		logValidator.verifyOnedriveActivityLog(fileLogs, activityMap.get("fileRenameLog"));
	}	
	

	@Test(groups={"FILE", "ONEDRIVE", "P1"})
	public void verifyFileSharingActivity() {
		LogUtils.logTestDescription("After sharing a file, verify the activity in the activity logs.");
		logValidator.verifyOnedriveActivityLog(fileLogs, activityMap.get("fileShareLog"));
	}

	@Test(groups={"FILE", "ONEDRIVE", "P1"})
	public void verifyFileUnSharingActivity() {
		LogUtils.logTestDescription("After sharing a file, verify the activity in the activity logs.");
		logValidator.verifyOnedriveActivityLog(fileLogs, activityMap.get("fileUnshareLog"));
	}
	
	@Test(groups={"FILE", "ONEDRIVE", "P1"})
	public void verifyFilePermissionsScopeChangeActivity() {
		LogUtils.logTestDescription("After changing the scope of a file, verify the activity in the activity logs.");
		logValidator.verifyOnedriveActivityLog(fileLogs, activityMap.get("fileScopeChangeLog"));
		
	}
	
	@Test(groups={"FILE", "ONEDRIVE", "P1"})
	public void verifyFileUpdateActivity() {
		LogUtils.logTestDescription("After updating the file, verify the activity in the activity logs.");
		logValidator.verifyOnedriveActivityLog(fileLogs, activityMap.get("fileEditLog"));
	}
	
	
	@Test(priority = -10, groups={"FOLDER", "ONEDRIVE", "P1"})
	public void performOneDriveFolderOperations() throws Exception {
		//Create folder
		Folder folder = universalApi.createFolderV2(destinationFolder);
		onedrivelog.setFolderUploadLog(onedrivelog.getFolderUploadLog().replace("{foldername}", destinationFolder));
		Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
		
		//Upload the file to the folder
		ItemResource itemResource = universalApi.uploadSimpleFile(folder.getId(), uniqueFilename, destinationFile);
		onedrivelog.setFolderFileUploadLog(onedrivelog.getFolderFileUploadLog().replace("{filename}", destinationFile));
		Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
		
		//Delete file
		universalApi.deleteFile(itemResource.getId(), itemResource.getETag());
		onedrivelog.setFolderFileDeleteLog(onedrivelog.getFolderFileDeleteLog().replace("{filename}", destinationFile));
		Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
		
		//Rename the folder
		String renamedFolder = destinationFolder+"_R";
		Folder updatedFolder = universalApi.renameFolder(folder.getId(), renamedFolder);
		onedrivelog.setFolderRenameLog(onedrivelog.getFolderRenameLog().replace("{foldername}", renamedFolder));
		Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
		
		//Rename again
		renamedFolder = destinationFolder;
		updatedFolder = universalApi.renameFolder(folder.getId(), renamedFolder);
		onedrivelog.setFolderRenameLog(onedrivelog.getFolderRenameLog().replace("{foldername}", renamedFolder));
		Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
		

		//Update the metadata of the folder by changing the title
		ListItemAllFields allfields = universalApi.getListItemAllFields(destinationFolder, true);
		String payload = "{\"__metadata\": {\"type\":\""+  allfields.getOdataType() +"\"}, \"Title\": \"Updated Folder Title\"}";
		universalApi.updateListItem(allfields.getOdataEditLink(), payload);
		onedrivelog.setFolderUpdateLog(onedrivelog.getFolderUpdateLog().replace("{foldername}", destinationFolder));
		Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);

		
		//Disable sharing if any before sharing
		universalApi.disableFolderSharing(destinationFolder);
		Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
		
		//Share the folder
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
		
		
		//Recycle the folder
		RecycleItem recycleItem = universalApi.recyleItem(destinationFolder, true);
		onedrivelog.setFolderDeleteLog(onedrivelog.getFolderDeleteLog().replace("{foldername}", destinationFolder));
		Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);

		//Restore the folder
		universalApi.restoreRecycleBinItem(recycleItem.getValue());
		onedrivelog.setFolderRestoreLog(onedrivelog.getFolderRestoreLog().replace("{foldername}", destinationFolder));
		Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
		
		//Delete folder
		universalApi.deleteFolderV2(folder.getId(), updatedFolder.getETag());
		onedrivelog.setFolderDeleteLog(onedrivelog.getFolderDeleteLog().replace("{foldername}", destinationFolder));
		Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
		
		//Wait for 3 mins for the logs to come to our portal
		Thread.sleep(CommonConstants.THREE_MINUTES_SLEEP);
	}
	
	
	@Test(groups={"FOLDER", "ONEDRIVE", "P1"})
	public void verifyFolderFileUploadActivity() {
		String expectedLog = onedrivelog.getFolderFileUploadLog();
		logValidator.verifyLog(messages, expectedLog);
	}

	@Test(groups={"FOLDER", "ONEDRIVE", "P1"})
	public void verifyFolderFileDeleteActivity() {
		String expectedLog = onedrivelog.getFolderFileDeleteLog();
		logValidator.verifyLog(messages, expectedLog);
	}
	
	@Test(groups={"FOLDER", "ONEDRIVE", "P1"})
	public void verifyFolderUploadActivity() {
		String expectedLog = onedrivelog.getFolderUploadLog();
		logValidator.verifyLog(messages, expectedLog);
	}

	@Test(groups={"FOLDER", "ONEDRIVE", "P1"})
	public void verifyFolderDeleteActivity() {
		String expectedLog = onedrivelog.getFolderDeleteLog();
		logValidator.verifyLog(messages, expectedLog);
	}
	
	@Test(groups={"FOLDER", "ONEDRIVE", "P1"})
	public void verifyFolderRestoreActivity() {
		String expectedLog = onedrivelog.getFolderRestoreLog();
		logValidator.verifyLog(messages, expectedLog);
	}
	
	
	@Test(groups={"FOLDER", "ONEDRIVE", "P1"})
	public void verifyFolderRenameActivity() {
		String expectedLog = onedrivelog.getFolderRenameLog();
		logValidator.verifyLog(messages, expectedLog);
	}
	
	@Test(groups={"FOLDER", "ONEDRIVE", "P1"})
	public void verifyFolderUpdateActivity() {
		String expectedLog = onedrivelog.getFolderUpdateLog();
		logValidator.verifyLog(messages, expectedLog);
	}

	@Test(groups={"FOLDER", "ONEDRIVE", "P1"})
	public void verifyFolderSharingActivity() {
		String expectedLog = onedrivelog.getFolderShareLog();
		logValidator.verifyLog(messages, expectedLog);
	}

	@Test(groups={"FOLDER", "ONEDRIVE", "P1"})
	public void verifyFolderUnSharingActivity() {
		String expectedLog = onedrivelog.getFolderUnShareLog();
		logValidator.verifyLog(messages, expectedLog);
	}
	
	@Test(groups={"FOLDER", "ONEDRIVE", "P1"})
	public void verifyFolderPermissionsScopeChangeActivity() {
		String expectedLog = onedrivelog.getFolderScopeChangeLog();
		logValidator.verifyLog(messages, expectedLog);
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
	
	@Test(groups={"LIST", "ONEDRIVE", "P1"})
	public void verifyEditDocumentListActivity() {
		String expectedLog = onedrivelog.getDocumentListEditLog();
		logValidator.verifyLog(messages, expectedLog);
	}
	
	@Test(groups={"LIST", "ONEDRIVE", "P1"})
	public void verifyShareDocumentListActivity() {
		String expectedLog = onedrivelog.getDocumentListShareLog();
		logValidator.verifyLog(messages, expectedLog);
	}
	
	@Test(groups={"LIST", "ONEDRIVE", "P1"})
	public void verifyUnShareDocumentListActivity() {
		String expectedLog = onedrivelog.getDocumentListUnShareLog();
		logValidator.verifyLog(messages, expectedLog);
	}
	
	@Test(groups={"LIST", "ONEDRIVE", "P1"})
	public void verifyUserUnShareDocumentListActivity() {
		String expectedLog = onedrivelog.getDocumentListUserUnShareLog();
		logValidator.verifyLog(messages, expectedLog);
	}
	
	
	@Test(priority = -5, groups={"FILE", "FOLDER", "LIST", "ONEDRIVE", "P1"})
	public void fetchActivityLogs() throws Exception {
		Thread.sleep(CommonConstants.FIVE_MINUTES_SLEEP);
		//Thread.sleep(CommonConstants.FIVE_MINUTES_SLEEP);

		ArrayList<String> logs;
		
		
		for (int retry = 0; retry < 2; retry++) {
			Thread.sleep(CommonConstants.THIRTY_SECONDS_SLEEP);
			try{
				String apiHost = suiteData.getScheme() + "://" + suiteData.getApiserverHostName();
				//Fetch the activity logs from yesterday to tomorrow and limited to 500
				//Get file related logs
				logs = searchDisplayLogs(-1, 1, "Office 365", "File", suiteData.getUsername().toLowerCase(), 
						apiHost, suiteData.getCSRFToken(), suiteData.getSessionID(), 0, 500);
				messages.addAll(logs);
				Set<String> hs = new HashSet<>();
				hs.addAll(messages);
				messages.clear();
				messages.addAll(hs);
				//Thread.sleep(10000);
				//Get folder related logs
				logs = searchDisplayLogs(-1, 1, "Office 365", "Folder", suiteData.getUsername().toLowerCase(), 
						apiHost, suiteData.getCSRFToken(), suiteData.getSessionID(), 0, 500);
				messages.addAll(logs);
				hs.addAll(messages);
				messages.clear();
				messages.addAll(hs);
				
				//Get list related logs
				logs = searchDisplayLogs(-1, 1, "Office 365", "List", suiteData.getUsername().toLowerCase(), 
						apiHost, suiteData.getCSRFToken(), suiteData.getSessionID(), 0, 500);
				messages.addAll(logs);
				hs.addAll(messages);
				messages.clear();
				messages.addAll(hs);
				
				
			}
			catch(Exception e) {}
		}
		Reporter.log("All messages:" + messages, true);
	}
	
	
	
	//Site related tests
	
	@Test(priority = -10, dataProviderClass = O365DataProvider.class, dataProvider = "SubSiteDataProvider", groups={"SITE", "ONEDRIVE", "P1"})
	public void performSPSiteOperations(Integer language, String siteUrl, String siteTitle, String siteDescription, String webtemplate, boolean setUseUniquePermissions) throws Exception {
		
		Parameters siteParams = new Parameters(language, siteUrl, siteTitle, siteDescription, webtemplate, setUseUniquePermissions);
//		String prefix = String.valueOf(System.currentTimeMillis());
//		
//		siteParams.setLanguage(1033);
//		siteParams.setUrl(prefix+"_RestWeb");
//		siteParams.setTitle(prefix+"_RestWeb");
//		siteParams.setDescription("REST created web2");
//		siteParams.setWebTemplate("BLOG#0");
		//siteParams.setUseUniquePermissions(true);
		//siteParams.setUseSamePermissionsAsParentSite(false);
		
		
		/*{ 'parameters': { '__metadata': { 'type': 'SP.WebCreationInformation' },
		    'Title': 'Team projects', 'Url': 'TeamProjects', 'WebTemplate': 'STS',
		    'UseSamePermissionsAsParentSite': true } }*/
		
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
		
		//onedrivelog.setSiteCreateLog(onedrivelog.getSiteCreateLog().replace("{sitename}", siteParams.getUrl()));
		
		
		
		//sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
		
		//universalSiteApi.deleteSite(siteUrl);
		/*
//		onedrivelog.setSiteDeleteLog(onedrivelog.getSiteDeleteLog().replace("{sitename}", siteParams.getUrl()));
//		sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
		
		try {

			for (int i = 1; i <= (waitTime * 60 * 1000); i+=60000 ) {
				sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
				HashMap<String, String> termmap = new HashMap<String, String>();
				termmap.put("facility", "Office 365");
				termmap.put("Object_type", ObjectType.Site.name());

				//Get site related logs
				siteLogs = this.getInvestigateLogs(-50, 10, "Office 365", termmap, suiteData.getUsername(), suiteData.getApiserverHostName(), 
						suiteData.getCSRFToken(), suiteData.getSessionID(), 0, 300, "Office 365");

			}
		}
		catch(Exception e) {}
		Reporter.log(MarshallingUtils.marshall(siteLogs), true);
		long total = siteLogs.getHits().getTotal();
		Reporter.log("Total site logs count:"+ total, true);

		//If total number of logs is less than zero, no logs are seen so skip the tests
		if (total <= 0) {
			throw new SkipException("Skipping the dependent tests..No logs are seen for sites in onedrive");
		}
		*/
//		
	}
	
	
	@Test(priority = -10, groups={"SITE1", "ONEDRIVE", "P1"})
	public void FileUploadToSite() throws Exception {
		universalSiteApi.uploadFileToSite(uniqueFilename, "Documents", "AutoTeamSite", "Hello.txt");
	}
	

	//@Test(groups={"SITE", "ONEDRIVE", "P1"})
	public void verifySiteUploadActivity() {
		String expectedLog = onedrivelog.getSiteCreateLog();
		logValidator.verifyLog(messages, expectedLog);
	}

	//@Test(groups={"SITE", "ONEDRIVE", "P1"})
	public void verifySiteDeleteActivity() {
		String expectedLog = onedrivelog.getSiteDeleteLog();
		logValidator.verifyLog(messages, expectedLog);
	}
	
	
	
	@Test(priority= -10, groups={"FILE", "CONTENT_INSPECTION", "ONEDRIVE", "SANITY", "REGRESSSION", "P1"})
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
		ItemResource pciUploadResponse 		= universalApi.uploadSimpleFile("/", 	pciFilepath, 	pciDestFile);
		ItemResource piiUploadResponse 		= universalApi.uploadSimpleFile("/", 	piiFilepath, 	piiDestFile);
		ItemResource hipaaUploadResponse 	= universalApi.uploadSimpleFile("/", 	hipaaFilepath, 	hipaaDestFile);
		
		createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
		onedrivelog.setPiiRiskLog(onedrivelog.getPiiRiskLog().replace("{filename}", piiDestFile)
																		.replace("{risktype}", "PII"));
		
		OneDriveBusinessActivity PIIRiskLog = new OneDriveBusinessActivity(onedrivelog.getPiiRiskLog(), createdTime, Severity.critical.name(),  
						"Content Inspection", suiteData.getSaasAppUsername().toLowerCase(), getETagAsDocumentId(piiUploadResponse.getETag()), piiDestFile, "PII", "Office 365");
		activityMap.put("PIIRiskLog", PIIRiskLog);


		onedrivelog.setPciRiskLog(onedrivelog.getPciRiskLog().replace("{filename}", pciDestFile)
																		.replace("{risktype}", "PCI"));
		OneDriveBusinessActivity PCIRiskLog = new OneDriveBusinessActivity(onedrivelog.getPciRiskLog(), createdTime, Severity.critical.name(),  
				"Content Inspection", suiteData.getSaasAppUsername().toLowerCase(), getETagAsDocumentId(pciUploadResponse.getETag()), pciDestFile, "PCI", "Office 365");

		
		activityMap.put("PCIRiskLog", PCIRiskLog);

		onedrivelog.setHipaaRiskLog(onedrivelog.getHipaaRiskLog().replace("{filename}", hipaaDestFile)
																		.replace("{risktype}", "PII, HIPAA"));
		OneDriveBusinessActivity HIPAARiskLog = new OneDriveBusinessActivity(onedrivelog.getHipaaRiskLog(), createdTime, Severity.critical.name(),  
				"Content Inspection", suiteData.getSaasAppUsername().toLowerCase(), getETagAsDocumentId(hipaaUploadResponse.getETag()), hipaaDestFile, "HIPAA", "Office 365");
		activityMap.put("HIPAARiskLog", HIPAARiskLog);

			
		//Wait for 5 mins
		sleep(CommonConstants.FIVE_MINUTES_SLEEP);
		
		try {

			for (int i = 1; i <= (waitTime * 60 * 1000); i+=60000 ) {
				sleep(CommonConstants.SIXTY_SECONDS_SLEEP);

				HashMap<String, String> termmap = new HashMap<String, String>();
				termmap.put("facility", "Office 365");
				termmap.put("Activity_type", "Content Inspection");
				this.contentInspectionLogs = this.getInvestigateLogs(-20, 10, "Office 365", termmap, suiteData.getUsername(), suiteData.getApiserverHostName(), 
						suiteData.getCSRFToken(), suiteData.getSessionID(), 0, 200, "Office 365");						
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
		universalApi.deleteFile(pciUploadResponse.getId(), 		"*");
		universalApi.deleteFile(piiUploadResponse.getId(), 		"*");
		universalApi.deleteFile(hipaaUploadResponse.getId(), 	"*");
		
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
	
	
	
	
	
	@Test(dataProviderClass = O365DataProvider.class, dataProvider = "ObjectTypeFilter", groups={"FILTERS", "ONEDRIVE", "REGRESSION", "P1"})
	public void dashboardObjectTypeFilters(String objType) throws Exception {
		ForensicSearchResults logs;
		HashMap<String, String> termmap = new HashMap<String, String>();
		termmap.put("facility", "Office 365");
		termmap.put("Object_type", objType);
		LogUtils.logTestDescription("Retrieve the objecttype and filter them by name:"+ objType);
			for (int retry = 0; retry < 1; retry++) {

				try{
					String apiHost = suiteData.getScheme() + "://" + suiteData.getApiserverHostName();
					//Fetch the activity logs from yesterday to tomorrow and limited to 500
					//Get file related logs
					logs = getInvestigateLogs(-18000, 10, "Office 365", termmap, suiteData.getUsername().toLowerCase(), 
							apiHost, suiteData.getCSRFToken(), suiteData.getSessionID(), 0, 500, "Office 365");

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
	
	
	@Test(dataProviderClass = O365DataProvider.class, dataProvider = "ActivityTypeFilter",groups={"FILTERS", "ONEDRIVE", "REGRESSION", "P1"})
	public void dashboardActivityTypeFilters(String activityType) throws Exception {
		ForensicSearchResults logs;
		LogUtils.logTestDescription("Retrieve the activities and filter them by name:"+ activityType);
		HashMap<String, String> termmap = new HashMap<String, String>();
		termmap.put("facility", "Office 365");
		termmap.put("Activity_type", activityType);
		
		for (int retry = 0; retry < 1; retry++) {

			try{
				String apiHost = suiteData.getScheme() + "://" + suiteData.getApiserverHostName();
				//Fetch the activity logs from yesterday to tomorrow and limited to 500
				//Get file related logs
				logs = getInvestigateLogs(-18000, 10, "Office 365", termmap, suiteData.getUsername().toLowerCase(), 
						apiHost, suiteData.getCSRFToken(), suiteData.getSessionID(), 0, 500, "Office 365");

				this.logValidator = new LogValidator(logs);

				logValidator.verifyMetadata();
				logValidator.verifyActivityType(activityType);
				assertTrue(logs.getHits().getTotal() > 0, "ActivityType " + activityType + " related messages are not present");
			}
			catch(Exception e) {}
		}
	}
	
	
	@Test(dataProviderClass = O365DataProvider.class, dataProvider = "SeverityFilter",groups={"FILTERS", "ONEDRIVE", "REGRESSION", "P1"})
	public void dashboardSeverityTypeFilters(String severityType) throws Exception {
		ForensicSearchResults logs;
		LogUtils.logTestDescription("Retrieve the activities and filter them by name:"+ severityType);
		HashMap<String, String> termmap = new HashMap<String, String>();
		termmap.put("facility", "Office 365");
		termmap.put("severity", severityType);
		termmap.put("__source", "API");
		//termmap.put("user", this.saasAppUserAccount.getUsername());
		
		for (int retry = 0; retry < 1; retry++) {

			try{
				String apiHost = suiteData.getScheme() + "://" + suiteData.getApiserverHostName();
				//Fetch the activity logs from yesterday to tomorrow and limited to 500
				//Get file related logs
				logs = getInvestigateLogs(-180000, 10, "Office 365", termmap, suiteData.getUsername().toLowerCase(), 
						apiHost, suiteData.getCSRFToken(), suiteData.getSessionID(), 0, 500, "Office 365");

				this.logValidator = new LogValidator(logs);

				logValidator.verifyMetadata();
				logValidator.verifySeverityType(severityType);
				assertTrue(logs.getHits().getTotal() > 0, "SeverityType " + severityType + " related messages are not present");
			}
			catch(Exception e) {}
		}
	}
	
	
	//@Test(groups={"FILTERS", "BOX", "REGRESSION", "P1"})
//	public void verifyAnonymizedLogs() throws Exception {
//		String[] steps = { "1. Turn on the anonymization and get the investigate logs.",
//		           "2. Verify the username in message is anonymized.",
//		           "3. Turn off the anonymizarion." };
//		LogUtils.logTestDescription(steps);
//		
//		String tenantAcctId = getTenantAccountId();
//		String payload = "{\"userAnonymization\":true,\"id\":\""+tenantAcctId+"\"}";
//		String responseBody = this.updateUserAnonymization(payload);
//		
//		ForensicSearchResults logs;
//		
//		HashMap<String, String> termmap = new HashMap<String, String>();
//		termmap.put("facility", "Box");
//		termmap.put("__source", "API");
//		
//		for (int retry = 0; retry < 1; retry++) {
//
//			try{
//				String apiHost = suiteData.getScheme() + "://" + suiteData.getApiserverHostName();
//				//Fetch the activity logs from yesterday to tomorrow and limited to 500
//				logs = getInvestigateLogs(-18000, 10, "Box", termmap, suiteData.getUsername().toLowerCase(), 
//						apiHost, suiteData.getCSRFToken(), suiteData.getSessionID(), 0, 500, "investigate");
//
//				this.logValidator = new LogValidator(logs);
//
//				logValidator.verifyMetadata();
//				//Add the method and update it
//				logValidator.verifyAnonymizedLogs();
//				
//			}
//			catch(Exception e) {}
//		}
//		
//		//Remove anonymization
//		payload = "{\"userAnonymization\":false,\"dpoName\":\""+ suiteData.getDpoUsername() +"\",\"dpoPassword\":\""+ suiteData.getDpoPassword() +"\", \"id\":\""+tenantAcctId+"\"}";
//		responseBody = this.updateUserAnonymization(payload);
//		Reporter.log("Response body:"+ responseBody, true);
//		for (int retry = 0; retry < 1; retry++) {
//
//			try{
//				String apiHost = suiteData.getScheme() + "://" + suiteData.getApiserverHostName();
//				//Fetch the activity logs from yesterday to tomorrow and limited to 500
//				logs = getInvestigateLogs(-18000, 10, "Box", termmap, suiteData.getUsername().toLowerCase(), 
//						apiHost, suiteData.getCSRFToken(), suiteData.getSessionID(), 0, 500, "investigate");
//
//				this.logValidator = new LogValidator(logs);
//
//				logValidator.verifyMetadata();
//				//Add the method and update it
//				logValidator.verifyUnAnonymizedLogs();
//				
//			}
//			catch(Exception e) {}
//		}
//	}
//	
//	
	
	@Test(groups={"FILTERS", "ONEDRIVE", "REGRESSION", "P1", "ANONYMIZATION"})
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
		termmap.put("facility", "Office 365");
		termmap.put("__source", "API");
		termmap.put("Object_type", "File");
		
		for (int retry = 0; retry < 1; retry++) {

			try{
				String apiHost = suiteData.getScheme() + "://" + suiteData.getApiserverHostName();
				//Fetch the activity logs from yesterday to tomorrow and limited to 500
				logs = getInvestigateLogs(-18000, 10, "Office 365", termmap, suiteData.getUsername().toLowerCase(), 
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
				logs = getInvestigateLogs(-18000, 10, "Office 365", termmap, suiteData.getUsername().toLowerCase(), 
						apiHost, suiteData.getCSRFToken(), suiteData.getSessionID(), 0, 500, "investigate");

				this.logValidator = new LogValidator(logs);

				logValidator.verifyMetadata();
				//Add the method and update it
				logValidator.verifyUnAnonymizedLogs();
				
			}
			catch(Exception e) {}
		}
	}
	
	
	@Test(groups={"EXPORT", "ONEDRIVE", "REGRESSION", "P1"})
	public void checkCSVExportOfExposedUsers() throws Exception {
		LogUtils.logTestDescription("Export the exposed users to user email and check");
		List<NameValuePair> headers = getHeaders();
		String path = suiteData.getAPIMap().get("getUIExportCSV") ;
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(), path);
		Reporter.log("URI ::"+dataUri.toString(), true);

		String payload = "{\"source\":{\"limit\":100,\"offset\":0,\"isInternal\":true,\"searchTextFromTable\":\"\",\"exportType\":\"users\",\"requestType\":\"users\",\"app\":\"Office 365\"}}";
		HttpResponse response =  restClient.doPost(dataUri, headers, null, new StringEntity(payload));

		String responseBody = ClientUtil.getResponseBody(response);
		Reporter.log("Response body:"+ responseBody, true);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
		CustomAssertion.assertTrue(responseBody.contains("true"), "Filelink sent to email", "File not sent");
		CustomAssertion.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_OK, "Not exported");
		
		sleep(30000);
		
		Office365MailActivities office365MailActivities = new Office365MailActivities(saasAppUserAccount.getUsername(), saasAppUserAccount.getPassword());
        
		Item findItem1 = office365MailActivities.findItemInDecending("Office365 Securlet Data Export");
		EmailMessage emailMessage = office365MailActivities.getEmailMessage(findItem1.getId().getUniqueId());
		AttachmentCollection attachments = emailMessage.getAttachments();
		
		boolean isPresent=false;
		String expectedAttachment = "Office365_securlet_users_" + DateUtils.getCurrentDate();
		
		for (Attachment attachment : attachments) {
			Reporter.log("Attacment Name :"+attachment.getName(), true);
			Reporter.log("Attachment Size :"+attachment.getSize(), true);
			
			if(attachment.getName().startsWith(expectedAttachment)) {
				isPresent = true;
				CustomAssertion.assertTrue(attachment.getSize() > 0, attachment.getName() + " size is "+attachment.getSize(), attachment.getName() + "size is zero" );
	        	CustomAssertion.assertTrue(attachment.getName().startsWith(expectedAttachment), attachment.getName()+ " Starts with "+ expectedAttachment, "Attachment name don't match");
			}
		}
		
		CustomAssertion.assertTrue(isPresent, "Expected attachment is present", "Expected attachment is not present" );
	}
	
	
	@Test(groups={"EXPORT", "ONEDRIVE", "REGRESSION", "P1"})
	public void checkCSVExportOfOtherRisks() throws Exception {
		LogUtils.logTestDescription("Export the other risks files to user email and check");
		List<NameValuePair> headers = getHeaders();
		String path = suiteData.getAPIMap().get("getUIExportCSV") ;
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(), path);
		Reporter.log("URI ::"+dataUri.toString(), true);

		String payload = "{\"source\":{\"limit\":20,\"offset\":0,\"isInternal\":true,\"requestType\":\"risky_docs\",\"searchTextFromTable\":\"\",\"orderBy\":\"name\",\"app\":\"Office 365\",\"exportType\":\"risky_docs\"}}";
		HttpResponse response =  restClient.doPost(dataUri, headers, null, new StringEntity(payload));

		String responseBody = ClientUtil.getResponseBody(response);
		Reporter.log("Response body:"+ responseBody, true);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
		CustomAssertion.assertTrue(responseBody.contains("true"), "Filelink sent to email", "File not sent");
		CustomAssertion.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_OK, "Not exported");
		
		sleep(30000);
		
		Office365MailActivities office365MailActivities = new Office365MailActivities(saasAppUserAccount.getUsername(), saasAppUserAccount.getPassword());
        
		Item findItem1 = office365MailActivities.findItemInDecending("Office365 Securlet Data Export");
		EmailMessage emailMessage = office365MailActivities.getEmailMessage(findItem1.getId().getUniqueId());
		AttachmentCollection attachments = emailMessage.getAttachments();
		
		boolean isPresent=false;
		String expectedAttachment = "Office365_securlet_risky_docs_" + DateUtils.getCurrentDate();
		
		for (Attachment attachment : attachments) {
			Reporter.log("Attacment Name :"+attachment.getName(), true);
			Reporter.log("Attachment Size :"+attachment.getSize(), true);
			
			if(attachment.getName().startsWith(expectedAttachment)) {
				isPresent = true;
				CustomAssertion.assertTrue(attachment.getSize() > 0, attachment.getName() + " size is "+attachment.getSize(), attachment.getName() + "size is zero" );
	        	CustomAssertion.assertTrue(attachment.getName().startsWith(expectedAttachment), attachment.getName()+ " Starts with "+ expectedAttachment, "Attachment name don't match");
			}
		}
		
		CustomAssertion.assertTrue(isPresent, "Expected attachment is present", "Expected attachment is not present" );
		
	}
	
	
	@Test(dataProviderClass = O365DataProvider.class, dataProvider = "ExportFormat", groups={"EXPORT", "ONEDRIVE", "REGRESSION", "P1"})
	public void checkActivityLogExport(String format) throws Exception {
		
		
		LogUtils.logTestDescription("Export the Activity log export in "+ format + " format and check the email");
		 
		String tsfrom = DateUtils.getDaysFromCurrentTime(-8);
		String tsto   = DateUtils.getDaysFromCurrentTime(1);	
		
		HashMap<String, String> hmap = new HashMap<String, String>();
		hmap.put("facility", "Office 365");
		
		//Get headers
		List<NameValuePair> headers = getHeaders();
		String payload = esQueryBuilder.getESQuery(tsfrom, tsto, "Office 365", hmap, this.suiteData.getSaasAppUsername().toLowerCase(), suiteData.getApiserverHostName(), 
				suiteData.getCSRFToken(), suiteData.getSessionID(), 0, 50000, "investigate", format, tsfrom, tsto);
		
		String path = suiteData.getAPIMap().get("getActivityLogExport") ;
		
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(), path);

		HttpResponse response =  restClient.doPost(dataUri, headers, null, new StringEntity(payload));

		String responseBody = ClientUtil.getResponseBody(response);
		Reporter.log("Response body:"+ responseBody, true);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
		CustomAssertion.assertTrue(responseBody.contains("true"), "Filelink sent to email", "File not sent");
		CustomAssertion.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_OK, "Not exported");
		
		sleep(30000);
		
		Office365MailActivities office365MailActivities = new Office365MailActivities(saasAppUserAccount.getUsername(), saasAppUserAccount.getPassword());
        // Get Download Reference URL..
		Item findItem = office365MailActivities.findItemInDecending("Log Export Request");
		String link = office365MailActivities.getDownloadHref(findItem);
		
		CustomAssertion.assertTrue(link !=null, "Download link "+ link + " is present", "Download link is not present");
		Reporter.log("Download link:"+link, true);
	}
	
	
	
	//Redefined export tests
	
	//C1876052
	@Test(groups={"EXPORT", "ONEDRIVE", "REGRESSION", "P1"})
	public void csvExportOfExposedFilesWithDefaultDashBoard() throws Exception {
		LogUtils.logTestDescription("Export the exposed files with default dashboard state to user email and check");
		List<NameValuePair> headers = getHeaders();
		
		String path = suiteData.getAPIMap().get("getUIExportCSV") ;
		//String path = "/admin/application/list/export_exposures_data"; 
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(), path);
		Reporter.log("URI ::"+dataUri.toString(), true);

		UIExposedDoc payload = this.getUIPayload(appName, null, null, "docs", 0, 0, true, "", "name", null, null, null);
		SecurletDocument exposedDocs = getUIExposedDocuments(MarshallingUtils.marshall(payload), null);
		
		//String payload = "{\"source\":{\"limit\":20,\"offset\":0,\"isInternal\":true,\"searchTextFromTable\":\"\",\"orderBy\":\"name\",\"exportType\":\"docs\",\"app\":\"Office 365\"}}";
		HttpResponse response =  restClient.doPost(dataUri, headers, null, new StringEntity(MarshallingUtils.marshall(payload)));

		String responseBody = ClientUtil.getResponseBody(response);
		Reporter.log("Response body:"+ responseBody, true);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
		CustomAssertion.assertTrue(responseBody.contains("true"), "Filelink sent to email", "File not sent");
		CustomAssertion.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_OK, "Not exported");

		sleep(30000);
		
		Office365MailActivities office365MailActivities = new Office365MailActivities(saasAppUserAccount.getUsername(), saasAppUserAccount.getPassword());
        
		Item findItem1 = office365MailActivities.findItemInDecending("Office365 Securlet Data Export");
		EmailMessage emailMessage = office365MailActivities.getEmailMessage(findItem1.getId().getUniqueId());
		AttachmentCollection attachments = emailMessage.getAttachments();
		
		boolean isPresent=false;
		String expectedAttachment = "Office365_securlet_docs_" + DateUtils.getCurrentDate();
		
		String tmpdir = System.getProperty("user.dir") + File.separator;
		
		String absoluteFilename = null;
		for (Attachment attachment : attachments) {
			
			Reporter.log("Attacment Name :"+attachment.getName(), true);
			Reporter.log("Attachment Size :"+attachment.getSize(), true);
			
			
			if(attachment.getName().startsWith(expectedAttachment)) {
				isPresent = true;
				CustomAssertion.assertTrue(attachment.getSize() > 0, attachment.getName() + " size is "+attachment.getSize(), attachment.getName() + "size is zero" );
	        	CustomAssertion.assertTrue(attachment.getName().startsWith(expectedAttachment), attachment.getName()+ " Starts with "+ expectedAttachment, "Attachment name don't match");
	        	
	        	//attachment.load();
	        	FileAttachment iAttachment = (FileAttachment) attachment;
	        	iAttachment.load();
                iAttachment.load( tmpdir + File.separator + iAttachment .getName());
                absoluteFilename = iAttachment .getName();
			}
		}
		
		CustomAssertion.assertTrue(isPresent, "Expected attachment " + expectedAttachment +" is present", "Expected attachment " + expectedAttachment +" is not present" );
		
		if (absoluteFilename != null) {
			String source = tmpdir + File.separator + absoluteFilename;
			String destination = tmpdir + File.separator + absoluteFilename.replace(".gz", "");
			
			uncompressgz(source, destination);
			
			List<Map<String, String>> allrows = unmarshallCSVIntoList(new File(destination), null);
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
	
	
	@Test(dataProviderClass = O365DataProvider.class, dataProvider = "ExportFilter", groups={"EXPORT", "ONEDRIVE", "REGRESSION", "P1"})
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
		
		Office365MailActivities office365MailActivities = new Office365MailActivities(saasAppUserAccount.getUsername(), saasAppUserAccount.getPassword());
        
		Item findItem1 = office365MailActivities.findItemInDecending("Office365 Securlet Data Export");
		EmailMessage emailMessage = office365MailActivities.getEmailMessage(findItem1.getId().getUniqueId());
		AttachmentCollection attachments = emailMessage.getAttachments();
		
		boolean isPresent=false;
		String expectedAttachment = "Office365_securlet_docs_" + DateUtils.getCurrentDate();
		String tmpdir = System.getProperty("user.dir") + File.separator;
		
		String absoluteFilename = null;
		for (Attachment attachment : attachments) {
			
			Reporter.log("Attacment Name :"+attachment.getName(), true);
			Reporter.log("Attachment Size :"+attachment.getSize(), true);
			
			
			if(attachment.getName().startsWith(expectedAttachment)) {
				isPresent = true;
				CustomAssertion.assertTrue(attachment.getSize() > 0, attachment.getName() + " size is "+attachment.getSize(), attachment.getName() + "size is zero" );
	        	CustomAssertion.assertTrue(attachment.getName().startsWith(expectedAttachment), attachment.getName()+ " Starts with "+ expectedAttachment, "Attachment name don't match");
	        	
	        	//attachment.load();
	        	FileAttachment iAttachment = (FileAttachment) attachment;
	        	iAttachment.load();
                iAttachment.load( tmpdir + File.separator + iAttachment .getName());
                absoluteFilename = iAttachment .getName();
			}
		}
		
		CustomAssertion.assertTrue(isPresent, "Expected attachment " + expectedAttachment +" is present", "Expected attachment " + expectedAttachment +" is not present" );
		
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
	
	
	
	
	
	@Test(dataProviderClass = O365DataProvider.class, dataProvider = "ExportUsersFilter", groups={"EXPORT", "ONEDRIVE", "REGRESSION", "P1"})
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
		
		sleep(30000);
		
		Office365MailActivities office365MailActivities = new Office365MailActivities(saasAppUserAccount.getUsername(), saasAppUserAccount.getPassword());
        
		Item findItem1 = office365MailActivities.findItemInDecending("Office365 Securlet Data Export");
		EmailMessage emailMessage = office365MailActivities.getEmailMessage(findItem1.getId().getUniqueId());
		AttachmentCollection attachments = emailMessage.getAttachments();
		
		boolean isPresent=false;
		String expectedAttachment = null;
		if (exportType.equals("users")) {
			expectedAttachment = "Office365_securlet_users_" + DateUtils.getCurrentDate();
		} else if( exportType.equals("ext_collabs")) {
			expectedAttachment = "Office365_securlet_ext_collabs_" + DateUtils.getCurrentDate();
		}
		 
		String tmpdir = System.getProperty("user.dir") + File.separator;
		
		String absoluteFilename = null;
		for (Attachment attachment : attachments) {
			Reporter.log("Attacment Name :"+attachment.getName(), true);
			Reporter.log("Attachment Size :"+attachment.getSize(), true);
			
			if(attachment.getName().startsWith(expectedAttachment)) {
				isPresent = true;
				CustomAssertion.assertTrue(attachment.getSize() > 0, attachment.getName() + " size is "+attachment.getSize(), attachment.getName() + "size is zero" );
	        	CustomAssertion.assertTrue(attachment.getName().startsWith(expectedAttachment), attachment.getName()+ " Starts with "+ expectedAttachment, "Attachment name don't match");
	        	
	        	FileAttachment iAttachment = (FileAttachment) attachment;
	        	iAttachment.load();
                iAttachment.load( tmpdir + File.separator + iAttachment .getName());
                absoluteFilename = iAttachment .getName();
			}
		}
		
		CustomAssertion.assertTrue(isPresent, "Expected attachment is present", "Expected attachment is not present" );
		
		//Get the exposed users with api
		List<NameValuePair> docparams = new ArrayList<NameValuePair>(); 
		docparams.add(new BasicNameValuePair(SecurletsConstants.IS_INTERNAL,  String.valueOf(isInternal)));
		docparams.add(new BasicNameValuePair(SecurletsConstants.APP,  facility.Office365.name()));
		
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
			exposedUsers = getExposedUsers(elapp.el_office_365.name(), docparams);
		} else if( exportType.equals("ext_collabs")) {
			exposedUsers = getCollaborators(elapp.el_office_365.name(), docparams);
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
	
	
	
	@Test(dataProviderClass = O365DataProvider.class, dataProvider = "RiskyFilesFilter", groups={"EXPORT", "ONEDRIVE", "REGRESSION", "P1"})
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

		sleep(30000);
		
		Office365MailActivities office365MailActivities = new Office365MailActivities(saasAppUserAccount.getUsername(), saasAppUserAccount.getPassword());
        
		Item findItem1 = office365MailActivities.findItemInDecending("Office365 Securlet Data Export");
		EmailMessage emailMessage = office365MailActivities.getEmailMessage(findItem1.getId().getUniqueId());
		AttachmentCollection attachments = emailMessage.getAttachments();
		
		boolean isPresent=false;
		String expectedAttachment = "Office365_securlet_risky_docs_" + DateUtils.getCurrentDate();
		String tmpdir = System.getProperty("user.dir") + File.separator; //System.getProperty("java.io.tmpdir") ; 
				
		Reporter.log("TmpDir:"+tmpdir, true);
		String absoluteFilename = null;
		for (Attachment attachment : attachments) {
			
			Reporter.log("Attacment Name :"+attachment.getName(), true);
			Reporter.log("Attachment Size :"+attachment.getSize(), true);
			
			
			if(attachment.getName().startsWith(expectedAttachment)) {
				isPresent = true;
				CustomAssertion.assertTrue(attachment.getSize() > 0, attachment.getName() + " size is "+attachment.getSize(), attachment.getName() + "size is zero" );
	        	CustomAssertion.assertTrue(attachment.getName().startsWith(expectedAttachment), attachment.getName()+ " Starts with "+ expectedAttachment, "Attachment name don't match");
	        	
	        	//attachment.load();
	        	FileAttachment iAttachment = (FileAttachment) attachment;
	        	iAttachment.load();
                iAttachment.load( tmpdir + File.separator + iAttachment .getName());
                absoluteFilename = iAttachment .getName();
			}
		}
		
		CustomAssertion.assertTrue(isPresent, "Expected attachment " + expectedAttachment +" is present", "Expected attachment " + expectedAttachment +" is not present" );
		
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
	
	
	
	
	
	
	
	
	
	
	@Test(dataProviderClass = O365DataProvider.class, dataProvider = "ExposureTypeFilter", groups={"FILTER", "ONEDRIVE", "REGRESSION", "P1"})
	public void applyFiltersOnExposureTypesAndValidate(String type) throws Exception {
		
		String steps[] = {
				"1. Apply the exposure type filter "+type,
				"2. Check the count is matching"
		};
	
		LogUtils.logTestDescription(steps);
			
		
			List<NameValuePair> qparams = new ArrayList<NameValuePair>(); 
			qparams.add(new BasicNameValuePair(SecurletsConstants.IS_INTERNAL,  Boolean.TRUE.toString()));
			qparams.add(new BasicNameValuePair(SecurletsConstants.APP,  facility.Office365.name()));
			
			ExposureTotals expsoureTotals = getExposuresMetricsTotal(elapp.el_office_365.name(), qparams);
			
			long count = 0;
			if (type.equals("public")) {
				count = expsoureTotals.getPublicExposouresCount();
			} else if(type.equals("all_internal")) {
				count = expsoureTotals.getInternalExposouresCount();
			} else if(type.equals("ext_count")) {
				count = expsoureTotals.getExternalExposouresCount();
			}
			
			//Get the exposed documents and check the document is publicly exposed
			List<NameValuePair> docparams = new ArrayList<NameValuePair>(); 
			docparams.add(new BasicNameValuePair(SecurletsConstants.IS_INTERNAL,  Boolean.TRUE.toString()));
			docparams.add(new BasicNameValuePair(SecurletsConstants.APP,  facility.Office365.name()));
			docparams.add(new BasicNameValuePair("exposures.types", type));
			
			SecurletDocument documents = getExposedDocuments(elapp.el_office_365.name(), docparams);
			
			//Reporter.log("After exposure, checking the publicly shared document is present in the exposed files tab...", true);
			//docValidator.verifyPubliclyExposedDocument(documents, this.saasAppUserAccount, sharedFile.getName(), "file");
			
			Reporter.log("After filter, verify the totals matching with actual object count...", true);
			CustomAssertion.assertEquals(documents.getMeta().getTotalCount() , count, "Exposure filter values not matching");
	}	
	
	
	
	@Test(groups={"FILTER", "ONEDRIVE", "REGRESSION", "P1"})
	public void applyFiltersOnVulnerabilityTypes() throws Exception {
		
		LogUtils.logTestDescription("Get the vulnerability types and verify them");
		Logger.info( "Getting vulnerability types ...");
		
		HashMap<String, String> additionalParams = new HashMap<String, String>();
		additionalParams.put("is_internal", "true");
		additionalParams.put("vl_types", 	"all");
		
		//After external exposure
		VulnerabilityTypes vulnerabilityTypes = getVulnerabilityTypes(elapp.el_office_365.name(), additionalParams);

		String vulnerabilities[] = {"pci", "pii", "hipaa",  "source_code", "virus", "dlp", "encryption", "vba_macros", "glba", "ferpa"};

		for (String vl : vulnerabilities) {
			Reporter.log("Verifying the vulnerability type:"+vl, true);
			Reporter.log("Vulnerability:" + vl + "::Count:" + vulnerabilityTypes.getObjects().getVulnerabilityCount(vl), true );
			CustomAssertion.assertTrue(vulnerabilityTypes.getObjects().getVulnerabilityCount(vl) >= 0, "Count is greater than zero");
		}
		
		for(CiqProfile ciqprofile : vulnerabilityTypes.getObjects().getCiqProfiles()) {
			Reporter.log("Verifying the CIQ profile name:"+ciqprofile.getId(), true);
			Reporter.log("Ciq Profile name:" +ciqprofile.getId() + ":: Count:"+ciqprofile.getTotal(), true);
			CustomAssertion.assertTrue(ciqprofile.getId()!=null, "CIQ profile id is not null");
			CustomAssertion.assertTrue(ciqprofile.getTotal() >= 0, "CIQ profile total is not null");
		}
	}	
	
	@Test(groups={"FILTER", "ONEDRIVE", "REGRESSION", "P1"})
	public void filterExposedUsers() throws Exception {
		//Get the exposed documents and check the document is publicly exposed
		List<NameValuePair> docparams = new ArrayList<NameValuePair>(); 
		docparams.add(new BasicNameValuePair(SecurletsConstants.IS_INTERNAL,  Boolean.FALSE.toString()));
		docparams.add(new BasicNameValuePair(SecurletsConstants.APP,  facility.Office365.name()));
		
		SecurletDocument documents = getExposedUsers(elapp.el_office_365.name(), docparams);
		 
		Reporter.log("After filter, verify the external user count is greater than zero", true);
		CustomAssertion.assertTrue(documents.getMeta().getTotalCount() > 0 , "External user count is greater than zero");
		
		
		docparams = new ArrayList<NameValuePair>(); 
		docparams.add(new BasicNameValuePair(SecurletsConstants.IS_INTERNAL,  Boolean.TRUE.toString()));
		docparams.add(new BasicNameValuePair(SecurletsConstants.APP,  facility.Office365.name()));
		
		
		documents = getExposedUsers(elapp.el_office_365.name(), docparams);
		Reporter.log("After filter, verify the internal user count is greater than zero", true);
		CustomAssertion.assertTrue(documents.getMeta().getTotalCount() > 0 , "Internal user count is greater than zero");
		
	}
	
	
	@Test(priority= -15, groups={"FILTERS", "HISTORY", "BOX", "REGRESSION", "P1"})
	public void verifyHistoryLogs() throws Exception {
		String[] steps = 
				 { "1. Retrieve the history logs for the term elastica.",
		           "2. Verify the login and logout."
				 };
		LogUtils.logTestDescription(steps);
		//this.ipInfo="";
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
	
	
	
	@Test(dataProviderClass = O365DataProvider.class, priority=-1, dataProvider = "I18NString", groups={"I18N", "P3", "I18N_FILE"})
	public void performOneDriveI18NFileOperations(String locale, String i18nString) throws Exception {
		
		OneDriveActivityLog onedrivelogI18N = new OneDriveActivityLog(); 
		String uniqueId = String.valueOf(System.currentTimeMillis());
		
		this.destinationI18NFile = i18nString + "_" + uniqueId + "_" + uniqueFilename;
		this.renamedI18NFile     = i18nString + "_ren_" + uniqueId + "_" +uniqueFilename;
		//Upload file 
		ItemResource itemResource = universalApi.uploadSimpleFile("/", uniqueFilename, destinationI18NFile);
		Reporter.log("Item Resource:"+ itemResource.getId(), true);
		Reporter.log("Item Name:"+ itemResource.getName(), true);
		Reporter.log("Item Size:"+ itemResource.getSize().longValue(), true);
		
		onedrivelogI18N.setFileUploadLog(onedrivelogI18N.getFileUploadLog().replace("{filename}", destinationI18NFile));
		
		createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
		OneDriveBusinessActivity fileUploadLog = new OneDriveBusinessActivity(onedrivelogI18N.getFileUploadLog(), createdTime, Severity.informational.name(),  
														ObjectType.File.name(), ActivityType.Upload.name(), suiteData.getSaasAppUsername(), 
														socUserName, getETagAsDocumentId(itemResource.getETag()), itemResource.getSize().longValue(),  "Root:Documents", "Office 365");
		
		activityMap.put(locale+"fileUploadLog", fileUploadLog);
		
		
		Thread.sleep(CommonConstants.THREE_MINUTES_SLEEP);
		
		//delete the file
		universalApi.deleteFile(itemResource.getId(), "*");
		onedrivelogI18N.setFileDeleteLog(onedrivelogI18N.getFileDeleteLog().replace("{filename}", destinationI18NFile));
		
		createdTime = DateUtils.getDateTime("yyyy-MM-dd'T'HH:mm");
		OneDriveBusinessActivity fileDeleteLog = new OneDriveBusinessActivity(onedrivelogI18N.getFileDeleteLog(), createdTime, Severity.informational.name(),  
										ObjectType.File.name(), ActivityType.Delete.name(), suiteData.getSaasAppUsername(), 
										socUserName, getETagAsDocumentId(itemResource.getETag()), itemResource.getSize(),  null, "Office 365");

		activityMap.put(locale+"fileDeleteLog", fileDeleteLog);
		Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
		
	}
	
	@Test(dependsOnMethods = "performOneDriveI18NFileOperations", priority=-1, groups={"I18N_FILE", "I18N", "P3"})
	public void fetchI18NLogs() throws Exception {
		//Fetch the logs
		try {

			for (int i = 1; i <= (waitTime * 60 * 1000); i+=60000 ) {
				sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
				HashMap<String, String> termmap = new HashMap<String, String>();
				termmap.put("facility", "Office 365");
				termmap.put("Object_type", ObjectType.File.name());

				//Get file related logs
				file18nLogs = this.getInvestigateLogs(-50, 10, "Office 365", termmap, suiteData.getUsername(), suiteData.getApiserverHostName(), 
						suiteData.getCSRFToken(), suiteData.getSessionID(), 0, 300, "Office 365");

			}
		}
		catch(Exception e) {}
		Reporter.log(MarshallingUtils.marshall(file18nLogs), true);
		long total = file18nLogs.getHits().getTotal();
		Reporter.log("Total file logs count:"+ total, true);

		//If total number of logs is less than zero, no logs are seen so skip the tests
		if (total <= 0) {
			throw new SkipException("Skipping the dependent tests..No logs are seen for file with I18n strings in onedrive");
		}
	}
	
	@Test(dependsOnMethods = "fetchI18NLogs", priority=-1, dataProviderClass = O365DataProvider.class, dataProvider = "I18NString", groups={"I18N_FILE", "I18N", "P3"})
	public void verifyI18NFileUploadActivity(String locale, String i18nString) {
		LogUtils.logTestDescription("After uploading a file, verify the activity in the activity logs.");
		logValidator.verifyOnedriveActivityLog(file18nLogs, activityMap.get(locale+"fileUploadLog"));
	}
	
	@Test(dependsOnMethods = "fetchI18NLogs", priority=-1, dataProviderClass = O365DataProvider.class, dataProvider = "I18NString", groups={"I18N_FILE", "I18N", "P3"})
	public void verifyI18NFileDeleteActivity(String locale, String i18nString) {
		LogUtils.logTestDescription("After deleting a file, verify the activity in the activity logs.");
		logValidator.verifyOnedriveActivityLog(file18nLogs, activityMap.get(locale+"fileDeleteLog"));
	}
	
	@Test(dependsOnMethods = "fetchI18NLogs", priority=-1, dataProviderClass = O365DataProvider.class, dataProvider = "I18NString", groups={"I18N_FILE", "I18N", "P3"})
	public void verifyI18NActivitySearch(String locale, String i18nString) throws Exception {
		LogUtils.logTestDescription("Search activity with I18N string.");
		ArrayList<String> logs = new ArrayList<String>();
		logs = searchDisplayLogs(-30, 5, "Office 365", i18nString, suiteData.getUsername(), suiteData.getApiserverHostName(), suiteData.getCSRFToken(), suiteData.getSessionID());
		
		String message = activityMap.get(locale + "I18NFileDownloadActivity").getMessage();
		CustomAssertion.assertTrue(logs.contains(message), message + " is present", message + " is not present");
	}
	
	@Test(dependsOnMethods = "fetchI18NLogs", priority=-1, dataProviderClass = O365DataProvider.class, dataProvider = "I18NString", groups={"I18N_FILE", "I18N", "P3"})
	public void verifyExposedFileSearch(String locale, String i18nString) throws Exception {
		UIExposedDoc payload = this.getUIPayload("Office 365", null, null, "risky_docs", 0, 20, true, URLEncoder.encode(i18nString, "UTF-8"), "name");
		SecurletDocument exposedDocs = getUIRiskyDocuments(MarshallingUtils.marshall(payload), null);
		CustomAssertion.assertTrue(exposedDocs.getMeta().getTotalCount() > 0, "Expected doc count is greater than one ", "Expected doc count is zero");
	}
	
	
	//@Test
	public void testRestore() throws Exception {

		//Upload file 
		ItemResource itemResource = universalApi.uploadSimpleFile("/", uniqueFilename, destinationFile);
		Reporter.log("Item Resource:"+ itemResource.getId(), true);
		Reporter.log("Item Name:"+ itemResource.getName(), true);

		onedrivelog.setFileUploadLog(onedrivelog.getFileUploadLog().replace("{filename}", destinationFile));
		Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);

		//Disable all sharing
//		universalApi.disableSharing(destinationFile);
//		Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
		//User unshared 'Root:Documents' with 'Everyone except external users(Read)'.

		//Share the file
//		SharingUserRoleAssignment shareObject = onedriveUtils.getFileShareObject(itemResource, 1, "Limited Access System Group"); //Limited Access System Group
//		DocumentSharingResult docSharingResult = universalApi.shareWithCollaborators(shareObject);
		Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
		SharingUserRoleAssignment shareObject = onedriveUtils.getFileShareObject(itemResource, 1, "pushparaj@aol.in"); //Limited Access System Group
		universalApi.shareWithCollaborators(shareObject);
		
		shareObject = onedriveUtils.getFileShareObject(itemResource, 2, "pushparaj@aol.in"); //Limited Access System Group
		universalApi.shareWithCollaborators(shareObject);
		
		Thread.sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
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
	
	
	//Utility methods

	public ArrayList<String> searchDisplayLogs(int from, int to, String facilty, String query, String email, 
			String apiServerUrl, String csrfToken, String sessionId, int offset, int limit) throws Exception {

		Reporter.log("Retrieving the logs from Elastic Search ...", true);
//		
//		String tsfrom = getDaysFromCurrentTime(from);
//		String tsto   = getDaysFromCurrentTime(to);
//		
		
		String tsfrom = DateUtils.getMinutesFromCurrentTime(-90);
		String tsto   = DateUtils.getMinutesFromCurrentTime(90);	
		
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
		return onedriveUtils.retrieveActualMessages(fsr);

	}
	
	
	
	
}