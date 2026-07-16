package tests;

import base.BaseTest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.BasePage;

/**
 * SimpleFrameworkValidationTest validates the core automation framework components
 * without depending on external websites. Tests framework initialization, BasePage methods,
 * and all integrated components.
 */
public class SimpleFrameworkValidationTest extends BaseTest {
    
    private static final Logger logger = LogManager.getLogger(SimpleFrameworkValidationTest.class);
    
    /**
     * Simple test page object for validation
     */
    private static class TestPage extends BasePage {
        public TestPage(org.openqa.selenium.WebDriver driver) {
            super(driver);
        }
    }
    
    @Test(priority = 1, description = "Verify framework initialization")
    public void testFrameworkInitialization() {
        logger.info("========== Test 1: Framework Initialization ==========");
        
        // Verify all core components are initialized
        Assert.assertNotNull(driver, "WebDriver should be initialized");
        Assert.assertNotNull(wait, "WebDriverWait should be initialized");
        Assert.assertNotNull(config, "ConfigReader should be initialized");
        Assert.assertNotNull(waitUtils, "WaitUtils should be initialized");
        
        // Verify browser is running
        String currentUrl = driver.getCurrentUrl();
        Assert.assertNotNull(currentUrl, "Browser should have a current URL");
        
        logger.info("✓ All framework components initialized successfully");
        logger.info("✓ Current URL: {}", currentUrl);
        logger.info("========== Test 1 Passed ==========");
    }
    
    @Test(priority = 2, description = "Verify BasePage instantiation")
    public void testBasePageCreation() {
        logger.info("========== Test 2: BasePage Creation ==========");
        
        // Create a BasePage instance
        TestPage testPage = new TestPage(driver);
        Assert.assertNotNull(testPage, "BasePage should be created");
        
        // Verify BasePage can access driver
        String pageTitle = testPage.getPageTitle();
        Assert.assertNotNull(pageTitle, "BasePage should be able to get page title");
        logger.info("✓ BasePage created and can access driver");
        logger.info("✓ Page title: {}", pageTitle);
        
        // Verify BasePage can get URL
        String pageUrl = testPage.getPageUrl();
        Assert.assertNotNull(pageUrl, "BasePage should be able to get URL");
        logger.info("✓ BasePage can get URL: {}", pageUrl);
        
        logger.info("========== Test 2 Passed ==========");
    }
    
    @Test(priority = 3, description = "Verify browser navigation")
    public void testBrowserNavigation() {
        logger.info("========== Test 3: Browser Navigation ==========");
        
        // Navigate to a test URL
        String testUrl = "data:text/html,<html><body><h1>Test Page</h1><p id='test'>Hello World</p></body></html>";
        driver.get(testUrl);
        logger.info("✓ Navigation successful");
        
        // Verify navigation
        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("data:text/html"), "Should navigate to data URL");
        logger.info("✓ Current URL verified: {}", currentUrl);
        
        // Verify page title access
        TestPage testPage = new TestPage(driver);
        String title = testPage.getPageTitle();
        Assert.assertNotNull(title, "Should be able to get page title");
        logger.info("✓ Page title: {}", title);
        
        logger.info("========== Test 3 Passed ==========");
    }
    
    @Test(priority = 4, description = "Verify BasePage isDisplayed method")
    public void testIsDisplayedMethod() {
        logger.info("========== Test 4: isDisplayed Method ==========");
        
        // Create test page with known elements
        String testHtml = "<html><body>" +
                "<h1 id='header'>Test Header</h1>" +
                "<p id='visible'>Visible Text</p>" +
                "<div id='container'><span id='nested'>Nested Element</span></div>" +
                "</body></html>";
        driver.get("data:text/html," + testHtml);
        
        TestPage testPage = new TestPage(driver);
        
        // Test isDisplayed on existing element
        boolean headerDisplayed = testPage.isDisplayed(By.id("header"));
        Assert.assertTrue(headerDisplayed, "Header should be displayed");
        logger.info("✓ isDisplayed() works for existing element");
        
        // Test isDisplayed on non-existing element
        boolean nonExistent = testPage.isDisplayed(By.id("nonexistent"));
        Assert.assertFalse(nonExistent, "Non-existent element should return false");
        logger.info("✓ isDisplayed() correctly returns false for non-existent element");
        
        logger.info("========== Test 4 Passed ==========");
    }
    
    @Test(priority = 5, description = "Verify BasePage getText method")
    public void testGetTextMethod() {
        logger.info("========== Test 5: getText Method ==========");
        
        // Create test page with known text
        String testHtml = "<html><body>" +
                "<h1 id='title'>Framework Validation</h1>" +
                "<p id='content'>This is test content</p>" +
                "</body></html>";
        driver.get("data:text/html," + testHtml);
        
        TestPage testPage = new TestPage(driver);
        
        // Test getText
        String titleText = testPage.getText(By.id("title"));
        Assert.assertEquals(titleText, "Framework Validation", "Title text should match");
        logger.info("✓ getText() retrieved correct text: {}", titleText);
        
        String contentText = testPage.getText(By.id("content"));
        Assert.assertEquals(contentText, "This is test content", "Content text should match");
        logger.info("✓ getText() works for paragraph: {}", contentText);
        
        logger.info("========== Test 5 Passed ==========");
    }
    
    @Test(priority = 6, description = "Verify BasePage type and click methods")
    public void testTypeAndClickMethods() {
        logger.info("========== Test 6: Type and Click Methods ==========");
        
        // Create test page with input and button
        String testHtml = "<html><body>" +
                "<input type='text' id='input1' />" +
                "<button id='button1'>Click Me</button>" +
                "</body></html>";
        driver.get("data:text/html," + testHtml);
        
        TestPage testPage = new TestPage(driver);
        
        // Test type method
        testPage.type(By.id("input1"), "Test Input");
        String inputValue = testPage.getAttribute(By.id("input1"), "value");
        Assert.assertEquals(inputValue, "Test Input", "Input value should be set");
        logger.info("✓ type() method works correctly");
        
        // Test click method
        testPage.click(By.id("button1"));
        logger.info("✓ click() method executed successfully");
        
        logger.info("========== Test 6 Passed ==========");
    }
    
    @Test(priority = 7, description = "Verify BasePage waitForElement method")
    public void testWaitForElementMethod() {
        logger.info("========== Test 7: waitForElement Method ==========");
        
        // Create test page
        String testHtml = "<html><body>" +
                "<div id='element1'>Element 1</div>" +
                "<div id='element2'>Element 2</div>" +
                "</body></html>";
        driver.get("data:text/html," + testHtml);
        
        TestPage testPage = new TestPage(driver);
        
        // Test waitForElement
        org.openqa.selenium.WebElement element = testPage.waitForElement(By.id("element1"));
        Assert.assertNotNull(element, "Element should be found");
        Assert.assertTrue(element.isDisplayed(), "Element should be displayed");
        logger.info("✓ waitForElement() found element successfully");
        
        // Test waitForElement with custom timeout
        org.openqa.selenium.WebElement element2 = testPage.waitForElement(By.id("element2"), 5);
        Assert.assertNotNull(element2, "Element should be found with custom timeout");
        logger.info("✓ waitForElement() with custom timeout works");
        
        logger.info("========== Test 7 Passed ==========");
    }
    
    @Test(priority = 8, description = "Verify BasePage dropdown methods")
    public void testDropdownMethods() {
        logger.info("========== Test 8: Dropdown Methods ==========");
        
        // Create test page with dropdown
        String testHtml = "<html><body>" +
                "<select id='dropdown'>" +
                "<option value='1'>Option 1</option>" +
                "<option value='2'>Option 2</option>" +
                "<option value='3' selected>Option 3</option>" +
                "</select>" +
                "</body></html>";
        driver.get("data:text/html," + testHtml);
        
        TestPage testPage = new TestPage(driver);
        
        // Test get selected option
        String selected = testPage.getSelectedDropdownText(By.id("dropdown"));
        Assert.assertEquals(selected, "Option 3", "Initially selected option should be Option 3");
        logger.info("✓ getSelectedDropdownText() works: {}", selected);
        
        // Test select by visible text
        testPage.selectDropdown(By.id("dropdown"), "Option 1");
        String newSelected = testPage.getSelectedDropdownText(By.id("dropdown"));
        Assert.assertEquals(newSelected, "Option 1", "Selected option should be Option 1");
        logger.info("✓ selectDropdown() works: {}", newSelected);
        
        // Test select by index
        testPage.selectDropdownByIndex(By.id("dropdown"), 1);
        String indexSelected = testPage.getSelectedDropdownText(By.id("dropdown"));
        Assert.assertEquals(indexSelected, "Option 2", "Selected option should be Option 2");
        logger.info("✓ selectDropdownByIndex() works: {}", indexSelected);
        
        logger.info("========== Test 8 Passed ==========");
    }
    
    @Test(priority = 9, description = "Verify framework stability")
    public void testFrameworkStability() {
        logger.info("========== Test 9: Framework Stability ==========");
        
        // Perform multiple operations to test stability
        for (int i = 1; i <= 3; i++) {
            String testHtml = "<html><body><h1 id='iteration'>Iteration " + i + "</h1></body></html>";
            driver.get("data:text/html," + testHtml);
            
            TestPage testPage = new TestPage(driver);
            String text = testPage.getText(By.id("iteration"));
            Assert.assertEquals(text, "Iteration " + i, "Should match iteration " + i);
            logger.info("✓ Iteration {} completed successfully", i);
        }
        
        // Verify driver is still functional
        Assert.assertNotNull(driver, "Driver should still be available");
        Assert.assertTrue(driver.getWindowHandles().size() > 0, "Browser window should be open");
        logger.info("✓ Framework is stable after multiple operations");
        
        logger.info("========== Test 9 Passed ==========");
    }
    
    @Test(priority = 10, description = "Intentional failure to test screenshot capture")
    public void testScreenshotOnFailure() {
        logger.info("========== Test 10: Screenshot Capture Test ==========");
        
        driver.get("data:text/html,<html><body><h1>Screenshot Test Page</h1></body></html>");
        
        // This will fail intentionally to trigger screenshot capture
        Assert.fail("Intentional failure to demonstrate screenshot capture and Extent Report integration");
    }
}
