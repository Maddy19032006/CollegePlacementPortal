# 🧪 Experiment 4: EC2 - Elastic Compute Cloud & Systemd
**Course:** ISWE406P · Agile Development Process and DevOps Lab  
**Lab Hours:** 4 Hours  
**Application Context:** College Placement Portal  

---

## 1. Aim
To deploy the College Placement Portal application onto an AWS EC2 instance, configure Termination Protection, evaluate EC2 Shutdown Behavior, manage Elastic Block Store (EBS) Volumes and Snapshots, create a custom Amazon Machine Image (AMI), and administer the service using the Linux System and Service Manager (`systemctl` and `journalctl`).

---

## 2. Practical Tasks & Verification Steps

### Task 1: Enable Termination Protection
1. In EC2 Console, select the instance running the Placement Portal.
2. Go to **Actions** $\rightarrow$ **Instance Settings** $\rightarrow$ **Change termination protection**.
3. Check **Enable** and save.
4. *Verification:* Attempt to terminate the instance via the AWS Console. AWS blocks the action with: `Failed to terminate an instance: The instance 'i-xxxx' may not be terminated. Modify its 'disableApiTermination' attribute and try again.`

### Task 2: Configure EC2 Shutdown Behavior
- Set Instance Shutdown Behavior to **Stop** (preserves EBS root volume data and state) vs **Terminate** (destroys instance upon OS shutdown).
- Test executing `sudo shutdown -h now` inside SSH; observe the AWS Console transitions state from `running` $\rightarrow$ `stopping` $\rightarrow$ `stopped`.

### Task 3: EBS Volume Snapshot & Custom AMI Creation
1. **Create EBS Snapshot:**
   - Go to **EC2 Console** $\rightarrow$ **Volumes** $\rightarrow$ Select root volume $\rightarrow$ **Actions** $\rightarrow$ **Create Snapshot**.
   - Name: `placement-portal-ebs-snapshot`.
2. **Create Amazon Machine Image (AMI):**
   - Select the instance $\rightarrow$ **Actions** $\rightarrow$ **Image and templates** $\rightarrow$ **Create image**.
   - Image name: `placement-portal-v1-golden-ami`.
   - *Result:* An immutable backup of the entire configured server ready to launch identical replacement instances in seconds.

### Task 4: Linux System & Service Manager (`systemctl` & `journalctl`)
Deploy the application JAR as a permanent system daemon using the provided unit file:

```bash
# 1. Copy the systemd unit file to system directory
sudo cp deployment/placement-portal.service /etc/systemd/system/placement-portal.service

# 2. Reload systemd daemon
sudo systemctl daemon-reload

# 3. Enable service to start automatically on system boot
sudo systemctl enable placement-portal

# 4. Start the service
sudo systemctl start placement-portal

# 5. Check service status (Active: active (running))
sudo systemctl status placement-portal

# 6. Restart the service after updates
sudo systemctl restart placement-portal

# 7. Stop the service
sudo systemctl stop placement-portal
```

### Task 5: System Logging with `journalctl`
Inspect real-time logs produced by the Spring Boot background process:
```bash
# Follow live logs in real time
sudo journalctl -u placement-portal -f

# View the last 100 log lines
sudo journalctl -u placement-portal -n 100

# View logs generated today
sudo journalctl -u placement-portal --since today
```

---

## 3. Viva Questions & Answers
1. **Q: What is the difference between EBS Volume and EBS Snapshot?**  
   *A: An EBS Volume is a live, attached block-storage device formatted with a filesystem. An EBS Snapshot is an incremental, point-in-time backup of that volume stored durably in Amazon S3.*
2. **Q: What does `systemctl daemon-reload` do?**  
   *A: It instructs the systemd init system (PID 1) to re-read and reload all unit configuration files from `/etc/systemd/system/`, registering any new or modified `.service` files.*
