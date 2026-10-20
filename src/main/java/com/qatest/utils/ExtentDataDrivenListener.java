package com.qatest.utils;
//package com.framework.listeners;

import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestResult;
import java.util.Map;

public class ExtentDataDrivenListener implements IInvokedMethodListener {

    @Override
    public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
        if (method.isTestMethod()) {
            Object[] parameters = testResult.getParameters();
            if (parameters.length > 0 && parameters[0] instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, String> dataMap = (Map<String, String>) parameters[0];
                
                // Create a clean readable string out of the Map for the Test Title
                // e.g., verifyLoginWithMap [Username=standard_user, Role=Admin]
                String uniqueTestName = testResult.getMethod().getMethodName() + "_" + dataMap.toString();
                
                //testResult.setAttribute("uniqueTestName", uniqueTestName);
                TestContextManager.setAttribute("uniqueTestName", uniqueTestName);
                //testResult.setAttribute("dataMap", dataMap); // Pass map downstream to Report Listener if needed
                TestContextManager.setAttribute("dataMap", dataMap);
            } else {
                //testResult.setAttribute("uniqueTestName", testResult.getMethod().getMethodName());
                TestContextManager.setAttribute("uniqueTestName", testResult.getMethod().getMethodName());
            }
            //System.out.println("Test Name beforeInvocation in ExtentDataDrivernListner: " + TestContextManager.getAttribute("uniqueTestName"));
            //System.out.println("Test Data beforeInvocation in ExtentDataDrivernListner: " + TestContextManager.getAttribute("dataMap"));
        }
    }

    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
    	// Clean up thread-local storage after test completion to prevent memory leaks (moved this code to tearDownTest)
        /*if (method.isTestMethod()) {
        	//System.out.println("Test Name afterInvocation in ExtentDataDrivernListner: " + TestContextManager.getAttribute("uniqueTestName"));
            //System.out.println("Test Data afterInvocation in ExtentDataDrivernListner: " + TestContextManager.getAttribute("dataMap"));
            TestContextManager.clear();
        }*/
    }
}