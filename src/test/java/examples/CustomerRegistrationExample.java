package examples;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.CustomerRegistrationPage;
import pages.DashboardPage;
import utils.ConfigReader;

/**
 * CustomerRegistrationExample demonstrates how to use the CustomerRegistrationPage
 * and DashboardPage page objects for customer registration workflows.
 * 
 * This is a reference implementation showing various ways to interact with
 * the customer registration form using the Page Object Model pattern.
 * 
 * Note: This example assumes the application is accessible. Update the base URL
 * in config.properties before running.
 */
public class CustomerRegistrationExample extends BaseTest {
    
    /**
     * Example 1: Basic navigation to Customer Registration page
     * Demonstrates how to navigate from Dashboard to Customer Registration
     */
    @Test(priority = 1, enabled = false)
    public void example1_NavigateToCustomerRegistration() {
        // Get base URL from config
        String baseUrl = ConfigReader.getInstance().getProperty("baseUrl");
        
        // Initialize Dashboard page
        DashboardPage dashboardPage = new DashboardPage(driver);
        dashboardPage.navigateToDashboard(baseUrl);
        
        // Verify dashboard loaded
        Assert.assertTrue(dashboardPage.isDashboardHeaderDisplayed(), 
            "Dashboard header should be displayed");
        
        // Navigate to Customer Registration page
        CustomerRegistrationPage registrationPage = dashboardPage.navigateToCustomerRegistration();
        
        // Verify registration page loaded
        Assert.assertTrue(registrationPage.isRegistrationFormDisplayed(), 
            "Customer Registration form should be displayed");
    }
    
    /**
     * Example 2: Fill individual form fields one by one
     * Demonstrates granular control over form field population
     */
    @Test(priority = 2, enabled = false)
    public void example2_FillIndividualFields() {
        // Navigate to registration page
        String baseUrl = ConfigReader.getInstance().getProperty("baseUrl");
        DashboardPage dashboardPage = new DashboardPage(driver);
        CustomerRegistrationPage registrationPage = dashboardPage.navigateToCustomerRegistration();
        
        // Fill personal information
        registrationPage.enterFirstName("John");
        registrationPage.enterLastName("Doe");
        registrationPage.enterDateOfBirth("01/15/1990");
        registrationPage.enterSSN("123-45-6789");
        
        // Fill contact information
        registrationPage.enterEmail("john.doe@example.com");
        registrationPage.enterPhoneNumber("(555) 123-4567");
        
        // Fill address information
        registrationPage.enterStreetAddress("123 Main Street");
        registrationPage.enterAddressLine2("Apt 4B");
        registrationPage.enterCity("New York");
        registrationPage.selectState("New York");
        registrationPage.enterZipCode("10001");
        registrationPage.enterCountry("USA");
        
        // Verify fields are populated
        Assert.assertEquals(registrationPage.getFirstName(), "John", 
            "First name should be populated");
        Assert.assertEquals(registrationPage.getEmail(), "john.doe@example.com", 
            "Email should be populated");
    }
    
    /**
     * Example 3: Fill form using the complete form fill method
     * Demonstrates efficient form population with all required fields
     */
    @Test(priority = 3, enabled = false)
    public void example3_FillCompleteFormAtOnce() {
        // Navigate to registration page
        String baseUrl = ConfigReader.getInstance().getProperty("baseUrl");
        CustomerRegistrationPage registrationPage = new CustomerRegistrationPage(driver);
        registrationPage.navigateToCustomerRegistration(baseUrl);
        
        // Fill the complete form at once
        registrationPage.fillCompleteCustomerRegistrationForm(
            "Jane",                     // First Name
            "Smith",                    // Last Name
            "05/20/1985",              // Date of Birth
            "987-65-4321",             // SSN
            "jane.smith@example.com",  // Email
            "(555) 987-6543",          // Phone Number
            "456 Oak Avenue",          // Street Address
            "Suite 100",               // Address Line 2 (optional)
            "Los Angeles",             // City
            "California",              // State
            "90001",                   // Zip Code
            "USA"                      // Country
        );
        
        // Verify all fields are populated
        Assert.assertEquals(registrationPage.getFirstName(), "Jane");
        Assert.assertEquals(registrationPage.getLastName(), "Smith");
        Assert.assertEquals(registrationPage.getEmail(), "jane.smith@example.com");
        Assert.assertEquals(registrationPage.getCity(), "Los Angeles");
        Assert.assertEquals(registrationPage.getSelectedState(), "California");
    }
    
    /**
     * Example 4: Submit form and verify success
     * Demonstrates complete registration workflow with validation
     */
    @Test(priority = 4, enabled = false)
    public void example4_SubmitFormAndVerifySuccess() {
        // Navigate to registration page
        String baseUrl = ConfigReader.getInstance().getProperty("baseUrl");
        CustomerRegistrationPage registrationPage = new CustomerRegistrationPage(driver);
        registrationPage.navigateToCustomerRegistration(baseUrl);
        
        // Fill the form
        registrationPage.fillCustomerRegistrationForm(
            "Michael",
            "Johnson",
            "03/10/1988",
            "456-78-9012",
            "michael.johnson@example.com",
            "(555) 456-7890",
            "789 Pine Street",
            "Chicago",
            "Illinois",
            "60601"
        );
        
        // Submit the form
        registrationPage.clickSubmitButton();
        
        // Wait for and verify success message
        boolean successDisplayed = registrationPage.waitForSuccessMessage();
        Assert.assertTrue(successDisplayed, "Success message should be displayed");
        
        String successMessage = registrationPage.getSuccessMessageText();
        Assert.assertFalse(successMessage.isEmpty(), "Success message should not be empty");
        
        System.out.println("Registration successful! Message: " + successMessage);
    }
    
    /**
     * Example 5: Test Clear button functionality
     * Demonstrates how to clear form fields
     */
    @Test(priority = 5, enabled = false)
    public void example5_TestClearButton() {
        // Navigate to registration page
        String baseUrl = ConfigReader.getInstance().getProperty("baseUrl");
        CustomerRegistrationPage registrationPage = new CustomerRegistrationPage(driver);
        registrationPage.navigateToCustomerRegistration(baseUrl);
        
        // Fill some fields
        registrationPage.enterFirstName("Test");
        registrationPage.enterLastName("User");
        registrationPage.enterEmail("test.user@example.com");
        
        // Verify fields are populated
        Assert.assertEquals(registrationPage.getFirstName(), "Test");
        
        // Click Clear button
        registrationPage.clickClearButton();
        
        // Verify fields are cleared
        Assert.assertTrue(registrationPage.getFirstName().isEmpty() || 
                         registrationPage.getFirstName().equals(""),
            "First name should be cleared");
    }
    
    /**
     * Example 6: Test Reset button functionality
     * Demonstrates how to reset form to default values
     */
    @Test(priority = 6, enabled = false)
    public void example6_TestResetButton() {
        // Navigate to registration page
        String baseUrl = ConfigReader.getInstance().getProperty("baseUrl");
        CustomerRegistrationPage registrationPage = new CustomerRegistrationPage(driver);
        registrationPage.navigateToCustomerRegistration(baseUrl);
        
        // Fill form with test data
        registrationPage.enterFirstName("Reset");
        registrationPage.enterLastName("Test");
        registrationPage.enterEmail("reset.test@example.com");
        
        // Click Reset button
        registrationPage.clickResetButton();
        
        // Verify form is reset (fields should be empty or default values)
        String firstNameAfterReset = registrationPage.getFirstName();
        System.out.println("First name after reset: " + firstNameAfterReset);
    }
    
    /**
     * Example 7: Test Cancel button functionality
     * Demonstrates how to cancel registration and verify navigation
     */
    @Test(priority = 7, enabled = false)
    public void example7_TestCancelButton() {
        // Navigate to registration page
        String baseUrl = ConfigReader.getInstance().getProperty("baseUrl");
        CustomerRegistrationPage registrationPage = new CustomerRegistrationPage(driver);
        registrationPage.navigateToCustomerRegistration(baseUrl);
        
        // Fill some fields
        registrationPage.enterFirstName("Cancel");
        registrationPage.enterLastName("Test");
        
        // Click Cancel button
        registrationPage.clickCancelButton();
        
        // Verify navigation (URL should change or dashboard should be displayed)
        String currentUrl = driver.getCurrentUrl();
        System.out.println("URL after cancel: " + currentUrl);
        
        // You might want to verify navigation to dashboard or previous page
        Assert.assertFalse(currentUrl.contains("registration"), 
            "Should navigate away from registration page after cancel");
    }
    
    /**
     * Example 8: Validate form with missing required fields
     * Demonstrates error handling and field validation
     */
    @Test(priority = 8, enabled = false)
    public void example8_ValidateMissingRequiredFields() {
        // Navigate to registration page
        String baseUrl = ConfigReader.getInstance().getProperty("baseUrl");
        CustomerRegistrationPage registrationPage = new CustomerRegistrationPage(driver);
        registrationPage.navigateToCustomerRegistration(baseUrl);
        
        // Submit form without filling required fields
        registrationPage.clickSubmitButton();
        
        // Wait for error message or field validation errors
        boolean errorDisplayed = registrationPage.waitForErrorMessage();
        
        // Check for general error message
        if (errorDisplayed) {
            String errorMessage = registrationPage.getErrorMessageText();
            System.out.println("Error message: " + errorMessage);
            Assert.assertTrue(registrationPage.isErrorMessageDisplayed(), 
                "Error message should be displayed for missing fields");
        }
        
        // Check for field-specific validation errors
        boolean firstNameError = registrationPage.isFieldErrorDisplayed("firstName");
        boolean emailError = registrationPage.isFieldErrorDisplayed("email");
        
        System.out.println("First name error displayed: " + firstNameError);
        System.out.println("Email error displayed: " + emailError);
    }
    
    /**
     * Example 9: Test field validation with invalid data
     * Demonstrates validation for invalid email and phone number formats
     */
    @Test(priority = 9, enabled = false)
    public void example9_TestFieldValidation() {
        // Navigate to registration page
        String baseUrl = ConfigReader.getInstance().getProperty("baseUrl");
        CustomerRegistrationPage registrationPage = new CustomerRegistrationPage(driver);
        registrationPage.navigateToCustomerRegistration(baseUrl);
        
        // Enter invalid email
        registrationPage.enterFirstName("John");
        registrationPage.enterLastName("Doe");
        registrationPage.enterEmail("invalid-email");  // Invalid format
        registrationPage.enterPhoneNumber("123");       // Invalid format
        
        // Submit form
        registrationPage.clickSubmitButton();
        
        // Check for email validation error
        if (registrationPage.isFieldErrorDisplayed("email")) {
            String emailError = registrationPage.getFieldErrorText("email");
            System.out.println("Email validation error: " + emailError);
            Assert.assertTrue(emailError.toLowerCase().contains("invalid") || 
                            emailError.toLowerCase().contains("email"),
                "Email error should indicate invalid format");
        }
        
        // Check for phone validation error
        if (registrationPage.isFieldErrorDisplayed("phoneNumber")) {
            String phoneError = registrationPage.getFieldErrorText("phoneNumber");
            System.out.println("Phone validation error: " + phoneError);
        }
    }
    
    /**
     * Example 10: Verify all form elements are displayed
     * Demonstrates page validation before proceeding with test
     */
    @Test(priority = 10, enabled = false)
    public void example10_VerifyAllFormElements() {
        // Navigate to registration page
        String baseUrl = ConfigReader.getInstance().getProperty("baseUrl");
        CustomerRegistrationPage registrationPage = new CustomerRegistrationPage(driver);
        registrationPage.navigateToCustomerRegistration(baseUrl);
        
        // Verify page header
        Assert.assertTrue(registrationPage.isPageHeaderDisplayed(), 
            "Page header should be displayed");
        String headerText = registrationPage.getPageHeaderText();
        System.out.println("Page header: " + headerText);
        
        // Verify all required fields
        Assert.assertTrue(registrationPage.areAllRequiredFieldsDisplayed(), 
            "All required fields should be displayed");
        
        // Verify individual fields
        Assert.assertTrue(registrationPage.isFirstNameFieldDisplayed(), 
            "First name field should be displayed");
        Assert.assertTrue(registrationPage.isLastNameFieldDisplayed(), 
            "Last name field should be displayed");
        Assert.assertTrue(registrationPage.isEmailFieldDisplayed(), 
            "Email field should be displayed");
        Assert.assertTrue(registrationPage.isPhoneNumberFieldDisplayed(), 
            "Phone number field should be displayed");
        
        // Verify action buttons
        Assert.assertTrue(registrationPage.isSubmitButtonDisplayed(), 
            "Submit button should be displayed");
        Assert.assertTrue(registrationPage.isSubmitButtonEnabled(), 
            "Submit button should be enabled");
        
        if (registrationPage.isClearButtonDisplayed()) {
            System.out.println("Clear button is available");
        }
        if (registrationPage.isResetButtonDisplayed()) {
            System.out.println("Reset button is available");
        }
        if (registrationPage.isCancelButtonDisplayed()) {
            System.out.println("Cancel button is available");
        }
    }
    
    /**
     * Example 11: Using complete address method
     * Demonstrates efficient address entry using helper method
     */
    @Test(priority = 11, enabled = false)
    public void example11_UseCompleteAddressMethod() {
        // Navigate to registration page
        String baseUrl = ConfigReader.getInstance().getProperty("baseUrl");
        CustomerRegistrationPage registrationPage = new CustomerRegistrationPage(driver);
        registrationPage.navigateToCustomerRegistration(baseUrl);
        
        // Fill personal and contact info
        registrationPage.enterFirstName("Sarah");
        registrationPage.enterLastName("Williams");
        registrationPage.enterDateOfBirth("07/25/1992");
        registrationPage.enterSSN("321-54-9876");
        registrationPage.enterEmail("sarah.williams@example.com");
        registrationPage.enterPhoneNumber("(555) 321-9876");
        
        // Fill complete address at once
        registrationPage.enterCompleteAddress(
            "321 Elm Street",          // Street Address
            "Unit 5C",                 // Address Line 2
            "Boston",                  // City
            "Massachusetts",           // State
            "02101",                   // Zip Code
            "USA"                      // Country
        );
        
        // Verify address fields
        Assert.assertEquals(registrationPage.getStreetAddress(), "321 Elm Street");
        Assert.assertEquals(registrationPage.getCity(), "Boston");
        Assert.assertEquals(registrationPage.getSelectedState(), "Massachusetts");
        Assert.assertEquals(registrationPage.getZipCode(), "02101");
    }
    
    /**
     * Example 12: Complete end-to-end registration workflow
     * Demonstrates full registration flow with all validations
     */
    @Test(priority = 12, enabled = false)
    public void example12_CompleteEndToEndWorkflow() {
        // Step 1: Navigate to Dashboard
        String baseUrl = ConfigReader.getInstance().getProperty("baseUrl");
        DashboardPage dashboardPage = new DashboardPage(driver);
        dashboardPage.navigateToDashboard(baseUrl);
        
        Assert.assertTrue(dashboardPage.isDashboardHeaderDisplayed(), 
            "Dashboard should be displayed");
        
        // Step 2: Navigate to Customer Registration
        CustomerRegistrationPage registrationPage = dashboardPage.navigateToCustomerRegistration();
        
        Assert.assertTrue(registrationPage.isRegistrationFormDisplayed(), 
            "Registration form should be displayed");
        
        // Step 3: Verify all form elements
        Assert.assertTrue(registrationPage.areAllRequiredFieldsDisplayed(), 
            "All required fields should be present");
        
        // Step 4: Fill complete registration form
        registrationPage.fillCompleteCustomerRegistrationForm(
            "Emma",
            "Davis",
            "11/30/1987",
            "654-32-1098",
            "emma.davis@example.com",
            "(555) 654-3210",
            "987 Maple Drive",
            "Building C",
            "Seattle",
            "Washington",
            "98101",
            "USA"
        );
        
        // Step 5: Verify all fields are populated correctly
        Assert.assertEquals(registrationPage.getFirstName(), "Emma", 
            "First name should match");
        Assert.assertEquals(registrationPage.getLastName(), "Davis", 
            "Last name should match");
        Assert.assertEquals(registrationPage.getEmail(), "emma.davis@example.com", 
            "Email should match");
        
        // Step 6: Submit the form
        registrationPage.clickSubmitButton();
        
        // Step 7: Verify success
        boolean successDisplayed = registrationPage.waitForSuccessMessage();
        Assert.assertTrue(successDisplayed, 
            "Success message should be displayed after submission");
        
        String successMessage = registrationPage.getSuccessMessageText();
        System.out.println("✓ Registration completed successfully!");
        System.out.println("✓ Success message: " + successMessage);
        
        // Step 8: Verify no errors
        Assert.assertFalse(registrationPage.isErrorMessageDisplayed(), 
            "No error messages should be displayed on successful registration");
        
        System.out.println("✓ End-to-end customer registration workflow completed successfully!");
    }
}
