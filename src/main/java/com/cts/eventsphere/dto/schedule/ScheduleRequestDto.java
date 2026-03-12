package com.cts.eventsphere.dto.schedule;

import com.cts.eventsphere.model.data.ScheduleStatus;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

/**
 * DTO for Request of Schedule.
 * * @author 2479623
 *
 * @version 1.0
 * @since 28-02-2026
 */
public record ScheduleRequestDto(
        @NotBlank(message = "Event ID must not be blank")
        String eventId,

        @NotNull(message = "Date must not be null")
        @FutureOrPresent(message = "Date must be today or in the future")
        LocalDate date,

        @NotBlank(message = "Time slot must not be blank")
        @Pattern(regexp = "^(\\d{2}:\\d{2}-\\d{2}:\\d{2})$",
                message = "Time slot must follow HH:mm-HH:mm format")
        String timeSlot,

        @NotBlank(message = "Activity must not be blank")
        @Size(max = 200, message = "Activity description must not exceed 200 characters")
        String activity,

        @NotNull(message = "Schedule status must not be null")
        ScheduleStatus status
) {
}