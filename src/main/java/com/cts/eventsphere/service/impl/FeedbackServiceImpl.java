package com.cts.eventsphere.service.impl;

import com.cts.eventsphere.dto.feedback.FeedbackRequestDto;
import com.cts.eventsphere.dto.feedback.FeedbackResponseDto;
import com.cts.eventsphere.dto.mapper.feedback.FeedbackRequestDtoMapper;
import com.cts.eventsphere.dto.mapper.feedback.FeedbackResponseDtoMapper;
import com.cts.eventsphere.model.FeedBack;
import com.cts.eventsphere.repository.FeedbackRepository;
import com.cts.eventsphere.service.FeedbackService;
//import com.cts.eventsphere.service.registration.RegistrationService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackRepository feedbackRepository;
//    private final RegistrationService registrationService; // Used to enforce AC2

    // ---------------------- CREATE ----------------------
    @Override
    public FeedbackResponseDto create(FeedbackRequestDto request) {
        validateRating(request.rating());                                   // AC3
//        ensureEligibleToSubmit(request.eventId(), request.attendeeId());     // AC2
        ensureNotDuplicate(request.eventId(), request.attendeeId(), null);   // AC1

        FeedBack entity = FeedbackRequestDtoMapper.toEntity(request);
        FeedBack saved = feedbackRepository.save(entity);
        return FeedbackResponseDtoMapper.toDTO(saved);
    }

    // ---------------------- READ ------------------------
    @Override
    @Transactional(readOnly = true)
    public Optional<FeedbackResponseDto> getById(String feedbackId) {
        return feedbackRepository.findById(feedbackId)
                .map(FeedbackResponseDtoMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FeedbackResponseDto> listByEvent(String eventId, Pageable pageable) {
        return feedbackRepository.findByEventId(eventId, pageable)
                .map(FeedbackResponseDtoMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FeedbackResponseDto> listByEventAndAttendee(String eventId, String attendeeId, Pageable pageable) {
        return feedbackRepository.findByEventIdAndAttendeeId(eventId, attendeeId, pageable)
                .map(FeedbackResponseDtoMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FeedbackResponseDto> listByEventAndDateRange(String eventId, LocalDateTime start, LocalDateTime end, Pageable pageable) {
        return feedbackRepository.findByEventIdAndDateBetween(eventId, start, end, pageable)
                .map(FeedbackResponseDtoMapper::toDTO);
    }

    // ---------------------- UPDATE ----------------------
    @Override
    public FeedbackResponseDto update(String feedbackId, FeedbackRequestDto request) {
        validateRating(request.rating()); // AC3

        FeedBack existing = feedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new EntityNotFoundException("Feedback not found: " + feedbackId));

        // If the association is being changed, enforce AC1 & AC2 again.
        boolean eventChanged = !existing.getEventId().equals(request.eventId());
        boolean attendeeChanged = !existing.getAttendeeId().equals(request.attendeeId());
        if (eventChanged || attendeeChanged) {
//            ensureEligibleToSubmit(request.eventId(), request.attendeeId());             // AC2
            ensureNotDuplicate(request.eventId(), request.attendeeId(), feedbackId);     // AC1
            existing.setEventId(request.eventId());
            existing.setAttendeeId(request.attendeeId());
        }

        existing.setRating(request.rating());
        existing.setComments(request.comments());
        existing.setDate(request.date());

        FeedBack saved = feedbackRepository.save(existing);
        return FeedbackResponseDtoMapper.toDTO(saved);
    }

    // ---------------------- DELETE ----------------------
    @Override
    public void delete(String feedbackId) {
        try {
            feedbackRepository.deleteById(feedbackId);
        } catch (EmptyResultDataAccessException ex) {
            throw new EntityNotFoundException("Feedback not found: " + feedbackId);
        }
    }

    // ===================== GUARD METHODS =====================

    /** AC3: rating must be 1..5 */
    private void validateRating(int rating) {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5.");
        }
    }

//    /** AC2: Only Confirmed/Checked-In attendees can submit feedback */
//    private void ensureEligibleToSubmit(String eventId, String attendeeId) {
//        boolean eligible = registrationService.isConfirmedOrCheckedIn(eventId, attendeeId);
//        if (!eligible) {
//            throw new IllegalStateException("Only Confirmed or Checked-In attendees can submit feedback.");
//        }
//    }

    /**
     * AC1: Ensure only one feedback per attendee per event.
     * If updating, allow the *same* record to pass via ignoreFeedbackId.
     */
    private void ensureNotDuplicate(String eventId, String attendeeId, String ignoreFeedbackId) {
        // Since the repository currently doesn't expose an existsByEventIdAndAttendeeId,
        // we fetch just the first page with size=1 to check existence efficiently.
        boolean exists = !feedbackRepository
                .findByEventIdAndAttendeeId(eventId, attendeeId, PageRequest.of(0, 1))
                .isEmpty();

        if (exists) {
            // If we're in update flow, ensure the found feedback is not a *different* one.
            if (ignoreFeedbackId == null) {
                throw new EntityExistsException("Feedback already exists for this attendee in this event.");
            } else {
                // When repository methods are limited, a second fetch by ID confirms ownership.
                Optional<FeedBack> maybeSame = feedbackRepository.findById(ignoreFeedbackId);
                if (maybeSame.isEmpty() ||
                        !eventId.equals(maybeSame.get().getEventId()) ||
                        !attendeeId.equals(maybeSame.get().getAttendeeId())) {
                    throw new EntityExistsException("Another feedback already exists for this attendee in this event.");
                }
            }
        }
    }
}