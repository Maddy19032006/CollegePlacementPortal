---
name: User Story
about: Template for Agile User Stories according to SCRUM framework
title: '[STORY]: '
labels: 'user-story, agile'
assignees: ''
---

### 👤 User Story
**As a** [role/user persona]  
**I want** [capability/action]  
**So that** [benefit/value achieved]  

---

### 🎯 Business Value & Priority
- **Priority:** P0 (Must Have) / P1 (Should Have) / P2 (Nice to Have)
- **Story Points:** [e.g., 1, 2, 3, 5, 8]
- **Sprint Target:** Sprint [1–5]
- **Epic:** [E1: Foundation | E2: Profile | E3: Companies | E4: Applications | E5: Dashboards | E6: DevOps]

---

### ✅ Acceptance Criteria (Gherkin Format)
```gherkin
Scenario: [Title of scenario]
  Given [initial context]
  When [action executed]
  Then [expected outcome]
```

---

### 📋 Definition of Done Checklist
- [ ] Code implemented on dedicated `feature/*` branch
- [ ] Unit & integration tests added and passing in CI (`mvn test`)
- [ ] Input validation applied (Jakarta Validation)
- [ ] Pull request peer-reviewed and approved
- [ ] Documentation updated in README/API docs
