package com.shubhdeep.ticketbooking.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateEventRequest {
    @NotBlank
    private String title;

    @NotBlank
    private String location;

    @NotNull
    @Future
    private LocalDateTime eventDateTime;

    @NotNull
    @Min(1)
    private Integer totalSeats;

    @NotNull
    @Min(1)
    private Double ticketPrice;
}
