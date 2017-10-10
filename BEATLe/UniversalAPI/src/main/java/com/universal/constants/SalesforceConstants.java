package com.universal.constants;

public class SalesforceConstants {
	//Authorization urls
		public static final String SF_OAUTH20_HOST						= "cs52.salesforce.com";
		public static final String SF_API_HOST							= "cs52.salesforce.com";
		public static final String SF_OAUTH20_AUTHORIZE_URL 			= "/services/oauth2/authorize";	
		public static final String SF_OAUTH20_TOKEN_URL 				= "/services/oauth2/token";
		public static final String SF_OAUTH20_REVOKE_URL				= "/oauth2/revoke";
		public static final String SF_OAUTH20_DESKTOP_REDIRECT_URL 		= "http://login.elastica.net/oauth20_desktop.srf";
		public static final String SF_GRANT_TYPE_AUTHORIZATION_CODE 	= "authorization_code";
		public static final String SF_GRANT_TYPE_REFRESH_TOKEN 			= "refresh_token";	
		public static final String SF_AUTHORIZE_URL						= "box.authorization.url";
		
		public static final String SF_API_VERSIONS						= "/services/data/";
		public static final String SF_RESOURCES_BY_VERSIONS				= "/services/data/{version}/";
		public static final String SF_API_QUERY							= "/services/data/v34.0/query";
		public static final String SF_CHATTER_UPLOAD_URI				= "/services/data/v34.0/chatter/users/me/files";
		public static final String SF_CHATTER_GROUP_CREATE_URI			= "/services/data/v34.0/chatter/groups";
		public static final String SF_CHATTER_GROUP_GET_URI				= "/services/data/v34.0/chatter/groups/{groupid}";
		public static final String SF_CHATTER_GROUP_DELETE_URI			= "/services/data/v34.0/chatter/groups/{groupid}";
		
		
		public static final String SF_CHATTER_FEEDELEMENT_CREATE_URI    = "/services/data/v31.0/chatter/feed-elements";
		public static final String SF_CHATTER_FEEDELEMENT_UPDATE_URI    = "/services/data/v34.0/chatter/feed-elements/{feedelementid}";
		
		public static final String SF_CHATTER_COMMENT_CREATE_URI    	= "/services/data/v34.0/chatter/feed-elements/{feedelementid}/capabilities/comments/items";
		public static final String SF_CHATTER_COMMENT_UPDATE_URI    	= "/services/data/v34.0/chatter/comments/{commentid}";
		
		public static final String SF_CHATTER_FEEDELEMENTLIKE_URI    	= "/services/data/v34.0/chatter/feed-elements/{feedelementid}/capabilities/chatter-likes/items";
		
		public static final String SF_CHATTER_FILESHARELINK_URI    		= "/services/data/v34.0/chatter/files/{fileid}/file-shares/link";
		public static final String SF_CHATTER_FILESHARES_URI    		= "/services/data/v34.0/chatter/files/{fileid}/file-shares";
		public static final String SF_CHATTER_CONTENTDOCUMENTLINK_URI   = "/services/data/v32.0/sobjects/ContentDocumentLink";
		
		public static final String SF_CHATTER_FILE_DELETE_URI			= "/services/data/v34.0/chatter/files/{fileid}";
		
		public static final String SF_LEAD_CREATE_URI		    		= "/services/data/v34.0/sobjects/Lead";
		public static final String SF_LEAD_UPDATE_URI		    		= "/services/data/v34.0/sobjects/Lead/{leadid}";
		public static final String SF_LEAD_GET_URI		    			= "/services/data/v34.0/sobjects/Lead/{leadid}";
		public static final String SF_LEAD_DELETE_URI		    		= "/services/data/v34.0/sobjects/Lead/{leadid}";
		
		//To create sobject like Account, Leads, Case, Opportunities, Leads
		public static final String SF_SOBJECT_CREATE_URI		    	= "/services/data/v34.0/sobjects/{sobjectname}";
		public static final String SF_SOBJECT_UPDATE_URI		    	= "/services/data/v34.0/sobjects/{sobjectname}/{sobjectid}";
		public static final String SF_SOBJECT_GET_URI		    		= "/services/data/v34.0/sobjects/{sobjectname}/{sobjectid}";
		public static final String SF_SOBJECT_DELETE_URI		    	= "/services/data/v34.0/sobjects/{sobjectname}/{sobjectid}";
		
		
		
		
		public static final String SF_UPLOADS_PATH						= "sf.uploadsfolder.path";
		
}
