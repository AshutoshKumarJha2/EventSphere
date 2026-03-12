package com.cts.eventsphere.service.impl;

import com.cts.eventsphere.dto.mapper.ticket.TicketDTOMapper;
import com.cts.eventsphere.dto.shared.GenericResponse;
import com.cts.eventsphere.dto.ticket.TicketListResponseDTO;
import com.cts.eventsphere.dto.ticket.TicketResponseDTO;
import com.cts.eventsphere.exception.ticket.TicketAlreadyExistsException;
import com.cts.eventsphere.exception.ticket.TicketNotFoundException;
import com.cts.eventsphere.model.Ticket;
import com.cts.eventsphere.model.data.TicketStatus;
import com.cts.eventsphere.repository.TicketRepository;
import com.cts.eventsphere.service.TicketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class TicketServiceImpl implements TicketService {
    private final TicketRepository ticketRepository;
    @Override
    public GenericResponse createTicket(String eventId, String type, double price, TicketStatus status) throws TicketAlreadyExistsException {
        var normalizedType = type.toLowerCase();
        var existingTicket = ticketRepository.findByType(normalizedType);
        if (existingTicket.isPresent()){
            throw new TicketAlreadyExistsException(String.format("Ticket type %s already exists", normalizedType));
        }
        var ticket = Ticket.builder()
                .eventId(eventId)
                .type(normalizedType)
                .price(BigDecimal.valueOf(price))
                .status(status)
                .build();

        ticketRepository.save(ticket);
        log.info("Ticket created with id: {}, for eventId: {}", ticket.getTicketId(), eventId);
        return new GenericResponse("Ticket created successfully");
    }


    @Override
    public TicketListResponseDTO getTicketsByEventId(String eventId, int page, int size) {
        log.info("Fetching tickets for eventId: {}, page: {}, size: {}", eventId, page, size);
        var ticketsPage = ticketRepository.findByEventId(eventId, PageRequest.of(page, size));
        var tickets = ticketsPage.getContent().stream()
                .map(TicketDTOMapper::toDTO)
                .toList();
        var sizeOfPage = tickets.size();
        var pageNumber = ticketsPage.getNumber();
        var totalElements = ticketsPage.getTotalElements();
        var totalPages = ticketsPage.getTotalPages();
        log.info("Fetched {} tickets for eventId: {}, page: {}, size: {}", sizeOfPage, eventId, page, size);
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
        log.info("Fetched {} tickets, page: {}, size: {}", sizeOfPage, page, size);
        return new TicketListResponseDTO(tickets, pageNumber, sizeOfPage, totalElements, totalPages);
    }

    @Override
    public TicketResponseDTO getTicketById(String ticketId) {
        var ticket = ticketRepository.findById(ticketId).orElseThrow(() -> new TicketNotFoundException("Ticket not found"));
        log.info("Fetched ticket with id: {}", ticketId);
        return TicketDTOMapper.toDTO(ticket);
    }

    @Override
    public GenericResponse updateTicket(String ticketId, String type, double price,TicketStatus status) {
        var ticket = ticketRepository.findById(ticketId).orElseThrow(() -> new TicketNotFoundException("Ticket not found"));

        ticket.setType(type);
        ticket.setPrice(BigDecimal.valueOf(price));
        ticket.setStatus(status);
        ticketRepository.save(ticket);
        log.info("Updated ticket with id: {}", ticketId);
        return new GenericResponse("Ticket updated successfully");
    }

    @Override
    public GenericResponse deleteTicket(String ticketId) {
        if (!ticketRepository.existsById(ticketId)) {
            throw new TicketNotFoundException("Ticket not found");
        }
        ticketRepository.deleteById(ticketId);
        log.info("Deleted ticket with id: {}", ticketId);
        return new GenericResponse("Ticket deleted successfully");
    }
}
