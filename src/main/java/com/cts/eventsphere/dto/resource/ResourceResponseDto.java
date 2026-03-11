package com.cts.eventsphere.dto.resource;

import com.cts.eventsphere.model.data.Availability;
import com.cts.eventsphere.model.data.ResourceType;

import java.math.BigDecimal;

public record ResourceResponseDto(String resourceId,
                                  String venueId,
                                  ResourceType type,
                                  String name,
                                  Availability availability,
                                  BigDecimal costRate
                                  ) {
}
