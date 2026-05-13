# PayFlow

A production-grade payments API built with Java 17 and Spring Boot 3.

Covers the core concerns of a real financial backend: atomic fund transfers,
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
- Apache Kafka (KRaft mode)
- Redis
- Maven

**Frontend**

- React 19, TypeScript
- Tailwind CSS
- Axios
- React Router v6
- React Testing Library

**Infrastructure**

- Docker (Kafka + Redis via docker-compose)
- AWS (EC2, S3) — planned

## Getting Started

### Prerequisites

- Java 17+
- PostgreSQL running locally
- Maven
- Node.js 18+
- Docker Desktop (for Kafka and Redis)

### Run Locally

```bash
git clone https://github.com/salmankareem1/payflow.git
cd payflow
```

Start Kafka and Redis:

```bash
docker-compose up -d
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

Run the backend:

```bash
./mvnw spring-boot:run
```

Run the frontend:

```bash
cd frontend
npm install
npm start
```

API runs on `http://localhost:8080`.
Frontend runs on `http://localhost:3000`.
Swagger UI available at `http://localhost:8080/swagger-ui.html`.

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
Every transfer involves three database writes: debit the sender, credit
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
because the version is no longer 5. Hibernate throws
ObjectOptimisticLockingFailureException. The service catches this and retries
up to three times before returning an error to the caller. Money cannot be
double-spent.

**Reference IDs**
Every transaction is assigned a UUID-based reference ID generated server-side.
This gives every transfer a unique, traceable identifier independent of the
database primary key.

**Kafka Event Publishing**
After every successful transfer, a TransactionEvent is published to the
`payflow.transaction.events` Kafka topic. The transfer service has no
knowledge of downstream consumers. Notification, fraud detection, and
analytics services can subscribe independently. Events are persisted to
disk and survive application restarts.

**Redis Caching**
Wallet lookups are cached in Redis using @Cacheable. Cache entries are
evicted on every update or delete using @CacheEvict. Wallet balances
during transfers are deliberately excluded from the cache to preserve
the integrity of the optimistic locking mechanism.

## Error Handling

The API returns specific HTTP status codes for every error condition.
A single @RestControllerAdvice handler maps each exception type to the
correct response.

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
- The JWT secret is injected via @Value from application.properties,
  never hardcoded in source
- Wallet creation accepts only userId and currency from the client.
  Balance is always initialised to zero server-side
- CSRF protection is disabled, correct for a stateless REST API using
  token-based authentication
- CORS is configured centrally via a CorsConfigurationSource bean

## Testing

**Backend** — JUnit 5 + Mockito — 17 tests

TransactionService (10 tests):

- Successful transfer with correct balance updates verified
- Insufficient funds: exception thrown, no database writes
- Same wallet rejection before any database call
- Null, zero, and negative amount validation
- Sender and receiver not found
- Currency mismatch: no database writes
- Exact balance transfer edge case

WalletService (7 tests):

- Get wallet by ID: found and not found
- Get all wallets
- Delete wallet: exists and not found
- Update wallet: exists and not found

**Frontend** — React Testing Library — 7 tests

LoginPage (7 tests):

- Form renders correctly
- User input on username and password fields
- Loading state during form submission
- Successful login redirect and token storage in sessionStorage
- 401 error message displayed correctly
- 500 generic error message displayed correctly

## Frontend

The React TypeScript frontend provides a complete interface for PayFlow.

- Login page with JWT authentication flow
- Protected routes, redirect to login if unauthenticated
- Dashboard with wallet cards showing live balances
- Create wallet form
- Transfer funds form with success and error feedback
- Recent transactions table with reference IDs and status badges
- Token stored in sessionStorage, cleared on logout
- Axios interceptor attaches JWT to every request automatically

## Project Status

Actively in development.

- ✅ Phase 1 — Bug fixes and production hardening
- ✅ Phase 2 — Custom exceptions, ResponseEntity, DTO layer, centralised CORS
- ✅ Phase 3 — Swagger/OpenAPI, Kafka event publishing, Redis caching
- ✅ Phase 4 — React TypeScript frontend, JWT auth flow, dashboard
- ✅ Phase 5 — Unit tests, JUnit 5 backend, React Testing Library frontend
- ⏳ Phase 6 — Docker full stack, deployment

## Author

Salman Abdul Kareem
Full Stack Engineer — Java Spring Boot, React TypeScript
Dublin, Ireland
[LinkedIn](https://www.linkedin.com/in/salman-kareem)
