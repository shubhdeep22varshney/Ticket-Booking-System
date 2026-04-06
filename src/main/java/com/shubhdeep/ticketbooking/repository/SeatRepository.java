package com.shubhdeep.ticketbooking.repository;

import com.shubhdeep.ticketbooking.entity.Seat;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SeatRepository extends JpaRepository<Seat, Long> {

    List<Seat> findByEventId(Long eventId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select s from Seat s where s.event.id = :eventId and s.id in :seatIds")
    List<Seat> findSeatsForUpdate(@Param("eventId") Long eventId, @Param("seatIds") List<Long> seatIds);
}
