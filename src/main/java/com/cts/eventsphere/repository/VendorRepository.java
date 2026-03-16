package com.cts.eventsphere.repository;

import com.cts.eventsphere.model.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * JPA Repository for managing vendor entities.
 * Provides abstraction for the persistence layer, enabling standard CRUD
 * operations and custom query execution against the 'vendor' table.
 * * Adheres to professional coding standards for data access objects.
 *
 * @author 2480177
 * @version 1.0
 * @since 26-02-2026
 */

@Repository
public interface VendorRepository extends JpaRepository<Vendor,String> {
}
