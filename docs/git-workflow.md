# Git Workflow

## Branching strategy

```
main          ──────────────────────────────────────►  (releasable, protected)
                ▲                    ▲
develop       ───┴────────────────────┴───────────►  (integration branch)
                ▲        ▲        ▲        ▲
feature/*    ───┴────────┴────────┴────────┴──────►  (one branch per story)
```

- **`main`** — always deployable. Only receives merges from `develop` at release points.
- **`develop`** — integration branch; every feature merges here via Pull Request.
- **`feature/<name>`** — one branch per user story or feature.

## Feature branches used in this project

| Branch | Content |
|---|---|
| `feature/project-setup` | Maven skeleton, config, .gitignore |
| `feature/authentication` | Registration, login, roles, security |
| `feature/student-profile` | Profile, skills, preparation, readiness score |
| `feature/company-management` | Company CRUD + search |
| `feature/application-tracking` | Apply, eligibility, status flow |
| `feature/dashboards` | Student/officer/admin dashboards |
| `feature/testing` | Unit + MockMvc tests |
| `feature/devops` | Docker, Jenkinsfile, SonarQube, docs |

## Daily flow

```bash
# start a story
git checkout develop && git pull
git checkout -b feature/authentication

# commit small and often
git add <files> && git commit -m "feat(auth): add registration with password rules"

# keep your branch up to date
git fetch origin && git rebase origin/develop

# open a Pull Request into develop; a teammate reviews; merge (no-ff)
```

## Commit message convention

`<type>(<scope>): <summary>` — types: `feat`, `fix`, `test`, `docs`, `refactor`, `chore`, `ci`.

Examples:
- `feat(application): reject duplicate applications`
- `test(eligibility): cover missing-skill failures`
- `ci(jenkins): add smoke test stage`

## Version tags

When releasing `develop` → `main`, tag: `git tag -a v1.0.0 -m "First release"`.
