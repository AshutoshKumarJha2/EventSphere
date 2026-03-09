package com.cts.eventsphere.service.impl;

import com.cts.eventsphere.dto.delivery.DeliveryRequestDto;
import com.cts.eventsphere.dto.delivery.DeliveryResponseDto;
import com.cts.eventsphere.dto.mapper.delivery.DeliveryRequestDtoMapper;
import com.cts.eventsphere.dto.mapper.delivery.DeliveryResponseDtoMapper;
import com.cts.eventsphere.exception.delivery.DeliveryNotFoundException;
import com.cts.eventsphere.model.Delivery;
import com.cts.eventsphere.repository.DeliveryRepository;
import com.cts.eventsphere.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation for Delivery Service
 *
 * @author 2480177
 * @version 1.0
 * @since 03-03-2026
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class DeliveryServiceImpl implements DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final DeliveryRequestDtoMapper requestDtoMapper;
    private final DeliveryResponseDtoMapper responseDtoMapper;

    /**
     * @param request
     * @return
     */
    @Override
    public DeliveryResponseDto createDelivery(DeliveryRequestDto request){
        log.info("Creating delivery...");
        Delivery delivery = requestDtoMapper.toEntity(request);
        Delivery saved = deliveryRepository.save(delivery);
        log.info("Delivery created with ID={}", saved.getDeliveryId());
        return responseDtoMapper.toDto(saved);
    }

    /**
     * @param deliveryId
     * @return
     * @throws DeliveryNotFoundException
     */
    @Override
    public DeliveryResponseDto getDeliveryById(String deliveryId){
        log.info("Fetching delivery ID={}", deliveryId);

        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new DeliveryNotFoundException(deliveryId));

        return responseDtoMapper.toDto(delivery);
    }

    /**
     * @return
     */
    @Override
    public List<DeliveryResponseDto> getAllDeliveries(){
        log.info("Fetching all deliveries");
        return deliveryRepository.findAll()
                .stream()
                .map(responseDtoMapper::toDto)
                .toList();
    }

    /**
     * @param deliveryId
     * @param request
     * @return
     * @throws DeliveryNotFoundException
     */
    @Override
    public DeliveryResponseDto updateDelivery(String deliveryId, DeliveryRequestDto request){
        log.info("Updating delivery ID={}", deliveryId);

        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new DeliveryNotFoundException(deliveryId));

        delivery.setInvoiceId(request.invoiceId());
        delivery.setItem(request.item());
        delivery.setQuantity(request.quantity());
        delivery.setDeliveryDate(request.deliveryDate());
        delivery.setStatus(request.status());
        delivery.setTrackingNumber(request.trackingNumber());

        Delivery updated = deliveryRepository.save(delivery);
        return responseDtoMapper.toDto(updated);
    }

    /**
     * @param deliveryId
     * @throws DeliveryNotFoundException
     */
    @Override
    public void deleteDelivery(String deliveryId){
        log.warn("Deleting delivery ID={}", deliveryId);

        if(!deliveryRepository.existsById(deliveryId)){
            throw new DeliveryNotFoundException(deliveryId);
        }

        deliveryRepository.deleteById(deliveryId);
    }
}