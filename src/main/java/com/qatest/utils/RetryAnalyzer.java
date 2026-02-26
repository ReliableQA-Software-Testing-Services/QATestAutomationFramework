/**
 * 
 */
package com.qatest.utils;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * @author Divyang
 *In a data-driven setup, if Row #5 fails due to a TimeoutException, you don't want to fail the whole build.
 *This TestNG listener will automatically re-run only that specific data row which failed.
 */
	
public class RetryAnalyzer implements IRetryAnalyzer {
	    private int count = 0;
	    private static final int MAX_RETRY = 2; // Retry twice

	    @Override
	    public boolean retry(ITestResult result) {
	        if (!result.isSuccess() && count < MAX_RETRY) {
	            count++;
	            return true; // Tells TestNG to run the test again
	        }
	        return false;
	    }
}
