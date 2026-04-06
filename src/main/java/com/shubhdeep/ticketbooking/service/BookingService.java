package com.shubhdeep.ticketbooking.service;

import com.shubhdeep.ticketbooking.dto.BookingRequest;
import com.shubhdeep.ticketbooking.dto.BookingResponse;

import java.util.List;

public interface BookingService {

    BookingResponse createBooking(BookingRequest request);

    List<BookingResponse> getBookingsByUser(Long userId);

    BookingResponse cancelBooking(Long bookingId);

    List<BookingResponse> getAllBookings();

    BookingResponse getBookingById(Long bookingId);

}