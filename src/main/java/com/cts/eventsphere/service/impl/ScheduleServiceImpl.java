package com.cts.eventsphere.service.impl;

import com.cts.eventsphere.exception.schedule.ScheduleNotFoundException;
import com.cts.eventsphere.model.Schedule;
import com.cts.eventsphere.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * [Detailed description of the class's responsibility]
 * * @author 2479623
 *
 * @version 1.0
 * @since 27-02-2026
 */
@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {
    /**
     * @param schedule
     * @return
     * @throws ScheduleNotFoundException
     */
    @Override
    public boolean updateById(Schedule schedule) throws ScheduleNotFoundException {
        return false;
    }

    /**
     * @param schedule
     * @return
     * @throws ScheduleNotFoundException
     */
    @Override
    public boolean deleteById(Schedule schedule) throws ScheduleNotFoundException {
        return false;
    }
}
