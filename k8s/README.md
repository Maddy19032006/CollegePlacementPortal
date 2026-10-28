# ☸️ Kubernetes Deployment Guide (Lab Experiment 6)

This directory contains the Kubernetes manifests to deploy the **College Placement Portal** on a local Kubernetes cluster (Minikube / MicroK8s / K3s) or cloud-managed K8s.

---

## 1. Architecture in Kubernetes

- **Namespace (`placement-portal`)**: Logical isolation for our microservices and resources.
- **ConfigMap (`placement-config`)**: Centralized application configuration.
- **Secret (`placement-secrets`)**: Sensitive credentials (MySQL root password, demo account passwords).
- **PersistentVolumeClaim (`mysql-pvc`)**: 2GB persistent storage so MySQL database records survive pod restarts.
- **MySQL Deployment & Service**: Single replica MySQL 8 with internal `ClusterIP`.
- **Application Deployment & Service**: 2 replicas of the Spring Boot application exposed via `NodePort: 30081`.

---

## 2. Prerequisites

- [Minikube](https://minikube.sigs.k8s.io/docs/start/) installed
- [kubectl](https://kubernetes.io/docs/tasks/tools/) installed
- [Docker](https://docs.docker.com/get-docker/) installed

---

## 3. Step-by-Step Deployment Instructions

### Step 1: Start Minikube
```bash
minikube start --driver=docker
```

### Step 2: Build Application Image inside Minikube Docker Environment
To make your locally built image directly available to Minikube without pushing to Docker Hub:
```bash
# Point local terminal to Minikube's Docker daemon
# On Linux/macOS:
eval $(minikube docker-env)

# On Windows PowerShell:
& minikube -p minikube docker-env --shell powershell | Invoke-Expression

# Build image with tag matching manifest
docker build -t placement-portal:local .
```

### Step 3: Apply All Manifests in Order
```bash
kubectl apply -f k8s/00-namespace.yaml
kubectl apply -f k8s/01-secrets.yaml
kubectl apply -f k8s/02-mysql-pvc.yaml
kubectl apply -f k8s/03-mysql.yaml
kubectl apply -f k8s/04-app-configmap.yaml
kubectl apply -f k8s/05-app.yaml
```
*(Or apply all at once: `kubectl apply -f k8s/`)*

### Step 4: Verify Resources
```bash
# Check Pods
kubectl get pods -n placement-portal

# Check Services
kubectl get svc -n placement-portal

# Check Persistent Volume Claims
kubectl get pvc -n placement-portal
```

### Step 5: Access the Application
Get the URL to open in browser:
```bash
minikube service placement-app-service -n placement-portal --url
```
Or use port forwarding:
```bash
kubectl port-forward -n placement-portal svc/placement-app-service 8081:8081
# Access at: http://localhost:8081
```

---

## 4. Teardown
```bash
kubectl delete -f k8s/
```
