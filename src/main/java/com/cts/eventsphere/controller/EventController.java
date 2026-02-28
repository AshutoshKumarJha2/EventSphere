package com.cts.eventsphere.controller;

import com.cts.eventsphere.dto.event.EventRequestDto;
import com.cts.eventsphere.dto.event.EventResponseDto;
import com.cts.eventsphere.dto.schedule.ScheduleRequestDto;
import com.cts.eventsphere.dto.schedule.ScheduleResponseDto;
import com.cts.eventsphere.model.Event;
import com.cts.eventsphere.service.EventService;
import com.cts.eventsphere.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * [Detailed description of the class's responsibility]
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
    public ResponseEntity<EventResponseDto> create(@RequestBody EventRequestDto event) {
        return new ResponseEntity<>(eventService.create(event), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<EventResponseDto>> readAll() {
        return ResponseEntity.ok(eventService.findAllEvents());
    }

    @GetMapping("/{id}")
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
    public ResponseEntity<ScheduleResponseDto> createActivity(@PathVariable String id, @RequestBody ScheduleRequestDto scheduleRequest) {
        return new ResponseEntity<>(eventService.addActivity(scheduleRequest), HttpStatus.CREATED);
    }

    @GetMapping("/{id}/schedules")
    public ResponseEntity<List<ScheduleResponseDto>> getAllActivity(@PathVariable String id) {
        return ResponseEntity.ok(eventService.findAllSchedules());
    }
}
