# PayFlow

A payments API built with Java and Spring Boot. Handles wallet management, 
fund transfers, and transaction history with JWT-based authentication.

## Tech Stack

**Backend**
- Java 17, Spring Boot
- Spring Security, JWT
- Hibernate / JPA
- PostgreSQL
- Maven

**Frontend**
- React, JavaScript
- Axios

**Infrastructure**
- Docker
- AWS (EC2, S3)
- Jenkins CI/CD

## Getting Started

### Prerequisites
- Java 17+
- PostgreSQL
- Maven

### Run Locally

```bash
git clone https://github.com/salmankareem1/payflow.git
cd payflow
```

Configure your database in `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/payflow
spring.datasource.username=your_username
spring.datasource.password=your_password
```

Then run:

```bash
./mvnw spring-boot:run
```

Frontend:

```bash
cd frontend
npm install
npm start
```

## API Reference

### Auth
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /auth/login | Returns JWT token |

### Wallets
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /api/wallet | Create wallet |
| GET | /api/wallets | List all wallets |
| GET | /api/wallet/{id} | Get wallet by ID |
| PUT | /api/wallet/{id} | Update wallet |
| DELETE | /api/wallet/{id} | Delete wallet |

### Transactions
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /api/transaction | Transfer funds between wallets |
| GET | /api/transactions | List all transactions |
| GET | /api/wallet/{id}/transactions | Get transactions for a wallet |

## Architecture

The transfer flow uses optimistic locking on the Wallet entity to handle 
concurrent requests safely. If two transfers targeting the same wallet 
arrive simultaneously, one will retry rather than overwrite the other's 
changes. Failed retries surface a clear error to the caller.

Authentication is stateless. A JWT is issued on login and validated on 
every subsequent request via a filter that runs before the Spring Security 
authorisation layer.

## In Progress

- JUnit / Mockito test coverage for service layer
- Kafka event publishing for transaction events
- Redis caching for wallet lookups
- Custom exception hierarchy replacing raw RuntimeException throws
