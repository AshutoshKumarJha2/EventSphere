package com.cts.eventsphere.service.impl;
import com.cts.eventsphere.dto.feedback.FeedbackRequestDto;
import com.cts.eventsphere.dto.feedback.FeedbackResponseDto;
import com.cts.eventsphere.dto.mapper.feedback.FeedbackRequestDtoMapper;
import com.cts.eventsphere.dto.mapper.feedback.FeedbackResponseDtoMapper;
import com.cts.eventsphere.exception.Feedback.FeedbackNotFoundException;
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
//    private final RegistrationService registrationService;

    // ---------------------- CREATE ----------------------
    @Override
    public FeedbackResponseDto create(FeedbackRequestDto request) {
        validateRating(request.rating());
//        ensureEligibleToSubmit(request.eventId(), request.attendeeId());
        ensureNotDuplicate(request.eventId(), request.attendeeId(), null);
        FeedBack entity = FeedbackRequestDtoMapper.toEntity(request);
        FeedBack saved = feedbackRepository.save(entity);
        return FeedbackResponseDtoMapper.toDTO(saved);
    }

    // ---------------------- READ ------------------------

    @Override
    @Transactional(readOnly = true)
    public Optional<FeedbackResponseDto> getById(String feedbackId) throws FeedbackNotFoundException {
        if(!feedbackRepository.existsById(feedbackId)){
            throw  new FeedbackNotFoundException("Feedback does not exist");
        }
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
    public Page<FeedbackResponseDto> listByEventAndDateRange(String eventId, LocalDateTime start, LocalDateTime end, Pageable pageable) {
        return feedbackRepository.findByEventIdAndDateBetween(eventId, start, end, pageable)
                .map(FeedbackResponseDtoMapper::toDTO);
    }




//
//    // ---------------------- UPDATE ----------------------
//    @Override
//    public FeedbackResponseDto update(String feedbackId, FeedbackRequestDto request) {
//        validateRating(request.rating()); // AC3
//
//        FeedBack existing = feedbackRepository.findById(feedbackId)
//                .orElseThrow(() -> new FeedbackNotFoundException("Feedback not found: " + feedbackId));
//
//        // If the association is being changed, enforce AC1 & AC2 again.
//        boolean eventChanged = !existing.getEventId().equals(request.eventId());
//        boolean attendeeChanged = !existing.getAttendeeId().equals(request.attendeeId());
//        if (eventChanged || attendeeChanged) {
////            ensureEligibleToSubmit(request.eventId(), request.attendeeId());             // AC2
//            ensureNotDuplicate(request.eventId(), request.attendeeId(), feedbackId);     // AC1
//            existing.setEventId(request.eventId());
//            existing.setAttendeeId(request.attendeeId());
//        }
//
//        existing.setRating(request.rating());
//        existing.setComments(request.comments());
//        existing.setDate(request.date());
//
//        FeedBack saved = feedbackRepository.save(existing);
//        return FeedbackResponseDtoMapper.toDTO(saved);
//    }
//
    // ---------------------- DELETE ----------------------
    @Override
    public void delete(String feedbackId) throws FeedbackNotFoundException {
        if(!feedbackRepository.existsById(feedbackId)){
            throw new FeedbackNotFoundException("Feedback Does not exist"+ feedbackId);
        }
        feedbackRepository.deleteById(feedbackId);
    }

    // ===================== GUARD METHODS =====================


    private void validateRating(int rating) {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5.");
        }
    }

//    private void ensureEligibleToSubmit(String eventId, String attendeeId) {
//        boolean eligible = registrationService.isConfirmedOrCheckedIn(eventId, attendeeId);
//        if (!eligible) {
//            throw new IllegalStateException("Only Confirmed or Checked-In attendees can submit feedback.");
//        }
//    }


    private void ensureNotDuplicate(String eventId, String attendeeId, String ignoreFeedbackId) {
        boolean exists = !feedbackRepository
                .findByEventIdAndAttendeeId(eventId, attendeeId, PageRequest.of(0, 1))
                .isEmpty();

        if (exists) {
            if (ignoreFeedbackId == null) {
                throw new EntityExistsException("Feedback already exists for this attendee in this event.");
            } else {
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