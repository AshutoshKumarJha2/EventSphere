package com.cts.eventsphere.controller;

import com.cts.eventsphere.dto.contract.ContractRequestDto;
import com.cts.eventsphere.dto.contract.ContractResponseDto;
import com.cts.eventsphere.service.ContractService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/contracts")
@RequiredArgsConstructor
@Slf4j
public class ContractController {

    private final ContractService contractService;

    @PostMapping
    public ContractResponseDto create(@RequestBody ContractRequestDto request) {
        log.info("Creating contract");
        return contractService.createContract(request);
    }

    @GetMapping("/{id}")
    public ContractResponseDto getById(@PathVariable String id) {
        log.info("Fetching contract with id: {}", id);
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

        log.info("Updating contract with id: {}", id);
        return contractService.updateContract(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        log.info("Deleting contract with id: {}", id);
        contractService.deleteContract(id);
    }
}
