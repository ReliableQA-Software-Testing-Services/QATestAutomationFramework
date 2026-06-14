/**
 * 
 */
package com.qatest.utils;

import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.qatest.base.BaseTest;

/**
 * @author Divyang
 *
 */
//public class Listeners extends BaseTest implements ITestListener {
public class Listeners implements ITestListener {
	private static ExtentReports extent = ExtentReportsManager.getInstance();
    //ExtentTest test;
	// ThreadLocal creates a unique instance of ExtentTest for every thread
    private static ThreadLocal<ExtentTest> extentTest = new ThreadLocal<ExtentTest>();
    
    // Static method to allow any class to log to the report
    public static ExtentTest getTest() {
        return extentTest.get();
    }
    
    @Override
    public void onStart(ITestContext context) {
    	// Get the suite name in the testng xml file
        String suiteName = context.getSuite().getName();
        String suiteTestName =context.getCurrentXmlTest().getName();
        //Below is just for user information
        //System.out.println("TestNG TestSuite: " + suiteName);
        System.out.println("Started a test in TestNG TestSuite: " + suiteName + " --> " + suiteTestName);
    }
    
    @Override
    public void onTestStart(ITestResult result) {
    	ExtentTest test = extent.createTest(result.getMethod().getMethodName());
    	// Assigning the suite name as a category for easy filtering
        test.assignCategory(result.getTestContext().getSuite().getName());
    	extentTest.set(test); // Store the test object for the current thread
    	
    	System.out.println("Started Test: " + result.getMethod().getMethodName());
    	getTest().log(Status.INFO, "Started Test: " + result.getMethod().getMethodName());
    	
    	// Check for DataProvider parameters
        Object[] params = result.getParameters();
        for (Object param : params) {
            if (param instanceof Map) {
                LoggingUtil.logDataMap((Map<String, String>) param);
            }
        }
        
        String browsr = result.getTestContext().getCurrentXmlTest().getParameter("browser");
        //defaulting to chrome browser
        if (browsr == null || browsr.isEmpty()) browsr = "chrome";
        
        String browsr_version = result.getTestContext().getCurrentXmlTest().getParameter("browser_version");
        //defaulting to the latest version of the browser
        if (browsr_version == null || browsr_version.isEmpty()) browsr_version = "latest";
        
        //print Application URL, Browser type, Headless mode, Selenium Grid Run flag in the report
    	getTest().log(Status.INFO, "Browser: " + browsr + 
    			", " + "Browser Version: " + browsr_version + 
    			", " + "URL: " + BaseTest.prop.getProperty("url") + 
    			", " + "Headless Mode:" + BaseTest.prop.getProperty("headless") + 
    			", " + "Selenium Grid Run:" + BaseTest.prop.getProperty("grid_run"));
    }
    
    @Override
    public void onTestFailure(ITestResult result) {
        System.out.println("Test failed: " + result.getName());
        //getTest().log(Status.FAIL, markup);
        //getTest().fail(result.getThrowable());
        getTest().log(Status.FAIL, "Test failed: " + result.getName());
        getTest().log(Status.FAIL, result.getThrowable());
        
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
        	String htmlTooltipForScreenshot = "<span style='"
    								+ "position: relative; "
    								+ "cursor: pointer; "
    								+ "color: red; "
    								+ "font-weight: bold;' "
    								+ "title='File Path: " + screenshotPath.replace("\\", "\\\\") + "'>"
    								+ "Hover over here to see the file path "
    								+ "</span>";
	        
	        //getTest().addScreenCaptureFromPath(screenshotPath);
	        //getTest().fail("Test Failed - Screenshot attached:", 
	        //        MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
	        getTest().log(Status.FAIL, "<b><font color=red>Screenshot of failure: Click on it to open</font></b>", 
	                MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath, htmlTooltipForScreenshot).build());
        } catch (Exception e) {
        	getTest().log(Status.INFO, "Failed to capture screenshot: " + e.getMessage());
        }
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        System.out.println("Test passed: " + result.getName());
        getTest().log(Status.PASS, "Test passed: " + result.getName());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        System.out.println("Skipped Test: " + result.getMethod().getMethodName());
        getTest().log(Status.SKIP, "Skipped Test: " + result.getMethod().getMethodName());
        getTest().log(Status.SKIP, result.getThrowable());
    }
    
    @Override
    public void onFinish(ITestContext context) {
        extent.flush(); // This is mandatory to generate the report file
        
        String suiteName = context.getSuite().getName();
        String suiteTestName =context.getCurrentXmlTest().getName();
        //Below is just for user information
        System.out.println("Finished a test in TestNG TestSuite: " + suiteName + " --> " + suiteTestName);
        
        //Below is just for user information
        /*System.out.println("Please refer to " + System.getProperty("user.dir") + "\\test_results\\extent-reports directory for the individual test results.");
        System.out.println("Please refer to " + System.getProperty("user.dir") + "\\target\\surefire-reports directory for the latest surefire report.");
        System.out.println("In case of errors, please refer to dump files (if any exist) [date].dump, [date]-jvmRun[N].dump and [date].dumpstream.");*/
    }
}