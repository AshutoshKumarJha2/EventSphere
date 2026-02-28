package com.cts.eventsphere.controller;

import com.cts.eventsphere.model.Schedule;
import com.cts.eventsphere.service.ScheduleService;
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
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/schedules")
public class ScheduleController {
    private final ScheduleService scheduleService;

    /**
    * [One line summary of method].
    * [Optional detailed explanation].
    *
    * @param name description
    * @return description of the return value
    * @throws IOException if a network error occurs
    */
    public ResponseEntity<Schedule> update(Schedule schedule){

    }

    public ResponseEntity<Schedule> delete(String id) {

    }
}
