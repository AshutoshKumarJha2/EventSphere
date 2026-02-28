package com.cts.eventsphere.dto;

import com.cts.eventsphere.model.data.UserRoles;
import com.cts.eventsphere.model.data.UserStatus;

/**
 * [Detailed description of the class's responsibility]
 * * @author 2480010
 *
 * @version 1.0
 * @since 28-02-2026
 */
public record UserResponseDto(
        String userId,
        String name,
        UserRoles role,
        String email,
        String password,
        String phone,
        UserStatus status,
        String createdAt,
        String updatedAt
) {
}
