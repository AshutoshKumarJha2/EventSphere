package com.cts.eventsphere.service.impl;

import com.cts.eventsphere.dto.event.EventRequestDto;
import com.cts.eventsphere.dto.event.EventResponseDto;
import com.cts.eventsphere.dto.mapper.event.EventRequestDtoMapper;
import com.cts.eventsphere.dto.mapper.event.EventResponseDtoMapper;
import com.cts.eventsphere.dto.mapper.schedule.ScheduleRequestDtoMapper;
import com.cts.eventsphere.dto.mapper.schedule.ScheduleResponseDtoMapper;
import com.cts.eventsphere.dto.schedule.ScheduleRequestDto;
import com.cts.eventsphere.dto.schedule.ScheduleResponseDto;
import com.cts.eventsphere.exception.event.EventNotFoundException;
import com.cts.eventsphere.model.Event;
import com.cts.eventsphere.model.Schedule;
import com.cts.eventsphere.repository.EventRepository;
import com.cts.eventsphere.repository.ScheduleRepository;
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
    private final EventRepository eventRepository;
    private final EventResponseDtoMapper eventResponseDtoMapper;
    private final EventRequestDtoMapper eventRequestDtoMapper;
    private final ScheduleRepository scheduleRepository;
    private final ScheduleResponseDtoMapper scheduleResponseDtoMapper;
    private final ScheduleRequestDtoMapper scheduleRequestDtoMapper;

    /**
     * @param eventRequest
     * @return
     */
    @Override
    public EventResponseDto create(EventRequestDto eventRequest) {
        Event event = eventRequestDtoMapper.toEntity(eventRequest);
        Event savedEvent = eventRepository.save(event);

        return eventResponseDtoMapper.toDTO(savedEvent);
    }

    /**
     * @return
     */
    @Override
    public List<EventResponseDto> findAllEvents() {
        return eventRepository.findAll().stream()
                .map(eventResponseDtoMapper::toDTO)
                .toList();
    }

    /**
     * @param eventId
     * @return
     * @throws EventNotFoundException
     */
    @Override
    public EventResponseDto findById(String eventId) throws EventNotFoundException {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));

        return eventResponseDtoMapper.toDTO(event);
    }

    /**
     * @param eventId
     * @return
     * @throws EventNotFoundException
     */
    @Override
    public boolean updateById(String eventId, EventRequestDto eventRequest) throws EventNotFoundException {
        if(!eventRepository.existsById(eventId)) {
            throw new EventNotFoundException(eventId);
        }

        Event event = eventRequestDtoMapper.toEntity(eventRequest);
        event.setEventId(eventId);
        eventRepository.save(event);

        return true;
    }

    /**
     * @param eventId
     * @return
     * @throws EventNotFoundException
     */
    @Override
    public boolean deleteById(String eventId) throws EventNotFoundException {
        if(!eventRepository.existsById(eventId)) {
            throw new EventNotFoundException(eventId);
        }
        eventRepository.deleteById(eventId);

        return true;
    }

    /**
     * @param scheduleRequest
     * @return
     */
    @Override
    public ScheduleResponseDto addActivity(String eventId, ScheduleRequestDto scheduleRequest) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));

        Schedule schedule = scheduleRequestDtoMapper.toEntity(scheduleRequest, event);
        Schedule savedSchedule = scheduleRepository.save(schedule);

        return scheduleResponseDtoMapper.toDTO(savedSchedule);
    }

    /**
     * @return
     */
    @Override
    public List<ScheduleResponseDto> findAllSchedules(String eventId) {
        return scheduleRepository.findAll().stream()
                .filter(s -> s.getEvent().getEventId().equals(eventId))
                .map(scheduleResponseDtoMapper::toDTO)
                .toList();
    }
}
