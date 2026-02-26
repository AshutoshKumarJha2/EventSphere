package com.cts.eventsphere.dto.mapper;

import com.cts.eventsphere.dto.ScheduleResponseDto;
import com.cts.eventsphere.model.Schedule;
import org.springframework.stereotype.Component;

/**
 * DTO Mapper for Schedule Response DTO.
 * * @author 2479623
 *
 * @version 1.0
 * @since 26-02-2026
 */
@Component
public class ScheduleResponseDtoMapper {
    public ScheduleResponseDto toDTO(Schedule schedule) {
        return new ScheduleResponseDto(
                schedule.getScheduleId(),
                schedule.getEventId(),
                schedule.getDate().toString(),
                schedule.getTimeSlot(),
                schedule.getActivity(),
                schedule.getStatus()
        );
    }
}
