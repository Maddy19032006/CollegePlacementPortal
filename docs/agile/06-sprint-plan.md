# 06 · Sprint Plan

Cadence: 5 sprints × 2 weeks. Team velocity: ~18–20 points/sprint.

## Sprint 1 — Foundation & Authentication (18 pts)
**Goal:** a running skeleton where users can register and log in securely.
- US-001 Project foundation & database schema (3)
- US-002 Registration with validation (5)
- US-003 Login, logout, role-based access (5)
- US-004 Demo data seeding (3)
- *Ceremonies:* backlog grooming before sprint; review = live register/login demo; retrospective actions captured in the template.
- **Demo:** fresh DB → register → login as student/officer → 403 checks.

## Sprint 2 — Student Profile & Preparation (16 pts)
**Goal:** a student can build a profile and see a readiness score.
- US-005 Profile create/edit (5)
- US-006 Skills management (3)
- US-007 Preparation scores with validation (3)
- US-008 Readiness score & visualization (5)
- **Demo:** save profile → enter scores → dashboard shows 0–100 readiness bar.

## Sprint 3 — Company Management & Eligibility (16 pts)
**Goal:** officers publish companies; students see where they qualify.
- US-009 Company CRUD (5)
- US-010 Search & filter (3)
- US-011 Company details (3)
- US-012 Eligibility checking (5)
- **Demo:** officer creates a company → student searches → eligibility shows pass/fail reasons.

## Sprint 4 — Applications & Dashboards (19 pts)
**Goal:** the full apply → track → decide loop works.
- US-013 Apply (5)
- US-014 Duplicate & deadline prevention (3)
- US-015 Student status tracking (3)
- US-016 Officer status updates (5)
- US-017 Student dashboard (5)
- US-018 Officer dashboard (3)
- US-019 Admin user management (3)
- *Note:* carried ~2 points of polish into Sprint 5.
- **Demo:** student applies → officer shortlists → student sees badge; duplicate/deadline rejections shown.

## Sprint 5 — Testing, Docker, Jenkins & SonarQube (24 pts)
**Goal:** production-grade engineering practices.
- US-020 Automated test suite (8)
- US-021 Docker & Compose (5)
- US-022 Jenkins pipeline (8)
- US-023 SonarQube (3)
- **Demo:** `mvn test` green → `docker compose up --build` → Jenkins pipeline end-to-end → SonarQube report.

## Sprint review & retrospective cadence
Every sprint ends with a review (live demo against acceptance criteria) and a retrospective using [10-retrospective-template.md](10-retrospective-template.md).
