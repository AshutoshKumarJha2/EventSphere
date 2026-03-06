package com.cts.eventsphere.service.impl;

import com.cts.eventsphere.exception.engagement.InvalidEngagementException;
import com.cts.eventsphere.model.Engagement;
import com.cts.eventsphere.model.data.EngagementType;
import com.cts.eventsphere.repository.EngagementRepository;
import com.cts.eventsphere.service.EngagementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class EngagementServiceImpl implements EngagementService {

    private final EngagementRepository engagementRepository;

    public EngagementServiceImpl(EngagementRepository engagementRepository) {
        this.engagementRepository = engagementRepository;
    }

    @Override
    public Engagement recordEngagement(Engagement engagement) {
        log.info("Recording {} for attendee={} event={}",
                engagement.getActivity(), engagement.getAttendeeId(), engagement.getEventId());

        if (engagement.getActivity() == null) {
            // minimal logging; let the global handler format the response
            throw new InvalidEngagementException("Activity type must be provided");
        }

        Engagement saved = engagementRepository.save(engagement);
        log.info("Engagement recorded id={}", saved.getEngagementId());
        return saved;
    }

    @Override
    public List<Engagement> getByEvent(String eventId) {
        log.info("Fetching engagements for event={}", eventId);
        return engagementRepository.findByEventId(eventId);
    }

    @Override
    public List<Engagement> getByActivityType(EngagementType activity) {
        log.info("Fetching engagements for activity={}", activity);
        return engagementRepository.findByActivity(activity);
    }

    @Override
    public List<Engagement> getFilteredEngagements(String eventId, EngagementType activity,
                                                   LocalDateTime start, LocalDateTime end) {
        log.info("Fetching engagements event={} activity={} range={}..{}", eventId, activity, start, end);
        return engagementRepository.findByEventIdAndActivityAndTimestampBetween(eventId, activity, start, end);
    }
}