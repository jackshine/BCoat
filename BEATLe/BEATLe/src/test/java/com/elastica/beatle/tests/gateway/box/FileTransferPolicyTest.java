package com.elastica.beatle.tests.gateway.box;

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
 * @author usman
 */



public class FileTransferPolicyTest extends CommonConfiguration {

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
	Map <String, String>data= new HashMap<String, String>();
	
	@BeforeMethod(alwaysRun=true)
	public void beforMethod(Method method) throws Exception {
		deleteAllPolicies();
		data.clear();
		Reporter.log("--------------------------------------------", true);
		Reporter.log("Deleting all policies", true);
		Reporter.log("--------------------------------------------", true);
	}
	
	
	
	@Test(groups ={"TEST"}, dataProvider = "_policyFileTransferupload")
	public void verifyFileTransferPolicyEnabledUpload(String policyName, String saasApps,  String testUserName,  String notifyEmailId, String transferType, String fileType, String fileName,  String activityType, String objectType, String severity, String logFile) throws Exception{
		Reporter.log("Validate User Upload the file/folder to folder with Policy Enabled", true);
		testUserName=testUserName+"@"+suiteData.getTenantDomainName();
		String expectedMsg="[ALERT] "+testUserName+ " attempted to "+transferType+" content:"+fileName.toLowerCase()+"."+fileType+" violating policy:"+policyName;
		System.out.println(expectedMsg);
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
	public Object[][] _policyFileTransferupload() {
		return new Object[][]{
			//Policy Name                 App     Test User   admin     Object Access Activity Access      Activity Type     Object Type  Severity
			{ "FILE_TRANSFER_UPLOAD1",   "Box",   "testuser1",  "admin",  "upload",  "txt",    		"Test",        "Policy Violation",  "Upload", "critical", "Test,AFolder,upload_txt.log" },
			{ "FILE_TRANSFER_UPLOAD2",   "Box",   "testuser1",  "admin",  "upload",  "pdf",    		"Test",        "Policy Violation",  "Upload", "critical","Test,AFolder,upload_pdf.log" },
			{ "FILE_TRANSFER_UPLOAD3",   "Box",   "testuser1",  "admin",  "upload",  "rtf",    		"Test",        "Policy Violation",  "Upload", "critical", "Test,AFolder,upload_rtf.log" },
			{ "FILE_TRANSFER_UPLOAD4",   "Box",   "testuser1",  "admin",  "upload",  "doc",    		"Test",        "Policy Violation",  "Upload", "critical", "Test,AFolder,upload_doc.log" },
			{ "FILE_TRANSFER_UPLOAD5",   "Box",   "testuser1",  "admin",  "upload",  "docx",  		"Test",        "Policy Violation",  "Upload", "critical", "Test,AFolder,upload_docx.log" },
			{ "FILE_TRANSFER_UPLOAD6",   "Box",   "testuser1",  "admin",  "upload",  "xlsx",   		"Test",        "Policy Violation",  "Upload", "critical", "Test,AFolder,upload_xlsx.log" },
			{ "FILE_TRANSFER_UPLOAD7",   "Box",   "testuser1",  "admin",  "upload",  "xlsm",   		"Test",        "Policy Violation",  "Upload", "critical", "Test,AFolder,upload_xlsm.log" },
			{ "FILE_TRANSFER_UPLOAD8",   "Box",   "testuser1",  "admin",  "upload",  "java",   		"Test",        "Policy Violation",  "Upload", "critical", "Test,AFolder,upload_java.log" },
			{ "FILE_TRANSFER_UPLOAD9",   "Box",   "testuser1",  "admin",  "upload",  "c",      		"Test",        "Policy Violation",  "Upload", "critical", "Test,AFolder,upload_c.log" },
			{ "FILE_TRANSFER_UPLOAD10",  "Box",   "testuser1",  "admin",  "upload",  "xml",    		"Test",        "Policy Violation",  "Upload", "critical", "Test,AFolder,upload_xml.log" },
			{ "FILE_TRANSFER_UPLOAD11",  "Box",   "testuser1",  "admin",  "upload",  "rar",    		"Test",        "Policy Violation",  "Upload", "critical", "Test,AFolder,upload_rar.log" },
			{ "FILE_TRANSFER_UPLOAD12",  "Box",   "testuser1",  "admin",  "upload",  "html",   		"Test",        "Policy Violation",  "Upload", "critical","Test,AFolder,upload_html.log" },
			{ "FILE_TRANSFER_UPLOAD13",  "Box",   "testuser1",  "admin",  "upload",  "boxnote",		"Test",        "Policy Violation",  "Upload", "critical","Test,AFolder,upload_boxnote.log" },
			{ "FILE_TRANSFER_UPLOAD14",  "Box",   "testuser1",  "admin",  "upload",  "py",     		"Test",        "Policy Violation",  "Upload", "critical","Test,AFolder,upload_py.log" },
			{ "FILE_TRANSFER_UPLOAD15",  "Box",   "testuser1",  "admin",  "upload",  "properties",	"Test",        "Policy Violation",  "Upload", "critical","Test,AFolder,upload_properties.log" },
			{ "FILE_TRANSFER_UPLOAD16",  "Box",   "testuser1",  "admin",  "upload",  "pem",    		"Test",        "Policy Violation",  "Upload", "critical","Test,AFolder,upload_pem.log" },
			{ "FILE_TRANSFER_UPLOAD17",  "Box",   "testuser1",  "admin",  "upload",  "json",    	"Test",        "Policy Violation",  "Upload", "critical","Test,AFolder,upload_json.log" },
			{ "FILE_TRANSFER_UPLOAD18",  "Box",   "testuser1",  "admin",  "upload",  "datatable",   "Test",        "Policy Violation",  "Upload", "critical","Test,AFolder,upload_datatable.log" },
			{ "FILE_TRANSFER_UPLOAD19",  "Box",   "testuser1",  "admin",  "upload",  "zip",   		"Test",        "Policy Violation",  "Upload", "critical","Test,AFolder,upload_zip.log" },
			{ "FILE_TRANSFER_UPLOAD20",  "Box",   "testuser1",  "admin",  "upload",  "mp4",  		 "Test",        "Policy Violation",  "Upload", "critical","Test,AFolder,upload_mp4.log" },
			{ "FILE_TRANSFER_UPLOAD21",  "Box",   "testuser1",  "admin",  "upload",  "png",   		"Test",        "Policy Violation",  "Upload", "critical","Test,AFolder,upload_png.log" },
			{ "FILE_TRANSFER_UPLOAD22",  "Box",   "testuser1",  "admin",  "upload",  "vb",   		"Test",        "Policy Violation",  "Upload", "critical","Test,AFolder,upload_vb.log" },
			{ "FILE_TRANSFER_UPLOAD23",  "Box",   "testuser1",  "admin",  "upload",  "xls",   		"Test",        "Policy Violation",  "Upload", "critical","Test,AFolder,upload_xls.log" },
			{ "FILE_TRANSFER_UPLOAD24",  "Box",   "testuser1",  "admin",  "upload",  "tbz2",  		 "Test",        "Policy Violation",  "Upload", "critical","Test,AFolder,upload_tbz2.log" },
			{ "FILE_TRANSFER_UPLOAD25",  "Box",   "testuser1",  "admin",  "upload",  "tgz",   		"Test",        "Policy Violation",  "Upload", "critical","Test,AFolder,upload_tgz.log" },
			{ "FILE_TRANSFER_UPLOAD26",  "Box",   "testuser1",  "admin",  "upload",  "7z",   		"Test",        "Policy Violation",  "Upload", "critical","Test,AFolder,upload_7z.log" },
			{ "FILE_TRANSFER_UPLOAD27",  "Box",   "testuser1",  "admin",  "upload",  "base64",   	"Test",        "Policy Violation",  "Upload", "critical","Test,AFolder,upload_base64.log" },
			{ "FILE_TRANSFER_UPLOAD28",  "Box",   "testuser1",  "admin",  "upload",  "exe",   		"Test",        "Policy Violation",  "Upload", "critical","Test,AFolder,upload_exe.log" },
			{ "FILE_TRANSFER_UPLOAD29",  "Box",   "testuser1",  "admin",  "upload",  "jpg",   		"Test",        "Policy Violation",  "Upload", "critical","Test,AFolder,upload_jpg.log" },
			{ "FILE_TRANSFER_UPLOAD30",  "Box",   "testuser1",  "admin",  "upload",  "js",   		"Test",        "Policy Violation",  "Upload", "critical","Test,AFolder,upload_js.log" },
			//{ "FILE_TRANSFER_UPLOAD31",  "Box",   "testuser1",  "admin",  "upload",  "mp3",   		"Test",        "Policy Violation",  "Upload", "critical","Test,AFolder,upload_mp3.log" },
			{ "FILE_TRANSFER_UPLOAD32",  "Box",   "testuser1",  "admin",  "upload",  "ods",   		"Test",        "Policy Violation",  "Upload", "critical","Test,AFolder,upload_ods.log" },
			{ "FILE_TRANSFER_UPLOAD33",  "Box",   "testuser1",  "admin",  "upload",  "odt",   		"Test",        "Policy Violation",  "Upload", "critical","Test,AFolder,upload_odt.log" },
			//{ "FILE_TRANSFER_UPLOAD22",  "Box",   "testuser1",  "admin",  "upload",  "__ALL_EL__",  "ANY",         "Policy Violation",  "Upload", "critical","Test,AFolder,upload_txt.log" },
			
			//{ "FILE_TRANSFER_DOWNLOAD13",  "Box",   "testuser1",  "admin",  "download", "__ALL_EL__",   "ANY",      "Policy Violation",  "Download", "critical","TestFile,AllFiles,download_rar.log" }
			
		};
	}
	
	
	
	
	@Test(groups ={"TEST"}, dataProvider = "_policyFileTransferDownload")
	public void verifyFileTransferPolicyEnabled(String policyName, String saasApps,  String testUserName,  String notifyEmailId, String transferType, String fileType, String fileName,  String activityType, String objectType, String severity, String logFile) throws Exception{
		Reporter.log("Validate User Upload the file/folder to folder with Policy Enabled", true);
		testUserName=testUserName+"@"+suiteData.getTenantDomainName();
		String expectedMsg="[ALERT] "+testUserName+ " attempted to "+transferType+" content:"+fileName.toLowerCase()+"."+fileType+" violating policy:"+policyName;
		policyDataMap.clear();
		policyDataMap.put(GatewayTestConstants.POLICY_NAME, policyName);
		policyDataMap.put(GatewayTestConstants.APPLICATIONS, saasApps);
		policyDataMap.put(GatewayTestConstants.TARGET_USER, testUserName);
		policyDataMap.put(GatewayTestConstants.NOTIFY_EMAILID, notifyEmailId+"@"+suiteData.getTenantDomainName());
		policyDataMap.put(GatewayTestConstants.TRANSFER_TYPE, transferType);
		policyDataMap.put(GatewayTestConstants.FILE_TYPE, fileType);
		policyDataMap.put(GatewayTestConstants.FILE_NAME, fileName);
		
		PolicyAccessEnforcement.fileTransferPolicyCreateEnable(suiteData, requestHeader, policyDataMap);
		replayLogsWithDelay(logFile);
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
			//Policy Name                 App     Test User   admin     Object Access Activity Access      Activity Type     Object Type  Severity
			{ "FILE_TRANSFER_DOWNLOAD1",   "Box",   "testuser1",  "admin",  "download",  "7z",         "Test",      "Policy Violation",  "Download", "critical","TestFile,AllFiles,download_7z.log" },
			//{ "FILE_TRANSFER_DOWNLOAD2",   "Box",   "testuser1",  "admin",  "download",  "boxnote",    "Test", "Policy Violation",  "Download", "critical","TestFile,AllFiles,download_boxnote.log" },
			//{ "FILE_TRANSFER_DOWNLOAD3",   "Box",   "testuser1",  "admin",  "download",  "doc",        "Test",      "Policy Violation",  "Download", "critical","TestFile,AllFiles,download_docc.log" },
			{ "FILE_TRANSFER_DOWNLOAD4",   "Box",   "testuser1",  "admin",  "download",  "docx",    "Test",      "Policy Violation",  "Download", "critical","TestFile,AllFiles,download_docx.log" },
			//{ "FILE_TRANSFER_DOWNLOAD5",   "Box",   "testuser1",  "admin",  "download",  "exe",   "Test",      "Policy Violation",  "Download", "critical","TestFile,AllFiles,download_exe.log" },
			{ "FILE_TRANSFER_DOWNLOAD6",   "Box",   "testuser1",  "admin",  "download",  "java",   "Test",      "Policy Violation",  "Download", "critical","TestFile,AllFiles,download_java.log" },
			{ "FILE_TRANSFER_DOWNLOAD7",   "Box",   "testuser1",  "admin",  "download",  "ods",   "Test",      "Policy Violation",  "Download", "critical","TestFile,AllFiles,download_ods.log" },
			//{ "FILE_TRANSFER_DOWNLOAD7",   "Box",   "testuser1",  "admin",  "download",  "odt",   "Test",      "Policy Violation",  "Download", "critical","TestFile,AllFiles,download_odt.log" },
			{ "FILE_TRANSFER_DOWNLOAD8",   "Box",   "testuser1",  "admin",  "download",  "pdf",   "Test",      "Policy Violation",  "Download", "critical","TestFile,AllFiles,download_pdf.log" },
			{ "FILE_TRANSFER_DOWNLOAD9",   "Box",   "testuser1",  "admin",  "download",  "rar",    "Test",      "Policy Violation",  "Download", "critical","TestFile,AllFiles,download_rar.log" },
			{ "FILE_TRANSFER_DOWNLOAD10",  "Box",   "testuser1",  "admin",  "download", "rtf",   "Test",      "Policy Violation",  "Download", "critical","TestFile,AllFiles,download_rtf.log" },
			{ "FILE_TRANSFER_DOWNLOAD11",  "Box",   "testuser1",  "admin",  "download", "tgz",    "Test",      "Policy Violation",  "Download", "critical","TestFile,AllFiles,download_tgz.log" },
			{ "FILE_TRANSFER_DOWNLOAD12",  "Box",   "testuser1",  "admin",  "download", "txt",    "Test",      "Policy Violation",  "Download", "critical","TestFile,AllFiles,download_txt.log" },
			{ "FILE_TRANSFER_DOWNLOAD12",  "Box",   "testuser1",  "admin",  "download", "xls",    "Test",      "Policy Violation",  "Download", "critical","TestFile,AllFiles,download_xls.log" },
			{ "FILE_TRANSFER_DOWNLOAD12",  "Box",   "testuser1",  "admin",  "download", "xlsm",    "Test",      "Policy Violation",  "Download", "critical","TestFile,AllFiles,download_xlsm.log" },
			{ "FILE_TRANSFER_DOWNLOAD12",  "Box",   "testuser1",  "admin",  "download", "xlsx",    "Test",      "Policy Violation",  "Download", "critical","TestFile,AllFiles,download_xlsx.log" },
			{ "FILE_TRANSFER_DOWNLOAD12",  "Box",   "testuser1",  "admin",  "download", "xml",    "Test",      "Policy Violation",  "Download", "critical","TestFile,AllFiles,download_xml.log" },
			{ "FILE_TRANSFER_DOWNLOAD12",  "Box",   "testuser1",  "admin",  "download", "zip",    "Test",      "Policy Violation",  "Download", "critical","TestFile,AllFiles,download_zip.log" },
			
			//{ "FILE_TRANSFER_DOWNLOAD13",  "Box",   "testuser1",  "admin",  "download", "__ALL_EL__",   "ANY",      "Policy Violation",  "Download", "critical","TestFile,AllFiles,download_rar.log" }
			
		};
	}
	
	@Test(groups ={"TEST","DEV"}, dataProvider = "_policyFileSizeTransfer")
	public void verifyFileTransferPolicyWithFileSizeEnabled(String policyName, String saasApps, String fileUploaded, String testUserName,  String notifyEmailId, String transferType, String fileType, String fileName, String fileSizeLargerThan, String fileSizeSmallerThan, String activityType, String objectType, String severity, String logFile) throws Exception{
		Reporter.log("Validate User Upload the file/folder to folder with Policy Enabled", true);
		testUserName=testUserName+"@"+suiteData.getTenantDomainName();
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
		String expectedMsg="[ALERT] "+testUserName+ " attempted to upload content:"+fileUploaded+" violating policy:"+policyName;
		PolicyAccessEnforcement.fileTransferPolicyWithFileSizeCreateEnable(suiteData, requestHeader, policyDataMap);
		replayLogsWithDelay(logFile);
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
	public Object[][] _policyFileSizeTransfer() {
		return new Object[][]{
			//Policy Name                 App     Test User   admin     Object Access Activity Access      Activity Type     Object Type  Severity
			
			{ "FILE_TRANSFER_UPLOAD_SIZE_LIMIT1",  "Box",  "testlessthan1mb.pages", "testuser1",  "admin",  "upload",  "__ALL_EL__",   "ANY", "0", "1",    "Policy Violation",  "Upload", "critical","AllFiles,Box,uploadLess1MBFile.log" },
			{ "FILE_TRANSFER_UPLOAD_SIZE_LIMIT2",  "Box",  "testgreatethan1mb.pages", "testuser1",  "admin",  "upload",  "__ALL_EL__",   "ANY", "1", "2",    "Policy Violation",  "Upload", "critical","AllFiles,Box,uploadLarger1MBFile.log" },
			{ "FILE_TRANSFER_UPLOAD_SIZE_LIMIT3",  "Box",  "modifypermissions.gif", "testuser1",  "admin",  "upload",  "__ALL_EL__",   "ANY", "1", "2",    "Policy Violation",  "Upload", "critical","AllFiles,Box,uploadLarger2MBFile.log" },
			{ "FILE_TRANSFER_UPLOAD_SIZE_LIMIT4",  "Box",  "testlessthan1mb.pages", "testuser1",  "admin",  "upload",  "__ALL_EL__",   "testlessthan1mb.pages", "0", "1",    "Policy Violation",  "Upload", "critical","AllFiles,Box,uploadLess1MBFile.log" },
			{ "FILE_TRANSFER_UPLOAD_SIZE_LIMIT5",  "Box",  "testgreatethan1mb.pages", "testuser1",  "admin",  "upload",  "__ALL_EL__",   "testgreatethan1mb.pages", "1", "2",    "Policy Violation",  "Upload", "critical","AllFiles,Box,uploadLarger1MBFile.log" },
			{ "FILE_TRANSFER_UPLOAD_SIZE_LIMIT6",  "Box",  "modifypermissions.gif", "testuser1",  "admin",  "upload",  "__ALL_EL__",   "modifypermissions.gif", "1", "2",    "Policy Violation",  "Upload", "critical","AllFiles,Box,uploadLarger2MBFile.log" },
			{ "FILE_TRANSFER_UPLOAD_SIZE_LIMIT7",  "Box",  "testlessthan1mb.pages", "testuser1",  "admin",  "upload",  "pages",   "testlessthan1mb.pages", "0", "1",    "Policy Violation",  "Upload", "critical","AllFiles,Box,uploadLess1MBFile.log" },
			{ "FILE_TRANSFER_UPLOAD_SIZE_LIMIT8",  "Box",  "modifypermissions.gif", "testuser1",  "admin",  "upload",  "gif",   "modifypermissions.gif", "1", "2",    "Policy Violation",  "Upload", "critical","AllFiles,Box,uploadLarger2MBFile.log" },
			{ "FILE_TRANSFER_UPLOAD_SIZE_LIMIT9",  "Box",  "testlessthan1mb.pages", "testuser1",  "admin",  "upload",  "pages",   "ANY", "0", "1",    "Policy Violation",  "Upload", "critical","AllFiles,Box,uploadLess1MBFile.log" },
			
		/*	{ "FILE_TRANSFER_DOWNLOAD1",  "Box",   "testuser1",  "admin",  "download",  "txt",    "Test",    "1", "2",  "Policy Violation",  "Download", "critical" },
			{ "FILE_TRANSFER_DOWNLOAD2",  "Box",   "testuser1",  "admin",  "download",  "pdf",    "Test",    "1", "2",  "Policy Violation",  "Download", "critical" },
			{ "FILE_TRANSFER_DOWNLOAD3",  "Box",   "testuser1",  "admin",  "download",  "rtf",    "Test",    "1", "2",  "Policy Violation",  "Download", "critical" },
			{ "FILE_TRANSFER_DOWNLOAD4",  "Box",   "testuser1",  "admin",  "download",  "doc",    "Test",    "1", "2",  "Policy Violation",  "Download", "critical" },
			{ "FILE_TRANSFER_DOWNLOAD5",  "Box",   "testuser1",  "admin",  "download",  "docx",   "Test",    "1", "2",  "Policy Violation",  "Download", "critical" },
			{ "FILE_TRANSFER_DOWNLOAD6",  "Box",   "testuser1",  "admin",  "download",  "xlsx",   "Test",    "1", "2",  "Policy Violation",  "Download", "critical" },
			{ "FILE_TRANSFER_DOWNLOAD7",  "Box",   "testuser1",  "admin",  "download",  "__ALL_EL__",   "ANY","1", "2", "Policy Violation",  "Download", "critical" }*/
		};
	}
	

	@Test(groups ={"DELETE_ALL"})
	public void deleteAllPolicies() throws Exception{
		Reporter.log("Validate User Upload the file/folder to folder with Policy Enabled", true);
		PolicyAccessEnforcement.deleteAllPolicy(suiteData,  requestHeader);
	}
	
}
