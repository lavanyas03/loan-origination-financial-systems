package listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import utils.ScreenshotUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ExtentReportListener implements TestNG ITestListener to generate Extent Reports
 * and capture screenshots on test failures.
 */
public class ExtentReportListener implements ITestListener {
    
    private static final Logger logger = LogManager.getLogger(ExtentReportListener.class);
    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();
    
    /**
     * Initializes Extent Reports at the start of test suite
     */
    @Override
    public void onStart(ITestContext context) {
        logger.info("========== Test Suite Started: {} ==========", context.getName());
        
        String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
        String reportPath = "reports/ExtentReport_" + timestamp + ".html";
        
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
        sparkReporter.config().setDocumentTitle("Automation Test Report");
        sparkReporter.config().setReportName("Loan Origination Financial Systems Test Report");
        sparkReporter.config().setTheme(Theme.STANDARD);
        sparkReporter.config().setTimeStampFormat("yyyy-MM-dd HH:mm:ss");
        
        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);
        
        // Add system information
        extent.setSystemInfo("Application", "Loan Origination Financial Systems");
        extent.setSystemInfo("Environment", "QA");
        extent.setSystemInfo("User", System.getProperty("user.name"));
        extent.setSystemInfo("OS", System.getProperty("os.name"));
        extent.setSystemInfo("Java Version", System.getProperty("java.version"));
        extent.setSystemInfo("Test Suite", context.getName());
        
        logger.info("Extent Report initialized at: {}", reportPath);
    }
    
    /**
     * Creates a test entry when test method starts
     */
    @Override
    public void onTestStart(ITestResult result) {
        logger.info("---------- Test Started: {} ----------", result.getMethod().getMethodName());
        
        ExtentTest test = extent.createTest(result.getMethod().getMethodName());
        
        // Add test description if available
        String description = result.getMethod().getDescription();
        if (description != null && !description.isEmpty()) {
            test.info("Description: " + description);
        }
        
        // Add test class information
        test.info("Test Class: " + result.getTestClass().getName());
        
        extentTest.set(test);
        logger.debug("ExtentTest created for: {}", result.getMethod().getMethodName());
    }
    
    /**
     * Logs test success in the report
     */
    @Override
    public void onTestSuccess(ITestResult result) {
        logger.info("---------- Test Passed: {} ----------", result.getMethod().getMethodName());
        
        ExtentTest test = extentTest.get();
        test.log(Status.PASS, MarkupHelper.createLabel(
            "Test Passed: " + result.getMethod().getMethodName(), ExtentColor.GREEN));
        
        // Log execution time
        long duration = (result.getEndMillis() - result.getStartMillis()) / 1000;
        test.info("Execution Time: " + duration + " seconds");
    }
    
    /**
     * Logs test failure in the report and captures screenshot
     */
    @Override
    public void onTestFailure(ITestResult result) {
        logger.error("---------- Test Failed: {} ----------", result.getMethod().getMethodName());
        
        ExtentTest test = extentTest.get();
        test.log(Status.FAIL, MarkupHelper.createLabel(
            "Test Failed: " + result.getMethod().getMethodName(), ExtentColor.RED));
        
        // Log failure reason
        test.fail("Failure Reason: " + result.getThrowable().getMessage());
        test.fail(result.getThrowable());
        
        // Capture screenshot on failure
        try {
            WebDriver driver = getDriverFromTest(result);
            if (driver != null) {
                String screenshotPath = ScreenshotUtils.captureScreenshot(driver, result.getMethod().getMethodName());
                test.addScreenCaptureFromPath(screenshotPath, "Failure Screenshot");
                logger.info("Screenshot captured for failed test: {}", screenshotPath);
            } else {
                logger.warn("WebDriver not found, screenshot not captured");
                test.warning("Screenshot could not be captured - WebDriver not available");
            }
        } catch (Exception e) {
            logger.error("Failed to capture screenshot", e);
            test.warning("Failed to capture screenshot: " + e.getMessage());
        }
        
        // Log execution time
        long duration = (result.getEndMillis() - result.getStartMillis()) / 1000;
        test.info("Execution Time: " + duration + " seconds");
    }
    
    /**
     * Logs test skip in the report
     */
    @Override
    public void onTestSkipped(ITestResult result) {
        logger.warn("---------- Test Skipped: {} ----------", result.getMethod().getMethodName());
        
        ExtentTest test = extentTest.get();
        test.log(Status.SKIP, MarkupHelper.createLabel(
            "Test Skipped: " + result.getMethod().getMethodName(), ExtentColor.YELLOW));
        
        // Log skip reason if available
        if (result.getThrowable() != null) {
            test.skip("Skip Reason: " + result.getThrowable().getMessage());
        }
    }
    
    /**
     * Logs test failure due to percentage of test passed
     */
    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        logger.warn("---------- Test Failed But Within Success Percentage: {} ----------", 
                   result.getMethod().getMethodName());
        
        ExtentTest test = extentTest.get();
        test.log(Status.WARNING, "Test Failed But Within Success Percentage");
    }
    
    /**
     * Finalizes and writes the report when test suite finishes
     */
    @Override
    public void onFinish(ITestContext context) {
        logger.info("========== Test Suite Finished: {} ==========", context.getName());
        
        if (extent != null) {
            extent.flush();
            logger.info("Extent Report generated successfully");
        }
        
        // Log summary
        logger.info("Total Tests: {}", context.getAllTestMethods().length);
        logger.info("Passed: {}", context.getPassedTests().size());
        logger.info("Failed: {}", context.getFailedTests().size());
        logger.info("Skipped: {}", context.getSkippedTests().size());
    }
    
    /**
     * Extracts WebDriver from test instance
     * 
     * @param result the test result
     * @return WebDriver instance or null
     */
    private WebDriver getDriverFromTest(ITestResult result) {
        try {
            Object testInstance = result.getInstance();
            
            // Try to get driver through reflection
            try {
                java.lang.reflect.Field driverField = testInstance.getClass().getSuperclass()
                    .getDeclaredField("driver");
                driverField.setAccessible(true);
                return (WebDriver) driverField.get(testInstance);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                logger.debug("Could not access driver field via reflection", e);
            }
            
            // Try BrowserFactory
            try {
                Class<?> browserFactoryClass = Class.forName("base.BrowserFactory");
                java.lang.reflect.Method getDriverMethod = browserFactoryClass.getMethod("getDriver");
                return (WebDriver) getDriverMethod.invoke(null);
            } catch (Exception e) {
                logger.debug("Could not get driver from BrowserFactory", e);
            }
            
        } catch (Exception e) {
            logger.error("Error getting WebDriver from test instance", e);
        }
        
        return null;
    }
    
    /**
     * Gets the current ExtentTest instance
     * 
     * @return ExtentTest instance
     */
    public static ExtentTest getTest() {
        return extentTest.get();
    }
}
