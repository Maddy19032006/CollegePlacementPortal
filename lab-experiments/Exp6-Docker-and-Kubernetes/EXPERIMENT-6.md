# 🧪 Experiment 6: Dockers and Kubernetes
**Course:** ISWE406P · Agile Development Process and DevOps Lab  
**Lab Hours:** 3 Hours  
**Application Context:** College Placement Portal  

---

## 1. Aim
To master Docker containerization, build optimized multi-stage Docker images, orchestrate multi-container systems with Docker Compose, manage Docker logs and volumes, and deploy containerized workloads onto Kubernetes (Minikube) using Pods, Deployments, Services, ConfigMaps, and Namespaces.

---

## 2. Docker Hands-on Commands & Tasks

### Task 1: Building Multi-Stage Docker Image
Inspect the project [`Dockerfile`](../../Dockerfile):
```dockerfile
# Stage 1: Build stage with Maven and JDK
FROM maven:3.9-eclipse-temurin-25 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn -B dependency:go-offline
COPY src ./src
RUN mvn -B clean package -DskipTests

# Stage 2: Minimal runtime with JRE Alpine & non-root user
FROM eclipse-temurin:25-jre-alpine
WORKDIR /app
RUN addgroup -S spring && adduser -S spring -G spring
USER spring
COPY --from=build /app/target/placement-portal.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
```

Build and tag the image:
```bash
docker build -t placement-portal:1.0 .
```

### Task 2: Managing Containers, Logs, and Volumes
```bash
# Run standalone container
docker run -d --name placement-app -p 8081:8081 placement-portal:1.0

# Inspect container logs
docker logs placement-app -f

# Inspect container resource consumption
docker stats placement-app

# List active Docker volumes
docker volume ls
```

### Task 3: Multi-Container Orchestration with Docker Compose
Run the full system (Spring Boot + MySQL 8 + Healthchecks + Persistent Volume):
```bash
# Build and run in detached mode
docker compose up -d --build

# Verify container statuses and health
docker compose ps

# View unified logs
docker compose logs -f app

# Tear down containers and networks (persisting database volume)
docker compose down
```

---

## 3. Kubernetes (Minikube) Orchestration

### Architecture of K8s Manifests in `k8s/`
- **Namespace (`00-namespace.yaml`):** Isolates the project into `placement-portal`.
- **Secret (`01-secrets.yaml`):** Opaque Base64 credentials for database.
- **PersistentVolumeClaim (`02-mysql-pvc.yaml`):** Dynamic 2Gi volume storage.
- **MySQL Deployment & Service (`03-mysql.yaml`):** Single replica DB with ClusterIP.
- **ConfigMap (`04-app-configmap.yaml`):** Application runtime properties.
- **App Deployment & Service (`05-app.yaml`):** 2 application replicas exposed via `NodePort: 30081`.

### Step-by-Step Minikube Execution Commands
```bash
# 1. Start local Kubernetes cluster
minikube start --driver=docker

# 2. Point terminal to Minikube's Docker daemon
eval $(minikube docker-env)

# 3. Build image directly inside Minikube
docker build -t placement-portal:local .

# 4. Apply all Kubernetes manifests
kubectl apply -f k8s/

# 5. Verify Pods, Deployments, and Services
kubectl get namespaces
kubectl get pods -n placement-portal -o wide
kubectl get deployments -n placement-portal
kubectl get svc -n placement-portal

# 6. Access the Application NodePort in Browser
minikube service placement-app-service -n placement-portal --url
```

---

## 4. Viva Questions & Answers
1. **Q: Why use a multi-stage Docker build?**  
   *A: Multi-stage builds separate the compile environment (JDK, Maven, dependencies ~800MB) from the runtime image (minimal JRE Alpine ~150MB), reducing attack surface, vulnerabilities, and transfer size.*
2. **Q: What is the difference between a Pod and a Deployment in Kubernetes?**  
   *A: A Pod is the smallest deployable execution unit containing one or more containers. A Deployment is a higher-level controller that manages desired state, scaling, rolling updates, self-healing, and replicas of Pods.*
