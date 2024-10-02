#!/usr/bin/env sh

echo "Creating Kubernetes cluster"
kind create cluster --config kind-config.yml

echo "Installing NGINX Ingress"
kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/main/deploy/static/provider/kind/deploy.yaml

echo "Waiting for NGINX to be ready"
sleep 10
kubectl wait --namespace ingress-nginx \
  --for=condition=ready pod \
  --selector=app.kubernetes.io/component=controller \
  --timeout=90s

echo "Cluster ready"