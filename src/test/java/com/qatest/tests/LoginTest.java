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
import com.qatest.base.BaseTest;
import com.qatest.pages.LoginPage;
import com.qatest.utils.ExcelUtil;
import com.qatest.utils.Listeners;
import com.qatest.utils.ScreenshotUtils;

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
	
	@DataProvider(name = "LoginData", parallel = true)
	public Object[][] getLoginData() {
	    // Logic inside ExcelUtil would package each row into a HashMap<String, String>
	    //return ExcelUtil.getTestDataAsMap(".\\test_data\\testdata_loginpage.xlsx", "td_loginpage", "run_flag", "Y"); 
		
		// System.getProperty("user.dir") returns the current project root path
        String projectPath = System.getProperty("user.dir");
        
        // Use File.separator to ensure it works on both Windows (\) and Linux/Mac (/)
        String excelPath = projectPath + File.separator + "test_data" +  
                           File.separator + "testdata_loginpage.xlsx";
        return ExcelUtil.getTestDataAsMap(excelPath, "td_loginpage", "run_flag", "Y"); 
	}

	@Test(dataProvider = "LoginData")
	public void loginTest(Map<String, String> data) {
	    // You only have one parameter, but you can access all data using Map:
	    //getDriver().get(data.get("URL"));
	    LoginPage loginPage = new LoginPage(getDriver());
	    //loginPage.doLogin(data.get("Username"), data.get("Password"));
	    //loginPage.login(data.get("username"), "");
	    loginPage.enterUsername(data.get("username"));
	    
	    String screenshotPath = ScreenshotUtils.captureScreenshot(getDriver(), data.get("username"));
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
		
		String screenshotPath = ScreenshotUtils.captureScreenshot(getDriver(), "test0");
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
