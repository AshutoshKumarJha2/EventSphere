package com.cts.eventsphere.UserModuleTest;

import com.cts.eventsphere.controller.UserController;
import com.cts.eventsphere.dto.user.UserRequestDto;
import com.cts.eventsphere.dto.user.UserResponseDto;
import com.cts.eventsphere.model.data.UserRoles;
import com.cts.eventsphere.model.data.UserStatus;
import com.cts.eventsphere.security.UserPrincipal;
import com.cts.eventsphere.service.UserService;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * This class is responsible for testing the UserController using Faker data. It will contain unit tests for all the endpoints in the UserController, ensuring that they behave as expected when provided with valid and invalid input data.
 * * @author 2480010
 *
 * @version 1.0
 * @since 13-03-2026
 */

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private Faker faker;
    private String userId;

    @BeforeEach
    void setUp() {
        faker = new Faker();
        userId = UUID.randomUUID().toString();
    }

    @Test
    @DisplayName("Get All Users - Should return 200 OK and list")
    void getAllUsers_Success() {
        // Arrange
        UserResponseDto userDto = new UserResponseDto(userId, faker.name().fullName(), UserRoles.attendee, faker.internet().emailAddress(), "12345", UserStatus.active);
        when(userService.getAllUsers()).thenReturn(List.of(userDto));

        // Act
        ResponseEntity<List<UserResponseDto>> response = userController.getAllUsers();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(userService, times(1)).getAllUsers();
    }

    @Test
    @DisplayName("Get User By ID - Should return 200 OK")
    void getUserById_Success() {
        // Arrange
        UserResponseDto userDto = new UserResponseDto(userId, "John Doe", UserRoles.attendee, "john@example.com", "9999", UserStatus.active);
        when(userService.getUser(userId)).thenReturn(userDto);

        // Act - Note: Passing userId as the body because of your @RequestBody String userId
        ResponseEntity<UserResponseDto> response = userController.getUserById(userId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("John Doe", response.getBody().name());
        verify(userService).getUser(userId);
    }

    @Test
    @DisplayName("Get My Details - Should return 200 OK")
    void getMyDetails_Success() {
        // Arrange
        UserPrincipal principal = mock(UserPrincipal.class);
        when(principal.userId()).thenReturn(userId);
        UserResponseDto userDto = new UserResponseDto(userId, "Me", UserRoles.attendee,"me@example.com", "111", UserStatus.active);
        when(userService.getUser(userId)).thenReturn(userDto);

        // Act
        ResponseEntity<UserResponseDto> response = userController.getMyDetails(principal);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userId, response.getBody().userId());
    }

    @Test
    @DisplayName("Change User Status - Should return Success message")
    void changeUserStatus_Success() {
        // Act
        ResponseEntity<UserResponseDto> response = userController.changeUserStatus(userId, "inactive");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User status updated successfully.", response.getBody());
        verify(userService).changeUserStatus(userId, "inactive");
    }

    @Test
    @DisplayName("Update User Details - Should return updated DTO")
    void updateUserDetails_Success() {
        // Arrange
        UserRequestDto requestDto = new UserRequestDto("New Name", null, null, null);
        UserResponseDto responseDto = new UserResponseDto(userId, "New Name", UserRoles.attendee,"email@test.com","555", UserStatus.active);
        when(userService.updateUserDetails(userId, requestDto)).thenReturn(responseDto);

        // Act
        ResponseEntity<UserResponseDto> response = userController.updateUserDetails(userId, requestDto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("New Name", response.getBody().name());
        verify(userService).updateUserDetails(userId, requestDto);
    }
}