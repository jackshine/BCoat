package com.elastica.beatle.tests.gateway;

import static org.testng.Assert.assertTrue;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.testng.Reporter;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.elastica.beatle.gateway.CommonConfiguration;
import com.elastica.beatle.gateway.GatewayTestConstants;
import com.elastica.beatle.gateway.LogValidator;
import com.elastica.beatle.gateway.PolicyAccessEnforcement;
import com.elastica.beatle.gateway.dto.GWForensicSearchResults;


/*******************Author**************
 * 
 * @author Rocky
 *
 */

public class UsersGroupsO365OneDriveTest extends CommonConfiguration {

	String currentTimeInJodaFormat;
	GWForensicSearchResults fsr;
	ArrayList<String> messages = new ArrayList<String>();
	ArrayList<String> objectTypeList = new ArrayList<String>();
	ArrayList<String> objectNameList = new ArrayList<String>();
	ArrayList<String> severityList = new ArrayList<String>();
	LogValidator logValidator;
	String userLitral="User";
	Map <String, String> data = new HashMap<String, String>();
	String policyName="PolicyFT_FileType";
	Map<String, String>policyDataMap= new HashMap<String, String>(); 
	
	@Test(dataProvider = "FileSharing_Groups")
	public void verify_FileSharingPolicyOnGroups(String senderGroup, String receiverGroup, String policyByGroup, String policyWithGroup, Boolean policyBlock, Boolean resBlocked, Boolean resNotify) throws Exception{
		Reporter.log("Validate Users and Groups based File Sharing policy", true);
		
		String sender = "testuser1@gatewayo365beatle.com";
		String receiver = "admin@gatewayo365beatle.com";
		String policyName = "UsersGroupsPolicy";
		String logFile = "testuser1,admin,fileshare.log";
		
		addUserToGroup(sender, senderGroup);
		addUserToGroup(receiver, receiverGroup);
		
		policyDataMap.clear();
		policyDataMap.put(GatewayTestConstants.POLICY_NAME, policyName);
		policyDataMap.put(GatewayTestConstants.APPLICATIONS, "Office 365");
		policyDataMap.put(GatewayTestConstants.SHARED_BY_GROUP, senderGroup);
		policyDataMap.put(GatewayTestConstants.SHARE_WITH_GROUP, receiverGroup);
		if (policyBlock) {
			policyDataMap.put(GatewayTestConstants.BLOCK_SHARE, "BLOCK_SHARE");
		} else {
			policyDataMap.put(GatewayTestConstants.BLOCK_SHARE, "NOTIFY_ONLY");
		}
		PolicyAccessEnforcement.fileSharingPolicyCreateEnable(suiteData, requestHeader, policyDataMap);
		
		replayLogs(logFile);
		
		removeUserFromGroup(sender, senderGroup);
		removeUserFromGroup(receiver, receiverGroup);
		PolicyAccessEnforcement.deletePolicy(suiteData, requestHeader, policyDataMap);
		
		String expectedMsg = "User sent email invitation(s) to admin@gatewayO365beatle.com for Test.docx.";
		data.clear();
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		boolean logValidation = validateLogsFields(fetchElasticSearchLogsUniversal(), data);
		if (resBlocked) {
			assertTrue(!logValidation, "Sharing Activity should be BLOCKED but Activity Logs on Investigate page is matched or SHOWN." );
		} else {
			assertTrue(logValidation, "Sharing Activity Logs on Investigate page does NOT match or NOT SHOWN." );
		}
		
		if (resNotify || resBlocked) {
			expectedMsg = "[ALERT] " + sender + " attempted to share content:test.docxwith external user(s):admin@gatewayo365beatle.com violating policy:" + policyName + "";
			data.clear();
			data.put("message", expectedMsg);
			data.put("account_type", "Internal");
			assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), "Alert Message for Sharing Activity on Investigate is NOT generated." );
		}
	}
	
	@DataProvider
	public Object[][] FileSharing_Groups() {
		return new Object[][]{
			//senderGroup, receiverGroup, policyByGroup, policyWithGroup, policyBlock, resBlocked, resNotify
			{"GroupA", "", "GroupA", "", false, false, true},
			{"GroupA", "", "GroupA", "", true, true, false},
//			{"", "", "GroupA", "", true, false, false},
			{"GroupA", "GroupB", "GroupA", "GroupB", true, true, false},
			{"GroupA", "", "GroupA", "GroupB", true, false, false},
			{"GroupA", "GroupC", "GroupA", "GroupB", true, false, false},
			{"GroupB", "GroupA", "GroupA", "GroupB", true, false, false},
			{"GroupB", "", "GroupA", "GroupB", true, false, false},
			{"GroupB", "GroupC", "GroupA", "GroupB", true, false, false},
		};	
	}

	@Test(dataProvider = "FileSharing_Users")
	public void verify_FileSharingPolicyOnUsers(String senderGroup, String receiverGroup, String policyByUser, String policyWithUser, Boolean policyBlock, Boolean resBlocked, Boolean resNotify) throws Exception{
		Reporter.log("Validate Users and Groups based File Sharing policy", true);
		
		String sender = "testuser1@gatewayo365beatle.com";
		String receiver = "admin@gatewayo365beatle.com";
		String policyName = "UsersGroupsPolicy";
		String logFile = "testuser1,admin,fileshare.log";
		
		addUserToGroup(sender, senderGroup);
		addUserToGroup(receiver, receiverGroup);
		
		policyDataMap.clear();
		policyDataMap.put(GatewayTestConstants.POLICY_NAME, policyName);
		policyDataMap.put(GatewayTestConstants.APPLICATIONS, "Office 365");
		policyDataMap.put(GatewayTestConstants.SHARED_BY, policyByUser);
//		policyDataMap.put(GatewayTestConstants.NOTIFY_EMAILID, "");
		policyDataMap.put(GatewayTestConstants.SHARE_WITH, policyWithUser);
//		policyDataMap.put(GatewayTestConstants.FILE_TYPE, "");
//		policyDataMap.put(GatewayTestConstants.FILE_NAME, "");
//		policyDataMap.put(GatewayTestConstants.SHARED_BY_GROUP, senderGroup);
//		policyDataMap.put(GatewayTestConstants.SHARE_WITH_GROUP, receiverGroup);
		if (policyBlock) {
			policyDataMap.put(GatewayTestConstants.BLOCK_SHARE, "BLOCK_SHARE");
		} else {
			policyDataMap.put(GatewayTestConstants.BLOCK_SHARE, "NOTIFY_ONLY");
		}
		PolicyAccessEnforcement.fileSharingPolicyCreateEnable(suiteData, requestHeader, policyDataMap);
		
		replayLogs(logFile);
		
		removeUserFromGroup(sender, senderGroup);
		removeUserFromGroup(receiver, receiverGroup);
		PolicyAccessEnforcement.deletePolicy(suiteData, requestHeader, policyDataMap);
		
		String expectedMsg = "User sent email invitation(s) to admin@gatewayO365beatle.com for Test.docx.";
		data.clear();
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		boolean logValidation = validateLogsFields(fetchElasticSearchLogsUniversal(), data);
		if (resBlocked) {
			assertTrue(!logValidation, "Sharing Activity should be BLOCKED but Activity Logs on Investigate page is matched or SHOWN." );
		} else {
			assertTrue(logValidation, "Sharing Activity Logs on Investigate page does NOT match or NOT SHOWN." );
		}
		
		if (resNotify || resBlocked) {
			expectedMsg = "[ALERT] " + sender + " attempted to share content:test.docxwith external user(s):admin@gatewayo365beatle.com violating policy:" + policyName + "";
			data.clear();
			data.put("message", expectedMsg);
			data.put("account_type", "Internal");
			assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), "Alert Message for Sharing Activity on Investigate is NOT generated." );
		}
	}
	
	@DataProvider
	public Object[][] FileSharing_Users() {
		return new Object[][]{
			//senderGroup, receiverGroup, policyByUser, policyWithUser, policyBlock, resBlocked, resNotify
			{"", "", "testuser1@gatewayo365beatle.com", "", true, true, false},
			{"", "", "admin@gatewayo365beatle.com", "", true, false, false},
			{"GroupA", "GroupA", "admin@gatewayo365beatle.com", "", true, false, false},
			{"", "", "", "admin@gatewayo365beatle.com", true, true, false},
			{"", "", "", "testuser1@gatewayo365beatle.com", true, true, false},
			{"GroupA", "GroupA", "", "testuser1@gatewayo365beatle.com", true, false, false},
			{"", "", "testuser1@gatewayo365beatle.com", "admin@gatewayo365beatle.com", true, true, false},
			{"", "", "admin@gatewayo365beatle.com", "testuser1@gatewayo365beatle.com", true, false, false},
			{"GroupA", "GroupB", "testuser1@gatewayo365beatle.com", "admin@gatewayo365beatle.com", true, true, false},
		};
		
	}
	
	@Test(dataProvider = "FileSharing")
	public void verify_FileSharingPolicy(String[] senderGroups, String[] receiverGroups, String policyByGroup, String policyWithUser, Boolean policyBlock, Boolean resBlocked, Boolean resNotify, String tcID) throws Exception{
		Reporter.log("Validate Users and Groups based File Sharing policy tcID : " + tcID, true);
		
		String sender = "testuser1@gatewayo365beatle.com";
		String receiver = "admin@gatewayo365beatle.com";
		String policyName = "UsersGroupsPolicy";
		String logFile = "testuser1,admin,fileshare.log";
		
		addUserToGroups(sender, senderGroups);
		addUserToGroups(receiver, receiverGroups);
		
		policyDataMap.clear();
		policyDataMap.put(GatewayTestConstants.POLICY_NAME, policyName);
		policyDataMap.put(GatewayTestConstants.APPLICATIONS, "Office 365");
		policyDataMap.put(GatewayTestConstants.SHARED_BY_GROUP, policyByGroup);
		if (policyBlock) {
			policyDataMap.put(GatewayTestConstants.BLOCK_SHARE, "BLOCK_SHARE");
		} else {
			policyDataMap.put(GatewayTestConstants.BLOCK_SHARE, "NOTIFY_ONLY");
		}
		PolicyAccessEnforcement.fileSharingPolicyCreateEnable(suiteData, requestHeader, policyDataMap);
		
		replayLogs(logFile);
		
		removeUserFromGroups(sender, senderGroups);
		removeUserFromGroups(receiver, receiverGroups);
		PolicyAccessEnforcement.deletePolicy(suiteData, requestHeader, policyDataMap);
		
		String expectedMsg = "User sent email invitation(s) to admin@gatewayO365beatle.com for Test.docx.";
		data.clear();
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		boolean logValidation = validateLogsFields(fetchElasticSearchLogsUniversal(), data);
		if (resBlocked) {
			assertTrue(!logValidation, "Sharing Activity should be BLOCKED but Activity Logs on Investigate page is matched or SHOWN." );
		} else {
			assertTrue(logValidation, "Sharing Activity Logs on Investigate page does NOT match or NOT SHOWN." );
		}
		
		if (resNotify || resBlocked) {
			expectedMsg = "[ALERT] " + sender + " attempted to share content:test.docxwith external user(s):admin@gatewayo365beatle.com admin@gatewayo365beatle.com violating policy:" + policyName + "";
			data.clear();
			data.put("message", expectedMsg);
			data.put("account_type", "Internal");
			assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), "Alert Message for Sharing Activity on Investigate is NOT generated." );
		}
	}
	
	@DataProvider
	public Object[][] FileSharing() {
		return new Object[][]{
			//senderGroup, receiverGroup, policyByGroup, policyWithUser, policyBlock, resBlocked, resNotify, tcId
			{new String[]{"GroupA", "GroupB"}, new String[]{""}, "GroupA", "", true, true, false, "C02200"},
			{new String[]{""}, new String[]{"GroupA", "GroupB"}, "GroupA", "", true, false, false, "C02300"},
			{new String[]{"GroupB"}, new String[]{"GroupA", "GroupB"}, "GroupA", "", true, false, false, "C02400"},
			{new String[]{"GroupA", "GroupB"}, new String[]{""}, "", "GroupA", true, false, false, "C02500"},
			{new String[]{""}, new String[]{"GroupA", "GroupB"}, "", "GroupA", true, true, false, "C02600"},
			{new String[]{"GroupB"}, new String[]{"GroupA", "GroupB"}, "", "GroupA", true, false, false, "C02700"},
		};
		
	}

	@Test(description="UserA part of GroupA, file sharing shouldn't allowed to UserB in GroupB (Policy set to block file sharing by GroupA with GroupB). Repeat file sharing 2 times, all times its blocked. - C02900")
	public void verify_FileSharingPolicyOnGroupsTwice() throws Exception{
		Reporter.log("UserA part of GroupA, file sharing shouldn't allowed to UserB in GroupB (Policy set to block file sharing by GroupA with GroupB). Repeat file sharing 2 times, all times its blocked.", true);
		
		String sender = "testuser1@gatewayo365beatle.com";
		String receiver = "admin@gatewayo365beatle.com";
		String policyName = "UsersGroupsPolicy";
		String logFile = "testuser1,admin,fileshare.log";
		String senderGroup = "GroupA";
		String receiverGroup = "GroupB";
		
		addUserToGroup(sender, senderGroup);
		addUserToGroup(receiver, receiverGroup);
		
		policyDataMap.clear();
		policyDataMap.put(GatewayTestConstants.POLICY_NAME, policyName);
		policyDataMap.put(GatewayTestConstants.APPLICATIONS, "Office 365");
		policyDataMap.put(GatewayTestConstants.SHARED_BY_GROUP, senderGroup);
		policyDataMap.put(GatewayTestConstants.SHARE_WITH_GROUP, receiverGroup);
		policyDataMap.put(GatewayTestConstants.BLOCK_SHARE, "BLOCK_SHARE");
		
		//create policy
		PolicyAccessEnforcement.fileSharingPolicyCreateEnable(suiteData, requestHeader, policyDataMap);
		
		//replay log first time
		replayLogs(logFile);
		
		String expectedMsg = "User sent email invitation(s) to admin@gatewayO365beatle.com for Test.docx.";
		data.clear();
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		boolean logValidation = validateLogsFields(fetchElasticSearchLogsUniversal(), data);
		assertTrue(!logValidation, "Sharing Activity should be BLOCKED but Activity Logs on Investigate page is matched or SHOWN." );
		
		expectedMsg = "[ALERT] " + sender + " attempted to share content:test.docxwith external user(s):admin@gatewayo365beatle.com violating policy:" + policyName + "";
		data.clear();
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), "Alert Message for Sharing Activity on Investigate is NOT generated." );
		
		//replay log second time
		replayLogs(logFile);

		expectedMsg = "User sent email invitation(s) to admin@gatewayO365beatle.com for Test.docx.";
		data.clear();
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		logValidation = validateLogsFields(fetchElasticSearchLogsUniversal(), data);
		assertTrue(!logValidation, "Sharing Activity should be BLOCKED but Activity Logs on Investigate page is matched or SHOWN." );

		expectedMsg = "[ALERT] " + sender + " attempted to share content:test.docxwith external user(s):admin@gatewayo365beatle.com violating policy:" + policyName + "";
		data.clear();
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), "Alert Message for Sharing Activity on Investigate is NOT generated." );				
		
		//clean up user groups and delete policy.
		removeUserFromGroup(sender, senderGroup);
		removeUserFromGroup(receiver, receiverGroup);
		PolicyAccessEnforcement.deletePolicy(suiteData, requestHeader, policyDataMap);
	}
	
	@Test(description="UserA shouldn't allowed to share file with UserB (Policy set to block file sharing from UserA to UserB). Repeat file sharing 2 times, all times its blocked. - C02800")
	public void verify_FileSharingPolicyOnUsersTwice() throws Exception {
		Reporter.log("UserA shouldn't allowed to share file with UserB (Policy set to block file sharing from UserA to UserB). Repeat file sharing 2 times, all times its blocked.", true);
		
		String sender = "testuser1@gatewayo365beatle.com";
		String receiver = "admin@gatewayo365beatle.com";
		String policyName = "UsersGroupsPolicy";
		String logFile = "testuser1,admin,fileshare.log";
		
		policyDataMap.clear();
		policyDataMap.put(GatewayTestConstants.POLICY_NAME, policyName);
		policyDataMap.put(GatewayTestConstants.APPLICATIONS, "Office 365");
		policyDataMap.put(GatewayTestConstants.SHARED_BY, sender);
		policyDataMap.put(GatewayTestConstants.SHARE_WITH, receiver);
		policyDataMap.put(GatewayTestConstants.BLOCK_SHARE, "BLOCK_SHARE");

		//create policy required for test case.
		PolicyAccessEnforcement.fileSharingPolicyCreateEnable(suiteData, requestHeader, policyDataMap);
		
		//replay the log first time.
		replayLogs(logFile);
		
		String expectedMsg = "User sent email invitation(s) to admin@gatewayO365beatle.com for Test.docx.";
		data.clear();
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		boolean logValidation = validateLogsFields(fetchElasticSearchLogsUniversal(), data);
		assertTrue(!logValidation, "Sharing Activity should be BLOCKED but Activity Logs on Investigate page is matched or SHOWN." );
		
		expectedMsg = "[ALERT] " + sender + " attempted to share content:test.docxwith external user(s):admin@gatewayo365beatle.com violating policy:" + policyName + "";
		data.clear();
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), "Alert Message for Sharing Activity on Investigate is NOT generated." );
		
		//replay the logs second time.
		replayLogs(logFile);
		
		expectedMsg = "User sent email invitation(s) to admin@gatewayO365beatle.com for Test.docx.";
		data.clear();
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		logValidation = validateLogsFields(fetchElasticSearchLogsUniversal(), data);
		assertTrue(!logValidation, "Sharing Activity should be BLOCKED but Activity Logs on Investigate page is matched or SHOWN." );
		
		expectedMsg = "[ALERT] " + sender + " attempted to share content:test.docxwith external user(s):admin@gatewayo365beatle.com violating policy:" + policyName + "";
		data.clear();
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), "Alert Message for Sharing Activity on Investigate is NOT generated." );
		
		//delete created policy.
		PolicyAccessEnforcement.deletePolicy(suiteData, requestHeader, policyDataMap);
	}
	
	@Test(dataProvider = "FileSharing_GroupsUsers")
	public void verify_FileSharingPolicyOnGroupsUsers(String senderGroup, String receiverGroup, String policyByGroup, String policyByUser, String policyWithGroup, String policyWithUser, String testID) throws Exception{
		Reporter.log("Validate Users and Groups based File Sharing policy #" + testID, true);
		
		String sender = "testuser1@gatewayo365beatle.com";
		String receiver = "admin@gatewayo365beatle.com";
		String policyName = "UsersGroupsPolicy";
		String logFile = "testuser1,admin,fileshare.log";
		
		addUserToGroup(sender, senderGroup);
		addUserToGroup(receiver, receiverGroup);
		
		policyDataMap.clear();
		policyDataMap.put(GatewayTestConstants.POLICY_NAME, policyName);
		policyDataMap.put(GatewayTestConstants.APPLICATIONS, "Office 365");
		policyDataMap.put(GatewayTestConstants.SHARED_BY_GROUP, policyByGroup);
		policyDataMap.put(GatewayTestConstants.SHARE_WITH_GROUP, policyWithGroup);
		policyDataMap.put(GatewayTestConstants.SHARED_BY, policyByUser);
		policyDataMap.put(GatewayTestConstants.SHARE_WITH, policyWithUser);
		policyDataMap.put(GatewayTestConstants.BLOCK_SHARE, "BLOCK_SHARE");
		
		PolicyAccessEnforcement.fileSharingPolicyCreateEnable(suiteData, requestHeader, policyDataMap);
		
		replayLogs(logFile);
		
		removeUserFromGroup(sender, senderGroup);
		removeUserFromGroup(receiver, receiverGroup);
		PolicyAccessEnforcement.deletePolicy(suiteData, requestHeader, policyDataMap);
		
		String expectedMsg = "User sent email invitation(s) to admin@gatewayO365beatle.com for Test.docx.";
		data.clear();
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		boolean logValidation = validateLogsFields(fetchElasticSearchLogsUniversal(), data);
		assertTrue(!logValidation, "Sharing Activity should be BLOCKED but Activity Logs on Investigate page is matched or SHOWN." );
		
		expectedMsg = "[ALERT] " + sender + " attempted to share content:test.docxwith external user(s):admin@gatewayo365beatle.com violating policy:" + policyName + "";
		data.clear();
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), "Alert Message for Sharing Activity on Investigate is NOT generated." );
	}
	
	@DataProvider
	public Object[][] FileSharing_GroupsUsers() {
		return new Object[][]{
			//senderGroup, receiverGroup, policyByGroup, policyByUser, policyWithGroup, policyWithUser, testID
			{"GroupA", "", "GroupA", "admin@gatewayo365beatle.com", "", "", "C03100"},
			{"", "", "GroupA", "testuser1@gatewayo365beatle.com", "", "", "C03200"},
			{"", "GroupA", "", "", "GroupA", "testuser1@gatewayo365beatle.com", "C03300"},
			{"", "", "", "", "GroupA", "admin@gatewayo365beatle.com", "C03400"},
			{"GroupA", "GroupB", "GroupA", "admin@gatewayo365beatle.com", "GroupB", "testuser1@gatewayo365beatle.com", "C03500"},
			{"GroupA", "", "GroupA", "admin@gatewayo365beatle.com", "GroupB", "admin@gatewayo365beatle.com", "C03600"},
			{"", "GroupB", "GroupA", "admin@gatewayo365beatle.com", "GroupB", "testuser1@gatewayo365beatle.com", "C03700"},
			{"", "", "GroupA", "testuser1@gatewayo365beatle.com", "GroupB", "admin@gatewayo365beatle.com", "C03800"},
		};	
	}
	
	@Test(description="Create Policy (Policy set to Block FileSharing by GroupA with GroupB) - Violating Appropriate - Disable Policy - NO Violations - Enable Policy - Violating Appropriate. - C04000")
	public void verify_FileSharingPolicyOnGroupsDisableEnable() throws Exception{
		Reporter.log("Create Policy (Policy set to Block FileSharing by GroupA with GroupB) - Violating Appropriate - Disable Policy - NO Violations - Enable Policy - Violating Appropriate.", true);
		
		String sender = "testuser1@gatewayo365beatle.com";
		String receiver = "admin@gatewayo365beatle.com";
		String policyName = "UsersGroupsPolicy";
		String logFile = "testuser1,admin,fileshare.log";
		String senderGroup = "GroupA";
		String receiverGroup = "GroupB";
		
		addUserToGroup(sender, senderGroup);
		addUserToGroup(receiver, receiverGroup);
		
		policyDataMap.clear();
		policyDataMap.put(GatewayTestConstants.POLICY_NAME, policyName);
		policyDataMap.put(GatewayTestConstants.APPLICATIONS, "Office 365");
		policyDataMap.put(GatewayTestConstants.SHARED_BY_GROUP, senderGroup);
		policyDataMap.put(GatewayTestConstants.SHARE_WITH_GROUP, receiverGroup);
		policyDataMap.put(GatewayTestConstants.BLOCK_SHARE, "BLOCK_SHARE");
		
		//create policy
		PolicyAccessEnforcement.fileSharingPolicyCreateEnable(suiteData, requestHeader, policyDataMap);
		
		//replay log first time
		replayLogs(logFile);
		
		String expectedMsg = "User sent email invitation(s) to admin@gatewayO365beatle.com for Test.docx.";
		data.clear();
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		boolean logValidation = validateLogsFields(fetchElasticSearchLogsUniversal(), data);
		assertTrue(!logValidation, "Sharing Activity should be BLOCKED but Activity Logs on Investigate page is matched or SHOWN." );
		
		expectedMsg = "[ALERT] " + sender + " attempted to share content:test.docxwith external user(s):admin@gatewayo365beatle.com violating policy:" + policyName + "";
		data.clear();
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), "Alert Message for Sharing Activity on Investigate is NOT generated." );
		
		//replay log second time
		replayLogs(logFile);

		expectedMsg = "User sent email invitation(s) to admin@gatewayO365beatle.com for Test.docx.";
		data.clear();
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		logValidation = validateLogsFields(fetchElasticSearchLogsUniversal(), data);
		assertTrue(!logValidation, "Sharing Activity should be BLOCKED but Activity Logs on Investigate page is matched or SHOWN." );

		expectedMsg = "[ALERT] " + sender + " attempted to share content:test.docxwith external user(s):admin@gatewayo365beatle.com violating policy:" + policyName + "";
		data.clear();
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), "Alert Message for Sharing Activity on Investigate is NOT generated." );				
		
		//clean up user groups and delete policy.
		removeUserFromGroup(sender, senderGroup);
		removeUserFromGroup(receiver, receiverGroup);
		PolicyAccessEnforcement.deletePolicy(suiteData, requestHeader, policyDataMap);
	}
	
//	@Test(groups ={"SANITY", "ACCESS_ENFORCEMENT_FILE_POLICY"},dataProvider = "_policyAccessEnabledDocument")
	public void verifyUserAccessEnforcementDocumentPolicyEnabled(String policyName, String saasApps,  String testUserName,  String notifyEmailId, String objectAccess, String activityAccess,   String activityType, String objectType, String severity, String logFile) throws Exception{
		Reporter.log("Validate User Upload the file/folder to folder with Policy Enabled", true);
		testUserName=testUserName+"@"+suiteData.getTenantDomainName();
		String expectedMsg="[ALERT] "+testUserName+ " attempted Activity: "+activityAccess+" on Object type: "+objectAccess+" name: "+objectType+" using Platform";
		policyDataMap.clear();
		policyDataMap.put(GatewayTestConstants.POLICY_NAME, policyName);
		policyDataMap.put(GatewayTestConstants.APPLICATIONS, saasApps);
		policyDataMap.put(GatewayTestConstants.TARGET_USER, testUserName);
		policyDataMap.put(GatewayTestConstants.NOTIFY_EMAILID, notifyEmailId+"@"+suiteData.getTenantDomainName());
		policyDataMap.put(GatewayTestConstants.OBJECT_ACCESS, objectAccess);
		policyDataMap.put(GatewayTestConstants.ACTIVITY_ACCESS, activityAccess);
		PolicyAccessEnforcement.accessEnforcementPolicyCreateEnable(suiteData, requestHeader, policyDataMap);
		replayLogsEPDV3(logFile);
		PolicyAccessEnforcement.deletePolicy(suiteData, requestHeader, policyDataMap);
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		data.put("policy_type", "AccessEnforcement");
		data.put("policy_action", "ALERT");
		data.put("action_taken", "block,");
		data.put("_PolicyViolated", policyName);
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), "Logs does not match" ); 


	}

	@DataProvider
	public Object[][] _policyAccessEnabledDocument() {
		return new Object[][]{
				//Policy Name                	    App     Test User admin   Object Access 		Activity Access      Activity Type
				{ "ACCESS_ENFORCE_DOCUMENT2",   "Office 365",   "testuser1",  "admin",   "Document",    	"Delete",      "Policy Violation",   "gwautomation.docx", "critical",  "DocumentOneD,Delete_doc.log" },

		};
	}

}