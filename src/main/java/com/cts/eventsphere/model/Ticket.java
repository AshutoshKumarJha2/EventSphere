package com.cts.eventsphere.model;

import com.cts.eventsphere.model.data.TicketStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
@DynamicInsert
public class Ticket {
    @Id
    @UuidGenerator
    private String ticketId;

    @Column
    private String eventId;

    @Column
    private String type;

    @Column
    private BigDecimal price;

    @Column(columnDefinition = "ENUM('active','inactive')")
    @Enumerated(EnumType.STRING)
    private TicketStatus status;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
