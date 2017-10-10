package com.elastica.gateway;

import java.io.File;
import java.util.List;
import java.util.Map;

public class GatewayTestConstants {

	public static final String EOE_DISPLAY_LOG 	 = "https://api-eoe.elastica-inc.com/eslogs/displaylogs/";
	public static final String QAVPC_DISPLAY_LOG = "https://qa-vpc-api.elastica-inc.com/eslogs/displaylogs/";
	public static final String PERF_DISPLAY_LOG  = "https://perfapi.elastica-inc.com/eslogs/displaylogs/";
	public static final String PROD_DISPLAY_LOG  = "https://api-vip.elastica.net/eslogs/displaylogs/";

	public static final int DEFAULT_ELEMENT_LOAD_TIME = 30000;
	public static final String ELASTICAQANET 	= "HBhzAg76hDGC6xoYIx+fVQ==";
	public static final String ELASTICACO 		= "HBhzAg76hDGC6xoYIx+fVQ==";
	public static final String ELASTICAQAINC 	= "HBhzAg76hDGC6xoYIx+fVQ==";
	public static final String ELASTICAQACOM 	= "rJebsyAI3RUS1DpNf4Bdpg==";
	public static final String GATEWAYBEATLECOM = "FGCkwUL5roSPS7TwmIlBIojwfpY5A2Geg+2ugXIPGGk=";
	public static final String GATEWAYO365BEATLECOM = "1FbN0+nX+oWncuRkXFmfqL1OSsYNX9xIq0mymmsA90Y=";
	public static final String FAKEGATEWAYBEATLECOM = "KotfBliFdcLtjJpLZcoFu71OSsYNX9xIq0mymmsA90Y=";
	
	public static final String SALESFORCEGATEWAYBEATLECOM = "UY/XWSnHnl7FLplgKcy+dOAjwq/SgEhtx0z96Ps50xQ=";
	public static final String BOXGATEWAYBEATLECOM = "KotfBliFdcLtjJpLZcoFu71OSsYNX9xIq0mymmsA90Y=";
	public static final String DEBUGGATEWAYBEATLECOM = "KotfBliFdcLtjJpLZcoFu71OSsYNX9xIq0mymmsA90Y=";
	
	
	
	public static final String FILE_NAME  	 	="file_name";
	public static final String POLICY_NAME 		="policy_name";
	public static final String TARGET_USER		="target_user";
	public static final String APPLICATIONS		="saas_apps";
	public static final String NOTIFY_EMAILID	="admin_email";
	public static final String OBJECT_ACCESS  	="object_access";
	public static final String ACTIVITY_ACCESS  ="activity_access";
	public static final String EXPECTED_MESSAGE  ="message";
	public static final String SHARE_WITH   	="SharedWith";
	public static final String SHARED_WITH   	="SharedWith";
	public static final String FILE_TYPE   		="file_type";
	public static final String SHARED_BY		="shared_by";
	
	public static final String VULNERABILITY_TYPE ="vulnerability_type";
	public static final String CONTENT_IQ		  ="content_iq";
	public static final String TRANSFER_TYPE      ="transfer_type";
	public static final String CIQ_WHITELIST      ="CIQ_WHITELIST";
	public static final String LARGER_THAN		  ="larger_than";
	public static final String SMALLER_THAN		  ="smaller_than";
	
	public static final String PLATFORM_TYPE  ="platform_type";
	public static final String BROWSER_TYPE  ="browser_type";
	
	public static final String GROUP		="user_group";
	public static final String USER_WLIST		="user_wlist";
	public static final String GROUP_WLIST		="group_wlist";
	
	public static final String GEOIP_SCOPE		        ="geoip_scope";
	public static final String GEOIP_SCOPE_WLIST		="geoip_scope_wlist";
	
	
	public static final String PLATFORM_LIST		="platform_list";
	
	public static final String PLATFORM_WLIST		="platform_wlist";
	
	
	public static final String BROWSER_LIST		="browser_list";
	public static final String BROWSER_WLIST		="browser_wlist";
	
	public static final String ACTIONS		="actions";
	public static final String ACOUNT_TYPE		="acount_type";
	
	public static final String REPLAY_LOGFOLDER_PATH = "/src/test/resources/LogReplayLogs/boxv2/";	
	public static final String CONTENTIQ_FILE_PATH = "/src/test/resources/dci/uploadCIQ/";
	public static List<Map<String, String>> LOG_MESSAGE;
	public static final String MESSAGE = "message";
	public static final String ACTIVITY_TYPE = "Activity_type";
	public static final String RISK = "Risks";
	public static final String OBJECT_TYPE = "Object_type";
	public static final String OBJECT_NAME = "ObjectName";
	public static final String SCOPE = "Scope";
	public static final String SEVERITY = "severity";
	public static final String USER_NAME ="user_name";
	public static final String REQ_URI ="req_uri";
	public static final String RESP_CODE ="resp_code";
	public static final String SHARE__WITH = "SharedWith";
	public static final String BROWSER = "browser";
	public static final String BROWSER_NAME = "Firefox";
	public static final String DEVICE ="device";
	public static final String IS_ANONYMOUS_PROXY ="is_anonymous_proxy";
	public static final String ACCOUNT_TYPE ="account_type";
	public static final String FACILITY ="facility";
	public static final String ELASTICA_USER ="elastica_user";
	public static final String DOMAIN ="domain";
	public static final String NAME ="name";
	public static final String USER ="user";
	public static final String FILE_SIZE ="File_Size";
	public static final String FILE_TYPE_GENERIC="file_type_generic";
	public static final String REFERER_URI ="referer_uri";
	public static final String USER_AGENT ="user-agent";
	public static final String SOURCE ="source";
	public static final String CONTENT_INLINE ="ContentInline";
	
	
	public static final String POLICY_TYPE ="policy_type";
	public static final String POLICY_ACTION ="policy_action";
	public static final String POLICY_VIOLATED ="PolicyViolated";
	public static final String ACTION_TAKEN ="action_taken";
	public static final String CRYPTO_KEY_SERVER ="crypto_key_server_used";
	public static final String CRYPTO_KEY_NAME ="crypto_key_name";
	public static final String CRYPTO_KEY_VERSION ="crypto_key_version";
	
	public static final String GMAIL_ATTACH = "Hello.pdf";
	public static final String CIQ_FILE = "pii.txt";
	public static final String FOLDER_SHARE = "FolderToShare";
	
	
	public static final String GDRIVE_1MB_FILE_UPLOAD = "upload1mb.pdf";
	 
	public static final String GDRIVE_ONE_DRIVE_FILE_ATTACH = "Dial-FAQ.pdf";
	public static final String GDRIVE_ONE_DRIVE_FILE_ATTACH_PERSONAL = "Dial-FAQ";
	public static final String GDRIVE_REGRESSION_FOLDER = "data";
	public static final String GDRIVE_ORIGINAL_FILE_PATH = System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+
			"resources"+File.separator+"meta"+File.separator;
	public static final String BOX_ORIGINAL_FILE_PATH = System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+
			"resources"+File.separator+"meta"+File.separator;
	public static final String AUTOIT_FILE_PATH = System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+
			"resources"+File.separator+"utils"+File.separator;
	public static final String GDRIVE_UPLOAD_ORIGINAL_FILE = "upload.pdf";
	public static final String GDRIVE_DOWNLOAD_ORIGINAL_FILE = "download.pdf";
	public static final String GDRIVE_DOWNLOAD_1MB_ORIGINAL_FILE = "download1mb.pdf";
	public static final String GDRIVE_DOWNLOAD_DECRYPTED_FILE = GDRIVE_UPLOAD_ORIGINAL_FILE + ".eef";
	public static final String GDRIVE_DOWNLOAD_DECRYPTED_FILE2 = "decryptdownload.pdf.eef";

	public static final String GDRIVE_ORGINAL_FILE = "readme.pdf";
	public static final String GDRIVE_ORGINAL_DOC = "mydoc.docx";
	public static final String GDRIVE_ORGINAL_FILE_UPLOAD_ENCRPT = "readme1.pdf";
	//public static final String GDRIVE_UPLOAD_FILE_6MB = "6MB.jpg";
	
	public static final String GDRIVE_DECRYPT_FILE_6MB = "6MB_Decrypt.zip.eef";
	public static final String GDRIVE_DECRYPT_FILE_10MB = "10MB_Decrypt.zip.eef";
	public static final String GDRIVE_DECRYPT_FILE_20MB = "20MB_Decrypt.dmg.eef";
	
	public static final String GDRIVE_DOWNLOAD_FILE_6MB = "6MB.jpg";
	public static final String GDRIVE_DOWNLOAD_FILE_10MB = "10MB.zip";
	public static final String GDRIVE_DOWNLOAD_FILE_20MB = "20MB.dmg";
	
	
	public static final String GDRIVE_UPLOAD_FILE_6MB = "6MB_Upload.zip";
	public static final String GDRIVE_UPLOAD_FILE_10MB = "10MB_Upload.zip";
	public static final String GDRIVE_UPLOAD_FILE_20MB = "20MB_Upload.dmg";
	public static final String GDRIVE_UPLOAD_FILE_100MB = "100mb.zip";
	public static final String GDRIVE_UPLOAD_FILE_200MB = "200mb.zip";
	public static final String GDRIVE_UPLOAD_FILE_500MB = "500mb.zip";
	
	public static final String GDRIVE_DOWNLOAD_FILE_100MB = "100mb_Download.zip";
	public static final String GDRIVE_DOWNLOAD_FILE_200MB = "200mb_Download.zip";
	public static final String GDRIVE_DOWNLOAD_FILE_500MB = "500mb_Download.zip";
	
	
	public static final String GDRIVE_ENCRYPTED_FILE = "readme1.pdf.eef";
	public static final String ENCRY_DECRYPT_POLICY_NAME = "FILE_ENCRYPTION_DECRYPTION_";
	public static final String CIQ_POLICY_NAME = "GW_CIQ_POLICY_";
	public static final String CIQ_EXCEPTION_NAME = "CIQ_EXCEPTION_POLICY_";
	public static final String ENC_DEC_CIQ_POLICY_NAME = "GW_ENC_DEC_CIQ_POLICY_";
	public static final String CIQ_DOWNLOAD_POLICY_NAME = "GW_CIQ_DOWNLOAD_POLICY_";
	public static final String CIQ_UPLOAD_POLICY_NAME = "GW_CIQ_UPLOAD_POLICY_";
	public static final String DCI_POLICY_NAME = "GW_DCI_POLICY_";
	public static final String O365_POLICY_NAME = "GW_POLICY_TEST";
	public static final String COMMON_AEP = "COMMON_AEP_TEST";
	public static final String CIQWORDBLOCK= "DOCFILEBLOCK";
	
	public static final String O365_SHARING_POLICY = "GW_FILE_SHARING_TEST";
	
	public static final String GDRIVE_POLICY_NAME = "GW_GDRIVE_POLICY_TEST";
	public static final String GDRIVE_UPLOAD_FILE ="original.rtf.eef";
	public static final String GDRIVE_CIQ_DOWNLOAD_FILE ="pii.txt";
	public static final String GDRIVE_ENCRYPTED_FILE_RENAME1 = "decrypt.pdf.eef";
	public static final String GDRIVE_ENCRYPTED_FILE_RENAME2 = "decrypt1.pdf.eef";
	public static final String GDRIVE_ENCRYPTED_FILE_RENAME3 = "decrypt2.pdf.eef";
	
	
	public static final String GDRIVE_FILE_EMAIL_LINK = "myshare";
	public static final String GDRIVE_FILE_PUBLIC_LINK = "publicshare.docx";
	public static final String GDRIVE_FILE ="Rename";
	public static final String GDRIVE_FILE2 ="Rename2";
	public static final String GDRIVE_SHARE_FILE = "share.pdf";
	public static final String MOVE_FILE = "Hello.pdf";
	public static final String GDRIVE_CREATE_DOC ="my_doc";
	public static final String GDRIVE_CREATE_DOC_COPY = "Copy of " + GDRIVE_CREATE_DOC;
	public static final String MOVE_TO_FOLDER = "new";

	
	public static final String GDRIVE_DELETE_FILE = "datadummy.pdf";
	public static final String GDRIVE_DELETE_FOLDER = "delete_folder";
	public static final String GDRIVE_CREATE_FOLDER = "empty_folder";
	public static final String GDRIVE_DOWNLOAD_FOLDER = "download";
	public static final String GDRIVE_RENAME_FOLDER = "rename_empty_folder";
	
//	public static final String GDRIVE_REQ_URI_LOGIN = "https://accounts.google.com/ServiceLoginAuth";
//	public static final String GDRIVE_REQ_URI_LOGOUT = "https://accounts.google.com/Logout?service=wise";
	public static final String GDRIVE_REQ_URI_LOGIN = "https://";
	public static final String GDRIVE_REQ_URI_LOGOUT = "https://";
	
	public static final String DROPBOX_REQ_URI_LOGIN = "https://www.dropbox.com/ajax_login";
	public static final String DROPBOX_FILESHARE = "Get Started with Dropbox.pdf";
	public static final String DROPBOX_REQ_URI_SHARE = "https://www.dropbox.com/sm/share_link/Get%20Started%20with%20Dropbox.pdf";
	public static final String DROPBOX_REQ_URI_DOWNLOAD = "https://dl-web.dropbox.com/get";
	public static final String DROPBOX_REQ_URI_UPLOAD ="https://dl-web.dropbox.com/upload";
	public static final String DROPBOX_DOWNLOAD_DECRYPTED_FILE = "readme1.pdf.eef";
	
	public static final String O365_FILE = "Dial-FAQ.pdf";
	public static final String O365_FILE_DOC = "mydoc.docx";
	public static final String O365_EMAIL_NEW_FOLDER = "NewFolderAdd";
	public static final String O365_EMAIL_SUB_FOLDER = "NewSubFolder";
	public static final String O365_ADMIN_USER = "Regression_Test_User";
	public static final String O365_TEST_GROUP = "testusergroup";
	public static final String O365_SHARED_MAIL_BOX_USER = "test";
	public static final String O365_TEST_DOMAIN = "testgatewayO365beatle.com";
	public static final String O365_RENAME = "Rename";
	public static final String O365_UPLOAD_FILE = "Hello.txt";
	
	
	public static final String O365_MULTI_UPLOAD_FILE1 = "multi1.txt";
	public static final String O365_MULTI_UPLOAD_FILE2 = "multi2.docx";
	public static final String O365_MULTI_UPLOAD_FILE3 = "multi3.pdf";
	public static final String O365_MULTI_UPLOAD_FOLDER = "mulltifolder";
	
	public static final String O365_DOWNLOAD_ENCRYPTED_FILE = "readme.pdf.eef";
	public static final String O365_UPLOAD_ENCRYPT_FILE = "readme1.pdf";
	public static final String O365_DELETE_ENCRYPTED_FILE = "readme1.pdf.eef";
	public static final String O365_UPLOAD_FILE_6MB = "6MB_Upload.zip";
	public static final String O365_UPLOAD_FILE_10MB = "10MB_Upload.zip";
	public static final String O365_UPLOAD_FILE_20MB = "20MB_Upload.dmg";
	
	public static final String O365_UPLOAD_FILE_6MB_Decrypt = "6MB_Decrypt.zip";
	public static final String O365_UPLOAD_FILE_10MB_Decrypt = "10MB_Decrypt.zip";
	public static final String O365_UPLOAD_FILE_20MB_Decrypt = "20MB_Decrypt.dmg";

	public static boolean REACH = true;
	public static final String O365_MOVE_TO_FOLDER = "AFolder";
	public static final String O365_ROOT_FOLDER ="Files";
	public static final String O365_CREATE_FOLDER = "NewFolder";
	public static final String O365_RENAME_FOLDER = "RENAMEFOLDER";
	public static final String O365_MOVE_TO_DOC = "test";
	
	public static final String ACCOUNT_TYPE_INTERNAL ="Internal";
	public static final String ACCOUNT_TYPE_EXTERNAL ="External";
	public static final String IS_ANONYMOUS_PROXY_FALSE ="false";
	public static final String IS_ANONYMOUS_PROXY_TRUE ="true";
	public static final String SEVERITY_INFORMATIONAL = "informational";
	public static final String SEVERITY_CRITICAL = "critical";
	public static final String SEVERITY_HIGH = "high";
	public static final String SEVERITY_ERROR = "error";
	public static final String CITY = "city";
	public static final String COUNTRY = "country";
	public static final String HOST = "host";
	public static final String CREATED_TIME_STAMP = "created_timestamp";
	public static final String INSERTED_TIME_STAMP = "inserted_timestamp";
	public static final String LATITUDE = "latitude";
	public static final String LONGITUDE = "longitude";
	public static final String LOCATION = "location";
	public static final String REGION = "region";
	public static final String AWS_REGION = "Region";
	public static final String REQ_SIZE = "req_size";
	public static final String RESP_SIZE = "resp_size";
	public static final String TIME_ZONE = "time_zone";
	public static final String VERSION = "version";
	public static final String STATUS_CODE = "status_code";
	
	public static final String FILE_TRANSFER_POLICY = "FileTransfer";
	public static final String ACCSESS_ENFORCEMENT_POLICY = "";
	public static final String FILE_SHARE_POLICY = "FileTransfer";
	
	public static final String POLICY_ACTION_ALERT ="ALERT";
	public static final String ACTION_TAKEN_ENCRYPT ="encrypt";
	public static final String ACTION_TAKEN_DECRYPT ="decrypt";
	
	
	public static final String CRYPTO_KEY_SERVER_USED ="50.207.165.70";
	public static final String CRYPTO_KEY_NAME_USED ="amateen-cert-test";
	public static final String CRYPTO_KEY_VERSION_USED ="1";
	public static final String _DOMAIN ="_domain";
	
	public static int INITIAL_WAIT=5;
	public static int RETRY_WAIT=10;
	public static int RETRY_COUNT=3;
	
	public static final String UPLOAD_FILE_PATH=System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+
	"resources"+File.separator+"meta"+File.separator;
	
	public static final String MULTIPLE_FILE_PATH=System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+
	"resources"+File.separator+"multi"+File.separator;
	
	public static final String UPLOAD_BIG_FILE_PATH= "D:\\largerfiles\\";
	
	public static final String ACTION_STATUS = "action_status";
	public static final String SUCCESS = "success";
	
	public static final String O365_EMAIL_APP_LINK = "https://outlook.office.com/owa/";
	
}
