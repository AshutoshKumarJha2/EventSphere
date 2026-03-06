package com.cts.eventsphere.dto.auth;

import lombok.Data;

/**
 * Dto for handling login requests, encapsulating user credentials and role information.
 * * @author 2480010
 *
 * @version 1.0
 * @since 03-03-2026
 */
//@Data
public record LoginRequestDto(
    String password,
    String email,
    String role
    )
{}

