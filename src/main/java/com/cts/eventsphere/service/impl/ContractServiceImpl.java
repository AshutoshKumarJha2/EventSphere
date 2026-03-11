package com.cts.eventsphere.service.impl;

import com.cts.eventsphere.dto.contract.ContractRequestDto;
import com.cts.eventsphere.dto.contract.ContractResponseDto;
import com.cts.eventsphere.dto.mapper.contract.ContractRequestDtoMapper;
import com.cts.eventsphere.dto.mapper.contract.ContractResponseDtoMapper;
import com.cts.eventsphere.exception.contract.ContractNotFoundException;
import com.cts.eventsphere.model.Contract;
import com.cts.eventsphere.model.Delivery;
import com.cts.eventsphere.model.Invoice;
import com.cts.eventsphere.model.Payment;
import com.cts.eventsphere.model.data.*;
import com.cts.eventsphere.repository.ContractRepository;
import com.cts.eventsphere.repository.DeliveryRepository;
import com.cts.eventsphere.repository.InvoiceRepository;
import com.cts.eventsphere.repository.PaymentRepository;
import com.cts.eventsphere.service.ContractService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    private final PaymentRepository paymentRepository;
    private final InvoiceRepository invoiceRepository;
    private final DeliveryRepository deliveryRepository;
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
    public void processContractInvoice(String contractId) {
        log.info("Starting invoice generation for contract: {}", contractId);

        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new ContractNotFoundException(contractId));

        Invoice invoice = new Invoice();
        invoice.setContractId(contract.getContractId());
        invoice.setTotalAmount(contract.getValue());
        invoice.setIssueDate(LocalDateTime.now());
        invoice.setDueDate(LocalDateTime.now().plusDays(30));
        invoice.setStatus(InvoiceStatus.issued);

        Invoice savedInvoice = invoiceRepository.save(invoice);

        Payment payment = new Payment();
        payment.setInvoice(savedInvoice);
        payment.setAmount(contract.getValue());
        payment.setMethod(PaymentMethod.bank_transfer);
        payment.setStatus(PaymentStatus.completed);
        payment.setPaymentDate(LocalDateTime.now());

        paymentRepository.save(payment);

        savedInvoice.setStatus(InvoiceStatus.paid);
        invoiceRepository.save(savedInvoice);

        log.info("Successfully generated paid invoice {} for contract {}",
                savedInvoice.getInvoiceId(), contractId);
    }

    /**
     * @param contractId
     * @param item
     * @param quantity
     * @return
     * @throws ContractNotFoundException
     */
    @Override
    public void addDeliverable(String contractId, String item, Integer quantity) {
        log.info("Adding deliverable to Contract: {}", contractId);

        Invoice invoice = invoiceRepository.findAll().stream()
                .filter(i -> i.getContractId().equals(contractId))
                .findFirst()
                .orElseThrow(() -> new ContractNotFoundException(contractId));

        Delivery delivery = new Delivery();
        delivery.setInvoice(invoice);
        delivery.setInvoiceId(invoice.getInvoiceId());
        delivery.setItem(item);
        delivery.setQuantity(quantity);
        delivery.setStatus(DeliveryStatus.scheduled);

        deliveryRepository.save(delivery);
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
     * @param status
     * @return
     * @throws ContractNotFoundException
     */
    @Override
    public ContractResponseDto updateContractStatus(String contractId, ContractStatus status) {
        log.info("Updating status for contract ID={} to {}", contractId, status);

        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new ContractNotFoundException(contractId));

        contract.setStatus(status);

        Contract updated = contractRepository.save(contract);
        return responseDtoMapper.toDto(updated);
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