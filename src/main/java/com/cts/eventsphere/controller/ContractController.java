package com.cts.eventsphere.controller;

import com.cts.eventsphere.dto.contract.ContractRequestDto;
import com.cts.eventsphere.dto.contract.ContractResponseDto;
import com.cts.eventsphere.dto.delivery.DeliveryRequestDto;
import com.cts.eventsphere.dto.invoice.InvoiceRequestDto;
import com.cts.eventsphere.dto.invoice.InvoiceResponseDto;
import com.cts.eventsphere.exception.contract.ContractNotFoundException;
import com.cts.eventsphere.model.data.ContractStatus;
import com.cts.eventsphere.service.ContractService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Rest Controller for Contract entity management.
 * Provides endpoints for creating, updating, and processing contract-related tasks. [cite: 34]
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

    /**
     * Initiates a new contract record in the system. [cite: 51]
     * Restricted to users with the 'ORGANIZER' role.
     *
     * @param request the contract details to be created, validated by @Valid [cite: 54]
     * @return the created contract response DTO wrapped in a ResponseEntity [cite: 55]
     */
    @PostMapping
    @PreAuthorize("hasRole('ORGANIZER')")
    public ResponseEntity<ContractResponseDto> create(@Valid @RequestBody ContractRequestDto request) {
        log.info("REST request to create contract for vendor: {}",request.vendorId());
        return new ResponseEntity<>(contractService.createContract(request), HttpStatus.CREATED);
    }

    /**
     * Creates a new invoice associated with a specific contract. [cite: 51]
     *
     * @param contractId unique identifier of the contract [cite: 54]
     * @param dto the invoice details to be processed [cite: 54]
     * @return the generated invoice details [cite: 55]
     */
    @PostMapping("/{contractId}/invoice")
    public ResponseEntity<InvoiceResponseDto> createInvoice(@PathVariable String contractId, @Valid @RequestBody InvoiceRequestDto dto) {
        return new ResponseEntity<>(contractService.createInvoice(contractId, dto), HttpStatus.CREATED);
    }

    /**
     * Adds a new deliverable item to an existing contract. [cite: 51]
     *
     * @param contractId unique identifier of the contract [cite: 54]
     * @param dto the delivery details to be added [cite: 54]
     * @return a success message confirming the delivery addition [cite: 55]
     */
    @PostMapping("/{contractId}/deliveries")
    public ResponseEntity<String> addDelivery(@PathVariable String contractId, @Valid @RequestBody DeliveryRequestDto dto) {
        contractService.addDeliverable(contractId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Delivery Added");
    }

    /**
     * Retrieves a single contract by its unique ID. [cite: 51]
     *
     * @param id the unique identifier of the contract [cite: 54]
     * @return the contract details wrapped in a ResponseEntity [cite: 55]
     */
    @GetMapping("/{id}")
    public ResponseEntity<ContractResponseDto> getById(@PathVariable String id) {
        log.info("REST request to fetch contract with ID={}", id);
        return ResponseEntity.ok(contractService.getContractById(id));
    }

    /**
     * Fetches a list of all contracts available in the system. [cite: 51]
     *
     * @return a list of all contract response DTOs [cite: 55]
     */
    @GetMapping
    public ResponseEntity<List<ContractResponseDto>> getAll() {
        log.info("REST request to fetch all contracts");
        return ResponseEntity.ok(contractService.getAllContracts());
    }

    /**
     * Updates only the status of an existing contract. [cite: 51]
     *
     * @param id the unique identifier of the contract [cite: 54]
     * @param status the new status to apply [cite: 54]
     * @return the updated contract details [cite: 55]
     */
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ORGANIZER', 'VENDOR')")
    public ResponseEntity<ContractResponseDto> updateStatus(
            @PathVariable String id,
            @RequestParam ContractStatus status) {

        log.info("REST request to update status for contract ID={} to {}", id, status);
        return ResponseEntity.ok(contractService.updateContractStatus(id, status));
    }

    /**
     * Performs a full update of an existing contract's information. [cite: 51]
     * Overwrites vendor, event, duration, and financial details. [cite: 52]
     *
     * @param id the unique identifier of the contract to update [cite: 54]
     * @param request the updated contract details wrapped in a DTO [cite: 54]
     * @return the updated contract response DTO wrapped in a ResponseEntity [cite: 55]
     * @throws ContractNotFoundException if the contract ID is not found [cite: 56]
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ORGANIZER')")
    public ResponseEntity<ContractResponseDto> update(
            @PathVariable String id,
            @Valid @RequestBody ContractRequestDto request){

        log.info("REST request to update contract with ID={}", id);
        return ResponseEntity.ok(contractService.updateContract(id, request));
    }

    /**
     * Deletes a contract record from the system permanently. [cite: 51]
     * Restricted to Admins.
     *
     * @param id the unique identifier of the contract to remove [cite: 54]
     * @return ResponseEntity with No Content status [cite: 55]
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        log.info("REST request to delete contract with ID={}", id);
        contractService.deleteContract(id);
        return ResponseEntity.noContent().build();
    }
}