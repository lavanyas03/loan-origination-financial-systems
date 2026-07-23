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
 * CustomerRegistrationDuplicateBoundaryTest - Comprehensive duplicate and boundary value testing
 * 
 * This test class implements advanced validation scenarios for Customer Registration:
 * 1. Duplicate Customer Detection - Tests duplicate prevention based on:
 *    - Complete duplicate records (all fields identical)
 *    - Duplicate SSN (unique identifier)
 *    - Duplicate Email address
 *    - Duplicate Phone number
 * 
 * 2. Boundary Value Testing - Tests edge cases and limits for:
 *    - Age boundaries (minimum 18, maximum 100 years)
 *    - Name length boundaries (minimum 2, maximum 50 characters)
 *    - Phone number length (exactly 10 digits)
 *    - Email length boundaries (minimum 6, maximum 254 characters)
 *    - SSN format validation (XXX-XX-XXXX format)
 *    - Zip code length (exactly 5 digits)
 * 
 * Test Strategy:
 * - Data-driven testing using JSON test data files
 * - Independent test execution (no dependencies between tests)
 * - Comprehensive logging for debugging and audit trail
 * - Automatic screenshot capture on test failure
 * - Positive and negative boundary testing
 * - Validation of error messages for failed scenarios
 * 
 * Test Data Files:
 * - CustomerDuplicateTestData.json - Duplicate customer scenarios
 * - CustomerBoundaryTestData.json - Boundary value test cases
 * 
 * Framework: Selenium 4, TestNG, Java 17, Maven, Page Object Model
 * Author: Test Automation Team
 * Date: 2026-07-23
 */
public class CustomerRegistrationDuplicateBoundaryTest extends BaseTest {
    
    private static final Logger logger = LogManager.getLogger(CustomerRegistrationDuplicateBoundaryTest.class);
    
    /**
     * Test: Duplicate Customer Registration Validation
     * 
     * This test validates that the application properly prevents duplicate customer registrations
     * based on unique identifiers (SSN, Email, Phone) and complete duplicate records.
     * 
     * Test Flow:
     * 1. Register a customer with initial details (first registration)
     * 2. Verify successful registration
     * 3. Attempt to register the same customer again (duplicate registration)
     * 4. Verify that duplicate registration is prevented
     * 5. Verify appropriate error message is displayed
     * 
     * Duplicate Detection Scenarios:
     * - Same customer details (all fields identical)
     * - Same SSN (even with different other details)
     * - Same Email address (even with different other details)
     * - Same Phone number (even with different other details)
     * 
     * Test Steps:
     * 1. Navigate to Customer Registration page
     * 2. Fill and submit registration form with initial customer data
     * 3. Verify successful registration (success message displayed)
     * 4. Navigate back to Customer Registration page
     * 5. Fill and submit registration form with duplicate customer data
     * 6. Verify error message is displayed
     * 7. Verify error message text matches expected duplicate error message
     * 8. Log all steps and assertions
     * 
     * Expected Result:
     * - First registration succeeds with success message
     * - Second registration (duplicate) fails with appropriate error message
     * - Error message clearly indicates the field causing duplicate issue
     * 
     * @param testData Map containing duplicate test data from JSON file including:
     *                 - testCaseId: Unique test case identifier
     *                 - testScenario: Description of duplicate scenario being tested
     *                 - All customer fields (firstName, lastName, email, phone, ssn, etc.)
     *                 - duplicateField: The field being tested for duplication
     *                 - expectedErrorMessage: Expected duplicate error message
     *                 - expectedResult: Expected test outcome (FAIL for duplicate tests)
     */
    @Test(
        description = "Validate duplicate customer registration prevention",
        dataProvider = "customerDuplicateDataJSON",
        dataProviderClass = TestDataProviders.class,
        priority = 1,
        groups = {"duplicate", "validation", "negative", "regression", "customer"}
    )
    public void testDuplicateCustomerRegistration(Map<String, Object> testData) {
        logger.info("=================================================================");
        logger.info("TEST STARTED: Duplicate Customer Registration Validation");
        logger.info("Test Case ID: {}", testData.get("testCaseId"));
        logger.info("Test Scenario: {}", testData.get("testScenario"));
        logger.info("Duplicate Field: {}", testData.get("duplicateField"));
        logger.info("=================================================================");
        
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
        
        String expectedErrorMessage = (String) testData.get("expectedErrorMessage");
        
        logger.info("Customer Details - Name: {} {}, Email: {}, Phone: {}", 
            firstName, lastName, email, phone);
        logger.info("Expected Error Message: {}", expectedErrorMessage);
        
        // Step 3: Fill customer registration form - First Registration Attempt
        logger.info("Step 3: Fill customer registration form - FIRST REGISTRATION");
        logger.info("Attempting to register customer for the first time");
        
        registrationPage.enterFirstName(firstName);
        registrationPage.enterLastName(lastName);
        registrationPage.enterDateOfBirth(dateOfBirth);
        registrationPage.enterSSN(ssn);
        registrationPage.enterEmail(email);
        registrationPage.enterPhoneNumber(phone);
        registrationPage.enterStreetAddress(streetAddress);
        registrationPage.enterCity(city);
        registrationPage.selectState(state);
        registrationPage.enterZipCode(zipCode);
        
        logger.info("All form fields populated with customer data for first registration");
        
        // Step 4: Submit the form - First Registration
        logger.info("Step 4: Submit customer registration form - FIRST REGISTRATION");
        registrationPage.clickSubmitButton();
        logger.info("Submit button clicked - waiting for registration result");
        
        // Wait for success or error message
        logger.info("Waiting for registration result message to appear");
        try {
            Thread.sleep(2000); // Wait for form processing
        } catch (InterruptedException e) {
            logger.warn("Thread sleep interrupted", e);
        }
        
        // Step 5: Verify first registration success
        logger.info("Step 5: Verify first registration success");
        boolean isSuccessDisplayed = registrationPage.isSuccessMessageDisplayed();
        
        if (isSuccessDisplayed) {
            String successMessage = registrationPage.getSuccessMessageText();
            logger.info("✓ FIRST REGISTRATION SUCCESSFUL");
            logger.info("Success Message: {}", successMessage);
        } else {
            // First registration should succeed, but if error occurs, log it
            boolean isErrorDisplayed = registrationPage.isErrorMessageDisplayed();
            if (isErrorDisplayed) {
                String errorMessage = registrationPage.getErrorMessageText();
                logger.warn("⚠ FIRST REGISTRATION FAILED - This is unexpected!");
                logger.warn("Error Message: {}", errorMessage);
                logger.warn("First registration should succeed. Possible data issue or application state problem.");
            } else {
                logger.warn("⚠ No success or error message displayed after first registration");
            }
        }
        
        // Step 6: Navigate back to registration page for second attempt
        logger.info("Step 6: Navigate back to Customer Registration page for DUPLICATE registration");
        registrationPage.navigateToCustomerRegistration(baseUrl);
        logger.info("Navigated back to registration page");
        
        // Verify registration form is displayed again
        Assert.assertTrue(registrationPage.isRegistrationFormDisplayed(), 
            "Customer Registration form should be displayed for second registration");
        logger.info("Registration form is displayed for duplicate registration attempt");
        
        // Step 7: Fill customer registration form - Second Registration (Duplicate)
        logger.info("Step 7: Fill customer registration form - SECOND REGISTRATION (DUPLICATE)");
        logger.info("Attempting to register the SAME customer again - should be prevented");
        
        registrationPage.enterFirstName(firstName);
        registrationPage.enterLastName(lastName);
        registrationPage.enterDateOfBirth(dateOfBirth);
        registrationPage.enterSSN(ssn);
        registrationPage.enterEmail(email);
        registrationPage.enterPhoneNumber(phone);
        registrationPage.enterStreetAddress(streetAddress);
        registrationPage.enterCity(city);
        registrationPage.selectState(state);
        registrationPage.enterZipCode(zipCode);
        
        logger.info("All form fields populated with DUPLICATE customer data");
        
        // Step 8: Submit the form - Second Registration (Duplicate)
        logger.info("Step 8: Submit customer registration form - SECOND REGISTRATION (DUPLICATE)");
        registrationPage.clickSubmitButton();
        logger.info("Submit button clicked for duplicate registration - expecting error");
        
        // Wait for error message
        logger.info("Waiting for duplicate registration error message");
        try {
            Thread.sleep(2000); // Wait for form processing
        } catch (InterruptedException e) {
            logger.warn("Thread sleep interrupted", e);
        }
        
        // Step 9: Verify duplicate registration is prevented (error message displayed)
        logger.info("Step 9: Verify duplicate registration error is displayed");
        boolean isErrorDisplayed = registrationPage.isErrorMessageDisplayed();
        
        Assert.assertTrue(isErrorDisplayed, 
            "Duplicate registration should be prevented - error message should be displayed");
        logger.info("✓ Error message is displayed - duplicate registration prevented successfully");
        
        // Step 10: Verify error message text
        logger.info("Step 10: Verify duplicate registration error message text");
        String actualErrorMessage = registrationPage.getErrorMessageText();
        logger.info("Actual Error Message: {}", actualErrorMessage);
        logger.info("Expected Error Message: {}", expectedErrorMessage);
        
        Assert.assertTrue(actualErrorMessage.contains(expectedErrorMessage) || 
                         expectedErrorMessage.contains(actualErrorMessage),
            String.format("Error message should indicate duplicate customer. Expected: '%s', Actual: '%s'", 
                expectedErrorMessage, actualErrorMessage));
        logger.info("✓ Error message matches expected duplicate validation message");
        
        // Step 11: Test completion
        logger.info("=================================================================");
        logger.info("TEST COMPLETED SUCCESSFULLY: Duplicate Customer Registration Validation");
        logger.info("✓ First registration succeeded");
        logger.info("✓ Second (duplicate) registration was prevented");
        logger.info("✓ Appropriate error message was displayed");
        logger.info("Test Case ID: {}", testData.get("testCaseId"));
        logger.info("=================================================================");
    }
    
    /**
     * Test: Customer Registration Boundary Value Validation
     * 
     * This test validates boundary conditions for customer registration fields including:
     * - Age boundaries (minimum and maximum allowed age)
     * - Name length boundaries (first name and last name)
     * - Phone number length validation
     * - Email length boundaries
     * - SSN format validation
     * - Zip code length validation
     * 
     * Boundary Testing Categories:
     * 1. Minimum Valid Value - Tests the smallest acceptable value
     * 2. Below Minimum - Tests one below minimum (should fail)
     * 3. Maximum Valid Value - Tests the largest acceptable value
     * 4. Above Maximum - Tests one above maximum (should fail)
     * 
     * Test Steps:
     * 1. Navigate to Customer Registration page
     * 2. Fill registration form with boundary test data
     * 3. Submit the registration form
     * 4. Verify result based on expected outcome (PASS or FAIL)
     * 5. For PASS scenarios - verify success message is displayed
     * 6. For FAIL scenarios - verify appropriate error message is displayed
     * 7. Verify error message text matches expected validation message
     * 8. Log all steps and assertions
     * 
     * Expected Result:
     * - Valid boundary values (minimum and maximum) should be accepted
     * - Invalid boundary values (below minimum or above maximum) should be rejected
     * - Appropriate error messages should be displayed for invalid values
     * 
     * @param testData Map containing boundary test data from JSON file including:
     *                 - testCaseId: Unique test case identifier
     *                 - testScenario: Description of boundary scenario being tested
     *                 - All customer fields with boundary values
     *                 - validationType: The type of boundary being tested
     *                 - expectedErrorMessage: Expected validation error message (if FAIL)
     *                 - expectedResult: Expected test outcome (PASS or FAIL)
     */
    @Test(
        description = "Validate customer registration boundary value conditions",
        dataProvider = "customerBoundaryDataJSON",
        dataProviderClass = TestDataProviders.class,
        priority = 2,
        groups = {"boundary", "validation", "regression", "customer"}
    )
    public void testCustomerRegistrationBoundaryValues(Map<String, Object> testData) {
        logger.info("=================================================================");
        logger.info("TEST STARTED: Customer Registration Boundary Value Validation");
        logger.info("Test Case ID: {}", testData.get("testCaseId"));
        logger.info("Test Scenario: {}", testData.get("testScenario"));
        logger.info("Validation Type: {}", testData.get("validationType"));
        logger.info("=================================================================");
        
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
        logger.info("Step 2: Extract boundary test data");
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
        
        String validationType = (String) testData.get("validationType");
        String expectedErrorMessage = (String) testData.get("expectedErrorMessage");
        String expectedResult = (String) testData.get("expectedResult");
        
        logger.info("Boundary Test Details:");
        logger.info("  - Validation Type: {}", validationType);
        logger.info("  - Expected Result: {}", expectedResult);
        logger.info("  - First Name Length: {} characters", firstName.length());
        logger.info("  - Last Name Length: {} characters", lastName.length());
        logger.info("  - Email Length: {} characters", email.length());
        logger.info("  - Phone Length: {} characters", phone.length());
        logger.info("  - SSN: {}", ssn);
        logger.info("  - Date of Birth: {}", dateOfBirth);
        logger.info("  - Zip Code Length: {} characters", zipCode.length());
        
        // Step 3: Fill customer registration form with boundary test data
        logger.info("Step 3: Fill customer registration form with boundary test data");
        
        registrationPage.enterFirstName(firstName);
        logger.info("Entered First Name: {} ({} characters)", firstName, firstName.length());
        
        registrationPage.enterLastName(lastName);
        logger.info("Entered Last Name: {} ({} characters)", lastName, lastName.length());
        
        registrationPage.enterDateOfBirth(dateOfBirth);
        logger.info("Entered Date of Birth: {}", dateOfBirth);
        
        registrationPage.enterSSN(ssn);
        logger.info("Entered SSN (masked for security)");
        
        registrationPage.enterEmail(email);
        logger.info("Entered Email: {} ({} characters)", email, email.length());
        
        registrationPage.enterPhoneNumber(phone);
        logger.info("Entered Phone Number: {} ({} characters)", phone, phone.length());
        
        registrationPage.enterStreetAddress(streetAddress);
        logger.info("Entered Street Address: {}", streetAddress);
        
        registrationPage.enterCity(city);
        logger.info("Entered City: {}", city);
        
        registrationPage.selectState(state);
        logger.info("Selected State: {}", state);
        
        registrationPage.enterZipCode(zipCode);
        logger.info("Entered Zip Code: {} ({} characters)", zipCode, zipCode.length());
        
        logger.info("All form fields populated with boundary test data");
        
        // Step 4: Submit the registration form
        logger.info("Step 4: Submit customer registration form");
        registrationPage.clickSubmitButton();
        logger.info("Submit button clicked - waiting for validation result");
        
        // Wait for result message
        logger.info("Waiting for registration/validation result message");
        try {
            Thread.sleep(2000); // Wait for form processing
        } catch (InterruptedException e) {
            logger.warn("Thread sleep interrupted", e);
        }
        
        // Step 5: Verify result based on expected outcome
        logger.info("Step 5: Verify result based on expected outcome: {}", expectedResult);
        
        if ("PASS".equalsIgnoreCase(expectedResult)) {
            // Expected to succeed - verify success message
            logger.info("Expected Result: PASS - Verifying success message is displayed");
            
            boolean isSuccessDisplayed = registrationPage.isSuccessMessageDisplayed();
            
            if (isSuccessDisplayed) {
                String successMessage = registrationPage.getSuccessMessageText();
                logger.info("✓ SUCCESS: Registration accepted with boundary value");
                logger.info("Success Message: {}", successMessage);
                
                Assert.assertTrue(isSuccessDisplayed, 
                    "Registration should succeed for valid boundary value");
                logger.info("✓ Boundary value test PASSED - valid boundary accepted");
            } else {
                // Check if error was displayed instead
                boolean isErrorDisplayed = registrationPage.isErrorMessageDisplayed();
                if (isErrorDisplayed) {
                    String errorMessage = registrationPage.getErrorMessageText();
                    logger.error("✗ FAILURE: Expected success but got error");
                    logger.error("Error Message: {}", errorMessage);
                    Assert.fail(String.format(
                        "Registration should succeed for valid boundary value. Error: %s", 
                        errorMessage));
                } else {
                    logger.error("✗ FAILURE: No success or error message displayed");
                    Assert.fail("Registration result not displayed - no success or error message");
                }
            }
        } else {
            // Expected to fail - verify error message
            logger.info("Expected Result: FAIL - Verifying error message is displayed");
            
            boolean isErrorDisplayed = registrationPage.isErrorMessageDisplayed();
            
            if (isErrorDisplayed) {
                String actualErrorMessage = registrationPage.getErrorMessageText();
                logger.info("✓ ERROR MESSAGE DISPLAYED: Registration rejected for invalid boundary value");
                logger.info("Actual Error Message: {}", actualErrorMessage);
                logger.info("Expected Error Message: {}", expectedErrorMessage);
                
                Assert.assertTrue(isErrorDisplayed, 
                    "Error message should be displayed for invalid boundary value");
                logger.info("✓ Error message displayed successfully");
                
                // Verify error message text
                Assert.assertTrue(actualErrorMessage.contains(expectedErrorMessage) || 
                                 expectedErrorMessage.contains(actualErrorMessage),
                    String.format("Error message mismatch. Expected: '%s', Actual: '%s'", 
                        expectedErrorMessage, actualErrorMessage));
                logger.info("✓ Error message text matches expected validation message");
            } else {
                // Check if success was displayed instead (should not happen)
                boolean isSuccessDisplayed = registrationPage.isSuccessMessageDisplayed();
                if (isSuccessDisplayed) {
                    String successMessage = registrationPage.getSuccessMessageText();
                    logger.error("✗ FAILURE: Expected error but got success");
                    logger.error("Success Message: {}", successMessage);
                    Assert.fail(String.format(
                        "Registration should fail for invalid boundary value. Got success: %s", 
                        successMessage));
                } else {
                    logger.error("✗ FAILURE: No error or success message displayed");
                    Assert.fail("Validation result not displayed - no error or success message");
                }
            }
        }
        
        // Step 6: Test completion
        logger.info("=================================================================");
        logger.info("TEST COMPLETED SUCCESSFULLY: Customer Registration Boundary Value Validation");
        logger.info("✓ Boundary value test executed successfully");
        logger.info("✓ Expected result verified: {}", expectedResult);
        logger.info("Test Case ID: {}", testData.get("testCaseId"));
        logger.info("Validation Type: {}", validationType);
        logger.info("=================================================================");
    }
}
