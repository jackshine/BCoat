package com.elastica.beatle.tests.securlets.salesforce;

import java.util.ArrayList;

import org.testng.annotations.DataProvider;

import com.elastica.beatle.i18n.I18N;

public class SalesforceDataProvider {
	
	
	@DataProvider(name = "fileExposuresProvider")
    public static Object[][] fileExposures()
    {	
        return new Object[][] { 
        						
		{ "Hello.java", "source_code", "true", "open"},
		
        };
    }
	
	@DataProvider(name = "ExportFormat")
    public static Object[][] ExportFormat()
    {	
        return new Object[][] { 
        						
		{ "cef" }, { "csv" }, { "leef" }
		
        };
    }
	
	
	
    @DataProvider(name = "sharingType")
    public static Object[][] dataProviderMethod()
    {	
        return new Object[][] { 
        	{ "V"}, {"C"}

        };
    }
    
    @DataProvider(name = "ActivityTypeFilter")
    public static Object[][] ActivityTypeFilter() {
    	return new Object[][] { 
    		
    		{"Download"}, {"Delete"}, {"Upload"}, {"Share"}, {"Unshare"}, {"Login"}, {"Change Collaboration Role"}, 
    		{"Rename"}, {"Edit"}, {"Undelete"}, {"Transfer Ownership"}, {"Move"}, {"Copy"}, {"Unsync"}, {"Add"}, {"Create"},
    		{"Delete"}, {"Lock"}, {"Unlock"}, {"Remove"}, {"Accept Collaboration"}, {"Content Inspection"}, {"Expire Sharing"}
    		
    	};
    }
    
    
    
    @DataProvider(name = "ObjectTypeFilter")
    public static Object[][] ObjectTypeFilter() {
    	return new Object[][] { 
    		{"File"}, {"Folder"}, {"Session"}, {"Web_Link"}, {"Session"}, {"User"}, {"Group"}, {"Unknown Device"}
    	};
    }
    
    @DataProvider(name = "SeverityFilter")
    public static Object[][] SeverityFilter() {
    	return new Object[][] { 
    		{"informational"}, {"critical"}
    	};
    }
    
    @DataProvider(name = "LocationFilter")
    public static Object[][] LocationFilter() {
    	return new Object[][] { 
    		{"informational", "Boardman (United States)"}, {"informational", "San Jose (United States)"}
    	};
    }
    
    @DataProvider(name = "ExportFilter")
    public static Object[][] ExportFilter()
    {	
        
    	//exposuretype, objectType, risk, ciq, contentType, fileType, exportType, searchText, isInternal
    	return new Object[][] { 
    		{ "ext_count", null, "pii", null, null, "WORDPROCESSOR", "docs", "", true}, 					
    		{ "public", null, "pii", null, null, "WORDPROCESSOR", "docs", "", true}, 
    		{ "all_internal", null, "hipaa", null, null, null, "docs", "", true}, 
    		{ "public", null, "pii", null, "computing", "WORDPROCESSOR", "docs", "", true}, 
    		{ null, null, null, null, "legal", null, "docs", "", true},
    		{ null, null, null, null, null, null, "docs", "English", true},
        };
    }
    
    @DataProvider(name = "ExportUsersFilter")
    public static Object[][] ExportUsersFilter()
    {	
        
    	//exposuretype, objectType, risk, ciq, contentType, fileType, exportType, searchText, isInternal
    	return new Object[][] { 
//    		{ null, null, null, null, null, null, "users", "", true}, 
    		{ "public", null, null, null, null, "WORDPROCESSOR", "users", "", true},
//    		{ null, null, null, null, null, null, "ext_collabs", "", true},
    		
        };
    }
    
    @DataProvider(name = "RiskyFilesFilter")
    public static Object[][] RiskyFilesFilter()
    {	
        
    	//exposuretype, objectType, risk, ciq, contentType, fileType, exportType, searchText, isInternal
    	return new Object[][] { 
    		{ null, null, null, null, null, null, "risky_docs", "hipaa", true},
    		/*{ "ext_count", "OneDrive", "pii", null, null, null, "risky_docs", "", true}, 					
    		{ "public", "OneDrive", "pii", null, null, "rtf", "docs", "", true}, 
    		{ "all_internal", "OneDrive", "pci", null, null, null, "docs", "", true}, 
    		{ "public", "OneDrive", "pii", null, "source_code", "java", "docs", "", true}, 
    		{ null, "OneDrive", null, null, "legal", null, "docs", "", true},
    		{ null, "OneDrive", null, null, null, null, "docs", "java", true},*/
        };
    }
    
    @DataProvider(name = "I18NString")
    public static Object[][] I18NString()
    {	
    	//locale, filenames
        return new Object[][] { 
			//Collaborator , role
			{ "ES_ES",  I18N.getString("language", "es_es")},
			//{ "HI_IN",  I18N.getString("language", "hi_in")},
			{ "UR_IN",  I18N.getString("language", "ur_in")},
			//{ "TA_IN",  I18N.getString("language", "ta_in")},
			{ "ZH_CN",  I18N.getString("language", "zh_cn")},
			{ "JA_JP",  I18N.getString("language", "ja_jp")},
			{ "FR_FR",  I18N.getString("language", "fr_fr")},
			{ "PT_BR",  I18N.getString("language", "pt_br")},
			{ "DE_DE",  I18N.getString("language", "de_de")},
			
        };
    }
    
}
