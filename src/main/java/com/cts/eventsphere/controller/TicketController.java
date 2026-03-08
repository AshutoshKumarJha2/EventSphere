package com.cts.eventsphere.controller;

import com.cts.eventsphere.dto.ticket.CreateTicketRequest;
import com.cts.eventsphere.dto.ticket.TicketListResponseDTO;
import com.cts.eventsphere.security.UserPrincipal;
import com.cts.eventsphere.service.TicketService;
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
@Controller
@RequestMapping("/api/v1")
public class TicketController {
    private TicketService ticketService;

    @PostMapping("/events/{eventId}/tickets")
    @PreAuthorize("hasRole('organizer')")
    public ResponseEntity<GenericResponse> createTicket(@RequestBody CreateTicketRequest request, @PathVariable String eventId, @AuthenticationPrincipal UserPrincipal userDetails) {
        return ResponseEntity.ok(ticketService.createTicket(eventId, request.type(), request.price(), request.status()));
    }

    @GetMapping("/events/{eventId}/tickets")
    public ResponseEntity<TicketListResponseDTO> getTicketsByEventId(@PathVariable String eventId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(ticketService.getTicketsByEventId(eventId, page, size));
    }

    @PutMapping("/tickets/{ticketId}")
    @PreAuthorize("hasRole('organizer') or hasRole('admin')")
    public ResponseEntity<GenericResponse> updateTicket(@PathVariable String ticketId, @RequestBody CreateTicketRequest request) {
        return ResponseEntity.ok(ticketService.updateTicket(ticketId, request.type(), request.price(), request.status()));
    }

    @DeleteMapping("/tickets/{ticketId}")
    @PreAuthorize("hasRole('organizer') or hasRole('admin')")
    public ResponseEntity<GenericResponse> deleteTicket(@PathVariable String ticketId) {
        return ResponseEntity.ok(ticketService.deleteTicket(ticketId));
    }
}
