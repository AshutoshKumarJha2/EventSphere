package com.cts.eventsphere.service;

import com.cts.eventsphere.dto.event.EventRequestDto;
import com.cts.eventsphere.dto.event.EventResponseDto;
import com.cts.eventsphere.dto.schedule.ScheduleRequestDto;
import com.cts.eventsphere.dto.schedule.ScheduleResponseDto;
import com.cts.eventsphere.exception.event.EventNotFoundException;
import com.cts.eventsphere.model.Schedule;

import java.util.List;

/**
 * Service Interface for Event Entity.
 * * @author 2479623
 *
 * @version 1.0
 * @since 27-02-2026
 */
public interface EventService {
    public EventResponseDto create(EventRequestDto event);

    public List<EventResponseDto> findAllEvents();

    public EventResponseDto findById(String eventId) throws EventNotFoundException;

    public boolean updateById(String eventId, EventRequestDto eventRequest) throws EventNotFoundException;

    public boolean deleteById(String eventId) throws EventNotFoundException;

    public ScheduleResponseDto addActivity(ScheduleRequestDto schedule);

    public List<ScheduleResponseDto> findAllSchedules();
}
