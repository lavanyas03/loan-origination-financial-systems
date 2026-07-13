package base;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

/**
 * BrowserFactory manages WebDriver instances using ThreadLocal for parallel execution support.
 * Supports Chrome, Firefox, and Edge browsers with WebDriverManager for automatic driver management.
 */
public class BrowserFactory {
    
    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    
    /**
     * Private constructor to prevent instantiation
     */
    private BrowserFactory() {
        throw new IllegalStateException("BrowserFactory is a utility class");
    }
    
    /**
     * Gets the WebDriver instance for the current thread
     * 
     * @return WebDriver instance
     */
    public static WebDriver getDriver() {
        return driver.get();
    }
    
    /**
     * Creates and initializes a WebDriver instance based on the specified browser type
     * 
     * @param browserName the name of the browser (chrome, firefox, edge)
     * @param headless whether to run browser in headless mode
     * @return initialized WebDriver instance
     * @throws IllegalArgumentException if browser name is not supported
     */
    public static WebDriver createDriver(String browserName, boolean headless) {
        WebDriver webDriver;
        
        switch (browserName.toLowerCase().trim()) {
            case "chrome":
                webDriver = createChromeDriver(headless);
                break;
                
            case "firefox":
                webDriver = createFirefoxDriver(headless);
                break;
                
            case "edge":
                webDriver = createEdgeDriver(headless);
                break;
                
            default:
                throw new IllegalArgumentException(
                    "Unsupported browser: " + browserName + ". Supported browsers are: chrome, firefox, edge"
                );
        }
        
        driver.set(webDriver);
        return webDriver;
    }
    
    /**
     * Creates a ChromeDriver instance with options
     * 
     * @param headless whether to run Chrome in headless mode
     * @return ChromeDriver instance
     */
    private static WebDriver createChromeDriver(boolean headless) {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        
        if (headless) {
            options.addArguments("--headless=new");
        }
        
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--remote-allow-origins=*");
        
        return new ChromeDriver(options);
    }
    
    /**
     * Creates a FirefoxDriver instance with options
     * 
     * @param headless whether to run Firefox in headless mode
     * @return FirefoxDriver instance
     */
    private static WebDriver createFirefoxDriver(boolean headless) {
        WebDriverManager.firefoxdriver().setup();
        FirefoxOptions options = new FirefoxOptions();
        
        if (headless) {
            options.addArguments("--headless");
        }
        
        options.addArguments("--disable-notifications");
        
        return new FirefoxDriver(options);
    }
    
    /**
     * Creates an EdgeDriver instance with options
     * 
     * @param headless whether to run Edge in headless mode
     * @return EdgeDriver instance
     */
    private static WebDriver createEdgeDriver(boolean headless) {
        WebDriverManager.edgedriver().setup();
        EdgeOptions options = new EdgeOptions();
        
        if (headless) {
            options.addArguments("--headless=new");
        }
        
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-notifications");
        options.addArguments("--remote-allow-origins=*");
        
        return new EdgeDriver(options);
    }
    
    /**
     * Quits the WebDriver instance and removes it from ThreadLocal
     */
    public static void quitDriver() {
        if (driver.get() != null) {
            driver.get().quit();
            driver.remove();
        }
    }
}
