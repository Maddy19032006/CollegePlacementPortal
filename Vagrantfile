# -*- mode: ruby -*-
# vi: set ft=ruby :
# ==============================================================================
# Vagrantfile for College Placement Portal (Lab Experiment 8)
# Demonstrates Type-2 Hypervisor Virtualization (VirtualBox) with Automated Provisioning
# ==============================================================================

Vagrant.configure("2") do |config|
  # Base Box: Ubuntu 22.04 LTS (Jammy Jellyfish)
  config.vm.box = "ubuntu/jammy64"

  # Hostname
  config.vm.hostname = "placement-portal-dev"

  # Port Forwarding: Access Spring Boot on host at http://localhost:8081
  config.vm.network "forwarded_port", guest: 8081, host: 8081, auto_correct: true
  # MySQL Port Forwarding (optional, forwarded to 3307 to prevent conflict with local MySQL)
  config.vm.network "forwarded_port", guest: 3306, host: 3307, auto_correct: true

  # VirtualBox Provider Settings (CPU / RAM Allocation)
  config.vm.provider "virtualbox" do |vb|
    vb.name = "CollegePlacementPortal-VM"
    vb.memory = "2048" # 2 GB RAM
    vb.cpus = 2        # 2 vCPUs
    vb.customize ["modifyvm", :id, "--natdnshostresolver1", "on"]
  end

  # Automated Shell Provisioning
  config.vm.provision "shell", inline: <<-SHELL
    set -e
    echo ">>> Updating Ubuntu packages..."
    sudo apt-get update -y
    sudo apt-get install -y openjdk-21-jdk maven mysql-server curl git

    echo ">>> Configuring MySQL database..."
    sudo systemctl enable mysql
    sudo systemctl start mysql

    sudo mysql -e "CREATE DATABASE IF NOT EXISTS placement_portal;"
    sudo mysql -e "CREATE USER IF NOT EXISTS 'placement_user'@'localhost' IDENTIFIED BY 'SecurePassword123!';"
    sudo mysql -e "GRANT ALL PRIVILEGES ON placement_portal.* TO 'placement_user'@'localhost';"
    sudo mysql -e "FLUSH PRIVILEGES;"

    echo ">>> Building and starting Placement Portal..."
    cd /vagrant
    # Run in background via nohup if jar is built, or compile on startup
    mvn clean package -DskipTests
    nohup java -jar target/placement-portal.jar \
      --spring.datasource.username=placement_user \
      --spring.datasource.password=SecurePassword123! \
      --server.port=8081 > /var/log/placement-portal.log 2>&1 &

    echo ">>> College Placement Portal is running at http://localhost:8081"
  SHELL
end
