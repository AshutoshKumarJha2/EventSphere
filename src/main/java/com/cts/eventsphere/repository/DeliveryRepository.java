package com.cts.eventsphere.repository;

import com.cts.eventsphere.model.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * JPA Repository interface for CRUD operations on delivery entities.
 * Facilitates the tracking of logistical items and resource fulfillment.
 *
 * @author 2480177
 * @version 1.0
 * @since 26-02-2026
 */

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, String> {

}
