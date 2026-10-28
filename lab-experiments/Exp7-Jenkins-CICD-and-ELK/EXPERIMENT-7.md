# 🧪 Experiment 7: Continuous Integration with Jenkins & Monitoring using ELK
**Course:** ISWE406P · Agile Development Process and DevOps Lab  
**Lab Hours:** 3 Hours  
**Application Context:** College Placement Portal  

---

## 1. Aim
To establish an automated Continuous Integration and Continuous Delivery (CI/CD) pipeline using Jenkins (Master-Slave architecture), enforce automated code quality gates with SonarQube, and deploy an ELK Stack (Elasticsearch, Logstash, Kibana) for centralized continuous application logging and real-time monitoring.

---

## 2. Jenkins CI/CD Pipeline Implementation

### Jenkins Master-Slave Architecture
- **Jenkins Master (Controller):** Schedules build jobs, dispatches builds to agents/slaves, and monitors results.
- **Jenkins Slave (Agent):** Executes compilation, test suites, Docker image builds, and deployments in an isolated environment.

### 8-Stage Declarative Pipeline ([`Jenkinsfile`](../../Jenkinsfile))
```groovy
pipeline {
    agent any
    stages {
        stage('Checkout')                  { steps { checkout scm } }
        stage('Build')                     { steps { bat 'mvn -B clean compile' } }
        stage('Unit & Integration Tests') { steps { bat 'mvn -B test' } }
        stage('Code Quality (SonarQube)')  { steps { bat 'mvn -B sonar:sonar ...' } }
        stage('Package')                   { steps { bat 'mvn -B -DskipTests package' } }
        stage('Docker Build')              { steps { bat 'docker build -t placement-portal:%BUILD_NUMBER% .' } }
        stage('Deploy')                    { steps { bat 'docker compose up -d' } }
        stage('Smoke Test')                { steps { /* Curl healthcheck verification */ } }
    }
}
```

---

## 3. Continuous Monitoring with the ELK Stack

### ELK Components & Responsibilities
1. **Elasticsearch (Port 9200):** Distributed JSON document store and search engine.
2. **Logstash (Port 5000 / 5044):** Pipeline engine that ingests, parses (Grok filter), and transforms log events.
3. **Kibana (Port 5601):** Web UI for querying logs and building visual metrics dashboards.

### ELK Data Flow
```
[ Spring Boot Application ] 
       │ writes logs via logback-spring.xml
       ▼
 [ logs/placement-portal.log ]
       │ monitored by Logstash
       ▼
  [ Logstash Pipeline ] ──( Grok regex parsing )──► [ Elasticsearch ] ──► [ Kibana Dashboard ]
```

### Starting and Verifying ELK
```bash
# 1. Start the ELK stack
docker compose -f docker-compose-elk.yml up -d

# 2. Verify containers are healthy
docker compose -f docker-compose-elk.yml ps

# 3. Test Elasticsearch API
curl http://localhost:9200/

# 4. Open Kibana in browser
# http://localhost:5601

# 5. Create index pattern in Kibana:
# Management -> Stack Management -> Index Patterns -> Create "placement-portal-logs-*"
# Timestamp field: @timestamp
```

---

## 4. Viva Questions & Answers
1. **Q: What is the benefit of Jenkins Master-Slave architecture?**  
   *A: It allows horizontally distributing heavy workloads across multiple agent machines with different operating systems, preventing the Master node from crashing due to resource exhaustion.*
2. **Q: What is a Grok filter in Logstash?**  
   *A: Grok is a Logstash pattern-matching plugin that uses named regular expressions to parse unstructured text log lines into structured, queryable fields (e.g. timestamp, log_level, class, thread).*
