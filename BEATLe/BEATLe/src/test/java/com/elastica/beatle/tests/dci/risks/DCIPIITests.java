package com.elastica.beatle.tests.dci.risks;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.entity.StringEntity;
import org.testng.Assert;
import org.testng.ITest;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.elastica.beatle.RestClient.ClientUtil;
import com.elastica.beatle.dci.DCICommonTest;
import com.elastica.beatle.dci.DCIConstants;
import com.elastica.beatle.dci.DCIFunctions;
import com.elastica.beatle.dci.SaasType;
import com.elastica.beatle.dci.TestParameters;
import com.elastica.beatle.es.ElasticSearchLogs;
import com.elastica.beatle.logger.Logger;
import com.universal.common.UniversalApi;
import com.universal.dtos.UserAccount;

import net.sf.json.JSONArray;
import net.sf.json.util.JSONTokener;

/**
 * 
 * @author eldorajan
 *
 */

public class DCIPIITests extends DCICommonTest implements ITest {

	DCIFunctions dciFunctions = null;
	ElasticSearchLogs esLogs = null;
	Map<String,String> folderInfo = new HashMap<String,String>();
	String uniqueId = UUID.randomUUID().toString();
	protected String mTestCaseName = "";
	int mainCounter = 0;int testCounter = 0;

	/**********************************************TEST METHODS***********************************************/


	@Test(dataProvider = "dataUploadPIIPositiveScenarios", groups ={"All"})
	public void testDisplayLogsGenerateRiskForPIIRiskPositiveScenarios(TestParameters testParams) throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();

		Logger.info(
				"************************************Starting "+testParams.getTestName()+" for saas app type:" + testParams.getSaasType()+ "******************");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description:"+testParams.getTestDescription());
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output****************");
		Logger.info("Filename:"+testParams.getFileName());
		Logger.info("Risk Types to be validated:"+testParams.getRisks());
		Logger.info("Saas App Type:"+testParams.getSaasType());
		Logger.info("*****************************************************");

		String[] risks = testParams.getRisks().toArray(new String[testParams.getRisks().size()]);
		List<NameValuePair> headers = dciFunctions.getBasicHeaders(suiteData);		
		verifyDisplayLogs(dciFunctions, testParams.getFileName(), testParams.getSaasType(), headers, risks, testParams.getPrimaryJson());

		Logger.info(
				"************************************Completed "+testParams.getTestName()+" for saas app type:" + testParams.getSaasType()+ "******************");

	}


	@Test(dataProvider = "dataUploadPIINegativeScenarios", groups ={"All"})
	public void testDisplayLogsGenerateRiskForPIIRiskNegativeScenarios(TestParameters testParams) throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();

		Logger.info(
				"************************************Starting "+testParams.getTestName()+" for saas app type:" + testParams.getSaasType()+ "******************");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description:"+testParams.getTestDescription());
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output****************");
		Logger.info("Filename:"+testParams.getFileName());
		Logger.info("Risk Types to be validated:No risks to be validated");
		Logger.info("Saas App Type:"+testParams.getSaasType());
		Logger.info("*****************************************************");

		List<NameValuePair> headers = dciFunctions.getBasicHeaders(suiteData);		
		verifyDisplayLogs(dciFunctions, testParams.getFileName(), testParams.getSaasType(), headers);

		Logger.info(
				"************************************Completed "+testParams.getTestName()+" for saas app type:" + testParams.getSaasType()+ "******************");

	}
	/**********************************************TEST METHODS***********************************************/
	/**********************************************DATA PROVIDERS*********************************************/

	@DataProvider(name = "dataUploadPIIPositiveScenarios")
	public Object[][] dataUploadPIIPositiveScenarios() {

		dciFunctions = new DCIFunctions();

		String[] risksFileTypes = {
				"PII","PII","PII","PII","PII",
				"PII","PII","PII","PII","PII",
				"PII","PII","PII","PII","PII",
				"PII","PII","PII","PII",
		};
		String[] fileNameFileTypes = {
				"Sample_Pres.txt","Sample_Sheet.html","Sample_Sheet.pdf","Sample_Sheets.zip","Sample.doc",
				"Sample.docx","Sample.htm","Sample.html","Sample.ods","Sample.odt",
				"Sample.pdf","Sample.ppt","Sample.pptx","Sample.rtf","Sample.tsv",
				"Sample.txt","Sample.xls","Sample.xlsx","Sample.zip",

		};
		String[] titleFileTypes = new String[fileNameFileTypes.length];String[] descriptionFileTypes = new String[fileNameFileTypes.length];
		for(int i=0;i<fileNameFileTypes.length;i++){
			titleFileTypes[i] = "Risk Generation/Validation for PII risks file types:"+fileNameFileTypes[i]+" for risks"+Arrays.asList(risksFileTypes[i].split(","));
			descriptionFileTypes[i] = "Upload PII risks file:"+fileNameFileTypes[i]+ ". Then verify risk logs are getting generated within the SLA provided"+" for risks"+Arrays.asList(risksFileTypes[i].split(",")); 
		}

		String[] risksDOB = {
				"PII","PII","PII","PII","PII","PII","PII","PII","PII"
		};
		String[] fileNameDOB= {
				"DOBFormat1.txt","DOBFormat2.txt","DOBFormat3.txt","DOBFormat4.txt","DOBFormat5.txt","DOBFormat6.txt","DOBFormat7.txt","DOBFormat8.txt","DOBFormat9.txt"

		};
		String[] titleDOB = new String[fileNameDOB.length];String[] descriptionDOB = new String[fileNameDOB.length];
		for(int i=0;i<fileNameDOB.length;i++){
			titleDOB[i] = "Risk Generation/Validation for PII risks file types:"+fileNameDOB[i]+" for risks"+Arrays.asList(risksDOB[i].split(","));
			descriptionDOB[i] = "Upload PII risks file:"+fileNameDOB[i]+ ". Then verify risk logs are getting generated within the SLA provided"+" for risks"+Arrays.asList(risksDOB[i].split(",")); 
		}

		String[] risksPositive = {
				"PII","PII","PII","PII","PII","PII",
				"PII","PII","PII","PII","PII",
				"PII","PII","PII","PII","PII",
				"PII","PII","PII","PII","PII",
				"PII","PII","PII","PII",
				"PII","PII","PII","PII",
				"PII","PII","PII","PII",
				"PII","PII","PII","PII",
				"PII","PII","PII","PII","PII","PII","PII",
				"PII","PII","PII","PII","PII","PII","PII",
				"PII","PII","PII","PII","PII",
				"PII","PII","PII","PII","PII",
				"PII","PII","PII","PII","PII",


				"PII","PII","PII","PII","PII",
				"PII","PII","PII","PII","PII",
				"PII","PII","PII","PII","PII",
				"PII","PII","PII","PII","PII",
				"PII","PII","PII","PII","PII",
				"PII","PII","PII","PII","PII"

		};

		String[] fileNamePositive = {

				"Email_Age_DOB.txt",
				"Email_Name_Address_DOB.txt",
				"Email_Name_Age_DOB.txt",
				"Email_Address_DOB.txt",
				"Name_Address_DOB.txt",
				"Name_Age_DOB.txt",

				"SSN_Address_Age.txt",
				"SSN_Address_DOB.txt",
				"SSN_Address_Email_Name.txt",
				"SSN_Address_Email.txt",
				"SSN_Address_Name.txt",
				"SSN_Address.txt",
				"SSN_Age.txt",
				"SSN_DOB_Email_Name.txt",
				"SSN_DOB_Email.txt",
				"SSN_DOB_Name.txt",
				"SSN_DOB.txt",
				"SSN_Email_Age.txt",
				"SSN_Email_Name_Age.txt",
				"SSN_Email_Name.txt",
				"SSN_Email.txt",

				"SSN_NA_Address_Age.txt",
				"SSN_NA_Address_DOB.txt",
				"SSN_NA_Address_Email_Name.txt",
				"SSN_NA_Address_Email.txt",
				"SSN_NA_Address_Name.txt",
				"SSN_NA_DOB_Email_Name.txt",
				"SSN_NA_DOB_Email.txt",
				"SSN_NA_DOB_Name.txt",
				"SSN_NA_Email_Age.txt",
				"SSN_NA_Email_Name_Age.txt",
				"SSN_NA_Email_Name.txt",
				"SSN_NA_Name_Age.txt",
				"SSN_NA_Name.txt",
				"SSN_Name_Age.txt",
				"SSN_Name.txt",

				"SSN_Age_DOB.txt",
				"SSN_Name_Age_Address_DOB_Email.txt",
				"SSN_Name_Age_Address_DOB.txt",
				"SSN_Name_Age_Address_Email.txt",
				"SSN_Name_Age_DOB_Email.txt",
				"SSN_Name_Address_DOB_Email.txt",
				"SSN_Age_Address_DOB_Email.txt",
				"SSN_Name_Age_Address.txt",
				"SSN_Name_Age_DOB.txt",
				"SSN_Name_Address_DOB.txt",
				"SSN_Age_Address_DOB.txt",
				"SSN_Age_Address_Email.txt",
				"SSN_Age_DOB_Email.txt",
				"SSN_Address_DOB_Email.txt",

				"SSN_NA_Address_DOB_Email.txt",
				"SSN_NA_Age_Address_DOB_Email.txt",
				"SSN_NA_Age_Address_DOB.txt",
				"SSN_NA_Age_Address_Email.txt",
				"SSN_NA_Age_DOB_Email.txt",
				"SSN_NA_Age_DOB.txt",
				"SSN_NA_DOB.txt",
				"SSN_NA_Name_Address_DOB_Email.txt",
				"SSN_NA_Name_Address_DOB.txt",
				"SSN_NA_Name_Age_Address_DOB_Email.txt",
				"SSN_NA_Name_Age_Address_DOB.txt",
				"SSN_NA_Name_Age_Address_Email.txt",
				"SSN_NA_Name_Age_Address.txt",
				"SSN_NA_Name_Age_DOB_Email.txt",
				"SSN_NA_Name_Age_DOB.txt",

				"SSN_Short_Address_Age.txt",
				"SSN_Short_Address_DOB_Email.txt",
				"SSN_Short_Address_DOB.txt",
				"SSN_Short_Address_Email_Name.txt",
				"SSN_Short_Address_Email.txt",
				"SSN_Short_Address_Name.txt",
				"SSN_Short_Address.txt",
				"SSN_Short_Age_Address_DOB_Email.txt",
				"SSN_Short_Age_Address_DOB.txt",
				"SSN_Short_Age_Address_Email.txt",
				"SSN_Short_Age_DOB_Email.txt",
				"SSN_Short_Age_DOB.txt",
				"SSN_Short_Age.txt",
				"SSN_Short_DOB_Email_Name.txt",
				"SSN_Short_DOB_Email.txt",
				"SSN_Short_DOB_Name.txt",
				"SSN_Short_DOB.txt",
				"SSN_Short_Email_Age.txt",
				"SSN_Short_Email_Name_Age.txt",
				"SSN_Short_Email_Name.txt",
				"SSN_Short_Email.txt",
				"SSN_Short_Name_Address_DOB_Email.txt",
				"SSN_Short_Name_Address_DOB.txt",
				"SSN_Short_Name_Age_Address_DOB_Email.txt",
				"SSN_Short_Name_Age_Address_DOB.txt",
				"SSN_Short_Name_Age_Address_Email.txt",
				"SSN_Short_Name_Age_Address.txt",
				"SSN_Short_Name_Age_DOB_Email.txt",
				"SSN_Short_Name_Age_DOB.txt",
				"SSN_Short_Name_Age.txt",
				"SSN_Short_Name.txt"


		};

		String[] titlePositive = new String[fileNamePositive.length];String[] descriptionPositive = new String[fileNamePositive.length];
		for(int i=0;i<fileNamePositive.length;i++){
			titlePositive[i] = "Risk Generation/Validation for PII risks file types:"+fileNamePositive[i]+" for risks"+Arrays.asList(risksPositive[i].split(","));
			descriptionPositive[i] = "Upload PII risks file:"+fileNamePositive[i]+ ". Then verify risk logs are getting generated within the SLA provided"+" for risks"+Arrays.asList(risksPositive[i].split(",")); 
		}

		String[] fileName = dciFunctions.merge(fileNameFileTypes,fileNameDOB,fileNamePositive);
		String[] title = dciFunctions.merge(titleFileTypes,titleDOB,titlePositive);
		String[] description = dciFunctions.merge(descriptionFileTypes,descriptionDOB,descriptionPositive);
		String[] risks = dciFunctions.merge(risksFileTypes,risksDOB,risksPositive);

		fileName = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_PII_POS_PATH,
				fileName);

		String[] piiJson = new String[fileName.length];
		for (int i = 0; i < fileName.length; i++) {			
			piiJson[i] = dciFunctions.piiRisksForAFile(fileName[i]);		
		}


		dciFunctions.fileUploadOrSendEmail(suiteData, folderInfo, fileName);

		Object[][] result = new Object[fileName.length][];
		for (int i = 0; i < fileName.length; i++) {
			result[i] = new Object[] { new TestParameters(title[i], description[i], fileName[i], SaasType.getSaasFilterType(suiteData.getSaasApp()), 
					Arrays.asList(risks[i].split(",")), piiJson[i])};
		}

		mainCounter = 0;testCounter = DCIConstants.DCI_COUNTER_MAX;

		return result;
	}



	@DataProvider(name = "dataUploadPIINegativeScenarios")
	public Object[][] dataUploadPIINegativeScenarios() {

		dciFunctions = new DCIFunctions();

		String[] fileName= dciFunctions.getFileName(DCIConstants.DCI_FILE_UPLOAD_PII_NEG_PATH);
		fileName = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_PII_NEG_PATH,
				fileName);
		dciFunctions.fileUploadOrSendEmail(suiteData, folderInfo, fileName);

		Object[][] result = new Object[fileName.length][];
		for (int i = 0; i < fileName.length; i++) {
			result[i] = new Object[] { new TestParameters("No Risk Generation/Validation for PII risks file:"+fileName[i], 
					"Upload no risks file:"+fileName[i]+ ". Then verify risk logs are not getting generated within the SLA provided",
					fileName[i], SaasType.getSaasFilterType(suiteData.getSaasApp()))};
		}

		dciFunctions.waitForMinutes(1);

		return result;
	}

	/**********************************************DATA PROVIDERS*********************************************/
	/**********************************************UTIL METHODS***********************************************/

	@Override
	public String getTestName() {
		return this.mTestCaseName;
	}

	private void verifyDisplayLogs(DCIFunctions dciFunctions, String fileName, String saasType,
			List<NameValuePair> headers, String[] risks, String piiJson) throws Exception {
		String hits = "";
		try {

			String payload = 
					dciFunctions.getSearchQueryForDCI(suiteData, dciFunctions.getMinusMinutesFromCurrentTime(1440) , 
							dciFunctions.getPlusMinutesFromCurrentTime(120), saasType, DCIConstants.CISourceType, 
							DCIConstants.CICriticalSeverityType, fileName, DCIConstants.CIFacilityType, DCIConstants.CIActivityType);
			Logger.info("****************Input Payload****************");
			Logger.info(payload);
			Logger.info("*********************************************");

			hits = fetchDisplayLogsCounter(dciFunctions, headers, payload, DCIConstants.DCI_COUNTER_MAX);

			Logger.info("****************Output Response****************");
			Logger.info(hits);
			Logger.info("***********************************************");

			String validationMessage = dciFunctions.validationPIIRiskLogs(hits, fileName, suiteData.getUsername(),
					"QA Admin", saasType, "API", risks, piiJson);

			Assert.assertEquals(validationMessage, "", "Output Response Validation is failing for fileName:"+fileName+
					" for saas app type:"+saasType);


		} finally {
			dciFunctions.cleanupFileFromTempFolder(fileName);
		}
	}

	private void verifyDisplayLogs(DCIFunctions dciFunctions, String fileName, String saasType,
			List<NameValuePair> headers) throws Exception {
		String hits = "";String responseBody="";
		try {

			String payload = 
					dciFunctions.getSearchQueryForDCI(suiteData, dciFunctions.getMinusMinutesFromCurrentTime(1440) , 
							dciFunctions.getPlusMinutesFromCurrentTime(120), saasType, DCIConstants.CISourceType, 
							DCIConstants.CICriticalSeverityType, fileName, DCIConstants.CIFacilityType, DCIConstants.CIActivityType);

			Logger.info("****************Input Payload****************");
			Logger.info(payload);
			Logger.info("*********************************************");

			for (int i = 0; i < DCIConstants.DCI_COUNTER_MIN; i++) {
				HttpResponse response = esLogs.getDisplayLogs(restClient, headers, suiteData.getApiserverHostName(), new StringEntity(payload));
				responseBody = ClientUtil.getResponseBody(response);

				hits = dciFunctions.getJSONValue(dciFunctions.getJSONValue(responseBody, "hits"), "hits");
			}
			Logger.info("****************Output Response****************");
			Logger.info(hits);
			Logger.info("***********************************************");

			JSONArray jArray = (JSONArray) new JSONTokener(hits).nextValue();
			Assert.assertTrue(jArray.size() == 0, "Expected no risk logs for file upload of " + fileName+
					" but seeing some risks:"+Arrays.asList(jArray));


		} finally {
			dciFunctions.cleanupFileFromTempFolder(fileName);
		}
	}

	private String fetchDisplayLogsCounter(DCIFunctions dciFunctions, List<NameValuePair> headers,
			String payload, int maxLimit) throws Exception{
		String hits="";
		for (int i = 0; i < testCounter; i++,mainCounter++) {

			HttpResponse response = esLogs.getDisplayLogs(restClient, headers, suiteData.getApiserverHostName(), new StringEntity(payload));
			String responseBody = ClientUtil.getResponseBody(response);
			hits = dciFunctions.getJSONValue(dciFunctions.getJSONValue(responseBody, "hits"), "hits");

			JSONArray jArray = (JSONArray) new JSONTokener(hits).nextValue();
			if (jArray.size() < 1 && i < maxLimit) {
				if(mainCounter>=maxLimit){
					Logger.info("Counter limit is reached");break;
				}else{
					dciFunctions.waitForOneMinute(i+1);
				}
				continue;
			} else {
				break;
			}

		}

		if(mainCounter>=DCIConstants.DCI_COUNTER_UL){
			testCounter=DCIConstants.DCI_COUNTER_LL;
		}
		return hits;
	}

	/**********************************************UTIL METHODS***********************************************/
	/**********************************************BEFORE/AFTER CLASS*****************************************/

	/**
	 * Delete all CIQ profiles
	 */
	@BeforeClass(groups ={"All"})
	public void deleteAllCIQProfiles() {
		dciFunctions = new DCIFunctions();
		dciFunctions.deleteAllPolicies(restClient, suiteData);
		dciFunctions.deleteAllCIQProfiles(restClient, suiteData);
		dciFunctions.deleteAllDictionaries(restClient, suiteData);
		dciFunctions.deleteAllTrainingProfiles(restClient, suiteData);
	}

	/**
	 * Create folders in saas apps
	 */
	@BeforeClass(groups ={"All"})
	public void createFolder() {
		dciFunctions = new DCIFunctions();
		try {
			UserAccount account = dciFunctions.getUserAccount(suiteData);
			UniversalApi universalApi = dciFunctions.getUniversalApi(suiteData, account);

			folderInfo = dciFunctions.createFolder(universalApi, suiteData, DCIConstants.DCI_FOLDER+uniqueId);
		} catch (Exception ex) {
			Logger.info("Issue with Create Folder Operation " + ex.getLocalizedMessage());
		}
	}

	/**
	 * Delete folders in saas apps
	 */
	@AfterClass(groups ={"All"})
	public void deleteFolder() {
		try {
			UserAccount account = dciFunctions.getUserAccount(suiteData);
			UniversalApi universalApi = dciFunctions.getUniversalApi(suiteData, account);

			dciFunctions.deleteFolder(universalApi, suiteData, folderInfo);
		} catch (Exception ex) {
			Logger.info("Issue with Delete Folder Operation " + ex.getLocalizedMessage());
		}
	}

	@BeforeMethod(groups ={"All"})
	public void testData(Method method, Object[] testData) {
		String testCase = "";
		if (testData != null && testData.length > 0) {
			TestParameters testParams = null;
			//Check if test method has actually received required parameters
			for (Object testParameter : testData) {
				if (testParameter instanceof TestParameters) {
					testParams = (TestParameters)testParameter;
					break;
				}
			}
			if (testParams != null) {
				testCase = testParams.getTestName();
			}
		}
		this.mTestCaseName = String.format("%s(%s)", method.getName(), testCase);

	}

	/**********************************************BEFORE/AFTER CLASS*****************************************/


}