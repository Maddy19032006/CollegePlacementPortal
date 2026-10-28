# Setup & Verification Record

This file records **exactly what was verified** in the development environment and **what requires manual setup**, so nothing is claimed that was not actually run.

## Verified in the development environment

| Item | Command | Result |
|---|---|---|
| Compile | `mvn clean compile` | ✅ BUILD SUCCESS |
| All tests | `mvn test` | ✅ 36/36 pass (0 failures, 0 errors) |
| Full build | `mvn clean package` | ✅ BUILD SUCCESS (jar produced, tests run during packaging) |
| Java version used | `java -version` | Java 25 (code targets Java 25 via `--release`) |
| Maven | `mvn -version` | 3.9.16 |
| Runtime smoke test | attempted `java -jar` boot | ⚠️ Not completed: no MySQL server was running on the dev machine (port 3306 closed), so the app could not start against a real database. The 36 MockMvc tests cover the same runtime stack (security chain, controllers, services, JPA) against in-memory H2 instead. |

## Requires manual setup (tools not available in the dev machine)

### 1. MySQL (local run without Docker)
- Install/start MySQL 8 and create the database:
  ```bash
  mysql -u root -p -e "CREATE DATABASE placement_portal;"
  ```
- Or rely on `createDatabaseIfNotExist=true` in the JDBC URL (needs privileges).
- **TODO(student):** run `mvn spring-boot:run`, open http://localhost:8081, login with `arjun / demoStudent@123`, confirm demo data appears.

### 2. Docker / Docker Compose
- **Not installed** on the development machine, so `docker compose up --build` **could not be executed here**.
- The `Dockerfile` (multi-stage Maven→JRE build, non-root runtime user) and `docker-compose.yml` (app + MySQL 8, health checks, env-var config) are complete and follow standard patterns.
- **TODO(student):** with Docker Desktop installed:
  ```bash
  cp .env.example .env   # set DB_PASSWORD
  docker compose up --build
  # verify http://localhost:8081/login.html returns 200
  docker compose ps       # both containers healthy
  ```

### 3. Jenkins
- The `Jenkinsfile` is a declarative pipeline; it cannot be validated without a Jenkins server.
- **TODO(student):** follow README §18 (Jenkins Setup). Required tool names: `jdk25`, `maven3`; optional credentials: `sonar-token`, `docker-registry`.
- The pipeline is written for a Windows agent (`bat` steps); switch `bat` → `sh` for Linux agents.

### 4. SonarQube
- Not run here (needs a server). Configuration is ready:
  - `pom.xml` has the SonarQube Maven plugin and project key
  - Token passed via `-Dsonar.token`, never committed
- **TODO(student):**
  ```bash
  docker run -d --name sonar -p 9000:9000 sonarqube:lts-community
  # open http://localhost:9000 (admin/admin), generate a token, then:
  mvn sonar:sonar -Dsonar.host.url=http://localhost:9000 -Dsonar.token=<YOUR_TOKEN>
  ```

## Security check performed before publishing

- ✅ No passwords, tokens or keys in source (demo passwords come from env vars with clearly-labeled dev defaults)
- ✅ `.env` is gitignored; `.env.example` contains placeholders only
- ✅ No IDE folders, build output (`target/`), or logs will be committed
- ✅ No personal data beyond fictional demo names/emails
