@echo off
REM ========================================================================
REM Customer Registration Validation Tests - Windows Batch Runner
REM ========================================================================
REM This script provides easy options to run validation tests
REM Author: Test Automation Team
REM Date: 2026-07-23 (Updated with Duplicate & Boundary Tests)
REM ========================================================================

echo.
echo ========================================================================
echo Customer Registration Validation Tests
echo ========================================================================
echo.
echo Select an option to run tests:
echo.
echo [1] Run All Validation Tests (50+ scenarios) - RECOMMENDED
echo [2] Run Field Validation Tests Only (27 scenarios)
echo [3] Run Duplicate and Boundary Tests Only (29 scenarios) - NEW
echo [4] Run Duplicate Detection Tests Only (4 scenarios) - NEW
echo [5] Run Boundary Value Tests Only (25 scenarios) - NEW
echo [6] Run All Fields Empty Test Only
echo [7] Run Error Persistence Test Only
echo [8] Run by Group: Validation Tests
echo [9] Run by Group: Negative Tests
echo [10] Run by Group: Boundary Tests - NEW
echo [11] Run by Group: Duplicate Tests - NEW
echo [12] Run Full Test Suite (includes all tests)
echo [13] Exit
echo.

set /p choice="Enter your choice (1-13): "

if "%choice%"=="1" goto run_all_validation
if "%choice%"=="2" goto run_field_validation
if "%choice%"=="3" goto run_duplicate_boundary
if "%choice%"=="4" goto run_duplicate_only
if "%choice%"=="5" goto run_boundary_only
if "%choice%"=="6" goto run_empty_fields
if "%choice%"=="7" goto run_error_persistence
if "%choice%"=="8" goto run_validation_group
if "%choice%"=="9" goto run_negative_group
if "%choice%"=="10" goto run_boundary_group
if "%choice%"=="11" goto run_duplicate_group
if "%choice%"=="12" goto run_full_suite
if "%choice%"=="13" goto exit

echo Invalid choice. Please run the script again.
goto end

:run_all_validation
echo.
echo ========================================================================
echo Running All Validation Tests (50+ scenarios)...
echo Includes: Field Validation, Duplicate Detection, Boundary Tests
echo ========================================================================
echo.
call mvn clean test -DsuiteXmlFile=testng-validation.xml
goto show_reports

:run_field_validation
echo.
echo ========================================================================
echo Running Field Validation Tests Only (27 scenarios)...
echo ========================================================================
echo.
call mvn clean test -Dtest=CustomerRegistrationValidationTest
goto show_reports

:run_duplicate_boundary
echo.
echo ========================================================================
echo Running Duplicate and Boundary Tests (29 scenarios)...
echo Includes: Duplicate Detection (4) + Boundary Values (25)
echo ========================================================================
echo.
call mvn clean test -Dtest=CustomerRegistrationDuplicateBoundaryTest
goto show_reports

:run_duplicate_only
echo.
echo ========================================================================
echo Running Duplicate Detection Tests Only (4 scenarios)...
echo ========================================================================
echo.
call mvn clean test -Dtest=CustomerRegistrationDuplicateBoundaryTest#testDuplicateCustomerRegistration
goto show_reports

:run_boundary_only
echo.
echo ========================================================================
echo Running Boundary Value Tests Only (25 scenarios)...
echo ========================================================================
echo.
call mvn clean test -Dtest=CustomerRegistrationDuplicateBoundaryTest#testCustomerRegistrationBoundaryValues
goto show_reports
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

:run_boundary_group
echo.
echo ========================================================================
echo Running Boundary Group Tests...
echo ========================================================================
echo.
call mvn clean test -Dgroups=boundary
goto show_reports

:run_duplicate_group
echo.
echo ========================================================================
echo Running Duplicate Group Tests...
echo ========================================================================
echo.
call mvn clean test -Dgroups=duplicate
goto show_reports

:run_full_suite
echo.
echo ========================================================================
echo Running Full Test Suite (includes all tests)...
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
