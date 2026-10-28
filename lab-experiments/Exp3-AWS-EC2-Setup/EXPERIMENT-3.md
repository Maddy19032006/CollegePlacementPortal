# 🧪 Experiment 3: AWS Service & EC2 Setup
**Course:** ISWE406P · Agile Development Process and DevOps Lab  
**Lab Hours:** 5 Hours  
**Application Context:** College Placement Portal  

---

## 1. Aim
To create an AWS account, launch and filter EC2 instances (Amazon Linux & Ubuntu), generate cryptographic key pairs (public/private keys), connect to instances via MobaXterm, PuTTY, and Git Bash, and configure password-based authentication without using a private key.

---

## 2. Tools & Cloud Services
- **Cloud Provider:** Amazon Web Services (AWS) EC2 (Elastic Compute Cloud)
- **AMI Distributions:** Amazon Linux 2023 & Ubuntu 22.04 LTS
- **SSH Clients:** PuTTY, Git Bash, MobaXterm, OpenSSH

---

## 3. Step-by-Step Practical Procedure

### Step 1: Create Key Pair in AWS Console
1. Navigate to **EC2 Console** $\rightarrow$ **Network & Security** $\rightarrow$ **Key Pairs**.
2. Click **Create key pair**:
   - Name: `placement-portal-key`
   - Key type: `RSA`
   - Private key format: `.pem` (for Git Bash / Linux / macOS) or `.ppk` (for PuTTY).
3. Download and secure the private key:
   ```bash
   chmod 400 placement-portal-key.pem
   ```

### Step 2: Launch EC2 Instances
- **Instance A (Amazon Linux):** Launch a `t2.micro` or `t3.micro` free-tier instance running Amazon Linux 2023.
- **Instance B (Ubuntu):** Launch a `t2.micro` running Ubuntu 22.04 LTS.
- **Security Group (Firewall) Configuration:**
  - Inbound rule 1: `SSH` (Port 22) $\rightarrow$ Source: `My IP` or `0.0.0.0/0`
  - Inbound rule 2: `Custom TCP` (Port 8081) $\rightarrow$ Source: `0.0.0.0/0` (Placement Portal)

### Step 3: Filtering Instances
Use the search and filter bar in the EC2 Console:
- Filter by Tag: `Environment = Production`
- Filter by AMI: `Ubuntu` or `Amazon Linux`
- Filter by Instance State: `Instance State = running`

### Step 4: Connecting via Different SSH Clients

#### A. Connecting via Git Bash / Terminal
```bash
ssh -i placement-portal-key.pem ubuntu@<EC2-PUBLIC-IP>
# For Amazon Linux:
# ssh -i placement-portal-key.pem ec2-user@<EC2-PUBLIC-IP>
```

#### B. Connecting via PuTTY
1. Open PuTTY.
2. Under **Host Name**, enter: `ubuntu@<EC2-PUBLIC-IP>`.
3. Go to **Connection** $\rightarrow$ **SSH** $\rightarrow$ **Auth** $\rightarrow$ **Credentials**.
4. Browse and select your `.ppk` file.
5. Click **Open** and accept the host key warning.

#### C. Connecting via MobaXterm
1. Click **Session** $\rightarrow$ **SSH**.
2. Remote host: `<EC2-PUBLIC-IP>`.
3. Specify username: `ubuntu`.
4. Check **Use private key** and browse to `placement-portal-key.pem`.
5. Click **OK**.

### Step 5: Accessing Instance Using Username & Password (Without Private Key)
*This is explicitly required by Experiment 3 syllabus!*
1. Connect using the key pair first:
   ```bash
   ssh -i placement-portal-key.pem ubuntu@<EC2-PUBLIC-IP>
   ```
2. Set a password for user `ubuntu`:
   ```bash
   sudo passwd ubuntu
   # Enter new password, e.g., LabDemo@2026
   ```
3. Enable password authentication in the SSH daemon:
   ```bash
   sudo sed -i 's/PasswordAuthentication no/PasswordAuthentication yes/' /etc/ssh/sshd_config
   sudo sed -i 's/KbdInteractiveAuthentication no/KbdInteractiveAuthentication yes/' /etc/ssh/sshd_config
   sudo systemctl restart sshd
   ```
4. Test connecting from your terminal **without** using the `-i` private key flag:
   ```bash
   ssh ubuntu@<EC2-PUBLIC-IP>
   # Password prompt appears: enter LabDemo@2026
   ```

---

## 4. Viva Questions & Answers
1. **Q: What is the default user for Amazon Linux vs. Ubuntu on EC2?**  
   *A: Amazon Linux uses `ec2-user`, while Ubuntu uses `ubuntu`.*
2. **Q: Why does SSH require permissions `400` on the private key file?**  
   *A: SSH clients refuse to use a private key if it is readable by other users on the operating system to prevent key compromise.*
