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
	
	@Parameters({"browser"})
	@Test
	public void gridDemo(String browser) throws MalformedURLException, InterruptedException {
		DesiredCapabilities caps = new DesiredCapabilities();
        if (browser.equalsIgnoreCase("chrome")) {
            caps.setBrowserName("chrome");
        } else if (browser.equalsIgnoreCase("firefox")) {
            caps.setBrowserName("firefox");
        }
        Listeners.getTest().log(Status.INFO, "Browser: " + browser);
        
        WebDriver remote_driver = new RemoteWebDriver(new URL("http://localhost:4445/wd/hub"), caps);
        remote_driver.get("https://www.google.com");
        remote_driver.findElement(By.name("q")).sendKeys("Learn Automation using Selenium Grid");
        
        Thread.sleep(5000);
        remote_driver.close();
        remote_driver.quit();
	}

}
