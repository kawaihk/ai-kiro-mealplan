package com.aimealplan.service.impl;

import com.aimealplan.entity.Role;
import com.aimealplan.entity.User;
import com.aimealplan.exception.DuplicateResourceException;
import com.aimealplan.exception.ResourceNotFoundException;
import com.aimealplan.model.UserDto;
import com.aimealplan.repository.UserRepository;
import com.aimealplan.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserDto createUser(UserDto userDto, String rawPassword) {
        if (userRepository.findByUsername(userDto.getUsername()).isPresent()) {
            throw new DuplicateResourceException(
                    "Username already in use: " + userDto.getUsername());
        }
        if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            throw new DuplicateResourceException(
                    "Email already in use: " + userDto.getEmail());
        }
        User user = User.builder()
                .username(userDto.getUsername())
                .email(userDto.getEmail())
                .role(userDto.getRole() != null ? userDto.getRole() : Role.USER)
                .password(passwordEncoder.encode(rawPassword))
                .build();
        return toDto(userRepository.save(user));
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<UserDto> getUserById(Long id) {
        return userRepository.findById(id).map(this::toDto);
    }

    @Override
    @Transactional
    public UserDto updateUser(Long id, UserDto userDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        // 自分以外が同じ username を使用していないか確認
        userRepository.findByUsername(userDto.getUsername())
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new DuplicateResourceException(
                            "Username already in use: " + userDto.getUsername());
                });

        // 自分以外が同じ email を使用していないか確認
        userRepository.findByEmail(userDto.getEmail())
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new DuplicateResourceException(
                            "Email already in use: " + userDto.getEmail());
                });

        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        if (userDto.getRole() != null) {
            user.setRole(userDto.getRole());
        }
        return toDto(userRepository.save(user));
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    private UserDto toDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }
}
