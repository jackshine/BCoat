/**
 * 
 */
package com.elastica.beatle.protect;

import java.io.File;

/**
 * @author shri
 *
 */
public class ProtectTestConstants {
	
	public static final String PROTECT_API_CONFIGURATION_FILEPATH = "/src/test/resources/protect/configurations/APIConfigurations.xml";	
	public static final String POLICY_FILE_NAME = "Test_Policy";
	public static final String CIQ_FILE_NAME = "Test_CIQ";
	public static final String OK = "ok";
	public static final String TRUE = "true";
	public static final String FALSE = "false";
	public static final String UPDATED_STATUS = "updated_status";
	public static final String SUB_ID = "sub_id";
	public static final String POLICIES = "policies";
	public static final String SUCCESS = "success";
	public static final String PROFILE_NAME = "profile_name";
	public static final String ACTION_STATUS = "action_status";
	public static final String fileName = "BE";
	public static final String fileExt = ".txt";
	public static final String EOE_URL = "https://api-eoe.elastica-inc.com/";
	public static final String PROTECT_RESOURCE_PATH = System.getProperty("user.dir") + File.separator + "src" + File.separator + "test" +File.separator +"resources" + File.separator + "protect" + File.separator;
	public static final String PROTECT_RESOURCE_CONTENTTYPE_PATH = System.getProperty("user.dir") + File.separator + "src" + File.separator + "test" +File.separator +"resources" + File.separator + "protect" + File.separator + "contentType" + File.separator;
	public static final String PROTECT_RESOURCE_FILEFORMAT_PATH = System.getProperty("user.dir") + File.separator + "src" + File.separator + "test" +File.separator +"resources" + File.separator + "protect" + File.separator + "fileformats" + File.separator;	
	public static final String ALERT_MESSAGE_1 = "[ALERT] User shared a document '";
	public static final String ALERT_MESSAGE_2 = "', and this activity has matched policy : ";
	public static final String ALERT_MESSAGE_3 = " Mail With Body', and content inspection has matched policy : ";
	public static final String ALERT_MESSAGE_4 = "[ALERT] User uploaded or modified a document '";
	public static final String ALERT_MESSAGE_5 = "', and content inspection has matched policy : ";
	public static final String DROPBOX_USER = "qa-admin@elasticaqa.net";
	public static final String DROPBOX_PASSWORD = "Elastica#456";
	public static final String SEVERITY = "severity";
	public static final String POLICY_ACTION = "policy_action";
	public static final String FACILITY = "facility";
	public static final String SOURCE = "__source";
	public static final String OBJECT_NAME = "Object_name";
	public static final String NAME = "name";
	public static final String POLICY_VIOLATED = "_PolicyViolated";
	public static final String POLICY_TYPE = "policy_type";
	public static final String POLICY_ID = "id";
	public static final String POLICY_SUB_ID = "sub_id";
	public static final String MESSAGE = "message";
	public static final String ACTIVITY_TYPE = "Activity_type";
	public static final String OBJECT_TYPE = "Object_type";
	public static final String OBJECT = "Object";
	public static final String CRITICAL = "critical";
	public static final String INFORMATIONAL = "informational";
	public static final String WARNING = "warning";
	public static final String ALERT = "ALERT";
	public static final String API = "API";
	public static final String FILE_SHARING_API = "FileSharingAPI";
	public static final String POLICY_VIOLATION = "Policy Violation";
	public static final String UNSHARE = "Unshare";
	public static final String SHARE = "Share";
	public static final String RESTRICT = "Restrict";
	public static final String TRASH = "Trash";
	public static final String CONTENT_INSPECTION = "Content Inspection";
	public static final String ROLE_CHANGE = "Role Change";
	public static final String ONEDRIVE_USERNAME = "qa-admin@o365security.net";
	public static final String ONEDRIVE_PASSWORD = "uF4$WCFj8zr@peh";
	public static final String EXPIRE_SHARING = "Expire Sharing";
	public static final String ROLE = "Role";
	public static final String PREVIOUS_ROLE = "PreviousRole";
	public static final String PUBLIC = "public";
	public static final String UNEXPOSED = "unexposed";
	public static final String EXTERNAL = "external";
	public static final String INTERNAL = "internal";
	public static final String EVERYONE_EXCEPT_EXTERNAL_USERS = "Everyone except external users";
	public static final String EVERYONE = "Everyone";
	public static final String UNSHARED_WITH = "Unshared_With";
	public static final String SHARED_WITH = "Shared_With";
	public static final String EXTERNAL_USER_O365 = "mayurbelekar@hotmail.com";
	public static final String OFFICE_365 = "Office 365";
	public static final String OFFICE365 = "Office365";	
	public static final String DELETE_EMAIL_ATTACHMENT = "deleteEmailAttachment";
	public static final String DELETE_EMAIL_BODY = "deleteEmailBody";	
	public static final String EXTERNAL_GMAIL_USER ="protectelastica@gmail.com";
	public static final String INTERNAL_SECURLET_O365BEATLE ="testuser1@securleto365beatle.com";
	public static final String INTERNAL_PROTECT_O365BEATLE ="testuser1@protecto365autobeatle.com";
	public static final String ALPHA_LOWER_CASE = "abcdefghijklmnopqrstuvwxyz";
	public static final String ALPHA_UPPER_CASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	public static final String NUMERIC = "0123456789";
	public static final String SPECIAL_CHARACTER = "!@#$%^&*()_+<>?:\".,;";
	public static final String CHANGE_COLLABORATION_ROLE = "Change Collaboration Role";
	public static final String USER = "user";
	public static final String FILE = "File";
	public static final String DOMAIN = "_domain";
	public static final String DOMAIN1 = "domain";
	public static final String BLOCKED_APPS = "blocked_apps";
	public static final String BLOCK = "BLOCK";
	public static final String THREATSCORE = "ThreatScore";
	public static final String ACCESS_ENFORCEMENT = "AccessEnforcementAPI";
	public static final String CONTENT_CHECKS = "content_checks";
	public static final String CONTENT_VULNERABILITY = "contentVulnerability";

	public static final String GENERIC_FILE_SHARING_POLICY ="/src/test/resources/protect/policyJSON/genericFileSharingPolicy.json";
	public static final String GENERIC_FILE_TRANSFER_POLICY = "/src/test/resources/protect/policyJSON/genericFileTransferPolicy.json";
	public static final String GENERIC_ACCESS_ENFORCEMENT_POLICY = "/src/test/resources/protect/policyJSON/genericAccessEnforcementPolicy.json";
	public static final String GENERIC_CREATE_POLICY_TEMPLATE ="/src/test/resources/protect/policyJSON/genericCreatePolicyTemplate.json";
	public static final String GENERIC_EDIT_POLICY_TEMPLATE ="/src/test/resources/protect/policyJSON/genericEditPolicyTemplate.json";
	public static final String CREATED_BY = "created_by";
	public static final String MODIFIED_BY = "modified_by";
	public static final String EXPOSURE_TYPE = "exposureType";
	public static final String EXTERNAL_RECIPIENTS = "External_recipients";
	public static final String SUBJECT = "subject";
	public static final String EMAIL_MESSAGE = "Email_Message";
	public static final String DETAILS = "details";
	public static final String PROTECT_FILE_TEMP_PATH = System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+"resources"+
			File.separator+"protect"+File.separator+"temp";
	public static final String MAIL = "Mail";
	public static final String PROFILE_ID = "id";
	public static final String MAIL_SUBJECT = "Protect Gmail Automation Test: ";
	
	public static final String _SOURCE1 = "_source";
	public static final String HITS = "hits";
	public static final String ALL = "all";
	public static final String DEL = "DEL";
	public static final String DISABLE = "DISABLE";
	public static final String TOTAL = "total";
	public static final String FILETYPEEXC = "FILETYPEEXC";
	public static final String NEW_FILES = "newFiles";
	public static final String ANYONE = "anyone";
	public static final String EXT_USER_GMAIL = "mayurbelekar@gmail.com";
	public static final String EXT_USER = "mayurbelekar@gmail.com";
	public static final String FILE1 = "FILE";
	public static final String PHONE_PROD = "8885097303";
	public static final String PHONE_CEP = "8885101225";
	public static final String ENV_EOE = "eoe";
	public static final String ENV_CEP = "cep";
	public static final String ENV_PROD = "prod";
	public static final String NO = "no";
	public static final String BOX = "Box";
	public static final String FOLDER ="folder";
	public static final String EDITOR ="editor";
	public static final String GOOGLE_DRIVE = "Google Drive";
	public static final String DROPBOX = "Dropbox";
	public static final String READER = "reader";
	public static final String WRITER = "writer";
	public static final String DELETE = "delete";
	public static final String COMPANY = "company";
	public static final String OPEN = "open";
	public static final String EXPIRES = "expires";
	public static final String REMOVE_COLLAB = "removeCollab";
	public static final String UNSHARE1 = "unshare";
	public static final String UPDATE_COMMENTER = "updateCommenter";
	public static final String EXT_COLLAB = "extcollab";
	public static final String GDRIVE = "GDrive";
	public static final String COMMENTER = "commenter";
	public static final String GDCHANGEACCESS = "GDCHANGEACCESS";
	public static final String GDREMOVELINK = "GDREMOVELINK";
	public static final String GDPREVENTWRITER = "GDPREVENTWRITER";
	public static final String GDPREVENTDOWNLOAD = "GDPREVENTDOWNLOAD";
	public static final String GDUPDATECOLLAB = "GDUPDATECOLLAB";
	public static final String GDREMOVECOLLAB = "GDREMOVECOLLAB";
	public static final String PROTECTBEATLECOM = "protectbeatle.com";
	public static final String VIEWER = "viewer";
	public static final String UPLOADER = "uploader";
	public static final String PREVIEWER = "previewer";
	public static final String CO_OWNER = "co-owner";
	public static final String COLLABORATORS = "collaborators";
	public static final String DEACTIVATEDPOL = "DEACTIVATEDPOL";
	public static final String COLLAB = "collab";
	public static final String IDENTIFICATION ="identification";
	
	public static final String ACCESS_MONITORING = "ACCESS_MONITORING";
	public static final String TENANT_A_POL = "TENANT_A_POL";
	public static final String TENANT_B_POL ="TENANT_B_POL";
	
	
	

	
}
