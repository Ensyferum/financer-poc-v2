@echo off
REM =====================================================
REM Financer - Database Migration (Serverless)
REM Description: Executes Flyway migrations without persistent container
REM Usage: run-migrations.bat
REM =====================================================

setlocal enabledelayedexpansion

REM =====================================================
REM Configuration
REM =====================================================
set LOG_DIR=..\..\logs
set LOG_FILE=%LOG_DIR%\migration-%date:~-4%%date:~3,2%%date:~0,2%-%time:~0,2%%time:~3,2%%time:~6,2%.log
set LOG_FILE=%LOG_FILE: =0%

REM =====================================================
REM Create logs directory
REM =====================================================
if not exist "%LOG_DIR%" mkdir "%LOG_DIR%"

REM =====================================================
REM Start logging
REM =====================================================
call :log "========================================================"
call :log "  FINANCER - DATABASE MIGRATION (SERVERLESS)"
call :log "========================================================"
call :log ""
call :log "[INFO] Migration Start Time: %date% %time%"
call :log "[INFO] Log File: %LOG_FILE%"
call :log ""

REM =====================================================
REM Check Docker
REM =====================================================
call :log "[INFO] Checking Docker availability..."
docker info >nul 2>&1
if errorlevel 1 (
    call :log "[ERROR] Docker is not running!"
    call :log "[ERROR] Please start Docker Desktop and try again."
    call :log ""
    call :log "[ERROR] Migration Status: FAILED - Docker not available"
    call :log "[ERROR] Exit Code: 1"
    call :log "========================================================"
    pause
    exit /b 1
)
call :log "[SUCCESS] Docker is running"
call :log ""

REM =====================================================
REM Check if PostgreSQL is running
REM =====================================================
call :log "[INFO] Checking PostgreSQL container..."
docker ps --filter "name=financer-postgres" --format "{{.Names}}" | findstr "financer-postgres" >nul
if errorlevel 1 (
    call :log "[WARNING] PostgreSQL container is not running"
    call :log "[INFO] Starting PostgreSQL..."
    
    docker-compose -f ..\..\docker-compose.infrastructure.yml up -d postgres
    if errorlevel 1 (
        call :log "[ERROR] Failed to start PostgreSQL"
        call :log "[ERROR] Migration Status: FAILED - Cannot start database"
        call :log "[ERROR] Exit Code: 2"
        call :log "========================================================"
        pause
        exit /b 2
    )
    
    call :log "[INFO] Waiting for PostgreSQL to be ready (30 seconds)..."
    timeout /t 30 /nobreak >nul
    call :log "[SUCCESS] PostgreSQL started"
) else (
    call :log "[SUCCESS] PostgreSQL is already running"
)
call :log ""

REM =====================================================
REM Execute Flyway Migration
REM =====================================================
call :log "[INFO] ========================================"
call :log "[INFO]   EXECUTING FLYWAY MIGRATION"
call :log "[INFO] ========================================"
call :log ""
call :log "[INFO] Flyway Version: 10-alpine"
call :log "[INFO] Database: jdbc:postgresql://postgres:5432/financer"
call :log "[INFO] User: financer_user"
call :log "[INFO] Schema: financer"
call :log "[INFO] Migration Location: /flyway/sql"
call :log ""

REM Capture Flyway output
for /f "delims=" %%i in ('docker-compose -f ..\..\docker-compose.infrastructure.yml --profile migration run --rm flyway-migration migrate 2^>^&1') do (
    echo %%i
    echo [FLYWAY] %%i >> "%LOG_FILE%"
)

set FLYWAY_EXIT_CODE=%errorlevel%

call :log ""
call :log "[INFO] ========================================"

if %FLYWAY_EXIT_CODE% equ 0 (
    call :log "[SUCCESS] Migration completed successfully!"
    call :log "[INFO] Migration Status: SUCCESS"
    call :log "[INFO] Exit Code: 0"
    call :log ""
    
    REM Get migration info
    call :log "[INFO] Fetching migration history..."
    docker exec financer-postgres psql -U financer_user -d financer -c "SELECT version, description, type, installed_on, success FROM financer.flyway_schema_history ORDER BY installed_rank DESC LIMIT 5;" >> "%LOG_FILE%" 2>&1
    
    call :log ""
    call :log "[INFO] Last 5 migrations applied (check log file for details)"
) else (
    call :log "[ERROR] Migration failed!"
    call :log "[ERROR] Migration Status: FAILED - Flyway execution error"
    call :log "[ERROR] Exit Code: %FLYWAY_EXIT_CODE%"
    call :log ""
    call :log "[ERROR] Error Details:"
    call :log "[ERROR] Check the Flyway output above for specific error messages"
    call :log ""
    call :log "[TROUBLESHOOTING]"
    call :log "  1. Check SQL syntax in migration files"
    call :log "  2. Verify database connectivity"
    call :log "  3. Check for constraint violations"
    call :log "  4. Review previous migrations for conflicts"
    call :log "  5. Use 'repair-migrations.bat' if checksums are mismatched"
)

call :log ""
call :log "[INFO] Migration End Time: %date% %time%"
call :log "========================================================"
call :log ""

if %FLYWAY_EXIT_CODE% neq 0 (
    pause
    exit /b %FLYWAY_EXIT_CODE%
)

echo.
echo [SUCCESS] Migration completed! Check logs at: %LOG_FILE%
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
