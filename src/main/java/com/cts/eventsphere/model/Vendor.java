package com.cts.eventsphere.model;

import com.cts.eventsphere.model.data.VendorStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Represents a vendor entity in the EventSphere system.
 *
 * @author 2480177
 * @version 1.0
 * @since 26-02-2026
 */

@Entity
@Table(name = "vendor")
@Data
public class Vendor {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "CHAR(36)")
    private String vendorId;

    @Column(nullable = false , length = 100)
    private String name;

    @Column(length = 100)
    private String contactInfo;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('active','inactive','blacklisted')")
    private VendorStatus status = VendorStatus.active;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
