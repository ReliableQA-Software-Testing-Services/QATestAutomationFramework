/**
 * 
 */
package com.qatest.utils;

import java.io.File;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

/**
 * @author Divyang
 *
 */
public class ExtentReportsManager {
	public static ExtentReports extent;

    public static ExtentReports getInstance() {
        if (extent == null) {
        	// System.getProperty("user.dir") returns the current project root path
            String projectPath = System.getProperty("user.dir");
            String timestamp = DateTimeUtil.getCurrentTimestamp();
            
            // Use File.separator to ensure it works on both Windows (\) and Linux/Mac (/)
            String reportFilePath = projectPath + File.separator + "test_results" + 
            		File.separator + "extent-reports" +           
            		File.separator + "TestReport_" + timestamp + ".html";
            //String path = System.getProperty("user.dir") + File.separator + "reports" + File.separator + "index.html";
            ExtentSparkReporter reporter = new ExtentSparkReporter(reportFilePath);
            reporter.config().setReportName("QA Automation Results");
            reporter.config().setDocumentTitle("Test Execution Report");

            extent = new ExtentReports();
            extent.attachReporter(reporter);
            extent.setSystemInfo("Tester", "QA Engineer");
            extent.setSystemInfo("Environment", "QA");
        }
        return extent;
    }
}
