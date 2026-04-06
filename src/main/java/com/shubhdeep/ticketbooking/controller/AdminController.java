package com.shubhdeep.ticketbooking.controller;

import com.shubhdeep.ticketbooking.dto.ApiResponse;
import com.shubhdeep.ticketbooking.dto.BookingResponse;
import com.shubhdeep.ticketbooking.dto.CreateEventRequest;
import com.shubhdeep.ticketbooking.dto.EventResponse;
import com.shubhdeep.ticketbooking.dto.SeatResponse;
import com.shubhdeep.ticketbooking.service.BookingService;
import com.shubhdeep.ticketbooking.service.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final EventService eventService;
    private final BookingService bookingService;

    @PostMapping("/events")
    public ResponseEntity<ApiResponse<EventResponse>> createEvent(
            @Valid @RequestBody CreateEventRequest request) {
        return ResponseEntity.ok(
                new ApiResponse<>("Event created successfully", eventService.createEvent(request))
        );
    }

    @PutMapping("/events/{eventId}")
    public ResponseEntity<ApiResponse<EventResponse>> updateEvent(
            @PathVariable Long eventId,
            @Valid @RequestBody CreateEventRequest request) {
        return ResponseEntity.ok(
                new ApiResponse<>("Event updated successfully", eventService.updateEvent(eventId, request))
        );
    }

    @DeleteMapping("/events/{eventId}")
    public ResponseEntity<ApiResponse<String>> deleteEvent(@PathVariable Long eventId) {
        eventService.deleteEvent(eventId);
        return ResponseEntity.ok(
                new ApiResponse<>("Event deleted successfully", "Deleted")
        );
    }

    @GetMapping("/events/{eventId}/seats")
    public ResponseEntity<ApiResponse<List<SeatResponse>>> getSeatStatusByEvent(@PathVariable Long eventId) {
        return ResponseEntity.ok(
                new ApiResponse<>("Seat status fetched successfully", eventService.getSeatStatusByEvent(eventId))
        );
    }

    @GetMapping("/bookings")
    public ResponseEntity<ApiResponse<List<BookingResponse>>> getAllBookings() {
        return ResponseEntity.ok(
                new ApiResponse<>("All bookings fetched successfully", bookingService.getAllBookings())
        );
    }

    @GetMapping("/bookings/{bookingId}")
    public ResponseEntity<ApiResponse<BookingResponse>> getBookingById(@PathVariable Long bookingId) {
        return ResponseEntity.ok(
                new ApiResponse<>("Booking fetched successfully", bookingService.getBookingById(bookingId))
        );
    }
}