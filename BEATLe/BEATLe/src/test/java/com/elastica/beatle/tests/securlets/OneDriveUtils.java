package com.elastica.beatle.tests.securlets;

import java.util.ArrayList;
import java.util.UUID;

import org.testng.Reporter;

import com.elastica.beatle.CommonTest;
import com.elastica.beatle.securlets.dto.ForensicSearchResults;
import com.elastica.beatle.securlets.dto.Hit;
import com.elastica.beatle.securlets.dto.Source;
import com.universal.dtos.box.FileEntry;
import com.universal.dtos.box.FileUploadResponse;
import com.universal.dtos.box.SharedLink;
import com.universal.dtos.onedrive.Folder;
import com.universal.dtos.onedrive.ItemResource;
import com.universal.dtos.onedrive.Metadata;
import com.universal.dtos.onedrive.Result;
import com.universal.dtos.onedrive.SharingUserRoleAssignment;
import com.universal.dtos.onedrive.SiteFileResource;
import com.universal.dtos.onedrive.UserRoleAssignment;

public class OneDriveUtils extends CommonTest{
	
	
	public SharingUserRoleAssignment getRootSiteFileShareObject(String fileurl, int role, String sharedWithUsers) throws Exception{
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
		shareObject.setResourceAddress(fileurl);
		shareObject.setValidateExistingPermissions(false);
		shareObject.setAdditiveMode(true);
		shareObject.setSendServerManagedNotification(false);
		shareObject.setCustomMessage("Hi Pls. look at the following document");
		shareObject.setIncludeAnonymousLinksInNotification(true);

		return shareObject;
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
	
	public SharingUserRoleAssignment getFolderShareObject(Folder itemResource, int role, String sharedWithUsers) throws Exception{
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
	
	
	public SharingUserRoleAssignment getDocumentShareObject(Result documentResource, int role, String sharedWithUsers) throws Exception{
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
		shareObject.setResourceAddress(documentResource.getMetadata().getUri());
		shareObject.setValidateExistingPermissions(false);
		shareObject.setAdditiveMode(true);
		shareObject.setSendServerManagedNotification(false);
		shareObject.setCustomMessage("Hi Pls. look at the following document");
		shareObject.setIncludeAnonymousLinksInNotification(false);

		return shareObject;
	}
	
	
	public SharingUserRoleAssignment getPublicShareObject(String resourceUrl, int role) throws Exception{
		//share the file with Everyone
		SharingUserRoleAssignment shareObject = new SharingUserRoleAssignment();

		ArrayList<UserRoleAssignment> alist = new ArrayList<UserRoleAssignment>();
		UserRoleAssignment userRoleAssignment = new UserRoleAssignment();

		Metadata metadata = new Metadata();
		metadata.setType("SP.Sharing.UserRoleAssignment");

		userRoleAssignment.setMetadata(metadata);
		userRoleAssignment.setRole(role);
		userRoleAssignment.setUserId("securletuser@gmail.com");
		alist.add(userRoleAssignment);

		shareObject.setUserRoleAssignments(alist);
		shareObject.setResourceAddress(resourceUrl);
		shareObject.setValidateExistingPermissions(false);
		shareObject.setAdditiveMode(true);
		shareObject.setSendServerManagedNotification(false);
		shareObject.setCustomMessage("Hi Pls. look at the following document");
		shareObject.setIncludeAnonymousLinksInNotification(true);

		return shareObject;
	}
	
	
	ArrayList<String> retrieveActualMessages(ForensicSearchResults fsr) {
		ArrayList<String> alist = new ArrayList<String>();
		
		for (Hit hit : fsr.getHits().getHits()) {
			Source source  = hit.getSource();
			alist.add(source.getMessage());
		}
		return alist;
	}
	
	
	
}
