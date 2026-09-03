/**
 * 
 */
package com.qatest.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.qatest.base.BaseTest;

/**
 * @author Divyang
 *
 */
public class LoggingUtil {

	/**
	 * 
	 */
	public LoggingUtil() {
		// TODO Auto-generated constructor stub
	}
	//a method that converts the Map into a clean, formatted HTML table to be displayed inside the Extent Report
	public static void logDataMap(Map<String, String> data) {
	    StringBuilder table = new StringBuilder();
	    table.append("<table style='width:100%; border: 1px solid black; border-collapse: collapse;'>");
	    //table.append("<tr style='background-color: #f2f2f2;'><th style='border: 1px solid black;'>Column</th><th style='border: 1px solid black;'>Value</th></tr>");
	    //removing background-color for dark mode
	    table.append("<tr><th style='border: 1px solid black;'>Column</th><th style='border: 1px solid black;'>Value</th></tr>");
	    
	    for (Map.Entry<String, String> entry : data.entrySet()) {
	        table.append("<tr>");
	        table.append("<td style='border: 1px solid black; padding: 5px;'>" + entry.getKey() + "</td>");
	        table.append("<td style='border: 1px solid black; padding: 5px;'>" + entry.getValue() + "</td>");
	        table.append("</tr>");
	    }
	    table.append("</table>");
	    
	    // Log the HTML table to the Extent Report
	    Listeners.getTest().log(Status.INFO, "<b>Test Data Used:</b><br>" + table.toString());
	}
	
	// Helper utility to transform Map into Extent table format
	public static String[][] convertMapTo2DArray(Map<String, String> map) {
        String[][] tableData = new String[map.size()][2];
        int i = 0;
        for (Map.Entry<String, String> entry : map.entrySet()) {
            tableData[i][0] = entry.getKey();
            tableData[i][1] = entry.getValue();
            i++;
        }
        return tableData;
    }
	
	// Method to parse and append Browser & Client runtime logs into Extent Report markdown code blocks
    public static void attachWebLogsToExtent(WebDriver driver, ExtentTest test) {
        String[] logTypes = { LogType.BROWSER, LogType.CLIENT };
        
        for (String logType : logTypes) {
            StringBuilder logBuilder = new StringBuilder();
            LogEntries entries = driver.manage().logs().get(logType);
            
            for (LogEntry entry : entries) {
                logBuilder.append("[").append(entry.getLevel()).append("] ")
                          .append(entry.getMessage()).append("\n");
            }
            
            if (logBuilder.length() > 0) {
                // Formatting inside Extent Report using standard HTML <pre> or Markdown markup code blocks
                test.log(Status.INFO, "<b>" + logType.toUpperCase() + " LOGS:</b><br><pre>" + logBuilder.toString() + "</pre>");
            }
        }
    }

    // Method to extract the raw Driver/Server text file from disk if the driver crashes on launch
    public static void attachDriverCrashLogsToExtent(String logPath, ExtentTest test) {
        try {
            File logFile = new File(logPath);
            if (logFile.exists()) {
                String content = new String(Files.readAllBytes(Paths.get(logPath)));
                // Truncate if logs are too long for the HTML report UI
                if (content.length() > 10000) {
                    content = content.substring(content.length() - 10000) + "\n...[Truncated due to size]...";
                }
                test.log(Status.WARNING, "<b>DRIVER VERBOSE CRASH LOGS:</b><br><pre>" + content + "</pre>");
            }
        } catch (IOException e) {
            test.log(Status.INFO, "Failed to read crash log file: " + e.getMessage());
        }
    }

    public static void attachLogFiles(ITestResult result, ExtentTest test) {
        String driverLogSrc = BaseTest.prop.getProperty("driver.logfile.path");
        String browserLogSrc = BaseTest.prop.getProperty("browserClient.logfile.path");

        if (driverLogSrc != null) {
            attachFileToReport(driverLogSrc, result.getMethod().getMethodName() + "_driver.log", "Driver Log", test);
        }
        if (browserLogSrc != null) {
            attachFileToReport(browserLogSrc, result.getMethod().getMethodName() + "_browser.log", "Browser Log", test);
        }
    }

    private static void attachFileToReport(String sourcePath, String targetFileName, String logLabel, ExtentTest test) {
        File source = new File(sourcePath);
        if (!source.exists()) return;

        // Relative path used inside the Extent HTML report page
        String relativeReportPath = "attachments/" + targetFileName;
        File destination = new File("test_results/extent-reports/" + relativeReportPath);

        try {
            // Copy file to the reporting root directory for distribution stability
            Files.copy(source.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
            
            // Inject an HTML download link directly into the Extent Report log view row
            String htmlAttachmentLink = String.format(
                "<a href='%s' target='_blank' style='color:#15c; font-weight:bold;'>📎 Download %s (%s)</a>",
                relativeReportPath, logLabel, targetFileName
            );
            
            test.log(Status.INFO, htmlAttachmentLink);
        } catch (IOException e) {
            test.log(Status.INFO, "Failed to attach file " + logLabel + ": " + e.getMessage());
        }
    }

    public static void saveAndPrintWebLogs(WebDriver driver, String filePath) {
        try (PrintWriter fileWriter = new PrintWriter(new FileOutputStream(filePath))) {
            
            //String[] logTypes = { LogType.BROWSER, LogType.CLIENT };
            String[] logTypes = { LogType.BROWSER };
            
            for (String logType : logTypes) {
                String header = "\n===== " + logType.toUpperCase() + " LOGS =====";
                
                // Write headers to both places
                //System.out.println(header);
                fileWriter.println(header);

                LogEntries entries = driver.manage().logs().get(logType);
                for (LogEntry entry : entries) {
                    String logLine = "[" + entry.getTimestamp() + "] " + "[" + entry.getLevel() + "] " + entry.getMessage();
                    
                    //System.out.println(logLine);   // Print out to Maven Terminal Console
                    fileWriter.println(logLine);   // Write out to output text file
                }
            }
        } catch (IOException e) {
            System.err.println("Could not save browser/client logs: " + e.getMessage());
        }
    }

    public static void saveAndPrintDriverLogs(WebDriver driver, String filePath) {
        try (PrintWriter fileWriter = new PrintWriter(new FileOutputStream(filePath, true))) {
            
            //String[] logTypes = { LogType.DRIVER, LogType.SERVER };
            String[] logTypes = { LogType.DRIVER };
            //String[] logTypes = { LogType.DRIVER, LogType.PERFORMANCE };
            
            for (String logType : logTypes) {
                String header = "\n===== " + logType.toUpperCase() + " LOGS =====";
                
                // Write headers to both places
                //System.out.println(header);
                fileWriter.println(header);

                LogEntries entries = driver.manage().logs().get(logType);
                for (LogEntry entry : entries) {
                    String logLine = "[" + entry.getTimestamp() + "] " + "[" + entry.getLevel() + "] " + entry.getMessage();
                    
                    //System.out.println(logLine);   // Print out to Maven Terminal Console
                    fileWriter.println(logLine);   // Write out to output text file
                }
            }
        } catch (IOException e) {
            System.err.println("Could not save driver/server logs: " + e.getMessage());
        }
    }
}