package com.cts.eventsphere.controller;

import com.cts.eventsphere.dto.contract.ContractRequestDto;
import com.cts.eventsphere.dto.contract.ContractResponseDto;
import com.cts.eventsphere.service.ContractService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Rest Controller for Contract Entity
 *
 * @author 2480177
 * @version 1.0
 * @since 03-03-2026
 */

@RestController
@RequestMapping("/api/v1/contracts")
@RequiredArgsConstructor
@Slf4j
public class ContractController {

    private final ContractService contractService;

    @PostMapping
    public ContractResponseDto create(@RequestBody ContractRequestDto request) {
        log.info("Received request to create contract: vendorId={}, eventId={}",
                request.vendorId(), request.eventId());
        ContractResponseDto response = contractService.createContract(request);
        log.info("Contract created successfully with ID={}", response.contractId());
        return response;
    }

    @GetMapping("/{id}")
    public ContractResponseDto getById(@PathVariable String id) {
        log.info("Fetching contract with ID={}", id);
        return contractService.getContractById(id);
    }

    @GetMapping
    public List<ContractResponseDto> getAll() {
        log.info("Fetching all contracts");
        return contractService.getAllContracts();
    }

    @PutMapping("/{id}")
    public ContractResponseDto update(
            @PathVariable String id,
            @RequestBody ContractRequestDto request) {

        log.info("Updating contract with ID={}", id);
        ContractResponseDto response = contractService.updateContract(id, request);
        log.info("Contract updated successfully with ID={}", id);
        return response;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        log.warn("Request received to delete contract with ID={}", id);
        contractService.deleteContract(id);
        log.info("Contract deleted successfully with ID={}", id);
    }
}