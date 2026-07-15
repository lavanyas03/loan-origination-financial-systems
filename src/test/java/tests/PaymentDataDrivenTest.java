package tests;

import base.BaseTest;
import dataproviders.TestDataProviders;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Map;

/**
 * Parameterized tests for Payment module using Excel and JSON data providers
 * Demonstrates payment processing validation with data-driven approach
 */
public class PaymentDataDrivenTest extends BaseTest {
    
    private static final Logger logger = LogManager.getLogger(PaymentDataDrivenTest.class);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    /**
     * Test payment processing validation using Excel DataProvider
     */
    @Test(dataProvider = "paymentDataExcel", dataProviderClass = TestDataProviders.class,
          description = "Validate payment processing data from Excel")
    public void testPaymentProcessingWithExcel(Map<String, String> testData) {
        String testCaseId = testData.get("testCaseId");
        logger.info("Executing payment test: {} using Excel data", testCaseId);
        
        // Extract test data
        String loanId = testData.get("loanId");
        String paymentAmount = testData.get("paymentAmount");
        String paymentDate = testData.get("paymentDate");
        String paymentMethod = testData.get("paymentMethod");
        String accountNumber = testData.get("accountNumber");
        String expectedStatus = testData.get("expectedStatus");
        
        logger.info("Payment Details - Loan: {}, Amount: ${}, Date: {}, Method: {}",
                loanId, paymentAmount, paymentDate, paymentMethod);
        
        // Validate required fields
        Assert.assertNotNull(loanId, "Loan ID should not be null");
        Assert.assertNotNull(paymentAmount, "Payment amount should not be null");
        Assert.assertNotNull(paymentDate, "Payment date should not be null");
        Assert.assertNotNull(paymentMethod, "Payment method should not be null");
        
        // Validate payment amount
        try {
            double amount = Double.parseDouble(paymentAmount);
            Assert.assertTrue(amount > 0, "Payment amount should be positive: " + amount);
            Assert.assertTrue(amount <= 1000000,
                    "Payment amount should not exceed $1,000,000: " + amount);
        } catch (NumberFormatException e) {
            Assert.fail("Payment amount should be a valid number: " + paymentAmount);
        }
        
        // Validate payment date format and value
        try {
            LocalDate date = LocalDate.parse(paymentDate, DATE_FORMATTER);
            LocalDate today = LocalDate.now();
            
            Assert.assertFalse(date.isAfter(today.plusYears(1)),
                    "Payment date should not be more than 1 year in the future: " + date);
            
            logger.debug("Payment date validated: {}", date);
        } catch (DateTimeParseException e) {
            Assert.fail("Payment date should be in yyyy-MM-dd format: " + paymentDate);
        }
        
        // Validate payment method
        String[] validPaymentMethods = {"ACH", "Credit Card", "Debit Card", "Check", "Wire Transfer"};
        boolean validPaymentMethod = false;
        for (String method : validPaymentMethods) {
            if (method.equals(paymentMethod)) {
                validPaymentMethod = true;
                break;
            }
        }
        Assert.assertTrue(validPaymentMethod, "Payment method should be valid: " + paymentMethod);
        
        // Validate account number format (masked)
        Assert.assertTrue(accountNumber.startsWith("****"),
                "Account number should be masked starting with ****: " + accountNumber);
        Assert.assertTrue(accountNumber.length() >= 8,
                "Masked account number should have sufficient length: " + accountNumber);
        
        logger.info("Test {} completed - Expected Status: {}", testCaseId, expectedStatus);
    }
    
    /**
     * Test payment processing validation using JSON DataProvider
     */
    @Test(dataProvider = "paymentDataJSON", dataProviderClass = TestDataProviders.class,
          description = "Validate payment processing data from JSON")
    public void testPaymentProcessingWithJSON(Map<String, Object> testData) {
        String testCaseId = String.valueOf(testData.get("testCaseId"));
        logger.info("Executing payment test: {} using JSON data", testCaseId);
        
        // Extract test data
        String loanId = String.valueOf(testData.get("loanId"));
        String paymentAmount = String.valueOf(testData.get("paymentAmount"));
        String paymentMethod = String.valueOf(testData.get("paymentMethod"));
        String expectedStatus = String.valueOf(testData.get("expectedStatus"));
        String expectedBalance = String.valueOf(testData.get("expectedBalance"));
        
        logger.info("Payment Details - Loan: {}, Amount: ${}, Method: {}, Expected Status: {}",
                loanId, paymentAmount, paymentMethod, expectedStatus);
        
        // Validate payment status based on amount
        try {
            double amount = Double.parseDouble(paymentAmount);
            double balance = Double.parseDouble(expectedBalance);
            
            // Validate expected balance is non-negative
            Assert.assertTrue(balance >= 0,
                    "Expected balance should be non-negative: " + balance);
            
            // Validate status consistency
            if ("Success".equals(expectedStatus)) {
                Assert.assertTrue(amount > 0,
                        "Successful payments should have positive amount: " + amount);
            } else if ("Partial Payment".equals(expectedStatus)) {
                // Partial payment indicates payment amount is less than due amount
                Assert.assertTrue(amount > 0,
                        "Partial payments should have positive amount: " + amount);
            }
            
        } catch (NumberFormatException e) {
            Assert.fail("Payment amounts should be valid numbers");
        }
        
        // Validate payment method specific rules
        if ("Credit Card".equals(paymentMethod) || "Debit Card".equals(paymentMethod)) {
            try {
                double amount = Double.parseDouble(paymentAmount);
                Assert.assertTrue(amount <= 50000,
                        "Card payments should not exceed $50,000: " + amount);
            } catch (NumberFormatException e) {
                logger.error("Error parsing payment amount", e);
            }
        }
        
        logger.info("Test {} completed - Balance After Payment: ${}", testCaseId, expectedBalance);
    }
    
    /**
     * Test payment date validations
     */
    @Test(dataProvider = "paymentDataJSON", dataProviderClass = TestDataProviders.class,
          description = "Validate payment date constraints")
    public void testPaymentDateValidation(Map<String, Object> testData) {
        String testCaseId = String.valueOf(testData.get("testCaseId"));
        logger.info("Executing payment date validation test: {}", testCaseId);
        
        String paymentDate = String.valueOf(testData.get("paymentDate"));
        
        try {
            LocalDate date = LocalDate.parse(paymentDate, DATE_FORMATTER);
            LocalDate today = LocalDate.now();
            
            logger.info("Payment Date: {}, Today: {}", date, today);
            
            // Validate payment is not in the past (for scheduled payments)
            // For this test, we'll check if it's within reasonable range
            LocalDate minDate = today.minusYears(1);
            LocalDate maxDate = today.plusMonths(6);
            
            Assert.assertFalse(date.isBefore(minDate),
                    "Payment date should not be more than 1 year in the past: " + date);
            Assert.assertFalse(date.isAfter(maxDate),
                    "Payment date should not be more than 6 months in the future: " + date);
            
            // Validate it's not a weekend for ACH payments
            String paymentMethod = String.valueOf(testData.get("paymentMethod"));
            if ("ACH".equals(paymentMethod)) {
                int dayOfWeek = date.getDayOfWeek().getValue();
                if (dayOfWeek == 6 || dayOfWeek == 7) {
                    logger.warn("ACH payment scheduled on weekend: {}", date);
                    // In real scenario, this might auto-adjust to next business day
                }
            }
            
        } catch (DateTimeParseException e) {
            Assert.fail("Payment date should be in valid format: " + paymentDate);
        }
        
        logger.info("Payment date validation test {} completed", testCaseId);
    }
    
    /**
     * Test payment method validations and constraints
     */
    @Test(dataProvider = "paymentDataExcel", dataProviderClass = TestDataProviders.class,
          description = "Validate payment method specific constraints")
    public void testPaymentMethodConstraints(Map<String, String> testData) {
        String testCaseId = testData.get("testCaseId");
        logger.info("Executing payment method constraints test: {}", testCaseId);
        
        String paymentMethod = testData.get("paymentMethod");
        String paymentAmount = testData.get("paymentAmount");
        String accountNumber = testData.get("accountNumber");
        
        logger.info("Validating {} payment for ${}", paymentMethod, paymentAmount);
        
        try {
            double amount = Double.parseDouble(paymentAmount);
            
            // Method-specific validations
            switch (paymentMethod) {
                case "ACH":
                    // ACH typically has no upper limit but has processing time
                    Assert.assertTrue(accountNumber.length() >= 8,
                            "ACH account number should be valid length");
                    logger.debug("ACH payment validated - 1-3 business days processing time");
                    break;
                    
                case "Credit Card":
                case "Debit Card":
                    // Card payments have transaction limits
                    Assert.assertTrue(amount <= 50000,
                            "Card payment amount should not exceed $50,000: " + amount);
                    logger.debug("Card payment validated - instant processing");
                    break;
                    
                case "Check":
                    // Check payments need verification period
                    logger.debug("Check payment validated - 3-5 business days processing time");
                    break;
                    
                case "Wire Transfer":
                    // Wire transfers typically for large amounts
                    if (amount < 10000) {
                        logger.warn("Wire transfer for small amount: ${} - consider alternative method", amount);
                    }
                    logger.debug("Wire transfer validated - same day processing");
                    break;
                    
                default:
                    Assert.fail("Unknown payment method: " + paymentMethod);
            }
            
        } catch (NumberFormatException e) {
            Assert.fail("Payment amount should be a valid number: " + paymentAmount);
        }
        
        logger.info("Payment method constraints test {} completed", testCaseId);
    }
    
    /**
     * Test balance calculation after payment
     */
    @Test(dataProvider = "paymentDataJSON", dataProviderClass = TestDataProviders.class,
          description = "Validate balance calculation after payment")
    public void testBalanceCalculation(Map<String, Object> testData) {
        String testCaseId = String.valueOf(testData.get("testCaseId"));
        logger.info("Executing balance calculation test: {}", testCaseId);
        
        String loanId = String.valueOf(testData.get("loanId"));
        String paymentAmount = String.valueOf(testData.get("paymentAmount"));
        String expectedBalance = String.valueOf(testData.get("expectedBalance"));
        String expectedStatus = String.valueOf(testData.get("expectedStatus"));
        
        try {
            double amount = Double.parseDouble(paymentAmount);
            double balance = Double.parseDouble(expectedBalance);
            
            logger.info("Loan: {}, Payment: ${}, Expected Balance: ${}, Status: {}",
                    loanId, amount, balance, expectedStatus);
            
            // Validate balance is reasonable
            Assert.assertTrue(balance >= 0,
                    "Balance should not be negative: " + balance);
            
            // For successful full payments, balance should decrease
            if ("Success".equals(expectedStatus)) {
                Assert.assertTrue(amount > 0,
                        "Payment amount should be positive for successful payment");
                
                // In real scenario, we'd fetch original balance and verify:
                // newBalance = originalBalance - paymentAmount + interest - fees
            }
            
            // For partial payments, balance should be greater than zero
            if ("Partial Payment".equals(expectedStatus)) {
                Assert.assertTrue(balance > 0,
                        "Partial payment should leave remaining balance: " + balance);
            }
            
        } catch (NumberFormatException e) {
            Assert.fail("Numeric values should be valid: " + e.getMessage());
        }
        
        logger.info("Balance calculation test {} completed", testCaseId);
    }
    
    /**
     * Test multiple payment scenarios for the same loan
     */
    @Test(dataProvider = "paymentDataJSON", dataProviderClass = TestDataProviders.class,
          description = "Validate payment transaction processing")
    public void testPaymentTransactionProcessing(Map<String, Object> testData) {
        String testCaseId = String.valueOf(testData.get("testCaseId"));
        logger.info("Executing payment transaction test: {}", testCaseId);
        
        String loanId = String.valueOf(testData.get("loanId"));
        String paymentAmount = String.valueOf(testData.get("paymentAmount"));
        String paymentMethod = String.valueOf(testData.get("paymentMethod"));
        String expectedStatus = String.valueOf(testData.get("expectedStatus"));
        
        // Simulate payment transaction processing
        logger.info("Processing payment transaction - Loan: {}, Amount: ${}, Method: {}",
                loanId, paymentAmount, paymentMethod);
        
        // Validate transaction can be processed
        Assert.assertNotNull(loanId, "Loan ID is required for payment processing");
        Assert.assertNotNull(paymentAmount, "Payment amount is required");
        Assert.assertNotNull(paymentMethod, "Payment method is required");
        
        // Validate expected status is one of the valid statuses
        String[] validStatuses = {"Success", "Failed", "Pending", "Partial Payment", "Declined"};
        boolean validStatus = false;
        for (String status : validStatuses) {
            if (status.equals(expectedStatus)) {
                validStatus = true;
                break;
            }
        }
        Assert.assertTrue(validStatus, "Expected status should be valid: " + expectedStatus);
        
        // Log transaction details for audit trail
        logger.info("Transaction processed - Test: {}, Status: {}", testCaseId, expectedStatus);
        logger.info("Transaction ID: TXN-{}-{}", testCaseId, System.currentTimeMillis());
        
        logger.info("Payment transaction test {} completed", testCaseId);
    }
}
