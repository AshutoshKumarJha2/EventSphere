package com.cts.eventsphere.security;

import com.cts.eventsphere.dto.auth.LoginRequestDto;
import com.cts.eventsphere.dto.auth.LoginResponseDto;
import com.cts.eventsphere.dto.auth.RegisterResponseDto;
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

    /**
    * Handles user registration. It checks if a user with the provided email already exists, and if not, it creates a new user with the provided details, hashes the password, and saves the user to the database. If the registration is successful, it returns a RegisterResponseDto containing the registered user's information.
    * * @param dto
    * @return RegisterResponseDto
     * @author 2480010
     * @version 1.0
     * @since 04-03-2026
    */
    public RegisterResponseDto register(UserRequestDto dto) {
        var existingUser = userRepository.findByEmail(dto.email());

        if (existingUser.isPresent()) {
            throw new UserAlreadyExistsException(dto.email());
        }

        User user = new User();
        user.setName(dto.name());
        user.setEmail(dto.email());
        user.setPhone(dto.phone());
//        user.setRole(dto.role());
        user.setPassword(passwordEncoder.encode(dto.password())); // Hashing
        userRepository.save(user);
        log.info("User {} registered with id {}", user.getName(),user.getUserId());
//        return String.valueOf(user.getRole());
        String successRegistration = "User registered successfully with email: " + user.getEmail();
        return new RegisterResponseDto(user.getUserId(), user.getName(), user.getEmail(), user.getRole().name(), user.getPhone(), user.getStatus().name(), successRegistration);
    }

    /**
    * Handles user login. It retrieves the user by email, checks if the provided password matches the stored hashed password, and if the authentication is successful, it generates an access token and a refresh token using JwtUtil. If the login fails due to an incorrect email or password, it throws appropriate exceptions.
    * * @param loginDto
    * @return LoginResponseDto
     * @author 2480010
     * @version 1.0
     * @since 04-03-2026
    */
    public LoginResponseDto login(LoginRequestDto loginDto) {
        User user = userRepository.findByEmail(loginDto.email())
                .orElseThrow(() ->{
                    log.warn("Login failed for email: {} - user not found", loginDto.email());
                    return new UserNotFoundException(loginDto.email());
                });

        if (!passwordEncoder.matches(loginDto.password(), user.getPassword())) {
            log.warn("Login failed: invalid password");
            throw new InvalidPasswordException("Invalid password provided");
        }

        String roleName = user.getRole().name();

        String accessToken = jwtUtil.generateAccessToken(user.getUserId(),user.getEmail(), roleName);
        String refreshToken = jwtUtil.generateRefreshToken(user.getUserId(),user.getEmail(), roleName);

        return new LoginResponseDto(accessToken, refreshToken, "Bearer");
    }

    /**
    * Handles token refresh. It generates new access and refresh tokens for the authenticated user based on their principal information. The new tokens are returned in a LoginResponseDto. This method is typically called when the client requests a token refresh to maintain an authenticated session without requiring the user to log in again.
    * * @param principal
    * @return LoginResponseDto
     * @author 2480010
     * @version 1.0
     * @since 04-03-2026
    */
    public LoginResponseDto refreshToken(UserPrincipal principal) {
        String userId = principal.userId();
        String email = principal.email();
        String roleName = principal.role();

        String newAccessToken = jwtUtil.generateAccessToken(userId, email, roleName);
        String newRefreshToken = jwtUtil.generateRefreshToken(userId, email, roleName);

        return new LoginResponseDto(newAccessToken, newRefreshToken, "Bearer");
    }
}