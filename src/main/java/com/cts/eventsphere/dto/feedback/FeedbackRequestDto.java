package com.cts.eventsphere.dto.feedback;

import java.time.LocalDateTime;

/**
 * FeedbackRequestDto representing Feedback details
 *
 * @author 2480027
 * @version 1.0
 * @since 02-03-2026
 */
public record FeedbackRequestDto(

        String eventId,
        String attendeeId,
        int rating,
        String comments,
        LocalDateTime date

) {
}
