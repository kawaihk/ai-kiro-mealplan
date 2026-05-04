package com.aimealplan.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class UserGoalDtoTest {

    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    private UserGoalDto validDto() {
        UserGoalDto dto = new UserGoalDto();
        dto.setUserId(1L);
        dto.setTargetCalories(2000);
        dto.setProteinRatio(30);
        dto.setFatRatio(30);
        dto.setCarbohydrateRatio(40);
        return dto;
    }

    // --- userId (@NotNull) ---

    @Test
    @DisplayName("userId - 正常系: 有効な値でバリデーションエラーなし")
    void userId_valid() {
        assertThat(validator.validate(validDto())).isEmpty();
    }

    @Test
    @DisplayName("userId - 異常系: null でバリデーションエラーが発生する")
    void userId_null() {
        UserGoalDto dto = validDto();
        dto.setUserId(null);

        Set<ConstraintViolation<UserGoalDto>> violations = validator.validate(dto);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getPropertyPath().toString()).isEqualTo("userId");
    }

    // --- proteinRatio / fatRatio / carbohydrateRatio (@Min(0) @Max(100)) ---

    @Test
    @DisplayName("proteinRatio - 異常系: 0未満でバリデーションエラーが発生する")
    void proteinRatio_belowMin() {
        UserGoalDto dto = validDto();
        dto.setProteinRatio(-1);

        Set<ConstraintViolation<UserGoalDto>> violations = validator.validate(dto);

        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("proteinRatio"));
    }

    @Test
    @DisplayName("proteinRatio - 異常系: 100超でバリデーションエラーが発生する")
    void proteinRatio_aboveMax() {
        UserGoalDto dto = validDto();
        dto.setProteinRatio(101);

        Set<ConstraintViolation<UserGoalDto>> violations = validator.validate(dto);

        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("proteinRatio"));
    }

    @Test
    @DisplayName("fatRatio - 異常系: 負の値でバリデーションエラーが発生する")
    void fatRatio_negative() {
        UserGoalDto dto = validDto();
        dto.setFatRatio(-1);

        Set<ConstraintViolation<UserGoalDto>> violations = validator.validate(dto);

        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("fatRatio"));
    }

    @Test
    @DisplayName("carbohydrateRatio - 異常系: 100超でバリデーションエラーが発生する")
    void carbohydrateRatio_aboveMax() {
        UserGoalDto dto = validDto();
        dto.setCarbohydrateRatio(101);

        Set<ConstraintViolation<UserGoalDto>> violations = validator.validate(dto);

        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("carbohydrateRatio"));
    }

    // --- @PfcRatioSum ---

    @Test
    @DisplayName("@PfcRatioSum - 正常系: PFC合計が100でバリデーションエラーなし")
    void pfcRatioSum_valid() {
        assertThat(validator.validate(validDto())).isEmpty();
    }

    @Test
    @DisplayName("@PfcRatioSum - 正常系: PFC全フィールドが null（未設定）でバリデーションエラーなし")
    void pfcRatioSum_allNull() {
        UserGoalDto dto = new UserGoalDto();
        dto.setUserId(1L);

        assertThat(validator.validate(dto)).isEmpty();
    }

    @Test
    @DisplayName("@PfcRatioSum - 異常系: PFC合計が100でない場合にバリデーションエラーが発生する")
    void pfcRatioSum_invalid() {
        UserGoalDto dto = validDto();
        dto.setProteinRatio(20); // 合計 20+30+40=90

        Set<ConstraintViolation<UserGoalDto>> violations = validator.validate(dto);

        assertThat(violations).anyMatch(v ->
                v.getConstraintDescriptor().getAnnotation().annotationType().getSimpleName()
                        .equals("PfcRatioSum"));
    }

    @Test
    @DisplayName("@PfcRatioSum - 異常系: 一部のみ設定で合計が100でない場合にバリデーションエラーが発生する")
    void pfcRatioSum_partialSet_invalid() {
        UserGoalDto dto = new UserGoalDto();
        dto.setUserId(1L);
        dto.setProteinRatio(50); // fat・carb は null(0) → 合計50

        Set<ConstraintViolation<UserGoalDto>> violations = validator.validate(dto);

        assertThat(violations).anyMatch(v ->
                v.getConstraintDescriptor().getAnnotation().annotationType().getSimpleName()
                        .equals("PfcRatioSum"));
    }
}
