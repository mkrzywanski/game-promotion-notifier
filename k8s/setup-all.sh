#!/bin/sh
./setup-helm-repositories.sh

helm dependency update backend-app 

minikube image load scrapper-app:latest

# helm install mongodb bitnami/mongodb -f ./mongodb/values.yml --version 11.0.0

# helm install mongodb-matching-service bitnami/mongodb -f ./mongodb/values.yml --version 11.0.0

# helm install mongodb-scrapping-service bitnami/mongodb -f ./mongodb/values.yml --version 11.0.0

# helm install elasticsearch elastic/elasticsearch -f ./elasticsearch/values.yml --version 7.16.3

./run-helm-charts.sh
