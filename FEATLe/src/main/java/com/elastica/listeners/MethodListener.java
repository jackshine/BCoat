package com.elastica.listeners;


import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.elastica.logger.Logger;

/**
 * Prints helper messages for all methods and logs them in logger file
 * @author Eldo Rajan
 *
 */
public class MethodListener implements ITestListener,IInvokedMethodListener {

	
	public void onFinish(ITestContext method) {
		// TODO Auto-generated method stub
	}

	
	public void onStart(ITestContext suite) {
		Logger.info("**************************************************************************");
		Logger.info("Started the test suite: " + suite.getName());
	}

	
	public void onTestFailedButWithinSuccessPercentage(ITestResult method) {
		// TODO Auto-generated method stub
	}

	
	public void onTestFailure(ITestResult method) {
		Logger.info("Failed the test case:" + method.getMethod().getMethodName());
	}

	
	public void onTestSkipped(ITestResult method) {
		Logger.info("Skipped the test case:" + method.getMethod().getMethodName());
	}

	
	public void onTestStart(ITestResult method) {
		//Logger.info("**********************Started the test case:" + method.getMethod().getMethodName()+ " **********************");
	}

	
	public void onTestSuccess(ITestResult method) {
		Logger.info("Completed the test case: " + method.getMethod().getMethodName());
		Logger.info("*********************************************************************************************");
	}


	@Override
	public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
		if(method.isTestMethod()){
			Logger.info("Started the test case:" + method.getTestMethod().getMethodName());
		}
	}

	@Override
	public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
	}
}
