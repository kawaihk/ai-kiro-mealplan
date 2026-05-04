package com.aimealplan.controller;

import com.aimealplan.entity.Role;
import com.aimealplan.exception.ResourceNotFoundException;
import com.aimealplan.model.UserCreateRequest;
import com.aimealplan.model.UserDto;
import com.aimealplan.model.UserUpdateRequest;
import com.aimealplan.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserDto> createUser(
            @Valid @RequestBody UserCreateRequest request) {
        UserDto userDto = UserDto.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .role(parseRole(request.getRole()))
                .build();
        UserDto created = userService.createUser(userDto, request.getPassword());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserUpdateRequest request) {
        UserDto userDto = UserDto.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .role(parseRole(request.getRole()))
                .build();
        return ResponseEntity.ok(userService.updateUser(id, userDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * ロール文字列を {@link Role} enum に変換します。
     * {@code null} または空文字の場合は {@code null} を返します。
     *
     * @param roleStr ロール文字列（"USER" / "ADMIN"、大文字・小文字不問）
     * @return 対応する {@link Role}、または {@code null}
     */
    private Role parseRole(String roleStr) {
        if (roleStr == null || roleStr.isBlank()) {
            return null;
        }
        return Role.valueOf(roleStr.toUpperCase());
    }
}
