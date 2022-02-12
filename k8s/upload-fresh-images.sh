#!/bin/bash
set -euxo pipefail
minikube image load scrapper-app:latest --overwrite=true