package com.cts.eventsphere.RegistrationModuleTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;

import com.cts.eventsphere.model.Event;
import com.cts.eventsphere.model.Ticket;
import com.cts.eventsphere.model.User;
import com.cts.eventsphere.repository.EventRepository;
import com.cts.eventsphere.repository.TicketRepository;
import com.cts.eventsphere.service.AuditService;
import com.cts.eventsphere.service.NotificationService;
import com.cts.eventsphere.service.impl.RegistrationServiceImpl;
import jakarta.persistence.EntityManager;
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

    @Mock
    private EntityManager entityManager;

    @Mock
    private NotificationService notificationService;

    @Mock
    private AuditService auditService;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private TicketRepository ticketRepository;

    @InjectMocks
    private RegistrationServiceImpl registrationService;

    private Faker faker;
    private Registration mockRegistration;
    private String actorId;
    private String userId;
    private String eventId;
    private String ticketId;
    private String registrationId;

    @BeforeEach
    void setUp() {
        faker = new Faker();
        actorId = faker.internet().uuid();
        userId = faker.internet().uuid();
        eventId = faker.internet().uuid();
        ticketId = faker.internet().uuid();
        registrationId = faker.internet().uuid();

        mockRegistration = Registration.builder()
                .registrationId(registrationId)
                .status(RegistrationStatus.pending)
                .build();
    }

    @Test
    void registerForEvent_Success() {
        Event mockEvent = mock(Event.class);
        Ticket mockTicketObj = mock(Ticket.class);

        when(registrationRepo.findByAttendeeUserIdAndEventEventId(userId, eventId)).thenReturn(Optional.empty());
        when(entityManager.getReference(User.class, userId)).thenReturn(mock(User.class));
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(mockEvent));
        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(mockTicketObj));
        when(registrationRepo.save(any(Registration.class))).thenReturn(mockRegistration);

        GenericResponse response = registrationService.registerForEvent(userId, eventId, ticketId);

        assertEquals("Registration successful", response.message());
        verify(registrationRepo, times(1)).save(any(Registration.class));
    }

    @Test
    void registerForEvent_ThrowsAlreadyExistsException() {
        when(registrationRepo.findByAttendeeUserIdAndEventEventId(userId, eventId)).thenReturn(Optional.ofNullable(mockRegistration));

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

        GenericResponse response = registrationService.approveRegistration(actorId, registrationId);

        assertEquals("Registration approved successfully", response.message());
        assertEquals(RegistrationStatus.confirmed, mockRegistration.getStatus());
        verify(registrationRepo, times(1)).save(mockRegistration);
    }

    @Test
    void cancelRegistration_ThrowsNotFoundException() {
        when(registrationRepo.findById(registrationId)).thenReturn(Optional.empty());

        assertThrows(
                RegistrationNotFoundException.class,
                () -> registrationService.cancelRegistration(actorId, registrationId)
        );
    }
}