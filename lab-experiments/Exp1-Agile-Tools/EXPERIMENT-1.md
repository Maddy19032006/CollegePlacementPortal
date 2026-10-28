# 🧪 Experiment 1: Introducing Agile Tools and Techniques
**Course:** ISWE406P · Agile Development Process and DevOps Lab  
**Lab Hours:** 4 Hours  
**Application Context:** College Placement Portal  

---

## 1. Aim
To analyze the core practices behind modern Agile tools (JIRA, ClickUp, GitHub, LeanKit), and to configure issue tracking, bug tracking, epics, user stories, story points, custom fields, labels, mentions, and pull request linking.

---

## 2. Tools & Platforms
- **GitHub Projects & Issues** (Primary open-source agile tracker)
- **JIRA Agile / ClickUp / LeanKit** (Enterprise agile project management)

---

## 3. Theory & Concepts
- **Epics:** Large bodies of work that can be broken down into smaller tasks (user stories). In our portal, we have 6 epics (Foundation, Student Profile, Company Catalog, Application Lifecycle, Dashboards, DevOps).
- **User Stories:** Small, deliverable requirements written from the end-user perspective:  
  *Format:* `"As a <role>, I want <feature>, so that <business value>"`
- **Story Points:** A relative metric of effort, complexity, and uncertainty using modified Fibonacci numbers ($1, 2, 3, 5, 8$).
- **Mentions & Linking:** Using `@username` to assign tasks and `#issue_id` (e.g. `Closes #14`) in Git commits or Pull Requests to link code changes to requirements automatically.

---

## 4. Practical Implementation Steps (For Your Lab Evaluation)

### Step 1: Create Issue Templates in GitHub
In this project, we created two standardized templates under `.github/ISSUE_TEMPLATE/`:
1. `user_story.md` (Contains user story format, Gherkin acceptance criteria, DoD checklist).
2. `bug_report.md` (Contains steps to reproduce, expected vs actual behavior, error payloads).

### Step 2: Create Agile Epics & Stories
Map the user stories from [`docs/agile/04-product-backlog.md`](../../docs/agile/04-product-backlog.md) into your GitHub / Jira board:
- **Story 1 (US-002):** User Registration with Validation (5 points)
- **Story 2 (US-008):** Placement Readiness Score & Visualization (5 points)
- **Story 3 (US-012):** Eligibility Checking Engine (5 points)
- **Story 4 (US-013):** Student Applies to Company (5 points)
- **Story 5 (US-014):** Duplicate & Deadline Prevention (3 points)

### Step 3: Configure GitHub Projects Board (Kanban)
1. In your GitHub repository, navigate to **Projects** $\rightarrow$ **New Project** $\rightarrow$ **Board View**.
2. Create columns: `📋 Backlog`, `🏗️ In Progress`, `🔍 Code Review / In Review`, `✅ Done`.
3. Add custom fields:
   - `Story Points` (Number)
   - `Priority` (Single Select: P0, P1, P2)
   - `Sprint` (Sprint 1, Sprint 2, etc.)

### Step 4: Link Issues to Pull Requests
When creating a pull request to merge a feature into `develop`, link the issue using the PR template:
```markdown
## Summary
Implemented student application submission with duplicate check.

Closes #13
```
*Result:* Merging the PR automatically transitions Issue #13 to the **Done** column on the board!

---

## 5. Expected Output
- A fully populated Kanban/Scrum board in GitHub or JIRA displaying Epics, Stories, and Points.
- Issue templates active when opening a new issue in GitHub.
- Automated status progression when pull requests are merged.

---

## 6. Viva Questions & Answers
1. **Q: What is the difference between an Epic and a User Story?**  
   *A: An Epic is a high-level strategic initiative spanning multiple sprints (e.g., Application Lifecycle), while a User Story is an atomic, estimable piece of deliverable value that fits within a single sprint (e.g., Prevent duplicate applications).*
2. **Q: Why are Story Points used instead of hours?**  
   *A: Story points measure relative complexity and effort independent of individual developer speed, accounting for uncertainty, risk, and team velocity.*
