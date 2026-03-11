package com.cts.eventsphere.service.impl;

import com.cts.eventsphere.model.Notification;
import com.cts.eventsphere.repository.NotificationRepository;
import com.cts.eventsphere.service.EmailService;
import com.cts.eventsphere.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Implementation for Notification Service
 * * @author 2479623
 *
 * @version 1.0
 * @since 10-03-2026
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;
    private final EmailService emailService;

    /**
     * @param userId
     * @param lastTimestamp
     * @param limit
     * @return
     */
    @Override
    public List<Notification> getNotificationsScroll(String userId, LocalDateTime lastTimestamp, int limit) {
        LocalDateTime anchor = (lastTimestamp == null) ? LocalDateTime.now() : lastTimestamp;
        log.debug("Fetching scroll notifications for user: {} using anchor timestamp: {}", userId, anchor);

        List<Notification> results = notificationRepository.findTop20ByUserIdAndCreatedAtLessThanOrderByCreatedAtDesc(
                userId,
                anchor
        );

        log.info("Found {} notifications for user: {} starting from {}", results.size(), userId, anchor);
        return results;
    }

    /**
     * @param userId
     * @param email
     * @param message
     * @param category
     */
    @Override
    @Transactional
    public void sendNotification(String userId, String email, String message, String category) {
        log.info("Processing notification for user: {} and category: {}", userId, category);

        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setMessage(message);
        notification.setCategory(category);
        notification.setStatus("Unread");

        Notification savedNotification = notificationRepository.save(notification);
        log.debug("Notification saved to DB with ID: {}", savedNotification.getNotificationId());

        log.info("Dispatching email notification to: {}", email);
        emailService.sendNotificationEmail(email, "New Notification: " + category, message);
    }

    /**
     * @param notificationId
     */
    @Transactional
    @Override
    public void markAsRead(String notificationId) {
        log.info("Attempting to mark notification {} as read", notificationId);
        notificationRepository.findById(notificationId).ifPresentOrElse(
                n -> {
                    n.setStatus("Read");
                    log.info("Notification {} status updated to Read", notificationId);
                },
                () -> log.warn("Notification {} not found, unable to mark as read", notificationId)
        );
    }
}