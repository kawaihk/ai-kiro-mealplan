package com.aimealplan.model;

import com.aimealplan.entity.Meal;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MealDto {
    private Long id;
    private Long mealPlanId;
    @NotNull
    private LocalDate mealDate;
    private Meal.MealType mealType; // BREAKFAST, LUNCH, DINNER, SNACK
    private String recipeName;
    private String instructions;
    @PositiveOrZero
    private Integer calories;
    @PositiveOrZero
    private Integer protein;
}