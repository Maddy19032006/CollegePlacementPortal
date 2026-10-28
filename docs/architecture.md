# Architecture

## 1. High-Level System Architecture

```mermaid
flowchart LR
    subgraph Client
        B["Browser\n(Bootstrap 5 UI)"]
    end
    subgraph Server["Spring Boot Application"]
        SC["Security\nFilter Chain"]
        C["Controllers\n(REST)"]
        S["Services\n(Business Logic)"]
        R["Repositories\n(Spring Data JPA)"]
    end
    DB[("MySQL 8")]
    B -->|HTTP + session cookie| SC
    SC --> C --> S --> R --> DB
```

- The browser talks to REST endpoints; static HTML/JS pages are served by Spring Boot from `static/`.
- Spring Security authenticates every request and enforces role rules before controllers run.
- All business rules live in the service layer; controllers only translate HTTP ↔ DTOs.

## 2. Application Component Flow

```mermaid
flowchart TD
    subgraph Web["Presentation"]
        P["Static pages\nlogin, dashboard,\ncompanies, applications"]
    end
    subgraph API["REST Layer"]
        AC["AuthController"]
        SCC["StudentController"]
        CC["CompanyController"]
        AXC["ApplicationController"]
        OC["OfficerController"]
        ADC["AdminController"]
    end
    subgraph Logic["Service Layer"]
        AS["AuthService"]
        SS["StudentService"]
        CS["CompanyService"]
        AXS["ApplicationService"]
        ES["EligibilityService"]
        DS["OfficerDashboardService"]
        ADS["AdminService"]
    end
    subgraph Data["Persistence"]
        UR["UserRepository"]
        SR["StudentProfileRepository"]
        PR["PreparationScoreRepository"]
        KR["SkillRepository"]
        CR["CompanyRepository"]
        AR["ApplicationRepository"]
    end
    P --> AC & SCC & CC & AXC & OC & ADC
    AC --> AS
    SCC --> SS
    CC --> CS
    AXC --> AXS
    OC --> DS
    ADC --> ADS
    AXS --> ES
    AS & SS & ADS --> UR
    SS --> SR & PR & KR
    CS --> CR
    AXS --> AR & CR & SR
    ES --> KR
```

## 3. CI/CD Pipeline

```mermaid
flowchart LR
    A[Git Push] --> B[Jenkins\nCheckout]
    B --> C[Build\nmvn compile]
    C --> D[Unit & Integration\nTests]
    D -->|fail| F[❌ Pipeline fails]
    D -->|pass| E[SonarQube\nCode Quality]
    E --> G[Package\nmvn package]
    G --> H[Docker Build]
    H --> I[Docker Push\nregistry]
    I --> J[Deploy\ndocker compose up]
    J --> K[Smoke Test\nHTTP check]
    K --> L[✅ Done]
```

## 4. Database ER Diagram

```mermaid
erDiagram
    USERS ||--o| STUDENT_PROFILES : "has"
    STUDENT_PROFILES ||--|| PREPARATION_SCORES : "has"
    STUDENT_PROFILES ||--o{ STUDENT_SKILLS : "has"
    SKILLS ||--o{ STUDENT_SKILLS : "listed-in"
    STUDENT_PROFILES ||--o{ APPLICATIONS : "submits"
    COMPANIES ||--o{ APPLICATIONS : "receives"

    USERS {
        bigint id PK
        varchar username UK
        varchar password "BCrypt hash"
        varchar email UK
        varchar full_name
        varchar role "STUDENT / PLACEMENT_OFFICER / ADMIN"
        boolean enabled
        datetime created_at
    }
    STUDENT_PROFILES {
        bigint id PK
        bigint user_id FK
        varchar registration_number UK
        varchar phone
        varchar department
        varchar program
        int graduation_year
        decimal cgpa
        varchar certifications
        varchar projects
        varchar resume_summary
    }
    PREPARATION_SCORES {
        bigint id PK
        bigint student_id FK
        int aptitude
        int coding
        int technical
        int projects
        int communication
    }
    SKILLS {
        bigint id PK
        varchar name UK
    }
    STUDENT_SKILLS {
        bigint id PK
        bigint student_id FK
        bigint skill_id FK
    }
    COMPANIES {
        bigint id PK
        varchar name
        varchar job_role
        decimal package_ctc
        decimal min_cgpa
        varchar required_departments
        varchar required_skills
        int min_graduation_year
        date application_deadline
        varchar description
    }
    APPLICATIONS {
        bigint id PK
        bigint student_id FK
        bigint company_id FK
        varchar status
        datetime applied_at
        datetime last_updated_at
    }
```

## 5. Application Workflow (Apply for a Company)

```mermaid
flowchart TD
    A[Student opens Companies page] --> B[Selects a company]
    B --> C{Logged in as\nSTUDENT?}
    C -->|no| D[Redirect to login]
    C -->|yes| E[Clicks Apply]
    E --> F[POST /api/applications?companyId=]
    F --> G{Profile saved?}
    G -->|no| H[400: Save your profile first]
    G -->|yes| I{Already applied?}
    I -->|yes| J[400: Duplicate application]
    I -->|no| K{Deadline passed?}
    K -->|yes| L[400: Deadline has passed]
    K -->|no| M{Eligible?\nCGPA / dept / year / skills}
    M -->|no| N[400: Not eligible + failed criteria]
    M -->|yes| O[Application saved\nstatus APPLIED]
    O --> P[Officer moves status:\nAPPLIED → SHORTLISTED → ... → SELECTED]
```

## 6. Application Status Flow

```mermaid
stateDiagram-v2
    [*] --> APPLIED
    APPLIED --> SHORTLISTED
    SHORTLISTED --> ONLINE_TEST
    ONLINE_TEST --> TECHNICAL_INTERVIEW
    TECHNICAL_INTERVIEW --> HR_INTERVIEW
    HR_INTERVIEW --> SELECTED
    APPLIED --> REJECTED
    SHORTLISTED --> REJECTED
    ONLINE_TEST --> REJECTED
    TECHNICAL_INTERVIEW --> REJECTED
    HR_INTERVIEW --> REJECTED
    SELECTED --> REJECTED
    REJECTED --> APPLIED
```
