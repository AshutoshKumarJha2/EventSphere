package com.cts.eventsphere.controller;

import com.cts.eventsphere.dto.feedback.FeedbackRequestDto;
import com.cts.eventsphere.dto.feedback.FeedbackResponseDto;
import com.cts.eventsphere.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * [ Detailed description of the class's responsibility]
 *
 * @author 2480027
 * @version 1.0
 * @since 05-03-2026
 */
@RestController
@RequestMapping("/api/v1/feedback")
@Slf4j
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService feedbackService;

    @PostMapping
    public FeedbackResponseDto create(@RequestBody FeedbackRequestDto feedbackRequestDto) {
        return null;
    }


    public Optional<FeedbackResponseDto> getById(@PathVariable String id){

        return feedbackService.getById(id);
    }

    @GetMapping("/api/events/{eventId}/feedback")
    public Page<FeedbackResponseDto> listByEvent( @PathVariable String eventId, Pageable pageable) {

        return feedbackService.listByEvent(eventId, pageable);
    }

    @DeleteMapping("/api/events/{feedbackId}/feedback/delete")
    public void deleteFeedback(@PathVariable String feedbackId){
        feedbackService.delete(feedbackId);
    }


}
