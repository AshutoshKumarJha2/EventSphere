package com.cts.eventsphere.EngagementModuleTest;

import com.cts.eventsphere.dto.engagement.EngagementRequestDto;
import com.cts.eventsphere.dto.engagement.EngagementResponseDto;
import com.cts.eventsphere.exception.engagement.InvalidEngagementException;
import com.cts.eventsphere.model.Engagement;
import com.cts.eventsphere.model.data.AuditAction;
import com.cts.eventsphere.model.data.EngagementType;
import com.cts.eventsphere.repository.EngagementRepository;
import com.cts.eventsphere.service.AuditService;
import com.cts.eventsphere.service.NotificationService;
import com.cts.eventsphere.service.impl.EngagementServiceImpl;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for EngagementServiceImpl including Audit and Notification verification.
 */
@ExtendWith(MockitoExtension.class)
class EngagementServiceImplFakerTest {

    private EngagementRepository engagementRepository;
    private AuditService auditService;
    private NotificationService notificationService;
    private EngagementServiceImpl service;
    private Faker faker;

    @BeforeEach
    void setup() {
        engagementRepository = mock(EngagementRepository.class);
        auditService = mock(AuditService.class);
        notificationService = mock(NotificationService.class);

        // Pass the new dependencies to the constructor
        service = new EngagementServiceImpl(engagementRepository, auditService, notificationService);
        faker = new Faker();
    }

    @Test
    void recordEngagement_withValidActivity_savesLogsAndNotifies() {
        // Arrange
        String attendeeId = faker.internet().uuid();
        EngagementType activity = faker.options().option(EngagementType.class);

        EngagementRequestDto requestDto = EngagementRequestDto.builder()
                .eventId(faker.internet().uuid())
                .attendeeId(attendeeId)
                .activity(activity)
                .activityTimestamp(LocalDateTime.now())
                .build();

        Engagement savedEntity = new Engagement();
        savedEntity.setEngagementId(faker.internet().uuid());

        when(engagementRepository.save(any(Engagement.class))).thenReturn(savedEntity);

        // Act
        EngagementResponseDto result = service.recordEngagement(requestDto);

        // Assert
        assertNotNull(result);
        assertEquals(savedEntity.getEngagementId(), result.engagementId());

        // Verify Repository interaction
        verify(engagementRepository, times(1)).save(any(Engagement.class));

        // Verify Audit interaction (Crucial for the new changes)
        verify(auditService, times(1)).logAudit(
                eq(attendeeId),
                eq(AuditAction.CREATE),
                eq(Engagement.class),
                eq(savedEntity.getEngagementId())
        );

        // Verify Notification interaction
        verify(notificationService, times(1)).sendNotification(
                eq(attendeeId),
                anyString(),
                eq("ENGAGEMENT_RECORDED")
        );
    }

    @Test
    void recordEngagement_futureTimestamp_throwsExceptionAndNoSideEffects() {
        EngagementRequestDto requestDto = EngagementRequestDto.builder()
                .eventId(faker.internet().uuid())
                .attendeeId(faker.internet().uuid())
                .activity(EngagementType.SESSION_JOIN)
                .activityTimestamp(LocalDateTime.now().plusDays(1)) // Future date
                .build();

        assertThrows(InvalidEngagementException.class, () -> service.recordEngagement(requestDto));

        // Ensure no data was saved or audited if validation fails
        verifyNoInteractions(engagementRepository, auditService, notificationService);
    }

    @Test
    void getByEvent_returnsDtoListFromRepository() {
        String eventId = faker.internet().uuid();
        Engagement e1 = new Engagement();
        e1.setEngagementId(faker.internet().uuid());

        when(engagementRepository.findByEventId(eventId)).thenReturn(List.of(e1));

        List<EngagementResponseDto> result = service.getByEvent(eventId);

        assertEquals(1, result.size());
        verify(engagementRepository).findByEventId(eventId);
        // Read operations should not trigger audits in this implementation
        verifyNoInteractions(auditService);
    }

    @Test
    void getFilteredEngagements_withValidFilters_returnsList() {
        String eventId = faker.internet().uuid();
        EngagementType activity = EngagementType.CHECK_IN;
        LocalDateTime start = LocalDateTime.now().minusDays(1);
        LocalDateTime end = LocalDateTime.now();

        Engagement e = new Engagement();
        e.setEngagementId(faker.internet().uuid());

        when(engagementRepository.findByEventIdAndActivityAndTimestampBetween(eq(eventId), eq(activity), eq(start), eq(end)))
                .thenReturn(List.of(e));

        List<EngagementResponseDto> result = service.getFilteredEngagements(eventId, activity, start, end);

        assertFalse(result.isEmpty());
        verify(engagementRepository).findByEventIdAndActivityAndTimestampBetween(eventId, activity, start, end);
    }
}