package com.cts.eventsphere.repository;

import com.cts.eventsphere.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * JPA Repository for the Event Entity.
 * * @author 2479623
 *
 * @version 1.0
 * @since 26-02-2026
 */
public interface ScheduleRepository extends JpaRepository<Schedule, String> {
}
