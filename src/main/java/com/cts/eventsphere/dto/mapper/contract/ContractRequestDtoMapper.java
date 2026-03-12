package com.cts.eventsphere.dto.mapper.contract;

import com.cts.eventsphere.dto.contract.ContractRequestDto;
import com.cts.eventsphere.model.Contract;
import org.springframework.stereotype.Component;

/**
 * Mapper for converting Contract entity to ContractRequestDto
 *
 * @author 2480177
 * @version 1.0
 * @since 02-03-2026
 */

@Component
public class ContractRequestDtoMapper {
    public Contract toEntity(ContractRequestDto dto){
        if (dto == null) {
            return null;
        }
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
