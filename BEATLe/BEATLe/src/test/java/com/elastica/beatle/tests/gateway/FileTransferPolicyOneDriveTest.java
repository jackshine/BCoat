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



public class FileTransferPolicyOneDriveTest extends CommonConfiguration {

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
	
	
	
	@Test(groups ={"TEST"}, dataProvider = "_policyFileTransferupload")
	public void verifyFileTransferPolicyEnabledUpload(String policyName, String saasApps,  String testUserName,  String notifyEmailId, String transferType, String fileType, String fileName,  String activityType, String objectType, String severity, String logFile) throws Exception{
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
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), "Logs does not match" );
		
		}
	
	@DataProvider
	public Object[][] _policyFileTransferupload() {
		return new Object[][]{
			//Policy Name                 App           Test User      admin   Object Access Activity Access  Activity Type     Object Type               Severity
			{ "FILE_TRANSFER_UPLOAD1",    "Office 365",   "testuser1",  "admin",  "upload",  "pdf",    		"Test",        "Policy Violation",  "Upload", "critical", "Folder,Upload_file_test_pdf.log" },
			{ "FILE_TRANSFER_UPLOAD2",    "Office 365",   "testuser1",  "admin",  "upload",  "7z",    		"Test",        "Policy Violation",  "Upload", "critical", "Folder,Upload_file_test_7z.log" },
			{ "FILE_TRANSFER_UPLOAD3",    "Office 365",   "testuser1",  "admin",  "upload",  "avi",    		"Test",        "Policy Violation",  "Upload", "critical", "Folder,Upload_file_test_avi.log" },
			{ "FILE_TRANSFER_UPLOAD4",    "Office 365",   "testuser1",  "admin",  "upload",  "bin",    		"Test",        "Policy Violation",  "Upload", "critical", "Folder,Upload_file_test_bin.log" },
			{ "FILE_TRANSFER_UPLOAD5",    "Office 365",   "testuser1",  "admin",  "upload",  "bmp",    		"Test",        "Policy Violation",  "Upload", "critical", "Folder,Upload_file_test_bmp.log" },
			{ "FILE_TRANSFER_UPLOAD6",    "Office 365",   "testuser1",  "admin",  "upload",  "bz2",    		"Test",        "Policy Violation",  "Upload", "critical", "Folder,Upload_file_test_bz2.log" },
			{ "FILE_TRANSFER_UPLOAD7",    "Office 365",   "testuser1",  "admin",  "upload",  "cs",    		"Test",        "Policy Violation",  "Upload", "critical", "Folder,Upload_file_test_cs.log" },
			{ "FILE_TRANSFER_UPLOAD8",    "Office 365",   "testuser1",  "admin",  "upload",  "csv",    		"Test",        "Policy Violation",  "Upload", "critical", "Folder,Upload_file_test_csv.log" },
			{ "FILE_TRANSFER_UPLOAD9",    "Office 365",   "testuser1",  "admin",  "upload",  "dmg",    		"Test",        "Policy Violation",  "Upload", "critical", "Folder,Upload_file_test_dmg.log" },
			{ "FILE_TRANSFER_UPLOAD10",   "Office 365",   "testuser1",  "admin",  "upload",  "doc",    		"Test",        "Policy Violation",  "Upload", "critical", "Folder,Upload_file_test_doc.log" },
			{ "FILE_TRANSFER_UPLOAD11",   "Office 365",   "testuser1",  "admin",  "upload",  "docx",    		"Test",        "Policy Violation",  "Upload", "critical", "Folder,Upload_file_test_docx.log" },
			{ "FILE_TRANSFER_UPLOAD12",   "Office 365",   "testuser1",  "admin",  "upload",  "exe",    		"Test",        "Policy Violation",  "Upload", "critical", "Folder,Upload_file_test_exe.log" },
			{ "FILE_TRANSFER_UPLOAD13",   "Office 365",   "testuser1",  "admin",  "upload",  "flac",    		"Test",        "Policy Violation",  "Upload", "critical", "Folder,Upload_file_test_flac.log" },
			{ "FILE_TRANSFER_UPLOAD14",   "Office 365",   "testuser1",  "admin",  "upload",  "flv",    		"Test",        "Policy Violation",  "Upload", "critical", "Folder,Upload_file_test_flv.log" },
			{ "FILE_TRANSFER_UPLOAD15",   "Office 365",   "testuser1",  "admin",  "upload",  "gif",    		"Test",        "Policy Violation",  "Upload", "critical", "Folder,Upload_file_test_gif.log" },
			{ "FILE_TRANSFER_UPLOAD16",   "Office 365",   "testuser1",  "admin",  "upload",  "gz",           "Test",        "Policy Violation",  "Upload", "critical", "Folder,Upload_file_test_gz.log" },
			{ "FILE_TRANSFER_UPLOAD17",   "Office 365",   "testuser1",  "admin",  "upload",  "html",    		"Test",        "Policy Violation",  "Upload", "critical", "Folder,Upload_file_test_html.log" },
			{ "FILE_TRANSFER_UPLOAD18",   "Office 365",   "testuser1",  "admin",  "upload",  "java",    		"Test",        "Policy Violation",  "Upload", "critical", "Folder,Upload_file_test_java.log" },
			{ "FILE_TRANSFER_UPLOAD19",   "Office 365",   "testuser1",  "admin",  "upload",  "jpeg",    		"Test",        "Policy Violation",  "Upload", "critical", "Folder,Upload_file_test_jpeg.log" },
			{ "FILE_TRANSFER_UPLOAD20",   "Office 365",   "testuser1",  "admin",  "upload",  "jpg",    		"Test",        "Policy Violation",  "Upload", "critical", "Folder,Upload_file_test_jpg.log" },
			{ "FILE_TRANSFER_UPLOAD21",   "Office 365",   "testuser1",  "admin",  "upload",  "js",    		"Test",        "Policy Violation",  "Upload", "critical", "Folder,Upload_file_test_js.log" },			
			{ "FILE_TRANSFER_UPLOAD22",   "Office 365",   "testuser1",  "admin",  "upload",  "json",    		"Test",        "Policy Violation",  "Upload", "critical", "Folder,Upload_file_test_json.log" },
			{ "FILE_TRANSFER_UPLOAD23",   "Office 365",   "testuser1",  "admin",  "upload",  "key",    		"Test",        "Policy Violation",  "Upload", "critical", "Folder,Upload_file_test_key.log" },
			{ "FILE_TRANSFER_UPLOAD24",   "Office 365",   "testuser1",  "admin",  "upload",  "mov",    		"Test",        "Policy Violation",  "Upload", "critical", "Folder,Upload_file_test_mov.log" },
			{ "FILE_TRANSFER_UPLOAD25",   "Office 365",   "testuser1",  "admin",  "upload",  "mp3",    		"Test",        "Policy Violation",  "Upload", "critical", "Folder,Upload_file_test_mp3.log" },
			{ "FILE_TRANSFER_UPLOAD26",   "Office 365",   "testuser1",  "admin",  "upload",  "mp4",    		"Test",        "Policy Violation",  "Upload", "critical", "Folder,Upload_file_test_mp4.log" },
			{ "FILE_TRANSFER_UPLOAD27",   "Office 365",   "testuser1",  "admin",  "upload",  "mpg",    		"Test",        "Policy Violation",  "Upload", "critical", "Folder,Upload_file_test_mpg.log" },
			{ "FILE_TRANSFER_UPLOAD28",   "Office 365",   "testuser1",  "admin",  "upload",  "numbers",      "Test",        "Policy Violation",  "Upload", "critical", "Folder,Upload_file_test_numbers.log" },
			{ "FILE_TRANSFER_UPLOAD29",   "Office 365",   "testuser1",  "admin",  "upload",  "odg",    		"Test",        "Policy Violation",  "Upload", "critical", "Folder,Upload_file_test_odg.log" },
			{ "FILE_TRANSFER_UPLOAD30",   "Office 365",   "testuser1",  "admin",  "upload",  "odp",          "Test",        "Policy Violation",  "Upload", "critical", "Folder,Upload_file_test_odp.log" },
			{ "FILE_TRANSFER_UPLOAD31",   "Office 365",   "testuser1",  "admin",  "upload",  "ods",    		"Test",        "Policy Violation",  "Upload", "critical", "Folder,Upload_file_test_ods.log" },
			{ "FILE_TRANSFER_UPLOAD32",   "Office 365",   "testuser1",  "admin",  "upload",  "odt",    	    "Test",        "Policy Violation",  "Upload", "critical", "Folder,Upload_file_test_odt.log" },
			{ "FILE_TRANSFER_UPLOAD33",   "Office 365",   "testuser1",  "admin",  "upload",  "pem",    	    "Test",        "Policy Violation",  "Upload", "critical", "Folder,Upload_file_test_pem.log" },
			{ "FILE_TRANSFER_UPLOAD34",   "Office 365",   "testuser1",  "admin",  "upload",  "ppt",    		"Test",        "Policy Violation",  "Upload", "critical", "Folder,Upload_file_test_ppt.log" },
			{ "FILE_TRANSFER_UPLOAD35",   "Office 365",   "testuser1",  "admin",  "upload",  "txt",    		"Test",        "Policy Violation",  "Upload", "critical", "Folder,Upload_file_test_txt.log" },
			{ "FILE_TRANSFER_UPLOAD36",   "Office 365",   "testuser1",  "admin",  "upload",  "xml",    		"Test",        "Policy Violation",  "Upload", "critical", "Folder,Upload_file_test_xml.log" },
			{ "FILE_TRANSFER_UPLOAD37",   "Office 365",   "testuser1",  "admin",  "upload",  "zip",    		"Test",        "Policy Violation",  "Upload", "critical", "Folder,Upload_file_test_zip.log" }
			
		};
	}	
	
	
	@Test(groups ={"TEST"}, dataProvider = "_policyFileTransferDownload")
	public void verifyFileTransferPolicyEnabledDownload(String policyName, String saasApps,  String testUserName,  String notifyEmailId, String transferType, String fileType, String fileName,  String activityType, String objectType, String severity, String logFile) throws Exception{
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
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), "Logs does not match" );
		
		}
	
	@DataProvider
	public Object[][] _policyFileTransferDownload() {
		return new Object[][]{
			//Policy Name                 App           Test User      admin   Object Access Activity Access  Activity Type     Object Type               Severity
			{ "FILE_TRANSFER_DOWNLOAD1",    "Office 365",   "testuser1",  "admin",  "download",   "pdf",      "Test",        "Policy Violation",  "Download", "critical",   "Folder,Download_file_test_pdf.log" },
			{ "FILE_TRANSFER_DOWNLOAD2",    "Office 365",   "testuser1",  "admin",  "download",   "7z",       "Test",        "Policy Violation",  "Download", "critical",   "Folder,Download_file_test_7z.log" },
			{ "FILE_TRANSFER_DOWNLOAD3",    "Office 365",   "testuser1",  "admin",  "download",   "avi",      "Test",        "Policy Violation",  "Download", "critical",   "Folder,Download_file_test_avi.log" },
			{ "FILE_TRANSFER_DOWNLOAD4",    "Office 365",   "testuser1",  "admin",  "download",   "doc",      "Test",        "Policy Violation",  "Download", "critical",   "Folder,Download_file_test_doc.log" },
			{ "FILE_TRANSFER_DOWNLOAD5",    "Office 365",   "testuser1",  "admin",  "download",   "docx",     "Test",        "Policy Violation",  "Download", "critical",   "Folder,Download_file_test_docx.log" },
			{ "FILE_TRANSFER_DOWNLOAD6",    "Office 365",   "testuser1",  "admin",  "download",   "exe",      "Test",        "Policy Violation",  "Download", "critical",   "Folder,Download_file_test_exe.log" },
			{ "FILE_TRANSFER_DOWNLOAD7",    "Office 365",   "testuser1",  "admin",  "download",   "java",     "Test",        "Policy Violation",  "Download", "critical",   "Folder,Download_file_test_java.log" },
			{ "FILE_TRANSFER_DOWNLOAD8",    "Office 365",   "testuser1",  "admin",  "download",   "json",     "Test",        "Policy Violation",  "Download", "critical",   "Folder,Download_file_test_json.log" },
			{ "FILE_TRANSFER_DOWNLOAD9",    "Office 365",   "testuser1",  "admin",  "download",   "txt",      "Test",        "Policy Violation",  "Download", "critical",   "Folder,Download_file_test_txt.log" }
			
		};
	}
	
	
	
	//@Test(groups ={"POLICY","DEV"}, dataProvider = "_policyFileSizeTransfer")
	public void verifyFileTransferPolicyWithFileSizeEnabled(String policyName, String saasApps,  String testUserName,  String notifyEmailId, String transferType, String fileType, String fileName, String fileSizeLargerThan, String fileSizeSmallerThan, String activityType, String objectType, String severity) throws Exception{
		Reporter.log("Validate User Upload the file/folder to folder with Policy Enabled", true);
		String expectedMsg="[ALERT] "+testUserName+ " attempted to upload content:"+fileName+" violating policy:"+policyName;
		policyDataMap.clear();
		policyDataMap.put(GatewayTestConstants.POLICY_NAME, policyName);
		policyDataMap.put(GatewayTestConstants.APPLICATIONS, saasApps);
		policyDataMap.put(GatewayTestConstants.TARGET_USER, testUserName+"@"+suiteData.getTenantDomainName());
		policyDataMap.put(GatewayTestConstants.NOTIFY_EMAILID, notifyEmailId+"@"+suiteData.getTenantDomainName());
		policyDataMap.put(GatewayTestConstants.TRANSFER_TYPE, transferType);
		policyDataMap.put(GatewayTestConstants.FILE_TYPE, fileType);
		policyDataMap.put(GatewayTestConstants.FILE_NAME, fileName);
		policyDataMap.put(GatewayTestConstants.LARGER_THAN, fileSizeLargerThan);
		policyDataMap.put(GatewayTestConstants.SMALLER_THAN, fileSizeSmallerThan);
		PolicyAccessEnforcement.fileTransferPolicyWithFileSizeCreateEnable(suiteData, requestHeader, policyDataMap);

		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		data.put("policy_type", "AccessEnforcement");
		data.put("policy_action", "ALERT");
		data.put("action_taken", "block,email,");
		data.put("_PolicyViolated", policyName);
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), "Logs does not match" );
		
	}
	
	@DataProvider
	public Object[][] _policyFileSizeTransfer() {
		return new Object[][]{
			//Policy Name                 App     Test User   admin     Object Access Activity Access      Activity Type     Object Type  Severity
			{ "FILE_TRANSFER_UPLOAD1",  "Box",   "testuser1",  "admin",  "upload",  "txt",    "Test",      "1", "2",    "Policy Violation",  "Upload", "critical" },
			{ "FILE_TRANSFER_UPLOAD2",  "Box",   "testuser1",  "admin",  "upload",  "pdf",    "Test",      "1", "2",    "Policy Violation",  "Upload", "critical" },
			{ "FILE_TRANSFER_UPLOAD3",  "Box",   "testuser1",  "admin",  "upload",  "rtf",    "Test",      "1", "2",    "Policy Violation",  "Upload", "critical" },
			{ "FILE_TRANSFER_UPLOAD4",  "Box",   "testuser1",  "admin",  "upload",  "doc",    "Test",      "1", "2",    "Policy Violation",  "Upload", "critical" },
			{ "FILE_TRANSFER_UPLOAD5",  "Box",   "testuser1",  "admin",  "upload",  "docx",   "Test",      "1", "2",    "Policy Violation",  "Upload", "critical" },
			{ "FILE_TRANSFER_UPLOAD6",  "Box",   "testuser1",  "admin",  "upload",  "xlsx",   "Test",      "1", "2",    "Policy Violation",  "Upload", "critical" },
			{ "FILE_TRANSFER_UPLOAD7",  "Box",   "testuser1",  "admin",  "upload",  "__ALL_EL__",   "ANY", "1", "2",    "Policy Violation",  "Upload", "critical" },
			
			{ "FILE_TRANSFER_DOWNLOAD1",  "Box",   "testuser1",  "admin",  "download",  "txt",    "Test",    "1", "2",  "Policy Violation",  "Download", "critical" },
			{ "FILE_TRANSFER_DOWNLOAD2",  "Box",   "testuser1",  "admin",  "download",  "pdf",    "Test",    "1", "2",  "Policy Violation",  "Download", "critical" },
			{ "FILE_TRANSFER_DOWNLOAD3",  "Box",   "testuser1",  "admin",  "download",  "rtf",    "Test",    "1", "2",  "Policy Violation",  "Download", "critical" },
			{ "FILE_TRANSFER_DOWNLOAD4",  "Box",   "testuser1",  "admin",  "download",  "doc",    "Test",    "1", "2",  "Policy Violation",  "Download", "critical" },
			{ "FILE_TRANSFER_DOWNLOAD5",  "Box",   "testuser1",  "admin",  "download",  "docx",   "Test",    "1", "2",  "Policy Violation",  "Download", "critical" },
			{ "FILE_TRANSFER_DOWNLOAD6",  "Box",   "testuser1",  "admin",  "download",  "xlsx",   "Test",    "1", "2",  "Policy Violation",  "Download", "critical" },
			{ "FILE_TRANSFER_DOWNLOAD7",  "Box",   "testuser1",  "admin",  "download",  "__ALL_EL__",   "ANY","1", "2", "Policy Violation",  "Download", "critical" }
		};
	}
	
	
	

	@Test(groups ={"DELETE_ALL"})
	public void deleteAllPolicies() throws Exception{
		Reporter.log("Validate User Upload the file/folder to folder with Policy Enabled", true);
		PolicyAccessEnforcement.deleteAllPolicy(suiteData,  requestHeader);
	}
	
}
