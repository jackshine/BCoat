package com.universal.common;

//import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;

import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.testng.Reporter;

import com.universal.dtos.UserAccount;
import com.universal.dtos.box.BoxCollaboration;
import com.universal.dtos.box.BoxGroup;
import com.universal.dtos.box.BoxMembership;
import com.universal.dtos.box.BoxUserInfo;
import com.universal.dtos.box.BoxWeblink;
import com.universal.dtos.box.CollaborationInput;
import com.universal.dtos.box.FileUploadResponse;
import com.universal.dtos.box.GroupInput;
import com.universal.dtos.box.GroupList;
import com.universal.dtos.box.MembershipInput;
import com.universal.dtos.box.SharedLink;
import com.universal.dtos.box.WeblinkInput;
import com.universal.dtos.onedrive.CopyObject;
import com.universal.dtos.onedrive.ItemResource;
import com.universal.dtos.onedrive.Parameters;
import com.universal.dtos.onedrive.Result;
import com.universal.dtos.onedrive.ShareType;
import com.universal.dtos.onedrive.SharingUserRoleAssignment;
import com.universal.dtos.onedrive.SiteFolderInput;
import com.universal.dtos.onedrive.SiteFolderUpdateInput;
import com.universal.dtos.onedrive.SiteInput;
import com.universal.dtos.onedrive.UpdateList;
import com.universal.dtos.onedrive.UserInput;
import com.universal.dtos.onedrive.Value;
import com.universal.util.TokenProducer;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Locale;
import java.util.ResourceBundle;

public class UniversalApi {
	Box box; 
	DropBox  dropbox;
	GDrive gDrive;
	OneDriveBusiness onedrivebusiness;
	Properties properties = new Properties();
	String application;
	private TokenProducer tokenProducer;
	private String tenant;
	Salesforce salesForce;
	ServiceNow serviceNow;
        GoogleMailServices googleMailServices;

    

	public UniversalApi(String application, UserAccount userAccount) throws Exception {		
		//Initialize box configuration
		Reporter.log("Universal API initialized", true);

		this.application = application;
		
		String domainname = StringUtils.split(userAccount.getUsername(), "@")[1];
		int dotcount = StringUtils.countMatches(domainname, ".");
		if (dotcount >= 2) {
			//This is added for salesforce users appended with the sandbox name
			String valid = userAccount.getUsername().substring(0,userAccount.getUsername().lastIndexOf("."));
			this.tenant = StringUtils.split(valid, "@")[1].replace(".", "");
		} else {
			this.tenant = StringUtils.split(userAccount.getUsername(), "@")[1].replace(".", "");
			
		}
		Reporter.log("#######################################", true);
		Reporter.log("***** Tenant name:"+ this.tenant, true);
		Reporter.log("#######################################", true);
		
		
		if (application.equalsIgnoreCase("BOX" )) {

			if (userAccount.getUserType().equals("EXTERNAL")) {
				Reporter.log("Initializing the configuration for box external user...", true);
				this.initExternalUserBoxConfiguration(userAccount);
			} else {
				Reporter.log("Initializing the configuration for box...", true);
				initBoxConfiguration(userAccount);
			}
		}
		
		if (application.equalsIgnoreCase("DROPBOX" )) {
			initDropBoxConfiguration(userAccount);	
		}
		
		if (application.equalsIgnoreCase("ONEDRIVEBUSINESS" )) {
			Reporter.log("Initializing the configuration for onedrive business...", true);
			initOneDriveBusinessConfiguration(userAccount);	
		}
		
		if (application.equalsIgnoreCase("GDRIVE" )) {
			Reporter.log("Initializing the configuration for GDRIVE...", true);
			initGDriveConfiguration(userAccount);	
		}
		
		if (application.equalsIgnoreCase("SALESFORCE")) {
			Reporter.log("Initializing the configuration for Salesforce...", true);
			initSalesforceConfiguration(userAccount);
		}
		
		if (application.equalsIgnoreCase("SERVICENOW")) {
			Reporter.log("Initializing the configuration for ServiceNow...", true);
			initServiceNowConfiguration(userAccount);
		}
                
        if (application.equalsIgnoreCase("GMAIL")) {
			Reporter.log("Initializing the configuration for Gmail...", true);
			initGmailConfiguration(userAccount);
		}

	}
        
        public GoogleMailServices getGoogleMailServices() {
            if (this.googleMailServices == null) {
			Reporter.log("Google Mail object initialization is not done. Please call universal api and then get gmail object", true);
		} 
        return this.googleMailServices;
         }
	
	public Salesforce getSalesforce() {
		if (this.salesForce == null) {
			Reporter.log("Salesforce object initialization is not done. Please call universal api and then get salesforce object", true);
		} 
		return this.salesForce;	
	}
	
	public ServiceNow getServiceNow() {
		if (this.serviceNow == null) {
			Reporter.log("ServiceNow object initialization is not done. Please call universal api and then get servicenow object", true);
		} 
		return this.serviceNow;	
	}

	public GDrive getgDrive() {
            if (this.gDrive == null) {
			Reporter.log("GDrive object initialization is not done. Please call universal api and then get servicenow object", true);
		} 
		return this.gDrive;
	}

	public void setgDrive(GDrive gDrive) {
		this.gDrive = gDrive;
	}

        public DropBox getDropbox() {
		return this.dropbox;
	}
        
	public <T> T listRootDrive(String path) throws Exception{

		if (application.equalsIgnoreCase("BOX" )) {
			Reporter.log(application + ": Getting the list of Root Folder");
			return box.getUserInfo();

		} else if (application.equalsIgnoreCase("DROPBOX")) {
			Reporter.log(application + ": Getting the list of Root Folder");	
			//return dropbox.getDefaultDrive();
                        return null;

		} else if (application.equalsIgnoreCase("ONEDRIVEBUSINESS")) {
			Reporter.log(application + ": Getting the list of Root Folder");	
			return onedrivebusiness.getDefaultDrive();	
			
		} else if (application.equalsIgnoreCase("GDRIVE")) {
			Reporter.log(application + ": Getting the list of Root Folder");	
			return (T) gDrive.listFile();	

		} else {
			return (T) "No Such Application Listed";
		}
	}
	
	@SuppressWarnings("unchecked")
	public <T> T listAllItems(String listId, boolean fetchCompleteList) throws Exception{

		if (application.equalsIgnoreCase("ONEDRIVEBUSINESS")) {
			Reporter.log(application + ": Getting the list of Root Folder");	
			return (T) onedrivebusiness.listAllItems(listId, fetchCompleteList);	
			
		} else {
			return (T) "No Such Application Listed";
		}
	}
	
	@SuppressWarnings("unchecked")
	public void deleteListItem(String editLink) throws Exception{

		if (application.equalsIgnoreCase("ONEDRIVEBUSINESS")) {
			Reporter.log(application + ": Getting the list of Root Folder");	
			 onedrivebusiness.deleteListItem(editLink);	
			
		} else {
			Reporter.log("No Such Application Listed", true);
		}
	}
	

	public FileUploadResponse uploadFile(String folderId, String filename) throws Exception{
		if (application.equalsIgnoreCase("BOX" )) {
			Reporter.log(application + ": Started upload of the file " + filename, true);
			FileUploadResponse fupload =  box.uploadFile(folderId, filename);
			Reporter.log(fupload.getResponseMessage(), true);
			return  fupload;

		} else if (application.equalsIgnoreCase("DROPBOX")) {
			Reporter.log(application + ": Started upload of the file " + filename, true);
			return dropbox.uploadFile(folderId, filename);

		} else if (application.equalsIgnoreCase("ONEDRIVEBUSINESS")) {
			Reporter.log(application + ": Started upload of the file " + filename, true);
			return onedrivebusiness.uploadFile(folderId, filename);
			
		} else if (application.equalsIgnoreCase("GDRIVE")) {
			Reporter.log(application + ": Started upload of the file " + filename, true);
			return gDrive.uploadFile(folderId,filename);
			
		} else {
			return new FileUploadResponse();
		}
	}

	public synchronized FileUploadResponse uploadFile(String folderId, String filename, String destinationFilename) throws Exception{
		FileUploadResponse fupload = null;
		if (application.equalsIgnoreCase("BOX" )) {
			Reporter.log(application + ": Started upload of the file " + filename, true);
			fupload =  box.uploadFile(folderId, filename, destinationFilename);
			Reporter.log(fupload.getResponseMessage(), true);
			return  fupload;
			
		} else if (application.equalsIgnoreCase("GDRIVE" )) {
			Reporter.log(application + ": Started upload of the file " + filename, true);
			fupload =  gDrive.uploadFile(folderId, filename, destinationFilename);
			Reporter.log(fupload.getResponseMessage(), true);
			return  fupload;
			
		} else if (application.equalsIgnoreCase("DROPBOX" )) {
			Reporter.log(application + ": Started upload of the file " + filename, true);
			fupload =  dropbox.uploadFile(folderId,filename ,destinationFilename);
			Reporter.log(fupload.getResponseMessage(), true);
			return  fupload;
			
		}  else{
			Reporter.log("No Application Found...", true);
		}

		return  fupload;
	}


	@SuppressWarnings("unchecked")
	public <T> T uploadFile(String filename) throws Exception{

		if (application.equalsIgnoreCase("ONEDRIVEBUSINESS")) {
			Reporter.log(application + ": Started upload of the file " + filename, true);
			return onedrivebusiness.uploadFile(filename);
			
		} else if (application.equalsIgnoreCase("GDRIVE")) {
			Reporter.log(application + ": Started upload of the file " + filename, true);
			return gDrive.uploadFile(filename);
			
		} else {
			return (T) "No Such Application Listed";
		}
	}

	@SuppressWarnings("unchecked")
	public <T> T createFile(String filename) throws Exception{

		if (application.equalsIgnoreCase("ONEDRIVEBUSINESS")) {
			Reporter.log(application + ": Started upload of the file " + filename, true);
			return onedrivebusiness.createFile(filename);
			
		} else {
			return (T) "No Such Application Listed";
		}
	}



	@SuppressWarnings("unchecked")
	public <T> T uploadSimpleFile(String folderId, String filename, String destinationFilename) throws Exception{
		if (application.equalsIgnoreCase("ONEDRIVEBUSINESS")) {
			Reporter.log(application + ": Started upload of the file " + filename + " to "+ destinationFilename, true);
			return onedrivebusiness.uploadFile(folderId, filename, destinationFilename);

		} else if (application.equalsIgnoreCase("GDRIVE")) {
			Reporter.log(application + ": Started upload of the file " + filename + " to "+ destinationFilename, true);
			return (T) gDrive.uploadFile(folderId, filename, destinationFilename);
			
		} else {
			return (T) "No Such Application Listed";
		}
	}	


	@SuppressWarnings("unchecked")
	public <T> T downloadFile(String fileId, String filename) throws Exception {

		if (application.equalsIgnoreCase("BOX" )) {
			Reporter.log(application + ": Started download of the file " + filename, true);
			return box.downloadFile(fileId, filename);

		} else if (application.equalsIgnoreCase("DROPBOX")) {
			return dropbox.downloadFile(fileId, filename);

		} else if (application.equalsIgnoreCase("ONEDRIVEBUSINESS")) {
			return onedrivebusiness.downloadFile(fileId, filename);

		} else {
			return (T) "No Such Application Listed";
		}
	}


	@SuppressWarnings("unchecked")
	public <T> T previewFile(String fileId, String filename, String width, String height) throws Exception{

		if (application.equalsIgnoreCase("BOX" )) {
			Reporter.log(application + ": Started preview of the file " + filename, true);
			return box.previewFile(fileId, filename, width, height);
			
		} else {
			return (T) "No Such Application Listed";
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public <T> T previewFile(String fileId, String filename) throws Exception{

		if (application.equalsIgnoreCase("BOX" )) {
			Reporter.log(application + ": Started preview of the file " + filename, true);
			return box.previewFile(fileId, filename);
			
		} else {
			return (T) "No Such Application Listed";
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public String getEmbeddedLink(String fileId) throws Exception{

		if (application.equalsIgnoreCase("BOX" )) {
			Reporter.log(application + ": Getting embedded link ", true);
			return box.getEmbeddedLink(fileId);
			
		} else {
			return "No Such Application Listed";
		}
	}

	@SuppressWarnings("unchecked")
	public <T> T renameFile(String fileId, String newname) throws Exception{

		if (application.equalsIgnoreCase("BOX" )) {
			Reporter.log(application + ": Executing renaming the file to " + newname, true);
			return box.renameFile(fileId, newname);

		} else if (application.equalsIgnoreCase("DROPBOX" )) {
			Reporter.log(application + ": Executing renaming the file to " + newname, true);
			return dropbox.renameFile(fileId, newname);

		} else if (application.equalsIgnoreCase("GDRIVE" )) {
			Reporter.log(application + ": Executing renaming the file to " + newname, true);
			return (T) gDrive.renameFile(fileId,newname);

		} else {
			return (T) "No Such Application Listed";
		}
	}

	@SuppressWarnings("unchecked")
	public <T> T renameItem(ItemResource itemResource) throws Exception{
		if (application.equalsIgnoreCase("ONEDRIVEBUSINESS" )) {
			Reporter.log(application + ": Executing renaming the file to " + itemResource.getId());
			return (T) onedrivebusiness.renameItem(itemResource);
			
		} else {
			return (T) "No Such Application Listed";
		}
	}

	@SuppressWarnings("unchecked")
	public <T> T ShareItem(String itemId, ShareType shareType) throws Exception{
		if (application.equalsIgnoreCase("ONEDRIVEBUSINESS" )) {
			Reporter.log(application + ": Sharing the item with id " + itemId);
			return (T) onedrivebusiness.createSharedLink(itemId, shareType);
			
		} else {
			return (T) "No Such Application Listed";
		}
	}

	@SuppressWarnings("unchecked")
	public <T> T copyItem(String itemId, CopyObject copyObject, boolean asyncCopy) throws Exception{
		if (application.equalsIgnoreCase("ONEDRIVEBUSINESS" )) {
			Reporter.log(application + ": Copying the item " + itemId);
			return (T) onedrivebusiness.copyItem(itemId, copyObject, asyncCopy);
		} else {
			return (T) "No Such Application Listed";
		}
	}

	
	@SuppressWarnings("unchecked")
	public <T> T createSite(SiteInput siteInput) throws Exception{
		if (application.equalsIgnoreCase("ONEDRIVEBUSINESS" )) {
			Reporter.log(application + ": Creating the site ");
			return (T) onedrivebusiness.createSite(siteInput);
		} else {
			return (T) "No Such Application Listed";
		}
	}
	
	@SuppressWarnings("unchecked")
	public void updateSite(Parameters updateInput) throws Exception{
		if (application.equalsIgnoreCase("ONEDRIVEBUSINESS" )) {
			Reporter.log(application + ": Updating the site ");
			onedrivebusiness.updateSite(updateInput);
		} else {
			Reporter.log("No Such Application Listed", true);
		}
	}
	
	
	
	@SuppressWarnings("unchecked")
	public void deleteSite(String siteUrl) throws Exception{
		if (application.equalsIgnoreCase("ONEDRIVEBUSINESS" )) {
			Reporter.log(application + ": Deleting the site ");
			onedrivebusiness.deleteSite(siteUrl);
		} 
	}
	
	
	@SuppressWarnings("unchecked")
	public <T> T  uploadFileToSite(String filename, String foldername, String siteurl, String destinationFilename)   throws Exception{
		if (application.equalsIgnoreCase("ONEDRIVEBUSINESS" )) {
			Reporter.log(application + ": uploading the file to the site ");
			return (T) onedrivebusiness.uploadFileToSite(filename, foldername, siteurl, destinationFilename);
		} else {
			return (T) "No Such Application Listed";
		}
	}
	
	
	//Root site methods start
	
	@SuppressWarnings("unchecked")
	public <T> T  uploadFileToRootSite(String rootsiteUrl, String filename, String foldername, String destinationFilename)   throws Exception{
		if (application.equalsIgnoreCase("ONEDRIVEBUSINESS" )) {
			Reporter.log(application + ": uploading the file to the site ");
			return (T) onedrivebusiness.uploadFileToRootSite(rootsiteUrl, filename, foldername, destinationFilename);
		} else {
			return (T) "No Such Application Listed";
		}
	}
	
	@SuppressWarnings("unchecked")
	public <T> T  listAllFilesForRootSiteFolder(String rootsiteUrl, String foldername)   throws Exception{
		if (application.equalsIgnoreCase("ONEDRIVEBUSINESS" )) {
			Reporter.log(application + ": List all files in the root site folder ");
			return (T) onedrivebusiness.listAllFilesForRootSiteFolder(rootsiteUrl, foldername);
		} else {
			return (T) "No Such Application Listed";
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public <T> T  getRootSiteDocumentListItems(String rootsiteUrl)   throws Exception{
		if (application.equalsIgnoreCase("ONEDRIVEBUSINESS" )) {
			Reporter.log(application + ": List all items in the document list of root site ");
			return (T) onedrivebusiness.getRootSiteDocumentListItems(rootsiteUrl);
		} else {
			return (T) "No Such Application Listed";
		}
	}
	
	@SuppressWarnings("unchecked")
	public <T> T  getRootSiteListGuid(String rootsiteUrl, String listname)   throws Exception{
		if (application.equalsIgnoreCase("ONEDRIVEBUSINESS" )) {
			Reporter.log(application + ": Get guid of root site name ");
			return (T) onedrivebusiness.getRootSiteListGuid(rootsiteUrl, listname);
		} else {
			return (T) "No Such Application Listed";
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public <T> T getRootSiteListItemAllFieldsByUrl(String rootsiteurl, String listname, String filename) throws Exception{

		if (application.equalsIgnoreCase("ONEDRIVEBUSINESS" )) {
			Reporter.log(application + ": Retrieving list item all fields by listname and fileurl" , true);
			return (T) onedrivebusiness.getRootSiteItemAllFieldsByUrl(rootsiteurl, listname, filename);

		} else {
			return (T) "No Such Application Listed";
		}
	}
	
	
	
	@SuppressWarnings("unchecked")
	public void addRootSiteRoleAssignmentForListItem(String rootsiteurl, String itemlink, String principalId, String roleId) throws Exception{

		if (application.equalsIgnoreCase("ONEDRIVEBUSINESS" )) {
			Reporter.log(application + ": Add role assignment for item " , true);
			onedrivebusiness.addRootSiteRoleAssignmentForListItem(rootsiteurl, itemlink, principalId, roleId);
		} 
	}
	

	@SuppressWarnings("unchecked")
	public void breakRoleInheritanceForRootSiteListItem(String rootsiteurl, String itemlink, boolean copyRoleAssignments, boolean clearSubscopes) throws Exception{

		if (application.equalsIgnoreCase("ONEDRIVEBUSINESS" )) {
			Reporter.log(application + ": Break role assignment for item " , true);
			onedrivebusiness.breakRoleInheritanceForRootSiteListItem(rootsiteurl, itemlink, copyRoleAssignments, clearSubscopes);
		} 
	}

	
	
	@SuppressWarnings("unchecked")
	public void removeRootSiteRoleAssignmentForListItem(String rootsiteurl, String itemlink, String principalId, String roleId) throws Exception{

		if (application.equalsIgnoreCase("ONEDRIVEBUSINESS" )) {
			Reporter.log(application + ": Remove role assignment for item " , true);
			onedrivebusiness.removeRootSiteRoleAssignmentForListItem(rootsiteurl, itemlink, principalId, roleId);
		} 
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getRootSiteUserList(String rootsiteurl) throws Exception{

		if (application.equalsIgnoreCase("ONEDRIVEBUSINESS" )) {
			Reporter.log(application + ": Retrieving list of users in rootsite" , true);
			return (T) onedrivebusiness.getRootSiteUserList(rootsiteurl);

		} else {
			return (T) "No Such Application Listed";
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public <T> T getRootSiteRolesAssignments(String rootsiteurl) throws Exception{

		if (application.equalsIgnoreCase("ONEDRIVEBUSINESS" )) {
			Reporter.log(application + ": Retrieving list of role assignments of rootsite" , true);
			return (T) onedrivebusiness.getRootSiteRolesAssignments(rootsiteurl);

		} else {
			return (T) "No Such Application Listed";
		}
	}

	@SuppressWarnings("unchecked")
	public <T> T getRootSiteRolesDefinitions(String rootsiteurl) throws Exception{

		if (application.equalsIgnoreCase("ONEDRIVEBUSINESS" )) {
			Reporter.log(application + ": Retrieving list of role definitions of rootsite" , true);
			return (T) onedrivebusiness.getRootSiteRolesDefinitions(rootsiteurl);

		} else {
			return (T) "No Such Application Listed";
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public <T> T getRootSiteFileSharedLink(String rootsiteurl, Object object) throws Exception {
		if (application.equalsIgnoreCase("ONEDRIVEBUSINESS" )) {
			Reporter.log(application + ": Creating shared link " , true);
			return (T) onedrivebusiness.getRootSiteFileSharedLink(rootsiteurl, (SharingUserRoleAssignment) object);

		} else {
			return (T) "No Such Application Listed";
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public <T> T getRootSiteFilePreAuthorizedAccessUrl(String rootsiteurl, String filerelativeurl, int expirytime) throws Exception {
		if (application.equalsIgnoreCase("ONEDRIVEBUSINESS" )) {
			Reporter.log(application + ": Creating preauthorized access url " , true);
			return (T) onedrivebusiness.getRootSiteFilePreAuthorizedAccessUrl(rootsiteurl, filerelativeurl, expirytime);

		} else {
			return (T) "No Such Application Listed";
		}
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getRoleAssignmentForRootSiteItem(String rootsiteurl, String itemlink) throws Exception {
		if (application.equalsIgnoreCase("ONEDRIVEBUSINESS" )) {
			Reporter.log(application + ": Get root site item role assignments " , true);
			return (T) onedrivebusiness.getRoleAssignmentForRootSiteItem(rootsiteurl, itemlink);

		} else {
			return (T) "No Such Application Listed";
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public void disableRootSiteItemSharing(String rootsiteurl, String itemlink) throws Exception {
		if (application.equalsIgnoreCase("ONEDRIVEBUSINESS" )) {
			Reporter.log(application + ": Disable root site item sharing " , true);
			onedrivebusiness.disableRootSiteItemSharing(rootsiteurl, itemlink);

		} else {
			Reporter.log("No Such Application Listed", true);
		}
	}

	
	@SuppressWarnings("unchecked")
	public <T> T recycleRootSiteFileItem(String rootsiteurl,  String relativeurl, boolean isFolder) throws Exception {
		if (application.equalsIgnoreCase("ONEDRIVEBUSINESS" )) {
			Reporter.log(application + ": Recycling Root site list item " , true);
			return (T) onedrivebusiness.recycleRootSiteFileItem(rootsiteurl, relativeurl, isFolder);

		} else {
			Reporter.log("No Such Application Listed", true);
			return null;
		}
	}


	public void restoreRootSiteRecyleBinItem(String rootsiteurl, String fileId) throws Exception {
		if (application.equalsIgnoreCase("ONEDRIVEBUSINESS" )) {
			Reporter.log(application + ": Restoring recycle bin item " , true);
			onedrivebusiness.restoreRootSiteRecyleBinItem(rootsiteurl, fileId);

		} else {
			Reporter.log("No Such Application Listed", true);
		}
	}
	
	
	public <T> T createRootSiteFolder(String rootsiteurl, SiteFolderInput sitefolderinput) throws Exception {
		if (application.equalsIgnoreCase("ONEDRIVEBUSINESS" )) {
			Reporter.log(application + ": Create Folder " , true);
			return onedrivebusiness.createRootSiteFolder(rootsiteurl, sitefolderinput);
		} else {
			System.out.println("No Such Application Listed");
			return null;
		}
	}
	
	public void renameRootSiteFolder(String rootsiteurl, String relativeurl, SiteFolderUpdateInput updateinput) throws Exception {
		if (application.equalsIgnoreCase("ONEDRIVEBUSINESS" )) {
			Reporter.log(application + ": Update Folder " , true);
			onedrivebusiness.renameRootSiteFolder(rootsiteurl, relativeurl, updateinput);
		} else {
			System.out.println("No Such Application Listed");
		}
	}
	
	public void deleteRootSiteFolder(String rootsiteurl, String relativeurl) throws Exception {
		if (application.equalsIgnoreCase("ONEDRIVEBUSINESS" )) {
			Reporter.log(application + ": Delete Folder " , true);
			onedrivebusiness.deleteRootSiteFolder(rootsiteurl, relativeurl);
		} else {
			System.out.println("No Such Application Listed");
		}
	}
	
	public <T> T getRootSiteFolderListItemAllFields(String rootsiteurl, String relativeurl) throws Exception {
		if (application.equalsIgnoreCase("ONEDRIVEBUSINESS" )) {
			Reporter.log(application + ": Get Folder item fields " , true);
			return onedrivebusiness.getRootSiteFolderListItemAllFields(rootsiteurl, relativeurl);
		} else {
			System.out.println("No Such Application Listed");
			return null;
		}
	}
	
	//Root site methods end
	
	//Subsite methods start
	//Root site methods start
	
		@SuppressWarnings("unchecked")
		public <T> T  uploadFileToSubSite(String subsiteUrl, String filename, String foldername, String destinationFilename)   throws Exception{
			if (application.equalsIgnoreCase("ONEDRIVEBUSINESS" )) {
				Reporter.log(application + ": uploading the file to the sub site ");
				return (T) onedrivebusiness.uploadFileToSubSite(subsiteUrl, filename, foldername, destinationFilename);
			} else {
				return (T) "No Such Application Listed";
			}
		}
		
		@SuppressWarnings("unchecked")
		public <T> T  listAllFilesForSubSiteFolder(String subsiteUrl, String foldername)   throws Exception{
			if (application.equalsIgnoreCase("ONEDRIVEBUSINESS" )) {
				Reporter.log(application + ": List all files in the sub site folder ");
				return (T) onedrivebusiness.listAllFilesForSubSiteFolder(subsiteUrl, foldername);
			} else {
				return (T) "No Such Application Listed";
			}
		}
		
		
		@SuppressWarnings("unchecked")
		public <T> T  getSubSiteDocumentListItems(String subsiteUrl)   throws Exception{
			if (application.equalsIgnoreCase("ONEDRIVEBUSINESS" )) {
				Reporter.log(application + ": List all items in the document list of sub site ");
				return (T) onedrivebusiness.getSubSiteDocumentListItems(subsiteUrl);
			} else {
				return (T) "No Such Application Listed";
			}
		}
		
		@SuppressWarnings("unchecked")
		public <T> T  getSubSiteListGuid(String subsiteUrl, String listname)   throws Exception{
			if (application.equalsIgnoreCase("ONEDRIVEBUSINESS" )) {
				Reporter.log(application + ": Get guid of root site name ");
				return (T) onedrivebusiness.getSubSiteListGuid(subsiteUrl, listname);
			} else {
				return (T) "No Such Application Listed";
			}
		}
		
		
		@SuppressWarnings("unchecked")
		public <T> T getSubSiteListItemAllFieldsByUrl(String subsiteUrl, String listname, String filename) throws Exception{

			if (application.equalsIgnoreCase("ONEDRIVEBUSINESS" )) {
				Reporter.log(application + ": Retrieving list item all fields by listname and fileurl" , true);
				return (T) onedrivebusiness.getSubSiteItemAllFieldsByUrl(subsiteUrl, listname, filename);

			} else {
				return (T) "No Such Application Listed";
			}
		}
		
		
		
		@SuppressWarnings("unchecked")
		public void addSubSiteRoleAssignmentForListItem(String subsiteUrl, String itemlink, String principalId, String roleId) throws Exception{

			if (application.equalsIgnoreCase("ONEDRIVEBUSINESS" )) {
				Reporter.log(application + ": Add role assignment for item " , true);
				onedrivebusiness.addSubSiteRoleAssignmentForListItem(subsiteUrl, itemlink, principalId, roleId);
			} 
		}
		

		@SuppressWarnings("unchecked")
		public void breakRoleInheritanceForSubSiteListItem(String subsiteUrl, String itemlink, boolean copyRoleAssignments, boolean clearSubscopes) throws Exception{

			if (application.equalsIgnoreCase("ONEDRIVEBUSINESS" )) {
				Reporter.log(application + ": Break role assignment for item " , true);
				onedrivebusiness.breakRoleInheritanceForSubSiteListItem(subsiteUrl, itemlink, copyRoleAssignments, clearSubscopes);
			} 
		}

		
		
		@SuppressWarnings("unchecked")
		public void removeSubSiteRoleAssignmentForListItem(String subsiteUrl, String itemlink, String principalId, String roleId) throws Exception{

			if (application.equalsIgnoreCase("ONEDRIVEBUSINESS" )) {
				Reporter.log(application + ": Remove role assignment for item " , true);
				onedrivebusiness.removeSubSiteRoleAssignmentForListItem(subsiteUrl, itemlink, principalId, roleId);
			} 
		}
		
		@SuppressWarnings("unchecked")
		public <T> T getSubSiteUserList(String subsiteUrl) throws Exception{

			if (application.equalsIgnoreCase("ONEDRIVEBUSINESS" )) {
				Reporter.log(application + ": Retrieving list of users in sub" , true);
				return (T) onedrivebusiness.getSubSiteUserList(subsiteUrl);

			} else {
				return (T) "No Such Application Listed";
			}
		}
		
		
		@SuppressWarnings("unchecked")
		public <T> T getSubSiteRolesAssignments(String subsiteUrl) throws Exception{

			if (application.equalsIgnoreCase("ONEDRIVEBUSINESS" )) {
				Reporter.log(application + ": Retrieving list of role assignments of subsite" , true);
				return (T) onedrivebusiness.getSubSiteRolesAssignments(subsiteUrl);

			} else {
				return (T) "No Such Application Listed";
			}
		}

		@SuppressWarnings("unchecked")
		public <T> T getSubSiteRolesDefinitions(String subsiteUrl) throws Exception{

			if (application.equalsIgnoreCase("ONEDRIVEBUSINESS" )) {
				Reporter.log(application + ": Retrieving list of role definitions of subsite" , true);
				return (T) onedrivebusiness.getSubSiteRolesDefinitions(subsiteUrl);

			} else {
				return (T) "No Such Application Listed";
			}
		}
		
		
		@SuppressWarnings("unchecked")
		public <T> T getSubSiteFileSharedLink(String subsiteUrl, Object object) throws Exception {
			if (application.equalsIgnoreCase("ONEDRIVEBUSINESS" )) {
				Reporter.log(application + ": Creating shared link " , true);
				return (T) onedrivebusiness.getSubSiteFileSharedLink(subsiteUrl, (SharingUserRoleAssignment) object);

			} else {
				return (T) "No Such Application Listed";
			}
		}
		
		
		@SuppressWarnings("unchecked")
		public <T> T getSubSiteFilePreAuthorizedAccessUrl(String subsiteUrl, String filerelativeurl, int expirytime) throws Exception {
			if (application.equalsIgnoreCase("ONEDRIVEBUSINESS" )) {
				Reporter.log(application + ": Creating preauthorized access url " , true);
				return (T) onedrivebusiness.getSubSiteFilePreAuthorizedAccessUrl(subsiteUrl, filerelativeurl, expirytime);

			} else {
				return (T) "No Such Application Listed";
			}
		}
		
		@SuppressWarnings("unchecked")
		public <T> T getRoleAssignmentForSubSiteItem(String subsiteUrl, String itemlink) throws Exception {
			if (application.equalsIgnoreCase("ONEDRIVEBUSINESS" )) {
				Reporter.log(application + ": Get sub site item role assignments " , true);
				return (T) onedrivebusiness.getRoleAssignmentForSubSiteItem(subsiteUrl, itemlink);

			} else {
				return (T) "No Such Application Listed";
			}
		}
		
		
		@SuppressWarnings("unchecked")
		public void disableSubSiteItemSharing(String subsiteUrl, String itemlink) throws Exception {
			if (application.equalsIgnoreCase("ONEDRIVEBUSINESS" )) {
				Reporter.log(application + ": Disable sub site item sharing " , true);
				onedrivebusiness.disableSubSiteItemSharing(subsiteUrl, itemlink);

			} else {
				Reporter.log("No Such Application Listed", true);
			}
		}

		
		@SuppressWarnings("unchecked")
		public <T> T recycleSubSiteFileItem(String subsiteUrl,  String relativeurl, boolean isFolder) throws Exception {
			if (application.equalsIgnoreCase("ONEDRIVEBUSINESS" )) {
				Reporter.log(application + ": Recycling sub site list item " , true);
				return (T) onedrivebusiness.recycleSubSiteFileItem(subsiteUrl, relativeurl, isFolder);

			} else {
				Reporter.log("No Such Application Listed", true);
				return null;
			}
		}


		public void restoreSubSiteRecyleBinItem(String subsiteUrl, String fileId) throws Exception {
			if (application.equalsIgnoreCase("ONEDRIVEBUSINESS" )) {
				Reporter.log(application + ": Restoring recycle bin item " , true);
				onedrivebusiness.restoreSubSiteRecyleBinItem(subsiteUrl, fileId);

			} else {
				Reporter.log("No Such Application Listed", true);
			}
		}
		
		
		public <T> T createSubSiteFolder(String subsiteUrl, SiteFolderInput sitefolderinput) throws Exception {
			if (application.equalsIgnoreCase("ONEDRIVEBUSINESS" )) {
				Reporter.log(application + ": Create Folder " , true);
				return onedrivebusiness.createSubSiteFolder(subsiteUrl, sitefolderinput);
			} else {
				System.out.println("No Such Application Listed");
				return null;
			}
		}
		
		public void renameSubSiteFolder(String subsiteUrl, String relativeurl, SiteFolderUpdateInput updateinput) throws Exception {
			if (application.equalsIgnoreCase("ONEDRIVEBUSINESS" )) {
				Reporter.log(application + ": Update Folder " , true);
				onedrivebusiness.renameSubSiteFolder(subsiteUrl, relativeurl, updateinput);
			} else {
				System.out.println("No Such Application Listed");
			}
		}
		
		public void deleteSubSiteFolder(String subsiteUrl, String relativeurl) throws Exception {
			if (application.equalsIgnoreCase("ONEDRIVEBUSINESS" )) {
				Reporter.log(application + ": Delete Folder " , true);
				onedrivebusiness.deleteSubSiteFolder(subsiteUrl, relativeurl);
			} else {
				System.out.println("No Such Application Listed");
			}
		}
		
		public <T> T getSubSiteFolderListItemAllFields(String subsiteUrl, String relativeurl) throws Exception {
			if (application.equalsIgnoreCase("ONEDRIVEBUSINESS" )) {
				Reporter.log(application + ": Get Folder item fields " , true);
				return onedrivebusiness.getSubSiteFolderListItemAllFields(subsiteUrl, relativeurl);
			} else {
				System.out.println("No Such Application Listed");
				return null;
			}
		}	
	
	
	//Subsite methods end
	
	
	
	
	@SuppressWarnings("unchecked")
	public <T> T getFileInfo(String fileId) throws Exception{

		if (application.equalsIgnoreCase("BOX" )) {
			Reporter.log(application + ": Retrieving the file information " , true);
			return box.getFileInfo(fileId);

		} else if (application.equalsIgnoreCase("DROPBOX" )) {
			Reporter.log(application + ": Retrieving the file information " , true);
			return dropbox.getFileInfo(fileId);

		} else if (application.equalsIgnoreCase("ONEDRIVEBUSINESS" )) {
			Reporter.log(application + ": Retrieving the file information " , true);
			return onedrivebusiness.getFileInfo(fileId);

		} else {
			return (T) "No Such Application Listed";
		}
	}

	@SuppressWarnings("unchecked")
	public <T> T updateFile(String fileId, String filename, String etag, String destinationFile) throws Exception{

		if (application.equalsIgnoreCase("BOX" )) {
			Reporter.log(application + ": Updating the file contents " , true);
			return box.updateFile(fileId, filename, etag, destinationFile);

		} else {
			return (T) "No Such Application Listed";
		}
	}

	@SuppressWarnings("unchecked")
	public <T> T copyFile(String fileId, String destinationFolderId) throws Exception{

		if (application.equalsIgnoreCase("BOX" )) {
			Reporter.log(application + ": Copy the file to another folder " , true);
			return box.copyFile(fileId, destinationFolderId);
		}
		else if (application.equalsIgnoreCase("DROPBOX" )) {
			Reporter.log(application + ": Copy the file to another folder " , true);
			return dropbox.copyFile(fileId, destinationFolderId);
		}
		else if (application.equalsIgnoreCase("GDRIVE" )) {
			Reporter.log(application + ": Copy the file to another folder " , true);
			return gDrive.insertFileIntoFolder(fileId, destinationFolderId);
		}
		else {
			return (T) "No Such Application Listed";
		}
	}


	@SuppressWarnings("unchecked")
	public void deleteFile(String fileId, String etag) throws Exception{

		if (application.equalsIgnoreCase("BOX" )) {
			Reporter.log(application + ": Moving the file to trash " , true);
			box.deleteFile(fileId, etag);
			
		} else if (application.equalsIgnoreCase("DROPBOX" )) {
			Reporter.log(application + ": Delete the file " , true);
			dropbox.deleteFileOrFolder(fileId);
			
		} else if (application.equalsIgnoreCase("ONEDRIVEBUSINESS" )) {
			Reporter.log(application + ": Delete the file " , true);
			onedrivebusiness.deleteFile(fileId, etag);
			
		} else if (application.equalsIgnoreCase("GDRIVE" )) {
			Reporter.log(application + ": Delete the file " , true);
			gDrive.deleteFile(fileId);
		} else {
			System.out.println("No Such Application Listed");
		}
	}

	@SuppressWarnings("unchecked")
	public void purgeFile(String fileId) throws Exception{

		if (application.equalsIgnoreCase("BOX" )) {
			Reporter.log(application + ": Purging the file... " , true);
			box.purgeFile(fileId);

		} else {
			System.out.println("No Such Application Listed");
		}
	}

	@SuppressWarnings("unchecked")
	public <T> T restoreFileFromTrash(String fileId, String newname) throws Exception{

		if (application.equalsIgnoreCase("BOX" )) {
			Reporter.log(application + ": Restoring the file from trash... " , true);
			return box.restoreFileFromTrash(fileId, newname);
			
		}  else if (application.equalsIgnoreCase("GDRIVE" )) {
			Reporter.log(application + ": Restoring the file from trash... " , true);
			return (T) gDrive.restoreFile(fileId);
			
		} else {
			System.out.println("No Such Application Listed");
			return null;
		}
	}


	@SuppressWarnings("unchecked")
	public <T> T getTrashedFile(String fileId) throws Exception{

		if (application.equalsIgnoreCase("BOX" )) {
			Reporter.log(application + ": Getting the trashed file " , true);
			return box.getTrashedFile(fileId);

		} else {
			return (T) "No Such Application Listed";
		}
	}


	@SuppressWarnings("unchecked")
	public <T> T updateFileInfo(String fileId, String updateJson) throws Exception{

		if (application.equalsIgnoreCase("BOX" )) {
			Reporter.log(application + ": Updating the file info" , true);
			return box.updateFileInfo(fileId, updateJson);

		} else {
			return (T) "No Such Application Listed";
		}
	}


	@SuppressWarnings("unchecked")
	public void lockFile(String fileId, boolean preventDownload) throws Exception{

		if (application.equalsIgnoreCase("BOX" )) {
			Reporter.log(application + ": Locking the file " , true);
			box.lockFile(fileId, preventDownload);

		} else {
			System.out.println("No Such Application Listed");
		}
	}


	@SuppressWarnings("unchecked")
	public void unlockFile(String fileId) throws Exception{

		if (application.equalsIgnoreCase("BOX" )) {
			Reporter.log(application + ": Unlocking the file " , true);
			box.unlockFile(fileId);

		} else {
			System.out.println("No Such Application Listed");
		}
	}


	public <T> T createSharedLink(String fileId, SharedLink sharedLink) throws Exception {
		if (application.equalsIgnoreCase("BOX" )) {
			Reporter.log(application + ": Creating shared link " , true);
			return box.createSharedLink(fileId, sharedLink);

		} else {
			System.out.println("No Such Application Listed");
		}
		return null;
	}

	public <T> T createDefaultSharedLink(String fileId) throws Exception {
		if (application.equalsIgnoreCase("BOX" )) {
			Reporter.log(application + ": Creating default shared link " , true);
			return box.createDefaultSharedLink(fileId);

		} else {
			System.out.println("No Such Application Listed");
		}
		return null;
	}

	public <T> T disableSharedLink(String fileId) throws Exception {
		if (application.equalsIgnoreCase("BOX" )) {
			Reporter.log(application + ": Disabling the shared link " , true);
			return box.disableSharedLink(fileId);
		} else {
			System.out.println("No Such Application Listed");
		}
		return null;
	}


	//Folder related Api's
	public <T> T getFolderInfo(String folderId) throws Exception {
		if (application.equalsIgnoreCase("BOX" )) {
			Reporter.log(application + ": Get folder info " , true);
			return box.getFolderInfo(folderId);
		}
		if (application.equalsIgnoreCase("DROPBOX" )) {
			Reporter.log(application + ": Get folder info " , true);
			return (T) dropbox.getFolderInfo(folderId);
		}
		else {
			System.out.println("No Such Application Listed");
			return null;
		}
	}

	public <T> T getFoldersItems(String folderId, int limit, int offset) throws Exception {
		if (application.equalsIgnoreCase("BOX" )) {
			Reporter.log(application + ": Get folders items " , true);
			return box.getFoldersItems(folderId, limit, offset);
		}
		else if (application.equalsIgnoreCase("DROPBOX" )) {
			Reporter.log(application + ": Get folders items " , true);
			return dropbox.getFoldersItems(folderId);
		}
		else {
			System.out.println("No Such Application Listed");
			return null;
		}
	}

	public <T> T createFolder(String folderId, String parentFolderId) throws Exception {
		if (application.equalsIgnoreCase("BOX" )) {
			Reporter.log(application + ": Create Folder " , true);
			return box.createFolder(folderId, parentFolderId);
		} else if (application.equalsIgnoreCase("DROPBOX" )) {
			Reporter.log(application + ": Create Folder " , true);
			return dropbox.createFolder(folderId,parentFolderId);
		} else if (application.equalsIgnoreCase("ONEDRIVEBUSINESS" )) {
			Reporter.log(application + ": Create Folder " , true);
			return onedrivebusiness.createFolder(folderId, parentFolderId);
		} else {
			System.out.println("No Such Application Listed");
			return null;
		}
	}

	public <T> T createFolder(String foldername) throws Exception {
		if (application.equalsIgnoreCase("BOX" )) {
			Reporter.log(application + ": Create Folder " , true);
			return  box.createFolder(foldername);
			
		} else if (application.equalsIgnoreCase("DROPBOX" )) {
			Reporter.log(application + ": Create Folder " , true);
			return dropbox.createFolder(foldername);
			
		} else if (application.equalsIgnoreCase("ONEDRIVEBUSINESS" )) {
			Reporter.log(application + ": Create Folder " , true);
			return onedrivebusiness.createFolder(foldername, "/");
			
		} else if (application.equalsIgnoreCase("GDRIVE" )) {
			Reporter.log(application + ": Create Folder " , true);
			return gDrive.createFolder(foldername);
			
		} else {
			System.out.println("No Such Application Listed");
			return null;
		}
	}

	public <T> T createFolderV2(String foldername) throws Exception {
		if (application.equalsIgnoreCase("ONEDRIVEBUSINESS" )) {
			Reporter.log(application + ": Create Folder " , true);
			return onedrivebusiness.createFolderV2(foldername);
			
		} else {
			System.out.println("No Such Application Listed");
			return null;
		}
	}

	public <T> T createFolderV2(String foldername, String parentId) throws Exception {
		if (application.equalsIgnoreCase("ONEDRIVEBUSINESS" )) {
			Reporter.log(application + ": Create Folder " , true);
			return onedrivebusiness.createFolderV2(foldername, parentId);
		} else {
			System.out.println("No Such Application Listed");
			return null;
		}
	}


	public void deleteFolderV2(String itemId, String etag) throws Exception {
		if (application.equalsIgnoreCase("ONEDRIVEBUSINESS" )) {
			Reporter.log(application + ": Delete Folder " , true);
			onedrivebusiness.deleteFolderV2(itemId, etag);
		} else {
			System.out.println("No Such Application Listed");
		}
	}

	public void downloadFileV2(String itemId, String filename) throws Exception {
		if (application.equalsIgnoreCase("ONEDRIVEBUSINESS" )) {
			Reporter.log(application + ": Download file " , true);
			onedrivebusiness.downloadFileV2(itemId, filename);
		} else {
			System.out.println("No Such Application Listed");
		}
	}


	public <T> T updateFolder(String folderId, String updatedName) throws Exception {
		if (application.equalsIgnoreCase("BOX")) {
			Reporter.log(application + ": Update Folder " , true);
			return box.updateFolder(folderId, updatedName);
		} else {
			System.out.println("No Such Application Listed");
			return null;
		}
	}

	public void deleteFolder(String folderId, boolean recursive, String etag) throws Exception {
		if (application.equalsIgnoreCase("BOX" )) {
			Reporter.log(application + ": Delete Folder " , true);
			box.deleteFolder(folderId, recursive, etag);
			
		} else if (application.equalsIgnoreCase("DROPBOX" )) {
			Reporter.log(application + ": Delete Folder " , true);
			dropbox.deleteFileOrFolder(folderId);
			
		} else if (application.equalsIgnoreCase("ONEDRIVEBUSINESS" )) {
			Reporter.log(application + ": Delete Folder " , true);
			onedrivebusiness.deleteFolder(folderId, recursive, etag);
			
		} else if (application.equalsIgnoreCase("GDRIVE")) {
			Reporter.log(application + ": Delete Folder " , true);
			gDrive.deleteFile(folderId); // It will also delete folder...TESTED...
			
		} else {
			System.out.println("No Such Application Listed");
		}
	}

	public <T> T copyFolder(String folderId, String destinationFolderId) throws Exception {
		if (application.equalsIgnoreCase("BOX" )) {
			Reporter.log(application + ": Copy Folder " , true);
			return box.copyFolder(folderId, destinationFolderId);
		} else if (application.equalsIgnoreCase("BOX" )) {
			Reporter.log(application + ": Copy Folder " , true);
			return dropbox.copyFolder(folderId, destinationFolderId);
		} else {
			System.out.println("No Such Application Listed");
			return null;
		}
	}

	public <T> T  createSharedLinkForFolder(String folderId) throws Exception {
		if (application.equalsIgnoreCase("BOX" )) {
			Reporter.log(application + ": Shared link of Folder " , true);
			return box.createSharedLinkForFolder(folderId);
			
		} else if (application.equalsIgnoreCase("DROPBOX" )) {
			Reporter.log(application + ": Shared link of Folder " , true);
			return dropbox.createSharedLinkForFolderORFile(folderId);
			
		} else {
			System.out.println("No Such Application Listed");
			return null;
		}
	}

	public <T> T  createSharedLinkForFolder(String folderId, SharedLink sharedLink) throws Exception {
		if (application.equalsIgnoreCase("BOX" )) {
			Reporter.log(application + ": Shared link of Folder " , true);
			return box.createSharedLinkForFolder(folderId, sharedLink);
		} else {
			System.out.println("No Such Application Listed");
			return null;
		}
	}

	public <T> T  disableSharedLinkForFolder(String folderId) throws Exception {
		if (application.equalsIgnoreCase("BOX" )) {
			Reporter.log(application + ": Disable Shared link of Folder " , true);
			return box.disableSharedLinkForFolder(folderId);
		} else {
			System.out.println("No Such Application Listed");
			return null;
		}
	}

	public <T> T  getFolderCollaborations(String folderId) throws Exception {
		if (application.equalsIgnoreCase("BOX" )) {
			Reporter.log(application + ": Folder collaborations " , true);
			return box.getFolderCollaborations(folderId);
		} else {
			System.out.println("No Such Application Listed");
			return null;
		}
	}

	public <T> T getFolderTrashedItems(String fields, int limit, int offset) throws Exception {
		if (application.equalsIgnoreCase("BOX" )) {
			Reporter.log(application + ": Folder trashed items " , true);
			return box.getFolderTrashedItems(fields, limit, offset);
		} else {
			System.out.println("No Such Application Listed");
			return null;
		}

	}

	public <T> T  getTrashedFolder(String folderId) throws Exception {
		if (application.equalsIgnoreCase("BOX" )) {
			Reporter.log(application + ": Get trashed folder " , true);
			return box.getTrashedFolder(folderId);
		} else {
			System.out.println("No Such Application Listed");
			return null;
		}
	}


	public void purgeFolder(String folderId) throws Exception {
		if (application.equalsIgnoreCase("BOX" )) {
			Reporter.log(application + ": Purge folder " , true);
			box.getTrashedFolder(folderId);
		} else {
			System.out.println("No Such Application Listed");
		}
	}


	public void restoreFolder(String folderId, String restoreFolderName) throws Exception {
		if (application.equalsIgnoreCase("BOX" )) {
			Reporter.log(application + ": Restore folder " , true);
			box.restoreFolder(folderId, restoreFolderName);
		} else {
			System.out.println("No Such Application Listed");
		}
	}


	public <T> T moveFolder(String sourceFolderId, String destinationFolderId) throws Exception {
		if (application.equalsIgnoreCase("BOX")) {
			Reporter.log(application + ": Move Folder " , true);
			return box.moveFolder(sourceFolderId, destinationFolderId);
			
		} else if (application.equalsIgnoreCase("DROPBOX")) {
			Reporter.log(application + ": Move Folder " , true);
			return dropbox.moveFolder(sourceFolderId, destinationFolderId);
			
		} else {
			System.out.println("No Such Application Listed");
			return null;
		}
	}

	public <T> T moveFile(String sourceFilename, String destinationFileName) throws Exception {
		if (application.equalsIgnoreCase("DROPBOX")) {
			Reporter.log(application + ": Move File " , true);
			return dropbox.moveFile(sourceFilename, destinationFileName);
		}
		else {
			System.out.println("No Such Application Listed");
			return null;
		}
	}

	//User related operations

	public <T> T createUser(Object object) throws Exception {
		if (application.equalsIgnoreCase("BOX" )) {
			Reporter.log(application + ": Create user " , true);
			return box.createUser((BoxUserInfo)object);
			
		} else if (application.equalsIgnoreCase("ONEDRIVEBUSINESS" )) {
			Reporter.log(application + ": Create user " , true);
			return onedrivebusiness.createUser((UserInput)object);
			
		} else	{
			System.out.println("No Such Application Listed");
			return null;
		}
	}

	
	public <T> T inviteUser(String enterpriseId, String emailId) throws Exception {
		if (application.equalsIgnoreCase("BOX" )) {
			Reporter.log(application + ": Invite user " , true);
			return box.inviteUser(enterpriseId, emailId);
			
		} else	{
			System.out.println("No Such Application Listed");
			return null;
		}
	}
	

	public <T> T updateUser(Object object) throws Exception {
		if (application.equalsIgnoreCase("BOX" )) {
			Reporter.log(application + ": Update user " , true);
			return box.updateUser((BoxUserInfo)(object));
			
		} else if (application.equalsIgnoreCase("ONEDRIVEBUSINESS" )) {
			Reporter.log(application + ": Create user " , true);
			return onedrivebusiness.updateUser((Value)object);
			
		} else {
			System.out.println("No Such Application Listed");
			return null;
		}
	}


	public void deleteUser(Object object) throws Exception {
		if (application.equalsIgnoreCase("BOX" )) {
			Reporter.log(application + ": Delete user " , true);
			box.deleteUser((BoxUserInfo)object);
			
		} else if (application.equalsIgnoreCase("ONEDRIVEBUSINESS" )) {
			Reporter.log(application + ": Create user " , true);
			onedrivebusiness.deleteUser((Value)object);
			
		} else {
			System.out.println("No Such Application Listed");
		}
	}


	public <T> T listUser() throws Exception {
		if (application.equalsIgnoreCase("BOX" )) {
			Reporter.log(application + ": List user " , true);
			return box.listUser();
			
		} else if(application.equalsIgnoreCase("ONEDRIVEBUSINESS")) {
			Reporter.log(application + ": List user " , true);
			return onedrivebusiness.listUsers();
			
		} else {
			System.out.println("No Such Application Listed");
			return null;
		}
	}
	
	public <T> T listAllUsers(int offset, int limit) throws Exception {
		if (application.equalsIgnoreCase("BOX" )) {
			Reporter.log(application + ": List all user " , true);
			return box.listAllUsers(offset, limit);
			
		} else {
			System.out.println("No Such Application Listed");
			return null;
		}
	}

	public <T> T transferOwner(String folderId, String currentOwnerId, String futureOwnerId) throws Exception {
		if (application.equalsIgnoreCase("BOX" )) {
			Reporter.log(application + ": Transferring the ownership " , true);
			return box.transferOwner(folderId, currentOwnerId, futureOwnerId);
		} else {
			System.out.println("No Such Application Listed");
		}
		return null;
	}


	public <T> T createGroup(Object object) throws Exception {
		if (application.equalsIgnoreCase("BOX" )) {
			Reporter.log(application + ": Create group " , true);
			return box.createGroup((GroupInput)object);
		} else {
			System.out.println("No Such Application Listed");
		}
		return null;
	}


	public <T> T updateGroup(Object object) throws Exception {
		if (application.equalsIgnoreCase("BOX" )) {
			Reporter.log(application + ": Update group " , true);
			return box.updateGroup((BoxGroup)(object));
		} else {
			System.out.println("No Such Application Listed");
		}
		return null;
	}


	public void deleteGroup(Object object) throws Exception {
		if (application.equalsIgnoreCase("BOX" )) {
			Reporter.log(application + ": Delete group " , true);
			box.deleteGroup((BoxGroup)object);
		} else {
			System.out.println("No Such Application Listed");
		}
	}

	@SuppressWarnings("unchecked")
	public <T> T listGroup() throws Exception {
		if (application.equalsIgnoreCase("BOX" )) {
			Reporter.log(application + ": List group " , true);
			return box.listGroup();
		} else {
			System.out.println("No Such Application Listed");
		}
		return null;
	}
	

	public <T> T createMembership(Object object) throws Exception {
		if (application.equalsIgnoreCase("BOX" )) {
			Reporter.log(application + ": Create membership " , true);
			return box.createMembership((MembershipInput)object);
		} else {
			System.out.println("No Such Application Listed");
		}
		return null;
	}


	public <T> T updateMembership(Object object) throws Exception {
		if (application.equalsIgnoreCase("BOX" )) {
			Reporter.log(application + ": Update membership " , true);
			return box.updateMembership((BoxMembership)(object));
		} else {
			System.out.println("No Such Application Listed");
		}
		return null;
	}


	public void deleteMembership(Object object) throws Exception {
		if (application.equalsIgnoreCase("BOX" )) {
			Reporter.log(application + ": Delete membership " , true);
			box.deleteMembership((BoxMembership)object);
		} else {
			System.out.println("No Such Application Listed");
		}
	}

	

	public <T> T createCollaboration(Object object) throws Exception {
		if (application.equalsIgnoreCase("BOX" )) {
			Reporter.log(application + ": Create collaboration " , true);
			return box.createCollaboration((CollaborationInput)object);
		} else {
			System.out.println("No Such Application Listed");
		}
		return null;
	}


	public <T> T updateCollaboration(Object object) throws Exception {
		if (application.equalsIgnoreCase("BOX" )) {
			Reporter.log(application + ": Update collaboration " , true);
			return box.updateCollaboration((BoxCollaboration)(object));
		} else {
			System.out.println("No Such Application Listed");
		}
		return null;
	}


	public void deleteCollaboration(Object object) throws Exception {
		if (application.equalsIgnoreCase("BOX" )) {
			Reporter.log(application + ": Delete collaboration " , true);
			box.deleteCollaboration((BoxCollaboration)(object));
		} else {
			System.out.println("No Such Application Listed");
		}
	}


	public <T> T createWeblink(Object object) throws Exception {
		if (application.equalsIgnoreCase("BOX" )) {
			Reporter.log(application + ": Create weblink " , true);
			return box.createWeblink((WeblinkInput)object);
		} else {
			System.out.println("No Such Application Listed");
		}
		return null;
	}


	public <T> T updateWeblink(Object object) throws Exception {
		if (application.equalsIgnoreCase("BOX" )) {
			Reporter.log(application + ": Update weblink " , true);
			return box.updateWeblink((BoxWeblink)(object));
		} else {
			System.out.println("No Such Application Listed");
		}
		return null;
	}


	public void deleteWeblink(Object object) throws Exception {
		if (application.equalsIgnoreCase("BOX" )) {
			Reporter.log(application + ": Delete weblink " , true);
			box.deleteWeblink((BoxWeblink)(object));
		} else {
			System.out.println("No Such Application Listed");
		}
	}


	public <T> T restoreWeblink(Object object) throws Exception {
		if (application.equalsIgnoreCase("BOX" )) {
			Reporter.log(application + ": Restore weblink " , true);
			return box.restoreWeblink((BoxWeblink)(object));
		} else {
			System.out.println("No Such Application Listed");
		}
		return null;
	}


	public <T> T copyWeblink(String weblinkId, String parentId) throws Exception {
		if (application.equalsIgnoreCase("BOX" )) {
			Reporter.log(application + ": Copy weblink " , true);
			return box.copyWeblink(weblinkId, parentId);
		} else {
			System.out.println("No Such Application Listed");
		}
		return null;
	}

	public <T> T createSharedLinkForWeblink(String weblinkId, SharedLink sharedLink) throws Exception {
		if (application.equalsIgnoreCase("BOX" )) {
			Reporter.log(application + ": Create shared link for weblink " , true);
			return box.createSharedLinkForWeblink(weblinkId, sharedLink);
		} else {
			System.out.println("No Such Application Listed");
		}
		return null;
	}

	public <T> T updateSharedLinkForWeblink(String weblinkId, SharedLink sharedLink) throws Exception {
		if (application.equalsIgnoreCase("BOX" )) {
			Reporter.log(application + ": Update shared link for weblink  " , true);
			return box.updateSharedLinkForWeblink(weblinkId, sharedLink);
		} else {
			System.out.println("No Such Application Listed");
		}
		return null;
	}

	public <T> T disableSharedLinkForWeblink(String weblinkId) throws Exception {
		if (application.equalsIgnoreCase("BOX" )) {
			Reporter.log(application + ": Disable shared link for weblink  " , true);
			return box.disableSharedLinkForWeblink(weblinkId);
		} else {
			System.out.println("No Such Application Listed");
		}
		return null;
	}

	public void logoutUser() throws Exception {
		if (application.equalsIgnoreCase("BOX" )) {
			Reporter.log(application + ": Session logout a user" , true);
			box.logoutUser();
		} else {
			System.out.println("No Such Application Listed");
		}
	}

	public void createSharedLink(String username, String password, String filename) throws Exception {
		if (application.equalsIgnoreCase("ONEDRIVEBUSINESS" )) {
			Reporter.log(application + ": Creating shared link " , true);
			onedrivebusiness.shareLink(username, password, filename);

		} else {
			System.out.println("No Such Application Listed");
		}
	}

	public void disableSharedLink(String username, String password, String filename) throws Exception {
		if (application.equalsIgnoreCase("ONEDRIVEBUSINESS" )) {
			Reporter.log(application + ": Disabling shared link " , true);
			onedrivebusiness.unshareLink(username, password, filename);

		} else {
			System.out.println("No Such Application Listed");
		}
	}

	@SuppressWarnings("unchecked")
	public <T> T shareWithCollaborators(Object object) throws Exception {
		if (application.equalsIgnoreCase("ONEDRIVEBUSINESS" )) {
			Reporter.log(application + ": Creating shared link " , true);
			return (T) onedrivebusiness.shareWithCollaborators((SharingUserRoleAssignment) object);

		} else {
			return (T) "No Such Application Listed";
		}
	}


	public <T> T createFolderAsUser(String folderId, String parentFolderId, String asUser) throws Exception {
		if (application.equalsIgnoreCase("BOX" )) {
			Reporter.log(application + ": Create Folder As user " , true);
			return box.createFolderAsUser(folderId, parentFolderId, asUser);
		} else {
			System.out.println("No Such Application Listed");
		}
		return null;
	}

	//Sharepoint apis


	@SuppressWarnings("unchecked")
	public void getFolderProperties(String folderId) throws Exception{

		if (application.equalsIgnoreCase("ONEDRIVEBUSINESS" )) {
			Reporter.log(application + ": Retrieving the folder properties " , true);
			onedrivebusiness.getFolderProperties(folderId);

		} else {
			//return (T) "No Such Application Listed";
		}
	}

	@SuppressWarnings("unchecked")
	public <T> T renameFolder(String folderId, String updatedName) throws Exception{

		if (application.equalsIgnoreCase("ONEDRIVEBUSINESS" )) {
			Reporter.log(application + ": Renaming the folder " , true);
			return onedrivebusiness.renameFolder(folderId, updatedName);

		} else {
			return (T) "No Such Application Listed";
		}
	}

	@SuppressWarnings("unchecked")
	public <T> T getListItemAllFields(String name, boolean isFolder) throws Exception{

		if (application.equalsIgnoreCase("ONEDRIVEBUSINESS" )) {
			Reporter.log(application + ": Retrieving list item all fields " , true);
			return (T) onedrivebusiness.getListItemAllField(name, isFolder);

		} else {
			return (T) "No Such Application Listed";
		}
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getListItemAllFieldsByUrl(String listname, String fileurl) throws Exception{

		if (application.equalsIgnoreCase("ONEDRIVEBUSINESS" )) {
			Reporter.log(application + ": Retrieving list item all fields by listname and fileurl" , true);
			return (T) onedrivebusiness.getListItemAllFieldsByUrl(listname, fileurl);

		} else {
			return (T) "No Such Application Listed";
		}
	}
	
	@SuppressWarnings("unchecked")
	public void resetRoleInheritance(String itemlink) throws Exception{

		if (application.equalsIgnoreCase("ONEDRIVEBUSINESS" )) {
			Reporter.log(application + ": Reset role inheritance " , true);
			onedrivebusiness.listItemResetRoleInheritance(itemlink);
		} 
	}
	

	@SuppressWarnings("unchecked")
	public void addRoleAssignmentForListItem(String itemlink, String principalId, String roleId) throws Exception{

		if (application.equalsIgnoreCase("ONEDRIVEBUSINESS" )) {
			Reporter.log(application + ": Add role assignment for item " , true);
			onedrivebusiness.addRoleAssignmentForListItem(itemlink, principalId, roleId);
		} 
	}

	
	
	@SuppressWarnings("unchecked")
	public void updateListItem(String editlink, String payload) throws Exception{

		if (application.equalsIgnoreCase("ONEDRIVEBUSINESS" )) {
			Reporter.log(application + ": Updating the list item " , true);
			onedrivebusiness.updateListItem(editlink, payload);
		} 
	}

	@SuppressWarnings("unchecked")
	public <T> T getSPList() throws Exception{

		if (application.equalsIgnoreCase("ONEDRIVEBUSINESS" )) {
			Reporter.log(application + ": Retrieving the sharepoint list " , true);
			return (T) onedrivebusiness.getSharePointList();

		} else {
			return (T) "No Such Application Listed";
		}
	}

	@SuppressWarnings("unchecked")
	public <T> T getSPUserList() throws Exception{

		if (application.equalsIgnoreCase("ONEDRIVEBUSINESS" )) {
			Reporter.log(application + ": Retrieving the user list information " , true);
			return (T) onedrivebusiness.getSharePointUserList();

		} else {
			return (T) "No Such Application Listed";
		}
	}

	@SuppressWarnings("unchecked")
	public <T> T getSPDocumentList() throws Exception{

		if (application.equalsIgnoreCase("ONEDRIVEBUSINESS" )) {
			Reporter.log(application + ": Retrieving the document list " , true);
			return (T) onedrivebusiness.getSharePointDocumentList();

		} else {
			return (T) "No Such Application Listed";
		}
	}


	public void updateSPList(String guid, Object object) throws Exception {
		if (application.equalsIgnoreCase("ONEDRIVEBUSINESS" )) {
			Reporter.log(application + ": Updating the list " , true);
			onedrivebusiness.updateSharePointList(guid, (UpdateList)(object));

		} else {
			Reporter.log("No Such Application Listed", true);
		}
	}

	public void breakRoleInheritanceForList(String guid, boolean copyRoleAssignments, boolean clearSubscopes) throws Exception {
		if (application.equalsIgnoreCase("ONEDRIVEBUSINESS" )) {
			Reporter.log(application + ": Breaking role inheritance for list " , true);
			onedrivebusiness.breakRoleInheritanceForList(guid, copyRoleAssignments, clearSubscopes);

		} else {
			Reporter.log("No Such Application Listed", true);
		}
	}

	public void breakRoleInheritanceForList(String guid, String itemId, boolean copyRoleAssignments, boolean clearSubscopes) throws Exception {
		if (application.equalsIgnoreCase("ONEDRIVEBUSINESS" )) {
			Reporter.log(application + ": Breaking role inheritance for list item " , true);
			onedrivebusiness.breakRoleInheritanceForListItem(guid, itemId, copyRoleAssignments, clearSubscopes);

		} else {
			Reporter.log("No Such Application Listed", true);
		}
	}

	public void breakRoleInheritanceForListItemByRelativeUrl(String relativeUrl, boolean copyRoleAssignments, boolean clearSubscopes) throws Exception {
		if (application.equalsIgnoreCase("ONEDRIVEBUSINESS" )) {
			Reporter.log(application + ": Breaking role inheritance for list item by relative url " , true);
			onedrivebusiness.breakRoleInheritanceForListItemByRelativeUrl(relativeUrl, copyRoleAssignments, clearSubscopes);

		} else {
			Reporter.log("No Such Application Listed", true);
		}
	}
	
	public void breakRoleInheritanceForListItem(String itemlink, boolean copyRoleAssignments, boolean clearSubscopes) throws Exception {
		if (application.equalsIgnoreCase("ONEDRIVEBUSINESS" )) {
			Reporter.log(application + ": Breaking role inheritance for list item  " , true);
			onedrivebusiness.breakRoleInheritanceForListItem(itemlink, copyRoleAssignments, clearSubscopes);

		} else {
			Reporter.log("No Such Application Listed", true);
		}
	}
	

	public void disableSharing(String filename) throws Exception {
		if (application.equalsIgnoreCase("ONEDRIVEBUSINESS" )) {
			Reporter.log(application + ": Disabling file sharing " , true);
			onedrivebusiness.disableSharing(filename);

		} else {
			Reporter.log("No Such Application Listed", true);
		}
	}

	public void disableFolderSharing(String filename) throws Exception {
		if (application.equalsIgnoreCase("ONEDRIVEBUSINESS" )) {
			Reporter.log(application + ": Disabling folder sharing " , true);
			onedrivebusiness.disableFolderSharing(filename);

		} else {
			Reporter.log("No Such Application Listed", true);
		}
	}

	public void disableDocumentListSharing(Result documentResult) throws Exception {
		if (application.equalsIgnoreCase("ONEDRIVEBUSINESS" )) {
			Reporter.log(application + ": Disabling document list sharing" , true);
			onedrivebusiness.disableDocumentListSharing(documentResult);

		} else {
			Reporter.log("No Such Application Listed", true);
		}
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getSharePointItemRolesAssignments(String filename) throws Exception{

		if (application.equalsIgnoreCase("ONEDRIVEBUSINESS" )) {
			Reporter.log(application + ": Retrieving the file information " , true);
			return onedrivebusiness.getSharePointItemRolesAssignments(filename);

		} else {
			return (T) "No Such Application Listed";
		}
	}


	@SuppressWarnings("unchecked")
	public <T> T getRecyleBinItems() throws Exception {
		if (application.equalsIgnoreCase("ONEDRIVEBUSINESS" )) {
			Reporter.log(application + ": Getting recycle bin list items " , true);
			return (T) onedrivebusiness.getRecyleBinItems();

		} else {
			Reporter.log("No Such Application Listed", true);
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public <T> T recyleItem(String filename, boolean isFolder) throws Exception {
		if (application.equalsIgnoreCase("ONEDRIVEBUSINESS" )) {
			Reporter.log(application + ": Recycling list item " , true);
			return (T) onedrivebusiness.recycleFileItem(filename, isFolder);

		} else {
			Reporter.log("No Such Application Listed", true);
			return null;
		}
	}


	public void restoreRecycleBinItem(String fileId) throws Exception {
		if (application.equalsIgnoreCase("ONEDRIVEBUSINESS" )) {
			Reporter.log(application + ": Restoring recycle bin item " , true);
			onedrivebusiness.restoreRecyleBinItem(fileId);

		} else {
			Reporter.log("No Such Application Listed", true);
		}
	}


	@SuppressWarnings("unchecked")
	public <T> T getSharePointRolesDefinitions() throws Exception {
		if (application.equalsIgnoreCase("ONEDRIVEBUSINESS" )) {
			Reporter.log(application + ": Retrieving role definitions... " , true);
			return (T) onedrivebusiness.getSharePointRolesDefinitions();

		} else {
			Reporter.log("No Such Application Listed", true);
			return null;
		}	
	}

	@SuppressWarnings("unchecked")
	public <T> T addSharePointListRolesAssignments(String principalId, String roleDefId) throws Exception {
		if (application.equalsIgnoreCase("ONEDRIVEBUSINESS" )) {
			Reporter.log(application + ": Adding role assignments... " , true);
			return (T) onedrivebusiness.addSharePointListRolesAssignments(principalId, roleDefId);

		} else {
			Reporter.log("No Such Application Listed", true);
			return null;
		}	
	}


	@SuppressWarnings("unchecked")
	public void updateFileContent(String fileId, String filename) throws Exception{

		if (application.equalsIgnoreCase("ONEDRIVEBUSINESS" )) {
			Reporter.log(application + ": Updating the file contents " , true);
			onedrivebusiness.updateFile(fileId, filename);

		} 
	}


	@SuppressWarnings("unchecked")
	public void copyFileTo(String sourcefilename, String destinationfolder, String filename, boolean overwrite) throws Exception{

		if (application.equalsIgnoreCase("ONEDRIVEBUSINESS" )) {
			Reporter.log(application + ": Copying the file to the destination " , true);
			onedrivebusiness.copyFileTo(sourcefilename, destinationfolder, filename, overwrite);

		} 
	}
	
	@SuppressWarnings("unchecked")
	public void moveFileTo(String sourcefilename, String destinationfolder, String filename, int overwrite) throws Exception{

		if (application.equalsIgnoreCase("ONEDRIVEBUSINESS" )) {
			Reporter.log(application + ": Moving the file to the destination " , true);
			onedrivebusiness.moveFileTo(sourcefilename, destinationfolder, filename, overwrite);

		} 
	}

	/***************************************************************************/
	/***********       Initializing the config for Box           ***************/
	/***************************************************************************/

	private synchronized void initBoxConfiguration(UserAccount userAccount) throws Exception {

		if(userAccount != null) {

			String bundleName =  "com.universal.properties.box"+tenant ;
			if (userAccount.getRole() != null) {
				bundleName +=  userAccount.getRole().toLowerCase();
			} 

			ResourceBundle rb = ResourceBundle.getBundle(bundleName);
			this.tokenProducer = new TokenProducer(rb.getString("clientid"), rb.getString("clientsecret"),
					rb.getString("clientsecret"), rb.getString("refreshtoken"));

			box = new Box(tokenProducer, userAccount);
			if (!box.initAccessTokenByRefreshTokenAndClientId()) {
				System.out.println("Required parameters are missing from the properties file");
			}

		} else {
			throw new Exception ("Unsupported Role");
		}

	}

	private void initExternalUserBoxConfiguration(UserAccount userAccount) throws Exception {
		if(userAccount != null) {

			String clientId = "mz70byrcdlfvmlj5y7o2eima01nodrgd";
			String clientSecret = "23Gy4eBWab97kt2mCWNajH2JxoGZfkzj";
			String authCode = "dksjfklsknoksrsKkdfls";
			String refreshToken = "ZbqBXZvRIsbNN3Q7yNByQKyEmTe7kVpRgyMG6Gvc6Lw35Nh1ATDLuJY9a2afQKii";
			this.tokenProducer = new TokenProducer(clientId, clientSecret, authCode, refreshToken);

			box = new Box(tokenProducer, userAccount);
			if (!box.initAccessTokenByRefreshTokenAndClientId()) {
				System.out.println("Required parameters are missing from the properties file");
			}
		} else {
			throw new Exception ("Unsupported Role");
		}
	}


	private synchronized void initOneDriveBusinessConfiguration(UserAccount userAccount) throws Exception {
		if(userAccount != null) {

			//String bundleName =  "com.universal.properties.onedrivebusiness"+tenant;
			String bundleName = "com.universal.properties."+userAccount.getEnvironment()+"_onedrivebusiness" + this.tenant ;

			if (userAccount.getRole() != null) {
				bundleName +=  userAccount.getRole().toLowerCase();
			} 

			ResourceBundle rb = ResourceBundle.getBundle(bundleName);

			this.tokenProducer = new TokenProducer(rb.getString("clientid"), rb.getString("clientsecret"),
					rb.getString("clientsecret"), rb.getString("refreshtoken"));

			onedrivebusiness = new OneDriveBusiness(tokenProducer, userAccount);
			if (!onedrivebusiness.initAccessTokenByRefreshTokenAndClientId()) {
				System.out.println("Required parameters are missing from the properties file");
			}
		} else {
			throw new Exception ("Unsupported Role");
		}
	}


	// https://www.dropbox.com/developers/apps/create/business
	private void initDropBoxConfiguration(UserAccount userAccount) throws Exception{ 
		DbxRequestConfig dbxRequestConfig = new DbxRequestConfig("Elastica", Locale.getDefault().toString()); 
		if(userAccount.getAccess_token()!=null){
			DbxClientV2 dbx_client = new DbxClientV2(dbxRequestConfig,userAccount.getAccess_token());
			this.dropbox=new DropBox(dbx_client);
			//System.out.println("## Dropbox Linked Account Info :"+dropbox.getLinkedAccount().getDisplayName()); 
			return;
		}
		UserAccount account=new UserAccount(null,null,"ADMIN");
		if(userAccount==null){
			userAccount=account;
		}
		ResourceBundle rb = ResourceBundle.getBundle("com.universal.properties.dropbox");

		if (userAccount.getRole().equalsIgnoreCase("ADMIN")) {
			DbxClientV2 dbx_client = new DbxClientV2(dbxRequestConfig, rb.getObject("accesstoken_admin").toString());
			this.dropbox=new DropBox(dbx_client);
		} else if(userAccount.getRole().equalsIgnoreCase("COADMIN")) {
			DbxClientV2 dbx_client = new DbxClientV2(dbxRequestConfig, rb.getObject("accesstoken_coadmin").toString());
			this.dropbox=new DropBox(dbx_client);
		} else if(userAccount.getRole().equalsIgnoreCase("ENDUSER")) {
			DbxClientV2 dbx_client = new DbxClientV2(dbxRequestConfig, rb.getObject("accesstoken_enduser").toString());
			this.dropbox=new DropBox(dbx_client);
		} 
		Reporter.log("## Dropbox Linked Account Info :"+dropbox.getLinkedAccount().getDisplayName(),true);  
	}

   private void initGDriveConfiguration(UserAccount userAccount) {
        this.gDrive = new GDrive(userAccount.getAccess_token()); 
        //Reporter.log("## GDRIVE Linked Account Info :" + gDrive.printAbout().toString(), true);
       // Reporter.log("## GDRIVE Linked Account Info :" + gDrive.printAbout().toString(), true);
        Reporter.log("## GDRIVE SUCCESSFULLY INITIALIZED ##", true);
    }

	public UserAccount getUserAccountForGdrive() throws IOException, GeneralSecurityException {
		ResourceBundle rb = ResourceBundle.getBundle("com.universal.properties.googledrive");
		String CLIENT_ID=rb.getObject("clientid").toString();
		String CLIENT_SECRET=rb.getObject("clientsecret").toString();
		String refreshToken=rb.getObject("refreshTokenElasticaQA").toString();
		GDriveAuthorization gDriveAuthorization=new GDriveAuthorization(CLIENT_ID, CLIENT_SECRET);
		UserAccount userAccount=new UserAccount(gDriveAuthorization.getAceessTokenFromRefreshAccessToken(refreshToken));
		return userAccount;
	}


	private synchronized void initSalesforceConfiguration(UserAccount userAccount) throws Exception {
		if(userAccount != null) {

			//String bundleName =  "com.universal.properties.sf"+tenant;
			String bundleName = "com.universal.properties.sf" + this.tenant ;

			if (userAccount.getRole() != null) {
				bundleName +=  userAccount.getRole().toLowerCase();
			} 
			
			Reporter.log("Loaded property bundle file:"+bundleName, true);
			ResourceBundle rb = ResourceBundle.getBundle(bundleName);
			
			Reporter.log("If token is passed from suite, I will use it. Else I will load from properties.", true);
			
			String clientId = (userAccount.getInstanceParams().get("clientId") != null) ? 
									userAccount.getInstanceParams().get("clientId") : rb.getString("clientid");
									
			String clientSecret = (userAccount.getInstanceParams().get("clientSecret") != null) ? 
									userAccount.getInstanceParams().get("clientSecret") : rb.getString("clientsecret");
			
			String refreshToken = "EQUZEc3QoEa0EqBzHVpoMLgr";
								
			this.tokenProducer = new TokenProducer(clientId, clientSecret, clientSecret, refreshToken);
			
			this.salesForce = new Salesforce(tokenProducer, userAccount);
			if (!salesForce.initAccessTokenByUsernameAndPassword()) {
				Reporter.log("Required parameters are missing from salesforce properties file");
			}
		} else {
			throw new Exception ("Unsupported Role");
		}
	}
	
	private synchronized void initServiceNowConfiguration(UserAccount userAccount) throws Exception {
		if(userAccount != null) {

			
			String bundleName = "com.universal.properties.sn" + this.tenant ;

			if (userAccount.getRole() != null) {
				bundleName +=  userAccount.getRole().toLowerCase();
			} else {
				bundleName +=  "admin";
			}

			ResourceBundle snrb = ResourceBundle.getBundle(bundleName);
			
			this.tokenProducer = new TokenProducer(snrb.getString("clientid"), snrb.getString("clientsecret"),
					snrb.getString("clientsecret"), snrb.getString("refreshtoken"));

			this.serviceNow = new ServiceNow(tokenProducer, userAccount);
			if (!serviceNow.initAccessTokenByUsernameAndPassword()) {
				Reporter.log("Required parameters are missing from servicenow properties file");
			}
		} else {
			throw new Exception ("Unsupported Role");
		}
	}
        
        public final void initGmailConfiguration(UserAccount userAccount){
            this.googleMailServices=new GoogleMailServices(userAccount.getAccess_token());
        }
	
}
