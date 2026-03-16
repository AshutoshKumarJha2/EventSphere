package com.cts.eventsphere.service;

import com.cts.eventsphere.dto.vendor.VendorRequestDto;
import com.cts.eventsphere.dto.vendor.VendorResponseDto;

import java.util.List;

/**
 * Service interface for Vendor Operations.
 * Defines the contract for managing vendor lifecycles including registration,
 * profile updates, and status management.
 *
 * @author 2480177
 * @version 1.0
 * @since 03-03-2026
 */

public interface VendorService {

    /**
     * Registers a new vendor in the system.
     *
     * @param request the vendor details to be saved
     * @return the saved vendor as a response DTO
     */
    VendorResponseDto createVendor(VendorRequestDto request);

    /**
     * Retrieves vendor details based on the provided ID.
     *
     * @param vendorId the unique identifier of the vendor
     * @return the vendor details response DTO
     */
    VendorResponseDto getVendorById(String vendorId);

    /**
     * Fetches all registered vendors from the database.
     *
     * @return a list of all vendor response DTOs
     */
    List<VendorResponseDto> getAllVendors();

    /**
     * Updates an existing vendor's profile information.
     *
     * @param vendorId the ID of the vendor to update
     * @param request the new details to apply
     * @return the updated vendor response DTO
     */
    VendorResponseDto updateVendor(String vendorId, VendorRequestDto request);

    /**
     * Removes a vendor record from the system.
     *
     * @param vendorId the ID of the vendor to delete
     */
    void deleteVendor(String vendorId);
}
