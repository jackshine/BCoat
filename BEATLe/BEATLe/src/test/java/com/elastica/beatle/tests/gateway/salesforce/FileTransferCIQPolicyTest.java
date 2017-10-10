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
 * @author usman
 */



public class FileTransferCIQPolicyTest extends CommonConfiguration {

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
	
	@BeforeMethod(alwaysRun=true)
	public void beforMethod(Method method) throws Exception {
		//deleteAllPolicies();
		Reporter.log("--------------------------------------------", true);
		Reporter.log("Deleting all policies", true);
		Reporter.log("--------------------------------------------", true);
	}
	
	@Test(groups ={"TEST","DEV"}, dataProvider = "_policyTransferWithRisk")
	public void verifyFileTransferPolicyWithRiskEnabled(String policyName, String saasApps,  String testUserName,  String notifyEmailId, String transferType, String fileType, String fileName, String vulnarabilityType, String vulnarabilityAssess, String contentIq, String activityType, String objectType, String severity, String riskFile, String logFile) throws Exception{
		Map <String, String>expectedFields= new HashMap<String, String>();
		Reporter.log("Validate User Upload the file/folder to folder with Policy Enabled", true);
		testUserName=testUserName+"@"+suiteData.getTenantDomainName();
		String expectedMsg="[ALERT] File "+riskFile.toLowerCase()+" upload violated policy - "+policyName;
		String expectedMsg1="File "+riskFile.toLowerCase()+" upload has risk(s) - "+vulnarabilityAssess;
		System.out.println("expectedMsg "+expectedMsg);
		System.out.println("expectedMsg1 "+expectedMsg1);
		
		policyDataMap.clear();
		policyDataMap.put(GatewayTestConstants.POLICY_NAME, policyName);
		policyDataMap.put(GatewayTestConstants.APPLICATIONS, saasApps);
		policyDataMap.put(GatewayTestConstants.TARGET_USER, testUserName);
		policyDataMap.put(GatewayTestConstants.NOTIFY_EMAILID, notifyEmailId+"@"+suiteData.getTenantDomainName());
		policyDataMap.put(GatewayTestConstants.TRANSFER_TYPE, transferType);
		policyDataMap.put(GatewayTestConstants.FILE_TYPE, fileType);
		policyDataMap.put(GatewayTestConstants.FILE_NAME, fileName);
		policyDataMap.put(GatewayTestConstants.VULNERABILITY_TYPE, vulnarabilityType);
		//policyDataMap.put(GatewayTestConstants.CONTENT_IQ, contentIq);
		//PolicyAccessEnforcement.fileTransferPolicyWithRiskContentIqCreateEnable(suiteData, requestHeader, policyDataMap);
		replayLogsSalesforceCIQ(logFile, riskFile);
		//replayCIQLogs(logFile, riskFile);
		expectedFields.put("message", expectedMsg);
		expectedFields.put("Risks", vulnarabilityType);
		expectedFields.put("Activity_type", "Policy Violation");
		expectedFields.put("policy_type", "FileTransfer");
		expectedFields.put("policy_action", "ALERT");
		expectedFields.put("severity", severity);
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), expectedFields)), "Logs does not match" );
		
		/*expectedFields.clear();
		expectedFields.put("message", expectedMsg1);
		expectedFields.put("Activity_type", "Content Inspection" );
		expectedFields.put("severity", severity);
		check=validateLogsFields(responce,expectedFields );
		Assert.assertTrue(check, "Test failing");*/
		
	}
	
	@DataProvider
	public Object[][] _policyTransferWithRisk() {
		return new Object[][]{
			//Policy Name                 App     Test User   admin     Object Access Activity Access      Activity Type     Object Type  Severity
			{ "FILE_TRANSFER_UPLOAD1",  "Salesforce",   "testuser1",  "admin",  "upload",  "txt",    "Test",    "GLBA","GLBA", null,    "Policy Violation",  "Upload", "critical", "glba.txt", "Salesforce,Files,upload_Test_txt.log" },
			{ "FILE_TRANSFER_UPLOAD1",  "Salesforce",   "testuser1",  "admin",  "upload",  "txt",    "Test",    "PII","PII", null,    "Policy Violation",  "Upload", "critical", "pii.rtf", "Salesforce,Files,upload_Test_txt.log" },
			{ "FILE_TRANSFER_UPLOAD1",  "Salesforce",   "testuser1",  "admin",  "upload",  "txt",    "Test",    "PCI","PCI", null,    "Policy Violation",  "Upload", "critical", "pci.txt", "Salesforce,Files,upload_Test_txt.log" },
			{ "FILE_TRANSFER_UPLOAD1",  "Salesforce",   "testuser1",  "admin",  "upload",  "txt",    "Test",    "FERPA","FERPA", null,    "Policy Violation",  "Upload", "critical", "FoothillSchedule.rtf", "Salesforce,Files,upload_Test_txt.log" },
			{ "FILE_TRANSFER_UPLOAD1",  "Salesforce",   "testuser1",  "admin",  "upload",  "txt",    "Test",    "HIPAA","PII, HIPAA", null,    "Policy Violation",  "Upload", "critical", "hipaa.txt", "Salesforce,Files,upload_Test_txt.log" },
			{ "FILE_TRANSFER_UPLOAD1",  "Salesforce",   "testuser1",  "admin",  "upload",  "txt",    "Test",    "Source Code","Source Code", null,    "Policy Violation",  "Upload", "critical", "Sample.py", "Salesforce,Files,upload_Test_txt.log" },
			
			
			//{ "FILE_TRANSFER_UPLOAD2",  "Box",   "testuser1",  "admin",  "upload",  "txt",    "Test",    "PCI","PCI", null,    "Policy Violation",  "Upload", "critical", "pci.txt", "Test,AFolder,upload_txt.log" },
			//{ "FILE_TRANSFER_UPLOAD3",  "Box",   "testuser1",  "admin",  "upload",  "txt",    "Test",    "GLBA","GLBA", null,    "Policy Violation",  "Upload", "critical", "glba.txt", "Test,AFolder,upload_txt.log" },
			//{ "FILE_TRANSFER_UPLOAD4",  "Box",   "testuser1",  "admin",  "upload",  "txt",    "Test",   "HIPAA", "PII, HIPAA",  null,    "Policy Violation",  "Upload", "critical", "hipaa.txt", "Test,AFolder,upload_txt.log" },
			//{ "FILE_TRANSFER_UPLOAD5",  "Box",   "testuser1",  "admin",  "upload",  "txt",    "Test",    "PII",  "PII", null,    "Policy Violation",  "Upload", "critical", "Sample.pdf", "Test,AFolder,upload_txt.log" },
			//{ "FILE_TRANSFER_UPLOAD6",  "Box",   "testuser1",  "admin",  "upload",  "txt",    "Test",    "Source Code","Source Code", null,    "Policy Violation",  "Upload", "critical", "Sample.py", "Test,AFolder,upload_txt.log" },
			//{ "FILE_TRANSFER_UPLOAD7",  "Box",   "testuser1",  "admin",  "upload",  "txt",    "Test",    "FERPA", "FERPA", null,    "Policy Violation",  "Upload", "critical", "FoothillSchedule.rtf", "Test,AFolder,upload_txt.log" },
		};
	}
	

	@Test(groups ={"DELETE_ALL"})
	public void deleteAllPolicies() throws Exception{
		Reporter.log("Validate User Upload the file/folder to folder with Policy Enabled", true);
		PolicyAccessEnforcement.deleteAllPolicy(suiteData,  requestHeader);
	}
	
}
