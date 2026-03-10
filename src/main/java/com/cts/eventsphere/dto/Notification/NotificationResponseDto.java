package com.cts.eventsphere.dto.Notification;

import com.cts.eventsphere.model.data.CategoryType;
import com.cts.eventsphere.model.data.StatusType;

import java.time.LocalDateTime;

/**
 * ResponseDtoClass for Notification
 *
 * @author 2480027
 * @version 1.0
 * @since 09-03-2026
 */
public record NotificationResponseDto(

     String id,
     String userId,
     String eventId,
     String message,
     CategoryType category,
     StatusType status,
     LocalDateTime createdDate
             )
{

}
