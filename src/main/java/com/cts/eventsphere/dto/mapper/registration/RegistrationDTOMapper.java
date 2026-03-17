package com.cts.eventsphere.dto.mapper.registration;

import com.cts.eventsphere.dto.registration.RegistrationDTO;
import com.cts.eventsphere.model.Registration;

public class RegistrationDTOMapper {
    public static RegistrationDTO toDTO(Registration registration) {
        return new RegistrationDTO(
                registration.getRegistrationId(),
                registration.getEvent().getEventId(),
                registration.getTicket().getTicketId(),
                registration.getAttendee().getUserId(),
                registration.getAttendee().getName(),
                registration.getAttendee().getEmail(),
                registration.getAttendee().getPhone(),
                registration.getStatus().name()
        );
    }

}
