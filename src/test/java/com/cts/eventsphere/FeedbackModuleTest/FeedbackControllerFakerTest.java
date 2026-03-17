package com.cts.eventsphere.FeedbackModuleTest;

import com.cts.eventsphere.controller.FeedbackController;
import com.cts.eventsphere.dto.feedback.FeedbackRequestDto;
import com.cts.eventsphere.dto.feedback.FeedbackResponseDto;
import com.cts.eventsphere.service.FeedbackService;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.ZoneId;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author 2480027
 * @version 1.0
 * @since 07-03-2026
 */

/**
 * Unit tests for FeedbackController
 */
@ExtendWith(MockitoExtension.class)
class FeedbackControllerFakerTest {

    @Mock
    private FeedbackService feedbackService;

    @InjectMocks
    private FeedbackController feedbackController;

    private Faker faker;

    @BeforeEach
    void setUp() {
        faker = new Faker();
    }

    @Test
    void create_shouldReturnResponseFromService() {
        FeedbackRequestDto request = new FeedbackRequestDto(
                faker.internet().uuid(),
                faker.internet().uuid(),
                faker.number().numberBetween(1, 5),
                faker.lorem().sentence(8).toString(),
                faker.date()
                        .past(10, TimeUnit.DAYS)
                        .toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime()
        );

        FeedbackResponseDto responseDto = new FeedbackResponseDto(
                faker.internet().uuid(),
                request.eventId(),
                request.attendeeId(),
                request.rating(),
                request.comments(),
                faker.date().past(10, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
                faker.date().past(10, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
                faker.date().past(10, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
        );

        when(feedbackService.create(request)).thenReturn(responseDto);

        ResponseEntity<FeedbackResponseDto> result = feedbackController.create(request);

        assertNotNull(result);
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(responseDto.eventId(), result.getBody().eventId());
        verify(feedbackService, times(1)).create(request);
    }

    @Test
    void getById_whenFound_returnsResponseEntityWithValue() {
        String id = faker.internet().uuid();
        FeedbackResponseDto dto = mock(FeedbackResponseDto.class);

        // Service now returns DTO directly
        when(feedbackService.getById(id)).thenReturn(dto);

        ResponseEntity<FeedbackResponseDto> result = feedbackController.getById(id);

        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertSame(dto, result.getBody());
        verify(feedbackService, times(1)).getById(id);
    }

    @Test
    void listByEvent_shouldReturnPageFromService() {
        String eventId = faker.internet().uuid();
        PageRequest pageable = PageRequest.of(0, 2);

        List<FeedbackResponseDto> content = List.of(
                mock(FeedbackResponseDto.class),
                mock(FeedbackResponseDto.class)
        );
        Page<FeedbackResponseDto> page = new PageImpl<>(content, pageable, content.size());

        when(feedbackService.listByEvent(eventId, pageable)).thenReturn(page);

        ResponseEntity<Page<FeedbackResponseDto>> result = feedbackController.listByEvent(eventId, pageable);

        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(2, result.getBody().getTotalElements());
        verify(feedbackService, times(1)).listByEvent(eventId, pageable);
    }

    @Test
    void deleteFeedback_shouldInvokeServiceDelete() {
        String id = faker.internet().uuid();
        doNothing().when(feedbackService).delete(id);

        ResponseEntity<Void> result = feedbackController.deleteFeedback(id);

        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        verify(feedbackService, times(1)).delete(id);
    }
}