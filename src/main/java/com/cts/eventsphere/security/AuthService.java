package com.cts.eventsphere.security;

import com.cts.eventsphere.dto.auth.LoginRequestDto;
import com.cts.eventsphere.dto.auth.LoginResponseDto;
import com.cts.eventsphere.dto.user.UserRequestDto;
import com.cts.eventsphere.model.User;
import com.cts.eventsphere.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * [Detailed description of the class's responsibility]
 * * @author 2480010
 *
 * @version 1.0
 * @since 04-03-2026
 */
//@Service
//@RequiredArgsConstructor
//public class AuthService {
//    private final JwtUtil jwtUtil;
//    private final UserRepository userRepository;
//    private final PasswordEncoder passwordEncoder;
//
//    public LoginResponseDto login(LoginRequestDto request) {
//        String token = jwtUtil.generateToken(request.getUsername());
//        return new LoginResponseDto(token);
//    }
//}



//public class AuthService {
//
//    private final UserRepository userRepository;
//    private final JwtUtil jwtUtil;
//    private final PasswordEncoder passwordEncoder;
//
//    public String authenticateAndGenerateToken(LoginRequestDto loginRequest) {
//        User user = userRepository.findByUsername(loginRequest.getUsername())
//                .orElseThrow(() -> new RuntimeException("Invalid Username or Password"));
//
//        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
//            throw new RuntimeException("Invalid Username or Password");
//        }
//
//        return jwtUtil.generateToken(user.getName());
//    }
//}




@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public String register(UserRequestDto dto) {
        User user = new User();
        user.setName(dto.name());
        user.setEmail(dto.email());
        user.setPhone(dto.phone());
        user.setRole(dto.role());
        user.setPassword(passwordEncoder.encode(dto.password())); // Hashing
        userRepository.save(user);
        return "User registered successfully";
    }

    public LoginResponseDto login(LoginRequestDto loginDto) {
        // 1. Find the user by email
        User user = userRepository.findByEmail(loginDto.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found with email: " + loginDto.getEmail()));

        // 2. Verify the password
        // (Matches the raw password from DTO against the hashed password in DB)
        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        // 3. Generate the tokens
        // Note: Use .name() or .toString() if your role is an Enum
        String roleName = user.getRole().name();

        String accessToken = jwtUtil.generateAccessToken(user.getEmail(), roleName);
        String refreshToken = jwtUtil.generateRefreshToken(user.getEmail(), roleName);

        // 4. Return the dual-token response
        return new LoginResponseDto(accessToken, refreshToken, "Bearer");
    }

    public LoginResponseDto refreshToken(UserPrincipal principal) {
        String email = principal.email();
        String roleName = principal.role();

        String newAccessToken = jwtUtil.generateAccessToken(email, roleName);
        String newRefreshToken = jwtUtil.generateRefreshToken(email, roleName);

        return new LoginResponseDto(newAccessToken, newRefreshToken, "Bearer");
    }
}