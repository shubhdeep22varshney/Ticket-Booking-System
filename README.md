# 🎟️ Distributed Ticket Booking System | Backend

A backend-only Spring Boot application for managing events and booking tickets in a distributed environment.
The system ensures secure user authentication and prevents overbooking using transactional locking mechanisms.

---

## 🚀 Tech Stack

* **Java 17**
* **Spring Boot**
* **Spring Security**
* **JWT Authentication**
* **Spring Data JPA (Hibernate)**
* **MySQL**
* **Maven**
* **Postman**

---

## 🔥 Features

### 🔐 Authentication & Security

* User registration and login
* JWT-based authentication
* Role-based authorization (**ADMIN**, **USER**)
* Secure endpoints using Spring Security

### 🛠️ Admin Features

* Create event
* Update event
* Delete event
* View all bookings

### 👤 User Features

* View all events
* Book tickets (multiple seats)
* Cancel bookings
* View own bookings

### ⚙️ System Features

* Prevents **double booking** using database locking
* ACID-compliant transactions using `@Transactional`
* Global exception handling
* Request validation using annotations (`@NotNull`, `@NotBlank`, etc.)
* Clean layered architecture (Controller → Service → Repository)

---

## 🔐 Authentication Flow

1. User logs in via:

   ```http
   POST /api/auth/login
   ```
2. Backend returns a JWT token
3. Include token in every protected request:

   ```http
   Authorization: Bearer <your_token>
   ```
4. Backend extracts user from token
   (No `userId` required in request body)

---

## 📦 API Endpoints

### 🔑 Auth APIs

* `POST /api/auth/register`
* `POST /api/auth/login`

### 🎫 Event APIs

* `GET /api/events`
* `GET /api/events/{eventId}`

### 🛠️ Admin APIs

* `POST /api/admin/events`
* `PUT /api/admin/events/{eventId}`
* `DELETE /api/admin/events/{eventId}`
* `GET /api/admin/bookings`
* `GET /api/admin/bookings/{bookingId}`

### 👤 User Booking APIs

* `POST /api/bookings`
* `GET /api/bookings/user`
* `PUT /api/bookings/{bookingId}/cancel`

---

## 🧪 Postman Testing

A Postman collection is included in the project:

```
postman_collection.json
```

### Steps:

1. Login and get JWT token
2. Add token in headers:

   ```
   Authorization: Bearer <token>
   ```
3. Test APIs (create event, booking, cancel, etc.)

---

## ⚙️ How to Run the Project

### 1. Clone the repository

```bash
git clone <your-repo-url>
cd distributed-ticket-booking-system-backend
```

### 2. Configure MySQL Database

Update `application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/ticket_booking_db
spring.datasource.username=root
spring.datasource.password=your_mysql_password
```

### 3. Run the application

```bash
mvn spring-boot:run
```

Server runs at:

```
http://localhost:8080
```

---

## 🧠 Key Concepts Used

* JWT Authentication
* Role-based Authorization
* Transaction Management (`@Transactional`)
* Pessimistic Locking (for concurrency control)
* DTO pattern (Request/Response separation)
* Global Exception Handling
* Validation with annotations
* Clean architecture design

---

## 🚀 Future Enhancements

* Swagger (API documentation)
* Docker support
* Redis caching (for seat locking / performance)
* Unit & integration testing
* Payment gateway integration
* Email notifications

---

## 📌 Important Notes

* `userId` is NOT passed in booking request
* User identity is extracted securely from JWT token
* Prevents overbooking using database locking

---

## 👨‍💻 Author

**Shubhdeep Varshney**

---

⭐ If you like this project, consider giving it a star!
