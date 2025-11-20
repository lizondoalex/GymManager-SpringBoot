#!/usr/bin/env bash

docker rm -f patient-service-db 2>/dev/null

docker run -d \
  --name patient-service-db \
  --network internal \
  -p 5432:5432 \
  -e POSTGRES_DB=patientdb \
  -e POSTGRES_USER=user \
  -e POSTGRES_PASSWORD=password \
  -v pgdata:/var/lib/postgresql/data \
  postgres:latest

echo "End of run-db.sh"
