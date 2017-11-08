#!/usr/bin/env bash

echo "Start minikube ================================================================================================"
(minikube delete || true) &>/dev/null && \
minikube start --insecure-registry "192.168.99.1:5000" --memory 8192 && \
eval $(minikube docker-env)

echo "Deploy Cassandra =============================================================================================="
kubectl create -f deploy/kubernetes/resources/cassandra && \
deploy/kubernetes/scripts/kubectl-wait-for-pods && \
kubectl exec cassandra-0 -- nodetool status