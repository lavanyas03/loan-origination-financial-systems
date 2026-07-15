package utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility class for reading test data from Excel files using Apache POI
 * Supports .xlsx format
 */
public class ExcelUtils {
    
    private static final Logger logger = LogManager.getLogger(ExcelUtils.class);
    
    /**
     * Private constructor to prevent instantiation
     */
    private ExcelUtils() {
        throw new IllegalStateException("Utility class");
    }
    
    /**
     * Reads all rows from an Excel sheet and returns as a list of maps
     * First row is considered as header
     * 
     * @param filePath Path to the Excel file
     * @param sheetName Name of the sheet to read
     * @return List of maps where each map represents a row with column name as key
     * @throws RuntimeException if file reading fails
     */
    public static List<Map<String, String>> getDataFromSheet(String filePath, String sheetName) {
        logger.info("Reading data from Excel file: {} | Sheet: {}", filePath, sheetName);
        
        List<Map<String, String>> dataList = new ArrayList<>();
        
        try (FileInputStream fis = new FileInputStream(new File(filePath));
             Workbook workbook = new XSSFWorkbook(fis)) {
            
            Sheet sheet = workbook.getSheet(sheetName);
            
            if (sheet == null) {
                logger.error("Sheet '{}' not found in file: {}", sheetName, filePath);
                throw new IllegalArgumentException("Sheet '" + sheetName + "' not found in Excel file");
            }
            
            // Get header row
            Row headerRow = sheet.getRow(0);
            if (headerRow == null) {
                logger.error("Header row is empty in sheet: {}", sheetName);
                throw new IllegalArgumentException("Header row is empty in sheet: " + sheetName);
            }
            
            List<String> headers = new ArrayList<>();
            for (Cell cell : headerRow) {
                headers.add(getCellValueAsString(cell));
            }
            
            logger.debug("Headers found: {}", headers);
            
            // Read data rows
            int lastRowNum = sheet.getLastRowNum();
            for (int i = 1; i <= lastRowNum; i++) {
                Row dataRow = sheet.getRow(i);
                
                if (dataRow == null || isRowEmpty(dataRow)) {
                    logger.debug("Skipping empty row at index: {}", i);
                    continue;
                }
                
                Map<String, String> rowData = new HashMap<>();
                for (int j = 0; j < headers.size(); j++) {
                    Cell cell = dataRow.getCell(j);
                    String cellValue = (cell != null) ? getCellValueAsString(cell) : "";
                    rowData.put(headers.get(j), cellValue);
                }
                
                dataList.add(rowData);
            }
            
            logger.info("Successfully read {} rows from sheet: {}", dataList.size(), sheetName);
            
        } catch (IOException e) {
            logger.error("Error reading Excel file: {}", filePath, e);
            throw new RuntimeException("Failed to read Excel file: " + filePath, e);
        }
        
        return dataList;
    }
    
    /**
     * Reads all rows from an Excel sheet and returns as a 2D Object array
     * Suitable for TestNG DataProvider
     * 
     * @param filePath Path to the Excel file
     * @param sheetName Name of the sheet to read
     * @return 2D Object array where each row is a test data set
     */
    public static Object[][] getDataAsObjectArray(String filePath, String sheetName) {
        logger.info("Reading data as Object array from Excel file: {} | Sheet: {}", filePath, sheetName);
        
        List<Map<String, String>> dataList = getDataFromSheet(filePath, sheetName);
        
        if (dataList.isEmpty()) {
            logger.warn("No data found in sheet: {}. Returning empty array.", sheetName);
            return new Object[0][0];
        }
        
        Object[][] data = new Object[dataList.size()][1];
        
        for (int i = 0; i < dataList.size(); i++) {
            data[i][0] = dataList.get(i);
        }
        
        logger.info("Converted {} rows to Object array", data.length);
        return data;
    }
    
    /**
     * Reads specific row from an Excel sheet
     * 
     * @param filePath Path to the Excel file
     * @param sheetName Name of the sheet to read
     * @param rowNum Row number to read (1-based index, excluding header)
     * @return Map representing the row data
     */
    public static Map<String, String> getRowData(String filePath, String sheetName, int rowNum) {
        logger.info("Reading row {} from sheet: {}", rowNum, sheetName);
        
        List<Map<String, String>> dataList = getDataFromSheet(filePath, sheetName);
        
        if (rowNum < 1 || rowNum > dataList.size()) {
            logger.error("Invalid row number: {}. Valid range: 1-{}", rowNum, dataList.size());
            throw new IndexOutOfBoundsException("Invalid row number: " + rowNum);
        }
        
        return dataList.get(rowNum - 1);
    }
    
    /**
     * Gets the total number of data rows in a sheet (excluding header)
     * 
     * @param filePath Path to the Excel file
     * @param sheetName Name of the sheet
     * @return Number of data rows
     */
    public static int getRowCount(String filePath, String sheetName) {
        logger.info("Getting row count from sheet: {}", sheetName);
        
        try (FileInputStream fis = new FileInputStream(new File(filePath));
             Workbook workbook = new XSSFWorkbook(fis)) {
            
            Sheet sheet = workbook.getSheet(sheetName);
            
            if (sheet == null) {
                logger.error("Sheet '{}' not found in file: {}", sheetName, filePath);
                return 0;
            }
            
            int count = 0;
            int lastRowNum = sheet.getLastRowNum();
            
            // Skip header row (index 0), count non-empty data rows
            for (int i = 1; i <= lastRowNum; i++) {
                Row row = sheet.getRow(i);
                if (row != null && !isRowEmpty(row)) {
                    count++;
                }
            }
            
            logger.info("Row count in sheet '{}': {}", sheetName, count);
            return count;
            
        } catch (IOException e) {
            logger.error("Error reading Excel file: {}", filePath, e);
            return 0;
        }
    }
    
    /**
     * Converts cell value to String based on cell type
     * 
     * @param cell Cell to read
     * @return String representation of cell value
     */
    private static String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }
        
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue().trim();
            case NUMERIC -> {
                if (DateUtil.isCellDateFormatted(cell)) {
                    yield cell.getDateCellValue().toString();
                } else {
                    // Format numeric value without scientific notation
                    double numValue = cell.getNumericCellValue();
                    if (numValue == (long) numValue) {
                        yield String.valueOf((long) numValue);
                    } else {
                        yield String.valueOf(numValue);
                    }
                }
            }
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            case FORMULA -> {
                try {
                    yield cell.getStringCellValue().trim();
                } catch (IllegalStateException e) {
                    // If formula result is numeric
                    yield String.valueOf(cell.getNumericCellValue());
                }
            }
            case BLANK -> "";
            default -> "";
        };
    }
    
    /**
     * Checks if a row is empty
     * 
     * @param row Row to check
     * @return true if row is empty, false otherwise
     */
    private static boolean isRowEmpty(Row row) {
        for (int cellNum = row.getFirstCellNum(); cellNum < row.getLastCellNum(); cellNum++) {
            Cell cell = row.getCell(cellNum);
            if (cell != null && cell.getCellType() != CellType.BLANK && 
                !getCellValueAsString(cell).isEmpty()) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Validates if Excel file exists and is accessible
     * 
     * @param filePath Path to the Excel file
     * @return true if file is valid, false otherwise
     */
    public static boolean validateExcelFile(String filePath) {
        File file = new File(filePath);
        
        if (!file.exists()) {
            logger.error("Excel file does not exist: {}", filePath);
            return false;
        }
        
        if (!file.canRead()) {
            logger.error("Excel file is not readable: {}", filePath);
            return false;
        }
        
        if (!filePath.toLowerCase().endsWith(".xlsx") && !filePath.toLowerCase().endsWith(".xls")) {
            logger.error("Invalid Excel file format: {}", filePath);
            return false;
        }
        
        logger.info("Excel file validated successfully: {}", filePath);
        return true;
    }
}
