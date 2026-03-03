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

        FeedBack entity = new FeedBack();
        entity.setEventId(dto.eventId());
        entity.setAttendeeId(dto.attendeeId());
        entity.setRating(dto.rating());
        entity.setComments(dto.comments());
        entity.setDate(dto.date());

        return entity;
    }
}