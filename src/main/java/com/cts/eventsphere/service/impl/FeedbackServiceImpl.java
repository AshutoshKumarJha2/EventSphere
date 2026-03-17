package com.cts.eventsphere.service.impl;

import com.cts.eventsphere.dto.feedback.FeedbackRequestDto;
import com.cts.eventsphere.dto.feedback.FeedbackResponseDto;
import com.cts.eventsphere.dto.mapper.feedback.FeedbackRequestDtoMapper;
import com.cts.eventsphere.dto.mapper.feedback.FeedbackResponseDtoMapper;
import com.cts.eventsphere.exception.Feedback.FeedbackNotFoundException;
import com.cts.eventsphere.model.FeedBack;
import com.cts.eventsphere.model.data.AuditAction;
import com.cts.eventsphere.repository.FeedbackRepository;
import com.cts.eventsphere.repository.RegistrationRepository;
import com.cts.eventsphere.service.AuditService;
import com.cts.eventsphere.service.FeedbackService;
import com.cts.eventsphere.service.NotificationService;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Service implementation for Feedback management.
 * Integrates with AuditService for activity tracking and NotificationService for user alerts.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final RegistrationRepository registrationRepository;
    private final NotificationService notificationService;
    private final AuditService auditService;

    @Override
    public FeedbackResponseDto create(FeedbackRequestDto request) {
        log.info("Processing feedback creation for event: {}", request.eventId());

        validateRating(request.rating());
        ensureEligibleToSubmit(request.eventId(), request.attendeeId());
        ensureNotDuplicate(request.eventId(), request.attendeeId());

        FeedBack entity = FeedbackRequestDtoMapper.toEntity(request);
        FeedBack saved = feedbackRepository.save(entity);
        log.info("Feedback saved with ID: {}", saved.getFeedbackId());

        // Standard Audit Call
        auditService.logAudit(request.attendeeId(), AuditAction.CREATE, FeedBack.class, saved.getFeedbackId());

        // Notification Call
        notificationService.sendNotification(
                request.attendeeId(),
                "Thank you! Your feedback for event " + request.eventId() + " has been received.",
                "FEEDBACK"
        );

        return FeedbackResponseDtoMapper.toDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public FeedbackResponseDto getById(String feedbackId) {
        log.info("Fetching feedback: {}", feedbackId);
        return feedbackRepository.findById(feedbackId)
                .map(FeedbackResponseDtoMapper::toDTO)
                .orElseThrow(() -> new FeedbackNotFoundException(feedbackId));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FeedbackResponseDto> listByEvent(String eventId, Pageable pageable) {
        return feedbackRepository.findByEventId(eventId, pageable)
                .map(FeedbackResponseDtoMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FeedbackResponseDto> listByEventAndDateRange(
            String eventId, LocalDateTime start, LocalDateTime end, Pageable pageable) {
        return feedbackRepository.findByEventIdAndDateBetween(eventId, start, end, pageable)
                .map(FeedbackResponseDtoMapper::toDTO);
    }

    @Override
    public void delete(String feedbackId) {
        log.info("Deleting feedback: {}", feedbackId);
        FeedBack feedback = feedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new FeedbackNotFoundException(feedbackId));

        feedbackRepository.deleteById(feedbackId);

        // Audit for deletion - using "SYSTEM" or a specific admin ID if available
        auditService.logAudit(feedback.getAttendeeId(), AuditAction.DELETE, FeedBack.class, feedbackId);
    }

    // ===================== HELPER METHODS =====================

    private void validateRating(int rating) {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5.");
        }
    }

    private void ensureEligibleToSubmit(String eventId, String attendeeId) {
        var registration = registrationRepository.findByAttendeeUserIdAndEventEventId(attendeeId, eventId)
                .orElseThrow(() -> new IllegalStateException("Attendee is not registered for this event."));

        String status = registration.getStatus().name();
        if (!(status.equalsIgnoreCase("confirmed") || status.equalsIgnoreCase("checked_in"))) {
            throw new IllegalStateException("Only confirmed or checked-in attendees can provide feedback.");
        }
    }

    private void ensureNotDuplicate(String eventId, String attendeeId) {
        boolean exists = !feedbackRepository
                .findByEventIdAndAttendeeId(eventId, attendeeId, PageRequest.of(0, 1))
                .isEmpty();

        if (exists) {
            throw new EntityExistsException("Feedback already submitted for this event.");
        }
    }
}