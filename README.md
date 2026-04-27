# PayFlow

A production-grade payments API built with Java 17 and Spring Boot 3.

Covers the core concerns of a real financial backend — atomic fund transfers,
concurrent request handling, JWT-based authentication, and a clean REST API
with proper HTTP semantics throughout.

This is a personal project built to go deep on backend engineering, not broad.
Every design decision has a reason behind it.

## Tech Stack

**Backend**

- Java 17, Spring Boot 3
- Spring Security, JWT (JJWT)
- Spring Data JPA, Hibernate
- PostgreSQL
- Maven

**Frontend**

- React, TypeScript
- Axios

**Infrastructure (in progress)**

- Docker
- AWS (EC2, S3)

## Getting Started

### Prerequisites

- Java 17+
- PostgreSQL running locally
- Maven

### Run Locally

```bash
git clone https://github.com/salmankareem1/payflow.git
cd payflow
```

Create a database named `payflowdb` in PostgreSQL, then configure
`src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/payflowdb
spring.datasource.username=your_username
spring.datasource.password=your_password
app.jwt.secret=your-secret-key-minimum-32-characters
app.jwt.expiration-ms=3600000
```

Run the application:

```bash
./mvnw spring-boot:run
```

Frontend:

```bash
cd frontend
npm install
npm start
```

API runs on `http://localhost:8080`.
Frontend runs on `http://localhost:3000`.

## API Reference

All endpoints except `/auth/login` require a valid JWT token in the
Authorization header: `Authorization: Bearer <token>`

### Authentication

| Method | Endpoint    | Description                          |
| ------ | ----------- | ------------------------------------ |
| POST   | /auth/login | Authenticate and receive a JWT token |

### Wallets

| Method | Endpoint         | Description           | Status         |
| ------ | ---------------- | --------------------- | -------------- |
| POST   | /api/wallet      | Create a new wallet   | 201 Created    |
| GET    | /api/wallets     | List all wallets      | 200 OK         |
| GET    | /api/wallet/{id} | Get a wallet by ID    | 200 OK         |
| PUT    | /api/wallet/{id} | Update wallet details | 200 OK         |
| DELETE | /api/wallet/{id} | Delete a wallet       | 204 No Content |

### Transactions

| Method | Endpoint                      | Description                    | Status |
| ------ | ----------------------------- | ------------------------------ | ------ |
| POST   | /api/transaction              | Transfer funds between wallets | 200 OK |
| GET    | /api/transactions             | List all transactions          | 200 OK |
| GET    | /api/wallet/{id}/transactions | Get transactions for a wallet  | 200 OK |

## How Transfers Work

Fund transfers are the core of PayFlow. Three things make them reliable.

**Atomicity**
Every transfer involves three database writes — debit the sender, credit
the receiver, record the transaction. All three are wrapped in a single
@Transactional boundary. If anything fails mid-transfer, all writes are
rolled back. No partial state is ever committed to the database.

**Concurrency**
The Wallet entity carries a @Version field managed by Hibernate. On every
update, Hibernate generates:

```sql
UPDATE wallet SET balance = ?, version = ? WHERE id = ? AND version = ?
```

If two concurrent transfers both read version 5 and race to save, the first
succeeds and increments the version to 6. The second update matches zero rows
because the version is no longer 5 — Hibernate throws
ObjectOptimisticLockingFailureException. The service catches this and retries
up to three times before returning an error to the caller. Money cannot be
double-spent.

**Reference IDs**
Every transaction is assigned a UUID-based reference ID generated server-side.
This gives every transfer a unique, traceable identifier independent of the
database primary key.

## Error Handling

The API returns specific HTTP status codes for every error condition.
A single @RestControllerAdvice handler maps each exception type to the
correct response — nothing returns 400 when a 404 or 500 is appropriate.

| Scenario                | Status Code               |
| ----------------------- | ------------------------- |
| Wallet not found        | 404 Not Found             |
| Insufficient funds      | 422 Unprocessable Entity  |
| Duplicate transaction   | 409 Conflict              |
| Currency mismatch       | 400 Bad Request           |
| Same wallet transfer    | 400 Bad Request           |
| Validation failure      | 400 Bad Request           |
| Unexpected server error | 500 Internal Server Error |

## Security

- JWT tokens are validated on every request via a filter that runs before
  the Spring Security authorisation layer
- The JWT secret is injected via @Value from application.properties —
  never hardcoded in source
- Wallet creation accepts only userId and currency from the client —
  balance is always initialised to zero server-side
- CSRF protection is disabled — correct for a stateless REST API using
  token-based authentication
- CORS is configured centrally via a CorsConfigurationSource bean

## Project Status

Actively in development.

- ✅ Phase 1 — Bug fixes and production hardening
- ✅ Phase 2 — Custom exceptions, ResponseEntity, DTO layer, centralised CORS
- 🔄 Phase 3 — Swagger/OpenAPI, Kafka event publishing, Redis caching
- ⏳ Phase 4 — React TypeScript frontend complete
- ⏳ Phase 5 — Docker, deployment

## Author

Salman Abdul Kareem
Full Stack Engineer — Java Spring Boot, React TypeScript
Dublin, Ireland
[LinkedIn](https://www.linkedin.com/in/salman-kareem)
