package com.cts.eventsphere.security;

import com.cts.eventsphere.dto.auth.LoginRequestDto;
import com.cts.eventsphere.dto.auth.LoginResponseDto;
import com.cts.eventsphere.dto.user.UserRequestDto;
import com.cts.eventsphere.exception.user.InvalidPasswordException;
import com.cts.eventsphere.exception.user.UserAlreadyExistsException;
import com.cts.eventsphere.exception.user.UserNotFoundException;
import com.cts.eventsphere.model.User;
import com.cts.eventsphere.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * AuthService is responsible for handling the core authentication logic, including user registration, login, and token refresh operations. It interacts with the UserRepository to manage user data and uses PasswordEncoder for secure password handling. Additionally, it utilizes JwtUtil to generate and manage JWT tokens for authenticated sessions.
 * * @author 2480010
 *
 * @version 1.0
 * @since 04-03-2026
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public String register(UserRequestDto dto) {
        var existingUser = userRepository.findByEmail(dto.email());

        if (existingUser.isPresent()) {
            throw new UserAlreadyExistsException(dto.email());
        }

        User user = new User();
        user.setName(dto.name());
        user.setEmail(dto.email());
        user.setPhone(dto.phone());
        user.setRole(dto.role());
        user.setPassword(passwordEncoder.encode(dto.password())); // Hashing
        userRepository.save(user);
        log.info("User registered with details: {}", user);
        return "User registered successfully";
    }

    public LoginResponseDto login(LoginRequestDto loginDto) {
        User user = userRepository.findByEmail(loginDto.email())
                .orElseThrow(() -> new UserNotFoundException(loginDto.email()));

        if (!passwordEncoder.matches(loginDto.password(), user.getPassword())) {
            throw new InvalidPasswordException("Invalid password provided");
        }

        String roleName = user.getRole().name();

        String accessToken = jwtUtil.generateAccessToken(user.getUserId(),user.getEmail(), roleName);
        String refreshToken = jwtUtil.generateRefreshToken(user.getUserId(),user.getEmail(), roleName);

        return new LoginResponseDto(accessToken, refreshToken, "Bearer");
    }

    public LoginResponseDto refreshToken(UserPrincipal principal) {
        String userId = principal.userId();
        String email = principal.email();
        String roleName = principal.role();

        String newAccessToken = jwtUtil.generateAccessToken(userId, email, roleName);
        String newRefreshToken = jwtUtil.generateRefreshToken(userId, email, roleName);

        return new LoginResponseDto(newAccessToken, newRefreshToken, "Bearer");
    }
}