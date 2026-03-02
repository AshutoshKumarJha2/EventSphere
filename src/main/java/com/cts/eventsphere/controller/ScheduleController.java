package com.cts.eventsphere.controller;

import com.cts.eventsphere.dto.schedule.ScheduleRequestDto;
import com.cts.eventsphere.dto.schedule.ScheduleResponseDto;
import com.cts.eventsphere.model.Schedule;
import com.cts.eventsphere.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Rest Controller for Schedule Entity.
 * * @author 2479623
 *
 * @version 1.0
 * @since 27-02-2026
 */
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/events/{eventId}/schedules")
public class ScheduleController {
    private final ScheduleService scheduleService;

    @PostMapping("/{id}")
    public ResponseEntity<ScheduleResponseDto> update(@PathVariable String eventId, @PathVariable String id, ScheduleRequestDto scheduleRequest){
        return ResponseEntity.ok(scheduleService.updateById(eventId, id, scheduleRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Schedule> delete(@PathVariable String id) {
        scheduleService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
