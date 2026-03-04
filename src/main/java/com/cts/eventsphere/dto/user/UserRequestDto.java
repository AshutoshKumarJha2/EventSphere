package com.cts.eventsphere.dto.user;

import com.cts.eventsphere.model.data.UserRoles;
import com.cts.eventsphere.model.data.UserStatus;

/**
 * [Detailed description of the class's responsibility]
 * * @author 2480010
 *
 * @version 1.0
 * @since 03-03-2026
 */
public record UserRequestDto(
        String name,
        String email,
        String password,
        String phone,
        UserRoles role
) {
}
