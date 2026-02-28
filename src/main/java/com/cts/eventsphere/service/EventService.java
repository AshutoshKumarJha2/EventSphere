package com.cts.eventsphere.service;

import com.cts.eventsphere.exception.event.EventNotFoundException;
import com.cts.eventsphere.model.Event;
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
    public Event create(Event event);

    public List<Event> findAllEvents();

    public Event findById(String eventId) throws EventNotFoundException;

    public boolean updateById(String eventId) throws EventNotFoundException;

    public boolean deleteById(String eventId) throws EventNotFoundException;

    public boolean addActivity(Schedule schedule);

    public List<Schedule> findAllSchedules();
}
