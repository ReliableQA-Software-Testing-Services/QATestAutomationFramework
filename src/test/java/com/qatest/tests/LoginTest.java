/**
 * 
 */
package com.qatest.tests;

import java.io.File;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.qatest.base.BaseTest;
import com.qatest.pages.LoginPage;
import com.qatest.utils.ExcelUtil;
import com.qatest.utils.Listeners;
import com.qatest.utils.LoggingUtil;
import com.qatest.utils.RetryAnalyzer;
import com.qatest.utils.ScreenshotUtils;
import com.qatest.utils.TestContextManager;

/**
 * @author Divyang
 *
 */
public class LoginTest extends BaseTest {

	/**
	 * 
	 */
	public LoginTest() {
		// TODO Auto-generated constructor stub
	}
	
	/*@DataProvider(name = "LoginData")
    public Object[][] getData() {
        return ExcelUtil.getTestData("./src/test/resources/testdata/testdata_loginpage.xlsx", "td_loginpage", "run_flag", "Y");
    }

	@Test(dataProvider = "LoginData")
	public void loginTest(String user, String pwd) {
	    // Correctly passes the thread-specific driver to the Page Object
	    LoginPage loginPage = new LoginPage(getDriver()); 
	    loginPage.login(user, pwd);
	}*/
	
	@DataProvider(name = "LoginData", parallel = false)
	public Object[][] getLoginData() {
		/* When you use @DataProvider(parallel = true), TestNG automatically spins up 
		 * distinct execution threads and instantiates a unique, independent ITestResult object 
		 * for every single data iteration. */
		
	    // Logic inside ExcelUtil would package each row into a HashMap<String, String>
	    //return ExcelUtil.getTestDataAsMap(".\\test_data\\testdata_loginpage.xlsx", "td_loginpage", "run_flag", "Y");
		
        // Use File.separator to ensure it works on both Windows (\) and Linux/Mac (/)
        String excelPath = TESTDATA_PARENT_DIR_PATH +  
                           File.separator + "testdata_loginpage.xlsx";
        ExcelUtil login_dataset = new ExcelUtil();
        return login_dataset.getTestDataAsMap(excelPath, "td_loginpage", "run_flag", "Y"); 
	}

	//@Test(dataProvider = "LoginData", retryAnalyzer = RetryAnalyzer.class)
	@Test(dataProvider = "LoginData")
	public void loginTest(Map<String, String> data) {
	    // You only have one parameter, but you can access all data using Map:
	    //getDriver().get(data.get("URL"));
		System.out.println("Executing Thread ID: " + Thread.currentThread().getId() + 
                " | Testing with User: " + data.get("username"));
		String uniqueTestName = (String) TestContextManager.getAttribute("uniqueTestName");
		System.out.println("Started Test: " + uniqueTestName);
    	Listeners.getTest().log(Status.INFO, "Started Test: " + uniqueTestName);
    	
    	// Automatically dump the entire Excel row HashMap as a clean table inside the Extent Report
        @SuppressWarnings("unchecked")
        //Map<String, String> dataMap = (Map<String, String>) result.getAttribute("dataMap");
        Map<String, String> dataMap = (Map<String, String>) TestContextManager.getAttribute("dataMap");
        if (dataMap != null) {
        	/*Listeners.getTest().log(Status.INFO, "<b>Test Data Used:</b><br>" + 
        					MarkupHelper.createTable(LoggingUtil.convertMapTo2DArray(dataMap)));*/
        	Listeners.getTest().log(Status.INFO, "<b>Test Data Used:</b><br>" + 
        					LoggingUtil.logDataMap(dataMap));
        }
	    
        LoginPage loginPage = new LoginPage(getDriver());
	    //loginPage.doLogin(data.get("Username"), data.get("Password"));
	    //loginPage.login(data.get("username"), "");
	    loginPage.enterUsername(data.get("username"));
	    
	    //String screenshotPath = ScreenshotUtils.captureScreenshot(getDriver(), data.get("username"));
	    String screenshotPath = ScreenshotUtils.captureScreenshot(getDriver());
	    Listeners.getTest().log(Status.INFO, "User: " + data.get("username"), 
				MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
	    
	    loginPage.enterPassword("");
	    loginPage.clickLogin();
	    
	    String actualTitle = getDriver().getTitle();
        //String expectedTitle = "GitHub";

        // Hard Assert: Test will stop here if assertion fails
        Assert.assertEquals(actualTitle, data.get("expected_pagetitle"), "Page title does not match!");
	}
	
	@Test
	public void print_msg() {
		System.out.println("Hi, Divyang!");
		
		//String screenshotPath = ScreenshotUtils.captureScreenshot(getDriver(), "test0");
		String screenshotPath = ScreenshotUtils.captureScreenshot(getDriver());
		//Listeners.getTest().addScreenCaptureFromPath(screenshotPath);
		
		//Fail intentionally
		Assert.assertEquals("a", "b", "a is not b");
		
		//Below lines won't be executed as the above assertion failed !
		Listeners.getTest().log(Status.INFO, "Hi, Divyang!", 
				MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
		//Listeners.getTest().info("Hi, Divyang!", 
		//		MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
	}
}
