package utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Utility class to generate Excel test data files from JSON
 * Run this class to create Excel files for testing
 */
public class ExcelDataGenerator {
    
    /**
     * Main method to generate all Excel test data files
     */
    public static void main(String[] args) {
        String testDataPath = "src/test/resources/testdata/";
        
        System.out.println("Starting Excel test data file generation...\n");
        
        try {
            // Generate Customer Test Data Excel
            generateCustomerExcel(testDataPath);
            
            // Generate Loan Test Data Excel
            generateLoanExcel(testDataPath);
            
            // Generate Payment Test Data Excel
            generatePaymentExcel(testDataPath);
            
            System.out.println("\nAll Excel test data files generated successfully!");
            System.out.println("Files created in: " + testDataPath);
            
        } catch (Exception e) {
            System.err.println("Error generating Excel files: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Generate Customer Test Data Excel file
     */
    private static void generateCustomerExcel(String path) throws IOException {
        String jsonFile = path + "CustomerTestData.json";
        String excelFile = path + "CustomerTestData.xlsx";
        
        System.out.println("Generating CustomerTestData.xlsx...");
        
        List<Map<String, Object>> customerData = JSONUtils.getDataFromJSON(jsonFile);
        
        try (Workbook workbook = new XSSFWorkbook()) {
            // Create CustomerData sheet
            Sheet sheet = workbook.createSheet("CustomerData");
            
            // Create header row
            Row headerRow = sheet.createRow(0);
            String[] headers = {
                "testCaseId", "firstName", "lastName", "email", "phone",
                "ssn", "dateOfBirth", "employmentStatus", "annualIncome",
                "creditScore", "expectedResult"
            };
            
            CellStyle headerStyle = createHeaderStyle(workbook);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }
            
            // Create data rows
            for (int i = 0; i < customerData.size(); i++) {
                Row row = sheet.createRow(i + 1);
                Map<String, Object> data = customerData.get(i);
                
                row.createCell(0).setCellValue(String.valueOf(data.get("testCaseId")));
                row.createCell(1).setCellValue(String.valueOf(data.get("firstName")));
                row.createCell(2).setCellValue(String.valueOf(data.get("lastName")));
                row.createCell(3).setCellValue(String.valueOf(data.get("email")));
                row.createCell(4).setCellValue(String.valueOf(data.get("phone")));
                row.createCell(5).setCellValue(String.valueOf(data.get("ssn")));
                row.createCell(6).setCellValue(String.valueOf(data.get("dateOfBirth")));
                row.createCell(7).setCellValue(String.valueOf(data.get("employmentStatus")));
                row.createCell(8).setCellValue(String.valueOf(data.get("annualIncome")));
                row.createCell(9).setCellValue(String.valueOf(data.get("creditScore")));
                row.createCell(10).setCellValue(String.valueOf(data.get("expectedResult")));
            }
            
            // Auto-size columns
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }
            
            // Create InvalidData sheet with sample invalid data
            createInvalidDataSheet(workbook);
            
            // Write to file
            try (FileOutputStream fos = new FileOutputStream(new File(excelFile))) {
                workbook.write(fos);
            }
            
            System.out.println("✓ CustomerTestData.xlsx created successfully");
        }
    }
    
    /**
     * Generate Loan Test Data Excel file
     */
    private static void generateLoanExcel(String path) throws IOException {
        String jsonFile = path + "LoanTestData.json";
        String excelFile = path + "LoanTestData.xlsx";
        
        System.out.println("Generating LoanTestData.xlsx...");
        
        List<Map<String, Object>> loanData = JSONUtils.getDataFromJSON(jsonFile);
        
        try (Workbook workbook = new XSSFWorkbook()) {
            // Create LoanData sheet
            Sheet sheet = workbook.createSheet("LoanData");
            
            // Create header row
            Row headerRow = sheet.createRow(0);
            String[] headers = {
                "testCaseId", "customerId", "loanType", "loanAmount", "loanTerm",
                "interestRate", "purpose", "collateral", "expectedStatus",
                "expectedMonthlyPayment"
            };
            
            CellStyle headerStyle = createHeaderStyle(workbook);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }
            
            // Create data rows
            for (int i = 0; i < loanData.size(); i++) {
                Row row = sheet.createRow(i + 1);
                Map<String, Object> data = loanData.get(i);
                
                row.createCell(0).setCellValue(String.valueOf(data.get("testCaseId")));
                row.createCell(1).setCellValue(String.valueOf(data.get("customerId")));
                row.createCell(2).setCellValue(String.valueOf(data.get("loanType")));
                row.createCell(3).setCellValue(String.valueOf(data.get("loanAmount")));
                row.createCell(4).setCellValue(String.valueOf(data.get("loanTerm")));
                row.createCell(5).setCellValue(String.valueOf(data.get("interestRate")));
                row.createCell(6).setCellValue(String.valueOf(data.get("purpose")));
                row.createCell(7).setCellValue(String.valueOf(data.get("collateral")));
                row.createCell(8).setCellValue(String.valueOf(data.get("expectedStatus")));
                row.createCell(9).setCellValue(String.valueOf(data.get("expectedMonthlyPayment")));
            }
            
            // Auto-size columns
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }
            
            // Write to file
            try (FileOutputStream fos = new FileOutputStream(new File(excelFile))) {
                workbook.write(fos);
            }
            
            System.out.println("✓ LoanTestData.xlsx created successfully");
        }
    }
    
    /**
     * Generate Payment Test Data Excel file
     */
    private static void generatePaymentExcel(String path) throws IOException {
        String jsonFile = path + "PaymentTestData.json";
        String excelFile = path + "PaymentTestData.xlsx";
        
        System.out.println("Generating PaymentTestData.xlsx...");
        
        List<Map<String, Object>> paymentData = JSONUtils.getDataFromJSON(jsonFile);
        
        try (Workbook workbook = new XSSFWorkbook()) {
            // Create PaymentData sheet
            Sheet sheet = workbook.createSheet("PaymentData");
            
            // Create header row
            Row headerRow = sheet.createRow(0);
            String[] headers = {
                "testCaseId", "loanId", "paymentAmount", "paymentDate",
                "paymentMethod", "accountNumber", "expectedStatus", "expectedBalance"
            };
            
            CellStyle headerStyle = createHeaderStyle(workbook);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }
            
            // Create data rows
            for (int i = 0; i < paymentData.size(); i++) {
                Row row = sheet.createRow(i + 1);
                Map<String, Object> data = paymentData.get(i);
                
                row.createCell(0).setCellValue(String.valueOf(data.get("testCaseId")));
                row.createCell(1).setCellValue(String.valueOf(data.get("loanId")));
                row.createCell(2).setCellValue(String.valueOf(data.get("paymentAmount")));
                row.createCell(3).setCellValue(String.valueOf(data.get("paymentDate")));
                row.createCell(4).setCellValue(String.valueOf(data.get("paymentMethod")));
                row.createCell(5).setCellValue(String.valueOf(data.get("accountNumber")));
                row.createCell(6).setCellValue(String.valueOf(data.get("expectedStatus")));
                row.createCell(7).setCellValue(String.valueOf(data.get("expectedBalance")));
            }
            
            // Auto-size columns
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }
            
            // Write to file
            try (FileOutputStream fos = new FileOutputStream(new File(excelFile))) {
                workbook.write(fos);
            }
            
            System.out.println("✓ PaymentTestData.xlsx created successfully");
        }
    }
    
    /**
     * Create InvalidData sheet for negative testing
     */
    private static void createInvalidDataSheet(Workbook workbook) {
        Sheet sheet = workbook.createSheet("InvalidData");
        
        // Create header row
        Row headerRow = sheet.createRow(0);
        String[] headers = {
            "testCaseId", "firstName", "lastName", "email", "phone",
            "ssn", "dateOfBirth", "employmentStatus", "annualIncome",
            "creditScore", "expectedResult"
        };
        
        CellStyle headerStyle = createHeaderStyle(workbook);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
        
        // Add sample invalid data
        Object[][] invalidData = {
            {"CUST_INV_001", "", "Doe", "invalid-email", "123", "invalid-ssn", "invalid-date", "Unknown", "-1000", "1000", "FAIL"},
            {"CUST_INV_002", "Test", "", "test@", "5551234567890", "123456789", "2030-01-01", "", "abc", "xyz", "FAIL"},
            {"CUST_INV_003", "Special@#$", "Char!@#", "no-at-sign", "", "", "", "Retired", "0", "300", "FAIL"}
        };
        
        for (int i = 0; i < invalidData.length; i++) {
            Row row = sheet.createRow(i + 1);
            for (int j = 0; j < invalidData[i].length; j++) {
                row.createCell(j).setCellValue(String.valueOf(invalidData[i][j]));
            }
        }
        
        // Auto-size columns
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }
    }
    
    /**
     * Create header cell style
     */
    private static CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 11);
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.CENTER);
        return style;
    }
}
