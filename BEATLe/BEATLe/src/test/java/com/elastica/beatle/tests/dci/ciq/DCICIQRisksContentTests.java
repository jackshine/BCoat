package com.elastica.beatle.tests.dci.ciq;

import java.io.File;
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

import net.sf.json.util.JSONTokener;


public class DCICIQRisksContentTests extends DCICommonTest implements ITest {

	protected String mTestCaseName = "";
	DCIFunctions dciFunctions = null;
	ElasticSearchLogs esLogs = null;
	Map<String,String> folderInfo = new HashMap<String,String>();
	String uniqueId = UUID.randomUUID().toString();
	int mainCounter = 0;int testCounter = 0;


	/**********************************************TEST METHODS***********************************************/

	/**
	 * Create CIQ Profile with name with predefined terms and upload file. 
	 * Then verify risk logs are getting generated within the SLA provided.
	 * Once logs are generated verify the contents
	 * @param testParams
	 * @throws Exception
	 */
	@Test(dataProvider = "dataUpload", groups ={"RisksContent"})
	public void testDisplayLogsForRiskGeneratedInCIQProfile(TestParameters testParams) throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();

		Logger.info(
				"************************************Starting "+testParams.getTestName()+" for filename:" + testParams.getFileName() + " and saas app type:" + testParams.getSaasType()+ "******************");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description:"+testParams.getTestDescription());
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output****************");
		Logger.info("Filename:"+testParams.getFileName());Logger.info("Risk Type:"+testParams.getRisks());
		Logger.info("CIQ Profilename:"+testParams.getCiq().get("ciqProfileName"));
		Logger.info("Saas App Type:" + testParams.getSaasType());
		Logger.info("*****************************************************");

		String[] risks = testParams.getRisks().toArray(new String[testParams.getRisks().size()]);
		List<NameValuePair> headers = dciFunctions.getBasicHeaders(suiteData);		
		verifyDisplayLogsWithCIQ(dciFunctions, testParams.getFileName(), 
				testParams.getSaasType(), headers, risks, testParams.getCiq());
		Logger.info(
				"************************************Completed "+testParams.getTestName()+" for filename:" + testParams.getFileName() + " and saas app type:" + testParams.getSaasType()+ "******************");

	}

	/**********************************************TEST METHODS***********************************************/
	/**********************************************DATA PROVIDERS*********************************************/

	@SuppressWarnings("unchecked")
	@DataProvider(name = "dataUpload")
	public Object[][] dataUpload() {
		dciFunctions = new DCIFunctions();

		String[] fileName = dciFunctions.getFileName(DCIConstants.DCI_FILE_UPLOAD_CIQ_RISKS_CONTENT_PATH);
		String[] terms = dciFunctions.getCIQRiskContentProfileName(fileName);
		String[] risks = dciFunctions.riskTypesForAFile(fileName);
		Map<String, String>[] ciqArray = new HashMap[fileName.length];

		System.out.println(fileName.length+"::::"+terms.length+"::::"+risks.length);
		System.out.println(Arrays.asList(fileName)+"::::"+Arrays.asList(terms)+"::::"+Arrays.asList(risks));

		/** Create Dictionary **/
		List<NameValuePair> headers = dciFunctions.getBrowserHeaders(suiteData);

		String dictName = "Custom_Dictionaries_Only";
		String dictDescription = "Custom Dictionaries Only Description";
		String dictKeywords = "custom_dictionaries_only";
		try {
			List<String> keywords= new ArrayList<String>();
			keywords.add(dictKeywords);
			dciFunctions.createDictionary(restClient, suiteData, dictName, 
					dictDescription, null, keywords, headers);
		} catch (Exception ex) {
			Logger.info("Issue with Create Custom Dictionary" + ex.getLocalizedMessage());
		}
		/***********************/

		for(int i = 0; i < fileName.length; i++) {
			try {
				Map<String, String> ciq=createCIQProfile(fileName[i],terms[i]);
				ciqArray[i] = ciq;
			} catch (Exception ex) {
				Logger.info("Issue with Create CIQ Profile" + ex.getLocalizedMessage());
			}
		}


		fileName = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_CIQ_RISKS_CONTENT_PATH,
				fileName);
		dciFunctions.fileUploadOrSendEmail(suiteData, folderInfo, fileName);

		Object[][] result = new Object[fileName.length][];
		for (int i = 0; i < fileName.length; i++) {
			result[i] = new Object[] { new TestParameters("Risk Generation/Validation for ContentIQ Profile:"+terms[i]+"for file:"+fileName[i], 
					"Create CIQ Profile with name:"+terms[i]+" and upload file:"+fileName[i]+ ". Then verify risk logs are getting generated within the SLA provided",
					fileName[i], SaasType.getSaasFilterType(suiteData.getSaasApp()), Arrays.asList(risks[i].split(",")), ciqArray[i])};
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

	private Map<String,String> createCIQProfile(String fileName,String ciqProfileName) throws Exception{
		Logger.info("Creating CIQ profile with custom dictionaries and risks in progress");
		String ciqProfileDescription=ciqProfileName+" Description";
		List<String> valuesPreDefDict=null;List<String> valuesPreDefTerms=null;
		List<String> valuesCustomDict=null;List<String> valuesCustomTerms=null;
		List<String> valuesRiskTypes=null;List<String> valuesContentTypes=null;
		List<String> valuesFileFormat=null;List<String> valuesMLProfile=null;
		int threshold=1;
		Map<String, String> ciq=new HashMap<String, String>();

		if(fileName.equalsIgnoreCase("risks_only.txt")){
			valuesRiskTypes= Arrays.asList("dlp","hipaa","vba_macros","virus","pci",
					"pii","ferpa","glba");threshold=0;
		}else if(fileName.equalsIgnoreCase("content_only.txt")){
			valuesContentTypes=Arrays.asList(
					"business","computing","cryptographic_keys","design doc",
					"encryption","engineering","health","legal","source_code"
					);threshold=0;
		}else if(fileName.equalsIgnoreCase("pdt_only.txt")){
			valuesPreDefTerms=Arrays.asList("Brazil CPF Number");ciq.put("PDT", "Brazil CPF Number");
		}else if(fileName.equalsIgnoreCase("pdd_only.txt")){
			valuesPreDefDict=Arrays.asList("Illegal Drugs");ciq.put("PDD", "Illegal Drugs");
		}else if(fileName.equalsIgnoreCase("custom_terms_only.txt")){
			valuesCustomTerms=Arrays.asList("custom_terms_only");ciq.put("CT", "custom_terms_only");
		}else if(fileName.equalsIgnoreCase("custom_dictionaries_only.txt")){
			valuesCustomDict=Arrays.asList("Custom_Dictionaries_Only");ciq.put("CD", "Custom_Dictionaries_Only");
		}else{
			if(fileName.contains("risk")){
				valuesRiskTypes= Arrays.asList("dlp","hipaa","vba_macros","virus","pci",
						"pii","ferpa","glba");
			}
			if(fileName.contains("content")){
				valuesContentTypes=Arrays.asList(
						"business","computing","cryptographic_keys","design doc",
						"encryption","engineering","health","legal","source_code"
						);
			}
			if(fileName.contains("pdt")){
				valuesPreDefTerms=Arrays.asList("Brazil CPF Number");ciq.put("PDT", "Brazil CPF Number");
			}
			if(fileName.contains("pdd")){
				valuesPreDefDict=Arrays.asList("Illegal Drugs");ciq.put("PDD", "Illegal Drugs");
			}
			if(fileName.contains("ct")){
				valuesCustomTerms=Arrays.asList("custom_terms_only");ciq.put("CT", "custom_terms_only");
			}
			if(fileName.contains("cd")){
				valuesCustomDict=Arrays.asList("Custom_Dictionaries_Only");ciq.put("CD", "Custom_Dictionaries_Only");
			}
		}

		ciq.put("ciqProfileName", ciqProfileName);
		ciq.put("ciqProfileDescription", ciqProfileDescription);

		if(fileName.equalsIgnoreCase("risk_content.txt")){
			threshold=0;
		}

		dciFunctions.createCIQProfile(suiteData, restClient, ciqProfileName, ciqProfileDescription, 
				valuesPreDefDict, valuesPreDefTerms, valuesCustomDict, valuesCustomTerms, 
				valuesRiskTypes, valuesContentTypes, valuesFileFormat,valuesMLProfile,
				"high", threshold, true, 1, false);

		Logger.info("Creating CIQ profile with custom dictionaries and risks is completed");

		return ciq;
	}




	private void verifyDisplayLogsWithCIQ(DCIFunctions dciFunctions, String fileName, String saasType,
			List<NameValuePair> headers, String[] risks, Map<String, String> ciqValues) throws Exception {
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

			String validationMessage = dciFunctions.validationRiskLogsWithCIQProfile(hits, fileName, suiteData.getUsername(),
					"QA Admin", saasType, "API", dciFunctions.riskTypesForAFile(fileName),
					dciFunctions.docClassTypesForAFile(fileName), 1, ciqValues);

			Assert.assertEquals(validationMessage, "", "Output Response Validation is failing for fileName:" + fileName
					+ " for saas app type:" + saasType);

		} finally {
			dciFunctions.cleanupFileFromTempFolder(fileName);
		}
	}

	private String fetchDisplayLogsCounter(DCIFunctions dciFunctions, List<NameValuePair> headers, String payload,
			int maxLimit) throws Exception {
		String hits = "";
		for (int i = 0; i < testCounter; i++, mainCounter++) {

			HttpResponse response = esLogs.getDisplayLogs(restClient, headers, suiteData.getApiserverHostName(),
					new StringEntity(payload));
			String responseBody = ClientUtil.getResponseBody(response);
			hits = dciFunctions.getJSONValue(dciFunctions.getJSONValue(responseBody, "hits"), "hits");

			net.sf.json.JSONArray jArray = (net.sf.json.JSONArray) new JSONTokener(hits).nextValue();
			if (jArray.size() < 1 && i < maxLimit) {
				if (mainCounter >= maxLimit) {
					Logger.info("Counter limit is reached");
					break;
				} else {
					dciFunctions.waitForOneMinute(i + 1);
				}
				continue;
			} else {
				break;
			}

		}

		if (mainCounter >= DCIConstants.DCI_COUNTER_MAX) {
			testCounter = DCIConstants.DCI_COUNTER_LL;
		}
		return hits;
	}


	/**********************************************UTIL METHODS***********************************************/
	/**********************************************BEFORE/AFTER CLASS*****************************************/

	/**
	 * Create folders in saas apps
	 */
	@BeforeClass(groups ={"RisksContent"})
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
	@AfterClass(groups ={"RisksContent"})
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
	 * @throws Exception 
	 */
	@BeforeClass(groups ={"RisksContent"})
	public void deleteContentIqProfileBeforeTestStarts() {
		dciFunctions = new DCIFunctions();
		dciFunctions.deleteAllPolicies(restClient, suiteData);
		dciFunctions.deleteAllCIQProfiles(restClient, suiteData);
		dciFunctions.deleteAllDictionaries(restClient, suiteData);
		dciFunctions.deleteAllTrainingProfiles(restClient, suiteData);
	}

	/**
	 * Delete content iq profile
	 * @throws Exception 
	 */
	@AfterClass(groups ={"RisksContent"})
	public void deleteContentIqProfileAfterTestEnds() {
		dciFunctions = new DCIFunctions();
		dciFunctions.deleteAllPolicies(restClient, suiteData);
		dciFunctions.deleteAllCIQProfiles(restClient, suiteData);
		dciFunctions.deleteAllDictionaries(restClient, suiteData);
		dciFunctions.deleteAllTrainingProfiles(restClient, suiteData);
	}


	@BeforeMethod(groups ={"RisksContent"})
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
			s3.downloadWholeFolder(DCIConstants.DCI_S3_BUCKET, "DCI/CIQ/RiskContentCIQ", 
					new File(DCIConstants.DCI_FILE_TEMP_PATH));

		} catch (Exception ex) {
			Logger.info("Downloading folder from S3 is failed with exception " + ex.getLocalizedMessage());
		}
	}

	/**********************************************BEFORE/AFTER METHODS/CLASS*****************************************/


}