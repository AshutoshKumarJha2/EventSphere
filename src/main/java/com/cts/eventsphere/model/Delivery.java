package com.cts.eventsphere.model;

import com.cts.eventsphere.model.data.DeliveryStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Represents a delivery entry for orders, equipment, or goods.
 *
 * @author 2480177
 * @version 1.0
 * @since 26-02-2026
 */

@Entity
@Table(name = "delivery")
@Data
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "CHAR(36)")
    private String deliveryId;

    @Column(nullable = false)
    private String invoiceId;

    @Column(nullable = false, length = 100)
    private String item;

    @Column(nullable = false)
    private Integer quantity;

    private LocalDateTime deliveryDate;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('scheduled','in_transit','delivered','failed','cancelled')")
    private DeliveryStatus status = DeliveryStatus.scheduled;

    @Column(length = 100)
    private String trackingNumber;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
