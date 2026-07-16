package pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.WaitUtils;

import java.time.Duration;
import java.util.List;

/**
 * BasePage provides reusable methods for all Page Object classes.
 * Implements common web element interactions with proper wait mechanisms and error handling.
 * All page objects should extend this class to inherit common functionality.
 */
public abstract class BasePage {
    
    protected static final Logger logger = LogManager.getLogger(BasePage.class);
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected WaitUtils waitUtils;
    protected int defaultTimeout;
    
    /**
     * Constructor initializes BasePage with WebDriver and timeout configuration
     * 
     * @param driver the WebDriver instance
     */
    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.defaultTimeout = 20;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(defaultTimeout));
        this.waitUtils = new WaitUtils(driver, defaultTimeout);
        logger.debug("BasePage initialized for: {}", this.getClass().getSimpleName());
    }
    
    /**
     * Constructor with custom timeout
     * 
     * @param driver the WebDriver instance
     * @param timeout custom timeout in seconds
     */
    public BasePage(WebDriver driver, int timeout) {
        this.driver = driver;
        this.defaultTimeout = timeout;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
        this.waitUtils = new WaitUtils(driver, timeout);
        logger.debug("BasePage initialized for: {} with timeout: {} seconds", 
                    this.getClass().getSimpleName(), timeout);
    }
    
    /**
     * Waits for element to be visible and clickable, then clicks it
     * 
     * @param locator the element locator
     */
    public void click(By locator) {
        try {
            logger.debug("Attempting to click element: {}", locator);
            WebElement element = waitUtils.waitForElementClickable(locator);
            element.click();
            logger.info("Successfully clicked element: {}", locator);
        } catch (ElementClickInterceptedException e) {
            logger.warn("Click intercepted, attempting JavaScript click: {}", locator);
            clickUsingJavaScript(locator);
        } catch (Exception e) {
            logger.error("Failed to click element: {}", locator, e);
            throw new RuntimeException("Failed to click element: " + locator, e);
        }
    }
    
    /**
     * Clicks element using JavaScript executor (alternative method)
     * 
     * @param locator the element locator
     */
    public void clickUsingJavaScript(By locator) {
        try {
            logger.debug("Attempting JavaScript click on element: {}", locator);
            WebElement element = waitUtils.waitForElementVisible(locator);
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].click();", element);
            logger.info("Successfully clicked element using JavaScript: {}", locator);
        } catch (Exception e) {
            logger.error("Failed to click element using JavaScript: {}", locator, e);
            throw new RuntimeException("Failed to click element using JavaScript: " + locator, e);
        }
    }
    
    /**
     * Waits for element to be visible, clears it, and types text
     * 
     * @param locator the element locator
     * @param text the text to type
     */
    public void type(By locator, String text) {
        try {
            logger.debug("Attempting to type '{}' into element: {}", text, locator);
            WebElement element = waitUtils.waitForElementVisible(locator);
            element.clear();
            element.sendKeys(text);
            logger.info("Successfully typed text into element: {}", locator);
        } catch (Exception e) {
            logger.error("Failed to type into element: {}", locator, e);
            throw new RuntimeException("Failed to type into element: " + locator, e);
        }
    }
    
    /**
     * Types text without clearing the field first
     * 
     * @param locator the element locator
     * @param text the text to type
     */
    public void typeWithoutClear(By locator, String text) {
        try {
            logger.debug("Attempting to type '{}' (without clear) into element: {}", text, locator);
            WebElement element = waitUtils.waitForElementVisible(locator);
            element.sendKeys(text);
            logger.info("Successfully typed text (without clear) into element: {}", locator);
        } catch (Exception e) {
            logger.error("Failed to type into element: {}", locator, e);
            throw new RuntimeException("Failed to type into element: " + locator, e);
        }
    }
    
    /**
     * Waits for element to be visible
     * 
     * @param locator the element locator
     * @return the visible WebElement
     */
    public WebElement waitForElement(By locator) {
        try {
            logger.debug("Waiting for element: {}", locator);
            return waitUtils.waitForElementVisible(locator);
        } catch (Exception e) {
            logger.error("Failed to find element: {}", locator, e);
            throw new RuntimeException("Element not found: " + locator, e);
        }
    }
    
    /**
     * Waits for element to be visible with custom timeout
     * 
     * @param locator the element locator
     * @param timeout custom timeout in seconds
     * @return the visible WebElement
     */
    public WebElement waitForElement(By locator, int timeout) {
        try {
            logger.debug("Waiting for element with timeout {} seconds: {}", timeout, locator);
            return waitUtils.waitForElementVisible(locator, timeout);
        } catch (Exception e) {
            logger.error("Failed to find element: {}", locator, e);
            throw new RuntimeException("Element not found: " + locator, e);
        }
    }
    
    /**
     * Checks if element is displayed on the page
     * 
     * @param locator the element locator
     * @return true if element is displayed, false otherwise
     */
    public boolean isDisplayed(By locator) {
        try {
            logger.debug("Checking if element is displayed: {}", locator);
            WebElement element = waitUtils.waitForElementVisible(locator, 5);
            boolean displayed = element.isDisplayed();
            logger.debug("Element display status: {} for locator: {}", displayed, locator);
            return displayed;
        } catch (Exception e) {
            logger.debug("Element not displayed: {}", locator);
            return false;
        }
    }
    
    /**
     * Checks if element is enabled
     * 
     * @param locator the element locator
     * @return true if element is enabled, false otherwise
     */
    public boolean isEnabled(By locator) {
        try {
            logger.debug("Checking if element is enabled: {}", locator);
            WebElement element = waitForElement(locator);
            boolean enabled = element.isEnabled();
            logger.debug("Element enabled status: {} for locator: {}", enabled, locator);
            return enabled;
        } catch (Exception e) {
            logger.debug("Element not found or not enabled: {}", locator);
            return false;
        }
    }
    
    /**
     * Checks if element is selected (for checkboxes and radio buttons)
     * 
     * @param locator the element locator
     * @return true if element is selected, false otherwise
     */
    public boolean isSelected(By locator) {
        try {
            logger.debug("Checking if element is selected: {}", locator);
            WebElement element = waitForElement(locator);
            boolean selected = element.isSelected();
            logger.debug("Element selected status: {} for locator: {}", selected, locator);
            return selected;
        } catch (Exception e) {
            logger.debug("Element not found or not selected: {}", locator);
            return false;
        }
    }
    
    /**
     * Gets the text of an element
     * 
     * @param locator the element locator
     * @return the element text
     */
    public String getText(By locator) {
        try {
            logger.debug("Getting text from element: {}", locator);
            WebElement element = waitForElement(locator);
            String text = element.getText();
            logger.debug("Retrieved text: '{}' from element: {}", text, locator);
            return text;
        } catch (Exception e) {
            logger.error("Failed to get text from element: {}", locator, e);
            throw new RuntimeException("Failed to get text from element: " + locator, e);
        }
    }
    
    /**
     * Gets an attribute value from an element
     * 
     * @param locator the element locator
     * @param attribute the attribute name
     * @return the attribute value
     */
    public String getAttribute(By locator, String attribute) {
        try {
            logger.debug("Getting attribute '{}' from element: {}", attribute, locator);
            WebElement element = waitForElement(locator);
            String value = element.getAttribute(attribute);
            logger.debug("Retrieved attribute '{}' value: '{}' from element: {}", attribute, value, locator);
            return value;
        } catch (Exception e) {
            logger.error("Failed to get attribute '{}' from element: {}", attribute, locator, e);
            throw new RuntimeException("Failed to get attribute from element: " + locator, e);
        }
    }
    
    /**
     * Selects dropdown option by visible text
     * 
     * @param locator the dropdown locator
     * @param visibleText the visible text to select
     */
    public void selectDropdown(By locator, String visibleText) {
        try {
            logger.debug("Selecting dropdown option by visible text '{}' for element: {}", visibleText, locator);
            WebElement element = waitForElement(locator);
            Select select = new Select(element);
            select.selectByVisibleText(visibleText);
            logger.info("Successfully selected dropdown option: '{}' for element: {}", visibleText, locator);
        } catch (Exception e) {
            logger.error("Failed to select dropdown option '{}' for element: {}", visibleText, locator, e);
            throw new RuntimeException("Failed to select dropdown option: " + locator, e);
        }
    }
    
    /**
     * Selects dropdown option by value
     * 
     * @param locator the dropdown locator
     * @param value the value to select
     */
    public void selectDropdownByValue(By locator, String value) {
        try {
            logger.debug("Selecting dropdown option by value '{}' for element: {}", value, locator);
            WebElement element = waitForElement(locator);
            Select select = new Select(element);
            select.selectByValue(value);
            logger.info("Successfully selected dropdown option by value: '{}' for element: {}", value, locator);
        } catch (Exception e) {
            logger.error("Failed to select dropdown option by value '{}' for element: {}", value, locator, e);
            throw new RuntimeException("Failed to select dropdown by value: " + locator, e);
        }
    }
    
    /**
     * Selects dropdown option by index
     * 
     * @param locator the dropdown locator
     * @param index the index to select (0-based)
     */
    public void selectDropdownByIndex(By locator, int index) {
        try {
            logger.debug("Selecting dropdown option by index '{}' for element: {}", index, locator);
            WebElement element = waitForElement(locator);
            Select select = new Select(element);
            select.selectByIndex(index);
            logger.info("Successfully selected dropdown option by index: '{}' for element: {}", index, locator);
        } catch (Exception e) {
            logger.error("Failed to select dropdown option by index '{}' for element: {}", index, locator, e);
            throw new RuntimeException("Failed to select dropdown by index: " + locator, e);
        }
    }
    
    /**
     * Gets the currently selected option text from dropdown
     * 
     * @param locator the dropdown locator
     * @return the selected option text
     */
    public String getSelectedDropdownText(By locator) {
        try {
            logger.debug("Getting selected dropdown text for element: {}", locator);
            WebElement element = waitForElement(locator);
            Select select = new Select(element);
            String selectedText = select.getFirstSelectedOption().getText();
            logger.debug("Selected dropdown text: '{}' for element: {}", selectedText, locator);
            return selectedText;
        } catch (Exception e) {
            logger.error("Failed to get selected dropdown text for element: {}", locator, e);
            throw new RuntimeException("Failed to get selected dropdown text: " + locator, e);
        }
    }
    
    /**
     * Gets all dropdown options
     * 
     * @param locator the dropdown locator
     * @return list of all option texts
     */
    public List<String> getAllDropdownOptions(By locator) {
        try {
            logger.debug("Getting all dropdown options for element: {}", locator);
            WebElement element = waitForElement(locator);
            Select select = new Select(element);
            return select.getOptions().stream()
                    .map(WebElement::getText)
                    .toList();
        } catch (Exception e) {
            logger.error("Failed to get dropdown options for element: {}", locator, e);
            throw new RuntimeException("Failed to get dropdown options: " + locator, e);
        }
    }
    
    /**
     * Scrolls to element using JavaScript
     * 
     * @param locator the element locator
     */
    public void scrollToElement(By locator) {
        try {
            logger.debug("Scrolling to element: {}", locator);
            WebElement element = waitForElement(locator);
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].scrollIntoView(true);", element);
            logger.info("Successfully scrolled to element: {}", locator);
        } catch (Exception e) {
            logger.error("Failed to scroll to element: {}", locator, e);
            throw new RuntimeException("Failed to scroll to element: " + locator, e);
        }
    }
    
    /**
     * Hovers over an element
     * 
     * @param locator the element locator
     */
    public void hover(By locator) {
        try {
            logger.debug("Hovering over element: {}", locator);
            WebElement element = waitForElement(locator);
            org.openqa.selenium.interactions.Actions actions = 
                new org.openqa.selenium.interactions.Actions(driver);
            actions.moveToElement(element).perform();
            logger.info("Successfully hovered over element: {}", locator);
        } catch (Exception e) {
            logger.error("Failed to hover over element: {}", locator, e);
            throw new RuntimeException("Failed to hover over element: " + locator, e);
        }
    }
    
    /**
     * Double clicks on an element
     * 
     * @param locator the element locator
     */
    public void doubleClick(By locator) {
        try {
            logger.debug("Double clicking element: {}", locator);
            WebElement element = waitUtils.waitForElementClickable(locator);
            org.openqa.selenium.interactions.Actions actions = 
                new org.openqa.selenium.interactions.Actions(driver);
            actions.doubleClick(element).perform();
            logger.info("Successfully double clicked element: {}", locator);
        } catch (Exception e) {
            logger.error("Failed to double click element: {}", locator, e);
            throw new RuntimeException("Failed to double click element: " + locator, e);
        }
    }
    
    /**
     * Right clicks on an element
     * 
     * @param locator the element locator
     */
    public void rightClick(By locator) {
        try {
            logger.debug("Right clicking element: {}", locator);
            WebElement element = waitUtils.waitForElementClickable(locator);
            org.openqa.selenium.interactions.Actions actions = 
                new org.openqa.selenium.interactions.Actions(driver);
            actions.contextClick(element).perform();
            logger.info("Successfully right clicked element: {}", locator);
        } catch (Exception e) {
            logger.error("Failed to right click element: {}", locator, e);
            throw new RuntimeException("Failed to right click element: " + locator, e);
        }
    }
    
    /**
     * Gets the current page title
     * 
     * @return the page title
     */
    public String getPageTitle() {
        try {
            String title = driver.getTitle();
            logger.debug("Page title: {}", title);
            return title;
        } catch (Exception e) {
            logger.error("Failed to get page title", e);
            throw new RuntimeException("Failed to get page title", e);
        }
    }
    
    /**
     * Gets the current page URL
     * 
     * @return the page URL
     */
    public String getPageUrl() {
        try {
            String url = driver.getCurrentUrl();
            logger.debug("Current URL: {}", url);
            return url;
        } catch (Exception e) {
            logger.error("Failed to get current URL", e);
            throw new RuntimeException("Failed to get current URL", e);
        }
    }
    
    /**
     * Waits for page title to contain expected text
     * 
     * @param expectedTitle the expected title text
     * @return true if title contains expected text
     */
    public boolean waitForPageTitle(String expectedTitle) {
        try {
            logger.debug("Waiting for page title to contain: {}", expectedTitle);
            boolean result = wait.until(ExpectedConditions.titleContains(expectedTitle));
            logger.info("Page title contains: {}", expectedTitle);
            return result;
        } catch (Exception e) {
            logger.error("Timeout waiting for page title to contain: {}", expectedTitle, e);
            return false;
        }
    }
    
    /**
     * Waits for URL to contain expected text
     * 
     * @param expectedUrl the expected URL text
     * @return true if URL contains expected text
     */
    public boolean waitForUrl(String expectedUrl) {
        try {
            logger.debug("Waiting for URL to contain: {}", expectedUrl);
            boolean result = wait.until(ExpectedConditions.urlContains(expectedUrl));
            logger.info("URL contains: {}", expectedUrl);
            return result;
        } catch (Exception e) {
            logger.error("Timeout waiting for URL to contain: {}", expectedUrl, e);
            return false;
        }
    }
    
    /**
     * Finds multiple elements
     * 
     * @param locator the element locator
     * @return list of WebElements
     */
    public List<WebElement> findElements(By locator) {
        try {
            logger.debug("Finding elements: {}", locator);
            waitUtils.waitForElementVisible(locator, 5);
            List<WebElement> elements = driver.findElements(locator);
            logger.debug("Found {} elements for locator: {}", elements.size(), locator);
            return elements;
        } catch (Exception e) {
            logger.warn("No elements found for locator: {}", locator);
            return List.of();
        }
    }
    
    /**
     * Gets the count of elements matching the locator
     * 
     * @param locator the element locator
     * @return count of elements
     */
    public int getElementCount(By locator) {
        try {
            logger.debug("Getting element count for: {}", locator);
            List<WebElement> elements = findElements(locator);
            int count = elements.size();
            logger.debug("Element count: {} for locator: {}", count, locator);
            return count;
        } catch (Exception e) {
            logger.warn("Failed to get element count for: {}", locator);
            return 0;
        }
    }
}
