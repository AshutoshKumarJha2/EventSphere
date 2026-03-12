package com.cts.eventsphere.service;

import com.cts.eventsphere.dto.registration.RegistrationDTO;
import com.cts.eventsphere.dto.registration.RegistrationListResponseDTO;
import com.cts.eventsphere.dto.shared.GenericResponse;

/**
 * Retistratoin Service Interface for handeling registration
 *
 * @author test-in-prod-10x
 * @version 1.0
 * @since 2026-03-05
 */
public interface RegistrationService {
    
    /**
     * Registers a user for a specific event with a chosen ticket.
     *
     * @param userId   The unique identifier of the user/attendee.
     * @param eventId  The unique identifier of the event.
     * @param ticketId The unique identifier of the ticket type.
     * @return A {@link GenericResponse} indicating the result of the registration.
     */
    GenericResponse registerForEvent(String userId, String eventId, String ticketId);

    /**
     * Retrieves the details of a specific registration by its ID.
     *
     * @param registrationId The unique identifier of the registration.
     * @return A {@link RegistrationDTO} containing the registration details.
     */
    RegistrationDTO getRegistrationById(String registrationId);

    /**
     * Retrieves a registration record based on a specific event and user combination.
     *
     * @param eventId The unique identifier of the event.
     * @param userId  The unique identifier of the user.
     * @return A {@link RegistrationDTO} representing the specific registration.
     */
    RegistrationDTO getRegistrationByEventIdAndUserId(String eventId, String userId);

    /**
     * Permanently deletes a registration record from the system.
     *
     * @param registrationId The unique identifier of the registration to delete.
     * @return A {@link GenericResponse} indicating successful deletion.
     */
    GenericResponse deleteRegistration(String registrationId);

    /**
     * Cancels an existing registration, typically initiated by the attendee.
     *
     * @param registrationId The unique identifier of the registration to cancel.
     * @return A {@link GenericResponse} indicating successful cancellation.
     */
    GenericResponse cancelRegistration(String registrationId);

    /**
     * Approves a pending registration, typically initiated by an organizer or admin.
     *
     * @param registrationId The unique identifier of the registration to approve.
     * @return A {@link GenericResponse} indicating successful approval.
     */
    GenericResponse approveRegistration(String registrationId);

    /**
     * Rejects a pending registration.
     *
     * @param registrationId The unique identifier of the registration to reject.
     * @return A {@link GenericResponse} indicating successful rejection.
     */
    GenericResponse rejectRegistration(String registrationId);

    /**
     * Retrieves a paginated list of registrations associated with a specific user.
     *
     * @param userId The unique identifier of the user.
     * @param size   The number of records per page.
     * @param page   The page number to retrieve.
     * @return A {@link RegistrationListResponseDTO} containing the user's registrations.
     */
    RegistrationListResponseDTO getRegistrationsByUserId(String userId, int size, int page);

    /**
     * Retrieves a paginated list of all registrations for a specific event.
     *
     * @param eventId The unique identifier of the event.
     * @param size    The number of records per page.
     * @param page    The page number to retrieve.
     * @return A {@link RegistrationListResponseDTO} containing the event's registrations.
     */
    RegistrationListResponseDTO getRegistrationsByEventId(String eventId, int size, int page);

    /**
     * Retrieves a paginated list of all registrations across the entire system.
     *
     * @param size The number of records per page.
     * @param page The page number to retrieve.
     * @return A {@link RegistrationListResponseDTO} containing all registration records.
     */
    RegistrationListResponseDTO getAllRegistrations(int size, int page);
}