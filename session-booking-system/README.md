# Global Class Offering Booking System

Backend service for managing course offerings, sessions, and bookings for a live-learning platform.

## Tech Stack

- Java 21
- Spring Boot 3
- Spring Data JPA
- MySQL
- Docker

## Features

- Create offerings
- Add sessions to offerings
- View offerings and sessions in the parent's local timezone
- Book offerings
- View booked offerings
- Prevent overlapping bookings
- Idempotent booking requests
- Concurrency handling using optimistic and pessimistic locking

## Running the Application

Build the application:

```bash
mvn clean package
```

Run using Docker:

```bash
docker-compose up --build
```

Application URL:

```text
http://localhost:8080/api
```

## API Testing

Postman collection is available under:

```text
postman/Global-Booking-System.postman_collection.json
```

Import the collection into Postman and execute the available requests.

## Key Design Decisions

- Session timings are stored in UTC.
- Teachers create sessions using their local timezone.
- Parents view sessions in their own timezone.
- Booking happens at the offering level.
- Pessimistic locking is used during booking to prevent conflicting concurrent requests.
- Optimistic locking is implemented using `@Version`.
- Idempotency keys prevent duplicate booking creation during retries.

## Assumptions

- Authentication and authorization are out of scope.
- Offerings do not have capacity limits.
- Parents book an entire offering rather than individual sessions.
- Session overlap is checked across all sessions belonging to booked offerings.

## Project Structure

```text
src/main/java/com/assignment/booking

├── controller
├── service
├── serviceimpl
├── repository
├── entity
├── dto
├── mapper
├── exception
├── util
└── config
```
