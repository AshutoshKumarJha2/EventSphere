package com.cts.eventsphere.dto.mapper.engagement;

import com.cts.eventsphere.dto.engagement.EngagementRequestDto;
import com.cts.eventsphere.model.Engagement;
import org.springframework.stereotype.Component;

/**
 * DTO Mapper for Engagement Request DTO.
 * Converts incoming request data into the Engagement entity.
 *
 * @author 2480027
 * @version 1.0
 * @since 02-03-2026
 */
@Component
public class EngagementRequestDtoMapper {

    public static Engagement toEntity(EngagementRequestDto dto) {
        if (dto == null) {
            return null;
        }

        return Engagement.builder()
                .eventId(dto.eventId())
                .attendeeId(dto.attendeeId())
                .activity(dto.activity())
                .timestamp(dto.activityTimestamp())
                .build();
    }
}