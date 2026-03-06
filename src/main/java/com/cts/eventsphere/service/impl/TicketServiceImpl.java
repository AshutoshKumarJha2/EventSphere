package com.cts.eventsphere.service.impl;

import com.cts.eventsphere.dto.mapper.ticket.TicketDTOMapper;
import com.cts.eventsphere.dto.shared.GenericResponse;
import com.cts.eventsphere.dto.ticket.TicketListResponseDTO;
import com.cts.eventsphere.dto.ticket.TicketResponseDTO;
import com.cts.eventsphere.exception.ticket.TicketNotFoundException;
import com.cts.eventsphere.model.Ticket;
import com.cts.eventsphere.model.data.TicketStatus;
import com.cts.eventsphere.repository.TicketRepository;
import com.cts.eventsphere.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Service
public class TicketServiceImpl implements TicketService {
    private TicketRepository ticketRepository;
    @Override
    public GenericResponse createTicket(String eventId, String type, double price, TicketStatus status) {
        var ticket = Ticket.builder()
                .eventId(eventId)
                .type(type)
                .price(BigDecimal.valueOf(price))
                .status(status)
                .build();

        ticketRepository.save(ticket);
        return new GenericResponse("Ticket created successfully");
    }


    @Override
    public TicketListResponseDTO getTicketsByEventId(String eventId, int page, int size) {
        var ticketsPage = ticketRepository.findByEventId(eventId, PageRequest.of(page, size));
        var tickets = ticketsPage.getContent().stream()
                .map(TicketDTOMapper::toDTO)
                .toList();
        var sizeOfPage = tickets.size();
        var pageNumber = ticketsPage.getNumber();
        var totalElements = ticketsPage.getTotalElements();
        var totalPages = ticketsPage.getTotalPages();
        return new TicketListResponseDTO(tickets, pageNumber, sizeOfPage, totalElements, totalPages);
    }

    @Override
    public TicketListResponseDTO getAllTickets(int page, int size) {
        var ticketsPage = ticketRepository.findAll(PageRequest.of(page, size));
        var tickets = ticketsPage.getContent().stream()
                .map(TicketDTOMapper::toDTO)
                .toList();
        var sizeOfPage = tickets.size();
        var pageNumber = ticketsPage.getNumber();
        var totalElements = ticketsPage.getTotalElements();
        var totalPages = ticketsPage.getTotalPages();
        return new TicketListResponseDTO(tickets, pageNumber, sizeOfPage, totalElements, totalPages);
    }

    @Override
    public TicketResponseDTO getTicketById(String ticketId) {
        var ticket = ticketRepository.findById(ticketId).orElseThrow(() -> new TicketNotFoundException("Ticket not found"));
        return TicketDTOMapper.toDTO(ticket);
    }

    @Override
    public GenericResponse updateTicket(String ticketId, String type, double price,TicketStatus status) {
        var ticket = ticketRepository.findById(ticketId).orElseThrow(() -> new TicketNotFoundException("Ticket not found"));

        ticket.setType(type);
        ticket.setPrice(BigDecimal.valueOf(price));
        ticket.setStatus(status);
        ticketRepository.save(ticket);
        return new GenericResponse("Ticket updated successfully");
    }

    @Override
    public GenericResponse deleteTicket(String ticketId) {
        if (!ticketRepository.existsById(ticketId)) {
            throw new TicketNotFoundException("Ticket not found");
        }
        ticketRepository.deleteById(ticketId);
        return new GenericResponse("Ticket deleted successfully");
    }
}
