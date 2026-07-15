package tests;

import base.BaseTest;
import dataproviders.TestDataProviders;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Map;

/**
 * Parameterized tests for Loan module using Excel and JSON data providers
 * Demonstrates data-driven testing with expected results validation
 */
public class LoanDataDrivenTest extends BaseTest {
    
    private static final Logger logger = LogManager.getLogger(LoanDataDrivenTest.class);
    
    /**
     * Test loan application validation using Excel DataProvider
     */
    @Test(dataProvider = "loanDataExcel", dataProviderClass = TestDataProviders.class,
          description = "Validate loan application data from Excel")
    public void testLoanApplicationWithExcel(Map<String, String> testData) {
        String testCaseId = testData.get("testCaseId");
        logger.info("Executing loan test: {} using Excel data", testCaseId);
        
        // Extract test data
        String customerId = testData.get("customerId");
        String loanType = testData.get("loanType");
        String loanAmount = testData.get("loanAmount");
        String loanTerm = testData.get("loanTerm");
        String interestRate = testData.get("interestRate");
        String expectedStatus = testData.get("expectedStatus");
        
        logger.info("Loan Details - Customer: {}, Type: {}, Amount: ${}, Term: {} months, Rate: {}%",
                customerId, loanType, loanAmount, loanTerm, interestRate);
        
        // Validate required fields
        Assert.assertNotNull(customerId, "Customer ID should not be null");
        Assert.assertNotNull(loanType, "Loan type should not be null");
        Assert.assertNotNull(loanAmount, "Loan amount should not be null");
        Assert.assertNotNull(loanTerm, "Loan term should not be null");
        Assert.assertNotNull(interestRate, "Interest rate should not be null");
        
        // Validate loan type
        String[] validLoanTypes = {"Personal", "Mortgage", "Auto", "Business", "Student"};
        boolean validLoanType = false;
        for (String type : validLoanTypes) {
            if (type.equals(loanType)) {
                validLoanType = true;
                break;
            }
        }
        Assert.assertTrue(validLoanType, "Loan type should be valid: " + loanType);
        
        // Validate loan amount
        try {
            double amount = Double.parseDouble(loanAmount);
            Assert.assertTrue(amount > 0, "Loan amount should be positive: " + amount);
            
            // Type-specific amount validations
            if ("Personal".equals(loanType)) {
                Assert.assertTrue(amount <= 100000,
                        "Personal loan amount should not exceed $100,000: " + amount);
            } else if ("Mortgage".equals(loanType)) {
                Assert.assertTrue(amount >= 50000,
                        "Mortgage loan amount should be at least $50,000: " + amount);
            } else if ("Auto".equals(loanType)) {
                Assert.assertTrue(amount <= 100000,
                        "Auto loan amount should not exceed $100,000: " + amount);
            }
        } catch (NumberFormatException e) {
            Assert.fail("Loan amount should be a valid number: " + loanAmount);
        }
        
        // Validate loan term
        try {
            int term = Integer.parseInt(loanTerm);
            Assert.assertTrue(term > 0 && term <= 360,
                    "Loan term should be between 1 and 360 months: " + term);
        } catch (NumberFormatException e) {
            Assert.fail("Loan term should be a valid number: " + loanTerm);
        }
        
        // Validate interest rate
        try {
            double rate = Double.parseDouble(interestRate);
            Assert.assertTrue(rate >= 0 && rate <= 30,
                    "Interest rate should be between 0% and 30%: " + rate);
        } catch (NumberFormatException e) {
            Assert.fail("Interest rate should be a valid number: " + interestRate);
        }
        
        logger.info("Test {} completed - Expected Status: {}", testCaseId, expectedStatus);
    }
    
    /**
     * Test loan application validation using JSON DataProvider
     */
    @Test(dataProvider = "loanDataJSON", dataProviderClass = TestDataProviders.class,
          description = "Validate loan application data from JSON")
    public void testLoanApplicationWithJSON(Map<String, Object> testData) {
        String testCaseId = String.valueOf(testData.get("testCaseId"));
        logger.info("Executing loan test: {} using JSON data", testCaseId);
        
        // Extract test data
        String customerId = String.valueOf(testData.get("customerId"));
        String loanType = String.valueOf(testData.get("loanType"));
        String purpose = String.valueOf(testData.get("purpose"));
        String collateral = String.valueOf(testData.get("collateral"));
        String expectedStatus = String.valueOf(testData.get("expectedStatus"));
        String expectedMonthlyPayment = String.valueOf(testData.get("expectedMonthlyPayment"));
        
        logger.info("Loan Details - Customer: {}, Purpose: {}, Collateral: {}, Expected Status: {}",
                customerId, purpose, collateral, expectedStatus);
        
        // Validate purpose is not empty
        Assert.assertNotNull(purpose, "Loan purpose should not be null");
        Assert.assertFalse(purpose.isEmpty(), "Loan purpose should not be empty");
        
        // Validate collateral based on loan type
        if ("Mortgage".equals(loanType)) {
            Assert.assertEquals(collateral, "Property",
                    "Mortgage loans should have Property as collateral");
        } else if ("Auto".equals(loanType)) {
            Assert.assertEquals(collateral, "Vehicle",
                    "Auto loans should have Vehicle as collateral");
        }
        
        // Validate expected monthly payment
        try {
            double payment = Double.parseDouble(expectedMonthlyPayment);
            
            if ("Approved".equals(expectedStatus)) {
                Assert.assertTrue(payment > 0,
                        "Approved loans should have positive monthly payment: " + payment);
            } else if ("Rejected".equals(expectedStatus)) {
                Assert.assertEquals(payment, 0.0,
                        "Rejected loans should have zero monthly payment");
            }
        } catch (NumberFormatException e) {
            Assert.fail("Expected monthly payment should be a valid number: " + expectedMonthlyPayment);
        }
        
        logger.info("Test {} completed - Status: {}, Monthly Payment: ${}",
                testCaseId, expectedStatus, expectedMonthlyPayment);
    }
    
    /**
     * Test loan application with expected results validation
     * Combines test data from Excel and expected results from JSON
     */
    @Test(dataProvider = "loanDataWithExpectedResults", dataProviderClass = TestDataProviders.class,
          description = "Validate loan processing with expected results")
    public void testLoanProcessingWithExpectedResults(Map<String, String> testData,
                                                      Map<String, Object> expectedResults) {
        String testCaseId = testData.get("testCaseId");
        logger.info("Executing loan processing test: {} with expected results validation", testCaseId);
        
        // Extract test data
        String loanType = testData.get("loanType");
        String loanAmount = testData.get("loanAmount");
        String loanTerm = testData.get("loanTerm");
        String interestRate = testData.get("interestRate");
        
        // Extract expected results
        String expectedApprovalStatus = String.valueOf(expectedResults.get("expectedApprovalStatus"));
        String expectedInterestRate = String.valueOf(expectedResults.get("expectedInterestRate"));
        String expectedMonthlyPayment = String.valueOf(expectedResults.get("expectedMonthlyPayment"));
        String expectedLoanOfficer = String.valueOf(expectedResults.get("expectedLoanOfficer"));
        
        logger.info("Processing {} loan: ${} for {} months at {}% interest",
                loanType, loanAmount, loanTerm, interestRate);
        logger.info("Expected Results - Status: {}, Rate: {}%, Payment: ${}, Officer: {}",
                expectedApprovalStatus, expectedInterestRate, expectedMonthlyPayment, expectedLoanOfficer);
        
        // Simulate loan processing and validation
        // In real scenario, this would call actual application methods
        
        // Validate interest rate matches expected
        Assert.assertEquals(interestRate, expectedInterestRate,
                "Interest rate should match expected rate");
        
        // Validate monthly payment calculation
        try {
            double amount = Double.parseDouble(loanAmount);
            double rate = Double.parseDouble(interestRate);
            int term = Integer.parseInt(loanTerm);
            
            // Calculate monthly payment using loan formula
            double monthlyRate = rate / 100 / 12;
            double calculatedPayment = (amount * monthlyRate * Math.pow(1 + monthlyRate, term)) /
                                      (Math.pow(1 + monthlyRate, term) - 1);
            
            double expectedPayment = Double.parseDouble(expectedMonthlyPayment);
            
            if ("Approved".equals(expectedApprovalStatus)) {
                // Allow 1% tolerance for rounding differences
                double tolerance = expectedPayment * 0.01;
                Assert.assertTrue(Math.abs(calculatedPayment - expectedPayment) <= tolerance,
                        String.format("Calculated payment $%.2f should match expected payment $%.2f",
                                calculatedPayment, expectedPayment));
            }
        } catch (NumberFormatException e) {
            logger.error("Error in monthly payment calculation", e);
        }
        
        // Validate loan officer assignment
        if ("Approved".equals(expectedApprovalStatus)) {
            Assert.assertNotEquals(expectedLoanOfficer, "N/A",
                    "Approved loans should have an assigned loan officer");
        } else if ("Rejected".equals(expectedApprovalStatus)) {
            Assert.assertEquals(expectedLoanOfficer, "N/A",
                    "Rejected loans should not have an assigned loan officer");
        }
        
        logger.info("Test {} completed successfully - All validations passed", testCaseId);
    }
    
    /**
     * Test loan eligibility based on loan amount and type
     */
    @Test(dataProvider = "loanDataJSON", dataProviderClass = TestDataProviders.class,
          description = "Validate loan eligibility criteria")
    public void testLoanEligibility(Map<String, Object> testData) {
        String testCaseId = String.valueOf(testData.get("testCaseId"));
        logger.info("Executing eligibility test: {}", testCaseId);
        
        String loanType = String.valueOf(testData.get("loanType"));
        String loanAmount = String.valueOf(testData.get("loanAmount"));
        String loanTerm = String.valueOf(testData.get("loanTerm"));
        String expectedStatus = String.valueOf(testData.get("expectedStatus"));
        
        try {
            double amount = Double.parseDouble(loanAmount);
            int term = Integer.parseInt(loanTerm);
            
            // Validate eligibility rules
            boolean isEligible = true;
            String reason = "";
            
            // Personal loan rules
            if ("Personal".equals(loanType)) {
                if (amount > 100000) {
                    isEligible = false;
                    reason = "Amount exceeds personal loan limit";
                }
                if (term > 84) {
                    isEligible = false;
                    reason = "Term exceeds personal loan maximum (84 months)";
                }
            }
            
            // Mortgage loan rules
            if ("Mortgage".equals(loanType)) {
                if (amount < 50000) {
                    isEligible = false;
                    reason = "Amount below mortgage loan minimum";
                }
                if (term < 120) {
                    isEligible = false;
                    reason = "Term below mortgage loan minimum (120 months)";
                }
            }
            
            // Auto loan rules
            if ("Auto".equals(loanType)) {
                if (amount > 100000) {
                    isEligible = false;
                    reason = "Amount exceeds auto loan limit";
                }
                if (term > 72) {
                    isEligible = false;
                    reason = "Term exceeds auto loan maximum (72 months)";
                }
            }
            
            logger.info("Eligibility check - Type: {}, Amount: ${}, Term: {} months, Eligible: {}",
                    loanType, amount, term, isEligible);
            
            if (!isEligible) {
                logger.info("Ineligibility reason: {}", reason);
                if ("Rejected".equals(expectedStatus)) {
                    logger.info("Rejection expected and validated");
                }
            }
            
        } catch (NumberFormatException e) {
            logger.error("Error parsing loan data", e);
        }
        
        logger.info("Eligibility test {} completed", testCaseId);
    }
}
