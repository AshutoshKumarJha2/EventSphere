package com.cts.eventsphere.model;

import com.cts.eventsphere.model.data.ContractStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Represents a contract between EventSphere and a vendor/client.
 *
 * @author 2480177
 * @version 1.0
 * @since 26-02-2026
 */

@Entity
@Table(name = "contract")
@Data
public class Contract {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "CHAR(36)")
    private String contractId;

    @Column(nullable = false, columnDefinition = "CHAR(36)")
    private String vendorId;

    @Column(nullable = false, columnDefinition = "CHAR(36)")
    private String eventId;

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column(nullable = false)
    private LocalDateTime endDate;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal value;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('draft','active','completed','terminated')")
    private ContractStatus status = ContractStatus.draft;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
