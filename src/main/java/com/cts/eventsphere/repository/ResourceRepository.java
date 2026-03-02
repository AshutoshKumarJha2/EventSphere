package com.cts.eventsphere.repository;

import com.cts.eventsphere.model.Resource;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * JPA Repository for the Resource Entity.
 * * @author 2479476
 *
 * @version 1.0
 * @since 27-02-2026
 */

public interface ResourceRepository extends JpaRepository<Resource,String> {
}
