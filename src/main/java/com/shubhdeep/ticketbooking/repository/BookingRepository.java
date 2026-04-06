
package com.shubhdeep.ticketbooking.repository;

import com.shubhdeep.ticketbooking.entity.Booking;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @EntityGraph(attributePaths = {"user", "event", "seats"})
    List<Booking> findByUserId(Long userId);

    boolean existsByEventId(Long eventId);
}