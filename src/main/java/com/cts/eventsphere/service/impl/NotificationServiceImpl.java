package com.cts.eventsphere.service.impl;

import com.cts.eventsphere.model.Notification;
import com.cts.eventsphere.repository.NotificationRepository;
import com.cts.eventsphere.service.EmailService;
import com.cts.eventsphere.service.NotificationService;
import lombok.RequiredArgsConstructor;
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

        return notificationRepository.findTop20ByUserIdAndCreatedAtLessThanOrderByCreatedAtDesc(
                userId,
                anchor
        );
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
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setMessage(message);
        notification.setCategory(category);
        notification.setStatus("Unread");

        notificationRepository.save(notification);

        emailService.sendNotificationEmail(email, "New Notification: " + category, message);
    }

    /**
     * @param notificationId
     */
    @Transactional
    @Override
    public void markAsRead(String notificationId) {
        notificationRepository.findById(notificationId).ifPresent(n -> n.setStatus("Read"));
    }
}
