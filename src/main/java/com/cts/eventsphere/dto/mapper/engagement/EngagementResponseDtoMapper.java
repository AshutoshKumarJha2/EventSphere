package com.cts.eventsphere.dto.mapper.engagement;

import com.cts.eventsphere.dto.engagement.EngagementResponseDto;
import com.cts.eventsphere.model.Engagement;
import org.springframework.stereotype.Component;

/**
 * DTO Mapper for Engagement Response DTO.
 *
 * @author 2480027
 * @version 1.0
 * @since 02-03-2026
 */
@Component
public class EngagementResponseDtoMapper {

    public static EngagementResponseDto toDTO(Engagement entity) {
        if (entity == null) {
            return null;
        }
        return new EngagementResponseDto(
                entity.getEngagementId(),
                entity.getEventId(),
                entity.getAttendeeId(),
                entity.getActivity(),
                entity.getActivityTimestamp(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}