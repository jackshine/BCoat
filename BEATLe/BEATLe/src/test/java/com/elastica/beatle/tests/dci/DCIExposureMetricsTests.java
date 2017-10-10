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
import org.testng.ITest;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.elastica.beatle.Priority;
import com.elastica.beatle.RestClient.ClientUtil;
import com.elastica.beatle.S3Utils.S3ActionHandler;
import com.elastica.beatle.dci.DCICommonTest;
import com.elastica.beatle.dci.DCIConstants;
import com.elastica.beatle.dci.DCIFunctions;
import com.elastica.beatle.dci.SaasType;
import com.elastica.beatle.dci.TestParameters;
import com.elastica.beatle.dci.dto.contentType.ContentType;
import com.elastica.beatle.dci.dto.contentType.ContentTypes;
import com.elastica.beatle.dci.dto.fileType.FileType;
import com.elastica.beatle.dci.dto.vulnerability.Vulnerability;
import com.elastica.beatle.es.ElasticSearchLogs;
import com.elastica.beatle.logger.Logger;
import com.elastica.beatle.securlets.dto.VulnerabilityTypes;
import com.universal.common.UniversalApi;
import com.universal.dtos.UserAccount;
import com.universal.dtos.onedrive.Folder;
import com.universal.dtos.onedrive.ItemResource;

import net.sf.json.JSONArray;
import net.sf.json.util.JSONTokener;

/**
 * 
 * @author eldorajan
 *
 */

public class DCIExposureMetricsTests extends DCICommonTest implements ITest {

	SoftAssert softAssert = null;

	DCIFunctions dciFunctions = null;
	ElasticSearchLogs esLogs = null;
	Map<String,String> folderInfo = new HashMap<String,String>();
	Folder folderResource = null;
	
	String uniqueId = UUID.randomUUID().toString();
	ItemResource[] fileResource = null;
	String[] fileName = null;
	String[] fileId = null;
	String[] fileEtag = null;
	String[] contentIQDictProfileId=null;String[] contentIQTermsProfileId=null;protected String mTestCaseName = "";

	int mainCounter = 0;int testCounter = 0;

	String[] riskFileName = {"ferpa.rtf","glba.txt","hipaa.rtf","pci.txt","pii.rtf","vba_macro.xls","virus.html"};
	String[] riskType={"ferpa","glba","hipaa","pci","pii","vba_macros","virus"};
	String[] risks={"FERPA","GLBA","HIPAA,PII","PCI","PII","VBA Macros","Virus/Malware"};

	String[] contentRiskFileName = {"business.txt","computing.doc","design.pdf","encryption.zip","engineering.doc","health.txt","legal.html","digital_certificate.pem","source_code.txt"};
	String[] contentRiskType={"business","computing","design doc","encryption","engineering","health","legal","cryptographic_keys","source_code"};
	String[] contentRiskTypes={"business","computing","design doc","encryption","engineering","health","legal","cryptographic_keys","source_code"};

	String[] contentFileName = {"business.txt","computing.doc","design.pdf","encryption.bin","engineering.doc","health.txt","legal.html","digital_certificate.pem","source_code.xls"/*,"audio.mp3","executable.exe","image.jpeg","video.mp4"*/};
	String[] contentType={"business","computing","design doc","encryption","engineering","health","legal","cryptographic_keys","source_code"/*,"audio","executable","image","video"*/};
	String[] contentTypes={"business","computing","design doc","encryption","engineering","health","legal","cryptographic_keys","source_code"/*,"audio","executable","image","video"*/};

	String[] ciqRiskFileName = {"Diseases.txt","US_License_Plate_Number.txt"};
	String[] ciqRiskType={"DCI_DIS","DCI_USALPN"};
	String[] ciqRisks={"ContentIQ Violations","ContentIQ Violations"};
	
	String[] fileClassFileName = {"ANIMATION.fli","DATABASE.ADB","DESKTOPPUBLISH.pub","ENCAPSULATION.aws","EXECUTABLE.sys",
			"FAXFORMAT.dcx","FONT.otf","MISC.HLP","MIXED.FW3","MOVIE.mpg","PRESENTATION.pptx","RASTERIMAGE.bmp",
			"SOUND.ac3","SPREADSHEET.csv","UNKNOWN.cab","VECTORGRAPHIC.dxf","WORDPROCESSOR.sam"};
	String[] fileClassType={"ANIMATION","DATABASE","DESKTOPPUBLSH","ENCAPSULATION","EXECUTABLE",
			"FAXFORMAT","FONT","MISC","MIXED","MOVIE","PRESENTATION","RASTERIMAGE",
			"SOUND","SPREADSHEET","UNKNOWN","VECTORGRAPHIC","WORDPROCESSOR"};
	String[] fileClassRisks={"ContentIQ Violations","ContentIQ Violations","ContentIQ Violations","ContentIQ Violations",
			"ContentIQ Violations","ContentIQ Violations","ContentIQ Violations","ContentIQ Violations",
			"ContentIQ Violations","ContentIQ Violations","ContentIQ Violations","ContentIQ Violations",
			"ContentIQ Violations","ContentIQ Violations","ContentIQ Violations","ContentIQ Violations","ContentIQ Violations",};
	
	
	/**********************************************TEST METHODS***********************************************/

	@Priority(1)
	@Test(dataProvider = "dataUploadRiskTypeUnexposed", groups ={"Unexposed"})
	public void testFilterCountCheckForUploadingOfUnexposedRiskTypeFile(TestParameters testParams) throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();

		String riskType=testParams.getRiskType(); 
		List<String> risks=testParams.getRisks();
		String fileName=testParams.getFileName();
		int countBeforeUI= testParams.getCountUI(); 
		int countBeforeAPI= testParams.getCountAPI(); 
		int countAfterExpectedUI=countBeforeUI+dciFunctions.getExpectedCount(fileName);	
		int countAfterExpectedAPI=countBeforeAPI+dciFunctions.getExpectedCount(fileName);		
		String saasType = SaasType.getSaasFilterType(suiteData.getSaasApp());
		String appType = SaasType.getAppType(suiteData.getSaasApp(), suiteData.getEnvironmentName());
		Map<String, String>  additionalParamsAPI = testParams.getHeadersAPI();
		Map<String, String>  additionalParamsUI =  testParams.getHeadersUI();

		Logger.info(
				"************************************ Starting the checking of increase in filter count"
						+ " for filename:" + fileName +" with risk types:"+risks+" and saas app type:" + saasType + " ******************");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description: Check the filter count increase after uploading of the risk file");
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output****************");
		Logger.info("Filename:"+fileName);Logger.info("Risk Types to be validated:"+risks);
		Logger.info("Current Filter Count from UI:"+ countBeforeUI);Logger.info("Expected Filter Count from UI:"+ (countAfterExpectedUI));
		Logger.info("Current Filter Count from API:"+ countBeforeAPI);Logger.info("Expected Filter Count from API:"+ (countAfterExpectedAPI));
		Logger.info("Saas App Type:"+suiteData.getSaasApp());
		Logger.info("*****************************************************");

		List<NameValuePair> headers = dciFunctions.getBasicHeaders(suiteData);	
		verifyRiskTypesDisplayLogs(dciFunctions, fileName, saasType, headers);

		Vulnerability vulnerability = dciFunctions.getVulnerability(suiteData, restClient, additionalParamsUI);
		Logger.info("Vulnerability:" + riskType + "::Count:" + vulnerability.getResults().getVulnerabilityCount(riskType));
		VulnerabilityTypes vulnerabilityTypes = dciFunctions.getVulnerabilityTypes(suiteData, restClient, appType, additionalParamsAPI);
		Logger.info("Vulnerability:" + riskType + "::Count:" + vulnerabilityTypes.getObjects().getVulnerabilityCount(riskType));
		int countAfterUI= vulnerability.getResults().getVulnerabilityCount(riskType);
		int countAfterAPI= vulnerabilityTypes.getObjects().getVulnerabilityCount(riskType);

		Logger.info("****************Actual Output****************");
		Logger.info("Expected Filter Count from UI:"+ (countAfterExpectedUI));Logger.info("Actual Filter Count from UI:"+ (countAfterUI));
		Logger.info("Expected Filter Count from API:"+ (countAfterExpectedAPI));Logger.info("Actual Filter Count from API:"+ (countAfterAPI));
		Logger.info("Saas App Type:"+suiteData.getSaasApp());
		Logger.info("*****************************************************");

		softAssert.assertEquals(countAfterUI, countAfterExpectedUI,"Risk count is not getting incremented for file:"+fileName+ " via UI");
		softAssert.assertEquals(countAfterAPI, countAfterExpectedAPI,"Risk count is not getting incremented for file:"+fileName+ " via API");
		softAssert.assertAll();

		Map<String, Integer> numberList=new HashMap<String, Integer>();
		numberList.put("ExpAPICount", countAfterExpectedAPI);numberList.put("ActAPICount", countAfterAPI);
		numberList.put("ExpUICount", countAfterExpectedUI);numberList.put("ActUICount", countAfterUI);
		//Assert.assertEquals(dciFunctions.numberComparison(numberList), "");
		
		Logger.info(
				"************************************ Completed the checking of increase in filter count"
						+ " for filename:" + fileName +" with risk types:"+risks+" and saas app type:" + saasType + " ******************");
	}

	@Priority(2)
	@Test(dataProvider = "dataDeleteRiskTypeUnexposed", groups ={"Unexposed"})
	public void testFilterCountCheckForDeletingOfUnexposedRiskTypeFile(TestParameters testParams) throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();

		String riskType=testParams.getRiskType(); 
		List<String> risks=testParams.getRisks();
		String fileName=testParams.getFileName();
		int countBeforeUI= testParams.getCountUI();
		int countBeforeAPI= testParams.getCountAPI();
		int countAfterExpectedUI=countBeforeUI-dciFunctions.getExpectedCount(fileName);
		int countAfterExpectedAPI=countBeforeAPI-dciFunctions.getExpectedCount(fileName);		
		String saasType = SaasType.getSaasFilterType(suiteData.getSaasApp());
		String appType = SaasType.getAppType(suiteData.getSaasApp(), suiteData.getEnvironmentName());
		Map<String, String>  additionalParamsAPI = testParams.getHeadersAPI();
		Map<String, String>  additionalParamsUI =  testParams.getHeadersUI();

		Logger.info(
				"************************************ Starting the checking of decrease in filter count"
						+ " for filename:" + fileName +" with risk types:"+risks+" and saas app type:" + saasType + " ******************");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description: Check the filter count increase after deleting of the risk file");
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output****************");
		Logger.info("Filename:"+fileName);Logger.info("Risk Types to be validated:"+risks);
		Logger.info("Current Filter Count from UI:"+ countBeforeUI);Logger.info("Expected Filter Count from UI:"+ (countAfterExpectedUI));
		Logger.info("Current Filter Count from API:"+ countBeforeAPI);Logger.info("Expected Filter Count from API:"+ (countAfterExpectedAPI));
		Logger.info("Saas App Type:"+suiteData.getSaasApp());
		Logger.info("*****************************************************");

		Vulnerability vulnerability = dciFunctions.getVulnerability(suiteData, restClient, additionalParamsUI);
		Logger.info("Vulnerability:" + riskType + "::Count:" + vulnerability.getResults().getVulnerabilityCount(riskType));
		VulnerabilityTypes vulnerabilityTypes = dciFunctions.getVulnerabilityTypes(suiteData, restClient, appType, additionalParamsAPI);
		Logger.info("Vulnerability:" + riskType + "::Count:" + vulnerabilityTypes.getObjects().getVulnerabilityCount(riskType));
		int countAfterUI= vulnerability.getResults().getVulnerabilityCount(riskType);
		int countAfterAPI= vulnerabilityTypes.getObjects().getVulnerabilityCount(riskType);

		Logger.info("****************Actual Output****************");
		Logger.info("Expected Filter Count from UI:"+ (countAfterExpectedUI));Logger.info("Actual Filter Count from UI:"+ (countAfterUI));
		Logger.info("Expected Filter Count from API:"+ (countAfterExpectedAPI));Logger.info("Actual Filter Count from API:"+ (countAfterAPI));
		Logger.info("Saas App Type:"+suiteData.getSaasApp());
		Logger.info("*****************************************************");

		softAssert.assertEquals(countAfterUI, countAfterExpectedUI,"Risk count is not getting decremented for file:"+fileName+ " via UI");
		softAssert.assertEquals(countAfterAPI, countAfterExpectedAPI,"Risk count is not getting decremented for file:"+fileName+ " via API");
		softAssert.assertAll();

		Map<String, Integer> numberList=new HashMap<String, Integer>();
		numberList.put("ExpAPICount", countAfterExpectedAPI);numberList.put("ActAPICount", countAfterAPI);
		numberList.put("ExpUICount", countAfterExpectedUI);numberList.put("ActUICount", countAfterUI);
		//Assert.assertEquals(dciFunctions.numberComparison(numberList), "");
		
		Logger.info(
				"************************************ Completed the checking of decrease in filter count"
						+ " for filename:" + fileName +" with risk types:"+risks+" and saas app type:" + saasType + " ******************");
	}

	@Priority(3)
	@Test(dataProvider = "dataUploadContentTypeUnexposed", groups ={"Unexposed"})
	public void testFilterCountCheckForUploadingOfUnexposedContentTypeFile(TestParameters testParams) throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();

		String contentType=testParams.getRiskType(); 
		List<String> contentTypes=testParams.getRisks();
		String fileName=testParams.getFileName();
		int countBeforeUI= testParams.getCountUI();
		int countBeforeAPI= testParams.getCountAPI();
		int countAfterExpectedUI=countBeforeUI+contentTypes.size();	
		int countAfterExpectedAPI=countBeforeAPI+contentTypes.size();		
		String saasType = SaasType.getSaasFilterType(suiteData.getSaasApp());
		String appType = SaasType.getAppType(suiteData.getSaasApp(), suiteData.getEnvironmentName());
		Map<String, String>  additionalParamsAPI = testParams.getHeadersAPI();
		Map<String, String>  additionalParamsUI =  testParams.getHeadersUI();

		Logger.info(
				"************************************ Starting the checking of increase in filter count"
						+ " for filename:" + fileName +" with content types:"+contentTypes+" and saas app type:" + saasType + " ******************");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description: Check the filter count increase after uploading of the content type file");
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output****************");
		Logger.info("Filename:"+fileName);Logger.info("Content Types to be validated:"+contentTypes);
		Logger.info("Current Filter Count from UI:"+ countBeforeUI);Logger.info("Expected Filter Count from UI:"+ (countAfterExpectedUI));
		Logger.info("Current Filter Count from API:"+ countBeforeAPI);Logger.info("Expected Filter Count from API:"+ (countAfterExpectedAPI));
		Logger.info("Saas App Type:"+suiteData.getSaasApp());
		Logger.info("*****************************************************");

		List<NameValuePair> headers = dciFunctions.getBasicHeaders(suiteData);	
		verifyRiskContentTypesDisplayLogs(dciFunctions, fileName, saasType, headers);

		ContentType cTypes = dciFunctions.getContentType(suiteData, restClient, additionalParamsUI);
		Logger.info("ContentType:" + contentType + "::Count:" + cTypes.getContentTypeCount(contentType));
		ContentTypes cType = dciFunctions.getContentTypes(suiteData, restClient, appType,additionalParamsAPI);
		Logger.info("ContentType:" + contentType + "::Count:" + cType.getContentTypeCount(contentType));
		int countAfterUI= cTypes.getContentTypeCount(contentType);
		int countAfterAPI= cType.getContentTypeCount(contentType);

		Logger.info("****************Actual Output****************");
		Logger.info("Expected Filter Count from UI:"+ (countAfterExpectedUI));Logger.info("Actual Filter Count from UI:"+ (countAfterUI));
		Logger.info("Expected Filter Count from API:"+ (countAfterExpectedAPI));Logger.info("Actual Filter Count from API:"+ (countAfterAPI));
		Logger.info("Saas App Type:"+suiteData.getSaasApp());
		Logger.info("*****************************************************");

		softAssert.assertEquals(countAfterUI, countAfterExpectedUI,"Content Type count is not getting incremented for file:"+fileName+ " via UI");
		softAssert.assertEquals(countAfterAPI, countAfterExpectedAPI,"Content Type count is not getting incremented for file:"+fileName+ " via API");
		softAssert.assertAll();

		Map<String, Integer> numberList=new HashMap<String, Integer>();
		numberList.put("ExpAPICount", countAfterExpectedAPI);numberList.put("ActAPICount", countAfterAPI);
		numberList.put("ExpUICount", countAfterExpectedUI);numberList.put("ActUICount", countAfterUI);
		//Assert.assertEquals(dciFunctions.numberComparison(numberList), "");
		
		Logger.info(
				"************************************ Completed the checking of increase in filter count"
						+ " for filename:" + fileName +" with content types:"+contentTypes+" and saas app type:" + saasType + " ******************");
	}

	@Priority(4)
	@Test(dataProvider = "dataDeleteContentTypeUnexposed", groups ={"Unexposed"})
	public void testFilterCountCheckForDeletingOfUnexposedContentTypeFile(TestParameters testParams) throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();

		String contentType=testParams.getRiskType(); 
		List<String> contentTypes=testParams.getRisks();
		String fileName=testParams.getFileName();
		int countBeforeUI= testParams.getCountUI();
		int countBeforeAPI= testParams.getCountAPI();
		int countAfterExpectedUI=countBeforeUI-contentTypes.size();
		int countAfterExpectedAPI=countBeforeAPI-contentTypes.size();		
		String saasType = SaasType.getSaasFilterType(suiteData.getSaasApp());
		String appType = SaasType.getAppType(suiteData.getSaasApp(), suiteData.getEnvironmentName());
		Map<String, String>  additionalParamsAPI = testParams.getHeadersAPI();
		Map<String, String>  additionalParamsUI =  testParams.getHeadersUI();

		Logger.info(
				"************************************ Starting the checking of decrease in filter count"
						+ " for filename:" + fileName +" with content types:"+contentTypes+" and saas app type:" + saasType + " ******************");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description: Check the filter count increase after deleting of the content type file");
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output****************");
		Logger.info("Filename:"+fileName);Logger.info("Content Types to be validated:"+contentTypes);
		Logger.info("Current Filter Count from UI:"+ countBeforeUI);Logger.info("Expected Filter Count from UI:"+ (countAfterExpectedUI));
		Logger.info("Current Filter Count from API:"+ countBeforeAPI);Logger.info("Expected Filter Count from API:"+ (countAfterExpectedAPI));
		Logger.info("Saas App Type:"+suiteData.getSaasApp());
		Logger.info("*****************************************************");

		ContentType cTypes = dciFunctions.getContentType(suiteData, restClient, additionalParamsUI);
		Logger.info("ContentType:" + contentType + "::Count:" + cTypes.getContentTypeCount(contentType));
		ContentTypes cType = dciFunctions.getContentTypes(suiteData, restClient, appType,additionalParamsAPI);
		Logger.info("ContentType:" + contentType + "::Count:" + cType.getContentTypeCount(contentType));
		int countAfterUI= cTypes.getContentTypeCount(contentType);
		int countAfterAPI= cType.getContentTypeCount(contentType);

		Logger.info("****************Actual Output****************");
		Logger.info("Expected Filter Count from UI:"+ (countAfterExpectedUI));Logger.info("Actual Filter Count from UI:"+ (countAfterUI));
		Logger.info("Expected Filter Count from API:"+ (countAfterExpectedAPI));Logger.info("Actual Filter Count from API:"+ (countAfterAPI));
		Logger.info("Saas App Type:"+suiteData.getSaasApp());
		Logger.info("*****************************************************");

		softAssert.assertEquals(countAfterUI, countAfterExpectedUI,"Content type count is not getting decremented for file:"+fileName+ " via UI");
		softAssert.assertEquals(countAfterAPI, countAfterExpectedAPI,"Content type count is not getting decremented for file:"+fileName+ " via API");
		softAssert.assertAll();

		Map<String, Integer> numberList=new HashMap<String, Integer>();
		numberList.put("ExpAPICount", countAfterExpectedAPI);numberList.put("ActAPICount", countAfterAPI);
		numberList.put("ExpUICount", countAfterExpectedUI);numberList.put("ActUICount", countAfterUI);
		//Assert.assertEquals(dciFunctions.numberComparison(numberList), "");
		
		Logger.info(
				"************************************ Completed the checking of decrease in filter count"
						+ " for filename:" + fileName +" with content types:"+contentTypes+" and saas app type:" + saasType + " ******************");
	}

	@Priority(5)
	@Test(dataProvider = "dataUploadRiskTypeExposedPublic", groups ={"ExposedPublic"})
	public void testFilterCountCheckForUploadingOfExposedPublicRiskTypeFile(TestParameters testParams) throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();

		String riskType=testParams.getRiskType(); 
		List<String> risks=testParams.getRisks();
		String fileName=testParams.getFileName();
		int countBeforeUI= testParams.getCountUI(); 
		int countBeforeAPI= testParams.getCountAPI(); 
		int countAfterExpectedUI=countBeforeUI+dciFunctions.getExpectedCount(fileName);	
		int countAfterExpectedAPI=countBeforeAPI+dciFunctions.getExpectedCount(fileName);	
		String saasType = SaasType.getSaasFilterType(suiteData.getSaasApp());
		String appType = SaasType.getAppType(suiteData.getSaasApp(), suiteData.getEnvironmentName());
		Map<String, String>  additionalParamsAPI = testParams.getHeadersAPI();
		Map<String, String>  additionalParamsUI =  testParams.getHeadersUI();

		Logger.info(
				"************************************ Starting the checking of increase in filter count"
						+ " for filename:" + fileName +" with risk types:"+risks+" and saas app type:" + saasType + " ******************");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description: Check the filter count increase after uploading of the risk file");
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output****************");
		Logger.info("Filename:"+fileName);Logger.info("Risk Types to be validated:"+risks);
		Logger.info("Current Filter Count from UI:"+ countBeforeUI);Logger.info("Expected Filter Count from UI:"+ (countAfterExpectedUI));
		Logger.info("Current Filter Count from API:"+ countBeforeAPI);Logger.info("Expected Filter Count from API:"+ (countAfterExpectedAPI));
		Logger.info("Saas App Type:"+suiteData.getSaasApp());
		Logger.info("*****************************************************");

		List<NameValuePair> headers = dciFunctions.getBasicHeaders(suiteData);	
		verifyRiskTypesDisplayLogs(dciFunctions, fileName, saasType, headers);

		Vulnerability vulnerability = dciFunctions.getVulnerability(suiteData, restClient, additionalParamsUI);
		Logger.info("Vulnerability:" + riskType + "::Count:" + vulnerability.getResults().getVulnerabilityCount(riskType));
		VulnerabilityTypes vulnerabilityTypes = dciFunctions.getVulnerabilityTypes(suiteData, restClient, appType, additionalParamsAPI);
		Logger.info("Vulnerability:" + riskType + "::Count:" + vulnerabilityTypes.getObjects().getVulnerabilityCount(riskType));
		int countAfterUI= vulnerability.getResults().getVulnerabilityCount(riskType);
		int countAfterAPI= vulnerabilityTypes.getObjects().getVulnerabilityCount(riskType);

		Logger.info("****************Actual Output****************");
		Logger.info("Expected Filter Count from UI:"+ (countBeforeUI+1));Logger.info("Actual Filter Count from UI:"+ (countAfterUI));
		Logger.info("Expected Filter Count from API:"+ (countBeforeAPI+1));Logger.info("Actual Filter Count from API:"+ (countAfterAPI));
		Logger.info("Saas App Type:"+suiteData.getSaasApp());
		Logger.info("*****************************************************");

		softAssert.assertEquals(countAfterUI, countAfterExpectedUI,"Risk count is not getting incremented for file:"+fileName+ " via UI");
		softAssert.assertEquals(countAfterAPI, countAfterExpectedAPI,"Risk count is not getting incremented for file:"+fileName+ " via API");
		softAssert.assertAll();

		Map<String, Integer> numberList=new HashMap<String, Integer>();
		numberList.put("ExpAPICount", countAfterExpectedAPI);numberList.put("ActAPICount", countAfterAPI);
		numberList.put("ExpUICount", countAfterExpectedUI);numberList.put("ActUICount", countAfterUI);
		//Assert.assertEquals(dciFunctions.numberComparison(numberList), "");
		
		Logger.info(
				"************************************ Completed the checking of increase in filter count"
						+ " for filename:" + fileName +" with risk types:"+risks+" and saas app type:" + saasType + " ******************");
	}

	@Priority(6)
	@Test(dataProvider = "dataDeleteRiskTypeExposedPublic", groups ={"ExposedPublic"})
	public void testFilterCountCheckForDeletionOfExposedPublicRiskTypeFile(TestParameters testParams) throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();

		String riskType=testParams.getRiskType(); 
		List<String> risks=testParams.getRisks();
		String fileName=testParams.getFileName();
		int countBeforeUI= testParams.getCountUI();
		int countBeforeAPI= testParams.getCountAPI();
		int countAfterExpectedUI=countBeforeUI-dciFunctions.getExpectedCount(fileName);	
		int countAfterExpectedAPI=countBeforeAPI-dciFunctions.getExpectedCount(fileName);		
		String saasType = SaasType.getSaasFilterType(suiteData.getSaasApp());
		String appType = SaasType.getAppType(suiteData.getSaasApp(), suiteData.getEnvironmentName());
		Map<String, String>  additionalParamsAPI = testParams.getHeadersAPI();
		Map<String, String>  additionalParamsUI =  testParams.getHeadersUI();

		Logger.info(
				"************************************ Starting the checking of decrease in filter count"
						+ " for filename:" + fileName +" with risk types:"+risks+" and saas app type:" + saasType + " ******************");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description: Check the filter count increase after deleting of the risk file");
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output****************");
		Logger.info("Filename:"+fileName);Logger.info("Risk Types to be validated:"+risks);
		Logger.info("Current Filter Count from UI:"+ countBeforeUI);Logger.info("Expected Filter Count from UI:"+ (countAfterExpectedUI));
		Logger.info("Current Filter Count from API:"+ countBeforeAPI);Logger.info("Expected Filter Count from API:"+ (countAfterExpectedAPI));
		Logger.info("Saas App Type:"+suiteData.getSaasApp());
		Logger.info("*****************************************************");

		Vulnerability vulnerability = dciFunctions.getVulnerability(suiteData, restClient, additionalParamsUI);
		Logger.info("Vulnerability:" + riskType + "::Count:" + vulnerability.getResults().getVulnerabilityCount(riskType));
		VulnerabilityTypes vulnerabilityTypes = dciFunctions.getVulnerabilityTypes(suiteData, restClient, appType, additionalParamsAPI);
		Logger.info("Vulnerability:" + riskType + "::Count:" + vulnerabilityTypes.getObjects().getVulnerabilityCount(riskType));
		int countAfterUI= vulnerability.getResults().getVulnerabilityCount(riskType);
		int countAfterAPI= vulnerabilityTypes.getObjects().getVulnerabilityCount(riskType);

		Logger.info("****************Actual Output****************");
		Logger.info("Expected Filter Count from UI:"+ (countAfterExpectedUI));Logger.info("Actual Filter Count from UI:"+ (countAfterUI));
		Logger.info("Expected Filter Count from API:"+ (countAfterExpectedAPI));Logger.info("Actual Filter Count from API:"+ (countAfterAPI));
		Logger.info("Saas App Type:"+suiteData.getSaasApp());
		Logger.info("*****************************************************");

		softAssert.assertEquals(countAfterUI, countAfterExpectedUI,"Risk count is not getting decremented for file:"+fileName+ " via UI");
		softAssert.assertEquals(countAfterAPI, countAfterExpectedAPI,"Risk count is not getting decremented for file:"+fileName+ " via API");
		softAssert.assertAll();

		Map<String, Integer> numberList=new HashMap<String, Integer>();
		numberList.put("ExpAPICount", countAfterExpectedAPI);numberList.put("ActAPICount", countAfterAPI);
		numberList.put("ExpUICount", countAfterExpectedUI);numberList.put("ActUICount", countAfterUI);
		//Assert.assertEquals(dciFunctions.numberComparison(numberList), "");
		
		Logger.info(
				"************************************ Completed the checking of decrease in filter count"
						+ " for filename:" + fileName +" with risk types:"+risks+" and saas app type:" + saasType + " ******************");
	}

	@Priority(7)
	@Test(dataProvider = "dataUploadContentTypeExposedPublic", groups ={"ExposedPublic"})
	public void testFilterCountCheckForUploadingOfExposedPublicContentTypeFile(TestParameters testParams) throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();

		String contentType=testParams.getRiskType(); 
		List<String> contentTypes=testParams.getRisks();
		String fileName=testParams.getFileName();
		int countBeforeUI= testParams.getCountUI();
		int countBeforeAPI= testParams.getCountAPI();
		int countAfterExpectedUI=countBeforeUI+contentTypes.size();	
		int countAfterExpectedAPI=countBeforeAPI+contentTypes.size();		
		String saasType = SaasType.getSaasFilterType(suiteData.getSaasApp());
		String appType = SaasType.getAppType(suiteData.getSaasApp(), suiteData.getEnvironmentName());
		Map<String, String>  additionalParamsAPI = testParams.getHeadersAPI();
		Map<String, String>  additionalParamsUI =  testParams.getHeadersUI();

		Logger.info(
				"************************************ Starting the checking of increase in filter count"
						+ " for filename:" + fileName +" with content types:"+contentTypes+" and saas app type:" + saasType + " ******************");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description: Check the filter count increase after uploading of the content type file");
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output****************");
		Logger.info("Filename:"+fileName);Logger.info("Content Types to be validated:"+contentTypes);
		Logger.info("Current Filter Count from UI:"+ countBeforeUI);Logger.info("Expected Filter Count from UI:"+ (countAfterExpectedUI));
		Logger.info("Current Filter Count from API:"+ countBeforeAPI);Logger.info("Expected Filter Count from API:"+ (countAfterExpectedAPI));
		Logger.info("Saas App Type:"+suiteData.getSaasApp());
		Logger.info("*****************************************************");

		List<NameValuePair> headers = dciFunctions.getBasicHeaders(suiteData);	
		verifyContentTypesDisplayLogs(dciFunctions, fileName, saasType, headers);

		ContentType cTypes = dciFunctions.getContentType(suiteData, restClient, additionalParamsUI);
		Logger.info("ContentType:" + contentType + "::Count:" + cTypes.getContentTypeCount(contentType));
		ContentTypes cType = dciFunctions.getContentTypes(suiteData, restClient, appType,additionalParamsAPI);
		Logger.info("ContentType:" + contentType + "::Count:" + cType.getContentTypeCount(contentType));
		int countAfterUI= cTypes.getContentTypeCount(contentType);
		int countAfterAPI= cType.getContentTypeCount(contentType);

		Logger.info("****************Actual Output****************");
		Logger.info("Expected Filter Count from UI:"+ (countAfterExpectedUI));Logger.info("Actual Filter Count from UI:"+ (countAfterUI));
		Logger.info("Expected Filter Count from API:"+ (countAfterExpectedAPI));Logger.info("Actual Filter Count from API:"+ (countAfterAPI));
		Logger.info("Saas App Type:"+suiteData.getSaasApp());
		Logger.info("*****************************************************");

		softAssert.assertEquals(countAfterUI, countAfterExpectedUI,"Content Type count is not getting incremented for file:"+fileName+ " via UI");
		softAssert.assertEquals(countAfterAPI, countAfterExpectedAPI,"Content Type count is not getting incremented for file:"+fileName+ " via API");
		softAssert.assertAll();

		Map<String, Integer> numberList=new HashMap<String, Integer>();
		numberList.put("ExpAPICount", countAfterExpectedAPI);numberList.put("ActAPICount", countAfterAPI);
		numberList.put("ExpUICount", countAfterExpectedUI);numberList.put("ActUICount", countAfterUI);
		//Assert.assertEquals(dciFunctions.numberComparison(numberList), "");
		
		Logger.info(
				"************************************ Completed the checking of increase in filter count"
						+ " for filename:" + fileName +" with content types:"+contentTypes+" and saas app type:" + saasType + " ******************");
	}

	@Priority(8)
	@Test(dataProvider = "dataDeleteContentTypeExposedPublic", groups ={"ExposedPublic"})
	public void testFilterCountCheckForDeletingOfExposedPublicContentTypeFile(TestParameters testParams) throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();

		String contentType=testParams.getRiskType(); 
		List<String> contentTypes=testParams.getRisks();
		String fileName=testParams.getFileName();
		int countBeforeUI= testParams.getCountUI();
		int countBeforeAPI= testParams.getCountAPI();
		int countAfterExpectedUI=countBeforeUI-contentTypes.size();	
		int countAfterExpectedAPI=countBeforeAPI-contentTypes.size();		
		String saasType = SaasType.getSaasFilterType(suiteData.getSaasApp());
		String appType = SaasType.getAppType(suiteData.getSaasApp(), suiteData.getEnvironmentName());
		Map<String, String>  additionalParamsAPI = testParams.getHeadersAPI();
		Map<String, String>  additionalParamsUI =  testParams.getHeadersUI();

		Logger.info(
				"************************************ Starting the checking of decrease in filter count"
						+ " for filename:" + fileName +" with content types:"+contentTypes+" and saas app type:" + saasType + " ******************");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description: Check the filter count increase after deleting of the content type file");
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output****************");
		Logger.info("Filename:"+fileName);Logger.info("Content Types to be validated:"+contentTypes);
		Logger.info("Current Filter Count from UI:"+ countBeforeUI);Logger.info("Expected Filter Count from UI:"+ (countAfterExpectedUI));
		Logger.info("Current Filter Count from API:"+ countBeforeAPI);Logger.info("Expected Filter Count from API:"+ (countAfterExpectedAPI));
		Logger.info("Saas App Type:"+suiteData.getSaasApp());
		Logger.info("*****************************************************");

		ContentType cTypes = dciFunctions.getContentType(suiteData, restClient, additionalParamsUI);
		Logger.info("ContentType:" + contentType + "::Count:" + cTypes.getContentTypeCount(contentType));
		ContentTypes cType = dciFunctions.getContentTypes(suiteData, restClient, appType,additionalParamsAPI);
		Logger.info("ContentType:" + contentType + "::Count:" + cType.getContentTypeCount(contentType));
		int countAfterUI= cTypes.getContentTypeCount(contentType);
		int countAfterAPI= cType.getContentTypeCount(contentType);

		Logger.info("****************Actual Output****************");
		Logger.info("Expected Filter Count from UI:"+ (countAfterExpectedUI));Logger.info("Actual Filter Count from API:"+ (countAfterUI));
		Logger.info("Expected Filter Count from API:"+ (countAfterExpectedAPI));Logger.info("Actual Filter Count from API:"+ (countAfterAPI));
		Logger.info("Saas App Type:"+suiteData.getSaasApp());
		Logger.info("*****************************************************");

		softAssert.assertEquals(countAfterUI, countAfterExpectedUI,"Content type count is not getting decremented for file:"+fileName+ " via UI");
		softAssert.assertEquals(countAfterAPI, countAfterExpectedAPI,"Content type count is not getting decremented for file:"+fileName+ " via API");
		softAssert.assertAll();

		Map<String, Integer> numberList=new HashMap<String, Integer>();
		numberList.put("ExpAPICount", countAfterExpectedAPI);numberList.put("ActAPICount", countAfterAPI);
		numberList.put("ExpUICount", countAfterExpectedUI);numberList.put("ActUICount", countAfterUI);
		//Assert.assertEquals(dciFunctions.numberComparison(numberList), "");
		
		Logger.info(
				"************************************ Completed the checking of decrease in filter count"
						+ " for filename:" + fileName +" with content types:"+contentTypes+" and saas app type:" + saasType + " ******************");
	}

	@Priority(9)
	@Test(dataProvider = "dataUploadRiskTypeExposedInternal", groups ={"ExposedInternal"})
	public void testFilterCountCheckForUploadingOfExposedInternalRiskTypeFile(TestParameters testParams) throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();

		String riskType=testParams.getRiskType(); 
		List<String> risks=testParams.getRisks();
		String fileName=testParams.getFileName();
		int countBeforeUI= testParams.getCountUI(); 
		int countBeforeAPI= testParams.getCountAPI(); 
		int countAfterExpectedUI=countBeforeUI+dciFunctions.getExpectedCount(fileName);	
		int countAfterExpectedAPI=countBeforeAPI+dciFunctions.getExpectedCount(fileName);		
		String saasType = SaasType.getSaasFilterType(suiteData.getSaasApp());
		String appType = SaasType.getAppType(suiteData.getSaasApp(), suiteData.getEnvironmentName());
		Map<String, String>  additionalParamsAPI = testParams.getHeadersAPI();
		Map<String, String>  additionalParamsUI =  testParams.getHeadersUI();

		Logger.info(
				"************************************ Starting the checking of increase in filter count"
						+ " for filename:" + fileName +" with risk types:"+risks+" and saas app type:" + saasType + " ******************");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description: Check the filter count increase after uploading of the risk file");
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output****************");
		Logger.info("Filename:"+fileName);Logger.info("Risk Types to be validated:"+risks);
		Logger.info("Current Filter Count from UI:"+ countBeforeUI);Logger.info("Expected Filter Count from UI:"+ (countAfterExpectedUI));
		Logger.info("Current Filter Count from API:"+ countBeforeAPI);Logger.info("Expected Filter Count from API:"+ (countAfterExpectedAPI));
		Logger.info("Saas App Type:"+suiteData.getSaasApp());
		Logger.info("*****************************************************");

		List<NameValuePair> headers = dciFunctions.getBasicHeaders(suiteData);	
		verifyRiskTypesDisplayLogs(dciFunctions, fileName, saasType, headers);

		Vulnerability vulnerability = dciFunctions.getVulnerability(suiteData, restClient, additionalParamsUI);
		Logger.info("Vulnerability:" + riskType + "::Count:" + vulnerability.getResults().getVulnerabilityCount(riskType));
		VulnerabilityTypes vulnerabilityTypes = dciFunctions.getVulnerabilityTypes(suiteData, restClient, appType, additionalParamsAPI);
		Logger.info("Vulnerability:" + riskType + "::Count:" + vulnerabilityTypes.getObjects().getVulnerabilityCount(riskType));
		int countAfterUI= vulnerability.getResults().getVulnerabilityCount(riskType);
		int countAfterAPI= vulnerabilityTypes.getObjects().getVulnerabilityCount(riskType);

		Logger.info("****************Actual Output****************");
		Logger.info("Expected Filter Count from UI:"+ (countBeforeUI+1));Logger.info("Actual Filter Count from UI:"+ (countAfterUI));
		Logger.info("Expected Filter Count from API:"+ (countBeforeAPI+1));Logger.info("Actual Filter Count from API:"+ (countAfterAPI));
		Logger.info("Saas App Type:"+suiteData.getSaasApp());
		Logger.info("*****************************************************");

		softAssert.assertEquals(countAfterUI, countAfterExpectedUI,"Risk count is not getting incremented for file:"+fileName+ " via UI");
		softAssert.assertEquals(countAfterAPI, countAfterExpectedAPI,"Risk count is not getting incremented for file:"+fileName+ " via API");
		softAssert.assertAll();

		Map<String, Integer> numberList=new HashMap<String, Integer>();
		numberList.put("ExpAPICount", countAfterExpectedAPI);numberList.put("ActAPICount", countAfterAPI);
		numberList.put("ExpUICount", countAfterExpectedUI);numberList.put("ActUICount", countAfterUI);
		//Assert.assertEquals(dciFunctions.numberComparison(numberList), "");
		
		Logger.info(
				"************************************ Completed the checking of increase in filter count"
						+ " for filename:" + fileName +" with risk types:"+risks+" and saas app type:" + saasType + " ******************");
	}

	@Priority(10)
	@Test(dataProvider = "dataDeleteRiskTypeExposedInternal", groups ={"ExposedInternal"})
	public void testFilterCountCheckForDeletionOfExposedInternalRiskTypeFile(TestParameters testParams) throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();

		String riskType=testParams.getRiskType(); 
		List<String> risks=testParams.getRisks();
		String fileName=testParams.getFileName();
		int countBeforeUI= testParams.getCountUI();
		int countBeforeAPI= testParams.getCountAPI();
		int countAfterExpectedUI=countBeforeUI-dciFunctions.getExpectedCount(fileName);
		int countAfterExpectedAPI=countBeforeAPI-dciFunctions.getExpectedCount(fileName);		
		String saasType = SaasType.getSaasFilterType(suiteData.getSaasApp());
		String appType = SaasType.getAppType(suiteData.getSaasApp(), suiteData.getEnvironmentName());
		Map<String, String>  additionalParamsAPI = testParams.getHeadersAPI();
		Map<String, String>  additionalParamsUI =  testParams.getHeadersUI();

		Logger.info(
				"************************************ Starting the checking of decrease in filter count"
						+ " for filename:" + fileName +" with risk types:"+risks+" and saas app type:" + saasType + " ******************");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description: Check the filter count increase after deleting of the risk file");
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output****************");
		Logger.info("Filename:"+fileName);Logger.info("Risk Types to be validated:"+risks);
		Logger.info("Current Filter Count from UI:"+ countBeforeUI);Logger.info("Expected Filter Count from UI:"+ (countAfterExpectedUI));
		Logger.info("Current Filter Count from API:"+ countBeforeAPI);Logger.info("Expected Filter Count from API:"+ (countAfterExpectedAPI));
		Logger.info("Saas App Type:"+suiteData.getSaasApp());
		Logger.info("*****************************************************");

		Vulnerability vulnerability = dciFunctions.getVulnerability(suiteData, restClient, additionalParamsUI);
		Logger.info("Vulnerability:" + riskType + "::Count:" + vulnerability.getResults().getVulnerabilityCount(riskType));
		VulnerabilityTypes vulnerabilityTypes = dciFunctions.getVulnerabilityTypes(suiteData, restClient, appType, additionalParamsAPI);
		Logger.info("Vulnerability:" + riskType + "::Count:" + vulnerabilityTypes.getObjects().getVulnerabilityCount(riskType));
		int countAfterUI= vulnerability.getResults().getVulnerabilityCount(riskType);
		int countAfterAPI= vulnerabilityTypes.getObjects().getVulnerabilityCount(riskType);

		Logger.info("****************Actual Output****************");
		Logger.info("Expected Filter Count from UI:"+ (countAfterExpectedUI));Logger.info("Actual Filter Count from UI:"+ (countAfterUI));
		Logger.info("Expected Filter Count from API:"+ (countAfterExpectedAPI));Logger.info("Actual Filter Count from API:"+ (countAfterAPI));
		Logger.info("Saas App Type:"+suiteData.getSaasApp());
		Logger.info("*****************************************************");

		softAssert.assertEquals(countAfterUI, countAfterExpectedUI,"Risk count is not getting decremented for file:"+fileName+ " via UI");
		softAssert.assertEquals(countAfterAPI, countAfterExpectedAPI,"Risk count is not getting decremented for file:"+fileName+ " via API");
		softAssert.assertAll();

		Map<String, Integer> numberList=new HashMap<String, Integer>();
		numberList.put("ExpAPICount", countAfterExpectedAPI);numberList.put("ActAPICount", countAfterAPI);
		numberList.put("ExpUICount", countAfterExpectedUI);numberList.put("ActUICount", countAfterUI);
		//Assert.assertEquals(dciFunctions.numberComparison(numberList), "");
		
		Logger.info(
				"************************************ Completed the checking of decrease in filter count"
						+ " for filename:" + fileName +" with risk types:"+risks+" and saas app type:" + saasType + " ******************");
	}

	@Priority(11)
	@Test(dataProvider = "dataUploadContentTypeExposedInternal", groups ={"ExposedInternal"})
	public void testFilterCountCheckForUploadingOfExposedInternalContentTypeFile(TestParameters testParams) throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();

		String contentType=testParams.getRiskType(); 
		List<String> contentTypes=testParams.getRisks();
		String fileName=testParams.getFileName();
		int countBeforeUI= testParams.getCountUI();
		int countBeforeAPI= testParams.getCountAPI();
		int countAfterExpectedUI=countBeforeUI+contentTypes.size();	
		int countAfterExpectedAPI=countBeforeAPI+contentTypes.size();	
		String saasType = SaasType.getSaasFilterType(suiteData.getSaasApp());
		String appType = SaasType.getAppType(suiteData.getSaasApp(), suiteData.getEnvironmentName());
		Map<String, String>  additionalParamsAPI = testParams.getHeadersAPI();
		Map<String, String>  additionalParamsUI =  testParams.getHeadersUI();

		Logger.info(
				"************************************ Starting the checking of increase in filter count"
						+ " for filename:" + fileName +" with content types:"+contentTypes+" and saas app type:" + saasType + " ******************");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description: Check the filter count increase after uploading of the content type file");
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output****************");
		Logger.info("Filename:"+fileName);Logger.info("Content Types to be validated:"+contentTypes);
		Logger.info("Current Filter Count from UI:"+ countBeforeUI);Logger.info("Expected Filter Count from UI:"+ (countAfterExpectedUI));
		Logger.info("Current Filter Count from API:"+ countBeforeAPI);Logger.info("Expected Filter Count from API:"+ (countAfterExpectedAPI));
		Logger.info("Saas App Type:"+suiteData.getSaasApp());
		Logger.info("*****************************************************");

		List<NameValuePair> headers = dciFunctions.getBasicHeaders(suiteData);	
		verifyContentTypesDisplayLogs(dciFunctions, fileName, saasType, headers);

		ContentType cTypes = dciFunctions.getContentType(suiteData, restClient, additionalParamsUI);
		Logger.info("ContentType:" + contentType + "::Count:" + cTypes.getContentTypeCount(contentType));
		ContentTypes cType = dciFunctions.getContentTypes(suiteData, restClient, appType,additionalParamsAPI);
		Logger.info("ContentType:" + contentType + "::Count:" + cType.getContentTypeCount(contentType));
		int countAfterUI= cTypes.getContentTypeCount(contentType);
		int countAfterAPI= cType.getContentTypeCount(contentType);

		Logger.info("****************Actual Output****************");
		Logger.info("Expected Filter Count from UI:"+ (countAfterExpectedUI));Logger.info("Actual Filter Count from UI:"+ (countAfterUI));
		Logger.info("Expected Filter Count from API:"+ (countAfterExpectedAPI));Logger.info("Actual Filter Count from API:"+ (countAfterAPI));
		Logger.info("Saas App Type:"+suiteData.getSaasApp());
		Logger.info("*****************************************************");

		softAssert.assertEquals(countAfterUI, countAfterExpectedUI,"Content Type count is not getting incremented for file:"+fileName+ " via UI");
		softAssert.assertEquals(countAfterAPI, countAfterExpectedAPI,"Content Type count is not getting incremented for file:"+fileName+ " via API");
		softAssert.assertAll();

		Map<String, Integer> numberList=new HashMap<String, Integer>();
		numberList.put("ExpAPICount", countAfterExpectedAPI);numberList.put("ActAPICount", countAfterAPI);
		numberList.put("ExpUICount", countAfterExpectedUI);numberList.put("ActUICount", countAfterUI);
		//Assert.assertEquals(dciFunctions.numberComparison(numberList), "");
		
		Logger.info(
				"************************************ Completed the checking of increase in filter count"
						+ " for filename:" + fileName +" with content types:"+contentTypes+" and saas app type:" + saasType + " ******************");
	}

	@Priority(12)
	@Test(dataProvider = "dataDeleteContentTypeExposedInternal", groups ={"ExposedInternal"})
	public void testFilterCountCheckForDeletingOfExposedInternalContentTypeFile(TestParameters testParams) throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();

		String contentType=testParams.getRiskType(); 
		List<String> contentTypes=testParams.getRisks();
		String fileName=testParams.getFileName();
		int countBeforeUI= testParams.getCountUI();
		int countBeforeAPI= testParams.getCountAPI();
		int countAfterExpectedUI=countBeforeUI-contentTypes.size();	
		int countAfterExpectedAPI=countBeforeAPI-contentTypes.size();		
		String saasType = SaasType.getSaasFilterType(suiteData.getSaasApp());
		String appType = SaasType.getAppType(suiteData.getSaasApp(), suiteData.getEnvironmentName());
		Map<String, String>  additionalParamsAPI = testParams.getHeadersAPI();
		Map<String, String>  additionalParamsUI =  testParams.getHeadersUI();

		Logger.info(
				"************************************ Starting the checking of decrease in filter count"
						+ " for filename:" + fileName +" with content types:"+contentTypes+" and saas app type:" + saasType + " ******************");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description: Check the filter count increase after deleting of the content type file");
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output****************");
		Logger.info("Filename:"+fileName);Logger.info("Content Types to be validated:"+contentTypes);
		Logger.info("Current Filter Count from UI:"+ countBeforeUI);Logger.info("Expected Filter Count from UI:"+ (countAfterExpectedUI));
		Logger.info("Current Filter Count from API:"+ countBeforeAPI);Logger.info("Expected Filter Count from API:"+ (countAfterExpectedAPI));
		Logger.info("Saas App Type:"+suiteData.getSaasApp());
		Logger.info("*****************************************************");

		ContentType cTypes = dciFunctions.getContentType(suiteData, restClient, additionalParamsUI);
		Logger.info("ContentType:" + contentType + "::Count:" + cTypes.getContentTypeCount(contentType));
		ContentTypes cType = dciFunctions.getContentTypes(suiteData, restClient, appType,additionalParamsAPI);
		Logger.info("ContentType:" + contentType + "::Count:" + cType.getContentTypeCount(contentType));
		int countAfterUI= cTypes.getContentTypeCount(contentType);
		int countAfterAPI= cType.getContentTypeCount(contentType);

		Logger.info("****************Actual Output****************");
		Logger.info("Expected Filter Count from UI:"+ (countAfterExpectedUI));Logger.info("Actual Filter Count from API:"+ (countAfterUI));
		Logger.info("Expected Filter Count from API:"+ (countAfterExpectedAPI));Logger.info("Actual Filter Count from API:"+ (countAfterAPI));
		Logger.info("Saas App Type:"+suiteData.getSaasApp());
		Logger.info("*****************************************************");

		softAssert.assertEquals(countAfterUI, countAfterExpectedUI,"Content type count is not getting decremented for file:"+fileName+ " via UI");
		softAssert.assertEquals(countAfterAPI, countAfterExpectedAPI,"Content type count is not getting decremented for file:"+fileName+ " via API");
		softAssert.assertAll();

		Map<String, Integer> numberList=new HashMap<String, Integer>();
		numberList.put("ExpAPICount", countAfterExpectedAPI);numberList.put("ActAPICount", countAfterAPI);
		numberList.put("ExpUICount", countAfterExpectedUI);numberList.put("ActUICount", countAfterUI);
		//Assert.assertEquals(dciFunctions.numberComparison(numberList), "");
		
		Logger.info(
				"************************************ Completed the checking of decrease in filter count"
						+ " for filename:" + fileName +" with content types:"+contentTypes+" and saas app type:" + saasType + " ******************");
	}

	@Priority(13)
	@Test(dataProvider = "dataUploadRiskTypeExposedExternal", groups ={"ExposedExternal"})
	public void testFilterCountCheckForUploadingOfExposedExternalRiskTypeFile(TestParameters testParams) throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();

		String riskType=testParams.getRiskType(); 
		List<String> risks=testParams.getRisks();
		String fileName=testParams.getFileName();
		int countBeforeUI= testParams.getCountUI(); 
		int countBeforeAPI= testParams.getCountAPI(); 
		int countAfterExpectedUI=countBeforeUI+dciFunctions.getExpectedCount(fileName);	
		int countAfterExpectedAPI=countBeforeAPI+dciFunctions.getExpectedCount(fileName);		
		String saasType = SaasType.getSaasFilterType(suiteData.getSaasApp());
		String appType = SaasType.getAppType(suiteData.getSaasApp(), suiteData.getEnvironmentName());
		Map<String, String>  additionalParamsAPI = testParams.getHeadersAPI();
		Map<String, String>  additionalParamsUI =  testParams.getHeadersUI();

		Logger.info(
				"************************************ Starting the checking of increase in filter count"
						+ " for filename:" + fileName +" with risk types:"+risks+" and saas app type:" + saasType + " ******************");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description: Check the filter count increase after uploading of the risk file");
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output****************");
		Logger.info("Filename:"+fileName);Logger.info("Risk Types to be validated:"+risks);
		Logger.info("Current Filter Count from UI:"+ countBeforeUI);Logger.info("Expected Filter Count from UI:"+ (countAfterExpectedUI));
		Logger.info("Current Filter Count from API:"+ countBeforeAPI);Logger.info("Expected Filter Count from API:"+ (countAfterExpectedAPI));
		Logger.info("Saas App Type:"+suiteData.getSaasApp());
		Logger.info("*****************************************************");

		List<NameValuePair> headers = dciFunctions.getBasicHeaders(suiteData);	
		verifyRiskTypesDisplayLogs(dciFunctions, fileName, saasType, headers);

		Vulnerability vulnerability = dciFunctions.getVulnerability(suiteData, restClient, additionalParamsUI);
		Logger.info("Vulnerability:" + riskType + "::Count:" + vulnerability.getResults().getVulnerabilityCount(riskType));
		VulnerabilityTypes vulnerabilityTypes = dciFunctions.getVulnerabilityTypes(suiteData, restClient, appType, additionalParamsAPI);
		Logger.info("Vulnerability:" + riskType + "::Count:" + vulnerabilityTypes.getObjects().getVulnerabilityCount(riskType));
		int countAfterUI= vulnerability.getResults().getVulnerabilityCount(riskType);
		int countAfterAPI= vulnerabilityTypes.getObjects().getVulnerabilityCount(riskType);

		Logger.info("****************Actual Output****************");
		Logger.info("Expected Filter Count from UI:"+ (countBeforeUI+1));Logger.info("Actual Filter Count from UI:"+ (countAfterUI));
		Logger.info("Expected Filter Count from API:"+ (countBeforeAPI+1));Logger.info("Actual Filter Count from API:"+ (countAfterAPI));
		Logger.info("Saas App Type:"+suiteData.getSaasApp());
		Logger.info("*****************************************************");

		softAssert.assertEquals(countAfterUI, countAfterExpectedUI,"Risk count is not getting incremented for file:"+fileName+ " via UI");
		softAssert.assertEquals(countAfterAPI, countAfterExpectedAPI,"Risk count is not getting incremented for file:"+fileName+ " via API");
		softAssert.assertAll();

		Map<String, Integer> numberList=new HashMap<String, Integer>();
		numberList.put("ExpAPICount", countAfterExpectedAPI);numberList.put("ActAPICount", countAfterAPI);
		numberList.put("ExpUICount", countAfterExpectedUI);numberList.put("ActUICount", countAfterUI);
		//Assert.assertEquals(dciFunctions.numberComparison(numberList), "");
		
		Logger.info(
				"************************************ Completed the checking of increase in filter count"
						+ " for filename:" + fileName +" with risk types:"+risks+" and saas app type:" + saasType + " ******************");
	}

	@Priority(14)
	@Test(dataProvider = "dataDeleteRiskTypeExposedExternal", groups ={"ExposedExternal"})
	public void testFilterCountCheckForDeletionOfExposedExternalRiskTypeFile(TestParameters testParams) throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();

		String riskType=testParams.getRiskType(); 
		List<String> risks=testParams.getRisks();
		String fileName=testParams.getFileName();
		int countBeforeUI= testParams.getCountUI();
		int countBeforeAPI= testParams.getCountAPI();
		int countAfterExpectedUI=countBeforeUI-dciFunctions.getExpectedCount(fileName);
		int countAfterExpectedAPI=countBeforeAPI-dciFunctions.getExpectedCount(fileName);	
		String saasType = SaasType.getSaasFilterType(suiteData.getSaasApp());
		String appType = SaasType.getAppType(suiteData.getSaasApp(), suiteData.getEnvironmentName());
		Map<String, String>  additionalParamsAPI = testParams.getHeadersAPI();
		Map<String, String>  additionalParamsUI =  testParams.getHeadersUI();

		Logger.info(
				"************************************ Starting the checking of decrease in filter count"
						+ " for filename:" + fileName +" with risk types:"+risks+" and saas app type:" + saasType + " ******************");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description: Check the filter count increase after deleting of the risk file");
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output****************");
		Logger.info("Filename:"+fileName);Logger.info("Risk Types to be validated:"+risks);
		Logger.info("Current Filter Count from UI:"+ countBeforeUI);Logger.info("Expected Filter Count from UI:"+ (countAfterExpectedUI));
		Logger.info("Current Filter Count from API:"+ countBeforeAPI);Logger.info("Expected Filter Count from API:"+ (countAfterExpectedAPI));
		Logger.info("Saas App Type:"+suiteData.getSaasApp());
		Logger.info("*****************************************************");

		Vulnerability vulnerability = dciFunctions.getVulnerability(suiteData, restClient, additionalParamsUI);
		Logger.info("Vulnerability:" + riskType + "::Count:" + vulnerability.getResults().getVulnerabilityCount(riskType));
		VulnerabilityTypes vulnerabilityTypes = dciFunctions.getVulnerabilityTypes(suiteData, restClient, appType, additionalParamsAPI);
		Logger.info("Vulnerability:" + riskType + "::Count:" + vulnerabilityTypes.getObjects().getVulnerabilityCount(riskType));
		int countAfterUI= vulnerability.getResults().getVulnerabilityCount(riskType);
		int countAfterAPI= vulnerabilityTypes.getObjects().getVulnerabilityCount(riskType);

		Logger.info("****************Actual Output****************");
		Logger.info("Expected Filter Count from UI:"+ (countAfterExpectedUI));Logger.info("Actual Filter Count from UI:"+ (countAfterUI));
		Logger.info("Expected Filter Count from API:"+ (countAfterExpectedAPI));Logger.info("Actual Filter Count from API:"+ (countAfterAPI));
		Logger.info("Saas App Type:"+suiteData.getSaasApp());
		Logger.info("*****************************************************");

		softAssert.assertEquals(countAfterUI, countAfterExpectedUI,"Risk count is not getting decremented for file:"+fileName+ " via UI");
		softAssert.assertEquals(countAfterAPI, countAfterExpectedAPI,"Risk count is not getting decremented for file:"+fileName+ " via API");
		softAssert.assertAll();

		Map<String, Integer> numberList=new HashMap<String, Integer>();
		numberList.put("ExpAPICount", countAfterExpectedAPI);numberList.put("ActAPICount", countAfterAPI);
		numberList.put("ExpUICount", countAfterExpectedUI);numberList.put("ActUICount", countAfterUI);
		//Assert.assertEquals(dciFunctions.numberComparison(numberList), "");
		
		Logger.info(
				"************************************ Completed the checking of decrease in filter count"
						+ " for filename:" + fileName +" with risk types:"+risks+" and saas app type:" + saasType + " ******************");
	}

	@Priority(15)
	@Test(dataProvider = "dataUploadContentTypeExposedExternal", groups ={"ExposedExternal"})
	public void testFilterCountCheckForUploadingOfExposedExternalContentTypeFile(TestParameters testParams) throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();

		String contentType=testParams.getRiskType(); 
		List<String> contentTypes=testParams.getRisks();
		String fileName=testParams.getFileName();
		int countBeforeUI= testParams.getCountUI();
		int countBeforeAPI= testParams.getCountAPI();
		int countAfterExpectedUI=countBeforeUI+contentTypes.size();		
		int countAfterExpectedAPI=countBeforeAPI+contentTypes.size();	
		String saasType = SaasType.getSaasFilterType(suiteData.getSaasApp());
		String appType = SaasType.getAppType(suiteData.getSaasApp(), suiteData.getEnvironmentName());
		Map<String, String>  additionalParamsAPI = testParams.getHeadersAPI();
		Map<String, String>  additionalParamsUI =  testParams.getHeadersUI();

		Logger.info(
				"************************************ Starting the checking of increase in filter count"
						+ " for filename:" + fileName +" with content types:"+contentTypes+" and saas app type:" + saasType + " ******************");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description: Check the filter count increase after uploading of the content type file");
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output****************");
		Logger.info("Filename:"+fileName);Logger.info("Content Types to be validated:"+contentTypes);
		Logger.info("Current Filter Count from UI:"+ countBeforeUI);Logger.info("Expected Filter Count from UI:"+ (countAfterExpectedUI));
		Logger.info("Current Filter Count from API:"+ countBeforeAPI);Logger.info("Expected Filter Count from API:"+ (countAfterExpectedAPI));
		Logger.info("Saas App Type:"+suiteData.getSaasApp());
		Logger.info("*****************************************************");

		List<NameValuePair> headers = dciFunctions.getBasicHeaders(suiteData);	
		verifyContentTypesDisplayLogs(dciFunctions, fileName, saasType, headers);

		ContentType cTypes = dciFunctions.getContentType(suiteData, restClient, additionalParamsUI);
		Logger.info("ContentType:" + contentType + "::Count:" + cTypes.getContentTypeCount(contentType));
		ContentTypes cType = dciFunctions.getContentTypes(suiteData, restClient, appType,additionalParamsAPI);
		Logger.info("ContentType:" + contentType + "::Count:" + cType.getContentTypeCount(contentType));
		int countAfterUI= cTypes.getContentTypeCount(contentType);
		int countAfterAPI= cType.getContentTypeCount(contentType);

		Logger.info("****************Actual Output****************");
		Logger.info("Expected Filter Count from UI:"+ (countAfterExpectedUI));Logger.info("Actual Filter Count from UI:"+ (countAfterUI));
		Logger.info("Expected Filter Count from API:"+ (countAfterExpectedAPI));Logger.info("Actual Filter Count from API:"+ (countAfterAPI));
		Logger.info("Saas App Type:"+suiteData.getSaasApp());
		Logger.info("*****************************************************");

		softAssert.assertEquals(countAfterUI, countAfterExpectedUI,"Content Type count is not getting incremented for file:"+fileName+ " via UI");
		softAssert.assertEquals(countAfterAPI, countAfterExpectedAPI,"Content Type count is not getting incremented for file:"+fileName+ " via API");
		softAssert.assertAll();

		Map<String, Integer> numberList=new HashMap<String, Integer>();
		numberList.put("ExpAPICount", countAfterExpectedAPI);numberList.put("ActAPICount", countAfterAPI);
		numberList.put("ExpUICount", countAfterExpectedUI);numberList.put("ActUICount", countAfterUI);
		//Assert.assertEquals(dciFunctions.numberComparison(numberList), "");
		
		Logger.info(
				"************************************ Completed the checking of increase in filter count"
						+ " for filename:" + fileName +" with content types:"+contentTypes+" and saas app type:" + saasType + " ******************");
	}

	@Priority(16)
	@Test(dataProvider = "dataDeleteContentTypeExposedExternal", groups ={"ExposedExternal"})
	public void testFilterCountCheckForDeletingOfExposedExternalContentTypeFile(TestParameters testParams) throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();

		String contentType=testParams.getRiskType(); 
		List<String> contentTypes=testParams.getRisks();
		String fileName=testParams.getFileName();
		int countBeforeUI= testParams.getCountUI();
		int countBeforeAPI= testParams.getCountAPI();
		int countAfterExpectedUI=countBeforeUI-contentTypes.size();	
		int countAfterExpectedAPI=countBeforeAPI-contentTypes.size();		
		String saasType = SaasType.getSaasFilterType(suiteData.getSaasApp());
		String appType = SaasType.getAppType(suiteData.getSaasApp(), suiteData.getEnvironmentName());
		Map<String, String>  additionalParamsAPI = testParams.getHeadersAPI();
		Map<String, String>  additionalParamsUI =  testParams.getHeadersUI();

		Logger.info(
				"************************************ Starting the checking of decrease in filter count"
						+ " for filename:" + fileName +" with content types:"+contentTypes+" and saas app type:" + saasType + " ******************");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description: Check the filter count increase after deleting of the content type file");
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output****************");
		Logger.info("Filename:"+fileName);Logger.info("Content Types to be validated:"+contentTypes);
		Logger.info("Current Filter Count from UI:"+ countBeforeUI);Logger.info("Expected Filter Count from UI:"+ (countAfterExpectedUI));
		Logger.info("Current Filter Count from API:"+ countBeforeAPI);Logger.info("Expected Filter Count from API:"+ (countAfterExpectedAPI));
		Logger.info("Saas App Type:"+suiteData.getSaasApp());
		Logger.info("*****************************************************");

		ContentType cTypes = dciFunctions.getContentType(suiteData, restClient, additionalParamsUI);
		Logger.info("ContentType:" + contentType + "::Count:" + cTypes.getContentTypeCount(contentType));
		ContentTypes cType = dciFunctions.getContentTypes(suiteData, restClient, appType,additionalParamsAPI);
		Logger.info("ContentType:" + contentType + "::Count:" + cType.getContentTypeCount(contentType));
		int countAfterUI= cTypes.getContentTypeCount(contentType);
		int countAfterAPI= cType.getContentTypeCount(contentType);

		Logger.info("****************Actual Output****************");
		Logger.info("Expected Filter Count from UI:"+ (countAfterExpectedUI));Logger.info("Actual Filter Count from API:"+ (countAfterUI));
		Logger.info("Expected Filter Count from API:"+ (countAfterExpectedAPI));Logger.info("Actual Filter Count from API:"+ (countAfterAPI));
		Logger.info("Saas App Type:"+suiteData.getSaasApp());
		Logger.info("*****************************************************");

		softAssert.assertEquals(countAfterUI, countAfterExpectedUI,"Content type count is not getting decremented for file:"+fileName+ " via UI");
		softAssert.assertEquals(countAfterAPI, countAfterExpectedAPI,"Content type count is not getting decremented for file:"+fileName+ " via API");
		softAssert.assertAll();

		Map<String, Integer> numberList=new HashMap<String, Integer>();
		numberList.put("ExpAPICount", countAfterExpectedAPI);numberList.put("ActAPICount", countAfterAPI);
		numberList.put("ExpUICount", countAfterExpectedUI);numberList.put("ActUICount", countAfterUI);
		//Assert.assertEquals(dciFunctions.numberComparison(numberList), "");
		
		Logger.info(
				"************************************ Completed the checking of decrease in filter count"
						+ " for filename:" + fileName +" with content types:"+contentTypes+" and saas app type:" + saasType + " ******************");
	}


	@Priority(17)
	@Test(dataProvider = "dataUploadRiskTypeCIQUnexposed", groups ={"Unexposed"})
	public void testFilterCountCheckForUploadingOfUnexposedCIQRiskTypeFile(TestParameters testParams) throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();

		String riskType=testParams.getRiskType(); 
		List<String> risks=testParams.getRisks();
		String fileName=testParams.getFileName();
		int countBeforeUI= testParams.getCountUI(); 
		int countBeforeAPI= testParams.getCountAPI(); 
		int countAfterExpectedUI=countBeforeUI+dciFunctions.getExpectedCount(fileName);	
		int countAfterExpectedAPI=countBeforeAPI+dciFunctions.getExpectedCount(fileName);		
		String saasType = SaasType.getSaasFilterType(suiteData.getSaasApp());
		String appType = SaasType.getAppType(suiteData.getSaasApp(), suiteData.getEnvironmentName());
		Map<String, String>  additionalParamsAPI = testParams.getHeadersAPI();
		Map<String, String>  additionalParamsUI =  testParams.getHeadersUI();

		Logger.info(
				"************************************ Starting the checking of increase in filter count"
						+ " for filename:" + fileName +" with ContentIQ risk types:"+risks+" and saas app type:" + saasType + " ******************");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description: Check the filter count increase after uploading of the ContentIQ risk file");
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output****************");
		Logger.info("Filename:"+fileName);Logger.info("Risk Types to be validated:"+risks);
		Logger.info("Current Filter Count from UI:"+ countBeforeUI);Logger.info("Expected Filter Count from UI:"+ (countAfterExpectedUI));
		Logger.info("Current Filter Count from API:"+ countBeforeAPI);Logger.info("Expected Filter Count from API:"+ (countAfterExpectedAPI));
		Logger.info("Saas App Type:"+suiteData.getSaasApp());
		Logger.info("*****************************************************");

		List<NameValuePair> headers = dciFunctions.getBasicHeaders(suiteData);	
		verifyRiskTypesDisplayLogs(dciFunctions, fileName, saasType, headers);

		Vulnerability vulnerability = dciFunctions.getVulnerability(suiteData, restClient, additionalParamsUI);
		Logger.info("Vulnerability:" + riskType + "::Count:" + vulnerability.getResults().getCIQCount(riskType));
		VulnerabilityTypes vulnerabilityTypes = dciFunctions.getVulnerabilityTypes(suiteData, restClient, appType, additionalParamsAPI);
		Logger.info("Vulnerability:" + riskType + "::Count:" + vulnerabilityTypes.getObjects().getCIQCount(riskType));
		int countAfterUI= vulnerability.getResults().getCIQCount(riskType);
		int countAfterAPI= vulnerabilityTypes.getObjects().getCIQCount(riskType);

		Logger.info("****************Actual Output****************");
		Logger.info("Expected Filter Count from UI:"+ (countBeforeUI+1));Logger.info("Actual Filter Count from UI:"+ (countAfterUI));
		Logger.info("Expected Filter Count from API:"+ (countBeforeAPI+1));Logger.info("Actual Filter Count from API:"+ (countAfterAPI));
		Logger.info("Saas App Type:"+suiteData.getSaasApp());
		Logger.info("*****************************************************");

		softAssert.assertEquals(countAfterUI, countAfterExpectedUI,"Risk count is not getting incremented for file:"+fileName+ " via UI");
		softAssert.assertEquals(countAfterAPI, countAfterExpectedAPI,"Risk count is not getting incremented for file:"+fileName+ " via API");
		softAssert.assertAll();

		Map<String, Integer> numberList=new HashMap<String, Integer>();
		numberList.put("ExpAPICount", countAfterExpectedAPI);numberList.put("ActAPICount", countAfterAPI);
		numberList.put("ExpUICount", countAfterExpectedUI);numberList.put("ActUICount", countAfterUI);
		//Assert.assertEquals(dciFunctions.numberComparison(numberList), "");
		
		Logger.info(
				"************************************ Completed the checking of increase in filter count"
						+ " for filename:" + fileName +" with ContentIQ risk types:"+risks+" and saas app type:" + saasType + " ******************");
	}

	@Priority(18)
	@Test(dataProvider = "dataDeleteRiskTypeCIQUnexposed", groups ={"Unexposed"})
	public void testFilterCountCheckForDeletingOfUnexposedCIQRiskTypeFile(TestParameters testParams) throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();

		try{
			String riskType=testParams.getRiskType(); 
			List<String> risks=testParams.getRisks();
			String fileName=testParams.getFileName();
			int countBeforeUI= testParams.getCountUI();
			int countBeforeAPI= testParams.getCountAPI();
			int countAfterExpectedUI=countBeforeUI-dciFunctions.getExpectedCount(fileName);
			int countAfterExpectedAPI=countBeforeAPI-dciFunctions.getExpectedCount(fileName);		
			String saasType = SaasType.getSaasFilterType(suiteData.getSaasApp());
			String appType = SaasType.getAppType(suiteData.getSaasApp(), suiteData.getEnvironmentName());
			Map<String, String>  additionalParamsAPI = testParams.getHeadersAPI();
			Map<String, String>  additionalParamsUI =  testParams.getHeadersUI();

			Logger.info(
					"************************************ Starting the checking of decrease in filter count"
							+ " for filename:" + fileName +" with ContentIQ risk types:"+risks+" and saas app type:" + saasType + " ******************");

			Logger.info("****************Test Case Description****************");
			Logger.info("Test Case Description: Check the filter count increase after deleting of the ContentIQ risk file");
			Logger.info("*****************************************************");
			Logger.info("****************Expected Output****************");
			Logger.info("Filename:"+fileName);Logger.info("Risk Types to be validated:"+risks);
			Logger.info("Current Filter Count from UI:"+ countBeforeUI);Logger.info("Expected Filter Count from UI:"+ (countAfterExpectedUI));
			Logger.info("Current Filter Count from API:"+ countBeforeAPI);Logger.info("Expected Filter Count from API:"+ (countAfterExpectedAPI));
			Logger.info("Saas App Type:"+suiteData.getSaasApp());
			Logger.info("*****************************************************");

			Vulnerability vulnerability = dciFunctions.getVulnerability(suiteData, restClient, additionalParamsUI);
			Logger.info("Vulnerability:" + riskType + "::Count:" + vulnerability.getResults().getCIQCount(riskType));
			VulnerabilityTypes vulnerabilityTypes = dciFunctions.getVulnerabilityTypes(suiteData, restClient, appType, additionalParamsAPI);
			Logger.info("Vulnerability:" + riskType + "::Count:" + vulnerabilityTypes.getObjects().getCIQCount(riskType));
			int countAfterUI= vulnerability.getResults().getCIQCount(riskType);
			int countAfterAPI= vulnerabilityTypes.getObjects().getCIQCount(riskType);

			Logger.info("****************Actual Output****************");
			Logger.info("Expected Filter Count from UI:"+ (countAfterExpectedUI));Logger.info("Actual Filter Count from UI:"+ (countAfterUI));
			Logger.info("Expected Filter Count from API:"+ (countAfterExpectedAPI));Logger.info("Actual Filter Count from API:"+ (countAfterAPI));
			Logger.info("Saas App Type:"+suiteData.getSaasApp());
			Logger.info("*****************************************************");

			softAssert.assertEquals(countAfterUI, countAfterExpectedUI,"Risk count is not getting decremented for file:"+fileName+ " via UI");
			softAssert.assertEquals(countAfterAPI, countAfterExpectedAPI,"Risk count is not getting decremented for file:"+fileName+ " via API");
			softAssert.assertAll();

			Map<String, Integer> numberList=new HashMap<String, Integer>();
			numberList.put("ExpAPICount", countAfterExpectedAPI);numberList.put("ActAPICount", countAfterAPI);
			numberList.put("ExpUICount", countAfterExpectedUI);numberList.put("ActUICount", countAfterUI);
			//Assert.assertEquals(dciFunctions.numberComparison(numberList), "");
			
			Logger.info(
					"************************************ Completed the checking of decrease in filter count"
							+ " for filename:" + fileName +" with ContentIQ risk types:"+risks+" and saas app type:" + saasType + " ******************");
		}finally{
			dciFunctions.deleteAllCIQProfiles(restClient, suiteData);
		}
	}

	@Priority(19)
	@Test(dataProvider = "dataUploadRiskTypeCIQExposedPublic", groups ={"ExposedPublic"})
	public void testFilterCountCheckForUploadingOfExposedPublicCIQRiskTypeFile(TestParameters testParams) throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();

		String riskType=testParams.getRiskType(); 
		List<String> risks=testParams.getRisks();
		String fileName=testParams.getFileName();
		int countBeforeUI= testParams.getCountUI(); 
		int countBeforeAPI= testParams.getCountAPI(); 
		int countAfterExpectedUI=countBeforeUI+dciFunctions.getExpectedCount(fileName);	
		int countAfterExpectedAPI=countBeforeAPI+dciFunctions.getExpectedCount(fileName);		
		String saasType = SaasType.getSaasFilterType(suiteData.getSaasApp());
		String appType = SaasType.getAppType(suiteData.getSaasApp(), suiteData.getEnvironmentName());
		Map<String, String>  additionalParamsAPI = testParams.getHeadersAPI();
		Map<String, String>  additionalParamsUI =  testParams.getHeadersUI();

		Logger.info(
				"************************************ Starting the checking of increase in filter count"
						+ " for filename:" + fileName +" with ContentIQ risk types:"+risks+" and saas app type:" + saasType + " ******************");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description: Check the filter count increase after uploading of the ContentIQ risk file");
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output****************");
		Logger.info("Filename:"+fileName);Logger.info("Risk Types to be validated:"+risks);
		Logger.info("Current Filter Count from UI:"+ countBeforeUI);Logger.info("Expected Filter Count from UI:"+ (countAfterExpectedUI));
		Logger.info("Current Filter Count from API:"+ countBeforeAPI);Logger.info("Expected Filter Count from API:"+ (countAfterExpectedAPI));
		Logger.info("Saas App Type:"+suiteData.getSaasApp());
		Logger.info("*****************************************************");

		List<NameValuePair> headers = dciFunctions.getBasicHeaders(suiteData);	
		verifyRiskTypesDisplayLogs(dciFunctions, fileName, saasType, headers);

		Vulnerability vulnerability = dciFunctions.getVulnerability(suiteData, restClient, additionalParamsUI);
		Logger.info("Vulnerability:" + riskType + "::Count:" + vulnerability.getResults().getCIQCount(riskType));
		VulnerabilityTypes vulnerabilityTypes = dciFunctions.getVulnerabilityTypes(suiteData, restClient, appType, additionalParamsAPI);
		Logger.info("Vulnerability:" + riskType + "::Count:" + vulnerabilityTypes.getObjects().getCIQCount(riskType));
		int countAfterUI= vulnerability.getResults().getCIQCount(riskType);
		int countAfterAPI= vulnerabilityTypes.getObjects().getCIQCount(riskType);

		Logger.info("****************Actual Output****************");
		Logger.info("Expected Filter Count from UI:"+ (countBeforeUI+1));Logger.info("Actual Filter Count from UI:"+ (countAfterUI));
		Logger.info("Expected Filter Count from API:"+ (countBeforeAPI+1));Logger.info("Actual Filter Count from API:"+ (countAfterAPI));
		Logger.info("Saas App Type:"+suiteData.getSaasApp());
		Logger.info("*****************************************************");

		softAssert.assertEquals(countAfterUI, countAfterExpectedUI,"Risk count is not getting incremented for file:"+fileName+ " via UI");
		softAssert.assertEquals(countAfterAPI, countAfterExpectedAPI,"Risk count is not getting incremented for file:"+fileName+ " via API");
		softAssert.assertAll();

		Map<String, Integer> numberList=new HashMap<String, Integer>();
		numberList.put("ExpAPICount", countAfterExpectedAPI);numberList.put("ActAPICount", countAfterAPI);
		numberList.put("ExpUICount", countAfterExpectedUI);numberList.put("ActUICount", countAfterUI);
		//Assert.assertEquals(dciFunctions.numberComparison(numberList), "");
		
		Logger.info(
				"************************************ Completed the checking of increase in filter count"
						+ " for filename:" + fileName +" with ContentIQ risk types:"+risks+" and saas app type:" + saasType + " ******************");
	}

	@Priority(20)
	@Test(dataProvider = "dataDeleteRiskTypeCIQExposedPublic", groups ={"ExposedPublic"})
	public void testFilterCountCheckForDeletionOfExposedPublicCIQRiskTypeFile(TestParameters testParams) throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();

		try{
			String riskType=testParams.getRiskType(); 
			List<String> risks=testParams.getRisks();
			String fileName=testParams.getFileName();
			int countBeforeUI= testParams.getCountUI();
			int countBeforeAPI= testParams.getCountAPI();
			int countAfterExpectedUI=countBeforeUI-dciFunctions.getExpectedCount(fileName);
			int countAfterExpectedAPI=countBeforeAPI-dciFunctions.getExpectedCount(fileName);		
			String saasType = SaasType.getSaasFilterType(suiteData.getSaasApp());
			String appType = SaasType.getAppType(suiteData.getSaasApp(), suiteData.getEnvironmentName());
			Map<String, String>  additionalParamsAPI = testParams.getHeadersAPI();
			Map<String, String>  additionalParamsUI =  testParams.getHeadersUI();

			Logger.info(
					"************************************ Starting the checking of decrease in filter count"
							+ " for filename:" + fileName +" with ContentIQ risk types:"+risks+" and saas app type:" + saasType + " ******************");

			Logger.info("****************Test Case Description****************");
			Logger.info("Test Case Description: Check the filter count increase after deleting of the ContentIQ risk file");
			Logger.info("*****************************************************");
			Logger.info("****************Expected Output****************");
			Logger.info("Filename:"+fileName);Logger.info("Risk Types to be validated:"+risks);
			Logger.info("Current Filter Count from UI:"+ countBeforeUI);Logger.info("Expected Filter Count from UI:"+ (countAfterExpectedUI));
			Logger.info("Current Filter Count from API:"+ countBeforeAPI);Logger.info("Expected Filter Count from API:"+ (countAfterExpectedAPI));
			Logger.info("Saas App Type:"+suiteData.getSaasApp());
			Logger.info("*****************************************************");

			Vulnerability vulnerability = dciFunctions.getVulnerability(suiteData, restClient, additionalParamsUI);
			Logger.info("Vulnerability:" + riskType + "::Count:" + vulnerability.getResults().getCIQCount(riskType));
			VulnerabilityTypes vulnerabilityTypes = dciFunctions.getVulnerabilityTypes(suiteData, restClient, appType, additionalParamsAPI);
			Logger.info("Vulnerability:" + riskType + "::Count:" + vulnerabilityTypes.getObjects().getCIQCount(riskType));
			int countAfterUI= vulnerability.getResults().getCIQCount(riskType);
			int countAfterAPI= vulnerabilityTypes.getObjects().getCIQCount(riskType);

			Logger.info("****************Actual Output****************");
			Logger.info("Expected Filter Count from UI:"+ (countAfterExpectedUI));Logger.info("Actual Filter Count from UI:"+ (countAfterUI));
			Logger.info("Expected Filter Count from API:"+ (countAfterExpectedAPI));Logger.info("Actual Filter Count from API:"+ (countAfterAPI));
			Logger.info("Saas App Type:"+suiteData.getSaasApp());
			Logger.info("*****************************************************");

			softAssert.assertEquals(countAfterUI, countAfterExpectedUI,"Risk count is not getting decremented for file:"+fileName+ " via UI");
			softAssert.assertEquals(countAfterAPI, countAfterExpectedAPI,"Risk count is not getting decremented for file:"+fileName+ " via API");
			softAssert.assertAll();

			Map<String, Integer> numberList=new HashMap<String, Integer>();
			numberList.put("ExpAPICount", countAfterExpectedAPI);numberList.put("ActAPICount", countAfterAPI);
			numberList.put("ExpUICount", countAfterExpectedUI);numberList.put("ActUICount", countAfterUI);
			//Assert.assertEquals(dciFunctions.numberComparison(numberList), "");
			
			Logger.info(
					"************************************ Completed the checking of decrease in filter count"
							+ " for filename:" + fileName +" with ContentIQ risk types:"+risks+" and saas app type:" + saasType + " ******************");
		}finally{
			dciFunctions.deleteAllCIQProfiles(restClient, suiteData);
		}
	}

	@Priority(21)
	@Test(dataProvider = "dataUploadRiskTypeCIQExposedInternal", groups ={"ExposedInternal"})
	public void testFilterCountCheckForUploadingOfExposedInternalCIQRiskTypeFile(TestParameters testParams) throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();

		String riskType=testParams.getRiskType(); 
		List<String> risks=testParams.getRisks();
		String fileName=testParams.getFileName();
		int countBeforeUI= testParams.getCountUI(); 
		int countBeforeAPI= testParams.getCountAPI(); 
		int countAfterExpectedUI=countBeforeUI+dciFunctions.getExpectedCount(fileName);	
		int countAfterExpectedAPI=countBeforeAPI+dciFunctions.getExpectedCount(fileName);		
		String saasType = SaasType.getSaasFilterType(suiteData.getSaasApp());
		String appType = SaasType.getAppType(suiteData.getSaasApp(), suiteData.getEnvironmentName());
		Map<String, String>  additionalParamsAPI = testParams.getHeadersAPI();
		Map<String, String>  additionalParamsUI =  testParams.getHeadersUI();

		Logger.info(
				"************************************ Starting the checking of increase in filter count"
						+ " for filename:" + fileName +" with ContentIQ risk types:"+risks+" and saas app type:" + saasType + " ******************");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description: Check the filter count increase after uploading of the ContentIQ risk file");
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output****************");
		Logger.info("Filename:"+fileName);Logger.info("Risk Types to be validated:"+risks);
		Logger.info("Current Filter Count from UI:"+ countBeforeUI);Logger.info("Expected Filter Count from UI:"+ (countAfterExpectedUI));
		Logger.info("Current Filter Count from API:"+ countBeforeAPI);Logger.info("Expected Filter Count from API:"+ (countAfterExpectedAPI));
		Logger.info("Saas App Type:"+suiteData.getSaasApp());
		Logger.info("*****************************************************");

		List<NameValuePair> headers = dciFunctions.getBasicHeaders(suiteData);	
		verifyRiskTypesDisplayLogs(dciFunctions, fileName, saasType, headers);

		Vulnerability vulnerability = dciFunctions.getVulnerability(suiteData, restClient, additionalParamsUI);
		Logger.info("Vulnerability:" + riskType + "::Count:" + vulnerability.getResults().getCIQCount(riskType));
		VulnerabilityTypes vulnerabilityTypes = dciFunctions.getVulnerabilityTypes(suiteData, restClient, appType, additionalParamsAPI);
		Logger.info("Vulnerability:" + riskType + "::Count:" + vulnerabilityTypes.getObjects().getCIQCount(riskType));
		int countAfterUI= vulnerability.getResults().getCIQCount(riskType);
		int countAfterAPI= vulnerabilityTypes.getObjects().getCIQCount(riskType);

		Logger.info("****************Actual Output****************");
		Logger.info("Expected Filter Count from UI:"+ (countBeforeUI+1));Logger.info("Actual Filter Count from UI:"+ (countAfterUI));
		Logger.info("Expected Filter Count from API:"+ (countBeforeAPI+1));Logger.info("Actual Filter Count from API:"+ (countAfterAPI));
		Logger.info("Saas App Type:"+suiteData.getSaasApp());
		Logger.info("*****************************************************");

		softAssert.assertEquals(countAfterUI, countAfterExpectedUI,"Risk count is not getting incremented for file:"+fileName+ " via UI");
		softAssert.assertEquals(countAfterAPI, countAfterExpectedAPI,"Risk count is not getting incremented for file:"+fileName+ " via API");
		softAssert.assertAll();

		Map<String, Integer> numberList=new HashMap<String, Integer>();
		numberList.put("ExpAPICount", countAfterExpectedAPI);numberList.put("ActAPICount", countAfterAPI);
		numberList.put("ExpUICount", countAfterExpectedUI);numberList.put("ActUICount", countAfterUI);
		//Assert.assertEquals(dciFunctions.numberComparison(numberList), "");
		
		Logger.info(
				"************************************ Completed the checking of increase in filter count"
						+ " for filename:" + fileName +" with ContentIQ risk types:"+risks+" and saas app type:" + saasType + " ******************");
	}

	@Priority(22)
	@Test(dataProvider = "dataDeleteRiskTypeCIQExposedInternal", groups ={"ExposedInternal"})
	public void testFilterCountCheckForDeletionOfExposedInternalCIQRiskTypeFile(TestParameters testParams) throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();

		try{
			String riskType=testParams.getRiskType(); 
			List<String> risks=testParams.getRisks();
			String fileName=testParams.getFileName();
			int countBeforeUI= testParams.getCountUI();
			int countBeforeAPI= testParams.getCountAPI();
			int countAfterExpectedUI=countBeforeUI-dciFunctions.getExpectedCount(fileName);
			int countAfterExpectedAPI=countBeforeAPI-dciFunctions.getExpectedCount(fileName);	
			String saasType = SaasType.getSaasFilterType(suiteData.getSaasApp());
			String appType = SaasType.getAppType(suiteData.getSaasApp(), suiteData.getEnvironmentName());
			Map<String, String>  additionalParamsAPI = testParams.getHeadersAPI();
			Map<String, String>  additionalParamsUI =  testParams.getHeadersUI();

			Logger.info(
					"************************************ Starting the checking of decrease in filter count"
							+ " for filename:" + fileName +" with ContentIQ risk types:"+risks+" and saas app type:" + saasType + " ******************");

			Logger.info("****************Test Case Description****************");
			Logger.info("Test Case Description: Check the filter count increase after deleting of the ContentIQ risk file");
			Logger.info("*****************************************************");
			Logger.info("****************Expected Output****************");
			Logger.info("Filename:"+fileName);Logger.info("Risk Types to be validated:"+risks);
			Logger.info("Current Filter Count from UI:"+ countBeforeUI);Logger.info("Expected Filter Count from UI:"+ (countAfterExpectedUI));
			Logger.info("Current Filter Count from API:"+ countBeforeAPI);Logger.info("Expected Filter Count from API:"+ (countAfterExpectedAPI));
			Logger.info("Saas App Type:"+suiteData.getSaasApp());
			Logger.info("*****************************************************");

			Vulnerability vulnerability = dciFunctions.getVulnerability(suiteData, restClient, additionalParamsUI);
			Logger.info("Vulnerability:" + riskType + "::Count:" + vulnerability.getResults().getCIQCount(riskType));
			VulnerabilityTypes vulnerabilityTypes = dciFunctions.getVulnerabilityTypes(suiteData, restClient, appType, additionalParamsAPI);
			Logger.info("Vulnerability:" + riskType + "::Count:" + vulnerabilityTypes.getObjects().getCIQCount(riskType));
			int countAfterUI= vulnerability.getResults().getCIQCount(riskType);
			int countAfterAPI= vulnerabilityTypes.getObjects().getCIQCount(riskType);

			Logger.info("****************Actual Output****************");
			Logger.info("Expected Filter Count from UI:"+ (countAfterExpectedUI));Logger.info("Actual Filter Count from UI:"+ (countAfterUI));
			Logger.info("Expected Filter Count from API:"+ (countAfterExpectedAPI));Logger.info("Actual Filter Count from API:"+ (countAfterAPI));
			Logger.info("Saas App Type:"+suiteData.getSaasApp());
			Logger.info("*****************************************************");

			softAssert.assertEquals(countAfterUI, countAfterExpectedUI,"Risk count is not getting decremented for file:"+fileName+ " via UI");
			softAssert.assertEquals(countAfterAPI, countAfterExpectedAPI,"Risk count is not getting decremented for file:"+fileName+ " via API");
			softAssert.assertAll();

			Map<String, Integer> numberList=new HashMap<String, Integer>();
			numberList.put("ExpAPICount", countAfterExpectedAPI);numberList.put("ActAPICount", countAfterAPI);
			numberList.put("ExpUICount", countAfterExpectedUI);numberList.put("ActUICount", countAfterUI);
			//Assert.assertEquals(dciFunctions.numberComparison(numberList), "");
			
			Logger.info(
					"************************************ Completed the checking of decrease in filter count"
							+ " for filename:" + fileName +" with ContentIQ risk types:"+risks+" and saas app type:" + saasType + " ******************");
		}finally{
			dciFunctions.deleteAllCIQProfiles(restClient, suiteData);
		}
	}

	@Priority(23)
	@Test(dataProvider = "dataUploadRiskTypeCIQExposedExternal", groups ={"ExposedExternal"})
	public void testFilterCountCheckForUploadingOfExposedExternalCIQRiskTypeFile(TestParameters testParams) throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();

		String riskType=testParams.getRiskType(); 
		List<String> risks=testParams.getRisks();
		String fileName=testParams.getFileName();
		int countBeforeUI= testParams.getCountUI(); 
		int countBeforeAPI= testParams.getCountAPI(); 
		int countAfterExpectedUI=countBeforeUI+dciFunctions.getExpectedCount(fileName);	
		int countAfterExpectedAPI=countBeforeAPI+dciFunctions.getExpectedCount(fileName);		
		String saasType = SaasType.getSaasFilterType(suiteData.getSaasApp());
		String appType = SaasType.getAppType(suiteData.getSaasApp(), suiteData.getEnvironmentName());
		Map<String, String>  additionalParamsAPI = testParams.getHeadersAPI();
		Map<String, String>  additionalParamsUI =  testParams.getHeadersUI();

		Logger.info(
				"************************************ Starting the checking of increase in filter count"
						+ " for filename:" + fileName +" with ContentIQ risk types:"+risks+" and saas app type:" + saasType + " ******************");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description: Check the filter count increase after uploading of the ContentIQ risk file");
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output****************");
		Logger.info("Filename:"+fileName);Logger.info("Risk Types to be validated:"+risks);
		Logger.info("Current Filter Count from UI:"+ countBeforeUI);Logger.info("Expected Filter Count from UI:"+ (countAfterExpectedUI));
		Logger.info("Current Filter Count from API:"+ countBeforeAPI);Logger.info("Expected Filter Count from API:"+ (countAfterExpectedAPI));
		Logger.info("Saas App Type:"+suiteData.getSaasApp());
		Logger.info("*****************************************************");

		List<NameValuePair> headers = dciFunctions.getBasicHeaders(suiteData);	
		verifyRiskTypesDisplayLogs(dciFunctions, fileName, saasType, headers);

		Vulnerability vulnerability = dciFunctions.getVulnerability(suiteData, restClient, additionalParamsUI);
		Logger.info("Vulnerability:" + riskType + "::Count:" + vulnerability.getResults().getCIQCount(riskType));
		VulnerabilityTypes vulnerabilityTypes = dciFunctions.getVulnerabilityTypes(suiteData, restClient, appType, additionalParamsAPI);
		Logger.info("Vulnerability:" + riskType + "::Count:" + vulnerabilityTypes.getObjects().getCIQCount(riskType));
		int countAfterUI= vulnerability.getResults().getCIQCount(riskType);
		int countAfterAPI= vulnerabilityTypes.getObjects().getCIQCount(riskType);

		Logger.info("****************Actual Output****************");
		Logger.info("Expected Filter Count from UI:"+ (countBeforeUI+1));Logger.info("Actual Filter Count from UI:"+ (countAfterUI));
		Logger.info("Expected Filter Count from API:"+ (countBeforeAPI+1));Logger.info("Actual Filter Count from API:"+ (countAfterAPI));
		Logger.info("Saas App Type:"+suiteData.getSaasApp());
		Logger.info("*****************************************************");

		softAssert.assertEquals(countAfterUI, countAfterExpectedUI,"Risk count is not getting incremented for file:"+fileName+ " via UI");
		softAssert.assertEquals(countAfterAPI, countAfterExpectedAPI,"Risk count is not getting incremented for file:"+fileName+ " via API");
		softAssert.assertAll();

		Map<String, Integer> numberList=new HashMap<String, Integer>();
		numberList.put("ExpAPICount", countAfterExpectedAPI);numberList.put("ActAPICount", countAfterAPI);
		numberList.put("ExpUICount", countAfterExpectedUI);numberList.put("ActUICount", countAfterUI);
		//Assert.assertEquals(dciFunctions.numberComparison(numberList), "");
		
		Logger.info(
				"************************************ Completed the checking of increase in filter count"
						+ " for filename:" + fileName +" with ContentIQ risk types:"+risks+" and saas app type:" + saasType + " ******************");
	}

	@Priority(24)
	@Test(dataProvider = "dataDeleteRiskTypeCIQExposedExternal", groups ={"ExposedExternal"})
	public void testFilterCountCheckForDeletionOfExposedExternalCIQRiskTypeFile(TestParameters testParams) throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();

		try{
			String riskType=testParams.getRiskType(); 
			List<String> risks=testParams.getRisks();
			String fileName=testParams.getFileName();
			int countBeforeUI= testParams.getCountUI();
			int countBeforeAPI= testParams.getCountAPI();
			int countAfterExpectedUI=countBeforeUI-dciFunctions.getExpectedCount(fileName);	
			int countAfterExpectedAPI=countBeforeAPI-dciFunctions.getExpectedCount(fileName);		
			String saasType = SaasType.getSaasFilterType(suiteData.getSaasApp());
			String appType = SaasType.getAppType(suiteData.getSaasApp(), suiteData.getEnvironmentName());
			Map<String, String>  additionalParamsAPI = testParams.getHeadersAPI();
			Map<String, String>  additionalParamsUI =  testParams.getHeadersUI();

			Logger.info(
					"************************************ Starting the checking of decrease in filter count"
							+ " for filename:" + fileName +" with ContentIQ risk types:"+risks+" and saas app type:" + saasType + " ******************");

			Logger.info("****************Test Case Description****************");
			Logger.info("Test Case Description: Check the filter count increase after deleting of the ContentIQ risk file");
			Logger.info("*****************************************************");
			Logger.info("****************Expected Output****************");
			Logger.info("Filename:"+fileName);Logger.info("Risk Types to be validated:"+risks);
			Logger.info("Current Filter Count from UI:"+ countBeforeUI);Logger.info("Expected Filter Count from UI:"+ (countAfterExpectedUI));
			Logger.info("Current Filter Count from API:"+ countBeforeAPI);Logger.info("Expected Filter Count from API:"+ (countAfterExpectedAPI));
			Logger.info("Saas App Type:"+suiteData.getSaasApp());
			Logger.info("*****************************************************");

			Vulnerability vulnerability = dciFunctions.getVulnerability(suiteData, restClient, additionalParamsUI);
			Logger.info("Vulnerability:" + riskType + "::Count:" + vulnerability.getResults().getCIQCount(riskType));
			VulnerabilityTypes vulnerabilityTypes = dciFunctions.getVulnerabilityTypes(suiteData, restClient, appType, additionalParamsAPI);
			Logger.info("Vulnerability:" + riskType + "::Count:" + vulnerabilityTypes.getObjects().getCIQCount(riskType));
			int countAfterUI= vulnerability.getResults().getCIQCount(riskType);
			int countAfterAPI= vulnerabilityTypes.getObjects().getCIQCount(riskType);

			Logger.info("****************Actual Output****************");
			Logger.info("Expected Filter Count from UI:"+ (countAfterExpectedUI));Logger.info("Actual Filter Count from UI:"+ (countAfterUI));
			Logger.info("Expected Filter Count from API:"+ (countAfterExpectedAPI));Logger.info("Actual Filter Count from API:"+ (countAfterAPI));
			Logger.info("Saas App Type:"+suiteData.getSaasApp());
			Logger.info("*****************************************************");

			softAssert.assertEquals(countAfterUI, countAfterExpectedUI,"Risk count is not getting decremented for file:"+fileName+ " via UI");
			softAssert.assertEquals(countAfterAPI, countAfterExpectedAPI,"Risk count is not getting decremented for file:"+fileName+ " via API");
			softAssert.assertAll();

			Map<String, Integer> numberList=new HashMap<String, Integer>();
			numberList.put("ExpAPICount", countAfterExpectedAPI);numberList.put("ActAPICount", countAfterAPI);
			numberList.put("ExpUICount", countAfterExpectedUI);numberList.put("ActUICount", countAfterUI);
			//Assert.assertEquals(dciFunctions.numberComparison(numberList), "");
			
			Logger.info(
					"************************************ Completed the checking of decrease in filter count"
							+ " for filename:" + fileName +" with ContentIQ risk types:"+risks+" and saas app type:" + saasType + " ******************");
		}finally{
			dciFunctions.deleteAllCIQProfiles(restClient, suiteData);
		}
	}
	
	@Priority(25)
	@Test(dataProvider = "dataUploadFileClassTypeUnexposed", groups ={"Unexposed"})
	public void testFilterCountCheckForUploadingOfUnexposedFileClassTypeFile(TestParameters testParams) throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();

		String fileType=testParams.getFileType(); 
		List<String> risks=testParams.getRisks();
		String fileName=testParams.getFileName();
		int countBeforeUI= testParams.getCountUI(); 
		int countAfterExpectedUI=countBeforeUI+dciFunctions.getExpectedCount(fileName);	
		String saasType = SaasType.getSaasFilterType(suiteData.getSaasApp());
		Map<String, String>  additionalParamsUI =  testParams.getHeadersUI();

		Logger.info(
				"************************************ Starting the checking of increase in filter count"
						+ " for filename:" + fileName +" with ContentIQ risk types:"+risks+" and saas app type:" + saasType + " ******************");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description: Check the filter count increase after uploading of the ContentIQ risk file");
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output****************");
		Logger.info("Filename:"+fileName);Logger.info("Risk Types to be validated:"+risks);
		Logger.info("Current Filter Count from UI:"+ countBeforeUI);Logger.info("Expected Filter Count from UI:"+ (countAfterExpectedUI));
		Logger.info("Saas App Type:"+suiteData.getSaasApp());
		Logger.info("*****************************************************");

		List<NameValuePair> headers = dciFunctions.getBasicHeaders(suiteData);	
		verifyRiskTypesDisplayLogs(dciFunctions, fileName, saasType, headers);

		FileType fileClass = dciFunctions.getFileType(suiteData, restClient, additionalParamsUI);
		Logger.info("FileType:" + fileType + "::Count:" + fileClass.getFileTypeCount(fileType));
		
		int countAfterUI= fileClass.getFileTypeCount(fileType);

		Logger.info("****************Actual Output****************");
		Logger.info("Expected Filter Count from UI:"+ (countBeforeUI+1));Logger.info("Actual Filter Count from UI:"+ (countAfterUI));
		Logger.info("Saas App Type:"+suiteData.getSaasApp());
		Logger.info("*****************************************************");

		softAssert.assertEquals(countAfterUI, countAfterExpectedUI,"Risk count is not getting incremented for file:"+fileName+ " via UI");
		softAssert.assertAll();
		
		Logger.info(
				"************************************ Completed the checking of increase in filter count"
						+ " for filename:" + fileName +" with ContentIQ risk types:"+risks+" and saas app type:" + saasType + " ******************");
	}

	@Priority(26)
	@Test(dataProvider = "dataDeleteFileClassTypeUnexposed", groups ={"Unexposed"})
	public void testFilterCountCheckForDeletingOfUnexposedFileClassTypeFile(TestParameters testParams) throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();

		try{
			String fileType=testParams.getFileType();
			List<String> risks=testParams.getRisks();
			String fileName=testParams.getFileName();
			int countBeforeUI= testParams.getCountUI();
			int countAfterExpectedUI=countBeforeUI-dciFunctions.getExpectedCount(fileName);
			String saasType = SaasType.getSaasFilterType(suiteData.getSaasApp());
			Map<String, String>  additionalParamsUI =  testParams.getHeadersUI();

			Logger.info(
					"************************************ Starting the checking of decrease in filter count"
							+ " for filename:" + fileName +" with ContentIQ risk types:"+risks+" and saas app type:" + saasType + " ******************");

			Logger.info("****************Test Case Description****************");
			Logger.info("Test Case Description: Check the filter count increase after deleting of the ContentIQ risk file");
			Logger.info("*****************************************************");
			Logger.info("****************Expected Output****************");
			Logger.info("Filename:"+fileName);Logger.info("Risk Types to be validated:"+risks);
			Logger.info("Current Filter Count from UI:"+ countBeforeUI);Logger.info("Expected Filter Count from UI:"+ (countAfterExpectedUI));
			Logger.info("Saas App Type:"+suiteData.getSaasApp());
			Logger.info("*****************************************************");

			FileType fileClass = dciFunctions.getFileType(suiteData, restClient, additionalParamsUI);
			Logger.info("FileType:" + fileType + "::Count:" + fileClass.getFileTypeCount(fileType));
			int countAfterUI= fileClass.getFileTypeCount(fileType);
			
			Logger.info("****************Actual Output****************");
			Logger.info("Expected Filter Count from UI:"+ (countAfterExpectedUI));Logger.info("Actual Filter Count from UI:"+ (countAfterUI));
			Logger.info("Saas App Type:"+suiteData.getSaasApp());
			Logger.info("*****************************************************");

			softAssert.assertEquals(countAfterUI, countAfterExpectedUI,"Risk count is not getting decremented for file:"+fileName+ " via UI");
			softAssert.assertAll();

			Logger.info(
					"************************************ Completed the checking of decrease in filter count"
							+ " for filename:" + fileName +" with ContentIQ risk types:"+risks+" and saas app type:" + saasType + " ******************");
		}finally{
			dciFunctions.deleteAllCIQProfiles(restClient, suiteData);
		}
	}

	@Priority(27)
	@Test(dataProvider = "dataUploadFileClassTypeExposedPublic", groups ={"ExposedPublic"})
	public void testFilterCountCheckForUploadingOfExposedPublicFileClassTypeFile(TestParameters testParams) throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();

		String fileType=testParams.getFileType();
		List<String> risks=testParams.getRisks();
		String fileName=testParams.getFileName();
		int countBeforeUI= testParams.getCountUI(); 
		int countAfterExpectedUI=countBeforeUI+dciFunctions.getExpectedCount(fileName);	
		String saasType = SaasType.getSaasFilterType(suiteData.getSaasApp());
		Map<String, String>  additionalParamsUI =  testParams.getHeadersUI();

		Logger.info(
				"************************************ Starting the checking of increase in filter count"
						+ " for filename:" + fileName +" with ContentIQ risk types:"+risks+" and saas app type:" + saasType + " ******************");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description: Check the filter count increase after uploading of the ContentIQ risk file");
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output****************");
		Logger.info("Filename:"+fileName);Logger.info("Risk Types to be validated:"+risks);
		Logger.info("Current Filter Count from UI:"+ countBeforeUI);Logger.info("Expected Filter Count from UI:"+ (countAfterExpectedUI));
		Logger.info("Saas App Type:"+suiteData.getSaasApp());
		Logger.info("*****************************************************");

		List<NameValuePair> headers = dciFunctions.getBasicHeaders(suiteData);	
		verifyRiskTypesDisplayLogs(dciFunctions, fileName, saasType, headers);

		FileType fileClass = dciFunctions.getFileType(suiteData, restClient, additionalParamsUI);
		Logger.info("FileType:" + fileType + "::Count:" + fileClass.getFileTypeCount(fileType));
		int countAfterUI= fileClass.getFileTypeCount(fileType);
		
		Logger.info("****************Actual Output****************");
		Logger.info("Expected Filter Count from UI:"+ (countBeforeUI+1));Logger.info("Actual Filter Count from UI:"+ (countAfterUI));
		Logger.info("Saas App Type:"+suiteData.getSaasApp());
		Logger.info("*****************************************************");

		softAssert.assertEquals(countAfterUI, countAfterExpectedUI,"Risk count is not getting incremented for file:"+fileName+ " via UI");
		softAssert.assertAll();
		
		Logger.info(
				"************************************ Completed the checking of increase in filter count"
						+ " for filename:" + fileName +" with ContentIQ risk types:"+risks+" and saas app type:" + saasType + " ******************");
	}

	@Priority(28)
	@Test(dataProvider = "dataDeleteFileClassTypeExposedPublic", groups ={"ExposedPublic"})
	public void testFilterCountCheckForDeletionOfExposedPublicFileClassTypeFile(TestParameters testParams) throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();

		try{
			String fileType=testParams.getFileType();
			List<String> risks=testParams.getRisks();
			String fileName=testParams.getFileName();
			int countBeforeUI= testParams.getCountUI();
			int countAfterExpectedUI=countBeforeUI-dciFunctions.getExpectedCount(fileName);
			String saasType = SaasType.getSaasFilterType(suiteData.getSaasApp());
			Map<String, String>  additionalParamsUI =  testParams.getHeadersUI();

			Logger.info(
					"************************************ Starting the checking of decrease in filter count"
							+ " for filename:" + fileName +" with ContentIQ risk types:"+risks+" and saas app type:" + saasType + " ******************");

			Logger.info("****************Test Case Description****************");
			Logger.info("Test Case Description: Check the filter count increase after deleting of the ContentIQ risk file");
			Logger.info("*****************************************************");
			Logger.info("****************Expected Output****************");
			Logger.info("Filename:"+fileName);Logger.info("Risk Types to be validated:"+risks);
			Logger.info("Current Filter Count from UI:"+ countBeforeUI);Logger.info("Expected Filter Count from UI:"+ (countAfterExpectedUI));
			Logger.info("Saas App Type:"+suiteData.getSaasApp());
			Logger.info("*****************************************************");

			FileType fileClass = dciFunctions.getFileType(suiteData, restClient, additionalParamsUI);
			Logger.info("FileType:" + fileType + "::Count:" + fileClass.getFileTypeCount(fileType));
			int countAfterUI= fileClass.getFileTypeCount(fileType);
			
			Logger.info("****************Actual Output****************");
			Logger.info("Expected Filter Count from UI:"+ (countAfterExpectedUI));Logger.info("Actual Filter Count from UI:"+ (countAfterUI));
			Logger.info("Saas App Type:"+suiteData.getSaasApp());
			Logger.info("*****************************************************");

			softAssert.assertEquals(countAfterUI, countAfterExpectedUI,"Risk count is not getting decremented for file:"+fileName+ " via UI");
			softAssert.assertAll();
			
			Logger.info(
					"************************************ Completed the checking of decrease in filter count"
							+ " for filename:" + fileName +" with ContentIQ risk types:"+risks+" and saas app type:" + saasType + " ******************");
		}finally{
			dciFunctions.deleteAllCIQProfiles(restClient, suiteData);
		}
	}

	@Priority(29)
	@Test(dataProvider = "dataUploadFileClassTypeExposedInternal", groups ={"ExposedInternal"})
	public void testFilterCountCheckForUploadingOfExposedInternalFileClassTypeFile(TestParameters testParams) throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();

		String fileType=testParams.getFileType();
		List<String> risks=testParams.getRisks();
		String fileName=testParams.getFileName();
		int countBeforeUI= testParams.getCountUI(); 
		int countAfterExpectedUI=countBeforeUI+dciFunctions.getExpectedCount(fileName);		
		String saasType = SaasType.getSaasFilterType(suiteData.getSaasApp());
		Map<String, String>  additionalParamsUI =  testParams.getHeadersUI();

		Logger.info(
				"************************************ Starting the checking of increase in filter count"
						+ " for filename:" + fileName +" with ContentIQ risk types:"+risks+" and saas app type:" + saasType + " ******************");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description: Check the filter count increase after uploading of the ContentIQ risk file");
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output****************");
		Logger.info("Filename:"+fileName);Logger.info("Risk Types to be validated:"+risks);
		Logger.info("Current Filter Count from UI:"+ countBeforeUI);Logger.info("Expected Filter Count from UI:"+ (countAfterExpectedUI));
		Logger.info("Saas App Type:"+suiteData.getSaasApp());
		Logger.info("*****************************************************");

		List<NameValuePair> headers = dciFunctions.getBasicHeaders(suiteData);	
		verifyRiskTypesDisplayLogs(dciFunctions, fileName, saasType, headers);

		FileType fileClass = dciFunctions.getFileType(suiteData, restClient, additionalParamsUI);
		Logger.info("FileType:" + fileType + "::Count:" + fileClass.getFileTypeCount(fileType));
		int countAfterUI= fileClass.getFileTypeCount(fileType);
		
		Logger.info("****************Actual Output****************");
		Logger.info("Expected Filter Count from UI:"+ (countBeforeUI+1));Logger.info("Actual Filter Count from UI:"+ (countAfterUI));
		Logger.info("Saas App Type:"+suiteData.getSaasApp());
		Logger.info("*****************************************************");

		softAssert.assertEquals(countAfterUI, countAfterExpectedUI,"Risk count is not getting incremented for file:"+fileName+ " via UI");
		softAssert.assertAll();
		
		Logger.info(
				"************************************ Completed the checking of increase in filter count"
						+ " for filename:" + fileName +" with ContentIQ risk types:"+risks+" and saas app type:" + saasType + " ******************");
	}

	@Priority(30)
	@Test(dataProvider = "dataDeleteFileClassTypeExposedInternal", groups ={"ExposedInternal"})
	public void testFilterCountCheckForDeletionOfExposedInternalFileClassTypeFile(TestParameters testParams) throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();

		try{
			String fileType=testParams.getFileType();
			List<String> risks=testParams.getRisks();
			String fileName=testParams.getFileName();
			int countBeforeUI= testParams.getCountUI();
			int countAfterExpectedUI=countBeforeUI-dciFunctions.getExpectedCount(fileName);
			String saasType = SaasType.getSaasFilterType(suiteData.getSaasApp());
			
			Map<String, String>  additionalParamsUI =  testParams.getHeadersUI();

			Logger.info(
					"************************************ Starting the checking of decrease in filter count"
							+ " for filename:" + fileName +" with ContentIQ risk types:"+risks+" and saas app type:" + saasType + " ******************");

			Logger.info("****************Test Case Description****************");
			Logger.info("Test Case Description: Check the filter count increase after deleting of the ContentIQ risk file");
			Logger.info("*****************************************************");
			Logger.info("****************Expected Output****************");
			Logger.info("Filename:"+fileName);Logger.info("Risk Types to be validated:"+risks);
			Logger.info("Current Filter Count from UI:"+ countBeforeUI);Logger.info("Expected Filter Count from UI:"+ (countAfterExpectedUI));
			Logger.info("Saas App Type:"+suiteData.getSaasApp());
			Logger.info("*****************************************************");

			FileType fileClass = dciFunctions.getFileType(suiteData, restClient, additionalParamsUI);
			Logger.info("FileType:" + fileType + "::Count:" + fileClass.getFileTypeCount(fileType));
			int countAfterUI= fileClass.getFileTypeCount(fileType);
			
			Logger.info("****************Actual Output****************");
			Logger.info("Expected Filter Count from UI:"+ (countAfterExpectedUI));Logger.info("Actual Filter Count from UI:"+ (countAfterUI));
			Logger.info("Saas App Type:"+suiteData.getSaasApp());
			Logger.info("*****************************************************");

			softAssert.assertEquals(countAfterUI, countAfterExpectedUI,"Risk count is not getting decremented for file:"+fileName+ " via UI");
			softAssert.assertAll();

			Logger.info(
					"************************************ Completed the checking of decrease in filter count"
							+ " for filename:" + fileName +" with ContentIQ risk types:"+risks+" and saas app type:" + saasType + " ******************");
		}finally{
			dciFunctions.deleteAllCIQProfiles(restClient, suiteData);
		}
	}

	@Priority(31)
	@Test(dataProvider = "dataUploadFileClassTypeExposedExternal", groups ={"ExposedExternal"})
	public void testFilterCountCheckForUploadingOfExposedExternalFileClassTypeFile(TestParameters testParams) throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();

		String fileType=testParams.getFileType();
		List<String> risks=testParams.getRisks();
		String fileName=testParams.getFileName();
		int countBeforeUI= testParams.getCountUI(); 
		int countAfterExpectedUI=countBeforeUI+dciFunctions.getExpectedCount(fileName);			
		String saasType = SaasType.getSaasFilterType(suiteData.getSaasApp());
		Map<String, String>  additionalParamsUI =  testParams.getHeadersUI();

		Logger.info(
				"************************************ Starting the checking of increase in filter count"
						+ " for filename:" + fileName +" with ContentIQ risk types:"+risks+" and saas app type:" + saasType + " ******************");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description: Check the filter count increase after uploading of the ContentIQ risk file");
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output****************");
		Logger.info("Filename:"+fileName);Logger.info("Risk Types to be validated:"+risks);
		Logger.info("Current Filter Count from UI:"+ countBeforeUI);Logger.info("Expected Filter Count from UI:"+ (countAfterExpectedUI));
		Logger.info("Saas App Type:"+suiteData.getSaasApp());
		Logger.info("*****************************************************");

		List<NameValuePair> headers = dciFunctions.getBasicHeaders(suiteData);	
		verifyRiskTypesDisplayLogs(dciFunctions, fileName, saasType, headers);

		FileType fileClass = dciFunctions.getFileType(suiteData, restClient, additionalParamsUI);
		Logger.info("FileType:" + fileType + "::Count:" + fileClass.getFileTypeCount(fileType));
		int countAfterUI= fileClass.getFileTypeCount(fileType);
		
		Logger.info("****************Actual Output****************");
		Logger.info("Expected Filter Count from UI:"+ (countBeforeUI+1));Logger.info("Actual Filter Count from UI:"+ (countAfterUI));
		Logger.info("Saas App Type:"+suiteData.getSaasApp());
		Logger.info("*****************************************************");

		softAssert.assertEquals(countAfterUI, countAfterExpectedUI,"Risk count is not getting incremented for file:"+fileName+ " via UI");
		softAssert.assertAll();

		Logger.info(
				"************************************ Completed the checking of increase in filter count"
						+ " for filename:" + fileName +" with ContentIQ risk types:"+risks+" and saas app type:" + saasType + " ******************");
	}

	@Priority(32)
	@Test(dataProvider = "dataDeleteFileClassTypeExposedExternal", groups ={"ExposedExternal"})
	public void testFilterCountCheckForDeletionOfExposedExternalFileClassTypeFile(TestParameters testParams) throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();

		try{
			String fileType=testParams.getFileType();
			List<String> risks=testParams.getRisks();
			String fileName=testParams.getFileName();
			int countBeforeUI= testParams.getCountUI();
			int countAfterExpectedUI=countBeforeUI-dciFunctions.getExpectedCount(fileName);	
			String saasType = SaasType.getSaasFilterType(suiteData.getSaasApp());
			Map<String, String>  additionalParamsUI =  testParams.getHeadersUI();

			Logger.info(
					"************************************ Starting the checking of decrease in filter count"
							+ " for filename:" + fileName +" with ContentIQ risk types:"+risks+" and saas app type:" + saasType + " ******************");

			Logger.info("****************Test Case Description****************");
			Logger.info("Test Case Description: Check the filter count increase after deleting of the ContentIQ risk file");
			Logger.info("*****************************************************");
			Logger.info("****************Expected Output****************");
			Logger.info("Filename:"+fileName);Logger.info("Risk Types to be validated:"+risks);
			Logger.info("Current Filter Count from UI:"+ countBeforeUI);Logger.info("Expected Filter Count from UI:"+ (countAfterExpectedUI));
			Logger.info("Saas App Type:"+suiteData.getSaasApp());
			Logger.info("*****************************************************");

			FileType fileClass = dciFunctions.getFileType(suiteData, restClient, additionalParamsUI);
			Logger.info("FileType:" + fileType + "::Count:" + fileClass.getFileTypeCount(fileType));
			int countAfterUI= fileClass.getFileTypeCount(fileType);
			
			Logger.info("****************Actual Output****************");
			Logger.info("Expected Filter Count from UI:"+ (countAfterExpectedUI));Logger.info("Actual Filter Count from UI:"+ (countAfterUI));
			Logger.info("Saas App Type:"+suiteData.getSaasApp());
			Logger.info("*****************************************************");

			softAssert.assertEquals(countAfterUI, countAfterExpectedUI,"Risk count is not getting decremented for file:"+fileName+ " via UI");
			softAssert.assertAll();

			Logger.info(
					"************************************ Completed the checking of decrease in filter count"
							+ " for filename:" + fileName +" with ContentIQ risk types:"+risks+" and saas app type:" + saasType + " ******************");
		}finally{
			dciFunctions.deleteAllCIQProfiles(restClient, suiteData);
		}
	}


	/**********************************************TEST METHODS***********************************************/
	/**********************************************UTIL METHODS***********************************************/

	@Override
	public String getTestName() {
		return this.mTestCaseName;
	}

	private void verifyRiskTypesDisplayLogs(DCIFunctions dciFunctions, String fileName, String saasType,
			List<NameValuePair> headers)
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


			hits = fetchDisplayLogsCounter(dciFunctions, headers, payload, DCIConstants.DCI_COUNTER_MEDIUM_LL);
			
			Logger.info("****************Output Response****************");
			Logger.info(hits);
			Logger.info("***********************************************");

			//JSONArray jArray = (JSONArray) new JSONTokener(hits).nextValue();
			//Assert.assertTrue(jArray.size() == 1, "Expected atleast one risk type log for file upload of " + fileName);


		} finally {
			dciFunctions.cleanupFileFromTempFolder(fileName);
		}
	}

	private void verifyRiskContentTypesDisplayLogs(DCIFunctions dciFunctions, String fileName, String saasType,
			List<NameValuePair> headers)
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

			hits = fetchDisplayLogsCounter(dciFunctions, headers, payload, DCIConstants.DCI_COUNTER_MEDIUM_LL);

			Logger.info("****************Output Response****************");
			Logger.info(hits);
			Logger.info("***********************************************");

			//JSONArray jArray = (JSONArray) new JSONTokener(hits).nextValue();
			//Assert.assertTrue(jArray.size() == 1, "Expected atleast one content type log for file upload of " + fileName);


		} finally {
			dciFunctions.cleanupFileFromTempFolder(fileName);
		}
	}

	private void verifyContentTypesDisplayLogs(DCIFunctions dciFunctions, String fileName, String saasType,
			List<NameValuePair> headers)
					throws Exception {
		String hits = "";

		try {

			String payload = 
					dciFunctions.getSearchQueryForDCI(suiteData, dciFunctions.getMinusMinutesFromCurrentTime(1440) , 
							dciFunctions.getPlusMinutesFromCurrentTime(120), saasType, DCIConstants.CISourceType, 
							DCIConstants.CIInformationalSeverityType, fileName, DCIConstants.CIFacilityType, DCIConstants.CIActivityType);
			Logger.info("****************Input Payload****************");
			Logger.info(payload);
			Logger.info("*********************************************");

			hits = fetchDisplayLogsCounter(dciFunctions, headers, payload, DCIConstants.DCI_COUNTER_MEDIUM_LL);

			Logger.info("****************Output Response****************");
			Logger.info(hits);
			Logger.info("***********************************************");

			//JSONArray jArray = (JSONArray) new JSONTokener(hits).nextValue();
			//Assert.assertTrue(jArray.size() == 1, "Expected atleast one content type log for file upload of " + fileName);


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

		if(mainCounter>=DCIConstants.DCI_COUNTER_MEDIUM_LL){
			testCounter=DCIConstants.DCI_COUNTER_LL;
		}
		return hits;
	}

	/**********************************************UTIL METHODS***********************************************/
	/**********************************************DATA PROVIDERS*********************************************/

	@DataProvider(name = "dataUploadRiskTypeUnexposed")
	public Object[][] dataUploadRiskTypeUnexposed() {

		dciFunctions = new DCIFunctions();

		UserAccount account = dciFunctions.getUserAccount(suiteData);
		UniversalApi universalApi = dciFunctions.getUniversalApi(suiteData, account);
		folderInfo = dciFunctions.createFolder(universalApi, suiteData,
				DCIConstants.DCI_FOLDER+uniqueId);
		
		String saasType = SaasType.getSaasFilterType(suiteData.getSaasApp());
		String appType = SaasType.getAppType(suiteData.getSaasApp(), suiteData.getEnvironmentName());
		String vlType = SaasType.getVlType(suiteData.getSaasApp());
		String objectType = SaasType.getObjectType(suiteData.getSaasApp());
		Map<String, String>  additionalParamsAPI = dciFunctions.generateHeadersAPI("false","true");
		Map<String, String>  additionalParamsUI = dciFunctions.generateHeadersUI(vlType, "true", "risky_docs", objectType);


		fileId = new String[riskFileName.length];
		fileEtag = new String[riskFileName.length];

		int[] countBeforeUI= new int[riskFileName.length];
		int[] countBeforeAPI= new int[riskFileName.length];
		
		Object[][] result = new Object[riskFileName.length][];

		try {
			riskFileName = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_RISK_TYPES_PATH, riskFileName);

			for (int i = 0; i < riskFileName.length; i++) {
				
				Vulnerability vulnerability = dciFunctions.getVulnerability(suiteData, restClient, additionalParamsUI);
				Logger.info("Vulnerability:" + riskType[i] + "::Count:" + vulnerability.getResults().getVulnerabilityCount(riskType[i]));
				VulnerabilityTypes vulnerabilityTypes = dciFunctions.getVulnerabilityTypes(suiteData, restClient, appType,additionalParamsAPI);
				Logger.info("Vulnerability:" + riskType[i] + "::Count:" + vulnerabilityTypes.getObjects().getVulnerabilityCount(riskType[i]));
				countBeforeUI[i]= vulnerability.getResults().getVulnerabilityCount(riskType[i]);
				countBeforeAPI[i]= vulnerabilityTypes.getObjects().getVulnerabilityCount(riskType[i]);

			}
			
			for (int i = 0; i < riskFileName.length; i++) {
				Map<String, String> fileInfo = dciFunctions.uploadFile(universalApi,suiteData, folderInfo.get("folderId"), riskFileName[i]);
				fileId[i] = fileInfo.get("fileId");
				fileEtag[i] = fileInfo.get("fileEtag");
				

				result[i] = new Object[] { new TestParameters("Filter count check"+riskFileName[i]+" for upload of risk types", 
						"Upload a risks types file:"+riskFileName[i]+ ". Then verify risk logs are getting generated within the SLA provided and count is getting increased ",
						riskFileName[i], saasType, riskType[i], Arrays.asList(risks[i].split(",")), countBeforeUI[i], countBeforeAPI[i], additionalParamsUI, additionalParamsAPI)};
			}
			

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}	

		dciFunctions.waitForMinutes(5);
		
		mainCounter = 0;testCounter = DCIConstants.DCI_COUNTER_MEDIUM_LL;		
		return result;
	}

	@DataProvider(name = "dataDeleteRiskTypeUnexposed")
	public Object[][] dataDeleteRiskTypeUnexposed() {

		dciFunctions = new DCIFunctions();
		Object[][] result = new Object[riskFileName.length][];
		String saasType = SaasType.getSaasFilterType(suiteData.getSaasApp());
		String appType = SaasType.getAppType(suiteData.getSaasApp(), suiteData.getEnvironmentName());
		String vlType = SaasType.getVlType(suiteData.getSaasApp());
		String objectType = SaasType.getObjectType(suiteData.getSaasApp());
		Map<String, String>  additionalParamsAPI = dciFunctions.generateHeadersAPI("false","true");
		Map<String, String>  additionalParamsUI = dciFunctions.generateHeadersUI(vlType, "true", "risky_docs", objectType);

		try {

			for (int i = 0; i < riskFileName.length; i++) {
				
				Vulnerability vulnerability = dciFunctions.getVulnerability(suiteData, restClient, additionalParamsUI);
				Logger.info("Vulnerability:" + riskType[i] + "::Count:" + vulnerability.getResults().getVulnerabilityCount(riskType[i]));
				VulnerabilityTypes vulnerabilityTypes = dciFunctions.getVulnerabilityTypes(suiteData, restClient, appType,additionalParamsAPI);
				Logger.info("Vulnerability:" + riskType[i] + "::Count:" + vulnerabilityTypes.getObjects().getVulnerabilityCount(riskType[i]));
				int countBeforeUI= vulnerability.getResults().getVulnerabilityCount(riskType[i]);
				int countBeforeAPI= vulnerabilityTypes.getObjects().getVulnerabilityCount(riskType[i]);


				Map<String, String> fileInfo = new HashMap<String, String>();
				fileInfo.put("fileId",fileId[i]);
				fileInfo.put("fileEtag",fileEtag[i]);

				result[i] = new Object[] { new TestParameters("Filter count check"+riskFileName[i]+" for deletion of risks types", 
						"Delete a risks types file:"+riskFileName[i]+ ". Then verify risk logs are getting generated within the SLA provided and count is getting decreased ",
						riskFileName[i], saasType, riskType[i], Arrays.asList(risks[i].split(",")), countBeforeUI, countBeforeAPI, additionalParamsUI, additionalParamsAPI)};
			}

			dciFunctions.waitForMinutes(1);
			UserAccount account = dciFunctions.getUserAccount(suiteData);
			UniversalApi universalApi = dciFunctions.getUniversalApi(suiteData, account);
			dciFunctions.deleteFolder(universalApi, suiteData, folderInfo);

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}	

		dciFunctions.waitForMinutes(5);

		return result;
	}

	@DataProvider(name = "dataUploadContentTypeUnexposed")
	public Object[][] dataUploadContentTypeUnexposed() {
		dciFunctions = new DCIFunctions();

		UserAccount account = dciFunctions.getUserAccount(suiteData);
		UniversalApi universalApi = dciFunctions.getUniversalApi(suiteData, account);
		folderInfo = dciFunctions.createFolder(universalApi, suiteData, 
				DCIConstants.DCI_FOLDER+uniqueId);
		String saasType = SaasType.getSaasFilterType(suiteData.getSaasApp());
		String appType = SaasType.getAppType(suiteData.getSaasApp(), suiteData.getEnvironmentName());
		String vlType = SaasType.getVlType(suiteData.getSaasApp());
		String objectType = SaasType.getObjectType(suiteData.getSaasApp());
		Map<String, String>  additionalParamsAPI = dciFunctions.generateHeadersAPI("false","true");
		Map<String, String>  additionalParamsUI = dciFunctions.generateHeadersUI(vlType, "true", "risky_docs", objectType);
		
		fileId = new String[contentRiskFileName.length];
		fileEtag = new String[contentRiskFileName.length];
		int[] countBeforeUI= new int[contentRiskFileName.length];
		int[] countBeforeAPI= new int[contentRiskFileName.length];
		
		Object[][] result = new Object[contentRiskFileName.length][];

		try {
			contentRiskFileName = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_RISK_CONTENT_TYPES_PATH, contentRiskFileName);

			for (int i = 0; i < contentRiskFileName.length; i++) {
				
				ContentType cTypes = dciFunctions.getContentType(suiteData, restClient, additionalParamsUI);
				Logger.info("ContentType:" + contentRiskType[i] + "::Count:" + cTypes.getContentTypeCount(contentRiskType[i]));
				ContentTypes cType = dciFunctions.getContentTypes(suiteData, restClient, appType,additionalParamsAPI);
				Logger.info("ContentType:" + contentRiskType[i] + "::Count:" + cType.getContentTypeCount(contentRiskType[i]));
				countBeforeUI[i]= cTypes.getContentTypeCount(contentRiskType[i]);
				countBeforeAPI[i]= cType.getContentTypeCount(contentRiskType[i]);
			}	
			
			for (int i = 0; i < contentRiskFileName.length; i++) {
				Map<String, String> fileInfo = dciFunctions.uploadFile(universalApi,suiteData, folderInfo.get("folderId"), contentRiskFileName[i]);
				fileId[i] = fileInfo.get("fileId");
				fileEtag[i] = fileInfo.get("fileEtag");

				result[i] = new Object[] { new TestParameters("Filter count check"+contentRiskFileName[i]+" for upload of content types", 
						"Upload a content types file:"+contentRiskFileName[i]+ ". Then verify risk logs are getting generated within the SLA provided and count is getting increased ",
						contentRiskFileName[i], saasType, contentRiskType[i], Arrays.asList(contentRiskTypes[i].split(",")), countBeforeUI[i], countBeforeAPI[i], additionalParamsUI, additionalParamsAPI)};
			}

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}	
		
		dciFunctions.waitForMinutes(5);
		
		mainCounter = 0;testCounter = DCIConstants.DCI_COUNTER_MEDIUM_LL;

		return result;
	}

	@DataProvider(name = "dataDeleteContentTypeUnexposed")
	public Object[][] dataDeleteContentTypeUnexposed() {
		dciFunctions = new DCIFunctions();
		Object[][] result = new Object[contentRiskFileName.length][];
		String saasType = SaasType.getSaasFilterType(suiteData.getSaasApp());
		String appType = SaasType.getAppType(suiteData.getSaasApp(), suiteData.getEnvironmentName());
		String vlType = SaasType.getVlType(suiteData.getSaasApp());
		String objectType = SaasType.getObjectType(suiteData.getSaasApp());
		Map<String, String>  additionalParamsAPI = dciFunctions.generateHeadersAPI("false","true");
		Map<String, String>  additionalParamsUI = dciFunctions.generateHeadersUI(vlType, "true", "risky_docs", objectType);

		
		try {


			for (int i = 0; i < contentRiskFileName.length; i++) {
				ContentType cTypes = dciFunctions.getContentType(suiteData, restClient, additionalParamsUI);
				Logger.info("ContentType:" + contentRiskType[i] + "::Count:" + cTypes.getContentTypeCount(contentRiskType[i]));
				ContentTypes cType = dciFunctions.getContentTypes(suiteData, restClient, appType,additionalParamsAPI);
				Logger.info("ContentType:" + contentRiskType[i] + "::Count:" + cType.getContentTypeCount(contentRiskType[i]));
				int countBeforeUI= cTypes.getContentTypeCount(contentRiskType[i]);
				int countBeforeAPI= cType.getContentTypeCount(contentRiskType[i]);

				Map<String, String> fileInfo = new HashMap<String, String>();
				fileInfo.put("fileId",fileId[i]);
				fileInfo.put("fileEtag",fileEtag[i]);

				result[i] = new Object[] { new TestParameters("Filter count check"+contentRiskFileName[i]+" for deletion of content types", 
						"Delete a risks/content types file:"+contentRiskFileName[i]+ ". Then verify risk logs are getting generated within the SLA provided and count is getting decreased ",
						contentRiskFileName[i], saasType, contentRiskType[i], Arrays.asList(contentRiskTypes[i].split(",")), countBeforeUI, countBeforeAPI, additionalParamsUI, additionalParamsAPI)};
			}

			dciFunctions.waitForMinutes(1);
			UserAccount account = dciFunctions.getUserAccount(suiteData);
			UniversalApi universalApi = dciFunctions.getUniversalApi(suiteData, account);
			dciFunctions.deleteFolder(universalApi, suiteData, folderInfo);


		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}	

		dciFunctions.waitForMinutes(5);

		return result;
	}

	@DataProvider(name = "dataUploadRiskTypeExposedPublic")
	public Object[][] dataUploadRiskTypeExposedPublic() {

		dciFunctions = new DCIFunctions();

		UserAccount account = dciFunctions.getUserAccount(suiteData);
		UniversalApi universalApi = dciFunctions.getUniversalApi(suiteData, account);
		
		if(SaasType.getSaasType(suiteData.getSaasApp()).equals(SaasType.Office365)){
			folderResource = dciFunctions.createFolderInOneDrive(universalApi, suiteData,
					DCIConstants.DCI_FOLDER+uniqueId);
		}else{
			folderInfo = dciFunctions.createFolder(universalApi, suiteData, 
					DCIConstants.DCI_FOLDER+uniqueId);
		}
		
		
		String saasType = SaasType.getSaasFilterType(suiteData.getSaasApp());
		String appType = SaasType.getAppType(suiteData.getSaasApp(), suiteData.getEnvironmentName());
		String vlType = SaasType.getVlType(suiteData.getSaasApp());
		String objectType = SaasType.getObjectType(suiteData.getSaasApp());
		Map<String, String>  additionalParamsAPI = dciFunctions.generateHeadersAPI("true","true");
		Map<String, String>  additionalParamsUI = dciFunctions.generateHeadersUI(vlType, "true", objectType);


		fileId = new String[riskFileName.length];
		fileEtag = new String[riskFileName.length];
		fileResource = new ItemResource[riskFileName.length];

		int[] countBeforeUI = new int[riskFileName.length];
		int[] countBeforeAPI = new int[riskFileName.length];

		Object[][] result = new Object[riskFileName.length][];

		try {
			riskFileName = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_RISK_TYPES_PATH, riskFileName);

			for (int i = 0; i < riskFileName.length; i++) {
				
				Vulnerability vulnerability = dciFunctions.getVulnerability(suiteData, restClient, additionalParamsUI);
				Logger.info("Vulnerability:" + riskType[i] + "::Count:" + vulnerability.getResults().getVulnerabilityCount(riskType[i]));
				VulnerabilityTypes vulnerabilityTypes = dciFunctions.getVulnerabilityTypes(suiteData, restClient, appType,additionalParamsAPI);
				Logger.info("Vulnerability:" + riskType[i] + "::Count:" + vulnerabilityTypes.getObjects().getVulnerabilityCount(riskType[i]));
				countBeforeUI[i]= vulnerability.getResults().getVulnerabilityCount(riskType[i]);
			    countBeforeAPI[i]= vulnerabilityTypes.getObjects().getVulnerabilityCount(riskType[i]);

			}
			
			for (int i = 0; i < riskFileName.length; i++) {
				if(SaasType.getSaasType(suiteData.getSaasApp()).equals(SaasType.Office365)){
					fileResource[i] = dciFunctions.uploadFileInOneDrive(universalApi, suiteData, 
							folderResource.getId(), riskFileName[i]);
					
					dciFunctions.shareFileOnOneDrive(fileResource[i], universalApi, "Public");
				}else{
					Map<String, String> fileInfo = dciFunctions.uploadFile(universalApi,suiteData, 
							folderInfo.get("folderId"), riskFileName[i]);
					fileId[i] = fileInfo.get("fileId");
					fileEtag[i] = fileInfo.get("fileEtag");

					fileInfo = dciFunctions.shareFile(suiteData, universalApi, folderInfo, fileInfo, "Public");
					fileId[i] = fileInfo.get("fileId");
					fileEtag[i] = fileInfo.get("fileEtag");

				}
				
				
				result[i] = new Object[] { new TestParameters("Filter count check"+riskFileName[i]+" for exposed public risks types", 
						"Upload and share publicly risks types file:"+riskFileName[i]+ ". Then verify risk logs are getting generated within the SLA provided and count is getting increased ",
						riskFileName[i], saasType, riskType[i], Arrays.asList(risks[i].split(",")), countBeforeUI[i], countBeforeAPI[i], additionalParamsUI, additionalParamsAPI)};
			}

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}	

		dciFunctions.waitForMinutes(5);

		mainCounter = 0;testCounter = DCIConstants.DCI_COUNTER_MEDIUM_LL;
		
		return result;
	}

	@DataProvider(name = "dataDeleteRiskTypeExposedPublic")
	public Object[][] dataDeleteRiskTypeExposedPublic() {

		dciFunctions = new DCIFunctions();
		String saasType = SaasType.getSaasFilterType(suiteData.getSaasApp());
		String appType = SaasType.getAppType(suiteData.getSaasApp(), suiteData.getEnvironmentName());
		String vlType = SaasType.getVlType(suiteData.getSaasApp());
		String objectType = SaasType.getObjectType(suiteData.getSaasApp());
		Map<String, String>  additionalParamsAPI = dciFunctions.generateHeadersAPI("true","true");
		Map<String, String>  additionalParamsUI = dciFunctions.generateHeadersUI(vlType, "true", objectType);

		Object[][] result = new Object[riskFileName.length][];
	
		try {
			for (int i = 0; i < riskFileName.length; i++) {
				
				Vulnerability vulnerability = dciFunctions.getVulnerability(suiteData, restClient, additionalParamsUI);
				Logger.info("Vulnerability:" + riskType[i] + "::Count:" + vulnerability.getResults().getVulnerabilityCount(riskType[i]));
				VulnerabilityTypes vulnerabilityTypes = dciFunctions.getVulnerabilityTypes(suiteData, restClient, appType,additionalParamsAPI);
				Logger.info("Vulnerability:" + riskType[i] + "::Count:" + vulnerabilityTypes.getObjects().getVulnerabilityCount(riskType[i]));
				int countBeforeUI= vulnerability.getResults().getVulnerabilityCount(riskType[i]);
				int countBeforeAPI= vulnerabilityTypes.getObjects().getVulnerabilityCount(riskType[i]);


				Map<String, String> fileInfo = new HashMap<String, String>();
				fileInfo.put("fileId",fileId[i]);
				fileInfo.put("fileEtag",fileEtag[i]);

				result[i] = new Object[] { new TestParameters("Filter count check"+riskFileName[i]+" for deletion of exposed public risks types", 
						"Delete a public shared risks types file:"+riskFileName[i]+ ". Then verify risk logs are getting generated within the SLA provided and count is getting decreased ",
						riskFileName[i], saasType, riskType[i], Arrays.asList(risks[i].split(",")), countBeforeUI, countBeforeAPI, additionalParamsUI, additionalParamsAPI)};
			}

			dciFunctions.waitForMinutes(1);
			UserAccount account = dciFunctions.getUserAccount(suiteData);
			UniversalApi universalApi = dciFunctions.getUniversalApi(suiteData, account);
			
			if(SaasType.getSaasType(suiteData.getSaasApp()).equals(SaasType.Office365)){
				Map<String,String> folderInfo = new HashMap<String,String>();
				folderInfo.put("folderName", folderResource.getName());
				folderInfo.put("folderId", folderResource.getId());
				folderInfo.put("folderEtag", folderResource.getETag());
				folderInfo.put("folderType", folderResource.getType()); 
				
				dciFunctions.deleteFolder(universalApi, suiteData, folderInfo);
			}else{
				dciFunctions.deleteFolder(universalApi, suiteData, folderInfo);
			}
			
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}	

		dciFunctions.waitForMinutes(5);

		return result;
	}

	@DataProvider(name = "dataUploadContentTypeExposedPublic")
	public Object[][] dataUploadContentTypeExposedPublic() {
		dciFunctions = new DCIFunctions();

		UserAccount account = dciFunctions.getUserAccount(suiteData);
		UniversalApi universalApi = dciFunctions.getUniversalApi(suiteData, account);
		if(SaasType.getSaasType(suiteData.getSaasApp()).equals(SaasType.Office365)){
			folderResource = dciFunctions.createFolderInOneDrive(universalApi, suiteData,
					DCIConstants.DCI_FOLDER+uniqueId);
		}else{
			folderInfo = dciFunctions.createFolder(universalApi, suiteData, 
					DCIConstants.DCI_FOLDER+uniqueId);
		}
		
		String saasType = SaasType.getSaasFilterType(suiteData.getSaasApp());
		String appType = SaasType.getAppType(suiteData.getSaasApp(), suiteData.getEnvironmentName());
		String vlType = SaasType.getVlType(suiteData.getSaasApp());
		String objectType = SaasType.getObjectType(suiteData.getSaasApp());
		Map<String, String>  additionalParamsAPI = dciFunctions.generateHeadersAPI("true","true");
		Map<String, String>  additionalParamsUI = dciFunctions.generateHeadersUI(vlType, "true", objectType);

		
		fileId = new String[contentFileName.length];
		fileEtag = new String[contentFileName.length];
		fileResource = new ItemResource[contentFileName.length];

		int[] countBeforeUI = new int[contentFileName.length];
		int[] countBeforeAPI = new int[contentFileName.length];

		Object[][] result = new Object[contentFileName.length][];

		try {
			contentFileName = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_CONTENT_TYPES_PATH, contentFileName);

			for (int i = 0; i < contentFileName.length; i++) {
				
				ContentType cTypes = dciFunctions.getContentType(suiteData, restClient, additionalParamsUI);
				Logger.info("ContentType:" + contentType[i] + "::Count:" + cTypes.getContentTypeCount(contentType[i]));
				ContentTypes cType = dciFunctions.getContentTypes(suiteData, restClient, appType,additionalParamsAPI);
				Logger.info("ContentType:" + contentType[i] + "::Count:" + cType.getContentTypeCount(contentType[i]));
				countBeforeUI[i]= cTypes.getContentTypeCount(contentType[i]);
				countBeforeAPI[i]= cType.getContentTypeCount(contentType[i]);

			}
			
			for (int i = 0; i < contentFileName.length; i++) {
				if(SaasType.getSaasType(suiteData.getSaasApp()).equals(SaasType.Office365)){
					fileResource[i] = dciFunctions.uploadFileInOneDrive(universalApi, suiteData, 
							folderResource.getId(), contentFileName[i]);
					
					dciFunctions.shareFileOnOneDrive(fileResource[i], universalApi, "Public");
				}else{
					Map<String, String> fileInfo = dciFunctions.uploadFile(universalApi,suiteData, 
							folderInfo.get("folderId"), contentFileName[i]);
					fileId[i] = fileInfo.get("fileId");
					fileEtag[i] = fileInfo.get("fileEtag");

					fileInfo = dciFunctions.shareFile(suiteData, universalApi, folderInfo, fileInfo, "Public");
					fileId[i] = fileInfo.get("fileId");
					fileEtag[i] = fileInfo.get("fileEtag");

				}

				result[i] = new Object[] { new TestParameters("Filter count check"+contentFileName[i]+" for upload of exposed public content types", 
						"Upload a exposed publicly content types file:"+contentFileName[i]+ ". Then verify risk logs are getting generated within the SLA provided and count is getting increased ",
						contentFileName[i], saasType, contentType[i], Arrays.asList(contentTypes[i].split(",")), countBeforeUI[i], countBeforeAPI[i], additionalParamsUI, additionalParamsAPI)};
			}

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}	

		dciFunctions.waitForMinutes(5);

		mainCounter = 0;testCounter = DCIConstants.DCI_COUNTER_MEDIUM_LL;
		
		return result;
	}

	@DataProvider(name = "dataDeleteContentTypeExposedPublic")
	public Object[][] dataDeleteContentTypeExposedPublic() {


		dciFunctions = new DCIFunctions();
		String saasType = SaasType.getSaasFilterType(suiteData.getSaasApp());
		String appType = SaasType.getAppType(suiteData.getSaasApp(), suiteData.getEnvironmentName());
		String vlType = SaasType.getVlType(suiteData.getSaasApp());
		String objectType = SaasType.getObjectType(suiteData.getSaasApp());
		Map<String, String>  additionalParamsAPI = dciFunctions.generateHeadersAPI("true","true");
		Map<String, String>  additionalParamsUI = dciFunctions.generateHeadersUI(vlType, "true", objectType);

		Object[][] result = new Object[contentFileName.length][];

		try {


			for (int i = 0; i < contentFileName.length; i++) {

				ContentType cTypes = dciFunctions.getContentType(suiteData, restClient, additionalParamsUI);
				Logger.info("ContentType:" + contentType[i] + "::Count:" + cTypes.getContentTypeCount(contentType[i]));
				ContentTypes cType = dciFunctions.getContentTypes(suiteData, restClient, appType,additionalParamsAPI);
				Logger.info("ContentType:" + contentType[i] + "::Count:" + cType.getContentTypeCount(contentType[i]));
				int countBeforeUI= cTypes.getContentTypeCount(contentType[i]);
				int countBeforeAPI= cType.getContentTypeCount(contentType[i]);

				Map<String, String> fileInfo = new HashMap<String, String>();
				fileInfo.put("fileId",fileId[i]);
				fileInfo.put("fileEtag",fileEtag[i]);

				result[i] = new Object[] { new TestParameters("Filter count check"+contentFileName[i]+" for deletion of exposed public content types", 
						"Delete a expose public content types file:"+contentFileName[i]+ ". Then verify risk logs are getting generated within the SLA provided and count is getting decreased ",
						contentFileName[i], saasType, contentType[i], Arrays.asList(contentTypes[i].split(",")), countBeforeUI, countBeforeAPI, additionalParamsUI, additionalParamsAPI)};
			}

			dciFunctions.waitForMinutes(1);
			UserAccount account = dciFunctions.getUserAccount(suiteData);
			UniversalApi universalApi = dciFunctions.getUniversalApi(suiteData, account);
			if(SaasType.getSaasType(suiteData.getSaasApp()).equals(SaasType.Office365)){
				Map<String,String> folderInfo = new HashMap<String,String>();
				folderInfo.put("folderName", folderResource.getName());
				folderInfo.put("folderId", folderResource.getId());
				folderInfo.put("folderEtag", folderResource.getETag());
				folderInfo.put("folderType", folderResource.getType()); 
				
				dciFunctions.deleteFolder(universalApi, suiteData, folderInfo);
			}else{
				dciFunctions.deleteFolder(universalApi, suiteData, folderInfo);
			}

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}	


		dciFunctions.waitForMinutes(5);

		return result;
	}

	@DataProvider(name = "dataUploadRiskTypeExposedInternal")
	public Object[][] dataUploadRiskTypeExposedInternal() {

		dciFunctions = new DCIFunctions();

		String saasType = SaasType.getSaasFilterType(suiteData.getSaasApp());
		String appType = SaasType.getAppType(suiteData.getSaasApp(), suiteData.getEnvironmentName());
		String vlType = SaasType.getVlType(suiteData.getSaasApp());
		String objectType = SaasType.getObjectType(suiteData.getSaasApp());
		Map<String, String>  additionalParamsAPI = dciFunctions.generateHeadersAPI("true","true");
		Map<String, String>  additionalParamsUI = dciFunctions.generateHeadersUI(vlType, "true", objectType);
		
		UserAccount account = dciFunctions.getUserAccount(suiteData);
		UniversalApi universalApi = dciFunctions.getUniversalApi(suiteData, account);
		if(SaasType.getSaasType(suiteData.getSaasApp()).equals(SaasType.Office365)){
			folderResource = dciFunctions.createFolderInOneDrive(universalApi, suiteData,
					DCIConstants.DCI_FOLDER+uniqueId);
		}else if(SaasType.getSaasType(suiteData.getSaasApp()).equals(SaasType.Dropbox)){
			Logger.info("Folder in dropbox is not created");
			folderInfo.put("folderName", File.separator+dciFunctions.getDropboxInternalFolderName(suiteData));
			folderInfo.put("folderId", File.separator+dciFunctions.getDropboxInternalFolderName(suiteData));
			folderInfo.put("folderEtag", "");
			folderInfo.put("folderType", ""); 
		}else{
			folderInfo = dciFunctions.createFolder(universalApi, suiteData, 
					DCIConstants.DCI_FOLDER+uniqueId);
		}

		fileName = new String[riskFileName.length];
		fileId = new String[riskFileName.length];
		fileEtag = new String[riskFileName.length];
		fileResource = new ItemResource[riskFileName.length];

		int[] countBeforeUI = new int[riskFileName.length];
		int[] countBeforeAPI = new int[riskFileName.length];
		
		
		Object[][] result = new Object[riskFileName.length][];

		try {
			riskFileName = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_RISK_TYPES_PATH, riskFileName);
			
			for (int i = 0; i < riskFileName.length; i++) {
				Vulnerability vulnerability = dciFunctions.getVulnerability(suiteData, restClient, additionalParamsUI);
				Logger.info("Vulnerability:" + riskType[i] + "::Count:" + vulnerability.getResults().getVulnerabilityCount(riskType[i]));
				VulnerabilityTypes vulnerabilityTypes = dciFunctions.getVulnerabilityTypes(suiteData, restClient, appType,additionalParamsAPI);
				Logger.info("Vulnerability:" + riskType[i] + "::Count:" + vulnerabilityTypes.getObjects().getVulnerabilityCount(riskType[i]));
				countBeforeUI[i]= vulnerability.getResults().getVulnerabilityCount(riskType[i]);
				countBeforeAPI[i]= vulnerabilityTypes.getObjects().getVulnerabilityCount(riskType[i]);
			}

			for (int i = 0; i < riskFileName.length; i++) {
				if(SaasType.getSaasType(suiteData.getSaasApp()).equals(SaasType.Office365)){
					fileResource[i] = dciFunctions.uploadFileInOneDrive(universalApi, suiteData, 
							folderResource.getId(), riskFileName[i]);
					
					dciFunctions.shareFileOnOneDrive(fileResource[i], universalApi, "Internal");
				}else if(SaasType.getSaasType(suiteData.getSaasApp()).equals(SaasType.Dropbox)){
					Map<String, String> fileInfo = new HashMap<String, String>();
					fileInfo.put("fileName", folderInfo.get("folderName")+File.separator+riskFileName[i]);
					fileInfo.put("fileId", folderInfo.get("folderName")+File.separator+riskFileName[i]);
					fileInfo.put("fileEtag", "");
					 
					fileInfo = dciFunctions.shareFile(suiteData, universalApi, folderInfo, fileInfo, "Internal");
					fileName[i] = fileInfo.get("fileName");
					fileId[i] = fileInfo.get("fileId");
					fileEtag[i] = fileInfo.get("fileEtag");
				}else{
					Map<String, String> fileInfo = dciFunctions.uploadFile(universalApi,suiteData, 
							folderInfo.get("folderId"), riskFileName[i]);
					fileId[i] = fileInfo.get("fileId");
					fileEtag[i] = fileInfo.get("fileEtag");

					fileInfo = dciFunctions.shareFile(suiteData, universalApi, folderInfo, fileInfo, "Internal");
					fileId[i] = fileInfo.get("fileId");
					fileEtag[i] = fileInfo.get("fileEtag");

				}

				result[i] = new Object[] { new TestParameters("Filter count check"+riskFileName[i]+" for exposed internal risks types", 
						"Upload and share internal risks types file:"+riskFileName[i]+ ". Then verify risk logs are getting generated within the SLA provided and count is getting increased ",
						riskFileName[i], saasType, riskType[i], Arrays.asList(risks[i].split(",")), countBeforeUI[i], countBeforeAPI[i], additionalParamsUI, additionalParamsAPI)};
			}

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}	

		dciFunctions.waitForMinutes(5);

		mainCounter = 0;testCounter = DCIConstants.DCI_COUNTER_MEDIUM_LL;
		
		return result;
	}

	@DataProvider(name = "dataDeleteRiskTypeExposedInternal")
	public Object[][] dataDeleteRiskTypeExposedInternal() {

		dciFunctions = new DCIFunctions();
		String saasType = SaasType.getSaasFilterType(suiteData.getSaasApp());
		String appType = SaasType.getAppType(suiteData.getSaasApp(), suiteData.getEnvironmentName());
		String vlType = SaasType.getVlType(suiteData.getSaasApp());
		String objectType = SaasType.getObjectType(suiteData.getSaasApp());
		Map<String, String>  additionalParamsAPI = dciFunctions.generateHeadersAPI("true","true");
		Map<String, String>  additionalParamsUI = dciFunctions.generateHeadersUI(vlType, "true", objectType);

		Object[][] result = new Object[riskFileName.length][];

		try {


			for (int i = 0; i < riskFileName.length; i++) {
				
				Vulnerability vulnerability = dciFunctions.getVulnerability(suiteData, restClient, additionalParamsUI);
				Logger.info("Vulnerability:" + riskType[i] + "::Count:" + vulnerability.getResults().getVulnerabilityCount(riskType[i]));
				VulnerabilityTypes vulnerabilityTypes = dciFunctions.getVulnerabilityTypes(suiteData, restClient, appType,additionalParamsAPI);
				Logger.info("Vulnerability:" + riskType[i] + "::Count:" + vulnerabilityTypes.getObjects().getVulnerabilityCount(riskType[i]));
				int countBeforeUI= vulnerability.getResults().getVulnerabilityCount(riskType[i]);
				int countBeforeAPI= vulnerabilityTypes.getObjects().getVulnerabilityCount(riskType[i]);


				Map<String, String> fileInfo = new HashMap<String, String>();
				fileInfo.put("fileId",fileId[i]);
				fileInfo.put("fileEtag",fileEtag[i]);

				result[i] = new Object[] { new TestParameters("Filter count check"+riskFileName[i]+" for deletion of exposed internal risks types", 
						"Delete a internal shared risks types file:"+riskFileName[i]+ ". Then verify risk logs are getting generated within the SLA provided and count is getting decreased ",
						riskFileName[i], saasType, riskType[i], Arrays.asList(risks[i].split(",")), countBeforeUI, countBeforeAPI, additionalParamsUI, additionalParamsAPI)};
			}

			dciFunctions.waitForMinutes(1);
			UserAccount account = dciFunctions.getUserAccount(suiteData);
			UniversalApi universalApi = dciFunctions.getUniversalApi(suiteData, account);
			if(SaasType.getSaasType(suiteData.getSaasApp()).equals(SaasType.Office365)){
				Map<String,String> folderInfo = new HashMap<String,String>();
				folderInfo.put("folderName", folderResource.getName());
				folderInfo.put("folderId", folderResource.getId());
				folderInfo.put("folderEtag", folderResource.getETag());
				folderInfo.put("folderType", folderResource.getType()); 
				
				dciFunctions.deleteFolder(universalApi, suiteData, folderInfo);
			}else if(SaasType.getSaasType(suiteData.getSaasApp()).equals(SaasType.Dropbox)){
				for (int i = 0; i < riskFileName.length; i++) {
					Map<String, String> fileInfo = new HashMap<String, String>();
					fileInfo.put("fileName",fileName[i]);
					fileInfo.put("fileId",fileId[i]);
					fileInfo.put("fileEtag",fileEtag[i]);
					dciFunctions.deleteFile(universalApi, suiteData, fileInfo);
				}
			}else{
				dciFunctions.deleteFolder(universalApi, suiteData, folderInfo);
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}	

		dciFunctions.waitForMinutes(5);

		return result;
	}

	@DataProvider(name = "dataUploadContentTypeExposedInternal")
	public Object[][] dataUploadContentTypeExposedInternal() {
		dciFunctions = new DCIFunctions();
		String saasType = SaasType.getSaasFilterType(suiteData.getSaasApp());
		String appType = SaasType.getAppType(suiteData.getSaasApp(), suiteData.getEnvironmentName());
		String vlType = SaasType.getVlType(suiteData.getSaasApp());
		String objectType = SaasType.getObjectType(suiteData.getSaasApp());
		Map<String, String>  additionalParamsAPI = dciFunctions.generateHeadersAPI("true","true");
		Map<String, String>  additionalParamsUI = dciFunctions.generateHeadersUI(vlType, "true", objectType);

		UserAccount account = dciFunctions.getUserAccount(suiteData);
		UniversalApi universalApi = dciFunctions.getUniversalApi(suiteData, account);
		if(SaasType.getSaasType(suiteData.getSaasApp()).equals(SaasType.Office365)){
			folderResource = dciFunctions.createFolderInOneDrive(universalApi, suiteData,
					DCIConstants.DCI_FOLDER+uniqueId);
		}else if(SaasType.getSaasType(suiteData.getSaasApp()).equals(SaasType.Dropbox)){
			Logger.info("Folder in dropbox is not created");
			folderInfo.put("folderName", File.separator+dciFunctions.getDropboxInternalFolderName(suiteData));
			folderInfo.put("folderId", File.separator+dciFunctions.getDropboxInternalFolderName(suiteData));
			folderInfo.put("folderEtag", "");
			folderInfo.put("folderType", ""); 
		}else{
			folderInfo = dciFunctions.createFolder(universalApi, suiteData, 
					DCIConstants.DCI_FOLDER+uniqueId);
		}

		fileName = new String[contentFileName.length];
		fileId = new String[contentFileName.length];
		fileEtag = new String[contentFileName.length];
		fileResource = new ItemResource[contentFileName.length];

		int[] countBeforeUI = new int[contentFileName.length];
		int[] countBeforeAPI = new int[contentFileName.length];
		
		Object[][] result = new Object[contentFileName.length][];

		try {
			contentFileName = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_CONTENT_TYPES_PATH, contentFileName);

			for (int i = 0; i < contentFileName.length; i++) {
				
				ContentType cTypes = dciFunctions.getContentType(suiteData, restClient, additionalParamsUI);
				Logger.info("ContentType:" + contentType[i] + "::Count:" + cTypes.getContentTypeCount(contentType[i]));
				ContentTypes cType = dciFunctions.getContentTypes(suiteData, restClient, appType,additionalParamsAPI);
				Logger.info("ContentType:" + contentType[i] + "::Count:" + cType.getContentTypeCount(contentType[i]));
				countBeforeUI[i]= cTypes.getContentTypeCount(contentType[i]);
				countBeforeAPI[i]= cType.getContentTypeCount(contentType[i]);
			}	
			for (int i = 0; i < contentFileName.length; i++) {
				if(SaasType.getSaasType(suiteData.getSaasApp()).equals(SaasType.Office365)){
					fileResource[i] = dciFunctions.uploadFileInOneDrive(universalApi, suiteData, 
							folderResource.getId(), contentFileName[i]);
					
					dciFunctions.shareFileOnOneDrive(fileResource[i], universalApi, "Internal");
				}else if(SaasType.getSaasType(suiteData.getSaasApp()).equals(SaasType.Dropbox)){
					Map<String, String> fileInfo = new HashMap<String, String>();
					fileInfo.put("fileName", folderInfo.get("folderName")+File.separator+contentFileName[i]);
					fileInfo.put("fileId", folderInfo.get("folderName")+File.separator+contentFileName[i]);
					fileInfo.put("fileEtag", "");
					 
					fileInfo = dciFunctions.shareFile(suiteData, universalApi, folderInfo, fileInfo, "Internal");
					fileName[i] = fileInfo.get("fileName");
					fileId[i] = fileInfo.get("fileId");
					fileEtag[i] = fileInfo.get("fileEtag");
				}else{
					Map<String, String> fileInfo = dciFunctions.uploadFile(universalApi,suiteData, 
							folderInfo.get("folderId"), contentFileName[i]);
					fileId[i] = fileInfo.get("fileId");
					fileEtag[i] = fileInfo.get("fileEtag");

					fileInfo = dciFunctions.shareFile(suiteData, universalApi, folderInfo, fileInfo, "Internal");
					fileId[i] = fileInfo.get("fileId");
					fileEtag[i] = fileInfo.get("fileEtag");

				}

				result[i] = new Object[] { new TestParameters("Filter count check"+contentFileName[i]+" for upload of exposed internal content types", 
						"Upload a exposed internal content types file:"+contentFileName[i]+ ". Then verify risk logs are getting generated within the SLA provided and count is getting increased ",
						contentFileName[i], saasType, contentType[i], Arrays.asList(contentTypes[i].split(",")), countBeforeUI[i], countBeforeAPI[i], additionalParamsUI, additionalParamsAPI)};
			}

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}	

		dciFunctions.waitForMinutes(5);

		mainCounter = 0;testCounter = DCIConstants.DCI_COUNTER_MEDIUM_LL;
		
		return result;
	}

	@DataProvider(name = "dataDeleteContentTypeExposedInternal")
	public Object[][] dataDeleteContentTypeExposedInternal() {


		dciFunctions = new DCIFunctions();
		String saasType = SaasType.getSaasFilterType(suiteData.getSaasApp());
		String appType = SaasType.getAppType(suiteData.getSaasApp(), suiteData.getEnvironmentName());
		String vlType = SaasType.getVlType(suiteData.getSaasApp());
		String objectType = SaasType.getObjectType(suiteData.getSaasApp());
		Map<String, String>  additionalParamsAPI = dciFunctions.generateHeadersAPI("true","true");
		Map<String, String>  additionalParamsUI = dciFunctions.generateHeadersUI(vlType, "true", objectType);

		Object[][] result = new Object[contentFileName.length][];

		try {


			for (int i = 0; i < contentFileName.length; i++) {
				
				ContentType cTypes = dciFunctions.getContentType(suiteData, restClient, additionalParamsUI);
				Logger.info("ContentType:" + contentType[i] + "::Count:" + cTypes.getContentTypeCount(contentType[i]));
				ContentTypes cType = dciFunctions.getContentTypes(suiteData, restClient, appType,additionalParamsAPI);
				Logger.info("ContentType:" + contentType[i] + "::Count:" + cType.getContentTypeCount(contentType[i]));
				int countBeforeUI= cTypes.getContentTypeCount(contentType[i]);
				int countBeforeAPI= cType.getContentTypeCount(contentType[i]);

				Map<String, String> fileInfo = new HashMap<String, String>();
				fileInfo.put("fileId",fileId[i]);
				fileInfo.put("fileEtag",fileEtag[i]);

				result[i] = new Object[] { new TestParameters("Filter count check"+contentFileName[i]+" for deletion of exposed internal content types", 
						"Delete a expose internal content types file:"+contentFileName[i]+ ". Then verify risk logs are getting generated within the SLA provided and count is getting decreased ",
						contentFileName[i], saasType, contentType[i], Arrays.asList(contentTypes[i].split(",")), countBeforeUI, countBeforeAPI, additionalParamsUI, additionalParamsAPI)};
			}

			dciFunctions.waitForMinutes(1);
			UserAccount account = dciFunctions.getUserAccount(suiteData);
			UniversalApi universalApi = dciFunctions.getUniversalApi(suiteData, account);
			if(SaasType.getSaasType(suiteData.getSaasApp()).equals(SaasType.Office365)){
				Map<String,String> folderInfo = new HashMap<String,String>();
				folderInfo.put("folderName", folderResource.getName());
				folderInfo.put("folderId", folderResource.getId());
				folderInfo.put("folderEtag", folderResource.getETag());
				folderInfo.put("folderType", folderResource.getType()); 
				
				dciFunctions.deleteFolder(universalApi, suiteData, folderInfo);
			}else if(SaasType.getSaasType(suiteData.getSaasApp()).equals(SaasType.Dropbox)){
				for (int i = 0; i < contentFileName.length; i++) {
					Map<String, String> fileInfo = new HashMap<String, String>();
					fileInfo.put("fileName",fileName[i]);
					fileInfo.put("fileId",fileId[i]);
					fileInfo.put("fileEtag",fileEtag[i]);
					dciFunctions.deleteFile(universalApi, suiteData, fileInfo);
				}
			}else{
				dciFunctions.deleteFolder(universalApi, suiteData, folderInfo);
			}

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}	

		dciFunctions.waitForMinutes(5);

		return result;
	}

	@DataProvider(name = "dataUploadRiskTypeExposedExternal")
	public Object[][] dataUploadRiskTypeExposedExternal() {

		dciFunctions = new DCIFunctions();
		String saasType = SaasType.getSaasFilterType(suiteData.getSaasApp());
		String appType = SaasType.getAppType(suiteData.getSaasApp(), suiteData.getEnvironmentName());
		String vlType = SaasType.getVlType(suiteData.getSaasApp());
		String objectType = SaasType.getObjectType(suiteData.getSaasApp());
		Map<String, String>  additionalParamsAPI = dciFunctions.generateHeadersAPI("true",true);
		Map<String, String>  additionalParamsUI = dciFunctions.generateHeadersUI(vlType, true, objectType);

		UserAccount account = dciFunctions.getUserAccount(suiteData);
		UniversalApi universalApi = dciFunctions.getUniversalApi(suiteData, account);
		if(SaasType.getSaasType(suiteData.getSaasApp()).equals(SaasType.Office365)){
			folderResource = dciFunctions.createFolderInOneDrive(universalApi, suiteData,
					DCIConstants.DCI_FOLDER+uniqueId);
			folderInfo.put("folderName", folderResource.getName());
			folderInfo.put("folderId", folderResource.getId());
			folderInfo.put("folderEtag", folderResource.getETag());
			folderInfo.put("folderType", folderResource.getType());
		}else{
			folderInfo = dciFunctions.createFolder(universalApi, suiteData, 
					DCIConstants.DCI_FOLDER+uniqueId);
		}


		fileId = new String[riskFileName.length];
		fileEtag = new String[riskFileName.length];
		fileResource = new ItemResource[riskFileName.length];

		int[] countBeforeUI = new int[riskFileName.length];
		int[] countBeforeAPI = new int[riskFileName.length];
		
		Object[][] result = new Object[riskFileName.length][];

		account = dciFunctions.getUserAccount(suiteData);
		universalApi = dciFunctions.getUniversalApi(suiteData, account);
		
		try {
			riskFileName = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_RISK_TYPES_PATH, riskFileName);

			Map<String, String> fileInfo = new HashMap<String, String>();

			for (int i = 0; i < riskFileName.length; i++) {
				
				Vulnerability vulnerability = dciFunctions.getVulnerability(suiteData, restClient, additionalParamsUI);
				Logger.info("Vulnerability:" + riskType[i] + "::Count:" + vulnerability.getResults().getVulnerabilityCount(riskType[i]));
				VulnerabilityTypes vulnerabilityTypes = dciFunctions.getVulnerabilityTypes(suiteData, restClient, appType,additionalParamsAPI);
				Logger.info("Vulnerability:" + riskType[i] + "::Count:" + vulnerabilityTypes.getObjects().getVulnerabilityCount(riskType[i]));
				countBeforeUI[i]= vulnerability.getResults().getVulnerabilityCount(riskType[i]);
				countBeforeAPI[i]= vulnerabilityTypes.getObjects().getVulnerabilityCount(riskType[i]);
			}
			for (int i = 0; i < riskFileName.length; i++) {
				
				
				if(SaasType.getSaasType(suiteData.getSaasApp()).equals(SaasType.Office365)){
					fileResource[i] = dciFunctions.uploadFileInOneDrive(universalApi, suiteData, 
							folderResource.getId(), riskFileName[i]);
					dciFunctions.shareFileOnOneDrive(fileResource[i], universalApi, "External");
				}else{
					fileInfo = dciFunctions.uploadFile(universalApi,suiteData, folderInfo.get("folderId"), riskFileName[i]);
					fileId[i] = fileInfo.get("fileId");
					fileEtag[i] = fileInfo.get("fileEtag");
					
					if(SaasType.getSaasType(suiteData.getSaasApp()).equals(SaasType.GoogleDrive)){
						fileInfo = dciFunctions.shareFile(suiteData, universalApi, folderInfo, fileInfo, "External");
						fileId[i] = fileInfo.get("fileId");
						fileEtag[i] = fileInfo.get("fileEtag");
					}
					
				}
				
				result[i] = new Object[] { new TestParameters("Filter count check"+riskFileName[i]+" for exposed external risks types", 
						"Upload and share external risks types file:"+riskFileName[i]+ ". Then verify risk logs are getting generated within the SLA provided and count is getting increased ",
						riskFileName[i], saasType, riskType[i], Arrays.asList(risks[i].split(",")), countBeforeUI[i], countBeforeAPI[i], additionalParamsUI, additionalParamsAPI)};
			}

			dciFunctions.waitForMinutes(1);
			account = dciFunctions.getUserAccount(suiteData);
			universalApi = dciFunctions.getUniversalApi(suiteData, account);
			
			if(SaasType.getSaasType(suiteData.getSaasApp()).equals(SaasType.Office365)||
					SaasType.getSaasType(suiteData.getSaasApp()).equals(SaasType.GoogleDrive)){
			}else{
				folderInfo = dciFunctions.shareFile(suiteData, universalApi, folderInfo, fileInfo, "External");
			}

			
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}	

		dciFunctions.waitForMinutes(5);

		mainCounter = 0;testCounter = DCIConstants.DCI_COUNTER_MEDIUM_LL;
		
		return result;
	}

	@DataProvider(name = "dataDeleteRiskTypeExposedExternal")
	public Object[][] dataDeleteRiskTypeExposedExternal() {

		dciFunctions = new DCIFunctions();
		String saasType = SaasType.getSaasFilterType(suiteData.getSaasApp());
		String appType = SaasType.getAppType(suiteData.getSaasApp(), suiteData.getEnvironmentName());
		String vlType = SaasType.getVlType(suiteData.getSaasApp());
		String objectType = SaasType.getObjectType(suiteData.getSaasApp());
		Map<String, String>  additionalParamsAPI = dciFunctions.generateHeadersAPI("true",true);
		Map<String, String>  additionalParamsUI = dciFunctions.generateHeadersUI(vlType, true, objectType);

		Object[][] result = new Object[riskFileName.length][];

		try {


			for (int i = 0; i < riskFileName.length; i++) {
				
				Vulnerability vulnerability = dciFunctions.getVulnerability(suiteData, restClient, additionalParamsUI);
				Logger.info("Vulnerability:" + riskType[i] + "::Count:" + vulnerability.getResults().getVulnerabilityCount(riskType[i]));
				VulnerabilityTypes vulnerabilityTypes = dciFunctions.getVulnerabilityTypes(suiteData, restClient, appType,additionalParamsAPI);
				Logger.info("Vulnerability:" + riskType[i] + "::Count:" + vulnerabilityTypes.getObjects().getVulnerabilityCount(riskType[i]));
				int countBeforeUI= vulnerability.getResults().getVulnerabilityCount(riskType[i]);
				int countBeforeAPI= vulnerabilityTypes.getObjects().getVulnerabilityCount(riskType[i]);


				Map<String, String> fileInfo = new HashMap<String, String>();
				fileInfo.put("fileId",fileId[i]);
				fileInfo.put("fileEtag",fileEtag[i]);

				result[i] = new Object[] { new TestParameters("Filter count check"+riskFileName[i]+" for deletion of exposed external risks types", 
						"Delete a external shared risks types file:"+riskFileName[i]+ ". Then verify risk logs are getting generated within the SLA provided and count is getting decreased ",
						riskFileName[i], saasType, riskType[i], Arrays.asList(risks[i].split(",")), countBeforeUI, countBeforeAPI, additionalParamsUI, additionalParamsAPI)};
			}

			dciFunctions.waitForMinutes(1);
			UserAccount account = dciFunctions.getUserAccount(suiteData);
			UniversalApi universalApi = dciFunctions.getUniversalApi(suiteData, account);
			if(SaasType.getSaasType(suiteData.getSaasApp()).equals(SaasType.Office365)){
				Map<String,String> folderInfo = new HashMap<String,String>();
				folderInfo.put("folderName", folderResource.getName());
				folderInfo.put("folderId", folderResource.getId());
				folderInfo.put("folderEtag", folderResource.getETag());
				folderInfo.put("folderType", folderResource.getType()); 
				
				dciFunctions.deleteFolder(universalApi, suiteData, folderInfo);
			}else{
				dciFunctions.deleteFolder(universalApi, suiteData, folderInfo);
			}

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}	

		dciFunctions.waitForMinutes(5);

		return result;
	}

	@DataProvider(name = "dataUploadContentTypeExposedExternal")
	public Object[][] dataUploadContentTypeExposedExternal() {
		dciFunctions = new DCIFunctions();
		String saasType = SaasType.getSaasFilterType(suiteData.getSaasApp());
		String appType = SaasType.getAppType(suiteData.getSaasApp(), suiteData.getEnvironmentName());
		String vlType = SaasType.getVlType(suiteData.getSaasApp());
		String objectType = SaasType.getObjectType(suiteData.getSaasApp());
		Map<String, String>  additionalParamsAPI = dciFunctions.generateHeadersAPI("true",true);
		Map<String, String>  additionalParamsUI = dciFunctions.generateHeadersUI(vlType, true, objectType);
		
		UserAccount account = dciFunctions.getUserAccount(suiteData);
		UniversalApi universalApi = dciFunctions.getUniversalApi(suiteData, account);
		if(SaasType.getSaasType(suiteData.getSaasApp()).equals(SaasType.Office365)){
			folderResource = dciFunctions.createFolderInOneDrive(universalApi, suiteData,
					DCIConstants.DCI_FOLDER+uniqueId);
			folderInfo.put("folderName", folderResource.getName());
			folderInfo.put("folderId", folderResource.getId());
			folderInfo.put("folderEtag", folderResource.getETag());
			folderInfo.put("folderType", folderResource.getType());
		}else{
			folderInfo = dciFunctions.createFolder(universalApi, suiteData, 
					DCIConstants.DCI_FOLDER+uniqueId);
		}

		fileId = new String[contentFileName.length];
		fileEtag = new String[contentFileName.length];
		fileResource = new ItemResource[contentFileName.length];

		int[] countBeforeUI = new int[contentFileName.length];
		int[] countBeforeAPI = new int[contentFileName.length];
		
		Object[][] result = new Object[contentFileName.length][];

		account = dciFunctions.getUserAccount(suiteData);
		universalApi = dciFunctions.getUniversalApi(suiteData, account);
		
		try {
			contentFileName = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_CONTENT_TYPES_PATH, contentFileName);

			Map<String, String> fileInfo = new HashMap<String, String>();

			for (int i = 0; i < contentFileName.length; i++) {
				
				ContentType cTypes = dciFunctions.getContentType(suiteData, restClient, additionalParamsUI);
				Logger.info("ContentType:" + contentType[i] + "::Count:" + cTypes.getContentTypeCount(contentType[i]));
				ContentTypes cType = dciFunctions.getContentTypes(suiteData, restClient, appType,additionalParamsAPI);
				Logger.info("ContentType:" + contentType[i] + "::Count:" + cType.getContentTypeCount(contentType[i]));
				countBeforeUI[i]= cTypes.getContentTypeCount(contentType[i]);
				countBeforeAPI[i]= cType.getContentTypeCount(contentType[i]);

			}
			for (int i = 0; i < contentFileName.length; i++) {
				if(SaasType.getSaasType(suiteData.getSaasApp()).equals(SaasType.Office365)){
					fileResource[i] = dciFunctions.uploadFileInOneDrive(universalApi, suiteData, 
							folderResource.getId(), contentFileName[i]);
					dciFunctions.shareFileOnOneDrive(fileResource[i], universalApi, "External");
				}else{
					fileInfo = dciFunctions.uploadFile(universalApi,suiteData, folderInfo.get("folderId"), contentFileName[i]);
					fileId[i] = fileInfo.get("fileId");
					fileEtag[i] = fileInfo.get("fileEtag");
					if(SaasType.getSaasType(suiteData.getSaasApp()).equals(SaasType.GoogleDrive)){
						fileInfo = dciFunctions.shareFile(suiteData, universalApi, folderInfo, fileInfo, "External");
						fileId[i] = fileInfo.get("fileId");
						fileEtag[i] = fileInfo.get("fileEtag");
					}
				}

				result[i] = new Object[] { new TestParameters("Filter count check"+contentFileName[i]+" for upload of exposed external content types", 
						"Upload a exposed external content types file:"+contentFileName[i]+ ". Then verify risk logs are getting generated within the SLA provided and count is getting increased ",
						contentFileName[i], saasType, contentType[i], Arrays.asList(contentTypes[i].split(",")), countBeforeUI[i], countBeforeAPI[i], additionalParamsUI, additionalParamsAPI)};
			}

			dciFunctions.waitForMinutes(2);
			account = dciFunctions.getUserAccount(suiteData);
			universalApi = dciFunctions.getUniversalApi(suiteData, account);
			if(SaasType.getSaasType(suiteData.getSaasApp()).equals(SaasType.Office365)||
					SaasType.getSaasType(suiteData.getSaasApp()).equals(SaasType.GoogleDrive)){
			}else{
				folderInfo = dciFunctions.shareFile(suiteData, universalApi, folderInfo, fileInfo, "External");
			}


		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}	

		dciFunctions.waitForMinutes(5);
		
		mainCounter = 0;testCounter = DCIConstants.DCI_COUNTER_MEDIUM_LL;

		return result;
	}

	@DataProvider(name = "dataDeleteContentTypeExposedExternal")
	public Object[][] dataDeleteContentTypeExposedExternal() {

		dciFunctions = new DCIFunctions();	
		String saasType = SaasType.getSaasFilterType(suiteData.getSaasApp());
		String appType = SaasType.getAppType(suiteData.getSaasApp(), suiteData.getEnvironmentName());
		String vlType = SaasType.getVlType(suiteData.getSaasApp());
		String objectType = SaasType.getObjectType(suiteData.getSaasApp());
		Map<String, String>  additionalParamsAPI = dciFunctions.generateHeadersAPI("true","true");
		Map<String, String>  additionalParamsUI = dciFunctions.generateHeadersUI(vlType, "true", objectType);

		Object[][] result = new Object[contentFileName.length][];

		try {

			for (int i = 0; i < contentFileName.length; i++) {
				
				ContentType cTypes = dciFunctions.getContentType(suiteData, restClient, additionalParamsUI);
				Logger.info("ContentType:" + contentType[i] + "::Count:" + cTypes.getContentTypeCount(contentType[i]));
				ContentTypes cType = dciFunctions.getContentTypes(suiteData, restClient, appType,additionalParamsAPI);
				Logger.info("ContentType:" + contentType[i] + "::Count:" + cType.getContentTypeCount(contentType[i]));
				int countBeforeUI= cTypes.getContentTypeCount(contentType[i]);
				int countBeforeAPI= cType.getContentTypeCount(contentType[i]);

				Map<String, String> fileInfo = new HashMap<String, String>();
				fileInfo.put("fileId",fileId[i]);
				fileInfo.put("fileEtag",fileEtag[i]);

				result[i] = new Object[] { new TestParameters("Filter count check"+contentFileName[i]+" for deletion of exposed external content types", 
						"Delete a expose external content types file:"+contentFileName[i]+ ". Then verify risk logs are getting generated within the SLA provided and count is getting decreased ",
						contentFileName[i], saasType, contentType[i], Arrays.asList(contentTypes[i].split(",")), countBeforeUI, countBeforeAPI, additionalParamsUI, additionalParamsAPI)};
			}

			dciFunctions.waitForMinutes(1);
			UserAccount account = dciFunctions.getUserAccount(suiteData);
			UniversalApi universalApi = dciFunctions.getUniversalApi(suiteData, account);
			if(SaasType.getSaasType(suiteData.getSaasApp()).equals(SaasType.Office365)){
				Map<String,String> folderInfo = new HashMap<String,String>();
				folderInfo.put("folderName", folderResource.getName());
				folderInfo.put("folderId", folderResource.getId());
				folderInfo.put("folderEtag", folderResource.getETag());
				folderInfo.put("folderType", folderResource.getType()); 
				
				dciFunctions.deleteFolder(universalApi, suiteData, folderInfo);
			}else{
				dciFunctions.deleteFolder(universalApi, suiteData, folderInfo);
			}

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}	

		dciFunctions.waitForMinutes(5);

		return result;
	}


	@DataProvider(name = "dataUploadRiskTypeCIQUnexposed")
	public Object[][] dataUploadRiskTypeCIQUnexposed() {

		dciFunctions = new DCIFunctions();
		String saasType = SaasType.getSaasFilterType(suiteData.getSaasApp());
		String appType = SaasType.getAppType(suiteData.getSaasApp(), suiteData.getEnvironmentName());
		String vlType = SaasType.getVlType(suiteData.getSaasApp());
		String objectType = SaasType.getObjectType(suiteData.getSaasApp());
		Map<String, String>  additionalParamsAPI = dciFunctions.generateHeadersAPI("false","true");
		Map<String, String>  additionalParamsUI = dciFunctions.generateHeadersUI(vlType, "true", "risky_docs", objectType);

		UserAccount account = dciFunctions.getUserAccount(suiteData);
		UniversalApi universalApi = dciFunctions.getUniversalApi(suiteData, account);
		folderInfo = dciFunctions.createFolder(universalApi, suiteData, 
				DCIConstants.DCI_FOLDER+uniqueId);

		fileId = new String[ciqRiskFileName.length];
		fileEtag = new String[ciqRiskFileName.length];

		try {
			String ciqDictionaries = dciFunctions.predefinedDictionariesCIQProfileText(ciqRiskType[0]);
			String ciqDictProfileNames = dciFunctions.predefinedDictionariesCIQProfileName(ciqRiskType[0]); 
			String ciqDictProfileDescription = dciFunctions.predefinedDictionariesCIQProfileDescription(ciqRiskType[0]);

			String ciqTerms = dciFunctions.predefinedTermsCIQProfileText(ciqRiskType[1]);
			String ciqTermsProfileNames = dciFunctions.predefinedTermsCIQProfileName(ciqRiskType[1]); 
			String ciqTermsProfileDescription = dciFunctions.predefinedTermsCIQProfileDescription(ciqRiskType[1]);

			dciFunctions.createCIQPredefinedDictionaries(restClient, suiteData, ciqDictionaries, ciqDictProfileNames,
					ciqDictProfileDescription, "high", 1, true, 1, false);
			dciFunctions.createCIQPredefinedTerms(restClient, suiteData, ciqTerms, ciqTermsProfileNames,
					ciqTermsProfileDescription, "high", 1, true, 1, false);
			
		} catch (Exception ex) {
			Logger.info("Issue with Create Content Iq Profiles" + ex.getLocalizedMessage());
		}

		int[] countBeforeUI= new int[ciqRiskFileName.length];
		int[] countBeforeAPI= new int[ciqRiskFileName.length];

		Object[][] result = new Object[ciqRiskFileName.length][];

		try {
			ciqRiskFileName = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_CIQ_EXPOSURE_PATH, ciqRiskFileName);

			for (int i = 0; i < ciqRiskFileName.length; i++) {
				
				Vulnerability vulnerability = dciFunctions.getVulnerability(suiteData, restClient, additionalParamsUI);
				Logger.info("Vulnerability:" + ciqRiskType[i] + "::Count:" + vulnerability.getResults().getCIQCount(ciqRiskType[i]));
				VulnerabilityTypes vulnerabilityTypes = dciFunctions.getVulnerabilityTypes(suiteData, restClient, appType,additionalParamsAPI);
				Logger.info("Vulnerability:" + ciqRiskType[i] + "::Count:" + vulnerabilityTypes.getObjects().getCIQCount(ciqRiskType[i]));
				countBeforeUI[i]= vulnerability.getResults().getCIQCount(ciqRiskType[i]);
				countBeforeAPI[i]= vulnerabilityTypes.getObjects().getCIQCount(ciqRiskType[i]);

			}
			for (int i = 0; i < ciqRiskFileName.length; i++) {	
				Map<String, String> fileInfo = dciFunctions.uploadFile(universalApi,suiteData, folderInfo.get("folderId"), ciqRiskFileName[i]);
				fileId[i] = fileInfo.get("fileId");
				fileEtag[i] = fileInfo.get("fileEtag");
				

				result[i] = new Object[] { new TestParameters("Filter count check"+ciqRiskFileName[i]+" for upload of ciqRisk types", 
						"Upload a ciqRisks types file:"+ciqRiskFileName[i]+ ". Then verify ciqRisk logs are getting generated within the SLA provided and count is getting increased ",
						ciqRiskFileName[i], saasType, ciqRiskType[i], Arrays.asList(ciqRisks[i].split(",")), countBeforeUI[i], countBeforeAPI[i], additionalParamsUI, additionalParamsAPI)};
			}

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}	
		
		mainCounter = 0;testCounter = DCIConstants.DCI_COUNTER_MEDIUM_LL;

		return result;
	}

	@DataProvider(name = "dataDeleteRiskTypeCIQUnexposed")
	public Object[][] dataDeleteRiskTypeCIQUnexposed() {

		dciFunctions = new DCIFunctions();
		String saasType = SaasType.getSaasFilterType(suiteData.getSaasApp());
		String appType = SaasType.getAppType(suiteData.getSaasApp(), suiteData.getEnvironmentName());
		String vlType = SaasType.getVlType(suiteData.getSaasApp());
		String objectType = SaasType.getObjectType(suiteData.getSaasApp());
		Map<String, String>  additionalParamsAPI = dciFunctions.generateHeadersAPI("false","true");
		Map<String, String>  additionalParamsUI = dciFunctions.generateHeadersUI(vlType, "true", "risky_docs", objectType);

		Object[][] result = new Object[ciqRiskFileName.length][];

		try {

			for (int i = 0; i < ciqRiskFileName.length; i++) {
				
				Vulnerability vulnerability = dciFunctions.getVulnerability(suiteData, restClient, additionalParamsUI);
				Logger.info("Vulnerability:" + ciqRiskType[i] + "::Count:" + vulnerability.getResults().getCIQCount(ciqRiskType[i]));
				VulnerabilityTypes vulnerabilityTypes = dciFunctions.getVulnerabilityTypes(suiteData, restClient, appType,additionalParamsAPI);
				Logger.info("Vulnerability:" + ciqRiskType[i] + "::Count:" + vulnerabilityTypes.getObjects().getCIQCount(ciqRiskType[i]));
				int countBeforeUI= vulnerability.getResults().getCIQCount(ciqRiskType[i]);
				int countBeforeAPI= vulnerabilityTypes.getObjects().getCIQCount(ciqRiskType[i]);


				Map<String, String> fileInfo = new HashMap<String, String>();
				fileInfo.put("fileId",fileId[i]);
				fileInfo.put("fileEtag",fileEtag[i]);

				result[i] = new Object[] { new TestParameters("Filter count check"+ciqRiskFileName[i]+" for deletion of ciqRisks types", 
						"Delete a ciqRisks types file:"+ciqRiskFileName[i]+ ". Then verify ciqRisk logs are getting generated within the SLA provided and count is getting decreased ",
						ciqRiskFileName[i], saasType, ciqRiskType[i], Arrays.asList(ciqRisks[i].split(",")), countBeforeUI, countBeforeAPI, additionalParamsUI, additionalParamsAPI)};
			}

			dciFunctions.waitForMinutes(1);
			UserAccount account = dciFunctions.getUserAccount(suiteData);
			UniversalApi universalApi = dciFunctions.getUniversalApi(suiteData, account);
			dciFunctions.deleteFolder(universalApi, suiteData, folderInfo);

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}	

		dciFunctions.waitForMinutes(5);

		return result;
	}

	@DataProvider(name = "dataUploadRiskTypeCIQExposedPublic")
	public Object[][] dataUploadRiskTypeCIQExposedPublic() {

		dciFunctions = new DCIFunctions();
		String saasType = SaasType.getSaasFilterType(suiteData.getSaasApp());
		String appType = SaasType.getAppType(suiteData.getSaasApp(), suiteData.getEnvironmentName());
		String vlType = SaasType.getVlType(suiteData.getSaasApp());
		String objectType = SaasType.getObjectType(suiteData.getSaasApp());
		Map<String, String>  additionalParamsAPI = dciFunctions.generateHeadersAPI("true","true");
		Map<String, String>  additionalParamsUI = dciFunctions.generateHeadersUI(vlType, "true", objectType);

		UserAccount account = dciFunctions.getUserAccount(suiteData);
		UniversalApi universalApi = dciFunctions.getUniversalApi(suiteData, account);
		if(SaasType.getSaasType(suiteData.getSaasApp()).equals(SaasType.Office365)){
			folderResource = dciFunctions.createFolderInOneDrive(universalApi, suiteData,
					DCIConstants.DCI_FOLDER+uniqueId);
		}else{
			folderInfo = dciFunctions.createFolder(universalApi, suiteData, 
					DCIConstants.DCI_FOLDER+uniqueId);
		}

		try {
			String ciqDictionaries = dciFunctions.predefinedDictionariesCIQProfileText(ciqRiskType[0]);
			String ciqDictProfileNames = dciFunctions.predefinedDictionariesCIQProfileName(ciqRiskType[0]); 
			String ciqDictProfileDescription = dciFunctions.predefinedDictionariesCIQProfileDescription(ciqRiskType[0]);

			String ciqTerms = dciFunctions.predefinedTermsCIQProfileText(ciqRiskType[1]);
			String ciqTermsProfileNames = dciFunctions.predefinedTermsCIQProfileName(ciqRiskType[1]); 
			String ciqTermsProfileDescription = dciFunctions.predefinedTermsCIQProfileDescription(ciqRiskType[1]);

			dciFunctions.createCIQPredefinedDictionaries(restClient, suiteData, ciqDictionaries, ciqDictProfileNames,
					ciqDictProfileDescription, "high", 1, true, 1, false);
			dciFunctions.createCIQPredefinedTerms(restClient, suiteData, ciqTerms, ciqTermsProfileNames,
					ciqTermsProfileDescription, "high", 1, true, 1, false);
			
		} catch (Exception ex) {
			Logger.info("Issue with Create Content Iq Profiles" + ex.getLocalizedMessage());
		}

		fileId = new String[ciqRiskFileName.length];
		fileEtag = new String[ciqRiskFileName.length];
		fileResource = new ItemResource[ciqRiskFileName.length];

		int[] countBeforeUI= new int[ciqRiskFileName.length];
		int[] countBeforeAPI= new int[ciqRiskFileName.length];
		
		Object[][] result = new Object[ciqRiskFileName.length][];

		try {
			ciqRiskFileName = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_CIQ_EXPOSURE_PATH, ciqRiskFileName);

			for (int i = 0; i < ciqRiskFileName.length; i++) {
				
				Vulnerability vulnerability = dciFunctions.getVulnerability(suiteData, restClient, additionalParamsUI);
				Logger.info("Vulnerability:" + ciqRiskType[i] + "::Count:" + vulnerability.getResults().getCIQCount(ciqRiskType[i]));
				VulnerabilityTypes vulnerabilityTypes = dciFunctions.getVulnerabilityTypes(suiteData, restClient, appType,additionalParamsAPI);
				Logger.info("Vulnerability:" + ciqRiskType[i] + "::Count:" + vulnerabilityTypes.getObjects().getCIQCount(ciqRiskType[i]));
				countBeforeUI[i]= vulnerability.getResults().getCIQCount(ciqRiskType[i]);
				countBeforeAPI[i]= vulnerabilityTypes.getObjects().getCIQCount(ciqRiskType[i]);
			}for (int i = 0; i < ciqRiskFileName.length; i++) {
				if(SaasType.getSaasType(suiteData.getSaasApp()).equals(SaasType.Office365)){
					fileResource[i] = dciFunctions.uploadFileInOneDrive(universalApi, suiteData, 
							folderResource.getId(), ciqRiskFileName[i]);
					
					dciFunctions.shareFileOnOneDrive(fileResource[i], universalApi, "Public");
				}else{
					Map<String, String> fileInfo = dciFunctions.uploadFile(universalApi,suiteData, 
							folderInfo.get("folderId"), ciqRiskFileName[i]);
					fileId[i] = fileInfo.get("fileId");
					fileEtag[i] = fileInfo.get("fileEtag");

					fileInfo = dciFunctions.shareFile(suiteData, universalApi, folderInfo, fileInfo, "Public");
					fileId[i] = fileInfo.get("fileId");
					fileEtag[i] = fileInfo.get("fileEtag");

				}

				result[i] = new Object[] { new TestParameters("Filter count check"+ciqRiskFileName[i]+" for exposed public ciqRisks types", 
						"Upload and share publicly ciqRisks types file:"+ciqRiskFileName[i]+ ". Then verify ciqRisk logs are getting generated within the SLA provided and count is getting increased ",
						ciqRiskFileName[i], saasType, ciqRiskType[i], Arrays.asList(ciqRisks[i].split(",")), countBeforeUI[i], countBeforeAPI[i], additionalParamsUI, additionalParamsAPI)};
			}

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}	

		dciFunctions.waitForMinutes(5);

		mainCounter = 0;testCounter = DCIConstants.DCI_COUNTER_MEDIUM_LL;
		
		return result;
	}

	@DataProvider(name = "dataDeleteRiskTypeCIQExposedPublic")
	public Object[][] dataDeleteRiskTypeCIQExposedPublic() {

		dciFunctions = new DCIFunctions();
		String saasType = SaasType.getSaasFilterType(suiteData.getSaasApp());
		String appType = SaasType.getAppType(suiteData.getSaasApp(), suiteData.getEnvironmentName());
		String vlType = SaasType.getVlType(suiteData.getSaasApp());
		String objectType = SaasType.getObjectType(suiteData.getSaasApp());
		Map<String, String>  additionalParamsAPI = dciFunctions.generateHeadersAPI("true","true");
		Map<String, String>  additionalParamsUI = dciFunctions.generateHeadersUI(vlType, "true", objectType);

		Object[][] result = new Object[ciqRiskFileName.length][];

		try {


			for (int i = 0; i < ciqRiskFileName.length; i++) {
				
				Vulnerability vulnerability = dciFunctions.getVulnerability(suiteData, restClient, additionalParamsUI);
				Logger.info("Vulnerability:" + ciqRiskType[i] + "::Count:" + vulnerability.getResults().getCIQCount(ciqRiskType[i]));
				VulnerabilityTypes vulnerabilityTypes = dciFunctions.getVulnerabilityTypes(suiteData, restClient, appType,additionalParamsAPI);
				Logger.info("Vulnerability:" + ciqRiskType[i] + "::Count:" + vulnerabilityTypes.getObjects().getCIQCount(ciqRiskType[i]));
				int countBeforeUI= vulnerability.getResults().getCIQCount(ciqRiskType[i]);
				int countBeforeAPI= vulnerabilityTypes.getObjects().getCIQCount(ciqRiskType[i]);


				Map<String, String> fileInfo = new HashMap<String, String>();
				fileInfo.put("fileId",fileId[i]);
				fileInfo.put("fileEtag",fileEtag[i]);

				result[i] = new Object[] { new TestParameters("Filter count check"+ciqRiskFileName[i]+" for deletion of exposed public ciqRisks types", 
						"Delete a public shared ciqRisks types file:"+ciqRiskFileName[i]+ ". Then verify ciqRisk logs are getting generated within the SLA provided and count is getting decreased ",
						ciqRiskFileName[i], saasType, ciqRiskType[i], Arrays.asList(ciqRisks[i].split(",")), countBeforeUI, countBeforeAPI, additionalParamsUI, additionalParamsAPI)};
			}

			dciFunctions.waitForMinutes(1);
			UserAccount account = dciFunctions.getUserAccount(suiteData);
			UniversalApi universalApi = dciFunctions.getUniversalApi(suiteData, account);
			if(SaasType.getSaasType(suiteData.getSaasApp()).equals(SaasType.Office365)){
				Map<String,String> folderInfo = new HashMap<String,String>();
				folderInfo.put("folderName", folderResource.getName());
				folderInfo.put("folderId", folderResource.getId());
				folderInfo.put("folderEtag", folderResource.getETag());
				folderInfo.put("folderType", folderResource.getType()); 
				
				dciFunctions.deleteFolder(universalApi, suiteData, folderInfo);
			}else{
				dciFunctions.deleteFolder(universalApi, suiteData, folderInfo);
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}	

		dciFunctions.waitForMinutes(5);

		return result;
	}

	@DataProvider(name = "dataUploadRiskTypeCIQExposedInternal")
	public Object[][] dataUploadRiskTypeCIQExposedInternal() {

		dciFunctions = new DCIFunctions();
		String saasType = SaasType.getSaasFilterType(suiteData.getSaasApp());
		String appType = SaasType.getAppType(suiteData.getSaasApp(), suiteData.getEnvironmentName());
		String vlType = SaasType.getVlType(suiteData.getSaasApp());
		String objectType = SaasType.getObjectType(suiteData.getSaasApp());
		Map<String, String>  additionalParamsAPI = dciFunctions.generateHeadersAPI("true","true");
		Map<String, String>  additionalParamsUI = dciFunctions.generateHeadersUI(vlType, "true", objectType);

		UserAccount account = dciFunctions.getUserAccount(suiteData);
		UniversalApi universalApi = dciFunctions.getUniversalApi(suiteData, account);
		if(SaasType.getSaasType(suiteData.getSaasApp()).equals(SaasType.Office365)){
			folderResource = dciFunctions.createFolderInOneDrive(universalApi, suiteData,
					DCIConstants.DCI_FOLDER+uniqueId);
		}else if(SaasType.getSaasType(suiteData.getSaasApp()).equals(SaasType.Dropbox)){
			Logger.info("Folder in dropbox is not created");
			folderInfo.put("folderName", File.separator+dciFunctions.getDropboxInternalFolderName(suiteData));
			folderInfo.put("folderId", File.separator+dciFunctions.getDropboxInternalFolderName(suiteData));
			folderInfo.put("folderEtag", "");
			folderInfo.put("folderType", ""); 
		}else{
			folderInfo = dciFunctions.createFolder(universalApi, suiteData, 
					DCIConstants.DCI_FOLDER+uniqueId);
		}
		
		try {
			String ciqDictionaries = dciFunctions.predefinedDictionariesCIQProfileText(ciqRiskType[0]);
			String ciqDictProfileNames = dciFunctions.predefinedDictionariesCIQProfileName(ciqRiskType[0]); 
			String ciqDictProfileDescription = dciFunctions.predefinedDictionariesCIQProfileDescription(ciqRiskType[0]);

			String ciqTerms = dciFunctions.predefinedTermsCIQProfileText(ciqRiskType[1]);
			String ciqTermsProfileNames = dciFunctions.predefinedTermsCIQProfileName(ciqRiskType[1]); 
			String ciqTermsProfileDescription = dciFunctions.predefinedTermsCIQProfileDescription(ciqRiskType[1]);

			dciFunctions.createCIQPredefinedDictionaries(restClient, suiteData, ciqDictionaries, ciqDictProfileNames,
					ciqDictProfileDescription, "high", 1, true, 1, false);
			dciFunctions.createCIQPredefinedTerms(restClient, suiteData, ciqTerms, ciqTermsProfileNames,
					ciqTermsProfileDescription, "high", 1, true, 1, false);
			
		} catch (Exception ex) {
			Logger.info("Issue with Create Content Iq Profiles" + ex.getLocalizedMessage());
		}

		fileName = new String[ciqRiskFileName.length];
		fileId = new String[ciqRiskFileName.length];
		fileEtag = new String[ciqRiskFileName.length];
		fileResource = new ItemResource[ciqRiskFileName.length];

		int[] countBeforeUI= new int[ciqRiskFileName.length];
		int[] countBeforeAPI= new int[ciqRiskFileName.length];

		
		Object[][] result = new Object[ciqRiskFileName.length][];

		try {
			ciqRiskFileName = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_CIQ_EXPOSURE_PATH, ciqRiskFileName);

			for (int i = 0; i < ciqRiskFileName.length; i++) {
				
				Vulnerability vulnerability = dciFunctions.getVulnerability(suiteData, restClient, additionalParamsUI);
				Logger.info("Vulnerability:" + ciqRiskType[i] + "::Count:" + vulnerability.getResults().getCIQCount(ciqRiskType[i]));
				VulnerabilityTypes vulnerabilityTypes = dciFunctions.getVulnerabilityTypes(suiteData, restClient, appType,additionalParamsAPI);
				Logger.info("Vulnerability:" + ciqRiskType[i] + "::Count:" + vulnerabilityTypes.getObjects().getCIQCount(ciqRiskType[i]));
				countBeforeUI[i]= vulnerability.getResults().getCIQCount(ciqRiskType[i]);
				countBeforeAPI[i]= vulnerabilityTypes.getObjects().getCIQCount(ciqRiskType[i]);
			}for (int i = 0; i < ciqRiskFileName.length; i++) {
				if(SaasType.getSaasType(suiteData.getSaasApp()).equals(SaasType.Office365)){
					fileResource[i] = dciFunctions.uploadFileInOneDrive(universalApi, suiteData, 
							folderResource.getId(), ciqRiskFileName[i]);
					
					dciFunctions.shareFileOnOneDrive(fileResource[i], universalApi, "Internal");
				}else if(SaasType.getSaasType(suiteData.getSaasApp()).equals(SaasType.Dropbox)){
					Map<String, String> fileInfo = new HashMap<String, String>();
					fileInfo.put("fileName", folderInfo.get("folderName")+File.separator+ciqRiskFileName[i]);
					fileInfo.put("fileId", folderInfo.get("folderName")+File.separator+ciqRiskFileName[i]);
					fileInfo.put("fileEtag", "");
					 
					fileInfo = dciFunctions.shareFile(suiteData, universalApi, folderInfo, fileInfo, "Internal"); 
					fileName[i] = fileInfo.get("fileName");
					fileId[i] = fileInfo.get("fileId");
					fileEtag[i] = fileInfo.get("fileEtag");
				}else{
					Map<String, String> fileInfo = dciFunctions.uploadFile(universalApi,suiteData, 
							folderInfo.get("folderId"), ciqRiskFileName[i]);
					fileId[i] = fileInfo.get("fileId");
					fileEtag[i] = fileInfo.get("fileEtag");

					fileInfo = dciFunctions.shareFile(suiteData, universalApi, folderInfo, fileInfo, "Internal");
					fileId[i] = fileInfo.get("fileId");
					fileEtag[i] = fileInfo.get("fileEtag");

				}

				result[i] = new Object[] { new TestParameters("Filter count check"+ciqRiskFileName[i]+" for exposed internal ciqRisks types", 
						"Upload and share internal ciqRisks types file:"+ciqRiskFileName[i]+ ". Then verify ciqRisk logs are getting generated within the SLA provided and count is getting increased ",
						ciqRiskFileName[i], saasType, ciqRiskType[i], Arrays.asList(ciqRisks[i].split(",")), countBeforeUI[i], countBeforeAPI[i], additionalParamsUI, additionalParamsAPI)};
			}

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}	

		dciFunctions.waitForMinutes(5);

		mainCounter = 0;testCounter = DCIConstants.DCI_COUNTER_MEDIUM_LL;
		
		return result;
	}

	@DataProvider(name = "dataDeleteRiskTypeCIQExposedInternal")
	public Object[][] dataDeleteRiskTypeCIQExposedInternal() {

		dciFunctions = new DCIFunctions();
		String saasType = SaasType.getSaasFilterType(suiteData.getSaasApp());
		String appType = SaasType.getAppType(suiteData.getSaasApp(), suiteData.getEnvironmentName());
		String vlType = SaasType.getVlType(suiteData.getSaasApp());
		String objectType = SaasType.getObjectType(suiteData.getSaasApp());
		Map<String, String>  additionalParamsAPI = dciFunctions.generateHeadersAPI("true","true");
		Map<String, String>  additionalParamsUI = dciFunctions.generateHeadersUI(vlType, "true", objectType);

		Object[][] result = new Object[ciqRiskFileName.length][];

		try {


			for (int i = 0; i < ciqRiskFileName.length; i++) {
				
				Vulnerability vulnerability = dciFunctions.getVulnerability(suiteData, restClient, additionalParamsUI);
				Logger.info("Vulnerability:" + ciqRiskType[i] + "::Count:" + vulnerability.getResults().getCIQCount(ciqRiskType[i]));
				VulnerabilityTypes vulnerabilityTypes = dciFunctions.getVulnerabilityTypes(suiteData, restClient, appType,additionalParamsAPI);
				Logger.info("Vulnerability:" + ciqRiskType[i] + "::Count:" + vulnerabilityTypes.getObjects().getCIQCount(ciqRiskType[i]));
				int countBeforeUI= vulnerability.getResults().getCIQCount(ciqRiskType[i]);
				int countBeforeAPI= vulnerabilityTypes.getObjects().getCIQCount(ciqRiskType[i]);


				Map<String, String> fileInfo = new HashMap<String, String>();
				fileInfo.put("fileId",fileId[i]);
				fileInfo.put("fileEtag",fileEtag[i]);

				result[i] = new Object[] { new TestParameters("Filter count check"+ciqRiskFileName[i]+" for deletion of exposed internal ciqRisks types", 
						"Delete a internal shared ciqRisks types file:"+ciqRiskFileName[i]+ ". Then verify ciqRisk logs are getting generated within the SLA provided and count is getting decreased ",
						ciqRiskFileName[i], saasType, ciqRiskType[i], Arrays.asList(ciqRisks[i].split(",")), countBeforeUI, countBeforeAPI, additionalParamsUI, additionalParamsAPI)};
			}

			dciFunctions.waitForMinutes(1);
			UserAccount account = dciFunctions.getUserAccount(suiteData);
			UniversalApi universalApi = dciFunctions.getUniversalApi(suiteData, account);
			if(SaasType.getSaasType(suiteData.getSaasApp()).equals(SaasType.Office365)){
				Map<String,String> folderInfo = new HashMap<String,String>();
				folderInfo.put("folderName", folderResource.getName());
				folderInfo.put("folderId", folderResource.getId());
				folderInfo.put("folderEtag", folderResource.getETag());
				folderInfo.put("folderType", folderResource.getType()); 
				
				dciFunctions.deleteFolder(universalApi, suiteData, folderInfo);
			}else if(SaasType.getSaasType(suiteData.getSaasApp()).equals(SaasType.Dropbox)){
				for (int i = 0; i < ciqRiskFileName.length; i++) {
					Map<String, String> fileInfo = new HashMap<String, String>();
					fileInfo.put("fileName",fileName[i]);
					fileInfo.put("fileId",fileId[i]);
					fileInfo.put("fileEtag",fileEtag[i]);
					dciFunctions.deleteFile(universalApi, suiteData, fileInfo);
				}
			}else{
				dciFunctions.deleteFolder(universalApi, suiteData, folderInfo);
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}	

		dciFunctions.waitForMinutes(5);

		return result;
	}

	@DataProvider(name = "dataUploadRiskTypeCIQExposedExternal")
	public Object[][] dataUploadRiskTypeCIQExposedExternal() {

		dciFunctions = new DCIFunctions();

		String saasType = SaasType.getSaasFilterType(suiteData.getSaasApp());
		String appType = SaasType.getAppType(suiteData.getSaasApp(), suiteData.getEnvironmentName());
		String vlType = SaasType.getVlType(suiteData.getSaasApp());
		String objectType = SaasType.getObjectType(suiteData.getSaasApp());
		Map<String, String>  additionalParamsAPI = dciFunctions.generateHeadersAPI("true",true);
		Map<String, String>  additionalParamsUI = dciFunctions.generateHeadersUI(vlType, true, objectType);
		
		UserAccount account = dciFunctions.getUserAccount(suiteData);
		UniversalApi universalApi = dciFunctions.getUniversalApi(suiteData, account);
		if(SaasType.getSaasType(suiteData.getSaasApp()).equals(SaasType.Office365)){
			folderResource = dciFunctions.createFolderInOneDrive(universalApi, suiteData,
					DCIConstants.DCI_FOLDER+uniqueId);
			folderInfo.put("folderName", folderResource.getName());
			folderInfo.put("folderId", folderResource.getId());
			folderInfo.put("folderEtag", folderResource.getETag());
			folderInfo.put("folderType", folderResource.getType());
		}else{
			folderInfo = dciFunctions.createFolder(universalApi, suiteData, 
					DCIConstants.DCI_FOLDER+uniqueId);
		}

		try {
			String ciqDictionaries = dciFunctions.predefinedDictionariesCIQProfileText(ciqRiskType[0]);
			String ciqDictProfileNames = dciFunctions.predefinedDictionariesCIQProfileName(ciqRiskType[0]); 
			String ciqDictProfileDescription = dciFunctions.predefinedDictionariesCIQProfileDescription(ciqRiskType[0]);

			String ciqTerms = dciFunctions.predefinedTermsCIQProfileText(ciqRiskType[1]);
			String ciqTermsProfileNames = dciFunctions.predefinedTermsCIQProfileName(ciqRiskType[1]); 
			String ciqTermsProfileDescription = dciFunctions.predefinedTermsCIQProfileDescription(ciqRiskType[1]);

			dciFunctions.createCIQPredefinedDictionaries(restClient, suiteData, ciqDictionaries, ciqDictProfileNames,
					ciqDictProfileDescription, "high", 1, true, 1, false);
			dciFunctions.createCIQPredefinedTerms(restClient, suiteData, ciqTerms, ciqTermsProfileNames,
					ciqTermsProfileDescription, "high", 1, true, 1, false);
			
		} catch (Exception ex) {
			Logger.info("Issue with Create Content Iq Profiles" + ex.getLocalizedMessage());
		}


		fileId = new String[ciqRiskFileName.length];
		fileEtag = new String[ciqRiskFileName.length];
		fileResource = new ItemResource[ciqRiskFileName.length];
		int[] countBeforeUI= new int[ciqRiskFileName.length];
		int[] countBeforeAPI= new int[ciqRiskFileName.length];

		Object[][] result = new Object[ciqRiskFileName.length][];

		account = dciFunctions.getUserAccount(suiteData);
		universalApi = dciFunctions.getUniversalApi(suiteData, account);
		
		try {
			ciqRiskFileName = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_CIQ_EXPOSURE_PATH, ciqRiskFileName);

			Map<String, String> fileInfo = new HashMap<String, String>();

			for (int i = 0; i < ciqRiskFileName.length; i++) {

				Vulnerability vulnerability = dciFunctions.getVulnerability(suiteData, restClient, additionalParamsUI);
				Logger.info("Vulnerability:" + ciqRiskType[i] + "::Count:" + vulnerability.getResults().getCIQCount(ciqRiskType[i]));
				VulnerabilityTypes vulnerabilityTypes = dciFunctions.getVulnerabilityTypes(suiteData, restClient, appType,additionalParamsAPI);
				Logger.info("Vulnerability:" + ciqRiskType[i] + "::Count:" + vulnerabilityTypes.getObjects().getCIQCount(ciqRiskType[i]));
				countBeforeUI[i]= vulnerability.getResults().getCIQCount(ciqRiskType[i]);
				countBeforeAPI[i]= vulnerabilityTypes.getObjects().getCIQCount(ciqRiskType[i]);
			}for (int i = 0; i < ciqRiskFileName.length; i++) {
				if(SaasType.getSaasType(suiteData.getSaasApp()).equals(SaasType.Office365)){
					fileResource[i] = dciFunctions.uploadFileInOneDrive(universalApi, suiteData, 
							folderResource.getId(), ciqRiskFileName[i]);
					dciFunctions.shareFileOnOneDrive(fileResource[i], universalApi, "External");
				}else{
					fileInfo = dciFunctions.uploadFile(universalApi,suiteData, folderInfo.get("folderId"), ciqRiskFileName[i]);
					fileId[i] = fileInfo.get("fileId");
					fileEtag[i] = fileInfo.get("fileEtag");
					if(SaasType.getSaasType(suiteData.getSaasApp()).equals(SaasType.GoogleDrive)){
						fileInfo = dciFunctions.shareFile(suiteData, universalApi, folderInfo, fileInfo, "External");
						fileId[i] = fileInfo.get("fileId");
						fileEtag[i] = fileInfo.get("fileEtag");
					}
				}

				result[i] = new Object[] { new TestParameters("Filter count check"+ciqRiskFileName[i]+" for exposed external ciq risks types", 
						"Upload and share external ciq risks types file:"+ciqRiskFileName[i]+ ". Then verify ciq risk logs are getting generated within the SLA provided and count is getting increased ",
						ciqRiskFileName[i], saasType, ciqRiskType[i], Arrays.asList(ciqRisks[i].split(",")), countBeforeUI[i], countBeforeAPI[i], additionalParamsUI, additionalParamsAPI)};
			}

			dciFunctions.waitForMinutes(1);
			account = dciFunctions.getUserAccount(suiteData);
			universalApi = dciFunctions.getUniversalApi(suiteData, account);
			if(SaasType.getSaasType(suiteData.getSaasApp()).equals(SaasType.Office365)||
					SaasType.getSaasType(suiteData.getSaasApp()).equals(SaasType.GoogleDrive)){
			}else{
				folderInfo = dciFunctions.shareFile(suiteData, universalApi, folderInfo, fileInfo, "External");
			}

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}	

		dciFunctions.waitForMinutes(5);

		mainCounter = 0;testCounter = DCIConstants.DCI_COUNTER_MEDIUM_LL;
		
		return result;
	}

	@DataProvider(name = "dataDeleteRiskTypeCIQExposedExternal")
	public Object[][] dataDeleteRiskTypeCIQExposedExternal() {

		dciFunctions = new DCIFunctions();
		String saasType = SaasType.getSaasFilterType(suiteData.getSaasApp());
		String appType = SaasType.getAppType(suiteData.getSaasApp(), suiteData.getEnvironmentName());
		String vlType = SaasType.getVlType(suiteData.getSaasApp());
		String objectType = SaasType.getObjectType(suiteData.getSaasApp());
		Map<String, String>  additionalParamsAPI = dciFunctions.generateHeadersAPI("true",true);
		Map<String, String>  additionalParamsUI = dciFunctions.generateHeadersUI(vlType, true, objectType);
		
		Object[][] result = new Object[ciqRiskFileName.length][];

		try {
			for (int i = 0; i < ciqRiskFileName.length; i++) {
				
				Vulnerability vulnerability = dciFunctions.getVulnerability(suiteData, restClient, additionalParamsUI);
				Logger.info("Vulnerability:" + ciqRiskType[i] + "::Count:" + vulnerability.getResults().getCIQCount(ciqRiskType[i]));
				VulnerabilityTypes vulnerabilityTypes = dciFunctions.getVulnerabilityTypes(suiteData, restClient, appType,additionalParamsAPI);
				Logger.info("Vulnerability:" + ciqRiskType[i] + "::Count:" + vulnerabilityTypes.getObjects().getCIQCount(ciqRiskType[i]));
				int countBeforeUI= vulnerability.getResults().getCIQCount(ciqRiskType[i]);
				int countBeforeAPI= vulnerabilityTypes.getObjects().getCIQCount(ciqRiskType[i]);


				Map<String, String> fileInfo = new HashMap<String, String>();
				fileInfo.put("fileId",fileId[i]);
				fileInfo.put("fileEtag",fileEtag[i]);

				result[i] = new Object[] { new TestParameters("Filter count check"+ciqRiskFileName[i]+" for deletion of exposed external ciqRisks types", 
						"Delete a external shared ciq risks types file:"+ciqRiskFileName[i]+ ". Then verify ciq risk logs are getting generated within the SLA provided and count is getting decreased ",
						ciqRiskFileName[i], saasType, ciqRiskType[i], Arrays.asList(ciqRisks[i].split(",")), countBeforeUI, countBeforeAPI, additionalParamsUI, additionalParamsAPI)};
			}

			dciFunctions.waitForMinutes(1);
			UserAccount account = dciFunctions.getUserAccount(suiteData);
			UniversalApi universalApi = dciFunctions.getUniversalApi(suiteData, account);
			if(SaasType.getSaasType(suiteData.getSaasApp()).equals(SaasType.Office365)){
				Map<String,String> folderInfo = new HashMap<String,String>();
				folderInfo.put("folderName", folderResource.getName());
				folderInfo.put("folderId", folderResource.getId());
				folderInfo.put("folderEtag", folderResource.getETag());
				folderInfo.put("folderType", folderResource.getType()); 
				
				dciFunctions.deleteFolder(universalApi, suiteData, folderInfo);
			}else{
				dciFunctions.deleteFolder(universalApi, suiteData, folderInfo);
			}

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}	

		dciFunctions.waitForMinutes(5);

		return result;
	}
	
	@DataProvider(name = "dataUploadFileClassTypeUnexposed")
	public Object[][] dataUploadFileClassTypeUnexposed() {

		dciFunctions = new DCIFunctions();
		try {dciFunctions.createCIQProfileWithFileFormats(restClient, suiteData);} catch (Exception e) {}
		
		String saasType = SaasType.getSaasFilterType(suiteData.getSaasApp());
		String vlType = SaasType.getVlType(suiteData.getSaasApp());
		String objectType = SaasType.getObjectType(suiteData.getSaasApp());
		Map<String, String>  additionalParamsUI = dciFunctions.generateHeadersUI(vlType, "true", "risky_docs", objectType);

		UserAccount account = dciFunctions.getUserAccount(suiteData);
		UniversalApi universalApi = dciFunctions.getUniversalApi(suiteData, account);
		folderInfo = dciFunctions.createFolder(universalApi, suiteData, 
				DCIConstants.DCI_FOLDER+uniqueId);

		fileId = new String[fileClassFileName.length];
		fileEtag = new String[fileClassFileName.length];

		int[] countBeforeUI = new int[fileClassFileName.length];
		Object[][] result = new Object[fileClassFileName.length][];

		try {
			fileClassFileName = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_CIQ_FILE_CLASS, fileClassFileName);

			for (int i = 0; i < fileClassFileName.length; i++) {
				
				FileType fileClass = dciFunctions.getFileType(suiteData, restClient, additionalParamsUI);
				Logger.info("FileClass:" + fileClassType[i] + "::Count:" + fileClass.getFileTypeCount(fileClassType[i]));
				countBeforeUI[i]= fileClass.getFileTypeCount(fileClassType[i]);
			}for (int i = 0; i < fileClassFileName.length; i++) {	
				Map<String, String> fileInfo = dciFunctions.uploadFile(universalApi,suiteData, folderInfo.get("folderId"), fileClassFileName[i]);
				fileId[i] = fileInfo.get("fileId");
				fileEtag[i] = fileInfo.get("fileEtag");
				

				result[i] = new Object[] { new TestParameters("Filter count check"+fileClassFileName[i]+" for upload of ciqRisk types", 
						"Upload a fileClassRisks types file:"+fileClassFileName[i]+ ". Then verify ciqRisk logs are getting generated within the SLA provided and count is getting increased ",
						fileClassFileName[i], saasType, fileClassType[i], Arrays.asList(fileClassRisks[i].split(",")), countBeforeUI[i], additionalParamsUI)};
			}

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}	
		
		mainCounter = 0;testCounter = DCIConstants.DCI_COUNTER_MEDIUM_LL;

		return result;
	}

	@DataProvider(name = "dataDeleteFileClassTypeUnexposed")
	public Object[][] dataDeleteFileClassTypeUnexposed() {

		dciFunctions = new DCIFunctions();
		String saasType = SaasType.getSaasFilterType(suiteData.getSaasApp());
		String vlType = SaasType.getVlType(suiteData.getSaasApp());
		String objectType = SaasType.getObjectType(suiteData.getSaasApp());
		Map<String, String>  additionalParamsUI = dciFunctions.generateHeadersUI(vlType, "true", "risky_docs", objectType);

		Object[][] result = new Object[fileClassFileName.length][];

		try {

			for (int i = 0; i < fileClassFileName.length; i++) {
				
				FileType fileClass = dciFunctions.getFileType(suiteData, restClient, additionalParamsUI);
				Logger.info("FileClass:" + fileClassType[i] + "::Count:" + fileClass.getFileTypeCount(fileClassType[i]));
				int countBeforeUI= fileClass.getFileTypeCount(fileClassType[i]);
				

				Map<String, String> fileInfo = new HashMap<String, String>();
				fileInfo.put("fileId",fileId[i]);
				fileInfo.put("fileEtag",fileEtag[i]);

				result[i] = new Object[] { new TestParameters("Filter count check"+fileClassFileName[i]+" for deletion of fileClassRisks types", 
						"Delete a fileClassRisks types file:"+fileClassFileName[i]+ ". Then verify ciqRisk logs are getting generated within the SLA provided and count is getting decreased ",
						fileClassFileName[i], saasType, fileClassType[i], Arrays.asList(fileClassRisks[i].split(",")), countBeforeUI, additionalParamsUI)};
			}

			dciFunctions.waitForMinutes(1);
			UserAccount account = dciFunctions.getUserAccount(suiteData);
			UniversalApi universalApi = dciFunctions.getUniversalApi(suiteData, account);
			dciFunctions.deleteFolder(universalApi, suiteData, folderInfo);

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}	

		dciFunctions.waitForMinutes(5);

		return result;
	}

	@DataProvider(name = "dataUploadFileClassTypeExposedPublic")
	public Object[][] dataUploadFileClassTypeExposedPublic() {

		dciFunctions = new DCIFunctions();
		try {dciFunctions.createCIQProfileWithFileFormats(restClient, suiteData);} catch (Exception e) {}
		
		String saasType = SaasType.getSaasFilterType(suiteData.getSaasApp());
		String vlType = SaasType.getVlType(suiteData.getSaasApp());
		String objectType = SaasType.getObjectType(suiteData.getSaasApp());
		Map<String, String>  additionalParamsUI = dciFunctions.generateHeadersUI(vlType, "true", objectType);

		UserAccount account = dciFunctions.getUserAccount(suiteData);
		UniversalApi universalApi = dciFunctions.getUniversalApi(suiteData, account);
		if(SaasType.getSaasType(suiteData.getSaasApp()).equals(SaasType.Office365)){
			folderResource = dciFunctions.createFolderInOneDrive(universalApi, suiteData,
					DCIConstants.DCI_FOLDER+uniqueId);
		}else{
			folderInfo = dciFunctions.createFolder(universalApi, suiteData, 
					DCIConstants.DCI_FOLDER+uniqueId);
		}

		fileId = new String[fileClassFileName.length];
		fileEtag = new String[fileClassFileName.length];
		fileResource = new ItemResource[fileClassFileName.length];

		int[] countBeforeUI = new int[fileClassFileName.length];
		Object[][] result = new Object[fileClassFileName.length][];

		try {
			fileClassFileName = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_CIQ_FILE_CLASS, fileClassFileName);
			
			for (int i = 0; i < fileClassFileName.length; i++) {
				
				FileType fileClass = dciFunctions.getFileType(suiteData, restClient, additionalParamsUI);
				Logger.info("FileClass:" + fileClassType[i] + "::Count:" + fileClass.getFileTypeCount(fileClassType[i]));
				countBeforeUI[i]= fileClass.getFileTypeCount(fileClassType[i]);
			}for (int i = 0; i < fileClassFileName.length; i++) {	
				if(SaasType.getSaasType(suiteData.getSaasApp()).equals(SaasType.Office365)){
					fileResource[i] = dciFunctions.uploadFileInOneDrive(universalApi, suiteData, 
							folderResource.getId(), fileClassFileName[i]);
					
					dciFunctions.shareFileOnOneDrive(fileResource[i], universalApi, "Public");
				}else{
					Map<String, String> fileInfo = dciFunctions.uploadFile(universalApi,suiteData, 
							folderInfo.get("folderId"), fileClassFileName[i]);
					fileId[i] = fileInfo.get("fileId");
					fileEtag[i] = fileInfo.get("fileEtag");

					fileInfo = dciFunctions.shareFile(suiteData, universalApi, folderInfo, fileInfo, "Public");
					fileId[i] = fileInfo.get("fileId");
					fileEtag[i] = fileInfo.get("fileEtag");

				}

				result[i] = new Object[] { new TestParameters("Filter count check"+fileClassFileName[i]+" for exposed public fileClassRisks types", 
						"Upload and share publicly fileClassRisks types file:"+fileClassFileName[i]+ ". Then verify ciqRisk logs are getting generated within the SLA provided and count is getting increased ",
						fileClassFileName[i], saasType, fileClassType[i], Arrays.asList(fileClassRisks[i].split(",")), countBeforeUI[i], additionalParamsUI)};
			}

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}	

		dciFunctions.waitForMinutes(5);

		mainCounter = 0;testCounter = DCIConstants.DCI_COUNTER_MEDIUM_LL;
		
		return result;
	}

	@DataProvider(name = "dataDeleteFileClassTypeExposedPublic")
	public Object[][] dataDeleteFileClassTypeExposedPublic() {

		dciFunctions = new DCIFunctions();
		String saasType = SaasType.getSaasFilterType(suiteData.getSaasApp());
		String vlType = SaasType.getVlType(suiteData.getSaasApp());
		String objectType = SaasType.getObjectType(suiteData.getSaasApp());
		Map<String, String>  additionalParamsUI = dciFunctions.generateHeadersUI(vlType, "true", objectType);

		Object[][] result = new Object[fileClassFileName.length][];

		try {


			for (int i = 0; i < fileClassFileName.length; i++) {
				
				FileType fileClass = dciFunctions.getFileType(suiteData, restClient, additionalParamsUI);
				Logger.info("FileClass:" + fileClassType[i] + "::Count:" + fileClass.getFileTypeCount(fileClassType[i]));
				int countBeforeUI= fileClass.getFileTypeCount(fileClassType[i]);

				Map<String, String> fileInfo = new HashMap<String, String>();
				fileInfo.put("fileId",fileId[i]);
				fileInfo.put("fileEtag",fileEtag[i]);

				result[i] = new Object[] { new TestParameters("Filter count check"+fileClassFileName[i]+" for deletion of exposed public fileClassRisks types", 
						"Delete a public shared fileClassRisks types file:"+fileClassFileName[i]+ ". Then verify ciqRisk logs are getting generated within the SLA provided and count is getting decreased ",
						fileClassFileName[i], saasType, fileClassType[i], Arrays.asList(fileClassRisks[i].split(",")), countBeforeUI, additionalParamsUI)};
			}

			dciFunctions.waitForMinutes(1);
			UserAccount account = dciFunctions.getUserAccount(suiteData);
			UniversalApi universalApi = dciFunctions.getUniversalApi(suiteData, account);
			if(SaasType.getSaasType(suiteData.getSaasApp()).equals(SaasType.Office365)){
				Map<String,String> folderInfo = new HashMap<String,String>();
				folderInfo.put("folderName", folderResource.getName());
				folderInfo.put("folderId", folderResource.getId());
				folderInfo.put("folderEtag", folderResource.getETag());
				folderInfo.put("folderType", folderResource.getType()); 
				
				dciFunctions.deleteFolder(universalApi, suiteData, folderInfo);
			}else{
				dciFunctions.deleteFolder(universalApi, suiteData, folderInfo);
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}	

		dciFunctions.waitForMinutes(5);

		return result;
	}

	@DataProvider(name = "dataUploadFileClassTypeExposedInternal")
	public Object[][] dataUploadFileClassTypeExposedInternal() {

		dciFunctions = new DCIFunctions();
		try {dciFunctions.createCIQProfileWithFileFormats(restClient, suiteData);} catch (Exception e) {}
		
		String saasType = SaasType.getSaasFilterType(suiteData.getSaasApp());
		String vlType = SaasType.getVlType(suiteData.getSaasApp());
		String objectType = SaasType.getObjectType(suiteData.getSaasApp());
		Map<String, String>  additionalParamsUI = dciFunctions.generateHeadersUI(vlType, "true", objectType);

		UserAccount account = dciFunctions.getUserAccount(suiteData);
		UniversalApi universalApi = dciFunctions.getUniversalApi(suiteData, account);
		if(SaasType.getSaasType(suiteData.getSaasApp()).equals(SaasType.Office365)){
			folderResource = dciFunctions.createFolderInOneDrive(universalApi, suiteData,
					DCIConstants.DCI_FOLDER+uniqueId);
		}else if(SaasType.getSaasType(suiteData.getSaasApp()).equals(SaasType.Dropbox)){
			Logger.info("Folder in dropbox is not created");
			folderInfo.put("folderName", File.separator+dciFunctions.getDropboxInternalFolderName(suiteData));
			folderInfo.put("folderId", File.separator+dciFunctions.getDropboxInternalFolderName(suiteData));
			folderInfo.put("folderEtag", "");
			folderInfo.put("folderType", ""); 
		}else{
			folderInfo = dciFunctions.createFolder(universalApi, suiteData, 
					DCIConstants.DCI_FOLDER+uniqueId);
		}
		
		fileName = new String[fileClassFileName.length];
		fileId = new String[fileClassFileName.length];
		fileEtag = new String[fileClassFileName.length];
		fileResource = new ItemResource[fileClassFileName.length];

		int[] countBeforeUI = new int[fileClassFileName.length];
		Object[][] result = new Object[fileClassFileName.length][];

		try {
			fileClassFileName = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_CIQ_FILE_CLASS, fileClassFileName);

			for (int i = 0; i < fileClassFileName.length; i++) {
				
				FileType fileClass = dciFunctions.getFileType(suiteData, restClient, additionalParamsUI);
				Logger.info("FileClass:" + fileClassType[i] + "::Count:" + fileClass.getFileTypeCount(fileClassType[i]));
				countBeforeUI[i]= fileClass.getFileTypeCount(fileClassType[i]);
			}for (int i = 0; i < fileClassFileName.length; i++) {	
				if(SaasType.getSaasType(suiteData.getSaasApp()).equals(SaasType.Office365)){
					fileResource[i] = dciFunctions.uploadFileInOneDrive(universalApi, suiteData, 
							folderResource.getId(), fileClassFileName[i]);
					
					dciFunctions.shareFileOnOneDrive(fileResource[i], universalApi, "Internal");
				}else if(SaasType.getSaasType(suiteData.getSaasApp()).equals(SaasType.Dropbox)){
					Map<String, String> fileInfo = new HashMap<String, String>();
					fileInfo.put("fileName", folderInfo.get("folderName")+File.separator+fileClassFileName[i]);
					fileInfo.put("fileId", folderInfo.get("folderName")+File.separator+fileClassFileName[i]);
					fileInfo.put("fileEtag", "");
					 
					fileInfo = dciFunctions.shareFile(suiteData, universalApi, folderInfo, fileInfo, "Internal"); 
					fileName[i] = fileInfo.get("fileName");
					fileId[i] = fileInfo.get("fileId");
					fileEtag[i] = fileInfo.get("fileEtag");
				}else{
					Map<String, String> fileInfo = dciFunctions.uploadFile(universalApi,suiteData, 
							folderInfo.get("folderId"), fileClassFileName[i]);
					fileId[i] = fileInfo.get("fileId");
					fileEtag[i] = fileInfo.get("fileEtag");

					fileInfo = dciFunctions.shareFile(suiteData, universalApi, folderInfo, fileInfo, "Internal");
					fileId[i] = fileInfo.get("fileId");
					fileEtag[i] = fileInfo.get("fileEtag");

				}

				result[i] = new Object[] { new TestParameters("Filter count check"+fileClassFileName[i]+" for exposed internal fileClassRisks types", 
						"Upload and share internal fileClassRisks types file:"+fileClassFileName[i]+ ". Then verify ciqRisk logs are getting generated within the SLA provided and count is getting increased ",
						fileClassFileName[i], saasType, fileClassType[i], Arrays.asList(fileClassRisks[i].split(",")), countBeforeUI[i], additionalParamsUI)};
			}

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}	

		dciFunctions.waitForMinutes(5);

		mainCounter = 0;testCounter = DCIConstants.DCI_COUNTER_MEDIUM_LL;
		
		return result;
	}

	@DataProvider(name = "dataDeleteFileClassTypeExposedInternal")
	public Object[][] dataDeleteFileClassTypeExposedInternal() {

		dciFunctions = new DCIFunctions();
		String saasType = SaasType.getSaasFilterType(suiteData.getSaasApp());
		String vlType = SaasType.getVlType(suiteData.getSaasApp());
		String objectType = SaasType.getObjectType(suiteData.getSaasApp());
		Map<String, String>  additionalParamsUI = dciFunctions.generateHeadersUI(vlType, "true", objectType);

		Object[][] result = new Object[fileClassFileName.length][];

		try {


			for (int i = 0; i < fileClassFileName.length; i++) {
				
				FileType fileClass = dciFunctions.getFileType(suiteData, restClient, additionalParamsUI);
				Logger.info("FileClass:" + fileClassType[i] + "::Count:" + fileClass.getFileTypeCount(fileClassType[i]));
				int countBeforeUI= fileClass.getFileTypeCount(fileClassType[i]);
				

				Map<String, String> fileInfo = new HashMap<String, String>();
				fileInfo.put("fileId",fileId[i]);
				fileInfo.put("fileEtag",fileEtag[i]);

				result[i] = new Object[] { new TestParameters("Filter count check"+fileClassFileName[i]+" for deletion of exposed internal fileClassRisks types", 
						"Delete a internal shared fileClassRisks types file:"+fileClassFileName[i]+ ". Then verify ciqRisk logs are getting generated within the SLA provided and count is getting decreased ",
						fileClassFileName[i], saasType, fileClassType[i], Arrays.asList(fileClassRisks[i].split(",")), countBeforeUI, additionalParamsUI)};
			}

			dciFunctions.waitForMinutes(1);
			UserAccount account = dciFunctions.getUserAccount(suiteData);
			UniversalApi universalApi = dciFunctions.getUniversalApi(suiteData, account);
			if(SaasType.getSaasType(suiteData.getSaasApp()).equals(SaasType.Office365)){
				Map<String,String> folderInfo = new HashMap<String,String>();
				folderInfo.put("folderName", folderResource.getName());
				folderInfo.put("folderId", folderResource.getId());
				folderInfo.put("folderEtag", folderResource.getETag());
				folderInfo.put("folderType", folderResource.getType()); 
				
				dciFunctions.deleteFolder(universalApi, suiteData, folderInfo);
			}else if(SaasType.getSaasType(suiteData.getSaasApp()).equals(SaasType.Dropbox)){
				for (int i = 0; i < fileClassFileName.length; i++) {
					Map<String, String> fileInfo = new HashMap<String, String>();
					fileInfo.put("fileName",fileName[i]);
					fileInfo.put("fileId",fileId[i]);
					fileInfo.put("fileEtag",fileEtag[i]);
					dciFunctions.deleteFile(universalApi, suiteData, fileInfo);
				}
			}else{
				dciFunctions.deleteFolder(universalApi, suiteData, folderInfo);
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}	

		dciFunctions.waitForMinutes(5);

		return result;
	}

	@DataProvider(name = "dataUploadFileClassTypeExposedExternal")
	public Object[][] dataUploadFileClassTypeExposedExternal() {

		dciFunctions = new DCIFunctions();
		try {dciFunctions.createCIQProfileWithFileFormats(restClient, suiteData);} catch (Exception e) {}
		
		String saasType = SaasType.getSaasFilterType(suiteData.getSaasApp());
		String vlType = SaasType.getVlType(suiteData.getSaasApp());
		String objectType = SaasType.getObjectType(suiteData.getSaasApp());
		Map<String, String>  additionalParamsUI = dciFunctions.generateHeadersUI(vlType, true, objectType);
		
		UserAccount account = dciFunctions.getUserAccount(suiteData);
		UniversalApi universalApi = dciFunctions.getUniversalApi(suiteData, account);
		if(SaasType.getSaasType(suiteData.getSaasApp()).equals(SaasType.Office365)){
			folderResource = dciFunctions.createFolderInOneDrive(universalApi, suiteData,
					DCIConstants.DCI_FOLDER+uniqueId);
			folderInfo.put("folderName", folderResource.getName());
			folderInfo.put("folderId", folderResource.getId());
			folderInfo.put("folderEtag", folderResource.getETag());
			folderInfo.put("folderType", folderResource.getType());
		}else{
			folderInfo = dciFunctions.createFolder(universalApi, suiteData, 
					DCIConstants.DCI_FOLDER+uniqueId);
		}


		fileId = new String[fileClassFileName.length];
		fileEtag = new String[fileClassFileName.length];
		fileResource = new ItemResource[fileClassFileName.length];

		int[] countBeforeUI = new int[fileClassFileName.length];
		
		Object[][] result = new Object[fileClassFileName.length][];
		
		try {
			fileClassFileName = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_CIQ_FILE_CLASS, fileClassFileName);
			Map<String, String> fileInfo = new HashMap<String, String>();
			
			for (int i = 0; i < fileClassFileName.length; i++) {
				FileType fileClass = dciFunctions.getFileType(suiteData, restClient, additionalParamsUI);
				Logger.info("FileClass:" + fileClassType[i] + "::Count:" + fileClass.getFileTypeCount(fileClassType[i]));
				countBeforeUI[i]= fileClass.getFileTypeCount(fileClassType[i]);
			}
			
			account = dciFunctions.getUserAccount(suiteData); 
			universalApi = dciFunctions.getUniversalApi(suiteData, account);
			
			for (int i = 0; i < fileClassFileName.length; i++) {	
				if(SaasType.getSaasType(suiteData.getSaasApp()).equals(SaasType.Office365)){
					fileResource[i] = dciFunctions.uploadFileInOneDrive(universalApi, suiteData, 
							folderResource.getId(), fileClassFileName[i]);
					dciFunctions.shareFileOnOneDrive(fileResource[i], universalApi, "External");
				}else{
					fileInfo = dciFunctions.uploadFile(universalApi,suiteData, folderInfo.get("folderId"), fileClassFileName[i]);
					fileId[i] = fileInfo.get("fileId");
					fileEtag[i] = fileInfo.get("fileEtag");
					if(SaasType.getSaasType(suiteData.getSaasApp()).equals(SaasType.GoogleDrive)){
						fileInfo = dciFunctions.shareFile(suiteData, universalApi, folderInfo, fileInfo, "External");
						fileId[i] = fileInfo.get("fileId");
						fileEtag[i] = fileInfo.get("fileEtag");
					}
				}

				result[i] = new Object[] { new TestParameters("Filter count check"+fileClassFileName[i]+" for exposed external ciq risks types", 
						"Upload and share external ciq risks types file:"+fileClassFileName[i]+ ". Then verify ciq risk logs are getting generated within the SLA provided and count is getting increased ",
						fileClassFileName[i], saasType, fileClassType[i], Arrays.asList(fileClassRisks[i].split(",")), countBeforeUI[i], additionalParamsUI)};
			}

			dciFunctions.waitForMinutes(1);
			
			account = dciFunctions.getUserAccount(suiteData);
			universalApi = dciFunctions.getUniversalApi(suiteData, account);
			if(SaasType.getSaasType(suiteData.getSaasApp()).equals(SaasType.Office365)||
					SaasType.getSaasType(suiteData.getSaasApp()).equals(SaasType.GoogleDrive)){
			}else{
				folderInfo = dciFunctions.shareFile(suiteData, universalApi, folderInfo, fileInfo, "External");
			}

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}	

		dciFunctions.waitForMinutes(5);

		mainCounter = 0;testCounter = DCIConstants.DCI_COUNTER_MEDIUM_LL;
		
		return result;
	}

	@DataProvider(name = "dataDeleteFileClassTypeExposedExternal")
	public Object[][] dataDeleteFileClassTypeExposedExternal() {

		dciFunctions = new DCIFunctions();
		String saasType = SaasType.getSaasFilterType(suiteData.getSaasApp());
		String vlType = SaasType.getVlType(suiteData.getSaasApp());
		String objectType = SaasType.getObjectType(suiteData.getSaasApp());
		Map<String, String>  additionalParamsUI = dciFunctions.generateHeadersUI(vlType, true, objectType);
		
		Object[][] result = new Object[fileClassFileName.length][];

		try {
			for (int i = 0; i < fileClassFileName.length; i++) {
				
				FileType fileClass = dciFunctions.getFileType(suiteData, restClient, additionalParamsUI);
				Logger.info("FileClass:" + fileClassType[i] + "::Count:" + fileClass.getFileTypeCount(fileClassType[i]));
				int countBeforeUI= fileClass.getFileTypeCount(fileClassType[i]);
				

				Map<String, String> fileInfo = new HashMap<String, String>();
				fileInfo.put("fileId",fileId[i]);
				fileInfo.put("fileEtag",fileEtag[i]);

				result[i] = new Object[] { new TestParameters("Filter count check"+fileClassFileName[i]+" for deletion of exposed external fileClassRisks types", 
						"Delete a external shared ciq risks types file:"+fileClassFileName[i]+ ". Then verify ciq risk logs are getting generated within the SLA provided and count is getting decreased ",
						fileClassFileName[i], saasType, fileClassType[i], Arrays.asList(fileClassRisks[i].split(",")), countBeforeUI, additionalParamsUI)};
			}

			dciFunctions.waitForMinutes(1);
			UserAccount account = dciFunctions.getUserAccount(suiteData);
			UniversalApi universalApi = dciFunctions.getUniversalApi(suiteData, account);
			if(SaasType.getSaasType(suiteData.getSaasApp()).equals(SaasType.Office365)){
				Map<String,String> folderInfo = new HashMap<String,String>();
				folderInfo.put("folderName", folderResource.getName());
				folderInfo.put("folderId", folderResource.getId());
				folderInfo.put("folderEtag", folderResource.getETag());
				folderInfo.put("folderType", folderResource.getType()); 
				
				dciFunctions.deleteFolder(universalApi, suiteData, folderInfo);
			}else{
				dciFunctions.deleteFolder(universalApi, suiteData, folderInfo);
			}

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}	

		dciFunctions.waitForMinutes(5);

		return result;
	}

	/**********************************************DATA PROVIDERS*********************************************/
	/**********************************************BEFORE/AFTER CLASS*****************************************/

	/**
	 * Create content iq profile
	 */
	@BeforeClass(groups ={"Unexposed"})
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
	@AfterClass(groups ={"Unexposed","ExposedPublic","ExposedInternal","ExposedExternal"})
	public void deleteContentIqProfile(){
		dciFunctions = new DCIFunctions();
		dciFunctions.deleteAllPolicies(restClient, suiteData);
		dciFunctions.deleteAllCIQProfiles(restClient, suiteData);
		dciFunctions.deleteAllDictionaries(restClient, suiteData);
		dciFunctions.deleteAllTrainingProfiles(restClient, suiteData);
	}
	
	@AfterClass(groups ={"ExposedInternal"})
	public void deleteTemp(){
		dciFunctions = new DCIFunctions();
		if(SaasType.getSaasType(suiteData.getSaasApp()).equals(SaasType.Office365)){
			dciFunctions.waitForMinutes(10);
		}
	}

	@BeforeMethod(groups ={"Unexposed","ExposedPublic","ExposedInternal","ExposedExternal"})
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

		softAssert = new SoftAssert();
	}
	
	@BeforeSuite(alwaysRun=true)
	public void downloadFileFromS3() {
		try {
			S3ActionHandler s3 = new S3ActionHandler();
			s3.downloadWholeFolder(DCIConstants.DCI_S3_BUCKET, "DCI/CIQ/Exposure", 
					new File(DCIConstants.DCI_FILE_TEMP_PATH));
			
		} catch (Exception ex) {
			Logger.info("Downloading folder from S3 is failed with exception " + ex.getLocalizedMessage());
		}
	}

	/**********************************************BEFORE/AFTER CLASS*****************************************/


}

