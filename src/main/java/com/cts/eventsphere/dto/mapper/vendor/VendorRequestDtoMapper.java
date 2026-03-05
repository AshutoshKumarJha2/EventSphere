package com.cts.eventsphere.dto.mapper.vendor;


import com.cts.eventsphere.dto.vendor.VendorRequestDto;
import com.cts.eventsphere.model.Vendor;
import org.springframework.stereotype.Component;

@Component
public class VendorRequestDtoMapper{
    public Vendor toEntity(VendorRequestDto dto){
        Vendor vendor = new Vendor();
        vendor.setName(dto.name());
        vendor.setContactInfo(dto.contactInfo());
        vendor.setStatus(dto.status());
        return vendor;
    }
}
