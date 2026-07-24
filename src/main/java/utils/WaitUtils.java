package utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Function;

/**
 * WaitUtils provides reusable explicit wait and fluent wait methods for common element interactions.
 * All methods implement proper exception handling and logging.
 */
public class WaitUtils {
    
    private static final Logger logger = LogManager.getLogger(WaitUtils.class);
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final int defaultTimeout;
    private final int pollingInterval;
    
    /**
     * Constructor initializes WaitUtils with WebDriver and default timeout
     * 
     * @param driver the WebDriver instance
     * @param timeout the default timeout in seconds
     */
    public WaitUtils(WebDriver driver, int timeout) {
        this.driver = driver;
        this.defaultTimeout = timeout;
        this.pollingInterval = 500; // Default polling interval in milliseconds
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
        logger.debug("WaitUtils initialized with timeout: {} seconds", timeout);
    }
    
    /**
     * Waits for element to be visible
     * 
     * @param locator the element locator
     * @return the visible WebElement
     */
    public WebElement waitForElementVisible(By locator) {
        try {
            logger.debug("Waiting for element to be visible: {}", locator);
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            logger.debug("Element is now visible: {}", locator);
            return element;
        } catch (TimeoutException e) {
            logger.error("Timeout waiting for element to be visible: {}", locator);
            throw new TimeoutException("Element not visible after " + defaultTimeout + " seconds: " + locator, e);
        }
    }
    
    /**
     * Waits for element to be visible with custom timeout
     * 
     * @param locator the element locator
     * @param timeout custom timeout in seconds
     * @return the visible WebElement
     */
    public WebElement waitForElementVisible(By locator, int timeout) {
        try {
            logger.debug("Waiting for element to be visible with timeout {} seconds: {}", timeout, locator);
            WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
            return customWait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        } catch (TimeoutException e) {
            logger.error("Timeout waiting for element to be visible: {}", locator);
            throw new TimeoutException("Element not visible after " + timeout + " seconds: " + locator, e);
        }
    }
    
    /**
     * Waits for element to be clickable
     * 
     * @param locator the element locator
     * @return the clickable WebElement
     */
    public WebElement waitForElementClickable(By locator) {
        try {
            logger.debug("Waiting for element to be clickable: {}", locator);
            WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
            logger.debug("Element is now clickable: {}", locator);
            return element;
        } catch (TimeoutException e) {
            logger.error("Timeout waiting for element to be clickable: {}", locator);
            throw new TimeoutException("Element not clickable after " + defaultTimeout + " seconds: " + locator, e);
        }
    }
    
    /**
     * Waits for element to be clickable with custom timeout
     * 
     * @param locator the element locator
     * @param timeout custom timeout in seconds
     * @return the clickable WebElement
     */
    public WebElement waitForElementClickable(By locator, int timeout) {
        try {
            logger.debug("Waiting for element to be clickable with timeout {} seconds: {}", timeout, locator);
            WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
            return customWait.until(ExpectedConditions.elementToBeClickable(locator));
        } catch (TimeoutException e) {
            logger.error("Timeout waiting for element to be clickable: {}", locator);
            throw new TimeoutException("Element not clickable after " + timeout + " seconds: " + locator, e);
        }
    }
    
    /**
     * Waits for element to be present in DOM
     * 
     * @param locator the element locator
     * @return the present WebElement
     */
    public WebElement waitForElementPresent(By locator) {
        try {
            logger.debug("Waiting for element to be present: {}", locator);
            WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
            logger.debug("Element is now present: {}", locator);
            return element;
        } catch (TimeoutException e) {
            logger.error("Timeout waiting for element to be present: {}", locator);
            throw new TimeoutException("Element not present after " + defaultTimeout + " seconds: " + locator, e);
        }
    }
    
    /**
     * Waits for all elements to be visible
     * 
     * @param locator the elements locator
     * @return list of visible WebElements
     */
    public List<WebElement> waitForAllElementsVisible(By locator) {
        try {
            logger.debug("Waiting for all elements to be visible: {}", locator);
            List<WebElement> elements = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
            logger.debug("All elements are now visible: {}, count: {}", locator, elements.size());
            return elements;
        } catch (TimeoutException e) {
            logger.error("Timeout waiting for all elements to be visible: {}", locator);
            throw new TimeoutException("Elements not visible after " + defaultTimeout + " seconds: " + locator, e);
        }
    }
    
    /**
     * Waits for element to be invisible
     * 
     * @param locator the element locator
     * @return true if element becomes invisible
     */
    public boolean waitForElementInvisible(By locator) {
        try {
            logger.debug("Waiting for element to be invisible: {}", locator);
            boolean invisible = wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
            logger.debug("Element is now invisible: {}", locator);
            return invisible;
        } catch (TimeoutException e) {
            logger.error("Timeout waiting for element to be invisible: {}", locator);
            throw new TimeoutException("Element still visible after " + defaultTimeout + " seconds: " + locator, e);
        }
    }
    
    /**
     * Waits for text to be present in element
     * 
     * @param locator the element locator
     * @param text the expected text
     * @return true if text is present
     */
    public boolean waitForTextInElement(By locator, String text) {
        try {
            logger.debug("Waiting for text '{}' in element: {}", text, locator);
            boolean present = wait.until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
            logger.debug("Text '{}' is now present in element: {}", text, locator);
            return present;
        } catch (TimeoutException e) {
            logger.error("Timeout waiting for text '{}' in element: {}", text, locator);
            throw new TimeoutException("Text '" + text + "' not present after " + defaultTimeout + " seconds: " + locator, e);
        }
    }
    
    /**
     * Waits for attribute to contain specific value
     * 
     * @param locator the element locator
     * @param attribute the attribute name
     * @param value the expected attribute value
     * @return true if attribute contains value
     */
    public boolean waitForAttributeContains(By locator, String attribute, String value) {
        try {
            logger.debug("Waiting for attribute '{}' to contain '{}' in element: {}", attribute, value, locator);
            boolean contains = wait.until(ExpectedConditions.attributeContains(locator, attribute, value));
            logger.debug("Attribute '{}' now contains '{}' in element: {}", attribute, value, locator);
            return contains;
        } catch (TimeoutException e) {
            logger.error("Timeout waiting for attribute '{}' to contain '{}' in element: {}", attribute, value, locator);
            throw new TimeoutException("Attribute '" + attribute + "' does not contain '" + value + "' after " + defaultTimeout + " seconds", e);
        }
    }
    
    /**
     * Waits for frame to be available and switches to it
     * 
     * @param locator the frame locator
     * @return the WebDriver focused on the frame
     */
    public WebDriver waitForFrameAndSwitch(By locator) {
        try {
            logger.debug("Waiting for frame and switching: {}", locator);
            WebDriver frame = wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(locator));
            logger.debug("Switched to frame: {}", locator);
            return frame;
        } catch (TimeoutException e) {
            logger.error("Timeout waiting for frame: {}", locator);
            throw new TimeoutException("Frame not available after " + defaultTimeout + " seconds: " + locator, e);
        }
    }
    
    /**
     * Waits for alert to be present
     * 
     * @return the Alert object
     */
    public Alert waitForAlert() {
        try {
            logger.debug("Waiting for alert to be present");
            Alert alert = wait.until(ExpectedConditions.alertIsPresent());
            logger.debug("Alert is now present");
            return alert;
        } catch (TimeoutException e) {
            logger.error("Timeout waiting for alert");
            throw new TimeoutException("Alert not present after " + defaultTimeout + " seconds", e);
        }
    }
    
    /**
     * Waits for number of windows to be a specific count
     * 
     * @param numberOfWindows expected number of windows
     * @return true if window count matches
     */
    public boolean waitForNumberOfWindows(int numberOfWindows) {
        try {
            logger.debug("Waiting for number of windows to be: {}", numberOfWindows);
            boolean result = wait.until(ExpectedConditions.numberOfWindowsToBe(numberOfWindows));
            logger.debug("Number of windows is now: {}", numberOfWindows);
            return result;
        } catch (TimeoutException e) {
            logger.error("Timeout waiting for number of windows to be: {}", numberOfWindows);
            throw new TimeoutException("Window count not " + numberOfWindows + " after " + defaultTimeout + " seconds", e);
        }
    }
    
    /**
     * Creates a fluent wait with custom configuration
     * 
     * @param timeout timeout in seconds
     * @param pollingTime polling interval in milliseconds
     * @return configured FluentWait instance
     */
    public FluentWait<WebDriver> createFluentWait(int timeout, int pollingTime) {
        logger.debug("Creating fluent wait with timeout: {} seconds, polling: {} ms", timeout, pollingTime);
        return new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(timeout))
                .pollingEvery(Duration.ofMillis(pollingTime))
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class);
    }
    
    /**
     * Fluent wait for element to be visible
     * 
     * @param locator the element locator
     * @param timeout timeout in seconds
     * @return the visible WebElement
     */
    public WebElement fluentWaitForElementVisible(By locator, int timeout) {
        try {
            logger.debug("Fluent wait for element to be visible: {}", locator);
            FluentWait<WebDriver> fluentWait = createFluentWait(timeout, pollingInterval);
            return fluentWait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        } catch (TimeoutException e) {
            logger.error("Fluent wait timeout for element visibility: {}", locator);
            throw new TimeoutException("Element not visible after " + timeout + " seconds: " + locator, e);
        }
    }
    
    /**
     * Fluent wait for element to be clickable
     * 
     * @param locator the element locator
     * @param timeout timeout in seconds
     * @return the clickable WebElement
     */
    public WebElement fluentWaitForElementClickable(By locator, int timeout) {
        try {
            logger.debug("Fluent wait for element to be clickable: {}", locator);
            FluentWait<WebDriver> fluentWait = createFluentWait(timeout, pollingInterval);
            return fluentWait.until(ExpectedConditions.elementToBeClickable(locator));
        } catch (TimeoutException e) {
            logger.error("Fluent wait timeout for element clickable: {}", locator);
            throw new TimeoutException("Element not clickable after " + timeout + " seconds: " + locator, e);
        }
    }
    
    /**
     * Fluent wait with custom condition
     * 
     * @param condition the custom ExpectedCondition
     * @param timeout timeout in seconds
     * @param <T> the type returned by the condition
     * @return the result of the condition
     */
    public <T> T fluentWaitForCondition(Function<WebDriver, T> condition, int timeout) {
        try {
            logger.debug("Fluent wait for custom condition with timeout: {} seconds", timeout);
            FluentWait<WebDriver> fluentWait = createFluentWait(timeout, pollingInterval);
            return fluentWait.until(condition);
        } catch (TimeoutException e) {
            logger.error("Fluent wait timeout for custom condition");
            throw new TimeoutException("Custom condition not met after " + timeout + " seconds", e);
        }
    }
    
    /**
     * Waits for page to load completely
     * Uses JavaScript to check document ready state
     * 
     * @return true if page loaded successfully
     */
    public boolean waitForPageLoad() {
        try {
            logger.debug("Waiting for page to load");
            ExpectedCondition<Boolean> pageLoadCondition = driver -> {
                JavascriptExecutor js = (JavascriptExecutor) driver;
                return js.executeScript("return document.readyState").equals("complete");
            };
            boolean loaded = wait.until(pageLoadCondition);
            logger.debug("Page load completed");
            return loaded;
        } catch (TimeoutException e) {
            logger.error("Timeout waiting for page to load");
            throw new TimeoutException("Page not loaded after " + defaultTimeout + " seconds", e);
        }
    }
    
    /**
     * Waits for jQuery to complete (if jQuery is present)
     * 
     * @return true if jQuery is inactive or not present
     */
    public boolean waitForJQueryLoad() {
        try {
            logger.debug("Waiting for jQuery to complete");
            ExpectedCondition<Boolean> jQueryLoad = driver -> {
                try {
                    JavascriptExecutor js = (JavascriptExecutor) driver;
                    return (Boolean) js.executeScript("return jQuery.active == 0");
                } catch (Exception e) {
                    // jQuery not present, return true
                    return true;
                }
            };
            boolean loaded = wait.until(jQueryLoad);
            logger.debug("jQuery load completed");
            return loaded;
        } catch (TimeoutException e) {
            logger.error("Timeout waiting for jQuery to complete");
            throw new TimeoutException("jQuery still active after " + defaultTimeout + " seconds", e);
        }
    }
    
    /**
     * Waits for Angular to complete (if Angular is present)
     * 
     * @return true if Angular is inactive or not present
     */
    public boolean waitForAngularLoad() {
        try {
            logger.debug("Waiting for Angular to complete");
            ExpectedCondition<Boolean> angularLoad = driver -> {
                try {
                    JavascriptExecutor js = (JavascriptExecutor) driver;
                    return (Boolean) js.executeScript("return window.getAllAngularTestabilities().findIndex(x=>!x.isStable()) === -1");
                } catch (Exception e) {
                    // Angular not present, return true
                    return true;
                }
            };
            boolean loaded = wait.until(angularLoad);
            logger.debug("Angular load completed");
            return loaded;
        } catch (TimeoutException e) {
            logger.error("Timeout waiting for Angular to complete");
            throw new TimeoutException("Angular still active after " + defaultTimeout + " seconds", e);
        }
    }
    
    /**
     * Static method to create explicit wait with custom timeout
     * 
     * @param driver the WebDriver instance
     * @param timeout timeout in seconds
     * @return WebDriverWait instance
     */
    public static WebDriverWait createExplicitWait(WebDriver driver, int timeout) {
        return new WebDriverWait(driver, Duration.ofSeconds(timeout));
    }
    
    /**
     * Hard wait (Thread.sleep) - use sparingly, prefer explicit waits
     * 
     * @param seconds seconds to wait
     */
    public void hardWait(int seconds) {
        try {
            logger.debug("Hard wait for {} seconds", seconds);
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException e) {
            logger.error("Hard wait interrupted", e);
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * Waits for page to complete loading (document.readyState === "complete")
     * Uses JavaScript executor to check if page load is complete
     */
    public void waitForPageToLoad() {
        try {
            logger.debug("Waiting for page to load completely");
            wait.until((ExpectedCondition<Boolean>) driver -> {
                if (driver instanceof JavascriptExecutor) {
                    JavascriptExecutor js = (JavascriptExecutor) driver;
                    return js.executeScript("return document.readyState").equals("complete");
                }
                return true;
            });
            logger.debug("Page loaded successfully");
        } catch (TimeoutException e) {
            logger.error("Timeout waiting for page to load");
            throw new TimeoutException("Page did not load after " + defaultTimeout + " seconds", e);
        }
    }
}
