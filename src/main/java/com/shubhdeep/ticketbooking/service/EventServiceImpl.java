package com.shubhdeep.ticketbooking.service;

import com.shubhdeep.ticketbooking.dto.CreateEventRequest;
import com.shubhdeep.ticketbooking.dto.EventResponse;
import com.shubhdeep.ticketbooking.dto.SeatResponse;
import com.shubhdeep.ticketbooking.entity.Event;
import com.shubhdeep.ticketbooking.entity.Seat;
import com.shubhdeep.ticketbooking.enums.SeatStatus;
import com.shubhdeep.ticketbooking.exception.BookingConflictException;
import com.shubhdeep.ticketbooking.exception.ResourceNotFoundException;
import com.shubhdeep.ticketbooking.repository.BookingRepository;
import com.shubhdeep.ticketbooking.repository.EventRepository;
import com.shubhdeep.ticketbooking.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final SeatRepository seatRepository;
    private final BookingRepository bookingRepository;

    @Override
    @Transactional
    public EventResponse createEvent(CreateEventRequest request) {
        Event event = Event.builder()
                .title(request.getTitle())
                .location(request.getLocation())
                .eventDateTime(request.getEventDateTime())
                .totalSeats(request.getTotalSeats())
                .ticketPrice(request.getTicketPrice())
                .build();

        Event savedEvent = eventRepository.save(event);

        List<Seat> seats = new ArrayList<>();
        for (int i = 1; i <= request.getTotalSeats(); i++) {
            seats.add(Seat.builder()
                    .event(savedEvent)
                    .seatNumber("S-" + i)
                    .status(SeatStatus.AVAILABLE)
                    .build());
        }

        seatRepository.saveAll(seats);
        return mapToResponse(savedEvent, seats);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventResponse> getAllEvents() {
        return eventRepository.findAll().stream()
                .map(event -> mapToResponse(event, seatRepository.findByEventId(event.getId())))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SeatResponse> getSeatStatusByEvent(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + eventId));

        List<Seat> seats = seatRepository.findByEventId(event.getId());

        return seats.stream()
                .map(this::mapSeat)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public EventResponse getEventById(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + eventId));

        return mapToResponse(event, seatRepository.findByEventId(eventId));
    }

    @Override
    @Transactional
    public EventResponse updateEvent(Long eventId, CreateEventRequest request) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + eventId));

        event.setTitle(request.getTitle());
        event.setLocation(request.getLocation());
        event.setEventDateTime(request.getEventDateTime());
        event.setTotalSeats(request.getTotalSeats());
        event.setTicketPrice(request.getTicketPrice());

        Event updatedEvent = eventRepository.save(event);
        List<Seat> seats = seatRepository.findByEventId(eventId);

        return mapToResponse(updatedEvent, seats);
    }

    @Override
    @Transactional
    public void deleteEvent(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + eventId));

        if (bookingRepository.existsByEventId(eventId)) {
            throw new BookingConflictException("Cannot delete event because bookings already exist for this event.");
        }

        List<Seat> seats = seatRepository.findByEventId(eventId);
        seatRepository.deleteAll(seats);
        eventRepository.delete(event);
    }

    private EventResponse mapToResponse(Event event, List<Seat> seats) {
        return EventResponse.builder()
                .id(event.getId())
                .title(event.getTitle())
                .location(event.getLocation())
                .eventDateTime(event.getEventDateTime())
                .totalSeats(event.getTotalSeats())
                .ticketPrice(event.getTicketPrice())
                .seats(seats.stream().map(this::mapSeat).toList())
                .build();
    }

    private SeatResponse mapSeat(Seat seat) {
        return SeatResponse.builder()
                .id(seat.getId())
                .seatNumber(seat.getSeatNumber())
                .status(seat.getStatus())
                .build();
    }
}