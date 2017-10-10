package com.elastica.beatle.tests.dci.tp;

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


public class DCITrainingProfileCombinationTests extends DCICommonTest implements ITest {

	protected String mTestCaseName = "";
	DCIFunctions dciFunctions = null;
	ElasticSearchLogs esLogs = null;
	Map<String,String> folderInfo = new HashMap<String,String>();
	String uniqueId = UUID.randomUUID().toString();
	int mainCounter = 0;int testCounter = 0;


	/**********************************************TEST METHODS***********************************************/

	
	@Test(dataProvider = "dataUploadFERPA", groups ={"FERPA"})
	public void testDisplayLogsForFERPARiskGeneratedInTrainingProfileCIQProfile(TestParameters testParams) throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();

		Logger.info(
				"************************************Starting "+testParams.getTestName()+" for filename:" + testParams.getFileName() + " and saas app type:" + testParams.getSaasType()+ "******************");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description:"+testParams.getTestDescription());
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output****************");
		Logger.info("Filename:"+testParams.getFileName());
		Logger.info("Risk Type:"+testParams.getRisks());
		Logger.info("CIQ Profilename:"+testParams.getCiq().get("ciqProfileName"));
		Logger.info("Saas App Type:" + testParams.getSaasType());
		Logger.info("*****************************************************");

		List<NameValuePair> headers = dciFunctions.getBasicHeaders(suiteData);		
		verifyRiskTypesDisplayLogs(dciFunctions, testParams.getFileName(), SaasType.getSaasFilterType(suiteData.getSaasApp()),
				headers, testParams.getCiq());

		
		Logger.info(
				"************************************Completed "+testParams.getTestName()+" for filename:" + testParams.getFileName() + " and saas app type:" + testParams.getSaasType()+ "******************");

	}
	
	@Test(dataProvider = "dataUploadGLBA", groups ={"GLBA"})
	public void testDisplayLogsForGLBARiskGeneratedInTrainingProfileCIQProfile(TestParameters testParams) throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();

		Logger.info(
				"************************************Starting "+testParams.getTestName()+" for filename:" + testParams.getFileName() + " and saas app type:" + testParams.getSaasType()+ "******************");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description:"+testParams.getTestDescription());
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output****************");
		Logger.info("Filename:"+testParams.getFileName());
		Logger.info("Risk Type:"+testParams.getRisks());
		Logger.info("CIQ Profilename:"+testParams.getCiq().get("ciqProfileName"));
		Logger.info("Saas App Type:" + testParams.getSaasType());
		Logger.info("*****************************************************");

		List<NameValuePair> headers = dciFunctions.getBasicHeaders(suiteData);		
		verifyRiskTypesDisplayLogs(dciFunctions, testParams.getFileName(), SaasType.getSaasFilterType(suiteData.getSaasApp()),
				headers, testParams.getCiq());

		
		Logger.info(
				"************************************Completed "+testParams.getTestName()+" for filename:" + testParams.getFileName() + " and saas app type:" + testParams.getSaasType()+ "******************");

	}
	
	@Test(dataProvider = "dataUploadHIPAA", groups ={"HIPAA"})
	public void testDisplayLogsForHIPAARiskGeneratedInTrainingProfileCIQProfile(TestParameters testParams) throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();

		Logger.info(
				"************************************Starting "+testParams.getTestName()+" for filename:" + testParams.getFileName() + " and saas app type:" + testParams.getSaasType()+ "******************");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description:"+testParams.getTestDescription());
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output****************");
		Logger.info("Filename:"+testParams.getFileName());
		Logger.info("Risk Type:"+testParams.getRisks());
		Logger.info("CIQ Profilename:"+testParams.getCiq().get("ciqProfileName"));
		Logger.info("Saas App Type:" + testParams.getSaasType());
		Logger.info("*****************************************************");

		List<NameValuePair> headers = dciFunctions.getBasicHeaders(suiteData);		
		verifyRiskTypesDisplayLogs(dciFunctions, testParams.getFileName(), SaasType.getSaasFilterType(suiteData.getSaasApp()),
				headers, testParams.getCiq());

		
		Logger.info(
				"************************************Completed "+testParams.getTestName()+" for filename:" + testParams.getFileName() + " and saas app type:" + testParams.getSaasType()+ "******************");

	}
	
	@Test(dataProvider = "dataUploadPCI", groups ={"PCI"})
	public void testDisplayLogsForPCIRiskGeneratedInTrainingProfileCIQProfile(TestParameters testParams) throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();

		Logger.info(
				"************************************Starting "+testParams.getTestName()+" for filename:" + testParams.getFileName() + " and saas app type:" + testParams.getSaasType()+ "******************");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description:"+testParams.getTestDescription());
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output****************");
		Logger.info("Filename:"+testParams.getFileName());
		Logger.info("Risk Type:"+testParams.getRisks());
		Logger.info("CIQ Profilename:"+testParams.getCiq().get("ciqProfileName"));
		Logger.info("Saas App Type:" + testParams.getSaasType());
		Logger.info("*****************************************************");

		List<NameValuePair> headers = dciFunctions.getBasicHeaders(suiteData);		
		verifyRiskTypesDisplayLogs(dciFunctions, testParams.getFileName(), SaasType.getSaasFilterType(suiteData.getSaasApp()),
				headers, testParams.getCiq());

		
		Logger.info(
				"************************************Completed "+testParams.getTestName()+" for filename:" + testParams.getFileName() + " and saas app type:" + testParams.getSaasType()+ "******************");

	}
	
	@Test(dataProvider = "dataUploadPII", groups ={"PII"})
	public void testDisplayLogsForPIIRiskGeneratedInTrainingProfileCIQProfile(TestParameters testParams) throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();

		Logger.info(
				"************************************Starting "+testParams.getTestName()+" for filename:" + testParams.getFileName() + " and saas app type:" + testParams.getSaasType()+ "******************");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description:"+testParams.getTestDescription());
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output****************");
		Logger.info("Filename:"+testParams.getFileName());
		Logger.info("Risk Type:"+testParams.getRisks());
		Logger.info("CIQ Profilename:"+testParams.getCiq().get("ciqProfileName"));
		Logger.info("Saas App Type:" + testParams.getSaasType());
		Logger.info("*****************************************************");

		List<NameValuePair> headers = dciFunctions.getBasicHeaders(suiteData);		
		verifyRiskTypesDisplayLogs(dciFunctions, testParams.getFileName(), SaasType.getSaasFilterType(suiteData.getSaasApp()),
				headers, testParams.getCiq());

		
		Logger.info(
				"************************************Completed "+testParams.getTestName()+" for filename:" + testParams.getFileName() + " and saas app type:" + testParams.getSaasType()+ "******************");

	}
	
	

	/**********************************************TEST METHODS***********************************************/
	/**********************************************DATA PROVIDERS
	 * @throws Exception *********************************************/

	@SuppressWarnings("unchecked")
	@DataProvider(name = "dataUploadFERPA")
	public Object[][] dataUploadFERPA() throws Exception {
		dciFunctions = new DCIFunctions();

		/** Create Dictionary **/
		List<NameValuePair> headers = dciFunctions.getBrowserHeaders(suiteData);

		String dictName = "custom_dictionaries_only";
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
		
		for(int i=0;i<mlProfileNames.length;i++){
			String mlProfileName = mlProfileNames[i];
			createTrainingProfile(mlProfileName);
		}
		
		dciFunctions.waitForMinutes(15);
		
		for(String mlProfileName:mlProfileNames){
			dciFunctions.activateDeactivateMLProfile(suiteData, restClient, mlProfileName, true);
		}
		
		String[] fileName = dciFunctions.getFileName(DCIConstants.DCI_TP_FERPA_COMBO_UPLOAD_PATH);
		String[] fileNameNoExtn = dciFunctions.getFileNameNoExtension(DCIConstants.DCI_TP_FERPA_COMBO_UPLOAD_PATH);
		String[] risks = dciFunctions.riskTypesForAFile(fileName);

		Map<String, String>[] ciqArray = new HashMap[fileNameNoExtn.length];
		for (int i = 0; i < fileNameNoExtn.length; i++) {	
			Map<String,String> ciq = createCIQProfile(fileNameNoExtn[i]);
			ciqArray[i] = ciq;
			
		}
		
		fileName = dciFunctions.createSampleFileType(DCIConstants.DCI_TP_FERPA_COMBO_UPLOAD_PATH,
					fileName);
		dciFunctions.fileUploadOrSendEmail(suiteData, folderInfo, fileName);

		Object[][] result = new Object[fileName.length][];
		for (int i = 0; i < fileName.length;i++) {
			result[i] = new Object[] { new TestParameters("Risk Generation/Validation for ContentIQ Profile:"+ciqArray[i].get("ciqProfileName")+"for file:"+fileName[i], 
							"Create CIQ Profile with name:"+ciqArray[i].get("ciqProfileName")+" and upload file:"+fileName[i]+ ". Then verify risk logs are getting generated within the SLA provided",
							fileName[i], SaasType.getSaasFilterType(suiteData.getSaasApp()), Arrays.asList(risks[i].split(",")), ciqArray[i])};
			
		}

		mainCounter = 0;testCounter = DCIConstants.DCI_COUNTER_MAX;
		
		return result;
	}


	@SuppressWarnings("unchecked")
	@DataProvider(name = "dataUploadGLBA")
	public Object[][] dataUploadGLBA() throws Exception {
		dciFunctions = new DCIFunctions();

		/** Create Dictionary **/
		List<NameValuePair> headers = dciFunctions.getBrowserHeaders(suiteData);

		String dictName = "custom_dictionaries_only";
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
		
		for(int i=0;i<mlProfileNames.length;i++){
			String mlProfileName = mlProfileNames[i];
			createTrainingProfile(mlProfileName);
		}
		
		dciFunctions.waitForMinutes(15);
		
		for(String mlProfileName:mlProfileNames){
			dciFunctions.activateDeactivateMLProfile(suiteData, restClient, mlProfileName, true);
		}
		
		String[] fileName = dciFunctions.getFileName(DCIConstants.DCI_TP_GLBA_COMBO_UPLOAD_PATH);
		String[] fileNameNoExtn = dciFunctions.getFileNameNoExtension(DCIConstants.DCI_TP_GLBA_COMBO_UPLOAD_PATH);
		String[] risks = dciFunctions.riskTypesForAFile(fileName);

		Map<String, String>[] ciqArray = new HashMap[fileNameNoExtn.length];
		for (int i = 0; i < fileNameNoExtn.length; i++) {	
			Map<String,String> ciq = createCIQProfile(fileNameNoExtn[i]);
			ciqArray[i] = ciq;
			
		}
		
		fileName = dciFunctions.createSampleFileType(DCIConstants.DCI_TP_GLBA_COMBO_UPLOAD_PATH,
				fileName);
		dciFunctions.fileUploadOrSendEmail(suiteData, folderInfo, fileName);

		Object[][] result = new Object[fileName.length][];
		for (int i = 0; i < fileName.length;i++) {
			result[i] = new Object[] { new TestParameters("Risk Generation/Validation for ContentIQ Profile:"+ciqArray[i].get("ciqProfileName")+"for file:"+fileName[i], 
							"Create CIQ Profile with name:"+ciqArray[i].get("ciqProfileName")+" and upload file:"+fileName[i]+ ". Then verify risk logs are getting generated within the SLA provided",
							fileName[i], SaasType.getSaasFilterType(suiteData.getSaasApp()), Arrays.asList(risks[i].split(",")), ciqArray[i])};
			
		}

		mainCounter = 0;testCounter = DCIConstants.DCI_COUNTER_MAX;
		
		return result;
	}
	
	@SuppressWarnings("unchecked")
	@DataProvider(name = "dataUploadHIPAA")
	public Object[][] dataUploadHIPAA() throws Exception {
		dciFunctions = new DCIFunctions();

		/** Create Dictionary **/
		List<NameValuePair> headers = dciFunctions.getBrowserHeaders(suiteData);

		String dictName = "custom_dictionaries_only";
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
		
		for(int i=0;i<mlProfileNames.length;i++){
			String mlProfileName = mlProfileNames[i];
			createTrainingProfile(mlProfileName);
		}
		
		dciFunctions.waitForMinutes(15);
		
		for(String mlProfileName:mlProfileNames){
			dciFunctions.activateDeactivateMLProfile(suiteData, restClient, mlProfileName, true);
		}
		
		String[] fileName = dciFunctions.getFileName(DCIConstants.DCI_TP_HIPAA_COMBO_UPLOAD_PATH);
		String[] fileNameNoExtn = dciFunctions.getFileNameNoExtension(DCIConstants.DCI_TP_HIPAA_COMBO_UPLOAD_PATH);
		String[] risks = dciFunctions.riskTypesForAFile(fileName);

		Map<String, String>[] ciqArray = new HashMap[fileNameNoExtn.length];
		for (int i = 0; i < fileNameNoExtn.length; i++) {	
			Map<String,String> ciq = createCIQProfile(fileNameNoExtn[i]);
			ciqArray[i] = ciq;
			
		}
		
		fileName = dciFunctions.createSampleFileType(DCIConstants.DCI_TP_HIPAA_COMBO_UPLOAD_PATH,
				fileName);
		dciFunctions.fileUploadOrSendEmail(suiteData, folderInfo, fileName);

		Object[][] result = new Object[fileName.length][];
		for (int i = 0; i < fileName.length;i++) {
			result[i] = new Object[] { new TestParameters("Risk Generation/Validation for ContentIQ Profile:"+ciqArray[i].get("ciqProfileName")+"for file:"+fileName[i], 
							"Create CIQ Profile with name:"+ciqArray[i].get("ciqProfileName")+" and upload file:"+fileName[i]+ ". Then verify risk logs are getting generated within the SLA provided",
							fileName[i], SaasType.getSaasFilterType(suiteData.getSaasApp()), Arrays.asList(risks[i].split(",")), ciqArray[i])};
			
		}

		mainCounter = 0;testCounter = DCIConstants.DCI_COUNTER_MAX;
		
		return result;
	}
	
	@SuppressWarnings("unchecked")
	@DataProvider(name = "dataUploadPCI")
	public Object[][] dataUploadPCI() throws Exception {
		dciFunctions = new DCIFunctions();

		/** Create Dictionary **/
		List<NameValuePair> headers = dciFunctions.getBrowserHeaders(suiteData);

		String dictName = "custom_dictionaries_only";
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
		
		for(int i=0;i<mlProfileNames.length;i++){
			String mlProfileName = mlProfileNames[i];
			createTrainingProfile(mlProfileName);
		}
		
		dciFunctions.waitForMinutes(15);
		
		for(String mlProfileName:mlProfileNames){
			dciFunctions.activateDeactivateMLProfile(suiteData, restClient, mlProfileName, true);
		}
		
		String[] fileName = dciFunctions.getFileName(DCIConstants.DCI_TP_PCI_COMBO_UPLOAD_PATH);
		String[] fileNameNoExtn = dciFunctions.getFileNameNoExtension(DCIConstants.DCI_TP_PCI_COMBO_UPLOAD_PATH);
		String[] risks = dciFunctions.riskTypesForAFile(fileName);

		Map<String, String>[] ciqArray = new HashMap[fileNameNoExtn.length];
		for (int i = 0; i < fileNameNoExtn.length; i++) {	
			Map<String,String> ciq = createCIQProfile(fileNameNoExtn[i]);
			ciqArray[i] = ciq;
			
		}
		
		fileName = dciFunctions.createSampleFileType(DCIConstants.DCI_TP_PCI_COMBO_UPLOAD_PATH,
				fileName);
		dciFunctions.fileUploadOrSendEmail(suiteData, folderInfo, fileName);

		Object[][] result = new Object[fileName.length][];
		for (int i = 0; i < fileName.length;i++) {
			result[i] = new Object[] { new TestParameters("Risk Generation/Validation for ContentIQ Profile:"+ciqArray[i].get("ciqProfileName")+"for file:"+fileName[i], 
							"Create CIQ Profile with name:"+ciqArray[i].get("ciqProfileName")+" and upload file:"+fileName[i]+ ". Then verify risk logs are getting generated within the SLA provided",
							fileName[i], SaasType.getSaasFilterType(suiteData.getSaasApp()), Arrays.asList(risks[i].split(",")), ciqArray[i])};
			
		}

		mainCounter = 0;testCounter = DCIConstants.DCI_COUNTER_MAX;
		
		return result;
	}
	
	@SuppressWarnings("unchecked")
	@DataProvider(name = "dataUploadPII")
	public Object[][] dataUploadPII() throws Exception {
		dciFunctions = new DCIFunctions();

		/** Create Dictionary **/
		List<NameValuePair> headers = dciFunctions.getBrowserHeaders(suiteData);

		String dictName = "custom_dictionaries_only";
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
		
		for(int i=0;i<mlProfileNames.length;i++){
			String mlProfileName = mlProfileNames[i];
			createTrainingProfile(mlProfileName);
		}
		
		dciFunctions.waitForMinutes(15);
		
		for(String mlProfileName:mlProfileNames){
			dciFunctions.activateDeactivateMLProfile(suiteData, restClient, mlProfileName, true);
		}
		
		String[] fileName = dciFunctions.getFileName(DCIConstants.DCI_TP_PII_COMBO_UPLOAD_PATH);
		String[] fileNameNoExtn = dciFunctions.getFileNameNoExtension(DCIConstants.DCI_TP_PII_COMBO_UPLOAD_PATH);
		String[] risks = dciFunctions.riskTypesForAFile(fileName);

		Map<String, String>[] ciqArray = new HashMap[fileNameNoExtn.length];
		for (int i = 0; i < fileNameNoExtn.length; i++) {	
			Map<String,String> ciq = createCIQProfile(fileNameNoExtn[i]);
			ciqArray[i] = ciq;
			
		}
		
		fileName = dciFunctions.createSampleFileType(DCIConstants.DCI_TP_PII_COMBO_UPLOAD_PATH,
				fileName);
		dciFunctions.fileUploadOrSendEmail(suiteData, folderInfo, fileName);

		Object[][] result = new Object[fileName.length][];
		for (int i = 0; i < fileName.length;i++) {
			result[i] = new Object[] { new TestParameters("Risk Generation/Validation for ContentIQ Profile:"+ciqArray[i].get("ciqProfileName")+"for file:"+fileName[i], 
							"Create CIQ Profile with name:"+ciqArray[i].get("ciqProfileName")+" and upload file:"+fileName[i]+ ". Then verify risk logs are getting generated within the SLA provided",
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


	public void createTrainingProfile(String mlProfileName) throws Exception{
		List<NameValuePair> headers = dciFunctions.getCookieHeaders(suiteData);
		
		Logger.info("Creating ML profile name:"+mlProfileName+" in progress");
		
		dciFunctions.createNewMLProfile(suiteData, restClient, mlProfileName, mlProfileName+" Sample Description");
	
		String filePathPS = DCIConstants.DCI_TP_UPLOAD_PATH + File.separator + mlProfileName + File.separator + "Positive";
		String filePathNS = DCIConstants.DCI_TP_UPLOAD_PATH + File.separator + mlProfileName + File.separator + "Negative";
		
		String[] trainingFilesPositive = dciFunctions.getFileName(filePathPS);
		String[] trainingFilesNegative = dciFunctions.getFileName(filePathNS);
		
		for(String fileName: trainingFilesPositive){
			dciFunctions.uploadFileIntoTrainingProfile(suiteData, restClient, headers, mlProfileName,fileName, 
					"PS", filePathPS+ File.separator +fileName);
		}
		for(String fileName: trainingFilesNegative){
			dciFunctions.uploadFileIntoTrainingProfile(suiteData, restClient, headers, mlProfileName,fileName, 
					"NS", filePathNS+ File.separator +fileName);
		}
		
		dciFunctions.processMLProfile(suiteData, restClient, mlProfileName);
		
		Logger.info("Creating ML profile name:"+mlProfileName+" is completed");
	}
	
	
	
	private Map<String,String> createCIQProfile(String fileName) throws Exception{
		String ciqProfileName=fileName+" Name";
		String ciqProfileDescription=fileName+" Description";
		
		String PDT="France National Identification Number (INSEE)";
		String PDD="Illegal Drugs";
		String CD="custom_dictionaries_only";
		String CT="custom_terms_only";
		String fileFormat="class:WORDPROCESSOR";
		String mlProfileName=getMLProfileCategory(fileName);
		String riskTypes=getRiskCategory(fileName);
		String contentTypes=getContentCategory(fileName);
		
		Logger.info("Creating CIQ profile with name:"+ciqProfileName+" with ML profile name:"+mlProfileName+" in progress");
	
		List<String> valuesPreDefDict=new ArrayList<String>();valuesPreDefDict.add(PDD);
		List<String> valuesPreDefTerms=new ArrayList<String>();valuesPreDefTerms.add(PDT);
		List<String> valuesCustomDict=new ArrayList<String>();valuesCustomDict.add(CD);
		List<String> valuesCustomTerms=new ArrayList<String>();valuesCustomTerms.add(CT);
		List<String> valuesFileFormat=new ArrayList<String>();valuesFileFormat.add(fileFormat);
		List<String> valuesRiskTypes=new ArrayList<String>();valuesFileFormat.add(riskTypes);
		List<String> valuesContentTypes=new ArrayList<String>();valuesFileFormat.add(contentTypes);
		List<String> valuesMLProfile=new ArrayList<String>();valuesMLProfile.add(mlProfileName);
		
		dciFunctions.createCIQProfile(suiteData, restClient, ciqProfileName, ciqProfileDescription, 
				valuesPreDefDict, valuesPreDefTerms, valuesCustomDict, valuesCustomTerms, 
				valuesRiskTypes, valuesContentTypes, valuesFileFormat, valuesMLProfile,
				"high", 1, true, 1, false);

		Map<String, String> ciq=new HashMap<String, String>();
		ciq.put("ciqProfileName", ciqProfileName);
		ciq.put("ciqProfileType", "PDT_PDD_CD_CT_FileFormat_Risk_Content_MLProfile");
		ciq.put("ciqProfileDescription", ciqProfileDescription);
		ciq.put("ciqProfileKeyword", "");
		ciq.put("ciqProfileCount", "1");
		ciq.put("ciqType", "PDT_PDD_CD_CT_FileFormat_Risk_Content_MLProfile");
		ciq.put("ciqPDT", PDT);
		ciq.put("ciqPDD", PDD);
		ciq.put("ciqCT", CT);
		ciq.put("ciqCD", CD);
		ciq.put("ciqMLProfile", mlProfileName);
		
		Logger.info("Creating CIQ profile with name:"+ciqProfileName+" with ML profile name:"+mlProfileName+" is completed");
		
		return ciq;
	}


	private void verifyRiskTypesDisplayLogs(DCIFunctions dciFunctions, String fileName, String saasType, List<NameValuePair> headers,
			Map<String, String> ciq)
			throws Exception{
		String hits = "";
		
		int riskCount=1;
		
		try {

			String payload = 
					dciFunctions.getSearchQueryForDCI(suiteData, dciFunctions.getMinusMinutesFromCurrentTime(1440) , 
							dciFunctions.getPlusMinutesFromCurrentTime(120), saasType, DCIConstants.CISourceType, 
							DCIConstants.CICriticalSeverityType, fileName, DCIConstants.CIFacilityType, DCIConstants.CIActivityType);
			
			Logger.info("****************Input Payload****************");
			Logger.info(payload);
			Logger.info("*********************************************");

			hits = fetchDisplayLogsCounter(dciFunctions, headers, payload, DCIConstants.DCI_COUNTER_MEDIUM, riskCount);

			Logger.info("****************Output Response****************");
			Logger.info(hits);
			Logger.info("***********************************************");

			Map<String, String> CIJson = dciFunctions.
					populateContentInspectionJson(suiteData,fileName, riskCount, 
							"critical", "API", true, ciq);
			
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
		
		if(riskCount==0){
			HttpResponse response = esLogs.getDisplayLogs(restClient, headers, suiteData.getApiserverHostName(), new StringEntity(payload));
			responseBody = ClientUtil.getResponseBody(response);
		}else{
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
		}
		
		
		
		return dciFunctions.getJSONValue(responseBody, "hits");
	}

	private String getMLProfileCategory(String fileName){
		String category = "";
		for(String mlProfile:mlProfileNames){
			if(fileName.contains(mlProfile)){
				category = mlProfile;
				break;
			}
		}
		
		
		return category;
	}
	
	private String getRiskCategory(String fileName){
		String category = "";
		
		for(String risk:risks){
			if(fileName.contains(risk)){
				category = risk;
				break;
			}
		}
		
		
		return category;
	}
	
	private String getContentCategory(String fileName){
		String category = "";
		for(String content:contents){
			if(fileName.contains("cryptographic_keys")){
				category = "cryptographic_keys";
				break;
			}else if(fileName.contains("design")){
				category = "design doc";
				break;
			}else if(fileName.contains("sourcecode")){
				category = "source_code";
				break;
			}else if(fileName.contains(content)){
				category = content;
				break;
			}
		}
		
		
		
		
		return category;
	}

	/**********************************************UTIL METHODS***********************************************/
	/**********************************************BEFORE/AFTER CLASS*****************************************/

	/**
	 * Create folders in saas apps
	 */
	@BeforeClass(groups ={"FERPA","GLBA","HIPAA","PII","PCI"})
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
	@AfterClass(groups ={"FERPA","GLBA","HIPAA","PII","PCI"})
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
	@BeforeClass(groups ={"FERPA","GLBA","HIPAA","PII","PCI"})
	public void deleteContentIqProfileBeforeTestStarts() {
		dciFunctions = new DCIFunctions();
		dciFunctions.deleteAllCIQProfiles(restClient, suiteData);
		dciFunctions.deleteAllDictionaries(restClient, suiteData);
		dciFunctions.deleteAllTrainingProfiles(restClient, suiteData);
	}

	/**
	 * Delete content iq profile
	 */
	@AfterClass(groups ={"FERPA","GLBA","HIPAA","PII","PCI"})
	public void deleteContentIqProfileAfterTestEnds() {
		dciFunctions = new DCIFunctions();
		dciFunctions.deleteAllCIQProfiles(restClient, suiteData);
		dciFunctions.deleteAllDictionaries(restClient, suiteData);
		dciFunctions.deleteAllTrainingProfiles(restClient, suiteData);
	}


	@BeforeMethod(groups ={"FERPA","GLBA","HIPAA","PII","PCI"})
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

	public String[] mlProfileNames= {
			"1040",
			"1099",
			"Accessory_Dwelling_Application",
			"Building_Permit_Application",
			"Fafsa",
			"Lease",
			"Mortgage_Release",
			"Subpoena_Civil_Case"
	};
	
	String[] risks={"dlp","hipaa","vba_macros","virus","pci",
			"pii","ferpa","glba"};
	
	String[] contents={"business","computing","cryptographic_keys","design doc",
	"encryption","engineering","health","legal","source_code"};
	
	/**********************************************BEFORE/AFTER METHODS/CLASS*****************************************/
	

}