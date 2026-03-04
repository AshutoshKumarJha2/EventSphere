package com.cts.eventsphere.repository;

import com.cts.eventsphere.model.Booking;
import com.cts.eventsphere.model.data.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

/**
 * JPA Repository for the Booking Entity.
 * * @author 2479476
 *
 * @version 1.0
 * @since 27-02-2026
 */


public interface BookingRepository extends JpaRepository<Booking,String> {

//    @Query("""
//        SELECT COUNT(b) > 0
//        FROM Booking b
//        WHERE b.venueId = :venueId
//          AND b.status = :status
//          AND (
//              (b.event.startDate <= :endDate AND b.event.endDate >= :startDate)
//          )
//    """)
//    boolean existsOverlappingBooking(
//            @Param("venueId") String venueId,
//            @Param("startDate") LocalDateTime startDate,
//            @Param("endDate") LocalDateTime endDate,
//            @Param("status") BookingStatus status
//    );


}
