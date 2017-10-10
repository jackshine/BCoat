package com.elastica.action.universalapi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.mail.internet.MimeMessage;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.apache.pdfbox.util.PDFTextStripperByArea;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.dropbox.core.v2.DbxSharing;
import com.elastica.common.SuiteData;
import com.elastica.logger.Logger;
import com.google.api.services.gmail.model.Message;
import com.universal.common.DropBox;
import com.universal.common.GDrive;
import com.universal.common.GDriveAuthorization;
import com.universal.common.GoogleMailServices;
import com.universal.common.Office365MailActivities;
import com.universal.common.Salesforce;
import com.universal.common.UniversalApi;
import com.universal.dtos.UserAccount;
import com.universal.dtos.box.AccessibleBy;
import com.universal.dtos.box.BoxCollaboration;
import com.universal.dtos.box.BoxFolder;
import com.universal.dtos.box.CollaborationInput;
import com.universal.dtos.box.FileEntry;
import com.universal.dtos.box.FileUploadResponse;
import com.universal.dtos.box.Item;
import com.universal.dtos.box.SharedLink;
import com.universal.dtos.onedrive.DocumentSharingResult;
import com.universal.dtos.onedrive.Folder;
import com.universal.dtos.onedrive.ItemResource;
import com.universal.dtos.onedrive.Metadata;
import com.universal.dtos.onedrive.SharingUserRoleAssignment;
import com.universal.dtos.onedrive.UserRoleAssignment;
import com.universal.dtos.salesforce.ChatterFile;

import microsoft.exchange.webservices.data.core.enumeration.service.DeleteMode;

/**
 * Universal API Functions
 * @author eldorajan
 *
 */
public class UniversalAPIFunctions {


	public Map<String,String> createFolder(UniversalApi universalApi, SuiteData suiteData, String folderName) {
		Logger.info("Creating new folder "+folderName+" in saas app is in progress");


		String saasType = suiteData.getSaasAppName();
		SaasType stype = SaasType.getSaasType(saasType);
		String folderId = null;String folderEtag = null;String folderType = null;
		Map<String,String> folderInfo = new HashMap<String,String>();
		try {
			switch (stype) {
			case Box: {
				org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.OFF);
				BoxFolder folderObj = universalApi.createFolder(folderName);
				folderId = folderObj.getId();
				folderEtag = folderObj.getEtag();
				folderType = folderObj.getType();
				org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.INFO);
				break;
			}
			case Dropbox: {
				universalApi.createFolder(File.separator+folderName);
				folderId = File.separator+folderName;folderEtag = "";
				break;
			}
			case GoogleDrive: {
				String folderObj = universalApi.createFolder(folderName);
				folderId = folderObj;folderEtag = "";
				break;
			}
			case OneDrive:
			case Office365:
			case OneDriveBusiness: {
				org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.OFF);
				Folder folderObj = universalApi.createFolderV2(folderName);
				folderId = folderObj.getId();folderEtag = (String) folderObj.getETag();
				org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.INFO);

				break;
			}
			default: {

				break;
			}
			}
		} catch (Exception ex) {
			org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.INFO);

			ex.printStackTrace();
		}

		Logger.info("Creating new folder "+folderName+" in saas app is completed");

		folderInfo.put("folderName", folderName);
		folderInfo.put("folderId", folderId);
		folderInfo.put("folderEtag", folderEtag);
		folderInfo.put("folderType", folderType); 

		return folderInfo;
	}


	public Folder createFolderInOneDrive(UniversalApi universalApi, SuiteData suiteData, String folderName) {
		Logger.info("Creating new folder "+folderName+" in saas app is in progress");

		Folder folderObj=null;
		String saasType = suiteData.getSaasAppName();
		SaasType stype = SaasType.getSaasType(saasType);
		try {
			switch (stype) {
			case OneDrive:
			case Office365:
			case OneDriveBusiness: {
				org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.OFF);
				folderObj = universalApi.createFolderV2(folderName);
				org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.INFO);
				break;
			}
			default: {

				break;
			}
			}
		} catch (Exception ex) {
			org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.INFO);

			ex.printStackTrace();
		}

		return folderObj;
	}

	/**
	 * delete folder for saastype in saas app
	 * @param saasType
	 * @param folderId
	 */
	public void deleteFolder(UniversalApi universalApi, SuiteData suiteData, Map<String,String> folderInfo) {
		Logger.info("Deleting folder "+folderInfo.get("folderId")+" in saas app is in progress");

		String saasType = suiteData.getSaasAppName();
		SaasType stype = SaasType.getSaasType(saasType);
		try {
			switch (stype) {
			case Box: {
				org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.OFF);
				universalApi.deleteFolder(folderInfo.get("folderId"), true, null);
				org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.INFO);
				break;
			}
			case Dropbox: {
				universalApi.deleteFolder(folderInfo.get("folderId"), true, null);
				break;
			}
			case GoogleDrive: {
				universalApi.deleteFolder(folderInfo.get("folderId"), true, null);
				break;
			}
			case OneDrive:
			case Office365:
			case OneDriveBusiness: {
				org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.OFF);
				universalApi.deleteFolderV2(folderInfo.get("folderId"), folderInfo.get("folderEtag"));
				org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.INFO);
				break;
			}
			default: {

				break;
			}
			}
		} catch (Exception ex) {
			org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.INFO);

		}

		Logger.info("Deleting new folder "+folderInfo.get("folderId")+" in saas app is completed");

	}

	/**
	 * Delete file
	 * @param universalApi
	 * @param saasType
	 * @param fileInfo
	 */
	public void deleteFile(UniversalApi universalApi, SuiteData suiteData, Map<String,String> fileInfo) {
		Logger.info("Deleting file "+fileInfo.get("fileId")+" in saas app is in progress");
		String saasType = suiteData.getSaasAppName();
		SaasType stype = SaasType.getSaasType(saasType);
		try {
			switch (stype) {
			case Box: {
				org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.OFF);
				universalApi.deleteFile(fileInfo.get("fileId"), fileInfo.get("fileEtag"));
				org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.INFO);
				break;
			}
			case Dropbox: {
				universalApi.deleteFile(fileInfo.get("fileName"), null);
				break;
			}
			case GoogleDrive: {
				universalApi.deleteFile(fileInfo.get("fileId"), null);
				break;
			}
			case OneDrive:
			case Office365:
			case OneDriveBusiness: {
				org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.OFF);
				universalApi.deleteFile(fileInfo.get("fileId"), fileInfo.get("fileEtag"));
				org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.INFO);
				break;
			}
			case Salesforce:{
				Salesforce sfapi = universalApi.getSalesforce();
				sfapi.deleteFile(fileInfo.get("fileId"));
				break;
			}
			default: {

				break;
			}
			}
		} catch (Exception ex) {
			org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.INFO);

		}

		Logger.info("Deleting file "+fileInfo.get("fileId")+" in saas app is completed");

	}

	/**
	 * upload a file into folder for saastype in saas app
	 * @param saasType
	 * @param folderId
	 * @param fileName
	 */
	public Map<String,String> uploadFile(UniversalApi universalApi, SuiteData suiteData, String folderId, String fileName) {
		Logger.info("Uploading file "+fileName+" in saas app is in progress");
		String saasType = suiteData.getSaasAppName();
		SaasType stype = SaasType.getSaasType(saasType);

		String fileId = null;String fileEtag = null;
		Map<String,String> fileInfo = new HashMap<String,String>();

		try {
			switch (stype) {
			case Box: {
				org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.OFF);
				FileUploadResponse fResponse = universalApi.uploadFile(folderId,
						fileName, fileName);
				fileId = fResponse.getFileId();
				fileEtag = (String) fResponse.getEtag();
				org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.INFO);
				break;
			}
			case Dropbox: {
				FileUploadResponse fResponse = universalApi.uploadFile(folderId,fileName);
				fileId = fResponse.getFileId();fileEtag = "";
				break;
			}
			case GoogleDrive: {
				FileUploadResponse fResponse = universalApi.uploadFile(folderId, fileName);
				fileId = fResponse.getFileId();
				fileName = "//"+fileName;
				break;
			}
			case OneDrive:
			case Office365:
			case OneDriveBusiness: {
				org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.OFF);
				ItemResource fResponse = universalApi.uploadSimpleFile(folderId, fileName, fileName);
				fileId = fResponse.getId();
				fileEtag = (String) fResponse.getETag();
				org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.INFO);

				break;
			}
			case Salesforce: {
				Salesforce sfapi = universalApi.getSalesforce();
				ChatterFile chatterfile = sfapi.uploadFileToChatter(fileName, fileName);
				fileId = chatterfile.getId();
				break;
			}
			default: {

				break;
			}
			}

		} catch (Exception ex) {
			org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.INFO);

		}

		fileInfo.put("fileName", fileName);
		fileInfo.put("fileId", fileId);
		fileInfo.put("fileEtag", fileEtag);

		Logger.info("Uploading file "+fileName+" in saas app is completed");

		return fileInfo;
	}

	public ItemResource uploadFileInOneDrive(UniversalApi universalApi, SuiteData suiteData, String folderId, String fileName) {
		Logger.info("Uploading file "+fileName+" in saas app is in progress");
		String saasType = suiteData.getSaasAppName();
		SaasType stype = SaasType.getSaasType(saasType);
		ItemResource itemResource=null;
		try {
			switch (stype) {

			case OneDrive:
			case Office365:
			case OneDriveBusiness: {
				org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.OFF);
				itemResource = universalApi.uploadSimpleFile(folderId, fileName, fileName);
				org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.INFO);

				break;
			}
			default: {

				break;
			}
			}

		} catch (Exception ex) {
			org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.INFO);

		}

		Logger.info("Uploading file "+fileName+" in saas app is completed");

		return itemResource;
	}

	public Map<String,String> shareFile(SuiteData suiteData, UniversalApi universalApi,
			Map<String,String> folderInfo,Map<String,String> fileInfo, String shareType) {

		String saasType = suiteData.getSaasAppName();
		SaasType stype = SaasType.getSaasType(saasType);

		FileEntry sharedFile = null;
		try {
			switch (stype) {
			case Box: {
				org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.OFF);
				if(shareType.equalsIgnoreCase("Public")){
					String fileName=fileInfo.get("fileName");String fileId=fileInfo.get("fileId");

					Logger.info("Sharing of file "+fileName+" in saas app is in progress");

					SharedLink sharedLink = new SharedLink();
					sharedLink.setAccess("open");
					sharedFile = universalApi.createSharedLink(fileId, sharedLink); 

					fileInfo.put("fileId", sharedFile.getId());
					fileInfo.put("fileEtag", sharedFile.getEtag());
					fileInfo.put("fileName", fileName);

					Logger.info("Shared file access:" + sharedFile.getSharedLink().getAccess());
					Logger.info("Shared file effective access:" + sharedFile.getSharedLink().getEffectiveAccess());

					Logger.info("Sharing of file "+fileName+" in saas app is completed");

				}else if(shareType.equalsIgnoreCase("Internal")){
					String fileName=fileInfo.get("fileName");String fileId=fileInfo.get("fileId");

					Logger.info("Sharing of file "+fileName+" in saas app is in progress");

					SharedLink sharedLink = new SharedLink();
					sharedLink.setAccess("company");
					sharedFile = universalApi.createSharedLink(fileId, sharedLink); 

					fileInfo.put("fileId", sharedFile.getId());
					fileInfo.put("fileEtag", sharedFile.getEtag());
					fileInfo.put("fileName", fileName);

					Logger.info("Shared file access:" + sharedFile.getSharedLink().getAccess());
					Logger.info("Shared file effective access:" + sharedFile.getSharedLink().getEffectiveAccess());

					Logger.info("Sharing of file "+fileName+" in saas app is completed");

				}else if(shareType.equalsIgnoreCase("External")){
					String folderName=folderInfo.get("folderName");String folderId=folderInfo.get("folderId");

					Logger.info("Sharing of folder "+folderName+" in saas app is in progress");

					CollaborationInput collabInput = new CollaborationInput();
					Item item = new Item();
					item.setId(folderId);
					item.setType(folderInfo.get("folderType"));

					AccessibleBy aby = new AccessibleBy();
					aby.setName("Automation Tester");
					aby.setType("user");
					aby.setLogin("theautomationtester87@gmail.com");

					collabInput.setItem(item);
					collabInput.setAccessibleBy(aby);
					collabInput.setRole("editor");

					//Create the collaboration
					BoxCollaboration collaboration = universalApi.createCollaboration(collabInput);
					Logger.info("Collaboration Id:" + collaboration.getId());

					fileInfo.put("folderId", folderId);
					fileInfo.put("folderEtag", "*");
					fileInfo.put("folderName", folderName);

					Logger.info("Sharing of folder "+folderName+" in saas app is completed");
				}

				org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.INFO);

				break;
			}
			case Dropbox: {
				String fileName=fileInfo.get("fileName");
				String folderName=folderInfo.get("folderName");String folderId=folderInfo.get("folderId");String folderEtag=folderInfo.get("folderEtag");

				DropBox dropbox = universalApi.getDropbox();

				if(shareType.equalsIgnoreCase("Public")){
					dropbox.createSharedLinkForFolderORFile(folderId + "/" + fileName);
				}else if(shareType.equalsIgnoreCase("Internal")){
					fileInfo = uploadFile(universalApi,suiteData, folderId, fileName.replace(File.separator+getDropboxInternalFolderName(suiteData)+File.separator, ""));
					fileInfo.put("fileName", fileName);
					fileInfo.put("fileEtag", "");
				}else if(shareType.equalsIgnoreCase("External")){
					String externalUserEmailId=suiteData.getSaasAppExternalUsername();
					String externalUserAccessToken=suiteData.getSaasAppExternalUserToken();
					fileInfo = dropbox.shareAndMountFolder(folderId, externalUserEmailId, 
							DbxSharing.AccessLevel.viewer, externalUserAccessToken);
					fileInfo.put("folderId", folderId);
					fileInfo.put("folderName", folderName);
					fileInfo.put("folderEtag", folderEtag);
				}
				break;
			}
			case GoogleDrive: {
				String fileId=fileInfo.get("fileId");
				GDrive gDrive = universalApi.getgDrive();
				if(shareType.equalsIgnoreCase("External")){
					gDrive.insertPermission(gDrive.getDriveService(), fileId, "theautomationtester87@gmail.com", "user", "reader");
					/*fileInfo.put("folderId", permission.getId());
					fileInfo.put("folderEtag", permission.getEtag());
					fileInfo.put("folderName", folderName);*/
				}
				if(shareType.equalsIgnoreCase("Internal")){
					gDrive.insertPermission(gDrive.getDriveService(), fileId, suiteData.getTenantDomainName(), "domain", "reader");
					/*fileInfo.put("fileId", permission.getId());
					fileInfo.put("fileEtag", permission.getEtag());
					fileInfo.put("fileName", fileName);*/
				}
				if(shareType.equalsIgnoreCase("Public")){
					gDrive.insertPermission(gDrive.getDriveService(), fileId, null, "anyone", "reader");
					/*fileInfo.put("fileId", permission.getId());
					fileInfo.put("fileEtag", permission.getEtag());
					fileInfo.put("fileName", fileName);*/
				}
				break;
			}
			case OneDrive:
			case Office365:
			case OneDriveBusiness: {

				org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.OFF);
				ItemResource fileResource = null;Folder folderResource = null;
				if(shareType.equalsIgnoreCase("Public")){
					shareFileOnOneDrive(fileResource, universalApi, shareType);
				}else if(shareType.equalsIgnoreCase("Internal")){
					shareFileOnOneDrive(fileResource, universalApi, shareType);
				}else if(shareType.equalsIgnoreCase("External")){
					shareFolderOnOneDrive(folderResource, universalApi, shareType);
				}
				org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.INFO);

				break;
			}
			default: {

				break;
			}
			}

		} catch (Exception ex) {
			org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.INFO);
			ex.printStackTrace();
		}


		return fileInfo;
	}

	public DocumentSharingResult shareFileOnOneDrive(ItemResource itemResource, UniversalApi universalApi, String exposureType) throws Exception{
		//share the file with Everyone
		SharingUserRoleAssignment shareObject = new SharingUserRoleAssignment();
		ArrayList<UserRoleAssignment> alist = new ArrayList<UserRoleAssignment>();
		UserRoleAssignment userRoleAssignment = new UserRoleAssignment();
		Metadata metadata = new Metadata();
		metadata.setType("SP.Sharing.UserRoleAssignment");
		userRoleAssignment.setMetadata(metadata);

		if(exposureType.equalsIgnoreCase("Public")){
			userRoleAssignment.setRole(2);
			userRoleAssignment.setUserId("theautomationtester87@gmail.com");
			alist.add(userRoleAssignment);

			shareObject.setUserRoleAssignments(alist);
			shareObject.setResourceAddress(itemResource.getWebUrl());
			shareObject.setValidateExistingPermissions(false);
			shareObject.setAdditiveMode(true);
			shareObject.setSendServerManagedNotification(false);
			shareObject.setCustomMessage("Hi Pls. look at the following document");
			shareObject.setIncludeAnonymousLinksInNotification(true);
		} else if(exposureType.equalsIgnoreCase("Internal")){
			userRoleAssignment.setRole(2);
			userRoleAssignment.setUserId("Everyone except external users");
			alist.add(userRoleAssignment);

			shareObject.setUserRoleAssignments(alist);
			shareObject.setResourceAddress(itemResource.getWebUrl());
			shareObject.setValidateExistingPermissions(false);
			shareObject.setAdditiveMode(true);
			shareObject.setSendServerManagedNotification(false);
			shareObject.setCustomMessage("Hi Pls. look at the following document");
			shareObject.setIncludeAnonymousLinksInNotification(false);
		}else if(exposureType.equalsIgnoreCase("External")){
			userRoleAssignment.setRole(1);
			userRoleAssignment.setUserId("theautomationtester87@gmail.com");
			alist.add(userRoleAssignment);

			shareObject.setUserRoleAssignments(alist);
			shareObject.setResourceAddress(itemResource.getWebUrl());
			shareObject.setValidateExistingPermissions(false);
			shareObject.setAdditiveMode(true);
			shareObject.setSendServerManagedNotification(false);
			shareObject.setCustomMessage("Hi Pls. look at the following document");
			shareObject.setIncludeAnonymousLinksInNotification(false);
		}

		DocumentSharingResult documentSharingResult = universalApi.shareWithCollaborators(shareObject);
		Logger.info(documentSharingResult);
		return documentSharingResult;
	}


	public DocumentSharingResult shareFolderOnOneDrive(Folder itemResource, UniversalApi universalApi, String exposureType) throws Exception{
		SharingUserRoleAssignment sharingUserRoleAssignment = new SharingUserRoleAssignment();
		ArrayList<UserRoleAssignment> userRoleAssignmentList = new ArrayList<UserRoleAssignment>();
		UserRoleAssignment userRoleAssignment = new UserRoleAssignment();
		Metadata metadata = new Metadata();
		metadata.setType("SP.Sharing.UserRoleAssignment");
		userRoleAssignment.setMetadata(metadata);
		if(exposureType.equalsIgnoreCase("Public")){
			userRoleAssignment.setUserId("Everyone");
			userRoleAssignment.setRole(1);
		} else if(exposureType.equalsIgnoreCase("Internal")){
			userRoleAssignment.setUserId("Everyone except external users");
			userRoleAssignment.setRole(2);
		}else if(exposureType.equalsIgnoreCase("External")){
			userRoleAssignment.setUserId("theautomationtester87@gmail.com");
			userRoleAssignment.setRole(2);
		}
		userRoleAssignmentList.add(userRoleAssignment);
		sharingUserRoleAssignment.setUserRoleAssignments(userRoleAssignmentList);
		sharingUserRoleAssignment.setResourceAddress(itemResource.getWebUrl());
		sharingUserRoleAssignment.setValidateExistingPermissions(false);
		sharingUserRoleAssignment.setAdditiveMode(true);
		sharingUserRoleAssignment.setSendServerManagedNotification(false);
		sharingUserRoleAssignment.setCustomMessage("This is a custom message");
		if(exposureType.equalsIgnoreCase("External")){
			sharingUserRoleAssignment.setIncludeAnonymousLinksInNotification(true);
		}else{
			sharingUserRoleAssignment.setIncludeAnonymousLinksInNotification(false);
		}
		DocumentSharingResult documentSharingResult = universalApi.shareWithCollaborators(sharingUserRoleAssignment);
		Logger.info(documentSharingResult);
		return documentSharingResult;
	}


	public Map<String,String> sendMailWithAttachmentBody(SuiteData suiteData, String fileName, String bodyType) {

		String saasType = suiteData.getSaasAppName();
		String username = suiteData.getSaasAppUsername();
		String password = suiteData.getSaasAppPassword();
		String recipientEmail = suiteData.getSaasAppUsername();

		Map<String,String> messageInfo = new HashMap<String,String>();

		SaasType stype = SaasType.getSaasType(saasType);
		Office365MailActivities objMail=null;GoogleMailServices gobjMail=null;

		String fileLocation = fileName;
		File attachmentFile = new File(fileLocation);
		ArrayList<String> attachment = new ArrayList<String>();
		attachment.add(attachmentFile.toString());

		try {

			switch (stype) {
			case GmailAttachment: {
				Logger.info("Sending email "+fileName+" as attachment in saas app is in progress");

				gobjMail = new GoogleMailServices(suiteData.getSaasAppClientId(), 
						suiteData.getSaasAppClientSecret(), suiteData.getSaasAppToken()); 
				List<String> to=new ArrayList<String>();to.add("box-"+username);
				Message message = gobjMail.sendMailWithAttachment(to, null, null, fileName+" Mail With Attachment", 
						"This is test mail", fileLocation, fileName);

				messageInfo.put("messageId", message.getId());

				Logger.info("Sending email "+fileName+" as attachment in saas app is completed");
				break;
			}
			case GmailBody: {
				Logger.info("Sending email "+fileName+" as body in saas app is in progress");

				gobjMail = new GoogleMailServices(suiteData.getSaasAppClientId(), 
						suiteData.getSaasAppClientSecret(), suiteData.getSaasAppToken()); 
				List<String> to=new ArrayList<String>();to.add("box-"+username);
				String mailBody = readFile(fileLocation);
				Message message = gobjMail.sendMailWithBody(to, null, null, fileName+" Mail With Body", 
						mailBody);

				messageInfo.put("messageId", message.getId());

				Logger.info("Sending email "+fileName+" as body in saas app is completed");
				break;
			}
			case GmailAttachmentBody: {
				Logger.info("Sending email "+fileName+" as attachment and body in saas app is in progress");

				gobjMail = new GoogleMailServices(suiteData.getSaasAppClientId(), 
						suiteData.getSaasAppClientSecret(), suiteData.getSaasAppToken()); 
				List<String> to=new ArrayList<String>();to.add("box-"+username);
				String mailBody = readFile(fileLocation);
				Message message = gobjMail.sendMailWithAttachment(to, null, null, fileName+" Mail With Attachment and Body", 
						mailBody, fileLocation, fileName);

				messageInfo.put("messageId", message.getId());

				Logger.info("Sending email "+fileName+" as attachment in saas app is completed");
				break;
			}
			case Office365MailAttachment: {
				Logger.info("Sending email "+fileName+" as attachment in saas app is in progress");

				objMail = new Office365MailActivities(username,password); 
				objMail.sendMail(recipientEmail, fileName+" Mail With Attachment", "This is test mail", bodyType, attachment, false);

				Logger.info("Sending email "+fileName+" as attachment in saas app is completed");
				break;
			}
			case Office365MailBody: {
				Logger.info("Sending email "+fileName+" as body in saas app is in progress");

				objMail = new Office365MailActivities(username,password); 
				String mailBody = readFile(fileLocation);
				objMail.sendMail(recipientEmail, fileName+" Mail With Body", mailBody, bodyType, null, false);

				Logger.info("Sending email "+fileName+" as body in saas app is completed");
				break;
			}
			case Office365MailAttachmentBody: {
				Logger.info("Sending email "+fileName+" as attachment and body in saas app is in progress");

				objMail = new Office365MailActivities(username,password); 
				String mailBody = readFile(fileLocation);
				objMail.sendMail(recipientEmail, fileName+" Mail With Attachment and Body", mailBody, bodyType, attachment, false);


				Logger.info("Sending email "+fileName+" as attachment in saas app is completed");
				break;
			}
			default: {
				Logger.info("Saas App not supported");
				break;
			}
			}

		} catch (Exception ex) {
			org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.INFO);
			Logger.info("Issue in Sending Mail/Message :"+ex.getLocalizedMessage()); 
		}

		return messageInfo;
	}



	public void deleteEmails(SuiteData suiteData,List<String> mailId) {
		try {


			SaasType stype = SaasType.getSaasType(suiteData.getSaasAppName());
			try {

				switch (stype) {
				case GmailAttachment:
				case GmailBody:
				case GmailAttachmentBody: {
					for(String s:mailId){
						GoogleMailServices gobjMail = new GoogleMailServices(suiteData.getSaasAppClientId(), 
								suiteData.getSaasAppClientSecret(), suiteData.getSaasAppToken());
						gobjMail.deleteThread(s);
					}
					break;
				}
				case Office365MailAttachment:
				case Office365MailBody:
				case Office365MailAttachmentBody: {
					for(String s:mailId){
						Office365MailActivities objMail = new Office365MailActivities(suiteData.getSaasAppUsername(),suiteData.getSaasAppPassword()); 
						DeleteMode delMode = DeleteMode.HardDelete;	
						objMail.deleteMail(s,delMode);
					}
					break;
				}
				default: {
					Logger.info("Saas App not supported");
					break;
				}
				}

			} catch (Exception ex) {
				org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.INFO);
				Logger.info("Issue in Sending Mail/Message :"+ex.getLocalizedMessage()); 
			}


		} catch (Exception ex) {
			Logger.info("Issue with Delete Mail Operation " + ex.getLocalizedMessage());
		}
	}

	public UserAccount getUserAccount(SuiteData suiteData) {
		SaasType stype = SaasType.getSaasType(suiteData.getSaasAppName());
		UserAccount account = null;
		try {
			switch (stype) {
			case Box: {
				org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.OFF);
				account = new UserAccount(suiteData.getSaasAppUsername(), 
						suiteData.getSaasAppPassword(), suiteData.getSaasAppUserRole());
				org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.INFO);
				break;
			}
			case Dropbox: {
				account = new UserAccount(suiteData.getSaasAppUsername(), 
						suiteData.getSaasAppPassword(), suiteData.getSaasAppUserRole(), suiteData.getSaasAppToken());
				break;
			}
			case GoogleDrive: {
				GDriveAuthorization gDriveAuthorization=new GDriveAuthorization(suiteData.getSaasAppClientId(), 
						suiteData.getSaasAppClientSecret());
				String authToken = gDriveAuthorization.getAceessTokenFromRefreshAccessToken(suiteData.getSaasAppToken());
				account = new UserAccount(suiteData.getSaasAppUsername(), 
						suiteData.getSaasAppPassword(), suiteData.getSaasAppUserRole(),authToken);
				break;
			}
			case OneDrive:
			case Office365:
			case OneDriveBusiness: {
				org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.OFF);
				account = new UserAccount(suiteData.getSaasAppUsername(), 
						suiteData.getSaasAppPassword(), suiteData.getSaasAppUserRole(), suiteData.getEnvName(), null, 
						suiteData.getTenantDomainName());
				org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.INFO);
				break;
			}
			case Salesforce: {
				HashMap<String, String> instanceParams = new HashMap<String, String>();
				instanceParams.put("apiHost", suiteData.getSaasAppLoginHost());
				account = new UserAccount(suiteData.getSaasAppUsername(), suiteData.getSaasAppPassword(), 
						suiteData.getSaasAppUserRole(), instanceParams);
				break;
			}
			default: {

				break;
			}
			}

		} catch (Exception ex) {
			org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.INFO);

		}
		return account;
	}

	public UniversalApi getUniversalApi(SuiteData suiteData,UserAccount account) {
		SaasType stype = SaasType.getSaasType(suiteData.getSaasAppName());
		UniversalApi universalApi = null;

		try {
			switch (stype) {
			case Box: {
				org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.OFF);
				universalApi = new UniversalApi("BOX", account);
				org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.INFO);
				break;
			}
			case Dropbox: {
				universalApi = new UniversalApi("DROPBOX", account);
				break;
			}
			case GoogleDrive: {
				universalApi = new UniversalApi("GDRIVE", account);
				break;
			}
			case OneDrive:
			case Office365:
			case OneDriveBusiness: {
				org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.OFF);
				universalApi = new UniversalApi("ONEDRIVEBUSINESS", account);
				org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.INFO);
				break;
			}
			case Salesforce: {
				universalApi = new UniversalApi("SALESFORCE", account);
				break;
			}
			default: {

				break;
			}
			}

		} catch (Exception ex) {
			org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.INFO);
			ex.printStackTrace();
		}
		return universalApi;
	}	

	public String getDropboxInternalFolderName(SuiteData suiteData){
		if(suiteData.getTenantDomainName().equalsIgnoreCase("securletautofeatle.com")){
			return "securletautofe Team Folder";
		}else if(suiteData.getTenantDomainName().equalsIgnoreCase("securletfeatle.com")){
			return "securletfeatle Team Folder";
		}else{
			return "Elastica QA Team Folder";
		}
	}


	public String readFile(String filePath) {
		String body="";

		if(filePath.contains(".pdf")){
			try{
				PDDocument document = null; 
				document = PDDocument.load(new File(filePath));
				document.getClass();
				if( !document.isEncrypted() ){
					PDFTextStripperByArea stripper = new PDFTextStripperByArea();
					stripper.setSortByPosition( true );
					PDFTextStripper Tstripper = new PDFTextStripper();
					String st = Tstripper.getText(document);
					body+=st;body+=System.getProperty("line.separator");

					document.close();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}else if(filePath.contains(".xls")){
			try {
				FileInputStream file = new FileInputStream(new File(filePath));
				XSSFWorkbook workbook = new XSSFWorkbook(file);
				XSSFSheet sheet = workbook.getSheetAt(0);
				Iterator<Row> rowIterator = sheet.iterator();
				while (rowIterator.hasNext()){
					Row row = rowIterator.next();
					Iterator<Cell> cellIterator = row.cellIterator();
					while (cellIterator.hasNext()){
						Cell cell = cellIterator.next();
						switch (cell.getCellType()){
						case Cell.CELL_TYPE_NUMERIC:
							body+=cell.getNumericCellValue();body+=System.getProperty("line.separator");
							break;
						case Cell.CELL_TYPE_STRING:
							body+=cell.getStringCellValue();body+=System.getProperty("line.separator");
							break;
						}
					}
				}
				workbook.close();
				file.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}else if(filePath.contains(".docx")||filePath.contains(".doc")){
			try{

				File file = new File(filePath);
				FileInputStream fis = new FileInputStream(file.getAbsolutePath());
				HWPFDocument document = new HWPFDocument(fis);
				WordExtractor extractor = new WordExtractor(document);
				String[] fileData = extractor.getParagraphText();
				for (int i = 0; i < fileData.length; i++){
					if (fileData[i] != null)
						body+=fileData[i];
				}
				extractor.close();
				fis.close();
			}
			catch (Exception ex){
				ex.printStackTrace();
			}
		}
		else{
			StringBuffer stringBuffer = new StringBuffer();
			try{
				BufferedReader br = new BufferedReader(
						new InputStreamReader(
								new FileInputStream(filePath), "UTF8"));

				String sCurrentLine;
				while ((sCurrentLine = br.readLine()) != null) {
					stringBuffer.append(sCurrentLine);
					stringBuffer.append(System.getProperty("line.separator"));
					stringBuffer.append("\n");
				}
				br.close();


			} catch (Exception e) {
				e.printStackTrace();
			}
			body = stringBuffer.toString().replaceAll("�", "");


		}
		System.out.println("##############Email Body:"+body);
		return body;
	}


	public String getVerificationCodeFromEmail(SuiteData suiteData, String subject) throws Exception{

		GoogleMailServices gmailServices = new GoogleMailServices(suiteData.getSaasAppClientId(), 
				suiteData.getSaasAppClientSecret(), suiteData.getSaasAppToken());
			
		Message message = gmailServices.getLatestMail(subject);
		MimeMessage mimeMessage = gmailServices.getMimeMessage(message.getId());
		String content = (String) mimeMessage.getContent();
		content = content.substring(content.indexOf("Verification Code")).replaceAll("\\n", "").replaceAll("\\r", "");
		content = content.substring(content.indexOf("Verification Code"),content.indexOf("If"));
		
		String verificationCode = content.replace("Verification Code: ", "");
		
		return verificationCode;
	}

	public String getVerificationCode(SuiteData suiteData) throws Exception{
		String clientId = "998314684213-34jm3g4k92nejio174qnnb32vojbqg0n.apps.googleusercontent.com";
		String clientSecret = "YkMqf5GWiHQgHbA1P7BNhxto";
		String refreshToken = "1/5kWmXhyU4AWIGY0f3fcWET6U3YxSWXJl-Ygz39BlS14";
		String subject = "Sandbox: Verify your identity in Salesforce";

		suiteData.setSaasAppClientId(clientId);
		suiteData.setSaasAppClientSecret(clientSecret);
		suiteData.setSaasAppToken(refreshToken);
		
		return getVerificationCodeFromEmail(suiteData, subject);
	}
	

	

	
	

}