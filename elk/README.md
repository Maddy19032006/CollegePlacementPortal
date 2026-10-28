# 📊 Continuous Monitoring using ELK Stack (Lab Experiment 7)

This directory details the integration of the **ELK Stack** (Elasticsearch, Logstash, Kibana) with the **College Placement Portal** for centralized logging and continuous system monitoring.

---

## 1. Components of ELK in This Project

1. **Logstash (Ingestion & Transformation)**
   - Collects unstructured and structured logs from the Spring Boot application log files (`logs/placement-portal.log`) or direct TCP streams.
   - Parses logs via Grok filters into timestamp, log level (INFO, WARN, ERROR), thread, and class names.
2. **Elasticsearch (Indexing & Storage)**
   - Stores parsed log events in searchable JSON documents indexed by date (`placement-portal-logs-YYYY.MM.dd`).
   - Enables fast full-text search across thousands of log entries.
3. **Kibana (Visualization & Dashboards)**
   - Web interface accessible at `http://localhost:5601`.
   - Allows creating real-time visual charts:
     - Error rate per minute
     - Login attempts and security failures
     - Job application submissions over time
     - API response latencies and HTTP status codes

---

## 2. Architecture & Data Flow

```
[ Spring Boot Application ]
          │ (writes to logs/placement-portal.log via logback-spring.xml)
          ▼
     [ Logstash ]  (Grok filter extracts timestamp, level, message)
          │
          ▼
   [ Elasticsearch ]  (Stores & indexes log documents on port 9200)
          │
          ▼
      [ Kibana ]  (Interactive dashboards & query explorer on port 5601)
```

---

## 3. How to Run the ELK Stack

### Step 1: Start ELK Containers
```bash
docker compose -f docker-compose-elk.yml up -d
```

### Step 2: Verify Services are Running
- **Elasticsearch:** open `http://localhost:9200` in browser (should return JSON status with cluster name).
- **Kibana:** open `http://localhost:5601` in browser.

### Step 3: Configure Index Pattern in Kibana
1. Open **Kibana** at `http://localhost:5601`.
2. Go to **Management** $\rightarrow$ **Stack Management** $\rightarrow$ **Index Patterns**.
3. Click **Create index pattern**.
4. Enter pattern: `placement-portal-logs-*`.
5. Select timestamp field: `@timestamp`.
6. Click **Discover** to explore real-time live application logs!
