package com.cts.eventsphere.repository;
import com.cts.eventsphere.model.FeedBack;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
/**
 * [ Detailed description of the class's responsibility]
 *
 * @author 2480027
 * @version 1.0
 * @since 26-02-2026
 */
public interface FeedbackRepository extends JpaRepository<FeedBack, String> {

    Page<FeedBack> findByEventId(String eventId, Pageable pageable);

    Page<FeedBack> findByEventIdAndAttendeeId(String eventId, String attendeeId, Pageable pageable);

    Page<FeedBack> findByEventIdAndDateBetween(String eventId, LocalDateTime start, LocalDateTime end, Pageable pageable);
}