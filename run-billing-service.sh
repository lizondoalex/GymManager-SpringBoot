#!/usr/bin/env bash

./billing-service/mvnw -f billing-service/pom.xml clean install -DskipTests

docker build -t billing-service ./billing-service

docker rm -f billing-service 2>/dev/null

docker run -d \
  --name billing-service \
  --network internal \
  -p 4001:4001 \
  -p 9001:9001 \
  billing-service

echo "End of run-billing-service.sh"