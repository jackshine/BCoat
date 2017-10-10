package com.elastica.asserts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.Reporter;

/**
 * Soft Assert Class
 * @author Eldo Rajan
 *
 */
public class SoftAsserter {

	private static Map<ITestResult,List<String>> verificationFailuresMap = new HashMap<ITestResult,List<String>>();
	
	public static void assertTrue(boolean condition, String errMsg) {
    	try {
    		Assert.assertTrue(condition);
    	} catch(Throwable e) {
    		addVerificationFailure(errMsg + " Exception msg: "+e.getMessage());
    	}
    }
	
	public static void assertFalse(boolean condition, String errMsg)  {
    	try{
    		Assert.assertFalse(condition);
    	}catch(Throwable e) {
    		addVerificationFailure(errMsg + " Exception msg: "+e.getMessage());
    	}
    }
 
    public static void assertEquals(Object actual, Object expected, String errMsg)  {
    	try {
    		Assert.assertEquals(actual, expected);
		} catch(Throwable e) {
    		addVerificationFailure(errMsg + " Exception msg: "+e.getMessage());
		}
    }
    
    public static void assertEquals(Object actual, Object expected)  {
    	try {
    		Assert.assertEquals(actual, expected);
		} catch(Throwable e) {
    		addVerificationFailure(" Exception msg: "+e.getMessage());
		}
    }
    
    public static void assertTrue(boolean condition) {
    	try {
    		Assert.assertTrue(condition);
    	} catch(Throwable e) {
    		addVerificationFailure(" Exception msg: "+e.getMessage());
    	}
    }
    
    public static void assertFalse(boolean condition)  {
    	try {
    		Assert.assertFalse(condition);
    	} catch(Throwable e) {
    		addVerificationFailure(" Exception msg: "+e.getMessage());
    	}
    }
    
    public static void assertNotNull(Object actual)  {
    	try {
    		Assert.assertNotNull(actual);
		} catch(Throwable e) {
    		addVerificationFailure(" Exception msg: "+e.getMessage());
		}
    }
 
    public static void assertNotNull(Object actual, String errMsg)  {
    	try {
    		Assert.assertNotNull(actual, errMsg);
		} catch(Throwable e) {
    		addVerificationFailure(errMsg+" Exception msg: "+e.getMessage());
		}
    }
 
	public static List<String> getVerificationFailures() {
		List<String> verificationFailures = verificationFailuresMap.get(Reporter.getCurrentTestResult());
		return verificationFailures == null ? new ArrayList<String>() : verificationFailures;
	}
	
	private static void addVerificationFailure(String e) {
		List<String> verificationFailures = getVerificationFailures();
		verificationFailures.add(e);
		verificationFailuresMap.put(Reporter.getCurrentTestResult(), verificationFailures);
	}
}
