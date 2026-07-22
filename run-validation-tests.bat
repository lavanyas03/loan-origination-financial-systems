@echo off
REM ========================================================================
REM Customer Registration Validation Tests - Windows Batch Runner
REM ========================================================================
REM This script provides easy options to run validation tests
REM Author: Test Automation Team
REM Date: 2026-07-22
REM ========================================================================

echo.
echo ========================================================================
echo Customer Registration Validation Tests
echo ========================================================================
echo.
echo Select an option to run tests:
echo.
echo [1] Run All Validation Tests (29 scenarios) - RECOMMENDED
echo [2] Run Data-Driven Validation Test Only (27 scenarios)
echo [3] Run All Fields Empty Test Only
echo [4] Run Error Persistence Test Only
echo [5] Run by Group: Validation Tests
echo [6] Run by Group: Negative Tests
echo [7] Run Full Test Suite (includes validation tests)
echo [8] Exit
echo.

set /p choice="Enter your choice (1-8): "

if "%choice%"=="1" goto run_all_validation
if "%choice%"=="2" goto run_data_driven
if "%choice%"=="3" goto run_empty_fields
if "%choice%"=="4" goto run_error_persistence
if "%choice%"=="5" goto run_validation_group
if "%choice%"=="6" goto run_negative_group
if "%choice%"=="7" goto run_full_suite
if "%choice%"=="8" goto exit

echo Invalid choice. Please run the script again.
goto end

:run_all_validation
echo.
echo ========================================================================
echo Running All Validation Tests (29 scenarios)...
echo ========================================================================
echo.
call mvn clean test -DsuiteXmlFile=testng-validation.xml
goto show_reports

:run_data_driven
echo.
echo ========================================================================
echo Running Data-Driven Validation Test (27 scenarios)...
echo ========================================================================
echo.
call mvn clean test -Dtest=CustomerRegistrationValidationTest#testCustomerRegistrationFieldValidation
goto show_reports

:run_empty_fields
echo.
echo ========================================================================
echo Running All Fields Empty Test...
echo ========================================================================
echo.
call mvn clean test -Dtest=CustomerRegistrationValidationTest#testAllMandatoryFieldsEmpty
goto show_reports

:run_error_persistence
echo.
echo ========================================================================
echo Running Error Persistence Test...
echo ========================================================================
echo.
call mvn clean test -Dtest=CustomerRegistrationValidationTest#testErrorMessagePersistence
goto show_reports

:run_validation_group
echo.
echo ========================================================================
echo Running Validation Group Tests...
echo ========================================================================
echo.
call mvn clean test -Dgroups=validation
goto show_reports

:run_negative_group
echo.
echo ========================================================================
echo Running Negative Group Tests...
echo ========================================================================
echo.
call mvn clean test -Dgroups=negative
goto show_reports

:run_full_suite
echo.
echo ========================================================================
echo Running Full Test Suite (includes validation tests)...
echo ========================================================================
echo.
call mvn clean test -DsuiteXmlFile=testng.xml
goto show_reports

:show_reports
echo.
echo ========================================================================
echo Test Execution Completed!
echo ========================================================================
echo.
echo Reports and Logs Location:
echo - Extent Report: reports\extent-report.html
echo - TestNG Report: target\surefire-reports\index.html
echo - Logs: logs\
echo - Screenshots (on failure): screenshots\
echo.
echo Opening Extent Report...
echo.

REM Try to open the Extent Report if it exists
if exist "reports\extent-report.html" (
    start reports\extent-report.html
) else if exist "target\extent-report.html" (
    start target\extent-report.html
) else (
    echo Extent Report not found. Check the reports directory.
)

echo.
echo Press any key to view TestNG Report or close...
pause >nul

if exist "target\surefire-reports\index.html" (
    start target\surefire-reports\index.html
)

goto end

:exit
echo.
echo Exiting...
goto end

:end
echo.
echo Thank you for using Customer Registration Validation Tests!
echo.
pause
