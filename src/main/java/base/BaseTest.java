package base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import utils.ConfigReader;
import utils.WaitUtils;

import java.time.Duration;

/**
 * BaseTest provides core test setup and teardown functionality.
 * All test classes should extend this class to inherit browser initialization and cleanup.
 * Includes utilities for configuration, waits, and screenshots.
 */
public class BaseTest {
    
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected ConfigReader config;
    protected WaitUtils waitUtils;
    
    /**
     * Constructor loads configuration using ConfigReader
     */
    public BaseTest() {
        config = ConfigReader.getInstance();
    }
    
    /**
     * Setup method executed before each test method
     * Initializes browser, configures waits, and navigates to base URL
     */
    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        // Get configuration values using ConfigReader
        String browserName = config.getProperty("browser", "chrome");
        String baseURL = config.getProperty("baseURL");
        boolean headless = config.getBooleanProperty("headless", false);
        boolean maximizeWindow = config.getBooleanProperty("maximizeWindow", true);
        boolean clearCookies = config.getBooleanProperty("clearCookies", true);
        
        int implicitWait = config.getIntProperty("implicitWait", 10);
        int explicitWait = config.getIntProperty("explicitWait", 20);
        int pageLoadTimeout = config.getIntProperty("pageLoadTimeout", 30);
        
        // Create and configure WebDriver
        driver = BrowserFactory.createDriver(browserName, headless);
        
        // Configure timeouts
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(pageLoadTimeout));
        
        // Initialize explicit wait
        wait = new WebDriverWait(driver, Duration.ofSeconds(explicitWait));
        
        // Initialize WaitUtils
        waitUtils = new WaitUtils(driver, explicitWait);
        
        // Clear cookies if configured
        if (clearCookies) {
            driver.manage().deleteAllCookies();
        }
        
        // Maximize window if configured
        if (maximizeWindow) {
            driver.manage().window().maximize();
        }
        
        // Navigate to base URL
        if (baseURL != null && !baseURL.isEmpty()) {
            driver.get(baseURL);
        }
    }
    
    /**
     * Teardown method executed after each test method
     * Closes browser and cleans up resources
     */
    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        BrowserFactory.quitDriver();
        driver = null;
        wait = null;
        waitUtils = null;
    }
    
    /**
     * Gets the current WebDriver instance
     * 
     * @return WebDriver instance
     */
    public WebDriver getDriver() {
        return driver;
    }
    
    /**
     * Gets the WebDriverWait instance
     * 
     * @return WebDriverWait instance
     */
    public WebDriverWait getWait() {
        return wait;
    }
    
    /**
     * Gets the WaitUtils instance
     * 
     * @return WaitUtils instance
     */
    public WaitUtils getWaitUtils() {
        return waitUtils;
    }
    
    /**
     * Gets the ConfigReader instance
     * 
     * @return ConfigReader instance
     */
    public ConfigReader getConfigReader() {
        return config;
    }
    
    /**
     * Gets a configuration property value
     * 
     * @param key the property key
     * @return the property value
     */
    public String getConfigProperty(String key) {
        return config.getProperty(key);
    }
}
