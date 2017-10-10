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

public class GDriveFileSharingPolicyTest extends CommonConfiguration {
	String currentTimeInJodaFormat;
	GWForensicSearchResults fsr;
	ArrayList<String> messages = new ArrayList<String>();
	ArrayList<String> objectTypeList = new ArrayList<String>();
	ArrayList<String> objectNameList = new ArrayList<String>();
	ArrayList<String> severityList = new ArrayList<String>();
	LogValidator logValidator;
	String userLitral="User";
//	String fileName="Test.pdf";
//	String policyName="PolicyFT_FileType";
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
			
			{ "FILE_SHARING1",   "Google Drive",   "testuser4",  "__ALL_EL__", 	"admin",   "odg",    	"test", "Policy Violation",  "File", "critical", "Google Drive,Admin_2,Share_File_odg.log" },
			{ "FILE_SHARING2",   "Google Drive",   "testuser4",  "__ALL_EL__",  "admin",   "odp",    	"test", "Policy Violation",  "File", "critical", "Google Drive,Admin_2,Share_File_odp.log"},
		    { "FILE_SHARING3",   "Google Drive",   "testuser4",  "__ALL_EL__", 	"admin",   "ods",    	"test", "Policy Violation",  "File", "critical", "Google Drive,Admin_2,Share_File_ods.log" },
		    { "FILE_SHARING4",   "Google Drive",   "testuser4",  "__ALL_EL__", 	"admin",   "odt",   	"test", "Policy Violation",  "File", "critical", "Google Drive,Admin_2,Share_File_odt.log" },
			{ "FILE_SHARING5",   "Google Drive",   "testuser4",  "__ALL_EL__", 	"admin",   "otg",    	"test", "Policy Violation",  "File", "critical", "Google Drive,Admin_2,Share_File_otg.log" },
			{ "FILE_SHARING6",   "Google Drive",   "testuser4",  "__ALL_EL__", 	"admin",   "otp",    	"test", "Policy Violation",  "File", "critical", "Google Drive,Admin_2,Share_File_otp.log" },
			{ "FILE_SHARING7",   "Google Drive",   "testuser4",  "__ALL_EL__", 	"admin",   "ots",    	"test", "Policy Violation",  "File", "critical", "Google Drive,Admin_2,Share_File_ots.log" },
			{ "FILE_SHARING8",   "Google Drive",   "testuser4",  "__ALL_EL__", 	"admin",   "ott",    	"test", "Policy Violation",  "File", "critical", "Google Drive,Admin_2,Share_File_ott.log" },
			{ "FILE_SHARING9",   "Google Drive",   "testuser4",  "__ALL_EL__", 	"admin",   "pages",    	"test", "Policy Violation",  "File", "critical", "Google Drive,Admin_2,Share_File_pages.log" },
			{ "FILE_SHARING10",  "Google Drive",   "testuser4",  "__ALL_EL__", 	"admin",   "pdf",       "test", "Policy Violation",  "File", "critical", "Google Drive,Admin_2,Share_File_pdf.log" },
			{ "FILE_SHARING11",  "Google Drive",   "testuser4",  "__ALL_EL__", 	"admin",   "pem",       "test", "Policy Violation",  "File", "critical", "Google Drive,Admin_2,Share_File_pem.log" },
			{ "FILE_SHARING12",  "Google Drive",   "testuser4",  "__ALL_EL__", 	"admin",   "png",       "test", "Policy Violation",  "File", "critical", "Google Drive,Admin_2,Share_File_png.log" },
			{ "FILE_SHARING13",  "Google Drive",   "testuser4",  "__ALL_EL__", 	"admin",   "ppt",       "test", "Policy Violation",  "File", "critical", "Google Drive,Admin_2,Share_File_ppt.log" },
			{ "FILE_SHARING14",  "Google Drive",   "testuser4",  "__ALL_EL__", 	"admin",   "pptx",      "test", "Policy Violation",  "File", "critical", "Google Drive,Admin_2,Share_File_pptx.log" },
			{ "FILE_SHARING15",  "Google Drive",   "testuser4",  "__ALL_EL__", 	"admin",   "properties", "test", "Policy Violation",  "File", "critical", "Google Drive,Admin_2,Share_File_properties.log" },
			{ "FILE_SHARING16",  "Google Drive",   "testuser4",  "__ALL_EL__", 	"admin",   "rar",       "test", "Policy Violation",  "File", "critical", "Google Drive,Admin_2,Share_File_rar.log" },
			{ "FILE_SHARING17",  "Google Drive",   "testuser4",  "__ALL_EL__", 	"admin",   "rtf",       "test", "Policy Violation",  "File", "critical", "Google Drive,Admin_2,Share_File_rtf.log" },
			{ "FILE_SHARING18",  "Google Drive",   "testuser4",  "__ALL_EL__", 	"admin",   "xls",       "test", "Policy Violation",  "File", "critical", "Google Drive,Admin_2,Share_File_xls.log" },
			
		};
	}
	
	
	
	@Test(groups ={"TEST"},dataProvider = "_policySharingFileGenericFileType")
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
	
	@Test(groups ={"TEST"},dataProvider = "_policySharingFileGenericFileName")
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
