@echo off
REM =====================================================
REM Financer - Build All Libraries
REM Description: Compiles all shared libraries and validates artifacts
REM Usage: build-libs.bat
REM =====================================================

setlocal enabledelayedexpansion

REM =====================================================
REM Configuration
REM =====================================================
set LIBS_DIR=%~dp0..\libs
set LOG_DIR=%~dp0..\logs
set LOG_FILE=%LOG_DIR%\build-libs-%date:~-4%%date:~3,2%%date:~0,2%-%time:~0,2%%time:~3,2%%time:~6,2%.log
set LOG_FILE=%LOG_FILE: =0%

REM Create logs directory
if not exist "%LOG_DIR%" mkdir "%LOG_DIR%"

REM =====================================================
REM Start logging
REM =====================================================
call :log "========================================================"
call :log "  FINANCER - BUILD SHARED LIBRARIES"
call :log "========================================================"
call :log ""
call :log "[INFO] Build Start Time: %date% %time%"
call :log "[INFO] Log File: %LOG_FILE%"
call :log "[INFO] Working Directory: %LIBS_DIR%"
call :log ""

REM =====================================================
REM Check Maven
REM =====================================================
call :log "[INFO] Checking Maven availability..."
mvn --version >nul 2>&1
if errorlevel 1 (
    call :log "[ERROR] Maven is not installed or not in PATH!"
    call :log "[ERROR] Please install Maven and try again."
    call :log "[ERROR] Exit Code: 1"
    call :log "========================================================"
    pause
    exit /b 1
)
call :log "[SUCCESS] Maven is available"
call :log ""

REM =====================================================
REM Clean Maven Local Repository (libs only)
REM =====================================================
call :log "[INFO] Cleaning previous Financer artifacts from local repository..."
set M2_REPO=%USERPROFILE%\.m2\repository\com\financer
if exist "%M2_REPO%" (
    rmdir /s /q "%M2_REPO%" 2>nul
    call :log "[SUCCESS] Cleaned: %M2_REPO%"
) else (
    call :log "[INFO] No previous artifacts found"
)
call :log ""

REM =====================================================
REM Build All Libraries
REM =====================================================
call :log "[INFO] ========================================"
call :log "[INFO]   BUILDING ALL LIBRARIES"
call :log "[INFO] ========================================"
call :log ""

cd /d "%LIBS_DIR%"

call :log "[INFO] Executing: mvn clean install -DskipTests"
call :log ""

REM Capture Maven output
mvn clean install -DskipTests >> "%LOG_FILE%" 2>&1
set MAVEN_EXIT_CODE=%errorlevel%

call :log ""
call :log "[INFO] ========================================"

if %MAVEN_EXIT_CODE% equ 0 (
    call :log "[SUCCESS] Build completed successfully!"
    call :log "[INFO] Build Status: SUCCESS"
    call :log "[INFO] Exit Code: 0"
) else (
    call :log "[ERROR] Build failed!"
    call :log "[ERROR] Build Status: FAILED"
    call :log "[ERROR] Exit Code: %MAVEN_EXIT_CODE%"
    call :log "[ERROR] Check log file for details: %LOG_FILE%"
    call :log "========================================================"
    pause
    exit /b %MAVEN_EXIT_CODE%
)

call :log ""

REM =====================================================
REM Validate Artifacts
REM =====================================================
call :log "[INFO] ========================================"
call :log "[INFO]   VALIDATING ARTIFACTS"
call :log "[INFO] ========================================"
call :log ""

set VALIDATION_FAILED=0
set M2_REPO=%USERPROFILE%\.m2\repository\com\financer

REM List of expected artifacts
set LIBS=financer-common financer-eureka-client
set VERSION=1.0.0

for %%L in (%LIBS%) do (
    call :log "[INFO] Validating: %%L"
    
    set LIB_DIR=%M2_REPO%\%%L\%VERSION%
    set JAR_FILE=!LIB_DIR!\%%L-%VERSION%.jar
    set POM_FILE=!LIB_DIR!\%%L-%VERSION%.pom
    set SOURCES_FILE=!LIB_DIR!\%%L-%VERSION%-sources.jar
    
    REM Check JAR
    if exist "!JAR_FILE!" (
        call :log "  [OK] JAR found: %%L-%VERSION%.jar"
        
        REM Check JAR size
        for %%F in ("!JAR_FILE!") do set JAR_SIZE=%%~zF
        if !JAR_SIZE! gtr 1024 (
            call :log "      Size: !JAR_SIZE! bytes"
        ) else (
            call :log "      [WARN] JAR seems too small: !JAR_SIZE! bytes"
        )
    ) else (
        call :log "  [ERROR] JAR not found: %%L-%VERSION%.jar"
        set VALIDATION_FAILED=1
    )
    
    REM Check POM
    if exist "!POM_FILE!" (
        call :log "  [OK] POM found: %%L-%VERSION%.pom"
    ) else (
        call :log "  [ERROR] POM not found: %%L-%VERSION%.pom"
        set VALIDATION_FAILED=1
    )
    
    REM Check Sources
    if exist "!SOURCES_FILE!" (
        call :log "  [OK] Sources found: %%L-%VERSION%-sources.jar"
    ) else (
        call :log "  [WARN] Sources not found: %%L-%VERSION%-sources.jar"
    )
    
    call :log ""
)

call :log "[INFO] ========================================"

if %VALIDATION_FAILED% equ 0 (
    call :log "[SUCCESS] All artifacts validated successfully!"
    call :log "[INFO] Validation Status: SUCCESS"
) else (
    call :log "[ERROR] Artifact validation failed!"
    call :log "[ERROR] Validation Status: FAILED"
    call :log "[ERROR] Some artifacts are missing or invalid"
)

call :log ""

REM =====================================================
REM Summary
REM =====================================================
call :log "[INFO] ========================================"
call :log "[INFO]   BUILD SUMMARY"
call :log "[INFO] ========================================"
call :log ""
call :log "[INFO] Maven Build: %MAVEN_EXIT_CODE%"
call :log "[INFO] Artifact Validation: %VALIDATION_FAILED%"
call :log "[INFO] Local Repository: %M2_REPO%"
call :log ""
call :log "[INFO] Libraries built:"
for %%L in (%LIBS%) do (
    call :log "  - %%L v%VERSION%"
)
call :log ""
call :log "[INFO] Build End Time: %date% %time%"
call :log "========================================================"
call :log ""

if %VALIDATION_FAILED% neq 0 (
    set FINAL_EXIT_CODE=2
) else (
    set FINAL_EXIT_CODE=%MAVEN_EXIT_CODE%
)

if %FINAL_EXIT_CODE% equ 0 (
    echo.
    echo [SUCCESS] Build completed successfully!
    echo [INFO] All libraries are available in Maven local repository
    echo [INFO] Log file: %LOG_FILE%
    echo.
) else (
    echo.
    echo [ERROR] Build failed with exit code %FINAL_EXIT_CODE%
    echo [ERROR] Check log file: %LOG_FILE%
    echo.
)

pause
exit /b %FINAL_EXIT_CODE%

REM =====================================================
REM Logging Function
REM =====================================================
:log
set msg=%~1
if "%msg%"=="" (
    echo.
    echo. >> "%LOG_FILE%"
) else (
    echo %msg%
    echo %msg% >> "%LOG_FILE%"
)
goto :eof

endlocal
