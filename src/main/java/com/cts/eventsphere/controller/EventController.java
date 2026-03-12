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
public class EventController {
    private final EventService eventService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZER', 'VENUE_MANAGER')")
    public ResponseEntity<EventResponseDto> create(@RequestBody EventRequestDto event) {
        log.info("Received request to create a new event: {}", event.name());
        EventResponseDto createdEvent = eventService.create(event);
        log.info("Successfully created event with ID: {}", createdEvent.id());
        return new ResponseEntity<>(createdEvent, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<EventResponseDto>> readAll() {
        log.info("Received request to fetch all events");
        List<EventResponseDto> events = eventService.findAllEvents();
        log.info("Successfully retrieved {} events", events.size());
        return ResponseEntity.ok(events);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZER', 'VENUE_MANAGER')")
    public ResponseEntity<Void> update(@PathVariable String id, @RequestBody EventRequestDto eventRequest) {
        log.info("Received request to update event with ID: {}", id);
        eventService.updateById(id, eventRequest);
        log.info("Successfully updated event with ID: {}", id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        log.info("Received request to delete event with ID: {}", id);
        eventService.deleteById(id);
        log.info("Successfully deleted event with ID: {}", id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/schedules")
    @PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZER', 'VENUE_MANAGER')")
    public ResponseEntity<ScheduleResponseDto> createActivity(@PathVariable String id, @RequestBody ScheduleRequestDto scheduleRequest) {
        log.info("Received request to add activity to event ID: {}", id);
        ScheduleResponseDto response = eventService.addActivity(id, scheduleRequest);
        log.info("Successfully added activity with ID: {} to event ID: {}", response.eventId(), id);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}/schedules")
    public ResponseEntity<List<ScheduleResponseDto>> getAllActivity(@PathVariable String id) {
        log.info("Received request to fetch all activities for event ID: {}", id);
        List<ScheduleResponseDto> schedules = eventService.findAllSchedules(id);
        log.info("Successfully retrieved {} activities for event ID: {}", schedules.size(), id);
        return ResponseEntity.ok(schedules);
    }
}