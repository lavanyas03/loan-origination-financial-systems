package tests;

import base.BaseTest;
import dataproviders.TestDataProviders;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Map;

/**
 * Parameterized tests for Customer module using Excel and JSON data providers
 * Demonstrates data-driven testing approach
 */
public class CustomerDataDrivenTest extends BaseTest {
    
    private static final Logger logger = LogManager.getLogger(CustomerDataDrivenTest.class);
    
    /**
     * Test customer data validation using Excel DataProvider
     */
    @Test(dataProvider = "customerDataExcel", dataProviderClass = TestDataProviders.class,
          description = "Validate customer data from Excel")
    public void testCustomerValidationWithExcel(Map<String, String> testData) {
        String testCaseId = testData.get("testCaseId");
        logger.info("Executing test: {} using Excel data", testCaseId);
        
        // Extract test data
        String firstName = testData.get("firstName");
        String lastName = testData.get("lastName");
        String email = testData.get("email");
        String phone = testData.get("phone");
        String ssn = testData.get("ssn");
        String expectedResult = testData.get("expectedResult");
        
        logger.info("Customer Details - Name: {} {}, Email: {}, Phone: {}, SSN: {}",
                firstName, lastName, email, phone, ssn);
        
        // Validate required fields are not empty
        Assert.assertNotNull(firstName, "First name should not be null");
        Assert.assertNotNull(lastName, "Last name should not be null");
        Assert.assertNotNull(email, "Email should not be null");
        Assert.assertNotNull(phone, "Phone should not be null");
        Assert.assertNotNull(ssn, "SSN should not be null");
        
        // Validate first name format
        Assert.assertTrue(firstName.matches("^[a-zA-Z]+$"), 
                "First name should contain only letters: " + firstName);
        
        // Validate last name format
        Assert.assertTrue(lastName.matches("^[a-zA-Z]+$"), 
                "Last name should contain only letters: " + lastName);
        
        // Validate email format
        if (expectedResult.equals("PASS")) {
            Assert.assertTrue(email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"),
                    "Email should be in valid format: " + email);
        }
        
        // Validate phone format (10 digits)
        if (expectedResult.equals("PASS")) {
            String phoneDigits = phone.replaceAll("[^0-9]", "");
            Assert.assertEquals(phoneDigits.length(), 10,
                    "Phone should contain exactly 10 digits: " + phone);
        }
        
        // Validate SSN format
        if (expectedResult.equals("PASS")) {
            Assert.assertTrue(ssn.matches("^\\d{3}-\\d{2}-\\d{4}$"),
                    "SSN should be in format XXX-XX-XXXX: " + ssn);
        }
        
        logger.info("Test {} completed successfully with expected result: {}", testCaseId, expectedResult);
    }
    
    /**
     * Test customer data validation using JSON DataProvider
     */
    @Test(dataProvider = "customerDataJSON", dataProviderClass = TestDataProviders.class,
          description = "Validate customer data from JSON")
    public void testCustomerValidationWithJSON(Map<String, Object> testData) {
        String testCaseId = String.valueOf(testData.get("testCaseId"));
        logger.info("Executing test: {} using JSON data", testCaseId);
        
        // Extract test data
        String firstName = String.valueOf(testData.get("firstName"));
        String lastName = String.valueOf(testData.get("lastName"));
        String employmentStatus = String.valueOf(testData.get("employmentStatus"));
        String annualIncome = String.valueOf(testData.get("annualIncome"));
        String creditScore = String.valueOf(testData.get("creditScore"));
        String expectedResult = String.valueOf(testData.get("expectedResult"));
        
        logger.info("Customer Details - Name: {} {}, Employment: {}, Income: {}, Credit Score: {}",
                firstName, lastName, employmentStatus, annualIncome, creditScore);
        
        // Validate employment status
        String[] validStatuses = {"Employed", "Self-Employed", "Unemployed", "Retired"};
        boolean validEmploymentStatus = false;
        for (String status : validStatuses) {
            if (status.equals(employmentStatus)) {
                validEmploymentStatus = true;
                break;
            }
        }
        Assert.assertTrue(validEmploymentStatus,
                "Employment status should be valid: " + employmentStatus);
        
        // Validate annual income
        try {
            double income = Double.parseDouble(annualIncome);
            Assert.assertTrue(income >= 0, "Annual income should be non-negative: " + income);
            
            if (expectedResult.equals("PASS")) {
                Assert.assertTrue(income >= 25000,
                        "For approved customers, income should be at least $25,000: " + income);
            }
        } catch (NumberFormatException e) {
            Assert.fail("Annual income should be a valid number: " + annualIncome);
        }
        
        // Validate credit score
        try {
            int score = Integer.parseInt(creditScore);
            Assert.assertTrue(score >= 300 && score <= 850,
                    "Credit score should be between 300 and 850: " + score);
            
            if (expectedResult.equals("PASS")) {
                Assert.assertTrue(score >= 620,
                        "For approved customers, credit score should be at least 620: " + score);
            }
        } catch (NumberFormatException e) {
            Assert.fail("Credit score should be a valid number: " + creditScore);
        }
        
        logger.info("Test {} completed successfully with expected result: {}", testCaseId, expectedResult);
    }
    
    /**
     * Test invalid customer data scenarios using Excel DataProvider
     */
    @Test(dataProvider = "invalidCustomerDataExcel", dataProviderClass = TestDataProviders.class,
          description = "Validate invalid customer data handling",
          enabled = false) // Enable after Excel file is generated with InvalidData sheet
    public void testInvalidCustomerData(Map<String, String> testData) {
        String testCaseId = testData.get("testCaseId");
        logger.info("Executing negative test: {} using Excel data", testCaseId);
        
        String email = testData.get("email");
        String phone = testData.get("phone");
        String ssn = testData.get("ssn");
        String expectedResult = testData.get("expectedResult");
        
        logger.info("Testing invalid data - Email: {}, Phone: {}, SSN: {}", email, phone, ssn);
        
        // Verify expected result is FAIL for invalid data
        Assert.assertEquals(expectedResult, "FAIL",
                "Invalid customer data should have FAIL as expected result");
        
        // Validate that at least one field is invalid
        boolean hasInvalidEmail = !email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
        boolean hasInvalidPhone = phone.replaceAll("[^0-9]", "").length() != 10;
        boolean hasInvalidSSN = !ssn.matches("^\\d{3}-\\d{2}-\\d{4}$");
        
        Assert.assertTrue(hasInvalidEmail || hasInvalidPhone || hasInvalidSSN,
                "Test data should contain at least one invalid field");
        
        logger.info("Negative test {} completed - invalid data detected as expected", testCaseId);
    }
    
    /**
     * Test customer profile completeness
     */
    @Test(dataProvider = "customerDataJSON", dataProviderClass = TestDataProviders.class,
          description = "Validate customer profile completeness")
    public void testCustomerProfileCompleteness(Map<String, Object> testData) {
        String testCaseId = String.valueOf(testData.get("testCaseId"));
        logger.info("Executing completeness test: {}", testCaseId);
        
        // Required fields for customer profile
        String[] requiredFields = {
            "testCaseId", "firstName", "lastName", "email", "phone",
            "ssn", "dateOfBirth", "employmentStatus", "annualIncome", "creditScore"
        };
        
        // Verify all required fields are present and not empty
        for (String field : requiredFields) {
            Assert.assertTrue(testData.containsKey(field),
                    "Required field missing: " + field);
            
            Object value = testData.get(field);
            Assert.assertNotNull(value, "Field should not be null: " + field);
            
            String stringValue = String.valueOf(value);
            Assert.assertFalse(stringValue.isEmpty() || stringValue.equals("null"),
                    "Field should not be empty: " + field);
        }
        
        logger.info("Profile completeness validated for test: {}", testCaseId);
    }
}
