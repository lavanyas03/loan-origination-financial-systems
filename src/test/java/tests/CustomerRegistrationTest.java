package tests;

import base.BaseTest;
import dataproviders.TestDataProviders;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.CustomerRegistrationPage;

import java.util.Map;

/**
 * CustomerRegistrationTest - Test class for Customer Registration happy-path automation
 * 
 * This test validates the complete customer registration workflow with valid data:
 * - Reads test data from JSON file using DataProvider
 * - Populates all required customer registration fields
 * - Submits the registration form
 * - Verifies successful registration with confirmation message
 * - Captures and validates the generated Customer ID
 * - Captures screenshots automatically on test failure
 * - Logs all major test steps
 * 
 * Framework: Selenium 4, TestNG, Java 17, Maven, Page Object Model
 * Author: Test Automation Team
 * Date: 2026-07-21
 */
public class CustomerRegistrationTest extends BaseTest {
    
    private static final Logger logger = LogManager.getLogger(CustomerRegistrationTest.class);
    
    /**
     * Test: Successful customer registration with valid data (Happy Path)
     * 
     * Test Steps:
     * 1. Navigate to Customer Registration page
     * 2. Read valid customer test data from JSON file
     * 3. Fill all required customer registration fields
     * 4. Submit the registration form
     * 5. Verify success message is displayed
     * 6. Verify success message text contains confirmation
     * 7. Extract and validate Customer ID is not empty
     * 8. Log the generated Customer ID
     * 
     * Expected Result:
     * - Registration completes successfully
     * - Success message is displayed with confirmation text
     * - Valid Customer ID is generated and displayed
     * 
     * @param testData Map containing customer test data from JSON file
     */
    @Test(
        description = "Verify customer registration with valid data - Happy Path",
        dataProvider = "customerDataJSON",
        dataProviderClass = TestDataProviders.class,
        priority = 1,
        groups = {"smoke", "regression", "customer"}
    )
    public void testSuccessfulCustomerRegistration(Map<String, Object> testData) {
        logger.info("=================================================================");
        logger.info("TEST STARTED: Customer Registration Happy Path Test");
        logger.info("Test Case ID: {}", testData.get("testCaseId"));
        logger.info("=================================================================");
        
        // Filter to test only PASS scenarios (happy path)
        String expectedResult = (String) testData.get("expectedResult");
        if (!"PASS".equalsIgnoreCase(expectedResult)) {
            logger.info("Skipping test case {} - Expected result is not PASS", testData.get("testCaseId"));
            throw new org.testng.SkipException("Skipping non-PASS test case for happy path test");
        }
        
        // Step 1: Navigate to Customer Registration page
        logger.info("Step 1: Navigate to Customer Registration page");
        String baseUrl = config.getProperty("baseURL");
        CustomerRegistrationPage registrationPage = new CustomerRegistrationPage(driver);
        registrationPage.navigateToCustomerRegistration(baseUrl);
        logger.info("Successfully navigated to Customer Registration page");
        
        // Verify registration form is displayed
        Assert.assertTrue(registrationPage.isRegistrationFormDisplayed(), 
            "Customer Registration form should be displayed");
        logger.info("Registration form is displayed and ready for input");
        
        // Step 2: Extract customer data from test data map
        logger.info("Step 2: Extract customer data from test data");
        String firstName = (String) testData.get("firstName");
        String lastName = (String) testData.get("lastName");
        String email = (String) testData.get("email");
        String phone = (String) testData.get("phone");
        String ssn = (String) testData.get("ssn");
        String dateOfBirth = (String) testData.get("dateOfBirth");
        String streetAddress = (String) testData.get("streetAddress");
        String city = (String) testData.get("city");
        String state = (String) testData.get("state");
        String zipCode = (String) testData.get("zipCode");
        
        logger.info("Customer Data: {} {}, Email: {}, DOB: {}", firstName, lastName, email, dateOfBirth);
        logger.info("Address: {}, {}, {}, {}", streetAddress, city, state, zipCode);
        
        // Step 3: Fill customer registration form with all required fields
        logger.info("Step 3: Fill customer registration form with all required fields");
        
        logger.info("Entering personal information");
        registrationPage.enterFirstName(firstName);
        registrationPage.enterLastName(lastName);
        registrationPage.enterDateOfBirth(dateOfBirth);
        registrationPage.enterSSN(ssn);
        
        logger.info("Entering contact information");
        registrationPage.enterEmail(email);
        registrationPage.enterPhoneNumber(phone);
        
        logger.info("Entering address information");
        registrationPage.enterStreetAddress(streetAddress);
        registrationPage.enterCity(city);
        registrationPage.selectState(state);
        registrationPage.enterZipCode(zipCode);
        
        logger.info("All registration form fields have been populated successfully");
        
        // Step 4: Submit the registration form
        logger.info("Step 4: Submit the registration form");
        Assert.assertTrue(registrationPage.isSubmitButtonEnabled(), 
            "Submit button should be enabled before submission");
        
        registrationPage.clickSubmitButton();
        logger.info("Registration form submitted successfully");
        
        // Step 5: Wait for and verify success message is displayed
        logger.info("Step 5: Wait for and verify success message is displayed");
        boolean successMessageDisplayed = registrationPage.waitForSuccessMessage();
        Assert.assertTrue(successMessageDisplayed, 
            "Success message should be displayed after successful registration");
        logger.info("Success message is displayed");
        
        // Step 6: Verify success message text contains confirmation
        logger.info("Step 6: Verify success message text contains confirmation");
        String successMessage = registrationPage.getSuccessMessageText();
        Assert.assertNotNull(successMessage, 
            "Success message text should not be null");
        Assert.assertFalse(successMessage.isEmpty(), 
            "Success message text should not be empty");
        
        logger.info("Success Message: {}", successMessage);
        
        // Verify success message contains expected keywords
        Assert.assertTrue(
            successMessage.toLowerCase().contains("success") || 
            successMessage.toLowerCase().contains("registered") ||
            successMessage.toLowerCase().contains("completed"),
            "Success message should contain confirmation keywords like 'success', 'registered', or 'completed'"
        );
        logger.info("Success message text is validated and contains expected confirmation");
        
        // Step 7: Extract and validate Customer ID from success message
        logger.info("Step 7: Extract and validate Customer ID from success message");
        String customerId = registrationPage.getCustomerIdFromSuccessMessage();
        
        Assert.assertNotNull(customerId, 
            "Customer ID should not be null");
        Assert.assertFalse(customerId.isEmpty(), 
            "Customer ID should not be empty - A valid Customer ID should be generated");
        Assert.assertTrue(customerId.length() > 0, 
            "Customer ID should have a valid length");
        
        logger.info("Customer ID successfully extracted and validated: {}", customerId);
        
        // Step 8: Final verification summary
        logger.info("=================================================================");
        logger.info("TEST COMPLETED SUCCESSFULLY");
        logger.info("Customer: {} {}", firstName, lastName);
        logger.info("Email: {}", email);
        logger.info("Generated Customer ID: {}", customerId);
        logger.info("Success Message: {}", successMessage);
        logger.info("=================================================================");
        
        // Additional assertions for test completeness
        Assert.assertTrue(registrationPage.isSuccessMessageDisplayed(), 
            "Success message should remain visible");
        
        logger.info("Customer registration happy-path test completed successfully");
    }
}
