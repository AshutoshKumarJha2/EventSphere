package com.cts.eventsphere.AlertsModuleTest;

import com.cts.eventsphere.model.Notification;
import com.cts.eventsphere.repository.NotificationRepository;
import com.cts.eventsphere.service.EmailService;
import com.cts.eventsphere.service.impl.NotificationServiceImpl;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * [Detailed description of the class's responsibility]
 * * @author 2479623
 *
 * @version 1.0
 * @since 10-03-2026
 */
public class NotificationServiceTest {

    private NotificationRepository notificationRepository;
    private EmailService emailService;
    private NotificationServiceImpl notificationService;
    private Faker faker;

    @BeforeEach
    void setUp() {
        notificationRepository = mock(NotificationRepository.class);
        emailService = mock(EmailService.class);
        notificationService = new NotificationServiceImpl(notificationRepository, emailService);
        faker = new Faker();
    }

    @Test
    void testGetNotificationsScroll() {
        String userId = faker.idNumber().valid();
        LocalDateTime lastTimestamp = LocalDateTime.now().minusDays(1);
        Notification notification = new Notification();
        notification.setNotificationId(faker.idNumber().valid());
        notification.setUserId(userId);
        notification.setMessage(faker.lorem().sentence());

        when(notificationRepository.findTop20ByUserIdAndCreatedAtLessThanOrderByCreatedAtDesc(eq(userId), any()))
                .thenReturn(List.of(notification));

        List<Notification> results = notificationService.getNotificationsScroll(userId, lastTimestamp, 10);

        assertThat(results).hasSize(1);
        assertThat(results.get(0).getMessage()).isEqualTo(notification.getMessage());
        verify(notificationRepository).findTop20ByUserIdAndCreatedAtLessThanOrderByCreatedAtDesc(eq(userId), any());
    }

    @Test
    void testSendNotification() {
        String userId = faker.idNumber().valid();
        String email = faker.internet().emailAddress();
        String message = faker.lorem().sentence();
        String category = "INFO";

        Notification saved = new Notification();
        saved.setNotificationId(faker.idNumber().valid());
        when(notificationRepository.save(any(Notification.class))).thenReturn(saved);

        notificationService.sendNotification(userId, email, message, category);

        ArgumentCaptor<Notification> captor = ArgumentCaptor.forClass(Notification.class);
        verify(notificationRepository).save(captor.capture());
        Notification captured = captor.getValue();

        assertThat(captured.getUserId()).isEqualTo(userId);
        assertThat(captured.getMessage()).isEqualTo(message);
        assertThat(captured.getCategory()).isEqualTo(category);
        assertThat(captured.getStatus()).isEqualTo("Unread");

        verify(emailService).sendNotificationEmail(eq(email), contains(category), eq(message));
    }

    @Test
    void testMarkAsReadWhenNotificationExists() {
        String notificationId = faker.idNumber().valid();
        Notification notification = new Notification();
        notification.setNotificationId(notificationId);
        notification.setStatus("Unread");

        when(notificationRepository.findById(notificationId)).thenReturn(Optional.of(notification));

        notificationService.markAsRead(notificationId);

        assertThat(notification.getStatus()).isEqualTo("Read");
        verify(notificationRepository).findById(notificationId);
    }

    @Test
    void testMarkAsReadWhenNotificationNotFound() {
        String notificationId = faker.idNumber().valid();
        when(notificationRepository.findById(notificationId)).thenReturn(Optional.empty());

        notificationService.markAsRead(notificationId);

        verify(notificationRepository).findById(notificationId);
        // No exception thrown, status remains unchanged
    }
}
