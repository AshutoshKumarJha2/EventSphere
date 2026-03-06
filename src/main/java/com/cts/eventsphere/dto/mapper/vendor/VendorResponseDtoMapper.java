package com.cts.eventsphere.dto.mapper.vendor;

import com.cts.eventsphere.dto.vendor.VendorResponseDto;
import com.cts.eventsphere.model.Vendor;
import org.springframework.stereotype.Component;

/**
 * Mapper for converting Vendor entity to VendorResponseDto
 *
 * @author 2480177
 * @version 1.0
 * @since 02-03-2026
 */

@Component
public class VendorResponseDtoMapper{
    public VendorResponseDto toDto(Vendor vendor){
        return new VendorResponseDto(
                vendor.getVendorId(),
                vendor.getName(),
                vendor.getContactInfo(),
                vendor.getStatus(),
                vendor.getCreatedAt(),
                vendor.getUpdatedAt()
        );
    }
}
