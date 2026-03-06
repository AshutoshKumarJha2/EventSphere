package com.cts.eventsphere.controller;

import com.cts.eventsphere.model.Engagement;
import com.cts.eventsphere.model.data.EngagementType;
import com.cts.eventsphere.service.EngagementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Controller for Engagement Operations
 *
 * @author 2480027
 * @version 1.0
 * @since 03-03-2026
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/engagements")
public class EngagementController {

    private final EngagementService engagementService;

    public EngagementController(EngagementService engagementService) {
        this.engagementService = engagementService;
    }


    @GetMapping("/event/{eventId}/log")
    public ResponseEntity<List<Engagement>> getByEvent(@PathVariable String eventId) {
        log.info("API: get engagements for event={}", eventId);
        return ResponseEntity.ok(engagementService.getByEvent(eventId));
    }


    @GetMapping("/activity/{activity}/log")
    public ResponseEntity<List<Engagement>> getByActivity(@PathVariable EngagementType activity) {
        log.info("API: get engagements for activity={}", activity);
        return ResponseEntity.ok(engagementService.getByActivityType(activity));
    }

    @GetMapping("/filter")
    public ResponseEntity<List<Engagement>> getDetailedFilter(
            @RequestParam String eventId,
            @RequestParam EngagementType activity,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {

        log.info("API: filter engagements event={} activity={} range={}..{}", eventId, activity, start, end);
        return ResponseEntity.ok(engagementService.getFilteredEngagements(eventId, activity, start, end));
    }
}
