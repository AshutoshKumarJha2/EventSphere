package com.cts.eventsphere.service.impl;

import com.cts.eventsphere.dto.mapper.schedule.ScheduleRequestDtoMapper;
import com.cts.eventsphere.dto.mapper.schedule.ScheduleResponseDtoMapper;
import com.cts.eventsphere.dto.schedule.ScheduleRequestDto;
import com.cts.eventsphere.dto.schedule.ScheduleResponseDto;
import com.cts.eventsphere.exception.schedule.ScheduleNotFoundException;
import com.cts.eventsphere.model.Schedule;
import com.cts.eventsphere.repository.ScheduleRepository;
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
    private final ScheduleRepository scheduleRepository;
    private final ScheduleResponseDtoMapper scheduleResponseDtoMapper;
    private final ScheduleRequestDtoMapper scheduleRequestDtoMapper;

    /**
     * @param scheduleRequest
     * @return
     * @throws ScheduleNotFoundException
     */
    @Override
    public ScheduleResponseDto updateById(String id, ScheduleRequestDto scheduleRequest) throws ScheduleNotFoundException {
        if(!scheduleRepository.existsById(id)) {
            throw new ScheduleNotFoundException(id);
        }

        Schedule schedule = scheduleRequestDtoMapper.toEntity(scheduleRequest);
        schedule.setScheduleId(id);
        Schedule updatedSchedule = scheduleRepository.save(schedule);
        return scheduleResponseDtoMapper.toDTO(updatedSchedule);
    }

    /**
     * @param id
     * @return
     * @throws ScheduleNotFoundException
     */
    @Override
    public boolean deleteById(String id) throws ScheduleNotFoundException {
        if(!scheduleRepository.existsById(id)) {
            throw new ScheduleNotFoundException(id);
        }

        scheduleRepository.deleteById(id);
        return true;
    }
}
