# 🧪 Experiment 2: Agile Project Management Tools
**Course:** ISWE406P · Agile Development Process and DevOps Lab  
**Lab Hours:** 4 Hours  
**Application Context:** College Placement Portal  

---

## 1. Aim
To manage agile workflows, prioritize product backlogs, analyze sprint performance, apply Lean metrics (Lead Time, Cycle Time, WIP Limits), and execute sprint planning and retrospectives using Agile tools (Wrike, Monday.com, Zoho Sprints, Kanbanize, Taiga.io).

---

## 2. Tools Compared
| Tool | Primary Strength (Per Syllabus) | Placement Portal Application |
|---|---|---|
| **Zoho Sprints / JIRA** | Backlog prioritization & release management | Managing 23 user stories across 5 Sprints |
| **ClickUp / Monday.com** | Best visual aids to distinguish sprint items | Color-coded priorities (P0=Red, P1=Orange, P2=Green) |
| **Kanbanize / LeanKit** | Online portfolio Kanban & Lean metrics | Monitoring WIP limits and flow bottlenecks |
| **Taiga.io** | Clean open-source Agile UX | Scrum burndown and velocity charts |

---

## 3. Backlog Prioritization Techniques
In our portal project, the 23 stories are prioritized using the **MoSCoW Method**:
1. **Must Have (P0 - 72 Points):**
   - User Auth, Profile Management, Eligibility Engine, Application Lifecycle, Automated Tests, Docker, Jenkins CI/CD.
2. **Should Have (P1 - 15 Points):**
   - Demo data seeding, Admin user management, SonarQube code quality.
3. **Could Have (P2 - 5 Points):**
   - Email notifications, resume PDF generation (deferred to future sprints).

---

## 4. Sprint Cadence & Execution (College Placement Portal)
- **Sprint Duration:** 2 Weeks (10 working days).
- **Target Velocity:** 18–20 Story Points per sprint.

```
Sprint 1 (18 pts): Project Foundation & Authentication (US-001 - US-004)
Sprint 2 (16 pts): Student Profile & Readiness Score Engine (US-005 - US-008)
Sprint 3 (16 pts): Company Catalog & Eligibility Evaluation (US-009 - US-012)
Sprint 4 (19 pts): Application State Machine & Dashboards (US-013 - US-019)
Sprint 5 (23 pts): Testing, Docker, Jenkins CI/CD & SonarQube (US-020 - US-023)
```

---

## 5. Lean Metrics & Sprint Retrospective
- **Work In Progress (WIP) Limit:** Maximum 3 items in `In Progress` at any time to prevent multitasking bottlenecks.
- **Cycle Time:** Average time from starting work (`In Progress`) to completion (`Done`) = ~2.5 days per story.
- **Sprint Retrospective Checklist:**
  - *What went well:* Automated MockMvc tests caught duplicate application bugs early.
  - *What didn't go well:* Database environment setup differed across team machines.
  - *Action Item:* Standardized container deployment via `docker-compose.yml` to guarantee parity.

---

## 6. Viva Questions & Answers
1. **Q: What is the purpose of Work In Progress (WIP) limits in Kanban?**  
   *A: WIP limits reduce multitasking, uncover system bottlenecks, and minimize context-switching overhead, ensuring steady flow.*
2. **Q: How does a team calculate sprint velocity?**  
   *A: Velocity is the sum of story points of all User Stories completed and meeting the Definition of Done (DoD) during a sprint.*
