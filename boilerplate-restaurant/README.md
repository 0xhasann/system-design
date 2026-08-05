# Restaurant Table Reservation System — Spring Boot Starter

A minimal Spring Boot starter project. Implement the Restaurant Table
Reservation System API on top of this scaffold.

## Prerequisites

- Java 17+
- Maven 3.8+

## Quickstart

```bash
# Build
mvn clean install

# Run tests
mvn test

# Start the application
mvn spring-boot:run
```

The app listens on `http://localhost:8080`.

## What's included

- Spring Boot 3.2.5, Java 17
- Dependencies: `spring-boot-starter-web`, `spring-boot-starter-data-jpa`,
  `spring-boot-starter-validation`, `h2` (runtime), `spring-boot-starter-test`
- In-memory H2 database — no DB setup required
- H2 console at `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:reservations`)
- A context-loads sanity test

## Project structure

```
boilerplate-restaurant/
├── pom.xml
└── src/
    ├── main/
    │   ├── java/com/shurutech/reservation/
    │   │   └── ReservationApplication.java
    │   └── resources/
    │       └── application.properties
    └── test/
        └── java/com/shurutech/reservation/
            └── ReservationApplicationTests.java
```

Build your entities, repositories, services, controllers, DTOs, and tests on
top of this structure as you see fit.

## Seeding data

You'll need some tables (and possibly a few existing reservations) in the
database to exercise the API. Spring Boot supports several seeding mechanisms
— pick whichever you're most comfortable with.

For the dataset we'd like you to load, see [`TEST_DATA.md`](TEST_DATA.md).

### Option 1 — `CommandLineRunner` bean (Java, type-safe)

Runs once at application startup. Uses your repositories, so refactors stay safe.

```java
@Bean
CommandLineRunner seed(YourRepository repo) {
    return args -> {
        if (repo.count() == 0) {
            repo.save(new YourEntity(/* your fields */));
        }
    };
}
```

### Option 2 — `src/main/resources/data.sql`

Spring Boot auto-runs this file against the configured datasource on startup.

```sql
INSERT INTO your_table_name (col_a, col_b) VALUES ('value1', 42);
```

When `spring.jpa.hibernate.ddl-auto=update` (the default in this boilerplate),
also add this to `application.properties` so the SQL runs *after* the schema
is generated from your entities:

```properties
spring.jpa.defer-datasource-initialization=true
```

### Option 3 — Test fixtures via `@BeforeEach`

For tests, seed each scenario fresh inside the test class. This is the most
common pattern for test fixtures.

```java
@BeforeEach
void setUp() {
    yourRepository.save(new YourEntity(/* your fields */));
}
```
