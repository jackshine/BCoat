package com.elastica.beatle.securlets;

import java.util.ArrayList;

import org.testng.annotations.DataProvider;

import com.elastica.beatle.i18n.I18N;

public class O365DataProvider {

	@DataProvider(name = "exposeAndRemediateDataProvider")
	public static Object[][] exposeAndRemediateDataProvider()
	{	
		String[] remedialAction1 = {"UNSHARE"};
		String[] metaInfo1		 = {"Everyone except external users-Read"};
		String[] metaInfo11		 = {"Everyone except external users-Contribute"};
		String[] metaInfo21		 = {"Everyone-Read"};
		String[] metaInfo22		 = {"Everyone-Contribute"};

		String[] remedialAction2 = {"SHARE_ACCESS"};
		String[] metaInfo2		 = {"Everyone-Read"};

		String[] remedialAction = {"UNSHARE", "UNSHARE"};
		String[] metaInfo3		= {"open-edit", "open-view"};

		return new Object[][] { 
			//int role, String sharedWithUsers, 		String currentLink, String[] remedialAction, String[] metaInfo, Service to call

			{  1, "Everyone except external users", remedialAction1, metaInfo1},
//			{  2, "Everyone except external users", "Everyone except external users-Contribute" , remedialAction1, metaInfo11, "APIServer" },
//			{  1, "Everyone"					  , "Everyone-Read"								, remedialAction1, metaInfo21, "APIServer" },
//			{  2, "Everyone"					  , "Everyone-Contribute"						, remedialAction1, metaInfo22, "APIServer" },
//			{  2, "Everyone"                      , "Everyone-Contribute"						, remedialAction2, metaInfo21, "APIServer" },
//			{  2, "Everyone"                      , "Everyone-Contribute"						, remedialAction2, metaInfo1, "APIServer" },
//			{  2, "Everyone"                      , "Everyone-Contribute"						, remedialAction2, metaInfo11, "APIServer" },
//			{  1, "Everyone"                      , "Everyone-Read"								, remedialAction2, metaInfo1, "APIServer" },
//			{  1, "Everyone"                      , "Everyone-Read"								, remedialAction2, metaInfo11, "APIServer" },
//			{  1, "Everyone"                      , "Everyone-Read"								, remedialAction2, metaInfo22, "APIServer" },
//			{  2, "Everyone except external users", "Everyone except external users-Contribute"	, remedialAction2, metaInfo1, "APIServer" },
//			{  2, "Everyone except external users", "Everyone except external users-Contribute"	, remedialAction2, metaInfo21, "APIServer" },
//			{  2, "Everyone except external users", "Everyone except external users-Contribute"	, remedialAction2, metaInfo22, "APIServer" },
//			{  1, "Everyone except external users", "Everyone except external users-Read"		, remedialAction2, metaInfo21, "APIServer" },
//			{  1, "Everyone except external users", "Everyone except external users-Read"		, remedialAction2, metaInfo22,  "APIServer" },
//			{  1, "Everyone except external users", "Everyone except external users-Read"		, remedialAction2, metaInfo11, "APIServer" },
			
		};
	}
	
	@DataProvider(name = "ExposureRemediationProvider")
	public static Object[][] ExposureRemediationProvider()
	{	
		return new Object[][] { 
			//User, Permission, RemedialAction
			{"Guest Reader", 				"Read",		 		"UNSHARE"},
			{"Guest Contributor", 			"Contribute",		"UNSHARE"},
			
			{"Everyone", 					"Contribute", 		"UNSHARE"},
			{"Everyone", 					"Full Control", 	"UNSHARE"},
			{"Everyone", 					"Design", 			"UNSHARE"},
			{"Everyone", 					"Edit", 			"UNSHARE"},
			{"Everyone", 					"Read", 			"UNSHARE"},
			{"Everyone except external users", "Contribute", 	"UNSHARE"},
			{"Everyone except external users", "Full Control", 	"UNSHARE"},
			{"Everyone except external users", "Design", 		"UNSHARE"},
			{"Everyone except external users", "Edit", 			"UNSHARE"},
			{"Everyone except external users", "Read", 			"UNSHARE"}
			
		};
	}
	
	
	
	@DataProvider(name = "PublicInternalExposureRemediationProvider")
	public static Object[][] PublicInternalExposureRemediationProvider()
	{	
		
		// Sample combination
		/*
		String exposedTo[] = {"Guest Reader", "Everyone except external users"};
		String exposedPermissions[] = {"Read", "Contribute"};
		String remedialAction[] = {"UNSHARE", "UNSHARE"};
		String metaInfo[] = {"open-view", "Everyone except external users-Contribute"};
		*/
		
		return new Object[][] { 
			//User, Permission, RemedialAction, current link
			{new String[] {"Guest Reader", "Everyone except external users"}, new String[] {"Read", "Contribute"},
					new String[] {"UNSHARE", "UNSHARE"}, new String[] {"open-view", "Everyone except external users-Contribute"}},
			
			{new String[] {"Guest Reader", "Everyone except external users"}, new String[] {"Read", "Design"},
						new String[] {"UNSHARE", "UNSHARE"}, new String[] {"open-view", "Everyone except external users-Design"}},
			
			{new String[] {"Guest Reader", "Everyone except external users"}, new String[] {"Read", "Read"},
							new String[] {"UNSHARE", "UNSHARE"}, new String[] {"open-view", "Everyone except external users-Read"}},
			
			{new String[] {"Guest Reader", "Everyone except external users"}, new String[] {"Read", "Full Control"},
								new String[] {"UNSHARE", "UNSHARE"}, new String[] {"open-view", "Everyone except external users-Full Control"}},
			
			{new String[] {"Guest Reader", "Everyone except external users"}, new String[] {"Read", "Edit"},
									new String[] {"UNSHARE", "UNSHARE"}, new String[] {"open-view", "Everyone except external users-Edit"}},
			
			
			{new String[] {"Guest Contributor", "Everyone except external users"}, new String[] {"Contribute", "Contribute"},
						new String[] {"UNSHARE", "UNSHARE"}, new String[] {"open-edit", "Everyone except external users-Contribute"}},
			
			{new String[] {"Guest Contributor", "Everyone except external users"}, new String[] {"Contribute", "Full Control"},
							new String[] {"UNSHARE", "UNSHARE"}, new String[] {"open-edit", "Everyone except external users-Full Control"}},
			
			{new String[] {"Guest Contributor", "Everyone except external users"}, new String[] {"Contribute", "Design"},
								new String[] {"UNSHARE", "UNSHARE"}, new String[] {"open-edit", "Everyone except external users-Design"}},
			
			{new String[] {"Guest Contributor", "Everyone except external users"}, new String[] {"Contribute", "Read"},
									new String[] {"UNSHARE", "UNSHARE"}, new String[] {"open-edit", "Everyone except external users-Read"}},
			
			{new String[] {"Guest Contributor", "Everyone except external users"}, new String[] {"Contribute", "Edit"},
										new String[] {"UNSHARE", "UNSHARE"}, new String[] {"open-edit", "Everyone except external users-Edit"}},
			
			
			{new String[] {"Guest Reader", "Everyone"}, new String[] {"Read", "Contribute"},
													new String[] {"UNSHARE", "UNSHARE"}, new String[] {"open-view", "Everyone-Contribute"}},
									
			{new String[] {"Guest Reader", "Everyone"}, new String[] {"Read", "Design"},
													new String[] {"UNSHARE", "UNSHARE"}, new String[] {"open-view", "Everyone-Design"}},

			{new String[] {"Guest Reader", "Everyone"}, new String[] {"Read", "Read"},
													new String[] {"UNSHARE", "UNSHARE"}, new String[] {"open-view", "Everyone-Read"}},

			{new String[] {"Guest Reader", "Everyone"}, new String[] {"Read", "Full Control"},
													new String[] {"UNSHARE", "UNSHARE"}, new String[] {"open-view", "Everyone-Full Control"}},

			{new String[] {"Guest Reader", "Everyone"}, new String[] {"Read", "Edit"},
													new String[] {"UNSHARE", "UNSHARE"}, new String[] {"open-view", "Everyone-Edit"}},


			{new String[] {"Guest Contributor", "Everyone"}, new String[] {"Contribute", "Contribute"},
													new String[] {"UNSHARE", "UNSHARE"}, new String[] {"open-edit", "Everyone-Contribute"}},

			{new String[] {"Guest Contributor", "Everyone"}, new String[] {"Contribute", "Full Control"},
													new String[] {"UNSHARE", "UNSHARE"}, new String[] {"open-edit", "Everyone-Full Control"}},

			{new String[] {"Guest Contributor", "Everyone"}, new String[] {"Contribute", "Design"},
													new String[] {"UNSHARE", "UNSHARE"}, new String[] {"open-edit", "Everyone-Design"}},

			{new String[] {"Guest Contributor", "Everyone"}, new String[] {"Contribute", "Read"},
													new String[] {"UNSHARE", "UNSHARE"}, new String[] {"open-edit", "Everyone-Read"}},

			{new String[] {"Guest Contributor", "Everyone"}, new String[] {"Contribute", "Edit"},
													new String[] {"UNSHARE", "UNSHARE"}, new String[] {"open-edit", "Everyone-Edit"}},
		};
	}
	
	@DataProvider(name = "ShareAccessRemediationProvider")
	public static Object[][] ShareAccessRemediationProvider()
	{	
		
		// Sample combination
		/*
		String exposedTo[] = {"Guest Reader", "Everyone except external users"};
		String exposedPermissions[] = {"Read", "Contribute"};
		String remedialAction[] = {"UNSHARE", "UNSHARE"};
		String metaInfo[] = {"open-view", "Everyone except external users-Contribute"};
		String expectedExposuresAfterRemediation[] = {"internal"};
		*/
		
		return new Object[][] { 
			//User, Permission, RemedialAction, current link
			{new String[] {"Guest Reader", "Everyone except external users"}, new String[] {"Read", "Contribute"},
					new String[] {"UNSHARE", "SHARE_ACCESS"}, new String[] {"open-view", "Everyone except external users-Read"}, 
						new String[]{"internal", "nopublic", "noexternal"}},
			
			{new String[] {"Guest Reader", "Everyone except external users"}, new String[] {"Read", "Design"},
						new String[] {"UNSHARE", "SHARE_ACCESS"}, new String[] {"open-view", "Everyone except external users-Edit"}, 
						new String[]{"internal", "nopublic", "noexternal"}},
			
			{new String[] {"Guest Reader", "Everyone except external users"}, new String[] {"Read", "Read"},
							new String[] {"UNSHARE", "SHARE_ACCESS"}, new String[] {"open-view", "Everyone except external users-Contribute"}, 
							new String[]{"internal", "nopublic", "noexternal"}},
			
			{new String[] {"Guest Reader", "Everyone except external users"}, new String[] {"Read", "Full Control"},
								new String[] {"SHARE_ACCESS"}, new String[] {"Everyone except external users-Read"}, 
								new String[]{"internal", "public", "noexternal"}},
			
			{new String[] {"Guest Reader", "Everyone except external users"}, new String[] {"Read", "Edit"},
									new String[] {"SHARE_ACCESS"}, new String[] {"Everyone except external users-Read"}, 
									new String[]{"internal", "public", "noexternal"}},
			
			
			{new String[] {"Guest Contributor", "Everyone except external users"}, new String[] {"Contribute", "Contribute"},
						new String[] {"UNSHARE", "SHARE_ACCESS"}, new String[] {"open-edit", "Everyone except external users-Read"}, 
									new String[]{"internal", "nopublic", "noexternal"}},
			
			{new String[] {"Guest Contributor", "Everyone except external users"}, new String[] {"Contribute", "Full Control"},
							new String[] {"UNSHARE", "SHARE_ACCESS"}, new String[] {"open-edit", "Everyone except external users-Design"}, 
											new String[]{"internal", "nopublic", "noexternal"}},
			
			{new String[] {"Guest Contributor", "Everyone except external users"}, new String[] {"Contribute", "Design"},
								new String[] {"SHARE_ACCESS"}, new String[] {"Everyone except external users-Edit"}, 
								new String[]{"internal", "public", "noexternal"}},
			
			{new String[] {"Guest Contributor", "Everyone except external users"}, new String[] {"Contribute", "Read"},
									new String[] {"SHARE_ACCESS"}, new String[] {"Everyone except external users-Contribute"}, 
									new String[]{"internal", "public", "noexternal"}},
			
			{new String[] {"Guest Contributor", "Everyone except external users"}, new String[] {"Contribute", "Edit"},
										new String[] { "SHARE_ACCESS"}, new String[] {"Everyone except external users-Read"}, 
										new String[]{"internal", "public", "noexternal"}}
		};
	}
	
	
	
	
	@DataProvider(name = "CollabUpdateRemediationProvider")
	public static Object[][] CollabUpdateRemediationProvider()
	{	
		
		// Sample combination
		/*
		String exposedTo[] = {"Guest Reader", "Everyone except external users"};
		String exposedPermissions[] = {"Read", "Contribute"};
		String remedialAction[] = {"UNSHARE", "UNSHARE"};
		String metaInfo[] = {"open-view", "Everyone except external users-Contribute"};
		String expectedExposuresAfterRemediation[] = {"internal"};
		
		String collabUpdateRole = "Read";
		*/
		
		return new Object[][] { 
			
			
			{new String[] {"Pushparaj Thangaraj"}, new String[] {"Contribute"},
													new String[] {"COLLAB_UPDATE"}, new String[] {"pushpan@gmail.com"}, new String[] {"Read"},
													new String[] {"nointernal", "nopublic", "external"}},

			{new String[] {"testuser1 B1", "Pushparaj Thangaraj"}, new String[] {"Read", "Contribute"},
				new String[] {"COLLAB_UPDATE", "COLLAB_UPDATE"}, new String[] {"testuser1_b1@securletdddo365beatle.onmicrosoft.com", "pushpan@gmail.com"}, new String[] {"Edit", "Design"},
				new String[] {"nointernal", "nopublic", "external"}},
			
			{new String[] {"testuser1 B1", "Pushparaj Thangaraj"}, new String[] {"Design", "Edit"},
					new String[] {"COLLAB_UPDATE", "COLLAB_UPDATE"}, new String[] {"testuser1_b1@securletdddo365beatle.onmicrosoft.com", "pushpan@gmail.com"}, new String[] {"Edit", "Design"},
					new String[] {"nointernal", "nopublic", "external"}},
			
			{new String[] {"testuser1 B1", "Pushparaj Thangaraj"}, new String[] {"Full Control", "Contribute"},
						new String[] {"COLLAB_UPDATE", "COLLAB_UPDATE"}, new String[] {"testuser1_b1@securletdddo365beatle.onmicrosoft.com", "pushpan@gmail.com"}, new String[] {"Design", "Read"},
						new String[] {"nointernal", "nopublic", "external"}},
			
			
			{new String[] {"Pushparaj Thangaraj"}, new String[] {"Read"},
														new String[] {"COLLAB_UPDATE"}, new String[] {"pushpan@gmail.com"}, new String[] {"Edit"},
														new String[] {"nointernal", "nopublic", "external"}},
			
			{new String[] {"Pushparaj Thangaraj"}, new String[] {"Full Control"},
															new String[] {"COLLAB_UPDATE"}, new String[] {"pushpan@gmail.com"}, new String[] {"Read"},
															new String[] {"nointernal", "nopublic", "external"}},
			
			{new String[] {"Pushparaj Thangaraj"}, new String[] {"Design"},
																new String[] {"COLLAB_UPDATE"}, new String[] {"pushpan@gmail.com"}, new String[] {"Edit"},
																new String[] {"nointernal", "nopublic", "external"}},
			
			{new String[] {"Pushparaj Thangaraj"}, new String[] {"Edit"},
																	new String[] {"COLLAB_UPDATE"}, new String[] {"pushpan@gmail.com"}, new String[] {"Read"},
																	new String[] {"nointernal", "nopublic", "external"}},
			
			{new String[] {"Pushparaj Thangaraj", "testuser1 B1"}, new String[] {"Read", "Contribute"},
														new String[] {"COLLAB_UPDATE", "COLLAB_UPDATE"}, new String[] {"pushpan@gmail.com", "testuser1_b1@securletdddo365beatle.onmicrosoft.com"}, new String[] {"Edit", "Design"},
														new String[] {"nointernal", "nopublic", "external"}},
			
			{new String[] {"Pushparaj Thangaraj", "testuser1 B1"}, new String[] {"Edit", "Design"},
														new String[] {"COLLAB_UPDATE", "COLLAB_UPDATE"}, new String[] {"pushpan@gmail.com", "testuser1_b1@securletdddo365beatle.onmicrosoft.com"}, 
														new String[] {"Read", "Read"}, new String[] {"nointernal", "nopublic", "external"}},
			
			{new String[] {"Pushparaj Thangaraj", "testuser1 B1"}, new String[] {"Full Control", "Full Control"},
															new String[] {"COLLAB_UPDATE", "COLLAB_UPDATE"}, new String[] {"pushpan@gmail.com", "testuser1_b1@securletdddo365beatle.onmicrosoft.com"}, 
															new String[] {"Design", "Contribute"}, new String[] {"nointernal", "nopublic", "external"}}
			
		};
	}
	
	@DataProvider(name = "CollabRemoveRemediationProvider")
	public static Object[][] CollabRemoveRemediationProvider()
	{	
		
		// Sample combination
		/*
		String exposedTo[] = {"Guest Reader", "Everyone except external users"};
		String exposedPermissions[] = {"Read", "Contribute"};
		String remedialAction[] = {"UNSHARE", "UNSHARE"};
		String metaInfo[] = {"open-view", "Everyone except external users-Contribute"};
		String expectedExposuresAfterRemediation[] = {"internal"};
		
		String collabUpdateRole = "Read";
		*/
		
		return new Object[][] { 
			
			
			{new String[] {"Pushparaj Thangaraj"}, new String[] {"Contribute"},
													new String[] {"COLLAB_REMOVE"}, new String[] {"pushpan@gmail.com"}},

			{new String[] {"testuser1 B1", "Pushparaj Thangaraj"}, new String[] {"Read", "Contribute"},
				new String[] {"COLLAB_REMOVE", "COLLAB_REMOVE"}, new String[] {"testuser1_b1@securletdddo365beatle.onmicrosoft.com", "pushpan@gmail.com"}},
			
		};
	}
	
	
	
	@DataProvider(name = "AllAccessRemediationProvider")
	public static Object[][] AllAccessRemediationProvider()
	{	
		
		// Sample combination
		/*
		String exposedTo[] = {"Guest Reader", "Everyone except external users"};
		String exposedPermissions[] = {"Read", "Contribute"};
		String remedialAction[] = {"UNSHARE", "UNSHARE"};
		String metaInfo[] = {"open-view", "Everyone except external users-Contribute"};
		String expectedExposuresAfterRemediation[] = {"internal"};
		*/
		
		return new Object[][] { 
			//User, Permission, RemedialAction, current link(metainfo)
			{new String[] {"Guest Reader", "Everyone except external users", "Pushparaj Thangaraj"}, new String[] {"Read", "Contribute", "Contribute"},
						new String[] {"UNSHARE", "SHARE_ACCESS", "COLLAB_REMOVE"}, 
						new String[] {"open-view", "Everyone except external users-Read", "pushpan@gmail.com"}, 
						new String[]{"internal", "nopublic", "noexternal"}},

			{new String[] {"Guest Reader", "Everyone except external users", "Pushparaj Thangaraj"}, new String[] {"Read", "Design", "Contribute"},
						new String[] {"UNSHARE", "SHARE_ACCESS", "COLLAB_REMOVE"}, 
						new String[] {"open-view", "Everyone except external users-Contribute", "pushpan@gmail.com"}, 
						new String[]{"internal", "nopublic", "noexternal"}},

			{new String[] {"Guest Writer", "Everyone except external users", "Pushparaj Thangaraj"}, new String[] {"Contribute", "Full Control", "Contribute"},
						new String[] {"UNSHARE", "SHARE_ACCESS", "COLLAB_REMOVE"}, 
						new String[] {"open-edit", "Everyone except external users-Design", "pushpan@gmail.com"}, 
						new String[]{"internal", "nopublic", "noexternal"}},
			
			{new String[] {"Guest Writer", "Everyone except external users", "Pushparaj Thangaraj"}, new String[] {"Contribute", "Edit", "Contribute"},
						new String[] {"UNSHARE", "SHARE_ACCESS", "COLLAB_REMOVE"}, 
						new String[] {"open-edit", "Everyone except external users-Read", "pushpan@gmail.com"}, 
						new String[]{"internal", "nopublic", "noexternal"}},
			
		};
	}
	

	@DataProvider(name = "sharedLinkDataProvider")
	public static Object[][] dataProviderMethod()
	{	
		String[] remedialAction1 = {"UNSHARE"};
		String[] metaInfo1		 = {"Everyone except external users-Read"};
		String[] metaInfo11		 = {"Everyone except external users-Contribute"};
		String[] metaInfo21		 = {"Everyone-Read"};
		String[] metaInfo22		 = {"Everyone-Contribute"};

		String[] remedialAction2 = {"SHARE_ACCESS"};
		String[] metaInfo2		 = {"Everyone-Read"};

		String[] remedialAction = {"UNSHARE", "UNSHARE"};
		String[] metaInfo3		= {"open-edit", "open-view"};

		return new Object[][] { 
			//String testname, 							int role, String sharedWithUsers, 		String currentLink, String[] remedialAction, String[] metaInfo, Service to call

			{ "Source code remediation with view unshare", 1, "Everyone except external users", "Everyone except external users-Contribute" , remedialAction1, metaInfo1,  "APIServer"  },
			{ "Source code remediation with edit unshare", 2, "Everyone except external users", "Everyone except external users-Contribute" , remedialAction1, metaInfo11, "APIServer" },
			{ "Source code remediation with view unshare", 1, "Everyone"					  , "Everyone-Read"								, remedialAction1, metaInfo21, "APIServer" },
			{ "Source code remediation with edit unshare", 2, "Everyone"					  , "Everyone-Contribute"						, remedialAction1, metaInfo22, "APIServer" },
			{ "Source code remediation      Share access", 2, "Everyone"                      , "Everyone-Contribute"						, remedialAction2, metaInfo21, "APIServer" },
			{ "Source code remediation      Share access", 2, "Everyone"                      , "Everyone-Contribute"						, remedialAction2, metaInfo1, "APIServer" },
			{ "Source code remediation      Share access", 2, "Everyone"                      , "Everyone-Contribute"						, remedialAction2, metaInfo11, "APIServer" },
			{ "Source code remediation      Share access", 1, "Everyone"                      , "Everyone-Read"				, remedialAction2, metaInfo1, "APIServer" },
			{ "Source code remediation      Share access", 1, "Everyone"                      , "Everyone-Read"				, remedialAction2, metaInfo11, "APIServer" },
			{ "Source code remediation      Share access", 1, "Everyone"                      , "Everyone-Read"				, remedialAction2, metaInfo22, "APIServer" },
			{ "Source code remediation      Share access", 2, "Everyone except external users", "Everyone except external users-Contribute"	, remedialAction2, metaInfo1, "APIServer" },
			{ "Source code remediation      Share access", 2, "Everyone except external users", "Everyone except external users-Contribute"	, remedialAction2, metaInfo21, "APIServer" },
			{ "Source code remediation      Share access", 2, "Everyone except external users", "Everyone except external users-Contribute"	, remedialAction2, metaInfo22, "APIServer" },
			{ "Source code remediation      Share access", 1, "Everyone except external users", "Everyone except external users-Read"	, remedialAction2, metaInfo21, "APIServer" },
			{ "Source code remediation      Share access", 1, "Everyone except external users", "Everyone except external users-Read"	, remedialAction2, metaInfo22,  "APIServer" },
			{ "Source code remediation      Share access", 1, "Everyone except external users", "Everyone except external users-Read"	, remedialAction2, metaInfo11, "APIServer" },
			
		};
	}
	
	@DataProvider(name = "exposureTypeProvider")
	public static Object[][] dataProviderMethod2()
	{	
		return new Object[][] { 
			//filename,    exposure, sharedRole, Service to call 
			//Shared role  1- read, 2 -  write, 3 - full control
			//Users need to be created before running the test
			{ "test.pdf", "PUBLIC",   1, "APIServer" },
			{ "test.pdf", "PUBLIC",   2, "APIServer" },
			{ "test.pdf", "INTERNAL", 1, "APIServer" },
			{ "test.pdf", "INTERNAL", 2, "APIServer" },
			{ "test.pdf", "EXTERNAL", 1, "APIServer" },
			{ "test.pdf", "EXTERNAL", 2, "APIServer" },
			

		};
	}
	
	@DataProvider(name = "ActivityTypeFilter")
    public static Object[][] ActivityTypeFilter() {
    	return new Object[][] { 
    		
    		{"Delete"}, {"Upload"}, {"Share"}, {"Unshare"},  
    		{"Rename"}, {"Edit"}, {"Delete"}, {"Content Inspection"}, {"Receive"},
    		{"ScopeAdd"}, {"Send"}
    		
    	};
    }
    
    
    
    @DataProvider(name = "ObjectTypeFilter")
    public static Object[][] ObjectTypeFilter() {
    	return new Object[][] { 
    		{"File"}, {"Folder"}, {"List"}, {"Email_Message"}, {"Email_File_Attachment"}, {"User"}, {"Site"}
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
    		{"informational", "Bangalore (India)"}, {"informational", "Boardman (United States)"}, {"informational", "San Jose (United States)"}
    	};
    }
    
    @DataProvider(name = "ExposureTypeFilter")
    public static Object[][] ExposureTypeFilter() {
    	return new Object[][] { 
    		{"public"}, {"all_internal"}, {"ext_count"}
    	};
    }
    
    @DataProvider(name = "ExportFormat")
    public static Object[][] ExportFormat()
    {	
        return new Object[][] { 
        						
		{ "cef" }, { "csv" }, { "leef" }
		
        };
    }
    
    @DataProvider(name = "ExportFilter")
    public static Object[][] ExportFilter()
    {	
        
    	//exposuretype, objectType, risk, ciq, contentType, fileType, exportType, searchText, isInternal
    	return new Object[][] { 
    		{ "ext_count", "OneDrive", "pii", null, null, "rtf", "docs", "", true}, 					
    		{ "public", "OneDrive", "pii", null, null, "rtf", "docs", "", true}, 
    		{ "all_internal", "OneDrive", "pci", null, null, null, "docs", "", true}, 
    		{ "public", "OneDrive", "pii", null, "source_code", "java", "docs", "", true}, 
    		{ null, "OneDrive", null, null, "legal", null, "docs", "", true},
    		{ null, "OneDrive", null, null, null, null, "docs", "java", true},
    		{ null, "OneDrive", null, null, null, null, "docs", null, false},
        };
    }
    
    @DataProvider(name = "ExportUsersFilter")
    public static Object[][] ExportUsersFilter()
    {	
        
    	//exposuretype, objectType, risk, ciq, contentType, fileType, exportType, searchText, isInternal
    	return new Object[][] { 
    		{ null, null, null, null, null, null, "users", "", true}, 
    		{ "public", null, null, null, null, "WORDPROCESSOR", "users", "", true},
    		{ null, null, null, null, null, null, "ext_collabs", "", true},
    		{ null, null, null, null, null, null, "users", "", false}, 
    		{ "public", null, null, null, null, "WORDPROCESSOR", "users", "", false},
    		{ null, null, null, null, null, null, "ext_collabs", "", false},
    		
        };
    }
    
    @DataProvider(name = "RiskyFilesFilter")
    public static Object[][] RiskyFilesFilter()
    {	
        
    	//exposuretype, objectType, risk, ciq, contentType, fileType, exportType, searchText, isInternal
    	return new Object[][] { 
    		{ null, "OneDrive", null, null, null, "rtf", "risky_docs", "", true},
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
    
    @DataProvider(name = "SubSiteDataProvider")
    public static Object[][] SubSiteDataProvider()
    {	
    	//isthisLastRow is a tweak to find the last row in the dataprovider as it is not possible to find the length at runtime
    	//, isThisLastRow, language, siteurl, title, description, sitetemplate, useuniquepermissions
        return new Object[][] { 

    	{ false, 1033, "blrteamsite", 			"blrteamsite", 			"Team Site created by automation script", 			"STS#0", 		true },
    	{ false, 1033, "blrblanksite", 			"blrblanksite", 		"Blank Site created by automation script", 			"STS#1", 		true },
    	{ false, 1033, "blrdocumentworkspace", 	"blrdocumentworkspace", "Document workspace created by automation script", 	"STS#2", 		true },
    	{ false, 1033, "blrblogsite", 			"blrblogsite", 			"Blog Site created by automation script", 			"BLOG#0",		true },
    	{ false, 1033, "blrdocumentcenter", 	"blrdocumentcenter", 	"Document Center created by automation script", 	"BDR#0", 		true },
    	{ true,  1033, "blrprojectsite", 		"blrprojectsite", 		"Project Site created by automation script", 		"PROJECTSITE#0",true },
        	
        	/*
        	STS#0 Team Site
        	STS#1Blank Site
        	STS#2 Document Workspace
        	MPS#0 Basic Meeting Workspace
        	MPS#1 Blank Meeting Workspace
        	MPS#2 Decision Meeting Workspace
        	MPS#3 Social Meeting Workspace
        	MPS#4 Multipage Meeting Workspace
        	CENTRALADMIN#0 Central Admin Site
        	WIKI#0 Wiki Site
        	BLOG#0 Blog
        	SGS#0 Group Work Site
        	TENANTADMIN#0 Tenant Admin Site
        	APP#0 App Template
        	APPCATALOG#0 App Catalog Site
        	ACCSRV#0 Access Services Site
        	ACCSVC#0 Access Services Site Internal
        	ACCSVC#1 Access Services Site
        	BDR#0 Document Center
        	DEV#0 Developer Site
        	DOCMARKETPLACESITE#0 Academic Library
        	EDISC#0 Discovery Center
        	EDISC#1 Discovery Case
        	OFFILE#0 (obsolete) Records Center
        	OFFILE#1 Records Center
        	OSRV#0 Shared Services Administration Site
        	PPSMASite#0 PerformancePoint
        	BICenterSite#0 Business Intelligence Center
        	SPS#0 SharePoint Portal Server Site
        	SPSPERS#0 SharePoint Portal Server Personal Space
        	SPSPERS#2 Storage And Social SharePoint Portal …
        	SPSPERS#3 Storage Only SharePoint Portal Server…
        	SPSPERS#4 Social Only SharePoint Portal Server …
        	SPSPERS#5 Empty SharePoint Portal Server Person…
        	SPSMSITE#0 Personalization Site
        	SPSTOC#0 Contents area Template
        	SPSTOPIC#0 Topic area template
        	SPSNEWS#0 News Site
        	CMSPUBLISHING#0 Publishing Site
        	BLANKINTERNET#0 Publishing Site
        	BLANKINTERNET#1 Press Releases Site
        	BLANKINTERNET#2 Publishing Site with Workflow
        	SPSNHOME#0 News Site
        	SPSSITES#0 Site Directory
        	SPSCOMMU#0 Community area template
        	SPSREPORTCENTER#0 Report Center
        	SPSPORTAL#0 Collaboration Portal
        	SRCHCEN#0 Enterprise Search Center
        	PROFILES#0 Profiles
        	BLANKINTERNETCONT… Publishing Portal
        	SPSMSITEHOST#0 My Site Host
        	ENTERWIKI#0 Enterprise Wiki
        	PROJECTSITE#0 Project Site
        	PRODUCTCATALOG#0 Product Catalog
        	COMMUNITY#0 Community Site
        	COMMUNITYPORTAL#0 Community Portal
        	SRCHCENTERLITE#0 Basic Search Center
        	SRCHCENTERLITE#1 Basic Search Center
        	visprus#0 Visio Process Repository*/
			
        };
    }
}
