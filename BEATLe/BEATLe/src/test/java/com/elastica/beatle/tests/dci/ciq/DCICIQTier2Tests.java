package com.elastica.beatle.tests.dci.ciq;

import java.io.File;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.ArrayUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.entity.StringEntity;
import org.testng.Assert;
import org.testng.ITest;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.elastica.beatle.RestClient.ClientUtil;
import com.elastica.beatle.S3Utils.S3ActionHandler;
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

public class DCICIQTier2Tests extends DCICommonTest implements ITest {

	protected String mTestCaseName = "";
	DCIFunctions dciFunctions = null;
	ElasticSearchLogs esLogs = null;
	Map<String,String> folderInfo = new HashMap<String,String>();
	String uniqueId = UUID.randomUUID().toString();
	int mainCounter = 0;int testCounter = 0;

	/**********************************************TEST METHODS***********************************************/

	@Test(dataProvider = "dataUploadCompanyConfidential", groups ={"CompanyConfidential"})
	public void testDisplayLogsForRiskGeneratedForCompanyConfidential(TestParameters testParams) throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();

		Logger.info(
				"************************************Starting "+testParams.getTestName()+" for filename:" + testParams.getFileName() + " and saas app type:" + testParams.getSaasType()+ "******************");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description:"+testParams.getTestDescription());
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output****************");
		Logger.info("Filename:"+testParams.getFileName());Logger.info("Risk Type:ContentIQ Violations");
		Logger.info("CIQ Profilename:"+testParams.getContentIQProfileName());Logger.info("Terms:"+testParams.getTerms());
		Logger.info("*****************************************************");


		List<NameValuePair> headers = dciFunctions.getBasicHeaders(suiteData);		
		verifyDisplayLogsCompanyConfidential(dciFunctions, esLogs, headers, testParams.getFileName(), testParams.getSaasType(), 
				testParams.getContentIQProfileName(), testParams.getTerms());


		Logger.info(
				"************************************Completed "+testParams.getTestName()+" for filename:" + testParams.getFileName() + " and saas app type:" + testParams.getSaasType()+ "******************");

	}

	@Test(dataProvider = "dataUploadTitus", groups ={"Titus"})
	public void testDisplayLogsForRiskGeneratedForTitus(TestParameters testParams) throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();

		Logger.info(
				"************************************Starting "+testParams.getTestName()+" for filename:" + testParams.getFileName() + " and saas app type:" + testParams.getSaasType()+ "******************");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description:"+testParams.getTestDescription());
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output****************");
		Logger.info("Filename:"+testParams.getFileName());Logger.info("Risk Type:ContentIQ Violations");
		Logger.info("CIQ Profilename:"+testParams.getContentIQProfileName());Logger.info("Terms:"+testParams.getTerms());
		Logger.info("*****************************************************");


		List<NameValuePair> headers = dciFunctions.getBasicHeaders(suiteData);		
		verifyDisplayLogsTitus(dciFunctions, esLogs, headers, testParams.getFileName(), testParams.getSaasType(), 
				testParams.getContentIQProfileName(), testParams.getTerms(), testParams.getCount());

		Logger.info(
				"************************************Completed "+testParams.getTestName()+" for filename:" + testParams.getFileName() + " and saas app type:" + testParams.getSaasType()+ "******************");

	}

	@Test(dataProvider = "dataUploadForeignLanguage", groups ={"ForeignLanguage"})
	public void testDisplayLogsForRiskGeneratedInForeignLanguage(TestParameters testParams) throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();

		Logger.info(
				"************************************Starting "+testParams.getTestName()+" for filename:" + testParams.getFileName() + " and saas app type:" + testParams.getSaasType()+ "******************");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description:"+testParams.getTestDescription());
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output****************");
		Logger.info("Filename:"+testParams.getFileName());Logger.info("Risk Type:ContentIQ Violations");
		Logger.info("CIQ Profilename:"+testParams.getContentIQProfileName());
		Logger.info("Custom Regex Terms:"+testParams.getTerms());
		Logger.info("*****************************************************");


		List<NameValuePair> headers = dciFunctions.getBasicHeaders(suiteData);	
		verifyDisplayLogsForeignLanguage(dciFunctions, esLogs, headers, testParams.getFileName(), testParams.getSaasType(), 
				testParams.getContentIQProfileName(), testParams.getTerms(), testParams.getCount());

		Logger.info(
				"************************************Completed "+testParams.getTestName()+" for filename:" + testParams.getFileName() + " and saas app type:" + testParams.getSaasType()+ "******************");

	}

	@Test(dataProvider = "dataUploadPredefTerms", groups ={"PredefTerms"})
	public void testDisplayLogsForRiskGeneratedInPredefinedTerms(TestParameters testParams) throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();

		Logger.info(
				"************************************Starting "+testParams.getTestName()+" for filename:" + testParams.getFileName() + " and saas app type:" + testParams.getSaasType()+ "******************");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description:"+testParams.getTestDescription());
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output****************");
		Logger.info("Filename:"+testParams.getFileName());Logger.info("Risk Type:ContentIQ Violations");
		Logger.info("CIQ Profilename:"+testParams.getContentIQProfileName());Logger.info("Terms:"+testParams.getTerms());
		Logger.info("*****************************************************");


		List<NameValuePair> headers = dciFunctions.getBasicHeaders(suiteData);		
		verifyDisplayLogsPredefinedTerms(dciFunctions, esLogs, headers, testParams.getFileName(), testParams.getSaasType(), 
				testParams.getContentIQProfileName(), testParams.getTerms(), testParams.getCount());

		Logger.info(
				"************************************Completed "+testParams.getTestName()+" for filename:" + testParams.getFileName() + " and saas app type:" + testParams.getSaasType()+ "******************");

	}

	/**********************************************TEST METHODS***********************************************/
	/**********************************************DATA PROVIDERS*********************************************/

	@DataProvider(name = "dataUploadCompanyConfidential")
	public Object[][] dataUploadCompanyConfidential() {
		dciFunctions = new DCIFunctions();

		String[] fileName= dciFunctions.getFileName(DCIConstants.DCI_FILE_UPLOAD_CIQ_CC_PATH);
		String dictionaries = "Company Confidential";
		String ciqProfileName = "Company_Confidential";
		String ciqProfileDescription = "Company Confidential Description";


		try {
			dciFunctions.createContentIqProfile(restClient, suiteData, "PreDefDict", 
					ciqProfileName, ciqProfileDescription, dictionaries, false);

		} catch (Exception ex) {
			Logger.info("Issue with Create Content Iq Profiles" + ex.getLocalizedMessage());
		}

		fileName = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_CIQ_CC_PATH,
				fileName);
		dciFunctions.fileUploadOrSendEmail(suiteData, folderInfo, fileName);

		Object[][] result = new Object[fileName.length][];
		for (int i = 0; i < fileName.length; i++) {
			result[i] = new Object[] { new TestParameters("Risk Generation/Validation for Predefined Dictionaries:"+dictionaries+" in ContentIQ Profile:"+ciqProfileName+" for file:"+fileName[i], 
					"Create CIQ Profile with name:"+ciqProfileName+" with predefined dictionaries:"+dictionaries+" and upload file:"+fileName[i]+ ". Then verify risk logs are getting generated within the SLA provided",
					fileName[i], SaasType.getSaasFilterType(suiteData.getSaasApp()), ciqProfileName, dictionaries, "1")};
		}

		mainCounter = 0;testCounter = DCIConstants.DCI_COUNTER_MAX;

		return result;
	}

	@DataProvider(name = "dataUploadTitus")
	public Object[][] dataUploadTitus() {
		dciFunctions = new DCIFunctions();

		String[] fileName = dciFunctions.getFileName(DCIConstants.DCI_FILE_UPLOAD_CIQ_TITUS_PATH);
		String[] terms = dciFunctions.getCustomTermsTitus(fileName);
		String[] tags = dciFunctions.getCustomTagsTitus(fileName);

		Map<String,String> titusValues = dciFunctions.getTitusValues();
		try {

			for (String key : titusValues.keySet()) {
				dciFunctions.createCIQCustomTerms(restClient, suiteData, titusValues.get(key), key+"_Title", key+"_Description", 
						"high", 1, true, 1, false);
			}
			dciFunctions.waitForSeconds(10);

		} catch (Exception ex) {
			Logger.info("Issue with Create Content Iq Profiles" + ex.getLocalizedMessage());
		}

		fileName = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_CIQ_TITUS_PATH,
				fileName);
		dciFunctions.fileUploadOrSendEmail(suiteData, folderInfo, fileName);

		Object[][] result = new Object[fileName.length][];
		for (int i = 0; i < fileName.length; i++) {
			result[i] = new Object[] { new TestParameters("Risk Generation/Validation for Titus Custom Terms:"+terms[i]+" in ContentIQ Profile:"+terms[i]+"_Title for file:"+fileName[i], 
					"Create CIQ Profile with name:"+terms[i]+"_Title with custom regex terms:"+terms[i]+" and upload file:"+fileName[i]+ ". Then verify risk logs are getting generated within the SLA provided",
					fileName[i], SaasType.getSaasFilterType(suiteData.getSaasApp()), terms[i]+"_Title", tags[i], "1")};
		}

		mainCounter = 0;testCounter = DCIConstants.DCI_COUNTER_MAX;

		return result;
	}

	@DataProvider(name = "dataUploadForeignLanguage")
	public Object[][] dataUploadForeignLanguage() {
		dciFunctions = new DCIFunctions();

		String[] fileName= dciFunctions.getFileName(DCIConstants.DCI_FILE_UPLOAD_CIQ_FL_PATH);
		fileName = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_CIQ_FL_PATH,
				fileName);
		Map<String,String> flMap = dciFunctions.getCIQKeyValuesForeignLanguage();
		try {
			for (Map.Entry<String, String> entry : flMap.entrySet()) {
				String key = entry.getKey();String value = entry.getValue().split(":")[0];
				dciFunctions.createContentIqProfile(restClient, suiteData, "ForeignLanguage", 
						key, key+" Description", value, false);

			}
			dciFunctions.waitForSeconds(10);	
		} catch (Exception ex) {
			Logger.info("Issue with Create Content Iq Profiles" + ex.getLocalizedMessage());
		}

		dciFunctions.fileUploadOrSendEmail(suiteData, folderInfo, fileName);

		Object[][] result = new Object[fileName.length][];
		for (int i = 0; i < fileName.length; i++) {
			String ciqProfileName= dciFunctions.getCIQProfileName(flMap,fileName[i]);String ciqProfileTerm=flMap.get(ciqProfileName).split(":")[0]; ;String ciqProfileCount=flMap.get(ciqProfileName).split(":")[1];
			result[i] = new Object[] { new TestParameters("Risk Generation/Validation for Custom Terms for Foreign Language files:"+ciqProfileTerm+" in ContentIQ Profile:"+ciqProfileName+" for file:"+fileName[i], 
					"Create CIQ Profile with name:"+ciqProfileName+" with custom regex terms:"+ciqProfileTerm+" and upload file:"+fileName[i]+ ". Then verify risk logs are getting generated within the SLA provided",
					fileName[i], SaasType.getSaasFilterType(suiteData.getSaasApp()), ciqProfileName, ciqProfileTerm, ciqProfileCount)};
		}

		mainCounter = 0;testCounter = DCIConstants.DCI_COUNTER_MAX;

		return result;
	}

	@DataProvider(name = "dataUploadPredefTerms")
	public Object[][] dataUploadPredefTerms() {
		dciFunctions = new DCIFunctions();

		String[] terms = dciFunctions.predefinedTermsCIQProfileText();
		String[] ciqProfileNames = dciFunctions.predefinedTermsCIQProfileName(); 
		String[] fileNameExpected= dciFunctions.predefinedTermsCIQProfileFileName();

		String[] fileNameP1 = dciFunctions.getFileName(DCIConstants.DCI_FILE_UPLOAD_CIQ_P1_TERMS_PATH);
		String[] fileNameP2 = dciFunctions.getFileName(DCIConstants.DCI_FILE_UPLOAD_CIQ_P2_TERMS_PATH);

		dciFunctions.createAllCIQPredefinedTerms(restClient, suiteData);

		fileNameP1 = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_CIQ_P1_TERMS_PATH,
				fileNameP1);
		fileNameP2 = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_CIQ_P2_TERMS_PATH,
				fileNameP2);

		String[] fileName = (String[]) ArrayUtils.addAll(fileNameP1,fileNameP2);

		int[] fileIndices = dciFunctions.getIndex(fileNameExpected, fileName);
		String[] countTerms = dciFunctions.predefinedTermsCIQProfileCount();

		dciFunctions.fileUploadOrSendEmail(suiteData, folderInfo, fileName);

		Object[][] result = new Object[fileName.length][];
		for (int i = 0; i < fileName.length; i++) {
			int index = fileIndices[i];
			result[i] = new Object[] { new TestParameters("Risk Generation/Validation for Predefined Terms:"+terms[index]+" in ContentIQ Profile:"+ciqProfileNames[index]+" for file:"+fileName[i], 
					"Create CIQ Profile with name:"+ciqProfileNames[index]+" with predefined terms:"+terms[index]+" and upload file:"+fileName[i]+ ". Then verify risk logs are getting generated within the SLA provided",
					fileName[i], SaasType.getSaasFilterType(suiteData.getSaasApp()), ciqProfileNames[index], terms[index], countTerms[index])};
		}

		mainCounter = 0;testCounter = DCIConstants.DCI_COUNTER_MAX;

		return result;
	}


	/**********************************************DATA PROVIDERS*********************************************/
	/**********************************************UTIL METHODS***********************************************/

	@Override
	public String getTestName() {
		return this.mTestCaseName;
	}


	private void verifyDisplayLogsCompanyConfidential(DCIFunctions dciFunctions, ElasticSearchLogs esLogs, List<NameValuePair> headers, String fileName, 
			String saasType, String contentIQProfileName, String dictionaries)
					throws Exception {
		String hits = "";
		String[] keywords = dciFunctions.getCustomTagsCompanyConfidential(fileName).split("####");

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

			String validationMessage = dciFunctions.validationRiskLogsContentIQProfilePreDefinedDictionaries(hits, fileName, suiteData.getUsername(),
					"QA Admin", saasType, "API", "ContentIQ Violations", contentIQProfileName, dictionaries, keywords, 1);

			Assert.assertEquals(validationMessage, "", "Output Response Validation is failing for CIQ Profile with name:"+contentIQProfileName
					+" with predefined dictionaries:"+dictionaries+" and for fileName:"+fileName);



		} finally {
			dciFunctions.cleanupFileFromTempFolder(fileName);
		}
	}

	private void verifyDisplayLogsTitus(DCIFunctions dciFunctions, ElasticSearchLogs esLogs, List<NameValuePair> headers, String fileName, 
			String saasType, String contentIQProfileName, String terms, String count)
					throws Exception {
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

			String validationMessage = dciFunctions.validationRiskLogsContentIQProfileCustomTerms(hits, fileName, suiteData.getUsername(),
					"QA Admin", saasType, "API", "ContentIQ Violations", contentIQProfileName, terms, count);

			Assert.assertEquals(validationMessage, "", "Output Response Validation is failing for CIQ Profile with name:"+contentIQProfileName
					+" with predefined terms:"+terms+" and for fileName:"+fileName);



		} finally {
			dciFunctions.cleanupFileFromTempFolder(fileName);
		}
	}

	private void verifyDisplayLogsForeignLanguage(DCIFunctions dciFunctions, ElasticSearchLogs esLogs, List<NameValuePair> headers, String fileName, 
			String saasType, String contentIQProfileName, String terms, String count)
					throws Exception {
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

			String validationMessage = dciFunctions.validationRiskLogsContentIQProfileCustomTerms(hits, fileName, suiteData.getUsername(),
					"QA Admin", saasType, "API", "ContentIQ Violations", contentIQProfileName, terms, count);

			Assert.assertEquals(validationMessage, "", "Output Response Validation is failing for CIQ Profile with name:"+contentIQProfileName
					+" with predefined terms:"+terms+" and for fileName:"+fileName);



		} finally {
			dciFunctions.cleanupFileFromTempFolder(fileName);
		}
	}

	private void verifyDisplayLogsPredefinedTerms(DCIFunctions dciFunctions, ElasticSearchLogs esLogs, List<NameValuePair> headers, String fileName, 
			String saasType, String contentIQProfileName, String terms, String count)
					throws Exception {
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

			String validationMessage = dciFunctions.validationRiskLogsContentIQProfilePreDefinedTerms(hits, fileName, suiteData.getUsername(),
					"QA Admin", saasType, "API", "ContentIQ Violations", contentIQProfileName, terms, count, 1);

			Assert.assertEquals(validationMessage, "", "Output Response Validation is failing for CIQ Profile with name:"+contentIQProfileName
					+" with predefined terms:"+terms+" and for fileName:"+fileName);



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
	 * Create folders in saas apps
	 */
	@BeforeClass(groups ={"PredefTerms","CompanyConfidential","ForeignLanguage","Titus"})
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
	@AfterClass(groups ={"PredefTerms","CompanyConfidential","ForeignLanguage","Titus"})
	public void deleteFolder() {
		try {
			UserAccount account = dciFunctions.getUserAccount(suiteData);
			UniversalApi universalApi = dciFunctions.getUniversalApi(suiteData, account);

			dciFunctions.deleteFolder(universalApi, suiteData, folderInfo);
		} catch (Exception ex) {
			Logger.info("Issue with Delete Folder Operation " + ex.getLocalizedMessage());
		}
	}


	/**
	 * Delete content iq profile
	 */
	@BeforeClass(groups ={"PredefTerms","CompanyConfidential","ForeignLanguage","Titus"})
	public void deleteContentIqProfileBeforeTestStarts() {
		dciFunctions = new DCIFunctions();
		dciFunctions.deleteAllPolicies(restClient, suiteData);
		dciFunctions.deleteAllCIQProfiles(restClient, suiteData);
		dciFunctions.deleteAllDictionaries(restClient, suiteData);
		dciFunctions.deleteAllTrainingProfiles(restClient, suiteData);
	}

	/**
	 * Delete content iq profile
	 */
	@AfterClass(groups ={"PredefTerms","CompanyConfidential","ForeignLanguage","Titus"})
	public void deleteContentIqProfileAfterTestEnds() {
		dciFunctions = new DCIFunctions();
		dciFunctions.deleteAllPolicies(restClient, suiteData);
		dciFunctions.deleteAllCIQProfiles(restClient, suiteData);
		dciFunctions.deleteAllDictionaries(restClient, suiteData);
		dciFunctions.deleteAllTrainingProfiles(restClient, suiteData);
	}

	@BeforeMethod(groups ={"PredefTerms","CompanyConfidential","ForeignLanguage","Titus"})
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

	@BeforeSuite(alwaysRun=true)
	public void downloadFileFromS3() {
		try {
			S3ActionHandler s3 = new S3ActionHandler();
			s3.downloadWholeFolder(DCIConstants.DCI_S3_BUCKET, "DCI/CIQ/CompanyConfidential", 
					new File(DCIConstants.DCI_FILE_TEMP_PATH));
			s3.downloadWholeFolder(DCIConstants.DCI_S3_BUCKET, "DCI/CIQ/ForeignLanguage", 
					new File(DCIConstants.DCI_FILE_TEMP_PATH));
			s3.downloadWholeFolder(DCIConstants.DCI_S3_BUCKET, "DCI/CIQ/Titus", 
					new File(DCIConstants.DCI_FILE_TEMP_PATH));
			s3.downloadWholeFolder(DCIConstants.DCI_S3_BUCKET, "DCI/CIQ/P1Terms", 
					new File(DCIConstants.DCI_FILE_TEMP_PATH));
			s3.downloadWholeFolder(DCIConstants.DCI_S3_BUCKET, "DCI/CIQ/P2Terms", 
					new File(DCIConstants.DCI_FILE_TEMP_PATH));

		} catch (Exception ex) {
			Logger.info("Downloading folder from S3 is failed with exception " + ex.getLocalizedMessage());
		}
	}

	/**********************************************BEFORE/AFTER METHODS/CLASS*****************************************/


}