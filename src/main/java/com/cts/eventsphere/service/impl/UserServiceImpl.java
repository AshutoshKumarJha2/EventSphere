package com.cts.eventsphere.service.impl;

import com.cts.eventsphere.dto.mapper.user.UserResponseDtoMapper;
import com.cts.eventsphere.dto.user.UserRequestDto;
import com.cts.eventsphere.dto.user.UserResponseDto;
import com.cts.eventsphere.model.User;
import com.cts.eventsphere.repository.UserRepository;
import com.cts.eventsphere.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * [Detailed description of the class's responsibility]
 * * @author 2480010
 *
 * @version 1.0
 * @since 03-03-2026
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public List<UserResponseDto> getAllUsers() {
        List<User> userList = userRepository.findAll();
        return userList.stream().map(user -> UserResponseDtoMapper.toDTO(user)).toList();
    }

    @Override
    public UserResponseDto getUser(String userId) {
        User user =userRepository.findById(userId).orElseThrow(()->new IllegalArgumentException("User with id "+userId+" does not exist"));
        UserResponseDto userResponseDto = UserResponseDtoMapper.toDTO(user);
        return userResponseDto;
    }

    @Override
    public UserResponseDto updateUserDetails(String userId, UserRequestDto userRequestDto) {
        if(!userRepository.existsById(userId)){
            throw new IllegalArgumentException("User with user id "+userId+" does not exist");
        }
        User user = userRepository.findById(userId).orElseThrow(()->new IllegalArgumentException("User with id "+userId+" does not exist"));


        if (userRequestDto.email() != null && !userRequestDto.email().equalsIgnoreCase(user.getEmail())) {
            // Optionally check uniqueness
            if (userRepository.existsByEmail(userRequestDto.email())) {
                throw new IllegalArgumentException("Email already in use: " + userRequestDto.email());
            }
            user.setEmail(userRequestDto.email());
        }

        if (userRequestDto.name() != null) user.setName(userRequestDto.name());
        if (userRequestDto.role() != null) user.setRole(userRequestDto.role());
        if (userRequestDto.phone() != null) user.setPhone(userRequestDto.phone());

        // Handle password (hash it)
        if (userRequestDto.password() != null && !userRequestDto.password().isBlank()) {
            // String hashed = passwordEncoder.encode(dto.password());
            String hashed = userRequestDto.password(); // replace with encoder above
            user.setPassword(hashed);
        }

        User saved = userRepository.save(user);
        return UserResponseDtoMapper.toDTO(saved);
    }

}
