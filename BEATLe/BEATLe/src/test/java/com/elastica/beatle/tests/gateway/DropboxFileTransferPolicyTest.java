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



public class DropboxFileTransferPolicyTest extends CommonConfiguration {

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
	
	
	
	@Test(groups ={"TEST"}, dataProvider = "_PolicyFileTransferupload")
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
	public Object[][] _PolicyFileTransferupload() {
		return new Object[][]{
			//Policy Name                App        Test User      admin   Object Access Activity Access  Activity Type     Object Type               Severity
			{ "FILE_TRANSFER_UPLOAD1",   "Dropbox", "testuser4",  "admin",  "upload",  "7z",    "Test", "Policy Violation",  "Upload", "critical", "File,Dropbox_2,Upload_Test_7z.log" },
			{ "FILE_TRANSFER_UPLOAD2",   "Dropbox", "testuser4",  "admin",  "upload",  "avi",   "Test", "Policy Violation",  "Upload", "critical", "File,Dropbox_2,Upload_Test_avi.log" },
			{ "FILE_TRANSFER_UPLOAD3",   "Dropbox", "testuser4",  "admin",  "upload",  "bin",   "Test", "Policy Violation",  "Upload", "critical", "File,Dropbox_2,Upload_Test_bin.log" },
			{ "FILE_TRANSFER_UPLOAD4",   "Dropbox", "testuser4",  "admin",  "upload",  "bmp",   "Test", "Policy Violation",  "Upload", "critical", "File,Dropbox_2,Upload_Test_bmp.log" },
			{ "FILE_TRANSFER_UPLOAD5",   "Dropbox", "testuser4",  "admin",  "upload",  "bz2",   "Test", "Policy Violation",  "Upload", "critical", "File,Dropbox_2,Upload_Test_bz2.log" },
			{ "FILE_TRANSFER_UPLOAD6",   "Dropbox", "testuser4",  "admin",  "upload",  "cs",    "Test", "Policy Violation",  "Upload", "critical", "File,Dropbox_2,Upload_Test_cs.log" },
			{ "FILE_TRANSFER_UPLOAD7",   "Dropbox", "testuser4",  "admin",  "upload",  "csv",   "Test", "Policy Violation",  "Upload", "critical", "File,Dropbox_2,Upload_Test_csv.log" },
			{ "FILE_TRANSFER_UPLOAD8",   "Dropbox", "testuser4",  "admin",  "upload",  "dmg",   "Test", "Policy Violation",  "Upload", "critical", "File,Dropbox_2,Upload_Test_dmg.log" },
			{ "FILE_TRANSFER_UPLOAD9",   "Dropbox", "testuser4",  "admin",  "upload",  "doc",   "Test", "Policy Violation",  "Upload", "critical", "File,Dropbox_2,Upload_Test_doc.log" },
			{ "FILE_TRANSFER_UPLOAD10",  "Dropbox", "testuser4",  "admin",  "upload",  "docx",  "Test", "Policy Violation",  "Upload", "critical", "File,Dropbox_2,Upload_Test_docx.log" },
			{ "FILE_TRANSFER_UPLOAD11",  "Dropbox", "testuser4",  "admin",  "upload",  "exe",   "Test", "Policy Violation",  "Upload", "critical", "File,Dropbox_2,Upload_Test_exe.log" },
			{ "FILE_TRANSFER_UPLOAD12",  "Dropbox", "testuser4",  "admin",  "upload",  "flac",  "Test", "Policy Violation",  "Upload", "critical", "File,Dropbox_2,Upload_Test_flac.log" },
			{ "FILE_TRANSFER_UPLOAD13",  "Dropbox", "testuser4",  "admin",  "upload",  "flv",   "Test", "Policy Violation",  "Upload", "critical", "File,Dropbox_2,Upload_Test_flv.log" },
			{ "FILE_TRANSFER_UPLOAD14",  "Dropbox", "testuser4",  "admin",  "upload",  "gif",   "Test", "Policy Violation",  "Upload", "critical", "File,Dropbox_2,Upload_Test_gif.log" },
			{ "FILE_TRANSFER_UPLOAD15",  "Dropbox", "testuser4",  "admin",  "upload",  "gz",    "Test", "Policy Violation",  "Upload", "critical", "File,Dropbox_2,Upload_Test_gz.log" },
			{ "FILE_TRANSFER_UPLOAD16",  "Dropbox", "testuser4",  "admin",  "upload",  "html",  "Test", "Policy Violation",  "Upload", "critical", "File,Dropbox_2,Upload_Test_html.log" },
			{ "FILE_TRANSFER_UPLOAD17",  "Dropbox", "testuser4",  "admin",  "upload",  "java",  "Test", "Policy Violation",  "Upload", "critical", "File,Dropbox_2,Upload_Test_java.log" },
			{ "FILE_TRANSFER_UPLOAD18",  "Dropbox", "testuser4",  "admin",  "upload",  "jpeg",  "Test", "Policy Violation",  "Upload", "critical", "File,Dropbox_2,Upload_Test_jpeg.log" },
			{ "FILE_TRANSFER_UPLOAD19",  "Dropbox", "testuser4",  "admin",  "upload",  "jpg",   "Test", "Policy Violation",  "Upload", "critical", "File,Dropbox_2,Upload_Test_jpg.log" },
			{ "FILE_TRANSFER_UPLOAD20",  "Dropbox", "testuser4",  "admin",  "upload",  "json",  "Test", "Policy Violation",  "Upload", "critical", "File,Dropbox_2,Upload_Test_json.log" },
			{ "FILE_TRANSFER_UPLOAD21",  "Dropbox", "testuser4",  "admin",  "upload",  "key",   "Test", "Policy Violation",  "Upload", "critical", "File,Dropbox_2,Upload_Test_key.log" },
			{ "FILE_TRANSFER_UPLOAD22",  "Dropbox", "testuser4",  "admin",  "upload",  "mov",   "Test", "Policy Violation",  "Upload", "critical", "File,Dropbox_2,Upload_Test_mov.log" },
			{ "FILE_TRANSFER_UPLOAD23",  "Dropbox", "testuser4",  "admin",  "upload",  "mp3",   "Test", "Policy Violation",  "Upload", "critical", "File,Dropbox_2,Upload_Test_mp3.log" },
			{ "FILE_TRANSFER_UPLOAD24",  "Dropbox", "testuser4",  "admin",  "upload",  "mp4",   "Test", "Policy Violation",  "Upload", "critical", "File,Dropbox_2,Upload_Test_mp4.log" },
			{ "FILE_TRANSFER_UPLOAD25",  "Dropbox", "testuser4",  "admin",  "upload",  "mpg",   "Test", "Policy Violation",  "Upload", "critical", "File,Dropbox_2,Upload_Test_mpg.log" },
			{ "FILE_TRANSFER_UPLOAD26",  "Dropbox", "testuser4",  "admin",  "upload",  "numbers",   "Test", "Policy Violation",  "Upload", "critical", "File,Dropbox_2,Upload_Test_numbers.log" },
			{ "FILE_TRANSFER_UPLOAD27",  "Dropbox", "testuser4",  "admin",  "upload",  "odg",   "Test", "Policy Violation",  "Upload", "critical", "File,Dropbox_2,Upload_Test_odg.log" },
			{ "FILE_TRANSFER_UPLOAD28",  "Dropbox", "testuser4",  "admin",  "upload",  "odp",   "Test", "Policy Violation",  "Upload", "critical", "File,Dropbox_2,Upload_Test_odp.log" },
			{ "FILE_TRANSFER_UPLOAD29",  "Dropbox", "testuser4",  "admin",  "upload",  "ods",   "Test", "Policy Violation",  "Upload", "critical", "File,Dropbox_2,Upload_Test_ods.log" },
			{ "FILE_TRANSFER_UPLOAD30",  "Dropbox", "testuser4",  "admin",  "upload",  "odt",   "Test", "Policy Violation",  "Upload", "critical", "File,Dropbox_2,Upload_Test_odt.log" },
			{ "FILE_TRANSFER_UPLOAD31",  "Dropbox", "testuser4",  "admin",  "upload",  "pdf",   "Test", "Policy Violation",  "Upload", "critical", "File,Dropbox_2,Upload_Test_pdf.log" },
			{ "FILE_TRANSFER_UPLOAD33",  "Dropbox", "testuser4",  "admin",  "upload",  "pem",   "Test", "Policy Violation",  "Upload", "critical", "File,GDrive_2,Upload_Test_pem.log" },
			{ "FILE_TRANSFER_UPLOAD34",  "Dropbox", "testuser4",  "admin",  "upload",  "png",   "Test", "Policy Violation",  "Upload", "critical", "File,GDrive_2,Upload_Test_png.log" },
			{ "FILE_TRANSFER_UPLOAD35",  "Dropbox", "testuser4",  "admin",  "upload",  "ppt",   "Test", "Policy Violation",  "Upload", "critical", "File,GDrive_2,Upload_Test_ppt.log" },
			{ "FILE_TRANSFER_UPLOAD36",  "Dropbox", "testuser4",  "admin",  "upload",  "properties",   "Test", "Policy Violation",  "Upload", "critical", "File,GDrive_2,Upload_Test_properties.log" },
			{ "FILE_TRANSFER_UPLOAD37",  "Dropbox", "testuser4",  "admin",  "upload",  "rar",   "Test", "Policy Violation",  "Upload", "critical", "File,GDrive_2,Upload_Test_rar.log" },
			{ "FILE_TRANSFER_UPLOAD38",  "Dropbox", "testuser4",  "admin",  "upload",  "txt",   "Test", "Policy Violation",  "Upload", "critical", "File,GDrive_2,Upload_Test_txt.log" },
			{ "FILE_TRANSFER_UPLOAD39",  "Dropbox", "testuser4",  "admin",  "upload",  "xls",   "Test", "Policy Violation",  "Upload", "critical", "File,GDrive_2,Upload_Test_xls.log" },
			{ "FILE_TRANSFER_UPLOAD40",  "Dropbox", "testuser4",  "admin",  "upload",  "zip",   "Test", "Policy Violation",  "Upload", "critical", "File,GDrive_2,Upload_Test_zip.log" },
			
		};
	}	
	
	
	@Test(groups ={"TEST"}, dataProvider = "_PolicyFileTransferDownload")
	public void verifyEmailFileTransferPolicyEnabledDownload(String policyName, String saasApps,  String testUserName,  String notifyEmailId, String transferType, String fileType, String fileName,  String activityType, String objectType, String severity, String logFile) throws Exception{
		Reporter.log("Validate User Upload the file/folder to folder with Policy Enabled", true);
		testUserName=testUserName+"@"+suiteData.getTenantDomainName();
		String expectedMsg="[ALERT]  attempted to "+transferType+" content:"+fileName.toLowerCase()+"."+fileType+" violating policy:"+policyName;
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
	public Object[][] _PolicyFileTransferDownload() {
		return new Object[][]{
			//Policy Name                 App           Test User      admin   Object Access Activity Access  Activity Type     Object Type               Severity
			{ "FILE_TRANSFER_DOWNLOAD1",     "Dropbox", "testuser4",  "admin",  "download",   "7z",       "Test",        "Policy Violation",  "Download", "critical",   "File,Dropbox_2,Download_Test_7z.log" },
			{ "FILE_TRANSFER_DOWNLOAD2",     "Dropbox", "testuser4",  "admin",  "download",   "avi",      "Test",        "Policy Violation",  "Download", "critical",   "File,Dropbox_2,Download_Test_avi.log" },
			{ "FILE_TRANSFER_DOWNLOAD3",     "Dropbox", "testuser4",  "admin",  "download",   "bin",      "Test",        "Policy Violation",  "Download", "critical",   "File,Dropbox_2,Download_Test_bin.log" },
			{ "FILE_TRANSFER_DOWNLOAD4",     "Dropbox", "testuser4",  "admin",  "download",   "bmp",      "Test",        "Policy Violation",  "Download", "critical",   "File,Dropbox_2,Download_Test_bmp.log" },
			{ "FILE_TRANSFER_DOWNLOAD5",     "Dropbox", "testuser4",  "admin",  "download",   "bz2",      "Test",        "Policy Violation",  "Download", "critical",   "File,Dropbox_2,Download_Test_bz2.log" },
			{ "FILE_TRANSFER_DOWNLOAD6",     "Dropbox", "testuser4",  "admin",  "download",   "cs",       "Test",        "Policy Violation",  "Download", "critical",   "File,Dropbox_2,Download_Test_cs.log" },
			{ "FILE_TRANSFER_DOWNLOAD7",     "Dropbox", "testuser4",  "admin",  "download",   "csv",      "Test",        "Policy Violation",  "Download", "critical",   "File,Dropbox_2,Download_Test_csv.log" },
			{ "FILE_TRANSFER_DOWNLOAD8",     "Dropbox", "testuser4",  "admin",  "download",   "dmg",      "Test",        "Policy Violation",  "Download", "critical",   "File,Dropbox_2,Download_Test_dmg.log" },
			{ "FILE_TRANSFER_DOWNLOAD9",     "Dropbox", "testuser4",  "admin",  "download",   "doc",      "Test",        "Policy Violation",  "Download", "critical",   "File,Dropbox_2,Download_Test_doc.log" },
			{ "FILE_TRANSFER_DOWNLOAD10",    "Dropbox", "testuser4",  "admin",  "download",   "docx",     "Test",        "Policy Violation",  "Download", "critical",   "File,Dropbox_2,Download_Test_docx.log" },
			{ "FILE_TRANSFER_DOWNLOAD11",    "Dropbox", "testuser4",  "admin",  "download",   "exe",      "Test",        "Policy Violation",  "Download", "critical",   "File,Dropbox_2,Download_Test_exe.log" },
			{ "FILE_TRANSFER_DOWNLOAD12",    "Dropbox", "testuser4",  "admin",  "download",   "flac",     "Test",        "Policy Violation",  "Download", "critical",   "File,Dropbox_2,Download_Test_flac.log" },
			{ "FILE_TRANSFER_DOWNLOAD13",    "Dropbox", "testuser4",  "admin",  "download",   "flv",      "Test",        "Policy Violation",  "Download", "critical",   "File,Dropbox_2,Download_Test_flv.log" },
			{ "FILE_TRANSFER_DOWNLOAD14",    "Dropbox", "testuser4",  "admin",  "download",   "gif",      "Test",        "Policy Violation",  "Download", "critical",   "File,Dropbox_2,Download_Test_gif.log" },
			{ "FILE_TRANSFER_DOWNLOAD15",    "Dropbox", "testuser4",  "admin",  "download",   "gz",       "Test",        "Policy Violation",  "Download", "critical",   "File,Dropbox_2,Download_Test_gz.log" },
			{ "FILE_TRANSFER_DOWNLOAD16",    "Dropbox", "testuser4",  "admin",  "download",   "html",     "Test",        "Policy Violation",  "Download", "critical",   "File,Dropbox_2,Download_Test_html.log" },
			{ "FILE_TRANSFER_DOWNLOAD17",    "Dropbox", "testuser4",  "admin",  "download",   "java",     "Test",        "Policy Violation",  "Download", "critical",   "File,Dropbox_2,Download_Test_java.log" },
			{ "FILE_TRANSFER_DOWNLOAD18",    "Dropbox", "testuser4",  "admin",  "download",   "jpeg",     "Test",        "Policy Violation",  "Download", "critical",   "File,Dropbox_2,Download_Test_jpeg.log" },
			{ "FILE_TRANSFER_DOWNLOAD19",    "Dropbox", "testuser4",  "admin",  "download",   "jpg",      "Test",        "Policy Violation",  "Download", "critical",   "File,Dropbox_2,Download_Test_jpg.log" },
			{ "FILE_TRANSFER_DOWNLOAD20",    "Dropbox", "testuser4",  "admin",  "download",   "json",     "Test",        "Policy Violation",  "Download", "critical",   "File,Dropbox_2,Download_Test_json.log" },
			{ "FILE_TRANSFER_DOWNLOAD21",    "Dropbox", "testuser4",  "admin",  "download",   "key",      "Test",        "Policy Violation",  "Download", "critical",   "File,Dropbox_2,Download_Test_key.log" },
			{ "FILE_TRANSFER_DOWNLOAD22",    "Dropbox", "testuser4",  "admin",  "download",   "mp3",      "Test",        "Policy Violation",  "Download", "critical",   "File,Dropbox_2,Download_Test_mp3.log" },
			{ "FILE_TRANSFER_DOWNLOAD23",    "Dropbox", "testuser4",  "admin",  "download",   "mp4",      "Test",        "Policy Violation",  "Download", "critical",   "File,Dropbox_2,Download_Test_mp4.log" },
			{ "FILE_TRANSFER_DOWNLOAD24",    "Dropbox", "testuser4",  "admin",  "download",   "numbers",  "Test",        "Policy Violation",  "Download", "critical",   "File,Dropbox_2,Download_Test_numbers.log" },
			{ "FILE_TRANSFER_DOWNLOAD25",    "Dropbox", "testuser4",  "admin",  "download",   "pdf",      "Test",        "Policy Violation",  "Download", "critical",   "File,Dropbox_2,Download_Test_pdf.log" },
			{ "FILE_TRANSFER_DOWNLOAD26",    "Dropbox", "testuser4",  "admin",  "download",   "pem",      "Test",        "Policy Violation",  "Download", "critical",   "File,Dropbox_2,Download_Test_pem.log" },
			{ "FILE_TRANSFER_DOWNLOAD27",    "Dropbox", "testuser4",  "admin",  "download",   "ppt",      "Test",        "Policy Violation",  "Download", "critical",   "File,Dropbox_2,Download_Test_ppt.log" },
			{ "FILE_TRANSFER_DOWNLOAD28",    "Dropbox", "testuser4",  "admin",  "download",   "txt",      "Test",        "Policy Violation",  "Download", "critical",   "File,Dropbox_2,Download_Test_txt.log" },
			{ "FILE_TRANSFER_DOWNLOAD29",    "Dropbox", "testuser4",  "admin",  "download",   "zip",      "Test",        "Policy Violation",  "Download", "critical",   "File,Dropbox_2,Download_Test_zip.log" },


		};
	}
	
	
	

	@Test(groups ={"DELETE_ALL"})
	public void deleteAllPolicies() throws Exception{
		Reporter.log("Validate User Upload the file/folder to folder with Policy Enabled", true);
		PolicyAccessEnforcement.deleteAllPolicy(suiteData,  requestHeader);
	}
	
}
