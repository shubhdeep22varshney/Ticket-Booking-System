package com.shubhdeep.ticketbooking.service;

import com.shubhdeep.ticketbooking.dto.BookingRequest;
import com.shubhdeep.ticketbooking.dto.BookingResponse;
import com.shubhdeep.ticketbooking.entity.AppUser;
import com.shubhdeep.ticketbooking.entity.Booking;
import com.shubhdeep.ticketbooking.entity.Event;
import com.shubhdeep.ticketbooking.entity.Seat;
import com.shubhdeep.ticketbooking.enums.BookingStatus;
import com.shubhdeep.ticketbooking.enums.SeatStatus;
import com.shubhdeep.ticketbooking.exception.BookingConflictException;
import com.shubhdeep.ticketbooking.exception.ResourceNotFoundException;
import com.shubhdeep.ticketbooking.repository.AppUserRepository;
import com.shubhdeep.ticketbooking.repository.BookingRepository;
import com.shubhdeep.ticketbooking.repository.EventRepository;
import com.shubhdeep.ticketbooking.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final AppUserRepository userRepository;
    private final EventRepository eventRepository;
    private final SeatRepository seatRepository;
    private final BookingRepository bookingRepository;


    @Override
    @Transactional
    public BookingResponse createBooking(BookingRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        AppUser user = userRepository.findByEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));

        Event event = eventRepository.findById(request.getEventId())
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + request.getEventId()));

        List<Seat> seats = seatRepository.findSeatsForUpdate(request.getEventId(), request.getSeatIds());
        if (seats.size() != request.getSeatIds().size()) {
            throw new ResourceNotFoundException("One or more seats were not found for the selected event.");
        }

        boolean alreadyBooked = seats.stream().anyMatch(seat -> seat.getStatus() == SeatStatus.BOOKED);
        if (alreadyBooked) {
            throw new BookingConflictException("One or more selected seats are already booked.");
        }

        seats.forEach(seat -> seat.setStatus(SeatStatus.BOOKED));
        seatRepository.saveAll(seats);

        Booking booking = Booking.builder()
                .user(user)
                .event(event)
                .seats(seats)
                .bookingStatus(BookingStatus.CONFIRMED)
                .totalAmount(event.getTicketPrice() * seats.size())
                .bookedAt(LocalDateTime.now())
                .build();

        Booking savedBooking = bookingRepository.save(booking);
        return mapToResponse(savedBooking);
    }
    @Override
    @Transactional(readOnly = true)
    public List<BookingResponse> getBookingsByUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }

        return bookingRepository.findByUserId(userId).stream()
                .map(this::mapToResponse)
                .toList();
    }
    @Override
    @Transactional(readOnly = true)
    public List<BookingResponse> getAllBookings() {
        return bookingRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public BookingResponse getBookingById(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + bookingId));

        return mapToResponse(booking);
    }
    @Override
    @Transactional
    public BookingResponse cancelBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + bookingId));

        if (booking.getBookingStatus() == BookingStatus.CANCELLED) {
            throw new BookingConflictException("Booking is already cancelled.");
        }

        booking.setBookingStatus(BookingStatus.CANCELLED);
        booking.getSeats().forEach(seat -> seat.setStatus(SeatStatus.AVAILABLE));
        seatRepository.saveAll(booking.getSeats());
        return mapToResponse(bookingRepository.save(booking));
    }

    private BookingResponse mapToResponse(Booking booking) {
        return BookingResponse.builder()
                .bookingId(booking.getId())
                .userId(booking.getUser().getId())
                .eventId(booking.getEvent().getId())
                .eventTitle(booking.getEvent().getTitle())
                .bookingStatus(booking.getBookingStatus())
                .totalAmount(booking.getTotalAmount())
                .bookedAt(booking.getBookedAt())
                .bookedSeats(booking.getSeats().stream().map(Seat::getSeatNumber).toList())
                .build();
    }
}