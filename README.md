# 🛒 Microservices E-Commerce Backend
 
A production-grade distributed e-commerce backend built using **Spring Boot & Spring Cloud**, following real-world microservices architecture patterns.
 
This project demonstrates scalable system design, service-to-service communication, centralized security, event-driven architecture, and cloud-native best practices.
 
---
 
## 🚀 Project Overview
 
This system is designed as a distributed backend where each domain is isolated into its own microservice. Each service owns its data, communicates via REST (synchronous) or RabbitMQ (asynchronous), and is secured using JWT tokens issued by Keycloak.
 
---
 
## ✨ Features
 
- Independent domain services with clean boundaries
- API Gateway for centralized routing
- Service Discovery using Eureka
- Centralized authentication & RBAC via Keycloak
- JWT-based stateless authentication across all services
- Event-driven notifications via RabbitMQ
- Dockerized infrastructure
- Global exception handling with typed exceptions
- Stock management with deduct/restore on order lifecycle
 
---
 
## 🏗️ Architecture
 
```
                        Client
                           │
                           ▼
                     API Gateway (:8080)
                           │
       ┌───────────────────┼──────────────────────┐
       ▼                   ▼                      ▼
 User Service         Product Service         Cart Service
  (:8081)              (:8083)                 (:8082)
       │                   │                      │
       ▼                   ▼                      ▼
  PostgreSQL           PostgreSQL             PostgreSQL
 
                     Order Service
                      (:8084)
                    ┌────┴────┐
                    ▼         ▼
             Cart Service  Product Service
             (Feign)       (Feign - stock)
                    │
                    ▼
               RabbitMQ
                    │
                    ▼
          Notification Service
            (Email + Push)
 
         Eureka Server (Service Discovery)
         Keycloak (:8180)  — Identity Provider
```
 
### Architecture Principles
 
- **Database per service** — no shared schemas
- **Loose coupling** — services communicate via HTTP (Feign) or events (RabbitMQ)
- **Centralized routing** — all traffic flows through API Gateway
- **JWT propagation** — tokens issued by Keycloak, validated independently per service
- **Compensating transactions** — Keycloak rollback on DB failures in user registration
- **Price snapshot** — order items store price at time of purchase, immune to future price changes
 
---
 
## 🧰 Tech Stack
 
| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot |
| Gateway | Spring Cloud Gateway |
| Service Discovery | Eureka Server |
| Security | Spring Security + OAuth2 Resource Server |
| Identity & RBAC | Keycloak |
| Databases | PostgreSQL / MySQL |
| Messaging | RabbitMQ (AMQP) |
| HTTP Clients | OpenFeign |
| Containerization | Docker & Docker Compose |
| Build Tool | Maven |
 
---
 
## 📦 Microservices
 
### 🔹 API Gateway
- Central entry point for all client requests
- Dynamic routing to downstream services
- Handles cross-cutting concerns
 
### 🔹 Eureka Server
- Service registry for dynamic service discovery
- All services register on startup
 
### 🔹 User Service
- User registration with Keycloak + local DB dual-write
- Keycloak rollback (compensating transaction) on DB failure
- JWT-based profile retrieval (`getOrCreateUser`)
- Profile update (name, phone, profile image)
- Soft deactivation
- Address management (add, update, delete, set default)
- Global exception handler with typed exceptions (`EmailAlreadyExistsException`, `UserNotFoundException`, `KeycloakRollbackFailureException`, etc.)
 
### 🔹 Product Service
- Product CRUD with SKU auto-generation
- Category association
- Soft delete via status toggle
- Admin filtering with `Specification` (status, search, category, price range)
- Paginated responses
- Stock management with deduct/restore support
 
### 🔹 Cart Service
- User-scoped carts via `keycloakId`
- Add, update quantity, remove items, clear cart
- Product validation via synchronous Feign call
- Quantity ≤ 0 auto-removes the item
- JWT forwarded from gateway for authentication
 
### 🔹 Order Service
- Place order directly from active cart
- Price snapshot per order item
- Status lifecycle: `PENDING → CONFIRMED → SHIPPED → DELIVERED → CANCELLED`
- Validated status transitions (no illegal jumps)
- Stock deduction on placement, stock restoration on cancellation
- Order history per user
- Admin status update endpoint (ADMIN role only)
- Publishes order events to RabbitMQ on every status change
 
### 🔹 Notification Service
- Consumes order events from RabbitMQ
- Handles: `order.placed`, `order.confirmed`, `order.shipped`, `order.delivered`, `order.cancelled`
- Email service (structured, ready for JavaMail integration)
- Push notification service (structured, ready for FCM integration)
- Currently logs notifications — real providers pluggable without architectural changes
 
---
 
## 🔐 Security
 
Authentication & authorization is handled via:
 
- **Keycloak** for centralized identity management (realm: `ecom`)
- **Role-Based Access Control** — `USER` role assigned on registration, `ADMIN` role for privileged endpoints
- **JWT-based authentication** — tokens issued by Keycloak, validated via JWKS endpoint
- **Gateway-level enforcement** — all requests must carry a valid Bearer token
- **Per-service validation** — each service independently validates JWT using `issuer-uri`
- **`@PreAuthorize`** used at controller level (`isAuthenticated()`, `hasRole('ADMIN')`)
 
### Getting a Token (Postman)
 
```
POST http://localhost:8180/realms/ecom/protocol/openid-connect/token
Content-Type: application/x-www-form-urlencoded
 
grant_type=password
client_id=<your-client-id>
client_secret=<your-client-secret>
username=<registered-email>
password=<password>
```
 
Use the returned `access_token` as a Bearer token in all subsequent requests.
 
> **Note:** Keycloak client must have **Direct Access Grants** enabled. Users are registered with `username = email` — always use email in the username field.
 
---
 
## 🔄 Communication Patterns
 
### ✅ Synchronous (Feign)
 
| Caller | Target | Purpose |
|---|---|---|
| Cart Service | Product Service | Validate product existence & price |
| Order Service | Cart Service | Fetch cart items on order placement |
| Order Service | Product Service | Deduct / restore stock |
 
### ✅ Asynchronous (RabbitMQ)
 
| Publisher | Exchange | Routing Key | Consumer |
|---|---|---|---|
| Order Service | `order.exchange` | `order.placed` | Notification Service |
| Order Service | `order.exchange` | `order.confirmed` | Notification Service |
| Order Service | `order.exchange` | `order.shipped` | Notification Service |
| Order Service | `order.exchange` | `order.delivered` | Notification Service |
| Order Service | `order.exchange` | `order.cancelled` | Notification Service |
 
---
 
## 📋 API Reference
 
### User Service
 
| Method | Endpoint | Access | Description |
|---|---|---|---|
| POST | `/api/users/register` | Public | Register new user |
| GET | `/api/users/me` | Authenticated | Get profile |
| PATCH | `/api/users/me` | Authenticated | Update profile |
| DELETE | `/api/users/me` | Authenticated | Deactivate account |
| GET | `/api/users/me/addresses` | Authenticated | List addresses |
| POST | `/api/users/me/addresses` | Authenticated | Add address |
| PUT | `/api/users/me/addresses/{id}` | Authenticated | Update address |
| DELETE | `/api/users/me/addresses/{id}` | Authenticated | Delete address |
| PATCH | `/api/users/me/addresses/{id}/default` | Authenticated | Set default address |
 
### Product Service
 
| Method | Endpoint | Access | Description |
|---|---|---|---|
| GET | `/api/products` | Public | List active products (paginated) |
| GET | `/api/products/{id}` | Public | Get product by ID |
| POST | `/api/products/admin` | ADMIN | Create product |
| PATCH | `/api/products/admin/{id}/status` | ADMIN | Toggle active status |
| GET | `/api/products/admin` | ADMIN | Filtered product list |
| PUT | `/api/products/{id}/stock` | Internal | Deduct/restore stock |
 
### Cart Service
 
| Method | Endpoint | Access | Description |
|---|---|---|---|
| GET | `/api/cart` | Authenticated | Get user's cart |
| POST | `/api/cart/items` | Authenticated | Add item to cart |
| PATCH | `/api/cart/items/{productId}` | Authenticated | Update item quantity |
| DELETE | `/api/cart/items/{productId}` | Authenticated | Remove item |
| DELETE | `/api/cart` | Authenticated | Clear cart |
 
### Order Service
 
| Method | Endpoint | Access | Description |
|---|---|---|---|
| POST | `/api/orders` | Authenticated | Place order from cart |
| GET | `/api/orders` | Authenticated | Order history |
| GET | `/api/orders/{id}` | Authenticated | Get order by ID |
| DELETE | `/api/orders/{id}` | Authenticated | Cancel order |
| PATCH | `/api/orders/{id}/status` | ADMIN | Update order status |
 
---
 
## 🐳 Running the Project
 
### Prerequisites
 
- JDK 17+
- Maven
- Docker & Docker Compose
 
### Run with Docker Compose
 
```bash
docker compose up --build
```
 
### Run RabbitMQ (standalone)
 
```bash
docker run -d --name rabbitmq \
  -p 5672:5672 -p 15672:15672 \
  rabbitmq:management
```
 
RabbitMQ Management UI: `http://localhost:15672` (guest/guest)
 
### Run Manually
 
1. Start Eureka Server
2. Start Keycloak (`http://localhost:8180`)
3. Start User, Product, Cart, Order services
4. Start Notification Service
5. Start API Gateway
6. Access APIs via Gateway (`http://localhost:8080`)
 
---
 
## 📂 Project Structure
 
```
microservices-ecom/
│
├── gateway/                  # Spring Cloud Gateway
├── eureka/                   # Eureka Service Registry
├── user/                     # User + Address management
│   └── exception/            # Typed exceptions + GlobalExceptionHandler
├── product/                  # Product + Category + Stock
├── cart/                     # Cart + CartItems (user-scoped)
├── order/                    # Order lifecycle + status transitions
│   ├── client/               # Feign clients (Cart, Product)
│   └── messaging/            # RabbitMQ publisher + event DTOs
├── notification/             # RabbitMQ consumer (Email + Push)
│   ├── config/               # RabbitMQ queue/exchange config
│   └── service/              # EmailService, PushNotificationService
└── docker-compose.yml
```
 
---
 
## 🎯 What This Project Demonstrates
 
- Distributed system design with independent deployable services
- Microservices patterns: Database-per-service, API Gateway, Service Discovery
- Centralized authentication with Keycloak + JWT propagation
- RBAC with role-based endpoint protection
- Synchronous inter-service communication via Feign
- Asynchronous event-driven architecture via RabbitMQ
- Compensating transactions (Keycloak rollback on DB failure)
- Order status lifecycle with validated transitions
- Stock management across order placement and cancellation
- Clean exception hierarchy with global error handling
- Soft delete patterns (products, user accounts)
 
---
 
## 🛠 Future Enhancements
 
- [ ] Redis caching — product cache, stock counter (atomic DECRBY to prevent overselling), cart session cache
- [ ] Payment Service
- [ ] JavaMail integration in Notification Service
- [ ] FCM push notification integration
- [ ] Distributed tracing (Zipkin / Micrometer)
- [ ] Monitoring (Prometheus + Grafana)
- [ ] CI/CD pipeline
- [ ] Rate limiting at Gateway
- [ ] Refresh token handling
 
---
 
## 👨‍💻 Author
 
**Mohit Singh**
 
- GitHub: [https://github.com/Mohit-Singh-007](https://github.com/Mohit-Singh-007)
 
---
 
⭐ If you found this useful, feel free to star the repository!
