package com.cts.eventsphere.controller;

import com.cts.eventsphere.dto.vendor.VendorRequestDto;
import com.cts.eventsphere.dto.vendor.VendorResponseDto;
import com.cts.eventsphere.service.VendorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Rest Controller for Vendor Entity
 *
 * @author 2480177
 * @version 1.0
 * @since 03-03-2026
 */

@RestController
@RequestMapping("/api/v1/vendors")
@Slf4j
@RequiredArgsConstructor
@Validated
public class VendorController {

    private final VendorService vendorService;

    @PostMapping
    @PreAuthorize("hasRole('VENDOR')")
    public @Valid VendorResponseDto create(@Valid @RequestBody VendorRequestDto request) {
        log.info("Creating vendor with name={}", request.name());
        VendorResponseDto response = vendorService.createVendor(request);
        log.info("Vendor created successfully with ID={}", response.vendorId());
        return response;
    }

    @GetMapping("/{id}")
    public @Valid VendorResponseDto getById(@PathVariable String id) {
        log.info("Fetching vendor with ID={}", id);
        return vendorService.getVendorById(id);
    }

    @GetMapping
    public List<@Valid VendorResponseDto> getAll() {
        log.info("Fetching all vendors");
        return vendorService.getAllVendors();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('VENDOR')")
    public @Valid VendorResponseDto update(
            @PathVariable String id,
            @Valid @RequestBody VendorRequestDto request) {

        log.info("Updating vendor with ID={}", id);
        VendorResponseDto response = vendorService.updateVendor(id, request);
        log.info("Vendor updated successfully with ID={}", id);
        return response;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('VENDOR','ADMIN')")
    public void delete(@PathVariable String id) {
        log.warn("Request to delete vendor with ID={}", id);
        vendorService.deleteVendor(id);
        log.info("Vendor deleted successfully with ID={}", id);
    }
}