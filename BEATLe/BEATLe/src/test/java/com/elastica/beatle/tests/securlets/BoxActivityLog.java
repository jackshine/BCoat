package com.elastica.beatle.tests.securlets;

public class BoxActivityLog {
		//File logs
		private String fileUploadLog 				= "User uploaded {filename} to All Files";
		private String fileDownloadLog				= "User downloaded {filename}";
		private String fileEditLog					= "User edited {filename}";
		private String fileRenameLog				= "User renamed {filename}";
		private String fileLockLog					= "User created a lock on {filename}";
		private String fileUnlockLog				= "User removed a lock on {filename}";
		private String fileShareLog					= "User shared {filename}";
		private String fileUnshareLog				= "User unshared {filename}";
		private String fileDeleteLog				= "User deleted item {filename}";
		private String fileRestoreLog				= "User recovered an item from trash";
		private String filePurgeLog					= "User deleted item {filename}";
		
		//Folder logs
		private String folderUploadLog 				= "User uploaded {foldername} to {destinationfolder}";
		private String folderShareLog 				= "User shared {foldername}";
		private String folderUnshareLog 			= "User unshared {foldername}";
		private String folderDeleteLog 				= "User deleted item {foldername}";
		private String folderRestoreLog 			= "User recovered an item from trash";
		private String folderCopyLog				= "User copied {foldername}";
		private String folderToFileCopyLog			= "User copied {filename}";
		private String folderFileUploadLog			= "User uploaded {filename} to {foldername}";
		private String folderMoveLog				= "User moved {foldername}";
		private String folderUnsyncLog				= "User unsynced {foldername}";
		private String folderRenameLog				= "User renamed {foldername} to {updatedfoldername}"; //"User renamed {foldername}"; //"User renamed {foldername} to {updatedfoldername}";
		private String folderSharePermissionsLog	= "User updated share permissions on {foldername}";
		
		
		//weblink logs
		private String weblinkCreateLog				= "User uploaded {weblinkname} to {foldername}";
		private String weblinkUpdateLog				= "User renamed {weblinkname}";
		private String weblinkDeleteLog				= "User deleted item {weblinkname}";
		private String weblinkRestoreLog			= "User recovered an item from trash";
		private String weblinkCopyLog				= "User copied {weblinkname}";
		private String weblinkMoveLog				= "User moved {weblinkname}";
		private String weblinkShareExpirationLog	= "User set share expiration on {weblinkname}";
		private String weblinkShareLog				= "User shared {weblinkname}";
		private String weblinkUnshareLog			= "User unshared {weblinkname}";
		private String weblinkSharePermissionsLog	= "User updated share permissions on {weblinkname}";
		
		
		//group logs
		private String groupCreateLog				= "User created group {groupname}";
		private String groupEditLog					= "User edited group {groupname}";
		private String groupDeleteLog				= "User deleted group {groupname}";
		private String groupUserDeleteLog			= "User removed a user {username} from a group";
		private String groupUserAddLog				= "User added a new user {username} to a group";
		
		
		//User logs
		private String userCreateLog				= "User created a new user {username}";
		private String userUpdateLog				= "User edited user {username}";
		private String userDeleteLog				= "User deleted user {username}";
		private String userRoleChangeLog			= "User changed admin role for {username}";
		
		
		//Collaborator logs
		private String inviteCollaborator 			= "User invited a collaborator";
		private String removeCollaborator 			= "User removed a collaborator";
		private String acceptCollaboration 			= "User's invitee accepted a collaboration";
		private String addGroupCollaboration 		= "User Added Folder \"{foldername}\" to A Group \"{groupname}\"";
		private String removeGroupCollaboration 	= "User Removed Folder \"{foldername}\" From A Group \"{groupname}\"";
		private String transferOfOwnerShip 			= "Ownership of {collaborationname} has been transferred to external user";
		private String collaborationRoleChange 		= "User changed a collaboration role";
		private String deleteCollaboration	 		= "User deleted item {collaborationname}";
		
		//ContentInspection logs
		private String pciRiskLog			 		= "File {filename} has risk(s) - {risktype}";
		private String piiRiskLog			 		= "File {filename} has risk(s) - {risktype}";
		private String hipaaRiskLog			 		= "File {filename} has risk(s) - {risktype}";
		
		//File logs
		private String i18NFileUploadLog 				= "User uploaded {filename} to All Files";
		private String i18NFileDownloadLog				= "User downloaded {filename}";
		private String i18NFileEditLog					= "User edited {filename}";
		private String i18NFileRenameLog				= "User renamed {filename}";
		private String i18NFileLockLog					= "User created a lock on {filename}";
		private String i18NFileUnlockLog				= "User removed a lock on {filename}";
		private String i18NFileShareLog					= "User shared {filename}";
		private String i18NFileUnshareLog				= "User unshared {filename}";
		private String i18NFileDeleteLog				= "User deleted item {filename}";
		private String i18NFileRestoreLog				= "User recovered an item from trash";
		private String i18NFilePurgeLog					= "User deleted item {filename}";
		
		
		/**
		 * @return the i18NFileUploadLog
		 */
		public String getI18NFileUploadLog() {
			return i18NFileUploadLog;
		}
		/**
		 * @param i18nFileUploadLog the i18NFileUploadLog to set
		 */
		public void setI18NFileUploadLog(String i18nFileUploadLog) {
			i18NFileUploadLog = i18nFileUploadLog;
		}
		/**
		 * @return the i18NFileDownloadLog
		 */
		public String getI18NFileDownloadLog() {
			return i18NFileDownloadLog;
		}
		/**
		 * @param i18nFileDownloadLog the i18NFileDownloadLog to set
		 */
		public void setI18NFileDownloadLog(String i18nFileDownloadLog) {
			i18NFileDownloadLog = i18nFileDownloadLog;
		}
		/**
		 * @return the i18NFileEditLog
		 */
		public String getI18NFileEditLog() {
			return i18NFileEditLog;
		}
		/**
		 * @param i18nFileEditLog the i18NFileEditLog to set
		 */
		public void setI18NFileEditLog(String i18nFileEditLog) {
			i18NFileEditLog = i18nFileEditLog;
		}
		/**
		 * @return the i18NFileRenameLog
		 */
		public String getI18NFileRenameLog() {
			return i18NFileRenameLog;
		}
		/**
		 * @param i18nFileRenameLog the i18NFileRenameLog to set
		 */
		public void setI18NFileRenameLog(String i18nFileRenameLog) {
			i18NFileRenameLog = i18nFileRenameLog;
		}
		/**
		 * @return the i18NFileLockLog
		 */
		public String getI18NFileLockLog() {
			return i18NFileLockLog;
		}
		/**
		 * @param i18nFileLockLog the i18NFileLockLog to set
		 */
		public void setI18NFileLockLog(String i18nFileLockLog) {
			i18NFileLockLog = i18nFileLockLog;
		}
		/**
		 * @return the i18NFileUnlockLog
		 */
		public String getI18NFileUnlockLog() {
			return i18NFileUnlockLog;
		}
		/**
		 * @param i18nFileUnlockLog the i18NFileUnlockLog to set
		 */
		public void setI18NFileUnlockLog(String i18nFileUnlockLog) {
			i18NFileUnlockLog = i18nFileUnlockLog;
		}
		/**
		 * @return the i18NFileShareLog
		 */
		public String getI18NFileShareLog() {
			return i18NFileShareLog;
		}
		/**
		 * @param i18nFileShareLog the i18NFileShareLog to set
		 */
		public void setI18NFileShareLog(String i18nFileShareLog) {
			i18NFileShareLog = i18nFileShareLog;
		}
		/**
		 * @return the i18NFileUnshareLog
		 */
		public String getI18NFileUnshareLog() {
			return i18NFileUnshareLog;
		}
		/**
		 * @param i18nFileUnshareLog the i18NFileUnshareLog to set
		 */
		public void setI18NFileUnshareLog(String i18nFileUnshareLog) {
			i18NFileUnshareLog = i18nFileUnshareLog;
		}
		/**
		 * @return the i18NFileDeleteLog
		 */
		public String getI18NFileDeleteLog() {
			return i18NFileDeleteLog;
		}
		/**
		 * @param i18nFileDeleteLog the i18NFileDeleteLog to set
		 */
		public void setI18NFileDeleteLog(String i18nFileDeleteLog) {
			i18NFileDeleteLog = i18nFileDeleteLog;
		}
		/**
		 * @return the i18NFileRestoreLog
		 */
		public String getI18NFileRestoreLog() {
			return i18NFileRestoreLog;
		}
		/**
		 * @param i18nFileRestoreLog the i18NFileRestoreLog to set
		 */
		public void setI18NFileRestoreLog(String i18nFileRestoreLog) {
			i18NFileRestoreLog = i18nFileRestoreLog;
		}
		/**
		 * @return the i18NFilePurgeLog
		 */
		public String getI18NFilePurgeLog() {
			return i18NFilePurgeLog;
		}
		/**
		 * @param i18nFilePurgeLog the i18NFilePurgeLog to set
		 */
		public void setI18NFilePurgeLog(String i18nFilePurgeLog) {
			i18NFilePurgeLog = i18nFilePurgeLog;
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
		 * @return the hippaRiskLog
		 */
		public String getHipaaRiskLog() {
			return hipaaRiskLog;
		}
		/**
		 * @param hippaRiskLog the hippaRiskLog to set
		 */
		public void setHipaaRiskLog(String hippaRiskLog) {
			this.hipaaRiskLog = hippaRiskLog;
		}
		
		/**
		 * @return the folderSharePermissionsLog
		 */
		public String getFolderSharePermissionsLog() {
			return folderSharePermissionsLog;
		}
		/**
		 * @param folderSharePermissionsLog the folderSharePermissionsLog to set
		 */
		public void setFolderSharePermissionsLog(String folderSharePermissionsLog) {
			this.folderSharePermissionsLog = folderSharePermissionsLog;
		}
		
		
		/**
		 * @return the deleteCollaboration
		 */
		public String getDeleteCollaboration() {
			return deleteCollaboration;
		}
		/**
		 * @param deleteCollaboration the deleteCollaboration to set
		 */
		public void setDeleteCollaboration(String deleteCollaboration) {
			this.deleteCollaboration = deleteCollaboration;
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
		 * @return the folderUnsyncLog
		 */
		public String getFolderUnsyncLog() {
			return folderUnsyncLog;
		}
		/**
		 * @param folderUnsyncLog the folderUnsyncLog to set
		 */
		public void setFolderUnsyncLog(String folderUnsyncLog) {
			this.folderUnsyncLog = folderUnsyncLog;
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
			this.fileUploadLog = fileUploadLog;
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
		 * @return the fileLockLog
		 */
		public String getFileLockLog() {
			return fileLockLog;
		}
		/**
		 * @param fileLockLog the fileLockLog to set
		 */
		public void setFileLockLog(String fileLockLog) {
			this.fileLockLog = fileLockLog;
		}
		/**
		 * @return the fileUnlockLog
		 */
		public String getFileUnlockLog() {
			return fileUnlockLog;
		}
		/**
		 * @param fileUnlockLog the fileUnlockLog to set
		 */
		public void setFileUnlockLog(String fileUnlockLog) {
			this.fileUnlockLog = fileUnlockLog;
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
		 * @return the fileUnshareLog
		 */
		public String getFileUnshareLog() {
			return fileUnshareLog;
		}
		/**
		 * @param fileUnshareLog the fileUnshareLog to set
		 */
		public void setFileUnshareLog(String fileUnshareLog) {
			this.fileUnshareLog = fileUnshareLog;
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
		 * @return the filePurgeLog
		 */
		public String getFilePurgeLog() {
			return filePurgeLog;
		}
		/**
		 * @param filePurgeLog the filePurgeLog to set
		 */
		public void setFilePurgeLog(String filePurgeLog) {
			this.filePurgeLog = filePurgeLog;
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
		 * @return the folderUnshareLog
		 */
		public String getFolderUnshareLog() {
			return folderUnshareLog;
		}
		/**
		 * @param folderUnshareLog the folderUnshareLog to set
		 */
		public void setFolderUnshareLog(String folderUnshareLog) {
			this.folderUnshareLog = folderUnshareLog;
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
		 * @return the folderCopyLog
		 */
		public String getFolderCopyLog() {
			return folderCopyLog;
		}
		/**
		 * @param folderCopyLog the folderCopyLog to set
		 */
		public void setFolderCopyLog(String folderCopyLog) {
			this.folderCopyLog = folderCopyLog;
		}
		/**
		 * @return the folderToFileCopyLog
		 */
		public String getFolderToFileCopyLog() {
			return folderToFileCopyLog;
		}
		/**
		 * @param folderToFileCopyLog the folderToFileCopyLog to set
		 */
		public void setFolderToFileCopyLog(String folderToFileCopyLog) {
			this.folderToFileCopyLog = folderToFileCopyLog;
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
		 * @return the folderMoveLog
		 */
		public String getFolderMoveLog() {
			return folderMoveLog;
		}
		/**
		 * @param folderMoveLog the folderMoveLog to set
		 */
		public void setFolderMoveLog(String folderMoveLog) {
			this.folderMoveLog = folderMoveLog;
		}
		/**
		 * @return the weblinkCreateLog
		 */
		public String getWeblinkCreateLog() {
			return weblinkCreateLog;
		}
		/**
		 * @param weblinkCreateLog the weblinkCreateLog to set
		 */
		public void setWeblinkCreateLog(String weblinkCreateLog) {
			this.weblinkCreateLog = weblinkCreateLog;
		}
		/**
		 * @return the weblinkUpdateLog
		 */
		public String getWeblinkUpdateLog() {
			return weblinkUpdateLog;
		}
		/**
		 * @param weblinkUpdateLog the weblinkUpdateLog to set
		 */
		public void setWeblinkUpdateLog(String weblinkUpdateLog) {
			this.weblinkUpdateLog = weblinkUpdateLog;
		}
		/**
		 * @return the weblinkDeleteLog
		 */
		public String getWeblinkDeleteLog() {
			return weblinkDeleteLog;
		}
		/**
		 * @param weblinkDeleteLog the weblinkDeleteLog to set
		 */
		public void setWeblinkDeleteLog(String weblinkDeleteLog) {
			this.weblinkDeleteLog = weblinkDeleteLog;
		}
		/**
		 * @return the weblinkRestoreLog
		 */
		public String getWeblinkRestoreLog() {
			return weblinkRestoreLog;
		}
		/**
		 * @param weblinkRestoreLog the weblinkRestoreLog to set
		 */
		public void setWeblinkRestoreLog(String weblinkRestoreLog) {
			this.weblinkRestoreLog = weblinkRestoreLog;
		}
		/**
		 * @return the weblinkCopyLog
		 */
		public String getWeblinkCopyLog() {
			return weblinkCopyLog;
		}
		/**
		 * @param weblinkCopyLog the weblinkCopyLog to set
		 */
		public void setWeblinkCopyLog(String weblinkCopyLog) {
			this.weblinkCopyLog = weblinkCopyLog;
		}
		/**
		 * @return the weblinkMoveLog
		 */
		public String getWeblinkMoveLog() {
			return weblinkMoveLog;
		}
		/**
		 * @param weblinkMoveLog the weblinkMoveLog to set
		 */
		public void setWeblinkMoveLog(String weblinkMoveLog) {
			this.weblinkMoveLog = weblinkMoveLog;
		}
		/**
		 * @return the weblinkShareExpirationLog
		 */
		public String getWeblinkShareExpirationLog() {
			return weblinkShareExpirationLog;
		}
		/**
		 * @param weblinkShareExpirationLog the weblinkShareExpirationLog to set
		 */
		public void setWeblinkShareExpirationLog(String weblinkShareExpirationLog) {
			this.weblinkShareExpirationLog = weblinkShareExpirationLog;
		}
		/**
		 * @return the weblinkShareLog
		 */
		public String getWeblinkShareLog() {
			return weblinkShareLog;
		}
		/**
		 * @param weblinkShareLog the weblinkShareLog to set
		 */
		public void setWeblinkShareLog(String weblinkShareLog) {
			this.weblinkShareLog = weblinkShareLog;
		}
		/**
		 * @return the weblinkUnshareLog
		 */
		public String getWeblinkUnshareLog() {
			return weblinkUnshareLog;
		}
		/**
		 * @param weblinkUnshareLog the weblinkUnshareLog to set
		 */
		public void setWeblinkUnshareLog(String weblinkUnshareLog) {
			this.weblinkUnshareLog = weblinkUnshareLog;
		}
		/**
		 * @return the weblinkSharePermissionsLog
		 */
		public String getWeblinkSharePermissionsLog() {
			return weblinkSharePermissionsLog;
		}
		/**
		 * @param weblinkSharePermissionsLog the weblinkSharePermissionsLog to set
		 */
		public void setWeblinkSharePermissionsLog(String weblinkSharePermissionsLog) {
			this.weblinkSharePermissionsLog = weblinkSharePermissionsLog;
		}
		/**
		 * @return the groupCreateLog
		 */
		public String getGroupCreateLog() {
			return groupCreateLog;
		}
		/**
		 * @param groupCreateLog the groupCreateLog to set
		 */
		public void setGroupCreateLog(String groupCreateLog) {
			this.groupCreateLog = groupCreateLog;
		}
		/**
		 * @return the groupEditLog
		 */
		public String getGroupEditLog() {
			return groupEditLog;
		}
		/**
		 * @param groupEditLog the groupEditLog to set
		 */
		public void setGroupEditLog(String groupEditLog) {
			this.groupEditLog = groupEditLog;
		}
		/**
		 * @return the groupDeleteLog
		 */
		public String getGroupDeleteLog() {
			return groupDeleteLog;
		}
		/**
		 * @param groupDeleteLog the groupDeleteLog to set
		 */
		public void setGroupDeleteLog(String groupDeleteLog) {
			this.groupDeleteLog = groupDeleteLog;
		}
		/**
		 * @return the groupUserDeleteLog
		 */
		public String getGroupUserDeleteLog() {
			return groupUserDeleteLog;
		}
		/**
		 * @param groupUserDeleteLog the groupUserDeleteLog to set
		 */
		public void setGroupUserDeleteLog(String groupUserDeleteLog) {
			this.groupUserDeleteLog = groupUserDeleteLog;
		}
		/**
		 * @return the groupUserAddLog
		 */
		public String getGroupUserAddLog() {
			return groupUserAddLog;
		}
		/**
		 * @param groupUserAddLog the groupUserAddLog to set
		 */
		public void setGroupUserAddLog(String groupUserAddLog) {
			this.groupUserAddLog = groupUserAddLog;
		}
		/**
		 * @return the userCreateLog
		 */
		public String getUserCreateLog() {
			return userCreateLog;
		}
		/**
		 * @param userCreateLog the userCreateLog to set
		 */
		public void setUserCreateLog(String userCreateLog) {
			this.userCreateLog = userCreateLog;
		}
		/**
		 * @return the userUpdateLog
		 */
		public String getUserUpdateLog() {
			return userUpdateLog;
		}
		/**
		 * @param userUpdateLog the userUpdateLog to set
		 */
		public void setUserUpdateLog(String userUpdateLog) {
			this.userUpdateLog = userUpdateLog;
		}
		/**
		 * @return the userDeleteLog
		 */
		public String getUserDeleteLog() {
			return userDeleteLog;
		}
		/**
		 * @param userDeleteLog the userDeleteLog to set
		 */
		public void setUserDeleteLog(String userDeleteLog) {
			this.userDeleteLog = userDeleteLog;
		}
		/**
		 * @return the userRoleChangeLog
		 */
		public String getUserRoleChangeLog() {
			return userRoleChangeLog;
		}
		/**
		 * @param userRoleChangeLog the userRoleChangeLog to set
		 */
		public void setUserRoleChangeLog(String userRoleChangeLog) {
			this.userRoleChangeLog = userRoleChangeLog;
		}
		/**
		 * @return the inviteCollaborator
		 */
		public String getInviteCollaborator() {
			return inviteCollaborator;
		}
		/**
		 * @param inviteCollaborator the inviteCollaborator to set
		 */
		public void setInviteCollaborator(String inviteCollaborator) {
			this.inviteCollaborator = inviteCollaborator;
		}
		/**
		 * @return the removeCollaborator
		 */
		public String getRemoveCollaborator() {
			return removeCollaborator;
		}
		/**
		 * @param removeCollaborator the removeCollaborator to set
		 */
		public void setRemoveCollaborator(String removeCollaborator) {
			this.removeCollaborator = removeCollaborator;
		}
		/**
		 * @return the acceptCollaboration
		 */
		public String getAcceptCollaboration() {
			return acceptCollaboration;
		}
		/**
		 * @param acceptCollaboration the acceptCollaboration to set
		 */
		public void setAcceptCollaboration(String acceptCollaboration) {
			this.acceptCollaboration = acceptCollaboration;
		}
		/**
		 * @return the addGroupCollaboration
		 */
		public String getAddGroupCollaboration() {
			return addGroupCollaboration;
		}
		/**
		 * @param addGroupCollaboration the addGroupCollaboration to set
		 */
		public void setAddGroupCollaboration(String addGroupCollaboration) {
			this.addGroupCollaboration = addGroupCollaboration;
		}
		/**
		 * @return the removeGroupCollaboration
		 */
		public String getRemoveGroupCollaboration() {
			return removeGroupCollaboration;
		}
		/**
		 * @param removeGroupCollaboration the removeGroupCollaboration to set
		 */
		public void setRemoveGroupCollaboration(String removeGroupCollaboration) {
			this.removeGroupCollaboration = removeGroupCollaboration;
		}
		/**
		 * @return the transferOfOwnerShip
		 */
		public String getTransferOfOwnerShip() {
			return transferOfOwnerShip;
		}
		/**
		 * @param transferOfOwnerShip the transferOfOwnerShip to set
		 */
		public void setTransferOfOwnerShip(String transferOfOwnerShip) {
			this.transferOfOwnerShip = transferOfOwnerShip;
		}
		/**
		 * @return the collaborationRoleChange
		 */
		public String getCollaborationRoleChange() {
			return collaborationRoleChange;
		}
		/**
		 * @param collaborationRoleChange the collaborationRoleChange to set
		 */
		public void setCollaborationRoleChange(String collaborationRoleChange) {
			this.collaborationRoleChange = collaborationRoleChange;
		}
		

}
