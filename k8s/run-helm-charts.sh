#!/bin/bash
set -euxo pipefail

helm install rabbitmq-posts bitnami/rabbitmq -f ./rabbitmq/values.yml --version 8.27.0

helm install pn-site-scrapper backend-app -f pn-site-scrapper/values.yml
