package com.shubhdeep.ticketbooking.service;

import com.shubhdeep.ticketbooking.dto.CreateEventRequest;
import com.shubhdeep.ticketbooking.dto.EventResponse;
import com.shubhdeep.ticketbooking.dto.SeatResponse;

import java.util.List;

public interface EventService {

    EventResponse createEvent(CreateEventRequest request);

    List<EventResponse> getAllEvents();

    EventResponse getEventById(Long eventId);

    EventResponse updateEvent(Long eventId, CreateEventRequest request);

    void deleteEvent(Long eventId);

    List<SeatResponse> getSeatStatusByEvent(Long eventId);
}