package com.cts.eventsphere.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import com.cts.eventsphere.model.data.TicketStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity for ticket data
 *
 * @author test-in-prod-10x
 * @version 1.0
 * @since 2026-02-27
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ticket {
    @Id
    @UuidGenerator
    private String ticketId;

    @Column
    private String eventId;

    @Column
    private String type;

    @Column
    private double price;

    @Column(columnDefinition = "ENUM('active','inactive')")
    @Enumerated(EnumType.STRING)
    private TicketStatus status;

    @CreateTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
