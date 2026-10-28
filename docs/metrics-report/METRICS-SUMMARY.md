# 📊 Agile Quality & DevOps Metrics Summary Report
**Course Reference:** ISWE406L/P · Agile Development Process and DevOps  
**Evaluation Mapping:** Theory Module 6 (Agile in Product Development and Metrics) & Lab Experiment 7  
**Project:** College Placement Portal (`placement-portal`)  
**Evaluation Date:** October 2026  

---

## 1. Executive Summary & Verification Evidence

This report documents the empirical quality, testing, and process metrics captured directly from the automated Maven build, Surefire test engine, and JaCoCo code coverage analyzers. All metrics reflect a clean, regression-free, and production-ready state.

```
[ Automated Test Pass Rate: 100.0% ]   [ JaCoCo Instruction Coverage: 68.9% ]
[ Total Tests: 42 / 42 Passing ]       [ JaCoCo Line Coverage: 72.2% ]
[ Test Suites: 6 Suites (0 Failures) ] [ Total Classes Analyzed: 53 Classes ]
```

---

## 2. Automated Test Execution Metrics (Surefire Reports)

Sourced directly from `target/surefire-reports/`:

| Test Suite (Class) | Layer Tested | Tests Run | Failures | Errors | Skipped | Time (s) | Status |
|---|---|:---:|:---:|:---:|:---:|:---:|:---:|
| `AuthFlowIntegrationTest` | Security & Auth Filter Chain | 14 | 0 | 0 | 0 | 10.71 | ✅ PASS |
| `StudentJourneyIntegrationTest`| End-to-End Business Journey | 4 | 0 | 0 | 0 | 0.42 | ✅ PASS |
| `ApplicationServiceTest` | Service State Machine & Rules | 6 | 0 | 0 | 0 | 0.59 | ✅ PASS |
| `CompanyServiceTest` | Company CRUD & Constraints | 4 | 0 | 0 | 0 | 0.02 | ✅ PASS |
| `EligibilityServiceTest` | Multi-Factor Eligibility Engine | 8 | 0 | 0 | 0 | 0.10 | ✅ PASS |
| `ReadinessScoreCalculationTest`| Readiness Mathematical Weights | 6 | 0 | 0 | 0 | 0.01 | ✅ PASS |
| **Total Aggregates** | **Full System Verification** | **42** | **0** | **0** | **0** | **11.85 s** | **100% Pass** |

---

## 3. Code Coverage Metrics (JaCoCo Analyzer)

Extracted from the generated `jacoco.csv` (`docs/metrics-report/jacoco/jacoco.csv`):

| Metric Category | Covered | Missed | Total | Coverage Percentage | Target / Quality Gate |
|---|:---:|:---:|:---:|:---:|:---:|
| **Instructions (Bytecode)** | 3,070 | 1,386 | 4,456 | **68.9%** | $\ge 65.0\%$ (PASS) |
| **Lines of Code** | 725 | 279 | 1,004 | **72.2%** | $\ge 70.0\%$ (PASS) |
| **Branches (Decision Points)** | 85 | 129 | 214 | **39.7%** | Core logic covered |
| **Cyclomatic Complexity** | 247 | 179 | 426 | **58.0%** | Controlled paths |
| **Methods** | 221 | 98 | 319 | **69.3%** | $\ge 65.0\%$ (PASS) |
| **Classes Analyzed** | 53 | 0 | 53 | **100.0%** | All classes mapped |

### Package-Wise Coverage Breakdown:
1. `com.college.placement.service`: **~88%** instruction coverage (Core business rules, readiness score, eligibility checks, state transitions).
2. `com.college.placement.entity`: **~85%** instruction coverage (Domain models, JPA entities, lifecycle callbacks).
3. `com.college.placement.controller`: **~74%** instruction coverage (REST APIs, HTTP status codes, validation binding).
4. `com.college.placement.security`: **~80%** instruction coverage (Spring Security user details, authentication providers).
5. `com.college.placement.exception`: **~90%** instruction coverage (Global exception handler, standard API error payloads).

---

## 4. Theory Module 6 Mapping: Agile Metrics & Developer Practices

### A. Developer Practices & Static Analysis
- **Code Coverage:** JaCoCo XML & HTML reports generated and stored permanently in `docs/metrics-report/jacoco/index.html`.
- **Code Metrics:** Cyclomatic complexity monitored per method; high-risk conditionals isolated in dedicated unit tests (`EligibilityServiceTest` & `ApplicationServiceTest`).
- **Compiler & Code Analysis Warnings:** Zero compiler warnings under `javac -parameters --release 21`; SonarQube Maven plugin pre-configured with project key `college-placement-portal`.

### B. Bug Trend & Defect Density Report
- **Defects Discovered During Sprint Testing:**
  - *Defect BUG-01 (Resolved):* `LazyInitializationException` on lazy `User` and `Skill` proxies during officer student listing $\rightarrow$ Fixed via EAGER fetch, `@Transactional(readOnly = true)`, and `open-in-view=true`.
  - *Defect BUG-02 (Resolved):* Windows batch syntax termination due to nested parentheses in `run-app-local.bat` $\rightarrow$ Fixed using clean label-based `goto` execution flow.
- **Active Open Defects:** **0** (Defect Density = 0 defects / KLOC).

### C. Release & Agile Project Management Metrics
- **Committed vs. Completed Velocity:**
  - Sprint 1: Committed 18 pts $\rightarrow$ Completed 18 pts (100%)
  - Sprint 2: Committed 16 pts $\rightarrow$ Completed 16 pts (100%)
  - Sprint 3: Committed 16 pts $\rightarrow$ Completed 16 pts (100%)
  - Sprint 4: Committed 19 pts $\rightarrow$ Completed 19 pts (100%)
  - Sprint 5: Committed 23 pts $\rightarrow$ Completed 23 pts (100%)
- **Total Project Velocity:** **92 Story Points** delivered across 5 sprints.
- **Burn-down Trend:** Steady decline in remaining story points per 10-day sprint cycle with zero carry-over into post-release.
