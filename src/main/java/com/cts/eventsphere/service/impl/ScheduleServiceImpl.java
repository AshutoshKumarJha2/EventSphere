package com.cts.eventsphere.service.impl;

import com.cts.eventsphere.dto.mapper.schedule.ScheduleRequestDtoMapper;
import com.cts.eventsphere.dto.mapper.schedule.ScheduleResponseDtoMapper;
import com.cts.eventsphere.dto.schedule.ScheduleRequestDto;
import com.cts.eventsphere.dto.schedule.ScheduleResponseDto;
import com.cts.eventsphere.exception.event.EventNotFoundException;
import com.cts.eventsphere.exception.schedule.ScheduleNotFoundException;
import com.cts.eventsphere.model.Event;
import com.cts.eventsphere.model.Schedule;
import com.cts.eventsphere.repository.EventRepository;
import com.cts.eventsphere.repository.ScheduleRepository;
import com.cts.eventsphere.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Implementation for Service of Schedule Entity.
 * * @author 2479623
 *
 * @version 1.0
 * @since 27-02-2026
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduleServiceImpl implements ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final EventRepository eventRepository;
    private final ScheduleResponseDtoMapper scheduleResponseDtoMapper;
    private final ScheduleRequestDtoMapper scheduleRequestDtoMapper;

    /**
     * @param scheduleRequest
     * @return
     * @throws ScheduleNotFoundException
     */
    @Override
    public ScheduleResponseDto updateById(String eventId, String id, ScheduleRequestDto scheduleRequest) throws ScheduleNotFoundException {
        log.info("Attempting to update schedule ID: {} for event ID: {}", id, eventId);

        if(!scheduleRepository.existsById(id)) {
            log.error("Update failed: Schedule with ID {} does not exist", id);
            throw new ScheduleNotFoundException(id);
        }

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> {
                    log.error("Update failed: Event with ID {} not found", eventId);
                    return new EventNotFoundException(eventId);
                });

        log.debug("Mapping ScheduleRequestDto to Schedule entity for ID: {}", id);
        Schedule schedule = scheduleRequestDtoMapper.toEntity(scheduleRequest, event);
        schedule.setScheduleId(id);

        Schedule updatedSchedule = scheduleRepository.save(schedule);
        log.info("Successfully saved updated schedule ID: {}", updatedSchedule.getScheduleId());

        return scheduleResponseDtoMapper.toDTO(updatedSchedule);
    }

    /**
     * @param id
     * @return
     * @throws ScheduleNotFoundException
     */
    @Override
    public boolean deleteById(String id) throws ScheduleNotFoundException {
        log.info("Attempting to delete schedule with ID: {}", id);

        if(!scheduleRepository.existsById(id)) {
            log.warn("Delete aborted: Schedule ID {} not found in repository", id);
            throw new ScheduleNotFoundException(id);
        }

        scheduleRepository.deleteById(id);
        log.info("Successfully deleted schedule with ID: {}", id);
        return true;
    }
}