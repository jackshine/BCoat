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

public class DCINoAnchorPCIPIITests extends DCICommonTest implements ITest {

	DCIFunctions dciFunctions = null;
	ElasticSearchLogs esLogs = null;
	Map<String,String> folderInfo = new HashMap<String,String>();
	String uniqueId = UUID.randomUUID().toString();
	protected String mTestCaseName = "";
	int mainCounter = 0;int testCounter = 0;

	/**********************************************TEST METHODS***********************************************/

	@Test(dataProvider = "dataUploadNoAnchor", groups ={"NoAnchor"})
	public void testDisplayLogsGenerateRiskNoAnchor(TestParameters testParams) throws Exception {
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

		List<NameValuePair> headers = dciFunctions.getBasicHeaders(suiteData);		
		verifyRiskTypesDisplayLogs(dciFunctions, testParams.getFileName(), testParams.getSaasType(), headers);

		Logger.info(
				"************************************Completed "+testParams.getTestName()+" for saas app type:" + testParams.getSaasType()+ "******************");

	}
	
	@Test(dataProvider = "dataUploadGermanNoAnchor", groups ={"German"})
	public void testDisplayLogsGenerateRiskGermanNoAnchor(TestParameters testParams) throws Exception {
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

		List<NameValuePair> headers = dciFunctions.getBasicHeaders(suiteData);		
		verifyRiskTypesDisplayLogs(dciFunctions, testParams.getFileName(), testParams.getSaasType(), headers);

		Logger.info(
				"************************************Completed "+testParams.getTestName()+" for saas app type:" + testParams.getSaasType()+ "******************");

	}
	
	@Test(dataProvider = "dataUploadFrenchNoAnchor", groups ={"French"})
	public void testDisplayLogsGenerateRiskFrenchNoAnchor(TestParameters testParams) throws Exception {
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

		List<NameValuePair> headers = dciFunctions.getBasicHeaders(suiteData);		
		verifyRiskTypesDisplayLogs(dciFunctions, testParams.getFileName(), testParams.getSaasType(), headers);

		Logger.info(
				"************************************Completed "+testParams.getTestName()+" for saas app type:" + testParams.getSaasType()+ "******************");

	}
	
	@Test(dataProvider = "dataUploadUKNoAnchor", groups ={"UK"})
	public void testDisplayLogsGenerateRiskUKNoAnchor(TestParameters testParams) throws Exception {
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

		List<NameValuePair> headers = dciFunctions.getBasicHeaders(suiteData);		
		verifyRiskTypesDisplayLogs(dciFunctions, testParams.getFileName(), testParams.getSaasType(), headers);

		Logger.info(
				"************************************Completed "+testParams.getTestName()+" for saas app type:" + testParams.getSaasType()+ "******************");

	}
	
	@Test(dataProvider = "dataUpload", groups ={"All"})
	public void testDisplayLogsGenerateRisk(TestParameters testParams) throws Exception {
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

		List<NameValuePair> headers = dciFunctions.getBasicHeaders(suiteData);		
		verifyRiskTypesDisplayLogs(dciFunctions, testParams.getFileName(), testParams.getSaasType(), headers);

		Logger.info(
				"************************************Completed "+testParams.getTestName()+" for saas app type:" + testParams.getSaasType()+ "******************");

	}

	
	@Test(dataProvider = "dataUploadNegative", groups ={"All"})
	public void testDisplayLogsGenerateNoRisk(TestParameters testParams) throws Exception {
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

	@DataProvider(name = "dataUploadNoAnchor")
	public Object[][] dataUploadNoAnchor() {

		dciFunctions = new DCIFunctions();

		String[] fileName= dciFunctions.getFileName(DCIConstants.DCI_FILE_UPLOAD_PII_PCI_269_PATH+File.separator+"Positive");
		String[] risks = dciFunctions.riskTypesForAFile(fileName);
		fileName = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_PII_PCI_269_PATH+File.separator+"Positive",fileName);
		dciFunctions.fileUploadOrSendEmail(suiteData, folderInfo, fileName);

		Object[][] result = new Object[fileName.length][];
		for (int i = 0; i < fileName.length; i++) {
			result[i] = new Object[] { new TestParameters("Validation of uploaded file:"+fileName[i]+" for successful processing via Content Inspection Engine with splunk", 
					"Upload file:"+fileName[i]+ ". Then verify file is processed successfully with splunk within the SLA provided",
					fileName[i], SaasType.getSaasFilterType(suiteData.getSaasApp()), 
					Arrays.asList(risks[i].split(",")))};
		}

		mainCounter = 0;testCounter = DCIConstants.DCI_COUNTER_MAX;
		return result;
	}
	
	@DataProvider(name = "dataUploadGermanNoAnchor")
	public Object[][] dataUploadGermanNoAnchor() {

		dciFunctions = new DCIFunctions();

		String[] fileName= dciFunctions.getFileName(DCIConstants.DCI_FILE_UPLOAD_PII_PCI_269_PATH+File.separator+"German");
		String[] risks = dciFunctions.riskTypesForAFile(fileName);
		fileName = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_PII_PCI_269_PATH+File.separator+"German",fileName);
		dciFunctions.fileUploadOrSendEmail(suiteData, folderInfo, fileName);

		Object[][] result = new Object[fileName.length][];
		for (int i = 0; i < fileName.length; i++) {
			result[i] = new Object[] { new TestParameters("Validation of uploaded file:"+fileName[i]+" for successful processing via Content Inspection Engine with splunk", 
					"Upload file:"+fileName[i]+ ". Then verify file is processed successfully with splunk within the SLA provided",
					fileName[i], SaasType.getSaasFilterType(suiteData.getSaasApp()), 
					Arrays.asList(risks[i].split(",")))};
		}

		mainCounter = 0;testCounter = DCIConstants.DCI_COUNTER_MAX;
		return result;
	}
	
	@DataProvider(name = "dataUploadFrenchNoAnchor")
	public Object[][] dataUploadFrenchNoAnchor() {

		dciFunctions = new DCIFunctions();

		String[] fileName= dciFunctions.getFileName(DCIConstants.DCI_FILE_UPLOAD_PII_PCI_269_PATH+File.separator+"French");
		String[] risks = dciFunctions.riskTypesForAFile(fileName);
		fileName = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_PII_PCI_269_PATH+File.separator+"French",fileName);
		dciFunctions.fileUploadOrSendEmail(suiteData, folderInfo, fileName);

		Object[][] result = new Object[fileName.length][];
		for (int i = 0; i < fileName.length; i++) {
			result[i] = new Object[] { new TestParameters("Validation of uploaded file:"+fileName[i]+" for successful processing via Content Inspection Engine with splunk", 
					"Upload file:"+fileName[i]+ ". Then verify file is processed successfully with splunk within the SLA provided",
					fileName[i], SaasType.getSaasFilterType(suiteData.getSaasApp()), 
					Arrays.asList(risks[i].split(",")))};
		}

		mainCounter = 0;testCounter = DCIConstants.DCI_COUNTER_MAX;
		return result;
	}
	
	@DataProvider(name = "dataUploadUKNoAnchor")
	public Object[][] dataUploadUKNoAnchor() {

		dciFunctions = new DCIFunctions();

		String[] fileName= dciFunctions.getFileName(DCIConstants.DCI_FILE_UPLOAD_PII_PCI_269_PATH+File.separator+"UK");
		String[] risks = dciFunctions.riskTypesForAFile(fileName);
		fileName = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_PII_PCI_269_PATH+File.separator+"UK",fileName);
		dciFunctions.fileUploadOrSendEmail(suiteData, folderInfo, fileName);

		Object[][] result = new Object[fileName.length][];
		for (int i = 0; i < fileName.length; i++) {
			result[i] = new Object[] { new TestParameters("Validation of uploaded file:"+fileName[i]+" for successful processing via Content Inspection Engine with splunk", 
					"Upload file:"+fileName[i]+ ". Then verify file is processed successfully with splunk within the SLA provided",
					fileName[i], SaasType.getSaasFilterType(suiteData.getSaasApp()), 
					Arrays.asList(risks[i].split(",")))};
		}

		mainCounter = 0;testCounter = DCIConstants.DCI_COUNTER_MAX;
		return result;
	}
	
	@DataProvider(name = "dataUpload")
	public Object[][] dataUpload() {

		dciFunctions = new DCIFunctions();

		String[] fileName= dciFunctions.getFileName(DCIConstants.DCI_FILE_UPLOAD_PII_PCI_269_PATH+File.separator+"Combinations"+File.separator+"Positive");
		String[] risks = dciFunctions.riskTypesForAFile(fileName);
		fileName = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_PII_PCI_269_PATH+File.separator+"Combinations"+File.separator+"Positive",fileName);
		dciFunctions.fileUploadOrSendEmail(suiteData, folderInfo, fileName);

		Object[][] result = new Object[fileName.length][];
		for (int i = 0; i < fileName.length; i++) {
			result[i] = new Object[] { new TestParameters("Validation of uploaded file:"+fileName[i]+" for successful processing via Content Inspection Engine with splunk", 
					"Upload file:"+fileName[i]+ ". Then verify file is processed successfully with splunk within the SLA provided",
					fileName[i], SaasType.getSaasFilterType(suiteData.getSaasApp()), 
					Arrays.asList(risks[i].split(",")))};
		}

		mainCounter = 0;testCounter = DCIConstants.DCI_COUNTER_MAX;
		return result;
	}

	@DataProvider(name = "dataUploadNegative")
	public Object[][] dataUploadNegative() {

		dciFunctions = new DCIFunctions();

		String[] fileName= dciFunctions.getFileName(DCIConstants.DCI_FILE_UPLOAD_PII_PCI_269_PATH+File.separator+"Combinations"+File.separator+"Negative");
		fileName = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_PII_PCI_269_PATH+File.separator+"Combinations"+File.separator+"Negative",fileName);
		dciFunctions.fileUploadOrSendEmail(suiteData, folderInfo, fileName);

		Object[][] result = new Object[fileName.length][];
		for (int i = 0; i < fileName.length; i++) {
			result[i] = new Object[] { new TestParameters("No Risk Generation/Validation for PII risks file:"+fileName[i], 
					"Upload no risks file:"+fileName[i]+ ". Then verify risk logs are not getting generated within the SLA provided",
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

	private void verifyRiskTypesDisplayLogs(DCIFunctions dciFunctions, String fileName, String saasType, List<NameValuePair> headers)
			throws Exception{
		String hits = "";
		int rCount=1;

		try {

			String payload = 
					dciFunctions.getSearchQueryForDCI(suiteData, dciFunctions.getMinusMinutesFromCurrentTime(1440) , 
							dciFunctions.getPlusMinutesFromCurrentTime(120), saasType, DCIConstants.CISourceType, 
							DCIConstants.CICriticalSeverityType, fileName, DCIConstants.CIFacilityType, DCIConstants.CIActivityType);

			Logger.info("****************Input Payload****************");
			Logger.info(payload);
			Logger.info("*********************************************");

			hits = fetchDisplayLogsCounter(dciFunctions, headers, payload, DCIConstants.DCI_COUNTER_MAX, rCount);

			Logger.info("****************Output Response****************");
			Logger.info(hits);
			Logger.info("***********************************************");

			Map<String, String> CIJson = dciFunctions.
					populateContentInspectionJson(suiteData,fileName, rCount, 
							"critical", "API", true);

			String validationMessage = dciFunctions.validateDCIRiskContentLogs
					(suiteData, hits, CIJson);

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
			String payload, int maxLimit, int rCount) throws Exception{
		String responseBody = "";
		for (int i = 0; i < testCounter; i++,mainCounter++) {

			HttpResponse response = esLogs.getDisplayLogs(restClient, headers, suiteData.getApiserverHostName(), new StringEntity(payload));
			responseBody = ClientUtil.getResponseBody(response);
			String hits = dciFunctions.getJSONValue(dciFunctions.getJSONValue(responseBody, "hits"), "hits");

			JSONArray jArray = (JSONArray) new JSONTokener(hits).nextValue();
			if (jArray.size() < rCount && i < maxLimit) {
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
		return dciFunctions.getJSONValue(responseBody, "hits");
	}

	/**********************************************UTIL METHODS***********************************************/
	/**********************************************BEFORE/AFTER CLASS*****************************************/


	/**
	 * Delete content iq profile
	 */
	@AfterClass(groups ={"All","NoAnchor","French","German","UK"})
	public void deleteContentIqProfile() {
		dciFunctions = new DCIFunctions();
		dciFunctions.deleteAllPolicies(restClient, suiteData);
		dciFunctions.deleteAllCIQProfiles(restClient, suiteData);
		dciFunctions.deleteAllDictionaries(restClient, suiteData);
		dciFunctions.deleteAllTrainingProfiles(restClient, suiteData);
	}

	/**
	 * Create folders in saas apps
	 */
	@BeforeClass(groups ={"All","NoAnchor","French","German","UK"})
	public void createFolder() {
		dciFunctions = new DCIFunctions();
		try {
			UserAccount account = dciFunctions.getUserAccount(suiteData);
			UniversalApi universalApi = dciFunctions.getUniversalApi(suiteData, account);

			folderInfo = dciFunctions.createFolder(universalApi, suiteData, 
					DCIConstants.DCI_FOLDER+uniqueId);
		} catch (Exception ex) {
			Logger.info("Issue with Create Folder Operation " + ex.getLocalizedMessage());
		}
	}

	/**
	 * Delete folders in saas apps
	 */
	@AfterClass(groups ={"All","NoAnchor","French","German","UK"})
	public void deleteFolder() {
		try {
			UserAccount account = dciFunctions.getUserAccount(suiteData);
			UniversalApi universalApi = dciFunctions.getUniversalApi(suiteData, account);

			dciFunctions.deleteFolder(universalApi, suiteData, folderInfo);
		} catch (Exception ex) {
			Logger.info("Issue with Delete Folder Operation " + ex.getLocalizedMessage());
		}
	}

	@BeforeMethod(groups ={"All","NoAnchor","French","German","UK"})
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
