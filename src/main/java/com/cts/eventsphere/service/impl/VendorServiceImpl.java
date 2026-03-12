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

import java.util.List;

/**
 * Implementation for Vendor Service
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
     * @param request
     * @return
     */
    @Override
    public VendorResponseDto createVendor(VendorRequestDto request){
        log.info("Creating vendor...");
        Vendor vendor = requestDtoMapper.toEntity(request);
        Vendor saved = vendorRepository.save(vendor);
        log.info("Vendor created with ID={}", saved.getVendorId());
        return responseDtoMapper.toDto(saved);
    }

    /**
     * @param vendorId
     * @return
     * @throws VendorNotFoundException
     */
    @Override
    public VendorResponseDto getVendorById(String vendorId){
        log.info("Fetching vendor ID={}", vendorId);

        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new VendorNotFoundException(vendorId));

        return responseDtoMapper.toDto(vendor);
    }

    /**
     * @return
     */
    @Override
    public List<VendorResponseDto> getAllVendors(){
        log.info("Fetching all vendors");
        return vendorRepository.findAll()
                .stream()
                .map(responseDtoMapper::toDto)
                .toList();
    }

    /**
     * @param vendorId
     * @param request
     * @return
     * @throws VendorNotFoundException
     */
    @Override
    public VendorResponseDto updateVendor(String vendorId, VendorRequestDto request){
        log.info("Updating vendor ID={}", vendorId);

        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new VendorNotFoundException(vendorId));

        vendor.setName(request.name());
        vendor.setContactInfo(request.contactInfo());
        vendor.setStatus(request.status());

        Vendor updated = vendorRepository.save(vendor);
        return responseDtoMapper.toDto(updated);
    }

    /**
     * @param vendorId
     * @throws VendorNotFoundException
     */
    @Override
    public void deleteVendor(String vendorId){
        log.warn("Deleting vendor ID={}", vendorId);

        if(!vendorRepository.existsById(vendorId)){
            throw new VendorNotFoundException(vendorId);
        }

        vendorRepository.deleteById(vendorId);
    }
}