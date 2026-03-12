package com.cts.eventsphere.controller;

import com.cts.eventsphere.dto.ticket.CreateTicketRequest;
import com.cts.eventsphere.dto.ticket.TicketListResponseDTO;
import com.cts.eventsphere.security.UserPrincipal;
import com.cts.eventsphere.service.TicketService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.cts.eventsphere.dto.shared.GenericResponse;

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

    @PostMapping("/events/{eventId}/tickets")
    @PreAuthorize("hasRole('ORGANIZER')")
    public ResponseEntity<GenericResponse> createTicket(@RequestBody @Valid CreateTicketRequest request, @PathVariable String eventId, @AuthenticationPrincipal UserPrincipal userDetails) {
        log.info("Creating ticket for eventId: {}, userId: {}, request: {}", eventId, userDetails.userId(), request);
        return ResponseEntity.ok(ticketService.createTicket(eventId, request.type(), request.price(), request.status()));
    }

    @GetMapping("/events/{eventId}/tickets")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<TicketListResponseDTO> getTicketsByEventId(@PathVariable String eventId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        log.info("Fetching tickets for eventId: {}, page: {}, size: {}", eventId, page, size);
        return ResponseEntity.ok(ticketService.getTicketsByEventId(eventId, page, size));
    }

    @PutMapping("/tickets/{ticketId}")
    @PreAuthorize("hasRole('ORGANIZER') or hasRole('ADMIN')")
    public ResponseEntity<GenericResponse> updateTicket(@PathVariable String ticketId, @RequestBody CreateTicketRequest request) {
        log.info("Updating ticket with ticketId: {}, request: {}", ticketId, request);
        return ResponseEntity.ok(ticketService.updateTicket(ticketId, request.type(), request.price(), request.status()));
    }

    @DeleteMapping("/tickets/{ticketId}")
    @PreAuthorize("hasAnyRole('ORGANIZER', 'ADMIN')")
    public ResponseEntity<GenericResponse> deleteTicket(@PathVariable String ticketId) {
        log.info("Deleting ticket with ticketId: {}", ticketId);
        return ResponseEntity.ok(ticketService.deleteTicket(ticketId));
    }
}
