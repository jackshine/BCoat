package com.universal.constants;

public class ServiceNowConstants {
	//Authorization urls
		public static final String SN_OAUTH20_HOST						= "dev15829.service-now.com";
		public static final String SN_API_HOST							= "dev15829.service-now.com";
		public static final String SN_OAUTH20_AUTHORIZE_URL 			= "/services/oauth2/authorize";	
		public static final String SN_OAUTH20_TOKEN_URL 				= "/oauth_token.do";
		public static final String SN_OAUTH20_REVOKE_URL				= "/oauth2/revoke";
		public static final String SN_OAUTH20_DESKTOP_REDIRECT_URL 		= "http://login.elastica.net/oauth20_desktop.srf";
		public static final String SN_GRANT_TYPE_AUTHORIZATION_CODE 	= "authorization_code";
		public static final String SN_GRANT_TYPE_REFRESH_TOKEN 			= "refresh_token";	
		public static final String SN_AUTHORIZE_URL						= "sn.authorization.url";
		
		
		
		
		public static final String SN_INCIDENT_CREATE_URI				= "/api/now/v1/table/incident";
		public static final String SN_INCIDENT_GET_URI					= "/api/now/v1/table/incident/{sysid}";
		public static final String SN_INCIDENT_UPDATE_URI				= "/api/now/v1/table/incident/{sysid}";
		public static final String SN_INCIDENT_DELETE_URI				= "/api/now/v1/table/incident/{sysid}";
		public static final String SN_UPLOADS_PATH						= "sn.uploadsfolder.path";
		
		public static final String SN_RECORD_CREATE_URI					= "/api/now/v1/table/{tablename}";
		public static final String SN_RECORD_GET_URI					= "/api/now/v1/table/{tablename}/{sysid}";
		public static final String SN_RECORD_UPDATE_URI					= "/api/now/v1/table/{tablename}/{sysid}";
		public static final String SN_RECORD_DELETE_URI					= "/api/now/v1/table/{tablename}/{sysid}";
		
}
