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
            //String timestamp = DateTimeUtil.getCurrentTimestamp();
            String timestamp = DateTimeUtil.getCurrentTimestampInUTC();
            
            // Use File.separator to ensure it works on both Windows (\) and Linux/Mac (/)
            String sparkReportFilePath = projectPath + File.separator + "test_results" + 
            		File.separator + "extent-reports" +           
            		File.separator + "TestReport_" + timestamp + ".html";
            //String path = System.getProperty("user.dir") + File.separator + "reports" + File.separator + "index.html";
            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(sparkReportFilePath);
            sparkReporter.config().setReportName("QA Automation Results");
            sparkReporter.config().setDocumentTitle("Test Execution Report");
                        
            /*String emailReportFilePath = projectPath + File.separator + "test_results" + 
    		File.separator + "extent-reports" +           
    		File.separator + "EmailableTestReport_" + timestamp + ".html";
    		
            ExtentEmailReporter emailReporter = new ExtentEmailReporter(emailReportFilePath);*/

            extent = new ExtentReports();
            extent.attachReporter(sparkReporter);
            /*You can attach multiple reporters and output the same information to each report
            extent.attachReporter(sparkReporter, emailReporter);*/
            extent.setSystemInfo("Tester", "QA Engineer");
            extent.setSystemInfo("Environment", "QA");
            
        }
        return extent;
    }
}
