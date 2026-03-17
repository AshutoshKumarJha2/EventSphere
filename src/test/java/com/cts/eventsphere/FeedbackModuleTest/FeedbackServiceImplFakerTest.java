package com.cts.eventsphere.FeedbackModuleTest;

import com.cts.eventsphere.dto.feedback.FeedbackRequestDto;
import com.cts.eventsphere.dto.feedback.FeedbackResponseDto;
import com.cts.eventsphere.dto.mapper.feedback.FeedbackRequestDtoMapper;
import com.cts.eventsphere.dto.mapper.feedback.FeedbackResponseDtoMapper;
import com.cts.eventsphere.exception.Feedback.FeedbackNotFoundException;
import com.cts.eventsphere.model.FeedBack;
import com.cts.eventsphere.model.Registration;
import com.cts.eventsphere.model.data.AuditAction;
import com.cts.eventsphere.model.data.RegistrationStatus;
import com.cts.eventsphere.repository.FeedbackRepository;
import com.cts.eventsphere.repository.RegistrationRepository;
import com.cts.eventsphere.service.AuditService;
import com.cts.eventsphere.service.NotificationService;
import com.cts.eventsphere.service.impl.FeedbackServiceImpl;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FeedbackServiceImplFakerTest {

    private FeedbackRepository feedbackRepository;
    private RegistrationRepository registrationRepository;
    private AuditService auditService;
    private NotificationService notificationService;
    private FeedbackServiceImpl service;
    private Faker faker;

    @BeforeEach
    void setUp() {
        feedbackRepository = mock(FeedbackRepository.class);
        registrationRepository = mock(RegistrationRepository.class);
        auditService = mock(AuditService.class);
        notificationService = mock(NotificationService.class);

        // Updated constructor with new dependencies
        service = new FeedbackServiceImpl(
                feedbackRepository,
                registrationRepository,
                notificationService,
                auditService
        );
        faker = new Faker();
    }

    @Test
    void create_success_savesAuditsAndNotifies() {
        String eventId = faker.internet().uuid();
        String attendeeId = faker.internet().uuid();

        FeedbackRequestDto request = FeedbackRequestDto.builder()
                .eventId(eventId)
                .attendeeId(attendeeId)
                .rating(5)
                .build();

        Registration reg = mock(Registration.class);
        when(reg.getStatus()).thenReturn(RegistrationStatus.confirmed);
        when(registrationRepository.findByAttendeeUserIdAndEventEventId(attendeeId, eventId))
                .thenReturn(Optional.of(reg));

        when(feedbackRepository.findByEventIdAndAttendeeId(eq(eventId), eq(attendeeId), any(Pageable.class)))
                .thenReturn(Page.empty());

        FeedBack entity = new FeedBack();
        entity.setFeedbackId(faker.internet().uuid());
        entity.setEventId(eventId);
        entity.setAttendeeId(attendeeId);

        when(feedbackRepository.save(any(FeedBack.class))).thenReturn(entity);

        FeedbackResponseDto expectedDto = FeedbackResponseDto.builder()
                .feedbackId(entity.getFeedbackId())
                .build();

        try (MockedStatic<FeedbackRequestDtoMapper> reqMap = Mockito.mockStatic(FeedbackRequestDtoMapper.class);
             MockedStatic<FeedbackResponseDtoMapper> respMap = Mockito.mockStatic(FeedbackResponseDtoMapper.class)) {

            reqMap.when(() -> FeedbackRequestDtoMapper.toEntity(any())).thenReturn(entity);
            respMap.when(() -> FeedbackResponseDtoMapper.toDTO(any())).thenReturn(expectedDto);

            FeedbackResponseDto result = service.create(request);

            // Basic Assertions
            assertNotNull(result);
            assertEquals(expectedDto.feedbackId(), result.feedbackId());

            // Verify Audit Logging
            verify(auditService).logAudit(eq(attendeeId), eq(AuditAction.CREATE), eq(FeedBack.class), eq(entity.getFeedbackId()));

            // Verify Notification
            verify(notificationService).sendNotification(eq(attendeeId), anyString(), eq("FEEDBACK_CONFIRMATION"));
        }
    }

    @Test
    void delete_whenExists_deletesAndAudits() {
        String id = faker.internet().uuid();
        String attendeeId = faker.internet().uuid();

        FeedBack existingFeedback = new FeedBack();
        existingFeedback.setFeedbackId(id);
        existingFeedback.setAttendeeId(attendeeId);

        // Service now calls findById before deleting
        when(feedbackRepository.findById(id)).thenReturn(Optional.of(existingFeedback));
        doNothing().when(feedbackRepository).deleteById(id);

        service.delete(id);

        verify(feedbackRepository).deleteById(id);
        // Verify delete audit
        verify(auditService).logAudit(eq(attendeeId), eq(AuditAction.DELETE), eq(FeedBack.class), eq(id));
    }

    @Test
    void delete_whenNotExists_throwsExceptionAndNoAudit() {
        String id = faker.internet().uuid();
        when(feedbackRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(FeedbackNotFoundException.class, () -> service.delete(id));

        verify(feedbackRepository, never()).deleteById(anyString());
        verifyNoInteractions(auditService);
    }

    @Test
    void create_invalidRating_throwsExceptionAndNoSideEffects() {
        FeedbackRequestDto request = FeedbackRequestDto.builder()
                .rating(0)
                .build();

        assertThrows(IllegalArgumentException.class, () -> service.create(request));

        // Ensure no interactions occur if validation fails early
        verifyNoInteractions(feedbackRepository, auditService, notificationService);
    }

    @Test
    void getById_found_returnsMappedDto() {
        String id = faker.internet().uuid();
        FeedBack entity = new FeedBack();
        entity.setFeedbackId(id);
        FeedbackResponseDto mapped = FeedbackResponseDto.builder().feedbackId(id).build();

        when(feedbackRepository.findById(id)).thenReturn(Optional.of(entity));

        try (MockedStatic<FeedbackResponseDtoMapper> respMap = Mockito.mockStatic(FeedbackResponseDtoMapper.class)) {
            respMap.when(() -> FeedbackResponseDtoMapper.toDTO(entity)).thenReturn(mapped);

            FeedbackResponseDto result = service.getById(id);

            assertNotNull(result);
            assertEquals(id, result.feedbackId());
        }
    }
}