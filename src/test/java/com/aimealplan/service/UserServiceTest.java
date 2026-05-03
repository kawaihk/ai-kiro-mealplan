package com.aimealplan.service;

import com.aimealplan.model.User;
import com.aimealplan.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("新規ユーザーが正しく登録できること")
    void registerUser_ShouldSaveUser() {
        User user = new User();
        user.setUsername("testuser");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = userService.registerUser(user);

        verify(userRepository, times(1)).save(user);
        assertThat(result.getUsername()).isEqualTo("testuser");
    }

    @Test
    @DisplayName("ID指定でユーザー情報が取得できること")
    void getUserById_ShouldReturnUser() {
        User user = new User();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Optional<User> result = userService.getUserById(1L);

        assertThat(result).isPresent();
    }
}