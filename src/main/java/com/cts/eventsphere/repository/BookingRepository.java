package com.cts.eventsphere.repository;

import com.cts.eventsphere.model.Booking;
import com.cts.eventsphere.model.data.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * JPA Repository for the Booking Entity.
 * * @author 2479476
 *
 * @version 1.0
 * @since 27-02-2026
 */

@Repository
public interface BookingRepository extends JpaRepository<Booking, String> {
    List<Booking> findByEventId(String eventId);

    // Add this method
    List<Booking> findByVenue_VenueId(String venueId);
}
