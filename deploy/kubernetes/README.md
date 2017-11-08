# Install minikube on MacOS

curl -LO https://storage.googleapis.com/kubernetes-release/release/`curl -s https://storage.googleapis.com/kubernetes-release/release/stable.txt`/bin/darwin/amd64/kubectl

chmod +x ./kubectl

sudo mv ./kubectl /usr/local/bin/kubectl

# Start minikube

(minikube delete || true) &>/dev/null && \
minikube start --memory 8192 && \
eval $(minikube docker-env)

# Deploy Cassandra

kubectl create -f deploy/kubernetes/resources/cassandra && \
deploy/kubernetes/scripts/kubectl-wait-for-pods && \
kubectl exec cassandra-0 -- nodetool status

# Create Docker images

sbt -DbuildTarget=kubernetes clean docker:publishLocal

# Deploy Chirper

kubectl create -f deploy/kubernetes/resources/chirper && \
deploy/kubernetes/scripts/kubectl-wait-for-pods && \
kubectl get all

# Deploy NGINX

kubectl create -f deploy/kubernetes/resources/nginx && \
deploy/kubernetes/scripts/kubectl-wait-for-pods && \
kubectl get pods

# Verify deployment

echo "Chirper UI (HTTP): $(minikube service --url nginx-ingress | head -n 1)" && \
echo "Chirper UI (HTTPS): $(minikube service --url --https nginx-ingress | tail -n 1)" && \
echo "Kubernetes Dashboard: $(minikube dashboard --url)"

# Clean up

(minikube delete || true) &>/dev/null
