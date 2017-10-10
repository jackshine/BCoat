package com.elastica.beatle.tests.dci;

import java.io.File;
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
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.elastica.beatle.Priority;
import com.elastica.beatle.RestClient.ClientUtil;
import com.elastica.beatle.S3Utils.S3ActionHandler;
import com.elastica.beatle.dci.DCICommonTest;
import com.elastica.beatle.dci.DCIConstants;
import com.elastica.beatle.dci.DCIFunctions;
import com.elastica.beatle.dci.SaasType;
import com.elastica.beatle.dci.TestParameters;
import com.elastica.beatle.es.ElasticSearchLogs;
import com.elastica.beatle.logger.Logger;
import com.universal.common.GoogleMailServices;
import com.universal.common.Office365MailActivities;
import com.universal.common.UniversalApi;
import com.universal.dtos.UserAccount;

import net.sf.json.JSONArray;
import net.sf.json.util.JSONTokener;

/**
 * 
 * @author eldorajan
 *
 */

public class DCISaaSCIQProfileTests extends DCICommonTest{

	DCIFunctions dciFunctions = null;
	ElasticSearchLogs esLogs = null;
	Map<String,String> folderInfo = new HashMap<String,String>();
	String uniqueId = UUID.randomUUID().toString();
	List<String> uploadId = new ArrayList<String>();  
	Office365MailActivities objMail = null;
	GoogleMailServices gobjMail=null;

	int mainCounter = 0;int testCounter=0;

	String[] ciqType = {"PreDefDict","PreDefTerms","CustomDict","CustomTerms"};
	String[] ciqProfileFileName = {"Diseases.txt","US_License_Plate_Number.txt","Custom_Dictionaries.txt","Custom_Terms.txt"};
	String[] ciqProfileName = {"DCI_DIS","DCI_USALPN","DCI_CUSTOM_DICT","DCI_CUSTOM_TERMS"};
	String[] ciqProfileKeyword = {"leukemia","","dci_custom_dictionaries",""};
	String[] ciqProfileCount = {"1", "1", "1", "1"};
	String[] ciqProfileType = {"Diseases","US License Plate Numbers","DCI_CUSTOM_DICTIONARIES","DCI_CUSTOM_TERMS"};
	
	/**********************************************TEST METHODS***********************************************/

	public void fileUploadCIQToSaasApp() throws Exception {
		dciFunctions = new DCIFunctions();

		List<NameValuePair> headers = dciFunctions.getBrowserHeaders(suiteData);
		List<String> keywords = new ArrayList<String>();
		keywords.add("DCI_CUSTOM_DICTIONARIES");
		dciFunctions.createDictionary(restClient, suiteData, "DCI_CUSTOM_DICTIONARIES", "DCI Description", null, keywords,
				headers);
		
		for(int i=0;i<ciqType.length;i++){
			dciFunctions.createContentIqProfile(restClient, suiteData, ciqType[i], 
					ciqProfileName[i], ciqProfileName[i]+" Description", ciqProfileType[i], false);
		}
		ciqProfileFileName = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_CIQ_EXPOSURE_PATH, ciqProfileFileName);
		
		uploadId = dciFunctions.fileUploadOrSendEmail(suiteData, folderInfo, ciqProfileFileName);

		mainCounter = 0;testCounter = DCIConstants.DCI_COUNTER_MAX;

	}
	
	
	@Priority(1)
	@Test(groups ={"All","Attachment","Body","AttachmentBody"})
	public void testPredefinedDictionaryRisksForAFile() throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();

		fileUploadCIQToSaasApp();

		String fileName = ciqProfileFileName[0];
		String profileName = ciqProfileName[0];
		String profileType = ciqProfileType[0];
		String profileKeyword = ciqProfileKeyword[0];
		String count = ciqProfileCount[0];
		String type = ciqType[0];
		String saasType = SaasType.getSaasFilterType(suiteData.getSaasApp());

		Logger.info(
				"************************************ Starting the checking of ContentIQ Violations risks"
						+ " for filename:" + profileName + " with predefined dictionary risk "+profileType+
						" and saas app type:" + saasType + " ******************");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description: Create the ContentIQ Profile with a predefined dictionary,"
				+ "Upload CIQ predefined dictionary risk file and wait for few minutes, Verify the "
				+ "risks is generated for the respective file");
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output****************");
		Logger.info("Filename:"+fileName);
		Logger.info("Content IQ Profile Name:"+profileName);Logger.info("Content IQ Profile Type:"+profileType);
		Logger.info("Risk Type:ContentIQ Violations");
		Logger.info("Saas App Type:"+suiteData.getSaasApp());
		Logger.info("*****************************************************");


		List<NameValuePair> headers = dciFunctions.getBasicHeaders(suiteData);		
		verifyRiskTypesDisplayLogs(dciFunctions, fileName, saasType, headers, profileName, profileType, profileKeyword, count, type);

		Logger.info(
				"************************************ Completed the checking of ContentIQ Violations risks"
						+ " for filename:" + profileName + " with predefined dictionary risk "+profileType+
						" and saas app type:" + saasType + " ******************");
	}

	@Priority(2)
	@Test(groups ={"All","Attachment","Body","AttachmentBody"})
	public void testPredefinedTermsRisksForAFile() throws Exception {
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


		List<NameValuePair> headers = dciFunctions.getBasicHeaders(suiteData);		
		verifyRiskTypesDisplayLogs(dciFunctions, fileName, saasType, headers, profileName, profileType, profileKeyword, count, type);
		
		Logger.info(
				"************************************ Completed the checking of ContentIQ Violations risks"
						+ " for filename:" + profileName + " with predefined terms risk "+profileType+
						" and saas app type:" + saasType + " ******************");
	}

	@Priority(3)
	@Test(groups ={"All","Attachment","Body","AttachmentBody"})
	public void testCustomDictionaryRisksForAFile() throws Exception {
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


		List<NameValuePair> headers = dciFunctions.getBasicHeaders(suiteData);		
		verifyRiskTypesDisplayLogs(dciFunctions, fileName, saasType, headers, profileName, profileType, profileKeyword, count, type);
		
		Logger.info(
				"************************************ Completed the checking of ContentIQ Violations risks"
						+ " for filename:" + profileName + " with custom dictionary risk "+profileType+
						" and saas app type:" + saasType + " ******************");
	}

	@Priority(4)
	@Test(groups ={"All","Attachment","Body","AttachmentBody"})
	public void testCustomTermsRisksForAFile() throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();
	
		String fileName = ciqProfileFileName[3];
		String profileName = ciqProfileName[3];
		String profileType = ciqProfileType[3];
		String profileKeyword = ciqProfileKeyword[3];
		String count = ciqProfileCount[3];
		String type = ciqType[3];
		String saasType = SaasType.getSaasFilterType(suiteData.getSaasApp());

		Logger.info(
				"************************************ Starting the checking of ContentIQ Violations risks"
						+ " for filename:" + profileName + " with custom terms risk "+profileType+
						" and saas app type:" + saasType + " ******************");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description: Create the ContentIQ Profile with a custom terms,"
				+ "Upload CIQ custom terms risk file and wait for few minutes, Verify the "
				+ "risks is generated for the respective file");
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output****************");
		Logger.info("Filename:"+fileName);
		Logger.info("Content IQ Profile Name:"+profileName);Logger.info("Content IQ Profile Type:"+profileType);
		Logger.info("Risk Type:ContentIQ Violations");
		Logger.info("Saas App Type:"+suiteData.getSaasApp());
		Logger.info("*****************************************************");


		List<NameValuePair> headers = dciFunctions.getBasicHeaders(suiteData);		
		verifyRiskTypesDisplayLogs(dciFunctions, fileName, saasType, headers, profileName, profileType, profileKeyword, count, type);
		
		Logger.info(
				"************************************ Completed the checking of ContentIQ Violations risks"
						+ " for filename:" + profileName + " with custom terms risk "+profileType+
						" and saas app type:" + saasType + " ******************");
	}
	
	@Priority(5)
	@Test(dataProvider = "dataUploadCustomTerms", groups ={"All","Attachment","Body","AttachmentBody"})
	public void testi18nStringForCustomTerms(TestParameters testParams) throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();
	
		String fileName = testParams.getFileName();
		String profileName = testParams.getCiq().get("ciqProfileName");
		String profileType = testParams.getCiq().get("ciqProfileType");
		String profileKeyword = testParams.getCiq().get("ciqProfileKeyword");
		String count = testParams.getCiq().get("ciqCount");
		String type = testParams.getCiq().get("ciqType");
		String saasType = SaasType.getSaasFilterType(suiteData.getSaasApp());

		Logger.info(
				"************************************ Completed the checking of ContentIQ Violations risks"
						+ " for filename:" + fileName + " with custom terms risk "+profileName+
						" and saas app type:" + saasType + " ******************");
		
		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description:"+testParams.getTestDescription());
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output****************");
		Logger.info("Filename:"+testParams.getFileName());
		Logger.info("Risk Type:"+Arrays.asList(testParams.getRisks()));
		Logger.info("CIQ Profile Name:"+testParams.getCiq().get("ciqProfileName"));
		Logger.info("CIQ Profile Keyword:"+testParams.getCiq().get("ciqProfileKeyword"));
		Logger.info("*****************************************************");

		List<NameValuePair> headers = dciFunctions.getBasicHeaders(suiteData);		
		verifyRiskTypesDisplayLogs(dciFunctions, fileName, saasType, headers, 
				profileName, profileType, profileKeyword, count, type);
		
		Logger.info(
				"************************************ Completed the checking of ContentIQ Violations risks"
						+ " for filename:" + fileName + " with custom terms risk "+profileName+
						" and saas app type:" + saasType + " ******************");
	}


	/**********************************************TEST METHODS***********************************************/
	/**********************************************DATA PROVIDERS*********************************************/

	@DataProvider(name = "dataUploadCustomTerms")
	public Object[][] dataUploadCustomTerms() {
		dciFunctions = new DCIFunctions();

		String ciqType = "CustomTerms";String ciqProfileCount = "1";
		String[] fileName = dciFunctions.getFileName(DCIConstants.DCI_FILE_UPLOAD_CIQ_i18n_PATH);
		String[] fileNameNoExtn = dciFunctions.getFileNameNoExtension(DCIConstants.DCI_FILE_UPLOAD_CIQ_i18n_PATH);
		String[] profileName = dciFunctions.appendArray(fileNameNoExtn,"DCI_");
		String[] profileDescription = dciFunctions.appendArray(fileNameNoExtn,"Description ");
		String[] profileText = dciFunctions.readFiles(DCIConstants.DCI_FILE_UPLOAD_CIQ_i18n_PATH);
		
		for (int i = 0; i < fileName.length; i++) {
			dciFunctions.createCIQCustomTerms(restClient, suiteData, profileText[i], 
					profileName[i], profileDescription[i], "high", 1, true, 1, false);
		}
		
		fileName = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_CIQ_i18n_PATH, fileName);
		
		uploadId = dciFunctions.fileUploadOrSendEmail(suiteData, folderInfo, fileName);
		
		Object[][] result = new Object[fileName.length][];
		for (int i = 0; i < fileName.length; i++) {
			
			Map<String,String> ciq = new HashMap<String,String>();
			ciq.put("ciqProfileName", profileName[i]);
			ciq.put("ciqProfileType", profileText[i]);
			ciq.put("ciqProfileKeyword", "");
			ciq.put("ciqType", ciqType);
			ciq.put("ciqCount", ciqProfileCount);
			
			result[i] = new Object[] { new TestParameters("Risk Generation/Validation for Custom Terms:"+profileText[i]+" in ContentIQ Profile:"+profileName[i]+" for file:"+fileName[i], 
					"Create CIQ Profile with name:"+profileName[i]+" with predefined terms:"+profileText[i]+" and upload file:"+fileName[i]+ ". Then verify risk logs are getting generated within the SLA provided",
					fileName[i], SaasType.getSaasFilterType(suiteData.getSaasApp()), dciFunctions.getCIQRisksTypes(fileName[i]), ciq)};
			
		}

		mainCounter = 0;testCounter = DCIConstants.DCI_COUNTER_MAX;

		return result;
	}

	/**********************************************DATA PROVIDERS*********************************************/
	/**********************************************UTIL METHODS***********************************************/

	private void verifyRiskTypesDisplayLogs(DCIFunctions dciFunctions, String fileName, String saasType, List<NameValuePair> headers,
			String ciqProfileName, String ciqProfileType, String ciqProfileKeyword, String ciqProfileCount, String ciqType)
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

			Map<String, String> CIJson = dciFunctions.
					populateContentInspectionJson(suiteData,fileName, riskCount, 
							"critical", "API", true, Arrays.asList(ciqProfileName.split(",")),  
							Arrays.asList(ciqProfileType.split(",")), Arrays.asList(ciqProfileKeyword.split(",")),  
							Arrays.asList(ciqProfileCount.split(",")), Arrays.asList(ciqType.split(",")));
			
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
	 * @throws Exception 
	 */
	//@BeforeSuite(alwaysRun=true)
	public void deleteAllCIQProfilesBeforeSuite() throws Exception {
		dciFunctions = new DCIFunctions();
		dciFunctions.cleanupAllTenants(restClient, suiteData);
	}
	
	/**
	 * Delete all CIQ profiles
	 * @throws Exception 
	 */
	@AfterSuite(alwaysRun=true)
	public void deleteAllCIQProfilesAfterSuite() throws Exception {
		dciFunctions = new DCIFunctions();
		dciFunctions.cleanupAllTenants(restClient, suiteData);
	}
	
	
	/**
	 * Create folders in saas apps
	 */
	@BeforeClass(groups ={"All"})
	public void createFolder() {
		dciFunctions = new DCIFunctions();
		try {
			if(suiteData.getSaasApp().equalsIgnoreCase("Salesforce")){
				Logger.info("No need to create folder for salesforce");
			}else{
				UserAccount account = dciFunctions.getUserAccount(suiteData);
				UniversalApi universalApi = dciFunctions.getUniversalApi(suiteData, account);
				folderInfo = dciFunctions.createFolder(universalApi, suiteData, 
						DCIConstants.DCI_FOLDER+uniqueId);
			}
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
			if(suiteData.getSaasApp().equalsIgnoreCase("Salesforce")){
				for(String id:uploadId){
					Map<String,String> fileInfo = new HashMap<String,String> ();
					fileInfo.put("fileId", id);
					dciFunctions.deleteFile(universalApi, suiteData, fileInfo);
				}
			}else{
				dciFunctions.deleteFolder(universalApi, suiteData, folderInfo);
			}
		} catch (Exception ex) {
			Logger.info("Issue with Delete Folder Operation " + ex.getLocalizedMessage());
		}
	}

	/**
	 * Delete mails in saas app
	 */
	@AfterClass(groups ={"Attachment","Body","AttachmentBody"})
	public void deleteMails() {
		dciFunctions.deleteAllEmailsFromInbox(suiteData);
	}


	@BeforeSuite(alwaysRun=true)
	public void downloadFileFromS3() {
		try {
			S3ActionHandler s3 = new S3ActionHandler();
			s3.downloadWholeFolder(DCIConstants.DCI_S3_BUCKET, "DCI/CIQ/Exposure", 
					new File(DCIConstants.DCI_FILE_TEMP_PATH));
			s3.downloadWholeFolder(DCIConstants.DCI_S3_BUCKET, "DCI/CIQ/i18n", 
					new File(DCIConstants.DCI_FILE_TEMP_PATH));
			
		} catch (Exception ex) {
			Logger.info("Downloading folder from S3 is failed with exception " + ex.getLocalizedMessage());
		}
	}

	/**********************************************BEFORE/AFTER CLASS*****************************************/


}
