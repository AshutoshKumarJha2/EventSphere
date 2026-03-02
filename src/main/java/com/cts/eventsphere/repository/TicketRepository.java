package com.cts.eventsphere.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cts.eventsphere.model.Ticket;

/**
 * Ticket repository for getting ticket and saving tickets
 *
 * @author test-in-prod-10x
 * @version 1.0
 * @since 2026-03-02
 */
public interface TicketRepository extends JpaRepository<Ticket, String> {
    
}
