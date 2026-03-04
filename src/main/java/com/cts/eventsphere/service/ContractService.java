package com.cts.eventsphere.service;

import com.cts.eventsphere.dto.contract.ContractRequestDto;
import com.cts.eventsphere.dto.contract.ContractResponseDto;

import java.util.List;

public interface ContractService {
    ContractResponseDto createContract(ContractRequestDto request);

    ContractResponseDto getContractById(String contractId);

    List<ContractResponseDto> getAllContracts();

    ContractResponseDto updateContract(String contractId, ContractRequestDto request);

    void deleteContract(String contractId);
}
