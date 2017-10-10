package com.elastica.beatle.tests.dci;

import java.io.File;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.ITest;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.elastica.beatle.S3Utils.S3ActionHandler;
import com.elastica.beatle.dci.DCICommonTest;
import com.elastica.beatle.dci.DCIConstants;
import com.elastica.beatle.dci.DCIFunctions;
import com.elastica.beatle.dci.SaasType;
import com.elastica.beatle.dci.TestParameters;
import com.elastica.beatle.es.ElasticSearchLogs;
import com.elastica.beatle.logger.Logger;
import com.elastica.beatle.splunk.SplunkQueries;
import com.elastica.beatle.splunk.SplunkQueryResult;
import com.universal.common.UniversalApi;
import com.universal.dtos.UserAccount;

/**
 * 
 * @author eldorajan
 *
 */

public class DCIFileTypeTests extends DCICommonTest implements ITest {

	DCIFunctions dciFunctions = null;
	ElasticSearchLogs esLogs = null;
	Map<String,String> folderInfo = new HashMap<String,String>();
	String uniqueId = UUID.randomUUID().toString();
	protected String mTestCaseName = "";
	 

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
		Logger.info("Filename:"+testParams.getFileName());
		Logger.info("Saas App Type:"+testParams.getSaasType());
		Logger.info("*****************************************************");

		List<NameValuePair> headers = dciFunctions.getBasicHeaders(suiteData);

		verifySplunkQueryLogs(dciFunctions, testParams.getFileName(), testParams.getSaasType(), headers);

		Logger.info(
				"************************************Completed "+testParams.getTestName()+" for filename:" + testParams.getFileName() + " and saas app type:" + testParams.getSaasType()+ "******************");

	}


	/**********************************************TEST METHODS***********************************************/
	/**********************************************DATA PROVIDERS*********************************************/

	@DataProvider(name = "dataUpload")
	public Object[][] dataUpload() {

		dciFunctions = new DCIFunctions();

		String[] fileNameAnimation = dciFunctions.getFileName(DCIConstants.DCI_FILE_UPLOAD_FILE_CLASS_PATH+File.separator+"ANIMATION");
		String[] fileNameDatabase = dciFunctions.getFileName(DCIConstants.DCI_FILE_UPLOAD_FILE_CLASS_PATH+File.separator+"DATABASE");
		String[] fileNameDesktopPublish = dciFunctions.getFileName(DCIConstants.DCI_FILE_UPLOAD_FILE_CLASS_PATH+File.separator+"DESKTOPPUBLISH");
		String[] fileNameEncapsulation = dciFunctions.getFileName(DCIConstants.DCI_FILE_UPLOAD_FILE_CLASS_PATH+File.separator+"ENCAPSULATION");
		String[] fileNameExecutable = dciFunctions.getFileName(DCIConstants.DCI_FILE_UPLOAD_FILE_CLASS_PATH+File.separator+"EXECUTABLE");
		String[] fileNameFaxFormat = dciFunctions.getFileName(DCIConstants.DCI_FILE_UPLOAD_FILE_CLASS_PATH+File.separator+"FAXFORMAT");
		String[] fileNameFont = dciFunctions.getFileName(DCIConstants.DCI_FILE_UPLOAD_FILE_CLASS_PATH+File.separator+"FONT");
		String[] fileNameMisc = dciFunctions.getFileName(DCIConstants.DCI_FILE_UPLOAD_FILE_CLASS_PATH+File.separator+"MISC");
		String[] fileNameMixed = dciFunctions.getFileName(DCIConstants.DCI_FILE_UPLOAD_FILE_CLASS_PATH+File.separator+"MIXED");
		String[] fileNameMovie = dciFunctions.getFileName(DCIConstants.DCI_FILE_UPLOAD_FILE_CLASS_PATH+File.separator+"MOVIE");
		String[] fileNamePresentation = dciFunctions.getFileName(DCIConstants.DCI_FILE_UPLOAD_FILE_CLASS_PATH+File.separator+"PRESENTATION");
		String[] fileNameRasterImage = dciFunctions.getFileName(DCIConstants.DCI_FILE_UPLOAD_FILE_CLASS_PATH+File.separator+"RASTERIMAGE");
		String[] fileNameSound = dciFunctions.getFileName(DCIConstants.DCI_FILE_UPLOAD_FILE_CLASS_PATH+File.separator+"SOUND");
		String[] fileNameSpreadsheet= dciFunctions.getFileName(DCIConstants.DCI_FILE_UPLOAD_FILE_CLASS_PATH+File.separator+"SPREADSHEET");
		String[] fileNameUnknown = dciFunctions.getFileName(DCIConstants.DCI_FILE_UPLOAD_FILE_CLASS_PATH+File.separator+"UNKNOWN");
		String[] fileNameVectorGraphic = dciFunctions.getFileName(DCIConstants.DCI_FILE_UPLOAD_FILE_CLASS_PATH+File.separator+"VECTORGRAPHIC");
		String[] fileNameWordProcessor = dciFunctions.getFileName(DCIConstants.DCI_FILE_UPLOAD_FILE_CLASS_PATH+File.separator+"WORDPROCESSOR");
	
		fileNameAnimation = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_FILE_CLASS_PATH+File.separator+"ANIMATION",fileNameAnimation);
		fileNameDatabase = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_FILE_CLASS_PATH+File.separator+"DATABASE",fileNameDatabase);
		fileNameDesktopPublish = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_FILE_CLASS_PATH+File.separator+"DESKTOPPUBLISH",fileNameDesktopPublish);
		fileNameEncapsulation = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_FILE_CLASS_PATH+File.separator+"ENCAPSULATION",fileNameEncapsulation);
		fileNameExecutable = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_FILE_CLASS_PATH+File.separator+"EXECUTABLE",fileNameExecutable);
		fileNameFaxFormat = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_FILE_CLASS_PATH+File.separator+"FAXFORMAT",fileNameFaxFormat);
		fileNameFont = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_FILE_CLASS_PATH+File.separator+"FONT",fileNameFont);
		fileNameMisc = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_FILE_CLASS_PATH+File.separator+"MISC",fileNameMisc);
		fileNameMixed = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_FILE_CLASS_PATH+File.separator+"MIXED",fileNameMixed);
		fileNameMovie = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_FILE_CLASS_PATH+File.separator+"MOVIE",fileNameMovie);
		fileNamePresentation = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_FILE_CLASS_PATH+File.separator+"PRESENTATION",fileNamePresentation);
		fileNameRasterImage = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_FILE_CLASS_PATH+File.separator+"RASTERIMAGE",fileNameRasterImage);
		fileNameSound = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_FILE_CLASS_PATH+File.separator+"SOUND",fileNameSound);
		fileNameSpreadsheet= dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_FILE_CLASS_PATH+File.separator+"SPREADSHEET",fileNameSpreadsheet);
		fileNameUnknown = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_FILE_CLASS_PATH+File.separator+"UNKNOWN",fileNameUnknown);
		fileNameVectorGraphic = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_FILE_CLASS_PATH+File.separator+"VECTORGRAPHIC",fileNameVectorGraphic);
		fileNameWordProcessor = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_FILE_CLASS_PATH+File.separator+"WORDPROCESSOR",fileNameWordProcessor);
		
		String[] fileName = dciFunctions.merge(fileNameAnimation,fileNameDatabase,fileNameDesktopPublish,fileNameEncapsulation,
				fileNameExecutable,fileNameFaxFormat,fileNameFont,fileNameMisc,fileNameMixed,fileNameMovie,fileNamePresentation,fileNameRasterImage,
				fileNameSound,fileNameSpreadsheet,fileNameUnknown,fileNameVectorGraphic,fileNameWordProcessor);

		/*String[] fileNameFaxFormat = dciFunctions.getFileName(DCIConstants.DCI_FILE_TYPES_UPLOAD_PATH+File.separator+"FaxFormat");
		fileNameFaxFormat = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_TYPES_UPLOAD_PATH+File.separator+"FaxFormat",fileNameFaxFormat);
		String[] fileName = dciFunctions.merge(fileNameFaxFormat);*/

		dciFunctions.fileUploadOrSendEmail(suiteData, folderInfo, fileName);

		Object[][] result = new Object[fileName.length][];
		for (int i = 0; i < fileName.length; i++) {
			result[i] = new Object[] { new TestParameters("Validation of uploaded file:"+fileName[i]+" for successful processing via Content Inspection Engine with splunk", 
					"Upload file:"+fileName[i]+ ". Then verify file is processed successfully with splunk within the SLA provided",
					fileName[i], SaasType.getSaasFilterType(suiteData.getSaasApp()))};
		}

		dciFunctions.waitForMinutes(20);


		return result;
	}

	/**********************************************DATA PROVIDERS*********************************************/
	/**********************************************UTIL METHODS***********************************************/

	@Override
	public String getTestName() {
		return this.mTestCaseName;
	}

	private void verifySplunkQueryLogs(DCIFunctions dciFunctions, String fileName,
			String saasType, List<NameValuePair> headers)
					throws Exception {
		try {	
			String resourceId = dciFunctions.getResourceId(restClient, esLogs, suiteData, fileName);
			
			SplunkQueryResult splunkResult = SplunkQueries.lookForProcessedCILogsForAFile(
					suiteData.getEnvironmentName(), resourceId, "-2h");
			Assert.assertTrue(splunkResult.getEventsCount()>=1, "CI processing not happened for file:"+fileName);
			
			JSONArray result = splunkResult.getQueryResult().getJSONArray("results"); 
			for(int i = 0;i<result.length();){
				JSONObject resultObject = result.getJSONObject(i);
				String rawJson = ((String) resultObject.get("_raw")).replaceAll("_raw(.*)DEBUG-CI results for file(.*) - ", "");
				rawJson = rawJson.replaceAll("(.*) Finished Job:",""); 
				rawJson = rawJson.replace("u'", "\"");rawJson = rawJson.replace("'", "\"");
				rawJson = rawJson.replace("\"_silent\": [None]", "\"_silent\": []");
				rawJson = rawJson.replace(": True", ": true");
				rawJson = rawJson.replace(": False", ": false");
				
				String message = dciFunctions.verifyFileInfoSplunk(rawJson, fileName);
				Assert.assertEquals(message, "","Expected no errors while validation");
				break;
				
			}	

			} finally {
				dciFunctions.cleanupFileFromTempFolder(fileName);
			}
		}


		/**********************************************UTIL METHODS***********************************************/
		/**********************************************BEFORE/AFTER CLASS*****************************************/


		/**
		 * Delete content iq profile
		 */
		@AfterClass(groups ={"All"})
		public void deleteContentIqProfile() {
			dciFunctions = new DCIFunctions();
			//dciFunctions.deleteAllCIQProfile(restClient, suiteData);
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
				s3.downloadWholeFolder(DCIConstants.DCI_S3_BUCKET, "DCI/FileFormat/FileClass", 
						new File(DCIConstants.DCI_FILE_TEMP_PATH));
				
			} catch (Exception ex) {
				Logger.info("Downloading folder from S3 is failed with exception " + ex.getLocalizedMessage());
			}
		}
		
		/**********************************************BEFORE/AFTER CLASS*****************************************/


	}
