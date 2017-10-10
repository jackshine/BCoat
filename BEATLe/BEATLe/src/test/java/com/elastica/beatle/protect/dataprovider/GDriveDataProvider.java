package com.elastica.beatle.protect.dataprovider;

import org.testng.annotations.DataProvider;

public class GDriveDataProvider {
	
/*	private static GExcelDataProvider dataProvider;
	   
	public static void initialize() {
       String excelId = "1TmiwMrU3bFF0IwxwzTIXjkE5ipiITxpj0OHTAlxQkuE";
       dataProvider = new GExcelDataProvider(excelId);
	}
   
	public static GExcelDataProvider getDataProvider(){
       return dataProvider;
	}*/
   
	@DataProvider(name = "ChangeAccessDataProvider")
	public static Object[][] getChangeAccessDataProvider() {
		return new Object[][] {

				new String[] { "GDPOLICY101", "Policy Desc", "reader", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "writer" },
				new String[] { "GDPOLICY102", "Policy Desc", "reader", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "txt", "no", "0,0", "no", "no", "writer" },
				new String[] { "GDPOLICY103", "Policy Desc", "reader", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "writer" },
				new String[] { "GDPOLICY104", "Policy Desc", "reader", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PII", "no", "writer" },
				new String[] { "GDPOLICY105", "Policy Desc", "reader", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PCI", "no", "writer" },
				new String[] { "GDPOLICY106", "Policy Desc", "reader", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "GLBA", "no", "writer" },
				new String[] { "GDPOLICY107", "Policy Desc", "reader", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "no", "no", "0,0", "PCI", "no", "writer" },
				new String[] { "GDPOLICY108", "Policy Desc", "reader", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "no", "no", "0,0", "PII", "no", "writer" },
				new String[] { "GDPOLICY109", "Policy Desc", "reader", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "rtf", "no", "0,0", "no", "no", "writer" },
				new String[] { "GDPOLICY110", "Policy Desc", "reader", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "no", "rtf", "0,0", "no", "no", "writer" },
				new String[] { "GDPOLICY111", "Policy Desc", "reader", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "docx", "no", "docx", "0,0", "no", "no", "commenter" },
				new String[] { "GDPOLICY112", "Policy Desc", "reader", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "docx", "docx", "no", "0,0", "no", "no", "commenter" },
				new String[] { "GDPOLICY113", "Policy Desc", "reader", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "doc", "doc", "no", "0,0", "no", "no", "commenter" },
				new String[] { "GDPOLICY114", "Policy Desc", "reader", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "docx", "no", "doc", "0,0", "no", "no", "commenter" },
				new String[] { "GDPOLICY115", "Policy Desc", "reader", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "java", "java", "no", "0,0", "no", "no", "commenter" },
				new String[] { "GDPOLICY116", "Policy Desc", "reader", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "java", "no", "no", "0,0", "Source Code", "no", "commenter" },
				new String[] { "GDPOLICY117", "Policy Desc", "reader", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "html", "html", "no", "0,0", "no", "no", "commenter" },
				new String[] { "GDPOLICY118", "Policy Desc", "writer", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "html", "no", "html", "0,0", "no", "no", "commenter" },
				new String[] { "GDPOLICY119", "Policy Desc", "writer", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "exe", "exe", "no", "0,0", "no", "no", "commenter" },
				new String[] { "GDPOLICY120", "Policy Desc", "writer", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "exe", "no", "no", "2097152,0", "no", "no", "commenter" },
				new String[] { "GDPOLICY121", "Policy Desc", "writer", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "jpg", "jpg", "no", "0,0", "no", "no", "commenter" },
				new String[] { "GDPOLICY122", "Policy Desc", "writer", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "pdf", "pdf", "no", "0,0", "no", "no", "commenter" },
				new String[] { "GDPOLICY123", "Policy Desc", "writer", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "pdf", "no", "pdf", "0,0", "no", "no", "commenter" },
				new String[] { "GDPOLICY124", "Policy Desc", "writer", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "pdf", "no", "pdf", "0,0", "HIPAA", "no", "commenter" },
				new String[] { "GDPOLICY125", "Policy Desc", "writer", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "cs", "no", "cs", "0,0", "Source Code", "no", "reader" },
				new String[] { "GDPOLICY126", "Policy Desc", "writer", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "bin", "no", "no", "0,0", "Encryption", "no", "reader" },
				new String[] { "GDPOLICY127", "Policy Desc", "writer", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "bin", "no", "bin", "0,0", "no", "no", "reader" },
				new String[] { "GDPOLICY128", "Policy Desc", "writer", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xlsm", "no", "no", "0,0", "VBA Macros", "no", "reader" },
				new String[] { "GDPOLICY129", "Policy Desc", "writer", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xlsm", "xlsm", "no", "0,0", "no", "no", "reader" },
				new String[] { "GDPOLICY130", "Policy Desc", "writer", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xls", "no", "no", "0,0", "Encryption", "no", "reader" },
				new String[] { "GDPOLICY131", "Policy Desc", "writer", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xls", "xls", "no", "0,0", "no", "no", "reader" },
				new String[] { "GDPOLICY132", "Policy Desc", "writer", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xls", "no", "xls", "0,0", "no", "no", "reader" },
				new String[] { "GDPOLICY133", "Policy Desc", "writer", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "rar", "no", "0,0", "no", "no", "reader" },
				new String[] { "GDPOLICY134", "Policy Desc", "writer", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "no", "0,0", "Virus", "no", "reader" },
				new String[] { "GDPOLICY135", "Policy Desc", "writer", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "pdf", "no", "pdf", "0,0", "PII", "no", "reader" },
				new String[] { "GDPOLICY136", "Policy Desc", "writer", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "zip", "no", "zip", "0,0", "no", "no", "reader" },
				new String[] { "GDPOLICY137", "Policy Desc", "writer", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "zip", "no", "no", "0,0", "Virus", "no", "reader" },
				new String[] { "GDPOLICY138", "Policy Desc", "writer", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "zip", "no", "zip", "0,0", "Virus", "no", "reader" },
				new String[] { "GDPOLICY139", "Policy Desc", "reader", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "js", "no", "js", "0,0", "no", "no", "commenter" },
				new String[] { "GDPOLICY140", "Policy Desc", "reader", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "js", "no", "js", "0,0", "Source Code", "no", "commenter" },
			};
		
	}
	
	@DataProvider(name = "PreventDownloadPrintCopyDataProvider")
	public static Object[][] getPreventDownloadPrintCopyDataProvider() {
		return new Object[][] {
				
				new String[] { "GDPOLICY401", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "preventCopyPrintDownload" },
				new String[] { "GDPOLICY402", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "txt", "no", "0,0", "no", "no", "preventCopyPrintDownload" },
				new String[] { "GDPOLICY403", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "preventCopyPrintDownload" },
				new String[] { "GDPOLICY404", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PII", "no", "preventCopyPrintDownload" },
				new String[] { "GDPOLICY405", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PCI", "no", "preventCopyPrintDownload" },
				new String[] { "GDPOLICY406", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "GLBA", "no", "preventCopyPrintDownload" },
				new String[] { "GDPOLICY407", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "rtf", "no", "0,0", "PCI", "no", "preventCopyPrintDownload" },
				new String[] { "GDPOLICY408", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "no", "rtf", "0,0", "PII", "no", "preventCopyPrintDownload" },
				new String[] { "GDPOLICY409", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "no", "rtf", "0,0", "PCI", "no", "preventCopyPrintDownload" },
				new String[] { "GDPOLICY410", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "no", "rtf", "0,0", "no", "no", "preventCopyPrintDownload" },
				new String[] { "GDPOLICY411", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "docx", "no", "docx", "0,0", "no", "no", "preventCopyPrintDownload" },
				new String[] { "GDPOLICY412", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "docx", "docx", "no", "0,0", "no", "no", "preventCopyPrintDownload" },
				new String[] { "GDPOLICY413", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "doc", "doc", "no", "0,0", "no", "no", "preventCopyPrintDownload" },
				new String[] { "GDPOLICY414", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "docx", "no", "doc", "0,0", "no", "no", "preventCopyPrintDownload" },
				new String[] { "GDPOLICY415", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "java", "java", "no", "0,0", "no", "no", "preventCopyPrintDownload" },
				new String[] { "GDPOLICY416", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "java", "no", "no", "0,0", "Source Code", "no", "preventCopyPrintDownload" },
				new String[] { "GDPOLICY417", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "html", "html", "no", "0,0", "no", "no", "preventCopyPrintDownload" },
				new String[] { "GDPOLICY418", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "html", "no", "html", "0,0", "no", "no", "preventCopyPrintDownload" },
				new String[] { "GDPOLICY419", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "exe", "exe", "no", "0,0", "no", "no", "preventCopyPrintDownload" },
				new String[] { "GDPOLICY420", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "exe", "no", "no", "2097152,0", "no", "no", "preventCopyPrintDownload" },
				new String[] { "GDPOLICY421", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "jpg", "jpg", "no", "0,0", "no", "no", "preventCopyPrintDownload" },
				new String[] { "GDPOLICY422", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "pdf", "pdf", "no", "0,0", "HIPAA", "no", "preventCopyPrintDownload" },
				new String[] { "GDPOLICY423", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "cs", "cs", "no", "0,0", "Source Code", "no", "preventCopyPrintDownload" },
				new String[] { "GDPOLICY424", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "pdf", "no", "pdf", "0,0", "HIPAA", "no", "preventCopyPrintDownload" },
				new String[] { "GDPOLICY425", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "cs", "no", "cs", "0,0", "Source Code", "no", "preventCopyPrintDownload" },
				new String[] { "GDPOLICY426", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "bin", "bin", "no", "0,0", "Encryption", "no", "preventCopyPrintDownload" },
				new String[] { "GDPOLICY427", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "bin", "bin", "bin", "0,0", "no", "no", "preventCopyPrintDownload" },
				new String[] { "GDPOLICY428", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xlsm", "xlsm", "no", "0,0", "VBA Macros", "no", "preventCopyPrintDownload" },
				new String[] { "GDPOLICY429", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xlsm", "xlsm", "no", "0,0", "no", "no", "preventCopyPrintDownload" },
				new String[] { "GDPOLICY430", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xls", "no", "xls", "0,0", "Encryption", "no", "preventCopyPrintDownload" },
				new String[] { "GDPOLICY431", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xls", "xls", "no", "0,0", "no", "no", "preventCopyPrintDownload" },
				new String[] { "GDPOLICY432", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xls", "xls", "xls", "0,0", "no", "no", "preventCopyPrintDownload" },
				new String[] { "GDPOLICY433", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "rar", "no", "0,0", "Virus", "no", "preventCopyPrintDownload" },
				new String[] { "GDPOLICY434", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "no", "preventCopyPrintDownload" },
				new String[] { "GDPOLICY435", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "pdf", "no", "pdf", "0,0", "PII", "no", "preventCopyPrintDownload" },
				new String[] { "GDPOLICY436", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "zip", "no", "zip", "0,0", "Virus", "no", "preventCopyPrintDownload" },
				new String[] { "GDPOLICY437", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "zip", "zip", "no", "0,0", "Virus", "no", "preventCopyPrintDownload" },
				new String[] { "GDPOLICY438", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "zip", "no", "zip", "0,0", "no", "no", "preventCopyPrintDownload" },
				new String[] { "GDPOLICY439", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "js", "js", "no", "0,0", "Source Code", "no", "preventCopyPrintDownload" },
				new String[] { "GDPOLICY440", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "js", "no", "js", "0,0", "Source Code", "no", "preventCopyPrintDownload" },

		};
		
	}
	
	@DataProvider(name = "PreventWritersCanShareDataProvider")
	public static Object[][] getPreventWritersCanShareDataProvider() {
		return new Object[][] {
				
				new String[] { "GDPOLICY501", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "preventWritersCanShare" },
				new String[] { "GDPOLICY502", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "txt", "no", "0,0", "no", "no", "preventWritersCanShare" },
				new String[] { "GDPOLICY503", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "preventWritersCanShare" },
				new String[] { "GDPOLICY504", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PII", "no", "preventWritersCanShare" },
				new String[] { "GDPOLICY505", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PCI", "no", "preventWritersCanShare" },
				new String[] { "GDPOLICY506", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "GLBA", "no", "preventWritersCanShare" },
				new String[] { "GDPOLICY507", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "rtf", "no", "0,0", "PCI", "no", "preventWritersCanShare" },
				new String[] { "GDPOLICY508", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "no", "rtf", "0,0", "PII", "no", "preventWritersCanShare" },
				new String[] { "GDPOLICY509", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "no", "rtf", "0,0", "PCI", "no", "preventWritersCanShare" },
				new String[] { "GDPOLICY510", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "no", "rtf", "0,0", "no", "no", "preventWritersCanShare" },
				new String[] { "GDPOLICY511", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "docx", "no", "docx", "0,0", "no", "no", "preventWritersCanShare" },
				new String[] { "GDPOLICY512", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "docx", "docx", "no", "0,0", "no", "no", "preventWritersCanShare" },
				new String[] { "GDPOLICY513", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "doc", "doc", "no", "0,0", "no", "no", "preventWritersCanShare" },
				new String[] { "GDPOLICY514", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "docx", "no", "doc", "0,0", "no", "no", "preventWritersCanShare" },
				new String[] { "GDPOLICY515", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "java", "java", "no", "0,0", "no", "no", "preventWritersCanShare" },
				new String[] { "GDPOLICY516", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "java", "no", "no", "0,0", "Source Code", "no", "preventWritersCanShare" },
				new String[] { "GDPOLICY517", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "html", "html", "no", "0,0", "no", "no", "preventWritersCanShare" },
				new String[] { "GDPOLICY518", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "html", "no", "html", "0,0", "no", "no", "preventWritersCanShare" },
				new String[] { "GDPOLICY519", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "exe", "exe", "no", "0,0", "no", "no", "preventWritersCanShare" },
				new String[] { "GDPOLICY520", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "exe", "no", "no", "2097152,0", "no", "no", "preventWritersCanShare" },
				new String[] { "GDPOLICY521", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "jpg", "jpg", "no", "0,0", "no", "no", "preventWritersCanShare" },
				new String[] { "GDPOLICY522", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "pdf", "pdf", "no", "0,0", "HIPAA", "no", "preventWritersCanShare" },
				new String[] { "GDPOLICY523", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "cs", "cs", "no", "0,0", "Source Code", "no", "preventWritersCanShare" },
				new String[] { "GDPOLICY524", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "pdf", "no", "pdf", "0,0", "HIPAA", "no", "preventWritersCanShare" },
				new String[] { "GDPOLICY525", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "cs", "no", "cs", "0,0", "Source Code", "no", "preventWritersCanShare" },
				new String[] { "GDPOLICY526", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "bin", "bin", "no", "0,0", "Encryption", "no", "preventWritersCanShare" },
				new String[] { "GDPOLICY527", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "bin", "bin", "bin", "0,0", "no", "no", "preventWritersCanShare" },
				new String[] { "GDPOLICY528", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xlsm", "xlsm", "no", "0,0", "VBA Macros", "no", "preventWritersCanShare" },
				new String[] { "GDPOLICY529", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xlsm", "xlsm", "no", "0,0", "no", "no", "preventWritersCanShare" },
				new String[] { "GDPOLICY530", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xls", "no", "xls", "0,0", "Encryption", "no", "preventWritersCanShare" },
				new String[] { "GDPOLICY531", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xls", "xls", "no", "0,0", "no", "no", "preventWritersCanShare" },
				new String[] { "GDPOLICY532", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xls", "xls", "xls", "0,0", "no", "no", "preventWritersCanShare" },
				new String[] { "GDPOLICY533", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "rar", "no", "0,0", "Virus", "no", "preventWritersCanShare" },
				new String[] { "GDPOLICY534", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "no", "preventWritersCanShare" },
				new String[] { "GDPOLICY535", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "pdf", "no", "pdf", "0,0", "PII", "no", "preventWritersCanShare" },
				new String[] { "GDPOLICY536", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "zip", "no", "zip", "0,0", "Virus", "no", "preventWritersCanShare" },
				new String[] { "GDPOLICY537", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "zip", "zip", "no", "0,0", "Virus", "no", "preventWritersCanShare" },
				new String[] { "GDPOLICY538", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "zip", "no", "zip", "0,0", "no", "no", "preventWritersCanShare" },
				new String[] { "GDPOLICY539", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "js", "js", "no", "0,0", "Source Code", "no", "preventWritersCanShare" },
				new String[] { "GDPOLICY540", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "js", "no", "js", "0,0", "Source Code", "no", "preventWritersCanShare" },
				new String[] { "GDPOLICY541", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "zip", "zip", "no", "1048576,2097152", "Virus", "no", "preventWritersCanShare" },
				new String[] { "GDPOLICY542", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "zip", "zip", "no", "1048576,0", "Virus", "no", "preventWritersCanShare" },
				new String[] { "GDPOLICY543", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "zip", "zip", "no", "0,2097152", "Virus", "no", "preventWritersCanShare" },
		};
	}
	
	@DataProvider(name = "RemediationCombinationDataProvider")
	public static Object[][] getRemediationCombinationDataProvider() {
		return new Object[][] {

				new String[] { "GDPOLICY701", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "preventWritersCanShare,preventCopyPrintDownload" },
				new String[] { "GDPOLICY702", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "txt", "no", "0,0", "no", "no", "preventWritersCanShare,preventCopyPrintDownload" },
				new String[] { "GDPOLICY703", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "preventWritersCanShare,preventCopyPrintDownload" },
				new String[] { "GDPOLICY704", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PII", "no", "preventWritersCanShare,preventCopyPrintDownload" },
				new String[] { "GDPOLICY705", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PCI", "no", "preventWritersCanShare,preventCopyPrintDownload" },
				new String[] { "GDPOLICY706", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "GLBA", "no", "preventWritersCanShare,preventCopyPrintDownload" },
				new String[] { "GDPOLICY707", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "rtf", "no", "0,0", "PCI", "no", "preventWritersCanShare,preventCopyPrintDownload" },
				new String[] { "GDPOLICY708", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "no", "rtf", "0,0", "PII", "no", "preventWritersCanShare,preventCopyPrintDownload" },
				new String[] { "GDPOLICY709", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "no", "rtf", "0,0", "PCI", "no", "preventWritersCanShare,preventCopyPrintDownload" },
				new String[] { "GDPOLICY710", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "no", "rtf", "0,0", "no", "no", "preventWritersCanShare,preventCopyPrintDownload" },
				new String[] { "GDPOLICY711", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "docx", "no", "docx", "0,0", "no", "no", "preventWritersCanShare,preventCopyPrintDownload" },
				new String[] { "GDPOLICY712", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "docx", "docx", "no", "0,0", "no", "no", "preventWritersCanShare,preventCopyPrintDownload" },
				new String[] { "GDPOLICY713", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "doc", "doc", "no", "0,0", "no", "no", "preventWritersCanShare,preventCopyPrintDownload" },
				new String[] { "GDPOLICY714", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "docx", "no", "doc", "0,0", "no", "no", "preventWritersCanShare,preventCopyPrintDownload" },
				new String[] { "GDPOLICY715", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "java", "java", "no", "0,0", "no", "no", "preventWritersCanShare,preventCopyPrintDownload" },
				new String[] { "GDPOLICY716", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "java", "no", "no", "0,0", "Source Code", "no", "preventWritersCanShare,preventCopyPrintDownload" },
				new String[] { "GDPOLICY717", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "html", "html", "no", "0,0", "no", "no", "preventWritersCanShare,preventCopyPrintDownload" },
				new String[] { "GDPOLICY718", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "html", "no", "html", "0,0", "no", "no", "preventWritersCanShare,preventCopyPrintDownload" },
				new String[] { "GDPOLICY719", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "exe", "exe", "no", "0,0", "no", "no", "preventWritersCanShare,preventCopyPrintDownload" },
				new String[] { "GDPOLICY720", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "exe", "no", "no", "2097152,0", "no", "no", "preventWritersCanShare,preventCopyPrintDownload" },
				new String[] { "GDPOLICY721", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "jpg", "jpg", "no", "0,0", "no", "no", "preventWritersCanShare,preventCopyPrintDownload" },
				new String[] { "GDPOLICY722", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "pdf", "pdf", "no", "0,0", "HIPAA", "no", "preventWritersCanShare,preventCopyPrintDownload" },
				new String[] { "GDPOLICY723", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "cs", "cs", "no", "0,0", "Source Code", "no", "preventWritersCanShare,preventCopyPrintDownload" },
				new String[] { "GDPOLICY724", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "pdf", "no", "pdf", "0,0", "HIPAA", "no", "preventWritersCanShare,preventCopyPrintDownload" },
				new String[] { "GDPOLICY725", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "cs", "no", "cs", "0,0", "Source Code", "no", "preventWritersCanShare,preventCopyPrintDownload" },
				new String[] { "GDPOLICY726", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "bin", "bin", "no", "0,0", "Encryption", "no", "preventWritersCanShare,preventCopyPrintDownload" },
				new String[] { "GDPOLICY727", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "bin", "bin", "bin", "0,0", "no", "no", "preventWritersCanShare,preventCopyPrintDownload" },
				new String[] { "GDPOLICY728", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xlsm", "xlsm", "no", "0,0", "VBA Macros", "no", "preventWritersCanShare,preventCopyPrintDownload" },
				new String[] { "GDPOLICY729", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xlsm", "xlsm", "no", "0,0", "no", "no", "preventWritersCanShare,preventCopyPrintDownload" },
				new String[] { "GDPOLICY730", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xls", "no", "xls", "0,0", "Encryption", "no", "preventWritersCanShare,preventCopyPrintDownload" },
				new String[] { "GDPOLICY731", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xls", "xls", "no", "0,0", "no", "no", "preventWritersCanShare,preventCopyPrintDownload" },
				new String[] { "GDPOLICY732", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xls", "xls", "xls", "0,0", "no", "no", "preventWritersCanShare,preventCopyPrintDownload" },
				new String[] { "GDPOLICY733", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "rar", "no", "0,0", "Virus", "no", "preventWritersCanShare,preventCopyPrintDownload" },
				new String[] { "GDPOLICY734", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "no", "preventWritersCanShare,preventCopyPrintDownload" },
				new String[] { "GDPOLICY735", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "pdf", "no", "pdf", "0,0", "PII", "no", "preventWritersCanShare,preventCopyPrintDownload" },
				new String[] { "GDPOLICY736", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "zip", "no", "zip", "0,0", "Virus", "no", "preventWritersCanShare,preventCopyPrintDownload" },
				new String[] { "GDPOLICY737", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "zip", "zip", "no", "0,0", "Virus", "no", "preventWritersCanShare,preventCopyPrintDownload" },
				new String[] { "GDPOLICY738", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "zip", "no", "zip", "0,0", "no", "no", "preventWritersCanShare,preventCopyPrintDownload" },
				new String[] { "GDPOLICY739", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "js", "js", "no", "0,0", "Source Code", "no", "preventWritersCanShare,preventCopyPrintDownload" },
				new String[] { "GDPOLICY740", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "js", "no", "js", "0,0", "Source Code", "no", "preventWritersCanShare,preventCopyPrintDownload" },
				new String[] { "GDPOLICY741", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "zip", "zip", "no", "1048576,2097152", "Virus", "no", "preventWritersCanShare,preventCopyPrintDownload" },
				new String[] { "GDPOLICY742", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "zip", "zip", "no", "1048576,0", "Virus", "no", "preventWritersCanShare,preventCopyPrintDownload" },
				new String[] { "GDPOLICY743", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "zip", "zip", "no", "0,2097152", "Virus", "no", "preventWritersCanShare,preventCopyPrintDownload" },

		};
	}
	
	@DataProvider(name = "RemoveCollaboratorDataProvider")
	public static Object[][] getRemoveCollaboratorDataProvider() {
		return new Object[][] {
				new String[] { "GDPOLICY301", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "removeCollab" },
				new String[] { "GDPOLICY302", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "txt", "no", "0,0", "no", "no", "removeCollab" },
				new String[] { "GDPOLICY303", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "removeCollab" },
				new String[] { "GDPOLICY304", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PII", "no", "removeCollab" },
				new String[] { "GDPOLICY305", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PCI", "no", "removeCollab" },
				new String[] { "GDPOLICY306", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "GLBA", "no", "removeCollab" },
				new String[] { "GDPOLICY307", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "rtf", "no", "0,0", "PCI", "no", "removeCollab" },
				new String[] { "GDPOLICY308", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "no", "rtf", "0,0", "PII", "no", "removeCollab" },
				new String[] { "GDPOLICY309", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "no", "rtf", "0,0", "PCI", "no", "removeCollab" },
				new String[] { "GDPOLICY310", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "no", "rtf", "0,0", "no", "no", "removeCollab" },
				new String[] { "GDPOLICY311", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "docx", "no", "docx", "0,0", "no", "no", "removeCollab" },
				new String[] { "GDPOLICY312", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "docx", "docx", "no", "0,0", "no", "no", "removeCollab" },
				new String[] { "GDPOLICY313", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "doc", "doc", "no", "0,0", "no", "no", "removeCollab" },
				new String[] { "GDPOLICY314", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "docx", "no", "doc", "0,0", "no", "no", "removeCollab" },
				new String[] { "GDPOLICY315", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "java", "java", "no", "0,0", "no", "no", "removeCollab" },
				new String[] { "GDPOLICY316", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "java", "no", "no", "0,0", "Source Code", "no", "removeCollab" },
				new String[] { "GDPOLICY317", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "html", "html", "no", "0,0", "no", "no", "removeCollab" },
				new String[] { "GDPOLICY318", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "html", "no", "html", "0,0", "no", "no", "removeCollab" },
				new String[] { "GDPOLICY319", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "exe", "exe", "no", "0,0", "no", "no", "removeCollab" },
				new String[] { "GDPOLICY320", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "exe", "no", "no", "2097152,0", "no", "no", "removeCollab" },
				new String[] { "GDPOLICY321", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "jpg", "jpg", "no", "0,0", "no", "no", "removeCollab" },
				new String[] { "GDPOLICY322", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "pdf", "pdf", "no", "0,0", "HIPAA", "no", "removeCollab" },
				new String[] { "GDPOLICY323", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "cs", "cs", "no", "0,0", "Source Code", "no", "removeCollab" },
				new String[] { "GDPOLICY324", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "pdf", "no", "pdf", "0,0", "HIPAA", "no", "removeCollab" },
				new String[] { "GDPOLICY325", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "cs", "no", "cs", "0,0", "Source Code", "no", "removeCollab" },
				new String[] { "GDPOLICY326", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "bin", "bin", "bin", "0,0", "Encryption", "no", "removeCollab" },
				new String[] { "GDPOLICY327", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "bin", "bin", "bin", "0,0", "no", "no", "removeCollab" },
				new String[] { "GDPOLICY328", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xlsm", "xlsm", "no", "0,0", "VBA Macros", "no", "removeCollab" },
				new String[] { "GDPOLICY329", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xlsm", "xlsm", "no", "0,0", "no", "no", "removeCollab" },
				new String[] { "GDPOLICY330", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xls", "no", "xls", "0,0", "Encryption", "no", "removeCollab" },
				new String[] { "GDPOLICY331", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xls", "xls", "no", "0,0", "no", "no", "removeCollab" },
				new String[] { "GDPOLICY332", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xls", "xls", "xls", "0,0", "no", "no", "removeCollab" },
				new String[] { "GDPOLICY333", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "rar", "no", "0,0", "Virus", "no", "removeCollab" },
				new String[] { "GDPOLICY334", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "no", "removeCollab" },
				new String[] { "GDPOLICY335", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "pdf", "no", "pdf", "0,0", "PII", "no", "removeCollab" },
				new String[] { "GDPOLICY336", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "zip", "no", "zip", "0,0", "Virus", "no", "removeCollab" },
				new String[] { "GDPOLICY337", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "zip", "no", "no", "0,0", "Virus", "no", "removeCollab" },
				new String[] { "GDPOLICY338", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "zip", "no", "zip", "0,0", "no", "no", "removeCollab" },
				new String[] { "GDPOLICY339", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "js", "no", "js", "0,0", "Source Code", "no", "removeCollab" },
				new String[] { "GDPOLICY340", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "js", "no", "js", "0,0", "no", "no", "removeCollab" },
		};
		
	}
	
	@DataProvider(name = "RemoveLinkDataProvider")
	public static Object[][] getRemoveLinkDataProvider() {
		return new Object[][] {
				// Data Exposure - unexposed
				new String[] { "GDPOLICY1", "Policy Desc", "DataExposure", "Google Drive", "unexposed", "admin@protectbeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "no", "no" },
				new String[] { "GDPOLICY2", "Policy Desc", "DataExposure", "Google Drive", "unexposed", "admin@protectbeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "rar", "no", "0,0", "Virus", "no", "no" },
				new String[] { "GDPOLICY3", "Policy Desc", "DataExposure", "Google Drive", "unexposed", "admin@protectbeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "xls", "no", "xls", "0,0", "Encryption", "no", "no" },
				new String[] { "GDPOLICY4", "Policy Desc", "DataExposure", "Google Drive", "unexposed", "admin@protectbeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "xls", "xls", "no", "0,0", "Encryption", "no", "no" },
				new String[] { "GDPOLICY5", "Policy Desc", "DataExposure", "Google Drive", "unexposed", "admin@protectbeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "bin", "bin", "no", "0,0", "Encryption", "no", "no" },
				new String[] { "GDPOLICY6", "Policy Desc", "DataExposure", "Google Drive", "unexposed", "admin@protectbeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "xlsm", "xlsm", "no", "0,0", "VBA Macros", "no", "no" },
				new String[] { "GDPOLICY7", "Policy Desc", "DataExposure", "Google Drive", "unexposed", "admin@protectbeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "pdf", "pdf", "no", "0,0", "HIPAA", "no", "no" },
				new String[] { "GDPOLICY8", "Policy Desc", "DataExposure", "Google Drive", "unexposed", "admin@protectbeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "pdf", "pdf", "no", "0,0", "PII", "no", "no" },
				new String[] { "GDPOLICY9", "Policy Desc", "DataExposure", "Google Drive", "unexposed", "admin@protectbeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "cs", "cs", "no", "0,0", "Source Code", "no", "no" },
				new String[] { "GDPOLICY10", "Policy Desc", "DataExposure", "Google Drive", "unexposed", "admin@protectbeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "java", "java", "no", "0,0", "Source Code", "no", "no" },
				new String[] { "GDPOLICY11", "Policy Desc", "DataExposure", "Google Drive", "unexposed", "admin@protectbeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "txt", "txt", "0,0", "GLBA", "no", "no" },
				new String[] { "GDPOLICY12", "Policy Desc", "DataExposure", "Google Drive", "unexposed", "admin@protectbeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "txt", "txt", "0,0", "PII", "no", "no" },
				new String[] { "GDPOLICY13", "Policy Desc", "DataExposure", "Google Drive", "unexposed", "admin@protectbeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "txt", "txt", "0,0", "PCI", "no", "no" },

				// Data Exposure - Public //Verifing unshare remidiation
				new String[] { "GDPOLICY14", "Policy Desc", "DataExposure", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "unshare" },
				new String[] { "GDPOLICY15", "Policy Desc", "DataExposure", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "txt", "no", "0,0", "no", "no", "unshare" },
				new String[] { "GDPOLICY16", "Policy Desc", "DataExposure", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "unshare" },
				new String[] { "GDPOLICY17", "Policy Desc", "DataExposure", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PII", "no", "unshare" },
				new String[] { "GDPOLICY18", "Policy Desc", "DataExposure", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PCI", "no", "unshare" },
				new String[] { "GDPOLICY19", "Policy Desc", "DataExposure", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "GLBA", "no", "unshare" },
				new String[] { "GDPOLICY20", "Policy Desc", "DataExposure", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "rtf", "no", "0,0", "PCI", "no", "unshare" },
				new String[] { "GDPOLICY21", "Policy Desc", "DataExposure", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "no", "rtf", "0,0", "PII", "no", "unshare" },
				new String[] { "GDPOLICY22", "Policy Desc", "DataExposure", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "no", "rtf", "0,0", "PCI", "no", "unshare" },
				new String[] { "GDPOLICY23", "Policy Desc", "DataExposure", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "no", "rtf", "0,0", "no", "no", "unshare" },
				new String[] { "GDPOLICY24", "Policy Desc", "DataExposure", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "docx", "no", "docx", "0,0", "no", "no", "unshare" },
				new String[] { "GDPOLICY25", "Policy Desc", "DataExposure", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "docx", "docx", "no", "0,0", "no", "no", "unshare" },
				new String[] { "GDPOLICY26", "Policy Desc", "DataExposure", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "doc", "doc", "no", "0,0", "no", "no", "unshare" },
				new String[] { "GDPOLICY27", "Policy Desc", "DataExposure", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "docx", "no", "doc", "0,0", "no", "no", "unshare" },
				new String[] { "GDPOLICY28", "Policy Desc", "DataExposure", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "java", "java", "no", "0,0", "no", "no", "unshare" },
				new String[] { "GDPOLICY29", "Policy Desc", "DataExposure", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "java", "no", "no", "0,0", "Source Code", "no", "unshare" },
				new String[] { "GDPOLICY30", "Policy Desc", "DataExposure", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "html", "html", "no", "0,0", "no", "no", "unshare" },
				new String[] { "GDPOLICY31", "Policy Desc", "DataExposure", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "html", "no", "html", "0,0", "no", "no", "unshare" },
				new String[] { "GDPOLICY32", "Policy Desc", "DataExposure", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "exe", "exe", "no", "0,0", "no", "no", "unshare" },
				new String[] { "GDPOLICY33", "Policy Desc", "DataExposure", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "exe", "no", "no", "2097152,0", "no", "no", "unshare" },
				new String[] { "GDPOLICY34", "Policy Desc", "DataExposure", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "jpg", "jpg", "no", "0,0", "no", "no", "unshare" },
				new String[] { "GDPOLICY35", "Policy Desc", "DataExposure", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "pdf", "pdf", "no", "0,0", "HIPAA", "no", "unshare" },
				new String[] { "GDPOLICY36", "Policy Desc", "DataExposure", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "cs", "cs", "no", "0,0", "Source Code", "no", "unshare" },
				new String[] { "GDPOLICY37", "Policy Desc", "DataExposure", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "pdf", "no", "pdf", "0,0", "HIPAA", "no", "unshare" },
				new String[] { "GDPOLICY38", "Policy Desc", "DataExposure", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "cs", "no", "cs", "0,0", "Source Code", "no", "unshare" },
				new String[] { "GDPOLICY39", "Policy Desc", "DataExposure", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "bin", "bin", "no", "0,0", "Encryption", "no", "unshare" },
				new String[] { "GDPOLICY40", "Policy Desc", "DataExposure", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "bin", "bin", "bin", "0,0", "no", "no", "unshare" },
				new String[] { "GDPOLICY41", "Policy Desc", "DataExposure", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xlsm", "xlsm", "no", "0,0", "VBA Macros", "no", "unshare" },
				new String[] { "GDPOLICY42", "Policy Desc", "DataExposure", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xlsm", "xlsm", "no", "0,0", "no", "no", "unshare" },
				new String[] { "GDPOLICY43", "Policy Desc", "DataExposure", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xls", "no", "xls", "0,0", "Encryption", "no", "unshare" },
				new String[] { "GDPOLICY44", "Policy Desc", "DataExposure", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xls", "xls", "no", "0,0", "no", "no", "unshare" },
				new String[] { "GDPOLICY45", "Policy Desc", "DataExposure", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xls", "xls", "xls", "0,0", "no", "no", "unshare" },
				new String[] { "GDPOLICY46", "Policy Desc", "DataExposure", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "rar", "no", "0,0", "Virus", "no", "unshare" },
				new String[] { "GDPOLICY47", "Policy Desc", "DataExposure", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "no", "unshare" },
				new String[] { "GDPOLICY48", "Policy Desc", "DataExposure", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "pdf", "no", "pdf", "0,0", "PII", "no", "unshare" },
				new String[] { "GDPOLICY49", "Policy Desc", "DataExposure", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "zip", "no", "zip", "0,0", "Virus", "no", "unshare" },
				new String[] { "GDPOLICY50", "Policy Desc", "DataExposure", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "zip", "zip", "no", "0,0", "Virus", "no", "unshare" },
				new String[] { "GDPOLICY51", "Policy Desc", "DataExposure", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "zip", "no", "zip", "0,0", "no", "no", "unshare" },
				new String[] { "GDPOLICY52", "Policy Desc", "DataExposure", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "js", "js", "no", "0,0", "Source Code", "no", "unshare" },
				new String[] { "GDPOLICY53", "Policy Desc", "DataExposure", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "js", "no", "js", "0,0", "Source Code", "no", "unshare" },

		};
	}
	
	@DataProvider(name = "UpdateCollaboratorDataProvider")
	public static Object[][] getUpdateCollaboratorDataProvider() {
		return new Object[][] {
				new String[] { "GDPOLICY601", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "updateWriter" },
				new String[] { "GDPOLICY602", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "txt", "no", "0,0", "no", "no", "updateWriter" },
				new String[] { "GDPOLICY603", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "updateWriter" },
				new String[] { "GDPOLICY604", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PII", "no", "updateWriter" },
				new String[] { "GDPOLICY605", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PCI", "no", "updateWriter" },
				new String[] { "GDPOLICY606", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "GLBA", "no", "updateWriter" },
				new String[] { "GDPOLICY607", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "rtf", "no", "0,0", "PCI", "no", "updateWriter" },
				new String[] { "GDPOLICY608", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "no", "rtf", "0,0", "PII", "no", "updateWriter" },
				new String[] { "GDPOLICY609", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "no", "rtf", "0,0", "PCI", "no", "updateWriter" },
				new String[] { "GDPOLICY610", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "no", "rtf", "0,0", "no", "no", "updateWriter" },
				new String[] { "GDPOLICY611", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "docx", "no", "docx", "0,0", "no", "no", "updateWriter" },
				new String[] { "GDPOLICY612", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "docx", "docx", "no", "0,0", "no", "no", "updateWriter" },
				new String[] { "GDPOLICY613", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "doc", "doc", "no", "0,0", "no", "no", "updateReader" },
				new String[] { "GDPOLICY614", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "docx", "no", "doc", "0,0", "no", "no", "updateReader" },
				new String[] { "GDPOLICY615", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "java", "java", "no", "0,0", "no", "no", "updateReader" },
				new String[] { "GDPOLICY616", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "java", "no", "no", "0,0", "Source Code", "no", "updateReader" },
				new String[] { "GDPOLICY617", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "html", "html", "no", "0,0", "no", "no", "updateReader" },
				new String[] { "GDPOLICY618", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "html", "no", "html", "0,0", "no", "no", "updateReader" },
				new String[] { "GDPOLICY619", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "exe", "exe", "no", "0,0", "no", "no", "updateReader" },
				new String[] { "GDPOLICY620", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "exe", "no", "no", "2097152,0", "no", "no", "updateReader" },
				new String[] { "GDPOLICY621", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "jpg", "jpg", "no", "0,0", "no", "no", "updateReader" },
				new String[] { "GDPOLICY622", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "pdf", "pdf", "no", "0,0", "HIPAA", "no", "updateReader" },
				new String[] { "GDPOLICY623", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "cs", "cs", "no", "0,0", "Source Code", "no", "updateReader" },
				new String[] { "GDPOLICY624", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "pdf", "no", "pdf", "0,0", "HIPAA", "no", "updateCommenter" },
				new String[] { "GDPOLICY625", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "cs", "no", "cs", "0,0", "Source Code", "no", "updateCommenter" },
				new String[] { "GDPOLICY626", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "bin", "bin", "no", "0,0", "Encryption", "no", "updateCommenter" },
				new String[] { "GDPOLICY627", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xlsm", "xlsm", "no", "0,0", "VBA Macros", "no", "updateCommenter" },
				new String[] { "GDPOLICY628", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xlsm", "xlsm", "no", "0,0", "no", "no", "updateCommenter" },
				new String[] { "GDPOLICY629", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xls", "no", "xls", "0,0", "Encryption", "no", "updateCommenter" },
				new String[] { "GDPOLICY630", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xls", "xls", "no", "0,0", "no", "no", "updateCommenter" },
				new String[] { "GDPOLICY631", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xls", "xls", "xls", "0,0", "no", "no", "updateCommenter" },
				new String[] { "GDPOLICY632", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "rar", "no", "0,0", "Virus", "no", "updateCommenter" },
				new String[] { "GDPOLICY633", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "no", "updateCommenter" },
				new String[] { "GDPOLICY634", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "pdf", "no", "pdf", "0,0", "PII", "no", "updateCommenter" },
		};
	}

	@DataProvider(name = "MoveToTrashDataProvider")
	public static Object[][] getGmailMoveToTrashData() {
		return new Object[][] {
			// External User
			new String[] { "GMPOLICY01", "Policy Desc", "DataExposure", "Gmail", "external", "admin@protectbeatle.com", "any", "any", "no", "no", "mayurbelekar@gmail.com", "any", "no", "no", "txt",  "no",   "txt", "0,0", "PII",         "no", "moveToTrash" },
			new String[] { "GMPOLICY02", "Policy Desc", "DataExposure", "Gmail", "external", "admin@protectbeatle.com", "any", "any", "no", "no", "mayurbelekar@gmail.com", "any", "no", "no", "txt",  "no",   "txt", "0,0", "PCI",         "no", "moveToTrash" },
			new String[] { "GMPOLICY03", "Policy Desc", "DataExposure", "Gmail", "external", "admin@protectbeatle.com", "any", "any", "no", "no", "mayurbelekar@gmail.com", "any", "no", "no", "txt",  "no",   "txt", "0,0", "GLBA",        "no", "moveToTrash" },
			new String[] { "GMPOLICY04", "Policy Desc", "DataExposure", "Gmail", "external", "admin@protectbeatle.com", "any", "any", "no", "no", "mayurbelekar@gmail.com", "any", "no", "no", "rtf",  "rtf",  "no",  "0,0", "PCI",         "no", "moveToTrash" },
			new String[] { "GMPOLICY05", "Policy Desc", "DataExposure", "Gmail", "external", "admin@protectbeatle.com", "any", "any", "no", "no", "mayurbelekar@gmail.com", "any", "no", "no", "rtf",  "no",   "rtf", "0,0", "PII",         "no", "moveToTrash" },
			new String[] { "GMPOLICY06", "Policy Desc", "DataExposure", "Gmail", "external", "admin@protectbeatle.com", "any", "any", "no", "no", "mayurbelekar@gmail.com", "any", "no", "no", "rtf",  "no",   "rtf", "0,0", "PCI",         "no", "moveToTrash" },
			new String[] { "GMPOLICY07", "Policy Desc", "DataExposure", "Gmail", "external", "admin@protectbeatle.com", "any", "any", "no", "no", "mayurbelekar@gmail.com", "any", "no", "no", "java", "no",   "no",  "0,0", "Source Code", "no", "moveToTrash" },
			new String[] { "GMPOLICY08", "Policy Desc", "DataExposure", "Gmail", "external", "admin@protectbeatle.com", "any", "any", "no", "no", "mayurbelekar@gmail.com", "any", "no", "no", "pdf",  "pdf",  "no",  "0,0", "HIPAA",       "no", "moveToTrash" },
			new String[] { "GMPOLICY09", "Policy Desc", "DataExposure", "Gmail", "external", "admin@protectbeatle.com", "any", "any", "no", "no", "mayurbelekar@gmail.com", "any", "no", "no", "cs",   "cs",   "no",  "0,0", "Source Code", "no", "moveToTrash" },
			new String[] { "GMPOLICY10", "Policy Desc", "DataExposure", "Gmail", "external", "admin@protectbeatle.com", "any", "any", "no", "no", "mayurbelekar@gmail.com", "any", "no", "no", "pdf",  "no",   "pdf", "0,0", "HIPAA",       "no", "moveToTrash" },
			new String[] { "GMPOLICY11", "Policy Desc", "DataExposure", "Gmail", "external", "admin@protectbeatle.com", "any", "any", "no", "no", "mayurbelekar@gmail.com", "any", "no", "no", "cs",   "no",   "cs",  "0,0", "Source Code", "no", "moveToTrash" },
			new String[] { "GMPOLICY12", "Policy Desc", "DataExposure", "Gmail", "external", "admin@protectbeatle.com", "any", "any", "no", "no", "mayurbelekar@gmail.com", "any", "no", "no", "bin",  "bin",  "no",  "0,0", "Encryption",  "no", "moveToTrash" },
			new String[] { "GMPOLICY13", "Policy Desc", "DataExposure", "Gmail", "external", "admin@protectbeatle.com", "any", "any", "no", "no", "mayurbelekar@gmail.com", "any", "no", "no", "xlsm", "xlsm", "no",  "0,0", "VBA Macros",  "no", "moveToTrash" },
			new String[] { "GMPOLICY14", "Policy Desc", "DataExposure", "Gmail", "external", "admin@protectbeatle.com", "any", "any", "no", "no", "mayurbelekar@gmail.com", "any", "no", "no", "xls",  "no",   "xls", "0,0", "Encryption",  "no", "moveToTrash" },
			new String[] { "GMPOLICY15", "Policy Desc", "DataExposure", "Gmail", "external", "admin@protectbeatle.com", "any", "any", "no", "no", "mayurbelekar@gmail.com", "any", "no", "no", "rar",  "rar",  "no",  "0,0", "Virus",       "no", "moveToTrash" },
			new String[] { "GMPOLICY16", "Policy Desc", "DataExposure", "Gmail", "external", "admin@protectbeatle.com", "any", "any", "no", "no", "mayurbelekar@gmail.com", "any", "no", "no", "rar",  "no",   "rar", "0,0", "Virus",       "no", "moveToTrash" },
			new String[] { "GMPOLICY17", "Policy Desc", "DataExposure", "Gmail", "external", "admin@protectbeatle.com", "any", "any", "no", "no", "mayurbelekar@gmail.com", "any", "no", "no", "pdf",  "no",   "pdf", "0,0", "PII",         "no", "moveToTrash" },
			new String[] { "GMPOLICY18", "Policy Desc", "DataExposure", "Gmail", "external", "admin@protectbeatle.com", "any", "any", "no", "no", "mayurbelekar@gmail.com", "any", "no", "no", "zip",  "no",   "zip", "0,0", "Virus",       "no", "moveToTrash" },
			new String[] { "GMPOLICY19", "Policy Desc", "DataExposure", "Gmail", "external", "admin@protectbeatle.com", "any", "any", "no", "no", "mayurbelekar@gmail.com", "any", "no", "no", "zip",  "zip",  "no",  "0,0", "Virus",       "no", "moveToTrash" },
			new String[] { "GMPOLICY20", "Policy Desc", "DataExposure", "Gmail", "external", "admin@protectbeatle.com", "any", "any", "no", "no", "mayurbelekar@gmail.com", "any", "no", "no", "js",  "js",    "no",  "0,0", "Source Code", "no", "moveToTrash" },
			new String[] { "GMPOLICY21", "Policy Desc", "DataExposure", "Gmail", "external", "admin@protectbeatle.com", "any", "any", "no", "no", "mayurbelekar@gmail.com", "any", "no", "no", "js",  "no",    "js",  "0,0", "Source Code", "no", "moveToTrash" },
			
			// File Size scenarios
			new String[] { "GMPOLICY22", "Policy Desc", "DataExposure", "Gmail", "external",  "admin@protectbeatle.com", "any", "any", "no", "no", "mayurbelekar@gmail.com",         "any", "no", "no", "zip",  "no",   "zip", "1048576,0", "Virus", "no", "moveToTrash" },
			new String[] { "GMPOLICY23", "Policy Desc", "DataExposure", "Gmail", "external",  "admin@protectbeatle.com", "any", "any", "no", "no", "mayurbelekar@gmail.com",         "any", "no", "no", "pdf",  "no",   "pdf", "0,1048576", "PII",   "no", "moveToTrash" },
			new String[] { "GMPOLICY24", "Policy Desc", "DataExposure", "Gmail", "unexposed", "admin@protectbeatle.com", "any", "any", "no", "no", "protectauto1@protectbeatle.com", "any", "no", "no", "zip",  "no",   "zip", "1048576,0", "Virus", "no", "moveToTrash" },
			new String[] { "GMPOLICY25", "Policy Desc", "DataExposure", "Gmail", "unexposed", "admin@protectbeatle.com", "any", "any", "no", "no", "protectauto1@protectbeatle.com", "any", "no", "no", "pdf",  "no",   "pdf", "0,1048576", "PII",   "no", "moveToTrash" },
			
			// Internal User
			new String[] { "GMPOLICY26", "Policy Desc", "DataExposure", "Gmail", "unexposed", "admin@protectbeatle.com", "any", "any", "no", "no", "protectauto1@protectbeatle.com", "any", "no", "no", "txt",  "no",   "txt", "0,0", "PII",         "no", "moveToTrash" },
			new String[] { "GMPOLICY27", "Policy Desc", "DataExposure", "Gmail", "unexposed", "admin@protectbeatle.com", "any", "any", "no", "no", "protectauto1@protectbeatle.com", "any", "no", "no", "txt",  "no",   "txt", "0,0", "PCI",         "no", "moveToTrash" },
			new String[] { "GMPOLICY28", "Policy Desc", "DataExposure", "Gmail", "unexposed", "admin@protectbeatle.com", "any", "any", "no", "no", "protectauto1@protectbeatle.com", "any", "no", "no", "txt",  "no",   "txt", "0,0", "GLBA",        "no", "moveToTrash" },
			new String[] { "GMPOLICY29", "Policy Desc", "DataExposure", "Gmail", "unexposed", "admin@protectbeatle.com", "any", "any", "no", "no", "protectauto1@protectbeatle.com", "any", "no", "no", "rtf",  "rtf",  "no",  "0,0", "PCI",         "no", "moveToTrash" },
			new String[] { "GMPOLICY30", "Policy Desc", "DataExposure", "Gmail", "unexposed", "admin@protectbeatle.com", "any", "any", "no", "no", "protectauto1@protectbeatle.com", "any", "no", "no", "rtf",  "no",   "rtf", "0,0", "PII",         "no", "moveToTrash" },
			new String[] { "GMPOLICY31", "Policy Desc", "DataExposure", "Gmail", "unexposed", "admin@protectbeatle.com", "any", "any", "no", "no", "protectauto1@protectbeatle.com", "any", "no", "no", "rtf",  "no",   "rtf", "0,0", "PCI",         "no", "moveToTrash" },
			new String[] { "GMPOLICY32", "Policy Desc", "DataExposure", "Gmail", "unexposed", "admin@protectbeatle.com", "any", "any", "no", "no", "protectauto1@protectbeatle.com", "any", "no", "no", "java", "no",   "no",  "0,0", "Source Code", "no", "moveToTrash" },
			new String[] { "GMPOLICY33", "Policy Desc", "DataExposure", "Gmail", "unexposed", "admin@protectbeatle.com", "any", "any", "no", "no", "protectauto1@protectbeatle.com", "any", "no", "no", "pdf",  "pdf",  "no",  "0,0", "HIPAA",       "no", "moveToTrash" },
			new String[] { "GMPOLICY34", "Policy Desc", "DataExposure", "Gmail", "unexposed", "admin@protectbeatle.com", "any", "any", "no", "no", "protectauto1@protectbeatle.com", "any", "no", "no", "cs",   "cs",   "no",  "0,0", "Source Code", "no", "moveToTrash" },
			new String[] { "GMPOLICY35", "Policy Desc", "DataExposure", "Gmail", "unexposed", "admin@protectbeatle.com", "any", "any", "no", "no", "protectauto1@protectbeatle.com", "any", "no", "no", "pdf",  "no",   "pdf", "0,0", "HIPAA",       "no", "moveToTrash" },
			new String[] { "GMPOLICY36", "Policy Desc", "DataExposure", "Gmail", "unexposed", "admin@protectbeatle.com", "any", "any", "no", "no", "protectauto1@protectbeatle.com", "any", "no", "no", "cs",   "no",   "cs",  "0,0", "Source Code", "no", "moveToTrash" },
			new String[] { "GMPOLICY37", "Policy Desc", "DataExposure", "Gmail", "unexposed", "admin@protectbeatle.com", "any", "any", "no", "no", "protectauto1@protectbeatle.com", "any", "no", "no", "bin",  "bin",  "no",  "0,0", "Encryption",  "no", "moveToTrash" },
			new String[] { "GMPOLICY38", "Policy Desc", "DataExposure", "Gmail", "unexposed", "admin@protectbeatle.com", "any", "any", "no", "no", "protectauto1@protectbeatle.com", "any", "no", "no", "xlsm", "xlsm", "no",  "0,0", "VBA Macros",  "no", "moveToTrash" },
			new String[] { "GMPOLICY39", "Policy Desc", "DataExposure", "Gmail", "unexposed", "admin@protectbeatle.com", "any", "any", "no", "no", "protectauto1@protectbeatle.com", "any", "no", "no", "xls",  "no",   "xls", "0,0", "Encryption",  "no", "moveToTrash" },
			new String[] { "GMPOLICY40", "Policy Desc", "DataExposure", "Gmail", "unexposed", "admin@protectbeatle.com", "any", "any", "no", "no", "protectauto1@protectbeatle.com", "any", "no", "no", "rar",  "rar",  "no",  "0,0", "Virus",       "no", "moveToTrash" },
			new String[] { "GMPOLICY41", "Policy Desc", "DataExposure", "Gmail", "unexposed", "admin@protectbeatle.com", "any", "any", "no", "no", "protectauto1@protectbeatle.com", "any", "no", "no", "rar",  "no",   "rar", "0,0", "Virus",       "no", "moveToTrash" },
			new String[] { "GMPOLICY42", "Policy Desc", "DataExposure", "Gmail", "unexposed", "admin@protectbeatle.com", "any", "any", "no", "no", "protectauto1@protectbeatle.com", "any", "no", "no", "pdf",  "no",   "pdf", "0,0", "PII",         "no", "moveToTrash" },
			new String[] { "GMPOLICY43", "Policy Desc", "DataExposure", "Gmail", "unexposed", "admin@protectbeatle.com", "any", "any", "no", "no", "protectauto1@protectbeatle.com", "any", "no", "no", "zip",  "no",   "zip", "0,0", "Virus",       "no", "moveToTrash" },
			new String[] { "GMPOLICY44", "Policy Desc", "DataExposure", "Gmail", "unexposed", "admin@protectbeatle.com", "any", "any", "no", "no", "protectauto1@protectbeatle.com", "any", "no", "no", "zip",  "zip",  "no",  "0,0", "Virus",       "no", "moveToTrash" },
			new String[] { "GMPOLICY45", "Policy Desc", "DataExposure", "Gmail", "unexposed", "admin@protectbeatle.com", "any", "any", "no", "no", "protectauto1@protectbeatle.com", "any", "no", "no", "js",  "js",    "no",  "0,0", "Source Code", "no", "moveToTrash" },
			new String[] { "GMPOLICY46", "Policy Desc", "DataExposure", "Gmail", "unexposed", "admin@protectbeatle.com", "any", "any", "no", "no", "protectauto1@protectbeatle.com", "any", "no", "no", "js",  "no",    "js",  "0,0", "Source Code", "no", "moveToTrash" },			
		};
	}
	
	@DataProvider(name = "GoogleDriveExposureDataProvider")
	public static Object[][] getGoogleDriveExposureCombinationTests() {
		return new Object[][] {
			new String[] { "GDSHAREDWITH1",     "Policy Desc", "reader", "Google Drive", "external",              "admin@protectbeatle.com", "any", "any", "no", "no", "mayurbelekar@gmail.com", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "no", "external", "yes" },
			new String[] { "GDSHAREDWITH2",     "Policy Desc", "reader", "Google Drive", "external",              "admin@protectbeatle.com", "any", "any", "no", "no", "any", "any", "mayurbelekar@gmail.com", "no", "txt", "no", "txt", "0,0", "no", "no", "no", "external", "no" },
			new String[] { "GDMULTIEXPOSURE3",  "Policy Desc", "reader", "Google Drive", "public,external,all",   "admin@protectbeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "no", "public,external", "yes" },
			new String[] { "GDMULTIEXPOSURE4",  "Policy Desc", "reader", "Google Drive", "public,external,all",   "admin@protectbeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "no", "external", "no" },
			new String[] { "GDMULTIEXPOSURE5",  "Policy Desc", "reader", "Google Drive", "public,external,all",   "admin@protectbeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "no", "public", "no" },
			new String[] { "GDMULTIEXPOSURE6",  "Policy Desc", "reader", "Google Drive", "external,internal,all", "admin@protectbeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "no", "internal,external", "yes" },
			new String[] { "GDMULTIEXPOSURE7",  "Policy Desc", "reader", "Google Drive", "external,internal,all", "admin@protectbeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "no", "extrenal,public", "no" },
			new String[] { "GDMULTIEXPOSURE8",  "Policy Desc", "reader", "Google Drive", "public,external,all",   "admin@protectbeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "no", "internal", "no" },
			new String[] { "GDMULTIEXPOSURE9",  "Policy Desc", "reader", "Google Drive", "public,external,all",   "admin@protectbeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "no", "external", "no" },
			new String[] { "GDMULTIEXPOSURE10", "Policy Desc", "reader", "Google Drive", "public,external,any",   "admin@protectbeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "no", "external", "yes" },
			new String[] { "GDMULTIEXPOSURE11", "Policy Desc", "reader", "Google Drive", "public,external,any",   "admin@protectbeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "no", "public", "yes" },
			new String[] { "GDMULTIEXPOSURE12", "Policy Desc", "reader", "Google Drive", "public,external,any",   "admin@protectbeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "no", "public,external", "yes" },
			new String[] { "GDMULTIEXPOSURE13", "Policy Desc", "reader", "Google Drive", "internal,external,any", "admin@protectbeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "no", "internal,external", "yes" },
			new String[] { "GDMULTIEXPOSURE14", "Policy Desc", "reader", "Google Drive", "internal,external,any", "admin@protectbeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "no", "external", "yes" },
			new String[] { "GDMULTIEXPOSURE15", "Policy Desc", "reader", "Google Drive", "internal,external,any", "admin@protectbeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "no", "internal", "yes" },
		};
	}
	
	@DataProvider(name = "GDrivePolicyViolation")
	public static Object[][] getPolicyViolationsInputData() {
		return new Object[][] {
				new String[] { "GDPVPOL01", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "rar", "no", "0,0", "Virus", "no", "no" },
				new String[] { "GDPVPOL02", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "html", "html", "no", "0,0", "no", "no", "no" },
				new String[] { "GDPVPOL03", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "exe", "exe", "no", "2097152,6291456", "no", "no", "no" },
				new String[] { "GDPVPOL04", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "txt", "no", "0,0", "PII", "no", "no" },
				new String[] { "GDPVPOL05", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "txt", "no", "0,0", "PCI", "no", "no" },
				new String[] { "GDPVPOL06", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "txt", "no", "0,0", "GLBA", "no", "no" },
				new String[] { "GDPVPOL07", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "zip", "zip", "no", "0,0", "Virus", "no", "no" },
				new String[] { "GDPVPOL08", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "bin", "bin", "no", "0,0", "Encryption", "no", "no" },
				new String[] { "GDPVPOL09", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "avi", "avi", "no", "0,0", "no", "no", "no" },
				new String[] { "GDPVPOL10", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "rar", "no", "0,0", "Virus", "no", "no" },
				new String[] { "GDPVPOL11", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "docx", "docx", "no", "0,0", "Source Code", "no", "no" },
				new String[] { "GDPVPOL12", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "cs", "cs", "no", "0,0", "Source Code", "no", "no" },
				new String[] { "GDPVPOL13", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "java", "java", "no", "0,0", "Source Code", "no", "no" },
				new String[] { "GDPVPOL14", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "cs", "cs", "no", "0,0", "HIPAA", "no", "no" },
				new String[] { "GDPVPOL15", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "jpg", "jpg", "no", "0,0", "no", "no", "no" },
				new String[] { "GDPVPOL16", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xls", "xls", "no", "0,0", "Encryption", "no", "no" },
				new String[] { "GDPVPOL17", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xlsm", "xlsm", "no", "0,0", "VBA Macros", "no", "no" },
				new String[] { "GDPVPOL18", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "js", "js", "no", "0,0", "Source Code", "no", "no" },				
				new String[] { "GDPVPOL19", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "mov", "mov", "no", "0,0", "no", "no", "no" },
				new String[] { "GDPVPOL20", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "mp4", "mp4", "no", "0,0", "no", "no", "no" },
				new String[] { "GDPVPOL21", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "mxf", "mxf", "no", "0,0", "no", "no", "no" },								
				new String[] { "GDPVPOL22", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "webm", "webm", "no", "0,0", "no", "no", "no" },				
				new String[] { "GDPVPOL23", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "pdf", "pdf", "no", "0,0", "PII", "no", "no" },
				new String[] { "GDPVPOL24", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "pdf", "pdf", "no", "0,0", "PCI", "no", "no" },
				new String[] { "GDPVPOL25", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "pdf", "pdf", "no", "0,0", "HIPAA", "no", "no" },				
				new String[] { "GDPVPOL26", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "rtf", "no", "0,0", "PII", "no", "no" },
				new String[] { "GDPVPOL27", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "rtf", "no", "0,0", "PCI", "no", "no" },
				new String[] { "GDPVPOL28", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "rtf", "no", "0,0", "GLBA", "no", "no" },
				new String[] { "GDPVPOL29", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "doc", "doc", "no", "0,0", "PII", "no", "no" },
				new String[] { "GDPVPOL30", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "doc", "doc", "no", "0,0", "PCI", "no", "no" },
				new String[] { "GDPVPOL31", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "doc", "doc", "no", "0,0", "GLBA", "no", "no" },
				new String[] { "GDPVPOL32", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "doc", "doc", "no", "0,0", "GLBA", "no", "no" },	

				new String[] { "GDPVPOL33", "Policy Desc", "reader", "Google Drive", "public,external,all",          "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PII", "no", "no", "external"},
				new String[] { "GDPVPOL34", "Policy Desc", "reader", "Google Drive", "public,internal,all",          "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PII", "no", "no", "public"},
				new String[] { "GDPVPOL35", "Policy Desc", "reader", "Google Drive", "internal,external,all",        "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PCI", "no", "no", "internal"},
				new String[] { "GDPVPOL36", "Policy Desc", "reader", "Google Drive", "external,internal,public,all", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PCI", "no", "no", "external"},
				new String[] { "GDPVPOL37", "Policy Desc", "reader", "Google Drive", "public,external,any",          "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PII", "no", "no", "external"},
				new String[] { "GDPVPOL38", "Policy Desc", "reader", "Google Drive", "public,external,any",          "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PCI", "no", "no", "public"},
				new String[] { "GDPVPOL39", "Policy Desc", "reader", "Google Drive", "internal,external,any",        "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "GLBA", "no", "no", "external"},
				new String[] { "GDPVPOL40", "Policy Desc", "reader", "Google Drive", "internal,external,any",        "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "GLBA", "no", "no", "internal"},
				new String[] { "GDPVPOL42", "Policy Desc", "reader", "Google Drive", "public,internal,any",          "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PII", "no", "no", "public"},
				new String[] { "GDPVPOL43", "Policy Desc", "reader", "Google Drive", "public,internal,any",          "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PCI", "no", "no", "internal"},
				new String[] { "GDPVPOL45", "Policy Desc", "reader", "Google Drive", "public,internal,external,any", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PII", "no", "no", "external"},
				new String[] { "GDPVPOL46", "Policy Desc", "reader", "Google Drive", "public,internal,external,any", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "GLBA", "no", "no", "internal"},
				new String[] { "GDPVPOL47", "Policy Desc", "reader", "Google Drive", "public,internal,external,any", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PCI", "no", "no", "public"},
				new String[] { "GDPVPOL48", "Policy Desc", "reader", "Google Drive", "public,external,any",          "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PII", "no", "no", "public"},
				
				new String[] { "GDPVPOL49", "Policy Desc", "reader", "Google Drive", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "txt", "no", "0,0", "PCI", "no", "no" },
				new String[] { "GDPVPOL50", "Policy Desc", "reader", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PII", "no", "no" },
				new String[] { "GDPVPOL51", "Policy Desc", "DataExposure", "Google Drive", "unexposed", "admin@protectbeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "pdf", "pdf", "no", "0,0", "PII", "no", "no" },
				new String[] { "GDPVPOL52", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "txt", "no", "0,0", "FERPA", "no", "no" },				
				new String[] { "GDPVPOL53", "Policy Desc", "reader", "Google Drive", "external", "any", "ProtectGDAutoGrp", "any", "no", "no", "any", "any", "no", "no", "doc", "doc", "no", "0,0", "PII", "no", "no" },
				new String[] { "GDPVPOL54", "Policy Desc", "reader", "Google Drive", "internal", "any", "ProtectGDAutoGrp", "any", "no", "no", "any", "any", "no", "no", "txt", "txt", "no", "0,0", "PCI", "no", "no" },
				
		};				
	}
	
	@DataProvider(name = "GDriveRemediation")
	public static Object[][] getGoogleDriveRemediationData() {
		return new Object[][] {

				new String[] { "GDCHANGEACCESS1", "Policy Desc", "reader", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PII", "no", "writer" },
				new String[] { "GDCHANGEACCESS2", "Policy Desc", "reader", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PCI", "no", "commenter" },
				new String[] { "GDCHANGEACCESS3", "Policy Desc", "writer", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "exe", "exe", "no", "0,0", "no", "no", "commenter" },
				new String[] { "GDCHANGEACCESS4", "Policy Desc", "writer", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "pdf", "no", "pdf", "0,0", "PII", "no", "reader" },
				new String[] { "GDCHANGEACCESS5", "Policy Desc", "reader", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PII", "no", "linkWriter" },
				new String[] { "GDCHANGEACCESS6", "Policy Desc", "reader", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PCI", "no", "linkReader" },
				new String[] { "GDCHANGEACCESS7", "Policy Desc", "reader", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "GLBA", "no", "linkCommenter" },
				new String[] { "GDCHANGEACCESS8", "Policy Desc", "reader", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PII", "no", "companyWriter" },
				new String[] { "GDCHANGEACCESS9", "Policy Desc", "writer", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PCI", "no", "companyReader" },
				new String[] { "GDCHANGEACCESS10", "Policy Desc", "writer", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "GLBA", "no", "companyCommenter" },
				new String[] { "GDCHANGEACCESS11", "Policy Desc", "writer", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PII", "no", "companyLinkWriter" },
				new String[] { "GDCHANGEACCESS12", "Policy Desc", "writer", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PCI", "no", "companyLinkReader" },
				new String[] { "GDCHANGEACCESS13", "Policy Desc", "writer", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "GLBA", "no", "companyLinkCommenter" },				
				new String[] { "GDREMOVELINK", "Policy Desc", "DataExposure", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PII", "no", "unshare" },
				new String[] { "GDPREVENTDOWNLOAD", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PCI", "no", "preventCopyPrintDownload" },
				new String[] { "GDPREVENTWRITER", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PII", "no", "preventWritersCanShare" },
				new String[] { "GDUPDATECOLLAB1", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PCI", "no", "updateWriter" },
				new String[] { "GDUPDATECOLLAB2", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "doc", "doc", "no", "0,0", "GLBA", "no", "updateReader" },
				new String[] { "GDUPDATECOLLAB3", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "pdf", "no", "pdf", "0,0", "HIPAA", "no", "updateCommenter" },
				new String[] { "GDREMOVECOLLAB", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PII", "no", "removeCollab" },
				// multi
/*				new String[] { "GDUPDATECOLLABSHAREACCESS", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PCI", "no", "writer,updateWriter" },
				new String[] { "GDREMOVECOLLABSHAREACCESS", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PCI", "no", "writer,removeCollab" },				
				new String[] { "GDUPDATECOLLABREMLINK", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PII", "no", "unshare,updateWriter" },*/
				new String[] { "GDREMOVECOLLABREMLINK", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PII", "no", "unshare,removeCollab" },
				new String[] { "GDUPDATECOLLABPREVENTDNLDCOPY", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PCI", "no", "preventCopyPrintDownload,updateWriter" },
				new String[] { "GDREMOVECOLLABPREVENTDNLDCOPY", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PCI", "no", "preventCopyPrintDownload,removeCollab" },
				new String[] { "GDUPDATECOLLABPREVENTWRITERSHARING", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PII", "no", "preventWritersCanShare,updateWriter" },
				new String[] { "GDREMOVECOLLABPREVENTWRITERSHARING", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PII", "no", "preventWritersCanShare,removeCollab" },
				
		};
	}
	
	@DataProvider(name = "MoveToTrashSanityDataProvider")
	public static Object[][] getGmailMoveToTrashSanityData() {
		return new Object[][] {
			// External User
			new String[] { "GmailPolicy", "Policy Desc", "DataExposure", "Gmail", "external", "admin@protectbeatle.com", "any", "any", "no", "no", "mayurbelekar@gmail.com", "any", "no", "no", "txt",  "no",   "txt", "0,0", "PII",         "no", "moveToTrash" },
		};
	}
	
}