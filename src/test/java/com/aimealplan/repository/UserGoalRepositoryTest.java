package com.aimealplan.repository;

import com.aimealplan.entity.Role;
import com.aimealplan.entity.User;
import com.aimealplan.entity.UserGoal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserGoalRepositoryTest {

    @Autowired
    private UserGoalRepository userGoalRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = userRepository.save(User.builder()
                .username("testuser")
                .email("test@example.com")
                .password("hashed")
                .role(Role.USER)
                .build());

        userGoalRepository.save(UserGoal.builder()
                .user(user)
                .targetCalories(2000)
                .targetProtein(30)
                .targetFat(30)
                .targetCarbohydrates(40)
                .build());
    }

    // --- findByUser ---

    @Test
    @DisplayName("findByUser - 正常系: 存在する User で UserGoal を返す")
    void findByUser_found() {
        Optional<UserGoal> result = userGoalRepository.findByUser(user);

        assertThat(result).isPresent();
        assertThat(result.get().getTargetCalories()).isEqualTo(2000);
        assertThat(result.get().getTargetProtein()).isEqualTo(30);
    }

    @Test
    @DisplayName("findByUser - 異常系: 目標未設定の User で Optional.empty を返す")
    void findByUser_notFound() {
        User otherUser = userRepository.save(User.builder()
                .username("otheruser")
                .email("other@example.com")
                .password("hashed")
                .role(Role.USER)
                .build());

        Optional<UserGoal> result = userGoalRepository.findByUser(otherUser);

        assertThat(result).isEmpty();
    }

    // --- findByUserId ---

    @Test
    @DisplayName("findByUserId - 正常系: 存在する userId で UserGoal を返す")
    void findByUserId_found() {
        Optional<UserGoal> result = userGoalRepository.findByUserId(user.getId());

        assertThat(result).isPresent();
        assertThat(result.get().getTargetCalories()).isEqualTo(2000);
    }

    @Test
    @DisplayName("findByUserId - 異常系: 存在しない userId で Optional.empty を返す")
    void findByUserId_notFound() {
        Optional<UserGoal> result = userGoalRepository.findByUserId(999L);

        assertThat(result).isEmpty();
    }

    // --- UNIQUE 制約（1ユーザー1目標）---

    @Test
    @DisplayName("save - 異常系: 同一ユーザーに2件目の UserGoal を保存すると例外をスロー")
    void save_duplicateUserGoal_throwsException() {
        UserGoal duplicate = UserGoal.builder()
                .user(user) // 同一ユーザー
                .targetCalories(2500)
                .build();

        org.junit.jupiter.api.Assertions.assertThrows(
                Exception.class, () -> {
                    userGoalRepository.saveAndFlush(duplicate);
                });
    }
}
