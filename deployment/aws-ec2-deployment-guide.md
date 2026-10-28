# ☁️ AWS EC2 & Linux Systemd Deployment Guide (Lab Experiments 3 & 4)

This guide walks through deploying and managing the **College Placement Portal** on AWS EC2, configuring Linux system services using `systemctl`, and setting up storage, snapshots, and AMIs.

---

## Experiment 3: AWS Account & EC2 Instance Setup

### 1. Create Key Pair
- In AWS Console $\rightarrow$ **EC2** $\rightarrow$ **Key Pairs** $\rightarrow$ **Create Key Pair**.
- Name: `placement-portal-key`.
- Key pair type: **RSA** or **ED25519**.
- Private key file format:
  - `.pem` for OpenSSH / Git Bash / macOS / Linux.
  - `.ppk` for PuTTY on Windows.

### 2. Launching EC2 Instances
- **Instance 1 (Amazon Linux):** Launch `t2.micro` or `t3.micro` using Amazon Linux 2023 AMI.
- **Instance 2 (Ubuntu):** Launch `t2.micro` using Ubuntu 22.04 LTS AMI.
- **Security Group Rules:**
  - `SSH (22)`: My IP (for administrative access)
  - `Custom TCP (8081)`: 0.0.0.0/0 (for College Placement Portal web traffic)
  - `HTTP (80)` / `HTTPS (443)`: 0.0.0.0/0

### 3. Connecting to the Instance
- **Via Git Bash / Terminal:**
  ```bash
  chmod 400 placement-portal-key.pem
  ssh -i placement-portal-key.pem ubuntu@<EC2-PUBLIC-IP>
  ```
- **Via PuTTY:**
  - Load `.ppk` key in `Connection -> SSH -> Auth -> Credentials`.
  - Enter Host: `ubuntu@<EC2-PUBLIC-IP>`.
- **Accessing via Password (without Private Key):**
  1. Open `/etc/ssh/sshd_config`.
  2. Set `PasswordAuthentication yes`.
  3. Create user password: `sudo passwd ubuntu`.
  4. Restart SSH: `sudo systemctl restart sshd`.

---

## Experiment 4: Advanced EC2 Operations & Linux Service Manager

### 1. Termination Protection & Shutdown Behavior
- **Termination Protection:** Select Instance $\rightarrow$ **Actions** $\rightarrow$ **Instance Settings** $\rightarrow$ **Change Termination Protection** $\rightarrow$ Enable. (Prevents accidental instance termination via console or API).
- **Shutdown Behavior:** Set to `Stop` (default) or `Terminate`.

### 2. EBS Volume, Snapshots & Custom AMI
- **Create EBS Snapshot:**
  - In EC2 Console $\rightarrow$ **Elastic Block Store** $\rightarrow$ **Volumes** $\rightarrow$ Select app volume $\rightarrow$ **Actions** $\rightarrow$ **Create Snapshot**.
- **Create Amazon Machine Image (AMI):**
  - Select Instance $\rightarrow$ **Actions** $\rightarrow$ **Image and templates** $\rightarrow$ **Create image**.
  - Name: `placement-portal-v1-ami`.
  - This allows instant cloning or autoscaling of our pre-configured server.

### 3. Linux System & Service Manager (`systemctl` and `journalctl`)
Deploy the application as a standard Linux background daemon using the provided unit file:

```bash
# 1. Copy the JAR and service file to server
scp -i placement-portal-key.pem target/placement-portal.jar ubuntu@<EC2-IP>:/tmp/
scp -i placement-portal-key.pem deployment/placement-portal.service ubuntu@<EC2-IP>:/tmp/

# 2. SSH into instance
ssh -i placement-portal-key.pem ubuntu@<EC2-IP>

# 3. Run automated setup script
chmod +x /tmp/ec2-setup.sh
sudo /tmp/ec2-setup.sh

# 4. Service Management Commands (Mandatory Lab Viva Commands)
sudo systemctl daemon-reload            # Reload systemd manager configuration
sudo systemctl enable placement-portal   # Enable service on boot
sudo systemctl start placement-portal    # Start the application
sudo systemctl status placement-portal   # Verify service state (Active: running)
sudo systemctl restart placement-portal  # Restart application
sudo systemctl stop placement-portal     # Stop application

# 5. Service Journaling and Log Inspection
sudo journalctl -u placement-portal -f               # Follow real-time application logs
sudo journalctl -u placement-portal -n 50            # View last 50 log lines
sudo journalctl -u placement-portal --since "1 hour ago"  # Time-filtered logs
```
