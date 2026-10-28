@echo off
echo ===================================================
echo  Deploying College Placement Portal to Minikube (K8s)
echo ===================================================
echo Applying Kubernetes manifests...
kubectl apply -f k8s/00-namespace.yaml
kubectl apply -f k8s/01-secrets.yaml
kubectl apply -f k8s/02-mysql-pvc.yaml
kubectl apply -f k8s/03-mysql.yaml
kubectl apply -f k8s/04-app-configmap.yaml
kubectl apply -f k8s/05-app.yaml
echo.
echo Checking Kubernetes resources:
kubectl get pods,svc,pvc -n placement-portal
echo.
echo To open in browser:
echo minikube service placement-app-service -n placement-portal --url
pause
