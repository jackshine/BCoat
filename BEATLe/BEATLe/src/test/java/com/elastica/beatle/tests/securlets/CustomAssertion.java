package com.elastica.beatle.tests.securlets;

import org.testng.Assert;
import org.testng.Reporter;

public class CustomAssertion {

    
    // CustomAssertion Methods

	public static void assertEquals(final boolean actual, final boolean expected, final String message) {
		Reporter.log("Actual value:" + actual + "," + "Expected value:"+ expected, true);
		Assert.assertEquals(actual, expected, message);
	}


	public static void assertEquals(final byte actual, final byte expected, final String message) {
		Reporter.log("Actual value:" + actual + "," + "Expected value:"+ expected, true);
		Assert.assertEquals(actual, expected, message);
	}
	
	public static void assertEquals(final int actual, final byte expected, final int message) {
		Reporter.log("Actual value:" + actual + "," + "Expected value:"+ expected, true);
		Assert.assertEquals(actual, expected, message);
	}
	
	public static void assertEquals(final long actual, final long expected, final String message) {
		Reporter.log("Actual value:" + actual + "," + "Expected value:"+ expected, true);
		Assert.assertEquals(actual, expected, message);
	}
	
	public static void assertEquals(final String actual, final String expected, final String message) {
		Reporter.log("Actual value:" + actual + "," + "Expected value:"+ expected, true);
		Assert.assertEquals(actual, expected, message);
	}
	
	public static void assertNull(final String actual, final String message) {
		Reporter.log("Actual value:" + actual + "," + "Expected value: null", true);
		Assert.assertNull(actual, message);
	}
	
	public static void assertTrue(boolean evaluated, String message) {
		if(!evaluated) {
			Reporter.log(message + " is not present", true);
		}
		Assert.assertTrue(evaluated, message);
		Reporter.log("Actual value:" + message , true);
	}
	
	public static void assertTrue(boolean evaluated, String expected, String unexpected) {
		Reporter.log("Expected Value: " + expected, true);
		if(!evaluated) {
			Reporter.log("Actual Value: " + unexpected, true);
		} else {
			Reporter.log("Actual Value: " + expected, true);
		}
		Assert.assertTrue(evaluated, unexpected);
	}
	
	
	public static void assertFalse(boolean evaluated, String message) {
		Reporter.log("Evaluated Value:" + evaluated, true);
		Assert.assertFalse(evaluated, message);
	}
}
