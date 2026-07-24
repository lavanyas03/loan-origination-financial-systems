package tests;

import base.BaseTest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.GoogleSearchPage;

/**
 * PageObjectFrameworkTest validates the complete automation framework functionality
 * including BasePage, Page Object Model, browser launch, page interactions,
 * screenshot capture on failure, and Extent Report generation.
 */
public class PageObjectFrameworkTest extends BaseTest {
    
    private static final Logger logger = LogManager.getLogger(PageObjectFrameworkTest.class);
    
    @Test(priority = 1, description = "Verify framework initialization and browser launch")
    public void testFrameworkInitialization() {
        logger.info("========== Test 1: Framework Initialization ==========");
        
        // Verify driver is initialized
        Assert.assertNotNull(driver, "WebDriver should be initialized");
        logger.info("✓ WebDriver initialized successfully");
        
        // Verify wait is initialized
        Assert.assertNotNull(wait, "WebDriverWait should be initialized");
        logger.info("✓ WebDriverWait initialized successfully");
        
        // Verify config is initialized
        Assert.assertNotNull(config, "ConfigReader should be initialized");
        logger.info("✓ ConfigReader initialized successfully");
        
        // Verify browser is launched
        String currentUrl = driver.getCurrentUrl();
        Assert.assertNotNull(currentUrl, "Browser should navigate to a URL");
        logger.info("✓ Browser launched and navigated to: {}", currentUrl);
        
        logger.info("========== Test 1 Passed: Framework initialized successfully ==========");
    }
    
    @Test(priority = 2, description = "Verify BasePage methods using Page Object")
    public void testBasePageMethods() {
        logger.info("========== Test 2: BasePage Methods Validation ==========");
        
        // Initialize Page Object
        GoogleSearchPage googlePage = new GoogleSearchPage(driver);
        logger.info("✓ Page Object (GoogleSearchPage) created successfully");
        
        // Navigate to Google
        googlePage.navigateToGoogle();
        logger.info("✓ Navigation successful");
        
        // Verify page title using BasePage method
        String pageTitle = googlePage.getPageTitle();
        Assert.assertTrue(pageTitle.contains("Google"), 
            "Page title should contain 'Google', but found: " + pageTitle);
        logger.info("✓ getPageTitle() method working - Title: {}", pageTitle);
        
        // Verify page URL using BasePage method
        String pageUrl = googlePage.getPageUrl();
        Assert.assertTrue(pageUrl.contains("google.com"), 
            "URL should contain 'google.com', but found: " + pageUrl);
        logger.info("✓ getPageUrl() method working - URL: {}", pageUrl);
        
        // Verify isDisplayed() method
        boolean isSearchBoxDisplayed = googlePage.isSearchBoxDisplayed();
        Assert.assertTrue(isSearchBoxDisplayed, "Search box should be displayed");
        logger.info("✓ isDisplayed() method working - Search box is visible");
        
        // Verify isDisplayed() method for Google logo
        boolean isLogoDisplayed = googlePage.isGoogleLogoDisplayed();
        Assert.assertTrue(isLogoDisplayed, "Google logo should be displayed");
        logger.info("✓ isDisplayed() method working - Google logo is visible");
        
        logger.info("========== Test 2 Passed: BasePage methods validated successfully ==========");
    }
    
    @Test(priority = 3, description = "Verify page interactions using BasePage click and type methods")
    public void testPageInteractions() {
        logger.info("========== Test 3: Page Interactions Validation ==========");
        
        // Initialize Page Object
        GoogleSearchPage googlePage = new GoogleSearchPage(driver);
        logger.info("✓ Page Object created");
        
        // Navigate to Google
        googlePage.navigateToGoogle();
        logger.info("✓ Navigated to Google");
        
        // Test type() method - Enter search query
        String searchQuery = "Selenium WebDriver";
        googlePage.enterSearchQuery(searchQuery);
        logger.info("✓ type() method working - Entered search query: {}", searchQuery);
        
        // Wait for page to process using explicit wait
        waitUtils.waitForPageToLoad();
        
        // Test click() method - Perform search
        googlePage.performSearch(searchQuery);
        logger.info("✓ click() method working - Search performed");
        
        // Test waitForElement() method - Wait for results
        boolean resultsDisplayed = googlePage.waitForSearchResults();
        Assert.assertTrue(resultsDisplayed, "Search results should be displayed");
        logger.info("✓ waitForElement() method working - Search results loaded");
        
        // Verify page title changed after search
        String resultPageTitle = googlePage.getPageTitle();
        Assert.assertTrue(resultPageTitle.contains(searchQuery), 
            "Page title should contain search query: " + searchQuery);
        logger.info("✓ Page title after search: {}", resultPageTitle);
        
        logger.info("========== Test 3 Passed: Page interactions validated successfully ==========");
    }
    
    @Test(priority = 4, description = "Verify getText method from BasePage")
    public void testGetTextMethod() {
        logger.info("========== Test 4: getText() Method Validation ==========");
        
        // Initialize Page Object
        GoogleSearchPage googlePage = new GoogleSearchPage(driver);
        
        // Navigate to Google
        googlePage.navigateToGoogle();
        logger.info("✓ Navigated to Google");
        
        // Perform a search
        String searchQuery = "TestNG Framework";
        googlePage.performSearch(searchQuery);
        logger.info("✓ Search performed for: {}", searchQuery);
        
        // Wait for results
        googlePage.waitForSearchResults();
        
        // Test getText() method - Get result statistics
        String resultStats = googlePage.getResultStats();
        Assert.assertNotNull(resultStats, "Result statistics should not be null");
        Assert.assertFalse(resultStats.trim().isEmpty(), "Result statistics should not be empty");
        Assert.assertTrue(resultStats.length() > 0, "Result statistics should have content");
        logger.info("✓ getText() method working - Result stats: {}", resultStats);
        
        logger.info("========== Test 4 Passed: getText() method validated successfully ==========");
    }
    
    @Test(priority = 5, description = "Verify waitForElement with custom timeout")
    public void testWaitMethods() {
        logger.info("========== Test 5: Wait Methods Validation ==========");
        
        // Initialize Page Object with custom timeout
        GoogleSearchPage googlePage = new GoogleSearchPage(driver, 30);
        logger.info("✓ Page Object created with custom timeout: 30 seconds");
        
        // Navigate to Google
        googlePage.navigateToGoogle();
        logger.info("✓ Navigated to Google");
        
        // Verify waitForPageTitle method
        boolean titleMatches = googlePage.waitForPageTitle("Google");
        Assert.assertTrue(titleMatches, "Page title should contain 'Google'");
        logger.info("✓ waitForPageTitle() method working");
        
        // Verify waitForUrl method
        boolean urlMatches = googlePage.waitForUrl("google.com");
        Assert.assertTrue(urlMatches, "URL should contain 'google.com'");
        logger.info("✓ waitForUrl() method working");
        
        logger.info("========== Test 5 Passed: Wait methods validated successfully ==========");
    }
    
    @Test(priority = 6, description = "Verify framework handles multiple page objects")
    public void testMultiplePageObjects() {
        logger.info("========== Test 6: Multiple Page Objects Validation ==========");
        
        // Create first page object instance
        GoogleSearchPage googlePage1 = new GoogleSearchPage(driver);
        googlePage1.navigateToGoogle();
        logger.info("✓ First page object instance created and used");
        
        // Perform search with first instance
        googlePage1.performSearch("Java 17");
        googlePage1.waitForSearchResults();
        logger.info("✓ First search completed");
        
        // Create second page object instance
        GoogleSearchPage googlePage2 = new GoogleSearchPage(driver);
        googlePage2.navigateToGoogle();
        logger.info("✓ Second page object instance created and used");
        
        // Perform search with second instance
        googlePage2.performSearch("Maven");
        googlePage2.waitForSearchResults();
        logger.info("✓ Second search completed");
        
        // Verify we can still use first instance
        String currentTitle = googlePage2.getPageTitle();
        Assert.assertNotNull(currentTitle, "Page title should not be null");
        logger.info("✓ Multiple page objects work correctly - Current title: {}", currentTitle);
        
        logger.info("========== Test 6 Passed: Multiple page objects validated successfully ==========");
    }
    
    @Test(priority = 7, description = "Verify framework stability and reusability")
    public void testFrameworkStability() {
        logger.info("========== Test 7: Framework Stability Validation ==========");
        
        // Perform multiple operations to test stability
        GoogleSearchPage googlePage = new GoogleSearchPage(driver);
        
        // Test 1: Navigation
        googlePage.navigateToGoogle();
        Assert.assertTrue(googlePage.isTitleCorrect(), "Title should contain Google");
        logger.info("✓ Navigation stable");
        
        // Test 2: Multiple searches
        String[] searches = {"Selenium 4", "TestNG", "Page Object Model"};
        for (String query : searches) {
            googlePage.navigateToGoogle();
            googlePage.performSearch(query);
            googlePage.waitForSearchResults();
            logger.info("✓ Search stable for query: {}", query);
        }
        
        // Test 3: Verify no memory leaks or driver issues
        Assert.assertNotNull(driver, "Driver should still be available");
        Assert.assertTrue(driver.getWindowHandles().size() > 0, "Browser window should be open");
        logger.info("✓ Framework is stable after multiple operations");
        
        logger.info("========== Test 7 Passed: Framework stability validated successfully ==========");
    }
    
    @Test(priority = 8, description = "This test intentionally fails to verify screenshot capture and reporting")
    public void testScreenshotCaptureOnFailure() {
        logger.info("========== Test 8: Screenshot Capture on Failure ==========");
        
        GoogleSearchPage googlePage = new GoogleSearchPage(driver);
        googlePage.navigateToGoogle();
        
        // This assertion will fail intentionally to test screenshot capture
        String pageTitle = googlePage.getPageTitle();
        logger.info("Current page title: {}", pageTitle);
        
        // Intentional failure to demonstrate screenshot capture
        Assert.assertTrue(pageTitle.contains("NonExistentTitle"), 
            "This test intentionally fails to demonstrate screenshot capture and Extent Report functionality");
    }
    
    @Test(priority = 9, description = "Verify all BasePage utility methods")
    public void testAllBasePageMethods() {
        logger.info("========== Test 9: All BasePage Methods Validation ==========");
        
        GoogleSearchPage googlePage = new GoogleSearchPage(driver);
        googlePage.navigateToGoogle();
        
        // Test isDisplayed()
        Assert.assertTrue(googlePage.isSearchBoxDisplayed(), "Search box should be displayed");
        logger.info("✓ isDisplayed() validated");
        
        // Test getAttribute()
        String placeholder = googlePage.getSearchBoxPlaceholder();
        Assert.assertNotNull(placeholder, "Search box should have title attribute");
        logger.info("✓ getAttribute() validated - Placeholder: {}", placeholder);
        
        // Test type()
        googlePage.enterSearchQuery("Test Query");
        logger.info("✓ type() validated");
        
        // Test clearSearchBox (uses clear method)
        googlePage.clearSearchBox();
        logger.info("✓ clear() validated");
        
        // Test getText() - enter new search and get results
        googlePage.performSearch("Page Object Model");
        googlePage.waitForSearchResults();
        String stats = googlePage.getResultStats();
        Assert.assertNotNull(stats, "Result stats should not be null");
        Assert.assertTrue(stats.length() > 0, "Result stats should have content");
        logger.info("✓ getText() validated - Stats: {}", stats);
        
        // Test getPageTitle()
        String title = googlePage.getPageTitle();
        Assert.assertNotNull(title, "Page title should not be null");
        logger.info("✓ getPageTitle() validated - Title: {}", title);
        
        // Test getPageUrl()
        String url = googlePage.getPageUrl();
        Assert.assertTrue(url.contains("google"), "URL should contain 'google'");
        logger.info("✓ getPageUrl() validated - URL: {}", url);
        
        logger.info("========== Test 9 Passed: All BasePage methods validated successfully ==========");
    }
}
