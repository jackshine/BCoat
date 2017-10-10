package com.universal.constants;

public class OneDriveBusinessConstants {
	
	
	public static final String ONEDRIVEBUSINESS_OAUTH20_HOST					= "login.windows.net";
	//public static final String ONEDRIVEBUSINESS_API_HOST						= "securleto365beatle-my.sharepoint.com";
	public static final String ONEDRIVEBUSINESS_API_HOST						= "{tenant}-my.sharepoint.com";
	public static final String ONEDRIVEBUSINESS_OAUTH20_AUTHORIZE_URL 			= "/common/oauth2/authorize";	
	public static final String ONEDRIVEBUSINESS_OAUTH20_TOKEN_URL 				= "/common/oauth2/token";	
	public static final String ONEDRIVEBUSINESS_OAUTH20_DESKTOP_REDIRECT_URL 	= "http://login.elastica.net/oauth20_desktop.srf";
	public static final String ONEDRIVEBUSINESS_GRANT_TYPE_AUTHORIZATION_CODE 	= "authorization_code";
	public static final String ONEDRIVEBUSINESS_GRANT_TYPE_REFRESH_TOKEN 		= "refresh_token";	
	public static final String ONEDRIVEBUSINESS_AUTHORIZE_URL					= "onedrivebusiness.authorization.url";
	public static final String ONEDRIVEBUSINESS_RESOURCE_URI					= "https://{tenant}-my.sharepoint.com/";
	public static final String ONEDRIVEBUSINESS_SITECOLLECTION_RESOURCE_URI		= "https://{tenant}.sharepoint.com/";
	//public static final String ONEDRIVEBUSINESS_RESOURCE_URI					= "https://securleto365beatle-my.sharepoint.com/";
	
	//Graph apis
	public static final String ONEDRIVEGRAPH_API_HOST							= "graph.windows.net";
	public static final String ONEDRIVEGRAPH_RESOURCE_URI						= "https://graph.windows.net/";
	
	
	
	//Authorization urls
	
	public static final String ONEDRIVEBUSINESS_OAUTH20_AUTHORIZE_TENANT_URL			= "https://login.windows.net/{tenantid}/oauth2/authorize";
	public static final String ONEDRIVEBUSINESS_OAUTH20_TOKEN_TENANT_URL				= "https://login.windows.net/{tenantid}/oauth2/token";
	
	public static final String ONEDRIVEBUSINESS_ADMIN_USERNAME	 						= "onedrivebusiness.admin.username";
	public static final String ONEDRIVEBUSINESS_ADMIN_PASSWORD	 						= "onedrivebusiness.admin.password";
	
	//API Paths	
	public static final String ONEDRIVEBUSINESS_API_ROOT  		   						= "https://api.onedrive.com/v1.0";			
	public static final String ONEDRIVEBUSINESS_API_DEFAULT_DRIVE      					= "onedrivebusiness.uri.getRootFolder";
	
	/** File Upload APIS **/	
	public static final String ONEDRIVEBUSINESS_API_UPLOAD_BY_PARENT_ID	 				= "onedrivebusiness.uri.uploadFile";
	public static final String ONEDRIVEBUSINESS_API_FILEINFO_URI 						= "onedrivebusiness.uri.fileInfo";
	public static final String ONEDRIVEBUSINESS_API_CREATEFILE_URI 						= "/_api/v1.0/me/Files";
	public static final String ONEDRIVEBUSINESS_API_UPDATEFILE_URI 						= "/_api/v1.0/me/Files/{fileid}/uploadContent";
	
	
	
	public static final String ONEDRIVEBUSINESS_API_CREATEFOLDER_URI 					= "onedrivebusiness.uri.createFolder";
	public static final String ONEDRIVEBUSINESS_API_DELETEFOLDER_URI 					= "onedrivebusiness.uri.deleteFolder";
	
	public static final String ONEDRIVEBUSINESS_API_CREATEFOLDERV2_URI 					= "onedrivebusiness.uri.createFolderV2";
	public static final String ONEDRIVEBUSINESS_API_DELETEFOLDERV2_URI 					= "onedrivebusiness.uri.deleteFolderV2";
	public static final String ONEDRIVEBUSINESS_API_CREATESUBFOLDERV2_URI 				= "onedrivebusiness.uri.createSubFolderV2";
	
	public static final String ONEDRIVEBUSINESS_API_GETFOLDERPROPERTIES_URI 			= "/_api/v1.0/me/Files/{folderid}";
	public static final String ONEDRIVEBUSINESS_API_UPDATEFOLDERPROPERTIES_URI 			= "/_api/v1.0/me/Files/{folderid}";
	
	
	
	
	
	
	public static final String ONEDRIVEBUSINESS_API_SIMPLE_UPLOAD_BY_PARENT_PATH		= "/drive/root:/{parent-path}/{filename}:/content";
	public static final String ONEDRIVEBUSINESS_API_SIMPLE_UPLOAD_BY_CHILDREN_PATH		= "/drive/items/{parent-id}/children/{filename}/content";
	public static final String ONEDRIVEBUSINESS_API_MULTIPART_UPLOAD_BY_CHILDREN_PATH	= "/drive/items/{parent-id}/children/{filename}/content";
	
	/**File download apis **/	 
	public static final String ONEDRIVEBUSINESS_API_DOWNLOAD_BY_ITEM_ID	 				= "onedrivebusiness.uri.getDownloadFile";
	public static final String ONEDRIVEBUSINESS_API_DOWNLOAD_BY_ITEM_IDV2_URI	 		= "onedrivebusiness.uri.getDownloadFileV2";
	
	public static final String ONEDRIVEBUSINESS_API_DOWNLOAD_BY_PATH_AND_FILENAME		= "/drive/root:/{path and filename}:/content";
	
	
	
	public static final String ONEDRIVEBUSINESS_API_DRIVES	  		   					= "/drives";
	public static final String ONEDRIVEBUSINESS_API_DRIVE_ROOT_CHILDREN					= "/drive/root/children";
	public static final String ONEDRIVEBUSINESS_API_DRIVE_BY_ID        					= "/drives/{drive-id}";
	public static final String ONEDRIVEBUSINESS_API_GET_CHILDRENS_BY_ITEMID				= "/drive/items/{item-id}/children";
	public static final String ONEDRIVEBUSINESS_UPLOADSFOLDER_PATH 						= "onedrivebusiness.uploadsfolder.path";
	public static final String ONEDRIVEBUSINESS_DOWNLOADSFOLDER_PATH 					= "onedrivebusiness.downloadsfolder.path";
	
	
	public static final String ONEDRIVEBUSINESS_API_UPDATE_ITEM_PATH					= "onedrivebusiness.uri.updateItem";
	public static final String ONEDRIVEBUSINESS_API_CREATE_SHAREDLINK_PATH				= "onedrivebusiness.uri.createSharedLink";
	public static final String ONEDRIVEBUSINESS_API_COPYITEM_PATH						= "onedrivebusiness.uri.copyItem";
	public static final String ONEDRIVEBUSINESS_API_MOVEITEM_PATH						= "onedrivebusiness.uri.moveItem";
	
	
	//Graph API's
	public static final String ONEDRIVEGRAPH_API_LIST_USERS_PATH						= "/{domain}/users";
	public static final String ONEDRIVEGRAPH_API_CREATE_USERS_PATH						= "/{domain}/users";
	public static final String ONEDRIVEGRAPH_API_DELETE_USERS_PATH						= "/{domain}/users/{user-id}";
	public static final String ONEDRIVEGRAPH_API_UPDATE_USERS_PATH						= "/{domain}/users/{user-id}";
	
	
	//Sharepoint uri's
	public static final String ONEDRIVESP_API_LIST_USERS_PATH							= "/Lists";
	public static final String ONEDRIVESP_API_UPDATE_LIST_PATH							= "/Lists(guid'{guid}')";
	public static final String ONEDRIVESP_API_GET_LIST_ITEMS							= "/Lists(guid'{guid}')/items";
	
	public static final String ONEDRIVESP_API_USER_ROLE_ASSIGNMENT						= "/SP.Sharing.DocumentSharingManager.UpdateDocumentSharingInfo";
	public static final String ONEDRIVESP_API_SITEUSERS									= "/siteusers";
	public static final String ONEDRIVESP_API_ROLEDEFINITIONS							= "/roledefinitions";
	public static final String ONEDRIVESP_API_ROLEASSIGNMENTS							= "/RoleAssignments";
	public static final String ONEDRIVESP_API_BREAKROLEINHERITANCE						= "/breakroleinheritance";
	public static final String ONEDRIVESP_API_RESETROLEINHERITANCE						= "/{itemlink}/resetroleinheritance";
	public static final String ONEDRIVESP_API_BREAKITEMROLEINHERITANCE					= "/{itemlink}/breakroleinheritance";
	public static final String ONEDRIVESP_API_ITEMROLEASSIGNMENTS						= "/GetFileByServerRelativeUrl('{filename}')/ListItemAllFields/roleassignments";
	public static final String ONEDRIVESP_API_FOLDERROLEASSIGNMENTS						= "/GetFolderByServerRelativeUrl('{foldername}')/ListItemAllFields/roleassignments";
	public static final String ONEDRIVESP_API_ADDLISTROLEASSIGNMENT						= "/roleassignments/addroleassignment(principalid={principalid},roleDefId={roleDefId})";
	public static final String ONEDRIVESP_API_ADDLISTITEMROLEASSIGNMENT					= "/{itemlink}/roleassignments/addroleassignment(principalid={principalid},roleDefId={roledefid})";
	public static final String ONEDRIVESP_API_RECYCLEBIN								= "/recyclebin";
	public static final String ONEDRIVESP_API_RECYCLEBIN_ITEM_RESTORE					= "/recyclebin(guid'{fileId}')/Restore";
	public static final String ONEDRIVESP_API_RECYCLEBIN_ITEM_RECYCLE					= "/GetFileByServerRelativeUrl('{name}')/recycle";
	public static final String ONEDRIVESP_API_RECYCLEBIN_FOLDER_RECYCLE					= "/GetFolderByServerRelativeUrl('{name}')/recycle";
	public static final String ONEDRIVESP_API_FILEITEMBYRELATIVEURL						= "/GetFileByServerRelativeUrl('{name}')/ListItemAllFields";
	public static final String ONEDRIVESP_API_FOLDERITEMBYRELATIVEURL					= "/GetFolderByServerRelativeUrl('{name}')/ListItemAllFields";
	public static final String ONEDRIVESP_API_EDITITEM									= "/{editlink}";
	public static final String ONEDRIVESP_API_COPYITEM									= "/GetFileById('{fileid}')/copyto(strnewurl='{destination}',boverwrite={boverwrite})";
	public static final String ONEDRIVESP_API_MOVEITEM									= "/GetFileById('{fileid}')/moveto(newurl='{destination}',flags={boverwrite})";
	public static final String ONEDRIVESP_API_CREATE_SITE								= "/_api/web/webinfos/add";
	public static final String ONEDRIVESP_API_ADD_SITE									= "/_api/web/webs/add";
	public static final String ONEDRIVESP_API_DELETE_SITE								= "/{siteurl}/_api/web";
	public static final String ONEDRIVESP_API_UPDATE_SITE								= "/{siteurl}/_api/web";
	public static final String ONEDRIVESP_API_SITE_LIST									= "/{siteurl}/_api/web/Lists";
	public static final String ONEDRIVESP_API_LISTITEMALLFIELDS							= "/Lists/GetByTitle('{listname}')/rootfolder/files/getbyurl(url='{fileurl}')/listitemallfields";
	
	//SD => shared documents
	public static final String ONEDRIVESP_API_SITE_SD_FILE_UPLOAD						= "/{siteurl}/_api/web/Lists(guid'{guid}')/rootfolder/Files/Add(url='{filename}',overwrite=true)";
	
	
	
	public static final String ONEDRIVESP_API_ROOTSITE_USERS							= "/sites/{siteurl}/_api/web/siteusers";
	public static final String ONEDRIVESP_API_ROOTSITE_ROLEDEFINITIONS					= "/sites/{siteurl}/_api/web/roledefinitions";
	public static final String ONEDRIVESP_API_ROOTSITE_ROLEASSIGNMENTS					= "/sites/{siteurl}/_api/web/RoleAssignments";
	public static final String ONEDRIVESP_API_ROOTSITE_RECYCLEBIN						= "/sites/{siteurl}/_api/web/recyclebin";
	public static final String ONEDRIVESP_API_ROOTSITE_FILE_UPLOAD						= "/sites/{siteurl}/_api/web/GetFolderByServerRelativeUrl('/sites/{siteurl}/{foldername}')/Files/Add(url='{filename}',overwrite=true)";
	public static final String ONEDRIVESP_API_ROOTSITE_LISTS							= "/sites/{siteurl}/_api/web/lists";
	public static final String ONEDRIVESP_API_ROOTSITE_LIST_ITEMS						= "/sites/{siteurl}/_api/web/lists(guid'{guid}')/items";
	public static final String ONEDRIVESP_API_ROOTSITE_LIST_FILES						= "/sites/{siteurl}/_api/web/GetFolderByServerRelativeUrl('/sites/{siteurl}/{foldername}')/Files";
	
	
	
	//"/sites/{siteurl}/_api/web/lists/GetFileByServerRelativeUrl('/Sites/securletautomationsite/Shared%20Documents/HIPAA_Test2.txt')/ListItemAllFields";
	public static final String ONEDRIVESP_API_ROOTSITE_LISTITEMALLFIELDS				= "/sites/{siteurl}/_api/web/GetFileByServerRelativeUrl('/Sites/{siteurl}/{listname}/{filename}')/ListItemAllFields";
	
	
	//Itemlink will have complete url of the items
	
	public static final String ONEDRIVESP_API_ROOTSITE_CREATE_FOLDER					= "/sites/{rootsiteurl}/_api/web/folders";
	public static final String ONEDRIVESP_API_ROOTSITE_FOLDER							= "/sites/{rootsiteurl}/_api/Web/GetFolderByServerRelativeUrl('{relativeurl}')";
	
	public static final String ONEDRIVESP_API_ROOTSITE_RESETROLEINHERITANCE				= "/sites/{rootsiteurl}/_api/{itemlink}/resetroleinheritance";
	public static final String ONEDRIVESP_API_ROOTSITE_BREAKITEMROLEINHERITANCE			= "/sites/{rootsiteurl}/_api/{itemlink}/breakroleinheritance";
	public static final String ONEDRIVESP_API_ROOTSITE_GETLISTITEMROLEASSIGNMENT		= "/sites/{rootsiteurl}/_api/{itemlink}/roleassignments";
	public static final String ONEDRIVESP_API_ROOTSITE_ADDLISTITEMROLEASSIGNMENT		= "/sites/{rootsiteurl}/_api/{itemlink}/roleassignments/addroleassignment(principalid={principalid},roleDefId={roledefid})";
	public static final String ONEDRIVESP_API_ROOTSITE_REMOVELISTITEMROLEASSIGNMENT		= "/sites/{rootsiteurl}/_api/{itemlink}/roleassignments/removeroleassignment(principalid={principalid},roleDefId={roledefid})";
	public static final String ONEDRIVESP_API_ROOTSITE_FILE_SHAREDLINK					= "/sites/{rootsiteurl}/_api/SP.Sharing.DocumentSharingManager.UpdateDocumentSharingInfo";
	public static final String ONEDRIVESP_API_ROOTSITE_FILE_PREAUTHORIZED_ACCESS_URL	= "/sites/{rootsiteurl}/_api/Web/GetFileByServerRelativeUrl('{filerelativeurl}')/GetPreAuthorizedAccessUrl({expirytime})";
	public static final String ONEDRIVESP_API_ROOTSITE_RECYCLEBIN_ITEM_RESTORE			= "/sites/{rootsiteurl}/_api/Web/recyclebin(guid'{id}')/Restore";
	public static final String ONEDRIVESP_API_ROOTSITE_RECYCLEBIN_ITEM_RECYCLE			= "/sites/{rootsiteurl}/_api/Web/GetFileByServerRelativeUrl('{relativeurl}')/recycle";
	
	public static final String ONEDRIVESP_API_ROOTSITE_RECYCLEBIN_FOLDER_RESTORE		= "/sites/{rootsiteurl}/_api/Web/recyclebin(guid'{id}')/Restore";
	public static final String ONEDRIVESP_API_ROOTSITE_RECYCLEBIN_FOLDER_RECYCLE		= "/sites/{rootsiteurl}/_api/Web/GetFolderByServerRelativeUrl('{relativeurl}')/recycle";
	public static final String ONEDRIVESP_API_ROOTSITE_LISTITEMALLFIELDSFOLDER			= "/sites/{rootsiteurl}/_api/web/GetFolderByServerRelativeUrl('{relativeurl}')/ListItemAllFields";
	
	//https://securletdddo365beatle.sharepoint.com/sites/securletautomationsite/_api/Web/GetFolderByServerRelativeUrl('/sites/securletautomationsite/Shared%20Documents/NewName')/ListItemAllFields
		
	//
	//public static final String ONEDRIVESP_API_SITE_SD_FILE_UPLOAD						= "/{siteurl}/_api/web/getbytitle('Documents')/rootfolder/Files/Add(url='{filename}',overwrite=true)";
	
	
	//https://securletdddo365beatle.sharepoint.com/sites/securletautomationsite/_api/web/siteusers
	
	//Subsite endpoints
	public static final String ONEDRIVESP_API_SUBSITE_LIST								= "/{subsiteurl}/_api/web/Lists";
	
	//SD => shared documents
	public static final String ONEDRIVESP_API_SUBSITE_SD_FILE_UPLOAD					= "/{subsiteurl}/_api/web/Lists(guid'{guid}')/rootfolder/Files/Add(url='{filename}',overwrite=true)";
	public static final String ONEDRIVESP_API_SUBSITE_USERS								= "/{subsiteurl}/_api/web/siteusers";
	public static final String ONEDRIVESP_API_SUBSITE_ROLEDEFINITIONS					= "/{subsiteurl}/_api/web/roledefinitions";
	public static final String ONEDRIVESP_API_SUBSITE_ROLEASSIGNMENTS					= "/{subsiteurl}/_api/web/RoleAssignments";
	public static final String ONEDRIVESP_API_SUBSITE_RECYCLEBIN						= "/{subsiteurl}/_api/web/recyclebin";
	public static final String ONEDRIVESP_API_SUBSITE_FILE_UPLOAD						= "/{subsiteurl}/_api/web/GetFolderByServerRelativeUrl('/{subsiteurl}/{foldername}')/Files/Add(url='{filename}',overwrite=true)";
	public static final String ONEDRIVESP_API_SUBSITE_LISTS								= "/{subsiteurl}/_api/web/lists";
	public static final String ONEDRIVESP_API_SUBSITE_LIST_ITEMS						= "/{subsiteurl}/_api/web/lists(guid'{guid}')/items";
	public static final String ONEDRIVESP_API_SUBSITE_LIST_FILES						= "/{subsiteurl}/_api/web/GetFolderByServerRelativeUrl('/{subsiteurl}/{foldername}')/Files";
	//"/sites/{siteurl}/_api/web/lists/GetFileByServerRelativeUrl('/Sites/securletautomationsite/Shared%20Documents/HIPAA_Test2.txt')/ListItemAllFields";
	public static final String ONEDRIVESP_API_SUBSITE_LISTITEMALLFIELDS					= "/{subsiteurl}/_api/web/GetFileByServerRelativeUrl('/{subsiteurl}/{listname}/{filename}')/ListItemAllFields";
	
	
	//Itemlink will have complete url of the items
	
	public static final String ONEDRIVESP_API_SUBSITE_CREATE_FOLDER						= "/{subsiteurl}/_api/web/folders";
	public static final String ONEDRIVESP_API_SUBSITE_FOLDER							= "/{subsiteurl}/_api/Web/GetFolderByServerRelativeUrl('{relativeurl}')";
	
	public static final String ONEDRIVESP_API_SUBSITE_RESETROLEINHERITANCE				= "/{subsiteurl}/_api/{itemlink}/resetroleinheritance";
	public static final String ONEDRIVESP_API_SUBSITE_BREAKITEMROLEINHERITANCE			= "/{subsiteurl}/_api/{itemlink}/breakroleinheritance";
	public static final String ONEDRIVESP_API_SUBSITE_GETLISTITEMROLEASSIGNMENT			= "/{subsiteurl}/_api/{itemlink}/roleassignments";
	public static final String ONEDRIVESP_API_SUBSITE_ADDLISTITEMROLEASSIGNMENT			= "/{subsiteurl}/_api/{itemlink}/roleassignments/addroleassignment(principalid={principalid},roleDefId={roledefid})";
	public static final String ONEDRIVESP_API_SUBSITE_REMOVELISTITEMROLEASSIGNMENT		= "/{subsiteurl}/_api/{itemlink}/roleassignments/removeroleassignment(principalid={principalid},roleDefId={roledefid})";
	public static final String ONEDRIVESP_API_SUBSITE_FILE_SHAREDLINK					= "/{subsiteurl}/_api/SP.Sharing.DocumentSharingManager.UpdateDocumentSharingInfo";
	public static final String ONEDRIVESP_API_SUBSITE_FILE_PREAUTHORIZED_ACCESS_URL		= "/{subsiteurl}/_api/Web/GetFileByServerRelativeUrl('{filerelativeurl}')/GetPreAuthorizedAccessUrl({expirytime})";
	public static final String ONEDRIVESP_API_SUBSITE_RECYCLEBIN_ITEM_RESTORE			= "/{subsiteurl}/_api/Web/recyclebin(guid'{id}')/Restore";
	public static final String ONEDRIVESP_API_SUBSITE_RECYCLEBIN_ITEM_RECYCLE			= "/{subsiteurl}/_api/Web/GetFileByServerRelativeUrl('{relativeurl}')/recycle";
	
	public static final String ONEDRIVESP_API_SUBSITE_RECYCLEBIN_FOLDER_RESTORE			= "/{subsiteurl}/_api/Web/recyclebin(guid'{id}')/Restore";
	public static final String ONEDRIVESP_API_SUBSITE_RECYCLEBIN_FOLDER_RECYCLE			= "/{subsiteurl}/_api/Web/GetFolderByServerRelativeUrl('{relativeurl}')/recycle";
	public static final String ONEDRIVESP_API_SUBSITE_LISTITEMALLFIELDSFOLDER			= "/{subsiteurl}/_api/web/GetFolderByServerRelativeUrl('{relativeurl}')/ListItemAllFields";
	
	
	
	
}
