package com.cts.eventsphere.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.cts.eventsphere.model.data.BookingStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;


/**
 *  Booking model class
 * * @author 2479476
 *
 * @version 1.0
 * @since 26-02-2026
 */

@Entity
@Table(name = "booking")
@Data
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String bookingId;


    @Column(name = "eventId", length = 36)
    private String eventId;

    @Column(name = "venueId", length = 36)
    private String venueId;


    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, columnDefinition = "ENUM('pending','confirmed','cancelled')")
    private BookingStatus status = BookingStatus.pending;

    @CreationTimestamp
    @Column(name = "createdAt", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updatedAt")
    private LocalDateTime updatedAt;

}