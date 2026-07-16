package pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * GoogleSearchPage represents the Google search page and its elements.
 * This is a sample Page Object that demonstrates the Page Object Model pattern
 * and uses BasePage reusable methods.
 */
public class GoogleSearchPage extends BasePage {
    
    private static final Logger logger = LogManager.getLogger(GoogleSearchPage.class);
    
    // Page URL
    private static final String PAGE_URL = "https://www.google.com";
    
    // Locators
    private final By searchBox = By.name("q");
    private final By searchButton = By.name("btnK");
    private final By luckyButton = By.name("btnI");
    private final By googleLogo = By.cssSelector("img[alt*='Google'], a[aria-label*='Google']");
    private final By searchResults = By.id("search");
    private final By resultStats = By.id("result-stats");
    private final By anySearchResult = By.cssSelector("#search .g, #rso .g");
    
    /**
     * Constructor
     * 
     * @param driver WebDriver instance
     */
    public GoogleSearchPage(WebDriver driver) {
        super(driver);
        logger.info("GoogleSearchPage initialized");
    }
    
    /**
     * Constructor with custom timeout
     * 
     * @param driver WebDriver instance
     * @param timeout custom timeout in seconds
     */
    public GoogleSearchPage(WebDriver driver, int timeout) {
        super(driver, timeout);
        logger.info("GoogleSearchPage initialized with timeout: {} seconds", timeout);
    }
    
    /**
     * Navigates to Google search page
     */
    public void navigateToGoogle() {
        logger.info("Navigating to Google search page");
        driver.get(PAGE_URL);
        waitForPageToLoad();
    }
    
    /**
     * Waits for page to load completely
     */
    public void waitForPageToLoad() {
        logger.debug("Waiting for Google search page to load");
        waitForElement(searchBox);
        logger.info("Google search page loaded successfully");
    }
    
    /**
     * Checks if Google logo is displayed
     * 
     * @return true if logo is displayed
     */
    public boolean isGoogleLogoDisplayed() {
        logger.debug("Checking if Google logo is displayed");
        try {
            // Try multiple selectors for the Google logo
            return isDisplayed(googleLogo) || isDisplayed(By.cssSelector("div[aria-label*='Google']"));
        } catch (Exception e) {
            logger.debug("Google logo check - trying alternative approach");
            return getPageTitle().contains("Google");
        }
    }
    
    /**
     * Checks if search box is displayed
     * 
     * @return true if search box is displayed
     */
    public boolean isSearchBoxDisplayed() {
        logger.debug("Checking if search box is displayed");
        return isDisplayed(searchBox);
    }
    
    /**
     * Checks if search button is displayed
     * 
     * @return true if search button is displayed
     */
    public boolean isSearchButtonDisplayed() {
        logger.debug("Checking if search button is displayed");
        // Google search button might not be visible initially, need to type first
        try {
            return isDisplayed(searchButton);
        } catch (Exception e) {
            logger.debug("Search button not visible (this is expected before typing)");
            return false;
        }
    }
    
    /**
     * Types search query in the search box
     * 
     * @param query the search query
     */
    public void enterSearchQuery(String query) {
        logger.info("Entering search query: {}", query);
        type(searchBox, query);
    }
    
    /**
     * Clicks the search button
     */
    public void clickSearchButton() {
        logger.info("Clicking search button");
        // Wait a moment for button to become visible after typing
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        click(searchButton);
    }
    
    /**
     * Performs a search by entering query and clicking search
     * 
     * @param query the search query
     */
    public void performSearch(String query) {
        logger.info("Performing search for: {}", query);
        enterSearchQuery(query);
        
        // Submit using Enter key instead of clicking button (more reliable)
        typeWithoutClear(searchBox, "\n");
        
        logger.info("Search submitted successfully");
    }
    
    /**
     * Gets the search box placeholder text
     * 
     * @return placeholder text
     */
    public String getSearchBoxPlaceholder() {
        logger.debug("Getting search box placeholder");
        return getAttribute(searchBox, "title");
    }
    
    /**
     * Checks if search results are displayed
     * 
     * @return true if search results are displayed
     */
    public boolean areSearchResultsDisplayed() {
        logger.debug("Checking if search results are displayed");
        try {
            // Check for search results container or any actual result
            return isDisplayed(searchResults) || isDisplayed(anySearchResult);
        } catch (Exception e) {
            logger.debug("Search results not displayed");
            return false;
        }
    }
    
    /**
     * Gets the result statistics text
     * 
     * @return result stats text (e.g., "About 1,234 results")
     */
    public String getResultStats() {
        logger.debug("Getting result statistics");
        try {
            return getText(resultStats);
        } catch (Exception e) {
            logger.warn("Could not retrieve result statistics from result-stats element");
            // Try alternative - check if we have any results
            try {
                int resultCount = getElementCount(anySearchResult);
                return "Found " + resultCount + " result elements";
            } catch (Exception ex) {
                logger.warn("Could not count results either");
                return "Results available";
            }
        }
    }
    
    /**
     * Clears the search box
     */
    public void clearSearchBox() {
        logger.info("Clearing search box");
        waitForElement(searchBox).clear();
    }
    
    /**
     * Checks if the page title contains Google
     * 
     * @return true if title contains "Google"
     */
    public boolean isTitleCorrect() {
        String title = getPageTitle();
        logger.debug("Validating page title: {}", title);
        return title.contains("Google");
    }
    
    /**
     * Waits for search results to load
     * 
     * @return true if results loaded
     */
    public boolean waitForSearchResults() {
        logger.debug("Waiting for search results to load");
        try {
            // Wait for either search results container or actual results
            waitForElement(anySearchResult, 10);
            return true;
        } catch (Exception e) {
            logger.debug("Checking alternative for search results");
            try {
                waitForElement(searchResults, 5);
                return true;
            } catch (Exception ex) {
                logger.error("Search results did not load", ex);
                return false;
            }
        }
    }
}
