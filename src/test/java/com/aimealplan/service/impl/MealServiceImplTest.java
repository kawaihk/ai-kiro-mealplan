package com.aimealplan.service.impl;

import com.aimealplan.entity.Meal;
import com.aimealplan.entity.MealPlan;
import com.aimealplan.exception.ResourceNotFoundException;
import com.aimealplan.model.MealDto;
import com.aimealplan.repository.MealPlanRepository;
import com.aimealplan.repository.MealRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MealServiceImplTest {

    @Mock
    private MealRepository mealRepository;

    @Mock
    private MealPlanRepository mealPlanRepository;

    @InjectMocks
    private MealServiceImpl mealService;

    private MealPlan mealPlan;
    private Meal meal;
    private MealDto mealDto;

    @BeforeEach
    void setUp() {
        mealPlan = MealPlan.builder().id(1L).title("週間プラン").build();
        meal = Meal.builder()
                .id(1L)
                .mealPlan(mealPlan)
                .mealDate(LocalDate.of(2026, 5, 4))
                .mealType(Meal.MealType.LUNCH)
                .recipeName("カレー")
                .calories(500)
                .protein(30)
                .build();
        mealDto = MealDto.builder()
                .mealPlanId(1L)
                .mealDate(LocalDate.of(2026, 5, 4))
                .mealType(Meal.MealType.LUNCH)
                .recipeName("カレー")
                .calories(500)
                .protein(30)
                .build();
    }

    // --- createMeal ---

    @Test
    @DisplayName("createMeal - 正常系: Meal を保存して DTO を返す")
    void createMeal_success() {
        when(mealPlanRepository.findById(1L)).thenReturn(Optional.of(mealPlan));
        when(mealRepository.save(any(Meal.class))).thenReturn(meal);

        MealDto result = mealService.createMeal(mealDto);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getRecipeName()).isEqualTo("カレー");
        assertThat(result.getCalories()).isEqualTo(500);
        assertThat(result.getProtein()).isEqualTo(30);
    }

    @Test
    @DisplayName("createMeal - 異常系: MealPlan不在で ResourceNotFoundException をスロー")
    void createMeal_mealPlanNotFound() {
        when(mealPlanRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> mealService.createMeal(mealDto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("MealPlan not found");
    }

    // --- getMealsByMealPlanId ---

    @Test
    @DisplayName("getMealsByMealPlanId - 正常系: Mealが1件以上の場合はリストを返す（existsById未呼び出し）")
    void getMealsByMealPlanId_withMeals() {
        when(mealRepository.findByMealPlanId(1L)).thenReturn(List.of(meal));

        List<MealDto> result = mealService.getMealsByMealPlanId(1L);

        assertThat(result).hasSize(1);
        verify(mealPlanRepository, never()).existsById(any());
    }

    @Test
    @DisplayName("getMealsByMealPlanId - 正常系: MealPlanが存在しMealが0件の場合は空リストを返す")
    void getMealsByMealPlanId_emptyMeals_mealPlanExists() {
        when(mealRepository.findByMealPlanId(1L)).thenReturn(Collections.emptyList());
        when(mealPlanRepository.existsById(1L)).thenReturn(true);

        List<MealDto> result = mealService.getMealsByMealPlanId(1L);

        assertThat(result).isEmpty();
        // existsById が呼ばれ、true を返した（MealPlan存在）ため例外がスローされないことを確認
        verify(mealPlanRepository).existsById(1L);
    }

    @Test
    @DisplayName("getMealsByMealPlanId - 異常系: MealPlanが存在しない場合は ResourceNotFoundException をスロー")
    void getMealsByMealPlanId_mealPlanNotFound() {
        when(mealRepository.findByMealPlanId(99L)).thenReturn(Collections.emptyList());
        when(mealPlanRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> mealService.getMealsByMealPlanId(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("MealPlan not found");
    }

    // --- getMealById ---

    @Test
    @DisplayName("getMealById - 正常系: mealPlanId が一致する場合は DTO を返す")
    void getMealById_success() {
        when(mealRepository.findById(1L)).thenReturn(Optional.of(meal));

        MealDto result = mealService.getMealById(1L, 1L);

        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("getMealById - 異常系: Meal不在で ResourceNotFoundException をスロー")
    void getMealById_mealNotFound() {
        when(mealRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> mealService.getMealById(1L, 99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Meal not found");
    }

    @Test
    @DisplayName("getMealById - 異常系: mealPlanId が不一致の場合は ResourceNotFoundException をスロー")
    void getMealById_wrongMealPlan() {
        when(mealRepository.findById(1L)).thenReturn(Optional.of(meal));

        assertThatThrownBy(() -> mealService.getMealById(99L, 1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("does not belong to MealPlan");
    }

    // --- updateMeal ---

    @Test
    @DisplayName("updateMeal - 正常系: Meal を更新して DTO を返す")
    void updateMeal_success() {
        MealDto updateDto = MealDto.builder()
                .mealDate(LocalDate.of(2026, 5, 5))
                .mealType(Meal.MealType.DINNER)
                .recipeName("ラーメン")
                .calories(700)
                .protein(25)
                .build();
        Meal updated = Meal.builder().id(1L).mealPlan(mealPlan).mealDate(LocalDate.of(2026, 5, 5))
                .mealType(Meal.MealType.DINNER).recipeName("ラーメン").calories(700).protein(25).build();

        when(mealRepository.findById(1L)).thenReturn(Optional.of(meal));
        when(mealRepository.save(any(Meal.class))).thenReturn(updated);

        MealDto result = mealService.updateMeal(1L, 1L, updateDto);

        assertThat(result.getRecipeName()).isEqualTo("ラーメン");
        assertThat(result.getCalories()).isEqualTo(700);
    }

    @Test
    @DisplayName("updateMeal - 異常系: mealPlanId が不一致の場合は ResourceNotFoundException をスロー")
    void updateMeal_wrongMealPlan() {
        when(mealRepository.findById(1L)).thenReturn(Optional.of(meal));

        assertThatThrownBy(() -> mealService.updateMeal(99L, 1L, mealDto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("does not belong to MealPlan");
    }

    // --- deleteMeal ---

    @Test
    @DisplayName("deleteMeal - 正常系: mealPlanId が一致する場合は削除を実行する")
    void deleteMeal_success() {
        when(mealRepository.findById(1L)).thenReturn(Optional.of(meal));

        mealService.deleteMeal(1L, 1L);

        verify(mealRepository).delete(meal);
    }

    @Test
    @DisplayName("deleteMeal - 異常系: mealPlanId が不一致の場合は ResourceNotFoundException をスロー")
    void deleteMeal_wrongMealPlan() {
        when(mealRepository.findById(1L)).thenReturn(Optional.of(meal));

        assertThatThrownBy(() -> mealService.deleteMeal(99L, 1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("does not belong to MealPlan");
    }

    @Test
    @DisplayName("deleteMeal - 異常系: Meal不在で ResourceNotFoundException をスロー")
    void deleteMeal_mealNotFound() {
        when(mealRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> mealService.deleteMeal(1L, 99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Meal not found");
    }
}
