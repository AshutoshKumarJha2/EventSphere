package com.cts.eventsphere.service.impl;

import com.cts.eventsphere.dto.feedback.FeedbackRequestDto;
import com.cts.eventsphere.dto.feedback.FeedbackResponseDto;
import com.cts.eventsphere.dto.mapper.feedback.FeedbackRequestDtoMapper;
import com.cts.eventsphere.dto.mapper.feedback.FeedbackResponseDtoMapper;
import com.cts.eventsphere.dto.registration.RegistrationDTO;
import com.cts.eventsphere.exception.Feedback.FeedbackNotFoundException;
import com.cts.eventsphere.model.FeedBack;
import com.cts.eventsphere.repository.FeedbackRepository;
import com.cts.eventsphere.service.FeedbackService;
import com.cts.eventsphere.service.RegistrationService;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Service implementation class for feedback entity
 *
 * @author 2480027
 * @version 1.0
 * @since 06-03-2026
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final RegistrationService registrationService;

    @Override
    public FeedbackResponseDto create(FeedbackRequestDto request) {
        log.info("Creating feedback for event={}, attendee={}", request.eventId(), request.attendeeId());

        validateRating(request.rating());
        ensureEligibleToSubmit(request.eventId(), request.attendeeId());
        ensureNotDuplicate(request.eventId(), request.attendeeId(), null);

        FeedBack entity = FeedbackRequestDtoMapper.toEntity(request);
        FeedBack saved = feedbackRepository.save(entity);

        log.info("Feedback created with id={}", saved.getFeedbackId());
        return FeedbackResponseDtoMapper.toDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FeedbackResponseDto> getById(String feedbackId) throws FeedbackNotFoundException {
        log.info("Fetching feedback id={}", feedbackId);

        if (!feedbackRepository.existsById(feedbackId)) {
            log.warn("Feedback not found id={}", feedbackId);
            throw new FeedbackNotFoundException("Feedback does not exist");
        }

        return feedbackRepository.findById(feedbackId)
                .map(FeedbackResponseDtoMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FeedbackResponseDto> listByEvent(String eventId, Pageable pageable) {
        log.info("Fetching feedback list for event={}", eventId);
        return feedbackRepository.findByEventId(eventId, pageable)
                .map(FeedbackResponseDtoMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FeedbackResponseDto> listByEventAndDateRange(
            String eventId, LocalDateTime start, LocalDateTime end, Pageable pageable) {

        log.info("Fetching feedback for event={} within date range", eventId);

        return feedbackRepository.findByEventIdAndDateBetween(eventId, start, end, pageable)
                .map(FeedbackResponseDtoMapper::toDTO);
    }

    @Override
    public void delete(String feedbackId) throws FeedbackNotFoundException {
        log.info("Deleting feedback id={}", feedbackId);

        if (!feedbackRepository.existsById(feedbackId)) {
            log.warn("Cannot delete — feedback not found id={}", feedbackId);
            throw new FeedbackNotFoundException("Feedback Does not exist " + feedbackId);
        }

        feedbackRepository.deleteById(feedbackId);
        log.info("Feedback deleted id={}", feedbackId);
    }

    // ===================== GUARD METHODS =====================

    private void validateRating(int rating) {
        if (rating < 1 || rating > 5) {
            log.warn("Invalid rating {}", rating);
            throw new IllegalArgumentException("Rating must be between 1 and 5.");
        }
    }

    private void ensureEligibleToSubmit(String eventId, String attendeeId) {
        RegistrationDTO dto = registrationService.getRegistrationByEventIdAndUserId(eventId, attendeeId);

        if (dto == null || dto.status() == null) {
            log.warn("No valid registration found for event={}, attendee={}", eventId, attendeeId);
            throw new IllegalStateException("Only Confirmed or Checked-In attendees can submit feedback.");
        }

        boolean eligible = dto.status().equalsIgnoreCase("Confirmed")
                || dto.status().equalsIgnoreCase("CheckedIn");

        if (!eligible) {
            log.warn("Attendee not eligible: event={}, attendee={}, status={}",
                    eventId, attendeeId, dto.status());
            throw new IllegalStateException("Only Confirmed or Checked-In attendees can submit feedback.");
        }
    }

    private void ensureNotDuplicate(String eventId, String attendeeId, String ignoreFeedbackId) {
        boolean exists = !feedbackRepository
                .findByEventIdAndAttendeeId(eventId, attendeeId, PageRequest.of(0, 1))
                .isEmpty();

        if (exists && ignoreFeedbackId == null) {
            log.warn("Duplicate feedback for event={}, attendee={}", eventId, attendeeId);
            throw new EntityExistsException("Feedback already exists for this attendee in this event.");
        }

        if (exists && ignoreFeedbackId != null) {
            Optional<FeedBack> existing = feedbackRepository.findById(ignoreFeedbackId);
            boolean same = existing.isPresent()
                    && eventId.equals(existing.get().getEventId())
                    && attendeeId.equals(existing.get().getAttendeeId());

            if (!same) {
                log.warn("Another feedback exists for event={}, attendee={} (different ID)", eventId, attendeeId);
                throw new EntityExistsException("Another feedback already exists for this attendee in this event.");
            }
        }
    }
}