package com.cts.eventsphere.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cts.eventsphere.model.Registration;

/**
 * Registration repository for getting registration and saving registration
 *
 * @author test-in-prod-10x
 * @version 1.0
 * @since 2026-03-02
 */
public interface RegistrationRepository extends JpaRepository<Registration, String> {
}
