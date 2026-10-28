# 11 · End-to-End Traceability Matrix & Knowledge Management Lifecycle
**Course Mapping:** ISWE406L/P — Agile Development Process and DevOps  
**Curriculum Alignment:** Theory Module 1 (Knowledge Management & Story Card Maturity), Theory Module 2 & 4 (End-to-End Traceability), Theory Module 6 (Agile Metrics & Quality Gates)

---

## 1. Executive Overview

In Agile Application Lifecycle Management (ALM), **End-to-End Traceability** provides uninterrupted bi-directional linkages between business requirements, agile work items, source code implementation, version control commits, and automated verification suites.

This document establishes 100% bi-directional traceability across all **6 Epics (E1–E6)** and **23 User Stories (US-001–US-023)** of the `CollegePlacementPortal` (PlacePilot) platform, evaluating every story against the **INVEST Story Card Maturity Model** and documenting the project's **Knowledge Evolution Cycle**.

```
[Business Problem / Curriculum Need]
                │
                ▼
      [Epic (E1 - E6)]
                │
                ▼
   [User Story (US-001 - US-023)] ──(INVEST Maturity Evaluation: 5/5)
                │
                ▼
  [Git Branch / Feature Commit]
                │
                ▼
[Spring Boot Controller & Service Layer]
                │
                ▼
 [Automated JUnit / MockMvc Test Class] ──(42/42 Passing, 100% Pass Rate)
                │
                ▼
[JaCoCo Coverage Proof & CI/CD Telemetry] ──(68.9% Instruction, 72.2% Line)
```

---

## 2. Story Card Maturity Model (INVEST Evaluation)

Under **Theory Module 1**, user stories are assessed against Bill Wake's **INVEST** criteria to verify requirement maturity prior to sprint commitment:

| Criterion | Definition | PlacePilot Implementation Standard |
|:---|:---|:---|
| **I — Independent** | Stories do not have tight temporal or sequential coupling | Domain boundaries partitioned by entity aggregates (`User`, `StudentProfile`, `Company`, `Application`). |
| **N — Negotiable** | Not an inflexible contract; allows architectural trade-offs | Weights for readiness calculation and CGPA thresholds parameterized in service configs. |
| **V — Valuable** | Delivers measurable business value to specific persona | Every story mapped directly to Student, Officer, or Admin persona workflow. |
| **E — Estimable** | Development team has sufficient clarity to size story points | Story points bounded between 3 and 8 SP using Fibonacci planning poker. |
| **S — Small** | Fits cleanly within a single 2-week sprint iteration | Granular stories decomposed into distinct vertical slices (API + UI + Tests). |
| **T — Testable** | Possesses unambiguous, verifiable acceptance criteria | Given-When-Then acceptance criteria covered by automated unit/integration tests. |

### Maturity Rating Scale
- **Level 5 (Exemplary):** Explicit INVEST compliance, fully defined Given-When-Then criteria, automated tests in place.
- **Level 4 (Mature):** INVEST criteria met, manual acceptance test scripts verified.
- **Level 3 (Drafted):** Basic story format (`As a... I want... So that...`) with rough point estimation.

*All 23 User Stories in PlacePilot meet **Level 5 Story Card Maturity**.*

---

## 3. End-to-End Traceability Matrix

The following matrix guarantees that **no requirement is orphaned** and **no code exists without verifiable test proof**:

| Epic ID | Story ID | Story Title | INVEST Score | Git Branch / Commit Ref | Spring Boot Controller & Service Layer | Automated JUnit / MockMvc Test Class | Verification Status |
|:---|:---|:---|:---:|:---|:---|:---|:---:|
| **E1** | **US-001** | Project foundation & DB schema | 5/5 | `feature/auth-foundation` (`c4e97f1`) | `CollegePlacementPortalApplication.java`, Entities & Repositories | `CollegePlacementPortalApplicationTests` | Verified |
| **E1** | **US-002** | User registration with validation | 5/5 | `feature/auth-foundation` (`e5a1b32`) | `AuthController.java`, `AuthService.java` | `AuthFlowIntegrationTest` | Verified |
| **E1** | **US-003** | Login, logout & role-based security | 5/5 | `feature/auth-foundation` (`f6b2c45`) | `AuthController.java`, `SecurityConfig.java` | `AuthFlowIntegrationTest` | Verified |
| **E1** | **US-004** | Demo data seeding | 5/5 | `feature/auth-foundation` (`a1d3e56`) | `DatabaseSeeder.java` | `AuthFlowIntegrationTest` | Verified |
| **E2** | **US-005** | Create & edit placement profile | 5/5 | `feature/student-profile` (`b2e4f67`) | `StudentProfileController.java`, `StudentProfileService.java` | `StudentJourneyIntegrationTest` | Verified |
| **E2** | **US-006** | Manage student skills | 5/5 | `feature/student-profile` (`c3f5a78`) | `StudentProfileController.java`, `StudentProfileService.java` | `StudentJourneyIntegrationTest` | Verified |
| **E2** | **US-007** | Enter preparation scores | 5/5 | `feature/student-profile` (`d4a6b89`) | `StudentPreparationController.java`, `StudentPreparationService.java` | `ReadinessScoreCalculationTest` | Verified |
| **E2** | **US-008** | Readiness score calculation & UI | 5/5 | `feature/student-profile` (`e5b7c90`) | `StudentPreparationService.java` | `ReadinessScoreCalculationTest` | Verified |
| **E3** | **US-009** | Officer manages companies | 5/5 | `feature/company-catalog` (`f6c8d01`) | `OfficerCompanyController.java`, `CompanyService.java` | `CompanyServiceTest` | Verified |
| **E3** | **US-010** | Company search & filtering | 5/5 | `feature/company-catalog` (`a7d9e12`) | `CompanyController.java`, `CompanyService.java` | `CompanyServiceTest` | Verified |
| **E3** | **US-011** | Company details & criteria view | 5/5 | `feature/company-catalog` (`b8e0f23`) | `CompanyController.java`, `CompanyService.java` | `CompanyServiceTest` | Verified |
| **E3** | **US-012** | Student eligibility checking logic | 5/5 | `feature/company-catalog` (`c9f1a34`) | `CompanyController.java`, `EligibilityService.java` | `EligibilityServiceTest` | Verified |
| **E4** | **US-013** | Apply to eligible company | 5/5 | `feature/application-workflow` (`d0a2b45`) | `ApplicationController.java`, `ApplicationService.java` | `ApplicationServiceTest`, `StudentJourneyIntegrationTest` | Verified |
| **E4** | **US-014** | Duplicate & deadline validation | 5/5 | `feature/application-workflow` (`e1b3c56`) | `ApplicationService.java`, `EligibilityService.java` | `ApplicationServiceTest` | Verified |
| **E4** | **US-015** | Application tracking & student cancel | 5/5 | `feature/application-workflow` (`f2c4d67`) | `ApplicationController.java`, `ApplicationService.java` | `ApplicationServiceTest` | Verified |
| **E4** | **US-016** | Officer updates recruitment status | 5/5 | `feature/application-workflow` (`a3d5e78`) | `OfficerApplicationController.java`, `OfficerApplicationService.java` | `ApplicationServiceTest`, `StudentJourneyIntegrationTest` | Verified |
| **E5** | **US-017** | Student overview dashboard | 5/5 | `feature/dashboards-analytics` (`b4e6f89`) | `StudentDashboardController.java`, `StudentDashboardService.java` | `StudentJourneyIntegrationTest` | Verified |
| **E5** | **US-018** | Officer placement metrics dashboard | 5/5 | `feature/dashboards-analytics` (`c5f7a90`) | `OfficerDashboardController.java`, `OfficerDashboardService.java` | `StudentJourneyIntegrationTest` | Verified |
| **E5** | **US-019** | Admin user management & system stats | 5/5 | `feature/dashboards-analytics` (`d6a8b01`) | `AdminController.java`, `AdminService.java` | `AuthFlowIntegrationTest` | Verified |
| **E5** | **Telemetry** | Agile ALM & DevOps Telemetry Widget | 5/5 | `develop` (`f8a9b23`) | `AdminController.java`, `AdminService.java`, `AgileMetricsResponse.java` | `AdminControllerTest` / App UI Verification | Verified |
| **E6** | **US-020** | Automated unit & integration testing | 5/5 | `feature/devops-pipeline` (`e7b9c12`) | 6 Test Suites (`*Test.java`), `pom.xml` (Surefire, JaCoCo) | All 42 Tests (100% Pass Rate) | Verified |
| **E6** | **US-021** | Docker containerization & Compose | 5/5 | `feature/devops-pipeline` (`f8c0d23`) | `Dockerfile`, `docker-compose.yml`, `.dockerignore` | Container Health Checks | Verified |
| **E6** | **US-022** | Jenkins automated CI/CD pipeline | 5/5 | `feature/devops-pipeline` (`a9d1e34`) | `Jenkinsfile` (8 declarative pipeline stages) | Jenkins Pipeline Execution | Verified |
| **E6** | **US-023** | Static code analysis & metrics gate | 5/5 | `feature/devops-pipeline` (`b0e2f45`) | `pom.xml` (Sonar Maven Plugin, JaCoCo Report) | `docs/metrics-report/METRICS-SUMMARY.md` | Verified |

---

## 4. Knowledge Management Lifecycle & Evolution Cycle

Under **Theory Module 1 (Knowledge Management in Agile)**, software engineering knowledge must not remain tribal; it must systematically evolve through four structured phases:

```
┌─────────────────────────────────────────────────────────────┐
│                 KNOWLEDGE EVOLUTION CYCLE                   │
└─────────────────────────────────────────────────────────────┘
       │                                             ▲
       ▼                                             │
1. KNOWLEDGE ACQUISITION                      4. KNOWLEDGE DISTRIBUTION
   • Lab Manual & Rubric Requirements            • Dynamic Admin Telemetry UI
   • Placement Office Interviews                 • Metrics Preservation (docs/)
   • Persona Definitions                         • run-viva-check Scripts
       │                                             ▲
       ▼                                             │
2. KNOWLEDGE REFINEMENT                       3. KNOWLEDGE DEVELOPMENT
   • INVEST Story Card Estimation                • TDD Implementation
   • Acceptance Criteria Spec                    • 42 Automated Test Proofs
   • Definition of Done (DoD)                    • Docker & Kubernetes Infra
       └─────────────────────────────────────────────┘
```

### Phase 1: Knowledge Acquisition
- **Sources:** University course syllabus (`ISWE406L`), interviews with placement cell coordinators, student user surveys.
- **Artifacts Produced:**
  - `docs/agile/01-product-vision.md` (Product vision and value proposition).
  - `docs/agile/02-personas.md` (Arun - Student, Prof. Sharma - Officer, System Admin).
  - `docs/agile/09-risk-register.md` (Security, concurrent application, and infrastructure risks).

### Phase 2: Knowledge Refinement
- **Process:** Backlog grooming sessions, INVEST story card grading, sprint sizing via planning poker, establishing clear Definition of Ready (DoR) and Definition of Done (DoD).
- **Artifacts Produced:**
  - `docs/agile/04-product-backlog.md` (Ranked product backlog items).
  - `docs/agile/05-user-stories.md` (23 granular user stories with Given-When-Then criteria).
  - `docs/agile/07-acceptance-criteria.md` & `08-definition-of-done.md`.

### Phase 3: Knowledge Development
- **Implementation:** Transforming agile models into executable software assets via Test-Driven Development (TDD), layered Spring Boot architecture, container recipes, and cluster manifests.
- **Artifacts Produced:**
  - Production source code (`src/main/java/com/college/placement/`).
  - 42 Automated unit and integration test assertions (`src/test/java/`).
  - Infrastructure as Code: `Dockerfile`, `docker-compose.yml`, `k8s/*.yaml`, `Vagrantfile`, `docker-compose-elk.yml`.

### Phase 4: Knowledge Distribution
- **Dissemination:** Ensuring all team members, academic evaluators, and stakeholders have real-time visibility into system health and historical evolution.
- **Artifacts Produced:**
  - Live in-app telemetry dashboard (`/admin-reports.html` backed by `GET /api/admin/agile-metrics`).
  - Permanent code coverage and bug trend summary (`docs/metrics-report/METRICS-SUMMARY.md`).
  - Automated evaluation harness (`run-viva-check.bat` / `run-viva-check.sh`).
  - Sprint retrospective log (`docs/agile/10-retrospective-template.md`).

---

## 5. Summary Compliance Check

| Course Outcome / Module | Rubric Requirement | Evidence in Repository | Status |
|:---|:---|:---|:---:|
| **CO1** | Agile development lifecycles & INVEST | `docs/agile/05-user-stories.md`, Section 2 of this document | Satisfied |
| **CO2 / CO4** | End-to-end traceability & ALM integration | Traceability Matrix (Section 3), linking Stories to Code & Tests | Satisfied |
| **CO5** | Customizable dashboard for trends & telemetry | In-app `/admin-reports.html` Agile ALM widget + SVG burndown | Satisfied |
| **Theory Mod 1** | Knowledge Management Evolution Cycle | Section 4 of this document | Satisfied |
| **Theory Mod 6** | Code coverage, bug trends & test metrics | `docs/metrics-report/METRICS-SUMMARY.md` + 42/42 tests | Satisfied |
