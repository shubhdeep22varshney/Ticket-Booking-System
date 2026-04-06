package com.shubhdeep.ticketbooking.controller;

import com.shubhdeep.ticketbooking.dto.ApiResponse;
import com.shubhdeep.ticketbooking.dto.EventResponse;
import com.shubhdeep.ticketbooking.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<EventResponse>>> getAllEvents() {
        return ResponseEntity.ok(new ApiResponse<>("Events fetched successfully", eventService.getAllEvents()));
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<ApiResponse<EventResponse>> getEventById(@PathVariable Long eventId) {
        return ResponseEntity.ok(new ApiResponse<>("Event fetched successfully", eventService.getEventById(eventId)));
    }

}
