#!/usr/bin/env bash

../analytics-service/mvnw -f ../analytics-service/pom.xml clean package -DskipTests

docker build -t analytics-service ../analytics-service

docker rm -f analytics-service 2>/dev/null

docker run -d \
  --name analytics-service \
  --network internal \
  -e SPRING_KAFKA_BOOTSTRAP_SERVERS=kafka:9092 \
  -p 4002:4002 \
  analytics-service

echo "End of run-analytics-service.sh"
