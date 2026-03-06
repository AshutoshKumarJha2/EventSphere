package com.cts.eventsphere.repository;

import com.cts.eventsphere.model.Engagement;
import com.cts.eventsphere.model.data.EngagementType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
/**
 * Jpa Repository for Engagement Operations
 *
 * @author 2480027
 * @version 1.0
 * @since 05-03-2026
 */

@Repository
public interface EngagementRepository extends JpaRepository<Engagement, String> {

    // Filter 1: All engagements for a specific event
    List<Engagement> findByEventId(String eventId);

    // Filter 2: Specific activity across all events (Global trends)
    List<Engagement> findByActivity(EngagementType activity);

    // Filter 3: Engagements within a specific time window across all events
    List<Engagement> findByTimestampBetween(LocalDateTime start, LocalDateTime end);

    // Filter 4: Granular filtering (The one we previously had)
    List<Engagement> findByEventIdAndActivityAndTimestampBetween(
            String eventId, EngagementType activity, LocalDateTime start, LocalDateTime end);
}