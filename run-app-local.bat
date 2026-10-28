@echo off
echo ===================================================
echo  🎓 College Placement Portal - Quick Launcher
echo ===================================================
echo.
echo Choose how you want to run the portal:
echo [1] Instant Demo Mode (Built-in In-Memory DB - Zero Install, Recommended)
echo [2] Production / Local MySQL Mode (Requires local MySQL on port 3306)
echo.
set /p mode="Enter choice [1 or 2] (Default is 1): "

if "%mode%"=="2" (
    echo.
    echo Starting with local MySQL...
    mvn spring-boot:run
) else (
    echo.
    echo Starting in Instant Demo Mode (In-Memory Database)...
    mvn spring-boot:run -Dspring-boot.run.profiles=h2
)
pause
