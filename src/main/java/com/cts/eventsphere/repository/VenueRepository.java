package com.cts.eventsphere.repository;

import com.cts.eventsphere.model.Venue;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * JPA Repository for the Venue Entity.
 * * @author 2479476
 *
 * @version 1.0
 * @since 26-02-2026
 */

public interface VenueRepository extends JpaRepository<Venue,String> {
}
