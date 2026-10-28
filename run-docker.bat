@echo off
echo ===================================================
echo  Starting College Placement Portal via Docker
echo ===================================================
echo Starting Spring Boot + MySQL containers...
docker compose up -d --build
echo.
echo Containers started!
docker compose ps
echo.
echo Application URL: http://localhost:8081
pause
