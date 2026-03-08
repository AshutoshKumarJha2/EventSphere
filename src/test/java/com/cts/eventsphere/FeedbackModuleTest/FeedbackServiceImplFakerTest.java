package com.cts.eventsphere.FeedbackModuleTest;
import com.cts.eventsphere.dto.feedback.FeedbackRequestDto;
import com.cts.eventsphere.dto.feedback.FeedbackResponseDto;
import com.cts.eventsphere.dto.mapper.feedback.FeedbackRequestDtoMapper;
import com.cts.eventsphere.dto.mapper.feedback.FeedbackResponseDtoMapper;
import com.cts.eventsphere.dto.registration.RegistrationDTO;
import com.cts.eventsphere.exception.Feedback.FeedbackNotFoundException;
import com.cts.eventsphere.model.FeedBack;
import com.cts.eventsphere.repository.FeedbackRepository;
import com.cts.eventsphere.service.RegistrationService;
import com.cts.eventsphere.service.impl.FeedbackServiceImpl;
import com.github.javafaker.Faker;
import jakarta.persistence.EntityExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
/**
 * @author 2480027
 * @version 1.0
 * @since 07-03-2026
 */

/**
 * Unit tests for FeedbackServiceImpl using JavaFaker and Mockito.
 */
@ExtendWith(MockitoExtension.class)
class FeedbackServiceImplFakerTest {

    private FeedbackRepository feedbackRepository;
    private RegistrationService registrationService;

    private FeedbackServiceImpl service;

    private Faker faker;

    @BeforeEach
    void setUp() {
        feedbackRepository = mock(FeedbackRepository.class);
        registrationService = mock(RegistrationService.class);
        service = new FeedbackServiceImpl(feedbackRepository, registrationService);
        faker = new Faker();
    }

    @Test
    void create_success_returnsMappedDto() {
        String eventId = faker.internet().uuid();
        String attendeeId = faker.internet().uuid();
        int rating = faker.number().numberBetween(1, 5);

        FeedbackRequestDto request = mock(FeedbackRequestDto.class);
        when(request.eventId()).thenReturn(eventId);
        when(request.attendeeId()).thenReturn(attendeeId);
        when(request.rating()).thenReturn(rating);

        RegistrationDTO reg = mock(RegistrationDTO.class);
        when(reg.status()).thenReturn("Confirmed");
        when(registrationService.getRegistrationByEventIdAndUserId(eventId, attendeeId)).thenReturn(reg);

        when(feedbackRepository.findByEventIdAndAttendeeId(eq(eventId), eq(attendeeId), any(Pageable.class)))
                .thenReturn(Page.empty());

        FeedBack entity = mock(FeedBack.class);
        when(entity.getFeedbackId()).thenReturn(faker.internet().uuid());
        FeedBack saved = entity;
        when(feedbackRepository.save(any(FeedBack.class))).thenReturn(saved);

        FeedbackResponseDto expectedDto = mock(FeedbackResponseDto.class);

        try (MockedStatic<FeedbackRequestDtoMapper> reqMap = Mockito.mockStatic(FeedbackRequestDtoMapper.class);
             MockedStatic<FeedbackResponseDtoMapper> respMap = Mockito.mockStatic(FeedbackResponseDtoMapper.class)) {

            reqMap.when(() -> FeedbackRequestDtoMapper.toEntity(request)).thenReturn(entity);
            respMap.when(() -> FeedbackResponseDtoMapper.toDTO(saved)).thenReturn(expectedDto);

            FeedbackResponseDto result = service.create(request);

            assertSame(expectedDto, result);
            verify(registrationService).getRegistrationByEventIdAndUserId(eventId, attendeeId);
            verify(feedbackRepository).findByEventIdAndAttendeeId(eq(eventId), eq(attendeeId), any(Pageable.class));
            verify(feedbackRepository).save(entity);
        }
    }

    @Test
    void create_invalidRating_throwsIllegalArgumentException() {
        FeedbackRequestDto request = mock(FeedbackRequestDto.class);
        when(request.eventId()).thenReturn(faker.internet().uuid());
        when(request.attendeeId()).thenReturn(faker.internet().uuid());
        when(request.rating()).thenReturn(0);

        assertThrows(IllegalArgumentException.class, () -> service.create(request));

        verifyNoInteractions(feedbackRepository, registrationService);
    }

    @Test
    void create_ineligibleAttendee_throwsIllegalStateException() {
        String eventId = faker.internet().uuid();
        String attendeeId = faker.internet().uuid();

        FeedbackRequestDto request = mock(FeedbackRequestDto.class);
        when(request.eventId()).thenReturn(eventId);
        when(request.attendeeId()).thenReturn(attendeeId);
        when(request.rating()).thenReturn(3);

        RegistrationDTO reg = mock(RegistrationDTO.class);
        when(reg.status()).thenReturn("Cancelled");
        when(registrationService.getRegistrationByEventIdAndUserId(eventId, attendeeId)).thenReturn(reg);

        assertThrows(IllegalStateException.class, () -> service.create(request));

        verify(registrationService).getRegistrationByEventIdAndUserId(eventId, attendeeId);
        verifyNoMoreInteractions(registrationService);
        verifyNoInteractions(feedbackRepository);
    }

    @Test
    void create_duplicateFeedback_throwsEntityExistsException() {
        String eventId = faker.internet().uuid();
        String attendeeId = faker.internet().uuid();

        FeedbackRequestDto request = mock(FeedbackRequestDto.class);
        when(request.eventId()).thenReturn(eventId);
        when(request.attendeeId()).thenReturn(attendeeId);
        when(request.rating()).thenReturn(4);

        RegistrationDTO reg = mock(RegistrationDTO.class);
        when(reg.status()).thenReturn("CheckedIn");
        when(registrationService.getRegistrationByEventIdAndUserId(eventId, attendeeId)).thenReturn(reg);

        Page<FeedBack> nonEmpty = new PageImpl<>(List.of(mock(FeedBack.class)));
        when(feedbackRepository.findByEventIdAndAttendeeId(eq(eventId), eq(attendeeId), any(Pageable.class)))
                .thenReturn(nonEmpty);

        assertThrows(EntityExistsException.class, () -> service.create(request));

        verify(registrationService).getRegistrationByEventIdAndUserId(eventId, attendeeId);
        verify(feedbackRepository).findByEventIdAndAttendeeId(eq(eventId), eq(attendeeId), any(Pageable.class));
        verifyNoMoreInteractions(feedbackRepository);
    }

    @Test
    void getById_found_returnsMappedOptional() throws Exception {
        String id = faker.internet().uuid();
        FeedBack entity = mock(FeedBack.class);
        FeedbackResponseDto mapped = mock(FeedbackResponseDto.class);

        when(feedbackRepository.existsById(id)).thenReturn(true);
        when(feedbackRepository.findById(id)).thenReturn(Optional.of(entity));

        try (MockedStatic<FeedbackResponseDtoMapper> respMap = Mockito.mockStatic(FeedbackResponseDtoMapper.class)) {
            respMap.when(() -> FeedbackResponseDtoMapper.toDTO(entity)).thenReturn(mapped);

            Optional<FeedbackResponseDto> result = service.getById(id);

            assertTrue(result.isPresent());
            assertSame(mapped, result.get());
            verify(feedbackRepository).existsById(id);
            verify(feedbackRepository).findById(id);
        }
    }

    @Test
    void getById_notFound_throwsFeedbackNotFoundException() {
        String id = faker.internet().uuid();
        when(feedbackRepository.existsById(id)).thenReturn(false);

        assertThrows(FeedbackNotFoundException.class, () -> service.getById(id));

        verify(feedbackRepository).existsById(id);
        verifyNoMoreInteractions(feedbackRepository);
    }

    @Test
    void listByEvent_mapsEntitiesToDtos() {
        String eventId = faker.internet().uuid();
        Pageable pageable = PageRequest.of(0, 2);

        FeedBack e1 = mock(FeedBack.class);
        FeedBack e2 = mock(FeedBack.class);
        Page<FeedBack> entityPage = new PageImpl<>(List.of(e1, e2), pageable, 2);

        FeedbackResponseDto d1 = mock(FeedbackResponseDto.class);
        FeedbackResponseDto d2 = mock(FeedbackResponseDto.class);

        when(feedbackRepository.findByEventId(eventId, pageable)).thenReturn(entityPage);

        try (MockedStatic<FeedbackResponseDtoMapper> respMap = Mockito.mockStatic(FeedbackResponseDtoMapper.class)) {
            respMap.when(() -> FeedbackResponseDtoMapper.toDTO(e1)).thenReturn(d1);
            respMap.when(() -> FeedbackResponseDtoMapper.toDTO(e2)).thenReturn(d2);

            Page<FeedbackResponseDto> result = service.listByEvent(eventId, pageable);

            assertEquals(2, result.getTotalElements());
            verify(feedbackRepository).findByEventId(eventId, pageable);
        }
    }

    @Test
    void listByEventAndDateRange_mapsEntitiesToDtos() {
        String eventId = faker.internet().uuid();
        LocalDateTime start = LocalDateTime.now().minusDays(7);
        LocalDateTime end = LocalDateTime.now();
        Pageable pageable = PageRequest.of(0, 3);

        FeedBack e1 = mock(FeedBack.class);
        Page<FeedBack> entityPage = new PageImpl<>(List.of(e1), pageable, 1);

        FeedbackResponseDto d1 = mock(FeedbackResponseDto.class);

        when(feedbackRepository.findByEventIdAndDateBetween(eventId, start, end, pageable))
                .thenReturn(entityPage);

        try (MockedStatic<FeedbackResponseDtoMapper> respMap = Mockito.mockStatic(FeedbackResponseDtoMapper.class)) {
            respMap.when(() -> FeedbackResponseDtoMapper.toDTO(e1)).thenReturn(d1);

            Page<FeedbackResponseDto> result = service.listByEventAndDateRange(eventId, start, end, pageable);

            assertEquals(1, result.getTotalElements());
            verify(feedbackRepository).findByEventIdAndDateBetween(eventId, start, end, pageable);
        }
    }

    @Test
    void delete_whenExists_deletesSuccessfully() {
        String id = faker.internet().uuid();
        when(feedbackRepository.existsById(id)).thenReturn(true);
        doNothing().when(feedbackRepository).deleteById(id);

        service.delete(id);

        verify(feedbackRepository).existsById(id);
        verify(feedbackRepository).deleteById(id);
    }

    @Test
    void delete_whenNotExists_throwsFeedbackNotFoundException() {
        String id = faker.internet().uuid();
        when(feedbackRepository.existsById(id)).thenReturn(false);

        assertThrows(FeedbackNotFoundException.class, () -> service.delete(id));

        verify(feedbackRepository).existsById(id);
        verifyNoMoreInteractions(feedbackRepository);
    }
}