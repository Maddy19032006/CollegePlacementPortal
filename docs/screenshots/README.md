# Screenshot Capture Guide & Viva Evidence Checklist
**Course:** ISWE406L/P — Agile Development Process and DevOps  
**Repository:** `CollegePlacementPortal` (PlacePilot)

This document provides a step-by-step checklist of exact commands to execute and screens to capture for all **8 Lab Experiments** required for the lab record and viva evaluation.

---

## Quick Reference: Running the System for Screenshots

To take web UI screenshots, start the application in one command:
```powershell
# Windows
.\run-app-local.bat
# Option 1: Instant In-Memory H2 Mode (Port 8081, zero setup)
```
or via terminal:
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=h2
```
Portal URL: **http://localhost:8081**  
Credentials:
- **Admin:** `admin` / `Admin@123`
- **Student:** `arun.kumar` / `Student@123`
- **Officer:** `sharma.tnp` / `Officer@123`

---

## Lab Experiment Checklist & Screenshot Mapping

### Lab 1: Git Version Control & Branching Strategy
- **Curriculum Objective:** Demonstrate version control, GitFlow branching model (`main`, `develop`, feature branches), and non-linear commit history.
- **Terminal Command:**
  ```bash
  git log --graph --oneline --decorate --all -n 20
  git branch -a
  ```
- **Screenshot Target (`docs/screenshots/lab1-git-branching.png`):**
  - Terminal showing color-coded Git graph with `main`, `develop`, and merged feature commits.
  - Highlights branch protection and structured commit messages (`feat:`, `fix:`, `test:`, `docs:`).

---

### Lab 2: Agile ALM & Sprint Backlog Management
- **Curriculum Objective:** Demonstrate User Story Cards (INVEST criteria), Sprint Planning, Team Velocity, and Sprint Burndown.
- **Web UI URL:** `http://localhost:8081/admin-reports.html` (Logged in as `admin`)
- **Screenshot Target (`docs/screenshots/lab2-agile-burndown.png`):**
  - The **Agile ALM & DevOps Telemetry** section on the Admin Reports page.
  - Highlights the live **Sprint 5 Burndown Chart** (SVG line graph: Ideal vs Actual Burn), the **Team Velocity across Sprints 1–5**, and the INVEST compliance badge.

---

### Lab 3: Build Automation & Dependency Management with Apache Maven
- **Curriculum Objective:** Demonstrate declarative build lifecycle, dependency scoping, and packaging.
- **Terminal Command:**
  ```bash
  mvn clean compile
  mvn package -DskipTests=false
  ```
- **Screenshot Target (`docs/screenshots/lab3-maven-build.png`):**
  - Terminal showing `[INFO] BUILD SUCCESS`, compiler version, dependency resolution, and created artifact `target/placement-portal-1.2.0.jar`.

---

### Lab 4: Automated Testing & Test-Driven Development (TDD)
- **Curriculum Objective:** Demonstrate automated unit and integration tests covering business rules and security policies using JUnit 5 and Spring Boot Test.
- **Terminal Command:**
  ```bash
  mvn test
  ```
- **Screenshot Target (`docs/screenshots/lab4-surefire-tests.png`):**
  - Terminal output showing all 6 test suites running:
    - `AuthFlowIntegrationTest` (14 tests)
    - `StudentJourneyIntegrationTest` (4 tests)
    - `ApplicationServiceTest` (6 tests)
    - `CompanyServiceTest` (4 tests)
    - `EligibilityServiceTest` (8 tests)
    - `ReadinessScoreCalculationTest` (6 tests)
  - Final summary: `Tests run: 42, Failures: 0, Errors: 0, Skipped: 0` with `[INFO] BUILD SUCCESS`.

---

### Lab 5: Code Coverage & Static Analysis with JaCoCo
- **Curriculum Objective:** Demonstrate bytecode-level test coverage metrics (Instruction, Line, Branch, Complexity) and quality gate analysis.
- **File to Open:** `docs/metrics-report/jacoco/index.html` in any browser.
- **Screenshot Target (`docs/screenshots/lab5-jacoco-coverage.png`):**
  - JaCoCo HTML report dashboard showing:
    - **68.9% Instruction Coverage** (3,070 / 4,456 covered)
    - **72.2% Line Coverage** (725 / 1,004 lines covered)
    - **39.7% Branch Coverage** (85 / 214 branches covered)
    - Clean package breakdown across `com.college.placement.*`.

---

### Lab 6: Continuous Integration with Jenkins Pipeline
- **Curriculum Objective:** Demonstrate an 8-stage automated CI/CD pipeline triggered on code commit.
- **File / View:** `Jenkinsfile` in repository root or Jenkins Web UI (`http://localhost:8080/job/placement-portal`).
- **Screenshot Target (`docs/screenshots/lab6-jenkins-pipeline.png`):**
  - Jenkins Pipeline Stage View showing green stages:
    1. Checkout SCM
    2. Build & Compile
    3. Unit & Integration Tests (JUnit)
    4. Code Quality & Coverage (JaCoCo / SonarQube)
    5. Package Executable JAR
    6. Build Docker Image
    7. Deploy to Staging / Local
    8. Post-Deploy Smoke Verification

---

### Lab 7: Containerization with Docker & Docker Compose
- **Curriculum Objective:** Demonstrate multi-stage Docker builds and multi-container orchestration.
- **Terminal Commands:**
  ```bash
  docker compose up -d --build
  docker compose ps
  docker images | grep placement-portal
  ```
- **Screenshot Target (`docs/screenshots/lab7-docker-compose.png`):**
  - Output of `docker compose ps` showing `placement-portal-app` (healthy, port 8081->8080) and `placement-portal-mysql` (healthy, port 3306).
  - Terminal showing successful container launch and health check logs.

---

### Lab 8: Kubernetes Orchestration & Infrastructure Monitoring
- **Curriculum Objective:** Demonstrate declarative cluster manifests (Deployments, Services, NodePort) and observability infrastructure.
- **Terminal Commands:**
  ```bash
  kubectl get all -n placement-portal
  # Observability / ELK inspection
  cat docker-compose-elk.yml
  cat deployment/placement-portal.service
  ```
- **Screenshot Target (`docs/screenshots/lab8-k8s-orchestration.png`):**
  - Terminal showing active pods, replica sets, and ClusterIP / NodePort services under the `placement-portal` namespace.
  - Or display of `k8s/*.yaml` deployment manifests alongside the systemd service file.

---

## App UI Gallery Checklist (PlacePilot Pages)

| File Name | Screen Description | Recommended Role |
|:---|:---|:---|
| `ui-01-landing.png` | Landing page with hero illustration & metrics | Public (`/index.html`) |
| `ui-02-login.png` | Clean login form with role switcher | Public (`/login.html`) |
| `ui-03-student-dashboard.png` | Student readiness score (gauge + progress bars) | Student (`/student-dashboard.html`) |
| `ui-04-student-profile.png` | Student academic & skills management form | Student (`/student-profile.html`) |
| `ui-05-student-readiness.png` | Placement readiness component breakdown | Student (`/student-preparation.html`) |
| `ui-06-company-catalog.png` | Job opportunities catalog with search & filters | Student (`/companies.html`) |
| `ui-07-application-tracking.png` | Student application status tracking pipeline | Student (`/student-applications.html`) |
| `ui-08-officer-dashboard.png` | Officer statistics and recruitment pipeline | Officer (`/officer-dashboard.html`) |
| `ui-09-officer-job-offers.png` | Officer job posting and deadline management | Officer (`/officer-companies.html`) |
| `ui-10-officer-students.png` | Officer student directory with CGPA and skills | Officer (`/officer-students.html`) |
| `ui-11-admin-analytics.png` | Admin placement analytics + Agile ALM widget | Admin (`/admin-reports.html`) |
| `ui-12-admin-users.png` | Admin user account management & role controls | Admin (`/admin-users.html`) |
