package com.shubhdeep.ticketbooking.repository;

import com.shubhdeep.ticketbooking.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
}
