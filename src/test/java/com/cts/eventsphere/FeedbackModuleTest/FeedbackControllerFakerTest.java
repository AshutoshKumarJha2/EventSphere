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
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
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
        // Arrange
        // Adapt field names to your actual FeedbackRequestDto (record/POJO).
        FeedbackRequestDto request = new FeedbackRequestDto(
                faker.internet().uuid(),                 // eventId
                faker.internet().uuid(),                 // attendeeID
                               // comment
                faker.number().numberBetween(1, 5),
                faker.lorem().sentence(8).toString(),

                faker.date()
                        .past(10, TimeUnit.DAYS)
                        .toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime()

        );

        FeedbackResponseDto response = new FeedbackResponseDto(
                faker.internet().uuid(),                 // feedbackId
                request.eventId(),
                request.attendeeId(),
                request.rating(),
                request.comments(),
                faker.date()
                        .past(10, TimeUnit.DAYS)
                        .toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime(),
                faker.date()
                .past(10, TimeUnit.DAYS)
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime(),
                faker.date()
                .past(10, TimeUnit.DAYS)
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime()
                );

        when(feedbackService.create(request)).thenReturn(response);

        // Act
        FeedbackResponseDto result = feedbackController.create(request);

        // Assert
        assertNotNull(result);
        assertEquals(response.eventId(), result.eventId());
        verify(feedbackService, times(1)).create(request);
        verifyNoMoreInteractions(feedbackService);
    }

    @Test
    void getById_whenFound_returnsOptionalWithValue() {
        // Arrange
        String id = faker.internet().uuid();
        FeedbackResponseDto dto = mock(FeedbackResponseDto.class);
        when(feedbackService.getById(id)).thenReturn(Optional.of(dto));

        // Act
        Optional<FeedbackResponseDto> result = feedbackController.getById(id);

        // Assert
        assertTrue(result.isPresent());
        assertSame(dto, result.get());
        verify(feedbackService, times(1)).getById(id);
        verifyNoMoreInteractions(feedbackService);
    }

    @Test
    void getById_whenNotFound_returnsEmptyOptional() {
        // Arrange
        String id = faker.internet().uuid();
        when(feedbackService.getById(id)).thenReturn(Optional.empty());

        // Act
        Optional<FeedbackResponseDto> result = feedbackController.getById(id);

        // Assert
        assertTrue(result.isEmpty());
        verify(feedbackService, times(1)).getById(id);
        verifyNoMoreInteractions(feedbackService);
    }

    @Test
    void listByEvent_shouldReturnPageFromService() {
        // Arrange
        String eventId = faker.internet().uuid();
        PageRequest pageable = PageRequest.of(0, 2);

        List<FeedbackResponseDto> content = List.of(
                mock(FeedbackResponseDto.class),
                mock(FeedbackResponseDto.class)
        );
        Page<FeedbackResponseDto> page = new PageImpl<>(content, pageable, content.size());

        when(feedbackService.listByEvent(eventId, pageable)).thenReturn(page);

        // Act
        Page<FeedbackResponseDto> result = feedbackController.listByEvent(eventId, pageable);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals(2, result.getContent().size());
        verify(feedbackService, times(1)).listByEvent(eventId, pageable);
        verifyNoMoreInteractions(feedbackService);
    }

    @Test
    void deleteFeedback_shouldInvokeServiceDelete() {
        // Arrange
        String id = faker.internet().uuid();
        doNothing().when(feedbackService).delete(id);

        // Act
        feedbackController.deleteFeedback(id);

        // Assert
        verify(feedbackService, times(1)).delete(id);
        verifyNoMoreInteractions(feedbackService);
    }
}