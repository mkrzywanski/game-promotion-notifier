#!/bin/bash
set -euxo pipefail
./setup-helm-repositories.sh
./setup-operators.sh
helm dependency update backend-app 

./upload-fresh-images.sh

# helm install mongodb bitnami/mongodb -f ./mongodb/values.yml --version 11.0.0

# helm install mongodb-matching-service bitnami/mongodb -f ./mongodb/values.yml --version 11.0.0

# helm install mongodb-scrapping-service bitnami/mongodb -f ./mongodb/values.yml --version 11.0.0

# helm install elasticsearch elastic/elasticsearch -f ./elasticsearch/values.yml --version 7.16.3

./run-helm-charts.sh
