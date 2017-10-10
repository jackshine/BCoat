package com.elastica.beatle.securlets;

import java.util.ArrayList;

import org.testng.annotations.DataProvider;

import com.elastica.beatle.i18n.I18N;

public class BoxDataProvider {
	
	
	
	@DataProvider(name = "InternalExternalProvider")
    public static Object[][] InternalExternalProvider()
    {	
        return new Object[][] { 
        						
		{ true }, { false }
		
        };
    }
	
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
	
	
	
    @DataProvider(name = "sharedLinkDataProvider")
    public static Object[][] dataProviderMethod()
    {	
    	
    	
        return new Object[][] { 
        						//violation, 	is_internal, exposure, 	remedial action,  remedial access, expectedResult, Service to call
        						
        						{ "source_code", "true", "open", 			"UNSHARE", 		null, 		null, "APIServer" },
        						{ "source_code", "true", "company", 		"UNSHARE", 		null, 		null, "APIServer" },
        						{ "source_code", "true", "collaborators", 	"UNSHARE", 		null, 		null, "APIServer" },
        						{ "source_code", "true", "open", 			"SHARE_ACCESS", "company", "company", "APIServer" },
        						{ "source_code", "true", "open", 			"SHARE_ACCESS", "collaborators", "collaborators", "APIServer" },
        						{ "source_code", "true", "company", 		"SHARE_ACCESS", "collaborators", "collaborators", "APIServer" },
        						{ "source_code", "true", "company", 	  	"SHARE_ACCESS", "open", 	"open", 	"APIServer" },
        						{ "source_code", "true", "collaborators", 	"SHARE_ACCESS", "company", 	"company", 	"APIServer" },
        						{ "source_code", "true", "collaborators", 	"SHARE_ACCESS", "open", 	"open", 	"APIServer" },
        						{ "source_code", "true", "open", 		  	"SHARE_EXPIRE", "5", 		"5",   "APIServer"},
        						{ "source_code", "true", "company", 	  	"SHARE_EXPIRE", "1", 		"1",	"APIServer"},
        						{ "source_code", "true", "collaborators", 	"SHARE_EXPIRE", "15", 		"15",   "APIServer"}

        };
    }
    
    
    
    @DataProvider(name = "InternalCollaborationDataProvider")
    public static Object[][] dataProviderForCollaborationRole()
    {	
    	//editor, viewer, previewer, uploader, previewer uploader, viewer uploader, co-owner, or owner
    	ArrayList<String> users = new ArrayList<String>();
    	users.add("pushpan@gmail.com");
    	users.add("t.pushparaj@gmail.com");
    	
    	ArrayList<String> roles = new ArrayList<String>();
    	roles.add("editor");
    	roles.add("viewer");
    	
    	ArrayList<String> remedialRoles = new ArrayList<String>();
    	roles.add("uploader");
    	roles.add("previewer");
    	
    	
        return new Object[][] { 
        	//testname, 	collaborators, currentRoles, 	remedialaction,  remedialRoles, expectedRole, Service to call
        	//Internal collaborator role change					
        	{ "Change Internal Collaborator role", "box-admin+1@securletbeatle.com", "editor", "COLLAB_UPDATE", "viewer", "viewer", 	false },
        	{ "Change Internal Collaborator role", "box-admin+1@securletbeatle.com", "viewer", "COLLAB_UPDATE", "editor", "editor", 	false },
			{ "Change Internal Collaborator role", "box-admin+1@securletbeatle.com", "editor", "COLLAB_UPDATE", "previewer", "previewer", 	false },
			{ "Change Internal Collaborator role", "box-admin+1@securletbeatle.com", "previewer", "COLLAB_UPDATE", "editor", "editor", 	false },
			{ "Change Internal Collaborator role", "box-admin+1@securletbeatle.com", "editor", "COLLAB_UPDATE", "uploader", "uploader",	false },
			{ "Change Internal Collaborator role", "box-admin+1@securletbeatle.com", "uploader", "COLLAB_UPDATE", "editor", "editor", 	false },
			{ "Change Internal Collaborator role", "box-admin+1@securletbeatle.com", "editor", "COLLAB_UPDATE", "previewer uploader", "previewer uploader", false },
			{ "Change Internal Collaborator role", "box-admin+1@securletbeatle.com", "previewer uploader", "COLLAB_UPDATE", "editor", "editor", 	false },
			{ "Change Internal Collaborator role", "box-admin+1@securletbeatle.com", "editor", "COLLAB_UPDATE", "viewer uploader", "viewer uploader", false },
			{ "Change Internal Collaborator role", "box-admin+1@securletbeatle.com", "viewer uploader", "COLLAB_UPDATE", "editor", "editor", 	false },
			{ "Change Internal Collaborator role", "box-admin+1@securletbeatle.com", "editor", "COLLAB_UPDATE", "co-owner", "co-owner", false },
			{ "Change Internal Collaborator role", "box-admin+1@securletbeatle.com", "co-owner", "COLLAB_UPDATE", "editor", "editor", 	false },
			
        	{ "Change Internal Collaborator role", "box-admin+1@securletbeatle.com", "editor", "COLLAB_UPDATE", "viewer", "viewer", 	false },
        	{ "Change Internal Collaborator role", "box-admin+1@securletbeatle.com", "viewer", "COLLAB_UPDATE", "previewer", "previewer", 	false },
        	{ "Change Internal Collaborator role", "box-admin+1@securletbeatle.com", "previewer", "COLLAB_UPDATE", "viewer", "viewer", 	false },
        	{ "Change Internal Collaborator role", "box-admin+1@securletbeatle.com", "viewer", "COLLAB_UPDATE", "uploader", "uploader",	false },
        	{ "Change Internal Collaborator role", "box-admin+1@securletbeatle.com", "uploader", "COLLAB_UPDATE", "viewer", "viewer", 	false },
        	{ "Change Internal Collaborator role", "box-admin+1@securletbeatle.com", "viewer", "COLLAB_UPDATE", "previewer uploader", "previewer uploader", false },
        	{ "Change Internal Collaborator role", "box-admin+1@securletbeatle.com", "previewer uploader", "COLLAB_UPDATE", "viewer", "viewer", 	false },
			{ "Change Internal Collaborator role", "box-admin+1@securletbeatle.com", "viewer", "COLLAB_UPDATE", "viewer uploader", "viewer uploader", false },
			{ "Change Internal Collaborator role", "box-admin+1@securletbeatle.com", "viewer uploader", "COLLAB_UPDATE", "viewer", "viewer", 	false },
			{ "Change Internal Collaborator role", "box-admin+1@securletbeatle.com", "viewer", "COLLAB_UPDATE", "co-owner", "co-owner", false },
			{ "Change Internal Collaborator role", "box-admin+1@securletbeatle.com", "co-owner", "COLLAB_UPDATE", "viewer", "viewer", 	false },	
			
        	{ "Change Internal Collaborator role", "box-admin+1@securletbeatle.com", "viewer", "COLLAB_UPDATE", "uploader", "uploader",	false },
        	{ "Change Internal Collaborator role", "box-admin+1@securletbeatle.com", "uploader", "COLLAB_UPDATE", "previewer", "previewer", 	false },
        	{ "Change Internal Collaborator role", "box-admin+1@securletbeatle.com", "previewer", "COLLAB_UPDATE", "previewer uploader", "previewer uploader", false },
        	{ "Change Internal Collaborator role", "box-admin+1@securletbeatle.com", "previewer uploader", "COLLAB_UPDATE", "previewer", "previewer", 	false },
			{ "Change Internal Collaborator role", "box-admin+1@securletbeatle.com", "previewer", "COLLAB_UPDATE", "viewer uploader", "viewer uploader", false },
			{ "Change Internal Collaborator role", "box-admin+1@securletbeatle.com", "viewer uploader", "COLLAB_UPDATE", "previewer", "previewer", 	false },
			{ "Change Internal Collaborator role", "box-admin+1@securletbeatle.com", "previewer", "COLLAB_UPDATE", "co-owner", "co-owner", false },
			{ "Change Internal Collaborator role", "box-admin+1@securletbeatle.com", "co-owner", "COLLAB_UPDATE", "previewer", "previewer", 	false },
			{ "Change Internal Collaborator role", "box-admin+1@securletbeatle.com", "previewer", "COLLAB_UPDATE", "co-owner", "co-owner", false },
			{ "Change Internal Collaborator role", "box-admin+1@securletbeatle.com", "co-owner", "COLLAB_UPDATE", "previewer uploader", "previewer uploader", false },
			
			{ "Change Internal Collaborator role", "box-admin+1@securletbeatle.com", "previewer uploader", "COLLAB_UPDATE", "uploader", "uploader",	false },
			{ "Change Internal Collaborator role", "box-admin+1@securletbeatle.com", "uploader", "COLLAB_UPDATE", "co-owner", "co-owner", false },
			{ "Change Internal Collaborator role", "box-admin+1@securletbeatle.com", "editor", "COLLAB_UPDATE", "previewer uploader", "previewer uploader", false },
			{ "Change Internal Collaborator role", "box-admin+1@securletbeatle.com", "co-owner", "COLLAB_UPDATE", "uploader", "uploader",	false },
			{ "Change Internal Collaborator role", "box-admin+1@securletbeatle.com", "uploader", "COLLAB_UPDATE", "viewer uploader", "viewer uploader", false },
			{ "Change Internal Collaborator role", "box-admin+1@securletbeatle.com", "viewer uploader", "COLLAB_UPDATE", "uploader", "uploader",	false },
			{ "Change Internal Collaborator role", "box-admin+1@securletbeatle.com", "uploader", "COLLAB_UPDATE", "co-owner", "co-owner", false },
			{ "Change Internal Collaborator role", "box-admin+1@securletbeatle.com", "co-owner", "COLLAB_UPDATE", "uploader", "uploader",	false },
			
			//When ownership is transferred to another user, current user will become an editor. so pass the editor role in expected role
			//Commenting for time being... add code to delete after ownership transfer
			{ "Change Internal Collaborator role", "box-admin+1@securletbeatle.com", "editor", "COLLAB_UPDATE", "owner", "editor", true },
			
        };
    }
    
    
    @DataProvider(name = "ExternalCollaborationDataProvider")
    public static Object[][] dataProviderForExternalCollaborationRole()
    {	
    	//editor, viewer, previewer, uploader, previewer uploader, viewer uploader, co-owner, or owner
    	ArrayList<String> users = new ArrayList<String>();
    	users.add("pushpan@gmail.com");
    	users.add("t.pushparaj@gmail.com");
    	
    	ArrayList<String> roles = new ArrayList<String>();
    	roles.add("editor");
    	roles.add("viewer");
    	
    	ArrayList<String> remedialRoles = new ArrayList<String>();
    	roles.add("uploader");
    	roles.add("previewer");
    	
    	
        return new Object[][] { 
        	//testname, 	collaborators, currentRoles, 	remedialaction,  remedialRoles, expectedRole, Service to call
        	
			//When ownership is transferred to another user, current user will become an editor. so pass the editor role in expected role
			//Commenting for time being... add code to delete after ownership transfer
			//{ "Change Internal Collaborator role", "mitthan.meena@elasticaqa.net", "editor", "COLLAB_UPDATE", "owner", "editor", true },
			
			
			//External collaborator related tests
			{ "Change External Collaborator role", "pushpan@gmail.com", "editor", "COLLAB_UPDATE", "viewer", "viewer", 	false },
        	{ "Change External Collaborator role", "pushpan@gmail.com", "viewer", "COLLAB_UPDATE", "editor", "editor", 	false },
			{ "Change External Collaborator role", "pushpan@gmail.com", "editor", "COLLAB_UPDATE", "previewer", "previewer", 	false },
			{ "Change External Collaborator role", "pushpan@gmail.com", "previewer", "COLLAB_UPDATE", "editor", "editor", 	false },
			{ "Change External Collaborator role", "pushpan@gmail.com", "editor", "COLLAB_UPDATE", "uploader", "uploader",	false },
			{ "Change External Collaborator role", "pushpan@gmail.com", "uploader", "COLLAB_UPDATE", "editor", "editor", 	false },
			{ "Change External Collaborator role", "pushpan@gmail.com", "editor", "COLLAB_UPDATE", "previewer uploader", "previewer uploader", false },
			{ "Change External Collaborator role", "pushpan@gmail.com", "previewer uploader", "COLLAB_UPDATE", "editor", "editor", 	false },
			{ "Change External Collaborator role", "pushpan@gmail.com", "editor", "COLLAB_UPDATE", "viewer uploader", "viewer uploader", false },
			{ "Change External Collaborator role", "pushpan@gmail.com", "viewer uploader", "COLLAB_UPDATE", "editor", "editor", 	false },
			{ "Change External Collaborator role", "pushpan@gmail.com", "editor", "COLLAB_UPDATE", "co-owner", "co-owner", false },
			{ "Change External Collaborator role", "pushpan@gmail.com", "co-owner", "COLLAB_UPDATE", "editor", "editor", 	false },
			
        	{ "Change External Collaborator role", "pushpan@gmail.com", "editor", "COLLAB_UPDATE", "viewer", "viewer", 	false },
        	{ "Change External Collaborator role", "pushpan@gmail.com", "viewer", "COLLAB_UPDATE", "previewer", "previewer", 	false },
        	{ "Change External Collaborator role", "pushpan@gmail.com", "previewer", "COLLAB_UPDATE", "viewer", "viewer", 	false },
        	{ "Change External Collaborator role", "pushpan@gmail.com", "viewer", "COLLAB_UPDATE", "uploader", "uploader",	false },
        	{ "Change External Collaborator role", "pushpan@gmail.com", "uploader", "COLLAB_UPDATE", "viewer", "viewer", 	false },
        	{ "Change External Collaborator role", "pushpan@gmail.com", "viewer", "COLLAB_UPDATE", "previewer uploader", "previewer uploader", false },
        	{ "Change External Collaborator role", "pushpan@gmail.com", "previewer uploader", "COLLAB_UPDATE", "viewer", "viewer", 	false },
			{ "Change External Collaborator role", "pushpan@gmail.com", "viewer", "COLLAB_UPDATE", "viewer uploader", "viewer uploader", false },
			{ "Change External Collaborator role", "pushpan@gmail.com", "viewer uploader", "COLLAB_UPDATE", "viewer", "viewer", 	false },
			{ "Change External Collaborator role", "pushpan@gmail.com", "viewer", "COLLAB_UPDATE", "co-owner", "co-owner", false },
			{ "Change External Collaborator role", "pushpan@gmail.com", "co-owner", "COLLAB_UPDATE", "viewer", "viewer", 	false },	
			
        	{ "Change External Collaborator role", "pushpan@gmail.com", "viewer", "COLLAB_UPDATE", "uploader", "uploader",	false },
        	{ "Change External Collaborator role", "pushpan@gmail.com", "uploader", "COLLAB_UPDATE", "previewer", "previewer", 	false },
        	{ "Change External Collaborator role", "pushpan@gmail.com", "previewer", "COLLAB_UPDATE", "previewer uploader", "previewer uploader", false },
        	{ "Change External Collaborator role", "pushpan@gmail.com", "previewer uploader", "COLLAB_UPDATE", "previewer", "previewer", 	false },
			{ "Change External Collaborator role", "pushpan@gmail.com", "previewer", "COLLAB_UPDATE", "viewer uploader", "viewer uploader", false },
			{ "Change External Collaborator role", "pushpan@gmail.com", "viewer uploader", "COLLAB_UPDATE", "previewer", "previewer", 	false },
			{ "Change External Collaborator role", "pushpan@gmail.com", "previewer", "COLLAB_UPDATE", "co-owner", "co-owner", false },
			{ "Change External Collaborator role", "pushpan@gmail.com", "co-owner", "COLLAB_UPDATE", "previewer", "previewer", 	false },
			{ "Change External Collaborator role", "pushpan@gmail.com", "previewer", "COLLAB_UPDATE", "co-owner", "co-owner", false },
			{ "Change External Collaborator role", "pushpan@gmail.com", "co-owner", "COLLAB_UPDATE", "previewer uploader", "previewer uploader", false },
			
			{ "Change External Collaborator role", "pushpan@gmail.com", "previewer uploader", "COLLAB_UPDATE", "uploader", "uploader",	false },
			{ "Change External Collaborator role", "pushpan@gmail.com", "uploader", "COLLAB_UPDATE", "co-owner", "co-owner", false },
			{ "Change External Collaborator role", "pushpan@gmail.com", "editor", "COLLAB_UPDATE", "previewer uploader", "previewer uploader", false },
			{ "Change External Collaborator role", "pushpan@gmail.com", "co-owner", "COLLAB_UPDATE", "uploader", "uploader",	false },
			{ "Change External Collaborator role", "pushpan@gmail.com", "uploader", "COLLAB_UPDATE", "viewer uploader", "viewer uploader", false },
			{ "Change External Collaborator role", "pushpan@gmail.com", "viewer uploader", "COLLAB_UPDATE", "uploader", "uploader",	false },
			{ "Change External Collaborator role", "pushpan@gmail.com", "uploader", "COLLAB_UPDATE", "co-owner", "co-owner", false },
			{ "Change External Collaborator role", "pushpan@gmail.com", "co-owner", "COLLAB_UPDATE", "uploader", "uploader",	false },
			
			//When ownership is transferred to another user, current user will become an editor. so pass the editor role in expected role
			{ "Change External Collaborator role", "pushpan@gmail.com", "editor", "COLLAB_UPDATE", "owner", "editor", true },

        };
    }
    
    
    @DataProvider(name = "SharedLinkCollaborationDataProvider")
    public static Object[][] dataProviderForSharedLinkCollaborationRole()
    {	
    	//editor, viewer, previewer, uploader, previewer uploader, viewer uploader, co-owner, or owner
    	ArrayList<String> users = new ArrayList<String>();
    	users.add("pushpan@gmail.com");
    	users.add("t.pushparaj@gmail.com");
    	
    	ArrayList<String> roles = new ArrayList<String>();
    	roles.add("editor");
    	roles.add("viewer");
    	
    	ArrayList<String> remedialRoles = new ArrayList<String>();
    	roles.add("uploader");
    	roles.add("previewer");
    	
    	
        return new Object[][] { 
        			//testname,  exposureType, 	remedialaction, remedy, collaborators, current role, remedy, remedialRoles, expectedRole, Service to call
        						
	{ "testcase1", "open", "UNSHARE", null, "mitthan.meena@elasticaqa.net,pushpan@gmail.com", "editor", "COLLAB_UPDATE", "mitthan.meena@elasticaqa.net" ,"viewer", "viewer", "APIServer" },
	{ "testcase2", "open", "SHARE_ACCESS", "company", "mitthan.meena@elasticaqa.net,pushpan@gmail.com", "editor", "COLLAB_REMOVE", "pushpan@gmail.com" ,"viewer", "viewer", "APIServer" },
	{ "testcase3", "open", "SHARE_ACCESS", "company", "mitthan.meena@elasticaqa.net,pushpan@gmail.com", "editor", "COLLAB_REMOVE", "mitthan.meena@elasticaqa.net,pushpan@gmail.com" ,"viewer", "viewer", "APIServer" },
								
        						

        };
    }
    
    
    @DataProvider(name = "metricsExposuresTotal")
    public static Object[][] dataProviderForMetricsExposuresTotal() {
    	return new Object[][] { 
    		//testname, 	rootfolder, filename, 		shared access, 	expectedchange,  remedialRoles, expectedRole, Service to call
    		{ "Test1", "/", "Hello.java", "open", "public", "APIServer" },
    		{ "Test1", "/", "BE.txt",     "open", "public", "APIServer" },
    		{ "Test1", "/", "BE.txt",     "company", "internal", "APIServer" }
    	};
    }
    
    
    
    @DataProvider(name = "metricsFolderExposuresTotal")
    public static Object[][] dataProviderForFolderMetricsExposuresTotal() {
    	
    	String collabs[] = {};
    	String internalCollabs[] = {"testuser1@securletautomation.com"};
    	String externalCollabs[] = {"pushpan@gmail.com"};
    	String internalexternalCollabs[] = {"testuser1@securletautomation.com","pushpan@gmail.com"};
    	
    	return new Object[][] { 
//    		testname, 								filename, 		shared access, 	expectedchange,  remedialRoles, expectedRole, Service to call
    		{ "public folder exposure"  , 				 "BE.txt",	   "open", 			"public", 			collabs, 			"APIServer" },
    		{ "internal folder exposure by company", 	 "BE.txt",     "company", 		"company", 			collabs, 			"APIServer" },
    		{ "internal collaborators folder exposure",  "BE.txt",     "collaborators", "internal", 		internalCollabs, 	"APIServer" },
    		{ "external collaborators folder exposure",  "BE.txt",     "collaborators", "external", 		externalCollabs, 	"APIServer" },
    		{ "internal and external collaborators folder exposure",   "BE.txt",         "collaborators", "internal_external", internalexternalCollabs, 	"APIServer" },
    		
    	};
    }
    
    
    // File type exposure total
    @DataProvider(name = "fileTypesExposuresTotal")
    public static Object[][] dataProviderForFileTypesExposuresTotal() {
    	return new Object[][] { 
    		//testname, 										folder, filename, 	shared access, 	term,  remedialRoles, expectedRole, Service to call
    		{ "Expose a java file and verify the filetype count", "/", "Hello.java", 				"open", 	"WORDPROCESSOR", 	"APIServer" },
    		{ "Expose a text file and verify the filetype count", "/", "BE.txt",     				"open", 	"WORDPROCESSOR", 	"APIServer" },
    		{ "Expose a pdf file and verify the filetype count",  "/", "test.pdf",   				"open", 	"WORDPROCESSOR", 	"APIServer" },
    		{ "Expose a rtf file and verify the filetype count",  "/", "PII_TXT.rtf",				"open", 	"WORDPROCESSOR", "APIServer" },
    		{ "Expose a xlsx file and verify the filetype count", "/", "PII_PCI_EmployeeList.xlsx",	"open", 	"SPREADSHEET", 	"APIServer" },
    		{ "Expose a xls  file and verify the filetype count", "/", "Sample.xls",				"open", 	"SPREADSHEET", 	"APIServer" },
    		{ "Expose a jpg  file and verify the filetype count", "/", "Sample.jpg",				"open", 	"RASTERIMAGE", 		"APIServer" },
    		{ "Expose a png  file and verify the filetype count", "/", "Sample.png",				"open", 	"RASTERIMAGE", 		"APIServer" },
    		
    		
    		
    	//	{ "Expose a text file and verify the filetype count", "/", "BE.txt",     "company", "internal", "APIServer" }
    	};
    }
    
    
    
    
    // contentTypesExposuresTotal
    @DataProvider(name = "contentTypesExposuresTotal")
    public static Object[][] dataProviderForContentTypesExposuresTotal() {
    	return new Object[][] { 
    		//testname, 										folder, filename, 	shared access, 	term,  remedialRoles, expectedRole, Service to call
    		{ "Expose a jpg  file and verify the  count", "/", "Sample.jpg",				"open", 	"image", 		"APIServer" },
    		{ "Expose a png  file and verify the  count", "/", "Sample.png",				"open", 	"image", 		"APIServer" },
    		{ "Expose a design doc and verify the  count", "/", "Design.pdf",				"open", 	"design", 		"APIServer" },
    		{ "Expose a health doc and verify the  count", "/", "Nutrition_Health.txt",		"open", 	"health", 		"APIServer" },
    		{ "Expose a legal doc and verify the  count", "/", "Legal-Divorce2.pdf",		"open", 	"legal", 		"APIServer" },
    		{ "Expose a computing doc and verify the  count", "/", "SC_GitHubJava.docx",	"open", 	"computing", 	"APIServer" },
    		{ "Expose a business doc and verify the  count", "/", "business.txt",			"open", 	"business", 	"APIServer" },
    		
    	};
    }
    
    // vulnerabilityTypesExposuresTotal
    @DataProvider(name = "vulnerabilityTypesExposuresTotal")
    public static Object[][] dataProviderForVulnerabilityTypesExposuresTotal() {
    	return new Object[][] { 
    		//testname, 										folder, filename, 	shared access, 	term,  remedialRoles, expectedRole, Service to call
    		{ "Expose a source code file", "/", "Hello.java",		"open", 	"source_code", 		"APIServer" },
    		
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
    
    
    @DataProvider(name = "ExternalUserInternalCollaborationDataProvider")
    public static Object[][] ExternalUserInternalCollaborationDataProvider()
    {	
    	String collaborator1 = "testuser1@securletbeatle.com";
    	
    	//editor, viewer, previewer, uploader, previewer uploader, viewer uploader, co-owner, or owner
        return new Object[][] { 
			//Collaborator , role
			{ collaborator1,  "editor"},
			{ collaborator1,  "viewer"},
			{ collaborator1,  "previewer"},
			{ collaborator1,  "uploader"},
			{ collaborator1,  "previewer uploader"},
			{ collaborator1,  "viewer uploader"},
			{ collaborator1,  "co-owner"}
			
        };
    }
    
    @DataProvider(name = "I18NString")
    public static Object[][] I18NString()
    {	
    	//locale, filenames
        return new Object[][] { 
			//Collaborator , role
			{ "ES_ES",  I18N.getString("language", "es_es")},
			{ "HI_IN",  I18N.getString("language", "hi_in")},
			{ "UR_IN",  I18N.getString("language", "ur_in")},
			{ "TA_IN",  I18N.getString("language", "ta_in")},
			{ "ZH_CN",  I18N.getString("language", "zh_cn")},
			{ "JA_JP",  I18N.getString("language", "ja_jp")},
			{ "FR_FR",  I18N.getString("language", "fr_fr")},
			{ "PT_BR",  I18N.getString("language", "pt_br")},
			{ "DE_DE",  I18N.getString("language", "de_de")},
			
        };
    }
    
    
}
