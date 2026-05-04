package com.aimealplan.service;

import com.aimealplan.entity.Meal;
import com.aimealplan.model.MealDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * {@link MealService} インターフェースのコントラクトテスト。
 * 実装の詳細なロジック検証は {@link com.aimealplan.service.impl.MealServiceImplTest} で行う。
 */
@ExtendWith(MockitoExtension.class)
class MealServiceTest {

    @Mock
    private MealService mealService;

    private MealDto buildMealDto(Long id) {
        return MealDto.builder()
                .id(id)
                .mealPlanId(1L)
                .mealDate(LocalDate.of(2026, 5, 4))
                .mealType(Meal.MealType.LUNCH)
                .recipeName("カレー")
                .calories(500)
                .protein(30)
                .build();
    }

    @Test
    @DisplayName("createMeal - インターフェースのメソッドが呼び出せること")
    void createMeal_interfaceContract() {
        MealDto dto = buildMealDto(null);
        MealDto expected = buildMealDto(1L);
        when(mealService.createMeal(dto)).thenReturn(expected);

        MealDto result = mealService.createMeal(dto);

        assertThat(result.getId()).isEqualTo(1L);
        verify(mealService).createMeal(dto);
    }

    @Test
    @DisplayName("getMealsByMealPlanId - インターフェースのメソッドが呼び出せること")
    void getMealsByMealPlanId_interfaceContract() {
        when(mealService.getMealsByMealPlanId(1L)).thenReturn(List.of(buildMealDto(1L)));

        List<MealDto> result = mealService.getMealsByMealPlanId(1L);

        assertThat(result).hasSize(1);
        verify(mealService).getMealsByMealPlanId(1L);
    }

    @Test
    @DisplayName("getMealById - インターフェースのメソッドが呼び出せること")
    void getMealById_interfaceContract() {
        when(mealService.getMealById(1L, 1L)).thenReturn(buildMealDto(1L));

        MealDto result = mealService.getMealById(1L, 1L);

        assertThat(result.getId()).isEqualTo(1L);
        verify(mealService).getMealById(1L, 1L);
    }

    @Test
    @DisplayName("updateMeal - インターフェースのメソッドが呼び出せること")
    void updateMeal_interfaceContract() {
        MealDto dto = buildMealDto(null);
        MealDto expected = buildMealDto(1L);
        when(mealService.updateMeal(1L, 1L, dto)).thenReturn(expected);

        MealDto result = mealService.updateMeal(1L, 1L, dto);

        assertThat(result.getId()).isEqualTo(1L);
        verify(mealService).updateMeal(1L, 1L, dto);
    }

    @Test
    @DisplayName("deleteMeal - インターフェースのメソッドが呼び出せること")
    void deleteMeal_interfaceContract() {
        doNothing().when(mealService).deleteMeal(1L, 1L);

        mealService.deleteMeal(1L, 1L);

        verify(mealService).deleteMeal(1L, 1L);
    }
}
