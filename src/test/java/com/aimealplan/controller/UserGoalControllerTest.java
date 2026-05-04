package com.aimealplan.controller;

import com.aimealplan.exception.ResourceNotFoundException;
import com.aimealplan.model.UserGoalDto;
import com.aimealplan.service.UserGoalService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserGoalController.class)
@WithMockUser
class UserGoalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserGoalService userGoalService;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String BASE_URL = "/api/users/1/goal";

    private UserGoalDto buildGoalDto() {
        UserGoalDto dto = new UserGoalDto();
        dto.setId(1L);
        dto.setUserId(1L);
        dto.setTargetCalories(2000);
        dto.setProteinRatio(30);
        dto.setFatRatio(30);
        dto.setCarbohydrateRatio(40);
        return dto;
    }

    // --- POST ---

    @Test
    @DisplayName("POST /api/users/{userId}/goal - 目標登録 正常系: 201 Created を返す")
    void createUserGoal_success() throws Exception {
        UserGoalDto request = buildGoalDto();
        when(userGoalService.createUserGoal(any(UserGoalDto.class))).thenReturn(buildGoalDto());

        mockMvc.perform(post(BASE_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.targetCalories").value(2000));
    }

    @Test
    @DisplayName("POST /api/users/{userId}/goal - 目標登録 異常系: PFC合計が100以外で 400 を返す")
    void createUserGoal_pfcValidationError() throws Exception {
        UserGoalDto request = new UserGoalDto();
        request.setUserId(1L);
        request.setProteinRatio(40);
        request.setFatRatio(40);
        request.setCarbohydrateRatio(40); // 合計120

        mockMvc.perform(post(BASE_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/users/{userId}/goal - 目標登録 異常系: ユーザー不在で 404 を返す")
    void createUserGoal_userNotFound() throws Exception {
        when(userGoalService.createUserGoal(any(UserGoalDto.class)))
                .thenThrow(new ResourceNotFoundException("User not found with id: 1"));

        mockMvc.perform(post(BASE_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(buildGoalDto())))
                .andExpect(status().isNotFound());
    }

    // --- GET ---

    @Test
    @DisplayName("GET /api/users/{userId}/goal - 目標取得 正常系: 200 OK を返す")
    void getUserGoal_success() throws Exception {
        when(userGoalService.getUserGoalByUserId(1L)).thenReturn(Optional.of(buildGoalDto()));

        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.targetCalories").value(2000));
    }

    @Test
    @DisplayName("GET /api/users/{userId}/goal - 目標取得 異常系: 未設定の場合 404 を返す")
    void getUserGoal_notFound() throws Exception {
        when(userGoalService.getUserGoalByUserId(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isNotFound());
    }

    // --- PUT ---

    @Test
    @DisplayName("PUT /api/users/{userId}/goal - 目標更新 正常系: 200 OK を返す")
    void updateUserGoal_success() throws Exception {
        when(userGoalService.updateUserGoal(eq(1L), any(UserGoalDto.class))).thenReturn(buildGoalDto());

        mockMvc.perform(put(BASE_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(buildGoalDto())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.targetCalories").value(2000));
    }

    @Test
    @DisplayName("PUT /api/users/{userId}/goal - 目標更新 異常系: 目標未設定で 404 を返す")
    void updateUserGoal_notFound() throws Exception {
        when(userGoalService.updateUserGoal(eq(1L), any(UserGoalDto.class)))
                .thenThrow(new ResourceNotFoundException("UserGoal not found for userId: 1"));

        mockMvc.perform(put(BASE_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(buildGoalDto())))
                .andExpect(status().isNotFound());
    }

    // --- DELETE ---

    @Test
    @DisplayName("DELETE /api/users/{userId}/goal - 目標削除 正常系: 204 No Content を返す")
    void deleteUserGoal_success() throws Exception {
        doNothing().when(userGoalService).deleteUserGoal(1L);

        mockMvc.perform(delete(BASE_URL).with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /api/users/{userId}/goal - 目標削除 異常系: 目標未設定で 404 を返す")
    void deleteUserGoal_notFound() throws Exception {
        doThrow(new ResourceNotFoundException("UserGoal not found for userId: 1"))
                .when(userGoalService).deleteUserGoal(1L);

        mockMvc.perform(delete(BASE_URL).with(csrf()))
                .andExpect(status().isNotFound());
    }
}
