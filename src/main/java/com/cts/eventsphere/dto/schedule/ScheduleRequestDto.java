package com.cts.eventsphere.dto.schedule;

import com.cts.eventsphere.model.data.ScheduleStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO for Request of Schedule.
 * * @author 2479623
 *
 * @version 1.0
 * @since 28-02-2026
 */
public record ScheduleRequestDto(
        String eventId,
        LocalDate date,
        String timeSlot,
        String activity,
        ScheduleStatus status
) {
}
