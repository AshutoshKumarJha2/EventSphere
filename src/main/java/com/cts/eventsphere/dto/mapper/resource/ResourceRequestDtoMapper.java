package com.cts.eventsphere.dto.mapper.resource;

import com.cts.eventsphere.dto.resource.ResourceRequestDto;
import com.cts.eventsphere.model.Resource;
import com.cts.eventsphere.model.data.Availability;

/**
 * Mapper utility to convert ResourceRequestDto to Resource Entity.
 */
public class ResourceRequestDtoMapper {

    public static Resource toEntity(ResourceRequestDto dto) {
        if (dto == null) {
            return null;
        }

        Resource resource = new Resource();

        resource.setName(dto.name());
        resource.setUnit(dto.unit());
        resource.setType(dto.type());
        resource.setCostRate(dto.costRate());

        resource.setAvailability(Availability.available);

        return resource;
    }
}