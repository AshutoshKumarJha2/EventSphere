package com.cts.eventsphere.service.impl;

import java.math.BigDecimal;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

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

/**
 * Service implementation for managing tickets.
 * Handles business logic for creating, retrieving, updating, and deleting tickets.
 * * @author test-in-prod-10x
 * @version 1.0
 * @since 2026-03-08
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TicketServiceImpl implements TicketService {
    private final TicketRepository ticketRepository;

    /**
     * Creates a new ticket for a specific event.
     *
     * @param eventId The ID of the event the ticket belongs to.
     * @param type    The category of the ticket (e.g., VIP, General). Case-insensitive.
     * @param price   The cost of the ticket.
     * @param status  The initial availability status of the ticket.
     * @return A {@link GenericResponse} indicating success.
     * @throws TicketAlreadyExistsException if a ticket with the same type already exists.
     */
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


    /**
     * Retrieves a paginated list of tickets for a specific event.
     *
     * @param eventId The ID of the event.
     * @param page    The page index (zero-based).
     * @param size    The number of items per page.
     * @return A {@link TicketListResponseDTO} containing ticket data and pagination metadata.
     */
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

    /**
     * Retrieves a paginated list of all tickets across all events.
     *
     * @param page The page index (zero-based).
     * @param size The number of items per page.
     * @return A {@link TicketListResponseDTO} containing all ticket records.
     */
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

    /**
     * Finds a single ticket by its unique identifier.
     *
     * @param ticketId The unique ID of the ticket.
     * @return The {@link TicketResponseDTO} representing the ticket details.
     * @throws TicketNotFoundException if no ticket is found with the given ID.
     */
    @Override
    public TicketResponseDTO getTicketById(String ticketId) {
        var ticket = ticketRepository.findById(ticketId).orElseThrow(() -> new TicketNotFoundException("Ticket not found"));
        log.info("Fetched ticket with id: {}", ticketId);
        return TicketDTOMapper.toDTO(ticket);
    }

    /**
     * Updates the details of an existing ticket.
     *
     * @param ticketId The ID of the ticket to update.
     * @param type     The new type/category for the ticket.
     * @param price    The updated price.
     * @param status   The updated availability status.
     * @return A {@link GenericResponse} indicating successful update.
     * @throws TicketNotFoundException if the ticket ID does not exist.
     */
    @Override
    public GenericResponse updateTicket(String ticketId, String type, double price, TicketStatus status) {
        var ticket = ticketRepository.findById(ticketId).orElseThrow(() -> new TicketNotFoundException("Ticket not found"));

        ticket.setType(type);
        ticket.setPrice(BigDecimal.valueOf(price));
        ticket.setStatus(status);
        ticketRepository.save(ticket);
        log.info("Updated ticket with id: {}", ticketId);
        return new GenericResponse("Ticket updated successfully");
    }

    /**
     * Deletes a ticket record from the database.
     *
     * @param ticketId The unique ID of the ticket to delete.
     * @return A {@link GenericResponse} indicating successful deletion.
     * @throws TicketNotFoundException if the ticket ID does not exist.
     */
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