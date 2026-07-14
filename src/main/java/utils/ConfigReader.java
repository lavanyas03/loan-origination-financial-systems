package utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * ConfigReader is a utility class to load and read configuration properties.
 * Implements singleton pattern to ensure single instance of configuration.
 */
public class ConfigReader {
    
    private static final Logger logger = LogManager.getLogger(ConfigReader.class);
    private static ConfigReader instance;
    private Properties properties;
    private static final String CONFIG_FILE_PATH = "src/main/resources/config.properties";
    
    /**
     * Private constructor to prevent direct instantiation
     */
    private ConfigReader() {
        properties = new Properties();
        loadProperties();
    }
    
    /**
     * Gets the singleton instance of ConfigReader
     * 
     * @return ConfigReader instance
     */
    public static synchronized ConfigReader getInstance() {
        if (instance == null) {
            instance = new ConfigReader();
        }
        return instance;
    }
    
    /**
     * Loads properties from the config.properties file
     */
    private void loadProperties() {
        try (InputStream inputStream = new FileInputStream(CONFIG_FILE_PATH)) {
            properties.load(inputStream);
            logger.info("Configuration properties loaded successfully from: {}", CONFIG_FILE_PATH);
        } catch (IOException e) {
            logger.error("Failed to load configuration properties from: {}", CONFIG_FILE_PATH, e);
            throw new RuntimeException("Failed to load config.properties: " + e.getMessage(), e);
        }
    }
    
    /**
     * Gets a property value as String
     * 
     * @param key the property key
     * @return the property value, or null if not found
     */
    public String getProperty(String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            logger.warn("Property '{}' not found in configuration", key);
        }
        return value;
    }
    
    /**
     * Gets a property value with a default value
     * 
     * @param key the property key
     * @param defaultValue the default value to return if property not found
     * @return the property value, or defaultValue if not found
     */
    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
    
    /**
     * Gets a property value as integer
     * 
     * @param key the property key
     * @return the property value as integer
     * @throws NumberFormatException if value cannot be parsed as integer
     */
    public int getIntProperty(String key) {
        String value = getProperty(key);
        if (value == null) {
            throw new IllegalArgumentException("Property '" + key + "' not found in configuration");
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            logger.error("Failed to parse property '{}' with value '{}' as integer", key, value);
            throw new NumberFormatException("Invalid integer value for property '" + key + "': " + value);
        }
    }
    
    /**
     * Gets a property value as integer with default value
     * 
     * @param key the property key
     * @param defaultValue the default value to return if property not found or invalid
     * @return the property value as integer, or defaultValue if not found or invalid
     */
    public int getIntProperty(String key, int defaultValue) {
        try {
            String value = getProperty(key);
            return value != null ? Integer.parseInt(value.trim()) : defaultValue;
        } catch (NumberFormatException e) {
            logger.warn("Failed to parse property '{}' as integer, using default: {}", key, defaultValue);
            return defaultValue;
        }
    }
    
    /**
     * Gets a property value as boolean
     * 
     * @param key the property key
     * @return the property value as boolean
     */
    public boolean getBooleanProperty(String key) {
        String value = getProperty(key);
        if (value == null) {
            throw new IllegalArgumentException("Property '" + key + "' not found in configuration");
        }
        return Boolean.parseBoolean(value.trim());
    }
    
    /**
     * Gets a property value as boolean with default value
     * 
     * @param key the property key
     * @param defaultValue the default value to return if property not found
     * @return the property value as boolean, or defaultValue if not found
     */
    public boolean getBooleanProperty(String key, boolean defaultValue) {
        String value = getProperty(key);
        return value != null ? Boolean.parseBoolean(value.trim()) : defaultValue;
    }
    
    /**
     * Gets a property value as long
     * 
     * @param key the property key
     * @return the property value as long
     * @throws NumberFormatException if value cannot be parsed as long
     */
    public long getLongProperty(String key) {
        String value = getProperty(key);
        if (value == null) {
            throw new IllegalArgumentException("Property '" + key + "' not found in configuration");
        }
        try {
            return Long.parseLong(value.trim());
        } catch (NumberFormatException e) {
            logger.error("Failed to parse property '{}' with value '{}' as long", key, value);
            throw new NumberFormatException("Invalid long value for property '" + key + "': " + value);
        }
    }
    
    /**
     * Gets a property value as long with default value
     * 
     * @param key the property key
     * @param defaultValue the default value to return if property not found or invalid
     * @return the property value as long, or defaultValue if not found or invalid
     */
    public long getLongProperty(String key, long defaultValue) {
        try {
            String value = getProperty(key);
            return value != null ? Long.parseLong(value.trim()) : defaultValue;
        } catch (NumberFormatException e) {
            logger.warn("Failed to parse property '{}' as long, using default: {}", key, defaultValue);
            return defaultValue;
        }
    }
    
    /**
     * Checks if a property key exists in configuration
     * 
     * @param key the property key
     * @return true if property exists, false otherwise
     */
    public boolean hasProperty(String key) {
        return properties.containsKey(key);
    }
}
