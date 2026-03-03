package com.cts.eventsphere.repository;

import com.cts.eventsphere.model.Engagement;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Jpa repository for engagement entity
 *
 * @author 2480027
 * @version 1.0
 * @since 03-03-2026
 */
public interface EngagementRepository extends JpaRepository<Engagement, String> {

}
