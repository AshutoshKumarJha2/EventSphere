package com.cts.eventsphere.model;


import com.cts.eventsphere.model.data.Availability;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * * Resource model class
 * @author 2479476
 * @version 1.0
 * @since 26-02-2026
 */


@Data
@Table(name = "resource")
@Entity
public class Resource {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String resourceId;

    @Column(columnDefinition = "CHAR(36)")
    private String venueId;

    private String type;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('available','in_use','unavailable')")
    private Availability availability = Availability.available;

    @Column(precision = 10, scale = 2)
    private BigDecimal costRate;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

}
