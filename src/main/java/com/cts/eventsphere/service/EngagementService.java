package com.cts.eventsphere.service;

import com.cts.eventsphere.dto.engagement.EngagementRequestDto;
import com.cts.eventsphere.dto.engagement.EngagementResponseDto;
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

    EngagementResponseDto recordEngagement(EngagementRequestDto engagementRequestDto);

    List<EngagementResponseDto> getByEvent(String eventId);

    List<EngagementResponseDto> getByActivityType(EngagementType activity);

    List<EngagementResponseDto> getFilteredEngagements(String eventId, EngagementType activity, LocalDateTime start, LocalDateTime end);
}