#!/bin/bash
set -euxo pipefail

kubectl create namespace logging

helm install elasticsearch bitnami/elasticsearch --version 17.9.2 --namespace logging -f logging/elasticsearch/values.yml

helm install kibana bitnami/kibana --version 9.3.3 --namespace logging -f logging/kibana/values.yml

kubectl apply -f logging/fluentd/elasticsearch-output.yml --namespace logging
kubectl apply -f logging/fluentd/json-log-parser.yml --namespace logging
helm install fluentd bitnami/fluentd --version 5.0.2 --namespace logging -f logging/fluentd/values.yml
