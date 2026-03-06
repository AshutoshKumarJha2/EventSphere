package com.cts.eventsphere.service;

import com.cts.eventsphere.model.Engagement;
import com.cts.eventsphere.model.data.EngagementType;
import java.time.LocalDateTime;
import java.util.List;
/**
 * Service for Engagement Operations
 *
 * @author 2480027
 * @version 1.0
 * @since 05-03-2026
 */
public interface EngagementService {

    Engagement recordEngagement(Engagement engagement);

    List<Engagement> getByEvent(String eventId);

    List<Engagement> getByActivityType(EngagementType activity);

    List<Engagement> getFilteredEngagements(String eventId, EngagementType activity, LocalDateTime start, LocalDateTime end);
}