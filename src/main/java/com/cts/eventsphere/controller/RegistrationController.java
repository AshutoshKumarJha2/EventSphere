package com.cts.eventsphere.controller;

import com.cts.eventsphere.dto.registration.RegistrationListResponseDTO;
import com.cts.eventsphere.dto.registration.RegistrationRequestDTO;
import com.cts.eventsphere.dto.shared.GenericResponse;
import com.cts.eventsphere.security.UserPrincipal;
import com.cts.eventsphere.service.RegistrationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


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

    @PostMapping("/events/{eventId}/registrations")
    @PreAuthorize("hasRole('ATTENDEE')")
    public ResponseEntity<GenericResponse> createRegistration(@PathVariable String eventId, @AuthenticationPrincipal UserPrincipal userDetails, @RequestBody @Valid RegistrationRequestDTO request) {
        var userId = userDetails.userId();
        var ticketId = request.ticketId();
        log.info("Creating registration for eventId: {}, userId: {}, ticketId: {}", eventId, userId, ticketId);
        return ResponseEntity.ok(registrationService.registerForEvent(userId, eventId, ticketId));
    }

    @GetMapping("/events/{eventId}/registrations")
    @PreAuthorize("hasAnyRole('ORGANIZER', 'ADMIN')")
    public ResponseEntity<RegistrationListResponseDTO> getAllRegistrationsByEvent(@PathVariable String eventId, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "0") int page) {
        log.info("Getting all events for eventId: {}", eventId);
        return ResponseEntity.ok(registrationService.getRegistrationsByEventId(eventId, size, page));
    }
    

    @PatchMapping("/registrations/{registrationId}/cancel")
    @PreAuthorize("hasRole('ATTENDEE')")
    public ResponseEntity<GenericResponse> cancelRegistration(@PathVariable String registrationId) {
        log.info("Cancelling registration with registrationId: {}", registrationId);
        return ResponseEntity.ok(registrationService.cancelRegistration(registrationId));
    }

    @PatchMapping("/registrations/{registrationId}/approve")
    @PreAuthorize("hasAnyRole('ORGANIZER', 'ADMIN')")
    public ResponseEntity<GenericResponse> approveRegistration(@PathVariable String registrationId) {
        log.info("Approving registration with registrationId: {}", registrationId);
        return ResponseEntity.ok(registrationService.approveRegistration(registrationId));
    }

    @PatchMapping("/registrations/{registrationId}/reject")
    @PreAuthorize("hasAnyRole('ORGANIZER','ADMIN')")
    public ResponseEntity<GenericResponse> rejectRegistration(@PathVariable String registrationId) {
        log.info("Rejecting registration with registrationId: {}", registrationId);
        return ResponseEntity.ok(registrationService.rejectRegistration(registrationId));
    }
}
