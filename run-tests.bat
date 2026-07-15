@echo off
REM Script to run tests with Maven

echo ========================================
echo Running TestNG Tests
echo ========================================
echo.

REM Check if Maven is installed
where mvn >nul 2>nul
if %errorlevel% equ 0 (
    echo Maven found. Running tests...
    echo.
    
    REM Check if argument is provided
    if "%1"=="" (
        echo Running all tests from testng.xml...
        mvn clean test
    ) else if "%1"=="customer" (
        echo Running Customer tests...
        mvn test -Dtest=CustomerDataDrivenTest
    ) else if "%1"=="loan" (
        echo Running Loan tests...
        mvn test -Dtest=LoanDataDrivenTest
    ) else if "%1"=="payment" (
        echo Running Payment tests...
        mvn test -Dtest=PaymentDataDrivenTest
    ) else if "%1"=="sample" (
        echo Running Sample tests...
        mvn test -Dtest=SampleTest
    ) else (
        echo Running all tests from testng.xml...
        mvn clean test
    )
) else (
    echo Maven not found in PATH. Please install Maven or use your IDE to run tests.
    echo.
    echo Alternative: Run tests from your IDE:
    echo 1. Right-click on testng.xml
    echo 2. Select "Run 'testng.xml'"
    echo.
    echo Or install Maven from: https://maven.apache.org/download.cgi
    echo.
    pause
)

echo.
echo ========================================
echo Test execution complete!
echo Check reports in: target/surefire-reports/
echo ========================================
echo.
pause
