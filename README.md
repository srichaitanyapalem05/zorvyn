# Finance Data Processing and Access Control Dashboard

A clean, modular Spring Boot backend for managing financial records with role-based access control.

---

## Tech Stack

- Java 17
- Spring Boot 3.2
- Spring Data JPA (Hibernate)
- PostgreSQL
- Spring Security + JWT
- Lombok
- Hibernate Validator
- Swagger / OpenAPI (springdoc)

---

## Architecture

Strict layered architecture following SOLID principles:

```
controller → service (interface + impl) → repository → database
```

Package structure:
```
com.finance.dashboard
├── controller        # REST endpoints
├── service           # interfaces
│   └── impl          # implementations
├── repository        # JPA repositories
├── model             # JPA entities
├── dto               # request/response DTOs
├── mapper            # manual entity ↔ DTO mappers
├── security          # JWT filter, UserDetailsService, JwtUtil
├── config            # SecurityConfig, OpenApiConfig, DataInitializer
├── exception         # GlobalExceptionHandler + custom exceptions
└── util              # RecordSpecification (JPA Criteria)
```

---

## Database Schema

### roles
| Column | Type   |
|--------|--------|
| id     | BIGINT |
| name   | VARCHAR (ADMIN, ANALYST, VIEWER) |

### users
| Column   | Type    |
|----------|---------|
| id       | BIGINT  |
| name     | VARCHAR |
| email    | VARCHAR (unique) |
| password | VARCHAR |
| status   | VARCHAR (ACTIVE, INACTIVE) |
| role_id  | FK → roles |
| deleted  | BOOLEAN |

### financial_records
| Column      | Type    |
|-------------|---------|
| id          | BIGINT  |
| amount      | DECIMAL(15,2) |
| type        | VARCHAR (INCOME, EXPENSE) |
| category    | VARCHAR |
| date        | DATE    |
| description | VARCHAR |
| created_by  | FK → users |
| deleted     | BOOLEAN |

---

## API Endpoints

### Auth
| Method | Path         | Description       |
|--------|--------------|-------------------|
| POST   | /auth/login  | Login, get JWT    |

### Users (ADMIN only for write)
| Method | Path         | Role Required     |
|--------|--------------|-------------------|
| POST   | /users       | ADMIN             |
| GET    | /users       | ALL               |
| PUT    | /users/{id}  | ADMIN             |
| DELETE | /users/{id}  | ADMIN             |

### Records
| Method | Path           | Role Required       |
|--------|----------------|---------------------|
| POST   | /records       | ADMIN, ANALYST      |
| GET    | /records       | ALL (with filters)  |
| PUT    | /records/{id}  | ADMIN, ANALYST      |
| DELETE | /records/{id}  | ADMIN               |

Query params for GET /records: `type`, `category`, `startDate`, `endDate`, `page`, `size`, `sort`

### Dashboard
| Method | Path                      | Role Required  |
|--------|---------------------------|----------------|
| GET    | /dashboard/summary        | ALL            |
| GET    | /dashboard/category-wise  | ADMIN, ANALYST |
| GET    | /dashboard/monthly        | ADMIN, ANALYST |
| GET    | /dashboard/recent         | ALL            |

---

## RBAC Rules

| Role    | Permissions                              |
|---------|------------------------------------------|
| VIEWER  | GET endpoints only                       |
| ANALYST | GET records + dashboard + create/update records |
| ADMIN   | Full CRUD access                         |

---

## How to Run

1. Create the PostgreSQL database:
```sql
CREATE DATABASE finance_db;
```

2. Update credentials in `application.properties` if needed.

3. Build and run:
```bash
./mvnw spring-boot:run
```

4. Swagger UI: http://localhost:8080/swagger-ui.html

5. Seed an admin user via POST /users (roles are auto-seeded on startup with IDs 1=ADMIN, 2=ANALYST, 3=VIEWER).

---

## Assumptions

- Soft delete is used for both users and records (`deleted = false` flag).
- Passwords are BCrypt-encoded.
- JWT tokens expire after 24 hours (configurable via `app.jwt.expiration`).
- Roles are seeded automatically on first startup.
- Monthly trends use native PostgreSQL `EXTRACT` for year/month grouping.
