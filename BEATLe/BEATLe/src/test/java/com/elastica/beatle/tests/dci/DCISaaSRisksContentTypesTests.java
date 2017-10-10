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
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import com.elastica.beatle.Priority;
import com.elastica.beatle.RestClient.ClientUtil;
import com.elastica.beatle.S3Utils.S3ActionHandler;
import com.elastica.beatle.dci.DCICommonTest;
import com.elastica.beatle.dci.DCIConstants;
import com.elastica.beatle.dci.DCIFunctions;
import com.elastica.beatle.dci.SaasType;
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

public class DCISaaSRisksContentTypesTests extends DCICommonTest{

	DCIFunctions dciFunctions = null;
	ElasticSearchLogs esLogs = null;
	Map<String,String> folderInfo = new HashMap<String,String>();
	String uniqueId = UUID.randomUUID().toString();
	List<String> uploadId = new ArrayList<String>();  
	Office365MailActivities objMail = null;
	GoogleMailServices gobjMail=null;

	int mainCounter = 0;int testCounter=0;

	String ferpaFileName="";String glbaFileName="";String hipaaFileName="";
	String pciFileName="";String piiFileName="";String vbaMacrosFileName="";String virusFileName="";

	String businessFileName="";String computingFileName="";
	String digitalCertFileName="";String designFileName="";String encryptionFileName="";
	String enggFileName="";String healthFileName="";
	String legalFileName="";String sourceCodeFileName="";
	
	String largeFileName="100MB.doc";
	
	/*String audioFileName="";String execFileName="";String imageFileName="";String videoFileName="";*/
	

	/**********************************************TEST METHODS***********************************************/

	public void fileUploadToSaasApp() throws Exception {
		dciFunctions = new DCIFunctions();

		ferpaFileName="ferpa.txt";glbaFileName="glba.txt";
		hipaaFileName="hipaa.rtf";pciFileName="pci.txt";piiFileName="pii.rtf";
		vbaMacrosFileName="vba_macro.xls";virusFileName="virus.html";

		/*audioFileName="audio.mp3";execFileName="executable.exe";imageFileName="image.jpeg";videoFileName="video.mp4";*/
		
		businessFileName="business.txt";computingFileName="computing.doc";
		digitalCertFileName="digital_certificate.pem";designFileName="design.txt";encryptionFileName="encryption.bin";
		enggFileName="engineering.doc";healthFileName="health.txt";
		legalFileName="legal.html";sourceCodeFileName="source_code.xls";

		if(suiteData.getSaasApp().equalsIgnoreCase("Office365MailBody")||
		   suiteData.getSaasApp().equalsIgnoreCase("Office365MailAttachmentBody")||
		   suiteData.getSaasApp().equalsIgnoreCase("GmailBody")||
		   suiteData.getSaasApp().equalsIgnoreCase("GmailAttachmentBody")){
			hipaaFileName="hipaa.txt";piiFileName="pii.txt";
			computingFileName="computing.txt";enggFileName="engineering.txt";
			sourceCodeFileName="source_code.html";designFileName="design_short.txt";
		}
		if(suiteData.getSaasApp().equalsIgnoreCase("Office365MailAttachment")||
				   suiteData.getSaasApp().equalsIgnoreCase("GmailAttachment")){
			hipaaFileName="hipaa.txt";piiFileName="pii.txt";
			computingFileName="computing.txt";enggFileName="engineering.txt";
			sourceCodeFileName="source_code.txt";designFileName="design_short.txt";
		}

		
		ferpaFileName = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_RISK_TYPES_PATH,
				ferpaFileName);
		glbaFileName = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_RISK_TYPES_PATH,
				glbaFileName);
		hipaaFileName = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_RISK_TYPES_PATH,
				hipaaFileName);
		pciFileName = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_RISK_TYPES_PATH,
				pciFileName);
		piiFileName = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_RISK_TYPES_PATH,
				piiFileName);
		vbaMacrosFileName = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_RISK_TYPES_PATH,
				vbaMacrosFileName);
		virusFileName = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_RISK_TYPES_PATH,
				virusFileName);
		businessFileName = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_CONTENT_TYPES_PATH,
				businessFileName);
		computingFileName = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_CONTENT_TYPES_PATH,
				computingFileName);
		digitalCertFileName = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_CONTENT_TYPES_PATH,
				digitalCertFileName);
		designFileName = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_CONTENT_TYPES_PATH,
				designFileName);
		encryptionFileName = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_CONTENT_TYPES_PATH,
				encryptionFileName);	
		enggFileName = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_CONTENT_TYPES_PATH,
				enggFileName);
		healthFileName = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_CONTENT_TYPES_PATH,
				healthFileName);
		legalFileName = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_CONTENT_TYPES_PATH,
				legalFileName);
		sourceCodeFileName = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_CONTENT_TYPES_PATH,
				sourceCodeFileName);
		
		/*audioFileName = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_CONTENT_TYPES_PATH,
				audioFileName);
		execFileName = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_CONTENT_TYPES_PATH,
				execFileName);
		imageFileName = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_CONTENT_TYPES_PATH,
				imageFileName);
		videoFileName = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_CONTENT_TYPES_PATH,
				videoFileName);*/
		
		largeFileName = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_TEMP_PATH,
				largeFileName);
		
		String[] fileName={encryptionFileName,ferpaFileName,glbaFileName,hipaaFileName,pciFileName,piiFileName,
				sourceCodeFileName,vbaMacrosFileName,virusFileName,
				businessFileName,computingFileName,digitalCertFileName,designFileName,
				enggFileName,healthFileName,legalFileName,largeFileName
				/*audioFileName,execFileName,imageFileName,videoFileName*/
		};	

		String[] fileName2={ferpaFileName,glbaFileName,hipaaFileName,pciFileName,piiFileName,sourceCodeFileName,
				businessFileName,computingFileName,digitalCertFileName,designFileName,enggFileName,healthFileName,legalFileName};
		
		
		if(suiteData.getSaasApp().equalsIgnoreCase("GmailBody")||
		   suiteData.getSaasApp().equalsIgnoreCase("GmailAttachmentBody")||
		   suiteData.getSaasApp().equalsIgnoreCase("Office365MailBody")||
		   suiteData.getSaasApp().equalsIgnoreCase("Office365MailAttachmentBody")){
			uploadId = dciFunctions.fileUploadOrSendEmail(suiteData, folderInfo, fileName2);
		}else{
			uploadId = dciFunctions.fileUploadOrSendEmail(suiteData, folderInfo, fileName);
		}
		
		mainCounter = 0;testCounter = DCIConstants.DCI_COUNTER_MAX;

	}

	@Priority(1)
	@Test(groups ={"All","Attachment","Body","AttachmentBody"})
	public void testFERPARisksForAFile() throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();

		fileUploadToSaasApp();

		String fileName = ferpaFileName;
		String saasType = SaasType.getSaasFilterType(suiteData.getSaasApp());

		Logger.info(
				"************************************ Starting the checking of FERPA risk type"
						+ " for filename:" + fileName + " and saas app type:" + saasType + " ******************");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description: Upload FERPA risk file and wait for few minutes, Verify the "
				+ "risk generated for the file type");
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output****************");
		Logger.info("Filename:"+fileName);Logger.info("Risk Types to be validated:"+Arrays.asList(dciFunctions.riskTypesForAFile(fileName)));
		Logger.info("Saas App Type:"+suiteData.getSaasApp());
		Logger.info("*****************************************************");

		List<NameValuePair> headers = dciFunctions.getBasicHeaders(suiteData);		
		verifyRiskTypesDisplayLogs(dciFunctions, fileName, saasType, headers);

		Logger.info(
				"************************************ Completed the checking of FERPA risk type"
						+ " for filename:" + fileName + " and saas app type:" + saasType + " ******************");

	}

	@Priority(2)
	@Test(groups ={"All","Attachment","Body","AttachmentBody"})
	public void testGLBARisksForAFile() throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();

		String fileName = glbaFileName;
		String saasType = SaasType.getSaasFilterType(suiteData.getSaasApp());

		Logger.info(
				"************************************ Starting the checking of GLBA risk type"
						+ " for filename:" + fileName + " and saas app type:" + saasType + " ******************");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description: Upload GLBA risk file and wait for few minutes, Verify the "
				+ "risk generated for the file type");
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output****************");
		Logger.info("Filename:"+fileName);Logger.info("Risk Types to be validated:"+Arrays.asList(dciFunctions.riskTypesForAFile(fileName)));
		Logger.info("Saas App Type:"+suiteData.getSaasApp());
		Logger.info("*****************************************************");

		List<NameValuePair> headers = dciFunctions.getBasicHeaders(suiteData);		
		verifyRiskTypesDisplayLogs(dciFunctions, fileName, saasType, headers);

		Logger.info(
				"************************************ Completed the checking of GLBA risk type"
						+ " for filename:" + fileName + " and saas app type:" + saasType + " ******************");

	}

	@Priority(3)
	@Test(groups ={"All","Attachment","Body","AttachmentBody"})
	public void testHIPAARisksForAFile() throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();

		String fileName = hipaaFileName;
		String saasType = SaasType.getSaasFilterType(suiteData.getSaasApp());

		Logger.info(
				"************************************ Starting the checking of HIPAA risk type"
						+ " for filename:" + fileName + " and saas app type:" + saasType + " ******************");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description: Upload HIPAA risk file and wait for few minutes, Verify the "
				+ "risk generated for the file type");
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output****************");
		Logger.info("Filename:"+fileName);Logger.info("Risk Types to be validated:"+Arrays.asList(dciFunctions.riskTypesForAFile(fileName)));
		Logger.info("Saas App Type:"+suiteData.getSaasApp());
		Logger.info("*****************************************************");

		List<NameValuePair> headers = dciFunctions.getBasicHeaders(suiteData);		
		verifyRiskTypesDisplayLogs(dciFunctions, fileName, saasType, headers);

		Logger.info(
				"************************************ Completed the checking of HIPAA risk type"
						+ " for filename:" + fileName + " and saas app type:" + saasType + " ******************");

	}

	@Priority(4)
	@Test(groups ={"All","Attachment","Body","AttachmentBody"})
	public void testPCIRisksForAFile() throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();
	
		String fileName = pciFileName;
		String saasType = SaasType.getSaasFilterType(suiteData.getSaasApp());

		Logger.info(
				"************************************ Starting the checking of PCI risk type"
						+ " for filename:" + fileName + " and saas app type:" + saasType + " ******************");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description: Upload PCI risk file and wait for few minutes, Verify the "
				+ "risk generated for the file type");
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output****************");
		Logger.info("Filename:"+fileName);Logger.info("Risk Types to be validated:"+Arrays.asList(dciFunctions.riskTypesForAFile(fileName)));
		Logger.info("Saas App Type:"+suiteData.getSaasApp());
		Logger.info("*****************************************************");

		List<NameValuePair> headers = dciFunctions.getBasicHeaders(suiteData);		
		verifyRiskTypesDisplayLogs(dciFunctions, fileName, saasType, headers);

		Logger.info(
				"************************************ Completed the checking of PCI risk type"
						+ " for filename:" + fileName + " and saas app type:" + saasType + " ******************");

	}

	@Priority(5)
	@Test(groups ={"All","Attachment","Body","AttachmentBody"})
	public void testPIIRisksForAFile() throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();

		String fileName = piiFileName;
		String saasType = SaasType.getSaasFilterType(suiteData.getSaasApp());

		Logger.info(
				"************************************ Starting the checking of PII risk type"
						+ " for filename:" + fileName + " and saas app type:" + saasType + " ******************");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description: Upload PII risk file and wait for few minutes, Verify the "
				+ "risk generated for the file type");
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output****************");
		Logger.info("Filename:"+fileName);Logger.info("Risk Types to be validated:"+Arrays.asList(dciFunctions.riskTypesForAFile(fileName)));
		Logger.info("Saas App Type:"+suiteData.getSaasApp());
		Logger.info("*****************************************************");

		List<NameValuePair> headers = dciFunctions.getBasicHeaders(suiteData);		
		verifyRiskTypesDisplayLogs(dciFunctions, fileName, saasType, headers);

		Logger.info(
				"************************************ Completed the checking of PII type"
						+ " for filename:" + fileName + " and saas app type:" + saasType + " ******************");

	}

	

	@Priority(6)
	@Test(groups ={"All","Attachment"})
	public void testVBAMacrosRisksForAFile() throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();

		String fileName = vbaMacrosFileName;
		String saasType = SaasType.getSaasFilterType(suiteData.getSaasApp());

		Logger.info(
				"************************************ Starting the checking of VBA Macros risk type"
						+ " for filename:" + fileName + " and saas app type:" + saasType + " ******************");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description: Upload VBA Macros risk file and wait for few minutes, Verify the "
				+ "risk generated for the file type");
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output****************");
		Logger.info("Filename:"+fileName);Logger.info("Risk Types to be validated:"+Arrays.asList(dciFunctions.riskTypesForAFile(fileName)));
		Logger.info("Saas App Type:"+suiteData.getSaasApp());
		Logger.info("*****************************************************");

		List<NameValuePair> headers = dciFunctions.getBasicHeaders(suiteData);		
		verifyRiskTypesDisplayLogs(dciFunctions, fileName, saasType, headers);

		Logger.info(
				"************************************ Completed the checking of VBA Macros risk type"
						+ " for filename:" + fileName + " and saas app type:" + saasType + " ******************");

	}

	@Priority(7)
	@Test(groups ={"All","Attachment"})
	public void testVirusMalwareRisksForAFile() throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();

		String fileName = virusFileName;
		String saasType = SaasType.getSaasFilterType(suiteData.getSaasApp());

		Logger.info(
				"************************************ Starting the checking of Virus/Malware risk type"
						+ " for filename:" + fileName + " and saas app type:" + saasType + " ******************");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description: Upload Virus/Malware risk file and wait for few minutes, Verify the "
				+ "risk generated for the file type");
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output****************");
		Logger.info("Filename:"+fileName);Logger.info("Risk Types to be validated:"+Arrays.asList(dciFunctions.riskTypesForAFile(fileName)));
		Logger.info("Saas App Type:"+suiteData.getSaasApp());
		Logger.info("*****************************************************");

		List<NameValuePair> headers = dciFunctions.getBasicHeaders(suiteData);		
		verifyRiskTypesDisplayLogs(dciFunctions, fileName, saasType, headers);

		Logger.info(
				"************************************ Completed the checking of Virus/Malware risk type"
						+ " for filename:" + fileName + " and saas app type:" + saasType + " ******************");

	}

	/*@Priority(8)
	@Test(groups ={"All","Attachment"})
	public void testAudioContentTypesForAFile() throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();

		String fileName = audioFileName;
		String saasType = SaasType.getSaasFilterType(suiteData.getSaasApp());

		Logger.info(
				"************************************ Starting the checking of audio content type"
						+ " for filename:" + fileName + " and saas app type:" + saasType + " ******************");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description: Upload audio content file and wait for few minutes, Verify the "
				+ "content type generated for the file type");
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output****************");
		Logger.info("Filename:"+fileName);Logger.info("Content Types to be validated:"+Arrays.asList(dciFunctions.riskTypesForAFile(fileName)));
		Logger.info("Saas App Type:"+suiteData.getSaasApp());
		Logger.info("*****************************************************");

		List<NameValuePair> headers = dciFunctions.getBasicHeaders(suiteData);		
		verifyContentTypesDisplayLogs(dciFunctions, fileName, saasType, headers);

		Logger.info(
				"************************************ Completed the checking of audio content type"
						+ " for filename:" + fileName + " and saas app type:" + saasType + " ******************");

	}*/


	@Priority(9)
	@Test(groups ={"All","Attachment","Body","AttachmentBody"})
	public void testBusinessContentTypesForAFile() throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();

		String fileName = businessFileName;
		String saasType = SaasType.getSaasFilterType(suiteData.getSaasApp());

		Logger.info(
				"************************************ Starting the checking of business content type"
						+ " for filename:" + fileName + " and saas app type:" + saasType + " ******************");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description: Upload business content file and wait for few minutes, Verify the "
				+ "content type generated for the file type");
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output****************");
		Logger.info("Filename:"+fileName);Logger.info("Content Types to be validated:"+Arrays.asList(dciFunctions.riskTypesForAFile(fileName)));
		Logger.info("Saas App Type:"+suiteData.getSaasApp());
		Logger.info("*****************************************************");

		List<NameValuePair> headers = dciFunctions.getBasicHeaders(suiteData);		
		verifyContentTypesDisplayLogs(dciFunctions, fileName, saasType, headers);

		Logger.info(
				"************************************ Completed the checking of business content type"
						+ " for filename:" + fileName + " and saas app type:" + saasType + " ******************");

	}

	@Priority(10)
	@Test(groups ={"All","Attachment","Body","AttachmentBody"})
	public void testComputingContentTypesForAFile() throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();

		String fileName = computingFileName;
		String saasType = SaasType.getSaasFilterType(suiteData.getSaasApp());

		Logger.info(
				"************************************ Starting the checking of computing content type"
						+ " for filename:" + fileName + " and saas app type:" + saasType + " ******************");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description: Upload computing content file and wait for few minutes, Verify the "
				+ "content type generated for the file type");
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output****************");
		Logger.info("Filename:"+fileName);Logger.info("Content Types to be validated:"+Arrays.asList(dciFunctions.riskTypesForAFile(fileName)));
		Logger.info("Saas App Type:"+suiteData.getSaasApp());
		Logger.info("*****************************************************");

		List<NameValuePair> headers = dciFunctions.getBasicHeaders(suiteData);		
		verifyContentTypesDisplayLogs(dciFunctions, fileName, saasType, headers);

		Logger.info(
				"************************************ Completed the checking of computing content type"
						+ " for filename:" + fileName + " and saas app type:" + saasType + " ******************");

	}


	@Priority(11)
	@Test(groups ={"All","Attachment","Body","AttachmentBody"})
	public void testCryptographicKeysContentTypesForAFile() throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();

		String fileName = digitalCertFileName;
		String saasType = SaasType.getSaasFilterType(suiteData.getSaasApp());

		Logger.info(
				"************************************ Starting the checking of cryptographic_keys content type"
						+ " for filename:" + fileName + " and saas app type:" + saasType + " ******************");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description: Upload cryptographic_keys content file and wait for few minutes, Verify the "
				+ "content type generated for the file type");
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output****************");
		Logger.info("Filename:"+fileName);Logger.info("Content Types to be validated:"+Arrays.asList(dciFunctions.riskTypesForAFile(fileName)));
		Logger.info("Saas App Type:"+suiteData.getSaasApp());
		Logger.info("*****************************************************");

		List<NameValuePair> headers = dciFunctions.getBasicHeaders(suiteData);		
		verifyContentTypesDisplayLogs(dciFunctions, fileName, saasType, headers);

		Logger.info(
				"************************************ Completed the checking of cryptographic_keys content type"
						+ " for filename:" + fileName + " and saas app type:" + saasType + " ******************");

	}


	@Priority(12)
	@Test(groups ={"All","Attachment","Body","AttachmentBody"})
	public void testDesignContentTypesForAFile() throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();

		String fileName = designFileName;
		String saasType = SaasType.getSaasFilterType(suiteData.getSaasApp());

		Logger.info(
				"************************************ Starting the checking of design content type"
						+ " for filename:" + fileName + " and saas app type:" + saasType + " ******************");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description: Upload design content file and wait for few minutes, Verify the "
				+ "content type generated for the file type");
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output****************");
		Logger.info("Filename:"+fileName);Logger.info("Content Types to be validated:"+Arrays.asList(dciFunctions.riskTypesForAFile(fileName)));
		Logger.info("Saas App Type:"+suiteData.getSaasApp());
		Logger.info("*****************************************************");

		List<NameValuePair> headers = dciFunctions.getBasicHeaders(suiteData);		
		verifyContentTypesDisplayLogs(dciFunctions, fileName, saasType, headers);

		Logger.info(
				"************************************ Completed the checking of design content type"
						+ " for filename:" + fileName + " and saas app type:" + saasType + " ******************");

	}

	@Priority(13)
	@Test(groups ={"All","Attachment"})
	public void testEncryptionRisksForAFile() throws Exception {

		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();

		String fileName = encryptionFileName;
		String saasType = SaasType.getSaasFilterType(suiteData.getSaasApp());

		Logger.info(
				"************************************ Starting the checking of encryption content type"
						+ " for filename:" + fileName + " and saas app type:" + saasType + " ******************");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description: Upload encryption content file and wait for few minutes, Verify the "
				+ "risk generated for the file type");
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output****************");
		Logger.info("Filename:"+fileName);Logger.info("Content Types to be validated:"+Arrays.asList(dciFunctions.riskTypesForAFile(fileName)));
		Logger.info("Saas App Type:"+suiteData.getSaasApp());
		Logger.info("*****************************************************");

		List<NameValuePair> headers = dciFunctions.getBasicHeaders(suiteData);		
		verifyContentTypesDisplayLogs(dciFunctions, fileName, saasType, headers);
		
		Logger.info(
				"************************************ Completed the checking of encryption content type"
						+ " for filename:" + fileName + " and saas app type:" + saasType + " ******************");

	}
	
	@Priority(14)
	@Test(groups ={"All","Attachment","Body","AttachmentBody"})
	public void testEngineeringContentTypesForAFile() throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();

		String fileName = enggFileName;
		String saasType = SaasType.getSaasFilterType(suiteData.getSaasApp());

		Logger.info(
				"************************************ Starting the checking of engineering content type"
						+ " for filename:" + fileName + " and saas app type:" + saasType + " ******************");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description: Upload engineering content file and wait for few minutes, Verify the "
				+ "content type generated for the file type");
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output****************");
		Logger.info("Filename:"+fileName);Logger.info("Content Types to be validated:"+Arrays.asList(dciFunctions.riskTypesForAFile(fileName)));
		Logger.info("Saas App Type:"+suiteData.getSaasApp());
		Logger.info("*****************************************************");

		List<NameValuePair> headers = dciFunctions.getBasicHeaders(suiteData);		
		verifyContentTypesDisplayLogs(dciFunctions, fileName, saasType, headers);

		Logger.info(
				"************************************ Completed the checking of engineering content type"
						+ " for filename:" + fileName + " and saas app type:" + saasType + " ******************");

	}

	/*@Priority(15)
	@Test(groups ={"All","Attachment"})
	public void testExecutableContentTypesForAFile() throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();

		String fileName = execFileName;
		String saasType = SaasType.getSaasFilterType(suiteData.getSaasApp());

		Logger.info(
				"************************************ Starting the checking of executable content type"
						+ " for filename:" + fileName + " and saas app type:" + saasType + " ******************");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description: Upload executable content file and wait for few minutes, Verify the "
				+ "content type generated for the file type");
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output****************");
		Logger.info("Filename:"+fileName);Logger.info("Content Types to be validated:"+Arrays.asList(dciFunctions.riskTypesForAFile(fileName)));
		Logger.info("Saas App Type:"+suiteData.getSaasApp());
		Logger.info("*****************************************************");

		List<NameValuePair> headers = dciFunctions.getBasicHeaders(suiteData);		
		verifyContentTypesDisplayLogs(dciFunctions, fileName, saasType, headers);

		Logger.info(
				"************************************ Completed the checking of executable content type"
						+ " for filename:" + fileName + " and saas app type:" + saasType + " ******************");

	}*/

	@Priority(16)
	@Test(groups ={"All","Attachment","Body","AttachmentBody"})
	public void testHealthContentTypesForAFile() throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();

		String fileName = healthFileName;
		String saasType = SaasType.getSaasFilterType(suiteData.getSaasApp());

		Logger.info(
				"************************************ Starting the checking of health content type"
						+ " for filename:" + fileName + " and saas app type:" + saasType + " ******************");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description: Upload health content file and wait for few minutes, Verify the "
				+ "content type generated for the file type");
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output****************");
		Logger.info("Filename:"+fileName);Logger.info("Content Types to be validated:"+Arrays.asList(dciFunctions.riskTypesForAFile(fileName)));
		Logger.info("Saas App Type:"+suiteData.getSaasApp());
		Logger.info("*****************************************************");

		List<NameValuePair> headers = dciFunctions.getBasicHeaders(suiteData);		
		verifyContentTypesDisplayLogs(dciFunctions, fileName, saasType, headers);

		Logger.info(
				"************************************ Completed the checking of health content type"
						+ " for filename:" + fileName + " and saas app type:" + saasType + " ******************");

	}


	/*@Priority(17)
	@Test(groups ={"All","Attachment"})
	public void testImageContentTypesForAFile() throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();

		String fileName = imageFileName;
		String saasType = SaasType.getSaasFilterType(suiteData.getSaasApp());

		Logger.info(
				"************************************ Starting the checking of image content type"
						+ " for filename:" + fileName + " and saas app type:" + saasType + " ******************");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description: Upload image content file and wait for few minutes, Verify the "
				+ "content type generated for the file type");
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output****************");
		Logger.info("Filename:"+fileName);Logger.info("Content Types to be validated:"+Arrays.asList(dciFunctions.riskTypesForAFile(fileName)));
		Logger.info("Saas App Type:"+suiteData.getSaasApp());
		Logger.info("*****************************************************");

		List<NameValuePair> headers = dciFunctions.getBasicHeaders(suiteData);		
		verifyContentTypesDisplayLogs(dciFunctions, fileName, saasType, headers);

		Logger.info(
				"************************************ Completed the checking of image content type"
						+ " for filename:" + fileName + " and saas app type:" + saasType + " ******************");

	}*/

	@Priority(18)
	@Test(groups ={"All","Attachment","Body","AttachmentBody"})
	public void testLegalContentTypesForAFile() throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();

		String fileName = legalFileName;
		String saasType = SaasType.getSaasFilterType(suiteData.getSaasApp());

		Logger.info(
				"************************************ Starting the checking of legal content type"
						+ " for filename:" + fileName + " and saas app type:" + saasType + " ******************");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description: Upload legal content file and wait for few minutes, Verify the "
				+ "content type generated for the file type");
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output****************");
		Logger.info("Filename:"+fileName);Logger.info("Content Types to be validated:"+Arrays.asList(dciFunctions.riskTypesForAFile(fileName)));
		Logger.info("Saas App Type:"+suiteData.getSaasApp());
		Logger.info("*****************************************************");

		List<NameValuePair> headers = dciFunctions.getBasicHeaders(suiteData);		
		verifyContentTypesDisplayLogs(dciFunctions, fileName, saasType, headers);

		Logger.info(
				"************************************ Completed the checking of legal content type"
						+ " for filename:" + fileName + " and saas app type:" + saasType + " ******************");

	}

	@Priority(19)
	@Test(groups ={"All","Attachment","Body","AttachmentBody"})
	public void testSourceCodeRisksForAFile() throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();

		String fileName = sourceCodeFileName;
		String saasType = SaasType.getSaasFilterType(suiteData.getSaasApp());

		Logger.info(
				"************************************ Starting the checking of Source Code content type"
						+ " for filename:" + fileName + " and saas app type:" + saasType + " ******************");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description: Upload Source Code Content file and wait for few minutes, Verify the "
				+ "risk generated for the file type");
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output****************");
		Logger.info("Filename:"+fileName);Logger.info("Content Types to be validated:"+Arrays.asList(dciFunctions.riskTypesForAFile(fileName)));
		Logger.info("Saas App Type:"+suiteData.getSaasApp());
		Logger.info("*****************************************************");

		List<NameValuePair> headers = dciFunctions.getBasicHeaders(suiteData);		
		verifyContentTypesDisplayLogs(dciFunctions, fileName, saasType, headers);

		Logger.info(
				"************************************ Completed the checking of Source Code content type"
						+ " for filename:" + fileName + " and saas app type:" + saasType + " ******************");

	}
	
	/*@Priority(20)
	@Test(groups ={"All","Attachment"})
	public void testVideoContentTypesForAFile() throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();

		String fileName = videoFileName;
		String saasType = SaasType.getSaasFilterType(suiteData.getSaasApp());

		Logger.info(
				"************************************ Starting the checking of video content type"
						+ " for filename:" + fileName + " and saas app type:" + saasType + " ******************");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description: Upload video content file and wait for few minutes, Verify the "
				+ "content type generated for the file type");
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output****************");
		Logger.info("Filename:"+fileName);Logger.info("Content Types to be validated:"+Arrays.asList(dciFunctions.riskTypesForAFile(fileName)));
		Logger.info("Saas App Type:"+suiteData.getSaasApp());
		Logger.info("*****************************************************");

		List<NameValuePair> headers = dciFunctions.getBasicHeaders(suiteData);		
		verifyContentTypesDisplayLogs(dciFunctions, fileName, saasType, headers);

		Logger.info(
				"************************************ Completed the checking of video content type"
						+ " for filename:" + fileName + " and saas app type:" + saasType + " ******************");

	}*/
	
	@Priority(21)
	@Test(groups ={"All"})
	public void testLargeFileUploadBypassLogs() throws Exception {
		dciFunctions = new DCIFunctions();
		esLogs = new ElasticSearchLogs();
		String fileName = largeFileName;
		String saasType = SaasType.getSaasFilterType(suiteData.getSaasApp());

		Logger.info(
				"************************************ Starting the checking of 100 MB file warning"
						+ " for filename:" + fileName + " and saas app type:" + saasType + " ******************");

		Logger.info("****************Test Case Description****************");
		Logger.info("Test Case Description: Upload 100 MB file and wait for few minutes, Verify the "
				+ "warning log generated for the file type");
		Logger.info("*****************************************************");
		Logger.info("****************Expected Output****************");
		Logger.info("Filename:"+fileName);
		Logger.info("Saas App Type:"+suiteData.getSaasApp());
		Logger.info("*****************************************************");

		List<NameValuePair> headers = dciFunctions.getBasicHeaders(suiteData);		
		verifyRiskTypesDisplayLogs(dciFunctions, fileName, saasType, headers);

		Logger.info(
				"************************************ Completed the checking of 100 MB file warning"
						+ " for filename:" + fileName + " and saas app type:" + saasType + " ******************");

	}


	/**********************************************TEST METHODS***********************************************/
	/**********************************************UTIL METHODS***********************************************/

	private void verifyRiskTypesDisplayLogs(DCIFunctions dciFunctions, String fileName, String saasType, List<NameValuePair> headers)
			throws Exception{
		String hits = "";
		
		int riskCount=1;
		if(suiteData.getSaasApp().contains("AttachmentBody")||
				suiteData.getSaasApp().contains("attachmentbody")){
			riskCount=2;
		}

		try {
			String payload = "";
			if(fileName.contains("100MB.doc")){
				payload = 
						dciFunctions.getSearchQueryForDCI(suiteData, dciFunctions.getMinusMinutesFromCurrentTime(1440) , 
								dciFunctions.getPlusMinutesFromCurrentTime(120), saasType, DCIConstants.CISourceType, 
								DCIConstants.CIWarningSeverityType, fileName, DCIConstants.CIFacilityType, DCIConstants.CIBypassActivityType);
			}else{
				payload = 
						dciFunctions.getSearchQueryForDCI(suiteData, dciFunctions.getMinusMinutesFromCurrentTime(1440) , 
								dciFunctions.getPlusMinutesFromCurrentTime(120), saasType, DCIConstants.CISourceType, 
								DCIConstants.CICriticalSeverityType, fileName, DCIConstants.CIFacilityType, DCIConstants.CIActivityType);
			}
			
			Logger.info("****************Input Payload****************");
			Logger.info(payload);
			Logger.info("*********************************************");

			hits = fetchDisplayLogsCounter(dciFunctions, headers, payload, DCIConstants.DCI_COUNTER_MAX, riskCount);

			Logger.info("****************Output Response****************");
			Logger.info(hits);
			Logger.info("***********************************************");

			Map<String, String> CIJson = new HashMap<String, String>();
			if(fileName.contains("100MB.doc")){
				CIJson = dciFunctions.
						populateContentInspectionJson(suiteData,fileName, riskCount, 
								DCIConstants.CIWarningSeverityType, DCIConstants.CISourceType, true);
			}else{
				CIJson = dciFunctions.
						populateContentInspectionJson(suiteData,fileName, riskCount, 
								"critical", "API", false);
			}
			
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

		int riskCount=1;
		if(suiteData.getSaasApp().contains("AttachmentBody")||
				suiteData.getSaasApp().contains("attachmentbody")){
			riskCount=2;
		}

		try {

			String payload = 
					dciFunctions.getSearchQueryForDCI(suiteData, dciFunctions.getMinusMinutesFromCurrentTime(1440) , 
							dciFunctions.getPlusMinutesFromCurrentTime(120), saasType, DCIConstants.CISourceType, 
							DCIConstants.CIInformationalSeverityType, fileName, DCIConstants.CIFacilityType, DCIConstants.CIActivityType);
			
			Logger.info("****************Input Payload****************");
			Logger.info(payload);
			Logger.info("*********************************************");

			hits = fetchDisplayLogsCounter(dciFunctions, headers, payload, DCIConstants.DCI_COUNTER_MAX, riskCount);

			Logger.info("****************Output Response****************");
			Logger.info(hits);
			Logger.info("***********************************************");

			Map<String, String> CIJson = dciFunctions.
					populateContentInspectionJson(suiteData,fileName, riskCount, 
							"informational", "API", false);
			
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
	 */
	@BeforeClass(groups ={"All","Attachment","Body","AttachmentBody"})
	public void deleteAllCIQProfiles() {
		dciFunctions = new DCIFunctions();
		dciFunctions.deleteAllCIQProfiles(restClient, suiteData);
	}

	/**
	 * Delete all CIQ profiles
	 */
	//@BeforeClass(groups ={"All","Attachment","Body","AttachmentBody"})
	public void deleteAllPolicies() {
		dciFunctions = new DCIFunctions();
		try {
			HttpResponse listPolicies = dciFunctions.listPolicies(restClient, dciFunctions.getCookieHeaders(suiteData), 
					suiteData.getScheme(), suiteData.getHost());
			dciFunctions.deleteAllPolicies(restClient, listPolicies, dciFunctions.getCookieHeaders(suiteData), 
					suiteData.getScheme(), suiteData.getHost());

		} catch (Exception ex) {
			Logger.info("Issue with Deleting of all policies" + ex.getLocalizedMessage());
		}
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
			s3.downloadFileFromS3Bucket(DCIConstants.DCI_S3_BUCKET, "DCI"+File.separator+largeFileName, 
					DCIConstants.DCI_FILE_TEMP_PATH+File.separator+largeFileName);
		} catch (Exception ex) {
			Logger.info("Downloading file from S3 is failed with exception " + ex.getLocalizedMessage());
		}
	}


	/**********************************************BEFORE/AFTER CLASS*****************************************/


}
