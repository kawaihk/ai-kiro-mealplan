package com.aimealplan.service.impl;

import com.aimealplan.entity.Role;
import com.aimealplan.entity.User;
import com.aimealplan.exception.DuplicateResourceException;
import com.aimealplan.exception.ResourceNotFoundException;
import com.aimealplan.model.UserDto;
import com.aimealplan.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        user = User.builder().id(1L).username("testuser").email("test@example.com")
                .password("hashed").role(Role.USER).build();
        userDto = UserDto.builder().username("testuser").email("test@example.com").role(Role.USER).build();
    }

    // --- createUser ---

    @Test
    @DisplayName("createUser - 正常系: ユーザーを保存して DTO を返す")
    void createUser_success() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password123")).thenReturn("hashed");
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDto result = userService.createUser(userDto, "password123");

        assertThat(result.getUsername()).isEqualTo("testuser");
        verify(passwordEncoder).encode("password123");
    }

    @Test
    @DisplayName("createUser - 正常系: role が null の場合は USER がデフォルト設定される")
    void createUser_defaultRole() {
        UserDto dtoWithoutRole = UserDto.builder().username("testuser").email("test@example.com").build();
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("hashed");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> {
            User u = inv.getArgument(0);
            assertThat(u.getRole()).isEqualTo(Role.USER);
            return user;
        });

        userService.createUser(dtoWithoutRole, "password123");
    }

    @Test
    @DisplayName("createUser - 異常系: username重複で DuplicateResourceException をスロー")
    void createUser_duplicateUsername() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        assertThatThrownBy(() -> userService.createUser(userDto, "password123"))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("Username already in use");
    }

    @Test
    @DisplayName("createUser - 異常系: email重複で DuplicateResourceException をスロー")
    void createUser_duplicateEmail() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        assertThatThrownBy(() -> userService.createUser(userDto, "password123"))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("Email already in use");
    }

    // --- getAllUsers ---

    @Test
    @DisplayName("getAllUsers - 正常系: 全ユーザーのリストを返す")
    void getAllUsers_success() {
        when(userRepository.findAll()).thenReturn(List.of(user));

        List<UserDto> result = userService.getAllUsers();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getUsername()).isEqualTo("testuser");
    }

    // --- getUserById ---

    @Test
    @DisplayName("getUserById - 正常系: 存在するIDで Optional に包まれた DTO を返す")
    void getUserById_success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Optional<UserDto> result = userService.getUserById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getUsername()).isEqualTo("testuser");
    }

    @Test
    @DisplayName("getUserById - 異常系: 存在しないIDで Optional.empty を返す")
    void getUserById_notFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThat(userService.getUserById(99L)).isEmpty();
    }

    // --- updateUser ---

    @Test
    @DisplayName("updateUser - 正常系: ユーザー情報を更新して DTO を返す")
    void updateUser_success() {
        UserDto updateDto = UserDto.builder().username("updated").email("updated@example.com").role(Role.ADMIN).build();
        User updated = User.builder().id(1L).username("updated").email("updated@example.com").role(Role.ADMIN).password("hashed").build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findByUsername("updated")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("updated@example.com")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(updated);

        UserDto result = userService.updateUser(1L, updateDto);

        assertThat(result.getUsername()).isEqualTo("updated");
        assertThat(result.getRole()).isEqualTo(Role.ADMIN);
    }

    @Test
    @DisplayName("updateUser - 異常系: 存在しないIDで ResourceNotFoundException をスロー")
    void updateUser_notFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.updateUser(99L, userDto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("User not found");
    }

    @Test
    @DisplayName("updateUser - 異常系: 他ユーザーのusernameと重複で DuplicateResourceException をスロー")
    void updateUser_duplicateUsername() {
        User other = User.builder().id(2L).username("testuser").email("other@example.com").build();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(other));

        assertThatThrownBy(() -> userService.updateUser(1L, userDto))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("Username already in use");
    }

    // --- deleteUser ---

    @Test
    @DisplayName("deleteUser - 正常系: 存在するIDで削除を実行する")
    void deleteUser_success() {
        when(userRepository.existsById(1L)).thenReturn(true);

        userService.deleteUser(1L);

        verify(userRepository).deleteById(1L);
    }

    @Test
    @DisplayName("deleteUser - 異常系: 存在しないIDで ResourceNotFoundException をスロー")
    void deleteUser_notFound() {
        when(userRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> userService.deleteUser(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("User not found");
    }
}
