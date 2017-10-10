package com.elastica.asserts;

import java.util.List;

import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestResult;
import org.testng.Reporter;

import com.elastica.logger.Logger;

/**
 * Soft Assert Listener
 * @author Eldo Rajan
 *
 */
public class SoftAssertListener implements IInvokedMethodListener{

	public void afterInvocation(IInvokedMethod arg0, ITestResult arg1) {
		// TODO Auto-generated method stub
		
	}

	public void beforeInvocation(IInvokedMethod method, ITestResult result) {
		// TODO Auto-generated method stub
		Reporter.setCurrentTestResult(result);
		if (method.isTestMethod()) {
			List<String> verificationFailures = SoftAsserter.getVerificationFailures();
			int size = verificationFailures.size();
			if ( size > 0) {
				result.setStatus(ITestResult.FAILURE);
				result.setAttribute("ErrorMsg ", verificationFailures.toString());
				Logger.info(verificationFailures.toString());
				if (result.getThrowable() != null) {
					verificationFailures.add(result.getThrowable().getMessage());
				}
				StringBuffer failureMsg = new StringBuffer();
				for(int i=0;i<size;i++) {
					failureMsg.append(verificationFailures.get(i)).append("\n");
				}
				Throwable merged = new Throwable(failureMsg.toString());
				result.setThrowable(merged);
			}
		}
	}

}
