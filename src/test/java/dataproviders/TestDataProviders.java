package dataproviders;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.DataProvider;
import utils.ConfigReader;
import utils.ExcelUtils;
import utils.JSONUtils;

import java.io.File;

/**
 * Reusable TestNG DataProviders for test data management
 * Supports reading data from Excel and JSON files
 */
public class TestDataProviders {
    
    private static final Logger logger = LogManager.getLogger(TestDataProviders.class);
    private static final String TEST_DATA_PATH = ConfigReader.getInstance().getProperty("testDataPath");
    
    /**
     * DataProvider for Customer module test data from Excel
     * 
     * @return 2D Object array containing customer test data
     */
    @DataProvider(name = "customerDataExcel")
    public static Object[][] getCustomerDataFromExcel() {
        String filePath = TEST_DATA_PATH + "CustomerTestData.xlsx";
        logger.info("Loading customer data from Excel: {}", filePath);
        
        try {
            if (ExcelUtils.validateExcelFile(filePath)) {
                return ExcelUtils.getDataAsObjectArray(filePath, "CustomerData");
            } else {
                logger.error("Invalid or missing Excel file: {}", filePath);
                return new Object[0][0];
            }
        } catch (Exception e) {
            logger.error("Error loading customer data from Excel", e);
            return new Object[0][0];
        }
    }
    
    /**
     * DataProvider for Customer module test data from JSON
     * 
     * @return 2D Object array containing customer test data
     */
    @DataProvider(name = "customerDataJSON")
    public static Object[][] getCustomerDataFromJSON() {
        String filePath = TEST_DATA_PATH + "CustomerTestData.json";
        logger.info("Loading customer data from JSON: {}", filePath);
        
        try {
            if (JSONUtils.validateJSONFile(filePath)) {
                return JSONUtils.getDataAsObjectArray(filePath);
            } else {
                logger.error("Invalid or missing JSON file: {}", filePath);
                return new Object[0][0];
            }
        } catch (Exception e) {
            logger.error("Error loading customer data from JSON", e);
            return new Object[0][0];
        }
    }
    
    /**
     * DataProvider for Customer Registration Validation test data from JSON
     * Includes negative test scenarios for field validation testing
     * 
     * @return 2D Object array containing customer validation test data
     */
    @DataProvider(name = "customerValidationDataJSON")
    public static Object[][] getCustomerValidationDataFromJSON() {
        String filePath = TEST_DATA_PATH + "CustomerValidationTestData.json";
        logger.info("Loading customer validation data from JSON: {}", filePath);
        
        try {
            if (JSONUtils.validateJSONFile(filePath)) {
                return JSONUtils.getDataAsObjectArray(filePath);
            } else {
                logger.error("Invalid or missing JSON file: {}", filePath);
                return new Object[0][0];
            }
        } catch (Exception e) {
            logger.error("Error loading customer validation data from JSON", e);
            return new Object[0][0];
        }
    }
    
    /**
     * DataProvider for Loan module test data from Excel
     * 
     * @return 2D Object array containing loan test data
     */
    @DataProvider(name = "loanDataExcel")
    public static Object[][] getLoanDataFromExcel() {
        String filePath = TEST_DATA_PATH + "LoanTestData.xlsx";
        logger.info("Loading loan data from Excel: {}", filePath);
        
        try {
            if (ExcelUtils.validateExcelFile(filePath)) {
                return ExcelUtils.getDataAsObjectArray(filePath, "LoanData");
            } else {
                logger.error("Invalid or missing Excel file: {}", filePath);
                return new Object[0][0];
            }
        } catch (Exception e) {
            logger.error("Error loading loan data from Excel", e);
            return new Object[0][0];
        }
    }
    
    /**
     * DataProvider for Loan module test data from JSON
     * 
     * @return 2D Object array containing loan test data
     */
    @DataProvider(name = "loanDataJSON")
    public static Object[][] getLoanDataFromJSON() {
        String filePath = TEST_DATA_PATH + "LoanTestData.json";
        logger.info("Loading loan data from JSON: {}", filePath);
        
        try {
            if (JSONUtils.validateJSONFile(filePath)) {
                return JSONUtils.getDataAsObjectArray(filePath);
            } else {
                logger.error("Invalid or missing JSON file: {}", filePath);
                return new Object[0][0];
            }
        } catch (Exception e) {
            logger.error("Error loading loan data from JSON", e);
            return new Object[0][0];
        }
    }
    
    /**
     * DataProvider for Payment module test data from Excel
     * 
     * @return 2D Object array containing payment test data
     */
    @DataProvider(name = "paymentDataExcel")
    public static Object[][] getPaymentDataFromExcel() {
        String filePath = TEST_DATA_PATH + "PaymentTestData.xlsx";
        logger.info("Loading payment data from Excel: {}", filePath);
        
        try {
            if (ExcelUtils.validateExcelFile(filePath)) {
                return ExcelUtils.getDataAsObjectArray(filePath, "PaymentData");
            } else {
                logger.error("Invalid or missing Excel file: {}", filePath);
                return new Object[0][0];
            }
        } catch (Exception e) {
            logger.error("Error loading payment data from Excel", e);
            return new Object[0][0];
        }
    }
    
    /**
     * DataProvider for Payment module test data from JSON
     * 
     * @return 2D Object array containing payment test data
     */
    @DataProvider(name = "paymentDataJSON")
    public static Object[][] getPaymentDataFromJSON() {
        String filePath = TEST_DATA_PATH + "PaymentTestData.json";
        logger.info("Loading payment data from JSON: {}", filePath);
        
        try {
            if (JSONUtils.validateJSONFile(filePath)) {
                return JSONUtils.getDataAsObjectArray(filePath);
            } else {
                logger.error("Invalid or missing JSON file: {}", filePath);
                return new Object[0][0];
            }
        } catch (Exception e) {
            logger.error("Error loading payment data from JSON", e);
            return new Object[0][0];
        }
    }
    
    /**
     * Generic DataProvider for reading data from any Excel file and sheet
     * 
     * @param fileName Name of the Excel file (without path)
     * @param sheetName Name of the sheet to read
     * @return 2D Object array containing test data
     */
    public static Object[][] getDataFromExcel(String fileName, String sheetName) {
        String filePath = TEST_DATA_PATH + fileName;
        logger.info("Loading data from Excel: {} | Sheet: {}", filePath, sheetName);
        
        try {
            if (ExcelUtils.validateExcelFile(filePath)) {
                return ExcelUtils.getDataAsObjectArray(filePath, sheetName);
            } else {
                logger.error("Invalid or missing Excel file: {}", filePath);
                return new Object[0][0];
            }
        } catch (Exception e) {
            logger.error("Error loading data from Excel", e);
            return new Object[0][0];
        }
    }
    
    /**
     * Generic DataProvider for reading data from any JSON file
     * 
     * @param fileName Name of the JSON file (without path)
     * @return 2D Object array containing test data
     */
    public static Object[][] getDataFromJSON(String fileName) {
        String filePath = TEST_DATA_PATH + fileName;
        logger.info("Loading data from JSON: {}", filePath);
        
        try {
            if (JSONUtils.validateJSONFile(filePath)) {
                return JSONUtils.getDataAsObjectArray(filePath);
            } else {
                logger.error("Invalid or missing JSON file: {}", filePath);
                return new Object[0][0];
            }
        } catch (Exception e) {
            logger.error("Error loading data from JSON", e);
            return new Object[0][0];
        }
    }
    
    /**
     * Helper method to check if test data file exists
     * 
     * @param fileName Name of the file
     * @return true if file exists, false otherwise
     */
    public static boolean isTestDataFileExists(String fileName) {
        String filePath = TEST_DATA_PATH + fileName;
        File file = new File(filePath);
        boolean exists = file.exists();
        
        if (!exists) {
            logger.warn("Test data file not found: {}", filePath);
        }
        
        return exists;
    }
    
    /**
     * DataProvider for invalid customer data scenarios from Excel
     * 
     * @return 2D Object array containing invalid customer test data
     */
    @DataProvider(name = "invalidCustomerDataExcel")
    public static Object[][] getInvalidCustomerDataFromExcel() {
        String filePath = TEST_DATA_PATH + "CustomerTestData.xlsx";
        logger.info("Loading invalid customer data from Excel: {}", filePath);
        
        try {
            if (ExcelUtils.validateExcelFile(filePath)) {
                return ExcelUtils.getDataAsObjectArray(filePath, "InvalidData");
            } else {
                logger.error("Invalid or missing Excel file: {}", filePath);
                return new Object[0][0];
            }
        } catch (Exception e) {
            logger.warn("Sheet 'InvalidData' not found, returning empty array", e);
            return new Object[0][0];
        }
    }
    
    /**
     * DataProvider that combines data from multiple sources
     * Reads expected results from JSON for validation
     * 
     * @return 2D Object array with test data and expected results
     */
    @DataProvider(name = "loanDataWithExpectedResults")
    public static Object[][] getLoanDataWithExpectedResults() {
        String dataFilePath = TEST_DATA_PATH + "LoanTestData.xlsx";
        String expectedFilePath = TEST_DATA_PATH + "LoanExpectedResults.json";
        
        logger.info("Loading loan data with expected results");
        
        try {
            if (ExcelUtils.validateExcelFile(dataFilePath) && 
                JSONUtils.validateJSONFile(expectedFilePath)) {
                
                Object[][] testData = ExcelUtils.getDataAsObjectArray(dataFilePath, "LoanData");
                Object[][] expectedData = JSONUtils.getDataAsObjectArray(expectedFilePath);
                
                if (testData.length != expectedData.length) {
                    logger.warn("Test data and expected results count mismatch");
                }
                
                int minLength = Math.min(testData.length, expectedData.length);
                Object[][] combinedData = new Object[minLength][2];
                
                for (int i = 0; i < minLength; i++) {
                    combinedData[i][0] = testData[i][0]; // Test data
                    combinedData[i][1] = expectedData[i][0]; // Expected results
                }
                
                logger.info("Combined {} test data sets with expected results", combinedData.length);
                return combinedData;
                
            } else {
                logger.error("Invalid or missing test data files");
                return new Object[0][0];
            }
        } catch (Exception e) {
            logger.error("Error loading loan data with expected results", e);
            return new Object[0][0];
        }
    }
}
