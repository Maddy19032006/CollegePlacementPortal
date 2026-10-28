# 📘 Comprehensive Lab Experiments Manual & Viva Guide
**Course:** ISWE406P · Agile Development Process and DevOps Lab  
**Theory Link:** ISWE406L · Agile Development Process and DevOps  
**Application:** College Placement Portal  

---

## Quick Navigation to Experiments
- [Experiment 1: Agile Tools & Techniques](#experiment-1-introducing-agile-tools-and-techniques)
- [Experiment 2: Agile Project Management Tools](#experiment-2-agile-project-management-tools)
- [Experiment 3: AWS Services & EC2 Setup](#experiment-3-aws-service)
- [Experiment 4: EC2 Deep Dive & Linux Systemd Services](#experiment-4-ec2---elastic-compute-cloud)
- [Experiment 5: Git Version Control & Methodology](#experiment-5-git-version-control-and-branching)
- [Experiment 6: Docker Containerization & Kubernetes Orchestration](#experiment-6-dockers-and-kubernetes)
- [Experiment 7: Continuous Integration (Jenkins) & Continuous Monitoring (ELK)](#experiment-7-continuous-integration-with-jenkins-and-continuous-monitoring-using-elk)
- [Experiment 8: Data Virtualization & Vagrant Automation](#experiment-8-data-virtualization)

---

### Experiment 1: Introducing Agile Tools and Techniques
* **Syllabus Focus:** JIRA Agile / ClickUp / GitHub Issues, Epics, User Stories, Story Points, Sprint planning, Issue tracking, Pull Request links.
* **Implemented in Project:**
  - **Epics:** 6 defined Epics (E1 to E6) in [`docs/agile/03-epics.md`](file:///docs/agile/03-epics.md).
  - **Product Backlog & Stories:** 23 user stories with points (3 to 8 points) in [`docs/agile/04-product-backlog.md`](file:///docs/agile/04-product-backlog.md) and [`docs/agile/05-user-stories.md`](file:///docs/agile/05-user-stories.md).
  - **GitHub Templates:** Issue templates created under [`.github/ISSUE_TEMPLATE/`](file:///.github/ISSUE_TEMPLATE/).
  - **Pull Request Linking:** Pull request template in [`.github/pull_request_template.md`](file:///.github/pull_request_template.md) containing `Closes #<issue>` syntax for automated GitHub issue closing upon merge.
* **Viva Q&A:**
  - *Q: What is the purpose of Gherkin syntax in acceptance criteria?*  
    *A: It defines unambiguous Given-When-Then testable conditions, bridging business requirements and automated tests.*

---

### Experiment 2: Agile Project Management Tools
* **Syllabus Focus:** Backlog prioritization, Sprint cadence, Burn-down/velocity, Lean metrics, Definition of Done, Retrospectives.
* **Implemented in Project:**
  - **Sprint Plan:** 5 Sprints × 2 weeks cadence documented in [`docs/agile/06-sprint-plan.md`](file:///docs/agile/06-sprint-plan.md).
  - **Definition of Done (DoD):** 4-tier DoD (Code, Quality, Integration, Product) in [`docs/agile/08-definition-of-done.md`](file:///docs/agile/08-definition-of-done.md).
  - **Risk Register:** 10 prioritized risks with score and mitigation in [`docs/agile/09-risk-register.md`](file:///docs/agile/09-risk-register.md).
  - **Sprint Retrospective:** Completed Sprint 1 retrospective sample and blank template in [`docs/agile/10-retrospective-template.md`](file:///docs/agile/10-retrospective-template.md).

---

### Experiment 3: AWS Service
* **Syllabus Focus:** AWS Account, EC2 Instance (Amazon Linux & Ubuntu), Key pairs (public/private key), PuTTY / Git Bash connections, Security Group filtering, Password-based access.
* **Implemented in Project:**
  - Full instructions in [`deployment/aws-ec2-deployment-guide.md`](file:///deployment/aws-ec2-deployment-guide.md).
  - EC2 automated bootstrap script in [`deployment/ec2-setup.sh`](file:///deployment/ec2-setup.sh).
  - Password access authentication setup via `/etc/ssh/sshd_config`.

---

### Experiment 4: EC2 - Elastic Compute Cloud
* **Syllabus Focus:** Termination Protection, Shutdown behavior, EBS Snapshots, Custom AMI creation, Linux Service Manager (`systemctl` and `journalctl`).
* **Implemented in Project:**
  - **Systemd Unit File:** [`deployment/placement-portal.service`](file:///deployment/placement-portal.service) configured for auto-restart on failure.
  - **Commands for Lab Demonstration:**
    ```bash
    sudo systemctl daemon-reload
    sudo systemctl enable placement-portal
    sudo systemctl start placement-portal
    sudo systemctl status placement-portal
    sudo journalctl -u placement-portal -f
    ```

---

### Experiment 5: GIT Version Control and Branching
* **Syllabus Focus:** Git architecture, working with remote repositories, branching, merging, merge conflicts, Gitflow methodology.
* **Implemented in Project:**
  - **Branching Architecture:** `main` (production), `develop` (integration), `feature/*` (epics).
  - Documentation in [`docs/git-workflow.md`](file:///docs/git-workflow.md).
  - Git repository pre-initialized with `main` and `develop` active branches.
  - Standard Conventional Commits: `feat`, `fix`, `test`, `ci`, `docs`.

---

### Experiment 6: Dockers and Kubernetes
* **Syllabus Focus:** Dockerfile, Docker Compose, Docker logs & volumes, Minikube, Namespaces, Pods, Deployments, Services, PVC.
* **Implemented in Project:**
  - **Docker:** Multi-stage [`Dockerfile`](file:///Dockerfile) (Temurin 25 Alpine) + [`docker-compose.yml`](file:///docker-compose.yml) (App + MySQL 8 + health checks).
  - **Kubernetes Manifests:** Complete [`k8s/`](file:///k8s/) suite:
    - `00-namespace.yaml`: Isolated `placement-portal` namespace.
    - `01-secrets.yaml`: Secrets management.
    - `02-mysql-pvc.yaml`: Persistent volume claim for database.
    - `03-mysql.yaml`: MySQL deployment & ClusterIP service.
    - `04-app-configmap.yaml`: App configuration.
    - `05-app.yaml`: 2-replica application deployment & NodePort service (Port 30081).
  - **Execution Guide:** [`k8s/README.md`](file:///k8s/README.md).

---

### Experiment 7: Continuous Integration with Jenkins and Continuous Monitoring using ELK
* **Syllabus Focus:** CI/CD pipelines, Jenkins Master-Slave, SonarQube, ELK Stack (Elasticsearch, Logstash, Kibana), log parsing & dashboards.
* **Implemented in Project:**
  - **CI/CD:**
    - [`Jenkinsfile`](file:///Jenkinsfile): 8 stages (Checkout $\rightarrow$ Build $\rightarrow$ Test $\rightarrow$ SonarQube $\rightarrow$ Package $\rightarrow$ Docker $\rightarrow$ Deploy $\rightarrow$ Smoke Test).
    - [`.github/workflows/ci.yml`](file:///.github/workflows/ci.yml) & [`release.yml`](file:///.github/workflows/release.yml).
    - [`azure-pipelines.yml`](file:///azure-pipelines.yml) (Azure DevOps CI/CD).
  - **Continuous Monitoring (ELK):**
    - [`docker-compose-elk.yml`](file:///docker-compose-elk.yml): Elasticsearch (9200), Logstash (5000), Kibana (5601).
    - [`elk/logstash/pipeline/logstash.conf`](file:///elk/logstash/pipeline/logstash.conf): Grok filter pipeline.
    - [`src/main/resources/logback-spring.xml`](file:///src/main/resources/logback-spring.xml): Structured rolling log output.
    - [`elk/README.md`](file:///elk/README.md): Architecture & dashboard configuration guide.

---

### Experiment 8: Data Virtualization
* **Syllabus Focus:** Hypervisor Type 1 vs Type 2, Full vs Para-virtualization, VM image structures, Vagrant setup, basic commands, Vagrantfile initialization, automated VM provisioning.
* **Implemented in Project:**
  - **Vagrantfile:** [`Vagrantfile`](file:///Vagrantfile) targeting Ubuntu 22.04 LTS (`ubuntu/jammy64`) with 2 vCPUs, 2048MB RAM, port forward 8081, and shell provisioning script installing Java 21, MySQL, Maven, and auto-running the app.
  - **Documentation:** [`docs/data-virtualization-vagrant.md`](file:///docs/data-virtualization-vagrant.md).
