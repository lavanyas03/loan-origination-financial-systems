package utils;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ScreenshotUtils provides utilities to capture screenshots on test failures
 * and integrate with Extent Reports.
 */
public class ScreenshotUtils {
    
    private static final Logger logger = LogManager.getLogger(ScreenshotUtils.class);
    private static final String SCREENSHOT_DIR = "screenshots";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS");
    
    /**
     * Private constructor to prevent instantiation
     */
    private ScreenshotUtils() {
        throw new IllegalStateException("ScreenshotUtils is a utility class");
    }
    
    /**
     * Creates screenshot directory if it doesn't exist
     * 
     * @return Path to screenshot directory
     */
    private static Path createScreenshotDirectory() {
        try {
            Path screenshotPath = Paths.get(SCREENSHOT_DIR);
            if (!Files.exists(screenshotPath)) {
                Files.createDirectories(screenshotPath);
                logger.info("Created screenshot directory: {}", screenshotPath.toAbsolutePath());
            }
            return screenshotPath;
        } catch (IOException e) {
            logger.error("Failed to create screenshot directory", e);
            throw new RuntimeException("Failed to create screenshot directory", e);
        }
    }
    
    /**
     * Captures screenshot and saves to file
     * 
     * @param driver WebDriver instance
     * @param screenshotName name for the screenshot file
     * @return absolute path to saved screenshot
     */
    public static String captureScreenshot(WebDriver driver, String screenshotName) {
        if (driver == null) {
            logger.error("WebDriver is null, cannot capture screenshot");
            return null;
        }
        
        try {
            // Create screenshot directory
            Path screenshotDir = createScreenshotDirectory();
            
            // Generate unique filename with timestamp
            String timestamp = dateFormat.format(new Date());
            String fileName = screenshotName + "_" + timestamp + ".png";
            Path screenshotPath = screenshotDir.resolve(fileName);
            
            // Capture screenshot
            TakesScreenshot takesScreenshot = (TakesScreenshot) driver;
            File sourceFile = takesScreenshot.getScreenshotAs(OutputType.FILE);
            
            // Copy to destination
            Files.copy(sourceFile.toPath(), screenshotPath, StandardCopyOption.REPLACE_EXISTING);
            
            String absolutePath = screenshotPath.toAbsolutePath().toString();
            logger.info("Screenshot captured successfully: {}", absolutePath);
            return absolutePath;
            
        } catch (Exception e) {
            logger.error("Failed to capture screenshot: {}", screenshotName, e);
            return null;
        }
    }
    
    /**
     * Captures screenshot and returns as base64 string
     * 
     * @param driver WebDriver instance
     * @return base64 encoded screenshot string
     */
    public static String captureScreenshotAsBase64(WebDriver driver) {
        if (driver == null) {
            logger.error("WebDriver is null, cannot capture screenshot");
            return null;
        }
        
        try {
            TakesScreenshot takesScreenshot = (TakesScreenshot) driver;
            String base64Screenshot = takesScreenshot.getScreenshotAs(OutputType.BASE64);
            logger.debug("Screenshot captured as base64");
            return base64Screenshot;
        } catch (Exception e) {
            logger.error("Failed to capture screenshot as base64", e);
            return null;
        }
    }
    
    /**
     * Captures screenshot and attaches to Extent Report test
     * 
     * @param driver WebDriver instance
     * @param test ExtentTest instance
     * @param screenshotName name for the screenshot
     * @return absolute path to saved screenshot
     */
    public static String captureAndAttachScreenshot(WebDriver driver, ExtentTest test, String screenshotName) {
        if (driver == null) {
            logger.error("WebDriver is null, cannot capture screenshot");
            return null;
        }
        
        if (test == null) {
            logger.warn("ExtentTest is null, screenshot will be saved but not attached to report");
            return captureScreenshot(driver, screenshotName);
        }
        
        try {
            // Capture screenshot as base64 for Extent Report
            String base64Screenshot = captureScreenshotAsBase64(driver);
            
            if (base64Screenshot != null) {
                // Attach to Extent Report
                test.fail("Screenshot attached",
                        MediaEntityBuilder.createScreenCaptureFromBase64String(base64Screenshot).build());
                logger.info("Screenshot attached to Extent Report: {}", screenshotName);
            }
            
            // Also save screenshot to file for backup
            String screenshotPath = captureScreenshot(driver, screenshotName);
            
            return screenshotPath;
            
        } catch (Exception e) {
            logger.error("Failed to capture and attach screenshot: {}", screenshotName, e);
            return null;
        }
    }
    
    /**
     * Captures screenshot on test failure and attaches to Extent Report
     * 
     * @param driver WebDriver instance
     * @param test ExtentTest instance
     * @param testName name of the failed test
     * @param throwable the exception that caused the failure
     * @return absolute path to saved screenshot
     */
    public static String captureScreenshotOnFailure(WebDriver driver, ExtentTest test, String testName, Throwable throwable) {
        if (driver == null) {
            logger.error("WebDriver is null, cannot capture screenshot for failure");
            return null;
        }
        
        String screenshotName = testName + "_FAILED";
        logger.info("Capturing screenshot for test failure: {}", testName);
        
        try {
            // Capture screenshot as base64
            String base64Screenshot = captureScreenshotAsBase64(driver);
            
            if (test != null && base64Screenshot != null) {
                // Log failure with screenshot in Extent Report
                test.fail(throwable, MediaEntityBuilder.createScreenCaptureFromBase64String(base64Screenshot).build());
                logger.info("Failure screenshot attached to Extent Report: {}", testName);
            } else if (test != null) {
                // Log failure without screenshot
                test.fail(throwable);
            }
            
            // Save screenshot to file
            return captureScreenshot(driver, screenshotName);
            
        } catch (Exception e) {
            logger.error("Failed to capture screenshot on failure: {}", testName, e);
            return null;
        }
    }
    
    /**
     * Captures screenshot for passed test (optional)
     * 
     * @param driver WebDriver instance
     * @param test ExtentTest instance
     * @param testName name of the passed test
     * @return absolute path to saved screenshot
     */
    public static String captureScreenshotOnPass(WebDriver driver, ExtentTest test, String testName) {
        if (driver == null) {
            logger.error("WebDriver is null, cannot capture screenshot");
            return null;
        }
        
        String screenshotName = testName + "_PASSED";
        logger.info("Capturing screenshot for passed test: {}", testName);
        
        try {
            // Capture screenshot as base64
            String base64Screenshot = captureScreenshotAsBase64(driver);
            
            if (test != null && base64Screenshot != null) {
                // Attach screenshot to pass log
                test.pass("Test passed - Screenshot attached",
                        MediaEntityBuilder.createScreenCaptureFromBase64String(base64Screenshot).build());
                logger.info("Pass screenshot attached to Extent Report: {}", testName);
            }
            
            // Save screenshot to file
            return captureScreenshot(driver, screenshotName);
            
        } catch (Exception e) {
            logger.error("Failed to capture screenshot on pass: {}", testName, e);
            return null;
        }
    }
    
    /**
     * Attaches existing screenshot file to Extent Report
     * 
     * @param test ExtentTest instance
     * @param screenshotPath path to screenshot file
     * @param message message to log with screenshot
     */
    public static void attachScreenshotToReport(ExtentTest test, String screenshotPath, String message) {
        if (test == null) {
            logger.warn("ExtentTest is null, cannot attach screenshot to report");
            return;
        }
        
        if (screenshotPath == null || screenshotPath.isEmpty()) {
            logger.warn("Screenshot path is null or empty");
            return;
        }
        
        try {
            test.info(message, MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
            logger.debug("Screenshot attached to Extent Report from path: {}", screenshotPath);
        } catch (Exception e) {
            logger.error("Failed to attach screenshot to report: {}", screenshotPath, e);
        }
    }
    
    /**
     * Gets screenshot directory path
     * 
     * @return absolute path to screenshot directory
     */
    public static String getScreenshotDirectory() {
        return Paths.get(SCREENSHOT_DIR).toAbsolutePath().toString();
    }
    
    /**
     * Cleans up old screenshots (optional utility method)
     * Deletes screenshots older than specified days
     * 
     * @param daysToKeep number of days to keep screenshots
     * @return number of files deleted
     */
    public static int cleanupOldScreenshots(int daysToKeep) {
        try {
            Path screenshotDir = Paths.get(SCREENSHOT_DIR);
            if (!Files.exists(screenshotDir)) {
                logger.info("Screenshot directory does not exist, nothing to clean");
                return 0;
            }
            
            long cutoffTime = System.currentTimeMillis() - (daysToKeep * 24L * 60 * 60 * 1000);
            final int[] deletedCount = {0};
            
            Files.walk(screenshotDir)
                    .filter(Files::isRegularFile)
                    .filter(path -> {
                        try {
                            return Files.getLastModifiedTime(path).toMillis() < cutoffTime;
                        } catch (IOException e) {
                            return false;
                        }
                    })
                    .forEach(path -> {
                        try {
                            Files.delete(path);
                            deletedCount[0]++;
                        } catch (IOException e) {
                            logger.error("Failed to delete old screenshot: {}", path, e);
                        }
                    });
            
            logger.info("Cleaned up {} old screenshots older than {} days", deletedCount[0], daysToKeep);
            return deletedCount[0];
            
        } catch (Exception e) {
            logger.error("Failed to cleanup old screenshots", e);
            return 0;
        }
    }
}
