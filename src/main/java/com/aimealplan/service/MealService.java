package com.aimealplan.service;

import com.aimealplan.model.MealDto;

import java.util.List;

public interface MealService {

    /**
     * 指定したミールプランにミールを登録します。
     *
     * @param mealDto 登録するMeal情報
     * @return 登録されたMealのDTO
     */
    MealDto createMeal(MealDto mealDto);

    /**
     * 指定したミールプランに紐づくMeal一覧を取得します。
     *
     * @param mealPlanId ミールプランID
     * @return Meal DTOのリスト
     */
    List<MealDto> getMealsByMealPlanId(Long mealPlanId);

    /**
     * 指定ミールプランに属するMealを取得します。
     *
     * @param mealPlanId ミールプランID
     * @param id MealのID
     * @return Meal DTO
     */
    MealDto getMealById(Long mealPlanId, Long id);

    /**
     * 指定ミールプランに属するMealを更新します。
     *
     * @param mealPlanId ミールプランID
     * @param id      更新対象のMeal ID
     * @param mealDto 更新内容
     * @return 更新後のMeal DTO
     */
    MealDto updateMeal(Long mealPlanId, Long id, MealDto mealDto);

    /**
     * 指定ミールプランに属するMealを削除します。
     *
     * @param mealPlanId ミールプランID
     * @param id 削除対象のMeal ID
     */
    void deleteMeal(Long mealPlanId, Long id);
}
