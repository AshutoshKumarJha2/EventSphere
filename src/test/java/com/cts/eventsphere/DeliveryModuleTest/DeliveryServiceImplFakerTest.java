package com.cts.eventsphere.DeliveryModuleTest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cts.eventsphere.service.impl.DeliveryServiceImpl;
import com.cts.eventsphere.repository.DeliveryRepository;

import com.cts.eventsphere.model.Delivery;
import com.cts.eventsphere.dto.delivery.DeliveryResponseDto;
import com.cts.eventsphere.dto.mapper.delivery.DeliveryRequestDtoMapper;
import com.cts.eventsphere.dto.mapper.delivery.DeliveryResponseDtoMapper;
import com.cts.eventsphere.model.data.DeliveryStatus;

import com.github.javafaker.Faker;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeliveryServiceImplFakerTest {
    @Mock private DeliveryRepository deliveryRepository;
    @Mock private DeliveryRequestDtoMapper requestMapper;
    @Mock private DeliveryResponseDtoMapper responseMapper;

    @InjectMocks private DeliveryServiceImpl deliveryService;
    private Faker faker = new Faker();

    @Test
    void testUpdateDeliveryStatus_Success() {
        String dId = faker.internet().uuid();
        Delivery delivery = new Delivery();
        delivery.setDeliveryId(dId);

        when(deliveryRepository.findById(dId)).thenReturn(Optional.of(delivery));
        when(deliveryRepository.save(any())).thenReturn(delivery);
        when(responseMapper.toDto(any())).thenReturn(new DeliveryResponseDto(dId, "INV1", "Item", 1, LocalDateTime.now(), DeliveryStatus.delivered, "TRK", LocalDateTime.now(), LocalDateTime.now()));

        DeliveryResponseDto result = deliveryService.updateDeliveryStatus(dId, DeliveryStatus.delivered);

        assertEquals(DeliveryStatus.delivered, result.status());
        verify(deliveryRepository).save(delivery);
    }
}
