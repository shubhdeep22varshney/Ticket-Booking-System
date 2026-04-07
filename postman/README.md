## 🧪 Postman Collection

This project includes a Postman collection and environment file for testing all APIs.

### Files

* `postman/ticket-booking-system.postman_collection.json`
* `postman/ticket-booking-system.postman_environment.json`

### How to Use

1. Open **Postman**
2. Click **Import**
3. Import both files from the `postman/` folder
4. Select the environment: **ticket-booking-system-local**

### Environment Variables

Use these variables in Postman:

```text
baseUrl = http://localhost:8080
token   = 
```

* `baseUrl` is the backend server URL
* `token` stores the JWT token returned after login

### Testing Flow

1. Run **Register** API to create a new user
2. Run **Admin Login** or **User Login**
3. Copy the JWT token from the response
4. Paste the token into the `token` environment variable if it is not auto-saved
5. Test protected APIs like:

    * Create Event
    * Create Booking
    * Get My Bookings
    * Cancel Booking
    * Admin Booking APIs

### Authorization Format

For secured APIs, Postman uses:

```text
Authorization: Bearer {{token}}
```

### Notes

* Replace `baseUrl` if your server runs on another port
* Make sure the Spring Boot application is running before testing APIs
* If token expires, login again and update the `token` variable
