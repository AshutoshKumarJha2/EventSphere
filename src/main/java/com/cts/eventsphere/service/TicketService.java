package com.cts.eventsphere.service;

import com.cts.eventsphere.dto.shared.GenericResponse;
import com.cts.eventsphere.dto.ticket.TicketListResponseDTO;
import com.cts.eventsphere.dto.ticket.TicketResponseDTO;
import com.cts.eventsphere.model.data.TicketStatus;

public interface TicketService {
    GenericResponse createTicket(String eventId, String type, double price, TicketStatus status);
    TicketListResponseDTO getTicketsByEventId(String eventId, int page, int size);
    TicketListResponseDTO getAllTickets(int page, int size);
    TicketResponseDTO getTicketById(String ticketId);
    GenericResponse updateTicket(String ticketId, String type, double price, TicketStatus status);
    GenericResponse deleteTicket(String ticketId);
}
