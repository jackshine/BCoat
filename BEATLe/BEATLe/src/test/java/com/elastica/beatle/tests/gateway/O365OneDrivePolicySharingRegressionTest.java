package com.elastica.beatle.tests.gateway;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import static org.testng.Assert.assertTrue;
import java.io.IOException;
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
 *
 */

public class O365OneDrivePolicySharingRegressionTest extends CommonConfiguration {

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
	//Map <String, String> data = new HashMap<String, String>();
	Map<String, Object> expectedResult=new HashMap<>();
	
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
	public void verifyFileSharingPolicyEnabled(String policyName, String saasApps,  String sharedBy,   String shareWith, String notifyEmailId, String fileType, String fileName,  String activityType, String objectType, String severity, String logFile) throws Exception{
		Reporter.log("Validate User Upload the file/folder to folder with Policy Enabled", true);
		testUserName=sharedBy+"@"+suiteData.getTenantDomainName();
		String expectedMsg="[ALERT] "+testUserName+ " attempted to share content:"+fileName.toLowerCase()+"."+fileType+"with external user(s):ALL_EL__ violating policy:"+policyName;
		policyDataMap.clear();
		policyDataMap.put(GatewayTestConstants.POLICY_NAME, policyName);
		policyDataMap.put(GatewayTestConstants.APPLICATIONS, saasApps);
		policyDataMap.put(GatewayTestConstants.SHARED_BY,testUserName );
		policyDataMap.put(GatewayTestConstants.NOTIFY_EMAILID, notifyEmailId+"@"+suiteData.getTenantDomainName());
		policyDataMap.put(GatewayTestConstants.SHARE_WITH, shareWith);
		policyDataMap.put(GatewayTestConstants.FILE_TYPE, fileType);
		policyDataMap.put(GatewayTestConstants.FILE_NAME, fileName);
		PolicyAccessEnforcement.fileSharingPolicyCreateEnable(suiteData, requestHeader, policyDataMap);
		replayLogsEPDV3(logFile);
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
			
			{ "FILE_SHARING1",    "Office 365",   "testuser1",  "__ALL_EL__", 	"admin",  "bin",    	"Test",   "Policy Violation",  "File", "critical", "OneDrive,Share_File_Test_bin.log" },
			{ "FILE_SHARING2",    "Office 365",   "testuser1",  "__ALL_EL__",   "admin",  "bmp",    	"Test",   "Policy Violation",  "File", "critical", "OneDrive,Share_File_Test_bmp.log"},
			{ "FILE_SHARING3",    "Office 365",   "testuser1",  "__ALL_EL__", 	"admin",  "bz2",        "Test",   "Policy Violation",  "File", "critical", "OneDrive,Share_File_Test_bz2.log"},
			{ "FILE_SHARING4",    "Office 365",   "testuser1",  "__ALL_EL__", 	"admin",  "cs",   	    "Test",   "Policy Violation",  "File", "critical", "OneDrive,Share_File_Test_cs.log" },
			{ "FILE_SHARING5",    "Office 365",   "testuser1",  "__ALL_EL__", 	"admin",  "csv",    	"Test",   "Policy Violation",  "File", "critical", "OneDrive,Share_File_Test_csv.log" },
			{ "FILE_SHARING6",    "Office 365",   "testuser1",  "__ALL_EL__", 	"admin",  "doc",    	"Test",   "Policy Violation",  "File", "critical", "OneDrive,Share_File_Test_doc.log"},
			{ "FILE_SHARING7",    "Office 365",   "testuser1",  "__ALL_EL__", 	"admin",  "docx",    	"Test",   "Policy Violation",  "File", "critical", "OneDrive,Share_File_Test_docx.log" },
			{ "FILE_SHARING8",    "Office 365",   "testuser1",  "__ALL_EL__", 	"admin",  "exe",    	"Test",   "Policy Violation",  "File", "critical", "OneDrive,Share_File_Test_exe.log" },
			{ "FILE_SHARING9",    "Office 365",   "testuser1",  "__ALL_EL__", 	"admin",  "flac",    	"Test",   "Policy Violation",  "File", "critical", "OneDrive,Share_File_Test_flac.log" },
			{ "FILE_SHARING10",   "Office 365",   "testuser1",  "__ALL_EL__", 	"admin",  "gif",    	"Test",   "Policy Violation",  "File", "critical", "OneDrive,Share_File_Test_gif.log" },
			{ "FILE_SHARING11",   "Office 365",   "testuser1",  "__ALL_EL__", 	"admin",  "html",    	"Test",   "Policy Violation",  "File", "critical", "OneDrive,Share_File_Test_html.log" },
			{ "FILE_SHARING12",   "Office 365",   "testuser1",  "__ALL_EL__", 	"admin",  "java",    	"Test",   "Policy Violation",  "File", "critical", "OneDrive,Share_File_Test_java.log" },
			{ "FILE_SHARING13",   "Office 365",   "testuser1",  "__ALL_EL__", 	"admin",  "jpg",    	"Test",   "Policy Violation",  "File", "critical", "OneDrive,Share_File_Test_jpg.log" },
			{ "FILE_SHARING14",   "Office 365",   "testuser1",  "__ALL_EL__", 	"admin",  "json",    	"Test",   "Policy Violation",  "File", "critical", "OneDrive,Share_File_Test_json.log" },
			{ "FILE_SHARING15",   "Office 365",   "testuser1",  "__ALL_EL__", 	"admin",  "key",    	"Test",   "Policy Violation",  "File", "critical", "OneDrive,Share_File_Test_key.log" },
			{ "FILE_SHARING16",   "Office 365",   "testuser1",  "__ALL_EL__", 	"admin",  "mov",    	"Test",   "Policy Violation",  "File", "critical", "OneDrive,Share_File_Test_mov.log" },
			{ "FILE_SHARING17",   "Office 365",   "testuser1",  "__ALL_EL__", 	"admin",  "mp3",    	"Test",   "Policy Violation",  "File", "critical", "OneDrive,Share_File_Test_mp3.log" },
			{ "FILE_SHARING18",   "Office 365",   "testuser1",  "__ALL_EL__", 	"admin",  "mp4",    	"Test",   "Policy Violation",  "File", "critical", "OneDrive,Share_File_Test_mp4.log" },
			{ "FILE_SHARING19",   "Office 365",   "testuser1",  "__ALL_EL__", 	"admin",  "mpg",    	"Test",   "Policy Violation",  "File", "critical", "OneDrive,Share_File_Test_mpg.log" },
			{ "FILE_SHARING20",   "Office 365",   "testuser1",  "__ALL_EL__", 	"admin",  "numbers",    "Test",   "Policy Violation",  "File", "critical", "OneDrive,Share_File_Test_numbers.log" },
			{ "FILE_SHARING21",   "Office 365",   "testuser1",  "__ALL_EL__", 	"admin",  "odp",    	"Test",   "Policy Violation",  "File", "critical", "OneDrive,Share_File_Test_odp.log" },
			{ "FILE_SHARING22",   "Office 365",   "testuser1",  "__ALL_EL__", 	"admin",  "ods",    	"Test",   "Policy Violation",  "File", "critical", "OneDrive,Share_File_Test_ods.log" },
			{ "FILE_SHARING23",   "Office 365",   "testuser1",  "__ALL_EL__", 	"admin",  "odt",    	"Test",   "Policy Violation",  "File", "critical", "OneDrive,Share_File_Test_odt.log" },
			{ "FILE_SHARING24",   "Office 365",   "testuser1",  "__ALL_EL__", 	"admin",  "pages",    	"Test",   "Policy Violation",  "File", "critical", "OneDrive,Share_File_Test_pages.log" },
			{ "FILE_SHARING25",   "Office 365",   "testuser1",  "__ALL_EL__", 	"admin",  "pdf",    	"Test",   "Policy Violation",  "File", "critical", "OneDrive,Share_File_Test_pdf.log" },
			{ "FILE_SHARING26",   "Office 365",   "testuser1",  "__ALL_EL__", 	"admin",  "pem",    	"Test",   "Policy Violation",  "File", "critical", "OneDrive,Share_File_Test_pem.log" },
			{ "FILE_SHARING27",   "Office 365",   "testuser1",  "__ALL_EL__", 	"admin",  "png",    	"Test",   "Policy Violation",  "File", "critical", "OneDrive,Share_File_Test_png.log" },
			{ "FILE_SHARING28",   "Office 365",   "testuser1",  "__ALL_EL__", 	"admin",  "ppt",    	"Test",   "Policy Violation",  "File", "critical", "OneDrive,Share_File_Test_ppt.log" },
			{ "FILE_SHARING29",   "Office 365",   "testuser1",  "__ALL_EL__", 	"admin",  "properties", "Test",   "Policy Violation",  "File", "critical", "OneDrive,Share_File_Test_properties.log" },
			{ "FILE_SHARING30",   "Office 365",   "testuser1",  "__ALL_EL__", 	"admin",  "rar",    	"Test",   "Policy Violation",  "File", "critical", "OneDrive,Share_File_Test_rar.log" },
			{ "FILE_SHARING31",   "Office 365",   "testuser1",  "__ALL_EL__", 	"admin",  "txt",    	"Test",   "Policy Violation",  "File", "critical", "OneDrive,Share_File_Test_txt.log" },
			{ "FILE_SHARING32",   "Office 365",   "testuser1",  "__ALL_EL__", 	"admin",  "xls",    	"Test",   "Policy Violation",  "File", "critical", "OneDrive,Share_File_Test_xls.log" },
			{ "FILE_SHARING33",   "Office 365",   "testuser1",  "__ALL_EL__", 	"admin",  "zip",    	"Test",   "Policy Violation",  "File", "critical", "OneDrive,Share_File_Test_zip.log" },


		};
	}
	
	

	
}