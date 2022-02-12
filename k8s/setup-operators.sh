#!/bin/bash
set -euxo pipefail

kubectl create namespace monitoring

helm install prometheus bitnami/kube-prometheus --namespace monitoring --version 6.6.5

kubectl create secret generic datasource-secret --from-file=grafana/datasource-secret.yaml --namespace monitoring

#Create jvm dashboard config map
curl -o grafana/jvm-prometheus-dashboard.json https://grafana.com/api/dashboards/4701/revisions/latest/download
sed -i 's/"${DS_PROMETHEUS}"/{ "type": "prometheus", "uid": "Iz2Nvy-nk" }/g' grafana/jvm-prometheus-dashboard.json
kubectl create configmap jvm-prometheus-dashboard --from-file=grafana/jvm-prometheus-dashboard.json --namespace monitoring

#Create kubernetes dashboard config map
curl -o grafana/kubernetes-prometheus-dashboard.json https://grafana.com/api/dashboards/10000/revisions/latest/download
sed -i 's/"${DS_PROMETHEUS}"/{ "type": "prometheus", "uid": "Iz2Nvy-nk" }/g' grafana/kubernetes-prometheus-dashboard.json
kubectl create configmap kubernetes-prometheus-dashboard --from-file=grafana/kubernetes-prometheus-dashboard.json --namespace monitoring

helm install grafana bitnami/grafana --namespace monitoring --version 7.6.7 -f grafana/values.yml
