package com.aimealplan.model;

import com.aimealplan.entity.Meal;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class MealDtoTest {

    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    private MealDto validDto() {
        return MealDto.builder()
                .mealPlanId(1L)
                .mealDate(LocalDate.of(2026, 5, 4))
                .mealType(Meal.MealType.BREAKFAST)
                .recipeName("テスト料理")
                .calories(500)
                .protein(30)
                .build();
    }

    // --- mealDate ---

    @Test
    @DisplayName("mealDate - 正常系: 有効な日付でバリデーションエラーなし")
    void mealDate_valid() {
        Set<ConstraintViolation<MealDto>> violations = validator.validate(validDto());
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("mealDate - 異常系: null でバリデーションエラーが発生する")
    void mealDate_null() {
        MealDto dto = validDto();
        dto.setMealDate(null);

        Set<ConstraintViolation<MealDto>> violations = validator.validate(dto);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getPropertyPath().toString()).isEqualTo("mealDate");
    }

    // --- calories ---

    @Test
    @DisplayName("calories - 正常系: 0 でバリデーションエラーなし")
    void calories_zero() {
        MealDto dto = validDto();
        dto.setCalories(0);

        assertThat(validator.validate(dto)).isEmpty();
    }

    @Test
    @DisplayName("calories - 異常系: 負の値でバリデーションエラーが発生する")
    void calories_negative() {
        MealDto dto = validDto();
        dto.setCalories(-1);

        Set<ConstraintViolation<MealDto>> violations = validator.validate(dto);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getPropertyPath().toString()).isEqualTo("calories");
    }

    @Test
    @DisplayName("calories - 正常系: null でバリデーションエラーなし（任意項目）")
    void calories_null() {
        MealDto dto = validDto();
        dto.setCalories(null);

        assertThat(validator.validate(dto)).isEmpty();
    }

    // --- protein ---

    @Test
    @DisplayName("protein - 異常系: 負の値でバリデーションエラーが発生する")
    void protein_negative() {
        MealDto dto = validDto();
        dto.setProtein(-1);

        Set<ConstraintViolation<MealDto>> violations = validator.validate(dto);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getPropertyPath().toString()).isEqualTo("protein");
    }

    @Test
    @DisplayName("protein - 正常系: null でバリデーションエラーなし（任意項目）")
    void protein_null() {
        MealDto dto = validDto();
        dto.setProtein(null);

        assertThat(validator.validate(dto)).isEmpty();
    }
}
