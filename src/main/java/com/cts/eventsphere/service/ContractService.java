package com.cts.eventsphere.service;

import com.cts.eventsphere.dto.contract.ContractRequestDto;
import com.cts.eventsphere.dto.contract.ContractResponseDto;

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

    ContractResponseDto getContractById(String contractId);

    List<ContractResponseDto> getAllContracts();

    ContractResponseDto updateContract(String contractId, ContractRequestDto request);

    void deleteContract(String contractId);
}
