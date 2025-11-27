#!/usr/bin/env bash

../auth-service/mvnw -f ../auth-service/pom.xml clean package -DskipTests

docker build -t auth-service ../auth-service

docker rm -f auth-service 2>/dev/null

docker run -d \
  --name auth-service \
  --network internal \
  -e SPRING_DATASOURCE_PASSWORD=password \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://auth-service-db:5432/db \
  -e SPRING_DATASOURCE_USERNAME=admin_user \
  -e SPRING_SQL_INIT_MODE=always \
  -e SPRING_JPA_DEFER_DATASOURCE_INITIALIZATION=true \
  -e JWT_SECRET=MzU3OThhN2E5NzU2NzhjNDk2OGU0ODMzMzU1YTA5OGU= \
  auth-service:latest
# I KNOW THIS SHOULD BE SECRET BUT THE POINT OF
# HAVING SCRIPTS IS A WAY TO REMEMBER HOW THE PROJECT
# BUILDS SO THIS SHOULD ALSO BE IN HERE IN THIS CASE

echo "End of run-auth-service.sh"
