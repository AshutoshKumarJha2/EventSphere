package com.cts.eventsphere.dto;

import com.cts.eventsphere.model.data.ScheduleStatus;



/**
 * DTO for Schedule Entity.
 * * @author 2479623
 *
 * @version 1.0
 * @since 26-02-2026
 */
public record ScheduleResponseDto(
        String scheduleId,
        String eventId,
        String date,
        String timeSlot,
        String activity,
        ScheduleStatus status
) {
}
