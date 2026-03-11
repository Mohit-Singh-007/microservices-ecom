# 🛒 Microservices E-Commerce Backend

A production-grade distributed e-commerce backend built using **Spring Boot & Spring Cloud**, following real-world microservices architecture patterns.

This project demonstrates scalable system design, service-to-service communication, centralized security, and cloud-native best practices.

---

## 🚀 Project Overview

This system is designed as a distributed backend where each domain is isolated into its own microservice.

### ✨ Features

- Independent domain services
- API Gateway for centralized routing
- Service Discovery using Eureka
- Centralized authentication & RBAC
- Dockerized infrastructure
- Clean architecture & service boundaries

---

## 🏗️ Architecture

```
                Client
                   │
                   ▼
             API Gateway
                   │
     ┌─────────────┼─────────────┐
     ▼             ▼             ▼
 User Service   Product Service   Cart Service
     │
     ▼
 Database (per service)

         Eureka Server (Service Discovery)
```

### Architecture Principles

- Database per service
- Loose coupling
- Centralized routing
- Independent deployment
- Horizontal scalability

---

## 🧰 Tech Stack

- Java 17
- Spring Boot
- Spring Cloud Gateway
- Eureka Server
- Spring Security
- Keycloak (RBAC & Authentication)
- PostgreSQL / MySQL
- Docker & Docker Compose
- Maven

---

## 📦 Microservices

### 🔹 API Gateway
- Central entry point
- Dynamic routing
- Handles cross-cutting concerns

### 🔹 Eureka Server
- Service registry
- Dynamic service discovery

### 🔹 User Service
- User registration
- Profile management
- Role-based access integration

### 🔹 Product Service
- Product CRUD operations
- Inventory handling

### 🔹 Cart Service
- Add/remove cart items
- Product validation via synchronous calls

---

## 🔐 Security

Authentication & authorization handled via:

- Keycloak for centralized identity management
- Role-Based Access Control (RBAC)
- JWT-based authentication
- Gateway-level security enforcement

---

## 🔄 Communication Pattern

### ✅ Synchronous Communication
Used for:
- Cart → Product validation
- Order → User validation

Implemented using:
- FeignClient

### 🔜 Planned: Asynchronous Communication
- Kafka for event-driven architecture
- Payment & Notification flows

---

## 🐳 Running the Project

### 🛠 Prerequisites

- JDK 17+
- Maven
- Docker & Docker Compose
- PostgreSQL (if not using containerized DB)

---

### 🚀 Run with Docker Compose

```bash
docker compose up --build
```

---

### 🚀 Run Manually

1. Start Eureka Server  
2. Start all microservices  
3. Start API Gateway  
4. Start Keycloak  
5. Access APIs via Gateway  

---

## 📂 Project Structure

```
microservices-ecom/
│
├── gateway/
├── eureka/
├── user/
├── product/
├── cart/
└── docker-compose.yml
```

---

## 🎯 What This Project Demonstrates

- Distributed system design
- Microservices architecture
- Service discovery
- API Gateway routing
- Centralized authentication
- RBAC implementation
- Clean code & service isolation

---

## 🛠 Future Enhancements

- Order Service
- Payment Service
- Notification Service
- Kafka integration
- Redis caching
- Distributed tracing (Zipkin)
- Monitoring (Prometheus + Grafana)
- CI/CD pipeline

---

## 👨‍💻 Author

**Mohit Singh**

- GitHub: https://github.com/Mohit-Singh-007

---

⭐ If you found this useful, feel free to star the repository!
