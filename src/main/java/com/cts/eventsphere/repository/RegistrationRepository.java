package com.cts.eventsphere.repository;

import com.cts.eventsphere.model.data.RegistrationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.cts.eventsphere.model.Registration;

import java.util.Optional;

/**
 * Registration repository for getting registration and saving registration
 *
 * @author test-in-prod-10x
 * @version 1.0
 * @since 2026-03-02
 */
public interface RegistrationRepository extends JpaRepository<Registration, String> {
    Optional<Registration> findByAttendeeUserIdAndEventId(String userId, String eventId);
//    List<Registration> findByAttendeeId(String userId);
//    List<Registration> findByEventId(String eventId);
    Page<Registration> findByAttendeeUserId(String userId, Pageable pageable);
    Page<Registration> findByEventId(String eventId, Pageable pageable);
    Page<Registration> findByEventIdAndStatus(String eventId, RegistrationStatus status, Pageable pageable);
}
