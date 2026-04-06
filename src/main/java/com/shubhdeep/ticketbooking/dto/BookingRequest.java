package com.shubhdeep.ticketbooking.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class BookingRequest {

    @NotNull(message = "Event id is required")
    private Long eventId;

    @NotEmpty(message = "Seat ids cannot be empty")
    private List<Long> seatIds;
}