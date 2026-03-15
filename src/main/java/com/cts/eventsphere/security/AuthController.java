package com.cts.eventsphere.security;

import com.cts.eventsphere.dto.auth.LoginRequestDto;
import com.cts.eventsphere.dto.auth.LoginResponseDto;
import com.cts.eventsphere.dto.auth.RegisterResponseDto;
import com.cts.eventsphere.dto.user.UserRequestDto;
import com.cts.eventsphere.dto.user.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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

    /**
    * Handles user registration requests. It accepts a UserRequestDto containing the user's registration details, and returns a RegisterResponseDto with the registered user's information if the registration is successful.
    * * @param dto
    * @return ResponseEntity<RegisterResponseDto>
     * @author 2480010
     * @version 1.0
     * @since 03-03-2026
    */
    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDto> register(@RequestBody UserRequestDto dto) {
        return ResponseEntity.ok(authService.register(dto));
    }

    /**
    * Handles user login requests. It accepts a LoginRequestDto containing the user's login credentials, and returns a LoginResponseDto with the authentication token and user information if the login is successful.
    * * @param dto
    * @return ResponseEntity<LoginResponseDto>
     * @author 2480010
     * @version 1.0
     * @since 03-03-2026
    */
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto dto) {
        var response = authService.login(dto);
        return ResponseEntity.ok(response);
    }

    /**
    * Handles token refresh requests. It accepts the authenticated user's principal and returns a new LoginResponseDto with a refreshed authentication token if the refresh operation is successful.
    * * @param principal
    * @return ResponseEntity<LoginResponseDto>
     * @author 2480010
     * @version 1.0
     * @since 03-03-2026
    */
    @PostMapping("/refresh")
    public ResponseEntity<LoginResponseDto> refreshToken(@AuthenticationPrincipal UserPrincipal principal) {
        var response = authService.refreshToken(principal);
        return ResponseEntity.ok(response);
    }
}
