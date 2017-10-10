package com.elastica.beatle;

import java.util.Arrays;

import org.testng.ITestResult;

public class TestUtil {
	
	public static int getId(ITestResult result) {
        int id = result.getTestClass().getName().hashCode();
        id = id + result.getMethod().getMethodName().hashCode();
        id = id + (result.getParameters() != null ? Arrays.hashCode(result.getParameters()) : 0);
        return id;
    }

}
