# Hospital Management System (HMS) - Microservices Architecture

## Overview

The Hospital Management System is built using a microservices architecture with independent services for patient management, doctor scheduling, appointment booking, billing, payments, prescriptions, and notifications. Each service follows a Database-per-Service pattern and communicates via REST APIs or asynchronous Kafka events. All services are containerized using Docker and orchestrated using Docker Compose or Kubernetes (Minikube).

---

## Microservices Overview

| Service Name                | Description                                                         | Port |
| --------------------------- | ------------------------------------------------------------------- | ---- |
| Patient Service             | Manages patient records (CRUD, filtering, PII masking)              | 8081 |
| Doctor & Scheduling Service | Manages doctors, departments, and availability slots                | 8082 |
| Appointment Service         | Handles appointment booking, rescheduling, and cancellation         | 8083 |
| Billing Service             | Generates bills based on completed appointments                     | 8084 |
| Prescription Service        | Issues prescriptions linked to appointments                         | 8085 |
| Payment Service             | Manages payments, refunds, and idempotent charge handling           | 8086 |
| Notification Service        | Sends email/SMS confirmations and reminders                         | 8087 |
| API Gateway                 | Routes all external API requests to the corresponding microservices | 8091 |

---

## Tech Stack

**Backend:**

* Java 17
* Spring Boot 3.x
* Spring Cloud Gateway
* Spring Data JPA
* MySQL 8.0
* Apache Kafka
* Docker and Docker Compose
* Kubernetes (Minikube)

**Monitoring & Observability:**

* Prometheus
* Grafana
* Actuator Health Checks

**API Documentation:**

* OpenAPI 3.0 / Swagger UI (via `springdoc-openapi-starter-webmvc-ui`)

---

## Architecture Overview

Each service runs independently and communicates through REST APIs or Kafka topics. The API Gateway handles external requests and forwards them to the appropriate service.

```
[Client] --> [API Gateway] --> [Microservices] --> [Databases]
```

**Data Ownership & Communication Flow:**

* **Patient Service:** Source of truth for patient data.
* **Doctor Service:** Source of truth for doctor data and scheduling.
* **Appointment Service:** Uses patient and doctor read models to create appointments.
* **Billing Service:** Generates bills when appointments are completed.
* **Payment Service:** Processes payments and issues refunds.
* **Notification Service:** Listens to Kafka events and sends confirmations or reminders.

---

## Key Features

1. **CRUD Operations with Pagination and Filtering**
2. **PII Masking** for sensitive data like email and phone numbers.
3. **OpenAPI 3.0 Integration** for API documentation.
4. **Event-Driven Notifications** using Kafka.
5. **Database-per-Service Design** to ensure isolation.
6. **Standardized Error Schema:**

```json
{
  "code": 500,
  "message": "Error message",
  "correlationId": "uuid"
}
```

---

## Containerization with Docker

Each service includes a `Dockerfile`:

```dockerfile
FROM eclipse-temurin:17-jdk
WORKDIR /app
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
EXPOSE 8081
ENTRYPOINT ["java","-jar","/app.jar"]
```

### Docker Compose

The `docker-compose.yml` file builds and runs all services along with their MySQL containers and the API Gateway.

**Commands:**

```bash
# Build and start all containers
docker-compose up --build

# View running containers
docker ps

# View logs
docker logs <container-name> --tail 100
```

**API Gateway URL:**

```
http://localhost:8091/
```

---

## Kubernetes Deployment (Minikube)

Each service has its own deployment manifest including:

* Deployment YAML (with resource limits and probes)
* Service YAML (ClusterIP for internal access)
* ConfigMap/Secret YAML (for credentials)
* PersistentVolumeClaim (for MySQL persistence)

**Commands:**

```bash
kubectl apply -f patient-deployment.yaml
kubectl apply -f patient-service.yaml
kubectl get pods
kubectl get services
kubectl get deployments
```

---

## ER Diagrams

* Each microservice has its own ER diagram (refer to `/docs/er-diagrams/`).
* Global ER diagram shows entity relationships and ownership.

---

## Context Map

| Service              | Owns         | Reads From      | Publishes Events To   |
| -------------------- | ------------ | --------------- | --------------------- |
| Patient Service      | Patient      | -               | Appointment           |
| Doctor Service       | Doctor       | -               | Appointment           |
| Appointment Service  | Appointment  | Patient, Doctor | Notification, Billing |
| Billing Service      | Bill         | Appointment     | Payment               |
| Payment Service      | Payment      | Billing         | Notification          |
| Prescription Service | Prescription | Appointment     | -                     |
| Notification Service | Notification | All (Kafka)     | -                     |

---

## Health Checks and Monitoring

* Health endpoints: `/actuator/health`
* Metrics: `/actuator/metrics`
* Integrated with Prometheus and Grafana.

---

## Testing the APIs

1. Import the Postman collection from `/docs/HMS-APIs.postman_collection.json`.
2. Example endpoints:

   * Patient: `http://localhost:8091/api/v1/patients`
   * Doctor: `http://localhost:8091/api/v1/doctors`
   * Appointment: `http://localhost:8091/api/v1/appointments`
   * Billing: `http://localhost:8091/api/v1/bills`
   * Payment: `http://localhost:8091/api/v1/payments`
   * Notification: `http://localhost:8091/api/v1/notifications`

---

## Future Enhancements

* Implement distributed tracing (Zipkin/Jaeger).
* Add OAuth2 authentication (Keycloak).
* Introduce caching with Redis.
* Add resilience with Resilience4j.

---

## Author

Group 23
BITS Pilani Scalable Services Project — Scalable Microservices Architecture
