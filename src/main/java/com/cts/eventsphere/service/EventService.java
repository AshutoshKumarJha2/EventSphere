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

    /**
     * Creates a new event in the system.
     *
     * @param event the request DTO containing event details
     * @return the response DTO representing the created event
     */
    EventResponseDto create(EventRequestDto event);

    /**
     * Retrieves all events available in the system.
     *
     * @return a list of response DTOs representing all events
     */
    List<EventResponseDto> findAllEvents();

    /**
     * Finds an event by its unique identifier.
     *
     * @param eventId the unique identifier of the event
     * @return the response DTO representing the event
     * @throws EventNotFoundException if no event exists with the given ID
     */
    EventResponseDto findById(String eventId) throws EventNotFoundException;

    /**
     * Updates an existing event by its unique identifier.
     *
     * @param eventId the unique identifier of the event to update
     * @param eventRequest the request DTO containing updated event details
     * @return true if the update was successful, false otherwise
     * @throws EventNotFoundException if no event exists with the given ID
     */
    boolean updateById(String eventId, EventRequestDto eventRequest) throws EventNotFoundException;

    /**
     * Deletes an event by its unique identifier.
     *
     * @param eventId the unique identifier of the event to delete
     * @return true if the deletion was successful, false otherwise
     * @throws EventNotFoundException if no event exists with the given ID
     */
    boolean deleteById(String eventId) throws EventNotFoundException;

    /**
     * Adds a new activity (schedule) to an existing event.
     *
     * @param eventId the unique identifier of the event
     * @param schedule the request DTO containing schedule details
     * @return the response DTO representing the added schedule
     */
    ScheduleResponseDto addActivity(String eventId, ScheduleRequestDto schedule);

    /**
     * Retrieves all schedules associated with a specific event.
     *
     * @param eventId the unique identifier of the event
     * @return a list of response DTOs representing all schedules for the event
     */
    List<ScheduleResponseDto> findAllSchedules(String eventId);
}
