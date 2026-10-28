@echo off
echo ===================================================
echo  Starting College Placement Portal (Local Mode)
echo ===================================================
echo Prerequisites: Java 21/25 and Maven installed.
echo App will be accessible at: http://localhost:8081
echo Default Student: arjun / demoStudent@123
echo Default Officer: officer / demoOfficer@123
echo Default Admin:   admin / demoAdmin@123
echo ===================================================

mvn spring-boot:run
pause
