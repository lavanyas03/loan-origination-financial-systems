package base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;

/**
 * BaseTest provides core test setup and teardown functionality.
 * All test classes should extend this class to inherit browser initialization and cleanup.
 */
public class BaseTest {
    
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected Properties config;
    
    /**
     * Constructor loads configuration properties
     */
    public BaseTest() {
        config = new Properties();
        loadConfiguration();
    }
    
    /**
     * Loads configuration from config.properties file
     */
    private void loadConfiguration() {
        try (FileInputStream fis = new FileInputStream("src/main/resources/config.properties")) {
            config.load(fis);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.properties file: " + e.getMessage(), e);
        }
    }
    
    /**
     * Setup method executed before each test method
     * Initializes browser, configures waits, and navigates to base URL
     */
    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        // Get configuration values
        String browserName = config.getProperty("browser", "chrome");
        String baseURL = config.getProperty("baseURL");
        boolean headless = Boolean.parseBoolean(config.getProperty("headless", "false"));
        boolean maximizeWindow = Boolean.parseBoolean(config.getProperty("maximizeWindow", "true"));
        boolean clearCookies = Boolean.parseBoolean(config.getProperty("clearCookies", "true"));
        
        int implicitWait = Integer.parseInt(config.getProperty("implicitWait", "10"));
        int explicitWait = Integer.parseInt(config.getProperty("explicitWait", "20"));
        int pageLoadTimeout = Integer.parseInt(config.getProperty("pageLoadTimeout", "30"));
        
        // Create and configure WebDriver
        driver = BrowserFactory.createDriver(browserName, headless);
        
        // Configure timeouts
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(pageLoadTimeout));
        
        // Initialize explicit wait
        wait = new WebDriverWait(driver, Duration.ofSeconds(explicitWait));
        
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
     * Gets a configuration property value
     * 
     * @param key the property key
     * @return the property value
     */
    public String getConfigProperty(String key) {
        return config.getProperty(key);
    }
}
