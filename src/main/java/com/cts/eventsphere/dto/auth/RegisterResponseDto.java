package com.cts.eventsphere.dto.auth;

/**
 * [Detailed description of the class's responsibility]
 * * @author 2480010
 *
 * @version 1.0
 * @since 13-03-2026
 */
public record RegisterResponseDto(
        String userId,
        String userName,
        String userEmail,
        String role,
        String phoneNo,
        String userStatus,
        String message
) {
}
