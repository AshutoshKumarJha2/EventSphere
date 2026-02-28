package com.cts.eventsphere.service.impl;

import com.cts.eventsphere.dto.EventResponseDto;
import com.cts.eventsphere.exception.event.EventNotFoundException;
import com.cts.eventsphere.model.Event;
import com.cts.eventsphere.model.Schedule;
import com.cts.eventsphere.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation for Service Interface for Event Class.
 * * @author 2479623
 *
 * @version 1.0
 * @since 27-02-2026
 */
@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventResponseDto eventResponseDto;

    /**
     * @param event
     * @return
     */
    @Override
    public Event create(Event event) {
        return null;
    }

    /**
     * @return
     */
    @Override
    public List<Event> findAllEvents() {
        return List.of();
    }

    /**
     * @param eventId
     * @return
     * @throws EventNotFoundException
     */
    @Override
    public Event findById(String eventId) throws EventNotFoundException {
        return null;
    }

    /**
     * @param eventId
     * @return
     * @throws EventNotFoundException
     */
    @Override
    public boolean updateById(String eventId) throws EventNotFoundException {
        return false;
    }

    /**
     * @param eventId
     * @return
     * @throws EventNotFoundException
     */
    @Override
    public boolean deleteById(String eventId) throws EventNotFoundException {
        return false;
    }

    /**
     * @param schedule
     * @return
     */
    @Override
    public boolean addActivity(Schedule schedule) {
        return false;
    }

    /**
     * @return
     */
    @Override
    public List<Schedule> findAllSchedules() {
        return List.of();
    }
}
