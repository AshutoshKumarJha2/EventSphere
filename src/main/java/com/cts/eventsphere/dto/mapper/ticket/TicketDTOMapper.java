package com.cts.eventsphere.dto.mapper.ticket;

import com.cts.eventsphere.dto.ticket.TicketResponseDTO;
import com.cts.eventsphere.model.Ticket;

public class TicketDTOMapper {
    public static TicketResponseDTO toDTO(Ticket ticket) {
        return new TicketResponseDTO(
                ticket.getTicketId(),
                ticket.getEventId(),
                ticket.getType(),
                ticket.getPrice().doubleValue(),
                ticket.getStatus()
        );
    }
}
