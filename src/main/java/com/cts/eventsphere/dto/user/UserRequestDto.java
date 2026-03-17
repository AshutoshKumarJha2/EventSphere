package com.cts.eventsphere.dto.user;

/**
 * DTO for creating or updating User entity
 * * @author 2480010
 *
 * @version 1.0
 * @since 03-03-2026
 */
public record UserRequestDto(
        String name,
        String email,
        String password,
        String phone
//        UserRoles role
) {
}
