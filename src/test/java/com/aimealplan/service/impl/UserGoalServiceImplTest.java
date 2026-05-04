package com.aimealplan.service.impl;

import com.aimealplan.entity.User;
import com.aimealplan.entity.UserGoal;
import com.aimealplan.exception.ResourceNotFoundException;
import com.aimealplan.model.UserGoalDto;
import com.aimealplan.repository.UserGoalRepository;
import com.aimealplan.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserGoalServiceImplTest {

    @Mock
    private UserGoalRepository userGoalRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserGoalServiceImpl userGoalService;

    private User user;
    private UserGoal userGoal;
    private UserGoalDto userGoalDto;

    @BeforeEach
    void setUp() {
        user = User.builder().id(1L).username("testuser").email("test@example.com").build();
        userGoal = UserGoal.builder()
                .id(1L).user(user)
                .targetCalories(2000)
                .targetWeight(65.5)
                .activityLevel("MODERATE")
                .targetProtein(30).targetFat(30).targetCarbohydrates(40)
                .build();
        userGoalDto = new UserGoalDto();
        userGoalDto.setUserId(1L);
        userGoalDto.setTargetCalories(2000);
        userGoalDto.setTargetWeight(65.5);
        userGoalDto.setActivityLevel("MODERATE");
        userGoalDto.setProteinRatio(30);
        userGoalDto.setFatRatio(30);
        userGoalDto.setCarbohydrateRatio(40);
    }

    // --- createUserGoal ---

    @Test
    @DisplayName("createUserGoal - 正常系: UserGoal を保存して DTO を返す")
    void createUserGoal_success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userGoalRepository.save(any(UserGoal.class))).thenReturn(userGoal);

        UserGoalDto result = userGoalService.createUserGoal(userGoalDto);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getTargetCalories()).isEqualTo(2000);
        assertThat(result.getTargetWeight()).isEqualTo(65.5);
        assertThat(result.getActivityLevel()).isEqualTo("MODERATE");
        assertThat(result.getProteinRatio()).isEqualTo(30);
        assertThat(result.getFatRatio()).isEqualTo(30);
        assertThat(result.getCarbohydrateRatio()).isEqualTo(40);
    }

    @Test
    @DisplayName("createUserGoal - 異常系: ユーザー不在で ResourceNotFoundException をスロー")
    void createUserGoal_userNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userGoalService.createUserGoal(userGoalDto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("User not found");
    }

    // --- getUserGoalByUserId ---

    @Test
    @DisplayName("getUserGoalByUserId - 正常系: 目標が存在する場合は Optional に包まれた DTO を返す")
    void getUserGoalByUserId_success() {
        when(userGoalRepository.findByUserId(1L)).thenReturn(Optional.of(userGoal));

        Optional<UserGoalDto> result = userGoalService.getUserGoalByUserId(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getTargetCalories()).isEqualTo(2000);
    }

    @Test
    @DisplayName("getUserGoalByUserId - 正常系: 目標未設定の場合は Optional.empty を返す")
    void getUserGoalByUserId_notFound() {
        when(userGoalRepository.findByUserId(1L)).thenReturn(Optional.empty());

        assertThat(userGoalService.getUserGoalByUserId(1L)).isEmpty();
    }

    // --- updateUserGoal ---

    @Test
    @DisplayName("updateUserGoal - 正常系: UserGoal を更新して DTO を返す")
    void updateUserGoal_success() {
        UserGoalDto updateDto = new UserGoalDto();
        updateDto.setTargetCalories(2500);
        updateDto.setProteinRatio(35);
        updateDto.setFatRatio(25);
        updateDto.setCarbohydrateRatio(40);

        UserGoal updated = UserGoal.builder().id(1L).user(user)
                .targetCalories(2500).targetProtein(35).targetFat(25).targetCarbohydrates(40).build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userGoalRepository.findByUser(user)).thenReturn(Optional.of(userGoal));
        when(userGoalRepository.save(any(UserGoal.class))).thenReturn(updated);

        UserGoalDto result = userGoalService.updateUserGoal(1L, updateDto);

        assertThat(result.getTargetCalories()).isEqualTo(2500);
        assertThat(result.getProteinRatio()).isEqualTo(35);
    }

    @Test
    @DisplayName("updateUserGoal - 異常系: ユーザー不在で ResourceNotFoundException をスロー")
    void updateUserGoal_userNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userGoalService.updateUserGoal(99L, userGoalDto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("User not found");
    }

    @Test
    @DisplayName("updateUserGoal - 異常系: 目標未設定で ResourceNotFoundException をスロー")
    void updateUserGoal_goalNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userGoalRepository.findByUser(user)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userGoalService.updateUserGoal(1L, userGoalDto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("UserGoal not found");
    }

    // --- deleteUserGoal ---

    @Test
    @DisplayName("deleteUserGoal - 正常系: UserGoal を削除する")
    void deleteUserGoal_success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userGoalRepository.findByUser(user)).thenReturn(Optional.of(userGoal));

        userGoalService.deleteUserGoal(1L);

        verify(userGoalRepository).delete(userGoal);
    }

    @Test
    @DisplayName("deleteUserGoal - 異常系: ユーザー不在で ResourceNotFoundException をスロー")
    void deleteUserGoal_userNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userGoalService.deleteUserGoal(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("User not found");
    }

    @Test
    @DisplayName("deleteUserGoal - 異常系: 目標未設定で ResourceNotFoundException をスロー")
    void deleteUserGoal_goalNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userGoalRepository.findByUser(user)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userGoalService.deleteUserGoal(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("UserGoal not found");
    }
}
