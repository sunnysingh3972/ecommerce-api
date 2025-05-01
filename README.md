# ABCStore E-commerce Backend

This is a Spring Boot-based RESTful API backend developed as an MVP for ABCStore, a vendor selling products across various categories. The API supports JWT-based authentication with role-based access for CONSUMER and SELLER users.

---

## 📦 Features

- JWT Authentication and Role-based Authorization (CONSUMER, SELLER)
- Public product search
- Seller product management (CRUD)
- Consumer cart management (CRUD)
- Encrypted passwords stored in DB
- Error handling with appropriate status codes

---

## 🛠 Technologies

- Java 17
- Spring Boot
- Spring Security (with JWT)
- Spring Data JPA
- H2 / MySQL (depending on setup)
- Maven

---

## 🧑‍💻 User Roles

- **CONSUMER**: Can manage their shopping cart
- **SELLER**: Can manage product catalog
- **UserInfo Table**: Stores users with encrypted passwords and roles

---

## 🔐 Authentication

- **Login Endpoint**: `/api/public/login`
- JWT is returned upon successful login
- Must be sent in header for secured endpoints:

```
Authorization: Bearer <JWT_TOKEN>
```

- **401 Unauthorized**: If JWT is missing or invalid
- **403 Forbidden**: If accessing endpoint with incorrect role

---

## 📂 API Endpoints

### 🎓 Public Endpoints

#### 1. `GET /api/public/product/search?keyword=tablet`
- Search products by keyword in `productName` or `categoryName`
- **200 OK**: On success
- **400 Bad Request**: On error

#### 2. `POST /api/public/login`
- Authenticates user and returns JWT
- **Request Body**:
```json
{
  "username": "jack",
  "password": "pass_word"
}
```
- **200 OK**: On success
- **401 Unauthorized**: Invalid credentials

---

### 👤 Consumer Endpoints (`/api/auth/consumer/**`)

#### 3. `GET /api/auth/consumer/cart`
- Returns logged-in consumer's cart

#### 4. `POST /api/auth/consumer/cart`
- Adds product to cart
- **409 Conflict**: Product already in cart

#### 5. `PUT /api/auth/consumer/cart`
- Updates quantity of product in cart
- Adds if not in cart
- Deletes if quantity = 0

#### 6. `DELETE /api/auth/consumer/cart`
- Removes product from cart

---

### 🛙️ Seller Endpoints (`/api/auth/seller/**`)

#### 7. `GET /api/auth/seller/product/{productId}`
- Returns a product owned by seller with given ID

#### 8. `POST /api/auth/seller/product`
- Adds new product
- **201 Created**
- **Location** header: `http://localhost/api/auth/seller/product/{productId}`

#### 9. `GET /api/auth/seller/product`
- Returns all products owned by seller

#### 10. `PUT /api/auth/seller/product`
- Updates product details

#### 11. `DELETE /api/auth/seller/product/{productId}`
- Deletes product
- **404 Not Found**: If product not owned by seller

---

## 🧰 Example Users (in DB)

| userId | username | password (encrypted) | role     |
|--------|----------|-----------------------|----------|
| 1      | jack     | pass_word             | CONSUMER |
| 2      | bob      | pass_word             | CONSUMER |
| 3      | apple    | pass_word             | SELLER   |
| 4      | glaxo    | pass_word             | SELLER   |

---

## 📦 Categories (Sample)

- Fashion
- Electronics
- Books
- Groceries
- Medicines

---

## 🛒 Sample Product (DB)

```json
{
  "productId": 1,
  "productName": "Apple iPad 10.2 8th Gen WiFi iOS Tablet",
  "price": 29190,
  "categoryId": 2,
  "sellerId": 3
}
```

---

## 🔒 Security

- JWT Token required for all `/api/auth/**` endpoints
- Role-based access control enforced
- Invalid or missing tokens result in `401 Unauthorized`

---

## 🚀 Getting Started

1. Clone the repository
2. Setup database connection in `application.properties`
3. Run the application
4. Use Postman or similar to interact with the endpoints

---

## 📝 License

This project is for educational use only.

