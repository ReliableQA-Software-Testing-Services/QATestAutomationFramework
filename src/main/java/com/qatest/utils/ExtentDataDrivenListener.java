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
                String uniqueTestName = testResult.getMethod().getMethodName() + " " + dataMap.toString();
                
                testResult.setAttribute("uniqueTestName", uniqueTestName);
                testResult.setAttribute("dataMap", dataMap); // Pass map downstream to Report Listener if needed
            } else {
                testResult.setAttribute("uniqueTestName", testResult.getMethod().getMethodName());
            }
        }
    }

    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
        // Optional
    }
}