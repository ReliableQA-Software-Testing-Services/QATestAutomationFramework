/**
 * 
 */
package com.qatest.base;

import java.io.IOException;

import java.time.Duration;

import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;

import io.github.bonigarcia.wdm.WebDriverManager;

/**
 * @author Divyang
 *
 */
public class BaseTest {
    //protected WebDriver driver; //This is for non-parallel execution
	//ThreadLocal creates a separate driver instance for every thread, which is needed for parallel execution
    private static ThreadLocal<WebDriver> tldriver = new ThreadLocal<>();

    public static synchronized WebDriver getDriver() {
        return tldriver.get();
    }
    
    //protected Properties prop;
    public static Properties prop;
    ConfigReader cr;

    @BeforeMethod
    public void setUp() throws IOException {
        // Load config properties file (contains browser type, URL, etc.)
    	cr = new ConfigReader();
        prop = cr.init_prop();

        String browserName = prop.getProperty("browser");
        String  urlText = prop.getProperty("url");
        WebDriver driver = null;
        
        // Read the property set by Maven Profiles in pom.xml
        //String headless = System.getProperty("headlessMode");
        // Read the property from config properties file
        String headless = prop.getProperty("headless"); 

        if (browserName.equalsIgnoreCase("chrome")) {
        	ChromeOptions options = new ChromeOptions();
            
            if (headless.equalsIgnoreCase("true")) {
                options.addArguments("--headless=new");
                options.addArguments("--window-size=1920,1080");
            }
            
            WebDriverManager.chromedriver().setup();
            driver = new ChromeDriver(options);
            //tldriver.set(driver); // Set the driver for the current thread
        } else if (browserName.equalsIgnoreCase("firefox")) {
        	FirefoxOptions options = new FirefoxOptions();
            
            if (headless.equalsIgnoreCase("true")) {
                options.addArguments("--headless=new");
                options.addArguments("--window-size=1920,1080");
            }
            
            WebDriverManager.firefoxdriver().setup();
            driver = new FirefoxDriver(options);
            //tldriver.set(driver); // Set the driver for the current thread
        }
        
        tldriver.set(driver); // Set the driver for the current thread
        /*driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.get(prop.getProperty("url"));*/
        
        getDriver().manage().window().maximize();
        getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        getDriver().get(urlText);
        System.out.println("Browser: " + browserName + " launched with URL: " + urlText + " in Headless:" + headless + " mode");
    }

    @AfterMethod
    public void tearDown() {
        /*if (driver != null) {
            driver.quit();
        }*/
    	if (getDriver() != null) {
        getDriver().quit();
        tldriver.remove(); // Clean up the thread
    	}
    }
}