package com.cts.eventsphere.EngagementModuleTest;

import com.cts.eventsphere.exception.engagement.InvalidEngagementException;
import com.cts.eventsphere.model.Engagement;
import com.cts.eventsphere.model.data.EngagementType;
import com.cts.eventsphere.repository.EngagementRepository;
import com.cts.eventsphere.service.EngagementService;
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
 * @author 2480027
 * @version 1.0
 * @since 07-03-2026
 */

/**
 * Unit tests for EngagementServiceImpl
 */
@ExtendWith(MockitoExtension.class)
class EngagementServiceImplFakerTest {

    private EngagementRepository engagementRepository;
    private EngagementService service;

    private Faker faker;

    @BeforeEach
    void setup() {
        engagementRepository = mock(EngagementRepository.class);
        service = new EngagementServiceImpl(engagementRepository);
        faker = new Faker();
    }

    @Test
    void recordEngagement_withNullActivity_throwsInvalidEngagementException() {
        Engagement engagement = mock(Engagement.class);
        when(engagement.getActivity()).thenReturn(null);
        when(engagement.getAttendeeId()).thenReturn(faker.internet().uuid());
        when(engagement.getEventId()).thenReturn(faker.internet().uuid());

        assertThrows(InvalidEngagementException.class, () -> service.recordEngagement(engagement));
        verifyNoInteractions(engagementRepository);
    }

    @Test
    void recordEngagement_withValidActivity_savesAndReturns() {
        Engagement engagement = mock(Engagement.class);
        EngagementType activity = faker.options().option(EngagementType.values());

        when(engagement.getActivity()).thenReturn(activity);
        when(engagement.getAttendeeId()).thenReturn(faker.internet().uuid());
        when(engagement.getEventId()).thenReturn(faker.internet().uuid());

        Engagement saved = mock(Engagement.class);
        when(saved.getEngagementId()).thenReturn(faker.internet().uuid());
        when(engagementRepository.save(engagement)).thenReturn(saved);

        Engagement result = service.recordEngagement(engagement);

        assertNotNull(result);
        assertSame(saved, result);
        verify(engagementRepository, times(1)).save(same(engagement));
        verifyNoMoreInteractions(engagementRepository);
    }

    @Test
    void getByEvent_returnsListFromRepository() {
        String eventId = faker.internet().uuid();
        List<Engagement> repoResult = List.of(mock(Engagement.class), mock(Engagement.class));
        when(engagementRepository.findByEventId(eventId)).thenReturn(repoResult);

        List<Engagement> result = service.getByEvent(eventId);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(engagementRepository, times(1)).findByEventId(eventId);
        verifyNoMoreInteractions(engagementRepository);
    }

    @Test
    void getByActivityType_returnsListFromRepository() {
        EngagementType activity = faker.options().option(EngagementType.values());
        List<Engagement> repoResult = List.of(mock(Engagement.class));
        when(engagementRepository.findByActivity(activity)).thenReturn(repoResult);

        List<Engagement> result = service.getByActivityType(activity);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(engagementRepository, times(1)).findByActivity(activity);
        verifyNoMoreInteractions(engagementRepository);
    }

    @Test
    void getFilteredEngagements_returnsListFromRepository() {

        String eventId = faker.internet().uuid();
        EngagementType activity = faker.options().option(EngagementType.values());

        LocalDateTime start = LocalDateTime.now().minusDays(faker.number().numberBetween(5, 10));
        LocalDateTime end = LocalDateTime.now().minusDays(faker.number().numberBetween(0, 4));

        List<Engagement> repoResult = List.of(mock(Engagement.class), mock(Engagement.class), mock(Engagement.class));
        when(engagementRepository.findByEventIdAndActivityAndTimestampBetween(eventId, activity, start, end))
                .thenReturn(repoResult);

        List<Engagement> result = service.getFilteredEngagements(eventId, activity, start, end);

        assertNotNull(result);
        assertEquals(3, result.size());
        verify(engagementRepository, times(1))
                .findByEventIdAndActivityAndTimestampBetween(eq(eventId), eq(activity), eq(start), eq(end));
        verifyNoMoreInteractions(engagementRepository);
    }
}