# ABCStore - E-commerce Backend API

This is a Spring Boot-based RESTful API for McDiffyStore, an e-commerce platform that supports user registration, login, JWT-based authentication, and role-based access control for consumers and sellers.

## Features

- ✅ User Registration & JWT Login
- ✅ Role-based Access (SELLER, CONSUMER)
- ✅ Sellers can post and view their products
- ✅ Consumers can view available products
- ✅ Automatic Category handling
- ✅ H2 In-memory DB (for dev/testing)
- ✅ Secure endpoints with Spring Security

---

## Technologies Used

- Java 17+
- Spring Boot 3
- Spring Security
- Spring Data JPA
- H2 Database (in-memory)
- JWT (JSON Web Tokens)
- JUnit + MockMvc for testing

---

## Getting Started

### Prerequisites

- Java 17+
- Maven 3+

### Clone and Run

```bash
git clone https://github.com/your-repo/mcdiffystore.git
cd ABCstore
mvn spring-boot:run
