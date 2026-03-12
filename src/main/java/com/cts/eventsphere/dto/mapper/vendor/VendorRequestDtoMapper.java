package com.cts.eventsphere.dto.mapper.vendor;


import com.cts.eventsphere.dto.vendor.VendorRequestDto;
import com.cts.eventsphere.model.Vendor;
import org.springframework.stereotype.Component;

/**
 * Mapper for converting Vendor entity to VendorRequestDto
 *
 * @author 2480177
 * @version 1.0
 * @since 02-03-2026
 */

@Component
public class VendorRequestDtoMapper{
    public Vendor toEntity(VendorRequestDto dto){
        if (dto == null) {
            return null;
        }
        Vendor vendor = new Vendor();
        vendor.setName(dto.name());
        vendor.setContactInfo(dto.contactInfo());
        vendor.setStatus(dto.status());
        return vendor;
    }
}
