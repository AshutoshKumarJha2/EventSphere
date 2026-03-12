package com.cts.eventsphere.controller;

import com.cts.eventsphere.dto.contract.ContractRequestDto;
import com.cts.eventsphere.dto.contract.ContractResponseDto;
import com.cts.eventsphere.model.data.ContractStatus;
import com.cts.eventsphere.service.ContractService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
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
@Validated
public class ContractController {

    private final ContractService contractService;

    @PostMapping
    @PreAuthorize("hasRole('ORGANIZER')")
    public @Valid ContractResponseDto create(@Valid @RequestBody ContractRequestDto request) {
        log.info("Received request to create contract: vendorId={}, eventId={}",
                request.vendorId(), request.eventId());
        ContractResponseDto response = contractService.createContract(request);
        log.info("Contract created successfully with ID={}", response.contractId());
        return response;
    }

    @PostMapping("/{contractId}/invoice")
    @PreAuthorize("hasAnyRole('FINANCE', 'VENDOR')")
    public String generateInvoice(@PathVariable String contractId) {
        log.info("Received request to process billing for contract: {}", contractId);
        contractService.processContractInvoice(contractId);
        return "Invoice generated and Payment processed successfully for Contract ID: " + contractId;
    }

    @PostMapping("/{contractId}/deliveries")
    @PreAuthorize("hasRole('VENDOR')")
    public String addDelivery(
            @PathVariable String contractId,
            @RequestParam @NotNull String item,
            @RequestParam @Min(1) Integer quantity) {
        contractService.addDeliverable(contractId, item, quantity);
        return "Deliverable added to contract.";
    }

    @GetMapping("/{id}")
    public @Valid ContractResponseDto getById(@PathVariable String id) {
        log.info("Fetching contract with ID={}", id);
        return contractService.getContractById(id);
    }

    @GetMapping
    public List<@Valid ContractResponseDto> getAll() {
        log.info("Fetching all contracts");
        return contractService.getAllContracts();
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ORGANIZER', 'VENDOR')")
    public @Valid ContractResponseDto updateStatus(
            @PathVariable String id,
            @RequestParam ContractStatus status) {

        log.info("Request to update status for contract ID={} to {}", id, status);
        return contractService.updateContractStatus(id, status);
    }

    @PutMapping("/{id}")
    public @Valid ContractResponseDto update(
            @PathVariable String id,
            @Valid @RequestBody ContractRequestDto request) {

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