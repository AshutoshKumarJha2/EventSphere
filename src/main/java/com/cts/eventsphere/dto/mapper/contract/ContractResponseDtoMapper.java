package com.cts.eventsphere.dto.mapper.contract;

import com.cts.eventsphere.dto.contract.ContractResponseDto;
import com.cts.eventsphere.model.Contract;
import org.springframework.stereotype.Component;

/**
 * Mapper for converting Contract entity to ContractResponseDto
 *
 * @author 2480177
 * @version 1.0
 * @since 02-03-2026
 */

@Component
public class ContractResponseDtoMapper {
    public ContractResponseDto toDto(Contract contract){
        return new ContractResponseDto(
                contract.getContractId(),
                contract.getVendorId(),
                contract.getEventId(),
                contract.getStartDate(),
                contract.getEndDate(),
                contract.getValue(),
                contract.getStatus(),
                contract.getCreatedAt(),
                contract.getUpdatedAt()
        );
    }
}
