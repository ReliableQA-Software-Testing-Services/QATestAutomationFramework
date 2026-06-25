/**
 * 
 */
package com.qatest.tests;

import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;
import com.qatest.base.BaseTest;
import com.qatest.utils.Listeners;

/**
 * @author Divyang
 *
 */
public class GridDemo extends BaseTest {

	/**
	 * 
	 */
	public GridDemo() {
		// TODO Auto-generated constructor stub
	}
	
	//@Parameters({"browser"})
	@Test
	//public void gridDemo(String browser) throws MalformedURLException, InterruptedException {
	//public void gridDemo() throws InterruptedException, Exception {
	public void gridDemo() throws Exception {
		/*DesiredCapabilities caps = new DesiredCapabilities();
        if (browser.equalsIgnoreCase("chrome")) {
            caps.setBrowserName("chrome");
        } else if (browser.equalsIgnoreCase("firefox")) {
            caps.setBrowserName("firefox");
        }*/
        //Listeners.getTest().log(Status.INFO, "Browser: " + browser);
        
        //WebDriver remote_driver = new RemoteWebDriver(new URL("http://localhost:4445/wd/hub"), caps);
		try {
			WebDriver remote_driver = getDriver();
			// If the driver didn't initialize in @BeforeMethod, crash immediately and loudly
	    	/*if (remote_driver == null) {
	        	Assert.fail("Driver initialization failed in configuration. Cannot proceed with test execution.");
	    	}*/
	        remote_driver.navigate().to("https://www.google.com");
	        String actualTitle = remote_driver.getTitle();
	        String expectedTitle = "Google";

	        // Hard Assert: Test will stop here if assertion fails
	        Assert.assertEquals(actualTitle, expectedTitle, "Page title does not match!");
	        remote_driver.findElement(By.name("q")).sendKeys("Learn Automation using Selenium Grid");
	        
	        Thread.sleep(5000);
	        /*remote_driver.close();
	        remote_driver.quit();*/
		} catch (Exception e) {
			e.printStackTrace();
			//Assert.fail("Driver initialization failed in configuration. Cannot proceed with test execution.");
			throw e;
		}
        
	}

}
