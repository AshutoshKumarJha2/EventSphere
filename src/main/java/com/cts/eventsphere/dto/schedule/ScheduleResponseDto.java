package com.cts.eventsphere.dto.schedule;

import com.cts.eventsphere.model.data.ScheduleStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


/**
 * DTO for Schedule Entity.
 * * @author 2479623
 *
 * @version 1.0
 * @since 26-02-2026
 */
public record ScheduleResponseDto(
        @NotBlank(message = "Schedule ID must not be blank")
        String scheduleId,

        @NotBlank(message = "Event ID must not be blank")
        String eventId,

        @NotBlank(message = "Date must not be blank")
        String date,

        @NotBlank(message = "Time slot must not be blank")
        String timeSlot,

        @NotBlank(message = "Activity must not be blank")
        String activity,

        @NotNull(message = "Schedule status must not be null")
        ScheduleStatus status
) {
}