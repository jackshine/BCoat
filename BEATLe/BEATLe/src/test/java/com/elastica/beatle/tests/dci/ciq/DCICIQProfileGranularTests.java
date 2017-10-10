package com.elastica.beatle.tests.dci.ciq;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.entity.StringEntity;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import org.apache.commons.lang3.StringUtils;
import com.elastica.beatle.Priority;
import com.elastica.beatle.TestSuiteDTO;
import com.elastica.beatle.Authorization.AuthorizationHandler;
import com.elastica.beatle.RestClient.ClientUtil;
import com.elastica.beatle.S3Utils.S3ActionHandler;
import com.elastica.beatle.constants.FrameworkConstants;
import com.elastica.beatle.dci.DCICommonTest;
import com.elastica.beatle.dci.DCIConstants;
import com.elastica.beatle.dci.DCIFunctions;
import com.elastica.beatle.dci.SaasType;
import com.elastica.beatle.dci.TestParameters;
import com.elastica.beatle.dci.dto.ciq.CIQProfile;
import com.elastica.beatle.dci.dto.ciq.create.CreateCIQProfile;
import com.elastica.beatle.dci.dto.dictionary.Dictionary;
import com.elastica.beatle.dci.dto.tp.TrainingProfile;
import com.elastica.beatle.es.ElasticSearchLogs;
import com.elastica.beatle.fileHandler.FileHandlingUtils;
import com.elastica.beatle.logger.Logger;
import com.universal.common.UniversalApi;
import com.universal.dtos.UserAccount;

import net.sf.json.JSONArray;
import net.sf.json.util.JSONTokener;


public class DCICIQProfileGranularTests extends DCICommonTest{

	DCIFunctions dciFunctions = null;
	ElasticSearchLogs esLogs = null;
	Map<String,String> folderInfo = new HashMap<String,String>();
	String uniqueId = UUID.randomUUID().toString();
	boolean mlProcessedFlag = false;
	int mainCounter = 0;int testCounter = 0;
	TestSuiteDTO suiteData2 = new TestSuiteDTO();

	String[] ciqType = {"PreDefTerms","CustomDict","MLProfile"};
	String[] ciqProfileFileName = {"US_License_Plate_Number.txt","Custom_Dictionaries.txt","VA_1040_1.txt"};
	String[] ciqProfileName = {"DCI_USALPN","DCI_CUSTOM_DICT","DCI_1040"};
	String[] ciqProfileKeyword = {"","dci_custom_dictionaries",""};
	String[] ciqProfileCount = {"1", "1", "1"};
	String[] ciqProfileType = {"US License Plate Numbers","DCI_CUSTOM_DICTIONARIES","1040"};
	String trainingProfileName = "1040";

	/**********************************************TEST METHODS***********************************************/

	@Test(groups ={"Granular"})
	public void testCIQProfileOfTenantAIsNotVisibleForTenantB() throws Exception {
		dciFunctions = new DCIFunctions();

		int count1Before = dciFunctions.getCIQProfileCount(restClient,suiteData);
		int count2Before = dciFunctions.getCIQProfileCount(restClient,suiteData2);		

		String ciqTerm = "US License Plate Numbers";
		String ciqTermProfileName = ciqTerm+" Name"; 
		String ciqTermProfileDescription = ciqTerm+" Description";

		dciFunctions.createCIQPredefinedTerms(restClient, suiteData, ciqTerm, ciqTermProfileName,
				ciqTermProfileDescription, "high", 1, true, 1, false);

		HttpResponse response1 = dciFunctions.listContentIQProfile(restClient, dciFunctions.getCookieHeaders(suiteData), 
				suiteData.getScheme(), suiteData.getHost());
		HttpResponse response2 = dciFunctions.listContentIQProfile(restClient, dciFunctions.getCookieHeaders(suiteData2), 
				suiteData2.getScheme(), suiteData2.getHost());

		String responseBody1 =  ClientUtil.getResponseBody(response1);
		String responseBody2 =  ClientUtil.getResponseBody(response2);

		CIQProfile ciqProfile1 = dciFunctions.unmarshall(responseBody1, 
				CIQProfile.class);
		CIQProfile ciqProfile2 = dciFunctions.unmarshall(responseBody2, 
				CIQProfile.class);

		int count1 = ciqProfile1.getData().getMeta().getTotalCount();
		int count2 = ciqProfile2.getData().getMeta().getTotalCount();

		List<com.elastica.beatle.dci.dto.ciq.Object> object1 = ciqProfile1.getData().getObjects();
		List<com.elastica.beatle.dci.dto.ciq.Object> object2 = ciqProfile2.getData().getObjects();

		List<Integer> c1 = Arrays.asList(count1,object1.size(),count1Before+1);
		List<Integer> c2 = Arrays.asList(count2,object2.size(),count2Before);

		List<String> ciqTermProfileNames = dciFunctions.getAllCIQProfileNames(responseBody1);

		Assert.assertEquals(dciFunctions.numberComparison(c1), "",
				"CIQ profile count mismatching for Tenant A Expected: "+(count1Before+1)+" and Actual:"+count1+" & "+object1.size());
		Assert.assertEquals(dciFunctions.numberComparison(c2), "",
				"CIQ profile count mismatching for Tenant B Expected: "+count2Before+" and Actual:"+count2+" & "+object2.size());
		Assert.assertTrue(dciFunctions.checkValueExistsInList(ciqTermProfileNames, ciqTermProfileName),
				"Profile Name:"+ciqTermProfileName+" is not seen for Tenant A");
	}

	@Test(groups ={"Granular"})
	public void testDictionaryOfTenantAIsNotVisibleForTenantB() throws Exception {
		dciFunctions = new DCIFunctions();

		int count1Before = dciFunctions.getDictionaryCount(restClient,suiteData);
		int count2Before = dciFunctions.getDictionaryCount(restClient,suiteData2);	

		String dictionaryTerm = "DCI Custom Dictionaries Only";
		String dictionaryTermProfileName = dictionaryTerm+" Name"; 
		String dictionaryTermProfileDescription = dictionaryTerm+" Description";

		List<String> keywords = new ArrayList<String>();keywords.add(dictionaryTerm);
		dciFunctions.createDictionary(restClient, suiteData, dictionaryTermProfileName, dictionaryTermProfileDescription, 
				null, keywords, dciFunctions.getCookieHeaders(suiteData));

		HttpResponse response1 = dciFunctions.listDictionary(restClient, dciFunctions.getCookieHeaders(suiteData), 
				suiteData.getScheme(), suiteData.getHost());
		HttpResponse response2 = dciFunctions.listDictionary(restClient, dciFunctions.getCookieHeaders(suiteData2), 
				suiteData2.getScheme(), suiteData2.getHost());

		String dictionaryJson1 = ClientUtil.getResponseBody(response1);
		String dictionaryJson2 = ClientUtil.getResponseBody(response2);

		Dictionary dictionary1 = dciFunctions.unmarshall(dictionaryJson1, 
				Dictionary.class);
		Dictionary dictionary2 = dciFunctions.unmarshall(dictionaryJson2, 
				Dictionary.class);

		int count1 = dictionary1.getMeta().getTotalCount();
		int count2 = dictionary2.getMeta().getTotalCount();

		List<com.elastica.beatle.dci.dto.dictionary.Object> object1 = dictionary1.getObjects();
		List<com.elastica.beatle.dci.dto.dictionary.Object> object2 = dictionary2.getObjects();

		List<Integer> c1 = Arrays.asList(count1,object1.size(),count1Before+1);
		List<Integer> c2 = Arrays.asList(count2,object2.size(),count2Before);

		Assert.assertEquals(dciFunctions.numberComparison(c1), "",
				"Dictionary count mismatching for Tenant A Expected: "+(count1Before+1)+" and Actual:"+count1+" & "+object1.size());
		Assert.assertEquals(dciFunctions.numberComparison(c2), "",
				"Dictionary count mismatching for Tenant B Expected: "+count2Before+" and Actual:"+count2+" & "+object2.size());
		Assert.assertTrue(dciFunctions.getAllDictionaryNames(dictionaryJson1).contains(dictionaryTermProfileName),
				"Dictionary Name "+dictionaryTermProfileName+" is not seen for Tenant A, Actual:"+
						Arrays.asList(dciFunctions.getAllDictionaryNames(dictionaryJson1))		
				);
	}

	@Test(groups ={"Granular"})
	public void testTrainingProfilesOfTenantAIsNotVisibleForTenantB() throws Exception {
		dciFunctions = new DCIFunctions();

		int count1Before = dciFunctions.getTrainingProfileCount(restClient,suiteData);
		int count2Before = dciFunctions.getTrainingProfileCount(restClient,suiteData2);	

		dciFunctions.createTrainingProfile(restClient, suiteData, trainingProfileName);

		HttpResponse response1 = dciFunctions.listTrainingProfiles(restClient, dciFunctions.getCookieHeaders(suiteData), 
				suiteData.getScheme(), suiteData.getHost());
		HttpResponse response2 = dciFunctions.listTrainingProfiles(restClient, dciFunctions.getCookieHeaders(suiteData2), 
				suiteData2.getScheme(), suiteData2.getHost());

		String tpJson1 = ClientUtil.getResponseBody(response1);
		String tpJson2 = ClientUtil.getResponseBody(response2);

		TrainingProfile tp1 = dciFunctions.unmarshall(tpJson1, 
				TrainingProfile.class);
		TrainingProfile tp2 = dciFunctions.unmarshall(tpJson2, 
				TrainingProfile.class);

		int count1 = tp1.getData().getMeta().getTotalCount();
		int count2 = tp2.getData().getMeta().getTotalCount();

		List<com.elastica.beatle.dci.dto.tp.Object> object1 = tp1.getData().getObjects();
		List<com.elastica.beatle.dci.dto.tp.Object> object2 = tp2.getData().getObjects();

		List<Integer> c1 = Arrays.asList(count1,object1.size(),count1Before+1);
		List<Integer> c2 = Arrays.asList(count2,object2.size(),count2Before);

		Assert.assertEquals(dciFunctions.numberComparison(c1), "",
				"Training Profile count mismatching for Tenant A Expected: "+(count1Before+1)+" and Actual:"+count1+" & "+object1.size());
		Assert.assertEquals(dciFunctions.numberComparison(c2), "",
				"Training Profile count mismatching for Tenant B Expected: "+count2Before+" and Actual:"+count2+" & "+object2.size());
		Assert.assertTrue(dciFunctions.getAllTrainingProfileNames(tpJson1).contains(trainingProfileName),
				"Training Profile Name "+trainingProfileName+" is not seen for Tenant A, Actual:"+
						Arrays.asList(dciFunctions.getAllTrainingProfileNames(tpJson1))		
				);
	}

	@Priority(1)
	@Test(groups ={"E2E"})
	public void testPredefinedTermsRisksForAFile() throws Exception {
		fileUploadCIQToSaasApp();

		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();

		String fileName = ciqProfileFileName[0];
		String profileName = ciqProfileName[0];
		String profileType = ciqProfileType[0];
		String profileKeyword = ciqProfileKeyword[0];
		String count = ciqProfileCount[0];
		String type = ciqType[0];
		String saasType = SaasType.getSaasFilterType(suiteData.getSaasApp());

		Logger.info(
				"************************************ Starting the checking of ContentIQ Violations risks"
						+ " for filename:" + profileName + " with predefined terms risk "+profileType+
						" and saas app type:" + saasType + " ******************");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description: Create the ContentIQ Profile with a predefined terms,"
				+ "Upload CIQ predefined terms risk file and wait for few minutes, Verify the "
				+ "risks is generated for the respective file");
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output****************");
		Logger.info("Filename:"+fileName);
		Logger.info("Content IQ Profile Name:"+profileName);Logger.info("Content IQ Profile Type:"+profileType);
		Logger.info("Risk Type:ContentIQ Violations");
		Logger.info("Saas App Type:"+suiteData.getSaasApp());
		Logger.info("*****************************************************");


		verifyRiskTypesDisplayLogs(dciFunctions, suiteData, fileName, saasType, dciFunctions.getBasicHeaders(suiteData), 
				profileName, profileType, profileKeyword, count, type, null);
		verifyDisplayLogsNegative(dciFunctions, suiteData2, fileName, saasType, dciFunctions.getBasicHeaders(suiteData2));

		Logger.info(
				"************************************ Completed the checking of ContentIQ Violations risks"
						+ " for filename:" + profileName + " with predefined terms risk "+profileType+
						" and saas app type:" + saasType + " ******************");
	}

	@Priority(2)
	@Test(groups ={"E2E"})
	public void testCustomDictionaryRisksForAFile() throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();

		String fileName = ciqProfileFileName[1];
		String profileName = ciqProfileName[1];
		String profileType = ciqProfileType[1];
		String profileKeyword = ciqProfileKeyword[1];
		String count = ciqProfileCount[1];
		String type = ciqType[1];
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

		verifyRiskTypesDisplayLogs(dciFunctions, suiteData, fileName, saasType, dciFunctions.getBasicHeaders(suiteData),
				profileName, profileType, profileKeyword, count, type, null);
		verifyDisplayLogsNegative(dciFunctions, suiteData2, fileName, saasType, dciFunctions.getBasicHeaders(suiteData2));

		Logger.info(
				"************************************ Completed the checking of ContentIQ Violations risks"
						+ " for filename:" + profileName + " with custom dictionary risk "+profileType+
						" and saas app type:" + saasType + " ******************");
	}

	@Priority(3)
	@Test(groups ={"E2E"})
	public void testTrainingProfileRisksForAFile() throws Exception {
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
						+ " for filename:" + profileName + " with training profile risk "+profileType+
						" and saas app type:" + saasType + " ******************");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description: Create the ContentIQ Profile with a training profile,"
				+ "Upload CIQ training profile risk file and wait for few minutes, Verify the "
				+ "risks is generated for the respective file");
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output****************");
		Logger.info("Filename:"+fileName);
		Logger.info("Content IQ Profile Name:"+profileName);Logger.info("Content IQ Profile Type:"+profileType);
		Logger.info("Risk Type:ContentIQ Violations");
		Logger.info("Saas App Type:"+suiteData.getSaasApp());
		Logger.info("*****************************************************");
	
		Assert.assertTrue(mlProcessedFlag, "Training profile:"+profileType+ " did not get processed in 10 minutes");
		verifyRiskTypesDisplayLogs(dciFunctions, suiteData, fileName, saasType, dciFunctions.getBasicHeaders(suiteData), 
				profileName, profileType, profileKeyword, count, type, trainingProfileName);
		verifyDisplayLogsNegative(dciFunctions, suiteData2, fileName, saasType, dciFunctions.getBasicHeaders(suiteData2));

		Logger.info(
				"************************************ Completed the checking of ContentIQ Violations risks"
						+ " for filename:" + profileName + " with training profile risk "+profileType+
						" and saas app type:" + saasType + " ******************");
	}

	@Priority(4)
	//@Test(dataProvider = "ciqForeignLanguage", groups ={"Granular"})
	public void testCreationOfCustomTermsInContentIQProfile(TestParameters testParams) throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();

		String testDescription = testParams.getTestDescription();
		String profileName = testParams.getContentIQProfileName();
		HttpResponse response = testParams.getResponse();

		Logger.info(
				"************************************ Starting the checking in creation of ContentIQ Profile:"
						+profileName+" ******************");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description: "+testDescription);
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output****************");
		Logger.info("Content IQ Profile Name:"+profileName);
		Logger.info("Expected Output: 200 OK");
		Logger.info("*****************************************************");

		if(StringUtils.isBlank(response.toString())){
			Logger.info("Creation of CIQ Profile:"+profileName+" failed");
			Assert.assertNotNull(response,"Creation of CIQ Profile failed");
		}else{
			String responseBody =  ClientUtil.getResponseBody(response);
			Logger.info("Actual Output:"+response.getStatusLine());
			Logger.info("Response Body:"+responseBody);

			try{
				CreateCIQProfile ciqProfile = dciFunctions.unmarshall(responseBody, 
						CreateCIQProfile.class);
				String validationMessage="";
				validationMessage +=(ciqProfile.getKey().equalsIgnoreCase("ciq")) ? "" : 
					"Key field in create CIQ json is mismatching Expecting ciq but was " + ciqProfile.getKey()+"\n";
				validationMessage +=(ciqProfile.getCiq().size()==0) ? "" : 
					"CIQ field in create CIQ json is mismatching Expecting no values but was " + ciqProfile.getCiq().toString() +"\n";
				validationMessage +=(ciqProfile.getActionStatus() == true) ? "" : 
					"Action Status field in create CIQ json is mismatching Expecting true but was " + ciqProfile.getActionStatus()+"\n";
				validationMessage +=(StringUtils.isBlank(ciqProfile.getApiMessage())) ? "" : 
					"Api Message field in create CIQ json is mismatching Expecting no message but was " + ciqProfile.getApiMessage()+"\n";

				Assert.assertEquals(validationMessage, "", 
						"Output Response Validation is failing for CIQ profile name:"+profileName);
			}catch(Exception e){
				Logger.info("Creation of CIQ Profile:"+profileName+" failed");
				Assert.fail("Creation of CIQ Profile:"+profileName+" failed");
			}

		}

		Logger.info(
				"************************************ Completed the checking in creation of ContentIQ Profile:"
						+profileName+" ******************");
	}


	/**********************************************TEST METHODS***********************************************/
	/**********************************************DATA PROVIDERS*********************************************/

	@DataProvider(name = "ciqForeignLanguage")
	public Object[][] ciqForeignLanguage() throws Exception {
		dciFunctions = new DCIFunctions();
		//List<NameValuePair> headers = dciFunctions.getCookieHeaders(suiteData);
		List<NameValuePair> headers = dciFunctions.getBrowserHeaders(suiteData);

		String[] ciqProfileNames = {
				"Sample Content","1234567890","~!@#$%^&*()_+{}|:<>?",
				"Sample_123_*@#","Sample_6789","Sample_*@#","450_*@#",
				"المحتوى عينة","١٢٣٤٥٦٧٨٩٠","المحتوى عينة ١٢٣٤٥٦٧٨٩٠",
				"مخلصی",
				"重新定义",
				"重新定義",
				"นิยามใหม่",
				"エクスチェンジ",
				"교환할",
				"herdefiniëren",
				"la redéfinition",
				"Schwyzerdütsch",
				"Πολυγλωσσικό περιεχόμενο",
				"नमूना सामग्री",
				"scambiabilità",
				"giełda",
				"intercâmbio",
				"Обмен",
				"amortización",
		};


		Object[][] result = new Object[ciqProfileNames.length][];

		for (int i = 0; i < ciqProfileNames.length; i++) {
			List<String> valuesCustomTerms = new ArrayList<String>();valuesCustomTerms.add(ciqProfileNames[i]);
			String entity = dciFunctions.getSearchQueryForCIQ(ciqProfileNames[i], "Description", "high", 1, true, 1, false, null, 
					null, null, valuesCustomTerms , null, null, null, null);
			HttpResponse response = dciFunctions.createContentIqProfile(restClient, suiteData, headers, ciqProfileNames[i], entity);

			result[i] = new Object[] { new TestParameters("Test the creation of CIQ profile with name:"+ciqProfileNames[i],
					"Create CIQ Profile with name:"+ciqProfileNames[i]+" with custom regex terms. Then CIQ profile is getting generated",
					ciqProfileNames[i], response)};
		}
		return result;
	}

	/**********************************************DATA PROVIDERS*********************************************/
	/**********************************************UTIL METHODS***********************************************/

	public void fileUploadCIQToSaasApp() throws Exception {
		dciFunctions = new DCIFunctions();

		List<NameValuePair> headers = dciFunctions.getBrowserHeaders(suiteData);
		List<String> keywords = new ArrayList<String>();
		keywords.add("DCI_CUSTOM_DICTIONARIES");
		dciFunctions.createDictionary(restClient, suiteData, "DCI_CUSTOM_DICTIONARIES", "DCI Description", null, keywords,
				headers);
		dciFunctions.createTrainingProfile(restClient, suiteData, trainingProfileName);
		String[] mlProfileNames = {trainingProfileName};
		mlProcessedFlag = dciFunctions.checkIfAllMLProfilesAreProcessed(suiteData, restClient, mlProfileNames, 20);
		dciFunctions.activateDeactivateMLProfile(suiteData, restClient, trainingProfileName, true);

		for(int i=0;i<ciqType.length;i++){
			dciFunctions.createContentIqProfile(restClient, suiteData, ciqType[i], 
					ciqProfileName[i], ciqProfileName[i]+" Description", ciqProfileType[i], false);
		}

		ciqProfileFileName = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_CIQ_EXPOSURE_PATH,
				ciqProfileFileName);
		dciFunctions.fileUploadOrSendEmail(suiteData, folderInfo, ciqProfileFileName);

		mainCounter = 0;testCounter = DCIConstants.DCI_COUNTER_MAX;

	}

	private void verifyRiskTypesDisplayLogs(DCIFunctions dciFunctions, TestSuiteDTO suiteData, String fileName, String saasType, List<NameValuePair> headers,
			String ciqProfileName, String ciqProfileType, String ciqProfileKeyword, String ciqProfileCount, String ciqType, String ciqTrainingProfile)
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

			Map<String, String> CIJson = new HashMap<String, String>();
			if(StringUtils.isBlank(ciqTrainingProfile)){
				CIJson = dciFunctions.
						populateContentInspectionJson(suiteData,fileName, riskCount, 
								"critical", "API", true, Arrays.asList(ciqProfileName.split(",")),  
								Arrays.asList(ciqProfileType.split(",")), Arrays.asList(ciqProfileKeyword.split(",")),  
								Arrays.asList(ciqProfileCount.split(",")), Arrays.asList(ciqType.split(",")));
			}else{
				CIJson = dciFunctions.
						populateContentInspectionJson(suiteData,fileName, riskCount, 
								"critical", "API", true, Arrays.asList(ciqProfileName.split(",")),  
								Arrays.asList(ciqProfileType.split(",")), Arrays.asList(ciqProfileKeyword.split(",")),  
								Arrays.asList(ciqProfileCount.split(",")), Arrays.asList(ciqType.split(",")), 
								Arrays.asList(ciqTrainingProfile.split(",")));
			}
			
			String validationMessage = dciFunctions.validateDCIRiskContentLogs
					(suiteData, hits, CIJson);

			Assert.assertEquals(validationMessage, "", "Output Response Validation is failing for fileName:"+fileName+
					" for saas app type:"+saasType);


		} finally {
			dciFunctions.cleanupFileFromTempFolder(fileName);
		}
	}

	private void verifyDisplayLogsNegative(DCIFunctions dciFunctions, TestSuiteDTO suiteData, String fileName, String saasType,
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
	 * Create folders in saas apps
	 */
	@BeforeClass(groups ={"E2E"})
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
	@AfterClass(groups ={"E2E"})
	public void deleteFolder() {
		try {
			UserAccount account = dciFunctions.getUserAccount(suiteData);
			UniversalApi universalApi = dciFunctions.getUniversalApi(suiteData, account);

			dciFunctions.deleteFolder(universalApi, suiteData, folderInfo);
		} catch (Exception ex) {
			Logger.info("Issue with Delete Folder Operation " + ex.getLocalizedMessage());
		}
	}


	@BeforeClass(alwaysRun=true)
	public void dataSetupTenant2(ITestContext suiteConfigurations) {
		try {
			suiteData2.setCloudSocUname(suiteConfigurations.getCurrentXmlTest().getParameter("cloudSocUName2"));
			suiteData2.setTenantName(suiteConfigurations.getCurrentXmlTest().getParameter("tenantName2"));
			suiteData2.setSaasApp(suiteConfigurations.getCurrentXmlTest().getParameter("saasApp"));
			suiteData2.setDomainName(suiteConfigurations.getCurrentXmlTest().getParameter("domainName2"));

			suiteData2.setHost(suiteData.getHost());
			suiteData2.setScheme(suiteData.getScheme());
			suiteData2.setEnvironmentName(suiteData.getEnvironmentName());
			suiteData2.setAPIMap(suiteData.getAPIMap());
			suiteData2.setBaseVersion(suiteData.getBaseVersion());
			suiteData2.setApiserverHostName(suiteData.getApiserverHostName());
			suiteData2.setReferer(suiteData.getReferer());

			Map<String, String> credentialsDBconfig = FileHandlingUtils.readPropertyFile(FrameworkConstants.FRAMEWORK_CREDENTIALDB_FILEPATH);
			suiteData2.setUsername(credentialsDBconfig.get(suiteData2.getTenantName().toLowerCase().concat(".")
					.concat(suiteData2.getEnvironmentName()).concat(".").concat(suiteData2.getCloudSocUname()).concat(".").concat("userName")));
			suiteData2.setPassword(StringEscapeUtils.unescapeXml(credentialsDBconfig.get(suiteData2.getTenantName().toLowerCase().concat(".")
					.concat(suiteData2.getEnvironmentName()).concat(".").concat(suiteData2.getCloudSocUname()).concat(".").concat("userPassword"))));
			suiteData2.setTenantToken(credentialsDBconfig.get(suiteData2.getTenantName().toLowerCase().concat(".")
					.concat("tenantToken")));
			suiteData2.setSaasAppUsername(credentialsDBconfig.get(suiteData2.getSaasApp().toLowerCase().concat(".")
					.concat(suiteData2.getTenantName().toLowerCase()).concat(".").concat("saasAppUsername")));
			suiteData2.setSaasAppPassword(StringEscapeUtils.unescapeXml(credentialsDBconfig.get(suiteData2.getSaasApp().toLowerCase().concat(".")
					.concat(suiteData2.getTenantName().toLowerCase()).concat(".").concat("saasAppPassword"))));
			suiteData2.setSaasAppUserRole(credentialsDBconfig.get(suiteData2.getSaasApp().toLowerCase().concat(".")
					.concat(suiteData2.getTenantName().toLowerCase()).concat(".").concat("saasAppUserRole")));
			suiteData2.setSaasAppToken(credentialsDBconfig.get(suiteData2.getSaasApp().toLowerCase().concat(".")
					.concat(suiteData2.getTenantName().toLowerCase()).concat(".").concat("saasAppToken")));		
			suiteData2.setSaasAppClientId(credentialsDBconfig.get(suiteData2.getSaasApp().toLowerCase().concat(".")
					.concat(suiteData2.getTenantName().toLowerCase()).concat(".").concat("saasAppClientId")));
			suiteData2.setSaasAppClientSecret(credentialsDBconfig.get(suiteData2.getSaasApp().toLowerCase().concat(".")
					.concat(suiteData2.getTenantName().toLowerCase()).concat(".").concat("saasAppClientSecret")));

			HttpResponse CSRFHeader = AuthorizationHandler.getCSRFHeaders(suiteData2.getUsername(),suiteData2.getPassword(), suiteData2);
			suiteData2.setCSRFToken(AuthorizationHandler.getCSRFToken(CSRFHeader));
			suiteData2.setSessionID(AuthorizationHandler.getUserSessionID(CSRFHeader));		



		} catch (Exception ex) {
			Logger.info("Issue with Enabling of Content Inspection" + ex.getLocalizedMessage());
		}
	}

	/**
	 * Delete content iq profile
	 * @throws Exception 
	 */
	@BeforeClass(groups ={"Granular","E2E"})
	public void deleteContentIqProfileBeforeTestStarts() {
		dciFunctions = new DCIFunctions();
		dciFunctions.deleteAllPolicies(restClient, suiteData);
		dciFunctions.deleteAllPolicies(restClient, suiteData2);

		dciFunctions.deleteAllCIQProfiles(restClient, suiteData);
		dciFunctions.deleteAllCIQProfiles(restClient, suiteData2);

		dciFunctions.deleteAllDictionaries(restClient, suiteData);
		dciFunctions.deleteAllDictionaries(restClient, suiteData2);

		dciFunctions.deleteAllTrainingProfiles(restClient, suiteData);
		dciFunctions.deleteAllTrainingProfiles(restClient, suiteData2);
	}

	/**
	 * Delete content iq profile
	 * @throws Exception 
	 */
	@AfterClass(groups ={"Granular","E2E"})
	public void deleteContentIqProfileAfterTestEnds() {
		dciFunctions = new DCIFunctions();

		dciFunctions.deleteAllPolicies(restClient, suiteData);
		dciFunctions.deleteAllPolicies(restClient, suiteData2);

		dciFunctions.deleteAllCIQProfiles(restClient, suiteData);
		dciFunctions.deleteAllCIQProfiles(restClient, suiteData2);

		dciFunctions.deleteAllDictionaries(restClient, suiteData);
		dciFunctions.deleteAllDictionaries(restClient, suiteData2);

		dciFunctions.deleteAllTrainingProfiles(restClient, suiteData);
		dciFunctions.deleteAllTrainingProfiles(restClient, suiteData2);
	}

	@BeforeSuite(alwaysRun=true)
	public void downloadFileFromS3() {
		try {
			S3ActionHandler s3 = new S3ActionHandler();
			s3.downloadWholeFolder(DCIConstants.DCI_S3_BUCKET, "DCI/TrainingProfiles/Categories/1040", 
					new File(DCIConstants.DCI_FILE_TEMP_PATH));
			s3.downloadWholeFolder(DCIConstants.DCI_S3_BUCKET, "DCI/CIQ/Exposure", 
					new File(DCIConstants.DCI_FILE_TEMP_PATH));

		} catch (Exception ex) {
			Logger.info("Downloading folder from S3 is failed with exception " + ex.getLocalizedMessage());
		}
	}

	/**********************************************BEFORE/AFTER METHODS/CLASS*****************************************/


}