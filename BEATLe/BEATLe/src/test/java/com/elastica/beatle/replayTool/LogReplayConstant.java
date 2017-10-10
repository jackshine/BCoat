/**
 * 
 */
package com.elastica.beatle.replayTool;

/**
 * @author anuvrath
 *
 */
public class LogReplayConstant {
	public static final String REPLAY_LOGFOLDER_PATH = "/src/test/resources/LogReplayLogs/";	
	
	public static final String S3_BUCKET = "elastica-qa-automation";
	public static final String S3_FOLDER = "LogReplayLogs/";
	
	// Log File name constants
	public static final String TESTDATA = "F1,CreateFolder.log";	
	
	//	DROP BOX FILE NAMES
	public static final String DROP_BOX_CREATEFOLDER_LOG = "Dropbox_create_folder.log";
	public static final String DROP_BOX_UPLOADFILE_LOG = "Dropbox_upload_file.log";
	public static final String DROP_BOX_LOGIN_LOG = "Dropbox_login.log";
	public static final String DROP_BOX_LOGOUT_LOG = "Dropbox_logout.log";
	public static final String DROP_BOX_OPENUPLOAD_LOG = "Dropbox_opening_upload_file.log";
	public static final String DROP_BOX_RENAMEFOLDER_LOG = "Dropbox_rename_folder.log";
}