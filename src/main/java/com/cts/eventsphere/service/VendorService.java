package com.cts.eventsphere.service;

import com.cts.eventsphere.dto.vendor.VendorRequestDto;
import com.cts.eventsphere.dto.vendor.VendorResponseDto;

import java.util.List;

public interface VendorService {
    VendorResponseDto createVendor(VendorRequestDto request);

    VendorResponseDto getVendorById(String vendorId);

    List<VendorResponseDto> getAllVendors();

    VendorResponseDto updateVendor(String vendorId, VendorRequestDto request);

    void deleteVendor(String vendorId);
}
