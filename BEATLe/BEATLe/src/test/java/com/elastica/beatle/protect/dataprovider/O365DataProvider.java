package com.elastica.beatle.protect.dataprovider;

import org.testng.annotations.DataProvider;

public class O365DataProvider {
	
	@DataProvider(name = "DeleteUniquePermissionsDataProvider")
	public static Object[][] getDeleteUniquePermissionsProvider() {
		return new Object[][] {

				new String[] { "DELUNIQ301", "Policy Desc", "1", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "deleteUniquePermissions" },
				new String[] { "DELUNIQ302", "Policy Desc", "1", "Office 365", "Everyone", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "txt", "no", "0,0", "no", "no", "deleteUniquePermissions" },
				new String[] { "DELUNIQ303", "Policy Desc", "1", "Office 365", "Everyone", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "deleteUniquePermissions" },
				new String[] { "DELUNIQ304", "Policy Desc", "1", "Office 365", "Everyone", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PII", "no", "deleteUniquePermissions" },
				new String[] { "DELUNIQ305", "Policy Desc", "1", "Office 365", "Everyone", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PCI", "no", "deleteUniquePermissions" },
				new String[] { "DELUNIQ306", "Policy Desc", "1", "Office 365", "Everyone", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "GLBA", "no", "deleteUniquePermissions" },
				new String[] { "DELUNIQ307", "Policy Desc", "1", "Office 365", "Everyone", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "no", "no", "0,0", "PCI", "no", "deleteUniquePermissions" },
				new String[] { "DELUNIQ308", "Policy Desc", "1", "Office 365", "Everyone", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "no", "no", "0,0", "PII", "no", "deleteUniquePermissions" },
				new String[] { "DELUNIQ309", "Policy Desc", "1", "Office 365", "Everyone", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "rtf", "no", "0,0", "no", "no", "deleteUniquePermissions" },
				new String[] { "DELUNIQ310", "Policy Desc", "1", "Office 365", "Everyone", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "no", "rtf", "0,0", "no", "no", "deleteUniquePermissions" },
				new String[] { "DELUNIQ311", "Policy Desc", "1", "Office 365", "Everyone", "any", "any", "any", "no", "no", "any", "any", "no", "no", "docx", "no", "docx", "0,0", "no", "no", "deleteUniquePermissions" },
				new String[] { "DELUNIQ312", "Policy Desc", "1", "Office 365", "Everyone", "any", "any", "any", "no", "no", "any", "any", "no", "no", "docx", "docx", "no", "0,0", "no", "no", "deleteUniquePermissions" },
				new String[] { "DELUNIQ313", "Policy Desc", "1", "Office 365", "Everyone", "any", "any", "any", "no", "no", "any", "any", "no", "no", "doc", "doc", "no", "0,0", "no", "no", "deleteUniquePermissions" },
				new String[] { "DELUNIQ314", "Policy Desc", "1", "Office 365", "Everyone", "any", "any", "any", "no", "no", "any", "any", "no", "no", "docx", "no", "doc", "0,0", "no", "no", "deleteUniquePermissions" },
				new String[] { "DELUNIQ315", "Policy Desc", "1", "Office 365", "Everyone", "any", "any", "any", "no", "no", "any", "any", "no", "no", "java", "java", "no", "0,0", "no", "no", "deleteUniquePermissions" },
				new String[] { "DELUNIQ316", "Policy Desc", "1", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "java", "no", "no", "0,0", "Source Code", "no", "deleteUniquePermissions" },
				new String[] { "DELUNIQ317", "Policy Desc", "1", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "html", "html", "no", "0,0", "no", "no", "deleteUniquePermissions" },
				new String[] { "DELUNIQ318", "Policy Desc", "2", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "html", "no", "html", "0,0", "no", "no", "deleteUniquePermissions" },
				new String[] { "DELUNIQ319", "Policy Desc", "2", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "exe", "exe", "no", "0,0", "no", "no", "deleteUniquePermissions" },
				new String[] { "DELUNIQ320", "Policy Desc", "2", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "exe", "no", "no", "2097152,0", "no", "no", "deleteUniquePermissions" },
				new String[] { "DELUNIQ321", "Policy Desc", "2", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "jpg", "jpg", "no", "0,0", "no", "no", "deleteUniquePermissions" },
				new String[] { "DELUNIQ322", "Policy Desc", "2", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "pdf", "pdf", "no", "0,0", "no", "no", "deleteUniquePermissions" },
				new String[] { "DELUNIQ323", "Policy Desc", "2", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "pdf", "no", "pdf", "0,0", "no", "no", "deleteUniquePermissions" },
				new String[] { "DELUNIQ324", "Policy Desc", "2", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "pdf", "no", "pdf", "0,0", "HIPAA", "no", "deleteUniquePermissions" },
				new String[] { "DELUNIQ325", "Policy Desc", "2", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "cs", "no", "cs", "0,0", "Source Code", "no", "deleteUniquePermissions" },
				new String[] { "DELUNIQ326", "Policy Desc", "2", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "bin", "no", "no", "0,0", "Encryption", "no", "deleteUniquePermissions" },
				new String[] { "DELUNIQ327", "Policy Desc", "2", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xlsm", "no", "no", "0,0", "VBA Macros", "no", "deleteUniquePermissions" },
				new String[] { "DELUNIQ328", "Policy Desc", "2", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xlsm", "xlsm", "no", "0,0", "no", "no", "deleteUniquePermissions" },
				new String[] { "DELUNIQ329", "Policy Desc", "2", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xls", "no", "no", "0,0", "Encryption", "no", "deleteUniquePermissions" },
				new String[] { "DELUNIQ330", "Policy Desc", "2", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xls", "xls", "no", "0,0", "no", "no", "deleteUniquePermissions" },
				new String[] { "DELUNIQ331", "Policy Desc", "2", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xls", "no", "xls", "0,0", "no", "no", "deleteUniquePermissions" },
				new String[] { "DELUNIQ332", "Policy Desc", "2", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "rar", "no", "0,0", "no", "no", "deleteUniquePermissions" },
				new String[] { "DELUNIQ333", "Policy Desc", "2", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "no", "0,0", "Virus", "no", "deleteUniquePermissions" },
				new String[] { "DELUNIQ334", "Policy Desc", "2", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "pdf", "no", "pdf", "0,0", "PII", "no", "deleteUniquePermissions" },
				new String[] { "DELUNIQ335", "Policy Desc", "2", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "bin", "no", "bin", "0,0", "no", "no", "deleteUniquePermissions" },
				new String[] { "DELUNIQ336", "Policy Desc", "2", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "zip", "no", "zip", "0,0", "no", "no", "deleteUniquePermissions" },
				new String[] { "DELUNIQ337", "Policy Desc", "2", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "zip", "no", "no", "0,0", "Virus", "no", "deleteUniquePermissions" },
				new String[] { "DELUNIQ338", "Policy Desc", "2", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "zip", "no", "zip", "0,0", "Virus", "no", "deleteUniquePermissions" },
				new String[] { "DELUNIQ339", "Policy Desc", "2", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "js", "no", "no", "0,0", "Source Code", "no", "deleteUniquePermissions" },
				new String[] { "DELUNIQ340", "Policy Desc", "2", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "js", "no", "js", "0,0", "no", "no", "deleteUniquePermissions" },
			};
		
	}
	
	@DataProvider(name = "RemoveExternalCollaboratorDataProvider")
	public static Object[][] getRemoveExternalCollaboratorDataProvider() {
		return new Object[][] {

				new String[] { "ODPOLICY101", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "removeCollab" },
				new String[] { "ODPOLICY102", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "txt", "no", "0,0", "no", "no", "removeCollab" },
				new String[] { "ODPOLICY103", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "removeCollab" },
				new String[] { "ODPOLICY104", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PII", "no", "removeCollab" },
				new String[] { "ODPOLICY105", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PCI", "no", "removeCollab" },
				new String[] { "ODPOLICY106", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "GLBA", "no", "removeCollab" },
				new String[] { "ODPOLICY107", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "no", "no", "0,0", "PCI", "no", "removeCollab" },
				new String[] { "ODPOLICY108", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "no", "no", "0,0", "PII", "no", "removeCollab" },
				new String[] { "ODPOLICY109", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "rtf", "no", "0,0", "no", "no", "removeCollab" },
				new String[] { "ODPOLICY110", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "no", "rtf", "0,0", "no", "no", "removeCollab" },
				new String[] { "ODPOLICY111", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "docx", "no", "docx", "0,0", "no", "no", "removeCollab" },
				new String[] { "ODPOLICY112", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "docx", "docx", "no", "0,0", "no", "no", "removeCollab" },
				new String[] { "ODPOLICY113", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "doc", "doc", "no", "0,0", "no", "no", "removeCollab" },
				new String[] { "ODPOLICY114", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "docx", "no", "doc", "0,0", "no", "no", "removeCollab" },
				new String[] { "ODPOLICY115", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "java", "java", "no", "0,0", "no", "no", "removeCollab" },
				new String[] { "ODPOLICY116", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "java", "no", "no", "0,0", "Source Code", "no", "removeCollab" },
				new String[] { "ODPOLICY117", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "html", "html", "no", "0,0", "no", "no", "removeCollab" },
				new String[] { "ODPOLICY118", "Policy Desc", "2", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "html", "no", "html", "0,0", "no", "no", "removeCollab" },
				new String[] { "ODPOLICY119", "Policy Desc", "2", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "exe", "exe", "no", "0,0", "no", "no", "removeCollab" },
				new String[] { "ODPOLICY120", "Policy Desc", "2", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "exe", "no", "no", "2097152,0", "no", "no", "removeCollab" },
				new String[] { "ODPOLICY121", "Policy Desc", "2", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "jpg", "jpg", "no", "0,0", "no", "no", "removeCollab" },
				new String[] { "ODPOLICY122", "Policy Desc", "2", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "pdf", "pdf", "no", "0,0", "no", "no", "removeCollab" },
				new String[] { "ODPOLICY123", "Policy Desc", "2", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "pdf", "no", "pdf", "0,0", "no", "no", "removeCollab" },
				new String[] { "ODPOLICY124", "Policy Desc", "2", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "pdf", "no", "pdf", "0,0", "HIPAA", "no", "removeCollab" },
				new String[] { "ODPOLICY125", "Policy Desc", "2", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "cs", "no", "cs", "0,0", "Source Code", "no", "removeCollab" },
				new String[] { "ODPOLICY126", "Policy Desc", "2", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "bin", "no", "no", "0,0", "Encryption", "no", "removeCollab" },
				new String[] { "ODPOLICY127", "Policy Desc", "2", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xlsm", "no", "no", "0,0", "VBA Macros", "no", "removeCollab" },
				new String[] { "ODPOLICY128", "Policy Desc", "2", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xlsm", "xlsm", "no", "0,0", "no", "no", "removeCollab" },
				new String[] { "ODPOLICY129", "Policy Desc", "2", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xls", "no", "no", "0,0", "Encryption", "no", "removeCollab" },
				new String[] { "ODPOLICY130", "Policy Desc", "2", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xls", "xls", "no", "0,0", "no", "no", "removeCollab" },
				new String[] { "ODPOLICY131", "Policy Desc", "2", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xls", "no", "xls", "0,0", "no", "no", "removeCollab" },
				new String[] { "ODPOLICY132", "Policy Desc", "2", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "rar", "no", "0,0", "no", "no", "removeCollab" },
				new String[] { "ODPOLICY133", "Policy Desc", "2", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "no", "0,0", "Virus", "no", "removeCollab" },
				new String[] { "ODPOLICY134", "Policy Desc", "2", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "pdf", "no", "pdf", "0,0", "PII", "no", "removeCollab" },
				new String[] { "ODPOLICY135", "Policy Desc", "2", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "bin", "no", "bin", "0,0", "no", "no", "removeCollab" },
				new String[] { "ODPOLICY136", "Policy Desc", "2", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "zip", "no", "zip", "0,0", "no", "no", "removeCollab" },
				new String[] { "ODPOLICY137", "Policy Desc", "2", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "zip", "no", "no", "0,0", "Virus", "no", "removeCollab" },
				new String[] { "ODPOLICY138", "Policy Desc", "2", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "zip", "no", "zip", "0,0", "Virus", "no", "removeCollab" },
				new String[] { "ODPOLICY139", "Policy Desc", "2", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "js", "no", "no", "0,0", "Source Code", "no", "removeCollab" },
				new String[] { "ODPOLICY140", "Policy Desc", "2", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "js", "no", "js", "0,0", "no", "no", "removeCollab" },
			};
		
	}
	
	@DataProvider(name = "RemoveLinkDataProvider")
	public static Object[][] getRemoveLinkDataProvider() {
		return new Object[][] {
				new String[] { "ODPOLICY1", "Policy Desc", "DataExposure", "Office 365", "unexposed", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "no", "no" },
				new String[] { "ODPOLICY2", "Policy Desc", "DataExposure", "Office 365", "unexposed", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "rar", "no", "0,0", "Virus", "no", "no" },
				new String[] { "ODPOLICY3", "Policy Desc", "DataExposure", "Office 365", "unexposed", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "xls", "no", "xls", "0,0", "Encryption", "no", "no" },
				new String[] { "ODPOLICY4", "Policy Desc", "DataExposure", "Office 365", "unexposed", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "xls", "xls", "no", "0,0", "Encryption", "no", "no" },
				new String[] { "ODPOLICY5", "Policy Desc", "DataExposure", "Office 365", "unexposed", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "bin", "bin", "no", "0,0", "Encryption", "no", "no" },
				new String[] { "ODPOLICY6", "Policy Desc", "DataExposure", "Office 365", "unexposed", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "xlsm", "xlsm", "no", "0,0", "VBA Macros", "no", "no" },
				new String[] { "ODPOLICY7", "Policy Desc", "DataExposure", "Office 365", "unexposed", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "pdf", "pdf", "no", "0,0", "HIPAA", "no", "no" },
				new String[] { "ODPOLICY8", "Policy Desc", "DataExposure", "Office 365", "unexposed", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "pdf", "pdf", "no", "0,0", "PII", "no", "no" },
				new String[] { "ODPOLICY9", "Policy Desc", "DataExposure", "Office 365", "unexposed", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "cs", "cs", "no", "0,0", "Source Code", "no", "no" },
				new String[] { "ODPOLICY10", "Policy Desc", "DataExposure", "Office 365", "unexposed", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "java", "java", "no", "0,0", "Source Code", "no", "no" },
				new String[] { "ODPOLICY11", "Policy Desc", "DataExposure", "Office 365", "unexposed", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "txt", "txt", "0,0", "GLBA", "no", "no" },
				new String[] { "ODPOLICY12", "Policy Desc", "DataExposure", "Office 365", "unexposed", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "txt", "txt", "0,0", "PII", "no", "no" },
				new String[] { "ODPOLICY13", "Policy Desc", "DataExposure", "Office 365", "unexposed", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "txt", "txt", "0,0", "PCI", "no", "no" },
					
				new String[] { "ODPOLICY14", "Policy Desc", "1", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "unshare" },
				new String[] { "ODPOLICY15", "Policy Desc", "1", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "txt", "no", "0,0", "no", "no", "unshare" },
				new String[] { "ODPOLICY16", "Policy Desc", "1", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "unshare" },
				new String[] { "ODPOLICY17", "Policy Desc", "1", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PII", "no", "unshare" },
				new String[] { "ODPOLICY18", "Policy Desc", "1", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PCI", "no", "unshare" },
				new String[] { "ODPOLICY19", "Policy Desc", "1", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "GLBA", "no", "unshare" },
				new String[] { "ODPOLICY20", "Policy Desc", "1", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "no", "no", "0,0", "PCI", "no", "unshare" },
				new String[] { "ODPOLICY21", "Policy Desc", "1", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "no", "no", "0,0", "PII", "no", "unshare" },
				new String[] { "ODPOLICY22", "Policy Desc", "1", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "rtf", "no", "0,0", "no", "no", "unshare" },
				new String[] { "ODPOLICY23", "Policy Desc", "1", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "no", "rtf", "0,0", "no", "no", "unshare" },
				new String[] { "ODPOLICY24", "Policy Desc", "1", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "docx", "no", "docx", "0,0", "no", "no", "unshare" },
				new String[] { "ODPOLICY25", "Policy Desc", "1", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "docx", "docx", "no", "0,0", "no", "no", "unshare" },
				new String[] { "ODPOLICY26", "Policy Desc", "1", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "doc", "doc", "no", "0,0", "no", "no", "unshare" },
				new String[] { "ODPOLICY27", "Policy Desc", "1", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "docx", "no", "doc", "0,0", "no", "no", "unshare" },
				new String[] { "ODPOLICY28", "Policy Desc", "1", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "java", "java", "no", "0,0", "no", "no", "unshare" },
				new String[] { "ODPOLICY29", "Policy Desc", "1", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "java", "no", "no", "0,0", "Source Code", "no", "unshare" },
				new String[] { "ODPOLICY30", "Policy Desc", "1", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "html", "html", "no", "0,0", "no", "no", "unshare" },
				new String[] { "ODPOLICY31", "Policy Desc", "2", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "html", "no", "html", "0,0", "no", "no", "unshare" },
				new String[] { "ODPOLICY32", "Policy Desc", "2", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "exe", "exe", "no", "0,0", "no", "no", "unshare" },
				new String[] { "ODPOLICY33", "Policy Desc", "2", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "exe", "no", "no", "2097152,0", "no", "no", "unshare" },
				new String[] { "ODPOLICY34", "Policy Desc", "2", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "jpg", "jpg", "no", "0,0", "no", "no", "unshare" },
				new String[] { "ODPOLICY35", "Policy Desc", "2", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "pdf", "pdf", "no", "0,0", "no", "no", "unshare" },
				new String[] { "ODPOLICY36", "Policy Desc", "2", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "pdf", "no", "pdf", "0,0", "no", "no", "unshare" },
				new String[] { "ODPOLICY37", "Policy Desc", "2", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "pdf", "no", "pdf", "0,0", "HIPAA", "no", "unshare" },
				new String[] { "ODPOLICY38", "Policy Desc", "2", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "cs", "no", "cs", "0,0", "Source Code", "no", "unshare" },
				new String[] { "ODPOLICY39", "Policy Desc", "2", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "bin", "no", "no", "0,0", "Encryption", "no", "unshare" },
				new String[] { "ODPOLICY40", "Policy Desc", "2", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xlsm", "no", "no", "0,0", "VBA Macros", "no", "unshare" },
				new String[] { "ODPOLICY41", "Policy Desc", "2", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xlsm", "xlsm", "no", "0,0", "no", "no", "unshare" },
				new String[] { "ODPOLICY42", "Policy Desc", "2", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xls", "no", "no", "0,0", "Encryption", "no", "unshare" },
				new String[] { "ODPOLICY43", "Policy Desc", "2", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xls", "xls", "no", "0,0", "no", "no", "unshare" },
				new String[] { "ODPOLICY44", "Policy Desc", "2", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xls", "no", "xls", "0,0", "no", "no", "unshare" },
				new String[] { "ODPOLICY45", "Policy Desc", "2", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "rar", "no", "0,0", "no", "no", "unshare" },
				new String[] { "ODPOLICY46", "Policy Desc", "2", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "no", "0,0", "Virus", "no", "unshare" },
				new String[] { "ODPOLICY47", "Policy Desc", "2", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "pdf", "no", "pdf", "0,0", "PII", "no", "unshare" },
				new String[] { "ODPOLICY48", "Policy Desc", "2", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "zip", "no", "zip", "0,0", "Virus", "no", "unshare" },
				new String[] { "ODPOLICY49", "Policy Desc", "2", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "zip", "no", "no", "0,0", "Virus", "no", "unshare" },
				new String[] { "ODPOLICY50", "Policy Desc", "2", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "zip", "no", "zip", "0,0", "no", "no", "unshare" },
				new String[] { "ODPOLICY51", "Policy Desc", "2", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "js", "no", "no", "0,0", "Source Code", "no", "unshare" },
				new String[] { "ODPOLICY52", "Policy Desc", "2", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "js", "no", "js", "0,0", "no", "no", "unshare" },	
				new String[] { "ODPOLICY52UsrException", "Policy Desc", "2", "Office 365", "internal", "any", "any", "any", "admin@protecto365autobeatle.com", "no", "any", "any", "no", "no", "js", "no", "js", "0,0", "no", "no", "unshare" },
				new String[] { "ODPOLICY52GrpException", "Policy Desc", "2", "Office 365", "internal", "any", "any", "any", "no", "Protecto365Group", "any", "any", "no", "no", "js", "no", "js", "0,0", "no", "no", "unshare" },				
				new String[] { "ODPOLICY53", "Policy Desc", "1", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "PreDefDict", "unshare" },
				new String[] { "ODPOLICY54", "Policy Desc", "1", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "PreDefTerms", "unshare" },
				new String[] { "ODPOLICY55", "Policy Desc", "2", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "js", "no", "js", "0,0", "no", "CustomTerms", "unshare" },				
	
		};
		
	}
	
	@DataProvider(name = "UpdateExternalCollaboratorDataProvider")
	public static Object[][] getUpdateExternalCollaboratorDataProvider() {
		return new Object[][] {

				new String[] { "ODPOLICY201", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "updateCollabContribute" },
				new String[] { "ODPOLICY202", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "txt", "no", "0,0", "no", "no", "updateCollabContribute" },
				new String[] { "ODPOLICY203", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "updateCollabContribute" },
				new String[] { "ODPOLICY204", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PII", "no", "updateCollabContribute" },
				new String[] { "ODPOLICY205", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PCI", "no", "updateCollabContribute" },
				new String[] { "ODPOLICY206", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "GLBA", "no", "updateCollabContribute" },
				new String[] { "ODPOLICY207", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "no", "no", "0,0", "PCI", "no", "updateCollabContribute" },
				new String[] { "ODPOLICY208", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "no", "no", "0,0", "PII", "no", "updateCollabContribute" },
				new String[] { "ODPOLICY209", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "rtf", "no", "0,0", "no", "no", "updateCollabContribute" },
				new String[] { "ODPOLICY210", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "no", "rtf", "0,0", "no", "no", "updateCollabContribute" },
				new String[] { "ODPOLICY211", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "docx", "no", "docx", "0,0", "no", "no", "updateCollabContribute" },
				new String[] { "ODPOLICY212", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "docx", "docx", "no", "0,0", "no", "no", "updateCollabContribute" },
				new String[] { "ODPOLICY213", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "doc", "doc", "no", "0,0", "no", "no", "updateCollabContribute" },
				new String[] { "ODPOLICY214", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "docx", "no", "doc", "0,0", "no", "no", "updateCollabContribute" },
				new String[] { "ODPOLICY215", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "java", "java", "no", "0,0", "no", "no", "updateCollabContribute" },
				new String[] { "ODPOLICY216", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "java", "no", "no", "0,0", "Source Code", "no", "updateCollabContribute" },
				new String[] { "ODPOLICY217", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "html", "html", "no", "0,0", "no", "no", "updateCollabContribute" },
				new String[] { "ODPOLICY218", "Policy Desc", "2", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "html", "no", "html", "0,0", "no", "no", "updateCollabRead" },
				new String[] { "ODPOLICY219", "Policy Desc", "2", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "exe", "exe", "no", "0,0", "no", "no", "updateCollabRead" },
				new String[] { "ODPOLICY220", "Policy Desc", "2", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "exe", "no", "no", "2097152,0", "no", "no", "updateCollabRead" },
				new String[] { "ODPOLICY221", "Policy Desc", "2", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "jpg", "jpg", "no", "0,0", "no", "no", "updateCollabRead" },
				new String[] { "ODPOLICY222", "Policy Desc", "2", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "pdf", "pdf", "no", "0,0", "no", "no", "updateCollabRead" },
				new String[] { "ODPOLICY223", "Policy Desc", "2", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "pdf", "no", "pdf", "0,0", "no", "no", "updateCollabRead" },
				new String[] { "ODPOLICY224", "Policy Desc", "2", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "pdf", "no", "pdf", "0,0", "HIPAA", "no", "updateCollabRead" },
				new String[] { "ODPOLICY225", "Policy Desc", "2", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "cs", "no", "cs", "0,0", "Source Code", "no", "updateCollabRead" },
				new String[] { "ODPOLICY226", "Policy Desc", "2", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "bin", "no", "no", "0,0", "Encryption", "no", "updateCollabRead" },
				new String[] { "ODPOLICY227", "Policy Desc", "2", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xlsm", "no", "no", "0,0", "VBA Macros", "no", "updateCollabRead" },
				new String[] { "ODPOLICY228", "Policy Desc", "2", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xlsm", "xlsm", "no", "0,0", "no", "no", "updateCollabRead" },
				new String[] { "ODPOLICY229", "Policy Desc", "2", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xls", "no", "no", "0,0", "Encryption", "no", "updateCollabRead" },
				new String[] { "ODPOLICY230", "Policy Desc", "2", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xls", "xls", "no", "0,0", "no", "no", "updateCollabRead" },
				new String[] { "ODPOLICY231", "Policy Desc", "2", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xls", "no", "xls", "0,0", "no", "no", "updateCollabRead" },
				new String[] { "ODPOLICY232", "Policy Desc", "2", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "rar", "no", "0,0", "no", "no", "updateCollabRead" },
				new String[] { "ODPOLICY233", "Policy Desc", "2", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "no", "0,0", "Virus", "no", "updateCollabRead" },
				new String[] { "ODPOLICY234", "Policy Desc", "2", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "pdf", "no", "pdf", "0,0", "PII", "no", "updateCollabRead" },
				new String[] { "ODPOLICY235", "Policy Desc", "2", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "bin", "no", "bin", "0,0", "no", "no", "updateCollabRead" },
				new String[] { "ODPOLICY236", "Policy Desc", "2", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "zip", "no", "zip", "0,0", "no", "no", "updateCollabRead" },
				new String[] { "ODPOLICY237", "Policy Desc", "2", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "zip", "no", "no", "0,0", "Virus", "no", "updateCollabRead" },
				new String[] { "ODPOLICY238", "Policy Desc", "2", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "zip", "no", "zip", "0,0", "Virus", "no", "updateCollabRead" },
				new String[] { "ODPOLICY239", "Policy Desc", "2", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "js", "no", "no", "0,0", "Source Code", "no", "updateCollabRead" },
				new String[] { "ODPOLICY240", "Policy Desc", "2", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "js", "no", "js", "0,0", "no", "no", "updateCollabRead" },
				
				
				new String[] { "ODPOLICY241", "Policy Desc", "2", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "zip", "no", "zip", "1048576,2097152", "Virus", "no", "updateCollabRead" },
				new String[] { "ODPOLICY242", "Policy Desc", "2", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "zip", "no", "zip", "0,2097152", "Virus", "no", "updateCollabRead" },
				new String[] { "ODPOLICY243", "Policy Desc", "2", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "zip", "no", "zip", "1048576,0", "Virus", "no", "updateCollabRead" },
			};
		
	}
	
	@DataProvider(name = "UpdateFilePermissionsDataProvider")
	public static Object[][] getUpdateFilePermissionsDataProvider() {
		return new Object[][] {
				new String[] { "ODPOLICY401", "Policy Desc", "2", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "everyoneRead" },
				new String[] { "ODPOLICY402", "Policy Desc", "2", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "txt", "no", "0,0", "no", "no", "everyoneRead" },
				new String[] { "ODPOLICY403", "Policy Desc", "2", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "everyoneRead" },
				new String[] { "ODPOLICY404", "Policy Desc", "2", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PII", "no", "everyoneRead" },
				new String[] { "ODPOLICY405", "Policy Desc", "2", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PCI", "no", "everyoneRead" },
				new String[] { "ODPOLICY406", "Policy Desc", "2", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "GLBA", "no", "everyoneRead" },
				new String[] { "ODPOLICY407", "Policy Desc", "2", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "no", "no", "0,0", "PCI", "no", "everyoneRead" },
				new String[] { "ODPOLICY408", "Policy Desc", "2", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "no", "no", "0,0", "PII", "no", "everyoneRead" },
				new String[] { "ODPOLICY409", "Policy Desc", "2", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "rtf", "no", "0,0", "no", "no", "everyoneRead" },
				new String[] { "ODPOLICY410", "Policy Desc", "2", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "no", "rtf", "0,0", "no", "no", "everyoneRead" },
				new String[] { "ODPOLICY411", "Policy Desc", "2", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "docx", "no", "docx", "0,0", "no", "no", "readExceptExternal" },
				new String[] { "ODPOLICY412", "Policy Desc", "2", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "docx", "docx", "no", "0,0", "no", "no", "readExceptExternal" },
				new String[] { "ODPOLICY413", "Policy Desc", "2", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "doc", "doc", "no", "0,0", "no", "no", "readExceptExternal" },
				new String[] { "ODPOLICY414", "Policy Desc", "2", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "docx", "no", "doc", "0,0", "no", "no", "readExceptExternal" },
				new String[] { "ODPOLICY415", "Policy Desc", "2", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "java", "java", "no", "0,0", "no", "no", "readExceptExternal" },
				new String[] { "ODPOLICY416", "Policy Desc", "2", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "java", "no", "no", "0,0", "Source Code", "no", "readExceptExternal" },
				new String[] { "ODPOLICY417", "Policy Desc", "2", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "html", "html", "no", "0,0", "no", "no", "readExceptExternal" },
				new String[] { "ODPOLICY418", "Policy Desc", "2", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "html", "no", "html", "0,0", "no", "no", "readExceptExternal" },
				new String[] { "ODPOLICY419", "Policy Desc", "2", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "exe", "exe", "no", "0,0", "no", "no", "readExceptExternal" },
				new String[] { "ODPOLICY420", "Policy Desc", "2", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "exe", "no", "no", "2097152,0", "no", "no", "readExceptExternal" },
				new String[] { "ODPOLICY421", "Policy Desc", "1", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "jpg", "jpg", "no", "0,0", "no", "no", "everyoneContribute" },
				new String[] { "ODPOLICY422", "Policy Desc", "1", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "pdf", "pdf", "no", "0,0", "no", "no", "everyoneContribute" },
				new String[] { "ODPOLICY423", "Policy Desc", "1", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "pdf", "no", "pdf", "0,0", "no", "no", "everyoneContribute" },
				new String[] { "ODPOLICY424", "Policy Desc", "1", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "pdf", "no", "pdf", "0,0", "HIPAA", "no", "everyoneContribute" },
				new String[] { "ODPOLICY425", "Policy Desc", "1", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "cs", "no", "cs", "0,0", "Source Code", "no", "everyoneContribute" },
				new String[] { "ODPOLICY426", "Policy Desc", "1", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "bin", "no", "no", "0,0", "Encryption", "no", "everyoneContribute" },
				new String[] { "ODPOLICY427", "Policy Desc", "1", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xlsm", "no", "no", "0,0", "VBA Macros", "no", "everyoneContribute" },
				new String[] { "ODPOLICY428", "Policy Desc", "1", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xlsm", "xlsm", "no", "0,0", "no", "no", "everyoneContribute" },
				new String[] { "ODPOLICY429", "Policy Desc", "1", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xls", "no", "no", "0,0", "Encryption", "no", "everyoneContribute" },
				new String[] { "ODPOLICY430", "Policy Desc", "1", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xls", "xls", "no", "0,0", "no", "no", "everyoneContribute" },
				new String[] { "ODPOLICY431", "Policy Desc", "1", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xls", "no", "xls", "0,0", "no", "no", "contributeExceptExternal" },
				new String[] { "ODPOLICY432", "Policy Desc", "1", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "rar", "no", "0,0", "no", "no", "contributeExceptExternal" },
				new String[] { "ODPOLICY433", "Policy Desc", "1", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "no", "0,0", "Virus", "no", "contributeExceptExternal" },
				new String[] { "ODPOLICY434", "Policy Desc", "1", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "pdf", "no", "pdf", "0,0", "PII", "no", "contributeExceptExternal" },
				new String[] { "ODPOLICY435", "Policy Desc", "1", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "zip", "no", "zip", "0,0", "Virus", "no", "contributeExceptExternal" },
				new String[] { "ODPOLICY436", "Policy Desc", "1", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "zip", "no", "no", "0,0", "Virus", "no", "contributeExceptExternal" },
				new String[] { "ODPOLICY437", "Policy Desc", "1", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "zip", "no", "zip", "0,0", "no", "no", "contributeExceptExternal" },
				new String[] { "ODPOLICY438", "Policy Desc", "1", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "js", "no", "no", "0,0", "Source Code", "no", "contributeExceptExternal" },
				new String[] { "ODPOLICY439", "Policy Desc", "1", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "js", "no", "js", "0,0", "no", "no", "contributeExceptExternal" },			
			};
	}
	
	
	@DataProvider(name = "OneDriveExposureDataProvider")
	public static Object[][] getOneDriveExposureDataProvider() {
		return new Object[][] {
			new String[] { "ODSHAREDWITH1",     "Policy Desc", "DataExposure", "Office 365", "external",                     "admin@protecto365autobeatle.com", "any", "any", "no", "no", "mayurbelekar@hotmail.com", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "no", "external", "yes" },
			new String[] { "ODSHAREDWITH2",     "Policy Desc", "DataExposure", "Office 365", "external",                     "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "mayurbelekar@hotmail.com", "no", "txt", "no", "txt", "0,0", "no", "no", "no", "external", "no" },
			new String[] { "ODMULTIEXPOSURE3",  "Policy Desc", "DataExposure", "Office 365", "public,external,all",          "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "no", "external", "no" },
			new String[] { "ODMULTIEXPOSURE4",  "Policy Desc", "DataExposure", "Office 365", "public,external,all",          "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "no", "public", "no" },
			new String[] { "ODMULTIEXPOSURE5",  "Policy Desc", "DataExposure", "Office 365", "public,external,all",          "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "no", "external,public", "yes" },
			new String[] { "ODMULTIEXPOSURE6",  "Policy Desc", "DataExposure", "Office 365", "public,internal,all",          "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "no", "internal", "no" },
			new String[] { "ODMULTIEXPOSURE7",  "Policy Desc", "DataExposure", "Office 365", "public,internal,all",          "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "no", "public", "no" },
			new String[] { "ODMULTIEXPOSURE8",  "Policy Desc", "DataExposure", "Office 365", "public,internal,all",          "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "no", "public,internal", "yes" },
			new String[] { "ODMULTIEXPOSURE9",  "Policy Desc", "DataExposure", "Office 365", "external,internal,all",        "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "no", "internal", "no" },
			new String[] { "ODMULTIEXPOSURE10", "Policy Desc", "DataExposure", "Office 365", "external,internal,all",        "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "no", "external", "no" },
			new String[] { "ODMULTIEXPOSURE11", "Policy Desc", "DataExposure", "Office 365", "external,internal,all",        "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "no", "external,internal", "yes" },
			new String[] { "ODMULTIEXPOSURE12", "Policy Desc", "DataExposure", "Office 365", "external,internal,public,all", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "no", "external,internal", "no" },
			new String[] { "ODMULTIEXPOSURE13", "Policy Desc", "DataExposure", "Office 365", "external,internal,public,all", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "no", "public,internal", "no" },
			new String[] { "ODMULTIEXPOSURE14", "Policy Desc", "DataExposure", "Office 365", "external,internal,public,all", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "no", "external,public", "no" },
			new String[] { "ODMULTIEXPOSURE15", "Policy Desc", "DataExposure", "Office 365", "external,internal,public,all", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "no", "external,internal,public", "yes" },
			new String[] { "ODMULTIEXPOSURE16", "Policy Desc", "DataExposure", "Office 365", "external,internal,any",        "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "no", "external", "yes" },
			new String[] { "ODMULTIEXPOSURE17", "Policy Desc", "DataExposure", "Office 365", "external,internal,any",        "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "no", "external,internal", "yes" },
			new String[] { "ODMULTIEXPOSURE18", "Policy Desc", "DataExposure", "Office 365", "public,internal,any",          "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "no", "internal", "yes" },
			new String[] { "ODMULTIEXPOSURE19", "Policy Desc", "DataExposure", "Office 365", "public,internal,any",          "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "no", "public,internal", "yes" },
			new String[] { "ODMULTIEXPOSURE20", "Policy Desc", "DataExposure", "Office 365", "public,external,any",          "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "no", "public", "yes" },
			new String[] { "ODMULTIEXPOSURE21", "Policy Desc", "DataExposure", "Office 365", "public,external,any",          "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "no", "public,external", "yes" },
			new String[] { "ODMULTIEXPOSURE22", "Policy Desc", "DataExposure", "Office 365", "public,external,internal,any", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "no", "public,external", "yes" },
			new String[] { "ODMULTIEXPOSURE23", "Policy Desc", "DataExposure", "Office 365", "public,external,internal,any", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "no", "internal", "yes" },
			new String[] { "ODMULTIEXPOSURE24", "Policy Desc", "DataExposure", "Office 365", "public,external,internal,any", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "no", "public,external,internal", "yes" },
			new String[] { "ODMULTIEXPOSURE25", "Policy Desc", "DataExposure", "Office 365", "public,external,internal,any", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "no", "unexposed", "no" },
			
		};
	}
	
	@DataProvider(name = "ODrivePolicyViolation")
	public static Object[][] getPolicyViolationsInputData() {
		return new Object[][] {
				new String[] { "ODPVPOL01", "Policy Desc", "DataExposure", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "rar", "no", "0,0", "Virus", "no", "no" },
				new String[] { "ODPVPOL02", "Policy Desc", "DataExposure", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "html", "html", "no", "0,0", "no", "no", "no" },
				new String[] { "ODPVPOL03", "Policy Desc", "DataExposure", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "exe", "exe", "no", "2097152,6291456", "no", "no", "no" },
				new String[] { "ODPVPOL04", "Policy Desc", "DataExposure", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "txt", "no", "0,0", "PII", "no", "no" },
				new String[] { "ODPVPOL05", "Policy Desc", "DataExposure", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "txt", "no", "0,0", "PCI", "no", "no" },
				new String[] { "ODPVPOL06", "Policy Desc", "DataExposure", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "txt", "no", "0,0", "GLBA", "no", "no" },
				new String[] { "ODPVPOL07", "Policy Desc", "DataExposure", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "zip", "zip", "no", "0,0", "Virus", "no", "no" },
				new String[] { "ODPVPOL08", "Policy Desc", "DataExposure", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "bin", "bin", "no", "0,0", "Encryption", "no", "no" },
				new String[] { "ODPVPOL09", "Policy Desc", "DataExposure", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "avi", "avi", "no", "0,0", "no", "no", "no" },
				new String[] { "ODPVPOL10", "Policy Desc", "DataExposure", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "rar", "no", "0,0", "Virus", "no", "no" },
				new String[] { "ODPVPOL11", "Policy Desc", "DataExposure", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "docx", "docx", "no", "0,0", "Source Code", "no", "no" },
				new String[] { "ODPVPOL12", "Policy Desc", "DataExposure", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "cs", "cs", "no", "0,0", "Source Code", "no", "no" },
				new String[] { "ODPVPOL13", "Policy Desc", "DataExposure", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "java", "java", "no", "0,0", "Source Code", "no", "no" },
				new String[] { "ODPVPOL14", "Policy Desc", "DataExposure", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "cs", "cs", "no", "0,0", "HIPAA", "no", "no" },
				new String[] { "ODPVPOL15", "Policy Desc", "DataExposure", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "jpg", "jpg", "no", "0,0", "no", "no", "no" },
				new String[] { "ODPVPOL16", "Policy Desc", "DataExposure", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xls", "xls", "no", "0,0", "Encryption", "no", "no" },
				new String[] { "ODPVPOL17", "Policy Desc", "DataExposure", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xlsm", "xlsm", "no", "0,0", "VBA Macros", "no", "no" },
				new String[] { "ODPVPOL18", "Policy Desc", "DataExposure", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "js", "js", "no", "0,0", "Source Code", "no", "no" },				
				new String[] { "ODPVPOL19", "Policy Desc", "DataExposure", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "mov", "mov", "no", "0,0", "no", "no", "no" },
				new String[] { "ODPVPOL20", "Policy Desc", "DataExposure", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "mp4", "mp4", "no", "0,0", "no", "no", "no" },
				new String[] { "ODPVPOL21", "Policy Desc", "DataExposure", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "mxf", "mxf", "no", "0,0", "no", "no", "no" },								
				new String[] { "ODPVPOL22", "Policy Desc", "DataExposure", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "webm", "webm", "no", "0,0", "no", "no", "no" },				
				new String[] { "ODPVPOL23", "Policy Desc", "DataExposure", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "pdf", "pdf", "no", "0,0", "PII", "no", "no" },
				new String[] { "ODPVPOL24", "Policy Desc", "DataExposure", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "pdf", "pdf", "no", "0,0", "PCI", "no", "no" },
				new String[] { "ODPVPOL25", "Policy Desc", "DataExposure", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "pdf", "pdf", "no", "0,0", "HIPAA", "no", "no" },				
				new String[] { "ODPVPOL26", "Policy Desc", "DataExposure", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "rtf", "no", "0,0", "PII", "no", "no" },
				new String[] { "ODPVPOL27", "Policy Desc", "DataExposure", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "rtf", "no", "0,0", "PCI", "no", "no" },
				new String[] { "ODPVPOL28", "Policy Desc", "DataExposure", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "rtf", "no", "0,0", "GLBA", "no", "no" },
				new String[] { "ODPVPOL29", "Policy Desc", "DataExposure", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "doc", "doc", "no", "0,0", "PII", "no", "no" },
				new String[] { "ODPVPOL30", "Policy Desc", "DataExposure", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "doc", "doc", "no", "0,0", "PCI", "no", "no" },
				new String[] { "ODPVPOL31", "Policy Desc", "DataExposure", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "doc", "doc", "no", "0,0", "GLBA", "no", "no" },
				new String[] { "ODPVPOL32", "Policy Desc", "DataExposure", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "doc", "doc", "no", "0,0", "GLBA", "no", "no" },	
				new String[] { "ODPVPOL33", "Policy Desc", "DataExposure", "Office 365", "public,external,all",          "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "no", "external"},
				new String[] { "ODPVPOL34", "Policy Desc", "DataExposure", "Office 365", "public,internal,all",          "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "no", "public"},
				new String[] { "ODPVPOL35", "Policy Desc", "DataExposure", "Office 365", "internal,external,all",        "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "no", "internal"},
				new String[] { "ODPVPOL36", "Policy Desc", "DataExposure", "Office 365", "external,internal,public,all", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "no", "external"},
				new String[] { "ODPVPOL37", "Policy Desc", "DataExposure", "Office 365", "public,external,any",          "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "no", "external"},
				new String[] { "ODPVPOL38", "Policy Desc", "DataExposure", "Office 365", "public,external,any",          "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "no", "public"},
				new String[] { "ODPVPOL39", "Policy Desc", "DataExposure", "Office 365", "internal,external,any",        "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "no", "external"},
				new String[] { "ODPVPOL40", "Policy Desc", "DataExposure", "Office 365", "internal,external,any",        "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "no", "internal"},
				new String[] { "ODPVPOL42", "Policy Desc", "DataExposure", "Office 365", "public,internal,any",          "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "no", "public"},
				new String[] { "ODPVPOL43", "Policy Desc", "DataExposure", "Office 365", "public,internal,any",          "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "no", "internal"},
				new String[] { "ODPVPOL45", "Policy Desc", "DataExposure", "Office 365", "public,internal,external,any", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "no", "external"},
				new String[] { "ODPVPOL46", "Policy Desc", "DataExposure", "Office 365", "public,internal,external,any", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "no", "internal"},
				new String[] { "ODPVPOL47", "Policy Desc", "DataExposure", "Office 365", "public,internal,external,any", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "no", "public"},
				new String[] { "ODPVPOL48", "Policy Desc", "DataExposure", "Office 365", "public,external,any",          "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "no", "public"},
				new String[] { "ODPVPOL49", "Policy Desc", "DataExposure", "Office 365", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "txt", "no", "0,0", "no", "no", "no" },
				new String[] { "ODPVPOL50", "Policy Desc", "DataExposure", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "pdf", "pdf", "no", "0,0", "no", "no", "no" },
				new String[] { "ODPVPOL51", "Policy Desc", "DataExposure", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "pdf", "pdf", "no", "0,0", "no", "no", "no" },				
				new String[] { "ODPVPOL52", "Policy Desc", "DataExposure", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "js", "no", "js", "0,0", "no", "no", "unshare" },				
				new String[] { "ODPVPOL52UsrException", "Policy Desc", "DataExposure", "Office 365", "internal", "any", "any", "any", "admin@protecto365autobeatle.com", "no", "any", "any", "no", "no", "js", "no", "js", "0,0", "no", "no", "unshare" },
				new String[] { "ODPVPOL52GrpException", "Policy Desc", "DataExposure", "Office 365", "internal", "any", "any", "any", "no", "Protecto365Group", "any", "any", "no", "no", "js", "no", "js", "0,0", "no", "no", "unshare" },
				new String[] { "ODPVPOL53", "Policy Desc", "DataExposure", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "txt", "no", "0,0", "FERPA", "no", "no" },
				new String[] { "ODPVPOL54GrpInt", "Policy Desc", "2", "Office 365", "internal", "any", "Protecto365Group", "any", "no", "no", "any", "any", "no", "no", "js", "no", "js", "0,0", "no", "no", "unshare" },
				new String[] { "ODPVPOL54GrpExt", "Policy Desc", "DataExposure", "Office 365", "external", "any", "Protecto365Group", "any", "no", "no", "any", "any", "no", "no", "pdf", "pdf", "no", "0,0", "no", "no", "no" },				
				
		};				
	}	
	
	@DataProvider(name = "ODriveRemediation")
	public static Object[][] getOneDriveRemediationData() {
		return new Object[][] {
				new String[] { "ODUPDATEFILEPERM1", "Policy Desc", "2", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "everyoneRead" },
				new String[] { "ODUPDATEFILEPERM2", "Policy Desc", "2", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "docx", "no", "docx", "0,0", "no", "no", "readExceptExternal" },
				new String[] { "ODUPDATEFILEPERM3", "Policy Desc", "1", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "jpg", "jpg", "no", "0,0", "no", "no", "everyoneContribute" },
				new String[] { "ODUPDATEFILEPERM4", "Policy Desc", "1", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "pdf", "no", "pdf", "0,0", "PII", "no", "contributeExceptExternal" },				
				new String[] { "ODUPDATEFILEPERM5", "Policy Desc", "2", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "everyoneDesign" },
				new String[] { "ODUPDATEFILEPERM6", "Policy Desc", "2", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "designExceptExternal" },
				new String[] { "ODUPDATEFILEPERM7", "Policy Desc", "2", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "everyoneEdit" },
				new String[] { "ODUPDATEFILEPERM8", "Policy Desc", "2", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "editExceptExternal" },
				new String[] { "ODUPDATEFILEPERM9", "Policy Desc", "2", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "everyoneFullControl" },
				new String[] { "ODUPDATEFILEPERM10", "Policy Desc", "2", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "fullcontrolExceptExternal" },
				new String[] { "ODREMOVELINK", "Policy Desc", "1", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "unshare" },
				new String[] { "ODREMOVECOLLAB", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "removeCollab" },
				new String[] { "ODUPDATECOLLAB1", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "updateCollabContribute" },
				new String[] { "ODUPDATECOLLAB2", "Policy Desc", "2", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "pdf", "no", "pdf", "0,0", "PII", "no", "updateCollabRead" },
				new String[] { "ODUPDATECOLLAB3", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "updateCollabDesign" },
				new String[] { "ODUPDATECOLLAB4", "Policy Desc", "2", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "pdf", "no", "pdf", "0,0", "PII", "no", "updateCollabEdit" },
				new String[] { "ODUPDATECOLLAB5", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "updateCollabFullControl" },
				// multi remediations
				new String[] { "ODREMCOLLABUPDATEFILEPERM", "Policy Desc", "2", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "everyoneRead,removeCollab" },
				new String[] { "ODUPDCOLLABUPDATEFILEPERM", "Policy Desc", "2", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "everyoneRead,updateCollabRead" },
				
				new String[] { "ODREMCOLLABREMLINK", "Policy Desc", "2", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "unshare,removeCollab" },
				new String[] { "ODUPDCOLLABREMLINK", "Policy Desc", "2", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "unshare,updateCollabRead" },				
				
				
				
				
				
		};
	}
	
	@DataProvider(name = "O365MailPolicyData")
	public static Object[][] getO365MailPolicyData() {
		return new Object[][] {
				new String[] { "ODMPOLICY01", "Policy Desc", "DataExposure", "Office 365 Email", "unexposed", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "testuser1@protecto365autobeatle.com", "any", "no", "no", "txt", "txt", "no", "0,0", "PII", "no", "deleteEmailAttachment" },
				new String[] { "ODMPOLICY02", "Policy Desc", "DataExposure", "Office 365 Email", "unexposed", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "testuser1@protecto365autobeatle.com", "any", "no", "no", "txt", "txt", "no", "0,0", "PCI", "no", "deleteEmailAttachment" },
				new String[] { "ODMPOLICY03", "Policy Desc", "DataExposure", "Office 365 Email", "unexposed", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "testuser1@protecto365autobeatle.com", "any", "no", "no", "txt", "txt", "no", "0,0", "GLBA", "no", "deleteEmailAttachment" },
				new String[] { "ODMPOLICY04", "Policy Desc", "DataExposure", "Office 365 Email", "unexposed", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "testuser1@protecto365autobeatle.com", "any", "no", "no", "rtf", "rtf", "no", "0,0", "PCI", "no", "deleteEmailAttachment" },
				new String[] { "ODMPOLICY05", "Policy Desc", "DataExposure", "Office 365 Email", "unexposed", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "testuser1@protecto365autobeatle.com", "any", "no", "no", "rtf", "rtf", "no", "0,0", "PII", "no", "deleteEmailAttachment" },
				new String[] { "ODMPOLICY06", "Policy Desc", "DataExposure", "Office 365 Email", "unexposed", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "testuser1@protecto365autobeatle.com", "any", "no", "no", "pdf", "pdf", "no", "0,0", "HIPAA", "no", "deleteEmailAttachment" },
				new String[] { "ODMPOLICY07", "Policy Desc", "DataExposure", "Office 365 Email", "unexposed", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "testuser1@protecto365autobeatle.com", "any", "no", "no", "cs",  "cs", "no",  "0,0", "Source Code", "no", "deleteEmailAttachment" },
				new String[] { "ODMPOLICY08", "Policy Desc", "DataExposure", "Office 365 Email", "unexposed", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "testuser1@protecto365autobeatle.com", "any", "no", "no", "bin", "bin", "no", "0,0", "Encryption", "no", "deleteEmailAttachment" },
				new String[] { "ODMPOLICY09", "Policy Desc", "DataExposure", "Office 365 Email", "unexposed", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "testuser1@protecto365autobeatle.com", "any", "no", "no", "xlsm", "xlsm", "no", "0,0", "VBA Macros", "no", "deleteEmailAttachment" },
				new String[] { "ODMPOLICY10", "Policy Desc", "DataExposure", "Office 365 Email", "unexposed", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "testuser1@protecto365autobeatle.com", "any", "no", "no", "xls", "xls", "no", "0,0", "Encryption", "no", "deleteEmailAttachment" },
				new String[] { "ODMPOLICY11", "Policy Desc", "DataExposure", "Office 365 Email", "unexposed", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "testuser1@protecto365autobeatle.com", "any", "no", "no", "rar", "rar", "no", "0,0", "Virus", "no", "deleteEmailAttachment" },
				new String[] { "ODMPOLICY12", "Policy Desc", "DataExposure", "Office 365 Email", "external", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "mayurbelekar@hotmail.com", "any", "no", "no", "txt", "txt", "no", "0,0", "PII", "no", "deleteEmailAttachment" },
				new String[] { "ODMPOLICY13", "Policy Desc", "DataExposure", "Office 365 Email", "external", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "mayurbelekar@hotmail.com", "any", "no", "no", "txt", "txt", "no", "0,0", "PCI", "no", "deleteEmailAttachment" },
				new String[] { "ODMPOLICY14", "Policy Desc", "DataExposure", "Office 365 Email", "external", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "mayurbelekar@hotmail.com", "any", "no", "no", "txt", "txt", "no", "0,0", "GLBA", "no", "deleteEmailAttachment" },
				new String[] { "ODMPOLICY15", "Policy Desc", "DataExposure", "Office 365 Email", "external", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "mayurbelekar@hotmail.com", "any", "no", "no", "rtf", "rtf", "no", "0,0", "PCI", "no", "deleteEmailAttachment" },
				new String[] { "ODMPOLICY16", "Policy Desc", "DataExposure", "Office 365 Email", "external", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "mayurbelekar@hotmail.com", "any", "no", "no", "rtf", "rtf", "no", "0,0", "PII", "no", "deleteEmailAttachment" },
				new String[] { "ODMPOLICY17", "Policy Desc", "DataExposure", "Office 365 Email", "external", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "mayurbelekar@hotmail.com", "any", "no", "no", "pdf", "pdf", "no", "0,0", "HIPAA", "no", "deleteEmailAttachment" },
				new String[] { "ODMPOLICY18", "Policy Desc", "DataExposure", "Office 365 Email", "external", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "mayurbelekar@hotmail.com", "any", "no", "no", "cs",  "cs", "no",  "0,0", "Source Code", "no", "deleteEmailAttachment" },
				new String[] { "ODMPOLICY19", "Policy Desc", "DataExposure", "Office 365 Email", "external", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "mayurbelekar@hotmail.com", "any", "no", "no", "bin", "bin", "no", "0,0", "Encryption", "no", "deleteEmailAttachment" },
				new String[] { "ODMPOLICY20", "Policy Desc", "DataExposure", "Office 365 Email", "external", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "mayurbelekar@hotmail.com", "any", "no", "no", "xlsm", "xlsm", "no", "0,0", "VBA Macros", "no", "deleteEmailAttachment" },
				new String[] { "ODMPOLICY21", "Policy Desc", "DataExposure", "Office 365 Email", "external", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "mayurbelekar@hotmail.com", "any", "no", "no", "xls", "xls", "no", "0,0", "Encryption", "no", "deleteEmailAttachment" },
				new String[] { "ODMPOLICY22", "Policy Desc", "DataExposure", "Office 365 Email", "external", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "mayurbelekar@hotmail.com", "any", "no", "no", "rar", "rar", "no", "0,0", "Virus", "no", "deleteEmailAttachment" },
				new String[] { "ODMPOLICY23", "Policy Desc", "DataExposure", "Office 365 Email", "external", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "mayurbelekar@hotmail.com", "any", "no", "no", "txt", "txt", "no", "0,0", "PII", "no", "deleteEmailBody" },
				new String[] { "ODMPOLICY24", "Policy Desc", "DataExposure", "Office 365 Email", "external", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "mayurbelekar@hotmail.com", "any", "no", "no", "rtf", "rtf", "no", "0,0", "PCI", "no", "deleteEmailBody" },
				new String[] { "ODMPOLICY25", "Policy Desc", "DataExposure", "Office 365 Email", "external", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "mayurbelekar@hotmail.com", "any", "no", "no", "txt", "txt", "no", "0,0", "GLBA", "no", "deleteEmailBody" },		 
				new String[] { "ODMPOLICY26", "Policy Desc", "DataExposure", "Office 365 Email", "unexposed", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "testuser1@protecto365autobeatle.com", "any", "no", "no", "txt", "txt", "no", "0,0", "PII", "no", "deleteEmailBody" },
				new String[] { "ODMPOLICY27", "Policy Desc", "DataExposure", "Office 365 Email", "unexposed", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "testuser1@protecto365autobeatle.com", "any", "no", "no", "rtf", "rtf", "no", "0,0", "PCI", "no", "deleteEmailBody" },
				new String[] { "ODMPOLICY28", "Policy Desc", "DataExposure", "Office 365 Email", "unexposed", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "testuser1@protecto365autobeatle.com", "any", "no", "no", "txt", "txt", "no", "0,0", "GLBA", "no", "deleteEmailBody" },		 
				//new String[] { "ODMPOLICY29", "Policy Desc", "DataExposure", "Office 365 Email", "unexposed", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "testuser1@protecto365autobeatle.com", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "PreDefDict", "deleteEmailAttachment" },
				//new String[] { "ODMPOLICY30", "Policy Desc", "DataExposure", "Office 365 Email", "unexposed", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "testuser1@protecto365autobeatle.com", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "PreDefTerms", "deleteEmailAttachment" },		
				
				
		};
	}
	
	@DataProvider(name = "O365MailPolicySanityData")
	public static Object[][] getO365MailPolicySanityData() {
		return new Object[][] {
				new String[] { "ODMPOLICY", "Policy Desc", "DataExposure", "Office 365 Email", "unexposed", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "testuser1@protecto365autobeatle.com", "any", "no", "no", "txt", "txt", "no", "0,0", "PII", "no", "deleteEmailAttachment" },
				
				
		};
	}
	
	@DataProvider(name = "PolicyImpactData")
	public static Object[][] getPolicyImpactData() {
		return new Object[][] {
				new String[] { "PIPOLICY01", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "txt", "no", "0,0", "no", "no", "extcollab" },
				new String[] { "PIPOLICY02", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "extcollab" },
				new String[] { "PIPOLICY03", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PII", "no", "extcollab" },
				new String[] { "PIPOLICY04", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PCI", "no", "extcollab" },
				new String[] { "PIPOLICY05", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "GLBA", "no", "extcollab" },
				new String[] { "PIPOLICY06", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "no", "no", "0,0", "PCI", "no", "extcollab" },
				new String[] { "PIPOLICY07", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "no", "no", "0,0", "PII", "no", "extcollab" },
				new String[] { "PIPOLICY08", "Policy Desc", "2", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "everyoneRead" },
				new String[] { "PIPOLICY09", "Policy Desc", "1", "Office 365", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "txt", "no", "0,0", "no", "no", "no" },				
				new String[] { "PIPOLICY10", "Policy Desc", "DataExposure", "Office 365", "unexposed", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "no", "no" },
				new String[] { "PIPOLICY11", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "FERPA", "no", "extcollab" },				
		
		};				
	}		

	@DataProvider(name = "SitePolicyViolation")
	public static Object[][] getO365SitesPolicyViolation(){
		return new Object[][]{
			new String[] { "SITE_EXPO_01", "Policy Desc", "Guest Reader-Read,Everyone-Read", "Office 365 Site", "public,internal", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "no" },
			new String[] { "SITE_EXPO_02", "Policy Desc", "Guest Reader-Read,Everyone except external users-Read,Mayur Belekar-Contribute", "Office 365 Site", "public,internal", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "no" },
		};
	}
	
	@DataProvider(name = "SiteRemediation")
	public static Object[][] getO365SitesRemediation(){
		return new Object[][]{
			//Remove Sharing
			new String[] { "SITE_REMOVESHARE_01", "Policy Desc", "Everyone-Design",       "Office 365 Site", "external", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "removeShare" },
			new String[] { "SITE_REMOVESHARE_02", "Policy Desc", "Everyone-Read",         "Office 365 Site", "external", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "removeShare" },
			new String[] { "SITE_REMOVESHARE_03", "Policy Desc", "Everyone-Full Control", "Office 365 Site", "internal", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "removeShare" },
			new String[] { "SITE_REMOVESHARE_04", "Policy Desc", "Everyone-Edit",         "Office 365 Site", "internal", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "removeShare" },
			new String[] { "SITE_REMOVESHARE_05", "Policy Desc", "Everyone-Contribute",   "Office 365 Site", "internal", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "removeShare" },
			new String[] { "SITE_REMOVESHARE_06", "Policy Desc", "Guest Reader-Read",   "Office 365 Site", "public", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "removeShare" },
			new String[] { "SITE_REMOVESHARE_07", "Policy Desc", "Guest Contributor-Contribute",   "Office 365 Site", "public", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "removeShare" },
			
			new String[] { "SITE_REMOVESHARE_06", "Policy Desc", "Everyone except external users-Design",       "Office 365 Site", "internal", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "removeShare" },
			new String[] { "SITE_REMOVESHARE_07", "Policy Desc", "Everyone except external users-Read",         "Office 365 Site", "internal", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "removeShare" },
			new String[] { "SITE_REMOVESHARE_08", "Policy Desc", "Everyone except external users-Full Control", "Office 365 Site", "internal", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "removeShare" },
			new String[] { "SITE_REMOVESHARE_09", "Policy Desc", "Everyone except external users-Edit",         "Office 365 Site", "internal", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "removeShare" },
			new String[] { "SITE_REMOVESHARE_10", "Policy Desc", "Everyone except external users-Contribute",   "Office 365 Site", "internal", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "removeShare" },
			
			//Update Sharing
			new String[] { "SITE_UPDATESHARE_01", "Policy Desc", "Everyone-Design", "Office 365 Site", "internal", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "everyoneRead" },
			new String[] { "SITE_UPDATESHARE_02", "Policy Desc", "Everyone-Design", "Office 365 Site", "internal", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "everyoneContribute" },
			new String[] { "SITE_UPDATESHARE_03", "Policy Desc", "Everyone-Design", "Office 365 Site", "internal", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "everyoneEdit" },
			new String[] { "SITE_UPDATESHARE_04", "Policy Desc", "Everyone-Design", "Office 365 Site", "internal", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "everyoneFullControl" },
			new String[] { "SITE_UPDATESHARE_05", "Policy Desc", "Everyone-Design", "Office 365 Site", "internal", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "everyoneExceptExternalUserRead" },
			new String[] { "SITE_UPDATESHARE_06", "Policy Desc", "Everyone-Design", "Office 365 Site", "internal", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "everyoneExceptExternalUserContribute" },
			new String[] { "SITE_UPDATESHARE_07", "Policy Desc", "Everyone-Design", "Office 365 Site", "internal", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "everyoneExceptExternalUserDesign" },
			new String[] { "SITE_UPDATESHARE_08", "Policy Desc", "Everyone-Design", "Office 365 Site", "internal", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "everyoneExceptExternalUserEdit" },
			new String[] { "SITE_UPDATESHARE_09", "Policy Desc", "Everyone-Design", "Office 365 Site", "internal", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "everyoneExceptExternalUserFullControl" },
			
			new String[] { "SITE_UPDATESHARE_10", "Policy Desc", "Everyone-Read", "Office 365 Site", "internal", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "everyoneContribute" },
			new String[] { "SITE_UPDATESHARE_11", "Policy Desc", "Everyone-Read", "Office 365 Site", "internal", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "everyoneEdit" },
			new String[] { "SITE_UPDATESHARE_12", "Policy Desc", "Everyone-Read", "Office 365 Site", "internal", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "everyoneDesign" },
			new String[] { "SITE_UPDATESHARE_13", "Policy Desc", "Everyone-Read", "Office 365 Site", "internal", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "everyoneFullControl" },
			new String[] { "SITE_UPDATESHARE_14", "Policy Desc", "Everyone-Read", "Office 365 Site", "internal", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "everyoneExceptExternalUserRead" },
			new String[] { "SITE_UPDATESHARE_15", "Policy Desc", "Everyone-Read", "Office 365 Site", "internal", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "everyoneExceptExternalUserContribute" },
			new String[] { "SITE_UPDATESHARE_16", "Policy Desc", "Everyone-Read", "Office 365 Site", "internal", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "everyoneExceptExternalUserDesign" },
			new String[] { "SITE_UPDATESHARE_17", "Policy Desc", "Everyone-Read", "Office 365 Site", "internal", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "everyoneExceptExternalUserEdit" },
			new String[] { "SITE_UPDATESHARE_18", "Policy Desc", "Everyone-Read", "Office 365 Site", "internal", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "everyoneExceptExternalUserFullControl" },
			
			new String[] { "SITE_UPDATESHARE_19", "Policy Desc", "Everyone-Full Control", "Office 365 Site", "internal", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "everyoneContribute" },
			new String[] { "SITE_UPDATESHARE_20", "Policy Desc", "Everyone-Full Control", "Office 365 Site", "internal", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "everyoneEdit" },
			new String[] { "SITE_UPDATESHARE_21", "Policy Desc", "Everyone-Full Control", "Office 365 Site", "internal", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "everyoneDesign" },
			new String[] { "SITE_UPDATESHARE_22", "Policy Desc", "Everyone-Full Control", "Office 365 Site", "internal", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "everyoneRead" },
			new String[] { "SITE_UPDATESHARE_23", "Policy Desc", "Everyone-Full Control", "Office 365 Site", "internal", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "everyoneExceptExternalUserRead" },
			new String[] { "SITE_UPDATESHARE_24", "Policy Desc", "Everyone-Full Control", "Office 365 Site", "internal", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "everyoneExceptExternalUserContribute" },
			new String[] { "SITE_UPDATESHARE_25", "Policy Desc", "Everyone-Full Control", "Office 365 Site", "internal", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "everyoneExceptExternalUserDesign" },
			new String[] { "SITE_UPDATESHARE_26", "Policy Desc", "Everyone-Full Control", "Office 365 Site", "internal", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "everyoneExceptExternalUserEdit" },
			new String[] { "SITE_UPDATESHARE_27", "Policy Desc", "Everyone-Full Control", "Office 365 Site", "internal", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "everyoneExceptExternalUserFullControl" },
			
			new String[] { "SITE_UPDATESHARE_28", "Policy Desc", "Everyone-Edit", "Office 365 Site", "internal", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "everyoneContribute" },
			new String[] { "SITE_UPDATESHARE_29", "Policy Desc", "Everyone-Edit", "Office 365 Site", "internal", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "everyoneRead" },
			new String[] { "SITE_UPDATESHARE_30", "Policy Desc", "Everyone-Edit", "Office 365 Site", "internal", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "everyoneDesign" },
			new String[] { "SITE_UPDATESHARE_31", "Policy Desc", "Everyone-Edit", "Office 365 Site", "internal", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "everyoneFullControl" },
			new String[] { "SITE_UPDATESHARE_32", "Policy Desc", "Everyone-Edit", "Office 365 Site", "internal", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "everyoneExceptExternalUserRead" },
			new String[] { "SITE_UPDATESHARE_33", "Policy Desc", "Everyone-Edit", "Office 365 Site", "internal", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "everyoneExceptExternalUserContribute" },
			new String[] { "SITE_UPDATESHARE_34", "Policy Desc", "Everyone-Edit", "Office 365 Site", "internal", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "everyoneExceptExternalUserDesign" },
			new String[] { "SITE_UPDATESHARE_35", "Policy Desc", "Everyone-Edit", "Office 365 Site", "internal", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "everyoneExceptExternalUserEdit" },
			new String[] { "SITE_UPDATESHARE_36", "Policy Desc", "Everyone-Edit", "Office 365 Site", "internal", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "everyoneExceptExternalUserFullControl" },
			
			new String[] { "SITE_UPDATESHARE_37", "Policy Desc", "Everyone-Contribute", "Office 365 Site", "internal", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "everyoneEdit" },
			new String[] { "SITE_UPDATESHARE_38", "Policy Desc", "Everyone-Contribute", "Office 365 Site", "internal", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "everyoneRead" },
			new String[] { "SITE_UPDATESHARE_39", "Policy Desc", "Everyone-Contribute", "Office 365 Site", "internal", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "everyoneDesign" },
			new String[] { "SITE_UPDATESHARE_40", "Policy Desc", "Everyone-Contribute", "Office 365 Site", "internal", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "everyoneFullControl" },
			new String[] { "SITE_UPDATESHARE_41", "Policy Desc", "Everyone-Contribute", "Office 365 Site", "internal", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "everyoneExceptExternalUserRead" },
			new String[] { "SITE_UPDATESHARE_42", "Policy Desc", "Everyone-Contribute", "Office 365 Site", "internal", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "everyoneExceptExternalUserContribute" },
			new String[] { "SITE_UPDATESHARE_43", "Policy Desc", "Everyone-Contribute", "Office 365 Site", "internal", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "everyoneExceptExternalUserDesign" },
			new String[] { "SITE_UPDATESHARE_44", "Policy Desc", "Everyone-Contribute", "Office 365 Site", "internal", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "everyoneExceptExternalUserEdit" },
			new String[] { "SITE_UPDATESHARE_45", "Policy Desc", "Everyone-Contribute", "Office 365 Site", "internal", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "everyoneExceptExternalUserFullControl" },
			
			new String[] { "SITE_UPDATESHARE_46", "Policy Desc", "Everyone except external users-Read", "Office 365 Site", "internal", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "everyoneEdit" },
			new String[] { "SITE_UPDATESHARE_47", "Policy Desc", "Everyone except external users-Read", "Office 365 Site", "internal", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "everyoneRead" },
			new String[] { "SITE_UPDATESHARE_48", "Policy Desc", "Everyone except external users-Read", "Office 365 Site", "internal", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "everyoneDesign" },
			new String[] { "SITE_UPDATESHARE_49", "Policy Desc", "Everyone except external users-Read", "Office 365 Site", "internal", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "everyoneContribute" },
			new String[] { "SITE_UPDATESHARE_50", "Policy Desc", "Everyone except external users-Read", "Office 365 Site", "internal", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "everyoneFullControl" },
			new String[] { "SITE_UPDATESHARE_51", "Policy Desc", "Everyone except external users-Read", "Office 365 Site", "internal", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "everyoneExceptExternalUserContribute" },
			new String[] { "SITE_UPDATESHARE_52", "Policy Desc", "Everyone except external users-Read", "Office 365 Site", "internal", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "everyoneExceptExternalUserDesign" },
			new String[] { "SITE_UPDATESHARE_53", "Policy Desc", "Everyone except external users-Read", "Office 365 Site", "internal", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "everyoneExceptExternalUserEdit" },
			new String[] { "SITE_UPDATESHARE_54", "Policy Desc", "Everyone except external users-Read", "Office 365 Site", "internal", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "everyoneExceptExternalUserFullControl" },
			
			new String[] { "SITE_UPDATESHARE_55", "Policy Desc", "Everyone except external users-Edit", "Office 365 Site", "internal", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "everyoneEdit" },
			new String[] { "SITE_UPDATESHARE_56", "Policy Desc", "Everyone except external users-Edit", "Office 365 Site", "internal", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "everyoneRead" },
			new String[] { "SITE_UPDATESHARE_57", "Policy Desc", "Everyone except external users-Edit", "Office 365 Site", "internal", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "everyoneDesign" },
			new String[] { "SITE_UPDATESHARE_58", "Policy Desc", "Everyone except external users-Edit", "Office 365 Site", "internal", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "everyoneContribute" },
			new String[] { "SITE_UPDATESHARE_59", "Policy Desc", "Everyone except external users-Edit", "Office 365 Site", "internal", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "everyoneFullControl" },
			new String[] { "SITE_UPDATESHARE_60", "Policy Desc", "Everyone except external users-Edit", "Office 365 Site", "internal", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "everyoneExceptExternalUserContribute" },
			new String[] { "SITE_UPDATESHARE_61", "Policy Desc", "Everyone except external users-Edit", "Office 365 Site", "internal", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "everyoneExceptExternalUserDesign" },
			new String[] { "SITE_UPDATESHARE_62", "Policy Desc", "Everyone except external users-Edit", "Office 365 Site", "internal", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "everyoneExceptExternalUserRead" },
			new String[] { "SITE_UPDATESHARE_63", "Policy Desc", "Everyone except external users-Edit", "Office 365 Site", "internal", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "everyoneExceptExternalUserFullControl" },
			
			new String[] { "SITE_UPDATESHARE_64", "Policy Desc", "Everyone except external users-Contribute", "Office 365 Site", "internal", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "everyoneEdit" },
			new String[] { "SITE_UPDATESHARE_65", "Policy Desc", "Everyone except external users-Contribute", "Office 365 Site", "internal", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "everyoneRead" },
			new String[] { "SITE_UPDATESHARE_66", "Policy Desc", "Everyone except external users-Contribute", "Office 365 Site", "internal", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "everyoneDesign" },
			new String[] { "SITE_UPDATESHARE_67", "Policy Desc", "Everyone except external users-Contribute", "Office 365 Site", "internal", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "everyoneContribute" },
			new String[] { "SITE_UPDATESHARE_68", "Policy Desc", "Everyone except external users-Contribute", "Office 365 Site", "internal", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "everyoneFullControl" },
			new String[] { "SITE_UPDATESHARE_69", "Policy Desc", "Everyone except external users-Contribute", "Office 365 Site", "internal", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "everyoneExceptExternalUserEdit" },
			new String[] { "SITE_UPDATESHARE_70", "Policy Desc", "Everyone except external users-Contribute", "Office 365 Site", "internal", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "everyoneExceptExternalUserDesign" },
			new String[] { "SITE_UPDATESHARE_71", "Policy Desc", "Everyone except external users-Contribute", "Office 365 Site", "internal", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "everyoneExceptExternalUserRead" },
			new String[] { "SITE_UPDATESHARE_72", "Policy Desc", "Everyone except external users-Contribute", "Office 365 Site", "internal", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "everyoneExceptExternalUserFullControl" },
			
			new String[] { "SITE_UPDATESHARE_73", "Policy Desc", "Everyone except external users-Design", "Office 365 Site", "internal", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "everyoneEdit" },
			new String[] { "SITE_UPDATESHARE_74", "Policy Desc", "Everyone except external users-Design", "Office 365 Site", "internal", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "everyoneRead" },
			new String[] { "SITE_UPDATESHARE_75", "Policy Desc", "Everyone except external users-Design", "Office 365 Site", "internal", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "everyoneDesign" },
			new String[] { "SITE_UPDATESHARE_76", "Policy Desc", "Everyone except external users-Design", "Office 365 Site", "internal", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "everyoneContribute" },
			new String[] { "SITE_UPDATESHARE_77", "Policy Desc", "Everyone except external users-Design", "Office 365 Site", "internal", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "everyoneFullControl" },
			new String[] { "SITE_UPDATESHARE_78", "Policy Desc", "Everyone except external users-Design", "Office 365 Site", "internal", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "everyoneExceptExternalUserEdit" },
			new String[] { "SITE_UPDATESHARE_79", "Policy Desc", "Everyone except external users-Design", "Office 365 Site", "internal", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "everyoneExceptExternalUserContribute" },
			new String[] { "SITE_UPDATESHARE_80", "Policy Desc", "Everyone except external users-Design", "Office 365 Site", "internal", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "everyoneExceptExternalUserRead" },
			new String[] { "SITE_UPDATESHARE_81", "Policy Desc", "Everyone except external users-Design", "Office 365 Site", "internal", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "everyoneExceptExternalUserFullControl" },
			
			new String[] { "SITE_UPDATESHARE_82", "Policy Desc", "Everyone except external users-Full Control", "Office 365 Site", "internal", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "everyoneEdit" },
			new String[] { "SITE_UPDATESHARE_83", "Policy Desc", "Everyone except external users-Full Control", "Office 365 Site", "internal", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "everyoneRead" },
			new String[] { "SITE_UPDATESHARE_84", "Policy Desc", "Everyone except external users-Full Control", "Office 365 Site", "internal", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "everyoneDesign" },
			new String[] { "SITE_UPDATESHARE_85", "Policy Desc", "Everyone except external users-Full Control", "Office 365 Site", "internal", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "everyoneContribute" },
			new String[] { "SITE_UPDATESHARE_86", "Policy Desc", "Everyone except external users-Full Control", "Office 365 Site", "internal", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "everyoneFullControl" },
			new String[] { "SITE_UPDATESHARE_87", "Policy Desc", "Everyone except external users-Full Control", "Office 365 Site", "internal", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "everyoneExceptExternalUserEdit" },
			new String[] { "SITE_UPDATESHARE_88", "Policy Desc", "Everyone except external users-Full Control", "Office 365 Site", "internal", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "everyoneExceptExternalUserContribute" },
			new String[] { "SITE_UPDATESHARE_89", "Policy Desc", "Everyone except external users-Full Control", "Office 365 Site", "internal", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "everyoneExceptExternalUserRead" },
			new String[] { "SITE_UPDATESHARE_90", "Policy Desc", "Everyone except external users-Full Control", "Office 365 Site", "internal", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "everyoneExceptExternalUserDesign" },
			
			//Delete Permissions 
			new String[] { "SITE_DELETEPERM_01", "Policy Desc", "Everyone-Design",       "Office 365 Site", "external", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "deletePermissions" },
			new String[] { "SITE_DELETEPERM_02", "Policy Desc", "Everyone-Read",         "Office 365 Site", "external", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "deletePermissions" },
			new String[] { "SITE_DELETEPERM_03", "Policy Desc", "Everyone-Full Control", "Office 365 Site", "external", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "deletePermissions" },
			new String[] { "SITE_DELETEPERM_04", "Policy Desc", "Everyone-Edit",         "Office 365 Site", "external", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "deletePermissions" },
			new String[] { "SITE_DELETEPERM_05", "Policy Desc", "Everyone-Contribute",  "Office 365 Site", "external", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "deletePermissions" },
			
			new String[] { "SITE_DELETEPERM_06", "Policy Desc", "Everyone except external users-Design", "Office 365 Site", "internal", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "deletePermissions" },
			new String[] { "SITE_DELETEPERM_07", "Policy Desc", "Everyone except external users-Read", "Office 365 Site", "internal", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "deletePermissions" },
			new String[] { "SITE_DELETEPERM_08", "Policy Desc", "Everyone except external users-Full Control", "Office 365 Site", "internal", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "deletePermissions" },
			new String[] { "SITE_DELETEPERM_09", "Policy Desc", "Everyone except external users-Edit", "Office 365 Site", "internal", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "deletePermissions" },
			new String[] { "SITE_DELETEPERM_10", "Policy Desc", "Everyone except external users-Contribute", "Office 365 Site", "internal", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "deletePermissions" },
			
			//Remove Collaborator
			new String[] { "SITE_REMOVECOLLAB_01", "Policy Desc", "Mayur Belekar-Design", "Office 365 Site", "external", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "removeCollab" },
			new String[] { "SITE_REMOVECOLLAB_02", "Policy Desc", "Mayur Belekar-Read", "Office 365 Site", "external", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "removeCollab" },
			new String[] { "SITE_REMOVECOLLAB_03", "Policy Desc", "Mayur Belekar-Full Control", "Office 365 Site", "external", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "removeCollab" },
			new String[] { "SITE_REMOVECOLLAB_04", "Policy Desc", "Mayur Belekar-Edit", "Office 365 Site", "external", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "removeCollab" },
			new String[] { "SITE_REMOVECOLLAB_05", "Policy Desc", "Mayur Belekar-Contribute", "Office 365 Site", "external", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "removeCollab" },
			
			//Update Collaborator
			new String[] { "SITE_UPDATECOLLAB_01", "Policy Desc", "Mayur Belekar-Design", "Office 365 Site", "external", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "updateCollabRead" },
			new String[] { "SITE_UPDATECOLLAB_02", "Policy Desc", "Mayur Belekar-Design", "Office 365 Site", "external", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "updateCollabContribute" },
			new String[] { "SITE_UPDATECOLLAB_03", "Policy Desc", "Mayur Belekar-Design", "Office 365 Site", "external", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "updateCollabEdit" },
			new String[] { "SITE_UPDATECOLLAB_04", "Policy Desc", "Mayur Belekar-Design", "Office 365 Site", "external", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "updateCollabFullControl" },
			
			new String[] { "SITE_UPDATECOLLAB_05", "Policy Desc", "Mayur Belekar-Read", "Office 365 Site", "external", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "updateCollabDesign" },
			new String[] { "SITE_UPDATECOLLAB_06", "Policy Desc", "Mayur Belekar-Read", "Office 365 Site", "external", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "updateCollabContribute" },
			new String[] { "SITE_UPDATECOLLAB_07", "Policy Desc", "Mayur Belekar-Read", "Office 365 Site", "external", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "updateCollabEdit" },
			new String[] { "SITE_UPDATECOLLAB_08", "Policy Desc", "Mayur Belekar-Read", "Office 365 Site", "external", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "updateCollabFullControl" },
			
			new String[] { "SITE_UPDATECOLLAB_09", "Policy Desc", "Mayur Belekar-Full Control", "Office 365 Site", "external", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "updateCollabDesign" },
			new String[] { "SITE_UPDATECOLLAB_10", "Policy Desc", "Mayur Belekar-Full Control", "Office 365 Site", "external", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "updateCollabContribute" },
			new String[] { "SITE_UPDATECOLLAB_11", "Policy Desc", "Mayur Belekar-Full Control", "Office 365 Site", "external", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "updateCollabEdit" },
			new String[] { "SITE_UPDATECOLLAB_12", "Policy Desc", "Mayur Belekar-Full Control", "Office 365 Site", "external", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "updateCollabRead" },
			
			new String[] { "SITE_UPDATECOLLAB_13", "Policy Desc", "Mayur Belekar-Edit", "Office 365 Site", "external", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "updateCollabDesign" },
			new String[] { "SITE_UPDATECOLLAB_14", "Policy Desc", "Mayur Belekar-Edit", "Office 365 Site", "external", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "updateCollabContribute" },
			new String[] { "SITE_UPDATECOLLAB_15", "Policy Desc", "Mayur Belekar-Edit", "Office 365 Site", "external", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "updateCollabRead" },
			new String[] { "SITE_UPDATECOLLAB_16", "Policy Desc", "Mayur Belekar-Edit", "Office 365 Site", "external", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "updateCollabFullControl" },
			
			new String[] { "SITE_UPDATECOLLAB_17", "Policy Desc", "Mayur Belekar-Contribute", "Office 365 Site", "external", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "updateCollabDesign" },
			new String[] { "SITE_UPDATECOLLAB_18", "Policy Desc", "Mayur Belekar-Contribute", "Office 365 Site", "external", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "updateCollabEdit" },
			new String[] { "SITE_UPDATECOLLAB_19", "Policy Desc", "Mayur Belekar-Contribute", "Office 365 Site", "external", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "updateCollabRead" },
			new String[] { "SITE_UPDATECOLLAB_20", "Policy Desc", "Mayur Belekar-Contribute", "Office 365 Site", "external", "admin@protecto365autobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "Virus", "updateCollabFullControl" },
		};
	}
}