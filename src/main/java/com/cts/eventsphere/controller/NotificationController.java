package com.cts.eventsphere.controller;

import com.cts.eventsphere.model.Notification;
import com.cts.eventsphere.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Controller for Notifications.
 * * @author 2479623
 *
 * @version 1.0
 * @since 10-03-2026
 */
@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    /**
     * Infinite Scroll Endpoint
     * Usage: /api/v1/notifications/user-123/scroll?limit=10&lastTimestamp=2023-10-27T10:15:30
     */
    @GetMapping("/{userId}/scroll")
    public ResponseEntity<List<Notification>> getNotificationsScroll(
            @PathVariable String userId,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime lastTimestamp,
            @RequestParam(defaultValue = "20") int limit) {

        List<Notification> notifications = notificationService.getNotificationsScroll(userId, lastTimestamp, limit);
        return ResponseEntity.ok(notifications);
    }

    /**
     * Trigger a new notification (In-App + Email)
     */
    @PostMapping("/send")
    public ResponseEntity<Void> sendNotification(
            @RequestParam String userId,
            @RequestParam String email,
            @RequestParam String message,
            @RequestParam String category) {

        notificationService.sendNotification(userId, email, message, category);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Mark a specific notification as read
     */
    @PatchMapping("/{notificationId}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable String notificationId) {
        notificationService.markAsRead(notificationId);
        return ResponseEntity.noContent().build();
    }
}