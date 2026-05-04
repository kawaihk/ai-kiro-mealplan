package com.aimealplan.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class MealTest {

    private Meal buildMeal(Long id) {
        MealPlan mealPlan = MealPlan.builder().id(1L).title("週間プラン").build();
        return Meal.builder()
                .id(id)
                .mealPlan(mealPlan)
                .mealDate(LocalDate.of(2026, 5, 4))
                .mealType(Meal.MealType.LUNCH)
                .recipeName("カレー")
                .calories(500)
                .protein(30)
                .build();
    }

    @Test
    @DisplayName("@Builder - 全フィールドを指定してオブジェクトを生成できること")
    void builder_allFields() {
        Meal meal = buildMeal(1L);

        assertThat(meal.getId()).isEqualTo(1L);
        assertThat(meal.getMealDate()).isEqualTo(LocalDate.of(2026, 5, 4));
        assertThat(meal.getMealType()).isEqualTo(Meal.MealType.LUNCH);
        assertThat(meal.getRecipeName()).isEqualTo("カレー");
        assertThat(meal.getCalories()).isEqualTo(500);
        assertThat(meal.getProtein()).isEqualTo(30);
    }

    @Test
    @DisplayName("@NoArgsConstructor - 引数なしコンストラクタでオブジェクトを生成できること")
    void noArgsConstructor() {
        Meal meal = new Meal();
        assertThat(meal).isNotNull();
    }

    @Test
    @DisplayName("@Getter / @Setter - フィールドの取得・設定ができること")
    void getterSetter() {
        Meal meal = new Meal();
        meal.setRecipeName("ラーメン");
        meal.setCalories(600);

        assertThat(meal.getRecipeName()).isEqualTo("ラーメン");
        assertThat(meal.getCalories()).isEqualTo(600);
    }

    @Test
    @DisplayName("@EqualsAndHashCode(exclude = mealPlan) - mealPlan を除くフィールドで等価比較されること")
    void equalsAndHashCode_excludesMealPlan() {
        MealPlan plan1 = MealPlan.builder().id(1L).build();
        MealPlan plan2 = MealPlan.builder().id(2L).build();

        Meal meal1 = Meal.builder().id(1L).mealPlan(plan1).recipeName("カレー").build();
        Meal meal2 = Meal.builder().id(1L).mealPlan(plan2).recipeName("カレー").build();

        // mealPlan が異なっても id・recipeName が同じなら等価
        assertThat(meal1).isEqualTo(meal2);
        assertThat(meal1.hashCode()).isEqualTo(meal2.hashCode());
    }

    @Test
    @DisplayName("@ToString(exclude = mealPlan) - toString に mealPlan が含まれないこと（循環参照防止）")
    void toString_excludesMealPlan() {
        Meal meal = buildMeal(1L);
        String str = meal.toString();

        assertThat(str).doesNotContain("mealPlan=MealPlan");
        assertThat(str).contains("recipeName=カレー");
    }

    @Test
    @DisplayName("MealType enum - 全値が定義されていること")
    void mealType_enumValues() {
        assertThat(Meal.MealType.values()).containsExactlyInAnyOrder(
                Meal.MealType.BREAKFAST,
                Meal.MealType.LUNCH,
                Meal.MealType.DINNER,
                Meal.MealType.SNACK
        );
    }
}
