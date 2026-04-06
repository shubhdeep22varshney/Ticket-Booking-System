package com.shubhdeep.ticketbooking.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class EventResponse {
    private Long id;
    private String title;
    private String location;
    private LocalDateTime eventDateTime;
    private Integer totalSeats;
    private Double ticketPrice;
    private List<SeatResponse> seats;
}
