package com.elastica.beatle.tests.securlets;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;

public class OneDriveActivityLog {


	//File logs
	private String fileUploadLog 		= "User added '{filename}'.";
	private String docFileUploadLog 		= "User added '{filename}'.";
	private String excelFileUploadLog 		= "User added '{filename}'.";
	private String pptFileUploadLog 		= "User added '{filename}'.";
	
	
	private String fileDownloadLog		= "User downloaded '{filename}'";
	private String fileEditLog			= "User updated '{filename}'.";
	private String fileRenameLog		= "User renamed item to '{filename}'.";
	private String fileDeleteLog		= "File '{filename}' has been deleted.";
	private String fileRestoreLog		= "User restored '{filename}'.";
	private String fileShareLog			= "User shared '{filename}' with '{username}({permission})'."; //User shared 'Root:Documents' with 'Everyone except external users(Contribute)'.
	private String fileUnShareLog		= "User unshared '{filename}' with '{username}({permission})'.";
	private String fileScopeChangeLog	= "'{filename}' has stopped inheriting permissions of the parent.";//"Permission scope for '{filename}' has changed. Breaking inherited permissions.";
	private String fileShareNoUserLog	= "'{filename}' has been shared."; // '1453260792626_HIPAA_Test2.txt' has been shared
	private String fileUnShareNoUserLog	= "'{filename}' has been unshared."; // '1453260792626_HIPAA_Test2.txt' has been shared

	
	private String fileShareLogPartial = "User shared '{filename}'";
	private String fileUnShareLogPartial = "User unshared '{filename}'";
	
	
	private String fileCollaboratorEditShareLog 	= "User shared '{filename}' with '{username}(Contribute)'.";
	private String fileCollaboratorEditUnshareLog 	= "User unshared '{filename}' with '{username}(Contribute)'.";
	private String fileCollaboratorViewShareLog 	= "'{filename}' has been shared."; //"User shared '{filename}'"; //has been shared
	private String fileCollaboratorViewUnshareLog 	= "User unshared '{filename}' with '{username}(Read)'.";

	private String fileCopyLog		= "User renamed item to '{filename}'.";
	private String fileMoveLog		= "User added '{filename}'.";

	//Folder logs
	private String folderUploadLog 		= "User added '{foldername}'.";
	private String folderDeleteLog 		= "Folder '{foldername}' has been deleted.";
	private String folderRestoreLog 	= "User restored '{foldername}'.";
	private String folderFileUploadLog 	= "User added '{filename}'.";
	private String folderFileDeleteLog 	= "File '{filename}' has been deleted.";
	private String folderUpdateLog		= "User updated '{foldername}'.";
	private String folderRenameLog		= "User renamed item to '{foldername}'.";
	//	private String folderShareLog		= "User shared '{foldername}'";
	//	private String folderUnShareLog		= "User unshared '{foldername}'";
	private String folderShareLog		= "User shared '{foldername}' with '{username}({permission})'."; //User shared 'Root:Documents' with 'Everyone except external users(Contribute)'.
	private String folderUnShareLog		= "User unshared '{foldername}' with '{username}({permission})'.";
	private String folderScopeChangeLog	= "'{foldername}' has stopped inheriting permissions of the parent."; 
											//"Permission scope for '{foldername}' has changed. Breaking inherited permissions.";
											//User shared '1467744105919_Securlets_Automation' with 'pushpan@gmail.com(Contribute)'
	//List logs
	private String documentListEditLog	= "User updated 'Root:Documents'.";
	private String documentListShareLog = "User shared 'Root:Documents' with '{username}({permission})'.";
	private String documentListUnShareLog = "'Root:Documents' has been unshared.";
	private String documentListUserUnShareLog = "User unshared 'Root:Documents' with '{username}({permission})'.";
	
	
	//Site related logs
	private String siteCreateLog	= "A new Sub-site {sitename} has been created.";
	private String siteUpdateLog	= "A Sub-site {sitename} has been updated.";
	private String siteDeleteLog	= "A site {sitename} has been deleted.";
	
	private String siteFolderShareLog	= "'{foldername}' has been shared.";
	private String siteFolderUnShareLog	= "'{foldername}' has been unshared.";
	
	/**
	 * @return the siteFolderShareLog
	 */
	public String getSiteFolderShareLog() {
		return siteFolderShareLog;
	}

	/**
	 * @param siteFolderShareLog the siteFolderShareLog to set
	 */
	public void setSiteFolderShareLog(String siteFolderShareLog) {
		this.siteFolderShareLog = siteFolderShareLog;
	}

	/**
	 * @return the siteFolderUnShareLog
	 */
	public String getSiteFolderUnShareLog() {
		return siteFolderUnShareLog;
	}

	/**
	 * @param siteFolderUnShareLog the siteFolderUnShareLog to set
	 */
	public void setSiteFolderUnShareLog(String siteFolderUnShareLog) {
		this.siteFolderUnShareLog = siteFolderUnShareLog;
	}
	
	/**
	 * @return the fileShareLogPartial
	 */
	public String getFileShareLogPartial() {
		return fileShareLogPartial;
	}

	/**
	 * @param fileShareLogPartial the fileShareLogPartial to set
	 */
	public void setFileShareLogPartial(String fileShareLogPartial) {
		this.fileShareLogPartial = fileShareLogPartial;
	}

	/**
	 * @return the fileUnShareLogPartial
	 */
	public String getFileUnShareLogPartial() {
		return fileUnShareLogPartial;
	}

	/**
	 * @param fileUnShareLogPartial the fileUnShareLogPartial to set
	 */
	public void setFileUnShareLogPartial(String fileUnShareLogPartial) {
		this.fileUnShareLogPartial = fileUnShareLogPartial;
	}

	
	
	/**
	 * @return the fileUnShareNoUserLog
	 */
	public String getFileUnShareNoUserLog() {
		return fileUnShareNoUserLog;
	}

	/**
	 * @param fileUnShareNoUserLog the fileUnShareNoUserLog to set
	 */
	public void setFileUnShareNoUserLog(String fileUnShareNoUserLog) {
		this.fileUnShareNoUserLog = fileUnShareNoUserLog;
	}



	
	
	/**
	 * @return the siteUpdateLog
	 */
	public String getSiteUpdateLog() {
		return siteUpdateLog;
	}

	/**
	 * @param siteUpdateLog the siteUpdateLog to set
	 */
	public void setSiteUpdateLog(String siteUpdateLog) {
		this.siteUpdateLog = siteUpdateLog;
	}


	
	
	

	//ContentInspection logs
	private String pciRiskLog			 		= "File {filename} has risk(s) - {risktype}";
	private String piiRiskLog			 		= "File {filename} has risk(s) - {risktype}";
	private String hipaaRiskLog			 		= "File {filename} has risk(s) - {risktype}";

	//Collaborator log
	
	
	/**
	 * @return the siteCreateLog
	 */
	public String getSiteCreateLog() {
		return siteCreateLog;
	}

	/**
	 * @param siteCreateLog the siteCreateLog to set
	 */
	public void setSiteCreateLog(String siteCreateLog) {
		this.siteCreateLog = siteCreateLog;
	}

	/**
	 * @return the siteDeleteLog
	 */
	public String getSiteDeleteLog() {
		return siteDeleteLog;
	}

	/**
	 * @param siteDeleteLog the siteDeleteLog to set
	 */
	public void setSiteDeleteLog(String siteDeleteLog) {
		this.siteDeleteLog = siteDeleteLog;
	}

	
	
	/**
	 * @return the docFileUploadLog
	 */
	public String getDocFileUploadLog() {
		return docFileUploadLog;
	}

	/**
	 * @param docFileUploadLog the docFileUploadLog to set
	 */
	public void setDocFileUploadLog(String docFileUploadLog) {
		this.docFileUploadLog = docFileUploadLog;
	}

	/**
	 * @return the excelFileUploadLog
	 */
	public String getExcelFileUploadLog() {
		return excelFileUploadLog;
	}

	/**
	 * @param excelFileUploadLog the excelFileUploadLog to set
	 */
	public void setExcelFileUploadLog(String excelFileUploadLog) {
		this.excelFileUploadLog = excelFileUploadLog;
	}

	/**
	 * @return the pptFileUploadLog
	 */
	public String getPptFileUploadLog() {
		return pptFileUploadLog;
	}

	/**
	 * @param pptFileUploadLog the pptFileUploadLog to set
	 */
	public void setPptFileUploadLog(String pptFileUploadLog) {
		this.pptFileUploadLog = pptFileUploadLog;
	}

	/**
	 * @return the pciRiskLog
	 */
	public String getPciRiskLog() {
		return pciRiskLog;
	}

	/**
	 * @param pciRiskLog the pciRiskLog to set
	 */
	public void setPciRiskLog(String pciRiskLog) {
		this.pciRiskLog = pciRiskLog;
	}

	/**
	 * @return the piiRiskLog
	 */
	public String getPiiRiskLog() {
		return piiRiskLog;
	}

	/**
	 * @param piiRiskLog the piiRiskLog to set
	 */
	public void setPiiRiskLog(String piiRiskLog) {
		this.piiRiskLog = piiRiskLog;
	}

	/**
	 * @return the hipaaRiskLog
	 */
	public String getHipaaRiskLog() {
		return hipaaRiskLog;
	}

	/**
	 * @param hipaaRiskLog the hipaaRiskLog to set
	 */
	public void setHipaaRiskLog(String hipaaRiskLog) {
		this.hipaaRiskLog = hipaaRiskLog;
	}


	
	
	/**
	 * @return the fileCollaboratorEditShareLog
	 */
	public String getFileCollaboratorEditShareLog() {
		return fileCollaboratorEditShareLog;
	}

	/**
	 * @param fileCollaboratorEditShareLog the fileCollaboratorEditShareLog to set
	 */
	public void setFileCollaboratorEditShareLog(String fileCollaboratorEditShareLog) {
		this.fileCollaboratorEditShareLog = fileCollaboratorEditShareLog;
	}

	/**
	 * @return the fileCollaboratorEditUnshareLog
	 */
	public String getFileCollaboratorEditUnshareLog() {
		return fileCollaboratorEditUnshareLog;
	}

	/**
	 * @param fileCollaboratorEditUnshareLog the fileCollaboratorEditUnshareLog to set
	 */
	public void setFileCollaboratorEditUnshareLog(String fileCollaboratorEditUnshareLog) {
		this.fileCollaboratorEditUnshareLog = fileCollaboratorEditUnshareLog;
	}

	/**
	 * @return the fileCollaboratorViewShareLog
	 */
	public String getFileCollaboratorViewShareLog() {
		return fileCollaboratorViewShareLog;
	}

	/**
	 * @param fileCollaboratorViewShareLog the fileCollaboratorViewShareLog to set
	 */
	public void setFileCollaboratorViewShareLog(String fileCollaboratorViewShareLog) {
		this.fileCollaboratorViewShareLog = fileCollaboratorViewShareLog;
	}

	/**
	 * @return the fileCollaboratorViewUnshareLog
	 */
	public String getFileCollaboratorViewUnshareLog() {
		return fileCollaboratorViewUnshareLog;
	}

	/**
	 * @param fileCollaboratorViewUnshareLog the fileCollaboratorViewUnshareLog to set
	 */
	public void setFileCollaboratorViewUnshareLog(String fileCollaboratorViewUnshareLog) {
		this.fileCollaboratorViewUnshareLog = fileCollaboratorViewUnshareLog;
	}


	/**
	 * @return the fileCopyLog
	 */
	public String getFileCopyLog() {
		return fileCopyLog;
	}

	/**
	 * @param fileCopyLog the fileCopyLog to set
	 */
	public void setFileCopyLog(String fileCopyLog) {
		this.fileCopyLog = fileCopyLog;
	}

	/**
	 * @return the fileMoveLog
	 */
	public String getFileMoveLog() {
		return fileMoveLog;
	}

	/**
	 * @param fileMoveLog the fileMoveLog to set
	 */
	public void setFileMoveLog(String fileMoveLog) {
		this.fileMoveLog = fileMoveLog;
	}

	/**
	 * @return the fileShareNoUserLog
	 */
	public String getFileShareNoUserLog() {
		return fileShareNoUserLog;
	}

	/**
	 * @param fileShareNoUserLog the fileShareNoUserLog to set
	 */
	public void setFileShareNoUserLog(String fileShareNoUserLog) {
		this.fileShareNoUserLog = fileShareNoUserLog;
	}


	/**
	 * @return the listShareLog
	 */
	public String getDocumentListShareLog() {
		return documentListShareLog;
	}

	/**
	 * @param listShareLog the listShareLog to set
	 */
	public void setDocumentListShareLog(String documentListShareLog) {
		this.documentListShareLog = documentListShareLog;
	}

	/**
	 * @return the documentListEditLog
	 */
	public String getDocumentListEditLog() {
		return documentListEditLog;
	}

	/**
	 * @param documentListEditLog the documentListEditLog to set
	 */
	public void setDocumentListEditLog(String documentListEditLog) {
		this.documentListEditLog = documentListEditLog;
	}


	/**
	 * @return the fileUploadLog
	 */
	public String getFileUploadLog() {
		return fileUploadLog;
	}

	/**
	 * @param fileUploadLog the fileUploadLog to set
	 */
	public void setFileUploadLog(String fileUploadLog) {
		this.fileUploadLog =  fileUploadLog;
	}


	/**
	 * @return the fileDownloadLog
	 */
	public String getFileDownloadLog() {
		return fileDownloadLog;
	}
	/**
	 * @param fileDownloadLog the fileDownloadLog to set
	 */
	public void setFileDownloadLog(String fileDownloadLog) {
		this.fileDownloadLog = fileDownloadLog;
	}
	/**
	 * @return the fileEditLog
	 */
	public String getFileEditLog() {
		return fileEditLog;
	}
	/**
	 * @param fileEditLog the fileEditLog to set
	 */
	public void setFileEditLog(String fileEditLog) {
		this.fileEditLog = fileEditLog;
	}
	/**
	 * @return the fileRenameLog
	 */
	public String getFileRenameLog() {
		return fileRenameLog;
	}
	/**
	 * @param fileRenameLog the fileRenameLog to set
	 */
	public void setFileRenameLog(String fileRenameLog) {
		this.fileRenameLog = fileRenameLog;
	}
	/**
	 * @return the fileDeleteLog
	 */
	public String getFileDeleteLog() {
		return fileDeleteLog;
	}
	/**
	 * @param fileDeleteLog the fileDeleteLog to set
	 */
	public void setFileDeleteLog(String fileDeleteLog) {
		this.fileDeleteLog = fileDeleteLog;
	}

	/**
	 * @return the fileRestoreLog
	 */
	public String getFileRestoreLog() {
		return fileRestoreLog;
	}

	/**
	 * @param fileRestoreLog the fileRestoreLog to set
	 */
	public void setFileRestoreLog(String fileRestoreLog) {
		this.fileRestoreLog = fileRestoreLog;
	}





	/**
	 * @return the fileShareLog
	 */
	public String getFileShareLog() {
		return fileShareLog;
	}

	/**
	 * @param fileShareLog the fileShareLog to set
	 */
	public void setFileShareLog(String fileShareLog) {
		this.fileShareLog = fileShareLog;
	}

	/**
	 * @return the fileUnShareLog
	 */
	public String getFileUnShareLog() {
		return fileUnShareLog;
	}

	/**
	 * @param fileUnShareLog the fileUnShareLog to set
	 */
	public void setFileUnShareLog(String fileUnShareLog) {
		this.fileUnShareLog = fileUnShareLog;
	}



	/**
	 * @return the fileScopeChangeLog
	 */
	public String getFileScopeChangeLog() {
		return fileScopeChangeLog;
	}

	/**
	 * @param fileScopeChangeLog the fileScopeChangeLog to set
	 */
	public void setFileScopeChangeLog(String fileScopeChangeLog) {
		this.fileScopeChangeLog = fileScopeChangeLog;
	}

	/**
	 * @return the folderUploadLog
	 */
	public String getFolderUploadLog() {
		return folderUploadLog;
	}

	/**
	 * @param folderUploadLog the folderUploadLog to set
	 */
	public void setFolderUploadLog(String folderUploadLog) {
		this.folderUploadLog = folderUploadLog;
	}

	/**
	 * @return the folderDeleteLog
	 */
	public String getFolderDeleteLog() {
		return folderDeleteLog;
	}

	/**
	 * @param folderDeleteLog the folderDeleteLog to set
	 */
	public void setFolderDeleteLog(String folderDeleteLog) {
		this.folderDeleteLog = folderDeleteLog;
	}

	/**
	 * @return the folderFileUploadLog
	 */
	public String getFolderFileUploadLog() {
		return folderFileUploadLog;
	}

	/**
	 * @param folderFileUploadLog the folderFileUploadLog to set
	 */
	public void setFolderFileUploadLog(String folderFileUploadLog) {
		this.folderFileUploadLog = folderFileUploadLog;
	}


	/**
	 * @return the folderFileDeleteLog
	 */
	public String getFolderFileDeleteLog() {
		return folderFileDeleteLog;
	}

	/**
	 * @param folderFileDeleteLog the folderFileDeleteLog to set
	 */
	public void setFolderFileDeleteLog(String folderFileDeleteLog) {
		this.folderFileDeleteLog = folderFileDeleteLog;
	}

	/**
	 * @return the folderUpdateLog
	 */
	public String getFolderUpdateLog() {
		return folderUpdateLog;
	}

	/**
	 * @param folderUpdateLog the folderUpdateLog to set
	 */
	public void setFolderUpdateLog(String folderUpdateLog) {
		this.folderUpdateLog = folderUpdateLog;
	}

	/**
	 * @return the folderRenameLog
	 */
	public String getFolderRenameLog() {
		return folderRenameLog;
	}

	/**
	 * @param folderRenameLog the folderRenameLog to set
	 */
	public void setFolderRenameLog(String folderRenameLog) {
		this.folderRenameLog = folderRenameLog;
	}


	/**
	 * @return the folderShareLog
	 */
	public String getFolderShareLog() {
		return folderShareLog;
	}

	/**
	 * @param folderShareLog the folderShareLog to set
	 */
	public void setFolderShareLog(String folderShareLog) {
		this.folderShareLog = folderShareLog;
	}

	/**
	 * @return the folderUnShareLog
	 */
	public String getFolderUnShareLog() {
		return folderUnShareLog;
	}

	/**
	 * @param folderUnShareLog the folderUnShareLog to set
	 */
	public void setFolderUnShareLog(String folderUnShareLog) {
		this.folderUnShareLog = folderUnShareLog;
	}

	/**
	 * @return the folderScopeChangeLog
	 */
	public String getFolderScopeChangeLog() {
		return folderScopeChangeLog;
	}

	/**
	 * @param folderScopeChangeLog the folderScopeChangeLog to set
	 */
	public void setFolderScopeChangeLog(String folderScopeChangeLog) {
		this.folderScopeChangeLog = folderScopeChangeLog;
	}

	/**
	 * @return the folderRestoreLog
	 */
	public String getFolderRestoreLog() {
		return folderRestoreLog;
	}

	/**
	 * @param folderRestoreLog the folderRestoreLog to set
	 */
	public void setFolderRestoreLog(String folderRestoreLog) {
		this.folderRestoreLog = folderRestoreLog;
	}

	/**
	 * @return the documentListUnShareLog
	 */
	public String getDocumentListUnShareLog() {
		return documentListUnShareLog;
	}

	/**
	 * @param documentListUnShareLog the documentListUnShareLog to set
	 */
	public void setDocumentListUnShareLog(String documentListUnShareLog) {
		this.documentListUnShareLog = documentListUnShareLog;
	}

	/**
	 * @return the documentListUserUnShareLog
	 */
	public String getDocumentListUserUnShareLog() {
		return documentListUserUnShareLog;
	}

	/**
	 * @param documentListUserUnShareLog the documentListUserUnShareLog to set
	 */
	public void setDocumentListUserUnShareLog(String documentListUserUnShareLog) {
		this.documentListUserUnShareLog = documentListUserUnShareLog;
	}


	public String toString() {
		String all="";
		try {
			for(PropertyDescriptor propertyDescriptor : 
				Introspector.getBeanInfo(OneDriveActivityLog.class).getPropertyDescriptors()){

				// propertyEditor.getReadMethod() exposes the getter
				// btw, this may be null if you have a write-only property
				all += propertyDescriptor.getReadMethod() + "\n";
				System.out.println(propertyDescriptor.getReadMethod());
			}
		} catch (IntrospectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return all;
	}
}
