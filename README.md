# Distributed Ticket Booking System | Backend

This is a backend-only Spring Boot project for a distributed ticket booking system.

## Tech Stack
- Java 17
- Spring Boot 3.5.13
- Spring Web
- Spring Data JPA
- Spring Security (HTTP Basic)
- MySQL
- Maven
- Postman

## Main Features
- Admin can create events
- Auto-generate seats for each event
- User can view events and available/booked seats
- User can book multiple seats
- User can cancel bookings
- Uses transactional booking flow to reduce overbooking risk
- Uses pessimistic database locking when selecting seats for booking

## Default Login Credentials
### For Admin APIs
- username: `admin`
- password: `admin123`

### For User APIs
- username: `user`
- password: `user123`

## Seeded Database Users
These rows are inserted into the `users` table when the project starts for the first time:
- `id=1` → Admin User
- `id=2` → Demo User

Use `userId: 2` in booking requests.

## Project Structure
```text
src/main/java/com/shubhdeep/ticketbooking
├── config
├── controller
├── dto
├── entity
├── enums
├── exception
├── repository
└── service
```

## How to Run on Windows in IntelliJ IDEA
1. Install Java 17 or above.
2. Install MySQL and make sure the MySQL server is running.
3. Create or let Spring create the database `ticket_booking_db`.
4. Open `src/main/resources/application.properties`.
5. Change these values according to your MySQL setup:
   ```properties
   spring.datasource.username=root
   spring.datasource.password=root
   ```
6. Open the project in IntelliJ IDEA.
7. Wait for Maven dependencies to download.
8. Run `DistributedTicketBookingSystemApplication`.
9. Test APIs using Postman.

## Important Note About Password
The project currently uses this sample database password in `application.properties`:
```properties
spring.datasource.password=root
```
If your MySQL password is different, replace `root` with your actual MySQL password.

## API Summary
### 1) Create Event (ADMIN)
**POST** `/api/admin/events`

Authorization: Basic Auth (`admin` / `admin123`)

Sample Body:
```json
{
  "title": "Avengers Movie Show",
  "location": "Screen 1, Delhi",
  "eventDateTime": "2026-05-10T18:30:00",
  "totalSeats": 10,
  "ticketPrice": 250
}
```

### 2) Get All Events (USER or ADMIN)
**GET** `/api/events`

Authorization: Basic Auth (`user` / `user123`) or (`admin` / `admin123`)

### 3) Get Event By ID (USER or ADMIN)
**GET** `/api/events/{eventId}`

### 4) Create Booking (USER or ADMIN)
**POST** `/api/bookings`

Authorization: Basic Auth (`user` / `user123`)

Sample Body:
```json
{
  "userId": 2,
  "eventId": 1,
  "seatIds": [1, 2]
}
```

### 5) Get Bookings By User ID
**GET** `/api/bookings/user/2`

### 6) Cancel Booking
**PUT** `/api/bookings/{bookingId}/cancel`

## Suggested Postman Testing Flow
1. Create event as admin.
2. Get all events.
3. Get event by id and check seat ids.
4. Book seats using `userId = 2`.
5. Try booking the same seat again to see conflict handling.
6. Cancel booking and verify seat becomes available again.

## Resume Description You Can Use
- Developed backend using Spring Boot, Spring Data JPA, and MySQL for a distributed ticket booking system.
- Implemented ACID-compliant transactional booking flow with pessimistic locking to prevent concurrent overbooking.
- Designed REST APIs for admin and user operations including event creation, seat management, booking, and cancellation.
- Secured APIs using Spring Security and validated/tested endpoints using Postman.

## Next Improvements
- JWT authentication instead of basic auth
- Redis-based distributed lock
- Payment integration
- API gateway
- Docker support
- Unit and integration tests
- Swagger/OpenAPI documentation
- Kafka/RabbitMQ for async notifications
