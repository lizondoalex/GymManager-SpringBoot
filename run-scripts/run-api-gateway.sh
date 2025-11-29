#!/usr/bin/env bash

../api-gateway/mvnw -f ../api-gateway/pom.xml clean package -DskipTests

docker build -t api-gateway ../api-gateway

docker rm -f api-gateway 2>/dev/null

docker run -d \
  --name api-gateway \
  --network internal \
  -e AUTH_SERVICE_URL=http://auth-service:4005 \
  -p 4004:4004 \
  api-gateway

echo "End of run-api-gateway.sh"
