package com.cts.eventsphere.exception.venue;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class VenueNotFoundException extends RuntimeException {
    public VenueNotFoundException(String msg) {
        super(msg);
        log.error("VenueNotFoundException - {}", msg);
    }
}
