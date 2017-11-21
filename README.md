# Deploy
```bash
# required for new-minikube and deploy
export REGISTRY=10.15.4.117:5000

# once-off
./deploy/kubernetes/scripts/install --new-minikube
watch minikube status
minikube dashboard
./deploy/kubernetes/scripts/install --cassandra

# optional
./deploy/kubernetes/scripts/install --build-sbt
./deploy/kubernetes/scripts/install --deploy

# once-off
./deploy/kubernetes/scripts/install --nginx

# optional
minikube service nginx-ingress --url
./deploy/kubernetes/scripts/install --delete
```
