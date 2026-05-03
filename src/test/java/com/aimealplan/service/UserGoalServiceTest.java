package com.aimealplan.service;

import com.aimealplan.model.UserGoal;
import com.aimealplan.repository.UserGoalRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserGoalServiceTest {

    @Mock
    private UserGoalRepository userGoalRepository;

    @InjectMocks
    private UserGoalService userGoalService;

    @Test
    @DisplayName("ユーザー目標を保存できること")
    void saveGoal_ShouldSaveUserGoal() {
        UserGoal goal = new UserGoal();
        goal.setUserId(1L);
        when(userGoalRepository.save(any(UserGoal.class))).thenReturn(goal);

        UserGoal result = userGoalService.saveGoal(goal);

        verify(userGoalRepository, times(1)).save(goal);
        assertThat(result.getUserId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("存在しないユーザーIDの目標取得時に例外が発生すること")
    void getGoalByUserId_ShouldThrowException_WhenNotFound() {
        // Arrange
        Long userId = 999L;
        when(userGoalRepository.findByUserId(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> userGoalService.getGoalByUserId(userId))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("目標設定が見つかりません");
    }
}