package com.aimealplan.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    private User buildUser(Long id) {
        return User.builder()
                .id(id)
                .username("testuser")
                .email("test@example.com")
                .password("hashed_password")
                .role(Role.USER)
                .build();
    }

    @Test
    @DisplayName("@Builder - 全フィールドを指定してオブジェクトを生成できること")
    void builder_allFields() {
        User user = buildUser(1L);

        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getUsername()).isEqualTo("testuser");
        assertThat(user.getEmail()).isEqualTo("test@example.com");
        assertThat(user.getPassword()).isEqualTo("hashed_password");
        assertThat(user.getRole()).isEqualTo(Role.USER);
    }

    @Test
    @DisplayName("@NoArgsConstructor - 引数なしコンストラクタでオブジェクトを生成できること")
    void noArgsConstructor() {
        User user = new User();
        assertThat(user).isNotNull();
    }

    @Test
    @DisplayName("@Getter / @Setter - フィールドの取得・設定ができること")
    void getterSetter() {
        User user = new User();
        user.setUsername("newuser");
        user.setEmail("new@example.com");
        user.setRole(Role.ADMIN);

        assertThat(user.getUsername()).isEqualTo("newuser");
        assertThat(user.getEmail()).isEqualTo("new@example.com");
        assertThat(user.getRole()).isEqualTo(Role.ADMIN);
    }

    @Test
    @DisplayName("@EqualsAndHashCode(exclude = mealPlans) - mealPlans を除くフィールドで等価比較されること")
    void equalsAndHashCode_excludesMealPlans() {
        MealPlan plan = MealPlan.builder().id(1L).title("プラン").build();

        User user1 = User.builder().id(1L).username("testuser").email("test@example.com")
                .password("hashed").role(Role.USER).mealPlans(List.of(plan)).build();
        User user2 = User.builder().id(1L).username("testuser").email("test@example.com")
                .password("hashed").role(Role.USER).mealPlans(List.of()).build();

        // mealPlans が異なっても他フィールドが同じなら等価
        assertThat(user1).isEqualTo(user2);
        assertThat(user1.hashCode()).isEqualTo(user2.hashCode());
    }

    @Test
    @DisplayName("@EqualsAndHashCode - username が異なる場合は等価でないこと")
    void equalsAndHashCode_differentUsername_notEqual() {
        User user1 = buildUser(1L);
        User user2 = User.builder().id(1L).username("otheruser").email("test@example.com")
                .password("hashed_password").role(Role.USER).build();

        assertThat(user1).isNotEqualTo(user2);
    }

    @Test
    @DisplayName("@ToString(exclude = mealPlans) - toString に mealPlans が含まれないこと（循環参照防止）")
    void toString_excludesMealPlans() {
        User user = buildUser(1L);
        String str = user.toString();

        assertThat(str).doesNotContain("mealPlans=");
        assertThat(str).contains("username=testuser");
        assertThat(str).contains("email=test@example.com");
    }

    @Test
    @DisplayName("@ToString - toString にパスワードが含まれること（セキュリティ上の注意点確認）")
    void toString_containsPassword() {
        // パスワードが toString に含まれることを確認
        // 本番環境では @ToString(exclude = "password") の追加を検討すること
        User user = buildUser(1L);
        String str = user.toString();

        assertThat(str).contains("password=hashed_password");
    }

    @Test
    @DisplayName("Role enum - USER と ADMIN が定義されていること")
    void role_enumValues() {
        assertThat(Role.values()).containsExactlyInAnyOrder(Role.USER, Role.ADMIN);
    }
}
