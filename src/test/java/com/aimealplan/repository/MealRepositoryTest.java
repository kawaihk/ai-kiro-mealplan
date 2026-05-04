package com.aimealplan.repository;

import com.aimealplan.entity.Meal;
import com.aimealplan.entity.MealPlan;
import com.aimealplan.entity.Role;
import com.aimealplan.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class MealRepositoryTest {

    @Autowired
    private MealRepository mealRepository;

    @Autowired
    private MealPlanRepository mealPlanRepository;

    @Autowired
    private UserRepository userRepository;

    private MealPlan mealPlan;
    private MealPlan otherMealPlan;

    @BeforeEach
    void setUp() {
        User user = userRepository.save(User.builder()
                .username("testuser")
                .email("test@example.com")
                .password("hashed")
                .role(Role.USER)
                .build());

        mealPlan = mealPlanRepository.save(MealPlan.builder()
                .user(user)
                .title("週間プラン")
                .build());

        otherMealPlan = mealPlanRepository.save(MealPlan.builder()
                .user(user)
                .title("別プラン")
                .build());

        // mealPlan に2件登録
        mealRepository.save(Meal.builder()
                .mealPlan(mealPlan)
                .mealDate(LocalDate.of(2026, 5, 4))
                .mealType(Meal.MealType.BREAKFAST)
                .recipeName("トースト")
                .calories(300)
                .build());

        mealRepository.save(Meal.builder()
                .mealPlan(mealPlan)
                .mealDate(LocalDate.of(2026, 5, 4))
                .mealType(Meal.MealType.LUNCH)
                .recipeName("カレー")
                .calories(700)
                .build());

        // otherMealPlan に1件登録
        mealRepository.save(Meal.builder()
                .mealPlan(otherMealPlan)
                .mealDate(LocalDate.of(2026, 5, 4))
                .mealType(Meal.MealType.DINNER)
                .recipeName("ラーメン")
                .calories(600)
                .build());
    }

    // --- findByMealPlanId ---

    @Test
    @DisplayName("findByMealPlanId - 正常系: 指定した MealPlan に紐づく Meal のみを返す")
    void findByMealPlanId_returnsOnlyMatchingMeals() {
        List<Meal> result = mealRepository.findByMealPlanId(mealPlan.getId());

        assertThat(result).hasSize(2);
        assertThat(result).extracting(Meal::getRecipeName)
                .containsExactlyInAnyOrder("トースト", "カレー");
    }

    @Test
    @DisplayName("findByMealPlanId - 正常系: 別の MealPlan の Meal は含まれない")
    void findByMealPlanId_doesNotReturnOtherMealPlanMeals() {
        List<Meal> result = mealRepository.findByMealPlanId(mealPlan.getId());

        assertThat(result).extracting(Meal::getRecipeName)
                .doesNotContain("ラーメン");
    }

    @Test
    @DisplayName("findByMealPlanId - 正常系: Meal が0件の MealPlan では空リストを返す")
    void findByMealPlanId_emptyWhenNoMeals() {
        User user2 = userRepository.save(User.builder()
                .username("user2")
                .email("user2@example.com")
                .password("hashed")
                .role(Role.USER)
                .build());
        MealPlan emptyPlan = mealPlanRepository.save(MealPlan.builder()
                .user(user2)
                .title("空プラン")
                .build());

        List<Meal> result = mealRepository.findByMealPlanId(emptyPlan.getId());

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("findByMealPlanId - 異常系: 存在しない mealPlanId では空リストを返す")
    void findByMealPlanId_notExistingId_returnsEmpty() {
        List<Meal> result = mealRepository.findByMealPlanId(999L);

        assertThat(result).isEmpty();
    }
}
