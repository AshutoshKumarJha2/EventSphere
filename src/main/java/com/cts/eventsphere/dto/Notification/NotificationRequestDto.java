package com.cts.eventsphere.dto.Notification;

/**
 * RequestDto class for Notification
 *
 * @author 2480027
 * @version 1.0
 * @since 09-03-2026
 */

import com.cts.eventsphere.model.data.CategoryType;
import lombok.Data;


public record NotificationRequestDto (
     String userId,
     String eventId,
     String message,
     CategoryType category
){}
