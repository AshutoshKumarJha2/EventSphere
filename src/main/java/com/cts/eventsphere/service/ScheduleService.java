package com.cts.eventsphere.service;

import com.cts.eventsphere.dto.schedule.ScheduleRequestDto;
import com.cts.eventsphere.dto.schedule.ScheduleResponseDto;
import com.cts.eventsphere.exception.schedule.ScheduleNotFoundException;

/**
 * [Detailed description of the class's responsibility]
 * * @author 2479623
 *
 * @version 1.0
 * @since 27-02-2026
 */
public interface ScheduleService {
    public ScheduleResponseDto updateById(String id, ScheduleRequestDto schedule) throws ScheduleNotFoundException;

    public boolean deleteById(String id) throws ScheduleNotFoundException;
}
