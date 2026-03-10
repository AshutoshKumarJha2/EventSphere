package com.cts.eventsphere.dto.mapper.resource;

import com.cts.eventsphere.dto.resource.ResourceRequestDto;
import com.cts.eventsphere.model.Resource;

public class ResourceResponseDtoMapper {
    /**
     * Maps an Entity back to a Request DTO if needed (e.g., for logging or debugging).
     */
    public static ResourceRequestDto toDto(Resource resource) {
        if (resource == null) {
            return null;
        }

        return new ResourceRequestDto(
                "Resource Name", // Placeholder: You might need to add 'name' to the Entity
                resource.getVenue().getVenueId(),
                resource.getType(),
                resource.getCostRate(),
                1 // Placeholder for unit/quantity
        );
    }
}
