@echo off
title College Placement Portal

:: Ensure Maven and Java are in PATH if running from Explorer
set "PATH=%PATH%;C:\Apache Maven\apache-maven-3.9.16\bin;C:\Program Files\Java\jdk-25.0.3\bin"

cd /d "%~dp0"

echo ===================================================
echo  College Placement Portal - Quick Launcher
echo ===================================================
echo.
echo Choose how you want to run the portal:
echo [1] Instant Demo Mode - In-Memory DB (Zero Install, Recommended)
echo [2] Production / Local MySQL Mode (Requires MySQL on port 3306)
echo.

set "mode=1"
set /p mode="Enter choice [1 or 2] (Default is 1): "

if "%mode%"=="2" goto MYSQL_MODE
goto H2_MODE

:MYSQL_MODE
echo.
echo Starting with local MySQL...
call mvn spring-boot:run
goto FINISH

:H2_MODE
echo.
echo Starting in Instant Demo Mode with In-Memory Database...
call mvn spring-boot:run -Dspring-boot.run.profiles=h2
goto FINISH

:FINISH
if %ERRORLEVEL% NEQ 0 (
    echo.
    echo ===================================================
    echo Application exited with error code %ERRORLEVEL%.
    echo ===================================================
)

pause
