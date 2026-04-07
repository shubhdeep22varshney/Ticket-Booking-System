# 🎟️ Distributed Ticket Booking System (Backend)

A backend-only distributed ticket booking system built using **Spring Boot**, **MySQL**, and **JWT Authentication**.
The system supports secure ticket booking with concurrency control to prevent overbooking.

---

## 🚀 Features

### 🔐 Authentication & Authorization

* User Registration & Login
* JWT-based authentication
* Role-based access:

   * **ADMIN**
   * **USER**

---

### 🎫 Event Management (Admin)

* Create Event
* Update Event
* Delete Event
* View all bookings
* View booking by ID
* Check seat status for events

---

### 🧑‍💻 User Booking

* Book tickets (multiple seats)
* View own bookings
* Cancel booking

---

### 🎭 Event APIs

* Get all events
* Get event by ID

---

### ⚡ Concurrency Handling

* ACID-compliant transactions
* Prevents double booking of seats
* Uses database-level locking (JPA/Hibernate)

---

## 🛠️ Tech Stack

* **Backend:** Java, Spring Boot
* **Database:** MySQL
* **ORM:** JPA / Hibernate
* **Security:** Spring Security + JWT
* **Testing:** Postman
* **Build Tool:** Maven

---

## 📁 Project Structure

```
src/main/java/com/shubhdeep/ticketbooking
 ┣ config
 ┣ controller
 ┣ dto
 ┣ entity
 ┣ enums
 ┣ exception
 ┣ repository
 ┣ security
 ┣ service
```

---

## ⚙️ Setup Instructions

### 1. Clone the Repository

```bash
git clone https://github.com/YOUR_USERNAME/distributed-ticket-booking-system-backend.git
cd distributed-ticket-booking-system-backend
```

---

### 2. Configure MySQL

Create a database:

```sql
CREATE DATABASE ticket_booking;
```

Update `application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/ticket_booking
spring.datasource.username=root
spring.datasource.password=your_password

spring.jpa.hibernate.ddl-auto=update
```

---

### 3. Run the Application

Using IntelliJ or:

```bash
mvn spring-boot:run
```

Server will start at:

```
http://localhost:8080
```

---

## 🔑 Authentication Flow

1. Register user → `/auth/register`
2. Login → `/auth/login`
3. Copy JWT token from response
4. Add in headers:

```
Authorization: Bearer <token>
```

---

## 📬 API Endpoints Overview

### 🔐 Auth

* `POST /auth/register`
* `POST /auth/login`

---

### 🎭 Events

* `GET /api/events`
* `GET /api/events/{id}`

---

### 🎫 Booking (User)

* `POST /api/bookings`
* `GET /api/bookings/my`
* `PUT /api/bookings/{id}/cancel`

---

### 🛠️ Admin

* `POST /api/admin/events`
* `PUT /api/admin/events/{id}`
* `DELETE /api/admin/events/{id}`
* `GET /api/admin/bookings`
* `GET /api/admin/bookings/{id}`
* `GET /api/admin/events/{id}/seats`

---

## 🧪 Postman Collection

Postman files are available in:

```
postman/
```

### Files:

* `ticket-booking-system.postman_collection.json`
* `ticket-booking-system.postman_environment.json`

### Setup:

1. Import both files into Postman
2. Select environment: `ticket-booking-system-local`
3. Set:

```
baseUrl = http://localhost:8080
```

4. Login → token auto-saved or paste manually

---

## 🧠 Key Concepts Used

* JWT Authentication
* Role-Based Authorization
* REST API Design
* Transaction Management
* Exception Handling
* DTO Pattern
* Clean Architecture

---

## 📌 Future Improvements

* Swagger API Documentation
* Dockerization
* Redis caching for seat locking
* Rate limiting
* Email notifications

---

## 👨‍💻 Author

**Shubhdeep Varshney**

---

## ⭐ If you like this project

Give it a ⭐ on GitHub!
