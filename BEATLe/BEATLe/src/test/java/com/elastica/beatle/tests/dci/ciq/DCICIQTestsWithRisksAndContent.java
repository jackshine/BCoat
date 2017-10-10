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

import net.sf.json.JSONArray;
import net.sf.json.util.JSONTokener;

public class DCICIQTestsWithRisksAndContent extends DCICommonTest implements ITest {

	protected String mTestCaseName = "";
	DCIFunctions dciFunctions = null;
	ElasticSearchLogs esLogs = null;
	Map<String, String> folderInfo = new HashMap<String, String>();
	String uniqueId = UUID.randomUUID().toString();
	int mainCounter = 0;int testCounter = 0;

	/**********************************************
	 * TEST METHODS
	 ***********************************************/

	/**
	 * Create CIQ Profile with name with risks and upload file. Then verify risk
	 * logs are getting generated within the SLA provided. Once logs are
	 * generated verify the contents
	 * 
	 * @param testParams
	 * @throws Exception
	 */
	@Test(dataProvider = "dataUploadRisksOnlyCIQ", groups = { "Risks" })
	public void testDisplayLogsForRiskGeneratedInCIQProfileWithOnlyRisks(TestParameters testParams) throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();

		Logger.info("************************************Starting " + testParams.getTestName() + " for filename:"
				+ testParams.getFileName() + " and saas app type:" + testParams.getSaasType() + "******************");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description:" + testParams.getTestDescription());
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output****************");
		Logger.info("Filename:" + testParams.getFileName());
		Logger.info("Risk Types to be validated:" + testParams.getRisks());
		Logger.info("Saas App Type:" + testParams.getSaasType());
		Logger.info("*****************************************************");

		try {
			String[] risks = testParams.getRisks().toArray(new String[testParams.getRisks().size()]);
			List<NameValuePair> headers = dciFunctions.getBasicHeaders(suiteData);
			verifyDisplayLogsWithCIQ(dciFunctions, testParams.getFileName(), testParams.getSaasType(), headers, risks,
					testParams.getCiq());

		} finally {
			if (testParams.getDeleteFlag()) {
				dciFunctions.deleteAllCIQProfiles(restClient, suiteData);
				dciFunctions.deleteAllDictionaries(restClient, suiteData);
			}
		}
		Logger.info("************************************Completed " + testParams.getTestName() + " for filename:"
				+ testParams.getFileName() + " and saas app type:" + testParams.getSaasType() + "******************");

	}

	/**
	 * Create CIQ Profile with name with risks and upload file. Then verify risk
	 * logs are getting generated within the SLA provided. Once logs are
	 * generated verify the contents
	 * 
	 * @param testParams
	 * @throws Exception
	 */
	@Test(dataProvider = "dataUploadRisksSingleCIQ", groups = { "Risks" })
	public void testDisplayLogsForRiskGeneratedInCIQProfileWithSingleRisks(TestParameters testParams) throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();

		Logger.info("************************************Starting " + testParams.getTestName() + " for filename:"
				+ testParams.getFileName() + " and saas app type:" + testParams.getSaasType() + "******************");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description:" + testParams.getTestDescription());
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output****************");
		Logger.info("Filename:" + testParams.getFileName());
		Logger.info("Risk Types to be validated:" + testParams.getRisks());
		Logger.info("Saas App Type:" + testParams.getSaasType());
		Logger.info("*****************************************************");

		try {
			String[] risks = testParams.getRisks().toArray(new String[testParams.getRisks().size()]);
			List<NameValuePair> headers = dciFunctions.getBasicHeaders(suiteData);
			verifyDisplayLogsWithCIQ(dciFunctions, testParams.getFileName(), testParams.getSaasType(), headers, risks,
					testParams.getCiq());

		} finally {
			if (testParams.getDeleteFlag()) {
				dciFunctions.deleteAllCIQProfiles(restClient, suiteData);
				dciFunctions.deleteAllDictionaries(restClient, suiteData);
			}
		}
		Logger.info("************************************Completed " + testParams.getTestName() + " for filename:"
				+ testParams.getFileName() + " and saas app type:" + testParams.getSaasType() + "******************");

	}

	/**
	 * Create CIQ Profile with name with predefined dictionaries and upload
	 * file. Then verify risk logs are getting generated within the SLA
	 * provided. Once logs are generated verify the contents
	 * 
	 * @param testParams
	 * @throws Exception
	 */
	@Test(dataProvider = "dataUploadRisksPredefDict", groups = { "Risks" })
	public void testDisplayLogsForRiskGeneratedInPredefinedDictionariesRisks(TestParameters testParams) throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();

		Logger.info("************************************Starting " + testParams.getTestName() + " for filename:"
				+ testParams.getFileName() + " and saas app type:" + testParams.getSaasType() + "******************");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description:" + testParams.getTestDescription());
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output****************");
		Logger.info("Filename:" + testParams.getFileName());
		Logger.info("Risk Types to be validated:" + testParams.getRisks());
		Logger.info("Saas App Type:" + testParams.getSaasType());
		Logger.info("*****************************************************");
		try {
			String[] risks = testParams.getRisks().toArray(new String[testParams.getRisks().size()]);
			List<NameValuePair> headers = dciFunctions.getBasicHeaders(suiteData);
			verifyDisplayLogsWithCIQ(dciFunctions, testParams.getFileName(), testParams.getSaasType(), headers, risks,
					testParams.getCiq());

		} finally {
			if (testParams.getDeleteFlag()) {
				dciFunctions.deleteAllCIQProfiles(restClient, suiteData);
				dciFunctions.deleteAllDictionaries(restClient, suiteData);
			}
		}
		Logger.info("************************************Completed " + testParams.getTestName() + " for filename:"
				+ testParams.getFileName() + " and saas app type:" + testParams.getSaasType() + "******************");

	}

	/**
	 * Create CIQ Profile with name with predefined terms and upload file. Then
	 * verify risk logs are getting generated within the SLA provided. Once logs
	 * are generated verify the contents
	 * 
	 * @param testParams
	 * @throws Exception
	 */
	@Test(dataProvider = "dataUploadRisksPredefTerms", groups = { "Risks" })
	public void testDisplayLogsForRiskGeneratedInPredefinedTermsRisks(TestParameters testParams) throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();

		Logger.info("************************************Starting " + testParams.getTestName() + " for filename:"
				+ testParams.getFileName() + " and saas app type:" + testParams.getSaasType() + "******************");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description:" + testParams.getTestDescription());
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output****************");
		Logger.info("Filename:" + testParams.getFileName());
		Logger.info("Risk Types to be validated:" + testParams.getRisks());
		Logger.info("Saas App Type:" + testParams.getSaasType());
		Logger.info("*****************************************************");
		try {
			String[] risks = testParams.getRisks().toArray(new String[testParams.getRisks().size()]);
			List<NameValuePair> headers = dciFunctions.getBasicHeaders(suiteData);
			verifyDisplayLogsWithCIQ(dciFunctions, testParams.getFileName(), testParams.getSaasType(), headers, risks,
					testParams.getCiq());

		} finally {
			if (testParams.getDeleteFlag()) {
				dciFunctions.deleteAllCIQProfiles(restClient, suiteData);
				dciFunctions.deleteAllDictionaries(restClient, suiteData);
			}
		}
		Logger.info("************************************Completed " + testParams.getTestName() + " for filename:"
				+ testParams.getFileName() + " and saas app type:" + testParams.getSaasType() + "******************");

	}

	/**
	 * Create CIQ Profile with name with Custom terms and upload file. Then
	 * verify risk logs are getting generated within the SLA provided. Once logs
	 * are generated verify the contents
	 * 
	 * @param testParams
	 * @throws Exception
	 */
	@Test(dataProvider = "dataUploadRisksCustomTerms", groups = { "Risks" })
	public void testDisplayLogsForRiskGeneratedInCustomTermsRisks(TestParameters testParams) throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();

		Logger.info("************************************Starting " + testParams.getTestName() + " for filename:"
				+ testParams.getFileName() + " and saas app type:" + testParams.getSaasType() + "******************");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description:" + testParams.getTestDescription());
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output****************");
		Logger.info("Filename:" + testParams.getFileName());
		Logger.info("Risk Types to be validated:" + testParams.getRisks());
		Logger.info("Saas App Type:" + testParams.getSaasType());
		Logger.info("*****************************************************");
		try {
			String[] risks = testParams.getRisks().toArray(new String[testParams.getRisks().size()]);
			List<NameValuePair> headers = dciFunctions.getBasicHeaders(suiteData);
			verifyDisplayLogsWithCIQ(dciFunctions, testParams.getFileName(), testParams.getSaasType(), headers, risks,
					testParams.getCiq());

		} finally {
			if (testParams.getDeleteFlag()) {
				dciFunctions.deleteAllCIQProfiles(restClient, suiteData);
				dciFunctions.deleteAllDictionaries(restClient, suiteData);
			}
		}
		Logger.info("************************************Completed " + testParams.getTestName() + " for filename:"
				+ testParams.getFileName() + " and saas app type:" + testParams.getSaasType() + "******************");

	}

	/**
	 * Create CIQ Profile with name with Custom dictionaries and upload file.
	 * Then verify risk logs are getting generated within the SLA provided. Once
	 * logs are generated verify the contents
	 * 
	 * @param testParams
	 * @throws Exception
	 */
	@Test(dataProvider = "dataUploadRisksCustomDict", groups = { "Risks" })
	public void testDisplayLogsForRiskGeneratedInCustomDictionariesRisks(TestParameters testParams) throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();

		Logger.info("************************************Starting " + testParams.getTestName() + " for filename:"
				+ testParams.getFileName() + " and saas app type:" + testParams.getSaasType() + "******************");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description:" + testParams.getTestDescription());
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output****************");
		Logger.info("Filename:" + testParams.getFileName());
		Logger.info("Risk Types to be validated:" + testParams.getRisks());
		Logger.info("Saas App Type:" + testParams.getSaasType());
		Logger.info("*****************************************************");
		try {
			String[] risks = testParams.getRisks().toArray(new String[testParams.getRisks().size()]);
			List<NameValuePair> headers = dciFunctions.getBasicHeaders(suiteData);
			verifyDisplayLogsWithCIQ(dciFunctions, testParams.getFileName(), testParams.getSaasType(), headers, risks,
					testParams.getCiq());

		} finally {
			if (testParams.getDeleteFlag()) {
				dciFunctions.deleteAllCIQProfiles(restClient, suiteData);
				dciFunctions.deleteAllDictionaries(restClient, suiteData);
			}
		}
		Logger.info("************************************Completed " + testParams.getTestName() + " for filename:"
				+ testParams.getFileName() + " and saas app type:" + testParams.getSaasType() + "******************");

	}

	/**
	 * Create CIQ Profile with name with predefined dictionaries and upload
	 * file. Then verify risk logs are getting generated within the SLA
	 * provided. Once logs are generated verify the contents
	 * 
	 * @param testParams
	 * @throws Exception
	 */
	@Test(dataProvider = "dataUploadRisksNegPredefDict", groups = { "Risks" })
	public void testDisplayLogsForNoRiskGeneratedInPredefinedDictionariesRisks(TestParameters testParams) throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();

		Logger.info("************************************Starting " + testParams.getTestName() + " for filename:"
				+ testParams.getFileName() + " and saas app type:" + testParams.getSaasType() + "******************");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description:" + testParams.getTestDescription());
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output****************");
		Logger.info("Filename:" + testParams.getFileName());
		Logger.info("Risk Types to be validated:" + testParams.getRisks());
		Logger.info("Saas App Type:" + testParams.getSaasType());
		Logger.info("*****************************************************");
		try {
			String[] risks = testParams.getRisks().toArray(new String[testParams.getRisks().size()]);
			List<NameValuePair> headers = dciFunctions.getBasicHeaders(suiteData);
			verifyDisplayLogsWithCIQ(dciFunctions, testParams.getFileName(), testParams.getSaasType(), headers, risks,
					testParams.getCiq());

		} finally {
			if (testParams.getDeleteFlag()) {
				dciFunctions.deleteAllCIQProfiles(restClient, suiteData);
				dciFunctions.deleteAllDictionaries(restClient, suiteData);
			}
		}
		Logger.info("************************************Completed " + testParams.getTestName() + " for filename:"
				+ testParams.getFileName() + " and saas app type:" + testParams.getSaasType() + "******************");

	}

	/**
	 * Create CIQ Profile with name with predefined terms and upload file. Then
	 * verify risk logs are getting generated within the SLA provided. Once logs
	 * are generated verify the contents
	 * 
	 * @param testParams
	 * @throws Exception
	 */
	@Test(dataProvider = "dataUploadRisksNegPredefTerms", groups = { "Risks" })
	public void testDisplayLogsForNoRiskGeneratedInPredefinedTermsRisks(TestParameters testParams) throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();

		Logger.info("************************************Starting " + testParams.getTestName() + " for filename:"
				+ testParams.getFileName() + " and saas app type:" + testParams.getSaasType() + "******************");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description:" + testParams.getTestDescription());
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output****************");
		Logger.info("Filename:" + testParams.getFileName());
		Logger.info("Risk Types to be validated:" + testParams.getRisks());
		Logger.info("Saas App Type:" + testParams.getSaasType());
		Logger.info("*****************************************************");
		try {
			String[] risks = testParams.getRisks().toArray(new String[testParams.getRisks().size()]);
			List<NameValuePair> headers = dciFunctions.getBasicHeaders(suiteData);
			verifyDisplayLogsWithCIQ(dciFunctions, testParams.getFileName(), testParams.getSaasType(), headers, risks,
					testParams.getCiq());

		} finally {
			if (testParams.getDeleteFlag()) {
				dciFunctions.deleteAllCIQProfiles(restClient, suiteData);
				dciFunctions.deleteAllDictionaries(restClient, suiteData);
			}
		}
		Logger.info("************************************Completed " + testParams.getTestName() + " for filename:"
				+ testParams.getFileName() + " and saas app type:" + testParams.getSaasType() + "******************");

	}

	/**
	 * Create CIQ Profile with name with Custom terms and upload file. Then
	 * verify risk logs are getting generated within the SLA provided. Once logs
	 * are generated verify the contents
	 * 
	 * @param testParams
	 * @throws Exception
	 */
	@Test(dataProvider = "dataUploadRisksNegCustomTerms", groups = { "Risks" })
	public void testDisplayLogsForNoRiskGeneratedInCustomTermsRisks(TestParameters testParams) throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();

		Logger.info("************************************Starting " + testParams.getTestName() + " for filename:"
				+ testParams.getFileName() + " and saas app type:" + testParams.getSaasType() + "******************");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description:" + testParams.getTestDescription());
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output****************");
		Logger.info("Filename:" + testParams.getFileName());
		Logger.info("Risk Types to be validated:" + testParams.getRisks());
		Logger.info("Saas App Type:" + testParams.getSaasType());
		Logger.info("*****************************************************");
		try {
			String[] risks = testParams.getRisks().toArray(new String[testParams.getRisks().size()]);
			List<NameValuePair> headers = dciFunctions.getBasicHeaders(suiteData);
			verifyDisplayLogsWithCIQ(dciFunctions, testParams.getFileName(), testParams.getSaasType(), headers, risks,
					testParams.getCiq());

		} finally {
			if (testParams.getDeleteFlag()) {
				dciFunctions.deleteAllCIQProfiles(restClient, suiteData);
				dciFunctions.deleteAllDictionaries(restClient, suiteData);
			}
		}
		Logger.info("************************************Completed " + testParams.getTestName() + " for filename:"
				+ testParams.getFileName() + " and saas app type:" + testParams.getSaasType() + "******************");

	}

	/**
	 * Create CIQ Profile with name with Custom dictionaries and upload file.
	 * Then verify risk logs are getting generated within the SLA provided. Once
	 * logs are generated verify the contents
	 * 
	 * @param testParams
	 * @throws Exception
	 */
	@Test(dataProvider = "dataUploadRisksNegCustomDict", groups = { "Risks" })
	public void testDisplayLogsForNoRiskGeneratedInCustomDictionariesRisks(TestParameters testParams) throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();

		Logger.info("************************************Starting " + testParams.getTestName() + " for filename:"
				+ testParams.getFileName() + " and saas app type:" + testParams.getSaasType() + "******************");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description:" + testParams.getTestDescription());
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output****************");
		Logger.info("Filename:" + testParams.getFileName());
		Logger.info("Risk Types to be validated:" + testParams.getRisks());
		Logger.info("Saas App Type:" + testParams.getSaasType());
		Logger.info("*****************************************************");
		try {
			String[] risks = testParams.getRisks().toArray(new String[testParams.getRisks().size()]);
			List<NameValuePair> headers = dciFunctions.getBasicHeaders(suiteData);
			verifyDisplayLogsWithCIQ(dciFunctions, testParams.getFileName(), testParams.getSaasType(), headers, risks,
					testParams.getCiq());

		} finally {
			if (testParams.getDeleteFlag()) {
				dciFunctions.deleteAllCIQProfiles(restClient, suiteData);
				dciFunctions.deleteAllDictionaries(restClient, suiteData);
			}
		}
		Logger.info("************************************Completed " + testParams.getTestName() + " for filename:"
				+ testParams.getFileName() + " and saas app type:" + testParams.getSaasType() + "******************");

	}

	@Test(dataProvider = "dataUploadCIQRisksNegative", groups = { "Risks" })
	public void testDisplayLogsForNoRiskGeneratedForCIQFiles(TestParameters testParams) throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();

		Logger.info("************************************Starting " + testParams.getTestName() + " for saas app type:"
				+ testParams.getSaasType() + "******************");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description:" + testParams.getTestDescription());
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output****************");
		Logger.info("Filename:" + testParams.getFileName());
		Logger.info("Risk Types to be validated:No risks to be validated");
		Logger.info("Saas App Type:" + testParams.getSaasType());
		Logger.info("*****************************************************");
		try {
			List<NameValuePair> headers = dciFunctions.getBasicHeaders(suiteData);
			verifyDisplayLogs(dciFunctions, testParams.getFileName(), testParams.getSaasType(), headers);

		} finally {
			if (testParams.getDeleteFlag()) {
				dciFunctions.deleteAllCIQProfiles(restClient, suiteData);
				dciFunctions.deleteAllDictionaries(restClient, suiteData);
			}
		}
		Logger.info("************************************Completed " + testParams.getTestName() + " for saas app type:"
				+ testParams.getSaasType() + "******************");

	}


	@Test(dataProvider = "dataUploadRisks", groups ={"RisksOnly"})
	public void testDisplayLogsForRiskGeneratedInCIQProfileWithRisksOnly(TestParameters testParams) throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();

		Logger.info(
				"************************************Starting "+testParams.getTestName()+" for filename:" + testParams.getFileName() + " and saas app type:" + testParams.getSaasType()+ "******************");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description:"+testParams.getTestDescription());
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output****************");
		Logger.info("Filename:"+testParams.getFileName());
		Logger.info("Risk Types to be validated:"+testParams.getRisks());
		Logger.info("Saas App Type:"+testParams.getSaasType());
		Logger.info("*****************************************************");

		try{
			String[] risks = testParams.getRisks().toArray(new String[testParams.getRisks().size()]);
			List<NameValuePair> headers = dciFunctions.getBasicHeaders(suiteData);		
			verifyDisplayLogsWithCIQ(dciFunctions, testParams.getFileName(), testParams.getSaasType(), 
					headers, risks, testParams.getCiq());

		}finally{
			if(testParams.getDeleteFlag()){
				dciFunctions.deleteAllCIQProfiles(restClient, suiteData);
				dciFunctions.deleteAllDictionaries(restClient, suiteData);
			}
		}	
		Logger.info(
				"************************************Completed "+testParams.getTestName()+" for filename:" + testParams.getFileName() + " and saas app type:" + testParams.getSaasType()+ "******************");

	}

	@Test(dataProvider = "dataUploadContent", groups ={"ContentOnly"})
	public void testDisplayLogsForRiskGeneratedInCIQProfileWithContentOnly(TestParameters testParams) throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();

		Logger.info(
				"************************************Starting "+testParams.getTestName()+" for filename:" + testParams.getFileName() + " and saas app type:" + testParams.getSaasType()+ "******************");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description:"+testParams.getTestDescription());
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output****************");
		Logger.info("Filename:"+testParams.getFileName());
		Logger.info("Risk Types to be validated:"+testParams.getRisks());
		Logger.info("Saas App Type:"+testParams.getSaasType());
		Logger.info("*****************************************************");

		try{
			String[] risks = testParams.getRisks().toArray(new String[testParams.getRisks().size()]);
			List<NameValuePair> headers = dciFunctions.getBasicHeaders(suiteData);		
			verifyDisplayLogsWithCIQ(dciFunctions, testParams.getFileName(), testParams.getSaasType(), 
					headers, risks, testParams.getCiq());

		}finally{
			if(testParams.getDeleteFlag()){
				dciFunctions.deleteAllCIQProfiles(restClient, suiteData);
				dciFunctions.deleteAllDictionaries(restClient, suiteData);
			}
		}	
		Logger.info(
				"************************************Completed "+testParams.getTestName()+" for filename:" + testParams.getFileName() + " and saas app type:" + testParams.getSaasType()+ "******************");

	}

	/**********************************************
	 * TEST METHODS
	 ***********************************************/
	/**********************************************
	 * DATA PROVIDERS
	 *********************************************/

	@DataProvider(name = "dataUploadRisks")
	public Object[][] dataUploadRisks() {

		dciFunctions = new DCIFunctions();

		String ciqProfileName = "DCI_Only_Risks_Name";String ciqProfileDescription = "DCI Only Risks Description";
		String ciqType = "OnlyRisks";

		Map<String,String> ciq = new HashMap<String,String>();
		ciq.put("ciqProfileName", ciqProfileName);ciq.put("ciqProfileDescription", ciqProfileDescription);
		ciq.put("ciqType", ciqType);

		try {
			Logger.info("Creating CIQ profile with only risks and content in progress");
			List<String> valuesPreDefDict=null;List<String> valuesPreDefTerms=null;
			List<String> valuesCustomDict=null;List<String> valuesCustomTerms=null;
			List<String> valuesRisks= Arrays.asList("dlp","hipaa","vba_macros","virus","pci",
					"pii","ferpa","glba");
			List<String> valuesContentTypes=null;List<String> valuesFileFormat=null;List<String> valuesMLProfile=null;
			dciFunctions.createCIQProfile(suiteData, restClient, ciqProfileName, ciqProfileDescription, 
					valuesPreDefDict, valuesPreDefTerms, valuesCustomDict, valuesCustomTerms, 
					valuesRisks, valuesContentTypes, valuesFileFormat,valuesMLProfile, "high", 0, true, 1, false);
			Logger.info("Creating CIQ profile with only risks and content is completed");
			dciFunctions.waitForSeconds(10);

		} catch (Exception ex) {
			Logger.info("Issue with Create Content Iq Profiles" + ex.getLocalizedMessage());
		}



		String[] fileName = dciFunctions.getFileName(DCIConstants.DCI_FILE_UPLOAD_CIQ_RISKS_P2_PATH);	
		String[] risks = dciFunctions.riskTypesForAFile(fileName);


		fileName = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_CIQ_RISKS_P2_PATH,
				fileName);

		dciFunctions.fileUploadOrSendEmail(suiteData, folderInfo, fileName);

		Object[][] result = new Object[fileName.length][];
		for (int i = 0; i < fileName.length; i++) {
			boolean flag=false;if(fileName[i].contains("virus_ciq.zip")){flag=true;}
			result[i] = new Object[] { new TestParameters("Risk Generation/Validation for risks file types:"+fileName[i]+" for risks "+risks[i], 
					"Create CIQ profile with only risks:"+ciqProfileName+".Upload  risks file:"+fileName[i]+ ". Then verify risk logs are getting generated within the SLA provided"+" for risks "+Arrays.asList(risks[i].split(",")),
					fileName[i], SaasType.getSaasFilterType(suiteData.getSaasApp()), Arrays.asList(risks[i].split(",")), ciq, flag)};
		}

		mainCounter = 0;testCounter = DCIConstants.DCI_COUNTER_MAX;

		return result;
	}

	@DataProvider(name = "dataUploadContent")
	public Object[][] dataUploadContent() {

		dciFunctions = new DCIFunctions();

		String ciqProfileName = "DCI_Only_Content_Name";String ciqProfileDescription = "DCI Only Content Description";
		String ciqType = "OnlyContent";

		Map<String,String> ciq = new HashMap<String,String>();
		ciq.put("ciqProfileName", ciqProfileName);ciq.put("ciqProfileDescription", ciqProfileDescription);
		ciq.put("ciqType", ciqType);

		try {
			Logger.info("Creating CIQ profile with only risks and content in progress");
			List<String> valuesPreDefDict=null;List<String> valuesPreDefTerms=null;
			List<String> valuesCustomDict=null;List<String> valuesCustomTerms=null;
			List<String> valuesRisks= null;List<String> valuesFileFormat=null;List<String> valuesMLProfile=null;
			List<String> valuesContentTypes=Arrays.asList(
					"business","computing","cryptographic_keys","design doc",
					"encryption","engineering","health","legal","source_code"
					);
			dciFunctions.createCIQProfile(suiteData, restClient, ciqProfileName, ciqProfileDescription, 
					valuesPreDefDict, valuesPreDefTerms, valuesCustomDict, valuesCustomTerms, 
					valuesRisks, valuesContentTypes, valuesFileFormat,valuesMLProfile, "high", 0, true, 1, false);
			Logger.info("Creating CIQ profile with only risks and content is completed");
			dciFunctions.waitForSeconds(10);

		} catch (Exception ex) {
			Logger.info("Issue with Create Content Iq Profiles" + ex.getLocalizedMessage());
		}



		String[] fileName = dciFunctions.getFileName(DCIConstants.DCI_FILE_UPLOAD_CIQ_CONTENT_P2_PATH);	
		String[] risks = dciFunctions.riskTypesForAFile(fileName);

		fileName = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_CIQ_CONTENT_P2_PATH,
				fileName);
		dciFunctions.fileUploadOrSendEmail(suiteData, folderInfo, fileName);

		Object[][] result = new Object[fileName.length][];
		for (int i = 0; i < fileName.length; i++) {
			boolean flag=false;if(fileName[i].contains("video_ciq.mpg")){flag=true;}
			result[i] = new Object[] { new TestParameters("Risk Generation/Validation for risks file types:"+fileName[i]+" for risks "+risks[i], 
					"Create CIQ profile with only risks:"+ciqProfileName+".Upload  risks file:"+fileName[i]+ ". Then verify risk logs are getting generated within the SLA provided"+" for risks "+Arrays.asList(risks[i].split(",")),
					fileName[i], SaasType.getSaasFilterType(suiteData.getSaasApp()), Arrays.asList(risks[i].split(",")), ciq, flag)};
		}

		mainCounter = 0;testCounter = DCIConstants.DCI_COUNTER_MAX;

		return result;
	}



	@DataProvider(name = "dataUploadRisksOnlyCIQ")
	public Object[][] dataUploadRisksOnlyCIQ() {

		dciFunctions = new DCIFunctions();

		String ciqProfileName = "DCI_Only_Risks_Name";
		String ciqProfileDescription = "DCI Only Risks Description";
		String ciqType = "OnlyRisks";

		Map<String, String> ciq = new HashMap<String, String>();
		ciq.put("ciqProfileName", ciqProfileName);
		ciq.put("ciqProfileDescription", ciqProfileDescription);
		ciq.put("ciqType", ciqType);

		try {
			Logger.info("Creating CIQ profile with only risks in progress");
			List<String> valuesPreDefDict = null;
			List<String> valuesPreDefTerms = null;
			List<String> valuesCustomDict = null;
			List<String> valuesCustomTerms = null;List<String> valuesFileFormat=null;List<String> valuesMLProfile=null;
			List<String> valuesRisks = Arrays.asList("dlp", "hipaa", "vba_macros", "virus", "pci", "pii", "ferpa",
					"glba");
			List<String> valuesContentTypes = null;

			dciFunctions.createCIQProfile(suiteData, restClient, ciqProfileName, ciqProfileDescription, valuesPreDefDict, valuesPreDefTerms,
					valuesCustomDict, valuesCustomTerms, valuesRisks, valuesContentTypes, valuesFileFormat,valuesMLProfile, "high", 0, true, 1, false);
			Logger.info("Creating CIQ profile with only risks is completed");
			dciFunctions.waitForSeconds(10);

		} catch (Exception ex) {
			Logger.info("Issue with Create Content Iq Profiles" + ex.getLocalizedMessage());
		}

		String[] fileName = dciFunctions.getFileName(DCIConstants.DCI_FILE_UPLOAD_CIQ_RISKS_ONLY_PATH);
		String[] risks = dciFunctions.riskTypesForAFile(fileName);

		fileName = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_CIQ_RISKS_ONLY_PATH,
				fileName);
		dciFunctions.fileUploadOrSendEmail(suiteData, folderInfo, fileName);

		Object[][] result = new Object[fileName.length][];
		for (int i = 0; i < fileName.length; i++) {
			boolean flag = false;
			if (fileName[i].contains("virus")) {
				flag = true;
			}
			result[i] = new Object[] { new TestParameters(
					"Risk Generation/Validation for risks file types:" + fileName[i] + " for risks " + risks[i],
					"Create CIQ profile with only risks:" + ciqProfileName + ".Upload  risks file:" + fileName[i]
							+ ". Then verify risk logs are getting generated within the SLA provided" + " for risks "
							+ Arrays.asList(risks[i].split(",")),
							fileName[i], SaasType.getSaasFilterType(suiteData.getSaasApp()), Arrays.asList(risks[i].split(",")),
							ciq, flag) };
		}

		mainCounter = 0;
		testCounter = DCIConstants.DCI_COUNTER_MAX;

		return result;
	}

	@DataProvider(name = "dataUploadRisksSingleCIQ")
	public Object[][] dataUploadRisksSingleCIQ() {

		dciFunctions = new DCIFunctions();

		String[] fileName = dciFunctions.getFileName(DCIConstants.DCI_FILE_UPLOAD_CIQ_RISKS_ONLY_PATH);
		String[] risks = dciFunctions.riskTypesForAFile(fileName);

		String[] riskValues = { "ferpa", "glba", "hipaa", "pci", "pii", "vba_macros", "virus" };

		String[] ciqProfileName = new String[riskValues.length];
		String[] ciqProfileDescription = new String[riskValues.length];
		String[] ciqType = new String[riskValues.length];

		for (int i = 0; i < riskValues.length; i++) {
			try {
				ciqProfileName[i] = riskValues[i] + "_Only_Risks";
				ciqProfileDescription[i] = riskValues[i] + " Only Risk Description";
				ciqType[i] = "OnlyRisks";

				Logger.info("Creating CIQ profile with only risks in progress");
				List<String> valuesPreDefDict = null;
				List<String> valuesPreDefTerms = null;
				List<String> valuesCustomDict = null;
				List<String> valuesCustomTerms = null;List<String> valuesFileFormat=null;List<String> valuesMLProfile=null;
				List<String> valuesRisks = Arrays.asList(riskValues[i]);
				List<String> valuesContentTypes = null;

				dciFunctions.createCIQProfile(suiteData, restClient, ciqProfileName[i], ciqProfileDescription[i], valuesPreDefDict, valuesPreDefTerms,
						valuesCustomDict, valuesCustomTerms, valuesRisks, valuesContentTypes, valuesFileFormat,valuesMLProfile, "high", 0, true, 1, false);

				Logger.info("Creating CIQ profile with only risks is completed");

			} catch (Exception ex) {
				Logger.info("Issue with Create Content Iq Profiles" + ex.getLocalizedMessage());
			}
		}

		dciFunctions.waitForSeconds(10);

		fileName = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_CIQ_RISKS_ONLY_PATH,
				fileName);
		dciFunctions.fileUploadOrSendEmail(suiteData, folderInfo, fileName);

		Object[][] result = new Object[fileName.length][];
		for (int i = 0; i < fileName.length; i++) {
			Map<String, String> ciq = new HashMap<String, String>();
			ciq.put("ciqProfileName", ciqProfileName[i]);
			ciq.put("ciqProfileDescription", ciqProfileDescription[i]);
			ciq.put("ciqType", ciqType[i]);

			if (fileName[i].contains("hipaa")) {
				ciq.put("ciqProfileName", ciqProfileName[i] + ",pii_Only_Risks");
			}


			boolean flag = false;
			if (fileName[i].contains("virus")) {
				flag = true;
			}
			result[i] = new Object[] { new TestParameters(
					"Risk Generation/Validation for risks file types:" + fileName[i] + " for risks " + risks[i],
					"Create CIQ profile with only risks:" + ciqProfileName[i] + ".Upload  risks file:" + fileName[i]
							+ ". Then verify risk logs are getting generated within the SLA provided" + " for risks "
							+ Arrays.asList(risks[i].split(",")),
							fileName[i], SaasType.getSaasFilterType(suiteData.getSaasApp()), Arrays.asList(risks[i].split(",")),
							ciq, flag) };
		}

		mainCounter = 0;
		testCounter = DCIConstants.DCI_COUNTER_MAX;

		return result;
	}

	@DataProvider(name = "dataUploadRisksPredefDict")
	public Object[][] dataUploadRisksPredefDict() {
		dciFunctions = new DCIFunctions();

		String ciqProfileName = "DCI_Predef_Dictionary_Risks_Name";
		String ciqProfileDescription = "DCI Predefined Dictionaries With Risks Description";
		String ciqProfileType = "Illegal Drugs";
		String ciqProfileCount = "1";
		String ciqType = "PDD";
		String ciqProfileKeyword = "anadrol";

		Map<String, String> ciq = new HashMap<String, String>();
		ciq.put("ciqProfileName", ciqProfileName);
		ciq.put("ciqProfileDescription", ciqProfileDescription);
		ciq.put("ciqProfileType", ciqProfileType);
		ciq.put("ciqProfileCount", ciqProfileCount);
		ciq.put("ciqType", ciqType);
		ciq.put("ciqProfileKeyword", ciqProfileKeyword);

		try {
			Logger.info("Creating CIQ profile with predefined dictionaries and risks in progress");
			List<String> valuesPreDefDict = Arrays.asList(ciqProfileType);
			List<String> valuesPreDefTerms = null;
			List<String> valuesCustomDict = null;
			List<String> valuesCustomTerms = null;List<String> valuesFileFormat=null;List<String> valuesMLProfile=null;
			List<String> valuesRisks = Arrays.asList("dlp", "hipaa", "vba_macros", "virus", "pci", "pii", "ferpa",
					"glba");
			List<String> valuesContentTypes = null;

			dciFunctions.createCIQProfile(suiteData, restClient, ciqProfileName, ciqProfileDescription, valuesPreDefDict, valuesPreDefTerms,
					valuesCustomDict, valuesCustomTerms, valuesRisks, valuesContentTypes, valuesFileFormat,valuesMLProfile, "high", 1, true, 1, false);

			Logger.info("Creating CIQ profile with predefined dictionaries and risks is completed");
			dciFunctions.waitForSeconds(10);

		} catch (Exception ex) {
			Logger.info("Issue with Create Content Iq Profiles" + ex.getLocalizedMessage());
		}

		String[] fileName = dciFunctions.getFileName(DCIConstants.DCI_FILE_UPLOAD_CIQ_PREDEF_DICT_RISKS_PATH);
		String[] risks = dciFunctions.riskTypesForAFile(fileName);

		fileName = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_CIQ_PREDEF_DICT_RISKS_PATH,
				fileName);
		dciFunctions.fileUploadOrSendEmail(suiteData, folderInfo, fileName);

		Object[][] result = new Object[fileName.length][];
		for (int i = 0; i < fileName.length; i++) {
			boolean flag = false;
			if (fileName[i].contains("virus")) {
				flag = true;
			}
			result[i] = new Object[] { new TestParameters(
					"Risk Generation/Validation for risks file types:" + fileName[i] + " for risks " + risks[i],
					"Create CIQ profile with risks and PDDs:" + ciqProfileName + ".Upload  risks file:" + fileName[i]
							+ ". Then verify risk logs are getting generated within the SLA provided" + " for risks "
							+ Arrays.asList(risks[i].split(",")),
							fileName[i], SaasType.getSaasFilterType(suiteData.getSaasApp()), Arrays.asList(risks[i].split(",")),
							ciq, flag) };
		}

		mainCounter = 0;
		testCounter = DCIConstants.DCI_COUNTER_MAX;

		return result;
	}

	@DataProvider(name = "dataUploadRisksPredefTerms")
	public Object[][] dataUploadRisksPredefTerms() {
		dciFunctions = new DCIFunctions();

		String ciqProfileName = "DCI_Predef_Terms_Risks_Name";
		String ciqProfileDescription = "DCI Predefined Terms With Risks Description";
		String ciqProfileType = "France National ID Card (CNI)";
		String ciqProfileCount = "1";
		String ciqType = "PDT";

		Map<String, String> ciq = new HashMap<String, String>();
		ciq.put("ciqProfileName", ciqProfileName);
		ciq.put("ciqProfileDescription", ciqProfileDescription);
		ciq.put("ciqProfileType", ciqProfileType);
		ciq.put("ciqProfileCount", ciqProfileCount);
		ciq.put("ciqType", ciqType);

		try {
			Logger.info("Creating CIQ profile with predefined terms and risks in progress");
			List<String> valuesPreDefDict = null;
			List<String> valuesPreDefTerms = Arrays.asList(ciqProfileType);
			List<String> valuesCustomDict = null;
			List<String> valuesCustomTerms = null;List<String> valuesFileFormat=null;List<String> valuesMLProfile=null;
			List<String> valuesRisks = Arrays.asList("dlp", "hipaa", "vba_macros", "virus", "pci", "pii", "ferpa",
					"glba");
			List<String> valuesContentTypes = null;

			dciFunctions.createCIQProfile(suiteData, restClient, ciqProfileName, ciqProfileDescription, valuesPreDefDict, valuesPreDefTerms,
					valuesCustomDict, valuesCustomTerms, valuesRisks, valuesContentTypes, valuesFileFormat,valuesMLProfile, "high", 1, true, 1, false);

			Logger.info("Creating CIQ profile with predefined terms and risks is completed");
			dciFunctions.waitForSeconds(10);

		} catch (Exception ex) {
			Logger.info("Issue with Create Content Iq Profiles" + ex.getLocalizedMessage());
		}

		String[] fileName = dciFunctions.getFileName(DCIConstants.DCI_FILE_UPLOAD_CIQ_PREDEF_TERMS_RISKS_PATH);
		String[] risks = dciFunctions.riskTypesForAFile(fileName);

		fileName = dciFunctions
				.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_CIQ_PREDEF_TERMS_RISKS_PATH, fileName);
		dciFunctions.fileUploadOrSendEmail(suiteData, folderInfo, fileName);

		Object[][] result = new Object[fileName.length][];
		for (int i = 0; i < fileName.length; i++) {
			boolean flag = false;
			if (fileName[i].contains("virus")) {
				flag = true;
			}
			result[i] = new Object[] { new TestParameters(
					"Risk Generation/Validation for risks file types:" + fileName[i] + " for risks " + risks[i],
					"Create CIQ profile with risks and PDTs:" + ciqProfileName + ".Upload  risks file:" + fileName[i]
							+ ". Then verify risk logs are getting generated within the SLA provided" + " for risks "
							+ Arrays.asList(risks[i].split(",")),
							fileName[i], SaasType.getSaasFilterType(suiteData.getSaasApp()), Arrays.asList(risks[i].split(",")),
							ciq, flag) };
		}

		mainCounter = 0;
		testCounter = DCIConstants.DCI_COUNTER_MAX;

		return result;
	}

	@DataProvider(name = "dataUploadRisksCustomTerms")
	public Object[][] dataUploadRisksCustomTerms() {
		dciFunctions = new DCIFunctions();

		String ciqProfileName = "DCI_Custom_Terms_Risks_Name";
		String ciqProfileDescription = "DCI Custom Terms With Risks Description";
		String ciqProfileType = "DCI_CUSTOM_TERMS";
		String ciqProfileCount = "1";
		String ciqType = "CT";

		Map<String, String> ciq = new HashMap<String, String>();
		ciq.put("ciqProfileName", ciqProfileName);
		ciq.put("ciqProfileDescription", ciqProfileDescription);
		ciq.put("ciqProfileType", ciqProfileType);
		ciq.put("ciqProfileCount", ciqProfileCount);
		ciq.put("ciqType", ciqType);

		try {
			Logger.info("Creating CIQ profile with custom terms and risks in progress");
			List<String> valuesPreDefDict = null;
			List<String> valuesPreDefTerms = null;
			List<String> valuesCustomDict = null;List<String> valuesFileFormat=null;List<String> valuesMLProfile=null;
			List<String> valuesCustomTerms = Arrays.asList(ciqProfileType);
			List<String> valuesRisks = Arrays.asList("dlp", "hipaa", "vba_macros", "virus", "pci", "pii", "ferpa",
					"glba");
			List<String> valuesContentTypes = null;

			dciFunctions.createCIQProfile(suiteData, restClient, ciqProfileName, ciqProfileDescription, valuesPreDefDict, valuesPreDefTerms,
					valuesCustomDict, valuesCustomTerms, valuesRisks, valuesContentTypes, valuesFileFormat,valuesMLProfile, "high", 1, true, 1, false);

			Logger.info("Creating CIQ profile with custom terms and risks is completed");
			dciFunctions.waitForSeconds(10);

		} catch (Exception ex) {
			Logger.info("Issue with Create Content Iq Profiles" + ex.getLocalizedMessage());
		}

		String[] fileName = dciFunctions.getFileName(DCIConstants.DCI_FILE_UPLOAD_CIQ_CUSTOM_TERMS_RISKS_PATH);
		String[] risks = dciFunctions.riskTypesForAFile(fileName);

		fileName = dciFunctions
				.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_CIQ_CUSTOM_TERMS_RISKS_PATH, fileName);
		dciFunctions.fileUploadOrSendEmail(suiteData, folderInfo, fileName);

		Object[][] result = new Object[fileName.length][];
		for (int i = 0; i < fileName.length; i++) {
			boolean flag = false;
			if (fileName[i].contains("virus")) {
				flag = true;
			}
			result[i] = new Object[] { new TestParameters(
					"Risk Generation/Validation for risks file types:" + fileName[i] + " for risks " + risks[i],
					"Create CIQ profile with risks and CTs:" + ciqProfileName + ".Upload  risks file:" + fileName[i]
							+ ". Then verify risk logs are getting generated within the SLA provided" + " for risks "
							+ Arrays.asList(risks[i].split(",")),
							fileName[i], SaasType.getSaasFilterType(suiteData.getSaasApp()), Arrays.asList(risks[i].split(",")),
							ciq, flag) };
		}

		mainCounter = 0;
		testCounter = DCIConstants.DCI_COUNTER_MAX;

		return result;
	}

	@DataProvider(name = "dataUploadRisksCustomDict")
	public Object[][] dataUploadRisksCustomDict() {
		dciFunctions = new DCIFunctions();

		List<NameValuePair> headers = dciFunctions.getBrowserHeaders(suiteData);

		String ciqProfileName = "DCI_Custom_Dictionary_Risks_Name";
		String ciqProfileDescription = "DCI Custom Dictionaries With Risks Description";
		String ciqProfileType = "DCI_CUSTOM_TERMS";
		String ciqProfileCount = "1";
		String ciqType = "CD";
		String ciqProfileKeyword = "dci_custom_terms";

		Map<String, String> ciq = new HashMap<String, String>();
		ciq.put("ciqProfileName", ciqProfileName);
		ciq.put("ciqProfileDescription", ciqProfileDescription);
		ciq.put("ciqProfileType", ciqProfileType);
		ciq.put("ciqProfileCount", ciqProfileCount);
		ciq.put("ciqType", ciqType);
		ciq.put("ciqProfileKeyword", ciqProfileKeyword);

		try {

			List<String> keywords = new ArrayList<String>();
			keywords.add(ciqProfileType);
			dciFunctions.createDictionary(restClient, suiteData, ciqProfileType, "DCI Description", null, keywords,
					headers);

			Logger.info("Creating CIQ profile with custom dictionaries and risks in progress");
			List<String> valuesPreDefDict = null;
			List<String> valuesPreDefTerms = null;
			List<String> valuesCustomDict = Arrays.asList(ciqProfileType);
			List<String> valuesCustomTerms = null;List<String> valuesFileFormat=null;List<String> valuesMLProfile=null;
			List<String> valuesRisks = Arrays.asList("dlp", "hipaa", "vba_macros", "virus", "pci", "pii", "ferpa",
					"glba");
			List<String> valuesContentTypes = null;

			dciFunctions.createCIQProfile(suiteData, restClient, ciqProfileName, ciqProfileDescription, valuesPreDefDict, valuesPreDefTerms,
					valuesCustomDict, valuesCustomTerms, valuesRisks, valuesContentTypes, valuesFileFormat,valuesMLProfile, "high", 1, true, 1, false);

			Logger.info("Creating CIQ profile with custom dictionaries and risks is completed");
			dciFunctions.waitForSeconds(10);

		} catch (Exception ex) {
			Logger.info("Issue with Create Content Iq Profiles" + ex.getLocalizedMessage());
		}

		String[] fileName = dciFunctions.getFileName(DCIConstants.DCI_FILE_UPLOAD_CIQ_CUSTOM_DICT_RISKS_PATH);
		String[] risks = dciFunctions.riskTypesForAFile(fileName);

		fileName = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_CIQ_CUSTOM_DICT_RISKS_PATH,
				fileName);
		dciFunctions.fileUploadOrSendEmail(suiteData, folderInfo, fileName);

		Object[][] result = new Object[fileName.length][];
		for (int i = 0; i < fileName.length; i++) {
			boolean flag = false;
			if (fileName[i].contains("virus")) {
				flag = true;
			}
			result[i] = new Object[] { new TestParameters(
					"Risk Generation/Validation for risks file types:" + fileName[i] + " for risks " + risks[i],
					"Create CIQ profile with risks and CDs:" + ciqProfileName + ".Upload  risks file:" + fileName[i]
							+ ". Then verify risk logs are getting generated within the SLA provided" + " for risks "
							+ Arrays.asList(risks[i].split(",")),
							fileName[i], SaasType.getSaasFilterType(suiteData.getSaasApp()), Arrays.asList(risks[i].split(",")),
							ciq, flag) };
		}

		mainCounter = 0;
		testCounter = DCIConstants.DCI_COUNTER_MAX;

		return result;
	}

	@DataProvider(name = "dataUploadRisksNegPredefDict")
	public Object[][] dataUploadRisksNegPredefDict() {
		dciFunctions = new DCIFunctions();

		String ciqProfileName = "DCI_Predef_Dictionary_Risks_Name";
		String ciqProfileDescription = "DCI Predefined Dictionaries With Risks Description";
		String ciqProfileType = "Illegal Drugs";
		String ciqProfileCount = "1";
		String ciqType = "PDD";
		String ciqProfileKeyword = "anadrol";

		Map<String, String> ciq = new HashMap<String, String>();
		ciq.put("ciqProfileName", ciqProfileName);
		ciq.put("ciqProfileDescription", ciqProfileDescription);
		ciq.put("ciqProfileType", ciqProfileType);
		ciq.put("ciqProfileCount", ciqProfileCount);
		ciq.put("ciqType", ciqType);
		ciq.put("ciqProfileKeyword", ciqProfileKeyword);

		try {
			Logger.info("Creating CIQ profile with predefined dictionaries and risks in progress");
			List<String> valuesPreDefDict = Arrays.asList(ciqProfileType);
			List<String> valuesPreDefTerms = null;
			List<String> valuesCustomDict = null;
			List<String> valuesCustomTerms = null;List<String> valuesFileFormat=null;List<String> valuesMLProfile=null;
			List<String> valuesRisks = Arrays.asList("dlp", "hipaa", "vba_macros", "virus", "pci", "pii", "ferpa",
					"glba");
			List<String> valuesContentTypes = null;

			dciFunctions.createCIQProfile(suiteData, restClient, ciqProfileName, ciqProfileDescription, valuesPreDefDict, valuesPreDefTerms,
					valuesCustomDict, valuesCustomTerms, valuesRisks, valuesContentTypes, valuesFileFormat,valuesMLProfile, "high", 1, true, 1, false);

			Logger.info("Creating CIQ profile with predefined dictionaries and risks is completed");
			dciFunctions.waitForSeconds(10);

		} catch (Exception ex) {
			Logger.info("Issue with Create Content Iq Profiles" + ex.getLocalizedMessage());
		}

		String[] fileName = dciFunctions.getFileName(DCIConstants.DCI_FILE_UPLOAD_CIQ_RISKS_PATH);
		String[] risks = dciFunctions.riskTypesForAFile(fileName);

		fileName = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_CIQ_RISKS_PATH,
				fileName);
		dciFunctions.fileUploadOrSendEmail(suiteData, folderInfo, fileName);

		Object[][] result = new Object[fileName.length][];
		for (int i = 0; i < fileName.length; i++) {
			boolean flag = false;
			if (fileName[i].contains("virus")) {
				flag = true;
			}
			result[i] = new Object[] { new TestParameters(
					"Risk Generation/Validation for risks file types:" + fileName[i] + " for risks " + risks[i],
					"Create CIQ profile with risks and PDDs:" + ciqProfileName + ".Upload  risks file:" + fileName[i]
							+ ". Then verify risk logs are getting generated within the SLA provided" + " for risks "
							+ Arrays.asList(risks[i].split(",")),
							fileName[i], SaasType.getSaasFilterType(suiteData.getSaasApp()), Arrays.asList(risks[i].split(",")),
							ciq, flag) };
		}

		mainCounter = 0;
		testCounter = DCIConstants.DCI_COUNTER_MAX;

		return result;
	}

	@DataProvider(name = "dataUploadRisksNegPredefTerms")
	public Object[][] dataUploadRisksNegPredefTerms() {
		dciFunctions = new DCIFunctions();

		String ciqProfileName = "DCI_Predef_Terms_Risks_Name";
		String ciqProfileDescription = "DCI Predefined Terms With Risks Description";
		String ciqProfileType = "France National ID Card (CNI)";
		String ciqProfileCount = "1";
		String ciqType = "PDT";

		Map<String, String> ciq = new HashMap<String, String>();
		ciq.put("ciqProfileName", ciqProfileName);
		ciq.put("ciqProfileDescription", ciqProfileDescription);
		ciq.put("ciqProfileType", ciqProfileType);
		ciq.put("ciqProfileCount", ciqProfileCount);
		ciq.put("ciqType", ciqType);

		try {
			Logger.info("Creating CIQ profile with predefined terms and risks in progress");
			List<String> valuesPreDefDict = null;
			List<String> valuesPreDefTerms = Arrays.asList(ciqProfileType);
			List<String> valuesCustomDict = null;
			List<String> valuesCustomTerms = null;List<String> valuesFileFormat=null;List<String> valuesMLProfile=null;
			List<String> valuesRisks = Arrays.asList("dlp", "hipaa", "vba_macros", "virus", "pci", "pii", "ferpa",
					"glba");
			List<String> valuesContentTypes = null;

			dciFunctions.createCIQProfile(suiteData, restClient, ciqProfileName, ciqProfileDescription, valuesPreDefDict, valuesPreDefTerms,
					valuesCustomDict, valuesCustomTerms, valuesRisks, valuesContentTypes, valuesFileFormat,valuesMLProfile, "high", 1, true, 1, false);

			Logger.info("Creating CIQ profile with predefined terms and risks is completed");
			dciFunctions.waitForSeconds(10);

		} catch (Exception ex) {
			Logger.info("Issue with Create Content Iq Profiles" + ex.getLocalizedMessage());
		}

		String[] fileName = dciFunctions.getFileName(DCIConstants.DCI_FILE_UPLOAD_CIQ_RISKS_PATH);
		String[] risks = dciFunctions.riskTypesForAFile(fileName);

		fileName = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_CIQ_RISKS_PATH,
				fileName);
		dciFunctions.fileUploadOrSendEmail(suiteData, folderInfo, fileName);

		Object[][] result = new Object[fileName.length][];
		for (int i = 0; i < fileName.length; i++) {
			boolean flag = false;
			if (fileName[i].contains("virus")) {
				flag = true;
			}
			result[i] = new Object[] { new TestParameters(
					"Risk Generation/Validation for risks file types:" + fileName[i] + " for risks " + risks[i],
					"Create CIQ profile with risks and PDTs:" + ciqProfileName + ".Upload  risks file:" + fileName[i]
							+ ". Then verify risk logs are getting generated within the SLA provided" + " for risks "
							+ Arrays.asList(risks[i].split(",")),
							fileName[i], SaasType.getSaasFilterType(suiteData.getSaasApp()), Arrays.asList(risks[i].split(",")),
							ciq, flag) };
		}

		mainCounter = 0;
		testCounter = DCIConstants.DCI_COUNTER_MAX;

		return result;
	}

	@DataProvider(name = "dataUploadRisksNegCustomTerms")
	public Object[][] dataUploadRisksNegCustomTerms() {
		dciFunctions = new DCIFunctions();

		String ciqProfileName = "DCI_Custom_Terms_Risks_Name";
		String ciqProfileDescription = "DCI Custom Terms With Risks Description";
		String ciqProfileType = "DCI_CUSTOM_TERMS";
		String ciqProfileCount = "1";
		String ciqType = "CT";

		Map<String, String> ciq = new HashMap<String, String>();
		ciq.put("ciqProfileName", ciqProfileName);
		ciq.put("ciqProfileDescription", ciqProfileDescription);
		ciq.put("ciqProfileType", ciqProfileType);
		ciq.put("ciqProfileCount", ciqProfileCount);
		ciq.put("ciqType", ciqType);

		try {
			Logger.info("Creating CIQ profile with custom terms and risks in progress");
			List<String> valuesPreDefDict = null;
			List<String> valuesPreDefTerms = null;
			List<String> valuesCustomDict = null;
			List<String> valuesCustomTerms = Arrays.asList(ciqProfileType);
			List<String> valuesRisks = Arrays.asList("dlp", "hipaa", "vba_macros", "virus", "pci", "pii", "ferpa",
					"glba");
			List<String> valuesContentTypes = null;List<String> valuesFileFormat=null;List<String> valuesMLProfile=null;

			dciFunctions.createCIQProfile(suiteData, restClient, ciqProfileName, ciqProfileDescription, valuesPreDefDict, valuesPreDefTerms,
					valuesCustomDict, valuesCustomTerms, valuesRisks, valuesContentTypes, valuesFileFormat,valuesMLProfile, "high", 1, true, 1, false);

			Logger.info("Creating CIQ profile with custom terms and risks is completed");
			dciFunctions.waitForSeconds(10);

		} catch (Exception ex) {
			Logger.info("Issue with Create Content Iq Profiles" + ex.getLocalizedMessage());
		}

		String[] fileName = dciFunctions.getFileName(DCIConstants.DCI_FILE_UPLOAD_CIQ_RISKS_PATH);
		String[] risks = dciFunctions.riskTypesForAFile(fileName);

		fileName = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_CIQ_RISKS_PATH,
				fileName);
		dciFunctions.fileUploadOrSendEmail(suiteData, folderInfo, fileName);

		Object[][] result = new Object[fileName.length][];
		for (int i = 0; i < fileName.length; i++) {
			boolean flag = false;
			if (fileName[i].contains("virus")) {
				flag = true;
			}
			result[i] = new Object[] { new TestParameters(
					"Risk Generation/Validation for risks file types:" + fileName[i] + " for risks " + risks[i],
					"Create CIQ profile with risks and CTs:" + ciqProfileName + ".Upload  risks file:" + fileName[i]
							+ ". Then verify risk logs are getting generated within the SLA provided" + " for risks "
							+ Arrays.asList(risks[i].split(",")),
							fileName[i], SaasType.getSaasFilterType(suiteData.getSaasApp()), Arrays.asList(risks[i].split(",")),
							ciq, flag) };
		}

		mainCounter = 0;
		testCounter = DCIConstants.DCI_COUNTER_MAX;

		return result;
	}

	@DataProvider(name = "dataUploadRisksNegCustomDict")
	public Object[][] dataUploadRisksNegCustomDict() {
		dciFunctions = new DCIFunctions();

		List<NameValuePair> headers = dciFunctions.getBrowserHeaders(suiteData);

		String ciqProfileName = "DCI_Custom_Dictionary_Risks_Name";
		String ciqProfileDescription = "DCI Custom Dictionaries With Risks Description";
		String ciqProfileType = "DCI_CUSTOM_TERMS";
		String ciqProfileCount = "1";
		String ciqType = "CD";
		String ciqProfileKeyword = "dci_custom_terms";

		Map<String, String> ciq = new HashMap<String, String>();
		ciq.put("ciqProfileName", ciqProfileName);
		ciq.put("ciqProfileDescription", ciqProfileDescription);
		ciq.put("ciqProfileType", ciqProfileType);
		ciq.put("ciqProfileCount", ciqProfileCount);
		ciq.put("ciqType", ciqType);
		ciq.put("ciqProfileKeyword", ciqProfileKeyword);

		try {

			List<String> keywords = new ArrayList<String>();
			keywords.add(ciqProfileType);
			dciFunctions.createDictionary(restClient, suiteData, ciqProfileType, "DCI Description", null, keywords,
					headers);

			Logger.info("Creating CIQ profile with custom dictionaries and risks in progress");
			List<String> valuesPreDefDict = null;
			List<String> valuesPreDefTerms = null;
			List<String> valuesCustomDict = Arrays.asList(ciqProfileType);
			List<String> valuesCustomTerms = null;List<String> valuesFileFormat=null;List<String> valuesMLProfile=null;
			List<String> valuesRisks = Arrays.asList("dlp", "hipaa", "vba_macros", "virus", "pci", "pii", "ferpa",
					"glba");
			List<String> valuesContentTypes = null;

			dciFunctions.createCIQProfile(suiteData, restClient, ciqProfileName, ciqProfileDescription, valuesPreDefDict, valuesPreDefTerms,
					valuesCustomDict, valuesCustomTerms, valuesRisks, valuesContentTypes, valuesFileFormat,valuesMLProfile, "high", 1, true, 1, false);

			Logger.info("Creating CIQ profile with custom dictionaries and risks is completed");
			dciFunctions.waitForSeconds(10);

		} catch (Exception ex) {
			Logger.info("Issue with Create Content Iq Profiles" + ex.getLocalizedMessage());
		}

		String[] fileName = dciFunctions.getFileName(DCIConstants.DCI_FILE_UPLOAD_CIQ_RISKS_PATH);
		String[] risks = dciFunctions.riskTypesForAFile(fileName);

		fileName = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_CIQ_RISKS_PATH,
				fileName);
		dciFunctions.fileUploadOrSendEmail(suiteData, folderInfo, fileName);

		Object[][] result = new Object[fileName.length][];
		for (int i = 0; i < fileName.length; i++) {
			boolean flag = false;
			if (fileName[i].contains("virus")) {
				flag = true;
			}
			result[i] = new Object[] { new TestParameters(
					"Risk Generation/Validation for risks file types:" + fileName[i] + " for risks " + risks[i],
					"Create CIQ profile with risks and CDs:" + ciqProfileName + ".Upload  risks file:" + fileName[i]
							+ ". Then verify risk logs are getting generated within the SLA provided" + " for risks "
							+ Arrays.asList(risks[i].split(",")),
							fileName[i], SaasType.getSaasFilterType(suiteData.getSaasApp()), Arrays.asList(risks[i].split(",")),
							ciq, flag) };
		}

		mainCounter = 0;
		testCounter = DCIConstants.DCI_COUNTER_MAX;

		return result;
	}

	@DataProvider(name = "dataUploadCIQRisksNegative")
	public Object[][] dataUploadCIQRisksNegative() {

		dciFunctions = new DCIFunctions();

		String ciqProfileName = "DCI_Predef_Dictionary_Risks_Name";
		String ciqProfileDescription = "DCI Predefined Dictionaries With Risks Description";
		String ciqProfileType = "Illegal Drugs";

		try {
			Logger.info("Creating CIQ profile with predefined dictionaries and risks in progress");
			List<String> valuesPreDefDict = Arrays.asList(ciqProfileType);
			List<String> valuesPreDefTerms = null;
			List<String> valuesCustomDict = null;
			List<String> valuesCustomTerms = null;List<String> valuesFileFormat=null;List<String> valuesMLProfile=null;
			List<String> valuesRisks = Arrays.asList("dlp", "hipaa", "vba_macros", "virus", "pci", "pii", "ferpa",
					"glba");
			List<String> valuesContentTypes = null;

			dciFunctions.createCIQProfile(suiteData, restClient, ciqProfileName, ciqProfileDescription, valuesPreDefDict, valuesPreDefTerms,
					valuesCustomDict, valuesCustomTerms, valuesRisks, valuesContentTypes, valuesFileFormat,valuesMLProfile, "high", 1, true, 1, false);

			Logger.info("Creating CIQ profile with predefined dictionaries and risks is completed");
			dciFunctions.waitForSeconds(10);

		} catch (Exception ex) {
			Logger.info("Issue with Create Content Iq Profiles" + ex.getLocalizedMessage());
		}

		ciqProfileName = "DCI_Predef_Terms_Risks_Name";
		ciqProfileDescription = "DCI Predefined Terms With Risks Description";
		ciqProfileType = "France National ID Card (CNI)";

		try {
			Logger.info("Creating CIQ profile with predefined terms and risks in progress");
			List<String> valuesPreDefDict = null;
			List<String> valuesPreDefTerms = Arrays.asList(ciqProfileType);
			List<String> valuesCustomDict = null;
			List<String> valuesCustomTerms = null;
			List<String> valuesRisks = Arrays.asList("dlp", "hipaa", "vba_macros", "virus", "pci", "pii", "ferpa",
					"glba");
			List<String> valuesContentTypes = null;List<String> valuesFileFormat=null;List<String> valuesMLProfile=null;

			dciFunctions.createCIQProfile(suiteData, restClient, ciqProfileName, ciqProfileDescription, valuesPreDefDict, valuesPreDefTerms,
					valuesCustomDict, valuesCustomTerms, valuesRisks, valuesContentTypes, valuesFileFormat,valuesMLProfile, "high", 1, true, 1, false);

			Logger.info("Creating CIQ profile with predefined terms and risks is completed");
			dciFunctions.waitForSeconds(10);

		} catch (Exception ex) {
			Logger.info("Issue with Create Content Iq Profiles" + ex.getLocalizedMessage());
		}

		ciqProfileName = "DCI_Custom_Terms_Risks_Name";
		ciqProfileDescription = "DCI Custom Terms With Risks Description";
		ciqProfileType = "DCI_CUSTOM_TERMS";

		try {
			Logger.info("Creating CIQ profile with custom terms and risks in progress");
			List<String> valuesPreDefDict = null;
			List<String> valuesPreDefTerms = null;
			List<String> valuesCustomDict = null;
			List<String> valuesFileFormat=null;List<String> valuesMLProfile=null;
			List<String> valuesCustomTerms = Arrays.asList(ciqProfileType);
			List<String> valuesRisks = Arrays.asList("dlp", "hipaa", "vba_macros", "virus", "pci", "pii", "ferpa",
					"glba");
			List<String> valuesContentTypes = null;

			dciFunctions.createCIQProfile(suiteData, restClient, ciqProfileName, ciqProfileDescription, valuesPreDefDict, valuesPreDefTerms,
					valuesCustomDict, valuesCustomTerms, valuesRisks, valuesContentTypes, valuesFileFormat,valuesMLProfile, "high", 1, true, 1, false);

			Logger.info("Creating CIQ profile with custom terms and risks is completed");
			dciFunctions.waitForSeconds(10);

		} catch (Exception ex) {
			Logger.info("Issue with Create Content Iq Profiles" + ex.getLocalizedMessage());
		}

		List<NameValuePair> headers = dciFunctions.getBrowserHeaders(suiteData);

		ciqProfileName = "DCI_Custom_Dictionary_Risks_Name";
		ciqProfileDescription = "DCI Custom Dictionaries With Risks Description";
		ciqProfileType = "DCI_CUSTOM_TERMS";

		try {

			List<String> keywords = new ArrayList<String>();
			keywords.add(ciqProfileType);
			dciFunctions.createDictionary(restClient, suiteData, ciqProfileType, "DCI Description", null, keywords,
					headers);

			Logger.info("Creating CIQ profile with custom dictionaries and risks in progress");
			List<String> valuesPreDefDict = null;
			List<String> valuesPreDefTerms = null;
			List<String> valuesCustomDict = Arrays.asList(ciqProfileType);
			List<String> valuesCustomTerms = null;List<String> valuesFileFormat=null;List<String> valuesMLProfile=null;
			List<String> valuesRisks = Arrays.asList("dlp", "hipaa", "vba_macros", "virus", "pci", "pii", "ferpa",
					"glba");
			List<String> valuesContentTypes = null;

			dciFunctions.createCIQProfile(suiteData, restClient, ciqProfileName, ciqProfileDescription, valuesPreDefDict, valuesPreDefTerms,
					valuesCustomDict, valuesCustomTerms, valuesRisks, valuesContentTypes, valuesFileFormat,valuesMLProfile, "high", 1, true, 1, false);

			Logger.info("Creating CIQ profile with custom dictionaries and risks is completed");
			dciFunctions.waitForSeconds(10);

		} catch (Exception ex) {
			Logger.info("Issue with Create Content Iq Profiles" + ex.getLocalizedMessage());
		}

		ciqProfileName = "DCI_Only_Risks_Name";
		ciqProfileDescription = "DCI Only Risks Description";

		try {
			Logger.info("Creating CIQ profile with only risks in progress");
			List<String> valuesPreDefDict = null;
			List<String> valuesPreDefTerms = null;
			List<String> valuesCustomDict = null;
			List<String> valuesCustomTerms = null;List<String> valuesFileFormat=null;List<String> valuesMLProfile=null;
			List<String> valuesRisks = Arrays.asList("dlp", "hipaa", "vba_macros", "virus", "pci", "pii", "ferpa",
					"glba");
			List<String> valuesContentTypes = null;

			dciFunctions.createCIQProfile(suiteData, restClient, ciqProfileName, ciqProfileDescription, valuesPreDefDict, valuesPreDefTerms,
					valuesCustomDict, valuesCustomTerms, valuesRisks, valuesContentTypes, valuesFileFormat,valuesMLProfile, "high", 0, true, 1, false);
			Logger.info("Creating CIQ profile with only risks is completed");
			dciFunctions.waitForSeconds(10);

		} catch (Exception ex) {
			Logger.info("Issue with Create Content Iq Profiles" + ex.getLocalizedMessage());
		}

		String[] fileName = dciFunctions.getFileName(DCIConstants.DCI_FILE_UPLOAD_CIQRISKS_ONLY_PATH);

		fileName = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_CIQRISKS_ONLY_PATH,
				fileName);
		dciFunctions.fileUploadOrSendEmail(suiteData, folderInfo, fileName);

		Object[][] result = new Object[fileName.length][];
		for (int i = 0; i < fileName.length; i++) {
			boolean flag = false;
			if (fileName[i].contains("illegal_drugs.txt")) {
				flag = true;
			}
			result[i] = new Object[] { new TestParameters("No Risk Generation/Validation for ciq file:" + fileName[i],
					"Upload ciq file:" + fileName[i]
							+ ". Then verify risk logs are not getting generated within the SLA provided",
							fileName[i], SaasType.getSaasFilterType(suiteData.getSaasApp()), flag) };
		}

		dciFunctions.waitForMinutes(2);

		return result;
	}

	/**
	 * Create CIQ Profile with name with content types and upload file. 
	 * Then verify risk types logs are getting generated within the SLA provided.
	 * Once logs are generated verify the contents
	 * @param testParams
	 * @throws Exception
	 */
	@Test(dataProvider = "dataUploadContentOnlyCIQ", groups ={"Content"})
	public void testDisplayLogsForRisksGeneratedInCIQProfileWithOnlyContentTypes(TestParameters testParams) throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();

		Logger.info(
				"************************************Starting "+testParams.getTestName()+" for filename:" + testParams.getFileName() + " and saas app type:" + testParams.getSaasType()+ "******************");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description:"+testParams.getTestDescription());
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output****************");
		Logger.info("Filename:"+testParams.getFileName());
		Logger.info("Risk Types to be validated:"+testParams.getRisks());
		Logger.info("Saas App Type:"+testParams.getSaasType());
		Logger.info("*****************************************************");

		try{
			String[] risks = testParams.getRisks().toArray(new String[testParams.getRisks().size()]);
			List<NameValuePair> headers = dciFunctions.getBasicHeaders(suiteData);		
			verifyDisplayLogsWithCIQ(dciFunctions, testParams.getFileName(), testParams.getSaasType(), 
					headers, risks, testParams.getCiq());
		}finally{
			if(testParams.getDeleteFlag()){
				dciFunctions.deleteAllCIQProfiles(restClient, suiteData);
				dciFunctions.deleteAllDictionaries(restClient, suiteData);
			}
		}
		Logger.info(
				"************************************Completed "+testParams.getTestName()+" for filename:" + testParams.getFileName() + " and saas app type:" + testParams.getSaasType()+ "******************");

	}

	/**
	 * Create CIQ Profile with name with content types and upload file. 
	 * Then verify risk types logs are getting generated within the SLA provided.
	 * Once logs are generated verify the contents
	 * @param testParams
	 * @throws Exception
	 */
	@Test(dataProvider = "dataUploadContentSingleCIQ", groups ={"Content"})
	public void testDisplayLogsForRisksGeneratedInCIQProfileWithSingleContent(TestParameters testParams) throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();

		Logger.info(
				"************************************Starting "+testParams.getTestName()+" for filename:" + testParams.getFileName() + " and saas app type:" + testParams.getSaasType()+ "******************");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description:"+testParams.getTestDescription());
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output****************");
		Logger.info("Filename:"+testParams.getFileName());
		Logger.info("Risk Types to be validated:"+testParams.getRisks());
		Logger.info("Saas App Type:"+testParams.getSaasType());
		Logger.info("*****************************************************");

		try{
			String[] risks = testParams.getRisks().toArray(new String[testParams.getRisks().size()]);
			List<NameValuePair> headers = dciFunctions.getBasicHeaders(suiteData);		
			verifyDisplayLogsWithCIQ(dciFunctions, testParams.getFileName(), testParams.getSaasType(), 
					headers, risks, testParams.getCiq());

		}finally{
			if(testParams.getDeleteFlag()){
				dciFunctions.deleteAllCIQProfiles(restClient, suiteData);
				dciFunctions.deleteAllDictionaries(restClient, suiteData);
			}
		}
		Logger.info(
				"************************************Completed "+testParams.getTestName()+" for filename:" + testParams.getFileName() + " and saas app type:" + testParams.getSaasType()+ "******************");

	}



	/**
	 * Create CIQ Profile with name with predefined dictionaries and upload file. 
	 * Then verify risk logs are getting generated within the SLA provided.
	 * Once logs are generated verify the contents
	 * @param testParams
	 * @throws Exception
	 */
	@Test(dataProvider = "dataUploadContentPredefDict", groups ={"Content"})
	public void testDisplayLogsForRiskGeneratedInPredefinedDictionariesContent(TestParameters testParams) throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();

		Logger.info(
				"************************************Starting "+testParams.getTestName()+" for filename:" + testParams.getFileName() + " and saas app type:" + testParams.getSaasType()+ "******************");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description:"+testParams.getTestDescription());
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output****************");
		Logger.info("Filename:"+testParams.getFileName());
		Logger.info("Risk Types to be validated:"+testParams.getRisks());
		Logger.info("Saas App Type:"+testParams.getSaasType());
		Logger.info("*****************************************************");

		try{
			String[] risks = testParams.getRisks().toArray(new String[testParams.getRisks().size()]);
			List<NameValuePair> headers = dciFunctions.getBasicHeaders(suiteData);		
			verifyDisplayLogsWithCIQ(dciFunctions, testParams.getFileName(), testParams.getSaasType(), 
					headers, risks, testParams.getCiq());

		}finally{
			if(testParams.getDeleteFlag()){
				dciFunctions.deleteAllCIQProfiles(restClient, suiteData);
				dciFunctions.deleteAllDictionaries(restClient, suiteData);
			}
		}
		Logger.info(
				"************************************Completed "+testParams.getTestName()+" for filename:" + testParams.getFileName() + " and saas app type:" + testParams.getSaasType()+ "******************");

	}

	/**
	 * Create CIQ Profile with name with predefined terms and upload file. 
	 * Then verify risk logs are getting generated within the SLA provided.
	 * Once logs are generated verify the contents
	 * @param testParams
	 * @throws Exception
	 */
	@Test(dataProvider = "dataUploadContentPredefTerms", groups ={"Content"})
	public void testDisplayLogsForRiskGeneratedInPredefinedTermsContent(TestParameters testParams) throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();

		Logger.info(
				"************************************Starting "+testParams.getTestName()+" for filename:" + testParams.getFileName() + " and saas app type:" + testParams.getSaasType()+ "******************");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description:"+testParams.getTestDescription());
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output****************");
		Logger.info("Filename:"+testParams.getFileName());
		Logger.info("Risk Types to be validated:"+testParams.getRisks());
		Logger.info("Saas App Type:"+testParams.getSaasType());
		Logger.info("*****************************************************");

		try{
			String[] risks = testParams.getRisks().toArray(new String[testParams.getRisks().size()]);
			List<NameValuePair> headers = dciFunctions.getBasicHeaders(suiteData);		
			verifyDisplayLogsWithCIQ(dciFunctions, testParams.getFileName(), testParams.getSaasType(), 
					headers, risks, testParams.getCiq());

		}finally{
			if(testParams.getDeleteFlag()){
				dciFunctions.deleteAllCIQProfiles(restClient, suiteData);
				dciFunctions.deleteAllDictionaries(restClient, suiteData);
			}
		}
		Logger.info(
				"************************************Completed "+testParams.getTestName()+" for filename:" + testParams.getFileName() + " and saas app type:" + testParams.getSaasType()+ "******************");

	}

	/**
	 * Create CIQ Profile with name with Custom terms and upload file. 
	 * Then verify risk logs are getting generated within the SLA provided.
	 * Once logs are generated verify the contents
	 * @param testParams
	 * @throws Exception
	 */
	@Test(dataProvider = "dataUploadContentCustomTerms", groups ={"Content"})
	public void testDisplayLogsForRiskGeneratedInCustomTermsContent(TestParameters testParams) throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();

		Logger.info(
				"************************************Starting "+testParams.getTestName()+" for filename:" + testParams.getFileName() + " and saas app type:" + testParams.getSaasType()+ "******************");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description:"+testParams.getTestDescription());
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output****************");
		Logger.info("Filename:"+testParams.getFileName());
		Logger.info("Risk Types to be validated:"+testParams.getRisks());
		Logger.info("Saas App Type:"+testParams.getSaasType());
		Logger.info("*****************************************************");

		try{
			String[] risks = testParams.getRisks().toArray(new String[testParams.getRisks().size()]);
			List<NameValuePair> headers = dciFunctions.getBasicHeaders(suiteData);		
			verifyDisplayLogsWithCIQ(dciFunctions, testParams.getFileName(), testParams.getSaasType(), 
					headers, risks, testParams.getCiq());

		}finally{
			if(testParams.getDeleteFlag()){
				dciFunctions.deleteAllCIQProfiles(restClient, suiteData);
				dciFunctions.deleteAllDictionaries(restClient, suiteData);
			}
		}
		Logger.info(
				"************************************Completed "+testParams.getTestName()+" for filename:" + testParams.getFileName() + " and saas app type:" + testParams.getSaasType()+ "******************");

	}

	/**
	 * Create CIQ Profile with name with Custom dictionaries and upload file. 
	 * Then verify risk logs are getting generated within the SLA provided.
	 * Once logs are generated verify the contents
	 * @param testParams
	 * @throws Exception
	 */
	@Test(dataProvider = "dataUploadContentCustomDict", groups ={"Content"})
	public void testDisplayLogsForRiskGeneratedInCustomDictionariesContent(TestParameters testParams) throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();

		Logger.info(
				"************************************Starting "+testParams.getTestName()+" for filename:" + testParams.getFileName() + " and saas app type:" + testParams.getSaasType()+ "******************");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description:"+testParams.getTestDescription());
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output****************");
		Logger.info("Filename:"+testParams.getFileName());
		Logger.info("Risk Types to be validated:"+testParams.getRisks());
		Logger.info("Saas App Type:"+testParams.getSaasType());
		Logger.info("*****************************************************");
		try{
			String[] risks = testParams.getRisks().toArray(new String[testParams.getRisks().size()]);
			List<NameValuePair> headers = dciFunctions.getBasicHeaders(suiteData);		
			verifyDisplayLogsWithCIQ(dciFunctions, testParams.getFileName(), testParams.getSaasType(), 
					headers, risks, testParams.getCiq());

		}finally{
			if(testParams.getDeleteFlag()){
				dciFunctions.deleteAllCIQProfiles(restClient, suiteData);
				dciFunctions.deleteAllDictionaries(restClient, suiteData);
			}
		}
		Logger.info(
				"************************************Completed "+testParams.getTestName()+" for filename:" + testParams.getFileName() + " and saas app type:" + testParams.getSaasType()+ "******************");

	}

	/**
	 * Create CIQ Profile with name with predefined dictionaries and upload file. 
	 * Then verify risk logs are getting generated within the SLA provided.
	 * Once logs are generated verify the contents
	 * @param testParams
	 * @throws Exception
	 */
	@Test(dataProvider = "dataUploadContentNegPredefDict", groups ={"Content"})
	public void testDisplayLogsForNoRiskGeneratedInPredefinedDictionariesContent(TestParameters testParams) throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();

		Logger.info(
				"************************************Starting "+testParams.getTestName()+" for filename:" + testParams.getFileName() + " and saas app type:" + testParams.getSaasType()+ "******************");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description:"+testParams.getTestDescription());
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output****************");
		Logger.info("Filename:"+testParams.getFileName());
		Logger.info("Risk Types to be validated:"+testParams.getRisks());
		Logger.info("Saas App Type:"+testParams.getSaasType());
		Logger.info("*****************************************************");
		try{
			String[] risks = testParams.getRisks().toArray(new String[testParams.getRisks().size()]);
			List<NameValuePair> headers = dciFunctions.getBasicHeaders(suiteData);		
			verifyDisplayLogsWithContent(dciFunctions, testParams.getFileName(), testParams.getSaasType(), 
					headers, risks, testParams.getCiq());

		}finally{
			if(testParams.getDeleteFlag()){
				dciFunctions.deleteAllCIQProfiles(restClient, suiteData);
				dciFunctions.deleteAllDictionaries(restClient, suiteData);
			}
		}
		Logger.info(
				"************************************Completed "+testParams.getTestName()+" for filename:" + testParams.getFileName() + " and saas app type:" + testParams.getSaasType()+ "******************");

	}

	/**
	 * Create CIQ Profile with name with predefined terms and upload file. 
	 * Then verify risk logs are getting generated within the SLA provided.
	 * Once logs are generated verify the contents
	 * @param testParams
	 * @throws Exception
	 */
	@Test(dataProvider = "dataUploadContentNegPredefTerms", groups ={"Content"})
	public void testDisplayLogsForNoRiskGeneratedInPredefinedTermsContent(TestParameters testParams) throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();

		Logger.info(
				"************************************Starting "+testParams.getTestName()+" for filename:" + testParams.getFileName() + " and saas app type:" + testParams.getSaasType()+ "******************");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description:"+testParams.getTestDescription());
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output****************");
		Logger.info("Filename:"+testParams.getFileName());
		Logger.info("Risk Types to be validated:"+testParams.getRisks());
		Logger.info("Saas App Type:"+testParams.getSaasType());
		Logger.info("*****************************************************");
		try{
			String[] risks = testParams.getRisks().toArray(new String[testParams.getRisks().size()]);
			List<NameValuePair> headers = dciFunctions.getBasicHeaders(suiteData);		
			verifyDisplayLogsWithContent(dciFunctions, testParams.getFileName(), testParams.getSaasType(), 
					headers, risks, testParams.getCiq());

		}finally{
			if(testParams.getDeleteFlag()){
				dciFunctions.deleteAllCIQProfiles(restClient, suiteData);
				dciFunctions.deleteAllDictionaries(restClient, suiteData);
			}
		}
		Logger.info(
				"************************************Completed "+testParams.getTestName()+" for filename:" + testParams.getFileName() + " and saas app type:" + testParams.getSaasType()+ "******************");

	}

	/**
	 * Create CIQ Profile with name with Custom terms and upload file. 
	 * Then verify risk logs are getting generated within the SLA provided.
	 * Once logs are generated verify the contents
	 * @param testParams
	 * @throws Exception
	 */
	@Test(dataProvider = "dataUploadContentNegCustomTerms", groups ={"Content"})
	public void testDisplayLogsForNoRiskGeneratedInCustomTermsContent(TestParameters testParams) throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();

		Logger.info(
				"************************************Starting "+testParams.getTestName()+" for filename:" + testParams.getFileName() + " and saas app type:" + testParams.getSaasType()+ "******************");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description:"+testParams.getTestDescription());
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output****************");
		Logger.info("Filename:"+testParams.getFileName());
		Logger.info("Risk Types to be validated:"+testParams.getRisks());
		Logger.info("Saas App Type:"+testParams.getSaasType());
		Logger.info("*****************************************************");
		try{
			String[] risks = testParams.getRisks().toArray(new String[testParams.getRisks().size()]);
			List<NameValuePair> headers = dciFunctions.getBasicHeaders(suiteData);		
			verifyDisplayLogsWithContent(dciFunctions, testParams.getFileName(), testParams.getSaasType(), 
					headers, risks, testParams.getCiq());

		}finally{
			if(testParams.getDeleteFlag()){
				dciFunctions.deleteAllCIQProfiles(restClient, suiteData);
				dciFunctions.deleteAllDictionaries(restClient, suiteData);
			}
		}
		Logger.info(
				"************************************Completed "+testParams.getTestName()+" for filename:" + testParams.getFileName() + " and saas app type:" + testParams.getSaasType()+ "******************");

	}

	/**
	 * Create CIQ Profile with name with Custom dictionaries and upload file. 
	 * Then verify risk logs are getting generated within the SLA provided.
	 * Once logs are generated verify the contents
	 * @param testParams
	 * @throws Exception
	 */
	@Test(dataProvider = "dataUploadContentNegCustomDict", groups ={"Content"})
	public void testDisplayLogsForNoRiskGeneratedInCustomDictionariesContent(TestParameters testParams) throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();

		Logger.info(
				"************************************Starting "+testParams.getTestName()+" for filename:" + testParams.getFileName() + " and saas app type:" + testParams.getSaasType()+ "******************");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description:"+testParams.getTestDescription());
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output****************");
		Logger.info("Filename:"+testParams.getFileName());
		Logger.info("Risk Types to be validated:"+testParams.getRisks());
		Logger.info("Saas App Type:"+testParams.getSaasType());
		Logger.info("*****************************************************");
		try{
			String[] risks = testParams.getRisks().toArray(new String[testParams.getRisks().size()]);
			List<NameValuePair> headers = dciFunctions.getBasicHeaders(suiteData);		
			verifyDisplayLogsWithContent(dciFunctions, testParams.getFileName(), testParams.getSaasType(), 
					headers, risks, testParams.getCiq());

		}finally{
			if(testParams.getDeleteFlag()){
				dciFunctions.deleteAllCIQProfiles(restClient, suiteData);
				dciFunctions.deleteAllDictionaries(restClient, suiteData);
			}
		}
		Logger.info(
				"************************************Completed "+testParams.getTestName()+" for filename:" + testParams.getFileName() + " and saas app type:" + testParams.getSaasType()+ "******************");

	}

	@Test(dataProvider = "dataUploadCIQContentNegative", groups ={"Content"})
	public void testDisplayLogsForNoRiskGeneratedForCIQFilesContent(TestParameters testParams) throws Exception {
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
		try{
			List<NameValuePair> headers = dciFunctions.getBasicHeaders(suiteData);		
			verifyDisplayLogs(dciFunctions, testParams.getFileName(), testParams.getSaasType(), headers);

		}finally{
			if(testParams.getDeleteFlag()){
				dciFunctions.deleteAllCIQProfiles(restClient, suiteData);
				dciFunctions.deleteAllDictionaries(restClient, suiteData);
			}
		}	
		Logger.info(
				"************************************Completed "+testParams.getTestName()+" for saas app type:" + testParams.getSaasType()+ "******************");

	}


	/**********************************************
	 * DATA PROVIDERS
	 *********************************************/
	/**********************************************
	 * UTIL METHODS
	 ***********************************************/

	@Override
	public String getTestName() {
		return this.mTestCaseName;
	}

	private void verifyDisplayLogs(DCIFunctions dciFunctions, String fileName, String saasType,
			List<NameValuePair> headers) throws Exception {
		String hits = "";
		String responseBody = "";
		try {

			String payload = 
					dciFunctions.getSearchQueryForDCI(suiteData, dciFunctions.getMinusMinutesFromCurrentTime(1440) , 
							dciFunctions.getPlusMinutesFromCurrentTime(120), saasType, DCIConstants.CISourceType, 
							DCIConstants.CICriticalSeverityType, fileName, DCIConstants.CIFacilityType, DCIConstants.CIActivityType);

			Logger.info("****************Input Payload****************");
			Logger.info(payload);
			Logger.info("*********************************************");

			for (int i = 0; i < DCIConstants.DCI_COUNTER_MIN; i++) {
				HttpResponse response = esLogs.getDisplayLogs(restClient, headers, suiteData.getApiserverHostName(),
						new StringEntity(payload));
				responseBody = ClientUtil.getResponseBody(response);

				hits = dciFunctions.getJSONValue(dciFunctions.getJSONValue(responseBody, "hits"), "hits");
			}
			Logger.info("****************Output Response****************");
			Logger.info(hits);
			Logger.info("***********************************************");

			JSONArray jArray = (JSONArray) new JSONTokener(hits).nextValue();
			Assert.assertTrue(jArray.size() == 0, "Expected no risk logs for file upload of " + fileName
					+ " but seeing some risks:" + Arrays.asList(jArray));

		} finally {
			dciFunctions.cleanupFileFromTempFolder(fileName);
		}
	}

	private void verifyDisplayLogsWithContent(DCIFunctions dciFunctions, String fileName, String saasType,
			List<NameValuePair> headers, String[] risks, Map<String,String> ciqValues)
					throws Exception {
		String hits = "";
		try {

			String payload = 
					dciFunctions.getSearchQueryForDCI(dciFunctions.getMinusMinutesFromCurrentTime(1440) , dciFunctions.getPlusMinutesFromCurrentTime(120), saasType, "API",
							fileName, "investigate", suiteData);
			Logger.info("****************Input Payload****************");
			Logger.info(payload);
			Logger.info("*********************************************");

			hits = fetchDisplayLogsCounter(dciFunctions, headers, payload, DCIConstants.DCI_COUNTER_MAX_UL);


			Logger.info("****************Output Response****************");
			Logger.info(hits);
			Logger.info("***********************************************");

			String validationMessage = dciFunctions.validationRiskLogsWithCIQ(hits, fileName, suiteData.getUsername(),
					"QA Admin", saasType, "API", dciFunctions.riskTypesForAFile(fileName),
					dciFunctions.docClassTypesForAFile(fileName), 1, ciqValues);

			Assert.assertEquals(validationMessage, "", "Output Response Validation is failing for fileName:"+fileName+
					" for saas app type:"+saasType);


		} finally {
			dciFunctions.cleanupFileFromTempFolder(fileName);
		}
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

			String validationMessage = dciFunctions.validationRiskLogsWithCIQ(hits, fileName, suiteData.getUsername(),
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

			JSONArray jArray = (JSONArray) new JSONTokener(hits).nextValue();
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

	@DataProvider(name = "dataUploadContentOnlyCIQ")
	public Object[][] dataUploadContentOnlyCIQ() {

		dciFunctions = new DCIFunctions();

		String ciqProfileName = "DCI_Only_Content_Name";String ciqProfileDescription = "DCI Only Content Description";
		String ciqType = "OnlyContent";

		Map<String,String> ciq = new HashMap<String,String>();
		ciq.put("ciqProfileName", ciqProfileName);ciq.put("ciqProfileDescription", ciqProfileDescription);
		ciq.put("ciqType", ciqType);

		try {
			Logger.info("Creating CIQ profile with only content in progress");
			List<String> valuesPreDefDict=null;List<String> valuesPreDefTerms=null;
			List<String> valuesCustomDict=null;List<String> valuesCustomTerms=null;
			List<String> valuesRiskTypes=null;List<String> valuesFileFormat=null;List<String> valuesMLProfile=null;
			List<String> valuesContentTypes=Arrays.asList(
					"business","computing","cryptographic_keys","design doc",
					"encryption","engineering","health","legal","source_code"
					);
			dciFunctions.createCIQProfile(suiteData, restClient, ciqProfileName, ciqProfileDescription, valuesPreDefDict, valuesPreDefTerms,
					valuesCustomDict, valuesCustomTerms, valuesRiskTypes, valuesContentTypes, valuesFileFormat,valuesMLProfile, "high", 0, true, 1, false);
			Logger.info("Creating CIQ profile with only content is completed");
			dciFunctions.waitForSeconds(10);

		} catch (Exception ex) {
			Logger.info("Issue with Create Content Iq Profiles" + ex.getLocalizedMessage());
		}



		String[] fileName = dciFunctions.getFileName(DCIConstants.DCI_FILE_UPLOAD_CIQ_CONTENT_ONLY_PATH);	
		String[] risks = dciFunctions.riskTypesForAFile(fileName);

		fileName = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_CIQ_CONTENT_ONLY_PATH,
				fileName);
		dciFunctions.fileUploadOrSendEmail(suiteData, folderInfo, fileName);

		Object[][] result = new Object[fileName.length][];
		for (int i = 0; i < fileName.length; i++) {
			boolean flag=false;if(fileName[i].contains("source_code")){flag=true;}
			result[i] = new Object[] { new TestParameters("Log Generation/Validation for content file types:"+fileName[i]+" for risks "+risks[i], 
					"Create CIQ profile with only content:"+ciqProfileName+".Upload content file:"+fileName[i]+ 
					". Then verify classification logs are getting generated within the SLA provided"+" for content "+Arrays.asList(risks[i].split(",")),
					fileName[i], SaasType.getSaasFilterType(suiteData.getSaasApp()), Arrays.asList(risks[i].split(",")), ciq, flag)};
		}

		mainCounter = 0;testCounter = DCIConstants.DCI_COUNTER_MAX;

		return result;
	}

	@DataProvider(name = "dataUploadContentSingleCIQ")
	public Object[][] dataUploadContentSingleCIQ() {

		dciFunctions = new DCIFunctions();

		String[] fileName = dciFunctions.getFileName(DCIConstants.DCI_FILE_UPLOAD_CIQ_CONTENT_ONLY_PATH);	
		String[] risks = dciFunctions.riskTypesForAFile(fileName);

		String[] riskValues = {
				"business","computing","cryptographic_keys","design doc",
				"encryption","engineering","health","legal","source_code"
		};
		String[] ciqProfileName = new String[riskValues.length];String[] ciqProfileDescription = new String[riskValues.length];
		String[] ciqType = new String[riskValues.length];

		for (int i = 0; i < riskValues.length; i++) {	
			try {
				ciqProfileName[i]=riskValues[i]+"_Only_Content";ciqProfileDescription[i]=riskValues[i]+" Only Content Description";
				ciqType[i]="OnlyRisks";

				Logger.info("Creating CIQ profile with only content in progress");
				List<String> valuesPreDefDict=null;List<String> valuesPreDefTerms=null;
				List<String> valuesCustomDict=null;List<String> valuesCustomTerms=null;
				List<String> valuesRiskTypes=null;List<String> valuesFileFormat=null;List<String> valuesMLProfile=null;
				List<String> valuesContentTypes=Arrays.asList(riskValues[i]);

				dciFunctions.createCIQProfile(suiteData, restClient, ciqProfileName[i], ciqProfileDescription[i], valuesPreDefDict, valuesPreDefTerms,
						valuesCustomDict, valuesCustomTerms, valuesRiskTypes, valuesContentTypes, valuesFileFormat,valuesMLProfile, "high", 0, true, 1, false);

				Logger.info("Creating CIQ profile with only content is completed");


			} catch (Exception ex) {
				Logger.info("Issue with Create Content Iq Profiles" + ex.getLocalizedMessage());
			}
		}

		dciFunctions.waitForSeconds(10);

		fileName = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_CIQ_CONTENT_ONLY_PATH,
				fileName);
		dciFunctions.fileUploadOrSendEmail(suiteData, folderInfo, fileName);

		Object[][] result = new Object[fileName.length][];
		for (int i = 0; i < fileName.length; i++) {
			Map<String,String> ciq = new HashMap<String,String>();
			ciq.put("ciqProfileName", ciqProfileName[i]);ciq.put("ciqProfileDescription", ciqProfileDescription[i]);
			ciq.put("ciqType", ciqType[i]);


			boolean flag=false;if(fileName[i].contains("source_code")){flag=true;}
			result[i] = new Object[] { new TestParameters("Risk Generation/Validation for risks file types:"+fileName[i]+" for risks "+risks[i], 
					"Create CIQ profile with only risks:"+ciqProfileName[i]+".Upload  risks file:"+fileName[i]+ ". Then verify risk logs are getting generated within the SLA provided"+" for risks "+Arrays.asList(risks[i].split(",")),
					fileName[i], SaasType.getSaasFilterType(suiteData.getSaasApp()), Arrays.asList(risks[i].split(",")), ciq, flag)};
		}

		mainCounter = 0;testCounter = DCIConstants.DCI_COUNTER_MAX;

		return result;
	}



	@DataProvider(name = "dataUploadContentPredefDict")
	public Object[][] dataUploadContentPredefDict() {
		dciFunctions = new DCIFunctions();



		String ciqProfileName = "DCI_Predef_Dictionary_Content_Name";
		String ciqProfileDescription = "DCI Predefined Dictionaries With Content Description";
		String ciqProfileType = "Illegal Drugs";String ciqProfileCount = "1";String ciqType = "PDD";
		String ciqProfileKeyword = "anadrol";

		Map<String,String> ciq = new HashMap<String,String>();
		ciq.put("ciqProfileName", ciqProfileName);ciq.put("ciqProfileDescription", ciqProfileDescription);
		ciq.put("ciqProfileType", ciqProfileType);ciq.put("ciqProfileCount", ciqProfileCount);
		ciq.put("ciqType", ciqType);ciq.put("ciqProfileKeyword", ciqProfileKeyword);

		try {
			Logger.info("Creating CIQ profile with predefined dictionaries and risks in progress");
			List<String> valuesPreDefDict=Arrays.asList(ciqProfileType);List<String> valuesPreDefTerms=null;
			List<String> valuesCustomDict=null;List<String> valuesCustomTerms=null;
			List<String> valuesRiskTypes=null;List<String> valuesFileFormat=null;List<String> valuesMLProfile=null;
			List<String> valuesContentTypes=Arrays.asList(
					"business","computing","cryptographic_keys","design doc",
					"encryption","engineering","health","legal","source_code"
					);
			dciFunctions.createCIQProfile(suiteData, restClient, ciqProfileName, ciqProfileDescription, valuesPreDefDict, valuesPreDefTerms,
					valuesCustomDict, valuesCustomTerms, valuesRiskTypes, valuesContentTypes, valuesFileFormat,valuesMLProfile, "high", 1, true, 1, false);

			Logger.info("Creating CIQ profile with predefined dictionaries and risks is completed");
			dciFunctions.waitForSeconds(10);

		} catch (Exception ex) {
			Logger.info("Issue with Create Content Iq Profiles" + ex.getLocalizedMessage());
		}



		String[] fileName = dciFunctions.getFileName(DCIConstants.DCI_FILE_UPLOAD_CIQ_PREDEF_DICT_CONTENT_PATH);	
		String[] risks = dciFunctions.riskTypesForAFile(fileName);

		fileName = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_CIQ_PREDEF_DICT_CONTENT_PATH,
				fileName);
		dciFunctions.fileUploadOrSendEmail(suiteData, folderInfo, fileName);

		Object[][] result = new Object[fileName.length][];
		for (int i = 0; i < fileName.length; i++) {
			boolean flag=false;if(fileName[i].contains("source_code_illegal_drugs")){flag=true;}
			result[i] = new Object[] { new TestParameters("Risk Generation/Validation for risks file types:"+fileName[i]+" for risks "+risks[i], 
					"Create CIQ profile with risks and PDDs:"+ciqProfileName+".Upload  risks file:"+fileName[i]+ ". Then verify risk logs are getting generated within the SLA provided"+" for risks "+Arrays.asList(risks[i].split(",")),
					fileName[i], SaasType.getSaasFilterType(suiteData.getSaasApp()), Arrays.asList(risks[i].split(",")),
					ciq, flag)};
		}

		mainCounter = 0;testCounter = DCIConstants.DCI_COUNTER_MAX;

		return result;
	}



	@DataProvider(name = "dataUploadContentPredefTerms")
	public Object[][] dataUploadContentPredefTerms() {
		dciFunctions = new DCIFunctions();



		String ciqProfileName = "DCI_Predef_Terms_Content_Name";
		String ciqProfileDescription = "DCI Predefined Terms With Content Description";
		String ciqProfileType = "France National ID Card (CNI)";String ciqProfileCount = "1";String ciqType = "PDT";

		Map<String,String> ciq = new HashMap<String,String>();
		ciq.put("ciqProfileName", ciqProfileName);ciq.put("ciqProfileDescription", ciqProfileDescription);
		ciq.put("ciqProfileType", ciqProfileType);ciq.put("ciqProfileCount", ciqProfileCount);
		ciq.put("ciqType", ciqType);

		try {
			Logger.info("Creating CIQ profile with predefined terms and risks in progress");
			List<String> valuesPreDefDict=null;List<String> valuesPreDefTerms=Arrays.asList(ciqProfileType);
			List<String> valuesCustomDict=null;List<String> valuesCustomTerms=null;
			List<String> valuesRiskTypes=null;List<String> valuesFileFormat=null;List<String> valuesMLProfile=null;
			List<String> valuesContentTypes=Arrays.asList(
					"business","computing","cryptographic_keys","design doc",
					"encryption","engineering","health","legal","source_code"
					);

			dciFunctions.createCIQProfile(suiteData, restClient, ciqProfileName, ciqProfileDescription, valuesPreDefDict, valuesPreDefTerms,
					valuesCustomDict, valuesCustomTerms, valuesRiskTypes, valuesContentTypes, valuesFileFormat,valuesMLProfile, "high", 1, true, 1, false);

			Logger.info("Creating CIQ profile with predefined terms and risks is completed");
			dciFunctions.waitForSeconds(10);

		} catch (Exception ex) {
			Logger.info("Issue with Create Content Iq Profiles" + ex.getLocalizedMessage());
		}



		String[] fileName = dciFunctions.getFileName(DCIConstants.DCI_FILE_UPLOAD_CIQ_PREDEF_TERMS_CONTENT_PATH);	
		String[] risks = dciFunctions.riskTypesForAFile(fileName);

		fileName = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_CIQ_PREDEF_TERMS_CONTENT_PATH,
				fileName);
		dciFunctions.fileUploadOrSendEmail(suiteData, folderInfo, fileName);

		Object[][] result = new Object[fileName.length][];
		for (int i = 0; i < fileName.length; i++) {
			boolean flag=false;if(fileName[i].contains("source_code")){flag=true;}
			result[i] = new Object[] { new TestParameters("Risk Generation/Validation for risks file types:"+fileName[i]+" for risks "+risks[i], 
					"Create CIQ profile with risks and PDTs:"+ciqProfileName+".Upload  risks file:"+fileName[i]+ ". Then verify risk logs are getting generated within the SLA provided"+" for risks "+Arrays.asList(risks[i].split(",")),
					fileName[i], SaasType.getSaasFilterType(suiteData.getSaasApp()), Arrays.asList(risks[i].split(",")),
					ciq, flag)};
		}

		mainCounter = 0;testCounter = DCIConstants.DCI_COUNTER_MAX;

		return result;
	}

	@DataProvider(name = "dataUploadContentCustomTerms")
	public Object[][] dataUploadContentCustomTerms() {
		dciFunctions = new DCIFunctions();

		String ciqProfileName = "DCI_Custom_Terms_Content_Name";
		String ciqProfileDescription = "DCI Custom Terms With Content Description";
		String ciqProfileType = "DCI_CUSTOM_TERMS";String ciqProfileCount = "1";String ciqType = "CT";

		Map<String,String> ciq = new HashMap<String,String>();
		ciq.put("ciqProfileName", ciqProfileName);ciq.put("ciqProfileDescription", ciqProfileDescription);
		ciq.put("ciqProfileType", ciqProfileType);ciq.put("ciqProfileCount", ciqProfileCount);
		ciq.put("ciqType", ciqType);

		try {
			Logger.info("Creating CIQ profile with custom terms and content in progress");
			List<String> valuesPreDefDict=null;List<String> valuesPreDefTerms=null;
			List<String> valuesCustomDict=null;List<String> valuesCustomTerms=Arrays.asList(ciqProfileType);
			List<String> valuesRiskTypes=null;List<String> valuesFileFormat=null;List<String> valuesMLProfile=null;
			List<String> valuesContentTypes=Arrays.asList(
					"business","computing","cryptographic_keys","design doc",
					"encryption","engineering","health","legal","source_code"
					);

			dciFunctions.createCIQProfile(suiteData, restClient, ciqProfileName, ciqProfileDescription, valuesPreDefDict, valuesPreDefTerms,
					valuesCustomDict, valuesCustomTerms, valuesRiskTypes, valuesContentTypes, valuesFileFormat,valuesMLProfile, "high", 1, true, 1, false);

			Logger.info("Creating CIQ profile with custom terms and content is completed");
			dciFunctions.waitForSeconds(10);

		} catch (Exception ex) {
			Logger.info("Issue with Create Content Iq Profiles" + ex.getLocalizedMessage());
		}



		String[] fileName = dciFunctions.getFileName(DCIConstants.DCI_FILE_UPLOAD_CIQ_CUSTOM_TERMS_CONTENT_PATH);	
		String[] risks = dciFunctions.riskTypesForAFile(fileName);

		fileName = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_CIQ_CUSTOM_TERMS_CONTENT_PATH,
				fileName);
		dciFunctions.fileUploadOrSendEmail(suiteData, folderInfo, fileName);

		Object[][] result = new Object[fileName.length][];
		for (int i = 0; i < fileName.length; i++) {
			boolean flag=false;if(fileName[i].contains("source_code")){flag=true;}
			result[i] = new Object[] { new TestParameters("Risk Generation/Validation for risks file types:"+fileName[i]+" for risks "+risks[i], 
					"Create CIQ profile with risks and CTs:"+ciqProfileName+".Upload  risks file:"+fileName[i]+ ". Then verify risk logs are getting generated within the SLA provided"+" for risks "+Arrays.asList(risks[i].split(",")),
					fileName[i], SaasType.getSaasFilterType(suiteData.getSaasApp()), Arrays.asList(risks[i].split(",")),
					ciq, flag)};
		}

		mainCounter = 0;testCounter = DCIConstants.DCI_COUNTER_MAX;

		return result;
	}

	@DataProvider(name = "dataUploadContentCustomDict")
	public Object[][] dataUploadContentCustomDict() {
		dciFunctions = new DCIFunctions();

		List<NameValuePair> headers = dciFunctions.getBrowserHeaders(suiteData);

		String ciqProfileName = "DCI_Custom_Dictionary_Content_Name";
		String ciqProfileDescription = "DCI Custom Dictionaries With Content Description";
		String ciqProfileType = "DCI_CUSTOM_TERMS";String ciqProfileCount = "1";String ciqType = "CD";
		String ciqProfileKeyword = "dci_custom_terms";

		Map<String,String> ciq = new HashMap<String,String>();
		ciq.put("ciqProfileName", ciqProfileName);ciq.put("ciqProfileDescription", ciqProfileDescription);
		ciq.put("ciqProfileType", ciqProfileType);ciq.put("ciqProfileCount", ciqProfileCount);
		ciq.put("ciqType", ciqType);ciq.put("ciqProfileKeyword", ciqProfileKeyword);

		try {

			List<String> keywords= new ArrayList<String>();
			keywords.add(ciqProfileType);
			dciFunctions.createDictionary(restClient, suiteData, ciqProfileType, "DCI Description", null, keywords, headers);


			Logger.info("Creating CIQ profile with custom dictionaries and content in progress");
			List<String> valuesPreDefDict=null;List<String> valuesPreDefTerms=null;
			List<String> valuesCustomDict=Arrays.asList(ciqProfileType);List<String> valuesCustomTerms=null;
			List<String> valuesRiskTypes=null;List<String> valuesFileFormat=null;List<String> valuesMLProfile=null;
			List<String> valuesContentTypes=Arrays.asList(
					"business","computing","cryptographic_keys","design doc",
					"encryption","engineering","health","legal","source_code"
					);

			dciFunctions.createCIQProfile(suiteData, restClient, ciqProfileName, ciqProfileDescription, valuesPreDefDict, valuesPreDefTerms,
					valuesCustomDict, valuesCustomTerms, valuesRiskTypes, valuesContentTypes, valuesFileFormat,valuesMLProfile, "high", 1, true, 1, false);

			Logger.info("Creating CIQ profile with custom dictionaries and content is completed");
			dciFunctions.waitForSeconds(10);

		} catch (Exception ex) {
			Logger.info("Issue with Create Content Iq Profiles" + ex.getLocalizedMessage());
		}



		String[] fileName = dciFunctions.getFileName(DCIConstants.DCI_FILE_UPLOAD_CIQ_CUSTOM_DICT_CONTENT_PATH);	
		String[] risks = dciFunctions.riskTypesForAFile(fileName);

		fileName = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_CIQ_CUSTOM_DICT_CONTENT_PATH,
				fileName);
		dciFunctions.fileUploadOrSendEmail(suiteData, folderInfo, fileName);

		Object[][] result = new Object[fileName.length][];
		for (int i = 0; i < fileName.length; i++) {
			boolean flag=false;if(fileName[i].contains("source_code")){flag=true;}
			result[i] = new Object[] { new TestParameters("Risk Generation/Validation for risks file types:"+fileName[i]+" for risks "+risks[i], 
					"Create CIQ profile with risks and CDs:"+ciqProfileName+".Upload  risks file:"+fileName[i]+ ". Then verify risk logs are getting generated within the SLA provided"+" for risks "+Arrays.asList(risks[i].split(",")),
					fileName[i], SaasType.getSaasFilterType(suiteData.getSaasApp()), Arrays.asList(risks[i].split(",")),
					ciq, flag)};
		}

		mainCounter = 0;testCounter = DCIConstants.DCI_COUNTER_MAX;

		return result;
	}

	@DataProvider(name = "dataUploadContentNegPredefDict")
	public Object[][] dataUploadContentNegPredefDict() {
		dciFunctions = new DCIFunctions();



		String ciqProfileName = "DCI_Predef_Dictionary_Content_Name";
		String ciqProfileDescription = "DCI Predefined Dictionaries With Content Description";
		String ciqProfileType = "Illegal Drugs";String ciqProfileCount = "1";String ciqType = "PDD";
		String ciqProfileKeyword = "anadrol";

		Map<String,String> ciq = new HashMap<String,String>();
		ciq.put("ciqProfileName", ciqProfileName);ciq.put("ciqProfileDescription", ciqProfileDescription);
		ciq.put("ciqProfileType", ciqProfileType);ciq.put("ciqProfileCount", ciqProfileCount);
		ciq.put("ciqType", ciqType);ciq.put("ciqProfileKeyword", ciqProfileKeyword);

		try {
			Logger.info("Creating CIQ profile with predefined dictionaries and content in progress");
			List<String> valuesPreDefDict=Arrays.asList(ciqProfileType);List<String> valuesPreDefTerms=null;
			List<String> valuesCustomDict=null;List<String> valuesCustomTerms=null;
			List<String> valuesRiskTypes=null;List<String> valuesFileFormat=null;List<String> valuesMLProfile=null;
			List<String> valuesContentTypes=Arrays.asList(
					"business","computing","cryptographic_keys","design doc",
					"encryption","engineering","health","legal","source_code"
					);

			dciFunctions.createCIQProfile(suiteData, restClient, ciqProfileName, ciqProfileDescription, valuesPreDefDict, valuesPreDefTerms,
					valuesCustomDict, valuesCustomTerms, valuesRiskTypes, valuesContentTypes, valuesFileFormat,valuesMLProfile, "high", 1, true, 1, false);

			Logger.info("Creating CIQ profile with predefined dictionaries and content is completed");
			dciFunctions.waitForSeconds(10);

		} catch (Exception ex) {
			Logger.info("Issue with Create Content Iq Profiles" + ex.getLocalizedMessage());
		}



		String[] fileName = dciFunctions.getFileName(DCIConstants.DCI_FILE_UPLOAD_CIQ_CONTENT_PATH);	
		String[] risks = dciFunctions.riskTypesForAFile(fileName);

		fileName = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_CIQ_CONTENT_PATH,
				fileName);
		dciFunctions.fileUploadOrSendEmail(suiteData, folderInfo, fileName);

		Object[][] result = new Object[fileName.length][];
		for (int i = 0; i < fileName.length; i++) {
			boolean flag=false;if(fileName[i].contains("source_code")){flag=true;}
			result[i] = new Object[] { new TestParameters("Risk Generation/Validation for risks file types:"+fileName[i]+" for risks "+risks[i], 
					"Create CIQ profile with risks and PDDs:"+ciqProfileName+".Upload  risks file:"+fileName[i]+ ". Then verify risk logs are getting generated within the SLA provided"+" for risks "+Arrays.asList(risks[i].split(",")),
					fileName[i], SaasType.getSaasFilterType(suiteData.getSaasApp()), Arrays.asList(risks[i].split(",")),
					ciq, flag)};
		}

		mainCounter = 0;testCounter = DCIConstants.DCI_COUNTER_MAX;

		return result;
	}



	@DataProvider(name = "dataUploadContentNegPredefTerms")
	public Object[][] dataUploadContentNegPredefTerms() {
		dciFunctions = new DCIFunctions();



		String ciqProfileName = "DCI_Predef_Terms_Content_Name";
		String ciqProfileDescription = "DCI Predefined Terms With Content Description";
		String ciqProfileType = "France National ID Card (CNI)";String ciqProfileCount = "1";String ciqType = "PDT";

		Map<String,String> ciq = new HashMap<String,String>();
		ciq.put("ciqProfileName", ciqProfileName);ciq.put("ciqProfileDescription", ciqProfileDescription);
		ciq.put("ciqProfileType", ciqProfileType);ciq.put("ciqProfileCount", ciqProfileCount);
		ciq.put("ciqType", ciqType);

		try {
			Logger.info("Creating CIQ profile with predefined terms and content in progress");
			List<String> valuesPreDefDict=null;List<String> valuesPreDefTerms=Arrays.asList(ciqProfileType);
			List<String> valuesCustomDict=null;List<String> valuesCustomTerms=null;
			List<String> valuesRiskTypes=null;List<String> valuesFileFormat=null;List<String> valuesMLProfile=null;
			List<String> valuesContentTypes=Arrays.asList(
					"business","computing","cryptographic_keys","design doc",
					"encryption","engineering","health","legal","source_code"
					);

			dciFunctions.createCIQProfile(suiteData, restClient, ciqProfileName, ciqProfileDescription, valuesPreDefDict, valuesPreDefTerms,
					valuesCustomDict, valuesCustomTerms, valuesRiskTypes, valuesContentTypes, valuesFileFormat,valuesMLProfile, "high", 1, true, 1, false);

			Logger.info("Creating CIQ profile with predefined terms and content is completed");
			dciFunctions.waitForSeconds(10);

		} catch (Exception ex) {
			Logger.info("Issue with Create Content Iq Profiles" + ex.getLocalizedMessage());
		}



		String[] fileName = dciFunctions.getFileName(DCIConstants.DCI_FILE_UPLOAD_CIQ_CONTENT_PATH);	
		String[] risks = dciFunctions.riskTypesForAFile(fileName);

		fileName = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_CIQ_CONTENT_PATH,
				fileName);
		dciFunctions.fileUploadOrSendEmail(suiteData, folderInfo, fileName);

		Object[][] result = new Object[fileName.length][];
		for (int i = 0; i < fileName.length; i++) {
			boolean flag=false;if(fileName[i].contains("source_code")){flag=true;}
			result[i] = new Object[] { new TestParameters("Risk Generation/Validation for risks file types:"+fileName[i]+" for risks "+risks[i], 
					"Create CIQ profile with risks and PDTs:"+ciqProfileName+".Upload  risks file:"+fileName[i]+ ". Then verify risk logs are getting generated within the SLA provided"+" for risks "+Arrays.asList(risks[i].split(",")),
					fileName[i], SaasType.getSaasFilterType(suiteData.getSaasApp()), Arrays.asList(risks[i].split(",")),
					ciq, flag)};
		}

		mainCounter = 0;testCounter = DCIConstants.DCI_COUNTER_MAX;

		return result;
	}

	@DataProvider(name = "dataUploadContentNegCustomTerms")
	public Object[][] dataUploadContentNegCustomTerms() {
		dciFunctions = new DCIFunctions();

		String ciqProfileName = "DCI_Custom_Terms_Content_Name";
		String ciqProfileDescription = "DCI Custom Terms With Content Description";
		String ciqProfileType = "DCI_CUSTOM_TERMS";String ciqProfileCount = "1";String ciqType = "CT";

		Map<String,String> ciq = new HashMap<String,String>();
		ciq.put("ciqProfileName", ciqProfileName);ciq.put("ciqProfileDescription", ciqProfileDescription);
		ciq.put("ciqProfileType", ciqProfileType);ciq.put("ciqProfileCount", ciqProfileCount);
		ciq.put("ciqType", ciqType);

		try {
			Logger.info("Creating CIQ profile with custom terms and content in progress");
			List<String> valuesPreDefDict=null;List<String> valuesPreDefTerms=null;
			List<String> valuesCustomDict=null;List<String> valuesCustomTerms=Arrays.asList(ciqProfileType);
			List<String> valuesRiskTypes=null;List<String> valuesFileFormat=null;List<String> valuesMLProfile=null;
			List<String> valuesContentTypes=Arrays.asList(
					"business","computing","cryptographic_keys","design doc",
					"encryption","engineering","health","legal","source_code"
					);

			dciFunctions.createCIQProfile(suiteData, restClient, ciqProfileName, ciqProfileDescription, valuesPreDefDict, valuesPreDefTerms,
					valuesCustomDict, valuesCustomTerms, valuesRiskTypes, valuesContentTypes, valuesFileFormat,valuesMLProfile, "high", 1, true, 1, false);

			Logger.info("Creating CIQ profile with custom terms and content is completed");
			dciFunctions.waitForSeconds(10);

		} catch (Exception ex) {
			Logger.info("Issue with Create Content Iq Profiles" + ex.getLocalizedMessage());
		}



		String[] fileName = dciFunctions.getFileName(DCIConstants.DCI_FILE_UPLOAD_CIQ_CONTENT_PATH);	
		String[] risks = dciFunctions.riskTypesForAFile(fileName);

		fileName = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_CIQ_CONTENT_PATH,
				fileName);
		dciFunctions.fileUploadOrSendEmail(suiteData, folderInfo, fileName);

		Object[][] result = new Object[fileName.length][];
		for (int i = 0; i < fileName.length; i++) {
			boolean flag=false;if(fileName[i].contains("source_code")){flag=true;}
			result[i] = new Object[] { new TestParameters("Risk Generation/Validation for risks file types:"+fileName[i]+" for risks "+risks[i], 
					"Create CIQ profile with risks and CTs:"+ciqProfileName+".Upload  risks file:"+fileName[i]+ ". Then verify risk logs are getting generated within the SLA provided"+" for risks "+Arrays.asList(risks[i].split(",")),
					fileName[i], SaasType.getSaasFilterType(suiteData.getSaasApp()), Arrays.asList(risks[i].split(",")),
					ciq, flag)};
		}

		mainCounter = 0;testCounter = DCIConstants.DCI_COUNTER_MAX;

		return result;
	}

	@DataProvider(name = "dataUploadContentNegCustomDict")
	public Object[][] dataUploadContentNegCustomDict() {
		dciFunctions = new DCIFunctions();

		List<NameValuePair> headers = dciFunctions.getBrowserHeaders(suiteData);

		String ciqProfileName = "DCI_Custom_Dictionary_Content_Name";
		String ciqProfileDescription = "DCI Custom Dictionaries With Content Description";
		String ciqProfileType = "DCI_CUSTOM_TERMS";String ciqProfileCount = "1";String ciqType = "CD";
		String ciqProfileKeyword = "dci_custom_terms";

		Map<String,String> ciq = new HashMap<String,String>();
		ciq.put("ciqProfileName", ciqProfileName);ciq.put("ciqProfileDescription", ciqProfileDescription);
		ciq.put("ciqProfileType", ciqProfileType);ciq.put("ciqProfileCount", ciqProfileCount);
		ciq.put("ciqType", ciqType);ciq.put("ciqProfileKeyword", ciqProfileKeyword);

		try {

			List<String> keywords= new ArrayList<String>();
			keywords.add(ciqProfileType);
			dciFunctions.createDictionary(restClient, suiteData, ciqProfileType, "DCI Description", null, keywords, headers);


			Logger.info("Creating CIQ profile with custom dictionaries and content in progress");
			List<String> valuesPreDefDict=null;List<String> valuesPreDefTerms=null;
			List<String> valuesCustomDict=Arrays.asList(ciqProfileType);List<String> valuesCustomTerms=null;
			List<String> valuesRiskTypes=null;List<String> valuesFileFormat=null;List<String> valuesMLProfile=null;
			List<String> valuesContentTypes=Arrays.asList(
					"business","computing","cryptographic_keys","design doc",
					"encryption","engineering","health","legal","source_code"
					);

			dciFunctions.createCIQProfile(suiteData, restClient, ciqProfileName, ciqProfileDescription, valuesPreDefDict, valuesPreDefTerms,
					valuesCustomDict, valuesCustomTerms, valuesRiskTypes, valuesContentTypes, valuesFileFormat,valuesMLProfile, "high", 1, true, 1, false);

			Logger.info("Creating CIQ profile with custom dictionaries and content is completed");
			dciFunctions.waitForSeconds(10);

		} catch (Exception ex) {
			Logger.info("Issue with Create Content Iq Profiles" + ex.getLocalizedMessage());
		}



		String[] fileName = dciFunctions.getFileName(DCIConstants.DCI_FILE_UPLOAD_CIQ_CONTENT_PATH);	
		String[] risks = dciFunctions.riskTypesForAFile(fileName);


		fileName = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_CIQ_CONTENT_PATH,
				fileName);
		dciFunctions.fileUploadOrSendEmail(suiteData, folderInfo, fileName);

		Object[][] result = new Object[fileName.length][];
		for (int i = 0; i < fileName.length; i++) {
			boolean flag=false;if(fileName[i].contains("source_code")){flag=true;}
			result[i] = new Object[] { new TestParameters("Risk Generation/Validation for risks file types:"+fileName[i]+" for risks "+risks[i], 
					"Create CIQ profile with risks and CDs:"+ciqProfileName+".Upload  risks file:"+fileName[i]+ ". Then verify risk logs are getting generated within the SLA provided"+" for risks "+Arrays.asList(risks[i].split(",")),
					fileName[i], SaasType.getSaasFilterType(suiteData.getSaasApp()), Arrays.asList(risks[i].split(",")),
					ciq, flag)};
		}

		mainCounter = 0;testCounter = DCIConstants.DCI_COUNTER_MAX;

		return result;
	}


	@DataProvider(name = "dataUploadCIQContentNegative")
	public Object[][] dataUploadCIQContentNegative() {

		dciFunctions = new DCIFunctions();


		String ciqProfileName = "DCI_Predef_Dictionary_Content_Name";
		String ciqProfileDescription = "DCI Predefined Dictionaries With Content Description";
		String ciqProfileType = "Illegal Drugs";

		try {
			Logger.info("Creating CIQ profile with predefined dictionaries and content in progress");
			List<String> valuesPreDefDict=Arrays.asList(ciqProfileType);List<String> valuesPreDefTerms=null;
			List<String> valuesCustomDict=null;List<String> valuesCustomTerms=null;
			List<String> valuesRiskTypes=null;List<String> valuesFileFormat=null;List<String> valuesMLProfile=null;
			List<String> valuesContentTypes=Arrays.asList(
					"business","computing","cryptographic_keys","design doc",
					"encryption","engineering","health","legal","source_code"
					);

			dciFunctions.createCIQProfile(suiteData, restClient, ciqProfileName, ciqProfileDescription, valuesPreDefDict, valuesPreDefTerms,
					valuesCustomDict, valuesCustomTerms, valuesRiskTypes, valuesContentTypes, valuesFileFormat,valuesMLProfile, "high", 1, true, 1, false);

			Logger.info("Creating CIQ profile with predefined dictionaries and content is completed");
			dciFunctions.waitForSeconds(10);

		} catch (Exception ex) {
			Logger.info("Issue with Create Content Iq Profiles" + ex.getLocalizedMessage());
		}

		ciqProfileName = "DCI_Predef_Terms_Content_Name";
		ciqProfileDescription = "DCI Predefined Terms With Content Description";
		ciqProfileType = "France National ID Card (CNI)";

		try {
			Logger.info("Creating CIQ profile with predefined terms and content in progress");
			List<String> valuesPreDefDict=null;List<String> valuesPreDefTerms=Arrays.asList(ciqProfileType);
			List<String> valuesCustomDict=null;List<String> valuesCustomTerms=null;
			List<String> valuesRiskTypes=null;List<String> valuesFileFormat=null;List<String> valuesMLProfile=null;
			List<String> valuesContentTypes=Arrays.asList(
					"business","computing","cryptographic_keys","design doc",
					"encryption","engineering","health","legal","source_code"
					);
			dciFunctions.createCIQProfile(suiteData, restClient, ciqProfileName, ciqProfileDescription, valuesPreDefDict, valuesPreDefTerms,
					valuesCustomDict, valuesCustomTerms, valuesRiskTypes, valuesContentTypes, valuesFileFormat,valuesMLProfile, "high", 1, true, 1, false);

			Logger.info("Creating CIQ profile with predefined terms and content is completed");
			dciFunctions.waitForSeconds(10);

		} catch (Exception ex) {
			Logger.info("Issue with Create Content Iq Profiles" + ex.getLocalizedMessage());
		}

		ciqProfileName = "DCI_Custom_Terms_Content_Name";
		ciqProfileDescription = "DCI Custom Terms With Content Description";
		ciqProfileType = "DCI_CUSTOM_TERMS";

		try {
			Logger.info("Creating CIQ profile with custom terms and content in progress");
			List<String> valuesPreDefDict=null;List<String> valuesPreDefTerms=null;
			List<String> valuesCustomDict=null;List<String> valuesCustomTerms=Arrays.asList(ciqProfileType);
			List<String> valuesRiskTypes=null;List<String> valuesFileFormat=null;List<String> valuesMLProfile=null;
			List<String> valuesContentTypes=Arrays.asList(
					"business","computing","cryptographic_keys","design doc",
					"encryption","engineering","health","legal","source_code"
					);
			dciFunctions.createCIQProfile(suiteData, restClient, ciqProfileName, ciqProfileDescription, valuesPreDefDict, valuesPreDefTerms,
					valuesCustomDict, valuesCustomTerms, valuesRiskTypes, valuesContentTypes, valuesFileFormat,valuesMLProfile, "high", 1, true, 1, false);

			Logger.info("Creating CIQ profile with custom terms and content is completed");
			dciFunctions.waitForSeconds(10);

		} catch (Exception ex) {
			Logger.info("Issue with Create Content Iq Profiles" + ex.getLocalizedMessage());
		}


		List<NameValuePair> headers = dciFunctions.getBrowserHeaders(suiteData);

		ciqProfileName = "DCI_Custom_Dictionary_Content_Name";
		ciqProfileDescription = "DCI Custom Dictionaries With Content Description";
		ciqProfileType = "DCI_CUSTOM_TERMS";

		try {

			List<String> keywords= new ArrayList<String>();
			keywords.add(ciqProfileType);
			dciFunctions.createDictionary(restClient, suiteData, ciqProfileType, "DCI Description", null, keywords, headers);

			Logger.info("Creating CIQ profile with custom dictionaries and content in progress");
			List<String> valuesPreDefDict=null;List<String> valuesPreDefTerms=null;
			List<String> valuesCustomDict=Arrays.asList(ciqProfileType);List<String> valuesCustomTerms=null;
			List<String> valuesRiskTypes=null;List<String> valuesFileFormat=null;List<String> valuesMLProfile=null;
			List<String> valuesContentTypes=Arrays.asList(
					"business","computing","cryptographic_keys","design doc",
					"encryption","engineering","health","legal","source_code"
					);
			dciFunctions.createCIQProfile(suiteData, restClient, ciqProfileName, ciqProfileDescription, valuesPreDefDict, valuesPreDefTerms,
					valuesCustomDict, valuesCustomTerms, valuesRiskTypes, valuesContentTypes, valuesFileFormat,valuesMLProfile, "high", 1, true, 1, false);

			Logger.info("Creating CIQ profile with custom dictionaries and content is completed");
			dciFunctions.waitForSeconds(10);

		} catch (Exception ex) {
			Logger.info("Issue with Create Content Iq Profiles" + ex.getLocalizedMessage());
		}

		ciqProfileName = "DCI_Only_Content_Name";ciqProfileDescription = "DCI Only Content Description";

		try {
			Logger.info("Creating CIQ profile with only content in progress");
			List<String> valuesPreDefDict=null;List<String> valuesPreDefTerms=null;
			List<String> valuesCustomDict=null;List<String> valuesCustomTerms=null;
			List<String> valuesRiskTypes=null;List<String> valuesMLProfile=null;
			List<String> valuesContentTypes=Arrays.asList(
					"business","computing","cryptographic_keys","design doc",
					"encryption","engineering","health","legal","source_code"
					);
			List<String> valuesFileFormat=null;
			dciFunctions.createCIQProfile(suiteData, restClient, ciqProfileName, ciqProfileDescription, valuesPreDefDict, valuesPreDefTerms,
					valuesCustomDict, valuesCustomTerms, valuesRiskTypes, valuesContentTypes, valuesFileFormat,valuesMLProfile, "high", 0, true, 1, false);
			Logger.info("Creating CIQ profile with only content is completed");
			dciFunctions.waitForSeconds(10);

		} catch (Exception ex) {
			Logger.info("Issue with Create Content Iq Profiles" + ex.getLocalizedMessage());
		}


		String[] fileName = dciFunctions.getFileName(DCIConstants.DCI_FILE_UPLOAD_CIQCONTENT_ONLY_PATH);	
		fileName = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_CIQCONTENT_ONLY_PATH,
				fileName);
		dciFunctions.fileUploadOrSendEmail(suiteData, folderInfo, fileName);

		Object[][] result = new Object[fileName.length][];
		for (int i = 0; i < fileName.length; i++) {
			boolean flag=false;if(fileName[i].contains("illegal_drugs.txt")){flag=true;}
			result[i] = new Object[] { new TestParameters("No Risk Generation/Validation for ciq file:"+fileName[i], 
					"Upload ciq file:"+fileName[i]+ ". Then verify risk logs are not getting generated within the SLA provided",
					fileName[i], SaasType.getSaasFilterType(suiteData.getSaasApp()), flag)};
		}

		dciFunctions.waitForMinutes(2);

		return result;
	}


	/**********************************************UTIL METHODS***********************************************/
	/**********************************************BEFORE/AFTER CLASS*****************************************/


	/**
	 * Create folders in saas apps
	 */
	@BeforeClass(groups = { "Risks","Content","RisksOnly","ContentOnly" })
	public void createFolder() {
		dciFunctions = new DCIFunctions();
		try {
			UserAccount account = dciFunctions.getUserAccount(suiteData);
			UniversalApi universalApi = dciFunctions.getUniversalApi(suiteData, account);

			folderInfo = dciFunctions.createFolder(universalApi, suiteData, DCIConstants.DCI_FOLDER + uniqueId);
		} catch (Exception ex) {
			Logger.info("Issue with Create Folder Operation " + ex.getLocalizedMessage());
		}
	}

	/**
	 * Delete folders in saas apps
	 */
	@AfterClass(groups = { "Risks","Content","RisksOnly","ContentOnly" })
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
	@BeforeClass(groups = { "Risks","Content","RisksOnly","ContentOnly" })
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
	@AfterClass(groups = { "Risks","Content","RisksOnly","ContentOnly"  })
	public void deleteContentIqProfileAfterTestEnds() {
		dciFunctions = new DCIFunctions();
		dciFunctions.deleteAllPolicies(restClient, suiteData);
		dciFunctions.deleteAllCIQProfiles(restClient, suiteData);
		dciFunctions.deleteAllDictionaries(restClient, suiteData);
		dciFunctions.deleteAllTrainingProfiles(restClient, suiteData);
	}

	@BeforeMethod(groups = { "Risks","Content","RisksOnly","ContentOnly"  })
	public void testData(Method method, Object[] testData) {
		String testCase = "";
		if (testData != null && testData.length > 0) {
			TestParameters testParams = null;
			// Check if test method has actually received required parameters
			for (Object testParameter : testData) {
				if (testParameter instanceof TestParameters) {
					testParams = (TestParameters) testParameter;
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
			s3.downloadWholeFolder(DCIConstants.DCI_S3_BUCKET, "DCI/CIQ/RiskTypes", 
					new File(DCIConstants.DCI_FILE_TEMP_PATH));
			s3.downloadWholeFolder(DCIConstants.DCI_S3_BUCKET, "DCI/CIQ/ContentTypes", 
					new File(DCIConstants.DCI_FILE_TEMP_PATH));
			s3.downloadWholeFolder(DCIConstants.DCI_S3_BUCKET, "DCI/CIQ/RiskContentTypes", 
					new File(DCIConstants.DCI_FILE_TEMP_PATH));

		} catch (Exception ex) {
			Logger.info("Downloading folder from S3 is failed with exception " + ex.getLocalizedMessage());
		}
	}

	/**********************************************
	 * BEFORE/AFTER METHODS/CLASS
	 *****************************************/

}