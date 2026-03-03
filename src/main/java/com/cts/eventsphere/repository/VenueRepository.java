package com.cts.eventsphere.repository;

import com.cts.eventsphere.model.Venue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * JPA Repository for the Venue Entity.
 * * @author 2479476
 *
 * @version 1.0
 * @since 26-02-2026
 */

@Repository
public interface VenueRepository extends JpaRepository<Venue,String> {

    List<Venue> findByLocation(String location);

    List<Venue> findByCapacity(int capacity);

}
