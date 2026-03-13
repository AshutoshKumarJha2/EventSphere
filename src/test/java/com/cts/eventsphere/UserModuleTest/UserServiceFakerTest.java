package com.cts.eventsphere.UserModuleTest;

import com.cts.eventsphere.service.impl.UserServiceImpl;
import com.github.javafaker.Faker;
import com.cts.eventsphere.dto.user.UserRequestDto;
import com.cts.eventsphere.dto.user.UserResponseDto;
import com.cts.eventsphere.exception.user.EmailAlreadyExistsException;
import com.cts.eventsphere.exception.user.UserNotFoundException;
import com.cts.eventsphere.model.User;
import com.cts.eventsphere.model.data.UserRoles;
import com.cts.eventsphere.model.data.UserStatus;
import com.cts.eventsphere.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


/**
 * This class is responsible for testing the UserController using Faker data. It will contain unit tests for all the endpoints in the UserController, ensuring that they behave as expected when provided with valid and invalid input data.
 * * @author 2480010
 *
 * @version 1.0
 * @since 13-03-2026
 */

@ExtendWith(MockitoExtension.class)
class UserServiceFakerTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private Faker faker;
    private User testUser;
    private String userId;

    @BeforeEach
    void setUp() {
        faker = new Faker();
        userId = UUID.randomUUID().toString();

        testUser = new User();
        testUser.setUserId(userId);
        testUser.setName(faker.name().fullName());
        testUser.setEmail(faker.internet().emailAddress());
        testUser.setPhone(faker.phoneNumber().phoneNumber());
        testUser.setRole(UserRoles.attendee);
        testUser.setStatus(UserStatus.active);
    }

    @Test
    @DisplayName("Get User by ID - Success")
    void getUser_Success() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));

        UserResponseDto result = userService.getUser(userId);

        assertNotNull(result);
        assertEquals(testUser.getEmail(), result.email()); // Assuming DTO uses records
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    @DisplayName("Get User by ID - Throws UserNotFoundException")
    void getUser_NotFound() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUser(userId));
    }

    @Test
    @DisplayName("Update User - Success")
    void updateUserDetails_Success() {
        UserRequestDto request = new UserRequestDto(
                faker.name().fullName(),
                "newemail@example.com",
                null, // password
                faker.phoneNumber().phoneNumber()
        );

        when(userRepository.existsById(userId)).thenReturn(true);
        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        UserResponseDto result = userService.updateUserDetails(userId, request);

        assertNotNull(result);
        verify(userRepository).save(testUser);
    }

    @Test
    @DisplayName("Update User - Throws EmailAlreadyExists")
    void updateUserDetails_EmailConflict() {
        UserRequestDto request = new UserRequestDto(null, "existing@test.com", null, null);

        when(userRepository.existsById(userId)).thenReturn(true);
        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
        when(userRepository.existsByEmail("existing@test.com")).thenReturn(true);

        assertThrows(EmailAlreadyExistsException.class, () -> userService.updateUserDetails(userId, request));
    }

    @Test
    @DisplayName("Change User Status - Success")
    void changeUserStatus_Success() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));

        userService.changeUserStatus(userId, "suspended");

        assertEquals(UserStatus.suspended, testUser.getStatus());
        verify(userRepository).save(testUser);
    }
}
