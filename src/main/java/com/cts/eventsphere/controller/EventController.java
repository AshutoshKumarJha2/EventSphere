package com.cts.eventsphere.controller;

import com.cts.eventsphere.dto.event.EventRequestDto;
import com.cts.eventsphere.dto.event.EventResponseDto;
import com.cts.eventsphere.dto.schedule.ScheduleRequestDto;
import com.cts.eventsphere.dto.schedule.ScheduleResponseDto;
import com.cts.eventsphere.service.EventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Rest Controller for Event Entity.
 * * @author 2479623
 *
 * @version 1.0
 * @since 27-02-2026
 */
@RestController
@RequestMapping("/api/v1/events")
@Slf4j
@RequiredArgsConstructor
@EnableMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true
)
public class EventController {
    private final EventService eventService;

    @PostMapping
    @PreAuthorize("hasAnyRole('admin', 'organizer', 'venue_manager')")
    public ResponseEntity<EventResponseDto> create(@RequestBody EventRequestDto event) {
        return new ResponseEntity<>(eventService.create(event), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<EventResponseDto>> readAll() {
        return ResponseEntity.ok(eventService.findAllEvents());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('admin', 'organizer', 'venue_manager')")
    public ResponseEntity<Void> update(@PathVariable String id, @RequestBody EventRequestDto eventRequest) {
        eventService.updateById(id, eventRequest);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        eventService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/schedules")
    @PreAuthorize("hasAnyRole('admin', 'organizer', 'venue_manager')")
    public ResponseEntity<ScheduleResponseDto> createActivity(@PathVariable String id, @RequestBody ScheduleRequestDto scheduleRequest) {
        return new ResponseEntity<>(eventService.addActivity(id, scheduleRequest), HttpStatus.CREATED);
    }

    @GetMapping("/{id}/schedules")
    public ResponseEntity<List<ScheduleResponseDto>> getAllActivity(@PathVariable String id) {
        return ResponseEntity.ok(eventService.findAllSchedules(id));
    }
}
