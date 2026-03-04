package com.cts.eventsphere.service.impl;

import com.cts.eventsphere.dto.mapper.vendor.VendorRequestDtoMapper;
import com.cts.eventsphere.dto.mapper.vendor.VendorResponseDtoMapper;
import com.cts.eventsphere.dto.vendor.VendorRequestDto;
import com.cts.eventsphere.dto.vendor.VendorResponseDto;
import com.cts.eventsphere.model.Vendor;
import com.cts.eventsphere.repository.VendorRepository;
import com.cts.eventsphere.service.VendorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.PrivateKey;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VendorServiceImpl implements VendorService {
    private final VendorRepository vendorRepository;
    private final VendorRequestDtoMapper requestDtoMapper;
    private final VendorResponseDtoMapper responseDtoMapper;

    @Override
    public VendorResponseDto createVendor(VendorRequestDto request){
        Vendor vendor = requestDtoMapper.toEntity(request);
        Vendor saved = vendorRepository.save(vendor);
        return responseDtoMapper.toDto(saved);
    }

    @Override
    public VendorResponseDto getVendorById(String vendorId){
        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new RuntimeException("Vendor not found"));
        return responseDtoMapper.toDto(vendor);
    }

    @Override
    public List<VendorResponseDto> getAllVendors(){
        return vendorRepository.findAll()
                .stream()
                .map(responseDtoMapper::toDto)
                .toList();
    }

    @Override
    public VendorResponseDto updateVendor(String vendorId, VendorRequestDto request){
        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new RuntimeException("Vendor not found"));
        vendor.setName(request.name());
        vendor.setContactInfo(request.contactInfo());
        vendor.setStatus(request.status());
        Vendor updated = vendorRepository.save(vendor);
        return responseDtoMapper.toDto(updated);
    }

    @Override
    public void deleteVendor(String vendorId){
        if(!vendorRepository.existsById(vendorId)){
            throw new RuntimeException("Vendor not found");
        }
        vendorRepository.deleteById(vendorId);
    }

}
