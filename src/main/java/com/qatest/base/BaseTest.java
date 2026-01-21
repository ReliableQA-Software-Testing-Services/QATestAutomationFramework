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
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
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
    
    private ConfigReader cr;
    //protected Properties prop;
    public static Properties prop;
    
    //Default constructor
    protected BaseTest(){
    	// TODO 
    }

    @BeforeSuite
    public void setupSuite() {
    	// Load config properties file (contains browser type, URL, etc.)
    	cr = new ConfigReader();
        prop = cr.init_prop();
    }
    
    /*@BeforeTest
    public void setUpTest() throws IOException {
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
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.get(prop.getProperty("url"));
        
        getDriver().manage().window().maximize();
        getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        getDriver().get(urlText);
        System.out.println("Browser: " + browserName + " launched with URL: " + urlText + " in Headless:" + headless + " mode");
    }

    @AfterTest
    public void tearDownTest() {
        if (driver != null) {
            driver.quit();
        }
    	if (getDriver() != null) {
        getDriver().quit();
        tldriver.remove(); // Clean up the thread
    	}
    }
*/    
    @AfterSuite
    public void tearDownSuite() {
        //Below is just for user information
        System.out.println("Please refer to " + System.getProperty("user.dir") + "\\test_results\\extent-reports directory for the individual test results.");
        System.out.println("Please refer to " + System.getProperty("user.dir") + "\\target\\surefire-reports directory for the latest surefire report.");
        System.out.println("In case of errors, please refer to dump files (if any exist) [date].dump, [date]-jvmRun[N].dump and [date].dumpstream.");
    }
}