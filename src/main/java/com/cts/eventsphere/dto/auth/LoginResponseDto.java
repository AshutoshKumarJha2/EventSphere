package com.cts.eventsphere.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * Dto for handling login responses, encapsulating access and refresh tokens along with token type information.
 * * @author 2480010
 *
 * @version 1.0
 * @since 03-03-2026
 */
//@Data
//@RequiredArgsConstructor
//@AllArgsConstructor
public record LoginResponseDto(
        String accessToken,
    String refreshToken,
    String type
    )
{}
