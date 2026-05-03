package com.aimealplan.model;

import com.aimealplan.validation.PfcRatioSum;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * ユーザーの目標栄養素設定を転送するためのDTO。
 * (旧 UserGoal.java から Entity 責務を分離)
 */
@PfcRatioSum
@Data
public class UserGoalDto {
    private Long id;
    @NotNull
    private Long userId;
    private Double targetWeight;
    private String activityLevel;
    private Integer targetCalories;
    @Min(0) @Max(100) private Integer proteinRatio;
    @Min(0) @Max(100) private Integer fatRatio;
    @Min(0) @Max(100) private Integer carbohydrateRatio;
}
