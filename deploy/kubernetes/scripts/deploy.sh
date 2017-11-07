#!/usr/bin/env bash

echo "Start minikube ================================================================================================"
(minikube delete || true) &>/dev/null && \
minikube start --memory 8192 && \
eval $(minikube docker-env)

echo "Deploy Cassandra =============================================================================================="
kubectl create -f deploy/kubernetes/resources/cassandra && \
deploy/kubernetes/scripts/kubectl-wait-for-pods && \
kubectl exec cassandra-0 -- nodetool status

echo "Create Docker images =========================================================================================="
sbt -DbuildTarget=kubernetes clean docker:publishLocal

echo "Deploy Chirper ================================================================================================"
kubectl create -f deploy/kubernetes/resources/chirper && \
deploy/kubernetes/scripts/kubectl-wait-for-pods && \
kubectl get all

echo "Deploy NGINX =================================================================================================="
kubectl create -f deploy/kubernetes/resources/nginx && \
deploy/kubernetes/scripts/kubectl-wait-for-pods && \
kubectl get pods

echo "Verify deployment ============================================================================================="
echo "Chirper UI (HTTP): $(minikube service --url nginx-ingress | head -n 1)" && \
    echo "Chirper UI (HTTPS): $(minikube service --url --https nginx-ingress | tail -n 1)" && \
    echo "Kubernetes Dashboard: $(minikube dashboard --url)"