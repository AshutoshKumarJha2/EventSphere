package com.cts.eventsphere.controller;

import com.cts.eventsphere.model.Event;
import com.cts.eventsphere.service.EventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    public ResponseEntity<Event> create(Event event) {

    }

    public ResponseEntity<Event> readAll() {

    }


}
