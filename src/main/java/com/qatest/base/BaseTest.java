/**
 * 
 */
package com.qatest.base;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

import java.util.Properties;
import java.util.TimeZone;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
//import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterGroups;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import com.qatest.utils.ExtentReportsManager;
//import org.testng.xml.XmlTest;

//import com.aventstack.extentreports.Status;
//import com.qatest.utils.Listeners;

//import io.github.bonigarcia.wdm.WebDriverManager; //Selenium Manager(in-built with Selenium 4 onwards) eliminates the need for WebDriverManager

/**
 * @author Divyang
 *
 */
public class BaseTest {
	static {
		//set the default JVM timezone to UTC(or "America/New_York", "Asia/Kolkata", "Japan/Tokyo", etc) to adopt it across all global test runners
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
	}
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
/*
    @BeforeSuite
    public void beforeSuite() {
        System.out.println("Initialize DB connection, Reporting and/or Selenium Grid Setup for entire suite");
    }

    @AfterSuite
    public void afterSuite() {
        System.out.println("Close DB connection, flush the reporting or shutdown the Grid after suite execution");
    }
    
    @BeforeTest
    public void beforeTest() {
        System.out.println("Setup an environment(staging/prod) or select a browser before a <test> block in TestNG XML");
    }

    @AfterTest
    public void afterTest() {
        System.out.println("Teardown an environment(staging/prod) after a <test> block in TestNG XML");
    }
    
    @BeforeGroups({"ui", "api"})
    public void beforeUIGroup() {
        System.out.println("Select UI/API/Performance test resources");
    }

    @AfterGroups("ui")
    public void afterUIGroup() {
        System.out.println("Release UI/API/Performance test resources");
    }
    
    @BeforeClass
    public void beforeClass() {
        System.out.println("Initialize Page Objects or load test data before class tests");
    }

    @AfterClass
    public void afterClass() {
        System.out.println("Release resources after class tests");
    }
    
    @BeforeMethod
    public void beforeMethod() {
        System.out.println("Reset test data before each method");
    }

    @AfterMethod
    public void afterMethod() {
        System.out.println("Log results after each method");
    }
*/
    @BeforeSuite
	@Parameters({"env"})
    public void setUpSuite(@Optional("qa") String envName) {
    	// Load config properties file (contains URL, Headless Mode, Grid Run Flag, etc.)
    	cr = new ConfigReader();
        prop = cr.init_prop();

		// Print test environment in the dashboard of extent-reports
        ExtentReportsManager.setSystemInfoDynamically("Parallel Run Environment", envName);
    }
    
    @BeforeMethod
    @Parameters({"browser", "browser_version"})
    public void setUpTest(@Optional("chrome") String browser, 
    					  @Optional("latest") String browser_version, 
    					  ITestContext context) throws IOException, 
    														MalformedURLException, 
    														InterruptedException {
    	// Load config properties file (contains browser type, URL, etc.)
    	/*cr = new ConfigReader();
        prop = cr.init_prop();*/
    	
    	//defaulting to chrome browser and the latest version of the browser
    	if (browser == null || browser.isEmpty()) browser = "chrome";
    	if (browser_version == null || browser_version.isEmpty()) browser_version = "latest";
    	
    	// Print browser name in the dashboard of extent-reports
        //ExtentReportsManager.setSystemInfoDynamically("Browser", browser);
        
        //String browserName = prop.getProperty("browser");
        String  urlText = prop.getProperty("url");
        String gridRunFlag = prop.getProperty("grid_run");
        WebDriver driver = null;
        
        // Read the property set by Maven Profiles in pom.xml
        //String headless = System.getProperty("headlessMode");
        // Read the property from config properties file
        String headless = prop.getProperty("headless"); 

        /*if (browser.equalsIgnoreCase("chrome")) {
        
        } else if (browser.equalsIgnoreCase("firefox")) {
        	
        } else {
            throw new IllegalArgumentException("Unsupported browser: " + browser);
        }*/
        
        switch(browser.toUpperCase()) {
        	
        	case "FIREFOX":
        		FirefoxOptions firefox_options = new FirefoxOptions();
                
                if (headless.equalsIgnoreCase("true")) {
                	firefox_options.addArguments("-headless");
                    //firefox_options.addArguments("-window-size=1920,1080");
                }
                
                //WebDriverManager.firefoxdriver().setup();//setup the latest browser version, unless specified
                
                //For running Selenium Grid
                if (gridRunFlag.equalsIgnoreCase("true")) {
                	driver = new RemoteWebDriver(new URL(prop.getProperty("grid_hub_url")), firefox_options);
                	
                	//With WebDriverManager in Selenium Grid Nodes
                	/*driver = WebDriverManager.firefoxdriver()
                				.browserVersion(browser_version) //Specify major version (e.g. "120" or "121.0.6167.85")
                				.capabilities(firefox_options)
                				.remoteAddress(new URL(prop.getProperty("grid_hub_url")))
                				.create();*/
                } else {//local run
                	//WebDriverManager.firefoxdriver().setup();
                	//WebDriverManager.firefoxdriver().browserVersion(browser_version).setup();
                	driver = new FirefoxDriver(firefox_options);
                }
                break;
        	case "EDGE":
        		EdgeOptions edge_options = new EdgeOptions();
        		edge_options.addArguments("--start-maximized");
        		
        		if (headless.equalsIgnoreCase("true")) {
        			edge_options.addArguments("--headless=new");
        			edge_options.addArguments("--window-size=1920,1080");
                }
        		
        		//For running Selenium Grid
                if (gridRunFlag.equalsIgnoreCase("true")) {
                	driver = new RemoteWebDriver(new URL(prop.getProperty("grid_hub_url")), edge_options);
                	//With WebDriverManager in Selenium Grid Nodes
                	/*driver = WebDriverManager.edgedriver()
                				.browserVersion(browser_version) //Specify major version (e.g. "120" or "121.0.6167.85")
                				.capabilities(edge_options)
                				.remoteAddress(new URL(prop.getProperty("grid_hub_url")))
                				.create();*/
                } else {//local run
                	//WebDriverManager.edgedriver().setup();
                	//WebDriverManager.edgedriver().browserVersion(browser_version).setup();
                	driver = new EdgeDriver(edge_options);
                }
                break;
        	default:
        		ChromeOptions options = new ChromeOptions();
            	//options.addArguments("--incognito");
                options.addArguments("--start-maximized");
                //options.addArguments("--disable-gpu");
            	
            	//options.setCapability("os", "Windows");
            	//options.setCapability("os_version", "11");
            	//options.setCapability("browser_version", "latest");


                if (headless.equalsIgnoreCase("true")) {
                    options.addArguments("--headless=new");
                    options.addArguments("--window-size=1920,1080");
                }
                
                //WebDriverManager.chromedriver().browserVersion("83.0.4103").setup();//setup a specific browser version  
                //WebDriverManager.chromedriver().driverVersion("85.0.4183.38").setup();//setup a specific driver version
                //WebDriverManager.chromedriver().setup();//setup the latest browser and driver version, unless specified
                
                //For running Selenium Grid
                if (gridRunFlag.equalsIgnoreCase("true")) {
                	driver = new RemoteWebDriver(new URL(prop.getProperty("grid_hub_url")), options);
                	
                	//With WebDriverManager in Selenium Grid Nodes
                	/*driver = WebDriverManager.chromedriver()
                				.browserVersion(browser_version) //Specify major version (e.g. "120" or "121.0.6167.85")
                				.capabilities(options)
                				.remoteAddress(new URL(prop.getProperty("grid_hub_url")))
                				.create();*/
                } else {//local run
                	//WebDriverManager.chromedriver().setup();
                	//WebDriverManager.chromedriver().browserVersion(browser_version).setup();
                	driver = new ChromeDriver(options);
                }
        }
        
        tldriver.set(driver); // Set the driver for the current thread
        /*driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.get(prop.getProperty("url"));*/
        
        // Fetch timeouts from properties and convert to Long
        long pageLoad = Long.parseLong(prop.getProperty("timeout.pageLoad"));
        long script = Long.parseLong(prop.getProperty("timeout.script"));
        // Apply Global Timeouts
        getDriver().manage().timeouts().pageLoadTimeout(Duration.ofSeconds(pageLoad));
        getDriver().manage().timeouts().scriptTimeout(Duration.ofSeconds(script));
        
        getDriver().manage().window().maximize();
        //getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        getDriver().get(urlText);
        
        // Now get the browser name and version at run-time and store them in a test context of TestNG
        Capabilities caps = ((RemoteWebDriver) getDriver()).getCapabilities();
        browser = caps.getBrowserName();
        browser_version = caps.getBrowserVersion();
        
        // Store in TestNG context (thread-safe per test run)
        context.setAttribute("browserName", browser);
        context.setAttribute("browserVersion", browser_version);
        
        System.out.println("Browser:" + browser + 
        		" with version:" + browser_version +
        		" launched for URL:" + urlText + 
        		" in Headless:" + headless + 
        		" mode with Selenium Grid Run:" + gridRunFlag);
        //print Application URL, Browser type, Headless mode, Selenium Grid Run flag in the report
    	/*Listeners.getTest().log(Status.INFO, "Browser: " + browser + 
    			", " + "URL: " + urlText + 
    			", " + "Headless Mode:" + headless + 
    			", " + "Selenium Grid Run:" + gridRunFlag);*/
    }

    @AfterMethod
    public void tearDownTest() {
        /*if (driver != null) {
            driver.quit();
        }*/
    	if (getDriver() != null) {
        getDriver().quit();
        System.out.println("Browser closed.");
        		
        tldriver.remove(); // This is necessary to clean up the thread
    	}
    }
    
    @AfterSuite
    public void tearDownSuite() {
    	prop.clear();// Clear all config properties
    	
        //Below is just for user information
        System.out.println("Please refer to " + System.getProperty("user.dir") + "\\test_results\\extent-reports directory for the generated test results.");
        System.out.println("Please refer to " + System.getProperty("user.dir") + "\\target\\surefire-reports directory for the latest surefire report.");
        System.out.println("In case of errors, please refer to dump files (if any exist) [date].dump, [date]-jvmRun[N].dump and [date].dumpstream.");
    }
}
