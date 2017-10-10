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

import net.sf.json.util.JSONTokener;


public class DCICIQFileFormatTests extends DCICommonTest implements ITest {

	protected String mTestCaseName = "";
	DCIFunctions dciFunctions = null;
	ElasticSearchLogs esLogs = null;
	Map<String,String> folderInfo = new HashMap<String,String>();
	String uniqueId = UUID.randomUUID().toString();
	int mainCounter = 0;int testCounter = 0;


	/**********************************************TEST METHODS***********************************************/

	
	@Test(dataProvider = "dataUploadFileFormat", groups ={"FileFormat"})
	public void testDisplayLogsForRiskGeneratedInFileFormatCIQProfile(TestParameters testParams) throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();

		Logger.info(
				"************************************Starting "+testParams.getTestName()+" for filename:" + testParams.getFileName() + " and saas app type:" + testParams.getSaasType()+ "******************");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description:"+testParams.getTestDescription());
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output****************");
		Logger.info("Filename:"+testParams.getFileName());Logger.info("Risk Type:"+testParams.getRisks());
		Logger.info("CIQ Profilename:"+testParams.getCiq().get("ciqProfileName"));
		Logger.info("Saas App Type:" + testParams.getSaasType());
		Logger.info("*****************************************************");

		String[] risks = testParams.getRisks().toArray(new String[testParams.getRisks().size()]);
		List<NameValuePair> headers = dciFunctions.getBasicHeaders(suiteData);		
		verifyDisplayLogsWithCIQ(dciFunctions, testParams.getFileName(), 
				testParams.getSaasType(), headers, risks, testParams.getCiq());
		Logger.info(
				"************************************Completed "+testParams.getTestName()+" for filename:" + testParams.getFileName() + " and saas app type:" + testParams.getSaasType()+ "******************");

	}
	
	@Test(dataProvider = "dataUploadFileClass", groups ={"FileClass"})
	public void testDisplayLogsForRiskGeneratedInFileClassCIQProfile(TestParameters testParams) throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();

		Logger.info(
				"************************************Starting "+testParams.getTestName()+" for filename:" + testParams.getFileName() + " and saas app type:" + testParams.getSaasType()+ "******************");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description:"+testParams.getTestDescription());
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output****************");
		Logger.info("Filename:"+testParams.getFileName());Logger.info("Risk Type:"+testParams.getRisks());
		Logger.info("CIQ Profilename:"+testParams.getCiq().get("ciqProfileName"));
		Logger.info("Saas App Type:" + testParams.getSaasType());
		Logger.info("*****************************************************");

		String[] risks = testParams.getRisks().toArray(new String[testParams.getRisks().size()]);
		List<NameValuePair> headers = dciFunctions.getBasicHeaders(suiteData);		
		verifyDisplayLogsWithCIQ(dciFunctions, testParams.getFileName(), 
				testParams.getSaasType(), headers, risks, testParams.getCiq());
		Logger.info(
				"************************************Completed "+testParams.getTestName()+" for filename:" + testParams.getFileName() + " and saas app type:" + testParams.getSaasType()+ "******************");

	}
	
	@Test(dataProvider = "dataUploadFileFormatCombo", groups ={"FileFormatCombo"})
	public void testDisplayLogsForRiskGeneratedInCIQFileFormatProfileCombinations(TestParameters testParams) throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();

		Logger.info(
				"************************************Starting "+testParams.getTestName()+" for filename:" + testParams.getFileName() + " and saas app type:" + testParams.getSaasType()+ "******************");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description:"+testParams.getTestDescription());
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output****************");
		Logger.info("Filename:"+testParams.getFileName());Logger.info("Risk Type:"+testParams.getRisks());
		Logger.info("CIQ Profilename:"+testParams.getCiq().get("ciqProfileName"));
		Logger.info("Saas App Type:" + testParams.getSaasType());
		Logger.info("*****************************************************");

		String[] risks = testParams.getRisks().toArray(new String[testParams.getRisks().size()]);
		List<NameValuePair> headers = dciFunctions.getBasicHeaders(suiteData);		
		verifyDisplayLogsWithCIQ(dciFunctions, testParams.getFileName(), 
				testParams.getSaasType(), headers, risks, testParams.getCiq());
		Logger.info(
				"************************************Completed "+testParams.getTestName()+" for filename:" + testParams.getFileName() + " and saas app type:" + testParams.getSaasType()+ "******************");

	}
	
	@Test(dataProvider = "dataUploadFileClassCombo", groups ={"FileClassCombo"})
	public void testDisplayLogsForRiskGeneratedInCIQFileClassProfileCombinations(TestParameters testParams) throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();

		Logger.info(
				"************************************Starting "+testParams.getTestName()+" for filename:" + testParams.getFileName() + " and saas app type:" + testParams.getSaasType()+ "******************");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description:"+testParams.getTestDescription());
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output****************");
		Logger.info("Filename:"+testParams.getFileName());Logger.info("Risk Type:"+testParams.getRisks());
		Logger.info("CIQ Profilename:"+testParams.getCiq().get("ciqProfileName"));
		Logger.info("Saas App Type:" + testParams.getSaasType());
		Logger.info("*****************************************************");

		String[] risks = testParams.getRisks().toArray(new String[testParams.getRisks().size()]);
		List<NameValuePair> headers = dciFunctions.getBasicHeaders(suiteData);		
		verifyDisplayLogsWithCIQ(dciFunctions, testParams.getFileName(), 
				testParams.getSaasType(), headers, risks, testParams.getCiq());
		Logger.info(
				"************************************Completed "+testParams.getTestName()+" for filename:" + testParams.getFileName() + " and saas app type:" + testParams.getSaasType()+ "******************");

	}

	/**********************************************TEST METHODS***********************************************/
	/**********************************************DATA PROVIDERS
	 * @throws Exception *********************************************/

	@SuppressWarnings("unchecked")
	@DataProvider(name = "dataUploadFileFormat")
	public Object[][] dataUploadFileFormat() throws Exception {
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

		String[] fileName = dciFunctions.merge(fileNameAnimation,fileNameDatabase,fileNameDesktopPublish,fileNameEncapsulation,
				fileNameExecutable,fileNameFaxFormat,fileNameFont,fileNameMisc,fileNameMixed,fileNameMovie,fileNamePresentation,fileNameRasterImage,
				fileNameSound,fileNameSpreadsheet,fileNameUnknown,fileNameVectorGraphic,fileNameWordProcessor);
		String[] fileFormat = dciFunctions.getCIQFileFormatValue();
		String[] risks = dciFunctions.riskTypesForAFile(fileName);
		Map<String, String>[] ciqArray = new HashMap[fileName.length];

		Map<String,String> ciqValues = dciFunctions.getCIQFileFormatValues();
		for(int i = 0; i < fileName.length; i++) {
			try {
				Map<String, String> ciq=getCIQValues(ciqValues,fileName[i]);
				ciqArray[i] = ciq;
			} catch (Exception ex) {
				Logger.info("Issue with Create CIQ Profile" + ex.getLocalizedMessage());
			}
		}
		
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
		
		fileName = dciFunctions.merge(fileNameAnimation,fileNameDatabase,fileNameDesktopPublish,fileNameEncapsulation,
				fileNameExecutable,fileNameFaxFormat,fileNameFont,fileNameMisc,fileNameMixed,fileNameMovie,fileNamePresentation,fileNameRasterImage,
				fileNameSound,fileNameSpreadsheet,fileNameUnknown,fileNameVectorGraphic,fileNameWordProcessor);

		for(int i = 0; i < fileFormat.length; i++) {
			createCIQProfile(fileFormat[i]);
		}
		
		dciFunctions.fileUploadOrSendEmail(suiteData, folderInfo, fileName);

		Object[][] result = new Object[fileName.length][];
		Logger.info(fileName.length+"::::"+result.length+"::::"+risks.length+"::::"+fileFormat.length);
		
		for (int i = 0; i < fileName.length; i++) {
			result[i] = new Object[] { new TestParameters("Risk Generation/Validation for ContentIQ Profile:"+ciqArray[i].get("ciqProfileName")+"for file:"+fileName[i], 
					"Create CIQ Profile with name:"+ciqArray[i].get("ciqProfileName")+" and upload file:"+fileName[i]+ ". Then verify risk logs are getting generated within the SLA provided",
					fileName[i], SaasType.getSaasFilterType(suiteData.getSaasApp()), Arrays.asList(risks[i].split(",")), ciqArray[i])};
		}

		mainCounter = 0;testCounter = DCIConstants.DCI_COUNTER_MAX;
		
		return result;
	}
	
	@SuppressWarnings("unchecked")
	@DataProvider(name = "dataUploadFileClass")
	public Object[][] dataUploadFileClass() throws Exception {
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

		
		
		String[] fileName = dciFunctions.merge(fileNameAnimation,fileNameDatabase,fileNameDesktopPublish,fileNameEncapsulation,
				fileNameExecutable,fileNameFaxFormat,fileNameFont,fileNameMisc,fileNameMixed,fileNameMovie,fileNamePresentation,fileNameRasterImage,
				fileNameSound,fileNameSpreadsheet,fileNameUnknown,fileNameVectorGraphic,fileNameWordProcessor);
		String[] fileClass = dciFunctions.getCIQFileClassValue();
		String[] risks = dciFunctions.riskTypesForAFile(fileName);
		Map<String, String>[] ciqArray = new HashMap[fileName.length];
		Map<String,String> ciqValues = dciFunctions.getCIQFileClassValues();
		
		for(int i = 0; i < fileName.length; i++) {
			try {
				Map<String, String> ciq=getCIQValues(ciqValues,fileName[i]);
				ciqArray[i] = ciq;
			} catch (Exception ex) {
				Logger.info("Issue with Create CIQ Profile" + ex.getLocalizedMessage());
			}
		}
		
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
		
		fileName = dciFunctions.merge(fileNameAnimation,fileNameDatabase,fileNameDesktopPublish,fileNameEncapsulation,
				fileNameExecutable,fileNameFaxFormat,fileNameFont,fileNameMisc,fileNameMixed,fileNameMovie,fileNamePresentation,fileNameRasterImage,
				fileNameSound,fileNameSpreadsheet,fileNameUnknown,fileNameVectorGraphic,fileNameWordProcessor);

		for(int i = 0; i < fileClass.length; i++) {
			createCIQProfile("class:"+fileClass[i]);
		}
		
		dciFunctions.fileUploadOrSendEmail(suiteData, folderInfo, fileName);
		
		Object[][] result = new Object[fileName.length][];
		Logger.info(fileName.length+"::::"+result.length+"::::"+risks.length+"::::"+fileClass.length);

		for (int i = 0; i < fileName.length; i++) {
			result[i] = new Object[] { new TestParameters("Risk Generation/Validation for ContentIQ Profile:"+ciqArray[i].get("ciqProfileName")+"for file:"+fileName[i], 
					"Create CIQ Profile with name:"+ciqArray[i].get("ciqProfileName")+" and upload file:"+fileName[i]+ ". Then verify risk logs are getting generated within the SLA provided",
					fileName[i], SaasType.getSaasFilterType(suiteData.getSaasApp()), Arrays.asList(risks[i].split(",")), ciqArray[i])};
		}

		mainCounter = 0;testCounter = DCIConstants.DCI_COUNTER_MAX;
		
		return result;
	}


	@SuppressWarnings("unchecked")
	@DataProvider(name = "dataUploadFileFormatCombo")
	public Object[][] dataUploadFileFormatCombo() {
		dciFunctions = new DCIFunctions();

		String[] fileName = dciFunctions.getFileName(DCIConstants.DCI_FILE_UPLOAD_FILE_FORMAT_COMBO_PATH);
		String[] terms = dciFunctions.getCIQRiskContentProfileName(fileName);
		String[] risks = dciFunctions.riskTypesForAFile(fileName);
		Map<String, String>[] ciqArray = new HashMap[fileName.length];

		/** Create Dictionary **/
		List<NameValuePair> headers = dciFunctions.getBrowserHeaders(suiteData);

		String dictName = "Custom_Dictionaries_Only";
		String dictDescription = "Custom Dictionaries Only Description";
		String dictKeywords = "custom_dictionaries_only";
		try {
			List<String> keywords= new ArrayList<String>();
			keywords.add(dictKeywords);
			dciFunctions.createDictionary(restClient, suiteData, dictName, 
					dictDescription, null, keywords, headers);
		} catch (Exception ex) {
			Logger.info("Issue with Create Custom Dictionary" + ex.getLocalizedMessage());
		}
		/***********************/

		for(int i = 0; i < fileName.length; i++) {
			try {
				Map<String, String> ciq=createCIQProfile(fileName[i],terms[i],"ASCII_Text");
				ciqArray[i] = ciq;
			} catch (Exception ex) {
				Logger.info("Issue with Create CIQ Profile" + ex.getLocalizedMessage());
			}
		}

		fileName = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_FILE_FORMAT_COMBO_PATH,
				fileName);
		dciFunctions.fileUploadOrSendEmail(suiteData, folderInfo, fileName);
		
		Object[][] result = new Object[fileName.length][];

		for (int i = 0; i < fileName.length; i++) {
			result[i] = new Object[] { new TestParameters("Risk Generation/Validation for ContentIQ Profile:"+terms[i]+"for file:"+fileName[i], 
					"Create CIQ Profile with name:"+terms[i]+" and upload file:"+fileName[i]+ ". Then verify risk logs are getting generated within the SLA provided",
					fileName[i], SaasType.getSaasFilterType(suiteData.getSaasApp()), Arrays.asList(risks[i].split(",")), ciqArray[i])};
		}

		mainCounter = 0;testCounter = DCIConstants.DCI_COUNTER_MAX;
		
		return result;
	}
	
	@SuppressWarnings("unchecked")
	@DataProvider(name = "dataUploadFileClassCombo")
	public Object[][] dataUploadFileClassCombo() {
		dciFunctions = new DCIFunctions();

		String[] fileName = dciFunctions.getFileName(DCIConstants.DCI_FILE_UPLOAD_FILE_FORMAT_COMBO_PATH);
		String[] terms = dciFunctions.getCIQRiskContentProfileName(fileName);
		String[] risks = dciFunctions.riskTypesForAFile(fileName);
		Map<String, String>[] ciqArray = new HashMap[fileName.length];

		/** Create Dictionary **/
		List<NameValuePair> headers = dciFunctions.getBrowserHeaders(suiteData);

		String dictName = "Custom_Dictionaries_Only";
		String dictDescription = "Custom Dictionaries Only Description";
		String dictKeywords = "custom_dictionaries_only";
		try {
			List<String> keywords= new ArrayList<String>();
			keywords.add(dictKeywords);
			dciFunctions.createDictionary(restClient, suiteData, dictName, 
					dictDescription, null, keywords, headers);
		} catch (Exception ex) {
			Logger.info("Issue with Create Custom Dictionary" + ex.getLocalizedMessage());
		}
		/***********************/

		for(int i = 0; i < fileName.length; i++) {
			try {
				Map<String, String> ciq=createCIQProfile(fileName[i],terms[i],"class:WORDPROCESSOR");
				ciqArray[i] = ciq;
			} catch (Exception ex) {
				Logger.info("Issue with Create CIQ Profile" + ex.getLocalizedMessage());
			}
		}

		fileName = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_FILE_FORMAT_COMBO_PATH,
				fileName);
		dciFunctions.fileUploadOrSendEmail(suiteData, folderInfo, fileName);
		
		Object[][] result = new Object[fileName.length][];
		for (int i = 0; i < fileName.length; i++) {
			result[i] = new Object[] { new TestParameters("Risk Generation/Validation for ContentIQ Profile:"+terms[i]+"for file:"+fileName[i], 
					"Create CIQ Profile with name:"+terms[i]+" and upload file:"+fileName[i]+ ". Then verify risk logs are getting generated within the SLA provided",
					fileName[i], SaasType.getSaasFilterType(suiteData.getSaasApp()), Arrays.asList(risks[i].split(",")), ciqArray[i])};
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

	private void createCIQProfile(String fileFormatType) throws Exception{
		String ciqProfileName=fileFormatType.replace("class:", "")+" Name";
		String ciqProfileDescription=fileFormatType.replace("class:", "")+" Description";
		
		Logger.info("Creating CIQ profile with name:"+ciqProfileName+" file format type:"+fileFormatType+" in progress");
	
		List<String> valuesPreDefDict=null;List<String> valuesPreDefTerms=null;
		List<String> valuesCustomDict=null;List<String> valuesCustomTerms=null;
		List<String> valuesRiskTypes=null;List<String> valuesContentTypes=null;
		List<String> valuesFileFormat=new ArrayList<String>();valuesFileFormat.add(fileFormatType);
		List<String> valuesMLProfile=null;
		dciFunctions.createCIQProfile(suiteData, restClient, ciqProfileName, ciqProfileDescription, 
				valuesPreDefDict, valuesPreDefTerms, valuesCustomDict, valuesCustomTerms, 
				valuesRiskTypes, valuesContentTypes, valuesFileFormat,valuesMLProfile,
				"high", 0, true, 1, false);

		Logger.info("Creating CIQ profile with name:"+ciqProfileName+" file format type:"+fileFormatType+" is completed");
	}


	private Map<String, String> getCIQValues(Map<String,String> ciqValues, String fileName) {
		Map<String, String> ciq=new HashMap<String, String>();
		ciq.put("ciqProfileName", ciqValues.get(fileName)+" Name");
		ciq.put("ciqProfileDescription", ciqValues.get(fileName)+" Description");

		return ciq;
	}
	
	private Map<String,String> createCIQProfile(String fileName,String ciqProfileName, String fileFormat) throws Exception{
		Logger.info("Creating CIQ profile with custom dictionaries and risks in progress");
		String ciqProfileDescription=ciqProfileName+" Description";
		List<String> valuesPreDefDict=null;List<String> valuesPreDefTerms=null;
		List<String> valuesCustomDict=null;List<String> valuesCustomTerms=null;
		List<String> valuesRiskTypes=null;List<String> valuesContentTypes=null;
		List<String> valuesFileFormat=null;List<String> valuesMLProfile=null;
		int threshold=1;
		Map<String, String> ciq=new HashMap<String, String>();
		
		if(fileName.equalsIgnoreCase("ff_only.txt")){
			valuesFileFormat= Arrays.asList(fileFormat);threshold=0;
		}else{
			if(fileName.contains("ff")){
				valuesFileFormat= Arrays.asList(fileFormat);
			}
			if(fileName.contains("risk")){
				valuesRiskTypes= Arrays.asList("dlp","hipaa","vba_macros","virus","pci",
						"pii","ferpa","glba");
			}
			if(fileName.contains("content")){
				valuesContentTypes=Arrays.asList(
						"business","computing","cryptographic_keys","design doc",
						"encryption","engineering","health","legal","source_code"
						);
			}
			if(fileName.contains("pdt")){
				valuesPreDefTerms=Arrays.asList("Brazil CPF Number");ciq.put("PDT", "Brazil CPF Number");
			}
			if(fileName.contains("pdd")){
				valuesPreDefDict=Arrays.asList("Illegal Drugs");ciq.put("PDD", "Illegal Drugs");
			}
			if(fileName.contains("ct")){
				valuesCustomTerms=Arrays.asList("custom_terms_only");ciq.put("CT", "custom_terms_only");
			}
			if(fileName.contains("cd")){
				valuesCustomDict=Arrays.asList("Custom_Dictionaries_Only");ciq.put("CD", "Custom_Dictionaries_Only");
			}
			
		}
		
		ciq.put("ciqProfileName", ciqProfileName);
		ciq.put("ciqProfileDescription", ciqProfileDescription);

		if(fileName.equalsIgnoreCase("ff_risk_content.txt")||
			fileName.equalsIgnoreCase("ff_risk.txt")||
			 fileName.equalsIgnoreCase("ff_content.txt")){
			threshold=0;
		}
		
		dciFunctions.createCIQProfile(suiteData, restClient, ciqProfileName, ciqProfileDescription, 
				valuesPreDefDict, valuesPreDefTerms, valuesCustomDict, valuesCustomTerms, 
				valuesRiskTypes, valuesContentTypes, valuesFileFormat,valuesMLProfile,
				"high", threshold, true, 1, false);

		Logger.info("Creating CIQ profile with custom dictionaries and risks is completed");
		
		return ciq;
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

			String validationMessage = dciFunctions.validationRiskLogsWithCIQProfile(hits, fileName, suiteData.getUsername(),
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

			net.sf.json.JSONArray jArray = (net.sf.json.JSONArray) new JSONTokener(hits).nextValue();
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


	/**********************************************UTIL METHODS***********************************************/
	/**********************************************BEFORE/AFTER CLASS*****************************************/

	/**
	 * Create folders in saas apps
	 */
	@BeforeClass(groups ={"FileFormat","FileClass","FileFormatCombo","FileClassCombo"})
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
	@AfterClass(groups ={"FileFormat","FileClass","FileFormatCombo","FileClassCombo"})
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
	@BeforeClass(groups ={"FileFormat","FileClass","FileFormatCombo","FileClassCombo"})
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
	@AfterClass(groups ={"FileFormat","FileClass","FileFormatCombo","FileClassCombo"})
	public void deleteContentIqProfileAfterTestEnds() {
		dciFunctions = new DCIFunctions();
		dciFunctions.deleteAllPolicies(restClient, suiteData);
		dciFunctions.deleteAllCIQProfiles(restClient, suiteData);
		dciFunctions.deleteAllDictionaries(restClient, suiteData);
		dciFunctions.deleteAllTrainingProfiles(restClient, suiteData);
	}


	@BeforeMethod(groups ={"FileFormat","FileClass","FileFormatCombo","FileClassCombo"})
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
			s3.downloadWholeFolder(DCIConstants.DCI_S3_BUCKET, "DCI/FileFormat", 
					new File(DCIConstants.DCI_FILE_TEMP_PATH));
			
		} catch (Exception ex) {
			Logger.info("Downloading folder from S3 is failed with exception " + ex.getLocalizedMessage());
		}
	}

	/**********************************************BEFORE/AFTER METHODS/CLASS*****************************************/


}