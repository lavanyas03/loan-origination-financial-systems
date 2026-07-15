package utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Utility class for reading and writing JSON test data using Jackson
 * Supports JSON files with arrays and objects
 */
public class JSONUtils {
    
    private static final Logger logger = LogManager.getLogger(JSONUtils.class);
    private static final ObjectMapper objectMapper = new ObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT);
    
    /**
     * Private constructor to prevent instantiation
     */
    private JSONUtils() {
        throw new IllegalStateException("Utility class");
    }
    
    /**
     * Reads JSON file and returns as a list of maps
     * Expects JSON array of objects at root level
     * 
     * @param filePath Path to the JSON file
     * @return List of maps where each map represents a JSON object
     * @throws RuntimeException if file reading fails
     */
    public static List<Map<String, Object>> getDataFromJSON(String filePath) {
        logger.info("Reading data from JSON file: {}", filePath);
        
        try {
            File jsonFile = new File(filePath);
            
            if (!jsonFile.exists()) {
                logger.error("JSON file does not exist: {}", filePath);
                throw new IllegalArgumentException("JSON file not found: " + filePath);
            }
            
            JsonNode rootNode = objectMapper.readTree(jsonFile);
            
            // Handle array of objects
            if (rootNode.isArray()) {
                List<Map<String, Object>> dataList = objectMapper.convertValue(
                        rootNode,
                        new TypeReference<List<Map<String, Object>>>() {}
                );
                logger.info("Successfully read {} records from JSON file", dataList.size());
                return dataList;
            }
            
            // Handle single object - wrap in a list
            if (rootNode.isObject()) {
                Map<String, Object> singleObject = objectMapper.convertValue(
                        rootNode,
                        new TypeReference<Map<String, Object>>() {}
                );
                logger.info("Read single object from JSON file, returning as list");
                return Collections.singletonList(singleObject);
            }
            
            logger.error("Invalid JSON structure in file: {}", filePath);
            throw new IllegalArgumentException("JSON file must contain an array or object at root level");
            
        } catch (IOException e) {
            logger.error("Error reading JSON file: {}", filePath, e);
            throw new RuntimeException("Failed to read JSON file: " + filePath, e);
        }
    }
    
    /**
     * Reads JSON file and returns as a 2D Object array
     * Suitable for TestNG DataProvider
     * 
     * @param filePath Path to the JSON file
     * @return 2D Object array where each row is a test data set
     */
    public static Object[][] getDataAsObjectArray(String filePath) {
        logger.info("Reading data as Object array from JSON file: {}", filePath);
        
        List<Map<String, Object>> dataList = getDataFromJSON(filePath);
        
        if (dataList.isEmpty()) {
            logger.warn("No data found in JSON file: {}. Returning empty array.", filePath);
            return new Object[0][0];
        }
        
        Object[][] data = new Object[dataList.size()][1];
        
        for (int i = 0; i < dataList.size(); i++) {
            data[i][0] = dataList.get(i);
        }
        
        logger.info("Converted {} records to Object array", data.length);
        return data;
    }
    
    /**
     * Reads specific record from JSON file by index
     * 
     * @param filePath Path to the JSON file
     * @param index Index of the record to read (0-based)
     * @return Map representing the JSON object
     */
    public static Map<String, Object> getRecordByIndex(String filePath, int index) {
        logger.info("Reading record at index {} from JSON file: {}", index, filePath);
        
        List<Map<String, Object>> dataList = getDataFromJSON(filePath);
        
        if (index < 0 || index >= dataList.size()) {
            logger.error("Invalid index: {}. Valid range: 0-{}", index, dataList.size() - 1);
            throw new IndexOutOfBoundsException("Invalid index: " + index);
        }
        
        return dataList.get(index);
    }
    
    /**
     * Reads JSON file and returns as a specific type
     * 
     * @param filePath Path to the JSON file
     * @param typeReference TypeReference for the desired type
     * @param <T> Type to deserialize to
     * @return Deserialized object of type T
     */
    public static <T> T getDataAsType(String filePath, TypeReference<T> typeReference) {
        logger.info("Reading data from JSON file as specific type: {}", filePath);
        
        try {
            File jsonFile = new File(filePath);
            
            if (!jsonFile.exists()) {
                logger.error("JSON file does not exist: {}", filePath);
                throw new IllegalArgumentException("JSON file not found: " + filePath);
            }
            
            T data = objectMapper.readValue(jsonFile, typeReference);
            logger.info("Successfully deserialized JSON file to type: {}", typeReference.getType());
            return data;
            
        } catch (IOException e) {
            logger.error("Error reading JSON file: {}", filePath, e);
            throw new RuntimeException("Failed to read JSON file: " + filePath, e);
        }
    }
    
    /**
     * Reads a specific value from JSON file using JSON path
     * 
     * @param filePath Path to the JSON file
     * @param key Key/path to the value (supports nested keys with dot notation)
     * @return Value as Object, or null if not found
     */
    public static Object getValue(String filePath, String key) {
        logger.info("Reading value for key '{}' from JSON file: {}", key, filePath);
        
        try {
            File jsonFile = new File(filePath);
            JsonNode rootNode = objectMapper.readTree(jsonFile);
            
            // Handle dot notation for nested keys
            String[] keyParts = key.split("\\.");
            JsonNode currentNode = rootNode;
            
            for (String keyPart : keyParts) {
                if (currentNode.isArray()) {
                    try {
                        int index = Integer.parseInt(keyPart);
                        currentNode = currentNode.get(index);
                    } catch (NumberFormatException e) {
                        logger.error("Invalid array index: {}", keyPart);
                        return null;
                    }
                } else {
                    currentNode = currentNode.get(keyPart);
                }
                
                if (currentNode == null) {
                    logger.warn("Key '{}' not found in JSON file", key);
                    return null;
                }
            }
            
            return convertJsonNodeToObject(currentNode);
            
        } catch (IOException e) {
            logger.error("Error reading JSON file: {}", filePath, e);
            throw new RuntimeException("Failed to read JSON file: " + filePath, e);
        }
    }
    
    /**
     * Gets the total number of records in JSON array
     * 
     * @param filePath Path to the JSON file
     * @return Number of records
     */
    public static int getRecordCount(String filePath) {
        logger.info("Getting record count from JSON file: {}", filePath);
        
        try {
            File jsonFile = new File(filePath);
            JsonNode rootNode = objectMapper.readTree(jsonFile);
            
            if (rootNode.isArray()) {
                int count = rootNode.size();
                logger.info("Record count in JSON file: {}", count);
                return count;
            }
            
            if (rootNode.isObject()) {
                logger.info("JSON file contains a single object");
                return 1;
            }
            
            logger.warn("JSON file does not contain array or object");
            return 0;
            
        } catch (IOException e) {
            logger.error("Error reading JSON file: {}", filePath, e);
            return 0;
        }
    }
    
    /**
     * Validates if JSON file exists and is valid
     * 
     * @param filePath Path to the JSON file
     * @return true if file is valid JSON, false otherwise
     */
    public static boolean validateJSONFile(String filePath) {
        File file = new File(filePath);
        
        if (!file.exists()) {
            logger.error("JSON file does not exist: {}", filePath);
            return false;
        }
        
        if (!file.canRead()) {
            logger.error("JSON file is not readable: {}", filePath);
            return false;
        }
        
        if (!filePath.toLowerCase().endsWith(".json")) {
            logger.error("Invalid JSON file format: {}", filePath);
            return false;
        }
        
        try {
            objectMapper.readTree(file);
            logger.info("JSON file validated successfully: {}", filePath);
            return true;
        } catch (IOException e) {
            logger.error("Invalid JSON syntax in file: {}", filePath, e);
            return false;
        }
    }
    
    /**
     * Writes data to JSON file
     * 
     * @param filePath Path to the JSON file
     * @param data Data to write
     */
    public static void writeToJSON(String filePath, Object data) {
        logger.info("Writing data to JSON file: {}", filePath);
        
        try {
            File jsonFile = new File(filePath);
            
            // Create parent directories if they don't exist
            if (jsonFile.getParentFile() != null && !jsonFile.getParentFile().exists()) {
                boolean created = jsonFile.getParentFile().mkdirs();
                if (created) {
                    logger.debug("Created parent directories for: {}", filePath);
                }
            }
            
            objectMapper.writeValue(jsonFile, data);
            logger.info("Successfully wrote data to JSON file: {}", filePath);
            
        } catch (IOException e) {
            logger.error("Error writing to JSON file: {}", filePath, e);
            throw new RuntimeException("Failed to write to JSON file: " + filePath, e);
        }
    }
    
    /**
     * Converts JsonNode to appropriate Java object
     * 
     * @param node JsonNode to convert
     * @return Converted object
     */
    private static Object convertJsonNodeToObject(JsonNode node) {
        if (node.isNull()) {
            return null;
        } else if (node.isBoolean()) {
            return node.asBoolean();
        } else if (node.isInt()) {
            return node.asInt();
        } else if (node.isLong()) {
            return node.asLong();
        } else if (node.isDouble()) {
            return node.asDouble();
        } else if (node.isTextual()) {
            return node.asText();
        } else if (node.isArray()) {
            List<Object> list = new ArrayList<>();
            node.forEach(element -> list.add(convertJsonNodeToObject(element)));
            return list;
        } else if (node.isObject()) {
            Map<String, Object> map = new HashMap<>();
            node.fields().forEachRemaining(entry -> 
                map.put(entry.getKey(), convertJsonNodeToObject(entry.getValue()))
            );
            return map;
        }
        return node.toString();
    }
}
