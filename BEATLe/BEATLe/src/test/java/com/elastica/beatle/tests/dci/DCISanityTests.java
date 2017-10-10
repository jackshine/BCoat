package com.elastica.beatle.tests.dci;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.entity.StringEntity;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import com.elastica.beatle.Priority;
import com.elastica.beatle.TestSuiteDTO;
import com.elastica.beatle.RestClient.ClientUtil;
import com.elastica.beatle.S3Utils.S3ActionHandler;
import com.elastica.beatle.dci.DCICommonTest;
import com.elastica.beatle.dci.DCIConstants;
import com.elastica.beatle.dci.DCIFunctions;
import com.elastica.beatle.dci.SaasType;
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

public class DCISanityTests extends DCICommonTest{

	DCIFunctions dciFunctions = null;
	ElasticSearchLogs esLogs = null;
	Map<String,String> folderInfo = new HashMap<String,String>();
	String uniqueId = UUID.randomUUID().toString();
	UserAccount account = null;	UniversalApi universalApi = null;
	int mainCounter = 0;int testCounter=0;

	String pciFileName="pci.txt";String healthFileName="health.txt";String trainingProfileFileName="VA_1040_1.txt";
	String trainingProfileName="1040";boolean mlProcessedFlag=false; boolean tpFlag=false; 

	/**********************************************TEST METHODS***********************************************/

	@Priority(1)
	@Test(groups ={"Sanity"})
	public void testPCIRiskTypesForAFile() throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();

		fileUploadToSaasApp();
		String risks="PCI";
		String saasType = SaasType.getSaasFilterType(suiteData.getSaasApp());

		Logger.info(
				"************************************ Starting the checking of PCI risk type"
						+ " for filename:" + pciFileName + " and saas app type:" + saasType + " ******************");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description: Upload PCI risk file and wait for few minutes, Verify the "
				+ "risk generated for the file type");
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output****************");
		Logger.info("Filename:"+pciFileName);
		Logger.info("Risk Types to be validated:"+risks);
		Logger.info("Saas App Type:"+suiteData.getSaasApp());
		Logger.info("*****************************************************");

		List<NameValuePair> headers = dciFunctions.getBasicHeaders(suiteData);		
		verifyRiskTypesDisplayLogs(dciFunctions, pciFileName, saasType, headers);

		Logger.info(
				"************************************ Completed the checking of PCI risk type"
						+ " for filename:" + pciFileName + " and saas app type:" + saasType + " ******************");

	}

	@Priority(2)
	@Test(groups ={"Sanity"})
	public void testHealthContentTypesForAFile() throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();

		String risks="health";
		String saasType = SaasType.getSaasFilterType(suiteData.getSaasApp());

		Logger.info(
				"************************************ Starting the checking of health content type"
						+ " for filename:" + healthFileName + " and saas app type:" + saasType + " ******************");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description: Upload health content file and wait for few minutes, Verify the "
				+ "content type generated for the file type");
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output****************");
		Logger.info("Filename:"+healthFileName);
		Logger.info("Content Types to be validated:"+risks);
		Logger.info("Saas App Type:"+suiteData.getSaasApp());
		Logger.info("*****************************************************");

		List<NameValuePair> headers = dciFunctions.getBasicHeaders(suiteData);		
		verifyContentTypesDisplayLogs(dciFunctions, healthFileName, saasType, headers);

		Logger.info(
				"************************************ Completed the checking of health content type"
						+ " for filename:" + healthFileName + " and saas app type:" + saasType + " ******************");

	}

	@Priority(3)
	@Test(groups ={"Sanity"})
	public void testTrainingProfileContentIQViolationsRisksForAFile() throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();

		String profileName = trainingProfileName+" Name";
		String profileType = trainingProfileName;
		String profileKeyword = "";
		String count = "1";
		String type = "MLProfile";
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
		Logger.info("Filename:"+trainingProfileFileName);
		Logger.info("Content IQ Profile Name:"+profileName);
		Logger.info("Training Profile Name:"+profileType);
		Logger.info("Risk Type:ContentIQ Violations");
		Logger.info("Saas App Type:"+suiteData.getSaasApp());
		Logger.info("*****************************************************");

		Assert.assertTrue(tpFlag, "Failed to create training profile:"+profileType);
		Assert.assertTrue(mlProcessedFlag, "Training profile:"+profileType+ " did not get processed in 15 minutes");

		verifyRiskTypesDisplayLogs(dciFunctions, suiteData, trainingProfileFileName, saasType, dciFunctions.getBasicHeaders(suiteData), 
				profileName, profileType, profileKeyword, count, type, trainingProfileName);

		Logger.info(
				"************************************ Completed the checking of ContentIQ Violations risks"
						+ " for filename:" + profileName + " with training profile risk "+profileType+
						" and saas app type:" + saasType + " ******************");
	}


	/**********************************************TEST METHODS***********************************************/
	/**********************************************UTIL METHODS***********************************************/

	private void verifyRiskTypesDisplayLogs(DCIFunctions dciFunctions, String fileName, String saasType, List<NameValuePair> headers)
			throws Exception{
		String hits = "";

		try {

			String payload = 
					dciFunctions.getSearchQueryForDCI(suiteData, dciFunctions.getMinusMinutesFromCurrentTime(1440) , 
							dciFunctions.getPlusMinutesFromCurrentTime(120), saasType, DCIConstants.CISourceType, 
							DCIConstants.CICriticalSeverityType, fileName, DCIConstants.CIFacilityType, DCIConstants.CIActivityType);

			Logger.info("****************Input Payload****************");
			Logger.info(payload);
			Logger.info("*********************************************");

			hits = fetchDisplayLogsCounter(dciFunctions, headers, payload, DCIConstants.DCI_COUNTER_MEDIUM, 1);

			Logger.info("****************Output Response****************");
			Logger.info(hits);
			Logger.info("***********************************************");

			Map<String, String> CIJson = dciFunctions.
					populateContentInspectionJson(suiteData,fileName, 1, 
							"critical", "API", false);

			String validationMessage = dciFunctions.validateDCIRiskContentLogs
					(suiteData, hits, CIJson);

			Assert.assertEquals(validationMessage, "", "Output Response Validation is failing for fileName:"+fileName+
					" for saas app type:"+saasType);


		} finally {
			dciFunctions.cleanupFileFromTempFolder(fileName);
		}
	}



	private void verifyContentTypesDisplayLogs(DCIFunctions dciFunctions, String fileName, String saasType, List<NameValuePair> headers)
			throws Exception{
		String hits = "";

		try {

			String payload = 
					dciFunctions.getSearchQueryForDCI(suiteData, dciFunctions.getMinusMinutesFromCurrentTime(1440) , 
							dciFunctions.getPlusMinutesFromCurrentTime(120), saasType, DCIConstants.CISourceType, 
							DCIConstants.CIInformationalSeverityType, fileName, DCIConstants.CIFacilityType, DCIConstants.CIActivityType);

			Logger.info("****************Input Payload****************");
			Logger.info(payload);
			Logger.info("*********************************************");

			hits = fetchDisplayLogsCounter(dciFunctions, headers, payload, DCIConstants.DCI_COUNTER_MEDIUM, 1);

			Logger.info("****************Output Response****************");
			Logger.info(hits);
			Logger.info("***********************************************");

			Map<String, String> CIJson = dciFunctions.
					populateContentInspectionJson(suiteData,fileName, 1, 
							"informational", "API", false);

			String validationMessage = dciFunctions.validateDCIRiskContentLogs
					(suiteData, hits, CIJson);

			Assert.assertEquals(validationMessage, "", "Output Response Validation is failing for fileName:"+fileName+
					" for saas app type:"+saasType);


		} finally {
			dciFunctions.cleanupFileFromTempFolder(fileName);
		}
	}

	private void verifyRiskTypesDisplayLogs(DCIFunctions dciFunctions, TestSuiteDTO suiteData, String fileName, String saasType, List<NameValuePair> headers,
			String ciqProfileName, String ciqProfileType, String ciqProfileKeyword, String ciqProfileCount, String ciqType, String ciqTrainingProfile)
					throws Exception{
		String hits = "";

		try {

			String payload = 
					dciFunctions.getSearchQueryForDCI(suiteData, dciFunctions.getMinusMinutesFromCurrentTime(1440) , 
							dciFunctions.getPlusMinutesFromCurrentTime(120), saasType, DCIConstants.CISourceType, 
							DCIConstants.CICriticalSeverityType, fileName, DCIConstants.CIFacilityType, DCIConstants.CIActivityType);

			Logger.info("****************Input Payload****************");
			Logger.info(payload);
			Logger.info("*********************************************");

			hits = fetchDisplayLogsCounter(dciFunctions, headers, payload, DCIConstants.DCI_COUNTER_MEDIUM, 1);

			Logger.info("****************Output Response****************");
			Logger.info(hits);
			Logger.info("***********************************************");

			Map<String, String> CIJson = new HashMap<String, String>();
			if(StringUtils.isBlank(ciqTrainingProfile)){
				CIJson = dciFunctions.
						populateContentInspectionJson(suiteData,fileName, 1, 
								"critical", "API", true, Arrays.asList(ciqProfileName.split(",")),  
								Arrays.asList(ciqProfileType.split(",")), Arrays.asList(ciqProfileKeyword.split(",")),  
								Arrays.asList(ciqProfileCount.split(",")), Arrays.asList(ciqType.split(",")));
			}else{
				CIJson = dciFunctions.
						populateContentInspectionJson(suiteData,fileName, 1, 
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

		if(mainCounter>=DCIConstants.DCI_COUNTER_MAX){
			testCounter=DCIConstants.DCI_COUNTER_LL;
		}
		return dciFunctions.getJSONValue(responseBody, "hits");
	}




	public void fileUploadToSaasApp() throws Exception {
		String[] mlProfileNames = {trainingProfileName};

		Logger.info("******************** Creating training profile:"+trainingProfileName+
				" and CIQ profile:"+trainingProfileName+" Name is in progress ********************");
		try{
			try{dciFunctions.createTrainingProfile(restClient, suiteData, trainingProfileName);
			}catch(Exception e){tpFlag = false;}
			try{mlProcessedFlag = dciFunctions.checkIfAllMLProfilesAreProcessed(suiteData, restClient, mlProfileNames, 15);
			tpFlag = true;
			}catch(Exception e){tpFlag = false;}
			try{dciFunctions.activateDeactivateMLProfile(suiteData, restClient, trainingProfileName, true);
			}catch(Exception e){tpFlag = false;}
			try{dciFunctions.createContentIqProfile(restClient, suiteData, "MLProfile", 
					trainingProfileName+" Name", trainingProfileName+" Description", trainingProfileName, false);
			}catch(Exception e){tpFlag = false;}
			tpFlag = true;
			Logger.info("******************** Creating training profile:"+trainingProfileName+
					" and CIQ profile:"+trainingProfileName+" Name is completed ********************");
		}catch(Exception e){
			tpFlag = false;
			Logger.info("******************** Creating training profile:"+trainingProfileName+
					" and CIQ profile:"+trainingProfileName+" Name is failed ********************");
		}

		if(mlProcessedFlag){
			tpFlag = true;
		}


		Logger.info("******************** Uploading file into SaasApp is in progress ********************");

		dciFunctions = new DCIFunctions();

		pciFileName = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_SANITY_PATH,
				pciFileName);
		healthFileName = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_SANITY_PATH,
				healthFileName);
		trainingProfileFileName = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_SANITY_PATH,
				trainingProfileFileName);

		dciFunctions.uploadFile(universalApi,suiteData, folderInfo.get("folderId"), pciFileName);
		dciFunctions.uploadFile(universalApi,suiteData, folderInfo.get("folderId"), healthFileName);
		dciFunctions.uploadFile(universalApi,suiteData, folderInfo.get("folderId"), trainingProfileFileName);


		mainCounter = 0;testCounter = DCIConstants.DCI_COUNTER_MEDIUM;

		Logger.info("******************** Uploading file into SaasApp is completed ********************");
	}

	/**********************************************UTIL METHODS***********************************************/
	/**********************************************BEFORE/AFTER CLASS*****************************************/

	/**
	 * Delete all CIQ profiles
	 * @throws Exception 
	 */
	@BeforeClass(groups ={"Sanity"})
	public void deleteAllCIQProfiles() {
		dciFunctions.deleteAllPolicies(restClient, suiteData);
		dciFunctions.deleteAllCIQProfiles(restClient, suiteData);
		dciFunctions.deleteAllDictionaries(restClient, suiteData);
		dciFunctions.deleteAllTrainingProfiles(restClient, suiteData);
	}

	/**
	 * Create folders in saas apps
	 */
	@BeforeClass(groups ={"Sanity"})
	public void createFolder() {
		dciFunctions = new DCIFunctions();
		try {
			account = dciFunctions.getUserAccount(suiteData);
			universalApi = dciFunctions.getUniversalApi(suiteData, account);

			folderInfo = dciFunctions.createFolder(universalApi, suiteData, 
					DCIConstants.DCI_FOLDER+uniqueId);
		} catch (Exception ex) {
			Logger.info("Issue with Create Folder Operation " + ex.getLocalizedMessage());
		}
	}

	/**
	 * Delete folders in saas apps
	 */
	@AfterClass(groups ={"Sanity"})
	public void deleteFolder() {
		try {
			dciFunctions.deleteFolder(universalApi, suiteData, folderInfo);
		} catch (Exception ex) {
			Logger.info("Issue with Delete Folder Operation " + ex.getLocalizedMessage());
		}
	}

	@BeforeSuite(alwaysRun=true)
	public void downloadFileFromS3() {
		try {
			S3ActionHandler s3 = new S3ActionHandler();
			s3.downloadWholeFolder(DCIConstants.DCI_S3_BUCKET, "DCI/TrainingProfiles/Categories/1040", 
					new File(DCIConstants.DCI_FILE_TEMP_PATH));
		} catch (Exception ex) {
			Logger.info("Downloading folder from S3 is failed with exception " + ex.getLocalizedMessage());
		}
	}

	/**********************************************BEFORE/AFTER CLASS*****************************************/


}
