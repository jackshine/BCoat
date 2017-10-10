package com.elastica.beatle.tests.dci.risks;

import java.lang.reflect.Method;
import java.util.ArrayList;
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

public class DCISourceCodeTests extends DCICommonTest implements ITest {

	DCIFunctions dciFunctions = null;
	ElasticSearchLogs esLogs = null;
	Map<String,String> folderInfo = new HashMap<String,String>();
	String uniqueId = UUID.randomUUID().toString();
	protected String mTestCaseName = "";
	int mainCounter = 0;int testCounter = 0;

	/**********************************************TEST METHODS***********************************************/

	/**
	 * This test cases uploads a risk file into a saas app and validates if risk log is generated, 
	 * subsequently it is validated for it's risk types and doc classification
	 * @param fileName
	 * @param saasType
	 * @throws Exception
	 */
	@Test(dataProvider = "dataUploadFileFormats", groups ={"All"})
	public void testDisplayLogsGenerateRiskIntoOtherFileTypes(TestParameters testParams) throws Exception {
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
		verifyDisplayLogs(dciFunctions, testParams.getFileName(), testParams.getSaasType(), headers, risks);

		Logger.info(
				"************************************Completed "+testParams.getTestName()+" for saas app type:" + testParams.getSaasType()+ "******************");

	}

	@Test(dataProvider = "dataUploadFiles", groups ={"All"})
	public void testDisplayLogsGenerateRiskForFiles(TestParameters testParams) throws Exception {
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
		verifyDisplayLogs(dciFunctions, testParams.getFileName(), testParams.getSaasType(), headers, risks);

		Logger.info(
				"************************************Completed "+testParams.getTestName()+" for saas app type:" + testParams.getSaasType()+ "******************");

	}

	@Test(dataProvider = "dataUploadNoRisks", groups ={"All"})
	public void testDisplayLogsGenerateRiskForNoRisks(TestParameters testParams) throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();

		Logger.info(
				"************************************Starting "+testParams.getTestName()+" for saas app type:" + testParams.getSaasType()+ "******************");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description:"+testParams.getTestDescription());
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output****************");
		Logger.info("Filename:"+testParams.getFileName());
		Logger.info("No risks should be generated");
		Logger.info("Saas App Type:"+testParams.getSaasType());
		Logger.info("*****************************************************");

		List<NameValuePair> headers = dciFunctions.getBasicHeaders(suiteData);		
		verifyDisplayLogs(dciFunctions, testParams.getFileName(), testParams.getSaasType(), headers);

		Logger.info(
				"************************************Completed "+testParams.getTestName()+" for saas app type:" + testParams.getSaasType()+ "******************");

	}

	/**********************************************TEST METHODS***********************************************/
	/**********************************************DATA PROVIDERS*********************************************/

	@DataProvider(name = "dataUploadFileFormats")
	public Object[][] dataUploadFileFormats() {
		dciFunctions = new DCIFunctions();

		String[] risks = {"source_code"};
		String[] fileName= {"Sample.java","Sample.py","Sample.vb","Sample.json",
				"Sample.c","Sample.js"};

		Map<String,List<String>> fileMap = new HashMap<String,List<String>>();
		List<String> fileList = new ArrayList<String>();
		for (int i = 0; i < fileName.length; i++) {

			fileList = new ArrayList<String>();

			fileList.add(dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_SOURCE_CODE_PATH,
					fileName[i],"doc"));

			fileList.add(dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_SOURCE_CODE_PATH,
					fileName[i],"txt"));

			fileList.add(dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_SOURCE_CODE_PATH,
					fileName[i],"xls"));

			fileList.add(dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_SOURCE_CODE_PATH,
					fileName[i],"csv"));

			fileList.add(dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_SOURCE_CODE_PATH,
					fileName[i],"rtf"));

			fileList.add(dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_SOURCE_CODE_PATH,
					fileName[i],"jpg"));

			fileList.add(dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_SOURCE_CODE_PATH,
					fileName[i],"mov"));

			fileList.add(dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_SOURCE_CODE_PATH,
					fileName[i],"html"));


			fileMap.put(fileName[i], fileList);

		}

		List<TestParameters> list = new ArrayList<TestParameters>();

		UserAccount account = dciFunctions.getUserAccount(suiteData);
		UniversalApi universalApi = dciFunctions.getUniversalApi(suiteData, account);
		for (int i = 0; i < fileName.length; i++) {
			for (int j = 0; j < fileList.size(); j++) {
				dciFunctions.uploadFile(universalApi,suiteData, folderInfo.get("folderId"), fileMap.get(fileName[i]).get(j));

				list.add(new TestParameters(
						"Risk Generation/Validation in for actual file:"+fileName[i]+" converted to file:"+fileMap.get(fileName[i]).get(j)+" for validation of following risks:"+Arrays.asList(risks), 
						"Upload converted source code risk file:"+fileMap.get(fileName[i]).get(j)+ ". Then verify source code risk logs are getting generated within the SLA provided",
						fileMap.get(fileName[i]).get(j), SaasType.getSaasFilterType(suiteData.getSaasApp()), Arrays.asList(risks)));


			}
		}

		int dpLength = fileName.length*fileList.size();
		Logger.info("Test case will run for " + dpLength + " files");
		Object[][] result = new Object[dpLength][];
		for (int i = 0; i < dpLength; i++) {
			result[i] = new Object[]{list.get(i)};
		}

		mainCounter = 0;testCounter = DCIConstants.DCI_COUNTER_MAX;

		return result;
	}

	@DataProvider(name = "dataUploadFiles")
	public Object[][] dataUploadFiles() {

		dciFunctions = new DCIFunctions();

		String[] risks = {"source_code"};

		String[] fileNameCompressed= {"Sample.7z","Sample.tar","Sample.tbz2","Sample.zip","Sample.tgz"};
		String[] fileNameLanguageSyntax= {"Sample.7z","Sample.tar","Sample.tbz2","Sample.zip","Sample.tgz","Sample.boxnote"};
		String[] fileNameFalsePositive= {"FalsePositive.as","FalsePositive.c","FalsePositive.cpp","FalsePositive.cs","FalsePositive.go",
				"FalsePositive.java","FalsePositive.js","FalsePositive.php","FalsePositive.pl","FalsePositive.py","FalsePositive.rb","FalsePositive2.cpp"};

		String[] titleCompressed = new String[fileNameCompressed.length];
		String[] descriptionCompressed = new String[fileNameCompressed.length];
		for(int i=0;i<fileNameCompressed.length;i++){
			titleCompressed[i] = "Risk Generation/Validation for compressed file:"+fileNameCompressed[i]+" for risks"+Arrays.asList(risks);
			descriptionCompressed[i] = "Upload compressed file:"+fileNameCompressed[i]+ ". Then verify risk logs are getting generated within the SLA provided"+" for risks"+Arrays.asList(risks); 
		}
		String[] titleLanguageSyntax = new String[fileNameLanguageSyntax.length];
		String[] descriptionLanguageSyntax = new String[fileNameLanguageSyntax.length];
		for(int i=0;i<fileNameLanguageSyntax.length;i++){
			titleLanguageSyntax[i] = "Risk Generation/Validation for language file:"+fileNameLanguageSyntax[i]+" for risks"+Arrays.asList(risks);
			descriptionLanguageSyntax[i] = "Upload language file:"+fileNameLanguageSyntax[i]+ ". Then verify risk logs are getting generated within the SLA provided"+" for risks"+Arrays.asList(risks); 
		}
		String[] titleFalsePositive = new String[fileNameFalsePositive.length];
		String[] descriptionFalsePositive = new String[fileNameFalsePositive.length];
		for(int i=0;i<fileNameCompressed.length;i++){
			titleFalsePositive[i] = "Risk Generation/Validation for false positive file:"+fileNameFalsePositive[i]+" for risks"+Arrays.asList(risks);
			descriptionFalsePositive[i] = "Upload positive file:"+fileNameFalsePositive[i]+ ". Then verify risk logs are getting generated within the SLA provided"+" for risks"+Arrays.asList(risks); 
		}


		String[] fileName = dciFunctions.merge(fileNameCompressed,fileNameLanguageSyntax,fileNameFalsePositive);
		String[] title = dciFunctions.merge(titleCompressed,titleLanguageSyntax,titleFalsePositive);
		String[] description = dciFunctions.merge(descriptionCompressed,descriptionLanguageSyntax,descriptionFalsePositive);

		fileName = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_SOURCE_CODE_PATH,
				fileName);
		dciFunctions.fileUploadOrSendEmail(suiteData, folderInfo, fileName);

		Object[][] result = new Object[fileName.length][];
		for (int i = 0; i < fileName.length; i++) {
			result[i] = new Object[] { new TestParameters(title[i],description[i],
					fileName[i], SaasType.getSaasFilterType(suiteData.getSaasApp()), Arrays.asList(risks))};
		}

		mainCounter = 0;testCounter = DCIConstants.DCI_COUNTER_MAX;

		return result;
	}

	@DataProvider(name = "dataUploadNoRisks")
	public Object[][] dataUploadNoRisks() {

		dciFunctions = new DCIFunctions();

		String[] fileName= {"config.datatable","config.html","config.json","config.properties","config.xml","Sample.docx"};

		fileName = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_SOURCE_CODE_PATH,
				fileName);
		dciFunctions.fileUploadOrSendEmail(suiteData, folderInfo, fileName);

		Object[][] result = new Object[fileName.length][];
		for (int i = 0; i < fileName.length; i++) {
			result[i] = new Object[] { new TestParameters("No Risk Generation/Validation for language file:"+fileName[i], 
					"Upload no risks file:"+fileName[i]+ ". Then verify no risk logs are getting generated within the SLA provided",
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
			List<NameValuePair> headers, String[] risks) throws Exception {
		String hits = "";
		try {

			String payload = 
					dciFunctions.getSearchQueryForDCI(dciFunctions.getMinusMinutesFromCurrentTime(1440) , dciFunctions.getPlusMinutesFromCurrentTime(120), saasType, "API",
							fileName, "investigate", suiteData);
			Logger.info("****************Input Payload****************");
			Logger.info(payload);
			Logger.info("*********************************************");

			hits = fetchDisplayLogsCounter(dciFunctions, headers, payload, DCIConstants.DCI_COUNTER_MAX);

			Logger.info("****************Output Response****************");
			Logger.info(hits);
			Logger.info("***********************************************");

			String validationMessage = dciFunctions.validationRiskLogs(hits, fileName, suiteData.getUsername(),
					"QA Admin", saasType, "API", risks);

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
					dciFunctions.getSearchQueryForDCI(dciFunctions.getMinusMinutesFromCurrentTime(1440) , dciFunctions.getPlusMinutesFromCurrentTime(120), saasType, "API",
							fileName, "investigate", suiteData);
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
			Assert.assertTrue(jArray.size() == 0, "Expected no log for file upload of " + fileName);

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
