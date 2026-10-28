# 🎓 College Placement Portal

A full-stack web application for college students to manage their placement preparation and job applications, with a complete CI/CD pipeline. Built as an Agile Development & DevOps course project.

---

## 1. Project Overview

The College Placement Portal lets students maintain a placement profile, including CGPA, skills, projects, internships and extracurricular achievements; track preparation across five scored areas; receive best-fit job recommendations; check eligibility before applying; and follow every application status. Placement officers manage companies and applications; admins manage users and view system-wide statistics.

## 2. Problem Statement

Placement activity in colleges is usually tracked in spreadsheets and notice boards. Students do not know which companies they are eligible for or where they stand in each company's process, and officers spend manual effort matching students to openings. This portal centralizes profiles, preparation, eligibility, applications and status tracking behind role-based access.

## 3. Objectives

- Provide a single portal for placement preparation and applications
- Compute an objective **placement readiness score** for every student
- Automate **eligibility checking** against company requirements
- Give officers tools to manage companies, applications and statuses
- Demonstrate **Agile practices**, **automated testing**, **Docker** and a **Jenkins CI/CD pipeline**

## 4. Features

**Student** — register/login, manage a complete profile (CGPA, department, skills, projects, internships, extracurriculars and resume), enter preparation scores (aptitude / coding / technical / projects / communication), view readiness score with visual breakdown, receive personalised open-job recommendations, search & filter companies, see exact eligibility criteria, apply, track status, cancel pending applications, and use a personal dashboard with upcoming deadlines.

**Placement Officer** — dashboard with statistics, create/edit/delete companies with eligibility rules and deadlines, view all applications, filter by status, update application status through the controlled flow, view students and per-company eligibility.

**Admin** — dashboard, user list with role filter, enable/disable accounts, system-wide reports (applications by status, students by department).

## 5. User Roles

| Role | Capabilities |
|---|---|
| `STUDENT` | Own profile, preparation, applications |
| `PLACEMENT_OFFICER` | Companies, applications, statuses, student views |
| `ADMIN` | User management, statistics, read access to companies/applications |

## 6. Technology Stack

- **Backend:** Java 25, Spring Boot 3.5, Spring Web, Spring Data JPA, Spring Security, Jakarta Validation, Maven
- **Database:** MySQL 8 (H2 in-memory for tests)
- **Frontend:** HTML5, CSS3, JavaScript (ES6), Bootstrap 5
- **Testing:** JUnit 5, Mockito, Spring Boot Test, MockMvc, Spring Security Test
- **DevOps:** Git/GitHub, Jenkins, Docker, Docker Compose, SonarQube (optional)

## 7. Architecture

Layered Spring Boot architecture: `controller → service → repository → entity`, with `dto` for API payloads, `security` for auth, `exception` for global error handling and `config` for seeding. Full diagrams (system architecture, component flow, CI/CD, ER diagram, workflow) are in [docs/architecture.md](docs/architecture.md).

## 8. Database Design

| Table | Purpose | Key relationships |
|---|---|---|
| `users` | Login accounts, role, BCrypt password hash | 1–1 `student_profiles` |
| `student_profiles` | Placement profile | FK → users; 1–1 `preparation_scores`; 1–N `student_skills` |
| `preparation_scores` | Five 0–100 scores per student | FK → student_profiles |
| `skills` | Skill catalog | 1–N `student_skills` |
| `student_skills` | Join table student↔skill | FKs → student_profiles, skills |
| `companies` | Recruiter + eligibility rules | 1–N applications |
| `applications` | Student↔company with status, unique (student, company) | FKs → student_profiles, companies |

ER diagram: [docs/architecture.md](docs/architecture.md).

## 9. API Overview

| Method & Path | Role | Description |
|---|---|---|
| `POST /api/auth/register` | public | Create an account (password rules enforced) |
| `POST /api/auth/login` | public | Login, establishes the session |
| `GET /api/auth/me` | any | Current user |
| `GET /api/students/dashboard` | STUDENT | Dashboard summary |
| `GET / PUT /api/students/profile` | STUDENT | Read / save own profile |
| `GET / PUT /api/students/preparation` | STUDENT | Read / save preparation scores |
| `GET /api/students/applications` | STUDENT | Own applications |
| `GET /api/students/recommended-jobs` | STUDENT | Open jobs the student is eligible for, ranked by package and skills |
| `GET /api/students/companies/{id}/eligibility` | STUDENT | Exact eligibility verdict before applying |
| `DELETE /api/students/applications/{id}` | STUDENT | Cancel own APPLIED application |
| `GET /api/companies` | any logged-in | List/search companies (`?name=&department=`) |
| `GET /api/companies/{id}` | any logged-in | Company details |
| `POST /api/companies` | OFFICER/ADMIN | Create company |
| `PUT /api/companies/{id}` | OFFICER/ADMIN | Update company |
| `DELETE /api/companies/{id}` | OFFICER/ADMIN | Delete (only with zero applications) |
| `POST /api/applications?companyId=` | STUDENT | Apply (eligibility + deadline enforced) |
| `GET /api/applications?status=` | OFFICER/ADMIN | All applications |
| `PUT /api/applications/{id}/status` | OFFICER/ADMIN | Update status (validated transition) |
| `GET /api/officer/dashboard` | OFFICER/ADMIN | Officer statistics |
| `GET /api/officer/students` | OFFICER/ADMIN | All student profiles |
| `GET /api/officer/companies/{id}/eligible-students` | OFFICER/ADMIN | Eligibility per student |
| `GET /api/admin/users?role=` | ADMIN | Users |
| `POST /api/admin/officers` | ADMIN | Provision a placement officer account |
| `PUT /api/admin/users/{id}/status` | ADMIN | Enable/disable user |
| `GET /api/admin/stats` | ADMIN | System statistics |

Errors return a consistent JSON body: `{ "timestamp", "status", "error", "message", "path", "fieldErrors"? }`.

## 10. Agile Methodology

The project was planned with a product vision, personas, epics, a prioritized backlog and 5 sprints. See [docs/agile/](docs/agile/01-product-vision.md) — including 20 user stories with acceptance criteria, priorities and story points, a sprint plan, definition of done, risk register and retrospective template.

## 11. Git Workflow

Branching model: `main` (releasable) ← `develop` (integration) ← `feature/*`.

```bash
git checkout develop
git checkout -b feature/authentication   # one branch per story/feature
# ... commit work ...
# open a Pull Request into develop; release: develop -> main
```

Suggested branches for this repo: `feature/authentication`, `feature/student-profile`, `feature/company-management`, `feature/application-tracking`, `feature/testing`, `feature/devops`. Details: [docs/git-workflow.md](docs/git-workflow.md).

## 12. CI/CD Pipeline

Two ready-to-use pipelines are included:

- **GitHub Actions CI** ([ci.yml](.github/workflows/ci.yml)) runs on pull requests and pushes to `develop` / `main`: clean build, unit and integration tests, JAR artifact upload and a production Docker build check.
- **GitHub Actions CD** ([release.yml](.github/workflows/release.yml)) publishes immutable SHA-tagged and `latest` images to GitHub Container Registry whenever `main` changes. It can deploy automatically when `DEPLOY_ENABLED=true` is configured as a GitHub repository variable.
- **Jenkins** ([Jenkinsfile](Jenkinsfile)) remains available for a Jenkins-based demonstration: **Checkout → Build → Tests → SonarQube → Package → Docker → Deploy → Smoke Test**.

### GitHub Actions deployment configuration

The delivery workflow is safe by default: it publishes the image but does not connect to a server. To enable the optional production deploy job, configure these repository values:

| Type | Name | Value |
|---|---|---|
| Variable | `DEPLOY_ENABLED` | `true` |
| Variable | `DEPLOY_USER` | SSH user on the deployment host |
| Variable | `DEPLOY_PATH` | Absolute path containing `docker-compose.yml` |
| Secret | `DEPLOY_HOST` | Hostname or IP address |
| Secret | `DEPLOY_SSH_KEY` | Private Ed25519 deployment key |

The deployment host must be authenticated to pull the package from GHCR and contain this repository's `docker-compose.yml`. The workflow supplies `APP_IMAGE=ghcr.io/<owner>/placement-portal:<commit SHA>` and performs `docker compose pull` followed by a no-build restart, enabling a repeatable rollback by choosing an earlier image tag.

## 13. Docker Setup

```bash
cp .env.example .env    # set DB_PASSWORD (and demo passwords if you like)
docker compose up --build
# app: http://localhost:8081
```

MySQL data persists in the `mysql-data` volume. Health checks gate app startup until MySQL is ready.

## 14. SonarQube Setup

1. Run SonarQube locally (e.g. `docker run -d -p 9000:9000 sonarqube:lts-community`)
2. Generate a token in SonarQube (My Account → Security)
3. Run: `mvn sonar:sonar -Dsonar.host.url=http://localhost:9000 -Dsonar.token=<YOUR_TOKEN>`

The token is passed via command line/environment — **never committed**. Jenkins reads it from the `sonar-token` credential. Details: [docs/setup-and-verification.md](docs/setup-and-verification.md).

## 15. Local Setup (without Docker)

**Prerequisites:** JDK 25+, Maven 3.8+, MySQL 8+

```bash
mysql -u root -p -e "CREATE DATABASE IF NOT EXISTS placement_portal;"
mvn spring-boot:run          # or: set DB_USERNAME/DB_PASSWORD env vars first
# open http://localhost:8081
```

Tables are created automatically (`ddl-auto=update`) and demo data is seeded on first run.

## 16. Environment Variables

| Variable | Default | Purpose |
|---|---|---|
| `DB_HOST` / `DB_PORT` / `DB_NAME` | `localhost / 3306 / placement_portal` | MySQL location |
| `DB_USERNAME` / `DB_PASSWORD` | `root / root` | MySQL credentials |
| `SERVER_PORT` | `8081` | App port |
| `JPA_DDL_AUTO` | `update` | Schema handling |
| `DEMO_PASSWORD_STUDENT` | `demoStudent@123` | Seeded student password |
| `DEMO_PASSWORD_OFFICER` | `demoOfficer@123` | Seeded officer password |
| `DEMO_PASSWORD_ADMIN` | `demoAdmin@123` | Seeded admin password |
| `SONAR_HOST_URL` / `SONAR_TOKEN` | — | SonarQube (optional) |

## 17. Test Execution

```bash
mvn test        # 36 tests: unit + MockMvc integration + security
mvn package     # build jar (tests run automatically)
```

Coverage includes the readiness formula, eligibility rules, application business rules (duplicate/deadline/ineligible/status transitions), company validation, registration/password hashing, role-based access and the full student journey.

## 18. Jenkins Setup

1. Install Jenkins LTS + plugins: Pipeline, Git, Docker, SonarQube Scanner, JUnit
2. Manage Jenkins → Tools: add JDK `jdk25` and Maven `maven3`
3. Add credentials: `sonar-token` (Secret Text), `docker-registry` (Username/Password, optional)
4. New Item → Pipeline → Pipeline script from SCM → point at this repository
5. The pipeline picks up the [Jenkinsfile](Jenkinsfile)

## 19. Screenshots

Placeholders — add screenshots here before submission:

`docs/screenshots/` (see [docs/screenshots/README.md](docs/screenshots/README.md))

## 20. Future Enhancements

- JWT authentication for a decoupled SPA frontend
- Email/notification service for status changes
- Resume file upload with storage
- Interview scheduling for officers
- Exportable placement reports (CSV/PDF)

## 21. Known Limitations

- Session-based auth (single instance; not suitable for horizontal scaling)
- Skills stored as a catalog + join table, but company requirements are comma-separated text
- No pagination on list endpoints (fine at course-project scale)
- No HTTPS termination (handled by reverse proxy in real deployments)
- SonarQube/Deploy stages require external infrastructure, verified manually

## 22. Team / Project Info

| | |
|---|---|
| **Course** | Agile Development & DevOps |
| **Team** | *< Your names / register numbers here >* |
| **Guide** | *< Faculty guide name here >* |
| **Institution** | *< College name here >* |
| **Academic year** | 2026–27 |

## Demo Credentials

| Role | Username | Password |
|---|---|---|
| Student | `arjun` | `demoStudent@123` |
| Student | `priya` | `demoStudent@123` |
| Student | `rahul` | `demoStudent@123` |
| Placement Officer | `officer` | `demoOfficer@123` |
| Admin | `admin` | `demoAdmin@123` |

> Demo passwords are injected via environment variables (`DEMO_PASSWORD_*`), overridable in `.env` — never hardcoded in source.
