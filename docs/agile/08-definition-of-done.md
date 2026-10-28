# 08 · Definition of Done

A user story is **Done** when all of the following are true:

## Code
- [ ] Implemented on a `feature/*` branch with small, focused commits
- [ ] Follows the layered architecture (no business logic in controllers)
- [ ] Meaningful names; no dead code, no commented-out blocks, no TODOs left
- [ ] No secrets or credentials in source

## Quality
- [ ] Input validated (Jakarta Validation) with useful error messages
- [ ] Errors return the standard JSON error body with correct HTTP status
- [ ] `mvn test` passes with new tests covering the story's acceptance criteria
- [ ] No new compiler warnings introduced

## Review & integration
- [ ] Pull request opened into `develop`
- [ ] Peer reviewed and approved
- [ ] Merged without conflicts; CI green on `develop`

## Product
- [ ] Acceptance criteria demonstrated in the sprint review
- [ ] Works on a fresh database (migrations/seeding handled)
- [ ] README/API docs updated if endpoints or setup changed

## Definition of Done for the whole project
- [ ] All P0 backlog items Done; P1 items either Done or consciously deferred
- [ ] Full test suite green in CI
- [ ] `docker compose up --build` runs the system from scratch
- [ ] Jenkinsfile executes all stages (or documents exactly which need external setup)
- [ ] Documentation complete: README, architecture, agile folder, setup record
- [ ] Repository contains no secrets, build artifacts or IDE files
