package com.cts.eventsphere.dto.engagement;

/**
 * EngagementResponseDto for representing engagement details
 *
 * @author 2480027
 * @version 1.0
 * @since 02-03-2026
 */

import com.cts.eventsphere.model.data.EngagementType;

import java.time.LocalDateTime;

public record EngagementResponseDto(
        String engagementId,
        String eventId,
        String attendeeId,
        EngagementType activity,
        LocalDateTime activityTimestamp,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
