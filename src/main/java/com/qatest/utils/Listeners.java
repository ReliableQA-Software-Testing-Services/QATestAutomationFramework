/**
 * 
 */
package com.qatest.utils;

import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.testng.IConfigurationListener;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
//import org.testng.SkipException;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.qatest.base.BaseTest;

/**
 * @author Divyang
 *
 */
//public class Listeners extends BaseTest implements ITestListener {
public class Listeners implements ISuiteListener, ITestListener, IConfigurationListener {
	public static ExtentReports extent = ExtentReportsManager.getInstance();
    //ExtentTest test;
	// ThreadLocal creates a unique instance of ExtentTest for every thread
    private static ThreadLocal<ExtentTest> extentTest = new ThreadLocal<ExtentTest>();
    
    // Static method to allow any class to log to the report
    public static ExtentTest getTest() {
        return extentTest.get();
    }
    
    @Override
    public void onStart(ISuite suite) {
    	String msgOnSuiteStart = "Started TestSuite: " + suite.getName();
    	System.out.println(msgOnSuiteStart);
    	//getTest().log(Status.INFO, msgOnSuiteStart);        
    }

    @Override
    public void onStart(ITestContext context) {
    	// Get the suite name in the testng xml file
        String suiteName = context.getSuite().getName();
        String suiteTestName =context.getCurrentXmlTest().getName();
        //Below is just for user information
        //System.out.println("TestNG TestSuite: " + suiteName);
        System.out.println("Started a test in TestNG TestSuite: " + suiteName + " --> " + suiteTestName);
        //getTest().log(Status.INFO, "Started a test in TestNG TestSuite: " + suiteName + " --> " + suiteTestName);
    }
    
    @Override
    public void onTestStart(ITestResult result) {
    	// Retrieve the unique name generated from the data iteration
        String uniqueTestName = (String) result.getAttribute("uniqueTestName");
        if (uniqueTestName == null) {
            uniqueTestName = result.getMethod().getMethodName();
        }
        
    	String browserName = (String) result.getTestContext().getAttribute("browserName");
        String browserVersion = (String) result.getTestContext().getAttribute("browserVersion");

        if (BaseTest.getDriver() != null && browserName != null && browserVersion != null) {
        	ExtentTest test = extent.createTest(uniqueTestName)
                    .assignCategory(browserName) // categorize by browser
                    .assignDevice(browserVersion); // show version per test

        	//ExtentTest test = extent.createTest(result.getMethod().getMethodName());
        	
        	// Assigning the suite name as a category for easy filtering in the test reports
            //test.assignCategory(result.getTestContext().getSuite().getName());
            // Assigning browser name as a category for easy filtering in the test reports
        	//String browser_name = result.getTestContext().getCurrentXmlTest().getParameter("browser");
        	//defaulting to chrome browser and the latest version of the browser
        	//if (browser_name == null || browser_name.isEmpty()) browser_name = "chrome";
            //test.assignCategory(browser_name);
            
        	extentTest.set(test); // Store the test object for the current thread
        	
        	System.out.println("Started Test: " + result.getMethod().getMethodName());
        	getTest().log(Status.INFO, "Started Test: " + result.getMethod().getMethodName());
        	
        	// Print the DataProvider parameters inside the Extent Report
        	/*Object[] params = result.getParameters();
            for (Object param : params) {
                if (param instanceof Map) {
                	LoggingUtil.logDataMap((Map<String, String>) param);
                }
            }*/
            
            // Automatically dump the entire Excel row HashMap as a clean table inside the Extent Report
            @SuppressWarnings("unchecked")
            Map<String, String> dataMap = (Map<String, String>) result.getAttribute("dataMap");
            if (dataMap != null) {
            	getTest().log(Status.INFO, "<b>Test Data Used:</b><br>" + 
            					MarkupHelper.createTable(LoggingUtil.convertMapTo2DArray(dataMap)));
            }
            
            /*String browsr = result.getTestContext().getCurrentXmlTest().getParameter("browser");
            //defaulting to chrome browser
            if (browsr == null || browsr.isEmpty()) browsr = "chrome";
            
            String browsr_version = result.getTestContext().getCurrentXmlTest().getParameter("browser_version");
            //defaulting to the latest version of the browser
            if (browsr_version == null || browsr_version.isEmpty()) browsr_version = "latest";*/
                    
            //print Application URL, Browser type, Headless mode, Selenium Grid Run flag in the report
        	getTest().log(Status.INFO, "<b>Test Environment: </b>" + System.getProperty("test.env") 
        			+ ", " + "<b>Browser: </b>" + browserName 
        			+ ", " + "<b>Browser Version: </b> " + browserVersion 
        			+ ", " + "<b>URL: </b>" + BaseTest.prop.getProperty("url") 
        			+ ", " + "<b>Headless Mode: </b>" + BaseTest.prop.getProperty("headless") 
        			+ ", " + "<b>Selenium Grid Run: </b>" + BaseTest.prop.getProperty("grid_run"));
        }
    }
    
    @Override
    public void onTestFailure(ITestResult result) {
    	String errorMessage = handleNullPtrException(result, result.getName() + " has been failed.");
        
        // Report the reason for failure in ExtentReports
        //getTest().log(Status.FAIL, errorMessage);
        getTest().log(Status.FAIL, "TEST FAILED: " 
        		+ result.getMethod().getMethodName() 
        		+ "----->" + errorMessage);
    	
        System.out.println("Test failed: " + result.getName());
        //getTest().log(Status.FAIL, markup);
        //getTest().fail(result.getThrowable());
        getTest().log(Status.FAIL, "Test failed: " + result.getName());
        getTest().log(Status.FAIL, result.getThrowable()); //This is required to be logged for Bugs tab in Extent HTML Reports
        
        // Use the driver instance from the result to capture the screenshot
        // This ensures you get the screenshot from the correct thread in parallel runs
        /*try {
             WebDriver driver = (WebDriver) result.getTestClass().getRealClass().getField("driver").get(result.getInstance());
        } catch (Exception e) {
        	System.out.println("Hi Divyang ! There is somethign wrong in this code");
            e.printStackTrace();
        }*/
        
        /*try {
	        Object currentClass = result.getInstance(); 
	        WebDriver driver = ((com.qatest.base.BaseTest) currentClass).getDriver(); 
	        String screenshotPath = ScreenshotUtils.captureScreenshot(driver, result.getMethod().getMethodName());*/
        
        try {
        	//String screenshotPath = ScreenshotUtils.captureScreenshot(getDriver(), result.getMethod().getMethodName());
        	WebDriver driver = BaseTest.getDriver();
        	//String screenshotPath = ScreenshotUtils.captureScreenshot(driver, result.getMethod().getMethodName());
        	String screenshotPath = ScreenshotUtils.captureScreenshot(driver);
        	/*String htmlTooltipForScreenshot = "<span style='"
    								+ "position: relative; "
    								+ "cursor: pointer; "
    								+ "color: red; "
    								+ "font-weight: bold;' "
    								+ "title='File Path: " + screenshotPath.replace("\\", "\\\\") + "'>"
    								+ "Hover over here to see the filepath"
    								+ "</span>";*/
	        
	        //getTest().addScreenCaptureFromPath(screenshotPath);
	        //getTest().fail("Test Failed - Screenshot attached:", 
	        //        MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
	        /*getTest().log(Status.FAIL, "<b><font color=red>Screenshot of failure: Click on it to open</font></b>", 
	                MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath, htmlTooltipForScreenshot).build());*/
	        getTest().log(Status.FAIL, "<b><font color=red>Screenshot of failure: Click on it to open</font></b>", 
	                MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
        } catch (Exception e) {
        	getTest().log(Status.INFO, "Failed to capture screenshot: " + e.getMessage());
        }
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        System.out.println("Test passed: " + result.getName());
        getTest().log(Status.PASS, "Test passed: " + result.getName());
    }

    /*@Override
    public void onTestSkipped(ITestResult result) {
    	String errorMessage = handleNullPtrException(result, result.getMethod().getMethodName() + " has been skipped.");
        
        // Force the status to FAIL in ExtentReports
        getTest().log(Status.FAIL, "CRITICAL SKIP (Test marked as Fail): " 
        		+ result.getMethod().getMethodName() 
        		+ "----->" + errorMessage);
        System.out.println("Skipped Test: " + result.getMethod().getMethodName());
        getTest().log(Status.FAIL, "Skipped Test: " + result.getMethod().getMethodName());
        //getTest().log(Status.SKIP, result.getThrowable());
        getTest().log(Status.FAIL, result.getThrowable()); //This is required to be logged for Bugs tab in Extent HTML Reports
    }*/
    
    @Override
    public void onTestSkipped(ITestResult result) {
    	if (result.getAttribute("skippedDueToConfigFailure") != null) {
    		// Force the status to FAIL in ExtentReports
    		System.out.println("Configuration/Setup failure. Failed Test: " + result.getMethod().getMethodName());
            getTest().log(Status.FAIL, "Configuration/Setup failure. Failed Test: " + result.getMethod().getMethodName());
    	} else {
    		System.out.println("Test skipped: " + result.getName());
            getTest().log(Status.SKIP, "Test skipped: " + result.getName());
    	}
    }
    
    @Override
    public void onFinish(ITestContext context) {
        //extent.flush(); // This is mandatory to generate the report file
        
        String suiteName = context.getSuite().getName();
        String suiteTestName =context.getCurrentXmlTest().getName();
        //Below is just for user information
        System.out.println("Finished a test in TestNG TestSuite: " + suiteName + " --> " + suiteTestName);
        
        //Below is just for user information
        /*System.out.println("Please refer to " + System.getProperty("user.dir") + "\\test_results\\extent-reports directory for the individual test results.");
        System.out.println("Please refer to " + System.getProperty("user.dir") + "\\target\\surefire-reports directory for the latest surefire report.");
        System.out.println("In case of errors, please refer to dump files (if any exist) [date].dump, [date]-jvmRun[N].dump and [date].dumpstream.");*/
    }
    
    @Override
    public void onFinish(ISuite suite) {
    	String msgOnSuiteFinish = "Finished TestSuite: " + suite.getName();
    	System.out.println(msgOnSuiteFinish);
    	//getTest().log(Status.INFO, msgOnSuiteFinish);
    }
    
    @Override
    public void onConfigurationFailure(ITestResult itr) {
//        Throwable throwable = itr.getThrowable();
//        
//        // Check if the failure is due to Selenium Grid session creation
//        if (throwable != null && throwable.getMessage().contains("SessionNotCreatedException")) {
//            System.err.println("ALERT: Selenium Grid failed to create a session for: " + itr.getMethod().getMethodName());
//        }
//
//        // 1. Log to Extent Reports so it doesn't show a blank/passed report
//        if (getTest() != null) {
//            //ExtentManager.getTest().fail("Configuration Failed: " + throwable.getMessage());
//            //ExtentManager.getTest().fail(throwable);
//            getTest().log(Status.FAIL, "Configuration Failed on Selenium Grid: " + throwable.getMessage());
//            getTest().log(Status.FAIL, throwable);
//        } else {
//            // If the test instance wasn't even initialized in Extent yet:
//            ExtentTest test = extent.createTest(itr.getMethod().getMethodName());
//            extentTest.set(test); // Store the test object for the current thread
//        	
//        	getTest().log(Status.FAIL, "Setup failed on Selenium Grid: " + throwable.getMessage());
//        	getTest().log(Status.FAIL, throwable);
//        }
    	
    	// A configuration method (like @BeforeMethod / @BeforeClass) has failed.
        // Mark this in the test result attributes so downstream handlers know.
        itr.setAttribute("skippedDueToConfigFailure", true);
    	String errorMessage = handleNullPtrException(itr, itr.getMethod().getMethodName() + ": Setup failed due to Configuration/Grid failure !");
        
        // Force the status to FAIL in ExtentReports
        getTest().log(Status.FAIL, "Test Setup failed due to Configuration/Grid failure for: " 
        		+ itr.getMethod().getMethodName() 
        		+ "----->" + errorMessage);
        getTest().log(Status.FAIL, itr.getThrowable()); //This is required to be logged for Bugs tab in Extent HTML Reports
        // Note: Maven Surefire report automatically picks up IConfigurationListener failures 
        // and marks the test suite as failed/skipped with the stack trace.
    }

    public String handleNullPtrException(ITestResult result, String errorMessage) {
    	// IF THE TEST WAS SKIPPED BECAUSE OF A GRID CONFIGURATION FAILURE:
        // TestNG sets the status to SKIP, but we force ExtentReports to show FAIL.
        if (getTest() == null) {
            // If onTestStart didn't create an ExtentTest test object yet because @BeforeMethod failed early
        	ExtentTest test = extent.createTest(result.getMethod().getMethodName());
            extentTest.set(test); // Store the test object for the current thread
        }
        
        //String errorMessage = "Skipped due to Configuration/Grid failure.";
        
        // Check if the TestNG throwable framework actually has a message
        if (result.getThrowable() != null) {
            String rawMessage = result.getThrowable().getMessage();
            if (rawMessage != null) {
                errorMessage = rawMessage; 
                // If you have a custom replaceAll logic here, secure it like this:
                // errorMessage = errorMessage.replaceAll("somePattern", "replacement");
            } else {
                errorMessage = result.getThrowable().toString(); // Fallback if getMessage() is null
            }
        }
        return errorMessage;
    }   
}