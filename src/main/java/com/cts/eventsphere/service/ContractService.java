package com.cts.eventsphere.service;

import com.cts.eventsphere.dto.contract.ContractRequestDto;
import com.cts.eventsphere.dto.contract.ContractResponseDto;
import com.cts.eventsphere.model.data.ContractStatus;

import java.util.List;

/**
 * Service interface for Contract Operations
 *
 * @author 2480177
 * @version 1.0
 * @since 03-03-2026
 */

public interface ContractService {
    ContractResponseDto createContract(ContractRequestDto request);

    void processContractInvoice(String contractId);

    void addDeliverable(String contractId, String item, Integer quantity);

    ContractResponseDto getContractById(String contractId);

    List<ContractResponseDto> getAllContracts();

    ContractResponseDto updateContractStatus(String contractId, ContractStatus status);

    ContractResponseDto updateContract(String contractId, ContractRequestDto request);

    void deleteContract(String contractId);
}
