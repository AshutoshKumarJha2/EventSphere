package com.cts.eventsphere.exception.ticket;

/**
 * Exception raised when ticket not found
 *
 * @author test-in-prod-10x
 * @version 1.0
 * @since 2026-03-05
 */
public class TicketNotFoundException  extends  RuntimeException{
    public TicketNotFoundException(String message){
        super(message);
    }
}
