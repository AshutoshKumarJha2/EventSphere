package com.cts.eventsphere.dto.resource;

import com.cts.eventsphere.model.data.ResourceType;

import java.math.BigDecimal;

public record ResourceRequestDto(
                                String name,
                                 String venueId,
                                 ResourceType type,
                                 BigDecimal costRate,
                                 Integer unit
                               ) {
}
