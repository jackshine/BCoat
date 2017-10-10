package com.elastica.tests.google;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.io.File;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.entity.StringEntity;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.elastica.common.GWCommonTest;
import com.elastica.gateway.GWCommonUtils;
import com.elastica.gateway.GatewayTestConstants;
import com.elastica.listeners.Priority;
import com.elastica.logger.Logger;
import com.elastica.restClient.Client;
import com.elastica.restClient.ClientUtil;
import com.elastica.action.infra.InfraUtils;

public class GDriveTestsRegression extends GWCommonTest{
	Map <String, Object> expectedDataMap = new HashMap<String, Object>();
	String fromTime=backend.getCurrentTime();
	String title;
	List<NameValuePair> headers;
	String blockUser = "Block user";
	String userId = null;
	protected Client restClient=new Client();
	InfraUtils Infrautils= new InfraUtils();

	@Priority(1)
	@Test(groups ={"Regression", "GW"})
	public void gDriveOperations() throws Exception {
		fromTime=backend.getCurrentTime();
		printCredentials();
		Logger.info("Starting performing activities on GDrive saas app");
		login.loginCloudSocPortal(getWebDriver(), suiteData);
		gda.login(getWebDriver(), suiteData);
		Logger.info("Finished performing activities on GDrive saas app");
	}
	
	@Priority(2)
	@Test(groups ={"REACH"})
	public void gDriveOperationsLogin() throws Exception {
		printCredentials();
		Logger.info("Login for Reach");
		gda.login(getWebDriver(), suiteData);
		Logger.info("Finished performing activities on GDrive saas app");
		
	}
	
	@Priority(2)
//	@Test(groups ={"Regression"})
	public void gDrive_Test_ValidateSplunkLog() throws Exception {
		assertTrue(backend.validateSplunkLog(suiteData), "Host is not found");
	}
	

	
	@Priority(3)
	@Test(groups ={"Regression", "REACH"})
	public void gDrive_Test_CreateDoc() throws Exception {
		Logger.info("Create Doc");
		printCredentials();
		gda.homepage(getWebDriver(), suiteData);
		gda.deleteFile(getWebDriver(), GatewayTestConstants.GDRIVE_CREATE_DOC);
		gda.createDocFile(getWebDriver(), GatewayTestConstants.GDRIVE_CREATE_DOC);
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACCOUNT_TYPE, suiteData.getAccountType());
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Create");
		expectedDataMap.put(GatewayTestConstants.IS_ANONYMOUS_PROXY, "false");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User Created new Document in root"); 
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Document"); 
		expectedDataMap.put(GatewayTestConstants.RESP_CODE, "302");
		expectedDataMap.put(GatewayTestConstants.REFERER_URI, "");
		
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("Completed Create Doc");
		gda.homepage(getWebDriver(), suiteData);
	}
	
	@Priority(4)
	@Test(groups ={"Regression", "REACH"})
	public void gDrive_Test_Copy_File() throws Exception {
		Logger.info("Copy File Permanantly");
		printCredentials();
		gda.selectFile(getWebDriver(), GatewayTestConstants.GDRIVE_CREATE_DOC);
		gda.copyFile(getWebDriver(), GatewayTestConstants.GDRIVE_CREATE_DOC);
		gda.deleteFile(getWebDriver(), GatewayTestConstants.GDRIVE_CREATE_DOC_COPY);
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACCOUNT_TYPE, suiteData.getAccountType());
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Copy");
		expectedDataMap.put(GatewayTestConstants.IS_ANONYMOUS_PROXY, "false");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User made a copy of item "+ GatewayTestConstants.GDRIVE_CREATE_DOC +" named " + GatewayTestConstants.GDRIVE_CREATE_DOC_COPY + " with parent root");
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File/Folder");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("Copy File Permanantly");
	}
	
	@Priority(5)
	@Test(groups ={"Regression", "REACH"})
	public void gDrive_Test_View_File() throws Exception {
		Logger.info("Viewed Doc");
		printCredentials();
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACCOUNT_TYPE, suiteData.getAccountType());
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "View");
		expectedDataMap.put(GatewayTestConstants.IS_ANONYMOUS_PROXY, "false");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User Viewed document Untitled document"); 
		expectedDataMap.put(GatewayTestConstants.RESP_CODE, "200");
		expectedDataMap.put(GatewayTestConstants.REFERER_URI, "");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("Completed Viewed Doc");
	}

	@Priority(6)
	@Test(groups ={"Regression", "REACH"})
	public void gDrive_Test_Delete_File_Permantly() throws Exception {
		Logger.info("Deleted File Permanantly");
		printCredentials();
		gda.homepage(getWebDriver(), suiteData);
		gda.deleteFile(getWebDriver(), GatewayTestConstants.GDRIVE_CREATE_DOC);
		gda.deleteForever(getWebDriver(), GatewayTestConstants.GDRIVE_CREATE_DOC);
		gda.deleteForever(getWebDriver(), GatewayTestConstants.GDRIVE_CREATE_DOC_COPY);
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACCOUNT_TYPE, suiteData.getAccountType());
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Delete Forever");
		expectedDataMap.put(GatewayTestConstants.IS_ANONYMOUS_PROXY, "false");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User deleted item(s) "+ GatewayTestConstants.GDRIVE_CREATE_DOC + " permanantly"); 
		expectedDataMap.put(GatewayTestConstants.RESP_CODE, "200");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("Completed Deleted File Permanantly");
	}
	
	@Priority(7)
	@Test(groups ={"Regression", "REACH"})
	public void gDrive_Test_CreateFolder() throws Exception {
		Logger.info("Create Folder");
		printCredentials();
		gda.homepage(getWebDriver(), suiteData);
		gda.deleteFile(getWebDriver(), GatewayTestConstants.GDRIVE_CREATE_FOLDER);
		gda.createFolder(getWebDriver(), GatewayTestConstants.GDRIVE_CREATE_FOLDER);
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACCOUNT_TYPE, suiteData.getAccountType());
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Create");
		expectedDataMap.put(GatewayTestConstants.IS_ANONYMOUS_PROXY, "false");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User created folder named " + GatewayTestConstants.GDRIVE_CREATE_FOLDER +" in folder root");
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Folder");
		expectedDataMap.put(GatewayTestConstants.RESP_CODE, "200");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("Completed Create Folder");
	}
	
	@Priority(8)
	@Test(groups ={"Regression", "REACH"})
	public void gDrive_Test_Link_Share_TopMenu() throws Exception {
		Logger.info("Create Folder");
		printCredentials();
		gda.homepage(getWebDriver(), suiteData);
		gda.shareByLinkByTopMenu(getWebDriver(), GatewayTestConstants.GDRIVE_CREATE_FOLDER);
		Logger.info("==================================================================================");
		Logger.info("Link Sharing");
		Logger.info("==================================================================================");
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User turned on link sharing for file/folder " + GatewayTestConstants.GDRIVE_CREATE_FOLDER);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Share");
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File/Folder");
		expectedDataMap.put(GatewayTestConstants.REQ_URI, "https://clients6.google.com/batch/drive");
		expectedDataMap.put(GatewayTestConstants.SHARE__WITH, "ALL_EL__");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("==================================================================================");
		Logger.info("Completed Link Sharing");
		Logger.info("==================================================================================");
	}
	
	@Priority(8)
	@Test(groups ={"Regression", "REACH"})
	public void gDrive_Test_Move_Folder() throws Exception {
		Logger.info("Moved Folder");
		printCredentials();
		gda.move(getWebDriver(), GatewayTestConstants.GDRIVE_CREATE_FOLDER, GatewayTestConstants.MOVE_TO_FOLDER);
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACCOUNT_TYPE, suiteData.getAccountType());
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Move");
		expectedDataMap.put(GatewayTestConstants.IS_ANONYMOUS_PROXY, "false");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User moved item " + GatewayTestConstants.GDRIVE_CREATE_FOLDER +
				" from root to folder " + GatewayTestConstants.MOVE_TO_FOLDER); 
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File/Folder");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("Completed Moved Folder");
	}
	
	@Priority(9)
	@Test(groups ={"Regression", "REACH"})
	public void gDrive_Test_Folder_Browse() throws Exception {
		Logger.info("Browsed Folder");
		printCredentials();
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACCOUNT_TYPE, suiteData.getAccountType());
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "View");
		expectedDataMap.put(GatewayTestConstants.IS_ANONYMOUS_PROXY, "false");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User browsed folder named "+ GatewayTestConstants.MOVE_TO_FOLDER); 
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Folder");
		expectedDataMap.put(GatewayTestConstants.RESP_CODE, "200");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("Completed Browsed Folder");
	}
	
	@Priority(10)
	@Test(groups ={"Regression", "REACH"})
	public void gDrive_Test_Share_Folders_Permission() throws Exception {
		Logger.info("Share Folder");
		printCredentials();
		gda.homepage(getWebDriver(), suiteData);
		gda.rightClickAndshareByEmailBothEditAndView(getWebDriver(), suiteData.getUsername(), GatewayTestConstants.GDRIVE_CREATE_FOLDER);
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Share");
		expectedDataMap.put(GatewayTestConstants.FILE_TYPE_GENERIC, "");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "&lt;" + suiteData.getUsername() +"&gt; as collaborators on item "+ GatewayTestConstants.GDRIVE_CREATE_FOLDER );
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File/Folder");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("Completed Share Folder");
	}
	
	//@Priority(11)
	//@Test(groups ={"Regression"})
	public void gDrive_Test_Permission_Folder() throws Exception {
		Logger.info("Changed to View Permission");
		printCredentials();
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Share");
		expectedDataMap.put(GatewayTestConstants.FILE_TYPE_GENERIC, "");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "&lt;" + suiteData.getUsername() +"&gt; as collaborators on item "+ GatewayTestConstants.GDRIVE_CREATE_FOLDER );
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File/Folder");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("Completed Changed to View Permission");
	}
	
	@Priority(12)
	@Test(groups ={"Regression", "REACH"})
	public void gDrive_Test_Collaborators_Folder() throws Exception {
		Logger.info("Collaborator Folder");
		printCredentials();
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACCOUNT_TYPE, suiteData.getAccountType());
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Share");
		expectedDataMap.put(GatewayTestConstants.IS_ANONYMOUS_PROXY, "false");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User added QA Admin &lt;" + suiteData.getUsername() + "&gt; as collaborators on item "+ GatewayTestConstants.GDRIVE_CREATE_FOLDER);
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File/Folder");
		expectedDataMap.put(GatewayTestConstants.FILE_TYPE_GENERIC, "");
		
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("Completed Collaborator Folder");
	}
	
	@Priority(13)
	@Test(groups ={"Regression", "REACH"})
	public void gDrive_Test_UnShare_Folder() throws Exception {
		Logger.info("Unshare Folder");
		printCredentials();
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACCOUNT_TYPE, suiteData.getAccountType());
		expectedDataMap.put(GatewayTestConstants.IS_ANONYMOUS_PROXY, "false");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User turned off company wide sharing for item " + GatewayTestConstants.GDRIVE_CREATE_FOLDER);
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File/Folder");
		expectedDataMap.put(GatewayTestConstants.RESP_CODE, "200");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("Completed Unshare Folder");
	}
	
	@Priority(14)
	@Test(groups ={"Regression", "REACH"})
	public void gDrive_Test_Rename_Folder() throws Exception {
		Logger.info("Rename Folder");
		printCredentials();
		gda.homepage(getWebDriver(), suiteData);
		gda.selectFile(getWebDriver(), GatewayTestConstants.GDRIVE_CREATE_FOLDER);
		gda.renameFile(getWebDriver(), GatewayTestConstants.GDRIVE_CREATE_FOLDER, GatewayTestConstants.GDRIVE_RENAME_FOLDER);
		gda.selectFile(getWebDriver(), GatewayTestConstants.GDRIVE_RENAME_FOLDER);
		gda.renameFile(getWebDriver(), GatewayTestConstants.GDRIVE_RENAME_FOLDER, GatewayTestConstants.GDRIVE_CREATE_FOLDER);
		gda.selectFile(getWebDriver(), GatewayTestConstants.GDRIVE_CREATE_FOLDER);
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACCOUNT_TYPE, suiteData.getAccountType());
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Rename");
		expectedDataMap.put(GatewayTestConstants.IS_ANONYMOUS_PROXY, "false");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User renamed an item "+ GatewayTestConstants.GDRIVE_RENAME_FOLDER);
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File/Folder");
		expectedDataMap.put(GatewayTestConstants.RESP_CODE, "200");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("Completed Rename Folder");
	}
	
	@Priority(15)
	@Test(groups ={"Regression", "REACH"})
	public void gDrive_Test_Delete_Folder() throws Exception {
		Logger.info("Delete Folder");
		printCredentials();
		gda.homepage(getWebDriver(), suiteData);
		gda.deleteFile(getWebDriver(), GatewayTestConstants.GDRIVE_CREATE_FOLDER);
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACCOUNT_TYPE, suiteData.getAccountType());
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Remove");
		expectedDataMap.put(GatewayTestConstants.IS_ANONYMOUS_PROXY, "false");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User deleted item(s) " + GatewayTestConstants.GDRIVE_CREATE_FOLDER);
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File/Folder");
		expectedDataMap.put(GatewayTestConstants.RESP_CODE, "200");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("Completed Delete Folder");
	}
	
	@Priority(16)
	@Test(groups ={"Regression", "REACH"})
	public void gDrive_Test_Restore_Folder() throws Exception {
		Logger.info("Restore Folder");
		printCredentials();
		gda.restoreFile(getWebDriver(), GatewayTestConstants.GDRIVE_CREATE_FOLDER);
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACCOUNT_TYPE, suiteData.getAccountType());
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Restore");
		expectedDataMap.put(GatewayTestConstants.IS_ANONYMOUS_PROXY, "false");
		expectedDataMap.put(GatewayTestConstants.MESSAGE,  "User restored folder/file named " + GatewayTestConstants.GDRIVE_CREATE_FOLDER);
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File/Folder");
		expectedDataMap.put(GatewayTestConstants.RESP_CODE, "200");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("Completed Restore Folder");
	}
	
	@Priority(17)
	@Test(groups ={"Regression", "REACH"})
	public void gDrive_Test_Delete_Folder_Forever() throws Exception {
		Logger.info("Delete Forever");
		printCredentials();
		gda.homepage(getWebDriver(), suiteData);
		gda.deleteFile(getWebDriver(), GatewayTestConstants.GDRIVE_CREATE_FOLDER);
		gda.deleteForever(getWebDriver(), GatewayTestConstants.GDRIVE_CREATE_FOLDER);
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACCOUNT_TYPE, suiteData.getAccountType());
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Delete Forever");
		expectedDataMap.put(GatewayTestConstants.IS_ANONYMOUS_PROXY, "false");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User deleted item(s) "+ GatewayTestConstants.GDRIVE_CREATE_FOLDER + " permanantly"); 
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File/Folder");
		expectedDataMap.put(GatewayTestConstants.RESP_CODE, "200");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("Completed Delete Forever");
	}

	@Priority(18)
	@Test(groups ={"Regression", "REACH"}) //dependsOnMethods = { "gDriveOperations", "gDriveOperationsLogin" })
	public void gDrive_Test_Move_File() throws Exception {
		Logger.info("Move File ");
		printCredentials();
		gda.homepage(getWebDriver(), suiteData);
		gda.hardWait(20);
		gda.move(getWebDriver(), GatewayTestConstants.MOVE_FILE, GatewayTestConstants.MOVE_TO_FOLDER);
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACCOUNT_TYPE, suiteData.getAccountType());
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Move");
		expectedDataMap.put(GatewayTestConstants.IS_ANONYMOUS_PROXY, "false");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User moved item " + GatewayTestConstants.MOVE_FILE +" from root to folder " + GatewayTestConstants.MOVE_TO_FOLDER); 
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File/Folder");
		expectedDataMap.put(GatewayTestConstants.RESP_CODE, "200");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("Completed Move File ");
	}

	@Priority(19)
	@Test(groups ={"Regression", "REACH"}) //dependsOnMethods = { "gDriveOperations", "gDriveOperationsLogin" })
	public void gDrive_Test_Invalid_Login() throws Exception {
		Logger.info("Invalid Login");
		printCredentials();
		gda.logout(getWebDriver());
		String password = suiteData.getSaasAppPassword();
		suiteData.setSaasAppPassword("incorrect");
		gda.relogin(getWebDriver(), suiteData);
		suiteData.setSaasAppPassword(password);
		assertTrue(gda.relogin(getWebDriver(), suiteData));
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.remove(GatewayTestConstants.FACILITY);
		expectedDataMap.put(GatewayTestConstants.ACCOUNT_TYPE, suiteData.getAccountType());
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "InvalidLogin");
		expectedDataMap.put(GatewayTestConstants.IS_ANONYMOUS_PROXY, "false");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User Login Failed!");
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Session");
		expectedDataMap.put(GatewayTestConstants.RESP_CODE, "200");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		
		Logger.info("Completed Invalid Login");
	}
	
	@Priority(20)
	@Test(groups ={"Regression", "REACH"}) //dependsOnMethods = { "gDriveOperations", "gDriveOperationsLogin" })
	public void gDrive_Test_ValidateLoging() throws Exception {
		Logger.info("Login");
		printCredentials();
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.remove(GatewayTestConstants.FACILITY);
		expectedDataMap.put(GatewayTestConstants.ACCOUNT_TYPE,suiteData.getAccountType());
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Login");
		expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		expectedDataMap.put(GatewayTestConstants.ELASTICA_USER, suiteData.getTestUsername());
		expectedDataMap.put(GatewayTestConstants.IS_ANONYMOUS_PROXY, "false");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User logged in"); 
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Session");
		expectedDataMap.put(GatewayTestConstants.RESP_CODE, "302");
		expectedDataMap.put(GatewayTestConstants.REQ_URI, GatewayTestConstants.GDRIVE_REQ_URI_LOGIN);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.put(GatewayTestConstants.USER,  suiteData.getTestUsername());
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("Login Event Verification Successfull");
		Logger.info("Completed Login");
	}

	@Priority(21)
	@Test(groups ={"Regression", "REACH"}) //dependsOnMethods = { "gDriveOperations", "gDriveOperationsLogin" })
	public void gDrive_Test_DeleteFile() throws Exception {
		Logger.info(" Delete File");
		printCredentials();
		fromTime=backend.getCurrentTime();
		gda.homepage(getWebDriver(), suiteData);
		gda.selectFolder(getWebDriver(), GatewayTestConstants.GDRIVE_REGRESSION_FOLDER);
		gda.deleteFile(getWebDriver(), GatewayTestConstants.GDRIVE_DELETE_FILE);
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACCOUNT_TYPE,suiteData.getAccountType());
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Remove");
		expectedDataMap.put(GatewayTestConstants.IS_ANONYMOUS_PROXY, "false");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User deleted item(s) " + GatewayTestConstants.GDRIVE_DELETE_FILE);
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File/Folder");
		expectedDataMap.put(GatewayTestConstants.RESP_CODE, "200");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("Completed Delete File");
	}
	
	@Priority(22)
	@Test(groups ={"Regression", "REACH"}) //dependsOnMethods = { "gDriveOperations", "gDriveOperationsLogin" })
	public void gDrive_Test_Restore_File() throws Exception {
		Logger.info("Restore File");
		printCredentials();
		gda.restoreFile(getWebDriver(), GatewayTestConstants.GDRIVE_DELETE_FILE);
		gda.selectFolder(getWebDriver(), GatewayTestConstants.GDRIVE_REGRESSION_FOLDER);
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACCOUNT_TYPE,suiteData.getAccountType());
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Restore");
		expectedDataMap.put(GatewayTestConstants.IS_ANONYMOUS_PROXY, "false");
		expectedDataMap.put(GatewayTestConstants.MESSAGE,  "User restored folder/file named " + GatewayTestConstants.GDRIVE_DELETE_FILE);
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File/Folder");
		expectedDataMap.put(GatewayTestConstants.RESP_CODE, "200");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("Completed Restore File");
	}
	
	@Priority(23)
	@Test(groups ={"Regression", "REACH"}) //dependsOnMethods = { "gDriveOperations", "gDriveOperationsLogin" }) 
	public void gDrive_Test_Rename_File() throws Exception {
		Logger.info("Rename File");
		printCredentials();
		gda.homepage(getWebDriver(), suiteData);
		gda.selectFolder(getWebDriver(), GatewayTestConstants.GDRIVE_REGRESSION_FOLDER);
		gda.selectFile(getWebDriver(), GatewayTestConstants.GDRIVE_FILE);
		gda.renameFile(getWebDriver(), GatewayTestConstants.GDRIVE_FILE, GatewayTestConstants.GDRIVE_FILE2);
		gda.selectFile(getWebDriver(), GatewayTestConstants.GDRIVE_FILE2);
		gda.renameFile(getWebDriver(), GatewayTestConstants.GDRIVE_FILE2, GatewayTestConstants.GDRIVE_FILE);
		gda.selectFile(getWebDriver(), GatewayTestConstants.GDRIVE_FILE);
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACCOUNT_TYPE,suiteData.getAccountType());
		expectedDataMap.put(GatewayTestConstants.IS_ANONYMOUS_PROXY, "false");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User renamed an item "+ GatewayTestConstants.GDRIVE_FILE2);
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File/Folder");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("Completed Rename File");
	}

	@Priority(24)
	@Test(groups ={"Regression", "REACH"}) //dependsOnMethods = { "gDriveOperations", "gDriveOperationsLogin" })
	public void gDrive_Test_Download_Normal() throws Exception {
		Logger.info("Download Normal File");
		printCredentials();
		gda.homepage(getWebDriver(), suiteData);
		gda.selectFolder(getWebDriver(), GatewayTestConstants.GDRIVE_REGRESSION_FOLDER);
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		gda.download(getWebDriver(), GatewayTestConstants.GDRIVE_UPLOAD_ORIGINAL_FILE);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Download");
		expectedDataMap.put(GatewayTestConstants.ACCOUNT_TYPE, suiteData.getAccountType());
		expectedDataMap.put(GatewayTestConstants.IS_ANONYMOUS_PROXY, "false");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User downloaded contents of file " + GatewayTestConstants.GDRIVE_UPLOAD_ORIGINAL_FILE + ".");
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File");
		expectedDataMap.put(GatewayTestConstants.RESP_CODE, "200");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("Completed Download Normal File");
	}
	
	@Priority(25)
	@Test(groups ={"Regression", "REACH"}) //dependsOnMethods = { "gDriveOperations", "gDriveOperationsLogin" })
	public void gDrive_Test_Download_Preview_Normal() throws Exception {
		Logger.info("Preview Normal File");
		printCredentials();
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "View");
		expectedDataMap.put(GatewayTestConstants.ACCOUNT_TYPE, suiteData.getAccountType());
		expectedDataMap.put(GatewayTestConstants.IS_ANONYMOUS_PROXY, "false");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User previewed contents of file "+ GatewayTestConstants.GDRIVE_UPLOAD_ORIGINAL_FILE); 
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File");
		expectedDataMap.put(GatewayTestConstants.RESP_CODE, "200");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("Completed Preview Normal File");
	}

	@Priority(26)
	@Test(groups ={"Regression", "REACH"}) //dependsOnMethods = { "gDriveOperations", "gDriveOperationsLogin" })
	public void gDrive_Test_Download_Decryption() throws Exception {
		Logger.info("Download Decryption");
		printCredentials();
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		fromTime=backend.getCurrentTime();
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME + suiteData.getSaasAppName(), suiteData, backend.getGWHeaders(suiteData));
		policy.createEnableEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME + suiteData.getSaasAppName(), suiteData, backend.getGWHeaders(suiteData));
		gda.homepage(getWebDriver(), suiteData);
		gda.selectFolder(getWebDriver(), GatewayTestConstants.GDRIVE_REGRESSION_FOLDER);
		gda.download(getWebDriver(), GatewayTestConstants.GDRIVE_DOWNLOAD_DECRYPTED_FILE);
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME + suiteData.getSaasAppName(), suiteData, backend.getGWHeaders(suiteData));

		Logger.info("Download Decrypted File");
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.remove(GatewayTestConstants.COUNTRY);
		expectedDataMap.remove(GatewayTestConstants.USER_NAME);
		expectedDataMap.put(GatewayTestConstants.ACTION_TAKEN, "decrypt");
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "File Decryption");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "File " + 
				GatewayTestConstants.GDRIVE_DOWNLOAD_DECRYPTED_FILE +" decrypted on download for user " + suiteData.getSaasAppUsername() +".");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("Completed Download Decrypted File");
		
	}
	
	@Priority(27)
	@Test(groups ={"Regression", "REACH"}) //dependsOnMethods = { "gDriveOperations", "gDriveOperationsLogin" })
	public void gDrive_Test_File_Decryption_Alert_Msg() throws Exception {
		Logger.info("Alert Decryption");
		printCredentials();
		fromTime=backend.getCurrentTime();
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.remove(GatewayTestConstants.COUNTRY);
		expectedDataMap.remove(GatewayTestConstants.USER_NAME);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Policy Violation");
		expectedDataMap.put(GatewayTestConstants.ACCOUNT_TYPE, suiteData.getAccountType());
		expectedDataMap.put(GatewayTestConstants.MESSAGE,  "[ALERT] " +suiteData.getSaasAppUsername() +" attempted to download content:" + 
				GatewayTestConstants.GDRIVE_DOWNLOAD_DECRYPTED_FILE +" violating policy:" + GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME + suiteData.getSaasAppName());
		expectedDataMap.put(GatewayTestConstants.POLICY_VIOLATED, GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME + suiteData.getSaasAppName());
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "critical");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("Completed Alert Decryption");

		
	}
	
	@Priority(29)
	@Test(groups ={"Regression", "REACH"}) //dependsOnMethods = { "gDriveOperations", "gDriveOperationsLogin" })
	public void gDrive__Test_Share_Via_Email_Permission() throws Exception {
		Logger.info("File Collaborators");
		printCredentials();
		fromTime=backend.getCurrentTime();
		gda.homepage(getWebDriver(), suiteData);
		gda.selectFolder(getWebDriver(), GatewayTestConstants.GDRIVE_REGRESSION_FOLDER);

		gda.rightClickAndshareByEmailBothEditAndView(getWebDriver(), suiteData.getUsername(), GatewayTestConstants.GDRIVE_FILE);
		Logger.info("File Share ");
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Share");
		expectedDataMap.put(GatewayTestConstants.FILE_TYPE_GENERIC, "");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "&lt;" + suiteData.getUsername() +"&gt; as collaborators on item "+ GatewayTestConstants.GDRIVE_FILE );
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("Completed File Share ");
	}
	
	@Priority(30)
	@Test(groups ={"Regression", "REACH"}) //dependsOnMethods = { "gDriveOperations", "gDriveOperationsLogin" }) 
	public void gDrive_Test_Unshare_File() throws Exception {
		Logger.info("File UnShare ");
		printCredentials();
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACCOUNT_TYPE, suiteData.getAccountType());
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Unshare");
		expectedDataMap.put(GatewayTestConstants.IS_ANONYMOUS_PROXY, "false");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User turned off company wide sharing for item " + GatewayTestConstants.GDRIVE_FILE);
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("Completed File UnShare ");
	}
	
	@Priority(31)
	//@Test(groups ={"Regression"}, dependsOnMethods = { "gDriveOperations" })  
	public void gDrive_Test_Permission_File() throws Exception {
		Logger.info("Public Share ");
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Share");
		expectedDataMap.put(GatewayTestConstants.FILE_TYPE_GENERIC, "");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "&lt;" + suiteData.getUsername() +"&gt; as collaborators on item "+ GatewayTestConstants.GDRIVE_FILE );
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("Completed Public Share ");
	}
	
	@Priority(31)
	@Test(groups ={"Regression", "REACH"}) //dependsOnMethods = { "gDriveOperations", "gDriveOperationsLogin" }) 
	public void gDrive_Test_Download_Folder() throws Exception {
		Logger.info("Download Folder");
		printCredentials();
		gda.homepage(getWebDriver(), suiteData);
		gda.hardWait(20);
		gda.downloadFolder(getWebDriver(), "download_folder");
		gda.homepage(getWebDriver(), suiteData);
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACCOUNT_TYPE, suiteData.getAccountType());
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Download");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User downloaded contents of folder as filename");
		expectedDataMap.put(GatewayTestConstants.IS_ANONYMOUS_PROXY, "false");
		//expectedDataMap.put(GatewayTestConstants.RESP_CODE, "200");
		expectedDataMap.put(GatewayTestConstants.FILE_SIZE, "4"); //Cheking the decimal digit of size
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("Completed Download Folder");
	} 
	
	@Priority(32)
	//@Test(groups ={"Regression"})
	public void gDrive_Test_Upload_File_Normal() throws Exception {
		fromTime=backend.getCurrentTime();
		printCredentials();
		Logger.info("==================================================================================");
		Logger.info("Upload File");
		Logger.info("==================================================================================");
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME+ suiteData.getSaasAppName(), suiteData, backend.getHeaders(suiteData));
		gda.homepage(getWebDriver(), suiteData);
		gda.selectFolder(getWebDriver(), GatewayTestConstants.GDRIVE_REGRESSION_FOLDER);
		gda.deleteFile(getWebDriver(), GatewayTestConstants.GDRIVE_ORGINAL_FILE);
		gda.uploadFile1(getWebDriver(), System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+
				"resources"+File.separator+"meta"+File.separator + GatewayTestConstants.GDRIVE_ORGINAL_FILE);
		expectedDataMap.clear(); 
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Upload");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User uploaded file " + GatewayTestConstants.GDRIVE_ORGINAL_FILE); 
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "File");
		expectedDataMap.put(GatewayTestConstants.FILE_SIZE, GWCommonUtils.getFileSize(GatewayTestConstants.GDRIVE_ORGINAL_FILE));
		expectedDataMap.put(GatewayTestConstants.OBJECT_NAME, GatewayTestConstants.GDRIVE_ORGINAL_FILE);
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("==================================================================================");
		Logger.info("Upload File Successful");
		Logger.info("==================================================================================");
	}
	
	@Priority(33)
	//@Test(groups ={"Regression"})
	public void gDrive_Test_Upload_And_Encrypt() throws Exception {
		Logger.info("==================================================================================");
		Logger.info("Upload File And Encrypt ");
		Logger.info("==================================================================================");
		printCredentials();
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME + suiteData.getSaasAppName(), suiteData, backend.getGWHeaders(suiteData));
		policy.createEnableEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME + suiteData.getSaasAppName(), suiteData, backend.getGWHeaders(suiteData));

		gda.homepage(getWebDriver(), suiteData);
		gda.deleteFile(getWebDriver(), GatewayTestConstants.GDRIVE_ORGINAL_FILE_UPLOAD_ENCRPT + ".eef");
		
		gda.uploadFile(getWebDriver(), System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+
				"resources"+File.separator+"meta"+File.separator + GatewayTestConstants.GDRIVE_ORGINAL_FILE_UPLOAD_ENCRPT);
		expectedDataMap.clear(); 
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "File Encryption");
		expectedDataMap.put(GatewayTestConstants.ACTION_TAKEN, "encrypt");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, 
				"File " + GatewayTestConstants.GDRIVE_ORGINAL_FILE_UPLOAD_ENCRPT + " encrypted on upload for user " + suiteData.getSaasAppUsername()); 
		expectedDataMap.put(GatewayTestConstants.POLICY_TYPE, "FileTransfer");
		expectedDataMap.put(GatewayTestConstants.FILE_SIZE, "78");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		expectedDataMap.remove(GatewayTestConstants.COUNTRY);
		expectedDataMap.remove(GatewayTestConstants.USER_NAME);
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME + suiteData.getSaasAppName(), suiteData, backend.getGWHeaders(suiteData));
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		
		gda.homepage(getWebDriver(), suiteData);
		if (!gda.isExist(getWebDriver(), GatewayTestConstants.GDRIVE_ORGINAL_FILE_UPLOAD_ENCRPT +".eef")) {
			Assert.assertTrue(false, "File " + GatewayTestConstants.GDRIVE_ORGINAL_FILE_UPLOAD_ENCRPT + ".eef not found in "+ suiteData.getSaasAppName() + ".");
		}
		Logger.info("==================================================================================");
		Logger.info("Upload File And EncryptSuccessful");
		Logger.info("==================================================================================");
	}
	
	@Priority(34)
	@Test(groups ={"Regression", "REACH"})
	public void gDrive_Test_Logout() throws Exception {
		Logger.info("Logout");
		printCredentials();
		fromTime=backend.getCurrentTime();
		gda.logout(getWebDriver());
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.remove(GatewayTestConstants.FACILITY);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Logout");
		expectedDataMap.put(GatewayTestConstants.ACCOUNT_TYPE, suiteData.getAccountType());
		expectedDataMap.put(GatewayTestConstants.IS_ANONYMOUS_PROXY, "false");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User logged out");
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Session");
		expectedDataMap.put(GatewayTestConstants.REQ_URI, GatewayTestConstants.GDRIVE_REQ_URI_LOGOUT);
		//expectedDataMap.put(GatewayTestConstants.RESP_CODE, "302");
		expectedDataMap.put(GatewayTestConstants.REFERER_URI, "");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "informational");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("Completed Logout");
	}
	
	@Priority(1)
	@Test(groups ={ "GW"})
	public void CreateAndDeleteUser() throws Exception {
		headers = backend.getHeaders(suiteData);
		String userEmail = "testuser3@gatewaybeatle.com";
		//pod: AuF*x9fnxv(05fL
		String payload="{\"user\":{\"first_name\":\"GWTest2\",\"last_name\":\"Name\",\"email\":\"" + userEmail + "\",\"secondary_user_id\":\"\",\"password\":\"AuF*x9fnxv(05fL\",\"title\":\"\",\"work_phone\":\"\",\"cell_phone\":\"\",\"access_profiles\":[],\"is_admin\":false,\"is_active\":true,\"notes\":\"\",\"is_dpo\":false}}";
		Logger.info("****CreateDeleteUser() *****  payload.." + payload);
		HttpResponse response=infra.SearchUser(suiteData, headers,  userEmail);
		String responseBody = ClientUtil.getResponseBody(response);
		
		HttpResponse responseCreatUser=null;
		userId=infra.getUserid(responseBody, userEmail);
		if(null==userId||(userId.isEmpty())) {
			responseCreatUser = infra.createUser(suiteData, headers, payload);
			String responseBody1 = ClientUtil.getResponseBody(responseCreatUser);
			System.out.println("Response: "+responseBody1);
			String action_status =(String) new JSONObject(responseBody1).get("action_status").toString();
			assertEquals(action_status,"success", "user Creation expectedDataMap  verification failed");
		}
		
		response=infra.SearchUser(suiteData, headers,  userEmail);
		userId = null;
		userId = infra.searchUserid(suiteData, headers, userEmail);
		Logger.info("The User Created is :" + userId);
		policy.deactivateEncryptionDecryptionPolicy(blockUser, suiteData, backend.getGWHeaders(suiteData));
		login.loginCloudSocPortal(getWebDriver(), suiteData);
		suiteData.setSaasAppUsername(userEmail);
		suiteData.setSaasAppPassword("gatewaybeatle");
		gda.login(getWebDriver(), suiteData);
		gda.logout(getWebDriver());
		// delete user
		policy.activateEncryptionDecryptionPolicy(blockUser, suiteData, backend.getGWHeaders(suiteData));
	}
	
	
	@Priority(2)
	@Test(groups ={ "GW"}, timeOut = 25000)
	public void verifyUserBlockDeleteUser() throws Exception {
		gda.relogin(getWebDriver(), suiteData);
	}
	
	@Priority(3)
	@Test(groups ={ "GW"})
	public void deleteUserAndVerifyLogin() throws Exception {
		policy.deactivateEncryptionDecryptionPolicy(blockUser, suiteData, backend.getGWHeaders(suiteData));
		Logger.info(" delete user with id"+userId);
		HttpResponse responseDeleteUser =infra.deleteUser(suiteData, headers, userId);
		assertEquals( responseDeleteUser.getStatusLine().getStatusCode(),HttpStatus.SC_NO_CONTENT, "Response code verification failed");
		Logger.info("user is deleted");
		Logger.info("Actual result :"+responseDeleteUser.getStatusLine().getStatusCode());
		Logger.info("Expected result :"+HttpStatus.SC_NO_CONTENT);
		Logger.info("Testcase execution Completed ");
		gda.relogin(getWebDriver(), suiteData);
	}
	
	@Priority(4)
	@Test( groups ={ "GW"}, dataProvider = "groupData" )
	public void CreateGroup(String description,String action,String id,String payload,String type,String name) throws Exception {
		headers = backend.getHeaders(suiteData);
		Logger.info("exeuting testcase ****CreateGroup() ***** Description"+description);
		Logger.info("group Action"+action+" testcase type"+type);
		// search before creation
		HttpResponse responseSearch=infra.SearchGroup(suiteData, headers, name);
		String SearchResponseBody = ClientUtil.getResponseBody(responseSearch);
		String groupid=infra.getGroupid(SearchResponseBody, name);
		System.out.println("Payload " + payload);
		if(groupid.isEmpty()) {
			// create group request
			HttpResponse response=infra.createGroup(suiteData, headers, payload);
			String responseBody = ClientUtil.getResponseBody(response);
			//Assert the Response expectedDataMap
			String action_status =(String) new JSONObject(responseBody).get("action_status").toString();
			assertEquals(action_status,"success", "group expectedDataMap verification failed");
			Logger.info("Create group  Response: "+action_status);
			// Search the Added group
			if(type.contains("normal")) {
				HttpResponse searchResponse=infra.SearchGroup(suiteData, headers, name);
				String searchResponseBody = ClientUtil.getResponseBody(searchResponse);
				//Assert the Response expectedDataMap
				assertEquals(searchResponse.getStatusLine().getStatusCode(),HttpStatus.SC_OK, "Response code verification failed");
				//Logger.info("** addGroup** Response: ",searchResponse.getStatusLine().getStatusCode());
				String Searchname=infra.getGroupName(searchResponseBody, name);
				assertEquals(name,Searchname, "Name searched failed");
				Logger.info("Expected status : "+Searchname);
				Logger.info("Actual status :"+name);
				Logger.info("Testcase execution Completed ");
			}
		}
		else
		{
			Logger.info("Group already present: ");
		}
	}

	/*Author: Vijay Gangwar
	*todo: add enduser
	*params none
	*/
	@Priority(5)
	@Test (  groups ={ "GW"}, dataProvider = "UsertoGrp"  )
	public void AddUsertoGroup(String email,String id,String payload,String group) throws Exception {
		Logger.info("exeuting testcase ****AddDpoUser() ***** Assembl id:C2094 ...");
		Logger.info("Description: Create the DPO user Enable request for Elastica  App  :*** ");
		Logger.info(" Steps : 1 search if user already exist. 2.  Add  DPO user.3. search added user ");			
		// search user			
		// check if user is alrady present.
		headers = backend.getHeaders(suiteData);
		restClient = new Client();
		String userId =infra.searchUserid(suiteData, headers, email);
		// add end user if not present
		if(null!=userId)
		{

			///search group and
			HttpResponse	 response2=infra.SearchGroup(suiteData, headers, group);
			String responseBody2 = ClientUtil.getResponseBody(response2);
			//JSONArray summaryObject =  new JSONArray(ClientUtil.ClientUtil.getResponseBody(response1)).ge.getJSONArray("objects");
			System.out.println("e ****responseBody ***** ..."+responseBody2);
			Logger.info("search user responseBody: ."+responseBody2);
			String GroupId=Infrautils.getUserid(responseBody2,"groupslist","name",group);
			
			// check if user already present in group 
			
			// search user 
			String crossSearchUri =suiteData.getReferer()+"/admin/group/users?group_name="+group;
				Logger.info("search user uri ."+crossSearchUri);
				//String payload=InfraConstants.ASSING_USER_PL;
				URI crossSearchdataUri = ClientUtil.BuidURI(crossSearchUri);
				HttpResponse crossSearchresponse =  restClient.doGet(crossSearchdataUri, headers);//doPost(SearchdataUri, headers, null, new StringEntity(payload));
				Thread.sleep(3000);
				String crossSearchResponseBody = ClientUtil.getResponseBody(crossSearchresponse);
				String SearchedId="";
				SearchedId=Infrautils.SearchUserInGrp(crossSearchResponseBody,"tenantgroupusers","user_email",email,"user_email");
			if((!SearchedId.contains(email)))
			{
				String searchUri =suiteData.getReferer()+"/admin/user/assign";
				Logger.info("search user uri ."+searchUri);
				
				URI SearchdataUri = ClientUtil.BuidURI(searchUri);
				HttpResponse Searchresponse =  restClient.doPost(SearchdataUri, headers, null, new StringEntity(payload));
				String SearchResponseBody = ClientUtil.getResponseBody(Searchresponse);
				Logger.info("responseBody: "+SearchResponseBody);
				String action_status =(String) new JSONObject(SearchResponseBody).get("action_status").toString();
				assertEquals(action_status,"success", "Response code verification failed");
				// cross check for user asignment
				 searchUri =suiteData.getReferer()+"/admin/group/users?group_name="+group;
				Logger.info("search user uri ."+searchUri);
				//String payload=InfraConstants.ASSING_USER_PL;
				 SearchdataUri = ClientUtil.BuidURI(searchUri);
				HttpResponse Searchresponse1 =  restClient.doGet(SearchdataUri, headers);//doPost(SearchdataUri, headers, null, new StringEntity(payload));
				Thread.sleep(3000);
				String SearchResponseBody1 = ClientUtil.getResponseBody(Searchresponse1);
				// search user in group
				String user=Infrautils.SearchUserInGrp(SearchResponseBody1,"tenantgroupusers","user_email",email,"group_name");
				assertEquals(user,group, "Group id do not match verification failed");
				Logger.info(" Expected Result: Added user:"+group);
				Logger.info(" Actual Result: added user found:"+user);
			}
			else
			{
				Logger.info(" user already present in the group found:");
			}
				
			//  add user to group
			Logger.info(" Test execution is completed");
		}
		else
		{
			Logger.info("user not not present: ");
			Logger.info(" Test execution is completed");
		}
	}
	
	@Priority(6)
	@Test(groups ={ "GW"}, dependsOnMethods = { "gDriveOperations" })
	public void createPolicyAndDownload() throws Exception {
		gda.homepage(getWebDriver(), suiteData);
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME, suiteData, backend.getHeaders(suiteData));
		policy.createEnableEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME, suiteData, backend.getGWHeaders(suiteData));
		gda.selectFolder(getWebDriver(), GatewayTestConstants.GDRIVE_REGRESSION_FOLDER);
		gda.download(getWebDriver(), GatewayTestConstants.GDRIVE_ENCRYPTED_FILE_RENAME1);
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Policy Violation");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "critical");
		expectedDataMap.put(GatewayTestConstants.MESSAGE,  "[ALERT] " + suiteData.getSaasAppUsername() +" attempted to download content:" + 
				GatewayTestConstants.GDRIVE_ENCRYPTED_FILE_RENAME1 +" violating policy:" + GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME);
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	@Priority(7)
	@Test(groups ={"GW"}, dependsOnMethods = { "gDriveOperations" })
	public void updatePolicyByDeactivateAndDownload() throws Exception {
		// Rename file name and verify log is not present for Rename file after de-active policy 
		gda.selectFile(getWebDriver(), GatewayTestConstants.GDRIVE_ENCRYPTED_FILE_RENAME1);
		policy.deactivateEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME, suiteData, backend.getGWHeaders(suiteData));
		gda.renameFile(getWebDriver(), GatewayTestConstants.GDRIVE_ENCRYPTED_FILE_RENAME1, GatewayTestConstants.GDRIVE_ENCRYPTED_FILE_RENAME2);
		gda.download(getWebDriver(), GatewayTestConstants.GDRIVE_ENCRYPTED_FILE_RENAME2);
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Policy Violation");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "critical");
		expectedDataMap.put(GatewayTestConstants.MESSAGE,  "[ALERT] " + suiteData.getSaasAppUsername() +" attempted to download content:" + 
				GatewayTestConstants.GDRIVE_ENCRYPTED_FILE_RENAME2 +" violating policy:" + GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME);
		assertFalse(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	@Priority(8)
	@Test(groups ={"GW"}, dependsOnMethods = { "gDriveOperations" })
	public void updatePolicyByActivateAndDownload() throws Exception {
		policy.activateEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME, suiteData, backend.getGWHeaders(suiteData));
		gda.download(getWebDriver(), GatewayTestConstants.GDRIVE_ENCRYPTED_FILE_RENAME2);
		gda.selectFile(getWebDriver(), GatewayTestConstants.GDRIVE_ENCRYPTED_FILE_RENAME2);
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Policy Violation");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "critical");
		expectedDataMap.put(GatewayTestConstants.MESSAGE,  "[ALERT] " + suiteData.getSaasAppUsername() +" attempted to download content:" + 
				GatewayTestConstants.GDRIVE_ENCRYPTED_FILE_RENAME2 +" violating policy:" + GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME);
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
	}
	
	@Priority(9)
	@Test(groups ={"GW"}, dependsOnMethods = { "gDriveOperations" })
	public void deletePolicy() throws Exception {
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME, suiteData, backend.getHeaders(suiteData));
		gda.renameFile(getWebDriver(), GatewayTestConstants.GDRIVE_ENCRYPTED_FILE_RENAME2, GatewayTestConstants.GDRIVE_ENCRYPTED_FILE_RENAME3);
		gda.download(getWebDriver(), GatewayTestConstants.GDRIVE_ENCRYPTED_FILE_RENAME3);
		gda.selectFile(getWebDriver(), GatewayTestConstants.GDRIVE_ENCRYPTED_FILE_RENAME3);
		expectedDataMap.clear();
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Policy Violation");
		expectedDataMap.put(GatewayTestConstants.SEVERITY, "critical");
		expectedDataMap.put(GatewayTestConstants.MESSAGE,  "[ALERT] " + suiteData.getSaasAppUsername() +" attempted to download content:" + 
				GatewayTestConstants.GDRIVE_ENCRYPTED_FILE_RENAME3 +" violating policy:" + GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME);
		//backend.assertAndValidateLogNotPresent(client, suiteData, fromTime, expectedDataMap);
		assertFalse(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		gda.renameFile(getWebDriver(), GatewayTestConstants.GDRIVE_ENCRYPTED_FILE_RENAME3, GatewayTestConstants.GDRIVE_ENCRYPTED_FILE_RENAME1);
	}
	
	
	
	@DataProvider
	public static Object[][] groupData() {
		return new Object[][]{
			// type    		id	   		value         
			{ "Add Group", "add", "C2129", "{\"group\":{\"name\":\"Gateway\",\"description\":\"test group\",\"is_active\":true,\"notes\":\"\"}}","normal","Gateway"},
			};
	}
	
	@DataProvider
	public static Object [][] UsertoGrp()
	{
		return new Object[][]{
			//user type    	email	id	   		payload      Group   																																																						casetype
			{ "testuser3@gatewaybeatle.com",  "C18886", "{\"email\":\"testuser3@gatewaybeatle.com\",\"deleted_groups\":[],\"added_groups\":[\"Gateway\"]}","Gateway"}
			
		};
	}
	
	@BeforeClass(groups ={"Regression", "REACH"})
	public void doBeforeClass() throws Exception {
		Logger.info("Delete Policy Before Test ");
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME + suiteData.getSaasAppName(), 
				suiteData, backend.getGWHeaders(suiteData));
	}
	
	@AfterClass(groups ={"Regression", "REACH"})
	public void doAfterClass() throws Exception {
		Logger.info("Delete Policy After Test ");
		policy.deleteEncryptionDecryptionPolicy(GatewayTestConstants.ENCRY_DECRYPT_POLICY_NAME + suiteData.getSaasAppName(), 
				suiteData, backend.getGWHeaders(suiteData));
	}
	
}