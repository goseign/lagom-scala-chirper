#!/usr/bin/env bash

#echo "Create Docker images =========================================================================================="
#sbt -DbuildTarget=kubernetes -DbuildVersion=1.0.1-SNAPSHOT clean docker:publishLocal

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