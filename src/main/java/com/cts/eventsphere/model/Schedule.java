package com.cts.eventsphere.model;

import com.cts.eventsphere.model.data.ScheduleStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Schedule model class.
 * * @author 2479623
 *
 * @version 1.0
 * @since 26-02-2026
 */
@Entity
@Table(name = "schedule")
@Data
public class Schedule {
    @Id
            @GeneratedValue(strategy = GenerationType.UUID)
            @Column(columnDefinition = "CHAR(36)")
    private String scheduleId;

    @Column(nullable = false, columnDefinition = "CHAR(36)")
    private String eventId;

    @Column(nullable = false)
    private LocalDateTime date;

    @Column(nullable = false, columnDefinition = "CHAR(36)")
    private String timeSlot;

    @Column(nullable = false, columnDefinition = "CHAR(100)")
    private String activity;

    @Enumerated(EnumType.STRING)
            @Column(columnDefinition = "ENUM('draft','active','completed','terminated')")
    private ScheduleStatus status = ScheduleStatus.draft;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
