package com.cts.eventsphere.NotificationModuleTest;

import com.cts.eventsphere.model.Notification;
import com.cts.eventsphere.repository.NotificationRepository;
import com.cts.eventsphere.service.EmailService;
import com.cts.eventsphere.service.impl.NotificationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for NotificationServiceImpl.
 * * @author 2479623
 *
 * @version 1.0
 * @since 15-03-2026
 */
public class NotificationServiceImplTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSendNotification_savesNotificationAndSendsEmail() {
        String userId = "user-123";
        String email = "user@example.com";
        String message = "Test message";
        String category = "INFO";

        Notification savedNotification = new Notification();
        savedNotification.setNotificationId("notif-1");
        when(notificationRepository.save(any(Notification.class))).thenReturn(savedNotification);

        notificationService.sendNotification(userId, email, message, category);

        ArgumentCaptor<Notification> captor = ArgumentCaptor.forClass(Notification.class);
        verify(notificationRepository).save(captor.capture());
        Notification captured = captor.getValue();
        assertEquals(userId, captured.getUserId());
        assertEquals(message, captured.getMessage());
        assertEquals(category, captured.getCategory());
        assertEquals("Unread", captured.getStatus());

        verify(emailService).sendNotificationEmail(eq(email), contains(category), eq(message));
    }

    @Test
    void testGetNotificationsScroll_returnsNotifications() {
        String userId = "user-123";
        LocalDateTime anchor = LocalDateTime.now();
        Notification n1 = new Notification();
        n1.setNotificationId("n1");
        n1.setUserId(userId);

        when(notificationRepository.findTop20ByUserIdAndCreatedAtLessThanOrderByCreatedAtDesc(userId, anchor))
                .thenReturn(List.of(n1));

        List<Notification> results = notificationService.getNotificationsScroll(userId, anchor, 20);

        assertEquals(1, results.size());
        assertEquals("n1", results.get(0).getNotificationId());
    }

    @Test
    void testMarkAsRead_updatesStatusIfFound() {
        String notificationId = "notif-1";
        Notification n = new Notification();
        n.setNotificationId(notificationId);
        n.setStatus("Unread");

        when(notificationRepository.findById(notificationId)).thenReturn(Optional.of(n));

        notificationService.markAsRead(notificationId);

        assertEquals("Read", n.getStatus());
    }

    @Test
    void testMarkAsRead_logsWarningIfNotFound() {
        String notificationId = "missing-id";
        when(notificationRepository.findById(notificationId)).thenReturn(Optional.empty());

        assertDoesNotThrow(() -> notificationService.markAsRead(notificationId));
    }
}
