package com.shubhdeep.ticketbooking.controller;

import com.shubhdeep.ticketbooking.dto.ApiResponse;
import com.shubhdeep.ticketbooking.dto.BookingRequest;
import com.shubhdeep.ticketbooking.dto.BookingResponse;
import com.shubhdeep.ticketbooking.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<ApiResponse<BookingResponse>> createBooking(@Valid @RequestBody BookingRequest request) {
        return ResponseEntity.ok(new ApiResponse<>("Booking created successfully", bookingService.createBooking(request)));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<BookingResponse>>> getBookingsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(new ApiResponse<>("Bookings fetched successfully", bookingService.getBookingsByUser(userId)));
    }

    @PutMapping("/{bookingId}/cancel")
    public ResponseEntity<ApiResponse<BookingResponse>> cancelBooking(@PathVariable Long bookingId) {
        return ResponseEntity.ok(new ApiResponse<>("Booking cancelled successfully", bookingService.cancelBooking(bookingId)));
    }


}
