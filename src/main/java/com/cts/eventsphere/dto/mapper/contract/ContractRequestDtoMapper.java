package com.cts.eventsphere.dto.mapper.contract;

import com.cts.eventsphere.dto.contract.ContractRequestDto;
import com.cts.eventsphere.model.Contract;
import org.springframework.stereotype.Component;

@Component
public class ContractRequestDtoMapper {
    public Contract toEntity(ContractRequestDto dto){
        Contract contract = new Contract();
        contract.setVendorId(dto.vendorId());
        contract.setEventId(dto.eventId());
        contract.setStartDate(dto.startDate());
        contract.setEndDate(dto.endDate());
        contract.setValue(dto.value());
        contract.setStatus(dto.status());
        return contract;
    }
}
