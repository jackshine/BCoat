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
import com.elastica.beatle.replayTool.EPDV1SampleTest;


/*******************Author**************
 * 
 * @author Afjal
 */

public class FileSharingPolicyTest extends CommonConfiguration {
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
	
	@Test(groups ={"TEST"},dataProvider = "_policySharingFileLink")
	public void verifyFileSharingPolicyLinkEnabled(String policyName, String saasApps,  String sharedBy,   String shareWith, String notifyEmailId, String fileType, String fileName,  String activityType, String objectType, String severity, String logFile) throws Exception{
		Reporter.log("Validate User Upload the file/folder to folder with Policy Enabled", true);
		testUserName=sharedBy+"@"+suiteData.getTenantDomainName();
		String expectedMsg="[ALERT] "+testUserName+ " attempted to share content:"+fileName.toLowerCase()+"with external user(s):ALL_EL__ violating policy:"+policyName;
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
	public Object[][] _policySharingFileLink() {
		return new Object[][]{
			//Policy Name        App     Shared by  Share with  						admin     File Type    File Name      			Activity Type
			
			{ "FILE_SHARING1_LINK",   "Salesforce",   "testuser1",  "__ALL_EL__", 	"admin",   "",    	"", "Policy Violation",  "File", "critical", "Salesforce,Admin_2,Share_File_Test_mp3.log" },
			{ "FILE_SHARING2_Link",   "Salesforce",   "testuser1",  "__ALL_EL__",   "admin",   "",    	"", "Policy Violation",  "File", "critical", "Salesforce,Admin_2,Share_File_Test_mp4.log"},
		    { "FILE_SHARING3_LINK",   "Salesforce",   "testuser1",  "__ALL_EL__", 	"admin",   "",    	"", "Policy Violation",  "File", "critical", "Salesforce,Admin_2,Share_File_Test_ods.log" },
		    { "FILE_SHARING4_LINK",   "Salesforce",   "testuser1",  "__ALL_EL__", 	"admin",   "",   	"", "Policy Violation",  "File", "critical", "Salesforce,Admin_2,Share_File_Test_odt.log" },
			{ "FILE_SHARING5_LINK",   "Salesforce",   "testuser1",  "__ALL_EL__", 	"admin",   "",    	"", "Policy Violation",  "File", "critical", "Salesforce,Admin_2,Share_File_Test_pdf.log" },
			{ "FILE_SHARING6_LINK",   "Salesforce",   "testuser1",  "__ALL_EL__", 	"admin",   "",    	"", "Policy Violation",  "File", "critical", "Salesforce,Admin_2,Share_File_Test_png.log" },
			{ "FILE_SHARING7_LINK",   "Salesforce",   "testuser1",  "__ALL_EL__", 	"admin",   "",    	"", "Policy Violation",  "File", "critical", "Salesforce,Admin_2,Share_File_Test_ppt.log" },
			{ "FILE_SHARING8_LINK",   "Salesforce",   "testuser1",  "__ALL_EL__", 	"admin",   "",    	"", "Policy Violation",  "File", "critical", "Salesforce,Admin_2,Share_File_Test_rtf.log" },
			{ "FILE_SHARING9_LINK",   "Salesforce",   "testuser1",  "__ALL_EL__", 	"admin",   "",    	"", "Policy Violation",  "File", "critical", "Salesforce,Admin_2,Share_File_Test_tif.log" },
			{ "FILE_SHARING10_LINK",  "Salesforce",   "testuser1",  "__ALL_EL__", 	"admin",   "",      "", "Policy Violation",  "File", "critical", "Salesforce,Admin_2,Share_File_Test_txt.log" },
			{ "FILE_SHARING11_LINK",  "Salesforce",   "testuser1",  "__ALL_EL__", 	"admin",   "",      "", "Policy Violation",  "File", "critical", "Salesforce,Admin_2,Share_File_Test_xlsm.log" },
			{ "FILE_SHARING12_LINK",  "Salesforce",   "testuser1",  "__ALL_EL__", 	"admin",   "",      "", "Policy Violation",  "File", "critical", "Salesforce,Admin_2,Share_File_Test_zip.log" },
			
		};
	}
	
	
	
	@Test(groups ={"TEST"},dataProvider = "_policySharingFileGenericFileType")
	public void verifyFileSharingPolicyGenericFileTypeEnabled(String policyName, String saasApps,  String sharedBy,   String shareWith, String notifyEmailId, String genericFileType, String fileType, String fileName,  String activityType, String objectType, String severity, String logFile) throws Exception{
		Reporter.log("Validate User Upload the file/folder to folder with Policy Enabled", true);
		testUserName=sharedBy+"@"+suiteData.getTenantDomainName();
		String expectedMsg="[ALERT] "+testUserName+ " attempted to share content:"+fileName.toLowerCase()+""+fileType+"with external user(s):ALL_EL__ violating policy:"+policyName;
		policyDataMap.clear();
		policyDataMap.put(GatewayTestConstants.POLICY_NAME, policyName);
		policyDataMap.put(GatewayTestConstants.APPLICATIONS, saasApps);
		policyDataMap.put(GatewayTestConstants.SHARED_BY,testUserName );
		policyDataMap.put(GatewayTestConstants.NOTIFY_EMAILID, notifyEmailId+"@"+suiteData.getTenantDomainName());
		policyDataMap.put(GatewayTestConstants.SHARE_WITH, shareWith);
		policyDataMap.put(GatewayTestConstants.FILE_TYPE, genericFileType);
		policyDataMap.put(GatewayTestConstants.FILE_NAME, fileName);
		//PolicyAccessEnforcement.deletePolicy(suiteData, requestHeader, policyDataMap);
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
			
			{ "FILE_SHARING1_LINK",   "Salesforce",   "testuser1",  "__ALL_EL__", 	"admin",  "__ALL_EL__",  "",    	"",     "Policy Violation",  "File", "critical", "Salesforce,Admin_2,Share_File_Test_mp3.log" },
			{ "FILE_SHARING2_LINK",   "Salesforce",   "testuser1",  "__ALL_EL__", 	"admin",  "__ALL_EL__",  "",    	"",     "Policy Violation",  "File", "critical", "Salesforce,Admin_2,Share_File_Test_mp4.log" },
			{ "FILE_SHARING3_LINK",   "Salesforce",   "testuser1",  "__ALL_EL__", 	"admin",  "__ALL_EL__",  "",    	"",     "Policy Violation",  "File", "critical", "Salesforce,Admin_2,Share_File_Test_ods.log" },
			{ "FILE_SHARING4_LINK",   "Salesforce",   "testuser1",  "__ALL_EL__", 	"admin",  "__ALL_EL__",  "",    	"",     "Policy Violation",  "File", "critical", "Salesforce,Admin_2,Share_File_Test_odt.log" },
			{ "FILE_SHARING5_LINK",   "Salesforce",   "testuser1",  "__ALL_EL__", 	"admin",  "__ALL_EL__",  "",    	"",     "Policy Violation",  "File", "critical", "Salesforce,Admin_2,Share_File_Test_pdf.log" },
			{ "FILE_SHARING6_LINK",   "Salesforce",   "testuser1",  "__ALL_EL__", 	"admin",  "__ALL_EL__",  "",    	"",     "Policy Violation",  "File", "critical", "Salesforce,Admin_2,Share_File_Test_png.log" },
			{ "FILE_SHARING7_LINK",   "Salesforce",   "testuser1",  "__ALL_EL__", 	"admin",  "__ALL_EL__",  "",    	"",     "Policy Violation",  "File", "critical", "Salesforce,Admin_2,Share_File_Test_ppt.log" },
			{ "FILE_SHARING8_LINK",   "Salesforce",   "testuser1",  "__ALL_EL__", 	"admin",  "__ALL_EL__",  "",    	"",     "Policy Violation",  "File", "critical", "Salesforce,Admin_2,Share_File_Test_rtf.log" },
			{ "FILE_SHARING9_LINK",   "Salesforce",   "testuser1",  "__ALL_EL__", 	"admin",  "__ALL_EL__",  "",    	"",     "Policy Violation",  "File", "critical", "Salesforce,Admin_2,Share_File_Test_tif.log" },
			{ "FILE_SHARING10_LINK",  "Salesforce",   "testuser1",  "__ALL_EL__", 	"admin",  "__ALL_EL__",  "",    	"",     "Policy Violation",  "File", "critical", "Salesforce,Admin_2,Share_File_Test_txt.log" },
			{ "FILE_SHARING11_LINK",  "Salesforce",   "testuser1",  "__ALL_EL__", 	"admin",  "__ALL_EL__",  "",    	"",     "Policy Violation",  "File", "critical", "Salesforce,Admin_2,Share_File_Test_xlsm.log" },
			{ "FILE_SHARING12_LINK",  "Salesforce",   "testuser1",  "__ALL_EL__", 	"admin",  "__ALL_EL__",  "",    	"",     "Policy Violation",  "File", "critical", "Salesforce,Admin_2,Share_File_Test_zip.log" },
		};
	}
	
	@Test(groups ={"TEST"},dataProvider = "_policySharingFileGenericFileName")
	public void verifyFileSharingPolicyGenericFileNameEnabled(String policyName, String saasApps,  String sharedBy,   String shareWith, String notifyEmailId, String genericFileType, String fileType, String fileName,  String activityType, String objectType, String severity, String logFile) throws Exception{
		Reporter.log("Validate User Upload the file/folder to folder with Policy Enabled", true);
		testUserName=sharedBy+"@"+suiteData.getTenantDomainName();
		String expectedMsg="[ALERT] "+testUserName+ " attempted to share content:"+""+fileType+"with external user(s):ALL_EL__ violating policy:"+policyName;
		System.out.println(expectedMsg);
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
	public Object[][] _policySharingFileGenericFileName() {
		return new Object[][]{
			//Policy Name        App     Shared by  Share with  						admin     File Type    File Name      			Activity Type
			
			{ "FILE_SHARING1_LINK",   "Salesforce",   "testuser1",  "__ALL_EL__", 	"admin",  "__ALL_EL__",  "",    	"Any", "Policy Violation",  "File", "critical", "Salesforce,Admin_2,Share_File_Test_mp3.log" },
			{ "FILE_SHARING2_LINK",   "Salesforce",   "testuser1",  "__ALL_EL__", 	"admin",  "__ALL_EL__",  "",    	"Any", "Policy Violation",  "File", "critical", "Salesforce,Admin_2,Share_File_Test_mp4.log" },
			{ "FILE_SHARING3_LINK",   "Salesforce",   "testuser1",  "__ALL_EL__", 	"admin",  "__ALL_EL__",  "",    	"Any", "Policy Violation",  "File", "critical", "Salesforce,Admin_2,Share_File_Test_ods.log" },
			{ "FILE_SHARING4_LINK",   "Salesforce",   "testuser1",  "__ALL_EL__", 	"admin",  "__ALL_EL__",  "",    	"Any", "Policy Violation",  "File", "critical", "Salesforce,Admin_2,Share_File_Test_odt.log" },
			{ "FILE_SHARING5_LINK",   "Salesforce",   "testuser1",  "__ALL_EL__", 	"admin",  "__ALL_EL__",  "",    	"Any", "Policy Violation",  "File", "critical", "Salesforce,Admin_2,Share_File_Test_pdf.log" },
			{ "FILE_SHARING6_LINK",   "Salesforce",   "testuser1",  "__ALL_EL__", 	"admin",  "__ALL_EL__",  "",    	"Any", "Policy Violation",  "File", "critical", "Salesforce,Admin_2,Share_File_Test_png.log" },
			{ "FILE_SHARING7_LINK",   "Salesforce",   "testuser1",  "__ALL_EL__", 	"admin",  "__ALL_EL__",  "",    	"Any", "Policy Violation",  "File", "critical", "Salesforce,Admin_2,Share_File_Test_ppt.log" },
			{ "FILE_SHARING8_LINK",   "Salesforce",   "testuser1",  "__ALL_EL__", 	"admin",  "__ALL_EL__",  "",    	"Any", "Policy Violation",  "File", "critical", "Salesforce,Admin_2,Share_File_Test_rtf.log" },
			{ "FILE_SHARING9_LINK",   "Salesforce",   "testuser1",  "__ALL_EL__", 	"admin",  "__ALL_EL__",  "",    	"Any", "Policy Violation",  "File", "critical", "Salesforce,Admin_2,Share_File_Test_tif.log" },
			{ "FILE_SHARING10_LINK",  "Salesforce",   "testuser1",  "__ALL_EL__", 	"admin",  "__ALL_EL__",  "",    	"Any", "Policy Violation",  "File", "critical", "Salesforce,Admin_2,Share_File_Test_txt.log" },
			{ "FILE_SHARING11_LINK",  "Salesforce",   "testuser1",  "__ALL_EL__", 	"admin",  "__ALL_EL__",  "",    	"Any", "Policy Violation",  "File", "critical", "Salesforce,Admin_2,Share_File_Test_xlsm.log" },
			{ "FILE_SHARING12_LINK",  "Salesforce",   "testuser1",  "__ALL_EL__", 	"admin",  "__ALL_EL__",  "",    	"Any", "Policy Violation",  "File", "critical", "Salesforce,Admin_2,Share_File_Test_zip.log" },
	
	};
	}	
	

	@Test(groups ={"DELETE_ALL"})
	public void deleteAllPolicies() throws Exception{
		Reporter.log("Validate User Upload the file/folder to folder with Policy Enabled", true);
		PolicyAccessEnforcement.deleteAllPolicy(suiteData,  requestHeader);
	}
	
}
