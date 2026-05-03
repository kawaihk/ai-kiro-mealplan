package com.aimealplan.controller;

import com.aimealplan.service.UserGoalService;
import com.aimealplan.model.UserGoal;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserGoalController.class)
public class UserGoalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserGoalService userGoalService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("POST /api/v1/goals - PFC比率の合計が100%なら成功すること")
    void saveGoal_Success_WhenPfcSumIs100() throws Exception {
        UserGoal goal = new UserGoal();
        goal.setUserId(1L);
        goal.setProteinRatio(20);
        goal.setFatRatio(30);
        goal.setCarbohydrateRatio(50);

        when(userGoalService.saveGoal(any(UserGoal.class))).thenReturn(goal);

        mockMvc.perform(post("/api/v1/goals")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(goal)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST /api/v1/goals - PFC比率の合計が100%でないなら400エラーになること")
    void saveGoal_Fail_WhenPfcSumIsNot100() throws Exception {
        UserGoal invalidGoal = new UserGoal();
        invalidGoal.setUserId(1L);
        invalidGoal.setProteinRatio(20);
        invalidGoal.setFatRatio(20);
        invalidGoal.setCarbohydrateRatio(20); // 合計60%

        // Note: Controllerに @Valid または @Validated が付与されていることが前提
        mockMvc.perform(post("/api/v1/goals")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidGoal)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/v1/goals - 負の値を設定した場合はバリデーションエラーになること")
    void saveGoal_Fail_WhenNegativeValue() throws Exception {
        mockMvc.perform(post("/api/v1/goals")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"userId\":1, \"proteinRatio\":-10, \"fatRatio\":10, \"carbohydrateRatio\":10}"))
                .andExpect(status().isBadRequest());
    }
}