package com.elastica.beatle.tests.dci;

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

/**
 * 
 * @author eldorajan
 *
 */

public class DCIGoldenSetTests extends DCICommonTest implements ITest {

	DCIFunctions dciFunctions = null;
	ElasticSearchLogs esLogs = null;
	Map<String,String> folderInfo = new HashMap<String,String>();
	String uniqueId = UUID.randomUUID().toString();
	protected String mTestCaseName = "";
	
	int mainCounter = 0;int testCounter = 0;

	/**********************************************TEST METHODS***********************************************/

	/**
	 * This test cases uploads a risk file into a saas app and validates if risk log is generated, 
	 * subsequently it is validated for it's risk types and doc classification
	 * @param fileName
	 * @param saasType
	 * @throws Exception
	 */
	@Test(dataProvider = "dataUpload", groups ={"All"})
	public void testDisplayLogsGenerateRisk(TestParameters testParams) throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();
		Logger.info(
				"************************************Starting "+testParams.getTestName()+" for filename:" + testParams.getFileName() + " and saas app type:" + testParams.getSaasType()+ "******************");
		
		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description:"+testParams.getTestDescription());
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output****************");
		Logger.info("Filename:"+testParams.getFileName());Logger.info("Risk Types to be validated:"+testParams.getRisks());
		Logger.info("Saas App Type:"+testParams.getSaasType());
		Logger.info("*****************************************************");
		
		List<NameValuePair> headers = dciFunctions.getBasicHeaders(suiteData);		
		verifyRiskTypesDisplayLogs(dciFunctions, testParams.getFileName(), testParams.getSaasType(), headers);

		Logger.info(
				"************************************Completed "+testParams.getTestName()+" for filename:" + testParams.getFileName() + " and saas app type:" + testParams.getSaasType()+ "******************");
		
	}


	/**********************************************TEST METHODS***********************************************/
	/**********************************************DATA PROVIDERS*********************************************/

	@DataProvider(name = "dataUpload")
	public Object[][] dataUpload() {
		
		dciFunctions = new DCIFunctions();

		String[] fileName = dciFunctions.getFileName(DCIConstants.DCI_FILE_UPLOAD_GS_PATH);	
		String[] risks = dciFunctions.riskTypesForAFile(fileName);
		
		fileName = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_GS_PATH, fileName);

		dciFunctions.fileUploadOrSendEmail(suiteData, folderInfo, fileName);

		Object[][] result = new Object[fileName.length][];
		for (int i = 0; i < fileName.length; i++) {	
			result[i] = new Object[] { new TestParameters("Risk Generation/Validation in uploaded file:"+fileName[i]+" for validation of following risks:"+Arrays.asList(risks[i].split(",")), 
					"Create CIQ Profile with name:ContentIQProfileName with all predefined dictionaries/terms and upload file:"+fileName[i]+ ". Then verify risk logs are getting generated within the SLA provided",
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
	 * Create content iq profile
	 */
	@BeforeClass(groups ={"All"})
	public void createContentIqProfile() {
		dciFunctions = new DCIFunctions();
		try {
			dciFunctions.createAllCIQPredefinedDictionaries(restClient, suiteData);
			dciFunctions.createAllCIQPredefinedTerms(restClient, suiteData);

		} catch (Exception ex) {
			Logger.info("Issue with Create Content Iq Profiles" + ex.getLocalizedMessage());
		}
	}


	

	/**
	 * Delete content iq profile
	 * @throws Exception 
	 */
	@AfterClass(groups ={"All"})
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
	@BeforeClass(groups ={"All"})
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
	
	@BeforeSuite(alwaysRun=true)
	public void downloadFileFromS3() {
		try {
			S3ActionHandler s3 = new S3ActionHandler();
			s3.downloadWholeFolder(DCIConstants.DCI_S3_BUCKET, "DCI/GoldenSet", 
					new File(DCIConstants.DCI_FILE_TEMP_PATH));
			
		} catch (Exception ex) {
			Logger.info("Downloading folder from S3 is failed with exception " + ex.getLocalizedMessage());
		}
	}

	/**********************************************BEFORE/AFTER CLASS*****************************************/


}
