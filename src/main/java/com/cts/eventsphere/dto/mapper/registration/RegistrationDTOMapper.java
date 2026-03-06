package com.cts.eventsphere.dto.mapper.registration;

import com.cts.eventsphere.dto.registration.RegistrationDTO;
import com.cts.eventsphere.model.Registration;

public class RegistrationDTOMapper {
    public static RegistrationDTO toDTO(Registration registration) {
        return new RegistrationDTO(
                registration.getRegistrationId(),
                registration.getEventId(),
                registration.getTicketId(),
                registration.getAttendeeId(),
                registration.getStatus().name()
        );
    }

}
