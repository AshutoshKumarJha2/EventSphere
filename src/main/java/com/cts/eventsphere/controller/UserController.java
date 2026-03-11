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
    @GetMapping
    public ResponseEntity<List<UserResponseDto>>getAllUsers(){
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<UserResponseDto> getUserById(@RequestBody String userId){
        return ResponseEntity.ok(userService.getUser(userId));
    }

    @PutMapping("/users/{userId}")
    public ResponseEntity<UserResponseDto> updateUserDetails(@RequestBody String userId, @RequestBody UserRequestDto userRequestDto){
        return ResponseEntity.ok(userService.updateUserDetails(userId,userRequestDto));
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserResponseDto> getMyDetails(@AuthenticationPrincipal UserPrincipal userPrincipal){
        String authenticatedUserId = userPrincipal.userId();
        return ResponseEntity.ok(userService.getUser(authenticatedUserId));
    }
}
