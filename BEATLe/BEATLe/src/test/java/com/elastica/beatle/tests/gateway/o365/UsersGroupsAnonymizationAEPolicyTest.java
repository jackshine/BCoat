package com.elastica.beatle.tests.gateway.o365;


import org.apache.http.HttpResponse;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;

import static org.testng.Assert.assertTrue;
import static org.testng.Assert.assertFalse;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.Reporter;

import com.elastica.beatle.Authorization.AuthorizationHandler;
import com.elastica.beatle.gateway.CommonConfiguration;
import com.elastica.beatle.gateway.GatewayTestConstants;
import com.elastica.beatle.gateway.LogValidator;
import com.elastica.beatle.gateway.PolicyAccessEnforcement;
import com.elastica.beatle.gateway.dto.GWForensicSearchResults;
import com.elastica.beatle.protect.ProtectFunctions;


/*******************Author**************
 * 
 * @author Rocky
 */

public class UsersGroupsAnonymizationAEPolicyTest extends CommonConfiguration {

	String currentTimeInJodaFormat;
	GWForensicSearchResults fsr;
	ArrayList<String> messages = new ArrayList<String>();
	ArrayList<String> objectTypeList = new ArrayList<String>();
	ArrayList<String> objectNameList = new ArrayList<String>();
	ArrayList<String> severityList = new ArrayList<String>();
	LogValidator logValidator;
	String userLitral="User";
	String policyName="PolicyFT_FileType";
	Map<String, String>policyDataMap= new HashMap<String, String>(); 
	ProtectFunctions protectFunctions = new ProtectFunctions();
	Map <String, String> data = new HashMap<String, String>();

	@BeforeClass
	public void AddAnonymization() throws Exception {
		Reporter.log("Add Anonymization to Tenant", true);
		
		String tenantAcctId = getTenantAccountId();
		String payload = "{\"userAnonymization\":true,\"dpoName\": \"\", \"dpoPassword\": \"\",\"id\":\""+tenantAcctId+"\"}";
		Reporter.log("Payload:"+ payload, true);
		String responseBody = updateUserAnonymization(payload);
		
		//Invalidate the session after anonymization turned
		Reporter.log("Regenerate the session after anonymization turned on...:", true);
		HttpResponse CSRFHeader = AuthorizationHandler.getCSRFHeaders(suiteData.getUsername(),suiteData.getPassword(), suiteData);
		suiteData.setCSRFToken(AuthorizationHandler.getCSRFToken(CSRFHeader));
		suiteData.setSessionID(AuthorizationHandler.getUserSessionID(CSRFHeader));
		
	}
	
	@AfterClass
	public void RemoveAnonymization() throws Exception {
		Reporter.log("Remove Anonymization from Tenant", true);
		
		String tenantAcctId = getTenantAccountId();
		String payload = "{\"userAnonymization\":false,\"dpoName\":\""+ suiteData.getDpoUsername() +"\",\"dpoPassword\":\""+ suiteData.getDpoPassword() +"\", \"id\":\""+tenantAcctId+"\"}";
		Reporter.log("Payload:"+ payload, true);
		String responseBody = updateUserAnonymization(payload);
		Reporter.log("Response body:"+ responseBody, true);

	}
	
	@Test(dataProvider = "AE_DocumentEdit")
	public void verify_UserAndGroupsAE_DocumentEditPolicy(String testUser, String testUserGroup, String policyUser, String policyGroup, String objectAccess, String activityAccess, Boolean verifyBlocked, String logFile, String tcId) throws Exception{
		Reporter.log("Validate User and Groups for AE Policy with Document + Edit.", true);
		
		String policyName = "AE_O365_UsersGroups1";
		String saasApps = "Office 365";
		String severity = "critical";
		String activityType = "Policy Violation";
		
		testUser=testUser+"@"+suiteData.getTenantDomainName();
		
		addUserToGroup(testUser, testUserGroup);
		//the below is special case for c008 tests.
		if (policyUser.startsWith("admin")) {
			addUserToGroup(policyUser, testUserGroup);
		}
		
		policyDataMap.clear();
		policyDataMap.put(GatewayTestConstants.POLICY_NAME, policyName);
		policyDataMap.put(GatewayTestConstants.APPLICATIONS, saasApps);
		policyDataMap.put(GatewayTestConstants.TARGET_USER, policyUser);
		policyDataMap.put(GatewayTestConstants.TARGET_GROUP, policyGroup);
		policyDataMap.put(GatewayTestConstants.OBJECT_ACCESS, objectAccess);
		policyDataMap.put(GatewayTestConstants.ACTIVITY_ACCESS, activityAccess);
		PolicyAccessEnforcement.accessEnforcementPolicyCreateEnable(suiteData, requestHeader, policyDataMap);
		
		replayLogsEPDV3(logFile);
		
		removeUserFromGroup(testUser, testUserGroup);
		//the below is special case for c008 tests.
		if (policyUser.startsWith("admin")) {
			addUserToGroup(policyUser, testUserGroup);
		}
		PolicyAccessEnforcement.deletePolicy(suiteData, requestHeader, policyDataMap);
		
		String expectedMsg="[ALERT] "+testUser+ " attempted Activity: "+activityAccess+" on Object type: "+objectAccess+"  using Platform";
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		data.put("policy_type", "AccessEnforcement");
		data.put("policy_action", "ALERT");
		data.put("action_taken", "block,");
		data.put("_PolicyViolated", policyName);
		
		boolean validationResult = validateLogsFields(fetchElasticSearchLogsUniversal(activityType, severity), data);
		if (verifyBlocked) {
			assertTrue(validationResult, "Investigate log for Blocking " + objectAccess + " + " + activityAccess + " is NOT available or Activity is NOT Blocked!!");
		} else {
			assertFalse(validationResult, "Investigate log for Blocking " + objectAccess + " + " + activityAccess + " is available or Activity is Blocked!!");
		}
	}
	
	@DataProvider
	public Object[][] AE_DocumentEdit() {
		return new Object[][]{
			//Test User, Test User Group, objectAccess, activityAccess, verifyBlocked, tcId
			{"testuser1", "GroupA", "", "GroupA", "Document", "Edit", true, "WordDocument,Edit_IN_Word_Online.log", "C00100"},
			{"testuser1", "GroupA", "", "GroupA", "Document", "Edit", false, "OneDrive,Create_folder.log", "C00200"},
			{"testuser1", "", "", "GroupA", "Document", "Edit", false, "WordDocument,Edit_IN_Word_Online.log", "C00300"},
			{"testuser1", "GroupB", "", "GroupA", "Document", "Edit", false, "WordDocument,Edit_IN_Word_Online.log", "C00400"},
			{"testuser1", "", "testuser1@gatewayo365beatle.com", "", "Document", "Edit", true, "WordDocument,Edit_IN_Word_Online.log", "C00500"},
			{"testuser1", "", "testuser1@gatewayo365beatle.com", "", "Document", "Edit", false, "OneDrive,Create_folder.log", "C00600"},
			{"testuser1", "GroupA", "testuser1@gatewayo365beatle.com", "", "Document", "Edit", true, "WordDocument,Edit_IN_Word_Online.log", "C00700"},
			{"testuser1", "GroupA", "admin@gatewayo365beatle.com", "", "Document", "Edit", false, "WordDocument,Edit_IN_Word_Online.log", "C00800"},
			{"testuser1", "GroupA", "admin@gatewayo365beatle.com", "GroupA", "Document", "Edit", true, "WordDocument,Edit_IN_Word_Online.log", "C00900"},
			{"testuser1", "", "testuser1@gatewayo365beatle.com", "GroupA", "Document", "Edit", true, "WordDocument,Edit_IN_Word_Online.log", "C01100"},
			
			{"testuser1", "GroupA", "", "GroupA", "Recycle Bin", "Empty", true, "WordDocument,Edit_IN_Word_Online.log", "C00100"},
			{"testuser1", "GroupA", "", "GroupA", "Recycle Bin", "Empty", false, "OneDrive,Create_folder.log", "C00200"},
			{"testuser1", "", "", "GroupA", "Recycle Bin", "Empty", false, "WordDocument,Edit_IN_Word_Online.log", "C00300"},
			{"testuser1", "GroupB", "", "GroupA", "Recycle Bin", "Empty", false, "WordDocument,Edit_IN_Word_Online.log", "C00400"},
			{"testuser1", "", "testuser1@gatewayo365beatle.com", "", "Recycle Bin", "Empty", true, "WordDocument,Edit_IN_Word_Online.log", "C00500"},
			{"testuser1", "", "testuser1@gatewayo365beatle.com", "", "Recycle Bin", "Empty", false, "OneDrive,Create_folder.log", "C00600"},
			{"testuser1", "GroupA", "testuser1@gatewayo365beatle.com", "", "Recycle Bin", "Empty", true, "WordDocument,Edit_IN_Word_Online.log", "C00700"},
			{"testuser1", "GroupA", "admin@gatewayo365beatle.com", "", "Recycle Bin", "Empty", false, "WordDocument,Edit_IN_Word_Online.log", "C00800"},
			{"testuser1", "GroupA", "admin@gatewayo365beatle.com", "GroupA", "Recycle Bin", "Empty", true, "WordDocument,Edit_IN_Word_Online.log", "C00900"},
			{"testuser1", "", "testuser1@gatewayo365beatle.com", "GroupA", "Recycle Bin", "Empty", true, "WordDocument,Edit_IN_Word_Online.log", "C01100"},
		};
	}
	
	@Test(dataProvider = "AE_FolderOperations")
	public void verify_UserAndGroupsAE_FolderOperationsPolicy(String testUser, String testUserGroup, String policyUser, String policyGroup, String objectAccess, String activityAccess, String fileName, Boolean verifyBlocked, String logFile, String tcId) throws Exception{
		Reporter.log("Validate User and Groups for AE Policy with Document + Edit.", true);
		
		String policyName = "AE_O365_UsersGroups2";
		String saasApps = "Office 365";
		String severity = "critical";
		String activityType = "Policy Violation";
		
		testUser=testUser+"@"+suiteData.getTenantDomainName();
		
		addUserToGroup(testUser, testUserGroup);
		//the below is special case for c008 tests.
		if (policyUser.startsWith("admin")) {
			addUserToGroup(policyUser, testUserGroup);
		}
		
		policyDataMap.clear();
		policyDataMap.put(GatewayTestConstants.POLICY_NAME, policyName);
		policyDataMap.put(GatewayTestConstants.APPLICATIONS, saasApps);
		policyDataMap.put(GatewayTestConstants.TARGET_USER, policyUser);
		policyDataMap.put(GatewayTestConstants.TARGET_GROUP, policyGroup);
		policyDataMap.put(GatewayTestConstants.OBJECT_ACCESS, objectAccess);
		policyDataMap.put(GatewayTestConstants.ACTIVITY_ACCESS, activityAccess);
		PolicyAccessEnforcement.accessEnforcementPolicyCreateEnable(suiteData, requestHeader, policyDataMap);
		
		replayLogsEPDV3(logFile);
		
		removeUserFromGroup(testUser, testUserGroup);
		//the below is special case for c008 tests.
		if (policyUser.startsWith("admin")) {
			addUserToGroup(policyUser, testUserGroup);
		}
		PolicyAccessEnforcement.deletePolicy(suiteData, requestHeader, policyDataMap);
		
		String expectedMsg="[ALERT] "+testUser+ " attempted Activity: "+activityAccess+" on Object type: "+objectAccess+" name: "+fileName+" using Platform";
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		data.put("policy_type", "AccessEnforcement");
		data.put("policy_action", "ALERT");
		data.put("action_taken", "block,");
		data.put("_PolicyViolated", policyName);
		
		boolean validationResult = validateLogsFields(fetchElasticSearchLogsUniversal(activityType, severity), data);
		if (verifyBlocked) {
			assertTrue(validationResult, "Investigate log for Blocking " + objectAccess + " + " + activityAccess + " is NOT available or Activity is NOT Blocked!!");
		} else {
			assertFalse(validationResult, "Investigate log for Blocking " + objectAccess + " + " + activityAccess + " is available or Activity is Blocked!!");
		}
	}
	
	@DataProvider
	public Object[][] AE_FolderOperations() {
		return new Object[][]{
			//Test User, Test User Group, verifyBlocked, tcId
			{"testuser1", "GroupA", "", "GroupA", "Folder", "Create", "gwtest1", true, "OneDrive,Create_folder.log", "C00100"},
			{"testuser1", "GroupA", "", "GroupA", "Folder", "Create", "gwtest1", false, "WordDocument,Edit_IN_Word_Online.log", "C00200"},
			{"testuser1", "", "", "GroupA", "Folder", "Create", "gwtest1", false, "OneDrive,Create_folder.log", "C00300"},
			{"testuser1", "GroupB", "", "GroupA", "Folder", "Create", "gwtest1", false, "OneDrive,Create_folder.log", "C00400"},
			{"testuser1", "", "testuser1@gatewayo365beatle.com", "", "Folder", "Create", "gwtest1", true, "OneDrive,Create_folder.log", "C00500"},
			{"testuser1", "", "testuser1@gatewayo365beatle.com", "", "Folder", "Create", "gwtest1", false, "WordDocument,Edit_IN_Word_Online.log", "C00600"},
			{"testuser1", "GroupA", "testuser1@gatewayo365beatle.com", "", "Folder", "Create", "gwtest1", true, "OneDrive,Create_folder.log", "C00700"},
			{"testuser1", "GroupA", "admin@gatewayo365beatle.com", "", "Folder", "Create", "gwtest1", false, "OneDrive,Create_folder.log", "C00800"},
			{"testuser1", "GroupA", "admin@gatewayo365beatle.com", "GroupA", "Folder", "Create", "gwtest1", true, "OneDrive,Create_folder.log", "C00900"},
			{"testuser1", "", "testuser1@gatewayo365beatle.com", "GroupA", "Folder", "Create", "gwtest1", true, "OneDrive,Create_folder.log", "C01100"},
			
			{"testuser1", "GroupA", "", "GroupA", "Folder/OneNote notebook", "Delete", "", true, "OneDrive,Delete_folder.log", "C00100"},
			{"testuser1", "GroupA", "", "GroupA", "Folder/OneNote notebook", "Delete", "", false, "WordDocument,Edit_IN_Word_Online.log", "C00200"},
			{"testuser1", "", "", "GroupA", "Folder/OneNote notebook", "Delete", "", false, "OneDrive,Delete_folder.log", "C00300"},
			{"testuser1", "GroupB", "", "GroupA", "Folder/OneNote notebook", "Delete", "", false, "OneDrive,Delete_folder.log", "C00400"},
			{"testuser1", "", "testuser1@gatewayo365beatle.com", "", "Folder/OneNote notebook", "Delete", "", true, "OneDrive,Delete_folder.log", "C00500"},
			{"testuser1", "", "testuser1@gatewayo365beatle.com", "", "Folder/OneNote notebook", "Delete", "", false, "WordDocument,Edit_IN_Word_Online.log", "C00600"},
			{"testuser1", "GroupA", "testuser1@gatewayo365beatle.com", "", "Folder/OneNote notebook", "Delete", "", true, "OneDrive,Delete_folder.log", "C00700"},
			{"testuser1", "GroupA", "admin@gatewayo365beatle.com", "", "Folder/OneNote notebook", "Delete", "", false, "OneDrive,Delete_folder.log", "C00800"},
			{"testuser1", "GroupA", "admin@gatewayo365beatle.com", "GroupA", "Folder/OneNote notebook", "Delete", "", true, "OneDrive,Delete_folder.log", "C00900"},
			{"testuser1", "", "testuser1@gatewayo365beatle.com", "GroupA", "Folder/OneNote notebook", "Delete", "", true, "OneDrive,Delete_folder.log", "C01100"},
			
			{"testuser1", "GroupA", "", "GroupA", "Document/Folder", "Copy", "u3", true, "OneDrive,Copy_folder.log", "C00100"},
			{"testuser1", "GroupA", "", "GroupA", "Document/Folder", "Copy", "u3", false, "WordDocument,Edit_IN_Word_Online.log", "C00200"},
			{"testuser1", "", "", "GroupA", "Document/Folder", "Copy", "u3", false, "OneDrive,Copy_folder.log", "C00300"},
			{"testuser1", "GroupB", "", "GroupA", "Document/Folder", "Copy", "u3", false, "OneDrive,Copy_folder.log", "C00400"},
			{"testuser1", "", "testuser1@gatewayo365beatle.com", "", "Document/Folder", "Copy", "u3", true, "OneDrive,Copy_folder.log", "C00500"},
			{"testuser1", "", "testuser1@gatewayo365beatle.com", "", "Document/Folder", "Copy", "u3", false, "WordDocument,Edit_IN_Word_Online.log", "C00600"},
			{"testuser1", "GroupA", "testuser1@gatewayo365beatle.com", "", "Document/Folder", "Copy", "u3", true, "OneDrive,Copy_folder.log", "C00700"},
			{"testuser1", "GroupA", "admin@gatewayo365beatle.com", "", "Document/Folder", "Copy", "u3", false, "OneDrive,Copy_folder.log", "C00800"},
			{"testuser1", "GroupA", "admin@gatewayo365beatle.com", "GroupA", "Document/Folder", "Copy", "u3", true, "OneDrive,Copy_folder.log", "C00900"},
			{"testuser1", "", "testuser1@gatewayo365beatle.com", "GroupA", "Document/Folder", "Copy", "u3", true, "OneDrive,Copy_folder.log", "C01100"},
			
			{"testuser1", "GroupA", "", "GroupA", "Document/Folder", "Move", "agwfolder", true, "OneDrive,Move_folder.log", "C00100"},
			{"testuser1", "GroupA", "", "GroupA", "Document/Folder", "Move", "agwfolder", false, "WordDocument,Edit_IN_Word_Online.log", "C00200"},
			{"testuser1", "", "", "GroupA", "Document/Folder", "Move", "agwfolder", false, "OneDrive,Move_folder.log", "C00300"},
			{"testuser1", "GroupB", "", "GroupA", "Document/Folder", "Move", "agwfolder", false, "OneDrive,Move_folder.log", "C00400"},
			{"testuser1", "", "testuser1@gatewayo365beatle.com", "", "Document/Folder", "Move", "agwfolder", true, "OneDrive,Move_folder.log", "C00500"},
			{"testuser1", "", "testuser1@gatewayo365beatle.com", "", "Document/Folder", "Move", "agwfolder", false, "WordDocument,Edit_IN_Word_Online.log", "C00600"},
			{"testuser1", "GroupA", "testuser1@gatewayo365beatle.com", "", "Document/Folder", "Move", "agwfolder", true, "OneDrive,Move_folder.log", "C00700"},
			{"testuser1", "GroupA", "admin@gatewayo365beatle.com", "", "Document/Folder", "Move", "agwfolder", false, "OneDrive,Move_folder.log", "C00800"},
			{"testuser1", "GroupA", "admin@gatewayo365beatle.com", "GroupA", "Document/Folder", "Move", "agwfolder", true, "OneDrive,Move_folder.log", "C00900"},
			{"testuser1", "", "testuser1@gatewayo365beatle.com", "GroupA", "Document/Folder", "Move", "agwfolder", true, "OneDrive,Move_folder.log", "C01100"},
			
			{"testuser1", "GroupA", "", "GroupA", "Document/Folder", "Rename", "agwfolder", true, "OneDrive,Rename_folder.log", "C00100"},
			{"testuser1", "GroupA", "", "GroupA", "Document/Folder", "Rename", "agwfolder", false, "WordDocument,Edit_IN_Word_Online.log", "C00200"},
			{"testuser1", "", "", "GroupA", "Document/Folder", "Rename", "agwfolder", false, "OneDrive,Rename_folder.log", "C00300"},
			{"testuser1", "GroupB", "", "GroupA", "Document/Folder", "Rename", "agwfolder", false, "OneDrive,Rename_folder.log", "C00400"},
			{"testuser1", "", "testuser1@gatewayo365beatle.com", "", "Document/Folder", "Rename", "agwfolder", true, "OneDrive,Rename_folder.log", "C00500"},
			{"testuser1", "", "testuser1@gatewayo365beatle.com", "", "Document/Folder", "Rename", "agwfolder", false, "WordDocument,Edit_IN_Word_Online.log", "C00600"},
			{"testuser1", "GroupA", "testuser1@gatewayo365beatle.com", "", "Document/Folder", "Rename", "agwfolder", true, "OneDrive,Rename_folder.log", "C00700"},
			{"testuser1", "GroupA", "admin@gatewayo365beatle.com", "", "Document/Folder", "Rename", "agwfolder", false, "OneDrive,Rename_folder.log", "C00800"},
			{"testuser1", "GroupA", "admin@gatewayo365beatle.com", "GroupA", "Document/Folder", "Rename", "agwfolder", true, "OneDrive,Rename_folder.log", "C00900"},
			{"testuser1", "", "testuser1@gatewayo365beatle.com", "GroupA", "Document/Folder", "Rename", "agwfolder", true, "OneDrive,Rename_folder.log", "C01100"},
			
			{"testuser1", "GroupA", "", "GroupA", "Document", "Delete", "gwautomation.docx", true, "DocumentOneD,Delete_doc.log", "C00100"},
			{"testuser1", "GroupA", "", "GroupA", "Document", "Delete", "gwautomation.docx", false, "WordDocument,Edit_IN_Word_Online.log", "C00200"},
			{"testuser1", "", "", "GroupA", "Document", "Delete", "gwautomation.docx", false, "DocumentOneD,Delete_doc.log", "C00300"},
			{"testuser1", "GroupB", "", "GroupA", "Document", "Delete", "gwautomation.docx", false, "DocumentOneD,Delete_doc.log", "C00400"},
			{"testuser1", "", "testuser1@gatewayo365beatle.com", "", "Document", "Delete", "gwautomation.docx", true, "DocumentOneD,Delete_doc.log", "C00500"},
			{"testuser1", "", "testuser1@gatewayo365beatle.com", "", "Document", "Delete", "gwautomation.docx", false, "WordDocument,Edit_IN_Word_Online.log", "C00600"},
			{"testuser1", "GroupA", "testuser1@gatewayo365beatle.com", "", "Document", "Delete", "gwautomation.docx", true, "DocumentOneD,Delete_doc.log", "C00700"},
			{"testuser1", "GroupA", "admin@gatewayo365beatle.com", "", "Document", "Delete", "gwautomation.docx", false, "DocumentOneD,Delete_doc.log", "C00800"},
			{"testuser1", "GroupA", "admin@gatewayo365beatle.com", "GroupA", "Document", "Delete", "gwautomation.docx", true, "DocumentOneD,Delete_doc.log", "C00900"},
			{"testuser1", "", "testuser1@gatewayo365beatle.com", "GroupA", "Document", "Delete", "gwautomation.docx", true, "DocumentOneD,Delete_doc.log", "C01100"},
			
			{"testuser1", "GroupA", "", "GroupA", "Document", "Download", "gwautomation.docx", true, "DocumentOneD,Download_doc.log", "C00100"},
			{"testuser1", "GroupA", "", "GroupA", "Document", "Download", "gwautomation.docx", false, "WordDocument,Edit_IN_Word_Online.log", "C00200"},
			{"testuser1", "", "", "GroupA", "Document", "Download", "gwautomation.docx", false, "DocumentOneD,Download_doc.log", "C00300"},
			{"testuser1", "GroupB", "", "GroupA", "Document", "Download", "gwautomation.docx", false, "DocumentOneD,Download_doc.log", "C00400"},
			{"testuser1", "", "testuser1@gatewayo365beatle.com", "", "Document", "Download", "gwautomation.docx", true, "DocumentOneD,Download_doc.log", "C00500"},
			{"testuser1", "", "testuser1@gatewayo365beatle.com", "", "Document", "Download", "gwautomation.docx", false, "WordDocument,Edit_IN_Word_Online.log", "C00600"},
			{"testuser1", "GroupA", "testuser1@gatewayo365beatle.com", "", "Document", "Download", "gwautomation.docx", true, "DocumentOneD,Download_doc.log", "C00700"},
			{"testuser1", "GroupA", "admin@gatewayo365beatle.com", "", "Document", "Download", "gwautomation.docx", false, "DocumentOneD,Download_doc.log", "C00800"},
			{"testuser1", "GroupA", "admin@gatewayo365beatle.com", "GroupA", "Document", "Download", "gwautomation.docx", true, "DocumentOneD,Download_doc.log", "C00900"},
			{"testuser1", "", "testuser1@gatewayo365beatle.com", "GroupA", "Document", "Download", "gwautomation.docx", true, "DocumentOneD,Download_doc.log", "C01100"},
			
			{"testuser1", "GroupA", "", "GroupA", "Document", "Upload", "gwautomation.docx", true, "DocumentOneD,Upload_docx.log", "C00100"},
			{"testuser1", "GroupA", "", "GroupA", "Document", "Upload", "gwautomation.docx", false, "WordDocument,Edit_IN_Word_Online.log", "C00200"},
			{"testuser1", "", "", "GroupA", "Document", "Upload", "gwautomation.docx", false, "DocumentOneD,Upload_docx.log", "C00300"},
			{"testuser1", "GroupB", "", "GroupA", "Document", "Upload", "gwautomation.docx", false, "DocumentOneD,Upload_docx.log", "C00400"},
			{"testuser1", "", "testuser1@gatewayo365beatle.com", "", "Document", "Upload", "gwautomation.docx", true, "DocumentOneD,Upload_docx.log", "C00500"},
			{"testuser1", "", "testuser1@gatewayo365beatle.com", "", "Document", "Upload", "gwautomation.docx", false, "WordDocument,Edit_IN_Word_Online.log", "C00600"},
			{"testuser1", "GroupA", "testuser1@gatewayo365beatle.com", "", "Document", "Upload", "gwautomation.docx", true, "DocumentOneD,Upload_docx.log", "C00700"},
			{"testuser1", "GroupA", "admin@gatewayo365beatle.com", "", "Document", "Upload", "gwautomation.docx", false, "DocumentOneD,Upload_docx.log", "C00800"},
			{"testuser1", "GroupA", "admin@gatewayo365beatle.com", "GroupA", "Document", "Upload", "gwautomation.docx", true, "DocumentOneD,Upload_docx.log", "C00900"},
			{"testuser1", "", "testuser1@gatewayo365beatle.com", "GroupA", "Document", "Upload", "gwautomation.docx", true, "DocumentOneD,Upload_docx.log", "C01100"},
		};
	}
}
