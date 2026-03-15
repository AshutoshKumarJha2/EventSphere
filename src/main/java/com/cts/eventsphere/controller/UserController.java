package com.cts.eventsphere.controller;

import com.cts.eventsphere.dto.user.UserRequestDto;
import com.cts.eventsphere.dto.user.UserResponseDto;
import com.cts.eventsphere.security.UserPrincipal;
import com.cts.eventsphere.service.UserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Rest Controller for User entity
 * * @author 2480010
 *
 * @version 1.0
 * @since 03-03-2026
 */
@RestController
@Data
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UserController {
    private final UserService userService;

    /**
        * Endpoint to retrieve all users. Accessible only by users with the 'ADMIN' role.
        * @return ResponseEntity<List<UserResponseDto>>
        * @author 2480010
        * @version 1.0
        * @since 03-03-2026
        */
    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponseDto>>getAllUsers(){
        return ResponseEntity.ok(userService.getAllUsers());
    }

    
    /**
    * Endpoint to retrieve a user by their ID. Accessible by authenticated users. It accepts a userId as a path variable and returns the corresponding user details in the response.
    * * @param userId
    * @return ResponseEntity<UserResponseDto>
     * @author 2480010
     * @version 1.0
     * @since 03-03-2026
    */
    @GetMapping("/users/{userId}")
    public ResponseEntity<UserResponseDto> getUserById(@RequestBody String userId){
        return ResponseEntity.ok(userService.getUser(userId));
    }

    /**
    * Endpoint to update user details. Accessible by authenticated users. It accepts a userId as a path variable and a UserRequestDto containing the updated user details in the request body, and returns the updated user details in the response.
    * * @param userId
     * @param userRequestDto
    * @return ResponseEntity<UserResponseDto>
     * @author 2480010
     * @version 1.0
     * @since 03-03-2026
    */
    @PutMapping("/users/{userId}")
    public ResponseEntity<UserResponseDto> updateUserDetails(@RequestBody String userId, @RequestBody UserRequestDto userRequestDto){
        return ResponseEntity.ok(userService.updateUserDetails(userId,userRequestDto));
    }

    /**
    * Endpoint to retrieve the details of the currently authenticated user. Accessible by authenticated users. It uses the @AuthenticationPrincipal annotation to access the details of the authenticated user and returns their user details in the response.
     * @author 2480010
     * @version 1.0
     * @since 03-03-2026
    * * @param userPrincipal
    * @return ResponseEntity<UserResponseDto>
    */
    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserResponseDto> getMyDetails(@AuthenticationPrincipal UserPrincipal userPrincipal){
        String authenticatedUserId = userPrincipal.userId();
        return ResponseEntity.ok(userService.getUser(authenticatedUserId));
    }

    /**
     * Changes the role of a user. This endpoint is restricted to users with the ADMIN role. It accepts the userId as a path variable and the new role as a request parameter, and returns the updated user details after the role change.
    * * @param userId
    * @return ResponseEntity<UserResponseDto>
     * @author 2480010
     * @version 1.0
     * @since 03-03-2026
    */
    @PatchMapping("/users/{userId}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDto> changeUserStatus(@PathVariable String userId, @RequestParam String status) {
        UserResponseDto updatedUserStatus = userService.changeUserStatus(userId, status);
        return ResponseEntity.ok(updatedUserStatus);
    }

    /**
    * Changes the role of a user. This endpoint is restricted to users with the ADMIN role. It accepts the userId as a path variable and the new role as a request parameter, and returns the updated user details after the role change.
    * * @param userId
     * @param role
    * @return ResponseEntity<UserResponseDto>
     * @author 2480010
     * @version 1.0
     * @since 03-03-2026
    */
    @PatchMapping("/users/{userId}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDto> changeUserRole(@PathVariable String userId, @RequestParam String role) {
        UserResponseDto updatedUserRole = userService.changeUserRole(userId, role);
        return ResponseEntity.ok(updatedUserRole);
    }

}
