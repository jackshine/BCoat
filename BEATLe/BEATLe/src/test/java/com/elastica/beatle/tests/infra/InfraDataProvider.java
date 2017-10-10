package com.elastica.beatle.tests.infra;

import org.testng.annotations.DataProvider;

import com.elastica.beatle.i18n.I18N;

public class InfraDataProvider {

	@DataProvider
	public static Object [][] UsertoGrp()
	{
		return new Object[][]{
			//user type    	email	id	   		payload      Group   																																																						casetype
			{ "vijay.gangwar+16@infrabeatle.com",  "C18886", "{\"email\":\"vijay.gangwar+16@infrabeatle.com\",\"deleted_groups\":[],\"added_groups\":[\"Blr\"]}","Blr","{\"user\":{\"first_name\":\"AjayEUser\",\"last_name\":\"may\",\"email\":\"vijay.gangwar+16@infrabeatle.com\",\"secondary_user_id\":\"\",\"password\":\"\",\"title\":\"\",\"work_phone\":\"\",\"cell_phone\":\"\",\"access_profiles\":[],\"is_admin\":false,\"is_active\":true,\"notes\":\"\",\"is_dpo\":false}}"}
			
		};
	}
	@DataProvider
	public static Object [][] GrouptoUser()
	{
		return new Object[][]{
			//user type    	email	id	   		payload      Group   																																																						casetype
			{ "vijay.gangwar+54@infrabeatle.com",  "C180", "{\"name\":\"FrontEnd\",\"deleted_users\":[],\"added_users\":[\"vijay.gangwar+54@infrabeatle.com\"]}","FrontEnd"}
			
		};
	}
	@DataProvider
	public static Object[][] createUserData() {
		return new Object[][]{
			//user type    	email	id	   		payload         																																																						casetype
			{ "sysAdmin user","vijay.gangwar+12@infrabeatle.com",  "C18886", "{\"user\":{\"first_name\":\"AjayAdmin\",\"last_name\":\"may\",\"email\":\"vijay.gangwar+12@infrabeatle.com\",\"secondary_user_id\":\"\",\"password\":\"\",\"title\":\"\",\"work_phone\":\"\",\"cell_phone\":\"\",\"access_profiles\":[],\"is_admin\":true,\"is_active\":true,\"notes\":\"\",\"is_dpo\":false}}","normal"},
			{ "End user", "vijay.gangwar+13@infrabeatle.com" , "C18881",  "{\"user\":{\"first_name\":\"AjayEUser\",\"last_name\":\"may\",\"email\":\"vijay.gangwar+13@infrabeatle.com\",\"secondary_user_id\":\"\",\"password\":\"\",\"title\":\"\",\"work_phone\":\"\",\"cell_phone\":\"\",\"access_profiles\":[],\"is_admin\":false,\"is_active\":true,\"notes\":\"\",\"is_dpo\":false}}","normal"},
			{ "inactive Admin", "vijay.gangwar+14@infrabeatle.com" , "TBM",  "{\"user\":{\"first_name\":\"AjayInactiveAdmin\",\"last_name\":\"may\",\"email\":\"vijay.gangwar+14@infrabeatle.com\",\"secondary_user_id\":\"\",\"password\":\"\",\"title\":\"\",\"work_phone\":\"\",\"cell_phone\":\"\",\"access_profiles\":[],\"is_admin\":false,\"is_active\":false,\"notes\":\"\",\"is_dpo\":false}}","normal"},
			{ "inactive user",   "vijay.gangwar+16@infrabeatle.com","C2095",  "{\"user\":{\"first_name\":\"AjayInactiveUser\",\"last_name\":\"may\",\"email\":\"vijay.gangwar+16@infrabeatle.com\",\"secondary_user_id\":\"\",\"password\":\"Matrix$123\",\"title\":\"\",\"work_phone\":\"\",\"cell_phone\":\"\",\"access_profiles\":[],\"is_admin\":false,\"is_active\":false,\"notes\":\"\",\"is_dpo\":false}}","normal"},
			{ "same name dif email", "vijay.gangwar+17@infrabeatle.com"  ,"C2123",  "{\"user\":{\"first_name\":\"AjayInactiveUser\",\"last_name\":\"may\",\"email\":\"vijay.gangwar+17@infrabeatle.com\",\"secondary_user_id\":\"\",\"password\":\"\",\"title\":\"\",\"work_phone\":\"\",\"cell_phone\":\"\",\"access_profiles\":[],\"is_admin\":false,\"is_active\":false,\"notes\":\"\",\"is_dpo\":false}}","normal"},
			{ "same email",   "vijay.gangwar+40@infrabeatle.com","C2124",  "{\"user\":{\"first_name\":\"AjayDup\",\"last_name\":\"may\",\"email\":\"vijay.gangwar+12@infrabeatle.com\",\"secondary_user_id\":\"\",\"password\":\"\",\"title\":\"\",\"work_phone\":\"\",\"cell_phone\":\"\",\"access_profiles\":[],\"is_admin\":false,\"is_active\":false,\"notes\":\"\",\"is_dpo\":false}}","error"},
			{ "DPO user", "vijay.gangwar+68@infrabeatle.com" , "C18881",  "{\"user\":{\"first_name\":\"Vijay\",\"last_name\":\"dpo\",\"email\":\"vijay.gangwar+68@infrabeatle.com\",\"secondary_user_id\":\"\",\"password\":\"\",\"title\":\"\",\"work_phone\":\"\",\"cell_phone\":\"\",\"access_profiles\":[],\"is_admin\":false,\"is_active\":true,\"notes\":\"\",\"is_dpo\":true}}","normal"},
			{ "special char in fname user",  "vijay.gangwar+18@infrabeatle.com"  ,"C2123",  "{\"user\":{\"first_name\":\"AjayInactiveUser##$$##\",\"last_name\":\"may\",\"email\":\"vijay.gangwar+18@infrabeatle.com\",\"secondary_user_id\":\"\",\"password\":\"\",\"title\":\"\",\"work_phone\":\"\",\"cell_phone\":\"\",\"access_profiles\":[],\"is_admin\":false,\"is_active\":false,\"notes\":\"\",\"is_dpo\":false}}","error"},
			//{ "special char in fname user", "abcttest@infrabeatle.com " , "C2079",  blanktes
			{ "max char in fname user", "vijay.gangwar+19@infrabeatle.com" , "C2080",  "{\"user\":{\"first_name\":\"AmaxnamelenthgthestwithmaxassixtyAmaxnamelenthgthestwithmaxassixty\",\"last_name\":\" \",\"email\":\"vijay.gangwar+19@infrabeatle.com \",\"secondary_user_id\":\"\",\"password\":\"\",\"title\":\"\",\"work_phone\":\"\",\"cell_phone\":\"\",\"access_profiles\":[],\"is_admin\":false,\"is_active\":false,\"notes\":\"\",\"is_dpo\": }}","error"},
			{ "max char in lname user",  "vijay.gangwar+20@infrabeatle.com" ,"C2082",  "{\"user\":{\"first_name\":\"AmaxnamelenthgthestwithmaxassixtyAmaxnamelenthgthestwithmaxassixty\",\"last_name\":\" \",\"email\":\"vijay.gangwar+20@infrabeatle.com \",\"secondary_user_id\":\"\",\"password\":\"\",\"title\":\"\",\"work_phone\":\"\",\"cell_phone\":\"\",\"access_profiles\":[],\"is_admin\":false,\"is_active\":false,\"notes\":\"\",\"is_dpo\": }}","error"},
			{ "all field filled",   "vijay.gangwar+22@infrabeatle.com" ,"C2084", "{\"user\":{\"first_name\":\"Allfield\",\"last_name\":\"adfkjads\",\"email\":\"vijay.gangwar+22@infrabeatle.com\",\"secondary_user_id\":\"\",\"password\":\"\",\"title\":\"dsmkjllads;gf\",\"work_phone\":\"1-123445768\",\"cell_phone\":\"1-1234567\",\"access_profiles\":[],\"is_admin\":false,\"is_active\":true,\"notes\":\"dasgkjnv\",\"is_dpo\":false}}","normal"},
			{ "max char in notes field",   "maxnotes.may6@infrabeatle.com","C2086",   "{\"user\":{\"first_name\":\"Allfield\",\"last_name\":\"adfkjads\",\"email\":\"maxnotes.may6@infrabeatle.com\",\"secondary_user_id\":\",\"password\":\",\"title\":\"dsmkjllads;gf\",\"work_phone\":\"1-123445768\",\"cell_phone\":\"1-1234567\",\"access_profiles\":[],\"is_admin\":false,\"is_active\":true,\"notes\":\"max character in notes filed test Amaxnamelenthgthestwithmaxassixty Amaxnamelenthgthestwithmaxassixty####"+
					"max character in notes filed test Amaxnamelenthgthestwithmaxassixty Amaxnamelenthgthestwithmaxassixty####max character in notes filed test Amaxnamelenthgthestwithmaxassixty Amaxnamelenthgthestwithmaxassixty####max character in notes filed test Amaxnamelenthgthestwithmaxassixty Amaxnamelenthgthestwithmaxassixty####max character in notes filed test Amaxnamelenthgthestwithmaxassixty Amaxnamelenthgthestwithmaxassixty####max character in notes filed test Amaxnamelenthgthestwithmaxassixty Amaxnamelenthgthestwithmaxassixty####max character in notes filed test Amaxnamelenthgthestwithmaxassixty Amaxnamelenthgthestwithmaxassixty####max character in notes filed test Amaxnamelenthgthestwithmaxassixty Amaxnamelenthgthestwithmaxassixty####max character in notes filed test Amaxnamelenthgthestwithmaxassixty Amaxnamelenthgthestwithmaxassixty####max character in notes filed test Amaxnamelenthgthestwithmax total char :1025 \",\"is_dpo\":false}}","error"},
			{ "work phone error",   "vijay.19@infrabeatle.com" ,"C2087", "{\"user\":{\"first_name\":\"Aphoneerror1\",\"last_name\":\"adfkjads\",\"email\":\"vijay.19@infrabeatle.com\",\"secondary_user_id\":\",\"password\":\",\"title\":\"dsmkjllads;gf\",\"work_phone\":\"000123445768\",\"cell_phone\":\"1-1234567\",\"access_profiles\":[],\"is_admin\":false,\"is_active\":true,\"notes\":\"dasgkjnv\",\"is_dpo\":false}}","error"},
			{ "cell phone error",   "vijay.19@infrabeatle.com" ,"C2087", "{\"user\":{\"first_name\":\"Aphoneerror2\",\"last_name\":\"adfkjads\",\"email\":\"vijay.19@infrabeatle.com\",\"secondary_user_id\":\",\"password\":\",\"title\":\"dsmkjllads;gf\",\"work_phone\":\"1-123445768\",\"cell_phone\":\"9831234567\",\"access_profiles\":[],\"is_admin\":false,\"is_active\":true,\"notes\":\"dasgkjnv\",\"is_dpo\":false}}","error"},
			{ " special char work phone error",   "vijay.19@infrabeatle.com" ,"C2090", "{\"user\":{\"first_name\":\"Aphoneerror\",\"last_name\":\"adfkjads\",\"email\":\"vijay.19@infrabeatle.com\",\"secondary_user_id\":\",\"password\":\",\"title\":\"dsmkjllads;gf\",\"work_phone\":\"1-123445768$$\",\"cell_phone\":\"1-1234567\",\"access_profiles\":[],\"is_admin\":false,\"is_active\":true,\"notes\":\"dasgkjnv\",\"is_dpo\":false}}","error"},
			{ "special char cell phone error",   "vijay.19@infrabeatle.com" ,"C20891", "{\"user\":{\"first_name\":\"Aphoneerror\",\"last_name\":\"adfkjads\",\"email\":\"vijay.19@infrabeatle.com\",\"secondary_user_id\":\",\"password\":\",\"title\":\"dsmkjllads;gf\",\"work_phone\":\"1-123445768\",\"cell_phone\":\"91-1234567$$$\",\"access_profiles\":[],\"is_admin\":false,\"is_active\":true,\"notes\":\"dasgkjnv\",\"is_dpo\":false}}","error"},
			{ "max number cell phone error",   "vijay.19@infrabeatle.com" ,"C20891", "{\"user\":{\"first_name\":\"Aphoneerror\",\"last_name\":\"adfkjads\",\"email\":\"vijay.19@infrabeatle.com\",\"secondary_user_id\":\",\"password\":\",\"title\":\"dsmkjllads;gf\",\"work_phone\":\"1-123445768\",\"cell_phone\":\"91-123456789012345\",\"access_profiles\":[],\"is_admin\":false,\"is_active\":true,\"notes\":\"dasgkjnv\",\"is_dpo\":false}}","error"},
			{ "number in first name",   "vijay.19@infrabeatle.com" ,"C20891", "{\"user\":{\"first_name\":\"Apho12345\",\"last_name\":\"adfkjads\",\"email\":\"vijay.19@infrabeatle.com\",\"secondary_user_id\":\",\"password\":\",\"title\":\"dsmkjllads;gf\",\"work_phone\":\"1-123445768\",\"cell_phone\":\"91-123456789012345\",\"access_profiles\":[],\"is_admin\":false,\"is_active\":true,\"notes\":\"dasgkjnv\",\"is_dpo\":false}}","error"},
			{ "small letter names",   "vijay.19@infrabeatle.com" ,"C2192", "{\"user\":{\"first_name\":\"alpha\",\"last_name\":\"adfkjads\",\"email\":\"vijay.19@infrabeatle.com\",\"secondary_user_id\":\",\"password\":\",\"title\":\"dsmkjllads;gf\",\"work_phone\":\"1-123445768\",\"cell_phone\":\"91-123456789012345\",\"access_profiles\":[],\"is_admin\":false,\"is_active\":true,\"notes\":\"dasgkjnv\",\"is_dpo\":false}}","error"},
			{ "empty mandatory fields",   "vijay.19@infrabeatle.com" ,"C2077", "{\"user\":{\"first_name\":\"\",\"last_name\":\"\",\"email\":\"\",\"secondary_user_id\":\",\"password\":\",\"title\":\"\",\"work_phone\":\"\",\"cell_phone\":\"\",\"access_profiles\":[],\"is_admin\":false,\"is_active\":true,\"notes\":\"\",\"is_dpo\":false}}","error"},
			{ "Wrong email", "vijay.test15.infrabeatle.com"  ,"C2123",  "{\"user\":{\"first_name\":\"AjayUser\",\"last_name\":\"may\",\"email\":\"vijay.14.infrabeatle.com\",\"secondary_user_id\":\"\",\"password\":\"\",\"title\":\"\",\"work_phone\":\"\",\"cell_phone\":\"\",\"access_profiles\":[],\"is_admin\":false,\"is_active\":false,\"notes\":\"\",\"is_dpo\":false}}","error"},
			{ "bulk operation user","vijay.gangwar+23@infrabeatle.com",  "C18886", "{\"user\":{\"first_name\":\"Adminbulk1\",\"last_name\":\"Bulk\",\"email\":\"vijay.gangwar+23@infrabeatle.com\",\"secondary_user_id\":\"\",\"password\":\"\",\"title\":\"\",\"work_phone\":\"\",\"cell_phone\":\"\",\"access_profiles\":[],\"is_admin\":true,\"is_active\":true,\"notes\":\"\",\"is_dpo\":false}}","normal"},
			{ "bulk operation user","vijay.gangwar+24@infrabeatle.com",  "C18886", "{\"user\":{\"first_name\":\"Adminbulk2\",\"last_name\":\"bulk\",\"email\":\"vijay.gangwar+24@infrabeatle.com\",\"secondary_user_id\":\"\",\"password\":\"\",\"title\":\"\",\"work_phone\":\"\",\"cell_phone\":\"\",\"access_profiles\":[],\"is_admin\":true,\"is_active\":true,\"notes\":\"\",\"is_dpo\":false}}","normal"},
			{ "sysAdmin user","vijay.gangwar+12@infrabeatle.com",  "C18886", "{\"user\":{\"first_name\":\"AjayAdmin\",\"last_name\":\"User\",\"email\":\"vijay.gangwar+12@test123.com\",\"secondary_user_id\":\"\",\"password\":\"\",\"title\":\"\",\"work_phone\":\"\",\"cell_phone\":\"\",\"access_profiles\":[],\"is_admin\":true,\"is_active\":true,\"notes\":\"\",\"is_dpo\":false}}","error"},
			{ "End user","vijay.gangwar+16@test123.com",  "C18886", "{\"user\":{\"first_name\":\"Test\",\"last_name\":\"User\",\"email\":\"vijay.gangwar+16@test123.com\",\"secondary_user_id\":\"\",\"password\":\"\",\"title\":\"\",\"work_phone\":\"\",\"cell_phone\":\"\",\"access_profiles\":[],\"is_admin\":false,\"is_active\":true,\"notes\":\"\",\"is_dpo\":false}}","error"},
			//{ "InvalidLogin",   "Session", "mohammad.usman", "informational",  "User Login Failed!"     C2087             }
			
			
		};
	}
	

	@DataProvider
	public static Object[][] DeleteAll() {
		return new Object[][]{
			//Action    		id	   searchdata		payload        
			{ "delete all", "C18881","", "{\"users\":["+
			"{\"email\":\"admin+1@infrabeatle.com\",\"resource_uri\":\"/infrabeatlecom/api/admin/v1/users/561f84239dfa5152caddbb34/\"},{\"email\":\"vijay.gangwar+24@infrabeatle.com\",\"resource_uri\":\"/infrabeatlecom/api/admin/v1/users/55eebeb29dfa5114d21b9ac3/\"},"+
			"{\"email\":\"vijay.gangwar+68@infrabeatle.com\",\"resource_uri\":\"/infrabeatlecom/api/admin/v1/users/56250b1cbf83124d0f2fcecf/\"},{\"email\":\"vijay.gangwar+1@infra.com\",\"resource_uri\":\"/infrabeatlecom/api/admin/v1/users/562a311e9dfa5112949fc99a/\"},{\"email\":\"vijay.gangwar@elastica.com\",\"resource_uri\":\"/infrabeatlecom/api/admin/v1/users/5630ccd8bf8312104c694f5c/\"},{\"email\":\"admin+7@infrabeatle.com\",\"resource_uri\":\"/infrabeatlecom/api/admin/v1/users/55eebe05bf8312477d680b62/\"},{\"email\":\"admin+71@infrabeatle.com\",\"resource_uri\":\"/infrabeatlecom/api/admin/v1/users/5617a99b9dfa5126eb03b9f2/\"}]}"}
		};
	}

	@DataProvider
	public static Object [][] GrptoUser()
	{
		return new Object[][]{
			//user type    	email	id	   		payload      Group   																																																						casetype
			{ "vijay.gangwar+17@infrabeatle.com",  "C18886", "{\"name\":\"GroupImport\",\"deleted_users\":[],\"added_users\":[\"vijay.gangwar+17@infrabeatle.com\"]}","GroupImport"}
			
		};
	}
	@DataProvider
	public static Object[][] filter() {
		return new Object[][]{
			// type    		id	   		value         
			{ "user", "is_active", "C2148", "false","normal"},
			{ "user", "is_active" ,"C2149", "true","normal"},
			{ "user", "created_by" ,"C2149",  "admin@infrabeatle.com","normal"},
	//		{ "user", "created_by" ,"C2162",  "admin@infrabeatle.com","normal"},
	    	{ "user", "is_admin" ,"C2164",  "true","normal"},
		//	{ "user", "roles" ,"S2164",  "true","normal"},
			{ "user", "is_admin" ,"C2165",  "false","normal"},//
			{ "group", "is_active", "C16204", "false","normal"},
			{ "group", "is_active" ,"C16205", "true","normal"},
			{ "group", "created_by" ,"C16206",  "admin@infrabeatle.com","normal"},
//			{ "group", "created_by" ,"C2162",  "admin@infrabeatle.com","normal"},
			{ "user", "roles" ,"C2164",  "admin","normal"},
	//		{ "user", "roles" ,"S2164",  "sysadmin","normal"},
	//		{ "user", "roles" ,"C2165",  "end_user","normal"},
			{ "profiles", "is_active", "C2148", "false","normal"},
			{ "profiles", "is_active" ,"C2149", "true","normal"},
			{ "profiles", "created_by" ,"C2149",  "admin@infrabeatle.com","normal"},
			{ "profiles", "created_by" ,"C2162",  "admin@infrabeatle.com","normal"},
	//		{ "profiles", "roles" ,"C2164",  "admin","normal"},
	//		{ "profiles", "roles" ,"S2164",  "sysadmin","normal"},
	//		{ "profiles", "roles" ,"C2165",  "end_user","normal"},
//			{ "remove filter", "is_active" ,"C2147",  "true","normal"},
//			//{ "inactive user",   "C2095",  "AjayInactiveUser"}
			
		};
	}
	
	@DataProvider
	public static Object[][] multiFilter() {
		return new Object[][]{
			// type   	is_active created_by roles access_profile       
			{ "user", "true", "admin@infrabeatle.com", "null","null"},
			{ "user", "false", "admin@infrabeatle.com", "null","null"},
			{"user", "true", "admin@infrabeatle.com", "sysadmin","null"},
			{"user", "false", "admin@infrabeatle.com", "sysadmin","null"},
			{"user", "true", "admin@infrabeatle.com", "end_user","null"},
			{"user", "fasle", "admin@infrabeatle.com", "end_user","null"},
			{"user", "true", "admin@infrabeatle.com", "admin","null"},
			{"user", "false", "admin@infrabeatle.com", "admin","null"},
			{"user", "true", "admin@infrabeatle.com", "admin","Admin-all"},
			{"user", "false", "admin@infrabeatle.com", "admin","Admin-all"},
			{"user", "false", "admin@infrabeatle.com", "admin","Admin-all"},
			{"user", "true", "System", "admin","Admin-all"},
			{"user", "false", "null", "null","Admin-all"},
//			//{"user", "false", "null", "null","Salesforce"},
			{"user", "null", "null", "null","Salesforce"},
			{"user", "null", "admin@infrabeatle.com", "admin","null"},
			{"user", "null", "admin@infrabeatle.com", "sysadmin","null"},
			{"user", "null", "admin@infrabeatle.com", "end_user","null"},
			{"group", "true", "admin@infrabeatle.com", "null","null"},
			{"group", "false", "admin@infrabeatle.com", "null","null"},
			{"profiles", "true", "admin@infrabeatle.com", "null","null"},
			{"profiles", "false", "admin@infrabeatle.com", "null","null"},
//			{"removeUserFilter", "null", "null", "null","null"},
//			{"removeGroupFilter", "null", "null", "null","null"},
	//		{"removeProfileFilter", "null", "null", "null","null"},
//			{"removeFilter", "true", "admin@infrabeatle.com", "null","null"}
			
		};
	}
	

	@DataProvider
	public static Object[][] ImportUser() {
		return new Object[][]{
			//user type    		id	   		first name         
			{ "Admin",  "C18886","vijay.admin+33@infrabeatle.com", "Admin_import.csv","normal"},
			{ "German user",  "TBM","vijay.gangwar+6@infrabeatle.com", "German_import_user.csv","normal"},
			{ "French user",  "TBM","vijay.gangwar+11@infrabeatle.com", "French_import_user.csv","normal"},
			{ "Maxican user",  "TBM","vijay.gangwar+8@infrabeatle.com", "Maxican_import_user.csv","normal"},
			{ "Japanese user",  "TBM","vijay.gangwar+15@infrabeatle.com", "Japanese_import_user.csv","normal"},
			{ "unicode user",  "TBM","vijay.gangwar+4@infrabeatle.com", "unicode_import.csv","normal"},
			{ "Bulkuser",   "C18881","vijay.gangwar+25@infrabeatle.com",  "BulkImport.csv","normal"},
			{ "NonExistingGroup",   "C18738", "vijay.gangwar+21@infrabeatle.com", "BulkImportNonexistingGroup.csv","normal"},
			{ "ErrorUser",   "C3473", "vijay.gangwar@elastica.com", "User_import.csv","error"},
			{ "Diff formate",   "C2460", "vijay.gangwar+5@elastica.co", "import_template_diff.csv","error"},
			{ "diff extension",   "C2461", "vijay.123test@infrabeatle.com", "import_template.xls","error"}
			
			
		};
	}

	@DataProvider
	public static Object[][] InactiveUser() {
		return new Object[][]{
			//pl1    		pl2	   		Pl3        
			{ "{\"group\":{\"name\":\"ActiveGroup \",\"description\":\"test group\",\"is_active\":true,\"notes\":\"\"}}", 
				"{\"user\":{\"first_name\":\"ActiveUser\",\"last_name\":\"may\",\"email\":\"vijay.gangwar+33@infrabeatle.com\",\"secondary_user_id\":\"\",\"password\":\"\",\"title\":\"\",\"work_phone\":\"\",\"cell_phone\":\"\",\"access_profiles\":[],\"is_admin\":true,\"is_active\":true,\"notes\":\"\",\"is_dpo\":false}}",
				"{\"email\":\"vijay.gangwar+33@infrabeatle.com\",\"deleted_groups\":[],\"added_groups\":[\"ActiveGroup \"]}"}
		
			
			
		};
	}
	
	@DataProvider
	public static Object[][] RBACImportUser() {
		return new Object[][]{
			//user type    		id	   		first name         
			{ "Detect Admin",  "TBD","vijay.admin+54@infrabeatle.com", "Rbac_AdminImport.csv","normal"},
			{ "other domain admin user supported",   "TBD", "vijay.gangwar+54@seculetbeatle.com", "Rbac_AdminImport_error.csv","normal"}
			
			
		};
	}
	/*
	 * Author: Vijay Gangwar
	 * data provider 
	 */
	@DataProvider
	public static Object[][] ImportGroup() {
		return new Object[][]{
			//user type    		id	   		first name         
			{ "group Import",  "C18886","GroupImport", "elastica_group_import_template.csv","normal"},
			{ "German user",  "TBM", "Sütterlin","German_group_import.csv","normal"},
			{ "French Group",  "TBM", "Crémieux","French_group_import.csv","normal"},
			{ "Maxican Group",  "TBM", "Peña","Maxican_group_import.csv","normal"},
			{ "Japanese group",  "TBM", "算用数字","Japanese_group_import.csv","normal"},
			{ "unicode Group",  "TBM", "ßß®®¥¥ƒ∂√","Unicode_group_import.csv","normal"},
			{ "bulkMixedImport",   "C2460","InactiveGroup1",  "Bulk_group_import.csv","normal"},
			//	{ "diff extension",   "C2461",  "import_template.xls"},
			//	{ "NonExistingGroup",   "C18738",  "BulkImportNonexistingGroup.csv"}

		};
	}
		
	
	@DataProvider
	public static Object[][] editUserData() {
		return new Object[][]{
			// Action    		id	   	   	email						value  updatedField	payload         
			{ "Edit  first name",  "C2104","admin+12@infrabeatle.com","Cail","first_name", "{\"user\":{\"first_name\":\"Cail\",\"last_name\":\"Alain\",\"title\":\"User\",\"secondary_user_id\":\"1234\",\"work_phone\":\"1-23311143\",\"cell_phone\":\"1-23331116\",\"access_profiles\":[],\"is_admin\":false,\"is_active\":true,\"notes\":\"\",\"is_dpo\":false},\"id\":\""+InfraConstants.DUMMY+"\"}","normal"},
			{ "Edit  last name",  "C2104","admin+12@infrabeatle.com","Turing","last_name", "{\"user\":{\"first_name\":\"Cail\",\"last_name\":\"Turing\",\"title\":\"User\",\"secondary_user_id\":\"1234\",\"work_phone\":\"1-23311143\",\"cell_phone\":\"1-23331116\",\"access_profiles\":[],\"is_admin\":false,\"is_active\":true,\"notes\":\"\",\"is_dpo\":false},\"id\":\""+InfraConstants.DUMMY+"\"}","normal"},
			{ "change user to admin",  "C2104","vijay.gangwar+25@infrabeatle.com","true","is_admin", "{\"user\":{\"first_name\":\"Import\",\"last_name\":\"Bulk\",\"title\":\"Engineer\",\"secondary_user_id\":\"\",\"work_phone\":\"1-2333411123\",\"cell_phone\":\"1-2333411123\",\"access_profiles\":[],\"is_admin\":true,\"is_active\":true,\"notes\":\"\",\"is_dpo\":false},\"id\":\""+InfraConstants.DUMMY+"\"}","normal"},
			{ "change admin to user",  "C2104","vijay.gangwar+25@infrabeatle.com","false", "is_admin","{\"user\":{\"first_name\":\"Import\",\"last_name\":\"Bulk\",\"title\":\"Engineer\",\"secondary_user_id\":\"\",\"work_phone\":\"1-2333411123\",\"cell_phone\":\"1-2333411123\",\"access_profiles\":[],\"is_admin\":false,\"is_active\":true,\"notes\":\"\",\"is_dpo\":false},\"id\":\""+InfraConstants.DUMMY+"\"}","normal"},
			{ "update Title to Doctor",  "C2104","vijay.gangwar+25@infrabeatle.com","Doctor", "title","{\"user\":{\"first_name\":\"Import\",\"last_name\":\"Bulk\",\"title\":\"Doctor\",\"secondary_user_id\":\"\",\"work_phone\":\"1-2333411123\",\"cell_phone\":\"1-2333411123\",\"access_profiles\":[],\"is_admin\":false,\"is_active\":true,\"notes\":\"\",\"is_dpo\":false},\"id\":\""+InfraConstants.DUMMY+"\"}","normal"},	
			{ "update workphone and home phone ",  "C2104","vijay.gangwar+25@infrabeatle.com","1-2333411199", "work_phone","{\"user\":{\"first_name\":\"Import\",\"last_name\":\"Bulk\",\"title\":\"Doctor\",\"secondary_user_id\":\"\",\"work_phone\":\"1-2333411199\",\"cell_phone\":\"1-2333411199\",\"access_profiles\":[],\"is_admin\":false,\"is_active\":false,\"notes\":\"\",\"is_dpo\":false},\"id\":\""+InfraConstants.DUMMY+"\"}","normal"},
			{ "update notes and deactivate user ",  "C2104","vijay.gangwar+25@infrabeatle.com","this is test note", "notes","{\"user\":{\"first_name\":\"Import\",\"last_name\":\"Bulk\",\"title\":\"Doctor\",\"secondary_user_id\":\"\",\"work_phone\":\"1-2333411199\",\"cell_phone\":\"1-2333411199\",\"access_profiles\":[],\"is_admin\":false,\"is_active\":false,\"notes\":\"this is test note\",\"is_dpo\":false},\"id\":\""+InfraConstants.DUMMY+"\"}","normal"},
			{ "activate a deactivated user ",  "C2104","vijay.gangwar+25@infrabeatle.com","true", "is_active","{\"user\":{\"first_name\":\"Import\",\"last_name\":\"Bulk\",\"title\":\"Doctor\",\"secondary_user_id\":\"\",\"work_phone\":\"1-2333411199\",\"cell_phone\":\"1-2333411199\",\"access_profiles\":[],\"is_admin\":false,\"is_active\":true,\"notes\":\"this is test note\",\"is_dpo\":false},\"id\":\""+InfraConstants.DUMMY+"\"}","normal"},
			
			
		};
	}
	
	@DataProvider
	public static Object[][] revertUserData() {
		return new Object[][]{
			// Action    		id	   	   	email						value  updatedField	payload         
			{ "Revert user to Active State ","vijay.gangwar+14@infrabeatle.com", "{\"user\":{\"first_name\":\"Edit\",\"last_name\":\"Admin\",\"title\":\"\",\"secondary_user_id\":\"\",\"work_phone\":\"\",\"cell_phone\":\"\",\"access_profiles\":[],\"is_admin\":true,\"is_active\":true,\"notes\":\"\",\"is_dpo\":false},\"id\":\""+InfraConstants.DUMMY+"\"}"},
			{ "Revert user to Inactive State ","vijay.gangwar+14@infrabeatle.com","{\"user\":{\"first_name\":\"Edit\",\"last_name\":\"Admin\",\"title\":\"\",\"secondary_user_id\":\"\",\"work_phone\":\"\",\"cell_phone\":\"\",\"access_profiles\":[],\"is_admin\":true,\"is_active\":false,\"notes\":\"\",\"is_dpo\":false},\"id\":\""+InfraConstants.DUMMY+"\"}"}
			
			
		};
	}
	
	@DataProvider
	public static Object[][] editGroupData() {
		return new Object[][]{
			// Action    		id	   	   	email						value  updatedField	payload         
			{ "Deactivate  first name",  "C2104","Blr","false","is_active", "{\"group\":{\"name\":\"Blr\",\"is_active\":false,\"notes\":\"testgrp123\",\"description\":\"testgrp\"},\"id\":\""+InfraConstants.DUMMY+"\"}","normal"},
			{ "activate  last name",  "C2104","Blr","true","is_active", "{\"group\":{\"name\":\"Blr\",\"is_active\":true,\"notes\":\"testgrp123\",\"description\":\"testgrp\"},\"id\":\""+InfraConstants.DUMMY+"\"}","normal"},
			{ "Edit Group name",  "C2104","Blr","true","is_active", "{\"group\":{\"name\":\"BlrOne\",\"is_active\":true,\"notes\":\"testgrp123\",\"description\":\"testgrp\"},\"id\":\""+InfraConstants.DUMMY+"\"}","error"},		
			{ "Edit notes field name",  "C2104","Blr","true","is_active", "{\"group\":{\"name\":\"Blr\",\"is_active\":true,\"notes\":\"the is a testgrp\",\"description\":\"testgrp\"},\"id\":\""+InfraConstants.DUMMY+"\"}","normal"},
			{ "Edit description field ",  "C2104","Blr","true","is_active", "{\"group\":{\"name\":\"Blr\",\"is_active\":true,\"notes\":\"the is a testgrp\",\"description\":\"testgrp is test\"},\"id\":\""+InfraConstants.DUMMY+"\"}","normal"},
			{ "Edit Group name to empty",  "C2104","Blr","true","is_active", "{\"group\":{\"name\":\"\",\"is_active\":true,\"notes\":\"testgrp123\",\"description\":\"testgrp\"},\"id\":\""+InfraConstants.DUMMY+"\"}","error"},
		};
	}
	@DataProvider
	public static Object[][] groupData() {
		return new Object[][]{
			// type    		id	   		value         
			{ "Add Group", "add", "C2129", "{\"group\":{\"name\":\"BlrTest\",\"description\":\"test group\",\"is_active\":true,\"notes\":\"\"}}","normal","BlrTest"},
			{ "Max char in Name", "add" ,"C2133", "{\"group\":{\"name\":\"BlrTestGroupforMaxCharacterLenthTestBlrTestGroupforMaxCharacterLenthTest\",\"description\":\"testgrp\",\"is_active\":true,\"notes\":\"testgrp\"}}","error","BlrTestGroupforMaxCharacterLenthTestBlrTestGroupforMaxCharacterLenthTest"},
		    { "group name with special char", "add" ,"C213",  "{\"group\":{\"name\":\"Blr##$$\",\"description\":\"testgrp\",\"is_active\":true,\"notes\":\"testgrp\"}}","normal","Blr##$$"},
			{ "user", "add" ,"C2162",  "{\"group\":{\"name\":\"BlrInactivegrp\",\"description\":\"test group\",\"is_active\":false,\"notes\":\"\"}}","normal","BlrInactivegrp"},
			{ "Add Group", "add", "C2129", "{\"group\":{\"name\":\"BlrTest\",\"description\":\"test group\",\"is_active\":true,\"notes\":\"\"}}","normal","BlrTest"},
			{ "duplicate group", "add" ,"C2162",  "{\"group\":{\"name\":\"BlrTest\",\"description\":\"test group\",\"is_active\":true,\"notes\":\"\"}}","error","BlrTest"},
			{ "Active inactive mix", "add" ,"C2162",  "{\"group\":{\"name\":\"InactiveActivegrp\",\"description\":\"test group\",\"is_active\":true,\"notes\":\"\"}}","normal","InactiveActivegrp"},
		//	{ "Active inactive mix", "add" ,"C2162",  "{\"group\":{\"name\":\"InactiveActivegrp\",\"description\":\"test group\",\"is_active\":false,\"notes\":\"\"}}","normal","InactiveActivegrp"},
			
		};
	}
	
	@DataProvider
	public static Object[][] groupDataI18n() {
		return new Object[][]{
			// type    		id	   		value  
//			{ "Name", "add" ,"C2133", "{\"group\":{\"name\":\"Tendukar\",\"description\":\"testgrp\",\"is_active\":true,\"notes\":\"testgrp\"}}","normal","Tendukar"},
			{ "Chinnese language  Group", "add", "C2129", "{\"group\":{\"name\":\""+I18N.getString("groupname","zh_CN")+"\",\"description\":\"蕡蕇蕱 蛃袚觙 谾踘遳 嫷 岈岋\",\"is_active\":true,\"notes\":\""+I18N.getString("notes","zh_CN")+"\"}}","normal",I18N.getString("groupname","zh_CN")},
			{ "Japanese language  Group", "add", "C2129", "{\"group\":{\"name\":\"みỦ馜碜餯\",\"description\":\"みỦ馜碜餯 だぎゃ期詪䄦\",\"is_active\":true,\"notes\":\"みỦ馜碜餯 だぎゃ期詪䄦\"}}","normal","みỦ馜碜餯"},
			{ "French language  Group", "add", "C2129", "{\"group\":{\"name\":\"L'éducation\",\"description\":\"L'éducation doit être gratuite\",\"is_active\":true,\"notes\":\"L'éducation doit être gratuite\"}}","normal","L'éducation"},
			{ "German language  Group", "add", "C2129", "{\"group\":{\"name\":\"KÜCHENUHR\",\"description\":\"DIE KÜCHENUHR\",\"is_active\":true,\"notes\":\"DIE KÜCHENUHR\"}}","normal","KÜCHENUHR"},
			{ "Mexican language  Group", "add", "C2129", "{\"group\":{\"name\":\"Fiestas\",\"description\":\"Fiestas Patrias de Chile\",\"is_active\":true,\"notes\":\"Fiestas Patrias de Chile\"}}","normal","Fiestas"},
			{ "Russian language  Group", "add", "C2129", "{\"group\":{\"name\":\"Москве\",\"description\":\"Я живу2 в Москве\",\"is_active\":true,\"notes\":\"Я живу2 в Москве\"}}","normal","Москве"},
			{ "Portuguese language  Group", "add", "C2129", "{\"group\":{\"name\":\"é fácil\",\"description\":\"é fácil de introduzir\",\"is_active\":true,\"notes\":\"é fácil de introduzir\"}}","normal","é fácil"},
			{ "Hebrew language  Group", "add", "C2129", "{\"group\":{\"name\":\"שלהואהואהוא\",\"description\":\"Hebrew group\",\"is_active\":true,\"notes\":\"שלהואהואהוא\"}}","normal","שלהואהואהוא"},
			{ "Arabic language  Group", "add", "C2129", "{\"group\":{\"name\":\"پاکستان\",\"description\":\"پاکستان - ‎کھیل - ‎ورلڈ - ‎آس پاس\",\"is_active\":true,\"notes\":\"پاکستان - ‎کھیل - ‎ورلڈ - ‎آس پاس\"}}","normal","پاکستان"},
			{ "Hindi language  Group", "add", "C2129", "{\"group\":{\"name\":\"रामपाल यादव\",\"description\":\"रामपाल यादव\",\"is_active\":true,\"notes\":\"रामपाल यादव\"}}","normal","रामपाल यादव"},
//			
//			{ "Max char in Name", "add" ,"C2133", "{\"group\":{\"name\":\"BlrTestGroupforMaxCharacterLenthTestBlrTestGroupforMaxCharacterLenthTest\",\"description\":\"testgrp\",\"is_active\":true,\"notes\":\"testgrp\"}}","error","BlrTestGroupforMaxCharacterLenthTestBlrTestGroupforMaxCharacterLenthTest"},
//		    { "group name with special char", "add" ,"C213",  "{\"group\":{\"name\":\"Blr##$$\",\"description\":\"testgrp\",\"is_active\":true,\"notes\":\"testgrp\"}}","normal","Blr##$$"},
//			{ "user", "add" ,"C2162",  "{\"group\":{\"name\":\"BlrInactivegrp\",\"description\":\"test group\",\"is_active\":false,\"notes\":\"\"}}","normal","BlrInactivegrp"},
//			{ "Add Group", "add", "C2129", "{\"group\":{\"name\":\"BlrTest\",\"description\":\"test group\",\"is_active\":true,\"notes\":\"\"}}","normal","BlrTest"},
//			{ "duplicate group", "add" ,"C2162",  "{\"group\":{\"name\":\"BlrTest\",\"description\":\"test group\",\"is_active\":true,\"notes\":\"\"}}","error","BlrTest"},
//			{ "Active inactive mix", "add" ,"C2162",  "{\"group\":{\"name\":\"InactiveActivegrp\",\"description\":\"test group\",\"is_active\":true,\"notes\":\"\"}}","normal","InactiveActivegrp"},
		//	{ "Active inactive mix", "add" ,"C2162",  "{\"group\":{\"name\":\"InactiveActivegrp\",\"description\":\"test group\",\"is_active\":false,\"notes\":\"\"}}","normal","InactiveActivegrp"},
			
		};
	}
	@DataProvider
	public static Object [][] DelUserfmGrp()
	{
		return new Object[][]{
			//user type    	email	id	   		payload      Group   																																																						casetype
			{ "vijay.gangwar+16@infrabeatle.com",  "C18886", "{\"name\":\"Blr\",\"deleted_users\":[\""+InfraConstants.DUMMY+"\"],\"added_users\":[]}","Blr"}

		};
	}

	@DataProvider
	public static Object[][] deleteUserData() {
		return new Object[][]{
			//user type    		id	   		first name         
			{ "Admin user",  "C18886", "AjayAdmin","vijay.gangwar+12@infrabeatle.com"},
			{ "Imported  user ",  "TBM", "Import","vijay.gangwar+6@infrabeatle.com"},
			{ "End user",   "C18881",  "AjayEUser","vijay.gangwar+13@infrabeatle.com"},
			{ "inactive Admin",   "TBM",  "AjayInactiveAdmin","vijay.gangwar+14@infrabeatle.com"},
		//	{ "inactive user",   "C2095",  "AjayInactiveUser","vijay.gangwar+14@infrabeatle.com"},
			{ "DPO user",   "C2095",  "AugDpo","vijay.gangwar+62@infrabeatle.com"},
			
		};
	}
	
	@DataProvider
	public static Object[][] deleteGroupData() {
		return new Object[][]{
			//Group type    		id	   		Group name         
			{ "Delete group ",  "C18886", "BlrTest"},
			{ "End user",   "C18881",  "BlrTestGroupforMaxCharacterLenthTestBlrTestGroupforMaxCharacterLenthTest"},
			//{ "inactive special char",   "TBM",  "Blr##$$"},
			{ "inactive group",   "C2095",  "BlrInactivegrp"}

		};
	}


	@DataProvider
	public static Object[][] SecondaryIdUserData() {
		return new Object[][]{
			//user type    	email	id	   		payload         																																																						casetype
			{ "Secondry id user","admin+3@infrabeatle.com",  "185824", "{\"user\":{\"first_name\":\"Secondaryid\",\"last_name\":\"Infra\",\"email\":\"admin+3@infrabeatle.com\",\"secondary_user_id\":\"3048@admin+3@infrabeatle.com\",\"password\":\"\",\"title\":\"\",\"work_phone\":\"\",\"cell_phone\":\"\",\"access_profiles\":[],\"is_admin\":false,\"is_active\":true,\"notes\":\"\",\"is_dpo\":false}}","normal"},
			{ "Secondry user", "admin+4@infrabeatle.com" , "C185817",  "{\"user\":{\"first_name\":\"AjayEUser\",\"last_name\":\"may\",\"email\":\"admin+3@infrabeatle.com\",\"secondary_user_id\":\"123\",\"password\":\"\",\"title\":\"\",\"work_phone\":\"\",\"cell_phone\":\"\",\"access_profiles\":[],\"is_admin\":false,\"is_active\":true,\"notes\":\"\",\"is_dpo\":false}}","normal"},
			{ "Secondry multi", "admin+5@infrabeatle.com" , "C185818",  "{\"user\":{\"first_name\":\"SecondaryMulti\",\"last_name\":\"Id\",\"email\":\"admin+4@infrabeatle.com\",\"secondary_user_id\":\"123456@infrabeatle.com\",\"password\":\"\",\"title\":\"\",\"work_phone\":\"\",\"cell_phone\":\"\",\"access_profiles\":[],\"is_admin\":false,\"is_active\":true,\"notes\":\"\",\"is_dpo\":false}}","normal"},
			{ "Secondry Special",   "admin+6@infrabeatle.com","C185821",  "{\"user\":{\"first_name\":\"SecondarySpecial\",\"last_name\":\"Special\",\"email\":\"admin+6@infrabeatle.com\",\"secondary_user_id\":\"123$$#@\",\"password\":\"\",\"title\":\"\",\"work_phone\":\"\",\"cell_phone\":\"\",\"access_profiles\":[],\"is_admin\":false,\"is_active\":true,\"notes\":\"\",\"is_dpo\":false}}","normal"},
			{ "Secondry alphaNumeric", "admin.test@infrabeatle.com"  ,"C185822",  "{\"user\":{\"first_name\":\"SecondryAlpha\",\"last_name\":\"Alpha\",\"email\":\"admin+7@infrabeatle.com\",\"secondary_user_id\":\"abcde12345\",\"password\":\"\",\"title\":\"\",\"work_phone\":\"\",\"cell_phone\":\"\",\"access_profiles\":[],\"is_admin\":false,\"is_active\":true,\"notes\":\"\",\"is_dpo\":false}}","error"},
			{ "SecondryId different domain name",   "gangwar.vijay@gmail.com","C185829",  "{\"user\":{\"first_name\":\"Secondary\",\"last_name\":\"GmailId\",\"email\":\"admin+8@infrabeatle.com\",\"secondary_user_id\":\"gangwar.vijay@gmail.com\",\"password\":\"\",\"title\":\"\",\"work_phone\":\"\",\"cell_phone\":\"\",\"access_profiles\":[],\"is_admin\":false,\"is_active\":true,\"notes\":\"\",\"is_dpo\":false}}","error"},
			{ "Secondry id user","admin+8@infrabeatle.com",  "185824", "{\"user\":{\"first_name\":\"Duplicate\",\"last_name\":\"Secondaryid\",\"email\":\"admin+8@infrabeatle.com\",\"secondary_user_id\":\"3048@admin+3@infrabeatle.com\",\"password\":\"\",\"title\":\"\",\"work_phone\":\"\",\"cell_phone\":\"\",\"access_profiles\":[],\"is_admin\":false,\"is_active\":true,\"notes\":\"\",\"is_dpo\":false}}","normal"},
			{ "special char in fname user",  "vijay.16@elastica.co"  ,"C2123",  "{\"user\":{\"first_name\":\"AjayInactiveUser##$$##\",\"last_name\":\"may\",\"email\":\"vijay.16@elastica.co\",\"secondary_user_id\":\"\",\"password\":\"\",\"title\":\"\",\"work_phone\":\"\",\"cell_phone\":\"\",\"access_profiles\":[],\"is_admin\":false,\"is_active\":false,\"notes\":\"\",\"is_dpo\":false}}","error"},

		};
	}

	@DataProvider
	public static Object[][] ActionData() {
		return new Object[][]{
			//Action    		id	   searchdata		payload        
			{ "deactivate","C18886","vijay.gangwar+23@infrabeatle.com","{\"action\":\"deactivate\",\"users\":[{\"email\":\"vijay.gangwar+23@infrabeatle.com\",\"resource_uri\":\"/infrabeatlecom/api/admin/v1/users/dummy/\"}]}"},
			{ "activate", "C3473","vijay.gangwar+23@infrabeatle.com", "{\"action\":\"activate\",\"users\":[{\"email\":\"vijay.gangwar+23@infrabeatle.com\",\"resource_uri\":\"/infrabeatlecom/api/admin/v1/users/dummy/\"}]}"},
			{ "delete", "C18881","vijay.gangwar+23@infrabeatle.com", "{\"users\":[{\"email\":\"vijay.gangwar+23@infrabeatle.com\",\"resource_uri\":\"/infrabeatlecom/api/admin/v1/users/dummy/\"}]}"}
		};
	}

	
	@DataProvider
	public static Object[][] createDeleteData() {
		return new Object[][]{
			// Action    		id	   		payload         
			{ "Create Delete user","vijay.21@infrabeatle.com",  "C2129", "{\"user\":{\"first_name\":\"AAdelet\",\"last_name\":\"may\",\"email\":\"vijay.21@infrabeatle.com\",\"secondary_user_id\":\"\",\"password\":\"\",\"title\":\"\",\"work_phone\":\"\",\"cell_phone\":\"\",\"access_profiles\":[],\"is_admin\":true,\"is_active\":true,\"notes\":\"\",\"is_dpo\":false}}"},
			//	{ "Active inactive mix", "C2162",  "{\"group\":{\"name\":\"BlrInactivegrp\",\"description\":\"test group\",\"is_active\":true,\"notes\":\"\"}}","normal","BlrInactivegrp"},


		};
	}
	
	@DataProvider
	public static Object[][] createDeleteDataI18n() {
		return new Object[][]{
			// Action    		id	   		payload         
			{ "Create Chinesse user","vijay.gangwar+49@infrabeatle.com",  "C2129", "{\"user\":{\"first_name\":\""+I18N.getString("groupname","zh_CN")+"\",\"last_name\":\""+I18N.getString("groupname","zh_CN")+"\",\"email\":\"vijay.gangwar+49@infrabeatle.com\",\"secondary_user_id\":\"\",\"password\":\"\",\"title\":\"\",\"work_phone\":\"\",\"cell_phone\":\"\",\"access_profiles\":[],\"is_admin\":true,\"is_active\":true,\"notes\":\"\",\"is_dpo\":false}}"},
			{ "Create Japanese user","vijay.gangwar+49@infrabeatle.com",  "C2129", "{\"user\":{\"first_name\":\"みỦ馜碜餯\",\"last_name\":\"だぎゃ期詪䄦\",\"email\":\"vijay.gangwar+49@infrabeatle.com\",\"secondary_user_id\":\"\",\"password\":\"\",\"title\":\"\",\"work_phone\":\"\",\"cell_phone\":\"\",\"access_profiles\":[],\"is_admin\":true,\"is_active\":true,\"notes\":\"\",\"is_dpo\":false}}"},
			{ "Create French user","vijay.gangwar+49@infrabeatle.com",  "C2129", "{\"user\":{\"first_name\":\"L'éducation\",\"last_name\":\"être\",\"email\":\"vijay.gangwar+49@infrabeatle.com\",\"secondary_user_id\":\"\",\"password\":\"\",\"title\":\"\",\"work_phone\":\"\",\"cell_phone\":\"\",\"access_profiles\":[],\"is_admin\":true,\"is_active\":true,\"notes\":\"\",\"is_dpo\":false}}"},
			{ "Create German user","vijay.gangwar+49@infrabeatle.com",  "C2129", "{\"user\":{\"first_name\":\"Fiestas\",\"last_name\":\"Patrias\",\"email\":\"vijay.gangwar+49@infrabeatle.com\",\"secondary_user_id\":\"\",\"password\":\"\",\"title\":\"\",\"work_phone\":\"\",\"cell_phone\":\"\",\"access_profiles\":[],\"is_admin\":true,\"is_active\":true,\"notes\":\"\",\"is_dpo\":false}}"},
			{ "Create Mexican user","vijay.gangwar+49@infrabeatle.com",  "C2129", "{\"user\":{\"first_name\":\"Москве\",\"last_name\":\"живу2\",\"email\":\"vijay.gangwar+49@infrabeatle.com\",\"secondary_user_id\":\"\",\"password\":\"\",\"title\":\"\",\"work_phone\":\"\",\"cell_phone\":\"\",\"access_profiles\":[],\"is_admin\":true,\"is_active\":true,\"notes\":\"\",\"is_dpo\":false}}"},
			{ "Create Russian user","vijay.gangwar+49@infrabeatle.com",  "C2129", "{\"user\":{\"first_name\":\"fácil\",\"last_name\":\"introduzir\",\"email\":\"vijay.gangwar+49@infrabeatle.com\",\"secondary_user_id\":\"\",\"password\":\"\",\"title\":\"\",\"work_phone\":\"\",\"cell_phone\":\"\",\"access_profiles\":[],\"is_admin\":true,\"is_active\":true,\"notes\":\"\",\"is_dpo\":false}}"},
			{ "Create Hebrew user","vijay.gangwar+49@infrabeatle.com",  "C2129", "{\"user\":{\"first_name\":\"שלהואהואהוא\",\"last_name\":\"שלהואהואהוא\",\"email\":\"vijay.gangwar+49@infrabeatle.com\",\"secondary_user_id\":\"\",\"password\":\"\",\"title\":\"\",\"work_phone\":\"\",\"cell_phone\":\"\",\"access_profiles\":[],\"is_admin\":true,\"is_active\":true,\"notes\":\"\",\"is_dpo\":false}}"},
			{ "Create Arabic user","vijay.gangwar+49@infrabeatle.com",  "C2129", "{\"user\":{\"first_name\":\"پاکستان\",\"last_name\":\"ورلڈ\",\"email\":\"vijay.gangwar+49@infrabeatle.com\",\"secondary_user_id\":\"\",\"password\":\"\",\"title\":\"\",\"work_phone\":\"\",\"cell_phone\":\"\",\"access_profiles\":[],\"is_admin\":true,\"is_active\":true,\"notes\":\"\",\"is_dpo\":false}}"},
//			{ "Create Hindi user","vijay.gangwar+49@infrabeatle.com",  "C2129", "{\"user\":{\"first_name\":\"रामपाल\",\"last_name\":\"यादव\",\"email\":\"vijay.gangwar+49@infrabeatle.com\",\"secondary_user_id\":\"\",\"password\":\"\",\"title\":\"\",\"work_phone\":\"\",\"cell_phone\":\"\",\"access_profiles\":[],\"is_admin\":true,\"is_active\":true,\"notes\":\"\",\"is_dpo\":false}}"},
	

		};
	}
	
	@DataProvider
	public Object[][] CreateDeleteProfileI18N() {
		return new Object[][]{
			// type    		id	   		value         
		//	{ "Securlets all OneDrive Profile All", "Create" ,"tbm", "{\"profile\":{\"name\":\"SecurletAllOnedriveAll\",\"description\":\"\",\"is_active\":true,\"notes\":\"\",\"permitted_apps\":{\"Securlets\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"REPORT\",\"VIEW\"]},\"permitted_services\":{\"OneDrive Personal\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"REPORT\",\"VIEW\"]},\"information_level\":{\"information_level\":[{\"DETECT\":[\"ALL\"],\"all\":true},{\"INVESTIGATE\":[\"ALL\"],\"all\":true},{\"AUDIT\":[\"Not anonymized\"]}]},\"global_settings\":{},\"permitted_domains\":[\"__ALL_EL__\"]}}","normal","SecurletAllOnedriveAll"},
			{ "Create Chinesse user", "Create", "C416155", "{\"profile\":{\"name\":\""+I18N.getString("groupname","zh_CN")+"\",\"description\":\"profile\",\"is_active\":true,\"notes\":\"\",\"permitted_apps\":{\"Dashboard\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"VIEW\"],\"Securlets\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"REPORT\",\"VIEW\"]},\"permitted_services\":{\"Dropbox\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"REPORT\",\"VIEW\"]},\"information_level\":{\"information_level\":[{\"DETECT\":[\"ALL\"],\"all\":true},{\"INVESTIGATE\":[\"ALL\"],\"all\":true},{\"AUDIT\":[\"Not anonymized\"]}]},\"global_settings\":{}}}","normal",I18N.getString("groupname","zh_CN")},
			{ "Create Japanese user", "Create", "C416155", "{\"profile\":{\"name\":\"みỦ馜碜餯\",\"description\":\"profile\",\"is_active\":true,\"notes\":\"\",\"permitted_apps\":{\"Dashboard\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"VIEW\"],\"Securlets\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"REPORT\",\"VIEW\"]},\"permitted_services\":{\"Dropbox\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"REPORT\",\"VIEW\"]},\"information_level\":{\"information_level\":[{\"DETECT\":[\"ALL\"],\"all\":true},{\"INVESTIGATE\":[\"ALL\"],\"all\":true},{\"AUDIT\":[\"Not anonymized\"]}]},\"global_settings\":{}}}","normal","みỦ馜碜餯"},
			{ "Create French user", "Create", "C416155", "{\"profile\":{\"name\":\"L'éducation\",\"description\":\"profile\",\"is_active\":true,\"notes\":\"\",\"permitted_apps\":{\"Dashboard\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"VIEW\"],\"Securlets\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"REPORT\",\"VIEW\"]},\"permitted_services\":{\"Dropbox\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"REPORT\",\"VIEW\"]},\"information_level\":{\"information_level\":[{\"DETECT\":[\"ALL\"],\"all\":true},{\"INVESTIGATE\":[\"ALL\"],\"all\":true},{\"AUDIT\":[\"Not anonymized\"]}]},\"global_settings\":{}}}","normal","L'éducation"},
			{ "Create German user", "Create", "C416155", "{\"profile\":{\"name\":\"Fiestas\",\"description\":\"profile\",\"is_active\":true,\"notes\":\"\",\"permitted_apps\":{\"Dashboard\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"VIEW\"],\"Securlets\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"REPORT\",\"VIEW\"]},\"permitted_services\":{\"Dropbox\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"REPORT\",\"VIEW\"]},\"information_level\":{\"information_level\":[{\"DETECT\":[\"ALL\"],\"all\":true},{\"INVESTIGATE\":[\"ALL\"],\"all\":true},{\"AUDIT\":[\"Not anonymized\"]}]},\"global_settings\":{}}}","normal","Fiestas"},
			{ "Create Mexican user", "Create", "C416155", "{\"profile\":{\"name\":\"Москве\",\"description\":\"profile\",\"is_active\":true,\"notes\":\"\",\"permitted_apps\":{\"Dashboard\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"VIEW\"],\"Securlets\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"REPORT\",\"VIEW\"]},\"permitted_services\":{\"Dropbox\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"REPORT\",\"VIEW\"]},\"information_level\":{\"information_level\":[{\"DETECT\":[\"ALL\"],\"all\":true},{\"INVESTIGATE\":[\"ALL\"],\"all\":true},{\"AUDIT\":[\"Not anonymized\"]}]},\"global_settings\":{}}}","normal","Москве"},
			{ "Create Russian user", "Create", "C416155", "{\"profile\":{\"name\":\"fácil\",\"description\":\"profile\",\"is_active\":true,\"notes\":\"\",\"permitted_apps\":{\"Dashboard\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"VIEW\"],\"Securlets\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"REPORT\",\"VIEW\"]},\"permitted_services\":{\"Dropbox\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"REPORT\",\"VIEW\"]},\"information_level\":{\"information_level\":[{\"DETECT\":[\"ALL\"],\"all\":true},{\"INVESTIGATE\":[\"ALL\"],\"all\":true},{\"AUDIT\":[\"Not anonymized\"]}]},\"global_settings\":{}}}","normal","fácil"},
			{ "Create Hebrew user", "Create", "C416155", "{\"profile\":{\"name\":\"שלהואהואהוא\",\"description\":\"profile\",\"is_active\":true,\"notes\":\"\",\"permitted_apps\":{\"Dashboard\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"VIEW\"],\"Securlets\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"REPORT\",\"VIEW\"]},\"permitted_services\":{\"Dropbox\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"REPORT\",\"VIEW\"]},\"information_level\":{\"information_level\":[{\"DETECT\":[\"ALL\"],\"all\":true},{\"INVESTIGATE\":[\"ALL\"],\"all\":true},{\"AUDIT\":[\"Not anonymized\"]}]},\"global_settings\":{}}}","normal","שלהואהואהוא"},
			{ "Create Arabic user", "Create", "C416155", "{\"profile\":{\"name\":\"پاکستان\",\"description\":\"profile\",\"is_active\":true,\"notes\":\"\",\"permitted_apps\":{\"Dashboard\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"VIEW\"],\"Securlets\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"REPORT\",\"VIEW\"]},\"permitted_services\":{\"Dropbox\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"REPORT\",\"VIEW\"]},\"information_level\":{\"information_level\":[{\"DETECT\":[\"ALL\"],\"all\":true},{\"INVESTIGATE\":[\"ALL\"],\"all\":true},{\"AUDIT\":[\"Not anonymized\"]}]},\"global_settings\":{}}}","normal","پاکستان"},
			{ "Create Hindi user", "Create", "C416155", "{\"profile\":{\"name\":\"रामपाल\",\"description\":\"profile\",\"is_active\":true,\"notes\":\"\",\"permitted_apps\":{\"Dashboard\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"VIEW\"],\"Securlets\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"REPORT\",\"VIEW\"]},\"permitted_services\":{\"Dropbox\":[\"EXPORT\",\"IMPORT\",\"MODIFY\",\"REPORT\",\"VIEW\"]},\"information_level\":{\"information_level\":[{\"DETECT\":[\"ALL\"],\"all\":true},{\"INVESTIGATE\":[\"ALL\"],\"all\":true},{\"AUDIT\":[\"Not anonymized\"]}]},\"global_settings\":{}}}","normal","रामपाल"},
	
		};
 }

}
