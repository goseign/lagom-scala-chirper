#!/usr/bin/env bash

echo 'Start minikube:'
echo '==============='
echo '(minikube delete || true) &>/dev/null && \'
echo 'minikube start --insecure-registry "192.168.99.1:5000" --memory 8192 && \'
echo 'eval $(minikube docker-env)'
echo ''
echo "Deploy Cassandra:"
echo '================='
echo 'kubectl create -f deploy/kubernetes/resources/cassandra && \'
echo 'deploy/kubernetes/scripts/kubectl-wait-for-pods && \'
echo 'kubectl exec cassandra-0 -- nodetool status'