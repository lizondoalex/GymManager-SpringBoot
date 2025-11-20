#!/usr/bin/env bash

./patient-service/mvnw -f patient-service/pom.xml clean package -DskipTests

docker build -t patient-service ./patient-service

docker rm -f patient-service 2>/dev/null

docker run -d \
  --name patient-service \
  --network internal \
  -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://patient-service-db:5432/patientdb \
  -e SPRING_DATASOURCE_USERNAME=user \
  -e SPRING_DATASOURCE_PASSWORD=password \
  -e BILLING_SERVICE_ADDRESS=billing-service \
  -e BILLING_SERVICE_GRPC_PORT=9001 \
  patient-service

echo "End of run-patient-service.sh"