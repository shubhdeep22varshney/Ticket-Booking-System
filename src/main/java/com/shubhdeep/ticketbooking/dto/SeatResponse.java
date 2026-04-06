package com.shubhdeep.ticketbooking.dto;

import com.shubhdeep.ticketbooking.enums.SeatStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SeatResponse {
    private Long id;
    private String seatNumber;
    private SeatStatus status;
}
