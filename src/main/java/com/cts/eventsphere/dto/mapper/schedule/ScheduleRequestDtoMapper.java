package com.cts.eventsphere.dto.mapper.schedule;

import com.cts.eventsphere.dto.schedule.ScheduleRequestDto;
import com.cts.eventsphere.model.Event;
import com.cts.eventsphere.model.Schedule;
import org.springframework.stereotype.Component;

/**
 * Dto Mapper for Schedule Request DTO.
 * * @author 2479623
 *
 * @version 1.0
 * @since 28-02-2026
 */
@Component
public class ScheduleRequestDtoMapper {
    public Schedule toEntity(ScheduleRequestDto dto, Event event) {
        if(dto == null) {
            return null;
        }

        Schedule schedule = new Schedule();
        schedule.setEvent(event);
        schedule.setDate(dto.date());
        schedule.setTimeSlot(dto.timeSlot());
        schedule.setActivity(dto.activity());
        schedule.setStatus(dto.status());
        return schedule;
    }
}
