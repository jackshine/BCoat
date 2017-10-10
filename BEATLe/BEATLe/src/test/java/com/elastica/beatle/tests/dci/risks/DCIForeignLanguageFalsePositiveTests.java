package com.elastica.beatle.tests.dci.risks;

import java.io.File;
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

public class DCIForeignLanguageFalsePositiveTests extends DCICommonTest implements ITest {

	DCIFunctions dciFunctions = null;
	ElasticSearchLogs esLogs = null;
	Map<String,String> folderInfo = new HashMap<String,String>();
	String uniqueId = UUID.randomUUID().toString();
	protected String mTestCaseName = "";
	int mainCounter = 0;int testCounter = 0;

	/**********************************************TEST METHODS***********************************************/

	@Test(dataProvider = "dataUploadChinese", groups ={"Chinese"})
	public void testDisplayLogsGenerateNoRisksForChineseFalsePositiveFiles(TestParameters testParams) throws Exception {
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
	
	@Test(dataProvider = "dataUploadDutch", groups ={"Dutch"})
	public void testDisplayLogsGenerateNoRisksForDutchFalsePositiveFiles(TestParameters testParams) throws Exception {
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
	
	@Test(dataProvider = "dataUploadFrench", groups ={"French"})
	public void testDisplayLogsGenerateNoRisksForFrenchFalsePositiveFiles(TestParameters testParams) throws Exception {
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
	
	@Test(dataProvider = "dataUploadGerman", groups ={"German"})
	public void testDisplayLogsGenerateNoRisksForGermanFalsePositiveFiles(TestParameters testParams) throws Exception {
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
	
	@Test(dataProvider = "dataUploadJapanese", groups ={"Japanese"})
	public void testDisplayLogsGenerateNoRisksForJapaneseFalsePositiveFiles(TestParameters testParams) throws Exception {
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
	
	@Test(dataProvider = "dataUploadKorean", groups ={"Korean"})
	public void testDisplayLogsGenerateNoRisksForKoreanFalsePositiveFiles(TestParameters testParams) throws Exception {
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
	
	@Test(dataProvider = "dataUploadSpanish", groups ={"Spanish"})
	public void testDisplayLogsGenerateNoRisksForSpanishFalsePositiveFiles(TestParameters testParams) throws Exception {
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

	@DataProvider(name = "dataUploadChinese")
	public Object[][] dataUploadChinese() {

		dciFunctions = new DCIFunctions();

		String[] fileName= dciFunctions.getFileName(DCIConstants.DCI_FILE_UPLOAD_PII_FP_CHINESE);

		fileName = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_PII_FP_CHINESE,
				fileName);
		dciFunctions.fileUploadOrSendEmail(suiteData, folderInfo, fileName);

		Object[][] result = new Object[fileName.length][];
		for (int i = 0; i < fileName.length; i++) {
			result[i] = new Object[] { new TestParameters("No Risk Generation/Validation for Chinese false positive files:"+fileName[i], 
					"Upload Chinese false positive file:"+fileName[i]+ ". Then verify risk logs are not getting generated within the SLA provided",
					fileName[i], SaasType.getSaasFilterType(suiteData.getSaasApp()))};
		}

		dciFunctions.waitForMinutes(5);

		return result;
	}
	
	@DataProvider(name = "dataUploadDutch")
	public Object[][] dataUploadDutch() {

		dciFunctions = new DCIFunctions();

		String[] fileName= dciFunctions.getFileName(DCIConstants.DCI_FILE_UPLOAD_PII_FP_DUTCH);

		fileName = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_PII_FP_DUTCH,
				fileName);
		dciFunctions.fileUploadOrSendEmail(suiteData, folderInfo, fileName);

		Object[][] result = new Object[fileName.length][];
		for (int i = 0; i < fileName.length; i++) {
			result[i] = new Object[] { new TestParameters("No Risk Generation/Validation for Dutch false positive files:"+fileName[i], 
					"Upload Dutch false positive file:"+fileName[i]+ ". Then verify risk logs are not getting generated within the SLA provided",
					fileName[i], SaasType.getSaasFilterType(suiteData.getSaasApp()))};
		}

		dciFunctions.waitForMinutes(5);

		return result;
	}
	
	@DataProvider(name = "dataUploadFrench")
	public Object[][] dataUploadFrench() {

		dciFunctions = new DCIFunctions();

		String[] fileName= dciFunctions.getFileName(DCIConstants.DCI_FILE_UPLOAD_PII_FP_FRENCH);

		fileName = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_PII_FP_FRENCH,
				fileName);
		dciFunctions.fileUploadOrSendEmail(suiteData, folderInfo, fileName);

		Object[][] result = new Object[fileName.length][];
		for (int i = 0; i < fileName.length; i++) {
			result[i] = new Object[] { new TestParameters("No Risk Generation/Validation for French false positive files:"+fileName[i], 
					"Upload French false positive file:"+fileName[i]+ ". Then verify risk logs are not getting generated within the SLA provided",
					fileName[i], SaasType.getSaasFilterType(suiteData.getSaasApp()))};
		}

		dciFunctions.waitForMinutes(5);

		return result;
	}
	
	@DataProvider(name = "dataUploadGerman")
	public Object[][] dataUploadGerman() {

		dciFunctions = new DCIFunctions();

		String[] fileName= dciFunctions.getFileName(DCIConstants.DCI_FILE_UPLOAD_PII_FP_GERMAN);

		fileName = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_PII_FP_GERMAN,
				fileName);
		dciFunctions.fileUploadOrSendEmail(suiteData, folderInfo, fileName);

		Object[][] result = new Object[fileName.length][];
		for (int i = 0; i < fileName.length; i++) {
			result[i] = new Object[] { new TestParameters("No Risk Generation/Validation for German false positive files:"+fileName[i], 
					"Upload German false positive file:"+fileName[i]+ ". Then verify risk logs are not getting generated within the SLA provided",
					fileName[i], SaasType.getSaasFilterType(suiteData.getSaasApp()))};
		}

		dciFunctions.waitForMinutes(5);

		return result;
	}
	
	@DataProvider(name = "dataUploadJapanese")
	public Object[][] dataUploadJapanese() {

		dciFunctions = new DCIFunctions();

		String[] fileName= dciFunctions.getFileName(DCIConstants.DCI_FILE_UPLOAD_PII_FP_JAPANESE);

		fileName = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_PII_FP_JAPANESE,
				fileName);
		dciFunctions.fileUploadOrSendEmail(suiteData, folderInfo, fileName);

		Object[][] result = new Object[fileName.length][];
		for (int i = 0; i < fileName.length; i++) {
			result[i] = new Object[] { new TestParameters("No Risk Generation/Validation for Japanese false positive files:"+fileName[i], 
					"Upload Japanese false positive file:"+fileName[i]+ ". Then verify risk logs are not getting generated within the SLA provided",
					fileName[i], SaasType.getSaasFilterType(suiteData.getSaasApp()))};
		}

		dciFunctions.waitForMinutes(5);

		return result;
	}
	
	@DataProvider(name = "dataUploadKorean")
	public Object[][] dataUploadKorean() {

		dciFunctions = new DCIFunctions();

		String[] fileName= dciFunctions.getFileName(DCIConstants.DCI_FILE_UPLOAD_PII_FP_KOREAN);

		fileName = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_PII_FP_KOREAN,
				fileName);
		dciFunctions.fileUploadOrSendEmail(suiteData, folderInfo, fileName);

		Object[][] result = new Object[fileName.length][];
		for (int i = 0; i < fileName.length; i++) {
			result[i] = new Object[] { new TestParameters("No Risk Generation/Validation for Korean false positive files:"+fileName[i], 
					"Upload Korean false positive file:"+fileName[i]+ ". Then verify risk logs are not getting generated within the SLA provided",
					fileName[i], SaasType.getSaasFilterType(suiteData.getSaasApp()))};
		}

		dciFunctions.waitForMinutes(5);

		return result;
	}
	
	@DataProvider(name = "dataUploadSpanish")
	public Object[][] dataUploadSpanish() {

		dciFunctions = new DCIFunctions();

		String[] fileName= dciFunctions.getFileName(DCIConstants.DCI_FILE_UPLOAD_PII_FP_SPANISH);

		fileName = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_PII_FP_SPANISH,
				fileName);
		dciFunctions.fileUploadOrSendEmail(suiteData, folderInfo, fileName);

		Object[][] result = new Object[fileName.length][];
		for (int i = 0; i < fileName.length; i++) {
			result[i] = new Object[] { new TestParameters("No Risk Generation/Validation for Spanish false positive files:"+fileName[i], 
					"Upload Spanish false positive file:"+fileName[i]+ ". Then verify risk logs are not getting generated within the SLA provided",
					fileName[i], SaasType.getSaasFilterType(suiteData.getSaasApp()))};
		}

		dciFunctions.waitForMinutes(5);

		return result;
	}
	
	

	/**********************************************DATA PROVIDERS*********************************************/
	/**********************************************UTIL METHODS***********************************************/

	@Override
	public String getTestName() {
		return this.mTestCaseName;
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

	/**********************************************UTIL METHODS***********************************************/
	/**********************************************BEFORE/AFTER CLASS*****************************************/

	
	
	/**
	 * Delete all CIQ profiles
	 */
	@BeforeClass(groups ={"Chinese","Dutch","French","German","Japanese","Korean","Spanish"})
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
	@BeforeClass(groups ={"Chinese","Dutch","French","German","Japanese","Korean","Spanish"})
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
	@AfterClass(groups ={"Chinese","Dutch","French","German","Japanese","Korean","Spanish"})
	public void deleteFolder() {
		try {
			UserAccount account = dciFunctions.getUserAccount(suiteData);
			UniversalApi universalApi = dciFunctions.getUniversalApi(suiteData, account);

			dciFunctions.deleteFolder(universalApi, suiteData, folderInfo);
		} catch (Exception ex) {
			Logger.info("Issue with Delete Folder Operation " + ex.getLocalizedMessage());
		}
	}

	@BeforeMethod(groups ={"Chinese","Dutch","French","German","Japanese","Korean","Spanish"})
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
