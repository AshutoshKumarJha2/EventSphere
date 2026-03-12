package com.cts.eventsphere.dto.mapper.feedback;

import com.cts.eventsphere.dto.feedback.FeedbackRequestDto;
import com.cts.eventsphere.model.FeedBack;
import org.springframework.stereotype.Component;

/**
 * DTO Mapper for Feedback Request DTO.
 *
 * @author 2480027
 * @version 1.0
 * @since 02-03-2026
 */
@Component
public class FeedbackRequestDtoMapper {

    public static FeedBack toEntity(FeedbackRequestDto dto) {
        if (dto == null) {
            return null;
        }

        return FeedBack.builder()
                .eventId(dto.eventId())
                .attendeeId(dto.attendeeId())
                .rating(dto.rating())
                .comments(dto.comments())
                .date(dto.date())
                .build();
    }
}