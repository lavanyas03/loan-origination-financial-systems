package pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * DashboardPage represents the main dashboard page and its navigation elements.
 * This page object provides navigation methods to different sections of the application,
 * including Customer Registration, Loan Processing, and other key features.
 */
public class DashboardPage extends BasePage {
    
    private static final Logger logger = LogManager.getLogger(DashboardPage.class);
    
    // Page URL
    private static final String PAGE_URL = "/dashboard";
    
    // Dashboard Locators
    private final By dashboardHeader = By.cssSelector("h1.dashboard-header, .dashboard-title");
    private final By welcomeMessage = By.cssSelector(".welcome-message, .user-greeting");
    
    // Navigation Menu Locators
    private final By customerRegistrationLink = By.cssSelector("a[href*='customer/registration'], " +
            "#customerRegistration, .customer-registration-link, " +
            "nav a:has-text('Customer Registration')");
    private final By loanApplicationLink = By.cssSelector("a[href*='loan/application'], " +
            "#loanApplication, .loan-application-link");
    private final By paymentProcessingLink = By.cssSelector("a[href*='payment/processing'], " +
            "#paymentProcessing, .payment-processing-link");
    private final By customerSearchLink = By.cssSelector("a[href*='customer/search'], " +
            "#customerSearch, .customer-search-link");
    private final By reportsLink = By.cssSelector("a[href*='reports'], " +
            "#reports, .reports-link");
    
    // Alternative navigation locators (menu items, buttons)
    private final By customerMenu = By.cssSelector(".customer-menu, #customerMenu");
    private final By loanMenu = By.cssSelector(".loan-menu, #loanMenu");
    private final By paymentMenu = By.cssSelector(".payment-menu, #paymentMenu");
    
    // Dashboard Widgets/Cards
    private final By totalCustomersWidget = By.cssSelector(".total-customers-widget, #totalCustomers");
    private final By activeLoansWidget = By.cssSelector(".active-loans-widget, #activeLoans");
    private final By pendingApplicationsWidget = By.cssSelector(".pending-applications-widget, #pendingApplications");
    
    /**
     * Constructor
     * 
     * @param driver WebDriver instance
     */
    public DashboardPage(WebDriver driver) {
        super(driver);
        logger.info("DashboardPage initialized");
    }
    
    /**
     * Constructor with custom timeout
     * 
     * @param driver WebDriver instance
     * @param timeout custom timeout in seconds
     */
    public DashboardPage(WebDriver driver, int timeout) {
        super(driver, timeout);
        logger.info("DashboardPage initialized with timeout: {} seconds", timeout);
    }
    
    // ==================== Navigation Methods ====================
    
    /**
     * Navigates to Dashboard page
     * 
     * @param baseUrl the base application URL
     */
    public void navigateToDashboard(String baseUrl) {
        logger.info("Navigating to Dashboard page");
        driver.get(baseUrl + PAGE_URL);
        waitForPageToLoad();
    }
    
    /**
     * Waits for Dashboard page to load completely
     */
    public void waitForPageToLoad() {
        logger.debug("Waiting for Dashboard page to load");
        waitForElement(dashboardHeader);
        logger.info("Dashboard page loaded successfully");
    }
    
    /**
     * Navigates to Customer Registration page from Dashboard
     * This method clicks on the Customer Registration link/button
     * 
     * @return CustomerRegistrationPage instance for method chaining
     */
    public CustomerRegistrationPage navigateToCustomerRegistration() {
        logger.info("Navigating to Customer Registration page from Dashboard");
        try {
            // Try clicking the direct link first
            click(customerRegistrationLink);
            logger.info("Successfully navigated to Customer Registration page");
        } catch (Exception e) {
            logger.warn("Direct link not found, attempting alternative navigation method");
            try {
                // Try alternative: click customer menu then registration
                click(customerMenu);
                click(customerRegistrationLink);
                logger.info("Successfully navigated to Customer Registration via menu");
            } catch (Exception ex) {
                logger.error("Failed to navigate to Customer Registration page", ex);
                throw new RuntimeException("Unable to navigate to Customer Registration page", ex);
            }
        }
        return new CustomerRegistrationPage(driver);
    }
    
    /**
     * Navigates to Loan Application page from Dashboard
     */
    public void navigateToLoanApplication() {
        logger.info("Navigating to Loan Application page from Dashboard");
        try {
            click(loanApplicationLink);
            logger.info("Successfully navigated to Loan Application page");
        } catch (Exception e) {
            logger.warn("Direct link not found, attempting alternative navigation");
            click(loanMenu);
            click(loanApplicationLink);
            logger.info("Successfully navigated to Loan Application via menu");
        }
    }
    
    /**
     * Navigates to Payment Processing page from Dashboard
     */
    public void navigateToPaymentProcessing() {
        logger.info("Navigating to Payment Processing page from Dashboard");
        try {
            click(paymentProcessingLink);
            logger.info("Successfully navigated to Payment Processing page");
        } catch (Exception e) {
            logger.warn("Direct link not found, attempting alternative navigation");
            click(paymentMenu);
            click(paymentProcessingLink);
            logger.info("Successfully navigated to Payment Processing via menu");
        }
    }
    
    /**
     * Navigates to Customer Search page from Dashboard
     */
    public void navigateToCustomerSearch() {
        logger.info("Navigating to Customer Search page from Dashboard");
        click(customerSearchLink);
        logger.info("Successfully navigated to Customer Search page");
    }
    
    /**
     * Navigates to Reports page from Dashboard
     */
    public void navigateToReports() {
        logger.info("Navigating to Reports page from Dashboard");
        click(reportsLink);
        logger.info("Successfully navigated to Reports page");
    }
    
    // ==================== Validation Methods ====================
    
    /**
     * Checks if Dashboard header is displayed
     * 
     * @return true if Dashboard header is displayed
     */
    public boolean isDashboardHeaderDisplayed() {
        logger.debug("Checking if Dashboard header is displayed");
        return isDisplayed(dashboardHeader);
    }
    
    /**
     * Gets the Dashboard header text
     * 
     * @return the Dashboard header text
     */
    public String getDashboardHeaderText() {
        logger.debug("Getting Dashboard header text");
        return getText(dashboardHeader);
    }
    
    /**
     * Checks if welcome message is displayed
     * 
     * @return true if welcome message is displayed
     */
    public boolean isWelcomeMessageDisplayed() {
        logger.debug("Checking if welcome message is displayed");
        return isDisplayed(welcomeMessage);
    }
    
    /**
     * Gets the welcome message text
     * 
     * @return the welcome message text
     */
    public String getWelcomeMessageText() {
        logger.debug("Getting welcome message text");
        return getText(welcomeMessage);
    }
    
    /**
     * Checks if Customer Registration link is displayed
     * 
     * @return true if Customer Registration link is displayed
     */
    public boolean isCustomerRegistrationLinkDisplayed() {
        logger.debug("Checking if Customer Registration link is displayed");
        return isDisplayed(customerRegistrationLink);
    }
    
    /**
     * Checks if Loan Application link is displayed
     * 
     * @return true if Loan Application link is displayed
     */
    public boolean isLoanApplicationLinkDisplayed() {
        logger.debug("Checking if Loan Application link is displayed");
        return isDisplayed(loanApplicationLink);
    }
    
    /**
     * Checks if Payment Processing link is displayed
     * 
     * @return true if Payment Processing link is displayed
     */
    public boolean isPaymentProcessingLinkDisplayed() {
        logger.debug("Checking if Payment Processing link is displayed");
        return isDisplayed(paymentProcessingLink);
    }
    
    /**
     * Checks if all main navigation links are displayed
     * 
     * @return true if all main navigation links are displayed
     */
    public boolean areAllNavigationLinksDisplayed() {
        logger.debug("Checking if all main navigation links are displayed");
        return isCustomerRegistrationLinkDisplayed() 
            && isLoanApplicationLinkDisplayed() 
            && isPaymentProcessingLinkDisplayed();
    }
    
    // ==================== Dashboard Widget Methods ====================
    
    /**
     * Checks if Total Customers widget is displayed
     * 
     * @return true if Total Customers widget is displayed
     */
    public boolean isTotalCustomersWidgetDisplayed() {
        logger.debug("Checking if Total Customers widget is displayed");
        return isDisplayed(totalCustomersWidget);
    }
    
    /**
     * Gets the Total Customers count from the widget
     * 
     * @return the total customers count
     */
    public String getTotalCustomersCount() {
        logger.debug("Getting Total Customers count");
        return getText(totalCustomersWidget);
    }
    
    /**
     * Checks if Active Loans widget is displayed
     * 
     * @return true if Active Loans widget is displayed
     */
    public boolean isActiveLoansWidgetDisplayed() {
        logger.debug("Checking if Active Loans widget is displayed");
        return isDisplayed(activeLoansWidget);
    }
    
    /**
     * Gets the Active Loans count from the widget
     * 
     * @return the active loans count
     */
    public String getActiveLoansCount() {
        logger.debug("Getting Active Loans count");
        return getText(activeLoansWidget);
    }
    
    /**
     * Checks if Pending Applications widget is displayed
     * 
     * @return true if Pending Applications widget is displayed
     */
    public boolean isPendingApplicationsWidgetDisplayed() {
        logger.debug("Checking if Pending Applications widget is displayed");
        return isDisplayed(pendingApplicationsWidget);
    }
    
    /**
     * Gets the Pending Applications count from the widget
     * 
     * @return the pending applications count
     */
    public String getPendingApplicationsCount() {
        logger.debug("Getting Pending Applications count");
        return getText(pendingApplicationsWidget);
    }
}
