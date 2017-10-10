package com.elastica.beatle.protect.dataprovider;

import org.testng.annotations.DataProvider;

public class DropboxDataProvider {
	
	@DataProvider(name = "DeleteFileDataProvider")
	public static Object[][] getDeleteFileDataProvider() {
		return new Object[][] {
				new String[] { "DBPOLICY1", "Policy Desc", "DataExposure", "Dropbox", "unexposed", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "rar", "no", "0,0", "Virus", "no", "delete" },
				new String[] { "DBPOLICY2", "Policy Desc", "DataExposure", "Dropbox", "unexposed", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xls", "no", "xls", "0,0", "Encryption", "no", "delete" },
				new String[] { "DBPOLICY3", "Policy Desc", "DataExposure", "Dropbox", "unexposed", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xls", "xls", "no", "0,0", "Encryption", "no", "delete" },
				new String[] { "DBPOLICY4", "Policy Desc", "DataExposure", "Dropbox", "unexposed", "any", "any", "any", "no", "no", "any", "any", "no", "no", "bin", "no", "bin", "0,0", "Encryption", "no", "delete" },
				new String[] { "DBPOLICY5", "Policy Desc", "DataExposure", "Dropbox", "unexposed", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xlsm", "xlsm", "no", "0,0", "VBA Macros", "no", "delete" },
 				new String[] { "DBPOLICY6", "Policy Desc", "DataExposure", "Dropbox", "unexposed", "any", "any", "any", "no", "no", "any", "any", "no", "no", "pdf", "pdf", "no", "0,0", "HIPAA", "no", "delete" },
 
				new String[] { "DBPOLICY7", "Policy Desc", "DataExposure", "Dropbox", "unexposed", "any", "any", "any", "no", "no", "any", "any", "no", "no", "pdf", "pdf", "no", "0,0", "PII", "no", "delete" },
				new String[] { "DBPOLICY8", "Policy Desc", "DataExposure", "Dropbox", "unexposed", "any", "any", "any", "no", "no", "any", "any", "no", "no", "cs", "cs", "no", "0,0", "Source Code", "no", "delete" },
				new String[] { "DBPOLICY9", "Policy Desc", "DataExposure", "Dropbox", "unexposed", "any", "any", "any", "no", "no", "any", "any", "no", "no", "java", "java", "no", "0,0", "Source Code", "no", "delete" },
				new String[] { "DBPOLICY10", "Policy Desc", "DataExposure", "Dropbox", "unexposed", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "txt", "txt", "0,0", "GLBA", "no", "delete" },
				new String[] { "DBPOLICY11", "Policy Desc", "DataExposure", "Dropbox", "unexposed", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "txt", "txt", "0,0", "PII", "no", "delete" },
				new String[] { "DBPOLICY12", "Policy Desc", "DataExposure", "Dropbox", "unexposed", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "txt", "txt", "0,0", "PCI", "no", "delete" },
				
				new String[] { "DBPOLICY13", "Policy Desc", "DataExposure", "Dropbox", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "delete" },
				new String[] { "DBPOLICY14", "Policy Desc", "DataExposure", "Dropbox", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "txt", "no", "0,0", "no", "no", "delete" },
				new String[] { "DBPOLICY15", "Policy Desc", "DataExposure", "Dropbox", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "delete" },
				new String[] { "DBPOLICY16", "Policy Desc", "DataExposure", "Dropbox", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PII", "no", "delete" },
				new String[] { "DBPOLICY17", "Policy Desc", "DataExposure", "Dropbox", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "no", "0,0", "PCI", "no", "delete" },
				new String[] { "DBPOLICY18", "Policy Desc", "DataExposure", "Dropbox", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "GLBA", "no", "delete" },
				
				new String[] { "DBPOLICY19", "Policy Desc", "DataExposure", "Dropbox", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "no", "no", "0,0", "PCI", "no", "delete" },
				new String[] { "DBPOLICY20", "Policy Desc", "DataExposure", "Dropbox", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "no", "no", "0,0", "PII", "no", "delete" },
				new String[] { "DBPOLICY21", "Policy Desc", "DataExposure", "Dropbox", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "rtf", "no", "0,0", "no", "no", "delete" },
				new String[] { "DBPOLICY22", "Policy Desc", "DataExposure", "Dropbox", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "no", "rtf", "0,0", "no", "no", "delete" },
				new String[] { "DBPOLICY23", "Policy Desc", "DataExposure", "Dropbox", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "docx", "no", "docx", "0,0", "no", "no", "delete" },
				new String[] { "DBPOLICY24", "Policy Desc", "DataExposure", "Dropbox", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "docx", "docx", "no", "0,0", "no", "no", "delete" },
				
				new String[] { "DBPOLICY25", "Policy Desc", "DataExposure", "Dropbox", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "doc", "doc", "no", "0,0", "no", "no", "delete" },
				new String[] { "DBPOLICY26", "Policy Desc", "DataExposure", "Dropbox", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "docx", "no", "doc", "0,0", "no", "no", "delete" },
				new String[] { "DBPOLICY27", "Policy Desc", "DataExposure", "Dropbox", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "java", "java", "no", "0,0", "no", "no", "delete" },
				new String[] { "DBPOLICY28", "Policy Desc", "DataExposure", "Dropbox", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "java", "no", "no", "0,0", "Source Code", "no", "delete" },
				new String[] { "DBPOLICY29", "Policy Desc", "DataExposure", "Dropbox", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "html", "html", "no", "0,0", "no", "no", "delete" },
				new String[] { "DBPOLICY30", "Policy Desc", "DataExposure", "Dropbox", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "html", "no", "html", "0,0", "no", "no", "delete" },
				
				new String[] { "DBPOLICY31", "Policy Desc", "DataExposure", "Dropbox", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "exe", "exe", "no", "0,0", "no", "no", "delete" },
				new String[] { "DBPOLICY32", "Policy Desc", "DataExposure", "Dropbox", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "exe", "no", "no", "2097152,0", "no", "no", "delete" },
				new String[] { "DBPOLICY33", "Policy Desc", "DataExposure", "Dropbox", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "jpg", "jpg", "no", "0,0", "no", "no", "delete" },
				new String[] { "DBPOLICY34", "Policy Desc", "DataExposure", "Dropbox", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "pdf", "pdf", "no", "0,0", "no", "no", "delete" },
				new String[] { "DBPOLICY35", "Policy Desc", "DataExposure", "Dropbox", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "pdf", "no", "pdf", "0,0", "no", "no", "delete" },
				new String[] { "DBPOLICY36", "Policy Desc", "DataExposure", "Dropbox", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "pdf", "no", "no", "0,0", "HIPAA", "no", "delete" },
				
				new String[] { "DBPOLICY37", "Policy Desc", "DataExposure", "Dropbox", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "cs", "no", "no", "0,0", "Source Code", "no", "delete" },
				new String[] { "DBPOLICY38", "Policy Desc", "DataExposure", "Dropbox", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "bin", "no", "no", "0,0", "Encryption", "no", "delete" },
				new String[] { "DBPOLICY39", "Policy Desc", "DataExposure", "Dropbox", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xlsm", "no", "no", "0,0", "VBA Macros", "no", "delete" },
				new String[] { "DBPOLICY40", "Policy Desc", "DataExposure", "Dropbox", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xlsm", "xlsm", "no", "0,0", "no", "no", "delete" },
				new String[] { "DBPOLICY41", "Policy Desc", "DataExposure", "Dropbox", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xls", "no", "no", "0,0", "Encryption", "no", "delete" },
				
				new String[] { "DBPOLICY42", "Policy Desc", "DataExposure", "Dropbox", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xls", "xls", "no", "0,0", "no", "no", "delete" },
				new String[] { "DBPOLICY43", "Policy Desc", "DataExposure", "Dropbox", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xls", "no", "xls", "0,0", "no", "no", "delete" },
				new String[] { "DBPOLICY44", "Policy Desc", "DataExposure", "Dropbox", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "rar", "no", "0,0", "no", "no", "delete" },
				new String[] { "DBPOLICY45", "Policy Desc", "DataExposure", "Dropbox", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "no", "0,0", "Virus", "no", "delete" },
				new String[] { "DBPOLICY46", "Policy Desc", "DataExposure", "Dropbox", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "pdf", "no", "pdf", "0,0", "PII", "no", "delete" },
								
				new String[] { "DBPOLICY47", "Policy Desc", "DataExposure", "Dropbox", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "zip", "zip", "zip", "0,0", "no", "no", "delete" },
				new String[] { "DBPOLICY48", "Policy Desc", "DataExposure", "Dropbox", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "zip", "zip", "no", "0,0", "Virus", "no", "delete" },
				new String[] { "DBPOLICY49", "Policy Desc", "DataExposure", "Dropbox", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "zip", "zip", "zip", "0,0", "Virus", "no", "delete" },
				new String[] { "DBPOLICY50", "Policy Desc", "DataExposure", "Dropbox", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "js", "no", "js", "0,0", "Source Code", "no", "delete" },
				new String[] { "DBPOLICY51", "Policy Desc", "DataExposure", "Dropbox", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "js", "js", "js", "0,0", "no", "no", "delete" },
				new String[] { "DBPOLICY52", "Policy Desc", "DataExposure", "Dropbox", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "js", "js", "no", "0,0", "Source Code", "no", "delete" },
				
				new String[] { "DBPOLICY53", "Policy Desc", "DataExposure", "Dropbox", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "zip", "zip", "no", "1048576,2097152", "Virus", "no", "delete" },
				new String[] { "DBPOLICY54", "Policy Desc", "DataExposure", "Dropbox", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "zip", "zip", "no", "0,2097152", "Virus", "no", "delete" },
				new String[] { "DBPOLICY55", "Policy Desc", "DataExposure", "Dropbox", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "zip", "zip", "no", "1048576,0", "Virus", "no", "delete" },

		};
	}
	
	/**
	 * 
	 * @return
	 */
	@DataProvider(name = "RevokeSharedLink")
	public static Object[][] getRevokeSharedLinkDataProvider() {
		return new Object[][] {
				
				new String[] { "DBPOLICY101", "Policy Desc", "DataExposure", "Dropbox", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "sharedLinkRevoke" },
				new String[] { "DBPOLICY102", "Policy Desc", "DataExposure", "Dropbox", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "txt", "no", "0,0", "no", "no", "sharedLinkRevoke" },
				new String[] { "DBPOLICY103", "Policy Desc", "DataExposure", "Dropbox", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "sharedLinkRevoke" },
				new String[] { "DBPOLICY104", "Policy Desc", "DataExposure", "Dropbox", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PII", "no", "sharedLinkRevoke" },
				new String[] { "DBPOLICY105", "Policy Desc", "DataExposure", "Dropbox", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "no", "0,0", "PCI", "no", "sharedLinkRevoke" },
				new String[] { "DBPOLICY106", "Policy Desc", "DataExposure", "Dropbox", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "GLBA", "no", "sharedLinkRevoke" },
				
				new String[] { "DBPOLICY107", "Policy Desc", "DataExposure", "Dropbox", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "no", "no", "0,0", "PCI", "no", "sharedLinkRevoke" },
				new String[] { "DBPOLICY108", "Policy Desc", "DataExposure", "Dropbox", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "no", "no", "0,0", "PII", "no", "sharedLinkRevoke" },
				new String[] { "DBPOLICY109", "Policy Desc", "DataExposure", "Dropbox", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "rtf", "no", "0,0", "no", "no", "sharedLinkRevoke" },
				new String[] { "DBPOLICY110", "Policy Desc", "DataExposure", "Dropbox", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "no", "rtf", "0,0", "no", "no", "sharedLinkRevoke" },
				new String[] { "DBPOLICY111", "Policy Desc", "DataExposure", "Dropbox", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "docx", "no", "docx", "0,0", "no", "no", "sharedLinkRevoke" },
				new String[] { "DBPOLICY112", "Policy Desc", "DataExposure", "Dropbox", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "docx", "docx", "no", "0,0", "no", "no", "sharedLinkRevoke" },
				
				new String[] { "DBPOLICY113", "Policy Desc", "DataExposure", "Dropbox", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "doc", "doc", "no", "0,0", "no", "no", "sharedLinkRevoke" },
				new String[] { "DBPOLICY114", "Policy Desc", "DataExposure", "Dropbox", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "docx", "no", "doc", "0,0", "no", "no", "sharedLinkRevoke" },
				new String[] { "DBPOLICY115", "Policy Desc", "DataExposure", "Dropbox", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "java", "java", "no", "0,0", "no", "no", "sharedLinkRevoke" },
				new String[] { "DBPOLICY116", "Policy Desc", "DataExposure", "Dropbox", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "java", "no", "no", "0,0", "Source Code", "no", "sharedLinkRevoke" },
				new String[] { "DBPOLICY117", "Policy Desc", "DataExposure", "Dropbox", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "html", "html", "no", "0,0", "no", "no", "sharedLinkRevoke" },
				new String[] { "DBPOLICY118", "Policy Desc", "DataExposure", "Dropbox", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "html", "no", "html", "0,0", "no", "no", "sharedLinkRevoke" },
				
				new String[] { "DBPOLICY119", "Policy Desc", "DataExposure", "Dropbox", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "exe", "exe", "no", "0,0", "no", "no", "sharedLinkRevoke" },
				new String[] { "DBPOLICY120", "Policy Desc", "DataExposure", "Dropbox", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "exe", "no", "no", "2097152,0", "no", "no", "sharedLinkRevoke" },
				new String[] { "DBPOLICY121", "Policy Desc", "DataExposure", "Dropbox", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "jpg", "jpg", "no", "0,0", "no", "no", "sharedLinkRevoke" },
				new String[] { "DBPOLICY122", "Policy Desc", "DataExposure", "Dropbox", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "pdf", "pdf", "no", "0,0", "no", "no", "sharedLinkRevoke" },
				new String[] { "DBPOLICY123", "Policy Desc", "DataExposure", "Dropbox", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "pdf", "no", "pdf", "0,0", "no", "no", "sharedLinkRevoke" },
				new String[] { "DBPOLICY124", "Policy Desc", "DataExposure", "Dropbox", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "pdf", "no", "no", "0,0", "HIPAA", "no", "sharedLinkRevoke" },
				
				new String[] { "DBPOLICY125", "Policy Desc", "DataExposure", "Dropbox", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "cs", "no", "no", "0,0", "Source Code", "no", "sharedLinkRevoke" },
				new String[] { "DBPOLICY126", "Policy Desc", "DataExposure", "Dropbox", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "bin", "no", "no", "0,0", "Encryption", "no", "sharedLinkRevoke" },
				new String[] { "DBPOLICY127", "Policy Desc", "DataExposure", "Dropbox", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xlsm", "no", "no", "0,0", "VBA Macros", "no", "sharedLinkRevoke" },
				new String[] { "DBPOLICY128", "Policy Desc", "DataExposure", "Dropbox", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xlsm", "xlsm", "no", "0,0", "no", "no", "sharedLinkRevoke" },
				new String[] { "DBPOLICY129", "Policy Desc", "DataExposure", "Dropbox", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xls", "no", "no", "0,0", "Encryption", "no", "sharedLinkRevoke" },
				
				new String[] { "DBPOLICY130", "Policy Desc", "DataExposure", "Dropbox", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xls", "xls", "no", "0,0", "no", "no", "sharedLinkRevoke" },
				new String[] { "DBPOLICY131", "Policy Desc", "DataExposure", "Dropbox", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xls", "no", "xls", "0,0", "no", "no", "sharedLinkRevoke" },
				new String[] { "DBPOLICY132", "Policy Desc", "DataExposure", "Dropbox", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "rar", "no", "0,0", "no", "no", "sharedLinkRevoke" },
				new String[] { "DBPOLICY133", "Policy Desc", "DataExposure", "Dropbox", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "no", "0,0", "Virus", "no", "sharedLinkRevoke" },
				new String[] { "DBPOLICY134", "Policy Desc", "DataExposure", "Dropbox", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "pdf", "no", "pdf", "0,0", "PII", "no", "sharedLinkRevoke" },
								
				new String[] { "DBPOLICY135", "Policy Desc", "DataExposure", "Dropbox", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "zip", "zip", "zip", "0,0", "no", "no", "sharedLinkRevoke" },
				new String[] { "DBPOLICY136", "Policy Desc", "DataExposure", "Dropbox", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "zip", "zip", "no", "0,0", "Virus", "no", "sharedLinkRevoke" },
				new String[] { "DBPOLICY137", "Policy Desc", "DataExposure", "Dropbox", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "zip", "zip", "zip", "0,0", "Virus", "no", "sharedLinkRevoke" },
				new String[] { "DBPOLICY138", "Policy Desc", "DataExposure", "Dropbox", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "js", "no", "js", "0,0", "Source Code", "no", "sharedLinkRevoke" },
				new String[] { "DBPOLICY139", "Policy Desc", "DataExposure", "Dropbox", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "js", "js", "js", "0,0", "no", "no", "sharedLinkRevoke" },
				new String[] { "DBPOLICY140", "Policy Desc", "DataExposure", "Dropbox", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "js", "js", "no", "0,0", "Source Code", "no", "sharedLinkRevoke" },
				
				new String[] { "DBPOLICY141", "Policy Desc", "DataExposure", "Dropbox", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "zip", "zip", "no", "1048576,2097152", "Virus", "no", "sharedLinkRevoke" },
				new String[] { "DBPOLICY142", "Policy Desc", "DataExposure", "Dropbox", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "zip", "zip", "no", "0,2097152", "Virus", "no", "sharedLinkRevoke" },
				new String[] { "DBPOLICY143", "Policy Desc", "DataExposure", "Dropbox", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "zip", "zip", "no", "1048576,0", "Virus", "no", "sharedLinkRevoke" },

		};
	}
	
	@DataProvider(name = "DropboxExposureDataProvider")
	public static Object[][] getDropboxExposureCombinationTests() {
		return new Object[][] {
			new String[] { "DPMULTIEXPOSURE1",  "Policy Desc", "DataExposure", "Dropbox", "external",                     "any", "any", "any", "no", "no", "admin@dciautobeatle.com", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "no", "external", "yes" },
			new String[] { "DPMULTIEXPOSURE2",  "Policy Desc", "DataExposure", "Dropbox", "external",                     "any", "any", "any", "no", "no", "any", "any", "admin@dciautobeatle.com", "no", "txt", "no", "txt", "0,0", "no", "no", "no", "external", "no" },
			new String[] { "DPMULTIEXPOSURE3",  "Policy Desc", "DataExposure", "Dropbox", "public,external,all",          "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "no", "external", "no" },
			new String[] { "DPMULTIEXPOSURE4",  "Policy Desc", "DataExposure", "Dropbox", "public,internal,all",          "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "no", "public", "no" },
			new String[] { "DPMULTIEXPOSURE5",  "Policy Desc", "DataExposure", "Dropbox", "internal,external,all",        "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "no", "internal", "no" },
			new String[] { "DPMULTIEXPOSURE6",  "Policy Desc", "DataExposure", "Dropbox", "internal,public,all",          "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "no", "internal", "no" },
			new String[] { "DPMULTIEXPOSURE7",  "Policy Desc", "DataExposure", "Dropbox", "public,external,all",          "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "no", "public,external", "yes" },
			new String[] { "DPMULTIEXPOSURE8",  "Policy Desc", "DataExposure", "Dropbox", "external,internal,public,all", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "no", "public", "no" },
			new String[] { "DPMULTIEXPOSURE9",  "Policy Desc", "DataExposure", "Dropbox", "external,internal,public,all", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "no", "external,internal,public", "yes" },
			new String[] { "DPMULTIEXPOSURE10", "Policy Desc", "DataExposure", "Dropbox", "public,external,any",          "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "no", "external", "yes" },
			new String[] { "DPMULTIEXPOSURE11", "Policy Desc", "DataExposure", "Dropbox", "public,external,any",          "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "no", "public", "yes" },
			new String[] { "DPMULTIEXPOSURE12", "Policy Desc", "DataExposure", "Dropbox", "public,external,any",          "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "no", "internal", "No" },
			new String[] { "DPMULTIEXPOSURE13", "Policy Desc", "DataExposure", "Dropbox", "internal,external,any",        "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "no", "external", "yes" },
			new String[] { "DPMULTIEXPOSURE14", "Policy Desc", "DataExposure", "Dropbox", "internal,external,any",        "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "no", "internal", "yes" },
			new String[] { "DPMULTIEXPOSURE15", "Policy Desc", "DataExposure", "Dropbox", "internal,external,any",        "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "no", "public", "no" },
			new String[] { "DPMULTIEXPOSURE16", "Policy Desc", "DataExposure", "Dropbox", "public,internal,any",          "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "no", "public", "yes" },
			new String[] { "DPMULTIEXPOSURE17", "Policy Desc", "DataExposure", "Dropbox", "public,internal,any",          "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "no", "internal", "yes" },
			new String[] { "DPMULTIEXPOSURE18", "Policy Desc", "DataExposure", "Dropbox", "public,internal,any",          "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "no", "external", "no" },
			new String[] { "DPMULTIEXPOSURE19", "Policy Desc", "DataExposure", "Dropbox", "public,internal,external,any", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "no", "external", "yes" },
			new String[] { "DPMULTIEXPOSURE20", "Policy Desc", "DataExposure", "Dropbox", "public,internal,external,any", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "no", "internal", "yes" },
			new String[] { "DPMULTIEXPOSURE21", "Policy Desc", "DataExposure", "Dropbox", "public,internal,external,any", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "no", "public", "yes" },
			new String[] { "DPMULTIEXPOSURE22", "Policy Desc", "DataExposure", "Dropbox", "public,internal,external,any", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "no", "no", "no" },
			new String[] { "DPMULTIEXPOSURE23", "Policy Desc", "DataExposure", "Dropbox", "public,internal,external,any", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "no", "public,internal,external", "yes" },
		};
	}

	@DataProvider(name = "DropboxRemediationsDataProvider")
	public static Object[][] getDropboxRemediationsTests() {
		return new Object[][] {
			new String[] { "DBREMEDIATION1", "Policy Desc", "DataExposure", "Dropbox", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "delete" },
			new String[] { "DBREMEDIATION2", "Policy Desc", "DataExposure", "Dropbox", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "sharedLinkRevoke" },
			new String[] { "DBREMEDIATION3", "Policy Desc", "DataExposure", "Dropbox", "unexposed", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "txt", "no", "0,0", "FERPA", "no", "delete" },			
			//new String[] { "DBREMEDIATION3", "Policy Desc", "DataExposure", "Dropbox", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "rar", "no", "0,0", "Virus", "no", "sharedLinkRevoke" },
			//new String[] { "DBREMEDIATION4", "Policy Desc", "DataExposure", "Dropbox", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "sharedLinkRevoke" },
			//new String[] { "DBREMEDIATION5", "Policy Desc", "DataExposure", "Dropbox", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "delete" },
			//new String[] { "DBREMEDIATION6", "Policy Desc", "DataExposure", "Dropbox", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "delete" },			
		};
	}
	
	@DataProvider(name = "DropboxSeverityDataProvider")
	public static Object[][] getDropboxSeverityTests() {
		return new Object[][] {
			new String[] { "SeverityInfo", "Policy Desc", "DataExposure", "Dropbox", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "no", "information"},
			new String[] { "SeverityLow", "Policy Desc", "DataExposure", "Dropbox", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "no", "low"},
			new String[] { "SeverityMedium", "Policy Desc", "DataExposure", "Dropbox", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "no", "medium"},
			new String[] { "SeverityHigh", "Policy Desc", "DataExposure", "Dropbox", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "no", "high"},
			new String[] { "SeverityCritical", "Policy Desc", "DataExposure", "Dropbox", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "no", "critical"},
			
		};
	}
	
	@DataProvider(name = "DropboxPolicyViolation")
	public static Object[][] getPolicyViolationsInputData() {
		return new Object[][] {
				
				new String[] { "DBPVPOL01", "Policy Desc", "DataExposure", "Dropbox", "unexposed", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "rar", "no", "0,0", "Virus", "no", "no" },
				new String[] { "DBPVPOL02", "Policy Desc", "DataExposure", "Dropbox", "unexposed", "any", "any", "any", "no", "no", "any", "any", "no", "no", "html", "html", "no", "0,0", "no", "no", "no" },
				new String[] { "DBPVPOL03", "Policy Desc", "DataExposure", "Dropbox", "unexposed", "any", "any", "any", "no", "no", "any", "any", "no", "no", "exe", "exe", "no", "2097152,6291456", "no", "no", "no" },
				new String[] { "DBPVPOL04", "Policy Desc", "DataExposure", "Dropbox", "unexposed", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "txt", "no", "0,0", "PII", "no", "no" },
				new String[] { "DBPVPOL05", "Policy Desc", "DataExposure", "Dropbox", "unexposed", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "txt", "no", "0,0", "PCI", "no", "no" },
				new String[] { "DBPVPOL06", "Policy Desc", "DataExposure", "Dropbox", "unexposed", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "txt", "no", "0,0", "GLBA", "no", "no" },
				new String[] { "DBPVPOL07", "Policy Desc", "DataExposure", "Dropbox", "unexposed", "any", "any", "any", "no", "no", "any", "any", "no", "no", "zip", "zip", "no", "0,0", "Virus", "no", "no" },
				new String[] { "DBPVPOL08", "Policy Desc", "DataExposure", "Dropbox", "unexposed", "any", "any", "any", "no", "no", "any", "any", "no", "no", "bin", "bin", "no", "0,0", "Encryption", "no", "no" },
				new String[] { "DBPVPOL09", "Policy Desc", "DataExposure", "Dropbox", "unexposed", "any", "any", "any", "no", "no", "any", "any", "no", "no", "avi", "avi", "no", "0,0", "no", "no", "no" },
				new String[] { "DBPVPOL10", "Policy Desc", "DataExposure", "Dropbox", "unexposed", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "rar", "no", "0,0", "Virus", "no", "no" },
				new String[] { "DBPVPOL11", "Policy Desc", "DataExposure", "Dropbox", "unexposed", "any", "any", "any", "no", "no", "any", "any", "no", "no", "docx", "docx", "no", "0,0", "Source Code", "no", "no" },
				new String[] { "DBPVPOL12", "Policy Desc", "DataExposure", "Dropbox", "unexposed", "any", "any", "any", "no", "no", "any", "any", "no", "no", "cs", "cs", "no", "0,0", "Source Code", "no", "no" },
				new String[] { "DBPVPOL13", "Policy Desc", "DataExposure", "Dropbox", "unexposed", "any", "any", "any", "no", "no", "any", "any", "no", "no", "java", "java", "no", "0,0", "Source Code", "no", "no" },
				new String[] { "DBPVPOL14", "Policy Desc", "DataExposure", "Dropbox", "unexposed", "any", "any", "any", "no", "no", "any", "any", "no", "no", "cs", "cs", "no", "0,0", "HIPAA", "no", "no" },
				new String[] { "DBPVPOL15", "Policy Desc", "DataExposure", "Dropbox", "unexposed", "any", "any", "any", "no", "no", "any", "any", "no", "no", "jpg", "jpg", "no", "0,0", "no", "no", "no" },
				new String[] { "DBPVPOL16", "Policy Desc", "DataExposure", "Dropbox", "unexposed", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xls", "xls", "no", "0,0", "Encryption", "no", "no" },
				new String[] { "DBPVPOL17", "Policy Desc", "DataExposure", "Dropbox", "unexposed", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xlsm", "xlsm", "no", "0,0", "VBA Macros", "no", "no" },
				new String[] { "DBPVPOL18", "Policy Desc", "DataExposure", "Dropbox", "unexposed", "any", "any", "any", "no", "no", "any", "any", "no", "no", "js", "js", "no", "0,0", "Source Code", "no", "no" },				
				new String[] { "DBPVPOL19", "Policy Desc", "DataExposure", "Dropbox", "unexposed", "any", "any", "any", "no", "no", "any", "any", "no", "no", "mov", "mov", "no", "0,0", "no", "no", "no" },
				new String[] { "DBPVPOL20", "Policy Desc", "DataExposure", "Dropbox", "unexposed", "any", "any", "any", "no", "no", "any", "any", "no", "no", "mp4", "mp4", "no", "0,0", "no", "no", "no" },
				new String[] { "DBPVPOL21", "Policy Desc", "DataExposure", "Dropbox", "unexposed", "any", "any", "any", "no", "no", "any", "any", "no", "no", "mxf", "mxf", "no", "0,0", "no", "no", "no" },								
				new String[] { "DBPVPOL22", "Policy Desc", "DataExposure", "Dropbox", "unexposed", "any", "any", "any", "no", "no", "any", "any", "no", "no", "webm", "webm", "no", "0,0", "no", "no", "no" },				
				new String[] { "DBPVPOL23", "Policy Desc", "DataExposure", "Dropbox", "unexposed", "any", "any", "any", "no", "no", "any", "any", "no", "no", "pdf", "pdf", "no", "0,0", "PII", "no", "no" },
				new String[] { "DBPVPOL24", "Policy Desc", "DataExposure", "Dropbox", "unexposed", "any", "any", "any", "no", "no", "any", "any", "no", "no", "pdf", "pdf", "no", "0,0", "PCI", "no", "no" },
				new String[] { "DBPVPOL25", "Policy Desc", "DataExposure", "Dropbox", "unexposed", "any", "any", "any", "no", "no", "any", "any", "no", "no", "pdf", "pdf", "no", "0,0", "HIPAA", "no", "no" },				
				new String[] { "DBPVPOL26", "Policy Desc", "DataExposure", "Dropbox", "unexposed", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "rtf", "no", "0,0", "PII", "no", "no" },
				new String[] { "DBPVPOL27", "Policy Desc", "DataExposure", "Dropbox", "unexposed", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "rtf", "no", "0,0", "PCI", "no", "no" },
				new String[] { "DBPVPOL28", "Policy Desc", "DataExposure", "Dropbox", "unexposed", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "rtf", "no", "0,0", "GLBA", "no", "no" },
				new String[] { "DBPVPOL29", "Policy Desc", "DataExposure", "Dropbox", "unexposed", "any", "any", "any", "no", "no", "any", "any", "no", "no", "doc", "doc", "no", "0,0", "PII", "no", "no" },
				new String[] { "DBPVPOL30", "Policy Desc", "DataExposure", "Dropbox", "unexposed", "any", "any", "any", "no", "no", "any", "any", "no", "no", "doc", "doc", "no", "0,0", "PCI", "no", "no" },
				new String[] { "DBPVPOL31", "Policy Desc", "DataExposure", "Dropbox", "unexposed", "any", "any", "any", "no", "no", "any", "any", "no", "no", "doc", "doc", "no", "0,0", "GLBA", "no", "no" },
				new String[] { "DBPVPOL32", "Policy Desc", "DataExposure", "Dropbox", "unexposed", "any", "any", "any", "no", "no", "any", "any", "no", "no", "doc", "doc", "no", "0,0", "GLBA", "no", "no" },	
				new String[] { "DBPVPOL33", "Policy Desc", "DataExposure", "Dropbox", "public,external,all",          "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "no", "external"},
				new String[] { "DBPVPOL34", "Policy Desc", "DataExposure", "Dropbox", "public,external,any",        "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "no", "public"},
				new String[] { "DBPVPOL35", "Policy Desc", "DataExposure", "Dropbox", "public,internal,any",          "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "no", "public"},
				new String[] { "DBPVPOL36", "Policy Desc", "DataExposure", "Dropbox", "public,internal,external,any", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "no", "public"},
				new String[] { "DBPVPOL37", "Policy Desc", "DataExposure", "Dropbox", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "txt", "no", "0,0", "PCI", "no", "no" },
				new String[] { "DBPVPOL38", "Policy Desc", "DataExposure", "Dropbox", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "avi", "no", "no", "0,0", "no", "no", "no" },
				new String[] { "DBPVPOL39", "Policy Desc", "DataExposure", "Dropbox", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "mxf", "no", "no", "0,0", "no", "no", "no" },
				new String[] { "DBPVPOL40", "Policy Desc", "DataExposure", "Dropbox", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "mp4", "no", "no", "0,0", "no", "no", "no" },
				new String[] { "DBPVPOL41", "Policy Desc", "DataExposure", "Dropbox", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "mov", "no", "no", "0,0", "no", "no", "no" },
				new String[] { "DBPVPOL42", "Policy Desc", "DataExposure", "Dropbox", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "webm", "no", "no", "0,0", "no", "no", "no" },
				new String[] { "DBPVPOL43", "Policy Desc", "DataExposure", "Dropbox", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "rar", "no", "0,0", "Virus", "no", "no" },				
				new String[] { "DELPOLICY", "Policy Desc", "DataExposure", "Dropbox", "unexposed", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "no" },
				new String[] { "DISABLEPOLICY", "Policy Desc", "DataExposure", "Dropbox", "unexposed", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "no" },
				new String[] { "DBPVPOL44", "Policy Desc", "DataExposure", "Dropbox", "unexposed", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "txt", "no", "0,0", "FERPA", "no", "no" },
				new String[] { "CIEXCEPPOL", "Policy Desc", "DataExposure", "Dropbox", "unexposed", "any", "any", "any", "no", "no", "any", "any", "no", "no", "no", "no", "no", "0,0", "no", "no", "no", "", "WordProcessor", null, "WordProcessor" },
				new String[] { "DBPVPOL45", "Policy Desc", "DataExposure", "Dropbox", "unexposed", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "txt", "no", "0,0", "PII,PCI", "no", "no" },
				new String[] { "DBPVPOL_Grp_Internal_Share", "Policy Desc", "DataExposure", "Dropbox", "internal",          "any", "ProtectDBAutoGrp", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "no"},
				//new String[] { "DBPVPOL_Grp_External_Share", "Policy Desc", "DataExposure", "Dropbox", "external",          "any", "ProtectAutoGrp", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "no"},				
		};				
	}
	
	@DataProvider(name = "PolicyI18NData")
	public static Object[][] getI18NInputData() {
		return new Object[][] {
				new String[] { "I18NPOLNAME_CN", "Policy Desc", "DataExposure", "Dropbox", "unexposed", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "delete" },
				new String[] { "I18NFILENAME_CN", "Policy Desc", "DataExposure", "Dropbox", "unexposed", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "delete" },								
				new String[] { "I18NPOLDESC_CN", "Policy Desc", "DataExposure", "Dropbox", "unexposed", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "delete" },												
				new String[] { "I18NPOLNAME_ES", "Policy Desc", "DataExposure", "Dropbox", "unexposed", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "delete" },
				new String[] { "I18NFILENAME_ES", "Policy Desc", "DataExposure", "Dropbox", "unexposed", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "delete" },								
				new String[] { "I18NPOLDESC_ES", "Policy Desc", "DataExposure", "Dropbox", "unexposed", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "delete" },				
		};				
	}
	
	@DataProvider(name = "PolicyImpactData")
	public static Object[][] getPolicyImpactData() {
		return new Object[][] {

				new String[] { "PIPOLICY1", "Policy Desc", "DataExposure", "Dropbox", "unexposed", "any", "any","any", "no", "no", "any", "any", "no", "no", "rar", "rar", "no", "0,0", "Virus", "no", "" },
				new String[] { "PIPOLICY2", "Policy Desc", "DataExposure", "Dropbox", "unexposed", "any", "any","any", "no", "no", "any", "any", "no", "no", "xlsm", "xlsm", "no", "0,0", "VBA Macros", "no", "" },
				new String[] { "PIPOLICY3", "Policy Desc", "DataExposure", "Dropbox", "unexposed", "any", "any","any", "no", "no", "any", "any", "no", "no", "pdf", "pdf", "no", "0,0", "HIPAA", "no", "" },
				new String[] { "PIPOLICY4", "Policy Desc", "DataExposure", "Dropbox", "unexposed", "any", "any","any", "no", "no", "any", "any", "no", "no", "pdf", "pdf", "no", "0,0", "PII", "no", "" },
				new String[] { "PIPOLICY5", "Policy Desc", "DataExposure", "Dropbox", "unexposed", "any", "any","any", "no", "no", "any", "any", "no", "no", "cs", "cs", "no", "0,0", "Source Code", "no", "" },
				new String[] { "PIPOLICY6", "Policy Desc", "DataExposure", "Dropbox", "unexposed", "any", "any","any", "no", "no", "any", "any", "no", "no", "java", "java", "no", "0,0", "Source Code", "no", "" },
				new String[] { "PIPOLICY7", "Policy Desc", "DataExposure", "Dropbox", "unexposed", "any", "any","any", "no", "no", "any", "any", "no", "no", "txt", "txt", "txt", "0,0", "GLBA", "no", "" },
				new String[] { "PIPOLICY8", "Policy Desc", "DataExposure", "Dropbox", "unexposed", "any", "any","any", "no", "no", "any", "any", "no", "no", "txt", "txt", "txt", "0,0", "PII", "no", "" },
				new String[] { "PIPOLICY9", "Policy Desc", "DataExposure", "Dropbox", "unexposed", "any", "any","any", "no", "no", "any", "any", "no", "no", "txt", "txt", "txt", "0,0", "PCI", "no", "" },
				new String[] { "PIPOLICY10", "Policy Desc", "DataExposure", "Box", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "txt", "no", "0,0", "no", "no", "extcollab" },
				new String[] { "PIPOLICY11", "Policy Desc", "DataExposure", "Box", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "extcollab" },
				new String[] { "PIPOLICY12", "Policy Desc", "DataExposure", "Box", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PII", "no", "extcollab" },
				new String[] { "PIPOLICY13", "Policy Desc", "DataExposure", "Box", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PCI", "no", "extcollab" },
				new String[] { "PIPOLICY14", "Policy Desc", "DataExposure", "Box", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "GLBA", "no", "extcollab" },
				new String[] { "PIPOLICY15", "Policy Desc", "DataExposure", "Box", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "no", "rtf", "0,0", "PCI", "no", "extcollab" },
				new String[] { "PIPOLICY16", "Policy Desc", "DataExposure", "Box", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "no", "no", "0,0", "PII", "no", "extcollab" },
				new String[] { "PIPOLICY17", "Policy Desc", "DataExposure", "Box", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "rtf", "no", "0,0", "no", "no", "extcollab" },
				new String[] { "PIPOLICY18", "Policy Desc", "DataExposure", "Box", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "no", "rtf", "0,0", "no", "no", "extcollab" },
				new String[] { "PIPOLICY19", "Policy Desc", "DataExposure", "Box", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "docx", "no", "docx", "0,0", "no", "no", "extcollab" },
				new String[] { "PIPOLICY20", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PII", "no", "extcollab" },
				new String[] { "PIPOLICY21", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PCI", "no", "extcollab" },
				new String[] { "PIPOLICY22", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "GLBA", "no", "extcollab" },
				new String[] { "PIPOLICY23", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "rtf", "no", "0,0", "PCI", "no", "extcollab" },
				new String[] { "PIPOLICY24", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "no", "rtf", "0,0", "PII", "no", "extcollab" },
				new String[] { "PIPOLICY25", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "no", "rtf", "0,0", "PCI", "no", "extcollab" },
				new String[] { "PIPOLICY26", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "no", "rtf", "0,0", "no", "no", "extcollab" },
				new String[] { "PIPOLICY27", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "docx", "no", "docx", "0,0", "no", "no", "extcollab" },
				new String[] { "PIPOLICY28", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "docx", "docx", "no", "0,0", "no", "no", "extcollab" },
				new String[] { "PIPOLICY29", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "doc", "doc", "no", "0,0", "no", "no", "extcollab" },
				new String[] { "PIPOLICY30", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "docx", "no", "doc", "0,0", "no", "no", "extcollab" },
				new String[] { "PIPOLICY31", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "java", "java", "no", "0,0", "no", "no", "extcollab" },
				new String[] { "PIPOLICY32", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "java", "no", "no", "0,0", "Source Code", "no", "extcollab" },
				new String[] { "PIPOLICY33", "Policy Desc", "DataExposure", "Box", "public,external,any", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "txt", "no", "0,0", "no", "no", "extcollab" },
				new String[] { "PIPOLICY34", "Policy Desc", "DataExposure", "Box", "public,external,all", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "txt", "no", "0,0", "no", "no", "extcollab" },
				new String[] { "PIPOLICY35", "Policy Desc", "reader", "Google Drive", "internal,external,any", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PII", "no", "extcollab" },
				new String[] { "PIPOLICY36", "Policy Desc", "reader", "Google Drive", "internal,external,all", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PII", "no", "extcollab" },			
				new String[] { "PIPOLICY37", "Policy Desc", "DataExposure", "Box", "internal", "box-admin@protectautobeatle.com", "any", "any", "no", "no", "protectauto1@protectautobeatle.com", "any", "no", "no", "txt", "txt", "no", "0,0", "PII", "no", "intcollab" },				
				new String[] { "PIPOLICY38", "Policy Desc", "DataExposure", "Box", "public", "box-admin@protectautobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "txt", "no", "0,0", "PII", "no", "no" },
				new String[] { "DEACTIVATEDPOL", "Policy Desc", "DataExposure", "Dropbox", "unexposed", "any", "any","any", "no", "no", "any", "any", "no", "no", "pdf", "pdf", "no", "0,0", "PII", "no", "" },
				new String[] { "PIPOLICY39", "Policy Desc", "DataExposure", "Google Drive", "unexposed", "admin@protectautobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "no", "" },
				new String[] { "PIPOLICY40", "Policy Desc", "DataExposure", "Box", "unexposed", "box-admin@protectautobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "no", "" },
				new String[] { "PIPOLICY41", "Policy Desc", "DataExposure", "Dropbox", "unexposed", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "rar", "no", "0,0", "Virus", "no", "" },
				//new String[] { "PIPOLICY42", "Policy Desc", "DataExposure", "Dropbox", "unexposed", "any", "any", "any", "no", "no", "any", "any", "no", "no", "exe", "exe", "no", "2097152,6291456", "no", "no", "no" },
				//new String[] { "PIPOLICY43", "Policy Desc", "DataExposure", "Box", "public", "box-admin@protectbeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "exe", "exe", "no", "2097152,6291456", "no", "no", "no" },
				new String[] { "PIPOLICY44", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "exe", "exe", "no", "2097152,6291456", "no", "no", "no" },
				new String[] { "PIPOLICY45", "Policy Desc", "DataExposure", "Dropbox", "unexposed", "any", "any","any", "no", "no", "any", "any", "no", "no", "txt", "txt", "txt", "0,0", "FERPA", "no", "" },
				new String[] { "PIPOLICY46", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "FERPA", "no", "extcollab" },
				new String[] { "PIPOLICY47", "Policy Desc", "DataExposure", "Box", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "FERPA", "no", "extcollab" },				
		
		};				
	}	
	
	
}
