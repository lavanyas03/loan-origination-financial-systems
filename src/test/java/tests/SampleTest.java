package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Sample test class to verify BrowserFactory and BaseTest implementation
 */
public class SampleTest extends BaseTest {
    
    @Test(description = "Verify browser launches and navigates to base URL")
    public void testBrowserLaunch() {
        // Verify driver is initialized
        Assert.assertNotNull(driver, "WebDriver should be initialized");
        
        // Verify driver is active
        Assert.assertNotNull(driver.getCurrentUrl(), "Should navigate to a URL");
        
        // Verify page title is accessible
        String title = driver.getTitle();
        System.out.println("Page Title: " + title);
        Assert.assertNotNull(title, "Page title should not be null");
        
        // Verify wait is initialized
        Assert.assertNotNull(wait, "WebDriverWait should be initialized");
        
        System.out.println("Test executed successfully!");
        System.out.println("Current URL: " + driver.getCurrentUrl());
        System.out.println("Browser: " + getConfigProperty("browser"));
    }
    
    @Test(description = "Verify browser window is maximized")
    public void testBrowserMaximized() {
        // Verify driver is initialized
        Assert.assertNotNull(driver, "WebDriver should be initialized");
        
        // Get window dimension
        org.openqa.selenium.Dimension windowSize = driver.manage().window().getSize();
        System.out.println("Window Size: " + windowSize.getWidth() + "x" + windowSize.getHeight());
        
        // Verify window has reasonable dimensions (maximized windows are typically large)
        Assert.assertTrue(windowSize.getWidth() > 800, "Window width should be greater than 800px");
        Assert.assertTrue(windowSize.getHeight() > 600, "Window height should be greater than 600px");
    }
    
    @Test(description = "Verify implicit wait is configured")
    public void testImplicitWaitConfiguration() {
        // Verify driver is initialized
        Assert.assertNotNull(driver, "WebDriver should be initialized");
        
        // Verify config property is loaded
        String implicitWait = getConfigProperty("implicitWait");
        Assert.assertNotNull(implicitWait, "Implicit wait configuration should be loaded");
        System.out.println("Configured Implicit Wait: " + implicitWait + " seconds");
    }
}
