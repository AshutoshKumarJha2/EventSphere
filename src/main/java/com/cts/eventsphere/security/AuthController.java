package com.cts.eventsphere.security;

import com.cts.eventsphere.dto.auth.LoginRequestDto;
import com.cts.eventsphere.dto.auth.LoginResponseDto;
import com.cts.eventsphere.dto.user.UserRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * AuthController is responsible for handling authentication-related HTTP requests, including user registration, login, and token refresh operations. It interacts with the AuthService to perform these operations and returns appropriate responses to the client.
 * * @author 2480010
 *
 * @version 1.0
 * @since 03-03-2026
 */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserRequestDto dto) {
        return ResponseEntity.ok(authService.register(dto));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto dto) {
        var response = authService.login(dto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponseDto> refreshToken(@AuthenticationPrincipal UserPrincipal principal) {
        var response = authService.refreshToken(principal);
        return ResponseEntity.ok(response);
    }
}
