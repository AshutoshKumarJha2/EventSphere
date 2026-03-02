package com.cts.eventsphere.dto.mapper;

import com.cts.eventsphere.dto.UserResponseDto;

import com.cts.eventsphere.model.User;
import org.springframework.stereotype.Component;

/**
 * Mapper for converting User Entity to User response DTO
 * * @author 2480010
 *
 * @version 1.0
 * @since 28-02-2026
 */

@Component
public class UserResponseDtoMapper {
    public UserResponseDto toDTO(User user){
        return new UserResponseDto(
                user.getUserId(),
                user.getName(),
                user.getRole(),
                user.getEmail(),
                user.getPassword(),
                user.getPhone(),
                user.getStatus(),
                user.getCreatedAt().toString(),
                user.getUpdatedAt().toString()
        );
    }

}
