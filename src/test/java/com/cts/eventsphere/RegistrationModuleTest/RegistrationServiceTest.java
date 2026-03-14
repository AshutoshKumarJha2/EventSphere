package com.cts.eventsphere.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cts.eventsphere.dto.shared.GenericResponse;
import com.cts.eventsphere.exception.registration.RegistrationAlreadyExistsException;
import com.cts.eventsphere.exception.registration.RegistrationNotFoundException;
import com.cts.eventsphere.model.Registration;
import com.cts.eventsphere.model.data.RegistrationStatus;
import com.cts.eventsphere.repository.RegistrationRepository;
import com.github.javafaker.Faker;

@ExtendWith(MockitoExtension.class)
class RegistrationServiceTest {

    @Mock
    private RegistrationRepository registrationRepo;

    @InjectMocks
    private RegistrationServiceImpl registrationService;

    private Faker faker;
    private Registration mockRegistration;
    private String userId;
    private String eventId;
    private String ticketId;
    private String registrationId;

    @BeforeEach
    void setUp() {
        faker = new Faker();
        userId = faker.internet().uuid();
        eventId = faker.internet().uuid();
        ticketId = faker.internet().uuid();
        registrationId = faker.internet().uuid();

        mockRegistration = Registration.builder()
                .registrationId(registrationId)
                .attendeeId(userId)
                .eventId(eventId)
                .ticketId(ticketId)
                .status(RegistrationStatus.pending)
                .build();
    }

    @Test
    void registerForEvent_Success() {
        when(registrationRepo.findByAttendeeIdAndEventId(userId, eventId)).thenReturn(null);
        when(registrationRepo.save(any(Registration.class))).thenReturn(mockRegistration);

        GenericResponse response = registrationService.registerForEvent(userId, eventId, ticketId);

        assertEquals("Registration successful", response.message());
        verify(registrationRepo, times(1)).save(any(Registration.class));
    }

    @Test
    void registerForEvent_ThrowsAlreadyExistsException() {
        when(registrationRepo.findByAttendeeIdAndEventId(userId, eventId)).thenReturn(mockRegistration);

        RegistrationAlreadyExistsException exception = assertThrows(
                RegistrationAlreadyExistsException.class,
                () -> registrationService.registerForEvent(userId, eventId, ticketId)
        );
        assertTrue(exception.getMessage().contains("already registered"));
        verify(registrationRepo, never()).save(any(Registration.class));
    }

    @Test
    void approveRegistration_Success() {
        when(registrationRepo.findById(registrationId)).thenReturn(Optional.of(mockRegistration));
        when(registrationRepo.save(any(Registration.class))).thenReturn(mockRegistration);

        GenericResponse response = registrationService.approveRegistration(registrationId);

        assertEquals("Registration approved successfully", response.message());
        assertEquals(RegistrationStatus.confirmed, mockRegistration.getStatus());
        verify(registrationRepo, times(1)).save(mockRegistration);
    }

    @Test
    void cancelRegistration_ThrowsNotFoundException() {
        when(registrationRepo.findById(registrationId)).thenReturn(Optional.empty());

        assertThrows(
                RegistrationNotFoundException.class,
                () -> registrationService.cancelRegistration(registrationId)
        );
    }
}