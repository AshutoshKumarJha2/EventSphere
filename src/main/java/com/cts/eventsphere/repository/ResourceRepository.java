package com.cts.eventsphere.repository;

import com.cts.eventsphere.model.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * JPA Repository for the Resource Entity.
 * @author 2479476
 * @version 1.1
 * @since 27-02-2026
 */
@Repository
public interface ResourceRepository extends JpaRepository<Resource, String> {
    Resource findByName(String name);

    List<Resource> findByVenue_VenueId(String venueId);
}