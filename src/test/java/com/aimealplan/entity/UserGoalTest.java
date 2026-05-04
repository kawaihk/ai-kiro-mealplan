package com.aimealplan.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserGoalTest {

    private User buildUser(Long id) {
        return User.builder()
                .id(id)
                .username("testuser")
                .email("test@example.com")
                .password("hashed")
                .role(Role.USER)
                .build();
    }

    private UserGoal buildUserGoal(Long id) {
        return UserGoal.builder()
                .id(id)
                .user(buildUser(1L))
                .targetCalories(2000)
                .targetWeight(65.0)
                .activityLevel("MODERATE")
                .targetProtein(30)
                .targetFat(30)
                .targetCarbohydrates(40)
                .build();
    }

    @Test
    @DisplayName("@Builder - 全フィールドを指定してオブジェクトを生成できること")
    void builder_allFields() {
        UserGoal userGoal = buildUserGoal(1L);

        assertThat(userGoal.getId()).isEqualTo(1L);
        assertThat(userGoal.getTargetCalories()).isEqualTo(2000);
        assertThat(userGoal.getTargetWeight()).isEqualTo(65.0);
        assertThat(userGoal.getActivityLevel()).isEqualTo("MODERATE");
        assertThat(userGoal.getTargetProtein()).isEqualTo(30);
        assertThat(userGoal.getTargetFat()).isEqualTo(30);
        assertThat(userGoal.getTargetCarbohydrates()).isEqualTo(40);
        assertThat(userGoal.getUser().getUsername()).isEqualTo("testuser");
    }

    @Test
    @DisplayName("@NoArgsConstructor - 引数なしコンストラクタでオブジェクトを生成できること")
    void noArgsConstructor() {
        UserGoal userGoal = new UserGoal();
        assertThat(userGoal).isNotNull();
    }

    @Test
    @DisplayName("@Getter / @Setter - フィールドの取得・設定ができること")
    void getterSetter() {
        UserGoal userGoal = new UserGoal();
        userGoal.setTargetCalories(2500);
        userGoal.setTargetProtein(35);
        userGoal.setActivityLevel("HIGH");

        assertThat(userGoal.getTargetCalories()).isEqualTo(2500);
        assertThat(userGoal.getTargetProtein()).isEqualTo(35);
        assertThat(userGoal.getActivityLevel()).isEqualTo("HIGH");
    }

    @Test
    @DisplayName("@EqualsAndHashCode(exclude = user) - user を除くフィールドで等価比較されること")
    void equalsAndHashCode_excludesUser() {
        User user1 = buildUser(1L);
        User user2 = buildUser(2L);

        UserGoal goal1 = UserGoal.builder().id(1L).user(user1).targetCalories(2000).build();
        UserGoal goal2 = UserGoal.builder().id(1L).user(user2).targetCalories(2000).build();

        // user が異なっても id・targetCalories が同じなら等価
        assertThat(goal1).isEqualTo(goal2);
        assertThat(goal1.hashCode()).isEqualTo(goal2.hashCode());
    }

    @Test
    @DisplayName("@EqualsAndHashCode - targetCalories が異なる場合は等価でないこと")
    void equalsAndHashCode_differentCalories_notEqual() {
        UserGoal goal1 = UserGoal.builder().id(1L).targetCalories(2000).build();
        UserGoal goal2 = UserGoal.builder().id(1L).targetCalories(2500).build();

        assertThat(goal1).isNotEqualTo(goal2);
    }

    @Test
    @DisplayName("@ToString(exclude = user) - toString に user が含まれないこと（循環参照防止）")
    void toString_excludesUser() {
        UserGoal userGoal = buildUserGoal(1L);
        String str = userGoal.toString();

        assertThat(str).doesNotContain("user=User");
        assertThat(str).contains("targetCalories=2000");
        assertThat(str).contains("activityLevel=MODERATE");
    }
}
