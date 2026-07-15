@echo off
REM Script to generate Excel test data files

echo ========================================
echo Excel Test Data Generator
echo ========================================
echo.

REM Check if Maven is installed
where mvn >nul 2>nul
if %errorlevel% equ 0 (
    echo Maven found. Generating Excel files...
    echo.
    mvn compile exec:java -Dexec.mainClass="utils.ExcelDataGenerator"
) else (
    echo Maven not found in PATH. Please install Maven or use your IDE to run ExcelDataGenerator.java
    echo.
    echo Alternative: Run the ExcelDataGenerator class from your IDE:
    echo 1. Open src/main/java/utils/ExcelDataGenerator.java
    echo 2. Right-click and select "Run 'ExcelDataGenerator.main()'"
    echo.
    pause
)

echo.
echo ========================================
echo Excel files should now be generated in:
echo src/test/resources/testdata/
echo ========================================
echo.
pause
