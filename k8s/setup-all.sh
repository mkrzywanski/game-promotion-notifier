#!/bin/bash
set -euxo pipefail

./setup-helm-repositories.sh
./setup-operators.sh
helm dependency update backend-app 

./upload-fresh-images.sh

./run-helm-charts.sh
