package com.cts.eventsphere.service.impl;

import com.cts.eventsphere.dto.engagement.EngagementRequestDto;
import com.cts.eventsphere.dto.engagement.EngagementResponseDto;
import com.cts.eventsphere.dto.mapper.engagement.EngagementRequestDtoMapper;
import com.cts.eventsphere.dto.mapper.engagement.EngagementResponseDtoMapper;
import com.cts.eventsphere.exception.engagement.EngagementNotFoundException;
import com.cts.eventsphere.exception.engagement.InvalidEngagementException;
import com.cts.eventsphere.model.Engagement;
import com.cts.eventsphere.model.data.AuditAction;
import com.cts.eventsphere.model.data.EngagementType;
import com.cts.eventsphere.repository.EngagementRepository;
import com.cts.eventsphere.service.AuditService;
import com.cts.eventsphere.service.EngagementService;
import com.cts.eventsphere.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service implementation for Engagement management.
 * Provides logic for recording activities, tracking audit logs, and sending notifications.
 *
 * @author 2480027
 * @version 1.1
 * @since 08-03-2026
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class EngagementServiceImpl implements EngagementService {

    private final EngagementRepository engagementRepository;
    private final AuditService auditService;
    private final NotificationService notificationService;

    @Override
    public EngagementResponseDto recordEngagement(EngagementRequestDto requestDto) {
        log.info("Recording {} for attendee={} event={}",
                requestDto.activity(), requestDto.attendeeId(), requestDto.eventId());

        if (requestDto.activityTimestamp() != null &&
                requestDto.activityTimestamp().isAfter(LocalDateTime.now())) {
            throw new InvalidEngagementException("Engagement timestamp cannot be in the future");
        }

        Engagement entity = EngagementRequestDtoMapper.toEntity(requestDto);
        Engagement saved = engagementRepository.save(entity);
        log.info("Engagement recorded with id={}", saved.getEngagementId());

        // Audit Logging
        auditService.logAudit(
                requestDto.attendeeId(),
                AuditAction.CREATE,
                Engagement.class,
                saved.getEngagementId()
        );

        // Notification Trigger
        notificationService.sendNotification(
                requestDto.attendeeId(),
                "Your activity '" + requestDto.activity() + "' has been successfully recorded.",
                "ENGAGEMENT_RECORDED"
        );

        return EngagementResponseDtoMapper.toDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EngagementResponseDto> getByEvent(String eventId) {
        log.info("Fetching engagements for event={}", eventId);
        List<Engagement> results = engagementRepository.findByEventId(eventId);

        if (results.isEmpty()) {
            throw new EngagementNotFoundException("No engagements found for event: " + eventId);
        }

        return results.stream()
                .map(EngagementResponseDtoMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EngagementResponseDto> getByActivityType(EngagementType activity) {
        log.info("Fetching engagements for activity={}", activity);
        List<Engagement> results = engagementRepository.findByActivity(activity);

        if (results.isEmpty()) {
            throw new EngagementNotFoundException("No engagements found for activity type: " + activity);
        }

        return results.stream()
                .map(EngagementResponseDtoMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EngagementResponseDto> getFilteredEngagements(String eventId, EngagementType activity,
                                                              LocalDateTime start, LocalDateTime end) {
        log.info("Filtering engagements event={} activity={} range={}..{}", eventId, activity, start, end);

        if (start != null && end != null && start.isAfter(end)) {
            throw new InvalidEngagementException("Start date must be before end date");
        }

        List<Engagement> results = engagementRepository.findByEventIdAndActivityAndTimestampBetween(eventId, activity, start, end);

        if (results.isEmpty()) {
            throw new EngagementNotFoundException("No engagements match the provided filters");
        }

        return results.stream()
                .map(EngagementResponseDtoMapper::toDTO)
                .collect(Collectors.toList());
    }
}