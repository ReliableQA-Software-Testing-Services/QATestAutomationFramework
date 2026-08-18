/**
 * 
 */
package com.qatest.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
import java.io.IOException;
//import java.io.OutputStream;
//import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.time.Duration;

import java.util.Properties;
import java.util.TimeZone;
import java.util.logging.Level;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.GeckoDriverService;
//import org.openqa.selenium.firefox.FirefoxDriverLogLevel;
//import org.openqa.selenium.logging.LogEntries;
//import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
//import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.SessionId;
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

import com.qatest.utils.DateTimeUtil;
import com.qatest.utils.ExtentReportsManager;
import com.qatest.utils.Listeners;
//import org.testng.xml.XmlTest;
import com.qatest.utils.LoggingUtil;
//import com.qatest.utils.TeeOutputStream;
import com.qatest.utils.UniquePathCreator;

//import com.aventstack.extentreports.Status;
//import com.qatest.utils.Listeners;

//import io.github.bonigarcia.wdm.WebDriverManager; //Selenium Manager(in-built with Selenium 4.6 onwards) eliminates the need for WebDriverManager

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
    
    //private ConfigReader cr;
    //protected Properties prop;
    public static Properties prop = null;
    
    // Get project's root directory path
    public static final String PROJECT_ROOT_PATH = System.getProperty("user.dir");
    // Use File.separator to ensure it works on both Windows (\) and Linux/Mac (/)
    public static final String EXTENTREPORTS_DIR_PATH = PROJECT_ROOT_PATH 
    		+ File.separator + "test_results" 
    		+ File.separator + "extent-reports";
    // Set path for logs directory
    public static final String LOGS_DIR_PATH = EXTENTREPORTS_DIR_PATH 
    		+ File.separator + "logs";
    // Set path for screenshots directory
    public static final String SCREENSHOTS_DIR_PATH = EXTENTREPORTS_DIR_PATH 
    		+ File.separator + "screenshots";
    // Set path for test data directory
    public static final String TESTDATA_PARENT_DIR_PATH = PROJECT_ROOT_PATH 
    		+ File.separator + "test_data";
    // Set path for test configurations directory
    public static final String TEST_CONFIGS_DIR_PATH = PROJECT_ROOT_PATH 
    		+ File.separator + "test_config";
    
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
    public void setUpSuite(@Optional("qa") String envName) throws FileNotFoundException {
    	// Load config properties file (contains URL, Headless Mode, Grid Run Flag, etc.)
    	/*cr = new ConfigReader();
        prop = cr.init_prop();*/
        prop = new Properties();
        
        if (envName == null || envName.isEmpty()) envName = "qa";
        
        System.setProperty("test.env", envName);
        System.out.println("Test Environment: " + System.getProperty("test.env"));
        
        String configFilePath = null;
        
        switch(envName.toUpperCase()) {
        	
	    	case "PROD":
	    		// Use File.separator to ensure it works on both Windows (\) and Linux/Mac (/)
	            configFilePath = TEST_CONFIGS_DIR_PATH + 
	            			File.separator + "config - prod.properties";
	            break;
	    	case "UAT":
	    		configFilePath = TEST_CONFIGS_DIR_PATH + 
        					File.separator + "config - stage.properties";
	    		break;
	    	default:
	    		configFilePath = TEST_CONFIGS_DIR_PATH + 
        					File.separator + "config - qa.properties";
	    		//break;
	    	/*default:
	    		System.out.println("Invalid test environment; please set the correct test environment(QA, UAT, PROD, etc) and run the tests again.");
	    		System.exit(0);*/
        }
        
        try {
            FileInputStream ip = new FileInputStream(configFilePath);
            prop.load(ip);
        } catch (IOException e) {
            e.printStackTrace();
        }

		// Print test environment in the dashboard of extent-reports
        ExtentReportsManager.setSystemInfoDynamically("Parallel Run Environment", envName);
        
        // Create logs directory unless it exists
        //new File(prop.getProperty("logs.directory")).mkdirs();
        File logsDir = new File(LOGS_DIR_PATH);
        if (!logsDir.exists()) {
        	logsDir.mkdirs();
        }
        
        // Create screenshots directory unless it exists
        //new File(prop.getProperty("screenshots.directory")).mkdirs();
        File screenshotsDir = new File(SCREENSHOTS_DIR_PATH);
        if (!screenshotsDir.exists()) {
        	screenshotsDir.mkdirs();
        }
    }
    
    @BeforeMethod
    @Parameters({"browser", "browser_version"})
    public void setUpTest(@Optional("chrome") String browser, 
    					  @Optional("latest") String browser_version, 
    					  ITestContext context) throws IOException, 
    													MalformedURLException, 
    													InterruptedException,
    													Exception {
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
        String utc_timestamp = null;
        Path uniqueDriverLogPath = null;
        String driverLogPath = null;
        
        // Read the property set by Maven Profiles in pom.xml
        //String headless = System.getProperty("headlessMode");
        // Read the property from config properties file
        String headless = prop.getProperty("headless"); 
        
        // Create LoggingPreferences
        LoggingPreferences logPrefs = new LoggingPreferences();
        logPrefs.enable(LogType.BROWSER, Level.ALL);      // This log type pertains to logs from the browser.
        logPrefs.enable(LogType.DRIVER, Level.ALL);       // This log pertains to logs from the WebDriver implementation
        //logPrefs.enable(LogType.PERFORMANCE, Level.ALL);  // Performance logs (not applicable to Firefox browser)
        //logPrefs.enable(LogType.CLIENT, Level.ALL);       // Client logs (no longer supported in Selenium 4 and later)
        //logPrefs.enable(LogType.SERVER, Level.ALL);       // Server logs (no longer supported in Selenium 4 and later)

        /*if (browser.equalsIgnoreCase("chrome")) {
        
        } else if (browser.equalsIgnoreCase("firefox")) {
        	
        } else {
            throw new IllegalArgumentException("Unsupported browser: " + browser);
        }*/
        
        switch(browser.toUpperCase()) {
        	
        	case "FIREFOX":
        		FirefoxOptions firefox_options = new FirefoxOptions();
        		//firefox_options.addArguments("--kiosk");// to open browser in full-screen mode
        		//firefox_options.addArguments("--marionette-port 67123");// This is not advisable for parallel run 
        		//as there will be race-condition while multiple threads try to utilize the same port 
        		
        		utc_timestamp =  DateTimeUtil.getCurrentTimestampInUTC();
                uniqueDriverLogPath = UniquePathCreator.createUniquePath(LOGS_DIR_PATH, "geckodriver_logs_" + utc_timestamp + "_UTC", "log", false);
                driverLogPath = uniqueDriverLogPath.toAbsolutePath().toString();
        		// Build the service and declare your target log file
        		GeckoDriverService firefox_service = new GeckoDriverService.Builder()
        				.withTruncatedLogs(false)
        		        .withLogFile(new File(driverLogPath))
        		        .build();
        		
        		// Set logging preferences in W3C-compliant way
        		firefox_options.setCapability("moz:firefoxOptions", logPrefs);
        		//firefox_options.setLogLevel(FirefoxDriverLogLevel.TRACE);
        		
                
                if (headless.equalsIgnoreCase("true")) {
                	firefox_options.addArguments("-headless");
                    //firefox_options.addArguments("-window-size=1920,1080");
                	firefox_options.addArguments("--disable-gpu");
                } /*else {
                	firefox_options.addArguments("-start-maximized");// There is no such option for Firefox as of 13-Aug-2026
                }*/
                
                //WebDriverManager.firefoxdriver().setup();//setup the latest browser version, unless specified
                
                //For running Selenium Grid
                if (gridRunFlag.equalsIgnoreCase("true")) {
                	firefox_options.addArguments("--no-sandbox"); // Helps prevent crashes in Docker/Linux environments
                	firefox_options.addArguments("--disable-dev-shm-usage"); // Fixes OOM memory crashes in Docker/Linux nodes
                	
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
                	//driver = new FirefoxDriver(firefox_options);
                	driver = new FirefoxDriver(firefox_service, firefox_options);
                }
                break;
        	case "EDGE":
        		EdgeOptions edge_options = new EdgeOptions();
        		//edge_options.addArguments("--start-maximized");
        		
        		// Enable performance logs for Edge browser
        		//logPrefs.enable(LogType.PERFORMANCE, Level.ALL);
        		
        		// Set logging preferences in W3C-compliant way
        		//edge_options.setCapability("goog:loggingPrefs", logPrefs);
        		edge_options.setCapability(EdgeOptions.LOGGING_PREFS, logPrefs);
        		
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
                //options.addArguments("--start-maximized");
                //options.addArguments("--disable-gpu");
                
                // 1. Core arguments to fix the DevToolsActivePort issue
                options.addArguments("--no-sandbox"); // Bypasses OS security model layers (Critical for errors)
                options.addArguments("--disable-dev-shm-usage"); // Forces Chrome to use system memory instead of /dev/shm
                
                // 2. Fixes for recent Chrome updates and profile sharing locks
                //options.addArguments("--remote-debugging-port=9222");// This is not recommended
                // Force Selenium to use standard input/output pipes instead of local ports for communication in case firewalls or restricted non-admin privileges block local port listening
                options.addArguments("--remote-debugging-pipe");// Alternative communication channel if port fails
                // Create a unique/isolated temporary profile directory name for this run to allow port mapping
                utc_timestamp =  DateTimeUtil.getCurrentTimestampInUTC();
                String uniqueProfileDir = "selenium_unique_profile_" + utc_timestamp;
                //String uniqueProfilePath = System.getProperty("java.io.tmpdir") + uniqueProfileDir;
                Path uniqueProfilePath = UniquePathCreator.createUniquePath(System.getProperty("java.io.tmpdir"), uniqueProfileDir, null, true);
                options.addArguments("--user-data-dir=" + uniqueProfilePath.toAbsolutePath().toString());
            	
                // 1. Set Driver logs filepath
                //String driverLogPath = "target/chromedriver_server_verbose.log";
                //String  driverLogPath = LOGS_DIR_PATH 
                //		+ File.separator 
                //		+ "chromedriver_server_verbose_logs_" + utc_timestamp + "_UTC.log";
                
                uniqueDriverLogPath = UniquePathCreator.createUniquePath(LOGS_DIR_PATH, "chromedriver_verbose_logs_" + utc_timestamp + "_UTC", "log", false);
                driverLogPath = uniqueDriverLogPath.toAbsolutePath().toString();
                
                //new File(prop.getProperty("logs.directory")).mkdirs();
                
                /*// 2. Dual Stream Setup (Console + File)
                FileOutputStream driverFileStream = new FileOutputStream(driverLogPath, true);// 'true' enables append mode
                OutputStream dualStream = new TeeOutputStream(System.out, driverFileStream);
                
                ChromeDriverService service = new ChromeDriverService.Builder()
                        .withVerbose(true)
                        .withLogOutput(dualStream)
                        .build();*/
                
                // 2. Only Persistent File Setup (Physical File)
                ChromeDriverService service = new ChromeDriverService.Builder()
                        .withVerbose(true)
                        .withLogFile(new File(driverLogPath))
                        .build();
                
                // Enable performance logs for Chrome browser
        		//logPrefs.enable(LogType.PERFORMANCE, Level.ALL);

                // 3. Enable Browser & Client Logs  // Set logging preferences in W3C-compliant way
                //options.setCapability("goog:loggingPrefs", logPrefs);
                options.setCapability(ChromeOptions.LOGGING_PREFS, logPrefs);

            	//options.setCapability("os", "Windows");
            	//options.setCapability("os_version", "11");
            	//options.setCapability("browser_version", "latest");


                if (headless.equalsIgnoreCase("true")) {
                    options.addArguments("--headless=new");
                    options.addArguments("--window-size=1920,1080"); 
                }/* else {
                	options.addArguments("--start-maximized");
                }*/
                
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
                	driver = new ChromeDriver(service, options);
                	//driver = new ChromeDriver(options);
                }
        }
        
        tldriver.set(driver); // Set the driver for the current thread
        /*driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.get(prop.getProperty("url"));*/
        
        // Fetch timeouts from properties and convert to Long
        long pageLoad = Long.parseLong(prop.getProperty("timeout.pageLoad"));
        long script = Long.parseLong(prop.getProperty("timeout.script"));
        long implicitWait = Long.parseLong(prop.getProperty("timeout.implicit"));
        long explicitWait = Long.parseLong(prop.getProperty("timeout.explicit"));
        
        // Apply Global Timeouts
        getDriver().manage().timeouts().pageLoadTimeout(Duration.ofSeconds(pageLoad));
        getDriver().manage().timeouts().scriptTimeout(Duration.ofSeconds(script));
        //getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));
        
        getDriver().manage().window().maximize();// This will be ignored when running in headless mode
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
        		" in Headless:" + headless + " mode" + 
        		" with Selenium Grid Run:" + gridRunFlag + 
        		" in test environment: " + System.getProperty("test.env"));
        //print Application URL, Browser type, Headless mode, Selenium Grid Run flag in the report
    	/*Listeners.getTest().log(Status.INFO, "Browser: " + browser + 
    			", " + "URL: " + urlText + 
    			", " + "Headless Mode:" + headless + 
    			", " + "Selenium Grid Run:" + gridRunFlag);*/
    }

    @AfterMethod(alwaysRun = true) //browser cleanup happens even if the test is skipped or fails early
    public void tearDownTest() {
        /*if (driver != null) {
            driver.quit();
        }*/
    	if (getDriver() != null) {
    		try {
    			//dualStream.flush();// This is required to write driver logs in a file
        		
        		//Capture current WebDriver session id
                SessionId session = ((RemoteWebDriver) getDriver()).getSessionId();
                String currentSessionID = session.toString();
                String timestamp = DateTimeUtil.getCurrentTimestampInUTC();
                
                System.out.println("Session ID: " + currentSessionID);
        		
        		/*// Save Browser logs in a file
                String browserLogFilepath = LOGS_DIR_PATH + File.separator + "browser_logs_" + currentSessionID + "_" + timestamp + "_UTC.log";
                LoggingUtil.saveAndPrintWebLogs(getDriver(), browserLogFilepath);
        		// Flush buffered Driver logs in a Driver logs file before quitting the Driver
                String driverLogFilePath = LOGS_DIR_PATH + File.separator + "driver_logs_" + currentSessionID + "_" + timestamp + "_UTC.log";
        		LoggingUtil.saveAndPrintDriverLogs(getDriver(), driverLogFilePath);*/
    		} catch (Exception e) {
    			e.printStackTrace();
    		} finally {
    			getDriver().quit();
        		System.out.println("Browser closed.");
        		
        		tldriver.remove(); // This is necessary to clean up the thread
    		}
    	}
    }
    
    @AfterSuite(alwaysRun = true)
    public void tearDownSuite() {
    	if (Listeners.extent != null) {    	
    		Listeners.extent.flush();// This is mandatory to generate the extent-report file
    	}
    	
    	if (prop != null) {
    		prop.clear();// Clear all config properties
    	}
    	
        //Below statements are just for user information
        System.out.println("Please refer to " + System.getProperty("user.dir") + "\\test_results\\extent-reports directory for the generated test results.");
        System.out.println("Please refer to " + System.getProperty("user.dir") + "\\target\\surefire-reports directory for the latest surefire report.");
        System.out.println("In case of errors, please refer to dump files (if any exist) [date].dump, [date]-jvmRun[N].dump and [date].dumpstream.");
    }
    
}
