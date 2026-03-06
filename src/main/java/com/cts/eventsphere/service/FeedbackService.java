package com.cts.eventsphere.service;

/**
 *Service class for feedback entity
 *
 * @author 2480027
 * @version 1.0
 * @since 02-03-2026
 */

import com.cts.eventsphere.dto.feedback.FeedbackRequestDto;
import com.cts.eventsphere.dto.feedback.FeedbackResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Optional;

public interface FeedbackService {

    FeedbackResponseDto create(FeedbackRequestDto request);

    Optional<FeedbackResponseDto> getById(String feedbackId);

    Page<FeedbackResponseDto> listByEvent(String eventId, Pageable pageable);

    Page<FeedbackResponseDto> listByEventAndDateRange(String eventId, LocalDateTime start, LocalDateTime end, Pageable pageable);

    void delete(String feedbackId);
}