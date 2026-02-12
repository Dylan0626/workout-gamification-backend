# Workout Gamification Backend (Event-Driven Microservices)

A backend system that records user workouts and awards achievements asynchronously using Kafka.  
Built as two Spring Boot microservices with PostgreSQL databases and Flyway migrations.

---

## Services

### 1) workout-service (port 8081)
- Accepts workout submissions via REST
- Persists workouts to Postgres
- Publishes a `workout.completed` Kafka event

### 2) achievement-service (port 8082)
- Consumes `workout.completed` events from Kafka
- Updates per-user stats (workout count, calorie total, streak)
- Awards achievements using idempotent processing

---

## Architecture (High Level)

Client  
→ workout-service (REST API)  
→ Postgres (workoutdb)  
→ Kafka Topic: `workout.completed`  
→ achievement-service  
→ Postgres (achievementdb)  
→ achievements REST API

---

## Tech Stack
- Java 17, Spring Boot
- Spring Web (REST APIs)
- Spring Data JPA + Hibernate
- Apache Kafka (Confluent Docker image)
- PostgreSQL 16
- Flyway database migrations
- Docker Compose infrastructure
- JUnit + Mockito testing

---

## Local Setup

### Prerequisites
You must have installed:
- Docker + Docker Compose
- Java 17

---

## Start Infrastructure (Kafka + Postgres)

From the project root (where `docker-compose.yml` exists):

```bash
docker compose up -d
```

Verify containers are running:

```bash
docker ps
```

(Optional) Kafka UI will be available at:

http://localhost:8085

---

## Run the Services

Open two terminals.

### Terminal 1 — workout-service

```bash
cd services/workout-service
./mvnw spring-boot:run
```

### Terminal 2 — achievement-service

```bash
cd services/achievement-service
./mvnw spring-boot:run
```

---

## End-to-End Demo (Kafka Flow)

### 1) Submit a workout (workout-service)

```bash
curl -X POST http://localhost:8081/workouts \
  -H "Content-Type: application/json" \
  -d '{"userId":"dylan","workoutType":"RUN","durationMinutes":5,"caloriesBurned":50}'
```

Run this multiple times to trigger milestone achievements.

---

### 2) Retrieve achievements (achievement-service)

```bash
curl http://localhost:8082/achievements/dylan
```

Expected achievements include:
- FIRST_WORKOUT
- THREE_WORKOUTS
- CALORIES_1000
- STREAK_3

---

## Key Features
- Event-driven architecture: workout-service publishes Kafka events and achievement-service consumes them asynchronously.
- Database-per-service design: each microservice owns its own Postgres database.
- Idempotent processing: achievement-service prevents duplicate event handling using stored event IDs.
- Flyway migrations: schema is created automatically on startup.

---

## Future Improvements
- Swagger/OpenAPI documentation
- Outbox pattern for guaranteed event delivery
- Dead Letter Queue (DLQ) + retry strategy for Kafka consumers
- Authentication (JWT) and user identity management
- Containerize services using Dockerfiles
