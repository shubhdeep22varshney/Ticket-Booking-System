package com.shubhdeep.ticketbooking;

import com.shubhdeep.ticketbooking.dto.BookingRequest;
import com.shubhdeep.ticketbooking.entity.AppUser;
import com.shubhdeep.ticketbooking.entity.Event;
import com.shubhdeep.ticketbooking.entity.Seat;
import com.shubhdeep.ticketbooking.enums.BookingStatus;
import com.shubhdeep.ticketbooking.enums.Role;
import com.shubhdeep.ticketbooking.enums.SeatStatus;
import com.shubhdeep.ticketbooking.exception.BookingConflictException;
import com.shubhdeep.ticketbooking.repository.AppUserRepository;
import com.shubhdeep.ticketbooking.repository.BookingRepository;
import com.shubhdeep.ticketbooking.repository.EventRepository;
import com.shubhdeep.ticketbooking.repository.SeatRepository;
import com.shubhdeep.ticketbooking.service.BookingService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 100 users try to book the SAME seat at the same moment.
 * Expected: exactly 1 booking succeeds, 99 get BookingConflictException.
 */
@Testcontainers
@SpringBootTest
class BookingConcurrencyTest {

    @Container
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("ticket_booking")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void props(DynamicPropertyRegistry r) {
        r.add("spring.datasource.url", mysql::getJdbcUrl);
        r.add("spring.datasource.username", mysql::getUsername);
        r.add("spring.datasource.password", mysql::getPassword);
        r.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
    }

    @Autowired BookingService bookingService;
    @Autowired AppUserRepository userRepository;
    @Autowired EventRepository eventRepository;
    @Autowired SeatRepository seatRepository;
    @Autowired BookingRepository bookingRepository;

    @BeforeEach
    @AfterEach
    void cleanDb() {
        bookingRepository.deleteAll();
        seatRepository.deleteAll();
        eventRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void onlyOneUserCanBookTheSameSeat() throws Exception {
        final int THREADS = 100;

        // ---- 1. Prepare data: 1 event, 1 seat, 100 users ----
        Event event = eventRepository.save(Event.builder()
                .title("Concurrency Test Concert")
                .location("Test Arena")
                .eventDateTime(LocalDateTime.now().plusDays(10))
                .totalSeats(1)
                .ticketPrice(500.0)
                .build());

        Seat seat = seatRepository.save(Seat.builder()
                .event(event)
                .seatNumber("A1")
                .status(SeatStatus.AVAILABLE)
                .build());

        List<String> emails = new ArrayList<>();
        for (int i = 0; i < THREADS; i++) {
            String email = "user" + i + "@test.com";
            userRepository.save(AppUser.builder()
                    .fullName("Test User " + i)
                    .email(email)
                    .password("password")
                    .role(Role.USER)
                    .build());
            emails.add(email);
        }

        // ---- 2. Release all threads at the same moment ----
        ExecutorService pool = Executors.newFixedThreadPool(THREADS);
        CountDownLatch ready = new CountDownLatch(THREADS);
        CountDownLatch start = new CountDownLatch(1);
        AtomicInteger success = new AtomicInteger();
        AtomicInteger conflict = new AtomicInteger();
        AtomicInteger otherErrors = new AtomicInteger();

        List<Future<?>> futures = new ArrayList<>();
        for (String email : emails) {
            futures.add(pool.submit(() -> {
                ready.countDown();
                try {
                    start.await();
                    // createBooking() reads the user from SecurityContext, which is per-thread
                    SecurityContextHolder.getContext().setAuthentication(
                            new UsernamePasswordAuthenticationToken(email, null, List.of()));

                    BookingRequest req = new BookingRequest();
                    req.setEventId(event.getId());
                    req.setSeatIds(List.of(seat.getId()));

                    bookingService.createBooking(req);
                    success.incrementAndGet();
                } catch (BookingConflictException e) {
                    conflict.incrementAndGet();            // expected for the 99 losers
                } catch (Exception e) {
                    otherErrors.incrementAndGet();
                    System.out.println("Unexpected error: " + e);
                } finally {
                    SecurityContextHolder.clearContext();
                }
                return null;
            }));
        }

        ready.await();
        start.countDown();                                  // GO
        for (Future<?> f : futures) f.get(60, TimeUnit.SECONDS);
        pool.shutdown();

        System.out.println("Threads: " + THREADS
                + " | Success: " + success.get()
                + " | Conflict: " + conflict.get()
                + " | Other errors: " + otherErrors.get());

        // ---- 3. Verify thread results ----
        assertEquals(1, success.get(), "Exactly one booking must succeed");
        assertEquals(THREADS - 1, conflict.get(), "All others must get BookingConflictException");
        assertEquals(0, otherErrors.get(), "No unexpected errors");

        // ---- 4. Verify database state ----
        assertEquals(1, bookingRepository.count(), "Only one booking row must exist");
        assertEquals(BookingStatus.CONFIRMED,
                bookingRepository.findAll().get(0).getBookingStatus());
        assertEquals(SeatStatus.BOOKED,
                seatRepository.findById(seat.getId()).orElseThrow().getStatus());
    }
}