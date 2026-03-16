package com.cts.eventsphere.service.impl;

import com.cts.eventsphere.dto.contract.ContractRequestDto;
import com.cts.eventsphere.dto.contract.ContractResponseDto;
import com.cts.eventsphere.dto.delivery.DeliveryRequestDto;
import com.cts.eventsphere.dto.invoice.InvoiceRequestDto;
import com.cts.eventsphere.dto.invoice.InvoiceResponseDto;
import com.cts.eventsphere.dto.mapper.contract.ContractRequestDtoMapper;
import com.cts.eventsphere.dto.mapper.contract.ContractResponseDtoMapper;
import com.cts.eventsphere.exception.contract.ContractNotFoundException;
import com.cts.eventsphere.model.Contract;
import com.cts.eventsphere.model.data.ContractStatus;
import com.cts.eventsphere.repository.ContractRepository;
import com.cts.eventsphere.service.ContractService;
import com.cts.eventsphere.service.DeliveryService;
import com.cts.eventsphere.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service Implementation for Contract operations.
 * Handles business logic for contract lifecycle management including creation,
 * status transitions, and integration with delivery and billing services.
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

    private final DeliveryService deliveryService;
    private final InvoiceService invoiceService;

    /**
     * Creates a new contract and persists it to the database.
     * Uses mapper to convert incoming DTO to a persistence entity.
     *
     * @param request the contract details provided by the organizer
     * @return the created contract details as a response DTO
     */
    @Override
    @Transactional
    public ContractResponseDto createContract(ContractRequestDto request) {
        log.info("Attempting to create a new contract for vendor: {}", request.vendorId());
        Contract saved = contractRepository.save(requestDtoMapper.toEntity(request));
        log.info("Successfully created contract with ID: {}", saved.getContractId());
        return responseDtoMapper.toDto(saved);
    }

    /**
     * Adds a deliverable item to an existing contract.
     * Validates contract existence before delegating to DeliveryService.
     *
     * @param contractId the unique identifier of the contract
     * @param dto the delivery details to be recorded
     * @throws ContractNotFoundException if the contract ID does not exist
     */
    @Override
    @Transactional
    public void addDeliverable(String contractId, DeliveryRequestDto dto) {
        validateContract(contractId);
        deliveryService.createDelivery(dto);
    }

    /**
     * Generates an invoice for an existing contract.
     * Validates contract existence before delegating to InvoiceService.
     *
     * @param contractId the unique identifier of the contract
     * @param dto the billing details for the invoice
     * @return the generated invoice response DTO
     * @throws ContractNotFoundException if the contract ID does not exist
     */
    @Override
    @Transactional
    public InvoiceResponseDto createInvoice(String contractId, InvoiceRequestDto dto) {
        validateContract(contractId);
        return invoiceService.generateInvoice(contractId, dto);
    }

    /**
     * Retrieves a specific contract by its unique identifier.
     *
     * @param contractId the ID of the contract to retrieve
     * @return the contract details as a response DTO
     * @throws ContractNotFoundException if the ID is not found in the database
     */
    @Override
    @Transactional(readOnly = true)
    public ContractResponseDto getContractById(String contractId) {
        log.info("Fetching contract details for ID={}", contractId);

        return contractRepository.findById(contractId)
                .map(responseDtoMapper::toDto)
                .orElseThrow(() -> new ContractNotFoundException("Contract not found for ID: " + contractId));
    }

    /**
     * Retrieves all contracts currently stored in the system.
     *
     * @return a list of all contract response DTOs
     */
    @Override
    @Transactional(readOnly = true)
    public List<ContractResponseDto> getAllContracts() {
        log.info("Fetching all contracts from database");
        return contractRepository.findAll().stream()
                .map(responseDtoMapper::toDto)
                .toList();
    }

    /**
     * Updates the status of an existing contract record.
     *
     * @param contractId the unique identifier of the contract
     * @param status the new status to be applied
     * @return the updated contract details
     * @throws ContractNotFoundException if the contract record does not exist
     */
    @Override
    @Transactional
    public ContractResponseDto updateContractStatus(String contractId, ContractStatus status) {
        log.info("Attempting to update status for contract ID: {} to {}", contractId, status);

        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new ContractNotFoundException("Contract not found for ID: " + contractId));

        contract.setStatus(status);

        log.info("Successfully updated contract status for ID: {}", contractId);
        return responseDtoMapper.toDto(contractRepository.save(contract));
    }

    /**
     * Updates the comprehensive details of an existing contract record.
     * Overwrites duration and value based on the provided request.
     *
     * @param contractId the unique identifier of the contract to update
     * @param request the new contract details provided via DTO
     * @return the updated contract details as a response DTO
     * @throws ContractNotFoundException if the contract record does not exist in the database
     */
    @Override
    @Transactional
    public ContractResponseDto updateContract(String contractId, ContractRequestDto request) {
        log.info("Attempting to update details for contract ID: {}", contractId);

        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new ContractNotFoundException("Contract not found for ID: " + contractId));

        contract.setStartDate(request.startDate());
        contract.setEndDate(request.endDate());
        contract.setValue(request.value());

        log.info("Successfully updated contract details for ID: {}", contractId);
        return responseDtoMapper.toDto(contractRepository.save(contract));
    }

    /**
     * Removes a contract record from the system based on the provided ID.
     *
     * @param contractId the unique identifier of the contract to delete
     * @throws ContractNotFoundException if the contract is not found
     */
    @Override
    @Transactional
    public void deleteContract(String contractId) {
        log.info("Attempting to delete contract ID: {}", contractId);

        if (!contractRepository.existsById(contractId)) {
            throw new ContractNotFoundException("Contract not found for ID :" + contractId);
        }

        contractRepository.deleteById(contractId);
        log.info("Successfully deleted contract ID: {}", contractId);
    }

    /**
     * Internal helper method to validate the existence of a contract.
     *
     * @param contractId the unique identifier to check
     * @throws ContractNotFoundException if the contract does not exist
     */
    private void validateContract(String contractId) {
        if (!contractRepository.existsById(contractId)) {
            log.error("Validation failed: Contract {} not found", contractId);
            throw new ContractNotFoundException("Contract not found with ID: " + contractId);
        }
    }
}