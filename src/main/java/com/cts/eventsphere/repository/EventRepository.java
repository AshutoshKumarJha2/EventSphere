package com.cts.eventsphere.repository;

import com.cts.eventsphere.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * JPA Repository for the Event Entity.
 * * @author 2479623
 *
 * @version 1.0
 * @since 26-02-2026
 */
@Repository
public interface EventRepository extends JpaRepository<Event, String> {

    Event findByEventId(String eventId);
}
