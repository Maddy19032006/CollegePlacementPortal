# 05 · User Stories

## US-001 — Project foundation & database schema
**As a** development team, **I want** a Maven/Spring Boot skeleton with a clean package structure and a MySQL schema, **so that** all features build on a shared, testable foundation.
- **Priority:** P0 · **Points:** 3 · **Sprint:** 1
- **Acceptance criteria**
  - `mvn clean compile` succeeds; layered packages (controller/service/repository/entity/dto/security/exception/config) exist
  - Entities User, StudentProfile, PreparationScore, Company, Application, Skill, StudentSkill map with PK/FK constraints
  - App starts against an empty database and creates tables automatically

## US-002 — User registration with validation
**As a** student, **I want** to create an account with a validated username, email and strong password, **so that** my identity and data are protected.
- **Priority:** P0 · **Points:** 5 · **Sprint:** 1
- **Acceptance criteria**
  - Password ≥ 8 chars with upper, lower, digit, special char; weak passwords rejected with a clear message
  - Duplicate username/email rejected (400)
  - Password stored only as a BCrypt hash — never plain text
  - Role is STUDENT or PLACEMENT_OFFICER (ADMIN is seeded only)

## US-003 — Login, logout & role-based access
**As a** registered user, **I want** to log in and reach only my role's pages, **so that** students, officers and admins see what they should.
- **Priority:** P0 · **Points:** 5 · **Sprint:** 1
- **Acceptance criteria**
  - Valid credentials start a session; invalid ones return 401 with a friendly message
  - `/api/students/**` requires STUDENT; `/api/officer/**` requires OFFICER/ADMIN; `/api/admin/**` requires ADMIN
  - Students get 403 on officer/admin endpoints (tested)
  - Logout ends the session

## US-004 — Demo data seeding
**As a** developer/presenter, **I want** demo users, companies and applications created on first startup, **so that** the app is instantly demonstrable.
- **Priority:** P1 · **Points:** 3 · **Sprint:** 1
- **Acceptance criteria**
  - Seeding runs only when the database is empty
  - Demo passwords come from environment variables
  - README documents the demo credentials

## US-005 — Create & edit placement profile
**As a** student, **I want** to save my academic details, certifications, projects and resume summary, **so that** eligibility and officers can evaluate me.
- **Priority:** P0 · **Points:** 5 · **Sprint:** 2
- **Acceptance criteria**
  - Fields: registration no. (unique), phone, department, program, graduation year, CGPA, certifications, projects, resume summary
  - CGPA must be 0–10; required fields validated server- and client-side
  - First save creates the profile; later saves update it

## US-006 — Manage skills
**As a** student, **I want** to list my skills, **so that** skill-based eligibility can be checked.
- **Priority:** P0 · **Points:** 3 · **Sprint:** 2
- **Acceptance criteria**
  - Skills entered as a comma-separated list on the profile
  - Stored in skills + student_skills tables (no duplicates per student)
  - Returned by the profile API

## US-007 — Enter preparation scores with validation
**As a** student, **I want** to record aptitude, coding, technical, projects and communication scores, **so that** my readiness can be measured.
- **Priority:** P0 · **Points:** 3 · **Sprint:** 2
- **Acceptance criteria**
  - Each score must be an integer 0–100; anything else returns 400 with a field error
  - Scores persist and round-trip through the API

## US-008 — Placement readiness score & visualization
**As a** student, **I want** a weighted readiness score, **so that** I know how placement-ready I am.
- **Priority:** P0 · **Points:** 5 · **Sprint:** 2
- **Acceptance criteria**
  - Score = aptitude×0.20 + coding×0.30 + technical×0.20 + projects×0.20 + communication×0.10
  - Correctly verified by unit tests (e.g. 80/70/90/60/50 → 72.0)
  - Dashboard shows overall score plus per-component progress bars

## US-009 — Officer manages companies
**As a** placement officer, **I want** to create, edit and delete companies with role, package, eligibility rules and deadlines, **so that** students see accurate openings.
- **Priority:** P0 · **Points:** 5 · **Sprint:** 3
- **Acceptance criteria**
  - Only OFFICER/ADMIN can write companies; students get 403 (tested)
  - minCGPA 0–10; deadline must be today or later; delete blocked when applications exist
  - Changes appear immediately in the company list

## US-010 — Company search & filtering
**As a** student, **I want** to search companies by name and filter by department, **so that** I can find relevant openings fast.
- **Priority:** P0 · **Points:** 3 · **Sprint:** 3
- **Acceptance criteria**
  - `GET /api/companies?name=&department=` supports both, combined
  - Results sorted by deadline; empty results show a friendly message

## US-011 — Company details
**As a** student, **I want** full details of a company, **so that** I can decide whether to apply.
- **Priority:** P0 · **Points:** 3 · **Sprint:** 3
- **Acceptance criteria**
  - Details include role, package, min CGPA, departments, skills, min graduation year, deadline, description

## US-012 — Eligibility checking
**As a** student, **I want** the system to check my eligibility for a company, **so that** I only apply where I qualify.
- **Priority:** P0 · **Points:** 5 · **Sprint:** 3
- **Acceptance criteria**
  - Checks CGPA, graduation year, department, required skills
  - Returns eligible true/false with a list of failed criteria
  - Boundary case: CGPA exactly at the minimum is eligible (tested)

## US-013 — Apply to a company
**As an** eligible student, **I want** to apply to a company, **so that** I can be considered for the role.
- **Priority:** P0 · **Points:** 5 · **Sprint:** 4
- **Acceptance criteria**
  - Application starts in APPLIED status
  - Requires a saved profile; eligibility re-validated server-side at apply time
  - Student sees the application in "My Applications"

## US-014 — Duplicate & deadline prevention
**As a** system, **I want** to block duplicate applications and applications after the deadline, **so that** data stays clean.
- **Priority:** P0 · **Points:** 3 · **Sprint:** 4
- **Acceptance criteria**
  - Second application to the same company → 400 "already applied" (tested)
  - Application after the deadline → 400 "deadline ... has passed" (tested)

## US-015 — Application status tracking (student)
**As a** student, **I want** to see each application's current status, **so that** I know where I stand in each process.
- **Priority:** P0 · **Points:** 3 · **Sprint:** 4
- **Acceptance criteria**
  - Status shown as a colored badge; applied date visible
  - Cancel allowed only while status is APPLIED and only for own applications

## US-016 — Officer updates application status
**As a** placement officer, **I want** to move applications through SHORTLISTED → ONLINE_TEST → TECHNICAL_INTERVIEW → HR_INTERVIEW → SELECTED (or REJECTED), **so that** students get accurate progress.
- **Priority:** P0 · **Points:** 5 · **Sprint:** 4
- **Acceptance criteria**
  - Invalid transitions rejected (e.g. APPLIED → SELECTED) with 400 (tested)
  - Only OFFICER/ADMIN can update statuses

## US-017 — Student dashboard
**As a** student, **I want** a dashboard with my readiness score, application counts, recent statuses and upcoming deadlines, **so that** I see everything at a glance.
- **Priority:** P0 · **Points:** 5 · **Sprint:** 4
- **Acceptance criteria**
  - Shows readiness score, total applications, selected count
  - Lists recent applications and next 5 upcoming deadlines

## US-018 — Officer dashboard & statistics
**As a** placement officer, **I want** counts of students, companies, applications, selections and rejections, **so that** I can monitor the placement season.
- **Priority:** P0 · **Points:** 3 · **Sprint:** 4
- **Acceptance criteria**
  - Dashboard shows all five counters plus recent applications

## US-019 — Admin user management
**As an** admin, **I want** to list users by role and enable/disable accounts, **so that** I can control access.
- **Priority:** P1 · **Points:** 3 · **Sprint:** 4
- **Acceptance criteria**
  - Filter by role works; disabled users cannot log in
  - Admin accounts cannot be disabled; admin sees system statistics/reports

## US-020 — Automated test suite
**As a** team, **we want** automated unit and integration tests covering business rules and security, **so that** CI catches regressions.
- **Priority:** P0 · **Points:** 8 · **Sprint:** 5
- **Acceptance criteria**
  - Tests cover: readiness formula, eligibility, duplicate/deadline/ineligible applications, status transitions, company validation, password hashing, role restrictions, full student journey
  - `mvn test` passes locally and must pass in Jenkins

## US-021 — Docker & Docker Compose
**As a** devops engineer, **I want** the app and MySQL containerized, **so that** the system runs anywhere with one command.
- **Priority:** P0 · **Points:** 5 · **Sprint:** 5
- **Acceptance criteria**
  - Multi-stage Dockerfile (Maven build → JRE 25 runtime, non-root user)
  - `docker compose up --build` starts app + MySQL with health checks
  - No secrets in images; all config via environment variables

## US-022 — Jenkins CI/CD pipeline
**As a** devops engineer, **I want** a Jenkins pipeline that builds, tests, scans, packages, containers and deploys, **so that** every commit is verified automatically.
- **Priority:** P0 · **Points:** 8 · **Sprint:** 5
- **Acceptance criteria**
  - Stages: Checkout → Build → Tests → SonarQube → Package → Docker Build/Push → Deploy → Smoke Test
  - Failing tests fail the pipeline; JUnit results published
  - Credentials only via Jenkins credential store

## US-023 — SonarQube code quality
**As a** team, **we want** static analysis on every pipeline run, **so that** code smells and vulnerabilities surface early.
- **Priority:** P1 · **Points:** 3 · **Sprint:** 5
- **Acceptance criteria**
  - Maven Sonar plugin configured with project key
  - Token supplied via environment/credentials, never committed
