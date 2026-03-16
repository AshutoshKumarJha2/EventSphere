package com.cts.eventsphere.model;

import com.cts.eventsphere.model.data.RegistrationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entity class for Registration table
 *
 * @author test-in-prod-10x
 * @version 1.0
 * @since 2026-03-02
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@DynamicInsert
public class Registration {
    @Id
    @UuidGenerator
    private String registrationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "eventId")
    private Event event;

    @ManyToOne
    @JoinColumn(name = "ticketId")
    Ticket ticket;

    @Column
    private  LocalDate date;

    @Column(columnDefinition = "ENUM('pending', 'confirmed', 'cancelled', 'checked_in')")
    @Enumerated(EnumType.STRING)
    RegistrationStatus status;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attendeeId", nullable = false)
    private User attendee;
}
