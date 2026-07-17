package pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * CustomerRegistrationPage represents the Customer Registration page and its elements.
 * This page object follows the Page Object Model pattern and provides reusable methods
 * for interacting with customer registration form fields.
 * 
 * Handles customer data entry including:
 * - Personal information (First Name, Last Name, Date of Birth, SSN)
 * - Contact information (Email, Phone Number)
 * - Address information (Street, City, State, Zip Code)
 * - Form actions (Submit, Clear, Reset, Cancel)
 */
public class CustomerRegistrationPage extends BasePage {
    
    private static final Logger logger = LogManager.getLogger(CustomerRegistrationPage.class);
    
    // Page URL
    private static final String PAGE_URL = "/customer/registration";
    
    // Personal Information Locators
    private final By firstNameField = By.id("firstName");
    private final By lastNameField = By.id("lastName");
    private final By dateOfBirthField = By.id("dateOfBirth");
    private final By ssnField = By.id("ssn");
    
    // Contact Information Locators
    private final By emailField = By.id("email");
    private final By phoneNumberField = By.id("phoneNumber");
    
    // Address Information Locators
    private final By streetAddressField = By.id("streetAddress");
    private final By addressLine2Field = By.id("addressLine2");
    private final By cityField = By.id("city");
    private final By stateDropdown = By.id("state");
    private final By zipCodeField = By.id("zipCode");
    private final By countryField = By.id("country");
    
    // Action Buttons Locators
    private final By submitButton = By.id("submitButton");
    private final By clearButton = By.id("clearButton");
    private final By resetButton = By.id("resetButton");
    private final By cancelButton = By.id("cancelButton");
    
    // Page Validation Locators
    private final By pageHeader = By.cssSelector("h1, .page-header");
    private final By registrationForm = By.id("customerRegistrationForm");
    private final By successMessage = By.cssSelector(".success-message, .alert-success");
    private final By errorMessage = By.cssSelector(".error-message, .alert-danger");
    
    // Field Validation Locators
    private final By firstNameError = By.id("firstNameError");
    private final By lastNameError = By.id("lastNameError");
    private final By dateOfBirthError = By.id("dateOfBirthError");
    private final By ssnError = By.id("ssnError");
    private final By emailError = By.id("emailError");
    private final By phoneNumberError = By.id("phoneNumberError");
    private final By addressError = By.id("addressError");
    
    /**
     * Constructor
     * 
     * @param driver WebDriver instance
     */
    public CustomerRegistrationPage(WebDriver driver) {
        super(driver);
        logger.info("CustomerRegistrationPage initialized");
    }
    
    /**
     * Constructor with custom timeout
     * 
     * @param driver WebDriver instance
     * @param timeout custom timeout in seconds
     */
    public CustomerRegistrationPage(WebDriver driver, int timeout) {
        super(driver, timeout);
        logger.info("CustomerRegistrationPage initialized with timeout: {} seconds", timeout);
    }
    
    // ==================== Navigation Methods ====================
    
    /**
     * Navigates to Customer Registration page using relative URL
     * 
     * @param baseUrl the base application URL
     */
    public void navigateToCustomerRegistration(String baseUrl) {
        logger.info("Navigating to Customer Registration page");
        driver.get(baseUrl + PAGE_URL);
        waitForPageToLoad();
    }
    
    /**
     * Waits for page to load completely
     */
    public void waitForPageToLoad() {
        logger.debug("Waiting for Customer Registration page to load");
        waitForElement(registrationForm);
        logger.info("Customer Registration page loaded successfully");
    }
    
    // ==================== Personal Information Methods ====================
    
    /**
     * Enters first name in the First Name field
     * 
     * @param firstName the first name to enter
     */
    public void enterFirstName(String firstName) {
        logger.info("Entering first name: {}", firstName);
        type(firstNameField, firstName);
    }
    
    /**
     * Enters last name in the Last Name field
     * 
     * @param lastName the last name to enter
     */
    public void enterLastName(String lastName) {
        logger.info("Entering last name: {}", lastName);
        type(lastNameField, lastName);
    }
    
    /**
     * Enters date of birth in the Date of Birth field
     * Format should match the expected format (e.g., MM/DD/YYYY or YYYY-MM-DD)
     * 
     * @param dateOfBirth the date of birth to enter
     */
    public void enterDateOfBirth(String dateOfBirth) {
        logger.info("Entering date of birth: {}", dateOfBirth);
        type(dateOfBirthField, dateOfBirth);
    }
    
    /**
     * Enters SSN in the SSN field
     * 
     * @param ssn the Social Security Number to enter
     */
    public void enterSSN(String ssn) {
        logger.info("Entering SSN (masked for security)");
        type(ssnField, ssn);
    }
    
    /**
     * Gets the value of the First Name field
     * 
     * @return the first name value
     */
    public String getFirstName() {
        logger.debug("Getting first name field value");
        return getAttribute(firstNameField, "value");
    }
    
    /**
     * Gets the value of the Last Name field
     * 
     * @return the last name value
     */
    public String getLastName() {
        logger.debug("Getting last name field value");
        return getAttribute(lastNameField, "value");
    }
    
    /**
     * Gets the value of the Date of Birth field
     * 
     * @return the date of birth value
     */
    public String getDateOfBirth() {
        logger.debug("Getting date of birth field value");
        return getAttribute(dateOfBirthField, "value");
    }
    
    /**
     * Gets the value of the SSN field
     * 
     * @return the SSN value
     */
    public String getSSN() {
        logger.debug("Getting SSN field value");
        return getAttribute(ssnField, "value");
    }
    
    // ==================== Contact Information Methods ====================
    
    /**
     * Enters email address in the Email field
     * 
     * @param email the email address to enter
     */
    public void enterEmail(String email) {
        logger.info("Entering email: {}", email);
        type(emailField, email);
    }
    
    /**
     * Enters phone number in the Phone Number field
     * 
     * @param phoneNumber the phone number to enter
     */
    public void enterPhoneNumber(String phoneNumber) {
        logger.info("Entering phone number: {}", phoneNumber);
        type(phoneNumberField, phoneNumber);
    }
    
    /**
     * Gets the value of the Email field
     * 
     * @return the email value
     */
    public String getEmail() {
        logger.debug("Getting email field value");
        return getAttribute(emailField, "value");
    }
    
    /**
     * Gets the value of the Phone Number field
     * 
     * @return the phone number value
     */
    public String getPhoneNumber() {
        logger.debug("Getting phone number field value");
        return getAttribute(phoneNumberField, "value");
    }
    
    // ==================== Address Information Methods ====================
    
    /**
     * Enters street address in the Street Address field
     * 
     * @param streetAddress the street address to enter
     */
    public void enterStreetAddress(String streetAddress) {
        logger.info("Entering street address: {}", streetAddress);
        type(streetAddressField, streetAddress);
    }
    
    /**
     * Enters address line 2 (apartment, suite, etc.)
     * 
     * @param addressLine2 the address line 2 to enter
     */
    public void enterAddressLine2(String addressLine2) {
        logger.info("Entering address line 2: {}", addressLine2);
        type(addressLine2Field, addressLine2);
    }
    
    /**
     * Enters city in the City field
     * 
     * @param city the city to enter
     */
    public void enterCity(String city) {
        logger.info("Entering city: {}", city);
        type(cityField, city);
    }
    
    /**
     * Selects state from the State dropdown
     * 
     * @param state the state to select
     */
    public void selectState(String state) {
        logger.info("Selecting state: {}", state);
        selectDropdown(stateDropdown, state);
    }
    
    /**
     * Selects state from the State dropdown by value
     * 
     * @param stateValue the state value to select
     */
    public void selectStateByValue(String stateValue) {
        logger.info("Selecting state by value: {}", stateValue);
        selectDropdownByValue(stateDropdown, stateValue);
    }
    
    /**
     * Enters zip code in the Zip Code field
     * 
     * @param zipCode the zip code to enter
     */
    public void enterZipCode(String zipCode) {
        logger.info("Entering zip code: {}", zipCode);
        type(zipCodeField, zipCode);
    }
    
    /**
     * Enters country in the Country field
     * 
     * @param country the country to enter
     */
    public void enterCountry(String country) {
        logger.info("Entering country: {}", country);
        type(countryField, country);
    }
    
    /**
     * Gets the value of the Street Address field
     * 
     * @return the street address value
     */
    public String getStreetAddress() {
        logger.debug("Getting street address field value");
        return getAttribute(streetAddressField, "value");
    }
    
    /**
     * Gets the value of the Address Line 2 field
     * 
     * @return the address line 2 value
     */
    public String getAddressLine2() {
        logger.debug("Getting address line 2 field value");
        return getAttribute(addressLine2Field, "value");
    }
    
    /**
     * Gets the value of the City field
     * 
     * @return the city value
     */
    public String getCity() {
        logger.debug("Getting city field value");
        return getAttribute(cityField, "value");
    }
    
    /**
     * Gets the selected state from the State dropdown
     * 
     * @return the selected state
     */
    public String getSelectedState() {
        logger.debug("Getting selected state value");
        return getSelectedDropdownText(stateDropdown);
    }
    
    /**
     * Gets the value of the Zip Code field
     * 
     * @return the zip code value
     */
    public String getZipCode() {
        logger.debug("Getting zip code field value");
        return getAttribute(zipCodeField, "value");
    }
    
    /**
     * Gets the value of the Country field
     * 
     * @return the country value
     */
    public String getCountry() {
        logger.debug("Getting country field value");
        return getAttribute(countryField, "value");
    }
    
    /**
     * Enters complete address information
     * 
     * @param streetAddress the street address
     * @param addressLine2 the address line 2 (optional, can be null or empty)
     * @param city the city
     * @param state the state
     * @param zipCode the zip code
     * @param country the country
     */
    public void enterCompleteAddress(String streetAddress, String addressLine2, 
                                     String city, String state, String zipCode, String country) {
        logger.info("Entering complete address information");
        enterStreetAddress(streetAddress);
        if (addressLine2 != null && !addressLine2.trim().isEmpty()) {
            enterAddressLine2(addressLine2);
        }
        enterCity(city);
        selectState(state);
        enterZipCode(zipCode);
        enterCountry(country);
        logger.info("Complete address information entered successfully");
    }
    
    // ==================== Complete Form Fill Method ====================
    
    /**
     * Fills the complete customer registration form with all required information
     * 
     * @param firstName the first name
     * @param lastName the last name
     * @param dateOfBirth the date of birth
     * @param ssn the Social Security Number
     * @param email the email address
     * @param phoneNumber the phone number
     * @param streetAddress the street address
     * @param city the city
     * @param state the state
     * @param zipCode the zip code
     */
    public void fillCustomerRegistrationForm(String firstName, String lastName, 
                                            String dateOfBirth, String ssn, 
                                            String email, String phoneNumber, 
                                            String streetAddress, String city, 
                                            String state, String zipCode) {
        logger.info("Filling customer registration form with all information");
        
        // Personal Information
        enterFirstName(firstName);
        enterLastName(lastName);
        enterDateOfBirth(dateOfBirth);
        enterSSN(ssn);
        
        // Contact Information
        enterEmail(email);
        enterPhoneNumber(phoneNumber);
        
        // Address Information
        enterStreetAddress(streetAddress);
        enterCity(city);
        selectState(state);
        enterZipCode(zipCode);
        
        logger.info("Customer registration form filled successfully");
    }
    
    /**
     * Fills the complete customer registration form including optional fields
     * 
     * @param firstName the first name
     * @param lastName the last name
     * @param dateOfBirth the date of birth
     * @param ssn the Social Security Number
     * @param email the email address
     * @param phoneNumber the phone number
     * @param streetAddress the street address
     * @param addressLine2 the address line 2 (optional)
     * @param city the city
     * @param state the state
     * @param zipCode the zip code
     * @param country the country
     */
    public void fillCompleteCustomerRegistrationForm(String firstName, String lastName, 
                                                     String dateOfBirth, String ssn, 
                                                     String email, String phoneNumber, 
                                                     String streetAddress, String addressLine2,
                                                     String city, String state, 
                                                     String zipCode, String country) {
        logger.info("Filling complete customer registration form with all fields");
        
        // Personal Information
        enterFirstName(firstName);
        enterLastName(lastName);
        enterDateOfBirth(dateOfBirth);
        enterSSN(ssn);
        
        // Contact Information
        enterEmail(email);
        enterPhoneNumber(phoneNumber);
        
        // Address Information
        enterCompleteAddress(streetAddress, addressLine2, city, state, zipCode, country);
        
        logger.info("Complete customer registration form filled successfully");
    }
    
    // ==================== Action Button Methods ====================
    
    /**
     * Clicks the Submit button to submit the registration form
     */
    public void clickSubmitButton() {
        logger.info("Clicking Submit button");
        click(submitButton);
        logger.info("Submit button clicked successfully");
    }
    
    /**
     * Clicks the Clear button to clear all form fields
     */
    public void clickClearButton() {
        logger.info("Clicking Clear button");
        click(clearButton);
        logger.info("Clear button clicked successfully");
    }
    
    /**
     * Clicks the Reset button to reset form to default values
     */
    public void clickResetButton() {
        logger.info("Clicking Reset button");
        click(resetButton);
        logger.info("Reset button clicked successfully");
    }
    
    /**
     * Clicks the Cancel button to cancel the registration
     */
    public void clickCancelButton() {
        logger.info("Clicking Cancel button");
        click(cancelButton);
        logger.info("Cancel button clicked successfully");
    }
    
    // ==================== Validation Methods ====================
    
    /**
     * Checks if the registration form is displayed
     * 
     * @return true if registration form is displayed
     */
    public boolean isRegistrationFormDisplayed() {
        logger.debug("Checking if registration form is displayed");
        return isDisplayed(registrationForm);
    }
    
    /**
     * Checks if the page header is displayed
     * 
     * @return true if page header is displayed
     */
    public boolean isPageHeaderDisplayed() {
        logger.debug("Checking if page header is displayed");
        return isDisplayed(pageHeader);
    }
    
    /**
     * Gets the page header text
     * 
     * @return the page header text
     */
    public String getPageHeaderText() {
        logger.debug("Getting page header text");
        return getText(pageHeader);
    }
    
    /**
     * Checks if the Submit button is displayed
     * 
     * @return true if Submit button is displayed
     */
    public boolean isSubmitButtonDisplayed() {
        logger.debug("Checking if Submit button is displayed");
        return isDisplayed(submitButton);
    }
    
    /**
     * Checks if the Submit button is enabled
     * 
     * @return true if Submit button is enabled
     */
    public boolean isSubmitButtonEnabled() {
        logger.debug("Checking if Submit button is enabled");
        return isEnabled(submitButton);
    }
    
    /**
     * Checks if the Clear button is displayed
     * 
     * @return true if Clear button is displayed
     */
    public boolean isClearButtonDisplayed() {
        logger.debug("Checking if Clear button is displayed");
        return isDisplayed(clearButton);
    }
    
    /**
     * Checks if the Reset button is displayed
     * 
     * @return true if Reset button is displayed
     */
    public boolean isResetButtonDisplayed() {
        logger.debug("Checking if Reset button is displayed");
        return isDisplayed(resetButton);
    }
    
    /**
     * Checks if the Cancel button is displayed
     * 
     * @return true if Cancel button is displayed
     */
    public boolean isCancelButtonDisplayed() {
        logger.debug("Checking if Cancel button is displayed");
        return isDisplayed(cancelButton);
    }
    
    /**
     * Checks if success message is displayed after form submission
     * 
     * @return true if success message is displayed
     */
    public boolean isSuccessMessageDisplayed() {
        logger.debug("Checking if success message is displayed");
        return isDisplayed(successMessage);
    }
    
    /**
     * Gets the success message text
     * 
     * @return the success message text
     */
    public String getSuccessMessageText() {
        logger.debug("Getting success message text");
        return getText(successMessage);
    }
    
    /**
     * Checks if error message is displayed
     * 
     * @return true if error message is displayed
     */
    public boolean isErrorMessageDisplayed() {
        logger.debug("Checking if error message is displayed");
        return isDisplayed(errorMessage);
    }
    
    /**
     * Gets the error message text
     * 
     * @return the error message text
     */
    public String getErrorMessageText() {
        logger.debug("Getting error message text");
        return getText(errorMessage);
    }
    
    /**
     * Checks if a specific field has validation error displayed
     * 
     * @param fieldName the name of the field (firstName, lastName, email, etc.)
     * @return true if field error is displayed
     */
    public boolean isFieldErrorDisplayed(String fieldName) {
        logger.debug("Checking if {} field error is displayed", fieldName);
        By errorLocator = switch (fieldName.toLowerCase()) {
            case "firstname", "first name" -> firstNameError;
            case "lastname", "last name" -> lastNameError;
            case "dateofbirth", "date of birth", "dob" -> dateOfBirthError;
            case "ssn" -> ssnError;
            case "email" -> emailError;
            case "phonenumber", "phone number", "phone" -> phoneNumberError;
            case "address" -> addressError;
            default -> {
                logger.warn("Unknown field name: {}", fieldName);
                yield null;
            }
        };
        
        if (errorLocator != null) {
            return isDisplayed(errorLocator);
        }
        return false;
    }
    
    /**
     * Gets the validation error text for a specific field
     * 
     * @param fieldName the name of the field (firstName, lastName, email, etc.)
     * @return the field error text
     */
    public String getFieldErrorText(String fieldName) {
        logger.debug("Getting {} field error text", fieldName);
        By errorLocator = switch (fieldName.toLowerCase()) {
            case "firstname", "first name" -> firstNameError;
            case "lastname", "last name" -> lastNameError;
            case "dateofbirth", "date of birth", "dob" -> dateOfBirthError;
            case "ssn" -> ssnError;
            case "email" -> emailError;
            case "phonenumber", "phone number", "phone" -> phoneNumberError;
            case "address" -> addressError;
            default -> {
                logger.warn("Unknown field name: {}", fieldName);
                yield null;
            }
        };
        
        if (errorLocator != null) {
            return getText(errorLocator);
        }
        return "";
    }
    
    /**
     * Waits for the success message to appear after form submission
     * 
     * @return true if success message appears within timeout
     */
    public boolean waitForSuccessMessage() {
        logger.debug("Waiting for success message to appear");
        try {
            waitForElement(successMessage, 10);
            logger.info("Success message appeared");
            return true;
        } catch (Exception e) {
            logger.warn("Success message did not appear within timeout");
            return false;
        }
    }
    
    /**
     * Waits for the error message to appear
     * 
     * @return true if error message appears within timeout
     */
    public boolean waitForErrorMessage() {
        logger.debug("Waiting for error message to appear");
        try {
            waitForElement(errorMessage, 10);
            logger.info("Error message appeared");
            return true;
        } catch (Exception e) {
            logger.warn("Error message did not appear within timeout");
            return false;
        }
    }
    
    // ==================== Field Validation Methods ====================
    
    /**
     * Checks if First Name field is displayed
     * 
     * @return true if First Name field is displayed
     */
    public boolean isFirstNameFieldDisplayed() {
        logger.debug("Checking if First Name field is displayed");
        return isDisplayed(firstNameField);
    }
    
    /**
     * Checks if Last Name field is displayed
     * 
     * @return true if Last Name field is displayed
     */
    public boolean isLastNameFieldDisplayed() {
        logger.debug("Checking if Last Name field is displayed");
        return isDisplayed(lastNameField);
    }
    
    /**
     * Checks if Email field is displayed
     * 
     * @return true if Email field is displayed
     */
    public boolean isEmailFieldDisplayed() {
        logger.debug("Checking if Email field is displayed");
        return isDisplayed(emailField);
    }
    
    /**
     * Checks if Phone Number field is displayed
     * 
     * @return true if Phone Number field is displayed
     */
    public boolean isPhoneNumberFieldDisplayed() {
        logger.debug("Checking if Phone Number field is displayed");
        return isDisplayed(phoneNumberField);
    }
    
    /**
     * Checks if all required fields are displayed on the page
     * 
     * @return true if all required fields are displayed
     */
    public boolean areAllRequiredFieldsDisplayed() {
        logger.debug("Checking if all required fields are displayed");
        return isFirstNameFieldDisplayed() 
            && isLastNameFieldDisplayed() 
            && isDisplayed(dateOfBirthField)
            && isDisplayed(ssnField)
            && isEmailFieldDisplayed() 
            && isPhoneNumberFieldDisplayed()
            && isDisplayed(streetAddressField)
            && isDisplayed(cityField)
            && isDisplayed(stateDropdown)
            && isDisplayed(zipCodeField);
    }
}
