package com.cts.eventsphere.repository;
import com.cts.eventsphere.model.Engagement;
import org.springframework.data.jpa.repository.JpaRepository;
import com.cts.eventsphere.model.data.EngagementType;
import com.cts.eventsphere.model.data.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

/**
 * Jpa repository for engagement entity
 *
 * @author 2480027
 * @version 1.0
 * @since 03-03-2026
 */

import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface EngagementRepository extends JpaRepository<Engagement, String>, JpaSpecificationExecutor<Engagement> {

    // --- AC2: Filtering by event/activity/time window (paged) ---
    Page<Engagement> findByEventId(String eventId, Pageable pageable);

    Page<Engagement> findByEventIdAndActivity(String eventId, EngagementType activity, Pageable pageable);

    Page<Engagement> findByEventIdAndActivityTimestampBetween(
            String eventId, LocalDateTime start, LocalDateTime end, Pageable pageable
    );

    Page<Engagement> findByEventIdAndActivityAndActivityTimestampBetween(
            String eventId, EngagementType activity, LocalDateTime start, LocalDateTime end, Pageable pageable
    );

    // --- AC1: KPIs - count only engagements from attendees with CONFIRMED registration ---
    // Requires a Registration entity/table with fields: eventId, attendeeId, status (UserStatus)
    @Query("""
           SELECT COUNT(e)
           FROM Engagement e, Registration r
           WHERE r.eventId = e.eventId
             AND r.attendeeId = e.attendeeId
             AND r.status = :status
             AND e.eventId = :eventId
             AND (:activity IS NULL OR e.activity = :activity)
             AND (:start IS NULL OR e.activityTimestamp >= :start)
             AND (:end   IS NULL OR e.activityTimestamp <= :end)
           """)
    long countForEventWithRegistrationStatus(
            @Param("eventId") String eventId,
            @Param("activity") EngagementType activity,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("status") UserStatus status
    );

    // Convenience method for AC1 (CONFIRMED only)
    default long countConfirmedEngagements(
            String eventId,
            EngagementType activity,
            LocalDateTime start,
            LocalDateTime end
    ) {
        return countForEventWithRegistrationStatus(eventId, activity, start, end, UserStatus.CONFIRMED);
    }
}
