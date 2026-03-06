package com.cts.eventsphere.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.cts.eventsphere.model.Registration;

import java.util.List;

/**
 * Registration repository for getting registration and saving registration
 *
 * @author test-in-prod-10x
 * @version 1.0
 * @since 2026-03-02
 */
public interface RegistrationRepository extends JpaRepository<Registration, String> {
    Registration findByAttendeeIdAndEventId(String userId, String eventId);
//    List<Registration> findByAttendeeId(String userId);
//    List<Registration> findByEventId(String eventId);
    Page<Registration> findByAttendeeId(String userId, Pageable pageable);
    Page<Registration> findByEventId(String eventId, Pageable pageable);
}
