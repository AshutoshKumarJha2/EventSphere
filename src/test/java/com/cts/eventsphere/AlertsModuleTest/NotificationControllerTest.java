package com.cts.eventsphere.AlertsModuleTest;

import com.cts.eventsphere.controller.NotificationController;
import com.cts.eventsphere.model.Notification;
import com.cts.eventsphere.service.NotificationService;
import com.cts.eventsphere.security.JwtUtil;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.security.autoconfigure.SecurityAutoConfiguration;
import org.springframework.boot.security.autoconfigure.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.security.autoconfigure.web.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Validates the NotificationController endpoints by mocking service dependencies.
 * Security filters are disabled to focus purely on controller logic and
 * request/response mapping.
 * * * @author 2479623
 *
 * @version 1.0
 * @since 10-03-2026
 */
@WebMvcTest(controllers = NotificationController.class,
        excludeAutoConfiguration = {
                SecurityAutoConfiguration.class,
                SecurityFilterAutoConfiguration.class,
                UserDetailsServiceAutoConfiguration.class
        })
@AutoConfigureMockMvc(addFilters = false) // This disables all Security Filters (CSRF, Auth, etc.)
public class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private NotificationService notificationService;

    @MockitoBean
    private JwtUtil jwtUtil;

    private Faker faker;

    @BeforeEach
    void setUp() {
        faker = new Faker();
    }

    @Test
    void testGetNotificationsScroll() throws Exception {
        String userId = faker.idNumber().valid();
        LocalDateTime lastTimestamp = LocalDateTime.now().minusDays(1);
        Notification notification = new Notification();
        notification.setNotificationId(faker.idNumber().valid());
        notification.setMessage(faker.lorem().sentence());

        Mockito.when(notificationService.getNotificationsScroll(eq(userId), any(), anyInt()))
                .thenReturn(List.of(notification));

        mockMvc.perform(get("/api/v1/notifications/{userId}/scroll", userId)
                        .param("limit", "5")
                        .param("lastTimestamp", lastTimestamp.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].message").value(notification.getMessage()));
    }

    @Test
    void testSendNotification() throws Exception {
        String userId = faker.idNumber().valid();
        String email = faker.internet().emailAddress();
        String message = faker.lorem().sentence();
        String category = "INFO";

        mockMvc.perform(post("/api/v1/notifications/send")
                        .param("userId", userId)
                        .param("email", email)
                        .param("message", message)
                        .param("category", category)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isCreated());

        Mockito.verify(notificationService).sendNotification(eq(userId), eq(email), eq(message), eq(category));
    }

    @Test
    void testMarkAsRead() throws Exception {
        String notificationId = faker.idNumber().valid();

        mockMvc.perform(patch("/api/v1/notifications/{notificationId}/read", notificationId))
                .andExpect(status().isNoContent());

        Mockito.verify(notificationService).markAsRead(eq(notificationId));
    }
}