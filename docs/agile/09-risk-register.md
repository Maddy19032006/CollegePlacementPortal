# 09 · Risk Register

| ID | Risk | Likelihood | Impact | Score | Mitigation | Owner | Status |
|---|---|---|---|---|---|---|---|
| R1 | Team member unfamiliar with Spring Security → security holes | Medium | High | 6 | Follow a fixed checklist (hashing, role rules, 401/403 handlers); security tests required | Dev pair | Closed — security tests in CI |
| R2 | MySQL setup differences break local runs | High | Medium | 6 | Defaults in `application.properties`, `.env.example`, Docker Compose fallback | All | Open — document any local fix |
| R3 | Scope creep (extra features before core loop works) | Medium | High | 6 | Backlog is prioritized; P0 first; "Do not overbuild" list in project brief | Scrum master | Open |
| R4 | Business-rule regressions (duplicates, deadlines, transitions) | Medium | High | 6 | Automated tests for every rule; CI fails on red | QA owner | Closed — 36 tests |
| R5 | Jenkins/SonarQube infrastructure unavailable near deadline | Medium | Medium | 4 | Pipeline documented for local setup; stages gated on env vars; demo can show local run + tests | DevOps owner | Open |
| R6 | Docker not installable on some laptops | Medium | Medium | 4 | Local (non-Docker) run path documented and verified | DevOps owner | Open |
| R7 | Deadline pressure near submission week | Medium | High | 6 | 5-sprint plan with buffer; stretch goals (P2) cut first | Team | Open |
| R8 | Merge conflicts from long-lived branches | Medium | Low | 2 | Rebase on develop frequently; small stories; PR review | All | Open |
| R9 | Demo data lost/edited during viva | Low | Medium | 2 | Seeder runs only on empty DB; reset by dropping schema; credentials documented | Team | Open |
| R10 | Sensitive data accidentally committed | Low | High | 3 | `.gitignore` covers `.env`, IDE, target; secrets only via env vars; pre-push review checklist | All | Open |

Review cadence: risks re-scored at every retrospective.
