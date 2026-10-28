# College Placement Portal — Jenkins + GitHub + Docker Setup Guide
**Course:** ISWE406P — Agile Development Process and DevOps Lab  
**Lab Experiments Covered:** Exp 5 (Git), Exp 6 (Docker), Exp 7 (Jenkins CI/CD)

> **Goal:** Every commit you push to GitHub automatically triggers Jenkins to compile, test, containerize, push to Docker Hub, and deploy the live portal — all without touching your machine.

---

## Architecture Overview

```
┌───────────────────────────────────────────────────────────────┐
│                    Full CI/CD Data Flow                       │
│                                                               │
│  You push code                                                │
│       │                                                       │
│       ▼                                                       │
│  ┌─────────────┐   Webhook / Poll   ┌─────────────────────┐  │
│  │   GitHub    │ ──────────────────▶│     Jenkins         │  │
│  │   (SCM)     │                    │  (CI/CD Orchestrate)│  │
│  └─────────────┘                    └────────┬────────────┘  │
│                                              │               │
│                                    8-Stage Pipeline:         │
│                                    1. Checkout from GitHub    │
│                                    2. Build & Compile        │
│                                    3. JUnit + JaCoCo Tests   │
│                                    4. Package JAR            │
│                                    5. Docker Build Image     │
│                                    6. Docker Push to Hub ───▶│──▶ Docker Hub
│                                    7. Deploy with Compose    │
│                                    8. Smoke Test             │
│                                              │               │
│                                              ▼               │
│                                   ┌──────────────────────┐   │
│                                   │  App running locally  │   │
│                                   │  http://localhost:8081│   │
│                                   │  (MySQL in container) │   │
│                                   └──────────────────────┘   │
└───────────────────────────────────────────────────────────────┘
```

---

## Step 1: Install Jenkins Locally (One-Time Setup)

### Option A: Docker (Recommended — Zero Install)
```bash
# Run Jenkins with Docker pre-installed inside it (docker-in-docker capable image)
docker run -d \
  --name jenkins \
  -p 8080:8080 -p 50000:50000 \
  -v jenkins_home:/var/jenkins_home \
  -v /var/run/docker.sock:/var/run/docker.sock \
  jenkins/jenkins:lts-jdk21
```
- Access Jenkins at: **http://localhost:8080**
- Get the first-time unlock password: `docker exec jenkins cat /var/jenkins_home/secrets/initialAdminPassword`

### Option B: Windows Installer
1. Download Jenkins Windows installer from https://www.jenkins.io/download/ (LTS version).
2. Run the installer — Jenkins starts as a Windows Service on port 8080.
3. Access: **http://localhost:8080**

---

## Step 2: First-Time Jenkins Configuration

1. **Unlock Jenkins:** Paste the admin password from Step 1.
2. **Install Suggested Plugins** → click the recommended option.
3. **Create Admin User:** Set username, password, and email.
4. **Instance URL:** Leave as `http://localhost:8080/`.

---

## Step 3: Configure Tools (JDK + Maven)

Go to: **Manage Jenkins → Tools**

### Add JDK
| Setting | Value |
|:---|:---|
| Name | `jdk21` |
| JAVA_HOME | Your local JDK path, e.g. `C:\Program Files\Eclipse Adoptium\jdk-21.0.x.x-hotspot` |

*(Or tick "Install automatically" and choose Adoptium Temurin 21)*

### Add Maven
| Setting | Value |
|:---|:---|
| Name | `maven3` |
| Install automatically | ✅ Checked |
| Version | `3.9.x` (latest 3.x) |

---

## Step 4: Add Docker Hub Credential

Go to: **Manage Jenkins → Credentials → System → Global credentials → Add Credentials**

| Field | Value |
|:---|:---|
| Kind | `Username with password` |
| ID | `docker-hub-creds` *(must match Jenkinsfile exactly)* |
| Username | Your Docker Hub username (e.g. `maddy19032006`) |
| Password | Your Docker Hub password or Access Token |
| Description | `Docker Hub login for placement-portal` |

> **Getting a Docker Hub token:** Log into hub.docker.com → Account Settings → Security → New Access Token (scope: Read/Write). Use this instead of your real password.

---

## Step 5: Update Your Docker Hub Username in Jenkinsfile

Open [`Jenkinsfile`](./Jenkinsfile) and change line 18 to your actual Docker Hub username:

```groovy
// BEFORE:
IMAGE_NAME = "maddy19032006/placement-portal"

// AFTER (replace with your username):
IMAGE_NAME = "YOUR_DOCKERHUB_USERNAME/placement-portal"
```

Commit and push this change to GitHub:
```bash
git add Jenkinsfile
git commit -m "config: set Docker Hub username in Jenkinsfile"
git push origin develop
```

---

## Step 6: Create the Jenkins Pipeline Job

1. On the Jenkins dashboard, click **New Item**.
2. Enter name: `PlacementPortal-Pipeline`.
3. Select **Pipeline** → click **OK**.
4. In the **Pipeline** section:
   - Definition: `Pipeline script from SCM`
   - SCM: `Git`
   - Repository URL: `https://github.com/Maddy19032006/CollegePlacementPortal.git`
   - Branch Specifier: `*/develop` *(or `*/main` for production)*
   - Script Path: `Jenkinsfile` *(already at repo root)*
5. Click **Save**.

---

## Step 7: Set Up GitHub Webhook (Auto-Trigger on Push)

> This makes Jenkins automatically start a build every time you push code to GitHub — no manual triggering needed.

### In Jenkins:
1. Go to your pipeline job → **Configure** → **Build Triggers**.
2. Check ✅ **"GitHub hook trigger for GITScm polling"**.
3. Click **Save**.

### In GitHub:
1. Go to your repo: **Settings → Webhooks → Add webhook**.
2. Fill in:
   | Field | Value |
   |:---|:---|
   | Payload URL | `http://YOUR_PC_IP:8080/github-webhook/` |
   | Content type | `application/json` |
   | Which events | `Just the push event` |
3. Click **Add webhook**.

> **Finding your IP:** Run `ipconfig` in CMD and use your IPv4 address (e.g. `192.168.1.x`). Your PC and phone must be on the same network, OR use a tunnel like `ngrok http 8080` to get a public URL.

---

## Step 8: Run Your First Build

### Trigger Manually (First Time):
1. Open the `PlacementPortal-Pipeline` job.
2. Click **Build Now** on the left sidebar.
3. Watch the pipeline in real-time: click the **#1** build → **Console Output**.

### What Jenkins Does (All 8 Stages):
```
✅ Stage 1: Checkout from GitHub   — Clones your repo from GitHub
✅ Stage 2: Build & Compile        — mvn clean compile
✅ Stage 3: Tests + JaCoCo        — 42/42 tests pass; coverage report generated
✅ Stage 4: Package JAR            — target/placement-portal.jar archived
✅ Stage 5: Docker Build Image     — Builds production image using your Dockerfile
✅ Stage 6: Docker Push to Hub     — Pushes to hub.docker.com/r/YOU/placement-portal
✅ Stage 7: Deploy with Compose    — Pulls new image, restarts app + MySQL containers
✅ Stage 8: Smoke Test             — Hits http://localhost:8081/login.html; confirms 200 OK
```

---

## Step 9: Verify Everything is Working

### Check the running containers:
```bash
docker ps
```
You should see:
```
CONTAINER ID   IMAGE                             STATUS          PORTS
xxxx           maddy19032006/placement-portal    Up (healthy)    0.0.0.0:8081->8081/tcp
xxxx           mysql:8.0                         Up (healthy)    3306/tcp
```

### Open the live application:
```
http://localhost:8081
```

Login credentials:
| Role | Username | Password |
|:---|:---|:---|
| **Admin** | `admin` | `Admin@123` |
| **Student** | `arun.kumar` | `Student@123` |
| **Officer** | `sharma.tnp` | `Officer@123` |

### Check your image on Docker Hub:
```
https://hub.docker.com/r/YOUR_USERNAME/placement-portal
```
You should see the image tagged `latest` and `#BUILD_NUMBER`.

---

## Step 10: The Full Developer Workflow (After Setup)

From now on, the entire workflow is:

```bash
# 1. Make any code change
# 2. Commit and push to GitHub
git add .
git commit -m "feat: your change description"
git push origin develop

# 3. Jenkins detects the push via webhook → pipeline triggers automatically
# 4. Within ~3-4 minutes, the new image is on Docker Hub and the local app is restarted
# 5. Open http://localhost:8081 to see your changes live
```

---

## Troubleshooting Quick Reference

| Problem | Fix |
|:---|:---|
| `docker-hub-creds not found` | Re-check the credential ID is exactly `docker-hub-creds` in Jenkins |
| `docker: command not found` in pipeline | Make sure Docker Desktop is running; restart Jenkins service after Docker is up |
| Smoke test fails | Increase the retry loop count in Jenkinsfile (change `30` to `40`) |
| Webhook not triggering | Use `ngrok http 8080` for a public URL and update the webhook payload URL |
| Port 8080 conflict | Change Jenkins port: edit `httpPort` in `jenkins.xml` service config |
| `jdk21` tool not found | Go to Manage Jenkins → Tools → Add JDK 21 with name `jdk21` |
