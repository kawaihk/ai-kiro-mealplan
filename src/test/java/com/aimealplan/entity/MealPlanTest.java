package com.aimealplan.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class MealPlanTest {

    private MealPlan buildMealPlan(Long id) {
        User user = User.builder().id(1L).username("testuser").build();
        return MealPlan.builder()
                .id(id)
                .user(user)
                .title("週間プラン")
                .goal("ダイエット")
                .build();
    }

    @Test
    @DisplayName("@Builder - 全フィールドを指定してオブジェクトを生成できること")
    void builder_allFields() {
        MealPlan mealPlan = buildMealPlan(1L);

        assertThat(mealPlan.getId()).isEqualTo(1L);
        assertThat(mealPlan.getTitle()).isEqualTo("週間プラン");
        assertThat(mealPlan.getGoal()).isEqualTo("ダイエット");
        assertThat(mealPlan.getUser().getUsername()).isEqualTo("testuser");
    }

    @Test
    @DisplayName("@NoArgsConstructor - 引数なしコンストラクタでオブジェクトを生成できること")
    void noArgsConstructor() {
        MealPlan mealPlan = new MealPlan();
        assertThat(mealPlan).isNotNull();
    }

    @Test
    @DisplayName("@Getter / @Setter - フィールドの取得・設定ができること")
    void getterSetter() {
        MealPlan mealPlan = new MealPlan();
        mealPlan.setTitle("テストプラン");
        mealPlan.setGoal("筋肥大");

        assertThat(mealPlan.getTitle()).isEqualTo("テストプラン");
        assertThat(mealPlan.getGoal()).isEqualTo("筋肥大");
    }

    @Test
    @DisplayName("@EqualsAndHashCode(exclude = {user, meals}) - user・meals を除くフィールドで等価比較されること")
    void equalsAndHashCode_excludesUserAndMeals() {
        User user1 = User.builder().id(1L).username("user1").build();
        User user2 = User.builder().id(2L).username("user2").build();
        Meal meal = Meal.builder().id(1L).recipeName("カレー").build();

        MealPlan plan1 = MealPlan.builder().id(1L).user(user1).title("プラン").meals(List.of(meal)).build();
        MealPlan plan2 = MealPlan.builder().id(1L).user(user2).title("プラン").meals(List.of()).build();

        // user・meals が異なっても id・title が同じなら等価
        assertThat(plan1).isEqualTo(plan2);
        assertThat(plan1.hashCode()).isEqualTo(plan2.hashCode());
    }

    @Test
    @DisplayName("@ToString(exclude = {user, meals}) - toString に user・meals が含まれないこと（循環参照防止）")
    void toString_excludesUserAndMeals() {
        MealPlan mealPlan = buildMealPlan(1L);
        String str = mealPlan.toString();

        assertThat(str).doesNotContain("user=User");
        assertThat(str).doesNotContain("meals=");
        assertThat(str).contains("title=週間プラン");
    }
}
