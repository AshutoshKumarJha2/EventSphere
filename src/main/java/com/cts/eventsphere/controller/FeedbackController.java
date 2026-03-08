package com.cts.eventsphere.controller;

import com.cts.eventsphere.dto.feedback.FeedbackRequestDto;
import com.cts.eventsphere.dto.feedback.FeedbackResponseDto;
import com.cts.eventsphere.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * Controller for feedback entity
 *
 * @author 2480027
 * @version 1.1
 * @since 03-03-2026
 */
@RestController
@RequestMapping("/api/v1/feedback")
@Slf4j
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService feedbackService;

    @PostMapping
//    @PreAuthorize("hasRole('ATTENDEE')")
    public FeedbackResponseDto create(@RequestBody FeedbackRequestDto feedbackRequestDto) {
        log.info("REST request to save Feedback : {}", feedbackRequestDto);
        FeedbackResponseDto result = feedbackService.create(feedbackRequestDto);
        log.info("Feedback created successfully with data: {}", result);
        return result;
    }

    @GetMapping("/{id}")
    public Optional<FeedbackResponseDto> getById(@PathVariable String id) {
        log.info("REST request to get Feedback by ID : {}", id);
        Optional<FeedbackResponseDto> response = feedbackService.getById(id);
        if (response.isEmpty()) {
            log.warn("Feedback with ID : {} not found", id);
        }
        return response;
    }

    @GetMapping("/event/{eventId}")
    public Page<FeedbackResponseDto> listByEvent(@PathVariable String eventId, Pageable pageable) {
        log.info("REST request to get a page of Feedbacks for Event ID : {} with Pageable: {}", eventId, pageable);
        Page<FeedbackResponseDto> page = feedbackService.listByEvent(eventId, pageable);
        log.info("Fetched {} feedback records for Event ID : {}", page.getNumberOfElements(), eventId);
        return page;
    }

    @DeleteMapping("/{feedbackId}")
    public void deleteFeedback(@PathVariable String feedbackId) {
        log.info("REST request to delete Feedback ID : {}", feedbackId);
        feedbackService.delete(feedbackId);
        log.info("Successfully deleted Feedback ID : {}", feedbackId);
    }
}