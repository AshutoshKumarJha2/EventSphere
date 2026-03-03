package com.cts.eventsphere.repository;

import com.cts.eventsphere.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * JPA Repository for the Booking Entity.
 * * @author 2479476
 *
 * @version 1.0
 * @since 27-02-2026
 */


public interface BookingRepository extends JpaRepository<Booking,String> {
}
