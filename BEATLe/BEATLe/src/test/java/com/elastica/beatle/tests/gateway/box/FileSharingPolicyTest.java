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

import com.elastica.beatle.RawJsonParser;
import com.elastica.beatle.gateway.CommonConfiguration;
import com.elastica.beatle.gateway.GatewayTestConstants;
import com.elastica.beatle.gateway.LogValidator;
import com.elastica.beatle.gateway.PolicyAccessEnforcement;
import com.elastica.beatle.gateway.dto.GWForensicSearchResults;
import com.elastica.beatle.protect.ProtectFunctions;
import com.elastica.beatle.replayTool.EPDV1SampleTest;


/*******************Author**************
 * 
 * @author usman
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
		String expectedMsg="[ALERT] "+testUserName+ " attempted to share content:"+fileName.toLowerCase()+"."+fileType+"with external user(s):mohd.afjal@elastica.co violating policy:"+policyName;
		
		//String expectedMsg="[ALERT] "+testUserName+ " attempted to share content:"+fileName.toLowerCase()+"."+fileType+"with external user(s):";
		System.out.println(expectedMsg);
		policyDataMap.clear();
		policyDataMap.put(GatewayTestConstants.POLICY_NAME, policyName);
		policyDataMap.put(GatewayTestConstants.APPLICATIONS, saasApps);
		policyDataMap.put(GatewayTestConstants.SHARED_BY,testUserName );
		policyDataMap.put(GatewayTestConstants.NOTIFY_EMAILID, notifyEmailId+"@"+suiteData.getTenantDomainName());
		policyDataMap.put(GatewayTestConstants.SHARE_WITH, shareWith);
		policyDataMap.put(GatewayTestConstants.FILE_TYPE, fileType);
		policyDataMap.put(GatewayTestConstants.FILE_NAME, fileName);
		//PolicyAccessEnforcement.deletePolicy(suiteData, requestHeader, policyDataMap);
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

	
	/*	expectedResult.put("message", expectedMsg);
		//expectedResult.put("account_type", "Internal");
		
		String query = "$.hits.hits[*].source";
	       
	       String expectedkey = "message";
	       String expectedvalue = expectedMsg; //"User changed permissions for th";
	       
	       
	       
	       expectedResult.put(expectedkey, expectedvalue);
	       //expectedResult.put("Activity_type", "Update Link Sharin4");
	       expectedResult.put("transit_hosts", "");
	       //expectedResult.put("latitude", RawJsonParser.NotEmpty);
	      // expectedResult.put("req_size", "2322");
	       //expectedResult.put("version", RawJsonParser.NotZero);
	       
	       //expectedResult.put("user", "testuser3@gatewaybeatle.co4");
	       
	       
	       //expectedResult.put("resp_size", RawJsonParser);
	       
	       //expectedResult.put("transit_hosts", "");
	       
	       boolean findExpectedKeysAndPartialValues = RawJsonParser.findExpectedKeysAndPartialValues(fetchElasticSearchLogsUniversal(),query,expectedResult,"message");
		
		
		
	//	RawJsonParser.findExpectedKeysAndPartialValues(fetchElasticSearchLogsUniversal(),);
		assertTrue(findExpectedKeysAndPartialValues, "Test failed" ); 
	*/
		
	}
	
	@DataProvider
	public Object[][] _policySharingFile() {
		return new Object[][]{
			//Policy Name        App     Shared by  Share with  						admin     File Type    File Name      			Activity Type
			
			{ "FILE_SHARING1",    "Box",   "testuser1",  "__ALL_EL__", 	 "admin",  "doc",    	"computing",         "Policy Violation",  "File", "critical", "BoxFile,share_doc.log" },
			{ "FILE_SHARING2",    "Box",   "testuser1",  "__ALL_EL__",   "admin",  "docx",    	"GatewayTest1",      "Policy Violation",  "File", "critical", "BoxFile,share_docx.log"},
			{ "FILE_SHARING3",    "Box",   "testuser1",  "__ALL_EL__", 	 "admin",  "java",      "SequenceDetectorConstants",      "Policy Violation",  "File", "critical", "BoxFile,share_java.log"},
			{ "FILE_SHARING4",    "Box",   "testuser1",  "__ALL_EL__", 	 "admin",  "mp3",   	"audio",             "Policy Violation",  "File", "critical", "BoxFile,share_mp3.log" },
			{ "FILE_SHARING5",    "Box",   "testuser1",  "__ALL_EL__", 	"admin",   "pdf",    	"Test",              "Policy Violation",  "File", "critical",  "BoxFile,share_pdf.log" },
			{ "FILE_SHARING6",    "Box",   "testuser1",  "__ALL_EL__", 	"admin",   "py",    	"Sample",            "Policy Violation",  "File", "critical", "BoxFile,share_py.log"},
			{ "FILE_SHARING7",    "Box",   "testuser1",  "__ALL_EL__", 	"admin",   "txt",    	"business",          "Policy Violation",  "File", "critical", "BoxFile,share_txt.log" },
			{ "FILE_SHARING8",    "Box",   "testuser1",  "__ALL_EL__", 	"admin",   "xlsx",    	"GatewayTest",       "Policy Violation",  "File", "critical",    "BoxFile,share_xlsx.log" },
			{ "FILE_SHARING9",    "Box",   "testuser1",  "__ALL_EL__", 	"admin",   "html",    	"Test", "Policy Violation",  "File", "critical", "BoxFile,share_html.log" },
			{ "FILE_SHARING10",   "Box",   "testuser1",  "__ALL_EL__", 	"admin",   "js",    	"Test",        		          "Policy Violation",  "File", "critical", "BoxFile,share_js.log" },
			{ "FILE_SHARING11",   "Box",   "testuser1",  "__ALL_EL__", 	"admin",   "json",    	"Test",        		          "Policy Violation",  "File", "critical", "BoxFile,share_json.log" },
			{ "FILE_SHARING12",   "Box",   "testuser1",  "__ALL_EL__", 	"admin",   "key",    	"Test",        		          "Policy Violation",  "File", "critical", "BoxFile,share_key.log" },
			{ "FILE_SHARING13",   "Box",   "testuser1",  "__ALL_EL__", 	"admin",   "otp",    	"Test",        		          "Policy Violation",  "File", "critical", "BoxFile,share_otp.log" },
			{ "FILE_SHARING14",   "Box",   "testuser1",  "__ALL_EL__", 	"admin",   "7z",    	"Test",        		          "Policy Violation",  "File", "critical", "BoxFile,share_7z.log" },
			{ "FILE_SHARING15",   "Box",   "testuser1",  "__ALL_EL__", 	"admin",   "avi",    	"Test",        		          "Policy Violation",  "File", "critical", "BoxFile,share_avi.log" },
			{ "FILE_SHARING16",   "Box",   "testuser1",  "__ALL_EL__", 	"admin",   "bin",    	"Test",        		          "Policy Violation",  "File", "critical", "BoxFile,share_bin.log" },
			{ "FILE_SHARING17",   "Box",   "testuser1",  "__ALL_EL__", 	"admin",   "bmp",    	"Test",        		          "Policy Violation",  "File", "critical", "BoxFile,share_bmp.log" },
			{ "FILE_SHARING18",   "Box",   "testuser1",  "__ALL_EL__", 	"admin",   "bz2",    	"Test",        		          "Policy Violation",  "File", "critical", "BoxFile,share_bz2.log" },
			{ "FILE_SHARING19",   "Box",   "testuser1",  "__ALL_EL__", 	"admin",   "cs",    	"Test",        		          "Policy Violation",  "File", "critical", "BoxFile,share_cs.log" },
			{ "FILE_SHARING20",   "Box",   "testuser1",  "__ALL_EL__", 	"admin",   "csv",    	"Test",        		          "Policy Violation",  "File", "critical", "BoxFile,share_csv.log" },
			{ "FILE_SHARING21",   "Box",   "testuser1",  "__ALL_EL__", 	"admin",   "dmg",    	"Test",        		          "Policy Violation",  "File", "critical", "BoxFile,share_dmg.log" },
			{ "FILE_SHARING22",   "Box",   "testuser1",  "__ALL_EL__", 	"admin",   "exe",    	"Test",        		          "Policy Violation",  "File", "critical", "BoxFile,share_exe.log" },
			{ "FILE_SHARING23",   "Box",   "testuser1",  "__ALL_EL__", 	"admin",   "flac",    	"Test",        		          "Policy Violation",  "File", "critical", "BoxFile,share_flac.log" },
			{ "FILE_SHARING24",   "Box",   "testuser1",  "__ALL_EL__", 	"admin",   "flv",    	"Test",        		          "Policy Violation",  "File", "critical", "BoxFile,share_flv.log" },
			{ "FILE_SHARING25",   "Box",   "testuser1",  "__ALL_EL__", 	"admin",   "gif",    	"Test",        		          "Policy Violation",  "File", "critical", "BoxFile,share_gif.log" },
			{ "FILE_SHARING26",   "Box",   "testuser1",  "__ALL_EL__", 	"admin",   "gz",    	"Test",        		          "Policy Violation",  "File", "critical", "BoxFile,share_gz.log" },
			{ "FILE_SHARING27",   "Box",   "testuser1",  "__ALL_EL__", 	"admin",   "jpeg",    	"Test",        		          "Policy Violation",  "File", "critical", "BoxFile,share_jpeg.log" },
			{ "FILE_SHARING28",   "Box",   "testuser1",  "__ALL_EL__", 	"admin",   "jpg",    	"Test",        		          "Policy Violation",  "File", "critical", "BoxFile,share_jpg.log" },
			{ "FILE_SHARING29",   "Box",   "testuser1",  "__ALL_EL__", 	"admin",   "mov",    	"Test",        		          "Policy Violation",  "File", "critical", "BoxFile,share_mov.log" },
			{ "FILE_SHARING30",   "Box",   "testuser1",  "__ALL_EL__", 	"admin",   "mp4",    	"Test",        		          "Policy Violation",  "File", "critical", "BoxFile,share_mp4.log" },
			{ "FILE_SHARING31",   "Box",   "testuser1",  "__ALL_EL__", 	"admin",   "mpg",    	"Test",        		          "Policy Violation",  "File", "critical", "BoxFile,share_mpg.log" },
			{ "FILE_SHARING32",   "Box",   "testuser1",  "__ALL_EL__", 	"admin",   "numbers",    	"Test",        		          "Policy Violation",  "File", "critical", "BoxFile,share_numbers.log" },
			{ "FILE_SHARING33",   "Box",   "testuser1",  "__ALL_EL__", 	"admin",   "odg",    	"Test",        		          "Policy Violation",  "File", "critical", "BoxFile,share_odg.log" },
			{ "FILE_SHARING34",   "Box",   "testuser1",  "__ALL_EL__", 	"admin",   "odp",    	"Test",        		          "Policy Violation",  "File", "critical", "BoxFile,share_odp.log" },
			{ "FILE_SHARING35",   "Box",   "testuser1",  "__ALL_EL__", 	"admin",   "ods",    	"Test",        		          "Policy Violation",  "File", "critical", "BoxFile,share_ods.log" },
			{ "FILE_SHARING36",   "Box",   "testuser1",  "__ALL_EL__", 	"admin",   "otg",    	"Test",        		          "Policy Violation",  "File", "critical", "BoxFile,share_otg.log" },
			{ "FILE_SHARING37",   "Box",   "testuser1",  "__ALL_EL__", 	"admin",   "otp",    	"Test",        		          "Policy Violation",  "File", "critical", "BoxFile,share_otp.log" },
			{ "FILE_SHARING38",   "Box",   "testuser1",  "__ALL_EL__", 	"admin",   "ots",    	"Test",        		          "Policy Violation",  "File", "critical", "BoxFile,share_ots.log" },
			{ "FILE_SHARING39",   "Box",   "testuser1",  "__ALL_EL__", 	"admin",   "ott",    	"Test",        		          "Policy Violation",  "File", "critical", "BoxFile,share_ott.log" },
			{ "FILE_SHARING40",   "Box",   "testuser1",  "__ALL_EL__", 	"admin",   "pages",    	"Test",        		          "Policy Violation",  "File", "critical", "BoxFile,share_pages.log" },
			{ "FILE_SHARING41",   "Box",   "testuser1",  "__ALL_EL__", 	"admin",   "pem",    	"Test",        		          "Policy Violation",  "File", "critical", "BoxFile,share_pem.log" },
			{ "FILE_SHARING42",   "Box",   "testuser1",  "__ALL_EL__", 	"admin",   "png",    	"Test",        		          "Policy Violation",  "File", "critical", "BoxFile,share_png.log" },
			{ "FILE_SHARING43",   "Box",   "testuser1",  "__ALL_EL__", 	"admin",   "vb",    	"Test",        		          "Policy Violation",  "File", "critical", "BoxFile,share_vb.log" },
			{ "FILE_SHARING44",   "Box",   "testuser1",  "__ALL_EL__", 	"admin",   "xml",    	"Test",        		          "Policy Violation",  "File", "critical", "BoxFile,share_xml.log" },
			{ "FILE_SHARING45",   "Box",   "testuser1",  "__ALL_EL__", 	"admin",   "zip",    	"Test",        		          "Policy Violation",  "File", "critical", "BoxFile,share_zip.log" }
	

		};
	}
	
	
	@Test(groups ={"TEST"},dataProvider = "_policySharingFileAll")
	public void verifyFileSharingPolicyEnabledAll(String policyName, String saasApps,  String sharedBy,   String shareWith, String notifyEmailId, String fileType, String fileName,  String activityType, String objectType, String severity, String logFile) throws Exception{
		Reporter.log("Validate User Upload the file/folder to folder with Policy Enabled", true);
		testUserName=sharedBy+"@"+suiteData.getTenantDomainName();
		String expectedMsg="[ALERT] "+testUserName+ " attempted to share content:"+fileName.toLowerCase()+"."+fileType+"with external user(s):ALL_EL__ violating policy:"+policyName;
		
		//String expectedMsg="[ALERT] "+testUserName+ " attempted to share content:"+fileName.toLowerCase()+"."+fileType+"with external user(s):";
		System.out.println(expectedMsg);
		policyDataMap.clear();
		policyDataMap.put(GatewayTestConstants.POLICY_NAME, policyName);
		policyDataMap.put(GatewayTestConstants.APPLICATIONS, saasApps);
		policyDataMap.put(GatewayTestConstants.SHARED_BY,testUserName );
		policyDataMap.put(GatewayTestConstants.NOTIFY_EMAILID, notifyEmailId+"@"+suiteData.getTenantDomainName());
		policyDataMap.put(GatewayTestConstants.SHARE_WITH, shareWith);
		policyDataMap.put(GatewayTestConstants.FILE_TYPE, fileType);
		policyDataMap.put(GatewayTestConstants.FILE_NAME, fileName);
		//PolicyAccessEnforcement.deletePolicy(suiteData, requestHeader, policyDataMap);
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
	public Object[][] _policySharingFileAll() {
		return new Object[][]{
			//Policy Name        App     Shared by  Share with  						admin     File Type    File Name      			Activity Type
			
			{ "FILE_SHARING3",    "Box",   "testuser1",  "__ALL_EL__", 	 "admin",  "java",      "SequenceDetectorConstants",      "Policy Violation",  "File", "critical", "BoxFile,share_java.log"},
			{ "FILE_SHARING5",    "Box",   "testuser1",  "__ALL_EL__", 	"admin",   "pdf",    	"Test",              "Policy Violation",  "File", "critical",  "BoxFile,share_pdf.log" },
			{ "FILE_SHARING6",    "Box",   "testuser1",  "__ALL_EL__", 	"admin",   "py",    	"Sample",            "Policy Violation",  "File", "critical", "BoxFile,share_py.log"},
			{ "FILE_SHARING7",    "Box",   "testuser1",  "__ALL_EL__", 	"admin",   "txt",    	"business",          "Policy Violation",  "File", "critical", "BoxFile,share_txt.log" },
			{ "FILE_SHARING8",    "Box",   "testuser1",  "__ALL_EL__", 	"admin",   "xlsx",    	"GatewayTest",       "Policy Violation",  "File", "critical",    "BoxFile,share_xlsx.log" },


		};
	}
	
	
	
	@Test(groups ={"TEST"},dataProvider = "_policySharingFileWithEmail")
	public void verifyFileSharingPolicyWithEmailEnabled(String policyName, String saasApps,  String sharedBy,   String shareWith, String notifyEmailId, String fileType, String fileName,  String activityType, String objectType, String severity, String logFile) throws Exception{
		Reporter.log("Validate User Upload the file/folder to folder with Policy Enabled", true);
		testUserName=sharedBy+"@"+suiteData.getTenantDomainName();
		String expectedMsg="[ALERT] "+testUserName+ " attempted to share content:"+fileName.toLowerCase()+"."+fileType+"with external user(s):mohd.afjal@elastica.co violating policy:"+policyName;
		
		//String expectedMsg="[ALERT] "+testUserName+ " attempted to share content:"+fileName.toLowerCase()+"."+fileType+"with external user(s):";
		System.out.println(expectedMsg);
		policyDataMap.clear();
		policyDataMap.put(GatewayTestConstants.POLICY_NAME, policyName);
		policyDataMap.put(GatewayTestConstants.APPLICATIONS, saasApps);
		policyDataMap.put(GatewayTestConstants.SHARED_BY,testUserName );
		policyDataMap.put(GatewayTestConstants.NOTIFY_EMAILID, notifyEmailId+"@"+suiteData.getTenantDomainName());
		policyDataMap.put(GatewayTestConstants.SHARE_WITH, shareWith);
		policyDataMap.put(GatewayTestConstants.FILE_TYPE, fileType);
		policyDataMap.put(GatewayTestConstants.FILE_NAME, fileName);
		//PolicyAccessEnforcement.deletePolicy(suiteData, requestHeader, policyDataMap);
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

	
		/*expectedResult.put("message", expectedMsg);
		//expectedResult.put("account_type", "Internal");
		
		String query = "$.hits.hits[*].source";
	       
	       String expectedkey = "message";
	       String expectedvalue = expectedMsg; //"User changed permissions for th";
	       
	       
	       
	       expectedResult.put(expectedkey, expectedvalue);
	       //expectedResult.put("Activity_type", "Update Link Sharin4");
	       expectedResult.put("transit_hosts", "");
	       //expectedResult.put("latitude", RawJsonParser.NotEmpty);
	      // expectedResult.put("req_size", "2322");
	       //expectedResult.put("version", RawJsonParser.NotZero);
	       
	       //expectedResult.put("user", "testuser3@gatewaybeatle.co4");
	       
	       
	       //expectedResult.put("resp_size", RawJsonParser);
	       
	       //expectedResult.put("transit_hosts", "");
	       
	       boolean findExpectedKeysAndPartialValues = RawJsonParser.findExpectedKeysAndPartialValues(fetchElasticSearchLogsUniversal(),query,expectedResult,"message");
		
		
		
	//	RawJsonParser.findExpectedKeysAndPartialValues(fetchElasticSearchLogsUniversal(),);
		assertTrue(findExpectedKeysAndPartialValues, "Test failed" ); 
	*/
		
	
	
	}
	
	@DataProvider
	public Object[][] _policySharingFileWithEmail() {
		return new Object[][]{
			//Policy Name        App     Shared by  Share with  						admin     File Type    File Name      			Activity Type
			
			{ "FILE_SHARING1",   "Box",   "testuser1",  "mohd.afjal@elastica.co", 	    "admin",   "doc",    	"computing",                  "Policy Violation",  "File", "critical", "BoxFile,share_doc.log" },
			{ "FILE_SHARING2",   "Box",   "testuser1",  "mohd.afjal@elastica.co", 		"admin",   "docx",    	"GatewayTest1",      		  "Policy Violation",  "File", "critical", "BoxFile,share_docx.log" },
			{ "FILE_SHARING3",   "Box",   "testuser1",  "mohd.afjal@elastica.co", 		"admin",   "java",    	"SequenceDetectorConstants",  "Policy Violation",  "File", "critical", "BoxFile,share_java.log" },
			{ "FILE_SHARING4",   "Box",   "testuser1",  "mohd.afjal@elastica.co", 		"admin",   "mp3",   	"audio",   				      "Policy Violation",  "File", "critical", "BoxFile,share_mp3.log" },
			{ "FILE_SHARING5",   "Box",   "testuser1",  "mohd.afjal@elastica.co", 		"admin",   "pdf",    	"Test",    				      "Policy Violation",  "File", "critical", "BoxFile,share_pdf.log" },
			{ "FILE_SHARING6",   "Box",   "testuser1",  "mohd.afjal@elastica.co", 		"admin",   "py",    	"Sample",					  "Policy Violation",  "File", "critical", "BoxFile,share_py.log" },
			{ "FILE_SHARING7",   "Box",   "testuser1",  "mohd.afjal@elastica.co", 		"admin",   "txt",    	"business",        			  "Policy Violation",  "File", "critical", "BoxFile,share_txt.log" },
			{ "FILE_SHARING8",   "Box",   "testuser1",  "mohd.afjal@elastica.co", 		"admin",   "xlsx",    	"GatewayTest",        		  "Policy Violation",  "File", "critical", "BoxFile,share_xlsx.log" },
			{ "FILE_SHARING9",   "Box",   "testuser1",  "mohd.afjal@elastica.co", 		"admin",   "html",    	"Test",        		          "Policy Violation",  "File", "critical", "BoxFile,share_html.log" },
			{ "FILE_SHARING10",   "Box",   "testuser1",  "mohd.afjal@elastica.co", 		"admin",   "js",    	"Test",        		          "Policy Violation",  "File", "critical", "BoxFile,share_js.log" },
			{ "FILE_SHARING11",   "Box",   "testuser1",  "mohd.afjal@elastica.co", 		"admin",   "json",    	"Test",        		          "Policy Violation",  "File", "critical", "BoxFile,share_json.log" },
			{ "FILE_SHARING12",   "Box",   "testuser1",  "mohd.afjal@elastica.co", 		"admin",   "key",    	"Test",        		          "Policy Violation",  "File", "critical", "BoxFile,share_key.log" },
			{ "FILE_SHARING13",   "Box",   "testuser1",  "mohd.afjal@elastica.co", 		"admin",   "otp",    	"Test",        		          "Policy Violation",  "File", "critical", "BoxFile,share_otp.log" },
			{ "FILE_SHARING14",   "Box",   "testuser1",  "mohd.afjal@elastica.co", 		"admin",   "7z",    	"Test",        		          "Policy Violation",  "File", "critical", "BoxFile,share_7z.log" },
			{ "FILE_SHARING15",   "Box",   "testuser1",  "mohd.afjal@elastica.co", 		"admin",   "avi",    	"Test",        		          "Policy Violation",  "File", "critical", "BoxFile,share_avi.log" },
			{ "FILE_SHARING16",   "Box",   "testuser1",  "mohd.afjal@elastica.co", 		"admin",   "bin",    	"Test",        		          "Policy Violation",  "File", "critical", "BoxFile,share_bin.log" },
			{ "FILE_SHARING17",   "Box",   "testuser1",  "mohd.afjal@elastica.co", 		"admin",   "bmp",    	"Test",        		          "Policy Violation",  "File", "critical", "BoxFile,share_bmp.log" },
			{ "FILE_SHARING18",   "Box",   "testuser1",  "mohd.afjal@elastica.co", 		"admin",   "bz2",    	"Test",        		          "Policy Violation",  "File", "critical", "BoxFile,share_bz2.log" },
			{ "FILE_SHARING19",   "Box",   "testuser1",  "mohd.afjal@elastica.co", 		"admin",   "cs",    	"Test",        		          "Policy Violation",  "File", "critical", "BoxFile,share_cs.log" },
			{ "FILE_SHARING20",   "Box",   "testuser1",  "mohd.afjal@elastica.co", 		"admin",   "csv",    	"Test",        		          "Policy Violation",  "File", "critical", "BoxFile,share_csv.log" },
			{ "FILE_SHARING21",   "Box",   "testuser1",  "mohd.afjal@elastica.co", 		"admin",   "dmg",    	"Test",        		          "Policy Violation",  "File", "critical", "BoxFile,share_dmg.log" },
			{ "FILE_SHARING22",   "Box",   "testuser1",  "mohd.afjal@elastica.co", 		"admin",   "exe",    	"Test",        		          "Policy Violation",  "File", "critical", "BoxFile,share_exe.log" },
			{ "FILE_SHARING23",   "Box",   "testuser1",  "mohd.afjal@elastica.co", 		"admin",   "flac",    	"Test",        		          "Policy Violation",  "File", "critical", "BoxFile,share_flac.log" },
			{ "FILE_SHARING24",   "Box",   "testuser1",  "mohd.afjal@elastica.co", 		"admin",   "flv",    	"Test",        		          "Policy Violation",  "File", "critical", "BoxFile,share_flv.log" },
			{ "FILE_SHARING25",   "Box",   "testuser1",  "mohd.afjal@elastica.co", 		"admin",   "gif",    	"Test",        		          "Policy Violation",  "File", "critical", "BoxFile,share_gif.log" },
			{ "FILE_SHARING26",   "Box",   "testuser1",  "mohd.afjal@elastica.co", 		"admin",   "gz",    	"Test",        		          "Policy Violation",  "File", "critical", "BoxFile,share_gz.log" },
			{ "FILE_SHARING27",   "Box",   "testuser1",  "mohd.afjal@elastica.co", 		"admin",   "jpeg",    	"Test",        		          "Policy Violation",  "File", "critical", "BoxFile,share_jpeg.log" },
			{ "FILE_SHARING28",   "Box",   "testuser1",  "mohd.afjal@elastica.co", 		"admin",   "jpg",    	"Test",        		          "Policy Violation",  "File", "critical", "BoxFile,share_jpg.log" },
			{ "FILE_SHARING29",   "Box",   "testuser1",  "mohd.afjal@elastica.co", 		"admin",   "mov",    	"Test",        		          "Policy Violation",  "File", "critical", "BoxFile,share_mov.log" },
			{ "FILE_SHARING30",   "Box",   "testuser1",  "mohd.afjal@elastica.co", 		"admin",   "mp4",    	"Test",        		          "Policy Violation",  "File", "critical", "BoxFile,share_mp4.log" },
			{ "FILE_SHARING31",   "Box",   "testuser1",  "mohd.afjal@elastica.co", 		"admin",   "mpg",    	"Test",        		          "Policy Violation",  "File", "critical", "BoxFile,share_mpg.log" },
			{ "FILE_SHARING32",   "Box",   "testuser1",  "mohd.afjal@elastica.co", 		"admin",   "numbers",    	"Test",        		          "Policy Violation",  "File", "critical", "BoxFile,share_numbers.log" },
			{ "FILE_SHARING33",   "Box",   "testuser1",  "mohd.afjal@elastica.co", 		"admin",   "odg",    	"Test",        		          "Policy Violation",  "File", "critical", "BoxFile,share_odg.log" },
			{ "FILE_SHARING34",   "Box",   "testuser1",  "mohd.afjal@elastica.co", 		"admin",   "odp",    	"Test",        		          "Policy Violation",  "File", "critical", "BoxFile,share_odp.log" },
			{ "FILE_SHARING35",   "Box",   "testuser1",  "mohd.afjal@elastica.co", 		"admin",   "ods",    	"Test",        		          "Policy Violation",  "File", "critical", "BoxFile,share_ods.log" },
			{ "FILE_SHARING36",   "Box",   "testuser1",  "mohd.afjal@elastica.co", 		"admin",   "otg",    	"Test",        		          "Policy Violation",  "File", "critical", "BoxFile,share_otg.log" },
			{ "FILE_SHARING37",   "Box",   "testuser1",  "mohd.afjal@elastica.co", 		"admin",   "otp",    	"Test",        		          "Policy Violation",  "File", "critical", "BoxFile,share_otp.log" },
			{ "FILE_SHARING38",   "Box",   "testuser1",  "mohd.afjal@elastica.co", 		"admin",   "ots",    	"Test",        		          "Policy Violation",  "File", "critical", "BoxFile,share_ots.log" },
			{ "FILE_SHARING39",   "Box",   "testuser1",  "mohd.afjal@elastica.co", 		"admin",   "ott",    	"Test",        		          "Policy Violation",  "File", "critical", "BoxFile,share_ott.log" },
			{ "FILE_SHARING40",   "Box",   "testuser1",  "mohd.afjal@elastica.co", 		"admin",   "pages",    	"Test",        		          "Policy Violation",  "File", "critical", "BoxFile,share_pages.log" },
			{ "FILE_SHARING41",   "Box",   "testuser1",  "mohd.afjal@elastica.co", 		"admin",   "pem",    	"Test",        		          "Policy Violation",  "File", "critical", "BoxFile,share_pem.log" },
			{ "FILE_SHARING42",   "Box",   "testuser1",  "mohd.afjal@elastica.co", 		"admin",   "png",    	"Test",        		          "Policy Violation",  "File", "critical", "BoxFile,share_png.log" },
			{ "FILE_SHARING43",   "Box",   "testuser1",  "mohd.afjal@elastica.co", 		"admin",   "vb",    	"Test",        		          "Policy Violation",  "File", "critical", "BoxFile,share_vb.log" },
			{ "FILE_SHARING44",   "Box",   "testuser1",  "mohd.afjal@elastica.co", 		"admin",   "xml",    	"Test",        		          "Policy Violation",  "File", "critical", "BoxFile,share_xml.log" },
			{ "FILE_SHARING45",   "Box",   "testuser1",  "mohd.afjal@elastica.co", 		"admin",   "zip",    	"Test",        		          "Policy Violation",  "File", "critical", "BoxFile,share_zip.log" }
		};
	}
	
	
	@Test(groups ={"TEST"},dataProvider = "_policySharingFileGenericFileType")
	public void verifyFileSharingPolicyGenericFileTypeEnabled(String policyName, String saasApps,  String sharedBy,   String shareWith, String notifyEmailId, String genericFileType, String fileType, String fileName,  String activityType, String objectType, String severity, String logFile) throws Exception{
		Reporter.log("Validate User Upload the file/folder to folder with Policy Enabled", true);
		testUserName=sharedBy+"@"+suiteData.getTenantDomainName();
		String expectedMsg="[ALERT] "+testUserName+ " attempted to share content:"+fileName.toLowerCase()+"."+fileType+"with external user(s):mohd.afjal@elastica.co violating policy:"+policyName;
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
	public Object[][] _policySharingFileGenericFileType() {
		return new Object[][]{
			//Policy Name        App     Shared by  Share with  						admin     File Type    File Name      			Activity Type
			
			{ "FILE_SHARING1",   "Box",   "testuser1",  "__ALL_EL__", 	"admin",  "__ALL_EL__",  "doc",    	"computing",                 "Policy Violation",  "File", "critical", "BoxFile,share_doc.log" },
			{ "FILE_SHARING2",    "Box",   "testuser1",  "__ALL_EL__",   "admin", "__ALL_EL__",  "docx",    "GatewayTest1",              "Policy Violation",  "File", "critical", "BoxFile,share_docx.log"},
			{ "FILE_SHARING3",    "Box",   "testuser1",  "__ALL_EL__", 	"admin",  "__ALL_EL__",  "java",    "SequenceDetectorConstants", "Policy Violation",  "File", "critical", "BoxFile,share_java.log" },
			{ "FILE_SHARING4",    "Box",   "testuser1",  "__ALL_EL__", 	"admin",  "__ALL_EL__",  "mp3",   	"audio",                     "Policy Violation",  "File", "critical", "BoxFile,share_mp3.log" },
			{ "FILE_SHARING5",    "Box",   "testuser1",  "__ALL_EL__", 	"admin", "__ALL_EL__",   "pdf",    	"Test",                      "Policy Violation",  "File", "critical", "BoxFile,share_pdf.log" },
			{ "FILE_SHARING6",    "Box",   "testuser1",  "__ALL_EL__", 	"admin",  "__ALL_EL__",  "py",    	"Sample",                    "Policy Violation",  "File", "critical", "BoxFile,share_py.log" },
			{ "FILE_SHARING7",    "Box",   "testuser1",  "__ALL_EL__", 	"admin", "__ALL_EL__",   "txt",    	"business",                  "Policy Violation",  "File", "critical", "BoxFile,share_txt.log" },
			{ "FILE_SHARING8",    "Box",   "testuser1",  "__ALL_EL__", 	"admin", "__ALL_EL__",   "xlsx",    "GatewayTest",               "Policy Violation",  "File", "critical", "BoxFile,share_xlsx.log" },
			{ "FILE_SHARING9",    "Box",   "testuser1",  "__ALL_EL__", 	"admin", "__ALL_EL__",   "html",    "Test",                      "Policy Violation",  "File", "critical", "BoxFile,share_html.log" },
			{ "FILE_SHARING10",   "Box",   "testuser1",  "__ALL_EL__", 	"admin",  "__ALL_EL__",   "js", 	 "Test",        		     "Policy Violation",  "File", "critical", "BoxFile,share_js.log" },
			{ "FILE_SHARING11",   "Box",   "testuser1",  "__ALL_EL__", 	"admin",  "__ALL_EL__",   "json",	  "Test",        		      "Policy Violation",  "File", "critical", "BoxFile,share_json.log" },
			{ "FILE_SHARING12",   "Box",   "testuser1",  "__ALL_EL__", 	"admin",  "__ALL_EL__",   "key",	         "Test",        		      "Policy Violation",  "File", "critical", "BoxFile,share_key.log" },
			{ "FILE_SHARING13",   "Box",   "testuser1",  "__ALL_EL__", 	"admin",  "__ALL_EL__",   "otp",         "Test",        		      "Policy Violation",  "File", "critical", "BoxFile,share_otp.log" },
			{ "FILE_SHARING14",   "Box",   "testuser1",  "__ALL_EL__", 	"admin",  "__ALL_EL__",   "7z",         "Test",        		      "Policy Violation",  "File", "critical", "BoxFile,share_7z.log" },
			{ "FILE_SHARING15",   "Box",   "testuser1",  "__ALL_EL__", 	"admin",  "__ALL_EL__",   "avi",        "Test",        		      "Policy Violation",  "File", "critical", "BoxFile,share_avi.log" },
			{ "FILE_SHARING16",   "Box",   "testuser1",  "__ALL_EL__", 	"admin",  "__ALL_EL__",    "bin",        	 "Test",        		      "Policy Violation",  "File", "critical", "BoxFile,share_bin.log" },
			{ "FILE_SHARING17",   "Box",   "testuser1",  "__ALL_EL__", 	"admin",  "__ALL_EL__",    "bmp",	         "Test",        		      "Policy Violation",  "File", "critical", "BoxFile,share_bmp.log" },
			{ "FILE_SHARING18",   "Box",   "testuser1",  "__ALL_EL__", 	"admin",  "__ALL_EL__",    	 "bz2",        "Test",        		      "Policy Violation",  "File", "critical", "BoxFile,share_bz2.log" },
			{ "FILE_SHARING19",   "Box",   "testuser1",  "__ALL_EL__", 	"admin",  "__ALL_EL__",    	 "cs",        "Test",        		      "Policy Violation",  "File", "critical", "BoxFile,share_cs.log" },
			{ "FILE_SHARING20",   "Box",   "testuser1",  "__ALL_EL__", 	"admin",  "__ALL_EL__",    	 "csv",        "Test",        		      "Policy Violation",  "File", "critical", "BoxFile,share_csv.log" },
			{ "FILE_SHARING21",   "Box",   "testuser1",  "__ALL_EL__", 	"admin",  "__ALL_EL__",      "dmg",  	     "Test",        		      "Policy Violation",  "File", "critical", "BoxFile,share_dmg.log" },
			{ "FILE_SHARING22",   "Box",   "testuser1",  "__ALL_EL__", 	"admin",  "__ALL_EL__",    	 "exe",        "Test",        		      "Policy Violation",  "File", "critical", "BoxFile,share_exe.log" },
			{ "FILE_SHARING23",   "Box",   "testuser1",  "__ALL_EL__", 	"admin",  "__ALL_EL__",    	 "flac",       "Test",        		      "Policy Violation",  "File", "critical", "BoxFile,share_flac.log" },
			{ "FILE_SHARING24",   "Box",   "testuser1",  "__ALL_EL__", 	"admin",  "__ALL_EL__",    	 "flv",        "Test",        		      "Policy Violation",  "File", "critical", "BoxFile,share_flv.log" },
			{ "FILE_SHARING25",   "Box",   "testuser1",  "__ALL_EL__", 	"admin",  "__ALL_EL__",      "gif",     	 "Test",        		      "Policy Violation",  "File", "critical", "BoxFile,share_gif.log" },
			{ "FILE_SHARING26",   "Box",   "testuser1",  "__ALL_EL__", 	"admin",  "__ALL_EL__",    	 "gz",        "Test",        		      "Policy Violation",  "File", "critical", "BoxFile,share_gz.log" },
			{ "FILE_SHARING27",   "Box",   "testuser1",  "__ALL_EL__", 	"admin",  "__ALL_EL__",    	 "jpeg",        "Test",        		      "Policy Violation",  "File", "critical", "BoxFile,share_jpeg.log" },
			{ "FILE_SHARING28",   "Box",   "testuser1",  "__ALL_EL__", 	"admin",  "__ALL_EL__",    	 "jpg",        "Test",        		      "Policy Violation",  "File", "critical", "BoxFile,share_jpg.log" },
			{ "FILE_SHARING29",   "Box",   "testuser1",  "__ALL_EL__", 	"admin",  "__ALL_EL__",    	  "mov",       "Test",        		      "Policy Violation",  "File", "critical", "BoxFile,share_mov.log" },
			{ "FILE_SHARING30",   "Box",   "testuser1",  "__ALL_EL__", 	"admin",  "__ALL_EL__",    	  "mp4",       "Test",        		      "Policy Violation",  "File", "critical", "BoxFile,share_mp4.log" },
			{ "FILE_SHARING31",   "Box",   "testuser1",  "__ALL_EL__", 	"admin",  "__ALL_EL__",        "mpg",      "Test",        		      "Policy Violation",  "File", "critical", "BoxFile,share_mpg.log" },
			{ "FILE_SHARING32",   "Box",   "testuser1",  "__ALL_EL__", 	"admin",  "__ALL_EL__",    	  "numbers",       "Test",        		      "Policy Violation",  "File", "critical", "BoxFile,share_numbers.log" },
			{ "FILE_SHARING33",   "Box",   "testuser1",  "__ALL_EL__", 	"admin",  "__ALL_EL__",    "odg",	         "Test",        		      "Policy Violation",  "File", "critical", "BoxFile,share_odg.log" },
			{ "FILE_SHARING34",   "Box",   "testuser1",  "__ALL_EL__", 	"admin",  "__ALL_EL__",    "odp",	         "Test",        		      "Policy Violation",  "File", "critical", "BoxFile,share_odp.log" },
			{ "FILE_SHARING35",   "Box",   "testuser1",  "__ALL_EL__", 	"admin",  "__ALL_EL__",    	"ods",         "Test",        		      "Policy Violation",  "File", "critical", "BoxFile,share_ods.log" },
			{ "FILE_SHARING36",   "Box",   "testuser1",  "__ALL_EL__", 	"admin",  "__ALL_EL__",    	"otg",         "Test",        		      "Policy Violation",  "File", "critical", "BoxFile,share_otg.log" },
			{ "FILE_SHARING37",   "Box",   "testuser1",  "__ALL_EL__", 	"admin",  "__ALL_EL__",    	"otp",         "Test",        		      "Policy Violation",  "File", "critical", "BoxFile,share_otp.log" },
			{ "FILE_SHARING38",   "Box",   "testuser1",  "__ALL_EL__", 	"admin",  "__ALL_EL__",    	 "ots",        "Test",        		      "Policy Violation",  "File", "critical", "BoxFile,share_ots.log" },
			{ "FILE_SHARING39",   "Box",   "testuser1",  "__ALL_EL__", 	"admin",  "__ALL_EL__",    	 "ott",        "Test",        		      "Policy Violation",  "File", "critical", "BoxFile,share_ott.log" },
			{ "FILE_SHARING40",   "Box",   "testuser1",  "__ALL_EL__", 	"admin",  "__ALL_EL__",    	 "pages",        "Test",        		      "Policy Violation",  "File", "critical", "BoxFile,share_pages.log" },
			{ "FILE_SHARING41",   "Box",   "testuser1",  "__ALL_EL__", 	"admin",  "__ALL_EL__",    	 "pem",        "Test",        		      "Policy Violation",  "File", "critical", "BoxFile,share_pem.log" },
			{ "FILE_SHARING42",   "Box",   "testuser1",  "__ALL_EL__", 	"admin",  "__ALL_EL__",    	 "png",        "Test",        		      "Policy Violation",  "File", "critical", "BoxFile,share_png.log" },
			{ "FILE_SHARING43",   "Box",   "testuser1",  "__ALL_EL__", 	"admin",  "__ALL_EL__",    	 "vb",        "Test",        		       "Policy Violation",  "File", "critical", "BoxFile,share_vb.log" },
			{ "FILE_SHARING44",   "Box",   "testuser1",  "__ALL_EL__", 	"admin",  "__ALL_EL__",    	 "xml",        "Test",        		       "Policy Violation",  "File", "critical", "BoxFile,share_xml.log" },
			{ "FILE_SHARING45",   "Box",   "testuser1",  "__ALL_EL__", 	"admin",  "__ALL_EL__",      "zip",        "Test",        		       "Policy Violation",  "File", "critical", "BoxFile,share_zip.log" }
	

		};
	}
	
	
	@Test(groups ={"TEST"},dataProvider = "_policySharingFileGenericFileTypeAll")
	public void verifyFileSharingPolicyGenericFileTypeEnabledAll(String policyName, String saasApps,  String sharedBy,   String shareWith, String notifyEmailId, String genericFileType, String fileType, String fileName,  String activityType, String objectType, String severity, String logFile) throws Exception{
		Reporter.log("Validate User Upload the file/folder to folder with Policy Enabled", true);
		testUserName=sharedBy+"@"+suiteData.getTenantDomainName();
		String expectedMsg="[ALERT] "+testUserName+ " attempted to share content:"+fileName.toLowerCase()+"."+fileType+"with external user(s):mohd.afjal@elastica.co violating policy:"+policyName;
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
	public Object[][] _policySharingFileGenericFileTypeAll() {
		return new Object[][]{
			//Policy Name        App     Shared by  Share with  						admin     File Type    File Name      			Activity Type
			
			{ "FILE_SHARING1",   "Box",   "testuser1",  "__ALL_EL__", 	"admin",  "__ALL_EL__",  "doc",    	"computing",                 "Policy Violation",  "File", "critical", "BoxFile,share_doc.log" },
			{ "FILE_SHARING3",    "Box",   "testuser1",  "__ALL_EL__", 	"admin",  "__ALL_EL__",  "java",    "SequenceDetectorConstants", "Policy Violation",  "File", "critical", "BoxFile,share_java.log" },
			{ "FILE_SHARING7",    "Box",   "testuser1",  "__ALL_EL__", 	"admin", "__ALL_EL__",   "txt",    	"business",                  "Policy Violation",  "File", "critical", "BoxFile,share_txt.log" },
			{ "FILE_SHARING8",    "Box",   "testuser1",  "__ALL_EL__", 	"admin", "__ALL_EL__",   "xlsx",    "GatewayTest",               "Policy Violation",  "File", "critical", "BoxFile,share_xlsx.log" },
			
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
		//PolicyAccessEnforcement.deletePolicy(suiteData, requestHeader, policyDataMap);
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
	public Object[][] _policySharingFileGenericFileName() {
		return new Object[][]{
			//Policy Name        App     Shared by  Share with  						admin     File Type    File Name      			Activity Type
			
			{ "FILE_SHARING1",   "Box",   "testuser1",  "__ALL_EL__", 	"admin",  "__ALL_EL__",  "doc",    	"ANY", "Policy Violation",  "File", "critical", "BoxFile,share_doc.log" },
/*			{ "FILE_SHARING2",    "Box",   "testuser1",  "__ALL_EL__",   "admin", "__ALL_EL__",  "docx",    "ANY", "Policy Violation",  "File", "critical", "BoxFile,share_docx.log"},
			{ "FILE_SHARING3",    "Box",   "testuser1",  "__ALL_EL__", 	"admin",  "__ALL_EL__",  "java",    "ANY", "Policy Violation",  "File", "critical", "BoxFile,share_java.log" },
			{ "FILE_SHARING4",    "Box",   "testuser1",  "__ALL_EL__", 	"admin",  "__ALL_EL__",  "mp3",   	"ANY",                     "Policy Violation",  "File", "critical", "BoxFile,share_mp3.log" },
			{ "FILE_SHARING5",    "Box",   "testuser1",  "__ALL_EL__", 	"admin", "__ALL_EL__",   "pdf",    	"ANY",                      "Policy Violation",  "File", "critical", "BoxFile,share_pdf.log" },
			{ "FILE_SHARING6",    "Box",   "testuser1",  "__ALL_EL__", 	"admin",  "__ALL_EL__",  "py",    	"ANY",                    "Policy Violation",  "File", "critical", "BoxFile,share_py.log" },
			{ "FILE_SHARING7",    "Box",   "testuser1",  "__ALL_EL__", 	"admin", "__ALL_EL__",   "txt",    	"ANY",                  "Policy Violation",  "File", "critical", "BoxFile,share_txt.log" },
			{ "FILE_SHARING8",    "Box",   "testuser1",  "__ALL_EL__", 	"admin", "__ALL_EL__",   "xlsx",    "ANY",               "Policy Violation",  "File", "critical", "BoxFile,share_xlsx.log" },
			{ "FILE_SHARING9",    "Box",   "testuser1",  "__ALL_EL__", 	"admin", "__ALL_EL__",   "html",    "ANY",                      "Policy Violation",  "File", "critical", "BoxFile,share_html.log" },
			{ "FILE_SHARING10",   "Box",   "testuser1",  "__ALL_EL__", 	"admin",  "__ALL_EL__",   "js", 	 "ANY",        		     "Policy Violation",  "File", "critical", "BoxFile,share_js.log" },
			{ "FILE_SHARING11",   "Box",   "testuser1",  "__ALL_EL__", 	"admin",  "__ALL_EL__",   "json",	  "ANY",        		      "Policy Violation",  "File", "critical", "BoxFile,share_json.log" },
			{ "FILE_SHARING12",   "Box",   "testuser1",  "__ALL_EL__", 	"admin",  "__ALL_EL__",   "key",	   "ANY",        		      "Policy Violation",  "File", "critical", "BoxFile,share_key.log" },
			{ "FILE_SHARING13",   "Box",   "testuser1",  "__ALL_EL__", 	"admin",  "__ALL_EL__",   "otp",       "ANY",        		      "Policy Violation",  "File", "critical", "BoxFile,share_otp.log" },
			{ "FILE_SHARING14",   "Box",   "testuser1",  "__ALL_EL__", 	"admin",  "__ALL_EL__",   "7z",        "ANY",        		      "Policy Violation",  "File", "critical", "BoxFile,share_7z.log" },
			{ "FILE_SHARING15",   "Box",   "testuser1",  "__ALL_EL__", 	"admin",  "__ALL_EL__",   "avi",        "ANY",        		      "Policy Violation",  "File", "critical", "BoxFile,share_avi.log" },
			{ "FILE_SHARING16",   "Box",   "testuser1",  "__ALL_EL__", 	"admin",  "__ALL_EL__",    "bin",        "ANY",        		      "Policy Violation",  "File", "critical", "BoxFile,share_bin.log" },
			{ "FILE_SHARING17",   "Box",   "testuser1",  "__ALL_EL__", 	"admin",  "__ALL_EL__",    "bmp",	      "ANY",        		      "Policy Violation",  "File", "critical", "BoxFile,share_bmp.log" },
			{ "FILE_SHARING18",   "Box",   "testuser1",  "__ALL_EL__", 	"admin",  "__ALL_EL__",    	"bz2",        "ANY",        		      "Policy Violation",  "File", "critical", "BoxFile,share_bz2.log" },
			{ "FILE_SHARING19",   "Box",   "testuser1",  "__ALL_EL__", 	"admin",  "__ALL_EL__",    	 "cs",        "ANY",        		      "Policy Violation",  "File", "critical", "BoxFile,share_cs.log" },
			{ "FILE_SHARING20",   "Box",   "testuser1",  "__ALL_EL__", 	"admin",  "__ALL_EL__",    	 "csv",        "ANY",        		      "Policy Violation",  "File", "critical", "BoxFile,share_csv.log" },
			{ "FILE_SHARING21",   "Box",   "testuser1",  "__ALL_EL__", 	"admin",  "__ALL_EL__",      "dmg",  	   "ANY",        		      "Policy Violation",  "File", "critical", "BoxFile,share_dmg.log" },
			{ "FILE_SHARING22",   "Box",   "testuser1",  "__ALL_EL__", 	"admin",  "__ALL_EL__",    	 "exe",        "ANY",        		      "Policy Violation",  "File", "critical", "BoxFile,share_exe.log" },
			{ "FILE_SHARING23",   "Box",   "testuser1",  "__ALL_EL__", 	"admin",  "__ALL_EL__",    	 "flac",       "ANY",        		      "Policy Violation",  "File", "critical", "BoxFile,share_flac.log" },
			{ "FILE_SHARING24",   "Box",   "testuser1",  "__ALL_EL__", 	"admin",  "__ALL_EL__",    	 "flv",        "ANY",        		      "Policy Violation",  "File", "critical", "BoxFile,share_flv.log" },
			{ "FILE_SHARING25",   "Box",   "testuser1",  "__ALL_EL__", 	"admin",  "__ALL_EL__",      "gif",        "ANY",        		      "Policy Violation",  "File", "critical", "BoxFile,share_gif.log" },
			{ "FILE_SHARING26",   "Box",   "testuser1",  "__ALL_EL__", 	"admin",  "__ALL_EL__",    	 "gz",         "ANY",        		      "Policy Violation",  "File", "critical", "BoxFile,share_gz.log" },
			{ "FILE_SHARING27",   "Box",   "testuser1",  "__ALL_EL__", 	"admin",  "__ALL_EL__",    	 "jpeg",        "ANY",        		      "Policy Violation",  "File", "critical", "BoxFile,share_jpeg.log" },
			{ "FILE_SHARING28",   "Box",   "testuser1",  "__ALL_EL__", 	"admin",  "__ALL_EL__",    	 "jpg",        "ANY",        		      "Policy Violation",  "File", "critical", "BoxFile,share_jpg.log" },
			{ "FILE_SHARING29",   "Box",   "testuser1",  "__ALL_EL__", 	"admin",  "__ALL_EL__",    	  "mov",       "ANY",        		      "Policy Violation",  "File", "critical", "BoxFile,share_mov.log" },
			{ "FILE_SHARING30",   "Box",   "testuser1",  "__ALL_EL__", 	"admin",  "__ALL_EL__",    	  "mp4",       "ANY",        		      "Policy Violation",  "File", "critical", "BoxFile,share_mp4.log" },
			{ "FILE_SHARING31",   "Box",   "testuser1",  "__ALL_EL__", 	"admin",  "__ALL_EL__",        "mpg",      "ANY",        		      "Policy Violation",  "File", "critical", "BoxFile,share_mpg.log" },
			{ "FILE_SHARING32",   "Box",   "testuser1",  "__ALL_EL__", 	"admin",  "__ALL_EL__",    	  "numbers",    "ANY",        		      "Policy Violation",  "File", "critical", "BoxFile,share_numbers.log" },
			{ "FILE_SHARING33",   "Box",   "testuser1",  "__ALL_EL__", 	"admin",  "__ALL_EL__",       "odg",	    "ANY",        		      "Policy Violation",  "File", "critical", "BoxFile,share_odg.log" },
			{ "FILE_SHARING34",   "Box",   "testuser1",  "__ALL_EL__", 	"admin",  "__ALL_EL__",       "odp",	    "ANY",        		      "Policy Violation",  "File", "critical", "BoxFile,share_odp.log" },
			{ "FILE_SHARING35",   "Box",   "testuser1",  "__ALL_EL__", 	"admin",  "__ALL_EL__",    	  "ods",         "ANY",        		      "Policy Violation",  "File", "critical", "BoxFile,share_ods.log" },
			{ "FILE_SHARING36",   "Box",   "testuser1",  "__ALL_EL__", 	"admin",  "__ALL_EL__",    	  "otg",         "ANY",        		      "Policy Violation",  "File", "critical", "BoxFile,share_otg.log" },
			{ "FILE_SHARING37",   "Box",   "testuser1",  "__ALL_EL__", 	"admin",  "__ALL_EL__",    	  "otp",         "ANY",        		      "Policy Violation",  "File", "critical", "BoxFile,share_otp.log" },
			{ "FILE_SHARING38",   "Box",   "testuser1",  "__ALL_EL__", 	"admin",  "__ALL_EL__",    	 "ots",          "ANY",        		      "Policy Violation",  "File", "critical", "BoxFile,share_ots.log" },
			{ "FILE_SHARING39",   "Box",   "testuser1",  "__ALL_EL__", 	"admin",  "__ALL_EL__",    	 "ott",          "ANY",        		      "Policy Violation",  "File", "critical", "BoxFile,share_ott.log" },
			{ "FILE_SHARING40",   "Box",   "testuser1",  "__ALL_EL__", 	"admin",  "__ALL_EL__",    	 "pages",        "ANY",        		      "Policy Violation",  "File", "critical", "BoxFile,share_pages.log" },
			{ "FILE_SHARING41",   "Box",   "testuser1",  "__ALL_EL__", 	"admin",  "__ALL_EL__",    	 "pem",          "ANY",        		      "Policy Violation",  "File", "critical", "BoxFile,share_pem.log" },
			{ "FILE_SHARING42",   "Box",   "testuser1",  "__ALL_EL__", 	"admin",  "__ALL_EL__",    	 "png",          "ANY",        		      "Policy Violation",  "File", "critical", "BoxFile,share_png.log" },
			{ "FILE_SHARING43",   "Box",   "testuser1",  "__ALL_EL__", 	"admin",  "__ALL_EL__",    	 "vb",           "ANY",        		       "Policy Violation",  "File", "critical", "BoxFile,share_vb.log" },
	*/	//	{ "FILE_SHARING44",   "Box",   "testuser1",  "__ALL_EL__", 	"admin",  "__ALL_EL__",    	 "xml",          "ANY",        		       "Policy Violation",  "File", "critical", "BoxFile,share_xml.log" },
		//	{ "FILE_SHARING45",   "Box",   "testuser1",  "__ALL_EL__", 	"admin",  "__ALL_EL__",      "zip",          "ANY",        		       "Policy Violation",  "File", "critical", "BoxFile,share_zip.log" }
	

			
		};
	}
	
}
