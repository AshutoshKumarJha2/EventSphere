package com.cts.eventsphere.service.impl;

import com.cts.eventsphere.dto.delivery.DeliveryRequestDto;
import com.cts.eventsphere.dto.delivery.DeliveryResponseDto;
import com.cts.eventsphere.dto.mapper.delivery.DeliveryRequestDtoMapper;
import com.cts.eventsphere.dto.mapper.delivery.DeliveryResponseDtoMapper;
import com.cts.eventsphere.model.Delivery;
import com.cts.eventsphere.repository.DeliveryRepository;
import com.cts.eventsphere.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {
    private final DeliveryRepository deliveryRepository;
    private final DeliveryRequestDtoMapper requestDtoMapper;
    private final DeliveryResponseDtoMapper responseDtoMapper;

    @Override
    public DeliveryResponseDto createDelivery(DeliveryRequestDto request){
        Delivery delivery = requestDtoMapper.toEntity(request);
        Delivery saved = deliveryRepository.save(delivery);
        return responseDtoMapper.toDto(saved);
    }

    @Override
    public DeliveryResponseDto getDeliveryById(String deliveryId){
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new RuntimeException("Delivery not found"));
        return responseDtoMapper.toDto(delivery);
    }

    @Override
    public List<DeliveryResponseDto> getAllDeliveries(){
        return deliveryRepository.findAll()
                .stream()
                .map(responseDtoMapper::toDto)
                .toList();
    }

    @Override
    public DeliveryResponseDto updateDelivery(String deliveryId, DeliveryRequestDto request){
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new RuntimeException("Delivery not found"));
        delivery.setInvoiceId(request.invoiceId());
        delivery.setItem(request.item());
        delivery.setQuantity(request.quantity());
        delivery.setDeliveryDate(request.deliveryDate());
        delivery.setStatus(request.status());
        delivery.setTrackingNumber(request.trackingNumber());
        Delivery updated = deliveryRepository.save(delivery);
        return responseDtoMapper.toDto(updated);
    }

    @Override
    public void deleteDelivery(String deliveryId){
        if(!deliveryRepository.existsById(deliveryId)){
            throw new RuntimeException("Delivery not found");
        }
        deliveryRepository.deleteById(deliveryId);
    }
}
