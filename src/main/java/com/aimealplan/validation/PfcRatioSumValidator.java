package com.aimealplan.validation;

import com.aimealplan.model.UserGoalDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PfcRatioSumValidator implements ConstraintValidator<PfcRatioSum, UserGoalDto> {
    @Override
    public boolean isValid(UserGoalDto goal, ConstraintValidatorContext context) {
        if (goal == null) {
            return true;
        }
        Integer p = goal.getProteinRatio();
        Integer f = goal.getFatRatio();
        Integer c = goal.getCarbohydrateRatio();

        // 全フィールドが null の場合は未設定として許容する
        if (p == null && f == null && c == null) {
            return true;
        }

        // 一部でも設定されている場合は合計が100であることを検証する
        int sum = (p != null ? p : 0) + (f != null ? f : 0) + (c != null ? c : 0);
        return sum == 100;
    }
}