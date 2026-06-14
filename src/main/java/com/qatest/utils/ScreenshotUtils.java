/**
 * 
 */
package com.qatest.utils;

import org.apache.commons.io.FileUtils;

/**
 * @author Divyang
 *
 */

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//import java.text.SimpleDateFormat;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.Date;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.SessionId;

public class ScreenshotUtils {

    //public static String captureScreenshot(WebDriver driver, String screenshotName) {
	public static String captureScreenshot(WebDriver driver) {
        try {
        	//String timestamp = new SimpleDateFormat("yyyyMmmrdd_HHMMSS").format(new Date());
        	
            File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            //Capture current WebDriver session id
            SessionId session = ((RemoteWebDriver) driver).getSessionId();
            String currentSessionID = session.toString();
            
            //String timestamp = DateTimeUtil.getCurrentTimestamp(); //You can also use in-built function System.currentTimeMillis()
            String timestamp = DateTimeUtil.getCurrentTimestampInUTC();
            //String destPath = "target/screenshots/" + screenshotName + ".png";
            //String destPath = System.getProperty("user.dir") + "/test_results/screenshots/" + screenshotName + "_" + timestamp + ".png";
            //String destPath = System.getProperty("user.dir") + "/test_results/screenshots/" + "Screenshot_" + timestamp + "_" + screenshotName + ".jpeg";
         
            // System.getProperty("user.dir") returns the current project root path
            String projectPath = System.getProperty("user.dir");
            
            // Use File.separator to ensure it works on both Windows (\) and Linux/Mac (/)
            String destPath = projectPath + File.separator + "test_results" + 
            					File.separator + "extent-reports" +
            					File.separator + "screenshots" + 
            					//File.separator + "Screenshot_" + currentSessionID + "_" + timestamp + "_" + screenshotName + ".jpeg";
        						File.separator + "Screenshot_" + currentSessionID + "_" + timestamp + ".jpeg";
            
            //Files.createDirectories(Paths.get("target/screenshots/"));
            //Files.copy(srcFile.toPath(), Paths.get(destPath));
            FileUtils.copyFile(srcFile, new File(destPath));
            return destPath;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
