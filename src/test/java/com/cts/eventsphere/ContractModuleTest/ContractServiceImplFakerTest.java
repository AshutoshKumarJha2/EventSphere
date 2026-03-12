package com.cts.eventsphere.ContractModuleTest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cts.eventsphere.service.impl.ContractServiceImpl;
import com.cts.eventsphere.repository.ContractRepository;
import com.cts.eventsphere.repository.InvoiceRepository;
import com.cts.eventsphere.repository.PaymentRepository;

import com.cts.eventsphere.model.Contract;
import com.cts.eventsphere.model.Invoice;
import com.cts.eventsphere.model.Payment;
import com.cts.eventsphere.dto.mapper.contract.ContractRequestDtoMapper;
import com.cts.eventsphere.dto.mapper.contract.ContractResponseDtoMapper;

import com.cts.eventsphere.exception.contract.ContractNotFoundException;

import com.github.javafaker.Faker;
import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContractServiceImplFakerTest {
    @Mock private ContractRepository contractRepository;
    @Mock private InvoiceRepository invoiceRepository;
    @Mock private PaymentRepository paymentRepository;
    @Mock private ContractRequestDtoMapper requestMapper;
    @Mock private ContractResponseDtoMapper responseMapper;

    @InjectMocks private ContractServiceImpl contractService;
    private Faker faker = new Faker();

    @Test
    void testProcessContractInvoice_Success() {
        String cId = faker.internet().uuid();
        Contract contract = new Contract();
        contract.setContractId(cId);
        contract.setValue(BigDecimal.valueOf(5000));

        when(contractRepository.findById(cId)).thenReturn(Optional.of(contract));
        when(invoiceRepository.save(any(Invoice.class))).thenAnswer(i -> i.getArgument(0));

        contractService.processContractInvoice(cId);

        verify(invoiceRepository, times(2)).save(any(Invoice.class));
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testGetContractById_NotFound() {
        String id = "invalid-id";
        when(contractRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(ContractNotFoundException.class, () -> contractService.getContractById(id));
    }
}
