package com.cts.eventsphere.dto.mapper;

import com.cts.eventsphere.dto.FeedbackDto;
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
public class FeedbackDtoMapper {

    public static FeedbackDto toDTO(FeedBack entity) {
        if (entity == null) {
            return null;
        }
        return new FeedbackDto(
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
