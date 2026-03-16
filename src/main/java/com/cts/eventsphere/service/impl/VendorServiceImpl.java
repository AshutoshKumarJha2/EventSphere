package com.cts.eventsphere.service.impl;

import com.cts.eventsphere.dto.mapper.vendor.VendorRequestDtoMapper;
import com.cts.eventsphere.dto.mapper.vendor.VendorResponseDtoMapper;
import com.cts.eventsphere.dto.vendor.VendorRequestDto;
import com.cts.eventsphere.dto.vendor.VendorResponseDto;
import com.cts.eventsphere.exception.vendor.VendorNotFoundException;
import com.cts.eventsphere.model.Vendor;
import com.cts.eventsphere.repository.VendorRepository;
import com.cts.eventsphere.service.VendorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementation for Vendor Service.
 * Manages the registration and profile lifecycle of service providers.
 *
 * @author 2480177
 * @version 1.0
 * @since 03-03-2026
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class VendorServiceImpl implements VendorService {

    private final VendorRepository vendorRepository;
    private final VendorRequestDtoMapper requestDtoMapper;
    private final VendorResponseDtoMapper responseDtoMapper;

    /**
     * Registers a new vendor in the system.
     *
     * @param request the vendor details to be saved
     * @return the saved vendor as a response DTO
     */
    @Override
    @Transactional
    public VendorResponseDto createVendor(VendorRequestDto request){
        log.info("Attempting to create a new vendor: {}",request.name());
        Vendor vendor = requestDtoMapper.toEntity(request);
        Vendor saved = vendorRepository.save(vendor);
        log.info("Vendor created with ID={}", saved.getVendorId());
        return responseDtoMapper.toDto(saved);
    }

    /**
     * Retrieves vendor details based on the provided ID.
     *
     * @param vendorId the unique identifier of the vendor
     * @return the vendor details
     * @throws VendorNotFoundException if no vendor exists with the given ID
     */
    @Override
    @Transactional(readOnly = true)
    public VendorResponseDto getVendorById(String vendorId){
        log.info("Fetching vendor details for ID={}", vendorId);

        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new VendorNotFoundException("Vendor not found with ID: " +vendorId));

        return responseDtoMapper.toDto(vendor);
    }

    /**
     * Fetches all registered vendors from the database.
     *
     * @return a list of all vendor response DTOs
     */
    @Override
    @Transactional(readOnly = true)
    public List<VendorResponseDto> getAllVendors(){
        log.info("Fetching all vendor details from database");
        return vendorRepository.findAll()
                .stream()
                .map(responseDtoMapper::toDto)
                .toList();
    }

    /**
     * Updates an existing vendor's profile information.
     *
     * @param vendorId the ID of the vendor to update
     * @param request the new details to apply
     * @return the updated vendor details
     * @throws VendorNotFoundException if the vendor ID is invalid
     */
    @Override
    @Transactional
    public VendorResponseDto updateVendor(String vendorId, VendorRequestDto request){
        log.info("Updating vendor details for ID={}", vendorId);

        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new VendorNotFoundException("Cannot update. Vendor not found with ID: "+vendorId));

        vendor.setName(request.name());
        vendor.setContactInfo(request.contactInfo());
        vendor.setStatus(request.status());

        Vendor updated = vendorRepository.save(vendor);
        log.info("Vendor ID={} updated successfully", vendorId);
        return responseDtoMapper.toDto(updated);
    }

    /**
     * Removes a vendor record from the system.
     *
     * @param vendorId the ID of the vendor to delete
     * @throws VendorNotFoundException if the vendor ID does not exist
     */
    @Override
    @Transactional
    public void deleteVendor(String vendorId){
        log.info("Attempting to Delete vendor with ID={}", vendorId);

        if(!vendorRepository.existsById(vendorId)){
            throw new VendorNotFoundException("Vendor not found with ID: "+vendorId);
        }

        vendorRepository.deleteById(vendorId);
        log.info("Vendor ID={} deleted successfully", vendorId);
    }
}