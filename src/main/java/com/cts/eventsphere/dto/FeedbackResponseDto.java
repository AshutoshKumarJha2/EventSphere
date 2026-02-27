package com.cts.eventsphere.dto;
import java.time.LocalDateTime;

/**
 * DTO for Feedback entity
 *
 * @author 2480027
 * @version 1.0
 * @since 26-02-2026
 */

public record FeedbackResponseDto(
        String feedbackId,
        String eventId,
        String attendeeId,
        int rating,
        String comments,
        LocalDateTime date,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}

