/**
 * 
 */
package com.qatest.utils;

import java.io.File;
import java.io.IOException;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import java.util.TimeZone;

/**
 * @author Divyang
 *
 */
public class ExtentReportsManager {
	private static ExtentReports extent;
	
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
            /*sparkReporter.config().setReportName("QA Automation Results");
            sparkReporter.config().setDocumentTitle("Test Execution Report");*/
            // Use an extent-reports configuration XML to format the HTML test results(e.g. timestamp format)
            String extentConfigFilePath = projectPath + File.separator + "src" + 
            		File.separator + "test" +
            		File.separator + "resources" +
            		File.separator + "extent-reports-config.xml";
            try {
            	sparkReporter.loadXMLConfig(extentConfigFilePath);
            } catch (IOException e) {
            	e.printStackTrace();
            }
                        
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
            extent.setSystemInfo("Target Timezone", TimeZone.getDefault().getID());//Dynamically logs the JVM timezone
            extent.setSystemInfo("User Name", System.getProperty("user.name"));
            extent.setSystemInfo("OS", System.getProperty("os.name"));
            extent.setSystemInfo("Java Version", System.getProperty("java.version"));
        }
        return extent;
    }
    
    public static void setSystemInfoDynamically(String property_name, String property_value) {
    	if(Listeners.extent != null) {
			Listeners.extent.setSystemInfo(property_name, property_value);
		}
    }

}
