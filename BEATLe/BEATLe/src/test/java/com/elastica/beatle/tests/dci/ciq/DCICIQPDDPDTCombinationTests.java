package com.elastica.beatle.tests.dci.ciq;

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

public class DCICIQPDDPDTCombinationTests extends DCICommonTest implements ITest {

	protected String mTestCaseName = "";
	DCIFunctions dciFunctions = null;
	ElasticSearchLogs esLogs = null;
	Map<String,String> folderInfo = new HashMap<String,String>();
	String uniqueId = UUID.randomUUID().toString();
	int mainCounter = 0;int testCounter = 0;

	/**********************************************TEST METHODS***********************************************/

	@Test(dataProvider = "dataUploadCC", groups ={"CC"})
	public void testDisplayLogsForRiskGeneratedInPDTCompanyConfidential(TestParameters testParams) throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();

		Logger.info(
				"************************************Starting "+testParams.getTestName()+" for filename:" + testParams.getFileName() + " and saas app type:" + testParams.getSaasType()+ "******************");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description:"+testParams.getTestDescription());
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output****************");
		Logger.info("Filename:"+testParams.getFileName());Logger.info("Risk Type:"+Arrays.asList(testParams.getRisks()));
		Logger.info("CIQ Profilename:"+testParams.getCiq().get("ciqProfileName"));
		Logger.info("Predefined Terms:"+testParams.getCiq().get("ciqPdt"));
		Logger.info("Predefined Dictionaries:"+testParams.getCiq().get("ciqPdd"));
		Logger.info("*****************************************************");


		List<NameValuePair> headers = dciFunctions.getBasicHeaders(suiteData);		
		verifyDisplayLogsPDTPDDCombinations(dciFunctions, esLogs, headers, testParams.getFileName(), testParams.getSaasType(), 
				testParams.getRisks(), testParams.getCiq());

		Logger.info(
				"************************************Completed "+testParams.getTestName()+" for filename:" + testParams.getFileName() + " and saas app type:" + testParams.getSaasType()+ "******************");

	}

	@Test(dataProvider = "dataUploadDIS", groups ={"DIS"})
	public void testDisplayLogsForRiskGeneratedInPDTDiseases(TestParameters testParams) throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();

		Logger.info(
				"************************************Starting "+testParams.getTestName()+" for filename:" + testParams.getFileName() + " and saas app type:" + testParams.getSaasType()+ "******************");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description:"+testParams.getTestDescription());
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output****************");
		Logger.info("Filename:"+testParams.getFileName());Logger.info("Risk Type:"+Arrays.asList(testParams.getRisks()));
		Logger.info("CIQ Profilename:"+testParams.getCiq().get("ciqProfileName"));
		Logger.info("Predefined Terms:"+testParams.getCiq().get("ciqPdt"));
		Logger.info("Predefined Dictionaries:"+testParams.getCiq().get("ciqPdd"));
		Logger.info("*****************************************************");


		List<NameValuePair> headers = dciFunctions.getBasicHeaders(suiteData);		
		verifyDisplayLogsPDTPDDCombinations(dciFunctions, esLogs, headers, testParams.getFileName(), testParams.getSaasType(), 
				testParams.getRisks(), testParams.getCiq());

		Logger.info(
				"************************************Completed "+testParams.getTestName()+" for filename:" + testParams.getFileName() + " and saas app type:" + testParams.getSaasType()+ "******************");

	}

	@Test(dataProvider = "dataUploadENE", groups ={"ENE"})
	public void testDisplayLogsForRiskGeneratedInPDTEnergy(TestParameters testParams) throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();

		Logger.info(
				"************************************Starting "+testParams.getTestName()+" for filename:" + testParams.getFileName() + " and saas app type:" + testParams.getSaasType()+ "******************");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description:"+testParams.getTestDescription());
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output****************");
		Logger.info("Filename:"+testParams.getFileName());Logger.info("Risk Type:"+Arrays.asList(testParams.getRisks()));
		Logger.info("CIQ Profilename:"+testParams.getCiq().get("ciqProfileName"));
		Logger.info("Predefined Terms:"+testParams.getCiq().get("ciqPdt"));
		Logger.info("Predefined Dictionaries:"+testParams.getCiq().get("ciqPdd"));
		Logger.info("*****************************************************");


		List<NameValuePair> headers = dciFunctions.getBasicHeaders(suiteData);		
		verifyDisplayLogsPDTPDDCombinations(dciFunctions, esLogs, headers, testParams.getFileName(), testParams.getSaasType(), 
				testParams.getRisks(), testParams.getCiq());

		Logger.info(
				"************************************Completed "+testParams.getTestName()+" for filename:" + testParams.getFileName() + " and saas app type:" + testParams.getSaasType()+ "******************");

	}

	@Test(dataProvider = "dataUploadGAM", groups ={"GAM"})
	public void testDisplayLogsForRiskGeneratedInPDTGambling(TestParameters testParams) throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();

		Logger.info(
				"************************************Starting "+testParams.getTestName()+" for filename:" + testParams.getFileName() + " and saas app type:" + testParams.getSaasType()+ "******************");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description:"+testParams.getTestDescription());
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output****************");
		Logger.info("Filename:"+testParams.getFileName());Logger.info("Risk Type:"+Arrays.asList(testParams.getRisks()));
		Logger.info("CIQ Profilename:"+testParams.getCiq().get("ciqProfileName"));
		Logger.info("Predefined Terms:"+testParams.getCiq().get("ciqPdt"));
		Logger.info("Predefined Dictionaries:"+testParams.getCiq().get("ciqPdd"));
		Logger.info("*****************************************************");


		List<NameValuePair> headers = dciFunctions.getBasicHeaders(suiteData);		
		verifyDisplayLogsPDTPDDCombinations(dciFunctions, esLogs, headers, testParams.getFileName(), testParams.getSaasType(), 
				testParams.getRisks(), testParams.getCiq());

		Logger.info(
				"************************************Completed "+testParams.getTestName()+" for filename:" + testParams.getFileName() + " and saas app type:" + testParams.getSaasType()+ "******************");

	}

	@Test(dataProvider = "dataUploadID", groups ={"ID"})
	public void testDisplayLogsForRiskGeneratedInPDTIllegalDrugs(TestParameters testParams) throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();

		Logger.info(
				"************************************Starting "+testParams.getTestName()+" for filename:" + testParams.getFileName() + " and saas app type:" + testParams.getSaasType()+ "******************");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description:"+testParams.getTestDescription());
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output****************");
		Logger.info("Filename:"+testParams.getFileName());Logger.info("Risk Type:"+Arrays.asList(testParams.getRisks()));
		Logger.info("CIQ Profilename:"+testParams.getCiq().get("ciqProfileName"));
		Logger.info("Predefined Terms:"+testParams.getCiq().get("ciqPdt"));
		Logger.info("Predefined Dictionaries:"+testParams.getCiq().get("ciqPdd"));
		Logger.info("*****************************************************");


		List<NameValuePair> headers = dciFunctions.getBasicHeaders(suiteData);		
		verifyDisplayLogsPDTPDDCombinations(dciFunctions, esLogs, headers, testParams.getFileName(), testParams.getSaasType(), 
				testParams.getRisks(), testParams.getCiq());

		Logger.info(
				"************************************Completed "+testParams.getTestName()+" for filename:" + testParams.getFileName() + " and saas app type:" + testParams.getSaasType()+ "******************");

	}

	@Test(dataProvider = "dataUploadOBS", groups ={"OBS"})
	public void testDisplayLogsForRiskGeneratedInPDTObscenities(TestParameters testParams) throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();

		Logger.info(
				"************************************Starting "+testParams.getTestName()+" for filename:" + testParams.getFileName() + " and saas app type:" + testParams.getSaasType()+ "******************");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description:"+testParams.getTestDescription());
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output****************");
		Logger.info("Filename:"+testParams.getFileName());Logger.info("Risk Type:"+Arrays.asList(testParams.getRisks()));
		Logger.info("CIQ Profilename:"+testParams.getCiq().get("ciqProfileName"));
		Logger.info("Predefined Terms:"+testParams.getCiq().get("ciqPdt"));
		Logger.info("Predefined Dictionaries:"+testParams.getCiq().get("ciqPdd"));
		Logger.info("*****************************************************");


		List<NameValuePair> headers = dciFunctions.getBasicHeaders(suiteData);		
		verifyDisplayLogsPDTPDDCombinations(dciFunctions, esLogs, headers, testParams.getFileName(), testParams.getSaasType(), 
				testParams.getRisks(), testParams.getCiq());

		Logger.info(
				"************************************Completed "+testParams.getTestName()+" for filename:" + testParams.getFileName() + " and saas app type:" + testParams.getSaasType()+ "******************");

	}

	@Test(dataProvider = "dataUploadPD", groups ={"PD"})
	public void testDisplayLogsForRiskGeneratedInPDTPharmaceuticalDrugs(TestParameters testParams) throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();

		Logger.info(
				"************************************Starting "+testParams.getTestName()+" for filename:" + testParams.getFileName() + " and saas app type:" + testParams.getSaasType()+ "******************");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description:"+testParams.getTestDescription());
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output****************");
		Logger.info("Filename:"+testParams.getFileName());Logger.info("Risk Type:"+Arrays.asList(testParams.getRisks()));
		Logger.info("CIQ Profilename:"+testParams.getCiq().get("ciqProfileName"));
		Logger.info("Predefined Terms:"+testParams.getCiq().get("ciqPdt"));
		Logger.info("Predefined Dictionaries:"+testParams.getCiq().get("ciqPdd"));
		Logger.info("*****************************************************");


		List<NameValuePair> headers = dciFunctions.getBasicHeaders(suiteData);		
		verifyDisplayLogsPDTPDDCombinations(dciFunctions, esLogs, headers, testParams.getFileName(), testParams.getSaasType(), 
				testParams.getRisks(), testParams.getCiq());

		Logger.info(
				"************************************Completed "+testParams.getTestName()+" for filename:" + testParams.getFileName() + " and saas app type:" + testParams.getSaasType()+ "******************");

	}

	@Test(dataProvider = "dataUploadTS", groups ={"TS"})
	public void testDisplayLogsForRiskGeneratedInPDTTickerSymbols(TestParameters testParams) throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();

		Logger.info(
				"************************************Starting "+testParams.getTestName()+" for filename:" + testParams.getFileName() + " and saas app type:" + testParams.getSaasType()+ "******************");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description:"+testParams.getTestDescription());
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output****************");
		Logger.info("Filename:"+testParams.getFileName());Logger.info("Risk Type:"+Arrays.asList(testParams.getRisks()));
		Logger.info("CIQ Profilename:"+testParams.getCiq().get("ciqProfileName"));
		Logger.info("Predefined Terms:"+testParams.getCiq().get("ciqPdt"));
		Logger.info("Predefined Dictionaries:"+testParams.getCiq().get("ciqPdd"));
		Logger.info("*****************************************************");


		List<NameValuePair> headers = dciFunctions.getBasicHeaders(suiteData);		
		verifyDisplayLogsPDTPDDCombinations(dciFunctions, esLogs, headers, testParams.getFileName(), testParams.getSaasType(), 
				testParams.getRisks(), testParams.getCiq());

		Logger.info(
				"************************************Completed "+testParams.getTestName()+" for filename:" + testParams.getFileName() + " and saas app type:" + testParams.getSaasType()+ "******************");

	}

	@Test(dataProvider = "dataUploadUSG", groups ={"USG"})
	public void testDisplayLogsForRiskGeneratedInPDTUSGExportControlledItems(TestParameters testParams) throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();

		Logger.info(
				"************************************Starting "+testParams.getTestName()+" for filename:" + testParams.getFileName() + " and saas app type:" + testParams.getSaasType()+ "******************");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description:"+testParams.getTestDescription());
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output****************");
		Logger.info("Filename:"+testParams.getFileName());Logger.info("Risk Type:"+Arrays.asList(testParams.getRisks()));
		Logger.info("CIQ Profilename:"+testParams.getCiq().get("ciqProfileName"));
		Logger.info("Predefined Terms:"+testParams.getCiq().get("ciqPdt"));
		Logger.info("Predefined Dictionaries:"+testParams.getCiq().get("ciqPdd"));
		Logger.info("*****************************************************");


		List<NameValuePair> headers = dciFunctions.getBasicHeaders(suiteData);		
		verifyDisplayLogsPDTPDDCombinations(dciFunctions, esLogs, headers, testParams.getFileName(), testParams.getSaasType(), 
				testParams.getRisks(), testParams.getCiq());

		Logger.info(
				"************************************Completed "+testParams.getTestName()+" for filename:" + testParams.getFileName() + " and saas app type:" + testParams.getSaasType()+ "******************");

	}

	@Test(dataProvider = "dataUploadVIO", groups ={"VIO"})
	public void testDisplayLogsForRiskGeneratedInPDTViolence(TestParameters testParams) throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();

		Logger.info(
				"************************************Starting "+testParams.getTestName()+" for filename:" + testParams.getFileName() + " and saas app type:" + testParams.getSaasType()+ "******************");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description:"+testParams.getTestDescription());
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output****************");
		Logger.info("Filename:"+testParams.getFileName());Logger.info("Risk Type:"+Arrays.asList(testParams.getRisks()));
		Logger.info("CIQ Profilename:"+testParams.getCiq().get("ciqProfileName"));
		Logger.info("Predefined Terms:"+testParams.getCiq().get("ciqPdt"));
		Logger.info("Predefined Dictionaries:"+testParams.getCiq().get("ciqPdd"));
		Logger.info("*****************************************************");


		List<NameValuePair> headers = dciFunctions.getBasicHeaders(suiteData);		
		verifyDisplayLogsPDTPDDCombinations(dciFunctions, esLogs, headers, testParams.getFileName(), testParams.getSaasType(), 
				testParams.getRisks(), testParams.getCiq());

		Logger.info(
				"************************************Completed "+testParams.getTestName()+" for filename:" + testParams.getFileName() + " and saas app type:" + testParams.getSaasType()+ "******************");

	}

	/**********************************************TEST METHODS***********************************************/
	/**********************************************DATA PROVIDERS*********************************************/

	@DataProvider(name = "dataUploadCC")
	public Object[][] dataUploadCC() {
		dciFunctions = new DCIFunctions();

		String[] fileName = dciFunctions.getFileName(DCIConstants.DCI_FILE_UPLOAD_CIQ_PDT_PDD_CC_PATH);
		String[] pdts = dciFunctions.predefinedTermsCIQProfile(fileName);
		String pdd = "Company Confidential";
		String[] pdtsTitle = dciFunctions.predefinedTermsCIQProfileName(fileName);
		String[] ciqTitle = dciFunctions.getCustomCIQTitle(pdtsTitle,pdd);
		String[] ciqDescription = dciFunctions.getCustomCIQDescription(pdts,pdd);

		for(int i=0;i<pdts.length;i++){
			dciFunctions.createCIQPredefinedDictionariesAndTerms(restClient, suiteData, pdd, pdts[i], ciqTitle[i], ciqDescription[i], 
					"high", 1, true, 1, false);
		}
		
		fileName = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_CIQ_PDT_PDD_CC_PATH,
				fileName);
		dciFunctions.fileUploadOrSendEmail(suiteData, folderInfo, fileName);
		
		Object[][] result = new Object[fileName.length][];
		for (int i = 0; i < fileName.length; i++) {
			Map<String,String> ciq = new HashMap<String,String>();
			ciq.put("ciqProfileName", ciqTitle[i]);
			ciq.put("ciqPdt", pdts[i]);ciq.put("ciqPdd", pdd);ciq.put("ciqCount", "1");
			ciq.put("ciqPdtValue", pdts[i]);
			ciq.put("ciqPddValue", "acknowledge that the harm of circumvention and disclosure would be substantial to");

			result[i] = new Object[] { new TestParameters("Risk Generation/Validation for PDT/PDD Terms:"+pdts[i]+" and "+pdd+" in ContentIQ Profile:"+ciqTitle[i]+" for file:"+fileName[i], 
					"Create CIQ Profile with name:"+ciqTitle[i]+" with pdt:"+pdts[i]+" ,pdd:"+pdd+" and upload file:"+fileName[i]+ ". Then verify risk logs are getting generated within the SLA provided",
					fileName[i], SaasType.getSaasFilterType(suiteData.getSaasApp()), dciFunctions.getCIQRisksTypes(fileName[i]), ciq)};
		}

		mainCounter = 0;testCounter = DCIConstants.DCI_COUNTER_MAX;

		return result;
	}

	@DataProvider(name = "dataUploadDIS")
	public Object[][] dataUploadDIS() {
		dciFunctions = new DCIFunctions();

		String[] fileName = dciFunctions.getFileName(DCIConstants.DCI_FILE_UPLOAD_CIQ_PDT_PDD_DIS_PATH);
		String[] pdts = dciFunctions.predefinedTermsCIQProfile(fileName);
		String pdd = "Diseases";
		String[] pdtsTitle = dciFunctions.predefinedTermsCIQProfileName(fileName);
		String[] ciqTitle = dciFunctions.getCustomCIQTitle(pdtsTitle,pdd);
		String[] ciqDescription = dciFunctions.getCustomCIQDescription(pdts,pdd);

		for(int i=0;i<pdts.length;i++){
			dciFunctions.createCIQPredefinedDictionariesAndTerms(restClient, suiteData, pdd, pdts[i], ciqTitle[i], ciqDescription[i], 
					"high", 1, true, 1, false);
		}

		fileName = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_CIQ_PDT_PDD_DIS_PATH,
				fileName);
		dciFunctions.fileUploadOrSendEmail(suiteData, folderInfo, fileName);

		Object[][] result = new Object[fileName.length][];
		for (int i = 0; i < fileName.length; i++) {
			Map<String,String> ciq = new HashMap<String,String>();
			ciq.put("ciqProfileName", ciqTitle[i]);
			ciq.put("ciqPdt", pdts[i]);ciq.put("ciqPdd", pdd);ciq.put("ciqCount", "1");
			ciq.put("ciqPdtValue", pdts[i]);
			ciq.put("ciqPddValue", "citrullinemia");

			result[i] = new Object[] { new TestParameters("Risk Generation/Validation for PDT/PDD Terms:"+pdts[i]+" and "+pdd+" in ContentIQ Profile:"+ciqTitle[i]+" for file:"+fileName[i], 
					"Create CIQ Profile with name:"+ciqTitle[i]+" with pdt:"+pdts[i]+" ,pdd:"+pdd+" and upload file:"+fileName[i]+ ". Then verify risk logs are getting generated within the SLA provided",
					fileName[i], SaasType.getSaasFilterType(suiteData.getSaasApp()), dciFunctions.getCIQRisksTypes(fileName[i]), ciq)};
		}

		mainCounter = 0;testCounter = DCIConstants.DCI_COUNTER_MAX;

		return result;
	}

	@DataProvider(name = "dataUploadENE")
	public Object[][] dataUploadENE() {
		dciFunctions = new DCIFunctions();

		String[] fileName = dciFunctions.getFileName(DCIConstants.DCI_FILE_UPLOAD_CIQ_PDT_PDD_ENE_PATH);
		String[] pdts = dciFunctions.predefinedTermsCIQProfile(fileName);
		String pdd = "Energy";
		String[] pdtsTitle = dciFunctions.predefinedTermsCIQProfileName(fileName);
		String[] ciqTitle = dciFunctions.getCustomCIQTitle(pdtsTitle,pdd);
		String[] ciqDescription = dciFunctions.getCustomCIQDescription(pdts,pdd);

		for(int i=0;i<pdts.length;i++){
			dciFunctions.createCIQPredefinedDictionariesAndTerms(restClient, suiteData, pdd, pdts[i], ciqTitle[i], ciqDescription[i], 
					"high", 1, true, 1, false);
		}

		fileName = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_CIQ_PDT_PDD_ENE_PATH,
				fileName);
		dciFunctions.fileUploadOrSendEmail(suiteData, folderInfo, fileName);

		Object[][] result = new Object[fileName.length][];
		for (int i = 0; i < fileName.length; i++) {
			Map<String,String> ciq = new HashMap<String,String>();
			ciq.put("ciqProfileName", ciqTitle[i]);
			ciq.put("ciqPdt", pdts[i]);ciq.put("ciqPdd", pdd);ciq.put("ciqCount", "1");
			ciq.put("ciqPdtValue", pdts[i]);
			ciq.put("ciqPddValue", "Curtailment");

			result[i] = new Object[] { new TestParameters("Risk Generation/Validation for PDT/PDD Terms:"+pdts[i]+" and "+pdd+" in ContentIQ Profile:"+ciqTitle[i]+" for file:"+fileName[i], 
					"Create CIQ Profile with name:"+ciqTitle[i]+" with pdt:"+pdts[i]+" ,pdd:"+pdd+" and upload file:"+fileName[i]+ ". Then verify risk logs are getting generated within the SLA provided",
					fileName[i], SaasType.getSaasFilterType(suiteData.getSaasApp()), dciFunctions.getCIQRisksTypes(fileName[i]), ciq)};
		}

		mainCounter = 0;testCounter = DCIConstants.DCI_COUNTER_MAX;

		return result;
	}

	@DataProvider(name = "dataUploadGAM")
	public Object[][] dataUploadGAM() {
		dciFunctions = new DCIFunctions();

		String[] fileName = dciFunctions.getFileName(DCIConstants.DCI_FILE_UPLOAD_CIQ_PDT_PDD_GAM_PATH);
		String[] pdts = dciFunctions.predefinedTermsCIQProfile(fileName);
		String pdd = "Gambling";
		String[] pdtsTitle = dciFunctions.predefinedTermsCIQProfileName(fileName);
		String[] ciqTitle = dciFunctions.getCustomCIQTitle(pdtsTitle,pdd);
		String[] ciqDescription = dciFunctions.getCustomCIQDescription(pdts,pdd);

		for(int i=0;i<pdts.length;i++){
			dciFunctions.createCIQPredefinedDictionariesAndTerms(restClient, suiteData, pdd, pdts[i], ciqTitle[i], ciqDescription[i], 
					"high", 1, true, 1, false);
		}

		fileName = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_CIQ_PDT_PDD_GAM_PATH,
				fileName);	
		dciFunctions.fileUploadOrSendEmail(suiteData, folderInfo, fileName);

		Object[][] result = new Object[fileName.length][];
		for (int i = 0; i < fileName.length; i++) {
			Map<String,String> ciq = new HashMap<String,String>();
			ciq.put("ciqProfileName", ciqTitle[i]);
			ciq.put("ciqPdt", pdts[i]);ciq.put("ciqPdd", pdd);ciq.put("ciqCount", "1");
			ciq.put("ciqPdtValue", pdts[i]);
			ciq.put("ciqPddValue", "baccarat");

			result[i] = new Object[] { new TestParameters("Risk Generation/Validation for PDT/PDD Terms:"+pdts[i]+" and "+pdd+" in ContentIQ Profile:"+ciqTitle[i]+" for file:"+fileName[i], 
					"Create CIQ Profile with name:"+ciqTitle[i]+" with pdt:"+pdts[i]+" ,pdd:"+pdd+" and upload file:"+fileName[i]+ ". Then verify risk logs are getting generated within the SLA provided",
					fileName[i], SaasType.getSaasFilterType(suiteData.getSaasApp()), dciFunctions.getCIQRisksTypes(fileName[i]), ciq)};
		}

		mainCounter = 0;testCounter = DCIConstants.DCI_COUNTER_MAX;

		return result;
	}

	@DataProvider(name = "dataUploadID")
	public Object[][] dataUploadID() {
		dciFunctions = new DCIFunctions();

		String[] fileName = dciFunctions.getFileName(DCIConstants.DCI_FILE_UPLOAD_CIQ_PDT_PDD_ID_PATH);
		String[] pdts = dciFunctions.predefinedTermsCIQProfile(fileName);
		String pdd = "Illegal Drugs";
		String[] pdtsTitle = dciFunctions.predefinedTermsCIQProfileName(fileName);
		String[] ciqTitle = dciFunctions.getCustomCIQTitle(pdtsTitle,pdd);
		String[] ciqDescription = dciFunctions.getCustomCIQDescription(pdts,pdd);

		for(int i=0;i<pdts.length;i++){
			dciFunctions.createCIQPredefinedDictionariesAndTerms(restClient, suiteData, pdd, pdts[i], ciqTitle[i], ciqDescription[i], 
					"high", 1, true, 1, false);
		}

		fileName = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_CIQ_PDT_PDD_ID_PATH,
				fileName);
		dciFunctions.fileUploadOrSendEmail(suiteData, folderInfo, fileName);

		Object[][] result = new Object[fileName.length][];
		for (int i = 0; i < fileName.length; i++) {
			Map<String,String> ciq = new HashMap<String,String>();
			ciq.put("ciqProfileName", ciqTitle[i]);
			ciq.put("ciqPdt", pdts[i]);ciq.put("ciqPdd", pdd);ciq.put("ciqCount", "1");
			ciq.put("ciqPdtValue", pdts[i]);
			ciq.put("ciqPddValue", "ganja");

			result[i] = new Object[] { new TestParameters("Risk Generation/Validation for PDT/PDD Terms:"+pdts[i]+" and "+pdd+" in ContentIQ Profile:"+ciqTitle[i]+" for file:"+fileName[i], 
					"Create CIQ Profile with name:"+ciqTitle[i]+" with pdt:"+pdts[i]+" ,pdd:"+pdd+" and upload file:"+fileName[i]+ ". Then verify risk logs are getting generated within the SLA provided",
					fileName[i], SaasType.getSaasFilterType(suiteData.getSaasApp()), dciFunctions.getCIQRisksTypes(fileName[i]), ciq)};
		}

		mainCounter = 0;testCounter = DCIConstants.DCI_COUNTER_MAX;

		return result;
	}

	@DataProvider(name = "dataUploadOBS")
	public Object[][] dataUploadOBS() {
		dciFunctions = new DCIFunctions();

		String[] fileName = dciFunctions.getFileName(DCIConstants.DCI_FILE_UPLOAD_CIQ_PDT_PDD_OBS_PATH);
		String[] pdts = dciFunctions.predefinedTermsCIQProfile(fileName);
		String pdd = "Obscenities";
		String[] pdtsTitle = dciFunctions.predefinedTermsCIQProfileName(fileName);
		String[] ciqTitle = dciFunctions.getCustomCIQTitle(pdtsTitle,pdd);
		String[] ciqDescription = dciFunctions.getCustomCIQDescription(pdts,pdd);

		for(int i=0;i<pdts.length;i++){
			dciFunctions.createCIQPredefinedDictionariesAndTerms(restClient, suiteData, pdd, pdts[i], ciqTitle[i], ciqDescription[i], 
					"high", 1, true, 1, false);
		}

		fileName = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_CIQ_PDT_PDD_OBS_PATH,
				fileName);
		dciFunctions.fileUploadOrSendEmail(suiteData, folderInfo, fileName);

		Object[][] result = new Object[fileName.length][];
		for (int i = 0; i < fileName.length; i++) {
			Map<String,String> ciq = new HashMap<String,String>();
			ciq.put("ciqProfileName", ciqTitle[i]);
			ciq.put("ciqPdt", pdts[i]);ciq.put("ciqPdd", pdd);ciq.put("ciqCount", "1");
			ciq.put("ciqPdtValue", pdts[i]);
			ciq.put("ciqPddValue", "bumfuck");

			result[i] = new Object[] { new TestParameters("Risk Generation/Validation for PDT/PDD Terms:"+pdts[i]+" and "+pdd+" in ContentIQ Profile:"+ciqTitle[i]+" for file:"+fileName[i], 
					"Create CIQ Profile with name:"+ciqTitle[i]+" with pdt:"+pdts[i]+" ,pdd:"+pdd+" and upload file:"+fileName[i]+ ". Then verify risk logs are getting generated within the SLA provided",
					fileName[i], SaasType.getSaasFilterType(suiteData.getSaasApp()), dciFunctions.getCIQRisksTypes(fileName[i]), ciq)};
		}

		mainCounter = 0;testCounter = DCIConstants.DCI_COUNTER_MAX;

		return result;
	}

	@DataProvider(name = "dataUploadPD")
	public Object[][] dataUploadPD() {
		dciFunctions = new DCIFunctions();

		String[] fileName = dciFunctions.getFileName(DCIConstants.DCI_FILE_UPLOAD_CIQ_PDT_PDD_PD_PATH);
		String[] pdts = dciFunctions.predefinedTermsCIQProfile(fileName);
		String pdd = "Pharmaceutical Drugs";
		String[] pdtsTitle = dciFunctions.predefinedTermsCIQProfileName(fileName);
		String[] ciqTitle = dciFunctions.getCustomCIQTitle(pdtsTitle,pdd);
		String[] ciqDescription = dciFunctions.getCustomCIQDescription(pdts,pdd);

		for(int i=0;i<pdts.length;i++){
			dciFunctions.createCIQPredefinedDictionariesAndTerms(restClient, suiteData, pdd, pdts[i], ciqTitle[i], ciqDescription[i], 
					"high", 1, true, 1, false);
		}

		fileName = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_CIQ_PDT_PDD_PD_PATH,
				fileName);
		dciFunctions.fileUploadOrSendEmail(suiteData, folderInfo, fileName);

		Object[][] result = new Object[fileName.length][];
		for (int i = 0; i < fileName.length; i++) {
			Map<String,String> ciq = new HashMap<String,String>();
			ciq.put("ciqProfileName", ciqTitle[i]);
			ciq.put("ciqPdt", pdts[i]);ciq.put("ciqPdd", pdd);ciq.put("ciqCount", "1");
			ciq.put("ciqPdtValue", pdts[i]);
			ciq.put("ciqPddValue", "abarelix");

			result[i] = new Object[] { new TestParameters("Risk Generation/Validation for PDT/PDD Terms:"+pdts[i]+" and "+pdd+" in ContentIQ Profile:"+ciqTitle[i]+" for file:"+fileName[i], 
					"Create CIQ Profile with name:"+ciqTitle[i]+" with pdt:"+pdts[i]+" ,pdd:"+pdd+" and upload file:"+fileName[i]+ ". Then verify risk logs are getting generated within the SLA provided",
					fileName[i], SaasType.getSaasFilterType(suiteData.getSaasApp()), dciFunctions.getCIQRisksTypes(fileName[i]), ciq)};
		}

		mainCounter = 0;testCounter = DCIConstants.DCI_COUNTER_MAX;

		return result;
	}

	@DataProvider(name = "dataUploadTS")
	public Object[][] dataUploadTS() {
		dciFunctions = new DCIFunctions();

		String[] fileName = dciFunctions.getFileName(DCIConstants.DCI_FILE_UPLOAD_CIQ_PDT_PDD_TS_PATH);
		String[] pdts = dciFunctions.predefinedTermsCIQProfile(fileName);
		String pdd = "Ticker Symbols";
		String[] pdtsTitle = dciFunctions.predefinedTermsCIQProfileName(fileName);
		String[] ciqTitle = dciFunctions.getCustomCIQTitle(pdtsTitle,pdd);
		String[] ciqDescription = dciFunctions.getCustomCIQDescription(pdts,pdd);

		for(int i=0;i<pdts.length;i++){
			dciFunctions.createCIQPredefinedDictionariesAndTerms(restClient, suiteData, pdd, pdts[i], ciqTitle[i], ciqDescription[i], 
					"high", 1, true, 1, false);
		}

		fileName = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_CIQ_PDT_PDD_TS_PATH,
				fileName);
		dciFunctions.fileUploadOrSendEmail(suiteData, folderInfo, fileName);

		Object[][] result = new Object[fileName.length][];
		for (int i = 0; i < fileName.length; i++) {
			Map<String,String> ciq = new HashMap<String,String>();
			ciq.put("ciqProfileName", ciqTitle[i]);
			ciq.put("ciqPdt", pdts[i]);ciq.put("ciqPdd", pdd);ciq.put("ciqCount", "1");
			ciq.put("ciqPdtValue", pdts[i]);
			ciq.put("ciqPddValue", "YUME");

			result[i] = new Object[] { new TestParameters("Risk Generation/Validation for PDT/PDD Terms:"+pdts[i]+" and "+pdd+" in ContentIQ Profile:"+ciqTitle[i]+" for file:"+fileName[i], 
					"Create CIQ Profile with name:"+ciqTitle[i]+" with pdt:"+pdts[i]+" ,pdd:"+pdd+" and upload file:"+fileName[i]+ ". Then verify risk logs are getting generated within the SLA provided",
					fileName[i], SaasType.getSaasFilterType(suiteData.getSaasApp()), dciFunctions.getCIQRisksTypes(fileName[i]), ciq)};
		}

		mainCounter = 0;testCounter = DCIConstants.DCI_COUNTER_MAX;

		return result;
	}

	@DataProvider(name = "dataUploadUSG")
	public Object[][] dataUploadUSG() {
		dciFunctions = new DCIFunctions();

		String[] fileName = dciFunctions.getFileName(DCIConstants.DCI_FILE_UPLOAD_CIQ_PDT_PDD_USG_PATH);
		String[] pdts = dciFunctions.predefinedTermsCIQProfile(fileName);
		String pdd = "USG Export Controlled Items";
		String[] pdtsTitle = dciFunctions.predefinedTermsCIQProfileName(fileName);
		String[] ciqTitle = dciFunctions.getCustomCIQTitle(pdtsTitle,pdd);
		String[] ciqDescription = dciFunctions.getCustomCIQDescription(pdts,pdd);

		for(int i=0;i<pdts.length;i++){
			dciFunctions.createCIQPredefinedDictionariesAndTerms(restClient, suiteData, pdd, pdts[i], ciqTitle[i], ciqDescription[i], 
					"high", 1, true, 1, false);
		}

		fileName = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_CIQ_PDT_PDD_USG_PATH,
				fileName);
		dciFunctions.fileUploadOrSendEmail(suiteData, folderInfo, fileName);

		Object[][] result = new Object[fileName.length][];
		for (int i = 0; i < fileName.length; i++) {
			Map<String,String> ciq = new HashMap<String,String>();
			ciq.put("ciqProfileName", ciqTitle[i]);
			ciq.put("ciqPdt", pdts[i]);ciq.put("ciqPdd", pdd);ciq.put("ciqCount", "1");
			ciq.put("ciqPdtValue", pdts[i]);
			ciq.put("ciqPddValue", "Abrin");

			result[i] = new Object[] { new TestParameters("Risk Generation/Validation for PDT/PDD Terms:"+pdts[i]+" and "+pdd+" in ContentIQ Profile:"+ciqTitle[i]+" for file:"+fileName[i], 
					"Create CIQ Profile with name:"+ciqTitle[i]+" with pdt:"+pdts[i]+" ,pdd:"+pdd+" and upload file:"+fileName[i]+ ". Then verify risk logs are getting generated within the SLA provided",
					fileName[i], SaasType.getSaasFilterType(suiteData.getSaasApp()), dciFunctions.getCIQRisksTypes(fileName[i]), ciq)};
		}

		mainCounter = 0;testCounter = DCIConstants.DCI_COUNTER_MAX;

		return result;
	}

	@DataProvider(name = "dataUploadVIO")
	public Object[][] dataUploadVIO() {
		dciFunctions = new DCIFunctions();

		String[] fileName = dciFunctions.getFileName(DCIConstants.DCI_FILE_UPLOAD_CIQ_PDT_PDD_VIO_PATH);
		String[] pdts = dciFunctions.predefinedTermsCIQProfile(fileName);
		String pdd = "Violence";
		String[] pdtsTitle = dciFunctions.predefinedTermsCIQProfileName(fileName);
		String[] ciqTitle = dciFunctions.getCustomCIQTitle(pdtsTitle,pdd);
		String[] ciqDescription = dciFunctions.getCustomCIQDescription(pdts,pdd);

		for(int i=0;i<pdts.length;i++){
			dciFunctions.createCIQPredefinedDictionariesAndTerms(restClient, suiteData, pdd, pdts[i], ciqTitle[i], ciqDescription[i], 
					"high", 1, true, 1, false);
		}

		fileName = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_CIQ_PDT_PDD_VIO_PATH,
				fileName);
		dciFunctions.fileUploadOrSendEmail(suiteData, folderInfo, fileName);

		Object[][] result = new Object[fileName.length][];
		for (int i = 0; i < fileName.length; i++) {
			Map<String,String> ciq = new HashMap<String,String>();
			ciq.put("ciqProfileName", ciqTitle[i]);
			ciq.put("ciqPdt", pdts[i]);ciq.put("ciqPdd", pdd);ciq.put("ciqCount", "1");
			ciq.put("ciqPdtValue", pdts[i]);
			ciq.put("ciqPddValue", "brutal");

			result[i] = new Object[] { new TestParameters("Risk Generation/Validation for PDT/PDD Terms:"+pdts[i]+" and "+pdd+" in ContentIQ Profile:"+ciqTitle[i]+" for file:"+fileName[i], 
					"Create CIQ Profile with name:"+ciqTitle[i]+" with pdt:"+pdts[i]+" ,pdd:"+pdd+" and upload file:"+fileName[i]+ ". Then verify risk logs are getting generated within the SLA provided",
					fileName[i], SaasType.getSaasFilterType(suiteData.getSaasApp()), dciFunctions.getCIQRisksTypes(fileName[i]), ciq)};
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


	private void verifyDisplayLogsPDTPDDCombinations(DCIFunctions dciFunctions, ElasticSearchLogs esLogs, List<NameValuePair> headers, String fileName, 
			String saasType, List<String> risks, Map<String,String> ciq)
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

			String validationMessage = dciFunctions.validationRiskLogsContentIQProfile(hits, fileName, suiteData.getUsername(),
					"QA Admin", saasType, "API", risks, ciq);

			Assert.assertEquals(validationMessage, "", "Output Response Validation is failing for CIQ Profile with name:"+ciq.get("ciqTitle")
			+" with predefined terms:"+ciq.get("ciqPdt")+" & predefined dictionaries:"+ciq.get("ciqPdd")+" and for fileName:"+fileName);



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
	@BeforeClass(groups ={"CC","DIS","ENE","GAM","ID",
			"OBS","PD","TS","USG","VIO"})
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
	@AfterClass(groups ={"CC","DIS","ENE","GAM","ID",
			"OBS","PD","TS","USG","VIO"})
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
	@BeforeClass(groups ={"CC","DIS","ENE","GAM","ID",
			"OBS","PD","TS","USG","VIO"})
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
	@AfterClass(groups ={"CC","DIS","ENE","GAM","ID",
			"OBS","PD","TS","USG","VIO"})
	public void deleteContentIqProfileAfterTestEnds() {
		dciFunctions = new DCIFunctions();
		dciFunctions.deleteAllPolicies(restClient, suiteData);
		dciFunctions.deleteAllCIQProfiles(restClient, suiteData);
		dciFunctions.deleteAllDictionaries(restClient, suiteData);
		dciFunctions.deleteAllTrainingProfiles(restClient, suiteData);
	}

	@BeforeMethod(groups ={"CC","DIS","ENE","GAM","ID",
			"OBS","PD","TS","USG","VIO"})
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

	/**********************************************BEFORE/AFTER METHODS/CLASS*****************************************/


}