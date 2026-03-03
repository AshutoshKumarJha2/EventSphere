package com.cts.eventsphere.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import com.cts.eventsphere.model.data.RegistrationStatus;

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
public class Registration {
    @Id
    @UuidGenerator
    String registrationId;

    @Column
    String eventId;

    @Column
    String attendeeId;

    @Column
    String ticketId;

    @Column
    LocalDateTime date;

    @Column(columnDefinition = "ENUM('pending', 'confirmed', 'cancelled', 'checked_in')")
    @Enumerated(EnumType.STRING)
    RegistrationStatus status;

    @CreateTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
