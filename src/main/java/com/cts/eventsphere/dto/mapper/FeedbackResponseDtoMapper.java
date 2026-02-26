package com.cts.eventsphere.dto.mapper;

import com.cts.eventsphere.dto.FeedbackResponseDto;
import com.cts.eventsphere.model.FeedBack;
import org.springframework.stereotype.Component;

/**
 * [ Detailed description of the class's responsibility]
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
        return new FeedbackResponseDto(
                entity.getFeedbackId(),
                entity.getEventId(),
                entity.getAttendeeId(),
                entity.getRating(),
                entity.getComments(),
                entity.getDate(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

}
