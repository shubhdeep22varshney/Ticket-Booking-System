package com.shubhdeep.ticketbooking.dto;

import com.shubhdeep.ticketbooking.enums.BookingStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class BookingResponse {
    private Long bookingId;
    private Long userId;
    private Long eventId;
    private String eventTitle;
    private BookingStatus bookingStatus;
    private Double totalAmount;
    private LocalDateTime bookedAt;
    private List<String> bookedSeats;
}
