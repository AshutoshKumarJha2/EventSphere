package com.cts.eventsphere.dto.schedule;

import com.cts.eventsphere.model.data.ScheduleStatus;

import java.time.LocalDateTime;

/**
 * [Detailed description of the class's responsibility]
 * * @author 2479623
 *
 * @version 1.0
 * @since 28-02-2026
 */
public record ScheduleRequestDto(
        String eventId,
        LocalDateTime date,
        String timeSlot,
        String activity,
        ScheduleStatus status
) {
}
