package com.elastica.beatle.detect;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.apache.commons.lang3.RandomStringUtils;

public class SequenceDetectorConstants {
	
	
	public static final String INVALID_LOGIN_LOGIN_UPLOAD = RandomStringUtils.randomAlphanumeric(12)+"_"+"INVALID_LOGIN_LOGIN_UPLOAD";
	public static final String INVALID_LOGIN_LOGIN_ANY  = RandomStringUtils.randomAlphanumeric(12)+"_"+"INVALID_LOGIN_LOGIN_ANY";
	public static final String CREATE_DELETE_RENAME  = RandomStringUtils.randomAlphanumeric(12)+"_"+"CREATE_DELETE_RENAME";
	public static final String CREATE_VIEW_SHARE_DELETE  = RandomStringUtils.randomAlphanumeric(12)+"_"+"CREATE_VIEW_SHARE_DELETE";
	
	public static final String COPY_DOWNLOAD_DELETE  = RandomStringUtils.randomAlphanumeric(12)+"_"+"COPY_DOWNLOAD_DELETE";
	public static final String COPY_DOWNLOAD_DELETE_TWO  = RandomStringUtils.randomAlphanumeric(12)+"_"+"COPY_DOWNLOAD_DELETE_TWO";
	public static final String COPY_DOWNLOAD_DELETE_THREE  = RandomStringUtils.randomAlphanumeric(12)+"_"+"COPY_DOWNLOAD_DELETE_THREE";
	public static final String COPY_DOWNLOAD_DELETE_FOUR  = RandomStringUtils.randomAlphanumeric(12)+"_"+"COPY_DOWNLOAD_DELETE_FOUR";
	
	
	
	public static final String SHARE_ANY_ACTIVITY_UNSHARE  = RandomStringUtils.randomAlphanumeric(12)+"_"+"SHARE_ANY_ACTIVITY_UNSHARE";
	public static final String SHARE_ANY_ACTIVITY_UNSHARE_TWO  = RandomStringUtils.randomAlphanumeric(12)+"_"+"SHARE_ANY_ACTIVITY_UNSHARE_TWO";
	public static final String SHARE_ANY_ACTIVITY_UNSHARE_THREE  = RandomStringUtils.randomAlphanumeric(12)+"_"+"SHARE_ANY_ACTIVITY_UNSHARE_THREE";
	public static final String SHARE_ANY_ACTIVITY_UNSHARE_FOUR  = RandomStringUtils.randomAlphanumeric(12)+"_"+"SHARE_ANY_ACTIVITY_UNSHARE_FOUR";
	
	
	public static final String CREATE_SHARE_TRASH_DELETE  = RandomStringUtils.randomAlphanumeric(12)+"_"+"CREATE_SHARE_TRASH_DELETE";
	public static final String SHARE_DOWNLOAD_UNSHARE_WAIT_REPEAT  = RandomStringUtils.randomAlphanumeric(12)+"_"+"SHARE_DOWNLOAD_UNSHARE_WAIT_REPEAT";
	public static final String SHARE_UNSHARE_WAIT_REPEAT  = RandomStringUtils.randomAlphanumeric(12)+"_"+"SHARE_UNSHARE_WAIT_REPEAT";
	
	public static final String DOWNLOAD_UPLOAD_WAIT_REPEAT  = RandomStringUtils.randomAlphanumeric(12)+"_"+"DOWNLOAD_UPLOAD_WAIT_REPEAT";
	public static final String DOWNLOAD_UPLOAD_WAIT_REPEAT_TWO  = RandomStringUtils.randomAlphanumeric(12)+"_"+"DOWNLOAD_UPLOAD_WAIT_REPEAT_TWO";
	public static final String DOWNLOAD_UPLOAD_WAIT_REPEAT_THREE  = RandomStringUtils.randomAlphanumeric(12)+"_"+"DOWNLOAD_UPLOAD_WAIT_REPEAT_THREE";
	public static final String DOWNLOAD_UPLOAD_WAIT_REPEAT_FOUR  = RandomStringUtils.randomAlphanumeric(12)+"_"+"DOWNLOAD_UPLOAD_WAIT_REPEAT_FOUR";
	
	public static final String UPLOAD_SHARE_WAIT_REPEAT  = RandomStringUtils.randomAlphanumeric(12)+"_"+"UPLOAD_SHARE_WAIT_REPEAT";
	public static final String INVALIDLOGIN_LOGIN_DOWNLOAD  = RandomStringUtils.randomAlphanumeric(12)+"_"+"INVALIDLOGIN_LOGIN_DOWNLOAD";
	public static final String DOWNLOAD_EXTRNAL_UPLOAD  = RandomStringUtils.randomAlphanumeric(12)+"_"+"DOWNLOAD_EXTRNAL_UPLOAD";
	public static final String TRASH_DELETE  = RandomStringUtils.randomAlphanumeric(12)+"_"+"TRASH_DELETE";
	public static final String FREQUENT_SESSION_DELETE  = RandomStringUtils.randomAlphanumeric(12)+"_"+"FREQUENT_SESSION_DELETE";
	public static final String LARGE_DOWNLOAD_DELETE   = RandomStringUtils.randomAlphanumeric(12)+"_"+"LARGE_DOWNLOAD_DELETE";
	public static final String LARGE_UPLOAD_DELETE  = RandomStringUtils.randomAlphanumeric(12)+"_"+"LARGE_UPLOAD_DELETE";
	public static final String LARGE_DELETE_DOWNLOAD  = RandomStringUtils.randomAlphanumeric(12)+"_"+"LARGE_DELETE_DOWNLOAD";
	public static final String LARGE_SHARE_UNSHARE  = RandomStringUtils.randomAlphanumeric(12)+"_"+"LARGE_SHARE_UNSHARE";
	
	public static final String SHARE_UNSHARE  = RandomStringUtils.randomAlphanumeric(12)+"_"+"SHARE_UNSHARE";
	public static final String SHARE_UNSHARE_TWO  = RandomStringUtils.randomAlphanumeric(12)+"_"+"SHARE_UNSHARE_TWO";
	public static final String SHARE_UNSHARE_THREE  = RandomStringUtils.randomAlphanumeric(12)+"_"+"SHARE_UNSHARE_THREE";
	public static final String SHARE_UNSHARE_FOUR  = RandomStringUtils.randomAlphanumeric(12)+"_"+"SHARE_UNSHARE_FOUR";
	
	
	public static final String FREQUENT_SHARE_UNSHARE  = RandomStringUtils.randomAlphanumeric(12)+"_"+"FREQUENT_SHARE_UNSHARE";
	public static final String FREQUENT_DELETE_DOWNLOAD  = RandomStringUtils.randomAlphanumeric(12)+"_"+"FREQUENT_DELETE_DOWNLOAD";
	public static final String UPLOAD_SHARE_UNSHARE  = RandomStringUtils.randomAlphanumeric(12)+"_"+"UPLOAD_SHARE_UNSHARE";
	public static final String INALID_LOGIN_LOGIN_LARGE_UPLOAD  = RandomStringUtils.randomAlphanumeric(12)+"_"+"INALID_LOGIN_LOGIN_LARGE_UPLOAD";
	public static final String INVALID_LOGIN_LOGIN_FREQUENT_USER_ACTIONS  = RandomStringUtils.randomAlphanumeric(12)+"_"+"INVALID_LOGIN_LOGIN_FREQUENT_USER_ACTIONS";
	public static final String INVALID_LOGIN_LOGIN_FREQUENT_SHARE  = RandomStringUtils.randomAlphanumeric(12)+"_"+"INVALID_LOGIN_LOGIN_FREQUENT_SHARE";
	public static final String INVALID_LOGIN_LOGIN_FREQUENT_DELETE  = RandomStringUtils.randomAlphanumeric(12)+"_"+"INVALID_LOGIN_LOGIN_FREQUENT_DELETE";
	public static final String LOGIN_LARGE_SHARE_UNSHARE  = RandomStringUtils.randomAlphanumeric(12)+"_"+"LOGIN_LARGE_SHARE_UNSHARE";
	public static final String LOGIN_FREQUENT_SHARE_UNSHARE  = RandomStringUtils.randomAlphanumeric(12)+"_"+"LOGIN_FREQUENT_SHARE_UNSHARE";
	public static final String LOGIN_LARGE_DOWNLOAD_DELETE  = RandomStringUtils.randomAlphanumeric(12)+"_"+"LOGIN_LARGE_DOWNLOAD_DELETE";
	public static final String LOGIN_LARGE_DOWNLOAD_LARGE_DELETE  = RandomStringUtils.randomAlphanumeric(12)+"_"+"LOGIN_LARGE_DOWNLOAD_LARGE_DELETE";
	public static final String UPLOAD_SHARE_UNSHARE_ONE  = RandomStringUtils.randomAlphanumeric(12)+"_"+"UPLOAD_SHARE_UNSHARE_ONE";
	public static final String UPLOAD_SHARE_UNSHARE_TWO  = RandomStringUtils.randomAlphanumeric(12)+"_"+"UPLOAD_SHARE_UNSHARE_TWO";
	public static final String UPLOAD_SHARE_UNSHARE_THREE  = RandomStringUtils.randomAlphanumeric(12)+"_"+"UPLOAD_SHARE_UNSHARE_THREE";
	public static final String UPLOAD_SHARE_UNSHARE_FOUR  = RandomStringUtils.randomAlphanumeric(12)+"_"+"UPLOAD_SHARE_UNSHARE_FOUR";
	public static final String SHARE_UNSHARE_WAIT_REPEAT_ONE  = RandomStringUtils.randomAlphanumeric(12)+"_"+"SHARE_UNSHARE_WAIT_REPEAT_ONE";
	public static final String SHARE_UNSHARE_WAIT_REPEAT_TWO  = RandomStringUtils.randomAlphanumeric(12)+"_"+"SHARE_UNSHARE_WAIT_REPEAT_TWO";
	public static final String SHARE_UNSHARE_WAIT_REPEAT_THREE  = RandomStringUtils.randomAlphanumeric(12)+"_"+"SHARE_UNSHARE_WAIT_REPEAT_THREE";
	public static final String SHARE_UNSHARE_WAIT_REPEAT_FOUR  = RandomStringUtils.randomAlphanumeric(12)+"_"+"SHARE_UNSHARE_WAIT_REPEAT_FOUR";
	public static final String UPLOAD_SHARE_WAIT_REPEAT_ONE  = RandomStringUtils.randomAlphanumeric(12)+"_"+"UPLOAD_SHARE_WAIT_REPEAT_ONE";//RandomStringUtils.randomAlphanumeric(12);
	public static final String UPLOAD_SHARE_WAIT_REPEAT_TWO  = RandomStringUtils.randomAlphanumeric(12)+"_"+"UPLOAD_SHARE_WAIT_REPEAT_TWO";
	public static final String UPLOAD_SHARE_WAIT_REPEAT_THREE  = RandomStringUtils.randomAlphanumeric(12)+"_"+"UPLOAD_SHARE_WAIT_REPEAT_THREE";
	public static final String UPLOAD_SHARE_WAIT_REPEAT_FOUR  = RandomStringUtils.randomAlphanumeric(12)+"_"+"UPLOAD_SHARE_WAIT_REPEAT_FOUR";
	public static final String TRASH_DELETE_ONE  = RandomStringUtils.randomAlphanumeric(12)+"_"+"TRASH_DELETE_ONE";
	public static final String TRASH_DELETE_TWO  = RandomStringUtils.randomAlphanumeric(12)+"_"+"TRASH_DELETE_TWO";
	public static final String TRASH_DELETE_THREE  = RandomStringUtils.randomAlphanumeric(12)+"_"+"TRASH_DELETE_THREE";
	public static final String TRASH_DELETE_FOUR  = RandomStringUtils.randomAlphanumeric(12)+"_"+"TRASH_DELETE_FOUR";
	public static final String INVALID_LOGIN  = RandomStringUtils.randomAlphanumeric(12)+"_"+"INVALID_LOGIN";
	public static final String UPLOAD  = RandomStringUtils.randomAlphanumeric(12)+"_"+"UPLOAD";
	public static final String UPLOAD_TWO = RandomStringUtils.randomAlphanumeric(12)+"_"+"UPLOAD_TWO";
	public static final String UPLOAD_THREE = RandomStringUtils.randomAlphanumeric(12)+"_"+"UPLOAD_THREE";
	public static final String UPLOAD_FOUR = RandomStringUtils.randomAlphanumeric(12)+"_"+"UPLOAD_FOUR";
	public static final String SHARE = RandomStringUtils.randomAlphanumeric(12)+"_"+"SHARE";
	public static final String SHARE_TWO = RandomStringUtils.randomAlphanumeric(12)+"_"+"SHARE_TWO";
	public static final String SHARE_THREE = RandomStringUtils.randomAlphanumeric(12)+"_"+"SHARE_THREE";
	public static final String SHARE_FOUR = RandomStringUtils.randomAlphanumeric(12)+"_"+"SHARE_FOUR";
	public static final String UNSHARE = RandomStringUtils.randomAlphanumeric(12)+"_"+"UNSHARE";
	public static final String UNSHARE_ONE = RandomStringUtils.randomAlphanumeric(12)+"_"+"UNSHARE_ONE";
	public static final String UNSHARE_TWO = RandomStringUtils.randomAlphanumeric(12)+"_"+"UNSHARE_TWO";
	public static final String UNSHARE_THREE = RandomStringUtils.randomAlphanumeric(12)+"_"+"UNSHARE_THREE";
	public static final String UNSHARE_FOUR = RandomStringUtils.randomAlphanumeric(12)+"_"+"UNSHARE_FOUR";
	public static final String DELETE  = RandomStringUtils.randomAlphanumeric(12)+"_"+"DELETE";
	public static final String DELETE_TWO  = RandomStringUtils.randomAlphanumeric(12)+"_"+"DELETE_TWO";
	public static final String DELETE_THREE  = RandomStringUtils.randomAlphanumeric(12)+"_"+"DELETE_THREE";
	public static final String DELETE_FOUR  = RandomStringUtils.randomAlphanumeric(12)+"_"+"DELETE_FOUR";
	public static final String CREATE  = RandomStringUtils.randomAlphanumeric(12)+"_"+"CREATE";
	public static final String CREATE_TWO  = RandomStringUtils.randomAlphanumeric(12)+"_"+"CREATE_TWO";
	public static final String CREATE_THREE  = RandomStringUtils.randomAlphanumeric(12)+"_"+"CREATE_THREE";
	public static final String CREATE_FOUR  = RandomStringUtils.randomAlphanumeric(12)+"_"+"CREATE_FOUR";
	public static final String RENAME  = RandomStringUtils.randomAlphanumeric(12)+"_"+"RENAME";
	public static final String RENAME_TWO  = RandomStringUtils.randomAlphanumeric(12)+"_"+"RENAME_TWO";
	public static final String RENAME_THREE  = RandomStringUtils.randomAlphanumeric(12)+"_"+"RENAME_THREE";
	public static final String RENAME_FOUR  = RandomStringUtils.randomAlphanumeric(12)+"_"+"RENAME_FOUR";
	public static final String DOWNLOAD  = RandomStringUtils.randomAlphanumeric(12)+"_"+"DOWNLOAD";
	public static final String DOWNLOAD_TWO  = RandomStringUtils.randomAlphanumeric(12)+"_"+"DOWNLOAD_TWO";
	public static final String DOWNLOAD_THREE  = RandomStringUtils.randomAlphanumeric(12)+"_"+"DOWNLOAD_THREE";
	public static final String DOWNLOAD_FOUR  = RandomStringUtils.randomAlphanumeric(12)+"_"+"DOWNLOAD_FOUR";
	public static final String VIEW  = RandomStringUtils.randomAlphanumeric(12)+"_"+"VIEW";
	public static final String VIEW_TWO  = RandomStringUtils.randomAlphanumeric(12)+"_"+"VIEW_TWO";
	public static final String VIEW_THREE   = RandomStringUtils.randomAlphanumeric(12)+"_"+"VIEW_THREE";
	public static final String VIEW_FOUR  = RandomStringUtils.randomAlphanumeric(12)+"_"+"VIEW_FOUR";
	public static final String MOVE_TWO  = RandomStringUtils.randomAlphanumeric(12)+"_"+"MOVE_TWO";
	public static final String MOVE_THREE  = RandomStringUtils.randomAlphanumeric(12)+"_"+"MOVE_THREE";
	public static final String MOVE  = RandomStringUtils.randomAlphanumeric(12)+"_"+"MOVE";
	public static final String MOVE_FOUR   = RandomStringUtils.randomAlphanumeric(12)+"_"+"MOVE_FOUR";
	public static final String EDIT  = RandomStringUtils.randomAlphanumeric(12)+"_"+"EDIT";
	public static final String EDIT_TWO   = RandomStringUtils.randomAlphanumeric(12)+"_"+"EDIT_TWO ";
	public static final String EDIT_THREE  = RandomStringUtils.randomAlphanumeric(12)+"_"+"EDIT_THREE";
	public static final String EDIT_FOUR  = RandomStringUtils.randomAlphanumeric(12)+"_"+"EDIT_FOUR";
	
	public static final String DELETE_COMMENT = RandomStringUtils.randomAlphanumeric(12)+"_"+"DELETE_COMMENT";
	public static final String DELETE_COMMENT_TWO = RandomStringUtils.randomAlphanumeric(12)+"_"+"DELETE_COMMENT_TWO";
	public static final String DELETE_COMMENT_THREE =RandomStringUtils.randomAlphanumeric(12)+"_"+ "DELETE_COMMENT_THREE";
	public static final String DELETE_COMMENT_FOUR = RandomStringUtils.randomAlphanumeric(12)+"_"+"DELETE_COMMENT_FOUR";

	public static final String POST_COMMENT = RandomStringUtils.randomAlphanumeric(12)+"_"+"POST_COMMENT";
	public static final String POST_COMMENT_TWO =RandomStringUtils.randomAlphanumeric(12)+"_"+ "POST_COMMENT_TWO";
	public static final String POST_COMMENT_THREE = RandomStringUtils.randomAlphanumeric(12)+"_"+"POST_COMMENT_THREE";
	public static final String POST_COMMENT_FOUR= RandomStringUtils.randomAlphanumeric(12)+"_"+"POST_COMMENT_FOUR";

	public static final String SET_LINK_EXPIRY = "SET_LINK_EXPIRY"+"_"+RandomStringUtils.randomAlphanumeric(12);
	public static final String FILE_LOCK = RandomStringUtils.randomAlphanumeric(12)+"_"+"FILE_LOCK";
	public static final String FILE_LOCK_TWO = RandomStringUtils.randomAlphanumeric(12)+"_"+"FILE_LOCK_TWO";
	public static final String FILE_LOCK_THREE= RandomStringUtils.randomAlphanumeric(12)+"_"+"FILE_LOCK_THREE";
	public static final String FILE_LOCK_FOUR= RandomStringUtils.randomAlphanumeric(12)+"_"+"FILE_LOCK_FOUR";

	public static final String FOLDER_MODIFY_PERMISSION= RandomStringUtils.randomAlphanumeric(12)+"_"+"FOLDER_MODIFY_PERMISSION";
	public static final String FOLDER_MODIFY_PERMISSION_TWO = RandomStringUtils.randomAlphanumeric(12)+"_"+"FOLDER_MODIFY_PERMISSION_TWO";
	public static final String FOLDER_MODIFY_PERMISSION_THREE= RandomStringUtils.randomAlphanumeric(12)+"_"+"FOLDER_MODIFY_PERMISSION_THREE";
	public static final String FOLDER_MODIFY_PERMISSION_FOUR= RandomStringUtils.randomAlphanumeric(12)+"_"+"FOLDER_MODIFY_PERMISSION_FOUR";
	
	public static final String FILE_SEARCH =RandomStringUtils.randomAlphanumeric(12)+"_"+ "FILE_SEARCH";
	public static final String FILE_SEARCH_TWO  = RandomStringUtils.randomAlphanumeric(12)+"_"+"FILE_SEARCH_TWO";
	public static final String FILE_SEARCH_THREE = RandomStringUtils.randomAlphanumeric(12)+"_"+"FILE_SEARCH_THREE";
	public static final String FILE_SEARCH_FOUR = RandomStringUtils.randomAlphanumeric(12)+"_"+"FILE_SEARCH_FOUR";
	
	public static final String FILE_UNLOCK = RandomStringUtils.randomAlphanumeric(12)+"_"+"FILE_UNLOCK";
	public static final String FILE_UNLOCK_TWO  =RandomStringUtils.randomAlphanumeric(12)+"_"+ "FILE_UNLOCK_TWO";
	public static final String FILE_UNLOCK_THREE = RandomStringUtils.randomAlphanumeric(12)+"_"+"FILE_UNLOCK_THREE";
	public static final String FILE_UNLOCK_FOUR = RandomStringUtils.randomAlphanumeric(12)+"_"+"FILE_UNLOCK_FOUR";

	public static final String FILE_CREATE_TAG = RandomStringUtils.randomAlphanumeric(12)+"_"+"FILE_CREATE_TAG";
	public static final String FILE_CREATE_TAG_TWO  = RandomStringUtils.randomAlphanumeric(12)+"_"+"FILE_CREATE_TAG_TWO";
	public static final String FILE_CREATE_TAG_THREE = RandomStringUtils.randomAlphanumeric(12)+"_"+"FILE_CREATE_TAG_THREE";
	public static final String FILE_CREATE_TAG_FOUR = RandomStringUtils.randomAlphanumeric(12)+"_"+"FILE_CREATE_TAG_FOUR";

	public static final String DELETE_TAG = RandomStringUtils.randomAlphanumeric(12)+"_"+"DELETE_TAG";
	public static final String DELETE_TAG_TWO  = RandomStringUtils.randomAlphanumeric(12)+"_"+"DELETE_TAG_TWO";
	public static final String DELETE_TAG_THREE =RandomStringUtils.randomAlphanumeric(12)+"_"+ "DELETE_TAG_THREE";
	public static final String DELETE_TAG_FOUR = RandomStringUtils.randomAlphanumeric(12)+"_"+"DELETE_TAG_FOUR";

	public static final String FILE_RESTORE = RandomStringUtils.randomAlphanumeric(12)+"_"+"FILE_RESTORE";
	public static final String FILE_RESTORE_TWO  = RandomStringUtils.randomAlphanumeric(12)+"_"+"FILE_RESTORE_TWO";
	public static final String FILE_RESTORE_THREE = RandomStringUtils.randomAlphanumeric(12)+"_"+"FILE_RESTORE_THREE";
	public static final String FILE_RESTORE_FOUR = RandomStringUtils.randomAlphanumeric(12)+"_"+"FILE_RESTORE_FOUR";

	public static final String FOLDER_EDIT_PROPERTIES = RandomStringUtils.randomAlphanumeric(12)+"_"+"FOLDER_EDIT_PROPERTIES";
	public static final String FOLDER_EDIT_PROPERTIES_TWO  = RandomStringUtils.randomAlphanumeric(12)+"_"+"FOLDER_EDIT_PROPERTIES_TWO";
	public static final String FOLDER_EDIT_PROPERTIES_THREE  = RandomStringUtils.randomAlphanumeric(12)+"_"+"FOLDER_EDIT_PROPERTIES_THREE";
	public static final String FOLDER_EDIT_PROPERTIES_FOUR = RandomStringUtils.randomAlphanumeric(12)+"_"+"FOLDER_EDIT_PROPERTIES_FOUR";
	
	public static final String FOLDER_MODIFY_PERMISSION1 = RandomStringUtils.randomAlphanumeric(12)+"_"+"FOLDER_MODIFY_PERMISSION1";
	public static final String FOLDER_MODIFY_PERMISSION1_TWO  = RandomStringUtils.randomAlphanumeric(12)+"_"+"FOLDER_MODIFY_PERMISSION1_TWO";
	public static final String FOLDER_MODIFY_PERMISSION1_THREE = RandomStringUtils.randomAlphanumeric(12)+"_"+"FOLDER_MODIFY_PERMISSION1_THREE";
	public static final String FOLDER_MODIFY_PERMISSION1_FOUR = RandomStringUtils.randomAlphanumeric(12)+"_"+"FOLDER_MODIFY_PERMISSION1_FOUR";

	public static final String FOLDER_MOVE = RandomStringUtils.randomAlphanumeric(12)+"_"+"FOLDER_MOVE";
	public static final String FOLDER_MOVE_TWO  = RandomStringUtils.randomAlphanumeric(12)+"_"+"FOLDER_MOVE_TWO";
	public static final String FOLDER_MOVE_THREE = RandomStringUtils.randomAlphanumeric(12)+"_"+"FOLDER_MOVE_THREE";
	public static final String FOLDER_MOVE_FOUR = RandomStringUtils.randomAlphanumeric(12)+"_"+"FOLDER_MOVE_FOUR";

	public static final String INVALID_LOGIN_BOX = RandomStringUtils.randomAlphanumeric(12)+"_"+"INVALID_LOGIN_BOX";
	public static final String INVALID_LOGIN_BOX_TWO  = RandomStringUtils.randomAlphanumeric(12)+"_"+"INVALID_LOGIN_BOX_TWO";
	public static final String INVALID_LOGIN_BOX_THREE = RandomStringUtils.randomAlphanumeric(12)+"_"+"INVALID_LOGIN_BOX_THREE";
	public static final String INVALID_LOGIN_BOX_FOUR = RandomStringUtils.randomAlphanumeric(12)+"_"+"INVALID_LOGIN_BOX_FOUR";

	public static final String LOGIN_BOX = RandomStringUtils.randomAlphanumeric(12)+"_"+"LOGIN_BOX";

	public static final String LOGOUT_BOX = RandomStringUtils.randomAlphanumeric(12)+"_"+"LOGOUT_BOX";
	public static final String LOGOUT_BOX_TWO  = RandomStringUtils.randomAlphanumeric(12)+"_"+"LOGOUT_BOX_TWO";
	public static final String LOGOUT_BOX_THREE = RandomStringUtils.randomAlphanumeric(12)+"_"+"LOGOUT_BOX_THREE";
	public static final String LOGOUT_BOX_FOUR = RandomStringUtils.randomAlphanumeric(12)+"_"+"LOGOUT_BOX_FOUR";
	
										/*FOR SALESFORCE 1*/
	public static final String CREATE_ONE_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"CREATE_ONE_SF";
	public static final String CREATE_TWO_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"CREATE_TWO_SF";
	public static final String CREATE_THREE_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"CREATE_THREE_SF";
	public static final String CREATE_FOUR_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"CREATE_FOUR_SF";
	
	public static final String DELETE_ONE_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"DELETE_ONE_SF";
	public static final String DELETE_TWO_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"DELETE_TWO_SF";
	public static final String DELETE_THREE_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"DELETE_THREE_SF";
	public static final String DELETE_FOUR_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"DELETE_FOUR_SF";
	
	public static final String DOWNLOAD_ONE_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"DOWNLOAD_ONE_SF";
	public static final String DOWNLOAD_TWO_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"DOWNLOAD_TWO_SF";
	public static final String DOWNLOAD_THREE_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"DOWNLOAD_THREE_SF";
	public static final String DOWNLOAD_FOUR_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"DOWNLOAD_FOUR_SF";	
	
	public static final String EDIT_ONE_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"EDIT_ONE_SF";
	public static final String EDIT_TWO_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"EDIT_TWO_SF";
	public static final String EDIT_THREE_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"EDIT_THREE_SF";
	public static final String EDIT_FOUR_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"EDIT_FOUR_SF";
	
	public static final String EXPORT_ONE_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"EXPORT_ONE_SF";
	public static final String EXPORT_TWO_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"EXPORT_TWO_SF";
	public static final String EXPORT_THREE_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"EXPORT_THREE_SF";
	public static final String EXPORT_FOUR_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"EXPORT_FOUR_SF";
	
	public static final String RUN_ONE_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"RUN_ONE_SF";
	public static final String RUN_TWO_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"RUN_TWO_SF";
	public static final String RUN_THREE_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"RUN_THREE_SF";
	public static final String RUN_FOUR_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"RUN_FOUR_SF";
	
	public static final String SHARE_ONE_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"SHARE_ONE_SF";
	public static final String SHARE_TWO_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"SHARE_TWO_SF";
	public static final String SHARE_THREE_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"SHARE_THREE_SF";
	public static final String SHARE_FOUR_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"SHARE_FOUR_SF";
	
	public static final String UPLOAD_ONE_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"UPLOAD_ONE_SF";
	public static final String UPLOAD_TWO_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"UPLOAD_TWO_SF";
	public static final String UPLOAD_THREE_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"UPLOAD_THREE_SF";
	public static final String UPLOAD_FOUR_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"UPLOAD_FOUR_SF";
	
	public static final String VIEW_ONE_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"VIEW_ONE_SF";
	public static final String VIEW_TWO_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"VIEW_TWO_SF";
	public static final String VIEW_THREE_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"VIEW_THREE_SF";
	public static final String VIEW_FOUR_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"VIEW_FOUR_SF";
	
	
	public static final String CREATE_SHARE_ONE_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"CREATE_SHARE_ONE_SF";
	public static final String CREATE_SHARE_TWO_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"CREATE_SHARE_TWO_SF";
	public static final String CREATE_SHARE_THREE_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"CREATE_SHARE_THREE_SF";
	public static final String CREATE_SHARE_FOUR_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"CREATE_SHARE_FOUR_SF";
	
	public static final String UPLOAD_VIEW_SHARE_ONE_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"UPLOAD_VIEW_SHARE_ONE_SF";
	public static final String UPLOAD_VIEW_SHARE_TWO_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"UPLOAD_VIEW_SHARE_TWO_SF";
	public static final String UPLOAD_VIEW_SHARE_THREE_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"UPLOAD_VIEW_SHARE_THREE_SF";
	public static final String UPLOAD_VIEW_SHARE_FOUR_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"UPLOAD_VIEW_SHARE_FOUR_SF";
	
	public static final String DOWNLOAD_UPLOAD_ONE_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"DOWNLOAD_UPLOAD_ONE_SF";
	public static final String DOWNLOAD_UPLOAD_TWO_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"DOWNLOAD_UPLOAD_TWO_SF";
	public static final String DOWNLOAD_UPLOAD_THREE_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"DOWNLOAD_UPLOAD_THREE_SF";
	public static final String DOWNLOAD_UPLOAD_FOUR_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"DOWNLOAD_UPLOAD_FOUR_SF";
	
	public static final String CREATE_VIEW_SHARE_DELETE_ONE_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"CREATE_VIEW_SHARE_DELETE_ONE_SF";
	public static final String CREATE_VIEW_SHARE_DELETE_TWO_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"CREATE_VIEW_SHARE_DELETE_TWO_SF";
	public static final String CREATE_VIEW_SHARE_DELETE_THREE_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"CREATE_VIEW_SHARE_DELETE_THREE_SF";
	public static final String CREATE_VIEW_SHARE_DELETE_FOUR_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"CREATE_VIEW_SHARE_DELETE_FOUR_SF";
	
	public static final String UPLOAD_VIEW_SHARE_DELETE_ONE_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"UPLOAD_VIEW_SHARE_DELETE_ONE_SF";
	public static final String UPLOAD_VIEW_SHARE_DELETE_TWO_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"UPLOAD_VIEW_SHARE_DELETE_TWO_SF";
	public static final String UPLOAD_VIEW_SHARE_DELETE_THREE_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"UPLOAD_VIEW_SHARE_DELETE_THREE_SF";
	public static final String UPLOAD_VIEW_SHARE_DELETE_FOUR_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"UPLOAD_VIEW_SHARE_DELETE_FOUR_SF";
	
	public static final String DOWNLOAD_DELETE_ONE_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"DOWNLOAD_DELETE_ONE_SF";
	public static final String DOWNLOAD_DELETE_TWO_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"DOWNLOAD_DELETE_TWO_SF";
	public static final String DOWNLOAD_DELETE_THREE_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"DOWNLOAD_DELETE_THREE_SF";
	public static final String DOWNLOAD_DELETE_FOUR_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"DOWNLOAD_DELETE_FOUR_SF";
	
	public static final String UPLOAD_VIEW_EDIT_SHARE_DELETE_ONE_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"UPLOAD_VIEW_EDIT_SHARE_DELETE_ONE_SF";
	public static final String UPLOAD_VIEW_EDIT_SHARE_DELETE_TWO_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"UPLOAD_VIEW_EDIT_SHARE_DELETE_TWO_SF";
	public static final String UPLOAD_VIEW_EDIT_SHARE_DELETE_THREE_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"UPLOAD_VIEW_EDIT_SHARE_DELETE_THREE_SF";
	public static final String UPLOAD_VIEW_EDIT_SHARE_DELETE_FOUR_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"UPLOAD_VIEW_EDIT_SHARE_DELETE_FOUR_SF";
	
	public static final String UPLOAD_GMAIL = RandomStringUtils.randomAlphanumeric(12)+"_"+"UPLOAD_GMAIL";
	public static final String UPLOAD_TWO_GMAIL = RandomStringUtils.randomAlphanumeric(12)+"_"+"UPLOAD_TWO_GMAIL";
	public static final String UPLOAD_THREE_GMAIL = RandomStringUtils.randomAlphanumeric(12)+"_"+"UPLOAD_THREE_GMAIL";
	public static final String UPLOAD_FOUR_GMAIL = RandomStringUtils.randomAlphanumeric(12)+"_"+"UPLOAD_FOUR_GMAIL";
	
	public static final String DELETE_ACCOUNT_ONE_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"DELETE_ACCOUNT_ONE_SF";
	public static final String DELETE_ACCOUNT_ONE_SF_TWO_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"DELETE_ACCOUNT_ONE_SF_TWO_SF";
	public static final String DELETE_ACCOUNT_ONE_SF_THREE_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"DELETE_ACCOUNT_ONE_SF_THREE_SF";
	public static final String DELETE_ACCOUNT_ONE_SF_FOUR_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"DELETE_ACCOUNT_ONE_SF_FOUR_SF";
	
	public static final String VIEW_ACCOUNT_ONE_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"VIEW_ACCOUNT_ONE_SF";
	public static final String VIEW_ACCOUNT_ONE_SF_TWO_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"VIEW_ACCOUNT_ONE_SF_TWO_SF";
	public static final String VIEW_ACCOUNT_ONE_SF_THREE_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"VIEW_ACCOUNT_ONE_SF_THREE_SF";
	public static final String VIEW_ACCOUNT_ONE_SF_FOUR_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"VIEW_ACCOUNT_ONE_SF_FOUR_SF";
	
	public static final String CONTACT_CREATE_ONE_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"CONTACT_CREATE_ONE_SF";
	public static final String CONTACT_CREATE_TWO_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"CONTACT_CREATE_TWO_SF";
	public static final String CONTACT_CREATE_THREE_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"CONTACT_CREATE_THREE_SF";
	public static final String CONTACT_CREATE_FOUR_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"CONTACT_CREATE_FOUR_SF";
	
	public static final String CONTACT_DELETE_ONE_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"CONTACT_DELETE_ONE_SF";
	public static final String CONTACT_DELETE_TWO_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"CONTACT_DELETE_TWO_SF";
	public static final String CONTACT_DELETE_THREE_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"CONTACT_DELETE_THREE_SF";
	public static final String CONTACT_DELETE_FOUR_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"CONTACT_DELETE_FOUR_SF";
	
	public static final String CONTACT_EDIT_ONE_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"CONTACT_EDIT_ONE_SF";
	public static final String CONTACT_EDIT_TWO_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"CONTACT_EDIT_TWO_SF";
	public static final String CONTACT_EDIT_THREE_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"CONTACT_EDIT_THREE_SF";
	public static final String CONTACT_EDIT_FOUR_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"CONTACT_EDIT_FOUR_SF";
	
	public static final String CONTACT_VIEW_ONE_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"CONTACT_VIEW_ONE_SF";
	public static final String CONTACT_VIEW_TWO_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"CONTACT_VIEW_TWO_SF";
	public static final String CONTACT_VIEW_THREE_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"CONTACT_VIEW_THREE_SF";
	public static final String CONTACT_VIEW_FOUR_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"CONTACT_VIEW_FOUR_SF";
	
	public static final String LEAD_CREATE_ONE_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"LEAD_CREATE_ONE_SF";
	public static final String LEAD_CREATE_TWO_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"LEAD_CREATE_TWO_SF";
	public static final String LEAD_CREATE_THREE_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"LEAD_CREATE_THREE_SF";
	public static final String LEAD_CREATE_FOUR_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"LEAD_CREATE_FOUR_SF";
	
	public static final String LEAD_EDIT_ONE_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"LEAD_EDIT_ONE_SF";
	public static final String LEAD_EDIT_TWO_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"LEAD_EDIT_TWO_SF";
	public static final String LEAD_EDIT_THREE_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"LEAD_EDIT_THREE_SF";
	public static final String LEAD_EDIT_FOUR_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"LEAD_EDIT_FOUR_SF";
	
	public static final String LEAD_DELETE_ONE_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"LEAD_DELETE_ONE_SF";
	public static final String LEAD_DELETE_TWO_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"LEAD_DELETE_TWO_SF";
	public static final String LEAD_DELETE_THREE_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"LEAD_DELETE_THREE_SF";
	public static final String LEAD_DELETE_FOUR_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"LEAD_DELETE_FOUR_SF";
	
	public static final String OPPORTUNITY_CREATE_ONE_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"OPPORTUNITY_CREATE_ONE_SF";
	public static final String OPPORTUNITY_CREATE_TWO_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"OPPORTUNITY_CREATE_TWO_SF";
	public static final String OPPORTUNITY_CREATE_THREE_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"OPPORTUNITY_CREATE_THREE_SF";
	public static final String OPPORTUNITY_CREATE_FOUR_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"OPPORTUNITY_CREATE_FOUR_SF";
	
	public static final String OPPORTUNITY_EDIT_ONE_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"OPPORTUNITY_EDIT_ONE_SF";
	public static final String OPPORTUNITY_EDIT_TWO_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"OPPORTUNITY_EDIT_TWO_SF";
	public static final String OPPORTUNITY_EDIT_THREE_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"OPPORTUNITY_EDIT_THREE_SF";
	public static final String OPPORTUNITY_EDIT_FOUR_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"OPPORTUNITY_EDIT_FOUR_SF";
	
	public static final String OPPORTUNITY_VIEW_ONE_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"OPPORTUNITY_VIEW_ONE_SF";
	public static final String OPPORTUNITY_VIEW_TWO_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"OPPORTUNITY_VIEW_TWO_SF";
	public static final String OPPORTUNITY_VIEW_THREE_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"OPPORTUNITY_VIEW_THREE_SF";
	public static final String OPPORTUNITY_VIEW_FOUR_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"OPPORTUNITY_VIEW_FOUR_SF";
	
	public static final String OPPORTUNITY_DELETE_ONE_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"OPPORTUNITY_DELETE_ONE_SF";
	public static final String OPPORTUNITY_DELETE_TWO_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"OPPORTUNITY_DELETE_TWO_SF";
	public static final String OPPORTUNITY_DELETE_THREE_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"OPPORTUNITY_DELETE_THREE_SF";
	public static final String OPPORTUNITY_DELETE_FOUR_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"OPPORTUNITY_DELETE_FOUR_SF";
	
	public static final String REPORT_CREATE_ONE_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"REPORT_CREATE_ONE_SF";
	public static final String REPORT_CREATE_TWO_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"REPORT_CREATE_TWO_SF";
	public static final String REPORT_CREATE_THREE_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"REPORT_CREATE_THREE_SF";
	public static final String REPORT_CREATE_FOUR_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"REPORT_CREATE_FOUR_SF";
	
	public static final String REPORT_DELETE_ONE_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"REPORT_DELETE_ONE_SF";
	public static final String REPORT_DELETE_TWO_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"REPORT_DELETE_TWO_SF";
	public static final String REPORT_DELETE_THREE_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"REPORT_DELETE_THREE_SF";
	public static final String REPORT_DELETE_FOUR_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"REPORT_DELETE_FOUR_SF";
	
	public static final String REPORT_EDIT_ONE_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"REPORT_EDIT_ONE_SF";
	public static final String REPORT_EDIT_TWO_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"REPORT_EDIT_TWO_SF";
	public static final String REPORT_EDIT_THREE_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"REPORT_EDIT_THREE_SF";
	public static final String REPORT_EDIT_FOUR_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"REPORT_EDIT_FOUR_SF";
	
	public static final String REPORT_EXPORT_ONE_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"REPORT_EXPORT_ONE_SF";
	public static final String REPORT_EXPORT_TWO_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"REPORT_EXPORT_TWO_SF";
	public static final String REPORT_EXPORT_THREE_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"REPORT_EXPORT_THREE_SF";
	public static final String REPORT_EXPORT_FOUR_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"REPORT_EXPORT_FOUR_SF";
	
	public static final String REPORT_RUN_ONE_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"REPORT_RUN_ONE_SF";
	public static final String REPORT_RUN_TWO_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"REPORT_RUN_TWO_SF";
	public static final String REPORT_RUN_THREE_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"REPORT_RUN_THREE_SF";
	public static final String REPORT_RUN_FOUR_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"REPORT_RUN_FOUR_SF";
	
	public static final String REPORT_VIEW_ONE_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"REPORT_VIEW_ONE_SF";
	public static final String REPORT_VIEW_TWO_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"REPORT_VIEW_TWO_SF";
	public static final String REPORT_VIEW_THREE_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"REPORT_VIEW_THREE_SF";
	public static final String REPORT_VIEW_FOUR_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"REPORT_VIEW_FOUR_SF";
	
	public static final String CONTACT_UPDATE_ONE_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"CONTACT_UPDATE_ONE_SF";
	public static final String CONTACT_UPDATE_TWO_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"CONTACT_UPDATE_TWO_SF";
	public static final String CONTACT_UPDATE_THREE_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"CONTACT_UPDATE_THREE_SF";
	public static final String CONTACT_UPDATE_FOUR_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"CONTACT_UPDATE_FOUR_SF";
	
	public static final String COMMENT_POST_ONE_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"COMMENT_POST_ONE_SF";
	public static final String COMMENT_POST_TWO_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"COMMENT_POST_TWO_SF";
	public static final String COMMENT_POST_THREE_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"COMMENT_POST_THREE_SF";
	public static final String COMMENT_POST_FOUR_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"COMMENT_POST_FOUR_SF";
	
	public static final String FILE_DOWNLOAD_ONE_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"FILE_DOWNLOAD_ONE_SF";
	public static final String FILE_DOWNLOAD_TWO_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"FILE_DOWNLOAD_TWO_SF";
	public static final String FILE_DOWNLOAD_THREE_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"FILE_DOWNLOAD_THREE_SF";
	public static final String FILE_DOWNLOAD_FOUR_SF = RandomStringUtils.randomAlphanumeric(12)+"_"+"FILE_DOWNLOAD_FOUR_SF";

													/*	OFFICE 365	*/	
	public static final String DOWNLOAD_OFFICE = RandomStringUtils.randomAlphanumeric(12)+"_"+"DOWNLOAD_OFFICE";
	public static final String DOWNLOAD_OFFICE_ONE = RandomStringUtils.randomAlphanumeric(12)+"_"+"DOWNLOAD_OFFICE_ONE";
	public static final String DOWNLOAD_OFFICE_TWO = RandomStringUtils.randomAlphanumeric(12)+"_"+"DOWNLOAD_OFFICE_TWO";
	public static final String DOWNLOAD_OFFICE_THREE= RandomStringUtils.randomAlphanumeric(12)+"_"+"DOWNLOAD_OFFICE_THREE";

	
	public static final String UPLOAD_OFFICE = RandomStringUtils.randomAlphanumeric(12)+"_"+"UPLOAD_OFFICE";
	public static final String UPLOAD_OFFICE_TWO = RandomStringUtils.randomAlphanumeric(12)+"_"+"UPLOAD_OFFICE_TWO";
	public static final String UPLOAD_OFFICE_THREE = RandomStringUtils.randomAlphanumeric(12)+"_"+"UPLOAD_OFFICE_THREE";
	public static final String UPLOAD_OFFICE_FOUR = RandomStringUtils.randomAlphanumeric(12)+"_"+"UPLOAD_OFFICE_FOUR";

	public static final String DELETE_OFFICE = RandomStringUtils.randomAlphanumeric(12)+"_"+"DELETE_OFFICE";
	public static final String DELETE_OFFICE_TWO = RandomStringUtils.randomAlphanumeric(12)+"_"+"DELETE_OFFICE";
	public static final String DELETE_OFFICE_THREE = RandomStringUtils.randomAlphanumeric(12)+"_"+"DELETE_OFFICE";
	public static final String DELETE_OFFICE_FOUR = RandomStringUtils.randomAlphanumeric(12)+"_"+"DELETE_OFFICE";

	
	public static final String SHARE_OFFICE = RandomStringUtils.randomAlphanumeric(12)+"_"+"SHARE_OFFICE";
	public static final String SHARE_OFFICE_TWO = RandomStringUtils.randomAlphanumeric(12)+"_"+"SHARE_OFFICE_TWO";
	public static final String SHARE_OFFICE_THREE = RandomStringUtils.randomAlphanumeric(12)+"_"+"SHARE_OFFICE_THREE";
	public static final String SHARE_OFFICE_FOUR = RandomStringUtils.randomAlphanumeric(12)+"_"+"SHARE_OFFICE_FOUR";

	
	public static final String FOLDER_COPY_OFFICE = RandomStringUtils.randomAlphanumeric(12)+"_"+"FOLDER_COPY_OFFICE";
	public static final String FOLDER_COPY_OFFICE_TWO = RandomStringUtils.randomAlphanumeric(12)+"_"+"FOLDER_COPY_OFFICE_TWO";
	public static final String FOLDER_COPY_OFFICE_THREE = RandomStringUtils.randomAlphanumeric(12)+"_"+"FOLDER_COPY_OFFICE_THREE";
	public static final String FOLDER_COPY_OFFICE_FOUR = RandomStringUtils.randomAlphanumeric(12)+"_"+"FOLDER_COPY_OFFICE_FOUR";

	public static final String FOLDER_CREATE_OFFICE = RandomStringUtils.randomAlphanumeric(12)+"_"+"FOLDER_CREATE_OFFICE";
	public static final String FOLDER_CREATE_OFFICE_TWO = RandomStringUtils.randomAlphanumeric(12)+"_"+"FOLDER_CREATE_OFFICE_TWO";
	public static final String FOLDER_CREATE_OFFICE_THREE = RandomStringUtils.randomAlphanumeric(12)+"_"+"FOLDER_CREATE_OFFICE_THREE";
	public static final String FOLDER_CREATE_OFFICE_FOUR = RandomStringUtils.randomAlphanumeric(12)+"_"+"FOLDER_CREATE_OFFICE_FOUR";

	public static final String FOLDER_MOVE_OFFICE = RandomStringUtils.randomAlphanumeric(12)+"_"+"FOLDER_MOVE_OFFICE";
	public static final String FOLDER_MOVE_OFFICE_TWO = RandomStringUtils.randomAlphanumeric(12)+"_"+"FOLDER_MOVE_OFFICE_TWO";
	public static final String FOLDER_MOVE_OFFICE_THREE= RandomStringUtils.randomAlphanumeric(12)+"_"+"FOLDER_MOVE_OFFICE_THREE";
	public static final String FOLDER_MOVE_OFFICE_FOUR = RandomStringUtils.randomAlphanumeric(12)+"_"+"FOLDER_MOVE_OFFICE_FOUR";


	public static final String FOLDER_RENAME_OFFICE = RandomStringUtils.randomAlphanumeric(12)+"_"+"FOLDER_RENAME_OFFICE";
	public static final String FOLDER_RENAME_OFFICE_TWO = RandomStringUtils.randomAlphanumeric(12)+"_"+"FOLDER_RENAME_OFFICE_TWO";
	public static final String FOLDER_RENAME_OFFICE_THREE = RandomStringUtils.randomAlphanumeric(12)+"_"+"FOLDER_RENAME_OFFICE_THREE";
	public static final String FOLDER_RENAME_OFFICE_FOUR= RandomStringUtils.randomAlphanumeric(12)+"_"+"FOLDER_RENAME_OFFICE_FOUR";

	public static final String LOGIN_OFFICE = RandomStringUtils.randomAlphanumeric(12)+"_"+"LOGIN_OFFICE";
	
	public static final String DOCUMENT_EDIT_OFFICE= RandomStringUtils.randomAlphanumeric(12)+"_"+"DOCUMENT_EDIT_OFFICE";
	public static final String DOCUMENT_EDIT_OFFICE_TWO= RandomStringUtils.randomAlphanumeric(12)+"_"+"DOCUMENT_EDIT_OFFICE_TWO";
	public static final String DOCUMENT_EDIT_OFFICE_THREE= RandomStringUtils.randomAlphanumeric(12)+"_"+"DOCUMENT_EDIT_OFFICE_THREE";
	public static final String DOCUMENT_EDIT_OFFICE_FOUR= RandomStringUtils.randomAlphanumeric(12)+"_"+"DOCUMENT_EDIT_OFFICE_FOUR";

	public static final String DOCUMENT_MOVE_OFFICE= RandomStringUtils.randomAlphanumeric(12)+"_"+"DOCUMENT_MOVE_OFFICE";
	public static final String DOCUMENT_MOVE_OFFICE_TWO= RandomStringUtils.randomAlphanumeric(12)+"_"+"DOCUMENT_MOVE_OFFICE_TWO";
	public static final String DOCUMENT_MOVE_OFFICE_THREE= RandomStringUtils.randomAlphanumeric(12)+"_"+"DOCUMENT_MOVE_OFFICE_THREE";
	public static final String DOCUMENT_MOVE_OFFICE_FOUR= RandomStringUtils.randomAlphanumeric(12)+"_"+"DOCUMENT_MOVE_OFFICE_FOUR";

	public static final String DOCUMENT_RENAME_OFFICE= RandomStringUtils.randomAlphanumeric(12)+"_"+"DOCUMENT_RENAME_OFFICE";
	public static final String DOCUMENT_RENAME_OFFICE_TWO= RandomStringUtils.randomAlphanumeric(12)+"_"+"DOCUMENT_RENAME_OFFICE_TWO";
	public static final String DOCUMENT_RENAME_OFFICE_THREE= RandomStringUtils.randomAlphanumeric(12)+"_"+"DOCUMENT_RENAME_OFFICE_THREE";
	public static final String DOCUMENT_RENAME_OFFICE_FOUR= RandomStringUtils.randomAlphanumeric(12)+"_"+"DOCUMENT_RENAME_OFFICE_FOUR";

	public static final String DOCUMENT_RESTORE_OFFICE= RandomStringUtils.randomAlphanumeric(12)+"_"+"DOCUMENT_RESTORE_OFFICE";
	public static final String DOCUMENT_RESTORE_OFFICE_TWO= RandomStringUtils.randomAlphanumeric(12)+"_"+"DOCUMENT_RESTORE_OFFICE_TWO";
	public static final String DOCUMENT_RESTORE_OFFICE_THREE= RandomStringUtils.randomAlphanumeric(12)+"_"+"DOCUMENT_RESTORE_OFFICE_THREE";
	public static final String DOCUMENT_RESTORE_OFFICE_FOUR= RandomStringUtils.randomAlphanumeric(12)+"_"+"DOCUMENT_RESTORE_OFFICE_FOUR";

	public static final String DOCUMENT_SHARE_OFFICE= RandomStringUtils.randomAlphanumeric(12)+"_"+"DOCUMENT_SHARE_OFFICE";
	public static final String DOCUMENT_SHARE_OFFICE_TWO= RandomStringUtils.randomAlphanumeric(12)+"_"+"DOCUMENT_SHARE_OFFICE_TWO";
	public static final String DOCUMENT_SHARE_OFFICE_THREE= RandomStringUtils.randomAlphanumeric(12)+"_"+"DOCUMENT_SHARE_OFFICE_THREE";
	public static final String DOCUMENT_SHARE_OFFICE_FOUR= RandomStringUtils.randomAlphanumeric(12)+"_"+"DOCUMENT_SHARE_OFFICE_FOUR";

	public static final String DOCUMENT_UNSHARE_OFFICE= RandomStringUtils.randomAlphanumeric(12)+"_"+"DOCUMENT_UNSHARE_OFFICE";
	public static final String DOCUMENT_UNSHARE_OFFICE_TWO= RandomStringUtils.randomAlphanumeric(12)+"_"+"DOCUMENT_UNSHARE_OFFICE_TWO";
	public static final String DOCUMENT_UNSHARE_OFFICE_THREE= RandomStringUtils.randomAlphanumeric(12)+"_"+"DOCUMENT_UNSHARE_OFFICE_THREE";
	public static final String DOCUMENT_UNSHARE_OFFICE_FOUR= RandomStringUtils.randomAlphanumeric(12)+"_"+"DOCUMENT_UNSHARE_OFFICE_FOUR";

	public static final String VIEW_FOLDER_OFFICE= RandomStringUtils.randomAlphanumeric(12)+"_"+"VIEW_FOLDER_OFFICE";
	public static final String VIEW_FOLDER_OFFICE_TWO= RandomStringUtils.randomAlphanumeric(12)+"_"+"VIEW_FOLDER_OFFICE_TWO";
	public static final String VIEW_FOLDER_OFFICE_THREE= RandomStringUtils.randomAlphanumeric(12)+"_"+"VIEW_FOLDER_OFFICE_THREE";
	public static final String VIEW_FOLDER_OFFICE_FOUR= RandomStringUtils.randomAlphanumeric(12)+"_"+"VIEW_FOLDER_OFFICE_FOUR";

	public static final String RECYCLE_BIN_EMPTY_OFFICE= RandomStringUtils.randomAlphanumeric(12)+"_"+"RECYCLE_BIN_EMPTY_OFFICE";
	public static final String RECYCLE_BIN_EMPTY_OFFICE_TWO= RandomStringUtils.randomAlphanumeric(12)+"_"+"RECYCLE_BIN_EMPTY_OFFICE_TWO";
	public static final String RECYCLE_BIN_EMPTY_OFFICE_THREE= RandomStringUtils.randomAlphanumeric(12)+"_"+"RECYCLE_BIN_EMPTY_OFFICE_THREE";
	public static final String RECYCLE_BIN_EMPTY_OFFICE_FOUR= RandomStringUtils.randomAlphanumeric(12)+"_"+"RECYCLE_BIN_EMPTY_OFFICE_FOUR";
	
	public static final String DOCUMENT_CREATE_OFFICE= RandomStringUtils.randomAlphanumeric(12)+"_"+"DOCUMENT_CREATE_OFFICE";
	public static final String DOCUMENT_CREATE_OFFICE_TWO= RandomStringUtils.randomAlphanumeric(12)+"_"+"DOCUMENT_CREATE_OFFICE_TWO";
	public static final String DOCUMENT_CREATE_OFFICE_THREE= RandomStringUtils.randomAlphanumeric(12)+"_"+"DOCUMENT_CREATE_OFFICE_THREE";
	public static final String DOCUMENT_CREATE_OFFICE_FOUR= RandomStringUtils.randomAlphanumeric(12)+"_"+"DOCUMENT_CREATE_OFFICE_FOUR";

	public static final String ADD = RandomStringUtils.randomAlphanumeric(12)+"_"+"ADD";
	public static final String ADD_TWO = RandomStringUtils.randomAlphanumeric(12)+"_"+"ADD_TWO";
	public static final String ADD_THREE = RandomStringUtils.randomAlphanumeric(12)+"_"+"ADD_THREE";
	public static final String ADD_FOUR = RandomStringUtils.randomAlphanumeric(12)+"_"+"ADD_FOUR";
	
	public static final String LOCK = RandomStringUtils.randomAlphanumeric(12)+"_"+"LOCK";
	public static final String LOCK_TWO = RandomStringUtils.randomAlphanumeric(12)+"_"+"LOCK_TWO";
	public static final String LOCK_THREE = RandomStringUtils.randomAlphanumeric(12)+"_"+"LOCK_THREE";
	public static final String LOCK_FOUR = RandomStringUtils.randomAlphanumeric(12)+"_"+"LOCK_FOUR";
	
	public static final String UNLOCK = RandomStringUtils.randomAlphanumeric(12)+"_"+"UNLOCK";
	public static final String UNLOCK_TWO = RandomStringUtils.randomAlphanumeric(12)+"_"+"UNLOCK_TWO";
	public static final String UNLOCK_THREE = RandomStringUtils.randomAlphanumeric(12)+"_"+"UNLOCK_THREE";
	public static final String UNLOCK_FOUR = RandomStringUtils.randomAlphanumeric(12)+"_"+"UNLOCK_FOUR";
	
	public static final String COPY =  RandomStringUtils.randomAlphanumeric(12)+"_"+"COPY";
	public static final String COPY_TWO =  RandomStringUtils.randomAlphanumeric(12)+"_"+"COPY_TWO";
	public static final String COPY_THREE =  RandomStringUtils.randomAlphanumeric(12)+"_"+"COPY_THREE";
	public static final String COPY_FOUR =  RandomStringUtils.randomAlphanumeric(12)+"_"+"COPY_FOUR";
	
	public static final String EXPIRE_SHARE = RandomStringUtils.randomAlphanumeric(12)+"_"+"EXPIRE_SHARE";
	public static final String EXPIRE_SHARE_TWO = RandomStringUtils.randomAlphanumeric(12)+"_"+"EXPIRE_SHARE_TWO";
	public static final String EXPIRE_SHARE_THREE = RandomStringUtils.randomAlphanumeric(12)+"_"+"EXPIRE_SHARE_THREE";
	public static final String EXPIRE_SHARE_FOUR = RandomStringUtils.randomAlphanumeric(12)+"_"+"EXPIRE_SHARE_FOUR";
	
	public static final String UNDELETE = RandomStringUtils.randomAlphanumeric(12)+"_"+"UNDELETE";
	public static final String UNDELETE_TWO  = RandomStringUtils.randomAlphanumeric(12)+"_"+"UNDELETE_TWO";
	public static final String UNDELETE_THREE = RandomStringUtils.randomAlphanumeric(12)+"_"+"UNDELETE_THREE";
	public static final String UNDELETE_FOUR = RandomStringUtils.randomAlphanumeric(12)+"_"+"UNDELETE_FOUR";
	public static final String ADD_EDIT_DELETE_USER_ONE = RandomStringUtils.randomAlphanumeric(12)+"_"+"ADD_EDIT_DELETE_USER_ONE";
	public static final String ADD_EDIT_DELETE_USER_TWO = RandomStringUtils.randomAlphanumeric(12)+"_"+"ADD_EDIT_DELETE_USER_TWO";
	public static final String ADD_EDIT_DELETE_USER_THREE = RandomStringUtils.randomAlphanumeric(12)+"_"+"ADD_EDIT_DELETE_USER_THREE";
	public static final String ADD_EDIT_DELETE_USER_FOUR = RandomStringUtils.randomAlphanumeric(12)+"_"+"ADD_EDIT_DELETE_USER_FOUR";
	
	public static final String CREATE_EDIT_DELETE_USER_ONE = RandomStringUtils.randomAlphanumeric(12)+"_"+"CREATE_EDIT_DELETE_USER_ONE";
	public static final String CREATE_EDIT_DELETE_USER_TWO = RandomStringUtils.randomAlphanumeric(12)+"_"+"CREATE_EDIT_DELETE_USER_TWO";
	public static final String CREATE_EDIT_DELETE_USER_THREE = RandomStringUtils.randomAlphanumeric(12)+"_"+"CREATE_EDIT_DELETE_USER_THREE";
	public static final String CREATE_EDIT_DELETE_USER_FOUR = RandomStringUtils.randomAlphanumeric(12)+"_"+"CREATE_EDIT_DELETE_USER_FOUR";
	
	public static final String UPLOAD_DOWNLOAD_SHARE_ONE = RandomStringUtils.randomAlphanumeric(12)+"_"+"UPLOAD_DOWNLOAD_SHARE_ONE";
	public static final String UPLOAD_DOWNLOAD_SHARE_TWO = RandomStringUtils.randomAlphanumeric(12)+"_"+"UPLOAD_DOWNLOAD_SHARE_TWO";
	public static final String UPLOAD_DOWNLOAD_SHARE_THREE = RandomStringUtils.randomAlphanumeric(12)+"_"+"UPLOAD_DOWNLOAD_SHARE_THREE";
	public static final String UPLOAD_DOWNLOAD_SHARE_FOUR = RandomStringUtils.randomAlphanumeric(12)+"_"+"UPLOAD_DOWNLOAD_SHARE_FOUR";
	
	public static final String SAHRE_UNSHARE_RENAME_ONE = RandomStringUtils.randomAlphanumeric(12)+"_"+"SAHRE_UNSHARE_RENAME_ONE";
	public static final String SAHRE_UNSHARE_RENAME_TWO = RandomStringUtils.randomAlphanumeric(12)+"_"+"SAHRE_UNSHARE_RENAME_TWO";
	public static final String SAHRE_UNSHARE_RENAME_THREE = RandomStringUtils.randomAlphanumeric(12)+"_"+"SAHRE_UNSHARE_RENAME_THREE";
	public static final String SAHRE_UNSHARE_RENAME_FOUR = RandomStringUtils.randomAlphanumeric(12)+"_"+"SAHRE_UNSHARE_RENAME_FOUR";
	
	public static final String LOCK_UNLOCK_DELETE_ONE = RandomStringUtils.randomAlphanumeric(12)+"_"+"LOCK_UNLOCK_DELETE_ONE";
	public static final String LOCK_UNLOCK_DELETE_TWO = RandomStringUtils.randomAlphanumeric(12)+"_"+"LOCK_UNLOCK_DELETE_TWO";
	public static final String LOCK_UNLOCK_DELETE_THREE = RandomStringUtils.randomAlphanumeric(12)+"_"+"LOCK_UNLOCK_DELETE_THREE";
	public static final String LOCK_UNLOCK_DELETE_FOUR = RandomStringUtils.randomAlphanumeric(12)+"_"+"LOCK_UNLOCK_DELETE_FOUR";
	
	public static final String UPLOAD_COPY_RENAME_ONE = RandomStringUtils.randomAlphanumeric(12)+"_"+"UPLOAD_COPY_RENAME_ONE";
	public static final String UPLOAD_COPY_RENAME_TWO = RandomStringUtils.randomAlphanumeric(12)+"_"+"UPLOAD_COPY_RENAME_TWO";
	public static final String UPLOAD_COPY_RENAME_THREE = RandomStringUtils.randomAlphanumeric(12)+"_"+"UPLOAD_COPY_RENAME_THREE";
	public static final String UPLOAD_COPY_RENAME_FOUR = RandomStringUtils.randomAlphanumeric(12)+"_"+"UPLOAD_COPY_RENAME_FOUR";
	
	public static final String SHARE_UNSHARE_DELETE_ONE = RandomStringUtils.randomAlphanumeric(12)+"_"+"SHARE_UNSHARE_DELETE_ONE";
	public static final String SHARE_UNSHARE_DELETE_TWO = RandomStringUtils.randomAlphanumeric(12)+"_"+"SHARE_UNSHARE_DELETE_TWO";
	public static final String SHARE_UNSHARE_DELETE_THREE = RandomStringUtils.randomAlphanumeric(12)+"_"+"SHARE_UNSHARE_DELETE_THREE";
	public static final String SHARE_UNSHARE_DELETE_FOUR = RandomStringUtils.randomAlphanumeric(12)+"_"+"SHARE_UNSHARE_DELETE_FOUR";
	
	public static final String COPY_REANME_DELETE_ONE = RandomStringUtils.randomAlphanumeric(12)+"_"+"COPY_REANME_DELETE_ONE";
	public static final String COPY_REANME_DELETE_TWO = RandomStringUtils.randomAlphanumeric(12)+"_"+"COPY_REANME_DELETE_TWO";
	public static final String COPY_REANME_DELETE_THREE = RandomStringUtils.randomAlphanumeric(12)+"_"+"COPY_REANME_DELETE_THREE";
	public static final String COPY_REANME_DELETE_FOUR = RandomStringUtils.randomAlphanumeric(12)+"_"+"COPY_REANME_DELETE_FOUR";
	
	public static final String SEND = RandomStringUtils.randomAlphanumeric(12)+"_"+"SEND";
	public static final String SEND_ONE = RandomStringUtils.randomAlphanumeric(12)+"_"+"SEND_ONE";
	public static final String SEND_TWO = RandomStringUtils.randomAlphanumeric(12)+"_"+"SEND_TWO";
	public static final String SEND_THREE = RandomStringUtils.randomAlphanumeric(12)+"_"+"SEND_THREE";
	
	public static final String RECEIVE = RandomStringUtils.randomAlphanumeric(12)+"_"+"RECEIVE";
	public static final String RECEIVE_ONE = RandomStringUtils.randomAlphanumeric(12)+"_"+"RECEIVE_ONE";
	public static final String RECEIVE_TWO = RandomStringUtils.randomAlphanumeric(12)+"_"+"RECEIVE_TWO";
	public static final String RECEIVE_THREE = RandomStringUtils.randomAlphanumeric(12)+"_"+"RECEIVE_THREE";
	
	public static final String CREATE_RECEIVE_SEND = RandomStringUtils.randomAlphanumeric(12)+"_"+"CREATE_RECEIVE_SEND";
	public static final String CREATE_RECEIVE_SEND_TWO = RandomStringUtils.randomAlphanumeric(12)+"_"+"CREATE_RECEIVE_SEND_TWO";
	public static final String CREATE_RECEIVE_SEND_THREE = RandomStringUtils.randomAlphanumeric(12)+"_"+"CREATE_RECEIVE_SEND_THREE";
	public static final String CREATE_RECEIVE_SEND_FOUR = RandomStringUtils.randomAlphanumeric(12)+"_"+"CREATE_RECEIVE_SEND_FOUR";
	
	public static final String RECEIVE_SEND = RandomStringUtils.randomAlphanumeric(12)+"_"+"RECEIVE_SEND";
	public static final String RECEIVE_SEND_TWO = RandomStringUtils.randomAlphanumeric(12)+"_"+"RECEIVE_SEND_TWO";
	public static final String RECEIVE_SEND_THREE = RandomStringUtils.randomAlphanumeric(12)+"_"+"RECEIVE_SEND_THREE";
	public static final String RECEIVE_SEND_FOUR = RandomStringUtils.randomAlphanumeric(12)+"_"+"RECEIVE_SEND_FOUR";
	
	public static final String SEND_RECEIVE_CREATE = RandomStringUtils.randomAlphanumeric(12)+"_"+"SEND_RECEIVE_CREATE";
	public static final String SEND_RECEIVE_CREATE_TWO = RandomStringUtils.randomAlphanumeric(12)+"_"+"SEND_RECEIVE_CREATE_TWO";
	public static final String SEND_RECEIVE_CREATE_THREE = RandomStringUtils.randomAlphanumeric(12)+"_"+"SEND_RECEIVE_CREATE_THREE";
	public static final String SEND_RECEIVE_CREATE_FOUR = RandomStringUtils.randomAlphanumeric(12)+"_"+"SEND_RECEIVE_CREATE_FOUR";
	
	public static final String EXCEL_WORKBOOK_VIEW = RandomStringUtils.randomAlphanumeric(12)+"_"+"EXCEL_WORKBOOK_VIEW";
	public static final String EXCEL_WORKBOOK_VIEW_TWO = RandomStringUtils.randomAlphanumeric(12)+"_"+"EXCEL_WORKBOOK_VIEW_TWO";
	public static final String EXCEL_WORKBOOK_VIEW_THREE = RandomStringUtils.randomAlphanumeric(12)+"_"+"EXCEL_WORKBOOK_VIEW_THREE";
	public static final String EXCEL_WORKBOOK_VIEW_FOUR = RandomStringUtils.randomAlphanumeric(12)+"_"+"EXCEL_WORKBOOK_VIEW_FOUR";
	
	public static final String DOCUMENT_DELETE = RandomStringUtils.randomAlphanumeric(12)+"_"+"DOCUMENT_DELETE";
	public static final String DOCUMENT_DELETE_TWO = RandomStringUtils.randomAlphanumeric(12)+"_"+"DOCUMENT_DELETE_TWO";
	public static final String DOCUMENT_DELETE_THREE = RandomStringUtils.randomAlphanumeric(12)+"_"+"DOCUMENT_DELETE_THREE";
	public static final String DOCUMENT_DELETE_FOUR = RandomStringUtils.randomAlphanumeric(12)+"_"+"DOCUMENT_DELETE_FOUR";
	
	public static final String DOWNLOAD_VIEW_DELETE_OFFICE =  RandomStringUtils.randomAlphanumeric(12)+"_"+"DOWNLOAD_VIEW_DELETE_OFFICE";
	public static final String DOWNLOAD_VIEW_DELETE_OFFICE_TWO =  RandomStringUtils.randomAlphanumeric(12)+"_"+"DOWNLOAD_VIEW_DELETE_OFFICE_TWO";
	public static final String DOWNLOAD_VIEW_DELETE_OFFICE_THREE =  RandomStringUtils.randomAlphanumeric(12)+"_"+"DOWNLOAD_VIEW_DELETE_OFFICE_THREE";
	public static final String DOWNLOAD_VIEW_DELETE_OFFICE_FOUR =  RandomStringUtils.randomAlphanumeric(12)+"_"+"DOWNLOAD_VIEW_DELETE_OFFICE_FOUR";
	
	public static final String COPY_MOVE_RENAME_OFFICE = RandomStringUtils.randomAlphanumeric(12)+"_"+"COPY_MOVE_RENAME_OFFICE";
	public static final String COPY_MOVE_RENAME_OFFICE_TWO = RandomStringUtils.randomAlphanumeric(12)+"_"+"COPY_MOVE_RENAME_OFFICE_TWO";
	public static final String COPY_MOVE_RENAME_OFFICE_THREE = RandomStringUtils.randomAlphanumeric(12)+"_"+"COPY_MOVE_RENAME_OFFICE_THREE";
	public static final String COPY_MOVE_RENAME_OFFICE_FOUR = RandomStringUtils.randomAlphanumeric(12)+"_"+"COPY_MOVE_RENAME_OFFICE_FOUR";
	
	public static final String SHARE_RENAME_OFFICE = RandomStringUtils.randomAlphanumeric(12)+"_"+"SHARE_RENAME_OFFICE";
	public static final String SHARE_RENAME_OFFICE_TWO = RandomStringUtils.randomAlphanumeric(12)+"_"+"SHARE_RENAME_OFFICE_TWO";
	public static final String SHARE_RENAME_OFFICE_THREE = RandomStringUtils.randomAlphanumeric(12)+"_"+"SHARE_RENAME_OFFICE_THREE";
	public static final String SHARE_RENAME_OFFICE_FOUR = RandomStringUtils.randomAlphanumeric(12)+"_"+"SHARE_RENAME_OFFICE_FOUR";
	
	public static final String VIEW_RENAME_OFFICE = RandomStringUtils.randomAlphanumeric(12)+"_"+"VIEW_RENAME_OFFICE";
	public static final String VIEW_RENAME_OFFICE_TWO = RandomStringUtils.randomAlphanumeric(12)+"_"+"VIEW_RENAME_OFFICE_TWO";
	public static final String VIEW_RENAME_OFFICE_THREE = RandomStringUtils.randomAlphanumeric(12)+"_"+"VIEW_RENAME_OFFICE_THREE";
	public static final String VIEW_RENAME_OFFICE_FOUR = RandomStringUtils.randomAlphanumeric(12)+"_"+"VIEW_RENAME_OFFICE_FOUR";
	
	public static final String CREATE_RENAME_OFFICE =RandomStringUtils.randomAlphanumeric(12)+"_"+"CREATE_RENAME_OFFICE";
	public static final String CREATE_RENAME_OFFICE_TWO = RandomStringUtils.randomAlphanumeric(12)+"_"+"CREATE_RENAME_OFFICE_TWO";
	public static final String CREATE_RENAME_OFFICE_THREE = RandomStringUtils.randomAlphanumeric(12)+"_"+"CREATE_RENAME_OFFICE_THREE";
	public static final String CREATE_RENAME_OFFICE_FOUR = RandomStringUtils.randomAlphanumeric(12)+"_"+"CREATE_RENAME_OFFICE_FOUR";
	
	public static final String CREATE_DELETE_OFFICE =RandomStringUtils.randomAlphanumeric(12)+"_"+"CREATE_DELETE_OFFICE";
	public static final String CREATE_DELETE_OFFICE_TWO = RandomStringUtils.randomAlphanumeric(12)+"_"+"CREATE_DELETE_OFFICE_TWO";
	public static final String CREATE_DELETE_OFFICE_THREE = RandomStringUtils.randomAlphanumeric(12)+"_"+"CREATE_DELETE_OFFICE_THREE";
	public static final String CREATE_DELETE_OFFICE_FOUR = RandomStringUtils.randomAlphanumeric(12)+"_"+"CREATE_DELETE_OFFICE_FOUR";
	
	public static final String VIEW_MOVE_OFFICE = RandomStringUtils.randomAlphanumeric(12)+"_"+"VIEW_MOVE_OFFICE";
	public static final String VIEW_MOVE_OFFICE_TWO = RandomStringUtils.randomAlphanumeric(12)+"_"+"VIEW_MOVE_OFFICE_TWO";
	public static final String VIEW_MOVE_OFFICE_THREE = RandomStringUtils.randomAlphanumeric(12)+"_"+"VIEW_MOVE_OFFICE_THREE";
	public static final String VIEW_MOVE_OFFICE_FOUR = RandomStringUtils.randomAlphanumeric(12)+"_"+"VIEW_MOVE_OFFICE_FOUR";
	
	public static final String DELETE_EMAIL = RandomStringUtils.randomAlphanumeric(12)+"_"+"DELETE_EMAIL";
	public static final String DELETE_EMAIL_TWO = RandomStringUtils.randomAlphanumeric(12)+"_"+"DELETE_EMAIL_TWO";
	public static final String DELETE_EMAIL_THREE = RandomStringUtils.randomAlphanumeric(12)+"_"+"DELETE_EMAIL_THREE";
	public static final String DELETE_EMAIL_FOUR = RandomStringUtils.randomAlphanumeric(12)+"_"+"DELETE_EMAIL_FOUR";
	
	public static final String DOWNLOAD_ATTACH =  RandomStringUtils.randomAlphanumeric(12)+"_"+"DOWNLOAD_ATTACH";
	public static final String DOWNLOAD_ATTACH_TWO =  RandomStringUtils.randomAlphanumeric(12)+"_"+"DOWNLOAD_ATTACH_TWO";
	public static final String DOWNLOAD_ATTACH_THREE =  RandomStringUtils.randomAlphanumeric(12)+"_"+"DOWNLOAD_ATTACH_THREE";
	public static final String DOWNLOAD_ATTACH_FOUR =  RandomStringUtils.randomAlphanumeric(12)+"_"+"DOWNLOAD_ATTACH_FOUR";
	
	public static final String UPLOAD_ATTACH = RandomStringUtils.randomAlphanumeric(12)+"_"+"UPLOAD_ATTACH";
	public static final String UPLOAD_ATTACH_TWO = RandomStringUtils.randomAlphanumeric(12)+"_"+"UPLOAD_ATTACH_TWO";
	public static final String UPLOAD_ATTACH_THREE = RandomStringUtils.randomAlphanumeric(12)+"_"+"UPLOAD_ATTACH_THREE";
	public static final String UPLOAD_ATTACH_FOUR = RandomStringUtils.randomAlphanumeric(12)+"_"+"UPLOAD_ATTACH_FOUR";
	

	
	
	public static Set<String> getListOfSequenceDetectors(){
		Set<String> sequenceDetectorList = new HashSet<>();
		Field[] fields = SequenceDetectorConstants.class.getFields();
		for (Field field : fields) {
			String sequenceDetector = field.getName();//.toString().replace("public static final java.lang.String com.elastica.beatle.detect.SequenceDetectorConstants.","");
			sequenceDetectorList.add(sequenceDetector);
		}
		return sequenceDetectorList;
	}

}
