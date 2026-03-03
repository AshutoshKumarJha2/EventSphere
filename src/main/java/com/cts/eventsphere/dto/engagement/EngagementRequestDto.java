package com.cts.eventsphere.dto.engagement;

import com.cts.eventsphere.model.data.EngagementType;

import java.time.LocalDateTime;

/**
 *  EngagementRequest dto representing engagement details
 *
 * @author 2480027
 * @version 1.0
 * @since 02-03-2026
 */
public record EngagementRequestDto(

        String eventId,
        String attendeeId,
        EngagementType activity,
        LocalDateTime activityTimestamp

) {

}
