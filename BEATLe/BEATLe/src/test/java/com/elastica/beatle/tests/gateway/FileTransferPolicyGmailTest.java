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



public class FileTransferPolicyGmailTest extends CommonConfiguration {

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
	@BeforeMethod(alwaysRun=true)
	public void beforMethod(Method method) throws Exception {
	deleteAllPolicies();
	Reporter.log("--------------------------------------------", true);
	Reporter.log("Deleting all policies", true);
	Reporter.log("--------------------------------------------", true);
	}
	
	
	
	@Test(groups ={"TEST"}, dataProvider = "_policyFileTransferEventupload")
	public void verifyFileTransferGmailPolicyEnabledUpload(String policyName, String saasApps,  String testUserName,  String notifyEmailId, String transferType, String fileType, String fileName,  String activityType, String objectType, String severity, String logFile) throws Exception{
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
		replayLogsEPDV3(logFile);
		PolicyAccessEnforcement.deletePolicy(suiteData, requestHeader, policyDataMap);
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		data.put("policy_type", "FileTransfer");
		data.put("policy_action", "ALERT");
		data.put("action_taken", "block,email,");
		data.put("_PolicyViolated", policyName);
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(activityType, severity), data)), "Logs does not match" );
		
		}
	
	@DataProvider
	public Object[][] _policyFileTransferEventupload() {
		return new Object[][]{
			//Policy Name                 App           Test User      admin   Object Access Activity Access  Activity Type     Object Type               Severity
			{ "FILE_TRANSFER_GMAIL_UPLOAD1",    "Gmail",   "testuser1",  "admin",  "upload",  "rtf",    		"pii",         "Policy Violation",  "Upload", "critical", "Mail,EmailUploadMultiFile_googleMail.log" },
			{ "FILE_TRANSFER_GMAIL_UPLOAD2",    "Gmail",   "testuser1",  "admin",  "upload",  "doc",    		"Test",        "Policy Violation",  "Upload", "critical", "Gmail,Upload_File_Test_doc.log" },
//			{ "FILE_TRANSFER_GMAIL_UPLOAD3",    "Gmail",   "testuser1",  "admin",  "upload",  "exe",    		"Test",        "Policy Violation",  "Upload", "critical", "Gmail,Upload_File_Test_exe.log" },
			{ "FILE_TRANSFER_GMAIL_UPLOAD4",    "Gmail",   "testuser1",  "admin",  "upload",  "mp4",    		"Test",        "Policy Violation",  "Upload", "critical", "Gmail,Upload_File_Test_mp4.log" },
			{ "FILE_TRANSFER_GMAIL_UPLOAD5",    "Gmail",   "testuser1",  "admin",  "upload",  "pdf",    		"Test",        "Policy Violation",  "Upload", "critical", "Gmail,Upload_File_Test_pdf.log" },
			{ "FILE_TRANSFER_GMAIL_UPLOAD6",    "Gmail",   "testuser1",  "admin",  "upload",  "xls",    		"Test",        "Policy Violation",  "Upload", "critical", "Gmail,Upload_File_Test_xls.log" },
			{ "FILE_TRANSFER_GMAIL_UPLOAD7",    "Gmail",   "testuser1",  "admin",  "upload",  "zip",    		"Test",        "Policy Violation",  "Upload", "critical", "Gmail,Upload_File_Test_zip.log" },
			{ "FILE_TRANSFER_GMAIL_UPLOAD8",    "Gmail",   "testuser1",  "admin",  "upload",  "7z",    		    "Test",        "Policy Violation",  "Upload", "critical", "Gmail,Upload_File_7z.log" },
			{ "FILE_TRANSFER_GMAIL_UPLOAD9",    "Gmail",   "testuser1",  "admin",  "upload",  "bin",    		"Test",        "Policy Violation",  "Upload", "critical", "Gmail,Upload_File_bin.log" },
			{ "FILE_TRANSFER_GMAIL_UPLOAD10",   "Gmail",   "testuser1",  "admin",  "upload",  "bz2",    		"Test",        "Policy Violation",  "Upload", "critical", "Gmail,Upload_File_bz2.log" },
			{ "FILE_TRANSFER_GMAIL_UPLOAD11",   "Gmail",   "testuser1",  "admin",  "upload",  "cs",    		    "Test",        "Policy Violation",  "Upload", "critical", "Gmail,Upload_File_cs.log" },
			{ "FILE_TRANSFER_GMAIL_UPLOAD12",   "Gmail",   "testuser1",  "admin",  "upload",  "docx",    		"Test",        "Policy Violation",  "Upload", "critical", "Gmail,Upload_File_docx.log" },
			{ "FILE_TRANSFER_GMAIL_UPLOAD13",   "Gmail",   "testuser1",  "admin",  "upload",  "gif",    		"Test",        "Policy Violation",  "Upload", "critical", "Gmail,Upload_File_gif.log" },
			{ "FILE_TRANSFER_GMAIL_UPLOAD14",   "Gmail",   "testuser1",  "admin",  "upload",  "html",    		"Test",        "Policy Violation",  "Upload", "critical", "Gmail,Upload_File_html.log" },
			{ "FILE_TRANSFER_GMAIL_UPLOAD15",   "Gmail",   "testuser1",  "admin",  "upload",  "java",    		"Test",        "Policy Violation",  "Upload", "critical", "Gmail,Upload_File_java.log" },
			{ "FILE_TRANSFER_GMAIL_UPLOAD16",   "Gmail",   "testuser1",  "admin",  "upload",  "jpeg",    		"Test",        "Policy Violation",  "Upload", "critical", "Gmail,Upload_File_jpeg.log" },
			{ "FILE_TRANSFER_GMAIL_UPLOAD17",   "Gmail",   "testuser1",  "admin",  "upload",  "json",    		"Test",        "Policy Violation",  "Upload", "critical", "Gmail,Upload_File_json.log" },
			{ "FILE_TRANSFER_GMAIL_UPLOAD18",   "Gmail",   "testuser1",  "admin",  "upload",  "key",    		"Test",        "Policy Violation",  "Upload", "critical", "Gmail,Upload_File_key.log" },
			{ "FILE_TRANSFER_GMAIL_UPLOAD19",   "Gmail",   "testuser1",  "admin",  "upload",  "numbers",        "Test",        "Policy Violation",  "Upload", "critical", "Gmail,Upload_File_numbers.log" },
			{ "FILE_TRANSFER_GMAIL_UPLOAD20",   "Gmail",   "testuser1",  "admin",  "upload",  "pem",    		"Test",        "Policy Violation",  "Upload", "critical", "Gmail,Upload_File_pem.log" },
			{ "FILE_TRANSFER_GMAIL_UPLOAD21",   "Gmail",   "testuser1",  "admin",  "upload",  "png",    		"Test",        "Policy Violation",  "Upload", "critical", "Gmail,Upload_File_png.log" },
			{ "FILE_TRANSFER_GMAIL_UPLOAD22",   "Gmail",   "testuser1",  "admin",  "upload",  "rar",    		"Test",        "Policy Violation",  "Upload", "critical", "Gmail,Upload_File_rar.log" },
		};
	}	
	
	
	@Test(groups ={"TEST"}, dataProvider = "_policyFileTransferEventDownload")
	public void verifyFileTransferGmailPolicyEnabledDownload(String policyName, String saasApps,  String testUserName,  String notifyEmailId, String transferType, String fileType, String fileName,  String activityType, String objectType, String severity, String logFile) throws Exception{
		Reporter.log("Validate User Upload the file/folder to folder with Policy Enabled", true);
		testUserName=testUserName+"@"+suiteData.getTenantDomainName();
		String expectedMsg="[ALERT]  attempted to "+transferType+" content:"+fileName.toLowerCase()+"."+fileType+" violating policy:"+policyName;
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
		replayLogsEPDV3(logFile);
		PolicyAccessEnforcement.deletePolicy(suiteData, requestHeader, policyDataMap);
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		data.put("policy_type", "FileTransfer");
		data.put("policy_action", "ALERT");
		data.put("action_taken", "block,email,");
		data.put("_PolicyViolated", policyName);
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(activityType, severity), data)), "Logs does not match" );
		
		}
	
	@DataProvider
	public Object[][] _policyFileTransferEventDownload() {
		return new Object[][]{
			//Policy Name                 App           Test User      admin   Object Access Activity Access  Activity Type     Object Type               Severity
			{ "FILE_TRANSFER_GMAIL_DOWNLOAD1",    "Gmail",   "testuser1",  "admin",  "download",   "doc",     "Test",        "Policy Violation",  "Download", "critical",   "Gmail,Download_File_Test_doc.log" },
			{ "FILE_TRANSFER_GMAIL_DOWNLOAD2",    "Gmail",   "testuser1",  "admin",  "download",   "mp4",     "Test",        "Policy Violation",  "Download", "critical",   "Gmail,Download_File_Test_mp4.log" },
			{ "FILE_TRANSFER_GMAIL_DOWNLOAD3",    "Gmail",   "testuser1",  "admin",  "download",   "pdf",     "Test",        "Policy Violation",  "Download", "critical",   "Gmail,Download_File_Test_pdf.log" },
			{ "FILE_TRANSFER_GMAIL_DOWNLOAD4",    "Gmail",   "testuser1",  "admin",  "download",   "xls",     "Test",        "Policy Violation",  "Download", "critical",   "Gmail,Download_File_Test_xls.log" },
			{ "FILE_TRANSFER_GMAIL_DOWNLOAD5",    "Gmail",   "testuser1",  "admin",  "download",   "zip",     "Test",        "Policy Violation",  "Download", "critical",   "Gmail,Download_File_Test_zip.log" },
//			{ "FILE_TRANSFER_GMAIL_DOWNLOAD6",    "Gmail",   "testuser1",  "admin",  "download",   "zip",     "Test",        "Policy Violation",  "Download", "critical",   "Gmail,Download_File_Test1_mp3.log" },
			{ "FILE_TRANSFER_GMAIL_DOWNLOAD7",    "Gmail",   "testuser1",  "admin",  "download",   "docx",    "Test",        "Policy Violation",  "Download", "critical",   "Gmail,Download_File_docx.log" },
			{ "FILE_TRANSFER_GMAIL_DOWNLOAD8",    "Gmail",   "testuser1",  "admin",  "download",   "gif",     "Test",        "Policy Violation",  "Download", "critical",   "Gmail,Download_File_gif.log" },
			{ "FILE_TRANSFER_GMAIL_DOWNLOAD9",    "Gmail",   "testuser1",  "admin",  "download",   "java",    "Test",        "Policy Violation",  "Download", "critical",   "Gmail,Download_File_java.log" },
			{ "FILE_TRANSFER_GMAIL_DOWNLOAD10",   "Gmail",   "testuser1",  "admin",  "download",   "json",    "Test",        "Policy Violation",  "Download", "critical",   "Gmail,Download_File_json.log" },
			{ "FILE_TRANSFER_GMAIL_DOWNLOAD11",   "Gmail",   "testuser1",  "admin",  "download",   "key",     "Test",        "Policy Violation",  "Download", "critical",   "Gmail,Download_File_key.log" },
			{ "FILE_TRANSFER_GMAIL_DOWNLOAD12",   "Gmail",   "testuser1",  "admin",  "download",   "pem",     "Test",        "Policy Violation",  "Download", "critical",   "Gmail,Download_File_pem.log" },

		};
	}
	
	
	@Test(groups ={"TEST"}, dataProvider = "_policyTransferFileSize")
	public void verifyFileTransferPolicyFileSizeEnabled(String policyName, String saasApps, String fileUploaded, String testUserName,  String notifyEmailId, String transferType, String fileType, String fileName, String fileSizeLargerThan, String fileSizeSmallerThan, String activityType, String objectType, String severity, String logFile) throws Exception{
		Reporter.log("Validate User Upload the file/folder to folder with Policy Enabled", true);
		testUserName=testUserName+"@"+suiteData.getTenantDomainName();
		String expectedMsg="[ALERT] "+testUserName+ " attempted to upload content:"+fileUploaded+" violating policy:"+policyName;
		policyDataMap.clear();
		policyDataMap.put(GatewayTestConstants.POLICY_NAME, policyName);
		policyDataMap.put(GatewayTestConstants.APPLICATIONS, saasApps);
		policyDataMap.put(GatewayTestConstants.TARGET_USER, testUserName);
		policyDataMap.put(GatewayTestConstants.NOTIFY_EMAILID, notifyEmailId+"@"+suiteData.getTenantDomainName());
		policyDataMap.put(GatewayTestConstants.TRANSFER_TYPE, transferType);
		policyDataMap.put(GatewayTestConstants.FILE_TYPE, fileType);
		policyDataMap.put(GatewayTestConstants.FILE_NAME, fileName);
		policyDataMap.put(GatewayTestConstants.LARGER_THAN, fileSizeLargerThan);
		policyDataMap.put(GatewayTestConstants.SMALLER_THAN, fileSizeSmallerThan);
		
		PolicyAccessEnforcement.fileTransferPolicyWithFileSizeCreateEnable(suiteData, requestHeader, policyDataMap);
		replayLogsEPDV3(logFile);
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
	public Object[][] _policyTransferFileSize() {
		return new Object[][]{
			//Policy Name                 App     Test User   admin     Object Access Activity Access      Activity Type     Object Type  Severity
			
			{ "FILE_TRANSFER_UPLOAD_SIZE_LIMIT1",  "Gmail",  "lessthan1mb_form.pdf", "testuser1",  "admin",  "upload",  "__ALL_EL__",   "ANY", "0", "1",    "Policy Violation",  "Upload", "critical","Gmail,Upload_LessThan1MB_File.log" },
			{ "FILE_TRANSFER_UPLOAD_SIZE_LIMIT2",  "Gmail",  "greaterthan1mb.pdf", "testuser1",  "admin",  "upload",  "__ALL_EL__",   "ANY", "1", "2",    "Policy Violation",  "Upload", "critical","Gmail,Upload_GreaterThan1MB_File.log" },
			{ "FILE_TRANSFER_UPLOAD_SIZE_LIMIT3",  "Gmail",  "greaterthan2mb.pdf", "testuser1",  "admin",  "upload",  "__ALL_EL__",   "ANY", "1", "2",    "Policy Violation",  "Upload", "critical","Gmail,Upload_GreaterThan2MB_File.log" },
			
			};
			}
	
	@Test(groups ={"TEST"}, dataProvider = "_downloadPolicyTransferFileSize")
	public void verifyFileTransferPolicyDownloadFileSizeEnabled(String policyName, String saasApps, String fileDownloaded, String testUserName,  String notifyEmailId, String transferType, String fileType, String fileName, String fileSizeLargerThan, String fileSizeSmallerThan, String activityType, String objectType, String severity, String logFile) throws Exception{
		Reporter.log("Validate User Upload the file/folder to folder with Policy Enabled", true);
		testUserName=testUserName+"@"+suiteData.getTenantDomainName();
		String expectedMsg="[ALERT]  attempted to download content:"+fileDownloaded+" violating policy:"+policyName;
		policyDataMap.clear();
		policyDataMap.put(GatewayTestConstants.POLICY_NAME, policyName);
		policyDataMap.put(GatewayTestConstants.APPLICATIONS, saasApps);
		policyDataMap.put(GatewayTestConstants.TARGET_USER, testUserName);
		policyDataMap.put(GatewayTestConstants.NOTIFY_EMAILID, notifyEmailId+"@"+suiteData.getTenantDomainName());
		policyDataMap.put(GatewayTestConstants.TRANSFER_TYPE, transferType);
		policyDataMap.put(GatewayTestConstants.FILE_TYPE, fileType);
		policyDataMap.put(GatewayTestConstants.FILE_NAME, fileName);
		policyDataMap.put(GatewayTestConstants.LARGER_THAN, fileSizeLargerThan);
		policyDataMap.put(GatewayTestConstants.SMALLER_THAN, fileSizeSmallerThan);
		
		PolicyAccessEnforcement.fileTransferPolicyWithFileSizeCreateEnable(suiteData, requestHeader, policyDataMap);
		replayLogsEPDV3(logFile);
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
	public Object[][] _downloadPolicyTransferFileSize() {
		return new Object[][]{
			//Policy Name                 App     Test User   admin     Object Access Activity Access      Activity Type     Object Type  Severity
			
			{ "FILE_TRANSFER_DOWNLOAD_SIZE_LIMIT1", "Gmail",  "lessthan1mb form.pdf", "testuser1",  "admin",  "download",  "__ALL_EL__",   "ANY", "0", "1",    "Policy Violation",  "Download", "critical", "Gmail,Download_LessThan1MB_File.log" },
			{ "FILE_TRANSFER_DOWNLOAD_SIZE_LIMIT2", "Gmail",  "greaterthan1mb.pdf", "testuser1",  "admin",  "download",  "__ALL_EL__",   "ANY", "1", "2",    "Policy Violation",  "Download", "critical", "Gmail,Download_GreaterThan1MB_File.log" },
			{ "FILE_TRANSFER_DOWNLOAD_SIZE_LIMIT3", "Gmail",  "greaterthan2mb.pdf", "testuser1",  "admin",  "download",  "__ALL_EL__",   "ANY", "1", "2",    "Policy Violation",  "Download", "critical", "Gmail,Download_GreaterThan2MB_File.log" },
			
			};
			}
	

	@Test(groups ={"DELETE_ALL"})
	public void deleteAllPolicies() throws Exception{
		Reporter.log("Validate User Upload the file/folder to folder with Policy Enabled", true);
		PolicyAccessEnforcement.deleteAllPolicy(suiteData,  requestHeader);
	}
	
}
