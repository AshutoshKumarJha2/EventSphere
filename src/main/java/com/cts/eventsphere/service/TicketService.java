package com.cts.eventsphere.service;

import com.cts.eventsphere.dto.shared.GenericResponse;
import com.cts.eventsphere.dto.ticket.TicketListResponseDTO;
import com.cts.eventsphere.dto.ticket.TicketResponseDTO;
import com.cts.eventsphere.model.data.TicketStatus;

/**
 * Service interface for managing ticket-related operations.
 * * @author test-in-prod-10x
 * @version 1.0
 * @since 2026-03-08
 */
public interface TicketService {

    /**
     * Creates a new ticket entry for a specific event.
     *
     * @param eventId The unique identifier of the event.
     * @param type    The category of the ticket (e.g., General, VIP).
     * @param price   The cost associated with the ticket.
     * @param status  The initial status of the ticket (e.g., available, sold_out).
     * @return A {@link GenericResponse} containing the operation status message.
     */
    GenericResponse createTicket(String eventId, String type, double price, TicketStatus status);

    /**
     * Retrieves a paginated list of tickets for a specific event.
     *
     * @param eventId The unique identifier of the event.
     * @param page    The page number to retrieve.
     * @param size    The number of records per page.
     * @return A {@link TicketListResponseDTO} containing the list of tickets and pagination details.
     */
    TicketListResponseDTO getTicketsByEventId(String eventId, int page, int size);

    /**
     * Retrieves a paginated list of all tickets across all events.
     *
     * @param page The page number to retrieve.
     * @param size The number of records per page.
     * @return A {@link TicketListResponseDTO} containing the full list of tickets.
     */
    TicketListResponseDTO getAllTickets(int page, int size);

    /**
     * Fetches details for a specific ticket by its unique identifier.
     *
     * @param ticketId The unique identifier of the ticket.
     * @return A {@link TicketResponseDTO} containing the ticket details.
     */
    TicketResponseDTO getTicketById(String ticketId);

    /**
     * Updates the details of an existing ticket.
     *
     * @param ticketId The unique identifier of the ticket to be updated.
     * @param type     The updated category of the ticket.
     * @param price    The updated cost.
     * @param status   The updated status.
     * @return A {@link GenericResponse} containing the operation status message.
     */
    GenericResponse updateTicket(String ticketId, String type, double price, TicketStatus status);

    /**
     * Deletes a ticket from the system.
     *
     * @param ticketId The unique identifier of the ticket to be removed.
     * @return A {@link GenericResponse} containing the operation status message.
     */
    GenericResponse deleteTicket(String ticketId);
}