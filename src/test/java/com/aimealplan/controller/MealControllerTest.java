package com.aimealplan.controller;

import com.aimealplan.service.MealService;
import com.aimealplan.model.Meal;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MealController.class)
public class MealControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MealService mealService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("POST /api/v1/meals - 食事データの登録")
    void registerMeal_Success() throws Exception {
        Meal meal = new Meal();
        meal.setUserId(1L);
        meal.setMealName("昼食");
        meal.setProtein(25);

        when(mealService.registerMeal(any(Meal.class))).thenReturn(meal);

        mockMvc.perform(post("/api/v1/meals")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(meal)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mealName").value("昼食"));
    }

    @Test
    @DisplayName("GET /api/v1/meals - 指定したユーザーの食事一覧取得")
    void getMeals_Success() throws Exception {
        Meal meal = new Meal();
        meal.setMealName("朝食");

        when(mealService.getMealsByUserId(1L)).thenReturn(Collections.singletonList(meal));

        mockMvc.perform(get("/api/v1/meals").param("userId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].mealName").value("朝食"));
    }
}