package com.cts.eventsphere.EngagementModuleTest;

import com.cts.eventsphere.controller.EngagementController;
import com.cts.eventsphere.model.Engagement;
import com.cts.eventsphere.model.data.EngagementType;
import com.cts.eventsphere.service.EngagementService;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
/**
 * @author 2480027
 * @version 1.0
 * @since 07-03-2026
 */

/**
 * Unit tests for Engagement Controller
 */

@ExtendWith(MockitoExtension.class)
class EngagementControllerFakerTest {

    @Mock
    private EngagementService engagementService;

    @InjectMocks
    private EngagementController engagementController;

    private Faker faker;

    @BeforeEach
    void setUp() {
        faker = new Faker();
    }

    @Test
    void getByEvent_returnsOkWithList() {
        String eventId = faker.internet().uuid();
        int size = faker.number().numberBetween(1, 5);

        List<Engagement> engagements = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            engagements.add(mock(Engagement.class));
        }

        when(engagementService.getByEvent(eventId)).thenReturn(engagements);

        ResponseEntity<List<Engagement>> response = engagementController.getByEvent(eventId);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(size, response.getBody().size());

        verify(engagementService, times(1)).getByEvent(eventId);
        verifyNoMoreInteractions(engagementService);
    }

    @Test
    void getByActivity_returnsOkWithList() {
        EngagementType activity = faker.options().option(EngagementType.values());
        int size = faker.number().numberBetween(1, 5);

        List<Engagement> engagements = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            engagements.add(mock(Engagement.class));
        }

        when(engagementService.getByActivityType(activity)).thenReturn(engagements);

        ResponseEntity<List<Engagement>> response = engagementController.getByActivity(activity);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(size, response.getBody().size());

        verify(engagementService, times(1)).getByActivityType(activity);
        verifyNoMoreInteractions(engagementService);
    }

    @Test
    void getDetailedFilter_returnsOkWithList() {
        String eventId = faker.internet().uuid();
        EngagementType activity = faker.options().option(EngagementType.values());

        int startMinusDays = faker.number().numberBetween(5, 10);
        int endMinusDays = faker.number().numberBetween(0, 4);
        LocalDateTime start = LocalDateTime.now().minusDays(startMinusDays);
        LocalDateTime end = LocalDateTime.now().minusDays(endMinusDays);

        int size = faker.number().numberBetween(1, 5);
        List<Engagement> engagements = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            engagements.add(mock(Engagement.class));
        }

        when(engagementService.getFilteredEngagements(eventId, activity, start, end)).thenReturn(engagements);

        ResponseEntity<List<Engagement>> response =
                engagementController.getDetailedFilter(eventId, activity, start, end);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(size, response.getBody().size());

        verify(engagementService, times(1))
                .getFilteredEngagements(eventId, activity, start, end);
        verifyNoMoreInteractions(engagementService);
    }
}