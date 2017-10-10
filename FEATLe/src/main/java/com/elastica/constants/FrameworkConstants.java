package com.elastica.constants;

import java.io.File;

/**
 * Framework Constants
 * @author Eldo Rajan
 *
 */

public class FrameworkConstants {

	public static String ghostDriverPath = System.getProperty("user.dir") +File.separator+"src"+File.separator+"test"+File.separator+
			"resources"+File.separator+"lib"+File.separator+"phantomjs.exe";
	
	public static String CONFIG_MODE = "mode";
	public static String CONFIG_HUBURL = "huburl";
	public static String CONFIG_BASEURL = "baseurl";
	public static String CONFIG_BROWSER = "browser";
	public static String CONFIG_CHROMEDRIVER = "chromedriver";
	public static String CONFIG_IEDRIVER = "iedriver";
	public static String CONFIG_GHOSTDRIVER = "ghostdriver";
	public static String CONFIG_BMPFLAG = "bmpflag";
	public static String CONFIG_PROXYEXTENSION = "proxyExtension";
	public static String CONFIG_PROPERTIES = "config/config.properties";
	
	public static String PAGEOBJECTS_BASENAME = "com.elastica.pageobjects.";
	
	public static final String FRAMEWORK_CONFIGURATION_FILEPATH = "/src/test/resources/config/config.xml";
	public static final String FRAMEWORK_DBCREDENTIAL_FILEPATH = "/src/test/resources/config/CredentialDB.xml";																   

	
	public static String fireBugPath = System.getProperty("user.dir") +File.separator+"src"+File.separator+"test"+File.separator+
			"resources"+File.separator+"lib"+File.separator+"firebug-2.0.14b1.xpi";
	public static String netExportPath = System.getProperty("user.dir") +File.separator+"src"+File.separator+"test"+File.separator+
			"resources"+File.separator+"lib"+File.separator+"netExport-0.9b7.xpi";
	public static String fireStarterPath = System.getProperty("user.dir") +File.separator+"src"+File.separator+"test"+File.separator+
			"resources"+File.separator+"lib"+File.separator+"fireStarter-0.1a6.xpi";
	
	public static String accessTokenEoE="Neo8xdmy0DY3dqpmGvMiaiahKELzMBEFsgZX8wyk";
	public static String accessTokenQAVPC = "OBYcr7KXvXZD8fxAfchsLNuFoq43sdcmHrLJC6Lu";
	public static String accessTokenCHA = "EzTxmV5reXvpfmyULo45lsawAeFgT1UZ1uCxSrI8";
	public static String accessTokenOthers = "EzTxmV5reXvpfmyULo45lsawAeFgT1UZ1uCxSrI8";
	
}
