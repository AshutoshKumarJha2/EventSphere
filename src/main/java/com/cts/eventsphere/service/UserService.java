package com.cts.eventsphere.service;

import com.cts.eventsphere.dto.user.UserRequestDto;
import com.cts.eventsphere.dto.user.UserResponseDto;
import com.cts.eventsphere.repository.UserRepository;
import org.jspecify.annotations.Nullable;

import java.util.List;

/**
 * [Detailed description of the class's responsibility]
 * * @author 2480010
 *
 * @version 1.0
 * @since 03-03-2026
 */
public interface UserService {

    public List<UserResponseDto> getAllUsers();

    public UserResponseDto getUser(String userId);

    public UserResponseDto updateUserDetails(String userId, UserRequestDto userRequestDto);

    public void changeUserStatus(String userId, String status);

    public void changeUserRole(String userId, String role);
}
