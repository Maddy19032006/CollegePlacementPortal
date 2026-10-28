#!/usr/bin/env bash
# ==============================================================================
# AWS EC2 Provisioning Script for College Placement Portal
# Target OS: Ubuntu 22.04 LTS / 24.04 LTS or Amazon Linux 2023
# ==============================================================================

set -euo pipefail

echo "=========================================="
echo " Starting EC2 Automated Provisioning"
echo "=========================================="

# 1. Update Package Manager
sudo apt-get update -y && sudo apt-get upgrade -y || sudo dnf update -y

# 2. Install OpenJDK 21 / 25
if command -v apt-get &> /dev/null; then
    sudo apt-get install -y openjdk-21-jdk mysql-server curl git
elif command -v dnf &> /dev/null; then
    sudo dnf install -y java-21-amazon-corretto mysql-server curl git
fi

# 3. Configure and Start MySQL
sudo systemctl enable mysql || sudo systemctl enable mysqld
sudo systemctl start mysql || sudo systemctl start mysqld

sudo mysql -e "CREATE DATABASE IF NOT EXISTS placement_portal;"
sudo mysql -e "CREATE USER IF NOT EXISTS 'placement_user'@'localhost' IDENTIFIED BY 'SecurePassword123!';"
sudo mysql -e "GRANT ALL PRIVILEGES ON placement_portal.* TO 'placement_user'@'localhost';"
sudo mysql -e "FLUSH PRIVILEGES;"

# 4. Create App Directory and Install Service
sudo mkdir -p /opt/placement-portal
sudo chown -R ubuntu:ubuntu /opt/placement-portal || sudo chown -R ec2-user:ec2-user /opt/placement-portal

if [ -f /tmp/placement-portal.jar ]; then
    sudo mv /tmp/placement-portal.jar /opt/placement-portal/placement-portal.jar
fi

if [ -f /tmp/placement-portal.service ]; then
    sudo cp /tmp/placement-portal.service /etc/systemd/system/placement-portal.service
    sudo systemctl daemon-reload
    sudo systemctl enable placement-portal
    sudo systemctl start placement-portal
fi

echo "=========================================="
echo " EC2 Provisioning Complete!"
echo " Check service: sudo systemctl status placement-portal"
echo " View logs: sudo journalctl -u placement-portal -f"
echo "=========================================="
