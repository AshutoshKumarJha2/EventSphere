package com.cts.eventsphere.controller;

import com.cts.eventsphere.dto.user.UserResponseDto;
import com.cts.eventsphere.service.UserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * [Detailed description of the class's responsibility]
 * * @author 2480010
 *
 * @version 1.0
 * @since 03-03-2026
 */
@RestController
@Data
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;
    @GetMapping
    public ResponseEntity<List<UserResponseDto>>getAllUsers(){
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDto> getUserById(@RequestBody String userId){
        return ResponseEntity.ok(userService.getUser(userId));
    }

//    @PutMapping("/{userId}")
//    public ResponseEntity<UserResponseDto> updateUserDetails(@RequestBody String userId, @RequestBody UserRequestDto userRequestDto){
//        return ResponseEntity.ok(userService.updateUserDetails(userId,userRequestDto));
//    }
}
