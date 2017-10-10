package com.elastica.beatle.tests.dci.content;

import java.io.File;
import java.lang.reflect.Method;
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

/**
 * 
 * @author eldorajan
 *
 */

public class DCIContentTypeTests extends DCICommonTest implements ITest {

	DCIFunctions dciFunctions = null;
	ElasticSearchLogs esLogs = null;
	Map<String,String> folderInfo = new HashMap<String,String>();
	String uniqueId = UUID.randomUUID().toString();
	String contentIQProfileId=null;protected String mTestCaseName = "";
	int mainCounter = 0;int testCounter = 0;

	/**********************************************TEST METHODS***********************************************/

	@Test(dataProvider = "dataUploadImagePositive", groups ={"image"})
	public void testDisplayLogsGenerateRiskForImageFiles(TestParameters testParams) throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();

		Logger.info(
				"************************************Starting "+testParams.getTestName()+" for saas app type:" + testParams.getSaasType()+ "******************");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description:"+testParams.getTestDescription());
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output****************");
		Logger.info("Filename:"+testParams.getFileName());
		Logger.info("Content Types to be validated:"+testParams.getRisks());
		Logger.info("Saas App Type:"+testParams.getSaasType());
		Logger.info("*****************************************************");
		
		List<NameValuePair> headers = dciFunctions.getBasicHeaders(suiteData);		
		verifyContentTypesDisplayLogs(dciFunctions, testParams.getFileName(), testParams.getSaasType(), headers);

		Logger.info(
				"************************************Completed "+testParams.getTestName()+" for saas app type:" + testParams.getSaasType()+ "******************");
	}

	@Test(dataProvider = "dataUploadAudioPositive", groups ={"audio"})
	public void testDisplayLogsGenerateRiskForAudioFiles(TestParameters testParams) throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();

		Logger.info(
				"************************************Starting "+testParams.getTestName()+" for saas app type:" + testParams.getSaasType()+ "******************");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description:"+testParams.getTestDescription());
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output****************");
		Logger.info("Filename:"+testParams.getFileName());
		Logger.info("Content Types to be validated:"+testParams.getRisks());
		Logger.info("Saas App Type:"+testParams.getSaasType());
		Logger.info("*****************************************************");

		List<NameValuePair> headers = dciFunctions.getBasicHeaders(suiteData);		
		verifyContentTypesDisplayLogs(dciFunctions, testParams.getFileName(), testParams.getSaasType(), headers);

		Logger.info(
				"************************************Completed "+testParams.getTestName()+" for saas app type:" + testParams.getSaasType()+ "******************");
	}

	@Test(dataProvider = "dataUploadVideoPositive", groups ={"video"})
	public void testDisplayLogsGenerateRiskForVideoFiles(TestParameters testParams) throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();

		Logger.info(
				"************************************Starting "+testParams.getTestName()+" for saas app type:" + testParams.getSaasType()+ "******************");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description:"+testParams.getTestDescription());
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output****************");
		Logger.info("Filename:"+testParams.getFileName());
		Logger.info("Content Types to be validated:"+testParams.getRisks());
		Logger.info("Saas App Type:"+testParams.getSaasType());
		Logger.info("*****************************************************");

		List<NameValuePair> headers = dciFunctions.getBasicHeaders(suiteData);		
		verifyContentTypesDisplayLogs(dciFunctions, testParams.getFileName(), testParams.getSaasType(), headers);

		Logger.info(
				"************************************Completed "+testParams.getTestName()+" for saas app type:" + testParams.getSaasType()+ "******************");
	}

	@Test(dataProvider = "dataUploadExecutablePositive", groups ={"executable"})
	public void testDisplayLogsGenerateRiskForExecutableFiles(TestParameters testParams) throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();

		Logger.info(
				"************************************Starting "+testParams.getTestName()+" for saas app type:" + testParams.getSaasType()+ "******************");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description:"+testParams.getTestDescription());
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output****************");
		Logger.info("Filename:"+testParams.getFileName());
		Logger.info("Content Types to be validated:"+testParams.getRisks());
		Logger.info("Saas App Type:"+testParams.getSaasType());
		Logger.info("*****************************************************");
		
		List<NameValuePair> headers = dciFunctions.getBasicHeaders(suiteData);		
		verifyContentTypesDisplayLogs(dciFunctions, testParams.getFileName(), testParams.getSaasType(), headers);

		Logger.info(
				"************************************Completed "+testParams.getTestName()+" for saas app type:" + testParams.getSaasType()+ "******************");
	}

	@Test(dataProvider = "dataUploadCryptographicKeysPositive", groups ={"crypto"})
	public void testDisplayLogsGenerateRiskForCryptographicKeysFiles(TestParameters testParams) throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();

		Logger.info(
				"************************************Starting "+testParams.getTestName()+" for saas app type:" + testParams.getSaasType()+ "******************");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description:"+testParams.getTestDescription());
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output****************");
		Logger.info("Filename:"+testParams.getFileName());
		Logger.info("Content Types to be validated:"+testParams.getRisks());
		Logger.info("Saas App Type:"+testParams.getSaasType());
		Logger.info("*****************************************************");

		List<NameValuePair> headers = dciFunctions.getBasicHeaders(suiteData);		
		verifyContentTypesDisplayLogs(dciFunctions, testParams.getFileName(), testParams.getSaasType(), headers);

		Logger.info(
				"************************************Completed "+testParams.getTestName()+" for saas app type:" + testParams.getSaasType()+ "******************");
	}

	@Test(dataProvider = "dataUploadBusinessPositive", groups ={"business"})
	public void testDisplayLogsGenerateRiskForBusinessFiles(TestParameters testParams) throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();

		Logger.info(
				"************************************Starting "+testParams.getTestName()+" for saas app type:" + testParams.getSaasType()+ "******************");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description:"+testParams.getTestDescription());
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output****************");
		Logger.info("Filename:"+testParams.getFileName());
		Logger.info("Content Types to be validated:"+testParams.getRisks());
		Logger.info("Saas App Type:"+testParams.getSaasType());
		Logger.info("*****************************************************");

		List<NameValuePair> headers = dciFunctions.getBasicHeaders(suiteData);		
		verifyContentTypesDisplayLogs(dciFunctions, testParams.getFileName(), testParams.getSaasType(), headers);

		Logger.info(
				"************************************Completed "+testParams.getTestName()+" for saas app type:" + testParams.getSaasType()+ "******************");
	}

	@Test(dataProvider = "dataUploadComputingPositive", groups ={"computing"})
	public void testDisplayLogsGenerateRiskForComputingFiles(TestParameters testParams) throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();

		Logger.info(
				"************************************Starting "+testParams.getTestName()+" for saas app type:" + testParams.getSaasType()+ "******************");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description:"+testParams.getTestDescription());
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output****************");
		Logger.info("Filename:"+testParams.getFileName());
		Logger.info("Content Types to be validated:"+testParams.getRisks());
		Logger.info("Saas App Type:"+testParams.getSaasType());
		Logger.info("*****************************************************");

		List<NameValuePair> headers = dciFunctions.getBasicHeaders(suiteData);		
		verifyContentTypesDisplayLogs(dciFunctions, testParams.getFileName(), testParams.getSaasType(), headers);

		Logger.info(
				"************************************Completed "+testParams.getTestName()+" for saas app type:" + testParams.getSaasType()+ "******************");
	}

	@Test(dataProvider = "dataUploadDesignPositive", groups ={"design"})
	public void testDisplayLogsGenerateRiskForDesignFiles(TestParameters testParams) throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();

		Logger.info(
				"************************************Starting "+testParams.getTestName()+" for saas app type:" + testParams.getSaasType()+ "******************");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description:"+testParams.getTestDescription());
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output****************");
		Logger.info("Filename:"+testParams.getFileName());
		Logger.info("Content Types to be validated:"+testParams.getRisks());
		Logger.info("Saas App Type:"+testParams.getSaasType());
		Logger.info("*****************************************************");

		List<NameValuePair> headers = dciFunctions.getBasicHeaders(suiteData);		
		verifyContentTypesDisplayLogs(dciFunctions, testParams.getFileName(), testParams.getSaasType(), headers);

		Logger.info(
				"************************************Completed "+testParams.getTestName()+" for saas app type:" + testParams.getSaasType()+ "******************");
	}

	@Test(dataProvider = "dataUploadEngineeringPositive", groups ={"engineering"})
	public void testDisplayLogsGenerateRiskForEngineeringFiles(TestParameters testParams) throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();

		Logger.info(
				"************************************Starting "+testParams.getTestName()+" for saas app type:" + testParams.getSaasType()+ "******************");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description:"+testParams.getTestDescription());
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output****************");
		Logger.info("Filename:"+testParams.getFileName());
		Logger.info("Content Types to be validated:"+testParams.getRisks());
		Logger.info("Saas App Type:"+testParams.getSaasType());
		Logger.info("*****************************************************");

		List<NameValuePair> headers = dciFunctions.getBasicHeaders(suiteData);		
		verifyContentTypesDisplayLogs(dciFunctions, testParams.getFileName(), testParams.getSaasType(), headers);

		Logger.info(
				"************************************Completed "+testParams.getTestName()+" for saas app type:" + testParams.getSaasType()+ "******************");
	}

	@Test(dataProvider = "dataUploadHealthPositive", groups ={"health"})
	public void testDisplayLogsGenerateRiskForHealthFiles(TestParameters testParams) throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();

		Logger.info(
				"************************************Starting "+testParams.getTestName()+" for saas app type:" + testParams.getSaasType()+ "******************");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description:"+testParams.getTestDescription());
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output****************");
		Logger.info("Filename:"+testParams.getFileName());
		Logger.info("Content Types to be validated:"+testParams.getRisks());
		Logger.info("Saas App Type:"+testParams.getSaasType());
		Logger.info("*****************************************************");

		List<NameValuePair> headers = dciFunctions.getBasicHeaders(suiteData);		
		verifyContentTypesDisplayLogs(dciFunctions, testParams.getFileName(), testParams.getSaasType(), headers);

		Logger.info(
				"************************************Completed "+testParams.getTestName()+" for saas app type:" + testParams.getSaasType()+ "******************");
	}

	@Test(dataProvider = "dataUploadLegalPositive", groups ={"legal"})
	public void testDisplayLogsGenerateRiskForLegalFiles(TestParameters testParams) throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();

		Logger.info(
				"************************************Starting "+testParams.getTestName()+" for saas app type:" + testParams.getSaasType()+ "******************");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description:"+testParams.getTestDescription());
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output****************");
		Logger.info("Filename:"+testParams.getFileName());
		Logger.info("Content Types to be validated:"+testParams.getRisks());
		Logger.info("Saas App Type:"+testParams.getSaasType());
		Logger.info("*****************************************************");

		List<NameValuePair> headers = dciFunctions.getBasicHeaders(suiteData);		
		verifyContentTypesDisplayLogs(dciFunctions, testParams.getFileName(), testParams.getSaasType(), headers);

		Logger.info(
				"************************************Completed "+testParams.getTestName()+" for saas app type:" + testParams.getSaasType()+ "******************");
	}


	/**********************************************TEST METHODS***********************************************/
	/**********************************************DATA PROVIDERS*********************************************/

	@DataProvider(name = "dataUploadImagePositive")
	public Object[][] dataUploadImagePositive() {

		dciFunctions = new DCIFunctions();

		String[] fileName = dciFunctions.getFileName(DCIConstants.DCI_FILE_UPLOAD_IMAGE_PATH);	
		fileName = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_IMAGE_PATH, fileName);

		dciFunctions.fileUploadOrSendEmail(suiteData, folderInfo, fileName);

		Object[][] result = new Object[fileName.length][];
		for (int i = 0; i < fileName.length; i++) {
			result[i] = new Object[] { new TestParameters("Content Classification Logs Generation/Validation for Image risks file types:"+fileName[i]+" for classification: image", 
					"Upload Image content type file:"+fileName[i]+ ". Then verify content classification logs are getting generated within the SLA provided"+" for classifications: image",
					fileName[i], SaasType.getSaasFilterType(suiteData.getSaasApp()))};
		}

		mainCounter = 0;testCounter = DCIConstants.DCI_COUNTER_MAX;

		return result;
	}

	@DataProvider(name = "dataUploadAudioPositive")
	public Object[][] dataUploadAudioPositive() {

		dciFunctions = new DCIFunctions();

		String[] fileName = dciFunctions.getFileName(DCIConstants.DCI_FILE_UPLOAD_AUDIO_PATH);	
		fileName = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_AUDIO_PATH, fileName);

		dciFunctions.fileUploadOrSendEmail(suiteData, folderInfo, fileName);

		Object[][] result = new Object[fileName.length][];
		for (int i = 0; i < fileName.length; i++) {
			result[i] = new Object[] { new TestParameters("Content Classification Logs Generation/Validation for Audio risks file types:"+fileName[i]+" for classification: audio", 
					"Upload Audio content type file:"+fileName[i]+ ". Then verify content classification logs are getting generated within the SLA provided"+" for classifications: audio",
					fileName[i], SaasType.getSaasFilterType(suiteData.getSaasApp()))};
		}

		mainCounter = 0;testCounter = DCIConstants.DCI_COUNTER_MAX;

		return result;
	}

	@DataProvider(name = "dataUploadVideoPositive")
	public Object[][] dataUploadVideoPositive() {

		dciFunctions = new DCIFunctions();

		String[] fileName = dciFunctions.getFileName(DCIConstants.DCI_FILE_UPLOAD_VIDEO_PATH);	
		fileName = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_VIDEO_PATH, fileName);

		dciFunctions.fileUploadOrSendEmail(suiteData, folderInfo, fileName);

		Object[][] result = new Object[fileName.length][];
		for (int i = 0; i < fileName.length; i++) {
			result[i] = new Object[] { new TestParameters("Content Classification Logs Generation/Validation for Video risks file types:"+fileName[i]+" for classification: video", 
					"Upload Video content type file:"+fileName[i]+ ". Then verify content classification logs are getting generated within the SLA provided"+" for classifications: video",
					fileName[i], SaasType.getSaasFilterType(suiteData.getSaasApp()))};
		}

		mainCounter = 0;testCounter = DCIConstants.DCI_COUNTER_MAX;

		return result;
	}

	@DataProvider(name = "dataUploadExecutablePositive")
	public Object[][] dataUploadExecutablePositive() {

		dciFunctions = new DCIFunctions();

		String[] fileName = dciFunctions.getFileName(DCIConstants.DCI_FILE_UPLOAD_EXEC_PATH);	
		fileName = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_EXEC_PATH, fileName);

		dciFunctions.fileUploadOrSendEmail(suiteData, folderInfo, fileName);

		Object[][] result = new Object[fileName.length][];
		for (int i = 0; i < fileName.length; i++) {
			result[i] = new Object[] { new TestParameters("Content Classification Logs Generation/Validation for Executable risks file types:"+fileName[i]+" for classification: executable", 
					"Upload Executable content type file:"+fileName[i]+ ". Then verify content classification logs are getting generated within the SLA provided"+" for classifications: executable",
					fileName[i], SaasType.getSaasFilterType(suiteData.getSaasApp()))};
		}

		mainCounter = 0;testCounter = DCIConstants.DCI_COUNTER_MAX;		
		return result;
	}

	@DataProvider(name = "dataUploadCryptographicKeysPositive")
	public Object[][] dataUploadCryptographicKeysPositive() {

		dciFunctions = new DCIFunctions();

		String[] fileName = dciFunctions.getFileName(DCIConstants.DCI_FILE_UPLOAD_CRYPTO_PATH);	
		fileName = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_CRYPTO_PATH, fileName);

		dciFunctions.fileUploadOrSendEmail(suiteData, folderInfo, fileName);

		Object[][] result = new Object[fileName.length][];
		for (int i = 0; i < fileName.length; i++) {
			result[i] = new Object[] { new TestParameters("Content Classification Logs Generation/Validation for Crypto risks file types:"+fileName[i]+" for classification: crypto", 
					"Upload Cryptographic Keys content type file:"+fileName[i]+ ". Then verify content classification logs are getting generated within the SLA provided"+" for classifications: crypto",
					fileName[i], SaasType.getSaasFilterType(suiteData.getSaasApp()))};
		}

		mainCounter = 0;testCounter = DCIConstants.DCI_COUNTER_MAX;		
		return result;
	}

	@DataProvider(name = "dataUploadBusinessPositive")
	public Object[][] dataUploadBusinessPositive() {

		dciFunctions = new DCIFunctions();

		String[] fileName = dciFunctions.getFileName(DCIConstants.DCI_FILE_UPLOAD_BUSINESS_PATH);	
		fileName = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_BUSINESS_PATH, fileName);

		dciFunctions.fileUploadOrSendEmail(suiteData, folderInfo, fileName);

		Object[][] result = new Object[fileName.length][];
		for (int i = 0; i < fileName.length; i++) {
			result[i] = new Object[] { new TestParameters("Content Classification Logs Generation/Validation for Business risks file types:"+fileName[i]+" for classification: image", 
					"Upload business content type file:"+fileName[i]+ ". Then verify content classification logs are getting generated within the SLA provided"+" for classifications: image",
					fileName[i], SaasType.getSaasFilterType(suiteData.getSaasApp()))};
		}

		mainCounter = 0;testCounter = DCIConstants.DCI_COUNTER_MAX;		
		return result;
	}

	@DataProvider(name = "dataUploadComputingPositive")
	public Object[][] dataUploadComputingPositive() {

		dciFunctions = new DCIFunctions();

		String[] fileName = dciFunctions.getFileName(DCIConstants.DCI_FILE_UPLOAD_COMPUTING_PATH);	
		fileName = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_COMPUTING_PATH, fileName);

		dciFunctions.fileUploadOrSendEmail(suiteData, folderInfo, fileName);

		Object[][] result = new Object[fileName.length][];
		for (int i = 0; i < fileName.length; i++) {
			result[i] = new Object[] { new TestParameters("Content Classification Logs Generation/Validation for Computing risks file types:"+fileName[i]+" for classification: computing", 
					"Upload Computing content type file:"+fileName[i]+ ". Then verify content classification logs are getting generated within the SLA provided"+" for classifications: computing",
					fileName[i], SaasType.getSaasFilterType(suiteData.getSaasApp()))};
		}

		mainCounter = 0;testCounter = DCIConstants.DCI_COUNTER_MAX;		
		return result;
	}

	@DataProvider(name = "dataUploadDesignPositive")
	public Object[][] dataUploadDesignPositive() {

		dciFunctions = new DCIFunctions();

		String[] fileName = dciFunctions.getFileName(DCIConstants.DCI_FILE_UPLOAD_DESIGN_PATH);	
		fileName = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_DESIGN_PATH, fileName);

		dciFunctions.fileUploadOrSendEmail(suiteData, folderInfo, fileName);

		Object[][] result = new Object[fileName.length][];
		for (int i = 0; i < fileName.length; i++) {
			result[i] = new Object[] { new TestParameters("Content Classification Logs Generation/Validation for Design risks file types:"+fileName[i]+" for classification: design", 
					"Upload Design Doc content type file:"+fileName[i]+ ". Then verify content classification logs are getting generated within the SLA provided"+" for classifications: design",
					fileName[i], SaasType.getSaasFilterType(suiteData.getSaasApp()))};
		}

		mainCounter = 0;testCounter = DCIConstants.DCI_COUNTER_MAX;		
		return result;
	}

	@DataProvider(name = "dataUploadEngineeringPositive")
	public Object[][] dataUploadEngineeringPositive() {

		dciFunctions = new DCIFunctions();

		String[] fileName = dciFunctions.getFileName(DCIConstants.DCI_FILE_UPLOAD_ENGINEERING_PATH);	
		fileName = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_ENGINEERING_PATH, fileName);

		dciFunctions.fileUploadOrSendEmail(suiteData, folderInfo, fileName);

		Object[][] result = new Object[fileName.length][];
		for (int i = 0; i < fileName.length; i++) {
			result[i] = new Object[] { new TestParameters("Content Classification Logs Generation/Validation for Engineering risks file types:"+fileName[i]+" for classification: engineering", 
					"Upload Engineering content type file:"+fileName[i]+ ". Then verify content classification logs are getting generated within the SLA provided"+" for classifications: engineering",
					fileName[i], SaasType.getSaasFilterType(suiteData.getSaasApp()))};
		}

		mainCounter = 0;testCounter = DCIConstants.DCI_COUNTER_MAX;		
		return result;
	}

	@DataProvider(name = "dataUploadHealthPositive")
	public Object[][] dataUploadHealthPositive() {

		dciFunctions = new DCIFunctions();

		String[] fileName = dciFunctions.getFileName(DCIConstants.DCI_FILE_UPLOAD_HEALTH_PATH);	
		fileName = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_HEALTH_PATH, fileName);

		dciFunctions.fileUploadOrSendEmail(suiteData, folderInfo, fileName);

		Object[][] result = new Object[fileName.length][];
		for (int i = 0; i < fileName.length; i++) {
			result[i] = new Object[] { new TestParameters("Content Classification Logs Generation/Validation for Health risks file types:"+fileName[i]+" for classification: health", 
					"Upload Health content type file:"+fileName[i]+ ". Then verify content classification logs are getting generated within the SLA provided"+" for classifications: health",
					fileName[i], SaasType.getSaasFilterType(suiteData.getSaasApp()))};
		}

		mainCounter = 0;testCounter = DCIConstants.DCI_COUNTER_MAX;		
		return result;
	}

	@DataProvider(name = "dataUploadLegalPositive")
	public Object[][] dataUploadLegalPositive() {

		dciFunctions = new DCIFunctions();

		String[] fileName = dciFunctions.getFileName(DCIConstants.DCI_FILE_UPLOAD_LEGAL_PATH);	
		fileName = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_LEGAL_PATH, fileName);

		dciFunctions.fileUploadOrSendEmail(suiteData, folderInfo, fileName);

		Object[][] result = new Object[fileName.length][];
		for (int i = 0; i < fileName.length; i++) {
			result[i] = new Object[] { new TestParameters("Content Classification Logs Generation/Validation for Legal risks file types:"+fileName[i]+" for classification: legal", 
					"Upload Legal content type file:"+fileName[i]+ ". Then verify content classification logs are getting generated within the SLA provided"+" for classifications: legal",
					fileName[i], SaasType.getSaasFilterType(suiteData.getSaasApp()))};
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


	private void verifyContentTypesDisplayLogs(DCIFunctions dciFunctions, String fileName, String saasType, List<NameValuePair> headers)
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
							DCIConstants.CIInformationalSeverityType, fileName, DCIConstants.CIFacilityType, DCIConstants.CIActivityType);
			
			Logger.info("****************Input Payload****************");
			Logger.info(payload);
			Logger.info("*********************************************");

			hits = fetchDisplayLogsCounter(dciFunctions, headers, payload, DCIConstants.DCI_COUNTER_MAX, riskCount);

			Logger.info("****************Output Response****************");
			Logger.info(hits);
			Logger.info("***********************************************");

			Map<String, String> CIJson = dciFunctions.
					populateContentInspectionJson(suiteData,fileName, riskCount, 
							"informational", "API", true);
			
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

	/**********************************************UTIL METHODS***********************************************/
	/**********************************************BEFORE/AFTER CLASS*****************************************/

	/**
	 * Delete all CIQ profiles
	 */
	@BeforeClass(groups ={"image","audio","video","executable",
			"crypto","business","computing","design","engineering","health","legal"})
	public void deleteAllCIQProfiles() {
		dciFunctions.deleteAllPolicies(restClient, suiteData);
		dciFunctions.deleteAllCIQProfiles(restClient, suiteData);
		dciFunctions.deleteAllDictionaries(restClient, suiteData);
		dciFunctions.deleteAllTrainingProfiles(restClient, suiteData);
	}



	/**
	 * Create folders in saas apps
	 */
	@BeforeClass(groups ={"image","audio","video","executable",
			"crypto","business","computing","design","engineering","health","legal"})
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
	@AfterClass(groups ={"image","audio","video","executable",
			"crypto","business","computing","design","engineering","health","legal"})
	public void deleteFolder() {
		try {
			UserAccount account = dciFunctions.getUserAccount(suiteData);
			UniversalApi universalApi = dciFunctions.getUniversalApi(suiteData, account);

			dciFunctions.deleteFolder(universalApi, suiteData, folderInfo);
		} catch (Exception ex) {
			Logger.info("Issue with Delete Folder Operation " + ex.getLocalizedMessage());
		}
	}

	@BeforeMethod(groups ={"image","audio","video","executable",
			"crypto","business","computing","design","engineering","health","legal"})
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
			s3.downloadWholeFolder(DCIConstants.DCI_S3_BUCKET, "DCI/Content", 
					new File(DCIConstants.DCI_FILE_TEMP_PATH));

		} catch (Exception ex) {
			Logger.info("Downloading folder from S3 is failed with exception " + ex.getLocalizedMessage());
		}
	}

	/**********************************************BEFORE/AFTER CLASS*****************************************/


}
