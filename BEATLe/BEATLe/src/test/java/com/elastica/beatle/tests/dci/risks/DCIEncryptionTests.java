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

public class DCIEncryptionTests extends DCICommonTest implements ITest {

	DCIFunctions dciFunctions = null;
	ElasticSearchLogs esLogs = null;
	Map<String,String> folderInfo = new HashMap<String,String>();
	String uniqueId = UUID.randomUUID().toString();
	protected String mTestCaseName = "";
	int mainCounter = 0;int testCounter = 0;

	/**********************************************TEST METHODS***********************************************/

	@Test(dataProvider = "dataUploadEncryptionPositive", groups ={"All"})
	public void testDisplayLogsGenerateRiskForEncryptionRiskPositiveScenarios(TestParameters testParams) throws Exception {
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
	
	@Test(dataProvider = "dataUploadEncryptionNegative", groups ={"All"})
	public void testDisplayLogsGenerateRiskForEncryptionRiskNegativeScenarios(TestParameters testParams) throws Exception {
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

	@DataProvider(name = "dataUploadEncryptionPositive")
	public Object[][] dataUploadEncryptionPositive() {

		dciFunctions = new DCIFunctions();

		
		
		String[] fileNameCompressed = dciFunctions.getFileName(DCIConstants.DCI_FILE_UPLOAD_ENCRYPTION_COMPRESSED_PATH);
		String[] fileNameCompressedFiles = dciFunctions.getFileName(DCIConstants.DCI_FILE_UPLOAD_ENCRYPTION_COMPRESSED_FILES_PATH);
		//String[] fileNameMacFiles = dciFunctions.getFileName(DCIConstants.DCI_FILE_UPLOAD_ENCRYPTION_MAC_FILES_PATH);
		String[] fileNameWindowsFiles = dciFunctions.getFileName(DCIConstants.DCI_FILE_UPLOAD_ENCRYPTION_WINDOWS_FILES_PATH);
		String[] fileNameOtherFiles = dciFunctions.getFileName(DCIConstants.DCI_FILE_UPLOAD_ENCRYPTION_OTHERS_FILES_PATH);
		String[] fileNamePictureFiles = dciFunctions.getFileName(DCIConstants.DCI_FILE_UPLOAD_ENCRYPTION_PICTURE_FILES_PATH);

		String[] risksCompressed  = dciFunctions.riskTypesForAFile(fileNameCompressed);
		String[] risksCompressedFiles = dciFunctions.riskTypesForAFile(fileNameCompressedFiles);
		//String[] risksMacFiles = dciFunctions.riskTypesForAFile(fileNameMacFiles);
		String[] risksWindowsFiles  = dciFunctions.riskTypesForAFile(fileNameWindowsFiles);
		String[] risksOtherFiles = dciFunctions.riskTypesForAFile(fileNameOtherFiles);
		String[] risksPictureFiles  = dciFunctions.riskTypesForAFile(fileNamePictureFiles );
		
		
		fileNameCompressed = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_ENCRYPTION_COMPRESSED_PATH,fileNameCompressed);
		fileNameCompressedFiles = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_ENCRYPTION_COMPRESSED_FILES_PATH,fileNameCompressedFiles);
		//fileNameMacFiles = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_ENCRYPTION_MAC_FILES_PATH,fileNameMacFiles);
		fileNameWindowsFiles = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_ENCRYPTION_WINDOWS_FILES_PATH,fileNameWindowsFiles);
		fileNameOtherFiles = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_ENCRYPTION_OTHERS_FILES_PATH,fileNameOtherFiles);
		fileNamePictureFiles = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_ENCRYPTION_PICTURE_FILES_PATH,fileNamePictureFiles);

		String[] titleCompressed = new String[fileNameCompressed.length];String[] descriptionCompressed = new String[fileNameCompressed.length];
		String[] titleCompressedFiles = new String[fileNameCompressedFiles.length];String[] descriptionCompressedFiles = new String[fileNameCompressedFiles.length];
		//String[] titleMacFiles = new String[fileNameMacFiles.length];String[] descriptionMacFiles = new String[fileNameMacFiles.length];
		String[] titleWindowsFiles = new String[fileNameWindowsFiles.length];String[] descriptionWindowsFiles = new String[fileNameWindowsFiles.length];
		String[] titleOtherFiles = new String[fileNameOtherFiles.length];String[] descriptionOtherFiles = new String[fileNameOtherFiles.length];
		String[] titlePictureFiles = new String[fileNamePictureFiles.length];String[] descriptionPictureFiles = new String[fileNamePictureFiles.length];

		for(int i=0;i<fileNameCompressed.length;i++){
			titleCompressed[i] = "Risk Generation/Validation for compressed encryption risks file types:"+fileNameCompressed[i]+" for risks "+risksCompressed[i];
			descriptionCompressed[i] = "Upload compressed encryption risks file:"+fileNameCompressed[i]+ ". Then verify risk logs are getting generated within the SLA provided"+" for risks "+risksCompressed[i]; 
		}
		for(int i=0;i<fileNameCompressedFiles.length;i++){
			titleCompressedFiles[i] = "Risk Generation/Validation for encryption risks file type in compressed format:"+fileNameCompressedFiles[i]+" for risks "+risksCompressedFiles[i];
			descriptionCompressedFiles[i] = "Upload encryption risks file in compressed format:"+fileNameCompressedFiles[i]+ ". Then verify risk logs are getting generated within the SLA provided"+" for risks "+risksCompressedFiles[i];
		}
		/*for(int i=0;i<fileNameMacFiles.length;i++){
			titleMacFiles[i] = "Risk Generation/Validation for compressed encryption risks mac file types:"+fileNameMacFiles[i]+" for risks "+risksMacFiles[i];
			descriptionMacFiles[i] = "Upload compressed encryption risks mac file:"+fileNameMacFiles[i]+ ". Then verify risk logs are getting generated within the SLA provided"+" for risks "+risksMacFiles[i];
		}*/
		for(int i=0;i<fileNameWindowsFiles.length;i++){
			titleWindowsFiles[i] = "Risk Generation/Validation for compressed encryption risks windows file types:"+fileNameWindowsFiles[i]+" for risks "+risksWindowsFiles[i];
			descriptionWindowsFiles[i] = "Upload compressed encryption risks windows file:"+fileNameWindowsFiles[i]+ ". Then verify risk logs are getting generated within the SLA provided"+" for risks "+risksWindowsFiles[i];
		}
		for(int i=0;i<fileNameOtherFiles.length;i++){
			titleOtherFiles[i] = "Risk Generation/Validation for encryption risks other file types:"+fileNameOtherFiles[i]+" for risks "+risksOtherFiles[i];
			descriptionOtherFiles[i] = "Upload encryption risks other files:"+fileNameOtherFiles[i]+ ". Then verify risk logs are getting generated within the SLA provided"+" for risks "+risksOtherFiles[i];
		}
		for(int i=0;i<fileNamePictureFiles.length;i++){
			titlePictureFiles[i] = "Risk Generation/Validation for encryption risks picture file types:"+fileNamePictureFiles[i]+" for risks "+risksPictureFiles[i];
			descriptionPictureFiles[i] = "Upload encryption risks picture files:"+fileNamePictureFiles[i]+ ". Then verify risk logs are getting generated within the SLA provided"+" for risks "+risksPictureFiles[i];
		}
		
		String[] fileName = dciFunctions.merge(fileNameCompressed,fileNameCompressedFiles,/*fileNameMacFiles,*/
											fileNameWindowsFiles,fileNameOtherFiles);
		String[] title = dciFunctions.merge(titleCompressed,titleCompressedFiles,/*titleMacFiles,*/
											titleWindowsFiles,titleOtherFiles);
		String[] description = dciFunctions.merge(descriptionCompressed,descriptionCompressedFiles,/*descriptionMacFiles,*/
												descriptionWindowsFiles,descriptionOtherFiles);
		String[] risks = dciFunctions.merge(risksCompressed,risksCompressedFiles,/*risksMacFiles,*/
				risksWindowsFiles,risksOtherFiles);
		dciFunctions.fileUploadOrSendEmail(suiteData, folderInfo, fileName);

		Object[][] result = new Object[fileName.length][];
		for (int i = 0; i < fileName.length; i++) {
			result[i] = new Object[] { new TestParameters(title[i], description[i], fileName[i],
					SaasType.getSaasFilterType(suiteData.getSaasApp()), Arrays.asList(risks[i].split(",")))};
		}

		mainCounter = 0;testCounter = DCIConstants.DCI_COUNTER_MAX;
		
		return result;
	}
	
	@DataProvider(name = "dataUploadEncryptionNegative")
	public Object[][] dataUploadEncryptionNegative() {

		dciFunctions = new DCIFunctions();

		String[] fileName = dciFunctions.getFileName(DCIConstants.DCI_FILE_UPLOAD_ENCRYPTION_NEGATIVE_PATH);	
		fileName = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_ENCRYPTION_NEGATIVE_PATH,
				fileName);
		dciFunctions.fileUploadOrSendEmail(suiteData, folderInfo, fileName);

		Object[][] result = new Object[fileName.length][];
		for (int i = 0; i < fileName.length; i++) {
			result[i] = new Object[] { new TestParameters("No Risk Generation/Validation for encryption risks file:"+fileName[i], 
					"Upload no risks file:"+fileName[i]+ ". Then verify risk logs are not getting generated within the SLA provided",
					fileName[i], SaasType.getSaasFilterType(suiteData.getSaasApp()))};
		}

		dciFunctions.waitForMinutes(2);

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
					dciFunctions.getSearchQueryForDCI(suiteData, dciFunctions.getMinusMinutesFromCurrentTime(1440) , 
							dciFunctions.getPlusMinutesFromCurrentTime(120), saasType, DCIConstants.CISourceType, 
							DCIConstants.CIInformationalSeverityType, fileName, DCIConstants.CIFacilityType, DCIConstants.CIActivityType);
			
			Logger.info("****************Input Payload****************");
			Logger.info(payload);
			Logger.info("*********************************************");
			
			hits = fetchDisplayLogsCounter(dciFunctions, headers, payload, DCIConstants.DCI_COUNTER_MAX);

			
			Logger.info("****************Output Response****************");
			Logger.info(hits);
			Logger.info("***********************************************");

			String validationMessage = dciFunctions.validationEncryptionRiskLogs(hits, fileName, suiteData.getUsername(),
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
					dciFunctions.getSearchQueryForDCI(suiteData, dciFunctions.getMinusMinutesFromCurrentTime(1440) , 
							dciFunctions.getPlusMinutesFromCurrentTime(120), saasType, DCIConstants.CISourceType, 
							DCIConstants.CIInformationalSeverityType, fileName, DCIConstants.CIFacilityType, DCIConstants.CIActivityType);
			
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
