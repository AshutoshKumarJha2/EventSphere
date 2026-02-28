package com.cts.eventsphere.dto.mapper.schedule;

import com.cts.eventsphere.dto.schedule.ScheduleRequestDto;
import com.cts.eventsphere.model.Schedule;
import org.springframework.stereotype.Component;

/**
 * [Detailed description of the class's responsibility]
 * * @author 2479623
 *
 * @version 1.0
 * @since 28-02-2026
 */
@Component
public class ScheduleRequestDtoMapper {
    public Schedule toEntity(ScheduleRequestDto dto) {
        if(dto == null) {
            return null;
        }

        Schedule schedule = new Schedule();
        schedule.setEventId(dto.eventId());
        schedule.setDate(dto.date());
        schedule.setTimeSlot(dto.timeSlot());
        schedule.setActivity(dto.activity());
        schedule.setStatus(dto.status());
        return schedule;
    }
}
