@echo off
echo ===================================================
echo  Starting Vagrant Virtual Machine (VirtualBox)
echo ===================================================
vagrant up
echo.
echo VM Status:
vagrant status
echo.
echo Application URL: http://localhost:8081
echo To SSH into the VM: vagrant ssh
pause
