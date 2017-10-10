package com.universal.constants;

public class BoxConstants {
	
	
	public static final String DEFAULT_DRIVE_URI = "box.uri.getDefaultDrive";
	public static final String BOX_UPLOADSFOLDER_PATH = "box.uploadsfolder.path";
	public static final String BOX_DOWNLOADSFOLDER_PATH = "box.downloadsfolder.path";
	
	
	
	//Authorization urls
	public static final String BOX_OAUTH20_HOST						= "app.box.com";
	public static final String BOX_API_HOST							= "api.box.com";
	public static final String BOX_OAUTH20_AUTHORIZE_URL 			= "/api/oauth2/authorize";	
	public static final String BOX_OAUTH20_TOKEN_URL 				= "/api/oauth2/token";
	public static final String BOX_OAUTH20_REVOKE_URL				= "/oauth2/revoke";
	public static final String BOX_OAUTH20_DESKTOP_REDIRECT_URL 	= "http://login.elastica.net/oauth20_desktop.srf";
	public static final String BOX_GRANT_TYPE_AUTHORIZATION_CODE 	= "authorization_code";
	public static final String BOX_GRANT_TYPE_REFRESH_TOKEN 		= "refresh_token";	
	public static final String BOX_AUTHORIZE_URL					= "box.authorization.url";
	
	
	//API Paths	
	public static final String BOX_API_USERINFO  		   			= "/2.0/users/me";
	public static final String BOX_API_ROOT  		   				= "https://api.onedrive.com/v1.0";
	public static final String BOX_API_UPLOAD_HOST  		   		= "upload.box.com";
	public static final String BOX_API_UPLOADCONTENT_URI 			= "/api/2.0/files/content";
	public static final String BOX_API_DOWNLOADCONTENT_URI 			= "box.uri.getDownloadFile";
	public static final String BOX_API_FILEINFO_URI 			    = "box.uri.getFileInfo";
	public static final String BOX_API_FILEFROMTRASH_URI			= "box.uri.getFileFromTrash";
	public static final String BOX_API_UPDATE_FILE_URI			    = "box.uri.updateFile";
	public static final String BOX_API_COPY_FILE_URI			    = "box.uri.copyFile";
	public static final String BOX_API_PREVIEW_FILE_URI			    = "box.uri.previewFile";
	
	
	//API Paths for folders
	public static final String BOX_API_CREATEFOLDER_URI			    = "box.uri.createFolder";
	public static final String BOX_API_FOLDERINFO_URI 			    = "box.uri.getFolderInfo";
	public static final String BOX_API_UPDATEFOLDER_URI				= "box.uri.updateFolder";
	public static final String BOX_API_DELETEFOLDER_URI			    = "box.uri.deleteFolder";
	public static final String BOX_API_COPYFOLDER_URI			    = "box.uri.copyFolder";
	
	public static final String BOX_API_FOLDERITEMS_URI			    = "box.uri.getFolderItems";
	public static final String BOX_API_FOLDERCOLLABORATIONS_URI		= "box.uri.folderCollaborations";
	public static final String BOX_API_FOLDERTRASHEDITEMS_URI		= "box.uri.getFolderTrashedItems";
	public static final String BOX_API_TRASHEDFOLDER_URI			= "box.uri.trashedFolder";
	public static final String BOX_API_RESTOREFOLDER_URI			= "box.uri.restoreFolder";
	public static final String BOX_API_TRANSFEROWNER_URI			= "box.uri.transferOwner";
	public static final String BOX_API_INVITEUSER_URI				= "box.uri.inviteUser";
	
	
	//API paths for users
	public static final String BOX_API_CREATEUSER_URI 				= "box.uri.createUser";
	public static final String BOX_API_UPDATEUSER_URI 				= "box.uri.updateUser";
	public static final String BOX_API_DELETEUSER_URI 				= "box.uri.deleteUser";
	public static final String BOX_API_LISTUSER_URI 				= "box.uri.listUser";
	
	
	//API paths for groups
	public static final String BOX_API_CREATEGROUPS_URI 			= "box.uri.createGroup";
	public static final String BOX_API_UPDATEGROUPS_URI 			= "box.uri.updateGroup";
	public static final String BOX_API_DELETEGROUPS_URI 			= "box.uri.deleteGroup";

	//API paths for memberships
	public static final String BOX_API_CREATEMEMBERSHIPS_URI 		= "box.uri.createMembership";
	public static final String BOX_API_UPDATEMEMBERSHIPS_URI 		= "box.uri.updateMembership";
	public static final String BOX_API_DELETEMEMBERSHIPS_URI 		= "box.uri.deleteMembership";
	
	public static final String BOX_API_CREATECOLLABORATIONS_URI 	= "box.uri.createCollaboration";
	public static final String BOX_API_UPDATECOLLABORATIONS_URI 	= "box.uri.updateCollaboration";
	public static final String BOX_API_DELETECOLLABORATIONS_URI 	= "box.uri.deleteCollaboration";
	
	public static final String BOX_API_CREATEWEBLINK_URI 			= "box.uri.createWeblink";
	public static final String BOX_API_UPDATEWEBLINK_URI 			= "box.uri.updateWeblink";
	public static final String BOX_API_DELETEWEBLINK_URI 			= "box.uri.deleteWeblink";
	public static final String BOX_API_COPYWEBLINK_URI			    = "box.uri.copyWeblink";
	//Box Account
	public static final String BOX_ADMIN_USERNAME	 				= "box.admin.username";
	public static final String BOX_ADMIN_PASSWORD	 				= "box.admin.password";
	
	public static final String BOX_API_DEFAULT_DRIVE      			= "/drive";
	public static final String BOX_API_DRIVES	  		   			= "/drives";
	public static final String BOX_API_DRIVE_ROOT_CHILDREN			= "/drive/root/children";
	public static final String BOX_API_DRIVE_BY_ID        			= "/drives/{drive-id}";
	public static final String BOX_API_GET_CHILDRENS_BY_ITEMID		= "/drive/items/{item-id}/children";	

	/** File Upload APIS **/	
	public static final String BOX_API_SIMPLE_UPLOAD_BY_PARENT_ID	 		= "/drive/items/{parent-id}:/{filename}:/content";
	public static final String BOX_API_SIMPLE_UPLOAD_BY_PARENT_PATH		= "/drive/root:/{parent-path}/{filename}:/content";
	public static final String BOX_API_SIMPLE_UPLOAD_BY_CHILDREN_PATH		= "/drive/items/{parent-id}/children/{filename}/content";
	public static final String BOX_API_MULTIPART_UPLOAD_BY_CHILDREN_PATH	= "/drive/items/{parent-id}/children/{filename}/content";

	/**File download apis **/	 
	public static final String BOX_API_DOWNLOAD_BY_ITEM_ID	 				= "/drive/items/{item-id}/content";
	public static final String BOX_API_DOWNLOAD_BY_PATH_AND_FILENAME		= "/drive/root:/{path and filename}:/content";
	
	
	/** File Preview API **/
	public static final String BOX_API_FILEPREVIEW_URI 				= "/2.0/files/{file_id}";
	
	
	
	

	

	
}
