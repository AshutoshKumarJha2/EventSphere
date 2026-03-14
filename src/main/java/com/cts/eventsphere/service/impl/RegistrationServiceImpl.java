package com.cts.eventsphere.service.impl;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.cts.eventsphere.dto.mapper.registration.RegistrationDTOMapper;
import com.cts.eventsphere.dto.registration.RegistrationDTO;
import com.cts.eventsphere.dto.registration.RegistrationListResponseDTO;
import com.cts.eventsphere.dto.shared.GenericResponse;
import com.cts.eventsphere.exception.registration.RegistrationAlreadyExistsException;
import com.cts.eventsphere.exception.registration.RegistrationNotFoundException;
import com.cts.eventsphere.model.Registration;
import com.cts.eventsphere.model.data.RegistrationStatus;
import com.cts.eventsphere.repository.RegistrationRepository;
import com.cts.eventsphere.service.RegistrationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementation of {@link RegistrationService} for managing event registrations.
 * * @author test-in-prod-10x
 * @version 1.0
 * @since 2026-03-08
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RegistrationServiceImpl implements RegistrationService {
    private final RegistrationRepository registrationRepo;

    /**
     * Registers a user for an event with a specific ticket.
     * * @param userId   The unique identifier of the user.
     * @param eventId  The unique identifier of the event.
     * @param ticketId The unique identifier of the selected ticket.
     * @return A {@link GenericResponse} indicating successful registration.
     * @throws RegistrationAlreadyExistsException if the user is already registered for the event.
     */
    @Override
    public GenericResponse registerForEvent(String userId, String eventId, String ticketId) {
        var registration = registrationRepo.findByAttendeeIdAndEventId(userId, eventId);
        if (registration.isPresent()) {
            throw new RegistrationAlreadyExistsException(String.format("User %s is already registered for event %s", userId, eventId));
        }
        var newRegistration = Registration.builder()
                .attendeeId(userId)
                .eventId(eventId)
                .ticketId(ticketId)
                .status(RegistrationStatus.pending)
                .build();
        registrationRepo.save(newRegistration);
        log.info("User {} registered for event {} with ticket {}", userId, eventId, ticketId);
        return new GenericResponse("Registration successful");
    }

    /**
     * Permanently deletes a registration record from the system.
     * * @param registrationId The unique identifier of the registration.
     * @return A {@link GenericResponse} indicating successful deletion.
     * @throws RegistrationNotFoundException if the registration ID does not exist.
     */
    @Override
    public GenericResponse deleteRegistration(String registrationId) {
        if (!registrationRepo.existsById(registrationId)) {
            throw new RegistrationNotFoundException(String.format("Registration with id %s not found", registrationId));
        }
        registrationRepo.deleteById(registrationId);
        log.info("Registration with id {} deleted", registrationId);
        return new GenericResponse("Registration deleted successfully");
    }

    /**
     * Cancels a registration by updating its status.
     * * @param registrationId The unique identifier of the registration.
     * @return A {@link GenericResponse} indicating successful cancellation.
     * @throws RegistrationNotFoundException if the registration ID does not exist.
     */
    @Override
    public GenericResponse cancelRegistration(String registrationId) {
        var registration = registrationRepo.findById(registrationId)
                .orElseThrow(() -> new RegistrationNotFoundException(String.format("Registration with id %s not found", registrationId)));
        registration.setStatus(RegistrationStatus.cancelled);
        registrationRepo.save(registration);
        log.info("Registration with id {} cancelled", registrationId);
        return new GenericResponse("Registration cancelled successfully");
    }

    /**
     * Approves a registration, changing its status to confirmed.
     * * @param registrationId The unique identifier of the registration.
     * @return A {@link GenericResponse} indicating successful approval.
     * @throws RegistrationNotFoundException if the registration ID does not exist.
     */
    @Override
    public GenericResponse approveRegistration(String registrationId) {
        var registration = registrationRepo.findById(registrationId)
                .orElseThrow(() -> new RegistrationNotFoundException(String.format("Registration with id %s not found", registrationId)));
        registration.setStatus(RegistrationStatus.confirmed);
        registrationRepo.save(registration);
        log.info("Registration with id {} approved", registrationId);
        return new GenericResponse("Registration approved successfully");
    }

    /**
     * Rejects a registration, changing its status to cancelled.
     * * @param registrationId The unique identifier of the registration.
     * @return A {@link GenericResponse} indicating successful rejection.
     * @throws RegistrationNotFoundException if the registration ID does not exist.
     */
    @Override
    public GenericResponse rejectRegistration(String registrationId) {
        var registration = registrationRepo.findById(registrationId)
                .orElseThrow(() -> new RegistrationNotFoundException(String.format("Registration with id %s not found", registrationId)));
        registration.setStatus(RegistrationStatus.cancelled);
        registrationRepo.save(registration);
        log.info("Registration with id {} rejected", registrationId);
        return new GenericResponse("Registration rejected successfully");
    }

    /**
     * Retrieves a paginated list of registrations for a specific user.
     * * @param userId The unique identifier of the attendee.
     * @param size   The page size.
     * @param page   The page number.
     * @return A {@link RegistrationListResponseDTO} containing the user's registrations.
     */
    @Override
    public RegistrationListResponseDTO getRegistrationsByUserId(String userId, int size, int page) {
        var pagable = PageRequest.of(page, size);
        var pages = registrationRepo.findByAttendeeId(userId, pagable);
        var registrations = pages.getContent().stream().map(RegistrationDTOMapper::toDTO).toList();
        var pageNo = pages.getNumber();
        var pageSize = pages.getSize();
        var totalElements = pages.getTotalElements();
        var totalPages = pages.getTotalPages();

        log.info("Fetched {} registrations for userId: {}, page: {}, size: {}", registrations.size(), userId, page, size);
        return new RegistrationListResponseDTO(
                registrations,
                pageNo,
                pageSize,
                totalElements,
                totalPages
        );

    }

    /**
     * Retrieves a paginated list of all registrations for a specific event.
     * * @param eventId The unique identifier of the event.
     * @param size    The page size.
     * @param page    The page number.
     * @return A {@link RegistrationListResponseDTO} containing all registrations for the event.
     */
    @Override
    public RegistrationListResponseDTO getRegistrationsByEventId(String eventId, int size, int page) {
        var pagable = PageRequest.of(page, size);
        var pages = registrationRepo.findByEventId(eventId, pagable);
        var registrations = pages.getContent().stream().map(RegistrationDTOMapper::toDTO).toList();
        var pageNo = pages.getNumber();
        var pageSize = pages.getSize();
        var totalElements = pages.getTotalElements();
        var totalPages = pages.getTotalPages();

        log.info("Fetched {} registrations for eventId: {}, page: {}, size: {}", registrations.size(), eventId, page, size);
        return new RegistrationListResponseDTO(
                registrations,
                pageNo,
                pageSize,
                totalElements,
                totalPages
        );
    }

    /**
     * Retrieves a paginated list of all registrations in the system.
     * * @param size The page size.
     * @param page The page number.
     * @return A {@link RegistrationListResponseDTO} containing all registrations.
     */
    @Override
    public RegistrationListResponseDTO getAllRegistrations(int size, int page) {
        var pagable = PageRequest.of(page, size);
        var pages = registrationRepo.findAll(pagable);
        var registrations = pages.getContent().stream().map(RegistrationDTOMapper::toDTO).toList();
        var pageNo = pages.getNumber();
        var pageSize = pages.getSize();
        var totalElements = pages.getTotalElements();
        var totalPages = pages.getTotalPages();

        log.info("Fetched {} registrations, page: {}, size: {}", registrations.size(), page, size);
        return new RegistrationListResponseDTO(
                registrations,
                pageNo,
                pageSize,
                totalElements,
                totalPages
        );

    }

    /**
     * Retrieves the details of a specific registration by its ID.
     * * @param registrationId The unique identifier of the registration.
     * @return The {@link RegistrationDTO} representation of the registration.
     * @throws RegistrationNotFoundException if the registration ID does not exist.
     */
    @Override
    public RegistrationDTO getRegistrationById(String registrationId) {
        var registration = registrationRepo.findById(registrationId)
                .orElseThrow(() -> new RegistrationNotFoundException(String.format("Registration with id %s not found", registrationId)));
        log.info("Fetched registration with id: {}", registrationId);
        return RegistrationDTOMapper.toDTO(registration);
    }


    /**
     * Retrieves a registration record for a specific user and event combination.
     * * @param eventId The unique identifier of the event.
     * @param userId  The unique identifier of the user.
     * @return The {@link RegistrationDTO} representation of the registration.
     * @throws RegistrationNotFoundException if no registration is found for the given user and event.
     */
    @Override
    public RegistrationDTO getRegistrationByEventIdAndUserId(String eventId, String userId) {
        var registration = registrationRepo.findByAttendeeIdAndEventId(userId, eventId).orElseThrow(() -> new RegistrationNotFoundException(String.format("Registration with eventId: %s and userId: %s not found", eventId, userId)));
        log.info("Fetched registration for userId: {}, eventId: {}", userId, eventId);
        return RegistrationDTOMapper.toDTO(registration);
    }
}