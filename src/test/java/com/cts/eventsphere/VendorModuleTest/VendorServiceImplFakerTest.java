package com.cts.eventsphere.VendorModuleTest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cts.eventsphere.service.impl.VendorServiceImpl;
import com.cts.eventsphere.repository.VendorRepository;

import com.cts.eventsphere.model.Vendor;
import com.cts.eventsphere.dto.vendor.VendorResponseDto;
import com.cts.eventsphere.dto.mapper.vendor.VendorResponseDtoMapper;

import com.github.javafaker.Faker;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VendorServiceImplFakerTest {
    @Mock private VendorRepository vendorRepository;
    @Mock private VendorResponseDtoMapper responseMapper;

    @InjectMocks private VendorServiceImpl vendorService;
    private Faker faker = new Faker();

    @Test
    void testGetAllVendors() {
        Vendor vendor = new Vendor();
        when(vendorRepository.findAll()).thenReturn(List.of(vendor));
        when(responseMapper.toDto(any())).thenReturn(mock(VendorResponseDto.class));

        List<VendorResponseDto> result = vendorService.getAllVendors();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }
}