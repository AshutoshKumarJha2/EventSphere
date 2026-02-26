package com.cts.eventsphere.model;

import com.cts.eventsphere.model.data.AvailabilityStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 *  Venue model class
 * * @author 2479476
 *
 * @version 1.0
 * @since 26-02-2026
 */

@Entity
@Table(name = "venue")
@Data
public class Venue {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "venueId")
    private String venueId ;


    @Column(nullable = false,columnDefinition = "VARCHAR(255)")
    private String location;

    @Column(nullable = false)
    private int capacity;

    @Column(nullable = false,columnDefinition = "VARCHAR(100)")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('available','unavailable','maintenance')")
    private AvailabilityStatus availabilityStatus  =  AvailabilityStatus.available;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

}
