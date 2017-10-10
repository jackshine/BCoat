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

public class DCIPCIPIITests extends DCICommonTest implements ITest {

	DCIFunctions dciFunctions = null;
	ElasticSearchLogs esLogs = null;
	Map<String,String> folderInfo = new HashMap<String,String>();
	String uniqueId = UUID.randomUUID().toString();
	protected String mTestCaseName = "";
	int mainCounter = 0;int testCounter = 0;

	/**********************************************TEST METHODS***********************************************/




	@Test(dataProvider = "dataUploadPCIPIICreditCardScenarios", groups ={"All"})
	public void testDisplayLogsGenerateRiskForPCIPIIRiskCreditCardScenarios(TestParameters testParams) throws Exception {
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


	/**********************************************TEST METHODS***********************************************/
	/**********************************************DATA PROVIDERS*********************************************/

	@DataProvider(name = "dataUploadPCIPIICreditCardScenarios")
	public Object[][] dataUploadPCICreditCardScenarios() {

		dciFunctions = new DCIFunctions();

		String[] risks = {

				"PII","PII",
				"PII","PII","PCI","PII,PCI","PCI",
				"PCI","PII,PCI","PII,PCI","PII,PCI","PCI",
				"PII,PCI","PCI","PCI","PII,PCI","PII,PCI",
				"PII,PCI","PCI","PII,PCI","PCI","PCI",
				"PII,PCI","PII,PCI","PII,PCI","PCI","PII,PCI",
				"PCI","PCI","PII,PCI","PII,PCI","PII,PCI",
				"PCI","PII,PCI","PCI","PCI","PII,PCI",
				"PII,PCI","PII,PCI","PCI","PII,PCI","PCI",
				"PCI","PII,PCI","PII,PCI","PII,PCI","PCI",
				"PII,PCI","PCI","PCI","PII,PCI","PII,PCI",
				"PII,PCI","PCI","PII,PCI","PCI","PCI",

				"PII,PCI","PII,PCI","PII,PCI","PII",
				"PII","PII","PII",

				"PII","PII",
				"PII","PII","PCI","PII,PCI","PCI",
				"PCI","PII,PCI","PII,PCI","PII,PCI","PCI",
				"PII,PCI","PCI","PCI","PII,PCI","PII,PCI",
				"PII,PCI","PCI","PII,PCI","PCI","PCI",
				"PII,PCI","PII,PCI","PII,PCI","PCI","PII,PCI",
				"PCI","PCI","PII,PCI","PII,PCI","PII,PCI",

		};


		String[] fileName= {
				"CCED_EMAIL_SSN.txt","CCED_NAME_EMAIL_SSN.txt",
				"CCED_NAME_SSN.txt","CCED_SSN.txt","CCN_CCED_EMAIL.txt","CCN_CCED_EMAIL_SSN.txt","CCN_CCED_NAME.txt",		
				"CCN_CCED_NAME_EMAIL.txt","CCN_CCED_NAME_EMAIL_SSN.txt","CCN_CCED_NAME_SSN.txt","CCN_CCED_SSN.txt","CCN_CCSC_CCED_EMAIL.txt",
				"CCN_CCSC_CCED_EMAIL_SSN.txt","CCN_CCSC_CCED_NAME.txt","CCN_CCSC_CCED_NAME_EMAIL.txt","CCN_CCSC_CCED_NAME_EMAIL_SSN.txt","CCN_CCSC_CCED_NAME_SSN.txt",
				"CCN_CCSC_CCED_SSN.txt","CCN_CCSC_EMAIL.txt","CCN_CCSC_EMAIL_SSN.txt","CCN_CCSC_NAME.txt","CCN_CCSC_NAME_EMAIL.txt",
				"CCN_CCSC_NAME_EMAIL_SSN.txt","CCN_CCSC_NAME_SSN.txt","CCN_CCSC_SSN.txt","CCN_CCTN_CCED_EMAIL.txt","CCN_CCTN_CCED_EMAIL_SSN.txt",	
				"CCN_CCTN_CCED_NAME.txt","CCN_CCTN_CCED_NAME_EMAIL.txt","CCN_CCTN_CCED_NAME_EMAIL_SSN.txt","CCN_CCTN_CCED_NAME_SSN.txt","CCN_CCTN_CCED_SSN.txt",
				"CCN_CCTN_CCSC_CCED_EMAIL.txt","CCN_CCTN_CCSC_CCED_EMAIL_SSN.txt","CCN_CCTN_CCSC_CCED_NAME.txt","CCN_CCTN_CCSC_CCED_NAME_EMAIL.txt","CCN_CCTN_CCSC_CCED_NAME_EMAIL_SSN.txt",
				"CCN_CCTN_CCSC_CCED_NAME_SSN.txt","CCN_CCTN_CCSC_CCED_SSN.txt","CCN_CCTN_CCSC_EMAIL.txt","CCN_CCTN_CCSC_EMAIL_SSN.txt","CCN_CCTN_CCSC_NAME.txt",
				"CCN_CCTN_CCSC_NAME_EMAIL.txt","CCN_CCTN_CCSC_NAME_EMAIL_SSN.txt","CCN_CCTN_CCSC_NAME_SSN.txt","CCN_CCTN_CCSC_SSN.txt","CCN_CCTN_EMAIL.txt",
				"CCN_CCTN_EMAIL_SSN.txt","CCN_CCTN_NAME.txt","CCN_CCTN_NAME_EMAIL.txt","CCN_CCTN_NAME_EMAIL_SSN.txt","CCN_CCTN_NAME_SSN.txt",
				"CCN_CCTN_SSN.txt","CCN_EMAIL.txt","CCN_EMAIL_SSN.txt","CCN_NAME.txt","CCN_NAME_EMAIL.txt",

				"CCN_NAME_EMAIL_SSN.txt","CCN_NAME_SSN.txt","CCN_SSN.txt","CCSC_CCED_EMAIL_SSN.txt",
				"CCSC_CCED_NAME_EMAIL_SSN.txt","CCSC_CCED_NAME_SSN.txt","CCSC_CCED_SSN.txt",

				"CCSC_EMAIL_SSN.txt","CCSC_NAME_EMAIL_SSN.txt",
				"CCSC_NAME_SSN.txt","CCSC_SSN.txt","CCTN_CCED_EMAIL.txt","CCTN_CCED_EMAIL_SSN.txt","CCTN_CCED_NAME.txt",
				"CCTN_CCED_NAME_EMAIL.txt","CCTN_CCED_NAME_EMAIL_SSN.txt","CCTN_CCED_NAME_SSN.txt","CCTN_CCED_SSN.txt","CCTN_CCSC_CCED_EMAIL.txt",
				"CCTN_CCSC_CCED_EMAIL_SSN.txt","CCTN_CCSC_CCED_NAME.txt","CCTN_CCSC_CCED_NAME_EMAIL.txt","CCTN_CCSC_CCED_NAME_EMAIL_SSN.txt","CCTN_CCSC_CCED_NAME_SSN.txt",
				"CCTN_CCSC_CCED_SSN.txt","CCTN_CCSC_EMAIL.txt","CCTN_CCSC_EMAIL_SSN.txt","CCTN_CCSC_NAME.txt","CCTN_CCSC_NAME_EMAIL.txt",
				"CCTN_CCSC_NAME_EMAIL_SSN.txt","CCTN_CCSC_NAME_SSN.txt","CCTN_CCSC_SSN.txt","CCTN_EMAIL.txt","CCTN_EMAIL_SSN.txt",
				"CCTN_NAME.txt","CCTN_NAME_EMAIL.txt","CCTN_NAME_EMAIL_SSN.txt","CCTN_NAME_SSN.txt","CCTN_SSN.txt",


		};

		fileName = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_PCI_PII_PATH,
				fileName);
		dciFunctions.fileUploadOrSendEmail(suiteData, folderInfo, fileName);

		Object[][] result = new Object[fileName.length][];
		for (int i = 0; i < fileName.length; i++) {
			result[i] = new Object[] { new TestParameters("Risk Generation/Validation for PCI risks file:"+fileName[i]+" for risks"+Arrays.asList(risks[i].split(",")), 
					"Upload PCI risks file:"+fileName[i]+ ". Then verify risk logs are getting generated within the SLA provided"+" for risks"+Arrays.asList(risks[i].split(",")),
					fileName[i], SaasType.getSaasFilterType(suiteData.getSaasApp()), Arrays.asList(risks[i].split(",")))};
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

	private void verifyDisplayLogs(DCIFunctions dciFunctions, String fileName, String saasType,
			List<NameValuePair> headers, String[] risks) throws Exception {
		String hits = "";
		try{	
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

			String validationMessage = dciFunctions.validationPCIPIIRiskLogs(hits, fileName, suiteData.getUsername(),
					"QA Admin", saasType, "API", risks);

			Assert.assertEquals(validationMessage, "", "Output Response Validation is failing for fileName:"+fileName+
					" for saas app type:"+saasType);


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
