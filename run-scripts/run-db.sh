#!/usr/bin/env bash

docker rm -f patient-service-db 2>/dev/null

docker run -d \
  --name patient-service-db \
  --network internal \
  -p 5000:5432 \
  -v /home/lizond/Documents/prueba/spring/database:/var/lib/postgresql \
  -e POSTGRES_DB=db \
  -e POSTGRES_PASSWORD=password \
  -e POSTGRES_USER=admin_user \
  postgres:latest

echo "End of run-db.sh"
