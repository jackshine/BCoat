package com.elastica.beatle.protect.dataprovider;

import org.testng.annotations.DataProvider;

public class SalesforceDataProvider {
	
	/*
	 * 1. Policy Name
	 * 2. Policy Description
	 * 3. PolicyType or SaaS app sharing
	 * 4. Cloud Service name
	 * 5. Exposure Type
	 * 6. File Owner Username
	 * 7. File Owner Group name
	 * 8. Domain Name
	 * 9. File Owner User Exception
	 * 10. File Owner Group Exception
	 * 11. File Shared Username
	 * 12. File Shared Groupname
	 * 13. File Shared Username Exception
	 * 14. File Shared Groupname Exception
	 * 15. File Extension
	 * 16. File Name
	 * 17. File Type
	 * 18. File Size
	 * 19. Risk Type
	 * 20. ContentIQ
	 * 21. Remediation
	 * 
	 */
	
	
	@DataProvider(name = "RemediationsCombination")
	public static Object[][] getSalesforceRemediationCombinationDataProvider() {
		return new Object[][] {
			new String[] { "SFREMCOMB1", "Policy Desc", "shareFilePublic,shareFileExternallyEdit", "Salesforce", "public,External", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "deleteSharedLink,removeCollaborator" },
			new String[] { "SFREMCOMB2", "Policy Desc", "shareFilePublic,shareFileExternallyView", "Salesforce", "public,External", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "deleteSharedLink,removeCollaborator" },
			new String[] { "SFREMCOMB3", "Policy Desc", "shareFilePublic,shareFileExternallyEdit", "Salesforce", "public,External", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "deleteSharedLink,updateCollaboratorView" },
			new String[] { "SFREMCOMB4", "Policy Desc", "shareFilePublic,shareFileExternallyView", "Salesforce", "public,External", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "deleteSharedLink,updateCollaboratorCollaborate" },
			
			new String[] { "SFREMCOMB5", "Policy Desc", "shareFileInternallyView,shareFileExternallyView", "Salesforce", "public,External", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "internalUserCollaborate,removeCollaborator" },
			new String[] { "SFREMCOMB6", "Policy Desc", "shareFileInternallyCollab,shareFileExternallyView", "Salesforce", "public,External", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "internalUserView,removeCollaborator" },
			
			new String[] { "SFREMCOMB7", "Policy Desc", "shareFileInternallyView,shareFileExternallyEdit", "Salesforce", "public,External", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "internalUserCollaborate,removeCollaborator" },
			new String[] { "SFREMCOMB8", "Policy Desc", "shareFileInternallyCollab,shareFileExternallyEdit", "Salesforce", "public,External", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "internalUserView,removeCollaborator" },
			
			new String[] { "SFREMCOMB9", "Policy Desc", "shareFileInternallyView,shareFileExternallyView", "Salesforce", "public,External", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "internalUserCollaborate,updateCollaboratorCollaborate" },
			new String[] { "SFREMCOMB10", "Policy Desc", "shareFileInternallyView,shareFileExternallyEdit", "Salesforce", "public,External", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "internalUserCollaborate,updateCollaboratorView" },
			
			new String[] { "SFREMCOMB11", "Policy Desc", "shareFileInternallyCollab,shareFileExternallyView", "Salesforce", "public,External", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "internalUserView,updateCollaboratorCollaborate" },
			new String[] { "SFREMCOMB12", "Policy Desc", "shareFileInternallyCollab,shareFileExternallyEdit", "Salesforce", "public,External", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "internalUserView,updateCollaboratorView" },
		};
	}
	
	@DataProvider(name = "Remediations")
	public static Object[][] getSalesforceRemediationDataProvider() {
		return new Object[][] {
			
			new String[] { "SFUNSHARE_01", "Policy Desc", "shareFilePublic", "Salesforce", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "deleteSharedLink" },
			new String[] { "SFUNSHARE_02", "Policy Desc", "shareFileInternallyCollab", "Salesforce", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "deleteSharedLink" },
			new String[] { "SFUNSHARE_03", "Policy Desc", "shareFileInternallyView", "Salesforce", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "deleteSharedLink" },
			new String[] { "SFUNSHARE_04", "Policy Desc", "shareFileExternallyView", "Salesforce", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "deleteSharedLink" },
			new String[] { "SFUNSHARE_05", "Policy Desc", "shareFileExternallyEdit", "Salesforce", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "deleteSharedLink" },
			
			new String[] { "SF_INT_COLLOB", "Policy Desc", "shareFileInternallyView", "Salesforce", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "internalUserCollaborate" },
			new String[] { "SF_INT_VIEW", "Policy Desc", "shareFileInternallyCollab", "Salesforce", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "internalUserView" },
			
			new String[] { "SF_REM_COLLAB_01", "Policy Desc", "shareFileExternallyView", "Salesforce", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "removeCollaborator" },
			new String[] { "SF_REM_COLLAB_02", "Policy Desc", "shareFileExternallyEdit", "Salesforce", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "removeCollaborator" },
			
			new String[] { "SF_EXT_COLLAB", "Policy Desc", "shareFileExternallyView", "Salesforce", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "updateCollaboratorCollaborate" },
			new String[] { "SF_EXT_VIEW", "Policy Desc", "shareFileExternallyEdit", "Salesforce", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "updateCollaboratorView" },
		};
	}
	
	@DataProvider(name = "CIQException")
	public static Object[][] getSalesforceContentIQExceptionDataProvider() {
		return new Object[][] {
			new String[] { "SF_CIQ_EXCEPTION", "Policy Desc", "shareFilePublic", "Salesforce", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "no", "no", "deleteSharedLink", "PII_Exception", "PII", "DCI_RISK" },
		};
	}
	
	@DataProvider(name = "Sanity")
	public static Object[][] getSalesforceSanityDataProvider() {
		return new Object[][] {
				new String[] { "SF_SANITY_UNSHARE", "Policy Desc", "shareFilePublic", "Salesforce", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "deleteSharedLink" },
			};
	}
	
	@DataProvider(name = "UserGroup")
	public static Object[][] getSalesforceUserGroupPolicyViolationsDataProvider() {
		return new Object[][] {
			new String[] { "SF_USER_01", "Policy Desc", "shareFileInternalUser7", "Salesforce", "unexposed", "any", "any", "any", "no", "no", "securletuser7@securletbeatle.com", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "no", "yes" },
			new String[] { "SF_USER_02", "Policy Desc", "shareFileInternalUser7", "Salesforce", "unexposed", "any", "any", "any", "no", "no", "any", "any", "securletuser8@securletbeatle.com", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "no", "yes" },
			new String[] { "SF_USER_03", "Policy Desc", "shareFileExternalUser1", "Salesforce", "external", "admin@securletbeatle.com", "any", "any", "no", "no", "protectauto1@protectbeatle.com", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "no", "yes" },
			new String[] { "SF_USER_04", "Policy Desc", "shareFileExternalUser1", "Salesforce", "external", "admin@securletbeatle.com", "any", "any", "no", "no", "any", "any", "protectauto2@protectbeatle.com", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "no", "yes" },
			new String[] { "SF_USER_05", "Policy Desc", "shareFileExternalUser1", "Salesforce", "external", "admin@securletbeatle.com", "any", "any", "no", "no", "any", "any", "protectauto1@protectbeatle.com", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "no", "no" },
			new String[] { "SF_USER_06", "Policy Desc", "shareFileExternalUser1", "Salesforce", "external", "any", "any", "any", "admin@securletbeatle.com", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "no", "no" },
			
			//new String[] { "SF_GROUP_01", "Policy Desc", "internalGroup", "Salesforce", "unexposed", "any", "any", "any", "no", "no", "any", "Protect Group All", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "no", "yes" },
		};
	}
	
	@DataProvider(name = "FileSize")
	public static Object[][] getSalesforceFileSizePolicyViolationsDataProvider() {
		return new Object[][] {
			new String[] { "SF_FILE_SIZE_01", "Policy Desc", "shareFilePublic", "Salesforce", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "zip", "no", "zip", "0,1048576", "Virus", "Virus", "no", "no" },
			new String[] { "SF_FILE_SIZE_02", "Policy Desc", "shareFilePublic", "Salesforce", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "zip", "no", "zip", "0,2097152", "Virus", "Virus", "no", "yes" },
			new String[] { "SF_FILE_SIZE_03", "Policy Desc", "shareFilePublic", "Salesforce", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "zip", "no", "zip", "0,3145728", "Virus", "Virus", "no", "yes" },
			new String[] { "SF_FILE_SIZE_04", "Policy Desc", "shareFilePublic", "Salesforce", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "zip", "no", "zip", "1048576,2097152", "Virus", "Virus", "no", "yes" },
			new String[] { "SF_FILE_SIZE_05", "Policy Desc", "shareFilePublic", "Salesforce", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "zip", "no", "zip", "2097152,3145728", "Virus", "Virus", "no", "no" },
			new String[] { "SF_FILE_SIZE_06", "Policy Desc", "shareFilePublic", "Salesforce", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "exe", "no", "no", "4194304,5242880", "no", "no", "no", "yes" },
			new String[] { "SF_FILE_SIZE_07", "Policy Desc", "shareFilePublic", "Salesforce", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "exe", "no", "no", "5242880,6291456", "no", "no", "no", "no" },
			new String[] { "SF_FILE_SIZE_08", "Policy Desc", "shareFilePublic", "Salesforce", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "exe", "no", "no", "4194304,6291456", "no", "no", "no", "yes" },
		};
	}
	
	@DataProvider(name = "SpecialCharacter")
	public static Object[][] getSalesforceSpecialCharacterPolicyViolationsDataProvider() {
		return new Object[][] {
			new String[] { "SF_SPECIALCHAR_01", "Policy Desc", "shareFilePublic", "Salesforce", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PII", "SC_PII", "no", "yes" },
			new String[] { "SF_SPECIALCHAR_02", "Policy Desc", "shareFilePublic", "Salesforce", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PII", "SC_PII", "no", "yes" },
			new String[] { "SF_SPECIALCHAR_03", "Policy Desc", "shareFilePublic", "Salesforce", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PII", "SC_PII", "no", "yes" },
			new String[] { "SF_SPECIALCHAR_04", "Policy Desc", "shareFilePublic", "Salesforce", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PII", "SC_PII", "no", "yes" },
			new String[] { "SF_SPECIALCHAR_05", "Policy Desc", "shareFilePublic", "Salesforce", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PII", "SC_PII", "no", "yes" },
			new String[] { "SF_SPECIALCHAR_06", "Policy Desc", "shareFilePublic", "Salesforce", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PII", "SC_PII", "no", "yes" },
			new String[] { "SF_SPECIALCHAR_07", "Policy Desc", "shareFilePublic", "Salesforce", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PII", "SC_PII", "no", "yes" },
			new String[] { "SF_SPECIALCHAR_08", "Policy Desc", "shareFilePublic", "Salesforce", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PII", "SC_PII", "no", "yes" },
			new String[] { "SF_SPECIALCHAR_09", "Policy Desc", "shareFilePublic", "Salesforce", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PII", "SC_PII", "no", "yes" },
		};
	}
	
	@DataProvider(name = "ContentType")
	public static Object[][] getSalesforceContentTypePolicyViolationsDataProvider() {
		return new Object[][] {
			new String[] { "SF_CONTENT_01", "Policy Desc", "shareFilePublic", "Salesforce", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "docx", "no", "business", "0,0", "Business", "CT_Business", "no", "yes" },
			new String[] { "SF_CONTENT_02", "Policy Desc", "shareFilePublic", "Salesforce", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "doc", "no", "computing", "0,0", "Computing", "CT_Computing", "no", "yes" },
			new String[] { "SF_CONTENT_03", "Policy Desc", "shareFilePublic", "Salesforce", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "doc", "no", "cryptographic", "0,0", "Cryptographic_keys", "CT_Cryptographic", "no", "yes" },
			new String[] { "SF_CONTENT_04", "Policy Desc", "shareFilePublic", "Salesforce", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "pdf", "no", "design", "0,0", "Design Doc", "CT_Design", "no", "yes" },
			new String[] { "SF_CONTENT_05", "Policy Desc", "shareFilePublic", "Salesforce", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "bin", "no", "encryption", "0,0", "Encryption", "CT_Encryption", "no", "yes" },
			new String[] { "SF_CONTENT_06", "Policy Desc", "shareFilePublic", "Salesforce", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "doc", "no", "engineering", "0,0", "Engineering", "CT_Engineering", "no", "yes" },
			new String[] { "SF_CONTENT_07", "Policy Desc", "shareFilePublic", "Salesforce", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "health", "0,0", "Health", "CT_Health", "no", "yes" },
			new String[] { "SF_CONTENT_08", "Policy Desc", "shareFilePublic", "Salesforce", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "html", "no", "legal", "0,0", "Legal", "CT_Legal", "no", "yes" },
			new String[] { "SF_CONTENT_09", "Policy Desc", "shareFilePublic", "Salesforce", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "java", "no", "sourcecode", "0,0", "Source_code", "CT_Sourcecode", "no", "yes" },
			
			new String[] { "SF_CONTENT_10", "Policy Desc", "shareFilePublic", "Salesforce", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "docx", "no", "business", "0,0", "Business,Computing", "CT_Business_Computing", "no", "yes" },
			new String[] { "SF_CONTENT_11", "Policy Desc", "shareFilePublic", "Salesforce", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "health", "0,0", "Health,Legal", "CT_Health_Legal", "no", "yes" },
			
			new String[] { "SF_CONTENT_12", "Policy Desc", "shareFilePublic", "Salesforce", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "docx", "no", "business", "0,0", "Computing", "NEG_CT_Computing", "no", "no" },
			new String[] { "SF_CONTENT_13", "Policy Desc", "shareFilePublic", "Salesforce", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "doc", "no", "computing", "0,0", "Business", "NEG_CT_Business", "no", "no" },
			new String[] { "SF_CONTENT_14", "Policy Desc", "shareFilePublic", "Salesforce", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "doc", "no", "cryptographic", "0,0", "Design Doc", "NEG_CT_Design", "no", "no" },
			new String[] { "SF_CONTENT_15", "Policy Desc", "shareFilePublic", "Salesforce", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "pdf", "no", "design", "0,0", "Cryptographic_keys", "NEG_CT_Cryptographic", "no", "no" },
		};
	}
	
	@DataProvider(name = "ContentProfileRisks")
	public static Object[][] getSalesforceRisksPolicyViolationsDataProvider() {
		return new Object[][] {
			new String[] { "SF_RISKS_01", "Policy Desc", "shareFilePublic", "Salesforce", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "zip", "no", "zip", "0,0", "Virus", "ZIP_VIRUS", "no", "yes" },
			new String[] { "SF_RISKS_02", "Policy Desc", "shareFilePublic", "Salesforce", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "RAR_VIRUS", "no", "yes" },
			new String[] { "SF_RISKS_03", "Policy Desc", "shareFilePublic", "Salesforce", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PII", "TXT_PII", "no", "yes" },
			new String[] { "SF_RISKS_04", "Policy Desc", "shareFilePublic", "Salesforce", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PCI", "TXT_PCI", "no", "yes" },
			new String[] { "SF_RISKS_05", "Policy Desc", "shareFilePublic", "Salesforce", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "GLBA", "TXT_GLBA", "no", "yes" },
			new String[] { "SF_RISKS_06", "Policy Desc", "shareFilePublic", "Salesforce", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "no", "rtf", "0,0", "PII", "RTF_PII", "no", "yes" },
			new String[] { "SF_RISKS_07", "Policy Desc", "shareFilePublic", "Salesforce", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "no", "rtf", "0,0", "PCI", "RTF_PCI", "no", "yes" },
			new String[] { "SF_RISKS_08", "Policy Desc", "shareFilePublic", "Salesforce", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "pdf", "no", "pdf", "0,0", "HIPAA", "PDF_HIPAA", "no", "yes" },
			new String[] { "SF_RISKS_09", "Policy Desc", "shareFilePublic", "Salesforce", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xlsm", "no", "xlsm", "0,0", "VBA_Macros", "XLSM_VBAM", "no", "yes" },
			new String[] { "SF_RISKS_10", "Policy Desc", "shareFilePublic", "Salesforce", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "FERPA", "TXT_FERPA", "no", "yes" },
			
			new String[] { "SF_RISKS_11", "Policy Desc", "shareFilePublic", "Salesforce", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PII,PCI,FERPA", "TXT_PCI_PII_FERPA", "no", "yes" },
			new String[] { "SF_RISKS_12", "Policy Desc", "shareFilePublic", "Salesforce", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PII,PCI,GLBA", "TXT_PCI_PII_GLBA", "no", "yes" },
			new String[] { "SF_RISKS_13", "Policy Desc", "shareFilePublic", "Salesforce", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "GLBA,FERPA", "TXT_FERPA_GLBA", "no", "yes" },
			new String[] { "SF_RISKS_14", "Policy Desc", "shareFilePublic", "Salesforce", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "no", "rtf", "0,0", "PII,PCI,FERPA", "RTF_PCI_PII_FERPA", "no", "yes" },
			new String[] { "SF_RISKS_15", "Policy Desc", "shareFilePublic", "Salesforce", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "no", "rtf", "0,0", "PII,PCI,GLBA", "RTF_PCI_PII_GLBA", "no", "yes" },
			new String[] { "SF_RISKS_16", "Policy Desc", "shareFilePublic", "Salesforce", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "no", "rtf", "0,0", "GLBA,FERPA", "RTF_FERPA_GLBA", "no", "yes" },
			
			new String[] { "SF_RISKS_17", "Policy Desc", "shareFilePublic", "Salesforce", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "no", "rtf", "0,0", "GLBA,FERPA,Virus", "VIRUS_FERPA_GLBA", "no", "yes" },
			new String[] { "SF_RISKS_18", "Policy Desc", "shareFilePublic", "Salesforce", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "no", "rtf", "0,0", "GLBA,FERPA,VBA_Macros", "VBA_FERPA_GLBA", "no", "yes" },
			new String[] { "SF_RISKS_19", "Policy Desc", "shareFilePublic", "Salesforce", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xlsm", "no", "xlsm", "0,0", "VBA_Macros,PCI,PII", "VBA_PCI_PII", "no", "yes" },
			new String[] { "SF_RISKS_20", "Policy Desc", "shareFilePublic", "Salesforce", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "zip", "no", "zip", "0,0", "Virus,PCI,PII", "VIRUS_PCI_PII", "no", "yes" },
			
			new String[] { "SF_RISKS_21", "Policy Desc", "shareFilePublic", "Salesforce", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "zip", "no", "zip", "0,0", "PII", "NEG_PII", "no", "no" },
			new String[] { "SF_RISKS_22", "Policy Desc", "shareFilePublic", "Salesforce", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "zip", "no", "zip", "0,0", "PCI", "NEG_PCI", "no", "no" },
			new String[] { "SF_RISKS_23", "Policy Desc", "shareFilePublic", "Salesforce", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "HIPAA", "NEG_HIPAA", "no", "no" },
			new String[] { "SF_RISKS_24", "Policy Desc", "shareFilePublic", "Salesforce", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "FERPA", "NEG_FERPA", "no", "no" },
			new String[] { "SF_RISKS_25", "Policy Desc", "shareFilePublic", "Salesforce", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "VBA_Macros", "NEG_VBA", "no", "no" },
			new String[] { "SF_RISKS_26", "Policy Desc", "shareFilePublic", "Salesforce", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "no", "rtf", "0,0", "Virus", "NEG_VIRUS", "no", "no" },
			new String[] { "SF_RISKS_27", "Policy Desc", "shareFilePublic", "Salesforce", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "pdf", "no", "pdf", "0,0", "DLP", "NEG_DLP", "no", "no" },
		};
	}
	
	@DataProvider(name = "ExposureCombination")
	public static Object[][] getSalesforceExposureCombinationsDataProvider() {
		return new Object[][] {
			
			new String[] { "SFEXPOSURE1", "Policy Desc", "shareFilePublic", "Salesforce", "public,internal,any", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "no", "yes" },
			new String[] { "SFEXPOSURE2", "Policy Desc", "shareFilePublic,shareFileInternallyView", "Salesforce", "public,internal,all", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "no", "yes" },
			new String[] { "SFEXPOSURE3", "Policy Desc", "shareFilePublic,shareFileInternallyView", "Salesforce", "public,external,all", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "no", "no" },
			
			new String[] { "SFEXPOSURE4", "Policy Desc", "shareFilePublic,shareFileInternallyView", "Salesforce", "public,internal,any", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "no", "yes" },
			new String[] { "SFEXPOSURE5", "Policy Desc", "shareFilePublic,shareFileInternallyView", "Salesforce", "internal,external,any", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "no", "yes" },
			new String[] { "SFEXPOSURE6", "Policy Desc", "shareFilePublic,shareFileInternallyView", "Salesforce", "public,external,any", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "no", "yes" },
			
			new String[] { "SFEXPOSURE7", "Policy Desc", "shareFilePublic,shareFileExternallyView", "Salesforce", "public,internal,all", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "no", "no" },
			new String[] { "SFEXPOSURE8", "Policy Desc", "shareFilePublic,shareFileExternallyView", "Salesforce", "internal,external,all", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "no", "no" },
			new String[] { "SFEXPOSURE9", "Policy Desc", "shareFilePublic,shareFileExternallyView", "Salesforce", "public,external,all", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "no", "yes" },
			
			new String[] { "SFEXPOSURE10", "Policy Desc", "shareFilePublic,shareFileExternallyView", "Salesforce", "public,internal,any", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "no", "yes" },
			new String[] { "SFEXPOSURE11", "Policy Desc", "shareFilePublic,shareFileExternallyView", "Salesforce", "internal,external,any", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "no", "yes" },
			new String[] { "SFEXPOSURE12", "Policy Desc", "shareFilePublic,shareFileExternallyView", "Salesforce", "public,external,any", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "no", "yes" },
			
			new String[] { "SFEXPOSURE13", "Policy Desc", "shareFilePublic,shareFileInternallyView,shareFileExternallyView", "Salesforce", "public,internal,all", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "no", "yes" },
			new String[] { "SFEXPOSURE14", "Policy Desc", "shareFilePublic,shareFileInternallyView,shareFileExternallyView", "Salesforce", "internal,external,all", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "no", "yes" },
			new String[] { "SFEXPOSURE15", "Policy Desc", "shareFilePublic,shareFileInternallyView,shareFileExternallyView", "Salesforce", "public,external,all", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "no", "yes" },
			new String[] { "SFEXPOSURE16", "Policy Desc", "shareFilePublic,shareFileInternallyView,shareFileExternallyView", "Salesforce", "public,internal,external,all", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "no", "yes" },
			
			new String[] { "SFEXPOSURE17", "Policy Desc", "shareFilePublic,shareFileInternallyView,shareFileExternallyView", "Salesforce", "public,internal,any", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "no", "yes" },
			new String[] { "SFEXPOSURE18", "Policy Desc", "shareFilePublic,shareFileInternallyView,shareFileExternallyView", "Salesforce", "internal,external,any", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "no", "yes" },
			new String[] { "SFEXPOSURE19", "Policy Desc", "shareFilePublic,shareFileInternallyView,shareFileExternallyView", "Salesforce", "public,external,any", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "no", "yes" },
			new String[] { "SFEXPOSURE20", "Policy Desc", "shareFilePublic,shareFileInternallyView,shareFileExternallyView", "Salesforce", "public,internal,external,any", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "no", "yes" },
			
			new String[] { "SFEXPOSURE21", "Policy Desc", "shareFileMultipleInternalUserCollab,shareFileMultipleExternalUserCollab", "Salesforce", "internal,external,any", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "no", "yes" },
			new String[] { "SFEXPOSURE22", "Policy Desc", "shareFileInternallyView,shareFileMultipleExternalUserCollab", "Salesforce", "internal,external,all", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "no", "yes" },
			new String[] { "SFEXPOSURE23", "Policy Desc", "shareFileMultipleInternalUserCollab,shareFileMultipleExternalUserCollab", "Salesforce", "public,internal,external,all", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "no", "no" },
			new String[] { "SFEXPOSURE24", "Policy Desc", "shareFileMultipleInternalUserCollab,shareFileMultipleExternalUserCollab", "Salesforce", "public,internal,external,any", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "no", "yes" },
			new String[] { "SFEXPOSURE25", "Policy Desc", "shareFileMultipleInternalUserCollab,shareFileMultipleExternalUserCollab", "Salesforce", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "no", "no" },
			
			new String[] { "SFEXPOSURE26", "Policy Desc", "shareFileMultipleInternalUserCollab", "Salesforce", "unexposed", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "no", "yes" },
			new String[] { "SFEXPOSURE27", "Policy Desc", "shareFileInternallyView", "Salesforce", "internal,unexposed,any", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "no", "yes" },
			
		};
	}
}