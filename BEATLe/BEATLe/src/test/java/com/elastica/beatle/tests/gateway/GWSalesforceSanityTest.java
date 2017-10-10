package com.elastica.beatle.tests.gateway;

import org.testng.annotations.Test;
import org.testng.annotations.DataProvider;
import static org.testng.Assert.assertTrue;
import java.io.IOException;
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


/*******************Author**************
 * 
 * @author Afjal
 * Every test has an id which is inherited from Elastica-ADB
 */

public class GWSalesforceSanityTest extends CommonConfiguration {

	String currentTimeInJodaFormat;
	GWForensicSearchResults fsr;
	ArrayList<String> messages = new ArrayList<String>();
	ArrayList<String> objectTypeList = new ArrayList<String>();
	ArrayList<String> objectNameList = new ArrayList<String>();
	ArrayList<String> severityList = new ArrayList<String>();
	LogValidator logValidator;
	String userLitral="User";
	Map <String, String> data = new HashMap<String, String>();
	String policyName="PolicyFT_FileType";
	Map<String, String>policyDataMap= new HashMap<String, String>(); 
	ProtectFunctions protectFunctions = new ProtectFunctions();
	Map<String, Object> expectedResult=new HashMap<>();
	
	/******************************************************************
	 * Validate the login,logout and invalid login functionality in Box
	 * @param activityType
	 * @param objectType
	 * @param severity
	 * @param expectedMsg
	 * @throws IOException
	 * @throws Exception
	 */
	
	
	@Test(groups ={"TEST","SANITY", "UPLOAD", "QAVPC","P1"},dataProvider = "_1147")
	public void verify_User_createAccount_1147(String activityType, String objectType, String objectName, String severity, String logFile) throws Exception{
		Reporter.log("Validate User created a new account with name", true);
		String expectedMsg="User created a new account with name";
		replayLogs(logFile);
		
		expectedResult.put("message", expectedMsg);
		expectedResult.put("account_type", "Internal");
		
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
		
	}
	
	@DataProvider
	public Object[][] _1147() {
		return new Object[][]{
			//Activity type  Object Type  Object Name, DestinationDolder  Severity  message
			{ "Create",    "Account", "Testelastica",  "informational", "Account,Salesforce,create.log"},
		};
	}
	
	
	@Test(groups ={"TEST","SANITY", "UPLOAD", "QAVPC","P1"},dataProvider = "_1596")
	public void verify_User_createContact_1596(String activityType, String objectType, String objectName, String severity, String logFile) throws Exception{
		Reporter.log("Validate User created contact named", true);
		String expectedMsg="User created a new contact with name "+objectName;
		replayLogs(logFile);
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(activityType, severity), data)), activityType+" Logs does not match" ); 
		
	}
	
	@DataProvider
	public Object[][] _1596() {
		return new Object[][]{
			//Activity type  Object Type  Object Name, DestinationDolder  Severity  message
			{ "Create",    "Contact", "Test Testcase",  "informational", "Contact,Salesforce,create.log"},
		};
	}
	
	
	@Test(groups ={"TEST","SANITY", "UPLOAD", "QAVPC","P1"},dataProvider = "_1093Lead")
	public void verify_User_createdLead_1093(String activityType, String objectType, String commentText, String severity, String logFile) throws Exception{
		Reporter.log("Validate User created a new lead", true);
	    String expectedMsg="User created a new lead with name "+commentText;
		replayLogs(logFile);
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
		
	}
	
	@DataProvider
	public Object[][] _1093Lead() {
		return new Object[][]{
			//Activity type  Object Type  Object Name   Severity  message
			{ "Create",    "Lead", "Test1 Testlead", "informational", "Lead,Salesforce,create.log"},
			
		};
	}
	
	@Test(groups ={"SANITY"},dataProvider = "_uploadSanity")
	public void verify_User_uploadFile_upload_Sanity(String activityType, String objectType, String objectName, String severity, String logFile) throws Exception{
		Reporter.log("Validate User uploaded the file", true);
		String expectedMsg="User uploaded file named "+objectName;
		replayLogs(logFile);
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
		
	}
	
	@DataProvider
	public Object[][] _uploadSanity() {
		return new Object[][]{
			//Activity type  Object Type  Object Name, DestinationDolder  Severity  message
			{ "Upload",    "File", "Test.doc", "informational", "Salesforce,Files,upload_Test_doc.log"},
			
			
		};
	}
	
	@Test(groups ={"TEST","SANITY", "UPLOAD", "QAVPC","P1"},dataProvider = "_shareFile")
	public void verify_User_uploadFile_shareFile(String activityType, String objectType, String objectName, String recp_name, String expectedMsg, String severity, String logFile) throws Exception{
		Reporter.log("Validate User shared the file", true);
		//String expectedMsg="User shared a file named "+objectName+" via public link";
		replayLogsEPDV3(logFile);
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
		
	}
	
	@DataProvider
	public Object[][] _shareFile() {
		return new Object[][]{
			//Activity type  Object Type  Object Name, recp_name message  Severity  Logfile
			{ "Share",    "File", "Test","Test group","User shared file  with group named Test group" , "informational", "file,share_with_group.log"},			
			
		};
	}
	
	
	
	@Test(groups ={"TEST","SANITY", "UPLOAD", "QAVPC","P1"},dataProvider = "_reportCreate")
	public void verify_User_createReport_report(String activityType, String objectType, String reportName, String severity, String logFile) throws Exception{
		Reporter.log("Validate User created the report", true);
		String expectedMsg="User created a new report "+reportName;
		replayLogs(logFile);
		data.put("message", expectedMsg);
		data.put("account_type", "Internal");
		assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
		
	}
	
	@DataProvider
	public Object[][] _reportCreate() {
		return new Object[][]{
			//Activity type  Object Type  Object Name, DestinationDolder  Severity  message
			{ "Create",  "Report", "Unsaved Report", "informational", "Report,Salesforce,create.log"}
			
		};
	}
	

	
}
