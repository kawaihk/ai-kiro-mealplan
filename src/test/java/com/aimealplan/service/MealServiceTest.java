package com.aimealplan.service;

import com.aimealplan.model.Meal;
import com.aimealplan.repository.MealRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MealServiceTest {

    @Mock
    private MealRepository mealRepository;

    @InjectMocks
    private MealService mealService;

    private Meal testMeal;

    @BeforeEach
    void setUp() {
        testMeal = new Meal();
        testMeal.setUserId(1L);
        testMeal.setMealName("テストの朝食");
    }

    @Test
    @DisplayName("食事登録時にリポジトリの保存処理が呼ばれること")
    void registerMeal_ShouldSaveMeal() {
        when(mealRepository.save(any(Meal.class))).thenReturn(testMeal);

        Meal result = mealService.registerMeal(testMeal);

        verify(mealRepository, times(1)).save(testMeal);
        assertThat(result.getMealName()).isEqualTo("テストの朝食");
    }

    @Test
    @DisplayName("ユーザーIDを指定して食事一覧が取得できること")
    void getMealsByUserId_ShouldReturnList() {
        when(mealRepository.findByUserId(1L)).thenReturn(Collections.singletonList(testMeal));

        List<Meal> result = mealService.getMealsByUserId(1L);

        assertThat(result).hasSize(1);
        verify(mealRepository, times(1)).findByUserId(1L);
    }
}