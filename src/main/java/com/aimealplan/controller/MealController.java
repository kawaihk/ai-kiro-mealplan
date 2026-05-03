package com.aimealplan.controller;

import com.aimealplan.model.MealDto;
import com.aimealplan.service.MealService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/meal-plans/{mealPlanId}/meals")
@RequiredArgsConstructor
public class MealController {

    private final MealService mealService;

    /**
     * 指定ミールプランにMealを登録します。
     */
    @PostMapping
    public ResponseEntity<MealDto> createMeal(
            @PathVariable Long mealPlanId,
            @Valid @RequestBody MealDto mealDto) {
        mealDto.setMealPlanId(mealPlanId);
        MealDto created = mealService.createMeal(mealDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * 指定ミールプランに紐づくMeal一覧を取得します。
     */
    @GetMapping
    public ResponseEntity<List<MealDto>> getMealsByMealPlanId(@PathVariable Long mealPlanId) {
        return ResponseEntity.ok(mealService.getMealsByMealPlanId(mealPlanId));
    }

    /**
     * 指定IDのMealを取得します。
     */
    @GetMapping("/{id}")
    public ResponseEntity<MealDto> getMealById(
            @PathVariable Long mealPlanId,
            @PathVariable Long id) {
        return ResponseEntity.ok(mealService.getMealById(mealPlanId, id));
    }

    /**
     * 指定IDのMealを更新します。
     */
    @PutMapping("/{id}")
    public ResponseEntity<MealDto> updateMeal(
            @PathVariable Long mealPlanId,
            @PathVariable Long id,
            @Valid @RequestBody MealDto mealDto) {
        mealDto.setMealPlanId(mealPlanId);
        return ResponseEntity.ok(mealService.updateMeal(mealPlanId, id, mealDto));
    }

    /**
     * 指定IDのMealを削除します。
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMeal(
            @PathVariable Long mealPlanId,
            @PathVariable Long id) {
        mealService.deleteMeal(mealPlanId, id);
        return ResponseEntity.noContent().build();
    }
}
