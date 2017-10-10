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
import com.universal.common.Office365MailActivities;
import com.universal.common.UniversalApi;
import com.universal.dtos.UserAccount;

import net.sf.json.JSONArray;
import net.sf.json.util.JSONTokener;

public class DCIContentIQProfileTests extends DCICommonTest implements ITest {

	protected String mTestCaseName = "";
	DCIFunctions dciFunctions = null;
	ElasticSearchLogs esLogs = null;
	Map<String,String> folderInfo = new HashMap<String,String>();
	String uniqueId = UUID.randomUUID().toString();
	String contentIQProfileIdDictId=null;String contentIQProfileIdTermId=null;
	int mainCounter = 0;int testCounter = 0;

	Office365MailActivities objMail = null;

	/**********************************************TEST METHODS***********************************************/


	@Test(dataProvider = "dataUploadPredefDict", groups ={"PredefDict","PredefDictAttachment","PredefDictBody"})
	public void testDisplayLogsForRiskGeneratedInPredefinedDictionaries(TestParameters testParams) throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();

		Logger.info(
				"************************************Starting "+testParams.getTestName()+" for filename:" + testParams.getFileName() + " and saas app type:" + testParams.getSaasType()+ "******************");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description:"+testParams.getTestDescription());
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output****************");
		Logger.info("Filename:"+testParams.getFileName());Logger.info("Risk Type:ContentIQ Violations");
		Logger.info("CIQ Profilename:"+testParams.getContentIQProfileName());Logger.info("Dictionaries:"+testParams.getDictionaries());
		Logger.info("*****************************************************");


		List<NameValuePair> headers = dciFunctions.getBasicHeaders(suiteData);		
		verifyDisplayLogsPredefinedDictionaries(dciFunctions, esLogs, headers, testParams.getFileName(), testParams.getSaasType(), 
				testParams.getContentIQProfileName(), testParams.getDictionaries());

		Logger.info(
				"************************************Completed "+testParams.getTestName()+" for filename:" + testParams.getFileName() + " and saas app type:" + testParams.getSaasType()+ "******************");

	}

	@Test(groups ={"PredefDictAttachment","PredefDictBody"})
	public void testDisplayLogsForRiskGeneratedInPredefinedDictionariesForMail() throws Exception {
		dciFunctions = new DCIFunctions();
		dciFunctions.deleteAllCIQProfiles(restClient, suiteData);
	}

	@Test(dataProvider = "dataUploadPredefTerms", groups ={"PredefTerms","PredefTermsAttachment","PredefTermsBody"})
	public void testDisplayLogsForRiskGeneratedInPredefinedTerms(TestParameters testParams) throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();

		Logger.info(
				"************************************Starting "+testParams.getTestName()+" for filename:" + testParams.getFileName() + " and saas app type:" + testParams.getSaasType()+ "******************");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description:"+testParams.getTestDescription());
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output****************");
		Logger.info("Filename:"+testParams.getFileName());Logger.info("Risk Type:ContentIQ Violations");
		Logger.info("CIQ Profilename:"+testParams.getContentIQProfileName());Logger.info("Terms:"+testParams.getTerms());
		Logger.info("*****************************************************");


		List<NameValuePair> headers = dciFunctions.getBasicHeaders(suiteData);		
		verifyDisplayLogsPredefinedTerms(dciFunctions, esLogs, headers, testParams.getFileName(), testParams.getSaasType(), 
				testParams.getContentIQProfileName(), testParams.getTerms(), testParams.getCount());

		Logger.info(
				"************************************Completed "+testParams.getTestName()+" for filename:" + testParams.getFileName() + " and saas app type:" + testParams.getSaasType()+ "******************");

	}


	@Test(dataProvider = "dataUploadPredefDictScan", groups ={"PredefDictScan"})
	public void testDisplayLogsForRiskGeneratedForScanNowOptionInPredefinedDictionaries(TestParameters testParams) throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();

		Logger.info(
				"************************************Starting "+testParams.getTestName()+" for filename:" + testParams.getFileName() + " and saas app type:" + testParams.getSaasType()+ "******************");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description:"+testParams.getTestDescription());
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output****************");
		Logger.info("Filename:"+testParams.getFileName());Logger.info("Risk Type:ContentIQ Violations");
		Logger.info("CIQ Profilename:"+testParams.getContentIQProfileName());Logger.info("Dictionaries:"+testParams.getDictionaries());
		Logger.info("*****************************************************");


		List<NameValuePair> headers = dciFunctions.getBasicHeaders(suiteData);		
		verifyDisplayLogsPredefinedDictionaries(dciFunctions, esLogs, headers, testParams.getFileName(), testParams.getSaasType(), 
				testParams.getContentIQProfileName(), testParams.getDictionaries());

		Logger.info(
				"************************************Completed "+testParams.getTestName()+" for filename:" + testParams.getFileName() + " and saas app type:" + testParams.getSaasType()+ "******************");

	}

	@Test(dataProvider = "dataUploadPredefTermsScan", groups ={"PredefTermsScan"})
	public void testDisplayLogsForRiskGeneratedForScanNowOptionInPredefinedTerms(TestParameters testParams) throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();

		Logger.info(
				"************************************Starting "+testParams.getTestName()+" for filename:" + testParams.getFileName() + " and saas app type:" + testParams.getSaasType()+ "******************");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description:"+testParams.getTestDescription());
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output****************");
		Logger.info("Filename:"+testParams.getFileName());Logger.info("Risk Type:ContentIQ Violations");
		Logger.info("CIQ Profilename:"+testParams.getContentIQProfileName());Logger.info("Terms:"+testParams.getTerms());
		Logger.info("*****************************************************");

		List<NameValuePair> headers = dciFunctions.getBasicHeaders(suiteData);	
		String[] risks = dciFunctions.getRisksTypes(testParams.getFileName());
		verifyDisplayLogsPredefinedTerms(dciFunctions, esLogs, headers, testParams.getFileName(), testParams.getSaasType(), 
				testParams.getContentIQProfileName(), testParams.getTerms(), testParams.getCount(), risks.length, risks);	

		Logger.info(
				"************************************Completed "+testParams.getTestName()+" for filename:" + testParams.getFileName() + " and saas app type:" + testParams.getSaasType()+ "******************");

	}

	@Test(dataProvider = "dataUploadCustomTerms", groups ={"CustomTerms"})
	public void testDisplayLogsForRiskGeneratedInCustomTerms(TestParameters testParams) throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();

		Logger.info(
				"************************************Starting "+testParams.getTestName()+" for filename:" + testParams.getFileName() + " and saas app type:" + testParams.getSaasType()+ "******************");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description:"+testParams.getTestDescription());
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output****************");
		Logger.info("Filename:"+testParams.getFileName());Logger.info("Risk Type:ContentIQ Violations");
		Logger.info("CIQ Profilename:"+testParams.getContentIQProfileName());
		Logger.info("Custom Regex Terms:"+testParams.getTerms());
		Logger.info("*****************************************************");


		List<NameValuePair> headers = dciFunctions.getBasicHeaders(suiteData);	
		verifyDisplayLogsCustomTerms(dciFunctions, esLogs, headers, testParams.getFileName(), testParams.getSaasType(), 
				testParams.getContentIQProfileName(), testParams.getTerms(), testParams.getCount());

		Logger.info(
				"************************************Completed "+testParams.getTestName()+" for filename:" + testParams.getFileName() + " and saas app type:" + testParams.getSaasType()+ "******************");

	}

	@Test(dataProvider = "dataUploadPredefDictNegative", groups ={"PredefDictNegative"})
	public void testDisplayLogsNotGeneratedForRiskInDisabledPredefinedDictionaries(TestParameters testParams) throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();

		Logger.info(
				"************************************Starting "+testParams.getTestName()+" for filename:" + testParams.getFileName() + " and saas app type:" + testParams.getSaasType()+ "******************");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description:"+testParams.getTestDescription());
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output****************");
		Logger.info("Filename:"+testParams.getFileName());Logger.info("Risk Type:ContentIQ Violations");
		Logger.info("CIQ Profilename:"+testParams.getContentIQProfileName());Logger.info("Dictionaries:"+testParams.getDictionaries());
		Logger.info("*****************************************************");


		List<NameValuePair> headers = dciFunctions.getBasicHeaders(suiteData);		
		verifyDisplayLogsNegative(dciFunctions, testParams.getFileName(), 
				testParams.getSaasType(), headers);

		Logger.info(
				"************************************Completed "+testParams.getTestName()+" for filename:" + testParams.getFileName() + " and saas app type:" + testParams.getSaasType()+ "******************");

	}


	@Test(dataProvider = "dataUploadPredefTermsNegative", groups ={"PredefTermsNegative"})
	public void testDisplayLogsNotGeneratedForRiskInDisabledPredefinedTerms(TestParameters testParams) throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();

		Logger.info(
				"************************************Starting "+testParams.getTestName()+" for filename:" + testParams.getFileName() + " and saas app type:" + testParams.getSaasType()+ "******************");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description:"+testParams.getTestDescription());
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output****************");
		Logger.info("Filename:"+testParams.getFileName());Logger.info("Risk Type:ContentIQ Violations");
		Logger.info("CIQ Profilename:"+testParams.getContentIQProfileName());Logger.info("Terms:"+testParams.getTerms());
		Logger.info("*****************************************************");


		List<NameValuePair> headers = dciFunctions.getBasicHeaders(suiteData);		
		if(testParams.getFileName().contains("US_Social_Security_Number.txt")||
				testParams.getFileName().contains("Credit_Card_Number.txt")){
			verifyRiskTypesDisplayLogs(dciFunctions, testParams.getFileName(), 
					testParams.getSaasType(), headers);
		}else{
			verifyDisplayLogsNegative(dciFunctions, testParams.getFileName(), 
					testParams.getSaasType(), headers);
		}
		Logger.info(
				"************************************Completed "+testParams.getTestName()+" for filename:" + testParams.getFileName() + " and saas app type:" + testParams.getSaasType()+ "******************");

	}

	/**********************************************TEST METHODS***********************************************/
	/**********************************************DATA PROVIDERS*********************************************/

	@DataProvider(name = "dataUploadPredefDict")
	public Object[][] dataUploadPredefDict() {
		dciFunctions = new DCIFunctions();

		String[] dictionaries = dciFunctions.predefinedDictionariesCIQProfileText();
		String[] ciqProfileNames = dciFunctions.predefinedDictionariesCIQProfileName(); 
		String[] fileName= dciFunctions.predefinedDictionariesCIQProfileFileName(); 

		dciFunctions.createAllCIQPredefinedDictionaries(restClient, suiteData);

		fileName = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_CIQ_DIC_PATH,
				fileName);
		dciFunctions.fileUploadOrSendEmail(suiteData, folderInfo, fileName);

		Object[][] result = new Object[dictionaries.length][];
		for (int i = 0; i < dictionaries.length; i++) {
			result[i] = new Object[] { new TestParameters("Risk Generation/Validation for Predefined Dictionary:"+dictionaries[i]+" in ContentIQ Profile:"+ciqProfileNames[i]+" for file:"+fileName[i], 
					"Create CIQ Profile with name:"+ciqProfileNames[i]+" with predefined dictionaries:"+dictionaries[i]+" and upload file:"+fileName[i]+ ". Then verify risk logs are getting generated within the SLA provided",
					fileName[i], SaasType.getSaasFilterType(suiteData.getSaasApp()), ciqProfileNames[i], dictionaries[i])};


		}

		mainCounter = 0;testCounter = DCIConstants.DCI_COUNTER_MAX;

		return result;
	}



	@DataProvider(name = "dataUploadPredefTerms")
	public Object[][] dataUploadPredefTerms() {
		dciFunctions = new DCIFunctions();

		String[] terms = dciFunctions.predefinedTermsCIQProfileText();
		String[] ciqProfileNames = dciFunctions.predefinedTermsCIQProfileName(); 
		String[] fileName= dciFunctions.predefinedTermsCIQProfileFileName(); 
		String[] countTerms = dciFunctions.predefinedTermsCIQProfileCount();

		dciFunctions.createAllCIQPredefinedTerms(restClient, suiteData);

		fileName = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_CIQ_TERMS_PATH,
				fileName);
		dciFunctions.fileUploadOrSendEmail(suiteData, folderInfo, fileName);

		Object[][] result = new Object[terms.length][];
		for (int i = 0; i < terms.length; i++) {
			result[i] = new Object[] { new TestParameters("Risk Generation/Validation for Predefined Terms:"+terms[i]+" in ContentIQ Profile:"+ciqProfileNames[i]+" for file:"+fileName[i], 
					"Create CIQ Profile with name:"+ciqProfileNames[i]+" with predefined terms:"+terms[i]+" and upload file:"+fileName[i]+ ". Then verify risk logs are getting generated within the SLA provided",
					fileName[i], SaasType.getSaasFilterType(suiteData.getSaasApp()), ciqProfileNames[i], terms[i], countTerms[i])};

		}

		mainCounter = 0;testCounter = DCIConstants.DCI_COUNTER_MAX;

		return result;
	}


	@DataProvider(name = "dataUploadPredefDictScan")
	public Object[][] dataUploadPredefDictScan() {
		dciFunctions = new DCIFunctions();

		String[] dictionaries = dciFunctions.predefinedDictionariesCIQProfileText();
		String[] ciqProfileNames = dciFunctions.predefinedDictionariesCIQProfileName(); 
		String[] ciqProfileDescriptions = dciFunctions.predefinedDictionariesCIQProfileDescription();
		String[] fileNames= dciFunctions.predefinedDictionariesCIQProfileFileName(); 

		String dictionary = dictionaries[0];
		String ciqProfileName = ciqProfileNames[0]; 
		String ciqProfileDescription = ciqProfileDescriptions[0];
		String fileName= fileNames[0]; 

		try {
			contentIQProfileIdDictId=dciFunctions.createCIQPredefinedDictionaries(restClient,suiteData,
					dictionary, ciqProfileName, ciqProfileDescription, "high", 1, false, 0, false);	
		} catch (Exception ex) {
			Logger.info("Issue with Create Content Iq Profiles" + ex.getLocalizedMessage());
		}

		fileName = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_CIQ_DIC_PATH,
				fileName);

		Object[][] result = new Object[1][];

		UserAccount account = dciFunctions.getUserAccount(suiteData);
		UniversalApi universalApi = dciFunctions.getUniversalApi(suiteData, account);
		dciFunctions.uploadFile(universalApi, suiteData, folderInfo.get("folderId"), fileName);

		result[0] = new Object[] { new TestParameters("Risk Generation/Validation for Predefined Dictionary:"+dictionary+" in ContentIQ Profile:"+ciqProfileName+" for file:"+fileName, 
				"Create CIQ Profile with name:"+ciqProfileName+" with predefined dictionaries:"+dictionary+" and upload file:"+fileName+ ". Then verify risk logs are getting generated after enabling scan now within the SLA provided",
				fileName, SaasType.getSaasFilterType(suiteData.getSaasApp()), ciqProfileName, dictionary)};

		dciFunctions.waitForMinutes(3);

		try {
			contentIQProfileIdDictId=dciFunctions.updateCIQPredefinedDictionary(restClient,suiteData,
					contentIQProfileIdDictId, dictionary, ciqProfileName, ciqProfileDescription, "high", 1, true, 1, false);
		} catch (Exception ex) {
			Logger.info("Issue with Update Content Iq Profiles" + ex.getLocalizedMessage());
		}

		try {
			dciFunctions.enableScanNow(restClient, dciFunctions.getCookieHeaders(suiteData), 
					SaasType.getSaasFilterType(suiteData.getSaasApp()), suiteData.getScheme(), suiteData.getHost());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		mainCounter = 0;testCounter = DCIConstants.DCI_COUNTER_MAX;

		return result;
	}

	@DataProvider(name = "dataUploadPredefTermsScan")
	public Object[][] dataUploadPredefTermsScan() {
		dciFunctions = new DCIFunctions();
		dciFunctions.deleteAllCIQProfiles(restClient, suiteData);

		String[] terms = dciFunctions.predefinedTermsCIQProfileText();
		String[] ciqProfileNames = dciFunctions.predefinedTermsCIQProfileName(); 
		String[] ciqProfileDescriptions = dciFunctions.predefinedTermsCIQProfileDescription();
		String[] fileNames= dciFunctions.predefinedTermsCIQProfileFileName(); 
		String[] countTerms = dciFunctions.predefinedTermsCIQProfileCount();

		String term = terms[0];
		String ciqProfileName = ciqProfileNames[0]; 
		String ciqProfileDescription = ciqProfileDescriptions[0];
		String fileName= fileNames[0]; 
		String countTerm= countTerms[0]; 


		try {
			contentIQProfileIdTermId=dciFunctions.createCIQPredefinedTerms(restClient,suiteData,
					term, ciqProfileName, ciqProfileDescription, "high", 1, false, 0, false);
		} catch (Exception ex) {
			Logger.info("Issue with Create Content Iq Profiles" + ex.getLocalizedMessage());
		}

		fileName = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_CIQ_TERMS_PATH,
				fileName);

		Object[][] result = new Object[1][];
		UserAccount account = dciFunctions.getUserAccount(suiteData);
		UniversalApi universalApi = dciFunctions.getUniversalApi(suiteData, account);
		dciFunctions.uploadFile(universalApi, suiteData, folderInfo.get("folderId"), fileName);

		result[0] = new Object[] { new TestParameters("Risk Generation/Validation for Predefined Terms:"+term+" in ContentIQ Profile:"+ciqProfileName+" for file:"+fileName, 
				"Create CIQ Profile with name:"+ciqProfileName+" with predefined terms:"+term+" and upload file:"+fileName+ ". Then verify risk logs are getting generated after enabling scan now within the SLA provided",
				fileName, SaasType.getSaasFilterType(suiteData.getSaasApp()), ciqProfileName, term, countTerm)};
		dciFunctions.waitForMinutes(3);

		try {
			contentIQProfileIdTermId=dciFunctions.updateCIQPredefinedTerm(restClient,suiteData,
					contentIQProfileIdTermId, term, ciqProfileName, ciqProfileDescription, "high", 1, true, 1, false);
		} catch (Exception ex) {
			Logger.info("Issue with Update Content Iq Profiles" + ex.getLocalizedMessage());
		}

		try {
			dciFunctions.enableScanNow(restClient, dciFunctions.getCookieHeaders(suiteData), SaasType.getSaasFilterType(suiteData.getSaasApp()), 
					suiteData.getScheme(), suiteData.getHost());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		mainCounter = 0;testCounter = DCIConstants.DCI_COUNTER_MAX;

		return result;
	}

	@DataProvider(name = "dataUploadCustomTerms")
	public Object[][] dataUploadCustomTerms() {
		dciFunctions = new DCIFunctions();

		String[] terms = {
				"^3[47][0-9]{13}$",
				"^3(?:0[0-5]|[68][0-9])[0-9]{11}$",
				"^65[4-9][0-9]{13}|64[4-9][0-9]{13}|6011[0-9]{12}|(622(?:12[6-9]|1[3-9][0-9]|[2-8][0-9][0-9]|9[01][0-9]|92[0-5])[0-9]{10})$",
				"^(?:2131|1800|35\\d{3})\\d{11}$",
				"^5[1-5][0-9]{14}$",
				"^4[0-9]{12}(?:[0-9]{3})?$",
				"(?:^|[^\\w\\d-])(\\d\\n\\d\\n\\d\\n[-\u200B_\u2013]\\d\\n\\d\\n[-_\u200B\u2013]\\d\\n\\d\\n\\d\\n\\d)(?:$|[^\\w\\d-])"
		};


		String[] ciqProfileNames = {"DCI_CUS_AMEX","DCI_CUS_DINERS",
				"DCI_CUS_DISCOVER","DCI_CUS_JCB","DCI_CUS_MASTER","DCI_CUS_VISA",
		"DCI_CUS_FILLABLE_SSN_W9"}; 

		String[] ciqProfileDescription = {"AmexCardCP","DinerCardCP",
				"DiscoverCardCP","JCBCardCP","MasterCardCP","VisaCardCP",
		"Custom Terms for SSN in Fillable W9 document"};

		String[] fileName= {"CreditCard_Amex.txt","CreditCard_Diner.txt",
				"CreditCard_Discover.txt","CreditCard_JCB.txt",
				"CreditCard_Master.txt","CreditCard_Visa.txt",
		"Form_W9_Filled_SSN.docx"};


		String[] countTerms = {"5","6","5","8","6","7","1"};		

		try {

			for (int i = 0; i < terms.length; i++) {
				dciFunctions.createCIQCustomTerms(restClient, suiteData, terms[i], ciqProfileNames[i], ciqProfileDescription[i], 
						"high", 1, true, 1, false);
			}
			dciFunctions.waitForSeconds(10);

		} catch (Exception ex) {
			Logger.info("Issue with Create Content Iq Profiles" + ex.getLocalizedMessage());
		}

		fileName = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_CIQ_CUSTOM_TERMS_PATH,
				fileName);
		dciFunctions.fileUploadOrSendEmail(suiteData, folderInfo, fileName);

		Object[][] result = new Object[terms.length][];

		for (int i = 0; i < terms.length; i++) {
			result[i] = new Object[] { new TestParameters("Risk Generation/Validation for Custom Regex Terms:"+terms[i]+" in ContentIQ Profile:"+ciqProfileNames[i]+" for file:"+fileName[i], 
					"Create CIQ Profile with name:"+ciqProfileNames[i]+" with custom regex terms:"+terms[i]+" and upload file:"+fileName[i]+ ". Then verify risk logs are getting generated within the SLA provided",
					fileName[i], SaasType.getSaasFilterType(suiteData.getSaasApp()), ciqProfileNames[i], terms[i], countTerms[i])};
		}

		mainCounter = 0;testCounter = DCIConstants.DCI_COUNTER_MAX;

		return result;
	}

	@DataProvider(name = "dataUploadPredefDictNegative")
	public Object[][] dataUploadPredefDictNegative() {
		dciFunctions = new DCIFunctions();

		String[] dictionaries = dciFunctions.predefinedDictionariesCIQProfileText();
		String[] ciqProfileNames = dciFunctions.predefinedDictionariesCIQProfileName(); 
		String[] ciqProfileDescription = dciFunctions.predefinedDictionariesCIQProfileDescription();
		String[] fileName= dciFunctions.predefinedDictionariesCIQProfileFileName(); 

		try {

			for (int i = 0; i < dictionaries.length; i++) {
				Logger.info("Creating CIQ Predefined Dictionaries:"+dictionaries[i]+" in progress");
				List<String> valuesPreDefDict= new ArrayList<String>();
				valuesPreDefDict.add(dictionaries[i]);
				List<String> valuesPreDefTerms=null;
				List<String> valuesCustomDict=null;
				List<String> valuesCustomTerms=null;
				List<String> valuesRiskTypes=null;
				List<String> valuesContentTypes=null;
				List<String> valuesFileFormat=null;
				List<String> valuesMLProfile=null;

				dciFunctions.createCIQProfile(suiteData, restClient, ciqProfileNames[i], ciqProfileDescription[i], 
						valuesPreDefDict, valuesPreDefTerms, valuesCustomDict, valuesCustomTerms, 
						valuesRiskTypes, valuesContentTypes, valuesFileFormat,valuesMLProfile, 
						"high", 1, false, 0, false);

				Logger.info("Creating CIQ Predefined Dictionaries:"+dictionaries[i]+" is completed");
			}
			dciFunctions.waitForSeconds(10);

		} catch (Exception ex) {
			Logger.info("Issue with Create Content Iq Profiles" + ex.getLocalizedMessage());
		}

		fileName = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_CIQ_DIC_PATH,
				fileName);
		dciFunctions.fileUploadOrSendEmail(suiteData, folderInfo, fileName);

		Object[][] result = new Object[dictionaries.length][];
		for (int i = 0; i < dictionaries.length; i++) {
			result[i] = new Object[] { new TestParameters("No Risk Generation/Validation for Predefined Dictionary:"+dictionaries[i]+" in ContentIQ Profile:"+ciqProfileNames[i]+" for file:"+fileName[i], 
					"Create CIQ Profile with name:"+ciqProfileNames[i]+" with predefined dictionaries:"+dictionaries[i]+" in disabled state and upload file:"+fileName[i]+ ". Then verify risk logs are not getting generated within the SLA provided",
					fileName[i], SaasType.getSaasFilterType(suiteData.getSaasApp()), ciqProfileNames[i], dictionaries[i])};

		}

		dciFunctions.waitForMinutes(3);

		return result;
	}

	@DataProvider(name = "dataUploadPredefTermsNegative")
	public Object[][] dataUploadPredefTermsNegative() {
		dciFunctions = new DCIFunctions();

		String[] terms = dciFunctions.predefinedTermsCIQProfileText();
		String[] ciqProfileNames = dciFunctions.predefinedTermsCIQProfileName(); 
		String[] ciqProfileDescription = dciFunctions.predefinedTermsCIQProfileDescription();
		String[] fileName= dciFunctions.predefinedTermsCIQProfileFileName(); 
		String[] countTerms = dciFunctions.predefinedTermsCIQProfileCount();

		try {

			for (int i = 0; i < terms.length; i++) {
				Logger.info("Creating CIQ Predefined Terms:"+terms[i]+" in progress");
				List<String> valuesPreDefDict=null;
				List<String> valuesPreDefTerms= new ArrayList<String>();
				valuesPreDefTerms.add(terms[i]);
				List<String> valuesCustomDict=null;
				List<String> valuesCustomTerms=null;
				List<String> valuesRiskTypes=null;
				List<String> valuesContentTypes=null;
				List<String> valuesFileFormat=null;
				List<String> valuesMLProfile=null;

				dciFunctions.createCIQProfile(suiteData, restClient, ciqProfileNames[i], ciqProfileDescription[i], 
						valuesPreDefDict, valuesPreDefTerms, valuesCustomDict, valuesCustomTerms, 
						valuesRiskTypes, valuesContentTypes, valuesFileFormat,valuesMLProfile, 
						"high", 1, false, 0, false);

				Logger.info("Creating CIQ Predefined Terms:"+terms[i]+" is completed");
			}
			dciFunctions.waitForSeconds(10);

		} catch (Exception ex) {
			Logger.info("Issue with Create Content Iq Profiles" + ex.getLocalizedMessage());
		}

		fileName = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_CIQ_TERMS_PATH,
				fileName);
		dciFunctions.fileUploadOrSendEmail(suiteData, folderInfo, fileName);

		Object[][] result = new Object[terms.length][];
		for (int i = 0; i < terms.length; i++) {
			result[i] = new Object[] { new TestParameters("No Risk Generation/Validation for Predefined Terms:"+terms[i]+" in ContentIQ Profile:"+ciqProfileNames[i]+" for file:"+fileName[i], 
					"Create CIQ Profile with name:"+ciqProfileNames[i]+" with predefined terms:"+terms[i]+" in disabled state and upload file:"+fileName[i]+ ". Then verify risk logs are not getting generated within the SLA provided",
					fileName[i], SaasType.getSaasFilterType(suiteData.getSaasApp()), ciqProfileNames[i], terms[i], countTerms[i])};


		}
		mainCounter = 0;testCounter = DCIConstants.DCI_COUNTER_MAX;	
		dciFunctions.waitForMinutes(3);

		return result;
	}

	/**********************************************DATA PROVIDERS*********************************************/
	/**********************************************UTIL METHODS***********************************************/

	@Override
	public String getTestName() {
		return this.mTestCaseName;
	}


	private void verifyDisplayLogsPredefinedDictionaries(DCIFunctions dciFunctions, ElasticSearchLogs esLogs, List<NameValuePair> headers, String fileName, 
			String saasType, String contentIQProfileName, String dictionaries)
					throws Exception {
		String hits = "";
		String[] keywords = dciFunctions.getKeywordsFromFile(fileName);

		ArrayList<String> keywordList = new ArrayList<String>();
		for(String k:keywords){
			String keyword = k.toLowerCase();
			if(contentIQProfileName.equalsIgnoreCase("DCI_TIC")){
				keyword = keyword.replace(".", " ");
			}
			keywordList.add(keyword);
		}
		keywords = keywordList.toArray(new String[keywordList.size()]);
		Arrays.sort(keywords);

		System.out.println(keywords.toString());

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

			String validationMessage = dciFunctions.validationRiskLogsContentIQProfilePreDefinedDictionaries(hits, fileName, suiteData.getUsername(),
					"QA Admin", saasType, "API", "ContentIQ Violations", contentIQProfileName, dictionaries, keywords, 1);

			Assert.assertEquals(validationMessage, "", "Output Response Validation is failing for CIQ Profile with name:"+contentIQProfileName
					+" with predefined dictionaries:"+dictionaries+" and for fileName:"+fileName);



		} finally {
			dciFunctions.cleanupFileFromTempFolder(fileName);
		}
	}

	private void verifyDisplayLogsPredefinedTerms(DCIFunctions dciFunctions, ElasticSearchLogs esLogs, List<NameValuePair> headers, String fileName, 
			String saasType, String contentIQProfileName, String terms, String count)
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

			String validationMessage = dciFunctions.validationRiskLogsContentIQProfilePreDefinedTerms(hits, fileName, suiteData.getUsername(),
					"QA Admin", saasType, "API", "ContentIQ Violations", contentIQProfileName, terms, count, 1);

			Assert.assertEquals(validationMessage, "", "Output Response Validation is failing for CIQ Profile with name:"+contentIQProfileName
					+" with predefined terms:"+terms+" and for fileName:"+fileName);



		} finally {
			dciFunctions.cleanupFileFromTempFolder(fileName);
		}
	}

	private void verifyDisplayLogsPredefinedTerms(DCIFunctions dciFunctions, ElasticSearchLogs esLogs, List<NameValuePair> headers, String fileName, 
			String saasType, String contentIQProfileName, String terms, String count, int riskCount, String[] risks)
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

			String validationMessage = dciFunctions.validationRiskLogsContentIQProfilePreDefinedTerms(hits, riskCount, fileName, suiteData.getUsername(),
					"QA Admin", saasType, "API", risks, contentIQProfileName, terms, count);

			Assert.assertEquals(validationMessage, "", "Output Response Validation is failing for CIQ Profile with name:"+contentIQProfileName
					+" with predefined terms:"+terms+" and for fileName:"+fileName);



		} finally {
			dciFunctions.cleanupFileFromTempFolder(fileName);
		}
	}

	private void verifyDisplayLogsCustomTerms(DCIFunctions dciFunctions, ElasticSearchLogs esLogs, List<NameValuePair> headers, String fileName, 
			String saasType, String contentIQProfileName, String terms, String count)
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

			String validationMessage = dciFunctions.validationRiskLogsContentIQProfileCustomTerms(hits, fileName, suiteData.getUsername(),
					"QA Admin", saasType, "API", "ContentIQ Violations", contentIQProfileName, terms, count);

			Assert.assertEquals(validationMessage, "", "Output Response Validation is failing for CIQ Profile with name:"+contentIQProfileName
					+" with predefined terms:"+terms+" and for fileName:"+fileName);



		} finally {
			dciFunctions.cleanupFileFromTempFolder(fileName);
		}
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
							"critical", "API", false);

			String validationMessage = dciFunctions.validateDCIRiskContentLogs
					(suiteData, hits, CIJson);

			Assert.assertEquals(validationMessage, "", "Output Response Validation is failing for fileName:"+fileName+
					" for saas app type:"+saasType);


		} finally {
			dciFunctions.cleanupFileFromTempFolder(fileName);
		}
	}

	private void verifyDisplayLogsNegative(DCIFunctions dciFunctions, String fileName, String saasType,
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
	 * Create folders in saas apps
	 */
	@BeforeClass(groups ={"PredefDict","PredefTerms","PredefDictScan",
			"PredefTermsScan","CustomTerms","PredefDictNegative","PredefTermsNegative"})
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
	@AfterClass(groups ={"PredefDict","PredefTerms","PredefDictScan","PredefTermsScan",
			"CustomTerms","PredefDictNegative","PredefTermsNegative"})
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
	@BeforeClass(groups ={"PredefDict","PredefTerms","PredefDictScan","PredefTermsScan",
			"PredefDictAttachment","PredefTermsAttachment","PredefDictBody","PredefTermsBody"})
	public void deleteContentIqProfileBeforeTestStarts() {
		dciFunctions = new DCIFunctions();
		dciFunctions.deleteAllPolicies(restClient, suiteData);
		dciFunctions.deleteAllCIQProfiles(restClient, suiteData);
		dciFunctions.deleteAllDictionaries(restClient, suiteData);
		dciFunctions.deleteAllTrainingProfiles(restClient, suiteData);
	}

	/**
	 * Delete content iq profile
	 */
	@AfterClass(groups ={"PredefDict","PredefTerms","PredefDictScan","PredefTermsScan",
			"PredefDictAttachment","PredefTermsAttachment","PredefDictBody","PredefTermsBody",
			"CustomTerms","PredefDictNegative","PredefTermsNegative"})
	public void deleteContentIqProfileAfterTestEnds() {
		dciFunctions = new DCIFunctions();
		dciFunctions.deleteAllPolicies(restClient, suiteData);
		dciFunctions.deleteAllCIQProfiles(restClient, suiteData);
		dciFunctions.deleteAllDictionaries(restClient, suiteData);
		dciFunctions.deleteAllTrainingProfiles(restClient, suiteData);
	}

	/**
	 * Delete mails in saas app
	 */
	@AfterClass(groups ={"PredefDictAttachment","PredefTermsAttachment","PredefDictBody","PredefTermsBody"})
	public void deleteMails() {
		dciFunctions.deleteAllEmailsFromInbox(suiteData);
	}



	@BeforeMethod(groups ={"PredefDict","PredefTerms","PredefDictScan","PredefTermsScan",
			"CustomTerms","PredefDictNegative","PredefTermsNegative"})
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
			s3.downloadWholeFolder(DCIConstants.DCI_S3_BUCKET, "DCI/CIQ/Dictionaries", 
					new File(DCIConstants.DCI_FILE_TEMP_PATH));
			s3.downloadWholeFolder(DCIConstants.DCI_S3_BUCKET, "DCI/CIQ/Terms", 
					new File(DCIConstants.DCI_FILE_TEMP_PATH));
			s3.downloadWholeFolder(DCIConstants.DCI_S3_BUCKET, "DCI/CIQ/CustomTerms", 
					new File(DCIConstants.DCI_FILE_TEMP_PATH));
		} catch (Exception ex) {
			Logger.info("Downloading folder from S3 is failed with exception " + ex.getLocalizedMessage());
		}
	}

	/**********************************************BEFORE/AFTER METHODS/CLASS*****************************************/


}