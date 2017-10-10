package com.elastica.beatle;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;
import org.testng.Reporter;
 
public class RetryAnalyzer implements IRetryAnalyzer  { 
private int count = 0; 
private int maxCount = 2; // set your count to re-run test
 
    @Override
    public boolean retry(ITestResult result) { 
    	
            if(count < maxCount) {  
            	count++;
                return true; 
            } 
            return false; 
    }
}