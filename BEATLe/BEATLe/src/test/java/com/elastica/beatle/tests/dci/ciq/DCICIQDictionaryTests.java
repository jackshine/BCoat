/*package com.elastica.beatle.tests.dci.ciq;

import java.io.File;
import java.io.FileInputStream;
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

import net.sf.json.JSONArray;
import net.sf.json.util.JSONTokener;

public class DCICIQDictionaryTests extends DCICommonTest implements ITest {

	protected String mTestCaseName = "";
	DCIFunctions dciFunctions = null;
	ElasticSearchLogs esLogs = null;
	Map<String,String> folderInfo = new HashMap<String,String>();
	String uniqueId = UUID.randomUUID().toString();
	int mainCounter = 0;int testCounter = 0;

	*//**********************************************TEST METHODS***********************************************//*

	@Test(dataProvider = "dataUpload", groups ={"All"})
	public void testCustomDictionaryRisksForAFile() throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();

		String fileName = ciqProfileFileName[2];
		String profileName = ciqProfileName[2];
		String profileType = ciqProfileType[2];
		String profileKeyword = ciqProfileKeyword[2];
		String count = ciqProfileCount[2];
		String type = ciqType[2];
		String saasType = SaasType.getSaasFilterType(suiteData.getSaasApp());

		Logger.info(
				"************************************ Starting the checking of ContentIQ Violations risks"
						+ " for filename:" + profileName + " with custom dictionary risk "+profileType+
						" and saas app type:" + saasType + " ******************");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description: Create the ContentIQ Profile with a custom dictionary,"
				+ "Upload CIQ custom dictionary risk file and wait for few minutes, Verify the "
				+ "risks is generated for the respective file");
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output****************");
		Logger.info("Filename:"+fileName);
		Logger.info("Content IQ Profile Name:"+profileName);Logger.info("Content IQ Profile Type:"+profileType);
		Logger.info("Risk Type:ContentIQ Violations");
		Logger.info("Saas App Type:"+suiteData.getSaasApp());
		Logger.info("*****************************************************");


		List<NameValuePair> headers = dciFunctions.getBasicHeaders(suiteData);		
		verifyRiskTypesDisplayLogs(dciFunctions, fileName, saasType, headers, profileName, profileType, profileKeyword, count, type);
		
		Logger.info(
				"************************************ Completed the checking of ContentIQ Violations risks"
						+ " for filename:" + profileName + " with custom dictionary risk "+profileType+
						" and saas app type:" + saasType + " ******************");
	}

	

	*//**********************************************TEST METHODS***********************************************//*
	*//**********************************************DATA PROVIDERS*********************************************//*

	@DataProvider(name = "dataUpload")
	public Object[][] dataUpload() {
		dciFunctions = new DCIFunctions();

		List<NameValuePair> headers = dciFunctions.getBrowserHeaders(suiteData);
		
		String[] dictionaryNames = dciFunctions.customDictionaryNames();
		String[] dictionaryKeywords = dciFunctions.customDictionaryKeywords(); 
		String[] dictionaryFiles= dciFunctions.customDictionaryFiles(); 
		String[] dictionaryKeywords = new String[dictionaryNames.length];
		
		String[] ciqCustomProfileName = dciFunctions.ciqCustomProfileNames();
		String[] ciqCustomDictionaryName = dciFunctions.ciqCustomDictionaryNames(); 
		String[] ciqCustomDictionaryFileName= dciFunctions.ciqCustomDictionaryFileNames(); 
		String[] ciqCustomDictionaryCount = dciFunctions.ciqCustomDictionaryCounts();
		
		for(int i=0;i<dictionaryNames.length;i++){
			if(dictionaryKeywords[i].equalsIgnoreCase("empty")){
				Map<String, String> fileDetails = createFileDetails(dictionaryFiles[i]);
				
				dciFunctions.createDictionary(restClient, suiteData, "DCI_CUSTOM_DICTIONARIES", "DCI Description",
						fileDetails, null, headers);
			}else if(dictionaryFiles[i].equalsIgnoreCase("empty")){
				List<String> keywords = Arrays.asList(dciFunctions.getKeywordsFromFile(
						new FileInputStream(DCIConstants.DCI_FILE_UPLOAD_CIQ_DICT_KEYWORD_PATH+dictionaryKeywords[i])));
				dciFunctions.createDictionary(restClient, suiteData, "DCI_CUSTOM_DICTIONARIES", "DCI Description",
						null, keywords, headers);
			}else{
				Map<String, String> fileDetails = createFileDetails(dictionaryFiles[i]);
				
				List<String> keywords = Arrays.asList(dciFunctions.getKeywordsFromFile(
						new FileInputStream(DCIConstants.DCI_FILE_UPLOAD_CIQ_DICT_KEYWORD_PATH+dictionaryKeywords[i])));
				dciFunctions.createDictionary(restClient, suiteData, "DCI_CUSTOM_DICTIONARIES", "DCI Description", 
						fileDetails, keywords, headers);
			}
		}
		
		for(int i=0;i<ciqCustomProfileName.length;i++){
			dciFunctions.createContentIqProfile(restClient, suiteData, "CustomDict", 
					ciqCustomProfileName[i], ciqCustomProfileName[i]+" Description", ciqCustomDictionaryName[i], false);
		}
		
		ciqCustomDictionaryFileName = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_CIQ_DICT_FILE_PATH, ciqCustomDictionaryFileName);
		dciFunctions.fileUploadOrSendEmail(suiteData, folderInfo, ciqCustomDictionaryFileName);

		
		Object[][] result = new Object[ciqCustomDictionaryFileName.length][];
		for (int i = 0; i < ciqCustomDictionaryFileName.length; i++) {
			Map<String,String> ciq = new HashMap<String,String>();
			ciq.put("ciqProfileName", ciqCustomProfileName[i]);
			ciq.put("ciqCustomDictName", ciqCustomDictionaryName[i]);
			ciq.put("ciqCustomDictKeywords", pdd);
			ciq.put("ciqCount", "1");
			
			result[i] = new Object[] { new TestParameters("Risk Generation/Validation for PDT/PDD Terms:"+pdts[i]+" and "+pdd+" in ContentIQ Profile:"+ciqTitle[i]+" for file:"+fileName[i], 
					"Create CIQ Profile with name:"+ciqTitle[i]+" with pdt:"+pdts[i]+" ,pdd:"+pdd+" and upload file:"+fileName[i]+ ". Then verify risk logs are getting generated within the SLA provided",
					fileName[i], SaasType.getSaasFilterType(suiteData.getSaasApp()), dciFunctions.getCIQRisksTypes(fileName[i]), ciq)};
		}

		mainCounter = 0;testCounter = DCIConstants.DCI_COUNTER_MAX;

		return result;
	}



	public Map<String, String> createFileDetails(String dictionaryFile) {
		Map<String,String> fileDetails = new HashMap<String,String>();
		fileDetails.put("fileName", dictionaryFile);
		fileDetails.put("filePath", DCIConstants.DCI_FILE_UPLOAD_CIQ_DICT_KEYWORD_PATH+dictionaryFile);
		return fileDetails;
	}

	

	*//**********************************************DATA PROVIDERS*********************************************//*
	*//**********************************************UTIL METHODS***********************************************//*

	@Override
	public String getTestName() {
		return this.mTestCaseName;
	}


	private void verifyRiskTypesDisplayLogs(DCIFunctions dciFunctions, String fileName, String saasType, List<NameValuePair> headers,
			String ciqProfileName, String ciqProfileType, String ciqProfileKeyword, String ciqProfileCount, String ciqType)
			throws Exception{
		String hits = "";
		
		int riskCount=1;
		if(suiteData.getSaasApp().contains("AttachmentBody")||
				suiteData.getSaasApp().contains("attachmentbody")){
			riskCount=2;
		}

		try {

			String payload = 
					dciFunctions.getSearchQueryForDCI(suiteData, dciFunctions.getMinusMinutesFromCurrentTime(1440) , 
							dciFunctions.getPlusMinutesFromCurrentTime(120), saasType, DCIConstants.CISourceType, 
							DCIConstants.CICriticalSeverityType, fileName, DCIConstants.CIFacilityType, DCIConstants.CIActivityType);
			
			Logger.info("****************Input Payload****************");
			Logger.info(payload);
			Logger.info("*********************************************");

			hits = fetchDisplayLogsCounter(dciFunctions, headers, payload, DCIConstants.DCI_COUNTER_MAX, riskCount);

			Logger.info("****************Output Response****************");
			Logger.info(hits);
			Logger.info("***********************************************");

			Map<String, String> CIJson = dciFunctions.
					populateContentInspectionJson(suiteData,fileName, riskCount, 
							"critical", "API", true, Arrays.asList(ciqProfileName.split(",")),  
							Arrays.asList(ciqProfileType.split(",")), Arrays.asList(ciqProfileKeyword.split(",")),  
							Arrays.asList(ciqProfileCount.split(",")), Arrays.asList(ciqType.split(",")));
			
			String validationMessage = dciFunctions.validateDCIRiskContentLogs
					(suiteData, hits, CIJson);

			Assert.assertEquals(validationMessage, "", "Output Response Validation is failing for fileName:"+fileName+
					" for saas app type:"+saasType);


		} finally {
			dciFunctions.cleanupFileFromTempFolder(fileName);
		}
	}

	private String fetchDisplayLogsCounter(DCIFunctions dciFunctions, List<NameValuePair> headers,
			String payload, int maxLimit, int riskCount) throws Exception{
		String responseBody = "";
		for (int i = 0; i < testCounter; i++,mainCounter++) {

			HttpResponse response = esLogs.getDisplayLogs(restClient, headers, suiteData.getApiserverHostName(), new StringEntity(payload));
			responseBody = ClientUtil.getResponseBody(response);
			String hits = dciFunctions.getJSONValue(dciFunctions.getJSONValue(responseBody, "hits"), "hits");

			JSONArray jArray = (JSONArray) new JSONTokener(hits).nextValue();
			if (jArray.size() < riskCount && i < maxLimit) {
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


	*//**********************************************UTIL METHODS***********************************************//*
	*//**********************************************BEFORE/AFTER CLASS*****************************************//*

	*//**
	 * Create folders in saas apps
	 *//*
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

	*//**
	 * Delete folders in saas apps
	 *//*
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


	*//**
	 * Delete content iq profile
	 * @throws Exception 
	 *//*
	@BeforeClass(groups ={"All"})
	public void deleteContentIqProfileBeforeTestStarts() {
		dciFunctions = new DCIFunctions();
		dciFunctions.deleteAllPolicies(restClient, suiteData);
		dciFunctions.deleteAllCIQProfiles(restClient, suiteData);
		dciFunctions.deleteAllDictionaries(restClient, suiteData);
		dciFunctions.deleteAllTrainingProfiles(restClient, suiteData);
	}

	*//**
	 * Delete content iq profile
	 * @throws Exception 
	 *//*
	@AfterClass(groups ={"All"})
	public void deleteContentIqProfileAfterTestEnds() {
		dciFunctions = new DCIFunctions();
		dciFunctions.deleteAllPolicies(restClient, suiteData);
		dciFunctions.deleteAllCIQProfiles(restClient, suiteData);
		dciFunctions.deleteAllDictionaries(restClient, suiteData);
		dciFunctions.deleteAllTrainingProfiles(restClient, suiteData);
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

	@BeforeSuite(alwaysRun=true)
	public void downloadFileFromS3() {
		try {
			S3ActionHandler s3 = new S3ActionHandler();
			s3.downloadWholeFolder(DCIConstants.DCI_S3_BUCKET, "DCI/CIQ/PDT_PDD", 
					new File(DCIConstants.DCI_FILE_TEMP_PATH));
			
		} catch (Exception ex) {
			Logger.info("Downloading folder from S3 is failed with exception " + ex.getLocalizedMessage());
		}
	}

	*//**********************************************BEFORE/AFTER METHODS/CLASS*****************************************//*


}*/