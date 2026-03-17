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

    public EngagementResponseDto toDTO(Engagement entity) {
        if (entity == null) {
            return null;
        }

        return EngagementResponseDto.builder()
                .engagementId(entity.getEngagementId())
                .eventId(entity.getEventId())
                .attendeeId(entity.getAttendeeId())
                .activity(entity.getActivity())
                .activityTimestamp(entity.getTimestamp())
                .build();
    }
}