package com.cts.eventsphere.service.impl;

import com.cts.eventsphere.dto.contract.ContractRequestDto;
import com.cts.eventsphere.dto.contract.ContractResponseDto;
import com.cts.eventsphere.dto.mapper.contract.ContractRequestDtoMapper;
import com.cts.eventsphere.dto.mapper.contract.ContractResponseDtoMapper;
import com.cts.eventsphere.model.Contract;
import com.cts.eventsphere.repository.ContractRepository;
import com.cts.eventsphere.service.ContractService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContractServiceImpl implements ContractService {

    private final ContractRepository contractRepository;
    private final ContractRequestDtoMapper requestDtoMapper;
    private final ContractResponseDtoMapper responseDtoMapper;

    @Override
    public ContractResponseDto createContract(ContractRequestDto request){
        Contract contract = requestDtoMapper.toEntity(request);
        Contract saved = contractRepository.save(contract);
        return responseDtoMapper.toDto(saved);
    }

    @Override
    public ContractResponseDto getContractById(String contractId){
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new RuntimeException("Contract not found"));
        return responseDtoMapper.toDto(contract);
    }

    @Override
    public List<ContractResponseDto> getAllContracts(){
        return contractRepository.findAll()
                .stream()
                .map(responseDtoMapper::toDto)
                .toList();
    }

    @Override
    public ContractResponseDto updateContract(String contractId, ContractRequestDto request){
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new RuntimeException("Contract not found"));
        contract.setVendorId(request.vendorId());
        contract.setEventId(request.eventId());
        contract.setStartDate(request.startDate());
        contract.setEndDate(request.endDate());
        contract.setValue(request.value());
        contract.setStatus(request.status());
        Contract updated = contractRepository.save(contract);
        return responseDtoMapper.toDto(updated);
    }

    @Override
    public void deleteContract(String contractId){
        if(!contractRepository.existsById(contractId)){
            throw new RuntimeException("Contract not found");
        }
        contractRepository.deleteById(contractId);
    }
}
