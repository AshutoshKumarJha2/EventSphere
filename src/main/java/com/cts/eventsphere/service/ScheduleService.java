package com.cts.eventsphere.service;

import com.cts.eventsphere.exception.schedule.ScheduleNotFoundException;
import com.cts.eventsphere.model.Schedule;

/**
 * [Detailed description of the class's responsibility]
 * * @author 2479623
 *
 * @version 1.0
 * @since 27-02-2026
 */
public interface ScheduleService {
    public boolean updateById(Schedule schedule) throws ScheduleNotFoundException;

    public boolean deleteById(Schedule schedule) throws ScheduleNotFoundException;
}
