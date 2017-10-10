/**
 *
 */
package com.elastica.beatle.dci;

import java.io.File;

/**
 * DCI Constants
 * @author eldorajan
 *
 */
public class DCIConstants {

	public static final String DCI_API_CONFIGURATION_FILEPATH = "/src/test/resources/dci/configurations/APIConfigurations.xml";

	public static final String DCI_S3_BUCKET = "elastica-qa-automation";

	public static final String DCI_FILE_PATH = System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+"resources"+
			File.separator+"dci";
	public static final String DCI_FILE_TEMP_PATH = DCI_FILE_PATH+File.separator+"temp";
	public static final String DCI_FILE_TEMP_S3_PATH = DCI_FILE_TEMP_PATH+File.separator+"DCI";

	public static final String DCI_FILE_UPLOAD_GS_PATH = DCI_FILE_TEMP_S3_PATH+File.separator+"GoldenSet";
	public static final String DCI_FILE_UPLOAD_FILE_CLASS_PATH = DCI_FILE_TEMP_S3_PATH+File.separator+"FileFormat"+File.separator+"FileClass";
	public static final String DCI_FILE_UPLOAD_FILE_FORMAT_COMBO_PATH = DCI_FILE_TEMP_S3_PATH+File.separator+"FileFormat"+File.separator+"Combinations";
	public static final String DCI_FILE_UPLOAD_CONTENT_PATH = DCI_FILE_TEMP_S3_PATH+File.separator+"Content";
	public static final String DCI_FILE_UPLOAD_CIQ_PATH = DCI_FILE_TEMP_S3_PATH+File.separator+"CIQ";
	public static final String DCI_FILE_UPLOAD_TRAINING_PROFILE_PATH = DCI_FILE_TEMP_S3_PATH+File.separator+"TrainingProfiles";

	public static final String DCI_TP_UPLOAD_PATH = DCI_FILE_UPLOAD_TRAINING_PROFILE_PATH+File.separator+"Categories";

	public static final String DCI_FILE_UPLOAD_AUDIO_PATH     = DCI_FILE_UPLOAD_CONTENT_PATH+File.separator+"audio";
	public static final String DCI_FILE_UPLOAD_BUSINESS_PATH  = DCI_FILE_UPLOAD_CONTENT_PATH+File.separator+"business";
	public static final String DCI_FILE_UPLOAD_COMPUTING_PATH = DCI_FILE_UPLOAD_CONTENT_PATH+File.separator+"computing";
	public static final String DCI_FILE_UPLOAD_CRYPTO_PATH    = DCI_FILE_UPLOAD_CONTENT_PATH+File.separator+"cryptographic_keys";
	public static final String DCI_FILE_UPLOAD_DESIGN_PATH    = DCI_FILE_UPLOAD_CONTENT_PATH+File.separator+"design";
	public static final String DCI_FILE_UPLOAD_ENGINEERING_PATH = DCI_FILE_UPLOAD_CONTENT_PATH+File.separator+"engineering";
	public static final String DCI_FILE_UPLOAD_EXEC_PATH 		= DCI_FILE_UPLOAD_CONTENT_PATH+File.separator+"executable";
	public static final String DCI_FILE_UPLOAD_HEALTH_PATH 		= DCI_FILE_UPLOAD_CONTENT_PATH+File.separator+"health";
	public static final String DCI_FILE_UPLOAD_IMAGE_PATH 		= DCI_FILE_UPLOAD_CONTENT_PATH+File.separator+"image";
	public static final String DCI_FILE_UPLOAD_LEGAL_PATH 		= DCI_FILE_UPLOAD_CONTENT_PATH+File.separator+"legal";
	public static final String DCI_FILE_UPLOAD_VIDEO_PATH 		= DCI_FILE_UPLOAD_CONTENT_PATH+File.separator+"video";

	public static final String DCI_FILE_UPLOAD_CIQ_DIC_PATH 		 = DCI_FILE_UPLOAD_CIQ_PATH+File.separator+"Dictionaries";
	public static final String DCI_FILE_UPLOAD_CIQ_TERMS_PATH 		 = DCI_FILE_UPLOAD_CIQ_PATH+File.separator+"Terms";
	public static final String DCI_FILE_UPLOAD_CIQ_TERMS_HS_PATH 	 = DCI_FILE_UPLOAD_CIQ_PATH+File.separator+"TermsHS";
	public static final String DCI_FILE_UPLOAD_CIQ_TERMS_NEGATIVE_PATH = DCI_FILE_UPLOAD_CIQ_PATH+File.separator+"TermsNegative";
	public static final String DCI_FILE_UPLOAD_CIQ_CUSTOM_TERMS_PATH = DCI_FILE_UPLOAD_CIQ_PATH+File.separator+"CustomTerms";
	public static final String DCI_FILE_UPLOAD_CIQ_EXPOSURE_PATH 	 = DCI_FILE_UPLOAD_CIQ_PATH+File.separator+"Exposure";
	public static final String DCI_FILE_UPLOAD_CIQ_P1_TERMS_PATH 	 = DCI_FILE_UPLOAD_CIQ_PATH+File.separator+"P1Terms";
	public static final String DCI_FILE_UPLOAD_CIQ_P2_TERMS_PATH 	 = DCI_FILE_UPLOAD_CIQ_PATH+File.separator+"P2Terms";
	public static final String DCI_FILE_UPLOAD_CIQ_CC_PATH 			 = DCI_FILE_UPLOAD_CIQ_PATH+File.separator+"CompanyConfidential";
	public static final String DCI_FILE_UPLOAD_CIQ_FL_PATH 			 = DCI_FILE_UPLOAD_CIQ_PATH+File.separator+"ForeignLanguage";
	public static final String DCI_FILE_UPLOAD_CIQ_TITUS_PATH 		 = DCI_FILE_UPLOAD_CIQ_PATH+File.separator+"Titus";
	public static final String DCI_FILE_UPLOAD_CIQ_RISKS_PATH        = DCI_FILE_UPLOAD_CIQ_PATH+File.separator+"RiskTypes"+File.separator+"Risks";
	public static final String DCI_FILE_UPLOAD_CIQ_PREDEF_DICT_RISKS_PATH  = DCI_FILE_UPLOAD_CIQ_PATH+File.separator+"RiskTypes"+File.separator+"Risks_PDD";
	public static final String DCI_FILE_UPLOAD_CIQ_PREDEF_TERMS_RISKS_PATH = DCI_FILE_UPLOAD_CIQ_PATH+File.separator+"RiskTypes"+File.separator+"Risks_PDT";
	public static final String DCI_FILE_UPLOAD_CIQ_CUSTOM_DICT_RISKS_PATH  = DCI_FILE_UPLOAD_CIQ_PATH+File.separator+"RiskTypes"+File.separator+"Risks_CD";
	public static final String DCI_FILE_UPLOAD_CIQ_CUSTOM_TERMS_RISKS_PATH = DCI_FILE_UPLOAD_CIQ_PATH+File.separator+"RiskTypes"+File.separator+"Risks_CT";
	public static final String DCI_FILE_UPLOAD_CIQ_RISKS_ONLY_PATH         = DCI_FILE_UPLOAD_CIQ_PATH+File.separator+"RiskTypes"+File.separator+"Risks_Only";
	public static final String DCI_FILE_UPLOAD_CIQRISKS_ONLY_PATH 		   = DCI_FILE_UPLOAD_CIQ_PATH+File.separator+"RiskTypes"+File.separator+"CIQ_Only";

	public static final String DCI_FILE_UPLOAD_CIQ_CONTENT_PATH 			 = DCI_FILE_UPLOAD_CIQ_PATH+File.separator+"ContentTypes"+File.separator+"Content";
	public static final String DCI_FILE_UPLOAD_CIQ_PREDEF_DICT_CONTENT_PATH  = DCI_FILE_UPLOAD_CIQ_PATH+File.separator+"ContentTypes"+File.separator+"Content_PDD";
	public static final String DCI_FILE_UPLOAD_CIQ_PREDEF_TERMS_CONTENT_PATH = DCI_FILE_UPLOAD_CIQ_PATH+File.separator+"ContentTypes"+File.separator+"Content_PDT";
	public static final String DCI_FILE_UPLOAD_CIQ_CUSTOM_DICT_CONTENT_PATH  = DCI_FILE_UPLOAD_CIQ_PATH+File.separator+"ContentTypes"+File.separator+"Content_CD";
	public static final String DCI_FILE_UPLOAD_CIQ_CUSTOM_TERMS_CONTENT_PATH = DCI_FILE_UPLOAD_CIQ_PATH+File.separator+"ContentTypes"+File.separator+"Content_CT";
	public static final String DCI_FILE_UPLOAD_CIQ_CONTENT_ONLY_PATH 		 = DCI_FILE_UPLOAD_CIQ_PATH+File.separator+"ContentTypes"+File.separator+"Content_Only";
	public static final String DCI_FILE_UPLOAD_CIQCONTENT_ONLY_PATH 		 = DCI_FILE_UPLOAD_CIQ_PATH+File.separator+"ContentTypes"+File.separator+"CIQ_Only";

	public static final String DCI_FILE_UPLOAD_CIQ_RISKS_P2_PATH 	  = DCI_FILE_UPLOAD_CIQ_PATH+File.separator+"RiskContentTypes"+File.separator+"Risks";
	public static final String DCI_FILE_UPLOAD_CIQ_CONTENT_P2_PATH 	  = DCI_FILE_UPLOAD_CIQ_PATH+File.separator+"RiskContentTypes"+File.separator+"Content";
	public static final String DCI_FILE_UPLOAD_CIQ_RISKS_CONTENT_PATH = DCI_FILE_UPLOAD_CIQ_PATH+File.separator+"RiskContentCIQ";

	public static final String DCI_FILE_UPLOAD_CIQ_PDT_PDD_CC_PATH 	= DCI_FILE_UPLOAD_CIQ_PATH+File.separator+"PDT_PDD"+File.separator+"Company_Confidential";
	public static final String DCI_FILE_UPLOAD_CIQ_PDT_PDD_DIS_PATH = DCI_FILE_UPLOAD_CIQ_PATH+File.separator+"PDT_PDD"+File.separator+"Diseases";
	public static final String DCI_FILE_UPLOAD_CIQ_PDT_PDD_ENE_PATH = DCI_FILE_UPLOAD_CIQ_PATH+File.separator+"PDT_PDD"+File.separator+"Energy";
	public static final String DCI_FILE_UPLOAD_CIQ_PDT_PDD_GAM_PATH = DCI_FILE_UPLOAD_CIQ_PATH+File.separator+"PDT_PDD"+File.separator+"Gambling";
	public static final String DCI_FILE_UPLOAD_CIQ_PDT_PDD_ID_PATH  = DCI_FILE_UPLOAD_CIQ_PATH+File.separator+"PDT_PDD"+File.separator+"Illegal_Drugs";
	public static final String DCI_FILE_UPLOAD_CIQ_PDT_PDD_OBS_PATH = DCI_FILE_UPLOAD_CIQ_PATH+File.separator+"PDT_PDD"+File.separator+"Obscenities";
	public static final String DCI_FILE_UPLOAD_CIQ_PDT_PDD_PD_PATH  = DCI_FILE_UPLOAD_CIQ_PATH+File.separator+"PDT_PDD"+File.separator+"Pharmaceutical_Drugs";
	public static final String DCI_FILE_UPLOAD_CIQ_PDT_PDD_TS_PATH  = DCI_FILE_UPLOAD_CIQ_PATH+File.separator+"PDT_PDD"+File.separator+"Ticker_Symbols";
	public static final String DCI_FILE_UPLOAD_CIQ_PDT_PDD_USG_PATH = DCI_FILE_UPLOAD_CIQ_PATH+File.separator+"PDT_PDD"+File.separator+"USG_Export_Controlled_Items";
	public static final String DCI_FILE_UPLOAD_CIQ_PDT_PDD_VIO_PATH = DCI_FILE_UPLOAD_CIQ_PATH+File.separator+"PDT_PDD"+File.separator+"Violence";
	public static final String DCI_FILE_UPLOAD_CIQ_i18n_PATH = DCI_FILE_UPLOAD_CIQ_PATH+File.separator+"i18n";



	public static final String DCI_FILE_UPLOAD_SANITY_PATH 				= DCI_FILE_PATH+File.separator+"uploadRisks"+File.separator+"Sanity";
	public static final String DCI_FILE_UPLOAD_CONTENT_TYPES_PATH 		= DCI_FILE_PATH+File.separator+"uploadRisks"+File.separator+"ContentTypes";
	public static final String DCI_FILE_UPLOAD_RISK_TYPES_PATH 			= DCI_FILE_PATH+File.separator+"uploadRisks"+File.separator+"RiskTypes";
	public static final String DCI_FILE_UPLOAD_RISK_CONTENT_TYPES_PATH  = DCI_FILE_PATH+File.separator+"uploadRisks"+File.separator+"RiskContentTypes";
	public static final String DCI_FILE_UPLOAD_SOURCE_CODE_PATH 		= DCI_FILE_PATH+File.separator+"uploadRisks"+File.separator+"SourceCode";
	public static final String DCI_FILE_UPLOAD_PCI_POS_PATH      		= DCI_FILE_PATH+File.separator+"uploadRisks"+File.separator+"PCI"+File.separator+"Positive";
	public static final String DCI_FILE_UPLOAD_PCI_NEG_PATH     		= DCI_FILE_PATH+File.separator+"uploadRisks"+File.separator+"PCI"+File.separator+"Negative";
	public static final String DCI_FILE_UPLOAD_PCI_P2_FILES_PATH 		= DCI_FILE_PATH+File.separator+"uploadRisks"+File.separator+"PCI"+File.separator+"P2Files";
	public static final String DCI_FILE_UPLOAD_PII_POS_PATH 			= DCI_FILE_PATH+File.separator+"uploadRisks"+File.separator+"PII"+File.separator+"Positive";
	public static final String DCI_FILE_UPLOAD_PII_NEG_PATH 			= DCI_FILE_PATH+File.separator+"uploadRisks"+File.separator+"PII"+File.separator+"Negative";
	public static final String DCI_FILE_UPLOAD_PII_P2_FILES_PATH 		= DCI_FILE_PATH+File.separator+"uploadRisks"+File.separator+"PII"+File.separator+"P2Files";
	public static final String DCI_FILE_UPLOAD_PII_FRENCH               = DCI_FILE_PATH+File.separator+"uploadRisks"+File.separator+"European_Files"+File.separator+"France_PII_Files";
	public static final String DCI_FILE_UPLOAD_PII_GERMAN               = DCI_FILE_PATH+File.separator+"uploadRisks"+File.separator+"European_Files"+File.separator+"Germany_PII_Files";
	public static final String DCI_FILE_UPLOAD_PII_UK                   = DCI_FILE_PATH+File.separator+"uploadRisks"+File.separator+"European_Files"+File.separator+"UK_PII_Files";
	public static final String DCI_FILE_UPLOAD_PCI_PII_NEW_POS_PATH     = DCI_FILE_PATH+File.separator+"uploadRisks"+File.separator+"PCI_PII"+File.separator+"New_Req"+File.separator+"Positive";
	public static final String DCI_FILE_UPLOAD_PCI_PII_NEW_NEG_PATH     = DCI_FILE_PATH+File.separator+"uploadRisks"+File.separator+"PCI_PII"+File.separator+"New_Req"+File.separator+"Negative";
	public static final String DCI_FILE_UPLOAD_PII_ADDRESS_PATH 		= DCI_FILE_PATH+File.separator+"uploadRisks"+File.separator+"PCI_PII"+File.separator+"Address_Formats";
	public static final String DCI_FILE_UPLOAD_PII_PCI_266_PATH 		= DCI_FILE_PATH+File.separator+"uploadRisks"+File.separator+"PII_PCI_266";
	public static final String DCI_FILE_UPLOAD_PII_PCI_267_PATH 		= DCI_FILE_PATH+File.separator+"uploadRisks"+File.separator+"PII_PCI_267";
	public static final String DCI_FILE_UPLOAD_PII_PCI_269_PATH 		= DCI_FILE_PATH+File.separator+"uploadRisks"+File.separator+"PII_PCI_269";
	public static final String DCI_FILE_UPLOAD_PCI_PII_PATH 			= DCI_FILE_PATH+File.separator+"uploadRisks"+File.separator+"PCI_PII"+File.separator+"Positive";
	public static final String DCI_FILE_UPLOAD_PCI_PATCH_PATH 			= DCI_FILE_PATH+File.separator+"uploadRisks"+File.separator+"PCI"+File.separator+"Patch_Testing_29268";
	public static final String DCI_FILE_UPLOAD_PII_FP_CHINESE           = DCI_FILE_PATH+File.separator+"uploadRisks"+File.separator+"ForeignLanguage"+File.separator+"Chinese";
	public static final String DCI_FILE_UPLOAD_PII_FP_DUTCH             = DCI_FILE_PATH+File.separator+"uploadRisks"+File.separator+"ForeignLanguage"+File.separator+"Dutch";
	public static final String DCI_FILE_UPLOAD_PII_FP_FRENCH 			= DCI_FILE_PATH+File.separator+"uploadRisks"+File.separator+"ForeignLanguage"+File.separator+"French";
	public static final String DCI_FILE_UPLOAD_PII_FP_GERMAN 			= DCI_FILE_PATH+File.separator+"uploadRisks"+File.separator+"ForeignLanguage"+File.separator+"German";
	public static final String DCI_FILE_UPLOAD_PII_FP_JAPANESE 			= DCI_FILE_PATH+File.separator+"uploadRisks"+File.separator+"ForeignLanguage"+File.separator+"Japanese";
	public static final String DCI_FILE_UPLOAD_PII_FP_KOREAN 			= DCI_FILE_PATH+File.separator+"uploadRisks"+File.separator+"ForeignLanguage"+File.separator+"Korean";
	public static final String DCI_FILE_UPLOAD_PII_FP_SPANISH 			= DCI_FILE_PATH+File.separator+"uploadRisks"+File.separator+"ForeignLanguage"+File.separator+"Spanish";
	public static final String DCI_FILE_UPLOAD_HIPAA_PATH 				= DCI_FILE_PATH+File.separator+"uploadRisks"+File.separator+"HIPAA"+File.separator+"Positive";
	public static final String DCI_FILE_UPLOAD_HIPAA_HEALTH_PATH 		= DCI_FILE_PATH+File.separator+"uploadRisks"+File.separator+"HIPAA"+File.separator+"Health";
	public static final String DCI_FILE_UPLOAD_HIPAA_LEGAL_PATH 		= DCI_FILE_PATH+File.separator+"uploadRisks"+File.separator+"HIPAA"+File.separator+"Legal";
	public static final String DCI_FILE_UPLOAD_HIPAA_HEALTH_LEGAL_PATH 	= DCI_FILE_PATH+File.separator+"uploadRisks"+File.separator+"HIPAA"+File.separator+"Health_Legal";
	public static final String DCI_FILE_UPLOAD_HIPAA_NEGATIVE_PATH 		= DCI_FILE_PATH+File.separator+"uploadRisks"+File.separator+"HIPAA"+File.separator+"Negative";
	public static final String DCI_FILE_UPLOAD_HIPAA_FILES_PATH 		= DCI_FILE_PATH+File.separator+"uploadRisks"+File.separator+"HIPAA"+File.separator+"Hipaa";
	public static final String DCI_FILE_UPLOAD_ENCRYPTION_COMPRESSED_PATH 		= DCI_FILE_PATH+File.separator+"uploadRisks"+File.separator+"Encryption"+File.separator+"Positive"+File.separator+"Compressed";
	public static final String DCI_FILE_UPLOAD_ENCRYPTION_COMPRESSED_FILES_PATH = DCI_FILE_PATH+File.separator+"uploadRisks"+File.separator+"Encryption"+File.separator+"Positive"+File.separator+"CompressedFiles";
	public static final String DCI_FILE_UPLOAD_ENCRYPTION_MAC_FILES_PATH 		= DCI_FILE_PATH+File.separator+"uploadRisks"+File.separator+"Encryption"+File.separator+"Positive"+File.separator+"Mac";
	public static final String DCI_FILE_UPLOAD_ENCRYPTION_WINDOWS_FILES_PATH 	= DCI_FILE_PATH+File.separator+"uploadRisks"+File.separator+"Encryption"+File.separator+"Positive"+File.separator+"MSDocuments";
	public static final String DCI_FILE_UPLOAD_ENCRYPTION_OPENOFFICE_FILES_PATH = DCI_FILE_PATH+File.separator+"uploadRisks"+File.separator+"Encryption"+File.separator+"Positive"+File.separator+"OpenOffice";
	public static final String DCI_FILE_UPLOAD_ENCRYPTION_OTHERS_FILES_PATH 	= DCI_FILE_PATH+File.separator+"uploadRisks"+File.separator+"Encryption"+File.separator+"Positive"+File.separator+"Others";
	public static final String DCI_FILE_UPLOAD_ENCRYPTION_PICTURE_FILES_PATH 	= DCI_FILE_PATH+File.separator+"uploadRisks"+File.separator+"Encryption"+File.separator+"Positive"+File.separator+"Pictures";
	public static final String DCI_FILE_UPLOAD_ENCRYPTION_NEGATIVE_PATH 		= DCI_FILE_PATH+File.separator+"uploadRisks"+File.separator+"Encryption"+File.separator+"Negative";
	public static final String DCI_FILE_UPLOAD_VIRUS_PATH 						= DCI_FILE_PATH+File.separator+"uploadRisks"+File.separator+"Virus";
	public static final String DCI_FILE_UPLOAD_FERPA_PATH 						= DCI_FILE_PATH+File.separator+"uploadRisks"+File.separator+"FERPA";
	public static final String DCI_FILE_UPLOAD_GLBA_PATH 						= DCI_FILE_PATH+File.separator+"uploadRisks"+File.separator+"GLBA"+File.separator+"Positive";
	public static final String DCI_FILE_UPLOAD_GLBA_NEGATIVE_PATH 				= DCI_FILE_PATH+File.separator+"uploadRisks"+File.separator+"GLBA"+File.separator+"Negative";
	public static final String DCI_FILE_UPLOAD_VBA_PATH 						= DCI_FILE_PATH+File.separator+"uploadRisks"+File.separator+"VBAMacros"+File.separator+"Positive";
	public static final String DCI_FILE_UPLOAD_VBA_NEGATIVE_PATH 				= DCI_FILE_PATH+File.separator+"uploadRisks"+File.separator+"VBAMacros"+File.separator+"Negative";
	public static final String DCI_RISK_FOREIGN_LANG_UPLOAD_PATH				= DCI_FILE_PATH+File.separator+"uploadRisks"+File.separator+"European_Files";

	public static final String DCI_TP_FERPA_COMBO_UPLOAD_PATH = DCI_FILE_PATH+File.separator+"uploadTrainingProfiles"+ File.separator+"Combinations"+File.separator+"ferpa";
	public static final String DCI_TP_GLBA_COMBO_UPLOAD_PATH  = DCI_FILE_PATH+File.separator+"uploadTrainingProfiles"+ File.separator+"Combinations"+File.separator+"glba";
	public static final String DCI_TP_HIPAA_COMBO_UPLOAD_PATH = DCI_FILE_PATH+File.separator+"uploadTrainingProfiles"+ File.separator+"Combinations"+File.separator+"hipaa";
	public static final String DCI_TP_PCI_COMBO_UPLOAD_PATH   = DCI_FILE_PATH+File.separator+"uploadTrainingProfiles"+ File.separator+"Combinations"+File.separator+"pci";
	public static final String DCI_TP_PII_COMBO_UPLOAD_PATH   = DCI_FILE_PATH+File.separator+"uploadTrainingProfiles"+ File.separator+"Combinations"+File.separator+"pii";
	public static final String DCI_FILE_UPLOAD_CIQ_FILE_CLASS = DCI_FILE_PATH+File.separator+"uploadRisks"+File.separator+"FileClass";
	public static final String DCI_FILE_UPLOAD_STRESS_PATH    = DCI_FILE_PATH+File.separator+"uploadStress";
	public static final String DCI_FILE_EXCEPTION_PATH        = DCI_FILE_PATH+File.separator+"risks"+File.separator+"exception.txt";
	public static final String DCI_FILE_UPLOAD_PATH 		  = System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+"resources"+
			File.separator+"uploads";
	
	public static final String DCI_FILE_UPLOAD_CIQ_DICT_KEYWORD_PATH    = DCI_FILE_PATH+File.separator+"uploadCIQ"+File.separator+"Dictionary_Keywords";
	public static final String DCI_FILE_UPLOAD_CIQ_DICT_FILE_PATH    = DCI_FILE_PATH+File.separator+"uploadCIQ"+File.separator+"Dictionary_Files";
	
	public static final String FRAMEWORK_CONFIGURATION_FILEPATH = "/src/test/resources/dci/configurations/dciConfigurations.xml";

	public static final String DCI_FOLDER = "DCI_BE_Automation";

	public static final int DCI_COUNTER_MAX_UL = 20;
	public static final int DCI_COUNTER_MAX = 15;
	public static final int DCI_COUNTER_MEDIUM = 10;
	public static final int DCI_COUNTER_MEDIUM_LL = 5;
	public static final int DCI_COUNTER_CIQ = 2;
	public static final int DCI_COUNTER_MIN = 1;

	public static final int DCI_COUNTER_UL = 20;
	public static final int DCI_COUNTER_ML = 10;
	public static final int DCI_COUNTER_LL = 1;

	public static final String CISourceType = "API";
	public static final String CICriticalSeverityType = "critical";
	public static final String CIInformationalSeverityType = "informational";
	public static final String CIWarningSeverityType = "warning";
	public static final String CIFacilityType = "investigate";

	public static final String CIActivityType = "Content Inspection";
	public static final String CIBypassActivityType = "Content Inspection Bypass";

	public static final String DCI_CIQ_NAME = "ContentIQProfileName";
	public static final String DCI_CIQ_DESC = "ContentIQProfileDescription";
	
	public static final String responseOKMessage="HTTP/1.1 200 OK";
	public static final String ciqCreateSuccessMessage="{\"action_status\": true, \"api_message\": \"\"}";

	public static final String POLICY_TYPE = "policy_type";
	public static final String POLICY_ID = "id";
	public static final String POLICY_SUB_ID = "sub_id";
	public static final String SUCCESS = "success";
	public static final String ACTION_STATUS = "action_status";
	public static final String CREATED_BY = "created_by";
	public static final String MODIFIED_BY = "modified_by";
	public static final String EXPOSURE_TYPE = "exposureType";

	
}
