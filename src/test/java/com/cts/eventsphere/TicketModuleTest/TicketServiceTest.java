package com.cts.eventsphere.TicketModuleTest;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Optional;

import com.cts.eventsphere.service.AuditService;
import com.cts.eventsphere.service.impl.TicketServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cts.eventsphere.dto.shared.GenericResponse;
import com.cts.eventsphere.exception.ticket.TicketAlreadyExistsException;
import com.cts.eventsphere.exception.ticket.TicketNotFoundException;
import com.cts.eventsphere.model.Ticket;
import com.cts.eventsphere.model.data.TicketStatus;
import com.cts.eventsphere.repository.TicketRepository;
import com.github.javafaker.Faker;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private AuditService auditService;

    @InjectMocks
    private TicketServiceImpl ticketService;

    private Faker faker;
    private Ticket mockTicket;
    private String actorId;
    private String eventId;
    private String ticketId;

    @BeforeEach
    void setUp() {
        faker = new Faker();
        actorId = faker.internet().uuid();
        eventId = faker.internet().uuid();
        ticketId = faker.internet().uuid();

        mockTicket = Ticket.builder()
                .ticketId(ticketId)
                .eventId(eventId)
                .type("vip")
                .price(BigDecimal.valueOf(150.0))
                .status(TicketStatus.active)
                .build();
    }

    @Test
    void createTicket_Success() {
        when(ticketRepository.findByEventIdAndType(eventId, "vip")).thenReturn(Optional.empty());
        when(ticketRepository.save(any(Ticket.class))).thenReturn(mockTicket);

        GenericResponse response = ticketService.createTicket(actorId, eventId, "VIP", 150.0, TicketStatus.active);

        assertEquals("Ticket created successfully", response.message());
        verify(ticketRepository, times(1)).save(any(Ticket.class));
    }

    @Test
    void createTicket_ThrowsAlreadyExistsException() {
        when(ticketRepository.findByEventIdAndType(eventId, "vip")).thenReturn(Optional.of(mockTicket));

        assertThrows(
                TicketAlreadyExistsException.class,
                () -> ticketService.createTicket(actorId, eventId, "VIP", 150.0, TicketStatus.active)
        );
        verify(ticketRepository, never()).save(any(Ticket.class));
    }

    @Test
    void updateTicket_Success() {
        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(mockTicket));
        when(ticketRepository.save(any(Ticket.class))).thenReturn(mockTicket);

        GenericResponse response = ticketService.updateTicket(actorId, ticketId, "early_bird", 99.99, TicketStatus.inactive);

        assertEquals("Ticket updated successfully", response.message());
        assertEquals("early_bird", mockTicket.getType());
        assertEquals(BigDecimal.valueOf(99.99), mockTicket.getPrice());
        assertEquals(TicketStatus.inactive, mockTicket.getStatus());
        verify(ticketRepository, times(1)).save(mockTicket);
    }

    @Test
    void deleteTicket_Success() {
        when(ticketRepository.existsById(ticketId)).thenReturn(true);

        GenericResponse response = ticketService.deleteTicket(actorId, ticketId);

        assertEquals("Ticket deleted successfully", response.message());
        verify(ticketRepository, times(1)).deleteById(ticketId);
    }

    @Test
    void deleteTicket_ThrowsNotFoundException() {
        when(ticketRepository.existsById(ticketId)).thenReturn(false);

        assertThrows(
                TicketNotFoundException.class,
                () -> ticketService.deleteTicket(actorId, ticketId)
        );
        verify(ticketRepository, never()).deleteById(anyString());
    }
}
