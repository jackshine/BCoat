package com.elastica.beatle.securlets;

import org.testng.Reporter;

public class LogUtils {
	public static void logTestDescription(String message) {
		Reporter.log("***************************************** Test Description ***************************************** ", true);
		Reporter.log(message, true);
		Reporter.log("**************************************************************************************************** ", true);
	}
	
	public static void logTestDescription(String[] messages) {
		Reporter.log("***************************************** Test Description ***************************************** ", true);
		for(String message : messages) {
			Reporter.log(message, true);
		}
		Reporter.log("**************************************************************************************************** ", true);
	}
	
	public static void logStep(String message) {
		Reporter.log(message, true);
		Reporter.log("**************************************************************************************************** ", true);
	}
}
