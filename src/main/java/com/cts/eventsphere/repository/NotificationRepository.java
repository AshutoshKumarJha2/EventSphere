package com.cts.eventsphere.repository;
import com.cts.eventsphere.model.Notification;
import com.cts.eventsphere.model.data.StatusType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
/**
 * Repository class for Notification
 *
 * @author 2480027
 * @version 1.0
 * @since 09-03-2026
 */
public interface NotificationRepository extends JpaRepository<Notification, String> {

    Page<Notification> findByUserIdOrderByCreatedAtDesc(String userId, Pageable pageable);

    Page<Notification> findByUserIdAndStatusOrderByCreatedAtDesc(String userId, StatusType status, Pageable pageable);

    List<Notification> findTop20ByUserIdAndNotificationIdGreaterThanOrderByNotificationIdAsc(String userId, String lastId);

}
