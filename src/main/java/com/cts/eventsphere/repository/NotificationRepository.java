package com.cts.eventsphere.repository;
import com.cts.eventsphere.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
/**
 * Repository class for Notification
 *
 * @author 2480027
 * @version 1.0
 * @since 09-03-2026
 */
public interface NotificationRepository extends JpaRepository<Notification, String> {
    List<Notification> findTop20ByUserIdAndCreatedAtLessThanOrderByCreatedAtDesc(String userId, LocalDateTime createdDate);
}
