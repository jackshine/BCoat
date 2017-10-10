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
import com.elastica.beatle.replayTool.EPDV1SampleTest;


/*******************Author**************
 * 
 * @author Afjal
 */

public class DropboxFileSharingPolicyTest extends CommonConfiguration {
	String currentTimeInJodaFormat;
	GWForensicSearchResults fsr;
	ArrayList<String> messages = new ArrayList<String>();
	ArrayList<String> objectTypeList = new ArrayList<String>();
	ArrayList<String> objectNameList = new ArrayList<String>();
	ArrayList<String> severityList = new ArrayList<String>();
	LogValidator logValidator;
	String userLitral="User";
	Map<String, String>policyDataMap= new HashMap<String, String>(); 
	ProtectFunctions protectFunctions = new ProtectFunctions();
	Map <String, String> data = new HashMap<String, String>();
	
	/*****************************************
	 * 
	 * @param policyName
	 * @param saasApps
	 * @param sharedBy
	 * @param shareWith
	 * @param notifyEmailId
	 * @param fileType
	 * @param fileName
	 * @param activityType
	 * @param objectType
	 * @param severity
	 * @throws Exception
	 */
	
	@BeforeMethod(alwaysRun=true)
	public void beforMethod(Method method) throws Exception {
		deleteAllPolicies();
		Reporter.log("--------------------------------------------", true);
		Reporter.log("Deleting all policies", true);
		Reporter.log("--------------------------------------------", true);
	}
	
	@Test(groups ={"TEST"},dataProvider = "_policySharingFile")
	public void verifyFileSharingPolicyLinkEnabled(String policyName, String saasApps,  String sharedBy,   String shareWith, String notifyEmailId, String fileType, String fileName,  String activityType, String objectType, String severity, String logFile) throws Exception{
		Reporter.log("Validate User Upload the file/folder to folder with Policy Enabled", true);
		testUserName=sharedBy+"@"+suiteData.getTenantDomainName();
		String expectedMsg="[ALERT] "+testUserName+ " attempted to share content:"+fileName.toLowerCase()+"."+fileType+"with external user(s):mohd afjal &lt;mohd.afjal@elastica.co&gt;, mohd.afjal@elastica.co violating policy:"+policyName;
		policyDataMap.clear();
		policyDataMap.put(GatewayTestConstants.POLICY_NAME, policyName);
		policyDataMap.put(GatewayTestConstants.APPLICATIONS, saasApps);
		policyDataMap.put(GatewayTestConstants.SHARED_BY,testUserName );
		policyDataMap.put(GatewayTestConstants.NOTIFY_EMAILID, notifyEmailId+"@"+suiteData.getTenantDomainName());
		policyDataMap.put(GatewayTestConstants.SHARE_WITH, shareWith);
		policyDataMap.put(GatewayTestConstants.FILE_TYPE, fileType);
		policyDataMap.put(GatewayTestConstants.FILE_NAME, fileName);
		PolicyAccessEnforcement.fileSharingPolicyCreateEnable(suiteData, requestHeader, policyDataMap);
		replayLogs(logFile);
		PolicyAccessEnforcement.deletePolicy(suiteData, requestHeader, policyDataMap);

		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		data.put("policy_type", "FileSharingGateway");
		data.put("policy_action", "ALERT");
		data.put("action_taken", "block,");
		data.put("_PolicyViolated", policyName);
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), "Logs does not match" ); 

		
	}
	
	@DataProvider
	public Object[][] _policySharingFile() {
		return new Object[][]{
			//Policy Name        App     Shared by  Share with  						admin     File Type    File Name      			Activity Type
			
			{ "FILE_SHARING1",   "Dropbox",   "admin",  "__ALL_EL__", 	"admin",   "odg",    	"test", "Policy Violation",  "File", "critical", "File,Dropbox_2,Share_Test_odg.log" },
//			{ "FILE_SHARING2",   "Dropbox",   "admin",  "__ALL_EL__",   "admin",   "odp",    	"test", "Policy Violation",  "File", "critical", "File,Dropbox_2,Share_Test_odp.log"},
//		    { "FILE_SHARING3",   "Dropbox",   "admin",  "__ALL_EL__", 	"admin",   "ods",    	"test", "Policy Violation",  "File", "critical", "File,Dropbox_2,Share_Test_ods.log" },
//		    { "FILE_SHARING4",   "Dropbox",   "admin",  "__ALL_EL__", 	"admin",   "odt",   	"test", "Policy Violation",  "File", "critical", "File,Dropbox_2,Share_Test_odt.log" },
//			{ "FILE_SHARING5",   "Dropbox",   "admin",  "__ALL_EL__", 	"admin",   "7z",    	"test", "Policy Violation",  "File", "critical", "File,Dropbox_2,Share_Test_7z.log" },
//			{ "FILE_SHARING6",   "Dropbox",   "admin",  "__ALL_EL__", 	"admin",   "avi",    	"test", "Policy Violation",  "File", "critical", "File,Dropbox_2,Share_Test_avi.log" },
//			{ "FILE_SHARING7",   "Dropbox",   "admin",  "__ALL_EL__", 	"admin",   "bin",    	"test", "Policy Violation",  "File", "critical", "File,Dropbox_2,Share_Test_bin.log" },
//			{ "FILE_SHARING8",   "Dropbox",   "admin",  "__ALL_EL__", 	"admin",   "bmp",    	"test", "Policy Violation",  "File", "critical", "File,Dropbox_2,Share_Test_bmp.log" },
//			{ "FILE_SHARING9",   "Dropbox",   "admin",  "__ALL_EL__", 	"admin",   "bz2",    	"test", "Policy Violation",  "File", "critical", "File,Dropbox_2,Share_Test_bz2.log" },
//			{ "FILE_SHARING10",  "Dropbox",   "admin",  "__ALL_EL__", 	"admin",   "pdf",       "test", "Policy Violation",  "File", "critical", "File,Dropbox_2,Share_Test_pdf.log" },
//			{ "FILE_SHARING11",  "Dropbox",   "admin",  "__ALL_EL__", 	"admin",   "pem",       "test", "Policy Violation",  "File", "critical", "File,Dropbox_2,Share_Test_pem.log" },
//			{ "FILE_SHARING12",  "Dropbox",   "admin",  "__ALL_EL__", 	"admin",   "png",       "test", "Policy Violation",  "File", "critical", "File,Dropbox_2,Share_Test_png.log" },
//			{ "FILE_SHARING13",  "Dropbox",   "admin",  "__ALL_EL__", 	"admin",   "ppt",       "test", "Policy Violation",  "File", "critical", "File,Dropbox_2,Share_Test_ppt.log" },
//			{ "FILE_SHARING14",  "Dropbox",   "admin",  "__ALL_EL__", 	"admin",   "cs",        "test", "Policy Violation",  "File", "critical", "File,Dropbox_2,Share_Test_cs.log" },
//			{ "FILE_SHARING15",  "Dropbox",   "admin",  "__ALL_EL__", 	"admin",   "properties", "test", "Policy Violation",  "File", "critical", "File,Dropbox_2,Share_Test_properties.log" },
//			{ "FILE_SHARING16",  "Dropbox",   "admin",  "__ALL_EL__", 	"admin",   "rar",       "test", "Policy Violation",  "File", "critical", "File,Dropbox_2,Share_Test_rar.log" },
//			{ "FILE_SHARING17",  "Dropbox",   "admin",  "__ALL_EL__", 	"admin",   "txt",       "test", "Policy Violation",  "File", "critical", "File,Dropbox_2,Share_Test_txt.log" },
//			{ "FILE_SHARING18",  "Dropbox",   "admin",  "__ALL_EL__", 	"admin",   "xls",       "test", "Policy Violation",  "File", "critical", "File,Dropbox_2,Share_Test_xls.log" },
//			{ "FILE_SHARING19",  "Dropbox",   "admin",  "__ALL_EL__", 	"admin",   "zip",       "test", "Policy Violation",  "File", "critical", "File,Dropbox_2,Share_Test_zip.log" },
//			{ "FILE_SHARING20",  "Dropbox",   "admin",  "__ALL_EL__", 	"admin",   "csv",       "test", "Policy Violation",  "File", "critical", "File,Dropbox_2,Share_Test_csv.log" },
//			{ "FILE_SHARING21",  "Dropbox",   "admin",  "__ALL_EL__", 	"admin",   "dmg",       "test", "Policy Violation",  "File", "critical", "File,Dropbox_2,Share_Test_dmg.log" },
//			{ "FILE_SHARING22",  "Dropbox",   "admin",  "__ALL_EL__", 	"admin",   "doc",       "test", "Policy Violation",  "File", "critical", "File,Dropbox_2,Share_Test_doc.log" },
//			{ "FILE_SHARING23",  "Dropbox",   "admin",  "__ALL_EL__", 	"admin",   "docx",      "test", "Policy Violation",  "File", "critical", "File,Dropbox_2,Share_Test_docx.log" },
//			{ "FILE_SHARING24",  "Dropbox",   "admin",  "__ALL_EL__", 	"admin",   "exe",       "test", "Policy Violation",  "File", "critical", "File,Dropbox_2,Share_Test_exe.log" },
//			{ "FILE_SHARING25",  "Dropbox",   "admin",  "__ALL_EL__", 	"admin",   "flac",      "test", "Policy Violation",  "File", "critical", "File,Dropbox_2,Share_Test_flac.log" },
//			{ "FILE_SHARING26",  "Dropbox",   "admin",  "__ALL_EL__", 	"admin",   "flv",       "test", "Policy Violation",  "File", "critical", "File,Dropbox_2,Share_Test_flv.log" },
//			{ "FILE_SHARING27",  "Dropbox",   "admin",  "__ALL_EL__", 	"admin",   "gif",       "test", "Policy Violation",  "File", "critical", "File,Dropbox_2,Share_Test_gif.log" },
//			{ "FILE_SHARING28",  "Dropbox",   "admin",  "__ALL_EL__", 	"admin",   "gz",        "test", "Policy Violation",  "File", "critical", "File,Dropbox_2,Share_Test_gz.log" },
//			{ "FILE_SHARING29",  "Dropbox",   "admin",  "__ALL_EL__", 	"admin",   "html",      "test", "Policy Violation",  "File", "critical", "File,Dropbox_2,Share_Test_html.log" },
//			{ "FILE_SHARING30",  "Dropbox",   "admin",  "__ALL_EL__", 	"admin",   "java",      "test", "Policy Violation",  "File", "critical", "File,Dropbox_2,Share_Test_java.log" },
//			{ "FILE_SHARING31",  "Dropbox",   "admin",  "__ALL_EL__", 	"admin",   "jpeg",      "test", "Policy Violation",  "File", "critical", "File,Dropbox_2,Share_Test_jpeg.log" },
//			{ "FILE_SHARING32",  "Dropbox",   "admin",  "__ALL_EL__", 	"admin",   "jpg",       "test", "Policy Violation",  "File", "critical", "File,Dropbox_2,Share_Test_jpg.log" },
//			{ "FILE_SHARING33",  "Dropbox",   "admin",  "__ALL_EL__", 	"admin",   "js",        "test", "Policy Violation",  "File", "critical", "File,Dropbox_2,Share_Test_js.log" },
//			{ "FILE_SHARING34",  "Dropbox",   "admin",  "__ALL_EL__", 	"admin",   "json",      "test", "Policy Violation",  "File", "critical", "File,Dropbox_2,Share_Test_json.log" },
//			{ "FILE_SHARING35",  "Dropbox",   "admin",  "__ALL_EL__", 	"admin",   "key",       "test", "Policy Violation",  "File", "critical", "File,Dropbox_2,Share_Test_key.log" },
//			{ "FILE_SHARING36",  "Dropbox",   "admin",  "__ALL_EL__", 	"admin",   "mov",       "test", "Policy Violation",  "File", "critical", "File,Dropbox_2,Share_Test_mov.log" },
//			{ "FILE_SHARING37",  "Dropbox",   "admin",  "__ALL_EL__", 	"admin",   "mp3",       "test", "Policy Violation",  "File", "critical", "File,Dropbox_2,Share_Test_mp3.log" },
//			{ "FILE_SHARING38",  "Dropbox",   "admin",  "__ALL_EL__", 	"admin",   "mp4",       "test", "Policy Violation",  "File", "critical", "File,Dropbox_2,Share_Test_mp4.log" },
//			{ "FILE_SHARING39",  "Dropbox",   "admin",  "__ALL_EL__", 	"admin",   "numbers",   "test", "Policy Violation",  "File", "critical", "File,Dropbox_2,Share_Test_numbers.log" },
//			{ "FILE_SHARING40",  "Dropbox",   "admin",  "__ALL_EL__", 	"admin",   "mpg",       "test", "Policy Violation",  "File", "critical", "File,Dropbox_2,Share_Test_mpg.log" },
			
		};
	}
	
	
	
//	@Test(groups ={"TEST"},dataProvider = "_policySharingFileGenericFileType")
	public void verifyFileSharingPolicyGenericFileTypeEnabled(String policyName, String saasApps,  String sharedBy,   String shareWith, String notifyEmailId, String genericFileType, String fileType, String fileName,  String activityType, String objectType, String severity, String logFile) throws Exception{
		Reporter.log("Validate User Upload the file/folder to folder with Policy Enabled", true);
		testUserName=sharedBy+"@"+suiteData.getTenantDomainName();
		String expectedMsg="[ALERT] "+testUserName+ " attempted to share content:"+fileName.toLowerCase()+"."+fileType+"with external user(s):mohd afjal &lt;mohd.afjal@elastica.co&gt;, mohd.afjal@elastica.co violating policy:"+policyName;
		policyDataMap.clear();
		policyDataMap.put(GatewayTestConstants.POLICY_NAME, policyName);
		policyDataMap.put(GatewayTestConstants.APPLICATIONS, saasApps);
		policyDataMap.put(GatewayTestConstants.SHARED_BY,testUserName );
		policyDataMap.put(GatewayTestConstants.NOTIFY_EMAILID, notifyEmailId+"@"+suiteData.getTenantDomainName());
		policyDataMap.put(GatewayTestConstants.SHARE_WITH, shareWith);
		policyDataMap.put(GatewayTestConstants.FILE_TYPE, genericFileType);
		policyDataMap.put(GatewayTestConstants.FILE_NAME, fileName);
		PolicyAccessEnforcement.fileSharingPolicyCreateEnable(suiteData, requestHeader, policyDataMap);
		replayLogs(logFile);
		PolicyAccessEnforcement.deletePolicy(suiteData, requestHeader, policyDataMap);
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		data.put("policy_type", "FileSharingGateway");
		data.put("policy_action", "ALERT");
		data.put("action_taken", "block,");
		data.put("_PolicyViolated", policyName);
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), "Logs does not match" ); 

	}
	
	@DataProvider
	public Object[][] _policySharingFileGenericFileType() {
		return new Object[][]{
			//Policy Name        App     Shared by  Share with  						admin     File Type    File Name      			Activity Type
			
			{ "FILE_SHARING1",   "Google Drive",   "testuser4",  "__ALL_EL__", 	"admin",  "__ALL_EL__",  "odg",    	"test",        "Policy Violation",  "File", "critical", "Google Drive,Admin_2,Share_File_odg.log" },
			{ "FILE_SHARING2",   "Google Drive",   "testuser4",  "__ALL_EL__", 	"admin",  "__ALL_EL__",  "odp",    	"test",        "Policy Violation",  "File", "critical", "Google Drive,Admin_2,Share_File_odp.log" },
			{ "FILE_SHARING3",   "Google Drive",   "testuser4",  "__ALL_EL__", 	"admin",  "__ALL_EL__",  "ods",    	"test",        "Policy Violation",  "File", "critical", "Google Drive,Admin_2,Share_File_ods.log" },
			{ "FILE_SHARING4",   "Google Drive",   "testuser4",  "__ALL_EL__", 	"admin",  "__ALL_EL__",  "odt",    	"test",        "Policy Violation",  "File", "critical", "Google Drive,Admin_2,Share_File_odt.log" },
			{ "FILE_SHARING5",   "Google Drive",   "testuser4",  "__ALL_EL__", 	"admin",  "__ALL_EL__",  "otg",    	"test",        "Policy Violation",  "File", "critical", "Google Drive,Admin_2,Share_File_otg.log" },
			{ "FILE_SHARING6",   "Google Drive",   "testuser4",  "__ALL_EL__", 	"admin",  "__ALL_EL__",  "otp",    	"test",        "Policy Violation",  "File", "critical", "Google Drive,Admin_2,Share_File_otp.log" },
			{ "FILE_SHARING7",   "Google Drive",   "testuser4",  "__ALL_EL__", 	"admin",  "__ALL_EL__",  "ots",    	"test",        "Policy Violation",  "File", "critical", "Google Drive,Admin_2,Share_File_ots.log" },
			{ "FILE_SHARING8",   "Google Drive",   "testuser4",  "__ALL_EL__", 	"admin",  "__ALL_EL__",  "ott",    	"test",        "Policy Violation",  "File", "critical", "Google Drive,Admin_2,Share_File_ott.log" },
			{ "FILE_SHARING9",   "Google Drive",   "testuser4",  "__ALL_EL__", 	"admin",  "__ALL_EL__",  "pages",   "test",        "Policy Violation",  "File", "critical", "Google Drive,Admin_2,Share_File_pages.log" },
			{ "FILE_SHARING10",  "Google Drive",   "testuser4",  "__ALL_EL__", 	"admin",  "__ALL_EL__",  "pdf",    	"test",        "Policy Violation",  "File", "critical", "Google Drive,Admin_2,Share_File_pdf.log" },
			{ "FILE_SHARING11",  "Google Drive",   "testuser4",  "__ALL_EL__", 	"admin",  "__ALL_EL__",  "pem",    	"test",        "Policy Violation",  "File", "critical", "Google Drive,Admin_2,Share_File_pem.log" },
			{ "FILE_SHARING12",  "Google Drive",   "testuser4",  "__ALL_EL__", 	"admin",  "__ALL_EL__",  "png",    	"test",        "Policy Violation",  "File", "critical", "Google Drive,Admin_2,Share_File_png.log" },
			{ "FILE_SHARING13",  "Google Drive",   "testuser4",  "__ALL_EL__", 	"admin",  "__ALL_EL__",  "ppt",    	"test",        "Policy Violation",  "File", "critical", "Google Drive,Admin_2,Share_File_ppt.log" },
			{ "FILE_SHARING14",  "Google Drive",   "testuser4",  "__ALL_EL__", 	"admin",  "__ALL_EL__",  "pptx",    "test",        "Policy Violation",  "File", "critical", "Google Drive,Admin_2,Share_File_pptx.log" },
			{ "FILE_SHARING15",  "Google Drive",   "testuser4",  "__ALL_EL__", 	"admin",  "__ALL_EL__",  "properties",    	"test",        "Policy Violation",  "File", "critical", "Google Drive,Admin_2,Share_File_properties.log" },
			{ "FILE_SHARING16",  "Google Drive",   "testuser4",  "__ALL_EL__", 	"admin",  "__ALL_EL__",  "rar",    	"test",        "Policy Violation",  "File", "critical", "Google Drive,Admin_2,Share_File_rar.log" },
			{ "FILE_SHARING17",  "Google Drive",   "testuser4",  "__ALL_EL__", 	"admin",  "__ALL_EL__",  "rtf",    	"test",        "Policy Violation",  "File", "critical", "Google Drive,Admin_2,Share_File_rtf.log" },
			{ "FILE_SHARING18",  "Google Drive",   "testuser4",  "__ALL_EL__", 	"admin",  "__ALL_EL__",  "xls",    	"test",        "Policy Violation",  "File", "critical", "Google Drive,Admin_2,Share_File_xls.log" },
		};
	}
	
//	@Test(groups ={"TEST"},dataProvider = "_policySharingFileGenericFileName")
	public void verifyFileSharingPolicyGenericFileNameEnabled(String policyName, String saasApps,  String sharedBy,   String shareWith, String notifyEmailId,   String fileType,   String activityType, String objectType, String severity, String logFile) throws Exception{
		Reporter.log("Validate User Upload the file/folder to folder with Policy Enabled", true);
		testUserName=sharedBy+"@"+suiteData.getTenantDomainName();
		String expectedMsg="[ALERT] "+testUserName+ " attempted to share content:test."+fileType+"with external user(s):mohd afjal &lt;mohd.afjal@elastica.co&gt;, mohd.afjal@elastica.co violating policy:"+policyName;
		System.out.println(expectedMsg);
		policyDataMap.clear();
		policyDataMap.put(GatewayTestConstants.POLICY_NAME, policyName);
		policyDataMap.put(GatewayTestConstants.APPLICATIONS, saasApps);
		policyDataMap.put(GatewayTestConstants.SHARED_BY,testUserName );
		policyDataMap.put(GatewayTestConstants.NOTIFY_EMAILID, notifyEmailId+"@"+suiteData.getTenantDomainName());
		policyDataMap.put(GatewayTestConstants.SHARE_WITH, shareWith);
		policyDataMap.put(GatewayTestConstants.FILE_TYPE, fileType);
		PolicyAccessEnforcement.fileSharingPolicyCreateEnable(suiteData, requestHeader, policyDataMap);
		replayLogs(logFile);
		PolicyAccessEnforcement.deletePolicy(suiteData, requestHeader, policyDataMap);
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		data.put("policy_type", "FileSharingGateway");
		data.put("policy_action", "ALERT");
		data.put("action_taken", "block,");
		data.put("_PolicyViolated", policyName);
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), "Logs does not match" ); 

	}
	
	@DataProvider
	public Object[][] _policySharingFileGenericFileName() {
		return new Object[][]{
			//Policy Name        App     Shared by  Share with  						admin     File Type    File Name      			Activity Type
			
			{ "FILE_SHARING1",   "Google Drive",   "testuser4",  "__ALL_EL__", 	"admin",    "odg",      "Policy Violation",  "File", "critical", "Google Drive,Admin_2,Share_File_odg.log" },
			{ "FILE_SHARING2",   "Google Drive",   "testuser4",  "__ALL_EL__", 	"admin",    "odp",      "Policy Violation",  "File", "critical", "Google Drive,Admin_2,Share_File_odp.log" },
			{ "FILE_SHARING3",   "Google Drive",   "testuser4",  "__ALL_EL__", 	"admin",    "ods",      "Policy Violation",  "File", "critical", "Google Drive,Admin_2,Share_File_ods.log" },
			{ "FILE_SHARING4",   "Google Drive",   "testuser4",  "__ALL_EL__", 	"admin",    "odt",      "Policy Violation",  "File", "critical", "Google Drive,Admin_2,Share_File_odt.log" },
			{ "FILE_SHARING5",   "Google Drive",   "testuser4",  "__ALL_EL__", 	"admin",    "otg",    	"Policy Violation",  "File", "critical", "Google Drive,Admin_2,Share_File_otg.log" },
			{ "FILE_SHARING6",   "Google Drive",   "testuser4",  "__ALL_EL__", 	"admin",    "otp",    	"Policy Violation",  "File", "critical", "Google Drive,Admin_2,Share_File_otp.log" },
			{ "FILE_SHARING7",   "Google Drive",   "testuser4",  "__ALL_EL__", 	"admin",    "ots",    	"Policy Violation",  "File", "critical", "Google Drive,Admin_2,Share_File_ots.log" },
			{ "FILE_SHARING8",   "Google Drive",   "testuser4",  "__ALL_EL__", 	"admin",    "ott",    	"Policy Violation",  "File", "critical", "Google Drive,Admin_2,Share_File_ott.log" },
			{ "FILE_SHARING9",   "Google Drive",   "testuser4",  "__ALL_EL__", 	"admin",    "pages",    "Policy Violation",  "File", "critical", "Google Drive,Admin_2,Share_File_pages.log" },
			{ "FILE_SHARING10",  "Google Drive",   "testuser4",  "__ALL_EL__", 	"admin",    "pdf",    	"Policy Violation",  "File", "critical", "Google Drive,Admin_2,Share_File_pdf.log" },
			{ "FILE_SHARING11",  "Google Drive",   "testuser4",  "__ALL_EL__", 	"admin",    "pem",    	"Policy Violation",  "File", "critical", "Google Drive,Admin_2,Share_File_pem.log" },
			{ "FILE_SHARING12",  "Google Drive",   "testuser4",  "__ALL_EL__", 	"admin",    "png",    	"Policy Violation",  "File", "critical", "Google Drive,Admin_2,Share_File_png.log" },
			{ "FILE_SHARING13",  "Google Drive",   "testuser4",  "__ALL_EL__", 	"admin",    "ppt",    	"Policy Violation",  "File", "critical", "Google Drive,Admin_2,Share_File_ppt.log" },
			{ "FILE_SHARING14",  "Google Drive",   "testuser4",  "__ALL_EL__", 	"admin",    "pptx",     "Policy Violation",  "File", "critical", "Google Drive,Admin_2,Share_File_pptx.log" },
			{ "FILE_SHARING15",  "Google Drive",   "testuser4",  "__ALL_EL__", 	"admin",    "properties", "Policy Violation",  "File", "critical", "Google Drive,Admin_2,Share_File_properties.log" },
			{ "FILE_SHARING16",  "Google Drive",   "testuser4",  "__ALL_EL__", 	"admin",    "rar",    	"Policy Violation",  "File", "critical", "Google Drive,Admin_2,Share_File_rar.log" },
			{ "FILE_SHARING17",  "Google Drive",   "testuser4",  "__ALL_EL__", 	"admin",    "rtf",    	"Policy Violation",  "File", "critical", "Google Drive,Admin_2,Share_File_rtf.log" },
			{ "FILE_SHARING18",  "Google Drive",   "testuser4",  "__ALL_EL__", 	"admin",    "xls",    	"Policy Violation",  "File", "critical", "Google Drive,Admin_2,Share_File_xls.log" },
	};
	}	
	

	@Test(groups ={"DELETE_ALL"})
	public void deleteAllPolicies() throws Exception{
		Reporter.log("Validate User Upload the file/folder to folder with Policy Enabled", true);
		PolicyAccessEnforcement.deleteAllPolicy(suiteData,  requestHeader);
	}
	
}
