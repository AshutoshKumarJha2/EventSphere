package com.cts.eventsphere.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cts.eventsphere.dto.shared.GenericResponse;
import com.cts.eventsphere.dto.ticket.CreateTicketRequest;
import com.cts.eventsphere.dto.ticket.TicketListResponseDTO;
import com.cts.eventsphere.security.UserPrincipal;
import com.cts.eventsphere.service.TicketService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Controller for ticket creation and updation
 *
 * @author test-in-prod-10x
 * @version 1.0
 * @since 2026-03-07
 */
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Slf4j
public class TicketController {
    private final TicketService ticketService;

    /**
     * Creates a new ticket for a specific event.
     *
     * @param request     The request body containing ticket details (type, price, status).
     * @param eventId     The unique identifier of the event to create the ticket for.
     * @param userDetails The currently authenticated user's details.
     * @return A {@link ResponseEntity} containing a {@link GenericResponse} indicating the result of the creation.
     */
    @PostMapping("/events/{eventId}/tickets")
    @PreAuthorize("hasRole('ORGANIZER')")
    public ResponseEntity<GenericResponse> createTicket(@RequestBody @Valid CreateTicketRequest request, @PathVariable String eventId, @AuthenticationPrincipal UserPrincipal userDetails) {
        log.info("Creating ticket for eventId: {}, userId: {}, request: {}", eventId, userDetails.userId(), request);
        return ResponseEntity.ok(ticketService.createTicket(eventId, request.type(), request.price(), request.status()));
    }

    /**
     * Retrieves a paginated list of tickets associated with a specific event.
     *
     * @param eventId The unique identifier of the event.
     * @param page    The page number to retrieve (defaults to 0).
     * @param size    The number of records per page (defaults to 10).
     * @return A {@link ResponseEntity} containing a {@link TicketListResponseDTO} with the paginated tickets.
     */
    @GetMapping("/events/{eventId}/tickets")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<TicketListResponseDTO> getTicketsByEventId(@PathVariable String eventId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        log.info("Fetching tickets for eventId: {}, page: {}, size: {}", eventId, page, size);
        return ResponseEntity.ok(ticketService.getTicketsByEventId(eventId, page, size));
    }

    /**
     * Updates an existing ticket's details.
     *
     * @param ticketId The unique identifier of the ticket to update.
     * @param request  The request body containing the updated ticket details (type, price, status).
     * @return A {@link ResponseEntity} containing a {@link GenericResponse} indicating the result of the update.
     */
    @PutMapping("/tickets/{ticketId}")
    @PreAuthorize("hasRole('ORGANIZER') or hasRole('ADMIN')")
    public ResponseEntity<GenericResponse> updateTicket(@PathVariable String ticketId, @RequestBody CreateTicketRequest request) {
        log.info("Updating ticket with ticketId: {}, request: {}", ticketId, request);
        return ResponseEntity.ok(ticketService.updateTicket(ticketId, request.type(), request.price(), request.status()));
    }

    /**
     * Deletes a specific ticket from the system.
     *
     * @param ticketId The unique identifier of the ticket to delete.
     * @return A {@link ResponseEntity} containing a {@link GenericResponse} indicating the result of the deletion.
     */
    @DeleteMapping("/tickets/{ticketId}")
    @PreAuthorize("hasAnyRole('ORGANIZER', 'ADMIN')")
    public ResponseEntity<GenericResponse> deleteTicket(@PathVariable String ticketId) {
        log.info("Deleting ticket with ticketId: {}", ticketId);
        return ResponseEntity.ok(ticketService.deleteTicket(ticketId));
    }
}