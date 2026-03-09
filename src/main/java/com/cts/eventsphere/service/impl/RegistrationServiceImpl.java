package com.cts.eventsphere.service.impl;

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
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RegistrationServiceImpl implements RegistrationService {
    private final RegistrationRepository registrationRepo;

    @Override
    public GenericResponse registerForEvent(String userId, String eventId, String ticketId) {
        var registration = registrationRepo.findByAttendeeIdAndEventId(userId, eventId);
        if (registration != null) {
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

    @Override
    public GenericResponse deleteRegistration(String registrationId) {
        if (!registrationRepo.existsById(registrationId)) {
            throw new RegistrationNotFoundException(String.format("Registration with id %s not found", registrationId));
        }
        registrationRepo.deleteById(registrationId);
        log.info("Registration with id {} deleted", registrationId);
        return new GenericResponse("Registration deleted successfully");
    }

    @Override
    public GenericResponse cancelRegistration(String registrationId) {
        var registration = registrationRepo.findById(registrationId)
                .orElseThrow(() -> new RegistrationNotFoundException(String.format("Registration with id %s not found", registrationId)));
        registration.setStatus(RegistrationStatus.cancelled);
        registrationRepo.save(registration);
        log.info("Registration with id {} cancelled", registrationId);
        return new GenericResponse("Registration cancelled successfully");
    }

    @Override
    public GenericResponse approveRegistration(String registrationId) {
        var registration = registrationRepo.findById(registrationId)
                .orElseThrow(() -> new RegistrationNotFoundException(String.format("Registration with id %s not found", registrationId)));
        registration.setStatus(RegistrationStatus.confirmed);
        registrationRepo.save(registration);
        log.info("Registration with id {} approved", registrationId);
        return new GenericResponse("Registration approved successfully");
    }

    @Override
    public GenericResponse rejectRegistration(String registrationId) {
        var registration = registrationRepo.findById(registrationId)
                .orElseThrow(() -> new RegistrationNotFoundException(String.format("Registration with id %s not found", registrationId)));
        registration.setStatus(RegistrationStatus.cancelled);
        registrationRepo.save(registration);
        log.info("Registration with id {} rejected", registrationId);
        return new GenericResponse("Registration rejected successfully");
    }

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

    @Override
    public RegistrationDTO getRegistrationById(String registrationId) {
        var registration = registrationRepo.findById(registrationId)
                .orElseThrow(() -> new RegistrationNotFoundException(String.format("Registration with id %s not found", registrationId)));
        log.info("Fetched registration with id: {}", registrationId);
        return RegistrationDTOMapper.toDTO(registration);
    }


    @Override
    public RegistrationDTO getRegistrationByEventIdAndUserId(String eventId, String userId) {
        var registration = registrationRepo.findByAttendeeIdAndEventId(userId, eventId);
        if (registration == null) {
            throw new RegistrationNotFoundException(String.format("Registration for user %s and event %s not found", userId, eventId));
        }
        log.info("Fetched registration for userId: {}, eventId: {}", userId, eventId);
        return RegistrationDTOMapper.toDTO(registration);
    }
}
