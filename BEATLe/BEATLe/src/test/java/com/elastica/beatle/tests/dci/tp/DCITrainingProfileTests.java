package com.elastica.beatle.tests.dci.tp;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
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
import com.elastica.beatle.dci.dto.Hit;
import com.elastica.beatle.dci.dto.Hits;
import com.elastica.beatle.es.ElasticSearchLogs;
import com.elastica.beatle.logger.Logger;
import com.universal.common.UniversalApi;
import com.universal.dtos.UserAccount;

import net.sf.json.JSONArray;
import net.sf.json.util.JSONTokener;


public class DCITrainingProfileTests extends DCICommonTest implements ITest {

	protected String mTestCaseName = "";
	DCIFunctions dciFunctions = null;
	ElasticSearchLogs esLogs = null;
	Map<String,String> folderInfo = new HashMap<String,String>();
	String uniqueId = UUID.randomUUID().toString();
	int mainCounter = 0;int testCounter = 0;


	/**********************************************TEST METHODS***********************************************/

	
	@Test(dataProvider = "dataUpload", groups ={"All"})
	public void testDisplayLogsForRiskGeneratedInTrainingProfileCIQProfile(TestParameters testParams) throws Exception {
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
				headers, testParams.getRisks(), testParams.getCiq().get("ciqProfileName"), testParams.getCiq().get("ciqProfileType"),
				testParams.getCiq().get("ciqProfileKeyword"), testParams.getCiq().get("ciqProfileCount"),
				testParams.getCiq().get("ciqType"), testParams.getCiq().get("ciqTrainingProfile"));

		
		Logger.info(
				"************************************Completed "+testParams.getTestName()+" for filename:" + testParams.getFileName() + " and saas app type:" + testParams.getSaasType()+ "******************");

	}
	
	

	/**********************************************TEST METHODS***********************************************/
	/**********************************************DATA PROVIDERS
	 * @throws Exception *********************************************/

	@SuppressWarnings("unchecked")
	@DataProvider(name = "dataUpload")
	public Object[][] dataUpload() throws Exception {
		dciFunctions = new DCIFunctions();

		List<Map<String,Object>> mlProfileList = 
				 new ArrayList<Map<String,Object>>();
		
		
		for(int i=0;i<mlProfileNames.length;i++){
			String mlProfileName = mlProfileNames[i];
			String filePathTF = DCIConstants.DCI_TP_UPLOAD_PATH + File.separator + 
					mlProfileName + File.separator + "Test";
			dciFunctions.createTrainingProfile(restClient, suiteData, mlProfileName);
			createCIQProfile(mlProfileName);
			
			Map<String, String> ciqValues = getCIQValues(mlProfileName);
			String[] fileName = dciFunctions.getFileName(filePathTF);
			String[] risks = dciFunctions.riskTypesForAFile(fileName);
			
			Map<String,Object> mlProfileMap = new HashMap<String,Object>();
			mlProfileMap.put("mlProfileName", mlProfileName);
			mlProfileMap.put("mlProfileFilePathTF", filePathTF);
			mlProfileMap.put("mlProfileCIQValues", ciqValues);
			mlProfileMap.put("mlProfileFileNames", fileName);
			mlProfileMap.put("mlProfileFileRisks", risks);
				
			mlProfileList.add(mlProfileMap);
		}
		
		dciFunctions.checkIfAllMLProfilesAreProcessed(suiteData, restClient, mlProfileNames, 150);
		
		for(String mlProfileName:mlProfileNames){
			dciFunctions.activateDeactivateMLProfile(suiteData, restClient, mlProfileName, true);
		}
		
		
		int resultCount = 0;
		for(Map<String,Object> mlMap:mlProfileList){
			String[] fileName = (String[]) mlMap.get("mlProfileFileNames");
			String filePath = (String) mlMap.get("mlProfileFilePathTF");	
			fileName = dciFunctions.createSampleFileType(filePath,fileName);
			dciFunctions.fileUploadOrSendEmail(suiteData, folderInfo, fileName);
			resultCount+=fileName.length;
		}
		
		Object[][] result = new Object[resultCount][];
		for (int i = 0; i < resultCount;) {
			for(Map<String,Object> mlMap:mlProfileList){
				String[] fileName = (String[]) mlMap.get("mlProfileFileNames");
				
				Map<String, String> ciqValues = (Map<String, String>) mlMap.get("mlProfileCIQValues");
				String[] fileNames = (String[]) mlMap.get("mlProfileFileNames");
				String[] risks =(String[]) mlMap.get("mlProfileFileRisks");
				
				System.out.println(Arrays.asList(fileNames));
				System.out.println(Arrays.asList(risks));
				
				for (int j = 0; j < fileNames.length; j++) {
					result[i] = new Object[] { new TestParameters("Risk Generation/Validation for ContentIQ Profile:"+ciqValues.get("ciqProfileName")+"for file:"+fileNames[j], 
							"Create CIQ Profile with name:"+ciqValues.get("ciqProfileName")+" and upload file:"+fileNames[j]+ ". Then verify risk logs are getting generated within the SLA provided",
							fileName[j], SaasType.getSaasFilterType(suiteData.getSaasApp()), Arrays.asList(risks[j].split(",")), ciqValues)};
					i++;
				}
				
			}
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

	private void createCIQProfile(String mlProfileName) throws Exception{
		String ciqProfileName=mlProfileName+" Name";
		String ciqProfileDescription=mlProfileName+" Description";
		
		Logger.info("Creating CIQ profile with name:"+ciqProfileName+" with ML profile name:"+mlProfileName+" in progress");
	
		List<String> valuesPreDefDict=null;List<String> valuesPreDefTerms=null;
		List<String> valuesCustomDict=null;List<String> valuesCustomTerms=null;
		List<String> valuesRiskTypes=null;List<String> valuesContentTypes=null;
		List<String> valuesFileFormat=null;
		List<String> valuesMLProfile=new ArrayList<String>();valuesMLProfile.add(mlProfileName);
		
		dciFunctions.createCIQProfile(suiteData, restClient, ciqProfileName, ciqProfileDescription, 
				valuesPreDefDict, valuesPreDefTerms, valuesCustomDict, valuesCustomTerms, 
				valuesRiskTypes, valuesContentTypes, valuesFileFormat,valuesMLProfile,
				"high", 0, true, 1, false);

		Logger.info("Creating CIQ profile with name:"+ciqProfileName+" with ML profile name:"+mlProfileName+" is completed");
	}


	private Map<String, String> getCIQValues(String mlProfileName) {
		Map<String, String> ciq=new HashMap<String, String>();
		ciq.put("ciqProfileName", mlProfileName+" Name");
		ciq.put("ciqProfileType", mlProfileName);
		ciq.put("ciqProfileDescription", mlProfileName+" Description");
		ciq.put("ciqProfileKeyword", "");
		ciq.put("ciqProfileCount", "1");
		ciq.put("ciqType", "TrainingProfile");
		ciq.put("ciqTrainingProfile", mlProfileName);

		return ciq;
	}
	
	private String getFileCategory(String fileName){
		String category = "";
		for(String mlProfile:mlProfileNames){
			if(fileName.contains(mlProfile)){
				category = mlProfile;
				break;
			}
		}
		
		
		return category;
	}
	
	private void verifyRiskTypesDisplayLogs(DCIFunctions dciFunctions, String fileName, String saasType, List<NameValuePair> headers,
			List<String> risks, String ciqProfileName, String ciqProfileType, String ciqProfileKeyword, String ciqProfileCount, String ciqType,
			String ciqTrainingProfile)
			throws Exception{
		String hitsJson = "";
		
		Map<String, String> CIJson = new HashMap<String, String>();
		
		try {

			int riskCount=1;
			try{ if(StringUtils.isEmpty(risks.toString().replace("[", "").replace("]", ""))) riskCount=0;}catch(Exception e){riskCount=0;}
			
			String payload = 
					dciFunctions.getSearchQueryForDCI(suiteData, dciFunctions.getMinusMinutesFromCurrentTime(1440) , 
							dciFunctions.getPlusMinutesFromCurrentTime(120), saasType, DCIConstants.CISourceType, 
							DCIConstants.CICriticalSeverityType, fileName, DCIConstants.CIFacilityType, DCIConstants.CIActivityType);
			Logger.info("****************Input Payload****************");
			Logger.info(payload);
			Logger.info("*********************************************");

			hitsJson = fetchDisplayLogsCounter(dciFunctions, headers, payload, DCIConstants.DCI_COUNTER_MAX, riskCount);

			Logger.info("****************Output Response****************");
			Logger.info(hitsJson);
			Logger.info("***********************************************");

			Hits hits = dciFunctions.unmarshall(hitsJson, Hits.class);
			List<Hit> hit = hits.getHits();
			if(hit.size()>0){
				if(fileName.contains("VB")){
					riskCount=1;
					String category = getFileCategory(fileName);
					
					ciqProfileName = category+" Name";
					ciqProfileType = category;
					ciqProfileKeyword = "";
					ciqProfileCount = "1";
					ciqType = "TrainingProfile";
					
					CIJson = dciFunctions.
							populateContentInspectionJson(suiteData,fileName, riskCount, 
									"critical", "API", true, Arrays.asList(ciqProfileName.split(",")),  
									Arrays.asList(ciqProfileType.split(",")), Arrays.asList(ciqProfileKeyword.split(",")),  
									Arrays.asList(ciqProfileCount.split(",")), Arrays.asList(ciqType.split(",")),
									Arrays.asList(ciqTrainingProfile.split(",")));
				}else{
					CIJson = dciFunctions.
							populateContentInspectionJson(suiteData,fileName, riskCount, 
									"critical", "API", true, Arrays.asList(ciqProfileName.split(",")),  
									Arrays.asList(ciqProfileType.split(",")), Arrays.asList(ciqProfileKeyword.split(",")),  
									Arrays.asList(ciqProfileCount.split(",")), Arrays.asList(ciqType.split(",")),
									Arrays.asList(ciqTrainingProfile.split(",")));
					
				}
				CIJson.put("mimeFlag","true");CIJson.put("docClassFlag","true");CIJson.put("strictFlag","false");
			}else{
				if(hit.size()==0){
					if(fileName.contains("VB")){
						CIJson.put("LogCount",Integer.toString(0));
					}else{
						CIJson.put("LogCount",Integer.toString(riskCount));
					}
				}	
			}
			
			String validationMessage = dciFunctions.validateDCIRiskContentLogs
					(suiteData, hitsJson, CIJson);

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


	/**********************************************UTIL METHODS***********************************************/
	/**********************************************BEFORE/AFTER CLASS*****************************************/

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


	/**
	 * Delete content iq profile
	 */
	@BeforeClass(groups ={"All"})
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

	public String[] mlProfileNames= {
			"1040",
			"1099",
			"Accessory_Dwelling_Application",
			"Airline_Itinerary",
			"Architecture_Principles_Template",
			"Bank_Account_Opening_Application",
			"Building_Permit_Application",
			"Business_Visa_Application",
			"Car_Loan_Application",
			"Certification_Inspection_Application",
			"Civil_Court_Summons_Form",
			"Civil_Judgement_Form",
			"Consulting_Template",
			"Credit_Card_Application",
			"Criminal_Court_Subpoena",
			"Criminal_Court_Summons",
			"Debt_Collection",
			"Direct_Deposition_Authorization",
			"Expense_Report",
			"Fafsa",
			"Financial_Statement",
			"Full_Environmental_Impact_Assessment",
			"I-9",
			"Immigration_Documents",
			"Immunization_Record",
			"Invoices",
			"Job_Description_Template",
			"Lease",
			"Marketing_Template",
			"Medical_Consent_Form",
			"Medical_History_Form",
			"Medical_Record",
			"Medical_Release_Form",
			"Mortgage_Application",
			"Mortgage_Loan_Application",
			"Mortgage_Release",
			"Other_Financial_Aid_Form",
			"Pay_Stub_Template",
			"Payment_Authorization",
			"Power_Of_Attorney",
			"Purchase_Order_Template",
			"Quaterly_Financial",
			"Rental_Application",
			"Risk_Assessment_Template",
			"Short_Environmental_Assessment_Form",
			"Software_Design_Template",
			"Subpoena_Civil_Case",
			"Tourist_Visa_Application",
			"W-2",
			"W-4",
			"Zoning_Clearance"
	};
	
	@BeforeSuite(alwaysRun=true)
	public void downloadFileFromS3() {
		try {
			S3ActionHandler s3 = new S3ActionHandler();
			s3.downloadWholeFolder(DCIConstants.DCI_S3_BUCKET, "DCI/TrainingProfiles/Categories", 
					new File(DCIConstants.DCI_FILE_TEMP_PATH));
			
		} catch (Exception ex) {
			Logger.info("Downloading folder from S3 is failed with exception " + ex.getLocalizedMessage());
		}
	}
	
	/**********************************************BEFORE/AFTER METHODS/CLASS*****************************************/


}