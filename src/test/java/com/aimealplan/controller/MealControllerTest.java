package com.aimealplan.controller;

import com.aimealplan.entity.Meal;
import com.aimealplan.exception.ResourceNotFoundException;
import com.aimealplan.model.MealDto;
import com.aimealplan.service.MealService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MealController.class)
@WithMockUser
class MealControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MealService mealService;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String BASE_URL = "/api/meal-plans/1/meals";

    private MealDto buildMealDto(Long id) {
        return MealDto.builder()
                .id(id)
                .mealPlanId(1L)
                .mealDate(LocalDate.of(2026, 5, 4))
                .mealType(Meal.MealType.LUNCH)
                .recipeName("チキンカレー")
                .calories(500)
                .protein(30)
                .build();
    }

    // --- POST ---

    @Test
    @DisplayName("POST /api/meal-plans/{mealPlanId}/meals - Meal登録 正常系: 201 Created を返す")
    void createMeal_success() throws Exception {
        MealDto request = buildMealDto(null);
        MealDto response = buildMealDto(1L);
        when(mealService.createMeal(any(MealDto.class))).thenReturn(response);

        mockMvc.perform(post(BASE_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.recipeName").value("チキンカレー"));
    }

    @Test
    @DisplayName("POST /api/meal-plans/{mealPlanId}/meals - Meal登録 異常系: mealDate未入力で 400 を返す")
    void createMeal_validationError() throws Exception {
        MealDto request = MealDto.builder().mealPlanId(1L).recipeName("カレー").build(); // mealDate null

        mockMvc.perform(post(BASE_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    // --- GET 一覧 ---

    @Test
    @DisplayName("GET /api/meal-plans/{mealPlanId}/meals - Meal一覧取得 正常系: 200 OK を返す")
    void getMealsByMealPlanId_success() throws Exception {
        when(mealService.getMealsByMealPlanId(1L)).thenReturn(List.of(buildMealDto(1L)));

        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @DisplayName("GET /api/meal-plans/{mealPlanId}/meals - Meal一覧取得 異常系: MealPlan不在で 404 を返す")
    void getMealsByMealPlanId_notFound() throws Exception {
        when(mealService.getMealsByMealPlanId(99L))
                .thenThrow(new ResourceNotFoundException("MealPlan not found with id: 99"));

        mockMvc.perform(get("/api/meal-plans/99/meals"))
                .andExpect(status().isNotFound());
    }

    // --- GET 単件 ---

    @Test
    @DisplayName("GET /api/meal-plans/{mealPlanId}/meals/{id} - Meal取得 正常系: 200 OK を返す")
    void getMealById_success() throws Exception {
        when(mealService.getMealById(1L, 1L)).thenReturn(buildMealDto(1L));

        mockMvc.perform(get(BASE_URL + "/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    @DisplayName("GET /api/meal-plans/{mealPlanId}/meals/{id} - Meal取得 異常系: 別MealPlanのMealで 404 を返す")
    void getMealById_wrongMealPlan() throws Exception {
        when(mealService.getMealById(1L, 99L))
                .thenThrow(new ResourceNotFoundException("Meal with id: 99 does not belong to MealPlan with id: 1"));

        mockMvc.perform(get(BASE_URL + "/99"))
                .andExpect(status().isNotFound());
    }

    // --- PUT ---

    @Test
    @DisplayName("PUT /api/meal-plans/{mealPlanId}/meals/{id} - Meal更新 正常系: 200 OK を返す")
    void updateMeal_success() throws Exception {
        MealDto request = buildMealDto(null);
        MealDto response = buildMealDto(1L);
        when(mealService.updateMeal(eq(1L), eq(1L), any(MealDto.class))).thenReturn(response);

        mockMvc.perform(put(BASE_URL + "/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    @DisplayName("PUT /api/meal-plans/{mealPlanId}/meals/{id} - Meal更新 異常系: 別MealPlanのMealで 404 を返す")
    void updateMeal_wrongMealPlan() throws Exception {
        MealDto request = buildMealDto(null);
        when(mealService.updateMeal(eq(1L), eq(99L), any(MealDto.class)))
                .thenThrow(new ResourceNotFoundException("Meal with id: 99 does not belong to MealPlan with id: 1"));

        mockMvc.perform(put(BASE_URL + "/99")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    // --- DELETE ---

    @Test
    @DisplayName("DELETE /api/meal-plans/{mealPlanId}/meals/{id} - Meal削除 正常系: 204 No Content を返す")
    void deleteMeal_success() throws Exception {
        doNothing().when(mealService).deleteMeal(1L, 1L);

        mockMvc.perform(delete(BASE_URL + "/1").with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /api/meal-plans/{mealPlanId}/meals/{id} - Meal削除 異常系: 別MealPlanのMealで 404 を返す")
    void deleteMeal_wrongMealPlan() throws Exception {
        doThrow(new ResourceNotFoundException("Meal with id: 99 does not belong to MealPlan with id: 1"))
                .when(mealService).deleteMeal(1L, 99L);

        mockMvc.perform(delete(BASE_URL + "/99").with(csrf()))
                .andExpect(status().isNotFound());
    }
}
