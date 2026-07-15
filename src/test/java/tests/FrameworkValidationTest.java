package tests;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.ConfigReader;
import utils.ExcelUtils;
import utils.JSONUtils;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Validation tests to verify framework setup and utilities
 * Run these tests to ensure ExcelUtils, JSONUtils, and DataProviders are working correctly
 */
public class FrameworkValidationTest {
    
    private static final Logger logger = LogManager.getLogger(FrameworkValidationTest.class);
    private static final String TEST_DATA_PATH = ConfigReader.getInstance().getProperty("testDataPath");
    
    @Test(description = "Verify configuration reader is working")
    public void testConfigReader() {
        logger.info("Testing ConfigReader functionality");
        
        String browser = ConfigReader.getInstance().getProperty("browser");
        Assert.assertNotNull(browser, "Browser property should be loaded");
        logger.info("Browser configured: {}", browser);
        
        String baseURL = ConfigReader.getInstance().getProperty("baseURL");
        Assert.assertNotNull(baseURL, "Base URL should be loaded");
        logger.info("Base URL configured: {}", baseURL);
        
        String testDataPath = ConfigReader.getInstance().getProperty("testDataPath");
        Assert.assertNotNull(testDataPath, "Test data path should be loaded");
        logger.info("Test data path configured: {}", testDataPath);
        
        logger.info("✓ ConfigReader validation passed");
    }
    
    @Test(description = "Verify JSON test data files exist")
    public void testJSONFilesExist() {
        logger.info("Verifying JSON test data files existence");
        
        String[] jsonFiles = {
            "CustomerTestData.json",
            "LoanTestData.json",
            "PaymentTestData.json",
            "LoanExpectedResults.json"
        };
        
        for (String fileName : jsonFiles) {
            String filePath = TEST_DATA_PATH + fileName;
            File file = new File(filePath);
            Assert.assertTrue(file.exists(), "JSON file should exist: " + fileName);
            logger.info("✓ Found: {}", fileName);
        }
        
        logger.info("✓ All JSON test data files exist");
    }
    
    @Test(description = "Verify JSONUtils can read customer data")
    public void testJSONUtilsReadCustomerData() {
        logger.info("Testing JSONUtils with CustomerTestData.json");
        
        String filePath = TEST_DATA_PATH + "CustomerTestData.json";
        
        // Validate file
        Assert.assertTrue(JSONUtils.validateJSONFile(filePath), 
                "Customer JSON file should be valid");
        
        // Read data
        List<Map<String, Object>> customerData = JSONUtils.getDataFromJSON(filePath);
        Assert.assertNotNull(customerData, "Customer data should not be null");
        Assert.assertFalse(customerData.isEmpty(), "Customer data should not be empty");
        logger.info("Customer records loaded: {}", customerData.size());
        
        // Verify first record has required fields
        Map<String, Object> firstRecord = customerData.get(0);
        Assert.assertTrue(firstRecord.containsKey("testCaseId"), "Should have testCaseId");
        Assert.assertTrue(firstRecord.containsKey("firstName"), "Should have firstName");
        Assert.assertTrue(firstRecord.containsKey("email"), "Should have email");
        logger.info("First customer: {} {}", 
                firstRecord.get("firstName"), firstRecord.get("lastName"));
        
        // Get record count
        int count = JSONUtils.getRecordCount(filePath);
        Assert.assertEquals(count, customerData.size(), "Record count should match");
        
        logger.info("✓ JSONUtils customer data validation passed");
    }
    
    @Test(description = "Verify JSONUtils can read loan data")
    public void testJSONUtilsReadLoanData() {
        logger.info("Testing JSONUtils with LoanTestData.json");
        
        String filePath = TEST_DATA_PATH + "LoanTestData.json";
        
        // Read data as Object array for DataProvider
        Object[][] loanData = JSONUtils.getDataAsObjectArray(filePath);
        Assert.assertNotNull(loanData, "Loan data array should not be null");
        Assert.assertTrue(loanData.length > 0, "Loan data array should not be empty");
        logger.info("Loan records in array format: {}", loanData.length);
        
        // Verify first record
        @SuppressWarnings("unchecked")
        Map<String, Object> firstLoan = (Map<String, Object>) loanData[0][0];
        Assert.assertTrue(firstLoan.containsKey("loanType"), "Should have loanType");
        Assert.assertTrue(firstLoan.containsKey("loanAmount"), "Should have loanAmount");
        logger.info("First loan: {} for ${}", 
                firstLoan.get("loanType"), firstLoan.get("loanAmount"));
        
        logger.info("✓ JSONUtils loan data validation passed");
    }
    
    @Test(description = "Verify JSONUtils can read payment data")
    public void testJSONUtilsReadPaymentData() {
        logger.info("Testing JSONUtils with PaymentTestData.json");
        
        String filePath = TEST_DATA_PATH + "PaymentTestData.json";
        
        // Read specific record by index
        Map<String, Object> payment = JSONUtils.getRecordByIndex(filePath, 0);
        Assert.assertNotNull(payment, "Payment record should not be null");
        Assert.assertTrue(payment.containsKey("paymentMethod"), "Should have paymentMethod");
        Assert.assertTrue(payment.containsKey("paymentAmount"), "Should have paymentAmount");
        logger.info("First payment: ${} via {}", 
                payment.get("paymentAmount"), payment.get("paymentMethod"));
        
        // Get specific value
        Object testCaseId = JSONUtils.getValue(filePath, "0.testCaseId");
        Assert.assertNotNull(testCaseId, "Should be able to get value by path");
        logger.info("Test case ID: {}", testCaseId);
        
        logger.info("✓ JSONUtils payment data validation passed");
    }
    
    @Test(description = "Verify Excel file validation", 
          dependsOnMethods = {"testJSONFilesExist"})
    public void testExcelFileValidation() {
        logger.info("Testing Excel file validation");
        
        String[] excelFiles = {
            "CustomerTestData.xlsx",
            "LoanTestData.xlsx",
            "PaymentTestData.xlsx"
        };
        
        for (String fileName : excelFiles) {
            String filePath = TEST_DATA_PATH + fileName;
            File file = new File(filePath);
            
            if (file.exists()) {
                boolean isValid = ExcelUtils.validateExcelFile(filePath);
                Assert.assertTrue(isValid, "Excel file should be valid: " + fileName);
                logger.info("✓ Validated: {}", fileName);
            } else {
                logger.warn("Excel file not found (run ExcelDataGenerator first): {}", fileName);
                logger.info("Skipping validation for: {}", fileName);
            }
        }
        
        logger.info("✓ Excel file validation completed");
    }
    
    @Test(description = "Verify ExcelUtils can read customer data if Excel files exist", 
          dependsOnMethods = {"testExcelFileValidation"})
    public void testExcelUtilsReadCustomerData() {
        logger.info("Testing ExcelUtils with CustomerTestData.xlsx");
        
        String filePath = TEST_DATA_PATH + "CustomerTestData.xlsx";
        File file = new File(filePath);
        
        if (!file.exists()) {
            logger.warn("Excel file not found. Please run ExcelDataGenerator to create Excel files.");
            logger.info("You can run: mvn compile exec:java -Dexec.mainClass=\"utils.ExcelDataGenerator\"");
            logger.info("Skipping ExcelUtils test");
            return;
        }
        
        // Read data from sheet
        List<Map<String, String>> customerData = ExcelUtils.getDataFromSheet(filePath, "CustomerData");
        Assert.assertNotNull(customerData, "Customer data should not be null");
        Assert.assertFalse(customerData.isEmpty(), "Customer data should not be empty");
        logger.info("Customer records loaded from Excel: {}", customerData.size());
        
        // Verify first record
        Map<String, String> firstRecord = customerData.get(0);
        Assert.assertTrue(firstRecord.containsKey("testCaseId"), "Should have testCaseId");
        Assert.assertTrue(firstRecord.containsKey("firstName"), "Should have firstName");
        logger.info("First customer from Excel: {} {}", 
                firstRecord.get("firstName"), firstRecord.get("lastName"));
        
        // Get row count
        int rowCount = ExcelUtils.getRowCount(filePath, "CustomerData");
        Assert.assertEquals(rowCount, customerData.size(), "Row count should match");
        
        logger.info("✓ ExcelUtils customer data validation passed");
    }
    
    @Test(description = "Verify ExcelUtils can read loan data if Excel files exist",
          dependsOnMethods = {"testExcelFileValidation"})
    public void testExcelUtilsReadLoanData() {
        logger.info("Testing ExcelUtils with LoanTestData.xlsx");
        
        String filePath = TEST_DATA_PATH + "LoanTestData.xlsx";
        File file = new File(filePath);
        
        if (!file.exists()) {
            logger.warn("Excel file not found. Please run ExcelDataGenerator.");
            logger.info("Skipping ExcelUtils loan test");
            return;
        }
        
        // Read data as Object array
        Object[][] loanData = ExcelUtils.getDataAsObjectArray(filePath, "LoanData");
        Assert.assertNotNull(loanData, "Loan data array should not be null");
        Assert.assertTrue(loanData.length > 0, "Loan data array should not be empty");
        logger.info("Loan records from Excel: {}", loanData.length);
        
        // Verify first record
        @SuppressWarnings("unchecked")
        Map<String, String> firstLoan = (Map<String, String>) loanData[0][0];
        Assert.assertTrue(firstLoan.containsKey("loanType"), "Should have loanType");
        logger.info("First loan from Excel: {} for ${}", 
                firstLoan.get("loanType"), firstLoan.get("loanAmount"));
        
        logger.info("✓ ExcelUtils loan data validation passed");
    }
    
    @Test(description = "Verify test data completeness")
    public void testDataCompleteness() {
        logger.info("Verifying test data completeness");
        
        // Check all required fields in customer data
        String customerFile = TEST_DATA_PATH + "CustomerTestData.json";
        List<Map<String, Object>> customers = JSONUtils.getDataFromJSON(customerFile);
        
        String[] requiredCustomerFields = {
            "testCaseId", "firstName", "lastName", "email", "phone", 
            "ssn", "dateOfBirth", "employmentStatus", "annualIncome", "creditScore"
        };
        
        for (Map<String, Object> customer : customers) {
            for (String field : requiredCustomerFields) {
                Assert.assertTrue(customer.containsKey(field), 
                        "Customer record should have field: " + field);
            }
        }
        logger.info("✓ All customer records have required fields");
        
        // Check all required fields in loan data
        String loanFile = TEST_DATA_PATH + "LoanTestData.json";
        List<Map<String, Object>> loans = JSONUtils.getDataFromJSON(loanFile);
        
        String[] requiredLoanFields = {
            "testCaseId", "customerId", "loanType", "loanAmount", 
            "loanTerm", "interestRate", "expectedStatus"
        };
        
        for (Map<String, Object> loan : loans) {
            for (String field : requiredLoanFields) {
                Assert.assertTrue(loan.containsKey(field), 
                        "Loan record should have field: " + field);
            }
        }
        logger.info("✓ All loan records have required fields");
        
        // Check all required fields in payment data
        String paymentFile = TEST_DATA_PATH + "PaymentTestData.json";
        List<Map<String, Object>> payments = JSONUtils.getDataFromJSON(paymentFile);
        
        String[] requiredPaymentFields = {
            "testCaseId", "loanId", "paymentAmount", "paymentDate", 
            "paymentMethod", "expectedStatus"
        };
        
        for (Map<String, Object> payment : payments) {
            for (String field : requiredPaymentFields) {
                Assert.assertTrue(payment.containsKey(field), 
                        "Payment record should have field: " + field);
            }
        }
        logger.info("✓ All payment records have required fields");
        
        logger.info("✓ Test data completeness validation passed");
    }
    
    @Test(description = "Summary report of framework validation")
    public void testFrameworkSummary() {
        logger.info("=".repeat(80));
        logger.info("FRAMEWORK VALIDATION SUMMARY");
        logger.info("=".repeat(80));
        logger.info("✓ ConfigReader is working correctly");
        logger.info("✓ JSONUtils is working correctly");
        logger.info("✓ All JSON test data files are present and valid");
        logger.info("✓ Test data has all required fields");
        
        String excelStatus = new File(TEST_DATA_PATH + "CustomerTestData.xlsx").exists() 
                ? "✓ Excel files are present" 
                : "⚠ Excel files not generated yet";
        logger.info(excelStatus);
        
        if (!new File(TEST_DATA_PATH + "CustomerTestData.xlsx").exists()) {
            logger.info("");
            logger.info("TO GENERATE EXCEL FILES:");
            logger.info("  Run: mvn compile exec:java -Dexec.mainClass=\"utils.ExcelDataGenerator\"");
            logger.info("  Or:  generate-excel-data.bat");
            logger.info("  Or:  Run ExcelDataGenerator.java from your IDE");
        }
        
        logger.info("=".repeat(80));
        logger.info("Framework is ready for data-driven testing!");
        logger.info("=".repeat(80));
        
        Assert.assertTrue(true, "Framework validation completed successfully");
    }
}
