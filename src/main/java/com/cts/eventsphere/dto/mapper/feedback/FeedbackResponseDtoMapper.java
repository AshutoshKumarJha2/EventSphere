package com.cts.eventsphere.dto.mapper.feedback;

import com.cts.eventsphere.dto.feedback.FeedbackResponseDto;
import com.cts.eventsphere.model.FeedBack;
import org.springframework.stereotype.Component;

/**
 * DTO Mapper for Feedback Response DTO.
 *
 * @author 2480027
 * @version 1.0
 * @since 26-02-2026
 */
@Component
public class FeedbackResponseDtoMapper {

    public static FeedbackResponseDto toDTO(FeedBack entity) {
        if (entity == null) {
            return null;
        }

        return FeedbackResponseDto.builder()
                .feedbackId(entity.getFeedbackId())
                .eventId(entity.getEventId())
                .attendeeId(entity.getAttendeeId())
                .rating(entity.getRating())
                .comments(entity.getComments())
                .date(entity.getDate())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}