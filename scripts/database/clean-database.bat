@echo off
REM =====================================================
REM Financer - Clean Database (DANGER!)
REM Description: Completely wipes database and recreates schema
REM Usage: clean-database.bat
REM WARNING: This deletes ALL data!
REM =====================================================

setlocal enabledelayedexpansion

REM =====================================================
REM Configuration
REM =====================================================
set LOG_DIR=..\..\logs
set LOG_FILE=%LOG_DIR%\clean-db-%date:~-4%%date:~3,2%%date:~0,2%-%time:~0,2%%time:~3,2%%time:~6,2%.log
set LOG_FILE=%LOG_FILE: =0%

REM =====================================================
REM Create logs directory
REM =====================================================
if not exist "%LOG_DIR%" mkdir "%LOG_DIR%"

REM =====================================================
REM Start logging
REM =====================================================
call :log "========================================================"
call :log "  FINANCER - CLEAN DATABASE (DANGER!)"
call :log "========================================================"
call :log ""
call :log "[WARNING] This will DELETE ALL DATA in the database!"
call :log "[INFO] Clean Start Time: %date% %time%"
call :log "[INFO] Log File: %LOG_FILE%"
call :log ""

REM =====================================================
REM Confirmation
REM =====================================================
echo.
echo ========================================================
echo   WARNING: DATABASE DESTRUCTION
echo ========================================================
echo.
echo This script will:
echo   1. Drop ALL tables in the 'financer' schema
echo   2. Drop the 'financer' schema completely
echo   3. Drop Flyway history table
echo   4. Reset database to initial state
echo.
echo THIS ACTION CANNOT BE UNDONE!
echo.
set /p confirm1="Type 'DELETE' to confirm: "

if not "%confirm1%"=="DELETE" (
    call :log "[INFO] Operation cancelled by user"
    call :log "========================================================"
    echo.
    echo [INFO] Operation cancelled.
    echo.
    pause
    exit /b 0
)

echo.
set /p confirm2="Are you ABSOLUTELY sure? (Y/N): "

if /i not "%confirm2%"=="Y" (
    call :log "[INFO] Operation cancelled by user (second confirmation)"
    call :log "========================================================"
    echo.
    echo [INFO] Operation cancelled.
    echo.
    pause
    exit /b 0
)

call :log "[WARNING] User confirmed database deletion"
call :log ""

REM =====================================================
REM Check Docker
REM =====================================================
call :log "[INFO] Checking Docker availability..."
docker info >nul 2>&1
if errorlevel 1 (
    call :log "[ERROR] Docker is not running!"
    call :log "[ERROR] Exit Code: 1"
    call :log "========================================================"
    pause
    exit /b 1
)
call :log "[SUCCESS] Docker is running"
call :log ""

REM =====================================================
REM Check PostgreSQL
REM =====================================================
call :log "[INFO] Checking PostgreSQL container..."
docker ps --filter "name=financer-postgres" --format "{{.Names}}" | findstr "financer-postgres" >nul
if errorlevel 1 (
    call :log "[WARNING] PostgreSQL is not running"
    call :log "[INFO] Starting PostgreSQL..."
    docker-compose -f ..\..\docker-compose.infrastructure.yml up -d postgres
    timeout /t 10 /nobreak >nul
)
call :log "[SUCCESS] PostgreSQL is running"
call :log ""

REM =====================================================
REM Backup current state (optional log)
REM =====================================================
call :log "[INFO] Logging current database state before deletion..."
docker exec financer-postgres psql -U financer_user -d financer -c "\dt financer.*" >> "%LOG_FILE%" 2>&1
call :log ""

REM =====================================================
REM Execute Cleanup
REM =====================================================
call :log "[INFO] ========================================"
call :log "[INFO]   EXECUTING DATABASE CLEANUP"
call :log "[INFO] ========================================"
call :log ""

REM Drop all tables in financer schema
call :log "[INFO] Step 1: Dropping all tables in 'financer' schema..."
docker exec financer-postgres psql -U financer_user -d financer -c "DROP SCHEMA IF EXISTS financer CASCADE;" >> "%LOG_FILE%" 2>&1
if errorlevel 1 (
    call :log "[ERROR] Failed to drop schema"
    call :log "[ERROR] Exit Code: 2"
    call :log "========================================================"
    pause
    exit /b 2
)
call :log "[SUCCESS] Schema 'financer' dropped"
call :log ""

REM Drop Flyway history (if exists in public schema)
call :log "[INFO] Step 2: Dropping Flyway history table..."
docker exec financer-postgres psql -U financer_user -d financer -c "DROP TABLE IF EXISTS public.flyway_schema_history CASCADE;" >> "%LOG_FILE%" 2>&1
call :log "[SUCCESS] Flyway history dropped"
call :log ""

REM Recreate schema
call :log "[INFO] Step 3: Recreating 'financer' schema..."
docker exec financer-postgres psql -U financer_user -d financer -c "CREATE SCHEMA IF NOT EXISTS financer;" >> "%LOG_FILE%" 2>&1
if errorlevel 1 (
    call :log "[ERROR] Failed to recreate schema"
    call :log "[ERROR] Exit Code: 3"
    call :log "========================================================"
    pause
    exit /b 3
)
call :log "[SUCCESS] Schema 'financer' recreated"
call :log ""

REM Verify cleanup
call :log "[INFO] Step 4: Verifying cleanup..."
docker exec financer-postgres psql -U financer_user -d financer -c "\dt financer.*" >> "%LOG_FILE%" 2>&1
call :log "[SUCCESS] Cleanup verification complete"
call :log ""

REM =====================================================
REM Summary
REM =====================================================
call :log "[INFO] ========================================"
call :log "[SUCCESS] Database cleaned successfully!"
call :log "[INFO] Clean Status: SUCCESS"
call :log "[INFO] Exit Code: 0"
call :log ""
call :log "[INFO] Database is now in clean state:"
call :log "  - Schema 'financer' exists but is empty"
call :log "  - All tables deleted"
call :log "  - Flyway history deleted"
call :log "  - Ready for fresh migrations"
call :log ""
call :log "[NEXT STEPS]"
call :log "  Run 'run-migrations.bat' to apply all migrations"
call :log ""
call :log "[INFO] Clean End Time: %date% %time%"
call :log "========================================================"

echo.
echo [SUCCESS] Database cleaned successfully!
echo [INFO] Log file: %LOG_FILE%
echo.
echo [NEXT STEP] Run 'run-migrations.bat' to apply migrations
echo.
pause
exit /b 0

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
