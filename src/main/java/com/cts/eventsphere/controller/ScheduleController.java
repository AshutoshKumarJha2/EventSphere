package com.cts.eventsphere.controller;

import com.cts.eventsphere.dto.schedule.ScheduleRequestDto;
import com.cts.eventsphere.dto.schedule.ScheduleResponseDto;
import com.cts.eventsphere.model.Schedule;
import com.cts.eventsphere.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZER', 'VENUE_MANAGER')")
    public ResponseEntity<ScheduleResponseDto> update(@PathVariable String eventId, @PathVariable String id, @RequestBody ScheduleRequestDto scheduleRequest){
        log.info("Received request to update schedule with ID: {} for event ID: {}", id, eventId);
        ScheduleResponseDto response = scheduleService.updateById(eventId, id, scheduleRequest);
        log.info("Successfully updated schedule with ID: {}", id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Schedule> delete(@PathVariable String id) {
        log.info("Received request to delete schedule with ID: {}", id);
        scheduleService.deleteById(id);
        log.info("Successfully deleted schedule with ID: {}", id);
        return ResponseEntity.noContent().build();
    }
}