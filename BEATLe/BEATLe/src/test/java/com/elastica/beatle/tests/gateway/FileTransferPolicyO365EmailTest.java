package com.elastica.beatle.tests.gateway;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;

import static org.testng.Assert.assertTrue;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.Reporter;
import com.elastica.beatle.gateway.CommonConfiguration;
import com.elastica.beatle.gateway.GatewayTestConstants;
import com.elastica.beatle.gateway.LogValidator;
import com.elastica.beatle.gateway.PolicyAccessEnforcement;
import com.elastica.beatle.gateway.dto.GWForensicSearchResults;
import com.elastica.beatle.protect.ProtectFunctions;


/*******************Author**************
 * 
 * @author Afjal
 */



public class FileTransferPolicyO365EmailTest extends CommonConfiguration {

	String currentTimeInJodaFormat;
	GWForensicSearchResults fsr;
	ArrayList<String> messages = new ArrayList<String>();
	ArrayList<String> objectTypeList = new ArrayList<String>();
	ArrayList<String> objectNameList = new ArrayList<String>();
	ArrayList<String> severityList = new ArrayList<String>();
	LogValidator logValidator;
	String userLitral="User";
	String fileName="Test.pdf";
	String policyName="PolicyFT_FileType";
	Map<String, String>policyDataMap= new HashMap<String, String>(); 
	ProtectFunctions protectFunctions = new ProtectFunctions();
	Map <String, String> data = new HashMap<String, String>();
	@BeforeMethod(alwaysRun=true)
	public void beforMethod(Method method) throws Exception {
	deleteAllPolicies();
	Reporter.log("--------------------------------------------", true);
	Reporter.log("Deleting all policies", true);
	Reporter.log("--------------------------------------------", true);
	}
	
	
	
	@Test(groups ={"TEST"}, dataProvider = "_emailPolicyFileTransferupload")
	public void verifyEmailFileTransferPolicyEnabledUpload(String policyName, String saasApps,  String testUserName,  String notifyEmailId, String transferType, String fileType, String fileName,  String activityType, String objectType, String severity, String logFile) throws Exception{
		Reporter.log("Validate User Upload the file/folder to folder with Policy Enabled", true);
		testUserName=testUserName+"@"+suiteData.getTenantDomainName();
		String expectedMsg="[ALERT] "+testUserName+ " attempted to "+transferType+" content:"+fileName.toLowerCase()+"."+fileType+" violating policy:"+policyName;
		System.out.println("expectedMsg: "+expectedMsg);
		policyDataMap.clear();
		policyDataMap.put(GatewayTestConstants.POLICY_NAME, policyName);
		policyDataMap.put(GatewayTestConstants.APPLICATIONS, saasApps);
		policyDataMap.put(GatewayTestConstants.TARGET_USER, testUserName);
		policyDataMap.put(GatewayTestConstants.NOTIFY_EMAILID, notifyEmailId+"@"+suiteData.getTenantDomainName());
		policyDataMap.put(GatewayTestConstants.TRANSFER_TYPE, transferType);
		policyDataMap.put(GatewayTestConstants.FILE_TYPE, fileType);
		policyDataMap.put(GatewayTestConstants.FILE_NAME, fileName);
		PolicyAccessEnforcement.fileTransferPolicyCreateEnable(suiteData, requestHeader, policyDataMap);
		replayLogs(logFile);
		PolicyAccessEnforcement.deletePolicy(suiteData, requestHeader, policyDataMap);
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		data.put("policy_type", "FileTransfer");
		data.put("policy_action", "ALERT");
		data.put("action_taken", "block,email,");
		data.put("_PolicyViolated", policyName);
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), "Logs does not match" );
		
		}
	
	@DataProvider
	public Object[][] _emailPolicyFileTransferupload() {
		return new Object[][]{
			//Policy Name                 App           Test User      admin   Object Access Activity Access  Activity Type     Object Type               Severity
			{ "FILE_TRANSFER_UPLOAD1",    "Office 365",   "testuser1",  "admin",  "upload",  "pdf",    		"Test",        "Policy Violation",  "Upload", "critical", "File,Office 365,Upload_Test_pdf.log" },
			{ "FILE_TRANSFER_UPLOAD2",    "Office 365",   "testuser1",  "admin",  "upload",  "7z",    		"Test",        "Policy Violation",  "Upload", "critical", "File,Office 365,Upload_Test_7z.log" },
			{ "FILE_TRANSFER_UPLOAD3",    "Office 365",   "testuser1",  "admin",  "upload",  "bin",    		"Test",        "Policy Violation",  "Upload", "critical", "File,Office 365,Upload_Test_bin.log" },
			{ "FILE_TRANSFER_UPLOAD4",    "Office 365",   "testuser1",  "admin",  "upload",  "doc",    		"Test",        "Policy Violation",  "Upload", "critical", "File,Office 365,Upload_Test_doc.log" },
			{ "FILE_TRANSFER_UPLOAD5",    "Office 365",   "testuser1",  "admin",  "upload",  "docx",    	"Test",        "Policy Violation",  "Upload", "critical", "File,Office 365,Upload_Test_docx.log" },
			{ "FILE_TRANSFER_UPLOAD6",   "Office 365",   "testuser1",  "admin",  "upload",  "exe",    		"Test",        "Policy Violation",  "Upload", "critical", "File,Office 365,Upload_Test_exe.log" },
			{ "FILE_TRANSFER_UPLOAD7",   "Office 365",   "testuser1",  "admin",  "upload",  "gif",    		"Test",        "Policy Violation",  "Upload", "critical", "File,Office 365,Upload_Test_gif.log" },
			{ "FILE_TRANSFER_UPLOAD8",   "Office 365",   "testuser1",  "admin",  "upload",  "html",    	"Test",        "Policy Violation",  "Upload", "critical", "File,Office 365,Upload_Test_html.log" },
			{ "FILE_TRANSFER_UPLOAD9",   "Office 365",   "testuser1",  "admin",  "upload",  "java",    	"Test",        "Policy Violation",  "Upload", "critical", "File,Office 365,Upload_Test_java.log" },
			{ "FILE_TRANSFER_UPLOAD10",   "Office 365",   "testuser1",  "admin",  "upload",  "jpg",    	    "Test",        "Policy Violation",  "Upload", "critical", "File,Office 365,Upload_Test_jpg.log" },
			{ "FILE_TRANSFER_UPLOAD11",   "Office 365",   "testuser1",  "admin",  "upload",  "json",    	"Test",        "Policy Violation",  "Upload", "critical", "File,Office 365,Upload_Test_json.log" },
			{ "FILE_TRANSFER_UPLOAD12",   "Office 365",   "testuser1",  "admin",  "upload",  "key",    		"Test",        "Policy Violation",  "Upload", "critical", "File,Office 365,Upload_Test_key.log" },
			{ "FILE_TRANSFER_UPLOAD13",   "Office 365",   "testuser1",  "admin",  "upload",  "mp3",    		"Test",        "Policy Violation",  "Upload", "critical", "File,Office 365,Upload_Test_mp3.log" },
			{ "FILE_TRANSFER_UPLOAD14",   "Office 365",   "testuser1",  "admin",  "upload",  "mp4",    		"Test",        "Policy Violation",  "Upload", "critical", "File,Office 365,Upload_Test_mp4.log" },
			{ "FILE_TRANSFER_UPLOAD15",   "Office 365",   "testuser1",  "admin",  "upload",  "numbers",     "Test",        "Policy Violation",  "Upload", "critical", "File,Office 365,Upload_Test_numbers.log" },
			{ "FILE_TRANSFER_UPLOAD16",   "Office 365",   "testuser1",  "admin",  "upload",  "pem",    	    "Test",        "Policy Violation",  "Upload", "critical", "File,Office 365,Upload_Test_pem.log" },
			{ "FILE_TRANSFER_UPLOAD17",   "Office 365",   "testuser1",  "admin",  "upload",  "png",    		"Test",        "Policy Violation",  "Upload", "critical", "File,Office 365,Upload_Test_png.log" },
			{ "FILE_TRANSFER_UPLOAD18",   "Office 365",   "testuser1",  "admin",  "upload",  "ppt",    		"Test",        "Policy Violation",  "Upload", "critical", "File,Office 365,Upload_Test_ppt.log" },
			{ "FILE_TRANSFER_UPLOAD19",   "Office 365",   "testuser1",  "admin",  "upload",  "txt",    		"Test",        "Policy Violation",  "Upload", "critical", "File,Office 365,Upload_Test_txt.log" },
			{ "FILE_TRANSFER_UPLOAD20",   "Office 365",   "testuser1",  "admin",  "upload",  "xls",    		"Test",        "Policy Violation",  "Upload", "critical", "File,Office 365,Upload_Test_xls.log" },
			{ "FILE_TRANSFER_UPLOAD21",   "Office 365",   "testuser1",  "admin",  "upload",  "zip",    		"Test",        "Policy Violation",  "Upload", "critical", "File,Office 365,Upload_Test_zip.log" }
			
		};
	}	
	
	
	@Test(groups ={"TEST"}, dataProvider = "_emaiPolicyFileTransferDownload")
	public void verifyEmailFileTransferPolicyEnabledDownload(String policyName, String saasApps,  String testUserName,  String notifyEmailId, String transferType, String fileType, String fileName,  String activityType, String objectType, String severity, String logFile) throws Exception{
		Reporter.log("Validate User Upload the file/folder to folder with Policy Enabled", true);
		testUserName=testUserName+"@"+suiteData.getTenantDomainName();
		String expectedMsg="[ALERT] "+testUserName+ " attempted to "+transferType+" content:"+fileName.toLowerCase()+"."+fileType+" violating policy:"+policyName;
		System.out.println("expectedMsg: "+expectedMsg);
		policyDataMap.clear();
		policyDataMap.put(GatewayTestConstants.POLICY_NAME, policyName);
		policyDataMap.put(GatewayTestConstants.APPLICATIONS, saasApps);
		policyDataMap.put(GatewayTestConstants.TARGET_USER, testUserName);
		policyDataMap.put(GatewayTestConstants.NOTIFY_EMAILID, notifyEmailId+"@"+suiteData.getTenantDomainName());
		policyDataMap.put(GatewayTestConstants.TRANSFER_TYPE, transferType);
		policyDataMap.put(GatewayTestConstants.FILE_TYPE, fileType);
		policyDataMap.put(GatewayTestConstants.FILE_NAME, fileName);
		PolicyAccessEnforcement.fileTransferPolicyCreateEnable(suiteData, requestHeader, policyDataMap);
		replayLogs(logFile);
		PolicyAccessEnforcement.deletePolicy(suiteData, requestHeader, policyDataMap);
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		data.put("policy_type", "FileTransfer");
		data.put("policy_action", "ALERT");
		data.put("action_taken", "block,email,");
		data.put("_PolicyViolated", policyName);
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), "Logs does not match" );
		
		}
	
	@DataProvider
	public Object[][] _emaiPolicyFileTransferDownload() {
		return new Object[][]{
			//Policy Name                 App           Test User      admin   Object Access Activity Access  Activity Type     Object Type               Severity
			{ "FILE_TRANSFER_DOWNLOAD1",    "Office 365",   "testuser1",  "admin",  "download",   "pdf",      "Test",        "Policy Violation",  "Download", "critical",   "File,Office 365,Download_Test_pdf.log" },
			{ "FILE_TRANSFER_DOWNLOAD2",    "Office 365",   "testuser1",  "admin",  "download",   "7z",       "Test",        "Policy Violation",  "Download", "critical",   "File,Office 365,Download_Test_7z.log" },
			{ "FILE_TRANSFER_DOWNLOAD3",    "Office 365",   "testuser1",  "admin",  "download",   "bin",      "Test",        "Policy Violation",  "Download", "critical",   "File,Office 365,Download_Test_bin.log" },
			{ "FILE_TRANSFER_DOWNLOAD4",    "Office 365",   "testuser1",  "admin",  "download",   "doc",      "Test",        "Policy Violation",  "Download", "critical",   "File,Office 365,Download_Test_doc.log" },
			{ "FILE_TRANSFER_DOWNLOAD5",    "Office 365",   "testuser1",  "admin",  "download",   "gif",      "Test",        "Policy Violation",  "Download", "critical",   "File,Office 365,Download_Test_gif.log" },
			{ "FILE_TRANSFER_DOWNLOAD6",    "Office 365",   "testuser1",  "admin",  "download",   "html",     "Test",        "Policy Violation",  "Download", "critical",   "File,Office 365,Download_Test_html.log" },
			{ "FILE_TRANSFER_DOWNLOAD7",    "Office 365",   "testuser1",  "admin",  "download",   "java",     "Test",        "Policy Violation",  "Download", "critical",   "File,Office 365,Download_Test_java.log" },
			{ "FILE_TRANSFER_DOWNLOAD8",    "Office 365",   "testuser1",  "admin",  "download",   "jpg",      "Test",        "Policy Violation",  "Download", "critical",   "File,Office 365,Download_Test_jpg.log" },
			{ "FILE_TRANSFER_DOWNLOAD9",    "Office 365",   "testuser1",  "admin",  "download",   "json",     "Test",        "Policy Violation",  "Download", "critical",   "File,Office 365,Download_Test_json.log" },
			{ "FILE_TRANSFER_DOWNLOAD10",    "Office 365",   "testuser1",  "admin",  "download",   "key",      "Test",        "Policy Violation",  "Download", "critical",   "File,Office 365,Download_Test_key.log" },
			{ "FILE_TRANSFER_DOWNLOAD11",    "Office 365",   "testuser1",  "admin",  "download",   "mp3",      "Test",        "Policy Violation",  "Download", "critical",   "File,Office 365,Download_Test_mp3.log" },
			{ "FILE_TRANSFER_DOWNLOAD12",    "Office 365",   "testuser1",  "admin",  "download",   "mp4",      "Test",        "Policy Violation",  "Download", "critical",   "File,Office 365,Download_Test_mp4.log" },
			{ "FILE_TRANSFER_DOWNLOAD13",    "Office 365",   "testuser1",  "admin",  "download",   "numbers",  "Test",        "Policy Violation",  "Download", "critical",   "File,Office 365,Download_Test_numbers.log" },
			{ "FILE_TRANSFER_DOWNLOAD14",    "Office 365",   "testuser1",  "admin",  "download",   "ppt",      "Test",        "Policy Violation",  "Download", "critical",   "File,Office 365,Download_Test_ppt.log" },
			{ "FILE_TRANSFER_DOWNLOAD15",    "Office 365",   "testuser1",  "admin",  "download",   "txt",      "Test",        "Policy Violation",  "Download", "critical",   "File,Office 365,Download_Test_txt.log" },
			{ "FILE_TRANSFER_DOWNLOAD16",    "Office 365",   "testuser1",  "admin",  "download",   "xls",      "Test",        "Policy Violation",  "Download", "critical",   "File,Office 365,Download_Test_xls.log" },
			{ "FILE_TRANSFER_DOWNLOAD17",    "Office 365",   "testuser1",  "admin",  "download",   "zip",      "Test",        "Policy Violation",  "Download", "critical",   "File,Office 365,Download_Test_zip.log" },
			
		};
	}
	
	
	

	@Test(groups ={"DELETE_ALL"})
	public void deleteAllPolicies() throws Exception{
		Reporter.log("Validate User Upload the file/folder to folder with Policy Enabled", true);
		PolicyAccessEnforcement.deleteAllPolicy(suiteData,  requestHeader);
	}
	
}
