package com.cts.eventsphere.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cts.eventsphere.dto.registration.RegistrationListResponseDTO;
import com.cts.eventsphere.dto.registration.RegistrationRequestDTO;
import com.cts.eventsphere.dto.shared.GenericResponse;
import com.cts.eventsphere.security.UserPrincipal;
import com.cts.eventsphere.service.RegistrationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


/**
 * RegistrationController class is responsible for handling HTTP requests related to event registrations.
 * It provides endpoints for creating, retrieving, updating, and deleting registrations.
 *
 * @author test-in-prod-10x
 * @version 1.0
 * @since 2026-03-08
 */
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Slf4j
public class RegistrationController {
    private final RegistrationService registrationService;

    /**
     * Registers an attendee for a specific event using a selected ticket.
     *
     * @param eventId     The unique identifier of the event.
     * @param userDetails The authenticated user principal representing the attendee.
     * @param request     The registration request containing ticket information.
     * @return A {@link ResponseEntity} containing a {@link GenericResponse} with the registration result.
     */
    @PostMapping("/events/{eventId}/registrations")
    @PreAuthorize("hasRole('ATTENDEE')")
    public ResponseEntity<GenericResponse> createRegistration(@PathVariable String eventId, @AuthenticationPrincipal UserPrincipal userDetails, @RequestBody @Valid RegistrationRequestDTO request) {
        var userId = userDetails.userId();
        var ticketId = request.ticketId();
        log.info("Creating registration for eventId: {}, userId: {}, ticketId: {}", eventId, userId, ticketId);
        return ResponseEntity.ok(registrationService.registerForEvent(userId, eventId, ticketId));
    }

    /**
     * Retrieves a paginated list of all registrations for a specific event.
     * Accessible only by Organizers or Admins.
     *
     * @param eventId The unique identifier of the event.
     * @param size    The number of records per page (defaults to 10).
     * @param page    The page number to retrieve (defaults to 0).
     * @return A {@link ResponseEntity} containing {@link RegistrationListResponseDTO} with the list of registrations.
     */
    @GetMapping("/events/{eventId}/registrations")
    @PreAuthorize("hasAnyRole('ORGANIZER', 'ADMIN')")
    public ResponseEntity<RegistrationListResponseDTO> getAllRegistrationsByEvent(@PathVariable String eventId, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "0") int page) {
        log.info("Getting all events for eventId: {}", eventId);
        return ResponseEntity.ok(registrationService.getRegistrationsByEventId(eventId, size, page));
    }
    

    /**
     * Cancels an existing registration.
     * This endpoint is intended for use by the Attendee.
     *
     * @param registrationId The unique identifier of the registration to cancel.
     * @return A {@link ResponseEntity} containing a {@link GenericResponse} confirming the cancellation.
     */
    @PatchMapping("/registrations/{registrationId}/cancel")
    @PreAuthorize("hasRole('ATTENDEE')")
    public ResponseEntity<GenericResponse> cancelRegistration(@PathVariable String registrationId) {
        log.info("Cancelling registration with registrationId: {}", registrationId);
        return ResponseEntity.ok(registrationService.cancelRegistration(registrationId));
    }

    /**
     * Approves a pending event registration.
     * Accessible only by Organizers or Admins.
     *
     * @param registrationId The unique identifier of the registration to approve.
     * @return A {@link ResponseEntity} containing a {@link GenericResponse} confirming the approval.
     */
    @PatchMapping("/registrations/{registrationId}/approve")
    @PreAuthorize("hasAnyRole('ORGANIZER', 'ADMIN')")
    public ResponseEntity<GenericResponse> approveRegistration(@PathVariable String registrationId) {
        log.info("Approving registration with registrationId: {}", registrationId);
        return ResponseEntity.ok(registrationService.approveRegistration(registrationId));
    }

    /**
     * Rejects a pending event registration.
     * Accessible only by Organizers or Admins.
     *
     * @param registrationId The unique identifier of the registration to reject.
     * @return A {@link ResponseEntity} containing a {@link GenericResponse} confirming the rejection.
     */
    @PatchMapping("/registrations/{registrationId}/reject")
    @PreAuthorize("hasAnyRole('ORGANIZER','ADMIN')")
    public ResponseEntity<GenericResponse> rejectRegistration(@PathVariable String registrationId) {
        log.info("Rejecting registration with registrationId: {}", registrationId);
        return ResponseEntity.ok(registrationService.rejectRegistration(registrationId));
    }
}