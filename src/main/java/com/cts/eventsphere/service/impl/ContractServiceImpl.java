package com.cts.eventsphere.service.impl;

import com.cts.eventsphere.dto.contract.ContractRequestDto;
import com.cts.eventsphere.dto.contract.ContractResponseDto;
import com.cts.eventsphere.dto.mapper.contract.ContractRequestDtoMapper;
import com.cts.eventsphere.dto.mapper.contract.ContractResponseDtoMapper;
import com.cts.eventsphere.exception.contract.ContractNotFoundException;
import com.cts.eventsphere.model.Contract;
import com.cts.eventsphere.repository.ContractRepository;
import com.cts.eventsphere.service.ContractService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation for Contract Service
 *
 * @author 2480177
 * @version 1.0
 * @since 03-03-2026
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class ContractServiceImpl implements ContractService {

    private final ContractRepository contractRepository;
    private final ContractRequestDtoMapper requestDtoMapper;
    private final ContractResponseDtoMapper responseDtoMapper;

    /**
     * @param request
     * @return
     */
    @Override
    public ContractResponseDto createContract(ContractRequestDto request){
        log.info("Creating contract...");
        Contract contract = requestDtoMapper.toEntity(request);
        Contract saved = contractRepository.save(contract);
        log.info("Contract created with ID={}", saved.getContractId());
        return responseDtoMapper.toDto(saved);
    }

    /**
     * @param contractId
     * @return
     * @throws ContractNotFoundException
     */
    @Override
    public ContractResponseDto getContractById(String contractId){
        log.info("Fetching contract with ID={}", contractId);

        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new ContractNotFoundException(contractId));

        return responseDtoMapper.toDto(contract);
    }

    /**
     * @return
     */
    @Override
    public List<ContractResponseDto> getAllContracts(){
        log.info("Fetching all contracts");
        return contractRepository.findAll()
                .stream()
                .map(responseDtoMapper::toDto)
                .toList();
    }

    /**
     * @param contractId
     * @param request
     * @return
     * @throws ContractNotFoundException
     */
    @Override
    public ContractResponseDto updateContract(String contractId, ContractRequestDto request){
        log.info("Updating contract ID={}", contractId);

        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new ContractNotFoundException(contractId));

        contract.setVendorId(request.vendorId());
        contract.setEventId(request.eventId());
        contract.setStartDate(request.startDate());
        contract.setEndDate(request.endDate());
        contract.setValue(request.value());
        contract.setStatus(request.status());

        Contract updated = contractRepository.save(contract);
        return responseDtoMapper.toDto(updated);
    }

    /**
     * @param contractId
     * @throws ContractNotFoundException
     */
    @Override
    public void deleteContract(String contractId){
        log.warn("Deleting contract ID={}", contractId);

        if(!contractRepository.existsById(contractId)){
            throw new ContractNotFoundException(contractId);
        }

        contractRepository.deleteById(contractId);
    }
}