package com.cts.eventsphere.dto.ticket;

import com.cts.eventsphere.model.data.TicketStatus;

/**
 * DTO object for creating ticket request
 *
 * @author test-in-prod-10x
 * @version 1.0
 * @since 2026-03-05
 */
public record CreateTicketRequest(
        String type,
        double price,
        TicketStatus status
    ) {
    
}
