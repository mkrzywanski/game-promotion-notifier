#!/bin/bash

#DEFAULTS
KCHOST=http://localhost:8081
REALM=promotion-notifier
CLIENT_ID=pn-subscription-service
CLIENT_SECRET=2f3ab454-d44f-4149-af29-58145c47df5d
UNAME=test
PASSWORD=test

while [ $# -gt 0 ]; do
  case "$1" in
    --host|-h)
      KCHOST="$2"
      ;;
    --realm|-r)
      REALM="$2"
      ;;
    --client_id|-c)
      CLIENT_ID="$2"
      ;;
    --client_secret|-s)
      CLIENT_SECRET="$2"
      ;;
    --user|-u)
      UNAME="$2"
      ;;
    --password|-p)
      PASSWORD="$2"
      ;;
    *)
      printf "***************************\n"
      printf "* Error: Invalid argument.*\n"
      printf "***************************\n"
      exit 1
  esac
  shift
  shift
done

ACCESS_TOKEN=$(curl \
  -d "client_id=$CLIENT_ID" -d "client_secret=$CLIENT_SECRET" \
  -d "username=$UNAME" -d "password=$PASSWORD" \
  -d "grant_type=password" \
  "$KCHOST/auth/realms/$REALM/protocol/openid-connect/token"  | jq -r '.access_token')

echo $ACCESS_TOKEN