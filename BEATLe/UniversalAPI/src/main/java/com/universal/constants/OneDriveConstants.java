package com.universal.constants;

public class OneDriveConstants {
	
	//Authorization urls
	public static final String ONEDRIVE_OAUTH20_AUTHORIZE_URL 					= "https://login.live.com/oauth20_authorize.srf";
	public static final String ONEDRIVE_OAUTH20_TOKEN_URL 						= "https://login.live.com/oauth20_token.srf";
	public static final String ONEDRIVE_OAUTH20_DESKTOP_REDIRECT_URL 			= "https://login.live.com/oauth20_desktop.srf";
	public static final String ONEDRIVE_GRANT_TYPE_AUTHORIZATION_CODE 			= "authorization_code";
	public static final String ONEDRIVE_GRANT_TYPE_REFRESH_TOKEN 				= "refresh_token";	
	
	//API Paths	
	public static final String ONEDRIVE_API_ROOT  		   						= "https://api.onedrive.com/v1.0";			
	public static final String ONEDRIVE_API_DEFAULT_DRIVE      					= "/drive";
	public static final String ONEDRIVE_API_DRIVES	  		   					= "/drives";
	public static final String ONEDRIVE_API_DRIVE_ROOT_CHILDREN					= "/drive/root/children";
	public static final String ONEDRIVE_API_DRIVE_BY_ID        					= "/drives/{drive-id}";
	public static final String ONEDRIVE_API_GET_CHILDRENS_BY_ITEMID				= "/drive/items/{item-id}/children";
	
	
	/** File Upload APIS **/	
	public static final String ONEDRIVE_API_SIMPLE_UPLOAD_BY_PARENT_ID	 		= "/drive/items/{parent-id}:/{filename}:/content";
	public static final String ONEDRIVE_API_SIMPLE_UPLOAD_BY_PARENT_PATH		= "/drive/root:/{parent-path}/{filename}:/content";
	public static final String ONEDRIVE_API_SIMPLE_UPLOAD_BY_CHILDREN_PATH		= "/drive/items/{parent-id}/children/{filename}/content";
	public static final String ONEDRIVE_API_MULTIPART_UPLOAD_BY_CHILDREN_PATH		= "/drive/items/{parent-id}/children/{filename}/content";
	
	/**File download apis **/	 
	public static final String ONEDRIVE_API_DOWNLOAD_BY_ITEM_ID	 				= "/drive/items/{item-id}/content";
	public static final String ONEDRIVE_API_DOWNLOAD_BY_PATH_AND_FILENAME		= "/drive/root:/{path and filename}:/content";

}
