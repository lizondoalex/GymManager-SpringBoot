#!/usr/bin/env bash

../patient-service/mvnw -f ../patient-service/pom.xml clean package -DskipTests

docker build -t patient-service ../patient-service

docker rm -f patient-service 2>/dev/null

docker run -d \
  --name patient-service \
  --network internal \
  -p 4000:4000 -e SPRING_DATASOURCE_URL=jdbc:postgresql://patient-service-db:5432/db \
  -e SPRING_DATASOURCE_USERNAME=admin_user \
  -e SPRING_DATASOURCE_PASSWORD=password \
  -e BILLING_SERVICE_ADDRESS=billing-service \
  -e BILLING_SERVICE_GRPC_PORT=9001 \
  -e SPRING_JPA_HIBERNATE_DLL_AUTO=update \
  -e SPRING_SQL_INIT_MODE=always \
  -e SPRING_KAFKA_BOOTSTRAP_SERVERS=kafka:9092 \
  patient-service

echo "End of run-patient-service.sh"
