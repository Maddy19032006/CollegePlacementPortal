# 07 · Acceptance Criteria

## Policy
- Every user story has measurable acceptance criteria written **before** development.
- A story is accepted only when every criterion passes in the sprint review demo.
- Criteria are phrased as Given/When/Then where useful; otherwise as verifiable statements.
- Automated tests are preferred evidence; a manual demo step is listed when automation is impractical.

## Format

```
US-XXX <title>
Given <initial context>
When  <action>
Then  <observable, testable outcome>
```

## Worked examples from this project

**Duplicate application (US-014)**
- Given Arjun has already applied to Infotech Solutions
- When he clicks Apply for Infotech Solutions again
- Then the API returns 400 with "You have already applied to this company" and no new row is created
- Evidence: `ApplicationServiceTest.duplicateApplicationIsRejected`

**Readiness score formula (US-008)**
- Given scores aptitude 80, coding 70, technical 90, projects 60, communication 50
- When the readiness score is computed
- Then the result is exactly 72.0 (16 + 21 + 18 + 12 + 5)
- Evidence: `ReadinessScoreCalculationTest.readinessScoreUsesTheDocumentedWeights`

**Role restriction (US-003)**
- Given a logged-in STUDENT
- When they call `POST /api/companies`
- Then the response is 403 Forbidden and no company is created
- Evidence: `AuthFlowIntegrationTest.studentCannotCreateCompany`

**Deadline rule (US-014)**
- Given a company whose deadline was yesterday
- When an eligible student applies
- Then the API returns 400 mentioning the deadline and no application is saved

**Status transition (US-016)**
- Given an application in APPLIED
- When an officer sets it to HR_INTERVIEW
- Then the API returns 400 with an invalid-transition message; APPLIED → SHORTLISTED succeeds

**Password storage (US-002)**
- Given a new user registers with password `Strong@Pass1`
- When the users table is inspected
- Then the stored value is a BCrypt hash and never the plain text
- Evidence: `AuthFlowIntegrationTest.registerStudentCreatesAccountWithHashedPassword`
