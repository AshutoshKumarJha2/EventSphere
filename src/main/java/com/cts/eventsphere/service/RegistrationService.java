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
    GenericResponse registerForEvent(String userId, String eventId, String ticketId);
    RegistrationDTO getRegistrationById(String registrationId);
    RegistrationDTO getRegistrationByEventIdAndUserId(String eventId, String userId);
    GenericResponse deleteRegistration(String registrationId);
    GenericResponse cancelRegistration(String registrationId);
    GenericResponse approveRegistration(String registrationId);
    GenericResponse rejectRegistration(String registrationId);
    RegistrationListResponseDTO getRegistrationsByUserId(String userId, int size, int page);
    RegistrationListResponseDTO getRegistrationsByEventId(String eventId, int size, int page);
    RegistrationListResponseDTO getAllRegistrations(int size, int page);
}
