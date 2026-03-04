package com.cts.eventsphere.controller;

import com.cts.eventsphere.dto.vendor.VendorRequestDto;
import com.cts.eventsphere.dto.vendor.VendorResponseDto;
import com.cts.eventsphere.service.VendorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/vendors")
@Slf4j
@RequiredArgsConstructor
public class VendorController {

    private final VendorService vendorService;

    @PostMapping
    public VendorResponseDto create(@RequestBody VendorRequestDto request) {
        return vendorService.createVendor(request);
    }

    @GetMapping("/{id}")
    public VendorResponseDto getById(@PathVariable String id) {
        return vendorService.getVendorById(id);
    }

    @GetMapping
    public List<VendorResponseDto> getAll() {
        return vendorService.getAllVendors();
    }

    @PutMapping("/{id}")
    public VendorResponseDto update(
            @PathVariable String id,
            @RequestBody VendorRequestDto request) {
        return vendorService.updateVendor(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        vendorService.deleteVendor(id);
    }

}
