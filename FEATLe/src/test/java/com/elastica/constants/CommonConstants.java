	package com.elastica.constants;

import java.io.File;


/**
 * Box Constants
 * @author Eldo Rajan
 *
 */
public class CommonConstants  {

	public static String downloadDir=System.getProperty("user.home")+File.separator+"Downloads";
	public static String logMessage = "";
	public static final int TRY_COUNT = 3;
	public static final int TRY_COUNT_EXT = 5;
	public static final int PAGE_LOAD_TIME = 10;
	public static final String FILE_UPLOAD_FIREFOX_EXE = System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+"resources"+File.separator+"utils"+File.separator+"FileUploadFirefox.exe";
	public static final String FILE_UPLOAD_FIREFOX_SCPT = System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+"resources"+File.separator+"utils"+File.separator+"FileUploadFirefox.scpt";
	public static final String FILE_UPLOAD_CHROME_EXE = System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+"resources"+File.separator+"utils"+File.separator+"FileUploadChrome.exe";
	public static final String FILE_UPLOAD_CHROME_SCPT = System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+"resources"+File.separator+"utils"+File.separator+"FileUploadChrome.scpt";
	
	public static final String AWS_SECRET_KEY = "AKIAJOCYNMSFMKCXIZIQ";
	public static final String AWS_SECRET_TOKEN = "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";
}