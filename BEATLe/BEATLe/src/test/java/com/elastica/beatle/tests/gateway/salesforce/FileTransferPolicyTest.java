package com.elastica.beatle.tests.gateway.salesforce;

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
		//PolicyAccessEnforcement.deletePolicy(suiteData, requestHeader, policyDataMap);
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
			//{ "FILE_TRANSFER_UPLOAD1",   "Box",   "testuser1",  "admin",  "upload",  "txt",    		"Test",        "Policy Violation",  "Upload", "critical", "Test,AFolder,upload_txt.log" },
			{ "SALESFORCE_FILE_TRANSFER_UPLOAD1",   "Salesforce",   "testuser1",  "admin",  "upload",  "pdf",    		"Test",        "Policy Violation",  "Upload", "critical","File,Salesforce,upload.log" },
			{ "SALESFORCE_FILE_TRANSFER_UPLOAD2",   "Salesforce",   "testuser1",  "admin",  "upload",  "pdf",    		"Test",        "Policy Violation",  "Upload", "critical","Salesforce,Files,upload_Sample_pdf.log" },
			{ "SALESFORCE_FILE_TRANSFER_UPLOAD3",   "Salesforce",   "testuser1",  "admin",  "upload",  "doc",    		"Test",        "Policy Violation",  "Upload", "critical","Salesforce,Files,upload_Test_doc.log" },
			{ "SALESFORCE_FILE_TRANSFER_UPLOAD4",   "Salesforce",   "testuser1",  "admin",  "upload",  "zip",    		"Test",        "Policy Violation",  "Upload", "critical","Salesforce,Files,upload_Test_zip.log" },
			{ "SALESFORCE_FILE_TRANSFER_UPLOAD5",   "Salesforce",   "testuser1",  "admin",  "upload",  "xml",    		"Test",        "Policy Violation",  "Upload", "critical","Salesforce,Files,upload_Test_xml.log" },
			{ "SALESFORCE_FILE_TRANSFER_UPLOAD6",   "Salesforce",   "testuser1",  "admin",  "upload",  "xlsx",    		"Test",        "Policy Violation",  "Upload", "critical","Salesforce,Files,upload_Test_xlsx.log" },
			{ "SALESFORCE_FILE_TRANSFER_UPLOAD7",   "Salesforce",   "testuser1",  "admin",  "upload",  "xlsm",    		"Test",        "Policy Violation",  "Upload", "critical","Salesforce,Files,upload_Test_xlsm.log" },
			{ "SALESFORCE_FILE_TRANSFER_UPLOAD8",   "Salesforce",   "testuser1",  "admin",  "upload",  "xls",    		"Test",        "Policy Violation",  "Upload", "critical","Salesforce,Files,upload_Test_xls.log" },
			{ "SALESFORCE_FILE_TRANSFER_UPLOAD9",   "Salesforce",   "testuser1",  "admin",  "upload",  "vb",    		"Test",        "Policy Violation",  "Upload", "critical","Salesforce,Files,upload_Test_vb.log" },
			{ "SALESFORCE_FILE_TRANSFER_UPLOAD10",   "Salesforce",   "testuser1",  "admin",  "upload",  "txt",    		"Test",        "Policy Violation",  "Upload", "critical","Salesforce,Files,upload_Test_txt.log" },
			{ "SALESFORCE_FILE_TRANSFER_UPLOAD11",   "Salesforce",   "testuser1",  "admin",  "upload",  "tgz",    		"Test",        "Policy Violation",  "Upload", "critical","Salesforce,Files,upload_Test_tgz.log" },
			{ "SALESFORCE_FILE_TRANSFER_UPLOAD12",   "Salesforce",   "testuser1",  "admin",  "upload",  "tbz2",    		"Test",        "Policy Violation",  "Upload", "critical","Salesforce,Files,upload_Test_tbz2.log" },
			{ "SALESFORCE_FILE_TRANSFER_UPLOAD13",   "Salesforce",   "testuser1",  "admin",  "upload",  "rtf",    		"Test",        "Policy Violation",  "Upload", "critical","Salesforce,Files,upload_Test_rtf.log" },
			{ "SALESFORCE_FILE_TRANSFER_UPLOAD14",   "Salesforce",   "testuser1",  "admin",  "upload",  "rar",    		"Test",        "Policy Violation",  "Upload", "critical","Salesforce,Files,upload_Test_rar.log" },
			{ "SALESFORCE_FILE_TRANSFER_UPLOAD15",   "Salesforce",   "testuser1",  "admin",  "upload",  "py",    		"Test",        "Policy Violation",  "Upload", "critical","Salesforce,Files,upload_Test_py.log" },
			{ "SALESFORCE_FILE_TRANSFER_UPLOAD16",   "Salesforce",   "testuser1",  "admin",  "upload",  "properties",    		"Test",        "Policy Violation",  "Upload", "critical","Salesforce,Files,upload_Test_properties.log" },
			{ "SALESFORCE_FILE_TRANSFER_UPLOAD17",   "Salesforce",   "testuser1",  "admin",  "upload",  "png",    		"Test",        "Policy Violation",  "Upload", "critical","Salesforce,Files,upload_Test_png.log" },
			{ "SALESFORCE_FILE_TRANSFER_UPLOAD18",   "Salesforce",   "testuser1",  "admin",  "upload",  "pem",    		"Test",        "Policy Violation",  "Upload", "critical","Salesforce,Files,upload_Test_pem.log" },
			{ "SALESFORCE_FILE_TRANSFER_UPLOAD19",   "Salesforce",   "testuser1",  "admin",  "upload",  "odt",    		"Test",        "Policy Violation",  "Upload", "critical","Salesforce,Files,upload_Test_odt.log" },
			{ "SALESFORCE_FILE_TRANSFER_UPLOAD20",   "Salesforce",   "testuser1",  "admin",  "upload",  "ods",    		"Test",        "Policy Violation",  "Upload", "critical","Salesforce,Files,upload_Test_ods.log" },
			{ "SALESFORCE_FILE_TRANSFER_UPLOAD21",   "Salesforce",   "testuser1",  "admin",  "upload",  "mp4",    		"Test",        "Policy Violation",  "Upload", "critical","Salesforce,Files,upload_Test_mp4.log" },			{ "SALESFORCE_FILE_TRANSFER_UPLOAD22",   "Salesforce",   "testuser1",  "admin",  "upload",  "mp3",    		"Test",        "Policy Violation",  "Upload", "critical","Salesforce,Files,upload_Test_mp3.log" },
			{ "SALESFORCE_FILE_TRANSFER_UPLOAD23",   "Salesforce",   "testuser1",  "admin",  "upload",  "json",    		"Test",        "Policy Violation",  "Upload", "critical","Salesforce,Files,upload_Test_json.log" },
			{ "SALESFORCE_FILE_TRANSFER_UPLOAD24",   "Salesforce",   "testuser1",  "admin",  "upload",  "java",    		"Test",        "Policy Violation",  "Upload", "critical","Salesforce,Files,upload_Test_java.log" },
			{ "SALESFORCE_FILE_TRANSFER_UPLOAD25",   "Salesforce",   "testuser1",  "admin",  "upload",  "js",    		"Test",        "Policy Violation",  "Upload", "critical","Salesforce,Files,upload_Test_js.log" },
			{ "SALESFORCE_FILE_TRANSFER_UPLOAD26",   "Salesforce",   "testuser1",  "admin",  "upload",  "jpg",    		"Test",        "Policy Violation",  "Upload", "critical","Salesforce,Files,upload_Test_jpg.log" },
			{ "SALESFORCE_FILE_TRANSFER_UPLOAD27",   "Salesforce",   "testuser1",  "admin",  "upload",  "html",    		"Test",        "Policy Violation",  "Upload", "critical","Salesforce,Files,upload_Test_html.log" },
			{ "SALESFORCE_FILE_TRANSFER_UPLOAD28",   "Salesforce",   "testuser1",  "admin",  "upload",  "exe",    		"Test",        "Policy Violation",  "Upload", "critical","Salesforce,Files,upload_Test_exe.log" },
			{ "SALESFORCE_FILE_TRANSFER_UPLOAD29",   "Salesforce",   "testuser1",  "admin",  "upload",  "docx",    		"Test",        "Policy Violation",  "Upload", "critical","Salesforce,Files,upload_Test_docx.log" },
			{ "SALESFORCE_FILE_TRANSFER_UPLOAD30",   "Salesforce",   "testuser1",  "admin",  "upload",  "datatable",    		"Test",        "Policy Violation",  "Upload", "critical","Salesforce,Files,upload_Test_datatable.log" },
			{ "SALESFORCE_FILE_TRANSFER_UPLOAD31",   "Salesforce",   "testuser1",  "admin",  "upload",  "c",    		"Test",        "Policy Violation",  "Upload", "critical","Salesforce,Files,upload_Test_c.log" },
			{ "SALESFORCE_FILE_TRANSFER_UPLOAD32",   "Salesforce",   "testuser1",  "admin",  "upload",  "boxnote",    		"Test",        "Policy Violation",  "Upload", "critical","Salesforce,Files,upload_Test_boxnote.log" },
			{ "SALESFORCE_FILE_TRANSFER_UPLOAD33",   "Salesforce",   "testuser1",  "admin",  "upload",  "base64",    		"Test",        "Policy Violation",  "Upload", "critical","Salesforce,Files,upload_Test_base64.log" },
			{ "SALESFORCE_FILE_TRANSFER_UPLOAD34",   "Salesforce",   "testuser1",  "admin",  "upload",  "7z",    		"Test",        "Policy Violation",  "Upload", "critical","Salesforce,Files,upload_Test_7z.log" }
			
		};
	}
	
	
	
	
	
	
	
	
	
	
	
	/*
	
	
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
		PolicyAccessEnforcement.deletePolicy(suiteData, requestHeader, policyDataMap);
		PolicyAccessEnforcement.fileTransferPolicyCreateEnable(suiteData, requestHeader, policyDataMap);
		replayLogs(logFile);
		PolicyAccessEnforcement.deletePolicy(suiteData, requestHeader, policyDataMap);
		fsr=fetchElasticSearchLogs();         		//fetch the logs from ES
		messages=processLogs(fsr);													//Process the messages from logs
		Assert.assertTrue(validateMessgaeBody(messages, expectedMsg), "Logs does not match" );    //Validate the messages
		Assert.assertTrue(validatePolicyDetails(messages, policyName), "Policy does not match" ); 
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
	*/
	
	@Test(groups ={"POLICY","DEV"}, dataProvider = "_policyFileSizeTransfer")
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
	
	
	
	
	
	
	
	@Test(groups ={"POLICY","DEV"}, dataProvider = "_policyTransferWithRisk")
	public void verifyFileTransferPolicyWithRiskEnabled(String policyName, String saasApps,  String testUserName,  String notifyEmailId, String transferType, String fileType, String fileName, String vulnarabilityType, String contentIq, String activityType, String objectType, String severity) throws Exception{
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
		policyDataMap.put(GatewayTestConstants.VULNERABILITY_TYPE, vulnarabilityType);
		policyDataMap.put(GatewayTestConstants.CONTENT_IQ, contentIq);
		
		
		PolicyAccessEnforcement.fileTransferPolicyWithRiskContentIqCreateEnable(suiteData, requestHeader, policyDataMap);
		//fileUpload.toBox(fileName);
		//fsr=fetchElasticSearchLogs(activityType, severity);         		//fetch the logs from ES
		//messages=processLogs(fsr);													//Process the messages from logs
		//Assert.assertTrue(validateMessgaeBody(messages, expectedMsg), "Logs does not match" );    //Validate the messages
	
	}
	
	@DataProvider
	public Object[][] _policyTransferWithRisk() {
		return new Object[][]{
			//Policy Name                 App     Test User   admin     Object Access Activity Access      Activity Type     Object Type  Severity
			{ "FILE_TRANSFER_UPLOAD1",  "Box",   "testuser1",  "admin",  "upload",  "txt",    "Test",    "PII", null,    "Policy Violation",  "Upload", "critical" },
			{ "FILE_TRANSFER_UPLOAD2",  "Box",   "testuser1",  "admin",  "upload",  "pdf",    "Test",     "PII", null,   "Policy Violation",  "Upload", "critical" },
			{ "FILE_TRANSFER_UPLOAD3",  "Box",   "testuser1",  "admin",  "upload",  "rtf",    "Test",      "PII", null,  "Policy Violation",  "Upload", "critical" },
			{ "FILE_TRANSFER_UPLOAD4",  "Box",   "testuser1",  "admin",  "upload",  "doc",    "Test",      "PII", null,  "Policy Violation",  "Upload", "critical" },
			{ "FILE_TRANSFER_UPLOAD5",  "Box",   "testuser1",  "admin",  "upload",  "docx",   "Test",     "PII", null,   "Policy Violation",  "Upload", "critical" },
			{ "FILE_TRANSFER_UPLOAD6",  "Box",   "testuser1",  "admin",  "upload",  "xlsx",   "Test",     "PII", null,   "Policy Violation",  "Upload", "critical" },
			{ "FILE_TRANSFER_UPLOAD7",  "Box",   "testuser1",  "admin",  "upload",  "__ALL_EL__",   "ANY",   "PII", null,     "Policy Violation",  "Upload", "critical" },
			
			{ "FILE_TRANSFER_DOWNLOAD1",  "Box",   "testuser1",  "admin",  "download",  "txt",    "Test",  "PII", null,    "Policy Violation",  "Download", "critical" },
			{ "FILE_TRANSFER_DOWNLOAD2",  "Box",   "testuser1",  "admin",  "download",  "pdf",    "Test",   "PII", null,   "Policy Violation",  "Download", "critical" },
			{ "FILE_TRANSFER_DOWNLOAD3",  "Box",   "testuser1",  "admin",  "download",  "rtf",    "Test",   "PII", null,   "Policy Violation",  "Download", "critical" },
			{ "FILE_TRANSFER_DOWNLOAD4",  "Box",   "testuser1",  "admin",  "download",  "doc",    "Test",   "PII", null,   "Policy Violation",  "Download", "critical" },
			{ "FILE_TRANSFER_DOWNLOAD5",  "Box",   "testuser1",  "admin",  "download",  "docx",   "Test",   "PII", null,   "Policy Violation",  "Download", "critical" },
			{ "FILE_TRANSFER_DOWNLOAD6",  "Box",   "testuser1",  "admin",  "download",  "xlsx",   "Test",    "PII", null,  "Policy Violation",  "Download", "critical" },
			{ "FILE_TRANSFER_DOWNLOAD7",  "Box",   "testuser1",  "admin",  "download",  "__ALL_EL__",   "ANY",  "1", "2",    "Policy Violation",  "Download", "critical" }
		};
	}
	
	

	@Test(groups ={"DELETE_ALL"})
	public void deleteAllPolicies() throws Exception{
		Reporter.log("Validate User Upload the file/folder to folder with Policy Enabled", true);
		PolicyAccessEnforcement.deleteAllPolicy(suiteData,  requestHeader);
	}
	
}
